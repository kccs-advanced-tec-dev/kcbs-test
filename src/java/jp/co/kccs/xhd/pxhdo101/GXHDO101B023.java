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
import jp.co.kccs.xhd.db.model.SrDpprint;
import jp.co.kccs.xhd.db.model.SubSrDpprint;
import jp.co.kccs.xhd.model.GXHDO101C001Model;
import jp.co.kccs.xhd.model.GXHDO101C002Model;
import jp.co.kccs.xhd.model.GXHDO101C003Model;
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
import org.apache.commons.lang3.StringUtils;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/08/01<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
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
 * 変更日	2022/06/02<br>
 * 計画書No	MB2205-D010<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	画面表示項目を追加、設備ﾃﾞｰﾀ連携ﾎﾞﾀﾝを追加<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101B023(印刷・DP2)ロジック
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2019/08/01
 */
public class GXHDO101B023 implements IFormLogic {

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
            processData.setNoCheckButtonId(Arrays.asList(GXHDO101B023Const.BTN_MAKUATSU_TOP,
                    GXHDO101B023Const.BTN_PTN_KYORI_START_TOP,
                    GXHDO101B023Const.BTN_PTN_KYORI_END_TOP,
                    GXHDO101B023Const.BTN_STARTDATETIME_TOP,
                    GXHDO101B023Const.BTN_ENDDATETIME_TOP,
                    GXHDO101B023Const.BTN_MAKUATSU_BOTTOM,
                    GXHDO101B023Const.BTN_PTN_KYORI_START_BOTTOM,
                    GXHDO101B023Const.BTN_PTN_KYORI_END_BOTTOM,
                    GXHDO101B023Const.BTN_STARTDATETIME_BOTTOM,
                    GXHDO101B023Const.BTN_ENDDATETIME_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(GXHDO101B023Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO101B023Const.BTN_INSERT_TOP,
                    GXHDO101B023Const.BTN_DELETE_TOP,
                    GXHDO101B023Const.BTN_UPDATE_TOP,
                    GXHDO101B023Const.BTN_SETSUBI_DATA_RENKEI_TOP,
                    GXHDO101B023Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO101B023Const.BTN_INSERT_BOTTOM,
                    GXHDO101B023Const.BTN_DELETE_BOTTOM,
                    GXHDO101B023Const.BTN_UPDATE_BOTTOM,
                    GXHDO101B023Const.BTN_SETSUBI_DATA_RENKEI_BOTTOM));

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
            ErrUtil.outputErrorLog("NumberFormatException発生", ex, LOGGER);
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
     * 膜厚(サブ画面Open)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openMakuatsu(ProcessData processData) {

        try {

            processData.setMethod("");
            // コールバックパラメータにてサブ画面起動用の値を設定
            processData.setCollBackParam("gxhdo101c001");

            // 膜厚(SPS)の現在の値をサブ画面の表示用の値に渡す
            GXHDO101C001 beanGXHDO101C001 = (GXHDO101C001) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C001);
            beanGXHDO101C001.setGxhdO101c001ModelView(beanGXHDO101C001.getGxhdO101c001Model().clone());

        } catch (CloneNotSupportedException ex) {

            ErrUtil.outputErrorLog("CloneNotSupportedException発生", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            return processData;

        }

        return processData;
    }

    /**
     * PTN距離ｽﾀｰﾄ(サブ画面Open)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openPtnKyoriStart(ProcessData processData) {
        try {
            // コールバックパラメータにてサブ画面起動用の値を設定
            processData.setCollBackParam("gxhdo101c002");
            processData.setMethod("");

            // PTN距離Xの現在の値をサブ画面の表示用の値に設定
            GXHDO101C002 beanGXHDO101C002 = (GXHDO101C002) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C002);
            beanGXHDO101C002.setGxhdO101c002ModelView(beanGXHDO101C002.getGxhdO101c002Model().clone());

            return processData;
        } catch (CloneNotSupportedException ex) {
            ErrUtil.outputErrorLog("CloneNotSupportedException発生", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            return processData;
        }
    }

    /**
     * PTN距離ｴﾝﾄﾞ(サブ画面Open)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openPtnKyoriEnd(ProcessData processData) {
        try {
            // コールバックパラメータにてサブ画面起動用の値を設定
            processData.setCollBackParam("gxhdo101c003");
            processData.setMethod("");

            // PTN距離Yの現在の値をサブ画面表示用に設定
            GXHDO101C003 beanGXHDO101C003 = (GXHDO101C003) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C003);
            beanGXHDO101C003.setGxhdO101c003ModelView(beanGXHDO101C003.getGxhdO101c003Model().clone());

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
        for (FXHDD01 item : processData.getItemList()) {
            if("【実測値記入】".equals(item.getKikakuChi())){
                item.setStandardPattern("");
            }
        }
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

        //ブレード外観
        FXHDD01 itemBladeGikan = getItemRow(processData.getItemList(), GXHDO101B023Const.BLADE_GAIKAN);
        if ("NG".equals(itemBladeGikan.getValue())) {
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemBladeGikan);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemBladeGikan.getLabel1());
        }

        //ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
        FXHDD01 itemStartjiNijimiKasure = getItemRow(processData.getItemList(), GXHDO101B023Const.STARTJI_NIJIMI_KASURE_CHECK);
        if ("NG".equals(itemStartjiNijimiKasure.getValue())) {
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemStartjiNijimiKasure);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemStartjiNijimiKasure.getLabel1());
        }

        //終了時ﾆｼﾞﾐ・ｶｽﾚ確認
        FXHDD01 itemShuryojiNijimiKasure = getItemRow(processData.getItemList(), GXHDO101B023Const.SHURYOU_JI_NIJIMI_KASURE_CHECK);
        if ("NG".equals(itemShuryojiNijimiKasure.getValue())) {
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemShuryojiNijimiKasure);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemShuryojiNijimiKasure.getLabel1());
        }
        
//        //版胴の確認
//        FXHDD01 itemHando = getItemRow(processData.getItemList(), GXHDO101B023Const.HANDOU_MEI);
//        FXHDD01 itemDenkyoku = getItemRow(processData.getItemList(), GXHDO101B023Const.DENKYOKU_SEIHAN_MEI);
//        if (itemHando != null && !"".equals(itemHando.getValue()) && itemDenkyoku != null && !"".equals(itemDenkyoku.getValue())) {
//            
//            if (!itemHando.getValue().equals(itemDenkyoku.getValue())) {
//                List<FXHDD01> errFxhdd01List = Arrays.asList(itemHando);
//                return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemHando.getLabel1());
//            }
//        }
        

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
            int jissekino = Integer.parseInt(StringUtil.nullToBlank(session.getAttribute("jissekino")));
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // 品質DB登録実績データ取得
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekino, formId);
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
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekino, formId);

                // 品質DB登録実績更新処理
                updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekino, JOTAI_FLG_KARI_TOROKU, systemTime);
            }

            if (StringUtil.isEmpty(processData.getInitJotaiFlg()) || JOTAI_FLG_SAKUJO.equals(processData.getInitJotaiFlg())) {

                // 印刷・DP2_仮登録登録処理
                insertTmpSrDpprint(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, jissekino, systemTime, processData.getItemList(), processData.getHiddenDataMap());

                // 印刷・DP2_ｻﾌﾞ画面仮登録登録処理
                insertTmpSubSrDpprint(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, jissekino, systemTime);

                // 前工程WIP取込ｻﾌﾞ画面仮登録登録処理
                insertTmpSrMwiplotlink(queryRunnerQcdb, conQcdb, kojyo, lotNo8, edaban, systemTime, jissekino);

            } else {

                // 印刷・DP2_仮登録更新処理
                updateTmpSrDpprint(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, jissekino, systemTime, processData.getItemList(), processData.getHiddenDataMap());

                // 印刷・DP2_ｻﾌﾞ画面仮登録更新処理
                updateTmpSubSrDpprint(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo8, edaban, jissekino, systemTime);

                // 前工程WIP取込_ｻﾌﾞ画面仮登録更新処理
                updateTmpSrMwiplotlink(queryRunnerQcdb, conQcdb, kojyo, lotNo8, edaban, systemTime, jissekino);
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

            // 膜厚入力画面背景色クリア
            GXHDO101C001 beanGXHDO101C001 = (GXHDO101C001) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C001);
            for (GXHDO101C001Model.MakuatsuData data : beanGXHDO101C001.getGxhdO101c001Model().getMakuatsuDataList()) {
                data.setStartTextBackColor("");
                data.setEndTextBackColor("");
            }

            // PTN距離ｽﾀｰﾄ画面背景色クリア
            GXHDO101C002 beanGXHDO101C002 = (GXHDO101C002) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C002);
            for (GXHDO101C002Model.PtnKyoriStartData data : beanGXHDO101C002.getGxhdO101c002Model().getPtnKyoriStartDataList()) {
                data.setPtnKyoriXTextBackColor("");
                data.setPtnKyoriYTextBackColor("");
            }

            // PTN距離ｴﾝﾄﾞ画面背景色クリア
            GXHDO101C003 beanGXHDO101C003 = (GXHDO101C003) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C003);
            for (GXHDO101C003Model.PtnKyoriEndData data : beanGXHDO101C003.getGxhdO101c003Model().getPtnKyoriEndDataList()) {
                data.setPtnKyoriXTextBackColor("");
                data.setPtnKyoriYTextBackColor("");
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

        // サブ画面の入力チェックを行う。
        List<String> errorListSubForm;
        // 膜厚(SPS)画面チェック
        errorListSubForm = checkSubFormMakuatsu();
        if (!errorListSubForm.isEmpty()) {
            processData.setSubInitDispMsgList(errorListSubForm);
            processData.setMethod("openMakuatsu");
            return processData;
        }

        // PTN距離ｽﾀｰﾄ画面チェック
        errorListSubForm = checkSubFormPtnKyoriStart();
        if (!errorListSubForm.isEmpty()) {
            processData.setSubInitDispMsgList(errorListSubForm);
            processData.setMethod("openPtnKyoriStart");
            return processData;
        }

        // PTN距離ｴﾝﾄﾞ画面チェック
        errorListSubForm = checkSubFormPtnKyoriEnd();
        if (!errorListSubForm.isEmpty()) {
            processData.setSubInitDispMsgList(errorListSubForm);
            processData.setMethod("openPtnKyoriEnd");
            return processData;
        }
        
        // 前工程WIP画面チェック
        errorListSubForm = checkmwLot();
        if (!errorListSubForm.isEmpty()) {
            processData.setSubInitDispMsgList(errorListSubForm);
            processData.setMethod("openWipImport");
            return processData;
        }


        // 規格チェック
        List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList = new ArrayList<>();
        for (FXHDD01 item : processData.getItemList()) {
            if("【実測値記入】".equals(item.getKikakuChi())){
                item.setStandardPattern("");
            }
        }
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

        //ブレード外観
        FXHDD01 itemBladeGikan = getItemRow(processData.getItemList(), GXHDO101B023Const.BLADE_GAIKAN);
        if ("NG".equals(itemBladeGikan.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemBladeGikan);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemBladeGikan.getLabel1());
        }

        //ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
        FXHDD01 itemStartjiNijimiKasure = getItemRow(processData.getItemList(), GXHDO101B023Const.STARTJI_NIJIMI_KASURE_CHECK);
        if ("NG".equals(itemStartjiNijimiKasure.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemStartjiNijimiKasure);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemStartjiNijimiKasure.getLabel1());
        }

        //終了時ﾆｼﾞﾐ・ｶｽﾚ確認
        FXHDD01 itemShuryojiNijimiKasure = getItemRow(processData.getItemList(), GXHDO101B023Const.SHURYOU_JI_NIJIMI_KASURE_CHECK);
        if ("NG".equals(itemShuryojiNijimiKasure.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemShuryojiNijimiKasure);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemShuryojiNijimiKasure.getLabel1());
        }

        ValidateUtil validateUtil = new ValidateUtil();
        // 開始日時、終了日時前後チェック
        FXHDD01 itemInsatsuKaishiDay = getItemRow(processData.getItemList(), GXHDO101B023Const.INSATSU_KAISHI_DAY); //印刷開始日
        FXHDD01 itemInsatsuKaishiTime = getItemRow(processData.getItemList(), GXHDO101B023Const.INSATSU_KAISHI_TIME); // 印刷開始時刻
        Date kaishiDate = DateUtil.convertStringToDate(itemInsatsuKaishiDay.getValue(), itemInsatsuKaishiTime.getValue());
        FXHDD01 itemInsatsuShuryouDay = getItemRow(processData.getItemList(), GXHDO101B023Const.INSATSU_SHURYOU_DAY); //印刷終了日INSATSU_SHURYOU_DAY
        FXHDD01 itemInsatsuShuryouTime = getItemRow(processData.getItemList(), GXHDO101B023Const.INSATSU_SHURYOU_TIME); //印刷終了時刻
        Date shuryoDate = DateUtil.convertStringToDate(itemInsatsuShuryouDay.getValue(), itemInsatsuShuryouTime.getValue());
        //R001チェック呼出し
        String msgCheckR001 = validateUtil.checkR001(itemInsatsuKaishiDay.getLabel1(), kaishiDate, itemInsatsuShuryouDay.getLabel1(), shuryoDate);
        if (!StringUtil.isEmpty(msgCheckR001)) {
            //エラー発生時
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemInsatsuKaishiDay, itemInsatsuKaishiTime, itemInsatsuShuryouDay, itemInsatsuShuryouTime);
            return MessageUtil.getErrorMessageInfo("", msgCheckR001, true, true, errFxhdd01List);
        }
//        FXHDD01 itemHando = getItemRow(processData.getItemList(), GXHDO101B023Const.HANDOU_MEI);
//        FXHDD01 itemDenkyoku = getItemRow(processData.getItemList(), GXHDO101B023Const.DENKYOKU_SEIHAN_MEI);
//        if (itemHando != null && !"".equals(itemHando.getValue()) && itemDenkyoku != null && !"".equals(itemDenkyoku.getValue())) {
//            
//            if (!itemHando.getValue().equals(itemDenkyoku.getValue())) {
//                List<FXHDD01> errFxhdd01List = Arrays.asList(itemHando);
//                return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemHando.getLabel1());
//            }
//        }


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
     * サブ画面(PTN距離ｽﾀｰﾄ)チェック処理
     *
     * @return エラーリスト
     */
    private List<String> checkSubFormPtnKyoriStart() {
        GXHDO101C002 beanGXHDO101C002 = (GXHDO101C002) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C002);
        return GXHDO101C002Logic.checkInput(beanGXHDO101C002.getGxhdO101c002Model());
    }

    /**
     * サブ画面(PTN距離ｴﾝﾄﾞ)チェック処理
     *
     * @return エラーリスト
     */
    private List<String> checkSubFormPtnKyoriEnd() {
        GXHDO101C003 beanGXHDO101C003 = (GXHDO101C003) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C003);
        return GXHDO101C003Logic.checkInput(beanGXHDO101C003.getGxhdO101c003Model());
    }
    
    /**
     * サブ画面(前工程WIP)チェック処理
     *
     * @return エラーリスト
     */
    private List<String> checkmwLot() {
        GXHDO101C020 beanGXHDO101C020 = (GXHDO101C020) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C020);
         return GXHDO101C020Logic.checkLotSps(beanGXHDO101C020.getGxhdO101c020Model());
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
            int jissekino = Integer.parseInt(StringUtil.nullToBlank(session.getAttribute("jissekino")));
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // 品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekino, formId);
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
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekino, formId);

                // 品質DB登録実績更新処理
                updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekino, JOTAI_FLG_TOROKUZUMI, systemTime);
            }

            // 仮登録状態の場合、仮登録のデータを削除する。
            SrDpprint tmpSrDpprint = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrDpprint> srDpprintList = getSrDpprintData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo8, edaban, jissekino);
                if (!srDpprintList.isEmpty()) {
                    tmpSrDpprint = srDpprintList.get(0);
                }

                deleteTmpSrDpprint(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban, jissekino);
                deleteTmpSubSrDpprint(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban, jissekino);
                deleteTmpSrMwiplotlink(queryRunnerQcdb, conQcdb, kojyo, lotNo8, edaban, jissekino);
            }

            // 印刷・DP2_登録処理
            insertSrDpprint(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, jissekino, systemTime, processData.getItemList(), tmpSrDpprint, processData.getHiddenDataMap());

            // 印刷・DP2_ｻﾌﾞ画面登録処理
            insertSubSrDpprint(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, jissekino, systemTime);

            // 前工程WIP取込ｻﾌﾞ画面登録処理
            insertSrMwiplotlink(queryRunnerQcdb, conQcdb, kojyo, lotNo8, edaban, systemTime, jissekino);

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
        // 膜厚(SPS)画面チェック
        errorListSubForm = checkSubFormMakuatsu();
        if (!errorListSubForm.isEmpty()) {
            processData.setSubInitDispMsgList(errorListSubForm);
            processData.setMethod("openMakuatsu");
            return processData;
        }

        // PTN距離ｽﾀｰﾄ画面チェック
        errorListSubForm = checkSubFormPtnKyoriStart();
        if (!errorListSubForm.isEmpty()) {
            processData.setSubInitDispMsgList(errorListSubForm);
            processData.setMethod("openPtnKyoriStart");
            return processData;
        }

        // PTN距離ｴﾝﾄﾞ画面チェック
        errorListSubForm = checkSubFormPtnKyoriEnd();
        if (!errorListSubForm.isEmpty()) {
            processData.setSubInitDispMsgList(errorListSubForm);
            processData.setMethod("openPtnKyoriEnd");
            return processData;
        }

        // 規格チェック
        List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList = new ArrayList<>();
        for (FXHDD01 item : processData.getItemList()) {
            if("【実測値記入】".equals(item.getKikakuChi())){
                item.setStandardPattern("");
            }
        }
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
        processData.setUserAuthParam(GXHDO101B023Const.USER_AUTH_UPDATE_PARAM);

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
            int jissekino = Integer.parseInt(StringUtil.nullToBlank(session.getAttribute("jissekino")));
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // 品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekino, formId);
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
            BigDecimal newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekino, formId);

            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            // 品質DB登録実績更新処理
            updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekino, JOTAI_FLG_TOROKUZUMI, systemTime);

            // 印刷・DP2_更新処理
            updateSrDpprint(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, jissekino, systemTime, processData.getItemList(), processData.getHiddenDataMap());

            // 印刷・DP2_ｻﾌﾞ画面更新処理
            updateSubSrDpprint(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo8, edaban, jissekino, systemTime);

            // 前工程WIP取込_ｻﾌﾞ画面更新処理
            updateSrMwiplotlink(queryRunnerQcdb, conQcdb, kojyo, lotNo8, edaban, systemTime, jissekino);

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
        processData.setUserAuthParam(GXHDO101B023Const.USER_AUTH_DELETE_PARAM);

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
            int jissekino = Integer.parseInt(StringUtil.nullToBlank(session.getAttribute("jissekino")));
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));

            // 品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekino, formId);
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
            BigDecimal newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekino, formId);

            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            // 品質DB登録実績更新処理
            updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekino, JOTAI_FLG_SAKUJO, systemTime);

            // 印刷・DP2_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo8, edaban, jissekino);
            insertDeleteDataTmpSrDpprint(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, jissekino, systemTime);

            // 印刷・DP2_ｻﾌﾞ画面仮登録登録処理
            insertDeleteDataTmpSubSrDpprint(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, jissekino, systemTime);

            // 印刷・DP2_削除処理
            deleteSrDpprint(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban, jissekino);

            // 印刷・DP2_ｻﾌﾞ画面削除処理
            deleteSubSrDpprint(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban, jissekino);

            // 前工程WIP取込_ｻﾌﾞ画面削除処理
            deleteSrMwiplotlink(queryRunnerQcdb, conQcdb, kojyo, lotNo8, edaban, systemTime, jissekino);

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
                activeIdList.addAll(Arrays.asList(GXHDO101B023Const.BTN_COPY_EDABAN_BOTTOM,
                        GXHDO101B023Const.BTN_MAKUATSU_BOTTOM,
                        GXHDO101B023Const.BTN_PTN_KYORI_START_BOTTOM,
                        GXHDO101B023Const.BTN_PTN_KYORI_END_BOTTOM,
                        GXHDO101B023Const.BTN_DELETE_BOTTOM,
                        GXHDO101B023Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B023Const.BTN_STARTDATETIME_BOTTOM,
                        GXHDO101B023Const.BTN_ENDDATETIME_BOTTOM,
                        GXHDO101B023Const.BTN_SETSUBI_DATA_RENKEI_BOTTOM,
                        GXHDO101B023Const.BTN_COPY_EDABAN_TOP,
                        GXHDO101B023Const.BTN_MAKUATSU_TOP,
                        GXHDO101B023Const.BTN_PTN_KYORI_START_TOP,
                        GXHDO101B023Const.BTN_PTN_KYORI_END_TOP,
                        GXHDO101B023Const.BTN_DELETE_TOP,
                        GXHDO101B023Const.BTN_UPDATE_TOP,
                        GXHDO101B023Const.BTN_STARTDATETIME_TOP,
                        GXHDO101B023Const.BTN_ENDDATETIME_TOP,
                        GXHDO101B023Const.BTN_SETSUBI_DATA_RENKEI_TOP
                ));
                inactiveIdList.addAll(Arrays.asList(GXHDO101B023Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B023Const.BTN_INSERT_BOTTOM,
                        GXHDO101B023Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B023Const.BTN_INSERT_TOP));

                break;
            default:
                activeIdList.addAll(Arrays.asList(GXHDO101B023Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B023Const.BTN_COPY_EDABAN_BOTTOM,
                        GXHDO101B023Const.BTN_MAKUATSU_BOTTOM,
                        GXHDO101B023Const.BTN_PTN_KYORI_START_BOTTOM,
                        GXHDO101B023Const.BTN_PTN_KYORI_END_BOTTOM,
                        GXHDO101B023Const.BTN_INSERT_BOTTOM,
                        GXHDO101B023Const.BTN_STARTDATETIME_BOTTOM,
                        GXHDO101B023Const.BTN_ENDDATETIME_BOTTOM,
                        GXHDO101B023Const.BTN_SETSUBI_DATA_RENKEI_BOTTOM,
                        GXHDO101B023Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B023Const.BTN_COPY_EDABAN_TOP,
                        GXHDO101B023Const.BTN_MAKUATSU_TOP,
                        GXHDO101B023Const.BTN_PTN_KYORI_START_TOP,
                        GXHDO101B023Const.BTN_PTN_KYORI_END_TOP,
                        GXHDO101B023Const.BTN_INSERT_TOP,
                        GXHDO101B023Const.BTN_STARTDATETIME_TOP,
                        GXHDO101B023Const.BTN_ENDDATETIME_TOP,
                        GXHDO101B023Const.BTN_SETSUBI_DATA_RENKEI_TOP
                ));

                inactiveIdList.addAll(Arrays.asList(GXHDO101B023Const.BTN_DELETE_BOTTOM,
                        GXHDO101B023Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B023Const.BTN_DELETE_TOP,
                        GXHDO101B023Const.BTN_UPDATE_TOP));

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
            case GXHDO101B023Const.BTN_MAKUATSU_TOP:
            case GXHDO101B023Const.BTN_MAKUATSU_BOTTOM:
                method = "openMakuatsu";
                break;
            // PTN距離X
            case GXHDO101B023Const.BTN_PTN_KYORI_START_TOP:
            case GXHDO101B023Const.BTN_PTN_KYORI_START_BOTTOM:
                method = "openPtnKyoriStart";
                break;
            // PTN距離Y
            case GXHDO101B023Const.BTN_PTN_KYORI_END_TOP:
            case GXHDO101B023Const.BTN_PTN_KYORI_END_BOTTOM:
                method = "openPtnKyoriEnd";
                break;
            // 仮登録
            case GXHDO101B023Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO101B023Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO101B023Const.BTN_INSERT_TOP:
            case GXHDO101B023Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 枝番コピー
            case GXHDO101B023Const.BTN_COPY_EDABAN_TOP:
            case GXHDO101B023Const.BTN_COPY_EDABAN_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 修正
            case GXHDO101B023Const.BTN_UPDATE_TOP:
            case GXHDO101B023Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO101B023Const.BTN_DELETE_TOP:
            case GXHDO101B023Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 開始日時
            case GXHDO101B023Const.BTN_STARTDATETIME_TOP:
            case GXHDO101B023Const.BTN_STARTDATETIME_BOTTOM:
                method = "setKaishiDateTime";
                break;
            // 開始日時
            case GXHDO101B023Const.BTN_ENDDATETIME_TOP:
            case GXHDO101B023Const.BTN_ENDDATETIME_BOTTOM:
                method = "setShuryouDateTime";
                break;
            // 前工程WIP
            case GXHDO101B023Const.BTN_WIP_IMPORT_TOP:
            case GXHDO101B023Const.BTN_WIP_IMPORT_BOTTOM:
                method = "openWipImport";
                break;
            // 設備ﾃﾞｰﾀ連携
            case GXHDO101B023Const.BTN_SETSUBI_DATA_RENKEI_TOP:
            case GXHDO101B023Const.BTN_SETSUBI_DATA_RENKEI_BOTTOM:
                method = "setSetsubiDataRenkei";
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
    private ProcessData setInitData(ProcessData processData) throws SQLException, NumberFormatException {

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
        if (!setInputItemData(processData, queryRunnerDoc, queryRunnerQcdb, lotNo, jissekino, formId)) {
            // エラー発生時は処理を中断
            processData.setFatalError(true);
            processData.setInitMessageList(Arrays.asList(MessageUtil.getMessage("XHD-000038")));
            return processData;
        }

        // 画面に取得した情報をセットする。(入力項目以外)
        setViewItemData(processData, sekkeiData, lotKbnMasData, ownerMasData, shikakariData, lotNo);

        // 隠しデータ保持
        processData.getHiddenDataMap().put("koteiKbn", StringUtil.nullToBlank(getMapData(sekkeiData, "PROCESS")));

        processData.setInitMessageList(errorMessageList);
        
        // 表示制御
        if (jissekino == 1 ) {
            List<FXHDD01> itemList = processData.getItemList();
            itemList.remove(getItemRow(itemList, GXHDO101B023Const.ZURERYOUKIJUNCHIX));
            itemList.remove(getItemRow(itemList, GXHDO101B023Const.ZURERYOUKIJUNCHIY));
            itemList.remove(getItemRow(itemList, GXHDO101B023Const.AWASESEIDODAKOU));
            itemList.remove(getItemRow(itemList, GXHDO101B023Const.AWASESEIDONAGARE));
        }
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
    private void setViewItemData(ProcessData processData, Map sekkeiData, Map lotKbnMasData, Map ownerMasData, Map shikakariData, String lotNo) {

        // ロットNo
        this.setItemData(processData, GXHDO101B023Const.LOTNO, lotNo);
        // KCPNO
        this.setItemData(processData, GXHDO101B023Const.KCPNO, StringUtil.nullToBlank(getMapData(shikakariData, "kcpno")));
        // セット数
        String suuryo = StringUtil.nullToBlank(getMapData(shikakariData, "suuryo"));
        String torikosuu = StringUtil.nullToBlank(getMapData(shikakariData, "torikosuu"));
        if (StringUtil.isEmpty(suuryo) || "0".equals(suuryo) || StringUtil.isEmpty(torikosuu) || "0".equals(torikosuu)) {
            this.setItemData(processData, GXHDO101B023Const.SET_SUU, "0");
        } else {
            BigDecimal decHasseisu = new BigDecimal(suuryo);
            BigDecimal decTorikosuu = new BigDecimal(torikosuu);
            BigDecimal setsu = decHasseisu.divide(decTorikosuu, 0, RoundingMode.DOWN);
            this.setItemData(processData, GXHDO101B023Const.SET_SUU, setsu.toPlainString());
        }

        // 客先
        this.setItemData(processData, GXHDO101B023Const.KYAKUSAKI, StringUtil.nullToBlank(getMapData(shikakariData, "tokuisaki")));

        // ロット区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); //ﾛｯﾄ区分ｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B023Const.LOT_KUBUN, "");
        } else {
            String lotKubun = StringUtil.nullToBlank(getMapData(lotKbnMasData, "lotkubun"));
            this.setItemData(processData, GXHDO101B023Const.LOT_KUBUN, lotkubuncode + ":" + lotKubun);
        }

        // オーナー
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));// ｵｰﾅｰｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B023Const.OWNER, "");
        } else {
            String owner = StringUtil.nullToBlank(getMapData(ownerMasData, "ownername"));
            this.setItemData(processData, GXHDO101B023Const.OWNER, ownercode + ":" + owner);
        }

        // 電極テープ
        this.setItemData(processData, GXHDO101B023Const.DENKYOKU_TAPE, StringUtil.nullToBlank(sekkeiData.get("GENRYOU"))
                + "  " + StringUtil.nullToBlank(sekkeiData.get("ETAPE")));

        // 積層数
        this.setItemData(processData, GXHDO101B023Const.SEKISOU_SU, StringUtil.nullToBlank(sekkeiData.get("EATUMI"))
                + "μm×"
                + StringUtil.nullToBlank(sekkeiData.get("SOUSUU"))
                + "層  "
                + StringUtil.nullToBlank(sekkeiData.get("EMAISUU"))
                + "枚");

        // 電極ペースト
        this.setItemData(processData, GXHDO101B023Const.DENKYOKU_PASTE, "");

        // 電極製版名
        this.setItemData(processData, GXHDO101B023Const.DENKYOKU_SEIHAN_MEI, StringUtil.nullToBlank(sekkeiData.get("PATTERN")));

        // 版胴名
