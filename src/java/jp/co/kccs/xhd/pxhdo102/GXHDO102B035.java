/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo102;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import jp.co.kccs.xhd.db.model.SrSlipFpBaketsu;
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
 * 変更日	2021/12/15<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B035(ｽﾘｯﾌﾟ作製・FP(ﾊﾞｹﾂ))
 *
 * @author KCSS K.Jo
 * @since 2021/12/15
 */
public class GXHDO102B035 implements IFormLogic {

    private static final Logger LOGGER = Logger.getLogger(GXHDO102B035.class.getName());
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
    public GXHDO102B035() {
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
                    GXHDO102B035Const.BTN_EDABAN_COPY_TOP,
                    GXHDO102B035Const.BTN_FPKAISHI_TOP,
                    GXHDO102B035Const.BTN_FILTERKOUKAN1FPTEISHI_TOP,
                    GXHDO102B035Const.BTN_FILTERKOUKAN1FPSAIKAI_TOP,
                    GXHDO102B035Const.BTN_EDABAN_COPY_BOTTOM,
                    GXHDO102B035Const.BTN_FPKAISHI_BOTTOM,
                    GXHDO102B035Const.BTN_FILTERKOUKAN1FPTEISHI_BOTTOM,
                    GXHDO102B035Const.BTN_FILTERKOUKAN1FPSAIKAI_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO102B035Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO102B035Const.BTN_INSERT_TOP,
                    GXHDO102B035Const.BTN_DELETE_TOP,
                    GXHDO102B035Const.BTN_UPDATE_TOP,
                    GXHDO102B035Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO102B035Const.BTN_INSERT_BOTTOM,
                    GXHDO102B035Const.BTN_DELETE_BOTTOM,
                    GXHDO102B035Const.BTN_UPDATE_BOTTOM
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
            case GXHDO102B035Const.BTN_EDABAN_COPY_TOP:
            case GXHDO102B035Const.BTN_EDABAN_COPY_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 仮登録
            case GXHDO102B035Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO102B035Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO102B035Const.BTN_INSERT_TOP:
            case GXHDO102B035Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 修正
            case GXHDO102B035Const.BTN_UPDATE_TOP:
            case GXHDO102B035Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO102B035Const.BTN_DELETE_TOP:
            case GXHDO102B035Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // F/P開始日時
            case GXHDO102B035Const.BTN_FPKAISHI_TOP:
            case GXHDO102B035Const.BTN_FPKAISHI_BOTTOM:
                method = "setFpkaishiDateTime";
                break;
            // F/P停止日時
            case GXHDO102B035Const.BTN_FILTERKOUKAN1FPTEISHI_TOP:
            case GXHDO102B035Const.BTN_FILTERKOUKAN1FPTEISHI_BOTTOM:
                method = "setFilterkoukan1fpteishiDateTime";
                break;
            // F/P再開日時
            case GXHDO102B035Const.BTN_FILTERKOUKAN1FPSAIKAI_TOP:
            case GXHDO102B035Const.BTN_FILTERKOUKAN1FPSAIKAI_BOTTOM:
                method = "setFilterkoukan1fpsaikaiDateTime";
                break;
            // F/P終了日時
            case GXHDO102B035Const.BTN_FPSYURYOU_TOP:
            case GXHDO102B035Const.BTN_FPSYURYOU_BOTTOM:
                method = "setFpsyuryouDateTime";
                break;
            // 正味重量①
            case GXHDO102B035Const.BTN_SYOUMIJYUURYOU1_TOP:
            case GXHDO102B035Const.BTN_SYOUMIJYUURYOU1_BOTTOM:
                method = "setSyoumijyuuryou1";
                break;
            // 正味重量②
            case GXHDO102B035Const.BTN_SYOUMIJYUURYOU2_TOP:
            case GXHDO102B035Const.BTN_SYOUMIJYUURYOU2_BOTTOM:
                method = "setSyoumijyuuryou2";
                break;
            // 正味重量③
            case GXHDO102B035Const.BTN_SYOUMIJYUURYOU3_TOP:
            case GXHDO102B035Const.BTN_SYOUMIJYUURYOU3_BOTTOM:
                method = "setSyoumijyuuryou3";
                break;
            // 正味重量④
            case GXHDO102B035Const.BTN_SYOUMIJYUURYOU4_TOP:
            case GXHDO102B035Const.BTN_SYOUMIJYUURYOU4_BOTTOM:
                method = "setSyoumijyuuryou4";
                break;
            // 正味重量⑤
            case GXHDO102B035Const.BTN_SYOUMIJYUURYOU5_TOP:
            case GXHDO102B035Const.BTN_SYOUMIJYUURYOU5_BOTTOM:
                method = "setSyoumijyuuryou5";
                break;
            // 正味重量⑥
            case GXHDO102B035Const.BTN_SYOUMIJYUURYOU6_TOP:
            case GXHDO102B035Const.BTN_SYOUMIJYUURYOU6_BOTTOM:
                method = "setSyoumijyuuryou6";
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

            // ｽﾘｯﾌﾟ作製・FP(バケツ)の入力項目の登録データ(仮登録時は仮登録データ)を取得
            List<SrSlipFpBaketsu> srSlipFpBaketsuDataList = getSrSlipFpBaketsuData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo9, oyalotEdaban, paramJissekino);
            if (srSlipFpBaketsuDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // メイン画面データ設定
            setInputItemDataMainForm(processData, srSlipFpBaketsuDataList.get(0));

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
     * F/P開始日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setFpkaishiDateTime(ProcessData processData) {

        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B035Const.FPKAISHI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B035Const.FPKAISHI_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * F/P停止日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setFilterkoukan1fpteishiDateTime(ProcessData processData) {

        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B035Const.FILTERKOUKAN1FPTEISHI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B035Const.FILTERKOUKAN1FPTEISHI_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * F/P再開日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setFilterkoukan1fpsaikaiDateTime(ProcessData processData) {

        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B035Const.FILTERKOUKAN1FPSAIKAI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B035Const.FILTERKOUKAN1FPSAIKAI_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * F/P終了日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setFpsyuryouDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B035Const.FPSYURYOU_DAY); // F/P終了日
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B035Const.FPSYURYOU_TIME); // F/P終了時間
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            // 【F/P終了日時】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
            ErrorMessageInfo checkItemErrorInfo = checkFpsyuuryounichijiDateTime(processData);
            if (checkItemErrorInfo != null) {
                processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
                return processData;
            }
            // F/P終了日時設定
            setDateTimeItem(itemDay, itemTime, new Date());
            // 「F/P時間」計算処理
            setFpjikanItem(processData);
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 【F/P終了日時】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
     *
     * @param processData 処理制御データ
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkFpsyuuryounichijiDateTime(ProcessData processData) {
        FXHDD01 itemFpkaisiDay = getItemRow(processData.getItemList(), GXHDO102B035Const.FPKAISHI_DAY); // F/P開始日
        FXHDD01 itemFpkaisiTime = getItemRow(processData.getItemList(), GXHDO102B035Const.FPKAISHI_TIME); // F/P開始時間

        // 「F/P開始日」ﾁｪｯｸ
        if (itemFpkaisiDay != null && StringUtil.isEmpty(itemFpkaisiDay.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemFpkaisiDay);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, true, errFxhdd01List, itemFpkaisiDay.getLabel1());
        }
        // 「F/P開始時間」ﾁｪｯｸ
        if (itemFpkaisiTime != null && StringUtil.isEmpty(itemFpkaisiTime.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemFpkaisiTime);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, true, errFxhdd01List, itemFpkaisiTime.getLabel1());
        }

        FXHDD01 itemFpteisiDay = getItemRow(processData.getItemList(), GXHDO102B035Const.FILTERKOUKAN1FPTEISHI_DAY); // F/P停止日
        FXHDD01 itemFpteisiTime = getItemRow(processData.getItemList(), GXHDO102B035Const.FILTERKOUKAN1FPTEISHI_TIME); // F/P停止時間
        FXHDD01 itemFpsaikaiDay = getItemRow(processData.getItemList(), GXHDO102B035Const.FILTERKOUKAN1FPSAIKAI_DAY); // F/P再開日
        FXHDD01 itemFpsaikaiTime = getItemRow(processData.getItemList(), GXHDO102B035Const.FILTERKOUKAN1FPSAIKAI_TIME); // F/P再開時間
        if (itemFpteisiDay == null || itemFpteisiTime == null || itemFpsaikaiDay == null || itemFpsaikaiTime == null) {
            return null;
        }
        if (StringUtil.isEmpty(itemFpteisiDay.getValue()) && StringUtil.isEmpty(itemFpteisiTime.getValue()) && StringUtil.isEmpty(itemFpsaikaiDay.getValue())
                && StringUtil.isEmpty(itemFpsaikaiTime.getValue())) {
            return null;
        }
        List<FXHDD01> itemList = Arrays.asList(itemFpteisiDay, itemFpteisiTime, itemFpsaikaiDay, itemFpsaikaiTime);
        ArrayList<FXHDD01> errorItemList = new ArrayList<>();
        // 「F/P停止日、F/P停止時間、F/P再開日、F/P再開時間」がすべて入力されているかﾁｪｯｸ
        itemList.stream().filter((item) -> (StringUtil.isEmpty(item.getValue()))).forEachOrdered((item) -> {
            errorItemList.add(item);
        });
        if (!errorItemList.isEmpty()) {
            StringBuilder errorMessageParams = new StringBuilder();
            List<FXHDD01> errorFxhdd01List = new ArrayList<>();
            errorItemList.stream().map((item) -> {
                // エラー情報の追加
                if (!StringUtil.isEmpty(errorMessageParams.toString())) {
                    // 追加された項目が既に存在している場合、エラーメッセージに分割文字「,」を追加
                    errorMessageParams.append(",");
                }
                errorMessageParams.append(item.getLabel1());
                return item;
            }).forEachOrdered((item) -> {
                errorFxhdd01List.add(item);
            });
            if (!errorFxhdd01List.isEmpty()) {
                return MessageUtil.getErrorMessageInfo("XHD-000037", true, true, errorFxhdd01List, errorMessageParams.toString());
            }
        }
        return null;
    }

    /**
     * 「F/P時間」計算処理
     *
     * @param processData 処理制御データ
     */
    private void setFpjikanItem(ProcessData processData) {
        FXHDD01 itemFpjikan = getItemRow(processData.getItemList(), GXHDO102B035Const.FPZIKAN); // F/P時間
        FXHDD01 itemFpteisiDay = getItemRow(processData.getItemList(), GXHDO102B035Const.FILTERKOUKAN1FPTEISHI_DAY); // F/P停止日
        FXHDD01 itemFpteisiTime = getItemRow(processData.getItemList(), GXHDO102B035Const.FILTERKOUKAN1FPTEISHI_TIME); // F/P停止時間
        FXHDD01 itemFpsaikaiDay = getItemRow(processData.getItemList(), GXHDO102B035Const.FILTERKOUKAN1FPSAIKAI_DAY); // F/P再開日
        FXHDD01 itemFpsaikaiTime = getItemRow(processData.getItemList(), GXHDO102B035Const.FILTERKOUKAN1FPSAIKAI_TIME); // F/P再開時間
        if (itemFpjikan == null) {
            return;
        }
        // Dateオブジェクト変換
        Date fpsyuuryouDate = getDateTimeItem(processData, GXHDO102B035Const.FPSYURYOU_DAY, GXHDO102B035Const.FPSYURYOU_TIME); // F/P終了日時
        Date fpkaisiDate = getDateTimeItem(processData, GXHDO102B035Const.FPKAISHI_DAY, GXHDO102B035Const.FPKAISHI_TIME); // F/P開始日時
        Date fpteisiDate = getDateTimeItem(processData, GXHDO102B035Const.FILTERKOUKAN1FPTEISHI_DAY, GXHDO102B035Const.FILTERKOUKAN1FPTEISHI_TIME); // F/P停止日時
        Date fpsaikaiDate = getDateTimeItem(processData, GXHDO102B035Const.FILTERKOUKAN1FPSAIKAI_DAY, GXHDO102B035Const.FILTERKOUKAN1FPSAIKAI_TIME); // F/P再開日時
        itemFpjikan.setValue("");
        if (StringUtil.isEmpty(itemFpteisiDay.getValue()) && StringUtil.isEmpty(itemFpteisiTime.getValue()) && StringUtil.isEmpty(itemFpsaikaiDay.getValue())
                && StringUtil.isEmpty(itemFpsaikaiTime.getValue())) {
            if (fpsyuuryouDate == null || fpkaisiDate == null) {
                return;
            }
            // 「F/P終了日+F/P終了時間」 - 「F/P開始日+F/P開始時間」(　時間　分)
            BigDecimal diffMinutes = BigDecimal.valueOf(DateUtil.diffMinutes(fpkaisiDate, fpsyuuryouDate));
            itemFpjikan.setValue(diffMinutes.toPlainString());
        } else {
            if (fpsyuuryouDate == null || fpkaisiDate == null || fpteisiDate == null || fpsaikaiDate == null) {
                return;
            }
            // (「F/P終了日+F/P終了時間」 - 「F/P開始日+F/P開始時間」) - (「F/P再開日+F/P再開時間」 - 「F/P停止日+F/P停止時間」)(　時間　分)
            BigDecimal diffMinutes = BigDecimal.valueOf(DateUtil.diffMinutes(fpkaisiDate, fpsyuuryouDate)).subtract(BigDecimal.valueOf(DateUtil.diffMinutes(fpteisiDate, fpsaikaiDate)));
            itemFpjikan.setValue(diffMinutes.toPlainString());
        }
    }

