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
import jp.co.kccs.xhd.db.model.Jisseki;
import jp.co.kccs.xhd.db.model.SrTerm;
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
import jp.co.kccs.xhd.util.NumberUtil;
import jp.co.kccs.xhd.util.SubFormUtil;
import org.apache.commons.dbutils.DbUtils;

/**
 * ===============================================================================<br>
 * <br>
 * システム名  品質DB(コンデンサ)<br>
 * <br>
 * 変更日      2019/09/02<br>
 * 計画書No    K1803-DS001<br>
 * 変更者      KCSS K.Jo<br>
 * 変更理由    新規作成<br>
 * <br>
 * 変更日      2020/04/08<br>
 * 計画書No    K1803-DS001<br>
 * 変更者      SYSNAVI K.Hisanaga<br>
 * 変更理由    課題対応<br>
 * <br>
 * <br>
 * 変更日      2020/09/08<br>
 * 計画書No    K2008-DS002<br>
 * 変更者      863 zhangjinyan<br>
 * 変更理由    仕様変更<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101B026(外部電極・外部電極塗布)ロジック
 *
 * @author KCSS K.Jo
 * @since  2019/09/02
 */
public class GXHDO101B026 implements IFormLogic {

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
                    GXHDO101B026Const.BTN_START_DATETIME_TOP,
                    GXHDO101B026Const.BTN_END_DATETIME_TOP,
                    GXHDO101B026Const.BTN_INSATSUHABA_KEISAN_TOP,
                    GXHDO101B026Const.BTN_MAWARIKOMI_AVE_KEISAN_TOP,
                    GXHDO101B026Const.BTN_ICHIZURE_MAX_KEISAN_TOP,
                    GXHDO101B026Const.BTN_TANSIKANHABA_MIN_KEISAN_TOP,
                    GXHDO101B026Const.BTN_START_DATETIME_BOTTOM,
                    GXHDO101B026Const.BTN_END_DATETIME_BOTTOM,
                    GXHDO101B026Const.BTN_INSATSUHABA_KEISAN_BOTTOM,
                    GXHDO101B026Const.BTN_MAWARIKOMI_AVE_KEISAN_BOTTOM,
                    GXHDO101B026Const.BTN_ICHIZURE_MAX_KEISAN_BOTTOM,
                    GXHDO101B026Const.BTN_TANSIKANHABA_MIN_KEISAN_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO101B026Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO101B026Const.BTN_INSERT_TOP,
                    GXHDO101B026Const.BTN_DELETE_TOP,
                    GXHDO101B026Const.BTN_UPDATE_TOP,
                    GXHDO101B026Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO101B026Const.BTN_INSERT_BOTTOM,
                    GXHDO101B026Const.BTN_DELETE_BOTTOM,
                    GXHDO101B026Const.BTN_UPDATE_BOTTOM));

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
        // 塗布後外観結果
        FXHDD01 itemDipgogaikankekka = getItemRow(processData.getItemList(), GXHDO101B026Const.DIPGOGAIKANKEKKA);
        if ("NG".equals(itemDipgogaikankekka.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemDipgogaikankekka);
            ErrorMessageInfo errorMessageInfo = MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemDipgogaikankekka.getLabel1());
            processData.setErrorMessageInfoList(Arrays.asList(errorMessageInfo));
            return processData;
        }

        //【関連ﾁｪｯｸ①】 入力不可ﾁｪｯｸ 設備種類
        ErrorMessageInfo checkKanren1ErrorInfo = checkKanren1(processData);
        if (checkKanren1ErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkKanren1ErrorInfo));
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

        // 処理数ﾁｪｯｸ
        FXHDD01 itemSuuryou = getItemRow(processData.getItemList(), GXHDO101B026Const.SUURYOU);
        if("".equals(itemSuuryou.getValue()) || (itemSuuryou.getValue() == null)){
            // 警告メッセージの設定
            processData.setWarnMessage(MessageUtil.getMessage("XHD-000078"));

            // 後続処理メソッド設定
            processData.setMethod("checkJuryouTemp");
        }else{
            checkJuryouTemp(processData);
        }

        return processData;

    }

    /**
     * 重量ﾁｪｯｸ(データチェック処理)
     *
     * @param processData 処理データ
     * @return 処理データ
     */
    public ProcessData checkJuryouTemp(ProcessData processData) {
        // セッションから情報を取得
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        BigDecimal paramSoujuryou = (BigDecimal) session.getAttribute("soujuryou");
        
        FXHDD01 itemJuryou = getItemRow(processData.getItemList(), GXHDO101B026Const.JURYOU);
        if(!"".equals(itemJuryou.getValue()) && (itemJuryou.getValue() != null) && (paramSoujuryou != null)){
            BigDecimal juryou = new BigDecimal(itemJuryou.getValue());
            if(juryou.compareTo(paramSoujuryou) == -1){
                // 警告メッセージの設定
                processData.setWarnMessage(MessageUtil.getMessage("XHD-000076"));
            }
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

                // 外部電極塗布_仮登録処理
                insertTmpSrTerm(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList());

            } else {

                // 外部電極塗布_仮登録更新処理
                updateTmpSrTerm(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList());

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
        ErrorMessageInfo checkItemResistAndCorrectInfo = checkItemResistAndCorrect(processData);
        if (checkItemResistAndCorrectInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemResistAndCorrectInfo));
            return processData;
        }
        
        //【関連ﾁｪｯｸ②】 入力必須ﾁｪｯｸ 設備種類
        ErrorMessageInfo checkKanren2ErrorInfo = checkKanren2(processData);
        if (checkKanren2ErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkKanren2ErrorInfo));
            return processData;
        }
        
        //【関連ﾁｪｯｸ①】 入力不可ﾁｪｯｸ 設備種類
        ErrorMessageInfo checkKanren1ErrorInfo = checkKanren1(processData);
        if (checkKanren1ErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkKanren1ErrorInfo));
            return processData;
        }

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

        // 処理数ﾁｪｯｸ
        FXHDD01 itemSuuryou = getItemRow(processData.getItemList(), GXHDO101B026Const.SUURYOU);
        if("".equals(itemSuuryou.getValue()) || (itemSuuryou.getValue() == null)){
            // 警告メッセージの設定
            processData.setWarnMessage(MessageUtil.getMessage("XHD-000078"));

            // 後続処理メソッド設定
            processData.setMethod("checkJuryou");
        }else{
            checkJuryou(processData);
        }
        return processData;
    }
    
    /**
     * 重量ﾁｪｯｸ(データチェック処理)
     *
     * @param processData 処理データ
     * @return 処理データ
     */
    public ProcessData checkJuryou(ProcessData processData) {
        // セッションから情報を取得
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        BigDecimal paramSoujuryou = (BigDecimal) session.getAttribute("soujuryou");
        
        FXHDD01 itemJuryou = getItemRow(processData.getItemList(), GXHDO101B026Const.JURYOU);
        if(!"".equals(itemJuryou.getValue()) && (itemJuryou.getValue() != null) && (paramSoujuryou != null)){
            BigDecimal juryou = new BigDecimal(itemJuryou.getValue());
            if(juryou.compareTo(paramSoujuryou) == -1){
                // 警告メッセージの設定
                processData.setWarnMessage(MessageUtil.getMessage("XHD-000076"));
            }
        }
        // 後続処理メソッド設定
        processData.setMethod("doResist");
        return processData;
    }
    
    /**
     *【関連ﾁｪｯｸ①】入力不可ﾁｪｯｸ 設備種類
     *
     * @param processData 処理データ
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkKanren1(ProcessData processData) {
        //設備種類
        FXHDD01 itemSetubisyurui = getItemRow(processData.getItemList(), GXHDO101B026Const.SETUBISYURUI);
        //(1)設備種類が入力(選択)されていた場合。
        if (!"".equals(itemSetubisyurui.getValue())) {
            //(1)選択された設備種類をもとに、特定の項目が入力されているかをﾁｪｯｸする。入力されていた場合ｴﾗｰ。
                //粘着ｼｰﾄﾛｯﾄ  1次側
                FXHDD01 itemNentyakusheetlot1 = getItemRow(processData.getItemList(), GXHDO101B026Const.NENTYAKUSHEETLOT1);
                //粘着ｼｰﾄﾛｯﾄ  2次側
                FXHDD01 itemNentyakusheetlot2 = getItemRow(processData.getItemList(), GXHDO101B026Const.NENTYAKUSHEETLOT2);
                //塗布ｼﾞｸﾞｻｲｽﾞ
                FXHDD01 itemDipjigusize = getItemRow(processData.getItemList(), GXHDO101B026Const.DIPJIGUSIZE);
            if ("ADM".equals(itemSetubisyurui.getValue())) {
                //粘着ｼｰﾄﾛｯﾄ  1次側
                if(!NumberUtil.isZeroOrEmpty(itemNentyakusheetlot1.getValue())){
                    // ｴﾗｰ項目をﾘｽﾄに追加
                    List<FXHDD01> errFxhdd01List = Arrays.asList(itemSetubisyurui,itemNentyakusheetlot1);
                    ErrorMessageInfo errorMessageInfo = MessageUtil.getErrorMessageInfo("XHD-000077", true, true, errFxhdd01List, itemSetubisyurui.getLabel1(),"ADM",itemNentyakusheetlot1.getLabel1());
                    return errorMessageInfo;                    
                }
                //粘着ｼｰﾄﾛｯﾄ  2次側
                if(!NumberUtil.isZeroOrEmpty(itemNentyakusheetlot2.getValue())){
                    // ｴﾗｰ項目をﾘｽﾄに追加
                    List<FXHDD01> errFxhdd01List = Arrays.asList(itemSetubisyurui,itemNentyakusheetlot2);
                    ErrorMessageInfo errorMessageInfo = MessageUtil.getErrorMessageInfo("XHD-000077", true, true, errFxhdd01List, itemSetubisyurui.getLabel1(),"ADM",itemNentyakusheetlot2.getLabel1());
                    return errorMessageInfo;                    
                }
            }else if ("N-ADM".equals(itemSetubisyurui.getValue())) {
                //粘着ｼｰﾄﾛｯﾄ  1次側
                if(!NumberUtil.isZeroOrEmpty(itemNentyakusheetlot1.getValue())){
                    // ｴﾗｰ項目をﾘｽﾄに追加
                    List<FXHDD01> errFxhdd01List = Arrays.asList(itemSetubisyurui,itemNentyakusheetlot1);
                    ErrorMessageInfo errorMessageInfo = MessageUtil.getErrorMessageInfo("XHD-000077", true, true, errFxhdd01List, itemSetubisyurui.getLabel1(),"N-ADM",itemNentyakusheetlot1.getLabel1());
                    return errorMessageInfo;                    
                }
                //粘着ｼｰﾄﾛｯﾄ  2次側
                if(!NumberUtil.isZeroOrEmpty(itemNentyakusheetlot2.getValue())){
                    // ｴﾗｰ項目をﾘｽﾄに追加
                    List<FXHDD01> errFxhdd01List = Arrays.asList(itemSetubisyurui,itemNentyakusheetlot2);
                    ErrorMessageInfo errorMessageInfo = MessageUtil.getErrorMessageInfo("XHD-000077", true, true, errFxhdd01List, itemSetubisyurui.getLabel1(),"N-ADM",itemNentyakusheetlot2.getLabel1());
                    return errorMessageInfo;                    
                }
                //塗布ｼﾞｸﾞｻｲｽﾞ
                if(!NumberUtil.isZeroOrEmpty(itemDipjigusize.getValue())){
                    // ｴﾗｰ項目をﾘｽﾄに追加
                    List<FXHDD01> errFxhdd01List = Arrays.asList(itemSetubisyurui,itemDipjigusize);
                    ErrorMessageInfo errorMessageInfo = MessageUtil.getErrorMessageInfo("XHD-000077", true, true, errFxhdd01List, itemSetubisyurui.getLabel1(),"N-ADM",itemDipjigusize.getLabel1());
                    return errorMessageInfo;                    
                }
            }else if ("MDS".equals(itemSetubisyurui.getValue())) {
                //塗布ｼﾞｸﾞｻｲｽﾞ
                if(!NumberUtil.isZeroOrEmpty(itemDipjigusize.getValue())){
                    // ｴﾗｰ項目をﾘｽﾄに追加
                    List<FXHDD01> errFxhdd01List = Arrays.asList(itemSetubisyurui,itemDipjigusize);
                    ErrorMessageInfo errorMessageInfo = MessageUtil.getErrorMessageInfo("XHD-000077", true, true, errFxhdd01List, itemSetubisyurui.getLabel1(),"MDS",itemDipjigusize.getLabel1());
                    return errorMessageInfo;                    
                }
            }
        }
        return null;
    }

    /**
     *【関連ﾁｪｯｸ②】入力必須ﾁｪｯｸ 設備種類
     *
     * @param processData 処理データ
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkKanren2(ProcessData processData) {
        //設備種類
        FXHDD01 itemSetubisyurui = getItemRow(processData.getItemList(), GXHDO101B026Const.SETUBISYURUI);
        //(1)設備種類が入力(選択)されていた場合。
        if (!"".equals(itemSetubisyurui.getValue())) {
            //(1)選択された設備種類をもとに、特定の項目が入力されているかをﾁｪｯｸする。入力されていた場合ｴﾗｰ。
                //粘着ｼｰﾄﾛｯﾄ  1次側
                FXHDD01 itemNentyakusheetlot1 = getItemRow(processData.getItemList(), GXHDO101B026Const.NENTYAKUSHEETLOT1);
                //粘着ｼｰﾄﾛｯﾄ  2次側
                FXHDD01 itemNentyakusheetlot2 = getItemRow(processData.getItemList(), GXHDO101B026Const.NENTYAKUSHEETLOT2);
                //塗布ｼﾞｸﾞｻｲｽﾞ
                FXHDD01 itemDipjigusize = getItemRow(processData.getItemList(), GXHDO101B026Const.DIPJIGUSIZE);
                //塗布ｼﾞｸﾞ取り個数
                FXHDD01 itemTofujigutorikosuu = getItemRow(processData.getItemList(), GXHDO101B026Const.TOFUJIGUTORIKOSUU);
                //塗布ｼﾞｸﾞ枚数
                FXHDD01 itemDipjigumaisuu = getItemRow(processData.getItemList(), GXHDO101B026Const.DIPJIGUMAISUU);
            if ("ADM".equals(itemSetubisyurui.getValue())) {
                //塗布ｼﾞｸﾞｻｲｽﾞ
                if(NumberUtil.isZeroOrEmpty(itemDipjigusize.getValue())){
                    // ｴﾗｰ項目をﾘｽﾄに追加
                    List<FXHDD01> errFxhdd01List = Arrays.asList(itemSetubisyurui,itemDipjigusize);
                    ErrorMessageInfo errorMessageInfo = MessageUtil.getErrorMessageInfo("XHD-000081", true, true, errFxhdd01List, itemSetubisyurui.getLabel1(),"ADM",itemDipjigusize.getLabel1());
                    return errorMessageInfo;                    
                }
                //塗布ｼﾞｸﾞ取り個数
                if(NumberUtil.isZeroOrEmpty(itemTofujigutorikosuu.getValue())){
                    // ｴﾗｰ項目をﾘｽﾄに追加
                    List<FXHDD01> errFxhdd01List = Arrays.asList(itemSetubisyurui,itemTofujigutorikosuu);
                    ErrorMessageInfo errorMessageInfo = MessageUtil.getErrorMessageInfo("XHD-000081", true, true, errFxhdd01List, itemSetubisyurui.getLabel1(),"ADM",itemTofujigutorikosuu.getLabel1());
                    return errorMessageInfo;                    
                }
                //塗布ｼﾞｸﾞ枚数
                if(NumberUtil.isZeroOrEmpty(itemDipjigumaisuu.getValue())){
                    // ｴﾗｰ項目をﾘｽﾄに追加
                    List<FXHDD01> errFxhdd01List = Arrays.asList(itemSetubisyurui,itemDipjigumaisuu);
                    ErrorMessageInfo errorMessageInfo = MessageUtil.getErrorMessageInfo("XHD-000081", true, true, errFxhdd01List, itemSetubisyurui.getLabel1(),"ADM",itemDipjigumaisuu.getLabel1());
                    return errorMessageInfo;                    
                }
            } else if ("N-ADM".equals(itemSetubisyurui.getValue())) {
                //塗布ｼﾞｸﾞ取り個数
                if (NumberUtil.isZeroOrEmpty(itemTofujigutorikosuu.getValue())) {
                    // ｴﾗｰ項目をﾘｽﾄに追加
                    List<FXHDD01> errFxhdd01List = Arrays.asList(itemSetubisyurui, itemTofujigutorikosuu);
                    ErrorMessageInfo errorMessageInfo = MessageUtil.getErrorMessageInfo("XHD-000081", true, true, errFxhdd01List, itemSetubisyurui.getLabel1(), "N-ADM", itemTofujigutorikosuu.getLabel1());
                    return errorMessageInfo;
                }

            } else if ("MDS".equals(itemSetubisyurui.getValue())) {
                //粘着ｼｰﾄﾛｯﾄ  1次側
                if (NumberUtil.isZeroOrEmpty(itemNentyakusheetlot1.getValue())) {
                    // ｴﾗｰ項目をﾘｽﾄに追加
                    List<FXHDD01> errFxhdd01List = Arrays.asList(itemSetubisyurui, itemNentyakusheetlot1);
                    ErrorMessageInfo errorMessageInfo = MessageUtil.getErrorMessageInfo("XHD-000081", true, true, errFxhdd01List, itemSetubisyurui.getLabel1(), "MDS", itemNentyakusheetlot1.getLabel1());
                    return errorMessageInfo;
                }
                //粘着ｼｰﾄﾛｯﾄ  2次側
                if (NumberUtil.isZeroOrEmpty(itemNentyakusheetlot2.getValue())) {
                    // ｴﾗｰ項目をﾘｽﾄに追加
                    List<FXHDD01> errFxhdd01List = Arrays.asList(itemSetubisyurui, itemNentyakusheetlot2);
                    ErrorMessageInfo errorMessageInfo = MessageUtil.getErrorMessageInfo("XHD-000081", true, true, errFxhdd01List, itemSetubisyurui.getLabel1(), "MDS", itemNentyakusheetlot2.getLabel1());
                    return errorMessageInfo;
                }
                //塗布ｼﾞｸﾞ取り個数
                if (NumberUtil.isZeroOrEmpty(itemTofujigutorikosuu.getValue())) {
                    // ｴﾗｰ項目をﾘｽﾄに追加
                    List<FXHDD01> errFxhdd01List = Arrays.asList(itemSetubisyurui, itemTofujigutorikosuu);
                    ErrorMessageInfo errorMessageInfo = MessageUtil.getErrorMessageInfo("XHD-000081", true, true, errFxhdd01List, itemSetubisyurui.getLabel1(), "MDS", itemTofujigutorikosuu.getLabel1());
                    return errorMessageInfo;
                }
            }
        }
        return null;
    }
    
    /**
     * 登録・修正項目チェック
     *
     * @param processData 処理データ
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkItemResistAndCorrect(ProcessData processData) {

        //設備種類
        FXHDD01 itemSetubisyurui = getItemRow(processData.getItemList(), GXHDO101B026Const.SETUBISYURUI);
        if ("".equals(itemSetubisyurui.getValue()) || itemSetubisyurui.getValue() == null) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemSetubisyurui);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemSetubisyurui.getLabel1());
        }
        
        //保持ｼﾞｸﾞ
        FXHDD01 itemHojijigu = getItemRow(processData.getItemList(), GXHDO101B026Const.HOJIJIGU);
        if ("".equals(itemHojijigu.getValue()) || itemHojijigu.getValue() == null) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemHojijigu);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemHojijigu.getLabel1());
        }
        
        //塗布ｼﾞｸﾞ取り個数
        FXHDD01 itemTofujigutorikosuu = getItemRow(processData.getItemList(), GXHDO101B026Const.TOFUJIGUTORIKOSUU);
        if ("".equals(itemTofujigutorikosuu.getValue()) || itemTofujigutorikosuu.getValue() == null) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemTofujigutorikosuu);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemTofujigutorikosuu.getLabel1());
        }

        // 塗布後外観結果
        FXHDD01 itemDipgogaikankekka = getItemRow(processData.getItemList(), GXHDO101B026Const.DIPGOGAIKANKEKKA);
        if ("".equals(itemDipgogaikankekka.getValue()) || itemDipgogaikankekka.getValue() == null) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemDipgogaikankekka);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemDipgogaikankekka.getLabel1());
        }
        
        //作業場所
        FXHDD01 itemSagyobasyo = getItemRow(processData.getItemList(), GXHDO101B026Const.SAGYOBASYO);
        if ("".equals(itemSagyobasyo.getValue()) || itemSagyobasyo.getValue() == null) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemSagyobasyo);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemSagyobasyo.getLabel1());
        }
        
        // 塗布後外観結果
        if ("NG".equals(itemDipgogaikankekka.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemDipgogaikankekka);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemDipgogaikankekka.getLabel1());
        }

        return null;
    }
    
    /**
     * 登録・修正項目チェック
     *
     * @param processData 処理データ
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkItemResistCorrect(ProcessData processData) {

        ValidateUtil validateUtil = new ValidateUtil();
        // 開始日時、終了日時前後チェック
        FXHDD01 itemKaishiDay = getItemRow(processData.getItemList(), GXHDO101B026Const.KAISHI_DAY); //開始日
        FXHDD01 itemKaishiTime = getItemRow(processData.getItemList(), GXHDO101B026Const.KAISHI_TIME); // 開始時刻
        Date kaishiDate = DateUtil.convertStringToDate(itemKaishiDay.getValue(), itemKaishiTime.getValue());
        FXHDD01 itemShuryouDay = getItemRow(processData.getItemList(), GXHDO101B026Const.SHURYOU_DAY); //終了日
        FXHDD01 itemShuryouTime = getItemRow(processData.getItemList(), GXHDO101B026Const.SHURYOU_TIME); //終了時刻
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
            SrTerm tmpSrTerm = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {
                
                // 更新前の値を取得
                List<SrTerm> srTermList = getSrTermData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo8, edaban, paramJissekino);
                if (!srTermList.isEmpty()) {
                    tmpSrTerm = srTermList.get(0);
                }
                
                deleteTmpSrTerm(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban, paramJissekino);
            }

            // 外部電極塗布_登録処理
            insertSrTerm(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList(), tmpSrTerm);

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
        ErrorMessageInfo checkItemResistAndCorrectInfo = checkItemResistAndCorrect(processData);
        if (checkItemResistAndCorrectInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemResistAndCorrectInfo));
            return processData;
        }
        
        //【関連ﾁｪｯｸ②】 入力必須ﾁｪｯｸ 設備種類
        ErrorMessageInfo checkKanren2ErrorInfo = checkKanren2(processData);
        if (checkKanren2ErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkKanren2ErrorInfo));
            return processData;
        }
        
        //【関連ﾁｪｯｸ①】 入力不可ﾁｪｯｸ 設備種類
        ErrorMessageInfo checkKanren1ErrorInfo = checkKanren1(processData);
        if (checkKanren1ErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkKanren1ErrorInfo));
            return processData;
        }
        
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
        processData.setUserAuthParam(GXHDO101B026Const.USER_AUTH_UPDATE_PARAM);

        // 処理数ﾁｪｯｸ
        FXHDD01 itemSuuryou = getItemRow(processData.getItemList(), GXHDO101B026Const.SUURYOU);
        if("".equals(itemSuuryou.getValue()) || (itemSuuryou.getValue() == null)){
            // 警告メッセージの設定
            processData.setWarnMessage(MessageUtil.getMessage("XHD-000078"));

            // 後続処理メソッド設定
            processData.setMethod("checkJuryouCorrect");
        }else{
            checkJuryouCorrect(processData);
        }
        return processData;
    }
    
    /**
     * 重量ﾁｪｯｸ(データチェック処理)
     *
     * @param processData 処理データ
     * @return 処理データ
     */
    public ProcessData checkJuryouCorrect(ProcessData processData) {
        // セッションから情報を取得
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        BigDecimal paramSoujuryou = (BigDecimal) session.getAttribute("soujuryou");
        
        FXHDD01 itemJuryou = getItemRow(processData.getItemList(), GXHDO101B026Const.JURYOU);
        if(!"".equals(itemJuryou.getValue()) && (itemJuryou.getValue() != null) && (paramSoujuryou != null)){
            BigDecimal juryou = new BigDecimal(itemJuryou.getValue());
            if(juryou.compareTo(paramSoujuryou) == -1){
                // 警告メッセージの設定
                processData.setWarnMessage(MessageUtil.getMessage("XHD-000076"));
            }
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

            // 外部電極塗布_更新処理
            updateSrTerm(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList());

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
        processData.setUserAuthParam(GXHDO101B026Const.USER_AUTH_DELETE_PARAM);

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

            // 外部電極塗布_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo8, edaban, paramJissekino);
            insertDeleteDataTmpSrTerm(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, paramJissekino, systemTime);

            // 外部電極塗布_削除処理
            deleteSrTerm(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban, paramJissekino);

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
                        GXHDO101B026Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO101B026Const.BTN_DELETE_BOTTOM,
                        GXHDO101B026Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B026Const.BTN_START_DATETIME_BOTTOM,
                        GXHDO101B026Const.BTN_END_DATETIME_BOTTOM,
                        GXHDO101B026Const.BTN_INSATSUHABA_KEISAN_BOTTOM,
                        GXHDO101B026Const.BTN_MAWARIKOMI_AVE_KEISAN_BOTTOM,
                        GXHDO101B026Const.BTN_ICHIZURE_MAX_KEISAN_BOTTOM,
                        GXHDO101B026Const.BTN_TANSIKANHABA_MIN_KEISAN_BOTTOM,
                        GXHDO101B026Const.BTN_EDABAN_COPY_TOP,
                        GXHDO101B026Const.BTN_DELETE_TOP,
                        GXHDO101B026Const.BTN_UPDATE_TOP,
                        GXHDO101B026Const.BTN_START_DATETIME_TOP,
                        GXHDO101B026Const.BTN_END_DATETIME_TOP,
                        GXHDO101B026Const.BTN_INSATSUHABA_KEISAN_TOP,
                        GXHDO101B026Const.BTN_MAWARIKOMI_AVE_KEISAN_TOP,
                        GXHDO101B026Const.BTN_ICHIZURE_MAX_KEISAN_TOP,
                        GXHDO101B026Const.BTN_TANSIKANHABA_MIN_KEISAN_TOP
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B026Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B026Const.BTN_INSERT_BOTTOM,
                        GXHDO101B026Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B026Const.BTN_INSERT_TOP));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO101B026Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B026Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO101B026Const.BTN_INSERT_BOTTOM,
                        GXHDO101B026Const.BTN_START_DATETIME_BOTTOM,
                        GXHDO101B026Const.BTN_END_DATETIME_BOTTOM,
                        GXHDO101B026Const.BTN_INSATSUHABA_KEISAN_BOTTOM,
                        GXHDO101B026Const.BTN_MAWARIKOMI_AVE_KEISAN_BOTTOM,
                        GXHDO101B026Const.BTN_ICHIZURE_MAX_KEISAN_BOTTOM,
                        GXHDO101B026Const.BTN_TANSIKANHABA_MIN_KEISAN_BOTTOM,
                        GXHDO101B026Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B026Const.BTN_EDABAN_COPY_TOP,
                        GXHDO101B026Const.BTN_INSERT_TOP,
                        GXHDO101B026Const.BTN_START_DATETIME_TOP,
                        GXHDO101B026Const.BTN_END_DATETIME_TOP,
                        GXHDO101B026Const.BTN_INSATSUHABA_KEISAN_TOP,
                        GXHDO101B026Const.BTN_MAWARIKOMI_AVE_KEISAN_TOP,
                        GXHDO101B026Const.BTN_ICHIZURE_MAX_KEISAN_TOP,
                        GXHDO101B026Const.BTN_TANSIKANHABA_MIN_KEISAN_TOP
                ));

                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B026Const.BTN_DELETE_BOTTOM,
                        GXHDO101B026Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B026Const.BTN_DELETE_TOP,
                        GXHDO101B026Const.BTN_UPDATE_TOP));

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
            case GXHDO101B026Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO101B026Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempResist";
                break;
            // 登録
            case GXHDO101B026Const.BTN_INSERT_TOP:
            case GXHDO101B026Const.BTN_INSERT_BOTTOM:
                method = "checkDataResist";
                break;
            // 枝番コピー
            case GXHDO101B026Const.BTN_EDABAN_COPY_TOP:
            case GXHDO101B026Const.BTN_EDABAN_COPY_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 修正
            case GXHDO101B026Const.BTN_UPDATE_TOP:
            case GXHDO101B026Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO101B026Const.BTN_DELETE_TOP:
            case GXHDO101B026Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 開始日時
            case GXHDO101B026Const.BTN_START_DATETIME_TOP:
            case GXHDO101B026Const.BTN_START_DATETIME_BOTTOM:
                method = "setKaishiDateTime";
                break;
            // 終了日時
            case GXHDO101B026Const.BTN_END_DATETIME_TOP:
            case GXHDO101B026Const.BTN_END_DATETIME_BOTTOM:
                method = "setShuryouDateTime";
                break;
            // 印刷幅計算
            case GXHDO101B026Const.BTN_INSATSUHABA_KEISAN_TOP:
            case GXHDO101B026Const.BTN_INSATSUHABA_KEISAN_BOTTOM:
                method = "setInsatsuhabaKeisan";
                break;
            // 回り込みAVE計算
            case GXHDO101B026Const.BTN_MAWARIKOMI_AVE_KEISAN_TOP:
            case GXHDO101B026Const.BTN_MAWARIKOMI_AVE_KEISAN_BOTTOM:
                method = "setMawarikomiAveKeisan";
                break;
            // 位置ｽﾞﾚMAX計算
            case GXHDO101B026Const.BTN_ICHIZURE_MAX_KEISAN_TOP:
            case GXHDO101B026Const.BTN_ICHIZURE_MAX_KEISAN_BOTTOM:
                method = "setIchizureMaxKeisan";
                break;
            // 端子間幅MIN計算
            case GXHDO101B026Const.BTN_TANSIKANHABA_MIN_KEISAN_TOP:
            case GXHDO101B026Const.BTN_TANSIKANHABA_MIN_KEISAN_BOTTOM:
                method = "setTansikanhabaMinKeisan";
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
        String tanijuryo = StringUtil.nullToBlank(getMapData(shikakariData, "tanijuryo")); // 受入れ単位重量

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
        String shorisuu = null;

        // ﾊﾟﾗﾒｰﾀﾃﾞｰﾀ情報の取得
        String strfxhbm03List = "";
        
        Map fxhbm03Data20 = loadFxhbm03Data(queryRunnerDoc, 20);
        if(fxhbm03Data20 != null && !fxhbm03Data20.isEmpty()){
            strfxhbm03List = StringUtil.nullToBlank(getMapData(fxhbm03Data20, "data"));
            String fxhbm03Data[] = strfxhbm03List.split(",");
            
            // 実績情報の取得
            List<Jisseki> jissekiData = loadJissekiData(queryRunnerWip, lotNo, fxhbm03Data);
            if(jissekiData != null && jissekiData.size() > 0){
                int dbShorisu = jissekiData.get(0).getSyorisuu(); //処理数
                if(dbShorisu > 0){
                    shorisuu = String.valueOf(dbShorisu);
                }
            }
        }
        
        // 磁器寸法2情報の取得
        Map cknsunpou2Data = loadCknSunpou2Data(queryRunnerQcdb, lotNo);
        
        // 入力項目の情報を画面にセットする。
        if (!setInputItemData(processData, queryRunnerDoc, queryRunnerQcdb, lotNo, formId, paramJissekino, shorisuu, tanijuryo)) {
            // エラー発生時は処理を中断
            processData.setFatalError(true);
            processData.setInitMessageList(Arrays.asList(MessageUtil.getMessage("XHD-000038")));
            return processData;
        }

        // 画面に取得した情報をセットする。(入力項目以外)
        setViewItemData(processData, sekkeiData, lotKbnMasData, ownerMasData, shikakariData, lotNo, shorisuu, cknsunpou2Data);

        processData.setInitMessageList(errorMessageList);
        return processData;

    }
    
    /**
     * [磁器寸法2]から、ﾃﾞｰﾀを取得
     * @param queryRunnerQcdb QueryRunnerオブジェクト(Qcdb)
     * @param lotNo ﾛｯﾄNo
     * @return
     * @throws SQLException 
     */
    private Map loadCknSunpou2Data(QueryRunner queryRunnerQcdb, String lotNo) throws SQLException {
        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 11);
        String lotNo3 = lotNo.substring(11, 14);

        // 磁器寸法1データの取得
        String sql = "SELECT   MAX(l) jikilsunpoumax"
                + "  , MIN(l) jikilsunpoumin"
                + "  , MAX(w) jikiwsunpoumax"
                + "  , MIN(w) jikiwsunpoumin"
                + "  , MAX(t) jikitsunpoumax"
                + "  , MIN(t) jikitsunpoumin "
                + "FROM sr_cknsunpou2  "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? ";

        List<Object> params = new ArrayList<>();
        params.add(lotNo1);
        params.add(lotNo2);
        params.add(lotNo3);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
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
     * @param shorisuu 送り良品数
     * @param cknsunpou2Data 磁器寸法2データ
     */
    private void setViewItemData(ProcessData processData, Map sekkeiData, Map lotKbnMasData, Map ownerMasData, Map shikakariData, String lotNo, String shorisuu, Map cknsunpou2Data) {
        
        // ロットNo
        this.setItemData(processData, GXHDO101B026Const.LOTNO, lotNo);
        // KCPNO
        this.setItemData(processData, GXHDO101B026Const.KCPNO, StringUtil.nullToBlank(getMapData(shikakariData, "kcpno")));
        // 客先
        this.setItemData(processData, GXHDO101B026Const.KYAKUSAKI, StringUtil.nullToBlank(getMapData(shikakariData, "tokuisaki")));

        // ロット区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); //ﾛｯﾄ区分ｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B026Const.LOT_KUBUN, "");
        } else {
            String lotKubun = StringUtil.nullToBlank(getMapData(lotKbnMasData, "lotkubun"));
            this.setItemData(processData, GXHDO101B026Const.LOT_KUBUN, lotkubuncode + ":" + lotKubun);
        }

        // オーナー
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));// ｵｰﾅｰｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B026Const.OWNER, "");
        } else {
            String owner = StringUtil.nullToBlank(getMapData(ownerMasData, "ownername"));
            this.setItemData(processData, GXHDO101B026Const.OWNER, ownercode + ":" + owner);
        }
        
        String lotpre = StringUtil.nullToBlank(getMapData(shikakariData, "lotpre")); // ﾛｯﾄﾌﾟﾚ
        // ﾛｯﾄﾌﾟﾚ
        this.setItemData(processData, GXHDO101B026Const.LOTPRE, lotpre);

        // ﾍﾟｰｽﾄ品名
        FXHDD01 itemPastehinmei = getItemRow(processData.getItemList(), GXHDO101B026Const.PASTEHINMEI);

        if ("".equals(itemPastehinmei.getValue()) || itemPastehinmei.getValue() == null) {
            String strKikakuchi = StringUtil.nullToBlank(itemPastehinmei.getKikakuChi()).replace("【", "");
            strKikakuchi = strKikakuchi.replace("】", "");
            this.setItemData(processData, GXHDO101B026Const.PASTEHINMEI, strKikakuchi);
        }

        if (cknsunpou2Data != null && !cknsunpou2Data.isEmpty()) {
            // 磁器L寸法(MAX)
            this.setItemData(processData, GXHDO101B026Const.JIKILSUNPOUMAX, 
                    StringUtil.nullToBlank(getMapData(cknsunpou2Data, "jikilsunpoumax")));
            // 磁器L寸法(MIN)
            this.setItemData(processData, GXHDO101B026Const.JIKILSUNPOUMIN, 
                    StringUtil.nullToBlank(getMapData(cknsunpou2Data, "jikilsunpoumin")));
            // 磁器W寸法(MAX)
            this.setItemData(processData, GXHDO101B026Const.JIKIWSUNPOUMAX, 
                    StringUtil.nullToBlank(getMapData(cknsunpou2Data, "jikiwsunpoumax")));
            // 磁器W寸法(MIN)
            this.setItemData(processData, GXHDO101B026Const.JIKIWSUNPOUMIN, 
                    StringUtil.nullToBlank(getMapData(cknsunpou2Data, "jikiwsunpoumin")));
            // 磁器T寸法(MAX)
            this.setItemData(processData, GXHDO101B026Const.JIKITSUNPOUMAX, 
                    StringUtil.nullToBlank(getMapData(cknsunpou2Data, "jikitsunpoumax")));
            // 磁器T寸法(MIN)
            this.setItemData(processData, GXHDO101B026Const.JIKITSUNPOUMIN, 
                    StringUtil.nullToBlank(getMapData(cknsunpou2Data, "jikitsunpoumin")));
        }
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
     * @param suuryou 送り良品数
     * @param tanijuryo 受入れ単位重量
     * @return 設定結果(失敗時false)
     * @throws SQLException 例外エラー
     */
    private boolean setInputItemData(ProcessData processData, QueryRunner queryRunnerDoc, QueryRunner queryRunnerQcdb,
            String lotNo, String formId, int jissekino, String suuryou, String tanijuryo) throws SQLException {

        List<SrTerm> srTermDataList = new ArrayList<>();
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
                
                //受入総重量計算処理
                FXHDD01 itemUkeireSojuryo = getItemRow(processData.getItemList(), GXHDO101B026Const.UKEIRESOUJYURYOU);
                FXHDD01 itemOkuriRyohinsu = getItemRow(processData.getItemList(), GXHDO101B026Const.SUURYOU);
                FXHDD01 itemUkeireTanijuryo = getItemRow(processData.getItemList(), GXHDO101B026Const.UKEIRETANNIJYURYO);

                // 送り良品数←初期表示時、「送り良品数の取得」参照
                itemOkuriRyohinsu.setValue(suuryou);
                
                // 受入れ単位重量←Ⅲ.画面表示仕様(18).単位重量
                itemUkeireTanijuryo.setValue(NumberUtil.getTruncatData(tanijuryo, itemUkeireTanijuryo.getInputLength(), itemUkeireTanijuryo.getInputLengthDec()));
                
                // 受入れ総重量←【受入れ総重量計算】参照
                if (checkUkeireSojuryo(itemUkeireSojuryo, itemOkuriRyohinsu, itemUkeireTanijuryo)) {
                    // ﾁｪｯｸに問題なければ値をセット
                    calcUkeireSojuryo(itemUkeireSojuryo, itemOkuriRyohinsu, itemUkeireTanijuryo);
                    // 受入れ総重量
                    this.setItemData(processData, GXHDO101B026Const.UKEIRESOUJYURYOU, itemUkeireSojuryo.getValue());
                }
                
                // 受入れ単位重量
                this.setItemData(processData, GXHDO101B026Const.UKEIRETANNIJYURYO, tanijuryo);
                // 送り良品数
                this.setItemData(processData, GXHDO101B026Const.SUURYOU, suuryou);
                
                return true;
            }

            // 外部電極塗布データ取得
            srTermDataList = getSrTermData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban, jissekino);
            if (srTermDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srTermDataList.isEmpty()) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srTermDataList.get(0));

        return true;

    }

    /**
     * 受入れ総重量の計算前ﾁｪｯｸ
     *
     * @param itemUkeireSojuryo 受入れ総重量
     * @param itemOkuriRyohinsu 送り良品数
     * @param itemUkeireTanijuryo 受入れ単位重量
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
     * @param itemOkuriRyohinsu 送り良品数
     * @param itemUkeireTanijuryo 受入れ単位重量
     */
    private void calcUkeireSojuryo(FXHDD01 itemUkeireSojuryo, FXHDD01 itemOkuriRyohinsu, FXHDD01 itemUkeireTanijuryo) {
        try {
            BigDecimal taniJuryo = new BigDecimal(itemUkeireTanijuryo.getValue());
            BigDecimal okuriRyohinsu = new BigDecimal(itemOkuriRyohinsu.getValue());

            //「送り良品数」　÷　100　×　「受入れ単位重量」 → 式を変換して先に「受入れ単位重量」を乗算
            BigDecimal calcResult = okuriRyohinsu.multiply(taniJuryo).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            //計算結果をセット
            itemUkeireSojuryo.setValue(calcResult.toPlainString());

        } catch (NullPointerException | NumberFormatException ex) {
            //処理なし
        }
    }

    /**
     * メイン画面データ設定処理
     *
     * @param processData 処理制御データ
     * @param srTermData 外部電極塗布データ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrTerm srTermData) {
        //受入れ良品数
        this.setItemData(processData, GXHDO101B026Const.SUURYOU, getSrTermItemData(GXHDO101B026Const.SUURYOU, srTermData));
        //受入れ単位重量
        this.setItemData(processData, GXHDO101B026Const.UKEIRETANNIJYURYO, getSrTermItemData(GXHDO101B026Const.UKEIRETANNIJYURYO, srTermData));
        //受入れ総重量
        this.setItemData(processData, GXHDO101B026Const.UKEIRESOUJYURYOU, getSrTermItemData(GXHDO101B026Const.UKEIRESOUJYURYOU, srTermData));
        //設備種類
        this.setItemData(processData, GXHDO101B026Const.SETUBISYURUI, getSrTermItemData(GXHDO101B026Const.SETUBISYURUI, srTermData));
        //塗布号機
        this.setItemData(processData, GXHDO101B026Const.GOUKI1, getSrTermItemData(GXHDO101B026Const.GOUKI1, srTermData));
        //塗布回数
        this.setItemData(processData, GXHDO101B026Const.TOFUKAISUU, getSrTermItemData(GXHDO101B026Const.TOFUKAISUU, srTermData));
        //ﾍﾟｰｽﾄ品名
        this.setItemData(processData, GXHDO101B026Const.PASTEHINMEI, getSrTermItemData(GXHDO101B026Const.PASTEHINMEI, srTermData));
        //ﾍﾟｰｽﾄﾛｯﾄNo
        this.setItemData(processData, GXHDO101B026Const.PASTELOTNO, getSrTermItemData(GXHDO101B026Const.PASTELOTNO, srTermData));
        //ﾍﾟｰｽﾄ再生回数
        this.setItemData(processData, GXHDO101B026Const.PASTESAISEIKAISUU, getSrTermItemData(GXHDO101B026Const.PASTESAISEIKAISUU, srTermData));
        //ﾍﾟｰｽﾄ粘度
        this.setItemData(processData, GXHDO101B026Const.PASTENENDO, getSrTermItemData(GXHDO101B026Const.PASTENENDO, srTermData));
        //条件設定者
        this.setItemData(processData, GXHDO101B026Const.SETTEISYA1, getSrTermItemData(GXHDO101B026Const.SETTEISYA1, srTermData));
        //ﾍﾟｰｽﾄ厚み設定値1次
        this.setItemData(processData, GXHDO101B026Const.PASTEATSUMI1JI, getSrTermItemData(GXHDO101B026Const.PASTEATSUMI1JI, srTermData));
        //ﾍﾟｰｽﾄ厚み設定値2次
        this.setItemData(processData, GXHDO101B026Const.PASTEATSUMI2JI, getSrTermItemData(GXHDO101B026Const.PASTEATSUMI2JI, srTermData));
        //保持ｼﾞｸﾞ
        this.setItemData(processData, GXHDO101B026Const.HOJIJIGU, getSrTermItemData(GXHDO101B026Const.HOJIJIGU, srTermData));
        //粘着ｼｰﾄﾛｯﾄ  1次側
        this.setItemData(processData, GXHDO101B026Const.NENTYAKUSHEETLOT1, getSrTermItemData(GXHDO101B026Const.NENTYAKUSHEETLOT1, srTermData));
        //粘着ｼｰﾄﾛｯﾄ  2次側
        this.setItemData(processData, GXHDO101B026Const.NENTYAKUSHEETLOT2, getSrTermItemData(GXHDO101B026Const.NENTYAKUSHEETLOT2, srTermData));
        //塗布ｼﾞｸﾞｻｲｽﾞ
        this.setItemData(processData, GXHDO101B026Const.DIPJIGUSIZE, getSrTermItemData(GXHDO101B026Const.DIPJIGUSIZE, srTermData));
        //塗布ｼﾞｸﾞ取り個数
        this.setItemData(processData, GXHDO101B026Const.TOFUJIGUTORIKOSUU, getSrTermItemData(GXHDO101B026Const.TOFUJIGUTORIKOSUU, srTermData));
        //塗布ｼﾞｸﾞ枚数
        this.setItemData(processData, GXHDO101B026Const.DIPJIGUMAISUU, getSrTermItemData(GXHDO101B026Const.DIPJIGUMAISUU, srTermData));
        //開始日
        this.setItemData(processData, GXHDO101B026Const.KAISHI_DAY, getSrTermItemData(GXHDO101B026Const.KAISHI_DAY, srTermData));
        //開始時刻
        this.setItemData(processData, GXHDO101B026Const.KAISHI_TIME, getSrTermItemData(GXHDO101B026Const.KAISHI_TIME, srTermData));
        //開始担当者
        this.setItemData(processData, GXHDO101B026Const.KAISHI_TANTOUSYA, getSrTermItemData(GXHDO101B026Const.KAISHI_TANTOUSYA, srTermData));
        //開始確認者
        this.setItemData(processData, GXHDO101B026Const.KAISHI_KAKUNINSYA, getSrTermItemData(GXHDO101B026Const.KAISHI_KAKUNINSYA, srTermData));
        //ｲﾝｸ厚みA
        this.setItemData(processData, GXHDO101B026Const.ATSUMIINKUA, getSrTermItemData(GXHDO101B026Const.ATSUMIINKUA, srTermData));
        //ｲﾝｸ厚みB
        this.setItemData(processData, GXHDO101B026Const.ATSUMIINKUB, getSrTermItemData(GXHDO101B026Const.ATSUMIINKUB, srTermData));
        //端面厚み
        this.setItemData(processData, GXHDO101B026Const.TANMENATSUMI2, getSrTermItemData(GXHDO101B026Const.TANMENATSUMI2, srTermData));
        //P寸法 AVE
        this.setItemData(processData, GXHDO101B026Const.PSUNPOUAVE2, getSrTermItemData(GXHDO101B026Const.PSUNPOUAVE2, srTermData));
        //P寸法 MAX
        this.setItemData(processData, GXHDO101B026Const.PSUNPOUMAX2, getSrTermItemData(GXHDO101B026Const.PSUNPOUMAX2, srTermData));
        //P寸法 MIN
        this.setItemData(processData, GXHDO101B026Const.PSUNPOUMIN2, getSrTermItemData(GXHDO101B026Const.PSUNPOUMIN2, srTermData));
        //L寸法 AVE
        this.setItemData(processData, GXHDO101B026Const.LSUNPOUAVE2, getSrTermItemData(GXHDO101B026Const.LSUNPOUAVE2, srTermData));
        //L寸法 MAX
        this.setItemData(processData, GXHDO101B026Const.LSUNPOUMAX2, getSrTermItemData(GXHDO101B026Const.LSUNPOUMAX2, srTermData));
        //L寸法 MIN
        this.setItemData(processData, GXHDO101B026Const.LSUNPOUMIN2, getSrTermItemData(GXHDO101B026Const.LSUNPOUMIN2, srTermData));
        //塗布後外観結果
        this.setItemData(processData, GXHDO101B026Const.DIPGOGAIKANKEKKA, getSrTermItemData(GXHDO101B026Const.DIPGOGAIKANKEKKA, srTermData));
        //処理個数
        this.setItemData(processData, GXHDO101B026Const.SYORIKOSUU, getSrTermItemData(GXHDO101B026Const.SYORIKOSUU, srTermData));
        //重量
        this.setItemData(processData, GXHDO101B026Const.JURYOU, getSrTermItemData(GXHDO101B026Const.JURYOU, srTermData));
        //終了日
        this.setItemData(processData, GXHDO101B026Const.SHURYOU_DAY, getSrTermItemData(GXHDO101B026Const.SHURYOU_DAY, srTermData));
        //終了時刻
        this.setItemData(processData, GXHDO101B026Const.SHURYOU_TIME, getSrTermItemData(GXHDO101B026Const.SHURYOU_TIME, srTermData));
        //終了担当者
        this.setItemData(processData, GXHDO101B026Const.SHURYOU_TANTOUSYA, getSrTermItemData(GXHDO101B026Const.SHURYOU_TANTOUSYA, srTermData));
        //作業場所
        this.setItemData(processData, GXHDO101B026Const.SAGYOBASYO, getSrTermItemData(GXHDO101B026Const.SAGYOBASYO, srTermData));
        //備考1
        this.setItemData(processData, GXHDO101B026Const.BIKO1, getSrTermItemData(GXHDO101B026Const.BIKO1, srTermData));
        //備考2
        this.setItemData(processData, GXHDO101B026Const.BIKO2, getSrTermItemData(GXHDO101B026Const.BIKO2, srTermData));
    }

    /**
     * 外部電極塗布の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @param jissekino 実績No
     * @return 外部電極塗布登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrTerm> getSrTermData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrTerm(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
        } else {
            return loadTmpSrTerm(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
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
     * [外部電極塗布]から、ﾃﾞｰﾀを取得
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
    private List<SrTerm> loadSrTerm(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, String rev) throws SQLException {

        String sql = "SELECT "
                + " kojyo,lotno,edaban,lotpre,kcpno,suuryou,ukeiretannijyuryo,ukeiresoujyuryou,jikilsunpoumax,jikilsunpoumin,jikiwsunpoumax,jikiwsunpoumin,"
                + "jikitsunpoumax,jikitsunpoumin,kyakusaki,sagyobasyo,gouki1,setteisya1,pastehinmei,pastelotno,pastesaiseikaisuu,pastenendo"
                + ",startdatetime,enddatetime,psunpouave2,dipjigusize,dipjigumaisuu,dipgogaikankekka,bikou1,bikou2,lsunpouave2,lsunpoumin2,lsunpoumax2"
                + ",psunpoumin2,psunpoumax2,setubisyurui,tofukaisuu,hojijigu,nentyakusheetlot1,nentyakusheetlot2,tofujigutorikosuu,StartTantosyacode"
                + ",StartKakuninsyacode,juryou,syorikosuu,EndTantosyacode,pasteatsumi1ji,pasteatsumi2ji,atsumiinkua,atsumiinkub,tanmenatsumi2,kaisuu,torokunichiji"
                + ",kosinnichiji,revision,'0' AS deleteflag "
                + "FROM sr_term "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? AND kaisuu = ? ";
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
        mapping.put("kojyo", "kojyo");                                 //工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                 //ﾛｯﾄNo
        mapping.put("edaban", "edaban");                               //枝番
        mapping.put("lotpre", "lotpre");                               //ﾛｯﾄﾌﾟﾚ
        mapping.put("kcpno", "kcpno");                                 //KCPNO
        mapping.put("suuryou", "suuryou");                             //数量
        mapping.put("ukeiretannijyuryo", "ukeiretannijyuryo");         //受入れ単位重量
        mapping.put("ukeiresoujyuryou", "ukeiresoujyuryou");           //受入れ総重量
        mapping.put("jikilsunpoumax", "jikilsunpoumax");               //磁器L寸法(MAX)
        mapping.put("jikilsunpoumin", "jikilsunpoumin");               //磁器L寸法(MIN)
        mapping.put("jikiwsunpoumax", "jikiwsunpoumax");               //磁器W寸法(MAX)
        mapping.put("jikiwsunpoumin", "jikiwsunpoumin");               //磁器W寸法(MIN)
        mapping.put("jikitsunpoumax", "jikitsunpoumax");               //磁器T寸法(MAX)
        mapping.put("jikitsunpoumin", "jikitsunpoumin");               //磁器T寸法(MIN)
        mapping.put("kyakusaki", "kyakusaki");                         //客先
        mapping.put("sagyobasyo", "sagyobasyo");                       //作業場所
        mapping.put("gouki1", "gouki1");                               //号機1
        mapping.put("setteisya1", "setteisya1");                       //条件設定者1
        mapping.put("pastehinmei", "pastehinmei");                     //ﾍﾟｰｽﾄ品名
        mapping.put("pastelotno", "pastelotno");                       //ﾍﾟｰｽﾄﾛｯﾄNo
        mapping.put("pastesaiseikaisuu", "pastesaiseikaisuu");         //ﾍﾟｰｽﾄ再生回数
        mapping.put("pastenendo", "pastenendo");                       //ﾍﾟｰｽﾄ粘度
        mapping.put("startdatetime", "startdatetime");                 //開始日時
        mapping.put("enddatetime", "enddatetime");                     //終了日時
        mapping.put("psunpouave2", "psunpouave2");                     //P寸法AVE
        mapping.put("dipjigusize", "dipjigusize");                     //DIP治具ｻｲｽﾞ
        mapping.put("dipjigumaisuu", "dipjigumaisuu");                 //DIP治具枚数
        mapping.put("dipgogaikankekka", "dipgogaikankekka");           //DIP後外観結果
        mapping.put("bikou1", "bikou1");                               //備考1
        mapping.put("bikou2", "bikou2");                               //備考2
        mapping.put("lsunpouave2", "lsunpouave2");                     //L寸法AVE
        mapping.put("lsunpoumin2", "lsunpoumin2");                     //L寸法MIN
        mapping.put("lsunpoumax2", "lsunpoumax2");                     //L寸法MAX
        mapping.put("psunpoumin2", "psunpoumin2");                     //P寸法MIN
        mapping.put("psunpoumax2", "psunpoumax2");                     //P寸法MAX
        mapping.put("setubisyurui", "setubisyurui");                   //設備種類
        mapping.put("tofukaisuu", "tofukaisuu");                       //塗布回数
        mapping.put("hojijigu", "hojijigu");                           //保持ｼﾞｸﾞ
        mapping.put("nentyakusheetlot1", "nentyakusheetlot1");         //粘着ｼｰﾄﾛｯﾄ1次側
        mapping.put("nentyakusheetlot2", "nentyakusheetlot2");         //粘着ｼｰﾄﾛｯﾄ2次側
        mapping.put("tofujigutorikosuu", "tofujigutorikosuu");         //塗布ｼﾞｸﾞ取り個数
        mapping.put("StartTantosyacode", "starttantosyacode");         //開始担当者
        mapping.put("StartKakuninsyacode", "startkakuninsyacode");     //開始確認者
        mapping.put("juryou", "juryou");                               //重量
        mapping.put("syorikosuu", "syorikosuu");                       //処理個数
        mapping.put("EndTantosyacode", "endtantosyacode");             //終了担当者
        mapping.put("pasteatsumi1ji", "pasteatsumi1ji");               //1次ﾍﾟｰｽﾄ厚み設定値
        mapping.put("pasteatsumi2ji", "pasteatsumi2ji");               //2次ﾍﾟｰｽﾄ厚み設定値
        mapping.put("atsumiinkua", "atsumiinkua");                     //ｲﾝｸ厚みA
        mapping.put("atsumiinkub", "atsumiinkub");                     //ｲﾝｸ厚みB
        mapping.put("tanmenatsumi2", "tanmenatsumi2");                 //端面厚み
        mapping.put("kaisuu", "kaisuu");                               //回数
        mapping.put("torokunichiji", "torokunichiji");                 //登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                   //更新日時
        mapping.put("revision", "revision");                           //revision
        mapping.put("deleteflag", "deleteflag");                       //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrTerm>> beanHandler = new BeanListHandler<>(SrTerm.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [外部電極塗布_仮登録]から、ﾃﾞｰﾀを取得
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
    private List<SrTerm> loadTmpSrTerm(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, String rev) throws SQLException {
        
        String sql = "SELECT "
                + " kojyo,lotno,edaban,lotpre,kcpno,suuryou,ukeiretannijyuryo,ukeiresoujyuryou,jikilsunpoumax,jikilsunpoumin,jikiwsunpoumax,jikiwsunpoumin,"
                + "jikitsunpoumax,jikitsunpoumin,kyakusaki,sagyobasyo,gouki1,setteisya1,pastehinmei,pastelotno,pastesaiseikaisuu,pastenendo"
                + ",startdatetime,enddatetime,psunpouave2,dipjigusize,dipjigumaisuu,dipgogaikankekka,bikou1,bikou2,lsunpouave2,lsunpoumin2,lsunpoumax2"
                + ",psunpoumin2,psunpoumax2,setubisyurui,tofukaisuu,hojijigu,nentyakusheetlot1,nentyakusheetlot2,tofujigutorikosuu,StartTantosyacode"
                + ",StartKakuninsyacode,juryou,syorikosuu,EndTantosyacode,pasteatsumi1ji,pasteatsumi2ji,atsumiinkua,atsumiinkub,tanmenatsumi2,kaisuu,torokunichiji"
                + ",kosinnichiji,revision,deleteflag "
                + " FROM tmp_sr_term "
                + " WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? AND kaisuu = ? AND deleteflag = ? ";
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
        mapping.put("kojyo", "kojyo");                                 //工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                 //ﾛｯﾄNo
        mapping.put("edaban", "edaban");                               //枝番
        mapping.put("lotpre", "lotpre");                               //ﾛｯﾄﾌﾟﾚ
        mapping.put("kcpno", "kcpno");                                 //KCPNO
        mapping.put("suuryou", "suuryou");                             //数量
        mapping.put("ukeiretannijyuryo", "ukeiretannijyuryo");         //受入れ単位重量
        mapping.put("ukeiresoujyuryou", "ukeiresoujyuryou");           //受入れ総重量
        mapping.put("jikilsunpoumax", "jikilsunpoumax");               //磁器L寸法(MAX)
        mapping.put("jikilsunpoumin", "jikilsunpoumin");               //磁器L寸法(MIN)
        mapping.put("jikiwsunpoumax", "jikiwsunpoumax");               //磁器W寸法(MAX)
        mapping.put("jikiwsunpoumin", "jikiwsunpoumin");               //磁器W寸法(MIN)
        mapping.put("jikitsunpoumax", "jikitsunpoumax");               //磁器T寸法(MAX)
        mapping.put("jikitsunpoumin", "jikitsunpoumin");               //磁器T寸法(MIN)
        mapping.put("kyakusaki", "kyakusaki");                         //客先
        mapping.put("sagyobasyo", "sagyobasyo");                       //作業場所
        mapping.put("gouki1", "gouki1");                               //号機1
        mapping.put("setteisya1", "setteisya1");                       //条件設定者1
        mapping.put("pastehinmei", "pastehinmei");                     //ﾍﾟｰｽﾄ品名
        mapping.put("pastelotno", "pastelotno");                       //ﾍﾟｰｽﾄﾛｯﾄNo
        mapping.put("pastesaiseikaisuu", "pastesaiseikaisuu");         //ﾍﾟｰｽﾄ再生回数
        mapping.put("pastenendo", "pastenendo");                       //ﾍﾟｰｽﾄ粘度
        mapping.put("startdatetime", "startdatetime");                 //開始日時
        mapping.put("enddatetime", "enddatetime");                     //終了日時
        mapping.put("psunpouave2", "psunpouave2");                     //P寸法AVE
        mapping.put("dipjigusize", "dipjigusize");                     //DIP治具ｻｲｽﾞ
        mapping.put("dipjigumaisuu", "dipjigumaisuu");                 //DIP治具枚数
        mapping.put("dipgogaikankekka", "dipgogaikankekka");           //DIP後外観結果
        mapping.put("bikou1", "bikou1");                               //備考1
        mapping.put("bikou2", "bikou2");                               //備考2
        mapping.put("lsunpouave2", "lsunpouave2");                     //L寸法AVE
        mapping.put("lsunpoumin2", "lsunpoumin2");                     //L寸法MIN
        mapping.put("lsunpoumax2", "lsunpoumax2");                     //L寸法MAX
        mapping.put("psunpoumin2", "psunpoumin2");                     //P寸法MIN
        mapping.put("psunpoumax2", "psunpoumax2");                     //P寸法MAX
        mapping.put("setubisyurui", "setubisyurui");                   //設備種類
        mapping.put("tofukaisuu", "tofukaisuu");                       //塗布回数
        mapping.put("hojijigu", "hojijigu");                           //保持ｼﾞｸﾞ
        mapping.put("nentyakusheetlot1", "nentyakusheetlot1");         //粘着ｼｰﾄﾛｯﾄ1次側
        mapping.put("nentyakusheetlot2", "nentyakusheetlot2");         //粘着ｼｰﾄﾛｯﾄ2次側
        mapping.put("tofujigutorikosuu", "tofujigutorikosuu");         //塗布ｼﾞｸﾞ取り個数
        mapping.put("StartTantosyacode", "starttantosyacode");         //開始担当者
        mapping.put("StartKakuninsyacode", "startkakuninsyacode");     //開始確認者
        mapping.put("juryou", "juryou");                               //重量
        mapping.put("syorikosuu", "syorikosuu");                       //処理個数
        mapping.put("EndTantosyacode", "endtantosyacode");             //終了担当者
        mapping.put("pasteatsumi1ji", "pasteatsumi1ji");               //1次ﾍﾟｰｽﾄ厚み設定値
        mapping.put("pasteatsumi2ji", "pasteatsumi2ji");               //2次ﾍﾟｰｽﾄ厚み設定値
        mapping.put("atsumiinkua", "atsumiinkua");                     //ｲﾝｸ厚みA
        mapping.put("atsumiinkub", "atsumiinkub");                     //ｲﾝｸ厚みB
        mapping.put("tanmenatsumi2", "tanmenatsumi2");                 //端面厚み
        mapping.put("kaisuu", "kaisuu");                               //回数
        mapping.put("torokunichiji", "torokunichiji");                 //登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                   //更新日時
        mapping.put("revision", "revision");                           //revision
        mapping.put("deleteflag", "deleteflag");                       //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrTerm>> beanHandler = new BeanListHandler<>(SrTerm.class, rowProcessor);

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

            // 外部電極塗布データ取得
            List<SrTerm> srTermDataList = getSrTermData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban, paramJissekino);
            if (srTermDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // メイン画面データ設定
            setInputItemDataMainForm(processData, srTermDataList.get(0));

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
     * @param srTermData 外部電極塗布
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrTerm srTermData) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srTermData != null) {
            // 元データが存在する場合元データより取得
            return getSrTermItemData(itemId, srTermData);
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
     * 外部電極塗布_仮登録(tmp_sr_term)登録処理
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
    private void insertTmpSrTerm(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {

        String sql = "INSERT INTO tmp_sr_term ("
                + " kojyo,lotno,edaban,lotpre,kcpno,suuryou,ukeiretannijyuryo,ukeiresoujyuryou,jikilsunpoumax,jikilsunpoumin,jikiwsunpoumax,jikiwsunpoumin,"
                + "jikitsunpoumax,jikitsunpoumin,kyakusaki,sagyobasyo,gouki1,setteisya1,pastehinmei,pastelotno,pastesaiseikaisuu,pastenendo"
                + ",startdatetime,enddatetime,psunpouave2,dipjigusize,dipjigumaisuu,dipgogaikankekka,bikou1,bikou2,lsunpouave2,lsunpoumin2,lsunpoumax2"
                + ",psunpoumin2,psunpoumax2,setubisyurui,tofukaisuu,hojijigu,nentyakusheetlot1,nentyakusheetlot2,tofujigutorikosuu,StartTantosyacode"
                + ",StartKakuninsyacode,juryou,syorikosuu,EndTantosyacode,pasteatsumi1ji,pasteatsumi2ji,atsumiinkua,atsumiinkub,tanmenatsumi2,kaisuu,torokunichiji"
                + ",kosinnichiji,revision,deleteflag"
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrTerm(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, itemList, null, jissekino);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 外部電極塗布_仮登録(tmp_sr_term)更新処理
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
    private void updateTmpSrTerm(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {

        String sql = "UPDATE tmp_sr_term SET "
                + " lotpre = ?,kcpno = ?,suuryou = ?,ukeiretannijyuryo = ?,ukeiresoujyuryou = ?,jikilsunpoumax = ?,jikilsunpoumin = ?,jikiwsunpoumax = ?,"
                + "jikiwsunpoumin = ?,jikitsunpoumax = ?,jikitsunpoumin = ?,kyakusaki = ?,sagyobasyo = ?,gouki1 = ?,setteisya1 = ?,pastehinmei = ?,pastelotno = ?,"
                + "pastesaiseikaisuu = ?,pastenendo = ?,startdatetime = ?,enddatetime = ?,psunpouave2 = ?,dipjigusize = ?,dipjigumaisuu = ?,"
                + "dipgogaikankekka = ?,bikou1 = ?,bikou2 = ?,lsunpouave2 = ?,lsunpoumin2 = ?,lsunpoumax2 = ?,psunpoumin2 = ?,psunpoumax2 = ?,"
                + "setubisyurui = ?,tofukaisuu = ?,hojijigu = ?,nentyakusheetlot1 = ?,nentyakusheetlot2 = ?,tofujigutorikosuu = ?,StartTantosyacode = ?,"
                + "StartKakuninsyacode = ?,juryou = ?,syorikosuu = ?,EndTantosyacode = ?,pasteatsumi1ji = ?,pasteatsumi2ji = ?,atsumiinkua = ?,"
                + "atsumiinkub = ?,tanmenatsumi2 = ?,kosinnichiji = ?,revision = ?,deleteflag = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrTerm> srSrTermList = getSrTermData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekino);
        SrTerm srTerm = null;
        if (!srSrTermList.isEmpty()) {
            srTerm = srSrTermList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrTerm(false, newRev, 0, "", "", "", systemTime, itemList, srTerm, jissekino);

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
     * 外部電極塗布_仮登録(tmp_sr_term)削除処理
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
    private void deleteTmpSrTerm(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        String sql = "DELETE FROM tmp_sr_term "
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
     * 外部電極塗布_仮登録(tmp_sr_term)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srTermData 外部電極塗布データ
     * @param jissekino 実績No
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrTerm(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList, SrTerm srTermData, int jissekino) {
        List<Object> params = new ArrayList<>();
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.LOTPRE, srTermData))); //ﾛｯﾄﾌﾟﾚ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.KCPNO, srTermData))); //KCPNO
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.SUURYOU, srTermData))); //数量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.UKEIRETANNIJYURYO, srTermData))); //受入れ単位重量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.UKEIRESOUJYURYOU, srTermData))); //受入れ総重量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.JIKILSUNPOUMAX, srTermData))); //磁器L寸法(MAX)
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.JIKILSUNPOUMIN, srTermData))); //磁器L寸法(MIN)
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.JIKIWSUNPOUMAX, srTermData))); //磁器W寸法(MAX)
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.JIKIWSUNPOUMIN, srTermData))); //磁器W寸法(MIN)
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.JIKITSUNPOUMAX, srTermData))); //磁器T寸法(MAX)
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.JIKITSUNPOUMIN, srTermData))); //磁器T寸法(MIN)
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.KYAKUSAKI, srTermData))); //客先
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.SAGYOBASYO, srTermData))); //作業場所
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.GOUKI1, srTermData))); //号機1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.SETTEISYA1, srTermData))); //条件設定者1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.PASTEHINMEI, srTermData))); //ﾍﾟｰｽﾄ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.PASTELOTNO, srTermData))); //ﾍﾟｰｽﾄﾛｯﾄNo
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.PASTESAISEIKAISUU, srTermData))); //ﾍﾟｰｽﾄ再生回数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.PASTENENDO, srTermData))); //ﾍﾟｰｽﾄ粘度
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.KAISHI_DAY, srTermData),
            getItemData(itemList, GXHDO101B026Const.KAISHI_TIME, srTermData))); // 開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.SHURYOU_DAY, srTermData),
            getItemData(itemList, GXHDO101B026Const.SHURYOU_TIME, srTermData))); // 終了日時
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.PSUNPOUAVE2, srTermData)));  // P寸法AVE
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.DIPJIGUSIZE, srTermData))); //DIP治具ｻｲｽﾞ
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.DIPJIGUMAISUU, srTermData))); //DIP治具枚数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.DIPGOGAIKANKEKKA, srTermData))); // DIP後外観結果
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.BIKO1, srTermData))); // 備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.BIKO2, srTermData))); // 備考2
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.LSUNPOUAVE2, srTermData)));  // L寸法AVE
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.LSUNPOUMIN2, srTermData)));  // L寸法MIN
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.LSUNPOUMAX2, srTermData)));  // L寸法MAX
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.PSUNPOUMIN2, srTermData))); //P寸法MIN
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.PSUNPOUMAX2, srTermData))); //P寸法MAX
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.SETUBISYURUI, srTermData))); // 設備種類
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.TOFUKAISUU, srTermData))); // 塗布回数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.HOJIJIGU, srTermData))); // 保持ｼﾞｸﾞ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.NENTYAKUSHEETLOT1, srTermData))); // 粘着ｼｰﾄﾛｯﾄ  1次側
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.NENTYAKUSHEETLOT2, srTermData))); // 粘着ｼｰﾄﾛｯﾄ  2次側
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.TOFUJIGUTORIKOSUU, srTermData))); // 塗布ｼﾞｸﾞ取り個数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.KAISHI_TANTOUSYA, srTermData))); // 開始担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.KAISHI_KAKUNINSYA, srTermData))); // 開始確認者
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.JURYOU, srTermData))); //重量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.SYORIKOSUU, srTermData))); // 処理個数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.SHURYOU_TANTOUSYA, srTermData))); // 終了担当者
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.PASTEATSUMI1JI, srTermData))); //1次ﾍﾟｰｽﾄ厚み設定値
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.PASTEATSUMI2JI, srTermData))); //2次ﾍﾟｰｽﾄ厚み設定値
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.ATSUMIINKUA, srTermData))); // ｲﾝｸ厚みA
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.ATSUMIINKUB, srTermData))); // ｲﾝｸ厚みB
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B026Const.TANMENATSUMI2, srTermData))); // 端面厚み
        if (isInsert) {
            params.add(jissekino); // 回数
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
     * 外部電極塗布(sr_term)登録処理
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
     * @param tmpSrTerm 仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrTerm(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino,Timestamp systemTime, List<FXHDD01> itemList, SrTerm tmpSrTerm) throws SQLException {

        String sql = "INSERT INTO sr_term ("
                + " kojyo,lotno,edaban,lotpre,kcpno,suuryou,ukeiretannijyuryo,ukeiresoujyuryou,jikilsunpoumax,jikilsunpoumin,jikiwsunpoumax,jikiwsunpoumin,jikitsunpoumax"
                + ",jikitsunpoumin,kyakusaki,sagyobasyo,gouki1,setteisya1,gouki2,setteisya2,pastehinmei,pastelotno,pastesaiseikaisuu"
                + ",pastekoukanjikan,pastenendo,pasteondo,pastekansannendo,pastekigen,startdatetime,enddatetime,senkougaikan,jikilsunpou,jikitsunpou,psunpou1a"
                + ",psunpou1b,psunpou1c,psunpou1d,psunpou1e,psunpouave1,psunpourange1,lsunpou1a,lsunpou1b,lsunpou1c,lsunpou1d,lsunpou1e,tanmenatsumi1"
                + ",sunpouhantei1,psunpou2a,psunpou2b,psunpou2c,psunpou2d,psunpou2e,psunpouave2,psunpourange2,lsunpou2a,lsunpou2b,lsunpou2c,lsunpou2d,lsunpou2e"
                + ",tanmenatsumi2,sunpouhantei2,pasteatsumi1,pasteatsumi2,dipjigusize,dipjigumaisuu,dipgogaikankekka,syochinaiyou,inkuatsumia,inkuatsumib"
                + ",setteijyouken,bikou1,bikou2,bikou3,psunpou1f,psunpou1g,psunpou1h,psunpou1i,psunpou1j,lsunpou1f,lsunpou1g,lsunpou1h,lsunpou1i,lsunpou1j"
                + ",psunpou2f,psunpou2g,psunpou2h,psunpou2i,psunpou2j,lsunpou2f,lsunpou2g,lsunpou2h,lsunpou2i,lsunpou2j,lsunpouave1,lsunpouave2,pastekokeibun"
                + ",Dip1AtamaDashiSettei,Dip1Clearance,Dip1SkeegeSettei,Dip1BlotClearance,Dip1Reveler,Dip2AtamaDashiSettei,Dip2Clearance,Dip2SkeegeSettei"
                + ",Dip2BlotClearance,Dip2Reveler,psunpou2k,psunpou2l,psunpou2m,psunpou2n,psunpou2o,psunpou2p,psunpou2q,psunpou2r,psunpou2s,psunpou2t,psunpou2u"
                + ",psunpou2v,psunpou2w,psunpou2x,psunpou2y,psunpou2z,psunpou2aa,psunpou2ab,psunpou2ac,psunpou2ad,psunpou2ae,psunpou2af,psunpou2ag,psunpou2ah"
                + ",psunpou2ai,psunpou2aj,psunpou2ak,psunpou2al,psunpou2am,psunpou2an,lsunpou2k,lsunpou2l,lsunpou2m,lsunpou2n,lsunpou2o,lsunpou2p,lsunpou2q"
                + ",lsunpou2r,lsunpou2s,lsunpou2t,lsunpou2u,lsunpou2v,lsunpou2w,lsunpou2x,lsunpou2y,lsunpou2z,lsunpou2aa,lsunpou2ab,lsunpou2ac,lsunpou2ad"
                + ",lsunpou2ae,lsunpou2af,lsunpou2ag,lsunpou2ah,lsunpou2ai,lsunpou2aj,lsunpou2ak,lsunpou2al,lsunpou2am,lsunpou2an,lsunpourange2,lsunpoumin2"
                + ",lsunpoumax2,wtsunpou2a,wtsunpou2b,wtsunpou2c,wtsunpou2d,wtsunpou2e,wtsunpou2f,wtsunpou2g,wtsunpou2h,wtsunpou2i,wtsunpou2j,wtsunpou2k,wtsunpou2l"
                + ",wtsunpou2m,wtsunpou2n,wtsunpou2o,wtsunpou2p,wtsunpou2q,wtsunpou2r,wtsunpou2s,wtsunpou2t,wtsunpou2u,wtsunpou2v,wtsunpou2w,wtsunpou2x,wtsunpou2y"
                + ",wtsunpou2z,wtsunpou2aa,wtsunpou2ab,wtsunpou2ac,wtsunpou2ad,wtsunpou2ae,wtsunpou2af,wtsunpou2ag,wtsunpou2ah,wtsunpou2ai,wtsunpou2aj,wtsunpou2ak"
                + ",wtsunpou2al,wtsunpou2am,wtsunpou2an,wtsunpouave2,wtsunpourange2,wtsunpoumin2,wtsunpoumax2,psunpoumin2,psunpoumax2,setubisyurui,tofukaisuu"
                + ",hojijigu,nentyakusheetlot1,nentyakusheetlot2,tofujigutorikosuu,StartTantosyacode,StartKakuninsyacode,juryou,syorikosuu,EndTantosyacode"
                + ",pasteatsumi1ji,pasteatsumi2ji,atsumiinkua,atsumiinkub,kaisuu,torokunichiji,kosinnichiji,revision"
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
                + ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
                + ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
                + ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrTerm(true, newRev, kojyo, lotNo, edaban, jissekino, systemTime, itemList, tmpSrTerm);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 外部電極塗布(sr_term)更新処理
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
    private void updateSrTerm(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {
        String sql = "UPDATE sr_term SET "
                + " lotpre = ?,kcpno = ?,suuryou = ?,ukeiretannijyuryo = ?,ukeiresoujyuryou = ?,jikilsunpoumax = ?,jikilsunpoumin = ?,jikiwsunpoumax = ?,"
                + "jikiwsunpoumin = ?,jikitsunpoumax = ?,jikitsunpoumin = ?,kyakusaki = ?,sagyobasyo = ?,gouki1 = ?,setteisya1 = ?,pastehinmei = ?,pastelotno = ?,"
                + "pastesaiseikaisuu = ?,pastenendo = ?,startdatetime = ?,enddatetime = ?,psunpouave2 = ?,dipjigusize = ?,dipjigumaisuu = ?,"
                + "dipgogaikankekka = ?,bikou1 = ?,bikou2 = ?,lsunpouave2 = ?,lsunpoumin2 = ?,lsunpoumax2 = ?,psunpoumin2 = ?,psunpoumax2 = ?,"
                + "setubisyurui = ?,tofukaisuu = ?,hojijigu = ?,nentyakusheetlot1 = ?,nentyakusheetlot2 = ?,tofujigutorikosuu = ?,StartTantosyacode = ?,"
                + "StartKakuninsyacode = ?,juryou = ?,syorikosuu = ?,EndTantosyacode = ?,pasteatsumi1ji = ?,pasteatsumi2ji = ?,atsumiinkua = ?,"
                + "atsumiinkub = ?,tanmenatsumi2 = ?,kosinnichiji = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrTerm> srTermList = getSrTermData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekino);
        SrTerm srTerm = null;
        if (!srTermList.isEmpty()) {
            srTerm = srTermList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrTerm(false, newRev, "", "", "", jissekino, systemTime, itemList, srTerm);

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
     * 外部電極塗布(sr_term)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srTermData 外部電極塗布データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrTerm(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            int jissekino, Timestamp systemTime, List<FXHDD01> itemList, SrTerm srTermData) {
        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo);  // 工場ｺｰﾄﾞ
            params.add(lotNo);  // ﾛｯﾄNo
            params.add(edaban); // 枝番
        }

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B026Const.LOTPRE, srTermData))); //ﾛｯﾄﾌﾟﾚ
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B026Const.KCPNO, srTermData))); //KCPNO
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B026Const.SUURYOU, srTermData))); //数量
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B026Const.UKEIRETANNIJYURYO, srTermData))); //受入れ単位重量
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B026Const.UKEIRESOUJYURYOU, srTermData))); //受入れ総重量
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B026Const.JIKILSUNPOUMAX, srTermData))); //磁器L寸法(MAX)
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B026Const.JIKILSUNPOUMIN, srTermData))); //磁器L寸法(MIN)
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B026Const.JIKIWSUNPOUMAX, srTermData))); //磁器W寸法(MAX)
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B026Const.JIKIWSUNPOUMIN, srTermData))); //磁器W寸法(MIN)
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B026Const.JIKITSUNPOUMAX, srTermData))); //磁器T寸法(MAX)
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B026Const.JIKITSUNPOUMIN, srTermData))); //磁器T寸法(MIN)
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B026Const.KYAKUSAKI, srTermData))); //客先
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B026Const.SAGYOBASYO, srTermData))); //作業場所
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B026Const.GOUKI1, srTermData))); //号機1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B026Const.SETTEISYA1, srTermData))); //条件設定者1
        if (isInsert) {
            params.add("");  // 号機2
            params.add("");  // 条件設定者2
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B026Const.PASTEHINMEI, srTermData))); //ﾍﾟｰｽﾄ品名
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B026Const.PASTELOTNO, srTermData))); //ﾍﾟｰｽﾄﾛｯﾄNo
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B026Const.PASTESAISEIKAISUU, srTermData))); //ﾍﾟｰｽﾄ再生回数
        if (isInsert) {
            params.add("");  // ﾍﾟｰｽﾄ交換時間
        }
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B026Const.PASTENENDO, srTermData))); //ﾍﾟｰｽﾄ粘度
        if (isInsert) {
            params.add(0);  // ﾍﾟｰｽﾄ温度
            params.add(0);  // ﾍﾟｰｽﾄ換算粘度
            params.add("");  // ﾍﾟｰｽﾄ期限
        }
        params.add(DBUtil.stringToDateObject(getItemData(itemList, GXHDO101B026Const.KAISHI_DAY, srTermData),
            getItemData(itemList, GXHDO101B026Const.KAISHI_TIME, srTermData))); // 開始日時
        params.add(DBUtil.stringToDateObject(getItemData(itemList, GXHDO101B026Const.SHURYOU_DAY, srTermData),
            getItemData(itemList, GXHDO101B026Const.SHURYOU_TIME, srTermData))); // 終了日時
        if (isInsert) {
            params.add("");  // 先行外観
            params.add(0);  // 磁器L寸法
            params.add(0);  // 磁器T寸法
            params.add(0);  // P寸法1OLD
            params.add(0);  // P寸法2OLD
            params.add(0);  // P寸法3OLD
            params.add(0);  // P寸法4OLD
            params.add(0);  // P寸法5OLD
            params.add(0);  // P寸法AVEOLD
            params.add(0);  // P寸法RANGEOLD
            params.add(0);  // L寸法1OLD
            params.add(0);  // L寸法2OLD
            params.add(0);  // L寸法3OLD
            params.add(0);  // L寸法4OLD
            params.add(0);  // L寸法5OLD
            params.add(0);  // 端面厚みOLD
            params.add("");  // 判定OLD
            params.add(0);  // P寸法1
            params.add(0);  // P寸法2
            params.add(0);  // P寸法3
            params.add(0);  // P寸法4
            params.add(0);  // P寸法5
        }
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B026Const.PSUNPOUAVE2, srTermData)));  // P寸法AVE
        if (isInsert) {
            params.add(0);  // P寸法RANGE
            params.add(0);  // L寸法1
            params.add(0);  // L寸法2
            params.add(0);  // L寸法3
            params.add(0);  // L寸法4
            params.add(0);  // L寸法5
            //params.add(0);  // 端面厚み
            params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B026Const.TANMENATSUMI2, srTermData)));  // 端面厚み
            params.add("");  // 判定
            params.add(0);  // ﾍﾟｰｽﾄ厚み設定値1次
            params.add(0);  // ﾍﾟｰｽﾄ厚み設定値2次
        }
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B026Const.DIPJIGUSIZE, srTermData))); //DIP治具ｻｲｽﾞ
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B026Const.DIPJIGUMAISUU, srTermData))); //DIP治具枚数
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B026Const.DIPGOGAIKANKEKKA, srTermData))); // DIP後外観結果
        if (isInsert) {
            params.add("");  // 処置内容
            params.add("");  // ｲﾝｸ厚みa
            params.add("");  // ｲﾝｸ厚みb
            params.add("");  // 設定条件
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B026Const.BIKO1, srTermData))); // 備考1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B026Const.BIKO2, srTermData))); // 備考2
        if (isInsert) {
            params.add("");  // 備考3
            params.add(0);  // P寸法6OLD
            params.add(0);  // P寸法7OLD
            params.add(0);  // P寸法8OLD
            params.add(0);  // P寸法9OLD
            params.add(0);  // P寸法10OLD
            params.add(0);  // L寸法6OLD
            params.add(0);  // L寸法7OLD
            params.add(0);  // L寸法8OLD
            params.add(0);  // L寸法9OLD
            params.add(0);  // L寸法10OLD
            params.add(0);  // P寸法6
            params.add(0);  // P寸法7
            params.add(0);  // P寸法8
            params.add(0);  // P寸法9
            params.add(0);  // P寸法10
            params.add(0);  // L寸法6
            params.add(0);  // L寸法7
            params.add(0);  // L寸法8
            params.add(0);  // L寸法9
            params.add(0);  // L寸法10
            params.add(0);  // L寸法AVEOLD
        }
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B026Const.LSUNPOUAVE2, srTermData)));  // L寸法AVE
        if (isInsert) {
            params.add(0);  // ﾍﾟｰｽﾄ固形分
            params.add(0);  // 1次頭出し設定
            params.add(0);  // 1次DIPｸﾘｱﾗﾝｽ設定
            params.add(0);  // 1次DIPｽｷｰｼﾞ設定
            params.add(0);  // 1次ﾌﾞﾛｯﾄｸﾘｱﾗﾝｽ設定
            params.add(0);  // 1次ﾚﾍﾞﾗｰ設定
            params.add(0);  // 2次頭出し設定
            params.add(0);  // 2次DIPｸﾘｱﾗﾝｽ設定
            params.add(0);  // 2次DIPｽｷｰｼﾞ設定
            params.add(0);  // 2次ﾌﾞﾛｯﾄｸﾘｱﾗﾝｽ設定
            params.add(0);  // 2次ﾚﾍﾞﾗｰ設定
            params.add(0);  // P寸法11
            params.add(0);  // P寸法12
            params.add(0);  // P寸法13
            params.add(0);  // P寸法14
            params.add(0);  // P寸法15
            params.add(0);  // P寸法16
            params.add(0);  // P寸法17
            params.add(0);  // P寸法18
            params.add(0);  // P寸法19
            params.add(0);  // P寸法20
            params.add(0);  // P寸法21
            params.add(0);  // P寸法22
            params.add(0);  // P寸法23
            params.add(0);  // P寸法24
            params.add(0);  // P寸法25
            params.add(0);  // P寸法26
            params.add(0);  // P寸法27
            params.add(0);  // P寸法28
            params.add(0);  // P寸法29
            params.add(0);  // P寸法30
            params.add(0);  // P寸法31
            params.add(0);  // P寸法32
            params.add(0);  // P寸法33
            params.add(0);  // P寸法34
            params.add(0);  // P寸法35
            params.add(0);  // P寸法36
            params.add(0);  // P寸法37
            params.add(0);  // P寸法38
            params.add(0);  // P寸法39
            params.add(0);  // P寸法40
            params.add(0);  // L寸法11
            params.add(0);  // L寸法12
            params.add(0);  // L寸法13
            params.add(0);  // L寸法14
            params.add(0);  // L寸法15
            params.add(0);  // L寸法16
            params.add(0);  // L寸法17
            params.add(0);  // L寸法18
            params.add(0);  // L寸法19
            params.add(0);  // L寸法20
            params.add(0);  // L寸法21
            params.add(0);  // L寸法22
            params.add(0);  // L寸法23
            params.add(0);  // L寸法24
            params.add(0);  // L寸法25
            params.add(0);  // L寸法26
            params.add(0);  // L寸法27
            params.add(0);  // L寸法28
            params.add(0);  // L寸法29
            params.add(0);  // L寸法30
            params.add(0);  // L寸法31
            params.add(0);  // L寸法32
            params.add(0);  // L寸法33
            params.add(0);  // L寸法34
            params.add(0);  // L寸法35
            params.add(0);  // L寸法36
            params.add(0);  // L寸法37
            params.add(0);  // L寸法38
            params.add(0);  // L寸法39
            params.add(0);  // L寸法40
            params.add(0);  // L寸法RANGE
        }
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B026Const.LSUNPOUMIN2, srTermData)));  // L寸法MIN
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B026Const.LSUNPOUMAX2, srTermData)));  // L寸法MAX
        if (isInsert) {
            params.add(0);  // WT寸法1
            params.add(0);  // WT寸法2
            params.add(0);  // WT寸法3
            params.add(0);  // WT寸法4
            params.add(0);  // WT寸法5
            params.add(0);  // WT寸法6
            params.add(0);  // WT寸法7
            params.add(0);  // WT寸法8
            params.add(0);  // WT寸法9
            params.add(0);  // WT寸法10
            params.add(0);  // WT寸法11
            params.add(0);  // WT寸法12
            params.add(0);  // WT寸法13
            params.add(0);  // WT寸法14
            params.add(0);  // WT寸法15
            params.add(0);  // WT寸法16
            params.add(0);  // WT寸法17
            params.add(0);  // WT寸法18
            params.add(0);  // WT寸法19
            params.add(0);  // WT寸法20
            params.add(0);  // WT寸法21
            params.add(0);  // WT寸法22
            params.add(0);  // WT寸法23
            params.add(0);  // WT寸法24
            params.add(0);  // WT寸法25
            params.add(0);  // WT寸法26
            params.add(0);  // WT寸法27
            params.add(0);  // WT寸法28
            params.add(0);  // WT寸法29
            params.add(0);  // WT寸法30
            params.add(0);  // WT寸法31
            params.add(0);  // WT寸法32
            params.add(0);  // WT寸法33
            params.add(0);  // WT寸法34
            params.add(0);  // WT寸法35
            params.add(0);  // WT寸法36
            params.add(0);  // WT寸法37
            params.add(0);  // WT寸法38
            params.add(0);  // WT寸法39
            params.add(0);  // WT寸法40
            params.add(0);  // WT寸法AVE
            params.add(0);  // WT寸法RANGE
            params.add(0);  // WT寸法MIN
            params.add(0);  // WT寸法MAX
        }
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B026Const.PSUNPOUMIN2, srTermData))); //P寸法MIN
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B026Const.PSUNPOUMAX2, srTermData))); //P寸法MAX
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B026Const.SETUBISYURUI, srTermData))); // 設備種類
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B026Const.TOFUKAISUU, srTermData))); // 塗布回数
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B026Const.HOJIJIGU, srTermData))); // 保持ｼﾞｸﾞ
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B026Const.NENTYAKUSHEETLOT1, srTermData))); // 粘着ｼｰﾄﾛｯﾄ  1次側
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B026Const.NENTYAKUSHEETLOT2, srTermData))); // 粘着ｼｰﾄﾛｯﾄ  2次側
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B026Const.TOFUJIGUTORIKOSUU, srTermData))); // 塗布ｼﾞｸﾞ取り個数
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B026Const.KAISHI_TANTOUSYA, srTermData))); // 開始担当者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B026Const.KAISHI_KAKUNINSYA, srTermData))); // 開始確認者
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B026Const.JURYOU, srTermData))); //重量
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B026Const.SYORIKOSUU, srTermData))); // 処理個数
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B026Const.SHURYOU_TANTOUSYA, srTermData))); // 終了担当者
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B026Const.PASTEATSUMI1JI, srTermData))); //1次ﾍﾟｰｽﾄ厚み設定値
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B026Const.PASTEATSUMI2JI, srTermData))); //2次ﾍﾟｰｽﾄ厚み設定値
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B026Const.ATSUMIINKUA, srTermData))); // ｲﾝｸ厚みA
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B026Const.ATSUMIINKUB, srTermData))); // ｲﾝｸ厚みB
        if (isInsert) {
            params.add(jissekino);  //回数
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時
        } else {
            params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B026Const.TANMENATSUMI2, srTermData)));  // 端面厚み
            params.add(systemTime); //更新日時
        }
        params.add(newRev); //revision
        
        return params;
    }

    /**
     * 外部電極塗布(sr_term)削除処理
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
    private void deleteSrTerm(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        String sql = "DELETE FROM sr_term "
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
     * [外部電極塗布_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_term "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? AND KAISUU = ? ";
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
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B026Const.KAISHI_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B026Const.KAISHI_TIME);
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
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B026Const.SHURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B026Const.SHURYOU_TIME);
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
     * @param srTermData 外部電極塗布データ
     * @return DB値
     */
    private String getSrTermItemData(String itemId, SrTerm srTermData) {
        switch (itemId) {
            //受入れ単位重量
            case GXHDO101B026Const.UKEIRETANNIJYURYO:
                return StringUtil.nullToBlank(srTermData.getUkeiretannijyuryo());
            //受入れ総重量
            case GXHDO101B026Const.UKEIRESOUJYURYOU:
                return StringUtil.nullToBlank(srTermData.getUkeiresoujyuryou());
            //設備種類
            case GXHDO101B026Const.SETUBISYURUI:
                return StringUtil.nullToBlank(srTermData.getSetubisyurui());
            //塗布号機
            case GXHDO101B026Const.GOUKI1:
                return StringUtil.nullToBlank(srTermData.getGouki1());
            //塗布回数
            case GXHDO101B026Const.TOFUKAISUU:
                return StringUtil.nullToBlank(srTermData.getTofukaisuu());
            //ﾍﾟｰｽﾄ品名
            case GXHDO101B026Const.PASTEHINMEI:
                return StringUtil.nullToBlank(srTermData.getPastehinmei());
            //ﾍﾟｰｽﾄﾛｯﾄNo
            case GXHDO101B026Const.PASTELOTNO:
                return StringUtil.nullToBlank(srTermData.getPastelotno());
            //ﾍﾟｰｽﾄ再生回数
            case GXHDO101B026Const.PASTESAISEIKAISUU:
                return StringUtil.nullToBlank(srTermData.getPastesaiseikaisuu());
            //ﾍﾟｰｽﾄ粘度
            case GXHDO101B026Const.PASTENENDO:
                return StringUtil.nullToBlank(srTermData.getPastenendo());
            //条件設定者
            case GXHDO101B026Const.SETTEISYA1:
                return StringUtil.nullToBlank(srTermData.getSetteisya1());
            //ﾍﾟｰｽﾄ厚み設定値1次
            case GXHDO101B026Const.PASTEATSUMI1JI:
                return StringUtil.nullToBlank(srTermData.getPasteatsumi1ji());
            //ﾍﾟｰｽﾄ厚み設定値2次
            case GXHDO101B026Const.PASTEATSUMI2JI:
                return StringUtil.nullToBlank(srTermData.getPasteatsumi2ji());
            //保持ｼﾞｸﾞ
            case GXHDO101B026Const.HOJIJIGU:
                return StringUtil.nullToBlank(srTermData.getHojijigu());
            //粘着ｼｰﾄﾛｯﾄ  1次側
            case GXHDO101B026Const.NENTYAKUSHEETLOT1:
                return StringUtil.nullToBlank(srTermData.getNentyakusheetlot1());
            //粘着ｼｰﾄﾛｯﾄ  2次側
            case GXHDO101B026Const.NENTYAKUSHEETLOT2:
                return StringUtil.nullToBlank(srTermData.getNentyakusheetlot2());
            //塗布ｼﾞｸﾞｻｲｽﾞ
            case GXHDO101B026Const.DIPJIGUSIZE:
                return StringUtil.nullToBlank(srTermData.getDipjigusize());
            //塗布ｼﾞｸﾞ取り個数
            case GXHDO101B026Const.TOFUJIGUTORIKOSUU:
                return StringUtil.nullToBlank(srTermData.getTofujigutorikosuu());
            //塗布ｼﾞｸﾞ枚数
            case GXHDO101B026Const.DIPJIGUMAISUU:
                return StringUtil.nullToBlank(srTermData.getDipjigumaisuu());
            //開始日
            case GXHDO101B026Const.KAISHI_DAY:
                return DateUtil.formattedTimestamp(srTermData.getStartdatetime(), "yyMMdd");
            //開始時刻
            case GXHDO101B026Const.KAISHI_TIME:
                return DateUtil.formattedTimestamp(srTermData.getStartdatetime(), "HHmm");
            //開始担当者
            case GXHDO101B026Const.KAISHI_TANTOUSYA:
                return StringUtil.nullToBlank(srTermData.getStarttantosyacode());
            //開始確認者
            case GXHDO101B026Const.KAISHI_KAKUNINSYA:
                return StringUtil.nullToBlank(srTermData.getStartkakuninsyacode());
            //ｲﾝｸ厚みA
            case GXHDO101B026Const.ATSUMIINKUA:
                return StringUtil.nullToBlank(srTermData.getAtsumiinkua());
            //ｲﾝｸ厚みB
            case GXHDO101B026Const.ATSUMIINKUB:
                return StringUtil.nullToBlank(srTermData.getAtsumiinkub());
            //端面厚み
            case GXHDO101B026Const.TANMENATSUMI2:
                return StringUtil.nullToBlank(srTermData.getTanmenatsumi2());
            //P寸法 AVE
            case GXHDO101B026Const.PSUNPOUAVE2:
                return StringUtil.nullToBlank(srTermData.getPsunpouave2());
            //P寸法 MAX
            case GXHDO101B026Const.PSUNPOUMAX2:
                return StringUtil.nullToBlank(srTermData.getPsunpoumax2());
            //P寸法 MIN
            case GXHDO101B026Const.PSUNPOUMIN2:
                return StringUtil.nullToBlank(srTermData.getPsunpoumin2());
            //L寸法 AVE
            case GXHDO101B026Const.LSUNPOUAVE2:
                return StringUtil.nullToBlank(srTermData.getLsunpouave2());
            //L寸法 MAX
            case GXHDO101B026Const.LSUNPOUMAX2:
                return StringUtil.nullToBlank(srTermData.getLsunpoumax2());
            //L寸法 MIN
            case GXHDO101B026Const.LSUNPOUMIN2:
                return StringUtil.nullToBlank(srTermData.getLsunpoumin2());
            //塗布後外観結果
            case GXHDO101B026Const.DIPGOGAIKANKEKKA:
                return StringUtil.nullToBlank(srTermData.getDipgogaikankekka());
            //処理個数
            case GXHDO101B026Const.SYORIKOSUU:
                return StringUtil.nullToBlank(srTermData.getSyorikosuu());
            //重量
            case GXHDO101B026Const.JURYOU:
                return StringUtil.nullToBlank(srTermData.getJuryou());
            //終了日
            case GXHDO101B026Const.SHURYOU_DAY:
                return DateUtil.formattedTimestamp(srTermData.getEnddatetime(), "yyMMdd");
            //終了時刻
            case GXHDO101B026Const.SHURYOU_TIME:
                return DateUtil.formattedTimestamp(srTermData.getEnddatetime(), "HHmm");
            //終了担当者
            case GXHDO101B026Const.SHURYOU_TANTOUSYA:
                return StringUtil.nullToBlank(srTermData.getEndtantosyacode());
            //作業場所
            case GXHDO101B026Const.SAGYOBASYO:
                return StringUtil.nullToBlank(srTermData.getSagyobasyo());
            //備考1
            case GXHDO101B026Const.BIKO1:
                return StringUtil.nullToBlank(srTermData.getBikou1());
            //備考2
            case GXHDO101B026Const.BIKO2:
                return StringUtil.nullToBlank(srTermData.getBikou2()); 
            // 処理数
            case GXHDO101B026Const.SUURYOU:
                return StringUtil.nullToBlank(srTermData.getSuuryou());
            // KCPNO
            case GXHDO101B026Const.KCPNO:
                return StringUtil.nullToBlank(srTermData.getKcpno());
            // 客先
            case GXHDO101B026Const.KYAKUSAKI:
                return StringUtil.nullToBlank(srTermData.getKyakusaki());
            // ﾛｯﾄﾌﾟﾚ
            case GXHDO101B026Const.LOTPRE:
                return StringUtil.nullToBlank(srTermData.getLotpre());
            default:
                return null;            
        }
    }

    /**
     * 外部電極塗布_仮登録(tmp_sr_term)登録処理(削除時)
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
    private void insertDeleteDataTmpSrTerm(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_term ("
                + " kojyo,lotno,edaban,lotpre,kcpno,suuryou,kyakusaki,sagyobasyo,gouki1,setteisya1,gouki2,setteisya2,pastehinmei,pastelotno,pastesaiseikaisuu"
                + ",pastekoukanjikan,pastenendo,pasteondo,pastekansannendo,pastekigen,startdatetime,enddatetime,senkougaikan,jikilsunpou,jikitsunpou,psunpou1a"
                + ",psunpou1b,psunpou1c,psunpou1d,psunpou1e,psunpouave1,psunpourange1,lsunpou1a,lsunpou1b,lsunpou1c,lsunpou1d,lsunpou1e,tanmenatsumi1"
                + ",sunpouhantei1,psunpou2a,psunpou2b,psunpou2c,psunpou2d,psunpou2e,psunpouave2,psunpourange2,lsunpou2a,lsunpou2b,lsunpou2c,lsunpou2d,lsunpou2e"
                + ",tanmenatsumi2,sunpouhantei2,pasteatsumi1,pasteatsumi2,dipjigusize,dipjigumaisuu,dipgogaikankekka,syochinaiyou,inkuatsumia,inkuatsumib"
                + ",setteijyouken,bikou1,bikou2,bikou3,psunpou1f,psunpou1g,psunpou1h,psunpou1i,psunpou1j,lsunpou1f,lsunpou1g,lsunpou1h,lsunpou1i,lsunpou1j"
                + ",psunpou2f,psunpou2g,psunpou2h,psunpou2i,psunpou2j,lsunpou2f,lsunpou2g,lsunpou2h,lsunpou2i,lsunpou2j,lsunpouave1,lsunpouave2,pastekokeibun"
                + ",Dip1AtamaDashiSettei,Dip1Clearance,Dip1SkeegeSettei,Dip1BlotClearance,Dip1Reveler,Dip2AtamaDashiSettei,Dip2Clearance,Dip2SkeegeSettei"
                + ",Dip2BlotClearance,Dip2Reveler,psunpou2k,psunpou2l,psunpou2m,psunpou2n,psunpou2o,psunpou2p,psunpou2q,psunpou2r,psunpou2s,psunpou2t,psunpou2u"
                + ",psunpou2v,psunpou2w,psunpou2x,psunpou2y,psunpou2z,psunpou2aa,psunpou2ab,psunpou2ac,psunpou2ad,psunpou2ae,psunpou2af,psunpou2ag,psunpou2ah"
                + ",psunpou2ai,psunpou2aj,psunpou2ak,psunpou2al,psunpou2am,psunpou2an,lsunpou2k,lsunpou2l,lsunpou2m,lsunpou2n,lsunpou2o,lsunpou2p,lsunpou2q"
                + ",lsunpou2r,lsunpou2s,lsunpou2t,lsunpou2u,lsunpou2v,lsunpou2w,lsunpou2x,lsunpou2y,lsunpou2z,lsunpou2aa,lsunpou2ab,lsunpou2ac,lsunpou2ad"
                + ",lsunpou2ae,lsunpou2af,lsunpou2ag,lsunpou2ah,lsunpou2ai,lsunpou2aj,lsunpou2ak,lsunpou2al,lsunpou2am,lsunpou2an,lsunpourange2,lsunpoumin2"
                + ",lsunpoumax2,wtsunpou2a,wtsunpou2b,wtsunpou2c,wtsunpou2d,wtsunpou2e,wtsunpou2f,wtsunpou2g,wtsunpou2h,wtsunpou2i,wtsunpou2j,wtsunpou2k,wtsunpou2l"
                + ",wtsunpou2m,wtsunpou2n,wtsunpou2o,wtsunpou2p,wtsunpou2q,wtsunpou2r,wtsunpou2s,wtsunpou2t,wtsunpou2u,wtsunpou2v,wtsunpou2w,wtsunpou2x,wtsunpou2y"
                + ",wtsunpou2z,wtsunpou2aa,wtsunpou2ab,wtsunpou2ac,wtsunpou2ad,wtsunpou2ae,wtsunpou2af,wtsunpou2ag,wtsunpou2ah,wtsunpou2ai,wtsunpou2aj,wtsunpou2ak"
                + ",wtsunpou2al,wtsunpou2am,wtsunpou2an,wtsunpouave2,wtsunpourange2,wtsunpoumin2,wtsunpoumax2,psunpoumin2,psunpoumax2,setubisyurui,tofukaisuu"
                + ",hojijigu,nentyakusheetlot1,nentyakusheetlot2,tofujigutorikosuu,StartTantosyacode,StartKakuninsyacode,juryou,syorikosuu,EndTantosyacode"
                + ",pasteatsumi1ji,pasteatsumi2ji,atsumiinkua,atsumiinkub,kaisuu,torokunichiji,kosinnichiji,revision,deleteflag"
                + ") SELECT "
                + " kojyo,lotno,edaban,lotpre,kcpno,suuryou,kyakusaki,sagyobasyo,gouki1,setteisya1,gouki2,setteisya2,pastehinmei,pastelotno,pastesaiseikaisuu"
                + ",pastekoukanjikan,pastenendo,pasteondo,pastekansannendo,pastekigen,startdatetime,enddatetime,senkougaikan,jikilsunpou,jikitsunpou,psunpou1a"
                + ",psunpou1b,psunpou1c,psunpou1d,psunpou1e,psunpouave1,psunpourange1,lsunpou1a,lsunpou1b,lsunpou1c,lsunpou1d,lsunpou1e,tanmenatsumi1"
                + ",sunpouhantei1,psunpou2a,psunpou2b,psunpou2c,psunpou2d,psunpou2e,psunpouave2,psunpourange2,lsunpou2a,lsunpou2b,lsunpou2c,lsunpou2d,lsunpou2e"
                + ",tanmenatsumi2,sunpouhantei2,pasteatsumi1,pasteatsumi2,dipjigusize,dipjigumaisuu,dipgogaikankekka,syochinaiyou,inkuatsumia,inkuatsumib"
                + ",setteijyouken,bikou1,bikou2,bikou3,psunpou1f,psunpou1g,psunpou1h,psunpou1i,psunpou1j,lsunpou1f,lsunpou1g,lsunpou1h,lsunpou1i,lsunpou1j"
                + ",psunpou2f,psunpou2g,psunpou2h,psunpou2i,psunpou2j,lsunpou2f,lsunpou2g,lsunpou2h,lsunpou2i,lsunpou2j,lsunpouave1,lsunpouave2,pastekokeibun"
                + ",Dip1AtamaDashiSettei,Dip1Clearance,Dip1SkeegeSettei,Dip1BlotClearance,Dip1Reveler,Dip2AtamaDashiSettei,Dip2Clearance,Dip2SkeegeSettei"
                + ",Dip2BlotClearance,Dip2Reveler,psunpou2k,psunpou2l,psunpou2m,psunpou2n,psunpou2o,psunpou2p,psunpou2q,psunpou2r,psunpou2s,psunpou2t,psunpou2u"
                + ",psunpou2v,psunpou2w,psunpou2x,psunpou2y,psunpou2z,psunpou2aa,psunpou2ab,psunpou2ac,psunpou2ad,psunpou2ae,psunpou2af,psunpou2ag,psunpou2ah"
                + ",psunpou2ai,psunpou2aj,psunpou2ak,psunpou2al,psunpou2am,psunpou2an,lsunpou2k,lsunpou2l,lsunpou2m,lsunpou2n,lsunpou2o,lsunpou2p,lsunpou2q"
                + ",lsunpou2r,lsunpou2s,lsunpou2t,lsunpou2u,lsunpou2v,lsunpou2w,lsunpou2x,lsunpou2y,lsunpou2z,lsunpou2aa,lsunpou2ab,lsunpou2ac,lsunpou2ad"
                + ",lsunpou2ae,lsunpou2af,lsunpou2ag,lsunpou2ah,lsunpou2ai,lsunpou2aj,lsunpou2ak,lsunpou2al,lsunpou2am,lsunpou2an,lsunpourange2,lsunpoumin2"
                + ",lsunpoumax2,wtsunpou2a,wtsunpou2b,wtsunpou2c,wtsunpou2d,wtsunpou2e,wtsunpou2f,wtsunpou2g,wtsunpou2h,wtsunpou2i,wtsunpou2j,wtsunpou2k,wtsunpou2l"
                + ",wtsunpou2m,wtsunpou2n,wtsunpou2o,wtsunpou2p,wtsunpou2q,wtsunpou2r,wtsunpou2s,wtsunpou2t,wtsunpou2u,wtsunpou2v,wtsunpou2w,wtsunpou2x,wtsunpou2y"
                + ",wtsunpou2z,wtsunpou2aa,wtsunpou2ab,wtsunpou2ac,wtsunpou2ad,wtsunpou2ae,wtsunpou2af,wtsunpou2ag,wtsunpou2ah,wtsunpou2ai,wtsunpou2aj,wtsunpou2ak"
                + ",wtsunpou2al,wtsunpou2am,wtsunpou2an,wtsunpouave2,wtsunpourange2,wtsunpoumin2,wtsunpoumax2,psunpoumin2,psunpoumax2,setubisyurui,tofukaisuu"
                + ",hojijigu,nentyakusheetlot1,nentyakusheetlot2,tofujigutorikosuu,StartTantosyacode,StartKakuninsyacode,juryou,syorikosuu,EndTantosyacode"
                + ",pasteatsumi1ji,pasteatsumi2ji,atsumiinkua,atsumiinkub,kaisuu,?,?,?,? "
                + " FROM sr_term "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? ";

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
     * [実績]から、処理数を取得
     *
     * @param queryRunnerWip QueryRunnerオブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param data ﾊﾟﾗﾒｰﾀﾃﾞｰﾀ(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<Jisseki> loadJissekiData(QueryRunner queryRunnerWip, String lotNo, String[] data) throws SQLException {

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
        
        Map<String, String> mapping = new HashMap<>();
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
     * [ﾊﾟﾗﾒｰﾀﾏｽﾀ]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param selectNo 処理区分
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhbm03Data(QueryRunner queryRunnerDoc,Integer selectNo) {
        try {
            
            // ﾊﾟﾗﾒｰﾀﾏｽﾀデータの取得
            String sql = "SELECT data "
                    + " FROM fxhbm03 WHERE user_name = 'common_user' AND ";
                    if(selectNo == 20){                        
                        sql= sql + " key = 'xhd_gaibudenkyoku_dandori_koteicode' ";
                    }
                    if(selectNo == 213){
                       sql= sql + " key = 'xhd_gaibudenkyoku_tofu_3tanshi' ";
                    }
                    if(selectNo == 214){
                       sql= sql + " key = 'xhd_gaibudenkyoku_tofu_4tanshi' ";
                    }
           
            return queryRunnerDoc.query(sql, new MapHandler());

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
        return null;
    }

    /**
     * 印刷幅計算
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setInsatsuhabaKeisan(ProcessData processDate) {

         // 継続メソッドのクリア
        processDate.setMethod("");

        return processDate;
    }
    
    /**
     * 回り込みAVE計算
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setMawarikomiAveKeisan(ProcessData processDate) {

         // 継続メソッドのクリア
        processDate.setMethod("");

        return processDate;
    }
    
    /**
     * 位置ｽﾞﾚMAX計算
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setIchizureMaxKeisan(ProcessData processDate) {

         // 継続メソッドのクリア
        processDate.setMethod("");

        return processDate;
    }
    
    /**
     * 端子間幅MIN計算
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setTansikanhabaMinKeisan(ProcessData processDate) {

         // 継続メソッドのクリア
        processDate.setMethod("");

        return processDate;
    }
}
