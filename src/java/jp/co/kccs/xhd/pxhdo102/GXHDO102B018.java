/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo102;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
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
import jp.co.kccs.xhd.db.model.SrBinderBfp;
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
 * 変更日	2021/10/29<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B018(ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ)
 *
 * @author KCSS K.Jo
 * @since 2021/10/29
 */
public class GXHDO102B018 implements IFormLogic {

    private static final Logger LOGGER = Logger.getLogger(GXHDO102B018.class.getName());
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
    public GXHDO102B018() {
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
                    GXHDO102B018Const.BTN_EDABAN_COPY_TOP,
                    GXHDO102B018Const.BTN_ASSOUKAISINICHIJI_TOP,
                    GXHDO102B018Const.BTN_ASSOUSYUURYOUNICHIJI_TOP,
                    GXHDO102B018Const.BTN_SOKUTEINICHIJI_TOP,
                    GXHDO102B018Const.BTN_EDABAN_COPY_BOTTOM,
                    GXHDO102B018Const.BTN_ASSOUKAISINICHIJI_BOTTOM,
                    GXHDO102B018Const.BTN_ASSOUSYUURYOUNICHIJI_BOTTOM,
                    GXHDO102B018Const.BTN_SOKUTEINICHIJI_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO102B018Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO102B018Const.BTN_INSERT_TOP,
                    GXHDO102B018Const.BTN_DELETE_TOP,
                    GXHDO102B018Const.BTN_UPDATE_TOP,
                    GXHDO102B018Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO102B018Const.BTN_INSERT_BOTTOM,
                    GXHDO102B018Const.BTN_DELETE_BOTTOM,
                    GXHDO102B018Const.BTN_UPDATE_BOTTOM
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
            case GXHDO102B018Const.BTN_EDABAN_COPY_TOP:
            case GXHDO102B018Const.BTN_EDABAN_COPY_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 仮登録
            case GXHDO102B018Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO102B018Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO102B018Const.BTN_INSERT_TOP:
            case GXHDO102B018Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 修正
            case GXHDO102B018Const.BTN_UPDATE_TOP:
            case GXHDO102B018Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO102B018Const.BTN_DELETE_TOP:
            case GXHDO102B018Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 圧送開始日時
            case GXHDO102B018Const.BTN_ASSOUKAISINICHIJI_TOP:
            case GXHDO102B018Const.BTN_ASSOUKAISINICHIJI_BOTTOM:
                method = "setAssoukaisinichiji";
                break;
            // 圧送終了日時
            case GXHDO102B018Const.BTN_ASSOUSYUURYOUNICHIJI_TOP:
            case GXHDO102B018Const.BTN_ASSOUSYUURYOUNICHIJI_BOTTOM:
                method = "setAssousyuuryounichiji";
                break;
            // 正味重量計算
            case GXHDO102B018Const.BTN_SYOUMIJYUURYOU_KEISAN_TOP:
            case GXHDO102B018Const.BTN_SYOUMIJYUURYOU_KEISAN_BOTTOM:
                method = "setSyoumijyuuryouKeisan";
                break;
            // ﾊﾞｲﾝﾀﾞｰ有効期限計算
            case GXHDO102B018Const.BTN_BINDERYUUKOUKIGEN_KEISAN_TOP:
            case GXHDO102B018Const.BTN_BINDERYUUKOUKIGEN_KEISAN_BOTTOM:
                method = "setBinderyuukoukigenKeisan";
                break;
            // 測定日時
            case GXHDO102B018Const.BTN_SOKUTEINICHIJI_TOP:
            case GXHDO102B018Const.BTN_SOKUTEINICHIJI_BOTTOM:
                method = "setSokuteinichiji";
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

            // ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽの入力項目の登録データ(仮登録時は仮登録データ)を取得
            List<SrBinderBfp> SrBinderBfpDataList = getSrBinderBfpData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo9, oyalotEdaban);
            if (SrBinderBfpDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // メイン画面データ設定
            setInputItemDataMainForm(processData, SrBinderBfpDataList.get(0));

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
     * 圧送開始日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setAssoukaisinichiji(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B018Const.ASSOUKAISI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B018Const.ASSOUKAISI_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 圧送終了日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setAssousyuuryounichiji(ProcessData processData) {

        FXHDD01 itemAssoukaisiDay = getItemRow(processData.getItemList(), GXHDO102B018Const.ASSOUKAISI_DAY);// 圧送開始日
        FXHDD01 itemAssoukaisiTime = getItemRow(processData.getItemList(), GXHDO102B018Const.ASSOUKAISI_TIME);// 圧送開始時間
        FXHDD01 itemAssousyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B018Const.ASSOUSYUURYOU_DAY);// 圧送終了日
        FXHDD01 itemAssousyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B018Const.ASSOUSYUURYOU_TIME);// 圧送終了時間
        if (itemAssoukaisiDay == null || itemAssoukaisiTime == null || itemAssousyuuryouDay == null || itemAssousyuuryouTime == null) {
            processData.setMethod("");
            return processData;
        }
        int diffHours;
        Date kaishijikan = null;
        Date syuuryoujikan = null;
        FXHDD01 assoujikan = getItemRow(processData.getItemList(), GXHDO102B018Const.ASSOUJIKAN);
        if (StringUtil.isEmpty(itemAssousyuuryouDay.getValue()) && StringUtil.isEmpty(itemAssousyuuryouTime.getValue())) {
            setDateTimeItem(itemAssousyuuryouDay, itemAssousyuuryouTime, new Date());
        }
        if (DateUtil.isValidYYMMDD(itemAssoukaisiDay.getValue()) && DateUtil.isValidHHMM(itemAssoukaisiTime.getValue())) {
            kaishijikan = DateUtil.convertStringToDate(itemAssoukaisiDay.getValue(), itemAssoukaisiTime.getValue());
        }
        if (DateUtil.isValidYYMMDD(itemAssousyuuryouDay.getValue()) && DateUtil.isValidHHMM(itemAssousyuuryouTime.getValue())) {
            syuuryoujikan = DateUtil.convertStringToDate(itemAssousyuuryouDay.getValue(), itemAssousyuuryouTime.getValue());
        }
        // 日付の差分分数の数取得処理
        diffHours = DateUtil.diffMinutes(kaishijikan, syuuryoujikan);
        // 計算結果の設定
        assoujikan.setValue(BigDecimal.valueOf(diffHours).toPlainString());
        processData.setMethod("");
        return processData;
    }

