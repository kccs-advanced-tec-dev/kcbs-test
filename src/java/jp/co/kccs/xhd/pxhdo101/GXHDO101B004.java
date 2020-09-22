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
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import jp.co.kccs.xhd.common.InitMessage;
import jp.co.kccs.xhd.common.KikakuError;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.db.model.SrSpssekisou;
import jp.co.kccs.xhd.db.model.SubSrSpssekisou;
import jp.co.kccs.xhd.model.GXHDO101C006Model;
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

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/02/26<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * 変更日       2019/9/18<br>
 * 計画書No     K1811-DS001<br>
 * 変更者       KCSS K.Jo<br>
 * 変更理由     項目追加・変更<br>
 * <br>
 * 変更日	2020/09/21<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	KCSS D.Yanagida<br>
 * 変更理由	ロット混合対応<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101B004(積層・SPS)ロジック
 *
 * @author KCSS K.Jo
 * @since  2019/02/26
 */
public class GXHDO101B004 implements IFormLogic {

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
            processData.setNoCheckButtonId(Arrays.asList(
                    GXHDO101B004Const.BTN_HAKURI_GNG_TOP,
                    GXHDO101B004Const.BTN_HAKURI_INPUT_TOP,
                    GXHDO101B004Const.BTN_START_DATETIME_TOP,
                    GXHDO101B004Const.BTN_END_DATETIME_TOP,
                    GXHDO101B004Const.BTN_HAKURI_GNG_BOTTOM,
                    GXHDO101B004Const.BTN_HAKURI_INPUT_BOTTOM,
                    GXHDO101B004Const.BTN_START_DATETIME_BOTTOM,
                    GXHDO101B004Const.BTN_END_DATETIME_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO101B004Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO101B004Const.BTN_INSERT_TOP,
                    GXHDO101B004Const.BTN_DELETE_TOP,
                    GXHDO101B004Const.BTN_UPDATE_TOP,
                    GXHDO101B004Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO101B004Const.BTN_INSERT_BOTTOM,
                    GXHDO101B004Const.BTN_DELETE_BOTTOM,
                    GXHDO101B004Const.BTN_UPDATE_BOTTOM));

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
     * 剥離内容入力画面(サブ画面Open)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openHakuriInput(ProcessData processData) {

        try {

            processData.setMethod("");
            // コールバックパラメータにてサブ画面起動用の値を設定
            processData.setCollBackParam("gxhdo101c006");

            // 膜厚(SPS)の現在の値をサブ画面の表示用の値に渡す
            GXHDO101C006 beanGXHDO101C006 = (GXHDO101C006) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C006);
            beanGXHDO101C006.setGxhdO101c006ModelView(beanGXHDO101C006.getGxhdO101c006Model().clone());

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

        //端子テープ種類
        FXHDD01 itemTtapeSyurui = getItemRow(processData.getItemList(), GXHDO101B004Const.TTAPE_SYURUI);
        if ("NG".equals(itemTtapeSyurui.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemTtapeSyurui);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemTtapeSyurui.getLabel1());
        }
        
        // 空打ち
        FXHDD01 itemKaraut = getItemRow(processData.getItemList(), GXHDO101B004Const.KARAUTI);
        // 空打ち[秒]
        FXHDD01 itemKarautibyou = getItemRow(processData.getItemList(), GXHDO101B004Const.KARAUTIBYOU);
        // 空打ち[回]
        FXHDD01 itemKarautikai = getItemRow(processData.getItemList(), GXHDO101B004Const.KARAUTIKAI);
        
        // 関連ﾁｪｯｸ
        if("なし".equals(itemKaraut.getValue())){
            if(!StringUtil.isEmpty(itemKarautibyou.getValue()) || !StringUtil.isEmpty(itemKarautikai.getValue())){
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemKaraut,itemKarautibyou,itemKarautikai);
                return MessageUtil.getErrorMessageInfo("XHD-000098", true, true, errFxhdd01List);
            }
        
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

                // 積層・SPS_仮登録登録処理
                insertTmpSrSpssekisou(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

                // 積層・SPS_ｻﾌﾞ画面仮登録登録処理
                insertTmpSubSrSpssekisou(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, systemTime);

            } else {

                // 積層・SPS_仮登録更新処理
                updateTmpSrSpssekisou(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

                // 積層・SPS_ｻﾌﾞ画面仮登録更新処理
                updateTmpSubSrSpssekisou(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo8, edaban, systemTime);
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
            GXHDO101C006 beanGXHDO101C006 = (GXHDO101C006)SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C006);
            for (GXHDO101C006Model.HakuriInputData data : beanGXHDO101C006.getGxhdO101c006Model().getHakuriInputDataList()) {
                data.setSetsuuTextBackColor("");
                data.setBikouTextBackColor("");
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

        //下端子ﾌﾞｸ抜き
        FXHDD01 itemShitaTanshiBukunuki = getItemRow(processData.getItemList(), GXHDO101B004Const.SHITA_TANSHI_BUKUNUKI);
        if(StringUtil.isEmpty(itemShitaTanshiBukunuki.getValue())){
            //エラー発生時
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemShitaTanshiBukunuki);
            return MessageUtil.getErrorMessageInfo("", MessageUtil.getMessage("XHD-000032", "下端子ﾌﾞｸ抜き"), true, true, errFxhdd01List);
        }
        
        //ﾜﾚ、ｳｷ、かみ込みなき事
        FXHDD01 itemGaikanKakunin5 = getItemRow(processData.getItemList(), GXHDO101B004Const.GAIKAN_KAKUNIN5);
        if(StringUtil.isEmpty(itemGaikanKakunin5.getValue())){
            //エラー発生時
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemGaikanKakunin5);
            return MessageUtil.getErrorMessageInfo("", MessageUtil.getMessage("XHD-000032", "ﾜﾚ、ｳｷ、かみ込みなき事"), true, true, errFxhdd01List);
        }

