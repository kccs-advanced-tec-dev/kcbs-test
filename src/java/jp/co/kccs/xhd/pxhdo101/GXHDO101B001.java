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
import jp.co.kccs.xhd.db.model.SrSpsprintGra;
import jp.co.kccs.xhd.db.model.SubSrSpsprintGra;
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
 * 変更日	2018/11/29<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * 変更日        2019/9/18<br>
 * 計画書No      K1811-DS001<br>
 * 変更者        KCSS K.Jo<br>
 * 変更理由      項目追加・変更<br>
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
 * 変更者	KCSS wxf<br>
 * 変更理由	項目追加・変更<br>
 * <br>
 * <br>
 * 変更日	2025/03/12<br>
 * 計画書No	MB2501-D004<br>
 * 変更者	KCSS A.Hayashi<br>
 * 変更理由	項目追加・変更<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101B001(SPS系印刷・SPSｸﾞﾗﾋﾞｱ)ロジック
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2018/11/29
 */
public class GXHDO101B001 implements IFormLogic {

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
            processData.setNoCheckButtonId(Arrays.asList(GXHDO101B001Const.BTN_MAKUATSU_SUBGAMEN_TOP,
                    GXHDO101B001Const.BTN_PTN_KYORI_X_SUBGAMEN_TOP,
                    GXHDO101B001Const.BTN_PTN_KYORI_Y_SUBGAMEN_TOP,
                    GXHDO101B001Const.BTN_START_DATETIME_TOP,
                    GXHDO101B001Const.BTN_END_DATETIME_TOP,
                    GXHDO101B001Const.BTN_MAKUATSU_SUBGAMEN_BOTTOM,
                    GXHDO101B001Const.BTN_PTN_KYORI_X_SUBGAMEN_BOTTOM,
                    GXHDO101B001Const.BTN_PTN_KYORI_Y_SUBGAMEN_BOTTOM,
                    GXHDO101B001Const.BTN_START_DATETIME_BOTTOM,
                    GXHDO101B001Const.BTN_END_DATETIME_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(GXHDO101B001Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO101B001Const.BTN_INSERT_TOP,
                    GXHDO101B001Const.BTN_DELETE_TOP,
                    GXHDO101B001Const.BTN_UPDATE_TOP,
                    GXHDO101B001Const.BTN_DATACOOPERATION_TOP,
                    GXHDO101B001Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO101B001Const.BTN_INSERT_BOTTOM,
                    GXHDO101B001Const.BTN_DELETE_BOTTOM,
                    GXHDO101B001Const.BTN_UPDATE_BOTTOM,
                    GXHDO101B001Const.BTN_DATACOOPERATION_BOTTOM));

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

        // 内部電極ﾍﾟｰｽﾄﾛｯﾄNo2
        FXHDD01 itemPasteLotNo2 = getItemRow(processData.getItemList(), GXHDO101B001Const.PASTE_LOT_NO2);
        // 内部電極ﾍﾟｰｽﾄ粘度2
        FXHDD01 itemPasteNendo2 = getItemRow(processData.getItemList(), GXHDO101B001Const.PASTE_NENDO2);
        // 内部電極ﾍﾟｰｽﾄ温度2
        FXHDD01 itemPasteOndo2 = getItemRow(processData.getItemList(), GXHDO101B001Const.PASTE_ONDO2);
        // 内部電極ﾍﾟｰｽﾄ固形分2
        FXHDD01 itemPasteKokeibun2 = getItemRow(processData.getItemList(), GXHDO101B001Const.PASTE_KOKEIBUN2);
	boolean no2ErrFlag = pasteChack(itemPasteLotNo2.getValue(), itemPasteNendo2.getValue(), itemPasteOndo2.getValue(), itemPasteKokeibun2.getValue());

	if (no2ErrFlag) {
	    List<FXHDD01> pasteNo3ErrList = Arrays.asList(itemPasteLotNo2, itemPasteNendo2, itemPasteOndo2, itemPasteKokeibun2);
	    return MessageUtil.getErrorMessageInfo("XHD-000236", true, true, pasteNo3ErrList);
	}
        
	// 内部電極ﾍﾟｰｽﾄﾛｯﾄNo3
	// 内部電極ﾍﾟｰｽﾄ粘度3
	// 内部電極ﾍﾟｰｽﾄ温度3
	// 内部電極ﾍﾟｰｽﾄ固形分3
	FXHDD01 itemPasteLotno3 = getItemRow(processData.getItemList(), GXHDO101B001Const.PASTE_LOT_NO3);
	FXHDD01 itemPasteNendo3 = getItemRow(processData.getItemList(), GXHDO101B001Const.PASTE_NENDO3);
	FXHDD01 itemPasteOndo3 = getItemRow(processData.getItemList(), GXHDO101B001Const.PASTE_ONDO3);
	FXHDD01 itemPasteKokeibun3 = getItemRow(processData.getItemList(), GXHDO101B001Const.PASTE_KOKEIBUN3);
	boolean no3ErrFlag = pasteChack(itemPasteLotno3.getValue(), itemPasteNendo3.getValue(), itemPasteOndo3.getValue(), itemPasteKokeibun3.getValue());

	if (no3ErrFlag) {
	    List<FXHDD01> pasteNo3ErrList = Arrays.asList(itemPasteLotno3, itemPasteNendo3, itemPasteOndo3, itemPasteKokeibun3);
	    return MessageUtil.getErrorMessageInfo("XHD-000236", true, true, pasteNo3ErrList);
	}
        
	// 内部電極ﾍﾟｰｽﾄﾛｯﾄNo4
	// 内部電極ﾍﾟｰｽﾄ粘度4
	// 内部電極ﾍﾟｰｽﾄ温度4
	// 内部電極ﾍﾟｰｽﾄ固形分4
	FXHDD01 itemPasteLotno4 = getItemRow(processData.getItemList(), GXHDO101B001Const.PASTE_LOT_NO4);
	FXHDD01 itemPasteNendo4 = getItemRow(processData.getItemList(), GXHDO101B001Const.PASTE_NENDO4);
	FXHDD01 itemPasteOndo4 = getItemRow(processData.getItemList(), GXHDO101B001Const.PASTE_ONDO4);
	FXHDD01 itemPasteKokeibun4 = getItemRow(processData.getItemList(), GXHDO101B001Const.PASTE_KOKEIBUN4);
	boolean no4ErrFlag = pasteChack(itemPasteLotno4.getValue(), itemPasteNendo4.getValue(), itemPasteOndo4.getValue(), itemPasteKokeibun4.getValue());

	if (no4ErrFlag) {
	    List<FXHDD01> pasteNo4ErrList = Arrays.asList(itemPasteLotno4, itemPasteNendo4, itemPasteOndo4, itemPasteKokeibun4);
	    return MessageUtil.getErrorMessageInfo("XHD-000237", true, true, pasteNo4ErrList);
	}
        
	// 内部電極ﾍﾟｰｽﾄﾛｯﾄNo5
	// 内部電極ﾍﾟｰｽﾄ粘度5
	// 内部電極ﾍﾟｰｽﾄ温度5
	// 内部電極ﾍﾟｰｽﾄ固形分5
	FXHDD01 itemPasteLotno5 = getItemRow(processData.getItemList(), GXHDO101B001Const.PASTE_LOT_NO5);
	FXHDD01 itemPasteNendo5 = getItemRow(processData.getItemList(), GXHDO101B001Const.PASTE_NENDO5);
	FXHDD01 itemPasteOndo5 = getItemRow(processData.getItemList(), GXHDO101B001Const.PASTE_ONDO5);
	FXHDD01 itemPasteKokeibun5 = getItemRow(processData.getItemList(), GXHDO101B001Const.PASTE_KOKEIBUN5);
	boolean no5ErrFlag = pasteChack(itemPasteLotno5.getValue(), itemPasteNendo5.getValue(), itemPasteOndo5.getValue(), itemPasteKokeibun5.getValue());

	if (no5ErrFlag) {
	    List<FXHDD01> pasteNo5ErrList = Arrays.asList(itemPasteLotno5, itemPasteNendo5, itemPasteOndo5, itemPasteKokeibun5);
	    return MessageUtil.getErrorMessageInfo("XHD-000238", true, true, pasteNo5ErrList);
	}
        
        FXHDD01 itemHando = getItemRow(processData.getItemList(), GXHDO101B001Const.SEIHAN_OR_HANDOU_MEI);
        FXHDD01 itemDenkyoku = getItemRow(processData.getItemList(), GXHDO101B001Const.DENKYOKU_SEIHAN_MEI);
        if (itemHando != null && !"".equals(itemHando.getValue()) && itemDenkyoku != null && !"".equals(itemDenkyoku.getValue())) {
            
            if (!itemHando.getValue().startsWith(itemDenkyoku.getValue())) {
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemHando);
                return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemHando.getLabel1());
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

                // 印刷SPSｸﾞﾗﾋﾞｱ_仮登録登録処理
                insertTmpSrSpsprintGra(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

                // 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面仮登録登録処理
                insertTmpSubSrSpsprintGra(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, systemTime);

                // 前工程WIP取込ｻﾌﾞ画面仮登録登録処理
                insertTmpSrMwiplotlink(queryRunnerQcdb, conQcdb, kojyo, lotNo8, edaban, systemTime);

            } else {

                // 印刷SPSｸﾞﾗﾋﾞｱ_仮登録更新処理
                updateTmpSrSpsprintGra(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

                // 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面仮登録更新処理
                updateTmpSubSrSpsprintGra(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo8, edaban, systemTime);
                
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

        // 内部電極ﾍﾟｰｽﾄﾛｯﾄNo2
        FXHDD01 itemPasteLotNo2 = getItemRow(processData.getItemList(), GXHDO101B001Const.PASTE_LOT_NO2);
        // 内部電極ﾍﾟｰｽﾄ粘度2
        FXHDD01 itemPasteNendo2 = getItemRow(processData.getItemList(), GXHDO101B001Const.PASTE_NENDO2);
        // 内部電極ﾍﾟｰｽﾄ温度2
        FXHDD01 itemPasteOndo2 = getItemRow(processData.getItemList(), GXHDO101B001Const.PASTE_ONDO2);
        // 内部電極ﾍﾟｰｽﾄ固形分2
        FXHDD01 itemPasteKokeibun2 = getItemRow(processData.getItemList(), GXHDO101B001Const.PASTE_KOKEIBUN2);
        
        boolean errFlag  = pasteChack(itemPasteLotNo2.getValue(), itemPasteNendo2.getValue(), itemPasteOndo2.getValue(), itemPasteKokeibun2.getValue());
               
        if (errFlag) {
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemPasteLotNo2,itemPasteNendo2,itemPasteOndo2,itemPasteKokeibun2);
            return MessageUtil.getErrorMessageInfo("XHD-000096", true, true, errFxhdd01List);
        }
        
	// 内部電極ﾍﾟｰｽﾄﾛｯﾄNo3
	// 内部電極ﾍﾟｰｽﾄ粘度3
	// 内部電極ﾍﾟｰｽﾄ温度3
	// 内部電極ﾍﾟｰｽﾄ固形分3
	FXHDD01 itemPasteLotno3 = getItemRow(processData.getItemList(), GXHDO101B001Const.PASTE_LOT_NO3);
	FXHDD01 itemPasteNendo3 = getItemRow(processData.getItemList(), GXHDO101B001Const.PASTE_NENDO3);
	FXHDD01 itemPasteOndo3 = getItemRow(processData.getItemList(), GXHDO101B001Const.PASTE_ONDO3);
	FXHDD01 itemPasteKokeibun3 = getItemRow(processData.getItemList(), GXHDO101B001Const.PASTE_KOKEIBUN3);
	boolean no3ErrFlag = pasteChack(itemPasteLotno3.getValue(), itemPasteNendo3.getValue(), itemPasteOndo3.getValue(), itemPasteKokeibun3.getValue());

	if (no3ErrFlag) {
	    List<FXHDD01> pasteNo3ErrList = Arrays.asList(itemPasteLotno3, itemPasteNendo3, itemPasteOndo3, itemPasteKokeibun3);
	    return MessageUtil.getErrorMessageInfo("XHD-000236", true, true, pasteNo3ErrList);
	}
        
	// 内部電極ﾍﾟｰｽﾄﾛｯﾄNo4
	// 内部電極ﾍﾟｰｽﾄ粘度4
	// 内部電極ﾍﾟｰｽﾄ温度4
	// 内部電極ﾍﾟｰｽﾄ固形分4
	FXHDD01 itemPasteLotno4 = getItemRow(processData.getItemList(), GXHDO101B001Const.PASTE_LOT_NO4);
	FXHDD01 itemPasteNendo4 = getItemRow(processData.getItemList(), GXHDO101B001Const.PASTE_NENDO4);
	FXHDD01 itemPasteOndo4 = getItemRow(processData.getItemList(), GXHDO101B001Const.PASTE_ONDO4);
	FXHDD01 itemPasteKokeibun4 = getItemRow(processData.getItemList(), GXHDO101B001Const.PASTE_KOKEIBUN4);
	boolean no4ErrFlag = pasteChack(itemPasteLotno4.getValue(), itemPasteNendo4.getValue(), itemPasteOndo4.getValue(), itemPasteKokeibun4.getValue());

	if (no4ErrFlag) {
	    List<FXHDD01> pasteNo4ErrList = Arrays.asList(itemPasteLotno4, itemPasteNendo4, itemPasteOndo4, itemPasteKokeibun4);
	    return MessageUtil.getErrorMessageInfo("XHD-000237", true, true, pasteNo4ErrList);
	}
        
	// 内部電極ﾍﾟｰｽﾄﾛｯﾄNo5
	// 内部電極ﾍﾟｰｽﾄ粘度5
	// 内部電極ﾍﾟｰｽﾄ温度5
	// 内部電極ﾍﾟｰｽﾄ固形分5
	FXHDD01 itemPasteLotno5 = getItemRow(processData.getItemList(), GXHDO101B001Const.PASTE_LOT_NO5);
	FXHDD01 itemPasteNendo5 = getItemRow(processData.getItemList(), GXHDO101B001Const.PASTE_NENDO5);
	FXHDD01 itemPasteOndo5 = getItemRow(processData.getItemList(), GXHDO101B001Const.PASTE_ONDO5);
	FXHDD01 itemPasteKokeibun5 = getItemRow(processData.getItemList(), GXHDO101B001Const.PASTE_KOKEIBUN5);
	boolean no5ErrFlag = pasteChack(itemPasteLotno5.getValue(), itemPasteNendo5.getValue(), itemPasteOndo5.getValue(), itemPasteKokeibun5.getValue());

	if (no5ErrFlag) {
	    List<FXHDD01> pasteNo5ErrList = Arrays.asList(itemPasteLotno5, itemPasteNendo5, itemPasteOndo5, itemPasteKokeibun5);
	    return MessageUtil.getErrorMessageInfo("XHD-000238", true, true, pasteNo5ErrList);
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
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemInsatsuKaishiDay, itemInsatsuKaishiTime, itemInsatsuShuryouDay, itemInsatsuShuryouTime);
            return MessageUtil.getErrorMessageInfo("", msgCheckR001, true, true, errFxhdd01List);
        }
        //版胴名、電極製版名
        FXHDD01 itemHando = getItemRow(processData.getItemList(), GXHDO101B001Const.SEIHAN_OR_HANDOU_MEI);
        FXHDD01 itemDenkyoku = getItemRow(processData.getItemList(), GXHDO101B001Const.DENKYOKU_SEIHAN_MEI);
        if (itemHando != null && !"".equals(itemHando.getValue()) && itemDenkyoku != null && !"".equals(itemDenkyoku.getValue())) {
            
            if (!itemHando.getValue().startsWith(itemDenkyoku.getValue())) {
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemHando);
                return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemHando.getLabel1());
            }
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
            SrSpsprintGra tmpSrSpsprintGra = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {
                
                // 更新前の値を取得
                List<SrSpsprintGra> srSpsprintGraList = getSrSpsprintGraData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo8, edaban);
                if (!srSpsprintGraList.isEmpty()) {
                    tmpSrSpsprintGra = srSpsprintGraList.get(0);
                }
                
