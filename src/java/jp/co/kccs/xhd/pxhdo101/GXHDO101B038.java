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
import jp.co.kccs.xhd.db.model.Jisseki;
import jp.co.kccs.xhd.db.model.MekkiRireki;
import jp.co.kccs.xhd.db.model.SrMekki;
import jp.co.kccs.xhd.db.model.SrQasunpou1;
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
 * システム名  品質情報管理システム<br>
 * <br>
 * 変更日      2019/11/05<br>
 * 計画書No    K1811-DS001<br>
 * 変更者      863 F.Zhang<br>
 * 変更理由    新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101B038(外部電極・ﾒｯｷ品質検査)ロジック
 *
 * @author 863 F.Zhang
 * @since  2019/11/05
 */
public class GXHDO101B038 implements IFormLogic {

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
                    GXHDO101B038Const.BTN_START_DATETIME_TOP,
                    GXHDO101B038Const.BTN_END_DATETIME_TOP,
                    GXHDO101B038Const.BTN_WIP_TORIKOMI_TOP,
                    GXHDO101B038Const.BTN_BUDOMARI_KEISAN_TOP,
                    GXHDO101B038Const.BTN_IJOURANK_SHUTOKU_TOP,
                    GXHDO101B038Const.BTN_RIREKI_TORIKOMI_TOP,
                    GXHDO101B038Const.BTN_MAKUATSU_TORIKOMI_TOP,
                    GXHDO101B038Const.BTN_START_DATETIME_BOTTOM,                    
                    GXHDO101B038Const.BTN_END_DATETIME_BOTTOM,
                    GXHDO101B038Const.BTN_WIP_TORIKOMI_BOTTOM,
                    GXHDO101B038Const.BTN_BUDOMARI_KEISAN_BOTTOM,
                    GXHDO101B038Const.BTN_IJOURANK_SHUTOKU_BOTTOM,
                    GXHDO101B038Const.BTN_RIREKI_TORIKOMI_BOTTOM,
                    GXHDO101B038Const.BTN_MAKUATSU_TORIKOMI_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO101B038Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO101B038Const.BTN_INSERT_TOP,
                    GXHDO101B038Const.BTN_DELETE_TOP,
                    GXHDO101B038Const.BTN_UPDATE_TOP,
                    GXHDO101B038Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO101B038Const.BTN_INSERT_BOTTOM,
                    GXHDO101B038Const.BTN_DELETE_BOTTOM,
                    GXHDO101B038Const.BTN_UPDATE_BOTTOM));

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
     * @param processData 処理制御データ
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

                // ﾒｯｷ_仮登録登録処理
                insertTmpSrMekki(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList(), processData);

            } else {

                // ﾒｯｷ_仮登録更新処理
                updateTmpSrMekki(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList(), processData);

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
            
            // 使用ﾄﾞｰﾑ明細の更新値を保持
            FXHDD01 domemeisai = getItemRow(processData.getItemList(), GXHDO101B038Const.DOMEMEISAI);
            if (domemeisai != null) {
                processData.getHiddenDataMap().put("domemeisai", StringUtil.nullToBlank(domemeisai.getValue()));
            }
            
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
        
        QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
        // 項目のチェック処理を行う。
        ErrorMessageInfo checkItemErrorInfo = checkItemResistCorrect(processData, queryRunnerDoc);
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
     * @param processData 処理制御データ
     * @param queryRunnerDoc 
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkItemResistCorrect(ProcessData processData, QueryRunner queryRunnerDoc) {
        
        List<FXHDD01> errFxhdd01List = new ArrayList<>();
        
        // セッションから情報を取得
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        
        // ﾊﾟﾗﾒｰﾀﾃﾞｰﾀ情報の取得
        String strfxhbm03List = "";
        // 実績No指定必須ﾁｪｯｸ処理
        int paramJissekino = (Integer) session.getAttribute("jissekino");
        if (paramJissekino == 1) {
            Map fxhbm03Data21 = loadFxhbm03Data(queryRunnerDoc, 21);
            if (fxhbm03Data21 != null && !fxhbm03Data21.isEmpty()) {
                strfxhbm03List = StringUtil.nullToBlank(getMapData(fxhbm03Data21, "data"));
                String fxhbm03Data[] = strfxhbm03List.split(",");
                for (int i = 0; i < fxhbm03Data.length; i++) {
                    for (FXHDD01 fxhdd001 : processData.getItemList()) {
                        if (fxhdd001.getItemId().equals(fxhbm03Data[i])) {
                            // 実績No指定必須ﾁｪｯｸ処理
                            FXHDD01 checkItem = getItemRow(processData.getItemList(), fxhbm03Data[i]);
                            if (StringUtil.isEmpty(StringUtil.trimAll(checkItem.getValue()))) {
                                //エラー発生時
                                errFxhdd01List = Arrays.asList(checkItem);
                                return MessageUtil.getErrorMessageInfo("XHD-000003", true, true, errFxhdd01List, checkItem.getLabel1());
                            }
                        }
                    } 
                }
            }
        } else if(paramJissekino == 2) {
            Map fxhbm03Data22 = loadFxhbm03Data(queryRunnerDoc, 22);
            if(fxhbm03Data22 != null && !fxhbm03Data22.isEmpty()) {
                strfxhbm03List = StringUtil.nullToBlank(getMapData(fxhbm03Data22, "data"));
                String fxhbm03Data[] = strfxhbm03List.split(",");
                for (int i = 0; i < fxhbm03Data.length; i++) {
                    for (FXHDD01 fxhdd001 : processData.getItemList()) {
                        if (fxhdd001.getItemId().equals(fxhbm03Data[i])) {
                            // 実績No指定必須ﾁｪｯｸ処理
                            FXHDD01 checkItem = getItemRow(processData.getItemList(), fxhbm03Data[i]);
                            if (StringUtil.isEmpty(StringUtil.trimAll(checkItem.getValue()))) {
                                //エラー発生時
                                errFxhdd01List = Arrays.asList(checkItem);
                                return MessageUtil.getErrorMessageInfo("XHD-000003", true, true, errFxhdd01List, checkItem.getLabel1());
                            }
                        }
                    }
                }
            }
        }
        
        // 熱処理/磁石選別必須ﾁｪｯｸ処理
        
        //KCPNO
        FXHDD01 itemKcpno = getItemRow(processData.getItemList(), GXHDO101B038Const.KCPNO);
        String kcpno9 = null;
        if(!StringUtil.isEmpty(itemKcpno.getValue()) && itemKcpno.getValue().length() > 9){
            kcpno9 = itemKcpno.getValue().substring(8, 9);
        }
        // 熱処理/磁石選別必須ﾁｪｯｸ処理
        Map fxhbm03Data30 = loadFxhbm03Data(queryRunnerDoc, 30);
        if(fxhbm03Data30 != null && !fxhbm03Data30.isEmpty()){
            strfxhbm03List = StringUtil.nullToBlank(getMapData(fxhbm03Data30, "data"));
            String fxhbm03Data[] = strfxhbm03List.split(",");
            for(int i = 0; i < fxhbm03Data.length; i++){
                if(!StringUtil.isEmpty(kcpno9) && kcpno9.equals(fxhbm03Data[i])) {
                    // 熱処理条件
                    FXHDD01 netusyoriJyouken = getItemRow(processData.getItemList(), GXHDO101B038Const.NETUSYORI_JYOUKEN);
                    if(StringUtil.isEmpty(StringUtil.trimAll(netusyoriJyouken.getValue()))){
                        //エラー発生時
                        errFxhdd01List = Arrays.asList(netusyoriJyouken);
                        return MessageUtil.getErrorMessageInfo("XHD-000003", true, true, errFxhdd01List, netusyoriJyouken.getLabel1());
                    }
                    // 熱処理日
                    FXHDD01 netusyoriDay = getItemRow(processData.getItemList(), GXHDO101B038Const.NETUSYORI_KAISI_DAY);
                    if(StringUtil.isEmpty(StringUtil.trimAll(netusyoriDay.getValue()))){
                        //エラー発生時
                        errFxhdd01List = Arrays.asList(netusyoriDay);
                        return MessageUtil.getErrorMessageInfo("XHD-000003", true, true, errFxhdd01List, netusyoriDay.getLabel1());
                    }
                    
                    // 熱処理時刻
                    FXHDD01 netusyoriTime = getItemRow(processData.getItemList(), GXHDO101B038Const.NETUSYORI_KAISI_TIME);
                    if(StringUtil.isEmpty(StringUtil.trimAll(netusyoriTime.getValue()))){
                        //エラー発生時
                        errFxhdd01List = Arrays.asList(netusyoriTime);
                        return MessageUtil.getErrorMessageInfo("XHD-000003", true, true, errFxhdd01List, netusyoriTime.getLabel1());
                    }
                    // 熱処理担当者
                    FXHDD01 netusyoriTantosya = getItemRow(processData.getItemList(), GXHDO101B038Const.NETUSYORI_TANTOSYA_CODE);
                    if(StringUtil.isEmpty(StringUtil.trimAll(netusyoriTantosya.getValue()))){
                        //エラー発生時
                        errFxhdd01List = Arrays.asList(netusyoriTantosya);
                        return MessageUtil.getErrorMessageInfo("XHD-000003", true, true, errFxhdd01List, netusyoriTantosya.getLabel1());
                    }
                    
                    // 磁石選別日
                    FXHDD01 jisyakusenbetuDay = getItemRow(processData.getItemList(), GXHDO101B038Const.JISYAKUSENBETU_KAISI_DAY);
                    if(StringUtil.isEmpty(StringUtil.trimAll(jisyakusenbetuDay.getValue()))){
                        //エラー発生時
                        errFxhdd01List = Arrays.asList(jisyakusenbetuDay);
                        return MessageUtil.getErrorMessageInfo("XHD-000003", true, true, errFxhdd01List, jisyakusenbetuDay.getLabel1());
                    }
                    
                    // 磁石選別時刻
                    FXHDD01 jisyakusenbetuTime = getItemRow(processData.getItemList(), GXHDO101B038Const.JISYAKUSENBETU_KAISI_TIME);
                    if(StringUtil.isEmpty(StringUtil.trimAll(jisyakusenbetuTime.getValue()))){
                        //エラー発生時
                        errFxhdd01List = Arrays.asList(jisyakusenbetuTime);
                        return MessageUtil.getErrorMessageInfo("XHD-000003", true, true, errFxhdd01List, jisyakusenbetuTime.getLabel1());
                    }
                    // 磁石選別担当者
                    FXHDD01 jisyakusenbetuTantosya = getItemRow(processData.getItemList(), GXHDO101B038Const.JISYAKUSENBETU_TANTOSYA_CODE);
                    if(StringUtil.isEmpty(StringUtil.trimAll(jisyakusenbetuTantosya.getValue()))){
                        //エラー発生時
                        errFxhdd01List = Arrays.asList(jisyakusenbetuTantosya);
                        return MessageUtil.getErrorMessageInfo("XHD-000003", true, true, errFxhdd01List, jisyakusenbetuTantosya.getLabel1());
                    } 
                }
            }
        }
        
        ValidateUtil validateUtil = new ValidateUtil();
        // 開始日時、終了日時前後チェック
        FXHDD01 itemKaishiDay = getItemRow(processData.getItemList(), GXHDO101B038Const.KAISHI_DAY); //開始日
        FXHDD01 itemKaishiTime = getItemRow(processData.getItemList(), GXHDO101B038Const.KAISHI_TIME); // 開始時刻
        Date kaishiDate = DateUtil.convertStringToDate(itemKaishiDay.getValue(), itemKaishiTime.getValue());
        FXHDD01 itemShuryouDay = getItemRow(processData.getItemList(), GXHDO101B038Const.SHURYOU_DAY); //終了日
        FXHDD01 itemShuryouTime = getItemRow(processData.getItemList(), GXHDO101B038Const.SHURYOU_TIME); //終了時刻
        Date shuryoDate = DateUtil.convertStringToDate(itemShuryouDay.getValue(), itemShuryouTime.getValue());
        //R001チェック呼出し
        String msgCheckR001 = validateUtil.checkR001(itemKaishiDay.getLabel1(), kaishiDate, itemShuryouDay.getLabel1(), shuryoDate);
        if (!StringUtil.isEmpty(msgCheckR001)) {
            //エラー発生時
            errFxhdd01List = Arrays.asList(itemKaishiDay, itemKaishiTime, itemShuryouDay, itemShuryouTime);
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
            SrMekki tmpSrMekki = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {
                
                // 更新前の値を取得
                List<SrMekki> srMekkiList = getSrMekkiData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo8, edaban, paramJissekino);
                if (!srMekkiList.isEmpty()) {
                    tmpSrMekki = srMekkiList.get(0);
                }
                
                deleteTmpSrMekki(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban, paramJissekino, processData);
            }

            // ﾒｯｷ_登録処理
            insertSrMekki(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList(), tmpSrMekki, processData);

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
    public ProcessData checkDataCorrect(ProcessData processData ) {
        
        QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
        // 項目のチェック処理を行う。
        ErrorMessageInfo checkItemErrorInfo = checkItemResistCorrect(processData, queryRunnerDoc);
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
        processData.setMethod("doKakunin");
        

        return processData;
    }
    
    /**
     * 修正処理時の確認
     * 
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doKakunin(ProcessData processData) {
        
        // 警告メッセージの設定
        processData.setWarnMessage("修正します。よろしいですか？");

        // ユーザ認証用のパラメータをセットする。
        processData.setRquireAuth(true);
        processData.setUserAuthParam(GXHDO101B038Const.USER_AUTH_UPDATE_PARAM);

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

            // ﾒｯｷ_更新処理
            updateSrMekki(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList(), processData);

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
        processData.setUserAuthParam(GXHDO101B038Const.USER_AUTH_DELETE_PARAM);

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

            // ﾒｯｷ_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo8, edaban, paramJissekino);
            insertDeleteDataTmpSrMekki(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData);

            // ﾒｯｷ削除処理
            deleteSrMekki(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban, paramJissekino, processData);

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
                        GXHDO101B038Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO101B038Const.BTN_DELETE_BOTTOM,
                        GXHDO101B038Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B038Const.BTN_START_DATETIME_BOTTOM,
                        GXHDO101B038Const.BTN_END_DATETIME_BOTTOM,
                        GXHDO101B038Const.BTN_WIP_TORIKOMI_BOTTOM,
                        GXHDO101B038Const.BTN_BUDOMARI_KEISAN_BOTTOM,
                        GXHDO101B038Const.BTN_IJOURANK_SHUTOKU_BOTTOM,
                        GXHDO101B038Const.BTN_RIREKI_TORIKOMI_BOTTOM,
                        GXHDO101B038Const.BTN_MAKUATSU_TORIKOMI_BOTTOM,
                        GXHDO101B038Const.BTN_WIP_TORIKOMI_TOP,
                        GXHDO101B038Const.BTN_BUDOMARI_KEISAN_TOP,
                        GXHDO101B038Const.BTN_IJOURANK_SHUTOKU_TOP,
                        GXHDO101B038Const.BTN_RIREKI_TORIKOMI_TOP,
                        GXHDO101B038Const.BTN_MAKUATSU_TORIKOMI_TOP,
                        GXHDO101B038Const.BTN_EDABAN_COPY_TOP,
                        GXHDO101B038Const.BTN_DELETE_TOP,
                        GXHDO101B038Const.BTN_UPDATE_TOP,
                        GXHDO101B038Const.BTN_START_DATETIME_TOP,
                        GXHDO101B038Const.BTN_END_DATETIME_TOP
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B038Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B038Const.BTN_INSERT_BOTTOM,
                        GXHDO101B038Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B038Const.BTN_INSERT_TOP));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO101B038Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B038Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO101B038Const.BTN_INSERT_BOTTOM,
                        GXHDO101B038Const.BTN_START_DATETIME_BOTTOM,
                        GXHDO101B038Const.BTN_END_DATETIME_BOTTOM,
                        GXHDO101B038Const.BTN_WIP_TORIKOMI_BOTTOM,
                        GXHDO101B038Const.BTN_BUDOMARI_KEISAN_BOTTOM,
                        GXHDO101B038Const.BTN_IJOURANK_SHUTOKU_BOTTOM,
                        GXHDO101B038Const.BTN_RIREKI_TORIKOMI_BOTTOM,
                        GXHDO101B038Const.BTN_MAKUATSU_TORIKOMI_BOTTOM,
                        GXHDO101B038Const.BTN_WIP_TORIKOMI_TOP,
                        GXHDO101B038Const.BTN_BUDOMARI_KEISAN_TOP,
                        GXHDO101B038Const.BTN_IJOURANK_SHUTOKU_TOP,
                        GXHDO101B038Const.BTN_RIREKI_TORIKOMI_TOP,
                        GXHDO101B038Const.BTN_MAKUATSU_TORIKOMI_TOP,
                        GXHDO101B038Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B038Const.BTN_EDABAN_COPY_TOP,
                        GXHDO101B038Const.BTN_INSERT_TOP,
                        GXHDO101B038Const.BTN_START_DATETIME_TOP,
                        GXHDO101B038Const.BTN_END_DATETIME_TOP
                ));

                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B038Const.BTN_DELETE_BOTTOM,
                        GXHDO101B038Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B038Const.BTN_DELETE_TOP,
                        GXHDO101B038Const.BTN_UPDATE_TOP));

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
            case GXHDO101B038Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO101B038Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempResist";
                break;
            // 登録
            case GXHDO101B038Const.BTN_INSERT_TOP:
            case GXHDO101B038Const.BTN_INSERT_BOTTOM:
                method = "checkDataResist";
                break;
            // 枝番コピー
            case GXHDO101B038Const.BTN_EDABAN_COPY_TOP:
            case GXHDO101B038Const.BTN_EDABAN_COPY_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 修正
            case GXHDO101B038Const.BTN_UPDATE_TOP:
            case GXHDO101B038Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO101B038Const.BTN_DELETE_TOP:
            case GXHDO101B038Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // ﾒｯｷ開始日時
            case GXHDO101B038Const.BTN_START_DATETIME_TOP:
            case GXHDO101B038Const.BTN_START_DATETIME_BOTTOM:
                method = "setKaishiDateTime";
                break;
            // ﾒｯｷ終了日時
            case GXHDO101B038Const.BTN_END_DATETIME_TOP:
            case GXHDO101B038Const.BTN_END_DATETIME_BOTTOM:
                method = "setShuryouDateTime";
                break;
            // WIP取込
            case GXHDO101B038Const.BTN_WIP_TORIKOMI_BOTTOM:
            case GXHDO101B038Const.BTN_WIP_TORIKOMI_TOP:
                method = "checkWipTorikomi";
                break;
            // 歩留まり計算
            case GXHDO101B038Const.BTN_BUDOMARI_KEISAN_BOTTOM:
            case GXHDO101B038Const.BTN_BUDOMARI_KEISAN_TOP:
                method = "budomariKeisan";
                break;
            // 異常品ﾗﾝｸ取得
            case GXHDO101B038Const.BTN_IJOURANK_SHUTOKU_BOTTOM:
            case GXHDO101B038Const.BTN_IJOURANK_SHUTOKU_TOP:
                method = "checkIjourankShutoku";
                break;
            // ﾒｯｷ履歴取込
            case GXHDO101B038Const.BTN_RIREKI_TORIKOMI_BOTTOM:
            case GXHDO101B038Const.BTN_RIREKI_TORIKOMI_TOP:
                method = "checkRirekiTorikomi";
                break;
            // ﾒｯｷ膜厚取込
            case GXHDO101B038Const.BTN_MAKUATSU_TORIKOMI_BOTTOM:
            case GXHDO101B038Const.BTN_MAKUATSU_TORIKOMI_TOP:
                method = "checkMakuatsuTorikomi";
                break;
            // 検査日時
            case GXHDO101B038Const.BTN_KENSA_DATETIME_TOP:
            case GXHDO101B038Const.BTN_KENSA_DATETIME_BOTTOM:
                method = "setKensaDateTime";
                break;
            // 熱処理日時
            case GXHDO101B038Const.BTN_NETUSYORI_DATETIME_TOP:
            case GXHDO101B038Const.BTN_NETUSYORI_DATETIME_BOTTOM:
                method = "setNetusyoriDateTime";
                break;
            // 磁石選別日時
            case GXHDO101B038Const.BTN_JISYAKUSENBETU_DATETIME_TOP:
            case GXHDO101B038Const.BTN_JISYAKUSENBETU_DATETIME_BOTTOM:
                method = "setJisyakusenbetuDateTime";
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
        hiddenMap.put("mekkisyurui", ""); //ﾒｯｷ種類

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
         
        Map fxhbm03Data = loadFxhbm03Data(queryRunnerDoc, 20);
        if (fxhbm03Data != null && !fxhbm03Data.isEmpty()) {
             strfxhbm03List = StringUtil.nullToBlank(getMapData(fxhbm03Data, "data"));
             String fxhbm03DataArr[]= strfxhbm03List.split(",");
             
            // 実績情報の取得
            List<Jisseki> jissekiData = loadJissekiData(queryRunnerWip, lotNo, fxhbm03DataArr);
            if(jissekiData != null && jissekiData.size() > 0){
                int dbShorisu = jissekiData.get(0).getSyorisuu(); //処理数               
                if(dbShorisu > 0){
                    syorisuu = String.valueOf(dbShorisu);
                
                }
            }
        }
        
        hiddenMap.put("syorisuu", syorisuu); //処理数
        
        // T寸法の取得
        String tsunpouave = null;
        List<SrQasunpou1> srQasunpou1Data = loadSrQasunpou1Data(queryRunnerQcdb, lotNo);
        if (srQasunpou1Data != null && srQasunpou1Data.size() > 0) {
            BigDecimal dbTsunpouave = srQasunpou1Data.get(0).getTsunave(); //T寸法(AVE)
            if (dbTsunpouave.compareTo(BigDecimal.ZERO) > 0) {
                tsunpouave = String.valueOf(dbTsunpouave);
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
        setViewItemData(processData, lotKbnMasData, ownerMasData, shikakariData, lotNo, syorisuu, tsunpouave);
        
        // ﾒｯｷ判定(項目名変更)
        String kcpno = StringUtil.nullToBlank(getMapData(shikakariData, "kcpno"));
        updateKoumokuNM(processData, queryRunnerDoc, kcpno, hiddenMap);

        processData.setInitMessageList(errorMessageList);
        return processData;

    }
    
    /**
     * ﾒｯｷ判定(項目名変更)
     * @param processData 処理制御データ
     * @param queryRunnerDoc QueryRunnerオブジェクト(DocServer)
     * @param kcpno KCPNO
     * @param hiddenMap 画面.ﾒｯｷ種類
     **/
    private ProcessData updateKoumokuNM(ProcessData processData, QueryRunner queryRunnerDoc, String kcpno, Map hiddenMap){
        boolean flg = false;
        String kcpno4 = null;
        if (!StringUtil.isEmpty(kcpno) && kcpno.length() >= 4) {
            kcpno4 = kcpno.substring(3, 4);
        }
        // 金ﾒｯｷ判定ｺｰﾄﾞ取得
        Map fxhbm03Data24 = loadFxhbm03Data(queryRunnerDoc, 24);
        if (fxhbm03Data24 == null || fxhbm03Data24.isEmpty()) {
            // ｴﾗｰﾒｯｾｰｼﾞを表示し、以降の処理を中止する。
            processData.getInitMessageList().add(MessageUtil.getMessage("XHD-000083", "EXHD-000004"));
            return processData;
        } else {
            String strfxhbm03List = StringUtil.nullToBlank(getMapData(fxhbm03Data24, "data"));
            //ﾊﾟﾗﾒｰﾀﾃﾞｰﾀを「,」(ｶﾝﾏ)でSPLITを行い、切り分けたﾃﾞｰﾀを金ﾒｯｷ判定ｺｰﾄﾞとする。
            String fxhbm03DataArr[]= strfxhbm03List.split(",");
            // KCPNOの4桁目を取得し、「金ﾒｯｷ判定ｺｰﾄﾞ」のいずれかのｺｰﾄﾞと一致するか判定する。
            for (int i = 0; i < fxhbm03DataArr.length; i++) {
                if (!StringUtil.isEmpty(kcpno4) && kcpno4.equals(fxhbm03DataArr[i])){
                    flg = true;
                    break;
                }
            }
            if (flg) {
                // 画面.ﾒｯｷ種類に "金ﾒｯｷ" を設定する。
                hiddenMap.put("mekkisyurui", "金ﾒｯｷ");
                // 画面項目名を変更する。
                // Sn膜厚(AVE)  →  Au膜厚(AVE)
                FXHDD01 makuatsusnAve = getItemRow(processData.getItemList(), GXHDO101B038Const.MAKUATSUSN_AVE);
                makuatsusnAve.setLabel1("Au膜厚(AVE)");
                // Sn膜厚(MAX)  →  Au膜厚(MAX)
                FXHDD01 makuatsusnMax = getItemRow(processData.getItemList(), GXHDO101B038Const.MAKUATSUSN_MAX);
                makuatsusnMax.setLabel1("Au膜厚(MAX)");
                // Sn膜厚(MIN)  →  Au膜厚(MIN)
                FXHDD01 makuatsusnMin = getItemRow(processData.getItemList(), GXHDO101B038Const.MAKUATSUSN_MIN);
                makuatsusnMin.setLabel1("Au膜厚(MIN)");
                return processData;
            }
        }
        
        // 銅ﾒｯｷ判定ｺｰﾄﾞ取得
        Map fxhbm03Data25 = loadFxhbm03Data(queryRunnerDoc, 25);
        if (fxhbm03Data25 == null || fxhbm03Data25.isEmpty()) {
            // ｴﾗｰﾒｯｾｰｼﾞを表示し、以降の処理を中止する。
            processData.getInitMessageList().add(MessageUtil.getMessage("XHD-000083", "EXHD-000005"));
            return processData;
        } else {
            String strfxhbm03List = StringUtil.nullToBlank(getMapData(fxhbm03Data25, "data"));
            //ﾊﾟﾗﾒｰﾀﾃﾞｰﾀを「,」(ｶﾝﾏ)でSPLITを行い、切り分けたﾃﾞｰﾀを銅ﾒｯｷ判定ｺｰﾄﾞとする。
            String fxhbm03DataArr[]= strfxhbm03List.split(",");
            // KCPNOの4桁目を取得し、「銅ﾒｯｷ判定ｺｰﾄﾞ」のいずれかのｺｰﾄﾞと一致するか判定する。
            for (int i = 0; i < fxhbm03DataArr.length; i++) {
                if (!StringUtil.isEmpty(kcpno4) && kcpno4.equals(fxhbm03DataArr[i])){
                    flg = true;
                    break;
                }
            }
            if (flg) {
                // 画面.ﾒｯｷ種類に "銅ﾒｯｷ" を設定する。
                hiddenMap.put("mekkisyurui", "銅ﾒｯｷ");
                // 画面項目名を変更する。
                // Sn膜厚(AVE)  →  Cu膜厚(AVE)
                FXHDD01 makuatsusnAve = getItemRow(processData.getItemList(), GXHDO101B038Const.MAKUATSUSN_AVE);
                makuatsusnAve.setLabel1("Cu膜厚(AVE)");
                // Sn膜厚(MAX)  →  Cu膜厚(MAX)
                FXHDD01 makuatsusnMax = getItemRow(processData.getItemList(), GXHDO101B038Const.MAKUATSUSN_MAX);
                makuatsusnMax.setLabel1("Cu膜厚(MAX)");
                // Sn膜厚(MIN)  →  Cu膜厚(MIN)
                FXHDD01 makuatsusnMin = getItemRow(processData.getItemList(), GXHDO101B038Const.MAKUATSUSN_MIN);
                makuatsusnMin.setLabel1("Cu膜厚(MIN)");
            }
        }
        
        return processData;
    }

    /**
     * 入力項目以外のデータを画面項目に設定
     *
     * @param processData 処理制御データ
     * @param lotKbnMasData ﾛｯﾄ区分ﾏｽﾀデータ
     * @param ownerMasData ｵｰﾅｰﾏｽﾀデータ
     * @param daPatternMasData 製版ﾏｽﾀデータ
     * @param shikakariData 仕掛データ
     * @param lotNo ﾛｯﾄNo
     */
    private void setViewItemData(ProcessData processData, Map lotKbnMasData, Map ownerMasData, Map shikakariData, String lotNo ,String syorisuu, String tsunpouave) {
        
        // ロットNo
        this.setItemData(processData, GXHDO101B038Const.LOTNO, lotNo);
        // KCPNO
        this.setItemData(processData, GXHDO101B038Const.KCPNO, StringUtil.nullToBlank(getMapData(shikakariData, "kcpno")));
        // 客先
        this.setItemData(processData, GXHDO101B038Const.KYAKUSAKI, StringUtil.nullToBlank(getMapData(shikakariData, "tokuisaki")));

        // ロット区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); //ﾛｯﾄ区分ｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B038Const.LOT_KUBUN, "");
        } else {
            String lotKubun = StringUtil.nullToBlank(getMapData(lotKbnMasData, "lotkubun"));
            this.setItemData(processData, GXHDO101B038Const.LOT_KUBUN, lotkubuncode + ":" + lotKubun);
        }

        // オーナー
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));// ｵｰﾅｰｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B038Const.OWNER, "");
        } else {
            String owner = StringUtil.nullToBlank(getMapData(ownerMasData, "ownername"));
            this.setItemData(processData, GXHDO101B038Const.OWNER, ownercode + ":" + owner);
        }
        
        //処理数
        this.setItemData(processData, GXHDO101B038Const.SYORISUU, syorisuu);
        
        //T寸法
        this.setItemData(processData, GXHDO101B038Const.TSUNPOU, tsunpouave);

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

        List<SrMekki> srMekkiDataList = new ArrayList<>();
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
                if (StringUtil.isEmpty(rev)) {
                    //【WIP取込】ﾎﾞﾀﾝ押下時の処理を呼び出す。	
                    doWipTorikomi(processData, 0);

                    //【歩留まり計算】ﾎﾞﾀﾝ押下時の処理を呼び出す。
                    checkBudomariKeisan(processData, 0);

                    //【異常品ﾗﾝｸ取得】ﾎﾞﾀﾝ押下時の処理を呼び出す。
                    // TODO  該当処理の設計がなし
                    //【ﾒｯｷ履歴取込】ﾎﾞﾀﾝ押下時の処理を呼び出す。
                    doRirekiTorikomi(processData, 0);

                    //【ﾒｯｷ膜厚取込】ﾎﾞﾀﾝ押下時の処理を呼び出す。
                    // TODO  該当処理の設計がなし
                }
                return true;
            }

            // ﾒｯｷデータ取得
            srMekkiDataList = getSrMekkiData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban, jissekino);
            if (srMekkiDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srMekkiDataList.isEmpty()) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srMekkiDataList.get(0));

        return true;

    }

    /**
     * メイン画面データ設定処理
     *
     * @param processData 処理制御データ
     * @param srMekkiData ﾒｯｷデータ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrMekki srMekkiData) {

        Map hiddenMap = processData.getHiddenDataMap();
        //受入れ単位重量
        this.setItemData(processData, GXHDO101B038Const.UKEIRE_TANNIJYURYO, getSrMekkiItemData(GXHDO101B038Const.UKEIRE_TANNIJYURYO, srMekkiData));
        //受入れ総重量
        this.setItemData(processData, GXHDO101B038Const.UKEIRE_SOUJYURYOU, getSrMekkiItemData(GXHDO101B038Const.UKEIRE_SOUJYURYOU, srMekkiData));
        //ﾒｯｷ場所
        this.setItemData(processData, GXHDO101B038Const.MEKKIBASYO, getSrMekkiItemData(GXHDO101B038Const.MEKKIBASYO, srMekkiData));
        //ﾒｯｷ場所設備
        this.setItemData(processData, GXHDO101B038Const.MEKKIBASYOSETUBI, getSrMekkiItemData(GXHDO101B038Const.MEKKIBASYOSETUBI, srMekkiData));
        // 号機
        this.setItemData(processData, GXHDO101B038Const.GOUKI, getSrMekkiItemData(GXHDO101B038Const.GOUKI, srMekkiData));
        //ﾄﾞｰﾑ個数
        this.setItemData(processData, GXHDO101B038Const.DOMEKOSUU, getSrMekkiItemData(GXHDO101B038Const.DOMEKOSUU, srMekkiData));
        //使用ﾄﾞｰﾑ明細
        this.setItemData(processData, GXHDO101B038Const.DOMEMEISAI, getSrMekkiItemData(GXHDO101B038Const.DOMEMEISAI, srMekkiData));        
        hiddenMap.put("domemeisai", getSrMekkiItemData(GXHDO101B038Const.DOMEMEISAI, srMekkiData));
        //条件NI(A)
        this.setItemData(processData, GXHDO101B038Const.JYOUKENNI_A, getSrMekkiItemData(GXHDO101B038Const.JYOUKENNI_A, srMekkiData));
        //条件NI(AM)
        this.setItemData(processData, GXHDO101B038Const.JYOUKENNI_AM, getSrMekkiItemData(GXHDO101B038Const.JYOUKENNI_AM, srMekkiData));
        //条件SN(A)
        this.setItemData(processData, GXHDO101B038Const.JYOUKENSN_A, getSrMekkiItemData(GXHDO101B038Const.JYOUKENSN_A, srMekkiData));
        //条件SN(AM)
        this.setItemData(processData, GXHDO101B038Const.JYOUKENSN_AM, getSrMekkiItemData(GXHDO101B038Const.JYOUKENSN_AM, srMekkiData));
        // ﾒｯｷ開始日
        this.setItemData(processData, GXHDO101B038Const.KAISHI_DAY, getSrMekkiItemData(GXHDO101B038Const.KAISHI_DAY, srMekkiData));
        // ﾒｯｷ開始時間
        this.setItemData(processData, GXHDO101B038Const.KAISHI_TIME, getSrMekkiItemData(GXHDO101B038Const.KAISHI_TIME, srMekkiData));
        // 開始担当者
        this.setItemData(processData, GXHDO101B038Const.KAISHI_TANTOSYA_CODE, getSrMekkiItemData(GXHDO101B038Const.KAISHI_TANTOSYA_CODE, srMekkiData));
        // ﾒｯｷ終了日
        this.setItemData(processData, GXHDO101B038Const.SHURYOU_DAY, getSrMekkiItemData(GXHDO101B038Const.SHURYOU_DAY, srMekkiData));
        // ﾒｯｷ終了時間
        this.setItemData(processData, GXHDO101B038Const.SHURYOU_TIME, getSrMekkiItemData(GXHDO101B038Const.SHURYOU_TIME, srMekkiData));
        // 終了担当者
        this.setItemData(processData, GXHDO101B038Const.SHURYOU_TANTOUSYA_CODE, getSrMekkiItemData(GXHDO101B038Const.SHURYOU_TANTOUSYA_CODE, srMekkiData));        
        //Ni膜厚(AVE)
        this.setItemData(processData, GXHDO101B038Const.MAKUATSUNI_AVE, getSrMekkiItemData(GXHDO101B038Const.MAKUATSUNI_AVE, srMekkiData));
        //Ni膜厚(MAX)
        this.setItemData(processData, GXHDO101B038Const.MAKUATSUNI_MAX, getSrMekkiItemData(GXHDO101B038Const.MAKUATSUNI_MAX, srMekkiData));
        //Ni膜厚(MIN)
        this.setItemData(processData, GXHDO101B038Const.MAKUATSUNI_MIN, getSrMekkiItemData(GXHDO101B038Const.MAKUATSUNI_MIN, srMekkiData));
        //Sn膜厚(AVE)
        this.setItemData(processData, GXHDO101B038Const.MAKUATSUSN_AVE, getSrMekkiItemData(GXHDO101B038Const.MAKUATSUSN_AVE, srMekkiData));
        //Sn膜厚(MAX)
        this.setItemData(processData, GXHDO101B038Const.MAKUATSUSN_MAX, getSrMekkiItemData(GXHDO101B038Const.MAKUATSUSN_MAX, srMekkiData));
        //Sn膜厚(MIN)
        this.setItemData(processData, GXHDO101B038Const.MAKUATSUSN_MIN, getSrMekkiItemData(GXHDO101B038Const.MAKUATSUSN_MIN, srMekkiData));
        //半田ﾇﾚ性
        this.setItemData(processData, GXHDO101B038Const.NUREKENSAKEKKA, getSrMekkiItemData(GXHDO101B038Const.NUREKENSAKEKKA, srMekkiData));
        //半田耐熱性
        this.setItemData(processData, GXHDO101B038Const.TAINETSUKENSAKEKKA, getSrMekkiItemData(GXHDO101B038Const.TAINETSUKENSAKEKKA, srMekkiData));
        //検査日
        this.setItemData(processData, GXHDO101B038Const.KENSA_DAY, getSrMekkiItemData(GXHDO101B038Const.KENSA_DAY, srMekkiData));
        //検査時刻
        this.setItemData(processData, GXHDO101B038Const.KENSA_TIME, getSrMekkiItemData(GXHDO101B038Const.KENSA_TIME, srMekkiData));
        //外観
        this.setItemData(processData, GXHDO101B038Const.GAIKAN, getSrMekkiItemData(GXHDO101B038Const.GAIKAN, srMekkiData));
        //検査・外観担当者
        this.setItemData(processData, GXHDO101B038Const.KENSA_TANTOUSYA, getSrMekkiItemData(GXHDO101B038Const.KENSA_TANTOUSYA, srMekkiData));
        //検査単位重量
        this.setItemData(processData, GXHDO101B038Const.KENSA_TANNIJYURYO, getSrMekkiItemData(GXHDO101B038Const.KENSA_TANNIJYURYO, srMekkiData));
        //検査総重量
        this.setItemData(processData, GXHDO101B038Const.KENSA_SOUJYURYOU, getSrMekkiItemData(GXHDO101B038Const.KENSA_SOUJYURYOU, srMekkiData));
        //良品数
        this.setItemData(processData, GXHDO101B038Const.SHUKKAKOSUU, getSrMekkiItemData(GXHDO101B038Const.SHUKKAKOSUU, srMekkiData));
        //歩留まり
        this.setItemData(processData, GXHDO101B038Const.BUDOMARI, getSrMekkiItemData(GXHDO101B038Const.BUDOMARI, srMekkiData));
        //熱処理条件
        this.setItemData(processData, GXHDO101B038Const.NETUSYORI_JYOUKEN, getSrMekkiItemData(GXHDO101B038Const.NETUSYORI_JYOUKEN, srMekkiData));
        //熱処理日
        this.setItemData(processData, GXHDO101B038Const.NETUSYORI_KAISI_DAY, getSrMekkiItemData(GXHDO101B038Const.NETUSYORI_KAISI_DAY, srMekkiData));
        //熱処理時刻
        this.setItemData(processData, GXHDO101B038Const.NETUSYORI_KAISI_TIME, getSrMekkiItemData(GXHDO101B038Const.NETUSYORI_KAISI_TIME, srMekkiData));
        //熱処理担当者
        this.setItemData(processData, GXHDO101B038Const.NETUSYORI_TANTOSYA_CODE, getSrMekkiItemData(GXHDO101B038Const.NETUSYORI_TANTOSYA_CODE, srMekkiData));
        //磁石選別日
        this.setItemData(processData, GXHDO101B038Const.JISYAKUSENBETU_KAISI_DAY, getSrMekkiItemData(GXHDO101B038Const.JISYAKUSENBETU_KAISI_DAY, srMekkiData));
        //磁石選別時刻
        this.setItemData(processData, GXHDO101B038Const.JISYAKUSENBETU_KAISI_TIME, getSrMekkiItemData(GXHDO101B038Const.JISYAKUSENBETU_KAISI_TIME, srMekkiData));
        //磁石選別担当者
        this.setItemData(processData, GXHDO101B038Const.JISYAKUSENBETU_TANTOSYA_CODE, getSrMekkiItemData(GXHDO101B038Const.JISYAKUSENBETU_TANTOSYA_CODE, srMekkiData));
        //異常発行
        this.setItemData(processData, GXHDO101B038Const.IJOUHAKKOU, getSrMekkiItemData(GXHDO101B038Const.IJOUHAKKOU, srMekkiData));
        //異常品ﾗﾝｸ
        this.setItemData(processData, GXHDO101B038Const.IJOURANK, getSrMekkiItemData(GXHDO101B038Const.IJOURANK, srMekkiData));
        //膜厚確認
        this.setItemData(processData, GXHDO101B038Const.MAKUATSU_KAKUNIN, getSrMekkiItemData(GXHDO101B038Const.MAKUATSU_KAKUNIN, srMekkiData));
        //ﾃｽﾄ品
        this.setItemData(processData, GXHDO101B038Const.TESTHIN, getSrMekkiItemData(GXHDO101B038Const.TESTHIN, srMekkiData));
        // 備考1
        this.setItemData(processData, GXHDO101B038Const.BIKO1, getSrMekkiItemData(GXHDO101B038Const.BIKO1, srMekkiData));
        // 備考2
        this.setItemData(processData, GXHDO101B038Const.BIKO2, getSrMekkiItemData(GXHDO101B038Const.BIKO2, srMekkiData));
 
    }

    /**
     * ﾒｯｷの入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @param jissekino 実績No
     * @return ﾒｯｷ処理登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrMekki> getSrMekkiData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrMekki(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
        } else {
            return loadTmpSrMekki(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
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

        // ﾛｯﾄ区分ﾏｽﾀｰデータの取得
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
     * @param queryRunnerWip オブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param date ﾊﾟﾗﾒｰﾀﾃﾞｰﾀ(検索キー)
     * @return 取得データ
     * @throws SQLException 
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
        return queryRunnerWip.query(sql, beanHandler, params.toArray());
    }
     
     /**
     * [QA寸法]から、ﾃﾞｰﾀを取得
     * @param queryRunnerQcdb オブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @return 取得データ
     * @throws SQLException 
     */
     private List<SrQasunpou1> loadSrQasunpou1Data(QueryRunner queryRunnerQcdb, String lotNo) throws SQLException {
        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 11);
        String lotNo3 = lotNo.substring(11, 14);
       
        
        // QA寸法情報データの取得
        String sql = "SELECT tsunave "
                + "FROM sr_qasunpou1 "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? "
                + "ORDER BY jissekino desc ";
        
        Map mapping = new HashMap<>();
        mapping.put("tsunave", "tsunave");
        
        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrQasunpou1>> beanHandler = new BeanListHandler<>(SrQasunpou1.class, rowProcessor);

        List<Object> params = new ArrayList<>();
        params.add(lotNo1);
        params.add(lotNo2);
        params.add(lotNo3);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }
     
     
    /**
     * [ﾊﾟﾗﾒｰﾀﾏｽﾀ]から、ﾃﾞｰﾀを取得
     * @param queryRunnerDoc オブジェクト
     * @param selectNo 検索条件KEYの区分
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhbm03Data(QueryRunner queryRunnerDoc, int selectNo) {
        try {

            // ﾊﾟﾗﾒｰﾀﾏｽﾀデータの取得
            String sql = "SELECT data "
                        + " FROM fxhbm03 "
                        + " WHERE user_name = 'common_user'  ";
            
            switch (selectNo){
                case 20:
                   sql= sql + "AND key = '工程コード_ﾒｯｷ社内' ";
                   break;
                case 21:
                   sql= sql + "AND key = 'xhd_外部電極ﾒｯｷ_入力必須ﾁｪｯｸ_実績1' ";
                   break;
                case 22:
                   sql= sql + "AND key = 'xhd_外部電極ﾒｯｷ_入力必須ﾁｪｯｸ_実績2' ";
                   break; 
                case 24:
                   sql= sql + "AND key = 'xhd_外部電極ﾒｯｷ_金ﾒｯｷ判定ｺｰﾄﾞ' ";
                   break;
                case 25:
                   sql= sql + "AND key = 'xhd_外部電極ﾒｯｷ_銅ﾒｯｷ判定ｺｰﾄﾞ' ";
                   break;
                case 26:
                   sql= sql + "AND key = '工程コード_ﾒｯｷQC判定' ";
                   break;
                case 30:
                   sql= sql + "AND key = 'xhd_外部電極ﾒｯｷ_熱処理_磁石選別_必須判定ｺｰﾄﾞ' ";
                   break;
                default:
                break;
            }
            DBUtil.outputSQLLog(sql, new Object[0], LOGGER);
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
        String sql = "SELECT kcpno, oyalotedaban, tokuisaki, lotkubuncode, ownercode "
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
     * [ﾒｯｷ]から、ﾃﾞｰﾀを取得
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
    private List<SrMekki> loadSrMekki(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, String rev) throws SQLException {

        String sql = "SELECT kojyo, lotno, edaban, kcpno, ukeiresuu, domekosuu, gouki, tantousya, "
                + "mekkikaishinichiji, mekkijyoukennia, mekkijyoukenniam, mekkijyoukensna, "
                + "mekkijyoukensnam, shukkakosuu, budomari, makuatsunimin, makuatsunimax, "
                + "makuatsuniave, makuatsunistd, makuatsusnmin, makuatsusnmax, makuatsusnave, "
                + "makuatsusnstd, nurekensakekka, tainetsukensakekka, gaikankensakekka, bikou1, "
                + "bikou2, bikou3, jissekino, tourokunichiji, koushinnichiji, domemeisai, "
                + "tyoseimaeph1, tyoseigoph1, tyoseijikan1, tyoseimaeph2, tyoseigoph2, "
                + "tyoseijikan2, tsunpou, barrelno, makuatsunicpl, makuatsusncpl, "
                + "sokuteinichiji, makuatsunicv, makuatsusncv, kensanichiji, kensatantousya, "
                + "makuatsutantosya, kaishinichiji_sn, tokuisaki, lotkubuncode, ownercode, "
                + "ukeiretannijyuryo, ukeiresoujyuryou, mekkibasyo, mekkibasyosetubi, "
                + "mekkisyuryounichiji, syuryousya, kensatannijyuryo, kensasoujyuryou, "
                + "netusyorijyouken, netusyorikaisinichiji, netusyoritantousya, "
                + "jisyakusenbetukaisinichiji, jisyakusenbetutantousya, ijouhakkou, "
                + "ijourank, makuatsukakunin, testhin, tsunpouave, mekkisyurui, revision, '0' AS deleteflag "
                + "FROM sr_mekki "
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
        mapping.put("kojyo", "kojyo"); // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno"); // ﾛｯﾄNo
        mapping.put("edaban", "edaban"); // 枝番
        mapping.put("kcpno", "kcpno"); // KCPNO
        mapping.put("ukeiresuu", "ukeiresuu"); // 処理数
        mapping.put("domekosuu", "domekosuu"); // ﾄﾞｰﾑ個数
        mapping.put("gouki", "gouki"); // 号機
        mapping.put("tantousya", "tantosyacode"); // 開始担当者
        mapping.put("mekkikaishinichiji", "mekkikaishinichiji"); // ﾒｯｷ開始日時
        mapping.put("mekkijyoukennia", "mekkijyoukennia"); // 条件NI(A)
        mapping.put("mekkijyoukenniam", "mekkijyoukenniam"); // 条件NI(AM)
        mapping.put("mekkijyoukensna", "mekkijyoukensna"); // 条件SN(A)
        mapping.put("mekkijyoukensnam", "mekkijyoukensnam"); // 条件SN(AM)
        mapping.put("shukkakosuu", "shukkakosuu"); // 良品数
        mapping.put("budomari", "budomari"); // 歩留まり
        mapping.put("makuatsunimin", "makuatsunimin"); // Ni膜厚(MIN)
        mapping.put("makuatsunimax", "makuatsunimax"); // Ni膜厚(MAX)
        mapping.put("makuatsuniave", "makuatsuniave"); // Ni膜厚(AVE)
        mapping.put("makuatsunistd", "makuatsunistd"); // Ni膜厚(STD)
        mapping.put("makuatsusnmin", "makuatsusnmin"); // Sn膜厚(MIN)
        mapping.put("makuatsusnmax", "makuatsusnmax"); // Sn膜厚(MAX)
        mapping.put("makuatsusnave", "makuatsusnave"); // Sn膜厚(AVE)
        mapping.put("makuatsusnstd", "makuatsusnstd"); // Sn膜厚(STD)
        mapping.put("nurekensakekka", "nurekensakekka"); // 半田ﾇﾚ性
        mapping.put("tainetsukensakekka", "tainetsukensakekka"); // 半田耐熱性
        mapping.put("gaikankensakekka", "gaikankensakekka"); // 外観
        mapping.put("bikou1", "biko1"); // 備考1
        mapping.put("bikou2", "biko2"); // 備考2
        mapping.put("bikou3", "biko3"); // 備考3
        mapping.put("jissekino", "jissekino"); // 回数
        mapping.put("tourokunichiji", "torokunichiji"); // 登録日時
        mapping.put("koushinnichiji", "kosinnichiji"); // 更新日時
        mapping.put("domemeisai", "domemeisai"); // 使用ﾄﾞｰﾑ明細
        mapping.put("tyoseimaeph1", "tyoseimaeph1"); // 1回目調整前PH値
        mapping.put("tyoseigoph1", "tyoseigoph1"); // 1回目調整後PH値
        mapping.put("tyoseijikan1", "tyoseijikan1"); // 1回目調整時間
        mapping.put("tyoseimaeph2", "tyoseimaeph2"); // 2回目調整前PH値
        mapping.put("tyoseigoph2", "tyoseigoph2"); // 2回目調整後PH値
        mapping.put("tyoseijikan2", "tyoseijikan2"); // 2回目調整時間
        mapping.put("tsunpou", "tsunpou"); // Ｔ寸法
        mapping.put("barrelno", "barrelno"); // ﾊﾞﾚﾙNo
        mapping.put("makuatsunicpl", "makuatsunicpl"); // Ni膜厚(CPL)
        mapping.put("makuatsusncpl", "makuatsusncpl"); // Sn膜厚(CPL)
        mapping.put("sokuteinichiji", "sokuteinichiji"); // 測定日時
        mapping.put("makuatsunicv", "makuatsunicv"); // Ni膜厚(CV)
        mapping.put("makuatsusncv", "makuatsusncv"); // Sn膜厚(CV)
        mapping.put("kensanichiji", "kensanichiji"); // 検査日時
        mapping.put("kensatantousya", "kensatantousya"); // 検査・外観担当者
        mapping.put("makuatsutantosya", "makuatsutantosya"); // 膜厚担当者
        mapping.put("kaishinichiji_sn", "kaishinichiji_sn"); // Sn開始日時
        mapping.put("tokuisaki", "tokuisaki"); // 客先
        mapping.put("lotkubuncode", "lotkubuncode"); // ﾛｯﾄ区分
        mapping.put("ownercode", "ownercode"); // ｵｰﾅｰ
        mapping.put("ukeiretannijyuryo", "ukeiretannijyuryo"); // 受入れ単位重量
        mapping.put("ukeiresoujyuryou", "ukeiresoujyuryou"); // 受入れ総重量
        mapping.put("mekkibasyo", "mekkibasyo"); // ﾒｯｷ場所
        mapping.put("mekkibasyosetubi", "mekkibasyosetubi"); // ﾒｯｷ場所設備
        mapping.put("mekkisyuryounichiji", "mekkisyuryounichiji"); // ﾒｯｷ終了日時
        mapping.put("syuryousya", "syuryousya"); // 終了担当者
        mapping.put("kensatannijyuryo", "kensatannijyuryo"); // 検査単位重量
        mapping.put("kensasoujyuryou", "kensasoujyuryou"); // 検査総重量
        mapping.put("netusyorijyouken", "netusyorijyouken"); // 熱処理条件
        mapping.put("netusyorikaisinichiji", "netusyorikaisinichiji"); // 熱処理開始日時
        mapping.put("netusyoritantousya", "netusyoritantousya"); // 熱処理担当者
        mapping.put("jisyakusenbetukaisinichiji", "jisyakusenbetukaisinichiji"); // 磁石選別開始日時
        mapping.put("jisyakusenbetutantousya", "jisyakusenbetutantousya"); // 磁石選別担当者
        mapping.put("ijouhakkou", "ijouhakkou"); // 異常発行
        mapping.put("ijourank", "ijourank"); // 異常品ﾗﾝｸ
        mapping.put("makuatsukakunin", "makuatsukakunin"); // 膜厚確認
        mapping.put("testhin", "testhin"); // ﾃｽﾄ品
        mapping.put("tsunpouave", "tsunpouave"); // T寸法AVE
        mapping.put("mekkisyurui", "mekkisyurui"); // ﾒｯｷ種類
        mapping.put("revision", "revision"); // revision
        mapping.put("deleteflag", "deleteflag"); // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrMekki>> beanHandler = new BeanListHandler<>(SrMekki.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [ﾒｯｷ_仮登録]から、ﾃﾞｰﾀを取得
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
    private List<SrMekki> loadTmpSrMekki(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, String rev) throws SQLException {
        
        String sql = "SELECT kojyo, lotno, edaban, kcpno, ukeiresuu, domekosuu, gouki, tantousya, "
                + "mekkikaishinichiji, mekkijyoukennia, mekkijyoukenniam, mekkijyoukensna, "
                + "mekkijyoukensnam, shukkakosuu, budomari, makuatsunimin, makuatsunimax, "
                + "makuatsuniave, makuatsunistd, makuatsusnmin, makuatsusnmax, makuatsusnave, "
                + "makuatsusnstd, nurekensakekka, tainetsukensakekka, gaikankensakekka, bikou1, "
                + "bikou2, bikou3, jissekino, tourokunichiji, koushinnichiji, domemeisai, "
                + "tyoseimaeph1, tyoseigoph1, tyoseijikan1, tyoseimaeph2, tyoseigoph2, "
                + "tyoseijikan2, tsunpou, barrelno, makuatsunicpl, makuatsusncpl, "
                + "sokuteinichiji, makuatsunicv, makuatsusncv, kensanichiji, kensatantousya, "
                + "makuatsutantosya, kaishinichiji_sn, tokuisaki, lotkubuncode, ownercode, "
                + "ukeiretannijyuryo, ukeiresoujyuryou, mekkibasyo, mekkibasyosetubi, "
                + "mekkisyuryounichiji, syuryousya, kensatannijyuryo, kensasoujyuryou, "
                + "netusyorijyouken, netusyorikaisinichiji, netusyoritantousya, "
                + "jisyakusenbetukaisinichiji, jisyakusenbetutantousya, ijouhakkou, "
                + "ijourank, makuatsukakunin, testhin, tsunpouave, mekkisyurui, revision, deleteflag "
                + "FROM tmp_sr_mekki "
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
        mapping.put("kojyo", "kojyo"); // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno"); // ﾛｯﾄNo
        mapping.put("edaban", "edaban"); // 枝番
        mapping.put("kcpno", "kcpno"); // KCPNO
        mapping.put("ukeiresuu", "ukeiresuu"); // 処理数
        mapping.put("domekosuu", "domekosuu"); // ﾄﾞｰﾑ個数
        mapping.put("gouki", "gouki"); // 号機
        mapping.put("tantousya", "tantosyacode"); // 開始担当者
        mapping.put("mekkikaishinichiji", "mekkikaishinichiji"); // ﾒｯｷ開始日時
        mapping.put("mekkijyoukennia", "mekkijyoukennia"); // 条件NI(A)
        mapping.put("mekkijyoukenniam", "mekkijyoukenniam"); // 条件NI(AM)
        mapping.put("mekkijyoukensna", "mekkijyoukensna"); // 条件SN(A)
        mapping.put("mekkijyoukensnam", "mekkijyoukensnam"); // 条件SN(AM)
        mapping.put("shukkakosuu", "shukkakosuu"); // 良品数
        mapping.put("budomari", "budomari"); // 歩留まり
        mapping.put("makuatsunimin", "makuatsunimin"); // Ni膜厚(MIN)
        mapping.put("makuatsunimax", "makuatsunimax"); // Ni膜厚(MAX)
        mapping.put("makuatsuniave", "makuatsuniave"); // Ni膜厚(AVE)
        mapping.put("makuatsunistd", "makuatsunistd"); // Ni膜厚(STD)
        mapping.put("makuatsusnmin", "makuatsusnmin"); // Sn膜厚(MIN)
        mapping.put("makuatsusnmax", "makuatsusnmax"); // Sn膜厚(MAX)
        mapping.put("makuatsusnave", "makuatsusnave"); // Sn膜厚(AVE)
        mapping.put("makuatsusnstd", "makuatsusnstd"); // Sn膜厚(STD)
        mapping.put("nurekensakekka", "nurekensakekka"); // 半田ﾇﾚ性
        mapping.put("tainetsukensakekka", "tainetsukensakekka"); // 半田耐熱性
        mapping.put("gaikankensakekka", "gaikankensakekka"); // 外観
        mapping.put("bikou1", "biko1"); // 備考1
        mapping.put("bikou2", "biko2"); // 備考2
        mapping.put("bikou3", "biko3"); // 備考3
        mapping.put("jissekino", "jissekino"); // 回数
        mapping.put("tourokunichiji", "torokunichiji"); // 登録日時
        mapping.put("koushinnichiji", "kosinnichiji"); // 更新日時
        mapping.put("domemeisai", "domemeisai"); // 使用ﾄﾞｰﾑ明細
        mapping.put("tyoseimaeph1", "tyoseimaeph1"); // 1回目調整前PH値
        mapping.put("tyoseigoph1", "tyoseigoph1"); // 1回目調整後PH値
        mapping.put("tyoseijikan1", "tyoseijikan1"); // 1回目調整時間
        mapping.put("tyoseimaeph2", "tyoseimaeph2"); // 2回目調整前PH値
        mapping.put("tyoseigoph2", "tyoseigoph2"); // 2回目調整後PH値
        mapping.put("tyoseijikan2", "tyoseijikan2"); // 2回目調整時間
        mapping.put("tsunpou", "tsunpou"); // Ｔ寸法
        mapping.put("barrelno", "barrelno"); // ﾊﾞﾚﾙNo
        mapping.put("makuatsunicpl", "makuatsunicpl"); // Ni膜厚(CPL)
        mapping.put("makuatsusncpl", "makuatsusncpl"); // Sn膜厚(CPL)
        mapping.put("sokuteinichiji", "sokuteinichiji"); // 測定日時
        mapping.put("makuatsunicv", "makuatsunicv"); // Ni膜厚(CV)
        mapping.put("makuatsusncv", "makuatsusncv"); // Sn膜厚(CV)
        mapping.put("kensanichiji", "kensanichiji"); // 検査日時
        mapping.put("kensatantousya", "kensatantousya"); // 検査・外観担当者
        mapping.put("makuatsutantosya", "makuatsutantosya"); // 膜厚担当者
        mapping.put("kaishinichiji_sn", "kaishinichiji_sn"); // Sn開始日時
        mapping.put("tokuisaki", "tokuisaki"); // 客先
        mapping.put("lotkubuncode", "lotkubuncode"); // ﾛｯﾄ区分
        mapping.put("ownercode", "ownercode"); // ｵｰﾅｰ
        mapping.put("ukeiretannijyuryo", "ukeiretannijyuryo"); // 受入れ単位重量
        mapping.put("ukeiresoujyuryou", "ukeiresoujyuryou"); // 受入れ総重量
        mapping.put("mekkibasyo", "mekkibasyo"); // ﾒｯｷ場所
        mapping.put("mekkibasyosetubi", "mekkibasyosetubi"); // ﾒｯｷ場所設備
        mapping.put("mekkisyuryounichiji", "mekkisyuryounichiji"); // ﾒｯｷ終了日時
        mapping.put("syuryousya", "syuryousya"); // 終了担当者
        mapping.put("kensatannijyuryo", "kensatannijyuryo"); // 検査単位重量
        mapping.put("kensasoujyuryou", "kensasoujyuryou"); // 検査総重量
        mapping.put("netusyorijyouken", "netusyorijyouken"); // 熱処理条件
        mapping.put("netusyorikaisinichiji", "netusyorikaisinichiji"); // 熱処理開始日時
        mapping.put("netusyoritantousya", "netusyoritantousya"); // 熱処理担当者
        mapping.put("jisyakusenbetukaisinichiji", "jisyakusenbetukaisinichiji"); // 磁石選別開始日時
        mapping.put("jisyakusenbetutantousya", "jisyakusenbetutantousya"); // 磁石選別担当者
        mapping.put("ijouhakkou", "ijouhakkou"); // 異常発行
        mapping.put("ijourank", "ijourank"); // 異常品ﾗﾝｸ
        mapping.put("makuatsukakunin", "makuatsukakunin"); // 膜厚確認
        mapping.put("testhin", "testhin"); // ﾃｽﾄ品
        mapping.put("tsunpouave", "tsunpouave"); // T寸法AVE
        mapping.put("mekkisyurui", "mekkisyurui"); // ﾒｯｷ種類
        mapping.put("revision", "revision"); // revision
        mapping.put("deleteflag", "deleteflag"); // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrMekki>> beanHandler = new BeanListHandler<>(SrMekki.class, rowProcessor);

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
     * @param processData 処理制御データ
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

            // ﾒｯｷデータ取得
            List<SrMekki> srMekkiDataList = getSrMekkiData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban, paramJissekino);
            if (srMekkiDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // メイン画面データ設定
            setInputItemDataMainForm(processData, srMekkiDataList.get(0));

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
     * @param srMekkiData ﾒｯｷデータ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrMekki srMekkiData) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srMekkiData != null) {
            // 元データが存在する場合元データより取得
            return getSrMekkiItemData(itemId, srMekkiData);
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
     * ﾒｯｷ_仮登録(tmp_sr_mekki)登録処理
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
    private void insertTmpSrMekki(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList,ProcessData processData) throws SQLException {

        String sql = "INSERT INTO tmp_sr_mekki ("
                + "kojyo, lotno, edaban, kcpno, ukeiresuu, domekosuu, gouki, tantousya, "
                + "mekkikaishinichiji, mekkijyoukennia, mekkijyoukenniam, mekkijyoukensna, "
                + "mekkijyoukensnam, shukkakosuu, budomari, makuatsunimin, makuatsunimax, "
                + "makuatsuniave, makuatsunistd, makuatsusnmin, makuatsusnmax, makuatsusnave, "
                + "makuatsusnstd, nurekensakekka, tainetsukensakekka, gaikankensakekka, bikou1, "
                + "bikou2, bikou3, jissekino, tourokunichiji, koushinnichiji, domemeisai, "
                + "tyoseimaeph1, tyoseigoph1, tyoseijikan1, tyoseimaeph2, tyoseigoph2, "
                + "tyoseijikan2, tsunpou, barrelno, makuatsunicpl, makuatsusncpl, "
                + "sokuteinichiji, makuatsunicv, makuatsusncv, kensanichiji, kensatantousya, "
                + "makuatsutantosya, kaishinichiji_sn, tokuisaki, lotkubuncode, ownercode, "
                + "ukeiretannijyuryo, ukeiresoujyuryou, mekkibasyo, mekkibasyosetubi, "
                + "mekkisyuryounichiji, syuryousya, kensatannijyuryo, kensasoujyuryou, "
                + "netusyorijyouken, netusyorikaisinichiji, netusyoritantousya, "
                + "jisyakusenbetukaisinichiji, jisyakusenbetutantousya, ijouhakkou, "
                + "ijourank, makuatsukakunin, testhin, tsunpouave, mekkisyurui, revision, "
                + "deleteflag "
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrMekki(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, itemList, null, jissekino, processData);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ﾒｯｷ_仮登録(tmp_sr_mekki)更新処理
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
    private void updateTmpSrMekki(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, ProcessData processData) throws SQLException {

        String sql = "UPDATE tmp_sr_mekki SET "
                + " kcpno = ?, ukeiresuu = ?, domekosuu = ?, gouki = ?, tantousya = ?, "
                + " mekkikaishinichiji = ?, mekkijyoukennia = ?, mekkijyoukenniam = ?, "
                + " mekkijyoukensna = ?, mekkijyoukensnam = ?, shukkakosuu = ?, "
                + " budomari = ?, makuatsunimin = ?, makuatsunimax = ?, makuatsuniave = ?, "
                + " makuatsusnmin = ?, makuatsusnmax = ?, makuatsusnave = ?, nurekensakekka = ?, "
                + " tainetsukensakekka = ?, gaikankensakekka = ?, bikou1 = ?, bikou2 = ?, "
                + " koushinnichiji = ?, domemeisai = ?, kensanichiji = ?, kensatantousya = ?, "
                + " tokuisaki = ?, lotkubuncode = ?, ownercode = ?, ukeiretannijyuryo = ?, "
                + " ukeiresoujyuryou = ?, mekkibasyo = ?, mekkibasyosetubi = ?, "
                + " mekkisyuryounichiji = ?, syuryousya = ?, kensatannijyuryo = ?, "
                + " kensasoujyuryou = ?, netusyorijyouken = ?, netusyorikaisinichiji = ?, "
                + " netusyoritantousya = ?, jisyakusenbetukaisinichiji = ?, jisyakusenbetutantousya = ?, "
                + " ijouhakkou = ?, ijourank = ?, makuatsukakunin = ?, testhin = ?, "
                + " tsunpouave = ?, mekkisyurui = ?, revision = ?, deleteflag = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? AND domemeisai = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrMekki> srMekkiList = getSrMekkiData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekino);
        SrMekki srMekki = null;
        if (!srMekkiList.isEmpty()) {
            srMekki = srMekkiList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrMekki(false, newRev, 0, "", "", "", systemTime, itemList, srMekki, jissekino, processData);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);        
        params.add(processData.getHiddenDataMap().get("domemeisai"));
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ﾒｯｷ_仮登録(tmp_sr_mekki)削除処理
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
    private void deleteTmpSrMekki(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino,ProcessData processData) throws SQLException {

        String sql = "DELETE FROM tmp_sr_mekki "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? AND domemeisai = ? AND revision = ?";

        //更新値設定
        List<Object> params = new ArrayList<>();

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(processData.getHiddenDataMap().get("domemeisai"));
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ﾒｯｷ_仮登録(tmp_sr_mekki)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srMekkiData ﾒｯｷデータ
     * @param jissekino 実績No
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrMekki(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList, SrMekki srMekkiData, int jissekino, ProcessData processData) {
        List<Object> params = new ArrayList<>();
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }        
        
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.KCPNO, srMekkiData))); //KCPNO
        
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.SYORISUU, srMekkiData))); // 処理数
        
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.DOMEKOSUU, srMekkiData))); // ﾄﾞｰﾑ個数
         
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.GOUKI, srMekkiData))); // 号機
        
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.KAISHI_TANTOSYA_CODE, srMekkiData))); // 開始担当者
        
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.KAISHI_DAY, srMekkiData),
            getItemData(itemList, GXHDO101B038Const.KAISHI_TIME, srMekkiData))); //ﾒｯｷ開始日時
        
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.JYOUKENNI_A, srMekkiData))); // 条件NI(A)
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.JYOUKENNI_AM, srMekkiData))); // 条件NI(AM)
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.JYOUKENSN_A, srMekkiData))); // 条件SN(A)
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.JYOUKENSN_AM, srMekkiData))); // 条件SN(AM)
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.SHUKKAKOSUU, srMekkiData))); // 良品数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.BUDOMARI, srMekkiData))); // 歩留まり
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.MAKUATSUNI_MIN, srMekkiData))); // Ni膜厚(MIN)
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.MAKUATSUNI_MAX, srMekkiData))); // Ni膜厚(MAX)
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.MAKUATSUNI_AVE, srMekkiData))); // Ni膜厚(AVE)
        if (isInsert) {
            params.add(null); // Ni膜厚(STD)
        }

        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.MAKUATSUSN_MIN, srMekkiData))); // Sn膜厚(MIN)
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.MAKUATSUSN_MAX, srMekkiData))); // Sn膜厚(MAX)
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.MAKUATSUSN_AVE, srMekkiData))); // Sn膜厚(AVE)
        if (isInsert) {
            params.add(null); // Sn膜厚(STD)
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.NUREKENSAKEKKA, srMekkiData))); // 半田ﾇﾚ性
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.TAINETSUKENSAKEKKA, srMekkiData))); // 半田耐熱性
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.GAIKAN, srMekkiData))); // 外観

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.BIKO1, srMekkiData))); // 備考1
        
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.BIKO2, srMekkiData))); // 備考2
        
        if (isInsert) {
            params.add(null); // 備考3
            params.add(jissekino); //回数
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時
            params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B038Const.DOMEMEISAI, srMekkiData))); //使用ﾄﾞｰﾑ明細
            params.add(null); // 1回目調整前PH値
            params.add(null); // 1回目調整後PH値
            params.add(null); // 1回目調整時間
            params.add(null); // 2回目調整前PH値
            params.add(null); // 2回目調整後PH値
            params.add(null); // 2回目調整時間
            params.add(null); // T寸法
            params.add(null); // ﾊﾞﾚﾙNo
            params.add(null); // Ni膜厚(CPL)
            params.add(null); // Sn膜厚(CPL)
            params.add(null); // 測定日時
            params.add(null); // Ni膜厚(CV)
            params.add(null); // Sn膜厚(CV)
        } else {
            params.add(systemTime); //更新日時
            params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B038Const.DOMEMEISAI, srMekkiData))); //使用ﾄﾞｰﾑ明細
        }
        
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.KENSA_DAY, srMekkiData),
            getItemData(itemList, GXHDO101B038Const.KENSA_TIME, srMekkiData))); //検査日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.KENSA_TANTOUSYA, srMekkiData))); //検査・外観担当者
        if (isInsert) {
            params.add(null); // 膜厚担当者
            params.add(null); // Sn開始日時
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.KYAKUSAKI, srMekkiData))); //客先 
        
        params.add(DBUtil.stringToStringObjectDefaultNull(StringUtil.nullToBlank(processData.getHiddenDataMap().get("lotkubuncode")))); // ﾛｯﾄ区分
        
        params.add(DBUtil.stringToStringObjectDefaultNull(StringUtil.nullToBlank(processData.getHiddenDataMap().get("ownercode")))); //ｵｰﾅｰ
        
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.UKEIRE_TANNIJYURYO, srMekkiData))); //受入れ単位重量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.UKEIRE_SOUJYURYOU, srMekkiData))); //受入れ総重量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.MEKKIBASYO, srMekkiData))); //ﾒｯｷ場所
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.MEKKIBASYOSETUBI, srMekkiData))); //ﾒｯｷ場所設備

        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.SHURYOU_DAY, srMekkiData),
            getItemData(itemList, GXHDO101B038Const.SHURYOU_TIME, srMekkiData))); // ﾒｯｷ終了日時
        
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.SHURYOU_TANTOUSYA_CODE, srMekkiData))); // 終了担当者
        
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.KENSA_TANNIJYURYO, srMekkiData))); // 検査単位重量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.KENSA_SOUJYURYOU, srMekkiData))); // 検査総重量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.NETUSYORI_JYOUKEN, srMekkiData))); // 熱処理条件
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.NETUSYORI_KAISI_DAY, srMekkiData),
            getItemData(itemList, GXHDO101B038Const.NETUSYORI_KAISI_TIME, srMekkiData))); // 熱処理開始日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.NETUSYORI_TANTOSYA_CODE, srMekkiData))); // 熱処理担当者
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.JISYAKUSENBETU_KAISI_DAY, srMekkiData),
            getItemData(itemList, GXHDO101B038Const.JISYAKUSENBETU_KAISI_TIME, srMekkiData))); // 磁石選別開始日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.JISYAKUSENBETU_TANTOSYA_CODE, srMekkiData))); // 磁石選別担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.IJOUHAKKOU, srMekkiData))); // 異常発行
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.IJOURANK, srMekkiData))); // 異常品ﾗﾝｸ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.MAKUATSU_KAKUNIN, srMekkiData))); // 膜厚確認
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.TESTHIN, srMekkiData))); // ﾃｽﾄ品
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B038Const.TSUNPOU, srMekkiData))); // T寸法AVE
        params.add(DBUtil.stringToStringObject(StringUtil.nullToBlank(processData.getHiddenDataMap().get("mekkisyurui")))); // ﾒｯｷ種類

        params.add(newRev); //revision
        params.add(deleteflag); //削除ﾌﾗｸﾞ
        
        return params;
    }

    /**
     * ﾒｯｷ(sr_mekki)登録処理
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
     * @param srMekki 仮登録データ
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void insertSrMekki(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino,Timestamp systemTime, List<FXHDD01> itemList, SrMekki srMekki, ProcessData processData) throws SQLException {

        String sql = "INSERT INTO sr_mekki ("
                + "kojyo, lotno, edaban, kcpno, ukeiresuu, domekosuu, gouki, tantousya, "
                + "mekkikaishinichiji, mekkijyoukennia, mekkijyoukenniam, mekkijyoukensna, "
                + "mekkijyoukensnam, shukkakosuu, budomari, makuatsunimin, makuatsunimax, "
                + "makuatsuniave, makuatsunistd, makuatsusnmin, makuatsusnmax, makuatsusnave, "
                + "makuatsusnstd, nurekensakekka, tainetsukensakekka, gaikankensakekka, bikou1, "
                + "bikou2, bikou3, jissekino, tourokunichiji, koushinnichiji, domemeisai, "
                + "tyoseimaeph1, tyoseigoph1, tyoseijikan1, tyoseimaeph2, tyoseigoph2, "
                + "tyoseijikan2, tsunpou, barrelno, makuatsunicpl, makuatsusncpl, "
                + "sokuteinichiji, makuatsunicv, makuatsusncv, kensanichiji, kensatantousya, "
                + "makuatsutantosya, kaishinichiji_sn, tokuisaki, lotkubuncode, ownercode, "
                + "ukeiretannijyuryo, ukeiresoujyuryou, mekkibasyo, mekkibasyosetubi, "
                + "mekkisyuryounichiji, syuryousya, kensatannijyuryo, kensasoujyuryou, "
                + "netusyorijyouken, netusyorikaisinichiji, netusyoritantousya, "
                + "jisyakusenbetukaisinichiji, jisyakusenbetutantousya, ijouhakkou, "
                + "ijourank, makuatsukakunin, testhin, tsunpouave, mekkisyurui, revision "
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrMekki(true, newRev, kojyo, lotNo, edaban, jissekino, systemTime, itemList, srMekki, processData);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ﾒｯｷ(sr_gdmekki)更新処理
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
    private void updateSrMekki(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, ProcessData processData) throws SQLException {
        String sql = "UPDATE sr_mekki SET "
                + " kcpno = ?, ukeiresuu = ?, domekosuu = ?, gouki = ?, tantousya = ?, "
                + " mekkikaishinichiji = ?, mekkijyoukennia = ?, mekkijyoukenniam = ?, "
                + " mekkijyoukensna = ?, mekkijyoukensnam = ?, shukkakosuu = ?, "
                + " budomari = ?, makuatsunimin = ?, makuatsunimax = ?, makuatsuniave = ?, "
                + " makuatsusnmin = ?, makuatsusnmax = ?, makuatsusnave = ?, nurekensakekka = ?, "
                + " tainetsukensakekka = ?, gaikankensakekka = ?, bikou1 = ?, bikou2 = ?, "
                + " koushinnichiji = ?, domemeisai = ?, kensanichiji = ?, kensatantousya = ?, "
                + " tokuisaki = ?, lotkubuncode = ?, ownercode = ?, ukeiretannijyuryo = ?, "
                + " ukeiresoujyuryou = ?, mekkibasyo = ?, mekkibasyosetubi = ?, "
                + " mekkisyuryounichiji = ?, syuryousya = ?, kensatannijyuryo = ?, "
                + " kensasoujyuryou = ?, netusyorijyouken = ?, netusyorikaisinichiji = ?, "
                + " netusyoritantousya = ?, jisyakusenbetukaisinichiji = ?, jisyakusenbetutantousya = ?, "
                + " ijouhakkou = ?, ijourank = ?, makuatsukakunin = ?, testhin = ?, "
                + " tsunpouave = ?, mekkisyurui = ?, revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? AND domemeisai = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrMekki> srMekkiList = getSrMekkiData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekino);
        SrMekki srMekki = null;
        if (!srMekkiList.isEmpty()) {
            srMekki = srMekkiList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrMekki(false, newRev, "", "", "", jissekino, systemTime, itemList, srMekki, processData);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(processData.getHiddenDataMap().get("domemeisai"));
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ﾒｯｷ(sr_mekki)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srMekkiData ﾒｯｷデータ
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrMekki(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            int jissekino, Timestamp systemTime, List<FXHDD01> itemList, SrMekki srMekkiData, ProcessData processData ) {
        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }        
        
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B038Const.KCPNO, srMekkiData))); //KCPNO
        
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B038Const.SYORISUU, srMekkiData))); // 処理数
        
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B038Const.DOMEKOSUU, srMekkiData))); // ﾄﾞｰﾑ個数
         
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B038Const.GOUKI, srMekkiData))); // 号機
        
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B038Const.KAISHI_TANTOSYA_CODE, srMekkiData))); // 開始担当者
        
        params.add(DBUtil.stringToDateObject(getItemData(itemList, GXHDO101B038Const.KAISHI_DAY, srMekkiData),
            getItemData(itemList, GXHDO101B038Const.KAISHI_TIME, srMekkiData))); //ﾒｯｷ開始日時
        
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B038Const.JYOUKENNI_A, srMekkiData))); // 条件NI(A)
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B038Const.JYOUKENNI_AM, srMekkiData))); // 条件NI(AM)
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B038Const.JYOUKENSN_A, srMekkiData))); // 条件SN(A)
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B038Const.JYOUKENSN_AM, srMekkiData))); // 条件SN(AM)
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B038Const.SHUKKAKOSUU, srMekkiData))); // 良品数
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B038Const.BUDOMARI, srMekkiData))); // 歩留まり
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B038Const.MAKUATSUNI_MIN, srMekkiData))); // Ni膜厚(MIN)
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B038Const.MAKUATSUNI_MAX, srMekkiData))); // Ni膜厚(MAX)
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B038Const.MAKUATSUNI_AVE, srMekkiData))); // Ni膜厚(AVE)
        if (isInsert) {
            params.add(BigDecimal.ZERO); // Ni膜厚(STD)
        }

        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B038Const.MAKUATSUSN_MIN, srMekkiData))); // Sn膜厚(MIN)
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B038Const.MAKUATSUSN_MAX, srMekkiData))); // Sn膜厚(MAX)
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B038Const.MAKUATSUSN_AVE, srMekkiData))); // Sn膜厚(AVE)
        if (isInsert) {
            params.add(BigDecimal.ZERO); // Sn膜厚(STD)
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B038Const.NUREKENSAKEKKA, srMekkiData))); // 半田ﾇﾚ性
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B038Const.TAINETSUKENSAKEKKA, srMekkiData))); // 半田耐熱性
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B038Const.GAIKAN, srMekkiData))); // 外観

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B038Const.BIKO1, srMekkiData))); // 備考1
        
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B038Const.BIKO2, srMekkiData))); // 備考2
        
        if (isInsert) {
            params.add(""); // 備考3
            params.add(jissekino); //回数
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時
            params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B038Const.DOMEMEISAI, srMekkiData))); //使用ﾄﾞｰﾑ明細
            params.add(BigDecimal.ZERO); // 1回目調整前PH値
            params.add(BigDecimal.ZERO); // 1回目調整後PH値
            params.add(0); // 1回目調整時間
            params.add(BigDecimal.ZERO); // 2回目調整前PH値
            params.add(BigDecimal.ZERO); // 2回目調整後PH値
            params.add(0); // 2回目調整時間
            params.add(BigDecimal.ZERO); // T寸法
            params.add(0); // ﾊﾞﾚﾙNo
            params.add(BigDecimal.ZERO); // Ni膜厚(CPL)
            params.add(BigDecimal.ZERO); // Sn膜厚(CPL)
            params.add("0000-00-00 00:00:00"); // 測定日時
            params.add(BigDecimal.ZERO); // Ni膜厚(CV)
            params.add(BigDecimal.ZERO); // Sn膜厚(CV)
        } else {
            params.add(systemTime); //更新日時
            params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B038Const.DOMEMEISAI, srMekkiData))); //使用ﾄﾞｰﾑ明細
        }
        
        params.add(DBUtil.stringToDateObject(getItemData(itemList, GXHDO101B038Const.KENSA_DAY, srMekkiData),
            getItemData(itemList, GXHDO101B038Const.KENSA_TIME, srMekkiData))); //検査日時
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B038Const.KENSA_TANTOUSYA, srMekkiData))); //検査・外観担当者
        if (isInsert) {
            params.add(""); // 膜厚担当者
            params.add("0000-00-00 00:00:00"); // Sn開始日時
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B038Const.KYAKUSAKI, srMekkiData))); //客先 
        
        params.add(DBUtil.stringToStringObject(StringUtil.nullToBlank(processData.getHiddenDataMap().get("lotkubuncode")))); // ﾛｯﾄ区分
        
        params.add(DBUtil.stringToStringObject(StringUtil.nullToBlank(processData.getHiddenDataMap().get("ownercode")))); //ｵｰﾅｰ
        
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B038Const.UKEIRE_TANNIJYURYO, srMekkiData))); //受入れ単位重量
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B038Const.UKEIRE_SOUJYURYOU, srMekkiData))); //受入れ総重量
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B038Const.MEKKIBASYO, srMekkiData))); //ﾒｯｷ場所
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B038Const.MEKKIBASYOSETUBI, srMekkiData))); //ﾒｯｷ場所設備

        params.add(DBUtil.stringToDateObject(getItemData(itemList, GXHDO101B038Const.SHURYOU_DAY, srMekkiData),
            getItemData(itemList, GXHDO101B038Const.SHURYOU_TIME, srMekkiData))); // ﾒｯｷ終了日時
        
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B038Const.SHURYOU_TANTOUSYA_CODE, srMekkiData))); // 終了担当者
        
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B038Const.KENSA_TANNIJYURYO, srMekkiData))); // 検査単位重量
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B038Const.KENSA_SOUJYURYOU, srMekkiData))); // 検査総重量
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B038Const.NETUSYORI_JYOUKEN, srMekkiData))); // 熱処理条件
        params.add(DBUtil.stringToDateObject(getItemData(itemList, GXHDO101B038Const.NETUSYORI_KAISI_DAY, srMekkiData),
            getItemData(itemList, GXHDO101B038Const.NETUSYORI_KAISI_TIME, srMekkiData))); // 熱処理開始日時
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B038Const.NETUSYORI_TANTOSYA_CODE, srMekkiData))); // 熱処理担当者
        params.add(DBUtil.stringToDateObject(getItemData(itemList, GXHDO101B038Const.JISYAKUSENBETU_KAISI_DAY, srMekkiData),
            getItemData(itemList, GXHDO101B038Const.JISYAKUSENBETU_KAISI_TIME, srMekkiData))); // 磁石選別開始日時
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B038Const.JISYAKUSENBETU_TANTOSYA_CODE, srMekkiData))); // 磁石選別担当者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B038Const.IJOUHAKKOU, srMekkiData))); // 異常発行
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B038Const.IJOURANK, srMekkiData))); // 異常品ﾗﾝｸ
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B038Const.MAKUATSU_KAKUNIN, srMekkiData))); // 膜厚確認
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B038Const.TESTHIN, srMekkiData))); // ﾃｽﾄ品
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B038Const.TSUNPOU, srMekkiData))); // T寸法AVE
        params.add(DBUtil.stringToStringObject(StringUtil.nullToBlank(processData.getHiddenDataMap().get("mekkisyurui")))); // ﾒｯｷ種類

        params.add(newRev); //revision
        
        return params;
    }

    /**
     * ﾒｯｷ(sr_mekki)削除処理
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
    private void deleteSrMekki(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino,ProcessData processData) throws SQLException {

        String sql = "DELETE FROM sr_mekki "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? AND domemeisai = ? AND revision = ?";

        //更新値設定
        List<Object> params = new ArrayList<>();

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(processData.getHiddenDataMap().get("domemeisai"));
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * [ﾒｯｷ_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_mekki "
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
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKaishiDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO101B038Const.KAISHI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO101B038Const.KAISHI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 終了時間設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setShuryouDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO101B038Const.SHURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO101B038Const.SHURYOU_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }

        processData.setMethod("");
        return processData;
    }
    
    /**
     * 検査時間設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKensaDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO101B038Const.KENSA_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO101B038Const.KENSA_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }

        processData.setMethod("");
        return processData;
    }
    
    /**
     * 熱処理時間設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setNetusyoriDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO101B038Const.NETUSYORI_KAISI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO101B038Const.NETUSYORI_KAISI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }

        processData.setMethod("");
        return processData;
    }
    
    /**
     * 磁石選別時間設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setJisyakusenbetuDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO101B038Const.JISYAKUSENBETU_KAISI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO101B038Const.JISYAKUSENBETU_KAISI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }

        processData.setMethod("");
        return processData;
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
     * @param srMekkiData ﾒｯｷデータ
     * @return DB値
     */
    private String getSrMekkiItemData(String itemId, SrMekki srMekkiData) {
        switch (itemId) {
            // 客先
            case GXHDO101B038Const.KYAKUSAKI:
                return StringUtil.nullToBlank(srMekkiData.getTokuisaki());
            // KCPNO
            case GXHDO101B038Const.KCPNO:
                return StringUtil.nullToBlank(srMekkiData.getKcpno());
            // 処理数
            case GXHDO101B038Const.SYORISUU:
                return StringUtil.nullToBlank(srMekkiData.getUkeiresuu());
            // 受入れ単位重量
            case GXHDO101B038Const.UKEIRE_TANNIJYURYO:
                return StringUtil.nullToBlank(srMekkiData.getUkeiretannijyuryo());
            // 受入れ総重量
            case GXHDO101B038Const.UKEIRE_SOUJYURYOU:
                return StringUtil.nullToBlank(srMekkiData.getUkeiresoujyuryou());
            // T寸法
            case GXHDO101B038Const.TSUNPOU:
                return StringUtil.nullToBlank(srMekkiData.getTsunpouave());
            // ﾒｯｷ場所
            case GXHDO101B038Const.MEKKIBASYO:
                return StringUtil.nullToBlank(srMekkiData.getMekkibasyo());
            // ﾒｯｷ場所設備
            case GXHDO101B038Const.MEKKIBASYOSETUBI:
                return StringUtil.nullToBlank(srMekkiData.getMekkibasyosetubi());
            // 号機
            case GXHDO101B038Const.GOUKI:
                return StringUtil.nullToBlank(srMekkiData.getGouki());             
            // ﾄﾞｰﾑ個数
            case GXHDO101B038Const.DOMEKOSUU:
                return StringUtil.nullToBlank(srMekkiData.getDomekosuu());
            // 使用ﾄﾞｰﾑ明細
            case GXHDO101B038Const.DOMEMEISAI:
                return StringUtil.nullToBlank(srMekkiData.getDomemeisai());
            // 条件NI(A)
            case GXHDO101B038Const.JYOUKENNI_A:
                return StringUtil.nullToBlank(srMekkiData.getMekkijyoukennia());
            // 条件NI(AM)
            case GXHDO101B038Const.JYOUKENNI_AM:
                return StringUtil.nullToBlank(srMekkiData.getMekkijyoukenniam());
            // 条件SN(A)
            case GXHDO101B038Const.JYOUKENSN_A:
                return StringUtil.nullToBlank(srMekkiData.getMekkijyoukensna());
            // 条件SN(AM)
            case GXHDO101B038Const.JYOUKENSN_AM:
                return StringUtil.nullToBlank(srMekkiData.getMekkijyoukensnam());
            // ﾒｯｷ開始日
            case GXHDO101B038Const.KAISHI_DAY:
                return DateUtil.formattedTimestamp(srMekkiData.getMekkikaishinichiji(), "yyMMdd");
            // ﾒｯｷ開始時間
            case GXHDO101B038Const.KAISHI_TIME:
                return DateUtil.formattedTimestamp(srMekkiData.getMekkikaishinichiji(), "HHmm");
            // 開始担当者
            case GXHDO101B038Const.KAISHI_TANTOSYA_CODE:
                return StringUtil.nullToBlank(srMekkiData.getTantosyacode());
            // ﾒｯｷ終了日
            case GXHDO101B038Const.SHURYOU_DAY:
                return DateUtil.formattedTimestamp(srMekkiData.getMekkisyuryounichiji(), "yyMMdd");
            // ﾒｯｷ終了時間
            case GXHDO101B038Const.SHURYOU_TIME:
                return DateUtil.formattedTimestamp(srMekkiData.getMekkisyuryounichiji(), "HHmm");
            // 終了担当者
            case GXHDO101B038Const.SHURYOU_TANTOUSYA_CODE:
                return StringUtil.nullToBlank(srMekkiData.getSyuryousya());
            // Ni膜厚(AVE)
            case GXHDO101B038Const.MAKUATSUNI_AVE:
                return StringUtil.nullToBlank(srMekkiData.getMakuatsuniave());
            // Ni膜厚(MAX)
            case GXHDO101B038Const.MAKUATSUNI_MAX:
                return StringUtil.nullToBlank(srMekkiData.getMakuatsunimax());
            // Ni膜厚(MIN)
            case GXHDO101B038Const.MAKUATSUNI_MIN:
                return StringUtil.nullToBlank(srMekkiData.getMakuatsunimin());
            // Sn膜厚(AVE)
            case GXHDO101B038Const.MAKUATSUSN_AVE:
                return StringUtil.nullToBlank(srMekkiData.getMakuatsusnave());
            // Sn膜厚(MAX)
            case GXHDO101B038Const.MAKUATSUSN_MAX:
                return StringUtil.nullToBlank(srMekkiData.getMakuatsusnmax());
            // Sn膜厚(MIN)
            case GXHDO101B038Const.MAKUATSUSN_MIN:
                return StringUtil.nullToBlank(srMekkiData.getMakuatsusnmin());
            // 半田ﾇﾚ性
            case GXHDO101B038Const.NUREKENSAKEKKA:
                return StringUtil.nullToBlank(srMekkiData.getNurekensakekka());
            // 半田耐熱性
            case GXHDO101B038Const.TAINETSUKENSAKEKKA:
                return StringUtil.nullToBlank(srMekkiData.getTainetsukensakekka());
            // 検査日
            case GXHDO101B038Const.KENSA_DAY:
                return DateUtil.formattedTimestamp(srMekkiData.getKensanichiji(), "yyMMdd");
            // 検査時刻
            case GXHDO101B038Const.KENSA_TIME:
                return DateUtil.formattedTimestamp(srMekkiData.getKensanichiji(), "HHmm");
            // 外観
            case GXHDO101B038Const.GAIKAN:
                return StringUtil.nullToBlank(srMekkiData.getGaikankensakekka());
            // 検査・外観担当者
            case GXHDO101B038Const.KENSA_TANTOUSYA:
                return StringUtil.nullToBlank(srMekkiData.getKensatantousya());
            // 検査単位重量
            case GXHDO101B038Const.KENSA_TANNIJYURYO:
                return StringUtil.nullToBlank(srMekkiData.getKensatannijyuryo());
            // 検査総重量
            case GXHDO101B038Const.KENSA_SOUJYURYOU:
                return StringUtil.nullToBlank(srMekkiData.getKensasoujyuryou());
            // 良品数
            case GXHDO101B038Const.SHUKKAKOSUU:
                return StringUtil.nullToBlank(srMekkiData.getShukkakosuu());
            // 歩留まり
            case GXHDO101B038Const.BUDOMARI:
                return StringUtil.nullToBlank(srMekkiData.getBudomari());
            // 熱処理条件
            case GXHDO101B038Const.NETUSYORI_JYOUKEN:
                return StringUtil.nullToBlank(srMekkiData.getNetusyorijyouken());
            // 熱処理日
            case GXHDO101B038Const.NETUSYORI_KAISI_DAY:
                return DateUtil.formattedTimestamp(srMekkiData.getNetusyorikaisinichiji(), "yyMMdd");
            // 熱処理時刻
            case GXHDO101B038Const.NETUSYORI_KAISI_TIME:
                return DateUtil.formattedTimestamp(srMekkiData.getNetusyorikaisinichiji(), "HHmm");
            // 熱処理担当者
            case GXHDO101B038Const.NETUSYORI_TANTOSYA_CODE:
                return StringUtil.nullToBlank(srMekkiData.getNetusyoritantousya());
            // 磁石選別日
            case GXHDO101B038Const.JISYAKUSENBETU_KAISI_DAY:
                return DateUtil.formattedTimestamp(srMekkiData.getJisyakusenbetukaisinichiji(), "yyMMdd");
            // 磁石選別時刻
            case GXHDO101B038Const.JISYAKUSENBETU_KAISI_TIME:
                return DateUtil.formattedTimestamp(srMekkiData.getJisyakusenbetukaisinichiji(), "HHmm");
            // 磁石選別担当者
            case GXHDO101B038Const.JISYAKUSENBETU_TANTOSYA_CODE:
                return StringUtil.nullToBlank(srMekkiData.getJisyakusenbetutantousya());
            // 異常発行
            case GXHDO101B038Const.IJOUHAKKOU:
                return StringUtil.nullToBlank(srMekkiData.getIjouhakkou());
            // 異常品ﾗﾝｸ
            case GXHDO101B038Const.IJOURANK:
                return StringUtil.nullToBlank(srMekkiData.getIjourank());
            // 膜厚確認
            case GXHDO101B038Const.MAKUATSU_KAKUNIN:
                return StringUtil.nullToBlank(srMekkiData.getMakuatsukakunin());
            // ﾃｽﾄ品
            case GXHDO101B038Const.TESTHIN:
                return StringUtil.nullToBlank(srMekkiData.getTesthin());
            // 備考1
            case GXHDO101B038Const.BIKO1:
                return StringUtil.nullToBlank(srMekkiData.getBiko1());
            // 備考2
            case GXHDO101B038Const.BIKO2:
                return StringUtil.nullToBlank(srMekkiData.getBiko2());
            default:
                return null;            
        }
    }

    /**
     * ﾒｯｷ_仮登録(tmp_sr_mekki)登録処理(削除時)
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
    private void insertDeleteDataTmpSrMekki(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, ProcessData processData) throws SQLException {

        String sql = "INSERT INTO tmp_sr_mekki ("
                + "kojyo, lotno, edaban, kcpno, ukeiresuu, domekosuu, gouki, tantousya, "
                + "mekkikaishinichiji, mekkijyoukennia, mekkijyoukenniam, mekkijyoukensna, "
                + "mekkijyoukensnam, shukkakosuu, budomari, makuatsunimin, makuatsunimax, "
                + "makuatsuniave, makuatsunistd, makuatsusnmin, makuatsusnmax, makuatsusnave, "
                + "makuatsusnstd, nurekensakekka, tainetsukensakekka, gaikankensakekka, bikou1, "
                + "bikou2, bikou3, jissekino, tourokunichiji, koushinnichiji, domemeisai, "
                + "tyoseimaeph1, tyoseigoph1, tyoseijikan1, tyoseimaeph2, tyoseigoph2, "
                + "tyoseijikan2, tsunpou, barrelno, makuatsunicpl, makuatsusncpl, "
                + "sokuteinichiji, makuatsunicv, makuatsusncv, kensanichiji, kensatantousya, "
                + "makuatsutantosya, kaishinichiji_sn, tokuisaki, lotkubuncode, ownercode, "
                + "ukeiretannijyuryo, ukeiresoujyuryou, mekkibasyo, mekkibasyosetubi, "
                + "mekkisyuryounichiji, syuryousya, kensatannijyuryo, kensasoujyuryou, "
                + "netusyorijyouken, netusyorikaisinichiji, netusyoritantousya, "
                + "jisyakusenbetukaisinichiji, jisyakusenbetutantousya, ijouhakkou, "
                + "ijourank, makuatsukakunin, testhin, tsunpouave, mekkisyurui, revision, "
                + "deleteflag "
                + ") SELECT "
                + "kojyo, lotno, edaban, kcpno, ukeiresuu, domekosuu, gouki, tantousya, "
                + "mekkikaishinichiji, mekkijyoukennia, mekkijyoukenniam, mekkijyoukensna, "
                + "mekkijyoukensnam, shukkakosuu, budomari, makuatsunimin, makuatsunimax, "
                + "makuatsuniave, makuatsunistd, makuatsusnmin, makuatsusnmax, makuatsusnave, "
                + "makuatsusnstd, nurekensakekka, tainetsukensakekka, gaikankensakekka, bikou1, "
                + "bikou2, bikou3, jissekino, ?, ?, domemeisai, "
                + "tyoseimaeph1, tyoseigoph1, tyoseijikan1, tyoseimaeph2, tyoseigoph2, "
                + "tyoseijikan2, tsunpou, barrelno, makuatsunicpl, makuatsusncpl, "
                + "sokuteinichiji, makuatsunicv, makuatsusncv, kensanichiji, kensatantousya, "
                + "makuatsutantosya, kaishinichiji_sn, tokuisaki, lotkubuncode, ownercode, "
                + "ukeiretannijyuryo, ukeiresoujyuryou, mekkibasyo, mekkibasyosetubi, "
                + "mekkisyuryounichiji, syuryousya, kensatannijyuryo, kensasoujyuryou, "
                + "netusyorijyouken, netusyorikaisinichiji, netusyoritantousya, "
                + "jisyakusenbetukaisinichiji, jisyakusenbetutantousya, ijouhakkou, "
                + "ijourank, makuatsukakunin, testhin, tsunpouave, mekkisyurui, ?, ? "
                + " FROM sr_mekki "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? AND domemeisai = ? ";

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
        params.add(jissekino); //作業回数
        params.add(processData.getHiddenDataMap().get("domemeisai"));//ﾄﾞｰﾑ明細
        
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }
    
    /**
     * WIP取込処理(ﾁｪｯｸ処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData checkWipTorikomi(ProcessData processData) {
        
        // ﾁｪｯｸ処理
        FXHDD01 ukeireTannijyuryoItem = getItemRow(processData.getItemList(), GXHDO101B038Const.UKEIRE_TANNIJYURYO);
        FXHDD01 syorisuuItem = getItemRow(processData.getItemList(), GXHDO101B038Const.SYORISUU);
        if(!StringUtil.isEmpty(ukeireTannijyuryoItem.getValue()) || !StringUtil.isEmpty(syorisuuItem.getValue())){
           // 警告メッセージの設定
            processData.setWarnMessage(MessageUtil.getMessage("XHD-000127")); 
        }
        processData.setMethod("wipTorikomi");
        return processData;
    }
    
    /**
     * WIP取込処理(ﾃﾞｰﾀ取得処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData wipTorikomi(ProcessData processData) throws SQLException{
        doWipTorikomi(processData, 1);
        return processData;
    }
    
    /**
     * WIP取込処理(ﾃﾞｰﾀ取得処理)
     *
     * @param processData 処理制御データ
     * @param syoriKubun 処理区分:0 画面初期処理、1 WIP取込ボタン押下処理
     * @return 処理制御データ
     */
    private ProcessData doWipTorikomi(ProcessData processData, int syoriKubun) throws SQLException{
        
        QueryRunner queryRunnerWip = new QueryRunner(processData.getDataSourceWip());
        QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
        
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String lotNo = (String) session.getAttribute("lotNo");
        
        //受入れ単位重量(単位重量)の取得
        //仕掛情報の単位重量取得
        Map shikakariData = loadShikakariTanijuryoData(queryRunnerWip, lotNo);
        if (shikakariData == null || shikakariData.isEmpty()) {
            if(syoriKubun == 0) {
                processData.getInitMessageList().add(MessageUtil.getMessage("XHD-000029"));
            } else {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000029"))));
            }
            return processData;
        }
        String tanijuryo = StringUtil.nullToBlank(getMapData(shikakariData, "tanijuryo")); //単位重量
        
        //処理数の取得
        String syorisuu = null;
        Map shukkakosuuData = loadFxhbm03Data(queryRunnerDoc, 26);
        if (shukkakosuuData == null || shukkakosuuData.isEmpty()) {
            if(syoriKubun == 0){
                processData.getInitMessageList().add(MessageUtil.getMessage("XHD-000083", "EXHD-000006"));
            } else {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000083", "EXHD-000006"))));
            }
            return processData;
        }
        String strfxhbm03List = StringUtil.nullToBlank(getMapData(shukkakosuuData, "data"));
        String fxhbm03DataArr[] = strfxhbm03List.split(",");
        // 実績情報の取得
        List<Jisseki> jissekiData = loadJissekiData(queryRunnerWip, lotNo, fxhbm03DataArr);
        if(jissekiData != null && jissekiData.size() > 0){
            int dbShorisu = jissekiData.get(0).getSyorisuu(); //処理数               
            if(dbShorisu > 0){
                syorisuu = String.valueOf(dbShorisu);
            }
        }
        
        // 画面表示設定
        // 受入れ単位重量
        FXHDD01 itemUkeireTanniJuryo = getItemRow(processData.getItemList(), GXHDO101B038Const.UKEIRE_TANNIJYURYO);
        itemUkeireTanniJuryo.setValue(NumberUtil.getTruncatData(tanijuryo, itemUkeireTanniJuryo.getInputLength(),itemUkeireTanniJuryo.getInputLengthDec()));
        
        //処理数
        this.setItemData(processData, GXHDO101B038Const.SYORISUU, syorisuu);
        if (syoriKubun == 1) {
            processData.setMethod("");
        }
        return processData; 
    }
    
    
    /**
     * 仕掛データの単位重量検索
     *
     * @param queryRunnerWip QueryRunnerオブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadShikakariTanijuryoData(QueryRunner queryRunnerWip, String lotNo) throws SQLException {
        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 11);
        String lotNo3 = lotNo.substring(11, 14);

        // 仕掛情報データの取得
        String sql = "SELECT tanijuryo "
                + " FROM sikakari WHERE kojyo = ? AND lotno = ? AND edaban = ? ";

        List<Object> params = new ArrayList<>();
        params.add(lotNo1);
        params.add(lotNo2);
        params.add(lotNo3);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerWip.query(sql, new MapHandler(), params.toArray());
    }
    
    /**
     * 歩留まり計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData budomariKeisan(ProcessData processData) {
        //歩留まり計算処理(ﾁｪｯｸ処理)
        checkBudomariKeisan(processData, 1);
        return processData;
    }
    
    /**
     * 歩留まり計算処理(ﾁｪｯｸ処理)
     *
     * @param processData 処理制御データ
     * @param syoriKubun 処理区分:0 画面初期処理、1 歩留まり計算ボタン押下処理
     * @return 処理制御データ
     */
    private ProcessData checkBudomariKeisan(ProcessData processData, int syoriKubun) {
        
        List<FXHDD01> errFxhdd01List = new ArrayList<>();        
        // ﾁｪｯｸ処理
        FXHDD01 shukkakosuuItem = getItemRow(processData.getItemList(), GXHDO101B038Const.SHUKKAKOSUU);
        // 「良品数」ﾁｪｯｸ
        // 入力されていない場合
        if(StringUtil.isEmpty(shukkakosuuItem.getValue())){
            // ｴﾗｰﾒｯｾｰｼﾞを表示し、当処理(歩留まり計算)を終了する。
            if(syoriKubun == 0) {
                processData.getInitMessageList().add(MessageUtil.getMessage("XHD-000158"));
            } else {
                errFxhdd01List = Arrays.asList(shukkakosuuItem);
                processData.getErrorMessageInfoList().add(MessageUtil.getErrorMessageInfo("XHD-000158", true, true, errFxhdd01List));
            }
            return processData;
        }
        //数値ではない場合
        if(!NumberUtil.isNumeric(shukkakosuuItem.getValue())){
            // ｴﾗｰﾒｯｾｰｼﾞを表示し、当処理(歩留まり計算)を終了する。
            if(syoriKubun == 0) {
                processData.getInitMessageList().add(MessageUtil.getMessage("XHD-000159"));
            } else {
                errFxhdd01List = Arrays.asList(shukkakosuuItem);
                processData.getErrorMessageInfoList().add(MessageUtil.getErrorMessageInfo("XHD-000159", true, true, errFxhdd01List));
            }
            return processData;
            
        }
        //「処理数」ﾁｪｯｸ
        String syorisuuValue = null;
        FXHDD01 itemSyorisuu = getItemRow(processData.getItemList(), GXHDO101B038Const.SYORISUU);
        syorisuuValue = StringUtil.nullToBlank(processData.getHiddenDataMap().get("syorisuu"));
        if(StringUtil.isEmpty(syorisuuValue)){
            syorisuuValue = itemSyorisuu.getValue();
        }
        //A.0やNULLだった場合 ｴﾗｰﾒｯｾｰｼﾞを表示し、以降の処理を中止する。
        if ("".equals(syorisuuValue) || syorisuuValue == null || NumberUtil.isZero(syorisuuValue)) {
            // ｴﾗｰﾒｯｾｰｼﾞを表示し、当処理(歩留まり計算)を終了する。
            if(syoriKubun == 0){
                processData.getInitMessageList().add(MessageUtil.getMessage("XHD-000105"));
            } else {
                errFxhdd01List = Arrays.asList(itemSyorisuu);
                processData.getErrorMessageInfoList().add(MessageUtil.getErrorMessageInfo("XHD-000105", true, true, errFxhdd01List));
            }
            return processData;
        }
        // 数値ではない場合 ｴﾗｰﾒｯｾｰｼﾞを表示し、以降の処理を中止する。
        if (!NumberUtil.isNumeric(syorisuuValue)) {
            // ｴﾗｰﾒｯｾｰｼﾞを表示し、当処理(歩留まり計算)を終了する。
            if(syoriKubun == 0){
                processData.getInitMessageList().add(MessageUtil.getMessage("XHD-000105"));
            } else {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000105"))));
                errFxhdd01List = Arrays.asList(itemSyorisuu);
                processData.getErrorMessageInfoList().add(MessageUtil.getErrorMessageInfo("XHD-000105", true, true, errFxhdd01List));
            }
            return processData;
        }
        
        // 「歩留まり」計算処理
        doBudomariKeisan(processData, syorisuuValue, syoriKubun);
        return processData;
    }
    
    /**
     * 歩留まり計算処理
     *
     * @param processData 処理制御データ
     * @param syorisuu 処理数
     * @param syoriKubun 処理区分:0 画面初期処理、1 歩留まり計算ボタン押下処理
     * @return 処理制御データ
     */
    private ProcessData doBudomariKeisan(ProcessData processData, String syorisuu, int syoriKubun) {
        // 良品数
        String shukkakosuu = StringUtil.nullToBlank(getItemData(processData.getItemList(), GXHDO101B038Const.SHUKKAKOSUU, null));
        // 処理数を数値変換
        BigDecimal decSyorisuu = new BigDecimal(syorisuu);
        //良品数を数値変換
        BigDecimal decShukkakosuu = new BigDecimal(shukkakosuu);
        BigDecimal budomari = decShukkakosuu.divide(decSyorisuu, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2);
       
        String budomariValue = budomari.toPlainString();
        FXHDD01 itemBudomari = getItemRow(processData.getItemList(), GXHDO101B038Const.BUDOMARI);
        itemBudomari.setValue(budomariValue);
        if(syoriKubun == 1){
            processData.setMethod("");
            // 背景色をクリア
            for (FXHDD01 fxhdd01 : processData.getItemList()) {
                fxhdd01.setBackColorInput(fxhdd01.getBackColorInputDefault());
            }
        }
        return processData;
    }
    
    /**
     * ﾒｯｷ履歴取込処理(ﾁｪｯｸ処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData checkRirekiTorikomi(ProcessData processData) {
        // ﾁｪｯｸ処理
        // 号機
        FXHDD01 goukiItem = getItemRow(processData.getItemList(), GXHDO101B038Const.GOUKI);
        // ﾄﾞｰﾑ個数
        FXHDD01 domekosuuItem = getItemRow(processData.getItemList(), GXHDO101B038Const.DOMEKOSUU);
        // 使用ﾄﾞｰﾑ明細
        FXHDD01 domemeisaiItem = getItemRow(processData.getItemList(), GXHDO101B038Const.DOMEMEISAI);
        // 条件NI(A)
        FXHDD01 jyoukenniaItem = getItemRow(processData.getItemList(), GXHDO101B038Const.JYOUKENNI_A);
        // 条件NI(AM)
        FXHDD01 jyoukenniamItem = getItemRow(processData.getItemList(), GXHDO101B038Const.JYOUKENNI_AM);
        // 条件SN(A)
        FXHDD01 jyoukensaaItem = getItemRow(processData.getItemList(), GXHDO101B038Const.JYOUKENSN_A);
        // 条件SN(AM)
        FXHDD01 jyoukensaamItem = getItemRow(processData.getItemList(), GXHDO101B038Const.JYOUKENSN_AM);
        // ﾒｯｷ開始日
        FXHDD01 kaishiDayItem = getItemRow(processData.getItemList(), GXHDO101B038Const.KAISHI_DAY);
        // ﾒｯｷ開始時間
        FXHDD01 kaishiTimeItem = getItemRow(processData.getItemList(), GXHDO101B038Const.KAISHI_TIME);
        // 開始担当者
        FXHDD01 kaishiTantosyaItem = getItemRow(processData.getItemList(), GXHDO101B038Const.KAISHI_TANTOSYA_CODE);
        
        processData.setMethod("rirekiTorikomi");
        if(!StringUtil.isEmpty(goukiItem.getValue()) || !StringUtil.isEmpty(domekosuuItem.getValue())
                || !StringUtil.isEmpty(domemeisaiItem.getValue()) || !StringUtil.isEmpty(jyoukenniaItem.getValue())
                || !StringUtil.isEmpty(jyoukenniamItem.getValue()) || !StringUtil.isEmpty(jyoukensaaItem.getValue())
                || !StringUtil.isEmpty(jyoukensaamItem.getValue()) || !StringUtil.isEmpty(kaishiDayItem.getValue())
                || !StringUtil.isEmpty(kaishiTimeItem.getValue()) || !StringUtil.isEmpty(kaishiTantosyaItem.getValue())){
           // 警告メッセージの設定
            processData.setWarnMessage(MessageUtil.getMessage("XHD-000128")); 
        }
        
        return processData;
    }
    /**
     * ﾒｯｷ履歴取込処理(ﾃﾞｰﾀ取得処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData rirekiTorikomi(ProcessData processData) throws SQLException{
        doRirekiTorikomi(processData, 1);
        return processData;
    }
    /**
     * ﾒｯｷ履歴取込処理(ﾃﾞｰﾀ取得処理)
     *
     * @param processData 処理制御データ
     * @param syoriKubun 処理区分:0 画面初期処理、1 ﾒｯｷ履歴取込ボタン押下処理
     * @return 処理制御データ
     */
    private ProcessData doRirekiTorikomi(ProcessData processData, int syoriKubun) throws SQLException{

        QueryRunner queryRunnerEquipment = new QueryRunner(processData.getDataSourceEquipment());
        
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String lotNo = (String) session.getAttribute("lotNo");
        
        // 検査単位重量(単位重量)の取得
        //ﾒｯｷ履歴情報の取得
        List<MekkiRireki> mekkiRirekiData = loadMekkiRirekiData(queryRunnerEquipment, lotNo);
        if (mekkiRirekiData == null || mekkiRirekiData.isEmpty()) {
            if(syoriKubun == 0){
                processData.getInitMessageList().add(MessageUtil.getMessage("XHD-000129"));
            } else {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000129"))));
            }
            return processData;
        } else {        
            // 号機
            this.setItemData(processData, GXHDO101B038Const.GOUKI, StringUtil.nullToBlank(mekkiRirekiData.get(0).getGoukiCode())); 
            // ﾄﾞｰﾑ個数
            this.setItemData(processData, GXHDO101B038Const.DOMEKOSUU, StringUtil.nullToBlank(mekkiRirekiData.get(0).getBunkatuNo()));
            // 使用ﾄﾞｰﾑ明細
            this.setItemData(processData, GXHDO101B038Const.DOMEMEISAI, StringUtil.nullToBlank(mekkiRirekiData.get(0).getDomeNo()));
            // 条件NI(A)
            this.setItemData(processData, GXHDO101B038Const.JYOUKENNI_A, StringUtil.nullToBlank(mekkiRirekiData.get(0).getNiA()));
            // 条件NI(AM)
            this.setItemData(processData, GXHDO101B038Const.JYOUKENNI_AM, StringUtil.nullToBlank(mekkiRirekiData.get(0).getNiAM()));
            // 条件SN(A)
            this.setItemData(processData, GXHDO101B038Const.JYOUKENSN_A, StringUtil.nullToBlank(mekkiRirekiData.get(0).getSnA()));
            // 条件SN(AM)
            this.setItemData(processData, GXHDO101B038Const.JYOUKENSN_AM, StringUtil.nullToBlank(mekkiRirekiData.get(0).getSnAM()));
            // ﾒｯｷ開始日
            this.setItemData(processData, GXHDO101B038Const.KAISHI_DAY, DateUtil.formattedTimestamp(mekkiRirekiData.get(0).getStartNichiji(), "yyMMdd"));
            // ﾒｯｷ開始時間
            this.setItemData(processData, GXHDO101B038Const.KAISHI_TIME, DateUtil.formattedTimestamp(mekkiRirekiData.get(0).getStartNichiji(), "HHmm"));
            // 開始担当者
            this.setItemData(processData, GXHDO101B038Const.KAISHI_TANTOSYA_CODE, StringUtil.nullToBlank(mekkiRirekiData.get(0).getTonyuSyaCode()));
        }
        if(syoriKubun == 1){
            processData.setMethod("");            
        }

        return processData;
    }
    
    /**
     * [ﾒｯｷ履歴]から、ﾃﾞｰﾀを取得
     * @param queryRunnerEquipment オブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param date ﾊﾟﾗﾒｰﾀﾃﾞｰﾀ(検索キー)
     * @return 取得データ
     * @throws SQLException 
     */
     private List<MekkiRireki> loadMekkiRirekiData(QueryRunner queryRunnerEquipment, String lotNo) throws SQLException {
         
        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 11);
        String lotNo3 = lotNo.substring(11, 14);
        
        // ﾊﾟﾗﾒｰﾀﾏｽﾀデータの取得
        String sql = "SELECT BunkatuNo, GoukiCode, DomeNo, NiA, NiAM, SnA, "
                + "SnAM, StartNichiji, TonyuSyaCode "
                + "FROM mekki_rireki "
                + "WHERE Kojyo = ? AND LotNo = ? AND EdaBan = ? "
                + " ORDER BY BunkatuNo DESC";
        
        Map mapping = new HashMap<>();
        mapping.put("BunkatuNo", "bunkatuNo"); // ﾄﾞｰﾑ個数
        mapping.put("GoukiCode", "goukiCode"); // 号機ｺｰﾄﾞ
        mapping.put("DomeNo", "domeNo"); // 使用ﾄﾞｰﾑ明細
        mapping.put("NiA", "niA"); // 条件NI(A)
        mapping.put("NiAM", "niAM"); // 条件NI(AM)
        mapping.put("SnA", "snA"); // 条件SN(A)
        mapping.put("SnAM", "snAM"); // 条件SN(AM)
        mapping.put("StartNichiji", "startNichiji"); // ﾒｯｷ開始日時
        mapping.put("TonyuSyaCode", "tonyuSyaCode"); // 開始担当者
        
        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<MekkiRireki>> beanHandler = new BeanListHandler<>(MekkiRireki.class, rowProcessor);

        List<Object> params = new ArrayList<>();
        params.add(lotNo1);
        params.add(lotNo2);
        params.add(lotNo3);                

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerEquipment.query(sql, beanHandler, params.toArray());
    }
    
}
