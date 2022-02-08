/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo102;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import jp.co.kccs.xhd.common.CompMessage;
import jp.co.kccs.xhd.common.InitMessage;
import jp.co.kccs.xhd.common.KikakuError;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.db.model.SikakariJson;
import jp.co.kccs.xhd.db.model.SrSlipBinderkongou;
import jp.co.kccs.xhd.pxhdo901.ErrorMessageInfo;
import jp.co.kccs.xhd.pxhdo901.IFormLogic;
import jp.co.kccs.xhd.pxhdo901.KikakuchiInputErrorInfo;
import jp.co.kccs.xhd.pxhdo901.ProcessData;
import jp.co.kccs.xhd.util.CommonUtil;
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
 * 変更日	2021/12/16<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B032(ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合)
 *
 * @author KCSS K.Jo
 * @since 2021/12/16
 */
public class GXHDO102B032 implements IFormLogic {

    private static final Logger LOGGER = Logger.getLogger(GXHDO102B032.class.getName());
    // 仮登録フラグ
    private static final String JOTAI_FLG_KARI_TOROKU = "0";
    // 登録済フラグ
    private static final String JOTAI_FLG_TOROKUZUMI = "1";
    // 削除フラグ
    private static final String JOTAI_FLG_SAKUJO = "9";
    // エラー背景色
    private static final String SQL_STATE_RECORD_LOCK_ERR = "55P03";

    /**
     * コンストラクタ
     */
    public GXHDO102B032() {
    }

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

            //処理時にエラーの背景色を戻さない機能として登録
            processData.setNoCheckButtonId(Arrays.asList(
                    GXHDO102B032Const.BTN_EDABAN_COPY_TOP,
                    GXHDO102B032Const.BTN_BINDERKONGOUKAISI_TOP,
                    GXHDO102B032Const.BTN_BINDERKONGOUSYUURYOU_TOP,
                    GXHDO102B032Const.BTN_EDABAN_COPY_BOTTOM,
                    GXHDO102B032Const.BTN_BINDERKONGOUKAISI_BOTTOM,
                    GXHDO102B032Const.BTN_BINDERKONGOUSYUURYOU_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO102B032Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO102B032Const.BTN_INSERT_TOP,
                    GXHDO102B032Const.BTN_DELETE_TOP,
                    GXHDO102B032Const.BTN_UPDATE_TOP,
                    GXHDO102B032Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO102B032Const.BTN_INSERT_BOTTOM,
                    GXHDO102B032Const.BTN_DELETE_BOTTOM,
                    GXHDO102B032Const.BTN_UPDATE_BOTTOM
            ));

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
     * ボタンID⇒メソッド名変換
     *
     * @param buttonId ボタンID
     * @return メソッド名
     */
    @Override
    public String convertButtonIdToMethod(String buttonId) {
        String method;
        switch (buttonId) {
            // 枝番コピー
            case GXHDO102B032Const.BTN_EDABAN_COPY_TOP:
            case GXHDO102B032Const.BTN_EDABAN_COPY_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 仮登録
            case GXHDO102B032Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO102B032Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO102B032Const.BTN_INSERT_TOP:
            case GXHDO102B032Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 修正
            case GXHDO102B032Const.BTN_UPDATE_TOP:
            case GXHDO102B032Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO102B032Const.BTN_DELETE_TOP:
            case GXHDO102B032Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // ﾊﾞｲﾝﾀﾞｰ混合開始日時
            case GXHDO102B032Const.BTN_BINDERKONGOUKAISI_TOP:
            case GXHDO102B032Const.BTN_BINDERKONGOUKAISI_BOTTOM:
                method = "setBinderkongoukaisi";
                break;
            // 1ﾛｯﾄ当たりｽﾗﾘｰ重量計算
            case GXHDO102B032Const.BTN_1LOTATARISLURRYJYUURYOU_TOP:
            case GXHDO102B032Const.BTN_1LOTATARISLURRYJYUURYOU_BOTTOM:
                method = "setIchilotatarislurryjyuuryou";
                break;
            // ｽﾘｯﾌﾟ予定重量計算
            case GXHDO102B032Const.BTN_SLIPYOTEIJYUURYOU_TOP:
            case GXHDO102B032Const.BTN_SLIPYOTEIJYUURYOU_BOTTOM:
                method = "setSlipyoteijyuuryou";
                break;
            // ﾊﾞｲﾝﾀﾞｰ混合終了日時
            case GXHDO102B032Const.BTN_BINDERKONGOUSYUURYOU_TOP:
            case GXHDO102B032Const.BTN_BINDERKONGOUSYUURYOU_BOTTOM:
                method = "setBinderkongousyuuryou";
                break;
            default:
                method = "error";
                break;
        }

        return method;
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
            int paramJissekino = (Integer) session.getAttribute("jissekino");
            String tantoshaCd = (String) session.getAttribute("tantoshaCd");
            String kojyo = lotNo.substring(0, 3);
            String lotNo9 = lotNo.substring(3, 12);

            // 前工程WIPから仕掛情報を取得処理
            Map shikakariData = loadShikakariDataFromWip(queryRunnerDoc, tantoshaCd, lotNo);
            if (shikakariData == null || shikakariData.isEmpty() || !shikakariData.containsKey("oyalotedaban")) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }
            String oyalotEdaban = StringUtil.nullToBlank(getMapData(shikakariData, "oyalotedaban")); //親ﾛｯﾄ枝番

