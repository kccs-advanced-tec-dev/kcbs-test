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
import jp.co.kccs.xhd.db.model.SrSlipSyukkakensa;
import jp.co.kccs.xhd.pxhdo901.ErrorMessageInfo;
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

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/12/22<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B037(ｽﾘｯﾌﾟ作製・出荷検査)
 *
 * @author KCSS K.Jo
 * @since 2021/12/22
 */
public class GXHDO102B037 implements IFormLogic {

    private static final Logger LOGGER = Logger.getLogger(GXHDO102B037.class.getName());
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
    public GXHDO102B037() {
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
                    GXHDO102B037Const.BTN_EDABAN_COPY_TOP,
                    GXHDO102B037Const.BTN_KANSOUKAISHI1_TOP,
                    GXHDO102B037Const.BTN_KANSOUSYUURYOU1_TOP,
                    GXHDO102B037Const.BTN_KANSOUKAISHI2_TOP,
                    GXHDO102B037Const.BTN_KANSOUSYUURYOU2_TOP,
                    GXHDO102B037Const.BTN_SOKUTEI_TOP,
                    GXHDO102B037Const.BTN_EDABAN_COPY_BOTTOM,
                    GXHDO102B037Const.BTN_KANSOUKAISHI1_BOTTOM,
                    GXHDO102B037Const.BTN_KANSOUSYUURYOU1_BOTTOM,
                    GXHDO102B037Const.BTN_KANSOUKAISHI2_BOTTOM,
                    GXHDO102B037Const.BTN_KANSOUSYUURYOU2_BOTTOM,
                    GXHDO102B037Const.BTN_SOKUTEI_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO102B037Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO102B037Const.BTN_INSERT_TOP,
                    GXHDO102B037Const.BTN_DELETE_TOP,
                    GXHDO102B037Const.BTN_UPDATE_TOP,
                    GXHDO102B037Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO102B037Const.BTN_INSERT_BOTTOM,
                    GXHDO102B037Const.BTN_DELETE_BOTTOM,
                    GXHDO102B037Const.BTN_UPDATE_BOTTOM
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
            case GXHDO102B037Const.BTN_EDABAN_COPY_TOP:
            case GXHDO102B037Const.BTN_EDABAN_COPY_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 仮登録
            case GXHDO102B037Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO102B037Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO102B037Const.BTN_INSERT_TOP:
            case GXHDO102B037Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 修正
            case GXHDO102B037Const.BTN_UPDATE_TOP:
            case GXHDO102B037Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO102B037Const.BTN_DELETE_TOP:
            case GXHDO102B037Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 乾燥開始日時①
            case GXHDO102B037Const.BTN_KANSOUKAISHI1_TOP:
            case GXHDO102B037Const.BTN_KANSOUKAISHI1_BOTTOM:
                method = "setKansoukaishi1";
                break;
            // 乾燥終了日時①
            case GXHDO102B037Const.BTN_KANSOUSYUURYOU1_TOP:
            case GXHDO102B037Const.BTN_KANSOUSYUURYOU1_BOTTOM:
                method = "setKansousyuuryou1";
                break;
            // 乾燥開始日時②
            case GXHDO102B037Const.BTN_KANSOUKAISHI2_TOP:
            case GXHDO102B037Const.BTN_KANSOUKAISHI2_BOTTOM:
                method = "setKansoukaishi2";
                break;
            // 乾燥終了日時②
            case GXHDO102B037Const.BTN_KANSOUSYUURYOU2_TOP:
            case GXHDO102B037Const.BTN_KANSOUSYUURYOU2_BOTTOM:
                method = "setKansousyuuryou2";
                break;
            // 乾燥後正味重量
            case GXHDO102B037Const.BTN_KANSOUGOSYOUMIJYUURYOU_TOP:
            case GXHDO102B037Const.BTN_KANSOUGOSYOUMIJYUURYOU_BOTTOM:
                method = "setKansougosyoumijyuuryou";
                break;
            // 固形分比率
            case GXHDO102B037Const.BTN_KOKEIBUNHIRITSU_TOP:
            case GXHDO102B037Const.BTN_KOKEIBUNHIRITSU_BOTTOM:
                method = "setKokeibunhiritsu";
                break;
            // 測定日時
            case GXHDO102B037Const.BTN_SOKUTEI_TOP:
            case GXHDO102B037Const.BTN_SOKUTEI_BOTTOM:
                method = "setSokutei";
                break;
            // 収率(%)
            case GXHDO102B037Const.BTN_SYUURITSU_TOP:
            case GXHDO102B037Const.BTN_SYUURITSU_BOTTOM:
                method = "setSyuuritsu";
                break;
            // ｽﾘｯﾌﾟ有効期限
            case GXHDO102B037Const.BTN_SLIPYUUKOUKIGEN_TOP:
            case GXHDO102B037Const.BTN_SLIPYUUKOUKIGEN_BOTTOM:
                method = "setSlipyuukoukigen";
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

            // ｽﾘｯﾌﾟ作製・出荷検査の入力項目の登録データ(仮登録時は仮登録データ)を取得
            List<SrSlipSyukkakensa> srSlipSyukkakensaDataList = getSrSlipSyukkakensaData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo9, oyalotEdaban, paramJissekino);
            if (srSlipSyukkakensaDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // メイン画面データ設定
            setInputItemDataMainForm(processData, srSlipSyukkakensaDataList.get(0));

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
     * 乾燥開始日時①設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKansoukaishi1(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B037Const.KANSOUKAISHI1_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B037Const.KANSOUKAISHI1_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 乾燥終了日時①設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKansousyuuryou1(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B037Const.KANSOUSYUURYOU1_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B037Const.KANSOUSYUURYOU1_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 乾燥開始日時②設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKansoukaishi2(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B037Const.KANSOUKAISHI2_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B037Const.KANSOUKAISHI2_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 乾燥終了日時②設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKansousyuuryou2(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B037Const.KANSOUSYUURYOU2_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B037Const.KANSOUSYUURYOU2_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 乾燥後正味重量設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKansougosyoumijyuuryou(ProcessData processData) {
        // 乾燥後総重量
        FXHDD01 kansougosoujyuuryou = getItemRow(processData.getItemList(), GXHDO102B037Const.KANSOUGOSOUJYUURYOU);
        // ﾙﾂﾎﾞ風袋重量
        FXHDD01 rutsubohuutaijyuuryou = getItemRow(processData.getItemList(), GXHDO102B037Const.RUTSUBOHUUTAIJYUURYOU);
        if (kansougosoujyuuryou == null || rutsubohuutaijyuuryou == null) {
            processData.setMethod("");
            return processData;
        }
        // 乾燥後正味重量ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkKansougosyoumijyuuryou(kansougosoujyuuryou, rutsubohuutaijyuuryou);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        calcKansougosyoumijyuuryou(processData, kansougosoujyuuryou, rutsubohuutaijyuuryou);
        return processData;
    }

    /**
     * 乾燥後正味重量ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
     *
     * @param kansougosoujyuuryou 乾燥後総重量
     * @param rutsubohuutaijyuuryou ﾙﾂﾎﾞ風袋重量
     * @return エラーメッセージ情報
     */
    public ErrorMessageInfo checkKansougosyoumijyuuryou(FXHDD01 kansougosoujyuuryou, FXHDD01 rutsubohuutaijyuuryou) {

        //「乾燥後総重量」ﾁｪｯｸ
        if (StringUtil.isEmpty(kansougosoujyuuryou.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(kansougosoujyuuryou);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, true, errFxhdd01List, kansougosoujyuuryou.getLabel1());
        }
        //「ﾙﾂﾎﾞ風袋重量」ﾁｪｯｸ
        if (StringUtil.isEmpty(rutsubohuutaijyuuryou.getValue())) {
            List<FXHDD01> errFxhdd01List = Arrays.asList(rutsubohuutaijyuuryou);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, true, errFxhdd01List, rutsubohuutaijyuuryou.getLabel1());
        }
        // 乾燥後総重量<ﾙﾂﾎﾞ風袋重量の場合
        BigDecimal kansougosoujyuuryouVal = new BigDecimal(kansougosoujyuuryou.getValue());
        BigDecimal rutsubohuutaijyuuryouVal = new BigDecimal(rutsubohuutaijyuuryou.getValue());
        if (kansougosoujyuuryouVal.compareTo(rutsubohuutaijyuuryouVal) == -1) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(kansougosoujyuuryou, rutsubohuutaijyuuryou);
            return MessageUtil.getErrorMessageInfo("XHD-000023", true, true, errFxhdd01List, kansougosoujyuuryou.getLabel1(), rutsubohuutaijyuuryou.getLabel1());
        }
        return null;
    }

    /**
     * 乾燥後正味重量計算
     *
     * @param processData 処理制御データ
     * @param kansougosoujyuuryou 乾燥後総重量
     * @param rutsubohuutaijyuuryou ﾙﾂﾎﾞ風袋重量
     */
    private void calcKansougosyoumijyuuryou(ProcessData processData, FXHDD01 kansougosoujyuuryou, FXHDD01 rutsubohuutaijyuuryou) {
        try {
            // 乾燥後正味重量
            FXHDD01 kansougosyoumijyuuryou = getItemRow(processData.getItemList(), GXHDO102B037Const.KANSOUGOSYOUMIJYUURYOU);
            //「乾燥後正味重量」= 「乾燥後総重量」 - 「ﾙﾂﾎﾞ風袋重量」 を算出する。
            BigDecimal itemKansougosoujyuuryouVal = new BigDecimal(kansougosoujyuuryou.getValue());
            BigDecimal itemRutsubohuutaijyuuryouVal = new BigDecimal(rutsubohuutaijyuuryou.getValue());
            BigDecimal itemSyoumijyuuryouVal = itemKansougosoujyuuryouVal.subtract(itemRutsubohuutaijyuuryouVal);
            // 計算結果の設定
            kansougosyoumijyuuryou.setValue(itemSyoumijyuuryouVal.toPlainString());
        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
            ErrUtil.outputErrorLog("乾燥後正味重量計算にエラー発生", ex, LOGGER);
        }
    }

    /**
     * 固形分比率設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKokeibunhiritsu(ProcessData processData) {
        // 乾燥後正味重量
        FXHDD01 kansougosyoumijyuuryou = getItemRow(processData.getItemList(), GXHDO102B037Const.KANSOUGOSYOUMIJYUURYOU);
        // 乾燥前ｽﾘｯﾌﾟ重量
        FXHDD01 kansoumaeslipjyuuryou = getItemRow(processData.getItemList(), GXHDO102B037Const.KANSOUMAESLIPJYUURYOU);
        if (kansougosyoumijyuuryou == null || kansoumaeslipjyuuryou == null) {
            processData.setMethod("");
            return processData;
        }
        // 固形分比率ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkKokeibunhiritsuu(kansougosyoumijyuuryou, kansoumaeslipjyuuryou);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        calcKokeibunhiritsu(processData, kansougosyoumijyuuryou, kansoumaeslipjyuuryou);

        return processData;
    }

    /**
     * 固形分比率ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
     *
     * @param kansougosyoumijyuuryou 乾燥後正味重量
     * @param kansoumaeslipjyuuryou 乾燥前ｽﾘｯﾌﾟ重量
     * @return エラーメッセージ情報
     */
    public ErrorMessageInfo checkKokeibunhiritsuu(FXHDD01 kansougosyoumijyuuryou, FXHDD01 kansoumaeslipjyuuryou) {

        //「乾燥後正味重量」ﾁｪｯｸ
        if (StringUtil.isEmpty(kansougosyoumijyuuryou.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(kansougosyoumijyuuryou);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, true, errFxhdd01List, kansougosyoumijyuuryou.getLabel1());
        }
        //「乾燥前ｽﾘｯﾌﾟ重量」ﾁｪｯｸ
        if (NumberUtil.isZeroOrEmpty(kansoumaeslipjyuuryou.getValue())) {
            List<FXHDD01> errFxhdd01List = Arrays.asList(kansoumaeslipjyuuryou);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, true, errFxhdd01List, kansoumaeslipjyuuryou.getLabel1());
        }

        return null;
    }

    /**
     * 固形分比率計算
     *
     * @param processData 処理制御データ
     * @param kansougosyoumijyuuryou 乾燥後正味重量
     * @param kansoumaeslipjyuuryou 乾燥前ｽﾘｯﾌﾟ重量
     */
    private void calcKokeibunhiritsu(ProcessData processData, FXHDD01 kansougosyoumijyuuryou, FXHDD01 kansoumaeslipjyuuryou) {
        try {
            // 固形分比率
            FXHDD01 kokeibunhiritsu = getItemRow(processData.getItemList(), GXHDO102B037Const.KOKEIBUNHIRITSU);
            //「固形分比率」= 「乾燥後正味重量」 ÷ 「乾燥前ｽﾘｯﾌﾟ重量」 * 100(小数点第三位を四捨五入) → 式を変換して先に100を乗算
            BigDecimal itemKansougosyoumijyuuryouVal = new BigDecimal(kansougosyoumijyuuryou.getValue());
            BigDecimal itemKansoumaeslipjyuuryouVal = new BigDecimal(kansoumaeslipjyuuryou.getValue());
            BigDecimal itemKokeibunhiritsuVal = itemKansougosyoumijyuuryouVal.multiply(BigDecimal.valueOf(100)).divide(itemKansoumaeslipjyuuryouVal, 2, RoundingMode.HALF_UP);
            // 計算結果の設定
            kokeibunhiritsu.setValue(itemKokeibunhiritsuVal.toPlainString());
        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
            ErrUtil.outputErrorLog("固形分比率計算にエラー発生", ex, LOGGER);
        }
    }

    /**
     * 測定日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setSokutei(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B037Const.SOKUTEI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B037Const.SOKUTEI_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 収率(%)設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     * @throws SQLException 例外エラー
     */
    public ProcessData setSyuuritsu(ProcessData processData) throws SQLException {
        QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
        QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String lotNo = (String) session.getAttribute("lotNo");
        ErrorMessageInfo checkItemErrorInfo = checkSyuuritsuItemData(processData, queryRunnerDoc, queryRunnerQcdb, lotNo);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 収率(%)ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
     *
     * @param processData 処理制御データ
     * @param queryRunnerDoc QueryRunnerオブジェクト(DocServer)
     * @param queryRunnerQcdb QueryRunnerオブジェクト(Qcdb)
     * @param lotNo ﾛｯﾄNo
     * @return エラーメッセージ情報
     * @throws SQLException 例外エラー
     */
    public ErrorMessageInfo checkSyuuritsuItemData(ProcessData processData, QueryRunner queryRunnerDoc, QueryRunner queryRunnerQcdb, String lotNo) throws SQLException {
        String kojyo = lotNo.substring(0, 3);
        String lotNo9 = lotNo.substring(3, 12);
        String edaban = lotNo.substring(12, 15);
        String slipjyuuryougoukei = "ｽﾘｯﾌﾟ重量合計";
        String slipyoteijyuuryou = "ｽﾘｯﾌﾟ予定重量";
        String youzaityouseiryou = "溶剤調整量";
        String slipjyuuryougoukeiVal;// ｽﾘｯﾌﾟ重量合計
        String youzaityouseiryouVal;// 溶剤調整量
        // [ｽﾘｯﾌﾟ作成・FP(ﾊﾞｹﾂ)]から、ﾃﾞｰﾀを取得
        Map fpBaketsuData = loadFpBaketsuData(queryRunnerQcdb, kojyo, lotNo9, edaban);
        slipjyuuryougoukeiVal = StringUtil.nullToBlank(getMapData(fpBaketsuData, "slipjyuuryougoukei"));
        if (StringUtil.isEmpty(slipjyuuryougoukeiVal)) {
            // [ｽﾘｯﾌﾟ作製・FP(成形ﾀﾝｸ)]から、ﾃﾞｰﾀを取得
            Map fpSeikeitankData = loadFpSeikeitankData(queryRunnerQcdb, kojyo, lotNo9, edaban);
            slipjyuuryougoukeiVal = StringUtil.nullToBlank(getMapData(fpSeikeitankData, "slipjyuuryougoukei"));
            if (StringUtil.isEmpty(slipjyuuryougoukeiVal)) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                return MessageUtil.getErrorMessageInfo("XHD-000037", false, false, new ArrayList<>(), slipjyuuryougoukei);
            }
        }
        if (!StringUtil.isEmpty(slipjyuuryougoukeiVal)) {
            // [ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合]から、ﾃﾞｰﾀを取得
            Map binderkongouData = loadBinderkongouData(queryRunnerQcdb, kojyo, lotNo9, edaban);
            // ｽﾘｯﾌﾟ予定重量
            String slipyoteijyuuryouVal = StringUtil.nullToBlank(getMapData(binderkongouData, "slipyoteijyuuryou"));
            if (binderkongouData == null || binderkongouData.isEmpty() || StringUtil.isEmpty(slipyoteijyuuryouVal)) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                return MessageUtil.getErrorMessageInfo("XHD-000037", false, false, new ArrayList<>(), slipyoteijyuuryou);
            }
            // [ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固定分測定]から、ﾃﾞｰﾀを取得
            Map kokeibunsokuteData = loadKokeibunsokuteData(queryRunnerQcdb, kojyo, lotNo9, edaban);
            youzaityouseiryouVal = StringUtil.nullToBlank(getMapData(kokeibunsokuteData, "youzaityouseiryou"));
            if (kokeibunsokuteData == null || kokeibunsokuteData.isEmpty() || StringUtil.isEmpty(youzaityouseiryouVal)) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                return MessageUtil.getErrorMessageInfo("XHD-000037", false, false, new ArrayList<>(), youzaityouseiryou);
            }
            // ｽﾘｯﾌﾟ予定重量、溶剤調整量の両方が0だった場合、エラー
            if (NumberUtil.isZero(slipyoteijyuuryouVal) && NumberUtil.isZero(youzaityouseiryouVal)) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                return MessageUtil.getErrorMessageInfo("XHD-000210", false, false, new ArrayList<>(), "ｽﾘｯﾌﾟ予定重量、溶剤調整量");
            }
            calcSyuuritsu(processData, slipjyuuryougoukeiVal, slipyoteijyuuryouVal, youzaityouseiryouVal);
        }
        return null;
    }

    /**
     * 収率(%)計算
     *
     * @param processData 処理制御データ
     * @param slipjyuuryougoukeiVal ｽﾘｯﾌﾟ重量合計
     * @param slipyoteijyuuryouVal ｽﾘｯﾌﾟ予定重量
     * @param youzaityouseiryouVal 溶剤調整量
     */
    private void calcSyuuritsu(ProcessData processData, String slipjyuuryougoukeiVal, String slipyoteijyuuryouVal, String youzaityouseiryouVal) {
        try {
            // 収率(%)
            FXHDD01 syuuritsu = getItemRow(processData.getItemList(), GXHDO102B037Const.SYUURITSU);
            //「収率(%)」= ｽﾘｯﾌﾟ重量合計 ÷ (ｽﾘｯﾌﾟ予定重量 + 溶剤調整量)
            BigDecimal itemSlipjyuuryougoukeiVal = new BigDecimal(Integer.parseInt(slipjyuuryougoukeiVal));
            BigDecimal itemSlipyoteijyuuryouVal = new BigDecimal(Integer.parseInt(slipyoteijyuuryouVal));
            BigDecimal itemYouzaityouseiryouVal = new BigDecimal(Integer.parseInt(youzaityouseiryouVal));

            BigDecimal itemKokeibunhiritsuVal = itemSlipjyuuryougoukeiVal.divide(itemSlipyoteijyuuryouVal.add(itemYouzaityouseiryouVal), 2, RoundingMode.HALF_UP);
            // 計算結果の設定
            syuuritsu.setValue(itemKokeibunhiritsuVal.toPlainString());
        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
            ErrUtil.outputErrorLog("収率(%)計算にエラー発生", ex, LOGGER);
        }
    }

    /**
     * ｽﾘｯﾌﾟ有効期限設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     * @throws SQLException 例外エラー
     */
    public ProcessData setSlipyuukoukigen(ProcessData processData) throws SQLException {
        QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
        QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String lotNo = (String) session.getAttribute("lotNo");
        String tantoshaCd = (String) session.getAttribute("tantoshaCd");
        // 前工程WIPから仕掛情報を取得処理
        Map shikakariData = loadShikakariDataFromWip(queryRunnerDoc, tantoshaCd, lotNo);
        ErrorMessageInfo checkItemErrorInfo = checkSlipyuukoukigenuItemData(processData, queryRunnerDoc, queryRunnerQcdb, shikakariData, lotNo);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        if (processData.getErrorMessageInfoList() != null && !processData.getErrorMessageInfoList().isEmpty()) {
            return processData;
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * ｽﾘｯﾌﾟ有効期限ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
     *
     * @param processData 処理制御データ
     * @param queryRunnerDoc QueryRunnerオブジェクト(DocServer)
     * @param queryRunnerQcdb QueryRunnerオブジェクト(Qcdb)
     * @param shikakariData 前工程WIPから仕掛情報
     * @param lotNo ﾛｯﾄNo
     * @return エラーメッセージ情報
     * @throws SQLException 例外エラー
     */
    public ErrorMessageInfo checkSlipyuukoukigenuItemData(ProcessData processData, QueryRunner queryRunnerDoc, QueryRunner queryRunnerQcdb, Map shikakariData, String lotNo) throws SQLException {
        String kojyo = lotNo.substring(0, 3);
        String lotNo9 = lotNo.substring(3, 12);
        String edaban = lotNo.substring(12, 15);
        String syurui = "ｽﾘｯﾌﾟ作製";
        String kikakuJoken = "規格情報";
        String binderkongousyuuryounichiji = "ﾊﾞｲﾝﾀﾞｰ混合終了時間";
        String kikakutiChi = null;
        Map binderkongousyuuryounichijiData = new HashMap<>();
        // [ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合]から、ﾃﾞｰﾀを取得
        Map binderkongouData = loadBinderkongouData(queryRunnerQcdb, kojyo, lotNo9, edaban);
        // ﾊﾞｲﾝﾀﾞｰ混合終了時間
        String binderkongousyuuryounichijiVal = StringUtil.nullToBlank(getMapData(binderkongouData, "binderkongousyuuryounichiji"));
        if (binderkongouData == null || binderkongouData.isEmpty() || StringUtil.isEmpty(binderkongousyuuryounichijiVal)) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            return MessageUtil.getErrorMessageInfo("XHD-000037", false, false, new ArrayList<>(), binderkongousyuuryounichiji);
        }
        // [前工程設計]から、ﾃﾞｰﾀを取得
        Map daMkSekKeiData = loadDaMkSekKeiData(queryRunnerQcdb, kojyo, lotNo9, edaban, syurui);
        // 設計No
        String sekkeiNo = StringUtil.nullToBlank(getMapData(daMkSekKeiData, "sekkeiNo"));
        // ﾊﾟﾀｰﾝ
        String pattern = StringUtil.nullToBlank(getMapData(daMkSekKeiData, "pattern"));
        if (daMkSekKeiData == null || daMkSekKeiData.isEmpty()) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            return MessageUtil.getErrorMessageInfo("XHD-000028", false, false, new ArrayList<>(), kikakuJoken);
        }
        // [前工程規格情報]から、ﾃﾞｰﾀを取得
        Map daMkJokenData = loadDaMkJokenData(queryRunnerQcdb, sekkeiNo);
        // 前工程規格情報の規格値
        kikakutiChi = StringUtil.nullToBlank(getMapData(daMkJokenData, "kikakuti"));
        if (daMkJokenData == null || daMkJokenData.isEmpty() || StringUtil.isEmpty(kikakutiChi)) {
            // [前工程標準規格情報]から、ﾃﾞｰﾀを取得
            Map daMkhYoJunJokenData = loadDaMkhYoJunJokenData(queryRunnerQcdb, (String) shikakariData.get("hinmei"), pattern);
            kikakutiChi = StringUtil.nullToBlank(getMapData(daMkhYoJunJokenData, "kikakuti"));
            if (daMkhYoJunJokenData == null || daMkhYoJunJokenData.isEmpty() || StringUtil.isEmpty(kikakutiChi)) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                return MessageUtil.getErrorMessageInfo("XHD-000028", false, false, new ArrayList<>(), kikakuJoken);
            }
        }
        // ﾊﾞｲﾝﾀﾞｰ混合終了時間
        binderkongousyuuryounichijiData.put("binderkongousyuuryounichiji", binderkongouData.get("binderkongousyuuryounichiji"));
        calcSlipyuukoukigen(processData, binderkongousyuuryounichijiData, kikakutiChi);

        return null;
    }

    /**
     * ｽﾘｯﾌﾟ有効期限計算
     *
     * @param processData 処理制御データ
     * @param binderkongousyuuryounichijiVal ﾊﾞｲﾝﾀﾞｰ混合終了時間
     * @param kikakutiChi 規格値
     */
    private void calcSlipyuukoukigen(ProcessData processData, Map binderkongousyuuryounichijiVal, String kikakutiChi) {
        try {
            // ｽﾘｯﾌﾟ有効期限
            FXHDD01 syuuritsu = getItemRow(processData.getItemList(), GXHDO102B037Const.SLIPYUUKOUKIGEN);

            // ｽﾘｯﾌﾟ有効期限 = ﾊﾞｲﾝﾀﾞｰ混合終了時間 + 取得した規格値
            BigDecimal itemKikakuChi = ValidateUtil.numberExtraction(StringUtil.nullToBlank(kikakutiChi).replace("【", "").replace("】", ""));
            String binderkongousyuuryounichiji = DateUtil.formattedTimestamp((Timestamp) binderkongousyuuryounichijiVal.get("binderkongousyuuryounichiji"), "yyMMdd");
            Date dateTime = DateUtil.addJikan(binderkongousyuuryounichiji, "0000", itemKikakuChi.intValue(), Calendar.DATE);
            if (dateTime != null) {
                // 計算結果の設定
                syuuritsu.setValue(new SimpleDateFormat("yyMMdd").format(dateTime));
            }
        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
            ErrUtil.outputErrorLog("ｽﾘｯﾌﾟ有効期限計算に" + ex.getClass().getSimpleName() + "エラー発生", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
        }
    }

    /**
     * [ｽﾘｯﾌﾟ作成・FP(ﾊﾞｹﾂ)]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb オブジェクト
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo LotNo
     * @param edaban 枝番
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFpBaketsuData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban) throws SQLException {
        // ｽﾘｯﾌﾟ重量合計、ﾊﾞｲﾝﾀﾞｰ混合終了日時の取得
        String sql = "SELECT slipjyuuryougoukei FROM sr_slip_fp_baketsu"
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? ";
        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * [ｽﾘｯﾌﾟ作製・FP(成形ﾀﾝｸ)]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb オブジェクト
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo LotNo
     * @param edaban 枝番
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFpSeikeitankData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban) throws SQLException {
        // ｽﾘｯﾌﾟ重量合計の取得
        String sql = "SELECT slipjyuuryougoukei FROM sr_slip_fp_seikeitank"
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? ";
        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * [ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb オブジェクト
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo LotNo
     * @param edaban 枝番
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadBinderkongouData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban) throws SQLException {
        // ｽﾘｯﾌﾟ予定重量の取得
        String sql = "SELECT slipyoteijyuuryou, binderkongousyuuryounichiji FROM sr_slip_binderkongou"
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? ";
        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * [ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固定分測定]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb オブジェクト
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo LotNo
     * @param edaban 枝番
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadKokeibunsokuteData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban) throws SQLException {
        // 溶剤調整量の取得
        String sql = "SELECT youzaityouseiryou FROM sr_slip_slipkokeibunsokutei"
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? ";
        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
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
     * [前工程規格情報]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb オブジェクト
     * @param sekkeiNo 設計No
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadDaMkJokenData(QueryRunner queryRunnerQcdb, String sekkeiNo) throws SQLException {
        // 前工程規格情報データの取得
        String sql = "SELECT kikakuti FROM da_mkjoken"
                + " WHERE sekkeino = ? AND kouteimei = ? AND koumokumei = ? AND kanrikoumokumei = ? ";
        List<Object> params = new ArrayList<>();
        params.add(sekkeiNo);
        params.add("ｽﾘｯﾌﾟ");
        params.add("出荷検査");
        params.add("ｽﾘｯﾌﾟ有効期限");
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * [前工程標準規格情報]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb オブジェクト
     * @param hinmei 品名
     * @param pattern ﾊﾟﾀｰﾝ
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadDaMkhYoJunJokenData(QueryRunner queryRunnerQcdb, String hinmei, String pattern) throws SQLException {
        // 前工程標準規格情報データの取得
        String sql = "SELECT kikakuti FROM da_mkhyojunjoken"
                + " WHERE hinmei = ? AND pattern = ? AND kouteimei = ? AND koumokumei = ? AND kanrikoumokumei = ? ";
        List<Object> params = new ArrayList<>();
        params.add(hinmei);
        params.add(pattern);
        params.add("ｽﾘｯﾌﾟ");
        params.add("出荷検査");
        params.add("ｽﾘｯﾌﾟ有効期限");
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
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

                // ｽﾘｯﾌﾟ作製・出荷検査_仮登録登録処理
                insertTmpSrSlipSyukkakensa(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, jissekiNo, strSystime, processData, formId);
            } else {

                // ｽﾘｯﾌﾟ作製・出荷検査_仮登録更新処理
                updateTmpSrSlipSyukkakensa(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, jissekiNo, strSystime, processData);
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
     * 規格値の入力値チェックを行う。
     * 規格値のエラー対象は引数のリスト(kikakuchiInputErrorInfoList)に項目情報を詰めて返される。
     *
     * @param processData 処理データ
     * @param kikakuchiInputErrorInfoList 規格値入力エラー情報リスト
     * @return チェックの正常終了時はNULL、異常時は内容に応じたエラーメッセージ情報を返す。
     */
    private ErrorMessageInfo checkInputKikakuchi(ProcessData processData, List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList) {

        // 乾燥時間①ﾁｪｯｸ
        List<String> kansoukaisi1List = Arrays.asList(GXHDO102B037Const.KANSOUKAISHI1_DAY, GXHDO102B037Const.KANSOUKAISHI1_TIME);
        List<String> kansousyuuryou1List = Arrays.asList(GXHDO102B037Const.KANSOUSYUURYOU1_DAY, GXHDO102B037Const.KANSOUSYUURYOU1_TIME);
        // 乾燥時間②ﾁｪｯｸ
        List<String> kansoukaisi2List = Arrays.asList(GXHDO102B037Const.KANSOUKAISHI2_DAY, GXHDO102B037Const.KANSOUKAISHI2_TIME);
        List<String> kansousyuuryou2List = Arrays.asList(GXHDO102B037Const.KANSOUSYUURYOU2_DAY, GXHDO102B037Const.KANSOUSYUURYOU2_TIME);
        // 規格値の入力値チェック必要の項目リスト
        List<FXHDD01> itemList = new ArrayList<>();
        // 乾燥時間①の時間差数
        FXHDD01 kansoukaisi1DiffMinutes = getDiffMinutes(processData, GXHDO102B037Const.KANSOUJIKAN1, kansoukaisi1List, kansousyuuryou1List);
        // 乾燥時間②の時間差数
        FXHDD01 kansoukaisi2DiffMinutes = getDiffMinutes(processData, GXHDO102B037Const.KANSOUJIKAN2, kansoukaisi2List, kansousyuuryou2List);

        // 項目の項目名を設置
        if (kansoukaisi1DiffMinutes != null) {
            kansoukaisi1DiffMinutes.setLabel1("乾燥時間①");
            itemList.add(kansoukaisi1DiffMinutes);
        }
        if (kansoukaisi2DiffMinutes != null) {
            kansoukaisi2DiffMinutes.setLabel1("乾燥時間②");
            itemList.add(kansoukaisi2DiffMinutes);
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
        setItemBackColor(processData, kansoukaisi1List, kansousyuuryou1List, "乾燥時間①", kikakuchiInputErrorInfoList, errorMessageInfo);
        setItemBackColor(processData, kansoukaisi2List, kansousyuuryou2List, "乾燥時間②", kikakuchiInputErrorInfoList, errorMessageInfo);

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
        int diffHours = 0;
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
                itemFxhdd01Clone = itemKakuhankaisiDay.clone();
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
     * @param kakuhankaisiList 開始時間項目リスト
     * @param kakuhansyuuryouList 終了時間項目リスト
     * @param label 項目の項目名
     * @param kikakuchiInputErrorInfoList 規格値入力エラー情報リスト
     * @param errorMessageInfo エラーメッセージ情報
     */
    private void setItemBackColor(ProcessData processData, List<String> kaisijikanList, List<String> syuuryoujikanList, String label, List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList,
            ErrorMessageInfo errorMessageInfo) {

        List<String> errorItemLabelList = new ArrayList<>();
        // エラー項目の背景色を設定
        kikakuchiInputErrorInfoList.stream().forEachOrdered((errorInfo) -> {
            errorItemLabelList.add(errorInfo.getItemLabel());
        });
        if (errorMessageInfo != null && !errorMessageInfo.getErrorMessage().contains(label)) {
            return;
        }
        if (errorItemLabelList.contains(label) || (errorMessageInfo != null && errorMessageInfo.getErrorMessage().contains(label))) {
            List<String> itemList = new ArrayList<>();
            itemList.addAll(kaisijikanList);
            itemList.addAll(syuuryoujikanList);

            itemList.stream().map((jikanName) -> getItemRow(processData.getItemList(), jikanName)).forEachOrdered((itemFxhdd01) -> {
                itemFxhdd01.setBackColorInput(ErrUtil.ERR_BACK_COLOR);
            });
        }
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
            SrSlipSyukkakensa tmpSrSlipSyukkakensa = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrSlipSyukkakensa> srSlipSyukkakensaList = getSrSlipSyukkakensaData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo9, edaban, jissekiNo);
                if (!srSlipSyukkakensaList.isEmpty()) {
                    tmpSrSlipSyukkakensa = srSlipSyukkakensaList.get(0);
                }

                deleteTmpSrSlipSyukkakensa(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban, jissekiNo);
            }

            // ｽﾘｯﾌﾟ作製・出荷検査_登録処理
            insertSrSlipSyukkakensa(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, jissekiNo, strSystime, processData, tmpSrSlipSyukkakensa, formId);

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
        processData.setUserAuthParam(GXHDO102B037Const.USER_AUTH_UPDATE_PARAM);

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

            // ｽﾘｯﾌﾟ作製・出荷検査_更新処理
            updateSrSlipSyukkakensa(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, jissekiNo, strSystime, processData);

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

        FXHDD01 itemKansoukaishi1Day = getItemRow(processData.getItemList(), GXHDO102B037Const.KANSOUKAISHI1_DAY);  // 乾燥開始日①
        FXHDD01 itemKansoukaishi1Time = getItemRow(processData.getItemList(), GXHDO102B037Const.KANSOUKAISHI1_TIME); // 乾燥開始時間①
        FXHDD01 itemKansousyuuryou1Day = getItemRow(processData.getItemList(), GXHDO102B037Const.KANSOUSYUURYOU1_DAY);    // 乾燥終了日①
        FXHDD01 itemKansousyuuryou1Time = getItemRow(processData.getItemList(), GXHDO102B037Const.KANSOUSYUURYOU1_TIME); // 乾燥終了時間①
        if (itemKansoukaishi1Day != null && itemKansoukaishi1Time != null && itemKansousyuuryou1Day != null && itemKansousyuuryou1Time != null) {
            // 画面.乾燥開始日① + 画面.乾燥開始時間①
            Date kansoukaisiDate = DateUtil.convertStringToDate(itemKansoukaishi1Day.getValue(), itemKansoukaishi1Time.getValue());
            // 画面.乾燥終了日① + 画面.乾燥終了時間①
            Date kansousyuuryouDate = DateUtil.convertStringToDate(itemKansousyuuryou1Day.getValue(), itemKansousyuuryou1Time.getValue());
            // R001チェック呼出し
            String msgKansoukaisiCheckR001 = validateUtil.checkR001("乾燥開始日時①", kansoukaisiDate, "乾燥終了日時①", kansousyuuryouDate);
            if (!StringUtil.isEmpty(msgKansoukaisiCheckR001)) {
                // エラー発生時
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemKansoukaishi1Day, itemKansoukaishi1Time, itemKansousyuuryou1Day, itemKansousyuuryou1Time);
                return MessageUtil.getErrorMessageInfo("", msgKansoukaisiCheckR001, true, true, errFxhdd01List);
            }
        }

        FXHDD01 itemKansoukaishi2Day = getItemRow(processData.getItemList(), GXHDO102B037Const.KANSOUKAISHI2_DAY);  // 乾燥開始日②
        FXHDD01 itemKansoukaishi2Time = getItemRow(processData.getItemList(), GXHDO102B037Const.KANSOUKAISHI2_TIME); // 乾燥開始時間②
        FXHDD01 itemKansousyuuryou2Day = getItemRow(processData.getItemList(), GXHDO102B037Const.KANSOUSYUURYOU2_DAY);    // 乾燥終了日②
        FXHDD01 itemKansousyuuryou2Time = getItemRow(processData.getItemList(), GXHDO102B037Const.KANSOUSYUURYOU2_TIME); // 乾燥終了時間②
        if (itemKansoukaishi2Day != null && itemKansoukaishi2Time != null && itemKansousyuuryou2Day != null && itemKansousyuuryou2Time != null) {
            // 画面.乾燥開始日① + 画面.乾燥開始時間①
            Date kansoukaisiDate = DateUtil.convertStringToDate(itemKansoukaishi2Day.getValue(), itemKansoukaishi2Time.getValue());
            // 画面.乾燥終了日① + 画面.乾燥終了時間①
            Date kansousyuuryouDate = DateUtil.convertStringToDate(itemKansousyuuryou2Day.getValue(), itemKansousyuuryou2Time.getValue());
            // R001チェック呼出し
            String msgKansoukaisiCheckR001 = validateUtil.checkR001("乾燥開始日時②", kansoukaisiDate, "乾燥終了日時②", kansousyuuryouDate);
            if (!StringUtil.isEmpty(msgKansoukaisiCheckR001)) {
                // エラー発生時
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemKansoukaishi2Day, itemKansoukaishi2Time, itemKansousyuuryou2Day, itemKansousyuuryou2Time);
                return MessageUtil.getErrorMessageInfo("", msgKansoukaisiCheckR001, true, true, errFxhdd01List);
            }
        }
        return null;
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
        processData.setUserAuthParam(GXHDO102B037Const.USER_AUTH_DELETE_PARAM);

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

            // ｽﾘｯﾌﾟ作製・出荷検査_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo9, edaban, paramJissekino);
            insertDeleteDataTmpSrSlipSyukkakensa(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo9, edaban, paramJissekino, strSystime);

            // ｽﾘｯﾌﾟ作製・出荷検査_削除処理
            deleteSrSlipSyukkakensa(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban, paramJissekino);

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
                        GXHDO102B037Const.BTN_UPDATE_TOP,
                        GXHDO102B037Const.BTN_DELETE_TOP,
                        GXHDO102B037Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B037Const.BTN_KANSOUKAISHI1_TOP,
                        GXHDO102B037Const.BTN_KANSOUSYUURYOU1_TOP,
                        GXHDO102B037Const.BTN_KANSOUKAISHI2_TOP,
                        GXHDO102B037Const.BTN_KANSOUSYUURYOU2_TOP,
                        GXHDO102B037Const.BTN_KANSOUGOSYOUMIJYUURYOU_TOP,
                        GXHDO102B037Const.BTN_KOKEIBUNHIRITSU_TOP,
                        GXHDO102B037Const.BTN_SOKUTEI_TOP,
                        GXHDO102B037Const.BTN_SYUURITSU_TOP,
                        GXHDO102B037Const.BTN_SLIPYUUKOUKIGEN_TOP,
                        GXHDO102B037Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B037Const.BTN_DELETE_BOTTOM,
                        GXHDO102B037Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B037Const.BTN_KANSOUKAISHI1_BOTTOM,
                        GXHDO102B037Const.BTN_KANSOUSYUURYOU1_BOTTOM,
                        GXHDO102B037Const.BTN_KANSOUKAISHI2_BOTTOM,
                        GXHDO102B037Const.BTN_KANSOUSYUURYOU2_BOTTOM,
                        GXHDO102B037Const.BTN_KANSOUGOSYOUMIJYUURYOU_BOTTOM,
                        GXHDO102B037Const.BTN_KOKEIBUNHIRITSU_BOTTOM,
                        GXHDO102B037Const.BTN_SOKUTEI_BOTTOM,
                        GXHDO102B037Const.BTN_SYUURITSU_BOTTOM,
                        GXHDO102B037Const.BTN_SLIPYUUKOUKIGEN_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B037Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B037Const.BTN_INSERT_TOP,
                        GXHDO102B037Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B037Const.BTN_INSERT_BOTTOM));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO102B037Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B037Const.BTN_INSERT_TOP,
                        GXHDO102B037Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B037Const.BTN_KANSOUKAISHI1_TOP,
                        GXHDO102B037Const.BTN_KANSOUSYUURYOU1_TOP,
                        GXHDO102B037Const.BTN_KANSOUKAISHI2_TOP,
                        GXHDO102B037Const.BTN_KANSOUSYUURYOU2_TOP,
                        GXHDO102B037Const.BTN_KANSOUGOSYOUMIJYUURYOU_TOP,
                        GXHDO102B037Const.BTN_KOKEIBUNHIRITSU_TOP,
                        GXHDO102B037Const.BTN_SOKUTEI_TOP,
                        GXHDO102B037Const.BTN_SYUURITSU_TOP,
                        GXHDO102B037Const.BTN_SLIPYUUKOUKIGEN_TOP,
                        GXHDO102B037Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B037Const.BTN_INSERT_BOTTOM,
                        GXHDO102B037Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B037Const.BTN_KANSOUKAISHI1_BOTTOM,
                        GXHDO102B037Const.BTN_KANSOUSYUURYOU1_BOTTOM,
                        GXHDO102B037Const.BTN_KANSOUKAISHI2_BOTTOM,
                        GXHDO102B037Const.BTN_KANSOUSYUURYOU2_BOTTOM,
                        GXHDO102B037Const.BTN_KANSOUGOSYOUMIJYUURYOU_BOTTOM,
                        GXHDO102B037Const.BTN_KOKEIBUNHIRITSU_BOTTOM,
                        GXHDO102B037Const.BTN_SOKUTEI_BOTTOM,
                        GXHDO102B037Const.BTN_SYUURITSU_BOTTOM,
                        GXHDO102B037Const.BTN_SLIPYUUKOUKIGEN_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B037Const.BTN_UPDATE_TOP,
                        GXHDO102B037Const.BTN_DELETE_TOP,
                        GXHDO102B037Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B037Const.BTN_DELETE_BOTTOM
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
        int paramJissekino = 1;
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
        this.setItemData(processData, GXHDO102B037Const.WIPLOTNO, lotNo);
        // ｽﾘｯﾌﾟ品名
        this.setItemData(processData, GXHDO102B037Const.SLIPHINMEI, StringUtil.nullToBlank(getMapData(shikakariData, "hinmei")));
        // ｽﾘｯﾌﾟLotNo
        this.setItemData(processData, GXHDO102B037Const.SLIPLOTNO, StringUtil.nullToBlank(getMapData(shikakariData, "lotno")));
        // ﾛｯﾄ区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode"));
        //ﾛｯﾄ区分名
        String lotkubun = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubun"));
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO102B037Const.LOTKUBUN, "");
        } else {
            if (!StringUtil.isEmpty(lotkubun)) {
                lotkubuncode = lotkubuncode + ":" + lotkubun;
            }
            this.setItemData(processData, GXHDO102B037Const.LOTKUBUN, lotkubuncode);
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

        List<SrSlipSyukkakensa> srSlipSyukkakensaList = new ArrayList<>();
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

            // ｽﾘｯﾌﾟ作製・出荷検査データ取得
            srSlipSyukkakensaList = getSrSlipSyukkakensaData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo9, edaban, jissekino);
            if (srSlipSyukkakensaList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srSlipSyukkakensaList.isEmpty()) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srSlipSyukkakensaList.get(0));

        return true;

    }

    /**
     * データ設定処理
     *
     * @param processData 処理制御データ
     * @param srSlipSyukkakensa ｽﾘｯﾌﾟ作製・出荷検査データ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrSlipSyukkakensa srSlipSyukkakensa) {

        // ﾙﾂﾎﾞNo
        this.setItemData(processData, GXHDO102B037Const.RUTSUBONO, getSrSlipSyukkakensaItemData(GXHDO102B037Const.RUTSUBONO, srSlipSyukkakensa));

        // ﾙﾂﾎﾞ風袋重量
        this.setItemData(processData, GXHDO102B037Const.RUTSUBOHUUTAIJYUURYOU, getSrSlipSyukkakensaItemData(GXHDO102B037Const.RUTSUBOHUUTAIJYUURYOU, srSlipSyukkakensa));

        // 乾燥前ｽﾘｯﾌﾟ重量
        this.setItemData(processData, GXHDO102B037Const.KANSOUMAESLIPJYUURYOU, getSrSlipSyukkakensaItemData(GXHDO102B037Const.KANSOUMAESLIPJYUURYOU, srSlipSyukkakensa));

        // 乾燥開始日①
        this.setItemData(processData, GXHDO102B037Const.KANSOUKAISHI1_DAY, getSrSlipSyukkakensaItemData(GXHDO102B037Const.KANSOUKAISHI1_DAY, srSlipSyukkakensa));

        // 乾燥開始時間①
        this.setItemData(processData, GXHDO102B037Const.KANSOUKAISHI1_TIME, getSrSlipSyukkakensaItemData(GXHDO102B037Const.KANSOUKAISHI1_TIME, srSlipSyukkakensa));

        // 乾燥終了日①
        this.setItemData(processData, GXHDO102B037Const.KANSOUSYUURYOU1_DAY, getSrSlipSyukkakensaItemData(GXHDO102B037Const.KANSOUSYUURYOU1_DAY, srSlipSyukkakensa));

        // 乾燥終了時間①
        this.setItemData(processData, GXHDO102B037Const.KANSOUSYUURYOU1_TIME, getSrSlipSyukkakensaItemData(GXHDO102B037Const.KANSOUSYUURYOU1_TIME, srSlipSyukkakensa));

        // 乾燥開始日②
        this.setItemData(processData, GXHDO102B037Const.KANSOUKAISHI2_DAY, getSrSlipSyukkakensaItemData(GXHDO102B037Const.KANSOUKAISHI2_DAY, srSlipSyukkakensa));

        // 乾燥開始時間②
        this.setItemData(processData, GXHDO102B037Const.KANSOUKAISHI2_TIME, getSrSlipSyukkakensaItemData(GXHDO102B037Const.KANSOUKAISHI2_TIME, srSlipSyukkakensa));

        // 乾燥終了日②
        this.setItemData(processData, GXHDO102B037Const.KANSOUSYUURYOU2_DAY, getSrSlipSyukkakensaItemData(GXHDO102B037Const.KANSOUSYUURYOU2_DAY, srSlipSyukkakensa));

        // 乾燥終了時間②
        this.setItemData(processData, GXHDO102B037Const.KANSOUSYUURYOU2_TIME, getSrSlipSyukkakensaItemData(GXHDO102B037Const.KANSOUSYUURYOU2_TIME, srSlipSyukkakensa));

        // 乾燥後総重量
        this.setItemData(processData, GXHDO102B037Const.KANSOUGOSOUJYUURYOU, getSrSlipSyukkakensaItemData(GXHDO102B037Const.KANSOUGOSOUJYUURYOU, srSlipSyukkakensa));

        // 乾燥後正味重量
        this.setItemData(processData, GXHDO102B037Const.KANSOUGOSYOUMIJYUURYOU, getSrSlipSyukkakensaItemData(GXHDO102B037Const.KANSOUGOSYOUMIJYUURYOU, srSlipSyukkakensa));

        // 固形分比率
        this.setItemData(processData, GXHDO102B037Const.KOKEIBUNHIRITSU, getSrSlipSyukkakensaItemData(GXHDO102B037Const.KOKEIBUNHIRITSU, srSlipSyukkakensa));

        // 固形分測定担当者
        this.setItemData(processData, GXHDO102B037Const.KOKEIBUNSOKUTEITANTOUSYA, getSrSlipSyukkakensaItemData(GXHDO102B037Const.KOKEIBUNSOKUTEITANTOUSYA, srSlipSyukkakensa));

        // 測定号機
        this.setItemData(processData, GXHDO102B037Const.SOKUTEIGOUKI, getSrSlipSyukkakensaItemData(GXHDO102B037Const.SOKUTEIGOUKI, srSlipSyukkakensa));

        // 測定日
        this.setItemData(processData, GXHDO102B037Const.SOKUTEI_DAY, getSrSlipSyukkakensaItemData(GXHDO102B037Const.SOKUTEI_DAY, srSlipSyukkakensa));

        // 測定時間
        this.setItemData(processData, GXHDO102B037Const.SOKUTEI_TIME, getSrSlipSyukkakensaItemData(GXHDO102B037Const.SOKUTEI_TIME, srSlipSyukkakensa));

        // 粘度測定結果
        this.setItemData(processData, GXHDO102B037Const.NENDOSOKUTEIKEKKA, getSrSlipSyukkakensaItemData(GXHDO102B037Const.NENDOSOKUTEIKEKKA, srSlipSyukkakensa));

        // 温度測定結果
        this.setItemData(processData, GXHDO102B037Const.ONDOSOKUTEIKEKKA, getSrSlipSyukkakensaItemData(GXHDO102B037Const.ONDOSOKUTEIKEKKA, srSlipSyukkakensa));

        // 粘度測定担当者
        this.setItemData(processData, GXHDO102B037Const.NENDOSOKUTEITANTOUSYA, getSrSlipSyukkakensaItemData(GXHDO102B037Const.NENDOSOKUTEITANTOUSYA, srSlipSyukkakensa));

        // 収率(%)
        this.setItemData(processData, GXHDO102B037Const.SYUURITSU, getSrSlipSyukkakensaItemData(GXHDO102B037Const.SYUURITSU, srSlipSyukkakensa));

        // ｽﾘｯﾌﾟ有効期限
        this.setItemData(processData, GXHDO102B037Const.SLIPYUUKOUKIGEN, getSrSlipSyukkakensaItemData(GXHDO102B037Const.SLIPYUUKOUKIGEN, srSlipSyukkakensa));

        // 備考1
        this.setItemData(processData, GXHDO102B037Const.BIKOU1, getSrSlipSyukkakensaItemData(GXHDO102B037Const.BIKOU1, srSlipSyukkakensa));

        // 備考2
        this.setItemData(processData, GXHDO102B037Const.BIKOU2, getSrSlipSyukkakensaItemData(GXHDO102B037Const.BIKOU2, srSlipSyukkakensa));

    }

    /**
     * ｽﾘｯﾌﾟ作製・出荷検査の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return ｽﾘｯﾌﾟ作製・出荷検査登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrSlipSyukkakensa> getSrSlipSyukkakensaData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrSlipSyukkakensa(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
        } else {
            return loadTmpSrSlipSyukkakensa(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
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
     * [ｽﾘｯﾌﾟ作製・出荷検査]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrSlipSyukkakensa> loadSrSlipSyukkakensa(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, String rev) throws SQLException {

        String sql = " SELECT "
                + " kojyo,lotno,edaban,jissekino,sliphinmei,sliplotno,lotkubun,genryoukigou,dasshisara,rutsubono,rutsubohuutaijyuuryou,"
                + "kansoumaeslipjyuuryou,kannsouki1,kannsouondo1,kansoujikan1,kansoukaishijikan1,kansousyuuryoujikan1,kannsouki2,kannsouondo2,"
                + "kansoujikan2,kansoukaishijikan2,kansousyuuryoujikan2,kansougosoujyuuryou,kansougosyoumijyuuryou,kokeibunhiritsu,"
                + "kokeibunsokuteitantousya,sokuteiki,sokuteigouki,spindlesyurui,kaitensuu,sokuteinichiji,nendosokuteikekka,ondosokuteikekka,"
                + "nendosokuteitantousya,syuuritsu,slipyuukoukigen,bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + " FROM sr_slip_syukkakensa "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? ";

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
        mapping.put("kojyo", "kojyo");                                          // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                          // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                        // 枝番
        mapping.put("jissekino", "jissekino");                                  // 実績No
        mapping.put("sliphinmei", "sliphinmei");                                // ｽﾘｯﾌﾟ品名
        mapping.put("sliplotno", "sliplotno");                                  // ｽﾘｯﾌﾟLotNo
        mapping.put("lotkubun", "lotkubun");                                    // ﾛｯﾄ区分
        mapping.put("genryoukigou", "genryoukigou");                            // 原料記号
        mapping.put("dasshisara", "dasshisara");                                // 脱脂皿の種類
        mapping.put("rutsubono", "rutsubono");                                  // ﾙﾂﾎﾞNo
        mapping.put("rutsubohuutaijyuuryou", "rutsubohuutaijyuuryou");          // ﾙﾂﾎﾞ風袋重量
        mapping.put("kansoumaeslipjyuuryou", "kansoumaeslipjyuuryou");          // 乾燥前ｽﾘｯﾌﾟ重量
        mapping.put("kannsouki1", "kannsouki1");                                // 乾燥機①
        mapping.put("kannsouondo1", "kannsouondo1");                            // 乾燥温度①
        mapping.put("kansoujikan1", "kansoujikan1");                            // 乾燥時間①
        mapping.put("kansoukaishijikan1", "kansoukaishijikan1");                // 乾燥開始日時①
        mapping.put("kansousyuuryoujikan1", "kansousyuuryoujikan1");            // 乾燥終了日時①
        mapping.put("kannsouki2", "kannsouki2");                                // 乾燥機②
        mapping.put("kannsouondo2", "kannsouondo2");                            // 乾燥温度②
        mapping.put("kansoujikan2", "kansoujikan2");                            // 乾燥時間②
        mapping.put("kansoukaishijikan2", "kansoukaishijikan2");                // 乾燥開始日時②
        mapping.put("kansousyuuryoujikan2", "kansousyuuryoujikan2");            // 乾燥終了日時②
        mapping.put("kansougosoujyuuryou", "kansougosoujyuuryou");              // 乾燥後総重量
        mapping.put("kansougosyoumijyuuryou", "kansougosyoumijyuuryou");        // 乾燥後正味重量
        mapping.put("kokeibunhiritsu", "kokeibunhiritsu");                      // 固形分比率
        mapping.put("kokeibunsokuteitantousya", "kokeibunsokuteitantousya");    // 固形分測定担当者
        mapping.put("sokuteiki", "sokuteiki");                                  // 測定器
        mapping.put("sokuteigouki", "sokuteigouki");                            // 測定号機
        mapping.put("spindlesyurui", "spindlesyurui");                          // ｽﾋﾟﾝﾄﾞﾙの種類
        mapping.put("kaitensuu", "kaitensuu");                                  // 回転数
        mapping.put("sokuteinichiji", "sokuteinichiji");                        // 測定日時
        mapping.put("nendosokuteikekka", "nendosokuteikekka");                  // 粘度測定結果
        mapping.put("ondosokuteikekka", "ondosokuteikekka");                    // 温度測定結果
        mapping.put("nendosokuteitantousya", "nendosokuteitantousya");          // 粘度測定担当者
        mapping.put("syuuritsu", "syuuritsu");                                  // 収率(%)
        mapping.put("slipyuukoukigen", "slipyuukoukigen");                      // ｽﾘｯﾌﾟ有効期限
        mapping.put("bikou1", "bikou1");                                        // 備考1
        mapping.put("bikou2", "bikou2");                                        // 備考2
        mapping.put("torokunichiji", "torokunichiji");                          // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                            // 更新日時
        mapping.put("revision", "revision");                                    // revision

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrSlipSyukkakensa>> beanHandler = new BeanListHandler<>(SrSlipSyukkakensa.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [ｽﾘｯﾌﾟ作製・出荷検査_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrSlipSyukkakensa> loadTmpSrSlipSyukkakensa(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, String rev) throws SQLException {

        String sql = " SELECT "
                + " kojyo,lotno,edaban,jissekino,sliphinmei,sliplotno,lotkubun,genryoukigou,dasshisara,rutsubono,rutsubohuutaijyuuryou,"
                + "kansoumaeslipjyuuryou,kannsouki1,kannsouondo1,kansoujikan1,kansoukaishijikan1,kansousyuuryoujikan1,kannsouki2,kannsouondo2,"
                + "kansoujikan2,kansoukaishijikan2,kansousyuuryoujikan2,kansougosoujyuuryou,kansougosyoumijyuuryou,kokeibunhiritsu,"
                + "kokeibunsokuteitantousya,sokuteiki,sokuteigouki,spindlesyurui,kaitensuu,sokuteinichiji,nendosokuteikekka,ondosokuteikekka,"
                + "nendosokuteitantousya,syuuritsu,slipyuukoukigen,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + " FROM tmp_sr_slip_syukkakensa "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? AND deleteflag = ? ";

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
        mapping.put("kojyo", "kojyo");                                          // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                          // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                        // 枝番
        mapping.put("jissekino", "jissekino");                                  // 実績No
        mapping.put("sliphinmei", "sliphinmei");                                // ｽﾘｯﾌﾟ品名
        mapping.put("sliplotno", "sliplotno");                                  // ｽﾘｯﾌﾟLotNo
        mapping.put("lotkubun", "lotkubun");                                    // ﾛｯﾄ区分
        mapping.put("genryoukigou", "genryoukigou");                            // 原料記号
        mapping.put("dasshisara", "dasshisara");                                // 脱脂皿の種類
        mapping.put("rutsubono", "rutsubono");                                  // ﾙﾂﾎﾞNo
        mapping.put("rutsubohuutaijyuuryou", "rutsubohuutaijyuuryou");          // ﾙﾂﾎﾞ風袋重量
        mapping.put("kansoumaeslipjyuuryou", "kansoumaeslipjyuuryou");          // 乾燥前ｽﾘｯﾌﾟ重量
        mapping.put("kannsouki1", "kannsouki1");                                // 乾燥機①
        mapping.put("kannsouondo1", "kannsouondo1");                            // 乾燥温度①
        mapping.put("kansoujikan1", "kansoujikan1");                            // 乾燥時間①
        mapping.put("kansoukaishijikan1", "kansoukaishijikan1");                // 乾燥開始日時①
        mapping.put("kansousyuuryoujikan1", "kansousyuuryoujikan1");            // 乾燥終了日時①
        mapping.put("kannsouki2", "kannsouki2");                                // 乾燥機②
        mapping.put("kannsouondo2", "kannsouondo2");                            // 乾燥温度②
        mapping.put("kansoujikan2", "kansoujikan2");                            // 乾燥時間②
        mapping.put("kansoukaishijikan2", "kansoukaishijikan2");                // 乾燥開始日時②
        mapping.put("kansousyuuryoujikan2", "kansousyuuryoujikan2");            // 乾燥終了日時②
        mapping.put("kansougosoujyuuryou", "kansougosoujyuuryou");              // 乾燥後総重量
        mapping.put("kansougosyoumijyuuryou", "kansougosyoumijyuuryou");        // 乾燥後正味重量
        mapping.put("kokeibunhiritsu", "kokeibunhiritsu");                      // 固形分比率
        mapping.put("kokeibunsokuteitantousya", "kokeibunsokuteitantousya");    // 固形分測定担当者
        mapping.put("sokuteiki", "sokuteiki");                                  // 測定器
        mapping.put("sokuteigouki", "sokuteigouki");                            // 測定号機
        mapping.put("spindlesyurui", "spindlesyurui");                          // ｽﾋﾟﾝﾄﾞﾙの種類
        mapping.put("kaitensuu", "kaitensuu");                                  // 回転数
        mapping.put("sokuteinichiji", "sokuteinichiji");                        // 測定日時
        mapping.put("nendosokuteikekka", "nendosokuteikekka");                  // 粘度測定結果
        mapping.put("ondosokuteikekka", "ondosokuteikekka");                    // 温度測定結果
        mapping.put("nendosokuteitantousya", "nendosokuteitantousya");          // 粘度測定担当者
        mapping.put("syuuritsu", "syuuritsu");                                  // 収率(%)
        mapping.put("slipyuukoukigen", "slipyuukoukigen");                      // ｽﾘｯﾌﾟ有効期限
        mapping.put("bikou1", "bikou1");                                        // 備考1
        mapping.put("bikou2", "bikou2");                                        // 備考2
        mapping.put("torokunichiji", "torokunichiji");                          // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                            // 更新日時
        mapping.put("revision", "revision");                                    // revision
        mapping.put("deleteflag", "deleteflag");                                // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrSlipSyukkakensa>> beanHandler = new BeanListHandler<>(SrSlipSyukkakensa.class, rowProcessor);

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
     * @param srSlipSyukkakensa ｽﾘｯﾌﾟ作製・出荷検査データ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrSlipSyukkakensa srSlipSyukkakensa) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srSlipSyukkakensa != null) {
            // 元データが存在する場合元データより取得
            return getSrSlipSyukkakensaItemData(itemId, srSlipSyukkakensa);
        } else {
            return null;
        }
    }

    /**
     * 項目データ(入力値)取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @param srSlipSyukkakensa ｽﾘｯﾌﾟ作製・出荷検査データ
     * @return 入力値
     */
    private String getItemKikakuchi(List<FXHDD01> listData, String itemId, SrSlipSyukkakensa srSlipSyukkakensa) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return StringUtil.nullToBlank(selectData.get(0).getKikakuChi()).replace("【", "").replace("】", "");
        } else if (srSlipSyukkakensa != null) {
            // 元データが存在する場合元データより取得
            return getSrSlipSyukkakensaItemData(itemId, srSlipSyukkakensa);
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
     * ｽﾘｯﾌﾟ作製・出荷検査_仮登録(tmp_sr_slip_syukkakensa)登録処理
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
    private void insertTmpSrSlipSyukkakensa(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, String systemTime, ProcessData processData, String formId) throws SQLException {

        String sql = "INSERT INTO tmp_sr_slip_syukkakensa ("
                + " kojyo,lotno,edaban,jissekino,sliphinmei,sliplotno,lotkubun,genryoukigou,dasshisara,rutsubono,rutsubohuutaijyuuryou,"
                + "kansoumaeslipjyuuryoukikaku,kansoumaeslipjyuuryou,kannsouki1,kannsouondo1,kansoujikan1,kansoukaishijikan1,kansousyuuryoujikan1,kannsouki2,kannsouondo2,"
                + "kansoujikan2,kansoukaishijikan2,kansousyuuryoujikan2,kansougosoujyuuryou,kansougosyoumijyuuryou,kokeibunkikaku,kokeibunhiritsu,"
                + "kokeibunsokuteitantousya,sokuteiki,sokuteigouki,spindlesyurui,kaitensuu,sokuteinichiji,nendosokuteikikaku,nendosokuteikekka,ondosokuteikikaku,ondosokuteikekka,"
                + "nendosokuteitantousya,syuuritsu,slipyuukoukigen,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + " ) VALUES ( "
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrSlipSyukkakensa(true, newRev, deleteflag, kojyo, lotNo, edaban, jissekino, systemTime, processData, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・出荷検査_仮登録(tmp_sr_slip_syukkakensa)更新処理
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
    private void updateTmpSrSlipSyukkakensa(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, String systemTime, ProcessData processData) throws SQLException {

        String sql = "UPDATE tmp_sr_slip_syukkakensa SET "
                + " sliphinmei = ?,sliplotno = ?,lotkubun = ?,genryoukigou = ?,dasshisara = ?,rutsubono = ?,rutsubohuutaijyuuryou = ?,kansoumaeslipjyuuryoukikaku = ?,kansoumaeslipjyuuryou = ?,"
                + "kannsouki1 = ?,kannsouondo1 = ?,kansoujikan1 = ?,kansoukaishijikan1 = ?,kansousyuuryoujikan1 = ?,kannsouki2 = ?,kannsouondo2 = ?,kansoujikan2 = ?,"
                + "kansoukaishijikan2 = ?,kansousyuuryoujikan2 = ?,kansougosoujyuuryou = ?,kansougosyoumijyuuryou = ?,kokeibunkikaku = ?,kokeibunhiritsu = ?,kokeibunsokuteitantousya = ?,"
                + "sokuteiki = ?,sokuteigouki = ?,spindlesyurui = ?,kaitensuu = ?,sokuteinichiji = ?,nendosokuteikikaku = ?,nendosokuteikekka = ?,ondosokuteikikaku = ?,ondosokuteikekka = ?,nendosokuteitantousya = ?,"
                + "syuuritsu = ?,slipyuukoukigen = ?,bikou1 = ?,bikou2 = ?,kosinnichiji = ?,revision = ?,deleteflag = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrSlipSyukkakensa> srSlipSyukkakensaList = getSrSlipSyukkakensaData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekino);
        SrSlipSyukkakensa srSlipSyukkakensa = null;
        if (!srSlipSyukkakensaList.isEmpty()) {
            srSlipSyukkakensa = srSlipSyukkakensaList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrSlipSyukkakensa(false, newRev, 0, "", "", "", jissekino, systemTime, processData, srSlipSyukkakensa);

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
     * ｽﾘｯﾌﾟ作製・出荷検査_仮登録(tmp_sr_slip_syukkakensa)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrSlipSyukkakensa(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        String sql = "DELETE FROM tmp_sr_slip_syukkakensa "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? AND revision = ?";

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
     * ｽﾘｯﾌﾟ作製・出荷検査_仮登録(tmp_sr_slip_syukkakensa)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srSlipSyukkakensa ｽﾘｯﾌﾟ作製・出荷検査データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrSlipSyukkakensa(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, int jissekino, String systemTime, ProcessData processData, SrSlipSyukkakensa srSlipSyukkakensa) {

        List<FXHDD01> pItemList = processData.getItemList();
        List<Object> params = new ArrayList<>();
        // 乾燥開始日時①
        String kansoukaishi1Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B037Const.KANSOUKAISHI1_TIME, srSlipSyukkakensa));
        // 乾燥終了日時①
        String kansousyuuryou1Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B037Const.KANSOUSYUURYOU1_TIME, srSlipSyukkakensa));
        // 乾燥開始日時②
        String kansoukaishi2Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B037Const.KANSOUKAISHI2_TIME, srSlipSyukkakensa));
        // 乾燥終了日時②
        String kansousyuuryou2Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B037Const.KANSOUSYUURYOU2_TIME, srSlipSyukkakensa));
        // 測定日時
        String sokuteiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B037Const.SOKUTEI_TIME, srSlipSyukkakensa));

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
            params.add(jissekino); //実績No
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B037Const.SLIPHINMEI, srSlipSyukkakensa)));                       // ｽﾘｯﾌﾟ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B037Const.SLIPLOTNO, srSlipSyukkakensa)));                        // ｽﾘｯﾌﾟLotNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B037Const.LOTKUBUN, srSlipSyukkakensa)));                         // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B037Const.GENRYOUKIGOU, srSlipSyukkakensa)));                // 原料記号
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B037Const.DASSHISARA, srSlipSyukkakensa)));                  // 脱脂皿の種類
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B037Const.RUTSUBONO, srSlipSyukkakensa)));                        // ﾙﾂﾎﾞNo
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B037Const.RUTSUBOHUUTAIJYUURYOU, srSlipSyukkakensa)));        // ﾙﾂﾎﾞ風袋重量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B037Const.KANSOUMAESLIPJYUURYOU, srSlipSyukkakensa)));       // 乾燥前ｽﾘｯﾌﾟ重量規格
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B037Const.KANSOUMAESLIPJYUURYOU, srSlipSyukkakensa)));        // 乾燥前ｽﾘｯﾌﾟ重量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B037Const.KANNSOUKI1, srSlipSyukkakensa)));                  // 乾燥機①
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B037Const.KANNSOUONDO1, srSlipSyukkakensa)));                // 乾燥温度①
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B037Const.KANSOUJIKAN1, srSlipSyukkakensa)));                // 乾燥時間①
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B037Const.KANSOUKAISHI1_DAY, srSlipSyukkakensa),
                "".equals(kansoukaishi1Time) ? "0000" : kansoukaishi1Time));                                                                               // 乾燥開始日時①
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B037Const.KANSOUSYUURYOU1_DAY, srSlipSyukkakensa),
                "".equals(kansousyuuryou1Time) ? "0000" : kansousyuuryou1Time));                                                                           // 乾燥終了日時①
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B037Const.KANNSOUKI2, srSlipSyukkakensa)));                  // 乾燥機②
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B037Const.KANNSOUONDO2, srSlipSyukkakensa)));                // 乾燥温度②
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B037Const.KANSOUJIKAN2, srSlipSyukkakensa)));                // 乾燥時間②
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B037Const.KANSOUKAISHI2_DAY, srSlipSyukkakensa),
                "".equals(kansoukaishi2Time) ? "0000" : kansoukaishi2Time));                                                                               // 乾燥開始日時②
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B037Const.KANSOUSYUURYOU2_DAY, srSlipSyukkakensa),
                "".equals(kansousyuuryou2Time) ? "0000" : kansousyuuryou2Time));                                                                           // 乾燥終了日時②
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B037Const.KANSOUGOSOUJYUURYOU, srSlipSyukkakensa)));          // 乾燥後総重量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B037Const.KANSOUGOSYOUMIJYUURYOU, srSlipSyukkakensa)));       // 乾燥後正味重量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B037Const.KOKEIBUNHIRITSU, srSlipSyukkakensa)));             // 固形分規格
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B037Const.KOKEIBUNHIRITSU, srSlipSyukkakensa)));              // 固形分比率
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B037Const.KOKEIBUNSOKUTEITANTOUSYA, srSlipSyukkakensa)));         // 固形分測定担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B037Const.SOKUTEIKI, srSlipSyukkakensa)));                   // 測定機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B037Const.SOKUTEIGOUKI, srSlipSyukkakensa)));                     // 測定号機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B037Const.SPINDLESYURUI, srSlipSyukkakensa)));               // ｽﾋﾟﾝﾄﾞﾙの種類
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B037Const.KAITENSUU, srSlipSyukkakensa)));                   // 回転数
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B037Const.SOKUTEI_DAY, srSlipSyukkakensa),
                "".equals(sokuteiTime) ? "0000" : sokuteiTime));                                                                                           // 測定日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B037Const.NENDOSOKUTEIKEKKA, srSlipSyukkakensa)));           // 粘度測定規格
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B037Const.NENDOSOKUTEIKEKKA, srSlipSyukkakensa)));            // 粘度測定結果
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B037Const.ONDOSOKUTEIKEKKA, srSlipSyukkakensa)));            // 温度測定規格
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B037Const.ONDOSOKUTEIKEKKA, srSlipSyukkakensa)));             // 温度測定結果
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B037Const.NENDOSOKUTEITANTOUSYA, srSlipSyukkakensa)));            // 粘度測定担当者
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B037Const.SYUURITSU, srSlipSyukkakensa)));                    // 収率(%)
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B037Const.SLIPYUUKOUKIGEN, srSlipSyukkakensa), "0000"));            // ｽﾘｯﾌﾟ有効期限
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B037Const.BIKOU1, srSlipSyukkakensa)));                           // 備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B037Const.BIKOU2, srSlipSyukkakensa)));                           // 備考2

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
     * ｽﾘｯﾌﾟ作製・出荷検査(sr_slip_syukkakensa)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param tmpSrSlipSyukkakensa 仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrSlipSyukkakensa(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekiNo, String systemTime, ProcessData processData, SrSlipSyukkakensa tmpSrSlipSyukkakensa, String formId) throws SQLException {

        String sql = "INSERT INTO sr_slip_syukkakensa ("
                + " kojyo,lotno,edaban,jissekino,sliphinmei,sliplotno,lotkubun,genryoukigou,dasshisara,rutsubono,rutsubohuutaijyuuryou,"
                + "kansoumaeslipjyuuryoukikaku,kansoumaeslipjyuuryou,kannsouki1,kannsouondo1,kansoujikan1,kansoukaishijikan1,kansousyuuryoujikan1,kannsouki2,kannsouondo2,"
                + "kansoujikan2,kansoukaishijikan2,kansousyuuryoujikan2,kansougosoujyuuryou,kansougosyoumijyuuryou,kokeibunkikaku,kokeibunhiritsu,"
                + "kokeibunsokuteitantousya,sokuteiki,sokuteigouki,spindlesyurui,kaitensuu,sokuteinichiji,nendosokuteikikaku,nendosokuteikekka,ondosokuteikikaku,ondosokuteikekka,"
                + "nendosokuteitantousya,syuuritsu,slipyuukoukigen,bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + " ) VALUES ( "
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrSlipSyukkakensa(true, newRev, kojyo, lotNo, edaban, jissekiNo, systemTime, processData, tmpSrSlipSyukkakensa);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・出荷検査(sr_slip_syukkakensa)更新処理
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
    private void updateSrSlipSyukkakensa(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekiNo, String systemTime, ProcessData processData) throws SQLException {

        String sql = "UPDATE sr_slip_syukkakensa SET "
                + " sliphinmei = ?,sliplotno = ?,lotkubun = ?,genryoukigou = ?,dasshisara = ?,rutsubono = ?,rutsubohuutaijyuuryou = ?,kansoumaeslipjyuuryoukikaku = ?,kansoumaeslipjyuuryou = ?,"
                + "kannsouki1 = ?,kannsouondo1 = ?,kansoujikan1 = ?,kansoukaishijikan1 = ?,kansousyuuryoujikan1 = ?,kannsouki2 = ?,kannsouondo2 = ?,kansoujikan2 = ?,"
                + "kansoukaishijikan2 = ?,kansousyuuryoujikan2 = ?,kansougosoujyuuryou = ?,kansougosyoumijyuuryou = ?,kokeibunkikaku = ?,kokeibunhiritsu = ?,kokeibunsokuteitantousya = ?,"
                + "sokuteiki = ?,sokuteigouki = ?,spindlesyurui = ?,kaitensuu = ?,sokuteinichiji = ?,nendosokuteikikaku = ?,nendosokuteikekka = ?,ondosokuteikikaku = ?,ondosokuteikekka = ?,nendosokuteitantousya = ?,"
                + "syuuritsu = ?,slipyuukoukigen = ?,bikou1 = ?,bikou2 = ?,kosinnichiji = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrSlipSyukkakensa> srSlipSyukkakensaList = getSrSlipSyukkakensaData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekiNo);
        SrSlipSyukkakensa srSlipSyukkakensa = null;
        if (!srSlipSyukkakensaList.isEmpty()) {
            srSlipSyukkakensa = srSlipSyukkakensaList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrSlipSyukkakensa(false, newRev, "", "", "", jissekiNo, systemTime, processData, srSlipSyukkakensa);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekiNo);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・出荷検査(sr_slip_syukkakensa)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srSlipSyukkakensa ｽﾘｯﾌﾟ作製・出荷検査データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrSlipSyukkakensa(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban, int jissekino,
            String systemTime, ProcessData processData, SrSlipSyukkakensa srSlipSyukkakensa) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // 乾燥開始日時①
        String kansoukaishi1Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B037Const.KANSOUKAISHI1_TIME, srSlipSyukkakensa));
        // 乾燥終了日時①
        String kansousyuuryou1Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B037Const.KANSOUSYUURYOU1_TIME, srSlipSyukkakensa));
        // 乾燥開始日時②
        String kansoukaishi2Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B037Const.KANSOUKAISHI2_TIME, srSlipSyukkakensa));
        // 乾燥終了日時②
        String kansousyuuryou2Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B037Const.KANSOUSYUURYOU2_TIME, srSlipSyukkakensa));
        // 測定日時
        String sokuteiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B037Const.SOKUTEI_TIME, srSlipSyukkakensa));
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
            params.add(jissekino); //実績No
        }

        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B037Const.SLIPHINMEI, srSlipSyukkakensa)));                       // ｽﾘｯﾌﾟ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B037Const.SLIPLOTNO, srSlipSyukkakensa)));                        // ｽﾘｯﾌﾟLotNo
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B037Const.LOTKUBUN, srSlipSyukkakensa)));                         // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B037Const.GENRYOUKIGOU, srSlipSyukkakensa)));                // 原料記号
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B037Const.DASSHISARA, srSlipSyukkakensa)));                  // 脱脂皿の種類
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B037Const.RUTSUBONO, srSlipSyukkakensa)));                        // ﾙﾂﾎﾞNo
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B037Const.RUTSUBOHUUTAIJYUURYOU, srSlipSyukkakensa)));        // ﾙﾂﾎﾞ風袋重量
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B037Const.KANSOUMAESLIPJYUURYOU, srSlipSyukkakensa)));       // 乾燥前ｽﾘｯﾌﾟ重量規格
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B037Const.KANSOUMAESLIPJYUURYOU, srSlipSyukkakensa)));        // 乾燥前ｽﾘｯﾌﾟ重量
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B037Const.KANNSOUKI1, srSlipSyukkakensa)));                  // 乾燥機①
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B037Const.KANNSOUONDO1, srSlipSyukkakensa)));                // 乾燥温度①
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B037Const.KANSOUJIKAN1, srSlipSyukkakensa)));                // 乾燥時間①
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B037Const.KANSOUKAISHI1_DAY, srSlipSyukkakensa),
                "".equals(kansoukaishi1Time) ? "0000" : kansoukaishi1Time));                                                                    // 乾燥開始日時①
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B037Const.KANSOUSYUURYOU1_DAY, srSlipSyukkakensa),
                "".equals(kansousyuuryou1Time) ? "0000" : kansousyuuryou1Time));                                                                // 乾燥終了日時①
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B037Const.KANNSOUKI2, srSlipSyukkakensa)));                  // 乾燥機②
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B037Const.KANNSOUONDO2, srSlipSyukkakensa)));                // 乾燥温度②
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B037Const.KANSOUJIKAN2, srSlipSyukkakensa)));                // 乾燥時間②
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B037Const.KANSOUKAISHI2_DAY, srSlipSyukkakensa),
                "".equals(kansoukaishi2Time) ? "0000" : kansoukaishi2Time));                                                                    // 乾燥開始日時②
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B037Const.KANSOUSYUURYOU2_DAY, srSlipSyukkakensa),
                "".equals(kansousyuuryou2Time) ? "0000" : kansousyuuryou2Time));                                                                // 乾燥終了日時②
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B037Const.KANSOUGOSOUJYUURYOU, srSlipSyukkakensa)));          // 乾燥後総重量
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B037Const.KANSOUGOSYOUMIJYUURYOU, srSlipSyukkakensa)));       // 乾燥後正味重量
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B037Const.KOKEIBUNHIRITSU, srSlipSyukkakensa)));             // 固形分規格
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B037Const.KOKEIBUNHIRITSU, srSlipSyukkakensa)));              // 固形分比率
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B037Const.KOKEIBUNSOKUTEITANTOUSYA, srSlipSyukkakensa)));         // 固形分測定担当者
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B037Const.SOKUTEIKI, srSlipSyukkakensa)));                   // 測定機
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B037Const.SOKUTEIGOUKI, srSlipSyukkakensa)));                     // 測定号機
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B037Const.SPINDLESYURUI, srSlipSyukkakensa)));               // ｽﾋﾟﾝﾄﾞﾙの種類
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B037Const.KAITENSUU, srSlipSyukkakensa)));                   // 回転数
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B037Const.SOKUTEI_DAY, srSlipSyukkakensa),
                "".equals(sokuteiTime) ? "0000" : sokuteiTime));                                                                                // 測定日時
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B037Const.NENDOSOKUTEIKEKKA, srSlipSyukkakensa)));           // 粘度測定規格
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B037Const.NENDOSOKUTEIKEKKA, srSlipSyukkakensa)));            // 粘度測定結果
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B037Const.ONDOSOKUTEIKEKKA, srSlipSyukkakensa)));            // 温度測定規格
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B037Const.ONDOSOKUTEIKEKKA, srSlipSyukkakensa)));             // 温度測定結果
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B037Const.NENDOSOKUTEITANTOUSYA, srSlipSyukkakensa)));            // 粘度測定担当者
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B037Const.SYUURITSU, srSlipSyukkakensa)));                    // 収率(%)
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B037Const.SLIPYUUKOUKIGEN, srSlipSyukkakensa), "0000"));            // ｽﾘｯﾌﾟ有効期限
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B037Const.BIKOU1, srSlipSyukkakensa)));                           // 備考1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B037Const.BIKOU2, srSlipSyukkakensa)));                           // 備考2

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
     * ｽﾘｯﾌﾟ作製・出荷検査(sr_slip_syukkakensa)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSrSlipSyukkakensa(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        String sql = "DELETE FROM sr_slip_syukkakensa "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? AND revision = ?";

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
     * [ｽﾘｯﾌﾟ作製・出荷検査_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @return 削除ﾌﾗｸﾞ最大値 + 1
     * @throws SQLException 例外エラー
     */
    private int getNewDeleteflag(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {
        String sql = "SELECT MAX(deleteflag) AS deleteflag "
                + "FROM tmp_sr_slip_syukkakensa "
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
     * @param srSlipSyukkakensa ｽﾘｯﾌﾟ作製・出荷検査データ
     * @return DB値
     */
    private String getSrSlipSyukkakensaItemData(String itemId, SrSlipSyukkakensa srSlipSyukkakensa) {
        switch (itemId) {
            // ｽﾘｯﾌﾟ品名
            case GXHDO102B037Const.SLIPHINMEI:
                return StringUtil.nullToBlank(srSlipSyukkakensa.getSliphinmei());

            // ｽﾘｯﾌﾟLotNo
            case GXHDO102B037Const.SLIPLOTNO:
                return StringUtil.nullToBlank(srSlipSyukkakensa.getSliplotno());

            // ﾛｯﾄ区分
            case GXHDO102B037Const.LOTKUBUN:
                return StringUtil.nullToBlank(srSlipSyukkakensa.getLotkubun());

            // 原料記号
            case GXHDO102B037Const.GENRYOUKIGOU:
                return StringUtil.nullToBlank(srSlipSyukkakensa.getGenryoukigou());

            // 脱脂皿の種類
            case GXHDO102B037Const.DASSHISARA:
                return StringUtil.nullToBlank(srSlipSyukkakensa.getDasshisara());

            // ﾙﾂﾎﾞNo
            case GXHDO102B037Const.RUTSUBONO:
                return StringUtil.nullToBlank(srSlipSyukkakensa.getRutsubono());

            // ﾙﾂﾎﾞ風袋重量
            case GXHDO102B037Const.RUTSUBOHUUTAIJYUURYOU:
                return StringUtil.nullToBlank(srSlipSyukkakensa.getRutsubohuutaijyuuryou());

            // 乾燥前ｽﾘｯﾌﾟ重量
            case GXHDO102B037Const.KANSOUMAESLIPJYUURYOU:
                return StringUtil.nullToBlank(srSlipSyukkakensa.getKansoumaeslipjyuuryou());

            // 乾燥機①
            case GXHDO102B037Const.KANNSOUKI1:
                return StringUtil.nullToBlank(srSlipSyukkakensa.getKannsouki1());

            // 乾燥温度①
            case GXHDO102B037Const.KANNSOUONDO1:
                return StringUtil.nullToBlank(srSlipSyukkakensa.getKannsouondo1());

            // 乾燥時間①
            case GXHDO102B037Const.KANSOUJIKAN1:
                return StringUtil.nullToBlank(srSlipSyukkakensa.getKansoujikan1());

            // 乾燥開始日①
            case GXHDO102B037Const.KANSOUKAISHI1_DAY:
                return DateUtil.formattedTimestamp(srSlipSyukkakensa.getKansoukaishijikan1(), "yyMMdd");

            // 乾燥開始時間①
            case GXHDO102B037Const.KANSOUKAISHI1_TIME:
                return DateUtil.formattedTimestamp(srSlipSyukkakensa.getKansoukaishijikan1(), "HHmm");

            // 乾燥終了日①
            case GXHDO102B037Const.KANSOUSYUURYOU1_DAY:
                return DateUtil.formattedTimestamp(srSlipSyukkakensa.getKansousyuuryoujikan1(), "yyMMdd");

            // 乾燥終了時間①
            case GXHDO102B037Const.KANSOUSYUURYOU1_TIME:
                return DateUtil.formattedTimestamp(srSlipSyukkakensa.getKansousyuuryoujikan1(), "HHmm");

            // 乾燥機②
            case GXHDO102B037Const.KANNSOUKI2:
                return StringUtil.nullToBlank(srSlipSyukkakensa.getKannsouki2());

            // 乾燥温度②
            case GXHDO102B037Const.KANNSOUONDO2:
                return StringUtil.nullToBlank(srSlipSyukkakensa.getKannsouondo2());

            // 乾燥時間②
            case GXHDO102B037Const.KANSOUJIKAN2:
                return StringUtil.nullToBlank(srSlipSyukkakensa.getKansoujikan2());

            // 乾燥開始日②
            case GXHDO102B037Const.KANSOUKAISHI2_DAY:
                return DateUtil.formattedTimestamp(srSlipSyukkakensa.getKansoukaishijikan2(), "yyMMdd");

            // 乾燥開始時間②
            case GXHDO102B037Const.KANSOUKAISHI2_TIME:
                return DateUtil.formattedTimestamp(srSlipSyukkakensa.getKansoukaishijikan2(), "HHmm");

            // 乾燥終了日②
            case GXHDO102B037Const.KANSOUSYUURYOU2_DAY:
                return DateUtil.formattedTimestamp(srSlipSyukkakensa.getKansousyuuryoujikan2(), "yyMMdd");

            // 乾燥終了時間②
            case GXHDO102B037Const.KANSOUSYUURYOU2_TIME:
                return DateUtil.formattedTimestamp(srSlipSyukkakensa.getKansousyuuryoujikan2(), "HHmm");

            // 乾燥後総重量
            case GXHDO102B037Const.KANSOUGOSOUJYUURYOU:
                return StringUtil.nullToBlank(srSlipSyukkakensa.getKansougosoujyuuryou());

            // 乾燥後正味重量
            case GXHDO102B037Const.KANSOUGOSYOUMIJYUURYOU:
                return StringUtil.nullToBlank(srSlipSyukkakensa.getKansougosyoumijyuuryou());

            // 固形分比率
            case GXHDO102B037Const.KOKEIBUNHIRITSU:
                return StringUtil.nullToBlank(srSlipSyukkakensa.getKokeibunhiritsu());

            // 固形分測定担当者
            case GXHDO102B037Const.KOKEIBUNSOKUTEITANTOUSYA:
                return StringUtil.nullToBlank(srSlipSyukkakensa.getKokeibunsokuteitantousya());

            // 測定器
            case GXHDO102B037Const.SOKUTEIKI:
                return StringUtil.nullToBlank(srSlipSyukkakensa.getSokuteiki());

            // 測定号機
            case GXHDO102B037Const.SOKUTEIGOUKI:
                return StringUtil.nullToBlank(srSlipSyukkakensa.getSokuteigouki());

            // ｽﾋﾟﾝﾄﾞﾙの種類
            case GXHDO102B037Const.SPINDLESYURUI:
                return StringUtil.nullToBlank(srSlipSyukkakensa.getSpindlesyurui());

            // 回転数
            case GXHDO102B037Const.KAITENSUU:
                return StringUtil.nullToBlank(srSlipSyukkakensa.getKaitensuu());

            // 測定日
            case GXHDO102B037Const.SOKUTEI_DAY:
                return DateUtil.formattedTimestamp(srSlipSyukkakensa.getSokuteinichiji(), "yyMMdd");

            // 測定時間
            case GXHDO102B037Const.SOKUTEI_TIME:
                return DateUtil.formattedTimestamp(srSlipSyukkakensa.getSokuteinichiji(), "HHmm");

            // 粘度測定結果
            case GXHDO102B037Const.NENDOSOKUTEIKEKKA:
                return StringUtil.nullToBlank(srSlipSyukkakensa.getNendosokuteikekka());

            // 温度測定結果
            case GXHDO102B037Const.ONDOSOKUTEIKEKKA:
                return StringUtil.nullToBlank(srSlipSyukkakensa.getOndosokuteikekka());

            // 粘度測定担当者
            case GXHDO102B037Const.NENDOSOKUTEITANTOUSYA:
                return StringUtil.nullToBlank(srSlipSyukkakensa.getNendosokuteitantousya());

            // 収率(%)
            case GXHDO102B037Const.SYUURITSU:
                return StringUtil.nullToBlank(srSlipSyukkakensa.getSyuuritsu());

            // ｽﾘｯﾌﾟ有効期限
            case GXHDO102B037Const.SLIPYUUKOUKIGEN:
                return DateUtil.formattedTimestamp(srSlipSyukkakensa.getSlipyuukoukigen(), "yyMMdd");

            // 備考1
            case GXHDO102B037Const.BIKOU1:
                return StringUtil.nullToBlank(srSlipSyukkakensa.getBikou1());

            // 備考2
            case GXHDO102B037Const.BIKOU2:
                return StringUtil.nullToBlank(srSlipSyukkakensa.getBikou2());

            default:
                return null;
        }
    }

    /**
     * ｽﾘｯﾌﾟ作製・出荷検査_仮登録(tmp_sr_slip_syukkakensa)登録処理(削除時)
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
    private void insertDeleteDataTmpSrSlipSyukkakensa(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, String systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_slip_syukkakensa ( "
                + " kojyo,lotno,edaban,jissekino,sliphinmei,sliplotno,lotkubun,genryoukigou,dasshisara,rutsubono,rutsubohuutaijyuuryou,"
                + "kansoumaeslipjyuuryoukikaku,kansoumaeslipjyuuryou,kannsouki1,kannsouondo1,kansoujikan1,kansoukaishijikan1,kansousyuuryoujikan1,kannsouki2,kannsouondo2,"
                + "kansoujikan2,kansoukaishijikan2,kansousyuuryoujikan2,kansougosoujyuuryou,kansougosyoumijyuuryou,kokeibunkikaku,kokeibunhiritsu,"
                + "kokeibunsokuteitantousya,sokuteiki,sokuteigouki,spindlesyurui,kaitensuu,sokuteinichiji,nendosokuteikikaku,nendosokuteikekka,ondosokuteikikaku,ondosokuteikekka,"
                + "nendosokuteitantousya,syuuritsu,slipyuukoukigen,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag"
                + ") SELECT "
                + " kojyo,lotno,edaban,jissekino,sliphinmei,sliplotno,lotkubun,genryoukigou,dasshisara,rutsubono,rutsubohuutaijyuuryou,"
                + "kansoumaeslipjyuuryoukikaku,kansoumaeslipjyuuryou,kannsouki1,kannsouondo1,kansoujikan1,kansoukaishijikan1,kansousyuuryoujikan1,kannsouki2,kannsouondo2,"
                + "kansoujikan2,kansoukaishijikan2,kansousyuuryoujikan2,kansougosoujyuuryou,kansougosyoumijyuuryou,kokeibunkikaku,kokeibunhiritsu,"
                + "kokeibunsokuteitantousya,sokuteiki,sokuteigouki,spindlesyurui,kaitensuu,sokuteinichiji,nendosokuteikikaku,nendosokuteikekka,ondosokuteikikaku,ondosokuteikekka,"
                + "nendosokuteitantousya,syuuritsu,slipyuukoukigen,bikou1,bikou2,?,?,?,? "
                + " FROM sr_slip_syukkakensa "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? ";

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

}
