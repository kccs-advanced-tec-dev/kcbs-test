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
import jp.co.kccs.xhd.db.model.SrMksinkuukansou;
import jp.co.kccs.xhd.db.model.Jisseki;
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
 * システム名  品質情報管理システム<br>
 * <br>
 * 変更日      2019/11/05<br>
 * 計画書No    K1811-DS001<br>
 * 変更者      863 K.Zhang<br>
 * 変更理由    新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101B039(外部電極・ﾒｯｷ真空乾燥)ロジック
 *
 * @author 863 K.Zhang
 * @since  2019/11/05
 */
public class GXHDO101B039 implements IFormLogic {

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
                    GXHDO101B039Const.BTN_START_DATETIME_TOP,
                    GXHDO101B039Const.BTN_BARRELSTART_DATETIME_TOP,
                    GXHDO101B039Const.BTN_START_DATETIME_BOTTOM,
                    GXHDO101B039Const.BTN_BARRELSTART_DATETIME_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO101B039Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO101B039Const.BTN_INSERT_TOP,
                    GXHDO101B039Const.BTN_DELETE_TOP,
                    GXHDO101B039Const.BTN_UPDATE_TOP,
                    GXHDO101B039Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO101B039Const.BTN_INSERT_BOTTOM,
                    GXHDO101B039Const.BTN_DELETE_BOTTOM,
                    GXHDO101B039Const.BTN_UPDATE_BOTTOM));

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
     * 仮登録処理(データチェック処理)
     *
     * @param processData 処理データ
     * @return 処理データ
     */
    public ProcessData checkDataTempResist(ProcessData processData) {
        
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
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // 品質DB登録実績データ取得
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, paramJissekino, formId);
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
                insertFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, paramJissekino, JOTAI_FLG_KARI_TOROKU, systemTime);
            } else {
                rev = new BigDecimal(processData.getInitRev());
                // 最新のリビジョンを採番
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, paramJissekino, formId);

                // 品質DB登録実績更新処理
                updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_KARI_TOROKU, systemTime, paramJissekino);
            }

            if (StringUtil.isEmpty(processData.getInitJotaiFlg()) || JOTAI_FLG_SAKUJO.equals(processData.getInitJotaiFlg())) {

                // ﾒｯｷ真空乾燥_仮登録登録処理
                insertTmpSrMksinkuukansou(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList(), processData);

            } else {

                // ﾒｯｷ真空乾燥_仮登録更新処理
                updateTmpSrMksinkuukansou(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList(), processData);

            }

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, paramJissekino, "0", kikakuError.getKikakuchiInputErrorInfoList());
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
    public ProcessData checkDataResist(ProcessData processData) {

        // 項目のチェック処理を行う。
        ErrorMessageInfo checkItemErrorInfo = checkItemResistCorrect(processData);
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
        processData.setMethod("doResist");

        return processData;
    }

    /**
     * 登録・修正項目チェック
     *
     * @param processData 処理データ
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkItemResistCorrect(ProcessData processData) {
               
        QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
        // ﾊﾟﾗﾒｰﾀﾃﾞｰﾀ情報の取得
        String strfxhbm03List = "";
        //KCPNO
        FXHDD01 itemKcpno = getItemRow(processData.getItemList(), GXHDO101B039Const.KCPNO);
        if(!StringUtil.isEmpty(itemKcpno.getValue()) && itemKcpno.getValue().length() > 9){
            String kcpno9 = itemKcpno.getValue().substring(8, 9);
            // ﾊﾞﾚﾙ洗浄必須ﾁｪｯｸ処理
            Map fxhbm03Data21 = loadFxhbm03Data(queryRunnerDoc,21);
            if(fxhbm03Data21 != null && !fxhbm03Data21.isEmpty()){
                strfxhbm03List = StringUtil.nullToBlank(getMapData(fxhbm03Data21, "data"));
                String fxhbm03Data[] = strfxhbm03List.split(",");
                for(int i = 0; i < fxhbm03Data.length; i++){
                    if(!StringUtil.isEmpty(kcpno9) && kcpno9.equals(fxhbm03Data[i])){
                        // ﾊﾞﾚﾙ洗浄開始日
                        FXHDD01 itemBarrelkaishiday = getItemRow(processData.getItemList(), GXHDO101B039Const.BARRELKAISHI_DAY);
                        // ﾊﾞﾚﾙ洗浄開始時刻
                        FXHDD01 itemBarrelkaishitime = getItemRow(processData.getItemList(), GXHDO101B039Const.BARRELKAISHI_TIME);
                        // ﾊﾞﾚﾙ洗浄担当者
                        FXHDD01 itemBarrelkaishitantosya = getItemRow(processData.getItemList(), GXHDO101B039Const.BARRELKAISHI_TANTOSYA);
                        if(StringUtil.isEmpty(StringUtil.trimAll(itemBarrelkaishiday.getValue()))){
                            //エラー発生時
                            List<FXHDD01> errFxhdd01List = Arrays.asList(itemBarrelkaishiday);
                            return MessageUtil.getErrorMessageInfo("XHD-000003", true, true, errFxhdd01List, itemBarrelkaishiday.getLabel1());
                        }
                        if(StringUtil.isEmpty(StringUtil.trimAll(itemBarrelkaishitime.getValue()))){
                            //エラー発生時
                            List<FXHDD01> errFxhdd01List = Arrays.asList(itemBarrelkaishitime);
                            return MessageUtil.getErrorMessageInfo("XHD-000003", true, true, errFxhdd01List, itemBarrelkaishitime.getLabel1());
                        }
                        if(StringUtil.isEmpty(StringUtil.trimAll(itemBarrelkaishitantosya.getValue()))){
                            //エラー発生時
                            List<FXHDD01> errFxhdd01List = Arrays.asList(itemBarrelkaishitantosya);
                            return MessageUtil.getErrorMessageInfo("XHD-000003", true, true, errFxhdd01List, itemBarrelkaishitantosya.getLabel1());
                        }
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
            int paramJissekino = (Integer) session.getAttribute("jissekino");
            String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
            String lotNo8 = lotNo.substring(3, 11); //ﾛｯﾄNo(8桁)
            String edaban = lotNo.substring(11, 14); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // 品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, paramJissekino, formId);
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
                insertFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, paramJissekino, JOTAI_FLG_TOROKUZUMI, systemTime);
            } else {
                rev = new BigDecimal(processData.getInitRev());
                // 最新のリビジョンを採番
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, paramJissekino, formId);

                // 品質DB登録実績更新処理
                updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_TOROKUZUMI, systemTime, paramJissekino);
            }

            // 仮登録状態の場合、仮登録のデータを削除する。
            SrMksinkuukansou tmpSrMksinkuukansou = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {
                
                // 更新前の値を取得
                List<SrMksinkuukansou> srMksinkuukansouList = getSrMksinkuukansouData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo8, edaban, paramJissekino);
                if (!srMksinkuukansouList.isEmpty()) {
                    tmpSrMksinkuukansou = srMksinkuukansouList.get(0);
                }
                
                deleteTmpSrMksinkuukansou(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban, paramJissekino);
            }

            // ﾒｯｷ真空乾燥_登録処理
            insertSrMksinkuukansou(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList(), tmpSrMksinkuukansou, processData);

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, paramJissekino, "0", kikakuError.getKikakuchiInputErrorInfoList());
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
        ErrorMessageInfo checkItemErrorInfo = checkItemResistCorrect(processData);
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
        processData.setUserAuthParam(GXHDO101B039Const.USER_AUTH_UPDATE_PARAM);

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
            int paramJissekino = (Integer) session.getAttribute("jissekino");
            String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
            String lotNo8 = lotNo.substring(3, 11); //ﾛｯﾄNo(8桁)
            String edaban = lotNo.substring(11, 14); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // 品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, paramJissekino, formId);
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
            BigDecimal newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, paramJissekino, formId);

            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            // 品質DB登録実績更新処理
            updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_TOROKUZUMI, systemTime, paramJissekino);

            // ﾒｯｷ真空乾燥_更新処理
            updateSrMksinkuukansou(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList(), processData);

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, paramJissekino, "0", kikakuError.getKikakuchiInputErrorInfoList());
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
        processData.setUserAuthParam(GXHDO101B039Const.USER_AUTH_DELETE_PARAM);

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

            // 品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, paramJissekino, formId);
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
            BigDecimal newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, paramJissekino, formId);

            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            // 品質DB登録実績更新処理
            updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_SAKUJO, systemTime, paramJissekino);

            // ﾒｯｷ真空乾燥_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo8, edaban, paramJissekino);
            insertDeleteDataTmpSrMksinkuukansou(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, paramJissekino, systemTime);

            // ﾒｯｷ真空乾燥_削除処理
            deleteSrMksinkuukansou(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban, paramJissekino);

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
                        GXHDO101B039Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO101B039Const.BTN_DELETE_BOTTOM,
                        GXHDO101B039Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B039Const.BTN_START_DATETIME_BOTTOM,
                        GXHDO101B039Const.BTN_BARRELSTART_DATETIME_BOTTOM,
                        GXHDO101B039Const.BTN_EDABAN_COPY_TOP,
                        GXHDO101B039Const.BTN_DELETE_TOP,
                        GXHDO101B039Const.BTN_UPDATE_TOP,
                        GXHDO101B039Const.BTN_START_DATETIME_TOP,
                        GXHDO101B039Const.BTN_BARRELSTART_DATETIME_TOP
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B039Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B039Const.BTN_INSERT_BOTTOM,
                        GXHDO101B039Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B039Const.BTN_INSERT_TOP));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO101B039Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B039Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO101B039Const.BTN_INSERT_BOTTOM,
                        GXHDO101B039Const.BTN_START_DATETIME_BOTTOM,
                        GXHDO101B039Const.BTN_BARRELSTART_DATETIME_BOTTOM,
                        GXHDO101B039Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B039Const.BTN_EDABAN_COPY_TOP,
                        GXHDO101B039Const.BTN_INSERT_TOP,
                        GXHDO101B039Const.BTN_START_DATETIME_TOP,
                        GXHDO101B039Const.BTN_BARRELSTART_DATETIME_TOP
                ));

                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B039Const.BTN_DELETE_BOTTOM,
                        GXHDO101B039Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B039Const.BTN_DELETE_TOP,
                        GXHDO101B039Const.BTN_UPDATE_TOP));

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
            case GXHDO101B039Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO101B039Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempResist";
                break;
            // 登録
            case GXHDO101B039Const.BTN_INSERT_TOP:
            case GXHDO101B039Const.BTN_INSERT_BOTTOM:
                method = "checkDataResist";
                break;
            // 枝番コピー
            case GXHDO101B039Const.BTN_EDABAN_COPY_TOP:
            case GXHDO101B039Const.BTN_EDABAN_COPY_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 修正
            case GXHDO101B039Const.BTN_UPDATE_TOP:
            case GXHDO101B039Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO101B039Const.BTN_DELETE_TOP:
            case GXHDO101B039Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 開始日時
            case GXHDO101B039Const.BTN_START_DATETIME_TOP:
            case GXHDO101B039Const.BTN_START_DATETIME_BOTTOM:
                method = "setKaishiDateTime";
                break;
            // ﾊﾞﾚﾙ洗浄開始日時
            case GXHDO101B039Const.BTN_BARRELSTART_DATETIME_TOP:
            case GXHDO101B039Const.BTN_BARRELSTART_DATETIME_BOTTOM:
                method = "setBarrelstartDateTime";
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
        int paramJissekino = (Integer) session.getAttribute("jissekino");
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

        //仕掛情報の取得
        Map shikakariData = loadShikakariData(queryRunnerWip, lotNo);
        if (shikakariData == null || shikakariData.isEmpty()) {
            errorMessageList.add(MessageUtil.getMessage("XHD-000029"));
        }
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); //ﾛｯﾄ区分ｺｰﾄﾞ
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));// ｵｰﾅｰｺｰﾄﾞ
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

        // 処理数の取得
         String syorisuu = null;
         
        //ﾃﾞｰﾀの取得
         String strfxhbm03List = "";
         
        Map fxhbm03Data = loadFxhbm03Data(queryRunnerDoc,20);
        if (fxhbm03Data != null && !fxhbm03Data.isEmpty()) {
             strfxhbm03List = StringUtil.nullToBlank(getMapData(fxhbm03Data, "data"));
             String fxhbm03DataArr []= strfxhbm03List.split(",");
             
            // 実績情報の取得
            List<Jisseki> jissekiData = loadJissekiData(queryRunnerDoc, lotNo, fxhbm03DataArr);
            if(jissekiData != null && jissekiData.size() > 0){
                int dbShorisu = jissekiData.get(0).getSyorisuu(); //処理数  
                if(dbShorisu > 0){
                    syorisuu = String.valueOf(dbShorisu);
                
                }
            }
        }
        
        // 入力項目の情報を画面にセットする。
        if (!setInputItemData(processData, queryRunnerDoc, queryRunnerQcdb, lotNo, formId, paramJissekino)) {
            // エラー発生時は処理を中断
            processData.setFatalError(true);
            processData.setInitMessageList(Arrays.asList(MessageUtil.getMessage("XHD-000038")));
            return processData;
        }

        // 画面に取得した情報をセットする。(入力項目以外)
        setViewItemData(processData, lotKbnMasData, ownerMasData, shikakariData, lotNo, syorisuu);

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
    private void setViewItemData(ProcessData processData, Map lotKbnMasData, Map ownerMasData, Map shikakariData, String lotNo ,String syorisuu) {
        
        // ロットNo
        this.setItemData(processData, GXHDO101B039Const.LOTNO, lotNo);
        // KCPNO
        this.setItemData(processData, GXHDO101B039Const.KCPNO, StringUtil.nullToBlank(getMapData(shikakariData, "kcpno")));
        // 客先
        this.setItemData(processData, GXHDO101B039Const.TOKUISAKI, StringUtil.nullToBlank(getMapData(shikakariData, "tokuisaki")));

        // ロット区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); //ﾛｯﾄ区分ｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B039Const.LOT_KUBUN, "");
        } else {
            String lotKubun = StringUtil.nullToBlank(getMapData(lotKbnMasData, "lotkubun"));
            this.setItemData(processData, GXHDO101B039Const.LOT_KUBUN, lotkubuncode + ":" + lotKubun);
        }

        // オーナー
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));// ｵｰﾅｰｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B039Const.OWNER, "");
        } else {
            String owner = StringUtil.nullToBlank(getMapData(ownerMasData, "ownername"));
            this.setItemData(processData, GXHDO101B039Const.OWNER, ownercode + ":" + owner);
        }
        
        //処理数
        this.setItemData(processData, GXHDO101B039Const.SYORISUU, syorisuu);
        
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

        List<SrMksinkuukansou> srMksinkuukansouDataList = new ArrayList<>();
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
                return true;
            }

            // ﾒｯｷ真空乾燥データ取得
            srMksinkuukansouDataList = getSrMksinkuukansouData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban, jissekino);
            if (srMksinkuukansouDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srMksinkuukansouDataList.isEmpty()) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srMksinkuukansouDataList.get(0));

        return true;

    }

    /**
     * メイン画面データ設定処理
     *
     * @param processData 処理制御データ
     * @param srMksinkuukansouData ﾒｯｷ真空乾燥データ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrMksinkuukansou srMksinkuukansouData) {

        // 号機
        this.setItemData(processData, GXHDO101B039Const.GOUKI, getSrMksinkuukansouItemData(GXHDO101B039Const.GOUKI, srMksinkuukansouData));
        // 乾燥時間
        this.setItemData(processData, GXHDO101B039Const.KANSOUJIKAN, getSrMksinkuukansouItemData(GXHDO101B039Const.KANSOUJIKAN, srMksinkuukansouData));
        // 温度
        this.setItemData(processData, GXHDO101B039Const.ONDO, getSrMksinkuukansouItemData(GXHDO101B039Const.ONDO, srMksinkuukansouData));
        // 真空度
        this.setItemData(processData, GXHDO101B039Const.SINKUUDO, getSrMksinkuukansouItemData(GXHDO101B039Const.SINKUUDO, srMksinkuukansouData));
        // 開始日
        this.setItemData(processData, GXHDO101B039Const.KAISHI_DAY, getSrMksinkuukansouItemData(GXHDO101B039Const.KAISHI_DAY, srMksinkuukansouData));
        // 開始時間
        this.setItemData(processData, GXHDO101B039Const.KAISHI_TIME, getSrMksinkuukansouItemData(GXHDO101B039Const.KAISHI_TIME, srMksinkuukansouData));
        // 開始担当者
        this.setItemData(processData, GXHDO101B039Const.KAISHI_TANTOSYA, getSrMksinkuukansouItemData(GXHDO101B039Const.KAISHI_TANTOSYA, srMksinkuukansouData));
        // 作業場所
        this.setItemData(processData, GXHDO101B039Const.SAGYOUBASYO, getSrMksinkuukansouItemData(GXHDO101B039Const.SAGYOUBASYO, srMksinkuukansouData));
        // ﾊﾞﾚﾙ洗浄開始日
        this.setItemData(processData, GXHDO101B039Const.BARRELKAISHI_DAY, getSrMksinkuukansouItemData(GXHDO101B039Const.BARRELKAISHI_DAY, srMksinkuukansouData));
        // ﾊﾞﾚﾙ洗浄開始時刻
        this.setItemData(processData, GXHDO101B039Const.BARRELKAISHI_TIME, getSrMksinkuukansouItemData(GXHDO101B039Const.BARRELKAISHI_TIME, srMksinkuukansouData));
        // ﾊﾞﾚﾙ洗浄担当者
        this.setItemData(processData, GXHDO101B039Const.BARRELKAISHI_TANTOSYA, getSrMksinkuukansouItemData(GXHDO101B039Const.BARRELKAISHI_TANTOSYA, srMksinkuukansouData));
        // 備考1
        this.setItemData(processData, GXHDO101B039Const.BIKO1, getSrMksinkuukansouItemData(GXHDO101B039Const.BIKO1, srMksinkuukansouData));
        // 備考2
        this.setItemData(processData, GXHDO101B039Const.BIKO2, getSrMksinkuukansouItemData(GXHDO101B039Const.BIKO2, srMksinkuukansouData));
 
    }

    /**
     * ﾒｯｷ真空乾燥の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @param jissekino 実績No
     * @return ﾒｯｷ真空乾燥登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrMksinkuukansou> getSrMksinkuukansouData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrMksinkuukansou(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
        } else {
            return loadTmpSrMksinkuukansou(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
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
     * @return 設計データ関連付けマップ
     */
    private Map getMapSekkeiAssociation() {
        Map<String, String> map = new LinkedHashMap<>();
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
     * [実績]から、ﾃﾞｰﾀを取得
     * @param queryRunnerDoc オブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param date ﾊﾟﾗﾒｰﾀﾃﾞｰﾀ(検索キー)
     * @return 取得データ
     * @throws SQLException 
     */
     private List<Jisseki> loadJissekiData(QueryRunner queryRunnerDoc, String lotNo, String[] data) throws SQLException {
         

        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 11);
        String lotNo3 = lotNo.substring(11, 14);
        
        List<String> dataList= new ArrayList<>(Arrays.asList(data));
        
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
        return queryRunnerDoc.query(sql, beanHandler, params.toArray());
    }
     
     
    /**
     * [ﾊﾟﾗﾒｰﾀﾏｽﾀ]から、ﾃﾞｰﾀを取得
     * @param queryRunnerDoc オブジェクト
     * @param selectNo 処理区分
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhbm03Data(QueryRunner queryRunnerDoc,Integer selectNo) {
        try {

            // ﾊﾟﾗﾒｰﾀﾏｽﾀデータの取得
             String sql = "SELECT data "
                        + " FROM fxhbm03 "
                        + " WHERE user_name = 'common_user' AND ";
                        if(selectNo == 20){
                            sql= sql + " key = '工程コード_ﾒｯｷ社内' ";
                        }
                        if(selectNo == 21){
                            sql= sql + " key = 'xhd_外部電極ﾒｯｷ真空乾燥_ﾊﾞﾚﾙ洗浄_必須判定ｺｰﾄﾞ' ";
                        }
            return queryRunnerDoc.query(sql, new MapHandler());
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
        return null;
                
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
        String sql = "SELECT kcpno, oyalotedaban, tokuisaki, lotkubuncode, ownercode, lotpre, tanijuryo "
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
     * [ﾒｯｷ真空乾燥]から、ﾃﾞｰﾀを取得
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
    private List<SrMksinkuukansou> loadSrMksinkuukansou(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, String rev) throws SQLException {

        String sql = "SELECT GoukiCode ,Kojyo ,LotNo ,EdaBan ,Syorikaisuu ,KCPNO ,TantousyaCode ,KansouJikan ,Ondo ,Sinkuudo ,"
                + " KaisiNichiji ,Bikou ,TourokuNichiji ,KousinNichiji ,tokuisaki ,lotkubuncode ,ownercode ,"
                + " syorisuu ,sagyoubasyo ,barrelkaishinichiji ,barreltantousya,bikou2 ,revision ,'0' AS deleteflag "
                + "FROM sr_mksinkuukansou "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? AND Syorikaisuu = ? ";
        
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
        mapping.put("GoukiCode", "goukicode"); //号機
        mapping.put("Kojyo", "kojyo"); //工場ｺｰﾄﾞ
        mapping.put("LotNo", "lotno"); //ﾛｯﾄNo
        mapping.put("EdaBan", "edaban"); //枝番
        mapping.put("Syorikaisuu", "syorikaisuu"); //回数
        mapping.put("KCPNO", "kcpno"); //KCPNO
        mapping.put("TantousyaCode", "tantousyacode"); //開始担当者
        mapping.put("KansouJikan", "kansoujikan"); //乾燥時間
        mapping.put("Ondo", "ondo"); //温度
        mapping.put("Sinkuudo", "sinkuudo"); //真空度
        mapping.put("KaisiNichiji", "kaisinichiji"); //開始日時
        mapping.put("Bikou", "bikou"); //備考1
        mapping.put("TourokuNichiji", "tourokunichiji"); //登録日時
        mapping.put("KousinNichiji", "kousinnichiji"); //更新日時
        mapping.put("tokuisaki", "tokuisaki"); //客先
        mapping.put("lotkubuncode", "lotkubuncode"); //ﾛｯﾄ区分
        mapping.put("ownercode", "ownercode"); //ｵｰﾅｰ
        mapping.put("syorisuu", "syorisuu"); //処理数
        mapping.put("sagyoubasyo", "sagyoubasyo"); //作業場所
        mapping.put("barrelkaishinichiji", "barrelkaishinichiji"); //ﾊﾞﾚﾙ洗浄開始日時
        mapping.put("barreltantousya", "barreltantousya"); //ﾊﾞﾚﾙ洗浄担当者
        mapping.put("bikou2", "bikou2"); //備考2
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrMksinkuukansou>> beanHandler = new BeanListHandler<>(SrMksinkuukansou.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [ﾒｯｷ真空乾燥_仮登録]から、ﾃﾞｰﾀを取得
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
    private List<SrMksinkuukansou> loadTmpSrMksinkuukansou(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, String rev) throws SQLException {
        
        String sql = "SELECT GoukiCode ,Kojyo ,LotNo ,EdaBan ,Syorikaisuu ,KCPNO ,TantousyaCode ,KansouJikan ,Ondo ,"
                + " Sinkuudo ,KaisiNichiji ,Bikou ,TourokuNichiji ,KousinNichiji ,"
                + " tokuisaki ,lotkubuncode ,ownercode ,syorisuu ,sagyoubasyo ,barrelkaishinichiji ,barreltantousya ,bikou2 ,revision ,deleteflag "
                + "FROM tmp_sr_mksinkuukansou "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? AND Syorikaisuu = ? AND deleteflag = ? ";
        
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
        mapping.put("GoukiCode", "goukicode"); //号機
        mapping.put("Kojyo", "kojyo"); //工場ｺｰﾄﾞ
        mapping.put("LotNo", "lotno"); //ﾛｯﾄNo
        mapping.put("EdaBan", "edaban"); //枝番
        mapping.put("Syorikaisuu", "syorikaisuu"); //回数
        mapping.put("KCPNO", "kcpno"); //KCPNO
        mapping.put("TantousyaCode", "tantousyacode"); //開始担当者
        mapping.put("KansouJikan", "kansoujikan"); //乾燥時間
        mapping.put("Ondo", "ondo"); //温度
        mapping.put("Sinkuudo", "sinkuudo"); //真空度
        mapping.put("KaisiNichiji", "kaisinichiji"); //開始日時
        mapping.put("Bikou", "bikou"); //備考1
        mapping.put("TourokuNichiji", "tourokunichiji"); //登録日時
        mapping.put("KousinNichiji", "kousinnichiji"); //更新日時
        mapping.put("tokuisaki", "tokuisaki"); //客先
        mapping.put("lotkubuncode", "lotkubuncode"); //ﾛｯﾄ区分
        mapping.put("ownercode", "ownercode"); //ｵｰﾅｰ
        mapping.put("syorisuu", "syorisuu"); //処理数
        mapping.put("sagyoubasyo", "sagyoubasyo"); //作業場所
        mapping.put("barrelkaishinichiji", "barrelkaishinichiji"); //ﾊﾞﾚﾙ洗浄開始日時
        mapping.put("barreltantousya", "barreltantousya"); //ﾊﾞﾚﾙ洗浄担当者
        mapping.put("bikou2", "bikou2"); //備考2
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrMksinkuukansou>> beanHandler = new BeanListHandler<>(SrMksinkuukansou.class, rowProcessor);

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
            Map shikakariData = loadShikakariData(queryRunnerWip, lotNo);
            String oyalotEdaban = StringUtil.nullToBlank(getMapData(shikakariData, "oyalotedaban")); //親ﾛｯﾄ枝番

            // 品質DB登録実績データ取得
            Map fxhdd03RevInfo = loadFxhdd03RevInfo(queryRunnerDoc, kojyo, lotNo8, oyalotEdaban, paramJissekino, formId);
            if (fxhdd03RevInfo == null || fxhdd03RevInfo.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            String jotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd03RevInfo, "jotai_flg"));
            
            if (!(JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg) || JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg))) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // ﾒｯｷ真空乾燥データ取得
            List<SrMksinkuukansou> srMksinkuukansouDataList = getSrMksinkuukansouData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban, paramJissekino);
            if (srMksinkuukansouDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // メイン画面データ設定
            setInputItemDataMainForm(processData, srMksinkuukansouDataList.get(0));

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
     * @param srMksinkuukansouData ﾒｯｷ真空乾燥データ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrMksinkuukansou srMksinkuukansouData) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srMksinkuukansouData != null) {
            // 元データが存在する場合元データより取得
            return getSrMksinkuukansouItemData(itemId, srMksinkuukansouData);
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
     * @param jissekino 実績No
     * @throws SQLException 例外ｴﾗｰ
     */
    private void updateFxhdd03(QueryRunner queryRunnerDoc, Connection conDoc, String tantoshaCd, String formId, BigDecimal rev,
            String kojyo, String lotNo, String edaban, String jotaiFlg, Timestamp systemTime, int jissekino) throws SQLException {
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
     * ﾒｯｷ真空乾燥_仮登録(tmp_sr_mksinkuukansou)登録処理
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
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void insertTmpSrMksinkuukansou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, ProcessData processData) throws SQLException {

        String sql = "INSERT INTO tmp_sr_mksinkuukansou ("
                + " GoukiCode ,Kojyo ,LotNo ,EdaBan ,Syorikaisuu ,KCPNO ,TantousyaCode ,KansouJikan ,Ondo ,Sinkuudo ,KaisiNichiji ,"
                + " Bikou ,TourokuNichiji ,KousinNichiji, tokuisaki ,lotkubuncode ,ownercode ,"
                + " syorisuu ,sagyoubasyo ,barrelkaishinichiji ,barreltantousya ,bikou2 ,revision ,deleteflag"
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrMksinkuukansou(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, itemList, null, jissekino, processData);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ﾒｯｷ真空乾燥_仮登録(tmp_sr_mksinkuukansou)更新処理
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
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void updateTmpSrMksinkuukansou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, ProcessData processData) throws SQLException {

        String sql = "UPDATE tmp_sr_mksinkuukansou SET "
                + " GoukiCode = ?,KCPNO = ?,TantousyaCode = ?,KansouJikan = ?,Ondo = ?, "
                + " Sinkuudo = ?,KaisiNichiji = ?,Bikou = ?,KousinNichiji = ?,tokuisaki = ?,lotkubuncode = ?,"
                + " ownercode = ?,syorisuu = ?,sagyoubasyo = ?,barrelkaishinichiji = ?,barreltantousya = ?,bikou2 = ?,revision = ?,deleteflag = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND Syorikaisuu = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrMksinkuukansou> srSrMksinkuukansouList = getSrMksinkuukansouData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekino);
        SrMksinkuukansou srMksinkuukansou = null;
        if (!srSrMksinkuukansouList.isEmpty()) {
            srMksinkuukansou = srSrMksinkuukansouList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrMksinkuukansou(false, newRev, 0, "", "", "", systemTime, itemList, srMksinkuukansou, jissekino, processData);

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
     * ﾒｯｷ真空乾燥_仮登録(tmp_sr_mksinkuukansou)削除処理
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
    private void deleteTmpSrMksinkuukansou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        String sql = "DELETE FROM tmp_sr_mksinkuukansou "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND Syorikaisuu = ? AND revision = ?";

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
     * ﾒｯｷ真空乾燥_仮登録(tmp_sr_mksinkuukansou)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srMksinkuukansouData ﾒｯｷ真空乾燥データ
     * @param jissekino 実績No
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrMksinkuukansou(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList, SrMksinkuukansou srMksinkuukansouData, int jissekino, ProcessData processData) {
        List<Object> params = new ArrayList<>();
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B039Const.GOUKI, srMksinkuukansouData))); // 号機
        
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
            params.add(jissekino); // 回数
        }

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B039Const.KCPNO, srMksinkuukansouData))); //KCPNO        

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B039Const.KAISHI_TANTOSYA, srMksinkuukansouData))); // 開始担当者
        
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B039Const.KANSOUJIKAN, srMksinkuukansouData))); // 乾燥時間
        
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B039Const.ONDO, srMksinkuukansouData))); // 温度
        
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B039Const.SINKUUDO, srMksinkuukansouData))); // 真空度
        
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B039Const.KAISHI_DAY, srMksinkuukansouData),
            getItemData(itemList, GXHDO101B039Const.KAISHI_TIME, srMksinkuukansouData))); //開始日時
        
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B039Const.BIKO1, srMksinkuukansouData))); // 備考1
        
        if (isInsert) {
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時
        } else {
            params.add(systemTime); //更新日時
        }
        
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B039Const.TOKUISAKI, srMksinkuukansouData))); // 客先

        params.add(DBUtil.stringToStringObjectDefaultNull(StringUtil.nullToBlank(processData.getHiddenDataMap().get("lotkubuncode")))); // ﾛｯﾄ区分
        
        params.add(DBUtil.stringToStringObjectDefaultNull(StringUtil.nullToBlank(processData.getHiddenDataMap().get("ownercode")))); //ｵｰﾅｰ
        
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B039Const.SYORISUU, srMksinkuukansouData))); // 処理数
        
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B039Const.SAGYOUBASYO, srMksinkuukansouData))); //作業場所
        
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B039Const.BARRELKAISHI_DAY, srMksinkuukansouData),
            getItemData(itemList, GXHDO101B039Const.BARRELKAISHI_TIME, srMksinkuukansouData))); // ﾊﾞﾚﾙ洗浄開始日時
        
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B039Const.BARRELKAISHI_TANTOSYA, srMksinkuukansouData))); //ﾊﾞﾚﾙ洗浄担当者
        
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B039Const.BIKO2, srMksinkuukansouData))); //備考2
        
        params.add(newRev); //revision
        params.add(deleteflag); //削除ﾌﾗｸﾞ
        
        return params;
    }

    /**
     * ﾒｯｷ真空乾燥(sr_mksinkuukansou)登録処理
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
     * @param tmpSrMksinkuukansou 仮登録データ
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void insertSrMksinkuukansou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino,Timestamp systemTime, List<FXHDD01> itemList, SrMksinkuukansou tmpSrMksinkuukansou, ProcessData processData) throws SQLException {

        String sql = "INSERT INTO sr_mksinkuukansou ("
                + "GoukiCode ,Kojyo ,LotNo ,EdaBan ,Syorikaisuu ,KCPNO ,TantousyaCode ,KansouJikan ,Ondo ,"
                + " Sinkuudo ,KaisiNichiji ,Bikou ,TourokuNichiji ,KousinNichiji ,"
                + " tokuisaki ,lotkubuncode ,ownercode ,syorisuu ,sagyoubasyo ,barrelkaishinichiji ,barreltantousya ,bikou2 ,revision "
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrMksinkuukansou(true, newRev, kojyo, lotNo, edaban, jissekino, systemTime, itemList, tmpSrMksinkuukansou, processData);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ﾒｯｷ真空乾燥(sr_mksinkuukansou)更新処理
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
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void updateSrMksinkuukansou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, ProcessData processData) throws SQLException {
        String sql = "UPDATE sr_mksinkuukansou SET "
                + " GoukiCode = ?,KCPNO = ?,TantousyaCode = ?,KansouJikan = ?,Ondo = ?,"
                + " Sinkuudo = ?,KaisiNichiji = ?,Bikou = ?,KousinNichiji = ?,tokuisaki = ?,lotkubuncode = ?,"
                + " ownercode = ?,syorisuu = ?,sagyoubasyo = ?,barrelkaishinichiji = ?,barreltantousya = ?,bikou2 = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND Syorikaisuu = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrMksinkuukansou> srMksinkuukansouList = getSrMksinkuukansouData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekino);
        SrMksinkuukansou srMksinkuukansou = null;
        if (!srMksinkuukansouList.isEmpty()) {
            srMksinkuukansou = srMksinkuukansouList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrMksinkuukansou(false, newRev, "", "", "", jissekino, systemTime, itemList, srMksinkuukansou, processData);

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
     * ﾒｯｷ真空乾燥(sr_mksinkuukansou)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srMksinkuukansouData ﾒｯｷ真空乾燥データ
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrMksinkuukansou(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            int jissekino, Timestamp systemTime, List<FXHDD01> itemList, SrMksinkuukansou srMksinkuukansouData, ProcessData processData ) {
        List<Object> params = new ArrayList<>();

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B039Const.GOUKI, srMksinkuukansouData))); // 号機
        
        if (isInsert) {
            params.add(kojyo);  // 工場ｺｰﾄﾞ
            params.add(lotNo);  // ﾛｯﾄNo
            params.add(edaban); // 枝番
            params.add(jissekino); // 回数
        }

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B039Const.KCPNO, srMksinkuukansouData))); // KCPNO
        
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B039Const.KAISHI_TANTOSYA, srMksinkuukansouData))); // 開始担当者
        
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B039Const.KANSOUJIKAN, srMksinkuukansouData))); // 乾燥時間
        
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B039Const.ONDO, srMksinkuukansouData))); // 温度
        
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B039Const.SINKUUDO, srMksinkuukansouData))); // 真空度
        
        params.add(DBUtil.stringToDateObject(getItemData(itemList, GXHDO101B039Const.KAISHI_DAY, srMksinkuukansouData),
            getItemData(itemList, GXHDO101B039Const.KAISHI_TIME, srMksinkuukansouData))); //開始日時
        
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B039Const.BIKO1, srMksinkuukansouData))); // 備考1
        
        if (isInsert) {
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時
        } else {
            params.add(systemTime); //更新日時
        }
        
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B039Const.TOKUISAKI, srMksinkuukansouData))); // 客先

        params.add(StringUtil.nullToBlank(processData.getHiddenDataMap().get("lotkubuncode"))); // ﾛｯﾄ区分
        
        params.add(StringUtil.nullToBlank(processData.getHiddenDataMap().get("ownercode"))); //ｵｰﾅｰ
        
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B039Const.SYORISUU, srMksinkuukansouData))); // 処理数
        
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B039Const.SAGYOUBASYO, srMksinkuukansouData))); // 作業場所
        
        params.add(DBUtil.stringToDateObject(getItemData(itemList, GXHDO101B039Const.BARRELKAISHI_DAY, srMksinkuukansouData),
            getItemData(itemList, GXHDO101B039Const.BARRELKAISHI_TIME, srMksinkuukansouData))); // ﾊﾞﾚﾙ洗浄開始日時
        
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B039Const.BARRELKAISHI_TANTOSYA, srMksinkuukansouData))); // ﾊﾞﾚﾙ洗浄担当者
        
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B039Const.BIKO2, srMksinkuukansouData))); // 備考2

        params.add(newRev); //revision
        
        return params;
    }

    /**
     * ﾒｯｷ真空乾燥(sr_mksinkuukansou)削除処理
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
    private void deleteSrMksinkuukansou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        String sql = "DELETE FROM sr_mksinkuukansou "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND Syorikaisuu = ? AND revision = ?";

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
     * [ﾒｯｷ真空乾燥_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_mksinkuukansou "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? AND Syorikaisuu = ? ";
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
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B039Const.KAISHI_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B039Const.KAISHI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processDate.setMethod("");
        return processDate;
    }

    /**
     * ﾊﾞﾚﾙ洗浄開始日時設定処理
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setBarrelstartDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B039Const.BARRELKAISHI_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B039Const.BARRELKAISHI_TIME);
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
     * @param srMksinkuukansouData ﾒｯｷ真空乾燥データ
     * @return DB値
     */
    private String getSrMksinkuukansouItemData(String itemId, SrMksinkuukansou srMksinkuukansouData) {
        switch (itemId) {
            // KCPNO
            case GXHDO101B039Const.KCPNO:
                return StringUtil.nullToBlank(srMksinkuukansouData.getKcpno());
            // 処理数
            case GXHDO101B039Const.SYORISUU:
                return StringUtil.nullToBlank(srMksinkuukansouData.getSyorisuu());
            // 号機
            case GXHDO101B039Const.GOUKI:
                return StringUtil.nullToBlank(srMksinkuukansouData.getGoukicode());
            // 乾燥時間    
            case GXHDO101B039Const.KANSOUJIKAN:
                return StringUtil.nullToBlank(srMksinkuukansouData.getKansoujikan());
            // 温度
            case GXHDO101B039Const.ONDO:
                return StringUtil.nullToBlank(srMksinkuukansouData.getOndo());
            // 真空度
            case GXHDO101B039Const.SINKUUDO:
                return StringUtil.nullToBlank(srMksinkuukansouData.getSinkuudo());
            // 開始日
            case GXHDO101B039Const.KAISHI_DAY:
                return DateUtil.formattedTimestamp(srMksinkuukansouData.getKaisinichiji(), "yyMMdd");
            // 開始時間
            case GXHDO101B039Const.KAISHI_TIME:
                return DateUtil.formattedTimestamp(srMksinkuukansouData.getKaisinichiji(), "HHmm");
            // 開始担当者
            case GXHDO101B039Const.KAISHI_TANTOSYA:
                return StringUtil.nullToBlank(srMksinkuukansouData.getTantousyacode());
            // 作業場所
            case GXHDO101B039Const.SAGYOUBASYO:
                return StringUtil.nullToBlank(srMksinkuukansouData.getSagyoubasyo());
            // ﾊﾞﾚﾙ洗浄開始日
            case GXHDO101B039Const.BARRELKAISHI_DAY:
                return DateUtil.formattedTimestamp(srMksinkuukansouData.getBarrelkaishinichiji(), "yyMMdd");
            // ﾊﾞﾚﾙ洗浄開始時刻
            case GXHDO101B039Const.BARRELKAISHI_TIME:
                return DateUtil.formattedTimestamp(srMksinkuukansouData.getBarrelkaishinichiji(), "HHmm");
            // ﾊﾞﾚﾙ洗浄担当者
            case GXHDO101B039Const.BARRELKAISHI_TANTOSYA:
                return StringUtil.nullToBlank(srMksinkuukansouData.getBarreltantousya());
            // 備考1
            case GXHDO101B039Const.BIKO1:
                return StringUtil.nullToBlank(srMksinkuukansouData.getBikou());
            // 備考2
            case GXHDO101B039Const.BIKO2:
                return StringUtil.nullToBlank(srMksinkuukansouData.getBikou2());
            default:
                return null;            
        }
    }

    /**
     * ﾒｯｷ真空乾燥_仮登録(tmp_sr_mksinkuukansou)登録処理(削除時)
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
    private void insertDeleteDataTmpSrMksinkuukansou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_mksinkuukansou ("
                + " GoukiCode ,Kojyo ,LotNo ,EdaBan ,Syorikaisuu ,KCPNO ,TantousyaCode ,KansouJikan ,Ondo ,Sinkuudo ,KaisiNichiji ,"
                + " Bikou ,TourokuNichiji ,KousinNichiji ,tokuisaki ,lotkubuncode ,ownercode ,syorisuu ,sagyoubasyo ,barrelkaishinichiji ,"
                + " barreltantousya ,bikou2 ,revision ,deleteflag"
                + ") SELECT "
                +  " GoukiCode ,Kojyo ,LotNo ,EdaBan ,Syorikaisuu ,KCPNO ,TantousyaCode ,KansouJikan ,Ondo ,"
                +  " Sinkuudo ,KaisiNichiji ,Bikou ,? ,? ,tokuisaki ,lotkubuncode ,"
                +  " ownercode ,syorisuu ,sagyoubasyo ,barrelkaishinichiji ,barreltantousya ,bikou2 ,? ,?"
                + " FROM sr_mksinkuukansou "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND Syorikaisuu = ? ";

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
    
}