    /**
     * 日付文字列⇒Dateオブジェクト変換
     *
     * @param itemDay 項目日付(日)
     * @param itemTime 項目日付(時間)
     * @return 変換後のデータ
     */
    private Date getDateTimeItem(ProcessData processData, String dayItemId, String timeItemId) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), dayItemId); // F/P終了日
        FXHDD01 itemTime = getItemRow(processData.getItemList(), timeItemId); // F/P終了時間
        if (itemDay == null || itemTime == null) {
            return null;
        }
        // Dateオブジェクト変換
        Date dateVal = DateUtil.convertStringToDate(itemDay.getValue(), itemTime.getValue()); // F/P終了日時
        return dateVal;
    }

    /**
     * 正味重量①処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setSyoumijyuuryou1(ProcessData processData) {
        // 総重量
        FXHDD01 soujyuuryou1 = getItemRow(processData.getItemList(), GXHDO102B035Const.SOUJYURYOU1);
        // 風袋重量
        FXHDD01 hutaijyuuryou1 = getItemRow(processData.getItemList(), GXHDO102B035Const.HUTAIJYUURYOU1);
        // 正味重量
        FXHDD01 syoumijyuuryou1 = getItemRow(processData.getItemList(), GXHDO102B035Const.SYOUMIJYUURYOU1);
        ErrorMessageInfo checkItemErrorInfo = checkSyoumijyuuryou(processData, soujyuuryou1, hutaijyuuryou1);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        calcSyoumijyuuryou(processData, soujyuuryou1, hutaijyuuryou1, syoumijyuuryou1);
        return processData;
    }

    /**
     * 正味重量②処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setSyoumijyuuryou2(ProcessData processData) {
        // 総重量
        FXHDD01 soujyuuryou2 = getItemRow(processData.getItemList(), GXHDO102B035Const.SOUJYURYOU2);
        // 風袋重量
        FXHDD01 hutaijyuuryou2 = getItemRow(processData.getItemList(), GXHDO102B035Const.HUTAIJYUURYOU2);
        // 正味重量
        FXHDD01 syoumijyuuryou2 = getItemRow(processData.getItemList(), GXHDO102B035Const.SYOUMIJYUURYOU2);
        ErrorMessageInfo checkItemErrorInfo = checkSyoumijyuuryou(processData, soujyuuryou2, hutaijyuuryou2);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        calcSyoumijyuuryou(processData, soujyuuryou2, hutaijyuuryou2, syoumijyuuryou2);
        return processData;
    }

    /**
     * 正味重量③処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setSyoumijyuuryou3(ProcessData processData) {
        // 総重量
        FXHDD01 soujyuuryou3 = getItemRow(processData.getItemList(), GXHDO102B035Const.SOUJYURYOU3);
        // 風袋重量
        FXHDD01 hutaijyuuryou3 = getItemRow(processData.getItemList(), GXHDO102B035Const.HUTAIJYUURYOU3);
        // 正味重量
        FXHDD01 syoumijyuuryou3 = getItemRow(processData.getItemList(), GXHDO102B035Const.SYOUMIJYUURYOU3);
        ErrorMessageInfo checkItemErrorInfo = checkSyoumijyuuryou(processData, soujyuuryou3, hutaijyuuryou3);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        calcSyoumijyuuryou(processData, soujyuuryou3, hutaijyuuryou3, syoumijyuuryou3);
        return processData;
    }

    /**
     * 正味重量④処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setSyoumijyuuryou4(ProcessData processData) {
        // 総重量
        FXHDD01 soujyuuryou4 = getItemRow(processData.getItemList(), GXHDO102B035Const.SOUJYURYOU4);
        // 風袋重量
        FXHDD01 hutaijyuuryou4 = getItemRow(processData.getItemList(), GXHDO102B035Const.HUTAIJYUURYOU4);
        // 正味重量
        FXHDD01 syoumijyuuryou4 = getItemRow(processData.getItemList(), GXHDO102B035Const.SYOUMIJYUURYOU4);
        ErrorMessageInfo checkItemErrorInfo = checkSyoumijyuuryou(processData, soujyuuryou4, hutaijyuuryou4);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        calcSyoumijyuuryou(processData, soujyuuryou4, hutaijyuuryou4, syoumijyuuryou4);
        return processData;
    }

    /**
     * 正味重量⑤処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setSyoumijyuuryou5(ProcessData processData) {
        // 総重量
        FXHDD01 soujyuuryou5 = getItemRow(processData.getItemList(), GXHDO102B035Const.SOUJYURYOU5);
        // 風袋重量
        FXHDD01 hutaijyuuryou5 = getItemRow(processData.getItemList(), GXHDO102B035Const.HUTAIJYUURYOU5);
        // 正味重量
        FXHDD01 syoumijyuuryou5 = getItemRow(processData.getItemList(), GXHDO102B035Const.SYOUMIJYUURYOU5);
        ErrorMessageInfo checkItemErrorInfo = checkSyoumijyuuryou(processData, soujyuuryou5, hutaijyuuryou5);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        calcSyoumijyuuryou(processData, soujyuuryou5, hutaijyuuryou5, syoumijyuuryou5);
        return processData;
    }

    /**
     * 正味重量⑥処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setSyoumijyuuryou6(ProcessData processData) {
        // 総重量
        FXHDD01 soujyuuryou6 = getItemRow(processData.getItemList(), GXHDO102B035Const.SOUJYURYOU6);
        // 風袋重量
        FXHDD01 hutaijyuuryou6 = getItemRow(processData.getItemList(), GXHDO102B035Const.HUTAIJYUURYOU6);
        // 正味重量
        FXHDD01 syoumijyuuryou6 = getItemRow(processData.getItemList(), GXHDO102B035Const.SYOUMIJYUURYOU6);
        ErrorMessageInfo checkItemErrorInfo = checkSyoumijyuuryou(processData, soujyuuryou6, hutaijyuuryou6);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        calcSyoumijyuuryou(processData, soujyuuryou6, hutaijyuuryou6, syoumijyuuryou6);
        return processData;
    }

    /**
     * 正味重量ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
     *
     * @param processData 処理制御データ
     * @param soujyuuryou 総重量
     * @param hutaijyuuryou 風袋重量
     * @return エラーメッセージ情報
     */
    public ErrorMessageInfo checkSyoumijyuuryou(ProcessData processData, FXHDD01 soujyuuryou, FXHDD01 hutaijyuuryou) {

        //「総重量」ﾁｪｯｸ
        if (soujyuuryou != null && StringUtil.isEmpty(soujyuuryou.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(soujyuuryou);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, true, errFxhdd01List, soujyuuryou.getLabel1());
        }
        //「風袋重量」ﾁｪｯｸ
        if (hutaijyuuryou != null && StringUtil.isEmpty(hutaijyuuryou.getValue())) {
            List<FXHDD01> errFxhdd01List = Arrays.asList(hutaijyuuryou);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, true, errFxhdd01List, hutaijyuuryou.getLabel1());
        }
        if (soujyuuryou != null && hutaijyuuryou != null) {
            // 総重量<風袋重量の場合
            BigDecimal soujyuuryouVal = new BigDecimal(soujyuuryou.getValue());
            BigDecimal hutaijyuuryouVal = new BigDecimal(hutaijyuuryou.getValue());
            if (soujyuuryouVal.compareTo(hutaijyuuryouVal) == -1) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                List<FXHDD01> errFxhdd01List = Arrays.asList(soujyuuryou, hutaijyuuryou);
                return MessageUtil.getErrorMessageInfo("XHD-000023", true, true, errFxhdd01List, soujyuuryou.getLabel1(), hutaijyuuryou.getLabel1());
            }
        }
        return null;
    }

    /**
     * 正味重量計算
     *
     * @param processData 処理制御データ
     * @param soujyuryouItem 総重量
     * @param hutaijyuuryouItem 風袋重量
     * @param itemSyoumijyuuryou 正味重量
     */
    private void calcSyoumijyuuryou(ProcessData processData, FXHDD01 soujyuryouItem, FXHDD01 hutaijyuuryouItem, FXHDD01 itemSyoumijyuuryou) {

        try {

            FXHDD01 itemSyoumijyuuryou1 = getItemRow(processData.getItemList(), GXHDO102B035Const.SYOUMIJYUURYOU1);// 正味重量①
            FXHDD01 itemSyoumijyuuryou2 = getItemRow(processData.getItemList(), GXHDO102B035Const.SYOUMIJYUURYOU2);// 正味重量②
            FXHDD01 itemSyoumijyuuryou3 = getItemRow(processData.getItemList(), GXHDO102B035Const.SYOUMIJYUURYOU3);// 正味重量③
            FXHDD01 itemSyoumijyuuryou4 = getItemRow(processData.getItemList(), GXHDO102B035Const.SYOUMIJYUURYOU4);// 正味重量④
            FXHDD01 itemSyoumijyuuryou5 = getItemRow(processData.getItemList(), GXHDO102B035Const.SYOUMIJYUURYOU5);// 正味重量⑤
            FXHDD01 itemSyoumijyuuryou6 = getItemRow(processData.getItemList(), GXHDO102B035Const.SYOUMIJYUURYOU6);// 正味重量⑥
            FXHDD01 itemSlipjyuuryougoukei = getItemRow(processData.getItemList(), GXHDO102B035Const.SLIPJYUURYOUGOUKEI);// ｽﾘｯﾌﾟ重量合計
            if (soujyuryouItem != null && hutaijyuuryouItem != null && itemSyoumijyuuryou != null) {
                //「正味重量」= 「総重量」 - 「風袋重量」 を算出する。
                BigDecimal itemSoujyuryouVal = new BigDecimal(soujyuryouItem.getValue());
                BigDecimal itemHutaijyuuryouVal = new BigDecimal(hutaijyuuryouItem.getValue());
                BigDecimal itemSyoumijyuuryouVal = itemSoujyuryouVal.subtract(itemHutaijyuuryouVal);
                //計算結果の設定
                itemSyoumijyuuryou.setValue(itemSyoumijyuuryouVal.toPlainString());
            }
            BigDecimal itemSyoumijyuuryou1Val = (BigDecimal) DBUtil.stringToBigDecimalObject(itemSyoumijyuuryou1.getValue()); // 正味重量①
            BigDecimal itemSyoumijyuuryou2Val = (BigDecimal) DBUtil.stringToBigDecimalObject(itemSyoumijyuuryou2.getValue()); // 正味重量②
            BigDecimal itemSyoumijyuuryou3Val = (BigDecimal) DBUtil.stringToBigDecimalObject(itemSyoumijyuuryou3.getValue()); // 正味重量③
            BigDecimal itemSyoumijyuuryou4Val = (BigDecimal) DBUtil.stringToBigDecimalObject(itemSyoumijyuuryou4.getValue()); // 正味重量④
            BigDecimal itemSyoumijyuuryou5Val = (BigDecimal) DBUtil.stringToBigDecimalObject(itemSyoumijyuuryou5.getValue()); // 正味重量⑤
            BigDecimal itemSyoumijyuuryou6Val = (BigDecimal) DBUtil.stringToBigDecimalObject(itemSyoumijyuuryou6.getValue()); // 正味重量⑥
            // 「ｽﾘｯﾌﾟ重量合計」計算処理:「正味重量①」 ～ 「正味重量⑥」 の和を算出する。
            if (itemSlipjyuuryougoukei != null) {
                BigDecimal itemYuudentaislurryjyuurougoukeiVal = itemSyoumijyuuryou1Val.add(itemSyoumijyuuryou2Val).add(itemSyoumijyuuryou3Val).add(
                        itemSyoumijyuuryou4Val).add(itemSyoumijyuuryou5Val).add(itemSyoumijyuuryou6Val);
                itemSlipjyuuryougoukei.setValue(itemYuudentaislurryjyuurougoukeiVal.toPlainString());
            }
        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
            ErrUtil.outputErrorLog(itemSyoumijyuuryou.getLabel1() + "にエラー発生", ex, LOGGER);
        }
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

                // ｽﾘｯﾌﾟ作製・FP(バケツ)_仮登録登録処理
                insertTmpSrSlipFpBaketsu(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, jissekiNo, strSystime, processData);
            } else {

                // ｽﾘｯﾌﾟ作製・FP(バケツ)_仮登録更新処理
                updateTmpSrSlipFpBaketsu(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, jissekiNo, strSystime, processData);
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
            SrSlipFpBaketsu tmpSrSlipFpBaketsu = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrSlipFpBaketsu> srSlipFpBaketsuList = getSrSlipFpBaketsuData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo9, edaban, jissekiNo);
                if (!srSlipFpBaketsuList.isEmpty()) {
                    tmpSrSlipFpBaketsu = srSlipFpBaketsuList.get(0);
                }

                deleteTmpSrSlipFpBaketsu(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban, jissekiNo);
            }

            // ｽﾘｯﾌﾟ作製・FP(バケツ)_登録処理
            insertSrSlipFpBaketsu(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, jissekiNo, strSystime, processData, tmpSrSlipFpBaketsu);

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
        processData.setUserAuthParam(GXHDO102B035Const.USER_AUTH_UPDATE_PARAM);

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

            // ｽﾘｯﾌﾟ作製・FP(バケツ)_更新処理
            updateSrSlipFpBaketsu(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, jissekiNo, strSystime, processData);

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
        FXHDD01 itemFpkaishiDay = getItemRow(processData.getItemList(), GXHDO102B035Const.FPKAISHI_DAY);  // F/P開始日
        FXHDD01 itemFpkaishiTime = getItemRow(processData.getItemList(), GXHDO102B035Const.FPKAISHI_TIME); // F/P開始時間
        FXHDD01 itemFpsyuryouDay = getItemRow(processData.getItemList(), GXHDO102B035Const.FPSYURYOU_DAY); // F/P終了日
        FXHDD01 itemFpsyuryouTime = getItemRow(processData.getItemList(), GXHDO102B035Const.FPSYURYOU_TIME); // F/P終了時間
        if (itemFpkaishiDay != null && itemFpkaishiTime != null && itemFpsyuryouDay != null && itemFpsyuryouTime != null) {
            // 画面.F/P開始日 + 画面.F/P開始時間
            Date kakuhankaisiDate = DateUtil.convertStringToDate(itemFpkaishiDay.getValue(), itemFpkaishiTime.getValue());
            // 画面.F/P終了日 + 画面.F/P終了時間
            Date kakuhansyuuryouDate = DateUtil.convertStringToDate(itemFpsyuryouDay.getValue(), itemFpsyuryouTime.getValue());
            // R001チェック呼出し
            String msgKakuhankaisiCheckR001 = validateUtil.checkR001("F/P開始日時", kakuhankaisiDate, "F/P終了日時", kakuhansyuuryouDate);
            if (!StringUtil.isEmpty(msgKakuhankaisiCheckR001)) {
                // エラー発生時
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemFpkaishiDay, itemFpkaishiTime, itemFpsyuryouDay, itemFpsyuryouTime);
                return MessageUtil.getErrorMessageInfo("", msgKakuhankaisiCheckR001, true, true, errFxhdd01List);
            }
        }

        FXHDD01 itemFpteishiDay = getItemRow(processData.getItemList(), GXHDO102B035Const.FILTERKOUKAN1FPTEISHI_DAY);  // F/P停止日
        FXHDD01 itemFpteishiTime = getItemRow(processData.getItemList(), GXHDO102B035Const.FILTERKOUKAN1FPTEISHI_TIME); // F/P停止時間
        FXHDD01 itemFpsaikaiDay = getItemRow(processData.getItemList(), GXHDO102B035Const.FILTERKOUKAN1FPSAIKAI_DAY);    // F/P再開日
        FXHDD01 itemFpsaikaiTime = getItemRow(processData.getItemList(), GXHDO102B035Const.FILTERKOUKAN1FPSAIKAI_TIME); // F/P再開時間
        if (itemFpteishiDay != null && itemFpteishiTime != null && itemFpsaikaiDay != null && itemFpsaikaiTime != null) {
            // 画面.F/P停止日 + 画面. F/P停止時間
            Date kansoukaisi1Date = DateUtil.convertStringToDate(itemFpteishiDay.getValue(), itemFpteishiTime.getValue());
            // 画面.F/P再開日 + 画面.F/P再開時間
            Date kansousyuuryou1Date = DateUtil.convertStringToDate(itemFpsaikaiDay.getValue(), itemFpsaikaiTime.getValue());
            // R001チェック呼出し
            String msgKansoukaisi1CheckR001 = validateUtil.checkR001("F/P停止日時", kansoukaisi1Date, "F/P再開日時", kansousyuuryou1Date);
            if (!StringUtil.isEmpty(msgKansoukaisi1CheckR001)) {
                // エラー発生時
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemFpteishiDay, itemFpteishiTime, itemFpsaikaiDay, itemFpsaikaiTime);
                return MessageUtil.getErrorMessageInfo("", msgKansoukaisi1CheckR001, true, true, errFxhdd01List);
            }
        }

        FXHDD01 itemAssoutankno = getItemRow(processData.getItemList(), GXHDO102B035Const.ASSOUTANKNO); // 圧送ﾀﾝｸ№
        FXHDD01 itemAssouregulatorno = getItemRow(processData.getItemList(), GXHDO102B035Const.ASSOUREGULATORNO); // 圧送ﾚｷﾞｭﾚｰﾀｰ№
        // ﾁｪｯｸﾎﾞｯｸｽ、圧送ﾀﾝｸ№、圧送ﾚｷﾞｭﾚｰﾀｰ№のﾁｪｯｸ処理
        return checkItemInfo(processData, itemAssoutankno, itemAssouregulatorno);
    }

    /**
     * ﾁｪｯｸﾎﾞｯｸｽ、圧送ﾀﾝｸ№、圧送ﾚｷﾞｭﾚｰﾀｰ№のﾁｪｯｸ処理
     *
     * @param processData 処理データ
     * @param itemAssoutankno 圧送ﾀﾝｸ№
     * @param itemAssouregulatorno 圧送ﾚｷﾞｭﾚｰﾀｰ№
     *
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkItemInfo(ProcessData processData, FXHDD01 itemAssoutankno, FXHDD01 itemAssouregulatorno) {

        // ﾁｪｯｸﾎﾞｯｸｽがすべてﾁｪｯｸされているかﾁｪｯｸ：排出容器の内袋、保存用ｻﾝﾌﾟﾙ回収
        List<String> itemIdList = Arrays.asList(GXHDO102B035Const.HAISYUTSUYOUKI, GXHDO102B035Const.HOZONYOUSAMPLEKAISYUU);
        for (String itemId : itemIdList) {
            FXHDD01 itemFxhdd01 = getItemRow(processData.getItemList(), itemId);
            if (!hasCheck(itemFxhdd01)) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemFxhdd01);
                return MessageUtil.getErrorMessageInfo("XHD-000199", true, true, errFxhdd01List, itemFxhdd01.getLabel1());
            }
        }

        // 「圧送ﾀﾝｸ№」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemAssoutankno.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemAssoutankno);
            return MessageUtil.getErrorMessageInfo("XHD-000003", true, true, errFxhdd01List, itemAssoutankno.getLabel1());
        }

        // 「圧送ﾚｷﾞｭﾚｰﾀｰ№」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemAssouregulatorno.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemAssouregulatorno);
            return MessageUtil.getErrorMessageInfo("XHD-000003", true, true, errFxhdd01List, itemAssouregulatorno.getLabel1());
        }
        return null;
    }

    /**
     * ﾁｪｯｸﾎﾞｯｸｽがﾁｪｯｸされているかﾁｪｯｸ。
     *
     * @param itemFxhdd01 項目データ
     * @return チェック結果(true:エラーなし、false:エラー有り)
     */
    private boolean hasCheck(FXHDD01 itemFxhdd01) {
        if (itemFxhdd01 == null) {
            return true;
        }
        return "true".equals(StringUtil.nullToBlank(itemFxhdd01.getValue()).toLowerCase());
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
        processData.setUserAuthParam(GXHDO102B035Const.USER_AUTH_DELETE_PARAM);

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

            // ｽﾘｯﾌﾟ作製・FP(バケツ)_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo9, edaban, paramJissekino);
            insertDeleteDataTmpSrSlipFpBaketsu(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo9, edaban, paramJissekino, strSystime);

            // ｽﾘｯﾌﾟ作製・FP(バケツ)_削除処理
            deleteSrSlipFpBaketsu(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban, paramJissekino);

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
                        GXHDO102B035Const.BTN_UPDATE_TOP,
                        GXHDO102B035Const.BTN_DELETE_TOP,
                        GXHDO102B035Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B035Const.BTN_FPKAISHI_TOP,
                        GXHDO102B035Const.BTN_FILTERKOUKAN1FPTEISHI_TOP,
                        GXHDO102B035Const.BTN_FILTERKOUKAN1FPSAIKAI_TOP,
                        GXHDO102B035Const.BTN_FPSYURYOU_TOP,
                        GXHDO102B035Const.BTN_SYOUMIJYUURYOU1_TOP,
                        GXHDO102B035Const.BTN_SYOUMIJYUURYOU2_TOP,
                        GXHDO102B035Const.BTN_SYOUMIJYUURYOU3_TOP,
                        GXHDO102B035Const.BTN_SYOUMIJYUURYOU4_TOP,
                        GXHDO102B035Const.BTN_SYOUMIJYUURYOU5_TOP,
                        GXHDO102B035Const.BTN_SYOUMIJYUURYOU6_TOP,
                        GXHDO102B035Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B035Const.BTN_DELETE_BOTTOM,
                        GXHDO102B035Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B035Const.BTN_FPKAISHI_BOTTOM,
                        GXHDO102B035Const.BTN_FILTERKOUKAN1FPTEISHI_BOTTOM,
                        GXHDO102B035Const.BTN_FILTERKOUKAN1FPSAIKAI_BOTTOM,
                        GXHDO102B035Const.BTN_FPSYURYOU_BOTTOM,
                        GXHDO102B035Const.BTN_SYOUMIJYUURYOU1_BOTTOM,
                        GXHDO102B035Const.BTN_SYOUMIJYUURYOU2_BOTTOM,
                        GXHDO102B035Const.BTN_SYOUMIJYUURYOU3_BOTTOM,
                        GXHDO102B035Const.BTN_SYOUMIJYUURYOU4_BOTTOM,
                        GXHDO102B035Const.BTN_SYOUMIJYUURYOU5_BOTTOM,
                        GXHDO102B035Const.BTN_SYOUMIJYUURYOU6_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B035Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B035Const.BTN_INSERT_TOP,
                        GXHDO102B035Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B035Const.BTN_INSERT_BOTTOM));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO102B035Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B035Const.BTN_INSERT_TOP,
                        GXHDO102B035Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B035Const.BTN_FPKAISHI_TOP,
                        GXHDO102B035Const.BTN_FILTERKOUKAN1FPTEISHI_TOP,
                        GXHDO102B035Const.BTN_FILTERKOUKAN1FPSAIKAI_TOP,
                        GXHDO102B035Const.BTN_FPSYURYOU_TOP,
                        GXHDO102B035Const.BTN_SYOUMIJYUURYOU1_TOP,
                        GXHDO102B035Const.BTN_SYOUMIJYUURYOU2_TOP,
                        GXHDO102B035Const.BTN_SYOUMIJYUURYOU3_TOP,
                        GXHDO102B035Const.BTN_SYOUMIJYUURYOU4_TOP,
                        GXHDO102B035Const.BTN_SYOUMIJYUURYOU5_TOP,
                        GXHDO102B035Const.BTN_SYOUMIJYUURYOU6_TOP,
                        GXHDO102B035Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B035Const.BTN_INSERT_BOTTOM,
                        GXHDO102B035Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B035Const.BTN_FPKAISHI_BOTTOM,
                        GXHDO102B035Const.BTN_FILTERKOUKAN1FPTEISHI_BOTTOM,
                        GXHDO102B035Const.BTN_FILTERKOUKAN1FPSAIKAI_BOTTOM,
                        GXHDO102B035Const.BTN_FPSYURYOU_BOTTOM,
                        GXHDO102B035Const.BTN_SYOUMIJYUURYOU1_BOTTOM,
                        GXHDO102B035Const.BTN_SYOUMIJYUURYOU2_BOTTOM,
                        GXHDO102B035Const.BTN_SYOUMIJYUURYOU3_BOTTOM,
                        GXHDO102B035Const.BTN_SYOUMIJYUURYOU4_BOTTOM,
                        GXHDO102B035Const.BTN_SYOUMIJYUURYOU5_BOTTOM,
                        GXHDO102B035Const.BTN_SYOUMIJYUURYOU6_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B035Const.BTN_UPDATE_TOP,
                        GXHDO102B035Const.BTN_DELETE_TOP,
                        GXHDO102B035Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B035Const.BTN_DELETE_BOTTOM
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
        this.setItemData(processData, GXHDO102B035Const.WIPLOTNO, lotNo);
        // ｽﾘｯﾌﾟ品名
        this.setItemData(processData, GXHDO102B035Const.SLIPHINMEI, StringUtil.nullToBlank(getMapData(shikakariData, "hinmei")));
        // ｽﾘｯﾌﾟLotNo
        this.setItemData(processData, GXHDO102B035Const.SLIPLOTNO, StringUtil.nullToBlank(getMapData(shikakariData, "lotno")));
        // ﾛｯﾄ区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode"));
        // ﾛｯﾄ区分名
        String lotkubun = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubun"));
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO102B035Const.LOTKUBUN, "");
        } else {
            if (!StringUtil.isEmpty(lotkubun)) {
                lotkubuncode = lotkubuncode + ":" + lotkubun;
            }
            this.setItemData(processData, GXHDO102B035Const.LOTKUBUN, lotkubuncode);
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

        List<SrSlipFpBaketsu> srSlipFpBaketsuList = new ArrayList<>();
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

            // ｽﾘｯﾌﾟ作製・FP(バケツ)データ取得
            srSlipFpBaketsuList = getSrSlipFpBaketsuData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo9, edaban, jissekino);
            if (srSlipFpBaketsuList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srSlipFpBaketsuList.isEmpty()) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srSlipFpBaketsuList.get(0));

        return true;

    }

    /**
     * データ設定処理
     *
     * @param processData 処理制御データ
     * @param srSlipFpBaketsu ｽﾘｯﾌﾟ作製・FP(バケツ)データ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrSlipFpBaketsu srSlipFpBaketsu) {

        // 排出容器の内袋
        this.setItemData(processData, GXHDO102B035Const.HAISYUTSUYOUKI, getSrSlipFpBaketsuItemData(GXHDO102B035Const.HAISYUTSUYOUKI, srSlipFpBaketsu));

        // 風袋重量①
        this.setItemData(processData, GXHDO102B035Const.HUTAIJYUURYOU1, getSrSlipFpBaketsuItemData(GXHDO102B035Const.HUTAIJYUURYOU1, srSlipFpBaketsu));

        // 風袋重量②
        this.setItemData(processData, GXHDO102B035Const.HUTAIJYUURYOU2, getSrSlipFpBaketsuItemData(GXHDO102B035Const.HUTAIJYUURYOU2, srSlipFpBaketsu));

        // 風袋重量③
        this.setItemData(processData, GXHDO102B035Const.HUTAIJYUURYOU3, getSrSlipFpBaketsuItemData(GXHDO102B035Const.HUTAIJYUURYOU3, srSlipFpBaketsu));

        // 風袋重量④
        this.setItemData(processData, GXHDO102B035Const.HUTAIJYUURYOU4, getSrSlipFpBaketsuItemData(GXHDO102B035Const.HUTAIJYUURYOU4, srSlipFpBaketsu));

        // 風袋重量⑤
        this.setItemData(processData, GXHDO102B035Const.HUTAIJYUURYOU5, getSrSlipFpBaketsuItemData(GXHDO102B035Const.HUTAIJYUURYOU5, srSlipFpBaketsu));

        // 風袋重量⑥
        this.setItemData(processData, GXHDO102B035Const.HUTAIJYUURYOU6, getSrSlipFpBaketsuItemData(GXHDO102B035Const.HUTAIJYUURYOU6, srSlipFpBaketsu));

        // 保管容器準備_担当者
        this.setItemData(processData, GXHDO102B035Const.HOKANYOUKIJYUNBITANTOUSYA, getSrSlipFpBaketsuItemData(GXHDO102B035Const.HOKANYOUKIJYUNBITANTOUSYA, srSlipFpBaketsu));

        // ﾌｨﾙﾀｰ取り付け_LotNo1
        this.setItemData(processData, GXHDO102B035Const.FILTERTORITSUKEFILTERLOTNO1, getSrSlipFpBaketsuItemData(GXHDO102B035Const.FILTERTORITSUKEFILTERLOTNO1, srSlipFpBaketsu));

        // ﾌｨﾙﾀｰ取り付け_取り付け本数1
        this.setItemData(processData, GXHDO102B035Const.FILTERTORITSUKETORITSUKEHONSUU1, getSrSlipFpBaketsuItemData(GXHDO102B035Const.FILTERTORITSUKETORITSUKEHONSUU1, srSlipFpBaketsu));

        // ﾌｨﾙﾀｰ取り付け_LotNo2
        this.setItemData(processData, GXHDO102B035Const.FILTERTORITSUKEFILTERLOTNO2, getSrSlipFpBaketsuItemData(GXHDO102B035Const.FILTERTORITSUKEFILTERLOTNO2, srSlipFpBaketsu));

        // ﾌｨﾙﾀｰ取り付け_取り付け本数2
        this.setItemData(processData, GXHDO102B035Const.FILTERTORITSUKETORITSUKEHONSUU2, getSrSlipFpBaketsuItemData(GXHDO102B035Const.FILTERTORITSUKETORITSUKEHONSUU2, srSlipFpBaketsu));

        // ﾌｨﾙﾀｰ取り付け_担当者
        this.setItemData(processData, GXHDO102B035Const.FILTERTORITSUKETANTOUSYA, getSrSlipFpBaketsuItemData(GXHDO102B035Const.FILTERTORITSUKETANTOUSYA, srSlipFpBaketsu));

        // 圧送ﾀﾝｸ№
        this.setItemData(processData, GXHDO102B035Const.ASSOUTANKNO, getSrSlipFpBaketsuItemData(GXHDO102B035Const.ASSOUTANKNO, srSlipFpBaketsu));

        // F/P開始日
        this.setItemData(processData, GXHDO102B035Const.FPKAISHI_DAY, getSrSlipFpBaketsuItemData(GXHDO102B035Const.FPKAISHI_DAY, srSlipFpBaketsu));

        // F/P開始時間
        this.setItemData(processData, GXHDO102B035Const.FPKAISHI_TIME, getSrSlipFpBaketsuItemData(GXHDO102B035Const.FPKAISHI_TIME, srSlipFpBaketsu));

        // 圧送ﾚｷﾞｭﾚｰﾀｰNo
        this.setItemData(processData, GXHDO102B035Const.ASSOUREGULATORNO, getSrSlipFpBaketsuItemData(GXHDO102B035Const.ASSOUREGULATORNO, srSlipFpBaketsu));

        // 圧送圧力
        this.setItemData(processData, GXHDO102B035Const.ASSOUATSURYOKU, getSrSlipFpBaketsuItemData(GXHDO102B035Const.ASSOUATSURYOKU, srSlipFpBaketsu));

        // ﾌｨﾙﾀｰﾊﾟｽ開始_担当者
        this.setItemData(processData, GXHDO102B035Const.FILTERPASSKAISHITANTOUSYA, getSrSlipFpBaketsuItemData(GXHDO102B035Const.FILTERPASSKAISHITANTOUSYA, srSlipFpBaketsu));

        // 保存用ｻﾝﾌﾟﾙ回収
        this.setItemData(processData, GXHDO102B035Const.HOZONYOUSAMPLEKAISYUU, getSrSlipFpBaketsuItemData(GXHDO102B035Const.HOZONYOUSAMPLEKAISYUU, srSlipFpBaketsu));

        // ﾌｨﾙﾀｰ交換①_F/P停止日
        this.setItemData(processData, GXHDO102B035Const.FILTERKOUKAN1FPTEISHI_DAY, getSrSlipFpBaketsuItemData(GXHDO102B035Const.FILTERKOUKAN1FPTEISHI_DAY, srSlipFpBaketsu));

        // ﾌｨﾙﾀｰ交換①_F/P停止時間
        this.setItemData(processData, GXHDO102B035Const.FILTERKOUKAN1FPTEISHI_TIME, getSrSlipFpBaketsuItemData(GXHDO102B035Const.FILTERKOUKAN1FPTEISHI_TIME, srSlipFpBaketsu));

        // ﾌｨﾙﾀｰ交換①_LotNo1
        this.setItemData(processData, GXHDO102B035Const.FILTERKOUKAN1LOTNO1, getSrSlipFpBaketsuItemData(GXHDO102B035Const.FILTERKOUKAN1LOTNO1, srSlipFpBaketsu));

        // ﾌｨﾙﾀｰ交換①_取り付け本数1
        this.setItemData(processData, GXHDO102B035Const.FILTERKOUKAN1TORITSUKEHONNSUU1, getSrSlipFpBaketsuItemData(GXHDO102B035Const.FILTERKOUKAN1TORITSUKEHONNSUU1, srSlipFpBaketsu));

        // ﾌｨﾙﾀｰ交換①_LotNo2
        this.setItemData(processData, GXHDO102B035Const.FILTERKOUKAN1LOTNO2, getSrSlipFpBaketsuItemData(GXHDO102B035Const.FILTERKOUKAN1LOTNO2, srSlipFpBaketsu));

        // ﾌｨﾙﾀｰ交換①_取り付け本数2
        this.setItemData(processData, GXHDO102B035Const.FILTERKOUKAN1TORITSUKEHONNSUU2, getSrSlipFpBaketsuItemData(GXHDO102B035Const.FILTERKOUKAN1TORITSUKEHONNSUU2, srSlipFpBaketsu));

        // ﾌｨﾙﾀｰ交換①_LotNo3
        this.setItemData(processData, GXHDO102B035Const.FILTERKOUKAN1LOTNO3, getSrSlipFpBaketsuItemData(GXHDO102B035Const.FILTERKOUKAN1LOTNO3, srSlipFpBaketsu));

        // ﾌｨﾙﾀｰ交換①_取り付け本数3
        this.setItemData(processData, GXHDO102B035Const.FILTERKOUKAN1TORITSUKEHONNSUU3, getSrSlipFpBaketsuItemData(GXHDO102B035Const.FILTERKOUKAN1TORITSUKEHONNSUU3, srSlipFpBaketsu));

        // ﾌｨﾙﾀｰ交換①_F/P再開日
        this.setItemData(processData, GXHDO102B035Const.FILTERKOUKAN1FPSAIKAI_DAY, getSrSlipFpBaketsuItemData(GXHDO102B035Const.FILTERKOUKAN1FPSAIKAI_DAY, srSlipFpBaketsu));

        // ﾌｨﾙﾀｰ交換①_F/P再開時間
        this.setItemData(processData, GXHDO102B035Const.FILTERKOUKAN1FPSAIKAI_TIME, getSrSlipFpBaketsuItemData(GXHDO102B035Const.FILTERKOUKAN1FPSAIKAI_TIME, srSlipFpBaketsu));

        // ﾌｨﾙﾀｰ交換①_担当者
        this.setItemData(processData, GXHDO102B035Const.FILTERKOUKAN1TANTOUSYA, getSrSlipFpBaketsuItemData(GXHDO102B035Const.FILTERKOUKAN1TANTOUSYA, srSlipFpBaketsu));

        // F/P終了日
        this.setItemData(processData, GXHDO102B035Const.FPSYURYOU_DAY, getSrSlipFpBaketsuItemData(GXHDO102B035Const.FPSYURYOU_DAY, srSlipFpBaketsu));

        // F/P終了時間
        this.setItemData(processData, GXHDO102B035Const.FPSYURYOU_TIME, getSrSlipFpBaketsuItemData(GXHDO102B035Const.FPSYURYOU_TIME, srSlipFpBaketsu));

        // F/P時間
        this.setItemData(processData, GXHDO102B035Const.FPZIKAN, getSrSlipFpBaketsuItemData(GXHDO102B035Const.FPZIKAN, srSlipFpBaketsu));

        // ﾌｨﾙﾀｰﾊﾟｽ終了_担当者
        this.setItemData(processData, GXHDO102B035Const.FILTERPASSSYUURYOUTANTOUSYA, getSrSlipFpBaketsuItemData(GXHDO102B035Const.FILTERPASSSYUURYOUTANTOUSYA, srSlipFpBaketsu));

        // 総重量①
        this.setItemData(processData, GXHDO102B035Const.SOUJYURYOU1, getSrSlipFpBaketsuItemData(GXHDO102B035Const.SOUJYURYOU1, srSlipFpBaketsu));

        // 総重量②
        this.setItemData(processData, GXHDO102B035Const.SOUJYURYOU2, getSrSlipFpBaketsuItemData(GXHDO102B035Const.SOUJYURYOU2, srSlipFpBaketsu));

        // 総重量③
        this.setItemData(processData, GXHDO102B035Const.SOUJYURYOU3, getSrSlipFpBaketsuItemData(GXHDO102B035Const.SOUJYURYOU3, srSlipFpBaketsu));

        // 総重量④
        this.setItemData(processData, GXHDO102B035Const.SOUJYURYOU4, getSrSlipFpBaketsuItemData(GXHDO102B035Const.SOUJYURYOU4, srSlipFpBaketsu));

        // 総重量⑤
        this.setItemData(processData, GXHDO102B035Const.SOUJYURYOU5, getSrSlipFpBaketsuItemData(GXHDO102B035Const.SOUJYURYOU5, srSlipFpBaketsu));

        // 総重量⑥
        this.setItemData(processData, GXHDO102B035Const.SOUJYURYOU6, getSrSlipFpBaketsuItemData(GXHDO102B035Const.SOUJYURYOU6, srSlipFpBaketsu));

        // 正味重量①
        this.setItemData(processData, GXHDO102B035Const.SYOUMIJYUURYOU1, getSrSlipFpBaketsuItemData(GXHDO102B035Const.SYOUMIJYUURYOU1, srSlipFpBaketsu));

        // 正味重量②
        this.setItemData(processData, GXHDO102B035Const.SYOUMIJYUURYOU2, getSrSlipFpBaketsuItemData(GXHDO102B035Const.SYOUMIJYUURYOU2, srSlipFpBaketsu));

        // 正味重量③
        this.setItemData(processData, GXHDO102B035Const.SYOUMIJYUURYOU3, getSrSlipFpBaketsuItemData(GXHDO102B035Const.SYOUMIJYUURYOU3, srSlipFpBaketsu));

        // 正味重量④
        this.setItemData(processData, GXHDO102B035Const.SYOUMIJYUURYOU4, getSrSlipFpBaketsuItemData(GXHDO102B035Const.SYOUMIJYUURYOU4, srSlipFpBaketsu));

        // 正味重量⑤
        this.setItemData(processData, GXHDO102B035Const.SYOUMIJYUURYOU5, getSrSlipFpBaketsuItemData(GXHDO102B035Const.SYOUMIJYUURYOU5, srSlipFpBaketsu));

        // 正味重量⑥
        this.setItemData(processData, GXHDO102B035Const.SYOUMIJYUURYOU6, getSrSlipFpBaketsuItemData(GXHDO102B035Const.SYOUMIJYUURYOU6, srSlipFpBaketsu));

        // ｽﾘｯﾌﾟ重量合計
        this.setItemData(processData, GXHDO102B035Const.SLIPJYUURYOUGOUKEI, getSrSlipFpBaketsuItemData(GXHDO102B035Const.SLIPJYUURYOUGOUKEI, srSlipFpBaketsu));

        // 備考1
        this.setItemData(processData, GXHDO102B035Const.BIKOU1, getSrSlipFpBaketsuItemData(GXHDO102B035Const.BIKOU1, srSlipFpBaketsu));

        // 備考2
        this.setItemData(processData, GXHDO102B035Const.BIKOU2, getSrSlipFpBaketsuItemData(GXHDO102B035Const.BIKOU2, srSlipFpBaketsu));

    }

    /**
     * ｽﾘｯﾌﾟ作製・FP(バケツ)の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @param jissekino 実績No
     * @return ｽﾘｯﾌﾟ作製・FP(バケツ)登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrSlipFpBaketsu> getSrSlipFpBaketsuData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrSlipFpBaketsu(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
        } else {
            return loadTmpSrSlipFpBaketsu(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
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
     * [ｽﾘｯﾌﾟ作製・FP(バケツ)]から、ﾃﾞｰﾀを取得
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
    private List<SrSlipFpBaketsu> loadSrSlipFpBaketsu(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, String rev) throws SQLException {

        String sql = " SELECT "
                + " kojyo,lotno,edaban,jissekino,sliphinmei,sliplotno,lotkubun,genryoukigou,haisyutsuyouki,hutaijyuuryou1,hutaijyuuryou2,"
                + "hutaijyuuryou3,hutaijyuuryou4,hutaijyuuryou5,hutaijyuuryou6,hokanyoukijyunbitantousya,filterrenketsu,filtertoritsuke1filterhinmei,"
                + "filtertoritsukefilterlotno1,filtertoritsuketoritsukehonsuu1,filtertoritsuke2filterhinmei,filtertoritsukefilterlotno2,"
                + "filtertoritsuketoritsukehonsuu2,filtertoritsuketantousya,assoutankno,fpkaishinichiji,assouregulatorno,assouatsuryoku,"
                + "filterpasskaishitantousya,hozonyousamplekaisyuu,filterkoukan1fpteishinichiji,filterkoukan11filterhinmei,filterkoukan1lotno1,"
                + "filterkoukan1toritsukehonnsuu1,filterkoukan12filterhinmei,filterkoukan1lotno2,filterkoukan1toritsukehonnsuu2,filterkoukan13filterhinmei,"
                + "filterkoukan1lotno3,filterkoukan1toritsukehonnsuu3,filterkoukan1fpsaikainichiji,filterkoukan1tantousya,fpsyuryounichiji,"
                + "fpzikan,filterpasssyuuryoutantousya,soujyuryou1,soujyuryou2,soujyuryou3,soujyuryou4,soujyuryou5,soujyuryou6,syoumijyuuryou1,"
                + "syoumijyuuryou2,syoumijyuuryou3,syoumijyuuryou4,syoumijyuuryou5,syoumijyuuryou6,slipjyuuryougoukei,bikou1,bikou2,torokunichiji,"
                + "kosinnichiji,revision "
                + " FROM sr_slip_fp_baketsu "
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
        mapping.put("kojyo", "kojyo");                                                        // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                                        // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                                      // 枝番
        mapping.put("jissekino", "jissekino");                                                // 実績No
        mapping.put("sliphinmei", "sliphinmei");                                              // ｽﾘｯﾌﾟ品名
        mapping.put("sliplotno", "sliplotno");                                                // ｽﾘｯﾌﾟLotNo
        mapping.put("lotkubun", "lotkubun");                                                  // ﾛｯﾄ区分
        mapping.put("genryoukigou", "genryoukigou");                                          // 原料記号
        mapping.put("haisyutsuyouki", "haisyutsuyouki");                                      // 排出容器の内袋
        mapping.put("hutaijyuuryou1", "hutaijyuuryou1");                                      // 風袋重量①
        mapping.put("hutaijyuuryou2", "hutaijyuuryou2");                                      // 風袋重量②
        mapping.put("hutaijyuuryou3", "hutaijyuuryou3");                                      // 風袋重量③
        mapping.put("hutaijyuuryou4", "hutaijyuuryou4");                                      // 風袋重量④
        mapping.put("hutaijyuuryou5", "hutaijyuuryou5");                                      // 風袋重量⑤
        mapping.put("hutaijyuuryou6", "hutaijyuuryou6");                                      // 風袋重量⑥
        mapping.put("hokanyoukijyunbitantousya", "hokanyoukijyunbitantousya");                // 保管容器準備_担当者
        mapping.put("filterrenketsu", "filterrenketsu");                                      // ﾌｨﾙﾀｰ連結
        mapping.put("filtertoritsuke1filterhinmei", "filtertoritsuke1filterhinmei");          // ﾌｨﾙﾀｰ取り付け_1次ﾌｨﾙﾀｰ品名
        mapping.put("filtertoritsukefilterlotno1", "filtertoritsukefilterlotno1");            // ﾌｨﾙﾀｰ取り付け_LotNo1
        mapping.put("filtertoritsuketoritsukehonsuu1", "filtertoritsuketoritsukehonsuu1");    // ﾌｨﾙﾀｰ取り付け_取り付け本数1
        mapping.put("filtertoritsuke2filterhinmei", "filtertoritsuke2filterhinmei");          // ﾌｨﾙﾀｰ取り付け_2次ﾌｨﾙﾀｰ品名
        mapping.put("filtertoritsukefilterlotno2", "filtertoritsukefilterlotno2");            // ﾌｨﾙﾀｰ取り付け_LotNo2
        mapping.put("filtertoritsuketoritsukehonsuu2", "filtertoritsuketoritsukehonsuu2");    // ﾌｨﾙﾀｰ取り付け_取り付け本数2
        mapping.put("filtertoritsuketantousya", "filtertoritsuketantousya");                  // ﾌｨﾙﾀｰ取り付け_担当者
        mapping.put("assoutankno", "assoutankno");                                            // 圧送ﾀﾝｸNo
        mapping.put("fpkaishinichiji", "fpkaishinichiji");                                    // F/P開始日時
        mapping.put("assouregulatorno", "assouregulatorno");                                  // 圧送ﾚｷﾞｭﾚｰﾀｰNo
        mapping.put("assouatsuryoku", "assouatsuryoku");                                      // 圧送圧力
        mapping.put("filterpasskaishitantousya", "filterpasskaishitantousya");                // ﾌｨﾙﾀｰﾊﾟｽ開始_担当者
        mapping.put("hozonyousamplekaisyuu", "hozonyousamplekaisyuu");                        // 保存用ｻﾝﾌﾟﾙ回収
        mapping.put("filterkoukan1fpteishinichiji", "filterkoukan1fpteishinichiji");          // ﾌｨﾙﾀｰ交換①_F/P停止日時
        mapping.put("filterkoukan11filterhinmei", "filterkoukan11filterhinmei");              // ﾌｨﾙﾀｰ交換①_1次ﾌｨﾙﾀｰ品名
        mapping.put("filterkoukan1lotno1", "filterkoukan1lotno1");                            // ﾌｨﾙﾀｰ交換①_LotNo1
        mapping.put("filterkoukan1toritsukehonnsuu1", "filterkoukan1toritsukehonnsuu1");      // ﾌｨﾙﾀｰ交換①_取り付け本数1
        mapping.put("filterkoukan12filterhinmei", "filterkoukan12filterhinmei");              // ﾌｨﾙﾀｰ交換①_2次ﾌｨﾙﾀｰ品名
        mapping.put("filterkoukan1lotno2", "filterkoukan1lotno2");                            // ﾌｨﾙﾀｰ交換①_LotNo2
        mapping.put("filterkoukan1toritsukehonnsuu2", "filterkoukan1toritsukehonnsuu2");      // ﾌｨﾙﾀｰ交換①_取り付け本数2
        mapping.put("filterkoukan13filterhinmei", "filterkoukan13filterhinmei");              // ﾌｨﾙﾀｰ交換①_3次ﾌｨﾙﾀｰ品名
        mapping.put("filterkoukan1lotno3", "filterkoukan1lotno3");                            // ﾌｨﾙﾀｰ交換①_LotNo3
        mapping.put("filterkoukan1toritsukehonnsuu3", "filterkoukan1toritsukehonnsuu3");      // ﾌｨﾙﾀｰ交換①_取り付け本数3
        mapping.put("filterkoukan1fpsaikainichiji", "filterkoukan1fpsaikainichiji");          // ﾌｨﾙﾀｰ交換①_F/P再開日時
        mapping.put("filterkoukan1tantousya", "filterkoukan1tantousya");                      // ﾌｨﾙﾀｰ交換①_担当者
        mapping.put("fpsyuryounichiji", "fpsyuryounichiji");                                  // F/P終了日時
        mapping.put("fpzikan", "fpzikan");                                                    // F/P時間
        mapping.put("filterpasssyuuryoutantousya", "filterpasssyuuryoutantousya");            // ﾌｨﾙﾀｰﾊﾟｽ終了_担当者
        mapping.put("soujyuryou1", "soujyuryou1");                                            // 総重量1
        mapping.put("soujyuryou2", "soujyuryou2");                                            // 総重量2
        mapping.put("soujyuryou3", "soujyuryou3");                                            // 総重量3
        mapping.put("soujyuryou4", "soujyuryou4");                                            // 総重量4
        mapping.put("soujyuryou5", "soujyuryou5");                                            // 総重量5
        mapping.put("soujyuryou6", "soujyuryou6");                                            // 総重量6
        mapping.put("syoumijyuuryou1", "syoumijyuuryou1");                                    // 正味重量1
        mapping.put("syoumijyuuryou2", "syoumijyuuryou2");                                    // 正味重量2
        mapping.put("syoumijyuuryou3", "syoumijyuuryou3");                                    // 正味重量3
        mapping.put("syoumijyuuryou4", "syoumijyuuryou4");                                    // 正味重量4
        mapping.put("syoumijyuuryou5", "syoumijyuuryou5");                                    // 正味重量5
        mapping.put("syoumijyuuryou6", "syoumijyuuryou6");                                    // 正味重量6
        mapping.put("slipjyuuryougoukei", "slipjyuuryougoukei");                              // ｽﾘｯﾌﾟ重量合計
        mapping.put("bikou1", "bikou1");                                                      // 備考1
        mapping.put("bikou2", "bikou2");                                                      // 備考2
        mapping.put("torokunichiji", "torokunichiji");                                        // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                                          // 更新日時
        mapping.put("revision", "revision");                                                  // revision

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrSlipFpBaketsu>> beanHandler = new BeanListHandler<>(SrSlipFpBaketsu.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [ｽﾘｯﾌﾟ作製・FP(バケツ)_仮登録]から、ﾃﾞｰﾀを取得
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
    private List<SrSlipFpBaketsu> loadTmpSrSlipFpBaketsu(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, String rev) throws SQLException {

        String sql = " SELECT "
                + " kojyo,lotno,edaban,jissekino,sliphinmei,sliplotno,lotkubun,genryoukigou,haisyutsuyouki,hutaijyuuryou1,hutaijyuuryou2,"
                + "hutaijyuuryou3,hutaijyuuryou4,hutaijyuuryou5,hutaijyuuryou6,hokanyoukijyunbitantousya,filterrenketsu,filtertoritsuke1filterhinmei,"
                + "filtertoritsukefilterlotno1,filtertoritsuketoritsukehonsuu1,filtertoritsuke2filterhinmei,filtertoritsukefilterlotno2,"
                + "filtertoritsuketoritsukehonsuu2,filtertoritsuketantousya,assoutankno,fpkaishinichiji,assouregulatorno,assouatsuryoku,"
                + "filterpasskaishitantousya,hozonyousamplekaisyuu,filterkoukan1fpteishinichiji,filterkoukan11filterhinmei,filterkoukan1lotno1,"
                + "filterkoukan1toritsukehonnsuu1,filterkoukan12filterhinmei,filterkoukan1lotno2,filterkoukan1toritsukehonnsuu2,filterkoukan13filterhinmei,"
                + "filterkoukan1lotno3,filterkoukan1toritsukehonnsuu3,filterkoukan1fpsaikainichiji,filterkoukan1tantousya,fpsyuryounichiji,"
                + "fpzikan,filterpasssyuuryoutantousya,soujyuryou1,soujyuryou2,soujyuryou3,soujyuryou4,soujyuryou5,soujyuryou6,syoumijyuuryou1,"
                + "syoumijyuuryou2,syoumijyuuryou3,syoumijyuuryou4,syoumijyuuryou5,syoumijyuuryou6,slipjyuuryougoukei,bikou1,bikou2,torokunichiji,"
                + "kosinnichiji,revision,deleteflag "
                + " FROM tmp_sr_slip_fp_baketsu "
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
        mapping.put("kojyo", "kojyo");                                                        // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                                        // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                                      // 枝番
        mapping.put("jissekino", "jissekino");                                                // 実績No
        mapping.put("sliphinmei", "sliphinmei");                                              // ｽﾘｯﾌﾟ品名
        mapping.put("sliplotno", "sliplotno");                                                // ｽﾘｯﾌﾟLotNo
        mapping.put("lotkubun", "lotkubun");                                                  // ﾛｯﾄ区分
        mapping.put("genryoukigou", "genryoukigou");                                          // 原料記号
        mapping.put("haisyutsuyouki", "haisyutsuyouki");                                      // 排出容器の内袋
        mapping.put("hutaijyuuryou1", "hutaijyuuryou1");                                      // 風袋重量①
        mapping.put("hutaijyuuryou2", "hutaijyuuryou2");                                      // 風袋重量②
        mapping.put("hutaijyuuryou3", "hutaijyuuryou3");                                      // 風袋重量③
        mapping.put("hutaijyuuryou4", "hutaijyuuryou4");                                      // 風袋重量④
        mapping.put("hutaijyuuryou5", "hutaijyuuryou5");                                      // 風袋重量⑤
        mapping.put("hutaijyuuryou6", "hutaijyuuryou6");                                      // 風袋重量⑥
        mapping.put("hokanyoukijyunbitantousya", "hokanyoukijyunbitantousya");                // 保管容器準備_担当者
        mapping.put("filterrenketsu", "filterrenketsu");                                      // ﾌｨﾙﾀｰ連結
        mapping.put("filtertoritsuke1filterhinmei", "filtertoritsuke1filterhinmei");          // ﾌｨﾙﾀｰ取り付け_1次ﾌｨﾙﾀｰ品名
        mapping.put("filtertoritsukefilterlotno1", "filtertoritsukefilterlotno1");            // ﾌｨﾙﾀｰ取り付け_LotNo1
        mapping.put("filtertoritsuketoritsukehonsuu1", "filtertoritsuketoritsukehonsuu1");    // ﾌｨﾙﾀｰ取り付け_取り付け本数1
        mapping.put("filtertoritsuke2filterhinmei", "filtertoritsuke2filterhinmei");          // ﾌｨﾙﾀｰ取り付け_2次ﾌｨﾙﾀｰ品名
        mapping.put("filtertoritsukefilterlotno2", "filtertoritsukefilterlotno2");            // ﾌｨﾙﾀｰ取り付け_LotNo2
        mapping.put("filtertoritsuketoritsukehonsuu2", "filtertoritsuketoritsukehonsuu2");    // ﾌｨﾙﾀｰ取り付け_取り付け本数2
        mapping.put("filtertoritsuketantousya", "filtertoritsuketantousya");                  // ﾌｨﾙﾀｰ取り付け_担当者
        mapping.put("assoutankno", "assoutankno");                                            // 圧送ﾀﾝｸNo
        mapping.put("fpkaishinichiji", "fpkaishinichiji");                                    // F/P開始日時
        mapping.put("assouregulatorno", "assouregulatorno");                                  // 圧送ﾚｷﾞｭﾚｰﾀｰNo
        mapping.put("assouatsuryoku", "assouatsuryoku");                                      // 圧送圧力
        mapping.put("filterpasskaishitantousya", "filterpasskaishitantousya");                // ﾌｨﾙﾀｰﾊﾟｽ開始_担当者
        mapping.put("hozonyousamplekaisyuu", "hozonyousamplekaisyuu");                        // 保存用ｻﾝﾌﾟﾙ回収
        mapping.put("filterkoukan1fpteishinichiji", "filterkoukan1fpteishinichiji");          // ﾌｨﾙﾀｰ交換①_F/P停止日時
        mapping.put("filterkoukan11filterhinmei", "filterkoukan11filterhinmei");              // ﾌｨﾙﾀｰ交換①_1次ﾌｨﾙﾀｰ品名
        mapping.put("filterkoukan1lotno1", "filterkoukan1lotno1");                            // ﾌｨﾙﾀｰ交換①_LotNo1
        mapping.put("filterkoukan1toritsukehonnsuu1", "filterkoukan1toritsukehonnsuu1");      // ﾌｨﾙﾀｰ交換①_取り付け本数1
        mapping.put("filterkoukan12filterhinmei", "filterkoukan12filterhinmei");              // ﾌｨﾙﾀｰ交換①_2次ﾌｨﾙﾀｰ品名
        mapping.put("filterkoukan1lotno2", "filterkoukan1lotno2");                            // ﾌｨﾙﾀｰ交換①_LotNo2
        mapping.put("filterkoukan1toritsukehonnsuu2", "filterkoukan1toritsukehonnsuu2");      // ﾌｨﾙﾀｰ交換①_取り付け本数2
        mapping.put("filterkoukan13filterhinmei", "filterkoukan13filterhinmei");              // ﾌｨﾙﾀｰ交換①_3次ﾌｨﾙﾀｰ品名
        mapping.put("filterkoukan1lotno3", "filterkoukan1lotno3");                            // ﾌｨﾙﾀｰ交換①_LotNo3
        mapping.put("filterkoukan1toritsukehonnsuu3", "filterkoukan1toritsukehonnsuu3");      // ﾌｨﾙﾀｰ交換①_取り付け本数3
        mapping.put("filterkoukan1fpsaikainichiji", "filterkoukan1fpsaikainichiji");          // ﾌｨﾙﾀｰ交換①_F/P再開日時
        mapping.put("filterkoukan1tantousya", "filterkoukan1tantousya");                      // ﾌｨﾙﾀｰ交換①_担当者
        mapping.put("fpsyuryounichiji", "fpsyuryounichiji");                                  // F/P終了日時
        mapping.put("fpzikan", "fpzikan");                                                    // F/P時間
        mapping.put("filterpasssyuuryoutantousya", "filterpasssyuuryoutantousya");            // ﾌｨﾙﾀｰﾊﾟｽ終了_担当者
        mapping.put("soujyuryou1", "soujyuryou1");                                            // 総重量1
        mapping.put("soujyuryou2", "soujyuryou2");                                            // 総重量2
        mapping.put("soujyuryou3", "soujyuryou3");                                            // 総重量3
        mapping.put("soujyuryou4", "soujyuryou4");                                            // 総重量4
        mapping.put("soujyuryou5", "soujyuryou5");                                            // 総重量5
        mapping.put("soujyuryou6", "soujyuryou6");                                            // 総重量6
        mapping.put("syoumijyuuryou1", "syoumijyuuryou1");                                    // 正味重量1
        mapping.put("syoumijyuuryou2", "syoumijyuuryou2");                                    // 正味重量2
        mapping.put("syoumijyuuryou3", "syoumijyuuryou3");                                    // 正味重量3
        mapping.put("syoumijyuuryou4", "syoumijyuuryou4");                                    // 正味重量4
        mapping.put("syoumijyuuryou5", "syoumijyuuryou5");                                    // 正味重量5
        mapping.put("syoumijyuuryou6", "syoumijyuuryou6");                                    // 正味重量6
        mapping.put("slipjyuuryougoukei", "slipjyuuryougoukei");                              // ｽﾘｯﾌﾟ重量合計
        mapping.put("bikou1", "bikou1");                                                      // 備考1
        mapping.put("bikou2", "bikou2");                                                      // 備考2
        mapping.put("torokunichiji", "torokunichiji");                                        // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                                          // 更新日時
        mapping.put("revision", "revision");                                                  // revision
        mapping.put("deleteflag", "deleteflag");                                              // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrSlipFpBaketsu>> beanHandler = new BeanListHandler<>(SrSlipFpBaketsu.class, rowProcessor);

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
     * @param srSlipFpBaketsu ｽﾘｯﾌﾟ作製・FP(バケツ)データ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrSlipFpBaketsu srSlipFpBaketsu) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srSlipFpBaketsu != null) {
            // 元データが存在する場合元データより取得
            return getSrSlipFpBaketsuItemData(itemId, srSlipFpBaketsu);
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
    private String getItemKikakuchi(List<FXHDD01> listData, String itemId, SrSlipFpBaketsu srSlipFpBaketsu) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return StringUtil.nullToBlank(selectData.get(0).getKikakuChi()).replace("【", "").replace("】", "");
        } else if (srSlipFpBaketsu != null) {
            // 元データが存在する場合元データより取得
            return getSrSlipFpBaketsuItemData(itemId, srSlipFpBaketsu);
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
     * ｽﾘｯﾌﾟ作製・FP(バケツ)_仮登録(tmp_sr_slip_fp_baketsu)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void insertTmpSrSlipFpBaketsu(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, String systemTime, ProcessData processData) throws SQLException {

        String sql = "INSERT INTO tmp_sr_slip_fp_baketsu ("
                + " kojyo,lotno,edaban,jissekino,sliphinmei,sliplotno,lotkubun,genryoukigou,haisyutsuyouki,hutaijyuuryou1,hutaijyuuryou2,"
                + "hutaijyuuryou3,hutaijyuuryou4,hutaijyuuryou5,hutaijyuuryou6,hokanyoukijyunbitantousya,filterrenketsu,filtertoritsuke1filterhinmei,"
                + "filtertoritsukefilterlotno1,filtertoritsuketoritsukehonsuu1,filtertoritsuke2filterhinmei,filtertoritsukefilterlotno2,"
                + "filtertoritsuketoritsukehonsuu2,filtertoritsuketantousya,assoutankno,fpkaishinichiji,assouregulatorno,assouatsuryokukikaku,assouatsuryoku,"
                + "filterpasskaishitantousya,hozonyousamplekaisyuu,filterkoukan1fpteishinichiji,filterkoukan11filterhinmei,filterkoukan1lotno1,"
                + "filterkoukan1toritsukehonnsuu1,filterkoukan12filterhinmei,filterkoukan1lotno2,filterkoukan1toritsukehonnsuu2,filterkoukan13filterhinmei,"
                + "filterkoukan1lotno3,filterkoukan1toritsukehonnsuu3,filterkoukan1fpsaikainichiji,filterkoukan1tantousya,fpsyuryounichiji,"
                + "fpzikan,filterpasssyuuryoutantousya,soujyuryou1,soujyuryou2,soujyuryou3,soujyuryou4,soujyuryou5,soujyuryou6,syoumijyuuryou1,"
                + "syoumijyuuryou2,syoumijyuuryou3,syoumijyuuryou4,syoumijyuuryou5,syoumijyuuryou6,slipjyuuryougoukei,bikou1,bikou2,torokunichiji,"
                + "kosinnichiji,revision,deleteflag "
                + " ) VALUES ( "
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrSlipFpBaketsu(true, newRev, deleteflag, kojyo, lotNo, edaban, jissekino, systemTime, processData, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・FP(バケツ)_仮登録(tmp_sr_slip_fp_baketsu)更新処理
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
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void updateTmpSrSlipFpBaketsu(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, String systemTime, ProcessData processData) throws SQLException {

        String sql = "UPDATE tmp_sr_slip_fp_baketsu SET "
                + " sliphinmei = ?,sliplotno = ?,lotkubun = ?,genryoukigou = ?,haisyutsuyouki = ?,hutaijyuuryou1 = ?,hutaijyuuryou2 = ?,hutaijyuuryou3 = ?,"
                + "hutaijyuuryou4 = ?,hutaijyuuryou5 = ?,hutaijyuuryou6 = ?,hokanyoukijyunbitantousya = ?,filterrenketsu = ?,filtertoritsuke1filterhinmei = ?,"
                + "filtertoritsukefilterlotno1 = ?,filtertoritsuketoritsukehonsuu1 = ?,filtertoritsuke2filterhinmei = ?,filtertoritsukefilterlotno2 = ?,"
                + "filtertoritsuketoritsukehonsuu2 = ?,filtertoritsuketantousya = ?,assoutankno = ?,fpkaishinichiji = ?,assouregulatorno = ?,assouatsuryokukikaku = ?,assouatsuryoku = ?,"
                + "filterpasskaishitantousya = ?,hozonyousamplekaisyuu = ?,filterkoukan1fpteishinichiji = ?,filterkoukan11filterhinmei = ?,filterkoukan1lotno1 = ?,"
                + "filterkoukan1toritsukehonnsuu1 = ?,filterkoukan12filterhinmei = ?,filterkoukan1lotno2 = ?,filterkoukan1toritsukehonnsuu2 = ?,filterkoukan13filterhinmei = ?,"
                + "filterkoukan1lotno3 = ?,filterkoukan1toritsukehonnsuu3 = ?,filterkoukan1fpsaikainichiji = ?,filterkoukan1tantousya = ?,fpsyuryounichiji = ?,fpzikan = ?,"
                + "filterpasssyuuryoutantousya = ?,soujyuryou1 = ?,soujyuryou2 = ?,soujyuryou3 = ?,soujyuryou4 = ?,soujyuryou5 = ?,soujyuryou6 = ?,syoumijyuuryou1 = ?,"
                + "syoumijyuuryou2 = ?,syoumijyuuryou3 = ?,syoumijyuuryou4 = ?,syoumijyuuryou5 = ?,syoumijyuuryou6 = ?,slipjyuuryougoukei = ?,bikou1 = ?,bikou2 = ?,"
                + "kosinnichiji = ?,revision = ?,deleteflag = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrSlipFpBaketsu> srSlipFpBaketsuList = getSrSlipFpBaketsuData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekino);
        SrSlipFpBaketsu srSlipFpBaketsu = null;
        if (!srSlipFpBaketsuList.isEmpty()) {
            srSlipFpBaketsu = srSlipFpBaketsuList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrSlipFpBaketsu(false, newRev, 0, "", "", "", jissekino, systemTime, processData, srSlipFpBaketsu);

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
     * ｽﾘｯﾌﾟ作製・FP(バケツ)_仮登録(tmp_sr_slip_fp_baketsu)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrSlipFpBaketsu(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        String sql = "DELETE FROM tmp_sr_slip_fp_baketsu "
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
     * ｽﾘｯﾌﾟ作製・FP(バケツ)_仮登録(tmp_sr_slip_fp_baketsu)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srSlipFpBaketsu ｽﾘｯﾌﾟ作製・FP(バケツ)データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrSlipFpBaketsu(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, int jissekino, String systemTime, ProcessData processData, SrSlipFpBaketsu srSlipFpBaketsu) {

        List<FXHDD01> pItemList = processData.getItemList();
        List<Object> params = new ArrayList<>();
        // F/P開始日時
        String fpkaishiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B035Const.FPKAISHI_TIME, srSlipFpBaketsu));
        // F/P停止日時
        String filterkoukan1fpteishiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B035Const.FILTERKOUKAN1FPTEISHI_TIME, srSlipFpBaketsu));
        // F/P再開日時
        String filterkoukan1fpsaikaiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B035Const.FILTERKOUKAN1FPSAIKAI_TIME, srSlipFpBaketsu));
        // F/P終了日時
        String fpsyuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B035Const.FPSYURYOU_TIME, srSlipFpBaketsu));
        if (isInsert) {
            params.add(kojyo);     // 工場ｺｰﾄﾞ
            params.add(lotNo);     // ﾛｯﾄNo
            params.add(edaban);    // 枝番
            params.add(jissekino); // 実績No
        }

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.SLIPHINMEI, srSlipFpBaketsu)));                                // ｽﾘｯﾌﾟ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.SLIPLOTNO, srSlipFpBaketsu)));                                 // ｽﾘｯﾌﾟLotNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.LOTKUBUN, srSlipFpBaketsu)));                                  // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B035Const.GENRYOUKIGOU, srSlipFpBaketsu)));                         // 原料記号
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B035Const.HAISYUTSUYOUKI, srSlipFpBaketsu), null));                                          // 排出容器の内袋
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.HUTAIJYUURYOU1, srSlipFpBaketsu)));                               // 風袋重量①
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.HUTAIJYUURYOU2, srSlipFpBaketsu)));                               // 風袋重量②
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.HUTAIJYUURYOU3, srSlipFpBaketsu)));                               // 風袋重量③
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.HUTAIJYUURYOU4, srSlipFpBaketsu)));                               // 風袋重量④
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.HUTAIJYUURYOU5, srSlipFpBaketsu)));                               // 風袋重量⑤
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.HUTAIJYUURYOU6, srSlipFpBaketsu)));                               // 風袋重量⑥
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.HOKANYOUKIJYUNBITANTOUSYA, srSlipFpBaketsu)));                 // 保管容器準備_担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B035Const.FILTERRENKETSU, srSlipFpBaketsu)));                       // ﾌｨﾙﾀｰ連結
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B035Const.FILTERTORITSUKE1FILTERHINMEI, srSlipFpBaketsu)));         // ﾌｨﾙﾀｰ取り付け_1次ﾌｨﾙﾀｰ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.FILTERTORITSUKEFILTERLOTNO1, srSlipFpBaketsu)));               // ﾌｨﾙﾀｰ取り付け_LotNo1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.FILTERTORITSUKETORITSUKEHONSUU1, srSlipFpBaketsu)));              // ﾌｨﾙﾀｰ取り付け_取り付け本数1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B035Const.FILTERTORITSUKE2FILTERHINMEI, srSlipFpBaketsu)));         // ﾌｨﾙﾀｰ取り付け_2次ﾌｨﾙﾀｰ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.FILTERTORITSUKEFILTERLOTNO2, srSlipFpBaketsu)));               // ﾌｨﾙﾀｰ取り付け_LotNo2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.FILTERTORITSUKETORITSUKEHONSUU2, srSlipFpBaketsu)));              // ﾌｨﾙﾀｰ取り付け_取り付け本数2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.FILTERTORITSUKETANTOUSYA, srSlipFpBaketsu)));                  // ﾌｨﾙﾀｰ取り付け_担当者
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.ASSOUTANKNO, srSlipFpBaketsu)));                                  // 圧送ﾀﾝｸNo
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.FPKAISHI_DAY, srSlipFpBaketsu),
                "".equals(fpkaishiTime) ? "0000" : fpkaishiTime));                                                                                                // F/P開始日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.ASSOUREGULATORNO, srSlipFpBaketsu)));                          // 圧送ﾚｷﾞｭﾚｰﾀｰNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B035Const.ASSOUATSURYOKU, srSlipFpBaketsu)));                       // 圧送圧力規格
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.ASSOUATSURYOKU, srSlipFpBaketsu)));                        // 圧送圧力
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.FILTERPASSKAISHITANTOUSYA, srSlipFpBaketsu)));                 // ﾌｨﾙﾀｰﾊﾟｽ開始_担当者
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B035Const.HOZONYOUSAMPLEKAISYUU, srSlipFpBaketsu), null));                                   // 保存用ｻﾝﾌﾟﾙ回収
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.FILTERKOUKAN1FPTEISHI_DAY, srSlipFpBaketsu),
                "".equals(filterkoukan1fpteishiTime) ? "0000" : filterkoukan1fpteishiTime));                                                                      // ﾌｨﾙﾀｰ交換①_F/P停止日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B035Const.FILTERKOUKAN11FILTERHINMEI, srSlipFpBaketsu)));           // ﾌｨﾙﾀｰ交換①_1次ﾌｨﾙﾀｰ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.FILTERKOUKAN1LOTNO1, srSlipFpBaketsu)));                       // ﾌｨﾙﾀｰ交換①_LotNo1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.FILTERKOUKAN1TORITSUKEHONNSUU1, srSlipFpBaketsu)));               // ﾌｨﾙﾀｰ交換①_取り付け本数1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B035Const.FILTERKOUKAN12FILTERHINMEI, srSlipFpBaketsu)));           // ﾌｨﾙﾀｰ交換①_2次ﾌｨﾙﾀｰ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.FILTERKOUKAN1LOTNO2, srSlipFpBaketsu)));                       // ﾌｨﾙﾀｰ交換①_LotNo2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.FILTERKOUKAN1TORITSUKEHONNSUU2, srSlipFpBaketsu)));               // ﾌｨﾙﾀｰ交換①_取り付け本数2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B035Const.FILTERKOUKAN13FILTERHINMEI, srSlipFpBaketsu)));           // ﾌｨﾙﾀｰ交換①_3次ﾌｨﾙﾀｰ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.FILTERKOUKAN1LOTNO3, srSlipFpBaketsu)));                       // ﾌｨﾙﾀｰ交換①_LotNo3
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.FILTERKOUKAN1TORITSUKEHONNSUU3, srSlipFpBaketsu)));               // ﾌｨﾙﾀｰ交換①_取り付け本数3
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.FILTERKOUKAN1FPSAIKAI_DAY, srSlipFpBaketsu),
                "".equals(filterkoukan1fpsaikaiTime) ? "0000" : filterkoukan1fpsaikaiTime));                                                                      // ﾌｨﾙﾀｰ交換①_F/P再開日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.FILTERKOUKAN1TANTOUSYA, srSlipFpBaketsu)));                    // ﾌｨﾙﾀｰ交換①_担当者
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.FPSYURYOU_DAY, srSlipFpBaketsu),
                "".equals(fpsyuryouTime) ? "0000" : fpsyuryouTime));                                                                                              // F/P終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.FPZIKAN, srSlipFpBaketsu)));                                   // F/P時間
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.FILTERPASSSYUURYOUTANTOUSYA, srSlipFpBaketsu)));               // ﾌｨﾙﾀｰﾊﾟｽ終了_担当者
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.SOUJYURYOU1, srSlipFpBaketsu)));                                  // 総重量1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.SOUJYURYOU2, srSlipFpBaketsu)));                                  // 総重量2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.SOUJYURYOU3, srSlipFpBaketsu)));                                  // 総重量3
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.SOUJYURYOU4, srSlipFpBaketsu)));                                  // 総重量4
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.SOUJYURYOU5, srSlipFpBaketsu)));                                  // 総重量5
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.SOUJYURYOU6, srSlipFpBaketsu)));                                  // 総重量6
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.SYOUMIJYUURYOU1, srSlipFpBaketsu)));                              // 正味重量1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.SYOUMIJYUURYOU2, srSlipFpBaketsu)));                              // 正味重量2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.SYOUMIJYUURYOU3, srSlipFpBaketsu)));                              // 正味重量3
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.SYOUMIJYUURYOU4, srSlipFpBaketsu)));                              // 正味重量4
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.SYOUMIJYUURYOU5, srSlipFpBaketsu)));                              // 正味重量5
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.SYOUMIJYUURYOU6, srSlipFpBaketsu)));                              // 正味重量6
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.SLIPJYUURYOUGOUKEI, srSlipFpBaketsu)));                           // ｽﾘｯﾌﾟ重量合計
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.BIKOU1, srSlipFpBaketsu)));                                    // 備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B035Const.BIKOU2, srSlipFpBaketsu)));                                    // 備考2

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
     * ｽﾘｯﾌﾟ作製・FP(バケツ)(sr_slip_fp_baketsu)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srSlipFpBaketsu 登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrSlipFpBaketsu(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, String systemTime, ProcessData processData, SrSlipFpBaketsu srSlipFpBaketsu) throws SQLException {

        String sql = "INSERT INTO sr_slip_fp_baketsu ("
                + " kojyo,lotno,edaban,jissekino,sliphinmei,sliplotno,lotkubun,genryoukigou,haisyutsuyouki,hutaijyuuryou1,hutaijyuuryou2,"
                + "hutaijyuuryou3,hutaijyuuryou4,hutaijyuuryou5,hutaijyuuryou6,hokanyoukijyunbitantousya,filterrenketsu,filtertoritsuke1filterhinmei,"
                + "filtertoritsukefilterlotno1,filtertoritsuketoritsukehonsuu1,filtertoritsuke2filterhinmei,filtertoritsukefilterlotno2,"
                + "filtertoritsuketoritsukehonsuu2,filtertoritsuketantousya,assoutankno,fpkaishinichiji,assouregulatorno,assouatsuryokukikaku,assouatsuryoku,"
                + "filterpasskaishitantousya,hozonyousamplekaisyuu,filterkoukan1fpteishinichiji,filterkoukan11filterhinmei,filterkoukan1lotno1,"
                + "filterkoukan1toritsukehonnsuu1,filterkoukan12filterhinmei,filterkoukan1lotno2,filterkoukan1toritsukehonnsuu2,filterkoukan13filterhinmei,"
                + "filterkoukan1lotno3,filterkoukan1toritsukehonnsuu3,filterkoukan1fpsaikainichiji,filterkoukan1tantousya,fpsyuryounichiji,"
                + "fpzikan,filterpasssyuuryoutantousya,soujyuryou1,soujyuryou2,soujyuryou3,soujyuryou4,soujyuryou5,soujyuryou6,syoumijyuuryou1,"
                + "syoumijyuuryou2,syoumijyuuryou3,syoumijyuuryou4,syoumijyuuryou5,syoumijyuuryou6,slipjyuuryougoukei,bikou1,bikou2,torokunichiji,"
                + "kosinnichiji,revision "
                + " ) VALUES ( "
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrSlipFpBaketsu(true, newRev, kojyo, lotNo, edaban, jissekino, systemTime, processData, srSlipFpBaketsu);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・FP(バケツ)(sr_slip_fp_baketsu)更新処理
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
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void updateSrSlipFpBaketsu(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, String systemTime, ProcessData processData) throws SQLException {

        String sql = "UPDATE sr_slip_fp_baketsu SET "
                + " sliphinmei = ?,sliplotno = ?,lotkubun = ?,genryoukigou = ?,haisyutsuyouki = ?,hutaijyuuryou1 = ?,hutaijyuuryou2 = ?,hutaijyuuryou3 = ?,"
                + "hutaijyuuryou4 = ?,hutaijyuuryou5 = ?,hutaijyuuryou6 = ?,hokanyoukijyunbitantousya = ?,filterrenketsu = ?,filtertoritsuke1filterhinmei = ?,"
                + "filtertoritsukefilterlotno1 = ?,filtertoritsuketoritsukehonsuu1 = ?,filtertoritsuke2filterhinmei = ?,filtertoritsukefilterlotno2 = ?,"
                + "filtertoritsuketoritsukehonsuu2 = ?,filtertoritsuketantousya = ?,assoutankno = ?,fpkaishinichiji = ?,assouregulatorno = ?,assouatsuryokukikaku = ?,assouatsuryoku = ?,"
                + "filterpasskaishitantousya = ?,hozonyousamplekaisyuu = ?,filterkoukan1fpteishinichiji = ?,filterkoukan11filterhinmei = ?,filterkoukan1lotno1 = ?,"
                + "filterkoukan1toritsukehonnsuu1 = ?,filterkoukan12filterhinmei = ?,filterkoukan1lotno2 = ?,filterkoukan1toritsukehonnsuu2 = ?,filterkoukan13filterhinmei = ?,"
                + "filterkoukan1lotno3 = ?,filterkoukan1toritsukehonnsuu3 = ?,filterkoukan1fpsaikainichiji = ?,filterkoukan1tantousya = ?,fpsyuryounichiji = ?,fpzikan = ?,"
                + "filterpasssyuuryoutantousya = ?,soujyuryou1 = ?,soujyuryou2 = ?,soujyuryou3 = ?,soujyuryou4 = ?,soujyuryou5 = ?,soujyuryou6 = ?,syoumijyuuryou1 = ?,"
                + "syoumijyuuryou2 = ?,syoumijyuuryou3 = ?,syoumijyuuryou4 = ?,syoumijyuuryou5 = ?,syoumijyuuryou6 = ?,slipjyuuryougoukei = ?,bikou1 = ?,bikou2 = ?,"
                + "kosinnichiji = ?,revision = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrSlipFpBaketsu> srSlipFpBaketsuList = getSrSlipFpBaketsuData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekino);
        SrSlipFpBaketsu srSlipFpBaketsu = null;
        if (!srSlipFpBaketsuList.isEmpty()) {
            srSlipFpBaketsu = srSlipFpBaketsuList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrSlipFpBaketsu(false, newRev, "", "", "", jissekino, systemTime, processData, srSlipFpBaketsu);

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
     * ｽﾘｯﾌﾟ作製・FP(バケツ)(sr_slip_fp_baketsu)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srSlipFpBaketsu ｽﾘｯﾌﾟ作製・FP(バケツ)データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrSlipFpBaketsu(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban, int jissekino,
            String systemTime, ProcessData processData, SrSlipFpBaketsu srSlipFpBaketsu) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // F/P開始日時
        String fpkaishiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B035Const.FPKAISHI_TIME, srSlipFpBaketsu));
        // F/P停止日時
        String filterkoukan1fpteishiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B035Const.FILTERKOUKAN1FPTEISHI_TIME, srSlipFpBaketsu));
        // F/P再開日時
        String filterkoukan1fpsaikaiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B035Const.FILTERKOUKAN1FPSAIKAI_TIME, srSlipFpBaketsu));
        // F/P終了日時
        String fpsyuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B035Const.FPSYURYOU_TIME, srSlipFpBaketsu));
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
            params.add(jissekino); // 実績No
        }
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B035Const.SLIPHINMEI, srSlipFpBaketsu)));                                // ｽﾘｯﾌﾟ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B035Const.SLIPLOTNO, srSlipFpBaketsu)));                                 // ｽﾘｯﾌﾟLotNo
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B035Const.LOTKUBUN, srSlipFpBaketsu)));                                  // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B035Const.GENRYOUKIGOU, srSlipFpBaketsu)));                         // 原料記号
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B035Const.HAISYUTSUYOUKI, srSlipFpBaketsu), 9));                                  // 排出容器の内袋
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B035Const.HUTAIJYUURYOU1, srSlipFpBaketsu)));                               // 風袋重量①
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B035Const.HUTAIJYUURYOU2, srSlipFpBaketsu)));                               // 風袋重量②
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B035Const.HUTAIJYUURYOU3, srSlipFpBaketsu)));                               // 風袋重量③
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B035Const.HUTAIJYUURYOU4, srSlipFpBaketsu)));                               // 風袋重量④
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B035Const.HUTAIJYUURYOU5, srSlipFpBaketsu)));                               // 風袋重量⑤
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B035Const.HUTAIJYUURYOU6, srSlipFpBaketsu)));                               // 風袋重量⑥
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B035Const.HOKANYOUKIJYUNBITANTOUSYA, srSlipFpBaketsu)));                 // 保管容器準備_担当者
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B035Const.FILTERRENKETSU, srSlipFpBaketsu)));                       // ﾌｨﾙﾀｰ連結
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B035Const.FILTERTORITSUKE1FILTERHINMEI, srSlipFpBaketsu)));         // ﾌｨﾙﾀｰ取り付け_1次ﾌｨﾙﾀｰ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B035Const.FILTERTORITSUKEFILTERLOTNO1, srSlipFpBaketsu)));               // ﾌｨﾙﾀｰ取り付け_LotNo1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B035Const.FILTERTORITSUKETORITSUKEHONSUU1, srSlipFpBaketsu)));              // ﾌｨﾙﾀｰ取り付け_取り付け本数1
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B035Const.FILTERTORITSUKE2FILTERHINMEI, srSlipFpBaketsu)));         // ﾌｨﾙﾀｰ取り付け_2次ﾌｨﾙﾀｰ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B035Const.FILTERTORITSUKEFILTERLOTNO2, srSlipFpBaketsu)));               // ﾌｨﾙﾀｰ取り付け_LotNo2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B035Const.FILTERTORITSUKETORITSUKEHONSUU2, srSlipFpBaketsu)));              // ﾌｨﾙﾀｰ取り付け_取り付け本数2
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B035Const.FILTERTORITSUKETANTOUSYA, srSlipFpBaketsu)));                  // ﾌｨﾙﾀｰ取り付け_担当者
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B035Const.ASSOUTANKNO, srSlipFpBaketsu)));                                  // 圧送ﾀﾝｸNo
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B035Const.FPKAISHI_DAY, srSlipFpBaketsu),
                "".equals(fpkaishiTime) ? "0000" : fpkaishiTime));                                                                                     // F/P開始日時
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B035Const.ASSOUREGULATORNO, srSlipFpBaketsu)));                          // 圧送ﾚｷﾞｭﾚｰﾀｰNo
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B035Const.ASSOUATSURYOKU, srSlipFpBaketsu)));                       // 圧送圧力規格
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B035Const.ASSOUATSURYOKU, srSlipFpBaketsu)));                        // 圧送圧力
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B035Const.FILTERPASSKAISHITANTOUSYA, srSlipFpBaketsu)));                 // ﾌｨﾙﾀｰﾊﾟｽ開始_担当者
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B035Const.HOZONYOUSAMPLEKAISYUU, srSlipFpBaketsu), 9));                           // 保存用ｻﾝﾌﾟﾙ回収
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B035Const.FILTERKOUKAN1FPTEISHI_DAY, srSlipFpBaketsu),
                "".equals(filterkoukan1fpteishiTime) ? "0000" : filterkoukan1fpteishiTime));                                                           // ﾌｨﾙﾀｰ交換①_F/P停止日時
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B035Const.FILTERKOUKAN11FILTERHINMEI, srSlipFpBaketsu)));           // ﾌｨﾙﾀｰ交換①_1次ﾌｨﾙﾀｰ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B035Const.FILTERKOUKAN1LOTNO1, srSlipFpBaketsu)));                       // ﾌｨﾙﾀｰ交換①_LotNo1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B035Const.FILTERKOUKAN1TORITSUKEHONNSUU1, srSlipFpBaketsu)));               // ﾌｨﾙﾀｰ交換①_取り付け本数1
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B035Const.FILTERKOUKAN12FILTERHINMEI, srSlipFpBaketsu)));           // ﾌｨﾙﾀｰ交換①_2次ﾌｨﾙﾀｰ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B035Const.FILTERKOUKAN1LOTNO2, srSlipFpBaketsu)));                       // ﾌｨﾙﾀｰ交換①_LotNo2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B035Const.FILTERKOUKAN1TORITSUKEHONNSUU2, srSlipFpBaketsu)));               // ﾌｨﾙﾀｰ交換①_取り付け本数2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B035Const.FILTERKOUKAN13FILTERHINMEI, srSlipFpBaketsu)));           // ﾌｨﾙﾀｰ交換①_3次ﾌｨﾙﾀｰ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B035Const.FILTERKOUKAN1LOTNO3, srSlipFpBaketsu)));                       // ﾌｨﾙﾀｰ交換①_LotNo3
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B035Const.FILTERKOUKAN1TORITSUKEHONNSUU3, srSlipFpBaketsu)));               // ﾌｨﾙﾀｰ交換①_取り付け本数3
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B035Const.FILTERKOUKAN1FPSAIKAI_DAY, srSlipFpBaketsu),
                "".equals(filterkoukan1fpsaikaiTime) ? "0000" : filterkoukan1fpsaikaiTime));                                                           // ﾌｨﾙﾀｰ交換①_F/P再開日時
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B035Const.FILTERKOUKAN1TANTOUSYA, srSlipFpBaketsu)));                    // ﾌｨﾙﾀｰ交換①_担当者
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B035Const.FPSYURYOU_DAY, srSlipFpBaketsu),
                "".equals(fpsyuryouTime) ? "0000" : fpsyuryouTime));                                                                                   // F/P終了日時
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B035Const.FPZIKAN, srSlipFpBaketsu)));                                   // F/P時間
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B035Const.FILTERPASSSYUURYOUTANTOUSYA, srSlipFpBaketsu)));               // ﾌｨﾙﾀｰﾊﾟｽ終了_担当者
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B035Const.SOUJYURYOU1, srSlipFpBaketsu)));                                  // 総重量1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B035Const.SOUJYURYOU2, srSlipFpBaketsu)));                                  // 総重量2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B035Const.SOUJYURYOU3, srSlipFpBaketsu)));                                  // 総重量3
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B035Const.SOUJYURYOU4, srSlipFpBaketsu)));                                  // 総重量4
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B035Const.SOUJYURYOU5, srSlipFpBaketsu)));                                  // 総重量5
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B035Const.SOUJYURYOU6, srSlipFpBaketsu)));                                  // 総重量6
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B035Const.SYOUMIJYUURYOU1, srSlipFpBaketsu)));                              // 正味重量1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B035Const.SYOUMIJYUURYOU2, srSlipFpBaketsu)));                              // 正味重量2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B035Const.SYOUMIJYUURYOU3, srSlipFpBaketsu)));                              // 正味重量3
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B035Const.SYOUMIJYUURYOU4, srSlipFpBaketsu)));                              // 正味重量4
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B035Const.SYOUMIJYUURYOU5, srSlipFpBaketsu)));                              // 正味重量5
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B035Const.SYOUMIJYUURYOU6, srSlipFpBaketsu)));                              // 正味重量6
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B035Const.SLIPJYUURYOUGOUKEI, srSlipFpBaketsu)));                           // ｽﾘｯﾌﾟ重量合計
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B035Const.BIKOU1, srSlipFpBaketsu)));                                    // 備考1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B035Const.BIKOU2, srSlipFpBaketsu)));                                    // 備考2

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
     * ｽﾘｯﾌﾟ作製・FP(バケツ)(sr_slip_fp_baketsu)削除処理
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
    private void deleteSrSlipFpBaketsu(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        String sql = "DELETE FROM sr_slip_fp_baketsu "
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
     * [ｽﾘｯﾌﾟ作製・FP(バケツ)_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_slip_fp_baketsu "
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
     * @param srSlipFpBaketsu ｽﾘｯﾌﾟ作製・FP(バケツ)データ
     * @return DB値
     */
    private String getSrSlipFpBaketsuItemData(String itemId, SrSlipFpBaketsu srSlipFpBaketsu) {
        switch (itemId) {
            // ｽﾘｯﾌﾟ品名
            case GXHDO102B035Const.SLIPHINMEI:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getSliphinmei());

            // ｽﾘｯﾌﾟLotNo
            case GXHDO102B035Const.SLIPLOTNO:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getSliplotno());

            // ﾛｯﾄ区分
            case GXHDO102B035Const.LOTKUBUN:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getLotkubun());

            // 原料記号
            case GXHDO102B035Const.GENRYOUKIGOU:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getGenryoukigou());

            // 排出容器の内袋
            case GXHDO102B035Const.HAISYUTSUYOUKI:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSlipFpBaketsu.getHaisyutsuyouki()));

            // 風袋重量①
            case GXHDO102B035Const.HUTAIJYUURYOU1:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getHutaijyuuryou1());

            // 風袋重量②
            case GXHDO102B035Const.HUTAIJYUURYOU2:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getHutaijyuuryou2());

            // 風袋重量③
            case GXHDO102B035Const.HUTAIJYUURYOU3:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getHutaijyuuryou3());

            // 風袋重量④
            case GXHDO102B035Const.HUTAIJYUURYOU4:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getHutaijyuuryou4());

            // 風袋重量⑤
            case GXHDO102B035Const.HUTAIJYUURYOU5:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getHutaijyuuryou5());

            // 風袋重量⑥
            case GXHDO102B035Const.HUTAIJYUURYOU6:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getHutaijyuuryou6());

            // 保管容器準備_担当者
            case GXHDO102B035Const.HOKANYOUKIJYUNBITANTOUSYA:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getHokanyoukijyunbitantousya());

            // ﾌｨﾙﾀｰ連結
            case GXHDO102B035Const.FILTERRENKETSU:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getFilterrenketsu());

            // ﾌｨﾙﾀｰ取り付け_1次ﾌｨﾙﾀｰ品名
            case GXHDO102B035Const.FILTERTORITSUKE1FILTERHINMEI:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getFiltertoritsuke1filterhinmei());

            // ﾌｨﾙﾀｰ取り付け_LotNo1
            case GXHDO102B035Const.FILTERTORITSUKEFILTERLOTNO1:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getFiltertoritsukefilterlotno1());

            // ﾌｨﾙﾀｰ取り付け_取り付け本数1
            case GXHDO102B035Const.FILTERTORITSUKETORITSUKEHONSUU1:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getFiltertoritsuketoritsukehonsuu1());

            // ﾌｨﾙﾀｰ取り付け_2次ﾌｨﾙﾀｰ品名
            case GXHDO102B035Const.FILTERTORITSUKE2FILTERHINMEI:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getFiltertoritsuke2filterhinmei());

            // ﾌｨﾙﾀｰ取り付け_LotNo2
            case GXHDO102B035Const.FILTERTORITSUKEFILTERLOTNO2:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getFiltertoritsukefilterlotno2());

            // ﾌｨﾙﾀｰ取り付け_取り付け本数2
            case GXHDO102B035Const.FILTERTORITSUKETORITSUKEHONSUU2:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getFiltertoritsuketoritsukehonsuu2());

            // ﾌｨﾙﾀｰ取り付け_担当者
            case GXHDO102B035Const.FILTERTORITSUKETANTOUSYA:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getFiltertoritsuketantousya());

            // 圧送ﾀﾝｸ№
            case GXHDO102B035Const.ASSOUTANKNO:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getAssoutankno());

            // F/P開始日
            case GXHDO102B035Const.FPKAISHI_DAY:
                return DateUtil.formattedTimestamp(srSlipFpBaketsu.getFpkaishinichiji(), "yyMMdd");

            // F/P開始時間
            case GXHDO102B035Const.FPKAISHI_TIME:
                return DateUtil.formattedTimestamp(srSlipFpBaketsu.getFpkaishinichiji(), "HHmm");

            // 圧送ﾚｷﾞｭﾚｰﾀｰNo
            case GXHDO102B035Const.ASSOUREGULATORNO:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getAssouregulatorno());

            // 圧送圧力
            case GXHDO102B035Const.ASSOUATSURYOKU:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getAssouatsuryoku());

            // ﾌｨﾙﾀｰﾊﾟｽ開始_担当者
            case GXHDO102B035Const.FILTERPASSKAISHITANTOUSYA:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getFilterpasskaishitantousya());

            // 保存用ｻﾝﾌﾟﾙ回収
            case GXHDO102B035Const.HOZONYOUSAMPLEKAISYUU:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSlipFpBaketsu.getHozonyousamplekaisyuu()));

            // ﾌｨﾙﾀｰ交換①_F/P停止日
            case GXHDO102B035Const.FILTERKOUKAN1FPTEISHI_DAY:
                return DateUtil.formattedTimestamp(srSlipFpBaketsu.getFilterkoukan1fpteishinichiji(), "yyMMdd");

            // ﾌｨﾙﾀｰ交換①_F/P停止時間
            case GXHDO102B035Const.FILTERKOUKAN1FPTEISHI_TIME:
                return DateUtil.formattedTimestamp(srSlipFpBaketsu.getFilterkoukan1fpteishinichiji(), "HHmm");

            // ﾌｨﾙﾀｰ交換①_1次ﾌｨﾙﾀｰ品名
            case GXHDO102B035Const.FILTERKOUKAN11FILTERHINMEI:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getFilterkoukan11filterhinmei());

            // ﾌｨﾙﾀｰ交換①_LotNo1
            case GXHDO102B035Const.FILTERKOUKAN1LOTNO1:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getFilterkoukan1lotno1());

            // ﾌｨﾙﾀｰ交換①_取り付け本数1
            case GXHDO102B035Const.FILTERKOUKAN1TORITSUKEHONNSUU1:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getFilterkoukan1toritsukehonnsuu1());

            // ﾌｨﾙﾀｰ交換①_2次ﾌｨﾙﾀｰ品名
            case GXHDO102B035Const.FILTERKOUKAN12FILTERHINMEI:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getFilterkoukan12filterhinmei());

            // ﾌｨﾙﾀｰ交換①_LotNo2
            case GXHDO102B035Const.FILTERKOUKAN1LOTNO2:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getFilterkoukan1lotno2());

            // ﾌｨﾙﾀｰ交換①_取り付け本数2
            case GXHDO102B035Const.FILTERKOUKAN1TORITSUKEHONNSUU2:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getFilterkoukan1toritsukehonnsuu2());

            // ﾌｨﾙﾀｰ交換①_3次ﾌｨﾙﾀｰ品名
            case GXHDO102B035Const.FILTERKOUKAN13FILTERHINMEI:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getFilterkoukan13filterhinmei());

            // ﾌｨﾙﾀｰ交換①_LotNo3
            case GXHDO102B035Const.FILTERKOUKAN1LOTNO3:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getFilterkoukan1lotno3());

            // ﾌｨﾙﾀｰ交換①_取り付け本数3
            case GXHDO102B035Const.FILTERKOUKAN1TORITSUKEHONNSUU3:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getFilterkoukan1toritsukehonnsuu3());

            // ﾌｨﾙﾀｰ交換①_F/P再開日
            case GXHDO102B035Const.FILTERKOUKAN1FPSAIKAI_DAY:
                return DateUtil.formattedTimestamp(srSlipFpBaketsu.getFilterkoukan1fpsaikainichiji(), "yyMMdd");

            // ﾌｨﾙﾀｰ交換①_F/P再開時間
            case GXHDO102B035Const.FILTERKOUKAN1FPSAIKAI_TIME:
                return DateUtil.formattedTimestamp(srSlipFpBaketsu.getFilterkoukan1fpsaikainichiji(), "HHmm");

            // ﾌｨﾙﾀｰ交換①_担当者
            case GXHDO102B035Const.FILTERKOUKAN1TANTOUSYA:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getFilterkoukan1tantousya());

            // F/P終了日
            case GXHDO102B035Const.FPSYURYOU_DAY:
                return DateUtil.formattedTimestamp(srSlipFpBaketsu.getFpsyuryounichiji(), "yyMMdd");

            // F/P終了時間
            case GXHDO102B035Const.FPSYURYOU_TIME:
                return DateUtil.formattedTimestamp(srSlipFpBaketsu.getFpsyuryounichiji(), "HHmm");

            // F/P時間
            case GXHDO102B035Const.FPZIKAN:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getFpzikan());

            // ﾌｨﾙﾀｰﾊﾟｽ終了_担当者
            case GXHDO102B035Const.FILTERPASSSYUURYOUTANTOUSYA:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getFilterpasssyuuryoutantousya());

            // 総重量①
            case GXHDO102B035Const.SOUJYURYOU1:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getSoujyuryou1());

            // 総重量②
            case GXHDO102B035Const.SOUJYURYOU2:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getSoujyuryou2());

            // 総重量③
            case GXHDO102B035Const.SOUJYURYOU3:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getSoujyuryou3());

            // 総重量④
            case GXHDO102B035Const.SOUJYURYOU4:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getSoujyuryou4());

            // 総重量⑤
            case GXHDO102B035Const.SOUJYURYOU5:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getSoujyuryou5());

            // 総重量⑥
            case GXHDO102B035Const.SOUJYURYOU6:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getSoujyuryou6());

            // 正味重量①
            case GXHDO102B035Const.SYOUMIJYUURYOU1:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getSyoumijyuuryou1());

            // 正味重量②
            case GXHDO102B035Const.SYOUMIJYUURYOU2:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getSyoumijyuuryou2());

            // 正味重量③
            case GXHDO102B035Const.SYOUMIJYUURYOU3:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getSyoumijyuuryou3());

            // 正味重量④
            case GXHDO102B035Const.SYOUMIJYUURYOU4:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getSyoumijyuuryou4());

            // 正味重量⑤
            case GXHDO102B035Const.SYOUMIJYUURYOU5:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getSyoumijyuuryou5());

            // 正味重量⑥
            case GXHDO102B035Const.SYOUMIJYUURYOU6:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getSyoumijyuuryou6());

            // ｽﾘｯﾌﾟ重量合計
            case GXHDO102B035Const.SLIPJYUURYOUGOUKEI:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getSlipjyuuryougoukei());

            // 備考1
            case GXHDO102B035Const.BIKOU1:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getBikou1());

            // 備考2
            case GXHDO102B035Const.BIKOU2:
                return StringUtil.nullToBlank(srSlipFpBaketsu.getBikou2());

            default:
                return null;
        }
    }

    /**
     * ｽﾘｯﾌﾟ作製・FP(バケツ)_仮登録(tmp_sr_slip_fp_baketsu)登録処理(削除時)
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void insertDeleteDataTmpSrSlipFpBaketsu(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, String systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_slip_fp_baketsu ( "
                + " kojyo,lotno,edaban,jissekino,sliphinmei,sliplotno,lotkubun,genryoukigou,haisyutsuyouki,hutaijyuuryou1,hutaijyuuryou2,"
                + "hutaijyuuryou3,hutaijyuuryou4,hutaijyuuryou5,hutaijyuuryou6,hokanyoukijyunbitantousya,filterrenketsu,filtertoritsuke1filterhinmei,"
                + "filtertoritsukefilterlotno1,filtertoritsuketoritsukehonsuu1,filtertoritsuke2filterhinmei,filtertoritsukefilterlotno2,"
                + "filtertoritsuketoritsukehonsuu2,filtertoritsuketantousya,assoutankno,fpkaishinichiji,assouregulatorno,assouatsuryokukikaku,assouatsuryoku,"
                + "filterpasskaishitantousya,hozonyousamplekaisyuu,filterkoukan1fpteishinichiji,filterkoukan11filterhinmei,filterkoukan1lotno1,"
                + "filterkoukan1toritsukehonnsuu1,filterkoukan12filterhinmei,filterkoukan1lotno2,filterkoukan1toritsukehonnsuu2,filterkoukan13filterhinmei,"
                + "filterkoukan1lotno3,filterkoukan1toritsukehonnsuu3,filterkoukan1fpsaikainichiji,filterkoukan1tantousya,fpsyuryounichiji,"
                + "fpzikan,filterpasssyuuryoutantousya,soujyuryou1,soujyuryou2,soujyuryou3,soujyuryou4,soujyuryou5,soujyuryou6,syoumijyuuryou1,"
                + "syoumijyuuryou2,syoumijyuuryou3,syoumijyuuryou4,syoumijyuuryou5,syoumijyuuryou6,slipjyuuryougoukei,bikou1,bikou2,torokunichiji,"
                + "kosinnichiji,revision,deleteflag "
                + ") SELECT "
                + " kojyo,lotno,edaban,jissekino,sliphinmei,sliplotno,lotkubun,genryoukigou,haisyutsuyouki,hutaijyuuryou1,hutaijyuuryou2,"
                + "hutaijyuuryou3,hutaijyuuryou4,hutaijyuuryou5,hutaijyuuryou6,hokanyoukijyunbitantousya,filterrenketsu,filtertoritsuke1filterhinmei,"
                + "filtertoritsukefilterlotno1,filtertoritsuketoritsukehonsuu1,filtertoritsuke2filterhinmei,filtertoritsukefilterlotno2,"
                + "filtertoritsuketoritsukehonsuu2,filtertoritsuketantousya,assoutankno,fpkaishinichiji,assouregulatorno,assouatsuryokukikaku,assouatsuryoku,"
                + "filterpasskaishitantousya,hozonyousamplekaisyuu,filterkoukan1fpteishinichiji,filterkoukan11filterhinmei,filterkoukan1lotno1,"
                + "filterkoukan1toritsukehonnsuu1,filterkoukan12filterhinmei,filterkoukan1lotno2,filterkoukan1toritsukehonnsuu2,filterkoukan13filterhinmei,"
                + "filterkoukan1lotno3,filterkoukan1toritsukehonnsuu3,filterkoukan1fpsaikainichiji,filterkoukan1tantousya,fpsyuryounichiji,"
                + "fpzikan,filterpasssyuuryoutantousya,soujyuryou1,soujyuryou2,soujyuryou3,soujyuryou4,soujyuryou5,soujyuryou6,syoumijyuuryou1,"
                + "syoumijyuuryou2,syoumijyuuryou3,syoumijyuuryou4,syoumijyuuryou5,syoumijyuuryou6,slipjyuuryougoukei,bikou1,bikou2,?,?,?,? "
                + " FROM sr_slip_fp_baketsu "
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