    /**
     * 正味重量計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setSyoumijyuuryouKeisan(ProcessData processData) {
        // 総重量
        FXHDD01 soujyuuryousokutei = getItemRow(processData.getItemList(), GXHDO102B018Const.SOUJYUURYOUSOKUTEI);
        // 風袋重量
        FXHDD01 fuutaijyuuryou = getItemRow(processData.getItemList(), GXHDO102B018Const.FUUTAIJYUURYOU);
        // 正味重量
        FXHDD01 syoumijyuuryou = getItemRow(processData.getItemList(), GXHDO102B018Const.SYOUMIJYUURYOU);
        if (soujyuuryousokutei == null || fuutaijyuuryou == null || syoumijyuuryou == null) {
            processData.setMethod("");
            return processData;
        }
        ErrorMessageInfo checkItemErrorInfo = checkSyoumijyuuryouKeisan(soujyuuryousokutei, fuutaijyuuryou);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        syoumijyuuryouKeisan(processData, soujyuuryousokutei, fuutaijyuuryou);
        return processData;
    }

    /**
     * 正味重量計算
     *
     * @param processData 処理制御データ
     * @param soujyuuryousokutei 総重量
     * @param fuutaijyuuryou 風袋重量
     */
    private void syoumijyuuryouKeisan(ProcessData processData, FXHDD01 soujyuuryousokutei, FXHDD01 fuutaijyuuryou) {
        try {
            // 正味重量
            FXHDD01 syoumijyuuryou = getItemRow(processData.getItemList(), GXHDO102B018Const.SYOUMIJYUURYOU);
            //「正味重量」= 「総重量」 - 「風袋重量」 を算出する。
            BigDecimal itemSoujyuuryousokuteiVal = new BigDecimal(soujyuuryousokutei.getValue());
            BigDecimal itemFuutaijyuuryouVal = new BigDecimal(fuutaijyuuryou.getValue());
            BigDecimal itemSyoumijyuuryouVal = itemSoujyuuryousokuteiVal.subtract(itemFuutaijyuuryouVal);
            // 計算結果の設定
            syoumijyuuryou.setValue(itemSyoumijyuuryouVal.toPlainString());
        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
            ErrUtil.outputErrorLog("正味重量計算にエラー発生", ex, LOGGER);
        }
    }