        //端子テープ種類
        FXHDD01 itemTtapeSyurui = getItemRow(processData.getItemList(), GXHDO101B004Const.TTAPE_SYURUI);
        if ("NG".equals(itemTtapeSyurui.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemTtapeSyurui);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemTtapeSyurui.getLabel1());
        }
        
        //空打ち
        FXHDD01 itemKarauti = getItemRow(processData.getItemList(), GXHDO101B004Const.KARAUTI);
        if(StringUtil.isEmpty(itemKarauti.getValue())){
            //エラー発生時
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemKarauti);
            return MessageUtil.getErrorMessageInfo("", MessageUtil.getMessage("XHD-000032", "空打ち"), true, true, errFxhdd01List);
        }
        
        // 空打ち
        FXHDD01 itemKaraut = getItemRow(processData.getItemList(), GXHDO101B004Const.KARAUTI);
        // 空打ち[秒]
        FXHDD01 itemKarautibyou = getItemRow(processData.getItemList(), GXHDO101B004Const.KARAUTIBYOU);
        // 空打ち[回]
        FXHDD01 itemKarautikai = getItemRow(processData.getItemList(), GXHDO101B004Const.KARAUTIKAI);
        
        // 関連ﾁｪｯｸ
        if("なし".equals(itemKaraut.getValue())){
            if(!StringUtil.isEmpty(itemKarautibyou.getValue()) || !StringUtil.isEmpty(itemKarautikai.getValue())){
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemKaraut,itemKarautibyou,itemKarautikai);
                return MessageUtil.getErrorMessageInfo("XHD-000098", true, true, errFxhdd01List);
            }
        
        }
        if("あり".equals(itemKaraut.getValue())){
            if(StringUtil.isEmpty(itemKarautibyou.getValue()) || StringUtil.isEmpty(itemKarautikai.getValue())){
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemKaraut,itemKarautibyou,itemKarautikai);
                return MessageUtil.getErrorMessageInfo("XHD-000099", true, true, errFxhdd01List);
            }
        
        }
        

        ValidateUtil validateUtil = new ValidateUtil();
        // 開始日時、終了日時前後チェック
        FXHDD01 itemKaishiDay = getItemRow(processData.getItemList(), GXHDO101B004Const.KAISHI_DAY); //開始日
        FXHDD01 itemKaishiTime = getItemRow(processData.getItemList(), GXHDO101B004Const.KAISHI_TIME); // 開始時刻
        Date kaishiDate = DateUtil.convertStringToDate(itemKaishiDay.getValue(), itemKaishiTime.getValue());
        FXHDD01 itemShuryouDay = getItemRow(processData.getItemList(), GXHDO101B004Const.SHURYO_DAY); //終了日
        FXHDD01 itemShuryouTime = getItemRow(processData.getItemList(), GXHDO101B004Const.SHURYO_TIME); //終了時刻
        Date shuryoDate = DateUtil.convertStringToDate(itemShuryouDay.getValue(), itemShuryouTime.getValue());
        //R001チェック呼出し
        String msgCheckR001 = validateUtil.checkR001(itemKaishiDay.getLabel1(), kaishiDate, itemShuryouDay.getLabel1(), shuryoDate);
        if (!StringUtil.isEmpty(msgCheckR001)) {
            //エラー発生時
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemKaishiDay, itemKaishiTime, itemShuryouDay, itemShuryouTime);
            return MessageUtil.getErrorMessageInfo("", msgCheckR001, true, true, errFxhdd01List);
        }

        return null;
    }

    /**
     * 剥離内容入力画面チェック処理
     *
     * @return エラーリスト
     */
    private List<String> checkSubFormHakuriInput() {
        GXHDO101C006 beanGXHDO101C006 = (GXHDO101C006) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C006);
        return GXHDO101C006Logic.checkInput(beanGXHDO101C006.getGxhdO101c006Model());
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
            SrSpssekisou tmpSrSpssekisou = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {
                
                // 更新前の値を取得
                List<SrSpssekisou> srSpssekisouList = getSrSpssekisouData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo8, edaban);
                if (!srSpssekisouList.isEmpty()) {
                    tmpSrSpssekisou = srSpssekisouList.get(0);
                }
                
                deleteTmpSrSpssekisou(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);
                deleteTmpSubSrSpssekisou(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);
            }

            // 積層・SPS_登録処理
            insertSrSpssekisou(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList(), tmpSrSpssekisou);

            // 積層・SPS_ｻﾌﾞ画面登録処理
            insertSubSrSpssekisou(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, systemTime);

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

        // 警告メッセージの設定
        processData.setWarnMessage("修正します。よろしいですか？");

        // ユーザ認証用のパラメータをセットする。
        processData.setRquireAuth(true);
        processData.setUserAuthParam(GXHDO101B004Const.USER_AUTH_UPDATE_PARAM);

        // 後続処理メソッド設定
        processData.setMethod("doCorrect");

        return processData;
    }
        
    /**
     * 剥離/画処NG計算処理(データチェック処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData checkHakuriGNGKeisan(ProcessData processData) {

        //・剥離NG回数
        FXHDD01 itemHngKaisuu = getItemRow(processData.getItemList(), GXHDO101B004Const.HNG_KAISUU);
        //・剥離NG_AVE
        FXHDD01 itemHngAVE = getItemRow(processData.getItemList(), GXHDO101B004Const.HNG_AVE);
        //・画処NG回数
        FXHDD01 itemGngKkaisuu = getItemRow(processData.getItemList(), GXHDO101B004Const.GNG_KAISUU);
        //・画処NG_AVE
        FXHDD01 itemGngKaisuuAVE = getItemRow(processData.getItemList(), GXHDO101B004Const.GNG_KAISUUAVE);
        
        if ((!"".equals(itemHngKaisuu.getValue()) && itemHngKaisuu.getValue() != null) || (!"".equals(itemHngAVE.getValue()) && itemHngAVE.getValue() != null) || 
                 (!"".equals(itemGngKkaisuu.getValue()) && itemGngKkaisuu.getValue() != null) || (!"".equals(itemGngKaisuuAVE.getValue()) && itemGngKaisuuAVE.getValue() != null)) {

            // エラーメッセージリスト
            List<String> errorMessageList = processData.getInitMessageList();

            errorMessageList.clear();
            errorMessageList.add("・剥離NG回数");
            errorMessageList.add("・剥離NG_AVE");
            errorMessageList.add("・画処NG回数");
            errorMessageList.add("・画処NG_AVE");
            
            // エラーがある場合はエラーをセット(警告表示)
            if (!errorMessageList.isEmpty()) {
                processData.setInfoMessageList(errorMessageList);
            }

            // 後続処理メソッド設定
            processData.setMethod("doHakuriGNGKeisan");

            return processData;

        }
        
        doHakuriGNGKeisan(processData);
        
        return processData;
    }
    
    /**
     * 剥離/画処NG計算処理(実処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doHakuriGNGKeisan(ProcessData processData) {
        
        QueryRunner queryRunnerSpskadoritu = new QueryRunner(processData.getDataSourceSpskadoritu());
        QueryRunner queryRunnerTtpkadoritu = new QueryRunner(processData.getDataSourceTtpkadoritu());

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String lotNo = (String) session.getAttribute("lotNo");
        String formId = StringUtil.nullToBlank(session.getAttribute("formId"));

        // 変数SUM1
        int sumHensuu1 = 0;
        // 変数SUM2
        int sumHensuu2 = 0;
        BigDecimal valueAVE1= BigDecimal.ZERO;
        BigDecimal valueAVE2= BigDecimal.ZERO;
        // 画面.セット数
        BigDecimal setSuu = BigDecimal.ZERO;     
        Boolean gngSetFlag = false;
        FXHDD01 itemSetSuu = getItemRow(processData.getItemList(), GXHDO101B004Const.SET_SUU);
        setSuu = new BigDecimal(itemSetSuu.getValue());
        
        // 「画処NG_AVE」項目情報を取得
        FXHDD01 itemGNGKaisuuAve = getItemRow(processData.getItemList(),GXHDO101B004Const.GNG_KAISUUAVE);
        // 「画処NG_AVE」小数桁を取得
        int gngKaisuuAveDecLength = Integer.parseInt(itemGNGKaisuuAve.getInputLengthDec());

        // 「剥離NG_AVE」項目情報を取得
        FXHDD01 itemHNGKaisuuAve = getItemRow(processData.getItemList(),GXHDO101B004Const.HNG_AVE);
        // 「剥離NG_AVE」小数桁を取得
        int hngKaisuuAveDecLength = Integer.parseInt(itemHNGKaisuuAve.getInputLengthDec());

        try {
            // (19)[積層履歴ﾃﾞｰﾀ]から、ﾃﾞｰﾀを取得
            Map sekisouRirekiData = this.loadSekisouRirekiData(queryRunnerSpskadoritu, lotNo);
            if (sekisouRirekiData == null || sekisouRirekiData.isEmpty() || 
                    (sekisouRirekiData.get("HakuriErrSuu") == null && sekisouRirekiData.get("CcdErrSuu") == null) ) {
                // A.取得出来なかった場合 ｱ.Ⅲ.画面表示仕様(20)を発行する。
                // (20)[剥離NG履歴]から、ﾃﾞｰﾀを取得
                Map hakuringrirekiData = this.loadHakuringrirekiData(queryRunnerTtpkadoritu, lotNo);
                if (hakuringrirekiData == null || hakuringrirekiData.isEmpty() || 
                    (hakuringrirekiData.get("SekisouSuu") == null)) {
                    // α.取得出来なかった場合ｴﾗｰﾒｯｾｰｼﾞを表示し、以降の処理は実行しない。
                    // ・ｴﾗｰｺｰﾄﾞ:XHD-000036
                    // ・ｴﾗ-ﾒｯｾｰｼﾞ:剥離/画処NGﾃﾞｰﾀが取得出来ません。
                    FacesContext facesContext = FacesContext.getCurrentInstance();
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000036"), null);
                    facesContext.addMessage(null, message);
                    processData.setMethod("");
                    return processData;
                }else{
                    // β.取得出来た場合
                    // a.取得した積層数を合計する。この値を「変数SUM1」とする
                    // b. a ÷ 画面.セット数の計算を行う。この値を「変数AVE1」とする
                    // a.変数SUM1
                    BigDecimal sumSekisouSuu= BigDecimal.ZERO;

                    for (Object key : hakuringrirekiData.keySet()) {
                        sumSekisouSuu = new BigDecimal(String.valueOf(hakuringrirekiData.get(key)));
                    }

                    if (!setSuu.equals(BigDecimal.ZERO)){
                        valueAVE1 = sumSekisouSuu.divide(setSuu, hngKaisuuAveDecLength ,BigDecimal.ROUND_DOWN);
                    }
                    sumHensuu1 = sumSekisouSuu.intValue();
                }
            }else{
                // B.取得出来た場合
                // ｱ.取得した剥離ｴﾗｰ数を合計する。この値を「変数SUM1」とする。
                // ｲ. ｱ ÷ 画面.セット数の計算を行う。この値を「変数AVE1」とする。

                // 剥離ｴﾗｰ数
                BigDecimal sumHakuriErrSuu= BigDecimal.ZERO;
                // 画処ｴﾗｰ数
                BigDecimal sumCcdErrSuu= BigDecimal.ZERO;

                for (Object key : sekisouRirekiData.keySet()) {
                    if("HakuriErrSuu".equals(key)){
                        sumHakuriErrSuu = new BigDecimal(String.valueOf(sekisouRirekiData.get(key)));
                    }
                    if("CcdErrSuu".equals(key)){
                        sumCcdErrSuu = new BigDecimal(String.valueOf(sekisouRirekiData.get(key)));
                    }
                }
                if (!setSuu.equals(BigDecimal.ZERO)){
                    valueAVE1 = sumHakuriErrSuu.divide(setSuu, hngKaisuuAveDecLength ,BigDecimal.ROUND_DOWN);
                }
                sumHensuu1 = sumHakuriErrSuu.intValue();

                // ｳ.取得した画処ｴﾗｰ数を合計する。この値を「変数SUM2」とする。
                // ｴ. ｳ ÷ 画面.セット数の計算を行う。この値を「変数AVE2」とする。
                if (!setSuu.equals(BigDecimal.ZERO)){
                    valueAVE2 = sumCcdErrSuu.divide(setSuu, gngKaisuuAveDecLength ,BigDecimal.ROUND_DOWN);
                }
                sumHensuu2 = sumCcdErrSuu.intValue();
                gngSetFlag = true;
            }

            //・剥離NG回数
            setItemData(processData,GXHDO101B004Const.HNG_KAISUU, String.valueOf(sumHensuu1));
            //・剥離NG_AVE
            setItemData(processData,GXHDO101B004Const.HNG_AVE, String.valueOf(valueAVE1));
            if (gngSetFlag){
                //・画処NG回数
                setItemData(processData,GXHDO101B004Const.GNG_KAISUU,String.valueOf(sumHensuu2));
                //・画処NG_AVE
                setItemData(processData,GXHDO101B004Const.GNG_KAISUUAVE,String.valueOf(valueAVE2));
            }
            

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            return processData;
        }

        // 後続処理メソッド設定
        processData.setMethod("");
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

            // 積層・SPS_更新処理
            updateSrSpssekisou(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

            // 積層・SPS_ｻﾌﾞ画面更新処理
            updateSubSrSpssekisou(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo8, edaban, systemTime);

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
        processData.setUserAuthParam(GXHDO101B004Const.USER_AUTH_DELETE_PARAM);

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

            // 積層・SPS_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo8, edaban);
            insertDeleteDataTmpSrSpssekisou(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, systemTime);

            // 積層・SPS_ｻﾌﾞ画面仮登録登録処理
            insertDeleteDataTmpSubSrSpssekisou(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, systemTime);

            // 積層・SPS_削除処理
            deleteSrSpssekisou(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);

            // 積層・SPS_ｻﾌﾞ画面削除処理
            deleteSubSrSpssekisou(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);

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
                activeIdList.addAll(Arrays.asList(
                        GXHDO101B004Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO101B004Const.BTN_HAKURI_INPUT_BOTTOM,
                        GXHDO101B004Const.BTN_HAKURI_GNG_BOTTOM,
                        GXHDO101B004Const.BTN_DELETE_BOTTOM,
                        GXHDO101B004Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B004Const.BTN_START_DATETIME_BOTTOM,
                        GXHDO101B004Const.BTN_END_DATETIME_BOTTOM,
                        GXHDO101B004Const.BTN_EDABAN_COPY_TOP,
                        GXHDO101B004Const.BTN_HAKURI_INPUT_TOP,
                        GXHDO101B004Const.BTN_HAKURI_GNG_TOP,
                        GXHDO101B004Const.BTN_DELETE_TOP,
                        GXHDO101B004Const.BTN_UPDATE_TOP,
                        GXHDO101B004Const.BTN_START_DATETIME_TOP,
                        GXHDO101B004Const.BTN_END_DATETIME_TOP
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B004Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B004Const.BTN_INSERT_BOTTOM,
                        GXHDO101B004Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B004Const.BTN_INSERT_TOP));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO101B004Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B004Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO101B004Const.BTN_HAKURI_INPUT_BOTTOM,
                        GXHDO101B004Const.BTN_HAKURI_GNG_BOTTOM,
                        GXHDO101B004Const.BTN_INSERT_BOTTOM,
                        GXHDO101B004Const.BTN_START_DATETIME_BOTTOM,
                        GXHDO101B004Const.BTN_END_DATETIME_BOTTOM,
                        GXHDO101B004Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B004Const.BTN_EDABAN_COPY_TOP,
                        GXHDO101B004Const.BTN_HAKURI_INPUT_TOP,
                        GXHDO101B004Const.BTN_HAKURI_GNG_TOP,
                        GXHDO101B004Const.BTN_INSERT_TOP,
                        GXHDO101B004Const.BTN_START_DATETIME_TOP,
                        GXHDO101B004Const.BTN_END_DATETIME_TOP
                ));

                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B004Const.BTN_DELETE_BOTTOM,
                        GXHDO101B004Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B004Const.BTN_DELETE_TOP,
                        GXHDO101B004Const.BTN_UPDATE_TOP));

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
            // 剥離内容入力
            case GXHDO101B004Const.BTN_HAKURI_INPUT_TOP:
            case GXHDO101B004Const.BTN_HAKURI_INPUT_BOTTOM:
                method = "openHakuriInput";
                break;
            // 剥離/画処NG計算
            case GXHDO101B004Const.BTN_HAKURI_GNG_TOP:
            case GXHDO101B004Const.BTN_HAKURI_GNG_BOTTOM:
                method = "checkHakuriGNGKeisan";
                break;
            // 仮登録
            case GXHDO101B004Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO101B004Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO101B004Const.BTN_INSERT_TOP:
            case GXHDO101B004Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 枝番コピー
            case GXHDO101B004Const.BTN_EDABAN_COPY_TOP:
            case GXHDO101B004Const.BTN_EDABAN_COPY_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 修正
            case GXHDO101B004Const.BTN_UPDATE_TOP:
            case GXHDO101B004Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO101B004Const.BTN_DELETE_TOP:
            case GXHDO101B004Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 開始日時
            case GXHDO101B004Const.BTN_START_DATETIME_TOP:
            case GXHDO101B004Const.BTN_START_DATETIME_BOTTOM:
                method = "setKaishiDateTime";
                break;
            // 終了日時
            case GXHDO101B004Const.BTN_END_DATETIME_TOP:
            case GXHDO101B004Const.BTN_END_DATETIME_BOTTOM:
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
        
        //上ｶﾊﾞｰﾃｰﾌﾟ
        String ueCoverTape1DataValue = "";
        //下ｶﾊﾞｰﾃｰﾌﾟ
        String shitaCoverTape1DataValue = "";
        
        //上ｶﾊﾞｰﾃｰﾌﾟ1対象項目の決定
        List<String> checkListDataVal = checkYoutoItems(processData,sekkeiData, getMapYoutoAssociation(),"CT",getMapSekkeiYotoAssociation());
        for(int i=0; i<=checkListDataVal.size()-1; i++){
            if(i > 0){
                if("ERROR".equals(checkListDataVal.get(0))){
                    errorMessageList.add(checkListDataVal.get(i));
                }else if("OK".equals(checkListDataVal.get(0))){
                    ueCoverTape1DataValue = checkListDataVal.get(i);
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
                    shitaCoverTape1DataValue = checkListDataVal.get(i);
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
        if (!setInputItemData(processData, queryRunnerDoc, queryRunnerQcdb, lotNo, formId)) {
            // エラー発生時は処理を中断
            processData.setFatalError(true);
            processData.setInitMessageList(Arrays.asList(MessageUtil.getMessage("XHD-000038")));
            return processData;
        }

        // 画面に取得した情報をセットする。(入力項目以外)
        setViewItemData(processData, sekkeiData, lotKbnMasData, ownerMasData, daPatternMasData, shikakariData, lotNo,
                ueCoverTape1DataValue, shitaCoverTape1DataValue);

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
     * @param ueCoverTape1 上カバーテープ１
     * @param shitaCoverTape1DataValue 下カバーテープ１
     */
    private void setViewItemData(ProcessData processData, Map sekkeiData, Map lotKbnMasData, Map ownerMasData, Map daPatternMasData, Map shikakariData, 
            String lotNo, String ueCoverTape1, String shitaCoverTape1) {

        // ロットNo
        this.setItemData(processData, GXHDO101B004Const.LOTNO, lotNo);
        // KCPNO
        this.setItemData(processData, GXHDO101B004Const.KCPNO, StringUtil.nullToBlank(getMapData(shikakariData, "kcpno")));
        // セット数
        String suuryo = StringUtil.nullToBlank(getMapData(shikakariData, "suuryo"));
        String torikosuu = StringUtil.nullToBlank(getMapData(shikakariData, "torikosuu"));
        if (StringUtil.isEmpty(suuryo) || "0".equals(suuryo) || StringUtil.isEmpty(torikosuu) || "0".equals(torikosuu)) {
            this.setItemData(processData, GXHDO101B004Const.SET_SUU, "0");
        } else {
            BigDecimal decHasseisu = new BigDecimal(suuryo);
            BigDecimal decTorikosuu = new BigDecimal(torikosuu);
            BigDecimal setsu = decHasseisu.divide(decTorikosuu, 0, RoundingMode.DOWN);
            this.setItemData(processData, GXHDO101B004Const.SET_SUU, setsu.toPlainString());
        }

        // 客先
        this.setItemData(processData, GXHDO101B004Const.KYAKUSAKI, StringUtil.nullToBlank(getMapData(shikakariData, "tokuisaki")));

        // ロット区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); //ﾛｯﾄ区分ｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B004Const.LOT_KUBUN, "");
        } else {
            String lotKubun = StringUtil.nullToBlank(getMapData(lotKbnMasData, "lotkubun"));
            this.setItemData(processData, GXHDO101B004Const.LOT_KUBUN, lotkubuncode + ":" + lotKubun);
        }

        // オーナー
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));// ｵｰﾅｰｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B004Const.OWNER, "");
        } else {
            String owner = StringUtil.nullToBlank(getMapData(ownerMasData, "ownername"));
            this.setItemData(processData, GXHDO101B004Const.OWNER, ownercode + ":" + owner);
        }

        // 電極テープ
        this.setItemData(processData, GXHDO101B004Const.DENKYOKU_TAPE, StringUtil.nullToBlank(sekkeiData.get("GENRYOU"))
                + "  " + StringUtil.nullToBlank(sekkeiData.get("ETAPE")));

        // 積層数
        this.setItemData(processData, GXHDO101B004Const.SEKISOU_SU, StringUtil.nullToBlank(sekkeiData.get("EATUMI"))
                + "μm×"
                + StringUtil.nullToBlank(sekkeiData.get("SOUSUU"))
                + "層  "
                + StringUtil.nullToBlank(sekkeiData.get("EMAISUU"))
                + "枚");

        // 上カバーテープ１
        this.setItemData(processData, GXHDO101B004Const.UE_COVER_TAPE1, ueCoverTape1);

        // 下カバーテープ１
        this.setItemData(processData, GXHDO101B004Const.SHITA_COVER_TAPE1, shitaCoverTape1);

        // 列 × 行
        String lRetsu = StringUtil.nullToBlank(getMapData(daPatternMasData, "LRETU")); //列
        String wRetsu = StringUtil.nullToBlank(getMapData(daPatternMasData, "WRETU")); //行
        this.setItemData(processData, GXHDO101B004Const.RETSU_GYOU, lRetsu + "×" + wRetsu);

        // ピッチ
        String lSun = StringUtil.nullToBlank(getMapData(daPatternMasData, "LSUN")); //LSUN
        String wSun = StringUtil.nullToBlank(getMapData(daPatternMasData, "WSUN")); //WSUN
        this.setItemData(processData, GXHDO101B004Const.PITCH, lSun + "×" + wSun);

        // 電極ペースト
        this.setItemData(processData, GXHDO101B004Const.DENKYOKU_PASTE, "");
        
        // 積層スライド量
        this.setItemData(processData, GXHDO101B004Const.SEKISOU_SLIDE_RYOU, "");

    }

    /**
     * 入力項目のデータを画面項目に設定
     *
     * @param processData 処理制御データ
     * @param queryRunnerDoc QueryRunnerオブジェクト(DocServer)
     * @param queryRunnerQcdb QueryRunnerオブジェクト(Qcdb)
     * @param lotNo ﾛｯﾄNo
     * @param formId 画面ID
     * @return 設定結果(失敗時false)
     * @throws SQLException 例外エラー
     */
    private boolean setInputItemData(ProcessData processData, QueryRunner queryRunnerDoc, QueryRunner queryRunnerQcdb,
            String lotNo, String formId) throws SQLException {

        List<SrSpssekisou> srSpssekisouDataList = new ArrayList<>();
        List<SubSrSpssekisou> subSrSpssekisouDataList = new ArrayList<>();
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
                setInputItemDataSubFormC006(null);

                return true;
            }

            // 積層・SPSデータ取得
            srSpssekisouDataList = getSrSpssekisouData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban);
            if (srSpssekisouDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // 積層・SPS_ｻﾌﾞ画面データ取得
            subSrSpssekisouDataList = getSubSrSpssekisouData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban);
            if (subSrSpssekisouDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srSpssekisouDataList.isEmpty() || subSrSpssekisouDataList.isEmpty()) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srSpssekisouDataList.get(0));

        // 膜厚入力画面データ設定
        setInputItemDataSubFormC006(subSrSpssekisouDataList.get(0));

        return true;

    }

    /**
     * メイン画面データ設定処理
     *
     * @param processData 処理制御データ
     * @param srSpssekisouData 積層SPSデータ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrSpssekisou srSpssekisouData) {
        // ｽﾘｯﾌﾟﾛｯﾄNo
        this.setItemData(processData, GXHDO101B004Const.SLIP_LOTNO, getSrSpssekisouItemData(GXHDO101B004Const.SLIP_LOTNO, srSpssekisouData));
        // ﾛｰﾙNo1
        this.setItemData(processData, GXHDO101B004Const.ROLL_NO1, getSrSpssekisouItemData(GXHDO101B004Const.ROLL_NO1, srSpssekisouData));
        // ﾛｰﾙNo2
        this.setItemData(processData, GXHDO101B004Const.ROLL_NO2, getSrSpssekisouItemData(GXHDO101B004Const.ROLL_NO2, srSpssekisouData));
        // ﾛｰﾙNo3
        this.setItemData(processData, GXHDO101B004Const.ROLL_NO3, getSrSpssekisouItemData(GXHDO101B004Const.ROLL_NO3, srSpssekisouData));
        // 原料記号
        this.setItemData(processData, GXHDO101B004Const.GENRYO_KIGOU, getSrSpssekisouItemData(GXHDO101B004Const.GENRYO_KIGOU, srSpssekisouData));
        // ＰＥＴフィルム種類
        this.setItemData(processData, GXHDO101B004Const.PET_FILM_SHURUI, getSrSpssekisouItemData(GXHDO101B004Const.PET_FILM_SHURUI, srSpssekisouData));
        // 固着シート
        this.setItemData(processData, GXHDO101B004Const.KOTYAKU_SHEET, getSrSpssekisouItemData(GXHDO101B004Const.KOTYAKU_SHEET, srSpssekisouData));
        // 下端子号機
        this.setItemData(processData, GXHDO101B004Const.SHITATANSHI_GOUKI, getSrSpssekisouItemData(GXHDO101B004Const.SHITATANSHI_GOUKI, srSpssekisouData));
        // 上端子号機
        this.setItemData(processData, GXHDO101B004Const.UWATANSHI_GOUKI, getSrSpssekisouItemData(GXHDO101B004Const.UWATANSHI_GOUKI, srSpssekisouData));
        // 下端子ﾌﾞｸ抜き
        this.setItemData(processData, GXHDO101B004Const.SHITA_TANSHI_BUKUNUKI, getSrSpssekisouItemData(GXHDO101B004Const.SHITA_TANSHI_BUKUNUKI, srSpssekisouData));
        // 積層号機
        this.setItemData(processData, GXHDO101B004Const.SEKISO_GOKI, getSrSpssekisouItemData(GXHDO101B004Const.SEKISO_GOKI, srSpssekisouData));
        // 下端子
        this.setItemData(processData, GXHDO101B004Const.SHITA_TANSHI, getSrSpssekisouItemData(GXHDO101B004Const.SHITA_TANSHI, srSpssekisouData));
        // 剥離吸引圧
        this.setItemData(processData, GXHDO101B004Const.HAKURI_KYUIN, getSrSpssekisouItemData(GXHDO101B004Const.HAKURI_KYUIN, srSpssekisouData));
        // 剥離速度
        this.setItemData(processData, GXHDO101B004Const.HAKURI_SPEED, getSrSpssekisouItemData(GXHDO101B004Const.HAKURI_SPEED, srSpssekisouData));
        // 剥離クリアランス
        this.setItemData(processData, GXHDO101B004Const.HAKURI_CLEARRANCE, getSrSpssekisouItemData(GXHDO101B004Const.HAKURI_CLEARRANCE, srSpssekisouData));
        // 剥離カット速度
        this.setItemData(processData, GXHDO101B004Const.HAKURI_CUT_SPEED, getSrSpssekisouItemData(GXHDO101B004Const.HAKURI_CUT_SPEED, srSpssekisouData));
        // 下パンチ温度
        this.setItemData(processData, GXHDO101B004Const.SHITA_PANCHI_ONDO, getSrSpssekisouItemData(GXHDO101B004Const.SHITA_PANCHI_ONDO, srSpssekisouData));
        // 上パンチ温度
        this.setItemData(processData, GXHDO101B004Const.UWA_PANCHI_ONDO, getSrSpssekisouItemData(GXHDO101B004Const.UWA_PANCHI_ONDO, srSpssekisouData));
        // 加圧時間
        this.setItemData(processData, GXHDO101B004Const.KAATU_JIKAN, getSrSpssekisouItemData(GXHDO101B004Const.KAATU_JIKAN, srSpssekisouData));
        // 加圧圧力
        this.setItemData(processData, GXHDO101B004Const.KAATU_ATURYOKU, getSrSpssekisouItemData(GXHDO101B004Const.KAATU_ATURYOKU, srSpssekisouData));
        // 最終加圧力
        this.setItemData(processData, GXHDO101B004Const.LAST_KAATURYOKU, getSrSpssekisouItemData(GXHDO101B004Const.LAST_KAATURYOKU, srSpssekisouData));
        // 最終加圧時間
        this.setItemData(processData, GXHDO101B004Const.LAST_KAATUJIKAN, getSrSpssekisouItemData(GXHDO101B004Const.LAST_KAATUJIKAN, srSpssekisouData));
        //空打ち
        this.setItemData(processData, GXHDO101B004Const.KARAUTI, getSrSpssekisouItemData(GXHDO101B004Const.KARAUTI, srSpssekisouData));
        // 空打ち[秒]
        this.setItemData(processData, GXHDO101B004Const.KARAUTIBYOU, getSrSpssekisouItemData(GXHDO101B004Const.KARAUTIBYOU, srSpssekisouData));
        // 空打ち[回]
        this.setItemData(processData, GXHDO101B004Const.KARAUTIKAI, getSrSpssekisouItemData(GXHDO101B004Const.KARAUTIKAI, srSpssekisouData));
        // 上端子
        this.setItemData(processData, GXHDO101B004Const.UWA_TANSHI, getSrSpssekisouItemData(GXHDO101B004Const.UWA_TANSHI, srSpssekisouData));
        // ｽﾞﾚ値
        this.setItemData(processData, GXHDO101B004Const.ZURETI, getSrSpssekisouItemData(GXHDO101B004Const.ZURETI, srSpssekisouData));
        // ﾜﾚ、ｳｷ、かみ込みなき事
        this.setItemData(processData, GXHDO101B004Const.GAIKAN_KAKUNIN5, getSrSpssekisouItemData(GXHDO101B004Const.GAIKAN_KAKUNIN5, srSpssekisouData));
        // 処理セット数
        this.setItemData(processData, GXHDO101B004Const.SYORI_SETSUU, getSrSpssekisouItemData(GXHDO101B004Const.SYORI_SETSUU, srSpssekisouData));
        // 良品セット数
        this.setItemData(processData, GXHDO101B004Const.RYOUHIN_SETSUU, getSrSpssekisouItemData(GXHDO101B004Const.RYOUHIN_SETSUU, srSpssekisouData));
        // 開始日
        this.setItemData(processData, GXHDO101B004Const.KAISHI_DAY, getSrSpssekisouItemData(GXHDO101B004Const.KAISHI_DAY, srSpssekisouData));
        // 開始時刻
        this.setItemData(processData, GXHDO101B004Const.KAISHI_TIME, getSrSpssekisouItemData(GXHDO101B004Const.KAISHI_TIME, srSpssekisouData));
        // 開始担当者
        this.setItemData(processData, GXHDO101B004Const.KAISHI_TANTOSYA, getSrSpssekisouItemData(GXHDO101B004Const.KAISHI_TANTOSYA, srSpssekisouData));
        // 開始確認者
        this.setItemData(processData, GXHDO101B004Const.KAISHI_KAKUNINSYA, getSrSpssekisouItemData(GXHDO101B004Const.KAISHI_KAKUNINSYA, srSpssekisouData));
        // 終了日
        this.setItemData(processData, GXHDO101B004Const.SHURYO_DAY, getSrSpssekisouItemData(GXHDO101B004Const.SHURYO_DAY, srSpssekisouData));
        // 終了時刻
        this.setItemData(processData, GXHDO101B004Const.SHURYO_TIME, getSrSpssekisouItemData(GXHDO101B004Const.SHURYO_TIME, srSpssekisouData));
        // 終了担当者
        this.setItemData(processData, GXHDO101B004Const.SHURYO_TANTOSYA, getSrSpssekisouItemData(GXHDO101B004Const.SHURYO_TANTOSYA, srSpssekisouData));
        // 端子テープ種類
        this.setItemData(processData, GXHDO101B004Const.TTAPE_SYURUI, getSrSpssekisouItemData(GXHDO101B004Const.TTAPE_SYURUI, srSpssekisouData));
        // 剥離NG回数
        this.setItemData(processData, GXHDO101B004Const.HNG_KAISUU, getSrSpssekisouItemData(GXHDO101B004Const.HNG_KAISUU, srSpssekisouData));
        // 剥離NG_AVE
        this.setItemData(processData, GXHDO101B004Const.HNG_AVE, getSrSpssekisouItemData(GXHDO101B004Const.HNG_AVE, srSpssekisouData));
        // 画処NG回数
        this.setItemData(processData, GXHDO101B004Const.GNG_KAISUU, getSrSpssekisouItemData(GXHDO101B004Const.GNG_KAISUU, srSpssekisouData));
        // 画処NG_AVE
        this.setItemData(processData, GXHDO101B004Const.GNG_KAISUUAVE, getSrSpssekisouItemData(GXHDO101B004Const.GNG_KAISUUAVE, srSpssekisouData));
        // 備考1
        this.setItemData(processData, GXHDO101B004Const.BIKOU1, getSrSpssekisouItemData(GXHDO101B004Const.BIKOU1, srSpssekisouData));
        // 備考2
        this.setItemData(processData, GXHDO101B004Const.BIKOU2, getSrSpssekisouItemData(GXHDO101B004Const.BIKOU2, srSpssekisouData));

    }

    /**
     * 剥離内容入力画面データ設定処理
     *
     * @param subSrSpssekisouData 積層・SPS_ｻﾌﾞ画面データ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     */
