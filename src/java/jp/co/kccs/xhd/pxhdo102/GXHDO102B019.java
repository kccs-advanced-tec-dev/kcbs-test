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
import jp.co.kccs.xhd.db.model.SrYuudentaiTenkazai;
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
 * 変更日	2021/11/07<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B019(誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定)
 *
 * @author KCSS K.Jo
 * @since 2021/11/07
 */
public class GXHDO102B019 implements IFormLogic {

    private static final Logger LOGGER = Logger.getLogger(GXHDO102B019.class.getName());
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
    public GXHDO102B019() {
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
                    GXHDO102B019Const.BTN_EDABAN_COPY_TOP,
                    GXHDO102B019Const.BTN_KAKUHANKAISI_TOP,
                    GXHDO102B019Const.BTN_KAKUHANSYUURYOU_TOP,
                    GXHDO102B019Const.BTN_KANSOUKAISI1_TOP,
                    GXHDO102B019Const.BTN_KANSOUSYUURYOU1_TOP,
                    GXHDO102B019Const.BTN_KANSOUKAISI2_TOP,
                    GXHDO102B019Const.BTN_KANSOUSYUURYOU2_TOP,
                    GXHDO102B019Const.BTN_EDABAN_COPY_BOTTOM,
                    GXHDO102B019Const.BTN_KAKUHANKAISI_BOTTOM,
                    GXHDO102B019Const.BTN_KAKUHANSYUURYOU_BOTTOM,
                    GXHDO102B019Const.BTN_KANSOUKAISI1_BOTTOM,
                    GXHDO102B019Const.BTN_KANSOUSYUURYOU1_BOTTOM,
                    GXHDO102B019Const.BTN_KANSOUKAISI2_BOTTOM,
                    GXHDO102B019Const.BTN_KANSOUSYUURYOU2_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO102B019Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO102B019Const.BTN_INSERT_TOP,
                    GXHDO102B019Const.BTN_DELETE_TOP,
                    GXHDO102B019Const.BTN_UPDATE_TOP,
                    GXHDO102B019Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO102B019Const.BTN_INSERT_BOTTOM,
                    GXHDO102B019Const.BTN_DELETE_BOTTOM,
                    GXHDO102B019Const.BTN_UPDATE_BOTTOM
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
            case GXHDO102B019Const.BTN_EDABAN_COPY_TOP:
            case GXHDO102B019Const.BTN_EDABAN_COPY_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 仮登録
            case GXHDO102B019Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO102B019Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO102B019Const.BTN_INSERT_TOP:
            case GXHDO102B019Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 修正
            case GXHDO102B019Const.BTN_UPDATE_TOP:
            case GXHDO102B019Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO102B019Const.BTN_DELETE_TOP:
            case GXHDO102B019Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 添加材ｽﾗﾘｰ重量計算
            case GXHDO102B019Const.BTN_TENKAZAISLURRYJYURYO_TOP:
            case GXHDO102B019Const.BTN_TENKAZAISLURRYJYURYO_BOTTOM:
                method = "setTenkazaislurryjyuryo";
                break;
            // 撹拌開始日時
            case GXHDO102B019Const.BTN_KAKUHANKAISI_TOP:
            case GXHDO102B019Const.BTN_KAKUHANKAISI_BOTTOM:
                method = "setKakuhankaisinichiji";
                break;
            // 撹拌終了日時
            case GXHDO102B019Const.BTN_KAKUHANSYUURYOU_TOP:
            case GXHDO102B019Const.BTN_KAKUHANSYUURYOU_BOTTOM:
                method = "setKakuhansyuuryounichiji";
                break;
            // 乾燥開始日時①
            case GXHDO102B019Const.BTN_KANSOUKAISI1_TOP:
            case GXHDO102B019Const.BTN_KANSOUKAISI1_BOTTOM:
                method = "setKansoukaisinichiji1";
                break;
            // 乾燥終了日時①
            case GXHDO102B019Const.BTN_KANSOUSYUURYOU1_TOP:
            case GXHDO102B019Const.BTN_KANSOUSYUURYOU1_BOTTOM:
                method = "setKansousyuuryounichiji1";
                break;
            // 乾燥開始日時②
            case GXHDO102B019Const.BTN_KANSOUKAISI2_TOP:
            case GXHDO102B019Const.BTN_KANSOUKAISI2_BOTTOM:
                method = "setKansoukaisinichiji2";
                break;
            // 乾燥終了日時②
            case GXHDO102B019Const.BTN_KANSOUSYUURYOU2_TOP:
            case GXHDO102B019Const.BTN_KANSOUSYUURYOU2_BOTTOM:
                method = "setKansousyuuryounichiji2";
                break;
            // 乾燥後正味重量①計算
            case GXHDO102B019Const.BTN_KANSOUGOSYOUMI1_TOP:
            case GXHDO102B019Const.BTN_KANSOUGOSYOUMI1_BOTTOM:
                method = "setKansougosyoumi1";
                break;
            // 乾燥後正味重量②計算
            case GXHDO102B019Const.BTN_KANSOUGOSYOUMI2_TOP:
            case GXHDO102B019Const.BTN_KANSOUGOSYOUMI2_BOTTOM:
                method = "setKansougosyoumi2";
                break;
            // 乾燥後正味重量③計算
            case GXHDO102B019Const.BTN_KANSOUGOSYOUMI3_TOP:
            case GXHDO102B019Const.BTN_KANSOUGOSYOUMI3_BOTTOM:
                method = "setKansougosyoumi3";
                break;
            // 固形分比率平均計算
            case GXHDO102B019Const.BTN_KOKEIBUNHIRITUHEIKIN_TOP:
            case GXHDO102B019Const.BTN_KOKEIBUNHIRITUHEIKIN_BOTTOM:
                method = "setKokeibunhirituheikin";
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

            // 誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定の入力項目の登録データ(仮登録時は仮登録データ)を取得
            List<SrYuudentaiTenkazai> srYuudentaiTenkazaiDataList = getSrYuudentaiTenkazaiData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo9, oyalotEdaban);
            if (srYuudentaiTenkazaiDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // メイン画面データ設定
            setInputItemDataMainForm(processData, srYuudentaiTenkazaiDataList.get(0));

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
     * 【添加材ｽﾗﾘｰ重量計算】ﾎﾞﾀﾝ押下時処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setTenkazaislurryjyuryo(ProcessData processData) {

        FXHDD01 itemSoujyuuryou1 = getItemRow(processData.getItemList(), GXHDO102B019Const.SOUJYUURYOU1); // 総重量①
        FXHDD01 itemFuutaijyuuryou1 = getItemRow(processData.getItemList(), GXHDO102B019Const.FUUTAIJYUURYOU1); // 風袋重量①

        FXHDD01 itemSoujyuuryou2 = getItemRow(processData.getItemList(), GXHDO102B019Const.SOUJYUURYOU2); // 総重量②
        FXHDD01 itemFuutaijyuuryou2 = getItemRow(processData.getItemList(), GXHDO102B019Const.FUUTAIJYUURYOU2); // 風袋重量②

        FXHDD01 itemSoujyuuryou3 = getItemRow(processData.getItemList(), GXHDO102B019Const.SOUJYUURYOU3); // 総重量③
        FXHDD01 itemFuutaijyuuryou3 = getItemRow(processData.getItemList(), GXHDO102B019Const.FUUTAIJYUURYOU3); // 風袋重量③
        ErrorMessageInfo checkItemErrorInfo = checkTenkazaislurryjyuryo(itemSoujyuuryou1, itemFuutaijyuuryou1, itemSoujyuuryou2, itemFuutaijyuuryou2, itemSoujyuuryou3, itemFuutaijyuuryou3
        );
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        // 「添加材ｽﾗﾘｰ重量」計算処理
        calcTenkazaislurryjyuryo(processData, itemSoujyuuryou1, itemFuutaijyuuryou1, itemSoujyuuryou2, itemFuutaijyuuryou2, itemSoujyuuryou3, itemFuutaijyuuryou3);
        return processData;
    }

    /**
     * 【添加材ｽﾗﾘｰ重量計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
     *
     * @param itemSoujyuuryou1 総重量①
     * @param itemFuutaijyuuryou1 風袋重量①
     * @param itemSoujyuuryou2 総重量②
     * @param itemFuutaijyuuryou2 風袋重量②
     * @param itemSoujyuuryou3 総重量③
     * @param itemFuutaijyuuryou3 風袋重量③
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkTenkazaislurryjyuryo(FXHDD01 itemSoujyuuryou1, FXHDD01 itemFuutaijyuuryou1, FXHDD01 itemSoujyuuryou2, FXHDD01 itemFuutaijyuuryou2,
            FXHDD01 itemSoujyuuryou3, FXHDD01 itemFuutaijyuuryou3) {

        //「総重量①」ﾁｪｯｸ
        if (itemSoujyuuryou1 != null && StringUtil.isEmpty(itemSoujyuuryou1.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemSoujyuuryou1);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemSoujyuuryou1.getLabel1());
        }
        // 「風袋重量①」ﾁｪｯｸ
        if (itemFuutaijyuuryou1 != null && StringUtil.isEmpty(itemFuutaijyuuryou1.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemFuutaijyuuryou1);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemFuutaijyuuryou1.getLabel1());
        }
        if (itemSoujyuuryou1 != null && itemFuutaijyuuryou1 != null) {
            // [総重量①]<[風袋重量①]場合エラー
            BigDecimal soujyuuryou1 = new BigDecimal(itemSoujyuuryou1.getValue());
            BigDecimal fuutaijyuuryou1 = new BigDecimal(itemFuutaijyuuryou1.getValue());
            if (soujyuuryou1.compareTo(fuutaijyuuryou1) < 0) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemSoujyuuryou1, itemFuutaijyuuryou1);
                return MessageUtil.getErrorMessageInfo("XHD-000023", true, false, errFxhdd01List, itemSoujyuuryou1.getLabel1(), itemFuutaijyuuryou1.getLabel1());
            }
        }

        if (itemSoujyuuryou2 != null && itemFuutaijyuuryou2 != null) {
            //「総重量②」ﾁｪｯｸ
            if (StringUtil.isEmpty(itemSoujyuuryou2.getValue()) && !StringUtil.isEmpty(itemFuutaijyuuryou2.getValue())) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemSoujyuuryou2);
                return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemSoujyuuryou2.getLabel1());

            }
            // 「風袋重量②」ﾁｪｯｸ
            if (StringUtil.isEmpty(itemFuutaijyuuryou2.getValue()) && !StringUtil.isEmpty(itemSoujyuuryou2.getValue())) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemFuutaijyuuryou2);
                return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemFuutaijyuuryou2.getLabel1());
            }

            // [総重量②]<[風袋重量②]場合エラー
            if (!(StringUtil.isEmpty(itemSoujyuuryou2.getValue()) && StringUtil.isEmpty(itemFuutaijyuuryou2.getValue()))) {
                BigDecimal soujyuuryou2 = new BigDecimal(itemSoujyuuryou2.getValue());
                BigDecimal fuutaijyuuryou2 = new BigDecimal(itemFuutaijyuuryou2.getValue());
                if (soujyuuryou2.compareTo(fuutaijyuuryou2) < 0) {
                    // ｴﾗｰ項目をﾘｽﾄに追加
                    List<FXHDD01> errFxhdd01List = Arrays.asList(itemSoujyuuryou2, itemFuutaijyuuryou2);
                    return MessageUtil.getErrorMessageInfo("XHD-000023", true, false, errFxhdd01List, itemSoujyuuryou2.getLabel1(), itemFuutaijyuuryou2.getLabel1());
                }
            }
        }

        if (itemSoujyuuryou3 != null && itemFuutaijyuuryou3 != null) {
            //「総重量③」ﾁｪｯｸ
            if (StringUtil.isEmpty(itemSoujyuuryou3.getValue()) && !StringUtil.isEmpty(itemFuutaijyuuryou3.getValue())) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemSoujyuuryou3);
                return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemSoujyuuryou3.getLabel1());

            }
            // 「風袋重量③」ﾁｪｯｸ
            if (StringUtil.isEmpty(itemFuutaijyuuryou3.getValue()) && !StringUtil.isEmpty(itemSoujyuuryou3.getValue())) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemFuutaijyuuryou3);
                return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemFuutaijyuuryou3.getLabel1());
            }
            // [総重量③]<[風袋重量③]場合エラー
            if (!(StringUtil.isEmpty(itemSoujyuuryou3.getValue()) && StringUtil.isEmpty(itemFuutaijyuuryou3.getValue()))) {
                BigDecimal soujyuuryou3 = new BigDecimal(itemSoujyuuryou3.getValue());
                BigDecimal fuutaijyuuryou3 = new BigDecimal(itemFuutaijyuuryou3.getValue());
                if (soujyuuryou3.compareTo(fuutaijyuuryou3) < 0) {
                    // ｴﾗｰ項目をﾘｽﾄに追加
                    List<FXHDD01> errFxhdd01List = Arrays.asList(itemSoujyuuryou3, itemFuutaijyuuryou3);
                    return MessageUtil.getErrorMessageInfo("XHD-000023", true, false, errFxhdd01List, itemSoujyuuryou3.getLabel1(), itemFuutaijyuuryou3.getLabel1());
                }
            }
        }

        return null;
    }

    /**
     * 「添加材ｽﾗﾘｰ重量」計算処理
     *
     * @param processData 処理制御データ
     * @param itemSoujyuuryou1 総重量①
     * @param itemFuutaijyuuryou1 風袋重量①
     * @param itemSoujyuuryou2 総重量②
     * @param itemFuutaijyuuryou2 風袋重量②
     * @param itemSoujyuuryou3 総重量③
     * @param itemFuutaijyuuryou3 風袋重量③
     */
    private void calcTenkazaislurryjyuryo(ProcessData processData, FXHDD01 itemSoujyuuryou1, FXHDD01 itemFuutaijyuuryou1, FXHDD01 itemSoujyuuryou2, FXHDD01 itemFuutaijyuuryou2,
            FXHDD01 itemSoujyuuryou3, FXHDD01 itemFuutaijyuuryou3) {
        try {

            FXHDD01 itemTenkazaislurryjyuuryou = getItemRow(processData.getItemList(), GXHDO102B019Const.TENKAZAISLURRYJYUURYOU); // 添加材ｽﾗﾘｰ重量
            BigDecimal itemSoujyuuryou1Va1 = (BigDecimal) DBUtil.stringToBigDecimalObject(itemSoujyuuryou1.getValue()); // 総重量①
            BigDecimal itemFuutaijyuuryou1Va1 = (BigDecimal) DBUtil.stringToBigDecimalObject(itemFuutaijyuuryou1.getValue()); // 風袋重量①
            BigDecimal itemTenkazaislurryjyuuryouVal;
            if (itemSoujyuuryou2 != null && itemFuutaijyuuryou2 != null && itemSoujyuuryou3 != null && itemFuutaijyuuryou3 != null) {
                BigDecimal itemSoujyuuryou2Va1 = (BigDecimal) DBUtil.stringToBigDecimalObject(itemSoujyuuryou2.getValue()); // 総重量②
                BigDecimal itemFuutaijyuuryou2Va1 = (BigDecimal) DBUtil.stringToBigDecimalObject(itemFuutaijyuuryou2.getValue()); // 風袋重量②
                BigDecimal itemSoujyuuryou3Va1 = (BigDecimal) DBUtil.stringToBigDecimalObject(itemSoujyuuryou3.getValue()); // 総重量③
                BigDecimal itemFuutaijyuuryou3Va1 = (BigDecimal) DBUtil.stringToBigDecimalObject(itemFuutaijyuuryou3.getValue()); // 風袋重量③

                // (「総重量①」-「風袋重量①」) + (「総重量②」-「風袋重量②」) + (「総重量③」-「風袋重量③」) = 「添加材ｽﾗﾘｰ重量」(小数以下四捨五入)
                itemTenkazaislurryjyuuryouVal = ((itemSoujyuuryou1Va1.subtract(itemFuutaijyuuryou1Va1)).add(itemSoujyuuryou2Va1.subtract(itemFuutaijyuuryou2Va1))
                        .add(itemSoujyuuryou3Va1.subtract(itemFuutaijyuuryou3Va1))).setScale(0, RoundingMode.HALF_UP);
                itemTenkazaislurryjyuuryou.setValue(itemTenkazaislurryjyuuryouVal.toPlainString());
            } else {
                // (「総重量①」-「風袋重量①」) + (「総重量②」-「風袋重量②」) + (「総重量③」-「風袋重量③」) = 「添加材ｽﾗﾘｰ重量」(小数以下四捨五入)
                itemTenkazaislurryjyuuryouVal = (itemSoujyuuryou1Va1.subtract(itemFuutaijyuuryou1Va1)).setScale(0, RoundingMode.HALF_UP);
                itemTenkazaislurryjyuuryou.setValue(itemTenkazaislurryjyuuryouVal.toPlainString());
            }

        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
            ErrUtil.outputErrorLog("添加材ｽﾗﾘｰ重量計算にエラー発生", ex, LOGGER);
        }

    }

    /**
     * 撹拌開始日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKakuhankaisinichiji(ProcessData processData) {

        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B019Const.KAKUHANKAISI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B019Const.KAKUHANKAISI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 撹拌終了日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKakuhansyuuryounichiji(ProcessData processData) {

        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B019Const.KAKUHANSYUURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B019Const.KAKUHANSYUURYOU_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 乾燥開始日時①設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKansoukaisinichiji1(ProcessData processData) {

        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B019Const.KANSOUKAISI1_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B019Const.KANSOUKAISI1_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
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
    public ProcessData setKansousyuuryounichiji1(ProcessData processData) {

        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B019Const.KANSOUSYUURYOU1_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B019Const.KANSOUSYUURYOU1_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
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
    public ProcessData setKansoukaisinichiji2(ProcessData processData) {

        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B019Const.KANSOUKAISI2_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B019Const.KANSOUKAISI2_TIME);
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
    public ProcessData setKansousyuuryounichiji2(ProcessData processData) {

        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B019Const.KANSOUSYUURYOU2_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B019Const.KANSOUSYUURYOU2_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 【乾燥後正味重量①計算】ﾎﾞﾀﾝ押下時処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKansougosyoumi1(ProcessData processData) {

        FXHDD01 itemKansougosoujyuuryou1 = getItemRow(processData.getItemList(), GXHDO102B019Const.KANSOUGOSOUJYUURYOU1);// 乾燥後総重量①
        FXHDD01 itemArumizarafuutaijyuuryou1 = getItemRow(processData.getItemList(), GXHDO102B019Const.ARUMIZARAFUUTAIJYUURYOU1);// ｱﾙﾐ皿風袋重量①
        FXHDD01 itemKansoumaeslurryjyuuryou1 = getItemRow(processData.getItemList(), GXHDO102B019Const.KANSOUMAESLURRYJYUURYOU1);// 乾燥前ｽﾗﾘｰ重量①
        FXHDD01 itemKokeibunhiritu1 = getItemRow(processData.getItemList(), GXHDO102B019Const.KOKEIBUNHIRITU1); // 固形分比率①
        // 【乾燥後正味重量①計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkKansougosyoumi(itemKansougosoujyuuryou1, itemArumizarafuutaijyuuryou1, itemKansoumaeslurryjyuuryou1);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        // 「乾燥後正味重量①」計算処理
        calcKansougosyoumi(processData, GXHDO102B019Const.KANSOUGOSYOUMIJYUURYOU1, itemKansougosoujyuuryou1, itemArumizarafuutaijyuuryou1, itemKansoumaeslurryjyuuryou1, itemKokeibunhiritu1);
        return processData;
    }

    /**
     * 【乾燥後正味重量②計算】ﾎﾞﾀﾝ押下時処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKansougosyoumi2(ProcessData processData) {

        FXHDD01 itemKansougosoujyuuryou2 = getItemRow(processData.getItemList(), GXHDO102B019Const.KANSOUGOSOUJYUURYOU2);// 乾燥後総重量②
        FXHDD01 itemArumizarafuutaijyuuryou2 = getItemRow(processData.getItemList(), GXHDO102B019Const.ARUMIZARAFUUTAIJYUURYOU2);// ｱﾙﾐ皿風袋重量②
        FXHDD01 itemKansoumaeslurryjyuuryou2 = getItemRow(processData.getItemList(), GXHDO102B019Const.KANSOUMAESLURRYJYUURYOU2);// 乾燥前ｽﾗﾘｰ重量②
        FXHDD01 itemKokeibunhiritu2 = getItemRow(processData.getItemList(), GXHDO102B019Const.KOKEIBUNHIRITU2); // 固形分比率②
        // 【乾燥後正味重量②計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkKansougosyoumi(itemKansougosoujyuuryou2, itemArumizarafuutaijyuuryou2, itemKansoumaeslurryjyuuryou2);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        // 「乾燥後正味重量②」計算処理
        calcKansougosyoumi(processData, GXHDO102B019Const.KANSOUGOSYOUMIJYUURYOU2, itemKansougosoujyuuryou2, itemArumizarafuutaijyuuryou2, itemKansoumaeslurryjyuuryou2, itemKokeibunhiritu2);
        return processData;
    }

    /**
     * 【乾燥後正味重量③計算】ﾎﾞﾀﾝ押下時処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKansougosyoumi3(ProcessData processData) {

        FXHDD01 itemKansougosoujyuuryou3 = getItemRow(processData.getItemList(), GXHDO102B019Const.KANSOUGOSOUJYUURYOU3);// 乾燥後総重量③
        FXHDD01 itemArumizarafuutaijyuuryou3 = getItemRow(processData.getItemList(), GXHDO102B019Const.ARUMIZARAFUUTAIJYUURYOU3);// ｱﾙﾐ皿風袋重量③
        FXHDD01 itemKansoumaeslurryjyuuryou3 = getItemRow(processData.getItemList(), GXHDO102B019Const.KANSOUMAESLURRYJYUURYOU3);// 乾燥前ｽﾗﾘｰ重量③
        FXHDD01 itemKokeibunhiritu3 = getItemRow(processData.getItemList(), GXHDO102B019Const.KOKEIBUNHIRITU3); // 固形分比率③
        if (itemKansougosoujyuuryou3 != null && itemArumizarafuutaijyuuryou3 != null && itemKansoumaeslurryjyuuryou3 != null) {
            // 【乾燥後正味重量②計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
            ErrorMessageInfo checkItemErrorInfo = checkKansougosyoumi(itemKansougosoujyuuryou3, itemArumizarafuutaijyuuryou3, itemKansoumaeslurryjyuuryou3);
            if (checkItemErrorInfo != null) {
                processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
                return processData;
            }
            processData.setMethod("");
            // 「乾燥後正味重量③」計算処理
            calcKansougosyoumi(processData, GXHDO102B019Const.KANSOUGOSYOUMIJYUURYOU3, itemKansougosoujyuuryou3, itemArumizarafuutaijyuuryou3, itemKansoumaeslurryjyuuryou3, itemKokeibunhiritu3);
        }

        return processData;
    }

    /**
     * 【乾燥後正味重量計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
     *
     * @param itemKansougosoujyuuryou 乾燥後総重量
     * @param itemArumizarafuutaijyuuryou ｱﾙﾐ皿風袋重量
     * @param itemKansoumaeslurryjyuuryou 乾燥前ｽﾗﾘｰ重量
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkKansougosyoumi(FXHDD01 itemKansougosoujyuuryou, FXHDD01 itemArumizarafuutaijyuuryou, FXHDD01 itemKansoumaeslurryjyuuryou) {

        // 「乾燥後総重量」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemKansougosoujyuuryou.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemKansougosoujyuuryou);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemKansougosoujyuuryou.getLabel1());
        }
        // 「ｱﾙﾐ皿風袋重量」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemArumizarafuutaijyuuryou.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemArumizarafuutaijyuuryou);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemArumizarafuutaijyuuryou.getLabel1());
        }
        // [乾燥後総重量]<[ｱﾙﾐ皿風袋重量]場合エラー
        BigDecimal kansougosoujyuuryou = new BigDecimal(itemKansougosoujyuuryou.getValue());
        BigDecimal arumizarafuutaijyuuryou = new BigDecimal(itemArumizarafuutaijyuuryou.getValue());
        if (kansougosoujyuuryou.compareTo(arumizarafuutaijyuuryou) < 0) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemKansougosoujyuuryou, itemArumizarafuutaijyuuryou);
            return MessageUtil.getErrorMessageInfo("XHD-000023", true, false, errFxhdd01List, itemKansougosoujyuuryou.getLabel1(), itemArumizarafuutaijyuuryou.getLabel1());
        }

        if (itemKansoumaeslurryjyuuryou.getValue() == null || new BigDecimal(itemKansoumaeslurryjyuuryou.getValue()).compareTo(BigDecimal.ZERO) == 0) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemKansoumaeslurryjyuuryou);
            return MessageUtil.getErrorMessageInfo("XHD-000181", true, true, errFxhdd01List, itemKansoumaeslurryjyuuryou.getLabel1());
        }
        return null;
    }

    /**
     * 【乾燥後正味重量計算】ﾎﾞﾀﾝ押下時計算処理
     *
     * @param processData 処理制御データ
     * @param kansougosyoumijyuuryou1ItemId 乾燥後正味重量項目ID
     * @param itemKansougosoujyuuryou 乾燥後総重量
     * @param itemArumizarafuutaijyuuryou ｱﾙﾐ皿風袋重量
     * @param itemKansoumaeslurryjyuuryou 乾燥後総重量
     * @param itemKokeibunhiritu 固形分比率
     */
    private void calcKansougosyoumi(ProcessData processData, String kansougosyoumijyuuryouItemId, FXHDD01 itemKansougosoujyuuryou, FXHDD01 itemArumizarafuutaijyuuryou,
            FXHDD01 itemKansoumaeslurryjyuuryou, FXHDD01 itemKokeibunhiritu) {

        try {
            FXHDD01 itemKansougosyoumijyuuryou = getItemRow(processData.getItemList(), kansougosyoumijyuuryouItemId); // 乾燥後正味重量
            // 「乾燥後総重量」-「ｱﾙﾐ皿風袋重量」=「乾燥後正味重量」
            BigDecimal itemKansougosoujyuuryouVal = new BigDecimal(itemKansougosoujyuuryou.getValue());
            BigDecimal itemArumizarafuutaijyuuryouVal = new BigDecimal(itemArumizarafuutaijyuuryou.getValue());
            itemKansougosyoumijyuuryou.setValue(itemKansougosoujyuuryouVal.subtract(itemArumizarafuutaijyuuryouVal).toPlainString());

            // 「乾燥後正味重量」 ÷ 「乾燥前ｽﾗﾘｰ重量」 ×100 = 「固形分比率」
            BigDecimal itemKansougosyoumijyuuryouVal = new BigDecimal(itemKansougosyoumijyuuryou.getValue());
            BigDecimal itemKansoumaeslurryjyuuryouVal = new BigDecimal(itemKansoumaeslurryjyuuryou.getValue());
            BigDecimal kokeibunhirituVal = itemKansougosyoumijyuuryouVal.multiply(BigDecimal.valueOf(100)).divide(itemKansoumaeslurryjyuuryouVal, 4, RoundingMode.HALF_UP);
            itemKokeibunhiritu.setValue(kokeibunhirituVal.toPlainString());
        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
            ErrUtil.outputErrorLog("乾燥後正味重量計算にエラー発生", ex, LOGGER);
        }

    }

    /**
     * 【固形分比率平均計算】ﾎﾞﾀﾝ押下時処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKokeibunhirituheikin(ProcessData processData) {
        FXHDD01 itemKokeibunhiritu1 = getItemRow(processData.getItemList(), GXHDO102B019Const.KOKEIBUNHIRITU1);// 固形分比率①
        FXHDD01 itemKokeibunhiritu2 = getItemRow(processData.getItemList(), GXHDO102B019Const.KOKEIBUNHIRITU2);// 固形分比率②
        FXHDD01 itemKokeibunhiritu3 = getItemRow(processData.getItemList(), GXHDO102B019Const.KOKEIBUNHIRITU3);// 固形分比率③
        // 【固形分比率平均計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkKokeibunhirituheikin(itemKokeibunhiritu1, itemKokeibunhiritu2);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        // 「固形分比率平均」計算処理
        calcKokeibunhirituheikin(processData, itemKokeibunhiritu1, itemKokeibunhiritu2, itemKokeibunhiritu3);
        return processData;
    }

    /**
     * 【固形分比率平均計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
     *
     * @param itemKokeibunhiritu1 固形分比率①
     * @param itemKokeibunhiritu2 固形分比率②
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkKokeibunhirituheikin(FXHDD01 itemKokeibunhiritu1, FXHDD01 itemKokeibunhiritu2) {

        // 「固形分比率①」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemKokeibunhiritu1.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemKokeibunhiritu1);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemKokeibunhiritu1.getLabel1());
        }
        // 「固形分比率②」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemKokeibunhiritu2.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemKokeibunhiritu2);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemKokeibunhiritu2.getLabel1());
        }
        return null;
    }

    /**
     * 【固形分比率平均計算】ﾎﾞﾀﾝ押下時計算処理
     *
     * @param processData 処理制御データ
     * @param itemKokeibunhiritu1 固形分比率①
     * @param itemKokeibunhiritu2 固形分比率②
     * @param itemKokeibunhiritu3 固形分比率③
     */
    private void calcKokeibunhirituheikin(ProcessData processData, FXHDD01 itemKokeibunhiritu1, FXHDD01 itemKokeibunhiritu2, FXHDD01 itemKokeibunhiritu3) {

        try {
            FXHDD01 itemKokeibunhirituheikin = getItemRow(processData.getItemList(), GXHDO102B019Const.KOKEIBUNHIRITUHEIKIN); // 固形分比率平均
            BigDecimal itemKokeibunhiritu1Val = new BigDecimal(itemKokeibunhiritu1.getValue());
            BigDecimal itemKokeibunhiritu2Val = new BigDecimal(itemKokeibunhiritu2.getValue());
            if (itemKokeibunhiritu3 == null || StringUtil.isEmpty(itemKokeibunhiritu3.getValue())) {
                // 「固形分比率③」が入力されていない場合、(「固形分比率①」+「固形分比率②」)÷2 = 固形分比率平均
                BigDecimal itemKokeibunhirituheikinVal = itemKokeibunhiritu1Val.add(itemKokeibunhiritu2Val).divide(BigDecimal.valueOf(2), 3, RoundingMode.HALF_UP);
                itemKokeibunhirituheikin.setValue(itemKokeibunhirituheikinVal.toPlainString());
            } else {
                // 「固形分比率③」が入力されている場合、(「固形分比率①」+「固形分比率②」+「固形分比率③」)÷3 = 固形分比率平均
                BigDecimal itemKokeibunhiritu3Val = new BigDecimal(itemKokeibunhiritu3.getValue());
                BigDecimal itemKokeibunhirituheikinVal = itemKokeibunhiritu1Val.add(itemKokeibunhiritu2Val).add(itemKokeibunhiritu3Val).divide(BigDecimal.valueOf(3), 3, RoundingMode.HALF_UP);
                itemKokeibunhirituheikin.setValue(itemKokeibunhirituheikinVal.toPlainString());
            }

        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
            ErrUtil.outputErrorLog("固形分比率平均計算にエラー発生", ex, LOGGER);
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
        // [固形分比率①]と[固形分比率②]と[固形分比率③]差値のチェック
        ErrorMessageInfo checkItemErrorInfo = checkKokeibunhiritu(processData);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        // 後続処理メソッド設定
        processData.setMethod("doTempRegist");
        return processData;
    }

    /**
     * [固形分比率①]と[固形分比率②]と[固形分比率③]差値のチェック
     *
     * @param processData 処理制御データ
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkKokeibunhiritu(ProcessData processData) {

        FXHDD01 itemKokeibunhiritu1 = getItemRow(processData.getItemList(), GXHDO102B019Const.KOKEIBUNHIRITU1);// 固形分比率①
        FXHDD01 itemKokeibunhiritu2 = getItemRow(processData.getItemList(), GXHDO102B019Const.KOKEIBUNHIRITU2);// 固形分比率②
        FXHDD01 itemKokeibunhiritu3 = getItemRow(processData.getItemList(), GXHDO102B019Const.KOKEIBUNHIRITU3);// 固形分比率③
        if (itemKokeibunhiritu1 != null && itemKokeibunhiritu2 != null) {
            BigDecimal itemKokeibunhiritu1Val = (BigDecimal) DBUtil.stringToBigDecimalObject(itemKokeibunhiritu1.getValue());
            BigDecimal itemKokeibunhiritu2Val = (BigDecimal) DBUtil.stringToBigDecimalObject(itemKokeibunhiritu2.getValue());
            BigDecimal compareValue = new BigDecimal(0.05).setScale(2, BigDecimal.ROUND_DOWN);
            // [固形分比率①]と[固形分比率②]の差が0.05％以上ある場合ｴﾗｰ。
            if (!StringUtil.isEmpty(itemKokeibunhiritu1.getValue()) && !StringUtil.isEmpty(itemKokeibunhiritu2.getValue())) {
                String kokeibunhirituDiffVal1 = (itemKokeibunhiritu1Val.subtract(itemKokeibunhiritu2Val)).toString().replace("-", "");
                if (new BigDecimal(kokeibunhirituDiffVal1).compareTo(compareValue) > -1) {
                    // ｴﾗｰ項目をﾘｽﾄに追加
                    List<FXHDD01> errFxhdd01List = Arrays.asList(itemKokeibunhiritu2);
                    return MessageUtil.getErrorMessageInfo("", "差が0.05％以上あります。再測定を実施してください。", true, false, errFxhdd01List);
                }
            }

            if (itemKokeibunhiritu3 != null) {
                BigDecimal itemKokeibunhiritu3Val = (BigDecimal) DBUtil.stringToBigDecimalObject(itemKokeibunhiritu3.getValue());
                String kokeibunhirituDiffVal2 = (itemKokeibunhiritu1Val.subtract(itemKokeibunhiritu3Val)).toString().replace("-", "");
                String kokeibunhirituDiffVal3 = (itemKokeibunhiritu2Val.subtract(itemKokeibunhiritu3Val)).toString().replace("-", "");
                // [固形分比率①]と[固形分比率②]と[固形分比率③]の差が0.05％以上ある場合ｴﾗｰ。
                if (!StringUtil.isEmpty(itemKokeibunhiritu1.getValue()) && !StringUtil.isEmpty(itemKokeibunhiritu3.getValue())) {

                    if (new BigDecimal(kokeibunhirituDiffVal2).compareTo(compareValue) > -1) {
                        // ｴﾗｰ項目をﾘｽﾄに追加
                        List<FXHDD01> errFxhdd01List = Arrays.asList(itemKokeibunhiritu3);
                        return MessageUtil.getErrorMessageInfo("", "差が0.05％以上あります。再測定を実施してください。", true, false, errFxhdd01List);
                    }
                }
                if (!StringUtil.isEmpty(itemKokeibunhiritu2.getValue()) && !StringUtil.isEmpty(itemKokeibunhiritu3.getValue())) {
                    if (new BigDecimal(kokeibunhirituDiffVal3).compareTo(compareValue) > -1) {
                        // ｴﾗｰ項目をﾘｽﾄに追加
                        List<FXHDD01> errFxhdd01List = Arrays.asList(itemKokeibunhiritu3);
                        return MessageUtil.getErrorMessageInfo("", "差が0.05％以上あります。再測定を実施してください。", true, false, errFxhdd01List);
                    }
                }
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

                // 誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定_仮登録登録処理
                insertTmpSrYuudentaiTenkazai(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, strSystime, processData);
            } else {

                // 誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定_仮登録更新処理
                updateTmpSrYuudentaiTenkazai(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);
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
        // [固形分比率①]と[固形分比率②]と[固形分比率③]差値のチェック
        ErrorMessageInfo checkItemKokeibunhirituErrorInfo = checkKokeibunhiritu(processData);
        if (checkItemKokeibunhirituErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemKokeibunhirituErrorInfo));
            return processData;
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
            SrYuudentaiTenkazai tmpSrYuudentaiTenkazai = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrYuudentaiTenkazai> srYuudentaiTenkazaiList = getSrYuudentaiTenkazaiData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo9, edaban);
                if (!srYuudentaiTenkazaiList.isEmpty()) {
                    tmpSrYuudentaiTenkazai = srYuudentaiTenkazaiList.get(0);
                }

                deleteTmpSrYuudentaiTenkazai(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);
            }

            // 誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定_登録処理
            insertSrYuudentaiTenkazai(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, strSystime, processData, tmpSrYuudentaiTenkazai);

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

        // [固形分比率①]と[固形分比率②]と[固形分比率③]差値のチェック
        ErrorMessageInfo checkItemKokeibunhirituErrorInfo = checkKokeibunhiritu(processData);
        if (checkItemKokeibunhirituErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemKokeibunhirituErrorInfo));
            return processData;
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
        processData.setUserAuthParam(GXHDO102B019Const.USER_AUTH_UPDATE_PARAM);

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

            // 誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定_更新処理
            updateSrYuudentaiTenkazai(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);

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
        FXHDD01 itemKakuhankaisiDay = getItemRow(processData.getItemList(), GXHDO102B019Const.KAKUHANKAISI_DAY);  // 撹拌開始日
        FXHDD01 itemKakuhankaisiTime = getItemRow(processData.getItemList(), GXHDO102B019Const.KAKUHANKAISI_TIME); // 撹拌開始時間
        // 画面.撹拌開始日 + 画面.撹拌開始時間
        Date kakuhankaisiDate = DateUtil.convertStringToDate(itemKakuhankaisiDay.getValue(), itemKakuhankaisiTime.getValue());
        FXHDD01 itemKakuhansyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B019Const.KAKUHANSYUURYOU_DAY);    // 撹拌終了日
        FXHDD01 itemKakuhansyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B019Const.KAKUHANSYUURYOU_TIME); // 撹拌終了時間
        // 画面.撹拌終了日 + 画面.撹拌終了時間
        Date kakuhansyuuryouDate = DateUtil.convertStringToDate(itemKakuhansyuuryouDay.getValue(), itemKakuhansyuuryouTime.getValue());
        // R001チェック呼出し
        String msgKakuhankaisiCheckR001 = validateUtil.checkR001("撹拌開始日時", kakuhankaisiDate, "撹拌終了日時", kakuhansyuuryouDate);
        if (!StringUtil.isEmpty(msgKakuhankaisiCheckR001)) {
            // エラー発生時
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemKakuhankaisiDay, itemKakuhankaisiTime, itemKakuhansyuuryouDay, itemKakuhansyuuryouTime);
            return MessageUtil.getErrorMessageInfo("", msgKakuhankaisiCheckR001, true, true, errFxhdd01List);
        }

        FXHDD01 itemKansoukaisi1Day = getItemRow(processData.getItemList(), GXHDO102B019Const.KANSOUKAISI1_DAY);  // 乾燥開始日①
        FXHDD01 itemKansoukaisi1Time = getItemRow(processData.getItemList(), GXHDO102B019Const.KANSOUKAISI1_TIME); // 乾燥開始時間①
        // 画面.乾燥開始日① + 画面.乾燥開始時間①
        Date kansoukaisi1Date = DateUtil.convertStringToDate(itemKansoukaisi1Day.getValue(), itemKansoukaisi1Time.getValue());
        FXHDD01 itemKansousyuuryou1Day = getItemRow(processData.getItemList(), GXHDO102B019Const.KANSOUSYUURYOU1_DAY);    // 乾燥終了日①
        FXHDD01 itemKansousyuuryou1Time = getItemRow(processData.getItemList(), GXHDO102B019Const.KANSOUSYUURYOU1_TIME); // 乾燥終了時間①
        // 画面.乾燥終了日① + 画面.乾燥終了時間①
        Date kansousyuuryou1Date = DateUtil.convertStringToDate(itemKansousyuuryou1Day.getValue(), itemKansousyuuryou1Time.getValue());
        // R001チェック呼出し
        String msgKansoukaisi1CheckR001 = validateUtil.checkR001("乾燥開始日時①", kansoukaisi1Date, "乾燥終了日時①", kansousyuuryou1Date);
        if (!StringUtil.isEmpty(msgKansoukaisi1CheckR001)) {
            // エラー発生時
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemKansoukaisi1Day, itemKansoukaisi1Time, itemKansousyuuryou1Day, itemKansousyuuryou1Time);
            return MessageUtil.getErrorMessageInfo("", msgKansoukaisi1CheckR001, true, true, errFxhdd01List);
        }

        FXHDD01 itemKansoukaisi2Day = getItemRow(processData.getItemList(), GXHDO102B019Const.KANSOUKAISI2_DAY);  // 乾燥開始日②
        FXHDD01 itemKansoukaisi2Time = getItemRow(processData.getItemList(), GXHDO102B019Const.KANSOUKAISI2_TIME); // 乾燥開始時間②
        FXHDD01 itemKansousyuuryou2Day = getItemRow(processData.getItemList(), GXHDO102B019Const.KANSOUSYUURYOU2_DAY);    // 乾燥終了日②
        FXHDD01 itemKansousyuuryou2Time = getItemRow(processData.getItemList(), GXHDO102B019Const.KANSOUSYUURYOU2_TIME); // 乾燥終了時間②
        if (itemKansoukaisi2Day != null && itemKansoukaisi2Time != null && itemKansousyuuryou2Day != null && itemKansousyuuryou2Time != null) {
            // 画面.乾燥開始日②+ 画面.乾燥開始時間②
            Date kansoukaisi2Date = DateUtil.convertStringToDate(itemKansoukaisi2Day.getValue(), itemKansoukaisi2Time.getValue());
            // 画面.乾燥終了日② + 画面.乾燥終了時間②
            Date kansousyuuryou2Date = DateUtil.convertStringToDate(itemKansousyuuryou2Day.getValue(), itemKansousyuuryou2Time.getValue());
            // R001チェック呼出し
            String msgKansoukaisi2CheckR001 = validateUtil.checkR001("乾燥開始日時②", kansoukaisi2Date, "乾燥終了日時②", kansousyuuryou2Date);
            if (!StringUtil.isEmpty(msgKansoukaisi2CheckR001)) {
                // エラー発生時
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemKansoukaisi2Day, itemKansoukaisi2Time, itemKansousyuuryou2Day, itemKansousyuuryou2Time);
                return MessageUtil.getErrorMessageInfo("", msgKansoukaisi2CheckR001, true, true, errFxhdd01List);
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

        // 撹拌時間ﾁｪｯｸ
        List<String> kakuhankaisiList = Arrays.asList(GXHDO102B019Const.KAKUHANKAISI_DAY, GXHDO102B019Const.KAKUHANKAISI_TIME);
        List<String> kakuhansyuuryouList = Arrays.asList(GXHDO102B019Const.KAKUHANSYUURYOU_DAY, GXHDO102B019Const.KAKUHANSYUURYOU_TIME);

        // 乾燥時間①ﾁｪｯｸ
        List<String> kansoukaisi1List = Arrays.asList(GXHDO102B019Const.KANSOUKAISI1_DAY, GXHDO102B019Const.KANSOUKAISI1_TIME);
        List<String> kansousyuuryou1List = Arrays.asList(GXHDO102B019Const.KANSOUSYUURYOU1_DAY, GXHDO102B019Const.KANSOUSYUURYOU1_TIME);

        // 乾燥時間②ﾁｪｯｸ
        List<String> kansoukaisi2List = Arrays.asList(GXHDO102B019Const.KANSOUKAISI2_DAY, GXHDO102B019Const.KANSOUKAISI2_TIME);
        List<String> kansousyuuryou2List = Arrays.asList(GXHDO102B019Const.KANSOUSYUURYOU2_DAY, GXHDO102B019Const.KANSOUSYUURYOU2_TIME);

        // 規格値の入力値チェック必要の項目リスト
        List<FXHDD01> itemList = new ArrayList<>();
        // 撹拌時間の時間差数
        FXHDD01 kakuhajikanDiffHours = getDiffMinutes(processData, GXHDO102B019Const.KAKUHANJIKAN, kakuhankaisiList, kakuhansyuuryouList);
        // 乾燥時間①の時間差数
        FXHDD01 kansoujikan1diffHours = getDiffMinutes(processData, GXHDO102B019Const.KANSOUJIKAN1, kansoukaisi1List, kansousyuuryou1List);
        // 乾燥時間②の時間差数
        FXHDD01 kansoujikan2diffHours = getDiffMinutes(processData, GXHDO102B019Const.KANSOUJIKAN2, kansoukaisi2List, kansousyuuryou2List);
        String kakuhajikan = "撹拌時間";
        String kansoujikan1 = "乾燥時間①";
        String kansoujikan2 = "乾燥時間②";
        // 項目の項目名を設置
        if (kakuhajikanDiffHours != null) {
            kakuhajikanDiffHours.setLabel1(kakuhajikan);
            itemList.add(kakuhajikanDiffHours);
        }
        // 項目の項目名を設置
        if (kansoujikan1diffHours != null) {
            kansoujikan1diffHours.setLabel1(kansoujikan1);
            itemList.add(kansoujikan1diffHours);
        }
        // 項目の項目名を設置
        if (kansoujikan2diffHours != null) {
            kansoujikan2diffHours.setLabel1(kansoujikan2);
            itemList.add(kansoujikan2diffHours);
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
        setItemBackColor(processData, kakuhankaisiList, kakuhansyuuryouList, kakuhajikan, kikakuchiInputErrorInfoList, errorMessageInfo);
        setItemBackColor(processData, kansoukaisi1List, kansousyuuryou1List, kansoujikan1, kikakuchiInputErrorInfoList, errorMessageInfo);
        setItemBackColor(processData, kansoukaisi2List, kansousyuuryou2List, kansoujikan2, kikakuchiInputErrorInfoList, errorMessageInfo);

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
     * @param kakuhankaisiList 開始時間項目リスト
     * @param kakuhansyuuryouList 終了時間項目リスト
     * @param label 項目の項目名
     * @param kikakuchiInputErrorInfoList 規格値入力エラー情報リスト
     * @param errorMessageInfo エラーメッセージ情報
     */
    private void setItemBackColor(ProcessData processData, List<String> kakuhankaisiList, List<String> kakuhansyuuryouList, String label, List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList,
            ErrorMessageInfo errorMessageInfo) {

        List<String> errorItemLabelList = new ArrayList<>();
        // エラー項目の背景色を設定
        List<String> allTyougouryouList = new ArrayList<>();
        allTyougouryouList.addAll(kakuhankaisiList);
        allTyougouryouList.addAll(kakuhansyuuryouList);

        kikakuchiInputErrorInfoList.stream().forEachOrdered((errorInfo) -> {
            errorItemLabelList.add(errorInfo.getItemLabel());
        });
        if (errorMessageInfo != null && !errorMessageInfo.getErrorMessage().contains(label)) {
            return;
        }
        if (errorItemLabelList.contains(label) || (errorMessageInfo != null && errorMessageInfo.getErrorMessage().contains(label))) {
            FXHDD01 item1Fxhdd01 = getItemRow(processData.getItemList(), allTyougouryouList.get(0));
            FXHDD01 item2Fxhdd01 = getItemRow(processData.getItemList(), allTyougouryouList.get(1));
            FXHDD01 item3Fxhdd01 = getItemRow(processData.getItemList(), allTyougouryouList.get(2));
            FXHDD01 item4Fxhdd01 = getItemRow(processData.getItemList(), allTyougouryouList.get(3));
            item1Fxhdd01.setBackColorInput(ErrUtil.ERR_BACK_COLOR);
            item2Fxhdd01.setBackColorInput(ErrUtil.ERR_BACK_COLOR);
            item3Fxhdd01.setBackColorInput(ErrUtil.ERR_BACK_COLOR);
            item4Fxhdd01.setBackColorInput(ErrUtil.ERR_BACK_COLOR);
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
        processData.setUserAuthParam(GXHDO102B019Const.USER_AUTH_DELETE_PARAM);

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

            // 誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo9, edaban);
            insertDeleteDataTmpSrYuudentaiTenkazai(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo9, edaban, strSystime);

            // 誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定_削除処理
            deleteSrYuudentaiTenkazai(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);

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
                        GXHDO102B019Const.BTN_UPDATE_TOP,
                        GXHDO102B019Const.BTN_DELETE_TOP,
                        GXHDO102B019Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B019Const.BTN_TENKAZAISLURRYJYURYO_TOP,
                        GXHDO102B019Const.BTN_KAKUHANKAISI_TOP,
                        GXHDO102B019Const.BTN_KAKUHANSYUURYOU_TOP,
                        GXHDO102B019Const.BTN_KANSOUKAISI1_TOP,
                        GXHDO102B019Const.BTN_KANSOUSYUURYOU1_TOP,
                        GXHDO102B019Const.BTN_KANSOUKAISI2_TOP,
                        GXHDO102B019Const.BTN_KANSOUSYUURYOU2_TOP,
                        GXHDO102B019Const.BTN_KANSOUGOSYOUMI1_TOP,
                        GXHDO102B019Const.BTN_KANSOUGOSYOUMI2_TOP,
                        GXHDO102B019Const.BTN_KANSOUGOSYOUMI3_TOP,
                        GXHDO102B019Const.BTN_KOKEIBUNHIRITUHEIKIN_TOP,
                        GXHDO102B019Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B019Const.BTN_DELETE_BOTTOM,
                        GXHDO102B019Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B019Const.BTN_TENKAZAISLURRYJYURYO_BOTTOM,
                        GXHDO102B019Const.BTN_KAKUHANKAISI_BOTTOM,
                        GXHDO102B019Const.BTN_KAKUHANSYUURYOU_BOTTOM,
                        GXHDO102B019Const.BTN_KANSOUKAISI1_BOTTOM,
                        GXHDO102B019Const.BTN_KANSOUSYUURYOU1_BOTTOM,
                        GXHDO102B019Const.BTN_KANSOUKAISI2_BOTTOM,
                        GXHDO102B019Const.BTN_KANSOUSYUURYOU2_BOTTOM,
                        GXHDO102B019Const.BTN_KANSOUGOSYOUMI1_BOTTOM,
                        GXHDO102B019Const.BTN_KANSOUGOSYOUMI2_BOTTOM,
                        GXHDO102B019Const.BTN_KANSOUGOSYOUMI3_BOTTOM,
                        GXHDO102B019Const.BTN_KOKEIBUNHIRITUHEIKIN_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B019Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B019Const.BTN_INSERT_TOP,
                        GXHDO102B019Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B019Const.BTN_INSERT_BOTTOM));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO102B019Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B019Const.BTN_INSERT_TOP,
                        GXHDO102B019Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B019Const.BTN_TENKAZAISLURRYJYURYO_TOP,
                        GXHDO102B019Const.BTN_KAKUHANKAISI_TOP,
                        GXHDO102B019Const.BTN_KAKUHANSYUURYOU_TOP,
                        GXHDO102B019Const.BTN_KANSOUKAISI1_TOP,
                        GXHDO102B019Const.BTN_KANSOUSYUURYOU1_TOP,
                        GXHDO102B019Const.BTN_KANSOUKAISI2_TOP,
                        GXHDO102B019Const.BTN_KANSOUSYUURYOU2_TOP,
                        GXHDO102B019Const.BTN_KANSOUGOSYOUMI1_TOP,
                        GXHDO102B019Const.BTN_KANSOUGOSYOUMI2_TOP,
                        GXHDO102B019Const.BTN_KANSOUGOSYOUMI3_TOP,
                        GXHDO102B019Const.BTN_KOKEIBUNHIRITUHEIKIN_TOP,
                        GXHDO102B019Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B019Const.BTN_INSERT_BOTTOM,
                        GXHDO102B019Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B019Const.BTN_TENKAZAISLURRYJYURYO_BOTTOM,
                        GXHDO102B019Const.BTN_KAKUHANKAISI_BOTTOM,
                        GXHDO102B019Const.BTN_KAKUHANSYUURYOU_BOTTOM,
                        GXHDO102B019Const.BTN_KANSOUKAISI1_BOTTOM,
                        GXHDO102B019Const.BTN_KANSOUSYUURYOU1_BOTTOM,
                        GXHDO102B019Const.BTN_KANSOUKAISI2_BOTTOM,
                        GXHDO102B019Const.BTN_KANSOUSYUURYOU2_BOTTOM,
                        GXHDO102B019Const.BTN_KANSOUGOSYOUMI1_BOTTOM,
                        GXHDO102B019Const.BTN_KANSOUGOSYOUMI2_BOTTOM,
                        GXHDO102B019Const.BTN_KANSOUGOSYOUMI3_BOTTOM,
                        GXHDO102B019Const.BTN_KOKEIBUNHIRITUHEIKIN_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B019Const.BTN_UPDATE_TOP,
                        GXHDO102B019Const.BTN_DELETE_TOP,
                        GXHDO102B019Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B019Const.BTN_DELETE_BOTTOM
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
        this.setItemData(processData, GXHDO102B019Const.WIPLOTNO, lotNo);
        // 誘電体ｽﾗﾘｰ品名
        this.setItemData(processData, GXHDO102B019Const.YUUDENTAISLURRYHINMEI, StringUtil.nullToBlank(getMapData(shikakariData, "hinmei")));
        // 誘電体ｽﾗﾘｰLotNo
        this.setItemData(processData, GXHDO102B019Const.YUUDENTAISLURRYLOTNO, StringUtil.nullToBlank(getMapData(shikakariData, "lotno")));
        // ﾛｯﾄ区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode"));
        // ﾛｯﾄ区分名
        String lotkubun = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubun"));
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO102B019Const.LOTKUBUN, "");
        } else {
            if (!StringUtil.isEmpty(lotkubun)) {
                lotkubuncode = lotkubuncode + ":" + lotkubun;
            }
            this.setItemData(processData, GXHDO102B019Const.LOTKUBUN, lotkubuncode);
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

        List<SrYuudentaiTenkazai> srYuudentaiTenkazaiList = new ArrayList<>();
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

            // 誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定データ取得
            srYuudentaiTenkazaiList = getSrYuudentaiTenkazaiData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo9, edaban);
            if (srYuudentaiTenkazaiList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srYuudentaiTenkazaiList.isEmpty()) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srYuudentaiTenkazaiList.get(0));

        return true;

    }

    /**
     * データ設定処理
     *
     * @param processData 処理制御データ
     * @param srYuudentaiTenkazai 誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定データ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrYuudentaiTenkazai srYuudentaiTenkazai) {

        // 風袋重量①
        this.setItemData(processData, GXHDO102B019Const.FUUTAIJYUURYOU1, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.FUUTAIJYUURYOU1, srYuudentaiTenkazai));

        // 風袋重量②
        this.setItemData(processData, GXHDO102B019Const.FUUTAIJYUURYOU2, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.FUUTAIJYUURYOU2, srYuudentaiTenkazai));

        // 風袋重量③
        this.setItemData(processData, GXHDO102B019Const.FUUTAIJYUURYOU3, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.FUUTAIJYUURYOU3, srYuudentaiTenkazai));

        // 総重量①
        this.setItemData(processData, GXHDO102B019Const.SOUJYUURYOU1, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.SOUJYUURYOU1, srYuudentaiTenkazai));

        // 総重量②
        this.setItemData(processData, GXHDO102B019Const.SOUJYUURYOU2, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.SOUJYUURYOU2, srYuudentaiTenkazai));

        // 総重量③
        this.setItemData(processData, GXHDO102B019Const.SOUJYUURYOU3, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.SOUJYUURYOU3, srYuudentaiTenkazai));

        // 添加材ｽﾗﾘｰ重量
        this.setItemData(processData, GXHDO102B019Const.TENKAZAISLURRYJYUURYOU, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.TENKAZAISLURRYJYUURYOU, srYuudentaiTenkazai));

        // 撹拌開始日
        this.setItemData(processData, GXHDO102B019Const.KAKUHANKAISI_DAY, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.KAKUHANKAISI_DAY, srYuudentaiTenkazai));

        // 撹拌開始時間
        this.setItemData(processData, GXHDO102B019Const.KAKUHANKAISI_TIME, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.KAKUHANKAISI_TIME, srYuudentaiTenkazai));

        // 撹拌終了日
        this.setItemData(processData, GXHDO102B019Const.KAKUHANSYUURYOU_DAY, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.KAKUHANSYUURYOU_DAY, srYuudentaiTenkazai));

        // 撹拌終了時間
        this.setItemData(processData, GXHDO102B019Const.KAKUHANSYUURYOU_TIME, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.KAKUHANSYUURYOU_TIME, srYuudentaiTenkazai));

        // ｱﾙﾐ皿風袋重量①
        this.setItemData(processData, GXHDO102B019Const.ARUMIZARAFUUTAIJYUURYOU1, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.ARUMIZARAFUUTAIJYUURYOU1, srYuudentaiTenkazai));

        // 乾燥前ｽﾗﾘｰ重量①
        this.setItemData(processData, GXHDO102B019Const.KANSOUMAESLURRYJYUURYOU1, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.KANSOUMAESLURRYJYUURYOU1, srYuudentaiTenkazai));

        // ｱﾙﾐ皿風袋重量②
        this.setItemData(processData, GXHDO102B019Const.ARUMIZARAFUUTAIJYUURYOU2, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.ARUMIZARAFUUTAIJYUURYOU2, srYuudentaiTenkazai));

        // 乾燥前ｽﾗﾘｰ重量②
        this.setItemData(processData, GXHDO102B019Const.KANSOUMAESLURRYJYUURYOU2, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.KANSOUMAESLURRYJYUURYOU2, srYuudentaiTenkazai));

        // ｱﾙﾐ皿風袋重量③
        this.setItemData(processData, GXHDO102B019Const.ARUMIZARAFUUTAIJYUURYOU3, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.ARUMIZARAFUUTAIJYUURYOU3, srYuudentaiTenkazai));

        // 乾燥前ｽﾗﾘｰ重量③
        this.setItemData(processData, GXHDO102B019Const.KANSOUMAESLURRYJYUURYOU3, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.KANSOUMAESLURRYJYUURYOU3, srYuudentaiTenkazai));

        // 乾燥開始日①
        this.setItemData(processData, GXHDO102B019Const.KANSOUKAISI1_DAY, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.KANSOUKAISI1_DAY, srYuudentaiTenkazai));

        // 乾燥開始時間①
        this.setItemData(processData, GXHDO102B019Const.KANSOUKAISI1_TIME, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.KANSOUKAISI1_TIME, srYuudentaiTenkazai));

        // 乾燥終了日①
        this.setItemData(processData, GXHDO102B019Const.KANSOUSYUURYOU1_DAY, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.KANSOUSYUURYOU1_DAY, srYuudentaiTenkazai));

        // 乾燥終了時間①
        this.setItemData(processData, GXHDO102B019Const.KANSOUSYUURYOU1_TIME, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.KANSOUSYUURYOU1_TIME, srYuudentaiTenkazai));

        // 乾燥開始日②
        this.setItemData(processData, GXHDO102B019Const.KANSOUKAISI2_DAY, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.KANSOUKAISI2_DAY, srYuudentaiTenkazai));

        // 乾燥開始時間②
        this.setItemData(processData, GXHDO102B019Const.KANSOUKAISI2_TIME, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.KANSOUKAISI2_TIME, srYuudentaiTenkazai));

        // 乾燥終了日②
        this.setItemData(processData, GXHDO102B019Const.KANSOUSYUURYOU2_DAY, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.KANSOUSYUURYOU2_DAY, srYuudentaiTenkazai));

        // 乾燥終了時間②
        this.setItemData(processData, GXHDO102B019Const.KANSOUSYUURYOU2_TIME, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.KANSOUSYUURYOU2_TIME, srYuudentaiTenkazai));

        // 乾燥後総重量①
        this.setItemData(processData, GXHDO102B019Const.KANSOUGOSOUJYUURYOU1, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.KANSOUGOSOUJYUURYOU1, srYuudentaiTenkazai));

        // 乾燥後総重量②
        this.setItemData(processData, GXHDO102B019Const.KANSOUGOSOUJYUURYOU2, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.KANSOUGOSOUJYUURYOU2, srYuudentaiTenkazai));

        // 乾燥後総重量③
        this.setItemData(processData, GXHDO102B019Const.KANSOUGOSOUJYUURYOU3, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.KANSOUGOSOUJYUURYOU3, srYuudentaiTenkazai));

        // 乾燥後正味重量①
        this.setItemData(processData, GXHDO102B019Const.KANSOUGOSYOUMIJYUURYOU1, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.KANSOUGOSYOUMIJYUURYOU1, srYuudentaiTenkazai));

        // 乾燥後正味重量②
        this.setItemData(processData, GXHDO102B019Const.KANSOUGOSYOUMIJYUURYOU2, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.KANSOUGOSYOUMIJYUURYOU2, srYuudentaiTenkazai));

        // 乾燥後正味重量③
        this.setItemData(processData, GXHDO102B019Const.KANSOUGOSYOUMIJYUURYOU3, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.KANSOUGOSYOUMIJYUURYOU3, srYuudentaiTenkazai));

        // 固形分比率①
        this.setItemData(processData, GXHDO102B019Const.KOKEIBUNHIRITU1, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.KOKEIBUNHIRITU1, srYuudentaiTenkazai));

        // 固形分比率②
        this.setItemData(processData, GXHDO102B019Const.KOKEIBUNHIRITU2, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.KOKEIBUNHIRITU2, srYuudentaiTenkazai));

        // 固形分比率③
        this.setItemData(processData, GXHDO102B019Const.KOKEIBUNHIRITU3, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.KOKEIBUNHIRITU3, srYuudentaiTenkazai));

        // 固形分比率平均
        this.setItemData(processData, GXHDO102B019Const.KOKEIBUNHIRITUHEIKIN, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.KOKEIBUNHIRITUHEIKIN, srYuudentaiTenkazai));

        // 固形分測定担当者
        this.setItemData(processData, GXHDO102B019Const.KOKEIBUNSOKUTEITANTOUSYA, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.KOKEIBUNSOKUTEITANTOUSYA, srYuudentaiTenkazai));

        // 備考1
        this.setItemData(processData, GXHDO102B019Const.BIKOU1, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.BIKOU1, srYuudentaiTenkazai));

        // 備考2
        this.setItemData(processData, GXHDO102B019Const.BIKOU2, getSrYuudentaiTenkazaiItemData(GXHDO102B019Const.BIKOU2, srYuudentaiTenkazai));

    }

    /**
     * 誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return 誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrYuudentaiTenkazai> getSrYuudentaiTenkazaiData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrYuudentaiTenkazai(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSrYuudentaiTenkazai(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
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
     * [誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrYuudentaiTenkazai> loadSrYuudentaiTenkazai(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = " SELECT "
                + " kojyo,lotno,edaban,yuudentaislurryhinmei,yuudentaislurrylotno,lotkubun,genryoulotno,genryoukigou,tenkazaislurryhinmei,"
                + "tenkazaislurrylotno,fuutaijyuuryou1,fuutaijyuuryou2,fuutaijyuuryou3,soujyuuryou1,soujyuuryou2,soujyuuryou3,"
                + "tenkazaislurryjyuuryou,kakuhanki,kaitensuu,kakuhanjikan,kakuhankaisinichiji,kakuhansyuuryounichiji,kansouzarasyurui,"
                + "arumizarafuutaijyuuryou1,kansoumaeslurryjyuuryou1,arumizarafuutaijyuuryou2,kansoumaeslurryjyuuryou2,arumizarafuutaijyuuryou3,"
                + "kansoumaeslurryjyuuryou3,kansouki1,kansouondo1,kansoujikan1,kansoukaisinichiji1,kansousyuuryounichiji1,kansouki2,kansouondo2,"
                + "kansoujikan2,kansoukaisinichiji2,kansousyuuryounichiji2,reikyakujikan,kansougosoujyuuryou1,kansougosoujyuuryou2,"
                + "kansougosoujyuuryou3,kansougosyoumijyuuryou1,kansougosyoumijyuuryou2,kansougosyoumijyuuryou3,kokeibunhiritu1,kokeibunhiritu2,"
                + "kokeibunhiritu3,kokeibunhirituheikin,kokeibunsokuteitantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + " FROM sr_yuudentai_tenkazai "
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
        mapping.put("kojyo", "kojyo");                                         // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                         // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                       // 枝番
        mapping.put("yuudentaislurryhinmei", "yuudentaislurryhinmei");         // 誘電体ｽﾗﾘｰ品名
        mapping.put("yuudentaislurrylotno", "yuudentaislurrylotno");           // 誘電体ｽﾗﾘｰLotNo
        mapping.put("lotkubun", "lotkubun");                                   // ﾛｯﾄ区分
        mapping.put("genryoulotno", "genryoulotno");                           // 原料LotNo
        mapping.put("genryoukigou", "genryoukigou");                           // 原料記号
        mapping.put("tenkazaislurryhinmei", "tenkazaislurryhinmei");           // 添加材ｽﾗﾘｰ品名
        mapping.put("tenkazaislurrylotno", "tenkazaislurrylotno");             // 添加材ｽﾗﾘｰLotNo
        mapping.put("fuutaijyuuryou1", "fuutaijyuuryou1");                     // 風袋重量①
        mapping.put("fuutaijyuuryou2", "fuutaijyuuryou2");                     // 風袋重量②
        mapping.put("fuutaijyuuryou3", "fuutaijyuuryou3");                     // 風袋重量③
        mapping.put("soujyuuryou1", "soujyuuryou1");                           // 総重量①
        mapping.put("soujyuuryou2", "soujyuuryou2");                           // 総重量②
        mapping.put("soujyuuryou3", "soujyuuryou3");                           // 総重量③
        mapping.put("tenkazaislurryjyuuryou", "tenkazaislurryjyuuryou");       // 添加材ｽﾗﾘｰ重量
        mapping.put("kakuhanki", "kakuhanki");                                 // 撹拌機
        mapping.put("kaitensuu", "kaitensuu");                                 // 回転数
        mapping.put("kakuhanjikan", "kakuhanjikan");                           // 撹拌時間
        mapping.put("kakuhankaisinichiji", "kakuhankaisinichiji");             // 撹拌開始日時
        mapping.put("kakuhansyuuryounichiji", "kakuhansyuuryounichiji");       // 撹拌終了日時
        mapping.put("kansouzarasyurui", "kansouzarasyurui");                   // 乾燥皿種類
        mapping.put("arumizarafuutaijyuuryou1", "arumizarafuutaijyuuryou1");   // ｱﾙﾐ皿風袋重量①
        mapping.put("kansoumaeslurryjyuuryou1", "kansoumaeslurryjyuuryou1");   // 乾燥前ｽﾗﾘｰ重量①
        mapping.put("arumizarafuutaijyuuryou2", "arumizarafuutaijyuuryou2");   // ｱﾙﾐ皿風袋重量②
        mapping.put("kansoumaeslurryjyuuryou2", "kansoumaeslurryjyuuryou2");   // 乾燥前ｽﾗﾘｰ重量②
        mapping.put("arumizarafuutaijyuuryou3", "arumizarafuutaijyuuryou3");   // ｱﾙﾐ皿風袋重量③
        mapping.put("kansoumaeslurryjyuuryou3", "kansoumaeslurryjyuuryou3");   // 乾燥前ｽﾗﾘｰ重量③
        mapping.put("kansouki1", "kansouki1");                                 // 乾燥機①
        mapping.put("kansouondo1", "kansouondo1");                             // 乾燥温度①
        mapping.put("kansoujikan1", "kansoujikan1");                           // 乾燥時間①
        mapping.put("kansoukaisinichiji1", "kansoukaisinichiji1");             // 乾燥開始日時①
        mapping.put("kansousyuuryounichiji1", "kansousyuuryounichiji1");       // 乾燥終了日時①
        mapping.put("kansouki2", "kansouki2");                                 // 乾燥機②
        mapping.put("kansouondo2", "kansouondo2");                             // 乾燥温度②
        mapping.put("kansoujikan2", "kansoujikan2");                           // 乾燥時間②
        mapping.put("kansoukaisinichiji2", "kansoukaisinichiji2");             // 乾燥開始日時②
        mapping.put("kansousyuuryounichiji2", "kansousyuuryounichiji2");       // 乾燥終了日時②
        mapping.put("reikyakujikan", "reikyakujikan");                         // 冷却時間
        mapping.put("kansougosoujyuuryou1", "kansougosoujyuuryou1");           // 乾燥後総重量①
        mapping.put("kansougosoujyuuryou2", "kansougosoujyuuryou2");           // 乾燥後総重量②
        mapping.put("kansougosoujyuuryou3", "kansougosoujyuuryou3");           // 乾燥後総重量③
        mapping.put("kansougosyoumijyuuryou1", "kansougosyoumijyuuryou1");     // 乾燥後正味重量①
        mapping.put("kansougosyoumijyuuryou2", "kansougosyoumijyuuryou2");     // 乾燥後正味重量②
        mapping.put("kansougosyoumijyuuryou3", "kansougosyoumijyuuryou3");     // 乾燥後正味重量③
        mapping.put("kokeibunhiritu1", "kokeibunhiritu1");                     // 固形分比率①
        mapping.put("kokeibunhiritu2", "kokeibunhiritu2");                     // 固形分比率②
        mapping.put("kokeibunhiritu3", "kokeibunhiritu3");                     // 固形分比率③
        mapping.put("kokeibunhirituheikin", "kokeibunhirituheikin");           // 固形分比率平均
        mapping.put("kokeibunsokuteitantousya", "kokeibunsokuteitantousya");   // 固形分測定担当者
        mapping.put("bikou1", "bikou1");                                       // 備考1
        mapping.put("bikou2", "bikou2");                                       // 備考2
        mapping.put("torokunichiji", "torokunichiji");                         // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                           // 更新日時
        mapping.put("revision", "revision");                                   // revision

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrYuudentaiTenkazai>> beanHandler = new BeanListHandler<>(SrYuudentaiTenkazai.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrYuudentaiTenkazai> loadTmpSrYuudentaiTenkazai(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = " SELECT "
                + " kojyo,lotno,edaban,yuudentaislurryhinmei,yuudentaislurrylotno,lotkubun,genryoulotno,genryoukigou,tenkazaislurryhinmei,"
                + "tenkazaislurrylotno,fuutaijyuuryou1,fuutaijyuuryou2,fuutaijyuuryou3,soujyuuryou1,soujyuuryou2,soujyuuryou3,"
                + "tenkazaislurryjyuuryou,kakuhanki,kaitensuu,kakuhanjikan,kakuhankaisinichiji,kakuhansyuuryounichiji,kansouzarasyurui,"
                + "arumizarafuutaijyuuryou1,kansoumaeslurryjyuuryou1,arumizarafuutaijyuuryou2,kansoumaeslurryjyuuryou2,arumizarafuutaijyuuryou3,"
                + "kansoumaeslurryjyuuryou3,kansouki1,kansouondo1,kansoujikan1,kansoukaisinichiji1,kansousyuuryounichiji1,kansouki2,kansouondo2,"
                + "kansoujikan2,kansoukaisinichiji2,kansousyuuryounichiji2,reikyakujikan,kansougosoujyuuryou1,kansougosoujyuuryou2,"
                + "kansougosoujyuuryou3,kansougosyoumijyuuryou1,kansougosyoumijyuuryou2,kansougosyoumijyuuryou3,kokeibunhiritu1,kokeibunhiritu2,"
                + "kokeibunhiritu3,kokeibunhirituheikin,kokeibunsokuteitantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + " FROM tmp_sr_yuudentai_tenkazai "
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
        mapping.put("kojyo", "kojyo");                                         // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                         // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                       // 枝番
        mapping.put("yuudentaislurryhinmei", "yuudentaislurryhinmei");         // 誘電体ｽﾗﾘｰ品名
        mapping.put("yuudentaislurrylotno", "yuudentaislurrylotno");           // 誘電体ｽﾗﾘｰLotNo
        mapping.put("lotkubun", "lotkubun");                                   // ﾛｯﾄ区分
        mapping.put("genryoulotno", "genryoulotno");                           // 原料LotNo
        mapping.put("genryoukigou", "genryoukigou");                           // 原料記号
        mapping.put("tenkazaislurryhinmei", "tenkazaislurryhinmei");           // 添加材ｽﾗﾘｰ品名
        mapping.put("tenkazaislurrylotno", "tenkazaislurrylotno");             // 添加材ｽﾗﾘｰLotNo
        mapping.put("fuutaijyuuryou1", "fuutaijyuuryou1");                     // 風袋重量①
        mapping.put("fuutaijyuuryou2", "fuutaijyuuryou2");                     // 風袋重量②
        mapping.put("fuutaijyuuryou3", "fuutaijyuuryou3");                     // 風袋重量③
        mapping.put("soujyuuryou1", "soujyuuryou1");                           // 総重量①
        mapping.put("soujyuuryou2", "soujyuuryou2");                           // 総重量②
        mapping.put("soujyuuryou3", "soujyuuryou3");                           // 総重量③
        mapping.put("tenkazaislurryjyuuryou", "tenkazaislurryjyuuryou");       // 添加材ｽﾗﾘｰ重量
        mapping.put("kakuhanki", "kakuhanki");                                 // 撹拌機
        mapping.put("kaitensuu", "kaitensuu");                                 // 回転数
        mapping.put("kakuhanjikan", "kakuhanjikan");                           // 撹拌時間
        mapping.put("kakuhankaisinichiji", "kakuhankaisinichiji");             // 撹拌開始日時
        mapping.put("kakuhansyuuryounichiji", "kakuhansyuuryounichiji");       // 撹拌終了日時
        mapping.put("kansouzarasyurui", "kansouzarasyurui");                   // 乾燥皿種類
        mapping.put("arumizarafuutaijyuuryou1", "arumizarafuutaijyuuryou1");   // ｱﾙﾐ皿風袋重量①
        mapping.put("kansoumaeslurryjyuuryou1", "kansoumaeslurryjyuuryou1");   // 乾燥前ｽﾗﾘｰ重量①
        mapping.put("arumizarafuutaijyuuryou2", "arumizarafuutaijyuuryou2");   // ｱﾙﾐ皿風袋重量②
        mapping.put("kansoumaeslurryjyuuryou2", "kansoumaeslurryjyuuryou2");   // 乾燥前ｽﾗﾘｰ重量②
        mapping.put("arumizarafuutaijyuuryou3", "arumizarafuutaijyuuryou3");   // ｱﾙﾐ皿風袋重量③
        mapping.put("kansoumaeslurryjyuuryou3", "kansoumaeslurryjyuuryou3");   // 乾燥前ｽﾗﾘｰ重量③
        mapping.put("kansouki1", "kansouki1");                                 // 乾燥機①
        mapping.put("kansouondo1", "kansouondo1");                             // 乾燥温度①
        mapping.put("kansoujikan1", "kansoujikan1");                           // 乾燥時間①
        mapping.put("kansoukaisinichiji1", "kansoukaisinichiji1");             // 乾燥開始日時①
        mapping.put("kansousyuuryounichiji1", "kansousyuuryounichiji1");       // 乾燥終了日時①
        mapping.put("kansouki2", "kansouki2");                                 // 乾燥機②
        mapping.put("kansouondo2", "kansouondo2");                             // 乾燥温度②
        mapping.put("kansoujikan2", "kansoujikan2");                           // 乾燥時間②
        mapping.put("kansoukaisinichiji2", "kansoukaisinichiji2");             // 乾燥開始日時②
        mapping.put("kansousyuuryounichiji2", "kansousyuuryounichiji2");       // 乾燥終了日時②
        mapping.put("reikyakujikan", "reikyakujikan");                         // 冷却時間
        mapping.put("kansougosoujyuuryou1", "kansougosoujyuuryou1");           // 乾燥後総重量①
        mapping.put("kansougosoujyuuryou2", "kansougosoujyuuryou2");           // 乾燥後総重量②
        mapping.put("kansougosoujyuuryou3", "kansougosoujyuuryou3");           // 乾燥後総重量③
        mapping.put("kansougosyoumijyuuryou1", "kansougosyoumijyuuryou1");     // 乾燥後正味重量①
        mapping.put("kansougosyoumijyuuryou2", "kansougosyoumijyuuryou2");     // 乾燥後正味重量②
        mapping.put("kansougosyoumijyuuryou3", "kansougosyoumijyuuryou3");     // 乾燥後正味重量③
        mapping.put("kokeibunhiritu1", "kokeibunhiritu1");                     // 固形分比率①
        mapping.put("kokeibunhiritu2", "kokeibunhiritu2");                     // 固形分比率②
        mapping.put("kokeibunhiritu3", "kokeibunhiritu3");                     // 固形分比率③
        mapping.put("kokeibunhirituheikin", "kokeibunhirituheikin");           // 固形分比率平均
        mapping.put("kokeibunsokuteitantousya", "kokeibunsokuteitantousya");   // 固形分測定担当者
        mapping.put("bikou1", "bikou1");                                       // 備考1
        mapping.put("bikou2", "bikou2");                                       // 備考2
        mapping.put("torokunichiji", "torokunichiji");                         // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                           // 更新日時
        mapping.put("revision", "revision");                                   // revision
        mapping.put("deleteflag", "deleteflag");                               // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrYuudentaiTenkazai>> beanHandler = new BeanListHandler<>(SrYuudentaiTenkazai.class, rowProcessor);

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
     * @param srYuudentaiTenkazai 誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定データ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrYuudentaiTenkazai srYuudentaiTenkazai) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srYuudentaiTenkazai != null) {
            // 元データが存在する場合元データより取得
            return getSrYuudentaiTenkazaiItemData(itemId, srYuudentaiTenkazai);
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
    private String getItemKikakuchi(List<FXHDD01> listData, String itemId, SrYuudentaiTenkazai srYuudentaiTenkazai) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return StringUtil.nullToBlank(selectData.get(0).getKikakuChi()).replace("【", "").replace("】", "");
        } else if (srYuudentaiTenkazai != null) {
            // 元データが存在する場合元データより取得
            return getSrYuudentaiTenkazaiItemData(itemId, srYuudentaiTenkazai);
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
     * 誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定_仮登録(tmp_sr_yuudentai_tenkazai)登録処理
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
    private void insertTmpSrYuudentaiTenkazai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "INSERT INTO tmp_sr_yuudentai_tenkazai ("
                + " kojyo,lotno,edaban,yuudentaislurryhinmei,yuudentaislurrylotno,lotkubun,genryoulotno,genryoukigou,tenkazaislurryhinmei,"
                + "tenkazaislurrylotno,fuutaijyuuryou1,fuutaijyuuryou2,fuutaijyuuryou3,soujyuuryou1,soujyuuryou2,soujyuuryou3,"
                + "tenkazaislurryjyuuryou,kakuhanki,kaitensuu,kakuhanjikan,kakuhankaisinichiji,kakuhansyuuryounichiji,kansouzarasyurui,"
                + "arumizarafuutaijyuuryou1,kansoumaeslurryjyuuryou1,arumizarafuutaijyuuryou2,kansoumaeslurryjyuuryou2,arumizarafuutaijyuuryou3,"
                + "kansoumaeslurryjyuuryou3,kansouki1,kansouondo1,kansoujikan1,kansoukaisinichiji1,kansousyuuryounichiji1,kansouki2,kansouondo2,"
                + "kansoujikan2,kansoukaisinichiji2,kansousyuuryounichiji2,reikyakujikan,kansougosoujyuuryou1,kansougosoujyuuryou2,"
                + "kansougosoujyuuryou3,kansougosyoumijyuuryou1,kansougosyoumijyuuryou2,kansougosyoumijyuuryou3,kokeibunhiritu1,kokeibunhiritu2,"
                + "kokeibunhiritu3,kokeibunhirituheikin,kokeibunsokuteitantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + " ) VALUES ( "
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrYuudentaiTenkazai(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, processData, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定_仮登録(tmp_sr_yuudentai_tenkazai)更新処理
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
    private void updateTmpSrYuudentaiTenkazai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "UPDATE tmp_sr_yuudentai_tenkazai SET "
                + " yuudentaislurryhinmei = ?,yuudentaislurrylotno = ?,lotkubun = ?,genryoulotno = ?,genryoukigou = ?,tenkazaislurryhinmei = ?,tenkazaislurrylotno = ?,"
                + "fuutaijyuuryou1 = ?,fuutaijyuuryou2 = ?,fuutaijyuuryou3 = ?,soujyuuryou1 = ?,soujyuuryou2 = ?,soujyuuryou3 = ?,tenkazaislurryjyuuryou = ?,"
                + "kakuhanki = ?,kaitensuu = ?,kakuhanjikan = ?,kakuhankaisinichiji = ?,kakuhansyuuryounichiji = ?,kansouzarasyurui = ?,arumizarafuutaijyuuryou1 = ?,"
                + "kansoumaeslurryjyuuryou1 = ?,arumizarafuutaijyuuryou2 = ?,kansoumaeslurryjyuuryou2 = ?,arumizarafuutaijyuuryou3 = ?,kansoumaeslurryjyuuryou3 = ?,"
                + "kansouki1 = ?,kansouondo1 = ?,kansoujikan1 = ?,kansoukaisinichiji1 = ?,kansousyuuryounichiji1 = ?,kansouki2 = ?,kansouondo2 = ?,kansoujikan2 = ?,"
                + "kansoukaisinichiji2 = ?,kansousyuuryounichiji2 = ?,reikyakujikan = ?,kansougosoujyuuryou1 = ?,kansougosoujyuuryou2 = ?,kansougosoujyuuryou3 = ?,"
                + "kansougosyoumijyuuryou1 = ?,kansougosyoumijyuuryou2 = ?,kansougosyoumijyuuryou3 = ?,kokeibunhiritu1 = ?,kokeibunhiritu2 = ?,kokeibunhiritu3 = ?,"
                + "kokeibunhirituheikin = ?,kokeibunsokuteitantousya = ?,bikou1 = ?,bikou2 = ?,kosinnichiji = ?,revision = ?,deleteflag = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrYuudentaiTenkazai> srYuudentaiTenkazaiList = getSrYuudentaiTenkazaiData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrYuudentaiTenkazai srYuudentaiTenkazai = null;
        if (!srYuudentaiTenkazaiList.isEmpty()) {
            srYuudentaiTenkazai = srYuudentaiTenkazaiList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrYuudentaiTenkazai(false, newRev, 0, "", "", "", systemTime, processData, srYuudentaiTenkazai);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定_仮登録(tmp_sr_yuudentai_tenkazai)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrYuudentaiTenkazai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sr_yuudentai_tenkazai "
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
     * 誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定_仮登録(tmp_sr_yuudentai_tenkazai)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srYuudentaiTenkazai 誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrYuudentaiTenkazai(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, String systemTime, ProcessData processData, SrYuudentaiTenkazai srYuudentaiTenkazai) {

        List<FXHDD01> pItemList = processData.getItemList();
        List<Object> params = new ArrayList<>();
        // 撹拌開始日時
        String kakuhankaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B019Const.KAKUHANKAISI_TIME, srYuudentaiTenkazai));
        // 撹拌終了日時
        String kakuhansyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B019Const.KAKUHANSYUURYOU_TIME, srYuudentaiTenkazai));
        // 乾燥開始日時①
        String kansoukaisi1Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B019Const.KANSOUKAISI1_TIME, srYuudentaiTenkazai));
        // 乾燥終了日時①
        String kansousyuuryou1Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B019Const.KANSOUSYUURYOU1_TIME, srYuudentaiTenkazai));
        // 乾燥開始日時②
        String kansoukaisi2Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B019Const.KANSOUKAISI2_TIME, srYuudentaiTenkazai));
        // 乾燥終了日時②
        String kansousyuuryou2Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B019Const.KANSOUSYUURYOU2_TIME, srYuudentaiTenkazai));
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B019Const.YUUDENTAISLURRYHINMEI, srYuudentaiTenkazai))); // 誘電体ｽﾗﾘｰ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B019Const.YUUDENTAISLURRYLOTNO, srYuudentaiTenkazai))); // 誘電体ｽﾗﾘｰLotNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B019Const.LOTKUBUN, srYuudentaiTenkazai))); // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B019Const.GENRYOULOTNO, srYuudentaiTenkazai))); // 原料LotNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B019Const.GENRYOUKIGOU, srYuudentaiTenkazai))); // 原料記号
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B019Const.TENKAZAISLURRYHINMEI, srYuudentaiTenkazai))); // 添加材ｽﾗﾘｰ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B019Const.TENKAZAISLURRYLOTNO, srYuudentaiTenkazai))); // 添加材ｽﾗﾘｰLotNo
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B019Const.FUUTAIJYUURYOU1, srYuudentaiTenkazai))); // 風袋重量①
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B019Const.FUUTAIJYUURYOU2, srYuudentaiTenkazai))); // 風袋重量②
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B019Const.FUUTAIJYUURYOU3, srYuudentaiTenkazai))); // 風袋重量③
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B019Const.SOUJYUURYOU1, srYuudentaiTenkazai))); // 総重量①
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B019Const.SOUJYUURYOU2, srYuudentaiTenkazai))); // 総重量②
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B019Const.SOUJYUURYOU3, srYuudentaiTenkazai))); // 総重量③
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B019Const.TENKAZAISLURRYJYUURYOU, srYuudentaiTenkazai))); // 添加材ｽﾗﾘｰ重量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B019Const.KAKUHANKI, srYuudentaiTenkazai))); // 撹拌機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B019Const.KAITENSUU, srYuudentaiTenkazai))); // 回転数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B019Const.KAKUHANJIKAN, srYuudentaiTenkazai))); // 撹拌時間
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B019Const.KAKUHANKAISI_DAY, srYuudentaiTenkazai),
                "".equals(kakuhankaisiTime) ? "0000" : kakuhankaisiTime)); // 撹拌開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B019Const.KAKUHANSYUURYOU_DAY, srYuudentaiTenkazai),
                "".equals(kakuhansyuuryouTime) ? "0000" : kakuhansyuuryouTime)); // 撹拌終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B019Const.KANSOUZARASYURUI, srYuudentaiTenkazai))); // 乾燥皿種類
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B019Const.ARUMIZARAFUUTAIJYUURYOU1, srYuudentaiTenkazai))); // ｱﾙﾐ皿風袋重量①
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B019Const.KANSOUMAESLURRYJYUURYOU1, srYuudentaiTenkazai))); // 乾燥前ｽﾗﾘｰ重量①
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B019Const.ARUMIZARAFUUTAIJYUURYOU2, srYuudentaiTenkazai))); // ｱﾙﾐ皿風袋重量②
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B019Const.KANSOUMAESLURRYJYUURYOU2, srYuudentaiTenkazai))); // 乾燥前ｽﾗﾘｰ重量②
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B019Const.ARUMIZARAFUUTAIJYUURYOU3, srYuudentaiTenkazai))); // ｱﾙﾐ皿風袋重量③
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B019Const.KANSOUMAESLURRYJYUURYOU3, srYuudentaiTenkazai))); // 乾燥前ｽﾗﾘｰ重量③
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B019Const.KANSOUKI1, srYuudentaiTenkazai))); // 乾燥機①
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B019Const.KANSOUONDO1, srYuudentaiTenkazai))); // 乾燥温度①
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B019Const.KANSOUJIKAN1, srYuudentaiTenkazai))); // 乾燥時間①
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B019Const.KANSOUKAISI1_DAY, srYuudentaiTenkazai),
                "".equals(kansoukaisi1Time) ? "0000" : kansoukaisi1Time)); // 乾燥開始日時①
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B019Const.KANSOUSYUURYOU1_DAY, srYuudentaiTenkazai),
                "".equals(kansousyuuryou1Time) ? "0000" : kansousyuuryou1Time)); // 乾燥終了日時①
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B019Const.KANSOUKI2, srYuudentaiTenkazai))); // 乾燥機②
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B019Const.KANSOUONDO2, srYuudentaiTenkazai))); // 乾燥温度②
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B019Const.KANSOUJIKAN2, srYuudentaiTenkazai))); // 乾燥時間②
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B019Const.KANSOUKAISI2_DAY, srYuudentaiTenkazai),
                "".equals(kansoukaisi2Time) ? "0000" : kansoukaisi2Time)); // 乾燥開始日時②
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B019Const.KANSOUSYUURYOU2_DAY, srYuudentaiTenkazai),
                "".equals(kansousyuuryou2Time) ? "0000" : kansousyuuryou2Time)); // 乾燥終了日時②
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B019Const.REIKYAKUJIKAN, srYuudentaiTenkazai))); // 冷却時間
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B019Const.KANSOUGOSOUJYUURYOU1, srYuudentaiTenkazai))); // 乾燥後総重量①
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B019Const.KANSOUGOSOUJYUURYOU2, srYuudentaiTenkazai))); // 乾燥後総重量②
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B019Const.KANSOUGOSOUJYUURYOU3, srYuudentaiTenkazai))); // 乾燥後総重量③
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B019Const.KANSOUGOSYOUMIJYUURYOU1, srYuudentaiTenkazai))); // 乾燥後正味重量①
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B019Const.KANSOUGOSYOUMIJYUURYOU2, srYuudentaiTenkazai))); // 乾燥後正味重量②
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B019Const.KANSOUGOSYOUMIJYUURYOU3, srYuudentaiTenkazai))); // 乾燥後正味重量③
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B019Const.KOKEIBUNHIRITU1, srYuudentaiTenkazai))); // 固形分比率①
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B019Const.KOKEIBUNHIRITU2, srYuudentaiTenkazai))); // 固形分比率②
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B019Const.KOKEIBUNHIRITU3, srYuudentaiTenkazai))); // 固形分比率③
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B019Const.KOKEIBUNHIRITUHEIKIN, srYuudentaiTenkazai))); // 固形分比率平均
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B019Const.KOKEIBUNSOKUTEITANTOUSYA, srYuudentaiTenkazai))); // 固形分測定担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B019Const.BIKOU1, srYuudentaiTenkazai))); // 備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B019Const.BIKOU2, srYuudentaiTenkazai))); // 備考2

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
     * 誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定(sr_yuudentai_tenkazai)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srYuudentaiTenkazai 登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrYuudentaiTenkazai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData, SrYuudentaiTenkazai srYuudentaiTenkazai) throws SQLException {

        String sql = "INSERT INTO sr_yuudentai_tenkazai ("
                + " kojyo,lotno,edaban,yuudentaislurryhinmei,yuudentaislurrylotno,lotkubun,genryoulotno,genryoukigou,tenkazaislurryhinmei,"
                + "tenkazaislurrylotno,fuutaijyuuryou1,fuutaijyuuryou2,fuutaijyuuryou3,soujyuuryou1,soujyuuryou2,soujyuuryou3,"
                + "tenkazaislurryjyuuryou,kakuhanki,kaitensuu,kakuhanjikan,kakuhankaisinichiji,kakuhansyuuryounichiji,kansouzarasyurui,"
                + "arumizarafuutaijyuuryou1,kansoumaeslurryjyuuryou1,arumizarafuutaijyuuryou2,kansoumaeslurryjyuuryou2,arumizarafuutaijyuuryou3,"
                + "kansoumaeslurryjyuuryou3,kansouki1,kansouondo1,kansoujikan1,kansoukaisinichiji1,kansousyuuryounichiji1,kansouki2,kansouondo2,"
                + "kansoujikan2,kansoukaisinichiji2,kansousyuuryounichiji2,reikyakujikan,kansougosoujyuuryou1,kansougosoujyuuryou2,"
                + "kansougosoujyuuryou3,kansougosyoumijyuuryou1,kansougosyoumijyuuryou2,kansougosyoumijyuuryou3,kokeibunhiritu1,kokeibunhiritu2,"
                + "kokeibunhiritu3,kokeibunhirituheikin,kokeibunsokuteitantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + " ) VALUES ( "
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrYuudentaiTenkazai(true, newRev, kojyo, lotNo, edaban, systemTime, processData, srYuudentaiTenkazai);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定(sr_yuudentai_tenkazai)更新処理
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
    private void updateSrYuudentaiTenkazai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "UPDATE sr_yuudentai_tenkazai SET "
                + " yuudentaislurryhinmei = ?,yuudentaislurrylotno = ?,lotkubun = ?,genryoulotno = ?,genryoukigou = ?,tenkazaislurryhinmei = ?,tenkazaislurrylotno = ?,"
                + "fuutaijyuuryou1 = ?,fuutaijyuuryou2 = ?,fuutaijyuuryou3 = ?,soujyuuryou1 = ?,soujyuuryou2 = ?,soujyuuryou3 = ?,tenkazaislurryjyuuryou = ?,"
                + "kakuhanki = ?,kaitensuu = ?,kakuhanjikan = ?,kakuhankaisinichiji = ?,kakuhansyuuryounichiji = ?,kansouzarasyurui = ?,arumizarafuutaijyuuryou1 = ?,"
                + "kansoumaeslurryjyuuryou1 = ?,arumizarafuutaijyuuryou2 = ?,kansoumaeslurryjyuuryou2 = ?,arumizarafuutaijyuuryou3 = ?,kansoumaeslurryjyuuryou3 = ?,"
                + "kansouki1 = ?,kansouondo1 = ?,kansoujikan1 = ?,kansoukaisinichiji1 = ?,kansousyuuryounichiji1 = ?,kansouki2 = ?,kansouondo2 = ?,kansoujikan2 = ?,"
                + "kansoukaisinichiji2 = ?,kansousyuuryounichiji2 = ?,reikyakujikan = ?,kansougosoujyuuryou1 = ?,kansougosoujyuuryou2 = ?,kansougosoujyuuryou3 = ?,"
                + "kansougosyoumijyuuryou1 = ?,kansougosyoumijyuuryou2 = ?,kansougosyoumijyuuryou3 = ?,kokeibunhiritu1 = ?,kokeibunhiritu2 = ?,kokeibunhiritu3 = ?,"
                + "kokeibunhirituheikin = ?,kokeibunsokuteitantousya = ?,bikou1 = ?,bikou2 = ?,kosinnichiji = ?,revision = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrYuudentaiTenkazai> srYuudentaiTenkazaiList = getSrYuudentaiTenkazaiData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrYuudentaiTenkazai srYuudentaiTenkazai = null;
        if (!srYuudentaiTenkazaiList.isEmpty()) {
            srYuudentaiTenkazai = srYuudentaiTenkazaiList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrYuudentaiTenkazai(false, newRev, "", "", "", systemTime, processData, srYuudentaiTenkazai);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定(sr_yuudentai_tenkazai)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srYuudentaiTenkazai 誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrYuudentaiTenkazai(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            String systemTime, ProcessData processData, SrYuudentaiTenkazai srYuudentaiTenkazai) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // 撹拌開始日時
        String kakuhankaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B019Const.KAKUHANKAISI_TIME, srYuudentaiTenkazai));
        // 撹拌終了日時
        String kakuhansyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B019Const.KAKUHANSYUURYOU_TIME, srYuudentaiTenkazai));
        // 乾燥開始日時①
        String kansoukaisi1Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B019Const.KANSOUKAISI1_TIME, srYuudentaiTenkazai));
        // 乾燥終了日時①
        String kansousyuuryou1Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B019Const.KANSOUSYUURYOU1_TIME, srYuudentaiTenkazai));
        // 乾燥開始日時②
        String kansoukaisi2Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B019Const.KANSOUKAISI2_TIME, srYuudentaiTenkazai));
        // 乾燥終了日時②
        String kansousyuuryou2Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B019Const.KANSOUSYUURYOU2_TIME, srYuudentaiTenkazai));
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B019Const.YUUDENTAISLURRYHINMEI, srYuudentaiTenkazai))); // 誘電体ｽﾗﾘｰ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B019Const.YUUDENTAISLURRYLOTNO, srYuudentaiTenkazai))); // 誘電体ｽﾗﾘｰLotNo
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B019Const.LOTKUBUN, srYuudentaiTenkazai))); // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B019Const.GENRYOULOTNO, srYuudentaiTenkazai))); // 原料LotNo
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B019Const.GENRYOUKIGOU, srYuudentaiTenkazai))); // 原料記号
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B019Const.TENKAZAISLURRYHINMEI, srYuudentaiTenkazai))); // 添加材ｽﾗﾘｰ品名
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B019Const.TENKAZAISLURRYLOTNO, srYuudentaiTenkazai))); // 添加材ｽﾗﾘｰLotNo
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B019Const.FUUTAIJYUURYOU1, srYuudentaiTenkazai))); // 風袋重量①
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B019Const.FUUTAIJYUURYOU2, srYuudentaiTenkazai))); // 風袋重量②
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B019Const.FUUTAIJYUURYOU3, srYuudentaiTenkazai))); // 風袋重量③
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B019Const.SOUJYUURYOU1, srYuudentaiTenkazai))); // 総重量①
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B019Const.SOUJYUURYOU2, srYuudentaiTenkazai))); // 総重量②
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B019Const.SOUJYUURYOU3, srYuudentaiTenkazai))); // 総重量③
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B019Const.TENKAZAISLURRYJYUURYOU, srYuudentaiTenkazai))); // 添加材ｽﾗﾘｰ重量
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B019Const.KAKUHANKI, srYuudentaiTenkazai))); // 撹拌機
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B019Const.KAITENSUU, srYuudentaiTenkazai))); // 回転数
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B019Const.KAKUHANJIKAN, srYuudentaiTenkazai))); // 撹拌時間
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B019Const.KAKUHANKAISI_DAY, srYuudentaiTenkazai),
                "".equals(kakuhankaisiTime) ? "0000" : kakuhankaisiTime)); // 撹拌開始日時
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B019Const.KAKUHANSYUURYOU_DAY, srYuudentaiTenkazai),
                "".equals(kakuhansyuuryouTime) ? "0000" : kakuhansyuuryouTime)); // 撹拌終了日時
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B019Const.KANSOUZARASYURUI, srYuudentaiTenkazai))); // 乾燥皿種類
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B019Const.ARUMIZARAFUUTAIJYUURYOU1, srYuudentaiTenkazai))); // ｱﾙﾐ皿風袋重量①
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B019Const.KANSOUMAESLURRYJYUURYOU1, srYuudentaiTenkazai))); // 乾燥前ｽﾗﾘｰ重量①
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B019Const.ARUMIZARAFUUTAIJYUURYOU2, srYuudentaiTenkazai))); // ｱﾙﾐ皿風袋重量②
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B019Const.KANSOUMAESLURRYJYUURYOU2, srYuudentaiTenkazai))); // 乾燥前ｽﾗﾘｰ重量②
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B019Const.ARUMIZARAFUUTAIJYUURYOU3, srYuudentaiTenkazai))); // ｱﾙﾐ皿風袋重量③
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B019Const.KANSOUMAESLURRYJYUURYOU3, srYuudentaiTenkazai))); // 乾燥前ｽﾗﾘｰ重量③
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B019Const.KANSOUKI1, srYuudentaiTenkazai))); // 乾燥機①
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B019Const.KANSOUONDO1, srYuudentaiTenkazai))); // 乾燥温度①
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B019Const.KANSOUJIKAN1, srYuudentaiTenkazai))); // 乾燥時間①
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B019Const.KANSOUKAISI1_DAY, srYuudentaiTenkazai),
                "".equals(kansoukaisi1Time) ? "0000" : kansoukaisi1Time)); // 乾燥開始日時①
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B019Const.KANSOUSYUURYOU1_DAY, srYuudentaiTenkazai),
                "".equals(kansousyuuryou1Time) ? "0000" : kansousyuuryou1Time)); // 乾燥終了日時①
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B019Const.KANSOUKI2, srYuudentaiTenkazai))); // 乾燥機②
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B019Const.KANSOUONDO2, srYuudentaiTenkazai))); // 乾燥温度②
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B019Const.KANSOUJIKAN2, srYuudentaiTenkazai))); // 乾燥時間②
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B019Const.KANSOUKAISI2_DAY, srYuudentaiTenkazai),
                "".equals(kansoukaisi2Time) ? "0000" : kansoukaisi2Time)); // 乾燥開始日時②
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B019Const.KANSOUSYUURYOU2_DAY, srYuudentaiTenkazai),
                "".equals(kansousyuuryou2Time) ? "0000" : kansousyuuryou2Time)); // 乾燥終了日時②
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B019Const.REIKYAKUJIKAN, srYuudentaiTenkazai))); // 冷却時間
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B019Const.KANSOUGOSOUJYUURYOU1, srYuudentaiTenkazai))); // 乾燥後総重量①
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B019Const.KANSOUGOSOUJYUURYOU2, srYuudentaiTenkazai))); // 乾燥後総重量②
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B019Const.KANSOUGOSOUJYUURYOU3, srYuudentaiTenkazai))); // 乾燥後総重量③
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B019Const.KANSOUGOSYOUMIJYUURYOU1, srYuudentaiTenkazai))); // 乾燥後正味重量①
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B019Const.KANSOUGOSYOUMIJYUURYOU2, srYuudentaiTenkazai))); // 乾燥後正味重量②
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B019Const.KANSOUGOSYOUMIJYUURYOU3, srYuudentaiTenkazai))); // 乾燥後正味重量③
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B019Const.KOKEIBUNHIRITU1, srYuudentaiTenkazai))); // 固形分比率①
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B019Const.KOKEIBUNHIRITU2, srYuudentaiTenkazai))); // 固形分比率②
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B019Const.KOKEIBUNHIRITU3, srYuudentaiTenkazai))); // 固形分比率③
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B019Const.KOKEIBUNHIRITUHEIKIN, srYuudentaiTenkazai))); // 固形分比率平均
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B019Const.KOKEIBUNSOKUTEITANTOUSYA, srYuudentaiTenkazai))); // 固形分測定担当者
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B019Const.BIKOU1, srYuudentaiTenkazai))); // 備考1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B019Const.BIKOU2, srYuudentaiTenkazai))); // 備考2

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
     * 誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定(sr_yuudentai_tenkazai)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSrYuudentaiTenkazai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sr_yuudentai_tenkazai "
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
     * [誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_yuudentai_tenkazai "
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
     * @param srYuudentaiTenkazai 誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定データ
     * @return DB値
     */
    private String getSrYuudentaiTenkazaiItemData(String itemId, SrYuudentaiTenkazai srYuudentaiTenkazai) {
        switch (itemId) {
            // 誘電体ｽﾗﾘｰ品名
            case GXHDO102B019Const.YUUDENTAISLURRYHINMEI:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getYuudentaislurryhinmei());

            // 誘電体ｽﾗﾘｰLotNo
            case GXHDO102B019Const.YUUDENTAISLURRYLOTNO:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getYuudentaislurrylotno());

            // ﾛｯﾄ区分
            case GXHDO102B019Const.LOTKUBUN:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getLotkubun());

            // 原料LotNo
            case GXHDO102B019Const.GENRYOULOTNO:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getGenryoulotno());

            // 原料記号
            case GXHDO102B019Const.GENRYOUKIGOU:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getGenryoukigou());

            // 添加材ｽﾗﾘｰ品名
            case GXHDO102B019Const.TENKAZAISLURRYHINMEI:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getTenkazaislurryhinmei());

            // 添加材ｽﾗﾘｰLotNo
            case GXHDO102B019Const.TENKAZAISLURRYLOTNO:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getTenkazaislurrylotno());

            // 風袋重量①
            case GXHDO102B019Const.FUUTAIJYUURYOU1:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getFuutaijyuuryou1());

            // 風袋重量②
            case GXHDO102B019Const.FUUTAIJYUURYOU2:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getFuutaijyuuryou2());

            // 風袋重量③
            case GXHDO102B019Const.FUUTAIJYUURYOU3:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getFuutaijyuuryou3());

            // 総重量①
            case GXHDO102B019Const.SOUJYUURYOU1:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getSoujyuuryou1());

            // 総重量②
            case GXHDO102B019Const.SOUJYUURYOU2:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getSoujyuuryou2());

            // 総重量③
            case GXHDO102B019Const.SOUJYUURYOU3:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getSoujyuuryou3());

            // 添加材ｽﾗﾘｰ重量
            case GXHDO102B019Const.TENKAZAISLURRYJYUURYOU:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getTenkazaislurryjyuuryou());

            // 撹拌機
            case GXHDO102B019Const.KAKUHANKI:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getKakuhanki());

            // 回転数
            case GXHDO102B019Const.KAITENSUU:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getKaitensuu());

            // 撹拌時間
            case GXHDO102B019Const.KAKUHANJIKAN:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getKakuhanjikan());

            // 撹拌開始日
            case GXHDO102B019Const.KAKUHANKAISI_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiTenkazai.getKakuhankaisinichiji(), "yyMMdd");

            // 撹拌開始時間
            case GXHDO102B019Const.KAKUHANKAISI_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiTenkazai.getKakuhankaisinichiji(), "HHmm");

            // 撹拌終了日
            case GXHDO102B019Const.KAKUHANSYUURYOU_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiTenkazai.getKakuhansyuuryounichiji(), "yyMMdd");

            // 撹拌終了時間
            case GXHDO102B019Const.KAKUHANSYUURYOU_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiTenkazai.getKakuhansyuuryounichiji(), "HHmm");

            // 乾燥皿種類
            case GXHDO102B019Const.KANSOUZARASYURUI:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getKansouzarasyurui());

            // ｱﾙﾐ皿風袋重量①
            case GXHDO102B019Const.ARUMIZARAFUUTAIJYUURYOU1:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getArumizarafuutaijyuuryou1());

            // 乾燥前ｽﾗﾘｰ重量①
            case GXHDO102B019Const.KANSOUMAESLURRYJYUURYOU1:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getKansoumaeslurryjyuuryou1());

            // ｱﾙﾐ皿風袋重量②
            case GXHDO102B019Const.ARUMIZARAFUUTAIJYUURYOU2:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getArumizarafuutaijyuuryou2());

            // 乾燥前ｽﾗﾘｰ重量②
            case GXHDO102B019Const.KANSOUMAESLURRYJYUURYOU2:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getKansoumaeslurryjyuuryou2());

            // ｱﾙﾐ皿風袋重量③
            case GXHDO102B019Const.ARUMIZARAFUUTAIJYUURYOU3:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getArumizarafuutaijyuuryou3());

            // 乾燥前ｽﾗﾘｰ重量③
            case GXHDO102B019Const.KANSOUMAESLURRYJYUURYOU3:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getKansoumaeslurryjyuuryou3());

            // 乾燥機①
            case GXHDO102B019Const.KANSOUKI1:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getKansouki1());

            // 乾燥温度①
            case GXHDO102B019Const.KANSOUONDO1:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getKansouondo1());

            // 乾燥時間①
            case GXHDO102B019Const.KANSOUJIKAN1:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getKansoujikan1());

            // 乾燥開始日①
            case GXHDO102B019Const.KANSOUKAISI1_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiTenkazai.getKansoukaisinichiji1(), "yyMMdd");

            // 乾燥開始時間①
            case GXHDO102B019Const.KANSOUKAISI1_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiTenkazai.getKansoukaisinichiji1(), "HHmm");

            // 乾燥終了日①
            case GXHDO102B019Const.KANSOUSYUURYOU1_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiTenkazai.getKansousyuuryounichiji1(), "yyMMdd");

            // 乾燥終了時間①
            case GXHDO102B019Const.KANSOUSYUURYOU1_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiTenkazai.getKansousyuuryounichiji1(), "HHmm");

            // 乾燥機②
            case GXHDO102B019Const.KANSOUKI2:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getKansouki2());

            // 乾燥温度②
            case GXHDO102B019Const.KANSOUONDO2:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getKansouondo2());

            // 乾燥時間②
            case GXHDO102B019Const.KANSOUJIKAN2:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getKansoujikan2());

            // 乾燥開始日②
            case GXHDO102B019Const.KANSOUKAISI2_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiTenkazai.getKansoukaisinichiji2(), "yyMMdd");

            // 乾燥開始時間②
            case GXHDO102B019Const.KANSOUKAISI2_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiTenkazai.getKansoukaisinichiji2(), "HHmm");

            // 乾燥終了日②
            case GXHDO102B019Const.KANSOUSYUURYOU2_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiTenkazai.getKansousyuuryounichiji2(), "yyMMdd");

            // 乾燥終了時間②
            case GXHDO102B019Const.KANSOUSYUURYOU2_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiTenkazai.getKansousyuuryounichiji2(), "HHmm");

            // 冷却時間
            case GXHDO102B019Const.REIKYAKUJIKAN:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getReikyakujikan());

            // 乾燥後総重量①
            case GXHDO102B019Const.KANSOUGOSOUJYUURYOU1:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getKansougosoujyuuryou1());

            // 乾燥後総重量②
            case GXHDO102B019Const.KANSOUGOSOUJYUURYOU2:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getKansougosoujyuuryou2());

            // 乾燥後総重量③
            case GXHDO102B019Const.KANSOUGOSOUJYUURYOU3:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getKansougosoujyuuryou3());

            // 乾燥後正味重量①
            case GXHDO102B019Const.KANSOUGOSYOUMIJYUURYOU1:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getKansougosyoumijyuuryou1());

            // 乾燥後正味重量②
            case GXHDO102B019Const.KANSOUGOSYOUMIJYUURYOU2:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getKansougosyoumijyuuryou2());

            // 乾燥後正味重量③
            case GXHDO102B019Const.KANSOUGOSYOUMIJYUURYOU3:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getKansougosyoumijyuuryou3());

            // 固形分比率①
            case GXHDO102B019Const.KOKEIBUNHIRITU1:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getKokeibunhiritu1());

            // 固形分比率②
            case GXHDO102B019Const.KOKEIBUNHIRITU2:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getKokeibunhiritu2());

            // 固形分比率③
            case GXHDO102B019Const.KOKEIBUNHIRITU3:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getKokeibunhiritu3());

            // 固形分比率平均
            case GXHDO102B019Const.KOKEIBUNHIRITUHEIKIN:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getKokeibunhirituheikin());

            // 固形分測定担当者
            case GXHDO102B019Const.KOKEIBUNSOKUTEITANTOUSYA:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getKokeibunsokuteitantousya());

            // 備考1
            case GXHDO102B019Const.BIKOU1:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getBikou1());

            // 備考2
            case GXHDO102B019Const.BIKOU2:
                return StringUtil.nullToBlank(srYuudentaiTenkazai.getBikou2());

            default:
                return null;
        }
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定_仮登録(tmp_sr_yuudentai_tenkazai)登録処理(削除時)
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
    private void insertDeleteDataTmpSrYuudentaiTenkazai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_yuudentai_tenkazai ( "
                + " kojyo,lotno,edaban,yuudentaislurryhinmei,yuudentaislurrylotno,lotkubun,genryoulotno,genryoukigou,tenkazaislurryhinmei,"
                + "tenkazaislurrylotno,fuutaijyuuryou1,fuutaijyuuryou2,fuutaijyuuryou3,soujyuuryou1,soujyuuryou2,soujyuuryou3,"
                + "tenkazaislurryjyuuryou,kakuhanki,kaitensuu,kakuhanjikan,kakuhankaisinichiji,kakuhansyuuryounichiji,kansouzarasyurui,"
                + "arumizarafuutaijyuuryou1,kansoumaeslurryjyuuryou1,arumizarafuutaijyuuryou2,kansoumaeslurryjyuuryou2,arumizarafuutaijyuuryou3,"
                + "kansoumaeslurryjyuuryou3,kansouki1,kansouondo1,kansoujikan1,kansoukaisinichiji1,kansousyuuryounichiji1,kansouki2,kansouondo2,"
                + "kansoujikan2,kansoukaisinichiji2,kansousyuuryounichiji2,reikyakujikan,kansougosoujyuuryou1,kansougosoujyuuryou2,"
                + "kansougosoujyuuryou3,kansougosyoumijyuuryou1,kansougosyoumijyuuryou2,kansougosyoumijyuuryou3,kokeibunhiritu1,kokeibunhiritu2,"
                + "kokeibunhiritu3,kokeibunhirituheikin,kokeibunsokuteitantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") SELECT "
                + " kojyo,lotno,edaban,yuudentaislurryhinmei,yuudentaislurrylotno,lotkubun,genryoulotno,genryoukigou,tenkazaislurryhinmei,"
                + "tenkazaislurrylotno,fuutaijyuuryou1,fuutaijyuuryou2,fuutaijyuuryou3,soujyuuryou1,soujyuuryou2,soujyuuryou3,"
                + "tenkazaislurryjyuuryou,kakuhanki,kaitensuu,kakuhanjikan,kakuhankaisinichiji,kakuhansyuuryounichiji,kansouzarasyurui,"
                + "arumizarafuutaijyuuryou1,kansoumaeslurryjyuuryou1,arumizarafuutaijyuuryou2,kansoumaeslurryjyuuryou2,arumizarafuutaijyuuryou3,"
                + "kansoumaeslurryjyuuryou3,kansouki1,kansouondo1,kansoujikan1,kansoukaisinichiji1,kansousyuuryounichiji1,kansouki2,kansouondo2,"
                + "kansoujikan2,kansoukaisinichiji2,kansousyuuryounichiji2,reikyakujikan,kansougosoujyuuryou1,kansougosoujyuuryou2,"
                + "kansougosoujyuuryou3,kansougosyoumijyuuryou1,kansougosyoumijyuuryou2,kansougosyoumijyuuryou3,kokeibunhiritu1,kokeibunhiritu2,"
                + "kokeibunhiritu3,kokeibunhirituheikin,kokeibunsokuteitantousya,bikou1,bikou2,?,?,?,? "
                + " FROM sr_yuudentai_tenkazai "
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
        String syurui = "誘電体ｽﾗﾘｰ作製";
        // [ﾊﾟﾗﾒｰﾀﾏｽﾀ]から、ﾃﾞｰﾀを取得
        Map fxhbm03Data = loadFxhbm03Data(queryRunnerDoc, "誘電体ｽﾗﾘｰ作製_添加材ｽﾗﾘｰ固形分測定_表示制御");
        // 画面非表示項目リスト:風袋重量②、風袋重量③、総重量②、総重量③、回転数、ｱﾙﾐ皿風袋重量③、乾燥前ｽﾗﾘｰ重量③、乾燥機②
        // 乾燥温度②、乾燥時間②、乾燥開始日②、乾燥開始時間②、乾燥終了日②、乾燥終了時間②、乾燥後総重量③、乾燥後正味重量③、固形分比率③
        List<String> notShowItemList = Arrays.asList(GXHDO102B019Const.FUUTAIJYUURYOU2, GXHDO102B019Const.FUUTAIJYUURYOU3, GXHDO102B019Const.SOUJYUURYOU2, GXHDO102B019Const.SOUJYUURYOU3,
                GXHDO102B019Const.KAITENSUU, GXHDO102B019Const.ARUMIZARAFUUTAIJYUURYOU3, GXHDO102B019Const.KANSOUMAESLURRYJYUURYOU3, GXHDO102B019Const.KANSOUKI2,
                GXHDO102B019Const.KANSOUONDO2, GXHDO102B019Const.KANSOUJIKAN2, GXHDO102B019Const.KANSOUKAISI2_DAY, GXHDO102B019Const.KANSOUKAISI2_TIME, GXHDO102B019Const.KANSOUSYUURYOU2_DAY,
                GXHDO102B019Const.KANSOUSYUURYOU2_TIME, GXHDO102B019Const.KANSOUGOSOUJYUURYOU3, GXHDO102B019Const.KANSOUGOSYOUMIJYUURYOU3, GXHDO102B019Const.KOKEIBUNHIRITU3);
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

}