    /**
     * 正味重量計算ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
     *
     * @param soujyuuryousokutei 総重量
     * @param fuutaijyuuryou 風袋重量
     * @return エラーメッセージ情報
     */
    public ErrorMessageInfo checkSyoumijyuuryouKeisan(FXHDD01 soujyuuryousokutei, FXHDD01 fuutaijyuuryou) {

        //「総重量」ﾁｪｯｸ
        if (StringUtil.isEmpty(soujyuuryousokutei.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(soujyuuryousokutei);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, true, errFxhdd01List, soujyuuryousokutei.getLabel1());
        }
        //「風袋重量」ﾁｪｯｸ
        if (StringUtil.isEmpty(fuutaijyuuryou.getValue())) {
            List<FXHDD01> errFxhdd01List = Arrays.asList(fuutaijyuuryou);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, true, errFxhdd01List, fuutaijyuuryou.getLabel1());
        }
        // 総重量<風袋重量の場合
        BigDecimal soujyuuryouVal = new BigDecimal(soujyuuryousokutei.getValue());
        BigDecimal youkijyuuryouVal = new BigDecimal(fuutaijyuuryou.getValue());
        if (soujyuuryouVal.compareTo(youkijyuuryouVal) == -1) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(soujyuuryousokutei, fuutaijyuuryou);
            return MessageUtil.getErrorMessageInfo("XHD-000023", true, true, errFxhdd01List, soujyuuryousokutei.getLabel1(), fuutaijyuuryou.getLabel1());
        }
        return null;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ有効期限計算
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     * @throws ParseException 変換エラー
     */
    public ProcessData setBinderyuukoukigenKeisan(ProcessData processData) throws ParseException {

        FXHDD01 itemBinderyuukoukigen = getItemRow(processData.getItemList(), GXHDO102B018Const.BINDERYUUKOUKIGEN);
        if (itemBinderyuukoukigen == null) {
            processData.setMethod("");
            return processData;
        }
        // ｼｽﾃﾑ日時(YYMMDD)+6か月(MM部分を+6)
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd");
        Date date = simpleDateFormat.parse(simpleDateFormat.format(new Date())); // ｼｽﾃﾑ日時
        // ｼｽﾃﾑ日時(YYMMDD)+時間で算出した日時を取得
        Date newDate = addMonth(date, 6);
        if (StringUtil.isEmpty(itemBinderyuukoukigen.getValue())) {
            itemBinderyuukoukigen.setValue(simpleDateFormat.format(newDate));
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * ｼｽﾃﾑ日時(YYMMDD)+時間で算出した日時を取得
     *
     * @param date ｼｽﾃﾑ日時
     * @param month 時間
     * @return ｼｽﾃﾑ日時+時間で算出した日時
     */
    private Date addMonth(Date date, int month) {
        Calendar clCalendar = Calendar.getInstance();
        clCalendar.setTime(date);
        clCalendar.add(Calendar.MONTH, month);
        return clCalendar.getTime();
    }

    /**
     * 測定日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setSokuteinichiji(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B018Const.SOKUTEI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B018Const.SOKUTEI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
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

                // ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ_仮登録登録処理
                insertTmpSrBinderBfp(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, strSystime, processData, formId);
            } else {

                // ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ_仮登録更新処理
                updateTmpSrBinderBfp(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);
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
            SrBinderBfp tmpSrBinderBfp = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrBinderBfp> srBinderBfpList = getSrBinderBfpData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo9, edaban);
                if (!srBinderBfpList.isEmpty()) {
                    tmpSrBinderBfp = srBinderBfpList.get(0);
                }

                deleteTmpSrBinderBfp(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);
            }

            // ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ_登録処理
            insertSrBinderBfp(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, strSystime, processData, tmpSrBinderBfp);

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
        processData.setUserAuthParam(GXHDO102B018Const.USER_AUTH_UPDATE_PARAM);

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

            // ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ_更新処理
            updateSrBinderBfp(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);

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
        FXHDD01 itemKakuhankaisiDay = getItemRow(processData.getItemList(), GXHDO102B018Const.ASSOUKAISI_DAY);  // 圧送開始日
        FXHDD01 itemKakuhankaisiTime = getItemRow(processData.getItemList(), GXHDO102B018Const.ASSOUKAISI_TIME); // 圧送開始時間
        FXHDD01 itemKakuhansyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B018Const.ASSOUSYUURYOU_DAY);    // 圧送終了日
        FXHDD01 itemKakuhansyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B018Const.ASSOUSYUURYOU_TIME); // 圧送終了時間
        if (itemKakuhankaisiDay != null && itemKakuhankaisiTime != null && itemKakuhansyuuryouDay != null && itemKakuhansyuuryouTime != null) {
            // 画面.圧送開始日 + 画面.圧送開始時間
            Date kakuhankaisiDate = DateUtil.convertStringToDate(itemKakuhankaisiDay.getValue(), itemKakuhankaisiTime.getValue());
            // 画面.圧送終了日 + 画面.圧送終了時間
            Date kakuhansyuuryouDate = DateUtil.convertStringToDate(itemKakuhansyuuryouDay.getValue(), itemKakuhansyuuryouTime.getValue());
            // R001チェック呼出し
            String msgKakuhankaisiCheckR001 = validateUtil.checkR001("圧送開始日時", kakuhankaisiDate, "圧送終了日時", kakuhansyuuryouDate);
            if (!StringUtil.isEmpty(msgKakuhankaisiCheckR001)) {
                // エラー発生時
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemKakuhankaisiDay, itemKakuhankaisiTime, itemKakuhansyuuryouDay, itemKakuhansyuuryouTime);
                return MessageUtil.getErrorMessageInfo("", msgKakuhankaisiCheckR001, true, true, errFxhdd01List);
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
        processData.setUserAuthParam(GXHDO102B018Const.USER_AUTH_DELETE_PARAM);

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

            // ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo9, edaban);
            insertDeleteDataTmpSrBinderBfp(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo9, edaban, strSystime);

            // ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ_削除処理
            deleteSrBinderBfp(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);

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
                        GXHDO102B018Const.BTN_UPDATE_TOP,
                        GXHDO102B018Const.BTN_DELETE_TOP,
                        GXHDO102B018Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B018Const.BTN_ASSOUKAISINICHIJI_TOP,
                        GXHDO102B018Const.BTN_ASSOUSYUURYOUNICHIJI_TOP,
                        GXHDO102B018Const.BTN_SOKUTEINICHIJI_TOP,
                        GXHDO102B018Const.BTN_SYOUMIJYUURYOU_KEISAN_TOP,
                        GXHDO102B018Const.BTN_BINDERYUUKOUKIGEN_KEISAN_TOP,
                        GXHDO102B018Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B018Const.BTN_DELETE_BOTTOM,
                        GXHDO102B018Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B018Const.BTN_ASSOUKAISINICHIJI_BOTTOM,
                        GXHDO102B018Const.BTN_ASSOUSYUURYOUNICHIJI_BOTTOM,
                        GXHDO102B018Const.BTN_SOKUTEINICHIJI_BOTTOM,
                        GXHDO102B018Const.BTN_SYOUMIJYUURYOU_KEISAN_BOTTOM,
                        GXHDO102B018Const.BTN_BINDERYUUKOUKIGEN_KEISAN_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B018Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B018Const.BTN_INSERT_TOP,
                        GXHDO102B018Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B018Const.BTN_INSERT_BOTTOM));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO102B018Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B018Const.BTN_INSERT_TOP,
                        GXHDO102B018Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B018Const.BTN_ASSOUKAISINICHIJI_TOP,
                        GXHDO102B018Const.BTN_ASSOUSYUURYOUNICHIJI_TOP,
                        GXHDO102B018Const.BTN_SOKUTEINICHIJI_TOP,
                        GXHDO102B018Const.BTN_SYOUMIJYUURYOU_KEISAN_TOP,
                        GXHDO102B018Const.BTN_BINDERYUUKOUKIGEN_KEISAN_TOP,
                        GXHDO102B018Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B018Const.BTN_INSERT_BOTTOM,
                        GXHDO102B018Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B018Const.BTN_ASSOUKAISINICHIJI_BOTTOM,
                        GXHDO102B018Const.BTN_ASSOUSYUURYOUNICHIJI_BOTTOM,
                        GXHDO102B018Const.BTN_SOKUTEINICHIJI_BOTTOM,
                        GXHDO102B018Const.BTN_SYOUMIJYUURYOU_KEISAN_BOTTOM,
                        GXHDO102B018Const.BTN_BINDERYUUKOUKIGEN_KEISAN_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B018Const.BTN_UPDATE_TOP,
                        GXHDO102B018Const.BTN_DELETE_TOP,
                        GXHDO102B018Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B018Const.BTN_DELETE_BOTTOM
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
        // 規格値により、画面項目を非表示にする。
        setItemNotShow(processData, queryRunnerDoc, queryRunnerQcdb, shikakariData, lotNo);
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
        this.setItemData(processData, GXHDO102B018Const.WIPLOTNO, lotNo);
        // ﾊﾞｲﾝﾀﾞｰ溶液品名
        this.setItemData(processData, GXHDO102B018Const.BINDERYOUEKIHINMEI, StringUtil.nullToBlank(getMapData(shikakariData, "hinmei")));
        // ﾊﾞｲﾝﾀﾞｰ溶液LotNo
        this.setItemData(processData, GXHDO102B018Const.BINDERYOUEKILOTNO, StringUtil.nullToBlank(getMapData(shikakariData, "lotno")));
        // ﾛｯﾄ区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode"));
        //ﾛｯﾄ区分名
        String lotkubun = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubun"));
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO102B018Const.LOTKUBUN, "");
        } else {
            if (!StringUtil.isEmpty(lotkubun)) {
                lotkubuncode = lotkubuncode + ":" + lotkubun;
            }
            this.setItemData(processData, GXHDO102B018Const.LOTKUBUN, lotkubuncode);
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

        List<SrBinderBfp> srBinderBfpList = new ArrayList<>();
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

            // ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽデータ取得
            srBinderBfpList = getSrBinderBfpData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo9, edaban);
            if (srBinderBfpList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srBinderBfpList.isEmpty()) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srBinderBfpList.get(0));

        return true;

    }

    /**
     * データ設定処理
     *
     * @param processData 処理制御データ
     * @param srBinderBfp ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽデータ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrBinderBfp srBinderBfp) {

        // 風袋重量
        this.setItemData(processData, GXHDO102B018Const.FUUTAIJYUURYOU, getSrBinderBfpItemData(GXHDO102B018Const.FUUTAIJYUURYOU, srBinderBfp));

        // ﾌｨﾙﾀｰLotNo
        this.setItemData(processData, GXHDO102B018Const.FILTERLOTNO, getSrBinderBfpItemData(GXHDO102B018Const.FILTERLOTNO, srBinderBfp));

        // 圧力
        this.setItemData(processData, GXHDO102B018Const.ATURYOKU, getSrBinderBfpItemData(GXHDO102B018Const.ATURYOKU, srBinderBfp));

        // 圧送開始日
        this.setItemData(processData, GXHDO102B018Const.ASSOUKAISI_DAY, getSrBinderBfpItemData(GXHDO102B018Const.ASSOUKAISI_DAY, srBinderBfp));

        // 圧送開始時間
        this.setItemData(processData, GXHDO102B018Const.ASSOUKAISI_TIME, getSrBinderBfpItemData(GXHDO102B018Const.ASSOUKAISI_TIME, srBinderBfp));

        // 圧送終了日
        this.setItemData(processData, GXHDO102B018Const.ASSOUSYUURYOU_DAY, getSrBinderBfpItemData(GXHDO102B018Const.ASSOUSYUURYOU_DAY, srBinderBfp));

        // 圧送終了時間
        this.setItemData(processData, GXHDO102B018Const.ASSOUSYUURYOU_TIME, getSrBinderBfpItemData(GXHDO102B018Const.ASSOUSYUURYOU_TIME, srBinderBfp));

        // 圧送時間
        this.setItemData(processData, GXHDO102B018Const.ASSOUJIKAN, getSrBinderBfpItemData(GXHDO102B018Const.ASSOUJIKAN, srBinderBfp));

        // ﾌｨﾙﾀｰ使用本数
        this.setItemData(processData, GXHDO102B018Const.FILTERSIYOUHONSUU, getSrBinderBfpItemData(GXHDO102B018Const.FILTERSIYOUHONSUU, srBinderBfp));

        // 総重量測定
        this.setItemData(processData, GXHDO102B018Const.SOUJYUURYOUSOKUTEI, getSrBinderBfpItemData(GXHDO102B018Const.SOUJYUURYOUSOKUTEI, srBinderBfp));

        // 正味重量
        this.setItemData(processData, GXHDO102B018Const.SYOUMIJYUURYOU, getSrBinderBfpItemData(GXHDO102B018Const.SYOUMIJYUURYOU, srBinderBfp));

        // ﾊﾞｲﾝﾀﾞｰ有効期限
        this.setItemData(processData, GXHDO102B018Const.BINDERYUUKOUKIGEN, getSrBinderBfpItemData(GXHDO102B018Const.BINDERYUUKOUKIGEN, srBinderBfp));

        // 測定日
        this.setItemData(processData, GXHDO102B018Const.SOKUTEI_DAY, getSrBinderBfpItemData(GXHDO102B018Const.SOKUTEI_DAY, srBinderBfp));

        // 測定時間
        this.setItemData(processData, GXHDO102B018Const.SOKUTEI_TIME, getSrBinderBfpItemData(GXHDO102B018Const.SOKUTEI_TIME, srBinderBfp));

        // 粘度測定値
        this.setItemData(processData, GXHDO102B018Const.NENDOSOKUTEITI, getSrBinderBfpItemData(GXHDO102B018Const.NENDOSOKUTEITI, srBinderBfp));

        // 温度
        this.setItemData(processData, GXHDO102B018Const.ONDO, getSrBinderBfpItemData(GXHDO102B018Const.ONDO, srBinderBfp));

        // 合否判定
        this.setItemData(processData, GXHDO102B018Const.GOUHIHANTEI, getSrBinderBfpItemData(GXHDO102B018Const.GOUHIHANTEI, srBinderBfp));

        // 担当者
        this.setItemData(processData, GXHDO102B018Const.TANTOUSYA, getSrBinderBfpItemData(GXHDO102B018Const.TANTOUSYA, srBinderBfp));

        // 確認者
        this.setItemData(processData, GXHDO102B018Const.KAKUNINSYA, getSrBinderBfpItemData(GXHDO102B018Const.KAKUNINSYA, srBinderBfp));

        // 備考1
        this.setItemData(processData, GXHDO102B018Const.BIKOU1, getSrBinderBfpItemData(GXHDO102B018Const.BIKOU1, srBinderBfp));

        // 備考2
        this.setItemData(processData, GXHDO102B018Const.BIKOU2, getSrBinderBfpItemData(GXHDO102B018Const.BIKOU2, srBinderBfp));

    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽの入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrBinderBfp> getSrBinderBfpData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrBinderBfp(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSrBinderBfp(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
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
     * [ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrBinderBfp> loadSrBinderBfp(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = " SELECT "
                + " kojyo,lotno,edaban,binderyouekihinmei,binderyouekilotno,lotkubun,dispagouki,fuutaijyuuryou,filterhinmei,"
                + "filterlotno,aturyoku,assoukaisinichiji,assousyuuryounichiji,assoujikan,filtersiyouhonsuu,soujyuuryousokutei,"
                + "syoumijyuuryou,binderyuukoukigen,sokuteinichiji,nendosokuteiti,ondo,gouhihantei,tantousya,kakuninsya,"
                + "bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + " FROM sr_binder_bfp "
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
        mapping.put("kojyo", "kojyo");                                 // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                 // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                               // 枝番
        mapping.put("binderyouekihinmei", "binderyouekihinmei");       // ﾊﾞｲﾝﾀﾞｰ溶液品名
        mapping.put("binderyouekilotno", "binderyouekilotno");         // ﾊﾞｲﾝﾀﾞｰ溶液LotNo
        mapping.put("lotkubun", "lotkubun");                           // ﾛｯﾄ区分
        mapping.put("dispagouki", "dispagouki");                       // ﾃﾞｨｽﾊﾟ号機
        mapping.put("fuutaijyuuryou", "fuutaijyuuryou");               // 風袋重量
        mapping.put("filterhinmei", "filterhinmei");                   // ﾌｨﾙﾀｰ品名
        mapping.put("filterlotno", "filterlotno");                     // ﾌｨﾙﾀｰLotNo
        mapping.put("aturyoku", "aturyoku");                           // 圧力
        mapping.put("assoukaisinichiji", "assoukaisinichiji");         // 圧送開始日時
        mapping.put("assousyuuryounichiji", "assousyuuryounichiji");   // 圧送終了日時
        mapping.put("assoujikan", "assoujikan");                       // 圧送時間
        mapping.put("filtersiyouhonsuu", "filtersiyouhonsuu");         // ﾌｨﾙﾀｰ使用本数
        mapping.put("soujyuuryousokutei", "soujyuuryousokutei");       // 総重量測定
        mapping.put("syoumijyuuryou", "syoumijyuuryou");               // 正味重量
        mapping.put("binderyuukoukigen", "binderyuukoukigen");         // ﾊﾞｲﾝﾀﾞｰ有効期限
        mapping.put("sokuteinichiji", "sokuteinichiji");               // 測定日時
        mapping.put("nendosokuteiti", "nendosokuteiti");               // 粘度測定値
        mapping.put("ondo", "ondo");                                   // 温度
        mapping.put("gouhihantei", "gouhihantei");                     // 合否判定
        mapping.put("tantousya", "tantousya");                         // 担当者
        mapping.put("kakuninsya", "kakuninsya");                       // 確認者
        mapping.put("bikou1", "bikou1");                               // 備考1
        mapping.put("bikou2", "bikou2");                               // 備考2
        mapping.put("torokunichiji", "torokunichiji");                 // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                   // 更新日時
        mapping.put("revision", "revision");                           // revision

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrBinderBfp>> beanHandler = new BeanListHandler<>(SrBinderBfp.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrBinderBfp> loadTmpSrBinderBfp(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = " SELECT "
                + " kojyo,lotno,edaban,binderyouekihinmei,binderyouekilotno,lotkubun,dispagouki,fuutaijyuuryou,filterhinmei,"
                + "filterlotno,aturyoku,assoukaisinichiji,assousyuuryounichiji,assoujikan,filtersiyouhonsuu,soujyuuryousokutei,"
                + "syoumijyuuryou,binderyuukoukigen,sokuteinichiji,nendosokuteiti,ondo,gouhihantei,tantousya,kakuninsya,"
                + "bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + " FROM tmp_sr_binder_bfp "
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
        mapping.put("kojyo", "kojyo");                                 // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                 // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                               // 枝番
        mapping.put("binderyouekihinmei", "binderyouekihinmei");       // ﾊﾞｲﾝﾀﾞｰ溶液品名
        mapping.put("binderyouekilotno", "binderyouekilotno");         // ﾊﾞｲﾝﾀﾞｰ溶液LotNo
        mapping.put("lotkubun", "lotkubun");                           // ﾛｯﾄ区分
        mapping.put("dispagouki", "dispagouki");                       // ﾃﾞｨｽﾊﾟ号機
        mapping.put("fuutaijyuuryou", "fuutaijyuuryou");               // 風袋重量
        mapping.put("filterhinmei", "filterhinmei");                   // ﾌｨﾙﾀｰ品名
        mapping.put("filterlotno", "filterlotno");                     // ﾌｨﾙﾀｰLotNo
        mapping.put("aturyoku", "aturyoku");                           // 圧力
        mapping.put("assoukaisinichiji", "assoukaisinichiji");         // 圧送開始日時
        mapping.put("assousyuuryounichiji", "assousyuuryounichiji");   // 圧送終了日時
        mapping.put("assoujikan", "assoujikan");                       // 圧送時間
        mapping.put("filtersiyouhonsuu", "filtersiyouhonsuu");         // ﾌｨﾙﾀｰ使用本数
        mapping.put("soujyuuryousokutei", "soujyuuryousokutei");       // 総重量測定
        mapping.put("syoumijyuuryou", "syoumijyuuryou");               // 正味重量
        mapping.put("binderyuukoukigen", "binderyuukoukigen");         // ﾊﾞｲﾝﾀﾞｰ有効期限
        mapping.put("sokuteinichiji", "sokuteinichiji");               // 測定日時
        mapping.put("nendosokuteiti", "nendosokuteiti");               // 粘度測定値
        mapping.put("ondo", "ondo");                                   // 温度
        mapping.put("gouhihantei", "gouhihantei");                     // 合否判定
        mapping.put("tantousya", "tantousya");                         // 担当者
        mapping.put("kakuninsya", "kakuninsya");                       // 確認者
        mapping.put("bikou1", "bikou1");                               // 備考1
        mapping.put("bikou2", "bikou2");                               // 備考2
        mapping.put("torokunichiji", "torokunichiji");                 // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                   // 更新日時
        mapping.put("revision", "revision");                           // revision
        mapping.put("deleteflag", "deleteflag");                       // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrBinderBfp>> beanHandler = new BeanListHandler<>(SrBinderBfp.class, rowProcessor);

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
     * @param srBinderBfp ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽデータ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrBinderBfp srBinderBfp) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srBinderBfp != null) {
            // 元データが存在する場合元データより取得
            return getSrBinderBfpItemData(itemId, srBinderBfp);
        } else {
            return null;
        }
    }

    /**
     * 項目データ(入力値)取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @param srBinderBfp ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽデータ
     * @return 入力値
     */
    private String getItemKikakuchi(List<FXHDD01> listData, String itemId, SrBinderBfp srBinderBfp) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return StringUtil.nullToBlank(selectData.get(0).getKikakuChi()).replace("【", "").replace("】", "");
        } else if (srBinderBfp != null) {
            // 元データが存在する場合元データより取得
            return getSrBinderBfpItemData(itemId, srBinderBfp);
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
     * ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ_仮登録(tmp_sr_binder_bfp)登録処理
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
    private void insertTmpSrBinderBfp(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData, String formId) throws SQLException {

        String sql = "INSERT INTO tmp_sr_binder_bfp ("
                + " kojyo,lotno,edaban,binderyouekihinmei,binderyouekilotno,lotkubun,dispagouki,fuutaijyuuryou,filterhinmei,"
                + "filterlotno,aturyoku,assoukaisinichiji,assousyuuryounichiji,assoujikan,filtersiyouhonsuu,soujyuuryousokutei,"
                + "syoumijyuuryou,binderyuukoukigen,sokuteinichiji,nendosokuteiti,ondo,gouhihantei,tantousya,kakuninsya,"
                + "bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + " ) VALUES ( "
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrBinderBfp(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, processData, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ_仮登録(tmp_sr_binder_bfp)更新処理
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
    private void updateTmpSrBinderBfp(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "UPDATE tmp_sr_binder_bfp SET "
                + " binderyouekihinmei = ?,binderyouekilotno = ?,lotkubun = ?,dispagouki = ?,fuutaijyuuryou = ?,filterhinmei = ?,filterlotno = ?,"
                + "aturyoku = ?,assoukaisinichiji = ?,assousyuuryounichiji = ?,assoujikan = ?,filtersiyouhonsuu = ?,soujyuuryousokutei = ?,"
                + "syoumijyuuryou = ?,binderyuukoukigen = ?,sokuteinichiji = ?,nendosokuteiti = ?,ondo = ?,gouhihantei = ?,tantousya = ?,kakuninsya = ?,"
                + "bikou1 = ?,bikou2 = ?,kosinnichiji = ?,revision = ?,deleteflag = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrBinderBfp> srBinderBfpList = getSrBinderBfpData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrBinderBfp srBinderBfp = null;
        if (!srBinderBfpList.isEmpty()) {
            srBinderBfp = srBinderBfpList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrBinderBfp(false, newRev, 0, "", "", "", systemTime, processData, srBinderBfp);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ_仮登録(tmp_sr_binder_bfp)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrBinderBfp(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sr_binder_bfp "
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
     * ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ_仮登録(tmp_sr_binder_bfp)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srBinderBfp ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽデータ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrBinderBfp(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, String systemTime, ProcessData processData, SrBinderBfp srBinderBfp) {

        List<FXHDD01> pItemList = processData.getItemList();
        List<Object> params = new ArrayList<>();
        // 圧送開始時間
        String assoukaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B018Const.ASSOUKAISI_TIME, srBinderBfp));
        // 圧送終了時間
        String assousyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B018Const.ASSOUSYUURYOU_TIME, srBinderBfp));
        // 測定時間
        String sokuteiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B018Const.SOKUTEI_TIME, srBinderBfp));
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B018Const.BINDERYOUEKIHINMEI, srBinderBfp))); // ﾊﾞｲﾝﾀﾞｰ溶液品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B018Const.BINDERYOUEKILOTNO, srBinderBfp))); // ﾊﾞｲﾝﾀﾞｰ溶液LotNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B018Const.LOTKUBUN, srBinderBfp))); // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B018Const.DISPAGOUKI, srBinderBfp))); // ﾃﾞｨｽﾊﾟ号機
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B018Const.FUUTAIJYUURYOU, srBinderBfp))); // 風袋重量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B018Const.FILTERHINMEI, srBinderBfp))); // ﾌｨﾙﾀｰ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B018Const.FILTERLOTNO, srBinderBfp))); // ﾌｨﾙﾀｰLotNo
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B018Const.ATURYOKU, srBinderBfp))); // 圧力
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B018Const.ASSOUKAISI_DAY, srBinderBfp),
                "".equals(assoukaisiTime) ? "0000" : assoukaisiTime)); // 圧送開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B018Const.ASSOUSYUURYOU_DAY, srBinderBfp),
                "".equals(assousyuuryouTime) ? "0000" : assousyuuryouTime)); // 圧送終了日時
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B018Const.ASSOUJIKAN, srBinderBfp))); // 圧送時間
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B018Const.FILTERSIYOUHONSUU, srBinderBfp))); // ﾌｨﾙﾀｰ使用本数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B018Const.SOUJYUURYOUSOKUTEI, srBinderBfp))); // 総重量測定
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B018Const.SYOUMIJYUURYOU, srBinderBfp))); // 正味重量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B018Const.BINDERYUUKOUKIGEN, srBinderBfp))); // ﾊﾞｲﾝﾀﾞｰ有効期限
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B018Const.SOKUTEI_DAY, srBinderBfp),
                "".equals(sokuteiTime) ? "0000" : sokuteiTime)); // 測定日時
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B018Const.NENDOSOKUTEITI, srBinderBfp))); // 粘度測定値
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B018Const.ONDO, srBinderBfp))); // 温度
        params.add(getComboBoxDbValue(getItemData(pItemList, GXHDO102B018Const.GOUHIHANTEI, srBinderBfp), null)); // 合否判定
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B018Const.TANTOUSYA, srBinderBfp))); // 担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B018Const.KAKUNINSYA, srBinderBfp))); // 確認者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B018Const.BIKOU1, srBinderBfp))); // 備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B018Const.BIKOU2, srBinderBfp))); // 備考2

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
     * ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ(sr_binder_bfp)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srBinderBfp 登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrBinderBfp(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData, SrBinderBfp srBinderBfp) throws SQLException {

        String sql = "INSERT INTO sr_binder_bfp ("
                + " kojyo,lotno,edaban,binderyouekihinmei,binderyouekilotno,lotkubun,dispagouki,fuutaijyuuryou,filterhinmei,"
                + "filterlotno,aturyoku,assoukaisinichiji,assousyuuryounichiji,assoujikan,filtersiyouhonsuu,soujyuuryousokutei,"
                + "syoumijyuuryou,binderyuukoukigen,sokuteinichiji,nendosokuteiti,ondo,gouhihantei,tantousya,kakuninsya,"
                + "bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + " ) VALUES ( "
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrBinderBfp(true, newRev, kojyo, lotNo, edaban, systemTime, processData, srBinderBfp);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ(sr_binder_bfp)更新処理
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
    private void updateSrBinderBfp(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "UPDATE sr_binder_bfp SET "
                + " binderyouekihinmei = ?,binderyouekilotno = ?,lotkubun = ?,dispagouki = ?,fuutaijyuuryou = ?,filterhinmei = ?,filterlotno = ?,"
                + "aturyoku = ?,assoukaisinichiji = ?,assousyuuryounichiji = ?,assoujikan = ?,filtersiyouhonsuu = ?,soujyuuryousokutei = ?,"
                + "syoumijyuuryou = ?,binderyuukoukigen = ?,sokuteinichiji = ?,nendosokuteiti = ?,ondo = ?,gouhihantei = ?,tantousya = ?,kakuninsya = ?,"
                + "bikou1 = ?,bikou2 = ?,kosinnichiji = ?,revision = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrBinderBfp> srBinderBfpList = getSrBinderBfpData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrBinderBfp srBinderBfp = null;
        if (!srBinderBfpList.isEmpty()) {
            srBinderBfp = srBinderBfpList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrBinderBfp(false, newRev, "", "", "", systemTime, processData, srBinderBfp);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ(sr_binder_bfp)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srBinderBfp ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽデータ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrBinderBfp(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            String systemTime, ProcessData processData, SrBinderBfp srBinderBfp) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // 圧送開始時間
        String assoukaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B018Const.ASSOUKAISI_TIME, srBinderBfp));
        // 圧送終了時間
        String assousyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B018Const.ASSOUSYUURYOU_TIME, srBinderBfp));
        // 測定時間
        String sokuteiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B018Const.SOKUTEI_TIME, srBinderBfp));
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }

        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B018Const.BINDERYOUEKIHINMEI, srBinderBfp))); // ﾊﾞｲﾝﾀﾞｰ溶液品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B018Const.BINDERYOUEKILOTNO, srBinderBfp))); // ﾊﾞｲﾝﾀﾞｰ溶液LotNo
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B018Const.LOTKUBUN, srBinderBfp))); // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B018Const.DISPAGOUKI, srBinderBfp))); // ﾃﾞｨｽﾊﾟ号機
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B018Const.FUUTAIJYUURYOU, srBinderBfp))); // 風袋重量
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B018Const.FILTERHINMEI, srBinderBfp))); // ﾌｨﾙﾀｰ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B018Const.FILTERLOTNO, srBinderBfp))); // ﾌｨﾙﾀｰLotNo
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B018Const.ATURYOKU, srBinderBfp))); // 圧力
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B018Const.ASSOUKAISI_DAY, srBinderBfp),
                "".equals(assoukaisiTime) ? "0000" : assoukaisiTime)); // 圧送開始日時
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B018Const.ASSOUSYUURYOU_DAY, srBinderBfp),
                "".equals(assousyuuryouTime) ? "0000" : assousyuuryouTime)); // 圧送終了日時
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B018Const.ASSOUJIKAN, srBinderBfp))); // 圧送時間
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B018Const.FILTERSIYOUHONSUU, srBinderBfp))); // ﾌｨﾙﾀｰ使用本数
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B018Const.SOUJYUURYOUSOKUTEI, srBinderBfp))); // 総重量測定
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B018Const.SYOUMIJYUURYOU, srBinderBfp))); // 正味重量
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B018Const.BINDERYUUKOUKIGEN, srBinderBfp))); // ﾊﾞｲﾝﾀﾞｰ有効期限
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B018Const.SOKUTEI_DAY, srBinderBfp),
                "".equals(sokuteiTime) ? "0000" : sokuteiTime)); // 測定日時
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B018Const.NENDOSOKUTEITI, srBinderBfp))); // 粘度測定値
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B018Const.ONDO, srBinderBfp))); // 温度
        params.add(getComboBoxDbValue(getItemData(pItemList, GXHDO102B018Const.GOUHIHANTEI, srBinderBfp), 9)); // 合否判定
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B018Const.TANTOUSYA, srBinderBfp))); // 担当者
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B018Const.KAKUNINSYA, srBinderBfp))); // 確認者
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B018Const.BIKOU1, srBinderBfp))); // 備考1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B018Const.BIKOU2, srBinderBfp))); // 備考2

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
     * ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ(sr_binder_bfp)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSrBinderBfp(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sr_binder_bfp "
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
     * [ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_binder_bfp "
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
     * @param srBinderBfp ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽデータ
     * @return DB値
     */
    private String getSrBinderBfpItemData(String itemId, SrBinderBfp srBinderBfp) {
        switch (itemId) {
            // ﾊﾞｲﾝﾀﾞｰ溶液品名
            case GXHDO102B018Const.BINDERYOUEKIHINMEI:
                return StringUtil.nullToBlank(srBinderBfp.getBinderyouekihinmei());

            // ﾊﾞｲﾝﾀﾞｰ溶液LotNo
            case GXHDO102B018Const.BINDERYOUEKILOTNO:
                return StringUtil.nullToBlank(srBinderBfp.getBinderyouekilotno());

            // ﾛｯﾄ区分
            case GXHDO102B018Const.LOTKUBUN:
                return StringUtil.nullToBlank(srBinderBfp.getLotkubun());

            // ﾃﾞｨｽﾊﾟ号機
            case GXHDO102B018Const.DISPAGOUKI:
                return StringUtil.nullToBlank(srBinderBfp.getDispagouki());

            // 風袋重量
            case GXHDO102B018Const.FUUTAIJYUURYOU:
                return StringUtil.nullToBlank(srBinderBfp.getFuutaijyuuryou());

            // ﾌｨﾙﾀｰ品名
            case GXHDO102B018Const.FILTERHINMEI:
                return StringUtil.nullToBlank(srBinderBfp.getFilterhinmei());

            // ﾌｨﾙﾀｰLotNo
            case GXHDO102B018Const.FILTERLOTNO:
                return StringUtil.nullToBlank(srBinderBfp.getFilterlotno());

            // 圧力
            case GXHDO102B018Const.ATURYOKU:
                return StringUtil.nullToBlank(srBinderBfp.getAturyoku());

            // 圧送開始日
            case GXHDO102B018Const.ASSOUKAISI_DAY:
                return DateUtil.formattedTimestamp(srBinderBfp.getAssoukaisinichiji(), "yyMMdd");

            // 圧送開始時間
            case GXHDO102B018Const.ASSOUKAISI_TIME:
                return DateUtil.formattedTimestamp(srBinderBfp.getAssoukaisinichiji(), "HHmm");

            // 圧送終了日
            case GXHDO102B018Const.ASSOUSYUURYOU_DAY:
                return DateUtil.formattedTimestamp(srBinderBfp.getAssousyuuryounichiji(), "yyMMdd");

            // 圧送終了時間
            case GXHDO102B018Const.ASSOUSYUURYOU_TIME:
                return DateUtil.formattedTimestamp(srBinderBfp.getAssousyuuryounichiji(), "HHmm");

            // 圧送時間
            case GXHDO102B018Const.ASSOUJIKAN:
                return StringUtil.nullToBlank(srBinderBfp.getAssoujikan());

            // ﾌｨﾙﾀｰ使用本数
            case GXHDO102B018Const.FILTERSIYOUHONSUU:
                return StringUtil.nullToBlank(srBinderBfp.getFiltersiyouhonsuu());

            // 総重量測定
            case GXHDO102B018Const.SOUJYUURYOUSOKUTEI:
                return StringUtil.nullToBlank(srBinderBfp.getSoujyuuryousokutei());

            // 正味重量
            case GXHDO102B018Const.SYOUMIJYUURYOU:
                return StringUtil.nullToBlank(srBinderBfp.getSyoumijyuuryou());

            // ﾊﾞｲﾝﾀﾞｰ有効期限
            case GXHDO102B018Const.BINDERYUUKOUKIGEN:
                return StringUtil.nullToBlank(srBinderBfp.getBinderyuukoukigen());

            // 測定日
            case GXHDO102B018Const.SOKUTEI_DAY:
                return DateUtil.formattedTimestamp(srBinderBfp.getSokuteinichiji(), "yyMMdd");

            // 測定時間
            case GXHDO102B018Const.SOKUTEI_TIME:
                return DateUtil.formattedTimestamp(srBinderBfp.getSokuteinichiji(), "HHmm");

            // 粘度測定値
            case GXHDO102B018Const.NENDOSOKUTEITI:
                return StringUtil.nullToBlank(srBinderBfp.getNendosokuteiti());

            // 温度
            case GXHDO102B018Const.ONDO:
                return StringUtil.nullToBlank(srBinderBfp.getOndo());

            // 合否判定
            case GXHDO102B018Const.GOUHIHANTEI:
                return getComboBoxCheckValue(StringUtil.nullToBlank(srBinderBfp.getGouhihantei()));

            // 担当者
            case GXHDO102B018Const.TANTOUSYA:
                return StringUtil.nullToBlank(srBinderBfp.getTantousya());

            // 確認者
            case GXHDO102B018Const.KAKUNINSYA:
                return StringUtil.nullToBlank(srBinderBfp.getKakuninsya());

            // 備考1
            case GXHDO102B018Const.BIKOU1:
                return StringUtil.nullToBlank(srBinderBfp.getBikou1());

            // 備考2
            case GXHDO102B018Const.BIKOU2:
                return StringUtil.nullToBlank(srBinderBfp.getBikou2());

            default:
                return null;
        }
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ_仮登録(tmp_sr_binder_bfp)登録処理(削除時)
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
    private void insertDeleteDataTmpSrBinderBfp(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_binder_bfp ( "
                + " kojyo,lotno,edaban,binderyouekihinmei,binderyouekilotno,lotkubun,dispagouki,fuutaijyuuryou,filterhinmei,"
                + "filterlotno,aturyoku,assoukaisinichiji,assousyuuryounichiji,assoujikan,filtersiyouhonsuu,soujyuuryousokutei,"
                + "syoumijyuuryou,binderyuukoukigen,sokuteinichiji,nendosokuteiti,ondo,gouhihantei,tantousya,kakuninsya,"
                + "bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") SELECT "
                + " kojyo,lotno,edaban,binderyouekihinmei,binderyouekilotno,lotkubun,dispagouki,fuutaijyuuryou,filterhinmei,"
                + "filterlotno,aturyoku,assoukaisinichiji,assousyuuryounichiji,assoujikan,filtersiyouhonsuu,soujyuuryousokutei,"
                + "syoumijyuuryou,binderyuukoukigen,sokuteinichiji,nendosokuteiti,ondo,gouhihantei,tantousya,kakuninsya,"
                + "bikou1,bikou2, "
                + " ?,?,?,? "
                + " FROM sr_binder_bfp "
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
     * 規格値により、画面項目を非表示にする。
     *
     * @param processData 処理制御データ
     * @param queryRunnerDoc QueryRunnerオブジェクト(DocServer)
     * @param queryRunnerQcdb QueryRunnerオブジェクト(Qcdb)
     * @param shikakariData 前工程WIPから仕掛情報
     * @param lotNo ﾛｯﾄNo
     * @throws SQLException 例外エラー
     */
    private void setItemNotShow(ProcessData processData, QueryRunner queryRunnerDoc, QueryRunner queryRunnerQcdb, Map shikakariData, String lotNo) throws SQLException {

        String kojyo = lotNo.substring(0, 3);
        String lotNo9 = lotNo.substring(3, 12);
        String edaban = lotNo.substring(12, 15);
        String syurui = "ﾊﾞｲﾝﾀﾞｰ溶液作製";
        // [ﾊﾟﾗﾒｰﾀﾏｽﾀ]から、ﾃﾞｰﾀを取得
        Map fxhbm03Data = loadFxhbm03Data(queryRunnerDoc, "ﾊﾞｲﾝﾀﾞｰ溶液作製_ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ_表示制御");
        // 画面非表示項目リスト:風袋重量、ﾌｨﾙﾀｰ品名、ﾌｨﾙﾀｰLotNo、圧力、圧送開始日、圧送開始時間、圧送終了日、圧送終了時間、圧送時間、ﾌｨﾙﾀｰ使用本数、総重量測定、正味重量、ﾊﾞｲﾝﾀﾞｰ有効期限
        List<String> notShowItemList = Arrays.asList(GXHDO102B018Const.FUUTAIJYUURYOU, GXHDO102B018Const.FILTERHINMEI, GXHDO102B018Const.FILTERLOTNO, GXHDO102B018Const.ATURYOKU,
                GXHDO102B018Const.ASSOUKAISI_DAY, GXHDO102B018Const.ASSOUKAISI_TIME, GXHDO102B018Const.ASSOUSYUURYOU_DAY, GXHDO102B018Const.ASSOUSYUURYOU_TIME,
                GXHDO102B018Const.ASSOUJIKAN, GXHDO102B018Const.FILTERSIYOUHONSUU, GXHDO102B018Const.SOUJYUURYOUSOKUTEI, GXHDO102B018Const.SYOUMIJYUURYOU, GXHDO102B018Const.BINDERYUUKOUKIGEN);
        if (fxhbm03Data == null) {
            // 取得できなかった場合、以下の項目を非表示にして処理を終了する。
            removeItemFromItemList(processData, notShowItemList);
            return;
        }

        // [前工程設計]から、ﾃﾞｰﾀを取得
        Map daMkSekKeiData = loadDaMkSekKeiData(queryRunnerQcdb, kojyo, lotNo9, edaban, syurui);
        if (daMkSekKeiData == null || daMkSekKeiData.isEmpty()) {
            // 取得できなかった場合、以下の項目を非表示にして処理を終了する。
            removeItemFromItemList(processData, notShowItemList);
            return;
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
            Map daMkhYoJunJokenData = loadDaMkhYoJunJokenData(queryRunnerQcdb, (String) shikakariData.get("hinmei"), pattern, syurui);
            if (daMkhYoJunJokenData == null || daMkhYoJunJokenData.isEmpty()) {
                // 取得できなかった場合、以下の項目を非表示にして処理を終了する。
                removeItemFromItemList(processData, notShowItemList);
                return;
            }
            // 前工程規格情報の規格値
            String kikakuti = StringUtil.nullToBlank(getMapData(daMkhYoJunJokenData, "kikakuti"));
            if (!"1".equals(kikakuti)) {
                // 取得できなかった場合、以下の項目を非表示にして処理を終了する。
                removeItemFromItemList(processData, notShowItemList);
            }
        } else {
            // 前工程規格情報の規格値
            String kikakuti = StringUtil.nullToBlank(getMapData(daMkJokenData, "kikakuti"));
            if (!"1".equals(kikakuti)) {
                // 取得できなかった場合、以下の項目を非表示にして処理を終了する。
                removeItemFromItemList(processData, notShowItemList);
            }
        }
    }

    /**
     * 項目の表示制御
     *
     * @param processData 処理制御データ
     * @param notShowItemList 画面非表示項目リスト
     */
    private void removeItemFromItemList(ProcessData processData, List<String> notShowItemList) {
        List<FXHDD01> itemList = processData.getItemList();
        // 以下の項目を画面非表示にする。
        notShowItemList.forEach((notShowItem) -> {
            itemList.remove(getItemRow(itemList, notShowItem));
        });
        itemList.sort(Comparator.comparing(FXHDD01::getItemNo));
        for (int i = 0; i < itemList.size(); i++) {
            FXHDD01 item = itemList.get(i);
            item.setItemIndex(i + 1);
        }
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

    /**
     * コンボボックス値(コンボボックス内のValue値)取得
     *
     * @param dbValue コンボボックス(DB内)Value値
     * @return コンボボックステキスト値
     */
    private String getComboBoxCheckValue(String dbValue) {
        if ("0".equals(dbValue)) {
            return "不合格";
        } else if ("1".equals(dbValue)) {
            return "合格";
        }
        return null;
    }

    /**
     * コンボボックス値(DB内のValue値)取得
     *
     * @param comboBoxValue コンボボックスValue値
     * @param defaultValue 選択されていないときのデフォルト
     * @return コンボボックステキスト値
     */
    private Integer getComboBoxDbValue(String checkBoxValue, Integer defaultValue) {
        if ("不合格".equals(StringUtil.nullToBlank(checkBoxValue))) {
            return 0;
        } else if ("合格".equals(StringUtil.nullToBlank(checkBoxValue))) {
            return 1;
        }
        return defaultValue;
    }
}
