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
import jp.co.kccs.xhd.common.ErrorListMessage;
import jp.co.kccs.xhd.common.InitMessage;
import jp.co.kccs.xhd.common.KikakuError;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.db.model.SrRsusprn;
import jp.co.kccs.xhd.db.model.SubSrRsusprn;
import jp.co.kccs.xhd.model.GXHDO101C004Model;
import jp.co.kccs.xhd.model.GXHDO101C005Model;
import jp.co.kccs.xhd.model.GXHDO101C020Model;
import jp.co.kccs.xhd.pxhdo901.ErrorMessageInfo;
import jp.co.kccs.xhd.pxhdo901.GXHDO901A;
import jp.co.kccs.xhd.pxhdo901.IFormLogic;
import jp.co.kccs.xhd.pxhdo901.KikakuchiInputErrorInfo;
import jp.co.kccs.xhd.pxhdo901.ProcessData;
import jp.co.kccs.xhd.util.CommonUtil;
import jp.co.kccs.xhd.util.DBUtil;
import jp.co.kccs.xhd.util.DateUtil;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.NumberUtil;
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
import org.apache.commons.lang3.StringUtils;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/01/17<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * 変更日	2019/09/19<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	項目追加・変更<br>
 * <br>
 * 変更日	2020/09/16<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	863 zhangjinyan<br>
 * 変更理由	仕様変更<br>
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
 * 変更日	2020/11/20<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	863 zhangjy<br>
 * 変更理由	フロー用仮登録の関連処理を追加<br>
 * <br>
 * 変更日       2022/03/09<br>
 * 計画書No     MB2202-D013<br>
 * 変更者       KCSS WXF<br>
 * 変更理由     仕様変更対応<br>
 * <br>
 * 変更日	2022/06/14<br>
 * 計画書No	MB2205-D010<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	画面表示項目を追加<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101B003(印刷RSUS)ロジック
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2019/01/17
 */