//        this.setItemData(processData, GXHDO101B023Const.HANDOU_MEI, StringUtil.nullToBlank(sekkeiData.get("PATTERN")));

    }

    /**
     * 入力項目のデータを画面項目に設定
     *
     * @param processData 処理制御データ
     * @param queryRunnerDoc QueryRunnerオブジェクト(DocServer)
     * @param queryRunnerQcdb QueryRunnerオブジェクト(Qcdb)
     * @param lotNo ﾛｯﾄNo
     * @param jissekino 実績No
     * @param formId 画面ID
     * @return 設定結果(失敗時false)
     * @throws SQLException 例外エラー
     */
    private boolean setInputItemData(ProcessData processData, QueryRunner queryRunnerDoc, QueryRunner queryRunnerQcdb,
            String lotNo, int jissekino, String formId) throws SQLException {

        List<SrDpprint> srDpprintDataList = new ArrayList<>();
        List<SubSrDpprint> subSrDpprintDataList = new ArrayList<>();
        String rev = "";
        String jotaiFlg = "";
        String kojyo = lotNo.substring(0, 3);
        String lotNo8 = lotNo.substring(3, 11);
        String edaban = lotNo.substring(11, 14);

        for (int i = 0; i < 5; i++) {
            // 品質DB実績登録Revision情報取得
            Map fxhdd03RevInfo = loadFxhdd03RevInfo(queryRunnerDoc, kojyo, lotNo8, edaban, jissekino, formId);
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
                
                // 前工程WIP取込画面データ設定
                setInputItemDataSubFormC020(queryRunnerQcdb, kojyo, lotNo8, edaban, jotaiFlg, jissekino);

                return true;
            }

            // 印刷・DP2データ取得
            srDpprintDataList = getSrDpprintData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban, jissekino);
            if (srDpprintDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // 印刷・DP2_ｻﾌﾞ画面データ取得
            subSrDpprintDataList = getSubSrDpprintData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban, jissekino);
            if (subSrDpprintDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srDpprintDataList.isEmpty() || subSrDpprintDataList.isEmpty()) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srDpprintDataList.get(0));

        // 膜厚入力画面データ設定
        setInputItemDataSubFormC001(subSrDpprintDataList.get(0), kojyo, lotNo8, edaban);

        // PTN距離X入力画面データ設定
        setInputItemDataSubFormC002(subSrDpprintDataList.get(0));

        // PTN距離Y入力画面データ設定
        setInputItemDataSubFormC003(subSrDpprintDataList.get(0));
        
        // 前工程WIP取込画面データ設定
        setInputItemDataSubFormC020(queryRunnerQcdb, kojyo, lotNo8, edaban, jotaiFlg, jissekino);

        return true;

    }

    /**
     * メイン画面データ設定処理
     *
     * @param processData 処理制御データ
     * @param srDpprintData 印刷・DP2データ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrDpprint srDpprintData) {

        // ｽﾘｯﾌﾟﾛｯﾄNo
        this.setItemData(processData, GXHDO101B023Const.SLIP_LOTNO, getSrDpprintItemData(GXHDO101B023Const.SLIP_LOTNO, srDpprintData));
        // ﾛｰﾙNo1
        this.setItemData(processData, GXHDO101B023Const.ROLL_NO1, getSrDpprintItemData(GXHDO101B023Const.ROLL_NO1, srDpprintData));
        // ﾛｰﾙNo2
        this.setItemData(processData, GXHDO101B023Const.ROLL_NO2, getSrDpprintItemData(GXHDO101B023Const.ROLL_NO2, srDpprintData));
        // ﾛｰﾙNo3
        this.setItemData(processData, GXHDO101B023Const.ROLL_NO3, getSrDpprintItemData(GXHDO101B023Const.ROLL_NO3, srDpprintData));
        // 原料記号
        this.setItemData(processData, GXHDO101B023Const.GENRYO_KIGOU, getSrDpprintItemData(GXHDO101B023Const.GENRYO_KIGOU, srDpprintData));
        // ﾃｰﾌﾟ厚み
        this.setItemData(processData, GXHDO101B023Const.TAPE_ATSU, getSrDpprintItemData(GXHDO101B023Const.TAPE_ATSU, srDpprintData));
        // 内部電極ﾍﾟｰｽﾄﾛｯﾄNo1
        this.setItemData(processData, GXHDO101B023Const.PASTE_LOT_NO1, getSrDpprintItemData(GXHDO101B023Const.PASTE_LOT_NO1, srDpprintData));
        // 内部電極ﾍﾟｰｽﾄ粘度1
        this.setItemData(processData, GXHDO101B023Const.PASTE_NENDO1, getSrDpprintItemData(GXHDO101B023Const.PASTE_NENDO1, srDpprintData));
        // 内部電極ﾍﾟｰｽﾄ温度1
        this.setItemData(processData, GXHDO101B023Const.PASTE_ONDO1, getSrDpprintItemData(GXHDO101B023Const.PASTE_ONDO1, srDpprintData));
        // 内部電極ﾍﾟｰｽﾄ固形分1
        this.setItemData(processData, GXHDO101B023Const.PASTE_KOKEIBUN1, getSrDpprintItemData(GXHDO101B023Const.PASTE_KOKEIBUN1, srDpprintData));
        // 内部電極ﾍﾟｰｽﾄﾛｯﾄNo2
        this.setItemData(processData, GXHDO101B023Const.PASTE_LOT_NO2, getSrDpprintItemData(GXHDO101B023Const.PASTE_LOT_NO2, srDpprintData));
        // PETﾌｨﾙﾑ種類
        this.setItemData(processData, GXHDO101B023Const.PET_FILM_SHURUI, getSrDpprintItemData(GXHDO101B023Const.PET_FILM_SHURUI, srDpprintData));
        // 印刷号機
        this.setItemData(processData, GXHDO101B023Const.INSATSU_GOUKI, getSrDpprintItemData(GXHDO101B023Const.INSATSU_GOUKI, srDpprintData));
        // 乾燥温度表示値1
        this.setItemData(processData, GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI1, getSrDpprintItemData(GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI1, srDpprintData));
        // 乾燥温度表示値2
        this.setItemData(processData, GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI2, getSrDpprintItemData(GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI2, srDpprintData));
        // 乾燥温度表示値3
        this.setItemData(processData, GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI3, getSrDpprintItemData(GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI3, srDpprintData));
        // 乾燥温度表示値4
        this.setItemData(processData, GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI4, getSrDpprintItemData(GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI4, srDpprintData));
        // 乾燥温度下側表示値1
        this.setItemData(processData, GXHDO101B023Const.KANSOU_ONDO_SHITA_HYOUJICHI1, getSrDpprintItemData(GXHDO101B023Const.KANSOU_ONDO_SHITA_HYOUJICHI1, srDpprintData));
        // 乾燥温度下側表示値2
        this.setItemData(processData, GXHDO101B023Const.KANSOU_ONDO_SHITA_HYOUJICHI2, getSrDpprintItemData(GXHDO101B023Const.KANSOU_ONDO_SHITA_HYOUJICHI2, srDpprintData));
        // 乾燥温度下側表示値3
        this.setItemData(processData, GXHDO101B023Const.KANSOU_ONDO_SHITA_HYOUJICHI3, getSrDpprintItemData(GXHDO101B023Const.KANSOU_ONDO_SHITA_HYOUJICHI3, srDpprintData));
        // 乾燥温度下側表示値4
        this.setItemData(processData, GXHDO101B023Const.KANSOU_ONDO_SHITA_HYOUJICHI4, getSrDpprintItemData(GXHDO101B023Const.KANSOU_ONDO_SHITA_HYOUJICHI4, srDpprintData));
        // 乾燥温度表示値5
        this.setItemData(processData, GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI5, getSrDpprintItemData(GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI5, srDpprintData));
        // 搬送速度
        this.setItemData(processData, GXHDO101B023Const.HANSOU_SOKUDO, getSrDpprintItemData(GXHDO101B023Const.HANSOU_SOKUDO, srDpprintData));
        // 圧胴圧力
        this.setItemData(processData, GXHDO101B023Const.ATSUDOU_ATSURYOKU, getSrDpprintItemData(GXHDO101B023Const.ATSUDOU_ATSURYOKU, srDpprintData));
        // ﾌﾞﾚｰﾄﾞ圧力
        this.setItemData(processData, GXHDO101B023Const.BLADE_ATSURYOKU, getSrDpprintItemData(GXHDO101B023Const.BLADE_ATSURYOKU, srDpprintData));
        // 版胴名
        this.setItemData(processData, GXHDO101B023Const.HANDOU_MEI, getSrDpprintItemData(GXHDO101B023Const.HANDOU_MEI, srDpprintData));
        // 版胴ﾛｯﾄNo
        this.setItemData(processData, GXHDO101B023Const.HANDOU_LOTNO, getSrDpprintItemData(GXHDO101B023Const.HANDOU_LOTNO, srDpprintData));
        // 版胴使用枚数
        this.setItemData(processData, GXHDO101B023Const.HANDOU_SHIYOU_MAISUU, getSrDpprintItemData(GXHDO101B023Const.HANDOU_SHIYOU_MAISUU, srDpprintData));
        // 圧胴ﾛｯﾄNo
        this.setItemData(processData, GXHDO101B023Const.ATSUDOU_LOTNO, getSrDpprintItemData(GXHDO101B023Const.ATSUDOU_LOTNO, srDpprintData));
        // 圧胴使用枚数
        this.setItemData(processData, GXHDO101B023Const.ATSUDOU_SIYOU_MAISUU, getSrDpprintItemData(GXHDO101B023Const.ATSUDOU_SIYOU_MAISUU, srDpprintData));
        // ﾌﾞﾚｰﾄﾞNo.
        this.setItemData(processData, GXHDO101B023Const.BLADE_NO, getSrDpprintItemData(GXHDO101B023Const.BLADE_NO, srDpprintData));
        // ﾌﾞﾚｰﾄﾞ外観
        this.setItemData(processData, GXHDO101B023Const.BLADE_GAIKAN, getSrDpprintItemData(GXHDO101B023Const.BLADE_GAIKAN, srDpprintData));
        // 印刷開始日
        this.setItemData(processData, GXHDO101B023Const.INSATSU_KAISHI_DAY, getSrDpprintItemData(GXHDO101B023Const.INSATSU_KAISHI_DAY, srDpprintData));
        // 印刷開始時刻
        this.setItemData(processData, GXHDO101B023Const.INSATSU_KAISHI_TIME, getSrDpprintItemData(GXHDO101B023Const.INSATSU_KAISHI_TIME, srDpprintData));
        // 印刷ｽﾀｰﾄ膜厚AVE
        this.setItemData(processData, GXHDO101B023Const.INSATSU_START_MAKUATSU_AVE, getSrDpprintItemData(GXHDO101B023Const.INSATSU_START_MAKUATSU_AVE, srDpprintData));
        // 印刷ｽﾀｰﾄ膜厚MAX
        this.setItemData(processData, GXHDO101B023Const.INSATSU_START_MAKUATSU_MAX, getSrDpprintItemData(GXHDO101B023Const.INSATSU_START_MAKUATSU_MAX, srDpprintData));
        // 印刷ｽﾀｰﾄ膜厚MIN
        this.setItemData(processData, GXHDO101B023Const.INSATSU_START_MAKUATSU_MIN, getSrDpprintItemData(GXHDO101B023Const.INSATSU_START_MAKUATSU_MIN, srDpprintData));
        // 印刷ｽﾀｰﾄ膜厚CV
        this.setItemData(processData, GXHDO101B023Const.INSATSU_START_MAKUATSU_CV, getSrDpprintItemData(GXHDO101B023Const.INSATSU_START_MAKUATSU_CV, srDpprintData));
        // PTN間距離印刷ｽﾀｰﾄ X Min
        this.setItemData(processData, GXHDO101B023Const.PTN_INSATSU_START_X_MIN, getSrDpprintItemData(GXHDO101B023Const.PTN_INSATSU_START_X_MIN, srDpprintData));
        // PTN間距離印刷ｽﾀｰﾄ Y Min
        this.setItemData(processData, GXHDO101B023Const.PTN_INSATSU_START_Y_MIN, getSrDpprintItemData(GXHDO101B023Const.PTN_INSATSU_START_Y_MIN, srDpprintData));
        // ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
        this.setItemData(processData, GXHDO101B023Const.STARTJI_NIJIMI_KASURE_CHECK, getSrDpprintItemData(GXHDO101B023Const.STARTJI_NIJIMI_KASURE_CHECK, srDpprintData));
        // 印刷ｽﾀｰﾄ時担当者
        this.setItemData(processData, GXHDO101B023Const.INSATSU_STARTJI_TANTOUSHA, getSrDpprintItemData(GXHDO101B023Const.INSATSU_STARTJI_TANTOUSHA, srDpprintData));
        // 印刷ｽﾀｰﾄ時確認者
        this.setItemData(processData, GXHDO101B023Const.INSATSU_STARTJI_KAKUNINSHA, getSrDpprintItemData(GXHDO101B023Const.INSATSU_STARTJI_KAKUNINSHA, srDpprintData));
        // 印刷終了日
        this.setItemData(processData, GXHDO101B023Const.INSATSU_SHURYOU_DAY, getSrDpprintItemData(GXHDO101B023Const.INSATSU_SHURYOU_DAY, srDpprintData));
        // 印刷終了時刻
        this.setItemData(processData, GXHDO101B023Const.INSATSU_SHURYOU_TIME, getSrDpprintItemData(GXHDO101B023Const.INSATSU_SHURYOU_TIME, srDpprintData));
        // 印刷ｴﾝﾄﾞ膜厚AVE
        this.setItemData(processData, GXHDO101B023Const.INSATSU_END_MAKUATSU_AVE, getSrDpprintItemData(GXHDO101B023Const.INSATSU_END_MAKUATSU_AVE, srDpprintData));
        // 印刷ｴﾝﾄﾞ膜厚MAX
        this.setItemData(processData, GXHDO101B023Const.INSATSU_END_MAKUATSU_MAX, getSrDpprintItemData(GXHDO101B023Const.INSATSU_END_MAKUATSU_MAX, srDpprintData));
        // 印刷ｴﾝﾄﾞ膜厚MIN
        this.setItemData(processData, GXHDO101B023Const.INSATSU_END_MAKUATSU_MIN, getSrDpprintItemData(GXHDO101B023Const.INSATSU_END_MAKUATSU_MIN, srDpprintData));
        // 印刷ｴﾝﾄﾞ膜厚CV
        this.setItemData(processData, GXHDO101B023Const.INSATSU_END_MAKUATSU_CV, getSrDpprintItemData(GXHDO101B023Const.INSATSU_END_MAKUATSU_CV, srDpprintData));
        // PTN間距離印刷ｴﾝﾄﾞ X Min
        this.setItemData(processData, GXHDO101B023Const.PTN_INSATSU_END_X_MIN, getSrDpprintItemData(GXHDO101B023Const.PTN_INSATSU_END_X_MIN, srDpprintData));
        // PTN間距離印刷ｴﾝﾄﾞ Y Min
        this.setItemData(processData, GXHDO101B023Const.PTN_INSATSU_END_Y_MIN, getSrDpprintItemData(GXHDO101B023Const.PTN_INSATSU_END_Y_MIN, srDpprintData));
        // 開始ﾃﾝｼｮﾝ計
        this.setItemData(processData, GXHDO101B023Const.KAISHI_TENSION_KEI, getSrDpprintItemData(GXHDO101B023Const.KAISHI_TENSION_KEI, srDpprintData));
        // 開始ﾃﾝｼｮﾝ前
        this.setItemData(processData, GXHDO101B023Const.KAISHI_TENSION_MAE, getSrDpprintItemData(GXHDO101B023Const.KAISHI_TENSION_MAE, srDpprintData));
        // 開始ﾃﾝｼｮﾝ奥
        this.setItemData(processData, GXHDO101B023Const.KAISHI_TENSION_OKU, getSrDpprintItemData(GXHDO101B023Const.KAISHI_TENSION_OKU, srDpprintData));
        // 終了ﾃﾝｼｮﾝ計
        this.setItemData(processData, GXHDO101B023Const.SHURYOU_TENSION_KEI, getSrDpprintItemData(GXHDO101B023Const.SHURYOU_TENSION_KEI, srDpprintData));
        // 終了ﾃﾝｼｮﾝ前
        this.setItemData(processData, GXHDO101B023Const.SHURYOU_TENSION_MAE, getSrDpprintItemData(GXHDO101B023Const.SHURYOU_TENSION_MAE, srDpprintData));
        // 終了ﾃﾝｼｮﾝ奥
        this.setItemData(processData, GXHDO101B023Const.SHURYOU_TENSION_OKU, getSrDpprintItemData(GXHDO101B023Const.SHURYOU_TENSION_OKU, srDpprintData));
        // 終了時ﾆｼﾞﾐ・ｶｽﾚ確認
        this.setItemData(processData, GXHDO101B023Const.SHURYOU_JI_NIJIMI_KASURE_CHECK, getSrDpprintItemData(GXHDO101B023Const.SHURYOU_JI_NIJIMI_KASURE_CHECK, srDpprintData));
        // 印刷エンド時担当者
        this.setItemData(processData, GXHDO101B023Const.INSATSU_ENDJI_TANTOUSHA, getSrDpprintItemData(GXHDO101B023Const.INSATSU_ENDJI_TANTOUSHA, srDpprintData));
        // 印刷枚数
        this.setItemData(processData, GXHDO101B023Const.INSATSU_MAISUU, getSrDpprintItemData(GXHDO101B023Const.INSATSU_MAISUU, srDpprintData));
        //備考1
        this.setItemData(processData, GXHDO101B023Const.BIKOU1, getSrDpprintItemData(GXHDO101B023Const.BIKOU1, srDpprintData));
        //備考2
        this.setItemData(processData, GXHDO101B023Const.BIKOU2, getSrDpprintItemData(GXHDO101B023Const.BIKOU2, srDpprintData));
        // ﾌﾞﾚｰﾄﾞ印刷長
        this.setItemData(processData, GXHDO101B023Const.BLADEINSATSUTYOU, getSrDpprintItemData(GXHDO101B023Const.BLADEINSATSUTYOU, srDpprintData));
        // ｽﾞﾚ量基準値X
        this.setItemData(processData, GXHDO101B023Const.ZURERYOUKIJUNCHIX, getSrDpprintItemData(GXHDO101B023Const.ZURERYOUKIJUNCHIX, srDpprintData));
        // ｽﾞﾚ量基準値Y
        this.setItemData(processData, GXHDO101B023Const.ZURERYOUKIJUNCHIY, getSrDpprintItemData(GXHDO101B023Const.ZURERYOUKIJUNCHIY, srDpprintData));
        // 合わせ精度 蛇行
        this.setItemData(processData, GXHDO101B023Const.AWASESEIDODAKOU, getSrDpprintItemData(GXHDO101B023Const.AWASESEIDODAKOU, srDpprintData));
        // 合わせ精度 流れ
        this.setItemData(processData, GXHDO101B023Const.AWASESEIDONAGARE, getSrDpprintItemData(GXHDO101B023Const.AWASESEIDONAGARE, srDpprintData));
        // 先行ﾛｯﾄNo
        this.setItemData(processData, GXHDO101B023Const.SENKOULOTNO, getSrDpprintItemData(GXHDO101B023Const.SENKOULOTNO, srDpprintData));
        // ﾃｰﾌﾟ使い切り
        this.setItemData(processData, GXHDO101B023Const.TAPETSUKAIKIRI, getSrDpprintItemData(GXHDO101B023Const.TAPETSUKAIKIRI, srDpprintData));
        // 次ﾛｯﾄへ
        this.setItemData(processData, GXHDO101B023Const.JILOTHE, getSrDpprintItemData(GXHDO101B023Const.JILOTHE, srDpprintData));
        // 成形長さ
        this.setItemData(processData, GXHDO101B023Const.SEIKEINAGASA, getSrDpprintItemData(GXHDO101B023Const.SEIKEINAGASA, srDpprintData));
        // 備考3
        this.setItemData(processData, GXHDO101B023Const.BIKOU3, getSrDpprintItemData(GXHDO101B023Const.BIKOU3, srDpprintData));
        // 備考4
        this.setItemData(processData, GXHDO101B023Const.BIKOU4, getSrDpprintItemData(GXHDO101B023Const.BIKOU4, srDpprintData));
        // 備考5
        this.setItemData(processData, GXHDO101B023Const.BIKOU5, getSrDpprintItemData(GXHDO101B023Const.BIKOU5, srDpprintData));
        // 印刷長さ
        this.setItemData(processData, GXHDO101B023Const.PRINTLENGTH, getSrDpprintItemData(GXHDO101B023Const.PRINTLENGTH, srDpprintData));

    }

    /**
     * 膜厚入力画面データ設定処理
     *
     * @param subSrDpprintData 印刷・DP2_ｻﾌﾞ画面データ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     */
    private void setInputItemDataSubFormC001(SubSrDpprint subSrDpprintData, String kojyo, String lotNo, String edaban) {
        // サブ画面の情報を取得
        GXHDO101C001 beanGXHDO101C001 = (GXHDO101C001) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C001);

        //データの設定
        String[] makuatsuStart;
        String[] makuatsuEnd;
        beanGXHDO101C001.setKojyo(kojyo); //工場ｺｰﾄﾞ
        beanGXHDO101C001.setLotno(lotNo); //ﾛｯﾄNo
        beanGXHDO101C001.setEdaban(edaban); //枝番

        GXHDO101C001Model model;
        if (subSrDpprintData == null) {
            // 登録データが無い場合空の状態で初期値をセットする。
            makuatsuStart = new String[]{"", "", "", "", "", "", "", "", ""}; //膜厚スタート1～9
            makuatsuEnd = new String[]{"", "", "", "", "", "", "", "", ""}; //膜厚エンド1～9
            model = GXHDO101C001Logic.createGXHDO101C001Model(makuatsuStart, makuatsuEnd);

        } else {
            // 登録データがあれば登録データをセットする。
            //膜厚スタート1～9
            makuatsuStart = new String[]{
                StringUtil.nullToBlank(subSrDpprintData.getMakuatsuStart1()),
                StringUtil.nullToBlank(subSrDpprintData.getMakuatsuStart2()),
                StringUtil.nullToBlank(subSrDpprintData.getMakuatsuStart3()),
                StringUtil.nullToBlank(subSrDpprintData.getMakuatsuStart4()),
                StringUtil.nullToBlank(subSrDpprintData.getMakuatsuStart5()),
                StringUtil.nullToBlank(subSrDpprintData.getMakuatsuStart6()),
                StringUtil.nullToBlank(subSrDpprintData.getMakuatsuStart7()),
                StringUtil.nullToBlank(subSrDpprintData.getMakuatsuStart8()),
                StringUtil.nullToBlank(subSrDpprintData.getMakuatsuStart9())};
            //膜厚エンド1～9
            makuatsuEnd = new String[]{
                StringUtil.nullToBlank(subSrDpprintData.getMakuatsuEnd1()),
                StringUtil.nullToBlank(subSrDpprintData.getMakuatsuEnd2()),
                StringUtil.nullToBlank(subSrDpprintData.getMakuatsuEnd3()),
                StringUtil.nullToBlank(subSrDpprintData.getMakuatsuEnd4()),
                StringUtil.nullToBlank(subSrDpprintData.getMakuatsuEnd5()),
                StringUtil.nullToBlank(subSrDpprintData.getMakuatsuEnd6()),
                StringUtil.nullToBlank(subSrDpprintData.getMakuatsuEnd7()),
                StringUtil.nullToBlank(subSrDpprintData.getMakuatsuEnd8()),
                StringUtil.nullToBlank(subSrDpprintData.getMakuatsuEnd9())
            };
            model = GXHDO101C001Logic.createGXHDO101C001Model(makuatsuStart, makuatsuEnd);
        }

        // サブ画面から戻ったときに値を設定する項目を指定する。
        model.setReturnItemIdStartAve(GXHDO101B023Const.INSATSU_START_MAKUATSU_AVE);
        model.setReturnItemIdStartMax(GXHDO101B023Const.INSATSU_START_MAKUATSU_MAX);
        model.setReturnItemIdStartMin(GXHDO101B023Const.INSATSU_START_MAKUATSU_MIN);
        model.setReturnItemIdStartCv(GXHDO101B023Const.INSATSU_START_MAKUATSU_CV);
        model.setReturnItemIdEndAve(GXHDO101B023Const.INSATSU_END_MAKUATSU_AVE);
        model.setReturnItemIdEndMax(GXHDO101B023Const.INSATSU_END_MAKUATSU_MAX);
        model.setReturnItemIdEndMin(GXHDO101B023Const.INSATSU_END_MAKUATSU_MIN);
        model.setReturnItemIdEndCv(GXHDO101B023Const.INSATSU_END_MAKUATSU_CV);
        beanGXHDO101C001.setGxhdO101c001Model(model);
    }

    /**
     * PTN距離X入力画面データ設定処理
     *
     * @param subSrDpprintData 印刷・DP2_ｻﾌﾞ画面データ
     */
    private void setInputItemDataSubFormC002(SubSrDpprint subSrDpprintData) {

        // PTN距離Xサブ画面初期表示データ設定
        GXHDO101C002 beanGXHDO101C002 = (GXHDO101C002) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C002);
        //データの設定
        String[] startPtnDistX;
        String[] startPtnDistY;
        GXHDO101C002Model model;
        if (subSrDpprintData == null) {
            startPtnDistX = new String[]{"", "", "", "", ""}; //PTN距離XStart
            startPtnDistY = new String[]{"", "", "", "", ""}; //PTN距離YStart

            model = GXHDO101C002Logic.createGXHDO101C002Model(startPtnDistX, startPtnDistY);

        } else {
            //PTN距離Xスタート1～5
            startPtnDistX = new String[]{
                StringUtil.nullToBlank(subSrDpprintData.getStartPtnDistX1()),
                StringUtil.nullToBlank(subSrDpprintData.getStartPtnDistX2()),
                StringUtil.nullToBlank(subSrDpprintData.getStartPtnDistX3()),
                StringUtil.nullToBlank(subSrDpprintData.getStartPtnDistX4()),
                StringUtil.nullToBlank(subSrDpprintData.getStartPtnDistX5())};
            //PTN距離Yスタート1～5
            startPtnDistY = new String[]{
                StringUtil.nullToBlank(subSrDpprintData.getStartPtnDistY1()),
                StringUtil.nullToBlank(subSrDpprintData.getStartPtnDistY2()),
                StringUtil.nullToBlank(subSrDpprintData.getStartPtnDistY3()),
                StringUtil.nullToBlank(subSrDpprintData.getStartPtnDistY4()),
                StringUtil.nullToBlank(subSrDpprintData.getStartPtnDistY5())};

            model = GXHDO101C002Logic.createGXHDO101C002Model(startPtnDistX, startPtnDistY);

        }
        model.setReturnItemIdStartXMin(GXHDO101B023Const.PTN_INSATSU_START_X_MIN);
        model.setReturnItemIdStartYMin(GXHDO101B023Const.PTN_INSATSU_START_Y_MIN);
        beanGXHDO101C002.setGxhdO101c002Model(model);
    }

    /**
     * PTN距離Y入力画面データ設定処理
     *
     * @param subSrDpprintData 印刷・DP2_ｻﾌﾞ画面データ
     */
    private void setInputItemDataSubFormC003(SubSrDpprint subSrDpprintData) {

        // PTN距離Yサブ画面初期表示データ設定
        GXHDO101C003 beanGXHDO101C003 = (GXHDO101C003) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C003);
        //データの設定
        String[] endPtnDistX;
        String[] endPtnDistY;
        GXHDO101C003Model model;
        if (subSrDpprintData == null) {
            endPtnDistX = new String[]{"", "", "", "", ""}; //PTN距離XEnd
            endPtnDistY = new String[]{"", "", "", "", ""}; //PTN距離YEnd
            model = GXHDO101C003Logic.createGXHDO101C003Model(endPtnDistX, endPtnDistY);

        } else {
            //PTN距離Xエンド1～5
            endPtnDistX = new String[]{
                StringUtil.nullToBlank(subSrDpprintData.getEndPtnDistX1()),
                StringUtil.nullToBlank(subSrDpprintData.getEndPtnDistX2()),
                StringUtil.nullToBlank(subSrDpprintData.getEndPtnDistX3()),
                StringUtil.nullToBlank(subSrDpprintData.getEndPtnDistX4()),
                StringUtil.nullToBlank(subSrDpprintData.getEndPtnDistX5())};
            //PTN距離Yエンド1～5
            endPtnDistY = new String[]{
                StringUtil.nullToBlank(subSrDpprintData.getEndPtnDistY1()),
                StringUtil.nullToBlank(subSrDpprintData.getEndPtnDistY2()),
                StringUtil.nullToBlank(subSrDpprintData.getEndPtnDistY3()),
                StringUtil.nullToBlank(subSrDpprintData.getEndPtnDistY4()),
                StringUtil.nullToBlank(subSrDpprintData.getEndPtnDistY5())};
            model = GXHDO101C003Logic.createGXHDO101C003Model(endPtnDistX, endPtnDistY);

        }
        model.setReturnItemIdEndXMin(GXHDO101B023Const.PTN_INSATSU_END_X_MIN);
        model.setReturnItemIdEndYMin(GXHDO101B023Const.PTN_INSATSU_END_Y_MIN);
        beanGXHDO101C003.setGxhdO101c003Model(model);
    }

    /**
     * 印刷・DP2の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @param jissekino 実績No
     * @return 印刷・DP2登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrDpprint> getSrDpprintData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrDpprint(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
        } else {
            return loadTmpSrDpprint(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
        }
    }

    /**
     * 印刷・DP2_ｻﾌﾞ画面の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @return 印刷・DP2_ｻﾌﾞ画面データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrDpprint> getSubSrDpprintData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {
        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSubSrDpprint(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
        } else {
            return loadTmpSubSrDpprint(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
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
                put("PROCESS", "工程区分");
            }
        };

        return map;
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
     * @param jissekino 実績No(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhdd03RevInfo(QueryRunner queryRunnerDoc, String kojyo, String lotNo,
            String edaban, int jissekino, String formId) throws SQLException {
        // 設計データの取得
        String sql = "SELECT rev, jotai_flg "
                + "FROM fxhdd03 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND jissekino = ? "
                + "AND gamen_id = ? ";

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
        // 設計データの取得
        String sql = "SELECT rev, jotai_flg "
                + "FROM fxhdd03 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND jissekino = ? "
                + "AND gamen_id = ? "
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
        // 設計データの取得
        String sql = "SELECT MAX(rev) AS rev "
                + "FROM fxhdd03 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND jissekino = ? "
                + "AND gamen_id = ? ";

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
     * [印刷・DP2]から、ﾃﾞｰﾀを取得
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
    private List<SrDpprint> loadSrDpprint(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, String rev) throws SQLException {

        String sql = "SELECT kojyo,lotno,edaban,kaisuu,kcpno,kouteikubun,tapelotno,petfilmsyurui,taperollno1,taperollno2,taperollno3,pastelotno,"
                + "pastenendo,pasteondo,pkokeibun1,pastelotno2,handoumei,handouno,handoumaisuu,bladeno,bladegaikan,BladeATu,AtudoNo,AtudoMaisuu,"
                + "AtuDoATu,gouki,kansouondo,kansouondo2,kansouondo3,kansouondo4,kansouondo5,hansouspeed,startdatetime,tantousya,kakuninsya,makuatuave_start,"
                + "makuatumax_start,makuatumin_start,makuatucv_start,nijimikasure_start,start_ptn_dist_x,start_ptn_dist_y,TensionS_sum,TensionStemae,"
                + "TensionSoku,enddatetime,tanto_end,printmaisuu,makuatuave_end,makuatumax_end,makuatumin_end,makuatucv_end,nijimikasure_end,end_ptn_dist_x,"
                + "end_ptn_dist_y,TensionE_sum,TensionEtemae,TensionEoku,genryoukigou,"
                + "bladeinsatsutyou,zureryoukijunchix,zureryoukijunchiy,awaseseidodakou,awaseseidonagare,CONCAT(skojyo , slotno , sedaban) senkoulotno,"
                + "tapetsukaikiri,jilothe,seikeinagasa,bikou3,bikou4,bikou5,"
                + "bikou1,bikou2,torokunichiji,kosinnichiji,revision,'0' AS deleteflag,tapeatu,printlength,kansouondoshita,kansouondoshita2,kansouondoshita3,kansouondoshita4 "
                + "FROM sr_dpprint "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? ";
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
        mapping.put("kaisuu", "kaisuu"); //回数
        mapping.put("kcpno", "kcpno"); //KCPNO
        mapping.put("kouteikubun", "kouteikubun"); //工程区分
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
        mapping.put("handoumei", "handoumei"); //版胴名
        mapping.put("handouno", "handouno"); //版胴No
        mapping.put("handoumaisuu", "handoumaisuu"); //版胴使用枚数
        mapping.put("bladeno", "bladeno"); //ﾌﾞﾚｰﾄﾞNo.
        mapping.put("bladegaikan", "bladegaikan"); //ﾌﾞﾚｰﾄﾞ外観
        mapping.put("BladeATu", "bladeatu"); //ﾌﾞﾚｰﾄﾞ圧力
        mapping.put("AtudoNo", "atudono"); //圧胴No
        mapping.put("AtudoMaisuu", "atudomaisuu"); //圧胴使用枚数
        mapping.put("AtuDoATu", "atudoatu"); //圧胴圧力
        mapping.put("gouki", "gouki"); //号機ｺｰﾄﾞ
        mapping.put("kansouondo", "kansouondo"); //乾燥温度
        mapping.put("kansouondo2", "kansouondo2"); //乾燥温度2
        mapping.put("kansouondo3", "kansouondo3"); //乾燥温度3
        mapping.put("kansouondo4", "kansouondo4"); //乾燥温度4
        mapping.put("kansouondo5", "kansouondo5"); //乾燥温度5
        mapping.put("hansouspeed", "hansouspeed"); //搬送速度
        mapping.put("startdatetime", "startdatetime"); //ﾌﾟﾘﾝﾄ開始日時
        mapping.put("tantousya", "tantousya"); //ｾｯﾃｨﾝｸﾞ担当者ｺｰﾄﾞ
        mapping.put("kakuninsya", "kakuninsya"); //印刷ｽﾀｰﾄ確認者
        mapping.put("makuatuave_start", "makuatuaveStart"); //ｽﾀｰﾄ時膜厚AVE
        mapping.put("makuatumax_start", "makuatumaxStart"); //ｽﾀｰﾄ時膜厚MAX
        mapping.put("makuatumin_start", "makuatuminStart"); //ｽﾀｰﾄ時膜厚MIN
        mapping.put("makuatucv_start", "makuatucvStart"); //印刷ｽﾀｰﾄ膜厚CV
        mapping.put("nijimikasure_start", "nijimikasureStart"); //ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
        mapping.put("start_ptn_dist_x", "startPtnDistX"); //ｽﾀｰﾄ時PTN間距離X
        mapping.put("start_ptn_dist_y", "startPtnDistY"); //ｽﾀｰﾄ時PTN間距離Y
        mapping.put("TensionS_sum", "tensionsSum"); //開始ﾃﾝｼｮﾝ計
        mapping.put("TensionStemae", "tensionstemae"); //ﾃﾝｼｮﾝ開始手前
        mapping.put("TensionSoku", "tensionsoku"); //ﾃﾝｼｮﾝ開始奥
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
        mapping.put("TensionE_sum", "tensioneSum"); //終了ﾃﾝｼｮﾝ計
        mapping.put("TensionEtemae", "tensionetemae"); //ﾃﾝｼｮﾝ終了手前
        mapping.put("TensionEoku", "tensioneoku"); //ﾃﾝｼｮﾝ終了奥
        mapping.put("genryoukigou", "genryoukigou"); //原料記号
        mapping.put("bladeinsatsutyou", "bladeinsatsutyou"); // ﾌﾞﾚｰﾄﾞ印刷長
        mapping.put("zureryoukijunchix", "zureryoukijunchix"); // ｽﾞﾚ量基準値X
        mapping.put("zureryoukijunchiy", "zureryoukijunchiy"); // ｽﾞﾚ量基準値Y
        mapping.put("awaseseidodakou", "awaseseidodakou"); // 合わせ精度 蛇行
        mapping.put("awaseseidonagare", "awaseseidonagare"); // 合わせ精度 流れ
        mapping.put("senkoulotno", "senkoulotno"); // 先行ﾛｯﾄNo
        mapping.put("tapetsukaikiri", "tapetsukaikiri"); // ﾃｰﾌﾟ使い切り
        mapping.put("jilothe", "jilothe"); // 次ﾛｯﾄへ
        mapping.put("seikeinagasa", "seikeinagasa"); // 成形長さ
        mapping.put("bikou3", "bikou3"); // 備考3
        mapping.put("bikou4", "bikou4"); // 備考4
        mapping.put("bikou5", "bikou5"); // 備考5
        mapping.put("bikou1", "bikou1"); //備考1
        mapping.put("bikou2", "bikou2"); //備考2
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); //更新日時
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ
        mapping.put("tapeatu", "tapeatu"); //テープ厚み
        mapping.put("printlength", "printlength"); //印刷長さ
        mapping.put("kansouondoshita", "kansouondoshita"); //乾燥温度下側
        mapping.put("kansouondoshita2", "kansouondoshita2"); //乾燥温度下側2
        mapping.put("kansouondoshita3", "kansouondoshita3"); //乾燥温度下側3
        mapping.put("kansouondoshita4", "kansouondoshita4"); //乾燥温度下側4

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrDpprint>> beanHandler = new BeanListHandler<>(SrDpprint.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [印刷・DP2_ｻﾌﾞ画面]から、ﾃﾞｰﾀを取得
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
    private List<SubSrDpprint> loadSubSrDpprint(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, String rev) throws SQLException {

        String sql = "SELECT kojyo,lotno,edaban,kaisuu,makuatsu_start1,makuatsu_start2,"
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
                + "FROM sub_sr_dpprint "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? ";
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
        mapping.put("kaisuu", "kaisuu"); //回数
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
        ResultSetHandler<List<SubSrDpprint>> beanHandler = new BeanListHandler<>(SubSrDpprint.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [印刷・DP2_仮登録]から、ﾃﾞｰﾀを取得
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
    private List<SrDpprint> loadTmpSrDpprint(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, String rev) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,kaisuu,kcpno,kouteikubun,tapelotno,petfilmsyurui,taperollno1,taperollno2,taperollno3,pastelotno,"
                + "pastenendo,pasteondo,pkokeibun1,pastelotno2,handoumei,handouno,handoumaisuu,bladeno,bladegaikan,BladeATu,AtudoNo,AtudoMaisuu,"
                + "AtuDoATu,gouki,kansouondo,kansouondo2,kansouondo3,kansouondo4,kansouondo5,hansouspeed,startdatetime,tantousya,kakuninsya,makuatuave_start,"
                + "makuatumax_start,makuatumin_start,makuatucv_start,nijimikasure_start,start_ptn_dist_x,start_ptn_dist_y,TensionS_sum,TensionStemae,"
                + "TensionSoku,enddatetime,tanto_end,printmaisuu,makuatuave_end,makuatumax_end,makuatumin_end,makuatucv_end,nijimikasure_end,end_ptn_dist_x,"
                + "end_ptn_dist_y,TensionE_sum,TensionEtemae,TensionEoku,genryoukigou,"
                + "bladeinsatsutyou,zureryoukijunchix,zureryoukijunchiy,awaseseidodakou,awaseseidonagare,CONCAT(skojyo , slotno , sedaban) senkoulotno,"
                + "tapetsukaikiri,jilothe,seikeinagasa,bikou3,bikou4,bikou5,"
                + "bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag,tapeatu,printlength,kansouondoshita,kansouondoshita2,kansouondoshita3,kansouondoshita4  "
                + "FROM tmp_sr_dpprint "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND deleteflag = ? ";
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
        mapping.put("kaisuu", "kaisuu"); //回数
        mapping.put("kcpno", "kcpno"); //KCPNO
        mapping.put("kouteikubun", "kouteikubun"); //工程区分
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
        mapping.put("handoumei", "handoumei"); //版胴名
        mapping.put("handouno", "handouno"); //版胴No
        mapping.put("handoumaisuu", "handoumaisuu"); //版胴使用枚数
        mapping.put("bladeno", "bladeno"); //ﾌﾞﾚｰﾄﾞNo.
        mapping.put("bladegaikan", "bladegaikan"); //ﾌﾞﾚｰﾄﾞ外観
        mapping.put("BladeATu", "bladeatu"); //ﾌﾞﾚｰﾄﾞ圧力
        mapping.put("AtudoNo", "atudono"); //圧胴No
        mapping.put("AtudoMaisuu", "atudomaisuu"); //圧胴使用枚数
        mapping.put("AtuDoATu", "atudoatu"); //圧胴圧力
        mapping.put("gouki", "gouki"); //号機ｺｰﾄﾞ
        mapping.put("kansouondo", "kansouondo"); //乾燥温度
        mapping.put("kansouondo2", "kansouondo2"); //乾燥温度2
        mapping.put("kansouondo3", "kansouondo3"); //乾燥温度3
        mapping.put("kansouondo4", "kansouondo4"); //乾燥温度4
        mapping.put("kansouondo5", "kansouondo5"); //乾燥温度5
        mapping.put("hansouspeed", "hansouspeed"); //搬送速度
        mapping.put("startdatetime", "startdatetime"); //ﾌﾟﾘﾝﾄ開始日時
        mapping.put("tantousya", "tantousya"); //ｾｯﾃｨﾝｸﾞ担当者ｺｰﾄﾞ
        mapping.put("kakuninsya", "kakuninsya"); //印刷ｽﾀｰﾄ確認者
        mapping.put("makuatuave_start", "makuatuaveStart"); //ｽﾀｰﾄ時膜厚AVE
        mapping.put("makuatumax_start", "makuatumaxStart"); //ｽﾀｰﾄ時膜厚MAX
        mapping.put("makuatumin_start", "makuatuminStart"); //ｽﾀｰﾄ時膜厚MIN
        mapping.put("makuatucv_start", "makuatucvStart"); //印刷ｽﾀｰﾄ膜厚CV
        mapping.put("nijimikasure_start", "nijimikasureStart"); //ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
        mapping.put("start_ptn_dist_x", "startPtnDistX"); //ｽﾀｰﾄ時PTN間距離X
        mapping.put("start_ptn_dist_y", "startPtnDistY"); //ｽﾀｰﾄ時PTN間距離Y
        mapping.put("TensionS_sum", "tensionsSum"); //開始ﾃﾝｼｮﾝ計
        mapping.put("TensionStemae", "tensionstemae"); //ﾃﾝｼｮﾝ開始手前
        mapping.put("TensionSoku", "tensionsoku"); //ﾃﾝｼｮﾝ開始奥
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
        mapping.put("TensionE_sum", "tensioneSum"); //終了ﾃﾝｼｮﾝ計
        mapping.put("TensionEtemae", "tensionetemae"); //ﾃﾝｼｮﾝ終了手前
        mapping.put("TensionEoku", "tensioneoku"); //ﾃﾝｼｮﾝ終了奥
        mapping.put("genryoukigou", "genryoukigou"); //原料記号
        mapping.put("bladeinsatsutyou", "bladeinsatsutyou"); // ﾌﾞﾚｰﾄﾞ印刷長
        mapping.put("zureryoukijunchix", "zureryoukijunchix"); // ｽﾞﾚ量基準値X
        mapping.put("zureryoukijunchiy", "zureryoukijunchiy"); // ｽﾞﾚ量基準値Y
        mapping.put("awaseseidodakou", "awaseseidodakou"); // 合わせ精度 蛇行
        mapping.put("awaseseidonagare", "awaseseidonagare"); // 合わせ精度 流れ
        mapping.put("senkoulotno", "senkoulotno"); // 先行ﾛｯﾄNo
        mapping.put("tapetsukaikiri", "tapetsukaikiri"); // ﾃｰﾌﾟ使い切り
        mapping.put("jilothe", "jilothe"); // 次ﾛｯﾄへ
        mapping.put("seikeinagasa", "seikeinagasa"); // 成形長さ
        mapping.put("bikou3", "bikou3"); // 備考3
        mapping.put("bikou4", "bikou4"); // 備考4
        mapping.put("bikou5", "bikou5"); // 備考5
        mapping.put("bikou1", "bikou1"); //備考1
        mapping.put("bikou2", "bikou2"); //備考2
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); //更新日時
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ
        mapping.put("tapeatu", "tapeatu"); //削除ﾌﾗｸﾞ
        mapping.put("printlength", "printlength"); //印刷長さ
        mapping.put("kansouondoshita", "kansouondoshita"); //乾燥温度下側
        mapping.put("kansouondoshita2", "kansouondoshita2"); //乾燥温度下側2
        mapping.put("kansouondoshita3", "kansouondoshita3"); //乾燥温度下側3
        mapping.put("kansouondoshita4", "kansouondoshita4"); //乾燥温度下側4

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrDpprint>> beanHandler = new BeanListHandler<>(SrDpprint.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [印刷・DP2_ｻﾌﾞ画面_仮登録]から、ﾃﾞｰﾀを取得
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
    private List<SubSrDpprint> loadTmpSubSrDpprint(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, String rev) throws SQLException {

        String sql = "SELECT kojyo,lotno,edaban,kaisuu,makuatsu_start1,makuatsu_start2,"
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
                + "FROM tmp_sub_sr_dpprint "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND deleteflag = ? ";
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
        mapping.put("kaisuu", "kaisuu"); //回数
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
        ResultSetHandler<List<SubSrDpprint>> beanHandler = new BeanListHandler<>(SubSrDpprint.class, rowProcessor);

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
            int jissekino = Integer.parseInt(StringUtil.nullToBlank(session.getAttribute("jissekino")));

            //仕掛情報の取得
            Map shikakariData = loadShikakariData(queryRunnerWip, lotNo);
            String oyalotEdaban = StringUtil.nullToBlank(getMapData(shikakariData, "oyalotedaban")); //親ﾛｯﾄ枝番

            // 品質DB登録実績データ取得
            Map fxhdd03RevInfo = loadFxhdd03RevInfo(queryRunnerDoc, kojyo, lotNo8, oyalotEdaban, jissekino, formId);
            if (fxhdd03RevInfo == null || fxhdd03RevInfo.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            String jotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd03RevInfo, "jotai_flg"));

            if (!(JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg) || JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg))) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // 印刷・DP2データ取得
            List<SrDpprint> srDpprintDataList = getSrDpprintData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban, jissekino);
            if (srDpprintDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // 印刷・DP2_ｻﾌﾞ画面データ取得
            List<SubSrDpprint> subSrDpprintDataList = getSubSrDpprintData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban, jissekino);
            if (subSrDpprintDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // メイン画面データ設定
            setInputItemDataMainForm(processData, srDpprintDataList.get(0));

            // 膜厚入力画面データ設定 ※工場ｺｰﾄﾞ、ﾛｯﾄNo、枝番は親ではなく自身の値を渡す。
            //   →子画面側処理で自身の枝番を保持しておく必要がある。データ自体は親データの枝番で検索済みのものを引き渡す。
            setInputItemDataSubFormC001(subSrDpprintDataList.get(0), kojyo, lotNo8, edaban);

            // PTN距離X入力画面データ設定
            setInputItemDataSubFormC002(subSrDpprintDataList.get(0));

            // PTN距離Y入力画面データ設定
            setInputItemDataSubFormC003(subSrDpprintDataList.get(0));
            
            // 前工程WIP取込画面データ設定
            // ※膜厚入力画面とは異なり、下記メソッド内で親データの検索を実行しているため親データの枝番、状態フラグを引き渡す。
            //   また前工程WIP取込画面自体で自身の枝番は参照不要
            setInputItemDataSubFormC020(queryRunnerQcdb, kojyo, lotNo8, oyalotEdaban, jotaiFlg, jissekino);

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
     * @param srDpprintData 印刷・DP2
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrDpprint srDpprintData) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srDpprintData != null) {
            // 元データが存在する場合元データより取得
            return getSrDpprintItemData(itemId, srDpprintData);
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
     * 印刷・DP2_仮登録(tmp_sr_dpprint)登録処理
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
     * @param hiddenDataMap 隠しデータMAP
     * @throws SQLException 例外エラー
     */
    private void insertTmpSrDpprint(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, Map<String, Object> hiddenDataMap) throws SQLException {

        String sql = "INSERT INTO tmp_sr_dpprint ("
                + "kojyo,lotno,edaban,kaisuu,kcpno,kouteikubun,tapelotno,petfilmsyurui,taperollno1,taperollno2,taperollno3,pastelotno,pastenendo,pasteondo,pkokeibun1,"
                + "pastelotno2,handoumei,handouno,handoumaisuu,bladeno,bladegaikan,BladeATu,AtudoNo,AtudoMaisuu,AtuDoATu,gouki,kansouondo,kansouondo2,kansouondo3,kansouondo4,"
                + "kansouondo5,hansouspeed,startdatetime,tantousya,kakuninsya,makuatuave_start,makuatumax_start,makuatumin_start,makuatucv_start,nijimikasure_start,"
                + "start_ptn_dist_x,start_ptn_dist_y,TensionS_sum,TensionStemae,TensionSoku,enddatetime,tanto_end,printmaisuu,makuatuave_end,makuatumax_end,makuatumin_end,"
                + "makuatucv_end,nijimikasure_end,end_ptn_dist_x,end_ptn_dist_y,TensionE_sum,TensionEtemae,TensionEoku,genryoukigou,"
                + "bladeinsatsutyou,zureryoukijunchix,zureryoukijunchiy,awaseseidodakou,awaseseidonagare,skojyo,slotno,sedaban,tapetsukaikiri,jilothe,seikeinagasa,bikou3,bikou4,bikou5,"
                + "bikou1,bikou2,torokunichiji,kosinnichiji,"
                + "revision,deleteflag,tapeatu,printlength,kansouondoshita,kansouondoshita2,kansouondoshita3,kansouondoshita4"
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrDpprint(true, newRev, deleteflag, kojyo, lotNo, edaban, jissekino, systemTime, itemList, null, hiddenDataMap);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷・DP2_仮登録(tmp_sr_dpprint)更新処理
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
     * @param hiddenDataMap 隠しデータMAP
     * @throws SQLException 例外エラー
     */
    private void updateTmpSrDpprint(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, Map<String, Object> hiddenDataMap) throws SQLException {

        String sql = "UPDATE tmp_sr_dpprint SET "
                + "kcpno = ?,kouteikubun = ?,tapelotno = ?,petfilmsyurui = ?,taperollno1 = ?,taperollno2 = ?,taperollno3 = ?,pastelotno = ?,pastenendo = ?,pasteondo = ?,"
                + "pkokeibun1 = ?,pastelotno2 = ?,handoumei = ?,handouno = ?,handoumaisuu = ?,bladeno = ?,bladegaikan = ?,BladeATu = ?,AtudoNo = ?,AtudoMaisuu = ?,AtuDoATu = ?,"
                + "gouki = ?,kansouondo = ?,kansouondo2 = ?,kansouondo3 = ?,kansouondo4 = ?,kansouondo5 = ?,hansouspeed = ?,startdatetime = ?,tantousya = ?,kakuninsya = ?,makuatuave_start = ?,"
                + "makuatumax_start = ?,makuatumin_start = ?,makuatucv_start = ?,nijimikasure_start = ?,start_ptn_dist_x = ?,start_ptn_dist_y = ?,TensionS_sum = ?,TensionStemae = ?,TensionSoku = ?,"
                + "enddatetime = ?,tanto_end = ?,printmaisuu = ?,makuatuave_end = ?,makuatumax_end = ?,makuatumin_end = ?,makuatucv_end = ?,nijimikasure_end = ?,end_ptn_dist_x = ?,"
                + "end_ptn_dist_y = ?,TensionE_sum = ?,TensionEtemae = ?,TensionEoku = ?,genryoukigou = ?,"
                + "bladeinsatsutyou = ?,zureryoukijunchix = ?,zureryoukijunchiy = ?,awaseseidodakou = ?,awaseseidonagare = ?,skojyo = ?,slotno = ?,sedaban = ?,tapetsukaikiri = ?,jilothe = ?,seikeinagasa = ?,"
                + "bikou3 = ?,bikou4 = ?,bikou5 = ?,bikou1 = ?,bikou2 = ?,kosinnichiji = ?,revision = ?,deleteflag = ?,tapeatu = ?,printlength = ? ,kansouondoshita = ?,kansouondoshita2 = ?,kansouondoshita3 = ?,kansouondoshita4 = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrDpprint> srDpprintList = getSrDpprintData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekino);
        SrDpprint srDpprint = null;
        if (!srDpprintList.isEmpty()) {
            srDpprint = srDpprintList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrDpprint(false, newRev, 0, "", "", "", jissekino, systemTime, itemList, srDpprint, hiddenDataMap);

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
     * 印刷・DP2_仮登録(tmp_sr_dpprint)削除処理
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
    private void deleteTmpSrDpprint(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        String sql = "DELETE FROM tmp_sr_dpprint "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND revision = ? ";

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
     * 印刷・DP2_仮登録(tmp_sr_dpprint)更新値パラメータ設定
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
     * @param srDpprintData 印刷・DP2データ
     * @param hiddenDataMap 隠しデータMAP
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrDpprint(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, SrDpprint srDpprintData, Map<String, Object> hiddenDataMap) {

        List<Object> params = new ArrayList<>();
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
            params.add(jissekino); //回数
        }
        
        // 先行ﾛｯﾄNo
        String senkoulotno = getItemData(itemList, GXHDO101B023Const.SENKOULOTNO, srDpprintData);
        String skKojyo = ""; //先行工場ｺｰﾄﾞ
        String skLotNo = ""; // 先行ﾛｯﾄNo
        String skEdaban = ""; // 先行枝番
        if (!StringUtil.isEmpty(senkoulotno)) {
            skKojyo = StringUtils.substring(senkoulotno, 0, 3);
            skLotNo = StringUtils.substring(senkoulotno, 3, 11);
            skEdaban = StringUtil.blankToNull(StringUtils.substring(senkoulotno, 11, 14));
        }

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.KCPNO, srDpprintData))); //KCPNO
        params.add(DBUtil.stringToStringObjectDefaultNull(StringUtil.nullToBlank(getMapData(hiddenDataMap, "koteiKbn")))); // 工程区分
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.SLIP_LOTNO, srDpprintData))); //ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.PET_FILM_SHURUI, srDpprintData))); //PETﾌｨﾙﾑ種類
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.ROLL_NO1, srDpprintData))); //ﾃｰﾌﾟﾛｰﾙNo1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.ROLL_NO2, srDpprintData))); //ﾃｰﾌﾟﾛｰﾙNo2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.ROLL_NO3, srDpprintData))); //ﾃｰﾌﾟﾛｰﾙNo3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.PASTE_LOT_NO1, srDpprintData))); //ﾍﾟｰｽﾄﾛｯﾄNo1
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.PASTE_NENDO1, srDpprintData))); //ﾍﾟｰｽﾄ粘度1
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.PASTE_ONDO1, srDpprintData))); //ﾍﾟｰｽﾄ温度1
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.PASTE_KOKEIBUN1, srDpprintData))); //ﾍﾟｰｽﾄ固形分1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.PASTE_LOT_NO2, srDpprintData))); //ﾍﾟｰｽﾄﾛｯﾄNo2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.HANDOU_MEI, srDpprintData))); //版胴名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.HANDOU_LOTNO, srDpprintData))); //版胴ﾛｯﾄNo
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.HANDOU_SHIYOU_MAISUU, srDpprintData))); //版胴使用枚数 
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.BLADE_NO, srDpprintData))); //ﾌﾞﾚｰﾄﾞNo.
        //ﾌﾞﾚｰﾄﾞ外観
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B023Const.BLADE_GAIKAN, srDpprintData))) {
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
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.BLADE_ATSURYOKU, srDpprintData))); //ﾌﾞﾚｰﾄﾞ圧力
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.ATSUDOU_LOTNO, srDpprintData))); //圧胴No
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.ATSUDOU_SIYOU_MAISUU, srDpprintData))); //圧胴使用枚数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.ATSUDOU_ATSURYOKU, srDpprintData))); //圧胴圧力
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.INSATSU_GOUKI, srDpprintData))); //号機ｺｰﾄﾞ
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI1, srDpprintData))); //乾燥温度
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI2, srDpprintData))); //乾燥温度2
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI3, srDpprintData))); //乾燥温度3
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI4, srDpprintData))); //乾燥温度4
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI5, srDpprintData))); //乾燥温度5
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.HANSOU_SOKUDO, srDpprintData))); //搬送速度
        params.add(DBUtil.stringToDateObjectDefaultNull(
                getItemData(itemList, GXHDO101B023Const.INSATSU_KAISHI_DAY, srDpprintData),
                getItemData(itemList, GXHDO101B023Const.INSATSU_KAISHI_TIME, srDpprintData))); //ﾌﾟﾘﾝﾄ開始日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.INSATSU_STARTJI_TANTOUSHA, srDpprintData))); //ｾｯﾃｨﾝｸﾞ担当者ｺｰﾄﾞ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.INSATSU_STARTJI_KAKUNINSHA, srDpprintData))); //印刷ｽﾀｰﾄ確認者
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.INSATSU_START_MAKUATSU_AVE, srDpprintData))); //ｽﾀｰﾄ時膜厚AVE
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.INSATSU_START_MAKUATSU_MAX, srDpprintData))); //ｽﾀｰﾄ時膜厚MAX
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.INSATSU_START_MAKUATSU_MIN, srDpprintData))); //ｽﾀｰﾄ時膜厚MIN
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.INSATSU_START_MAKUATSU_CV, srDpprintData))); //印刷ｽﾀｰﾄ膜厚CV
        //ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B023Const.STARTJI_NIJIMI_KASURE_CHECK, srDpprintData))) {
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
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.PTN_INSATSU_START_X_MIN, srDpprintData))); //ｽﾀｰﾄ時PTN間距離X
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.PTN_INSATSU_START_Y_MIN, srDpprintData))); //ｽﾀｰﾄ時PTN間距離Y
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.KAISHI_TENSION_KEI, srDpprintData))); //開始ﾃﾝｼｮﾝ計
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.KAISHI_TENSION_MAE, srDpprintData))); //ﾃﾝｼｮﾝ開始手前
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.KAISHI_TENSION_OKU, srDpprintData))); //ﾃﾝｼｮﾝ開始奥
        params.add(DBUtil.stringToDateObjectDefaultNull(
                getItemData(itemList, GXHDO101B023Const.INSATSU_SHURYOU_DAY, srDpprintData),
                getItemData(itemList, GXHDO101B023Const.INSATSU_SHURYOU_TIME, srDpprintData))); //ﾌﾟﾘﾝﾄ終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.INSATSU_ENDJI_TANTOUSHA, srDpprintData))); //終了時担当者ｺｰﾄﾞ
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.INSATSU_MAISUU, srDpprintData))); //印刷枚数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.INSATSU_END_MAKUATSU_AVE, srDpprintData))); //終了時膜厚AVE
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.INSATSU_END_MAKUATSU_MAX, srDpprintData))); //終了時膜厚MAX
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.INSATSU_END_MAKUATSU_MIN, srDpprintData))); //終了時膜厚MIN
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.INSATSU_END_MAKUATSU_CV, srDpprintData))); //印刷ｴﾝﾄﾞ膜厚CV
        //終了時ﾆｼﾞﾐ・ｶｽﾚ確認
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B023Const.SHURYOU_JI_NIJIMI_KASURE_CHECK, srDpprintData))) {
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
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.PTN_INSATSU_END_X_MIN, srDpprintData))); //終了時PTN間距離X
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.PTN_INSATSU_END_Y_MIN, srDpprintData))); //終了時PTN間距離Y
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.SHURYOU_TENSION_KEI, srDpprintData))); //終了ﾃﾝｼｮﾝ計
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.SHURYOU_TENSION_MAE, srDpprintData))); //ﾃﾝｼｮﾝ終了手前
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.SHURYOU_TENSION_OKU, srDpprintData))); //ﾃﾝｼｮﾝ終了奥
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.GENRYO_KIGOU, srDpprintData))); //原料記号
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.BLADEINSATSUTYOU, srDpprintData))); // ﾌﾞﾚｰﾄﾞ印刷長
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.ZURERYOUKIJUNCHIX, srDpprintData))); // ｽﾞﾚ量基準値X
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.ZURERYOUKIJUNCHIY, srDpprintData))); // ｽﾞﾚ量基準値Y
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.AWASESEIDODAKOU, srDpprintData))); // 合わせ精度 蛇行
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.AWASESEIDONAGARE, srDpprintData))); // 合わせ精度 流れ
        params.add(DBUtil.stringToStringObjectDefaultNull(skKojyo)); // 先行工場ｺｰﾄﾞ
        params.add(DBUtil.stringToStringObjectDefaultNull(skLotNo)); // 先行ﾛｯﾄNo
        params.add(DBUtil.stringToStringObjectDefaultNull(skEdaban)); // 先行枝番
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B023Const.TAPETSUKAIKIRI, srDpprintData),null)); // ﾃｰﾌﾟ使い切り
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B023Const.JILOTHE, srDpprintData),null)); // 次ﾛｯﾄへ
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.SEIKEINAGASA, srDpprintData))); // 成形長さ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.BIKOU3, srDpprintData))); // 備考3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.BIKOU4, srDpprintData))); // 備考4
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.BIKOU5, srDpprintData))); // 備考5
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.BIKOU1, srDpprintData))); //備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.BIKOU2, srDpprintData))); //備考2
        if (isInsert) {
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時
        } else {
            params.add(systemTime); //更新日時
        }
        params.add(newRev); //revision
        params.add(deleteflag); //削除ﾌﾗｸﾞ
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.TAPE_ATSU, srDpprintData))); //ﾃｰﾌﾟ厚み
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.PRINTLENGTH, srDpprintData))); // 印刷長さ
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.KANSOU_ONDO_SHITA_HYOUJICHI1, srDpprintData))); //乾燥温度下側
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.KANSOU_ONDO_SHITA_HYOUJICHI2, srDpprintData))); //乾燥温度下側2
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.KANSOU_ONDO_SHITA_HYOUJICHI3, srDpprintData))); //乾燥温度下側3
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B023Const.KANSOU_ONDO_SHITA_HYOUJICHI4, srDpprintData))); //乾燥温度下側4
        return params;
    }

    /**
     * 印刷・DP2_ｻﾌﾞ画面仮登録(tmp_sub_sr_dpprint)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void insertTmpSubSrDpprint(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime) throws SQLException {
        String sql = "INSERT INTO tmp_sub_sr_dpprint ("
                + "kojyo,lotno,edaban,kaisuu,makuatsu_start1,makuatsu_start2,makuatsu_start3,makuatsu_start4,makuatsu_start5,makuatsu_start6,"
                + "makuatsu_start7,makuatsu_start8,makuatsu_start9,start_ptn_dist_x1,start_ptn_dist_x2,start_ptn_dist_x3,"
                + "start_ptn_dist_x4,start_ptn_dist_x5,start_ptn_dist_y1,start_ptn_dist_y2,start_ptn_dist_y3,start_ptn_dist_y4,"
                + "start_ptn_dist_y5,makuatsu_end1,makuatsu_end2,makuatsu_end3,makuatsu_end4,makuatsu_end5,makuatsu_end6,"
                + "makuatsu_end7,makuatsu_end8,makuatsu_end9,end_ptn_dist_x1,end_ptn_dist_x2,end_ptn_dist_x3,end_ptn_dist_x4,"
                + "end_ptn_dist_x5,end_ptn_dist_y1,end_ptn_dist_y2,end_ptn_dist_y3,end_ptn_dist_y4,end_ptn_dist_y5,torokunichiji,"
                + "kosinnichiji,revision,deleteflag"
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSubSrDpprint(true, newRev, deleteflag, kojyo, lotNo, edaban, jissekino, systemTime);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷・DP2_ｻﾌﾞ画面仮登録(tmp_sub_sr_dpprint)更新処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void updateTmpSubSrDpprint(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime) throws SQLException {
        String sql = "UPDATE tmp_sub_sr_dpprint SET "
                + "makuatsu_start1 = ?,makuatsu_start2 = ?,makuatsu_start3 = ?,makuatsu_start4 = ?,makuatsu_start5 = ?,"
                + "makuatsu_start6 = ?,makuatsu_start7 = ?,makuatsu_start8 = ?,makuatsu_start9 = ?,"
                + "start_ptn_dist_x1 = ?,start_ptn_dist_x2 = ?,start_ptn_dist_x3 = ?,start_ptn_dist_x4 = ?,start_ptn_dist_x5 = ?,"
                + "start_ptn_dist_y1 = ?,start_ptn_dist_y2 = ?,start_ptn_dist_y3 = ?,start_ptn_dist_y4 = ?,start_ptn_dist_y5 = ?,"
                + "makuatsu_end1 = ?,makuatsu_end2 = ?,makuatsu_end3 = ?,makuatsu_end4 = ?,makuatsu_end5 = ?,"
                + "makuatsu_end6 = ?,makuatsu_end7 = ?,makuatsu_end8 = ?,makuatsu_end9 = ?,"
                + "end_ptn_dist_x1 = ?,end_ptn_dist_x2 = ?,end_ptn_dist_x3 = ?,end_ptn_dist_x4 = ?,end_ptn_dist_x5 = ?,"
                + "end_ptn_dist_y1 = ?,end_ptn_dist_y2 = ?,end_ptn_dist_y3 = ?,end_ptn_dist_y4 = ?,end_ptn_dist_y5 = ?,"
                + "kosinnichiji = ?,revision = ?,deleteflag = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ?  AND kaisuu = ? AND revision = ?";

        List<Object> params = setUpdateParameterTmpSubSrDpprint(false, newRev, 0, "", "", "", jissekino, systemTime);

        // 検索条件
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷・DP2_ｻﾌﾞ画面仮登録(tmp_sub_sr_dpprint)削除処理
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
    private void deleteTmpSubSrDpprint(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {
        String sql = "DELETE FROM tmp_sub_sr_dpprint "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND revision = ?";

        // 検索条件
        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷・DP2_ｻﾌﾞ画面仮登録(tmp_sub_sr_dpprint)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSubSrDpprint(boolean isInsert, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime) {
        List<Object> params = new ArrayList<>();

        // 子画面情報を取得
        GXHDO101C001 beanGXHDO101C001 = (GXHDO101C001) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C001);
        List<GXHDO101C001Model.MakuatsuData> makuatsuDataList = beanGXHDO101C001.getGxhdO101c001Model().getMakuatsuDataList();
        GXHDO101C002 beanGXHDO101C002 = (GXHDO101C002) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C002);
        List<GXHDO101C002Model.PtnKyoriStartData> ptnKyoriStartDataList = beanGXHDO101C002.getGxhdO101c002Model().getPtnKyoriStartDataList();
        GXHDO101C003 beanGXHDO101C003 = (GXHDO101C003) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C003);
        List<GXHDO101C003Model.PtnKyoriEndData> ptnKyoriEndDataList = beanGXHDO101C003.getGxhdO101c003Model().getPtnKyoriEndDataList();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
            params.add(jissekino); //回数
        }
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(0).getStartVal())); //膜厚ｽﾀｰﾄ1
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(1).getStartVal())); //膜厚ｽﾀｰﾄ2
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(2).getStartVal())); //膜厚ｽﾀｰﾄ3
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(3).getStartVal())); //膜厚ｽﾀｰﾄ4
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(4).getStartVal())); //膜厚ｽﾀｰﾄ5
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(5).getStartVal())); //膜厚ｽﾀｰﾄ6
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(6).getStartVal())); //膜厚ｽﾀｰﾄ7
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(7).getStartVal())); //膜厚ｽﾀｰﾄ8
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(8).getStartVal())); //膜厚ｽﾀｰﾄ9
        params.add(DBUtil.stringToIntObjectDefaultNull(ptnKyoriStartDataList.get(0).getPtnKyoriXVal())); //PTN距離X ｽﾀｰﾄ1
        params.add(DBUtil.stringToIntObjectDefaultNull(ptnKyoriStartDataList.get(1).getPtnKyoriXVal())); //PTN距離X ｽﾀｰﾄ2
        params.add(DBUtil.stringToIntObjectDefaultNull(ptnKyoriStartDataList.get(2).getPtnKyoriXVal())); //PTN距離X ｽﾀｰﾄ3
        params.add(DBUtil.stringToIntObjectDefaultNull(ptnKyoriStartDataList.get(3).getPtnKyoriXVal())); //PTN距離X ｽﾀｰﾄ4
        params.add(DBUtil.stringToIntObjectDefaultNull(ptnKyoriStartDataList.get(4).getPtnKyoriXVal())); //PTN距離X ｽﾀｰﾄ5
        params.add(DBUtil.stringToIntObjectDefaultNull(ptnKyoriStartDataList.get(0).getPtnKyoriYVal())); //PTN距離Y ｽﾀｰﾄ1
        params.add(DBUtil.stringToIntObjectDefaultNull(ptnKyoriStartDataList.get(1).getPtnKyoriYVal())); //PTN距離Y ｽﾀｰﾄ2
        params.add(DBUtil.stringToIntObjectDefaultNull(ptnKyoriStartDataList.get(2).getPtnKyoriYVal())); //PTN距離Y ｽﾀｰﾄ3
        params.add(DBUtil.stringToIntObjectDefaultNull(ptnKyoriStartDataList.get(3).getPtnKyoriYVal())); //PTN距離Y ｽﾀｰﾄ4
        params.add(DBUtil.stringToIntObjectDefaultNull(ptnKyoriStartDataList.get(4).getPtnKyoriYVal())); //PTN距離Y ｽﾀｰﾄ5
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(0).getEndVal())); //膜厚ｴﾝﾄﾞ1
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(1).getEndVal())); //膜厚ｴﾝﾄﾞ2
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(2).getEndVal())); //膜厚ｴﾝﾄﾞ3
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(3).getEndVal())); //膜厚ｴﾝﾄﾞ4
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(4).getEndVal())); //膜厚ｴﾝﾄﾞ5
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(5).getEndVal())); //膜厚ｴﾝﾄﾞ6
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(6).getEndVal())); //膜厚ｴﾝﾄﾞ7
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(7).getEndVal())); //膜厚ｴﾝﾄﾞ8
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(8).getEndVal())); //膜厚ｴﾝﾄﾞ9
        params.add(DBUtil.stringToIntObjectDefaultNull(ptnKyoriEndDataList.get(0).getPtnKyoriXVal())); //PTN距離X ｴﾝﾄﾞ1
        params.add(DBUtil.stringToIntObjectDefaultNull(ptnKyoriEndDataList.get(1).getPtnKyoriXVal())); //PTN距離X ｴﾝﾄﾞ2
        params.add(DBUtil.stringToIntObjectDefaultNull(ptnKyoriEndDataList.get(2).getPtnKyoriXVal())); //PTN距離X ｴﾝﾄﾞ3
        params.add(DBUtil.stringToIntObjectDefaultNull(ptnKyoriEndDataList.get(3).getPtnKyoriXVal())); //PTN距離X ｴﾝﾄﾞ4
        params.add(DBUtil.stringToIntObjectDefaultNull(ptnKyoriEndDataList.get(4).getPtnKyoriXVal())); //PTN距離X ｴﾝﾄﾞ5
        params.add(DBUtil.stringToIntObjectDefaultNull(ptnKyoriEndDataList.get(0).getPtnKyoriYVal())); //PTN距離Y ｴﾝﾄﾞ1
        params.add(DBUtil.stringToIntObjectDefaultNull(ptnKyoriEndDataList.get(1).getPtnKyoriYVal())); //PTN距離Y ｴﾝﾄﾞ2
        params.add(DBUtil.stringToIntObjectDefaultNull(ptnKyoriEndDataList.get(2).getPtnKyoriYVal())); //PTN距離Y ｴﾝﾄﾞ3
        params.add(DBUtil.stringToIntObjectDefaultNull(ptnKyoriEndDataList.get(3).getPtnKyoriYVal())); //PTN距離Y ｴﾝﾄﾞ4
        params.add(DBUtil.stringToIntObjectDefaultNull(ptnKyoriEndDataList.get(4).getPtnKyoriYVal())); //PTN距離Y ｴﾝﾄﾞ5
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
     * 印刷・DP2(sr_dpprint)登録処理
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
     * @param tmpSrDpprint 仮登録データ
     * @param hiddenDataMap 隠しデータMAP
     * @throws SQLException 例外エラー
     */
    private void insertSrDpprint(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, SrDpprint tmpSrDpprint,
            Map<String, Object> hiddenDataMap) throws SQLException {

        String sql = "INSERT INTO sr_dpprint ("
                + "kojyo,lotno,edaban,kaisuu,kcpno,kouteikubun,tapelotno,petfilmsyurui,taperollno1,taperollno2,taperollno3,pastelotno,pastenendo,pasteondo,pkokeibun1,"
                + "pastelotno2,handoumei,handouno,handoumaisuu,bladeno,bladegaikan,BladeATu,AtudoNo,AtudoMaisuu,AtuDoATu,gouki,kansouondo,kansouondo2,kansouondo3,kansouondo4,"
                + "kansouondo5,hansouspeed,startdatetime,tantousya,kakuninsya,makuatuave_start,makuatumax_start,makuatumin_start,makuatucv_start,nijimikasure_start,"
                + "start_ptn_dist_x,start_ptn_dist_y,TensionS_sum,TensionStemae,TensionSoku,enddatetime,tanto_end,printmaisuu,makuatuave_end,makuatumax_end,makuatumin_end,"
                + "makuatucv_end,nijimikasure_end,end_ptn_dist_x,end_ptn_dist_y,TensionE_sum,TensionEtemae,TensionEoku,genryoukigou,"
                + "bladeinsatsutyou,zureryoukijunchix,zureryoukijunchiy,awaseseidodakou,awaseseidonagare,skojyo,slotno,sedaban,tapetsukaikiri,jilothe,seikeinagasa,bikou3,bikou4,bikou5,"
                + "bikou1,bikou2,torokunichiji,kosinnichiji,"
                + "revision,tapeatu,printlength,kansouondoshita,kansouondoshita2,kansouondoshita3,kansouondoshita4"
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrDpprint(true, newRev, kojyo, lotNo, edaban, jissekino, systemTime, itemList, tmpSrDpprint, hiddenDataMap);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷・DP2(sr_dpprint)更新処理
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
     * @param hiddenDataMap 隠しデータMAP
     * @throws SQLException 例外エラー
     */
    private void updateSrDpprint(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, Map<String, Object> hiddenDataMap) throws SQLException {
        String sql = "UPDATE sr_dpprint SET "
                + "kcpno = ?,kouteikubun = ?,tapelotno = ?,petfilmsyurui = ?,taperollno1 = ?,taperollno2 = ?,taperollno3 = ?,pastelotno = ?,pastenendo = ?,pasteondo = ?,"
                + "pkokeibun1 = ?,pastelotno2 = ?,handoumei = ?,handouno = ?,handoumaisuu = ?,bladeno = ?,bladegaikan = ?,BladeATu = ?,AtudoNo = ?,AtudoMaisuu = ?,AtuDoATu = ?,"
                + "gouki = ?,kansouondo = ?,kansouondo2 = ?,kansouondo3 = ?,kansouondo4 = ?,kansouondo5 = ?,hansouspeed = ?,startdatetime = ?,tantousya = ?,kakuninsya = ?,makuatuave_start = ?,"
                + "makuatumax_start = ?,makuatumin_start = ?,makuatucv_start = ?,nijimikasure_start = ?,start_ptn_dist_x = ?,start_ptn_dist_y = ?,TensionS_sum = ?,TensionStemae = ?,TensionSoku = ?,"
                + "enddatetime = ?,tanto_end = ?,printmaisuu = ?,makuatuave_end = ?,makuatumax_end = ?,makuatumin_end = ?,makuatucv_end = ?,nijimikasure_end = ?,end_ptn_dist_x = ?,"
                + "end_ptn_dist_y = ?,TensionE_sum = ?,TensionEtemae = ?,TensionEoku = ?,genryoukigou = ?,"
                + "bladeinsatsutyou = ?,zureryoukijunchix = ?,zureryoukijunchiy = ?,awaseseidodakou = ?,awaseseidonagare = ?,skojyo = ?,slotno = ?,sedaban = ?,tapetsukaikiri = ?,jilothe = ?,seikeinagasa = ?,"
                + "bikou3 = ?,bikou4 = ?,bikou5 = ?,bikou1 = ?,bikou2 = ?,kosinnichiji = ?,revision = ?,tapeatu = ?,printlength = ?,kansouondoshita = ?,kansouondoshita2 = ?,kansouondoshita3 = ?,kansouondoshita4 = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND revision = ?";

        // 更新前の値を取得
        List<SrDpprint> srDpprintList = getSrDpprintData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekino);
        SrDpprint srDpprint = null;
        if (!srDpprintList.isEmpty()) {
            srDpprint = srDpprintList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrDpprint(false, newRev, "", "", "", jissekino, systemTime, itemList, srDpprint, hiddenDataMap);

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
     * 印刷・DP2(sr_dpprint)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srDpprintData 印刷・DP2データ
     * @param hiddenDataMap 隠しデータMAP
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrDpprint(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            int jissekino, Timestamp systemTime, List<FXHDD01> itemList, SrDpprint srDpprintData, Map<String, Object> hiddenDataMap) {

        // 工程区分の取得
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String koteiKbn = StringUtil.nullToBlank(session.getAttribute("koteikbn"));

        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
            params.add(jissekino); //回数
        }
        
        // 先行ﾛｯﾄNo
        String senkoulotno = getItemData(itemList, GXHDO101B023Const.SENKOULOTNO, srDpprintData);
        String skKojyo = ""; //先行工場ｺｰﾄﾞ
        String skLotNo = ""; // 先行ﾛｯﾄNo
        String skEdaban = ""; // 先行枝番
        if (!StringUtil.isEmpty(senkoulotno)) {
            skKojyo = StringUtils.substring(senkoulotno, 0, 3);
            skLotNo = StringUtils.substring(senkoulotno, 3, 11);
            skEdaban = StringUtil.blankToNull(StringUtils.substring(senkoulotno, 11, 14));
        }

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B023Const.KCPNO, srDpprintData))); //KCPNO
        params.add(DBUtil.stringToStringObject(StringUtil.nullToBlank(getMapData(hiddenDataMap, "koteiKbn")))); // 工程区分
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B023Const.SLIP_LOTNO, srDpprintData))); //ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B023Const.PET_FILM_SHURUI, srDpprintData))); //PETﾌｨﾙﾑ種類
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B023Const.ROLL_NO1, srDpprintData))); //ﾃｰﾌﾟﾛｰﾙNo1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B023Const.ROLL_NO2, srDpprintData))); //ﾃｰﾌﾟﾛｰﾙNo2
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B023Const.ROLL_NO3, srDpprintData))); //ﾃｰﾌﾟﾛｰﾙNo3
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B023Const.PASTE_LOT_NO1, srDpprintData))); //ﾍﾟｰｽﾄﾛｯﾄNo1
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B023Const.PASTE_NENDO1, srDpprintData))); //ﾍﾟｰｽﾄ粘度1
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B023Const.PASTE_ONDO1, srDpprintData))); //ﾍﾟｰｽﾄ温度1
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B023Const.PASTE_KOKEIBUN1, srDpprintData))); //ﾍﾟｰｽﾄ固形分1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B023Const.PASTE_LOT_NO2, srDpprintData))); //ﾍﾟｰｽﾄﾛｯﾄNo2
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B023Const.HANDOU_MEI, srDpprintData))); //版胴名
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B023Const.HANDOU_LOTNO, srDpprintData))); //版胴ﾛｯﾄNo
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B023Const.HANDOU_SHIYOU_MAISUU, srDpprintData))); //版胴使用枚数 
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B023Const.BLADE_NO, srDpprintData))); //ﾌﾞﾚｰﾄﾞNo.
        //ﾌﾞﾚｰﾄﾞ外観
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B023Const.BLADE_GAIKAN, srDpprintData))) {
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
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B023Const.BLADE_ATSURYOKU, srDpprintData))); //ﾌﾞﾚｰﾄﾞ圧力
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B023Const.ATSUDOU_LOTNO, srDpprintData))); //圧胴No
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B023Const.ATSUDOU_SIYOU_MAISUU, srDpprintData))); //圧胴使用枚数
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B023Const.ATSUDOU_ATSURYOKU, srDpprintData))); //圧胴圧力

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B023Const.INSATSU_GOUKI, srDpprintData))); //号機ｺｰﾄﾞ
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI1, srDpprintData))); //乾燥温度
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI2, srDpprintData))); //乾燥温度2
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI3, srDpprintData))); //乾燥温度3
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI4, srDpprintData))); //乾燥温度4
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI5, srDpprintData))); //乾燥温度5
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B023Const.HANSOU_SOKUDO, srDpprintData))); //搬送速度
        params.add(DBUtil.stringToDateObject(
                getItemData(itemList, GXHDO101B023Const.INSATSU_KAISHI_DAY, srDpprintData),
                getItemData(itemList, GXHDO101B023Const.INSATSU_KAISHI_TIME, srDpprintData))); //ﾌﾟﾘﾝﾄ開始日時
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B023Const.INSATSU_STARTJI_TANTOUSHA, srDpprintData))); //ｾｯﾃｨﾝｸﾞ担当者ｺｰﾄﾞ
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B023Const.INSATSU_STARTJI_KAKUNINSHA, srDpprintData))); //印刷ｽﾀｰﾄ確認者
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B023Const.INSATSU_START_MAKUATSU_AVE, srDpprintData))); //ｽﾀｰﾄ時膜厚AVE
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B023Const.INSATSU_START_MAKUATSU_MAX, srDpprintData))); //ｽﾀｰﾄ時膜厚MAX
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B023Const.INSATSU_START_MAKUATSU_MIN, srDpprintData))); //ｽﾀｰﾄ時膜厚MIN
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B023Const.INSATSU_START_MAKUATSU_CV, srDpprintData))); //印刷ｽﾀｰﾄ膜厚CV
        //ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B023Const.STARTJI_NIJIMI_KASURE_CHECK, srDpprintData))) {
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
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B023Const.PTN_INSATSU_START_X_MIN, srDpprintData))); //ｽﾀｰﾄ時PTN間距離X
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B023Const.PTN_INSATSU_START_Y_MIN, srDpprintData))); //ｽﾀｰﾄ時PTN間距離Y
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B023Const.KAISHI_TENSION_KEI, srDpprintData))); //開始ﾃﾝｼｮﾝ計
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B023Const.KAISHI_TENSION_MAE, srDpprintData))); //ﾃﾝｼｮﾝ開始手前
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B023Const.KAISHI_TENSION_OKU, srDpprintData))); //ﾃﾝｼｮﾝ開始奥
        params.add(DBUtil.stringToDateObject(
                getItemData(itemList, GXHDO101B023Const.INSATSU_SHURYOU_DAY, srDpprintData),
                getItemData(itemList, GXHDO101B023Const.INSATSU_SHURYOU_TIME, srDpprintData))); //ﾌﾟﾘﾝﾄ終了日時
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B023Const.INSATSU_ENDJI_TANTOUSHA, srDpprintData))); //終了時担当者ｺｰﾄﾞ
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B023Const.INSATSU_MAISUU, srDpprintData))); //印刷枚数
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B023Const.INSATSU_END_MAKUATSU_AVE, srDpprintData))); //終了時膜厚AVE
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B023Const.INSATSU_END_MAKUATSU_MAX, srDpprintData))); //終了時膜厚MAX
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B023Const.INSATSU_END_MAKUATSU_MIN, srDpprintData))); //終了時膜厚MIN
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B023Const.INSATSU_END_MAKUATSU_CV, srDpprintData))); //印刷ｴﾝﾄﾞ膜厚CV
        //終了時ﾆｼﾞﾐ・ｶｽﾚ確認
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B023Const.SHURYOU_JI_NIJIMI_KASURE_CHECK, srDpprintData))) {
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
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B023Const.PTN_INSATSU_END_X_MIN, srDpprintData))); //終了時PTN間距離X
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B023Const.PTN_INSATSU_END_Y_MIN, srDpprintData))); //終了時PTN間距離Y
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B023Const.SHURYOU_TENSION_KEI, srDpprintData))); //終了ﾃﾝｼｮﾝ計
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B023Const.SHURYOU_TENSION_MAE, srDpprintData))); //ﾃﾝｼｮﾝ終了手前
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B023Const.SHURYOU_TENSION_OKU, srDpprintData))); //ﾃﾝｼｮﾝ終了奥
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B023Const.GENRYO_KIGOU, srDpprintData))); //原料記号
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B023Const.BLADEINSATSUTYOU, srDpprintData))); // ﾌﾞﾚｰﾄﾞ印刷長
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B023Const.ZURERYOUKIJUNCHIX, srDpprintData))); // ｽﾞﾚ量基準値X
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B023Const.ZURERYOUKIJUNCHIY, srDpprintData))); // ｽﾞﾚ量基準値Y
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B023Const.AWASESEIDODAKOU, srDpprintData))); // 合わせ精度 蛇行
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B023Const.AWASESEIDONAGARE, srDpprintData))); // 合わせ精度 流れ
        params.add(DBUtil.stringToStringObject(skKojyo)); // 先行工場ｺｰﾄﾞ
        params.add(DBUtil.stringToStringObject(skLotNo)); // 先行ﾛｯﾄNo
        params.add(DBUtil.stringToStringObject(skEdaban)); // 先行枝番
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B023Const.TAPETSUKAIKIRI, srDpprintData),9)); // ﾃｰﾌﾟ使い切り
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B023Const.JILOTHE, srDpprintData),9)); // 次ﾛｯﾄへ
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B023Const.SEIKEINAGASA, srDpprintData))); // 成形長さ
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B023Const.BIKOU3, srDpprintData))); // 備考3
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B023Const.BIKOU4, srDpprintData))); // 備考4
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B023Const.BIKOU5, srDpprintData))); // 備考5        
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B023Const.BIKOU1, srDpprintData))); //備考1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B023Const.BIKOU2, srDpprintData))); //備考2
        if (isInsert) {
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時
        } else {
            params.add(systemTime); //更新日時
        }
        params.add(newRev); //revision
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B023Const.TAPE_ATSU, srDpprintData))); //ﾃｰﾌﾟ厚み
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B023Const.PRINTLENGTH, srDpprintData))); // 印刷長さ
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B023Const.KANSOU_ONDO_SHITA_HYOUJICHI1, srDpprintData))); //乾燥温度下側
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B023Const.KANSOU_ONDO_SHITA_HYOUJICHI2, srDpprintData))); //乾燥温度下側2
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B023Const.KANSOU_ONDO_SHITA_HYOUJICHI3, srDpprintData))); //乾燥温度下側3
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B023Const.KANSOU_ONDO_SHITA_HYOUJICHI4, srDpprintData))); //乾燥温度下側4

        return params;
    }

    /**
     * 印刷・DP2(sr_dpprint)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSrDpprint(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        String sql = "DELETE FROM sr_dpprint "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND revision = ?";

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
     * 印刷・DP2_ｻﾌﾞ画面(sub_sr_dpprint)登録処理
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
    private void insertSubSrDpprint(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime) throws SQLException {
        String sql = "INSERT INTO sub_sr_dpprint ("
                + "kojyo,lotno,edaban,kaisuu,makuatsu_start1,makuatsu_start2,makuatsu_start3,makuatsu_start4,makuatsu_start5,makuatsu_start6,"
                + "makuatsu_start7,makuatsu_start8,makuatsu_start9,start_ptn_dist_x1,start_ptn_dist_x2,start_ptn_dist_x3,"
                + "start_ptn_dist_x4,start_ptn_dist_x5,start_ptn_dist_y1,start_ptn_dist_y2,start_ptn_dist_y3,start_ptn_dist_y4,"
                + "start_ptn_dist_y5,makuatsu_end1,makuatsu_end2,makuatsu_end3,makuatsu_end4,makuatsu_end5,makuatsu_end6,"
                + "makuatsu_end7,makuatsu_end8,makuatsu_end9,end_ptn_dist_x1,end_ptn_dist_x2,end_ptn_dist_x3,end_ptn_dist_x4,"
                + "end_ptn_dist_x5,end_ptn_dist_y1,end_ptn_dist_y2,end_ptn_dist_y3,end_ptn_dist_y4,end_ptn_dist_y5,torokunichiji,"
                + "kosinnichiji,revision"
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSubSrDpprint(true, newRev, kojyo, lotNo, edaban, jissekino, systemTime);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷・DP2_ｻﾌﾞ画面(sub_sr_dpprint)更新処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void updateSubSrDpprint(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime) throws SQLException {
        String sql = "UPDATE sub_sr_dpprint SET "
                + "makuatsu_start1 = ?,makuatsu_start2 = ?,makuatsu_start3 = ?,makuatsu_start4 = ?,makuatsu_start5 = ?,"
                + "makuatsu_start6 = ?,makuatsu_start7 = ?,makuatsu_start8 = ?,makuatsu_start9 = ?,"
                + "start_ptn_dist_x1 = ?,start_ptn_dist_x2 = ?,start_ptn_dist_x3 = ?,start_ptn_dist_x4 = ?,start_ptn_dist_x5 = ?,"
                + "start_ptn_dist_y1 = ?,start_ptn_dist_y2 = ?,start_ptn_dist_y3 = ?,start_ptn_dist_y4 = ?,start_ptn_dist_y5 = ?,"
                + "makuatsu_end1 = ?,makuatsu_end2 = ?,makuatsu_end3 = ?,makuatsu_end4 = ?,makuatsu_end5 = ?,"
                + "makuatsu_end6 = ?,makuatsu_end7 = ?,makuatsu_end8 = ?,makuatsu_end9 = ?,"
                + "end_ptn_dist_x1 = ?,end_ptn_dist_x2 = ?,end_ptn_dist_x3 = ?,end_ptn_dist_x4 = ?,end_ptn_dist_x5 = ?,"
                + "end_ptn_dist_y1 = ?,end_ptn_dist_y2 = ?,end_ptn_dist_y3 = ?,end_ptn_dist_y4 = ?,end_ptn_dist_y5 = ?,"
                + "kosinnichiji = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND revision = ?";

        List<Object> params = setUpdateParameterSubSrDpprint(false, newRev, "", "", "", jissekino, systemTime);

        // 検索条件
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷・DP2_ｻﾌﾞ画面登録(tmp_sub_sr_dpprint)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSubSrDpprint(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo,
            String edaban, int jissekino, Timestamp systemTime) {
        List<Object> params = new ArrayList<>();

        // 子画面情報を取得
        GXHDO101C001 beanGXHDO101C001 = (GXHDO101C001) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C001);
        List<GXHDO101C001Model.MakuatsuData> makuatsuDataList = beanGXHDO101C001.getGxhdO101c001Model().getMakuatsuDataList();
        GXHDO101C002 beanGXHDO101C002 = (GXHDO101C002) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C002);
        List<GXHDO101C002Model.PtnKyoriStartData> ptnKyoriStartDataList = beanGXHDO101C002.getGxhdO101c002Model().getPtnKyoriStartDataList();

        GXHDO101C003 beanGXHDO101C003 = (GXHDO101C003) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C003);
        List<GXHDO101C003Model.PtnKyoriEndData> ptnKyoriEndDataList = beanGXHDO101C003.getGxhdO101c003Model().getPtnKyoriEndDataList();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
            params.add(jissekino); //回数
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
        params.add(DBUtil.stringToIntObject(ptnKyoriStartDataList.get(0).getPtnKyoriXVal())); //PTN距離X ｽﾀｰﾄ1
        params.add(DBUtil.stringToIntObject(ptnKyoriStartDataList.get(1).getPtnKyoriXVal())); //PTN距離X ｽﾀｰﾄ2
        params.add(DBUtil.stringToIntObject(ptnKyoriStartDataList.get(2).getPtnKyoriXVal())); //PTN距離X ｽﾀｰﾄ3
        params.add(DBUtil.stringToIntObject(ptnKyoriStartDataList.get(3).getPtnKyoriXVal())); //PTN距離X ｽﾀｰﾄ4
        params.add(DBUtil.stringToIntObject(ptnKyoriStartDataList.get(4).getPtnKyoriXVal())); //PTN距離X ｽﾀｰﾄ5
        params.add(DBUtil.stringToIntObject(ptnKyoriStartDataList.get(0).getPtnKyoriYVal())); //PTN距離Y ｽﾀｰﾄ1
        params.add(DBUtil.stringToIntObject(ptnKyoriStartDataList.get(1).getPtnKyoriYVal())); //PTN距離Y ｽﾀｰﾄ2
        params.add(DBUtil.stringToIntObject(ptnKyoriStartDataList.get(2).getPtnKyoriYVal())); //PTN距離Y ｽﾀｰﾄ3
        params.add(DBUtil.stringToIntObject(ptnKyoriStartDataList.get(3).getPtnKyoriYVal())); //PTN距離Y ｽﾀｰﾄ4
        params.add(DBUtil.stringToIntObject(ptnKyoriStartDataList.get(4).getPtnKyoriYVal())); //PTN距離Y ｽﾀｰﾄ5
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(0).getEndVal())); //膜厚ｴﾝﾄﾞ1
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(1).getEndVal())); //膜厚ｴﾝﾄﾞ2
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(2).getEndVal())); //膜厚ｴﾝﾄﾞ3
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(3).getEndVal())); //膜厚ｴﾝﾄﾞ4
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(4).getEndVal())); //膜厚ｴﾝﾄﾞ5
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(5).getEndVal())); //膜厚ｴﾝﾄﾞ6
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(6).getEndVal())); //膜厚ｴﾝﾄﾞ7
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(7).getEndVal())); //膜厚ｴﾝﾄﾞ8
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(8).getEndVal())); //膜厚ｴﾝﾄﾞ9
        params.add(DBUtil.stringToIntObject(ptnKyoriEndDataList.get(0).getPtnKyoriXVal())); //PTN距離X ｴﾝﾄﾞ1
        params.add(DBUtil.stringToIntObject(ptnKyoriEndDataList.get(1).getPtnKyoriXVal())); //PTN距離X ｴﾝﾄﾞ2
        params.add(DBUtil.stringToIntObject(ptnKyoriEndDataList.get(2).getPtnKyoriXVal())); //PTN距離X ｴﾝﾄﾞ3
        params.add(DBUtil.stringToIntObject(ptnKyoriEndDataList.get(3).getPtnKyoriXVal())); //PTN距離X ｴﾝﾄﾞ4
        params.add(DBUtil.stringToIntObject(ptnKyoriEndDataList.get(4).getPtnKyoriXVal())); //PTN距離X ｴﾝﾄﾞ5
        params.add(DBUtil.stringToIntObject(ptnKyoriEndDataList.get(0).getPtnKyoriYVal())); //PTN距離Y ｴﾝﾄﾞ1
        params.add(DBUtil.stringToIntObject(ptnKyoriEndDataList.get(1).getPtnKyoriYVal())); //PTN距離Y ｴﾝﾄﾞ2
        params.add(DBUtil.stringToIntObject(ptnKyoriEndDataList.get(2).getPtnKyoriYVal())); //PTN距離Y ｴﾝﾄﾞ3
        params.add(DBUtil.stringToIntObject(ptnKyoriEndDataList.get(3).getPtnKyoriYVal())); //PTN距離Y ｴﾝﾄﾞ4
        params.add(DBUtil.stringToIntObject(ptnKyoriEndDataList.get(4).getPtnKyoriYVal())); //PTN距離Y ｴﾝﾄﾞ5
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
     * 印刷・DP2_ｻﾌﾞ画面仮登録(tmp_sub_sr_dpprint)削除処理
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
    private void deleteSubSrDpprint(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {
        String sql = "DELETE FROM sub_sr_dpprint "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND revision = ?";

        // 検索条件
        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * [印刷・DP2_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @return 削除ﾌﾗｸﾞ最大値 + 1
     * @throws SQLException 例外エラー
     */
    private int getNewDeleteflag(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {
        String sql = "SELECT MAX(deleteflag) AS deleteflag "
                + "FROM tmp_sr_dpprint "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? ";
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
    public ProcessData setKaishiDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B023Const.INSATSU_KAISHI_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B023Const.INSATSU_KAISHI_TIME);
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
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B023Const.INSATSU_SHURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B023Const.INSATSU_SHURYOU_TIME);
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
        itemDay.setValue(new SimpleDateFormat("yyMMdd").format(setDateTime));
        itemTime.setValue(new SimpleDateFormat("HHmm").format(setDateTime));
    }

    /**
     * 項目IDに該当するDBの値を取得する。
     *
     * @param itemId 項目ID
     * @param srDpprintData 印刷グラビアデータ
     * @return DB値
     */
    private String getSrDpprintItemData(String itemId, SrDpprint srDpprintData) {
        switch (itemId) {
            // KCPNO
            case GXHDO101B023Const.KCPNO:
                return StringUtil.nullToBlank(srDpprintData.getKcpno());
            // ｽﾘｯﾌﾟﾛｯﾄNo
            case GXHDO101B023Const.SLIP_LOTNO:
                return StringUtil.nullToBlank(srDpprintData.getTapelotno());
            // ﾛｰﾙNo1
            case GXHDO101B023Const.ROLL_NO1:
                return StringUtil.nullToBlank(srDpprintData.getTaperollno1());
            // ﾛｰﾙNo2
            case GXHDO101B023Const.ROLL_NO2:
                return StringUtil.nullToBlank(srDpprintData.getTaperollno2());
            // ﾛｰﾙNo3
            case GXHDO101B023Const.ROLL_NO3:
                return StringUtil.nullToBlank(srDpprintData.getTaperollno3());
            // 原料記号
            case GXHDO101B023Const.GENRYO_KIGOU:
                return StringUtil.nullToBlank(srDpprintData.getGenryoukigou());
            // ﾃｰﾌﾟ厚み
            case GXHDO101B023Const.TAPE_ATSU:
                return StringUtil.nullToBlank(srDpprintData.getTapeatu());
            // 内部電極ﾍﾟｰｽﾄﾛｯﾄNo1
            case GXHDO101B023Const.PASTE_LOT_NO1:
                return StringUtil.nullToBlank(srDpprintData.getPastelotno());
            // 内部電極ﾍﾟｰｽﾄ粘度1
            case GXHDO101B023Const.PASTE_NENDO1:
                return StringUtil.nullToBlank(srDpprintData.getPastenendo());
            // 内部電極ﾍﾟｰｽﾄ温度1
            case GXHDO101B023Const.PASTE_ONDO1:
                return StringUtil.nullToBlank(srDpprintData.getPasteondo());
            // 内部電極ﾍﾟｰｽﾄ固形分1
            case GXHDO101B023Const.PASTE_KOKEIBUN1:
                return StringUtil.nullToBlank(srDpprintData.getPkokeibun1());
            // 内部電極ﾍﾟｰｽﾄﾛｯﾄNo2
            case GXHDO101B023Const.PASTE_LOT_NO2:
                return StringUtil.nullToBlank(srDpprintData.getPastelotno2());
            // PETﾌｨﾙﾑ種類
            case GXHDO101B023Const.PET_FILM_SHURUI:
                return StringUtil.nullToBlank(srDpprintData.getPetfilmsyurui());
            // 印刷号機
            case GXHDO101B023Const.INSATSU_GOUKI:
                return StringUtil.nullToBlank(srDpprintData.getGouki());
            // 乾燥温度表示値1
            case GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI1:
                return StringUtil.nullToBlank(srDpprintData.getKansouondo());
            // 乾燥温度表示値2
            case GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI2:
                return StringUtil.nullToBlank(srDpprintData.getKansouondo2());
            // 乾燥温度表示値3
            case GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI3:
                return StringUtil.nullToBlank(srDpprintData.getKansouondo3());
            // 乾燥温度表示値4
            case GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI4:
                return StringUtil.nullToBlank(srDpprintData.getKansouondo4());
            // 乾燥温度下側表示値1
            case GXHDO101B023Const.KANSOU_ONDO_SHITA_HYOUJICHI1:
                return StringUtil.nullToBlank(srDpprintData.getKansouondoshita());
            // 乾燥温度下側表示値2
            case GXHDO101B023Const.KANSOU_ONDO_SHITA_HYOUJICHI2:
                return StringUtil.nullToBlank(srDpprintData.getKansouondoshita2());
            // 乾燥温度下側表示値3
            case GXHDO101B023Const.KANSOU_ONDO_SHITA_HYOUJICHI3:
                return StringUtil.nullToBlank(srDpprintData.getKansouondoshita3());
            // 乾燥温度下側表示値4
            case GXHDO101B023Const.KANSOU_ONDO_SHITA_HYOUJICHI4:
                return StringUtil.nullToBlank(srDpprintData.getKansouondoshita4());
            // 乾燥温度表示値5
            case GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI5:
                return StringUtil.nullToBlank(srDpprintData.getKansouondo5());
            // 搬送速度
            case GXHDO101B023Const.HANSOU_SOKUDO:
                return StringUtil.nullToBlank(srDpprintData.getHansouspeed());
            // 圧胴圧力
            case GXHDO101B023Const.ATSUDOU_ATSURYOKU:
                return StringUtil.nullToBlank(srDpprintData.getAtudoatu());
            // ﾌﾞﾚｰﾄﾞ圧力
            case GXHDO101B023Const.BLADE_ATSURYOKU:
                return StringUtil.nullToBlank(srDpprintData.getBladeatu());
            // 版胴名
            case GXHDO101B023Const.HANDOU_MEI:
                return StringUtil.nullToBlank(srDpprintData.getHandoumei());
            // 版胴ﾛｯﾄNo
            case GXHDO101B023Const.HANDOU_LOTNO:
                return StringUtil.nullToBlank(srDpprintData.getHandouno());
            // 版胴使用枚数
            case GXHDO101B023Const.HANDOU_SHIYOU_MAISUU:
                return StringUtil.nullToBlank(srDpprintData.getHandoumaisuu());
            // 圧胴ﾛｯﾄNo
            case GXHDO101B023Const.ATSUDOU_LOTNO:
                return StringUtil.nullToBlank(srDpprintData.getAtudono());
            // 圧胴使用枚数
            case GXHDO101B023Const.ATSUDOU_SIYOU_MAISUU:
                return StringUtil.nullToBlank(srDpprintData.getAtudomaisuu());
            // ﾌﾞﾚｰﾄﾞNo.
            case GXHDO101B023Const.BLADE_NO:
                return StringUtil.nullToBlank(srDpprintData.getBladeno());
            // ﾌﾞﾚｰﾄﾞ外観
            case GXHDO101B023Const.BLADE_GAIKAN:
                switch (StringUtil.nullToBlank(srDpprintData.getBladegaikan())) {
                    case "0":
                        return "NG";
                    case "1":
                        return "OK";
                    default:
                        return "";
                }
            // 印刷開始日
            case GXHDO101B023Const.INSATSU_KAISHI_DAY:
                return DateUtil.formattedTimestamp(srDpprintData.getStartdatetime(), "yyMMdd");
            // 印刷開始時間
            case GXHDO101B023Const.INSATSU_KAISHI_TIME:
                return DateUtil.formattedTimestamp(srDpprintData.getStartdatetime(), "HHmm");
            // 印刷ｽﾀｰﾄ膜厚AVE
            case GXHDO101B023Const.INSATSU_START_MAKUATSU_AVE:
                return StringUtil.nullToBlank(srDpprintData.getMakuatuaveStart());
            // 印刷ｽﾀｰﾄ膜厚MAX
            case GXHDO101B023Const.INSATSU_START_MAKUATSU_MAX:
                return StringUtil.nullToBlank(srDpprintData.getMakuatumaxStart());
            // 印刷ｽﾀｰﾄ膜厚MIN
            case GXHDO101B023Const.INSATSU_START_MAKUATSU_MIN:
                return StringUtil.nullToBlank(srDpprintData.getMakuatuminStart());
            // 印刷ｽﾀｰﾄ膜厚CV
            case GXHDO101B023Const.INSATSU_START_MAKUATSU_CV:
                return StringUtil.nullToBlank(srDpprintData.getMakuatucvStart());
            // PTN間距離印刷ｽﾀｰﾄ X Min
            case GXHDO101B023Const.PTN_INSATSU_START_X_MIN:
                return StringUtil.nullToBlank(srDpprintData.getStartPtnDistX());
            // PTN間距離印刷ｽﾀｰﾄ Y Min
            case GXHDO101B023Const.PTN_INSATSU_START_Y_MIN:
                return StringUtil.nullToBlank(srDpprintData.getStartPtnDistY());
            // ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
            case GXHDO101B023Const.STARTJI_NIJIMI_KASURE_CHECK:
                switch (StringUtil.nullToBlank(srDpprintData.getNijimikasureStart())) {
                    case "0":
                        return "NG";
                    case "1":
                        return "OK";
                    default:
                        return "";
                }
            // 印刷ｽﾀｰﾄ時担当者
            case GXHDO101B023Const.INSATSU_STARTJI_TANTOUSHA:
                return StringUtil.nullToBlank(srDpprintData.getTantousya());
            // 印刷ｽﾀｰﾄ時確認者
            case GXHDO101B023Const.INSATSU_STARTJI_KAKUNINSHA:
                return StringUtil.nullToBlank(srDpprintData.getKakuninsya());
            // 印刷終了日
            case GXHDO101B023Const.INSATSU_SHURYOU_DAY:
                return DateUtil.formattedTimestamp(srDpprintData.getEnddatetime(), "yyMMdd");
            // 印刷終了時刻
            case GXHDO101B023Const.INSATSU_SHURYOU_TIME:
                return DateUtil.formattedTimestamp(srDpprintData.getEnddatetime(), "HHmm");
            // 印刷ｴﾝﾄﾞ膜厚AVE
            case GXHDO101B023Const.INSATSU_END_MAKUATSU_AVE:
                return StringUtil.nullToBlank(srDpprintData.getMakuatuaveEnd());
            // 印刷ｴﾝﾄﾞ膜厚MAX
            case GXHDO101B023Const.INSATSU_END_MAKUATSU_MAX:
                return StringUtil.nullToBlank(srDpprintData.getMakuatumaxEnd());
            // 印刷ｴﾝﾄﾞ膜厚MIN
            case GXHDO101B023Const.INSATSU_END_MAKUATSU_MIN:
                return StringUtil.nullToBlank(srDpprintData.getMakuatuminEnd());
            // 印刷ｴﾝﾄﾞ膜厚CV
            case GXHDO101B023Const.INSATSU_END_MAKUATSU_CV:
                return StringUtil.nullToBlank(srDpprintData.getMakuatucvEnd());
            // PTN間距離印刷ｴﾝﾄﾞ X Min
            case GXHDO101B023Const.PTN_INSATSU_END_X_MIN:
                return StringUtil.nullToBlank(srDpprintData.getEndPtnDistX());
            // PTN間距離印刷ｴﾝﾄﾞ Y Min
            case GXHDO101B023Const.PTN_INSATSU_END_Y_MIN:
                return StringUtil.nullToBlank(srDpprintData.getEndPtnDistY());
            // 開始ﾃﾝｼｮﾝ計
            case GXHDO101B023Const.KAISHI_TENSION_KEI:
                return StringUtil.nullToBlank(srDpprintData.getTensionsSum());
            // 開始ﾃﾝｼｮﾝ前
            case GXHDO101B023Const.KAISHI_TENSION_MAE:
                return StringUtil.nullToBlank(srDpprintData.getTensionstemae());
            // 開始ﾃﾝｼｮﾝ奥
            case GXHDO101B023Const.KAISHI_TENSION_OKU:
                return StringUtil.nullToBlank(srDpprintData.getTensionsoku());
            // 終了ﾃﾝｼｮﾝ計
            case GXHDO101B023Const.SHURYOU_TENSION_KEI:
                return StringUtil.nullToBlank(srDpprintData.getTensioneSum());
            // 終了ﾃﾝｼｮﾝ前
            case GXHDO101B023Const.SHURYOU_TENSION_MAE:
                return StringUtil.nullToBlank(srDpprintData.getTensionetemae());
            // 終了ﾃﾝｼｮﾝ奥
            case GXHDO101B023Const.SHURYOU_TENSION_OKU:
                return StringUtil.nullToBlank(srDpprintData.getTensioneoku());
            // 終了時ﾆｼﾞﾐ・ｶｽﾚ確認
            case GXHDO101B023Const.SHURYOU_JI_NIJIMI_KASURE_CHECK:
                switch (StringUtil.nullToBlank(srDpprintData.getNijimikasureEnd())) {
                    case "0":
                        return "NG";
                    case "1":
                        return "OK";
                    default:
                        return "";
                }

            // 印刷ｴﾝﾄﾞ時担当者
            case GXHDO101B023Const.INSATSU_ENDJI_TANTOUSHA:
                return StringUtil.nullToBlank(srDpprintData.getTantoEnd());
            // 印刷枚数
            case GXHDO101B023Const.INSATSU_MAISUU:
                return StringUtil.nullToBlank(srDpprintData.getPrintmaisuu());
            //備考1
            case GXHDO101B023Const.BIKOU1:
                return StringUtil.nullToBlank(srDpprintData.getBikou1());
            //備考2
            case GXHDO101B023Const.BIKOU2:
                return StringUtil.nullToBlank(srDpprintData.getBikou2());

            // ﾌﾞﾚｰﾄﾞ印刷長
            case GXHDO101B023Const.BLADEINSATSUTYOU:
                return StringUtil.nullToBlank(srDpprintData.getBladeinsatsutyou());

            // ｽﾞﾚ量基準値X
            case GXHDO101B023Const.ZURERYOUKIJUNCHIX:
                return StringUtil.nullToBlank(srDpprintData.getZureryoukijunchix());

            // ｽﾞﾚ量基準値Y
            case GXHDO101B023Const.ZURERYOUKIJUNCHIY:
                return StringUtil.nullToBlank(srDpprintData.getZureryoukijunchiy());

            // 合わせ精度 蛇行
            case GXHDO101B023Const.AWASESEIDODAKOU:
                return StringUtil.nullToBlank(srDpprintData.getAwaseseidodakou());

            // 合わせ精度 流れ
            case GXHDO101B023Const.AWASESEIDONAGARE:
                return StringUtil.nullToBlank(srDpprintData.getAwaseseidonagare());

            // 先行ﾛｯﾄNo
            case GXHDO101B023Const.SENKOULOTNO:
                return StringUtil.nullToBlank(srDpprintData.getSenkoulotno());

            // ﾃｰﾌﾟ使い切り
            case GXHDO101B023Const.TAPETSUKAIKIRI:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srDpprintData.getTapetsukaikiri()));

            // 次ﾛｯﾄへ
            case GXHDO101B023Const.JILOTHE:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srDpprintData.getJilothe()));

            // 成形長さ
            case GXHDO101B023Const.SEIKEINAGASA:
                return StringUtil.nullToBlank(srDpprintData.getSeikeinagasa());

            // 備考3
            case GXHDO101B023Const.BIKOU3:
                return StringUtil.nullToBlank(srDpprintData.getBikou3());

            // 備考4
            case GXHDO101B023Const.BIKOU4:
                return StringUtil.nullToBlank(srDpprintData.getBikou4());

            // 備考5
            case GXHDO101B023Const.BIKOU5:
                return StringUtil.nullToBlank(srDpprintData.getBikou5());

            // 印刷長さ
            case GXHDO101B023Const.PRINTLENGTH:
                return StringUtil.nullToBlank(srDpprintData.getPrintlength());

            default:
                return null;

        }
    }

    /**
     * 印刷・DP2_仮登録(tmp_sr_dpprint)登録処理(削除時)
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
    private void insertDeleteDataTmpSrDpprint(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_dpprint ("
                + "kojyo,lotno,edaban,kaisuu,kcpno,kouteikubun,tapelotno,petfilmsyurui,taperollno1,taperollno2,taperollno3,pastelotno,pastenendo,pasteondo,pkokeibun1,"
                + "pastelotno2,handoumei,handouno,handoumaisuu,bladeno,bladegaikan,BladeATu,AtudoNo,AtudoMaisuu,AtuDoATu,gouki,kansouondo,kansouondo2,kansouondo3,kansouondo4,"
                + "kansouondo5,hansouspeed,startdatetime,tantousya,kakuninsya,makuatuave_start,makuatumax_start,makuatumin_start,makuatucv_start,nijimikasure_start,"
                + "start_ptn_dist_x,start_ptn_dist_y,TensionS_sum,TensionStemae,TensionSoku,enddatetime,tanto_end,printmaisuu,makuatuave_end,makuatumax_end,makuatumin_end,"
                + "makuatucv_end,nijimikasure_end,end_ptn_dist_x,end_ptn_dist_y,TensionE_sum,TensionEtemae,TensionEoku,genryoukigou,"
                + "bladeinsatsutyou,zureryoukijunchix,zureryoukijunchiy,awaseseidodakou,awaseseidonagare,skojyo,slotno,sedaban,tapetsukaikiri,jilothe,seikeinagasa,bikou3,bikou4,bikou5,"
                + "bikou1,bikou2,torokunichiji,kosinnichiji,"
                + "revision,deleteflag,tapeatu,printlength,kansouondoshita,kansouondoshita2,kansouondoshita3,kansouondoshita4"
                + ") SELECT "
                + "kojyo,lotno,edaban,kaisuu,kcpno,kouteikubun,tapelotno,petfilmsyurui,taperollno1,taperollno2,taperollno3,pastelotno,pastenendo,pasteondo,pkokeibun1,"
                + "pastelotno2,handoumei,handouno,handoumaisuu,bladeno,bladegaikan,BladeATu,AtudoNo,AtudoMaisuu,AtuDoATu,gouki,kansouondo,kansouondo2,kansouondo3,kansouondo4,"
                + "kansouondo5,hansouspeed,startdatetime,tantousya,kakuninsya,makuatuave_start,makuatumax_start,makuatumin_start,makuatucv_start,nijimikasure_start,"
                + "start_ptn_dist_x,start_ptn_dist_y,TensionS_sum,TensionStemae,TensionSoku,enddatetime,tanto_end,printmaisuu,makuatuave_end,makuatumax_end,makuatumin_end,"
                + "makuatucv_end,nijimikasure_end,end_ptn_dist_x,end_ptn_dist_y,TensionE_sum,TensionEtemae,TensionEoku,genryoukigou,"
                + "bladeinsatsutyou,zureryoukijunchix,zureryoukijunchiy,awaseseidodakou,awaseseidonagare,skojyo,slotno,sedaban,tapetsukaikiri,jilothe,seikeinagasa,bikou3,bikou4,bikou5,"
                + "bikou1,bikou2,?,?,"
                + "?,?,tapeatu,printlength,kansouondoshita,kansouondoshita2,kansouondoshita3,kansouondoshita4 "
                + "FROM sr_dpprint "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? ";

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
        params.add(jissekino); //回数

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷・DP2_ｻﾌﾞ画面仮登録(tmp_sub_sr_dpprint)登録処理(削除時)
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void insertDeleteDataTmpSubSrDpprint(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime) throws SQLException {
        String sql = "INSERT INTO tmp_sub_sr_dpprint ("
                + "kojyo,lotno,edaban,kaisuu,makuatsu_start1,makuatsu_start2,makuatsu_start3,makuatsu_start4,makuatsu_start5,makuatsu_start6,"
                + "makuatsu_start7,makuatsu_start8,makuatsu_start9,start_ptn_dist_x1,start_ptn_dist_x2,start_ptn_dist_x3,"
                + "start_ptn_dist_x4,start_ptn_dist_x5,start_ptn_dist_y1,start_ptn_dist_y2,start_ptn_dist_y3,start_ptn_dist_y4,"
                + "start_ptn_dist_y5,makuatsu_end1,makuatsu_end2,makuatsu_end3,makuatsu_end4,makuatsu_end5,makuatsu_end6,"
                + "makuatsu_end7,makuatsu_end8,makuatsu_end9,end_ptn_dist_x1,end_ptn_dist_x2,end_ptn_dist_x3,end_ptn_dist_x4,"
                + "end_ptn_dist_x5,end_ptn_dist_y1,end_ptn_dist_y2,end_ptn_dist_y3,end_ptn_dist_y4,end_ptn_dist_y5,torokunichiji,"
                + "kosinnichiji,revision,deleteflag"
                + ") SELECT "
                + "kojyo,lotno,edaban,kaisuu,makuatsu_start1,makuatsu_start2,makuatsu_start3,makuatsu_start4,makuatsu_start5,makuatsu_start6,"
                + "makuatsu_start7,makuatsu_start8,makuatsu_start9,start_ptn_dist_x1,start_ptn_dist_x2,start_ptn_dist_x3,"
                + "start_ptn_dist_x4,start_ptn_dist_x5,start_ptn_dist_y1,start_ptn_dist_y2,start_ptn_dist_y3,start_ptn_dist_y4,"
                + "start_ptn_dist_y5,makuatsu_end1,makuatsu_end2,makuatsu_end3,makuatsu_end4,makuatsu_end5,makuatsu_end6,"
                + "makuatsu_end7,makuatsu_end8,makuatsu_end9,end_ptn_dist_x1,end_ptn_dist_x2,end_ptn_dist_x3,end_ptn_dist_x4,"
                + "end_ptn_dist_x5,end_ptn_dist_y1,end_ptn_dist_y2,end_ptn_dist_y3,end_ptn_dist_y4,end_ptn_dist_y5,?,"
                + "?,?,? "
                + "FROM sub_sr_dpprint "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? ";

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
        params.add(jissekino); //回数
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
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
    private void setInputItemDataSubFormC020(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban, String jotaiFlg, int jissekino) throws SQLException {
        // サブ画面の情報を取得
        GXHDO101C020 beanGXHDO101C020 = (GXHDO101C020) SubFormUtil.getSubFormBean("GXHDO101C020");
        List<Map<String, Object>> initDataSubFormC020 = null;
        if (JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg) || JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            initDataSubFormC020 = getInitDataSubFormC020(queryRunnerQcdb, kojyo, lotNo, edaban, jotaiFlg, jissekino);
        }
        
        GXHDO101C020Model model = new GXHDO101C020Model();
        // 登録データが無い場合空の状態で初期値をセットする。
        // 登録データがあれば登録データをセットする。
        model = GXHDO101C020Logic.createGXHDO101C020Model(initDataSubFormC020, "GXHDO101B023");

        model.setReturnItemId_TapeLot1_Hinmei(GXHDO101B023Const.DENKYOKU_TAPE);
        model.setReturnItemId_TapeLot1_Conventionallot(GXHDO101B023Const.SLIP_LOTNO);
        model.setReturnItemId_TapeLot1_Lotkigo(GXHDO101B023Const.GENRYO_KIGOU);
        model.setReturnItemId_TapeLot1_Tapelength(GXHDO101B023Const.SEIKEINAGASA);
        model.setReturnItemId_TapeLot1_Rollno(GXHDO101B023Const.ROLL_NO1);
        model.setReturnItemId_TapeLot2_Rollno(GXHDO101B023Const.ROLL_NO2);
        model.setReturnItemId_TapeLot3_Rollno(GXHDO101B023Const.ROLL_NO3);
        model.setReturnItemId_PasteLot1_Hinmei(GXHDO101B023Const.DENKYOKU_PASTE);
        model.setReturnItemId_PasteLot1_Conventionallot(GXHDO101B023Const.PASTE_LOT_NO1);
        model.setReturnItemId_PasteLot1_Kokeibunpct(GXHDO101B023Const.PASTE_KOKEIBUN1);
        model.setReturnItemId_PasteLot2_Conventionallot(GXHDO101B023Const.PASTE_LOT_NO2);
        //model.setReturnItemId_PasteLot2_Kokeibunpct(GXHDO101B023Const.PASTE_KOKEIBUN2);
        model.setReturnItemId_Petname(GXHDO101B023Const.PET_FILM_SHURUI);
        model.setReturnItemId_TapeLot1_thickness_um(GXHDO101B023Const.TAPE_ATSU);
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
            String kojyo, String lotNo, String edaban, String jotaiFlg, int jissekino) throws SQLException {
        
        String tableName = " from sr_mwiplotlink ";
        String whereSQL = " where kojyo = ? and lotno = ? and edaban = ? and gamenid = ? and deleteflag = ? and kaisuu = ?";
        if (JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg)) {
            tableName = " from tmp_sr_mwiplotlink ";
            whereSQL = " where kojyo = ? and lotno = ? and edaban = ? and gamenid = ? and kaisuu = ?";
        }
        
        String sql = "select mkojyo, mlotno, medaban, mkubun, mkubunno" + tableName + whereSQL;
        
        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add("GXHDO101B023");
        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            params.add(0);
        }
        params.add(jissekino);
        
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
            String kojyo, String lotNo, String edaban, Timestamp systemTime, int jissekino) throws SQLException {
        
        String sql = "INSERT INTO tmp_sr_mwiplotlink("
                + "kojyo, lotno, edaban, mkojyo, mlotno, medaban, mkubun, mkubunno, "
                + "gamenid, tourokunichiji, koushinnichiji, kaisuu) VALUES "
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
            params.add("GXHDO101B023"); //画面ID
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時
            params.add(jissekino); //回数
            
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
            String kojyo, String lotNo, String edaban, Timestamp systemTime, int jissekino) throws SQLException {
        
        String sql = "INSERT INTO sr_mwiplotlink("
                + "kojyo, lotno, edaban, mkojyo, mlotno, medaban, mkubun, mkubunno, "
                + "gamenid, tourokunichiji, koushinnichiji, deleteflag, kaisuu) VALUES "
                + "(? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?)";
        
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
            params.add("GXHDO101B023"); //画面ID
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時
            params.add(0); //削除フラグ
            params.add(jissekino); //回数
            
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
            String kojyo, String lotNo, String edaban, Timestamp systemTime, int jissekino) throws SQLException {
        
        deleteTmpSrMwiplotlink(queryRunnerQcdb, conQcdb, kojyo, lotNo, edaban, jissekino);
        insertTmpSrMwiplotlink(queryRunnerQcdb, conQcdb, kojyo, lotNo, edaban, systemTime, jissekino);
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
            String kojyo, String lotNo, String edaban, Timestamp systemTime, int jissekino) throws SQLException {
        
        GXHDO101C020 beanGXHDO101C020 = (GXHDO101C020) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C020);
        List<GXHDO101C020Model.GenryouLotData> genryouLotDataList = beanGXHDO101C020.getGxhdO101c020Model().getGenryouLotDataList();
        
        for (GXHDO101C020Model.GenryouLotData genryouLotData : genryouLotDataList) {
            
            String typeName = genryouLotData.getTypeName();
            String mkubun = getKubun(typeName);
            String mkubunno = getKubunNo(typeName);
            
            boolean isExist = isExistFromSrMwiplotlink(queryRunnerQcdb, kojyo, lotNo, edaban, mkubun, mkubunno, jissekino);
            if (isExist) {
                // データが存在の場合、deleteflagを更新
                updateSrMwiplotlinkToDelete(queryRunnerQcdb, conQcdb, kojyo, lotNo, edaban, mkubun, mkubunno, systemTime, jissekino);
            }
            insertSrMwiplotlinkByData(queryRunnerQcdb, conQcdb, kojyo, lotNo, edaban, systemTime, genryouLotData, jissekino);
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
            String kojyo, String lotNo, String edaban, String mkubun, String mkubunno, Timestamp systemTime, int jissekino) throws SQLException {
        
        String sql = "UPDATE sr_mwiplotlink "
                + "SET deleteflag = ?, koushinnichiji = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND gamenid = ? AND mkubun = ? AND mkubunno = ? AND deleteflag = '0' AND kaisuu = ?";
        
        List<Object> params = new ArrayList<>();
        // 更新内容
        params.add(1); //削除フラグ
        params.add(systemTime); //更新日

        // 検索条件
        params.add(kojyo); //工場ｺｰﾄﾞ
        params.add(lotNo); //ﾛｯﾄNo
        params.add(edaban); //枝番
        params.add("GXHDO101B023");
        params.add(mkubun);
        params.add(mkubunno);
        params.add(jissekino);

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
            GXHDO101C020Model.GenryouLotData genryouLotData, int jissekino) throws SQLException {
        
        if (StringUtil.isEmpty(genryouLotData.getValue())) {
            return;
        }
        
        String sql = "INSERT INTO sr_mwiplotlink("
                + "kojyo, lotno, edaban, mkojyo, mlotno, medaban, mkubun, mkubunno, "
                + "gamenid, tourokunichiji, koushinnichiji, deleteflag, kaisuu) VALUES "
                + "(? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?, ?)";
        
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
        params.add("GXHDO101B023"); //画面ID
        params.add(systemTime); //登録日時
        params.add(systemTime); //更新日時
        params.add(0); //削除フラグ
        params.add(jissekino); //回数

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
            String kojyo, String lotNo, String edaban, String mkubun, String mkubunno, int jissekino) throws SQLException {
        
        String sql = "select mkojyo, mlotno, medaban, mkubun, mkubunno "
                + "from sr_mwiplotlink "
                + "where kojyo = ? and lotno = ? and edaban = ? and gamenid = ? and mkubun = ? and mkubunno = ? and deleteflag = ? and kaisuu = ?";
        
        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add("GXHDO101B023");
        params.add(mkubun);
        params.add(mkubunno);
        params.add(0);
        params.add(jissekino);
        
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
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {
        
        String sql = "DELETE FROM tmp_sr_mwiplotlink "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND gamenid = ? AND kaisuu = ?";

        //更新値設定
        List<Object> params = new ArrayList<>();

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add("GXHDO101B023");
        params.add(jissekino);
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
            String kojyo, String lotNo, String edaban, Timestamp systemTime, int jissekino) throws SQLException {
        
        GXHDO101C020 beanGXHDO101C020 = (GXHDO101C020) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C020);
        List<GXHDO101C020Model.GenryouLotData> genryouLotDataList = beanGXHDO101C020.getGxhdO101c020Model().getGenryouLotDataList();
        
        for (GXHDO101C020Model.GenryouLotData genryouLotData : genryouLotDataList) {
            
            String typeName = genryouLotData.getTypeName();
            String mkubun = getKubun(typeName);
            String mkubunno = getKubunNo(typeName);
            
            if (isExistFromSrMwiplotlink(queryRunnerQcdb, kojyo, lotNo, edaban, mkubun, mkubunno, jissekino)) {
                updateSrMwiplotlinkToDelete(queryRunnerQcdb, conQcdb, kojyo, lotNo, edaban, mkubun, mkubunno, systemTime, jissekino);
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
            case GXHDO101C020Model.PASTE_LOT_4:
            case GXHDO101C020Model.PASTE_LOT_5:
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
            case GXHDO101C020Model.PASTE_LOT_3:
                mkubunno = "3";
                break;
            case GXHDO101C020Model.PASTE_LOT_4:
                mkubunno = "4";
                break;
            case GXHDO101C020Model.PASTE_LOT_5:
                mkubunno = "5";
                break;
        }
        return mkubunno;
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

    /**
     * 設備ﾃﾞｰﾀ連携処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setSetsubiDataRenkei(ProcessData processData) {

        QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
        // セッションから情報を取得
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String lotNo = (String) session.getAttribute("lotNo");
        int jissekino = Integer.parseInt(StringUtil.nullToBlank(session.getAttribute("jissekino")));
        try {
            // (23)[tmp_dpprint_kanri]から、ﾃﾞｰﾀの取得
            List<Map<String, Object>> tmpDpprintKanriDataList = loadTmpGraprintKanriData(queryRunnerQcdb, lotNo, null, jissekino);
            if (tmpDpprintKanriDataList == null || tmpDpprintKanriDataList.isEmpty()) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                ErrorMessageInfo checkItemError = MessageUtil.getErrorMessageInfo("XHD-000210", true, true, null, "設備ﾃﾞｰﾀ");
                if (checkItemError != null) {
                    processData.setErrorMessageInfoList(Arrays.asList(checkItemError));
                    return processData;
                }
            }
            HashMap<String, String> itemIdConvertMap = new HashMap<>();
            itemIdConvertMap.put(GXHDO101B023Const.INSATSU_GOUKI, "goukicode"); // 印刷号機
            itemIdConvertMap.put(GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI1, "insatsu_sps_gra_kansou_ondo_hyoujichi1"); // 乾燥温度表示値1(℃)
            itemIdConvertMap.put(GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI2, "insatsu_sps_gra_kansou_ondo_hyoujichi2"); // 乾燥温度表示値2(℃)
            itemIdConvertMap.put(GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI3, "insatsu_sps_gra_kansou_ondo_hyoujichi3"); // 乾燥温度表示値3(℃)
            itemIdConvertMap.put(GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI4, "insatsu_sps_gra_kansou_ondo_hyoujichi4"); // 乾燥温度表示値4(℃)
            itemIdConvertMap.put(GXHDO101B023Const.KANSOU_ONDO_SHITA_HYOUJICHI1, "insatsu_sps_gra_kansou_shita_ondo_hyoujichi1"); // 乾燥温度下側表示値1(℃)
            itemIdConvertMap.put(GXHDO101B023Const.KANSOU_ONDO_SHITA_HYOUJICHI2, "insatsu_sps_gra_kansou_shita_ondo_hyoujichi2"); // 乾燥温度下側表示値2(℃)
            itemIdConvertMap.put(GXHDO101B023Const.KANSOU_ONDO_SHITA_HYOUJICHI3, "insatsu_sps_gra_kansou_shita_ondo_hyoujichi3"); // 乾燥温度下側表示値3(℃)
            itemIdConvertMap.put(GXHDO101B023Const.KANSOU_ONDO_SHITA_HYOUJICHI4, "insatsu_sps_gra_kansou_shita_ondo_hyoujichi4"); // 乾燥温度下側表示値4(℃)
            itemIdConvertMap.put(GXHDO101B023Const.HANSOU_SOKUDO, "insatsu_sps_gra_hansou_sokudo"); // 搬送速度(m/min)
            itemIdConvertMap.put(GXHDO101B023Const.ATSUDOU_ATSURYOKU, "insatsu_sps_gra_atsudou_atsuryoku"); // 圧胴圧力
            itemIdConvertMap.put(GXHDO101B023Const.BLADE_ATSURYOKU, "insatsu_sps_gra_blade_atsuryoku"); // ﾌﾞﾚｰﾄﾞ圧力(Mpa)
            itemIdConvertMap.put(GXHDO101B023Const.HANDOU_MEI, "insatsu_sps_gra_seihan_or_handou_mei"); // 版胴名
            itemIdConvertMap.put(GXHDO101B023Const.HANDOU_LOTNO, "insatsu_sps_gra_seihan_or_handou_no"); // 版胴ﾛｯﾄNo
            itemIdConvertMap.put(GXHDO101B023Const.HANDOU_SHIYOU_MAISUU, "insatsu_sps_gra_seihan_or_handou_shiyou_maisuu"); // 版胴使用枚数(枚)
            itemIdConvertMap.put(GXHDO101B023Const.ATSUDOU_SIYOU_MAISUU, "insatsu_sps_gra_atsudou_siyou_maisuu"); // 圧胴使用枚数(枚)
            itemIdConvertMap.put(GXHDO101B023Const.BLADEINSATSUTYOU, "insatsu_sps_gra_blade_insatsutyo"); // ﾌﾞﾚｰﾄﾞ印刷長（終了時）
            itemIdConvertMap.put(GXHDO101B023Const.INSATSU_KAISHI_DAY, "insatsu_sps_gra_insatsu_kaishi_day"); // 印刷開始日
            itemIdConvertMap.put(GXHDO101B023Const.INSATSU_KAISHI_TIME, "insatsu_sps_gra_insatsu_kaishi_time"); // 印刷開始時刻
            itemIdConvertMap.put(GXHDO101B023Const.INSATSU_SHURYOU_DAY, "insatsu_sps_gra_insatsu_shuryou_day"); // 印刷終了日
            itemIdConvertMap.put(GXHDO101B023Const.INSATSU_SHURYOU_TIME, "insatsu_sps_gra_insatsu_shuuryou_time"); // 印刷終了日時刻
            itemIdConvertMap.put(GXHDO101B023Const.KAISHI_TENSION_KEI, "insatsu_sps_gra_kaishi_tension_kei"); // 開始ﾃﾝｼｮﾝ計(N)
            itemIdConvertMap.put(GXHDO101B023Const.KAISHI_TENSION_MAE, "insatsu_sps_gra_kaishi_tension_mae"); // 開始ﾃﾝｼｮﾝ前(N)
            itemIdConvertMap.put(GXHDO101B023Const.KAISHI_TENSION_OKU, "insatsu_sps_gra_kaishi_tension_oku"); // 開始ﾃﾝｼｮﾝ奥(N)
            itemIdConvertMap.put(GXHDO101B023Const.SHURYOU_TENSION_KEI, "insatsu_sps_gra_shuryou_tension_kei"); // 終了ﾃﾝｼｮﾝ計(N)
            itemIdConvertMap.put(GXHDO101B023Const.SHURYOU_TENSION_MAE, "insatsu_sps_gra_shuryou_tension_mae"); // 終了ﾃﾝｼｮﾝ前(N)
            itemIdConvertMap.put(GXHDO101B023Const.SHURYOU_TENSION_OKU, "insatsu_sps_gra_shuryou_tension_oku"); // 終了ﾃﾝｼｮﾝ奥(N)
            itemIdConvertMap.put(GXHDO101B023Const.INSATSU_MAISUU, "insatsu_sps_gra_insatsu_maisuu"); // 印刷枚数(枚)
            itemIdConvertMap.put(GXHDO101B023Const.ZURERYOUKIJUNCHIX, "insatsu_sps_gra_zurekijyunti_x"); // ｽﾞﾚ量基準値X
            itemIdConvertMap.put(GXHDO101B023Const.ZURERYOUKIJUNCHIY, "insatsu_sps_gra_zurekijyunti_y"); // ｽﾞﾚ量基準値Y
            itemIdConvertMap.put(GXHDO101B023Const.AWASESEIDODAKOU, "insatsu_sps_gra_awaseseido_dakou"); // 合わせ精度 蛇行
            itemIdConvertMap.put(GXHDO101B023Const.AWASESEIDONAGARE,  "insatsu_sps_gra_awaseseido_nagare"); // 合わせ精度 流れ
            itemIdConvertMap.put(GXHDO101B023Const.PRINTLENGTH, "insatsu_sps_gra_printlength"); // 印刷枚数(枚)
            
            ErrorMessageInfo checkItemError = checkDataCooperation(processData, queryRunnerQcdb, lotNo, 1, itemIdConvertMap, jissekino);
            if (checkItemError != null) {
                processData.setErrorMessageInfoList(Arrays.asList(checkItemError));
                return processData;
            }
            doDataCooperation(processData, queryRunnerQcdb, lotNo, 1, itemIdConvertMap, jissekino);
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
            HashMap<String, String> itemIdConvertMap, int jissekino) throws SQLException {
        ErrorMessageInfo checkItemError = null;
        // 検索条件:ﾃﾞｰﾀの種類==datasyurui で、Ⅲ.画面表示仕様(21)を発行する。
        List<Map<String, Object>> tmpDpprintKanriDataList = loadTmpGraprintKanriData(queryRunnerQcdb, lotNo, String.valueOf(datasyurui), jissekino);
        if (tmpDpprintKanriDataList != null && !tmpDpprintKanriDataList.isEmpty()) {
            // 取得したﾃﾞｰﾀで実績Noが高い管理Noで、Ⅲ.画面表示仕様(22)を発行する。
            Map<String, Object> tmpDpprintKanriData = tmpDpprintKanriDataList.get(0);
            List<Map<String, Object>> tmpDpprintDataList = loadTmpGraprintData(queryRunnerQcdb, (Long) tmpDpprintKanriData.get("kanrino"));
            if (tmpDpprintDataList != null && !tmpDpprintDataList.isEmpty()) {
                // Ⅵ.画面項目制御・出力仕様.G3)入力項目部.【設備ﾃﾞｰﾀ連携】ﾎﾞﾀﾝ押下時.開始時 の該当項目へ取得ﾃﾞｰﾀを上書きする。
                List<String> numberItemList;
                if (datasyurui == 1 || datasyurui == 2) {
                    // 開始時(ﾃﾞｰﾀ種類1or2)
                    numberItemList = Arrays.asList(GXHDO101B023Const.ATSUDOU_SIYOU_MAISUU, GXHDO101B023Const.HANDOU_SHIYOU_MAISUU);
                    checkItemError = checkDataCooperationItemData(processData, numberItemList, tmpDpprintDataList, itemIdConvertMap);
                    if (checkItemError == null) {
                        checkItemError = checkDataCooperation(processData, queryRunnerQcdb, lotNo, 3, itemIdConvertMap, jissekino);
                        if (checkItemError != null) {
                            return checkItemError;
                        }
                    } else {
                        return checkItemError;
                    }
                } else if (datasyurui == 3 || datasyurui == 4) {
                    // 終了時(ﾃﾞｰﾀ種類3or4)
                    numberItemList = Arrays.asList(GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI1, GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI2, GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI3, GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI4,
                            GXHDO101B023Const.HANSOU_SOKUDO,GXHDO101B023Const.ATSUDOU_ATSURYOKU,GXHDO101B023Const.BLADE_ATSURYOKU,
                            GXHDO101B023Const.KAISHI_TENSION_KEI,GXHDO101B023Const.KAISHI_TENSION_MAE,GXHDO101B023Const.KAISHI_TENSION_OKU,
                            GXHDO101B023Const.SHURYOU_TENSION_KEI,GXHDO101B023Const.SHURYOU_TENSION_MAE,GXHDO101B023Const.SHURYOU_TENSION_OKU,
                            GXHDO101B023Const.INSATSU_MAISUU,GXHDO101B023Const.ZURERYOUKIJUNCHIX,GXHDO101B023Const.ZURERYOUKIJUNCHIY,
                            GXHDO101B023Const.AWASESEIDODAKOU,GXHDO101B023Const.AWASESEIDONAGARE,GXHDO101B023Const.PRINTLENGTH,GXHDO101B023Const.KANSOU_ONDO_SHITA_HYOUJICHI1, GXHDO101B023Const.KANSOU_ONDO_SHITA_HYOUJICHI2, GXHDO101B023Const.KANSOU_ONDO_SHITA_HYOUJICHI3, GXHDO101B023Const.KANSOU_ONDO_SHITA_HYOUJICHI4);
                    checkItemError = checkDataCooperationItemData(processData, numberItemList, tmpDpprintDataList, itemIdConvertMap);
                }
            } else {
                if (datasyurui == 1 || datasyurui == 2) {
                    checkItemError = checkDataCooperation(processData, queryRunnerQcdb, lotNo, 3, itemIdConvertMap, jissekino);
                    if (checkItemError != null) {
                        return checkItemError;
                    }
                }
            }
        } else {
            datasyurui++;
            if (datasyurui <= 4) {
                checkItemError = checkDataCooperation(processData, queryRunnerQcdb, lotNo, datasyurui, itemIdConvertMap, jissekino);
                if (checkItemError != null) {
                    return checkItemError;
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
     * @param tmpSrDpprintDataList 取得ﾃﾞｰﾀ
     * @param itemIdConvertMap ﾌｫｰﾑﾊﾟﾗﾒｰﾀ(item_id)とtmp_sr_graprint(item_id)の対比表
     * @return ｴﾗｰﾒｯｾｰｼﾞ情報
     */
    private ErrorMessageInfo checkDataCooperationItemData(ProcessData processData, List<String> numberItemList, List<Map<String, Object>> tmpSrDpprintDataList,
            HashMap<String, String> itemIdConvertMap) {
        for(String itemId : numberItemList){
            FXHDD01 itemData = processData.getItemList().stream().filter(n -> itemId.equals(n.getItemId())).findFirst().orElse(null);
            String[] tmpSrGraprintItemId = {itemId};
            if (itemData != null) {
                if (itemIdConvertMap.containsKey(itemId)) {
                    tmpSrGraprintItemId[0] = itemIdConvertMap.get(itemId);
                }
                Map<String, Object> tmpSrGraprintData = tmpSrDpprintDataList.stream().filter(e -> tmpSrGraprintItemId[0].equals(e.get("item_id"))).findFirst().orElse(null);
                if (tmpSrGraprintData != null && !tmpSrGraprintData.isEmpty()) {
                    String ataiValue = StringUtil.nullToBlank(tmpSrGraprintData.get("atai"));
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
            HashMap<String, String> itemIdConvertMap, int jissekino) throws SQLException {
        // 検索条件:ﾃﾞｰﾀの種類==datasyurui で、Ⅲ.画面表示仕様(21)を発行する。
        List<Map<String, Object>> tmpDpprintKanriDataList = loadTmpGraprintKanriData(queryRunnerQcdb, lotNo, String.valueOf(datasyurui), jissekino);
        if (tmpDpprintKanriDataList != null && !tmpDpprintKanriDataList.isEmpty()) {
            // 取得したﾃﾞｰﾀで実績Noが高い管理Noで、Ⅲ.画面表示仕様(22)を発行する。
            Map<String, Object> tmpDpprintKanriData = tmpDpprintKanriDataList.get(0);
            List<Map<String, Object>> tmpDpprintDataList = loadTmpGraprintData(queryRunnerQcdb, (Long) tmpDpprintKanriData.get("kanrino"));
            if (tmpDpprintDataList != null && !tmpDpprintDataList.isEmpty()) {
                // Ⅵ.画面項目制御・出力仕様.G3)入力項目部.【設備ﾃﾞｰﾀ連携】ﾎﾞﾀﾝ押下時.開始時 の該当項目へ取得ﾃﾞｰﾀを上書きする。
                List<String> setValueItemList = null;
                if (datasyurui == 1 || datasyurui == 2) {
                    // 開始時(ﾃﾞｰﾀ種類1or2)
                    setValueItemList = Arrays.asList(GXHDO101B023Const.INSATSU_GOUKI, GXHDO101B023Const.HANDOU_MEI, GXHDO101B023Const.HANDOU_LOTNO, GXHDO101B023Const.ATSUDOU_SIYOU_MAISUU,
                            GXHDO101B023Const.HANDOU_SHIYOU_MAISUU, GXHDO101B023Const.INSATSU_KAISHI_DAY,GXHDO101B023Const.INSATSU_KAISHI_TIME);
                    setDataCooperationItemData(processData, setValueItemList, tmpDpprintDataList, itemIdConvertMap);
                    doDataCooperation(processData, queryRunnerQcdb, lotNo, 3, itemIdConvertMap, jissekino);
                } else if (datasyurui == 3 || datasyurui == 4) {
                    // 終了時(ﾃﾞｰﾀ種類3or4)
                    setValueItemList = Arrays.asList(GXHDO101B023Const.INSATSU_GOUKI, GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI1, GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI2, GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI3, GXHDO101B023Const.KANSOU_ONDO_HYOUJICHI4,
                            GXHDO101B023Const.HANSOU_SOKUDO,GXHDO101B023Const.ATSUDOU_ATSURYOKU,
                            GXHDO101B023Const.BLADE_ATSURYOKU, GXHDO101B023Const.BLADEINSATSUTYOU, GXHDO101B023Const.INSATSU_SHURYOU_DAY,
                            GXHDO101B023Const.INSATSU_SHURYOU_TIME,GXHDO101B023Const.KAISHI_TENSION_KEI,GXHDO101B023Const.KAISHI_TENSION_MAE,
                            GXHDO101B023Const.KAISHI_TENSION_OKU,GXHDO101B023Const.SHURYOU_TENSION_KEI,GXHDO101B023Const.SHURYOU_TENSION_MAE,
                            GXHDO101B023Const.SHURYOU_TENSION_OKU,GXHDO101B023Const.INSATSU_MAISUU,GXHDO101B023Const.ZURERYOUKIJUNCHIX,
                            GXHDO101B023Const.ZURERYOUKIJUNCHIY,GXHDO101B023Const.AWASESEIDODAKOU,GXHDO101B023Const.AWASESEIDONAGARE,GXHDO101B023Const.PRINTLENGTH,GXHDO101B023Const.KANSOU_ONDO_SHITA_HYOUJICHI1, GXHDO101B023Const.KANSOU_ONDO_SHITA_HYOUJICHI2, GXHDO101B023Const.KANSOU_ONDO_SHITA_HYOUJICHI3, GXHDO101B023Const.KANSOU_ONDO_SHITA_HYOUJICHI4);
                    setDataCooperationItemData(processData, setValueItemList, tmpDpprintDataList, itemIdConvertMap);
                }
            } else {
                if (datasyurui == 1 || datasyurui == 2) {
                    doDataCooperation(processData, queryRunnerQcdb, lotNo, 3, itemIdConvertMap, jissekino);
                }
            }
        } else {
            datasyurui++;
            if (datasyurui <= 4) {
                doDataCooperation(processData, queryRunnerQcdb, lotNo, datasyurui, itemIdConvertMap, jissekino);
            }
        }
    }

    /**
     * 【設備ﾃﾞｰﾀ連携】ﾎﾞﾀﾝ押下時.開始時 の該当項目へ取得ﾃﾞｰﾀを上書きする
     *
     * @param processData 処理制御データ
     * @param setValueItemList 項目リスト
     * @param tmpSrDpprintDataList 取得ﾃﾞｰﾀ
     * @param itemIdConvertMap ﾌｫｰﾑﾊﾟﾗﾒｰﾀ(item_id)とtmp_sr_graprint(item_id)の対比表
     */
    private void setDataCooperationItemData(ProcessData processData, List<String> setValueItemList, List<Map<String, Object>> tmpSrDpprintDataList,
            HashMap<String, String> itemIdConvertMap) {
        setValueItemList.forEach(itemId -> {
            FXHDD01 itemData = processData.getItemList().stream().filter(n -> itemId.equals(n.getItemId())).findFirst().orElse(null);
            String[] tmpSrGraprintItemId = {itemId};
            if (itemData != null) {
                if (itemIdConvertMap.containsKey(itemId)) {
                    tmpSrGraprintItemId[0] = itemIdConvertMap.get(itemId);
                }
                Map<String, Object> tmpSrDpprintData = tmpSrDpprintDataList.stream().filter(e -> tmpSrGraprintItemId[0].equals(e.get("item_id"))).findFirst().orElse(null);
                if (tmpSrDpprintData != null && !tmpSrDpprintData.isEmpty()) {
                    itemData.setValue(StringUtil.nullToBlank(tmpSrDpprintData.get("atai")));
                }
            }
        });
    }
    
    /**
     * (21)[tmp_dpprint_kanri]から、ﾃﾞｰﾀの取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param datasyurui データ種類(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<Map<String, Object>> loadTmpGraprintKanriData(QueryRunner queryRunnerQcdb, String lotNo, String datasyurui, int jissekino) throws SQLException {
        String kojyo = lotNo.substring(0, 3);
        String lotno = lotNo.substring(3, 11);
        String edaban = lotNo.substring(11, 14);

        // [tmp_dpprint_kanri]から、ﾃﾞｰﾀの取得
        String sql = "SELECT distinct t1.kanrino, kojyo, lotno, edaban, datasyurui, jissekino, torokunichiji"
                + " FROM tmp_sr_graprint_kanri t1 "
                + " INNER JOIN tmp_sr_graprint t2 ON t1.kanrino = t2.kanrino "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND (t1.lot_flg < 9 OR t1.lot_flg is NULL)";
        if (!StringUtil.isEmpty(datasyurui)) {
            sql += " AND datasyurui = ? ";
        }
        if (jissekino == 1) {
            sql += " AND t2.item_id = 'dp2mode' AND t2.atai = '0' ";
        } else {
            sql += " AND t2.item_id = 'dp2mode' AND t2.atai = '1' ";
        }
        sql += " order by jissekino desc";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotno);
        params.add(edaban);
        if (!StringUtil.isEmpty(datasyurui)) {
            params.add(datasyurui);
        }
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapListHandler(), params.toArray());
    }

    /**
     * (22)[tmp_dpprint]から、ﾃﾞｰﾀの取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kanrino 管理No(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<Map<String, Object>> loadTmpGraprintData(QueryRunner queryRunnerQcdb, Long kanrino) throws SQLException {
        // [tmp_dpprint]から、ﾃﾞｰﾀの取得
        String sql = "SELECT kanrino, item_id, atai"
                + " FROM tmp_sr_graprint WHERE kanrino = ?";

        List<Object> params = new ArrayList<>();
        params.add(kanrino);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapListHandler(), params.toArray());
    }
    
}