private void setInputItemDataSubFormC006(SubSrSpssekisou subSrSpssekisouData) {

        // サブ画面の情報を取得
        GXHDO101C006 beanGXHDO101C006 = (GXHDO101C006) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C006);

        //データの設定
        String[] setsuu;
        String[] bikou;

        GXHDO101C006Model model;
        if (subSrSpssekisouData == null) {
            // 登録データが無い場合空の状態で初期値をセットする。
            setsuu = new String[]{"", "", "", "", "", "", "", "", "", ""
                                 ,"", "", "", "", "", "", "", "", "", ""
                                 ,"", "", "", "", "", "", "", "", "", ""
                                 ,"", "", "", "", "", "", "", "", "", ""}; //ｾｯﾄ数入力1～40
            bikou = new String[]{"", "", "", "", "", "", "", "", "", ""
                                 ,"", "", "", "", "", "", "", "", "", ""
                                 ,"", "", "", "", "", "", "", "", "", ""
                                 ,"", "", "", "", "", "", "", "", "", ""}; //備考1～40
            model = GXHDO101C006Logic.createGXHDO101C006Model(setsuu, bikou);

        } else {
            // 登録データがあれば登録データをセットする。
            //ｾｯﾄ数入力1～40
            setsuu = new String[]{
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu1()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu2()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu3()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu4()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu5()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu6()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu7()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu8()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu9()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu10()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu11()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu12()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu13()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu14()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu15()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu16()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu17()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu18()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu19()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu20()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu21()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu22()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu23()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu24()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu25()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu26()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu27()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu28()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu29()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu30()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu31()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu32()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu33()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu34()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu35()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu36()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu37()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu38()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu39()),
                StringUtil.nullToBlank(subSrSpssekisouData.getSetsuu40())};
            //備考1～40
            bikou = new String[]{
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou1()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou2()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou3()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou4()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou5()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou6()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou7()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou8()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou9()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou10()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou11()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou12()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou13()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou14()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou15()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou16()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou17()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou18()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou19()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou20()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou21()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou22()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou23()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou24()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou25()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou26()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou27()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou28()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou29()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou30()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou31()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou32()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou33()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou34()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou35()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou36()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou37()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou38()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou39()),
                StringUtil.nullToBlank(subSrSpssekisouData.getBikou40())};
            model = GXHDO101C006Logic.createGXHDO101C006Model(setsuu, bikou);
        }

        beanGXHDO101C006.setGxhdO101c006Model(model);
    }

    /**
     * 積層・SPSの入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return 積層・SPS登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrSpssekisou> getSrSpssekisouData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrSpssekisou(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSrSpssekisou(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        }
    }

    /**
     * 積層・SPS_ｻﾌﾞ画面の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @return 積層・SPS_ｻﾌﾞ画面データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrSpssekisou> getSubSrSpssekisouData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {
        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSubSrSpssekisou(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSubSrSpssekisou(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        }
    }

    /**
     * [積層履歴ﾃﾞｰﾀ]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerSpskadoritu QueryRunnerオブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadSekisouRirekiData(QueryRunner queryRunnerSpskadoritu, String lotNo) throws SQLException {
        // 積層履歴ﾃﾞｰﾀの取得
        String sql = "SELECT sum(HakuriErrSuu) AS HakuriErrSuu,sum(CcdErrSuu) AS CcdErrSuu "
                + "FROM sekisourirekidata "
                + "WHERE LotNo = ? ";

        List<Object> params = new ArrayList<>();
        params.add(lotNo);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerSpskadoritu.query(sql, new MapHandler(), params.toArray());
    }
    
    /**
     * [剥離NG履歴]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerTtpkadoritu QueryRunnerオブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadHakuringrirekiData(QueryRunner queryRunnerTtpkadoritu, String lotNo) throws SQLException {
        // 剥離NG履歴ﾃﾞｰﾀの取得
        String sql = "SELECT sum(SekisouSuu) AS SekisouSuu "
                + "FROM hakuringrireki "
                + "WHERE LotNo = ? ";

        List<Object> params = new ArrayList<>();
        params.add(lotNo);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerTtpkadoritu.query(sql, new MapHandler(), params.toArray());
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
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
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
     * [積層SPS]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrSpssekisou> loadSrSpssekisou(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT kojyo,lotno,edaban,tntapesyurui,tntapeno,tntapegenryou,gouki"
                + ",startdatetime,enddatetime,sekisouzure,tantousya,kakuninsya"
                + ",sekisouzure2,bikou1,bikou2,bikou3,bikou4,bikou5,KCPNO,GoukiCode"
                + ",TpLot,HakuriSpeed,KanaOndo,DAturyoku,DKaatuJikan,CPressAturyoku"
                + ",CPressKaatuJikan,CPressKankakuSosuu,LastKaaturyoku,LastKaatuJikan"
                + ",FourSplitTantou,STaoreryo1,STaoreryo2,STaoreryo3,STaoreryo4"
                + ",STsunAve,STsunMax,STsunMin,STsunSiguma,HNGKaisuu,GaikanTantou"
                + ",CPressKaisuu,HNGKaisuuAve,GNGKaisuu,GNGKaisuuAve,tapelotno"
                + ",taperollno1,taperollno2,taperollno3,genryoukigou,petfilmsyurui"
                + ",Kotyakugouki,Kotyakusheet,ShitaTanshigouki,UwaTanshigouki"
                + ",ShitaTanshiBukunuki,ShitaTanshi,HakuriKyuin,HakuriClearrance"
                + ",HakuriCutSpeed,ShitaPanchiOndo,UwaPanchiOndo,KaatuJikan,KaatuAturyoku"
                + ",UwaTanshi,GaikanKakunin1,GaikanKakunin2,GaikanKakunin3,GaikanKakunin4"
                + ",SyoriSetsuu,RyouhinSetsuu,StartTantosyacode,EndTantousyacode"
                + ",TanshiTapeSyurui,torokunichiji,kosinnichiji,karauti,karautibyou"
                + ",karautikai,zureti,GaikanKakunin5,revision,'0' AS deleteflag "
                + "FROM sr_spssekisou "
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
        mapping.put("lotno", "lotno"); // ﾛｯﾄNo
        mapping.put("edaban", "edaban"); // 枝番
        mapping.put("tntapesyurui", "tntapesyurui"); // 端子ﾃｰﾌﾟ種類
        mapping.put("tntapeno", "tntapeno"); // 端子ﾃｰﾌﾟNo
        mapping.put("tntapegenryou", "tntapegenryou"); // 端子ﾃｰﾌﾟ原料
        mapping.put("gouki", "gouki"); // 号機ｺｰﾄﾞ
        mapping.put("startdatetime", "startdatetime"); // 開始日時
        mapping.put("enddatetime", "enddatetime"); // 終了日時
        mapping.put("sekisouzure", "sekisouzure"); // 積層ｽﾞﾚ
        mapping.put("tantousya", "tantousya"); // 担当者ｺｰﾄﾞ
        mapping.put("kakuninsya", "kakuninsya"); // 確認者ｺｰﾄﾞ
        mapping.put("sekisouzure2", "sekisouzure2"); // 積層ｽﾞﾚ2
        mapping.put("bikou1", "bikou1"); // 備考1
        mapping.put("bikou2", "bikou2"); // 備考2
        mapping.put("bikou3", "bikou3"); // 備考3
        mapping.put("bikou4", "bikou4"); // 備考4
        mapping.put("bikou5", "bikou5"); // 備考5
        mapping.put("KCPNO", "kcpno"); // KCPNO
        mapping.put("GoukiCode", "goukiCode"); // 号機ｺｰﾄﾞ
        mapping.put("TpLot", "tpLot"); // ﾃｰﾌﾟLot
        mapping.put("HakuriSpeed", "hakuriSpeed"); // 剥離ｽﾋﾟｰﾄﾞ
        mapping.put("KanaOndo", "kanaOndo"); // 金型温度
        mapping.put("DAturyoku", "daturyoku"); // 電極圧力
        mapping.put("DKaatuJikan", "dkaatuJikan"); // 電極加圧時間
        mapping.put("CPressAturyoku", "cpressAturyoku"); // 中間ﾌﾟﾚｽ圧力
        mapping.put("CPressKaatuJikan", "cpressKaatuJikan"); // 中間ﾌﾟﾚｽ加圧時間
        mapping.put("CPressKankakuSosuu", "cpressKankakuSosuu"); // 中間ﾌﾟﾚｽ間隔総数
        mapping.put("LastKaaturyoku", "lastKaaturyoku"); // 最終加圧力
        mapping.put("LastKaatuJikan", "lastKaatuJikan"); // 最終加圧時間
        mapping.put("FourSplitTantou", "fourSplitTantou"); // 4分割担当者ｺｰﾄﾞ
        mapping.put("STaoreryo1", "staoreryo1"); // 積層ﾀｵﾚ量1
        mapping.put("STaoreryo2", "staoreryo2"); // 積層ﾀｵﾚ量2
        mapping.put("STaoreryo3", "staoreryo3"); // 積層ﾀｵﾚ量3
        mapping.put("STaoreryo4", "staoreryo4"); // 積層ﾀｵﾚ量4
        mapping.put("STsunAve", "stsunAve"); // 積層後T寸法AVE
        mapping.put("STsunMax", "stsunMax"); // 積層後T寸法MAX
        mapping.put("STsunMin", "stsunMin"); // 積層後T寸法MIN
        mapping.put("STsunSiguma", "stsunSiguma"); // 積層後T寸法σ
        mapping.put("HNGKaisuu", "hngKaisuu"); // 隔離NG回数
        mapping.put("GaikanTantou", "gaikanTantou"); // 最終外観担当者ｺｰﾄﾞ
        mapping.put("CPressKaisuu", "cpressKaisuu"); // 中間ﾌﾟﾚｽ回数
        mapping.put("HNGKaisuuAve", "hngKaisuuAve"); // 剥離NG回数AVE
        mapping.put("GNGKaisuu", "gngKaisuu"); // 画像NG回数
        mapping.put("GNGKaisuuAve", "gngKaisuuAve"); // 画像NG回数AVE
        mapping.put("tapelotno", "tapelotno"); // ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
        mapping.put("taperollno1", "taperollno1"); // ﾃｰﾌﾟﾛｰﾙNo1
        mapping.put("taperollno2", "taperollno2"); // ﾃｰﾌﾟﾛｰﾙNo2
        mapping.put("taperollno3", "taperollno3"); // ﾃｰﾌﾟﾛｰﾙNo3
        mapping.put("genryoukigou", "genryoukigou"); // 原料記号
        mapping.put("petfilmsyurui", "petfilmsyurui"); // PETﾌｨﾙﾑ種類
        mapping.put("Kotyakugouki", "kotyakugouki"); // 固着ｼｰﾄ張付け機
        mapping.put("Kotyakusheet", "kotyakusheet"); // 固着ｼｰﾄ
        mapping.put("ShitaTanshigouki", "shitaTanshigouki"); // 下端子号機
        mapping.put("UwaTanshigouki", "uwaTanshigouki"); // 上端子号機
        mapping.put("ShitaTanshiBukunuki", "shitaTanshiBukunuki"); // 下端子ﾌﾞｸ抜き
        mapping.put("ShitaTanshi", "shitaTanshi"); // 下端子
        mapping.put("HakuriKyuin", "hakuriKyuin"); // 剥離吸引圧
        mapping.put("HakuriClearrance", "hakuriClearrance"); // 剥離ｸﾘｱﾗﾝｽ
        mapping.put("HakuriCutSpeed", "hakuriCutSpeed"); // 剥離ｶｯﾄ速度
        mapping.put("ShitaPanchiOndo", "shitaPanchiOndo"); // 下ﾊﾟﾝﾁ温度
        mapping.put("UwaPanchiOndo", "uwaPanchiOndo"); // 上ﾊﾟﾝﾁ温度
        mapping.put("KaatuJikan", "kaatuJikan"); // 加圧時間
        mapping.put("KaatuAturyoku", "kaatuAturyoku"); // 加圧圧力
        mapping.put("UwaTanshi", "uwaTanshi"); // 上端子
        mapping.put("GaikanKakunin1", "gaikanKakunin1"); // 外観確認1
        mapping.put("GaikanKakunin2", "gaikanKakunin2"); // 外観確認2
        mapping.put("GaikanKakunin3", "gaikanKakunin3"); // 外観確認3
        mapping.put("GaikanKakunin4", "gaikanKakunin4"); // 外観確認4
        mapping.put("SyoriSetsuu", "syoriSetsuu"); // 処理ｾｯﾄ数
        mapping.put("RyouhinSetsuu", "ryouhinSetsuu"); // 良品ｾｯﾄ数
        mapping.put("StartTantosyacode", "startTantosyacode"); // 開始担当者ｺｰﾄﾞ
        mapping.put("EndTantousyacode", "endTantousyacode"); // 終了担当者ｺｰﾄﾞ
        mapping.put("TanshiTapeSyurui", "tanshiTapeSyurui"); // 端子ﾃｰﾌﾟ種類
        mapping.put("torokunichiji", "torokunichiji"); // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); // 更新日時
        mapping.put("karauti", "karauti"); // 空打ち
        mapping.put("karautibyou", "karautibyou"); // 空打ち秒
        mapping.put("karautikai", "karautikai"); // 空打ち回
        mapping.put("zureti", "zureti"); // ｽﾞﾚ値
        mapping.put("GaikanKakunin5", "gaikanKakunin5"); // 外観確認5
        mapping.put("revision", "revision"); // revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrSpssekisou>> beanHandler = new BeanListHandler<>(SrSpssekisou.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [積層・SPS_ｻﾌﾞ画面]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrSpssekisou> loadSubSrSpssekisou(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT kojyo,lotno,edaban,setsuu1,setsuu2,setsuu3,setsuu4,setsuu5,setsuu6"
                + ",setsuu7,setsuu8,setsuu9,setsuu10,setsuu11,setsuu12,setsuu13,setsuu14"
                + ",setsuu15,setsuu16,setsuu17,setsuu18,setsuu19,setsuu20,setsuu21,setsuu22"
                + ",setsuu23,setsuu24,setsuu25,setsuu26,setsuu27,setsuu28,setsuu29,setsuu30"
                + ",setsuu31,setsuu32,setsuu33,setsuu34,setsuu35,setsuu36,setsuu37,setsuu38"
                + ",setsuu39,setsuu40,bikou1,bikou2,bikou3,bikou4,bikou5,bikou6,bikou7,bikou8"
                + ",bikou9,bikou10,bikou11,bikou12,bikou13,bikou14,bikou15,bikou16,bikou17"
                + ",bikou18,bikou19,bikou20,bikou21,bikou22,bikou23,bikou24,bikou25,bikou26"
                + ",bikou27,bikou28,bikou29,bikou30,bikou31,bikou32,bikou33,bikou34,bikou35"
                + ",bikou36,bikou37,bikou38,bikou39,bikou40,torokunichiji,kosinnichiji"
                + ",revision,'0' AS deleteflag "
                + "FROM sub_sr_spssekisou "
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
        mapping.put("kojyo", "kojyo"); // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno"); // ﾛｯﾄNo
        mapping.put("edaban", "edaban"); // 枝番
        mapping.put("setsuu1", "setsuu1"); // ｾｯﾄ数1
        mapping.put("setsuu2", "setsuu2"); // ｾｯﾄ数2
        mapping.put("setsuu3", "setsuu3"); // ｾｯﾄ数3
        mapping.put("setsuu4", "setsuu4"); // ｾｯﾄ数4
        mapping.put("setsuu5", "setsuu5"); // ｾｯﾄ数5
        mapping.put("setsuu6", "setsuu6"); // ｾｯﾄ数6
        mapping.put("setsuu7", "setsuu7"); // ｾｯﾄ数7
        mapping.put("setsuu8", "setsuu8"); // ｾｯﾄ数8
        mapping.put("setsuu9", "setsuu9"); // ｾｯﾄ数9
        mapping.put("setsuu10", "setsuu10"); // ｾｯﾄ数10
        mapping.put("setsuu11", "setsuu11"); // ｾｯﾄ数11
        mapping.put("setsuu12", "setsuu12"); // ｾｯﾄ数12
        mapping.put("setsuu13", "setsuu13"); // ｾｯﾄ数13
        mapping.put("setsuu14", "setsuu14"); // ｾｯﾄ数14
        mapping.put("setsuu15", "setsuu15"); // ｾｯﾄ数15
        mapping.put("setsuu16", "setsuu16"); // ｾｯﾄ数16
        mapping.put("setsuu17", "setsuu17"); // ｾｯﾄ数17
        mapping.put("setsuu18", "setsuu18"); // ｾｯﾄ数18
        mapping.put("setsuu19", "setsuu19"); // ｾｯﾄ数19
        mapping.put("setsuu20", "setsuu20"); // ｾｯﾄ数20
        mapping.put("setsuu21", "setsuu21"); // ｾｯﾄ数21
        mapping.put("setsuu22", "setsuu22"); // ｾｯﾄ数22
        mapping.put("setsuu23", "setsuu23"); // ｾｯﾄ数23
        mapping.put("setsuu24", "setsuu24"); // ｾｯﾄ数24
        mapping.put("setsuu25", "setsuu25"); // ｾｯﾄ数25
        mapping.put("setsuu26", "setsuu26"); // ｾｯﾄ数26
        mapping.put("setsuu27", "setsuu27"); // ｾｯﾄ数27
        mapping.put("setsuu28", "setsuu28"); // ｾｯﾄ数28
        mapping.put("setsuu29", "setsuu29"); // ｾｯﾄ数29
        mapping.put("setsuu30", "setsuu30"); // ｾｯﾄ数30
        mapping.put("setsuu31", "setsuu31"); // ｾｯﾄ数31
        mapping.put("setsuu32", "setsuu32"); // ｾｯﾄ数32
        mapping.put("setsuu33", "setsuu33"); // ｾｯﾄ数33
        mapping.put("setsuu34", "setsuu34"); // ｾｯﾄ数34
        mapping.put("setsuu35", "setsuu35"); // ｾｯﾄ数35
        mapping.put("setsuu36", "setsuu36"); // ｾｯﾄ数36
        mapping.put("setsuu37", "setsuu37"); // ｾｯﾄ数37
        mapping.put("setsuu38", "setsuu38"); // ｾｯﾄ数38
        mapping.put("setsuu39", "setsuu39"); // ｾｯﾄ数39
        mapping.put("setsuu40", "setsuu40"); // ｾｯﾄ数40
        mapping.put("bikou1", "bikou1"); // 備考1
        mapping.put("bikou2", "bikou2"); // 備考2
        mapping.put("bikou3", "bikou3"); // 備考3
        mapping.put("bikou4", "bikou4"); // 備考4
        mapping.put("bikou5", "bikou5"); // 備考5
        mapping.put("bikou6", "bikou6"); // 備考6
        mapping.put("bikou7", "bikou7"); // 備考7
        mapping.put("bikou8", "bikou8"); // 備考8
        mapping.put("bikou9", "bikou9"); // 備考9
        mapping.put("bikou10", "bikou10"); // 備考10
        mapping.put("bikou11", "bikou11"); // 備考11
        mapping.put("bikou12", "bikou12"); // 備考12
        mapping.put("bikou13", "bikou13"); // 備考13
        mapping.put("bikou14", "bikou14"); // 備考14
        mapping.put("bikou15", "bikou15"); // 備考15
        mapping.put("bikou16", "bikou16"); // 備考16
        mapping.put("bikou17", "bikou17"); // 備考17
        mapping.put("bikou18", "bikou18"); // 備考18
        mapping.put("bikou19", "bikou19"); // 備考19
        mapping.put("bikou20", "bikou20"); // 備考20
        mapping.put("bikou21", "bikou21"); // 備考21
        mapping.put("bikou22", "bikou22"); // 備考22
        mapping.put("bikou23", "bikou23"); // 備考23
        mapping.put("bikou24", "bikou24"); // 備考24
        mapping.put("bikou25", "bikou25"); // 備考25
        mapping.put("bikou26", "bikou26"); // 備考26
        mapping.put("bikou27", "bikou27"); // 備考27
        mapping.put("bikou28", "bikou28"); // 備考28
        mapping.put("bikou29", "bikou29"); // 備考29
        mapping.put("bikou30", "bikou30"); // 備考30
        mapping.put("bikou31", "bikou31"); // 備考31
        mapping.put("bikou32", "bikou32"); // 備考32
        mapping.put("bikou33", "bikou33"); // 備考33
        mapping.put("bikou34", "bikou34"); // 備考34
        mapping.put("bikou35", "bikou35"); // 備考35
        mapping.put("bikou36", "bikou36"); // 備考36
        mapping.put("bikou37", "bikou37"); // 備考37
        mapping.put("bikou38", "bikou38"); // 備考38
        mapping.put("bikou39", "bikou39"); // 備考39
        mapping.put("bikou40", "bikou40"); // 備考40
        mapping.put("torokunichiji", "torokunichiji"); // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); // 更新日時
        mapping.put("revision", "revision"); // revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SubSrSpssekisou>> beanHandler = new BeanListHandler<>(SubSrSpssekisou.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [積層SPS_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrSpssekisou> loadTmpSrSpssekisou(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        
        String sql = "SELECT kojyo,lotno,edaban,tntapesyurui,tntapeno,tntapegenryou,gouki"
                + ",startdatetime,enddatetime,sekisouzure,tantousya,kakuninsya"
                + ",sekisouzure2,bikou1,bikou2,bikou3,bikou4,bikou5,KCPNO,GoukiCode"
                + ",TpLot,HakuriSpeed,KanaOndo,DAturyoku,DKaatuJikan,CPressAturyoku"
                + ",CPressKaatuJikan,CPressKankakuSosuu,LastKaaturyoku,LastKaatuJikan"
                + ",FourSplitTantou,STaoreryo1,STaoreryo2,STaoreryo3,STaoreryo4"
                + ",STsunAve,STsunMax,STsunMin,STsunSiguma,HNGKaisuu,GaikanTantou"
                + ",CPressKaisuu,HNGKaisuuAve,GNGKaisuu,GNGKaisuuAve,tapelotno"
                + ",taperollno1,taperollno2,taperollno3,genryoukigou,petfilmsyurui"
                + ",Kotyakugouki,Kotyakusheet,ShitaTanshigouki,UwaTanshigouki"
                + ",ShitaTanshiBukunuki,ShitaTanshi,HakuriKyuin,HakuriClearrance"
                + ",HakuriCutSpeed,ShitaPanchiOndo,UwaPanchiOndo,KaatuJikan,KaatuAturyoku"
                + ",UwaTanshi,GaikanKakunin1,GaikanKakunin2,GaikanKakunin3,GaikanKakunin4"
                + ",SyoriSetsuu,RyouhinSetsuu,StartTantosyacode,EndTantousyacode"
                + ",TanshiTapeSyurui,torokunichiji,kosinnichiji,karauti,karautibyou"
                + ",karautikai,zureti,GaikanKakunin5,revision,deleteflag "
                + "FROM tmp_sr_spssekisou "
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
        mapping.put("kojyo", "kojyo"); // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno"); // ﾛｯﾄNo
        mapping.put("edaban", "edaban"); // 枝番
        mapping.put("tntapesyurui", "tntapesyurui"); // 端子ﾃｰﾌﾟ種類
        mapping.put("tntapeno", "tntapeno"); // 端子ﾃｰﾌﾟNo
        mapping.put("tntapegenryou", "tntapegenryou"); // 端子ﾃｰﾌﾟ原料
        mapping.put("gouki", "gouki"); // 号機ｺｰﾄﾞ
        mapping.put("startdatetime", "startdatetime"); // 開始日時
        mapping.put("enddatetime", "enddatetime"); // 終了日時
        mapping.put("sekisouzure", "sekisouzure"); // 積層ｽﾞﾚ
        mapping.put("tantousya", "tantousya"); // 担当者ｺｰﾄﾞ
        mapping.put("kakuninsya", "kakuninsya"); // 確認者ｺｰﾄﾞ
        mapping.put("sekisouzure2", "sekisouzure2"); // 積層ｽﾞﾚ2
        mapping.put("bikou1", "bikou1"); // 備考1
        mapping.put("bikou2", "bikou2"); // 備考2
        mapping.put("bikou3", "bikou3"); // 備考3
        mapping.put("bikou4", "bikou4"); // 備考4
        mapping.put("bikou5", "bikou5"); // 備考5
        mapping.put("kcpno", "KCPNO"); // KCPNO
        mapping.put("goukicode", "GoukiCode"); // 号機ｺｰﾄﾞ
        mapping.put("tplot", "TpLot"); // ﾃｰﾌﾟLot
        mapping.put("hakurispeed", "HakuriSpeed"); // 剥離ｽﾋﾟｰﾄﾞ
        mapping.put("kanaondo", "KanaOndo"); // 金型温度
        mapping.put("daturyoku", "DAturyoku"); // 電極圧力
        mapping.put("dkaatujikan", "DKaatuJikan"); // 電極加圧時間
        mapping.put("cpressaturyoku", "CPressAturyoku"); // 中間ﾌﾟﾚｽ圧力
        mapping.put("cpresskaatujikan", "CPressKaatuJikan"); // 中間ﾌﾟﾚｽ加圧時間
        mapping.put("cpresskankakusosuu", "CPressKankakuSosuu"); // 中間ﾌﾟﾚｽ間隔総数
        mapping.put("lastkaaturyoku", "LastKaaturyoku"); // 最終加圧力
        mapping.put("lastkaatujikan", "LastKaatuJikan"); // 最終加圧時間
        mapping.put("foursplittantou", "FourSplitTantou"); // 4分割担当者ｺｰﾄﾞ
        mapping.put("staoreryo1", "STaoreryo1"); // 積層ﾀｵﾚ量1
        mapping.put("staoreryo2", "STaoreryo2"); // 積層ﾀｵﾚ量2
        mapping.put("staoreryo3", "STaoreryo3"); // 積層ﾀｵﾚ量3
        mapping.put("staoreryo4", "STaoreryo4"); // 積層ﾀｵﾚ量4
        mapping.put("stsunave", "STsunAve"); // 積層後T寸法AVE
        mapping.put("stsunmax", "STsunMax"); // 積層後T寸法MAX
        mapping.put("stsunmin", "STsunMin"); // 積層後T寸法MIN
        mapping.put("stsunsiguma", "STsunSiguma"); // 積層後T寸法σ
        mapping.put("hngkaisuu", "HNGKaisuu"); // 隔離NG回数
        mapping.put("gaikantantou", "GaikanTantou"); // 最終外観担当者ｺｰﾄﾞ
        mapping.put("cpresskaisuu", "CPressKaisuu"); // 中間ﾌﾟﾚｽ回数
        mapping.put("hngkaisuuave", "HNGKaisuuAve"); // 剥離NG回数AVE
        mapping.put("gngkaisuu", "GNGKaisuu"); // 画像NG回数
        mapping.put("gngkaisuuave", "GNGKaisuuAve"); // 画像NG回数AVE
        mapping.put("tapelotno", "tapelotno"); // ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
        mapping.put("taperollno1", "taperollno1"); // ﾃｰﾌﾟﾛｰﾙNo1
        mapping.put("taperollno2", "taperollno2"); // ﾃｰﾌﾟﾛｰﾙNo2
        mapping.put("taperollno3", "taperollno3"); // ﾃｰﾌﾟﾛｰﾙNo3
        mapping.put("genryoukigou", "genryoukigou"); // 原料記号
        mapping.put("petfilmsyurui", "petfilmsyurui"); // PETﾌｨﾙﾑ種類
        mapping.put("kotyakugouki", "Kotyakugouki"); // 固着ｼｰﾄ張付け機
        mapping.put("kotyakusheet", "Kotyakusheet"); // 固着ｼｰﾄ
        mapping.put("shitatanshigouki", "ShitaTanshigouki"); // 下端子号機
        mapping.put("uwatanshigouki", "UwaTanshigouki"); // 上端子号機
        mapping.put("shitatanshibukunuki", "ShitaTanshiBukunuki"); // 下端子ﾌﾞｸ抜き
        mapping.put("shitatanshi", "ShitaTanshi"); // 下端子
        mapping.put("hakurikyuin", "HakuriKyuin"); // 剥離吸引圧
        mapping.put("hakuriclearrance", "HakuriClearrance"); // 剥離ｸﾘｱﾗﾝｽ
        mapping.put("hakuricutspeed", "HakuriCutSpeed"); // 剥離ｶｯﾄ速度
        mapping.put("shitapanchiondo", "ShitaPanchiOndo"); // 下ﾊﾟﾝﾁ温度
        mapping.put("uwapanchiondo", "UwaPanchiOndo"); // 上ﾊﾟﾝﾁ温度
        mapping.put("kaatujikan", "KaatuJikan"); // 加圧時間
        mapping.put("kaatuaturyoku", "KaatuAturyoku"); // 加圧圧力
        mapping.put("uwatanshi", "UwaTanshi"); // 上端子
        mapping.put("gaikankakunin1", "GaikanKakunin1"); // 外観確認1
        mapping.put("gaikankakunin2", "GaikanKakunin2"); // 外観確認2
        mapping.put("gaikankakunin3", "GaikanKakunin3"); // 外観確認3
        mapping.put("gaikankakunin4", "GaikanKakunin4"); // 外観確認4
        mapping.put("syorisetsuu", "SyoriSetsuu"); // 処理ｾｯﾄ数
        mapping.put("ryouhinsetsuu", "RyouhinSetsuu"); // 良品ｾｯﾄ数
        mapping.put("starttantosyacode", "StartTantosyacode"); // 開始担当者ｺｰﾄﾞ
        mapping.put("endtantousyacode", "EndTantousyacode"); // 終了担当者ｺｰﾄﾞ
        mapping.put("tanshitapesyurui", "TanshiTapeSyurui"); // 端子ﾃｰﾌﾟ種類
        mapping.put("torokunichiji", "torokunichiji"); // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); // 更新日時
        mapping.put("karauti", "karauti"); // 空打ち
        mapping.put("karautibyou", "karautibyou"); // 空打ち秒
        mapping.put("karautikai", "karautikai"); // 空打ち回
        mapping.put("zureti", "zureti"); // ｽﾞﾚ値
        mapping.put("gaikanKakunin5", "GaikanKakunin5"); // 外観確認5
        mapping.put("revision", "revision"); // revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrSpssekisou>> beanHandler = new BeanListHandler<>(SrSpssekisou.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [積層・SPS_ｻﾌﾞ画面_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrSpssekisou> loadTmpSubSrSpssekisou(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT kojyo,lotno,edaban,setsuu1,setsuu2,setsuu3,setsuu4,setsuu5,setsuu6"
                + ",setsuu7,setsuu8,setsuu9,setsuu10,setsuu11,setsuu12,setsuu13,setsuu14"
                + ",setsuu15,setsuu16,setsuu17,setsuu18,setsuu19,setsuu20,setsuu21,setsuu22"
                + ",setsuu23,setsuu24,setsuu25,setsuu26,setsuu27,setsuu28,setsuu29,setsuu30"
                + ",setsuu31,setsuu32,setsuu33,setsuu34,setsuu35,setsuu36,setsuu37,setsuu38"
                + ",setsuu39,setsuu40,bikou1,bikou2,bikou3,bikou4,bikou5,bikou6,bikou7,bikou8"
                + ",bikou9,bikou10,bikou11,bikou12,bikou13,bikou14,bikou15,bikou16,bikou17"
                + ",bikou18,bikou19,bikou20,bikou21,bikou22,bikou23,bikou24,bikou25,bikou26"
                + ",bikou27,bikou28,bikou29,bikou30,bikou31,bikou32,bikou33,bikou34,bikou35"
                + ",bikou36,bikou37,bikou38,bikou39,bikou40,torokunichiji,kosinnichiji"
                + ",revision,deleteflag "
                + "FROM tmp_sub_sr_spssekisou "
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
        mapping.put("kojyo", "kojyo"); // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno"); // ﾛｯﾄNo
        mapping.put("edaban", "edaban"); // 枝番
        mapping.put("setsuu1", "setsuu1"); // ｾｯﾄ数1
        mapping.put("setsuu2", "setsuu2"); // ｾｯﾄ数2
        mapping.put("setsuu3", "setsuu3"); // ｾｯﾄ数3
        mapping.put("setsuu4", "setsuu4"); // ｾｯﾄ数4
        mapping.put("setsuu5", "setsuu5"); // ｾｯﾄ数5
        mapping.put("setsuu6", "setsuu6"); // ｾｯﾄ数6
        mapping.put("setsuu7", "setsuu7"); // ｾｯﾄ数7
        mapping.put("setsuu8", "setsuu8"); // ｾｯﾄ数8
        mapping.put("setsuu9", "setsuu9"); // ｾｯﾄ数9
        mapping.put("setsuu10", "setsuu10"); // ｾｯﾄ数10
        mapping.put("setsuu11", "setsuu11"); // ｾｯﾄ数11
        mapping.put("setsuu12", "setsuu12"); // ｾｯﾄ数12
        mapping.put("setsuu13", "setsuu13"); // ｾｯﾄ数13
        mapping.put("setsuu14", "setsuu14"); // ｾｯﾄ数14
        mapping.put("setsuu15", "setsuu15"); // ｾｯﾄ数15
        mapping.put("setsuu16", "setsuu16"); // ｾｯﾄ数16
        mapping.put("setsuu17", "setsuu17"); // ｾｯﾄ数17
        mapping.put("setsuu18", "setsuu18"); // ｾｯﾄ数18
        mapping.put("setsuu19", "setsuu19"); // ｾｯﾄ数19
        mapping.put("setsuu20", "setsuu20"); // ｾｯﾄ数20
        mapping.put("setsuu21", "setsuu21"); // ｾｯﾄ数21
        mapping.put("setsuu22", "setsuu22"); // ｾｯﾄ数22
        mapping.put("setsuu23", "setsuu23"); // ｾｯﾄ数23
        mapping.put("setsuu24", "setsuu24"); // ｾｯﾄ数24
        mapping.put("setsuu25", "setsuu25"); // ｾｯﾄ数25
        mapping.put("setsuu26", "setsuu26"); // ｾｯﾄ数26
        mapping.put("setsuu27", "setsuu27"); // ｾｯﾄ数27
        mapping.put("setsuu28", "setsuu28"); // ｾｯﾄ数28
        mapping.put("setsuu29", "setsuu29"); // ｾｯﾄ数29
        mapping.put("setsuu30", "setsuu30"); // ｾｯﾄ数30
        mapping.put("setsuu31", "setsuu31"); // ｾｯﾄ数31
        mapping.put("setsuu32", "setsuu32"); // ｾｯﾄ数32
        mapping.put("setsuu33", "setsuu33"); // ｾｯﾄ数33
        mapping.put("setsuu34", "setsuu34"); // ｾｯﾄ数34
        mapping.put("setsuu35", "setsuu35"); // ｾｯﾄ数35
        mapping.put("setsuu36", "setsuu36"); // ｾｯﾄ数36
        mapping.put("setsuu37", "setsuu37"); // ｾｯﾄ数37
        mapping.put("setsuu38", "setsuu38"); // ｾｯﾄ数38
        mapping.put("setsuu39", "setsuu39"); // ｾｯﾄ数39
        mapping.put("setsuu40", "setsuu40"); // ｾｯﾄ数40
        mapping.put("bikou1", "bikou1"); // 備考1
        mapping.put("bikou2", "bikou2"); // 備考2
        mapping.put("bikou3", "bikou3"); // 備考3
        mapping.put("bikou4", "bikou4"); // 備考4
        mapping.put("bikou5", "bikou5"); // 備考5
        mapping.put("bikou6", "bikou6"); // 備考6
        mapping.put("bikou7", "bikou7"); // 備考7
        mapping.put("bikou8", "bikou8"); // 備考8
        mapping.put("bikou9", "bikou9"); // 備考9
        mapping.put("bikou10", "bikou10"); // 備考10
        mapping.put("bikou11", "bikou11"); // 備考11
        mapping.put("bikou12", "bikou12"); // 備考12
        mapping.put("bikou13", "bikou13"); // 備考13
        mapping.put("bikou14", "bikou14"); // 備考14
        mapping.put("bikou15", "bikou15"); // 備考15
        mapping.put("bikou16", "bikou16"); // 備考16
        mapping.put("bikou17", "bikou17"); // 備考17
        mapping.put("bikou18", "bikou18"); // 備考18
        mapping.put("bikou19", "bikou19"); // 備考19
        mapping.put("bikou20", "bikou20"); // 備考20
        mapping.put("bikou21", "bikou21"); // 備考21
        mapping.put("bikou22", "bikou22"); // 備考22
        mapping.put("bikou23", "bikou23"); // 備考23
        mapping.put("bikou24", "bikou24"); // 備考24
        mapping.put("bikou25", "bikou25"); // 備考25
        mapping.put("bikou26", "bikou26"); // 備考26
        mapping.put("bikou27", "bikou27"); // 備考27
        mapping.put("bikou28", "bikou28"); // 備考28
        mapping.put("bikou29", "bikou29"); // 備考29
        mapping.put("bikou30", "bikou30"); // 備考30
        mapping.put("bikou31", "bikou31"); // 備考31
        mapping.put("bikou32", "bikou32"); // 備考32
        mapping.put("bikou33", "bikou33"); // 備考33
        mapping.put("bikou34", "bikou34"); // 備考34
        mapping.put("bikou35", "bikou35"); // 備考35
        mapping.put("bikou36", "bikou36"); // 備考36
        mapping.put("bikou37", "bikou37"); // 備考37
        mapping.put("bikou38", "bikou38"); // 備考38
        mapping.put("bikou39", "bikou39"); // 備考39
        mapping.put("bikou40", "bikou40"); // 備考40
        mapping.put("torokunichiji", "torokunichiji"); // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); // 更新日時
        mapping.put("revision", "revision"); // revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SubSrSpssekisou>> beanHandler = new BeanListHandler<>(SubSrSpssekisou.class, rowProcessor);

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
            QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
            QueryRunner queryRunnerWip = new QueryRunner(processData.getDataSourceWip());

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

            // 積層・SPSデータ取得
            List<SrSpssekisou> srSpssekisouDataList = getSrSpssekisouData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban);
            if (srSpssekisouDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // 積層・SPS_ｻﾌﾞ画面データ取得
            List<SubSrSpssekisou> subSrSpssekisouDataList = getSubSrSpssekisouData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban);
            if (subSrSpssekisouDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // メイン画面データ設定
            setInputItemDataMainForm(processData, srSpssekisouDataList.get(0));

            // 剥離内容入力画面データ設定
            setInputItemDataSubFormC006(subSrSpssekisouDataList.get(0));

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
     * @param srSpssekisouData 積層・SPS
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrSpssekisou srSpssekisouData) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srSpssekisouData != null) {
            // 元データが存在する場合元データより取得
            return getSrSpssekisouItemData(itemId, srSpssekisouData);
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

//    /**
//     * 注意メッセージ表示
//     *
//     * @param processData 処理制御データ
//     * @return 処理制御データ
//     */
//    public ProcessData openInfoMessage(ProcessData processData) {
//
//        processData.setMethod("");
//
//        // メッセージを画面に渡す
//        InfoMessage beanInfoMessage = (InfoMessage) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_INFO_MESSAGE);
//        beanInfoMessage.setInfoMessageList(processData.getInfoMessageList());
//
//        // 実行スクリプトを設定
//        processData.setExecuteScript("PF('W_dlg_infoMessage').show();");
//
//        return processData;
//    }
    
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
     * 積層SPS_仮登録(tmp_sr_spssekisou)登録処理
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
    private void insertTmpSrSpssekisou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
              String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {

        String sql = "INSERT INTO tmp_sr_spssekisou ("
                + "kojyo,lotno,edaban,tntapesyurui,tntapeno,tntapegenryou,gouki,startdatetime,enddatetime,sekisouzure,tantousya,kakuninsya,sekisouzure2"
                + ",bikou1,bikou2,bikou3,bikou4,bikou5,KCPNO,GoukiCode,TpLot,HakuriSpeed,KanaOndo,DAturyoku,DKaatuJikan,CPressAturyoku,CPressKaatuJikan"
                + ",CPressKankakuSosuu,LastKaaturyoku,LastKaatuJikan,FourSplitTantou,STaoreryo1,STaoreryo2,STaoreryo3,STaoreryo4,STsunAve,STsunMax,STsunMin"
                + ",STsunSiguma,HNGKaisuu,GaikanTantou,CPressKaisuu,HNGKaisuuAve,GNGKaisuu,GNGKaisuuAve,tapelotno,taperollno1,taperollno2,taperollno3"
                + ",genryoukigou,petfilmsyurui,Kotyakugouki,Kotyakusheet,ShitaTanshigouki,UwaTanshigouki,ShitaTanshiBukunuki,ShitaTanshi,HakuriKyuin"
                + ",HakuriClearrance,HakuriCutSpeed,ShitaPanchiOndo,UwaPanchiOndo,KaatuJikan,KaatuAturyoku,UwaTanshi,GaikanKakunin1,GaikanKakunin2,GaikanKakunin3"
                + ",GaikanKakunin4,SyoriSetsuu,RyouhinSetsuu,StartTantosyacode,EndTantousyacode,TanshiTapeSyurui,torokunichiji"
                + ",kosinnichiji,karauti,karautibyou,karautikai,zureti,GaikanKakunin5,revision,deleteflag"
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
                + " ,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
                + ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrSpssekisou(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, itemList, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 積層SPS_仮登録(tmp_sr_spssekisou)更新処理
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
    private void updateTmpSrSpssekisou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {

        String sql = "UPDATE tmp_sr_spssekisou SET "
                + "tntapesyurui = ?,tntapeno = ?,tntapegenryou = ?,gouki = ?,startdatetime = ?,enddatetime = ?,"
                + "sekisouzure = ?,tantousya = ?,kakuninsya = ?,sekisouzure2 = ?,bikou1 = ?,bikou2 = ?,bikou3 = ?,bikou4 = ?,bikou5 = ?,KCPNO = ?,"
                + "GoukiCode = ?,TpLot = ?,HakuriSpeed = ?,KanaOndo = ?,DAturyoku = ?,DKaatuJikan = ?,CPressAturyoku = ?,CPressKaatuJikan = ?,"
                + "CPressKankakuSosuu = ?,LastKaaturyoku = ?,LastKaatuJikan = ?,FourSplitTantou = ?,STaoreryo1 = ?,STaoreryo2 = ?,STaoreryo3 = ?,"
                + "STaoreryo4 = ?,STsunAve = ?,STsunMax = ?,STsunMin = ?,STsunSiguma = ?,HNGKaisuu = ?,GaikanTantou = ?,CPressKaisuu = ?,HNGKaisuuAve = ?,"
                + "GNGKaisuu = ?,GNGKaisuuAve = ?,tapelotno = ?,taperollno1 = ?,taperollno2 = ?,taperollno3 = ?,genryoukigou = ?,petfilmsyurui = ?,"
                + "Kotyakusheet = ?,ShitaTanshigouki = ?,UwaTanshigouki = ?,ShitaTanshiBukunuki = ?,ShitaTanshi = ?,HakuriKyuin = ?,"
                + "HakuriClearrance = ?,HakuriCutSpeed = ?,ShitaPanchiOndo = ?,UwaPanchiOndo = ?,KaatuJikan = ?,KaatuAturyoku = ?,UwaTanshi = ?,"
                + "SyoriSetsuu = ?,RyouhinSetsuu = ?,StartTantosyacode = ?,EndTantousyacode = ?,TanshiTapeSyurui = ?,kosinnichiji = ?,"
                + "karauti = ?,karautibyou = ?,karautikai = ?,zureti = ?,GaikanKakunin5 = ?,revision = ?,deleteflag = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrSpssekisou> srSrSpssekisouList = getSrSpssekisouData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrSpssekisou srSpssekisou = null;
        if (!srSrSpssekisouList.isEmpty()) {
            srSpssekisou = srSrSpssekisouList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrSpssekisou(false, newRev, 0, "", "", "", systemTime, itemList, srSpssekisou);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 積層SPS_仮登録(tmp_sr_spssekisou)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrSpssekisou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sr_spssekisou "
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
     * 積層SPS_仮登録(tmp_sr_spssekisou)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srSpssekisouData 積層SPSデータ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrSpssekisou(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList, SrSpssekisou srSpssekisouData) {
        List<Object> params = new ArrayList<>();
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(null); //端子ﾃｰﾌﾟ種類
        params.add(null); //端子ﾃｰﾌﾟNo
        params.add(null); //端子ﾃｰﾌﾟ原料
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.SEKISO_GOKI, srSpssekisouData))); //積層号機
        params.add(DBUtil.stringToDateObjectDefaultNull(
            getItemData(itemList, GXHDO101B004Const.KAISHI_DAY, srSpssekisouData),
            getItemData(itemList, GXHDO101B004Const.KAISHI_TIME, srSpssekisouData))); //開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(
            getItemData(itemList, GXHDO101B004Const.SHURYO_DAY, srSpssekisouData),
            getItemData(itemList, GXHDO101B004Const.SHURYO_TIME, srSpssekisouData))); //終了日時
        params.add(null);//積層ｽﾞﾚ
        params.add(null);//担当者ｺｰﾄﾞ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.KAISHI_KAKUNINSYA, srSpssekisouData)));//確認者ｺｰﾄﾞ
        params.add(null);//積層ｽﾞﾚ2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.BIKOU1, srSpssekisouData))); //備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.BIKOU2, srSpssekisouData))); //備考2
        params.add(null);//備考3
        params.add(null);//備考4
        params.add(null);//備考5
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.KCPNO, srSpssekisouData))); //KCPNO
        params.add(null);//号機ｺｰﾄﾞ
        params.add(null);//ﾃｰﾌﾟLot
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.HAKURI_SPEED, srSpssekisouData))); //剥離速度
        params.add(null);//金型温度
        params.add(null);//電極圧力
        params.add(null);//電極加圧時間
        params.add(null);//中間ﾌﾟﾚｽ圧力
        params.add(null);//中間ﾌﾟﾚｽ加圧時間
        params.add(null);//中間ﾌﾟﾚｽ間隔総数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.LAST_KAATURYOKU, srSpssekisouData))); //最終加圧力
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.LAST_KAATUJIKAN, srSpssekisouData))); //最終加圧時間
        params.add(null);//4分割担当者ｺｰﾄﾞ
        params.add(null);//積層ﾀｵﾚ量1
        params.add(null);//積層ﾀｵﾚ量2
        params.add(null);//積層ﾀｵﾚ量3
        params.add(null);//積層ﾀｵﾚ量4
        params.add(null);//積層後T寸法AVE
        params.add(null);//積層後T寸法MAX
        params.add(null);//積層後T寸法MIN
        params.add(null);//積層後T寸法σ
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.HNG_KAISUU, srSpssekisouData))); //剥離NG回数
        params.add(null);//最終外観担当者ｺｰﾄﾞ
        params.add(null);//中間ﾌﾟﾚｽ回数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.HNG_AVE, srSpssekisouData))); //剥離NG_AVE
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.GNG_KAISUU, srSpssekisouData))); //画処NG回数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.GNG_KAISUUAVE, srSpssekisouData))); //画処NG_AVE
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.SLIP_LOTNO, srSpssekisouData))); //ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.ROLL_NO1, srSpssekisouData))); //ﾃｰﾌﾟﾛｰﾙNo1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.ROLL_NO2, srSpssekisouData))); //ﾃｰﾌﾟﾛｰﾙNo2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.ROLL_NO3, srSpssekisouData))); //ﾃｰﾌﾟﾛｰﾙNo3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.GENRYO_KIGOU, srSpssekisouData))); //原料記号
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.PET_FILM_SHURUI, srSpssekisouData))); //PETﾌｨﾙﾑ種類
        if (isInsert) {
            params.add(null); //固着シート貼付り機    
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.KOTYAKU_SHEET, srSpssekisouData))); //固着シート
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.SHITATANSHI_GOUKI, srSpssekisouData))); //下端子号機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.UWATANSHI_GOUKI, srSpssekisouData))); //上端子号機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.SHITA_TANSHI_BUKUNUKI, srSpssekisouData))); // 下端子ﾌﾞｸ抜き
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.SHITA_TANSHI, srSpssekisouData))); //下端子
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.HAKURI_KYUIN, srSpssekisouData))); //剥離吸引圧
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.HAKURI_CLEARRANCE, srSpssekisouData))); //剥離クリアランス
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.HAKURI_CUT_SPEED, srSpssekisouData))); //剥離カット速度
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.SHITA_PANCHI_ONDO, srSpssekisouData))); //下パンチ温度
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.UWA_PANCHI_ONDO, srSpssekisouData))); //上パンチ温度
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.KAATU_JIKAN, srSpssekisouData))); //加圧時間
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.KAATU_ATURYOKU, srSpssekisouData))); //加圧圧力
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.UWA_TANSHI, srSpssekisouData))); //上端子
        if (isInsert) {
            params.add(null); //外観確認1
            params.add(null); //外観確認2
            params.add(null); //外観確認3
            params.add(null); //外観確認4  
        }
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.SYORI_SETSUU, srSpssekisouData))); //処理セット数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.RYOUHIN_SETSUU, srSpssekisouData))); //良品セット数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.KAISHI_TANTOSYA, srSpssekisouData))); //開始担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.SHURYO_TANTOSYA, srSpssekisouData))); //終了担当者
        // 端子テープ種類
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B004Const.TTAPE_SYURUI, srSpssekisouData))) {
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
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.KARAUTI, srSpssekisouData))); //空打ち
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.KARAUTIBYOU, srSpssekisouData))); //空打ち[秒]
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.KARAUTIKAI, srSpssekisouData))); //空打ち[回]
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.ZURETI, srSpssekisouData))); //ｽﾞﾚ値
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B004Const.GAIKAN_KAKUNIN5, srSpssekisouData))); //外観確認5
        params.add(newRev); //revision
        params.add(deleteflag); //削除ﾌﾗｸﾞ
        

        return params;
    }

    /**
     * 積層・SPS_ｻﾌﾞ画面仮登録(tmp_sub_sr_spssekisou)登録処理
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
    private void insertTmpSubSrSpssekisou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        String sql = "INSERT INTO tmp_sub_sr_spssekisou ("
                + "kojyo,lotno,edaban,setsuu1,setsuu2,setsuu3,setsuu4,setsuu5,setsuu6,setsuu7,setsuu8,setsuu9,setsuu10,setsuu11"
                + ",setsuu12,setsuu13,setsuu14,setsuu15,setsuu16,setsuu17,setsuu18,setsuu19,setsuu20,setsuu21,setsuu22,setsuu23"
                + ",setsuu24,setsuu25,setsuu26,setsuu27,setsuu28,setsuu29,setsuu30,setsuu31,setsuu32,setsuu33,setsuu34,setsuu35"
                + ",setsuu36,setsuu37,setsuu38,setsuu39,setsuu40,bikou1,bikou2,bikou3,bikou4,bikou5,bikou6,bikou7,bikou8,bikou9"
                + ",bikou10,bikou11,bikou12,bikou13,bikou14,bikou15,bikou16,bikou17,bikou18,bikou19,bikou20,bikou21,bikou22,bikou23"
                + ",bikou24,bikou25,bikou26,bikou27,bikou28,bikou29,bikou30,bikou31,bikou32,bikou33,bikou34,bikou35,bikou36,bikou37"
                + ",bikou38,bikou39,bikou40,torokunichiji,kosinnichiji,revision,deleteflag"
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
                + ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";     

        List<Object> params = setUpdateParameterTmpSubSrSpssekisou(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 積層・SPS_ｻﾌﾞ画面仮登録(tmp_sub_sr_spssekisou)更新処理
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
    private void updateTmpSubSrSpssekisou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        String sql = "UPDATE tmp_sub_sr_spssekisou SET "
                + "setsuu1 = ?,setsuu2 = ?,setsuu3 = ?,setsuu4 = ?,setsuu5 = ?,setsuu6 = ?,setsuu7 = ?"
                + ",setsuu8 = ?,setsuu9 = ?,setsuu10 = ?,setsuu11 = ?,setsuu12 = ?,setsuu13 = ?,setsuu14 = ?,setsuu15 = ?,setsuu16 = ?"
                + ",setsuu17 = ?,setsuu18 = ?,setsuu19 = ?,setsuu20 = ?,setsuu21 = ?,setsuu22 = ?,setsuu23 = ?,setsuu24 = ?,setsuu25 = ?"
                + ",setsuu26 = ?,setsuu27 = ?,setsuu28 = ?,setsuu29 = ?,setsuu30 = ?,setsuu31 = ?,setsuu32 = ?,setsuu33 = ?,setsuu34 = ?"
                + ",setsuu35 = ?,setsuu36 = ?,setsuu37 = ?,setsuu38 = ?,setsuu39 = ?,setsuu40 = ?,bikou1 = ?,bikou2 = ?,bikou3 = ?"
                + ",bikou4 = ?,bikou5 = ?,bikou6 = ?,bikou7 = ?,bikou8 = ?,bikou9 = ?,bikou10 = ?,bikou11 = ?,bikou12 = ?,bikou13 = ?"
                + ",bikou14 = ?,bikou15 = ?,bikou16 = ?,bikou17 = ?,bikou18 = ?,bikou19 = ?,bikou20 = ?,bikou21 = ?,bikou22 = ?,bikou23 = ?"
                + ",bikou24 = ?,bikou25 = ?,bikou26 = ?,bikou27 = ?,bikou28 = ?,bikou29 = ?,bikou30 = ?,bikou31 = ?,bikou32 = ?,bikou33 = ?"
                + ",bikou34 = ?,bikou35 = ?,bikou36 = ?,bikou37 = ?,bikou38 = ?,bikou39 = ?,bikou40 = ?,kosinnichiji = ? ,revision = ? "
                + ",deleteflag = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ?";

        List<Object> params = setUpdateParameterTmpSubSrSpssekisou(false, newRev, 0, "", "", "", systemTime);

        // 検索条件
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 積層・SPS_ｻﾌﾞ画面仮登録(tmp_sub_sr_spssekisou)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSubSrSpssekisou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {
        String sql = "DELETE FROM tmp_sub_sr_spssekisou "
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
     * 積層・SPS_ｻﾌﾞ画面仮登録(tmp_sub_sr_spssekisou)更新値パラメータ設定
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
    private List<Object> setUpdateParameterTmpSubSrSpssekisou(boolean isInsert, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) {
        List<Object> params = new ArrayList<>();

        // 子画面情報を取得
        GXHDO101C006 beanGXHDO101C006 = (GXHDO101C006) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C006);
        List<GXHDO101C006Model.HakuriInputData> hakuriInputDataList = beanGXHDO101C006.getGxhdO101c006Model().getHakuriInputDataList();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(0).getSetsuuVal())); // ｾｯﾄ数1
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(1).getSetsuuVal())); // ｾｯﾄ数2
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(2).getSetsuuVal())); // ｾｯﾄ数3
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(3).getSetsuuVal())); // ｾｯﾄ数4
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(4).getSetsuuVal())); // ｾｯﾄ数5
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(5).getSetsuuVal())); // ｾｯﾄ数6
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(6).getSetsuuVal())); // ｾｯﾄ数7
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(7).getSetsuuVal())); // ｾｯﾄ数8
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(8).getSetsuuVal())); // ｾｯﾄ数9
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(9).getSetsuuVal())); // ｾｯﾄ数10
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(10).getSetsuuVal())); // ｾｯﾄ数11
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(11).getSetsuuVal())); // ｾｯﾄ数12
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(12).getSetsuuVal())); // ｾｯﾄ数13
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(13).getSetsuuVal())); // ｾｯﾄ数14
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(14).getSetsuuVal())); // ｾｯﾄ数15
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(15).getSetsuuVal())); // ｾｯﾄ数16
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(16).getSetsuuVal())); // ｾｯﾄ数17
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(17).getSetsuuVal())); // ｾｯﾄ数18
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(18).getSetsuuVal())); // ｾｯﾄ数19
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(19).getSetsuuVal())); // ｾｯﾄ数20
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(20).getSetsuuVal())); // ｾｯﾄ数21
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(21).getSetsuuVal())); // ｾｯﾄ数22
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(22).getSetsuuVal())); // ｾｯﾄ数23
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(23).getSetsuuVal())); // ｾｯﾄ数24
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(24).getSetsuuVal())); // ｾｯﾄ数25
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(25).getSetsuuVal())); // ｾｯﾄ数26
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(26).getSetsuuVal())); // ｾｯﾄ数27
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(27).getSetsuuVal())); // ｾｯﾄ数28
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(28).getSetsuuVal())); // ｾｯﾄ数29
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(29).getSetsuuVal())); // ｾｯﾄ数30
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(30).getSetsuuVal())); // ｾｯﾄ数31
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(31).getSetsuuVal())); // ｾｯﾄ数32
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(32).getSetsuuVal())); // ｾｯﾄ数33
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(33).getSetsuuVal())); // ｾｯﾄ数34
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(34).getSetsuuVal())); // ｾｯﾄ数35
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(35).getSetsuuVal())); // ｾｯﾄ数36
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(36).getSetsuuVal())); // ｾｯﾄ数37
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(37).getSetsuuVal())); // ｾｯﾄ数38
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(38).getSetsuuVal())); // ｾｯﾄ数39
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(39).getSetsuuVal())); // ｾｯﾄ数40
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(0).getBikouVal())); // 備考1
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(1).getBikouVal())); // 備考2
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(2).getBikouVal())); // 備考3
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(3).getBikouVal())); // 備考4
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(4).getBikouVal())); // 備考5
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(5).getBikouVal())); // 備考6
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(6).getBikouVal())); // 備考7
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(7).getBikouVal())); // 備考8
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(8).getBikouVal())); // 備考9
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(9).getBikouVal())); // 備考10
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(10).getBikouVal())); // 備考11
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(11).getBikouVal())); // 備考12
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(12).getBikouVal())); // 備考13
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(13).getBikouVal())); // 備考14
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(14).getBikouVal())); // 備考15
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(15).getBikouVal())); // 備考16
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(16).getBikouVal())); // 備考17
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(17).getBikouVal())); // 備考18
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(18).getBikouVal())); // 備考19
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(19).getBikouVal())); // 備考20
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(20).getBikouVal())); // 備考21
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(21).getBikouVal())); // 備考22
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(22).getBikouVal())); // 備考23
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(23).getBikouVal())); // 備考24
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(24).getBikouVal())); // 備考25
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(25).getBikouVal())); // 備考26
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(26).getBikouVal())); // 備考27
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(27).getBikouVal())); // 備考28
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(28).getBikouVal())); // 備考29
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(29).getBikouVal())); // 備考30
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(30).getBikouVal())); // 備考31
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(31).getBikouVal())); // 備考32
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(32).getBikouVal())); // 備考33
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(33).getBikouVal())); // 備考34
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(34).getBikouVal())); // 備考35
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(35).getBikouVal())); // 備考36
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(36).getBikouVal())); // 備考37
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(37).getBikouVal())); // 備考38
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(38).getBikouVal())); // 備考39
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(39).getBikouVal())); // 備考40

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
     * 積層SPS(sr_spssekisou)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param tmpSrSpssekisou 仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrSpssekisou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList, SrSpssekisou tmpSrSpssekisou) throws SQLException {

        String sql = "INSERT INTO sr_spssekisou ("
                + "kojyo,lotno,edaban,tntapesyurui,tntapeno,tntapegenryou,gouki,startdatetime,enddatetime,sekisouzure,tantousya"
                + ",kakuninsya,sekisouzure2,bikou1,bikou2,bikou3,bikou4,bikou5,KCPNO,GoukiCode,TpLot,HakuriSpeed,KanaOndo,DAturyoku"
                + ",DKaatuJikan,CPressAturyoku,CPressKaatuJikan,CPressKankakuSosuu,LastKaaturyoku,LastKaatuJikan,FourSplitTantou"
                + ",STaoreryo1,STaoreryo2,STaoreryo3,STaoreryo4,STsunAve,STsunMax,STsunMin,STsunSiguma,HNGKaisuu,GaikanTantou"
                + ",CPressKaisuu,HNGKaisuuAve,GNGKaisuu,GNGKaisuuAve,tapelotno,taperollno1,taperollno2,taperollno3,genryoukigou"
                + ",petfilmsyurui,Kotyakugouki,Kotyakusheet,ShitaTanshigouki,UwaTanshigouki,ShitaTanshiBukunuki,ShitaTanshi"
                + ",HakuriKyuin,HakuriClearrance,HakuriCutSpeed,ShitaPanchiOndo,UwaPanchiOndo,KaatuJikan,KaatuAturyoku,UwaTanshi"
                + ",GaikanKakunin1,GaikanKakunin2,GaikanKakunin3,GaikanKakunin4,SyoriSetsuu,RyouhinSetsuu,StartTantosyacode"
                + ",EndTantousyacode,TanshiTapeSyurui,torokunichiji,kosinnichiji,karauti,karautibyou,karautikai,zureti,GaikanKakunin5,revision"
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
                + ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
                + ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
        
        List<Object> params = setUpdateParameterSrSpssekisou(true, newRev, kojyo, lotNo, edaban, systemTime, itemList, tmpSrSpssekisou);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 積層SPS(sr_spssekisou)更新処理
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
    private void updateSrSpssekisou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {
        String sql = "UPDATE sr_spssekisou SET "
                + "tntapesyurui = ?,tntapeno = ?,tntapegenryou = ?,gouki = ?,startdatetime = ?,enddatetime = ?,sekisouzure = ?,tantousya = ?,kakuninsya = ?,"
                + "sekisouzure2 = ?,bikou1 = ?,bikou2 = ?,bikou3 = ?,bikou4 = ?,bikou5 = ?,KCPNO = ?,GoukiCode = ?,TpLot = ?,HakuriSpeed = ?,KanaOndo = ?,"
                + "DAturyoku = ?,DKaatuJikan = ?,CPressAturyoku = ?,CPressKaatuJikan = ?,CPressKankakuSosuu = ?,LastKaaturyoku = ?,LastKaatuJikan = ?,"
                + "FourSplitTantou = ?,STaoreryo1 = ?,STaoreryo2 = ?,STaoreryo3 = ?,STaoreryo4 = ?,STsunAve = ?,STsunMax = ?,STsunMin = ?,STsunSiguma = ?,"
                + "HNGKaisuu = ?,GaikanTantou = ?,CPressKaisuu = ?,HNGKaisuuAve = ?,GNGKaisuu = ?,GNGKaisuuAve = ?,tapelotno = ?,taperollno1 = ?,taperollno2 = ?,"
                + "taperollno3 = ?,genryoukigou = ?,petfilmsyurui = ?,Kotyakusheet = ?,ShitaTanshigouki = ?,UwaTanshigouki = ?,"
                + "ShitaTanshiBukunuki = ?,ShitaTanshi = ?,HakuriKyuin = ?,HakuriClearrance = ?,HakuriCutSpeed = ?,ShitaPanchiOndo = ?,UwaPanchiOndo = ?,"
                + "KaatuJikan = ?,KaatuAturyoku = ?,UwaTanshi = ?,SyoriSetsuu = ?,RyouhinSetsuu = ?,StartTantosyacode = ?,EndTantousyacode = ?,TanshiTapeSyurui = ?,"
                + "kosinnichiji = ?,karauti = ?,karautibyou = ?,karautikai = ?,zureti = ?,GaikanKakunin5 = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ?";

        // 更新前の値を取得
        List<SrSpssekisou> srSpssekisouList = getSrSpssekisouData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrSpssekisou srSpssekisou = null;
        if (!srSpssekisouList.isEmpty()) {
            srSpssekisou = srSpssekisouList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrSpssekisou(false, newRev, "", "", "", systemTime, itemList, srSpssekisou);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 積層SPS(sr_spssekisou)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srSpssekisouData 積層SPSデータ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrSpssekisou(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            Timestamp systemTime, List<FXHDD01> itemList, SrSpssekisou srSpssekisouData) {
        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(""); //端子ﾃｰﾌﾟ種類
        params.add(""); //端子ﾃｰﾌﾟNo
        params.add(""); //端子ﾃｰﾌﾟ原料
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B004Const.SEKISO_GOKI, srSpssekisouData))); //積層号機
        params.add(DBUtil.stringToDateObject(
                getItemData(itemList, GXHDO101B004Const.KAISHI_DAY, srSpssekisouData),
                getItemData(itemList, GXHDO101B004Const.KAISHI_TIME, srSpssekisouData))); //開始日時
        params.add(DBUtil.stringToDateObject(
                getItemData(itemList, GXHDO101B004Const.SHURYO_DAY, srSpssekisouData),
                getItemData(itemList, GXHDO101B004Const.SHURYO_TIME, srSpssekisouData))); //終了日時
        params.add(0); //積層ｽﾞﾚ
        params.add(""); //担当者ｺｰﾄﾞ
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B004Const.KAISHI_KAKUNINSYA, srSpssekisouData))); //確認者ｺｰﾄﾞ
        params.add(0); //積層ｽﾞﾚ2
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B004Const.BIKOU1, srSpssekisouData))); //備考1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B004Const.BIKOU2, srSpssekisouData))); //備考2
        params.add(""); //備考3
        params.add(""); //備考4
        params.add(""); //備考5
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B004Const.KCPNO, srSpssekisouData))); //KCPNO
        params.add(""); //号機ｺｰﾄﾞ
        params.add(""); //ﾃｰﾌﾟLot
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B004Const.HAKURI_SPEED, srSpssekisouData))); //剥離速度
        params.add(0); //金型温度
        params.add(0); //電極圧力
        params.add(0); //電極加圧時間
        params.add(0); //中間ﾌﾟﾚｽ圧力
        params.add(0); //中間ﾌﾟﾚｽ加圧時間
        params.add(0); //中間ﾌﾟﾚｽ間隔総数
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B004Const.LAST_KAATURYOKU, srSpssekisouData))); //最終加圧力
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B004Const.LAST_KAATUJIKAN, srSpssekisouData))); //最終加圧時間
        params.add(""); //4分割担当者ｺｰﾄﾞ
        params.add(0); //積層ﾀｵﾚ量1
        params.add(0); //積層ﾀｵﾚ量2
        params.add(0); //積層ﾀｵﾚ量3
        params.add(0); //積層ﾀｵﾚ量4
        params.add(0); //積層後T寸法AVE
        params.add(0); //積層後T寸法MAX
        params.add(0); //積層後T寸法MIN
        params.add(0); //積層後T寸法σ
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B004Const.HNG_KAISUU, srSpssekisouData))); //剥離NG回数
        params.add(""); //最終外観担当者ｺｰﾄﾞ
        params.add(0); //中間ﾌﾟﾚｽ回数
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B004Const.HNG_AVE, srSpssekisouData))); //剥離NG_AVE
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B004Const.GNG_KAISUU, srSpssekisouData))); //画処NG回数
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B004Const.GNG_KAISUUAVE, srSpssekisouData))); //画処NG_AVE
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B004Const.SLIP_LOTNO, srSpssekisouData))); //ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B004Const.ROLL_NO1, srSpssekisouData))); //ﾃｰﾌﾟﾛｰﾙNo1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B004Const.ROLL_NO2, srSpssekisouData))); //ﾃｰﾌﾟﾛｰﾙNo2
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B004Const.ROLL_NO3, srSpssekisouData))); //ﾃｰﾌﾟﾛｰﾙNo3
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B004Const.GENRYO_KIGOU, srSpssekisouData))); //原料記号
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B004Const.PET_FILM_SHURUI, srSpssekisouData))); //PETﾌｨﾙﾑ種類
        if (isInsert) {
            params.add(""); //固着シート貼付り機
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B004Const.KOTYAKU_SHEET, srSpssekisouData))); //固着シート
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B004Const.SHITATANSHI_GOUKI, srSpssekisouData))); //下端子号機
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B004Const.UWATANSHI_GOUKI, srSpssekisouData))); //上端子号機
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B004Const.SHITA_TANSHI_BUKUNUKI, srSpssekisouData))); //下端子ﾌﾞｸ抜き
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B004Const.SHITA_TANSHI, srSpssekisouData))); //下端子
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B004Const.HAKURI_KYUIN, srSpssekisouData))); //剥離吸引圧
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B004Const.HAKURI_CLEARRANCE, srSpssekisouData))); //剥離クリアランス
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B004Const.HAKURI_CUT_SPEED, srSpssekisouData))); //剥離カット速度
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B004Const.SHITA_PANCHI_ONDO, srSpssekisouData))); //下パンチ温度
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B004Const.UWA_PANCHI_ONDO, srSpssekisouData))); //上パンチ温度
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B004Const.KAATU_JIKAN, srSpssekisouData))); //加圧時間
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B004Const.KAATU_ATURYOKU, srSpssekisouData))); //加圧圧力
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B004Const.UWA_TANSHI, srSpssekisouData))); //上端子
        if (isInsert) {
            params.add(""); //外観確認1
            params.add(""); //外観確認2
            params.add(""); //外観確認3
            params.add(""); //外観確認4
        }
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B004Const.SYORI_SETSUU, srSpssekisouData))); //処理セット数
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B004Const.RYOUHIN_SETSUU, srSpssekisouData))); //良品セット数
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B004Const.KAISHI_TANTOSYA, srSpssekisouData))); //開始担当者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B004Const.SHURYO_TANTOSYA, srSpssekisouData))); //終了担当者
        // 端子テープ種類
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B004Const.TTAPE_SYURUI, srSpssekisouData))) {
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
        
        if (isInsert) {
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時

        } else {
            params.add(systemTime); //更新日時
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B004Const.KARAUTI, srSpssekisouData))); //空打ち
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B004Const.KARAUTIBYOU, srSpssekisouData)));//空打ち[秒]
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B004Const.KARAUTIKAI, srSpssekisouData)));//空打ち[回]
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B004Const.ZURETI, srSpssekisouData)));//ｽﾞﾚ値
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B004Const.GAIKAN_KAKUNIN5, srSpssekisouData)));//外観確認5
        params.add(newRev); //revision

        return params;
    }

    /**
     * 積層・SPS(sr_spssekisou)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSrSpssekisou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sr_spssekisou "
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
     * 積層・SPS_ｻﾌﾞ画面(sub_sr_spssekisou)登録処理
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
    private void insertSubSrSpssekisou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        String sql = "INSERT INTO sub_sr_spssekisou ("
                + "kojyo,lotno,edaban,setsuu1,setsuu2,setsuu3,setsuu4,setsuu5,setsuu6,setsuu7,setsuu8,setsuu9,setsuu10"
                + ",setsuu11,setsuu12,setsuu13,setsuu14,setsuu15,setsuu16,setsuu17,setsuu18,setsuu19,setsuu20,setsuu21"
                + ",setsuu22,setsuu23,setsuu24,setsuu25,setsuu26,setsuu27,setsuu28,setsuu29,setsuu30,setsuu31,setsuu32"
                + ",setsuu33,setsuu34,setsuu35,setsuu36,setsuu37,setsuu38,setsuu39,setsuu40,bikou1,bikou2,bikou3,bikou4"
                + ",bikou5,bikou6,bikou7,bikou8,bikou9,bikou10,bikou11,bikou12,bikou13,bikou14,bikou15,bikou16,bikou17"
                + ",bikou18,bikou19,bikou20,bikou21,bikou22,bikou23,bikou24,bikou25,bikou26,bikou27,bikou28,bikou29"
                + ",bikou30,bikou31,bikou32,bikou33,bikou34,bikou35,bikou36,bikou37,bikou38,bikou39,bikou40,torokunichiji"
                + ",kosinnichiji,revision"
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? "
                + ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        List<Object> params = setUpdateParameterSubSrSpssekisou(true, newRev, kojyo, lotNo, edaban, systemTime);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 積層・SPS_ｻﾌﾞ画面(sub_sr_spssekisou)更新処理
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
    private void updateSubSrSpssekisou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        String sql = "UPDATE sub_sr_spssekisou SET "
                + "setsuu1 = ?,setsuu2 = ?,setsuu3 = ?,setsuu4 = ?,setsuu5 = ?,setsuu6 = ?,setsuu7 = ?,setsuu8 = ?,"
                + "setsuu9 = ?,setsuu10 = ?,setsuu11 = ?,setsuu12 = ?,setsuu13 = ?,setsuu14 = ?,setsuu15 = ?,setsuu16 = ?,"
                + "setsuu17 = ?,setsuu18 = ?,setsuu19 = ?,setsuu20 = ?,setsuu21 = ?,setsuu22 = ?,setsuu23 = ?,setsuu24 = ?,"
                + "setsuu25 = ?,setsuu26 = ?,setsuu27 = ?,setsuu28 = ?,setsuu29 = ?,setsuu30 = ?,setsuu31 = ?,setsuu32 = ?,"
                + "setsuu33 = ?,setsuu34 = ?,setsuu35 = ?,setsuu36 = ?,setsuu37 = ?,setsuu38 = ?,setsuu39 = ?,setsuu40 = ?,"
                + "bikou1 = ?,bikou2 = ?,bikou3 = ?,bikou4 = ?,bikou5 = ?,bikou6 = ?,bikou7 = ?,bikou8 = ?,bikou9 = ?,"
                + "bikou10 = ?,bikou11 = ?,bikou12 = ?,bikou13 = ?,bikou14 = ?,bikou15 = ?,bikou16 = ?,bikou17 = ?,"
                + "bikou18 = ?,bikou19 = ?,bikou20 = ?,bikou21 = ?,bikou22 = ?,bikou23 = ?,bikou24 = ?,bikou25 = ?,"
                + "bikou26 = ?,bikou27 = ?,bikou28 = ?,bikou29 = ?,bikou30 = ?,bikou31 = ?,bikou32 = ?,bikou33 = ?,"
                + "bikou34 = ?,bikou35 = ?,bikou36 = ?,bikou37 = ?,bikou38 = ?,bikou39 = ?,bikou40 = ?,"
                + "kosinnichiji = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ?";

        List<Object> params = setUpdateParameterSubSrSpssekisou(false, newRev, "", "", "", systemTime);

        // 検索条件
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 積層・SPS_ｻﾌﾞ画面登録(tmp_sub_sr_spssekisou)更新値パラメータ設定
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
    private List<Object> setUpdateParameterSubSrSpssekisou(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo,
            String edaban, Timestamp systemTime) {
        List<Object> params = new ArrayList<>();

        // 子画面情報を取得
        GXHDO101C006 beanGXHDO101C006 = (GXHDO101C006) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C006);
        List<GXHDO101C006Model.HakuriInputData> makuatsuDataList = beanGXHDO101C006.getGxhdO101c006Model().getHakuriInputDataList();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番

        }
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(0).getSetsuuVal())); // ｾｯﾄ数1
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(1).getSetsuuVal())); // ｾｯﾄ数2
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(2).getSetsuuVal())); // ｾｯﾄ数3
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(3).getSetsuuVal())); // ｾｯﾄ数4
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(4).getSetsuuVal())); // ｾｯﾄ数5
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(5).getSetsuuVal())); // ｾｯﾄ数6
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(6).getSetsuuVal())); // ｾｯﾄ数7
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(7).getSetsuuVal())); // ｾｯﾄ数8
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(8).getSetsuuVal())); // ｾｯﾄ数9
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(9).getSetsuuVal())); // ｾｯﾄ数10
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(10).getSetsuuVal())); // ｾｯﾄ数11
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(11).getSetsuuVal())); // ｾｯﾄ数12
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(12).getSetsuuVal())); // ｾｯﾄ数13
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(13).getSetsuuVal())); // ｾｯﾄ数14
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(14).getSetsuuVal())); // ｾｯﾄ数15
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(15).getSetsuuVal())); // ｾｯﾄ数16
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(16).getSetsuuVal())); // ｾｯﾄ数17
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(17).getSetsuuVal())); // ｾｯﾄ数18
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(18).getSetsuuVal())); // ｾｯﾄ数19
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(19).getSetsuuVal())); // ｾｯﾄ数20
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(20).getSetsuuVal())); // ｾｯﾄ数21
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(21).getSetsuuVal())); // ｾｯﾄ数22
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(22).getSetsuuVal())); // ｾｯﾄ数23
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(23).getSetsuuVal())); // ｾｯﾄ数24
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(24).getSetsuuVal())); // ｾｯﾄ数25
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(25).getSetsuuVal())); // ｾｯﾄ数26
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(26).getSetsuuVal())); // ｾｯﾄ数27
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(27).getSetsuuVal())); // ｾｯﾄ数28
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(28).getSetsuuVal())); // ｾｯﾄ数29
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(29).getSetsuuVal())); // ｾｯﾄ数30
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(30).getSetsuuVal())); // ｾｯﾄ数31
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(31).getSetsuuVal())); // ｾｯﾄ数32
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(32).getSetsuuVal())); // ｾｯﾄ数33
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(33).getSetsuuVal())); // ｾｯﾄ数34
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(34).getSetsuuVal())); // ｾｯﾄ数35
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(35).getSetsuuVal())); // ｾｯﾄ数36
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(36).getSetsuuVal())); // ｾｯﾄ数37
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(37).getSetsuuVal())); // ｾｯﾄ数38
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(38).getSetsuuVal())); // ｾｯﾄ数39
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(39).getSetsuuVal())); // ｾｯﾄ数40
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(0).getBikouVal())); // 備考1
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(1).getBikouVal())); // 備考2
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(2).getBikouVal())); // 備考3
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(3).getBikouVal())); // 備考4
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(4).getBikouVal())); // 備考5
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(5).getBikouVal())); // 備考6
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(6).getBikouVal())); // 備考7
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(7).getBikouVal())); // 備考8
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(8).getBikouVal())); // 備考9
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(9).getBikouVal())); // 備考10
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(10).getBikouVal())); // 備考11
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(11).getBikouVal())); // 備考12
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(12).getBikouVal())); // 備考13
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(13).getBikouVal())); // 備考14
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(14).getBikouVal())); // 備考15
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(15).getBikouVal())); // 備考16
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(16).getBikouVal())); // 備考17
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(17).getBikouVal())); // 備考18
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(18).getBikouVal())); // 備考19
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(19).getBikouVal())); // 備考20
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(20).getBikouVal())); // 備考21
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(21).getBikouVal())); // 備考22
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(22).getBikouVal())); // 備考23
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(23).getBikouVal())); // 備考24
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(24).getBikouVal())); // 備考25
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(25).getBikouVal())); // 備考26
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(26).getBikouVal())); // 備考27
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(27).getBikouVal())); // 備考28
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(28).getBikouVal())); // 備考29
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(29).getBikouVal())); // 備考30
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(30).getBikouVal())); // 備考31
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(31).getBikouVal())); // 備考32
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(32).getBikouVal())); // 備考33
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(33).getBikouVal())); // 備考34
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(34).getBikouVal())); // 備考35
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(35).getBikouVal())); // 備考36
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(36).getBikouVal())); // 備考37
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(37).getBikouVal())); // 備考38
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(38).getBikouVal())); // 備考39
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(39).getBikouVal())); // 備考40

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
     * 積層・SPS_ｻﾌﾞ画面(sub_sr_spssekisou)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSubSrSpssekisou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {
        String sql = "DELETE FROM sub_sr_spssekisou "
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
     * [積層・SPS_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_spssekisou "
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
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B004Const.KAISHI_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B004Const.KAISHI_TIME);
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
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B004Const.SHURYO_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B004Const.SHURYO_TIME);
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
     * @param srSpssekisouData 積層・SPSデータ
     * @return DB値
     */
    private String getSrSpssekisouItemData(String itemId, SrSpssekisou srSpssekisouData) {
        switch (itemId) {
            // ｽﾘｯﾌﾟﾛｯﾄNo
            case GXHDO101B004Const.SLIP_LOTNO:
                return StringUtil.nullToBlank(srSpssekisouData.getTapelotno());
            // ﾛｰﾙNo1
            case GXHDO101B004Const.ROLL_NO1:
                return StringUtil.nullToBlank(srSpssekisouData.getTaperollno1());
            // ﾛｰﾙNo2
            case GXHDO101B004Const.ROLL_NO2:
                return StringUtil.nullToBlank(srSpssekisouData.getTaperollno2());
            // ﾛｰﾙNo3
            case GXHDO101B004Const.ROLL_NO3:
                return StringUtil.nullToBlank(srSpssekisouData.getTaperollno3());
            // 原料記号
            case GXHDO101B004Const.GENRYO_KIGOU:
                return StringUtil.nullToBlank(srSpssekisouData.getGenryoukigou());
            // ＰＥＴフィルム種類
            case GXHDO101B004Const.PET_FILM_SHURUI:
                return StringUtil.nullToBlank(srSpssekisouData.getPetfilmsyurui());
            // 固着シート
            case GXHDO101B004Const.KOTYAKU_SHEET:
                return StringUtil.nullToBlank(srSpssekisouData.getKotyakusheet());
            // 下端子号機
            case GXHDO101B004Const.SHITATANSHI_GOUKI:
                return StringUtil.nullToBlank(srSpssekisouData.getShitaTanshigouki());
            // 上端子号機
            case GXHDO101B004Const.UWATANSHI_GOUKI:
                return StringUtil.nullToBlank(srSpssekisouData.getUwaTanshigouki());
            // 下端子ﾌﾞｸ抜き
            case GXHDO101B004Const.SHITA_TANSHI_BUKUNUKI:
                return StringUtil.nullToBlank(srSpssekisouData.getShitaTanshiBukunuki());
            // 積層号機
            case GXHDO101B004Const.SEKISO_GOKI:
                return StringUtil.nullToBlank(srSpssekisouData.getGouki());
            // 下端子
            case GXHDO101B004Const.SHITA_TANSHI:
                return StringUtil.nullToBlank(srSpssekisouData.getShitaTanshi());
            // 剥離吸引圧
            case GXHDO101B004Const.HAKURI_KYUIN:
                return StringUtil.nullToBlank(srSpssekisouData.getHakuriKyuin());
            // 剥離速度
            case GXHDO101B004Const.HAKURI_SPEED:
                return StringUtil.nullToBlank(srSpssekisouData.getHakuriSpeed());
            // 剥離クリアランス
            case GXHDO101B004Const.HAKURI_CLEARRANCE:
                return StringUtil.nullToBlank(srSpssekisouData.getHakuriClearrance());
            // 剥離カット速度
            case GXHDO101B004Const.HAKURI_CUT_SPEED:
                return StringUtil.nullToBlank(srSpssekisouData.getHakuriCutSpeed());
            // 下パンチ温度
            case GXHDO101B004Const.SHITA_PANCHI_ONDO:
                return StringUtil.nullToBlank(srSpssekisouData.getShitaPanchiOndo());
            // 上パンチ温度
            case GXHDO101B004Const.UWA_PANCHI_ONDO:
                return StringUtil.nullToBlank(srSpssekisouData.getUwaPanchiOndo());
            // 加圧時間
            case GXHDO101B004Const.KAATU_JIKAN:
                return StringUtil.nullToBlank(srSpssekisouData.getKaatuJikan());
            // 加圧圧力
            case GXHDO101B004Const.KAATU_ATURYOKU:
                return StringUtil.nullToBlank(srSpssekisouData.getKaatuAturyoku());
            // 最終加圧力
            case GXHDO101B004Const.LAST_KAATURYOKU:
                return StringUtil.nullToBlank(srSpssekisouData.getLastKaaturyoku());
            // 最終加圧時間
            case GXHDO101B004Const.LAST_KAATUJIKAN:
                return StringUtil.nullToBlank(srSpssekisouData.getLastKaatuJikan());
            //空打ち
            case GXHDO101B004Const.KARAUTI:
                return StringUtil.nullToBlank(srSpssekisouData.getKarauti());
            //空打ち[秒]
            case GXHDO101B004Const.KARAUTIBYOU:
                return StringUtil.nullToBlank(srSpssekisouData.getKarautibyou());
            //空打ち[回]
            case GXHDO101B004Const.KARAUTIKAI:
                return StringUtil.nullToBlank(srSpssekisouData.getKarautikai());
            // 上端子
            case GXHDO101B004Const.UWA_TANSHI:
                return StringUtil.nullToBlank(srSpssekisouData.getUwaTanshi());
            //ｽﾞﾚ値
            case GXHDO101B004Const.ZURETI:
                return StringUtil.nullToBlank(srSpssekisouData.getZureti()); 
            //ﾜﾚ、ｳｷ、かみ込みなき事
            case GXHDO101B004Const.GAIKAN_KAKUNIN5:
                return StringUtil.nullToBlank(srSpssekisouData.getGaikanKakunin5());
            // 処理セット数
            case GXHDO101B004Const.SYORI_SETSUU:
                return StringUtil.nullToBlank(srSpssekisouData.getSyoriSetsuu());
            // 良品セット数
            case GXHDO101B004Const.RYOUHIN_SETSUU:
                return StringUtil.nullToBlank(srSpssekisouData.getRyouhinSetsuu());
            // 開始日
            case GXHDO101B004Const.KAISHI_DAY:
                return DateUtil.formattedTimestamp(srSpssekisouData.getStartdatetime(), "yyMMdd");
            // 開始時刻
            case GXHDO101B004Const.KAISHI_TIME:
                return DateUtil.formattedTimestamp(srSpssekisouData.getStartdatetime(), "HHmm");
            // 開始担当者
            case GXHDO101B004Const.KAISHI_TANTOSYA:
                return StringUtil.nullToBlank(srSpssekisouData.getStartTantosyacode());
            //開始確認者
            case GXHDO101B004Const.KAISHI_KAKUNINSYA:
                return StringUtil.nullToBlank(srSpssekisouData.getKakuninsya());
            // 終了日
            case GXHDO101B004Const.SHURYO_DAY:
                return DateUtil.formattedTimestamp(srSpssekisouData.getEnddatetime(), "yyMMdd");
            // 終了時刻
            case GXHDO101B004Const.SHURYO_TIME:
                return DateUtil.formattedTimestamp(srSpssekisouData.getEnddatetime(), "HHmm");
            // 終了担当者
            case GXHDO101B004Const.SHURYO_TANTOSYA:
                return StringUtil.nullToBlank(srSpssekisouData.getEndTantousyacode());
            // 端子テープ種類
            case GXHDO101B004Const.TTAPE_SYURUI:
                switch (StringUtil.nullToBlank(srSpssekisouData.getTanshiTapeSyurui())) {
                    case "0":
                        return "NG";
                    case "1":
                        return "OK";
                    default:
                        return "";
                }                
            // 剥離NG回数
            case GXHDO101B004Const.HNG_KAISUU:
                return StringUtil.nullToBlank(srSpssekisouData.getHngKaisuu());
            // 剥離NG_AVE
            case GXHDO101B004Const.HNG_AVE:
                return StringUtil.nullToBlank(srSpssekisouData.getHngKaisuuAve());
            // 画処NG回数
            case GXHDO101B004Const.GNG_KAISUU:
                return StringUtil.nullToBlank(srSpssekisouData.getGngKaisuu());
            // 画処NG_AVE
            case GXHDO101B004Const.GNG_KAISUUAVE:
                return StringUtil.nullToBlank(srSpssekisouData.getGngKaisuuAve());
            // 備考1
            case GXHDO101B004Const.BIKOU1:
                return StringUtil.nullToBlank(srSpssekisouData.getBikou1());
            // 備考2
            case GXHDO101B004Const.BIKOU2:
                return StringUtil.nullToBlank(srSpssekisouData.getBikou2());
            //KCPNo
            case GXHDO101B004Const.KCPNO:
                return StringUtil.nullToBlank(srSpssekisouData.getKcpno());

            default:
                return null;

        }
    }

    /**
     * 積層・SPS_仮登録(tmp_sr_spssekisou)登録処理(削除時)
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
    private void insertDeleteDataTmpSrSpssekisou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_spssekisou ("
                + "kojyo,lotno,edaban,tntapesyurui,tntapeno,tntapegenryou,gouki,startdatetime,enddatetime,sekisouzure"
                + ",tantousya,kakuninsya,sekisouzure2,bikou1,bikou2,bikou3,bikou4,bikou5,KCPNO,GoukiCode,TpLot,HakuriSpeed"
                + ",KanaOndo,DAturyoku,DKaatuJikan,CPressAturyoku,CPressKaatuJikan,CPressKankakuSosuu,LastKaaturyoku"
                + ",LastKaatuJikan,FourSplitTantou,STaoreryo1,STaoreryo2,STaoreryo3,STaoreryo4,STsunAve,STsunMax,STsunMin"
                + ",STsunSiguma,HNGKaisuu,GaikanTantou,CPressKaisuu,HNGKaisuuAve,GNGKaisuu,GNGKaisuuAve,tapelotno,taperollno1"
                + ",taperollno2,taperollno3,genryoukigou,petfilmsyurui,Kotyakugouki,Kotyakusheet,ShitaTanshigouki,UwaTanshigouki"
                + ",ShitaTanshiBukunuki,ShitaTanshi,HakuriKyuin,HakuriClearrance,HakuriCutSpeed,ShitaPanchiOndo,UwaPanchiOndo"
                + ",KaatuJikan,KaatuAturyoku,UwaTanshi,GaikanKakunin1,GaikanKakunin2,GaikanKakunin3,GaikanKakunin4,SyoriSetsuu"
                + ",RyouhinSetsuu,StartTantosyacode,EndTantousyacode,TanshiTapeSyurui,torokunichiji"
                + ",kosinnichiji,karauti,karautibyou,karautikai,zureti,GaikanKakunin5,revision,deleteflag"
                + ") SELECT "
                + "kojyo,lotno,edaban,tntapesyurui,tntapeno,tntapegenryou,gouki,startdatetime,enddatetime,sekisouzure"
                + ",tantousya,kakuninsya,sekisouzure2,bikou1,bikou2,bikou3,bikou4,bikou5,KCPNO,GoukiCode,TpLot,HakuriSpeed"
                + ",KanaOndo,DAturyoku,DKaatuJikan,CPressAturyoku,CPressKaatuJikan,CPressKankakuSosuu,LastKaaturyoku"
                + ",LastKaatuJikan,FourSplitTantou,STaoreryo1,STaoreryo2,STaoreryo3,STaoreryo4,STsunAve,STsunMax,STsunMin"
                + ",STsunSiguma,HNGKaisuu,GaikanTantou,CPressKaisuu,HNGKaisuuAve,GNGKaisuu,GNGKaisuuAve,tapelotno,taperollno1"
                + ",taperollno2,taperollno3,genryoukigou,petfilmsyurui,Kotyakugouki,Kotyakusheet,ShitaTanshigouki,UwaTanshigouki"
                + ",ShitaTanshiBukunuki,ShitaTanshi,HakuriKyuin,HakuriClearrance,HakuriCutSpeed,ShitaPanchiOndo,UwaPanchiOndo"
                + ",KaatuJikan,KaatuAturyoku,UwaTanshi,GaikanKakunin1,GaikanKakunin2,GaikanKakunin3,GaikanKakunin4,SyoriSetsuu"
                + ",RyouhinSetsuu,StartTantosyacode,EndTantousyacode,TanshiTapeSyurui"
                + ",?,?,karauti,karautibyou,karautikai,zureti,GaikanKakunin5,?,? "
                + "FROM sr_spssekisou "
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
     * 積層・SPS_ｻﾌﾞ画面仮登録(tmp_sub_sr_spssekisou)登録処理(削除時)
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
    private void insertDeleteDataTmpSubSrSpssekisou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        String sql = "INSERT INTO tmp_sub_sr_spssekisou ("
                + "kojyo,lotno,edaban,setsuu1,setsuu2,setsuu3,setsuu4,setsuu5,setsuu6,setsuu7,setsuu8,setsuu9,setsuu10,setsuu11"
                + ",setsuu12,setsuu13,setsuu14,setsuu15,setsuu16,setsuu17,setsuu18,setsuu19,setsuu20,setsuu21,setsuu22,setsuu23"
                + ",setsuu24,setsuu25,setsuu26,setsuu27,setsuu28,setsuu29,setsuu30,setsuu31,setsuu32,setsuu33,setsuu34,setsuu35"
                + ",setsuu36,setsuu37,setsuu38,setsuu39,setsuu40,bikou1,bikou2,bikou3,bikou4,bikou5,bikou6,bikou7,bikou8,bikou9"
                + ",bikou10,bikou11,bikou12,bikou13,bikou14,bikou15,bikou16,bikou17,bikou18,bikou19,bikou20,bikou21,bikou22,bikou23"
                + ",bikou24,bikou25,bikou26,bikou27,bikou28,bikou29,bikou30,bikou31,bikou32,bikou33,bikou34,bikou35,bikou36,bikou37"
                + ",bikou38,bikou39,bikou40,torokunichiji,kosinnichiji,revision,deleteflag"
                + ") SELECT "
                + "kojyo,lotno,edaban,setsuu1,setsuu2,setsuu3,setsuu4,setsuu5,setsuu6,setsuu7,setsuu8,setsuu9,setsuu10,setsuu11"
                + ",setsuu12,setsuu13,setsuu14,setsuu15,setsuu16,setsuu17,setsuu18,setsuu19,setsuu20,setsuu21,setsuu22,setsuu23"
                + ",setsuu24,setsuu25,setsuu26,setsuu27,setsuu28,setsuu29,setsuu30,setsuu31,setsuu32,setsuu33,setsuu34,setsuu35"
                + ",setsuu36,setsuu37,setsuu38,setsuu39,setsuu40,bikou1,bikou2,bikou3,bikou4,bikou5,bikou6,bikou7,bikou8,bikou9"
                + ",bikou10,bikou11,bikou12,bikou13,bikou14,bikou15,bikou16,bikou17,bikou18,bikou19,bikou20,bikou21,bikou22,bikou23"
                + ",bikou24,bikou25,bikou26,bikou27,bikou28,bikou29,bikou30,bikou31,bikou32,bikou33,bikou34,bikou35,bikou36,bikou37"
                + ",bikou38,bikou39,bikou40"
                + ",?,?,?,? "
                + "FROM sub_sr_spssekisou "
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

        if ("CT".equals(youtoType) || "CB".equals(youtoType)) {
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