            // (6)[原材料品質DB登録実績]から、ﾃﾞｰﾀを取得
            Map fxhdd11RevInfo = loadFxhdd11RevInfo(queryRunnerDoc, kojyo, lotNo9, oyalotEdaban, paramJissekino, formId);
            if (fxhdd11RevInfo == null || fxhdd11RevInfo.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            String jotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd11RevInfo, "jotai_flg"));

            if (!(JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg) || JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg))) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合の入力項目の登録データ(仮登録時は仮登録データ)を取得
            List<SrSlipBinderkongou> srSlipBinderkongouDataList = getSrSlipBinderkongouData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo9, oyalotEdaban);
            if (srSlipBinderkongouDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // メイン画面データ設定
            setInputItemDataMainForm(processData, srSlipBinderkongouDataList.get(0));

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
     * ﾊﾞｲﾝﾀﾞｰ混合開始日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setBinderkongoukaisi(ProcessData processData) {

        FXHDD01 itemKaisiDay = getItemRow(processData.getItemList(), GXHDO102B032Const.BINDERKONGOUKAISI_DAY); // ﾊﾞｲﾝﾀﾞｰ混合開始日
        FXHDD01 itemKaisiTime = getItemRow(processData.getItemList(), GXHDO102B032Const.BINDERKONGOUKAISI_TIME); // ﾊﾞｲﾝﾀﾞｰ混合開始時間
        FXHDD01 itemBinderkongoujikan = getItemRow(processData.getItemList(), GXHDO102B032Const.BINDERKONGOUJIKAN); // ﾊﾞｲﾝﾀﾞｰ混合時間
        if (itemKaisiDay != null && itemKaisiTime != null && StringUtil.isEmpty(itemKaisiDay.getValue()) && StringUtil.isEmpty(itemKaisiTime.getValue())) {
            setDateTimeItem(itemKaisiDay, itemKaisiTime, new Date());
            // 「ﾊﾞｲﾝﾀﾞｰ混合時間」ﾁｪｯｸ
            ErrorMessageInfo checkItemErrorInfo = checkBinderkongoujikan(itemBinderkongoujikan);
            if (checkItemErrorInfo != null) {
                processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
                return processData;
            }
            //「ﾊﾞｲﾝﾀﾞｰ混合終了予定日時」計算処理
            setBinderkongousyuuryouyoteinichiji(processData, itemKaisiDay, itemKaisiTime, itemBinderkongoujikan);
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 「ﾊﾞｲﾝﾀﾞｰ混合終了予定日時」計算処理
     *
     * @param processData 処理制御データ
     * @param itemKaisiDay ﾊﾞｲﾝﾀﾞｰ混合開始日
     * @param itemKaisiTime ﾊﾞｲﾝﾀﾞｰ混合開始時間
     * @param itemBinderkongoujikan ﾊﾞｲﾝﾀﾞｰ混合時間
     * @return 処理制御データ
     */
    private void setBinderkongousyuuryouyoteinichiji(ProcessData processData, FXHDD01 itemKaisiDay, FXHDD01 itemKaisiTime, FXHDD01 itemBinderkongoujikan) {
        try {
            FXHDD01 itemKongousyuuryouyoteiDay = getItemRow(processData.getItemList(), GXHDO102B032Const.BINDERKONGOUSYUURYOUYOTEI_DAY); // ﾊﾞｲﾝﾀﾞｰ混合終了予定日
            FXHDD01 itemKongousyuuryouyoteiTime = getItemRow(processData.getItemList(), GXHDO102B032Const.BINDERKONGOUSYUURYOUYOTEI_TIME); // ﾊﾞｲﾝﾀﾞｰ混合終了予定時間
            BigDecimal itemBinderkongoujikanKikakuChi = ValidateUtil.numberExtraction(StringUtil.nullToBlank(itemBinderkongoujikan.getKikakuChi()).replace("【", "").replace("】", ""));
            // (ﾊﾞｲﾝﾀﾞｰ混合開始日 + ﾊﾞｲﾝﾀﾞｰ混合開始時間) + ﾊﾞｲﾝﾀﾞｰ混合時間で算出した日時
            Date dateTime = DateUtil.addJikan(itemKaisiDay.getValue(), itemKaisiTime.getValue(), itemBinderkongoujikanKikakuChi.intValue(), Calendar.MINUTE);
            if (dateTime != null) {
                // ﾊﾞｲﾝﾀﾞｰ混合終了予定日の設定
                itemKongousyuuryouyoteiDay.setValue(new SimpleDateFormat("yyMMddHHmm").format(dateTime).substring(0, 6));
                // ﾊﾞｲﾝﾀﾞｰ混合終了予定時間の設定
                itemKongousyuuryouyoteiTime.setValue(new SimpleDateFormat("yyMMddHHmm").format(dateTime).substring(6, 10));
            }
        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
            ErrUtil.outputErrorLog("ﾊﾞｲﾝﾀﾞｰ混合終了予定日時計算にエラー発生", ex, LOGGER);
        }
    }

    /**
     * 【 ﾊﾞｲﾝﾀﾞｰ混合開始日時】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
     *
     * @param itemBinderkongoujikan ﾊﾞｲﾝﾀﾞｰ混合時間
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkBinderkongoujikan(FXHDD01 itemBinderkongoujikan) {

        BigDecimal itemBinderkongoujikanVal = ValidateUtil.getItemKikakuChiCheckVal(itemBinderkongoujikan);// ﾊﾞｲﾝﾀﾞｰ混合時間の規格値
        // 「ﾊﾞｲﾝﾀﾞｰ混合時間」ﾁｪｯｸ
        if (itemBinderkongoujikanVal == null) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemBinderkongoujikan);
            itemBinderkongoujikan.setBackColor3(ErrUtil.ERR_BACK_COLOR);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, true, errFxhdd01List, itemBinderkongoujikan.getLabel1());
        }
        return null;
    }

    /**
     * 1ﾛｯﾄ当たりｽﾗﾘｰ重量計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     * @throws SQLException 例外エラー
     */
    public ProcessData setIchilotatarislurryjyuuryou(ProcessData processData) throws SQLException {
        QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
        QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String lotNo = (String) session.getAttribute("lotNo");
        int jissekiNo = (Integer) session.getAttribute("jissekino");
        String formId = StringUtil.nullToBlank(session.getAttribute("formId"));
        String tantoshaCd = (String) session.getAttribute("tantoshaCd");
        //  1ﾛｯﾄ当たりｽﾗﾘｰ重量ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkIchilotatarislurryjyuuryou(processData, queryRunnerDoc, queryRunnerQcdb, lotNo, jissekiNo, formId, tantoshaCd);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 1ﾛｯﾄ当たりｽﾗﾘｰ重量ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
     *
     * @param processData 処理制御データ
     * @param queryRunnerDoc QueryRunnerオブジェクト(DocServer)
     * @param queryRunnerQcdb QueryRunnerオブジェクト(Qcdb)
     * @param lotNo ﾛｯﾄNo
     * @param jissekiNo 実績No(検索キー)
     * @param formId 画面ID(検索キー)
     * @param tantoshaCd 担当者ｺｰﾄﾞ
     * @return エラーメッセージ情報
     * @throws SQLException 例外エラー
     */
    public ErrorMessageInfo checkIchilotatarislurryjyuuryou(ProcessData processData, QueryRunner queryRunnerDoc, QueryRunner queryRunnerQcdb, String lotNo,
            int jissekiNo, String formId, String tantoshaCd) throws SQLException {
        String kojyo = lotNo.substring(0, 3);
        String lotNo9 = lotNo.substring(3, 12);
        String edaban = lotNo.substring(12, 15);
        String slurrygoukeijyuuryouVal;// ｽﾗﾘｰ合計重量
        String tyougouryou1Val;// 誘電体ｽﾗﾘｰ_調合量1
        String tyougouryou2Val;// 誘電体ｽﾗﾘｰ_調合量2
        String kikakuti;// 規格値
        // 1ﾛｯﾄ当たりｽﾗﾘｰ重量
        FXHDD01 lotatarislurryjyuuryou = getItemRow(processData.getItemList(), GXHDO102B032Const.ICHILOTATARISLURRYJYUURYOU);
        // [原材料品質DB登録実績]から、ﾃﾞｰﾀを取得
        Map fxhdd11RevInfo = loadFxhdd11RevInfo(queryRunnerDoc, kojyo, lotNo9, edaban, jissekiNo, formId);
        String rev = StringUtil.nullToBlank(getMapData(fxhdd11RevInfo, "rev"));
        // 前工程WIPから仕掛情報を取得処理
        Map shikakariData = loadShikakariDataFromWip(queryRunnerDoc, tantoshaCd, lotNo);
        // [ﾊﾟﾗﾒｰﾀﾏｽﾀ]から、ﾃﾞｰﾀを取得
        Map fxhbm03Data = loadFxhbm03Data(queryRunnerDoc, "ｽﾘｯﾌﾟ作製_ﾊﾞｲﾝﾀﾞｰ混合_重量計算_規定値");
        if (fxhbm03Data == null || fxhbm03Data.isEmpty()) {
            return MessageUtil.getErrorMessageInfo("XHD-000014", false, false, new ArrayList<>(), "");
        }
        // [前工程設計]から、ﾃﾞｰﾀを取得                                                                         
        Map daMkSekKeiData = loadDaMkSekKeiData(queryRunnerQcdb, kojyo, lotNo9, edaban, "ｽﾘｯﾌﾟ作製");
        if (daMkSekKeiData == null || daMkSekKeiData.isEmpty()) {
            return MessageUtil.getErrorMessageInfo("XHD-000014", false, false, new ArrayList<>(), "");
        }
        // 設計No
        String sekkeiNo = StringUtil.nullToBlank(getMapData(daMkSekKeiData, "sekkeiNo"));
        // ﾊﾟﾀｰﾝ
        String pattern = StringUtil.nullToBlank(getMapData(daMkSekKeiData, "pattern"));
        // ﾊﾟﾗﾒｰﾀﾃﾞｰﾀ
        String data = StringUtil.nullToBlank(getMapData(fxhbm03Data, "data"));
        String[] dataSplitList = data.split(",");
        // [前工程規格情報]から、ﾃﾞｰﾀを取得
        Map daMkJokenData = loadDaMkJokenData(queryRunnerQcdb, sekkeiNo, dataSplitList);
        if (daMkJokenData == null || daMkJokenData.isEmpty()) {
            // [前工程標準規格情報]から、ﾃﾞｰﾀを取得
            Map daMkhYoJunJokenData = loadDaMkhYoJunJokenData(queryRunnerQcdb, (String) shikakariData.get("hinmei"), pattern, "ｽﾘｯﾌﾟ作製");
            if (daMkhYoJunJokenData == null || daMkhYoJunJokenData.isEmpty()) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                return MessageUtil.getErrorMessageInfo("XHD-000028", false, false, new ArrayList<>(), "規格情報");
            }
            // 前工程規格情報の規格値
            kikakuti = StringUtil.nullToBlank(getMapData(daMkhYoJunJokenData, "kikakuti"));
        } else {
            // 前工程規格情報の規格値
            kikakuti = StringUtil.nullToBlank(getMapData(daMkJokenData, "kikakuti"));
        }
        BigDecimal itemKikakuChi = ValidateUtil.numberExtraction(StringUtil.nullToBlank(kikakuti).replace("【", "").replace("】", ""));
        // [ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)]から、ﾃﾞｰﾀを取得
        Map slurrykokeibuntyouseiData = loadSlurrykokeibuntyousei(queryRunnerQcdb, kojyo, lotNo9, edaban, rev);
        slurrygoukeijyuuryouVal = StringUtil.nullToBlank(getMapData(slurrykokeibuntyouseiData, "slurrygoukeijyuuryou"));
        if (slurrykokeibuntyouseiData == null || slurrykokeibuntyouseiData.isEmpty() || StringUtil.isEmpty(slurrygoukeijyuuryouVal)) {
            // [ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)]から、ﾃﾞｰﾀを取得
            Map tounyuusutenyoukiData = loadTounyuusutenyouki(queryRunnerQcdb, kojyo, lotNo9, edaban, rev);
            tyougouryou1Val = StringUtil.nullToBlank(getMapData(tounyuusutenyoukiData, "yuudentaislurry_tyougouryou1"));
            tyougouryou2Val = StringUtil.nullToBlank(getMapData(tounyuusutenyoukiData, "yuudentaislurry_tyougouryou2"));
            if (tounyuusutenyoukiData == null || tounyuusutenyoukiData.isEmpty() || StringUtil.isEmpty(tyougouryou1Val) || StringUtil.isEmpty(tyougouryou2Val)) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                return MessageUtil.getErrorMessageInfo("XHD-000210", false, false, new ArrayList<>(), "ﾃﾞｰﾀ");
            }
            // ｽﾃﾝ容器の計算
            calcSutenyouki(lotatarislurryjyuuryou, itemKikakuChi, tyougouryou1Val, tyougouryou2Val);
        } else {
            // 白ﾎﾟﾘの計算
            calcSiropori(lotatarislurryjyuuryou, itemKikakuChi, slurrygoukeijyuuryouVal);
        }

        return null;
    }

    /**
     * 1ﾛｯﾄ当たりｽﾗﾘｰ重量-ｽﾃﾝ容器の計算
     *
     * @param lotatarislurryjyuuryou 1ﾛｯﾄ当たりｽﾗﾘｰ重量
     * @param kikakuti 規格値
     * @param tyougouryou1Val 誘電体ｽﾗﾘｰ_調合量1
     * @param tyougouryou2Val 誘電体ｽﾗﾘｰ_調合量1
     */
    private void calcSutenyouki(FXHDD01 lotatarislurryjyuuryou, BigDecimal kikakuti, String tyougouryou1Val, String tyougouryou2Val) {
        try {
            BigDecimal itemTyougouryou1Val = new BigDecimal(Integer.parseInt(tyougouryou1Val));
            BigDecimal itemTyougouryou2Val = new BigDecimal(Integer.parseInt(tyougouryou2Val));

            // 1ﾛｯﾄ当たりｽﾗﾘｰ重量 = (誘電体ｽﾗﾘｰ_調合量1 + 誘電体ｽﾗﾘｰ_調合量2) ÷ 取得の規格値(小数以下四捨五入)
            BigDecimal lotatarislurryjyuuryouVal = (itemTyougouryou1Val.add(itemTyougouryou2Val)).divide(kikakuti, 0, RoundingMode.HALF_UP);
            // 計算結果の設定
            lotatarislurryjyuuryou.setValue(lotatarislurryjyuuryouVal.toPlainString());

        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
            ErrUtil.outputErrorLog("1ﾛｯﾄ当たりｽﾗﾘｰ重量計算にエラー発生", ex, LOGGER);
        }
    }

    /**
     * 1ﾛｯﾄ当たりｽﾗﾘｰ重量-白ﾎﾟﾘの計算
     *
     * @param lotatarislurryjyuuryou 1ﾛｯﾄ当たりｽﾗﾘｰ重量
     * @param kikakuti 規格値
     * @param slurrygoukeijyuuryouVal ｽﾗﾘｰ合計重量
     */
    private void calcSiropori(FXHDD01 lotatarislurryjyuuryou, BigDecimal kikakuti, String slurrygoukeijyuuryouVal) {
        try {
            BigDecimal itemSlurrygoukeijyuuryouVal = new BigDecimal(Integer.parseInt(slurrygoukeijyuuryouVal));
            // 1ﾛｯﾄ当たりｽﾗﾘｰ重量 = ｽﾗﾘｰ合計重量 ÷ 取得の規格値(小数以下四捨五入)
            BigDecimal lotatarislurryjyuuryouVal = itemSlurrygoukeijyuuryouVal.divide(kikakuti, 0, RoundingMode.HALF_UP);
            // 計算結果の設定
            lotatarislurryjyuuryou.setValue(lotatarislurryjyuuryouVal.toPlainString());

        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
            ErrUtil.outputErrorLog("1ﾛｯﾄ当たりｽﾗﾘｰ重量計算にエラー発生", ex, LOGGER);
        }
    }

    /**
     * ｽﾘｯﾌﾟ予定重量計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     * @throws SQLException 例外エラー
     */
    public ProcessData setSlipyoteijyuuryou(ProcessData processData) throws SQLException {
        QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
        QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String lotNo = (String) session.getAttribute("lotNo"); // ﾛｯﾄNo
        int jissekiNo = (Integer) session.getAttribute("jissekino");// 実績No
        String formId = StringUtil.nullToBlank(session.getAttribute("formId"));// 画面ID 
        String tantoshaCd = (String) session.getAttribute("tantoshaCd");// 担当者ｺｰﾄﾞ
        //  ｽﾘｯﾌﾟ予定重量ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkSlipyoteijyuuryou(processData, queryRunnerDoc, queryRunnerQcdb, lotNo, jissekiNo, formId, tantoshaCd);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * ｽﾘｯﾌﾟ予定重量計算ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
     *
     * @param processData 処理制御データ
     * @param queryRunnerDoc QueryRunnerオブジェクト(DocServer)
     * @param queryRunnerQcdb QueryRunnerオブジェクト(Qcdb)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param jissekiNo 実績No(検索キー)
     * @param formId 画面ID(検索キー)
     * @param tantoshaCd 担当者ｺｰﾄﾞ
     * @return エラーメッセージ情報
     * @throws SQLException 例外エラー
     */
    public ErrorMessageInfo checkSlipyoteijyuuryou(ProcessData processData, QueryRunner queryRunnerDoc, QueryRunner queryRunnerQcdb, String lotNo,
            int jissekiNo, String formId, String tantoshaCd) throws SQLException {
        String kojyo = lotNo.substring(0, 3);
        String lotNo9 = lotNo.substring(3, 12);
        String edaban = lotNo.substring(12, 15);
        // ｽﾘｯﾌﾟ予定重量
        FXHDD01 slipyoteijyuuryou = getItemRow(processData.getItemList(), GXHDO102B032Const.SLIPYOTEIJYUURYOU);
        // [原材料品質DB登録実績]から、ﾃﾞｰﾀを取得
        Map fxhdd11RevInfo = loadFxhdd11RevInfo(queryRunnerDoc, kojyo, lotNo9, edaban, jissekiNo, formId);
        String rev = StringUtil.nullToBlank(getMapData(fxhdd11RevInfo, "rev"));
        // (15)[ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)]から、ﾃﾞｰﾀを取得
        Map slurrykokeibuntyouseiData = loadSlurrykokeibuntyousei(queryRunnerQcdb, kojyo, lotNo9, edaban, rev);
        // (18)[ｽﾘｯﾌﾟ作製・溶剤秤量・投入(白ﾎﾟﾘ)]から、ﾃﾞｰﾀを取得
        Map tounyuuSiroporiData = loadTounyuuSiropori(queryRunnerQcdb, kojyo, lotNo9, edaban, rev);
        if (slurrykokeibuntyouseiData != null && tounyuuSiroporiData != null) {
            // 白ﾎﾟﾘの計算
            calcSlipyoteijyuuryouSiropori(slipyoteijyuuryou, slurrykokeibuntyouseiData, tounyuuSiroporiData);
        } else {
            // (14)[ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)]から、ﾃﾞｰﾀを取得
            Map tounyuusutenyoukiData = loadTounyuusutenyouki(queryRunnerQcdb, kojyo, lotNo9, edaban, rev);
            // (16)[ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入]から、ﾃﾞｰﾀを取得
            Map binderhyouryouTounyuuData = loadBinderhyouryouTounyuu(queryRunnerQcdb, kojyo, lotNo9, edaban, rev);
            // (17)[ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)]から、ﾃﾞｰﾀを取得
            Map sutenyoukiData = loadSutenyouki(queryRunnerQcdb, kojyo, lotNo9, edaban, rev);
            if (tounyuusutenyoukiData != null && binderhyouryouTounyuuData != null && sutenyoukiData != null) {
                // ｽﾃﾝ容器の計算
                calcSlipyoteijyuuryouSutenyouki(slipyoteijyuuryou, tounyuusutenyoukiData, binderhyouryouTounyuuData, sutenyoukiData);
            } else {
                // ｴﾗｰ項目をﾘｽﾄに追加
                return MessageUtil.getErrorMessageInfo("XHD-000210", false, false, new ArrayList<>(), "ﾃﾞｰﾀ");
            }
        }

        return null;
    }

    /**
     * ｽﾘｯﾌﾟ予定重量-ｽﾃﾝ容器の計算
     *
     * @param slipyoteijyuuryou ｽﾘｯﾌﾟ予定重量
     * @param tounyuusutenyoukiData ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)データ
     * @param binderhyouryouTounyuuData ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入データ
     * @param sutenyoukiData ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)データ
     */
    private void calcSlipyoteijyuuryouSutenyouki(FXHDD01 slipyoteijyuuryou, Map tounyuusutenyoukiData, Map binderhyouryouTounyuuData, Map sutenyoukiData) {
        try {
            String yuudentaislurryTyougouryou1 = StringUtil.nullToBlank(getMapData(tounyuusutenyoukiData, "yuudentaislurry_tyougouryou1"));// 誘電体ｽﾗﾘｰ_調合量1
            String tyougouryou2 = StringUtil.nullToBlank(getMapData(tounyuusutenyoukiData, "yuudentaislurry_tyougouryou2"));// 誘電体ｽﾗﾘｰ_調合量2
            String toluenetyouseiryou = StringUtil.nullToBlank(getMapData(tounyuusutenyoukiData, "toluenetyouseiryou"));// ﾄﾙｴﾝ調整量
            String zunsanzai1Tyougouryou1 = StringUtil.nullToBlank(getMapData(tounyuusutenyoukiData, "zunsanzai1_tyougouryou1"));// 分散材①_調合量1
            String zunsanzai1Tyougouryou2 = StringUtil.nullToBlank(getMapData(tounyuusutenyoukiData, "zunsanzai1_tyougouryou2"));// 分散材①_調合量2
            String zunsanzai2Tyougouryou1 = StringUtil.nullToBlank(getMapData(tounyuusutenyoukiData, "zunsanzai2_tyougouryou1"));// 分散材②_調合量1
            String zunsanzai2Tyougouryou2 = StringUtil.nullToBlank(getMapData(tounyuusutenyoukiData, "zunsanzai2_tyougouryou2"));// 分散材②_調合量2
            String bindertenkaryoukikaku = StringUtil.nullToBlank(getMapData(binderhyouryouTounyuuData, "bindertenkaryoukikaku"));// ﾊﾞｲﾝﾀﾞｰ添加量規格
            String youzaityouseiryou = StringUtil.nullToBlank(getMapData(sutenyoukiData, "youzaityouseiryou"));// 溶剤調整量
            BigDecimal itemYuudentaislurryTyougouryou1Val = new BigDecimal(Integer.parseInt(yuudentaislurryTyougouryou1));
            BigDecimal itemTyougouryou2Val = new BigDecimal(Integer.parseInt(tyougouryou2));
            BigDecimal itemToluenetyouseiryouVal = new BigDecimal(Integer.parseInt(toluenetyouseiryou));
            BigDecimal itemZunsanzai1Tyougouryou1Val = new BigDecimal(Integer.parseInt(zunsanzai1Tyougouryou1));
            BigDecimal itemZunsanzai1Tyougouryou2Val = new BigDecimal(Integer.parseInt(zunsanzai1Tyougouryou2));
            BigDecimal itemZunsanzai2Tyougouryou1Val = new BigDecimal(Integer.parseInt(zunsanzai2Tyougouryou1));
            BigDecimal itemZunsanzai2Tyougouryou2Val = new BigDecimal(Integer.parseInt(zunsanzai2Tyougouryou2));
            BigDecimal itemBindertenkaryoukikakuVal = ValidateUtil.numberExtraction(StringUtil.nullToBlank(bindertenkaryoukikaku).replace("【", "").replace("】", ""));
            BigDecimal itemYouzaityouseiryouVal = new BigDecimal(Integer.parseInt(youzaityouseiryou));
            BigDecimal slipyoteijyuuryouVal = itemYuudentaislurryTyougouryou1Val.add(itemTyougouryou2Val).add(itemToluenetyouseiryouVal).add(itemZunsanzai1Tyougouryou1Val)
                    .add(itemZunsanzai1Tyougouryou2Val).add(itemZunsanzai2Tyougouryou1Val).add(itemZunsanzai2Tyougouryou2Val).add(itemBindertenkaryoukikakuVal).add(itemYouzaityouseiryouVal);
            // 計算結果の設定
            slipyoteijyuuryou.setValue(slipyoteijyuuryouVal.toPlainString());

        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
            ErrUtil.outputErrorLog("ｽﾘｯﾌﾟ予定重量計算にエラー発生", ex, LOGGER);
        }
    }

    /**
     * ｽﾘｯﾌﾟ予定重量-白ﾎﾟﾘの計算
     *
     * @param slipyoteijyuuryou ｽﾘｯﾌﾟ予定重量
     * @param slurrykokeibuntyouseiData ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)データ
     * @param tounyuuSiroporiData ｽﾘｯﾌﾟ作製・溶剤秤量・投入(白ﾎﾟﾘ)データ
     */
    private void calcSlipyoteijyuuryouSiropori(FXHDD01 slipyoteijyuuryou, Map slurrykokeibuntyouseiData, Map tounyuuSiroporiData) {
        try {
            String slurrygoukeijyuuryou = StringUtil.nullToBlank(getMapData(slurrykokeibuntyouseiData, "slurrygoukeijyuuryou"));// ｽﾗﾘｰ合計重量
            String kokeibuntyouseiryou2 = StringUtil.nullToBlank(getMapData(slurrykokeibuntyouseiData, "kokeibuntyouseiryou2"));// 固形分調整量➁
            String youzai1Tyougouryou1 = StringUtil.nullToBlank(getMapData(tounyuuSiroporiData, "youzai1_tyougouryou1"));// 溶剤①_調合量1
            String youzai1Tyougouryou2 = StringUtil.nullToBlank(getMapData(tounyuuSiroporiData, "youzai1_tyougouryou2"));// 溶剤①_調合量2
            String youzai2Tyougouryou1 = StringUtil.nullToBlank(getMapData(tounyuuSiroporiData, "youzai2_tyougouryou1"));// 溶剤②_調合量1
            String youzai2Tyougouryou2 = StringUtil.nullToBlank(getMapData(tounyuuSiroporiData, "youzai2_tyougouryou2"));// 溶剤②_調合量2
            String youzai3Tyougouryou1 = StringUtil.nullToBlank(getMapData(tounyuuSiroporiData, "youzai3_tyougouryou1"));// 溶剤③_調合量1
            String youzai3Tyougouryou2 = StringUtil.nullToBlank(getMapData(tounyuuSiroporiData, "youzai3_tyougouryou2"));// 溶剤③_調合量2
            BigDecimal itemSlurrygoukeijyuuryouVal = new BigDecimal(Integer.parseInt(slurrygoukeijyuuryou));
            BigDecimal itemKokeibuntyouseiryou2Val = new BigDecimal(Integer.parseInt(kokeibuntyouseiryou2));
            BigDecimal itemYouzai1Tyougouryou1Val = new BigDecimal(Integer.parseInt(youzai1Tyougouryou1));
            BigDecimal itemYouzai1Tyougouryou2Val = new BigDecimal(Integer.parseInt(youzai1Tyougouryou2));
            BigDecimal itemYouzai2Tyougouryou1Val = new BigDecimal(Integer.parseInt(youzai2Tyougouryou1));
            BigDecimal itemYouzai2Tyougouryou2Val = new BigDecimal(Integer.parseInt(youzai2Tyougouryou2));
            BigDecimal itemYouzai3Tyougouryou1Val = new BigDecimal(Integer.parseInt(youzai3Tyougouryou1));
            BigDecimal itemYouzai3Tyougouryou2Val = new BigDecimal(Integer.parseInt(youzai3Tyougouryou2));
            // ｽﾘｯﾌﾟ予定重量の桁数
            BigDecimal slipyoteijyuuryouVal = itemSlurrygoukeijyuuryouVal.add(itemKokeibuntyouseiryou2Val).add(itemYouzai1Tyougouryou1Val).add(itemYouzai1Tyougouryou2Val).add(itemYouzai2Tyougouryou1Val)
                    .add(itemYouzai2Tyougouryou2Val).add(itemYouzai3Tyougouryou1Val).add(itemYouzai3Tyougouryou2Val);
            // 計算結果の設定
            slipyoteijyuuryou.setValue(slipyoteijyuuryouVal.toPlainString());

        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
            ErrUtil.outputErrorLog("ｽﾘｯﾌﾟ予定重量計算にエラー発生", ex, LOGGER);
        }
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合終了日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setBinderkongousyuuryou(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B032Const.BINDERKONGOUSYUURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B032Const.BINDERKONGOUSYUURYOU_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 仮登録処理(データチェック処理)
     *
     * @param processData 処理データ
     * @return 処理データ
     */
    public ProcessData checkDataTempRegist(ProcessData processData) {
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
            int jissekiNo = (Integer) session.getAttribute("jissekino");
            String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
            String lotNo9 = lotNo.substring(3, 12); //ﾛｯﾄNo(9桁)
            String edaban = lotNo.substring(12, 15); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // 原材料品質DB登録実績データ取得
            Map fxhdd11RevInfo = loadFxhdd11RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo9, edaban, jissekiNo, formId);
            ErrorMessageInfo checkRevMessageInfo = checkRevision(processData, fxhdd11RevInfo);
            // リビジョンエラー時はリターン
            if (checkRevMessageInfo != null) {
                processData.setErrorMessageInfoList(Arrays.asList(checkRevMessageInfo));
                // コネクションロールバック処理
                DBUtil.rollbackConnection(conDoc, LOGGER);
                DBUtil.rollbackConnection(conQcdb, LOGGER);
                return processData;
            }

            BigDecimal newRev = BigDecimal.ONE;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            String strSystime = sdf.format(systemTime);

            BigDecimal rev = BigDecimal.ZERO;
            if (StringUtil.isEmpty(processData.getInitJotaiFlg())) {
                // 原材料品質DB登録実績登録処理
                insertFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo9, edaban, jissekiNo, JOTAI_FLG_KARI_TOROKU, systemTime);
            } else {
                rev = new BigDecimal(processData.getInitRev());
                // 最新のリビジョンを採番
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo9, edaban, jissekiNo, formId);

                // 原材料品質DB登録実績更新処理
                updateFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo9, edaban, JOTAI_FLG_KARI_TOROKU, systemTime, jissekiNo);
            }

            if (StringUtil.isEmpty(processData.getInitJotaiFlg()) || JOTAI_FLG_SAKUJO.equals(processData.getInitJotaiFlg())) {

                // ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合_仮登録登録処理
                insertTmpSrSlipBinderkongou(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, strSystime, processData);
            } else {

                // ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合_仮登録更新処理
                updateTmpSrSlipBinderkongou(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);
            }

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert102B(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, jissekiNo, "0", kikakuError.getKikakuchiInputErrorInfoList());
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
            processData.getItemList().forEach((fxhdd01) -> {
                fxhdd01.setBackColorInput(fxhdd01.getBackColorInputDefault());
            });

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

        ErrorMessageInfo errorMessageInfo = checkInputKikakuchi(processData, kikakuchiInputErrorInfoList);
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
            int jissekiNo = (Integer) session.getAttribute("jissekino");
            String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
            String lotNo9 = lotNo.substring(3, 12); //ﾛｯﾄNo(9桁)
            String edaban = lotNo.substring(12, 15); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // 原材料品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd11RevInfo = loadFxhdd11RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo9, edaban, jissekiNo, formId);
            ErrorMessageInfo checkRevMessageInfo = checkRevision(processData, fxhdd11RevInfo);
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
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            String strSystime = sdf.format(systemTime);

            if (StringUtil.isEmpty(processData.getInitRev())) {
                // 原材料品質DB登録実績登録処理
                insertFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo9, edaban, jissekiNo, JOTAI_FLG_TOROKUZUMI, systemTime);
            } else {
                rev = new BigDecimal(processData.getInitRev());
                // 最新のリビジョンを採番
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo9, edaban, jissekiNo, formId);

                // 原材料品質DB登録実績更新処理
                updateFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo9, edaban, JOTAI_FLG_TOROKUZUMI, systemTime, jissekiNo);
            }

            // 仮登録状態の場合、仮登録のデータを削除する。
            SrSlipBinderkongou tmpSrSlipBinderkongou = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrSlipBinderkongou> srSlipBinderkongouList = getSrSlipBinderkongouData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo9, edaban);
                if (!srSlipBinderkongouList.isEmpty()) {
                    tmpSrSlipBinderkongou = srSlipBinderkongouList.get(0);
                }

                deleteTmpSrSlipBinderkongou(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);
            }

            // ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合_登録処理
            insertSrSlipBinderkongou(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, strSystime, processData, tmpSrSlipBinderkongou);

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert102B(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, jissekiNo, "0", kikakuError.getKikakuchiInputErrorInfoList());
            }
            // 処理後はエラーリストをクリア
            kikakuError.setKikakuchiInputErrorInfoList(new ArrayList<>());

            DbUtils.commitAndCloseQuietly(conDoc);
            DbUtils.commitAndCloseQuietly(conQcdb);

            // 後続処理メソッド設定
            processData.setMethod("");

            // 完了メッセージとコールバックパラメータを設定
            setCompMessage("登録しました。");
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
        ErrorMessageInfo errorMessageInfo = checkInputKikakuchi(processData, kikakuchiInputErrorInfoList);

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
        processData.setMethod("doCorrectKakunin");

        return processData;

    }

    /**
     * 修正処理時の確認メッセージ表示
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doCorrectKakunin(ProcessData processData) {

        // 警告メッセージの設定
        processData.setWarnMessage("修正します。よろしいですか？");

        // ユーザ認証用のパラメータをセットする。
        processData.setRquireAuth(true);
        processData.setUserAuthParam(GXHDO102B032Const.USER_AUTH_UPDATE_PARAM);

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
            int jissekiNo = (Integer) session.getAttribute("jissekino");
            String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
            String lotNo9 = lotNo.substring(3, 12); //ﾛｯﾄNo(9桁)
            String edaban = lotNo.substring(12, 15); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // 原材料品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd11RevInfo = loadFxhdd11RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo9, edaban, jissekiNo, formId);
            ErrorMessageInfo checkRevMessageInfo = checkRevision(processData, fxhdd11RevInfo);
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
            BigDecimal newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo9, edaban, jissekiNo, formId);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            String strSystime = sdf.format(systemTime);
            // 原材料品質DB登録実績更新処理
            updateFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo9, edaban, JOTAI_FLG_TOROKUZUMI, systemTime, jissekiNo);

            // ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合_更新処理
            updateSrSlipBinderkongou(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert102B(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, jissekiNo, "0", kikakuError.getKikakuchiInputErrorInfoList());
            }
            // 処理後はエラーリストをクリア
            kikakuError.setKikakuchiInputErrorInfoList(new ArrayList<>());

            DbUtils.commitAndCloseQuietly(conDoc);
            DbUtils.commitAndCloseQuietly(conQcdb);

            // 後続処理メソッド設定
            processData.setMethod("");

            // 完了メッセージとコールバックパラメータを設定
            setCompMessage("修正しました。");

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
     * 登録・修正項目チェック
     *
     * @param processData 処理データ
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkItemRegistCorrect(ProcessData processData) {

        ValidateUtil validateUtil = new ValidateUtil();
        FXHDD01 itemBinderkongoukaisiDay = getItemRow(processData.getItemList(), GXHDO102B032Const.BINDERKONGOUKAISI_DAY);  // ﾊﾞｲﾝﾀﾞｰ混合開始日
        FXHDD01 itemBinderkongoukaisiTime = getItemRow(processData.getItemList(), GXHDO102B032Const.BINDERKONGOUKAISI_TIME); // ﾊﾞｲﾝﾀﾞｰ混合開始時間
        FXHDD01 itemBinderkongousyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B032Const.BINDERKONGOUSYUURYOU_DAY); // ﾊﾞｲﾝﾀﾞｰ混合撹拌終了日
        FXHDD01 itemBinderkongousyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B032Const.BINDERKONGOUSYUURYOU_TIME); // ﾊﾞｲﾝﾀﾞｰ混合終了時間
        if (itemBinderkongoukaisiDay != null && itemBinderkongoukaisiTime != null && itemBinderkongousyuuryouDay != null && itemBinderkongousyuuryouTime != null) {
            // 画面.ﾊﾞｲﾝﾀﾞｰ混合開始日 + 画面.ﾊﾞｲﾝﾀﾞｰ混合開始時間
            Date binderkongoukaisiDate = DateUtil.convertStringToDate(itemBinderkongoukaisiDay.getValue(), itemBinderkongoukaisiTime.getValue());
            // 画面.ﾊﾞｲﾝﾀﾞｰ混合終了日 + 画面.ﾊﾞｲﾝﾀﾞｰ混合終了時間
            Date binderkongousyuuryouDate = DateUtil.convertStringToDate(itemBinderkongousyuuryouDay.getValue(), itemBinderkongousyuuryouTime.getValue());
            // R001チェック呼出し
            String msgBinderkongoukaisiCheckR001 = validateUtil.checkR001("ﾊﾞｲﾝﾀﾞｰ混合開始日時", binderkongoukaisiDate, "ﾊﾞｲﾝﾀﾞｰ混合終了日時", binderkongousyuuryouDate);
            if (!StringUtil.isEmpty(msgBinderkongoukaisiCheckR001)) {
                // エラー発生時
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemBinderkongoukaisiDay, itemBinderkongoukaisiTime, itemBinderkongousyuuryouDay, itemBinderkongousyuuryouTime);
                return MessageUtil.getErrorMessageInfo("", msgBinderkongoukaisiCheckR001, true, true, errFxhdd01List);
            }
        }
        return null;
    }

    /**
     * 規格値の入力値チェックを行う。
     * 規格値のエラー対象は引数のリスト(kikakuchiInputErrorInfoList)に項目情報を詰めて返される。
     *
     * @param processData 処理データ
     * @param kikakuchiInputErrorInfoList 規格値入力エラー情報リスト
     * @return チェックの正常終了時はNULL、異常時は内容に応じたエラーメッセージ情報を返す。
     */
    private ErrorMessageInfo checkInputKikakuchi(ProcessData processData, List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList) {

        // ﾊﾞｲﾝﾀﾞｰ混合時間ﾁｪｯｸ
        List<String> kakuhankaisiList = Arrays.asList(GXHDO102B032Const.BINDERKONGOUKAISI_DAY, GXHDO102B032Const.BINDERKONGOUKAISI_TIME);
        List<String> kakuhansyuuryouList = Arrays.asList(GXHDO102B032Const.BINDERKONGOUSYUURYOU_DAY, GXHDO102B032Const.BINDERKONGOUSYUURYOU_TIME);
        // 規格値の入力値チェック必要の項目リスト
        List<FXHDD01> itemList = new ArrayList<>();
        // ﾊﾞｲﾝﾀﾞｰ混合時間の時間差数
        FXHDD01 kongoujikanDiffMinutes = getDiffMinutes(processData, GXHDO102B032Const.BINDERKONGOUJIKAN, kakuhankaisiList, kakuhansyuuryouList);
        // 項目の項目名を設置
        if (kongoujikanDiffMinutes != null) {
            kongoujikanDiffMinutes.setLabel1("ﾊﾞｲﾝﾀﾞｰ混合時間");
            itemList.add(kongoujikanDiffMinutes);
        }
        processData.getItemList().stream().filter(
                (fxhdd01) -> !(StringUtil.isEmpty(fxhdd01.getStandardPattern()) || "【-】".equals(fxhdd01.getKikakuChi()))).filter(
                        (fxhdd01) -> !(!ValidateUtil.isInputColumn(fxhdd01) || StringUtil.isEmpty(fxhdd01.getValue()))).forEachOrdered(
                        (fxhdd01) -> {
                            // 規格チェックの対象項目である、かつ入力項目かつ入力値がある項目はリストに追加
                            itemList.add(fxhdd01);
                        });
        ErrorMessageInfo errorMessageInfo = ValidateUtil.checkInputKikakuchi(itemList, kikakuchiInputErrorInfoList);
        // エラー項目の背景色を設定
        setItemBackColor(processData, kakuhankaisiList, kakuhansyuuryouList, "ﾊﾞｲﾝﾀﾞｰ混合時間", kikakuchiInputErrorInfoList, errorMessageInfo);

        return errorMessageInfo;
    }

    /**
     * 取得時間差数は、設定した規格値と比較
     *
     * @param processData 処理データ
     * @param kikakuItem 規格値項目名
     * @param kakuhankaisiList 開始時間項目リスト
     * @param kakuhansyuuryouList 終了時間項目リスト
     */
    private FXHDD01 getDiffMinutes(ProcessData processData, String kikakuItem, List<String> kakuhankaisiList, List<String> kakuhansyuuryouList) {

        FXHDD01 kikakuFxhdd01 = getItemRow(processData.getItemList(), kikakuItem);
        if (kikakuFxhdd01 == null) {
            return null;
        }
        int diffHours;
        Date kaishijikan = null;
        Date syuuryoujikan = null;
        FXHDD01 itemFxhdd01Clone = null;
        try {
            // 開始日
            FXHDD01 itemKakuhankaisiDay = getItemRow(processData.getItemList(), kakuhankaisiList.get(0));
            // 開始時間
            FXHDD01 itemKakuhankaisiTime = getItemRow(processData.getItemList(), kakuhankaisiList.get(1));
            // 終了日
            FXHDD01 itemKakuhansyuuryouDay = getItemRow(processData.getItemList(), kakuhansyuuryouList.get(0));
            // 終了時間
            FXHDD01 itemKakuhansyuuryouTime = getItemRow(processData.getItemList(), kakuhansyuuryouList.get(1));
            if (DateUtil.isValidYYMMDD(itemKakuhankaisiDay.getValue()) && DateUtil.isValidHHMM(itemKakuhankaisiTime.getValue())) {
                kaishijikan = DateUtil.convertStringToDate(itemKakuhankaisiDay.getValue(), itemKakuhankaisiTime.getValue());
            }
            if (DateUtil.isValidYYMMDD(itemKakuhansyuuryouDay.getValue()) && DateUtil.isValidHHMM(itemKakuhansyuuryouTime.getValue())) {
                syuuryoujikan = DateUtil.convertStringToDate(itemKakuhansyuuryouDay.getValue(), itemKakuhansyuuryouTime.getValue());
            }
            if (kaishijikan == null || syuuryoujikan == null) {
                return null;
            }
            // 時間を利用するため、該当項目をCloneする
            if (itemFxhdd01Clone == null) {
                itemFxhdd01Clone = kikakuFxhdd01.clone();
            }
            // 日付の差分分数の数取得処理
            diffHours = DateUtil.diffMinutes(kaishijikan, syuuryoujikan);
        } catch (CloneNotSupportedException ex) {
            ErrUtil.outputErrorLog("CloneNotSupportedException発生", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            return null;
        }
        // 項目の規格値を設置
        itemFxhdd01Clone.setKikakuChi(kikakuFxhdd01.getKikakuChi());
        itemFxhdd01Clone.setStandardPattern(kikakuFxhdd01.getStandardPattern());
        itemFxhdd01Clone.setValue(String.valueOf(diffHours));
        return itemFxhdd01Clone;
    }

    /**
     * エラー項目の背景色を設定
     *
     * @param processData 処理データ
     * @param kaisijikanList 開始時間項目リスト
     * @param syuuryoujikanList 終了時間項目リスト
     * @param label1 項目の項目名
     * @param kikakuchiInputErrorInfoList 規格値入力エラー情報リスト
     * @param errorMessageInfo エラーメッセージ情報
     */
    private void setItemBackColor(ProcessData processData, List<String> kaisijikanList, List<String> syuuryoujikanList, String label1, List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList,
            ErrorMessageInfo errorMessageInfo) {
        List<String> errorItemLabelList = new ArrayList<>();
        // エラー項目の背景色を設定
        kikakuchiInputErrorInfoList.stream().forEachOrdered((errorInfo) -> {
            errorItemLabelList.add(errorInfo.getItemLabel());
        });
        if (errorMessageInfo != null && !errorMessageInfo.getErrorMessage().contains(label1)) {
            return;
        }
        if (errorItemLabelList.contains(label1) || (errorMessageInfo != null && errorMessageInfo.getErrorMessage().contains(label1))) {
            List<String> itemList = new ArrayList<>();
            itemList.addAll(kaisijikanList);
            itemList.addAll(syuuryoujikanList);

            itemList.stream().map((jikanName) -> getItemRow(processData.getItemList(), jikanName)).forEachOrdered((itemFxhdd01) -> {
                itemFxhdd01.setBackColorInput(ErrUtil.ERR_BACK_COLOR);
            });
        }
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
        processData.setUserAuthParam(GXHDO102B032Const.USER_AUTH_DELETE_PARAM);

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
            String lotNo9 = lotNo.substring(3, 12); //ﾛｯﾄNo(9桁)
            String edaban = lotNo.substring(12, 15); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));

            // 原材料品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd11RevInfo = loadFxhdd11RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo9, edaban, paramJissekino, formId);
            ErrorMessageInfo checkRevMessageInfo = checkRevision(processData, fxhdd11RevInfo);
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
            BigDecimal newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo9, edaban, paramJissekino, formId);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            String strSystime = sdf.format(systemTime);
            // 原材料品質DB登録実績更新処理
            updateFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo9, edaban, JOTAI_FLG_SAKUJO, systemTime, paramJissekino);

            // ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo9, edaban);
            insertDeleteDataTmpSrSlipBinderkongou(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo9, edaban, strSystime);

            // ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合_削除処理
            deleteSrSlipBinderkongou(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);

            DbUtils.commitAndCloseQuietly(conDoc);
            DbUtils.commitAndCloseQuietly(conQcdb);

            // 後続処理メソッド設定
            processData.setMethod("");

            // 完了メッセージとコールバックパラメータを設定
            setCompMessage("削除しました。");

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
                        GXHDO102B032Const.BTN_UPDATE_TOP,
                        GXHDO102B032Const.BTN_DELETE_TOP,
                        GXHDO102B032Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B032Const.BTN_BINDERKONGOUKAISI_TOP,
                        GXHDO102B032Const.BTN_1LOTATARISLURRYJYUURYOU_TOP,
                        GXHDO102B032Const.BTN_SLIPYOTEIJYUURYOU_TOP,
                        GXHDO102B032Const.BTN_BINDERKONGOUSYUURYOU_TOP,
                        GXHDO102B032Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B032Const.BTN_DELETE_BOTTOM,
                        GXHDO102B032Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B032Const.BTN_BINDERKONGOUKAISI_BOTTOM,
                        GXHDO102B032Const.BTN_1LOTATARISLURRYJYUURYOU_BOTTOM,
                        GXHDO102B032Const.BTN_SLIPYOTEIJYUURYOU_BOTTOM,
                        GXHDO102B032Const.BTN_BINDERKONGOUSYUURYOU_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B032Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B032Const.BTN_INSERT_TOP,
                        GXHDO102B032Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B032Const.BTN_INSERT_BOTTOM));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO102B032Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B032Const.BTN_INSERT_TOP,
                        GXHDO102B032Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B032Const.BTN_BINDERKONGOUKAISI_TOP,
                        GXHDO102B032Const.BTN_1LOTATARISLURRYJYUURYOU_TOP,
                        GXHDO102B032Const.BTN_SLIPYOTEIJYUURYOU_TOP,
                        GXHDO102B032Const.BTN_BINDERKONGOUSYUURYOU_TOP,
                        GXHDO102B032Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B032Const.BTN_INSERT_BOTTOM,
                        GXHDO102B032Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B032Const.BTN_BINDERKONGOUKAISI_BOTTOM,
                        GXHDO102B032Const.BTN_1LOTATARISLURRYJYUURYOU_BOTTOM,
                        GXHDO102B032Const.BTN_SLIPYOTEIJYUURYOU_BOTTOM,
                        GXHDO102B032Const.BTN_BINDERKONGOUSYUURYOU_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B032Const.BTN_UPDATE_TOP,
                        GXHDO102B032Const.BTN_DELETE_TOP,
                        GXHDO102B032Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B032Const.BTN_DELETE_BOTTOM
                ));

                break;

        }
        processData.setActiveButtonId(activeIdList);
        processData.setInactiveButtonId(inactiveIdList);
        return processData;
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
        session.setAttribute("jissekino", 1);
        int paramJissekino = (Integer) session.getAttribute("jissekino");
        String formId = StringUtil.nullToBlank(session.getAttribute("formId"));
        String tantoshaCd = (String) session.getAttribute("tantoshaCd");

        // エラーメッセージリスト
        List<String> errorMessageList = processData.getInitMessageList();

        // 設計情報の取得
        Map mkSekkeiData = this.loadMkSekkeiData(queryRunnerQcdb, queryRunnerWip, lotNo);
        if (mkSekkeiData == null || mkSekkeiData.isEmpty()) {
            errorMessageList.clear();
            errorMessageList.add(MessageUtil.getMessage("XHD-000014"));
            processData.setFatalError(true);
            processData.setInitMessageList(errorMessageList);
            return processData;
        }

        // 前工程WIPから仕掛情報を取得処理
        Map shikakariData = loadShikakariDataFromWip(queryRunnerDoc, tantoshaCd, lotNo);
        if (shikakariData == null || shikakariData.isEmpty()) {
            errorMessageList.add(MessageUtil.getMessage("XHD-000029"));
        }

        // 入力項目の情報を画面にセットする。
        if (!setInputItemData(processData, queryRunnerDoc, queryRunnerQcdb, lotNo, formId, paramJissekino)) {
            // エラー発生時は処理を中断
            processData.setFatalError(true);
            processData.setInitMessageList(Arrays.asList(MessageUtil.getMessage("XHD-000038")));
            return processData;
        }

        // 画面に取得した情報をセットする。(入力項目以外)
        setViewItemData(processData, shikakariData, lotNo);
        // 画面のラベル項目の値の背景色を取得できない場合、デフォルト値を設置
        processData.getItemList().stream().map((item) -> {
            if ((item.isRender1() || item.isRenderLinkButton()) && !"".equals(StringUtil.nullToBlank(item.getKikakuChi()))) {
                if ("".equals(StringUtil.nullToBlank(item.getBackColor3()))) {
                    item.setBackColor3("#EEEEEE");
                }
                if (0 == item.getFontSize3()) {
                    item.setFontSize3(16);
                }
            }
            return item;
        }).filter((item) -> (item.isRenderOutputLabel() && !"".equals(StringUtil.nullToBlank(item.getValue())))).map((item) -> {
            if ("".equals(StringUtil.nullToBlank(item.getBackColorInput()))) {
                item.setBackColorInput("#EEEEEE");
            }
            return item;
        }).filter((item) -> (0 == item.getFontSizeInput())).forEachOrdered((item) -> {
            item.setFontSizeInput(16);
        });
        processData.setInitMessageList(errorMessageList);
        return processData;
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
    private Map loadMkSekkeiData(QueryRunner queryRunnerQcdb, QueryRunner queryRunnerWip, String lotNo) throws SQLException {
        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 12);
        // 設計データの取得
        return CommonUtil.getMkSekkeiInfo(queryRunnerQcdb, queryRunnerWip, lotNo1, lotNo2, "001");
    }

    /**
     * 入力項目以外のデータを画面項目に設定
     *
     * @param processData 処理制御データ
     * @param shikakariData 仕掛データ
     * @param lotNo ﾛｯﾄNo
     */
    private void setViewItemData(ProcessData processData, Map shikakariData, String lotNo) {
        // WIPﾛｯﾄNo
        this.setItemData(processData, GXHDO102B032Const.WIPLOTNO, lotNo);
        // ｽﾘｯﾌﾟ品名
        this.setItemData(processData, GXHDO102B032Const.SLIPHINMEI, StringUtil.nullToBlank(getMapData(shikakariData, "hinmei")));
        // ｽﾘｯﾌﾟLotNo
        this.setItemData(processData, GXHDO102B032Const.SLIPLOTNO, StringUtil.nullToBlank(getMapData(shikakariData, "lotno")));
        // ﾛｯﾄ区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode"));
        // ﾛｯﾄ区分名
        String lotkubun = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubun"));
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO102B032Const.LOTKUBUN, "");
        } else {
            if (!StringUtil.isEmpty(lotkubun)) {
                lotkubuncode = lotkubuncode + ":" + lotkubun;
            }
            this.setItemData(processData, GXHDO102B032Const.LOTKUBUN, lotkubuncode);
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
     * @return 設定結果(失敗時false)
     * @throws SQLException 例外エラー
     */
    private boolean setInputItemData(ProcessData processData, QueryRunner queryRunnerDoc, QueryRunner queryRunnerQcdb,
            String lotNo, String formId, int jissekino) throws SQLException {

        List<SrSlipBinderkongou> srSlipBinderkongouList = new ArrayList<>();
        String rev = "";
        String jotaiFlg = "";
        String kojyo = lotNo.substring(0, 3);
        String lotNo9 = lotNo.substring(3, 12);
        String edaban = lotNo.substring(12, 15);

        for (int i = 0; i < 5; i++) {
            // (3)[原材料品質DB登録実績]から、ﾃﾞｰﾀを取得
            Map fxhdd11RevInfo = loadFxhdd11RevInfo(queryRunnerDoc, kojyo, lotNo9, edaban, jissekino, formId);
            rev = StringUtil.nullToBlank(getMapData(fxhdd11RevInfo, "rev"));
            jotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd11RevInfo, "jotai_flg"));

            // revisionが空のまたはjotaiFlgが"0"でも"1"でもない場合、新規としてデフォルト値を設定してリターンする。
            if (StringUtil.isEmpty(rev) || !(JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg) || JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg))) {
                // 【Ⅵ.画面項目制御・出力仕様.初期表示時①】を元に画面表示を行う。
                processData.setInitRev(rev);
                processData.setInitJotaiFlg(jotaiFlg);

                // 画面にデータを設定する(デフォルト値)
                processData.getItemList().forEach((fxhdd001) -> {
                    this.setItemData(processData, fxhdd001.getItemId(), fxhdd001.getInputDefault());
                });

                return true;
            }

            // ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合データ取得
            srSlipBinderkongouList = getSrSlipBinderkongouData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo9, edaban);
            if (srSlipBinderkongouList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srSlipBinderkongouList.isEmpty()) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srSlipBinderkongouList.get(0));

        return true;

    }

    /**
     * データ設定処理
     *
     * @param processData 処理制御データ
     * @param srSlipBinderkongou ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合データ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrSlipBinderkongou srSlipBinderkongou) {

        // ﾊﾞｲﾝﾀﾞｰ混合開始日
        this.setItemData(processData, GXHDO102B032Const.BINDERKONGOUKAISI_DAY, getSrSlipBinderkongouItemData(GXHDO102B032Const.BINDERKONGOUKAISI_DAY, srSlipBinderkongou));

        // ﾊﾞｲﾝﾀﾞｰ混合開始時間
        this.setItemData(processData, GXHDO102B032Const.BINDERKONGOUKAISI_TIME, getSrSlipBinderkongouItemData(GXHDO102B032Const.BINDERKONGOUKAISI_TIME, srSlipBinderkongou));

        // 開始電流値(A)
        this.setItemData(processData, GXHDO102B032Const.KAISIDENRYUUTI, getSrSlipBinderkongouItemData(GXHDO102B032Const.KAISIDENRYUUTI, srSlipBinderkongou));

        // 温度(往)
        this.setItemData(processData, GXHDO102B032Const.ONDO_OU, getSrSlipBinderkongouItemData(GXHDO102B032Const.ONDO_OU, srSlipBinderkongou));

        // 温度(還)
        this.setItemData(processData, GXHDO102B032Const.ONDO_KAN, getSrSlipBinderkongouItemData(GXHDO102B032Const.ONDO_KAN, srSlipBinderkongou));

        // 圧力(往)
        this.setItemData(processData, GXHDO102B032Const.ATURYOKU_OU, getSrSlipBinderkongouItemData(GXHDO102B032Const.ATURYOKU_OU, srSlipBinderkongou));

        // 圧力(還)
        this.setItemData(processData, GXHDO102B032Const.ATURYOKU_KAN, getSrSlipBinderkongouItemData(GXHDO102B032Const.ATURYOKU_KAN, srSlipBinderkongou));

        // ﾊﾞｲﾝﾀﾞｰ混合終了予定日
        this.setItemData(processData, GXHDO102B032Const.BINDERKONGOUSYUURYOUYOTEI_DAY, getSrSlipBinderkongouItemData(GXHDO102B032Const.BINDERKONGOUSYUURYOUYOTEI_DAY, srSlipBinderkongou));

        // ﾊﾞｲﾝﾀﾞｰ混合終了予定時間
        this.setItemData(processData, GXHDO102B032Const.BINDERKONGOUSYUURYOUYOTEI_TIME, getSrSlipBinderkongouItemData(GXHDO102B032Const.BINDERKONGOUSYUURYOUYOTEI_TIME, srSlipBinderkongou));

        // 1ﾛｯﾄ当たりｽﾗﾘｰ重量
        this.setItemData(processData, GXHDO102B032Const.ICHILOTATARISLURRYJYUURYOU, getSrSlipBinderkongouItemData(GXHDO102B032Const.ICHILOTATARISLURRYJYUURYOU, srSlipBinderkongou));

        // ｽﾘｯﾌﾟ予定重量
        this.setItemData(processData, GXHDO102B032Const.SLIPYOTEIJYUURYOU, getSrSlipBinderkongouItemData(GXHDO102B032Const.SLIPYOTEIJYUURYOU, srSlipBinderkongou));

        // ﾊﾞｲﾝﾀﾞｰ混合開始担当者
        this.setItemData(processData, GXHDO102B032Const.BINDERKONGOUKAISITANTOUSYA, getSrSlipBinderkongouItemData(GXHDO102B032Const.BINDERKONGOUKAISITANTOUSYA, srSlipBinderkongou));

        // ﾊﾞｲﾝﾀﾞｰ混合終了日
        this.setItemData(processData, GXHDO102B032Const.BINDERKONGOUSYUURYOU_DAY, getSrSlipBinderkongouItemData(GXHDO102B032Const.BINDERKONGOUSYUURYOU_DAY, srSlipBinderkongou));

        // ﾊﾞｲﾝﾀﾞｰ混合終了時間
        this.setItemData(processData, GXHDO102B032Const.BINDERKONGOUSYUURYOU_TIME, getSrSlipBinderkongouItemData(GXHDO102B032Const.BINDERKONGOUSYUURYOU_TIME, srSlipBinderkongou));

        // 終了電流値(A)
        this.setItemData(processData, GXHDO102B032Const.SYUURYOUDENRYUUTI, getSrSlipBinderkongouItemData(GXHDO102B032Const.SYUURYOUDENRYUUTI, srSlipBinderkongou));

        // ﾊﾞｲﾝﾀﾞｰ混合終了担当者
        this.setItemData(processData, GXHDO102B032Const.BINDERKONGOUSYUURYOUTANTOUSYA, getSrSlipBinderkongouItemData(GXHDO102B032Const.BINDERKONGOUSYUURYOUTANTOUSYA, srSlipBinderkongou));

        // 備考1
        this.setItemData(processData, GXHDO102B032Const.BIKOU1, getSrSlipBinderkongouItemData(GXHDO102B032Const.BIKOU1, srSlipBinderkongou));

        // 備考2
        this.setItemData(processData, GXHDO102B032Const.BIKOU2, getSrSlipBinderkongouItemData(GXHDO102B032Const.BIKOU2, srSlipBinderkongou));

    }

    /**
     * ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrSlipBinderkongou> getSrSlipBinderkongouData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrSlipBinderkongou(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSrSlipBinderkongou(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        }
    }

    /**
     * [原材料品質DB登録実績]から、ﾘﾋﾞｼﾞｮﾝ,状態ﾌﾗｸﾞを取得
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
    private Map loadFxhdd11RevInfo(QueryRunner queryRunnerDoc, String kojyo, String lotNo,
            String edaban, int jissekino, String formId) throws SQLException {
        // 原材料品質DB登録実績データの取得
        String sql = "SELECT rev, jotai_flg "
                + "FROM fxhdd11 "
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
     * [原材料品質DB登録実績]から、ﾃﾞｰﾀを取得
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
    private Map loadFxhdd11RevInfoWithLock(QueryRunner queryRunnerDoc, Connection conDoc, String kojyo, String lotNo,
            String edaban, int jissekino, String formId) throws SQLException {
        // 原材料品質DB登録実績データの取得
        String sql = "SELECT rev, jotai_flg "
                + "FROM fxhdd11 "
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
     * @param formId 画面ID(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private BigDecimal getNewRev(QueryRunner queryRunnerDoc, Connection conDoc, String kojyo, String lotNo,
            String edaban, int jissekino, String formId) throws SQLException {
        BigDecimal newRev = BigDecimal.ONE;
        // 原材料品質DB登録実績データの取得
        String sql = "SELECT MAX(rev) AS rev "
                + "FROM fxhdd11 "
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
     * [ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrSlipBinderkongou> loadSrSlipBinderkongou(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = " SELECT "
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,kakuhanmode,reikyakusuivalvekai,kaitenhoukou,kaitensuu,"
                + "binderkongoujikan,binderkongoukaisinichiji,kaisidenryuuti,ondo_ou,ondo_kan,aturyoku_ou,aturyoku_kan,binderkongousyuuryouyoteinichiji,"
                + "1lotatarislurryjyuuryou as ichilotatarislurryjyuuryou,slipyoteijyuuryou,binderkongoukaisitantousya,slipkokeibunsokutei,"
                + "binderkongousyuuryounichiji,syuuryoudenryuuti,binderkongousyuuryoutantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + " FROM sr_slip_binderkongou "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? ";

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
        mapping.put("kojyo", "kojyo");                                                          // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                                          // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                                        // 枝番
        mapping.put("sliphinmei", "sliphinmei");                                                // ｽﾘｯﾌﾟ品名
        mapping.put("sliplotno", "sliplotno");                                                  // ｽﾘｯﾌﾟLotNo
        mapping.put("lotkubun", "lotkubun");                                                    // ﾛｯﾄ区分
        mapping.put("genryoukigou", "genryoukigou");                                            // 原料記号
        mapping.put("kakuhanmode", "kakuhanmode");                                              // 撹拌ﾓｰﾄﾞ
        mapping.put("reikyakusuivalvekai", "reikyakusuivalvekai");                              // 冷却水ﾊﾞﾙﾌﾞ開
        mapping.put("kaitenhoukou", "kaitenhoukou");                                            // 回転方向
        mapping.put("kaitensuu", "kaitensuu");                                                  // 回転数(rpm)
        mapping.put("binderkongoujikan", "binderkongoujikan");                                  // ﾊﾞｲﾝﾀﾞｰ混合時間
        mapping.put("binderkongoukaisinichiji", "binderkongoukaisinichiji");                    // ﾊﾞｲﾝﾀﾞｰ混合開始日時
        mapping.put("kaisidenryuuti", "kaisidenryuuti");                                        // 開始電流値(A)
        mapping.put("ondo_ou", "ondo_ou");                                                      // 温度(往)
        mapping.put("ondo_kan", "ondo_kan");                                                    // 温度(還)
        mapping.put("aturyoku_ou", "aturyoku_ou");                                              // 圧力(往)
        mapping.put("aturyoku_kan", "aturyoku_kan");                                            // 圧力(還)
        mapping.put("binderkongousyuuryouyoteinichiji", "binderkongousyuuryouyoteinichiji");    // ﾊﾞｲﾝﾀﾞｰ混合終了予定日時
        mapping.put("ichilotatarislurryjyuuryou", "ichilotatarislurryjyuuryou");                // 1ﾛｯﾄ当たりｽﾗﾘｰ重量
        mapping.put("slipyoteijyuuryou", "slipyoteijyuuryou");                                  // ｽﾘｯﾌﾟ予定重量
        mapping.put("binderkongoukaisitantousya", "binderkongoukaisitantousya");                // ﾊﾞｲﾝﾀﾞｰ混合開始担当者
        mapping.put("slipkokeibunsokutei", "slipkokeibunsokutei");                              // ｽﾘｯﾌﾟ固形分測定
        mapping.put("binderkongousyuuryounichiji", "binderkongousyuuryounichiji");              // ﾊﾞｲﾝﾀﾞｰ混合終了日時
        mapping.put("syuuryoudenryuuti", "syuuryoudenryuuti");                                  // 終了電流値(A)
        mapping.put("binderkongousyuuryoutantousya", "binderkongousyuuryoutantousya");          // ﾊﾞｲﾝﾀﾞｰ混合終了担当者
        mapping.put("bikou1", "bikou1");                                                        // 備考1
        mapping.put("bikou2", "bikou2");                                                        // 備考2
        mapping.put("torokunichiji", "torokunichiji");                                          // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                                            // 更新日時
        mapping.put("revision", "revision");                                                    // revision

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrSlipBinderkongou>> beanHandler = new BeanListHandler<>(SrSlipBinderkongou.class,
                rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrSlipBinderkongou> loadTmpSrSlipBinderkongou(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = " SELECT "
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,kakuhanmode,reikyakusuivalvekai,kaitenhoukou,kaitensuu,"
                + "binderkongoujikan,binderkongoukaisinichiji,kaisidenryuuti,ondo_ou,ondo_kan,aturyoku_ou,aturyoku_kan,binderkongousyuuryouyoteinichiji,"
                + "1lotatarislurryjyuuryou as ichilotatarislurryjyuuryou,slipyoteijyuuryou,binderkongoukaisitantousya,slipkokeibunsokutei,"
                + "binderkongousyuuryounichiji,syuuryoudenryuuti,binderkongousyuuryoutantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + " FROM tmp_sr_slip_binderkongou "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND deleteflag = ? ";

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
        mapping.put("kojyo", "kojyo");                                                          // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                                          // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                                        // 枝番
        mapping.put("sliphinmei", "sliphinmei");                                                // ｽﾘｯﾌﾟ品名
        mapping.put("sliplotno", "sliplotno");                                                  // ｽﾘｯﾌﾟLotNo
        mapping.put("lotkubun", "lotkubun");                                                    // ﾛｯﾄ区分
        mapping.put("genryoukigou", "genryoukigou");                                            // 原料記号
        mapping.put("kakuhanmode", "kakuhanmode");                                              // 撹拌ﾓｰﾄﾞ
        mapping.put("reikyakusuivalvekai", "reikyakusuivalvekai");                              // 冷却水ﾊﾞﾙﾌﾞ開
        mapping.put("kaitenhoukou", "kaitenhoukou");                                            // 回転方向
        mapping.put("kaitensuu", "kaitensuu");                                                  // 回転数(rpm)
        mapping.put("binderkongoujikan", "binderkongoujikan");                                  // ﾊﾞｲﾝﾀﾞｰ混合時間
        mapping.put("binderkongoukaisinichiji", "binderkongoukaisinichiji");                    // ﾊﾞｲﾝﾀﾞｰ混合開始日時
        mapping.put("kaisidenryuuti", "kaisidenryuuti");                                        // 開始電流値(A)
        mapping.put("ondo_ou", "ondo_ou");                                                      // 温度(往)
        mapping.put("ondo_kan", "ondo_kan");                                                    // 温度(還)
        mapping.put("aturyoku_ou", "aturyoku_ou");                                              // 圧力(往)
        mapping.put("aturyoku_kan", "aturyoku_kan");                                            // 圧力(還)
        mapping.put("binderkongousyuuryouyoteinichiji", "binderkongousyuuryouyoteinichiji");    // ﾊﾞｲﾝﾀﾞｰ混合終了予定日時
        mapping.put("ichilotatarislurryjyuuryou", "ichilotatarislurryjyuuryou");                // 1ﾛｯﾄ当たりｽﾗﾘｰ重量
        mapping.put("slipyoteijyuuryou", "slipyoteijyuuryou");                                  // ｽﾘｯﾌﾟ予定重量
        mapping.put("binderkongoukaisitantousya", "binderkongoukaisitantousya");                // ﾊﾞｲﾝﾀﾞｰ混合開始担当者
        mapping.put("slipkokeibunsokutei", "slipkokeibunsokutei");                              // ｽﾘｯﾌﾟ固形分測定
        mapping.put("binderkongousyuuryounichiji", "binderkongousyuuryounichiji");              // ﾊﾞｲﾝﾀﾞｰ混合終了日時
        mapping.put("syuuryoudenryuuti", "syuuryoudenryuuti");                                  // 終了電流値(A)
        mapping.put("binderkongousyuuryoutantousya", "binderkongousyuuryoutantousya");          // ﾊﾞｲﾝﾀﾞｰ混合終了担当者
        mapping.put("bikou1", "bikou1");                                                        // 備考1
        mapping.put("bikou2", "bikou2");                                                        // 備考2
        mapping.put("torokunichiji", "torokunichiji");                                          // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                                            // 更新日時
        mapping.put("revision", "revision");                                                    // revision
        mapping.put("deleteflag", "deleteflag");                                                // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrSlipBinderkongou>> beanHandler = new BeanListHandler<>(SrSlipBinderkongou.class,
                rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
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
        return listData.stream().filter(n -> itemId.equals(n.getItemId())).findFirst().orElse(null);
    }

    /**
     * 項目データ(入力値)取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @param srSlipBinderkongou ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合データ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrSlipBinderkongou srSlipBinderkongou) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srSlipBinderkongou != null) {
            // 元データが存在する場合元データより取得
            return getSrSlipBinderkongouItemData(itemId, srSlipBinderkongou);
        } else {
            return null;
        }
    }

    /**
     * 項目データ(入力値)取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @param srGlasshyoryo ｶﾞﾗｽ作製・秤量データ
     * @return 入力値
     */
    private String getItemKikakuchi(List<FXHDD01> listData, String itemId, SrSlipBinderkongou srSlipBinderkongou) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return StringUtil.nullToBlank(selectData.get(0).getKikakuChi()).replace("【", "").replace("】", "");
        } else if (srSlipBinderkongou != null) {
            // 元データが存在する場合元データより取得
            return getSrSlipBinderkongouItemData(itemId, srSlipBinderkongou);
        } else {
            return null;
        }
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
     * 原材料品質DB登録実績(fxhdd11)登録処理
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
    private void insertFxhdd11(QueryRunner queryRunnerDoc, Connection conDoc, String tantoshaCd, String formId, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino, String jotaiFlg, Timestamp systemTime) throws SQLException {
        String sql = "INSERT INTO fxhdd11 ("
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
     * 原材料品質DB登録実績(fxhdd11)更新処理
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
    private void updateFxhdd11(QueryRunner queryRunnerDoc, Connection conDoc, String tantoshaCd, String formId, BigDecimal rev,
            String kojyo, String lotNo, String edaban, String jotaiFlg, Timestamp systemTime, int jissekino) throws SQLException {
        String sql = "UPDATE fxhdd11 SET "
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
     * ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合_仮登録(tmp_sr_slip_binderkongou)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void insertTmpSrSlipBinderkongou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "INSERT INTO tmp_sr_slip_binderkongou ("
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,kakuhanmode,reikyakusuivalvekai,kaitenhoukou,kaitensuu,"
                + "binderkongoujikan,binderkongoukaisinichiji,kaisidenryuuti,ondo_ou,ondo_kan,aturyoku_ou,aturyoku_kan,binderkongousyuuryouyoteinichiji,"
                + "1lotatarislurryjyuuryou,slipyoteijyuuryou,binderkongoukaisitantousya,slipkokeibunsokutei,binderkongousyuuryounichiji,"
                + "syuuryoudenryuuti,binderkongousyuuryoutantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + " ) VALUES ( "
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrSlipBinderkongou(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, processData, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合_仮登録(tmp_sr_slip_binderkongou)更新処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void updateTmpSrSlipBinderkongou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "UPDATE tmp_sr_slip_binderkongou SET "
                + " sliphinmei = ?,sliplotno = ?,lotkubun = ?,genryoukigou = ?,kakuhanmode = ?,reikyakusuivalvekai = ?,kaitenhoukou = ?,kaitensuu = ?,binderkongoujikan = ?,"
                + "binderkongoukaisinichiji = ?,kaisidenryuuti = ?,ondo_ou = ?,ondo_kan = ?,aturyoku_ou = ?,aturyoku_kan = ?,binderkongousyuuryouyoteinichiji = ?,"
                + "1lotatarislurryjyuuryou = ?,slipyoteijyuuryou = ?,binderkongoukaisitantousya = ?,slipkokeibunsokutei = ?,binderkongousyuuryounichiji = ?,"
                + "syuuryoudenryuuti = ?,binderkongousyuuryoutantousya = ?,bikou1 = ?,bikou2 = ?,kosinnichiji = ?,revision = ?,deleteflag = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrSlipBinderkongou> srSlipBinderkongouList = getSrSlipBinderkongouData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrSlipBinderkongou srSlipBinderkongou = null;
        if (!srSlipBinderkongouList.isEmpty()) {
            srSlipBinderkongou = srSlipBinderkongouList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrSlipBinderkongou(false, newRev, 0, "", "", "", systemTime, processData, srSlipBinderkongou);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合_仮登録(tmp_sr_slip_binderkongou)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrSlipBinderkongou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sr_slip_binderkongou "
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
     * ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合_仮登録(tmp_sr_slip_binderkongou)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srSlipBinderkongou ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrSlipBinderkongou(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, String systemTime, ProcessData processData, SrSlipBinderkongou srSlipBinderkongou) {

        List<FXHDD01> pItemList = processData.getItemList();
        List<Object> params = new ArrayList<>();
        // ﾊﾞｲﾝﾀﾞｰ混合開始日時
        String binderkongoukaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B032Const.BINDERKONGOUKAISI_TIME, srSlipBinderkongou));
        // ﾊﾞｲﾝﾀﾞｰ混合終了予定日時
        String binderkongousyuuryouyoteiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B032Const.BINDERKONGOUSYUURYOUYOTEI_TIME, srSlipBinderkongou));
        // ﾊﾞｲﾝﾀﾞｰ混合終了日時
        String binderkongousyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B032Const.BINDERKONGOUSYUURYOU_TIME, srSlipBinderkongou));
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B032Const.SLIPHINMEI, srSlipBinderkongou)));                           // ｽﾘｯﾌﾟ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B032Const.SLIPLOTNO, srSlipBinderkongou)));                            // ｽﾘｯﾌﾟLotNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B032Const.LOTKUBUN, srSlipBinderkongou)));                             // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B032Const.GENRYOUKIGOU, srSlipBinderkongou)));                    // 原料記号
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B032Const.KAKUHANMODE, srSlipBinderkongou)));                     // 撹拌ﾓｰﾄﾞ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B032Const.REIKYAKUSUIVALVEKAI, srSlipBinderkongou)));             // 冷却水ﾊﾞﾙﾌﾞ開
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B032Const.KAITENHOUKOU, srSlipBinderkongou)));                    // 回転方向
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B032Const.KAITENSUU, srSlipBinderkongou)));                          // 回転数(rpm)
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B032Const.BINDERKONGOUJIKAN, srSlipBinderkongou)));               // ﾊﾞｲﾝﾀﾞｰ混合時間
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B032Const.BINDERKONGOUKAISI_DAY, srSlipBinderkongou),
                "".equals(binderkongoukaisiTime) ? "0000" : binderkongoukaisiTime));                                                                            // ﾊﾞｲﾝﾀﾞｰ混合開始日時
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B032Const.KAISIDENRYUUTI, srSlipBinderkongou)));                   // 開始電流値(A)
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B032Const.ONDO_OU, srSlipBinderkongou)));                                 // 温度(往)
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B032Const.ONDO_KAN, srSlipBinderkongou)));                                // 温度(還)
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B032Const.ATURYOKU_OU, srSlipBinderkongou)));                      // 圧力(往)
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B032Const.ATURYOKU_KAN, srSlipBinderkongou)));                     // 圧力(還)
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B032Const.BINDERKONGOUSYUURYOUYOTEI_DAY, srSlipBinderkongou),
                "".equals(binderkongousyuuryouyoteiTime) ? "0000" : binderkongousyuuryouyoteiTime));                                                            // ﾊﾞｲﾝﾀﾞｰ混合終了予定日時
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B032Const.ICHILOTATARISLURRYJYUURYOU, srSlipBinderkongou)));              // 1ﾛｯﾄ当たりｽﾗﾘｰ重量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B032Const.SLIPYOTEIJYUURYOU, srSlipBinderkongou)));                       // ｽﾘｯﾌﾟ予定重量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B032Const.BINDERKONGOUKAISITANTOUSYA, srSlipBinderkongou)));           // ﾊﾞｲﾝﾀﾞｰ混合開始担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B032Const.SLIPKOKEIBUNSOKUTEI, srSlipBinderkongou)));             // ｽﾘｯﾌﾟ固形分測定
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B032Const.BINDERKONGOUSYUURYOU_DAY, srSlipBinderkongou),
                "".equals(binderkongousyuuryouTime) ? "0000" : binderkongousyuuryouTime));                                                                      // ﾊﾞｲﾝﾀﾞｰ混合終了日時
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B032Const.SYUURYOUDENRYUUTI, srSlipBinderkongou)));                // 終了電流値(A)
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B032Const.BINDERKONGOUSYUURYOUTANTOUSYA, srSlipBinderkongou)));        // ﾊﾞｲﾝﾀﾞｰ混合終了担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B032Const.BIKOU1, srSlipBinderkongou)));                               // 備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B032Const.BIKOU2, srSlipBinderkongou)));                               // 備考2

        if (isInsert) {
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時
        } else {
            params.add(systemTime); //更新日時
        }
        params.add(newRev);     //revision
        params.add(deleteflag); //削除ﾌﾗｸﾞ

        return params;
    }

    /**
     * ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合(sr_slip_binderkongou)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srSlipBinderkongou 登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrSlipBinderkongou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData, SrSlipBinderkongou srSlipBinderkongou) throws SQLException {

        String sql = "INSERT INTO sr_slip_binderkongou ("
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,kakuhanmode,reikyakusuivalvekai,kaitenhoukou,kaitensuu,"
                + "binderkongoujikan,binderkongoukaisinichiji,kaisidenryuuti,ondo_ou,ondo_kan,aturyoku_ou,aturyoku_kan,binderkongousyuuryouyoteinichiji,"
                + "1lotatarislurryjyuuryou,slipyoteijyuuryou,binderkongoukaisitantousya,slipkokeibunsokutei,binderkongousyuuryounichiji,"
                + "syuuryoudenryuuti,binderkongousyuuryoutantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + " ) VALUES ( "
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrSlipBinderkongou(true, newRev, kojyo, lotNo, edaban, systemTime, processData, srSlipBinderkongou);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合(sr_slip_binderkongou)更新処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void updateSrSlipBinderkongou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "UPDATE sr_slip_binderkongou SET "
                + " sliphinmei = ?,sliplotno = ?,lotkubun = ?,genryoukigou = ?,kakuhanmode = ?,reikyakusuivalvekai = ?,kaitenhoukou = ?,kaitensuu = ?,binderkongoujikan = ?,"
                + "binderkongoukaisinichiji = ?,kaisidenryuuti = ?,ondo_ou = ?,ondo_kan = ?,aturyoku_ou = ?,aturyoku_kan = ?,binderkongousyuuryouyoteinichiji = ?,"
                + "1lotatarislurryjyuuryou = ?,slipyoteijyuuryou = ?,binderkongoukaisitantousya = ?,slipkokeibunsokutei = ?,binderkongousyuuryounichiji = ?,"
                + "syuuryoudenryuuti = ?,binderkongousyuuryoutantousya = ?,bikou1 = ?,bikou2 = ?,kosinnichiji = ?,revision = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrSlipBinderkongou> srSlipBinderkongouList = getSrSlipBinderkongouData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrSlipBinderkongou srSlipBinderkongou = null;
        if (!srSlipBinderkongouList.isEmpty()) {
            srSlipBinderkongou = srSlipBinderkongouList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrSlipBinderkongou(false, newRev, "", "", "", systemTime, processData, srSlipBinderkongou);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合(sr_slip_binderkongou)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srSlipBinderkongou ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrSlipBinderkongou(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            String systemTime, ProcessData processData, SrSlipBinderkongou srSlipBinderkongou) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // ﾊﾞｲﾝﾀﾞｰ混合開始日時
        String binderkongoukaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B032Const.BINDERKONGOUKAISI_TIME, srSlipBinderkongou));
        // ﾊﾞｲﾝﾀﾞｰ混合終了予定日時
        String binderkongousyuuryouyoteiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B032Const.BINDERKONGOUSYUURYOUYOTEI_TIME, srSlipBinderkongou));
        // ﾊﾞｲﾝﾀﾞｰ混合終了日時
        String binderkongousyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B032Const.BINDERKONGOUSYUURYOU_TIME, srSlipBinderkongou));
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B032Const.SLIPHINMEI, srSlipBinderkongou)));                           // ｽﾘｯﾌﾟ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B032Const.SLIPLOTNO, srSlipBinderkongou)));                            // ｽﾘｯﾌﾟLotNo
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B032Const.LOTKUBUN, srSlipBinderkongou)));                             // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B032Const.GENRYOUKIGOU, srSlipBinderkongou)));                    // 原料記号
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B032Const.KAKUHANMODE, srSlipBinderkongou)));                     // 撹拌ﾓｰﾄﾞ
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B032Const.REIKYAKUSUIVALVEKAI, srSlipBinderkongou)));             // 冷却水ﾊﾞﾙﾌﾞ開
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B032Const.KAITENHOUKOU, srSlipBinderkongou)));                    // 回転方向
        params.add(DBUtil.stringToIntObject(getItemKikakuchi(pItemList, GXHDO102B032Const.KAITENSUU, srSlipBinderkongou)));                          // 回転数(rpm)
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B032Const.BINDERKONGOUJIKAN, srSlipBinderkongou)));               // ﾊﾞｲﾝﾀﾞｰ混合時間
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B032Const.BINDERKONGOUKAISI_DAY, srSlipBinderkongou),
                "".equals(binderkongoukaisiTime) ? "0000" : binderkongoukaisiTime));                                                                 // ﾊﾞｲﾝﾀﾞｰ混合開始日時
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B032Const.KAISIDENRYUUTI, srSlipBinderkongou)));                   // 開始電流値(A)
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B032Const.ONDO_OU, srSlipBinderkongou)));                                 // 温度(往)
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B032Const.ONDO_KAN, srSlipBinderkongou)));                                // 温度(還)
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B032Const.ATURYOKU_OU, srSlipBinderkongou)));                      // 圧力(往)
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B032Const.ATURYOKU_KAN, srSlipBinderkongou)));                     // 圧力(還)
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B032Const.BINDERKONGOUSYUURYOUYOTEI_DAY, srSlipBinderkongou),
                "".equals(binderkongousyuuryouyoteiTime) ? "0000" : binderkongousyuuryouyoteiTime));                                                 // ﾊﾞｲﾝﾀﾞｰ混合終了予定日時
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B032Const.ICHILOTATARISLURRYJYUURYOU, srSlipBinderkongou)));              // 1ﾛｯﾄ当たりｽﾗﾘｰ重量
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B032Const.SLIPYOTEIJYUURYOU, srSlipBinderkongou)));                       // ｽﾘｯﾌﾟ予定重量
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B032Const.BINDERKONGOUKAISITANTOUSYA, srSlipBinderkongou)));           // ﾊﾞｲﾝﾀﾞｰ混合開始担当者
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B032Const.SLIPKOKEIBUNSOKUTEI, srSlipBinderkongou)));             // ｽﾘｯﾌﾟ固形分測定
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B032Const.BINDERKONGOUSYUURYOU_DAY, srSlipBinderkongou),
                "".equals(binderkongousyuuryouTime) ? "0000" : binderkongousyuuryouTime));                                                           // ﾊﾞｲﾝﾀﾞｰ混合終了日時
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B032Const.SYUURYOUDENRYUUTI, srSlipBinderkongou)));                // 終了電流値(A)
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B032Const.BINDERKONGOUSYUURYOUTANTOUSYA, srSlipBinderkongou)));        // ﾊﾞｲﾝﾀﾞｰ混合終了担当者
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B032Const.BIKOU1, srSlipBinderkongou)));                               // 備考1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B032Const.BIKOU2, srSlipBinderkongou)));                               // 備考2

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
     * ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合(sr_slip_binderkongou)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSrSlipBinderkongou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sr_slip_binderkongou "
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
     * [ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_slip_binderkongou "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? ";
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
     * @param fxhdd11RevInfo 原材料品質DB登録実績データ
     * @return エラーメッセージ情報
     * @throws SQLException 例外エラー
     */
    private ErrorMessageInfo checkRevision(ProcessData processData, Map fxhdd11RevInfo) throws SQLException {
        if (StringUtil.isEmpty(processData.getInitJotaiFlg())) {
            // 新規の場合、データが存在する場合
            if (fxhdd11RevInfo != null && !fxhdd11RevInfo.isEmpty()) {
                return new ErrorMessageInfo(MessageUtil.getMessage("XHD-000026"));
            }
        } else {
            // 原材料品質DB登録実績データが取得出来ていない場合エラー
            if (fxhdd11RevInfo == null || fxhdd11RevInfo.isEmpty()) {
                return new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"));
            }

            // revisionが更新されていた場合エラー
            if (!processData.getInitRev().equals(StringUtil.nullToBlank(getMapData(fxhdd11RevInfo, "rev")))) {
                return new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"));
            }
        }

        return null;
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
     * @param srSlipBinderkongou ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合データ
     * @return DB値
     */
    private String getSrSlipBinderkongouItemData(String itemId, SrSlipBinderkongou srSlipBinderkongou) {
        switch (itemId) {
            // ｽﾘｯﾌﾟ品名
            case GXHDO102B032Const.SLIPHINMEI:
                return StringUtil.nullToBlank(srSlipBinderkongou.getSliphinmei());

            // ｽﾘｯﾌﾟLotNo
            case GXHDO102B032Const.SLIPLOTNO:
                return StringUtil.nullToBlank(srSlipBinderkongou.getSliplotno());

            // ﾛｯﾄ区分
            case GXHDO102B032Const.LOTKUBUN:
                return StringUtil.nullToBlank(srSlipBinderkongou.getLotkubun());

            // 原料記号
            case GXHDO102B032Const.GENRYOUKIGOU:
                return StringUtil.nullToBlank(srSlipBinderkongou.getGenryoukigou());

            // 撹拌ﾓｰﾄﾞ
            case GXHDO102B032Const.KAKUHANMODE:
                return StringUtil.nullToBlank(srSlipBinderkongou.getKakuhanmode());

            // 冷却水ﾊﾞﾙﾌﾞ開
            case GXHDO102B032Const.REIKYAKUSUIVALVEKAI:
                return StringUtil.nullToBlank(srSlipBinderkongou.getReikyakusuivalvekai());

            // 回転方向
            case GXHDO102B032Const.KAITENHOUKOU:
                return StringUtil.nullToBlank(srSlipBinderkongou.getKaitenhoukou());

            // 回転数(rpm)
            case GXHDO102B032Const.KAITENSUU:
                return StringUtil.nullToBlank(srSlipBinderkongou.getKaitensuu());

            // ﾊﾞｲﾝﾀﾞｰ混合時間
            case GXHDO102B032Const.BINDERKONGOUJIKAN:
                return StringUtil.nullToBlank(srSlipBinderkongou.getBinderkongoujikan());

            // ﾊﾞｲﾝﾀﾞｰ混合開始日
            case GXHDO102B032Const.BINDERKONGOUKAISI_DAY:
                return DateUtil.formattedTimestamp(srSlipBinderkongou.getBinderkongoukaisinichiji(), "yyMMdd");

            // ﾊﾞｲﾝﾀﾞｰ混合開始時間
            case GXHDO102B032Const.BINDERKONGOUKAISI_TIME:
                return DateUtil.formattedTimestamp(srSlipBinderkongou.getBinderkongoukaisinichiji(), "HHmm");

            // 開始電流値(A)
            case GXHDO102B032Const.KAISIDENRYUUTI:
                return StringUtil.nullToBlank(srSlipBinderkongou.getKaisidenryuuti());

            // 温度(往)
            case GXHDO102B032Const.ONDO_OU:
                return StringUtil.nullToBlank(srSlipBinderkongou.getOndo_ou());

            // 温度(還)
            case GXHDO102B032Const.ONDO_KAN:
                return StringUtil.nullToBlank(srSlipBinderkongou.getOndo_kan());

            // 圧力(往)
            case GXHDO102B032Const.ATURYOKU_OU:
                return StringUtil.nullToBlank(srSlipBinderkongou.getAturyoku_ou());

            // 圧力(還)
            case GXHDO102B032Const.ATURYOKU_KAN:
                return StringUtil.nullToBlank(srSlipBinderkongou.getAturyoku_kan());

            // ﾊﾞｲﾝﾀﾞｰ混合終了予定日
            case GXHDO102B032Const.BINDERKONGOUSYUURYOUYOTEI_DAY:
                return DateUtil.formattedTimestamp(srSlipBinderkongou.getBinderkongousyuuryouyoteinichiji(), "yyMMdd");

            // ﾊﾞｲﾝﾀﾞｰ混合終了予定時間
            case GXHDO102B032Const.BINDERKONGOUSYUURYOUYOTEI_TIME:
                return DateUtil.formattedTimestamp(srSlipBinderkongou.getBinderkongousyuuryouyoteinichiji(), "HHmm");

            // 1ﾛｯﾄ当たりｽﾗﾘｰ重量
            case GXHDO102B032Const.ICHILOTATARISLURRYJYUURYOU:
                return StringUtil.nullToBlank(srSlipBinderkongou.getIchilotatarislurryjyuuryou());

            // ｽﾘｯﾌﾟ予定重量
            case GXHDO102B032Const.SLIPYOTEIJYUURYOU:
                return StringUtil.nullToBlank(srSlipBinderkongou.getSlipyoteijyuuryou());

            // ﾊﾞｲﾝﾀﾞｰ混合開始担当者
            case GXHDO102B032Const.BINDERKONGOUKAISITANTOUSYA:
                return StringUtil.nullToBlank(srSlipBinderkongou.getBinderkongoukaisitantousya());

            // ｽﾘｯﾌﾟ固形分測定
            case GXHDO102B032Const.SLIPKOKEIBUNSOKUTEI:
                return StringUtil.nullToBlank(srSlipBinderkongou.getSlipkokeibunsokutei());

            // ﾊﾞｲﾝﾀﾞｰ混合終了日
            case GXHDO102B032Const.BINDERKONGOUSYUURYOU_DAY:
                return DateUtil.formattedTimestamp(srSlipBinderkongou.getBinderkongousyuuryounichiji(), "yyMMdd");

            // ﾊﾞｲﾝﾀﾞｰ混合終了時間
            case GXHDO102B032Const.BINDERKONGOUSYUURYOU_TIME:
                return DateUtil.formattedTimestamp(srSlipBinderkongou.getBinderkongousyuuryounichiji(), "HHmm");

            // 終了電流値(A)
            case GXHDO102B032Const.SYUURYOUDENRYUUTI:
                return StringUtil.nullToBlank(srSlipBinderkongou.getSyuuryoudenryuuti());

            // ﾊﾞｲﾝﾀﾞｰ混合終了担当者
            case GXHDO102B032Const.BINDERKONGOUSYUURYOUTANTOUSYA:
                return StringUtil.nullToBlank(srSlipBinderkongou.getBinderkongousyuuryoutantousya());

            // 備考1
            case GXHDO102B032Const.BIKOU1:
                return StringUtil.nullToBlank(srSlipBinderkongou.getBikou1());

            // 備考2
            case GXHDO102B032Const.BIKOU2:
                return StringUtil.nullToBlank(srSlipBinderkongou.getBikou2());

            default:
                return null;
        }
    }

    /**
     * ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合_仮登録(tmp_sr_slip_binderkongou)登録処理(削除時)
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void insertDeleteDataTmpSrSlipBinderkongou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_slip_binderkongou ( "
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,kakuhanmode,reikyakusuivalvekai,kaitenhoukou,kaitensuu,"
                + "binderkongoujikan,binderkongoukaisinichiji,kaisidenryuuti,ondo_ou,ondo_kan,aturyoku_ou,aturyoku_kan,binderkongousyuuryouyoteinichiji,"
                + "1lotatarislurryjyuuryou,slipyoteijyuuryou,binderkongoukaisitantousya,slipkokeibunsokutei,binderkongousyuuryounichiji,"
                + "syuuryoudenryuuti,binderkongousyuuryoutantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") SELECT "
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,kakuhanmode,reikyakusuivalvekai,kaitenhoukou,kaitensuu,"
                + "binderkongoujikan,binderkongoukaisinichiji,kaisidenryuuti,ondo_ou,ondo_kan,aturyoku_ou,aturyoku_kan,binderkongousyuuryouyoteinichiji,"
                + "1lotatarislurryjyuuryou,slipyoteijyuuryou,binderkongoukaisitantousya,slipkokeibunsokutei,binderkongousyuuryounichiji,"
                + "syuuryoudenryuuti,binderkongousyuuryoutantousya,bikou1,bikou2,?,?,?,? "
                + " FROM sr_slip_binderkongou "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? ";

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
     * 画面のBean情報を取得
     *
     * @param beanId フォームID
     * @return サブ画面情報
     */
    private Object getFormBean(String beanId) {
        return FacesContext.getCurrentInstance().
                getELContext().getELResolver().getValue(FacesContext.getCurrentInstance().
                        getELContext(), null, beanId);
    }

    /**
     * 完了メッセージ設定処理
     *
     * @param message 完了メッセージ
     */
    private void setCompMessage(String message) {
        CompMessage compMessage = (CompMessage) getFormBean("beanCompMessage");
        compMessage.setCompMessage(message);
    }

    /**
     * 前工程WIPから仕掛情報を取得する。
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param tantoshaCd 担当者コード
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadShikakariDataFromWip(QueryRunner queryRunnerDoc, String tantoshaCd, String lotNo) throws SQLException {

        List<SikakariJson> sikakariList = CommonUtil.getMwipResult(queryRunnerDoc, tantoshaCd, lotNo);
        SikakariJson sikakariObj;
        Map shikakariData = new HashMap();
        if (sikakariList != null) {
            sikakariObj = sikakariList.get(0);
            // 前工程WIPから取得した品名
            shikakariData.put("hinmei", sikakariObj.getHinmei());
            shikakariData.put("oyalotedaban", sikakariObj.getOyaLotEdaBan());
            shikakariData.put("lotkubuncode", sikakariObj.getLotKubunCode());
            shikakariData.put("lotkubun", sikakariObj.getLotkubun());
            shikakariData.put("lotno", sikakariObj.getConventionalLot());
        }

        return shikakariData;
    }

    /**
     * (14)[ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb オブジェクト
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo LotNo
     * @param edaban 枝番
     * @param rev revision
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadTounyuusutenyouki(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban, String rev) throws SQLException {
        // 誘電体ｽﾗﾘｰ_調合量1、誘電体ｽﾗﾘｰ_調合量2、ﾄﾙｴﾝ調整量、分散材①_調合量1、分散材①_調合量2、分散材②_調合量1、分散材②_調合量2の取得
        String sql = "SELECT yuudentaislurry_tyougouryou1, yuudentaislurry_tyougouryou2, toluenetyouseiryou,"
                + " zunsanzai1_tyougouryou1, zunsanzai1_tyougouryou2, zunsanzai2_tyougouryou1, zunsanzai2_tyougouryou2"
                + " FROM sr_slip_youzaihyouryou_tounyuu_sutenyouki"
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";
        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * (15)[ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb オブジェクト
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo LotNo
     * @param edaban 枝番
     * @param rev revision
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadSlurrykokeibuntyousei(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban, String rev) throws SQLException {
        // ｽﾗﾘｰ合計重量、固形分調整量➁の取得
        String sql = "SELECT slurrygoukeijyuuryou, kokeibuntyouseiryou2 FROM sr_slip_slurrykokeibuntyousei_siropori"
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";
        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * (16)[ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb オブジェクト
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo LotNo
     * @param edaban 枝番
     * @param rev revision
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadBinderhyouryouTounyuu(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban, String rev) throws SQLException {
        // ﾊﾞｲﾝﾀﾞｰ添加量規格の取得
        String sql = "SELECT bindertenkaryoukikaku FROM sr_slip_binderhyouryou_tounyuu"
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";
        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * (17)[ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb オブジェクト
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo LotNo
     * @param edaban 枝番
     * @param rev revision
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadSutenyouki(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban, String rev) throws SQLException {
        // 溶剤調整量の取得
        String sql = "SELECT youzaityouseiryou FROM sr_slip_slurrykokeibuntyousei_sutenyouki"
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";
        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * (18)[ｽﾘｯﾌﾟ作製・溶剤秤量・投入(白ﾎﾟﾘ)]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb オブジェクト
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo LotNo
     * @param edaban 枝番
     * @param rev revision
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadTounyuuSiropori(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban, String rev) throws SQLException {
        // 溶剤①_調合量1、溶剤①_調合量2、溶剤②_調合量1、溶剤②_調合量2、溶剤③_調合量1、溶剤③_調合量2の取得
        String sql = "SELECT youzai1_tyougouryou1, youzai1_tyougouryou2, youzai2_tyougouryou1, youzai2_tyougouryou2, youzai3_tyougouryou1, youzai3_tyougouryou2"
                + " FROM sr_slip_youzaihyouryou_tounyuu_siropori"
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";
        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * [前工程設計]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb オブジェクト
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo LotNo
     * @param edaban 枝番
     * @param syurui 種類
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadDaMkSekKeiData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban, String syurui) throws SQLException {
        // 前工程設計データの取得
        String sql = "SELECT sekkeino, pattern FROM da_mksekkei"
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND syurui = ? ";
        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(syurui);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * [ﾊﾟﾗﾒｰﾀﾏｽﾀ]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerDoc オブジェクト
     * @param key キー
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhbm03Data(QueryRunner queryRunnerDoc, String key) throws SQLException {
        // ﾊﾟﾗﾒｰﾀﾏｽﾀデータの取得
        String sql = "SELECT data "
                + " FROM fxhbm03 "
                + " WHERE user_name = 'common_user' AND key = ? ";
        List<Object> params = new ArrayList<>();
        params.add(key);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerDoc.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * [前工程規格情報]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb オブジェクト
     * @param sekkeiNo 設計No
     * @param data ﾊﾟﾗﾒｰﾀﾃﾞｰﾀ
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadDaMkJokenData(QueryRunner queryRunnerQcdb, String sekkeiNo, String[] data) throws SQLException {
        if (data == null || data.length < 3) {
            return null;
        }
        // 前工程規格情報データの取得
        String sql = "SELECT kikakuti FROM da_mkjoken"
                + " WHERE sekkeino = ? AND kouteimei = ? AND koumokumei = ? AND kanrikoumokumei = ? ";
        List<Object> params = new ArrayList<>();
        params.add(sekkeiNo);
        params.add(data[0]);
        params.add(data[1]);
        params.add(data[2]);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * [前工程標準規格情報]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb オブジェクト
     * @param hinmei 品名
     * @param pattern ﾊﾟﾀｰﾝ
     * @param syurui 種類
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadDaMkhYoJunJokenData(QueryRunner queryRunnerQcdb, String hinmei, String pattern, String syurui) throws SQLException {
        // 前工程標準規格情報データの取得
        String sql = "SELECT kikakuti FROM da_mkhyojunjoken"
                + " WHERE hinmei = ? AND pattern = ? AND syurui = ? ";
        List<Object> params = new ArrayList<>();
        params.add(hinmei);
        params.add(pattern);
        params.add(syurui);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }
}