                deleteTmpSrSpsprintGra(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);
                deleteTmpSubSrSpsprintGra(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);
                deleteTmpSrMwiplotlink(queryRunnerQcdb, conQcdb, kojyo, lotNo8, edaban);
            }

            // 印刷SPSｸﾞﾗﾋﾞｱ_登録処理
            insertSrSpsprintGra(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList(), tmpSrSpsprintGra);

            // 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面登録処理
            insertSubSrSpsprintGra(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, systemTime);

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
        processData.setUserAuthParam(GXHDO101B001Const.USER_AUTH_UPDATE_PARAM);

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

            // 印刷SPSｸﾞﾗﾋﾞｱ_更新処理
            updateSrSpsprintGra(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

            // 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面更新処理
            updateSubSrSpsprintGra(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo8, edaban, systemTime);
            
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
        processData.setUserAuthParam(GXHDO101B001Const.USER_AUTH_DELETE_PARAM);

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

            // 印刷SPSｸﾞﾗﾋﾞｱ_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo8, edaban);
            insertDeleteDataTmpSrSpsprintGra(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, systemTime);

            // 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面仮登録登録処理
            insertDeleteDataTmpSubSrSpsprintGra(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, systemTime);

            // 印刷SPSｸﾞﾗﾋﾞｱ_削除処理
            deleteSrSpsprintGra(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);

            // 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面削除処理
            deleteSubSrSpsprintGra(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);
            
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
                activeIdList.addAll(Arrays.asList(GXHDO101B001Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO101B001Const.BTN_MAKUATSU_SUBGAMEN_BOTTOM,
                        GXHDO101B001Const.BTN_PTN_KYORI_X_SUBGAMEN_BOTTOM,
                        GXHDO101B001Const.BTN_PTN_KYORI_Y_SUBGAMEN_BOTTOM,
                        GXHDO101B001Const.BTN_DELETE_BOTTOM,
                        GXHDO101B001Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B001Const.BTN_START_DATETIME_BOTTOM,
                        GXHDO101B001Const.BTN_END_DATETIME_BOTTOM,
                        GXHDO101B001Const.BTN_EDABAN_COPY_TOP,
                        GXHDO101B001Const.BTN_MAKUATSU_SUBGAMEN_TOP,
                        GXHDO101B001Const.BTN_PTN_KYORI_X_SUBGAMEN_TOP,
                        GXHDO101B001Const.BTN_PTN_KYORI_Y_SUBGAMEN_TOP,
                        GXHDO101B001Const.BTN_DELETE_TOP,
                        GXHDO101B001Const.BTN_UPDATE_TOP,
                        GXHDO101B001Const.BTN_START_DATETIME_TOP,
                        GXHDO101B001Const.BTN_END_DATETIME_TOP
                ));
                inactiveIdList.addAll(Arrays.asList(GXHDO101B001Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B001Const.BTN_INSERT_BOTTOM,
                        GXHDO101B001Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B001Const.BTN_INSERT_TOP));

                break;
            default:
                activeIdList.addAll(Arrays.asList(GXHDO101B001Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B001Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO101B001Const.BTN_MAKUATSU_SUBGAMEN_BOTTOM,
                        GXHDO101B001Const.BTN_PTN_KYORI_X_SUBGAMEN_BOTTOM,
                        GXHDO101B001Const.BTN_PTN_KYORI_Y_SUBGAMEN_BOTTOM,
                        GXHDO101B001Const.BTN_INSERT_BOTTOM,
                        GXHDO101B001Const.BTN_START_DATETIME_BOTTOM,
                        GXHDO101B001Const.BTN_END_DATETIME_BOTTOM,
                        GXHDO101B001Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B001Const.BTN_EDABAN_COPY_TOP,
                        GXHDO101B001Const.BTN_MAKUATSU_SUBGAMEN_TOP,
                        GXHDO101B001Const.BTN_PTN_KYORI_X_SUBGAMEN_TOP,
                        GXHDO101B001Const.BTN_PTN_KYORI_Y_SUBGAMEN_TOP,
                        GXHDO101B001Const.BTN_INSERT_TOP,
                        GXHDO101B001Const.BTN_START_DATETIME_TOP,
                        GXHDO101B001Const.BTN_END_DATETIME_TOP
                ));

                inactiveIdList.addAll(Arrays.asList(GXHDO101B001Const.BTN_DELETE_BOTTOM,
                        GXHDO101B001Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B001Const.BTN_DELETE_TOP,
                        GXHDO101B001Const.BTN_UPDATE_TOP));

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
            case GXHDO101B001Const.BTN_MAKUATSU_SUBGAMEN_TOP:
            case GXHDO101B001Const.BTN_MAKUATSU_SUBGAMEN_BOTTOM:
                method = "openMakuatsu";
                break;
            // PTN距離X
            case GXHDO101B001Const.BTN_PTN_KYORI_X_SUBGAMEN_TOP:
            case GXHDO101B001Const.BTN_PTN_KYORI_X_SUBGAMEN_BOTTOM:
                method = "openPtnKyoriStart";
                break;
            // PTN距離Y
            case GXHDO101B001Const.BTN_PTN_KYORI_Y_SUBGAMEN_TOP:
            case GXHDO101B001Const.BTN_PTN_KYORI_Y_SUBGAMEN_BOTTOM:
                method = "openPtnKyoriEnd";
                break;
            // 仮登録
            case GXHDO101B001Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO101B001Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO101B001Const.BTN_INSERT_TOP:
            case GXHDO101B001Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 枝番コピー
            case GXHDO101B001Const.BTN_EDABAN_COPY_TOP:
            case GXHDO101B001Const.BTN_EDABAN_COPY_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 修正
            case GXHDO101B001Const.BTN_UPDATE_TOP:
            case GXHDO101B001Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO101B001Const.BTN_DELETE_TOP:
            case GXHDO101B001Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 開始日時
            case GXHDO101B001Const.BTN_START_DATETIME_TOP:
            case GXHDO101B001Const.BTN_START_DATETIME_BOTTOM:
                method = "setKaishiDateTime";
                break;
            // 開始日時
            case GXHDO101B001Const.BTN_END_DATETIME_TOP:
            case GXHDO101B001Const.BTN_END_DATETIME_BOTTOM:
                method = "setShuryouDateTime";
                break;
            // 前工程WIP
            case GXHDO101B001Const.BTN_WIP_IMPORT_TOP:
            case GXHDO101B001Const.BTN_WIP_IMPORT_BOTTOM:
                method = "openWipImport";
                break;
            // 設備ﾃﾞｰﾀ連携
            case GXHDO101B001Const.BTN_DATACOOPERATION_TOP:
            case GXHDO101B001Const.BTN_DATACOOPERATION_BOTTOM:
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
        
        // A.用途1～8に’EA’が存在しない場合ｴﾗｰﾒｯｾｰｼﾞを表示し、処理を続行する。
        // YOUTOの末尾の数値と同様の項目[TLOT2]を対象項目とする。
        // ≪例≫YOUTO1であれば、[TLOT21]が対象項目となる。
        // ｲ.取得した値をG3)ｽﾗﾘｰﾛｯﾄへ表示する。
        if (StringUtil.nullToBlank(getMapData(sekkeiData, "YOUTO1")).equals("EA")) {
            setItemData(processData,GXHDO101B001Const.SLURRY_LOT, String.valueOf(StringUtil.nullToBlank(getMapData(sekkeiData, "TLOT21"))));
        } else if (StringUtil.nullToBlank(getMapData(sekkeiData, "YOUTO2")).equals("EA")) {
            setItemData(processData,GXHDO101B001Const.SLURRY_LOT, String.valueOf(StringUtil.nullToBlank(getMapData(sekkeiData, "TLOT22"))));
        } else if (StringUtil.nullToBlank(getMapData(sekkeiData, "YOUTO3")).equals("EA")) {
            setItemData(processData,GXHDO101B001Const.SLURRY_LOT, String.valueOf(StringUtil.nullToBlank(getMapData(sekkeiData, "TLOT23"))));
        } else if (StringUtil.nullToBlank(getMapData(sekkeiData, "YOUTO4")).equals("EA")) {
            setItemData(processData,GXHDO101B001Const.SLURRY_LOT, String.valueOf(StringUtil.nullToBlank(getMapData(sekkeiData, "TLOT24"))));
        } else if (StringUtil.nullToBlank(getMapData(sekkeiData, "YOUTO5")).equals("EA")) {
            setItemData(processData,GXHDO101B001Const.SLURRY_LOT, String.valueOf(StringUtil.nullToBlank(getMapData(sekkeiData, "TLOT25"))));
        } else if (StringUtil.nullToBlank(getMapData(sekkeiData, "YOUTO6")).equals("EA")) {
            setItemData(processData,GXHDO101B001Const.SLURRY_LOT, String.valueOf(StringUtil.nullToBlank(getMapData(sekkeiData, "TLOT26"))));
        } else if (StringUtil.nullToBlank(getMapData(sekkeiData, "YOUTO7")).equals("EA")) {
            setItemData(processData,GXHDO101B001Const.SLURRY_LOT, String.valueOf(StringUtil.nullToBlank(getMapData(sekkeiData, "TLOT27"))));
        } else if (StringUtil.nullToBlank(getMapData(sekkeiData, "YOUTO8")).equals("EA")) {
            setItemData(processData,GXHDO101B001Const.SLURRY_LOT, String.valueOf(StringUtil.nullToBlank(getMapData(sekkeiData, "TLOT28"))));
        } else {
            errorMessageList.add(MessageUtil.getMessage("XHD-000104"));
        }
        
        // ③[条件]情報の表示(取得した値は入力項目欄に設定する(label))
        // 1.Ⅲ.画面表示仕様(24)を発行する。
        Map daJokenDenkyokuData =  loadDaJokenData(queryRunnerQcdb, String.valueOf(StringUtil.nullToBlank(getMapData(sekkeiData, "SEKKEINO"))), "設計仕様", "電極", "ﾍﾟｰｽﾄ");
        if (daJokenDenkyokuData == null || daJokenDenkyokuData.isEmpty()) {
            errorMessageList.clear();
            errorMessageList.add(MessageUtil.getMessage("XHD-000014"));
            processData.setFatalError(true);
            processData.setInitMessageList(errorMessageList);
            return processData;
        }
        
         // 電極ﾍﾟｰｽﾄ(設計)
        this.setItemData(processData, GXHDO101B001Const.DENKYOKU_PASTE_SEKKEI, String.valueOf(getMapData(daJokenDenkyokuData, "KIKAKUCHI")));
        
        // 1.Ⅲ.画面表示仕様(24)を発行する。
        Map daJokenKokeibunData =  loadDaJokenData(queryRunnerQcdb, String.valueOf(StringUtil.nullToBlank(getMapData(sekkeiData, "SEKKEINO"))), "設計仕様", "電極", "ﾍﾟｰｽﾄ固形分");
        if (daJokenKokeibunData == null || daJokenKokeibunData.isEmpty()) {
            errorMessageList.clear();
            errorMessageList.add(MessageUtil.getMessage("XHD-000014"));
            processData.setFatalError(true);
            processData.setInitMessageList(errorMessageList);
            return processData;
        }
        
         // ﾍﾟｰｽﾄ固形分
        this.setItemData(processData, GXHDO101B001Const.PASTE_KOKEI_BUN, String.valueOf(getMapData(daJokenKokeibunData, "KIKAKUCHI")));
        
        // 条件情報チェック(対象のデータが取得出来ていない場合エラー)
        errorMessageList.addAll(ValidateUtil.checkSekkeiUnsetItems(daJokenDenkyokuData, getMapJokenAssociation()));

        // 条件情報チェック(対象のデータが取得出来ていない場合エラー)
        errorMessageList.addAll(ValidateUtil.checkSekkeiUnsetItems(daJokenKokeibunData, getMapJokenAssociation()));
        
        // ④PETﾌｨﾙﾑ種類、厚みリスト作成
        // 1.Ⅲ.画面表示仕様(24)を発行する。
        Map daJokenPetData =  loadDaJokenData(queryRunnerQcdb, String.valueOf(StringUtil.nullToBlank(getMapData(sekkeiData, "SEKKEINO"))), "設計仕様", "PET厚み", "PET厚み");
        if (daJokenPetData == null || daJokenPetData.isEmpty()) {
            errorMessageList.clear();
            errorMessageList.add(MessageUtil.getMessage("XHD-000014"));
            processData.setFatalError(true);
            processData.setInitMessageList(errorMessageList);
            return processData;
        }

        // 条件情報チェック(対象のデータが取得出来ていない場合エラー)
        errorMessageList.addAll(ValidateUtil.checkSekkeiUnsetItems(daJokenPetData, getMapJokenAssociation()));
        
        // 2.厚み/PET種類ﾘｽﾄ作成
        // スラッシュが含まれているかをチェック;
        if (String.valueOf(StringUtil.nullToBlank(getMapData(daJokenPetData, "KIKAKUCHI"))).contains("/")) {
            // A.1.で取得したPET種類を"/"で分けられる場合
            // ｱ.以下のﾘｽﾄを作成し、1行目の厚みにⅢ.画面表示仕様(2).電極厚み[EATUMI]を入れる。
            // 結果を格納するリスト
            List<List<String>> petList = new ArrayList<>();

            // 文字列をスラッシュで分割
            String[] parts = StringUtil.nullToBlank(getMapData(daJokenPetData, "KIKAKUCHI")).split("/");

            // 分割された各部分でリストを作成し、結果に追加
            for (int i = 0; i < parts.length; i++) {
                List<String> innerList = new ArrayList<>();
                // 初回時だけ電極厚みを設定
                if(i ==0){
                    // 部分文字列を追加
                    innerList.add(parts[i]); 
                    // 電極厚みを追加
                    innerList.add(StringUtil.nullToBlank(getMapData(sekkeiData, "EATUMI")));   
                } else {
                    // 部分文字列を追加
                    innerList.add(parts[i]);
                    // 空の文字列を追加 
                    innerList.add("");
                }
                // 結果のリストに追加
                petList.add(innerList);
            }
            
            // 3.厚みの取得
            // A.Ⅲ.画面表示仕様(25)を発行する。
            String param = loadParamData(queryRunnerDoc, "common_user", "印刷グラビア・厚みPETﾌｨﾙﾑ");
            if (param == null || param.isEmpty()) {
                errorMessageList.clear();
                errorMessageList.add(MessageUtil.getMessage("XHD-000232"));
                processData.setFatalError(true);
                processData.setInitMessageList(errorMessageList);
                return processData;
            }
            
            
            String[] groups = param.split("\\),\\(");
        
            List<List<String>> petMappingList = new ArrayList<>();
        
            for (String group : groups) {
                // グループ内の括弧を取り除く
                group = group.replace("(", "").replace(")", "");
                // コンマで分割してリストを作成
                String[] elements = group.split(",");
                List<String> itemList = new ArrayList<>(Arrays.asList(elements));
                petMappingList.add(itemList);
            }
            
            // 一致した結果を格納するList
            String atumisa = "";
            
            // 一致するデータを取り出すためのループ
            for (List<String> bSubList : petMappingList) {
                if (bSubList.size() > 1 && petList.size() > 0) {
                    // 一致条件の確認
                    if (bSubList.get(0).equals(petList.get(0).get(0)) && bSubList.get(1).equals(petList.get(1).get(0))) {
                        atumisa = bSubList.get(2);
                   }
                }
            }
            
            // 厚み差が取得できなかった場合
            if(StringUtil.nullToBlank(atumisa).isEmpty()){
                errorMessageList.clear();
                errorMessageList.add(MessageUtil.getMessage("XHD-000232"));
                processData.setFatalError(true);
                processData.setInitMessageList(errorMessageList);
                return processData;
            }
            
            petList.get(1).set(1,atumisa);
            
            // G3).厚み/PET種類①設定用文字列
            String atumi_pet1 = petList.get(0).get(1) + "μｍ/" + petList.get(0).get(0);
            
            // G3).厚み/PET種類②設定用文字列
            BigDecimal atumi = BigDecimal.ZERO;
            if(StringUtil.isEmpty(StringUtil.nullToBlank(getMapData(sekkeiData, "EATUMI")))){
                atumi = new BigDecimal(petList.get(1).get(1));
            } else{
                atumi = new BigDecimal(getMapData(sekkeiData, "EATUMI").toString()).add(new BigDecimal(petList.get(1).get(1)));
            }
            String atumi_pet2 = atumi.toString() + "μｍ/" + petList.get(1).get(0);
            
            // G3).厚み/PET種類①
            this.setItemData(processData, GXHDO101B001Const.ATSUMI_PET1, atumi_pet1);
            
            // G3).厚み/PET種類②
            this.setItemData(processData, GXHDO101B001Const.ATSUMI_PET2, atumi_pet2);

        } else {
            // B.1.で取得したPET種類に"/"が含まれない場合
            // ｱ.以下のﾘｽﾄを作成し、1行目の厚みにⅢ.画面表示仕様(2).電極厚み[EATUMI]を入れる。
            List<String> petList = new ArrayList<>();
            petList.add(StringUtil.nullToBlank(getMapData(daJokenPetData, "KIKAKUCHI")));
            petList.add(StringUtil.nullToBlank(getMapData(sekkeiData, "EATUMI")));
            
            // G3).厚み/PET種類①設定用文字列
            String atumi_pet1 = petList.get(1) + "μｍ/" + petList.get(0);
            
            // G3).厚み/PET種類①
            this.setItemData(processData, GXHDO101B001Const.ATSUMI_PET1, atumi_pet1);
            
            // G3).厚み/PET種類②
            this.setItemData(processData, GXHDO101B001Const.ATSUMI_PET2, "");
        }

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
     * @param shikakariData 仕掛データ
     * @param lotNo ﾛｯﾄNo
     */
    private void setViewItemData(ProcessData processData, Map sekkeiData, Map lotKbnMasData, Map ownerMasData, Map shikakariData, String lotNo) {

        // ロットNo
        this.setItemData(processData, GXHDO101B001Const.LOTNO, lotNo);
        // KCPNO
        this.setItemData(processData, GXHDO101B001Const.KCPNO, StringUtil.nullToBlank(getMapData(shikakariData, "kcpno")));
        // セット数
        String suuryo = StringUtil.nullToBlank(getMapData(shikakariData, "suuryo"));
        String torikosuu = StringUtil.nullToBlank(getMapData(shikakariData, "torikosuu"));
        if (StringUtil.isEmpty(suuryo) || "0".equals(suuryo) || StringUtil.isEmpty(torikosuu) || "0".equals(torikosuu)) {
            this.setItemData(processData, GXHDO101B001Const.SET_SUU, "0");
        } else {
            BigDecimal decHasseisu = new BigDecimal(suuryo);
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
            String owner = StringUtil.nullToBlank(getMapData(ownerMasData, "ownername"));
            this.setItemData(processData, GXHDO101B001Const.OWNER, ownercode + ":" + owner);
        }

        // 電極テープ
        this.setItemData(processData, GXHDO101B001Const.DENKYOKU_TAPE, StringUtil.nullToBlank(sekkeiData.get("ETAPE")));

        // 積層数
        this.setItemData(processData, GXHDO101B001Const.SEKISOU_SU, StringUtil.nullToBlank(sekkeiData.get("SOUSUU"))
                + "層  "
                + StringUtil.nullToBlank(sekkeiData.get("EMAISUU"))
                + "枚");

        // 電極製版名
        this.setItemData(processData, GXHDO101B001Const.DENKYOKU_SEIHAN_MEI, StringUtil.nullToBlank(sekkeiData.get("PATTERN")));

        // 版胴名
//        this.setItemData(processData, GXHDO101B001Const.SEIHAN_OR_HANDOU_MEI, StringUtil.nullToBlank(sekkeiData.get("PATTERN")));

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

        List<SrSpsprintGra> srSpsprintGraDataList = new ArrayList<>();
        List<SubSrSpsprintGra> subSrSpsprintGraDataList = new ArrayList<>();
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
                    if (setSanshouMotoLotData(processData, queryRunnerQcdb, queryRunnerDoc, formId, motoLotNo, kojyo, lotNo8, edaban)) {
                        //ｾｯﾄ成功時はリターン
                        return true;
                    }
                }
                
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
                setInputItemDataSubFormC020(queryRunnerQcdb, kojyo, lotNo8, edaban, jotaiFlg);

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
        
        // 前工程WIP取込画面データ設定
        setInputItemDataSubFormC020(queryRunnerQcdb, kojyo, lotNo8, edaban, jotaiFlg);

        return true;

    }

    /**
     * メイン画面データ設定処理
     *
     * @param processData 処理制御データ
     * @param srSpsprintGraData 印刷SPSｸﾞﾗﾋﾞｱデータ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrSpsprintGra srSpsprintGraData) {
        // ｽﾘｯﾌﾟﾛｯﾄNo
        this.setItemData(processData, GXHDO101B001Const.SLIP_LOTNO, getSrSpsprintGraItemData(GXHDO101B001Const.SLIP_LOTNO, srSpsprintGraData));
        // ﾛｰﾙNo1
        this.setItemData(processData, GXHDO101B001Const.ROLL_NO1, getSrSpsprintGraItemData(GXHDO101B001Const.ROLL_NO1, srSpsprintGraData));
        // ﾛｰﾙNo2
        this.setItemData(processData, GXHDO101B001Const.ROLL_NO2, getSrSpsprintGraItemData(GXHDO101B001Const.ROLL_NO2, srSpsprintGraData));
        // ﾛｰﾙNo3
        this.setItemData(processData, GXHDO101B001Const.ROLL_NO3, getSrSpsprintGraItemData(GXHDO101B001Const.ROLL_NO3, srSpsprintGraData));
        // 厚みNo1
        this.setItemData(processData, GXHDO101B001Const.ATSUMI_NO1, getSrSpsprintGraItemData(GXHDO101B001Const.ATSUMI_NO1, srSpsprintGraData));
        // 厚みNo2
        this.setItemData(processData, GXHDO101B001Const.ATSUMI_NO2, getSrSpsprintGraItemData(GXHDO101B001Const.ATSUMI_NO2, srSpsprintGraData));
        // 厚みNo3
        this.setItemData(processData, GXHDO101B001Const.ATSUMI_NO3, getSrSpsprintGraItemData(GXHDO101B001Const.ATSUMI_NO3, srSpsprintGraData));
        // 原料記号
        this.setItemData(processData, GXHDO101B001Const.GENRYO_KIGOU, getSrSpsprintGraItemData(GXHDO101B001Const.GENRYO_KIGOU, srSpsprintGraData));
        // ﾍﾟｰｽﾄﾛｯﾄNo1
        this.setItemData(processData, GXHDO101B001Const.PASTE_LOT_NO1, getSrSpsprintGraItemData(GXHDO101B001Const.PASTE_LOT_NO1, srSpsprintGraData));
        // 電極ﾍﾟｰｽﾄ
        this.setItemData(processData, GXHDO101B001Const.DENKYOKU_PASTE, getSrSpsprintGraItemData(GXHDO101B001Const.DENKYOKU_PASTE, srSpsprintGraData));
        // ﾍﾟｰｽﾄ粘度1
        this.setItemData(processData, GXHDO101B001Const.PASTE_NENDO1, getSrSpsprintGraItemData(GXHDO101B001Const.PASTE_NENDO1, srSpsprintGraData));
        // ﾍﾟｰｽﾄ温度1
        this.setItemData(processData, GXHDO101B001Const.PASTE_ONDO1, getSrSpsprintGraItemData(GXHDO101B001Const.PASTE_ONDO1, srSpsprintGraData));
        // ﾍﾟｰｽﾄ固形分1
        this.setItemData(processData, GXHDO101B001Const.PASTE_KOKEIBUN1, getSrSpsprintGraItemData(GXHDO101B001Const.PASTE_KOKEIBUN1, srSpsprintGraData));
        // ﾍﾟｰｽﾄﾛｯﾄNo2
        this.setItemData(processData, GXHDO101B001Const.PASTE_LOT_NO2, getSrSpsprintGraItemData(GXHDO101B001Const.PASTE_LOT_NO2, srSpsprintGraData));
        // ﾍﾟｰｽﾄ粘度2
        this.setItemData(processData, GXHDO101B001Const.PASTE_NENDO2, getSrSpsprintGraItemData(GXHDO101B001Const.PASTE_NENDO2, srSpsprintGraData));
        // ﾍﾟｰｽﾄ温度2
        this.setItemData(processData, GXHDO101B001Const.PASTE_ONDO2, getSrSpsprintGraItemData(GXHDO101B001Const.PASTE_ONDO2, srSpsprintGraData));
        // ﾍﾟｰｽﾄ固形分2
        this.setItemData(processData, GXHDO101B001Const.PASTE_KOKEIBUN2, getSrSpsprintGraItemData(GXHDO101B001Const.PASTE_KOKEIBUN2, srSpsprintGraData));
        // ﾍﾟｰｽﾄﾛｯﾄNo3
        this.setItemData(processData, GXHDO101B001Const.PASTE_LOT_NO3, getSrSpsprintGraItemData(GXHDO101B001Const.PASTE_LOT_NO3, srSpsprintGraData));
        // ﾍﾟｰｽﾄ粘度3
        this.setItemData(processData, GXHDO101B001Const.PASTE_NENDO3, getSrSpsprintGraItemData(GXHDO101B001Const.PASTE_NENDO3, srSpsprintGraData));
        // ﾍﾟｰｽﾄ温度3
        this.setItemData(processData, GXHDO101B001Const.PASTE_ONDO3, getSrSpsprintGraItemData(GXHDO101B001Const.PASTE_ONDO3, srSpsprintGraData));
        // ﾍﾟｰｽﾄ固形分3
        this.setItemData(processData, GXHDO101B001Const.PASTE_KOKEIBUN3, getSrSpsprintGraItemData(GXHDO101B001Const.PASTE_KOKEIBUN3, srSpsprintGraData));
        // ﾍﾟｰｽﾄﾛｯﾄNo4
        this.setItemData(processData, GXHDO101B001Const.PASTE_LOT_NO4, getSrSpsprintGraItemData(GXHDO101B001Const.PASTE_LOT_NO4, srSpsprintGraData));
        // ﾍﾟｰｽﾄ粘度4
        this.setItemData(processData, GXHDO101B001Const.PASTE_NENDO4, getSrSpsprintGraItemData(GXHDO101B001Const.PASTE_NENDO4, srSpsprintGraData));
        // ﾍﾟｰｽﾄ温度4
        this.setItemData(processData, GXHDO101B001Const.PASTE_ONDO4, getSrSpsprintGraItemData(GXHDO101B001Const.PASTE_ONDO4, srSpsprintGraData));
        // ﾍﾟｰｽﾄ固形分4
        this.setItemData(processData, GXHDO101B001Const.PASTE_KOKEIBUN4, getSrSpsprintGraItemData(GXHDO101B001Const.PASTE_KOKEIBUN4, srSpsprintGraData));
        // ﾍﾟｰｽﾄﾛｯﾄNo5
        this.setItemData(processData, GXHDO101B001Const.PASTE_LOT_NO5, getSrSpsprintGraItemData(GXHDO101B001Const.PASTE_LOT_NO5, srSpsprintGraData));
        // ﾍﾟｰｽﾄ粘度5
        this.setItemData(processData, GXHDO101B001Const.PASTE_NENDO5, getSrSpsprintGraItemData(GXHDO101B001Const.PASTE_NENDO5, srSpsprintGraData));
        // ﾍﾟｰｽﾄ温度5
        this.setItemData(processData, GXHDO101B001Const.PASTE_ONDO5, getSrSpsprintGraItemData(GXHDO101B001Const.PASTE_ONDO5, srSpsprintGraData));
        // ﾍﾟｰｽﾄ固形分5
        this.setItemData(processData, GXHDO101B001Const.PASTE_KOKEIBUN5, getSrSpsprintGraItemData(GXHDO101B001Const.PASTE_KOKEIBUN5, srSpsprintGraData));
        // PETﾌｨﾙﾑ種類No1
        this.setItemData(processData, GXHDO101B001Const.PET_FILM_NO1, getSrSpsprintGraItemData(GXHDO101B001Const.PET_FILM_NO1, srSpsprintGraData));
        // PETﾌｨﾙﾑ種類No2
        this.setItemData(processData, GXHDO101B001Const.PET_FILM_NO2, getSrSpsprintGraItemData(GXHDO101B001Const.PET_FILM_NO2, srSpsprintGraData));
        // PETﾌｨﾙﾑ種類No3
        this.setItemData(processData, GXHDO101B001Const.PET_FILM_NO3, getSrSpsprintGraItemData(GXHDO101B001Const.PET_FILM_NO3, srSpsprintGraData));
        // 印刷号機
        this.setItemData(processData, GXHDO101B001Const.INSATSU_GOUKI, getSrSpsprintGraItemData(GXHDO101B001Const.INSATSU_GOUKI, srSpsprintGraData));
        // 乾燥温度表示値1
        this.setItemData(processData, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI1, getSrSpsprintGraItemData(GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI1, srSpsprintGraData));
        // 乾燥温度表示値2
        this.setItemData(processData, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI2, getSrSpsprintGraItemData(GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI2, srSpsprintGraData));
        // 乾燥温度表示値3
        this.setItemData(processData, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI3, getSrSpsprintGraItemData(GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI3, srSpsprintGraData));
        // 乾燥温度表示値4
        this.setItemData(processData, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI4, getSrSpsprintGraItemData(GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI4, srSpsprintGraData));
        // 乾燥温度表示値1
        this.setItemData(processData, GXHDO101B001Const.KANSOU_ONDO_SHITA_HYOUJICHI1, getSrSpsprintGraItemData(GXHDO101B001Const.KANSOU_ONDO_SHITA_HYOUJICHI1, srSpsprintGraData));
        // 乾燥温度表示値2
        this.setItemData(processData, GXHDO101B001Const.KANSOU_ONDO_SHITA_HYOUJICHI2, getSrSpsprintGraItemData(GXHDO101B001Const.KANSOU_ONDO_SHITA_HYOUJICHI2, srSpsprintGraData));
        // 乾燥温度表示値3
        this.setItemData(processData, GXHDO101B001Const.KANSOU_ONDO_SHITA_HYOUJICHI3, getSrSpsprintGraItemData(GXHDO101B001Const.KANSOU_ONDO_SHITA_HYOUJICHI3, srSpsprintGraData));
        // 乾燥温度表示値4
        this.setItemData(processData, GXHDO101B001Const.KANSOU_ONDO_SHITA_HYOUJICHI4, getSrSpsprintGraItemData(GXHDO101B001Const.KANSOU_ONDO_SHITA_HYOUJICHI4, srSpsprintGraData));
        // 搬送速度
        this.setItemData(processData, GXHDO101B001Const.HANSOU_SOKUDO, getSrSpsprintGraItemData(GXHDO101B001Const.HANSOU_SOKUDO, srSpsprintGraData));
        // 開始テンション計
        this.setItemData(processData, GXHDO101B001Const.KAISHI_TENSION_KEI, getSrSpsprintGraItemData(GXHDO101B001Const.KAISHI_TENSION_KEI, srSpsprintGraData));
        // 開始テンション前
        this.setItemData(processData, GXHDO101B001Const.KAISHI_TENSION_MAE, getSrSpsprintGraItemData(GXHDO101B001Const.KAISHI_TENSION_MAE, srSpsprintGraData));
        // 開始テンション奥
        this.setItemData(processData, GXHDO101B001Const.KAISHI_TENSION_OKU, getSrSpsprintGraItemData(GXHDO101B001Const.KAISHI_TENSION_OKU, srSpsprintGraData));
        // 終了テンション計
        this.setItemData(processData, GXHDO101B001Const.SHURYOU_TENSION_KEI, getSrSpsprintGraItemData(GXHDO101B001Const.SHURYOU_TENSION_KEI, srSpsprintGraData));
        // 終了テンション前
        this.setItemData(processData, GXHDO101B001Const.SHURYOU_TENSION_MAE, getSrSpsprintGraItemData(GXHDO101B001Const.SHURYOU_TENSION_MAE, srSpsprintGraData));
        // 終了テンション奥
        this.setItemData(processData, GXHDO101B001Const.SHURYOU_TENSION_OKU, getSrSpsprintGraItemData(GXHDO101B001Const.SHURYOU_TENSION_OKU, srSpsprintGraData));
        // 圧胴圧力
        this.setItemData(processData, GXHDO101B001Const.ATSUDOU_ATSURYOKU, getSrSpsprintGraItemData(GXHDO101B001Const.ATSUDOU_ATSURYOKU, srSpsprintGraData));
        // ブレード圧力
        this.setItemData(processData, GXHDO101B001Const.BLADE_ATSURYOKU, getSrSpsprintGraItemData(GXHDO101B001Const.BLADE_ATSURYOKU, srSpsprintGraData));
        // 製版名 / 版胴名
        this.setItemData(processData, GXHDO101B001Const.SEIHAN_OR_HANDOU_MEI, getSrSpsprintGraItemData(GXHDO101B001Const.SEIHAN_OR_HANDOU_MEI, srSpsprintGraData));
        // 製版No / 版胴No
        this.setItemData(processData, GXHDO101B001Const.SEIHAN_OR_HANDOU_NO, getSrSpsprintGraItemData(GXHDO101B001Const.SEIHAN_OR_HANDOU_NO, srSpsprintGraData));
        // 製版使用枚数/版胴使用枚数
        this.setItemData(processData, GXHDO101B001Const.SEIHAN_OR_HANDOU_SHIYOU_MAISUU, getSrSpsprintGraItemData(GXHDO101B001Const.SEIHAN_OR_HANDOU_SHIYOU_MAISUU, srSpsprintGraData));
        // ｽｷｰｼﾞNo/圧胴No
        this.setItemData(processData, GXHDO101B001Const.SQUEEGEE_OR_ATSUDOU_NO, getSrSpsprintGraItemData(GXHDO101B001Const.SQUEEGEE_OR_ATSUDOU_NO, srSpsprintGraData));
        // 圧胴使用枚数
        this.setItemData(processData, GXHDO101B001Const.ATSUDOU_SIYOU_MAISUU, getSrSpsprintGraItemData(GXHDO101B001Const.ATSUDOU_SIYOU_MAISUU, srSpsprintGraData));
        // ブレードNo.
        this.setItemData(processData, GXHDO101B001Const.BLADE_NO, getSrSpsprintGraItemData(GXHDO101B001Const.BLADE_NO, srSpsprintGraData));
        // ブレード外観
        this.setItemData(processData, GXHDO101B001Const.BLADE_GAIKAN, getSrSpsprintGraItemData(GXHDO101B001Const.BLADE_GAIKAN, srSpsprintGraData));
        // ﾌﾞﾚｰﾄﾞ印刷長
        this.setItemData(processData, GXHDO101B001Const.BLADEINSATSUTYO, getSrSpsprintGraItemData(GXHDO101B001Const.BLADEINSATSUTYO, srSpsprintGraData));
        // 印刷開始日
        this.setItemData(processData, GXHDO101B001Const.INSATSU_KAISHI_DAY, getSrSpsprintGraItemData(GXHDO101B001Const.INSATSU_KAISHI_DAY, srSpsprintGraData));
        // 印刷開始時間
        this.setItemData(processData, GXHDO101B001Const.INSATSU_KAISHI_TIME, getSrSpsprintGraItemData(GXHDO101B001Const.INSATSU_KAISHI_TIME, srSpsprintGraData));
        // 印刷ｽﾀｰﾄ膜厚AVE
        this.setItemData(processData, GXHDO101B001Const.INSATSU_START_MAKUATSU_AVE, getSrSpsprintGraItemData(GXHDO101B001Const.INSATSU_START_MAKUATSU_AVE, srSpsprintGraData));
        // 印刷ｽﾀｰﾄ膜厚MAX
        this.setItemData(processData, GXHDO101B001Const.INSATSU_START_MAKUATSU_MAX, getSrSpsprintGraItemData(GXHDO101B001Const.INSATSU_START_MAKUATSU_MAX, srSpsprintGraData));
        // 印刷ｽﾀｰﾄ膜厚MIN
        this.setItemData(processData, GXHDO101B001Const.INSATSU_START_MAKUATSU_MIN, getSrSpsprintGraItemData(GXHDO101B001Const.INSATSU_START_MAKUATSU_MIN, srSpsprintGraData));
        // 印刷ｽﾀｰﾄ膜厚CV
        this.setItemData(processData, GXHDO101B001Const.INSATSU_START_MAKUATSU_CV, getSrSpsprintGraItemData(GXHDO101B001Const.INSATSU_START_MAKUATSU_CV, srSpsprintGraData));
        // PTN間距離印刷ｽﾀｰﾄ X Min
        this.setItemData(processData, GXHDO101B001Const.PTN_INSATSU_START_X_MIN, getSrSpsprintGraItemData(GXHDO101B001Const.PTN_INSATSU_START_X_MIN, srSpsprintGraData));
        // PTN間距離印刷ｽﾀｰﾄ Y Min
        this.setItemData(processData, GXHDO101B001Const.PTN_INSATSU_START_Y_MIN, getSrSpsprintGraItemData(GXHDO101B001Const.PTN_INSATSU_START_Y_MIN, srSpsprintGraData));
        // ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
        this.setItemData(processData, GXHDO101B001Const.STARTJI_NIJIMI_KASURE_CHECK, getSrSpsprintGraItemData(GXHDO101B001Const.STARTJI_NIJIMI_KASURE_CHECK, srSpsprintGraData));
        // 印刷スタート時担当者
        this.setItemData(processData, GXHDO101B001Const.INSATSU_STARTJI_TANTOUSHA, getSrSpsprintGraItemData(GXHDO101B001Const.INSATSU_STARTJI_TANTOUSHA, srSpsprintGraData));
        // 印刷スタート時確認者
        this.setItemData(processData, GXHDO101B001Const.INSATSU_STARTJI_KAKUNINSYA, getSrSpsprintGraItemData(GXHDO101B001Const.INSATSU_STARTJI_KAKUNINSYA, srSpsprintGraData));
        // 印刷終了日
        this.setItemData(processData, GXHDO101B001Const.INSATSU_SHUURYOU_DAY, getSrSpsprintGraItemData(GXHDO101B001Const.INSATSU_SHUURYOU_DAY, srSpsprintGraData));
        // 印刷終了時刻
        this.setItemData(processData, GXHDO101B001Const.INSATSU_SHUURYOU_TIME, getSrSpsprintGraItemData(GXHDO101B001Const.INSATSU_SHUURYOU_TIME, srSpsprintGraData));
        // 印刷ｴﾝﾄﾞ膜厚AVE
        this.setItemData(processData, GXHDO101B001Const.INSATSU_END_MAKUATSU_AVE, getSrSpsprintGraItemData(GXHDO101B001Const.INSATSU_END_MAKUATSU_AVE, srSpsprintGraData));
        // 印刷ｴﾝﾄﾞ膜厚MAX
        this.setItemData(processData, GXHDO101B001Const.INSATSU_END_MAKUATSU_MAX, getSrSpsprintGraItemData(GXHDO101B001Const.INSATSU_END_MAKUATSU_MAX, srSpsprintGraData));
        // 印刷ｴﾝﾄﾞ膜厚MIN
        this.setItemData(processData, GXHDO101B001Const.INSATSU_END_MAKUATSU_MIN, getSrSpsprintGraItemData(GXHDO101B001Const.INSATSU_END_MAKUATSU_MIN, srSpsprintGraData));
        // 印刷ｴﾝﾄﾞ膜厚CV
        this.setItemData(processData, GXHDO101B001Const.INSATSU_END_MAKUATSU_CV, getSrSpsprintGraItemData(GXHDO101B001Const.INSATSU_END_MAKUATSU_CV, srSpsprintGraData));
        // PTN間距離印刷ｴﾝﾄﾞ X Min
        this.setItemData(processData, GXHDO101B001Const.PTN_INSATSU_END_X_MIN, getSrSpsprintGraItemData(GXHDO101B001Const.PTN_INSATSU_END_X_MIN, srSpsprintGraData));
        // PTN間距離印刷ｴﾝﾄﾞ Y Min
        this.setItemData(processData, GXHDO101B001Const.PTN_INSATSU_END_Y_MIN, getSrSpsprintGraItemData(GXHDO101B001Const.PTN_INSATSU_END_Y_MIN, srSpsprintGraData));
        // 終了時ﾆｼﾞﾐ・ｶｽﾚ確認
        this.setItemData(processData, GXHDO101B001Const.SHUURYOU_JI_NIJIMI_KASURE_CHECK, getSrSpsprintGraItemData(GXHDO101B001Const.SHUURYOU_JI_NIJIMI_KASURE_CHECK, srSpsprintGraData));
        // 印刷エンド時担当者
        this.setItemData(processData, GXHDO101B001Const.INSATSU_ENDJI_TANTOUSHA, getSrSpsprintGraItemData(GXHDO101B001Const.INSATSU_ENDJI_TANTOUSHA, srSpsprintGraData));
        // 印刷枚数
        this.setItemData(processData, GXHDO101B001Const.INSATSU_MAISUU, getSrSpsprintGraItemData(GXHDO101B001Const.INSATSU_MAISUU, srSpsprintGraData));
        // 清掃 ローラ部
        this.setItemData(processData, GXHDO101B001Const.SEISOUROLLERBU, getSrSpsprintGraItemData(GXHDO101B001Const.SEISOUROLLERBU, srSpsprintGraData));
        // 清掃 印刷周辺
        this.setItemData(processData, GXHDO101B001Const.SEISOUINSATSUSYUHEN, getSrSpsprintGraItemData(GXHDO101B001Const.SEISOUINSATSUSYUHEN, srSpsprintGraData));
        // 清掃 乾燥炉内
        this.setItemData(processData, GXHDO101B001Const.SEISOUKANSOURONAI, getSrSpsprintGraItemData(GXHDO101B001Const.SEISOUKANSOURONAI, srSpsprintGraData));
        // 3μｍフィルター 適用
        this.setItemData(processData, GXHDO101B001Const.SANMICRONMFILTERTEKIYOU, getSrSpsprintGraItemData(GXHDO101B001Const.SANMICRONMFILTERTEKIYOU, srSpsprintGraData));
        // 3μｍフィルター 交換
        this.setItemData(processData, GXHDO101B001Const.SANMICRONMFILTERKOUKAN, getSrSpsprintGraItemData(GXHDO101B001Const.SANMICRONMFILTERKOUKAN, srSpsprintGraData));
        // インクパンストッパーロック確認
        this.setItemData(processData, GXHDO101B001Const.INKPANSTOPPERLOCKKAKUNIN, getSrSpsprintGraItemData(GXHDO101B001Const.INKPANSTOPPERLOCKKAKUNIN, srSpsprintGraData));
        // 先行ﾛｯﾄNo
        this.setItemData(processData, GXHDO101B001Const.SENKOULOTNO, getSrSpsprintGraItemData(GXHDO101B001Const.SENKOULOTNO, srSpsprintGraData));
        // ﾃｰﾌﾟ使い切り
        this.setItemData(processData, GXHDO101B001Const.TAPETSUKAIKIRI, getSrSpsprintGraItemData(GXHDO101B001Const.TAPETSUKAIKIRI, srSpsprintGraData));
        // 次ﾛｯﾄへ
        this.setItemData(processData, GXHDO101B001Const.JILOTHE, getSrSpsprintGraItemData(GXHDO101B001Const.JILOTHE, srSpsprintGraData));
        // 成形長さ
        this.setItemData(processData, GXHDO101B001Const.SEIKEINAGASA, getSrSpsprintGraItemData(GXHDO101B001Const.SEIKEINAGASA, srSpsprintGraData));
        // 歩留まり
        this.setItemData(processData, GXHDO101B001Const.BUDOMARI, getSrSpsprintGraItemData(GXHDO101B001Const.BUDOMARI, srSpsprintGraData));
        //備考1
        this.setItemData(processData, GXHDO101B001Const.BIKOU1, getSrSpsprintGraItemData(GXHDO101B001Const.BIKOU1, srSpsprintGraData));
        //備考2
        this.setItemData(processData, GXHDO101B001Const.BIKOU2, getSrSpsprintGraItemData(GXHDO101B001Const.BIKOU2, srSpsprintGraData));
        //備考3
        this.setItemData(processData, GXHDO101B001Const.BIKOU3, getSrSpsprintGraItemData(GXHDO101B001Const.BIKOU3, srSpsprintGraData));
        //備考4
        this.setItemData(processData, GXHDO101B001Const.BIKOU4, getSrSpsprintGraItemData(GXHDO101B001Const.BIKOU4, srSpsprintGraData));
        //備考5
        this.setItemData(processData, GXHDO101B001Const.BIKOU5, getSrSpsprintGraItemData(GXHDO101B001Const.BIKOU5, srSpsprintGraData));
        //印刷長さ
        this.setItemData(processData, GXHDO101B001Const.PRINTLENGTH, getSrSpsprintGraItemData(GXHDO101B001Const.PRINTLENGTH, srSpsprintGraData));
        //印刷ｾｯﾄ数
        this.setItemData(processData, GXHDO101B001Const.PRINT_SETSU, getSrSpsprintGraItemData(GXHDO101B001Const.PRINT_SETSU, srSpsprintGraData));
    }

    /**
     * 膜厚入力画面データ設定処理
     *
     * @param subSrSpsprintGraData 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面データ
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

        GXHDO101C001Model model;
        if (subSrSpsprintGraData == null) {
            // 登録データが無い場合空の状態で初期値をセットする。
            makuatsuStart = new String[]{"", "", "", "", "", "", "", "", ""}; //膜厚スタート1～9
            makuatsuEnd = new String[]{"", "", "", "", "", "", "", "", ""}; //膜厚エンド1～9
            model = GXHDO101C001Logic.createGXHDO101C001Model(makuatsuStart, makuatsuEnd);

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
            model = GXHDO101C001Logic.createGXHDO101C001Model(makuatsuStart, makuatsuEnd);
        }

        // サブ画面から戻ったときに値を設定する項目を指定する。
        model.setReturnItemIdStartAve(GXHDO101B001Const.INSATSU_START_MAKUATSU_AVE);
        model.setReturnItemIdStartMax(GXHDO101B001Const.INSATSU_START_MAKUATSU_MAX);
        model.setReturnItemIdStartMin(GXHDO101B001Const.INSATSU_START_MAKUATSU_MIN);
        model.setReturnItemIdStartCv(GXHDO101B001Const.INSATSU_START_MAKUATSU_CV);
        model.setReturnItemIdEndAve(GXHDO101B001Const.INSATSU_END_MAKUATSU_AVE);
        model.setReturnItemIdEndMax(GXHDO101B001Const.INSATSU_END_MAKUATSU_MAX);
        model.setReturnItemIdEndMin(GXHDO101B001Const.INSATSU_END_MAKUATSU_MIN);
        model.setReturnItemIdEndCv(GXHDO101B001Const.INSATSU_END_MAKUATSU_CV);
        beanGXHDO101C001.setGxhdO101c001Model(model);
    }

    /**
     * PTN距離X入力画面データ設定処理
     *
     * @param subSrSpsprintGraData 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面データ
     */
    private void setInputItemDataSubFormC002(SubSrSpsprintGra subSrSpsprintGraData) {

        // PTN距離Xサブ画面初期表示データ設定
        GXHDO101C002 beanGXHDO101C002 = (GXHDO101C002) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C002);
        //データの設定
        String[] startPtnDistX;
        String[] startPtnDistY;
        GXHDO101C002Model model;
        if (subSrSpsprintGraData == null) {
            startPtnDistX = new String[]{"", "", "", "", ""}; //PTN距離XStart
            startPtnDistY = new String[]{"", "", "", "", ""}; //PTN距離YStart

            model = GXHDO101C002Logic.createGXHDO101C002Model(startPtnDistX, startPtnDistY);

        } else {
            //PTN距離Xスタート1～5
            startPtnDistX = new String[]{
                StringUtil.nullToBlank(subSrSpsprintGraData.getStartPtnDistX1()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getStartPtnDistX2()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getStartPtnDistX3()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getStartPtnDistX4()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getStartPtnDistX5())};
            //PTN距離Yスタート1～5
            startPtnDistY = new String[]{
                StringUtil.nullToBlank(subSrSpsprintGraData.getStartPtnDistY1()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getStartPtnDistY2()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getStartPtnDistY3()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getStartPtnDistY4()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getStartPtnDistY5())};

            model = GXHDO101C002Logic.createGXHDO101C002Model(startPtnDistX, startPtnDistY);

        }
        model.setReturnItemIdStartXMin(GXHDO101B001Const.PTN_INSATSU_START_X_MIN);
        model.setReturnItemIdStartYMin(GXHDO101B001Const.PTN_INSATSU_START_Y_MIN);
        beanGXHDO101C002.setGxhdO101c002Model(model);
    }

    /**
     * PTN距離Y入力画面データ設定処理
     *
     * @param subSrSpsprintGraData 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面データ
     */
    private void setInputItemDataSubFormC003(SubSrSpsprintGra subSrSpsprintGraData) {

        // PTN距離Yサブ画面初期表示データ設定
        GXHDO101C003 beanGXHDO101C003 = (GXHDO101C003) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C003);
        //データの設定
        String[] endPtnDistX;
        String[] endPtnDistY;
        GXHDO101C003Model model;
        if (subSrSpsprintGraData == null) {
            endPtnDistX = new String[]{"", "", "", "", ""}; //PTN距離XEnd
            endPtnDistY = new String[]{"", "", "", "", ""}; //PTN距離YEnd
            model = GXHDO101C003Logic.createGXHDO101C003Model(endPtnDistX, endPtnDistY);

        } else {
            //PTN距離Xエンド1～5
            endPtnDistX = new String[]{
                StringUtil.nullToBlank(subSrSpsprintGraData.getEndPtnDistX1()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getEndPtnDistX2()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getEndPtnDistX3()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getEndPtnDistX4()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getEndPtnDistX5())};
            //PTN距離Yエンド1～5
            endPtnDistY = new String[]{
                StringUtil.nullToBlank(subSrSpsprintGraData.getEndPtnDistY1()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getEndPtnDistY2()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getEndPtnDistY3()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getEndPtnDistY4()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getEndPtnDistY5())};
            model = GXHDO101C003Logic.createGXHDO101C003Model(endPtnDistX, endPtnDistY);

        }
        model.setReturnItemIdEndXMin(GXHDO101B001Const.PTN_INSATSU_END_X_MIN);
        model.setReturnItemIdEndYMin(GXHDO101B001Const.PTN_INSATSU_END_Y_MIN);
        beanGXHDO101C003.setGxhdO101c003Model(model);
    }

    /**
     * 印刷SPSｸﾞﾗﾋﾞｱの入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return 印刷SPSｸﾞﾗﾋﾞｱ登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrSpsprintGra> getSrSpsprintGraData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {

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
    private List<SubSrSpsprintGra> getSubSrSpsprintGraData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {
        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSubSrSpsprintGra(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSubSrSpsprintGra(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
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
                put("GENRYOU", "原料");
                put("ETAPE", "電極ﾃｰﾌﾟ");
                put("EATUMI", "電極厚み");
                put("SOUSUU", "総数");
                put("EMAISUU", "電極枚数");
                put("PATTERN", "電極製版名");
            }
        };

        return map;
    }
    
    /**
     * 条件データ関連付けマップ取得
     *
     * @return 設計データ関連付けマップ
     */
    private Map getMapJokenAssociation() {
        Map<String, String> map = new LinkedHashMap<String, String>() {
            {
                put("KIKAKUCHI", "KIKAKUCHI");
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
     * [印刷SPSｸﾞﾗﾋﾞｱ]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrSpsprintGra> loadSrSpsprintGra(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT kojyo,lotno,edaban,tapelotno,petfilmsyurui,taperollno1,"
                + "taperollno2,taperollno3,pastelotno,pastenendo,pasteondo,"
                + "pkokeibun1,pastelotno2,pastenendo2,pasteondo2,pkokeibun2,"
                + "handoumei,handouno,handoumaisuu,bladeno,bladegaikan,BladeATu,"
                + "bladeinsatsutyo,AtudoNo,AtudoMaisuu,AtuDoATu,gouki,kansouondo,kansouondo2,"
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
                + "abzure_heikin_end,genryoukigou,bikou1,bikou2,"
                + "torokunichiji,kosinnichiji,revision,kcpno,'0' AS deleteflag,kakuninsya, "
                + "seisourollerbu,seisouinsatsusyuhen,seisoukansouronai,3micronmfiltertekiyou sanmicronmfiltertekiyou,"
                + "3micronmfilterkoukan sanmicronmfilterkoukan,inkpanstopperlockkakunin,skojyo,slotno,sedaban,tapetsukaikiri,"
                + "jilothe,seikeinagasa,bikou3,bikou4,bikou5,printlength,kansouondoshita,kansouondoshita2,kansouondoshita3,kansouondoshita4,"
                + "atsumi1,atsumi2,atsumi3,petfilmsyurui2,petfilmsyurui3,pastehinmei,"
                + "pastelotno3,pastenendo3,pasteondo3,pkokeibun3,"
                + "pastelotno4,pastenendo4,pasteondo4,pkokeibun4,"
                + "pastelotno5,pastenendo5,pasteondo5,pkokeibun5,"
                + "insatusetsuu,budomari "
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
        mapping.put("handoumei", "handoumei"); //版胴名
        mapping.put("handouno", "handouno"); //版胴No
        mapping.put("handoumaisuu", "handoumaisuu"); //版胴使用枚数
        mapping.put("bladeno", "bladeno"); //ﾌﾞﾚｰﾄﾞNo.
        mapping.put("bladegaikan", "bladegaikan"); //ﾌﾞﾚｰﾄﾞ外観
        mapping.put("BladeATu", "bladeATu"); //ﾌﾞﾚｰﾄﾞ圧力
        mapping.put("bladeinsatsutyo", "bladeinsatsutyo"); //ﾌﾞﾚｰﾄﾞ印刷長
        mapping.put("AtudoNo", "atudoNo"); //圧胴No
        mapping.put("AtudoMaisuu", "atudoMaisuu"); //圧胴使用枚数
        mapping.put("AtuDoATu", "atuDoATu"); //圧胴圧力
        mapping.put("gouki", "gouki"); //号機ｺｰﾄﾞ
        mapping.put("kansouondo", "kansouondo"); //乾燥温度
        mapping.put("kansouondo2", "kansouondo2"); //乾燥温度2
        mapping.put("kansouondo3", "kansouondo3"); //乾燥温度3
        mapping.put("kansouondo4", "kansouondo4"); //乾燥温度4
        mapping.put("kansouondoshita", "kansouondoshita"); //乾燥温度下側
        mapping.put("kansouondoshita2", "kansouondoshita2"); //乾燥温度下側2
        mapping.put("kansouondoshita3", "kansouondoshita3"); //乾燥温度下側3
        mapping.put("kansouondoshita4", "kansouondoshita4"); //乾燥温度下側4
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
        mapping.put("genryoukigou", "genryoukigou"); //原料記号
        mapping.put("bikou1", "bikou1"); //備考1
        mapping.put("bikou2", "bikou2"); //備考2
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); //更新日時
        mapping.put("revision", "revision"); //revision
        mapping.put("kcpno", "kcpno"); //KCPNO
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ
        mapping.put("kakuninsya", "kakuninsya"); //印刷スタート時確認者
        mapping.put("seisourollerbu", "seisourollerbu"); //清掃 ローラ部
        mapping.put("seisouinsatsusyuhen", "seisouinsatsusyuhen"); //清掃 印刷周辺
        mapping.put("seisoukansouronai", "seisoukansouronai"); //清掃 乾燥炉内
        mapping.put("sanmicronmfiltertekiyou", "sanmicronmfiltertekiyou"); //3μｍフィルター 適用
        mapping.put("sanmicronmfilterkoukan", "sanmicronmfilterkoukan"); //3μｍフィルター 交換
        mapping.put("inkpanstopperlockkakunin", "inkpanstopperlockkakunin"); //インクパンストッパーロック確認
        mapping.put("skojyo", "skojyo"); //先行工場ｺｰﾄﾞ
        mapping.put("slotno", "slotno"); //先行ﾛｯﾄNo
        mapping.put("sedaban", "sedaban"); //先行枝番
        mapping.put("tapetsukaikiri", "tapetsukaikiri"); //ﾃｰﾌﾟ使い切り
        mapping.put("jilothe", "jilothe"); //次ﾛｯﾄへ
        mapping.put("seikeinagasa", "seikeinagasa"); //成形長さ
        mapping.put("bikou3", "bikou3"); //備考3
        mapping.put("bikou4", "bikou4"); //備考4
        mapping.put("bikou5", "bikou5"); //備考5
        mapping.put("printlength", "printlength"); //印刷長さ
        mapping.put("atsumi1", "atsumi1"); //厚みNo1
        mapping.put("atsumi2", "atsumi2"); //厚みNo2
        mapping.put("atsumi3", "atsumi3"); //厚みNo3
        mapping.put("petfilmsyurui2", "petfilmsyurui2"); //PETﾌｨﾙﾑ種類No2
        mapping.put("petfilmsyurui3", "petfilmsyurui3"); //PETﾌｨﾙﾑ種類No3
        mapping.put("pastehinmei", "pastehinmei"); //電極ﾍﾟｰｽﾄ
        mapping.put("pastelotno3", "pastelotno3"); //ﾍﾟｰｽﾄﾛｯﾄNo3
        mapping.put("pastenendo3", "pastenendo3"); //ﾍﾟｰｽﾄ粘度3
        mapping.put("pasteondo3", "pasteondo3"); //ﾍﾟｰｽﾄ温度3
        mapping.put("pkokeibun3", "pkokeibun3"); //ﾍﾟｰｽﾄ固形分3
        mapping.put("pastelotno4", "pastelotno4"); //ﾍﾟｰｽﾄﾛｯﾄNo4
        mapping.put("pastenendo4", "pastenendo4"); //ﾍﾟｰｽﾄ粘度4
        mapping.put("pasteondo4", "pasteondo4"); //ﾍﾟｰｽﾄ温度4
        mapping.put("pkokeibun4", "pkokeibun4"); //ﾍﾟｰｽﾄ固形分4
        mapping.put("pastelotno5", "pastelotno5"); //ﾍﾟｰｽﾄﾛｯﾄNo5
        mapping.put("pastenendo5", "pastenendo5"); //ﾍﾟｰｽﾄ粘度5
        mapping.put("pasteondo5", "pasteondo5"); //ﾍﾟｰｽﾄ温度5
        mapping.put("pkokeibun5", "pkokeibun5"); //ﾍﾟｰｽﾄ固形分5
        mapping.put("insatusetsuu", "insatusetsuu"); //印刷ｾｯﾄ数
        mapping.put("budomari", "budomari"); //歩留まり

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrSpsprintGra>> beanHandler = new BeanListHandler<>(SrSpsprintGra.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrSpsprintGra> loadSubSrSpsprintGra(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

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
     * [印刷SPSｸﾞﾗﾋﾞｱ_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrSpsprintGra> loadTmpSrSpsprintGra(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,tapelotno,petfilmsyurui,taperollno1,"
                + "taperollno2,taperollno3,pastelotno,pastenendo,pasteondo,"
                + "pkokeibun1,pastelotno2,pastenendo2,pasteondo2,pkokeibun2,"
                + "handoumei,handouno,handoumaisuu,bladeno,bladegaikan,BladeATu,"
                + "bladeinsatsutyo,AtudoNo,AtudoMaisuu,AtuDoATu,gouki,kansouondo,kansouondo2,"
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
                + "abzure_heikin_end,genryoukigou,bikou1,bikou2,"
                + "torokunichiji,kosinnichiji,revision,kcpno,deleteflag,kakuninsya, "
                + "seisourollerbu,seisouinsatsusyuhen,seisoukansouronai,3micronmfiltertekiyou sanmicronmfiltertekiyou,"
                + "3micronmfilterkoukan sanmicronmfilterkoukan,inkpanstopperlockkakunin,skojyo,slotno,sedaban,tapetsukaikiri,"
                + "jilothe,seikeinagasa,bikou3,bikou4,bikou5,printlength,kansouondoshita,kansouondoshita2,kansouondoshita3,kansouondoshita4,"
                + "atsumi1,atsumi2,atsumi3,petfilmsyurui2,petfilmsyurui3,pastehinmei,"
                + "pastelotno3,pastenendo3,pasteondo3,pkokeibun3,"
                + "pastelotno4,pastenendo4,pasteondo4,pkokeibun4,"
                + "pastelotno5,pastenendo5,pasteondo5,pkokeibun5,"
                + "insatusetsuu,budomari "
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
        mapping.put("handoumei", "handoumei"); //版胴名
        mapping.put("handouno", "handouno"); //版胴No
        mapping.put("handoumaisuu", "handoumaisuu"); //版胴使用枚数
        mapping.put("bladeno", "bladeno"); //ﾌﾞﾚｰﾄﾞNo.
        mapping.put("bladegaikan", "bladegaikan"); //ﾌﾞﾚｰﾄﾞ外観
        mapping.put("BladeATu", "bladeATu"); //ﾌﾞﾚｰﾄﾞ圧力
        mapping.put("AtudoNo", "atudoNo"); //圧胴No
        mapping.put("bladeinsatsutyo", "bladeinsatsutyo"); //ﾌﾞﾚｰﾄﾞ印刷長
        mapping.put("AtudoMaisuu", "atudoMaisuu"); //圧胴使用枚数
        mapping.put("AtuDoATu", "atuDoATu"); //圧胴圧力
        mapping.put("gouki", "gouki"); //号機ｺｰﾄﾞ
        mapping.put("kansouondo", "kansouondo"); //乾燥温度
        mapping.put("kansouondo2", "kansouondo2"); //乾燥温度2
        mapping.put("kansouondo3", "kansouondo3"); //乾燥温度3
        mapping.put("kansouondo4", "kansouondo4"); //乾燥温度4
        mapping.put("kansouondoshita", "kansouondoshita"); //乾燥温度下側
        mapping.put("kansouondoshita2", "kansouondoshita2"); //乾燥温度下側2
        mapping.put("kansouondoshita3", "kansouondoshita3"); //乾燥温度下側3
        mapping.put("kansouondoshita4", "kansouondoshita4"); //乾燥温度下側4
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
        mapping.put("genryoukigou", "genryoukigou"); //原料記号
        mapping.put("bikou1", "bikou1"); //備考1
        mapping.put("bikou2", "bikou2"); //備考2
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); //更新日時
        mapping.put("revision", "revision"); //revision
        mapping.put("kcpno", "kcpno"); //KCPNO
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ
        mapping.put("kakuninsya", "kakuninsya"); //印刷スタート時確認者
        mapping.put("seisourollerbu", "seisourollerbu"); //清掃 ローラ部
        mapping.put("seisouinsatsusyuhen", "seisouinsatsusyuhen"); //清掃 印刷周辺
        mapping.put("seisoukansouronai", "seisoukansouronai"); //清掃 乾燥炉内
        mapping.put("sanmicronmfiltertekiyou", "sanmicronmfiltertekiyou"); //3μｍフィルター 適用
        mapping.put("sanmicronmfilterkoukan", "sanmicronmfilterkoukan"); //3μｍフィルター 交換
        mapping.put("inkpanstopperlockkakunin", "inkpanstopperlockkakunin"); //インクパンストッパーロック確認
        mapping.put("skojyo", "skojyo"); //先行工場ｺｰﾄﾞ
        mapping.put("slotno", "slotno"); //先行ﾛｯﾄNo
        mapping.put("sedaban", "sedaban"); //先行枝番
        mapping.put("tapetsukaikiri", "tapetsukaikiri"); //ﾃｰﾌﾟ使い切り
        mapping.put("jilothe", "jilothe"); //次ﾛｯﾄへ
        mapping.put("seikeinagasa", "seikeinagasa"); //成形長さ
        mapping.put("bikou3", "bikou3"); //備考3
        mapping.put("bikou4", "bikou4"); //備考4
        mapping.put("bikou5", "bikou5"); //備考5
        mapping.put("atsumi1", "atsumi1"); //厚みNo1
        mapping.put("atsumi2", "atsumi2"); //厚みNo2
        mapping.put("atsumi3", "atsumi3"); //厚みNo3
        mapping.put("petfilmsyurui2", "petfilmsyurui2"); //PETﾌｨﾙﾑ種類No2
        mapping.put("petfilmsyurui3", "petfilmsyurui3"); //PETﾌｨﾙﾑ種類No3
        mapping.put("pastehinmei", "pastehinmei"); //電極ﾍﾟｰｽﾄ
        mapping.put("pastelotno3", "pastelotno3"); //ﾍﾟｰｽﾄﾛｯﾄNo3
        mapping.put("pastenendo3", "pastenendo3"); //ﾍﾟｰｽﾄ粘度3
        mapping.put("pasteondo3", "pasteondo3"); //ﾍﾟｰｽﾄ温度3
        mapping.put("pkokeibun3", "pkokeibun3"); //ﾍﾟｰｽﾄ固形分3
        mapping.put("pastelotno4", "pastelotno4"); //ﾍﾟｰｽﾄﾛｯﾄNo4
        mapping.put("pastenendo4", "pastenendo4"); //ﾍﾟｰｽﾄ粘度4
        mapping.put("pasteondo4", "pasteondo4"); //ﾍﾟｰｽﾄ温度4
        mapping.put("pkokeibun4", "pkokeibun4"); //ﾍﾟｰｽﾄ固形分4
        mapping.put("pastelotno5", "pastelotno5"); //ﾍﾟｰｽﾄﾛｯﾄNo5
        mapping.put("pastenendo5", "pastenendo5"); //ﾍﾟｰｽﾄ粘度5
        mapping.put("pasteondo5", "pasteondo5"); //ﾍﾟｰｽﾄ温度5
        mapping.put("pkokeibun5", "pkokeibun5"); //ﾍﾟｰｽﾄ固形分5
        mapping.put("insatusetsuu", "insatusetsuu"); //印刷ｾｯﾄ数
        mapping.put("budomari", "budomari"); //歩留まり
        
        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrSpsprintGra>> beanHandler = new BeanListHandler<>(SrSpsprintGra.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrSpsprintGra> loadTmpSubSrSpsprintGra(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

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

            // 膜厚入力画面データ設定
            // ※工場ｺｰﾄﾞ、ﾛｯﾄNo、枝番は親ではなく自身の値を渡す。
            //   →子画面側処理で自身の枝番を保持しておく必要がある。データ自体は親データの枝番で検索済みのものを引き渡す。
            setInputItemDataSubFormC001(subSrSpsprintGraDataList.get(0), kojyo, lotNo8, edaban);

            // PTN距離X入力画面データ設定
            setInputItemDataSubFormC002(subSrSpsprintGraDataList.get(0));

            // PTN距離Y入力画面データ設定
            setInputItemDataSubFormC003(subSrSpsprintGraDataList.get(0));
            
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
     * @param srSpsprintGraData 印刷SPSｸﾞﾗﾋﾞｱ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrSpsprintGra srSpsprintGraData) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srSpsprintGraData != null) {
            // 元データが存在する場合元データより取得
            return getSrSpsprintGraItemData(itemId, srSpsprintGraData);
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
     * 印刷SPSｸﾞﾗﾋﾞｱ_仮登録(tmp_sr_spsprint_gra)登録処理
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
    private void insertTmpSrSpsprintGra(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {

        String sql = "INSERT INTO tmp_sr_spsprint_gra ("
                + "kojyo,lotno,edaban,tapelotno,petfilmsyurui,taperollno1,taperollno2,taperollno3,"
                + "pastelotno,pastenendo,pasteondo,pkokeibun1,pastelotno2,pastenendo2,pasteondo2,"
                + "pkokeibun2,handoumei,handouno,handoumaisuu,bladeno,bladegaikan,BladeATu,bladeinsatsutyo,AtudoNo,"
                + "AtudoMaisuu,AtuDoATu,gouki,kansouondo,kansouondo2,kansouondo3,kansouondo4,"
                + "hansouspeed,startdatetime,tantousya,makuatuave_start,makuatumax_start,"
                + "makuatumin_start,makuatucv_start,nijimikasure_start,start_ptn_dist_x,start_ptn_dist_y,"
                + "TensionS_sum,TensionStemae,TensionSoku,enddatetime,tanto_end,printmaisuu,"
                + "makuatuave_end,makuatumax_end,makuatumin_end,makuatucv_end,nijimikasure_end,end_ptn_dist_x,"
                + "end_ptn_dist_y,TensionE_sum,TensionEtemae,TensionEoku,printzure1_surihajime_start,"
                + "printzure2_center_start,printzure3_suriowari_start,abzure_heikin_start,"
                + "printzure1_surihajime_end,printzure2_center_end,printzure3_suriowari_end,"
                + "abzure_heikin_end,genryoukigou,bikou1,bikou2,torokunichiji,kosinnichiji,"
                + "revision,kcpno,kakuninsya,seisourollerbu,seisouinsatsusyuhen,seisoukansouronai,3micronmfiltertekiyou,"
                + "3micronmfilterkoukan,inkpanstopperlockkakunin,skojyo,slotno,sedaban,tapetsukaikiri,jilothe,seikeinagasa,"
                + "bikou3,bikou4,bikou5,deleteflag,printlength,kansouondoshita,kansouondoshita2,kansouondoshita3,kansouondoshita4,"
                + "atsumi1,atsumi2,atsumi3,petfilmsyurui2,petfilmsyurui3,pastehinmei,"
                + "pastelotno3,pastenendo3,pasteondo3,pkokeibun3,"
                + "pastelotno4,pastenendo4,pasteondo4,pkokeibun4,"
                + "pastelotno5,pastenendo5,pasteondo5,pkokeibun5,"
                + "insatusetsuu,budomari"
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrSpsprintGra(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, itemList, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷SPSｸﾞﾗﾋﾞｱ_仮登録(tmp_sr_spsprint_gra)更新処理
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
    private void updateTmpSrSpsprintGra(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {

        String sql = "UPDATE tmp_sr_spsprint_gra SET "
                + "tapelotno = ?,petfilmsyurui = ?,taperollno1 = ?,taperollno2 = ?,taperollno3 = ?,"
                + "pastelotno = ?,pastenendo = ?,pasteondo = ?,pkokeibun1 = ?,pastelotno2 = ?,pastenendo2 = ?,pasteondo2 = ?,"
                + "pkokeibun2 = ?,handoumei = ?,handouno = ?,handoumaisuu = ?,bladeno = ?,bladegaikan = ?,BladeATu = ?,bladeinsatsutyo=?,AtudoNo = ?,"
                + "AtudoMaisuu = ?,AtuDoATu = ?,gouki = ?,kansouondo = ?,kansouondo2 = ?,kansouondo3 = ?,kansouondo4 = ?,"
                + "hansouspeed = ?,startdatetime = ?,tantousya = ?,makuatuave_start = ?,makuatumax_start = ?,"
                + "makuatumin_start = ?,makuatucv_start = ?,nijimikasure_start = ?,start_ptn_dist_x = ?,start_ptn_dist_y = ?,"
                + "TensionS_sum = ?,TensionStemae = ?,TensionSoku = ?,enddatetime = ?,tanto_end = ?,printmaisuu = ?,"
                + "makuatuave_end = ?,makuatumax_end = ?,makuatumin_end = ?,makuatucv_end = ?,nijimikasure_end = ?,end_ptn_dist_x = ?,"
                + "end_ptn_dist_y = ?,TensionE_sum = ?,TensionEtemae = ?,TensionEoku = ?,genryoukigou = ?,bikou1 = ?,bikou2 = ?,"
                + "kosinnichiji = ?,revision = ?,kcpno = ?,kakuninsya = ?,seisourollerbu = ?,seisouinsatsusyuhen = ?,seisoukansouronai = ?,"
                + "3micronmfiltertekiyou = ?,3micronmfilterkoukan = ?,inkpanstopperlockkakunin = ?,skojyo = ?,slotno = ?,sedaban = ?,tapetsukaikiri = ?,"
                + "jilothe = ?,seikeinagasa = ?,bikou3 = ?,bikou4 = ?,bikou5 = ?,deleteflag = ?,printlength = ?,kansouondoshita = ?,kansouondoshita2 = ?,kansouondoshita3 = ?,kansouondoshita4 = ?,"
                + "atsumi1 = ?,atsumi2 = ?,atsumi3 = ?,petfilmsyurui2 = ?,petfilmsyurui3 = ?,pastehinmei = ?,"
                + "pastelotno3 = ?,pastenendo3 = ?,pasteondo3 = ?,pkokeibun3 = ?,"
                + "pastelotno4 = ?,pastenendo4 = ?,pasteondo4 = ?,pkokeibun4 = ?,"
                + "pastelotno5 = ?,pastenendo5 = ?,pasteondo5 = ?,pkokeibun5 = ?,"
                + "insatusetsuu = ?,budomari = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrSpsprintGra> srSpsprintGraList = getSrSpsprintGraData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrSpsprintGra srSpsprintGra = null;
        if (!srSpsprintGraList.isEmpty()) {
            srSpsprintGra = srSpsprintGraList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrSpsprintGra(false, newRev, 0, "", "", "", systemTime, itemList, srSpsprintGra);

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
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrSpsprintGra(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

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
     * 印刷SPSｸﾞﾗﾋﾞｱ_仮登録(tmp_sr_spsprint_gra)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srSpsprintGraData 印刷SPSｸﾞﾗﾋﾞｱデータ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrSpsprintGra(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList, SrSpsprintGra srSpsprintGraData) {
        List<Object> params = new ArrayList<>();
        String senkoulotnoVal = getItemData(itemList, GXHDO101B001Const.SENKOULOTNO, srSpsprintGraData);
        String skojyoVal = StringUtils.substring(senkoulotnoVal, 0, 3);
        String slotnoVal = StringUtils.substring(senkoulotnoVal, 3, 11);
        String sedabanVal = StringUtils.substring(senkoulotnoVal, 11, 14);
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.SLIP_LOTNO, srSpsprintGraData))); //ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.PET_FILM_NO1, srSpsprintGraData))); //PETﾌｨﾙﾑ種類
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.ROLL_NO1, srSpsprintGraData))); //ﾃｰﾌﾟﾛｰﾙNo1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.ROLL_NO2, srSpsprintGraData))); //ﾃｰﾌﾟﾛｰﾙNo2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.ROLL_NO3, srSpsprintGraData))); //ﾃｰﾌﾟﾛｰﾙNo3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.PASTE_LOT_NO1, srSpsprintGraData))); //ﾍﾟｰｽﾄﾛｯﾄNo1
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.PASTE_NENDO1, srSpsprintGraData))); //ﾍﾟｰｽﾄ粘度1
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.PASTE_ONDO1, srSpsprintGraData))); //ﾍﾟｰｽﾄ温度1
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.PASTE_KOKEIBUN1, srSpsprintGraData))); //ﾍﾟｰｽﾄ固形分1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.PASTE_LOT_NO2, srSpsprintGraData))); //ﾍﾟｰｽﾄﾛｯﾄNo2
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.PASTE_NENDO2, srSpsprintGraData))); //ﾍﾟｰｽﾄ粘度2
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.PASTE_ONDO2, srSpsprintGraData))); //ﾍﾟｰｽﾄ温度2
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.PASTE_KOKEIBUN2, srSpsprintGraData))); //ﾍﾟｰｽﾄ固形分2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.SEIHAN_OR_HANDOU_MEI, srSpsprintGraData))); //版胴名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.SEIHAN_OR_HANDOU_NO, srSpsprintGraData))); //版胴No
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.SEIHAN_OR_HANDOU_SHIYOU_MAISUU, srSpsprintGraData))); //版胴使用枚数 
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.BLADE_NO, srSpsprintGraData))); //ﾌﾞﾚｰﾄﾞNo.
        //ﾌﾞﾚｰﾄﾞ外観
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B001Const.BLADE_GAIKAN, srSpsprintGraData))) {
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
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.BLADE_ATSURYOKU, srSpsprintGraData))); //ﾌﾞﾚｰﾄﾞ圧力
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.BLADEINSATSUTYO, srSpsprintGraData))); //ﾌﾞﾚｰﾄﾞ印刷長
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.SQUEEGEE_OR_ATSUDOU_NO, srSpsprintGraData))); //圧胴No
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.ATSUDOU_SIYOU_MAISUU, srSpsprintGraData))); //圧胴使用枚数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.ATSUDOU_ATSURYOKU, srSpsprintGraData))); //圧胴圧力
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.INSATSU_GOUKI, srSpsprintGraData))); //号機ｺｰﾄﾞ
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI1, srSpsprintGraData))); //乾燥温度
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI2, srSpsprintGraData))); //乾燥温度2
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI3, srSpsprintGraData))); //乾燥温度3
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI4, srSpsprintGraData))); //乾燥温度4
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.HANSOU_SOKUDO, srSpsprintGraData))); //搬送速度
        params.add(DBUtil.stringToDateObjectDefaultNull(
                getItemData(itemList, GXHDO101B001Const.INSATSU_KAISHI_DAY, srSpsprintGraData),
                getItemData(itemList, GXHDO101B001Const.INSATSU_KAISHI_TIME, srSpsprintGraData))); //ﾌﾟﾘﾝﾄ開始日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.INSATSU_STARTJI_TANTOUSHA, srSpsprintGraData))); //ｾｯﾃｨﾝｸﾞ担当者ｺｰﾄﾞ
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.INSATSU_START_MAKUATSU_AVE, srSpsprintGraData))); //ｽﾀｰﾄ時膜厚AVE
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.INSATSU_START_MAKUATSU_MAX, srSpsprintGraData))); //ｽﾀｰﾄ時膜厚MAX
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.INSATSU_START_MAKUATSU_MIN, srSpsprintGraData))); //ｽﾀｰﾄ時膜厚MIN
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.INSATSU_START_MAKUATSU_CV, srSpsprintGraData))); //印刷ｽﾀｰﾄ膜厚CV
        //ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B001Const.STARTJI_NIJIMI_KASURE_CHECK, srSpsprintGraData))) {
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
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.PTN_INSATSU_START_X_MIN, srSpsprintGraData))); //ｽﾀｰﾄ時PTN間距離X
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.PTN_INSATSU_START_Y_MIN, srSpsprintGraData))); //ｽﾀｰﾄ時PTN間距離Y
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.KAISHI_TENSION_KEI, srSpsprintGraData))); //開始ﾃﾝｼｮﾝ計
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.KAISHI_TENSION_MAE, srSpsprintGraData))); //ﾃﾝｼｮﾝ開始手前
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.KAISHI_TENSION_OKU, srSpsprintGraData))); //ﾃﾝｼｮﾝ開始奥
        params.add(DBUtil.stringToDateObjectDefaultNull(
                getItemData(itemList, GXHDO101B001Const.INSATSU_SHUURYOU_DAY, srSpsprintGraData),
                getItemData(itemList, GXHDO101B001Const.INSATSU_SHUURYOU_TIME, srSpsprintGraData))); //ﾌﾟﾘﾝﾄ終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.INSATSU_ENDJI_TANTOUSHA, srSpsprintGraData))); //終了時担当者ｺｰﾄﾞ
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.INSATSU_MAISUU, srSpsprintGraData))); //印刷枚数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.INSATSU_END_MAKUATSU_AVE, srSpsprintGraData))); //終了時膜厚AVE
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.INSATSU_END_MAKUATSU_MAX, srSpsprintGraData))); //終了時膜厚MAX
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.INSATSU_END_MAKUATSU_MIN, srSpsprintGraData))); //終了時膜厚MIN
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.INSATSU_END_MAKUATSU_CV, srSpsprintGraData))); //印刷ｴﾝﾄﾞ膜厚CV
        //終了時ﾆｼﾞﾐ・ｶｽﾚ確認
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B001Const.SHUURYOU_JI_NIJIMI_KASURE_CHECK, srSpsprintGraData))) {
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
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.PTN_INSATSU_END_X_MIN, srSpsprintGraData))); //終了時PTN間距離X
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.PTN_INSATSU_END_Y_MIN, srSpsprintGraData))); //終了時PTN間距離Y
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.SHURYOU_TENSION_KEI, srSpsprintGraData))); //終了ﾃﾝｼｮﾝ計
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.SHURYOU_TENSION_MAE, srSpsprintGraData))); //ﾃﾝｼｮﾝ終了手前
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.SHURYOU_TENSION_OKU, srSpsprintGraData))); //ﾃﾝｼｮﾝ終了奥
        if (isInsert) {
            params.add(null); //印刷ズレ①刷り始め開始
            params.add(null); //印刷ズレ②中央開始
            params.add(null); //印刷ズレ③刷り終わり開始
            params.add(null); //ABズレ平均スタート
            params.add(null); //印刷ズレ①刷り始め終了
            params.add(null); //印刷ズレ②中央終了
            params.add(null); //印刷ズレ③刷り終わり終了
            params.add(null); //ABズレ平均終了
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.GENRYO_KIGOU, srSpsprintGraData))); //原料記号
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.BIKOU1, srSpsprintGraData))); //備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.BIKOU2, srSpsprintGraData))); //備考2
        if (isInsert) {
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時
        } else {
            params.add(systemTime); //更新日時
        }
        params.add(newRev); //revision
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.KCPNO, srSpsprintGraData))); //KCPNO
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.INSATSU_STARTJI_KAKUNINSYA, srSpsprintGraData))); //印刷ｽﾀｰﾄ時確認者
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B001Const.SEISOUROLLERBU, srSpsprintGraData), null)); // 清掃 ローラ部
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B001Const.SEISOUINSATSUSYUHEN, srSpsprintGraData), null)); // 清掃 印刷周辺
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B001Const.SEISOUKANSOURONAI, srSpsprintGraData), null)); // 清掃 乾燥炉内
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B001Const.SANMICRONMFILTERTEKIYOU, srSpsprintGraData), null)); // 3μｍフィルター 適用
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B001Const.SANMICRONMFILTERKOUKAN, srSpsprintGraData), null)); // 3μｍフィルター 交換
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B001Const.INKPANSTOPPERLOCKKAKUNIN, srSpsprintGraData), null)); // インクパンストッパーロック確認
        params.add(DBUtil.stringToStringObjectDefaultNull(skojyoVal)); //先行工場ｺｰﾄﾞ
        params.add(DBUtil.stringToStringObjectDefaultNull(slotnoVal)); //先行ﾛｯﾄNo
        params.add(DBUtil.stringToStringObjectDefaultNull(sedabanVal)); //先行枝番
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B001Const.TAPETSUKAIKIRI, srSpsprintGraData), null)); // ﾃｰﾌﾟ使い切り
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B001Const.JILOTHE, srSpsprintGraData), null)); // 次ﾛｯﾄへ
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.SEIKEINAGASA, srSpsprintGraData))); //成形長さ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.BIKOU3, srSpsprintGraData))); //備考3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.BIKOU4, srSpsprintGraData))); //備考4
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.BIKOU5, srSpsprintGraData))); //備考5
        params.add(deleteflag); //削除ﾌﾗｸﾞ
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.PRINTLENGTH, srSpsprintGraData))); //印刷長さ
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.KANSOU_ONDO_SHITA_HYOUJICHI1, srSpsprintGraData))); //乾燥温度下側
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.KANSOU_ONDO_SHITA_HYOUJICHI2, srSpsprintGraData))); //乾燥温度下側2
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.KANSOU_ONDO_SHITA_HYOUJICHI3, srSpsprintGraData))); //乾燥温度下側3
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.KANSOU_ONDO_SHITA_HYOUJICHI4, srSpsprintGraData))); //乾燥温度下側4
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.ATSUMI_NO1, srSpsprintGraData))); //厚みNo1
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.ATSUMI_NO2, srSpsprintGraData))); //厚みNo2
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.ATSUMI_NO3, srSpsprintGraData))); //厚みNo3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.PET_FILM_NO2, srSpsprintGraData))); //PETﾌｨﾙﾑ種類2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.PET_FILM_NO3, srSpsprintGraData))); //PETﾌｨﾙﾑ種類3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.DENKYOKU_PASTE, srSpsprintGraData))); //電極ﾍﾟｰｽﾄ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.PASTE_LOT_NO3, srSpsprintGraData))); //ﾍﾟｰｽﾄﾛｯﾄNo3
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.PASTE_NENDO3, srSpsprintGraData))); //ﾍﾟｰｽﾄ粘度3
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.PASTE_ONDO3, srSpsprintGraData))); //ﾍﾟｰｽﾄ温度3
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.PASTE_KOKEIBUN3, srSpsprintGraData))); //ﾍﾟｰｽﾄ固形分3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.PASTE_LOT_NO4, srSpsprintGraData))); //ﾍﾟｰｽﾄﾛｯﾄNo4
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.PASTE_NENDO4, srSpsprintGraData))); //ﾍﾟｰｽﾄ粘度4
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.PASTE_ONDO4, srSpsprintGraData))); //ﾍﾟｰｽﾄ温度4
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.PASTE_KOKEIBUN4, srSpsprintGraData))); //ﾍﾟｰｽﾄ固形分4
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.PASTE_LOT_NO5, srSpsprintGraData))); //ﾍﾟｰｽﾄﾛｯﾄNo5
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.PASTE_NENDO5, srSpsprintGraData))); //ﾍﾟｰｽﾄ粘度5
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.PASTE_ONDO5, srSpsprintGraData))); //ﾍﾟｰｽﾄ温度5
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.PASTE_KOKEIBUN5, srSpsprintGraData))); //ﾍﾟｰｽﾄ固形分5
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.PRINT_SETSU, srSpsprintGraData))); //印刷ｾｯﾄ数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B001Const.BUDOMARI, srSpsprintGraData))); //歩留まり

        return params;
    }

    /**
     * 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面仮登録(tmp_sub_sr_spsprint_gra)登録処理
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
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
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
     * 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面仮登録(tmp_sub_sr_spsprint_gra)更新値パラメータ設定
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
    private List<Object> setUpdateParameterTmpSubSrSpsprintGra(boolean isInsert, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) {
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
     * 印刷SPSｸﾞﾗﾋﾞｱ(sr_spsprint_gra)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param tmpSrSpsprintGra 仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrSpsprintGra(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList, SrSpsprintGra tmpSrSpsprintGra) throws SQLException {

        String sql = "INSERT INTO sr_spsprint_gra ("
                + "kojyo,lotno,edaban,tapelotno,petfilmsyurui,taperollno1,taperollno2,taperollno3,"
                + "pastelotno,pastenendo,pasteondo,pkokeibun1,pastelotno2,pastenendo2,pasteondo2,"
                + "pkokeibun2,handoumei,handouno,handoumaisuu,bladeno,bladegaikan,BladeATu,bladeinsatsutyo,AtudoNo,"
                + "AtudoMaisuu,AtuDoATu,gouki,kansouondo,kansouondo2,kansouondo3,kansouondo4,"
                + "hansouspeed,startdatetime,tantousya,makuatuave_start,makuatumax_start,"
                + "makuatumin_start,makuatucv_start,nijimikasure_start,start_ptn_dist_x,start_ptn_dist_y,"
                + "TensionS_sum,TensionStemae,TensionSoku,enddatetime,tanto_end,printmaisuu,"
                + "makuatuave_end,makuatumax_end,makuatumin_end,makuatucv_end,nijimikasure_end,end_ptn_dist_x,"
                + "end_ptn_dist_y,TensionE_sum,TensionEtemae,TensionEoku,printzure1_surihajime_start,"
                + "printzure2_center_start,printzure3_suriowari_start,abzure_heikin_start,"
                + "printzure1_surihajime_end,printzure2_center_end,printzure3_suriowari_end,"
                + "abzure_heikin_end,genryoukigou,bikou1,bikou2,torokunichiji,kosinnichiji,"
                + "revision,kcpno,kakuninsya,seisourollerbu,seisouinsatsusyuhen,seisoukansouronai,3micronmfiltertekiyou,"
                + "3micronmfilterkoukan,inkpanstopperlockkakunin,skojyo,slotno,sedaban,tapetsukaikiri,jilothe,seikeinagasa,"
                + "bikou3,bikou4,bikou5,printlength,kansouondoshita,kansouondoshita2,kansouondoshita3,kansouondoshita4,"
                + "atsumi1,atsumi2,atsumi3,petfilmsyurui2,petfilmsyurui3,pastehinmei,"
                + "pastelotno3,pastenendo3,pasteondo3,pkokeibun3,"
                + "pastelotno4,pastenendo4,pasteondo4,pkokeibun4,"
                + "pastelotno5,pastenendo5,pasteondo5,pkokeibun5,"
                + "insatusetsuu,budomari"
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        
        List<Object> params = setUpdateParameterSrSpsprintGra(true, newRev, kojyo, lotNo, edaban, systemTime, itemList, tmpSrSpsprintGra);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷SPSｸﾞﾗﾋﾞｱ(sr_spsprint_gra)更新処理
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
    private void updateSrSpsprintGra(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {
        String sql = "UPDATE sr_spsprint_gra SET "
                + "tapelotno = ?,petfilmsyurui = ?,taperollno1 = ?,taperollno2 = ?,taperollno3 = ?,"
                + "pastelotno = ?,pastenendo = ?,pasteondo = ?,pkokeibun1 = ?,pastelotno2 = ?,pastenendo2 = ?,pasteondo2 = ?,"
                + "pkokeibun2 = ?,handoumei = ?,handouno = ?,handoumaisuu = ?,bladeno = ?,bladegaikan = ?,BladeATu = ?,bladeinsatsutyo=?,AtudoNo = ?,"
                + "AtudoMaisuu = ?,AtuDoATu = ?,gouki = ?,kansouondo = ?,kansouondo2 = ?,kansouondo3 = ?,kansouondo4 = ?,"
                + "hansouspeed = ?,startdatetime = ?,tantousya = ?,makuatuave_start = ?,makuatumax_start = ?,"
                + "makuatumin_start = ?,makuatucv_start = ?,nijimikasure_start = ?,start_ptn_dist_x = ?,start_ptn_dist_y = ?,"
                + "TensionS_sum = ?,TensionStemae = ?,TensionSoku = ?,enddatetime = ?,tanto_end = ?,printmaisuu = ?,"
                + "makuatuave_end = ?,makuatumax_end = ?,makuatumin_end = ?,makuatucv_end = ?,nijimikasure_end = ?,end_ptn_dist_x = ?,"
                + "end_ptn_dist_y = ?,TensionE_sum = ?,TensionEtemae = ?,TensionEoku = ?,genryoukigou = ?,bikou1 = ?,bikou2 = ?,"
                + "kosinnichiji = ?,revision = ?,kcpno = ? ,kakuninsya = ? ,seisourollerbu = ?,seisouinsatsusyuhen = ?,seisoukansouronai = ?,"
                + "3micronmfiltertekiyou = ?,3micronmfilterkoukan = ?,inkpanstopperlockkakunin = ?,skojyo = ?,slotno = ?,sedaban = ?,tapetsukaikiri = ?,"
                + "jilothe = ?,seikeinagasa = ?,bikou3 = ?,bikou4 = ?,bikou5 = ?,printlength = ?,kansouondoshita = ?,kansouondoshita2 = ?,kansouondoshita3 = ?,kansouondoshita4 = ?,"
                + "atsumi1 = ?, atsumi2 = ?,atsumi3 = ?,petfilmsyurui2 = ?,petfilmsyurui3 = ?,pastehinmei = ?,"
                + "pastelotno3 = ?,pastenendo3 = ?,pasteondo3 = ?,pkokeibun3 = ?,"
                + "pastelotno4 = ?,pastenendo4 = ?,pasteondo4 = ?,pkokeibun4 = ?,"
                + "pastelotno5 = ?,pastenendo5 = ?,pasteondo5 = ?,pkokeibun5 = ?,"
                + "insatusetsuu = ?,budomari = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ?";

        // 更新前の値を取得
        List<SrSpsprintGra> srSpsprintGraList = getSrSpsprintGraData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrSpsprintGra srSpsprintGra = null;
        if (!srSpsprintGraList.isEmpty()) {
            srSpsprintGra = srSpsprintGraList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrSpsprintGra(false, newRev, "", "", "", systemTime, itemList, srSpsprintGra);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷SPSｸﾞﾗﾋﾞｱ(sr_spsprint_gra)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srSpsprintGraData 印刷SPSｸﾞﾗﾋﾞｱデータ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrSpsprintGra(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            Timestamp systemTime, List<FXHDD01> itemList, SrSpsprintGra srSpsprintGraData) {
        List<Object> params = new ArrayList<>();
        String senkoulotnoVal = getItemData(itemList, GXHDO101B001Const.SENKOULOTNO, srSpsprintGraData);
        String skojyoVal = StringUtils.substring(senkoulotnoVal, 0, 3);
        String slotnoVal = StringUtils.substring(senkoulotnoVal, 3, 11);
        String sedabanVal = StringUtils.substring(senkoulotnoVal, 11, 14);
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.SLIP_LOTNO, srSpsprintGraData))); //ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.PET_FILM_NO1, srSpsprintGraData))); //PETﾌｨﾙﾑ種類
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.ROLL_NO1, srSpsprintGraData))); //ﾃｰﾌﾟﾛｰﾙNo1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.ROLL_NO2, srSpsprintGraData))); //ﾃｰﾌﾟﾛｰﾙNo2
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.ROLL_NO3, srSpsprintGraData))); //ﾃｰﾌﾟﾛｰﾙNo3
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.PASTE_LOT_NO1, srSpsprintGraData))); //ﾍﾟｰｽﾄﾛｯﾄNo1
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.PASTE_NENDO1, srSpsprintGraData))); //ﾍﾟｰｽﾄ粘度1
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.PASTE_ONDO1, srSpsprintGraData))); //ﾍﾟｰｽﾄ温度1
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.PASTE_KOKEIBUN1, srSpsprintGraData))); //ﾍﾟｰｽﾄ固形分1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.PASTE_LOT_NO2, srSpsprintGraData))); //ﾍﾟｰｽﾄﾛｯﾄNo2
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.PASTE_NENDO2, srSpsprintGraData))); //ﾍﾟｰｽﾄ粘度2
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.PASTE_ONDO2, srSpsprintGraData))); //ﾍﾟｰｽﾄ温度2
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.PASTE_KOKEIBUN2, srSpsprintGraData))); //ﾍﾟｰｽﾄ固形分2
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.SEIHAN_OR_HANDOU_MEI, srSpsprintGraData))); //版胴名
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.SEIHAN_OR_HANDOU_NO, srSpsprintGraData))); //版胴No
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.SEIHAN_OR_HANDOU_SHIYOU_MAISUU, srSpsprintGraData))); //版胴使用枚数 
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.BLADE_NO, srSpsprintGraData))); //ﾌﾞﾚｰﾄﾞNo.
        //ﾌﾞﾚｰﾄﾞ外観
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B001Const.BLADE_GAIKAN, srSpsprintGraData))) {
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
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.BLADE_ATSURYOKU, srSpsprintGraData))); //ﾌﾞﾚｰﾄﾞ圧力
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.BLADEINSATSUTYO, srSpsprintGraData))); //ﾌﾞﾚｰﾄﾞ印刷長
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.SQUEEGEE_OR_ATSUDOU_NO, srSpsprintGraData))); //圧胴No
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.ATSUDOU_SIYOU_MAISUU, srSpsprintGraData))); //圧胴使用枚数
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.ATSUDOU_ATSURYOKU, srSpsprintGraData))); //圧胴圧力
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.INSATSU_GOUKI, srSpsprintGraData))); //号機ｺｰﾄﾞ
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI1, srSpsprintGraData))); //乾燥温度
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI2, srSpsprintGraData))); //乾燥温度2
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI3, srSpsprintGraData))); //乾燥温度3
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI4, srSpsprintGraData))); //乾燥温度4
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.HANSOU_SOKUDO, srSpsprintGraData))); //搬送速度
        params.add(DBUtil.stringToDateObject(
                getItemData(itemList, GXHDO101B001Const.INSATSU_KAISHI_DAY, srSpsprintGraData),
                getItemData(itemList, GXHDO101B001Const.INSATSU_KAISHI_TIME, srSpsprintGraData))); //ﾌﾟﾘﾝﾄ開始日時
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.INSATSU_STARTJI_TANTOUSHA, srSpsprintGraData))); //ｾｯﾃｨﾝｸﾞ担当者ｺｰﾄﾞ
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.INSATSU_START_MAKUATSU_AVE, srSpsprintGraData))); //ｽﾀｰﾄ時膜厚AVE
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.INSATSU_START_MAKUATSU_MAX, srSpsprintGraData))); //ｽﾀｰﾄ時膜厚MAX
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.INSATSU_START_MAKUATSU_MIN, srSpsprintGraData))); //ｽﾀｰﾄ時膜厚MIN
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.INSATSU_START_MAKUATSU_CV, srSpsprintGraData))); //印刷ｽﾀｰﾄ膜厚CV
        //ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B001Const.STARTJI_NIJIMI_KASURE_CHECK, srSpsprintGraData))) {
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
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.PTN_INSATSU_START_X_MIN, srSpsprintGraData))); //ｽﾀｰﾄ時PTN間距離X
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.PTN_INSATSU_START_Y_MIN, srSpsprintGraData))); //ｽﾀｰﾄ時PTN間距離Y
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.KAISHI_TENSION_KEI, srSpsprintGraData))); //開始ﾃﾝｼｮﾝ計
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.KAISHI_TENSION_MAE, srSpsprintGraData))); //ﾃﾝｼｮﾝ開始手前
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.KAISHI_TENSION_OKU, srSpsprintGraData))); //ﾃﾝｼｮﾝ開始奥
        params.add(DBUtil.stringToDateObject(
                getItemData(itemList, GXHDO101B001Const.INSATSU_SHUURYOU_DAY, srSpsprintGraData),
                getItemData(itemList, GXHDO101B001Const.INSATSU_SHUURYOU_TIME, srSpsprintGraData))); //ﾌﾟﾘﾝﾄ終了日時

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.INSATSU_ENDJI_TANTOUSHA, srSpsprintGraData))); //終了時担当者ｺｰﾄﾞ
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.INSATSU_MAISUU, srSpsprintGraData))); //印刷枚数
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.INSATSU_END_MAKUATSU_AVE, srSpsprintGraData))); //終了時膜厚AVE
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.INSATSU_END_MAKUATSU_MAX, srSpsprintGraData))); //終了時膜厚MAX
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.INSATSU_END_MAKUATSU_MIN, srSpsprintGraData))); //終了時膜厚MIN
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.INSATSU_END_MAKUATSU_CV, srSpsprintGraData))); //印刷ｴﾝﾄﾞ膜厚CV
        //終了時ﾆｼﾞﾐ・ｶｽﾚ確認
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B001Const.SHUURYOU_JI_NIJIMI_KASURE_CHECK, srSpsprintGraData))) {
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

        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.PTN_INSATSU_END_X_MIN, srSpsprintGraData))); //終了時PTN間距離X
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.PTN_INSATSU_END_Y_MIN, srSpsprintGraData))); //終了時PTN間距離Y
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.SHURYOU_TENSION_KEI, srSpsprintGraData))); //終了ﾃﾝｼｮﾝ計
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.SHURYOU_TENSION_MAE, srSpsprintGraData))); //ﾃﾝｼｮﾝ終了手前
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.SHURYOU_TENSION_OKU, srSpsprintGraData))); //ﾃﾝｼｮﾝ終了奥
        if (isInsert) {
            params.add(0); //印刷ズレ①刷り始め開始
            params.add(0); //印刷ズレ②中央開始
            params.add(0); //印刷ズレ③刷り終わり開始
            params.add(0); //ABズレ平均スタート
            params.add(0); //印刷ズレ①刷り始め終了
            params.add(0); //印刷ズレ②中央終了
            params.add(0); //印刷ズレ③刷り終わり終了
            params.add(0); //ABズレ平均終了
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.GENRYO_KIGOU, srSpsprintGraData))); //原料記号
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.BIKOU1, srSpsprintGraData))); //備考1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.BIKOU2, srSpsprintGraData))); //備考2

        if (isInsert) {
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時

        } else {
            params.add(systemTime); //更新日時
        }
        params.add(newRev); //revision
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.KCPNO, srSpsprintGraData))); //KCPNO
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.INSATSU_STARTJI_KAKUNINSYA, srSpsprintGraData))); //印刷スタート時確認者
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B001Const.SEISOUROLLERBU, srSpsprintGraData), 9)); // 清掃 ローラ部
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B001Const.SEISOUINSATSUSYUHEN, srSpsprintGraData), 9)); // 清掃 印刷周辺
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B001Const.SEISOUKANSOURONAI, srSpsprintGraData), 9)); // 清掃 乾燥炉内
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B001Const.SANMICRONMFILTERTEKIYOU, srSpsprintGraData), 9)); // 3μｍフィルター 適用
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B001Const.SANMICRONMFILTERKOUKAN, srSpsprintGraData), 9)); // 3μｍフィルター 交換
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B001Const.INKPANSTOPPERLOCKKAKUNIN, srSpsprintGraData), 9)); // インクパンストッパーロック確認
        params.add(DBUtil.stringToStringObject(skojyoVal)); //先行工場ｺｰﾄﾞ
        params.add(DBUtil.stringToStringObject(slotnoVal)); //先行ﾛｯﾄNo
        params.add(DBUtil.stringToStringObject(sedabanVal)); //先行枝番
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B001Const.TAPETSUKAIKIRI, srSpsprintGraData), 9)); // ﾃｰﾌﾟ使い切り
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B001Const.JILOTHE, srSpsprintGraData), 9)); // 次ﾛｯﾄへ
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.SEIKEINAGASA, srSpsprintGraData))); //成形長さ
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.BIKOU3, srSpsprintGraData))); //備考3
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.BIKOU4, srSpsprintGraData))); //備考4
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.BIKOU5, srSpsprintGraData))); //備考5
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.PRINTLENGTH, srSpsprintGraData))); //印刷長さ
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.KANSOU_ONDO_SHITA_HYOUJICHI1, srSpsprintGraData))); //乾燥温度下側
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.KANSOU_ONDO_SHITA_HYOUJICHI2, srSpsprintGraData))); //乾燥温度下側2
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.KANSOU_ONDO_SHITA_HYOUJICHI3, srSpsprintGraData))); //乾燥温度下側3
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.KANSOU_ONDO_SHITA_HYOUJICHI4, srSpsprintGraData))); //乾燥温度下側4
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.ATSUMI_NO1, srSpsprintGraData))); //厚みNo1
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.ATSUMI_NO2, srSpsprintGraData))); //厚みNo2
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.ATSUMI_NO3, srSpsprintGraData))); //厚みNo3
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.PET_FILM_NO2, srSpsprintGraData))); //PETﾌｨﾙﾑ種類No2
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.PET_FILM_NO3, srSpsprintGraData))); //PETﾌｨﾙﾑ種類No3
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.DENKYOKU_PASTE, srSpsprintGraData))); //電極ﾍﾟｰｽﾄ
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.PASTE_LOT_NO3, srSpsprintGraData))); //ﾍﾟｰｽﾄﾛｯﾄNo3
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.PASTE_NENDO3, srSpsprintGraData))); //ﾍﾟｰｽﾄ粘度3
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.PASTE_ONDO3, srSpsprintGraData))); //ﾍﾟｰｽﾄ温度3
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.PASTE_KOKEIBUN3, srSpsprintGraData))); //ﾍﾟｰｽﾄ固形分3
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.PASTE_LOT_NO4, srSpsprintGraData))); //ﾍﾟｰｽﾄﾛｯﾄNo4
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.PASTE_NENDO4, srSpsprintGraData))); //ﾍﾟｰｽﾄ粘度4
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.PASTE_ONDO4, srSpsprintGraData))); //ﾍﾟｰｽﾄ温度4
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.PASTE_KOKEIBUN4, srSpsprintGraData))); //ﾍﾟｰｽﾄ固形分4
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.PASTE_LOT_NO5, srSpsprintGraData))); //ﾍﾟｰｽﾄﾛｯﾄNo5
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.PASTE_NENDO5, srSpsprintGraData))); //ﾍﾟｰｽﾄ粘度5
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.PASTE_ONDO5, srSpsprintGraData))); //ﾍﾟｰｽﾄ温度5
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.PASTE_KOKEIBUN5, srSpsprintGraData))); //ﾍﾟｰｽﾄ固形分5
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.PRINT_SETSU, srSpsprintGraData))); //印刷ｾｯﾄ数
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.BUDOMARI, srSpsprintGraData))); //歩留まり

        return params;
    }

    /**
     * 印刷SPSｸﾞﾗﾋﾞｱ(sr_spsprint_gra)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSrSpsprintGra(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

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
     * @param conQcdb コネクション
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
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
     * @param conQcdb コネクション
     * @param rev revision
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void updateSubSrSpsprintGra(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        String sql = "UPDATE sub_sr_spsprint_gra SET "
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
     * 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面登録(tmp_sub_sr_spsprint_gra)更新値パラメータ設定
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
    private List<Object> setUpdateParameterSubSrSpsprintGra(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo,
            String edaban, Timestamp systemTime) {
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
     * 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面仮登録(tmp_sub_sr_spsprint_gra)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
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
                + "FROM tmp_sr_spsprint_gra "
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

    // 内部電極ﾍﾟｰｽﾄﾁｪｯｸ
    private boolean pasteChack(String pasteLot, String pasteNendo, String pasteOndo, String Kokeibun){
        boolean errFlag = true;

	// いずれかの項目が入力されている場合
	boolean isAnyValuePresentFlag = 
	    (pasteLot != null && !"".equals(pasteLot) ||
	    (pasteNendo != null && !"".equals(pasteNendo)) ||
	    (pasteOndo != null && !"".equals(pasteOndo)) ||
	    (Kokeibun != null && !"".equals(Kokeibun)));

	// いずれの項目も入力されていないときはエラーにはしない
	if (isAnyValuePresentFlag) {
	    // 入力項目が全て空か確認
	    if (pasteLot == null || "".equals(pasteLot) ||
	        pasteNendo == null || "".equals(pasteNendo) ||
	        pasteOndo == null || "".equals(pasteOndo) ||
	        Kokeibun == null || "".equals(Kokeibun)) {
	        
                // エラーをフラグ立てる
	        errFlag = true;
	    } else {
                // 全て入力されているのでエラーではない
	        errFlag = false;
	    }
	} else {
            // 全てが未入力の場合もエラーとはしない
	    errFlag = false;
	}
        
        return errFlag;
    }

    /**
     * 開始時間設定処理
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKaishiDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B001Const.INSATSU_KAISHI_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B001Const.INSATSU_KAISHI_TIME);
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
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B001Const.INSATSU_SHUURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B001Const.INSATSU_SHUURYOU_TIME);
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
     * @param srSpsprintGraData 印刷グラビアデータ
     * @return DB値
     */
    private String getSrSpsprintGraItemData(String itemId, SrSpsprintGra srSpsprintGraData) {
        switch (itemId) {
            // ｽﾘｯﾌﾟﾛｯﾄNo
            case GXHDO101B001Const.SLIP_LOTNO:
                return StringUtil.nullToBlank(srSpsprintGraData.getTapelotno());
            // ﾛｰﾙNo1
            case GXHDO101B001Const.ROLL_NO1:
                return StringUtil.nullToBlank(srSpsprintGraData.getTaperollno1());
            // ﾛｰﾙNo2
            case GXHDO101B001Const.ROLL_NO2:
                return StringUtil.nullToBlank(srSpsprintGraData.getTaperollno2());
            // ﾛｰﾙNo3
            case GXHDO101B001Const.ROLL_NO3:
                return StringUtil.nullToBlank(srSpsprintGraData.getTaperollno3());
            // 厚みNo1
            case GXHDO101B001Const.ATSUMI_NO1:
                return StringUtil.nullToBlank(srSpsprintGraData.getAtsumi1());
            // 厚みNo2
            case GXHDO101B001Const.ATSUMI_NO2:
                return StringUtil.nullToBlank(srSpsprintGraData.getAtsumi2());
            // 厚みNo3
            case GXHDO101B001Const.ATSUMI_NO3:
                return StringUtil.nullToBlank(srSpsprintGraData.getAtsumi3());
            // 原料記号
            case GXHDO101B001Const.GENRYO_KIGOU:
                return StringUtil.nullToBlank(srSpsprintGraData.getGenryoukigou());
            // 電極ﾍﾟｰｽﾄ
            case GXHDO101B001Const.DENKYOKU_PASTE:
                return StringUtil.nullToBlank(srSpsprintGraData.getPastehinmei());
            // ﾍﾟｰｽﾄﾛｯﾄNo1
            case GXHDO101B001Const.PASTE_LOT_NO1:
                return StringUtil.nullToBlank(srSpsprintGraData.getPastelotno());
            // ﾍﾟｰｽﾄ粘度1
            case GXHDO101B001Const.PASTE_NENDO1:
                return StringUtil.nullToBlank(srSpsprintGraData.getPastenendo());
            // ﾍﾟｰｽﾄ温度1
            case GXHDO101B001Const.PASTE_ONDO1:
                return StringUtil.nullToBlank(srSpsprintGraData.getPasteondo());
            // ﾍﾟｰｽﾄ固形分1
            case GXHDO101B001Const.PASTE_KOKEIBUN1:
                return StringUtil.nullToBlank(srSpsprintGraData.getPkokeibun1());
            // ﾍﾟｰｽﾄﾛｯﾄNo2
            case GXHDO101B001Const.PASTE_LOT_NO2:
                return StringUtil.nullToBlank(srSpsprintGraData.getPastelotno2());
            // ﾍﾟｰｽﾄ粘度2
            case GXHDO101B001Const.PASTE_NENDO2:
                return StringUtil.nullToBlank(srSpsprintGraData.getPastenendo2());
            // ﾍﾟｰｽﾄ温度2
            case GXHDO101B001Const.PASTE_ONDO2:
                return StringUtil.nullToBlank(srSpsprintGraData.getPasteondo2());
            // ﾍﾟｰｽﾄ固形分2
            case GXHDO101B001Const.PASTE_KOKEIBUN2:
                return StringUtil.nullToBlank(srSpsprintGraData.getPkokeibun2());
            // ﾍﾟｰｽﾄﾛｯﾄNo3
            case GXHDO101B001Const.PASTE_LOT_NO3:
                return StringUtil.nullToBlank(srSpsprintGraData.getPastelotno3());
            // ﾍﾟｰｽﾄ粘度3
            case GXHDO101B001Const.PASTE_NENDO3:
                return StringUtil.nullToBlank(srSpsprintGraData.getPastenendo3());
            // ﾍﾟｰｽﾄ温度3
            case GXHDO101B001Const.PASTE_ONDO3:
                return StringUtil.nullToBlank(srSpsprintGraData.getPasteondo3());
            // ﾍﾟｰｽﾄ固形分3
            case GXHDO101B001Const.PASTE_KOKEIBUN3:
                return StringUtil.nullToBlank(srSpsprintGraData.getPkokeibun3());
            // ﾍﾟｰｽﾄﾛｯﾄNo4
            case GXHDO101B001Const.PASTE_LOT_NO4:
                return StringUtil.nullToBlank(srSpsprintGraData.getPastelotno4());
            // ﾍﾟｰｽﾄ粘度4
            case GXHDO101B001Const.PASTE_NENDO4:
                return StringUtil.nullToBlank(srSpsprintGraData.getPastenendo4());
            // ﾍﾟｰｽﾄ温度4
            case GXHDO101B001Const.PASTE_ONDO4:
                return StringUtil.nullToBlank(srSpsprintGraData.getPasteondo4());
            // ﾍﾟｰｽﾄ固形分4
            case GXHDO101B001Const.PASTE_KOKEIBUN4:
                return StringUtil.nullToBlank(srSpsprintGraData.getPkokeibun4());
            // ﾍﾟｰｽﾄﾛｯﾄNo5
            case GXHDO101B001Const.PASTE_LOT_NO5:
                return StringUtil.nullToBlank(srSpsprintGraData.getPastelotno5());
            // ﾍﾟｰｽﾄ粘度5
            case GXHDO101B001Const.PASTE_NENDO5:
                return StringUtil.nullToBlank(srSpsprintGraData.getPastenendo5());
            // ﾍﾟｰｽﾄ温度5
            case GXHDO101B001Const.PASTE_ONDO5:
                return StringUtil.nullToBlank(srSpsprintGraData.getPasteondo5());
            // ﾍﾟｰｽﾄ固形分5
            case GXHDO101B001Const.PASTE_KOKEIBUN5:
                return StringUtil.nullToBlank(srSpsprintGraData.getPkokeibun5());
            // PETﾌｨﾙﾑ種類No1
            case GXHDO101B001Const.PET_FILM_NO1:
                return StringUtil.nullToBlank(srSpsprintGraData.getPetfilmsyurui());
            // PETﾌｨﾙﾑ種類No2
            case GXHDO101B001Const.PET_FILM_NO2:
                return StringUtil.nullToBlank(srSpsprintGraData.getPetfilmsyurui2());
            // PETﾌｨﾙﾑ種類No3
            case GXHDO101B001Const.PET_FILM_NO3:
                return StringUtil.nullToBlank(srSpsprintGraData.getPetfilmsyurui3());
            // 印刷号機
            case GXHDO101B001Const.INSATSU_GOUKI:
                return StringUtil.nullToBlank(srSpsprintGraData.getGouki());
            // 乾燥温度表示値1
            case GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI1:
                return StringUtil.nullToBlank(srSpsprintGraData.getKansouondo());
            // 乾燥温度表示値2
            case GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI2:
                return StringUtil.nullToBlank(srSpsprintGraData.getKansouondo2());
            // 乾燥温度表示値3
            case GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI3:
                return StringUtil.nullToBlank(srSpsprintGraData.getKansouondo3());
            // 乾燥温度表示値4
            case GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI4:
                return StringUtil.nullToBlank(srSpsprintGraData.getKansouondo4());
            // 乾燥温度下側表示値1
            case GXHDO101B001Const.KANSOU_ONDO_SHITA_HYOUJICHI1:
                return StringUtil.nullToBlank(srSpsprintGraData.getKansouondoshita());
            // 乾燥温度下側表示値2
            case GXHDO101B001Const.KANSOU_ONDO_SHITA_HYOUJICHI2:
                return StringUtil.nullToBlank(srSpsprintGraData.getKansouondoshita2());
            // 乾燥温度下側表示値3
            case GXHDO101B001Const.KANSOU_ONDO_SHITA_HYOUJICHI3:
                return StringUtil.nullToBlank(srSpsprintGraData.getKansouondoshita3());
            // 乾燥温度下側表示値4
            case GXHDO101B001Const.KANSOU_ONDO_SHITA_HYOUJICHI4:
                return StringUtil.nullToBlank(srSpsprintGraData.getKansouondoshita4());
            // 搬送速度
            case GXHDO101B001Const.HANSOU_SOKUDO:
                return StringUtil.nullToBlank(srSpsprintGraData.getHansouspeed());
            // 開始テンション計
            case GXHDO101B001Const.KAISHI_TENSION_KEI:
                return StringUtil.nullToBlank(srSpsprintGraData.getTensionSSum());
            // 開始テンション前
            case GXHDO101B001Const.KAISHI_TENSION_MAE:
                return StringUtil.nullToBlank(srSpsprintGraData.getTensionStemae());
            // 開始テンション奥
            case GXHDO101B001Const.KAISHI_TENSION_OKU:
                return StringUtil.nullToBlank(srSpsprintGraData.getTensionSoku());
            // 終了テンション計
            case GXHDO101B001Const.SHURYOU_TENSION_KEI:
                return StringUtil.nullToBlank(srSpsprintGraData.getTensionESum());
            // 終了テンション前
            case GXHDO101B001Const.SHURYOU_TENSION_MAE:
                return StringUtil.nullToBlank(srSpsprintGraData.getTensionEtemae());
            // 終了テンション奥
            case GXHDO101B001Const.SHURYOU_TENSION_OKU:
                return StringUtil.nullToBlank(srSpsprintGraData.getTensionEoku());
            // 圧胴圧力
            case GXHDO101B001Const.ATSUDOU_ATSURYOKU:
                return StringUtil.nullToBlank(srSpsprintGraData.getAtuDoATu());
            // ブレード圧力
            case GXHDO101B001Const.BLADE_ATSURYOKU:
                return StringUtil.nullToBlank(srSpsprintGraData.getBladeATu());
            // 製版名 / 版胴名
            case GXHDO101B001Const.SEIHAN_OR_HANDOU_MEI:
                return StringUtil.nullToBlank(srSpsprintGraData.getHandoumei());
            // 製版No / 版胴No
            case GXHDO101B001Const.SEIHAN_OR_HANDOU_NO:
                return StringUtil.nullToBlank(srSpsprintGraData.getHandouno());
            // 製版使用枚数/版胴使用枚数
            case GXHDO101B001Const.SEIHAN_OR_HANDOU_SHIYOU_MAISUU:
                return StringUtil.nullToBlank(srSpsprintGraData.getHandoumaisuu());
            // ｽｷｰｼﾞNo/圧胴No
            case GXHDO101B001Const.SQUEEGEE_OR_ATSUDOU_NO:
                return StringUtil.nullToBlank(srSpsprintGraData.getAtudoNo());
            // 圧胴使用枚数
            case GXHDO101B001Const.ATSUDOU_SIYOU_MAISUU:
                return StringUtil.nullToBlank(srSpsprintGraData.getAtudoMaisuu());
            // ブレードNo.
            case GXHDO101B001Const.BLADE_NO:
                return StringUtil.nullToBlank(srSpsprintGraData.getBladeno());
            // ブレード外観
            case GXHDO101B001Const.BLADE_GAIKAN:

                switch (StringUtil.nullToBlank(srSpsprintGraData.getBladegaikan())) {
                    case "0":
                        return "NG";
                    case "1":
                        return "OK";
                    default:
                        return "";
                }
            // ﾌﾞﾚｰﾄﾞ印刷長
            case GXHDO101B001Const.BLADEINSATSUTYO:
                return StringUtil.nullToBlank(srSpsprintGraData.getBladeinsatsutyo());
            // 印刷開始日
            case GXHDO101B001Const.INSATSU_KAISHI_DAY:
                return DateUtil.formattedTimestamp(srSpsprintGraData.getStartdatetime(), "yyMMdd");
            // 印刷開始時間
            case GXHDO101B001Const.INSATSU_KAISHI_TIME:
                return DateUtil.formattedTimestamp(srSpsprintGraData.getStartdatetime(), "HHmm");
            // 印刷ｽﾀｰﾄ膜厚AVE
            case GXHDO101B001Const.INSATSU_START_MAKUATSU_AVE:
                return StringUtil.nullToBlank(srSpsprintGraData.getMakuatuaveStart());
            // 印刷ｽﾀｰﾄ膜厚MAX
            case GXHDO101B001Const.INSATSU_START_MAKUATSU_MAX:
                return StringUtil.nullToBlank(srSpsprintGraData.getMakuatumaxStart());
            // 印刷ｽﾀｰﾄ膜厚MIN
            case GXHDO101B001Const.INSATSU_START_MAKUATSU_MIN:
                return StringUtil.nullToBlank(srSpsprintGraData.getMakuatuminStart());
            // 印刷ｽﾀｰﾄ膜厚CV
            case GXHDO101B001Const.INSATSU_START_MAKUATSU_CV:
                return StringUtil.nullToBlank(srSpsprintGraData.getMakuatucvStart());
            // PTN間距離印刷ｽﾀｰﾄ X Min
            case GXHDO101B001Const.PTN_INSATSU_START_X_MIN:
                return StringUtil.nullToBlank(srSpsprintGraData.getStartPtnDistX());
            // PTN間距離印刷ｽﾀｰﾄ Y Min
            case GXHDO101B001Const.PTN_INSATSU_START_Y_MIN:
                return StringUtil.nullToBlank(srSpsprintGraData.getStartPtnDistY());
            // ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
            case GXHDO101B001Const.STARTJI_NIJIMI_KASURE_CHECK:
                switch (StringUtil.nullToBlank(srSpsprintGraData.getNijimikasureStart())) {
                    case "0":
                        return "NG";
                    case "1":
                        return "OK";
                    default:
                        return "";
                }
            // 印刷スタート時担当者
            case GXHDO101B001Const.INSATSU_STARTJI_TANTOUSHA:
                return StringUtil.nullToBlank(srSpsprintGraData.getTantousya());
            // 印刷スタート時確認者
            case GXHDO101B001Const.INSATSU_STARTJI_KAKUNINSYA:
                return StringUtil.nullToBlank(srSpsprintGraData.getKakuninsya());
            // 印刷終了日
            case GXHDO101B001Const.INSATSU_SHUURYOU_DAY:
                return DateUtil.formattedTimestamp(srSpsprintGraData.getEnddatetime(), "yyMMdd");
            // 印刷終了時刻
            case GXHDO101B001Const.INSATSU_SHUURYOU_TIME:
                return DateUtil.formattedTimestamp(srSpsprintGraData.getEnddatetime(), "HHmm");
            // 印刷ｴﾝﾄﾞ膜厚AVE
            case GXHDO101B001Const.INSATSU_END_MAKUATSU_AVE:
                return StringUtil.nullToBlank(srSpsprintGraData.getMakuatuaveEnd());
            // 印刷ｴﾝﾄﾞ膜厚MAX
            case GXHDO101B001Const.INSATSU_END_MAKUATSU_MAX:
                return StringUtil.nullToBlank(srSpsprintGraData.getMakuatumaxEnd());
            // 印刷ｴﾝﾄﾞ膜厚MIN
            case GXHDO101B001Const.INSATSU_END_MAKUATSU_MIN:
                return StringUtil.nullToBlank(srSpsprintGraData.getMakuatuminEnd());
            // 印刷ｴﾝﾄﾞ膜厚CV
            case GXHDO101B001Const.INSATSU_END_MAKUATSU_CV:
                return StringUtil.nullToBlank(srSpsprintGraData.getMakuatucvEnd());
            // PTN間距離印刷ｴﾝﾄﾞ X Min
            case GXHDO101B001Const.PTN_INSATSU_END_X_MIN:
                return StringUtil.nullToBlank(srSpsprintGraData.getEndPtnDistX());
            // PTN間距離印刷ｴﾝﾄﾞ Y Min
            case GXHDO101B001Const.PTN_INSATSU_END_Y_MIN:
                return StringUtil.nullToBlank(srSpsprintGraData.getEndPtnDistY());
            // 終了時ﾆｼﾞﾐ・ｶｽﾚ確認
            case GXHDO101B001Const.SHUURYOU_JI_NIJIMI_KASURE_CHECK:
                switch (StringUtil.nullToBlank(srSpsprintGraData.getNijimikasureEnd())) {
                    case "0":
                        return "NG";
                    case "1":
                        return "OK";
                    default:
                        return "";
                }

            // 印刷エンド時担当者
            case GXHDO101B001Const.INSATSU_ENDJI_TANTOUSHA:
                return StringUtil.nullToBlank(srSpsprintGraData.getTantoEnd());
            // 印刷枚数
            case GXHDO101B001Const.INSATSU_MAISUU:
                return StringUtil.nullToBlank(srSpsprintGraData.getPrintmaisuu());
            // 清掃 ローラ部
            case GXHDO101B001Const.SEISOUROLLERBU:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSpsprintGraData.getSeisourollerbu()));
            // 清掃 印刷周辺
            case GXHDO101B001Const.SEISOUINSATSUSYUHEN:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSpsprintGraData.getSeisouinsatsusyuhen()));
            // 清掃 乾燥炉内
            case GXHDO101B001Const.SEISOUKANSOURONAI:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSpsprintGraData.getSeisoukansouronai()));
            // 3μｍフィルター 適用
            case GXHDO101B001Const.SANMICRONMFILTERTEKIYOU:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSpsprintGraData.getSanmicronmfiltertekiyou()));
            // 3μｍフィルター 交換
            case GXHDO101B001Const.SANMICRONMFILTERKOUKAN:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSpsprintGraData.getSanmicronmfilterkoukan()));
            // インクパンストッパーロック確認
            case GXHDO101B001Const.INKPANSTOPPERLOCKKAKUNIN:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSpsprintGraData.getInkpanstopperlockkakunin()));
            // 先行ﾛｯﾄNo
            case GXHDO101B001Const.SENKOULOTNO:
                return StringUtil.nullToBlank(srSpsprintGraData.getSkojyo()) + StringUtil.nullToBlank(srSpsprintGraData.getSlotno()) + StringUtil.nullToBlank(srSpsprintGraData.getSedaban());
            // ﾃｰﾌﾟ使い切り
            case GXHDO101B001Const.TAPETSUKAIKIRI:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSpsprintGraData.getTapetsukaikiri()));
            // 次ﾛｯﾄへ
            case GXHDO101B001Const.JILOTHE:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSpsprintGraData.getJilothe()));
            // 成形長さ
            case GXHDO101B001Const.SEIKEINAGASA:
                return StringUtil.nullToBlank(srSpsprintGraData.getSeikeinagasa());
            //備考1
            case GXHDO101B001Const.BIKOU1:
                return StringUtil.nullToBlank(srSpsprintGraData.getBikou1());
            //備考2
            case GXHDO101B001Const.BIKOU2:
                return StringUtil.nullToBlank(srSpsprintGraData.getBikou2());
            //備考3
            case GXHDO101B001Const.BIKOU3:
                return StringUtil.nullToBlank(srSpsprintGraData.getBikou3());
            //備考4
            case GXHDO101B001Const.BIKOU4:
                return StringUtil.nullToBlank(srSpsprintGraData.getBikou4());
            //備考5
            case GXHDO101B001Const.BIKOU5:
                return StringUtil.nullToBlank(srSpsprintGraData.getBikou5());
            //KCPNo
            case GXHDO101B001Const.KCPNO:
                return StringUtil.nullToBlank(srSpsprintGraData.getKcpno());
            //印刷長さ
            case GXHDO101B001Const.PRINTLENGTH:
                return StringUtil.nullToBlank(srSpsprintGraData.getPrintlength());
            //印刷ｾｯﾄ数
            case GXHDO101B001Const.PRINT_SETSU:
                return StringUtil.nullToBlank(srSpsprintGraData.getInsatusetsuu());
            //歩留まり
            case GXHDO101B001Const.BUDOMARI:
                return StringUtil.nullToBlank(srSpsprintGraData.getBudomari());

            default:
                return null;

        }
    }

    /**
     * 印刷SPSｸﾞﾗﾋﾞｱ_仮登録(tmp_sr_spsprint_gra)登録処理(削除時)
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
    private void insertDeleteDataTmpSrSpsprintGra(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_spsprint_gra ("
                + "kojyo,lotno,edaban,tapelotno,petfilmsyurui,taperollno1,taperollno2,taperollno3,"
                + "pastelotno,pastenendo,pasteondo,pkokeibun1,pastelotno2,pastenendo2,pasteondo2,"
                + "pkokeibun2,handoumei,handouno,handoumaisuu,bladeno,bladegaikan,BladeATu,bladeinsatsutyo,AtudoNo,"
                + "AtudoMaisuu,AtuDoATu,gouki,kansouondo,kansouondo2,kansouondo3,kansouondo4,"
                + "kansouondo5,hansouspeed,startdatetime,tantousya,makuatuave_start,makuatumax_start,"
                + "makuatumin_start,makuatucv_start,nijimikasure_start,start_ptn_dist_x,start_ptn_dist_y,"
                + "TensionS_sum,TensionStemae,TensionSoku,enddatetime,tanto_end,printmaisuu,"
                + "makuatuave_end,makuatumax_end,makuatumin_end,makuatucv_end,nijimikasure_end,end_ptn_dist_x,"
                + "end_ptn_dist_y,TensionE_sum,TensionEtemae,TensionEoku,printzure1_surihajime_start,"
                + "printzure2_center_start,printzure3_suriowari_start,abzure_heikin_start,"
                + "printzure1_surihajime_end,printzure2_center_end,printzure3_suriowari_end,"
                + "abzure_heikin_end,genryoukigou,bikou1,bikou2,torokunichiji,kosinnichiji,"
                + "revision,kcpno,kakuninsya,seisourollerbu,seisouinsatsusyuhen,seisoukansouronai,3micronmfiltertekiyou,"
                + "3micronmfilterkoukan,inkpanstopperlockkakunin,skojyo,slotno,sedaban,tapetsukaikiri,jilothe,seikeinagasa,"
                + "bikou3,bikou4,bikou5,deleteflag,printlength,kansouondoshita,kansouondoshita2,kansouondoshita3,kansouondoshita4,"
                + "atsumi1,atsumi2,atsumi3,petfilmsyurui2,petfilmsyurui3,pastehinmei,"
                + "pastelotno3,pastenendo3,pasteondo3,pkokeibun3,"
                + "pastelotno4,pastenendo4,pasteondo4,pkokeibun4,"
                + "pastelotno5,pastenendo5,pasteondo5,pkokeibun5,"
                + "insatusetsuu,budomari"
                + ") SELECT "
                + "kojyo,lotno,edaban,tapelotno,petfilmsyurui,taperollno1,taperollno2,taperollno3,"
                + "pastelotno,pastenendo,pasteondo,pkokeibun1,pastelotno2,pastenendo2,pasteondo2,"
                + "pkokeibun2,handoumei,handouno,handoumaisuu,bladeno,bladegaikan,BladeATu,bladeinsatsutyo,AtudoNo,"
                + "AtudoMaisuu,AtuDoATu,gouki,kansouondo,kansouondo2,kansouondo3,kansouondo4,"
                + "kansouondo5,hansouspeed,startdatetime,tantousya,makuatuave_start,makuatumax_start,"
                + "makuatumin_start,makuatucv_start,nijimikasure_start,start_ptn_dist_x,start_ptn_dist_y,"
                + "TensionS_sum,TensionStemae,TensionSoku,enddatetime,tanto_end,printmaisuu,"
                + "makuatuave_end,makuatumax_end,makuatumin_end,makuatucv_end,nijimikasure_end,end_ptn_dist_x,"
                + "end_ptn_dist_y,TensionE_sum,TensionEtemae,TensionEoku,printzure1_surihajime_start,"
                + "printzure2_center_start,printzure3_suriowari_start,abzure_heikin_start,"
                + "printzure1_surihajime_end,printzure2_center_end,printzure3_suriowari_end,"
                + "abzure_heikin_end,genryoukigou,bikou1,bikou2,?,?,"
                + "?,kcpno,kakuninsya,seisourollerbu,seisouinsatsusyuhen,seisoukansouronai,3micronmfiltertekiyou,"
                + "3micronmfilterkoukan,inkpanstopperlockkakunin,skojyo,slotno,sedaban,tapetsukaikiri,jilothe,seikeinagasa,"
                + "bikou3,bikou4,bikou5,?,printlength,kansouondoshita,kansouondoshita2,kansouondoshita3,kansouondoshita4,"
                + "atsumi1,atsumi2,atsumi3,petfilmsyurui2,petfilmsyurui3,pastehinmei,"
                + "pastelotno3,pastenendo3,pasteondo3,pkokeibun3,"
                + "pastelotno4,pastenendo4,pasteondo4,pkokeibun4,"
                + "pastelotno5,pastenendo5,pasteondo5,pkokeibun5,"
                + "insatusetsuu,budomari "
                + "FROM sr_spsprint_gra "
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
     * 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面仮登録(tmp_sub_sr_spsprint_gra)登録処理(削除時)
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
    private void insertDeleteDataTmpSubSrSpsprintGra(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        String sql = "INSERT INTO tmp_sub_sr_spsprint_gra ("
                + "kojyo,lotno,edaban,makuatsu_start1,makuatsu_start2,makuatsu_start3,makuatsu_start4,makuatsu_start5,makuatsu_start6,"
                + "makuatsu_start7,makuatsu_start8,makuatsu_start9,start_ptn_dist_x1,start_ptn_dist_x2,start_ptn_dist_x3,"
                + "start_ptn_dist_x4,start_ptn_dist_x5,start_ptn_dist_y1,start_ptn_dist_y2,start_ptn_dist_y3,start_ptn_dist_y4,"
                + "start_ptn_dist_y5,makuatsu_end1,makuatsu_end2,makuatsu_end3,makuatsu_end4,makuatsu_end5,makuatsu_end6,"
                + "makuatsu_end7,makuatsu_end8,makuatsu_end9,end_ptn_dist_x1,end_ptn_dist_x2,end_ptn_dist_x3,end_ptn_dist_x4,"
                + "end_ptn_dist_x5,end_ptn_dist_y1,end_ptn_dist_y2,end_ptn_dist_y3,end_ptn_dist_y4,end_ptn_dist_y5,torokunichiji,"
                + "kosinnichiji,revision,deleteflag"
                + ") SELECT "
                + "kojyo,lotno,edaban,makuatsu_start1,makuatsu_start2,makuatsu_start3,makuatsu_start4,makuatsu_start5,makuatsu_start6,"
                + "makuatsu_start7,makuatsu_start8,makuatsu_start9,start_ptn_dist_x1,start_ptn_dist_x2,start_ptn_dist_x3,"
                + "start_ptn_dist_x4,start_ptn_dist_x5,start_ptn_dist_y1,start_ptn_dist_y2,start_ptn_dist_y3,start_ptn_dist_y4,"
                + "start_ptn_dist_y5,makuatsu_end1,makuatsu_end2,makuatsu_end3,makuatsu_end4,makuatsu_end5,makuatsu_end6,"
                + "makuatsu_end7,makuatsu_end8,makuatsu_end9,end_ptn_dist_x1,end_ptn_dist_x2,end_ptn_dist_x3,end_ptn_dist_x4,"
                + "end_ptn_dist_x5,end_ptn_dist_y1,end_ptn_dist_y2,end_ptn_dist_y3,end_ptn_dist_y4,end_ptn_dist_y5,?,"
                + "?,?,? "
                + "FROM sub_sr_spsprint_gra "
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
     * 元データ設定処理
     * @param processData 処理制御データ
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param formId 画面ID
     * @param motoLotno 参照元ﾛｯﾄNo(ﾌﾙ桁)
     * @param sakiKojyo 工場ｺｰﾄﾞ
     * @param sakilotNo8 ﾛｯﾄNo(8桁)
     * @param sakiEdaban 枝番
     * @return 元データ設定 true(成功) false(失敗)
     * @throws SQLException 例外
     */
    private boolean setSanshouMotoLotData(ProcessData processData, QueryRunner queryRunnerQcdb,QueryRunner queryRunnerDoc, String formId, String motoLotno, 
            String sakiKojyo, String sakilotNo8, String sakiEdaban) throws SQLException{
        
        // 元ﾛｯﾄを分解
        String motoKojyo = motoLotno.substring(0, 3);
        String motoLotNo8 = motoLotno.substring(3, 11);
        String motoEdaban = motoLotno.substring(11, 14);

        Map fxhdd03RevInfo = loadFxhdd03RevInfo(queryRunnerDoc, motoKojyo, motoLotNo8, motoEdaban, formId);
        String rev = StringUtil.nullToBlank(getMapData(fxhdd03RevInfo, "rev"));
        
        // 印刷SPSｸﾞﾗﾋﾞｱデータ取得
        List<SrSpsprintGra> srSpsprintGraDataList = getSrSpsprintGraData(queryRunnerQcdb, rev, "1", motoKojyo, motoLotNo8, motoEdaban);
        if (srSpsprintGraDataList.isEmpty()) {
            //該当データが取得できなかった処理失敗としてリターンする
            return false;
        }

        // 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面データ取得
        List<SubSrSpsprintGra> subSrSpsprintGraDataList = getSubSrSpsprintGraData(queryRunnerQcdb, rev, "1", motoKojyo, motoLotNo8, motoEdaban);
        if (subSrSpsprintGraDataList.isEmpty()) {
            //該当データが取得できなかった処理失敗としてリターンする
            return false;
        }

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srSpsprintGraDataList.get(0));

        // 膜厚入力画面データ設定
        setInputItemDataSubFormC001(subSrSpsprintGraDataList.get(0), sakiKojyo, sakilotNo8, sakiEdaban);

        // PTN距離X入力画面データ設定
        setInputItemDataSubFormC002(subSrSpsprintGraDataList.get(0));

        // PTN距離Y入力画面データ設定
        setInputItemDataSubFormC003(subSrSpsprintGraDataList.get(0));
        
        // 前工程WIP取込画面データ設定
        setInputItemDataSubFormC020(queryRunnerQcdb, motoKojyo, motoLotNo8, motoEdaban, JOTAI_FLG_TOROKUZUMI);
        return true;
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
        model = GXHDO101C020Logic.createGXHDO101C020Model(initDataSubFormC020, "GXHDO101B001");

        model.setReturnItemId_TapeLot1_Hinmei(GXHDO101B001Const.DENKYOKU_TAPE);
        model.setReturnItemId_TapeLot1_Conventionallot(GXHDO101B001Const.SLIP_LOTNO);
        model.setReturnItemId_TapeLot1_Lotkigo(GXHDO101B001Const.GENRYO_KIGOU);
        model.setReturnItemId_TapeLot1_Rollno(GXHDO101B001Const.ROLL_NO1);
        model.setReturnItemId_TapeLot1_Tapelength(GXHDO101B001Const.SEIKEINAGASA);
        model.setReturnItemId_TapeLot2_Rollno(GXHDO101B001Const.ROLL_NO2);
        model.setReturnItemId_TapeLot3_Rollno(GXHDO101B001Const.ROLL_NO3);
        model.setReturnItemId_PasteLot1_Hinmei(GXHDO101B001Const.DENKYOKU_PASTE);
        model.setReturnItemId_PasteLot1_Conventionallot(GXHDO101B001Const.PASTE_LOT_NO1);
        model.setReturnItemId_PasteLot1_Kokeibunpct(GXHDO101B001Const.PASTE_KOKEIBUN1);
        model.setReturnItemId_PasteLot2_Conventionallot(GXHDO101B001Const.PASTE_LOT_NO2);
        model.setReturnItemId_PasteLot2_Kokeibunpct(GXHDO101B001Const.PASTE_KOKEIBUN2);
        model.setReturnItemId_TapeLot1_AtumiNo1(GXHDO101B001Const.ATSUMI_NO1);
        model.setReturnItemId_TapeLot2_AtumiNo2(GXHDO101B001Const.ATSUMI_NO2);
        model.setReturnItemId_TapeLot3_AtumiNo3(GXHDO101B001Const.ATSUMI_NO3);
        model.setReturnItemId_TapeLot1_PetFilmNo1(GXHDO101B001Const.PET_FILM_NO1);
        model.setReturnItemId_TapeLot2_PetFilmNo2(GXHDO101B001Const.PET_FILM_NO2);
        model.setReturnItemId_TapeLot3_PetFilmNo3(GXHDO101B001Const.PET_FILM_NO3);
        model.setReturnItemId_PasteLot3_Conventionallot(GXHDO101B001Const.PASTE_LOT_NO3);
        model.setReturnItemId_PasteLot3_Kokeibunpct(GXHDO101B001Const.PASTE_KOKEIBUN3);
        model.setReturnItemId_PasteLot4_Conventionallot(GXHDO101B001Const.PASTE_LOT_NO4);
        model.setReturnItemId_PasteLot4_Kokeibunpct(GXHDO101B001Const.PASTE_KOKEIBUN4);
        model.setReturnItemId_PasteLot5_Conventionallot(GXHDO101B001Const.PASTE_LOT_NO5);
        model.setReturnItemId_PasteLot5_Kokeibunpct(GXHDO101B001Const.PASTE_KOKEIBUN5);
        model.setMoto_Denkyoku_Paste_Sekkei(GXHDO101B001Const.DENKYOKU_PASTE_SEKKEI);
        model.setMoto_PasteKokeibun(GXHDO101B001Const.PASTE_KOKEI_BUN);
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
        params.add("GXHDO101B001");
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
            params.add("GXHDO101B001"); //画面ID
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
            params.add("GXHDO101B001"); //画面ID
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
        params.add("GXHDO101B001");
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
        params.add("GXHDO101B001"); //画面ID
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
        params.add("GXHDO101B001");
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
        params.add("GXHDO101B001");
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
        return "false";
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
            List<Map<String, Object>> tmpSrGraprintKanriDataList = loadTmpGraprintKanriData(queryRunnerQcdb, lotNo, null);
            if (tmpSrGraprintKanriDataList == null || tmpSrGraprintKanriDataList.isEmpty()) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                ErrorMessageInfo checkItemError = MessageUtil.getErrorMessageInfo("XHD-000210", true, true, null, "設備ﾃﾞｰﾀ");
                if (checkItemError != null) {
                    processData.setErrorMessageInfoList(Arrays.asList(checkItemError));
                    return processData;
                }
            }
            HashMap<String, String> itemIdConvertMap = new HashMap<>();
            itemIdConvertMap.put(GXHDO101B001Const.INSATSU_GOUKI, "goukicode");
            ErrorMessageInfo checkItemError = checkDataCooperation(processData, queryRunnerQcdb, lotNo, 1, itemIdConvertMap);
            if (checkItemError != null) {
                processData.setErrorMessageInfoList(Arrays.asList(checkItemError));
                return processData;
            }
            doDataCooperation(processData, queryRunnerQcdb, lotNo, 1, itemIdConvertMap);
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
            HashMap<String, String> itemIdConvertMap) throws SQLException {
        ErrorMessageInfo checkItemError = null;
        // 検索条件:ﾃﾞｰﾀの種類==datasyurui で、Ⅲ.画面表示仕様(21)を発行する。
        List<Map<String, Object>> tmpSrGraprintKanriDataList = loadTmpGraprintKanriData(queryRunnerQcdb, lotNo, String.valueOf(datasyurui));
        if (tmpSrGraprintKanriDataList != null && !tmpSrGraprintKanriDataList.isEmpty()) {
            // 取得したﾃﾞｰﾀで実績Noが高い管理Noで、Ⅲ.画面表示仕様(22)を発行する。
            Map<String, Object> tmpSrGraprintKanriData = tmpSrGraprintKanriDataList.get(0);
            List<Map<String, Object>> tmpSrGraprintDataList = loadTmpGraprintData(queryRunnerQcdb, (Long) tmpSrGraprintKanriData.get("kanrino"));
            if (tmpSrGraprintDataList != null && !tmpSrGraprintDataList.isEmpty()) {
                // Ⅵ.画面項目制御・出力仕様.G3)入力項目部.【設備ﾃﾞｰﾀ連携】ﾎﾞﾀﾝ押下時.開始時 の該当項目へ取得ﾃﾞｰﾀを上書きする。
                List<String> numberItemList;
                if (datasyurui == 1 || datasyurui == 2) {
                    // 開始時(ﾃﾞｰﾀ種類1or2)
                    numberItemList = Arrays.asList(GXHDO101B001Const.SEIHAN_OR_HANDOU_SHIYOU_MAISUU, GXHDO101B001Const.ATSUDOU_SIYOU_MAISUU);
                    checkItemError = checkDataCooperationItemData(processData, numberItemList, tmpSrGraprintDataList, itemIdConvertMap);
                    if (checkItemError == null) {
                        checkItemError = checkDataCooperation(processData, queryRunnerQcdb, lotNo, 3, itemIdConvertMap);
                        if (checkItemError != null) {
                            return checkItemError;
                        }
                    } else {
                        return checkItemError;
                    }
                } else if (datasyurui == 3 || datasyurui == 4) {
                    // 終了時(ﾃﾞｰﾀ種類3or4)
                    numberItemList = Arrays.asList(GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI1, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI2,
                            GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI3, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI4, 
                            GXHDO101B001Const.HANSOU_SOKUDO, GXHDO101B001Const.ATSUDOU_ATSURYOKU, GXHDO101B001Const.BLADE_ATSURYOKU, GXHDO101B001Const.BLADEINSATSUTYO,
                            GXHDO101B001Const.KAISHI_TENSION_KEI, GXHDO101B001Const.KAISHI_TENSION_MAE, GXHDO101B001Const.KAISHI_TENSION_OKU, GXHDO101B001Const.SHURYOU_TENSION_KEI,
                            GXHDO101B001Const.SHURYOU_TENSION_MAE, GXHDO101B001Const.SHURYOU_TENSION_OKU, GXHDO101B001Const.PRINTLENGTH, 
                            GXHDO101B001Const.INSATSU_MAISUU, GXHDO101B001Const.KANSOU_ONDO_SHITA_HYOUJICHI1, GXHDO101B001Const.KANSOU_ONDO_SHITA_HYOUJICHI2, GXHDO101B001Const.KANSOU_ONDO_SHITA_HYOUJICHI3, GXHDO101B001Const.KANSOU_ONDO_SHITA_HYOUJICHI4);
//                    Arrays.asList(GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI1, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI2,
//                            GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI3, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI4, GXHDO101B001Const.HANSOU_SOKUDO, GXHDO101B001Const.KAISHI_TENSION_KEI, GXHDO101B001Const.KAISHI_TENSION_MAE,
//                            GXHDO101B001Const.KAISHI_TENSION_OKU, GXHDO101B001Const.SHURYOU_TENSION_KEI, GXHDO101B001Const.SHURYOU_TENSION_MAE,
//                            GXHDO101B001Const.SHURYOU_TENSION_OKU, GXHDO101B001Const.ATSUDOU_ATSURYOKU, GXHDO101B001Const.BLADE_ATSURYOKU,
//                            GXHDO101B001Const.BLADEINSATSUTYO, GXHDO101B001Const.INSATSU_MAISUU, GXHDO101B001Const.PRINTLENGTH);
                    checkItemError = checkDataCooperationItemData(processData, numberItemList, tmpSrGraprintDataList, itemIdConvertMap);
                }
            } else {
                if (datasyurui == 1 || datasyurui == 2) {
                    checkItemError = checkDataCooperation(processData, queryRunnerQcdb, lotNo, 3, itemIdConvertMap);
                    if (checkItemError != null) {
                        return checkItemError;
                    }
                }
            }
        } else {
            datasyurui++;
            if (datasyurui <= 4) {
                checkItemError = checkDataCooperation(processData, queryRunnerQcdb, lotNo, datasyurui, itemIdConvertMap);
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
     * @param tmpSrGraprintDataList 取得ﾃﾞｰﾀ
     * @param itemIdConvertMap ﾌｫｰﾑﾊﾟﾗﾒｰﾀ(item_id)とtmp_sr_graprint(item_id)の対比表
     * @return ｴﾗｰﾒｯｾｰｼﾞ情報
     */
    private ErrorMessageInfo checkDataCooperationItemData(ProcessData processData, List<String> numberItemList, List<Map<String, Object>> tmpSrGraprintDataList,
            HashMap<String, String> itemIdConvertMap) {
        for(String itemId : numberItemList){
            FXHDD01 itemData = processData.getItemList().stream().filter(n -> itemId.equals(n.getItemId())).findFirst().orElse(null);
            String[] tmpSrGraprintItemId = {itemId};
            if (itemData != null) {
                if (itemIdConvertMap.containsKey(itemId)) {
                    tmpSrGraprintItemId[0] = itemIdConvertMap.get(itemId);
                }
                Map<String, Object> tmpSrGraprintData = tmpSrGraprintDataList.stream().filter(e -> tmpSrGraprintItemId[0].equals(e.get("item_id"))).findFirst().orElse(null);
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
            HashMap<String, String> itemIdConvertMap) throws SQLException {
        // 検索条件:ﾃﾞｰﾀの種類==datasyurui で、Ⅲ.画面表示仕様(21)を発行する。
        List<Map<String, Object>> tmpSrGraprintKanriDataList = loadTmpGraprintKanriData(queryRunnerQcdb, lotNo, String.valueOf(datasyurui));
        if (tmpSrGraprintKanriDataList != null && !tmpSrGraprintKanriDataList.isEmpty()) {
            // 取得したﾃﾞｰﾀで実績Noが高い管理Noで、Ⅲ.画面表示仕様(22)を発行する。
            Map<String, Object> tmpSrGraprintKanriData = tmpSrGraprintKanriDataList.get(0);
            List<Map<String, Object>> tmpSrGraprintDataList = loadTmpGraprintData(queryRunnerQcdb, (Long) tmpSrGraprintKanriData.get("kanrino"));
            if (tmpSrGraprintDataList != null && !tmpSrGraprintDataList.isEmpty()) {
                // Ⅵ.画面項目制御・出力仕様.G3)入力項目部.【設備ﾃﾞｰﾀ連携】ﾎﾞﾀﾝ押下時.開始時 の該当項目へ取得ﾃﾞｰﾀを上書きする。
                List<String> setValueItemList;
                if (datasyurui == 1 || datasyurui == 2) {
                    // 開始時(ﾃﾞｰﾀ種類1or2)
                    setValueItemList = Arrays.asList(GXHDO101B001Const.INSATSU_GOUKI, GXHDO101B001Const.SEIHAN_OR_HANDOU_MEI,
                            GXHDO101B001Const.SEIHAN_OR_HANDOU_NO, GXHDO101B001Const.SEIHAN_OR_HANDOU_SHIYOU_MAISUU, GXHDO101B001Const.ATSUDOU_SIYOU_MAISUU,
                            GXHDO101B001Const.INSATSU_KAISHI_DAY, GXHDO101B001Const.INSATSU_KAISHI_TIME);
                    setDataCooperationItemData(processData, setValueItemList, tmpSrGraprintDataList, itemIdConvertMap);
                    doDataCooperation(processData, queryRunnerQcdb, lotNo, 3, itemIdConvertMap);
                } else if (datasyurui == 3 || datasyurui == 4) {
                    // 終了時(ﾃﾞｰﾀ種類3or4)
                    setValueItemList = Arrays.asList(GXHDO101B001Const.INSATSU_GOUKI, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI1, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI2,
                            GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI3, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI4, 
                            GXHDO101B001Const.HANSOU_SOKUDO, GXHDO101B001Const.ATSUDOU_ATSURYOKU, GXHDO101B001Const.BLADE_ATSURYOKU, GXHDO101B001Const.BLADEINSATSUTYO,
                            GXHDO101B001Const.KAISHI_TENSION_KEI, GXHDO101B001Const.INSATSU_SHUURYOU_DAY, GXHDO101B001Const.INSATSU_SHUURYOU_TIME,
                            GXHDO101B001Const.KAISHI_TENSION_MAE, GXHDO101B001Const.KAISHI_TENSION_OKU, GXHDO101B001Const.SHURYOU_TENSION_KEI,
                            GXHDO101B001Const.SHURYOU_TENSION_MAE, GXHDO101B001Const.SHURYOU_TENSION_OKU, GXHDO101B001Const.PRINTLENGTH, 
                            GXHDO101B001Const.INSATSU_MAISUU, GXHDO101B001Const.KANSOU_ONDO_SHITA_HYOUJICHI1, GXHDO101B001Const.KANSOU_ONDO_SHITA_HYOUJICHI2, GXHDO101B001Const.KANSOU_ONDO_SHITA_HYOUJICHI3, GXHDO101B001Const.KANSOU_ONDO_SHITA_HYOUJICHI4);
                    setDataCooperationItemData(processData, setValueItemList, tmpSrGraprintDataList, itemIdConvertMap);
                }
            } else {
                if (datasyurui == 1 || datasyurui == 2) {
                    doDataCooperation(processData, queryRunnerQcdb, lotNo, 3, itemIdConvertMap);
                }
            }
        } else {
            datasyurui++;
            if (datasyurui <= 4) {
                doDataCooperation(processData, queryRunnerQcdb, lotNo, datasyurui, itemIdConvertMap);
            }
        }
    }

    /**
     * 【設備ﾃﾞｰﾀ連携】ﾎﾞﾀﾝ押下時.開始時 の該当項目へ取得ﾃﾞｰﾀを上書きする
     *
     * @param processData 処理制御データ
     * @param setValueItemList 項目リスト
     * @param tmpSrGraprintDataList 取得ﾃﾞｰﾀ
     * @param itemIdConvertMap ﾌｫｰﾑﾊﾟﾗﾒｰﾀ(item_id)とtmp_sr_graprint(item_id)の対比表
     */
    private void setDataCooperationItemData(ProcessData processData, List<String> setValueItemList, List<Map<String, Object>> tmpSrGraprintDataList,
            HashMap<String, String> itemIdConvertMap) {
        setValueItemList.forEach(itemId -> {
            FXHDD01 itemData = processData.getItemList().stream().filter(n -> itemId.equals(n.getItemId())).findFirst().orElse(null);
            String[] tmpSrGraprintItemId = {itemId};
            if (itemData != null) {
                if (itemIdConvertMap.containsKey(itemId)) {
                    tmpSrGraprintItemId[0] = itemIdConvertMap.get(itemId);
                }
                Map<String, Object> tmpSrGraprintData = tmpSrGraprintDataList.stream().filter(e -> tmpSrGraprintItemId[0].equals(e.get("item_id"))).findFirst().orElse(null);
                if (tmpSrGraprintData != null && !tmpSrGraprintData.isEmpty()) {
                    if (GXHDO101B001Const.PRINTLENGTH.equals(StringUtil.nullToBlank(tmpSrGraprintData.get("item_id")))) {
                        if (!"0".equals(StringUtil.nullToBlank(tmpSrGraprintData.get("atai")))) {
                            itemData.setValue(StringUtil.nullToBlank(tmpSrGraprintData.get("atai")));
                        }
                    } else {
                        itemData.setValue(StringUtil.nullToBlank(tmpSrGraprintData.get("atai")));
                    }
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
    private List<Map<String, Object>> loadTmpGraprintKanriData(QueryRunner queryRunnerQcdb, String lotNo, String datasyurui) throws SQLException {
        String kojyo = lotNo.substring(0, 3);
        String lotno = lotNo.substring(3, 11);
        String edaban = lotNo.substring(11, 14);

        // [tmp_sr_graprint_kanri]から、ﾃﾞｰﾀの取得
        String sql = "SELECT distinct t1.kanrino, kojyo, lotno, edaban, datasyurui, jissekino, torokunichiji"
                + " FROM tmp_sr_graprint_kanri t1 "
                + " INNER JOIN tmp_sr_graprint t2 ON t1.kanrino = t2.kanrino "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? ";
        if (!StringUtil.isEmpty(datasyurui)) {
            sql += " AND datasyurui = ? ";
        }
        sql += " AND t2.item_id = 'dp2mode' AND t2.atai = '0' AND (t1.lot_flg < 9 OR t1.lot_flg is NULL)";
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
     * (22)[tmp_sr_graprint]から、ﾃﾞｰﾀの取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kanrino 管理No(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<Map<String, Object>> loadTmpGraprintData(QueryRunner queryRunnerQcdb, Long kanrino) throws SQLException {
        // [tmp_sr_graprint]から、ﾃﾞｰﾀの取得
        String sql = "SELECT kanrino, item_id, atai"
                + " FROM tmp_sr_graprint WHERE kanrino = ?";

        List<Object> params = new ArrayList<>();
        params.add(kanrino);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapListHandler(), params.toArray());
    }
    
    /**
     * (24)[da_joken]から、ﾃﾞｰﾀの取得
     * 
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param sekkeiNo 設計No(検索キー)
     * @param kouteiMei 工程名(検索キー)
     * @param komokuMei 項目名(検索キー)
     * @param kanriKomoku 管理項目名(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadDaJokenData(QueryRunner queryRunnerQcdb, String sekkeiNo, String kouteiMei, String komokuMei, String kanriKomoku) throws SQLException {
        // 条件データ取得
        String sql = "SELECT KIKAKUCHI FROM da_joken "
                + "WHERE SEKKEINO = ? AND KOUTEIMEI = ? AND KOUMOKUMEI = ? AND KANRIKOUMOKU = ? ";
        
        List<Object> params = Arrays.asList(sekkeiNo, kouteiMei, komokuMei, kanriKomoku);
        
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map daJokendata = queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
        
        return daJokendata;
    }

    /**
     * (25)[ﾊﾟﾗﾒｰﾀﾏｽﾀ]から、ﾃﾞｰﾀの取得
     *
     * @param queryRunnerDoc オブジェクト
     * @param userName ユーザー名
     * @param key Key
     * @return 取得データ
     */
    private String loadParamData(QueryRunner queryRunnerDoc, String userName, String key) {
        try {

            // ﾊﾟﾗﾒｰﾀﾏｽﾀデータの取得
            String sql = "SELECT data "
                    + " FROM fxhbm03 "
                    + " WHERE user_name = ? AND key = ? ";

            List<Object> params = new ArrayList<>();
            params.add(userName);
            params.add(key);
            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            Map data = queryRunnerDoc.query(sql, new MapHandler(), params.toArray());
            if (data != null && !data.isEmpty()) {
                return StringUtil.nullToBlank(data.get("data"));
            }

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
        return null;

    }
    
}