public class GXHDO101B003 implements IFormLogic {

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
            processData.setNoCheckButtonId(Arrays.asList(GXHDO101B003Const.BTN_MAKUATSU_SUB_GAMEN_TOP,
                    GXHDO101B003Const.BTN_INSATSUHABA_SUB_GAMEN_TOP,
                    GXHDO101B003Const.BTN_STARTDATETIME_TOP,
                    GXHDO101B003Const.BTN_ENDDATETIME_TOP,
                    GXHDO101B003Const.BTN_MAKUATSU_SUB_GAMEN_BUTTOM,
                    GXHDO101B003Const.BTN_INSATSUHABA_SUB_GAMEN_BUTTOM,
                    GXHDO101B003Const.BTN_STARTDATETIME_BUTTOM,
                    GXHDO101B003Const.BTN_ENDDATETIME_BUTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(GXHDO101B003Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO101B003Const.BTN_INSERT_TOP,
                    GXHDO101B003Const.BTN_DELETE_TOP,
                    GXHDO101B003Const.BTN_UPDATE_TOP,
                    GXHDO101B003Const.BTN_KARI_TOUROKU_BUTTOM,
                    GXHDO101B003Const.BTN_INSERT_BUTTOM,
                    GXHDO101B003Const.BTN_DELETE_BUTTOM,
                    GXHDO101B003Const.BTN_UPDATE_BUTTOM));

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

            processData.setMethod("");
            // コールバックパラメータにてサブ画面起動用の値を設定
            processData.setCollBackParam("gxhdo101c004");

            // 膜厚(RSUS)の現在の値をサブ画面の表示用の値に渡す
            GXHDO101C004 beanGXHDO101C004 = (GXHDO101C004) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C004);
            beanGXHDO101C004.setGxhdO101c004ModelView(beanGXHDO101C004.getGxhdO101c004Model().clone());

        } catch (CloneNotSupportedException ex) {

            ErrUtil.outputErrorLog("CloneNotSupportedException発生", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            return processData;

        }

        return processData;
    }

    /**
     * 印刷幅(サブ画面Open)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openInsatsuhaba(ProcessData processData) {
        try {
            // コールバックパラメータにてサブ画面起動用の値を設定
            processData.setCollBackParam("gxhdo101c005");
            processData.setMethod("");

            // 印刷幅の現在の値をサブ画面の表示用の値に設定
            GXHDO101C005 beanGXHDO101C005 = (GXHDO101C005) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C005);
            beanGXHDO101C005.setGxhdO101c005ModelView(beanGXHDO101C005.getGxhdO101c005Model().clone());

            return processData;
        } catch (CloneNotSupportedException ex) {
            ErrUtil.outputErrorLog("CloneNotSupportedException発生", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            return processData;
        }
    }

    /**
     * 仮登録処理(データチェック処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
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
     * @param processData 処理制御データ
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkItemTempRegist(ProcessData processData) {

        //ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
        FXHDD01 itemStartjiNijimiKasure = getItemRow(processData.getItemList(), GXHDO101B003Const.STARTJI_NIJIMI_KASURE_CHECK);
        if ("NG".equals(itemStartjiNijimiKasure.getValue())) {
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemStartjiNijimiKasure);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemStartjiNijimiKasure.getLabel1());
        }

        //終了時ﾆｼﾞﾐ・ｶｽﾚ確認
        FXHDD01 itemShuryojiNijimiKasure = getItemRow(processData.getItemList(), GXHDO101B003Const.SHUURYOU_JI_NIJIMI_KASURE_CHECK);
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

                //ロールバック処理
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
                // 新しいリビジョンを採番
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId);

                // 品質DB登録実績更新処理
                updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_KARI_TOROKU, systemTime);
            }

            if (StringUtil.isEmpty(processData.getInitJotaiFlg()) || JOTAI_FLG_SAKUJO.equals(processData.getInitJotaiFlg())) {

                // 印刷RSUS_仮登録登録処理
                insertTmpSrRsusprn(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, systemTime, processData);

                // 印刷RSUS_ｻﾌﾞ画面仮登録登録処理
                insertTmpSubSrRsusprn(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, systemTime);

                // 前工程WIP取込ｻﾌﾞ画面仮登録登録処理
                insertTmpSrMwiplotlink(queryRunnerQcdb, conQcdb, kojyo, lotNo8, edaban, systemTime);

            } else {

                // 印刷RSUS_仮登録更新処理
                updateTmpSrRsusprn(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, systemTime, processData);

                // 印刷RSUS_ｻﾌﾞ画面仮登録更新処理
                updateTmpSubSrRsusprn(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo8, edaban, systemTime);

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
            
            // 膜厚入力画面背景色クリア
            GXHDO101C004 beanGXHDO101C004 = (GXHDO101C004) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C004);
            for (GXHDO101C004Model.MakuatsuData data : beanGXHDO101C004.getGxhdO101c004Model().getMakuatsuDataList()) {
                data.setStartTextBackColor("");
            }
            
            // 印刷幅画面背景色クリア
            GXHDO101C005 beanGXHDO101C005 = (GXHDO101C005) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C005);
            for (GXHDO101C005Model.PrintWidthData data : beanGXHDO101C005.getGxhdO101c005Model().getPrintWidthDataList()) {
                data.setStartTextBackColor("");
            }
            
            // 状態ﾌﾗｸﾞ、revisionを設定する。
            processData.setInitJotaiFlg(JOTAI_FLG_KARI_TOROKU);
            processData.setInitRev(newRev.toPlainString());

            return processData;
        } catch (SQLException e) {
            ErrUtil.outputErrorLog("SQLException発生", e, LOGGER);
            //ロールバック処理
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

        // サブ画面の入力チェックを行う。
        List<String> errorListSubForm;
        // 膜厚(SPS)画面チェック
        errorListSubForm = checkSubFormMakuatsu();
        if (!errorListSubForm.isEmpty()) {
            processData.setSubInitDispMsgList(errorListSubForm);
            processData.setMethod("openMakuatsu");
            return processData;
        }

        // 印刷幅画面チェック
        errorListSubForm = checkSubFormPrintWidth();
        if (!errorListSubForm.isEmpty()) {
            processData.setSubInitDispMsgList(errorListSubForm);
            processData.setMethod("openInsatsuhaba");
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
     * @param processData 処理制御データ
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkItemRegistCorrect(ProcessData processData) {
        
        // 印刷位置確認
         FXHDD01 startItiKakunin = getItemRow(processData.getItemList(), GXHDO101B003Const.STARTITIKAKUNIN);
        if (StringUtil.isEmpty(startItiKakunin.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(startItiKakunin);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, startItiKakunin.getLabel1());
        }

        //ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
        FXHDD01 itemStartjiNijimiKasure = getItemRow(processData.getItemList(), GXHDO101B003Const.STARTJI_NIJIMI_KASURE_CHECK);
        if ("NG".equals(itemStartjiNijimiKasure.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemStartjiNijimiKasure);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemStartjiNijimiKasure.getLabel1());
        }

        //終了時ﾆｼﾞﾐ・ｶｽﾚ確認
        FXHDD01 itemShuryojiNijimiKasure = getItemRow(processData.getItemList(), GXHDO101B003Const.SHUURYOU_JI_NIJIMI_KASURE_CHECK);
        if ("NG".equals(itemShuryojiNijimiKasure.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemShuryojiNijimiKasure);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemShuryojiNijimiKasure.getLabel1());
        }

        ValidateUtil validateUtil = new ValidateUtil();
        // 開始日時、終了日時前後チェック
        FXHDD01 itemInsatsuKaishiDay = getItemRow(processData.getItemList(), GXHDO101B003Const.INSATSU_KAISHI_DAY); //印刷開始日
        FXHDD01 itemInsatsuKaishiTime = getItemRow(processData.getItemList(), GXHDO101B003Const.INSATSU_KAISHI_TIME); // 印刷開始時刻
        Date kaishiDate = DateUtil.convertStringToDate(itemInsatsuKaishiDay.getValue(), itemInsatsuKaishiTime.getValue());
        FXHDD01 itemInsatsuShuryouDay = getItemRow(processData.getItemList(), GXHDO101B003Const.INSATSU_SHUURYOU_DAY); //印刷終了日
        FXHDD01 itemInsatsuShuryouTime = getItemRow(processData.getItemList(), GXHDO101B003Const.INSATSU_SHUURYOU_TIME); //印刷終了時刻
        Date shuryoDate = DateUtil.convertStringToDate(itemInsatsuShuryouDay.getValue(), itemInsatsuShuryouTime.getValue());
        //R001チェック呼出し
        String msgCheckR001 = validateUtil.checkR001(itemInsatsuKaishiDay.getLabel1(), kaishiDate, itemInsatsuShuryouDay.getLabel1(), shuryoDate);
        if (!StringUtil.isEmpty(msgCheckR001)) {
            //エラー発生時
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemInsatsuKaishiDay, itemInsatsuKaishiTime, itemInsatsuShuryouDay, itemInsatsuShuryouTime);
            return MessageUtil.getErrorMessageInfo("", msgCheckR001, true, true, errFxhdd01List);
        }

        // 印刷ｽﾀｰﾄ膜厚AVE
        FXHDD01 itemInsatsuStartMakuatsuAve = getItemRow(processData.getItemList(), GXHDO101B003Const.INSATSU_START_MAKUATSU_AVE);
        if (!StringUtil.isEmpty(itemInsatsuStartMakuatsuAve.getKikakuChi()) 
                && !"【-】".equals(itemInsatsuStartMakuatsuAve.getKikakuChi()) 
                && StringUtil.isEmpty(itemInsatsuStartMakuatsuAve.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemInsatsuStartMakuatsuAve);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, true, errFxhdd01List, itemInsatsuStartMakuatsuAve.getLabel1());
        }
        
        // 印刷ｽﾀｰﾄ膜厚MAX
        FXHDD01 itemInsatsuStartMakuatsuMax = getItemRow(processData.getItemList(), GXHDO101B003Const.INSATSU_START_MAKUATSU_MAX);
        if (!StringUtil.isEmpty(itemInsatsuStartMakuatsuMax.getKikakuChi()) 
                && !"【-】".equals(itemInsatsuStartMakuatsuMax.getKikakuChi()) 
                && StringUtil.isEmpty(itemInsatsuStartMakuatsuMax.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemInsatsuStartMakuatsuMax);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, true, errFxhdd01List, itemInsatsuStartMakuatsuMax.getLabel1());
        }
        
        // 印刷ｽﾀｰﾄ膜厚MIN
        FXHDD01 itemInsatsuStartMakuatsuMin = getItemRow(processData.getItemList(), GXHDO101B003Const.INSATSU_START_MAKUATSU_MIN);
        if (!StringUtil.isEmpty(itemInsatsuStartMakuatsuMin.getKikakuChi()) 
                && !"【-】".equals(itemInsatsuStartMakuatsuMin.getKikakuChi()) 
                && StringUtil.isEmpty(itemInsatsuStartMakuatsuMin.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemInsatsuStartMakuatsuMin);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, true, errFxhdd01List, itemInsatsuStartMakuatsuMin.getLabel1());
        }
        
        // 印刷ｽﾀｰﾄ膜厚CV
        FXHDD01 itemInsatsuStartMakuatsuCv = getItemRow(processData.getItemList(), GXHDO101B003Const.INSATSU_START_MAKUATSU_CV);
        if (!StringUtil.isEmpty(itemInsatsuStartMakuatsuCv.getKikakuChi()) 
                && !"【-】".equals(itemInsatsuStartMakuatsuCv.getKikakuChi()) 
                && StringUtil.isEmpty(itemInsatsuStartMakuatsuCv.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemInsatsuStartMakuatsuCv);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, true, errFxhdd01List, itemInsatsuStartMakuatsuCv.getLabel1());
        }
        return null;
    }

    /**
     * サブ画面(膜厚)チェック処理
     *
     * @return エラーリスト
     */
    private List<String> checkSubFormMakuatsu() {
        GXHDO101C004 beanGXHDO101C004 = (GXHDO101C004) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C004);
        return GXHDO101C004Logic.checkInput(beanGXHDO101C004.getGxhdO101c004Model());
    }

    /**
     * サブ画面(印刷幅)チェック処理
     *
     * @return エラーリスト
     */
    private List<String> checkSubFormPrintWidth() {
        GXHDO101C005 beanGXHDO101C005 = (GXHDO101C005) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C005);
        return GXHDO101C005Logic.checkInput(beanGXHDO101C005.getGxhdO101c005Model());
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
                //ロールバック処理
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
                // リビジョンを採番
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId);
                // 品質DB登録実績更新処理
                updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_TOROKUZUMI, systemTime);
            }

            // 仮登録状態の場合、仮登録のデータを削除する。
            SrRsusprn tmpSrRsusprn = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {
                // 更新前の値を取得
                List<SrRsusprn> srSrRsusprnList = getSrRsusprnData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo8, edaban);
                if (!srSrRsusprnList.isEmpty()) {
                    tmpSrRsusprn = srSrRsusprnList.get(0);
                }
              
                deleteTmpSrRsusprn(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);
                deleteTmpSubSrRsusprn(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);
                deleteTmpSrMwiplotlink(queryRunnerQcdb, conQcdb, kojyo, lotNo8, edaban);
            }

            // 印刷RSUS_登録処理
            insertSrRsusprn(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, systemTime, processData, tmpSrRsusprn);

            // 印刷RSUS_ｻﾌﾞ画面登録処理
            insertSubSrRsusprn(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, systemTime);

            // 前工程WIP取込ｻﾌﾞ画面登録処理
            insertSrMwiplotlink(queryRunnerQcdb, conQcdb, kojyo, lotNo8, edaban, systemTime);

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert(queryRunnerDoc,  conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, jissekiNo, "0", kikakuError.getKikakuchiInputErrorInfoList());
            }
            // 処理後はエラーリストをクリア
            kikakuError.setKikakuchiInputErrorInfoList(new ArrayList<>());

            DbUtils.commitAndCloseQuietly(conDoc);
            DbUtils.commitAndCloseQuietly(conQcdb);

            // 後続処理メソッド設定
            processData.setMethod("doPMLA0212");

            return processData;
        } catch (SQLException e) {
            ErrUtil.outputErrorLog("SQLException発生", e, LOGGER);

            //ロールバック処理
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
        // 製版ﾛｯﾄNoに値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, errorItemList);
        // 上記の処理でｴﾗｰが発生した場合、画面にエラーダイアログを出力する。
        if (!errorItemList.isEmpty()) {
            ErrorListMessage errorListMessageList = new ErrorListMessage();
            errorListMessageList.setResultMessage(MessageUtil.getMessage("buzailotnoErrorList"));
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
     * @param errorItemList エラー情報
     * @return レスポンスデータ
     */
    private void doCallPmla0212Api(ProcessData processData, String tantoshaCd, ArrayList<String> errorItemList) {

        // 製版ﾛｯﾄNoに値が入っている場合、以下の内容を元にAPIを呼び出す
        FXHDD01 itemSeihanNo = getItemRow(processData.getItemList(), GXHDO101B003Const.SEIHAN_OR_HANDOU_NO);
        if (itemSeihanNo == null || StringUtil.isEmpty(itemSeihanNo.getValue())) {
            return;
        }
        // 製版ﾛｯﾄNo
        String seihanNoValue = StringUtil.blankToNull(itemSeihanNo.getValue());
        // ﾛｯﾄNo
        FXHDD01 itemFxhdd01Lotno = getItemRow(processData.getItemList(), GXHDO101B003Const.LOTNO);
        String lotnoValue = null;
        if (itemFxhdd01Lotno != null) {
            // ﾛｯﾄNoの値
            lotnoValue = StringUtil.blankToNull(itemFxhdd01Lotno.getValue());
        }
        // 製版使用枚数
        FXHDD01 itemSeihanShiyouMaisuu = getItemRow(processData.getItemList(), GXHDO101B003Const.SEIHAN_OR_HANDOU_SHIYOU_MAISUU);
        String seihanShiyouMaisuuValue = null;
        if (itemSeihanShiyouMaisuu != null) {
            // 製版使用枚数の値
            seihanShiyouMaisuuValue = StringUtil.blankToNull(itemSeihanShiyouMaisuu.getValue());
            
        }
        ArrayList<String> paramsList = new ArrayList<>();
        paramsList.add(seihanNoValue);
        paramsList.add(tantoshaCd);
        paramsList.add("GXHDO101B003");
        paramsList.add(null);
        paramsList.add(null);
        paramsList.add(seihanShiyouMaisuuValue);
        paramsList.add(null);
        paramsList.add(lotnoValue);

        try {
            QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
            // 「/api/PMLA0212/doSave」APIを呼び出す
            String responseResult = CommonUtil.doRequestPmla0212Save(queryRunnerDoc, paramsList);
            if (!"ok".equals(responseResult)) {
                errorItemList.add(seihanNoValue);
            }
        } catch (Exception ex) {
            ErrUtil.outputErrorLog(itemSeihanNo.getLabel1() + "のﾃﾞｰﾀ連携処理エラー発生", ex, LOGGER);
            errorItemList.add(seihanNoValue);
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
        // 膜厚(SPS)画面チェック
        errorListSubForm = checkSubFormMakuatsu();
        if (!errorListSubForm.isEmpty()) {
            processData.setSubInitDispMsgList(errorListSubForm);
            processData.setMethod("openMakuatsu");
            return processData;
        }

        // 印刷幅画面チェック
        errorListSubForm = checkSubFormPrintWidth();
        if (!errorListSubForm.isEmpty()) {
            processData.setSubInitDispMsgList(errorListSubForm);
            processData.setMethod("openInsatsuhaba");
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
        processData.setUserAuthParam(GXHDO101B003Const.USER_AUTH_UPDATE_PARAM);

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
                //ロールバック処理
                DBUtil.rollbackConnection(conDoc, LOGGER);
                DBUtil.rollbackConnection(conQcdb, LOGGER);

                return processData;
            }

            BigDecimal rev = new BigDecimal(processData.getInitRev());
            // 新しいリビジョンを採番
            BigDecimal newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId);

            int jissekiNo = 1;
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            // 品質DB登録実績更新処理
            updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_TOROKUZUMI, systemTime);

            // 印刷RSUS_更新処理
            updateSrRsusprn(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, systemTime, processData);

            // 印刷RSUS_ｻﾌﾞ画面更新処理
            updateSubSrRsusprn(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo8, edaban, systemTime);

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
            //ロールバック処理
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
        processData.setUserAuthParam(GXHDO101B003Const.USER_AUTH_DELETE_PARAM);

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
                //ロールバック処理
                DBUtil.rollbackConnection(conDoc, LOGGER);
                DBUtil.rollbackConnection(conQcdb, LOGGER);

                return processData;
            }

            BigDecimal rev = new BigDecimal(processData.getInitRev());
            BigDecimal newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId);
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            // 品質DB登録実績更新処理
            updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_SAKUJO, systemTime);

            // 印刷RSUS_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo8, edaban);
            insertDeleteDataTmpSrRsusprn(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, systemTime);

            // 印刷RSUS_ｻﾌﾞ画面仮登録登録処理
            insertDeleteDataTmpSubSrRsusprn(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, systemTime);

            // 印刷RSUS_削除処理
            deleteSrRsusprn(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);

            // 印刷RSUS_ｻﾌﾞ画面削除処理
            deleteSubSrRsusprn(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);

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

            //ロールバック処理
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
                activeIdList.addAll(Arrays.asList(GXHDO101B003Const.BTN_COPY_EDABAN_BUTTOM,
                        GXHDO101B003Const.BTN_MAKUATSU_SUB_GAMEN_BUTTOM,
                        GXHDO101B003Const.BTN_INSATSUHABA_SUB_GAMEN_BUTTOM,
                        GXHDO101B003Const.BTN_DELETE_BUTTOM,
                        GXHDO101B003Const.BTN_UPDATE_BUTTOM,
                        GXHDO101B003Const.BTN_STARTDATETIME_BUTTOM,
                        GXHDO101B003Const.BTN_ENDDATETIME_BUTTOM,
                        GXHDO101B003Const.BTN_BUZAIZAIKOJYOHO_BOTTOM,
                        GXHDO101B003Const.BTN_COPY_EDABAN_TOP,
                        GXHDO101B003Const.BTN_MAKUATSU_SUB_GAMEN_TOP,
                        GXHDO101B003Const.BTN_INSATSUHABA_SUB_GAMEN_TOP,
                        GXHDO101B003Const.BTN_DELETE_TOP,
                        GXHDO101B003Const.BTN_UPDATE_TOP,
                        GXHDO101B003Const.BTN_STARTDATETIME_TOP,
                        GXHDO101B003Const.BTN_ENDDATETIME_TOP,
                        GXHDO101B003Const.BTN_BUZAIZAIKOJYOHO_TOP
                ));
                inactiveIdList.addAll(Arrays.asList(GXHDO101B003Const.BTN_KARI_TOUROKU_BUTTOM,
                        GXHDO101B003Const.BTN_INSERT_BUTTOM,
                        GXHDO101B003Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B003Const.BTN_INSERT_TOP));

                break;
            default:
                activeIdList.addAll(Arrays.asList(GXHDO101B003Const.BTN_KARI_TOUROKU_BUTTOM,
                        GXHDO101B003Const.BTN_COPY_EDABAN_BUTTOM,
                        GXHDO101B003Const.BTN_MAKUATSU_SUB_GAMEN_BUTTOM,
                        GXHDO101B003Const.BTN_INSATSUHABA_SUB_GAMEN_BUTTOM,
                        GXHDO101B003Const.BTN_INSERT_BUTTOM,
                        GXHDO101B003Const.BTN_STARTDATETIME_BUTTOM,
                        GXHDO101B003Const.BTN_ENDDATETIME_BUTTOM,
                        GXHDO101B003Const.BTN_BUZAIZAIKOJYOHO_BOTTOM,
                        GXHDO101B003Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B003Const.BTN_COPY_EDABAN_TOP,
                        GXHDO101B003Const.BTN_MAKUATSU_SUB_GAMEN_TOP,
                        GXHDO101B003Const.BTN_INSATSUHABA_SUB_GAMEN_TOP,
                        GXHDO101B003Const.BTN_INSERT_TOP,
                        GXHDO101B003Const.BTN_STARTDATETIME_TOP,
                        GXHDO101B003Const.BTN_ENDDATETIME_TOP,
                        GXHDO101B003Const.BTN_BUZAIZAIKOJYOHO_TOP
                ));

                inactiveIdList.addAll(Arrays.asList(GXHDO101B003Const.BTN_DELETE_BUTTOM,
                        GXHDO101B003Const.BTN_UPDATE_BUTTOM,
                        GXHDO101B003Const.BTN_DELETE_TOP,
                        GXHDO101B003Const.BTN_UPDATE_TOP));

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
            case GXHDO101B003Const.BTN_MAKUATSU_SUB_GAMEN_TOP:
            case GXHDO101B003Const.BTN_MAKUATSU_SUB_GAMEN_BUTTOM:
                method = "openMakuatsu";
                break;
            // 印刷幅
            case GXHDO101B003Const.BTN_INSATSUHABA_SUB_GAMEN_TOP:
            case GXHDO101B003Const.BTN_INSATSUHABA_SUB_GAMEN_BUTTOM:
                method = "openInsatsuhaba";
                break;

            // 仮登録
            case GXHDO101B003Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO101B003Const.BTN_KARI_TOUROKU_BUTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO101B003Const.BTN_INSERT_TOP:
            case GXHDO101B003Const.BTN_INSERT_BUTTOM:
                method = "checkDataRegist";
                break;
            // 枝番コピー
            case GXHDO101B003Const.BTN_COPY_EDABAN_TOP:
            case GXHDO101B003Const.BTN_COPY_EDABAN_BUTTOM:
                method = "confEdabanCopy";
                break;
            // 修正
            case GXHDO101B003Const.BTN_UPDATE_TOP:
            case GXHDO101B003Const.BTN_UPDATE_BUTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO101B003Const.BTN_DELETE_TOP:
            case GXHDO101B003Const.BTN_DELETE_BUTTOM:
                method = "checkDataDelete";
                break;
            // 開始日時
            case GXHDO101B003Const.BTN_STARTDATETIME_TOP:
            case GXHDO101B003Const.BTN_STARTDATETIME_BUTTOM:
                method = "setKaishiDateTime";
                break;
            // 終了日時
            case GXHDO101B003Const.BTN_ENDDATETIME_TOP:
            case GXHDO101B003Const.BTN_ENDDATETIME_BUTTOM:
                method = "setShuryouDateTime";
                break;
            // 前工程WIP
            case GXHDO101B003Const.BTN_WIP_IMPORT_TOP:
            case GXHDO101B003Const.BTN_WIP_IMPORT_BOTTOM:
                method = "openWipImport";
                break;
            // 部材在庫情報
            case GXHDO101B003Const.BTN_BUZAIZAIKOJYOHO_TOP:
            case GXHDO101B003Const.BTN_BUZAIZAIKOJYOHO_BOTTOM:
                method = "doBuzaizaikojyohoSyori";
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
        String genryou = StringUtil.nullToBlank(getMapData(sekkeiData, "GENRYOU")); //原料
        String eatumi = StringUtil.nullToBlank(getMapData(sekkeiData, "EATUMI")); //電極厚み

        // 設計情報チェック(対象のデータが取得出来ていない場合エラー)
        errorMessageList.addAll(ValidateUtil.checkSekkeiUnsetItems(sekkeiData, getMapSekkeiAssociation()));

        //仕掛情報の取得
        Map shikakariData = loadShikakariData(queryRunnerWip, lotNo);
        if (shikakariData == null || shikakariData.isEmpty()) {
            errorMessageList.add(MessageUtil.getMessage("XHD-000029"));
        }
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); //ﾛｯﾄ区分ｺｰﾄﾞ
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));// ｵｰﾅｰｺｰﾄﾞ

        // ロット区分マスタ情報の取得
        Map lotKbnMasData = loadLotKbnMas(queryRunnerWip, lotkubuncode);
        if (lotKbnMasData == null || lotKbnMasData.isEmpty()) {
            errorMessageList.add(MessageUtil.getMessage("XHD-000015"));
        }

        // オーナーマスタ情報の取得
        Map ownerMasData = loadOwnerMas(queryRunnerWip, ownercode);
        if (ownerMasData == null || ownerMasData.isEmpty()) {
            errorMessageList.add(MessageUtil.getMessage("XHD-000016"));
        }

        // 入力項目の情報を画面にセットする。
        if (!setInputItemData(processData, queryRunnerDoc, queryRunnerQcdb, lotNo, formId)) {
            // エラー発生時は処理を中断
            processData.setFatalError(true);
            processData.setInitMessageList(Arrays.asList(MessageUtil.getMessage("XHD-000038")));
            return processData;
        }
        
        // 値を保持
        processData.getHiddenDataMap().put("genryou", genryou);//原料
        processData.getHiddenDataMap().put("eatumi", eatumi);//電極厚み
        processData.getHiddenDataMap().put("lotkubuncode", lotkubuncode);//ﾛｯﾄ区分ｺｰﾄﾞ
        processData.getHiddenDataMap().put("ownercode", ownercode);// ｵｰﾅｰｺｰﾄﾞ
        
        // 画面に取得した情報をセットする。(入力項目以外)
        setViewItemData(processData, sekkeiData, lotKbnMasData, ownerMasData, shikakariData, lotNo);

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
    private void setViewItemData(ProcessData processData, Map sekkeiData, Map lotKbnMasData, Map ownerMasData,
            Map shikakariData, String lotNo) {

        // ロットNo
        this.setItemData(processData, GXHDO101B003Const.LOTNO, lotNo);
        // KCPNO
        this.setItemData(processData, GXHDO101B003Const.KCPNO, StringUtil.nullToBlank(getMapData(shikakariData, "kcpno")));
        // セット数
        String suuryo = StringUtil.nullToBlank(getMapData(shikakariData, "suuryo"));
        String torikosuu = StringUtil.nullToBlank(getMapData(shikakariData, "torikosuu"));
        if (StringUtil.isEmpty(suuryo) || "0".equals(suuryo) || StringUtil.isEmpty(torikosuu) || "0".equals(torikosuu)) {
            this.setItemData(processData, GXHDO101B003Const.SET_SUU, "0");
        } else {
            BigDecimal decHasseisu = new BigDecimal(suuryo);
            BigDecimal decTorikosuu = new BigDecimal(torikosuu);
            BigDecimal setsu = decHasseisu.divide(decTorikosuu, 0, RoundingMode.DOWN);
            this.setItemData(processData, GXHDO101B003Const.SET_SUU, setsu.toPlainString());
        }

        // 客先
        this.setItemData(processData, GXHDO101B003Const.KYAKUSAKI, StringUtil.nullToBlank(getMapData(shikakariData, "tokuisaki")));

        // ロット区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); //ﾛｯﾄ区分ｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B003Const.LOT_KUBUN, "");
        } else {
            String lotKubun = StringUtil.nullToBlank(getMapData(lotKbnMasData, "lotkubun"));
            this.setItemData(processData, GXHDO101B003Const.LOT_KUBUN, lotkubuncode + ":" + lotKubun);
        }

        // オーナー
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));// ｵｰﾅｰｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B003Const.OWNER, "");
        } else {
            String owner = StringUtil.nullToBlank(getMapData(ownerMasData, "ownername"));
            this.setItemData(processData, GXHDO101B003Const.OWNER, ownercode + ":" + owner);
        }

        // 電極テープ
        this.setItemData(processData, GXHDO101B003Const.DENKYOKU_TAPE, StringUtil.nullToBlank(sekkeiData.get("GENRYOU"))
                + "  " + StringUtil.nullToBlank(sekkeiData.get("ETAPE")) + "  " + StringUtil.nullToBlank(sekkeiData.get("ELOT")));

        // 積層数
        this.setItemData(processData, GXHDO101B003Const.SEKISOU_SU, StringUtil.nullToBlank(sekkeiData.get("EATUMI"))
                + "μm×"
                + StringUtil.nullToBlank(sekkeiData.get("SOUSUU"))
                + "層  "
                + StringUtil.nullToBlank(sekkeiData.get("EMAISUU"))
                + "枚");

        
        // 電極ペースト
        this.setItemData(processData, GXHDO101B003Const.DENKYOKU_PASTE, getKikakuchiValue(processData.getItemList(), GXHDO101B003Const.DENKYOKU_PASTE));

        // 製版名
        this.setItemData(processData, GXHDO101B003Const.SEIHAN_OR_HANDOU_MEI, StringUtil.nullToBlank(sekkeiData.get("PATTERN")));
        
        // 電極製版仕様
        this.setItemData(processData, GXHDO101B003Const.DENKYOKU_SEIHAN_SHIYOU, getKikakuchiValue(processData.getItemList(), GXHDO101B003Const.DENKYOKU_SEIHAN_SHIYOU));
        

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
     * @return 設定結果(失敗時false)
     */
    private boolean setInputItemData(ProcessData processData, QueryRunner queryRunnerDoc, QueryRunner queryRunnerQcdb,
            String lotNo, String formId) throws SQLException {

        List<SrRsusprn> srRsusprnDataList = new ArrayList<>();
        List<SubSrRsusprn> subSrRsusprnDataList = new ArrayList<>();
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
                setInputItemDataSubFormC004(null);

                // 印刷幅入力画面データ設定
                setInputItemDataSubFormC005(null);
                
                // 前工程WIP取込画面データ設定
                setInputItemDataSubFormC020(queryRunnerQcdb, kojyo, lotNo8, edaban, jotaiFlg);
                
                //ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
                FXHDD01 fxhdd01 = getItemRow(processData.getItemList(), GXHDO101B003Const.SQUEEGEE_SPEED);
                this.setItemData(processData, GXHDO101B003Const.SQUEEGEE_SPEED, getStringToIntValue(StringUtil.nullToBlank(fxhdd01.getKikakuChi()).replace("【", "")));
                //ｽｸﾚｯﾊﾟｰｽﾋﾟｰﾄﾞ 
                fxhdd01 = getItemRow(processData.getItemList(), GXHDO101B003Const.SCRAPER_SPEED);
                this.setItemData(processData, GXHDO101B003Const.SCRAPER_SPEED, getStringToIntValue(StringUtil.nullToBlank(fxhdd01.getKikakuChi()).replace("【", "")));

                return true;
            }

            // 印刷RSUSデータ取得
            srRsusprnDataList = getSrRsusprnData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban);
            if (srRsusprnDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // 印刷RSUS_ｻﾌﾞ画面データ取得
            subSrRsusprnDataList = getSubSrRsusprnData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban);
            if (subSrRsusprnDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srRsusprnDataList.isEmpty() || subSrRsusprnDataList.isEmpty()) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srRsusprnDataList.get(0));

        // 膜厚入力画面データ設定
        setInputItemDataSubFormC004(subSrRsusprnDataList.get(0));

        // 印刷幅入力画面データ設定
        setInputItemDataSubFormC005(subSrRsusprnDataList.get(0));
        
        // 前工程WIP取込画面データ設定
        setInputItemDataSubFormC020(queryRunnerQcdb, kojyo, lotNo8, edaban, jotaiFlg);

        return true;

    }

    /**
     * メイン画面データ設定処理
     *
     * @param processData 処理制御データ
     * @param srRsusprnData 印刷RSUSデータ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrRsusprn srRsusprnData) {
        // ｽﾘｯﾌﾟﾛｯﾄNo
        this.setItemData(processData, GXHDO101B003Const.SLIP_LOTNO, getSrRsusprnItemData(GXHDO101B003Const.SLIP_LOTNO, srRsusprnData));
        // ﾛｰﾙNo1
        this.setItemData(processData, GXHDO101B003Const.ROLL_NO1, getSrRsusprnItemData(GXHDO101B003Const.ROLL_NO1, srRsusprnData));
        // 原料記号
        this.setItemData(processData, GXHDO101B003Const.GENRYO_KIGOU, getSrRsusprnItemData(GXHDO101B003Const.GENRYO_KIGOU, srRsusprnData));
        // ﾍﾟｰｽﾄﾛｯﾄNo1
        this.setItemData(processData, GXHDO101B003Const.PASTE_LOT_NO1, getSrRsusprnItemData(GXHDO101B003Const.PASTE_LOT_NO1, srRsusprnData));
        // ﾍﾟｰｽﾄ粘度1
        this.setItemData(processData, GXHDO101B003Const.PASTE_NENDO1, getSrRsusprnItemData(GXHDO101B003Const.PASTE_NENDO1, srRsusprnData));
        // ﾍﾟｰｽﾄ温度1
        this.setItemData(processData, GXHDO101B003Const.PASTE_ONDO1, getSrRsusprnItemData(GXHDO101B003Const.PASTE_ONDO1, srRsusprnData));
        // ﾍﾟｰｽﾄ固形分1
        this.setItemData(processData, GXHDO101B003Const.PASTE_KOKEIBUN1, getSrRsusprnItemData(GXHDO101B003Const.PASTE_KOKEIBUN1, srRsusprnData));
        // ﾍﾟｰｽﾄﾛｯﾄNo2
        this.setItemData(processData, GXHDO101B003Const.PASTE_LOT_NO2, getSrRsusprnItemData(GXHDO101B003Const.PASTE_LOT_NO2, srRsusprnData));
        // ﾍﾟｰｽﾄ粘度2
        this.setItemData(processData, GXHDO101B003Const.PASTE_NENDO2, getSrRsusprnItemData(GXHDO101B003Const.PASTE_NENDO2, srRsusprnData));
        // ﾍﾟｰｽﾄ温度2
        this.setItemData(processData, GXHDO101B003Const.PASTE_ONDO2, getSrRsusprnItemData(GXHDO101B003Const.PASTE_ONDO2, srRsusprnData));
        // ﾍﾟｰｽﾄ固形分2
        this.setItemData(processData, GXHDO101B003Const.PASTE_KOKEIBUN2, getSrRsusprnItemData(GXHDO101B003Const.PASTE_KOKEIBUN2, srRsusprnData));
        // ＰＥＴフィルム種類
        this.setItemData(processData, GXHDO101B003Const.PET_FILM_SHURUI, getSrRsusprnItemData(GXHDO101B003Const.PET_FILM_SHURUI, srRsusprnData));
        // 印刷号機
        this.setItemData(processData, GXHDO101B003Const.INSATSU_GOUKI, getSrRsusprnItemData(GXHDO101B003Const.INSATSU_GOUKI, srRsusprnData));
        // 乾燥温度表示値1
        this.setItemData(processData, GXHDO101B003Const.KANSOU_ONDO_HYOUJICHI1, getSrRsusprnItemData(GXHDO101B003Const.KANSOU_ONDO_HYOUJICHI1, srRsusprnData));
        // 乾燥温度表示値2
        this.setItemData(processData, GXHDO101B003Const.KANSOU_ONDO_HYOUJICHI2, getSrRsusprnItemData(GXHDO101B003Const.KANSOU_ONDO_HYOUJICHI2, srRsusprnData));
        // 乾燥温度表示値3
        this.setItemData(processData, GXHDO101B003Const.KANSOU_ONDO_HYOUJICHI3, getSrRsusprnItemData(GXHDO101B003Const.KANSOU_ONDO_HYOUJICHI3, srRsusprnData));
        // 乾燥温度表示値4
        this.setItemData(processData, GXHDO101B003Const.KANSOU_ONDO_HYOUJICHI4, getSrRsusprnItemData(GXHDO101B003Const.KANSOU_ONDO_HYOUJICHI4, srRsusprnData));
        // 乾燥温度表示値5
        this.setItemData(processData, GXHDO101B003Const.KANSOU_ONDO_HYOUJICHI5, getSrRsusprnItemData(GXHDO101B003Const.KANSOU_ONDO_HYOUJICHI5, srRsusprnData));
        // 製版No
        this.setItemData(processData, GXHDO101B003Const.SEIHAN_OR_HANDOU_NO, getSrRsusprnItemData(GXHDO101B003Const.SEIHAN_OR_HANDOU_NO, srRsusprnData));
        // 製版使用枚数
        this.setItemData(processData, GXHDO101B003Const.SEIHAN_OR_HANDOU_SHIYOU_MAISUU, getSrRsusprnItemData(GXHDO101B003Const.SEIHAN_OR_HANDOU_SHIYOU_MAISUU, srRsusprnData));
        // 最大処理数
        this.setItemData(processData, GXHDO101B003Const.SAIDAISYORISUU, getSrRsusprnItemData(GXHDO101B003Const.SAIDAISYORISUU, srRsusprnData));
        // 累計処理数
        this.setItemData(processData, GXHDO101B003Const.RUIKEISYORISUU, getSrRsusprnItemData(GXHDO101B003Const.RUIKEISYORISUU, srRsusprnData));
        // ｽｷｰｼﾞNo
        this.setItemData(processData, GXHDO101B003Const.SQUEEGEE_OR_ATSUDOU_NO, getSrRsusprnItemData(GXHDO101B003Const.SQUEEGEE_OR_ATSUDOU_NO, srRsusprnData));
        // 印刷開始日
        this.setItemData(processData, GXHDO101B003Const.INSATSU_KAISHI_DAY, getSrRsusprnItemData(GXHDO101B003Const.INSATSU_KAISHI_DAY, srRsusprnData));
        // 印刷開始時間
        this.setItemData(processData, GXHDO101B003Const.INSATSU_KAISHI_TIME, getSrRsusprnItemData(GXHDO101B003Const.INSATSU_KAISHI_TIME, srRsusprnData));
        // 印刷ｽﾀｰﾄ膜厚AVE
        this.setItemData(processData, GXHDO101B003Const.INSATSU_START_MAKUATSU_AVE, getSrRsusprnItemData(GXHDO101B003Const.INSATSU_START_MAKUATSU_AVE, srRsusprnData));
        // 印刷ｽﾀｰﾄ膜厚MAX
        this.setItemData(processData, GXHDO101B003Const.INSATSU_START_MAKUATSU_MAX, getSrRsusprnItemData(GXHDO101B003Const.INSATSU_START_MAKUATSU_MAX, srRsusprnData));
        // 印刷ｽﾀｰﾄ膜厚MIN
        this.setItemData(processData, GXHDO101B003Const.INSATSU_START_MAKUATSU_MIN, getSrRsusprnItemData(GXHDO101B003Const.INSATSU_START_MAKUATSU_MIN, srRsusprnData));
        // 印刷ｽﾀｰﾄ膜厚CV
        this.setItemData(processData, GXHDO101B003Const.INSATSU_START_MAKUATSU_CV, getSrRsusprnItemData(GXHDO101B003Const.INSATSU_START_MAKUATSU_CV, srRsusprnData));
        // ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
        this.setItemData(processData, GXHDO101B003Const.STARTJI_NIJIMI_KASURE_CHECK, getSrRsusprnItemData(GXHDO101B003Const.STARTJI_NIJIMI_KASURE_CHECK, srRsusprnData));
        // 印刷スタート時担当者
        this.setItemData(processData, GXHDO101B003Const.INSATSU_STARTJI_TANTOUSHA, getSrRsusprnItemData(GXHDO101B003Const.INSATSU_STARTJI_TANTOUSHA, srRsusprnData));
        // 印刷終了日
        this.setItemData(processData, GXHDO101B003Const.INSATSU_SHUURYOU_DAY, getSrRsusprnItemData(GXHDO101B003Const.INSATSU_SHUURYOU_DAY, srRsusprnData));
        // 印刷終了時刻
        this.setItemData(processData, GXHDO101B003Const.INSATSU_SHUURYOU_TIME, getSrRsusprnItemData(GXHDO101B003Const.INSATSU_SHUURYOU_TIME, srRsusprnData));
        // 終了時ﾆｼﾞﾐ・ｶｽﾚ確認
        this.setItemData(processData, GXHDO101B003Const.SHUURYOU_JI_NIJIMI_KASURE_CHECK, getSrRsusprnItemData(GXHDO101B003Const.SHUURYOU_JI_NIJIMI_KASURE_CHECK, srRsusprnData));
        // 印刷エンド時担当者
        this.setItemData(processData, GXHDO101B003Const.INSATSU_ENDJI_TANTOUSHA, getSrRsusprnItemData(GXHDO101B003Const.INSATSU_ENDJI_TANTOUSHA, srRsusprnData));
        // 印刷枚数
        this.setItemData(processData, GXHDO101B003Const.INSATSU_MAISUU, getSrRsusprnItemData(GXHDO101B003Const.INSATSU_MAISUU, srRsusprnData));
        // 乾燥炉圧
        this.setItemData(processData, GXHDO101B003Const.KANSOU_RO_ATSU, getSrRsusprnItemData(GXHDO101B003Const.KANSOU_RO_ATSU, srRsusprnData));
        // MLD
        this.setItemData(processData, GXHDO101B003Const.MLD, getSrRsusprnItemData(GXHDO101B003Const.MLD, srRsusprnData));
        // 印刷幅
        this.setItemData(processData, GXHDO101B003Const.INSATSU_HABA, getSrRsusprnItemData(GXHDO101B003Const.INSATSU_HABA, srRsusprnData));
        // ﾃｰﾌﾞﾙｸﾘｱﾗﾝｽ
        this.setItemData(processData, GXHDO101B003Const.TABLE_CLEARANCE, getSrRsusprnItemData(GXHDO101B003Const.TABLE_CLEARANCE, srRsusprnData));
        //備考1
        this.setItemData(processData, GXHDO101B003Const.BIKOU1, getSrRsusprnItemData(GXHDO101B003Const.BIKOU1, srRsusprnData));
        //備考2
        this.setItemData(processData, GXHDO101B003Const.BIKOU2, getSrRsusprnItemData(GXHDO101B003Const.BIKOU2, srRsusprnData));
        //成形長さ
        this.setItemData(processData, GXHDO101B003Const.SEIKEINAGASA, getSrRsusprnItemData(GXHDO101B003Const.SEIKEINAGASA, srRsusprnData));
        // ｽｷｰｼﾞ枚数
        this.setItemData(processData, GXHDO101B003Const.SQUEEGEE_MAISU, getSrRsusprnItemData(GXHDO101B003Const.SQUEEGEE_MAISU, srRsusprnData));
        //ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
        this.setItemData(processData, GXHDO101B003Const.SQUEEGEE_SPEED, getSrRsusprnItemData(GXHDO101B003Const.SQUEEGEE_SPEED, srRsusprnData));
        //ｽｸﾚｯﾊﾟｰｽﾋﾟｰﾄﾞ    
        this.setItemData(processData, GXHDO101B003Const.SCRAPER_SPEED, getSrRsusprnItemData(GXHDO101B003Const.SCRAPER_SPEED, srRsusprnData));
        //差圧
        this.setItemData(processData, GXHDO101B003Const.SAATSU, getSrRsusprnItemData(GXHDO101B003Const.SAATSU, srRsusprnData));
        //印刷位置確認
        this.setItemData(processData, GXHDO101B003Const.STARTITIKAKUNIN, getSrRsusprnItemData(GXHDO101B003Const.STARTITIKAKUNIN, srRsusprnData));
        //印刷ｽﾀｰﾄ確認者
        this.setItemData(processData, GXHDO101B003Const.INSATSU_STARTJI_KAKUNINSYA, getSrRsusprnItemData(GXHDO101B003Const.INSATSU_STARTJI_KAKUNINSYA, srRsusprnData));
        // 先行ﾛｯﾄNo
        this.setItemData(processData, GXHDO101B003Const.SENKOULOTNO, getSrRsusprnItemData(GXHDO101B003Const.SENKOULOTNO, srRsusprnData));
        // ﾃｰﾌﾟ使い切り
        this.setItemData(processData, GXHDO101B003Const.TAPETSUKAIKIRI, getSrRsusprnItemData(GXHDO101B003Const.TAPETSUKAIKIRI, srRsusprnData));
        // 次ﾛｯﾄへ
        this.setItemData(processData, GXHDO101B003Const.JILOTHE, getSrRsusprnItemData(GXHDO101B003Const.JILOTHE, srRsusprnData));
        // 備考3
        this.setItemData(processData, GXHDO101B003Const.BIKOU3, getSrRsusprnItemData(GXHDO101B003Const.BIKOU3, srRsusprnData));
        // 備考4
        this.setItemData(processData, GXHDO101B003Const.BIKOU4, getSrRsusprnItemData(GXHDO101B003Const.BIKOU4, srRsusprnData));
        // 備考5
        this.setItemData(processData, GXHDO101B003Const.BIKOU5, getSrRsusprnItemData(GXHDO101B003Const.BIKOU5, srRsusprnData));
        
    }

    /**
     * 膜厚入力画面データ設定処理
     *
     * @param subSrRsusprnData 印刷RSUS_ｻﾌﾞ画面データ
     */
    private void setInputItemDataSubFormC004(SubSrRsusprn subSrRsusprnData) {
        // サブ画面の情報を取得
        GXHDO101C004 beanGXHDO101C004 = (GXHDO101C004) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C004);

        //データの設定
        String[] makuatsuStart;

        GXHDO101C004Model model;
        if (subSrRsusprnData == null) {
            // 登録データが無い場合空の状態で初期値をセットする。
            makuatsuStart = new String[]{"", "", "", "", ""}; //膜厚スタート1～9
            model = GXHDO101C004Logic.createGXHDO101C004Model(makuatsuStart);

        } else {
            // 登録データがあれば登録データをセットする。
            //膜厚スタート1～5
            makuatsuStart = new String[]{
                StringUtil.nullToBlank(subSrRsusprnData.getMakuatsuStart1()),
                StringUtil.nullToBlank(subSrRsusprnData.getMakuatsuStart2()),
                StringUtil.nullToBlank(subSrRsusprnData.getMakuatsuStart3()),
                StringUtil.nullToBlank(subSrRsusprnData.getMakuatsuStart4()),
                StringUtil.nullToBlank(subSrRsusprnData.getMakuatsuStart5())};
            model = GXHDO101C004Logic.createGXHDO101C004Model(makuatsuStart);
        }

        // サブ画面から戻ったときに値を設定する項目を指定する。
        model.setReturnItemIdStartAve(GXHDO101B003Const.INSATSU_START_MAKUATSU_AVE);
        model.setReturnItemIdStartMax(GXHDO101B003Const.INSATSU_START_MAKUATSU_MAX);
        model.setReturnItemIdStartMin(GXHDO101B003Const.INSATSU_START_MAKUATSU_MIN);
        model.setReturnItemIdStartCv(GXHDO101B003Const.INSATSU_START_MAKUATSU_CV);
        beanGXHDO101C004.setGxhdO101c004Model(model);
    }

    /**
     * 印刷幅入力画面データ設定処理
     *
     * @param subSrRsusprnData 印刷RSUS_ｻﾌﾞ画面データ
     */
    private void setInputItemDataSubFormC005(SubSrRsusprn subSrRsusprnData) {

        // 印刷幅サブ画面初期表示データ設定
        GXHDO101C005 beanGXHDO101C005 = (GXHDO101C005) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C005);
        //データの設定
        String[] startValues;
        GXHDO101C005Model model;
        if (subSrRsusprnData == null) {
            startValues = new String[]{"", "", "", "", ""}; //印刷幅Start
            model = GXHDO101C005Logic.createGXHDO101C005Model(startValues);
        } else {
            //スタート1～5
            startValues = new String[]{
                StringUtil.nullToBlank(subSrRsusprnData.getInsatuhabaStart1()),
                StringUtil.nullToBlank(subSrRsusprnData.getInsatuhabaStart2()),
                StringUtil.nullToBlank(subSrRsusprnData.getInsatuhabaStart3()),
                StringUtil.nullToBlank(subSrRsusprnData.getInsatuhabaStart4()),
                StringUtil.nullToBlank(subSrRsusprnData.getInsatuhabaStart5())};
            model = GXHDO101C005Logic.createGXHDO101C005Model(startValues);
        }

        // サブ画面から戻ったときに値を設定する項目を指定する。
        model.setReturnItemIdInsatsuHaba(GXHDO101B003Const.INSATSU_HABA);
        beanGXHDO101C005.setGxhdO101c005Model(model);
    }

    /**
     * 印刷RSUSの入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態フラグ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return 印刷RSUS登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrRsusprn> getSrRsusprnData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrRsusprn(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSrRsusprn(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        }
    }

    /**
     * 印刷RSUS_ｻﾌﾞ画面の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @return 印刷RSUS_ｻﾌﾞ画面データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrRsusprn> getSubSrRsusprnData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {
        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSubSrRsusprn(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSubSrRsusprn(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        }
    }

    /**
     * [設計]から、初期表示する情報を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param queryRunnerWip QueryRunnerオブジェクト
     * @param lotNo ロットNo(検索キー)
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
                put("PATTERN", "電極製版名");
                put("LASTLAYERSLIDERYO", "最上層スライド量");
            }
        };

        return map;
    }

    /**
     * [ﾛｯﾄ区分ﾏｽﾀｰ]から、ﾛｯﾄ区分を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
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
     * @param lotNo ロットNo(検索キー)
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
     * [品質DB登録実績]から、リビジョン,状態フラグを取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param formId 画面ID(検索キー)
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
     * @param formId 画面ID
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
     * @param formId 画面ID(検索キー)
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
     * [印刷RSUS]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrRsusprn> loadSrRsusprn(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT KOJYO,LOTNO,EDABAN,KCPNO,TAPESYURUI,TAPELOTNO,TapeSlipKigo,GENRYOKIGO,KAISINICHIJI,"
                + "SYURYONICHIJI,GOKI,SKEEGENO,SKEEGEMAISUU,SKEEGESPEED,KANSOONDO,CLEARANCE,SAATU,MAKUATU1,"
                + "SEIHANNO,SEIHANMAISUU,saidaisyorisuu,ruikeisyorisuu,PASTELOTNO,PASTENENDO,PASTEONDO,INSATUROLLNO,INSATUROLLNO2,INSATUROLLNO3,"
                + "INSATUROLLNO4,INSATUROLLNO5,INSATUHABASAVE,INSATUHABAEAVE,MLD,BIKO1,BIKO2,TOROKUNICHIJI,KOSINNICHIJI,TANTOSYA,pkokeibun1,"
                + "pastelotno2,pastenendo2,pasteondo2,pkokeibun2,petfilmsyurui,kansoondo2,kansoondo3,kansoondo4,"
                + "kansoondo5,seihanmei,makuatsu_ave_start,makuatsu_max_start,makuatsu_min_start,makuatucv_start,"
                + "nijimikasure_start,nijimikasure_end,tanto_end,printmaisuu,kansouroatsu,printhaba,table_clearrance,"
                + "tokuisaki,lotkubuncode,ownercode,seikeinagasa,genryou,eatumi,epaste,pattern,scraperspeed,startitikakunin,"
                + "kakuninsya,CONCAT(skojyo , slotno , sedaban) senkoulotno,tapetsukaikiri,jilothe,bikou3,bikou4,bikou5,revision,0 AS deleteflag "
                + "FROM sr_rsusprn "
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
        mapping.put("TAPESYURUI", "tapesyurui"); //ﾃｰﾌﾟ種類
        mapping.put("TAPELOTNO", "tapelotno"); //ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
        mapping.put("TapeSlipKigo", "tapeSlipKigo"); //ﾃｰﾌﾟｽﾘｯﾌﾟ記号
        mapping.put("GENRYOKIGO", "genryoukigou"); //原料記号
        mapping.put("KAISINICHIJI", "kaisinichiji"); //開始日時
        mapping.put("SYURYONICHIJI", "syuryonichiji"); //終了日時
        mapping.put("GOKI", "goki"); //号機
        mapping.put("SKEEGENO", "skeegeno"); //ｽｷｰｼﾞNo
        mapping.put("SKEEGEMAISUU", "skeegemaisuu"); //ｽｷｰｼﾞ枚数
        mapping.put("SKEEGESPEED", "skeegespeed"); //ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
        mapping.put("KANSOONDO", "kansoondo"); //乾燥温度
        mapping.put("CLEARANCE", "clearance"); //ｸﾘｱﾗﾝｽ設定値
        mapping.put("SAATU", "saatu"); //差圧
        mapping.put("MAKUATU1", "makuatu1"); //膜厚1
        mapping.put("SEIHANNO", "seihanno"); //製版No
        mapping.put("SEIHANMAISUU", "seihanmaisuu"); //製版枚数
        mapping.put("saidaisyorisuu", "saidaisyorisuu"); //最大処理数
        mapping.put("ruikeisyorisuu", "ruikeisyorisuu"); //累計処理数
        mapping.put("PASTELOTNO", "pastelotno"); //ﾍﾟｰｽﾄﾛｯﾄNo
        mapping.put("PASTENENDO", "pastenendo"); //ﾍﾟｰｽﾄ粘度
        mapping.put("PASTEONDO", "pasteondo"); //ﾍﾟｰｽﾄ温度
        mapping.put("INSATUROLLNO", "insaturollno"); //印刷ﾛｰﾙNo1
        mapping.put("INSATUROLLNO2", "insaturollno2"); //印刷ﾛｰﾙNo2
        mapping.put("INSATUROLLNO3", "insaturollno3"); //印刷ﾛｰﾙNo3
        mapping.put("INSATUROLLNO4", "insaturollno4"); //印刷ﾛｰﾙNo4
        mapping.put("INSATUROLLNO5", "insaturollno5"); //印刷ﾛｰﾙNo5
        mapping.put("INSATUHABASAVE", "insatuhabasave"); //印刷幅始め平均
        mapping.put("INSATUHABAEAVE", "insatuhabaeave"); //印刷幅終り平均
        mapping.put("MLD", "mld"); //MLD
        mapping.put("BIKO1", "biko1"); //備考1
        mapping.put("BIKO2", "biko2"); //備考2
        mapping.put("TANTOSYA", "tantosya"); //担当者ｺｰﾄﾞ
        mapping.put("pkokeibun1", "pkokeibun1"); //ﾍﾟｰｽﾄ固形分1
        mapping.put("pastelotno2", "pastelotno2"); //ﾍﾟｰｽﾄﾛｯﾄNo2
        mapping.put("pastenendo2", "pastenendo2"); //ﾍﾟｰｽﾄ粘度2
        mapping.put("pasteondo2", "pasteondo2"); //ﾍﾟｰｽﾄ温度2
        mapping.put("pkokeibun2", "pkokeibun2"); //ﾍﾟｰｽﾄ固形分2
        mapping.put("petfilmsyurui", "petfilmsyurui"); //PETﾌｨﾙﾑ種類
        mapping.put("kansoondo2", "kansoondo2"); //乾燥温度表示値2
        mapping.put("kansoondo3", "kansoondo3"); //乾燥温度表示値3
        mapping.put("kansoondo4", "kansoondo4"); //乾燥温度表示値4
        mapping.put("kansoondo5", "kansoondo5"); //乾燥温度表示値5
        mapping.put("seihanmei", "seihanmei"); //製版名
        mapping.put("makuatsu_ave_start", "makuatsuAveStart"); //印刷ｽﾀｰﾄ膜厚AVE
        mapping.put("makuatsu_max_start", "makuatsuMaxStart"); //印刷ｽﾀｰﾄ膜厚MAX
        mapping.put("makuatsu_min_start", "makuatsuMinStart"); //印刷ｽﾀｰﾄ膜厚MIN
        mapping.put("makuatucv_start", "makuatuCvStart"); //印刷ｽﾀｰﾄ膜厚CV
        mapping.put("nijimikasure_start", "nijimikasureStart"); //ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
        mapping.put("nijimikasure_end", "nijimikasureEnd"); //終了時ﾆｼﾞﾐ・ｶｽﾚ確認
        mapping.put("tanto_end", "tantoEnd"); //印刷ｴﾝﾄﾞ時担当者
        mapping.put("printmaisuu", "printmaisuu"); //印刷枚数
        mapping.put("kansouroatsu", "kansouroatsu"); //乾燥炉圧
        mapping.put("printhaba", "printhaba"); //印刷幅
        mapping.put("table_clearrance", "tableClearrance"); //ﾃｰﾌﾞﾙｸﾘｱﾗﾝｽ
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); //更新日時
        mapping.put("tokuisaki", "tokuisaki"); //客先
        mapping.put("lotkubuncode", "lotkubuncode"); //ﾛｯﾄ区分
        mapping.put("ownercode", "ownercode"); //ｵｰﾅｰ
        mapping.put("seikeinagasa", "seikeinagasa"); //成形長さ
        mapping.put("genryou", "genryou"); //原料
        mapping.put("eatumi", "eatumi"); //電極厚み
        mapping.put("epaste", "epaste"); //電極ﾍﾟｰｽﾄ
        mapping.put("pattern", "pattern"); //電極製版仕様
        mapping.put("scraperspeed", "scraperspeed"); //ｽｸﾚｯﾊﾟｰ速度
        mapping.put("startitikakunin", "startitikakunin"); //印刷位置確認
        mapping.put("kakuninsya", "kakuninsya"); //印刷ｽﾀｰﾄ確認者
        mapping.put("senkoulotno", "senkoulotno"); // 先行ﾛｯﾄNo
        mapping.put("tapetsukaikiri", "tapetsukaikiri"); // ﾃｰﾌﾟ使い切り
        mapping.put("jilothe", "jilothe"); // 次ﾛｯﾄへ
        mapping.put("bikou3", "bikou3"); //備考3
        mapping.put("bikou4", "bikou4"); //備考4
        mapping.put("bikou5", "bikou5"); //備考5
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrRsusprn>> beanHandler = new BeanListHandler<>(SrRsusprn.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [印刷RSUS_ｻﾌﾞ画面]からデータを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrRsusprn> loadSubSrRsusprn(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT kojyo,lotno,edaban,makuatsu_start1,makuatsu_start2,makuatsu_start3,makuatsu_start4,makuatsu_start5,"
                + "insatuhaba_start1,insatuhaba_start2,insatuhaba_start3,insatuhaba_start4,insatuhaba_start5,torokunichiji,"
                + "kosinnichiji,revision,0 AS deleteflag "
                + "FROM sub_sr_rsusprn "
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
        mapping.put("insatuhaba_start1", "insatuhabaStart1"); //印刷幅ｽﾀｰﾄ1
        mapping.put("insatuhaba_start2", "insatuhabaStart2"); //印刷幅ｽﾀｰﾄ2
        mapping.put("insatuhaba_start3", "insatuhabaStart3"); //印刷幅ｽﾀｰﾄ3
        mapping.put("insatuhaba_start4", "insatuhabaStart4"); //印刷幅ｽﾀｰﾄ4
        mapping.put("insatuhaba_start5", "insatuhabaStart5"); //印刷幅ｽﾀｰﾄ5
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); //更新日時
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SubSrRsusprn>> beanHandler = new BeanListHandler<>(SubSrRsusprn.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [印刷RSUS_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrRsusprn> loadTmpSrRsusprn(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        String sql = "SELECT KOJYO,LOTNO,EDABAN,KCPNO,TAPESYURUI,TAPELOTNO,TapeSlipKigo,GENRYOKIGO,KAISINICHIJI,"
                + "SYURYONICHIJI,GOKI,SKEEGENO,SKEEGEMAISUU,SKEEGESPEED,KANSOONDO,CLEARANCE,SAATU,MAKUATU1,"
                + "SEIHANNO,SEIHANMAISUU,saidaisyorisuu,ruikeisyorisuu,PASTELOTNO,PASTENENDO,PASTEONDO,INSATUROLLNO,INSATUROLLNO2,INSATUROLLNO3,"
                + "INSATUROLLNO4,INSATUROLLNO5,INSATUHABASAVE,INSATUHABAEAVE,MLD,BIKO1,BIKO2,TOROKUNICHIJI,KOSINNICHIJI,TANTOSYA,pkokeibun1,"
                + "pastelotno2,pastenendo2,pasteondo2,pkokeibun2,petfilmsyurui,kansoondo2,kansoondo3,kansoondo4,"
                + "kansoondo5,seihanmei,makuatsu_ave_start,makuatsu_max_start,makuatsu_min_start,makuatucv_start,"
                + "nijimikasure_start,nijimikasure_end,tanto_end,printmaisuu,kansouroatsu,printhaba,table_clearrance,"
                + "tokuisaki,lotkubuncode,ownercode,seikeinagasa,genryou,eatumi,epaste,pattern,scraperspeed,startitikakunin,"
                + "kakuninsya,CONCAT(skojyo , slotno , sedaban) senkoulotno,tapetsukaikiri,jilothe,bikou3,bikou4,bikou5,revision,deleteflag "
                + "FROM tmp_sr_rsusprn "
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
        mapping.put("TAPESYURUI", "tapesyurui"); //ﾃｰﾌﾟ種類
        mapping.put("TAPELOTNO", "tapelotno"); //ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
        mapping.put("TapeSlipKigo", "tapeSlipKigo"); //ﾃｰﾌﾟｽﾘｯﾌﾟ記号
        mapping.put("GENRYOKIGO", "genryoukigou"); //原料記号
        mapping.put("KAISINICHIJI", "kaisinichiji"); //開始日時
        mapping.put("SYURYONICHIJI", "syuryonichiji"); //終了日時
        mapping.put("GOKI", "goki"); //号機
        mapping.put("SKEEGENO", "skeegeno"); //ｽｷｰｼﾞNo
        mapping.put("SKEEGEMAISUU", "skeegemaisuu"); //ｽｷｰｼﾞ枚数
        mapping.put("SKEEGESPEED", "skeegespeed"); //ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
        mapping.put("KANSOONDO", "kansoondo"); //乾燥温度
        mapping.put("CLEARANCE", "clearance"); //ｸﾘｱﾗﾝｽ設定値
        mapping.put("SAATU", "saatu"); //差圧
        mapping.put("MAKUATU1", "makuatu1"); //膜厚1
        mapping.put("SEIHANNO", "seihanno"); //製版No
        mapping.put("SEIHANMAISUU", "seihanmaisuu"); //製版枚数
        mapping.put("saidaisyorisuu", "saidaisyorisuu"); //最大処理数
        mapping.put("ruikeisyorisuu", "ruikeisyorisuu"); //累計処理数
        mapping.put("PASTELOTNO", "pastelotno"); //ﾍﾟｰｽﾄﾛｯﾄNo
        mapping.put("PASTENENDO", "pastenendo"); //ﾍﾟｰｽﾄ粘度
        mapping.put("PASTEONDO", "pasteondo"); //ﾍﾟｰｽﾄ温度
        mapping.put("INSATUROLLNO", "insaturollno"); //印刷ﾛｰﾙNo1
        mapping.put("INSATUROLLNO2", "insaturollno2"); //印刷ﾛｰﾙNo2
        mapping.put("INSATUROLLNO3", "insaturollno3"); //印刷ﾛｰﾙNo3
        mapping.put("INSATUROLLNO4", "insaturollno4"); //印刷ﾛｰﾙNo4
        mapping.put("INSATUROLLNO5", "insaturollno5"); //印刷ﾛｰﾙNo5
        mapping.put("INSATUHABASAVE", "insatuhabasave"); //印刷幅始め平均
        mapping.put("INSATUHABAEAVE", "insatuhabaeave"); //印刷幅終り平均
        mapping.put("MLD", "mld"); //MLD
        mapping.put("BIKO1", "biko1"); //備考1
        mapping.put("BIKO2", "biko2"); //備考2
        mapping.put("TANTOSYA", "tantosya"); //担当者ｺｰﾄﾞ
        mapping.put("pkokeibun1", "pkokeibun1"); //ﾍﾟｰｽﾄ固形分1
        mapping.put("pastelotno2", "pastelotno2"); //ﾍﾟｰｽﾄﾛｯﾄNo2
        mapping.put("pastenendo2", "pastenendo2"); //ﾍﾟｰｽﾄ粘度2
        mapping.put("pasteondo2", "pasteondo2"); //ﾍﾟｰｽﾄ温度2
        mapping.put("pkokeibun2", "pkokeibun2"); //ﾍﾟｰｽﾄ固形分2
        mapping.put("petfilmsyurui", "petfilmsyurui"); //PETﾌｨﾙﾑ種類
        mapping.put("kansoondo2", "kansoondo2"); //乾燥温度表示値2
        mapping.put("kansoondo3", "kansoondo3"); //乾燥温度表示値3
        mapping.put("kansoondo4", "kansoondo4"); //乾燥温度表示値4
        mapping.put("kansoondo5", "kansoondo5"); //乾燥温度表示値5
        mapping.put("seihanmei", "seihanmei"); //製版名
        mapping.put("makuatsu_ave_start", "makuatsuAveStart"); //印刷ｽﾀｰﾄ膜厚AVE
        mapping.put("makuatsu_max_start", "makuatsuMaxStart"); //印刷ｽﾀｰﾄ膜厚MAX
        mapping.put("makuatsu_min_start", "makuatsuMinStart"); //印刷ｽﾀｰﾄ膜厚MIN
        mapping.put("makuatucv_start", "makuatuCvStart"); //印刷ｽﾀｰﾄ膜厚CV
        mapping.put("nijimikasure_start", "nijimikasureStart"); //ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
        mapping.put("nijimikasure_end", "nijimikasureEnd"); //終了時ﾆｼﾞﾐ・ｶｽﾚ確認
        mapping.put("tanto_end", "tantoEnd"); //印刷ｴﾝﾄﾞ時担当者
        mapping.put("printmaisuu", "printmaisuu"); //印刷枚数
        mapping.put("kansouroatsu", "kansouroatsu"); //乾燥炉圧
        mapping.put("printhaba", "printhaba"); //印刷幅
        mapping.put("table_clearrance", "tableClearrance"); //ﾃｰﾌﾞﾙｸﾘｱﾗﾝｽ
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); //更新日時
        mapping.put("tokuisaki", "tokuisaki"); //客先
        mapping.put("lotkubuncode", "lotkubuncode"); //ﾛｯﾄ区分
        mapping.put("ownercode", "ownercode"); //ｵｰﾅｰ
        mapping.put("seikeinagasa", "seikeinagasa"); //成形長さ
        mapping.put("genryou", "genryou"); //原料
        mapping.put("eatumi", "eatumi"); //電極厚み
        mapping.put("epaste", "epaste"); //電極ﾍﾟｰｽﾄ
        mapping.put("pattern", "pattern"); //電極製版仕様
        mapping.put("scraperspeed", "scraperspeed"); //ｽｸﾚｯﾊﾟｰ速度
        mapping.put("startitikakunin", "startitikakunin"); //印刷位置確認
        mapping.put("kakuninsya", "kakuninsya"); //印刷ｽﾀｰﾄ確認者
        mapping.put("senkoulotno", "senkoulotno"); // 先行ﾛｯﾄNo
        mapping.put("tapetsukaikiri", "tapetsukaikiri"); // ﾃｰﾌﾟ使い切り
        mapping.put("jilothe", "jilothe"); // 次ﾛｯﾄへ
        mapping.put("bikou3", "bikou3"); //備考3
        mapping.put("bikou4", "bikou4"); //備考4
        mapping.put("bikou5", "bikou5"); //備考5
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrRsusprn>> beanHandler = new BeanListHandler<>(SrRsusprn.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [印刷RSUS_ｻﾌﾞ画面_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrRsusprn> loadTmpSubSrRsusprn(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT kojyo,lotno,edaban,makuatsu_start1,makuatsu_start2,makuatsu_start3,makuatsu_start4,makuatsu_start5,"
                + "insatuhaba_start1,insatuhaba_start2,insatuhaba_start3,insatuhaba_start4,insatuhaba_start5,torokunichiji,"
                + "kosinnichiji,revision,deleteflag "
                + "FROM tmp_sub_sr_rsusprn "
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
        mapping.put("insatuhaba_start1", "insatuhabaStart1"); //印刷幅ｽﾀｰﾄ1
        mapping.put("insatuhaba_start2", "insatuhabaStart2"); //印刷幅ｽﾀｰﾄ2
        mapping.put("insatuhaba_start3", "insatuhabaStart3"); //印刷幅ｽﾀｰﾄ3
        mapping.put("insatuhaba_start4", "insatuhabaStart4"); //印刷幅ｽﾀｰﾄ4
        mapping.put("insatuhaba_start5", "insatuhabaStart5"); //印刷幅ｽﾀｰﾄ5
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); //更新日時
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SubSrRsusprn>> beanHandler = new BeanListHandler<>(SubSrRsusprn.class, rowProcessor);

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
            
            // 印刷RSUSデータ取得
            List<SrRsusprn> srRsusprnDataList = getSrRsusprnData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban);
            if (srRsusprnDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // 印刷RSUS_ｻﾌﾞ画面データ取得
            List<SubSrRsusprn> subSrRsusprnDataList = getSubSrRsusprnData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban);
            if (subSrRsusprnDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // メイン画面データ設定
            setInputItemDataMainForm(processData, srRsusprnDataList.get(0));

            // 膜厚入力画面データ設定 ※工場ｺｰﾄﾞ、ﾛｯﾄNo、枝番は親ではなく自身の値を渡す。
            //   →子画面側処理で自身の枝番を保持しておく必要がある。データ自体は親データの枝番で検索済みのものを引き渡す。
            setInputItemDataSubFormC004(subSrRsusprnDataList.get(0));

            // 印刷幅入力画面データ設定
            setInputItemDataSubFormC005(subSrRsusprnDataList.get(0));
            
            // 前工程WIP取込画面データ設定
            // ※膜厚入力画面とは異なり、下記メソッド内で親データの検索を実行しているため親データの枝番、状態フラグを引き渡す。
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
     * @param listData 項目リスト
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
     * @param srRsusprnData 印刷RSUSデータ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrRsusprn srRsusprnData) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srRsusprnData != null) {
            return getSrRsusprnItemData(itemId, srRsusprnData);
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
                + "edaban,jissekino,jotai_flg,tsuika_kotei_flg,tmp_kbn"
                + ") VALUES ("
                + "?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?) ";

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
        if (JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg)) {
            params.add(null); //仮登録区分
        } else {
            params.add(99); //仮登録区分
        }

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
     * @param systemTime システム日付
     * @throws SQLException 例外エラー
     */
    private void updateFxhdd03(QueryRunner queryRunnerDoc, Connection conDoc, String tantoshaCd, String formId, BigDecimal rev,
            String kojyo, String lotNo, String edaban, String jotaiFlg, Timestamp systemTime) throws SQLException {

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        boolean isFlowButtonClik = Boolean.parseBoolean(StringUtil.nullToBlank(session.getAttribute("isFlowButtonClik")));
        
        String sql = "UPDATE fxhdd03 SET "
                + "koshinsha = ?, koshin_date = ?,"
                + "rev = ?, jotai_flg = ? "
                + "WHERE gamen_id = ? AND kojyo = ? "
                + "  AND lotno = ? AND edaban = ? "
                + "  AND jissekino = 1  ";
        if (!JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg) || isFlowButtonClik) {
            sql = "UPDATE fxhdd03 SET "
                + "koshinsha = ?, koshin_date = ?,"
                + "rev = ?, jotai_flg = ?, tmp_kbn = ? " 
                + "WHERE gamen_id = ? AND kojyo = ? "
                + "  AND lotno = ? AND edaban = ? "
                + "  AND jissekino = 1  ";
        }

        List<Object> params = new ArrayList<>();
        // 更新内容
        params.add(tantoshaCd); //更新者
        params.add(systemTime); //更新日
        params.add(rev); //revision
        params.add(jotaiFlg); //状態ﾌﾗｸﾞ
        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            params.add(99); //仮登録区分
        } else if (JOTAI_FLG_SAKUJO.equals(jotaiFlg)) {
            params.add(0); //仮登録区分
        } else {
            if (isFlowButtonClik) {
                params.add(getTmpKbn(queryRunnerDoc)); //仮登録区分
            }
        }

        // 検索条件
        params.add(formId); //画面ID
        params.add(kojyo); //工場ｺｰﾄﾞ
        params.add(lotNo); //ﾛｯﾄNo
        params.add(edaban); //枝番

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerDoc.update(conDoc, sql, params.toArray());
    }

    /**
     * 印刷RSUS_仮登録(tmp_sr_rsusprn)登録処理
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
    private void insertTmpSrRsusprn(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, ProcessData processData) throws SQLException {
        String sql = "INSERT INTO tmp_sr_rsusprn ("
                + "KOJYO,LOTNO,EDABAN,KCPNO,TAPESYURUI,TAPELOTNO,TapeSlipKigo,GENRYOKIGO,KAISINICHIJI,SYURYONICHIJI,GOKI,SKEEGENO,"
                + "SKEEGEMAISUU,SKEEGESPEED,KANSOONDO,CLEARANCE,SAATU,MAKUATU1,SEIHANNO,SEIHANMAISUU,saidaisyorisuu,ruikeisyorisuu,PASTELOTNO,PASTENENDO,PASTEONDO,"
                + "INSATUROLLNO,INSATUROLLNO2,INSATUROLLNO3,INSATUROLLNO4,INSATUROLLNO5,INSATUHABASAVE,INSATUHABAEAVE,MLD,BIKO1,BIKO2,TOROKUNICHIJI,KOSINNICHIJI,"
                + "TANTOSYA,pkokeibun1,pastelotno2,pastenendo2,pasteondo2,pkokeibun2,petfilmsyurui,kansoondo2,kansoondo3,kansoondo4,"
                + "kansoondo5,seihanmei,makuatsu_ave_start,makuatsu_max_start,makuatsu_min_start,makuatucv_start,nijimikasure_start,"
                + "nijimikasure_end,tanto_end,printmaisuu,kansouroatsu,printhaba,table_clearrance,tokuisaki,lotkubuncode,ownercode,"
                + "seikeinagasa,genryou,eatumi,epaste,pattern,scraperspeed,startitikakunin,kakuninsya,skojyo,slotno,sedaban,tapetsukaikiri,"
                + "jilothe,bikou3,bikou4,bikou5,revision,deleteflag "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
                + ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        List<Object> params = setUpdateParameterTmpSrRsusprn(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, processData, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷RSUS_仮登録(tmp_sr_rsusprn)更新処理
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
     * @param processData プロセスデータ
     * @throws SQLException 例外エラー
     */
    private void updateTmpSrRsusprn(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, ProcessData processData) throws SQLException {
        String sql = "UPDATE tmp_sr_rsusprn SET "
                + "KCPNO = ?,TAPELOTNO = ?,GENRYOKIGO = ?,KAISINICHIJI = ?,SYURYONICHIJI = ?,"
                + "GOKI = ?,SKEEGENO = ?,SKEEGEMAISUU = ?,SKEEGESPEED = ?,KANSOONDO = ?,SAATU = ?,SEIHANNO = ?,SEIHANMAISUU = ?,saidaisyorisuu = ?,ruikeisyorisuu = ?,PASTELOTNO = ?,PASTENENDO = ?,PASTEONDO = ?,"
                + "INSATUROLLNO = ?,INSATUROLLNO2 = ?,INSATUROLLNO3 = ?,MLD = ?,BIKO1 = ?,BIKO2 = ?,KOSINNICHIJI = ?,TANTOSYA = ?,pkokeibun1 = ?,"
                + "pastelotno2 = ?,pastenendo2 = ?,pasteondo2 = ?,pkokeibun2 = ?,petfilmsyurui = ?,kansoondo2 = ?,kansoondo3 = ?,"
                + "kansoondo4 = ?,kansoondo5 = ?,seihanmei = ?,makuatsu_ave_start = ?,makuatsu_max_start = ?,makuatsu_min_start = ?,"
                + "makuatucv_start = ?,nijimikasure_start = ?,nijimikasure_end = ?,tanto_end = ?,printmaisuu = ?,kansouroatsu = ?,"
                + "printhaba = ?,table_clearrance = ?,tokuisaki = ?,lotkubuncode = ?,ownercode = ?,seikeinagasa = ?,genryou = ?,"
                + "eatumi = ?,epaste = ?,pattern = ?,scraperspeed = ?,startitikakunin = ?,kakuninsya = ?,skojyo = ?,slotno = ?,"
                + "sedaban = ?,tapetsukaikiri = ?,jilothe = ?,bikou3 = ?,bikou4 = ?,bikou5 = ?,revision = ?,deleteflag = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrRsusprn> srSrRsusprnList = getSrRsusprnData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrRsusprn srRsusprn = null;
        if (!srSrRsusprnList.isEmpty()) {
            srRsusprn = srSrRsusprnList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrRsusprn(false, newRev, 0, "", "", "", systemTime, processData, srRsusprn);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷RSUS_仮登録(tmp_sr_rsusprn)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrRsusprn(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sr_rsusprn "
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
     * 印刷RSUS_仮登録(tmp_sr_rsusprn)更新値ﾊﾟﾗﾒｰﾀ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime ｼｽﾃﾑ日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srRsusprnData 印刷RSUSデータ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrRsusprn(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo, String lotNo,
            String edaban, Timestamp systemTime, ProcessData processData, SrRsusprn srRsusprnData) {
        List<Object> params = new ArrayList<>();

        List<FXHDD01> itemList = processData.getItemList();
        Map hiddenDataMap = processData.getHiddenDataMap();
        
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        
        // 先行ﾛｯﾄNo
        String senkoulotno = getItemData(itemList, GXHDO101B003Const.SENKOULOTNO, srRsusprnData);
        String skKojyo = ""; //先行工場ｺｰﾄﾞ
        String skLotNo = ""; // 先行ﾛｯﾄNo
        String skEdaban = ""; // 先行枝番
        if (!StringUtil.isEmpty(senkoulotno)) {
            skKojyo = StringUtils.substring(senkoulotno, 0, 3);
            skLotNo = StringUtils.substring(senkoulotno, 3, 11);
            skEdaban = StringUtil.blankToNull(StringUtils.substring(senkoulotno, 11, 14));
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.KCPNO, srRsusprnData))); //KCPNo
        if (isInsert) {
            params.add(null); //ﾃｰﾌﾟ種類
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.SLIP_LOTNO, srRsusprnData))); //ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
        if (isInsert) {
            params.add(null); //ﾃｰﾌﾟｽﾘｯﾌﾟ記号
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.GENRYO_KIGOU, srRsusprnData))); //原料記号
        params.add(DBUtil.stringToDateObjectDefaultNull(
                getItemData(itemList, GXHDO101B003Const.INSATSU_KAISHI_DAY, srRsusprnData),
                getItemData(itemList, GXHDO101B003Const.INSATSU_KAISHI_TIME, srRsusprnData))); //開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(
                getItemData(itemList, GXHDO101B003Const.INSATSU_SHUURYOU_DAY, srRsusprnData),
                getItemData(itemList, GXHDO101B003Const.INSATSU_SHUURYOU_TIME, srRsusprnData))); //終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.INSATSU_GOUKI, srRsusprnData))); //号機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.SQUEEGEE_OR_ATSUDOU_NO, srRsusprnData))); //ｽｷｰｼﾞﾛｯﾄNo
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.SQUEEGEE_MAISU, srRsusprnData))); //ｽｷｰｼﾞ枚数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.SQUEEGEE_SPEED, srRsusprnData))); //ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.KANSOU_ONDO_HYOUJICHI1, srRsusprnData))); //乾燥温度表示値1
        if (isInsert) {
            params.add(null); //ｸﾘｱﾗﾝｽ設定値
        }   
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.SAATSU, srRsusprnData))); //差圧
        if (isInsert) {
            params.add(null); //膜厚
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.SEIHAN_OR_HANDOU_NO, srRsusprnData))); //製版No
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.SEIHAN_OR_HANDOU_SHIYOU_MAISUU, srRsusprnData))); //製版枚数

        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.SAIDAISYORISUU, srRsusprnData))); //最大処理数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.RUIKEISYORISUU, srRsusprnData))); //累計処理数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.PASTE_LOT_NO1, srRsusprnData))); //ﾍﾟｰｽﾄﾛｯﾄNo1
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.PASTE_NENDO1, srRsusprnData))); //ﾍﾟｰｽﾄ粘度1
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.PASTE_ONDO1, srRsusprnData))); //ﾍﾟｰｽﾄ温度1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.ROLL_NO1, srRsusprnData))); //印刷ﾛｰﾙNo1
        params.add(null); //印刷ﾛｰﾙNo2
        params.add(null);//印刷ﾛｰﾙNo3
        if (isInsert) {
            params.add(null); //印刷ﾛｰﾙNo4
            params.add(null); //印刷ﾛｰﾙNo5
            params.add(null); //印刷幅始め平均
            params.add(null); //印刷幅終り平均
        }

        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.MLD, srRsusprnData))); //MLD
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.BIKOU1, srRsusprnData))); //備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.BIKOU2, srRsusprnData))); //備考2
        if (isInsert) {
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時
        } else {
            params.add(systemTime); //更新日時
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.INSATSU_STARTJI_TANTOUSHA, srRsusprnData))); //担当者ｺｰﾄﾞ
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.PASTE_KOKEIBUN1, srRsusprnData))); //ﾍﾟｰｽﾄ固形分1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.PASTE_LOT_NO2, srRsusprnData))); //ﾍﾟｰｽﾄﾛｯﾄNo2
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.PASTE_NENDO2, srRsusprnData))); //ﾍﾟｰｽﾄ粘度2
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.PASTE_ONDO2, srRsusprnData))); //ﾍﾟｰｽﾄ温度2
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.PASTE_KOKEIBUN2, srRsusprnData))); //ﾍﾟｰｽﾄ固形分2        
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.PET_FILM_SHURUI, srRsusprnData))); //PETﾌｨﾙﾑ種類
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.KANSOU_ONDO_HYOUJICHI2, srRsusprnData))); //乾燥温度2
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.KANSOU_ONDO_HYOUJICHI3, srRsusprnData))); //乾燥温度3
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.KANSOU_ONDO_HYOUJICHI4, srRsusprnData))); //乾燥温度4
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.KANSOU_ONDO_HYOUJICHI5, srRsusprnData))); //乾燥温度5
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.SEIHAN_OR_HANDOU_MEI, srRsusprnData))); //製版名
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.INSATSU_START_MAKUATSU_AVE, srRsusprnData))); //ｽﾀｰﾄ時膜厚AVE
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.INSATSU_START_MAKUATSU_MAX, srRsusprnData))); //ｽﾀｰﾄ時膜厚MAX
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.INSATSU_START_MAKUATSU_MIN, srRsusprnData))); //ｽﾀｰﾄ時膜厚MIN
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.INSATSU_START_MAKUATSU_CV, srRsusprnData))); //印刷ｽﾀｰﾄ膜厚CV
        //ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B003Const.STARTJI_NIJIMI_KASURE_CHECK, srRsusprnData))) {
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
        //終了時ﾆｼﾞﾐ・ｶｽﾚ確認
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B003Const.SHUURYOU_JI_NIJIMI_KASURE_CHECK, srRsusprnData))) {
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
        
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.INSATSU_ENDJI_TANTOUSHA, srRsusprnData))); //印刷ｴﾝﾄﾞ時担当者
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.INSATSU_MAISUU, srRsusprnData))); //印刷枚数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.KANSOU_RO_ATSU, srRsusprnData))); //乾燥炉圧
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.INSATSU_HABA, srRsusprnData))); //印刷幅
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.TABLE_CLEARANCE, srRsusprnData))); //テーブルクリアランス
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.KYAKUSAKI, srRsusprnData))); //客先
        params.add(DBUtil.stringToStringObjectDefaultNull(StringUtil.nullToBlank(hiddenDataMap.get("lotkubuncode")))); //ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObjectDefaultNull(StringUtil.nullToBlank(hiddenDataMap.get("ownercode")))); //ｵｰﾅｰ
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.SEIKEINAGASA, srRsusprnData))); //成形長さ
        params.add(DBUtil.stringToStringObjectDefaultNull(StringUtil.nullToBlank(hiddenDataMap.get("genryou")))); //原料
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(StringUtil.nullToBlank(hiddenDataMap.get("eatumi")))); //電極厚み
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.DENKYOKU_PASTE, srRsusprnData))); //電極ﾍﾟｰｽﾄ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.DENKYOKU_SEIHAN_SHIYOU, srRsusprnData))); //電極製版仕様
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.SCRAPER_SPEED, srRsusprnData))); //ｽｸﾚｯﾊﾟｰ速度
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.STARTITIKAKUNIN, srRsusprnData))); //印刷位置確認
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.INSATSU_STARTJI_KAKUNINSYA, srRsusprnData))); //印刷ｽﾀｰﾄ確認者
        params.add(DBUtil.stringToStringObjectDefaultNull(skKojyo)); // 先行工場ｺｰﾄﾞ
        params.add(DBUtil.stringToStringObjectDefaultNull(skLotNo)); // 先行ﾛｯﾄNo
        params.add(DBUtil.stringToStringObjectDefaultNull(skEdaban)); // 先行枝番
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B003Const.TAPETSUKAIKIRI, srRsusprnData),null)); // ﾃｰﾌﾟ使い切り
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B003Const.JILOTHE, srRsusprnData),null)); // 次ﾛｯﾄへ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.BIKOU3, srRsusprnData))); //備考3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.BIKOU4, srRsusprnData))); //備考4
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B003Const.BIKOU5, srRsusprnData))); //備考5
        
        params.add(newRev); //revision
        params.add(deleteflag); //削除ﾌﾗｸﾞ

        return params;
    }

    /**
     * 印刷RSUS_ｻﾌﾞ画面仮登録(tmp_sub_sr_rsusprn)登録処理
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
    private void insertTmpSubSrRsusprn(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        String sql = "INSERT INTO tmp_sub_sr_rsusprn ("
                + "kojyo,lotno,edaban,makuatsu_start1,makuatsu_start2,makuatsu_start3,makuatsu_start4,makuatsu_start5,"
                + "insatuhaba_start1,insatuhaba_start2,insatuhaba_start3,insatuhaba_start4,insatuhaba_start5,torokunichiji,"
                + "kosinnichiji,revision,deleteflag"
                + ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        List<Object> params = setUpdateParameterTmpSubSrRsusprn(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷RSUS_ｻﾌﾞ画面仮登録(tmp_sub_sr_rsusprn)更新処理
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
    private void updateTmpSubSrRsusprn(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        String sql = "UPDATE tmp_sub_sr_rsusprn SET "
                + "makuatsu_start1 = ?,makuatsu_start2 = ?,makuatsu_start3 = ?,makuatsu_start4 = ?,makuatsu_start5 = ?,"
                + "insatuhaba_start1 = ?,insatuhaba_start2 = ?,insatuhaba_start3 = ?,insatuhaba_start4 = ?,"
                + "insatuhaba_start5 = ?,kosinnichiji = ?,revision = ?,deleteflag = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ?";

        List<Object> params = setUpdateParameterTmpSubSrRsusprn(false, newRev, 0, "", "", "", systemTime);

        // 検索条件
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷RSUS_ｻﾌﾞ画面仮登録(tmp_sub_sr_rsusprn)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSubSrRsusprn(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {
        String sql = "DELETE FROM tmp_sub_sr_rsusprn "
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
     * 印刷RSUS_ｻﾌﾞ画面仮登録(tmp_sub_sr_rsusprn)更新値ﾊﾟﾗﾒｰﾀ設定
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
    private List<Object> setUpdateParameterTmpSubSrRsusprn(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, Timestamp systemTime) {
        List<Object> params = new ArrayList<>();

        // 子画面情報を取得
        GXHDO101C004 beanGXHDO101C004 = (GXHDO101C004) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C004);
        List<GXHDO101C004Model.MakuatsuData> makuatsuDataList = beanGXHDO101C004.getGxhdO101c004Model().getMakuatsuDataList();
        GXHDO101C005 beanGXHDO101C005 = (GXHDO101C005) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C005);
        List<GXHDO101C005Model.PrintWidthData> printWidthDataList = beanGXHDO101C005.getGxhdO101c005Model().getPrintWidthDataList();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(0).getStartVal())); //膜厚ｽﾀｰﾄ1
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(1).getStartVal())); //膜厚ｽﾀｰﾄ2
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(2).getStartVal())); //膜厚ｽﾀｰﾄ3
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(3).getStartVal())); //膜厚ｽﾀｰﾄ4
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(4).getStartVal())); //膜厚ｽﾀｰﾄ5
        params.add(DBUtil.stringToIntObjectDefaultNull(printWidthDataList.get(0).getStartVal())); //印刷幅ｽﾀｰﾄ1
        params.add(DBUtil.stringToIntObjectDefaultNull(printWidthDataList.get(1).getStartVal())); //印刷幅ｽﾀｰﾄ2
        params.add(DBUtil.stringToIntObjectDefaultNull(printWidthDataList.get(2).getStartVal())); //印刷幅ｽﾀｰﾄ3
        params.add(DBUtil.stringToIntObjectDefaultNull(printWidthDataList.get(3).getStartVal())); //印刷幅ｽﾀｰﾄ4
        params.add(DBUtil.stringToIntObjectDefaultNull(printWidthDataList.get(4).getStartVal())); //印刷幅ｽﾀｰﾄ5

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
     * 印刷RSUS(sr_rsusprn)登録処理
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
     * @param tmpSrRsusprn 仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrRsusprn(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, ProcessData processData, SrRsusprn tmpSrRsusprn) throws SQLException {
        String sql = "INSERT INTO sr_rsusprn ("
                + "KOJYO,LOTNO,EDABAN,KCPNO,TAPESYURUI,TAPELOTNO,TapeSlipKigo,GENRYOKIGO,KAISINICHIJI,SYURYONICHIJI,GOKI,SKEEGENO,"
                + "SKEEGEMAISUU,SKEEGESPEED,KANSOONDO,CLEARANCE,SAATU,MAKUATU1,SEIHANNO,SEIHANMAISUU,saidaisyorisuu,ruikeisyorisuu,PASTELOTNO,PASTENENDO,PASTEONDO,"
                + "INSATUROLLNO,INSATUROLLNO2,INSATUROLLNO3,INSATUROLLNO4,INSATUROLLNO5,INSATUHABASAVE,INSATUHABAEAVE,MLD,BIKO1,BIKO2,"
                + "TOROKUNICHIJI,KOSINNICHIJI,TANTOSYA,pkokeibun1,pastelotno2,pastenendo2,pasteondo2,pkokeibun2,petfilmsyurui,kansoondo2,kansoondo3,kansoondo4,"
                + "kansoondo5,seihanmei,makuatsu_ave_start,makuatsu_max_start,makuatsu_min_start,makuatucv_start,nijimikasure_start,"
                + "nijimikasure_end,tanto_end,printmaisuu,kansouroatsu,printhaba,table_clearrance,tokuisaki,lotkubuncode,ownercode,seikeinagasa,genryou,eatumi,"
                + "epaste,pattern,scraperspeed,startitikakunin,kakuninsya,skojyo,slotno,sedaban,tapetsukaikiri,jilothe,bikou3,bikou4,bikou5,revision"
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        List<Object> params = setUpdateParameterSrRsusprn(true, newRev, kojyo, lotNo, edaban, systemTime, processData, tmpSrRsusprn);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷RSUS(sr_rsusprn)更新処理
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
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void updateSrRsusprn(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, ProcessData processData) throws SQLException {
        String sql = "UPDATE sr_rsusprn SET "
                + "KCPNO = ?,TAPELOTNO = ?,GENRYOKIGO = ?,KAISINICHIJI = ?,SYURYONICHIJI = ?,"
                + "GOKI = ?,SKEEGENO = ?,SKEEGEMAISUU = ?, SKEEGESPEED = ?, KANSOONDO = ?, SAATU = ?, SEIHANNO = ?,SEIHANMAISUU = ?,saidaisyorisuu = ?,ruikeisyorisuu = ?,PASTELOTNO = ?,PASTENENDO = ?,PASTEONDO = ?,"
                + "INSATUROLLNO = ?,INSATUROLLNO2 = ?,INSATUROLLNO3 = ?,MLD = ?,BIKO1 = ?,BIKO2 = ?,KOSINNICHIJI = ?,TANTOSYA = ?,pkokeibun1 = ?,"
                + "pastelotno2 = ?,pastenendo2 = ?,pasteondo2 = ?,pkokeibun2 = ?,petfilmsyurui = ?,kansoondo2 = ?,kansoondo3 = ?,"
                + "kansoondo4 = ?,kansoondo5 = ?,seihanmei = ?,makuatsu_ave_start = ?,makuatsu_max_start = ?,makuatsu_min_start = ?,"
                + "makuatucv_start = ?,nijimikasure_start = ?,nijimikasure_end = ?,tanto_end = ?,printmaisuu = ?,kansouroatsu = ?,"
                + "printhaba = ?,table_clearrance = ?,tokuisaki = ?,lotkubuncode = ?,ownercode = ?,seikeinagasa = ?,genryou = ?,"
                + "eatumi = ?,epaste = ?,pattern = ?,scraperspeed = ?,startitikakunin = ?,kakuninsya = ?,skojyo = ?,slotno = ?,"
                + "sedaban = ?,tapetsukaikiri = ?,jilothe = ?,bikou3 = ?,bikou4 = ?,bikou5 = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ?";
              
        // 更新前の値を取得
        List<SrRsusprn> srSrRsusprnList = getSrRsusprnData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrRsusprn srRsusprn = null;
        if (!srSrRsusprnList.isEmpty()) {
            srRsusprn = srSrRsusprnList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrRsusprn(false, newRev, "", "", "", systemTime, processData, srRsusprn);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷RSUS(sr_rsusprn)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srRsusprnData 印刷RSUSデータ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrRsusprn(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo,
            String edaban, Timestamp systemTime, ProcessData processData, SrRsusprn srRsusprnData) {
        List<Object> params = new ArrayList<>();
        List<FXHDD01> itemList = processData.getItemList();
        Map hiddenDataMap = processData.getHiddenDataMap();
        
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        // 先行ﾛｯﾄNo
        String senkoulotno = getItemData(itemList, GXHDO101B003Const.SENKOULOTNO, srRsusprnData);
        String skKojyo = ""; //先行工場ｺｰﾄﾞ
        String skLotNo = ""; // 先行ﾛｯﾄNo
        String skEdaban = ""; // 先行枝番
        if (!StringUtil.isEmpty(senkoulotno)) {
            skKojyo = StringUtils.substring(senkoulotno, 0, 3);
            skLotNo = StringUtils.substring(senkoulotno, 3, 11);
            skEdaban = StringUtil.blankToNull(StringUtils.substring(senkoulotno, 11, 14));
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B003Const.KCPNO, srRsusprnData))); //KCPNo
        if (isInsert) {
            params.add(""); //ﾃｰﾌﾟ種類
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B003Const.SLIP_LOTNO, srRsusprnData))); //ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
        if (isInsert) {
            params.add(""); //ﾃｰﾌﾟｽﾘｯﾌﾟ記号
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B003Const.GENRYO_KIGOU, srRsusprnData))); //原料記号
        params.add(DBUtil.stringToDateObject(
                getItemData(itemList, GXHDO101B003Const.INSATSU_KAISHI_DAY, srRsusprnData),
                getItemData(itemList, GXHDO101B003Const.INSATSU_KAISHI_TIME, srRsusprnData))); //開始日時
        params.add(DBUtil.stringToDateObject(
                getItemData(itemList, GXHDO101B003Const.INSATSU_SHUURYOU_DAY, srRsusprnData),
                getItemData(itemList, GXHDO101B003Const.INSATSU_SHUURYOU_TIME, srRsusprnData))); //終了日時
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B003Const.INSATSU_GOUKI, srRsusprnData))); //号機
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B003Const.SQUEEGEE_OR_ATSUDOU_NO, srRsusprnData))); //ｽｷｰｼﾞﾛｯﾄNo
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B003Const.SQUEEGEE_MAISU, srRsusprnData))); //ｽｷｰｼﾞ枚数
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B003Const.SQUEEGEE_SPEED, srRsusprnData))); //ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B003Const.KANSOU_ONDO_HYOUJICHI1, srRsusprnData))); //乾燥温度表示値1
        if (isInsert) {
            params.add(0); //ｸﾘｱﾗﾝｽ設定値
        }
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B003Const.SAATSU, srRsusprnData))); //差圧
        if (isInsert) {
            params.add(0); //膜厚
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B003Const.SEIHAN_OR_HANDOU_NO, srRsusprnData))); //製版No
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B003Const.SEIHAN_OR_HANDOU_SHIYOU_MAISUU, srRsusprnData))); //製版枚数

        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B003Const.SAIDAISYORISUU, srRsusprnData))); //最大処理数
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B003Const.RUIKEISYORISUU, srRsusprnData))); //累計処理数
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B003Const.PASTE_LOT_NO1, srRsusprnData))); //ﾍﾟｰｽﾄﾛｯﾄNo1
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B003Const.PASTE_NENDO1, srRsusprnData))); //ﾍﾟｰｽﾄ粘度1
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B003Const.PASTE_ONDO1, srRsusprnData))); //ﾍﾟｰｽﾄ温度1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B003Const.ROLL_NO1, srRsusprnData))); //印刷ﾛｰﾙNo1
        params.add(""); //印刷ﾛｰﾙNo2
        params.add("");//印刷ﾛｰﾙNo3
        if (isInsert) {
            params.add(""); //印刷ﾛｰﾙNo4
            params.add(""); //印刷ﾛｰﾙNo5
            params.add(0); //印刷幅始め平均
            params.add(0); //印刷幅終り平均
        }

        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B003Const.MLD, srRsusprnData))); //MLD
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B003Const.BIKOU1, srRsusprnData))); //備考1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B003Const.BIKOU2, srRsusprnData))); //備考2
        if (isInsert) {
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時
        } else {
            params.add(systemTime); //更新日時
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B003Const.INSATSU_STARTJI_TANTOUSHA, srRsusprnData))); //担当者ｺｰﾄﾞ
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B003Const.PASTE_KOKEIBUN1, srRsusprnData))); //ﾍﾟｰｽﾄ固形分1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B003Const.PASTE_LOT_NO2, srRsusprnData))); //ﾍﾟｰｽﾄﾛｯﾄNo2
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B003Const.PASTE_NENDO2, srRsusprnData))); //ﾍﾟｰｽﾄ粘度2
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B003Const.PASTE_ONDO2, srRsusprnData))); //ﾍﾟｰｽﾄ温度2
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B003Const.PASTE_KOKEIBUN2, srRsusprnData))); //ﾍﾟｰｽﾄ固形分2        
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B003Const.PET_FILM_SHURUI, srRsusprnData))); //PETﾌｨﾙﾑ種類
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B003Const.KANSOU_ONDO_HYOUJICHI2, srRsusprnData))); //乾燥温度2
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B003Const.KANSOU_ONDO_HYOUJICHI3, srRsusprnData))); //乾燥温度3
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B003Const.KANSOU_ONDO_HYOUJICHI4, srRsusprnData))); //乾燥温度4
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B003Const.KANSOU_ONDO_HYOUJICHI5, srRsusprnData))); //乾燥温度5
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B003Const.SEIHAN_OR_HANDOU_MEI, srRsusprnData))); //製版名
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B003Const.INSATSU_START_MAKUATSU_AVE, srRsusprnData))); //ｽﾀｰﾄ時膜厚AVE
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B003Const.INSATSU_START_MAKUATSU_MAX, srRsusprnData))); //ｽﾀｰﾄ時膜厚MAX
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B003Const.INSATSU_START_MAKUATSU_MIN, srRsusprnData))); //ｽﾀｰﾄ時膜厚MIN
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B003Const.INSATSU_START_MAKUATSU_CV, srRsusprnData))); //印刷ｽﾀｰﾄ膜厚CV
        //ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B003Const.STARTJI_NIJIMI_KASURE_CHECK, srRsusprnData))) {
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
        //終了時ﾆｼﾞﾐ・ｶｽﾚ確認
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B003Const.SHUURYOU_JI_NIJIMI_KASURE_CHECK, srRsusprnData))) {
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
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B003Const.INSATSU_ENDJI_TANTOUSHA, srRsusprnData))); //印刷ｴﾝﾄﾞ時担当者
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B003Const.INSATSU_MAISUU, srRsusprnData))); //印刷枚数
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B003Const.KANSOU_RO_ATSU, srRsusprnData))); //乾燥炉圧
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B003Const.INSATSU_HABA, srRsusprnData))); //印刷幅
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B003Const.TABLE_CLEARANCE, srRsusprnData))); //テーブルクリアランス
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B003Const.KYAKUSAKI, srRsusprnData))); //客先
        params.add(DBUtil.stringToStringObject(StringUtil.nullToBlank(hiddenDataMap.get("lotkubuncode")))); //ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObject(StringUtil.nullToBlank(hiddenDataMap.get("ownercode")))); //ｵｰﾅｰ
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B003Const.SEIKEINAGASA, srRsusprnData))); //成形長さ
        params.add(DBUtil.stringToStringObject(StringUtil.nullToBlank(hiddenDataMap.get("genryou")))); //原料
        params.add(DBUtil.stringToBigDecimalObject(StringUtil.nullToBlank(hiddenDataMap.get("eatumi")))); //電極厚み
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B003Const.DENKYOKU_PASTE, srRsusprnData))); //電極ﾍﾟｰｽﾄ
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B003Const.DENKYOKU_SEIHAN_SHIYOU, srRsusprnData))); //電極製版仕様
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B003Const.SCRAPER_SPEED, srRsusprnData))); //ｽｸﾚｯﾊﾟｰ速度
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B003Const.STARTITIKAKUNIN, srRsusprnData))); //印刷位置確認
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B003Const.INSATSU_STARTJI_KAKUNINSYA, srRsusprnData))); //印刷ｽﾀｰﾄ確認者
        params.add(DBUtil.stringToStringObject(skKojyo)); //先行工場ｺｰﾄﾞ
        params.add(DBUtil.stringToStringObject(skLotNo)); //先行ﾛｯﾄNo
        params.add(DBUtil.stringToStringObject(skEdaban)); //先行枝番
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B003Const.TAPETSUKAIKIRI, srRsusprnData),9)); //ﾃｰﾌﾟ使い切り
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B003Const.JILOTHE, srRsusprnData),9)); //次ﾛｯﾄへ
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B003Const.BIKOU3, srRsusprnData))); //備考3
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B003Const.BIKOU4, srRsusprnData))); //備考4
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B003Const.BIKOU5, srRsusprnData))); //備考5
        
        params.add(newRev); //revision
        return params;
    }

    /**
     * 印刷RSUS(sr_rsusprn)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSrRsusprn(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sr_rsusprn "
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
     * 印刷RSUS_ｻﾌﾞ画面(sub_sr_rsusprn)登録処理
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
    private void insertSubSrRsusprn(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        String sql = "INSERT INTO sub_sr_rsusprn ("
                + "kojyo,lotno,edaban,makuatsu_start1,makuatsu_start2,makuatsu_start3,makuatsu_start4,makuatsu_start5,"
                + "insatuhaba_start1,insatuhaba_start2,insatuhaba_start3,insatuhaba_start4,insatuhaba_start5,torokunichiji,"
                + "kosinnichiji,revision"
                + ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        List<Object> params = setUpdateParameterSubSrRsusprn(true, newRev, kojyo, lotNo, edaban, systemTime);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷RSUS_ｻﾌﾞ画面(sub_sr_rsusprn)更新処理
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
    private void updateSubSrRsusprn(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        String sql = "UPDATE sub_sr_rsusprn SET "
                + "makuatsu_start1 = ?,makuatsu_start2 = ?,makuatsu_start3 = ?,makuatsu_start4 = ?,makuatsu_start5 = ?,"
                + "insatuhaba_start1 = ?,insatuhaba_start2 = ?,insatuhaba_start3 = ?,insatuhaba_start4 = ?,"
                + "insatuhaba_start5 = ?,kosinnichiji = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ?";

        List<Object> params = setUpdateParameterSubSrRsusprn(false, newRev, "", "", "", systemTime);

        // 検索条件
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷RSUS_ｻﾌﾞ画面登録(sub_sr_rsusprn)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @return 更新ﾊﾟﾗﾒｰﾀ
     */
    private List<Object> setUpdateParameterSubSrRsusprn(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo,
            String edaban, Timestamp systemTime) {
        List<Object> params = new ArrayList<>();

        // 子画面情報を取得
        GXHDO101C004 beanGXHDO101C004 = (GXHDO101C004) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C004);
        List<GXHDO101C004Model.MakuatsuData> makuatsuDataList = beanGXHDO101C004.getGxhdO101c004Model().getMakuatsuDataList();
        GXHDO101C005 beanGXHDO101C005 = (GXHDO101C005) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C005);
        List<GXHDO101C005Model.PrintWidthData> printWidthDataList = beanGXHDO101C005.getGxhdO101c005Model().getPrintWidthDataList();

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
        params.add(DBUtil.stringToIntObject(printWidthDataList.get(0).getStartVal())); //印刷幅ｽﾀｰﾄ1
        params.add(DBUtil.stringToIntObject(printWidthDataList.get(1).getStartVal())); //印刷幅ｽﾀｰﾄ2
        params.add(DBUtil.stringToIntObject(printWidthDataList.get(2).getStartVal())); //印刷幅ｽﾀｰﾄ3
        params.add(DBUtil.stringToIntObject(printWidthDataList.get(3).getStartVal())); //印刷幅ｽﾀｰﾄ4
        params.add(DBUtil.stringToIntObject(printWidthDataList.get(4).getStartVal())); //印刷幅ｽﾀｰﾄ5
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
     * 印刷RSUS_ｻﾌﾞ画面仮登録(tmp_sub_sr_rsusprn)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSubSrRsusprn(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {
        String sql = "DELETE FROM sub_sr_rsusprn "
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
     * [印刷RSUS_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_rsusprn "
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
     * 開始時間設定処理
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKaishiDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B003Const.INSATSU_KAISHI_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B003Const.INSATSU_KAISHI_TIME);
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
    public ProcessData setShuryouDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B003Const.INSATSU_SHUURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B003Const.INSATSU_SHUURYOU_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }

        processDate.setMethod("");
        return processDate;
    }

    /**
     * 日付(日、時間)の項目にフォーマットの日付(yyMMdd,HHmm)をセットする
     *
     * @param itemDay 日付項目(日)
     * @param itemTime 日付項目(時間)
     * @param setDateTime 設定時間
     */
    private void setDateTimeItem(FXHDD01 itemDay, FXHDD01 itemTime, Date setDateTime) {
        itemDay.setValue(new SimpleDateFormat("yyMMdd").format(setDateTime));
        itemTime.setValue(new SimpleDateFormat("HHmm").format(setDateTime));
    }

    /**
     * 【部材在庫情報】ﾎﾞﾀﾝ押下時処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doBuzaizaikojyohoSyori(ProcessData processData) {
        FXHDD01 itemSeihanLotNo = getItemRow(processData.getItemList(), GXHDO101B003Const.SEIHAN_OR_HANDOU_NO); // 製版ﾛｯﾄNo
        if(itemSeihanLotNo == null){
            processData.setMethod("");
            return processData;
        }
        List<FXHDD01> errFxhdd01List = Arrays.asList(itemSeihanLotNo);
        // 「製版ﾛｯﾄNo」が入力されていない場合
        if (StringUtil.isEmpty(itemSeihanLotNo.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            processData.getErrorMessageInfoList().add(MessageUtil.getErrorMessageInfo("XHD-000003", true, true, errFxhdd01List, itemSeihanLotNo.getLabel1()));
            return processData;
        } else {
            // 「製版ﾛｯﾄNo」が入力されている場合
            if (StringUtil.getLength(itemSeihanLotNo.getValue()) != 9) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                processData.getErrorMessageInfoList().add(MessageUtil.getErrorMessageInfo("XHD-000004", true, true, errFxhdd01List, itemSeihanLotNo.getLabel1(), "9"));
                return processData;
            }
        }
        QueryRunner queryRunnerMLA = new QueryRunner(processData.getDataSourceMLAServer());
        // 部材在庫ﾃﾞｰﾀ取得
        Map<String, Object> fmlad01Data = getFmlad01Data(queryRunnerMLA, itemSeihanLotNo.getValue());
        if (fmlad01Data == null || fmlad01Data.isEmpty()) {
            processData.getErrorMessageInfoList().add(MessageUtil.getErrorMessageInfo("XHD-000219", true, true, errFxhdd01List));
            return processData;
        }
        String siyoMaisu = StringUtil.nullToBlank(fmlad01Data.get("siyo_maisu")); // 使用枚数
        String saidaiSiyoMaisu = StringUtil.nullToBlank(fmlad01Data.get("saidai_siyo_maisu")); // 最大使用枚数
        FXHDD01 itemRuikeisyorisuu = getItemRow(processData.getItemList(), GXHDO101B003Const.RUIKEISYORISUU); // 累計処理数
        FXHDD01 itemSaidaisyorisuu = getItemRow(processData.getItemList(), GXHDO101B003Const.SAIDAISYORISUU); // 最大処理数
        setItemIntValue(itemRuikeisyorisuu, siyoMaisu);
        setItemIntValue(itemSaidaisyorisuu, saidaiSiyoMaisu);
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
     * 項目IDに該当するDBの値を取得する。
     *
     * @param itemId 項目ID
     * @param srRsusprnData 印刷RSUSデータ
     * @return DB値
     */
    private String getSrRsusprnItemData(String itemId, SrRsusprn srRsusprnData) {
        switch (itemId) {
            // KCPNO
            case GXHDO101B003Const.KCPNO:
                return StringUtil.nullToBlank(srRsusprnData.getKcpno());
            // ｽﾘｯﾌﾟﾛｯﾄNo
            case GXHDO101B003Const.SLIP_LOTNO:
                return StringUtil.nullToBlank(srRsusprnData.getTapelotno());
            // ﾛｰﾙNo1
            case GXHDO101B003Const.ROLL_NO1:
                return StringUtil.nullToBlank(srRsusprnData.getInsaturollno());
            // 原料記号
            case GXHDO101B003Const.GENRYO_KIGOU:
                return StringUtil.nullToBlank(srRsusprnData.getGenryoukigou());
            // ﾍﾟｰｽﾄﾛｯﾄNo1
            case GXHDO101B003Const.PASTE_LOT_NO1:
                return StringUtil.nullToBlank(srRsusprnData.getPastelotno());
            // ﾍﾟｰｽﾄ粘度1
            case GXHDO101B003Const.PASTE_NENDO1:
                return StringUtil.nullToBlank(srRsusprnData.getPastenendo());
            // ﾍﾟｰｽﾄ温度1
            case GXHDO101B003Const.PASTE_ONDO1:
                return StringUtil.nullToBlank(srRsusprnData.getPasteondo());
            // ﾍﾟｰｽﾄ固形分1
            case GXHDO101B003Const.PASTE_KOKEIBUN1:
                return StringUtil.nullToBlank(srRsusprnData.getPkokeibun1());
            // ﾍﾟｰｽﾄﾛｯﾄNo2
            case GXHDO101B003Const.PASTE_LOT_NO2:
                return StringUtil.nullToBlank(srRsusprnData.getPastelotno2());
            // ﾍﾟｰｽﾄ粘度2
            case GXHDO101B003Const.PASTE_NENDO2:
                return StringUtil.nullToBlank(srRsusprnData.getPastenendo2());
            // ﾍﾟｰｽﾄ温度2
            case GXHDO101B003Const.PASTE_ONDO2:
                return StringUtil.nullToBlank(srRsusprnData.getPasteondo2());
            // ﾍﾟｰｽﾄ固形分2
            case GXHDO101B003Const.PASTE_KOKEIBUN2:
                return StringUtil.nullToBlank(srRsusprnData.getPkokeibun2());
            // ＰＥＴフィルム種類
            case GXHDO101B003Const.PET_FILM_SHURUI:
                return StringUtil.nullToBlank(srRsusprnData.getPetfilmsyurui());
            // 印刷号機
            case GXHDO101B003Const.INSATSU_GOUKI:
                return StringUtil.nullToBlank(srRsusprnData.getGoki());
            // 乾燥温度表示値1
            case GXHDO101B003Const.KANSOU_ONDO_HYOUJICHI1:
                return StringUtil.nullToBlank(srRsusprnData.getKansoondo());
            // 乾燥温度表示値2
            case GXHDO101B003Const.KANSOU_ONDO_HYOUJICHI2:
                return StringUtil.nullToBlank(srRsusprnData.getKansoondo2());
            // 乾燥温度表示値3
            case GXHDO101B003Const.KANSOU_ONDO_HYOUJICHI3:
                return StringUtil.nullToBlank(srRsusprnData.getKansoondo3());
            // 乾燥温度表示値4
            case GXHDO101B003Const.KANSOU_ONDO_HYOUJICHI4:
                return StringUtil.nullToBlank(srRsusprnData.getKansoondo4());
            // 乾燥温度表示値5
            case GXHDO101B003Const.KANSOU_ONDO_HYOUJICHI5:
                return StringUtil.nullToBlank(srRsusprnData.getKansoondo5());
            // 製版名
            case GXHDO101B003Const.SEIHAN_OR_HANDOU_MEI:
                return StringUtil.nullToBlank(srRsusprnData.getSeihanmei());
            // 製版No
            case GXHDO101B003Const.SEIHAN_OR_HANDOU_NO:
                return StringUtil.nullToBlank(srRsusprnData.getSeihanno());
            // 製版使用枚数
            case GXHDO101B003Const.SEIHAN_OR_HANDOU_SHIYOU_MAISUU:
                return StringUtil.nullToBlank(srRsusprnData.getSeihanmaisuu());
            // 最大処理数
            case GXHDO101B003Const.SAIDAISYORISUU:
                return StringUtil.nullToBlank(srRsusprnData.getSaidaisyorisuu());
            // 累計処理数
            case GXHDO101B003Const.RUIKEISYORISUU:
                return StringUtil.nullToBlank(srRsusprnData.getRuikeisyorisuu());
            // ｽｷｰｼﾞNo
            case GXHDO101B003Const.SQUEEGEE_OR_ATSUDOU_NO:
                return StringUtil.nullToBlank(srRsusprnData.getSkeegeno());
            // 印刷開始日
            case GXHDO101B003Const.INSATSU_KAISHI_DAY:
                return DateUtil.formattedTimestamp(srRsusprnData.getKaisinichiji(), "yyMMdd");
            // 印刷開始時間
            case GXHDO101B003Const.INSATSU_KAISHI_TIME:
                return DateUtil.formattedTimestamp(srRsusprnData.getKaisinichiji(), "HHmm");
            // 印刷ｽﾀｰﾄ膜厚AVE
            case GXHDO101B003Const.INSATSU_START_MAKUATSU_AVE:
                return StringUtil.nullToBlank(srRsusprnData.getMakuatsuAveStart());
            // 印刷ｽﾀｰﾄ膜厚MAX
            case GXHDO101B003Const.INSATSU_START_MAKUATSU_MAX:
                return StringUtil.nullToBlank(srRsusprnData.getMakuatsuMaxStart());
            // 印刷ｽﾀｰﾄ膜厚MIN
            case GXHDO101B003Const.INSATSU_START_MAKUATSU_MIN:
                return StringUtil.nullToBlank(srRsusprnData.getMakuatsuMinStart());
            // 印刷ｽﾀｰﾄ膜厚CV
            case GXHDO101B003Const.INSATSU_START_MAKUATSU_CV:
                return StringUtil.nullToBlank(srRsusprnData.getMakuatuCvStart());
            // ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
            case GXHDO101B003Const.STARTJI_NIJIMI_KASURE_CHECK:
                switch (StringUtil.nullToBlank(srRsusprnData.getNijimikasureStart())) {
                    case "0":
                        return "NG";
                    case "1":
                        return "OK";
                    default:
                        return "";
                }
            // 印刷スタート時担当者
            case GXHDO101B003Const.INSATSU_STARTJI_TANTOUSHA:
                return StringUtil.nullToBlank(srRsusprnData.getTantosya());
            // 印刷終了日
            case GXHDO101B003Const.INSATSU_SHUURYOU_DAY:
                return DateUtil.formattedTimestamp(srRsusprnData.getSyuryonichiji(), "yyMMdd");
            // 印刷終了時刻
            case GXHDO101B003Const.INSATSU_SHUURYOU_TIME:
                return DateUtil.formattedTimestamp(srRsusprnData.getSyuryonichiji(), "HHmm");
            // 終了時ﾆｼﾞﾐ・ｶｽﾚ確認
            case GXHDO101B003Const.SHUURYOU_JI_NIJIMI_KASURE_CHECK:
                switch (StringUtil.nullToBlank(srRsusprnData.getNijimikasureEnd())) {
                    case "0":
                        return "NG";
                    case "1":
                        return "OK";
                    default:
                        return "";
                }
            // 印刷エンド時担当者
            case GXHDO101B003Const.INSATSU_ENDJI_TANTOUSHA:
                return StringUtil.nullToBlank(srRsusprnData.getTantoEnd());
            // 印刷枚数
            case GXHDO101B003Const.INSATSU_MAISUU:
                return StringUtil.nullToBlank(srRsusprnData.getPrintmaisuu());
            // 乾燥炉圧
            case GXHDO101B003Const.KANSOU_RO_ATSU:
                return StringUtil.nullToBlank(srRsusprnData.getKansouroatsu());
            // MLD
            case GXHDO101B003Const.MLD:
                return StringUtil.nullToBlank(srRsusprnData.getMld());
            // 印刷幅
            case GXHDO101B003Const.INSATSU_HABA:
                return StringUtil.nullToBlank(srRsusprnData.getPrinthaba());
            // ﾃｰﾌﾞﾙｸﾘｱﾗﾝｽ
            case GXHDO101B003Const.TABLE_CLEARANCE:
                return StringUtil.nullToBlank(srRsusprnData.getTableClearrance());
            //備考1
            case GXHDO101B003Const.BIKOU1:
                return StringUtil.nullToBlank(srRsusprnData.getBiko1());
            //備考2
            case GXHDO101B003Const.BIKOU2:
                return StringUtil.nullToBlank(srRsusprnData.getBiko2());
            //成形長さ
            case GXHDO101B003Const.SEIKEINAGASA:
                return StringUtil.nullToBlank(srRsusprnData.getSeikeinagasa());
            // ｽｷｰｼﾞ枚数
            case GXHDO101B003Const.SQUEEGEE_MAISU:
                return StringUtil.nullToBlank(srRsusprnData.getSkeegemaisuu());
            // ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
            case GXHDO101B003Const.SQUEEGEE_SPEED:
                return StringUtil.nullToBlank(srRsusprnData.getSkeegespeed());
            // ｽｸﾚｯﾊﾟｰｽﾋﾟｰﾄﾞ
            case GXHDO101B003Const.SCRAPER_SPEED:
                return StringUtil.nullToBlank(srRsusprnData.getScraperspeed());
            // 差圧
            case GXHDO101B003Const.SAATSU:
                return StringUtil.nullToBlank(srRsusprnData.getSaatu());
            // 印刷位置確認
            case GXHDO101B003Const.STARTITIKAKUNIN:
                return StringUtil.nullToBlank(srRsusprnData.getStartitikakunin());
            // 印刷ｽﾀｰﾄ確認者
            case GXHDO101B003Const.INSATSU_STARTJI_KAKUNINSYA:
                return StringUtil.nullToBlank(srRsusprnData.getKakuninsya());
            //電極ﾍﾟｰｽﾄ
            case GXHDO101B003Const.DENKYOKU_PASTE:
                return StringUtil.nullToBlank(srRsusprnData.getEpaste());
            //電極製版仕様
            case GXHDO101B003Const.DENKYOKU_SEIHAN_SHIYOU:
                return StringUtil.nullToBlank(srRsusprnData.getPattern());
            // 先行ﾛｯﾄNo
            case GXHDO101B003Const.SENKOULOTNO:
                return StringUtil.nullToBlank(srRsusprnData.getSenkoulotno());
            // ﾃｰﾌﾟ使い切り
            case GXHDO101B003Const.TAPETSUKAIKIRI:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srRsusprnData.getTapetsukaikiri()));
            // 次ﾛｯﾄへ
            case GXHDO101B003Const.JILOTHE:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srRsusprnData.getJilothe()));
            // 備考3
            case GXHDO101B003Const.BIKOU3:
                return StringUtil.nullToBlank(srRsusprnData.getBikou3());
            // 備考4
            case GXHDO101B003Const.BIKOU4:
                return StringUtil.nullToBlank(srRsusprnData.getBikou4());
            // 備考5
            case GXHDO101B003Const.BIKOU5:
                return StringUtil.nullToBlank(srRsusprnData.getBikou5());
            
            default:
                return null;
        }
        
    }

    /**
     * 印刷RSUS_仮登録(tmp_sr_rsusprn)登録処理
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
    private void insertDeleteDataTmpSrRsusprn(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        String sql = "INSERT INTO tmp_sr_rsusprn ("
                + "KOJYO,LOTNO,EDABAN,KCPNO,TAPESYURUI,TAPELOTNO,TapeSlipKigo,GENRYOKIGO,KAISINICHIJI,SYURYONICHIJI,GOKI,SKEEGENO,"
                + "SKEEGEMAISUU,SKEEGESPEED,KANSOONDO,CLEARANCE,SAATU,MAKUATU1,SEIHANNO,SEIHANMAISUU,saidaisyorisuu,ruikeisyorisuu,PASTELOTNO,PASTENENDO,PASTEONDO,"
                + "INSATUROLLNO,INSATUROLLNO2,INSATUROLLNO3,INSATUROLLNO4,INSATUROLLNO5,INSATUHABASAVE,INSATUHABAEAVE,MLD,BIKO1,BIKO2,"
                + "TOROKUNICHIJI,KOSINNICHIJI,TANTOSYA,pkokeibun1,pastelotno2,pastenendo2,pasteondo2,pkokeibun2,petfilmsyurui,kansoondo2,kansoondo3,kansoondo4,"
                + "kansoondo5,seihanmei,makuatsu_ave_start,makuatsu_max_start,makuatsu_min_start,makuatucv_start,nijimikasure_start,"
                + "nijimikasure_end,tanto_end,printmaisuu,kansouroatsu,printhaba,table_clearrance,tokuisaki,lotkubuncode,ownercode,"
                + "seikeinagasa,genryou,eatumi,epaste,pattern,scraperspeed,startitikakunin,kakuninsya,skojyo,slotno,sedaban,tapetsukaikiri,jilothe,bikou3,bikou4,bikou5,revision,deleteflag"
                + ") SELECT "
                + "KOJYO,LOTNO,EDABAN,KCPNO,TAPESYURUI,TAPELOTNO,TapeSlipKigo,GENRYOKIGO,KAISINICHIJI,SYURYONICHIJI,GOKI,SKEEGENO,"
                + "SKEEGEMAISUU,SKEEGESPEED,KANSOONDO,CLEARANCE,SAATU,MAKUATU1,SEIHANNO,SEIHANMAISUU,saidaisyorisuu,ruikeisyorisuu,PASTELOTNO,PASTENENDO,PASTEONDO,"
                + "INSATUROLLNO,INSATUROLLNO2,INSATUROLLNO3,INSATUROLLNO4,INSATUROLLNO5,INSATUHABASAVE,INSATUHABAEAVE,MLD,BIKO1,BIKO2,"
                + "?,?,TANTOSYA,pkokeibun1,pastelotno2,pastenendo2,pasteondo2,pkokeibun2,petfilmsyurui,kansoondo2,kansoondo3,kansoondo4,"
                + "kansoondo5,seihanmei,makuatsu_ave_start,makuatsu_max_start,makuatsu_min_start,makuatucv_start,nijimikasure_start,"
                + "nijimikasure_end,tanto_end,printmaisuu,kansouroatsu,printhaba,table_clearrance,tokuisaki,lotkubuncode,ownercode,"
                + "seikeinagasa,genryou,eatumi,epaste,pattern,scraperspeed,startitikakunin,kakuninsya,skojyo,slotno,sedaban,tapetsukaikiri,jilothe,bikou3,bikou4,bikou5,?,? "
                + "FROM sr_rsusprn "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? ";

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
     * 印刷RSUS_ｻﾌﾞ画面仮登録(tmp_sub_sr_rsusprn)登録処理
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
    private void insertDeleteDataTmpSubSrRsusprn(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        String sql = "INSERT INTO tmp_sub_sr_rsusprn ("
                + "kojyo,lotno,edaban,makuatsu_start1,makuatsu_start2,makuatsu_start3,makuatsu_start4,makuatsu_start5,"
                + "insatuhaba_start1,insatuhaba_start2,insatuhaba_start3,insatuhaba_start4,insatuhaba_start5,torokunichiji,"
                + "kosinnichiji,revision,deleteflag"
                + ") SELECT "
                + "kojyo,lotno,edaban,makuatsu_start1,makuatsu_start2,makuatsu_start3,makuatsu_start4,makuatsu_start5,"
                + "insatuhaba_start1,insatuhaba_start2,insatuhaba_start3,insatuhaba_start4,insatuhaba_start5,?,"
                + "?,?,? "
                + "FROM sub_sr_rsusprn "
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
     * 文字列が出現するまでの数値を設定する。
     *
     * @param str 文字列

     * @return ret 文字列が出現するまでの数値
     */
    private static String getStringToIntValue(String str){
        String ret = null;
        StringBuilder strBud = new StringBuilder("");
        if(!StringUtil.isEmpty(str)){
            for (int i = 0;i<str.length();i++){
                
                if(i == 0 && !NumberUtil.isIntegerNumeric(str.valueOf(0))){
                    return ret;
                }
                if(NumberUtil.isIntegerNumeric(StringUtil.nullToBlank(str.charAt(i)))){
                    strBud.append(str.charAt(i));
                }
                if (!NumberUtil.isIntegerNumeric(StringUtil.nullToBlank(str.charAt(i)))) {
                    return strBud.toString();
                }
            }
        }
        return ret;
    }
    
    /**
     * 規格値を括り【】を除いて取得
     * @param itemList 項目リスト
     * @param itemId 項目ID
     * @return 規格値
     */
    private String getKikakuchiValue(List<FXHDD01> itemList, String itemId) {

        FXHDD01 item = getItemRow(itemList, itemId); // 規格値を取得する項目を取得
        // 項目が取得出来ないまたは規格値が設定されていない場合
        if (item == null || StringUtil.isEmpty(item.getKikakuChi())) {
            return "";
        }

        String kikakuchi = item.getKikakuChi();
        if (kikakuchi.startsWith("【")) {
            // 本来ありえないが1文字しかない場合、空を返却
            if (StringUtil.length(kikakuchi) == 1) {
                return "";
            }
            // 先頭文字以降を切り出し
            kikakuchi = kikakuchi.substring(1);
        }

        if (kikakuchi.endsWith("】")) {
            // 本来ありえないが1文字しかない場合、空を返却
            if (StringUtil.length(kikakuchi) == 1) {
                return "";
            }
            // 最終文字文字以前を切り出し
            kikakuchi = kikakuchi.substring(0, StringUtil.length(kikakuchi) - 1);
        }

        return kikakuchi;
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
        model = GXHDO101C020Logic.createGXHDO101C020Model(initDataSubFormC020, "GXHDO101B003");

        model.setReturnItemId_TapeLot1_Hinmei(GXHDO101B003Const.DENKYOKU_TAPE);
        model.setReturnItemId_TapeLot1_Tapelength(GXHDO101B003Const.SEIKEINAGASA);
        model.setReturnItemId_TapeLot1_Conventionallot(GXHDO101B003Const.SLIP_LOTNO);
        model.setReturnItemId_TapeLot1_Lotkigo(GXHDO101B003Const.GENRYO_KIGOU);
        model.setReturnItemId_TapeLot1_Rollno(GXHDO101B003Const.ROLL_NO1);
        model.setReturnItemId_TapeLot1_Tapelength(GXHDO101B003Const.SEIKEINAGASA);
        model.setReturnItemId_PasteLot1_Hinmei(GXHDO101B003Const.DENKYOKU_PASTE);
        model.setReturnItemId_PasteLot1_Conventionallot(GXHDO101B003Const.PASTE_LOT_NO1);
        model.setReturnItemId_PasteLot1_Kokeibunpct(GXHDO101B003Const.PASTE_KOKEIBUN1);
        model.setReturnItemId_PasteLot2_Conventionallot(GXHDO101B003Const.PASTE_LOT_NO2);
        model.setReturnItemId_PasteLot2_Kokeibunpct(GXHDO101B003Const.PASTE_KOKEIBUN2);
        model.setReturnItemId_Petname(GXHDO101B003Const.PET_FILM_SHURUI);
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
        params.add("GXHDO101B003");
        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            params.add(0);
        }
        
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapListHandler(), params.toArray());
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
            params.add("GXHDO101B003"); //画面ID
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
            params.add("GXHDO101B003"); //画面ID
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
        params.add("GXHDO101B003");
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
        params.add("GXHDO101B003"); //画面ID
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
        params.add("GXHDO101B003");
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
        params.add("GXHDO101B003");
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
                mkubun = "内部電極ﾍﾟｰｽﾄ";
                break;
            case GXHDO101C020Model.UWA_TANSHI:
                mkubun = "上端子ﾃｰﾌﾟ";
                break;
            case GXHDO101C020Model.SHITA_TANSHI:
                mkubun = "下端子ﾃｰﾌﾟ";
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
                mkubunno = "1";
                break;
            case GXHDO101C020Model.TAPE_LOT_2:
            case GXHDO101C020Model.PASTE_LOT_2:
                mkubunno = "2";
                break;
            case GXHDO101C020Model.TAPE_LOT_3:
                mkubunno = "3";
                break;
        }
        return mkubunno;
    }

    private int getTmpKbn(QueryRunner queryRunnerDoc) throws SQLException {
        int tmpKbn = 0;
        String sql = "SELECT tmp_kbn FROM fxhdm08 WHERE gamen_id = ?";
        
        List<Object> params = new ArrayList<>();
        params.add("GXHDO101B003");
        
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map<String, Object> result = queryRunnerDoc.query(sql, new MapHandler(), params.toArray());
        if (result != null && !result.isEmpty()) {
            tmpKbn = Integer.valueOf(StringUtil.nullToBlank(result.get("tmp_kbn")));
        }
        return tmpKbn;
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
