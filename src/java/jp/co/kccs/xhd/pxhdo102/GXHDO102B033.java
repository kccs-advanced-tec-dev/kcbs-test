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
import jp.co.kccs.xhd.common.ErrorListMessage;
import jp.co.kccs.xhd.common.InitMessage;
import jp.co.kccs.xhd.common.KikakuError;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.db.model.SikakariJson;
import jp.co.kccs.xhd.db.model.SrSlipSlipkokeibunsokutei;
import jp.co.kccs.xhd.db.model.SubSrSlipSlipkokeibunsokutei;
import jp.co.kccs.xhd.model.GXHDO102C016Model;
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
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.primefaces.context.RequestContext;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/12/13<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * 変更日	2022/05/16<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	材料品名ﾘﾝｸ押下時、調合量規格チェックの追加<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B033(ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定)
 *
 * @author KCSS K.Jo
 * @since 2021/12/13
 */
public class GXHDO102B033 implements IFormLogic {

    private static final Logger LOGGER = Logger.getLogger(GXHDO102B033.class.getName());
    private static final String JOTAI_FLG_KARI_TOROKU = "0";
    private static final String JOTAI_FLG_TOROKUZUMI = "1";
    private static final String JOTAI_FLG_SAKUJO = "9";
    private static final String SQL_STATE_RECORD_LOCK_ERR = "55P03";

    /**
     * コンストラクタ
     */
    public GXHDO102B033() {
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

            List<String> errorMassageList = checkExistFormItem(processData);
            if (0 < errorMassageList.size()) {
                processData.setFatalError(true);
                processData.setInitMessageList(errorMassageList);
                processData.setMethod("openInitMessage");
                return processData;
            }

            // 初期設定
            initGXHDO102B033A(processData);

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
                    GXHDO102B033Const.BTN_EDABAN_COPY_TOP,
                    GXHDO102B033Const.BTN_KANSOUKAISI1_TOP,
                    GXHDO102B033Const.BTN_KANSOUSYUURYOU1_TOP,
                    GXHDO102B033Const.BTN_KANSOUKAISI2_TOP,
                    GXHDO102B033Const.BTN_KANSOUSYUURYOU2_TOP,
                    GXHDO102B033Const.BTN_YOUZAIKEIRYOU_TOP,
                    GXHDO102B033Const.BTN_KAKUHANSYUURYOU_TOP,
                    GXHDO102B033Const.BTN_HMKK_KKHSYUURYOU_TOP,
                    GXHDO102B033Const.BTN_HAISYUTUKAISI_TOP,
                    GXHDO102B033Const.BTN_HAISYUTUSYUURYOU_TOP,
                    GXHDO102B033Const.BTN_EDABAN_COPY_BOTTOM,
                    GXHDO102B033Const.BTN_KANSOUKAISI1_BOTTOM,
                    GXHDO102B033Const.BTN_KANSOUSYUURYOU1_BOTTOM,
                    GXHDO102B033Const.BTN_KANSOUKAISI2_BOTTOM,
                    GXHDO102B033Const.BTN_KANSOUSYUURYOU2_BOTTOM,
                    GXHDO102B033Const.BTN_YOUZAIKEIRYOU_BOTTOM,
                    GXHDO102B033Const.BTN_KAKUHANSYUURYOU_BOTTOM,
                    GXHDO102B033Const.BTN_HMKK_KKHSYUURYOU_BOTTOM,
                    GXHDO102B033Const.BTN_HAISYUTUKAISI_BOTTOM,
                    GXHDO102B033Const.BTN_HAISYUTUSYUURYOU_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO102B033Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO102B033Const.BTN_INSERT_TOP,
                    GXHDO102B033Const.BTN_DELETE_TOP,
                    GXHDO102B033Const.BTN_UPDATE_TOP,
                    GXHDO102B033Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO102B033Const.BTN_INSERT_BOTTOM,
                    GXHDO102B033Const.BTN_DELETE_BOTTOM,
                    GXHDO102B033Const.BTN_UPDATE_BOTTOM
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
            case GXHDO102B033Const.BTN_EDABAN_COPY_TOP:
            case GXHDO102B033Const.BTN_EDABAN_COPY_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 仮登録
            case GXHDO102B033Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO102B033Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO102B033Const.BTN_INSERT_TOP:
            case GXHDO102B033Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 修正
            case GXHDO102B033Const.BTN_UPDATE_TOP:
            case GXHDO102B033Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO102B033Const.BTN_DELETE_TOP:
            case GXHDO102B033Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 乾燥開始日時①
            case GXHDO102B033Const.BTN_KANSOUKAISI1_TOP:
            case GXHDO102B033Const.BTN_KANSOUKAISI1_BOTTOM:
                method = "setKansoukaisi1DateTime";
                break;
            // 乾燥終了日時①
            case GXHDO102B033Const.BTN_KANSOUSYUURYOU1_TOP:
            case GXHDO102B033Const.BTN_KANSOUSYUURYOU1_BOTTOM:
                method = "setKansousyuuryou1DateTime";
                break;
            // 乾燥開始日時②
            case GXHDO102B033Const.BTN_KANSOUKAISI2_TOP:
            case GXHDO102B033Const.BTN_KANSOUKAISI2_BOTTOM:
                method = "setKansoukaisi2DateTime";
                break;
            // 乾燥終了日時②
            case GXHDO102B033Const.BTN_KANSOUSYUURYOU2_TOP:
            case GXHDO102B033Const.BTN_KANSOUSYUURYOU2_BOTTOM:
                method = "setKansousyuuryou2DateTime";
                break;
            // 乾燥後正味重量計算
            case GXHDO102B033Const.BTN_KANSOUGOSYOUMIJYUURYOU_TOP:
            case GXHDO102B033Const.BTN_KANSOUGOSYOUMIJYUURYOU_BOTTOM:
                method = "setKansougosyoumijyuuryou";
                break;
            // 溶剤調整量計算
            case GXHDO102B033Const.BTN_YOUZAITYOUSEIRYOU_TOP:
            case GXHDO102B033Const.BTN_YOUZAITYOUSEIRYOU_BOTTOM:
                method = "setYouzaityouseiryou";
                break;
            // 溶剤秤量日時
            case GXHDO102B033Const.BTN_YOUZAIKEIRYOU_TOP:
            case GXHDO102B033Const.BTN_YOUZAIKEIRYOU_BOTTOM:
                method = "setYouzaikeiryouDateTime";
                break;
            // 撹拌_撹拌開始日時
            case GXHDO102B033Const.BTN_KAKUHANKAISI_TOP:
            case GXHDO102B033Const.BTN_KAKUHANKAISI_BOTTOM:
                method = "setKakuhankaisiDateTime";
                break;
            // 撹拌_撹拌終了日時
            case GXHDO102B033Const.BTN_KAKUHANSYUURYOU_TOP:
            case GXHDO102B033Const.BTN_KAKUHANSYUURYOU_BOTTOM:
                method = "setKakuhansyuuryouDateTime";
                break;
            // 排出前撹拌_撹拌開始日時
            case GXHDO102B033Const.BTN_HMKK_KKHKAISI_TOP:
            case GXHDO102B033Const.BTN_HMKK_KKHKAISI_BOTTOM:
                method = "setHmkkkkhkaisiDateTime";
                break;
            // 排出前撹拌_撹拌終了日時
            case GXHDO102B033Const.BTN_HMKK_KKHSYUURYOU_TOP:
            case GXHDO102B033Const.BTN_HMKK_KKHSYUURYOU_BOTTOM:
                method = "setHmkkkkhsyuuryouDateTime";
                break;
            // 排出開始日時
            case GXHDO102B033Const.BTN_HAISYUTUKAISI_TOP:
            case GXHDO102B033Const.BTN_HAISYUTUKAISI_BOTTOM:
                method = "setHaisyutukaisiDateTime";
                break;
            // 排出終了日時
            case GXHDO102B033Const.BTN_HAISYUTUSYUURYOU_TOP:
            case GXHDO102B033Const.BTN_HAISYUTUSYUURYOU_BOTTOM:
                method = "setHaisyutusyuuryouDateTime";
                break;
            // 溶剤1_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B033Const.BTN_OPENC016SUBGAMEN1:
                method = "openC016SubGamen1";
                break;
            // 溶剤2_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B033Const.BTN_OPENC016SUBGAMEN2:
                method = "openC016SubGamen2";
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

            // [原材料品質DB登録実績]から、ﾃﾞｰﾀを取得
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

            // ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定の入力項目の登録データ(仮登録時は仮登録データ)を取得
            List<SrSlipSlipkokeibunsokutei> srSlipSlipkokeibunsokuteiDataList = getSrSlipSlipkokeibunsokuteiData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo9, oyalotEdaban);
            if (srSlipSlipkokeibunsokuteiDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }
            // ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定入力_ｻﾌﾞ画面データ取得
            List<SubSrSlipSlipkokeibunsokutei> subSrSlipSlipkokeibunsokuteiDataList = getSubSrSlipSlipkokeibunsokuteiData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo9, oyalotEdaban);
            if (subSrSlipSlipkokeibunsokuteiDataList.isEmpty() || subSrSlipSlipkokeibunsokuteiDataList.size() != 2) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }
            // メイン画面データ設定
            setInputItemDataMainForm(processData, srSlipSlipkokeibunsokuteiDataList.get(0));
            // ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定入力_ｻﾌﾞ画面データ設定
            setInputItemDataSubFormC016(processData, subSrSlipSlipkokeibunsokuteiDataList);

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
     * 仮登録処理(データチェック処理)
     *
     * @param processData 処理データ
     * @return 処理データ
     */
    public ProcessData checkDataTempRegist(ProcessData processData) {
        // 規格チェック
        List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList = new ArrayList<>();
        ErrorMessageInfo errorMessageInfo = checkInputKikakuchi(processData, kikakuchiInputErrorInfoList, false);

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
     * 項目の規格値を設置して、調合量X_1と調合量X_2の合計値を計算して入力値に設置
     *
     * @param processData 処理データ
     * @param kikakuItem 規格値項目名
     * @param tyougouryouItemList 項目リスト
     * @return 項目データ
     */
    private FXHDD01 setKikakuValue(ProcessData processData, String kikakuItem, List<String> tyougouryouItemList) {
        FXHDD01 kikakuFxhdd01 = getItemRow(processData.getItemList(), kikakuItem);
        if (kikakuFxhdd01 == null) {
            return null;
        }
        int tyougouryouTotalVal = 0;
        FXHDD01 itemFxhdd01Clone = null;
        try {
            for (String tyougouryouItem : tyougouryouItemList) {
                FXHDD01 itemFxhdd01 = getItemRow(processData.getItemList(), tyougouryouItem);
                if (itemFxhdd01 == null) {
                    continue;
                }
                // 未入力項目に対しては規格値ﾁｪｯｸしない
                if (!StringUtil.isEmpty(itemFxhdd01.getValue())) {
                    if (!NumberUtil.isIntegerNumeric(itemFxhdd01.getValue())) {
                        return null;
                    }
                    // 規格値ﾁｪｯｸに調合量X_1と調合量X_2の合計値を利用するため、該当項目をCloneする
                    if (itemFxhdd01Clone == null) {
                        itemFxhdd01Clone = itemFxhdd01.clone();
                    }
                    tyougouryouTotalVal += Integer.parseInt(itemFxhdd01.getValue());
                }
            }
        } catch (CloneNotSupportedException ex) {
            ErrUtil.outputErrorLog("CloneNotSupportedException発生", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            return null;
        }
        if (itemFxhdd01Clone != null) {

            // 項目の規格値を設置
            itemFxhdd01Clone.setKikakuChi(kikakuFxhdd01.getKikakuChi());
            itemFxhdd01Clone.setStandardPattern(kikakuFxhdd01.getStandardPattern());
            itemFxhdd01Clone.setValue(String.valueOf(tyougouryouTotalVal));
        }
        return itemFxhdd01Clone;
    }

    /**
     * 項目の規格値と表示ﾗﾍﾞﾙ1を設置
     *
     * @param processData 処理データ
     * @param itemList 規格値の入力値チェック必要の項目リスト
     * @param tyougouryouList 調合量リスト
     * @param tyogouryoukikaku 調合量規格
     * @param label1 表示ﾗﾍﾞﾙ1
     */
    private void setKikakuValueAndLabel1(ProcessData processData, List<FXHDD01> itemList, List<String> tyougouryouList, String tyogouryoukikaku, String label1) {
        // 調合量規格Xの規格は調合量X_1と調合量X_2に設置
        FXHDD01 tyougouryouKikakuItemFxhdd01 = setKikakuValue(processData, tyogouryoukikaku, tyougouryouList);
        // 項目の項目名を設置
        if (tyougouryouKikakuItemFxhdd01 != null) {
            tyougouryouKikakuItemFxhdd01.setLabel1(label1);
            itemList.add(tyougouryouKikakuItemFxhdd01);
        }
    }

    /**
     * 規格値の入力値チェックを行う。
     * 規格値のエラー対象は引数のリスト(kikakuchiInputErrorInfoList)に項目情報を詰めて返される。
     *
     * @param processData 処理データ
     * @param kikakuchiInputErrorInfoList 規格値入力エラー情報リスト
     * @param diffMinutesCheckFlg 撹拌時間ﾁｪｯｸ用フラグ
     * @return チェックの正常終了時はNULL、異常時は内容に応じたエラーメッセージ情報を返す。
     */
    private ErrorMessageInfo checkInputKikakuchi(ProcessData processData, List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList, boolean diffMinutesCheckFlg) {
        List<String> tyougouryouList = new ArrayList<>();
        List<String> tyogouryoukikaku = Arrays.asList(GXHDO102B033Const.YOUZAI1_TYOUGOURYOUKIKAKU, GXHDO102B033Const.YOUZAI2_TYOUGOURYOUKIKAKU);
        List<String> youzai1_tyougouryouList = Arrays.asList(GXHDO102B033Const.YOUZAI1_TYOUGOURYOU1, GXHDO102B033Const.YOUZAI1_TYOUGOURYOU2); // 溶剤1_調合量
        List<String> youzai2_tyougouryouList = Arrays.asList(GXHDO102B033Const.YOUZAI2_TYOUGOURYOU1, GXHDO102B033Const.YOUZAI2_TYOUGOURYOU2); // 溶剤2_調合量

        // 規格値の入力値チェック必要の項目リスト
        List<FXHDD01> itemList = new ArrayList<>();
        setKikakuValueAndLabel1(processData, itemList, youzai1_tyougouryouList, tyogouryoukikaku.get(0), "溶剤①_調合量"); // 溶剤①_調合量の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, youzai2_tyougouryouList, tyogouryoukikaku.get(1), "溶剤②_調合量"); // 溶剤②_調合量の規格値と表示ﾗﾍﾞﾙ1を設置

        tyougouryouList.addAll(youzai1_tyougouryouList);
        tyougouryouList.addAll(youzai2_tyougouryouList);

        // 撹拌_撹拌時間ﾁｪｯｸ
        List<String> kakuhanjikanList = Arrays.asList(GXHDO102B033Const.KAKUHAN_KAKUHANKAISI_DAY, GXHDO102B033Const.KAKUHAN_KAKUHANKAISI_TIME, GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOU_DAY,
                GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOU_TIME);
        // 排出前撹拌_撹拌時間ﾁｪｯｸ
        List<String> haisyutumaekakuhanjikanList = Arrays.asList(GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANKAISI_DAY, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANKAISI_TIME,
                GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOU_DAY, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOU_TIME);
        if (diffMinutesCheckFlg) {
            // 撹拌時間の規格チェック用項目
            FXHDD01 kakuhanjikanDiffMinutes = getdiffMinutesKikakuItem(processData, GXHDO102B033Const.KAKUHAN_KAKUHANJIKAN, kakuhanjikanList);
            // 項目の項目名を設置
            if (kakuhanjikanDiffMinutes != null) {
                kakuhanjikanDiffMinutes.setLabel1("撹拌_撹拌時間");
                itemList.add(kakuhanjikanDiffMinutes);
            }
            // 排出前撹拌_撹拌時間チェック用項目
            FXHDD01 haisyutumaekakuhanjikanDiffMinutes = getdiffMinutesKikakuItem(processData, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANJIKAN, haisyutumaekakuhanjikanList);
            // 項目の項目名を設置
            if (haisyutumaekakuhanjikanDiffMinutes != null) {
                haisyutumaekakuhanjikanDiffMinutes.setLabel1("排出前撹拌_撹拌時間");
                itemList.add(haisyutumaekakuhanjikanDiffMinutes);
            }
        }

        processData.getItemList().stream().filter(
                (fxhdd01) -> !(tyougouryouList.contains(fxhdd01.getItemId()) || tyogouryoukikaku.contains(fxhdd01.getItemId()))).filter(
                        (fxhdd01) -> !(StringUtil.isEmpty(fxhdd01.getStandardPattern()) || "【-】".equals(fxhdd01.getKikakuChi()))).filter(
                        (fxhdd01) -> !(!ValidateUtil.isInputColumn(fxhdd01) || StringUtil.isEmpty(fxhdd01.getValue()))).forEachOrdered(
                        (fxhdd01) -> {
                            // 規格チェックの対象項目である、かつ入力項目かつ入力値がある項目はリストに追加
                            itemList.add(fxhdd01);
                        });
        ErrorMessageInfo errorMessageInfo = ValidateUtil.checkInputKikakuchi(itemList, kikakuchiInputErrorInfoList);
        // エラー項目の背景色を設定
        List<String> errorTyougouryouList = new ArrayList<>();

        kikakuchiInputErrorInfoList.stream().filter((errorInfo)
                -> (tyougouryouList.contains(errorInfo.getItemId()))).forEachOrdered((errorInfo) -> {
            errorTyougouryouList.add(errorInfo.getItemId());
        });
        if (errorMessageInfo == null) {
            errorTyougouryouList.stream().map((itemId) -> tyougouryouList.indexOf(itemId)).map((index) -> {
                if ((index + 1) % 2 == 0) {
                    index--;
                } else {
                    index++;
                }
                return index;
            }).map((index)
                    -> getItemRow(processData.getItemList(), tyougouryouList.get(index))).filter((itemFxhdd01)
                    -> (itemFxhdd01 != null)).forEachOrdered((itemFxhdd01)
                    -> {
                itemFxhdd01.setBackColorInput(ErrUtil.ERR_BACK_COLOR);
            });
        } else {
            String itemId = errorMessageInfo.getErrorItemInfoList().get(0).getItemId();
            int index = tyougouryouList.indexOf(itemId);
            if (index != -1) {
                if ((index + 1) % 2 == 0) {
                    index--;
                } else {
                    index++;
                }
                FXHDD01 itemFxhdd01 = getItemRow(processData.getItemList(), tyougouryouList.get(index));
                if (itemFxhdd01 != null) {
                    itemFxhdd01.setBackColorInput(ErrUtil.ERR_BACK_COLOR);
                }
            }
        }

        if (diffMinutesCheckFlg) {
            // エラー項目の背景色を設定
            setItemBackColor(processData, kakuhanjikanList, "撹拌_撹拌時間", kikakuchiInputErrorInfoList, errorMessageInfo);
            setItemBackColor(processData, haisyutumaekakuhanjikanList, "排出前撹拌_撹拌時間", kikakuchiInputErrorInfoList, errorMessageInfo);
        }
        return errorMessageInfo;
    }

    /**
     * 終了時間-開始時間で計算して、算出した差分分数を項目の規格値に設定する
     *
     * @param processData 処理データ
     * @param kikakuItem 規格値項目名
     * @param kakuhanjikanList 時間項目リスト
     * @return 項目データ
     */
    private FXHDD01 getdiffMinutesKikakuItem(ProcessData processData, String kikakuItem, List<String> kakuhanjikanList) {

        FXHDD01 kikakuFxhdd01 = getItemRow(processData.getItemList(), kikakuItem);
        if (kikakuFxhdd01 == null) {
            return null;
        }
        int diffMinutes;
        Date kaishijikan = null;
        Date syuuryoujikan = null;
        FXHDD01 itemFxhdd01Clone = null;
        try {
            // 開始日
            FXHDD01 itemKakuhankaisiDay = getItemRow(processData.getItemList(), kakuhanjikanList.get(0));
            // 開始時間
            FXHDD01 itemKakuhankaisiTime = getItemRow(processData.getItemList(), kakuhanjikanList.get(1));
            // 終了日
            FXHDD01 itemKakuhansyuuryouDay = getItemRow(processData.getItemList(), kakuhanjikanList.get(2));
            // 終了時間
            FXHDD01 itemKakuhansyuuryouTime = getItemRow(processData.getItemList(), kakuhanjikanList.get(3));
            if (itemKakuhankaisiDay == null || itemKakuhankaisiTime == null || itemKakuhansyuuryouDay == null || itemKakuhansyuuryouTime == null) {
                return null;
            }
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
            diffMinutes = DateUtil.diffMinutes(kaishijikan, syuuryoujikan);
        } catch (CloneNotSupportedException ex) {
            ErrUtil.outputErrorLog("CloneNotSupportedException発生", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            return null;
        }
        // 項目の規格値を設置
        itemFxhdd01Clone.setKikakuChi(kikakuFxhdd01.getKikakuChi());
        itemFxhdd01Clone.setStandardPattern(kikakuFxhdd01.getStandardPattern());
        itemFxhdd01Clone.setValue(String.valueOf(diffMinutes));
        return itemFxhdd01Clone;
    }

    /**
     * エラー項目の背景色を設定
     *
     * @param processData 処理データ
     * @param jikanList 時間項目リスト
     * @param label 項目の項目名
     * @param kikakuchiInputErrorInfoList 規格値入力エラー情報リスト
     * @param errorMessageInfo エラーメッセージ情報
     */
    private void setItemBackColor(ProcessData processData, List<String> jikanList, String label1, List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList,
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
            jikanList.stream().map((jikanName) -> getItemRow(processData.getItemList(), jikanName)).forEachOrdered((itemFxhdd01) -> {
                itemFxhdd01.setBackColorInput(ErrUtil.ERR_BACK_COLOR);
            });
        }
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
            int paramJissekino = (Integer) session.getAttribute("jissekino");
            String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
            String lotNo9 = lotNo.substring(3, 12); //ﾛｯﾄNo
            String edaban = lotNo.substring(12, 15); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // 品質DB登録実績データ取得
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

            BigDecimal newRev = BigDecimal.ONE;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            String strSystime = sdf.format(systemTime);

            BigDecimal rev = BigDecimal.ZERO;
            if (StringUtil.isEmpty(processData.getInitJotaiFlg())) {
                // 品質DB登録実績登録処理
                insertFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo9, edaban, paramJissekino, JOTAI_FLG_KARI_TOROKU, systemTime);
            } else {
                rev = new BigDecimal(processData.getInitRev());
                // 最新のリビジョンを採番
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo9, edaban, paramJissekino, formId);

                // 品質DB登録実績更新処理
                updateFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo9, edaban, JOTAI_FLG_KARI_TOROKU, systemTime, paramJissekino);
            }

            if (StringUtil.isEmpty(processData.getInitJotaiFlg()) || JOTAI_FLG_SAKUJO.equals(processData.getInitJotaiFlg())) {

                // ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定_仮登録処理
                insertTmpSrSlipSlipkokeibunsokutei(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, strSystime, processData);
                // ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定入力ｻﾌﾞ画面の仮登録処理
                for (int i = 1; i <= 2; i++) {
                    insertTmpSubSrSlipSlipkokeibunsokutei(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, i, strSystime);
                }
            } else {

                // ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定_仮登録更新処理
                updateTmpSrSlipSlipkokeibunsokutei(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);
                // ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定入力ｻﾌﾞ画面の仮登録更新処理
                for (int i = 1; i <= 2; i++) {
                    updateTmpSubSrSlipSlipkokeibunsokutei(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, i, strSystime);
                }
            }

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert102B(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, paramJissekino, "0", kikakuError.getKikakuchiInputErrorInfoList());
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
        ErrorMessageInfo errorMessageInfo = checkInputKikakuchi(processData, kikakuchiInputErrorInfoList, true);

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
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    private ErrorMessageInfo checkItemRegistCorrect(ProcessData processData) {
        // 乾燥開始日時①、乾燥終了日時①チェック
        FXHDD01 itemKaisiDay = getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUKAISI1_DAY); // 乾燥開始日①
        FXHDD01 itemKaisiTime = getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUKAISI1_TIME); // 乾燥開始時間①
        FXHDD01 itemSyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUSYUURYOU1_DAY); // 乾燥終了日①
        FXHDD01 itemSyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUSYUURYOU1_TIME); // 乾燥終了時間①
        ErrorMessageInfo errorMsg = checkItemR001(itemKaisiDay, itemKaisiTime, itemSyuuryouDay, itemSyuuryouTime);
        if (errorMsg != null) {
            return errorMsg;
        }

        // 乾燥開始日時②、乾燥終了日時②前後チェック
        itemKaisiDay = getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUKAISI2_DAY); // 乾燥開始日②
        itemKaisiTime = getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUKAISI2_TIME); // 乾燥開始時間②
        itemSyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUSYUURYOU2_DAY); // 乾燥終了日②
        itemSyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUSYUURYOU2_TIME); // 乾燥終了時間②
        errorMsg = checkItemR001(itemKaisiDay, itemKaisiTime, itemSyuuryouDay, itemSyuuryouTime);
        if (errorMsg != null) {
            return errorMsg;
        }

        // 撹拌_撹拌開始日時、撹拌_撹拌終了日時前後チェック
        itemKaisiDay = getItemRow(processData.getItemList(), GXHDO102B033Const.KAKUHAN_KAKUHANKAISI_DAY); // 撹拌_撹拌開始日
        itemKaisiTime = getItemRow(processData.getItemList(), GXHDO102B033Const.KAKUHAN_KAKUHANKAISI_TIME); // 撹拌_撹拌開始時間
        itemSyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOU_DAY); // 撹拌_撹拌終了日
        itemSyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOU_TIME); // 撹拌_撹拌終了時間
        errorMsg = checkItemR001(itemKaisiDay, itemKaisiTime, itemSyuuryouDay, itemSyuuryouTime);
        if (errorMsg != null) {
            return errorMsg;
        }

        // 排出前撹拌_撹拌開始日時、排出前撹拌_撹拌終了日時前後チェック
        itemKaisiDay = getItemRow(processData.getItemList(), GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANKAISI_DAY); // 排出前撹拌_撹拌開始日
        itemKaisiTime = getItemRow(processData.getItemList(), GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANKAISI_TIME); // 排出前撹拌_撹拌開始時間
        itemSyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOU_DAY); // 排出前撹拌_撹拌終了日
        itemSyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOU_TIME); // 排出前撹拌_撹拌終了時間
        errorMsg = checkItemR001(itemKaisiDay, itemKaisiTime, itemSyuuryouDay, itemSyuuryouTime);
        if (errorMsg != null) {
            return errorMsg;
        }

        // 排出開始日時、排出終了日時前後チェック
        itemKaisiDay = getItemRow(processData.getItemList(), GXHDO102B033Const.HAISYUTUKAISI_DAY); // 排出開始日
        itemKaisiTime = getItemRow(processData.getItemList(), GXHDO102B033Const.HAISYUTUKAISI_TIME); // 排出開始時間
        itemSyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B033Const.HAISYUTUSYUURYOU_DAY); // 排出終了日
        itemSyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B033Const.HAISYUTUSYUURYOU_TIME); // 排出終了時間
        errorMsg = checkItemR001(itemKaisiDay, itemKaisiTime, itemSyuuryouDay, itemSyuuryouTime);
        if (errorMsg != null) {
            return errorMsg;
        }
        return null;
    }

    /**
     * 時刻前後ﾁｪｯｸ
     *
     * @param itemKaisiDay 開始日
     * @param itemKaisiTime 開始時間
     * @param itemSyuuryouDay 終了日
     * @param itemSyuuryouTime 終了時間
     * @return 処理制御データ
     */
    private ErrorMessageInfo checkItemR001(FXHDD01 itemKaisiDay, FXHDD01 itemKaisiTime, FXHDD01 itemSyuuryouDay, FXHDD01 itemSyuuryouTime) {
        ValidateUtil validateUtil = new ValidateUtil();
        if (itemKaisiDay != null && itemKaisiTime != null && itemSyuuryouDay != null && itemSyuuryouTime != null) {
            Date kaisiDate = DateUtil.convertStringToDate(itemKaisiDay.getValue(), itemKaisiTime.getValue());
            Date syuuryouDate = DateUtil.convertStringToDate(itemSyuuryouDay.getValue(), itemSyuuryouTime.getValue());
            //R001チェック呼出し
            String msgCheckR001 = validateUtil.checkR001(itemKaisiDay.getLabel1().replace("日", "日時"), kaisiDate, itemSyuuryouDay.getLabel1().replace("日", "日時"), syuuryouDate);
            if (!StringUtil.isEmpty(msgCheckR001)) {
                //エラー発生時
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemKaisiDay, itemKaisiTime, itemSyuuryouDay, itemSyuuryouTime);
                return MessageUtil.getErrorMessageInfo("", msgCheckR001, true, true, errFxhdd01List);
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
            int paramJissekino = (Integer) session.getAttribute("jissekino");
            String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
            String lotNo9 = lotNo.substring(3, 12); //ﾛｯﾄNo
            String edaban = lotNo.substring(12, 15); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // 品質DB登録実績データ取得
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

            BigDecimal rev = BigDecimal.ZERO;
            BigDecimal newRev = BigDecimal.ONE;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            String strSystime = sdf.format(systemTime);

            if (StringUtil.isEmpty(processData.getInitRev())) {
                // 品質DB登録実績登録処理
                insertFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo9, edaban, paramJissekino, JOTAI_FLG_TOROKUZUMI, systemTime);
            } else {
                rev = new BigDecimal(processData.getInitRev());
                // 最新のリビジョンを採番
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo9, edaban, paramJissekino, formId);

                // 品質DB登録実績更新処理
                updateFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo9, edaban, JOTAI_FLG_TOROKUZUMI, systemTime, paramJissekino);
            }

            // 仮登録状態の場合、仮登録のデータを削除する。
            SrSlipSlipkokeibunsokutei tmpSrSlipSlipkokeibunsokutei = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrSlipSlipkokeibunsokutei> srSlipSlipkokeibunsokuteiList = getSrSlipSlipkokeibunsokuteiData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo9, edaban);
                if (!srSlipSlipkokeibunsokuteiList.isEmpty()) {
                    tmpSrSlipSlipkokeibunsokutei = srSlipSlipkokeibunsokuteiList.get(0);
                }

                deleteTmpSrSlipSlipkokeibunsokutei(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);
                deleteTmpSubSrSlipSlipkokeibunsokutei(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);
            }

            // ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定_登録処理
            insertSrSlipSlipkokeibunsokutei(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, strSystime, processData, tmpSrSlipSlipkokeibunsokutei);
            // ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定入力ｻﾌﾞ画面の仮登録更新処理
            for (int i = 1; i <= 2; i++) {
                insertSubSrSlipSlipkokeibunsokutei(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, i, strSystime);
            }
            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert102B(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, paramJissekino, "0", kikakuError.getKikakuchiInputErrorInfoList());
            }

            // 処理後はエラーリストをクリア
            kikakuError.setKikakuchiInputErrorInfoList(new ArrayList<>());
            DbUtils.commitAndCloseQuietly(conDoc);
            DbUtils.commitAndCloseQuietly(conQcdb);

            // 後続処理メソッド設定
            processData.setMethod("doPMLA0212");

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
     * 部材在庫の重量ﾃﾞｰﾀ連携
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doPMLA0212(ProcessData processData) {
        // セッションから情報を取得
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
        // 部材在庫の重量ﾃﾞｰﾀ連携
        String responseResult = doPMLA0212Save(processData, tantoshaCd);
        if (!"ok".equals(responseResult)) {
            return processData;
        }
        // 後続処理メソッド設定
        processData.setMethod("");
        // 完了メッセージとコールバックパラメータを設定
        setCompMessage("登録しました。");
        processData.setCollBackParam("complete");
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
        ErrorMessageInfo errorMessageInfo = checkInputKikakuchi(processData, kikakuchiInputErrorInfoList, true);

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
        processData.setUserAuthParam(GXHDO102B033Const.USER_AUTH_UPDATE_PARAM);

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
            String lotNo9 = lotNo.substring(3, 12); //ﾛｯﾄNo
            String edaban = lotNo.substring(12, 15); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // 品質DB登録実績データ取得
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
            // 品質DB登録実績更新処理
            updateFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo9, edaban, JOTAI_FLG_TOROKUZUMI, systemTime, paramJissekino);

            // ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定_更新処理
            updateSrSlipSlipkokeibunsokutei(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);
            // ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定入力ｻﾌﾞ画面の更新処理
            for (int i = 1; i <= 2; i++) {
                updateSubSrSlipSlipkokeibunsokutei(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, i, strSystime);
            }

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert102B(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, paramJissekino, "0", kikakuError.getKikakuchiInputErrorInfoList());
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
        processData.setUserAuthParam(GXHDO102B033Const.USER_AUTH_DELETE_PARAM);

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
            String lotNo9 = lotNo.substring(3, 12); //ﾛｯﾄNo
            String edaban = lotNo.substring(12, 15); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));

            // 品質DB登録実績データ取得
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
            // 品質DB登録実績更新処理
            updateFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo9, edaban, JOTAI_FLG_SAKUJO, systemTime, paramJissekino);

            // ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo9, edaban, paramJissekino);
            insertDeleteDataTmpSrSlipSlipkokeibunsokutei(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo9, edaban, strSystime);

            // ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定入力_ｻﾌﾞ画面仮登録登録処理
            insertDeleteDataTmpSubSrSlipSlipkokeibunsokutei(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo9, edaban, strSystime);

            // ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定_削除処理
            deleteSrSlipSlipkokeibunsokutei(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);

            // ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定入力_ｻﾌﾞ画面削除処理
            deleteSubSrSlipSlipkokeibunsokutei(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);

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
     * 乾燥開始日時①設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKansoukaisi1DateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUKAISI1_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUKAISI1_TIME);
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
    public ProcessData setKansousyuuryou1DateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUSYUURYOU1_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUSYUURYOU1_TIME);
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
    public ProcessData setKansoukaisi2DateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUKAISI2_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUKAISI2_TIME);
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
    public ProcessData setKansousyuuryou2DateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUSYUURYOU2_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUSYUURYOU2_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 乾燥後正味重量計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKansougosyoumijyuuryou(ProcessData processData) {
        clearItemListBackColor(processData);
        ErrorMessageInfo checkItemErrorInfo = checkKansougosyoumijyuuryouKeisan(processData);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        calcKansougosyoumijyuuryou(processData);
        return processData;
    }

    /**
     * 乾燥後正味重量計算ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
     *
     * @param processData 処理制御データ
     * @return エラーメッセージ情報
     */
    public ErrorMessageInfo checkKansougosyoumijyuuryouKeisan(ProcessData processData) {
        // 乾燥後総重量
        FXHDD01 kansougosoujyuuryou = getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUGOSOUJYUURYOU);
        // ﾙﾂﾎﾞ風袋重量
        FXHDD01 rutubofuutaijyuuryou = getItemRow(processData.getItemList(), GXHDO102B033Const.RUTUBOFUUTAIJYUURYOU);
        // 乾燥前ｽﾘｯﾌﾟ重量
        FXHDD01 kansoumaeslipjyuuryou = getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUMAESLIPJYUURYOU);
        //「乾燥後総重量」ﾁｪｯｸ
        if (kansougosoujyuuryou != null && StringUtil.isEmpty(kansougosoujyuuryou.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(kansougosoujyuuryou);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, true, errFxhdd01List, kansougosoujyuuryou.getLabel1());
        }
        //「ﾙﾂﾎﾞ風袋重量」ﾁｪｯｸ
        if (rutubofuutaijyuuryou != null && StringUtil.isEmpty(rutubofuutaijyuuryou.getValue())) {
            List<FXHDD01> errFxhdd01List = Arrays.asList(rutubofuutaijyuuryou);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, true, errFxhdd01List, rutubofuutaijyuuryou.getLabel1());
        }
        if (kansougosoujyuuryou != null && rutubofuutaijyuuryou != null) {
            // [乾燥後総重量]<[ﾙﾂﾎﾞ風袋重量]場合
            BigDecimal kansougosoujyuuryouVal = new BigDecimal(kansougosoujyuuryou.getValue());
            BigDecimal rutubofuutaijyuuryouVal = new BigDecimal(rutubofuutaijyuuryou.getValue());
            if (kansougosoujyuuryouVal.compareTo(rutubofuutaijyuuryouVal) < 0) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                List<FXHDD01> errFxhdd01List = Arrays.asList(kansougosoujyuuryou, rutubofuutaijyuuryou);
                return MessageUtil.getErrorMessageInfo("XHD-000023", true, true, errFxhdd01List, kansougosoujyuuryou.getLabel1(), rutubofuutaijyuuryou.getLabel1());
            }
        }
        // 「乾燥前ｽﾘｯﾌﾟ重量」ﾁｪｯｸ
        if (kansoumaeslipjyuuryou != null) {
            if ("".equals(StringUtil.nullToBlank(kansoumaeslipjyuuryou.getValue())) || BigDecimal.ZERO.compareTo(new BigDecimal(kansoumaeslipjyuuryou.getValue())) == 0) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                List<FXHDD01> errFxhdd01List = Arrays.asList(kansoumaeslipjyuuryou);
                return MessageUtil.getErrorMessageInfo("XHD-000181", true, true, errFxhdd01List, kansoumaeslipjyuuryou.getLabel1());
            }
        }
        return null;
    }

    /**
     * 乾燥後正味重量計算
     *
     * @param processData 処理制御データ
     */
    private void calcKansougosyoumijyuuryou(ProcessData processData) {
        // 乾燥後総重量
        FXHDD01 kansougosoujyuuryou = getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUGOSOUJYUURYOU);
        // ﾙﾂﾎﾞ風袋重量
        FXHDD01 rutubofuutaijyuuryou = getItemRow(processData.getItemList(), GXHDO102B033Const.RUTUBOFUUTAIJYUURYOU);
        // 乾燥前ｽﾘｯﾌﾟ重量
        FXHDD01 kansoumaeslipjyuuryou = getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUMAESLIPJYUURYOU);
        // 乾燥後正味重量
        FXHDD01 kansougosyoumijyuuryou = getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUGOSYOUMIJYUURYOU);
        // 固形分比率
        FXHDD01 kokeibunhiritu = getItemRow(processData.getItemList(), GXHDO102B033Const.KOKEIBUNHIRITU);
        try {
            if (kansougosoujyuuryou != null && rutubofuutaijyuuryou != null && kansougosyoumijyuuryou != null) {
                //「乾燥後正味重量」= 「乾燥後総重量」 - 「ﾙﾂﾎﾞ風袋重量」 を算出する。
                BigDecimal itemKansougosoujyuuryouVal = new BigDecimal(kansougosoujyuuryou.getValue());
                BigDecimal itemRutubofuutaijyuuryouVal = new BigDecimal(rutubofuutaijyuuryou.getValue());
                BigDecimal itemKansougosyoumijyuuryouVal = itemKansougosoujyuuryouVal.subtract(itemRutubofuutaijyuuryouVal);
                //計算結果の設定
                kansougosyoumijyuuryou.setValue(itemKansougosyoumijyuuryouVal.toPlainString());

                if (kokeibunhiritu != null && kansoumaeslipjyuuryou != null) {
                    // 乾燥前ｽﾘｯﾌﾟ重量
                    BigDecimal itemKansoumaeslipjyuuryouVal = new BigDecimal(kansoumaeslipjyuuryou.getValue());
                    // 「固形分比率」= 「乾燥後正味重量」 ÷ 「乾燥前ｽﾘｯﾌﾟ重量」 * 100(小数点第3位を四捨五入) → 式を変換して先に100を乗算
                    BigDecimal kokeibunhirituVal = itemKansougosyoumijyuuryouVal.multiply(BigDecimal.valueOf(100)).divide(itemKansoumaeslipjyuuryouVal, 2, RoundingMode.HALF_UP);
                    //計算結果の設定
                    kokeibunhiritu.setValue(kokeibunhirituVal.toPlainString());
                }
            }
        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
            ErrUtil.outputErrorLog(kansougosyoumijyuuryou.getLabel1() + "計算にエラー発生", ex, LOGGER);
        }
    }

    /**
     * 溶剤調整量計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setYouzaityouseiryou(ProcessData processData) {
        clearItemListBackColor(processData);
        // 溶剤調整量
        FXHDD01 youzaityouseiryou = getItemRow(processData.getItemList(), GXHDO102B033Const.YOUZAITYOUSEIRYOU);
        // 固形分比率
        FXHDD01 kokeibunhiritu = getItemRow(processData.getItemList(), GXHDO102B033Const.KOKEIBUNHIRITU);
        if (youzaityouseiryou == null || kokeibunhiritu == null) {
            processData.setMethod("");
            return processData;
        }
        try {
            QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
            QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            String lotNo = (String) session.getAttribute("lotNo");
            String kojyo = lotNo.substring(0, 3);
            String lotNo9 = lotNo.substring(3, 12);
            String edaban = lotNo.substring(12, 15);
            String rev = StringUtil.nullToBlank(processData.getInitRev());
            String tantoshaCd = (String) session.getAttribute("tantoshaCd");
            // (20)[ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)]から、ﾃﾞｰﾀを取得
            Map srSlipSlurrykokeibuntyouseiSiroporiData = loadSrSlipSlurrykokeibuntyouseiSiropori(queryRunnerQcdb, kojyo, lotNo9, edaban, rev);
            // (21)[ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入]から、ﾃﾞｰﾀを取得
            Map srSlipBinderhyouryouTounyuuData = loadSrSlipBinderhyouryouTounyuu(queryRunnerQcdb, kojyo, lotNo9, edaban, rev);
            // (23)[ｽﾘｯﾌﾟ作製・溶剤秤量・投入(白ﾎﾟﾘ)]から、ﾃﾞｰﾀを取得
            Map srSlipYouzaihyouryouTounyuuSiroporiData = loadSrSlipYouzaihyouryouTounyuuSiropori(queryRunnerQcdb, kojyo, lotNo9, edaban, rev);
            BigDecimal slipJyuuryou = BigDecimal.ZERO;
            if (srSlipSlurrykokeibuntyouseiSiroporiData == null || srSlipSlurrykokeibuntyouseiSiroporiData.isEmpty()
                    || srSlipBinderhyouryouTounyuuData == null || srSlipBinderhyouryouTounyuuData.isEmpty()
                    || srSlipYouzaihyouryouTounyuuSiroporiData == null || srSlipYouzaihyouryouTounyuuSiroporiData.isEmpty()) {
                // (19)[ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)]から、ﾃﾞｰﾀを取得
                Map srSlipYouzaihyouryouTounyuuSutenyoukiData = loadSrSlipYouzaihyouryouTounyuuSutenyouki(queryRunnerQcdb, kojyo, lotNo9, edaban, rev);
                // (22)[ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)]から、ﾃﾞｰﾀを取得
                Map srSlipSlurrykokeibuntyouseiSutenyoukiData = loadSrSlipSlurrykokeibuntyouseiSutenyouki(queryRunnerQcdb, kojyo, lotNo9, edaban, rev);
                if (srSlipYouzaihyouryouTounyuuSutenyoukiData == null || srSlipYouzaihyouryouTounyuuSutenyoukiData.isEmpty()
                        || srSlipBinderhyouryouTounyuuData == null || srSlipBinderhyouryouTounyuuData.isEmpty()
                        || srSlipSlurrykokeibuntyouseiSutenyoukiData == null || srSlipSlurrykokeibuntyouseiSutenyoukiData.isEmpty()) {
                    // ｴﾗｰ項目をﾘｽﾄに追加
                    ErrorMessageInfo checkItemErrorInfo = MessageUtil.getErrorMessageInfo("XHD-000210", true, true, null, "ﾃﾞｰﾀ");
                    if (checkItemErrorInfo != null) {
                        processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
                        return processData;
                    }
                }
                slipJyuuryou = calcSutenyoukiSlipJyuuryou(srSlipYouzaihyouryouTounyuuSutenyoukiData, srSlipBinderhyouryouTounyuuData, srSlipSlurrykokeibuntyouseiSutenyoukiData);
            } else {
                slipJyuuryou = calcSiroporiSlipJyuuryou(srSlipSlurrykokeibuntyouseiSiroporiData, srSlipYouzaihyouryouTounyuuSiroporiData, srSlipBinderhyouryouTounyuuData);
            }
            HashMap<String, String> resultMap = new HashMap<>();
            // ②「固形分目標値」の取得
            ErrorMessageInfo errormessageinfo = getKoukeibunnmokuhyochi(queryRunnerDoc, queryRunnerQcdb, kojyo, lotNo9, edaban, tantoshaCd, resultMap);
            if (errormessageinfo != null) {
                processData.setErrorMessageInfoList(Arrays.asList(errormessageinfo));
                return processData;
            }
            //「固形分目標値」
            BigDecimal koukeibunnmokuhyochi = new BigDecimal((String) resultMap.get("kikakuti"));

            BigDecimal kokeibunhirituBgValue = new BigDecimal(kokeibunhiritu.getValue());
            // 「溶剤調整量」:「ｽﾘｯﾌﾟ重量」 × 「固形分比率」 × 「固形分目標値」 - 「ｽﾘｯﾌﾟ重量」 を算出する。
            BigDecimal youzaityouseiryouBgValue = slipJyuuryou.multiply(kokeibunhirituBgValue).multiply(koukeibunnmokuhyochi).subtract(slipJyuuryou).setScale(2, RoundingMode.HALF_DOWN);
            // 溶剤調整量に計算結果を設定
            youzaityouseiryou.setValue(youzaityouseiryouBgValue.setScale(0, RoundingMode.HALF_UP).toPlainString());
            // ﾄﾙｴﾝ調整量
            FXHDD01 toluenetyouseiryou = getItemRow(processData.getItemList(), GXHDO102B033Const.TOLUENETYOUSEIRYOU);
            // ｿﾙﾐｯｸｽ調整量
            FXHDD01 solmixtyouseiryou = getItemRow(processData.getItemList(), GXHDO102B033Const.SOLMIXTYOUSEIRYOU);
            // 溶剤調整量 ÷ 2
            BigDecimal youzaityouseiryou2Val = youzaityouseiryouBgValue.divide(new BigDecimal(2), 0, RoundingMode.HALF_UP);
            // ﾄﾙｴﾝ調整量
            if (toluenetyouseiryou != null) {
                //計算結果の設定
                toluenetyouseiryou.setValue(youzaityouseiryou2Val.toPlainString());
            }
            // ｿﾙﾐｯｸｽ調整量
            if (solmixtyouseiryou != null) {
                //計算結果の設定
                solmixtyouseiryou.setValue(youzaityouseiryou2Val.toPlainString());
            }
            processData.setMethod("");

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog(youzaityouseiryou.getLabel1() + "計算にエラー発生", ex, LOGGER);
        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
            ErrUtil.outputErrorLog(youzaityouseiryou.getLabel1() + "計算に" + ex.getClass().getSimpleName() + "エラー発生", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            return processData;
        }
        return processData;
    }

    /**
     * 背景色をデフォルトの背景色に戻す
     *
     * @param buttonId ボタンID
     */
    private void clearItemListBackColor(ProcessData processData) {
        processData.getItemList().forEach((fxhdd01) -> {
            fxhdd01.setBackColor3(fxhdd01.getBackColorInputDefault());
        });
    }

    /**
     * 「ｽﾘｯﾌﾟ重量」計算処理:ｽﾃﾝ容器の計算
     *
     * @param srSlipYouzaihyouryouTounyuuSutenyoukiData
     * (19)[ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)]から取得されるﾃﾞｰﾀ
     * @param srSlipBinderhyouryouTounyuuData
     * (21)[ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入]から取得されるﾃﾞｰﾀ
     * @param srSlipSlurrykokeibuntyouseiSutenyoukiData
     * (22)[ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)]から取得されるﾃﾞｰﾀ
     */
    private BigDecimal calcSutenyoukiSlipJyuuryou(Map srSlipYouzaihyouryouTounyuuSutenyoukiData, Map srSlipBinderhyouryouTounyuuData, Map srSlipSlurrykokeibuntyouseiSutenyoukiData) {
        // 誘電体ｽﾗﾘｰ_調合量1
        BigDecimal yuudentaislurry_tyougouryou1 = new BigDecimal(String.valueOf(srSlipYouzaihyouryouTounyuuSutenyoukiData.get("yuudentaislurry_tyougouryou1")));
        // 誘電体ｽﾗﾘｰ_調合量2
        BigDecimal yuudentaislurry_tyougouryou2 = new BigDecimal(String.valueOf(srSlipYouzaihyouryouTounyuuSutenyoukiData.get("yuudentaislurry_tyougouryou2")));
        // 分散材①_調合量1
        BigDecimal zunsanzai1_tyougouryou1 = new BigDecimal(String.valueOf(srSlipYouzaihyouryouTounyuuSutenyoukiData.get("zunsanzai1_tyougouryou1")));
        // 分散材①_調合量2
        BigDecimal zunsanzai1_tyougouryou2 = new BigDecimal(String.valueOf(srSlipYouzaihyouryouTounyuuSutenyoukiData.get("zunsanzai1_tyougouryou2")));
        // 分散材②_調合量1
        BigDecimal zunsanzai2_tyougouryou1 = new BigDecimal(String.valueOf(srSlipYouzaihyouryouTounyuuSutenyoukiData.get("zunsanzai2_tyougouryou1")));
        // 分散材②_調合量2
        BigDecimal zunsanzai2_tyougouryou2 = new BigDecimal(String.valueOf(srSlipYouzaihyouryouTounyuuSutenyoukiData.get("zunsanzai2_tyougouryou2")));
        // 溶剤①_調合量1
        BigDecimal youzai1_tyougouryou1 = new BigDecimal(String.valueOf(srSlipYouzaihyouryouTounyuuSutenyoukiData.get("youzai1_tyougouryou1")));
        // 溶剤①_調合量2
        BigDecimal youzai1_tyougouryou2 = new BigDecimal(String.valueOf(srSlipYouzaihyouryouTounyuuSutenyoukiData.get("youzai1_tyougouryou2")));
        // 溶剤②_調合量1
        BigDecimal youzai2_tyougouryou1 = new BigDecimal(String.valueOf(srSlipYouzaihyouryouTounyuuSutenyoukiData.get("youzai2_tyougouryou1")));
        // 溶剤②_調合量2
        BigDecimal youzai2_tyougouryou2 = new BigDecimal(String.valueOf(srSlipYouzaihyouryouTounyuuSutenyoukiData.get("youzai2_tyougouryou2")));

        // ﾊﾞｲﾝﾀﾞｰ添加量合計
        BigDecimal bindertenkaryougoukei = new BigDecimal(String.valueOf(srSlipBinderhyouryouTounyuuData.get("bindertenkaryougoukei")));

        // 溶剤調整量
        BigDecimal youzaityouseiryou = new BigDecimal(String.valueOf(srSlipSlurrykokeibuntyouseiSutenyoukiData.get("youzaityouseiryou")));
        // 溶剤②_調合量1
        BigDecimal youzai2_tyougouryou1_22 = new BigDecimal(String.valueOf(srSlipSlurrykokeibuntyouseiSutenyoukiData.get("youzai2_tyougouryou1")));
        // 溶剤②_調合量2
        BigDecimal youzai2_tyougouryou2_22 = new BigDecimal(String.valueOf(srSlipSlurrykokeibuntyouseiSutenyoukiData.get("youzai2_tyougouryou2")));
        // 2回目溶剤①_調合量1
        BigDecimal nikaimeyouzai1_tyougouryou1 = new BigDecimal(String.valueOf(srSlipSlurrykokeibuntyouseiSutenyoukiData.get("nikaimeyouzai1_tyougouryou1")));
        // 2回目溶剤①_調合量2
        BigDecimal nikaimeyouzai1_tyougouryou2 = new BigDecimal(String.valueOf(srSlipSlurrykokeibuntyouseiSutenyoukiData.get("nikaimeyouzai1_tyougouryou2")));
        // 2回目溶剤②_調合量1
        BigDecimal nikaimeyouzai2_tyougouryou1 = new BigDecimal(String.valueOf(srSlipSlurrykokeibuntyouseiSutenyoukiData.get("nikaimeyouzai2_tyougouryou1")));
        // 2回目溶剤②_調合量2
        BigDecimal nikaimeyouzai2_tyougouryou2 = new BigDecimal(String.valueOf(srSlipSlurrykokeibuntyouseiSutenyoukiData.get("nikaimeyouzai2_tyougouryou2")));
        return yuudentaislurry_tyougouryou1.add(yuudentaislurry_tyougouryou2).add(zunsanzai1_tyougouryou1).add(zunsanzai1_tyougouryou2).add(zunsanzai2_tyougouryou1).
                add(zunsanzai2_tyougouryou2).add(youzai1_tyougouryou1).add(youzai1_tyougouryou2).add(youzai2_tyougouryou1).add(youzai2_tyougouryou2).
                add(bindertenkaryougoukei).add(youzaityouseiryou).add(youzai2_tyougouryou1_22).add(youzai2_tyougouryou2_22).add(nikaimeyouzai1_tyougouryou1).
                add(nikaimeyouzai1_tyougouryou2).add(nikaimeyouzai2_tyougouryou1).add(nikaimeyouzai2_tyougouryou2);
    }

    /**
     * 「ｽﾘｯﾌﾟ重量」計算処理:白ﾎﾟﾘの計算
     *
     * @param srSlipSlurrykokeibuntyouseiSiroporiData
     * (20)[ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)]から取得されるﾃﾞｰﾀ
     * @param srSlipYouzaihyouryouTounyuuSiroporiData
     * (23)[ｽﾘｯﾌﾟ作製・溶剤秤量・投入(白ﾎﾟﾘ)]から取得されるﾃﾞｰﾀ
     * @param srSlipBinderhyouryouTounyuuData
     * (21)[ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入]から取得されるﾃﾞｰﾀ
     */
    private BigDecimal calcSiroporiSlipJyuuryou(Map srSlipSlurrykokeibuntyouseiSiroporiData, Map srSlipYouzaihyouryouTounyuuSiroporiData, Map srSlipBinderhyouryouTounyuuData) {
        // ｽﾗﾘｰ合計重量
        BigDecimal slurrygoukeijyuuryou = new BigDecimal(String.valueOf(srSlipSlurrykokeibuntyouseiSiroporiData.get("slurrygoukeijyuuryou")));

        // 溶剤①_調合量1
        BigDecimal youzai1_tyougouryou1 = new BigDecimal(String.valueOf(srSlipYouzaihyouryouTounyuuSiroporiData.get("youzai1_tyougouryou1")));
        // 溶剤①_調合量2
        BigDecimal youzai1_tyougouryou2 = new BigDecimal(String.valueOf(srSlipYouzaihyouryouTounyuuSiroporiData.get("youzai1_tyougouryou2")));
        // 溶剤②_調合量1
        BigDecimal youzai2_tyougouryou1 = new BigDecimal(String.valueOf(srSlipYouzaihyouryouTounyuuSiroporiData.get("youzai2_tyougouryou1")));
        // 溶剤②_調合量2
        BigDecimal youzai2_tyougouryou2 = new BigDecimal(String.valueOf(srSlipYouzaihyouryouTounyuuSiroporiData.get("youzai2_tyougouryou2")));
        // 溶剤③_調合量1
        BigDecimal youzai3_tyougouryou1 = new BigDecimal(String.valueOf(srSlipYouzaihyouryouTounyuuSiroporiData.get("youzai3_tyougouryou1")));
        // 溶剤③_調合量2
        BigDecimal youzai3_tyougouryou2 = new BigDecimal(String.valueOf(srSlipYouzaihyouryouTounyuuSiroporiData.get("youzai3_tyougouryou2")));
        // 溶剤④_調合量1
        BigDecimal youzai4_tyougouryou1 = new BigDecimal(String.valueOf(srSlipYouzaihyouryouTounyuuSiroporiData.get("youzai4_tyougouryou1")));
        // 溶剤④_調合量2
        BigDecimal youzai4_tyougouryou2 = new BigDecimal(String.valueOf(srSlipYouzaihyouryouTounyuuSiroporiData.get("youzai4_tyougouryou2")));

        // ﾊﾞｲﾝﾀﾞｰ添加量合計
        BigDecimal bindertenkaryougoukei = new BigDecimal(String.valueOf(srSlipBinderhyouryouTounyuuData.get("bindertenkaryougoukei")));

        return slurrygoukeijyuuryou.add(youzai1_tyougouryou1).add(youzai1_tyougouryou2).add(youzai2_tyougouryou1).add(youzai2_tyougouryou2).
                add(youzai3_tyougouryou1).add(youzai3_tyougouryou2).add(youzai4_tyougouryou1).add(youzai4_tyougouryou2).add(bindertenkaryougoukei);
    }

    /**
     * 溶剤秤量日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setYouzaikeiryouDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B033Const.YOUZAIKEIRYOU_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B033Const.YOUZAIKEIRYOU_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 【撹拌_撹拌開始日時】ﾎﾞﾀﾝ押下時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKakuhankaisiDateTime(ProcessData processData) {
        clearItemListBackColor(processData);
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B033Const.KAKUHAN_KAKUHANKAISI_DAY); // 撹拌_撹拌開始日
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B033Const.KAKUHAN_KAKUHANKAISI_TIME); // 撹拌_撹拌開始時間

        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            FXHDD01 itemKakuhanKakuhanjikan = getItemRow(processData.getItemList(), GXHDO102B033Const.KAKUHAN_KAKUHANJIKAN); // 撹拌_撹拌時間
            FXHDD01 itemYoteiDay = getItemRow(processData.getItemList(), GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOUYOTEI_DAY); // 撹拌_撹拌終了予定日
            FXHDD01 itemYoteiTime = getItemRow(processData.getItemList(), GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOUYOTEI_TIME); // 撹拌_撹拌終了予定時間
            //【撹拌_撹拌開始日時】ﾎﾞﾀﾝ押下時計算ﾁｪｯｸ処理
            ErrorMessageInfo checkItemErrorInfo = checkKakuhankaisi(itemKakuhanKakuhanjikan);
            if (checkItemErrorInfo != null) {
                processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
                return processData;
            }
            // 開始日時の設定
            setDateTimeItem(itemDay, itemTime, new Date());
            // 撹拌_撹拌終了予定時間の設定
            setYoteiDateTimeItem(itemDay, itemTime, itemYoteiDay, itemYoteiTime, itemKakuhanKakuhanjikan);
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 【撹拌_撹拌開始日時】ﾎﾞﾀﾝ押下時計算ﾁｪｯｸ処理
     *
     * @param itemKakuhanKakuhanjikan 撹拌_撹拌時間
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkKakuhankaisi(FXHDD01 itemKakuhanKakuhanjikan) {
        // 「撹拌_撹拌時間」ﾁｪｯｸ
        if (itemKakuhanKakuhanjikan != null) {
            BigDecimal itemKakuhanjikanBdVal = ValidateUtil.getItemKikakuChiCheckVal(itemKakuhanKakuhanjikan); // 撹拌時間の規格値
            if (itemKakuhanjikanBdVal == null) {
                itemKakuhanKakuhanjikan.setBackColor3(ErrUtil.ERR_BACK_COLOR);
                // ｴﾗｰ項目をﾘｽﾄに追加
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemKakuhanKakuhanjikan);
                return MessageUtil.getErrorMessageInfo("XHD-000037", true, true, errFxhdd01List, itemKakuhanKakuhanjikan.getLabel1());
            }
        }

        return null;
    }

    /**
     * 終了予定日付(日、時間)の項目にフォーマットの日付(yyMMdd,HHmm)をセットする
     *
     * @param processData 処理制御データ
     * @param itemDay 項目日付(日)
     * @param itemTime 項目日付(時間)
     * @param itemYoteiDay 予定日
     * @param itemYoteiTime 予定時間
     * @param itemKakuhanjikan 撹拌時間
     */
    private void setYoteiDateTimeItem(FXHDD01 itemDay, FXHDD01 itemTime, FXHDD01 itemYoteiDay, FXHDD01 itemYoteiTime, FXHDD01 itemKakuhanjikan) {
        try {
            if (itemYoteiDay == null || itemYoteiTime == null || itemKakuhanjikan == null) {
                return;
            }
            BigDecimal itemKakuhanjikanBdVal = ValidateUtil.getItemKikakuChiCheckVal(itemKakuhanjikan); // 撹拌時間の規格値
            if (itemKakuhanjikanBdVal == null) {
                return;
            }
            int itemKakuhanjikanIntVal = itemKakuhanjikanBdVal.intValue();
            Date dateTime = DateUtil.addJikan(itemDay.getValue(), itemTime.getValue(), itemKakuhanjikanIntVal, Calendar.MINUTE);
            if (dateTime != null) {
                setDateTimeItem(itemYoteiDay, itemYoteiTime, dateTime);
            }
        } catch (NumberFormatException ex) {
            ErrUtil.outputErrorLog("NumberFormatException発生", ex, LOGGER);
        }
    }

    /**
     * 撹拌_撹拌終了日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKakuhansyuuryouDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOU_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 【排出前撹拌_撹拌開始日時】ﾎﾞﾀﾝ押下時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setHmkkkkhkaisiDateTime(ProcessData processData) {
        clearItemListBackColor(processData);
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANKAISI_DAY); // 排出前撹拌_撹拌開始日
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANKAISI_TIME); // 排出前撹拌_撹拌開始時間

        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            FXHDD01 itemKakuhanjikan = getItemRow(processData.getItemList(), GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANJIKAN); // 排出前撹拌_撹拌時間
            FXHDD01 itemYoteiDay = getItemRow(processData.getItemList(), GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOUYOTEI_DAY); // 排出前撹拌_撹拌終了予定日
            FXHDD01 itemYoteiTime = getItemRow(processData.getItemList(), GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOUYOTEI_TIME); // 排出前撹拌_撹拌終了予定時間
            //【撹拌_撹拌開始日時】ﾎﾞﾀﾝ押下時計算ﾁｪｯｸ処理
            ErrorMessageInfo checkItemErrorInfo = checkKakuhankaisi(itemKakuhanjikan);
            if (checkItemErrorInfo != null) {
                processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
                return processData;
            }
            // 開始日時の設定
            setDateTimeItem(itemDay, itemTime, new Date());
            // 撹拌_撹拌終了予定時間の設定
            setYoteiDateTimeItem(itemDay, itemTime, itemYoteiDay, itemYoteiTime, itemKakuhanjikan);
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 排出前撹拌_撹拌開始日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setHmkkkkhsyuuryouDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOU_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 排出開始日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setHaisyutukaisiDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B033Const.HAISYUTUKAISI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B033Const.HAISYUTUKAISI_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 排出終了日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setHaisyutusyuuryouDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B033Const.HAISYUTUSYUURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B033Const.HAISYUTUSYUURYOU_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
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
                        GXHDO102B033Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B033Const.BTN_KANSOUKAISI1_TOP,
                        GXHDO102B033Const.BTN_KANSOUSYUURYOU1_TOP,
                        GXHDO102B033Const.BTN_KANSOUKAISI2_TOP,
                        GXHDO102B033Const.BTN_KANSOUSYUURYOU2_TOP,
                        GXHDO102B033Const.BTN_KANSOUGOSYOUMIJYUURYOU_TOP,
                        GXHDO102B033Const.BTN_YOUZAITYOUSEIRYOU_TOP,
                        GXHDO102B033Const.BTN_YOUZAIKEIRYOU_TOP,
                        GXHDO102B033Const.BTN_KAKUHANKAISI_TOP,
                        GXHDO102B033Const.BTN_KAKUHANSYUURYOU_TOP,
                        GXHDO102B033Const.BTN_HMKK_KKHKAISI_TOP,
                        GXHDO102B033Const.BTN_HMKK_KKHSYUURYOU_TOP,
                        GXHDO102B033Const.BTN_HAISYUTUKAISI_TOP,
                        GXHDO102B033Const.BTN_HAISYUTUSYUURYOU_TOP,
                        GXHDO102B033Const.BTN_UPDATE_TOP,
                        GXHDO102B033Const.BTN_DELETE_TOP,
                        GXHDO102B033Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B033Const.BTN_KANSOUKAISI1_BOTTOM,
                        GXHDO102B033Const.BTN_KANSOUSYUURYOU1_BOTTOM,
                        GXHDO102B033Const.BTN_KANSOUKAISI2_BOTTOM,
                        GXHDO102B033Const.BTN_KANSOUSYUURYOU2_BOTTOM,
                        GXHDO102B033Const.BTN_KANSOUGOSYOUMIJYUURYOU_BOTTOM,
                        GXHDO102B033Const.BTN_YOUZAITYOUSEIRYOU_BOTTOM,
                        GXHDO102B033Const.BTN_YOUZAIKEIRYOU_BOTTOM,
                        GXHDO102B033Const.BTN_KAKUHANKAISI_BOTTOM,
                        GXHDO102B033Const.BTN_KAKUHANSYUURYOU_BOTTOM,
                        GXHDO102B033Const.BTN_HMKK_KKHKAISI_BOTTOM,
                        GXHDO102B033Const.BTN_HMKK_KKHSYUURYOU_BOTTOM,
                        GXHDO102B033Const.BTN_HAISYUTUKAISI_BOTTOM,
                        GXHDO102B033Const.BTN_HAISYUTUSYUURYOU_BOTTOM,
                        GXHDO102B033Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B033Const.BTN_DELETE_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B033Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B033Const.BTN_INSERT_TOP,
                        GXHDO102B033Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B033Const.BTN_INSERT_BOTTOM));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO102B033Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B033Const.BTN_KANSOUKAISI1_TOP,
                        GXHDO102B033Const.BTN_KANSOUSYUURYOU1_TOP,
                        GXHDO102B033Const.BTN_KANSOUKAISI2_TOP,
                        GXHDO102B033Const.BTN_KANSOUSYUURYOU2_TOP,
                        GXHDO102B033Const.BTN_KANSOUGOSYOUMIJYUURYOU_TOP,
                        GXHDO102B033Const.BTN_YOUZAITYOUSEIRYOU_TOP,
                        GXHDO102B033Const.BTN_YOUZAIKEIRYOU_TOP,
                        GXHDO102B033Const.BTN_KAKUHANKAISI_TOP,
                        GXHDO102B033Const.BTN_KAKUHANSYUURYOU_TOP,
                        GXHDO102B033Const.BTN_HMKK_KKHKAISI_TOP,
                        GXHDO102B033Const.BTN_HMKK_KKHSYUURYOU_TOP,
                        GXHDO102B033Const.BTN_HAISYUTUKAISI_TOP,
                        GXHDO102B033Const.BTN_HAISYUTUSYUURYOU_TOP,
                        GXHDO102B033Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B033Const.BTN_INSERT_TOP,
                        GXHDO102B033Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B033Const.BTN_KANSOUKAISI1_BOTTOM,
                        GXHDO102B033Const.BTN_KANSOUSYUURYOU1_BOTTOM,
                        GXHDO102B033Const.BTN_KANSOUKAISI2_BOTTOM,
                        GXHDO102B033Const.BTN_KANSOUSYUURYOU2_BOTTOM,
                        GXHDO102B033Const.BTN_KANSOUGOSYOUMIJYUURYOU_BOTTOM,
                        GXHDO102B033Const.BTN_YOUZAITYOUSEIRYOU_BOTTOM,
                        GXHDO102B033Const.BTN_YOUZAIKEIRYOU_BOTTOM,
                        GXHDO102B033Const.BTN_KAKUHANKAISI_BOTTOM,
                        GXHDO102B033Const.BTN_KAKUHANSYUURYOU_BOTTOM,
                        GXHDO102B033Const.BTN_HMKK_KKHKAISI_BOTTOM,
                        GXHDO102B033Const.BTN_HMKK_KKHSYUURYOU_BOTTOM,
                        GXHDO102B033Const.BTN_HAISYUTUKAISI_BOTTOM,
                        GXHDO102B033Const.BTN_HAISYUTUSYUURYOU_BOTTOM,
                        GXHDO102B033Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B033Const.BTN_INSERT_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B033Const.BTN_UPDATE_TOP,
                        GXHDO102B033Const.BTN_DELETE_TOP,
                        GXHDO102B033Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B033Const.BTN_DELETE_BOTTOM
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
        // 項目の表示制御
        setItemRendered(processData, queryRunnerDoc, queryRunnerQcdb, shikakariData, lotNo);
        // 画面に取得した情報をセットする。(入力項目以外)
        setViewItemData(processData, shikakariData, lotNo);
        // 画面のラベル項目の値の背景色を取得できない場合、デフォルト値を設置
        GXHDO102C016Logic.setItemStyle(processData.getItemList());
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
     * @param sekkeiData 設計データ
     * @param ownerMasData ｵｰﾅｰﾏｽﾀデータ
     * @param shikakariData 仕掛データ
     * @param lotNo ﾛｯﾄNo
     */
    private void setViewItemData(ProcessData processData, Map shikakariData, String lotNo) {

        // WIPﾛｯﾄNo
        this.setItemData(processData, GXHDO102B033Const.WIPLOTNO, lotNo);
        // ｽﾘｯﾌﾟ品名
        this.setItemData(processData, GXHDO102B033Const.SLIPHINMEI, StringUtil.nullToBlank(getMapData(shikakariData, "hinmei")));
        // ｽﾘｯﾌﾟLotNo
        this.setItemData(processData, GXHDO102B033Const.SLIPLOTNO, StringUtil.nullToBlank(getMapData(shikakariData, "lotno")));
        // ﾛｯﾄ区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode"));
        // ﾛｯﾄ区分名称
        String lotkubun = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubun"));

        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO102B033Const.LOTKUBUN, "");
        } else {
            if (!StringUtil.isEmpty(lotkubun)) {
                lotkubuncode = lotkubuncode + ":" + lotkubun;
            }
            this.setItemData(processData, GXHDO102B033Const.LOTKUBUN, lotkubuncode);
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

        List<SrSlipSlipkokeibunsokutei> srSlipSlipkokeibunsokuteiList = new ArrayList<>();
        List<SubSrSlipSlipkokeibunsokutei> subSrSlipSlipkokeibunsokuteiList = new ArrayList<>();
        String rev = "";
        String jotaiFlg = "";
        String kojyo = lotNo.substring(0, 3);
        String lotNo9 = lotNo.substring(3, 12);
        String edaban = lotNo.substring(12, 15);

        for (int i = 0; i < 5; i++) {
            // [原材料品質DB登録実績]から、ﾃﾞｰﾀを取得
            Map fxhdd11RevInfo = loadFxhdd11RevInfo(queryRunnerDoc, kojyo, lotNo9, edaban, jissekino, formId);
            rev = StringUtil.nullToBlank(getMapData(fxhdd11RevInfo, "rev"));
            jotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd11RevInfo, "jotai_flg"));

            // revisionが空のまたはjotaiFlgが"0"でも"1"でもない場合、新規としてデフォルト値を設定してリターンする。
            if (StringUtil.isEmpty(rev) || !(JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg) || JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg))) {
                processData.setInitRev(rev);
                processData.setInitJotaiFlg(jotaiFlg);

                // 画面にデータを設定する(デフォルト値)
                processData.getItemList().forEach((fxhdd001) -> {
                    this.setItemData(processData, fxhdd001.getItemId(), fxhdd001.getInputDefault());
                });

                // ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定入力_ｻﾌﾞ画面データ設定
                setInputItemDataSubFormC016(processData, null);
                return true;
            }

            // ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定データ取得
            srSlipSlipkokeibunsokuteiList = getSrSlipSlipkokeibunsokuteiData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo9, edaban);
            if (srSlipSlipkokeibunsokuteiList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定入力_サブ画面データ取得
            subSrSlipSlipkokeibunsokuteiList = getSubSrSlipSlipkokeibunsokuteiData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo9, edaban);
            if (subSrSlipSlipkokeibunsokuteiList.isEmpty() || subSrSlipSlipkokeibunsokuteiList.size() != 2) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }
            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srSlipSlipkokeibunsokuteiList.isEmpty() || (subSrSlipSlipkokeibunsokuteiList.isEmpty() || subSrSlipSlipkokeibunsokuteiList.size() != 2)) {
            return false;
        }
        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srSlipSlipkokeibunsokuteiList.get(0));
        // ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定入力_ｻﾌﾞ画面データ設定
        setInputItemDataSubFormC016(processData, subSrSlipSlipkokeibunsokuteiList);
        return true;

    }

    /**
     * データ設定処理
     *
     * @param processData 処理制御データ
     * @param srSlipSlipkokeibunsokutei ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定
     */
    private void setInputItemDataMainForm(ProcessData processData, SrSlipSlipkokeibunsokutei srSlipSlipkokeibunsokutei) {
        // 秤量号機
        this.setItemData(processData, GXHDO102B033Const.GOKI, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.GOKI, srSlipSlipkokeibunsokutei));

        // ﾙﾂﾎﾞNo
        this.setItemData(processData, GXHDO102B033Const.RUTUBONO, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.RUTUBONO, srSlipSlipkokeibunsokutei));

        // ﾙﾂﾎﾞ風袋重量
        this.setItemData(processData, GXHDO102B033Const.RUTUBOFUUTAIJYUURYOU, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.RUTUBOFUUTAIJYUURYOU, srSlipSlipkokeibunsokutei));

        // 乾燥前ｽﾘｯﾌﾟ重量
        this.setItemData(processData, GXHDO102B033Const.KANSOUMAESLIPJYUURYOU, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.KANSOUMAESLIPJYUURYOU, srSlipSlipkokeibunsokutei));

        // 乾燥開始日①
        this.setItemData(processData, GXHDO102B033Const.KANSOUKAISI1_DAY, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.KANSOUKAISI1_DAY, srSlipSlipkokeibunsokutei));

        // 乾燥開始時間①
        this.setItemData(processData, GXHDO102B033Const.KANSOUKAISI1_TIME, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.KANSOUKAISI1_TIME, srSlipSlipkokeibunsokutei));

        // 乾燥終了日①
        this.setItemData(processData, GXHDO102B033Const.KANSOUSYUURYOU1_DAY, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.KANSOUSYUURYOU1_DAY, srSlipSlipkokeibunsokutei));

        // 乾燥終了時間①
        this.setItemData(processData, GXHDO102B033Const.KANSOUSYUURYOU1_TIME, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.KANSOUSYUURYOU1_TIME, srSlipSlipkokeibunsokutei));

        // 乾燥開始日②
        this.setItemData(processData, GXHDO102B033Const.KANSOUKAISI2_DAY, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.KANSOUKAISI2_DAY, srSlipSlipkokeibunsokutei));

        // 乾燥開始時間②
        this.setItemData(processData, GXHDO102B033Const.KANSOUKAISI2_TIME, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.KANSOUKAISI2_TIME, srSlipSlipkokeibunsokutei));

        // 乾燥終了日②
        this.setItemData(processData, GXHDO102B033Const.KANSOUSYUURYOU2_DAY, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.KANSOUSYUURYOU2_DAY, srSlipSlipkokeibunsokutei));

        // 乾燥終了時間②
        this.setItemData(processData, GXHDO102B033Const.KANSOUSYUURYOU2_TIME, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.KANSOUSYUURYOU2_TIME, srSlipSlipkokeibunsokutei));

        // 乾燥後総重量
        this.setItemData(processData, GXHDO102B033Const.KANSOUGOSOUJYUURYOU, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.KANSOUGOSOUJYUURYOU, srSlipSlipkokeibunsokutei));

        // 乾燥後正味重量
        this.setItemData(processData, GXHDO102B033Const.KANSOUGOSYOUMIJYUURYOU, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.KANSOUGOSYOUMIJYUURYOU, srSlipSlipkokeibunsokutei));

        // 固形分比率
        this.setItemData(processData, GXHDO102B033Const.KOKEIBUNHIRITU, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.KOKEIBUNHIRITU, srSlipSlipkokeibunsokutei));

        // 固形分測定担当者
        this.setItemData(processData, GXHDO102B033Const.KOKEIBUNSOKUTEITANTOUSYA, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.KOKEIBUNSOKUTEITANTOUSYA, srSlipSlipkokeibunsokutei));

        // 溶剤調整量
        this.setItemData(processData, GXHDO102B033Const.YOUZAITYOUSEIRYOU, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.YOUZAITYOUSEIRYOU, srSlipSlipkokeibunsokutei));

        // ﾄﾙｴﾝ調整量
        this.setItemData(processData, GXHDO102B033Const.TOLUENETYOUSEIRYOU, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.TOLUENETYOUSEIRYOU, srSlipSlipkokeibunsokutei));

        // ｿﾙﾐｯｸｽ調整量
        this.setItemData(processData, GXHDO102B033Const.SOLMIXTYOUSEIRYOU, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.SOLMIXTYOUSEIRYOU, srSlipSlipkokeibunsokutei));

        // 溶剤秤量日
        this.setItemData(processData, GXHDO102B033Const.YOUZAIKEIRYOU_DAY, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.YOUZAIKEIRYOU_DAY, srSlipSlipkokeibunsokutei));

        // 溶剤秤量時間
        this.setItemData(processData, GXHDO102B033Const.YOUZAIKEIRYOU_TIME, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.YOUZAIKEIRYOU_TIME, srSlipSlipkokeibunsokutei));

        // 溶剤①_部材在庫No1
        this.setItemData(processData, GXHDO102B033Const.YOUZAI1_BUZAIZAIKOLOTNO1, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.YOUZAI1_BUZAIZAIKOLOTNO1, srSlipSlipkokeibunsokutei));

        // 溶剤①_調合量1
        this.setItemData(processData, GXHDO102B033Const.YOUZAI1_TYOUGOURYOU1, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.YOUZAI1_TYOUGOURYOU1, srSlipSlipkokeibunsokutei));

        // 溶剤①_部材在庫No2
        this.setItemData(processData, GXHDO102B033Const.YOUZAI1_BUZAIZAIKOLOTNO2, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.YOUZAI1_BUZAIZAIKOLOTNO2, srSlipSlipkokeibunsokutei));

        // 溶剤①_調合量2
        this.setItemData(processData, GXHDO102B033Const.YOUZAI1_TYOUGOURYOU2, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.YOUZAI1_TYOUGOURYOU2, srSlipSlipkokeibunsokutei));

        // 溶剤②_部材在庫No1
        this.setItemData(processData, GXHDO102B033Const.YOUZAI2_BUZAIZAIKOLOTNO1, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.YOUZAI2_BUZAIZAIKOLOTNO1, srSlipSlipkokeibunsokutei));

        // 溶剤②_調合量1
        this.setItemData(processData, GXHDO102B033Const.YOUZAI2_TYOUGOURYOU1, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.YOUZAI2_TYOUGOURYOU1, srSlipSlipkokeibunsokutei));

        // 溶剤②_部材在庫No2
        this.setItemData(processData, GXHDO102B033Const.YOUZAI2_BUZAIZAIKOLOTNO2, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.YOUZAI2_BUZAIZAIKOLOTNO2, srSlipSlipkokeibunsokutei));

        // 溶剤②_調合量2
        this.setItemData(processData, GXHDO102B033Const.YOUZAI2_TYOUGOURYOU2, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.YOUZAI2_TYOUGOURYOU2, srSlipSlipkokeibunsokutei));

        // 担当者
        this.setItemData(processData, GXHDO102B033Const.TANTOUSYA, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.TANTOUSYA, srSlipSlipkokeibunsokutei));

        // 撹拌_撹拌開始日
        this.setItemData(processData, GXHDO102B033Const.KAKUHAN_KAKUHANKAISI_DAY, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.KAKUHAN_KAKUHANKAISI_DAY, srSlipSlipkokeibunsokutei));

        // 撹拌_撹拌開始時間
        this.setItemData(processData, GXHDO102B033Const.KAKUHAN_KAKUHANKAISI_TIME, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.KAKUHAN_KAKUHANKAISI_TIME, srSlipSlipkokeibunsokutei));

        // 開始電流値(A)
        this.setItemData(processData, GXHDO102B033Const.KAISIDENRYUUTI, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.KAISIDENRYUUTI, srSlipSlipkokeibunsokutei));

        // 撹拌_撹拌終了予定日
        this.setItemData(processData, GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOUYOTEI_DAY, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOUYOTEI_DAY, srSlipSlipkokeibunsokutei));

        // 撹拌_撹拌終了予定時間
        this.setItemData(processData, GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOUYOTEI_TIME, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOUYOTEI_TIME, srSlipSlipkokeibunsokutei));

        // 撹拌_撹拌終了日
        this.setItemData(processData, GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOU_DAY, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOU_DAY, srSlipSlipkokeibunsokutei));

        // 撹拌_撹拌終了時間
        this.setItemData(processData, GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOU_TIME, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOU_TIME, srSlipSlipkokeibunsokutei));

        // 終了電流値(A)
        this.setItemData(processData, GXHDO102B033Const.SYUURYOUDENRYUUTI, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.SYUURYOUDENRYUUTI, srSlipSlipkokeibunsokutei));

        // 撹拌_撹拌担当者
        this.setItemData(processData, GXHDO102B033Const.KAKUHAN_KAKUHANTANTOUSYA, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.KAKUHAN_KAKUHANTANTOUSYA, srSlipSlipkokeibunsokutei));

        // 排出前撹拌_撹拌開始日
        this.setItemData(processData, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANKAISI_DAY, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANKAISI_DAY, srSlipSlipkokeibunsokutei));

        // 排出前撹拌_撹拌開始時間
        this.setItemData(processData, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANKAISI_TIME, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANKAISI_TIME, srSlipSlipkokeibunsokutei));

        // 排出前撹拌_撹拌終了予定日
        this.setItemData(processData, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOUYOTEI_DAY, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOUYOTEI_DAY, srSlipSlipkokeibunsokutei));

        // 排出前撹拌_撹拌終了予定時間
        this.setItemData(processData, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOUYOTEI_TIME, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOUYOTEI_TIME, srSlipSlipkokeibunsokutei));

        // 排出前撹拌_撹拌終了日
        this.setItemData(processData, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOU_DAY, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOU_DAY, srSlipSlipkokeibunsokutei));

        // 排出前撹拌_撹拌終了時間
        this.setItemData(processData, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOU_TIME, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOU_TIME, srSlipSlipkokeibunsokutei));

        // 排出前撹拌_撹拌担当者
        this.setItemData(processData, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANTANTOUSYA, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANTANTOUSYA, srSlipSlipkokeibunsokutei));

        // 排出開始日
        this.setItemData(processData, GXHDO102B033Const.HAISYUTUKAISI_DAY, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.HAISYUTUKAISI_DAY, srSlipSlipkokeibunsokutei));

        // 排出開始時間
        this.setItemData(processData, GXHDO102B033Const.HAISYUTUKAISI_TIME, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.HAISYUTUKAISI_TIME, srSlipSlipkokeibunsokutei));

        // 排出終了日
        this.setItemData(processData, GXHDO102B033Const.HAISYUTUSYUURYOU_DAY, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.HAISYUTUSYUURYOU_DAY, srSlipSlipkokeibunsokutei));

        // 排出終了時間
        this.setItemData(processData, GXHDO102B033Const.HAISYUTUSYUURYOU_TIME, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.HAISYUTUSYUURYOU_TIME, srSlipSlipkokeibunsokutei));

        // 排出担当者
        this.setItemData(processData, GXHDO102B033Const.HAISYUTUTANTOUSYA, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.HAISYUTUTANTOUSYA, srSlipSlipkokeibunsokutei));

        // 備考1
        this.setItemData(processData, GXHDO102B033Const.BIKOU1, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.BIKOU1, srSlipSlipkokeibunsokutei));

        // 備考2
        this.setItemData(processData, GXHDO102B033Const.BIKOU2, getSrSlipSlipkokeibunsokuteiItemData(GXHDO102B033Const.BIKOU2, srSlipSlipkokeibunsokutei));

    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定データ
     * @throws SQLException 例外エラー
     */
    private List<SrSlipSlipkokeibunsokutei> getSrSlipSlipkokeibunsokuteiData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrSlipSlipkokeibunsokutei(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSrSlipSlipkokeibunsokutei(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        }
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
     * [品質DB登録実績]から、ﾘﾋﾞｼﾞｮﾝ,状態ﾌﾗｸﾞを取得
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
        // 品質DB登録実績データの取得
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
    private Map loadFxhdd11RevInfoWithLock(QueryRunner queryRunnerDoc, Connection conDoc, String kojyo, String lotNo,
            String edaban, int jissekino, String formId) throws SQLException {
        // 品質DB登録実績データの取得
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
        // 品質DB登録実績データの取得
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
     * [ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrSlipSlipkokeibunsokutei> loadSrSlipSlipkokeibunsokutei(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,goki,dassizaranosyurui,rutubono,rutubofuutaijyuuryou,"
                + "kansoumaeslipjyuuryou,kansouki1,kansouondo1,kansoujikan1,kansoukaisinichiji1,kansousyuuryounichiji1,kansouki2,kansouondo2,"
                + "kansoujikan2,kansoukaisinichiji2,kansousyuuryounichiji2,kansougosoujyuuryou,kansougosyoumijyuuryou,kokeibunhiritu,"
                + "kokeibunsokuteitantousya,youzaityouseiryou,toluenetyouseiryou,solmixtyouseiryou,youzaikeiryounichiji,youzai1_zairyouhinmei,"
                + "youzai1_tyougouryoukikaku,youzai1_buzaizaikolotno1,youzai1_tyougouryou1,youzai1_buzaizaikolotno2,youzai1_tyougouryou2,youzai2_zairyouhinmei,"
                + "youzai2_tyougouryoukikaku,youzai2_buzaizaikolotno1,youzai2_tyougouryou1,youzai2_buzaizaikolotno2,youzai2_tyougouryou2,tantousya,"
                + "kakuhan_kakuhanmode,kakuhan_kaitenhoukou,kakuhan_kaitensuu,kakuhan_kakuhanjikan,kakuhan_kakuhankaisinichiji,kaisidenryuuti,"
                + "kakuhan_kakuhansyuuryouyoteinichiji,kakuhan_kakuhansyuuryounichiji,syuuryoudenryuuti,kakuhan_kakuhantantousya,haisyutumaekakuhan_kakuhanmode,"
                + "haisyutumaekakuhan_kaitenhoukou,haisyutumaekakuhan_kaitensuu,haisyutumaekakuhan_kakuhanjikan,haisyutumaekakuhan_kakuhankaisinichiji,"
                + "haisyutumaekakuhan_kakuhansyuuryouyoteinichiji,haisyutumaekakuhan_kakuhansyuuryounichiji,haisyutumaekakuhan_kakuhantantousya,haisyutukaisinichiji,"
                + "haisyutusyuuryounichiji,haisyututantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + " FROM sr_slip_slipkokeibunsokutei "
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
        mapping.put("kojyo", "kojyo");                                                                                      // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                                                                      // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                                                                    // 枝番
        mapping.put("sliphinmei", "sliphinmei");                                                                            // ｽﾘｯﾌﾟ品名
        mapping.put("sliplotno", "sliplotno");                                                                              // ｽﾘｯﾌﾟLotNo
        mapping.put("lotkubun", "lotkubun");                                                                                // ﾛｯﾄ区分
        mapping.put("genryoukigou", "genryoukigou");                                                                        // 原料記号
        mapping.put("goki", "goki");                                                                                        // 秤量号機
        mapping.put("dassizaranosyurui", "dassizaranosyurui");                                                              // 脱脂皿の種類
        mapping.put("rutubono", "rutubono");                                                                                // ﾙﾂﾎﾞNo
        mapping.put("rutubofuutaijyuuryou", "rutubofuutaijyuuryou");                                                        // ﾙﾂﾎﾞ風袋重量
        mapping.put("kansoumaeslipjyuuryou", "kansoumaeslipjyuuryou");                                                      // 乾燥前ｽﾘｯﾌﾟ重量
        mapping.put("kansouki1", "kansouki1");                                                                              // 乾燥機①
        mapping.put("kansouondo1", "kansouondo1");                                                                          // 乾燥温度①
        mapping.put("kansoujikan1", "kansoujikan1");                                                                        // 乾燥時間①
        mapping.put("kansoukaisinichiji1", "kansoukaisinichiji1");                                                          // 乾燥開始日時①
        mapping.put("kansousyuuryounichiji1", "kansousyuuryounichiji1");                                                    // 乾燥終了日時①
        mapping.put("kansouki2", "kansouki2");                                                                              // 乾燥機②
        mapping.put("kansouondo2", "kansouondo2");                                                                          // 乾燥温度②
        mapping.put("kansoujikan2", "kansoujikan2");                                                                        // 乾燥時間②
        mapping.put("kansoukaisinichiji2", "kansoukaisinichiji2");                                                          // 乾燥開始日時②
        mapping.put("kansousyuuryounichiji2", "kansousyuuryounichiji2");                                                    // 乾燥終了日時②
        mapping.put("kansougosoujyuuryou", "kansougosoujyuuryou");                                                          // 乾燥後総重量
        mapping.put("kansougosyoumijyuuryou", "kansougosyoumijyuuryou");                                                    // 乾燥後正味重量
        mapping.put("kokeibunhiritu", "kokeibunhiritu");                                                                    // 固形分比率
        mapping.put("kokeibunsokuteitantousya", "kokeibunsokuteitantousya");                                                // 固形分測定担当者
        mapping.put("youzaityouseiryou", "youzaityouseiryou");                                                              // 溶剤調整量
        mapping.put("toluenetyouseiryou", "toluenetyouseiryou");                                                            // ﾄﾙｴﾝ調整量
        mapping.put("solmixtyouseiryou", "solmixtyouseiryou");                                                              // ｿﾙﾐｯｸｽ調整量
        mapping.put("youzaikeiryounichiji", "youzaikeiryounichiji");                                                        // 溶剤秤量日時
        mapping.put("youzai1_zairyouhinmei", "youzai1_zairyouhinmei");                                                      // 溶剤①_材料品名
        mapping.put("youzai1_tyougouryoukikaku", "youzai1_tyougouryoukikaku");                                              // 溶剤①_調合量規格
        mapping.put("youzai1_buzaizaikolotno1", "youzai1_buzaizaikolotno1");                                                // 溶剤①_部材在庫No1
        mapping.put("youzai1_tyougouryou1", "youzai1_tyougouryou1");                                                        // 溶剤①_調合量1
        mapping.put("youzai1_buzaizaikolotno2", "youzai1_buzaizaikolotno2");                                                // 溶剤①_部材在庫No2
        mapping.put("youzai1_tyougouryou2", "youzai1_tyougouryou2");                                                        // 溶剤①_調合量2
        mapping.put("youzai2_zairyouhinmei", "youzai2_zairyouhinmei");                                                      // 溶剤②_材料品名
        mapping.put("youzai2_tyougouryoukikaku", "youzai2_tyougouryoukikaku");                                              // 溶剤②_調合量規格
        mapping.put("youzai2_buzaizaikolotno1", "youzai2_buzaizaikolotno1");                                                // 溶剤②_部材在庫No1
        mapping.put("youzai2_tyougouryou1", "youzai2_tyougouryou1");                                                        // 溶剤②_調合量1
        mapping.put("youzai2_buzaizaikolotno2", "youzai2_buzaizaikolotno2");                                                // 溶剤②_部材在庫No2
        mapping.put("youzai2_tyougouryou2", "youzai2_tyougouryou2");                                                        // 溶剤②_調合量2
        mapping.put("tantousya", "tantousya");                                                                              // 担当者
        mapping.put("kakuhan_kakuhanmode", "kakuhan_kakuhanmode");                                                          // 撹拌_撹拌ﾓｰﾄﾞ
        mapping.put("kakuhan_kaitenhoukou", "kakuhan_kaitenhoukou");                                                        // 撹拌_回転方向
        mapping.put("kakuhan_kaitensuu", "kakuhan_kaitensuu");                                                              // 撹拌_回転数(rpm)
        mapping.put("kakuhan_kakuhanjikan", "kakuhan_kakuhanjikan");                                                        // 撹拌_撹拌時間
        mapping.put("kakuhan_kakuhankaisinichiji", "kakuhan_kakuhankaisinichiji");                                          // 撹拌_撹拌開始日時
        mapping.put("kaisidenryuuti", "kaisidenryuuti");                                                                    // 開始電流値(A)
        mapping.put("kakuhan_kakuhansyuuryouyoteinichiji", "kakuhan_kakuhansyuuryouyoteinichiji");                          // 撹拌_撹拌終了予定日時
        mapping.put("kakuhan_kakuhansyuuryounichiji", "kakuhan_kakuhansyuuryounichiji");                                    // 撹拌_撹拌終了日時
        mapping.put("syuuryoudenryuuti", "syuuryoudenryuuti");                                                              // 終了電流値(A)
        mapping.put("kakuhan_kakuhantantousya", "kakuhan_kakuhantantousya");                                                // 撹拌_撹拌担当者
        mapping.put("haisyutumaekakuhan_kakuhanmode", "haisyutumaekakuhan_kakuhanmode");                                    // 排出前撹拌_撹拌ﾓｰﾄﾞ
        mapping.put("haisyutumaekakuhan_kaitenhoukou", "haisyutumaekakuhan_kaitenhoukou");                                  // 排出前撹拌_回転方向
        mapping.put("haisyutumaekakuhan_kaitensuu", "haisyutumaekakuhan_kaitensuu");                                        // 排出前撹拌_回転数(rpm)
        mapping.put("haisyutumaekakuhan_kakuhanjikan", "haisyutumaekakuhan_kakuhanjikan");                                  // 排出前撹拌_撹拌時間
        mapping.put("haisyutumaekakuhan_kakuhankaisinichiji", "haisyutumaekakuhan_kakuhankaisinichiji");                    // 排出前撹拌_撹拌開始日時
        mapping.put("haisyutumaekakuhan_kakuhansyuuryouyoteinichiji", "haisyutumaekakuhan_kakuhansyuuryouyoteinichiji");    // 排出前撹拌_撹拌終了予定日時
        mapping.put("haisyutumaekakuhan_kakuhansyuuryounichiji", "haisyutumaekakuhan_kakuhansyuuryounichiji");              // 排出前撹拌_撹拌終了日時
        mapping.put("haisyutumaekakuhan_kakuhantantousya", "haisyutumaekakuhan_kakuhantantousya");                          // 排出前撹拌_撹拌担当者
        mapping.put("haisyutukaisinichiji", "haisyutukaisinichiji");                                                        // 排出開始日時
        mapping.put("haisyutusyuuryounichiji", "haisyutusyuuryounichiji");                                                  // 排出終了日時
        mapping.put("haisyututantousya", "haisyututantousya");                                                              // 排出担当者
        mapping.put("bikou1", "bikou1");                                                                                    // 備考1
        mapping.put("bikou2", "bikou2");                                                                                    // 備考2
        mapping.put("torokunichiji", "torokunichiji");                                                                      // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                                                                        // 更新日時
        mapping.put("revision", "revision");                                                                                // revision

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrSlipSlipkokeibunsokutei>> beanHandler = new BeanListHandler<>(SrSlipSlipkokeibunsokutei.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrSlipSlipkokeibunsokutei> loadTmpSrSlipSlipkokeibunsokutei(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,goki,dassizaranosyurui,rutubono,rutubofuutaijyuuryou,"
                + "kansoumaeslipjyuuryou,kansouki1,kansouondo1,kansoujikan1,kansoukaisinichiji1,kansousyuuryounichiji1,kansouki2,kansouondo2,"
                + "kansoujikan2,kansoukaisinichiji2,kansousyuuryounichiji2,kansougosoujyuuryou,kansougosyoumijyuuryou,kokeibunhiritu,"
                + "kokeibunsokuteitantousya,youzaityouseiryou,toluenetyouseiryou,solmixtyouseiryou,youzaikeiryounichiji,youzai1_zairyouhinmei,"
                + "youzai1_tyougouryoukikaku,youzai1_buzaizaikolotno1,youzai1_tyougouryou1,youzai1_buzaizaikolotno2,youzai1_tyougouryou2,youzai2_zairyouhinmei,"
                + "youzai2_tyougouryoukikaku,youzai2_buzaizaikolotno1,youzai2_tyougouryou1,youzai2_buzaizaikolotno2,youzai2_tyougouryou2,tantousya,"
                + "kakuhan_kakuhanmode,kakuhan_kaitenhoukou,kakuhan_kaitensuu,kakuhan_kakuhanjikan,kakuhan_kakuhankaisinichiji,kaisidenryuuti,"
                + "kakuhan_kakuhansyuuryouyoteinichiji,kakuhan_kakuhansyuuryounichiji,syuuryoudenryuuti,kakuhan_kakuhantantousya,haisyutumaekakuhan_kakuhanmode,"
                + "haisyutumaekakuhan_kaitenhoukou,haisyutumaekakuhan_kaitensuu,haisyutumaekakuhan_kakuhanjikan,haisyutumaekakuhan_kakuhankaisinichiji,"
                + "haisyutumaekakuhan_kakuhansyuuryouyoteinichiji,haisyutumaekakuhan_kakuhansyuuryounichiji,haisyutumaekakuhan_kakuhantantousya,haisyutukaisinichiji,"
                + "haisyutusyuuryounichiji,haisyututantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + " FROM tmp_sr_slip_slipkokeibunsokutei "
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
        mapping.put("kojyo", "kojyo");                                                                                      // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                                                                      // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                                                                    // 枝番
        mapping.put("sliphinmei", "sliphinmei");                                                                            // ｽﾘｯﾌﾟ品名
        mapping.put("sliplotno", "sliplotno");                                                                              // ｽﾘｯﾌﾟLotNo
        mapping.put("lotkubun", "lotkubun");                                                                                // ﾛｯﾄ区分
        mapping.put("genryoukigou", "genryoukigou");                                                                        // 原料記号
        mapping.put("goki", "goki");                                                                                        // 秤量号機
        mapping.put("dassizaranosyurui", "dassizaranosyurui");                                                              // 脱脂皿の種類
        mapping.put("rutubono", "rutubono");                                                                                // ﾙﾂﾎﾞNo
        mapping.put("rutubofuutaijyuuryou", "rutubofuutaijyuuryou");                                                        // ﾙﾂﾎﾞ風袋重量
        mapping.put("kansoumaeslipjyuuryou", "kansoumaeslipjyuuryou");                                                      // 乾燥前ｽﾘｯﾌﾟ重量
        mapping.put("kansouki1", "kansouki1");                                                                              // 乾燥機①
        mapping.put("kansouondo1", "kansouondo1");                                                                          // 乾燥温度①
        mapping.put("kansoujikan1", "kansoujikan1");                                                                        // 乾燥時間①
        mapping.put("kansoukaisinichiji1", "kansoukaisinichiji1");                                                          // 乾燥開始日時①
        mapping.put("kansousyuuryounichiji1", "kansousyuuryounichiji1");                                                    // 乾燥終了日時①
        mapping.put("kansouki2", "kansouki2");                                                                              // 乾燥機②
        mapping.put("kansouondo2", "kansouondo2");                                                                          // 乾燥温度②
        mapping.put("kansoujikan2", "kansoujikan2");                                                                        // 乾燥時間②
        mapping.put("kansoukaisinichiji2", "kansoukaisinichiji2");                                                          // 乾燥開始日時②
        mapping.put("kansousyuuryounichiji2", "kansousyuuryounichiji2");                                                    // 乾燥終了日時②
        mapping.put("kansougosoujyuuryou", "kansougosoujyuuryou");                                                          // 乾燥後総重量
        mapping.put("kansougosyoumijyuuryou", "kansougosyoumijyuuryou");                                                    // 乾燥後正味重量
        mapping.put("kokeibunhiritu", "kokeibunhiritu");                                                                    // 固形分比率
        mapping.put("kokeibunsokuteitantousya", "kokeibunsokuteitantousya");                                                // 固形分測定担当者
        mapping.put("youzaityouseiryou", "youzaityouseiryou");                                                              // 溶剤調整量
        mapping.put("toluenetyouseiryou", "toluenetyouseiryou");                                                            // ﾄﾙｴﾝ調整量
        mapping.put("solmixtyouseiryou", "solmixtyouseiryou");                                                              // ｿﾙﾐｯｸｽ調整量
        mapping.put("youzaikeiryounichiji", "youzaikeiryounichiji");                                                        // 溶剤秤量日時
        mapping.put("youzai1_zairyouhinmei", "youzai1_zairyouhinmei");                                                      // 溶剤①_材料品名
        mapping.put("youzai1_tyougouryoukikaku", "youzai1_tyougouryoukikaku");                                              // 溶剤①_調合量規格
        mapping.put("youzai1_buzaizaikolotno1", "youzai1_buzaizaikolotno1");                                                // 溶剤①_部材在庫No1
        mapping.put("youzai1_tyougouryou1", "youzai1_tyougouryou1");                                                        // 溶剤①_調合量1
        mapping.put("youzai1_buzaizaikolotno2", "youzai1_buzaizaikolotno2");                                                // 溶剤①_部材在庫No2
        mapping.put("youzai1_tyougouryou2", "youzai1_tyougouryou2");                                                        // 溶剤①_調合量2
        mapping.put("youzai2_zairyouhinmei", "youzai2_zairyouhinmei");                                                      // 溶剤②_材料品名
        mapping.put("youzai2_tyougouryoukikaku", "youzai2_tyougouryoukikaku");                                              // 溶剤②_調合量規格
        mapping.put("youzai2_buzaizaikolotno1", "youzai2_buzaizaikolotno1");                                                // 溶剤②_部材在庫No1
        mapping.put("youzai2_tyougouryou1", "youzai2_tyougouryou1");                                                        // 溶剤②_調合量1
        mapping.put("youzai2_buzaizaikolotno2", "youzai2_buzaizaikolotno2");                                                // 溶剤②_部材在庫No2
        mapping.put("youzai2_tyougouryou2", "youzai2_tyougouryou2");                                                        // 溶剤②_調合量2
        mapping.put("tantousya", "tantousya");                                                                              // 担当者
        mapping.put("kakuhan_kakuhanmode", "kakuhan_kakuhanmode");                                                          // 撹拌_撹拌ﾓｰﾄﾞ
        mapping.put("kakuhan_kaitenhoukou", "kakuhan_kaitenhoukou");                                                        // 撹拌_回転方向
        mapping.put("kakuhan_kaitensuu", "kakuhan_kaitensuu");                                                              // 撹拌_回転数(rpm)
        mapping.put("kakuhan_kakuhanjikan", "kakuhan_kakuhanjikan");                                                        // 撹拌_撹拌時間
        mapping.put("kakuhan_kakuhankaisinichiji", "kakuhan_kakuhankaisinichiji");                                          // 撹拌_撹拌開始日時
        mapping.put("kaisidenryuuti", "kaisidenryuuti");                                                                    // 開始電流値(A)
        mapping.put("kakuhan_kakuhansyuuryouyoteinichiji", "kakuhan_kakuhansyuuryouyoteinichiji");                          // 撹拌_撹拌終了予定日時
        mapping.put("kakuhan_kakuhansyuuryounichiji", "kakuhan_kakuhansyuuryounichiji");                                    // 撹拌_撹拌終了日時
        mapping.put("syuuryoudenryuuti", "syuuryoudenryuuti");                                                              // 終了電流値(A)
        mapping.put("kakuhan_kakuhantantousya", "kakuhan_kakuhantantousya");                                                // 撹拌_撹拌担当者
        mapping.put("haisyutumaekakuhan_kakuhanmode", "haisyutumaekakuhan_kakuhanmode");                                    // 排出前撹拌_撹拌ﾓｰﾄﾞ
        mapping.put("haisyutumaekakuhan_kaitenhoukou", "haisyutumaekakuhan_kaitenhoukou");                                  // 排出前撹拌_回転方向
        mapping.put("haisyutumaekakuhan_kaitensuu", "haisyutumaekakuhan_kaitensuu");                                        // 排出前撹拌_回転数(rpm)
        mapping.put("haisyutumaekakuhan_kakuhanjikan", "haisyutumaekakuhan_kakuhanjikan");                                  // 排出前撹拌_撹拌時間
        mapping.put("haisyutumaekakuhan_kakuhankaisinichiji", "haisyutumaekakuhan_kakuhankaisinichiji");                    // 排出前撹拌_撹拌開始日時
        mapping.put("haisyutumaekakuhan_kakuhansyuuryouyoteinichiji", "haisyutumaekakuhan_kakuhansyuuryouyoteinichiji");    // 排出前撹拌_撹拌終了予定日時
        mapping.put("haisyutumaekakuhan_kakuhansyuuryounichiji", "haisyutumaekakuhan_kakuhansyuuryounichiji");              // 排出前撹拌_撹拌終了日時
        mapping.put("haisyutumaekakuhan_kakuhantantousya", "haisyutumaekakuhan_kakuhantantousya");                          // 排出前撹拌_撹拌担当者
        mapping.put("haisyutukaisinichiji", "haisyutukaisinichiji");                                                        // 排出開始日時
        mapping.put("haisyutusyuuryounichiji", "haisyutusyuuryounichiji");                                                  // 排出終了日時
        mapping.put("haisyututantousya", "haisyututantousya");                                                              // 排出担当者
        mapping.put("bikou1", "bikou1");                                                                                    // 備考1
        mapping.put("bikou2", "bikou2");                                                                                    // 備考2
        mapping.put("torokunichiji", "torokunichiji");                                                                      // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                                                                        // 更新日時
        mapping.put("revision", "revision");                                                                                // revision
        mapping.put("deleteflag", "deleteflag");                                                                            // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrSlipSlipkokeibunsokutei>> beanHandler = new BeanListHandler<>(SrSlipSlipkokeibunsokutei.class, rowProcessor);

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
     * @param srSlipSlipkokeibunsokutei ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定データ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrSlipSlipkokeibunsokutei srSlipSlipkokeibunsokutei) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srSlipSlipkokeibunsokutei != null) {
            // 元データが存在する場合元データより取得
            return getSrSlipSlipkokeibunsokuteiItemData(itemId, srSlipSlipkokeibunsokutei);
        } else {
            return null;
        }
    }

    /**
     * 項目データ(入力値)取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @param srSlipSlipkokeibunsokutei ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定データ
     * @return 入力値
     */
    private String getItemKikakuchi(List<FXHDD01> listData, String itemId, SrSlipSlipkokeibunsokutei srSlipSlipkokeibunsokutei) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return StringUtil.nullToBlank(selectData.get(0).getKikakuChi()).replace("【", "").replace("】", "");
        } else if (srSlipSlipkokeibunsokutei != null) {
            // 元データが存在する場合元データより取得
            return getSrSlipSlipkokeibunsokuteiItemData(itemId, srSlipSlipkokeibunsokutei);
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
                + "? ,? ,? , ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

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
     * ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定_仮登録(tmp_sr_slip_slipkokeibunsokutei)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void insertTmpSrSlipSlipkokeibunsokutei(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "INSERT INTO tmp_sr_slip_slipkokeibunsokutei ( "
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,goki,dassizaranosyurui,rutubono,rutubofuutaijyuuryou,"
                + "kansoumaeslipjyuuryou,kansouki1,kansouondo1,kansoujikan1,kansoukaisinichiji1,kansousyuuryounichiji1,kansouki2,kansouondo2,"
                + "kansoujikan2,kansoukaisinichiji2,kansousyuuryounichiji2,kansougosoujyuuryou,kansougosyoumijyuuryou,kokeibunhiritu,"
                + "kokeibunsokuteitantousya,youzaityouseiryou,toluenetyouseiryou,solmixtyouseiryou,youzaikeiryounichiji,youzai1_zairyouhinmei,"
                + "youzai1_tyougouryoukikaku,youzai1_buzaizaikolotno1,youzai1_tyougouryou1,youzai1_buzaizaikolotno2,youzai1_tyougouryou2,youzai2_zairyouhinmei,"
                + "youzai2_tyougouryoukikaku,youzai2_buzaizaikolotno1,youzai2_tyougouryou1,youzai2_buzaizaikolotno2,youzai2_tyougouryou2,tantousya,"
                + "kakuhan_kakuhanmode,kakuhan_kaitenhoukou,kakuhan_kaitensuu,kakuhan_kakuhanjikan,kakuhan_kakuhankaisinichiji,kaisidenryuuti,"
                + "kakuhan_kakuhansyuuryouyoteinichiji,kakuhan_kakuhansyuuryounichiji,syuuryoudenryuuti,kakuhan_kakuhantantousya,haisyutumaekakuhan_kakuhanmode,"
                + "haisyutumaekakuhan_kaitenhoukou,haisyutumaekakuhan_kaitensuu,haisyutumaekakuhan_kakuhanjikan,haisyutumaekakuhan_kakuhankaisinichiji,"
                + "haisyutumaekakuhan_kakuhansyuuryouyoteinichiji,haisyutumaekakuhan_kakuhansyuuryounichiji,haisyutumaekakuhan_kakuhantantousya,haisyutukaisinichiji,"
                + "haisyutusyuuryounichiji,haisyututantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrSlipSlipkokeibunsokutei(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, processData, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定_仮登録(tmp_sr_slip_slipkokeibunsokutei)更新処理
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
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void updateTmpSrSlipSlipkokeibunsokutei(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "UPDATE tmp_sr_slip_slipkokeibunsokutei SET "
                + " sliphinmei = ?,sliplotno = ?,lotkubun = ?,genryoukigou = ?,goki = ?,dassizaranosyurui = ?,rutubono = ?,rutubofuutaijyuuryou = ?,kansoumaeslipjyuuryou = ?,"
                + "kansouki1 = ?,kansouondo1 = ?,kansoujikan1 = ?,kansoukaisinichiji1 = ?,kansousyuuryounichiji1 = ?,kansouki2 = ?,kansouondo2 = ?,kansoujikan2 = ?,kansoukaisinichiji2 = ?,"
                + "kansousyuuryounichiji2 = ?,kansougosoujyuuryou = ?,kansougosyoumijyuuryou = ?,kokeibunhiritu = ?,kokeibunsokuteitantousya = ?,youzaityouseiryou = ?,"
                + "toluenetyouseiryou = ?,solmixtyouseiryou = ?,youzaikeiryounichiji = ?,youzai1_zairyouhinmei = ?,youzai1_tyougouryoukikaku = ?,youzai1_buzaizaikolotno1 = ?,"
                + "youzai1_tyougouryou1 = ?,youzai1_buzaizaikolotno2 = ?,youzai1_tyougouryou2 = ?,youzai2_zairyouhinmei = ?,youzai2_tyougouryoukikaku = ?,youzai2_buzaizaikolotno1 = ?,"
                + "youzai2_tyougouryou1 = ?,youzai2_buzaizaikolotno2 = ?,youzai2_tyougouryou2 = ?,tantousya = ?,kakuhan_kakuhanmode = ?,kakuhan_kaitenhoukou = ?,kakuhan_kaitensuu = ?,"
                + "kakuhan_kakuhanjikan = ?,kakuhan_kakuhankaisinichiji = ?,kaisidenryuuti = ?,kakuhan_kakuhansyuuryouyoteinichiji = ?,kakuhan_kakuhansyuuryounichiji = ?,"
                + "syuuryoudenryuuti = ?,kakuhan_kakuhantantousya = ?,haisyutumaekakuhan_kakuhanmode = ?,haisyutumaekakuhan_kaitenhoukou = ?,haisyutumaekakuhan_kaitensuu = ?,"
                + "haisyutumaekakuhan_kakuhanjikan = ?,haisyutumaekakuhan_kakuhankaisinichiji = ?,haisyutumaekakuhan_kakuhansyuuryouyoteinichiji = ?,haisyutumaekakuhan_kakuhansyuuryounichiji = ?,"
                + "haisyutumaekakuhan_kakuhantantousya = ?,haisyutukaisinichiji = ?,haisyutusyuuryounichiji = ?,haisyututantousya = ?,bikou1 = ?,bikou2 = ?,kosinnichiji = ?,"
                + "revision = ?,deleteflag = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrSlipSlipkokeibunsokutei> srSlipSlipkokeibunsokuteiList = getSrSlipSlipkokeibunsokuteiData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrSlipSlipkokeibunsokutei srSlipSlipkokeibunsokutei = null;
        if (!srSlipSlipkokeibunsokuteiList.isEmpty()) {
            srSlipSlipkokeibunsokutei = srSlipSlipkokeibunsokuteiList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrSlipSlipkokeibunsokutei(false, newRev, 0, "", "", "", systemTime, processData, srSlipSlipkokeibunsokutei);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定_仮登録(tmp_sr_slip_slipkokeibunsokutei)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrSlipSlipkokeibunsokutei(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sr_slip_slipkokeibunsokutei "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

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
     * ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定_仮登録(tmp_sr_slip_slipkokeibunsokutei)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srSlipSlipkokeibunsokutei ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定データ
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrSlipSlipkokeibunsokutei(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, String systemTime, ProcessData processData, SrSlipSlipkokeibunsokutei srSlipSlipkokeibunsokutei) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // 乾燥開始日時①
        String kansoukaisi1Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B033Const.KANSOUKAISI1_TIME, srSlipSlipkokeibunsokutei));
        // 乾燥終了日時①
        String kansousyuuryou1Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B033Const.KANSOUSYUURYOU1_TIME, srSlipSlipkokeibunsokutei));
        // 乾燥開始日時②
        String kansoukaisi2Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B033Const.KANSOUKAISI2_TIME, srSlipSlipkokeibunsokutei));
        // 乾燥終了日時②
        String kansousyuuryou2Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B033Const.KANSOUSYUURYOU2_TIME, srSlipSlipkokeibunsokutei));
        // 溶剤秤量日時
        String youzaikeiryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B033Const.YOUZAIKEIRYOU_TIME, srSlipSlipkokeibunsokutei));
        // 撹拌_撹拌開始日時
        String kakuhankakuhankaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B033Const.KAKUHAN_KAKUHANKAISI_TIME, srSlipSlipkokeibunsokutei));
        // 撹拌_撹拌終了予定日時
        String kkhkakuhansyuuryouyoteiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOUYOTEI_TIME, srSlipSlipkokeibunsokutei));
        // 撹拌_撹拌終了日時
        String kkhkakuhansyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOU_TIME, srSlipSlipkokeibunsokutei));
        // 排出前撹拌_撹拌開始日時
        String hmkkkkhkaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANKAISI_TIME, srSlipSlipkokeibunsokutei));
        // 排出前撹拌_撹拌終了予定日時
        String hmkkkkhsyuuryouyoteiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOUYOTEI_TIME, srSlipSlipkokeibunsokutei));
        // 排出前撹拌_撹拌終了日時
        String hmkkkkhsyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOU_TIME, srSlipSlipkokeibunsokutei));
        // 排出開始日時
        String haisyutukaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B033Const.HAISYUTUKAISI_TIME, srSlipSlipkokeibunsokutei));
        // 排出終了日時
        String haisyutusyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B033Const.HAISYUTUSYUURYOU_TIME, srSlipSlipkokeibunsokutei));

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.SLIPHINMEI, srSlipSlipkokeibunsokutei)));                                 // ｽﾘｯﾌﾟ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.SLIPLOTNO, srSlipSlipkokeibunsokutei)));                                  // ｽﾘｯﾌﾟLotNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.LOTKUBUN, srSlipSlipkokeibunsokutei)));                                   // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B033Const.GENRYOUKIGOU, srSlipSlipkokeibunsokutei)));                          // 原料記号
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.GOKI, srSlipSlipkokeibunsokutei)));                                       // 秤量号機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B033Const.DASSIZARANOSYURUI, srSlipSlipkokeibunsokutei)));                     // 脱脂皿の種類
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.RUTUBONO, srSlipSlipkokeibunsokutei)));                                      // ﾙﾂﾎﾞNo
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.RUTUBOFUUTAIJYUURYOU, srSlipSlipkokeibunsokutei)));                   // ﾙﾂﾎﾞ風袋重量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.KANSOUMAESLIPJYUURYOU, srSlipSlipkokeibunsokutei)));                  // 乾燥前ｽﾘｯﾌﾟ重量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B033Const.KANSOUKI1, srSlipSlipkokeibunsokutei)));                             // 乾燥機①
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B033Const.KANSOUONDO1, srSlipSlipkokeibunsokutei)));                           // 乾燥温度①
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B033Const.KANSOUJIKAN1, srSlipSlipkokeibunsokutei)));                          // 乾燥時間①
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.KANSOUKAISI1_DAY, srSlipSlipkokeibunsokutei),
                "".equals(kansoukaisi1Time) ? "0000" : kansoukaisi1Time));                                                                                                   // 乾燥開始日時①
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.KANSOUSYUURYOU1_DAY, srSlipSlipkokeibunsokutei),
                "".equals(kansousyuuryou1Time) ? "0000" : kansousyuuryou1Time));                                                                                             // 乾燥終了日時①
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B033Const.KANSOUKI2, srSlipSlipkokeibunsokutei)));                             // 乾燥機②
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B033Const.KANSOUONDO2, srSlipSlipkokeibunsokutei)));                           // 乾燥温度②
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B033Const.KANSOUJIKAN2, srSlipSlipkokeibunsokutei)));                          // 乾燥時間②
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.KANSOUKAISI2_DAY, srSlipSlipkokeibunsokutei),
                "".equals(kansoukaisi2Time) ? "0000" : kansoukaisi2Time));                                                                                                   // 乾燥開始日時②
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.KANSOUSYUURYOU2_DAY, srSlipSlipkokeibunsokutei),
                "".equals(kansousyuuryou2Time) ? "0000" : kansousyuuryou2Time));                                                                                             // 乾燥終了日時②
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.KANSOUGOSOUJYUURYOU, srSlipSlipkokeibunsokutei)));                    // 乾燥後総重量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.KANSOUGOSYOUMIJYUURYOU, srSlipSlipkokeibunsokutei)));                 // 乾燥後正味重量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.KOKEIBUNHIRITU, srSlipSlipkokeibunsokutei)));                         // 固形分比率
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.KOKEIBUNSOKUTEITANTOUSYA, srSlipSlipkokeibunsokutei)));                   // 固形分測定担当者
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.YOUZAITYOUSEIRYOU, srSlipSlipkokeibunsokutei)));                             // 溶剤調整量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.TOLUENETYOUSEIRYOU, srSlipSlipkokeibunsokutei)));                            // ﾄﾙｴﾝ調整量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.SOLMIXTYOUSEIRYOU, srSlipSlipkokeibunsokutei)));                             // ｿﾙﾐｯｸｽ調整量
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.YOUZAIKEIRYOU_DAY, srSlipSlipkokeibunsokutei),
                "".equals(youzaikeiryouTime) ? "0000" : youzaikeiryouTime));                                                                                                 // 溶剤秤量日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B033Const.YOUZAI1_ZAIRYOUHINMEI, srSlipSlipkokeibunsokutei)));                 // 溶剤①_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B033Const.YOUZAI1_TYOUGOURYOUKIKAKU, srSlipSlipkokeibunsokutei)));             // 溶剤①_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.YOUZAI1_BUZAIZAIKOLOTNO1, srSlipSlipkokeibunsokutei)));                   // 溶剤①_部材在庫No1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.YOUZAI1_TYOUGOURYOU1, srSlipSlipkokeibunsokutei)));                          // 溶剤①_調合量1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.YOUZAI1_BUZAIZAIKOLOTNO2, srSlipSlipkokeibunsokutei)));                   // 溶剤①_部材在庫No2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.YOUZAI1_TYOUGOURYOU2, srSlipSlipkokeibunsokutei)));                          // 溶剤①_調合量2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B033Const.YOUZAI2_ZAIRYOUHINMEI, srSlipSlipkokeibunsokutei)));                 // 溶剤②_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B033Const.YOUZAI2_TYOUGOURYOUKIKAKU, srSlipSlipkokeibunsokutei)));             // 溶剤②_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.YOUZAI2_BUZAIZAIKOLOTNO1, srSlipSlipkokeibunsokutei)));                   // 溶剤②_部材在庫No1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.YOUZAI2_TYOUGOURYOU1, srSlipSlipkokeibunsokutei)));                          // 溶剤②_調合量1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.YOUZAI2_BUZAIZAIKOLOTNO2, srSlipSlipkokeibunsokutei)));                   // 溶剤②_部材在庫No2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.YOUZAI2_TYOUGOURYOU2, srSlipSlipkokeibunsokutei)));                          // 溶剤②_調合量2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.TANTOUSYA, srSlipSlipkokeibunsokutei)));                                  // 担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B033Const.KAKUHAN_KAKUHANMODE, srSlipSlipkokeibunsokutei)));                   // 撹拌_撹拌ﾓｰﾄﾞ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B033Const.KAKUHAN_KAITENHOUKOU, srSlipSlipkokeibunsokutei)));                  // 撹拌_回転方向
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B033Const.KAKUHAN_KAITENSUU, srSlipSlipkokeibunsokutei)));                     // 撹拌_回転数(rpm)
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B033Const.KAKUHAN_KAKUHANJIKAN, srSlipSlipkokeibunsokutei)));                  // 撹拌_撹拌時間
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.KAKUHAN_KAKUHANKAISI_DAY, srSlipSlipkokeibunsokutei),
                "".equals(kakuhankakuhankaisiTime) ? "0000" : kakuhankakuhankaisiTime));                                                                                     // 撹拌_撹拌開始日時
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.KAISIDENRYUUTI, srSlipSlipkokeibunsokutei)));                         // 開始電流値(A)
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOUYOTEI_DAY, srSlipSlipkokeibunsokutei),
                "".equals(kkhkakuhansyuuryouyoteiTime) ? "0000" : kkhkakuhansyuuryouyoteiTime));                                                                             // 撹拌_撹拌終了予定日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOU_DAY, srSlipSlipkokeibunsokutei),
                "".equals(kkhkakuhansyuuryouTime) ? "0000" : kkhkakuhansyuuryouTime));                                                                                       // 撹拌_撹拌終了日時
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.SYUURYOUDENRYUUTI, srSlipSlipkokeibunsokutei)));                      // 終了電流値(A)
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.KAKUHAN_KAKUHANTANTOUSYA, srSlipSlipkokeibunsokutei)));                   // 撹拌_撹拌担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANMODE, srSlipSlipkokeibunsokutei)));        // 排出前撹拌_撹拌ﾓｰﾄﾞ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAITENHOUKOU, srSlipSlipkokeibunsokutei)));       // 排出前撹拌_回転方向
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAITENSUU, srSlipSlipkokeibunsokutei)));          // 排出前撹拌_回転数(rpm)
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANJIKAN, srSlipSlipkokeibunsokutei)));       // 排出前撹拌_撹拌時間
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANKAISI_DAY, srSlipSlipkokeibunsokutei),
                "".equals(hmkkkkhkaisiTime) ? "0000" : hmkkkkhkaisiTime));                                                                                                   // 排出前撹拌_撹拌開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOUYOTEI_DAY, srSlipSlipkokeibunsokutei),
                "".equals(hmkkkkhsyuuryouyoteiTime) ? "0000" : hmkkkkhsyuuryouyoteiTime));                                                                                   // 排出前撹拌_撹拌終了予定日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOU_DAY, srSlipSlipkokeibunsokutei),
                "".equals(hmkkkkhsyuuryouTime) ? "0000" : hmkkkkhsyuuryouTime));                                                                                             // 排出前撹拌_撹拌終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANTANTOUSYA, srSlipSlipkokeibunsokutei)));        // 排出前撹拌_撹拌担当者
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.HAISYUTUKAISI_DAY, srSlipSlipkokeibunsokutei),
                "".equals(haisyutukaisiTime) ? "0000" : haisyutukaisiTime));                                                                                                 // 排出開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.HAISYUTUSYUURYOU_DAY, srSlipSlipkokeibunsokutei),
                "".equals(haisyutusyuuryouTime) ? "0000" : haisyutusyuuryouTime));                                                                                           // 排出終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.HAISYUTUTANTOUSYA, srSlipSlipkokeibunsokutei)));                          // 排出担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.BIKOU1, srSlipSlipkokeibunsokutei)));                                     // 備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B033Const.BIKOU2, srSlipSlipkokeibunsokutei)));                                     // 備考2

        if (isInsert) {
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時
        } else {
            params.add(systemTime); //更新日時
        }
        params.add(newRev);         //revision
        params.add(deleteflag);     //削除ﾌﾗｸﾞ

        return params;
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定(sr_slip_slipkokeibunsokutei)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param tmpSrSlipSlipkokeibunsokutei 仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrSlipSlipkokeibunsokutei(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData, SrSlipSlipkokeibunsokutei tmpSrSlipSlipkokeibunsokutei) throws SQLException {

        String sql = "INSERT INTO sr_slip_slipkokeibunsokutei ( "
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,goki,dassizaranosyurui,rutubono,rutubofuutaijyuuryou,"
                + "kansoumaeslipjyuuryou,kansouki1,kansouondo1,kansoujikan1,kansoukaisinichiji1,kansousyuuryounichiji1,kansouki2,kansouondo2,"
                + "kansoujikan2,kansoukaisinichiji2,kansousyuuryounichiji2,kansougosoujyuuryou,kansougosyoumijyuuryou,kokeibunhiritu,"
                + "kokeibunsokuteitantousya,youzaityouseiryou,toluenetyouseiryou,solmixtyouseiryou,youzaikeiryounichiji,youzai1_zairyouhinmei,"
                + "youzai1_tyougouryoukikaku,youzai1_buzaizaikolotno1,youzai1_tyougouryou1,youzai1_buzaizaikolotno2,youzai1_tyougouryou2,youzai2_zairyouhinmei,"
                + "youzai2_tyougouryoukikaku,youzai2_buzaizaikolotno1,youzai2_tyougouryou1,youzai2_buzaizaikolotno2,youzai2_tyougouryou2,tantousya,"
                + "kakuhan_kakuhanmode,kakuhan_kaitenhoukou,kakuhan_kaitensuu,kakuhan_kakuhanjikan,kakuhan_kakuhankaisinichiji,kaisidenryuuti,"
                + "kakuhan_kakuhansyuuryouyoteinichiji,kakuhan_kakuhansyuuryounichiji,syuuryoudenryuuti,kakuhan_kakuhantantousya,haisyutumaekakuhan_kakuhanmode,"
                + "haisyutumaekakuhan_kaitenhoukou,haisyutumaekakuhan_kaitensuu,haisyutumaekakuhan_kakuhanjikan,haisyutumaekakuhan_kakuhankaisinichiji,"
                + "haisyutumaekakuhan_kakuhansyuuryouyoteinichiji,haisyutumaekakuhan_kakuhansyuuryounichiji,haisyutumaekakuhan_kakuhantantousya,haisyutukaisinichiji,"
                + "haisyutusyuuryounichiji,haisyututantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrSlipSlipkokeibunsokutei(true, newRev, kojyo, lotNo, edaban, systemTime, processData, tmpSrSlipSlipkokeibunsokutei);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定(sr_slip_slipkokeibunsokutei)更新処理
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
    private void updateSrSlipSlipkokeibunsokutei(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {
        String sql = "UPDATE sr_slip_slipkokeibunsokutei SET "
                + " sliphinmei = ?,sliplotno = ?,lotkubun = ?,genryoukigou = ?,goki = ?,dassizaranosyurui = ?,rutubono = ?,rutubofuutaijyuuryou = ?,kansoumaeslipjyuuryou = ?,"
                + "kansouki1 = ?,kansouondo1 = ?,kansoujikan1 = ?,kansoukaisinichiji1 = ?,kansousyuuryounichiji1 = ?,kansouki2 = ?,kansouondo2 = ?,kansoujikan2 = ?,kansoukaisinichiji2 = ?,"
                + "kansousyuuryounichiji2 = ?,kansougosoujyuuryou = ?,kansougosyoumijyuuryou = ?,kokeibunhiritu = ?,kokeibunsokuteitantousya = ?,youzaityouseiryou = ?,"
                + "toluenetyouseiryou = ?,solmixtyouseiryou = ?,youzaikeiryounichiji = ?,youzai1_zairyouhinmei = ?,youzai1_tyougouryoukikaku = ?,youzai1_buzaizaikolotno1 = ?,"
                + "youzai1_tyougouryou1 = ?,youzai1_buzaizaikolotno2 = ?,youzai1_tyougouryou2 = ?,youzai2_zairyouhinmei = ?,youzai2_tyougouryoukikaku = ?,youzai2_buzaizaikolotno1 = ?,"
                + "youzai2_tyougouryou1 = ?,youzai2_buzaizaikolotno2 = ?,youzai2_tyougouryou2 = ?,tantousya = ?,kakuhan_kakuhanmode = ?,kakuhan_kaitenhoukou = ?,kakuhan_kaitensuu = ?,"
                + "kakuhan_kakuhanjikan = ?,kakuhan_kakuhankaisinichiji = ?,kaisidenryuuti = ?,kakuhan_kakuhansyuuryouyoteinichiji = ?,kakuhan_kakuhansyuuryounichiji = ?,"
                + "syuuryoudenryuuti = ?,kakuhan_kakuhantantousya = ?,haisyutumaekakuhan_kakuhanmode = ?,haisyutumaekakuhan_kaitenhoukou = ?,haisyutumaekakuhan_kaitensuu = ?,"
                + "haisyutumaekakuhan_kakuhanjikan = ?,haisyutumaekakuhan_kakuhankaisinichiji = ?,haisyutumaekakuhan_kakuhansyuuryouyoteinichiji = ?,haisyutumaekakuhan_kakuhansyuuryounichiji = ?,"
                + "haisyutumaekakuhan_kakuhantantousya = ?,haisyutukaisinichiji = ?,haisyutusyuuryounichiji = ?,haisyututantousya = ?,bikou1 = ?,bikou2 = ?,kosinnichiji = ?,"
                + "revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrSlipSlipkokeibunsokutei> srSlipSlipkokeibunsokuteiList = getSrSlipSlipkokeibunsokuteiData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrSlipSlipkokeibunsokutei srSlipSlipkokeibunsokutei = null;
        if (!srSlipSlipkokeibunsokuteiList.isEmpty()) {
            srSlipSlipkokeibunsokutei = srSlipSlipkokeibunsokuteiList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrSlipSlipkokeibunsokutei(false, newRev, "", "", "", systemTime, processData, srSlipSlipkokeibunsokutei);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定(sr_slip_slipkokeibunsokutei)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srSlipSlipkokeibunsokutei ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrSlipSlipkokeibunsokutei(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            String systemTime, ProcessData processData, SrSlipSlipkokeibunsokutei srSlipSlipkokeibunsokutei) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();

        // 乾燥開始日時①
        String kansoukaisi1Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B033Const.KANSOUKAISI1_TIME, srSlipSlipkokeibunsokutei));
        // 乾燥終了日時①
        String kansousyuuryou1Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B033Const.KANSOUSYUURYOU1_TIME, srSlipSlipkokeibunsokutei));
        // 乾燥開始日時②
        String kansoukaisi2Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B033Const.KANSOUKAISI2_TIME, srSlipSlipkokeibunsokutei));
        // 乾燥終了日時②
        String kansousyuuryou2Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B033Const.KANSOUSYUURYOU2_TIME, srSlipSlipkokeibunsokutei));
        // 溶剤秤量日時
        String youzaikeiryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B033Const.YOUZAIKEIRYOU_TIME, srSlipSlipkokeibunsokutei));
        // 撹拌_撹拌開始日時
        String kakuhankakuhankaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B033Const.KAKUHAN_KAKUHANKAISI_TIME, srSlipSlipkokeibunsokutei));
        // 撹拌_撹拌終了予定日時
        String kkhkakuhansyuuryouyoteiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOUYOTEI_TIME, srSlipSlipkokeibunsokutei));
        // 撹拌_撹拌終了日時
        String kkhkakuhansyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOU_TIME, srSlipSlipkokeibunsokutei));
        // 排出前撹拌_撹拌開始日時
        String hmkkkkhkaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANKAISI_TIME, srSlipSlipkokeibunsokutei));
        // 排出前撹拌_撹拌終了予定日時
        String hmkkkkhsyuuryouyoteiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOUYOTEI_TIME, srSlipSlipkokeibunsokutei));
        // 排出前撹拌_撹拌終了日時
        String hmkkkkhsyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOU_TIME, srSlipSlipkokeibunsokutei));
        // 排出開始日時
        String haisyutukaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B033Const.HAISYUTUKAISI_TIME, srSlipSlipkokeibunsokutei));
        // 排出終了日時
        String haisyutusyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B033Const.HAISYUTUSYUURYOU_TIME, srSlipSlipkokeibunsokutei));

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }

        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B033Const.SLIPHINMEI, srSlipSlipkokeibunsokutei)));                                 // ｽﾘｯﾌﾟ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B033Const.SLIPLOTNO, srSlipSlipkokeibunsokutei)));                                  // ｽﾘｯﾌﾟLotNo
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B033Const.LOTKUBUN, srSlipSlipkokeibunsokutei)));                                   // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B033Const.GENRYOUKIGOU, srSlipSlipkokeibunsokutei)));                          // 原料記号
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B033Const.GOKI, srSlipSlipkokeibunsokutei)));                                       // 秤量号機
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B033Const.DASSIZARANOSYURUI, srSlipSlipkokeibunsokutei)));                     // 脱脂皿の種類
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B033Const.RUTUBONO, srSlipSlipkokeibunsokutei)));                                      // ﾙﾂﾎﾞNo
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B033Const.RUTUBOFUUTAIJYUURYOU, srSlipSlipkokeibunsokutei)));                   // ﾙﾂﾎﾞ風袋重量
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B033Const.KANSOUMAESLIPJYUURYOU, srSlipSlipkokeibunsokutei)));                  // 乾燥前ｽﾘｯﾌﾟ重量
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B033Const.KANSOUKI1, srSlipSlipkokeibunsokutei)));                             // 乾燥機①
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B033Const.KANSOUONDO1, srSlipSlipkokeibunsokutei)));                           // 乾燥温度①
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B033Const.KANSOUJIKAN1, srSlipSlipkokeibunsokutei)));                          // 乾燥時間①
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B033Const.KANSOUKAISI1_DAY, srSlipSlipkokeibunsokutei),
                "".equals(kansoukaisi1Time) ? "0000" : kansoukaisi1Time));                                                                                        // 乾燥開始日時①
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B033Const.KANSOUSYUURYOU1_DAY, srSlipSlipkokeibunsokutei),
                "".equals(kansousyuuryou1Time) ? "0000" : kansousyuuryou1Time));                                                                                  // 乾燥終了日時①
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B033Const.KANSOUKI2, srSlipSlipkokeibunsokutei)));                             // 乾燥機②
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B033Const.KANSOUONDO2, srSlipSlipkokeibunsokutei)));                           // 乾燥温度②
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B033Const.KANSOUJIKAN2, srSlipSlipkokeibunsokutei)));                          // 乾燥時間②
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B033Const.KANSOUKAISI2_DAY, srSlipSlipkokeibunsokutei),
                "".equals(kansoukaisi2Time) ? "0000" : kansoukaisi2Time));                                                                                        // 乾燥開始日時②
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B033Const.KANSOUSYUURYOU2_DAY, srSlipSlipkokeibunsokutei),
                "".equals(kansousyuuryou2Time) ? "0000" : kansousyuuryou2Time));                                                                                  // 乾燥終了日時②
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B033Const.KANSOUGOSOUJYUURYOU, srSlipSlipkokeibunsokutei)));                    // 乾燥後総重量
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B033Const.KANSOUGOSYOUMIJYUURYOU, srSlipSlipkokeibunsokutei)));                 // 乾燥後正味重量
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B033Const.KOKEIBUNHIRITU, srSlipSlipkokeibunsokutei)));                         // 固形分比率
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B033Const.KOKEIBUNSOKUTEITANTOUSYA, srSlipSlipkokeibunsokutei)));                   // 固形分測定担当者
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B033Const.YOUZAITYOUSEIRYOU, srSlipSlipkokeibunsokutei)));                             // 溶剤調整量
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B033Const.TOLUENETYOUSEIRYOU, srSlipSlipkokeibunsokutei)));                            // ﾄﾙｴﾝ調整量
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B033Const.SOLMIXTYOUSEIRYOU, srSlipSlipkokeibunsokutei)));                             // ｿﾙﾐｯｸｽ調整量
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B033Const.YOUZAIKEIRYOU_DAY, srSlipSlipkokeibunsokutei),
                "".equals(youzaikeiryouTime) ? "0000" : youzaikeiryouTime));                                                                                      // 溶剤秤量日時
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B033Const.YOUZAI1_ZAIRYOUHINMEI, srSlipSlipkokeibunsokutei)));                 // 溶剤①_材料品名
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B033Const.YOUZAI1_TYOUGOURYOUKIKAKU, srSlipSlipkokeibunsokutei)));             // 溶剤①_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B033Const.YOUZAI1_BUZAIZAIKOLOTNO1, srSlipSlipkokeibunsokutei)));                   // 溶剤①_部材在庫No1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B033Const.YOUZAI1_TYOUGOURYOU1, srSlipSlipkokeibunsokutei)));                          // 溶剤①_調合量1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B033Const.YOUZAI1_BUZAIZAIKOLOTNO2, srSlipSlipkokeibunsokutei)));                   // 溶剤①_部材在庫No2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B033Const.YOUZAI1_TYOUGOURYOU2, srSlipSlipkokeibunsokutei)));                          // 溶剤①_調合量2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B033Const.YOUZAI2_ZAIRYOUHINMEI, srSlipSlipkokeibunsokutei)));                 // 溶剤②_材料品名
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B033Const.YOUZAI2_TYOUGOURYOUKIKAKU, srSlipSlipkokeibunsokutei)));             // 溶剤②_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B033Const.YOUZAI2_BUZAIZAIKOLOTNO1, srSlipSlipkokeibunsokutei)));                   // 溶剤②_部材在庫No1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B033Const.YOUZAI2_TYOUGOURYOU1, srSlipSlipkokeibunsokutei)));                          // 溶剤②_調合量1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B033Const.YOUZAI2_BUZAIZAIKOLOTNO2, srSlipSlipkokeibunsokutei)));                   // 溶剤②_部材在庫No2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B033Const.YOUZAI2_TYOUGOURYOU2, srSlipSlipkokeibunsokutei)));                          // 溶剤②_調合量2
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B033Const.TANTOUSYA, srSlipSlipkokeibunsokutei)));                                  // 担当者
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B033Const.KAKUHAN_KAKUHANMODE, srSlipSlipkokeibunsokutei)));                   // 撹拌_撹拌ﾓｰﾄﾞ
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B033Const.KAKUHAN_KAITENHOUKOU, srSlipSlipkokeibunsokutei)));                  // 撹拌_回転方向
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B033Const.KAKUHAN_KAITENSUU, srSlipSlipkokeibunsokutei)));                     // 撹拌_回転数(rpm)
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B033Const.KAKUHAN_KAKUHANJIKAN, srSlipSlipkokeibunsokutei)));                  // 撹拌_撹拌時間
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B033Const.KAKUHAN_KAKUHANKAISI_DAY, srSlipSlipkokeibunsokutei),
                "".equals(kakuhankakuhankaisiTime) ? "0000" : kakuhankakuhankaisiTime));                                                                          // 撹拌_撹拌開始日時
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B033Const.KAISIDENRYUUTI, srSlipSlipkokeibunsokutei)));                         // 開始電流値(A)
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOUYOTEI_DAY, srSlipSlipkokeibunsokutei),
                "".equals(kkhkakuhansyuuryouyoteiTime) ? "0000" : kkhkakuhansyuuryouyoteiTime));                                                                  // 撹拌_撹拌終了予定日時
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOU_DAY, srSlipSlipkokeibunsokutei),
                "".equals(kkhkakuhansyuuryouTime) ? "0000" : kkhkakuhansyuuryouTime));                                                                            // 撹拌_撹拌終了日時
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B033Const.SYUURYOUDENRYUUTI, srSlipSlipkokeibunsokutei)));                      // 終了電流値(A)
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B033Const.KAKUHAN_KAKUHANTANTOUSYA, srSlipSlipkokeibunsokutei)));                   // 撹拌_撹拌担当者
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANMODE, srSlipSlipkokeibunsokutei)));        // 排出前撹拌_撹拌ﾓｰﾄﾞ
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAITENHOUKOU, srSlipSlipkokeibunsokutei)));       // 排出前撹拌_回転方向
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAITENSUU, srSlipSlipkokeibunsokutei)));          // 排出前撹拌_回転数(rpm)
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANJIKAN, srSlipSlipkokeibunsokutei)));       // 排出前撹拌_撹拌時間
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANKAISI_DAY, srSlipSlipkokeibunsokutei),
                "".equals(hmkkkkhkaisiTime) ? "0000" : hmkkkkhkaisiTime));                                                                                        // 排出前撹拌_撹拌開始日時
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOUYOTEI_DAY, srSlipSlipkokeibunsokutei),
                "".equals(hmkkkkhsyuuryouyoteiTime) ? "0000" : hmkkkkhsyuuryouyoteiTime));                                                                        // 排出前撹拌_撹拌終了予定日時
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOU_DAY, srSlipSlipkokeibunsokutei),
                "".equals(hmkkkkhsyuuryouTime) ? "0000" : hmkkkkhsyuuryouTime));                                                                                  // 排出前撹拌_撹拌終了日時
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANTANTOUSYA, srSlipSlipkokeibunsokutei)));        // 排出前撹拌_撹拌担当者
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B033Const.HAISYUTUKAISI_DAY, srSlipSlipkokeibunsokutei),
                "".equals(haisyutukaisiTime) ? "0000" : haisyutukaisiTime));                                                                                      // 排出開始日時
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B033Const.HAISYUTUSYUURYOU_DAY, srSlipSlipkokeibunsokutei),
                "".equals(haisyutusyuuryouTime) ? "0000" : haisyutusyuuryouTime));                                                                                // 排出終了日時
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B033Const.HAISYUTUTANTOUSYA, srSlipSlipkokeibunsokutei)));                          // 排出担当者
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B033Const.BIKOU1, srSlipSlipkokeibunsokutei)));                                     // 備考1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B033Const.BIKOU2, srSlipSlipkokeibunsokutei)));                                     // 備考2

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
     * ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定(sr_slip_slipkokeibunsokutei)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSrSlipSlipkokeibunsokutei(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sr_slip_slipkokeibunsokutei "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

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
     * [ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_slip_slipkokeibunsokutei "
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
     * @param fxhdd11RevInfo 品質DB登録実績データ
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
            // 品質DB登録実績データが取得出来ていない場合エラー
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
     * @param srSlipSlipkokeibunsokutei ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定データ
     * @return DB値
     */
    private String getSrSlipSlipkokeibunsokuteiItemData(String itemId, SrSlipSlipkokeibunsokutei srSlipSlipkokeibunsokutei) {
        switch (itemId) {
            // ｽﾘｯﾌﾟ品名
            case GXHDO102B033Const.SLIPHINMEI:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getSliphinmei());

            // ｽﾘｯﾌﾟLotNo
            case GXHDO102B033Const.SLIPLOTNO:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getSliplotno());

            // ﾛｯﾄ区分
            case GXHDO102B033Const.LOTKUBUN:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getLotkubun());

            // 原料記号
            case GXHDO102B033Const.GENRYOUKIGOU:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getGenryoukigou());

            // 秤量号機
            case GXHDO102B033Const.GOKI:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getGoki());

            // 脱脂皿の種類
            case GXHDO102B033Const.DASSIZARANOSYURUI:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getDassizaranosyurui());

            // ﾙﾂﾎﾞNo
            case GXHDO102B033Const.RUTUBONO:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getRutubono());

            // ﾙﾂﾎﾞ風袋重量
            case GXHDO102B033Const.RUTUBOFUUTAIJYUURYOU:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getRutubofuutaijyuuryou());

            // 乾燥前ｽﾘｯﾌﾟ重量
            case GXHDO102B033Const.KANSOUMAESLIPJYUURYOU:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getKansoumaeslipjyuuryou());

            // 乾燥機①
            case GXHDO102B033Const.KANSOUKI1:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getKansouki1());

            // 乾燥温度①
            case GXHDO102B033Const.KANSOUONDO1:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getKansouondo1());

            // 乾燥時間①
            case GXHDO102B033Const.KANSOUJIKAN1:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getKansoujikan1());

            // 乾燥開始日①
            case GXHDO102B033Const.KANSOUKAISI1_DAY:
                return DateUtil.formattedTimestamp(srSlipSlipkokeibunsokutei.getKansoukaisinichiji1(), "yyMMdd");

            // 乾燥開始時間①
            case GXHDO102B033Const.KANSOUKAISI1_TIME:
                return DateUtil.formattedTimestamp(srSlipSlipkokeibunsokutei.getKansoukaisinichiji1(), "HHmm");

            // 乾燥終了日①
            case GXHDO102B033Const.KANSOUSYUURYOU1_DAY:
                return DateUtil.formattedTimestamp(srSlipSlipkokeibunsokutei.getKansousyuuryounichiji1(), "yyMMdd");

            // 乾燥終了時間①
            case GXHDO102B033Const.KANSOUSYUURYOU1_TIME:
                return DateUtil.formattedTimestamp(srSlipSlipkokeibunsokutei.getKansousyuuryounichiji1(), "HHmm");

            // 乾燥機②
            case GXHDO102B033Const.KANSOUKI2:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getKansouki2());

            // 乾燥温度②
            case GXHDO102B033Const.KANSOUONDO2:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getKansouondo2());

            // 乾燥時間②
            case GXHDO102B033Const.KANSOUJIKAN2:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getKansoujikan2());

            // 乾燥開始日②
            case GXHDO102B033Const.KANSOUKAISI2_DAY:
                return DateUtil.formattedTimestamp(srSlipSlipkokeibunsokutei.getKansoukaisinichiji2(), "yyMMdd");

            // 乾燥開始時間②
            case GXHDO102B033Const.KANSOUKAISI2_TIME:
                return DateUtil.formattedTimestamp(srSlipSlipkokeibunsokutei.getKansoukaisinichiji2(), "HHmm");

            // 乾燥終了日②
            case GXHDO102B033Const.KANSOUSYUURYOU2_DAY:
                return DateUtil.formattedTimestamp(srSlipSlipkokeibunsokutei.getKansousyuuryounichiji2(), "yyMMdd");

            // 乾燥終了時間②
            case GXHDO102B033Const.KANSOUSYUURYOU2_TIME:
                return DateUtil.formattedTimestamp(srSlipSlipkokeibunsokutei.getKansousyuuryounichiji2(), "HHmm");

            // 乾燥後総重量
            case GXHDO102B033Const.KANSOUGOSOUJYUURYOU:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getKansougosoujyuuryou());

            // 乾燥後正味重量
            case GXHDO102B033Const.KANSOUGOSYOUMIJYUURYOU:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getKansougosyoumijyuuryou());

            // 固形分比率
            case GXHDO102B033Const.KOKEIBUNHIRITU:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getKokeibunhiritu());

            // 固形分測定担当者
            case GXHDO102B033Const.KOKEIBUNSOKUTEITANTOUSYA:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getKokeibunsokuteitantousya());

            // 溶剤調整量
            case GXHDO102B033Const.YOUZAITYOUSEIRYOU:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getYouzaityouseiryou());

            // ﾄﾙｴﾝ調整量
            case GXHDO102B033Const.TOLUENETYOUSEIRYOU:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getToluenetyouseiryou());

            // ｿﾙﾐｯｸｽ調整量
            case GXHDO102B033Const.SOLMIXTYOUSEIRYOU:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getSolmixtyouseiryou());

            // 溶剤秤量日
            case GXHDO102B033Const.YOUZAIKEIRYOU_DAY:
                return DateUtil.formattedTimestamp(srSlipSlipkokeibunsokutei.getYouzaikeiryounichiji(), "yyMMdd");

            // 溶剤秤量時間
            case GXHDO102B033Const.YOUZAIKEIRYOU_TIME:
                return DateUtil.formattedTimestamp(srSlipSlipkokeibunsokutei.getYouzaikeiryounichiji(), "HHmm");

            // 溶剤①_材料品名
            case GXHDO102B033Const.YOUZAI1_ZAIRYOUHINMEI:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getYouzai1_zairyouhinmei());

            // 溶剤①_調合量規格
            case GXHDO102B033Const.YOUZAI1_TYOUGOURYOUKIKAKU:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getYouzai1_tyougouryoukikaku());

            // 溶剤①_部材在庫No1
            case GXHDO102B033Const.YOUZAI1_BUZAIZAIKOLOTNO1:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getYouzai1_buzaizaikolotno1());

            // 溶剤①_調合量1
            case GXHDO102B033Const.YOUZAI1_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getYouzai1_tyougouryou1());

            // 溶剤①_部材在庫No2
            case GXHDO102B033Const.YOUZAI1_BUZAIZAIKOLOTNO2:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getYouzai1_buzaizaikolotno2());

            // 溶剤①_調合量2
            case GXHDO102B033Const.YOUZAI1_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getYouzai1_tyougouryou2());

            // 溶剤②_材料品名
            case GXHDO102B033Const.YOUZAI2_ZAIRYOUHINMEI:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getYouzai2_zairyouhinmei());

            // 溶剤②_調合量規格
            case GXHDO102B033Const.YOUZAI2_TYOUGOURYOUKIKAKU:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getYouzai2_tyougouryoukikaku());

            // 溶剤②_部材在庫No1
            case GXHDO102B033Const.YOUZAI2_BUZAIZAIKOLOTNO1:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getYouzai2_buzaizaikolotno1());

            // 溶剤②_調合量1
            case GXHDO102B033Const.YOUZAI2_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getYouzai2_tyougouryou1());

            // 溶剤②_部材在庫No2
            case GXHDO102B033Const.YOUZAI2_BUZAIZAIKOLOTNO2:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getYouzai2_buzaizaikolotno2());

            // 溶剤②_調合量2
            case GXHDO102B033Const.YOUZAI2_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getYouzai2_tyougouryou2());

            // 担当者
            case GXHDO102B033Const.TANTOUSYA:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getTantousya());

            // 撹拌_撹拌ﾓｰﾄﾞ
            case GXHDO102B033Const.KAKUHAN_KAKUHANMODE:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getKakuhan_kakuhanmode());

            // 撹拌_回転方向
            case GXHDO102B033Const.KAKUHAN_KAITENHOUKOU:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getKakuhan_kaitenhoukou());

            // 撹拌_回転数(rpm)
            case GXHDO102B033Const.KAKUHAN_KAITENSUU:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getKakuhan_kaitensuu());

            // 撹拌_撹拌時間
            case GXHDO102B033Const.KAKUHAN_KAKUHANJIKAN:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getKakuhan_kakuhanjikan());

            // 撹拌_撹拌開始日
            case GXHDO102B033Const.KAKUHAN_KAKUHANKAISI_DAY:
                return DateUtil.formattedTimestamp(srSlipSlipkokeibunsokutei.getKakuhan_kakuhankaisinichiji(), "yyMMdd");

            // 撹拌_撹拌開始時間
            case GXHDO102B033Const.KAKUHAN_KAKUHANKAISI_TIME:
                return DateUtil.formattedTimestamp(srSlipSlipkokeibunsokutei.getKakuhan_kakuhankaisinichiji(), "HHmm");

            // 開始電流値(A)
            case GXHDO102B033Const.KAISIDENRYUUTI:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getKaisidenryuuti());

            // 撹拌_撹拌終了予定日
            case GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOUYOTEI_DAY:
                return DateUtil.formattedTimestamp(srSlipSlipkokeibunsokutei.getKakuhan_kakuhansyuuryouyoteinichiji(), "yyMMdd");

            // 撹拌_撹拌終了予定時間
            case GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOUYOTEI_TIME:
                return DateUtil.formattedTimestamp(srSlipSlipkokeibunsokutei.getKakuhan_kakuhansyuuryouyoteinichiji(), "HHmm");

            // 撹拌_撹拌終了日
            case GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOU_DAY:
                return DateUtil.formattedTimestamp(srSlipSlipkokeibunsokutei.getKakuhan_kakuhansyuuryounichiji(), "yyMMdd");

            // 撹拌_撹拌終了時間
            case GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOU_TIME:
                return DateUtil.formattedTimestamp(srSlipSlipkokeibunsokutei.getKakuhan_kakuhansyuuryounichiji(), "HHmm");

            // 終了電流値(A)
            case GXHDO102B033Const.SYUURYOUDENRYUUTI:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getSyuuryoudenryuuti());

            // 撹拌_撹拌担当者
            case GXHDO102B033Const.KAKUHAN_KAKUHANTANTOUSYA:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getKakuhan_kakuhantantousya());

            // 排出前撹拌_撹拌ﾓｰﾄﾞ
            case GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANMODE:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getHaisyutumaekakuhan_kakuhanmode());

            // 排出前撹拌_回転方向
            case GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAITENHOUKOU:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getHaisyutumaekakuhan_kaitenhoukou());

            // 排出前撹拌_回転数(rpm)
            case GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAITENSUU:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getHaisyutumaekakuhan_kaitensuu());

            // 排出前撹拌_撹拌時間
            case GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANJIKAN:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getHaisyutumaekakuhan_kakuhanjikan());

            // 排出前撹拌_撹拌開始日
            case GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANKAISI_DAY:
                return DateUtil.formattedTimestamp(srSlipSlipkokeibunsokutei.getHaisyutumaekakuhan_kakuhankaisinichiji(), "yyMMdd");

            // 排出前撹拌_撹拌開始時間
            case GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANKAISI_TIME:
                return DateUtil.formattedTimestamp(srSlipSlipkokeibunsokutei.getHaisyutumaekakuhan_kakuhankaisinichiji(), "HHmm");

            // 排出前撹拌_撹拌終了予定日
            case GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOUYOTEI_DAY:
                return DateUtil.formattedTimestamp(srSlipSlipkokeibunsokutei.getHaisyutumaekakuhan_kakuhansyuuryouyoteinichiji(), "yyMMdd");

            // 排出前撹拌_撹拌終了予定時間
            case GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOUYOTEI_TIME:
                return DateUtil.formattedTimestamp(srSlipSlipkokeibunsokutei.getHaisyutumaekakuhan_kakuhansyuuryouyoteinichiji(), "HHmm");

            // 排出前撹拌_撹拌終了日
            case GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOU_DAY:
                return DateUtil.formattedTimestamp(srSlipSlipkokeibunsokutei.getHaisyutumaekakuhan_kakuhansyuuryounichiji(), "yyMMdd");

            // 排出前撹拌_撹拌終了時間
            case GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOU_TIME:
                return DateUtil.formattedTimestamp(srSlipSlipkokeibunsokutei.getHaisyutumaekakuhan_kakuhansyuuryounichiji(), "HHmm");

            // 排出前撹拌_撹拌担当者
            case GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANTANTOUSYA:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getHaisyutumaekakuhan_kakuhantantousya());

            // 排出開始日
            case GXHDO102B033Const.HAISYUTUKAISI_DAY:
                return DateUtil.formattedTimestamp(srSlipSlipkokeibunsokutei.getHaisyutukaisinichiji(), "yyMMdd");

            // 排出開始時間
            case GXHDO102B033Const.HAISYUTUKAISI_TIME:
                return DateUtil.formattedTimestamp(srSlipSlipkokeibunsokutei.getHaisyutukaisinichiji(), "HHmm");

            // 排出終了日
            case GXHDO102B033Const.HAISYUTUSYUURYOU_DAY:
                return DateUtil.formattedTimestamp(srSlipSlipkokeibunsokutei.getHaisyutusyuuryounichiji(), "yyMMdd");

            // 排出終了時間
            case GXHDO102B033Const.HAISYUTUSYUURYOU_TIME:
                return DateUtil.formattedTimestamp(srSlipSlipkokeibunsokutei.getHaisyutusyuuryounichiji(), "HHmm");

            // 排出担当者
            case GXHDO102B033Const.HAISYUTUTANTOUSYA:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getHaisyututantousya());

            // 備考1
            case GXHDO102B033Const.BIKOU1:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getBikou1());

            // 備考2
            case GXHDO102B033Const.BIKOU2:
                return StringUtil.nullToBlank(srSlipSlipkokeibunsokutei.getBikou2());

            default:
                return null;
        }
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定_仮登録(tmp_sr_slip_slipkokeibunsokutei)登録処理(削除時)
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
    private void insertDeleteDataTmpSrSlipSlipkokeibunsokutei(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_slip_slipkokeibunsokutei ("
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,goki,dassizaranosyurui,rutubono,rutubofuutaijyuuryou,"
                + "kansoumaeslipjyuuryou,kansouki1,kansouondo1,kansoujikan1,kansoukaisinichiji1,kansousyuuryounichiji1,kansouki2,kansouondo2,"
                + "kansoujikan2,kansoukaisinichiji2,kansousyuuryounichiji2,kansougosoujyuuryou,kansougosyoumijyuuryou,kokeibunhiritu,"
                + "kokeibunsokuteitantousya,youzaityouseiryou,toluenetyouseiryou,solmixtyouseiryou,youzaikeiryounichiji,youzai1_zairyouhinmei,"
                + "youzai1_tyougouryoukikaku,youzai1_buzaizaikolotno1,youzai1_tyougouryou1,youzai1_buzaizaikolotno2,youzai1_tyougouryou2,youzai2_zairyouhinmei,"
                + "youzai2_tyougouryoukikaku,youzai2_buzaizaikolotno1,youzai2_tyougouryou1,youzai2_buzaizaikolotno2,youzai2_tyougouryou2,tantousya,"
                + "kakuhan_kakuhanmode,kakuhan_kaitenhoukou,kakuhan_kaitensuu,kakuhan_kakuhanjikan,kakuhan_kakuhankaisinichiji,kaisidenryuuti,"
                + "kakuhan_kakuhansyuuryouyoteinichiji,kakuhan_kakuhansyuuryounichiji,syuuryoudenryuuti,kakuhan_kakuhantantousya,haisyutumaekakuhan_kakuhanmode,"
                + "haisyutumaekakuhan_kaitenhoukou,haisyutumaekakuhan_kaitensuu,haisyutumaekakuhan_kakuhanjikan,haisyutumaekakuhan_kakuhankaisinichiji,"
                + "haisyutumaekakuhan_kakuhansyuuryouyoteinichiji,haisyutumaekakuhan_kakuhansyuuryounichiji,haisyutumaekakuhan_kakuhantantousya,haisyutukaisinichiji,"
                + "haisyutusyuuryounichiji,haisyututantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") SELECT "
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,goki,dassizaranosyurui,rutubono,rutubofuutaijyuuryou,"
                + "kansoumaeslipjyuuryou,kansouki1,kansouondo1,kansoujikan1,kansoukaisinichiji1,kansousyuuryounichiji1,kansouki2,kansouondo2,"
                + "kansoujikan2,kansoukaisinichiji2,kansousyuuryounichiji2,kansougosoujyuuryou,kansougosyoumijyuuryou,kokeibunhiritu,"
                + "kokeibunsokuteitantousya,youzaityouseiryou,toluenetyouseiryou,solmixtyouseiryou,youzaikeiryounichiji,youzai1_zairyouhinmei,"
                + "youzai1_tyougouryoukikaku,youzai1_buzaizaikolotno1,youzai1_tyougouryou1,youzai1_buzaizaikolotno2,youzai1_tyougouryou2,youzai2_zairyouhinmei,"
                + "youzai2_tyougouryoukikaku,youzai2_buzaizaikolotno1,youzai2_tyougouryou1,youzai2_buzaizaikolotno2,youzai2_tyougouryou2,tantousya,"
                + "kakuhan_kakuhanmode,kakuhan_kaitenhoukou,kakuhan_kaitensuu,kakuhan_kakuhanjikan,kakuhan_kakuhankaisinichiji,kaisidenryuuti,"
                + "kakuhan_kakuhansyuuryouyoteinichiji,kakuhan_kakuhansyuuryounichiji,syuuryoudenryuuti,kakuhan_kakuhantantousya,haisyutumaekakuhan_kakuhanmode,"
                + "haisyutumaekakuhan_kaitenhoukou,haisyutumaekakuhan_kaitensuu,haisyutumaekakuhan_kakuhanjikan,haisyutumaekakuhan_kakuhankaisinichiji,"
                + "haisyutumaekakuhan_kakuhansyuuryouyoteinichiji,haisyutumaekakuhan_kakuhansyuuryounichiji,haisyutumaekakuhan_kakuhantantousya,haisyutukaisinichiji,"
                + "haisyutusyuuryounichiji,haisyututantousya,bikou1,bikou2,?,?,?,? "
                + " FROM sr_slip_slipkokeibunsokutei "
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
     * 画面データ設定処理
     *
     * @param processData 処理制御データ
     */
    private void initGXHDO102B033A(ProcessData processData) {
        GXHDO102B033A bean = (GXHDO102B033A) getFormBean("gXHDO102B033A");
        bean.setWiplotno(getItemRow(processData.getItemList(), GXHDO102B033Const.WIPLOTNO));
        bean.setSliphinmei(getItemRow(processData.getItemList(), GXHDO102B033Const.SLIPHINMEI));
        bean.setSliplotno(getItemRow(processData.getItemList(), GXHDO102B033Const.SLIPLOTNO));
        bean.setLotkubun(getItemRow(processData.getItemList(), GXHDO102B033Const.LOTKUBUN));
        bean.setGenryoukigou(getItemRow(processData.getItemList(), GXHDO102B033Const.GENRYOUKIGOU));
        bean.setGoki(getItemRow(processData.getItemList(), GXHDO102B033Const.GOKI));
        bean.setDassizaranosyurui(getItemRow(processData.getItemList(), GXHDO102B033Const.DASSIZARANOSYURUI));
        bean.setRutubono(getItemRow(processData.getItemList(), GXHDO102B033Const.RUTUBONO));
        bean.setRutubofuutaijyuuryou(getItemRow(processData.getItemList(), GXHDO102B033Const.RUTUBOFUUTAIJYUURYOU));
        bean.setKansoumaeslipjyuuryou(getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUMAESLIPJYUURYOU));
        bean.setKansouki1(getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUKI1));
        bean.setKansouondo1(getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUONDO1));
        bean.setKansoujikan1(getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUJIKAN1));
        bean.setKansoukaisi1_day(getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUKAISI1_DAY));
        bean.setKansoukaisi1_time(getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUKAISI1_TIME));
        bean.setKansousyuuryou1_day(getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUSYUURYOU1_DAY));
        bean.setKansousyuuryou1_time(getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUSYUURYOU1_TIME));
        bean.setKansouki2(getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUKI2));
        bean.setKansouondo2(getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUONDO2));
        bean.setKansoujikan2(getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUJIKAN2));
        bean.setKansoukaisi2_day(getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUKAISI2_DAY));
        bean.setKansoukaisi2_time(getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUKAISI2_TIME));
        bean.setKansousyuuryou2_day(getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUSYUURYOU2_DAY));
        bean.setKansousyuuryou2_time(getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUSYUURYOU2_TIME));
        bean.setKansougosoujyuuryou(getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUGOSOUJYUURYOU));
        bean.setKansougosyoumijyuuryou(getItemRow(processData.getItemList(), GXHDO102B033Const.KANSOUGOSYOUMIJYUURYOU));
        bean.setKokeibunhiritu(getItemRow(processData.getItemList(), GXHDO102B033Const.KOKEIBUNHIRITU));
        bean.setKokeibunsokuteitantousya(getItemRow(processData.getItemList(), GXHDO102B033Const.KOKEIBUNSOKUTEITANTOUSYA));
        bean.setYouzaityouseiryou(getItemRow(processData.getItemList(), GXHDO102B033Const.YOUZAITYOUSEIRYOU));
        bean.setToluenetyouseiryou(getItemRow(processData.getItemList(), GXHDO102B033Const.TOLUENETYOUSEIRYOU));
        bean.setSolmixtyouseiryou(getItemRow(processData.getItemList(), GXHDO102B033Const.SOLMIXTYOUSEIRYOU));
        bean.setYouzaikeiryou_day(getItemRow(processData.getItemList(), GXHDO102B033Const.YOUZAIKEIRYOU_DAY));
        bean.setYouzaikeiryou_time(getItemRow(processData.getItemList(), GXHDO102B033Const.YOUZAIKEIRYOU_TIME));
        bean.setYouzai1_zairyouhinmei(getItemRow(processData.getItemList(), GXHDO102B033Const.YOUZAI1_ZAIRYOUHINMEI));
        bean.setYouzai1_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B033Const.YOUZAI1_TYOUGOURYOUKIKAKU));
        bean.setYouzai1_buzaizaikolotno1(getItemRow(processData.getItemList(), GXHDO102B033Const.YOUZAI1_BUZAIZAIKOLOTNO1));
        bean.setYouzai1_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B033Const.YOUZAI1_TYOUGOURYOU1));
        bean.setYouzai1_buzaizaikolotno2(getItemRow(processData.getItemList(), GXHDO102B033Const.YOUZAI1_BUZAIZAIKOLOTNO2));
        bean.setYouzai1_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B033Const.YOUZAI1_TYOUGOURYOU2));
        bean.setYouzai2_zairyouhinmei(getItemRow(processData.getItemList(), GXHDO102B033Const.YOUZAI2_ZAIRYOUHINMEI));
        bean.setYouzai2_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B033Const.YOUZAI2_TYOUGOURYOUKIKAKU));
        bean.setYouzai2_buzaizaikolotno1(getItemRow(processData.getItemList(), GXHDO102B033Const.YOUZAI2_BUZAIZAIKOLOTNO1));
        bean.setYouzai2_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B033Const.YOUZAI2_TYOUGOURYOU1));
        bean.setYouzai2_buzaizaikolotno2(getItemRow(processData.getItemList(), GXHDO102B033Const.YOUZAI2_BUZAIZAIKOLOTNO2));
        bean.setYouzai2_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B033Const.YOUZAI2_TYOUGOURYOU2));
        bean.setTantousya(getItemRow(processData.getItemList(), GXHDO102B033Const.TANTOUSYA));
        bean.setKakuhan_kakuhanmode(getItemRow(processData.getItemList(), GXHDO102B033Const.KAKUHAN_KAKUHANMODE));
        bean.setKakuhan_kaitenhoukou(getItemRow(processData.getItemList(), GXHDO102B033Const.KAKUHAN_KAITENHOUKOU));
        bean.setKakuhan_kaitensuu(getItemRow(processData.getItemList(), GXHDO102B033Const.KAKUHAN_KAITENSUU));
        bean.setKakuhan_kakuhanjikan(getItemRow(processData.getItemList(), GXHDO102B033Const.KAKUHAN_KAKUHANJIKAN));
        bean.setKakuhan_kakuhankaisi_day(getItemRow(processData.getItemList(), GXHDO102B033Const.KAKUHAN_KAKUHANKAISI_DAY));
        bean.setKakuhan_kakuhankaisi_time(getItemRow(processData.getItemList(), GXHDO102B033Const.KAKUHAN_KAKUHANKAISI_TIME));
        bean.setKaisidenryuuti(getItemRow(processData.getItemList(), GXHDO102B033Const.KAISIDENRYUUTI));
        bean.setKakuhan_kakuhansyuuryouyotei_day(getItemRow(processData.getItemList(), GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOUYOTEI_DAY));
        bean.setKakuhan_kakuhansyuuryouyotei_time(getItemRow(processData.getItemList(), GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOUYOTEI_TIME));
        bean.setKakuhan_kakuhansyuuryou_day(getItemRow(processData.getItemList(), GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOU_DAY));
        bean.setKakuhan_kakuhansyuuryou_time(getItemRow(processData.getItemList(), GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOU_TIME));
        bean.setSyuuryoudenryuuti(getItemRow(processData.getItemList(), GXHDO102B033Const.SYUURYOUDENRYUUTI));
        bean.setKakuhan_kakuhantantousya(getItemRow(processData.getItemList(), GXHDO102B033Const.KAKUHAN_KAKUHANTANTOUSYA));
        bean.setHaisyutumaekakuhan_kakuhanmode(getItemRow(processData.getItemList(), GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANMODE));
        bean.setHaisyutumaekakuhan_kaitenhoukou(getItemRow(processData.getItemList(), GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAITENHOUKOU));
        bean.setHaisyutumaekakuhan_kaitensuu(getItemRow(processData.getItemList(), GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAITENSUU));
        bean.setHaisyutumaekakuhan_kakuhanjikan(getItemRow(processData.getItemList(), GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANJIKAN));
        bean.setHaisyutumaekakuhan_kakuhankaisi_day(getItemRow(processData.getItemList(), GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANKAISI_DAY));
        bean.setHaisyutumaekakuhan_kakuhankaisi_time(getItemRow(processData.getItemList(), GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANKAISI_TIME));
        bean.setHaisyutumaekakuhan_kakuhansyuuryouyotei_day(getItemRow(processData.getItemList(), GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOUYOTEI_DAY));
        bean.setHaisyutumaekakuhan_kakuhansyuuryouyotei_time(getItemRow(processData.getItemList(), GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOUYOTEI_TIME));
        bean.setHaisyutumaekakuhan_kakuhansyuuryou_day(getItemRow(processData.getItemList(), GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOU_DAY));
        bean.setHaisyutumaekakuhan_kakuhansyuuryou_time(getItemRow(processData.getItemList(), GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOU_TIME));
        bean.setHaisyutumaekakuhan_kakuhantantousya(getItemRow(processData.getItemList(), GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANTANTOUSYA));
        bean.setHaisyutukaisi_day(getItemRow(processData.getItemList(), GXHDO102B033Const.HAISYUTUKAISI_DAY));
        bean.setHaisyutukaisi_time(getItemRow(processData.getItemList(), GXHDO102B033Const.HAISYUTUKAISI_TIME));
        bean.setHaisyutusyuuryou_day(getItemRow(processData.getItemList(), GXHDO102B033Const.HAISYUTUSYUURYOU_DAY));
        bean.setHaisyutusyuuryou_time(getItemRow(processData.getItemList(), GXHDO102B033Const.HAISYUTUSYUURYOU_TIME));
        bean.setHaisyututantousya(getItemRow(processData.getItemList(), GXHDO102B033Const.HAISYUTUTANTOUSYA));
        bean.setBikou1(getItemRow(processData.getItemList(), GXHDO102B033Const.BIKOU1));
        bean.setBikou2(getItemRow(processData.getItemList(), GXHDO102B033Const.BIKOU2));

    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定入力_ｻﾌﾞ画面データの規格値取得処理
     *
     * @param item 項目情報
     * @return 項目値
     */
    private String getFXHDD01KikakuChi(FXHDD01 item) {
        if (item == null) {
            return "";
        }
        return StringUtil.nullToBlank(item.getKikakuChi()).replace("【", "").replace("】", "");
    }

    /**
     * 溶剤1_材料品名のﾘﾝｸ押下時、 ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC016SubGamen1(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B033Const.YOUZAI1_BUZAIZAIKOLOTNO1, GXHDO102B033Const.YOUZAI1_TYOUGOURYOU1,
                GXHDO102B033Const.YOUZAI1_BUZAIZAIKOLOTNO2, GXHDO102B033Const.YOUZAI1_TYOUGOURYOU2);
        return openC016SubGamen(processData, 1, returnItemIdList, GXHDO102B033Const.YOUZAI1_TYOUGOURYOUKIKAKU);
    }

    /**
     * 溶剤2_材料品名のﾘﾝｸ押下時、 ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC016SubGamen2(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B033Const.YOUZAI2_BUZAIZAIKOLOTNO1, GXHDO102B033Const.YOUZAI2_TYOUGOURYOU1,
                GXHDO102B033Const.YOUZAI2_BUZAIZAIKOLOTNO2, GXHDO102B033Const.YOUZAI2_TYOUGOURYOU2);
        return openC016SubGamen(processData, 2, returnItemIdList, GXHDO102B033Const.YOUZAI2_TYOUGOURYOUKIKAKU);
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @param zairyokubun 材料区分
     * @param returnItemIdList サブ画面から戻ったときに値を設定必要項目リスト
     * @param itemTyougouryoukikaku 調合量規格
     * @return 処理制御データ
     */
    public ProcessData openC016SubGamen(ProcessData processData, int zairyokubun, List<String> returnItemIdList,String itemTyougouryoukikaku) {
        try {
            // 「秤量号機」
            FXHDD01 itemGoki = getItemRow(processData.getItemList(), GXHDO102B033Const.GOKI);
            // 「秤量号機」ﾁｪｯｸ処理
            ErrorMessageInfo checkItemErrorInfo = checkGoki(itemGoki);
            if (checkItemErrorInfo != null) {
                processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
                // エラーの場合はコールバック変数に"error0"をセット
                RequestContext context = RequestContext.getCurrentInstance();
                context.addCallbackParam("firstParam", "error0");
                return processData;
            }
            // 「調合量規格」
            FXHDD01 itemTyogouryoukikaku = getItemRow(processData.getItemList(), itemTyougouryoukikaku);
            // 「調合量規格」ﾁｪｯｸ処理
            ErrorMessageInfo checkItemTyogouryoukikaku1ErrorInfo = checkTyogouryoukikaku(itemTyogouryoukikaku);
            if (checkItemTyogouryoukikaku1ErrorInfo != null) {
                processData.setErrorMessageInfoList(Arrays.asList(checkItemTyogouryoukikaku1ErrorInfo));
                return processData;
            }
            processData.setMethod("");
            // コールバックパラメータにてサブ画面起動用の値を設定
            processData.setCollBackParam("gxhdo102c016");

            GXHDO102C016 beanGXHDO102C016 = (GXHDO102C016) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C016);
            GXHDO102C016Model gxhdo102c016model = beanGXHDO102C016.getGxhdO102c016Model();
            // 主画面からサブ画面に渡されたデータを設定
            setSubGamenInitData(gxhdo102c016model, zairyokubun, itemGoki, returnItemIdList);

            beanGXHDO102C016.setGxhdO102c016ModelView(gxhdo102c016model.clone());
        } catch (CloneNotSupportedException ex) {
            ErrUtil.outputErrorLog("CloneNotSupportedException発生", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            return processData;
        }

        return processData;
    }

    /**
     * 【材料品名】ﾘﾝｸ押下時、サブ画面Open時ﾁｪｯｸ処理
     *
     * @param itemTyogouryoukikaku 調合量規格
     * @return エラーメッセージ情報
     */ 
    private ErrorMessageInfo checkTyogouryoukikaku(FXHDD01 itemTyogouryoukikaku) {
        boolean checkResult = false;
        // 「調合量規格」ﾁｪｯｸ
        if(StringUtil.isEmpty(itemTyogouryoukikaku.getKikakuChi())){
            //「調合量規格1」の規格値が取得できない場合
            checkResult = true;          
        }else{
            if (StringUtil.isEmpty(itemTyogouryoukikaku.getKikakuChi().replace("【", "").replace("】", ""))) {
                //「調合量規格1」の規格値が取得できて、値がない場合
                checkResult = true;
            }
        }
        if(checkResult){
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemTyogouryoukikaku);
            return MessageUtil.getErrorMessageInfo("XHD-000019", true, true, errFxhdd01List, itemTyogouryoukikaku.getLabel1());
        }

        return null;
    }

    /**
     * 秤量号機の必須入力ﾁｪｯｸ処理
     *
     * @param itemGoki 秤量号機
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkGoki(FXHDD01 itemGoki) {
        // 「秤量号機」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemGoki.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemGoki);
            return MessageUtil.getErrorMessageInfo("XHD-000003", true, true, errFxhdd01List, itemGoki.getLabel1());
        }

        return null;
    }

    /**
     * 主画面からサブ画面に渡されたデータを設定
     *
     * @param gxhdo102c016model モデルデータ
     * @param zairyokubun 材料区分
     * @param itemGoki 秤量号機データ
     * @param returnItemIdList サブ画面から戻るデータリスト
     * @throws CloneNotSupportedException 例外エラー
     */
    private void setSubGamenInitData(GXHDO102C016Model gxhdo102c016model, int zairyokubun, FXHDD01 itemGoki, List<String> returnItemIdList) throws CloneNotSupportedException {
        GXHDO102C016Model.SubGamenData c016subgamendata = GXHDO102C016Logic.getC016subgamendata(gxhdo102c016model, zairyokubun);
        if (c016subgamendata == null) {
            return;
        }
        c016subgamendata.setSubDataGoki(StringUtil.nullToBlank(itemGoki.getValue()));
        c016subgamendata.setSubDataZairyokubun(zairyokubun);
        // サブ画面から戻ったときに値を設定する項目を指定する。
        c016subgamendata.setReturnItemIdBuzailotno1(returnItemIdList.get(0)); // 部材在庫No.X_1
        c016subgamendata.setReturnItemIdTyougouryou1(returnItemIdList.get(1)); // 調合量X_1
        c016subgamendata.setReturnItemIdBuzailotno2(returnItemIdList.get(2)); // 部材在庫NoX_2
        c016subgamendata.setReturnItemIdTyougouryou2(returnItemIdList.get(3)); // 調合量X_2
        gxhdo102c016model.setShowsubgamendata(c016subgamendata.clone());
        // サブ画面の調合残量の計算
        GXHDO102C016Logic.calcTyogouzanryou(gxhdo102c016model);
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定入力_ｻﾌﾞ画面データ設定処理
     *
     * @param processData 処理制御データ
     * @param subSrSlipSlipkokeibunsokuteiList ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定入力_ｻﾌﾞ画面データリスト
     */
    private void setInputItemDataSubFormC016(ProcessData processData, List<SubSrSlipSlipkokeibunsokutei> subSrSlipSlipkokeibunsokuteiList) {
        // サブ画面の情報を取得
        GXHDO102C016 beanGXHDO102C016 = (GXHDO102C016) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C016);

        GXHDO102C016Model model;
        if (subSrSlipSlipkokeibunsokuteiList == null) {
            // 登録データが無い場合、主画面の材料品名1-2と調合量規格1-2はｻﾌﾞ画面の初期値にセットする。
            subSrSlipSlipkokeibunsokuteiList = new ArrayList<>();
            SubSrSlipSlipkokeibunsokutei subgamen1 = new SubSrSlipSlipkokeibunsokutei();
            SubSrSlipSlipkokeibunsokutei subgamen2 = new SubSrSlipSlipkokeibunsokutei();

            subgamen1.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B033Const.YOUZAI1_ZAIRYOUHINMEI))); // 溶剤1_材料品名
            subgamen1.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B033Const.YOUZAI1_TYOUGOURYOUKIKAKU))); // 溶剤1_調合量規格
            subgamen1.setStandardpattern(getItemRow(processData.getItemList(), GXHDO102B033Const.YOUZAI1_TYOUGOURYOUKIKAKU).getStandardPattern()); // 溶剤1_調合量規格情報ﾊﾟﾀｰﾝ
            subgamen2.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B033Const.YOUZAI2_ZAIRYOUHINMEI))); // 溶剤2_材料品名
            subgamen2.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B033Const.YOUZAI2_TYOUGOURYOUKIKAKU))); // 溶剤2_調合量規格
            subgamen2.setStandardpattern(getItemRow(processData.getItemList(), GXHDO102B033Const.YOUZAI2_TYOUGOURYOUKIKAKU).getStandardPattern()); // 溶剤2_調合量規格情報ﾊﾟﾀｰﾝ
            subSrSlipSlipkokeibunsokuteiList.add(subgamen1);
            subSrSlipSlipkokeibunsokuteiList.add(subgamen2);
            model = GXHDO102C016Logic.createGXHDO102C016Model(subSrSlipSlipkokeibunsokuteiList);

        } else {
            // 登録データがあれば登録データをセットする。
            model = GXHDO102C016Logic.createGXHDO102C016Model(subSrSlipSlipkokeibunsokuteiList);
        }
        beanGXHDO102C016.setGxhdO102c016Model(model);
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定入力_ｻﾌﾞ画面の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定入力_ｻﾌﾞ画面登録データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrSlipSlipkokeibunsokutei> getSubSrSlipSlipkokeibunsokuteiData(QueryRunner queryRunnerQcdb,
            String rev, String jotaiFlg, String kojyo, String lotNo, String edaban) throws SQLException {
        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSubSrSlipSlipkokeibunsokutei(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSubSrSlipSlipkokeibunsokutei(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        }
    }

    /**
     * [ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定入力_ｻﾌﾞ画面]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrSlipSlipkokeibunsokutei> loadSubSrSlipSlipkokeibunsokutei(QueryRunner queryRunnerQcdb,
            String kojyo, String lotNo, String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + "kojyo,lotno,edaban,zairyokubun,tyogouryoukikaku,tyogouzanryou,zairyohinmei,"
                + "buzailotno1,buzaihinmei1,tyougouryou1_1,tyougouryou1_2,tyougouryou1_3,tyougouryou1_4,"
                + "tyougouryou1_5,tyougouryou1_6,buzailotno2,buzaihinmei2,tyougouryou2_1,tyougouryou2_2,"
                + "tyougouryou2_3,tyougouryou2_4,tyougouryou2_5,tyougouryou2_6,torokunichiji,kosinnichiji,"
                + "revision, '0' AS deleteflag "
                + " FROM sub_sr_slip_slipkokeibunsokutei "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? ";

        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            sql += "AND revision = ? ";
        }
        sql += " order by zairyokubun ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);

        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            params.add(rev);
        }

        Map<String, String> mapping = new HashMap<>();
        mapping.put("kojyo", "kojyo");                           // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                           // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                         // 枝番
        mapping.put("zairyokubun", "zairyokubun");               // 材料区分
        mapping.put("tyogouryoukikaku", "tyogouryoukikaku");     // 調合量規格
        mapping.put("tyogouzanryou", "tyogouzanryou");           // 調合残量
        mapping.put("zairyohinmei", "zairyohinmei");             // 材料品名
        mapping.put("buzailotno1", "buzailotno1");               // 部材在庫No1
        mapping.put("buzaihinmei1", "buzaihinmei1");             // 部材在庫品名1
        mapping.put("tyougouryou1_1", "tyougouryou1_1");         // 調合量1_1
        mapping.put("tyougouryou1_2", "tyougouryou1_2");         // 調合量1_2
        mapping.put("tyougouryou1_3", "tyougouryou1_3");         // 調合量1_3
        mapping.put("tyougouryou1_4", "tyougouryou1_4");         // 調合量1_4
        mapping.put("tyougouryou1_5", "tyougouryou1_5");         // 調合量1_5
        mapping.put("tyougouryou1_6", "tyougouryou1_6");         // 調合量1_6
        mapping.put("buzailotno2", "buzailotno2");               // 部材在庫No1
        mapping.put("buzaihinmei2", "buzaihinmei2");             // 部材在庫品名1
        mapping.put("tyougouryou2_1", "tyougouryou2_1");         // 調合量1_1
        mapping.put("tyougouryou2_2", "tyougouryou2_2");         // 調合量1_2
        mapping.put("tyougouryou2_3", "tyougouryou2_3");         // 調合量1_3
        mapping.put("tyougouryou2_4", "tyougouryou2_4");         // 調合量1_4
        mapping.put("tyougouryou2_5", "tyougouryou2_5");         // 調合量1_5
        mapping.put("tyougouryou2_6", "tyougouryou2_6");         // 調合量1_6
        mapping.put("torokunichiji", "torokunichiji");           // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");             // 更新日時
        mapping.put("revision", "revision");                     // revision
        mapping.put("deleteflag", "deleteflag");                 // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SubSrSlipSlipkokeibunsokutei>> beanHandler = new BeanListHandler<>(SubSrSlipSlipkokeibunsokutei.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定入力_ｻﾌﾞ画面_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrSlipSlipkokeibunsokutei> loadTmpSubSrSlipSlipkokeibunsokutei(QueryRunner queryRunnerQcdb,
            String kojyo, String lotNo, String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + "kojyo,lotno,edaban,zairyokubun,tyogouryoukikaku,tyogouzanryou,zairyohinmei,"
                + "buzailotno1,buzaihinmei1,tyougouryou1_1,tyougouryou1_2,tyougouryou1_3,tyougouryou1_4,"
                + "tyougouryou1_5,tyougouryou1_6,buzailotno2,buzaihinmei2,tyougouryou2_1,tyougouryou2_2,"
                + "tyougouryou2_3,tyougouryou2_4,tyougouryou2_5,tyougouryou2_6,torokunichiji,kosinnichiji,"
                + "revision, deleteflag "
                + " FROM tmp_sub_sr_slip_slipkokeibunsokutei "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND deleteflag = ? ";

        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            sql += "AND revision = ? ";
        }
        sql += " order by zairyokubun ";

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
        mapping.put("kojyo", "kojyo");                           // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                           // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                         // 枝番
        mapping.put("zairyokubun", "zairyokubun");               // 材料区分
        mapping.put("tyogouryoukikaku", "tyogouryoukikaku");     // 調合量規格
        mapping.put("tyogouzanryou", "tyogouzanryou");           // 調合残量
        mapping.put("zairyohinmei", "zairyohinmei");             // 材料品名
        mapping.put("buzailotno1", "buzailotno1");               // 部材在庫No1
        mapping.put("buzaihinmei1", "buzaihinmei1");             // 部材在庫品名1
        mapping.put("tyougouryou1_1", "tyougouryou1_1");         // 調合量1_1
        mapping.put("tyougouryou1_2", "tyougouryou1_2");         // 調合量1_2
        mapping.put("tyougouryou1_3", "tyougouryou1_3");         // 調合量1_3
        mapping.put("tyougouryou1_4", "tyougouryou1_4");         // 調合量1_4
        mapping.put("tyougouryou1_5", "tyougouryou1_5");         // 調合量1_5
        mapping.put("tyougouryou1_6", "tyougouryou1_6");         // 調合量1_6
        mapping.put("buzailotno2", "buzailotno2");               // 部材在庫No1
        mapping.put("buzaihinmei2", "buzaihinmei2");             // 部材在庫品名1
        mapping.put("tyougouryou2_1", "tyougouryou2_1");         // 調合量1_1
        mapping.put("tyougouryou2_2", "tyougouryou2_2");         // 調合量1_2
        mapping.put("tyougouryou2_3", "tyougouryou2_3");         // 調合量1_3
        mapping.put("tyougouryou2_4", "tyougouryou2_4");         // 調合量1_4
        mapping.put("tyougouryou2_5", "tyougouryou2_5");         // 調合量1_5
        mapping.put("tyougouryou2_6", "tyougouryou2_6");         // 調合量1_6
        mapping.put("torokunichiji", "torokunichiji");           // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");             // 更新日時
        mapping.put("revision", "revision");                     // revision
        mapping.put("deleteflag", "deleteflag");                 // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SubSrSlipSlipkokeibunsokutei>> beanHandler = new BeanListHandler<>(SubSrSlipSlipkokeibunsokutei.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定入力_サブ画面_仮登録(tmp_sub_sr_slip_slipkokeibunsokutei)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param zairyokubun 材料区分
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void insertTmpSubSrSlipSlipkokeibunsokutei(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal newRev, int deleteflag, String kojyo, String lotNo, String edaban, Integer zairyokubun,
            String systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sub_sr_slip_slipkokeibunsokutei ( "
                + "kojyo,lotno,edaban,zairyokubun,tyogouryoukikaku,tyogouzanryou,zairyohinmei,"
                + "buzailotno1,buzaihinmei1,tyougouryou1_1,tyougouryou1_2,tyougouryou1_3,tyougouryou1_4,"
                + "tyougouryou1_5,tyougouryou1_6,buzailotno2,buzaihinmei2,tyougouryou2_1,tyougouryou2_2,"
                + "tyougouryou2_3,tyougouryou2_4,tyougouryou2_5,tyougouryou2_6,torokunichiji,kosinnichiji,"
                + "revision, deleteflag "
                + " ) VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";

        List<Object> params = setUpdateParameterTmpSubSrSlipSlipkokeibunsokutei(true, newRev, deleteflag, kojyo, lotNo, edaban, zairyokubun, systemTime);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定入力_仮登録(tmp_sub_sr_slip_slipkokeibunsokutei)更新処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param zairyokubun 材料区分
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void updateTmpSubSrSlipSlipkokeibunsokutei(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal rev, BigDecimal newRev, String kojyo, String lotNo,
            String edaban, Integer zairyokubun, String systemTime) throws SQLException {

        String sql = "UPDATE tmp_sub_sr_slip_slipkokeibunsokutei SET "
                + "tyogouryoukikaku = ?,tyogouzanryou = ?,zairyohinmei = ?,"
                + "buzailotno1 = ?,buzaihinmei1 = ?,tyougouryou1_1 = ?,tyougouryou1_2 = ?,tyougouryou1_3 = ?,tyougouryou1_4 = ?,"
                + "tyougouryou1_5 = ?,tyougouryou1_6 = ?,buzailotno2 = ?,buzaihinmei2 = ?,tyougouryou2_1 = ?,tyougouryou2_2 = ?,"
                + "tyougouryou2_3 = ?,tyougouryou2_4 = ?,tyougouryou2_5 = ?,tyougouryou2_6 = ?,kosinnichiji = ?,revision = ?, deleteflag = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND zairyokubun = ? AND revision = ? ";

        List<Object> params = setUpdateParameterTmpSubSrSlipSlipkokeibunsokutei(false, newRev, 0, kojyo, lotNo, edaban, zairyokubun, systemTime);

        // 検索条件
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(zairyokubun);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定入力_サブ画面仮登録(tmp_sub_sr_slip_slipkokeibunsokutei)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param zairyokubun 材料区分
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSubSrSlipSlipkokeibunsokutei(boolean isInsert, BigDecimal newRev,
            int deleteflag, String kojyo, String lotNo, String edaban, Integer zairyokubun, String systemTime) {

        List<Object> params = new ArrayList<>();

        // 子画面情報を取得
        GXHDO102C016 beanGXHDO102C016 = (GXHDO102C016) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C016);
        GXHDO102C016Model gxhdO102c016Model = beanGXHDO102C016.getGxhdO102c016Model();

        // ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定入力_サブ画面から更新値を取得
        ArrayList<Object> subGamenDataList = getSubGamenData(gxhdO102c016Model, zairyokubun);
        // 調合量規格
        FXHDD01 tyogouryoukikaku = (FXHDD01) subGamenDataList.get(0);
        // 調合残量
        FXHDD01 tyogouzanryou = (FXHDD01) subGamenDataList.get(1);
        // 部材①
        List<FXHDD01> buzaitab1DataList = (List<FXHDD01>) subGamenDataList.get(2);
        // 部材②
        List<FXHDD01> buzaitab2DataList = (List<FXHDD01>) subGamenDataList.get(3);

        if (isInsert) {
            params.add(kojyo); // 工場ｺｰﾄﾞ
            params.add(lotNo); // ﾛｯﾄNo
            params.add(edaban); // 枝番
            params.add(zairyokubun); // 材料区分
        }

        params.add(DBUtil.stringToStringObjectDefaultNull(tyogouryoukikaku.getValue())); // 調合量規格
        params.add(DBUtil.stringToIntObjectDefaultNull(tyogouzanryou.getValue())); // 調合残量
        params.add(DBUtil.stringToStringObjectDefaultNull(buzaitab1DataList.get(0).getValue())); // 材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(buzaitab1DataList.get(1).getValue())); // 部材在庫No1
        params.add(DBUtil.stringToStringObjectDefaultNull(buzaitab1DataList.get(2).getValue())); // 部材在庫品名1
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(3).getValue())); // 調合量1_1
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(4).getValue())); // 調合量1_2
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(5).getValue())); // 調合量1_3
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(6).getValue())); // 調合量1_4
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(7).getValue())); // 調合量1_5
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(8).getValue())); // 調合量1_6

        params.add(DBUtil.stringToStringObjectDefaultNull(buzaitab2DataList.get(1).getValue())); // 部材在庫No2
        params.add(DBUtil.stringToStringObjectDefaultNull(buzaitab2DataList.get(2).getValue())); // 部材在庫品名2
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab2DataList.get(3).getValue())); // 調合量2_1
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab2DataList.get(4).getValue())); // 調合量2_2
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab2DataList.get(5).getValue())); // 調合量2_3
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab2DataList.get(6).getValue())); // 調合量2_4
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab2DataList.get(7).getValue())); // 調合量2_5
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab2DataList.get(8).getValue())); // 調合量2_6

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
     * ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定入力_サブ画面仮登録(tmp_sub_sr_slip_slipkokeibunsokutei)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSubSrSlipSlipkokeibunsokutei(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal rev, String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sub_sr_slip_slipkokeibunsokutei "
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
     * ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定入力_サブ画面(sub_sr_slip_slipkokeibunsokutei)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param zairyokubun 材料区分
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void insertSubSrSlipSlipkokeibunsokutei(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal newRev, String kojyo, String lotNo, String edaban,
            Integer zairyokubun, String systemTime) throws SQLException {
        String sql = "INSERT INTO sub_sr_slip_slipkokeibunsokutei ( "
                + "kojyo,lotno,edaban,zairyokubun,tyogouryoukikaku,tyogouzanryou,zairyohinmei,"
                + "buzailotno1,buzaihinmei1,tyougouryou1_1,tyougouryou1_2,tyougouryou1_3,tyougouryou1_4,"
                + "tyougouryou1_5,tyougouryou1_6,buzailotno2,buzaihinmei2,tyougouryou2_1,tyougouryou2_2,"
                + "tyougouryou2_3,tyougouryou2_4,tyougouryou2_5,tyougouryou2_6,torokunichiji,kosinnichiji,"
                + "revision "
                + " ) VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";

        List<Object> params = setUpdateParameterSubSrSlipSlipkokeibunsokutei(true, newRev, kojyo, lotNo, edaban, zairyokubun, systemTime);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定入力_ｻﾌﾞ画面(sub_sr_slip_slipkokeibunsokutei)更新処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param zairyokubun 材料区分
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void updateSubSrSlipSlipkokeibunsokutei(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal rev, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            Integer zairyokubun, String systemTime) throws SQLException {

        String sql = "UPDATE sub_sr_slip_slipkokeibunsokutei SET "
                + "tyogouryoukikaku = ?,tyogouzanryou = ?,zairyohinmei = ?,"
                + "buzailotno1 = ?,buzaihinmei1 = ?,tyougouryou1_1 = ?,tyougouryou1_2 = ?,tyougouryou1_3 = ?,tyougouryou1_4 = ?,"
                + "tyougouryou1_5 = ?,tyougouryou1_6 = ?,buzailotno2 = ?,buzaihinmei2 = ?,tyougouryou2_1 = ?,tyougouryou2_2 = ?,"
                + "tyougouryou2_3 = ?,tyougouryou2_4 = ?,tyougouryou2_5 = ?,tyougouryou2_6 = ?,kosinnichiji = ?,revision = ?"
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND zairyokubun = ? AND revision = ? ";

        List<Object> params = setUpdateParameterSubSrSlipSlipkokeibunsokutei(false, newRev, kojyo, lotNo, edaban, zairyokubun, systemTime);

        // 検索条件
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(zairyokubun);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定入力_サブ画面から更新値を取得
     *
     * @param gxhdO102c016Model モデルデータ
     * @param zairyokubun 材料区分
     * @return 更新値情報
     */
    private ArrayList<Object> getSubGamenData(GXHDO102C016Model gxhdO102c016Model, Integer zairyokubun) {
        GXHDO102C016Model.SubGamenData c016subgamendata = GXHDO102C016Logic.getC016subgamendata(gxhdO102c016Model, zairyokubun);
        ArrayList<Object> returnList = new ArrayList<>();
        // 調合量規格
        FXHDD01 tyogouryoukikaku = c016subgamendata.getSubDataTyogouryoukikaku();
        // 調合残量
        FXHDD01 tyogouzanryou = c016subgamendata.getSubDataTyogouzanryou();
        // 部材①
        List<FXHDD01> buzaitab1DataList = c016subgamendata.getSubDataBuzaitab1();
        // 部材②
        List<FXHDD01> buzaitab2DataList = c016subgamendata.getSubDataBuzaitab2();
        returnList.add(tyogouryoukikaku);
        returnList.add(tyogouzanryou);
        returnList.add(buzaitab1DataList);
        returnList.add(buzaitab2DataList);
        return returnList;
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定入力_サブ画面登録(tmp_sub_sr_slip_slipkokeibunsokutei)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param zairyokubun 材料区分
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSubSrSlipSlipkokeibunsokutei(boolean isInsert, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Integer zairyokubun, String systemTime) {

        List<Object> params = new ArrayList<>();

        // 子画面情報を取得
        GXHDO102C016 beanGXHDO102C016 = (GXHDO102C016) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C016);
        GXHDO102C016Model gxhdO102c016Model = beanGXHDO102C016.getGxhdO102c016Model();
        // ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定入力_サブ画面から更新値を取得
        ArrayList<Object> subGamenDataList = getSubGamenData(gxhdO102c016Model, zairyokubun);
        // 調合量規格
        FXHDD01 tyogouryoukikaku = (FXHDD01) subGamenDataList.get(0);
        // 調合残量
        FXHDD01 tyogouzanryou = (FXHDD01) subGamenDataList.get(1);
        // 部材①
        List<FXHDD01> buzaitab1DataList = (List<FXHDD01>) subGamenDataList.get(2);
        // 部材②
        List<FXHDD01> buzaitab2DataList = (List<FXHDD01>) subGamenDataList.get(3);

        if (isInsert) {
            params.add(kojyo); // 工場ｺｰﾄﾞ
            params.add(lotNo); // ﾛｯﾄNo
            params.add(edaban); // 枝番
            params.add(zairyokubun); // 材料区分
        }

        params.add(DBUtil.stringToStringObject(tyogouryoukikaku.getValue())); // 調合量規格
        params.add(DBUtil.stringToIntObject(tyogouzanryou.getValue())); // 調合残量
        params.add(DBUtil.stringToStringObject(buzaitab1DataList.get(0).getValue())); // 材料品名
        params.add(DBUtil.stringToStringObject(buzaitab1DataList.get(1).getValue())); // 部材在庫No1
        params.add(DBUtil.stringToStringObject(buzaitab1DataList.get(2).getValue())); // 部材在庫品名1
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(3).getValue())); // 調合量1_1
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(4).getValue())); // 調合量1_2
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(5).getValue())); // 調合量1_3
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(6).getValue())); // 調合量1_4
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(7).getValue())); // 調合量1_5
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(8).getValue())); // 調合量1_6

        params.add(DBUtil.stringToStringObject(buzaitab2DataList.get(1).getValue())); // 部材在庫No2
        params.add(DBUtil.stringToStringObject(buzaitab2DataList.get(2).getValue())); // 部材在庫品名2
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab2DataList.get(3).getValue())); // 調合量2_1
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab2DataList.get(4).getValue())); // 調合量2_2
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab2DataList.get(5).getValue())); // 調合量2_3
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab2DataList.get(6).getValue())); // 調合量2_4
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab2DataList.get(7).getValue())); // 調合量2_5
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab2DataList.get(8).getValue())); // 調合量2_6

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
     * ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定入力_サブ画面仮登録(tmp_sub_sr_slip_slipkokeibunsokutei)登録処理(削除時)
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void insertDeleteDataTmpSubSrSlipSlipkokeibunsokutei(QueryRunner queryRunnerQcdb,
            Connection conQcdb, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, String systemTime) throws SQLException {
        String sql = "INSERT INTO tmp_sub_sr_slip_slipkokeibunsokutei( "
                + "kojyo,lotno,edaban,zairyokubun,tyogouryoukikaku,tyogouzanryou,zairyohinmei,"
                + "buzailotno1,buzaihinmei1,tyougouryou1_1,tyougouryou1_2,tyougouryou1_3,tyougouryou1_4,"
                + "tyougouryou1_5,tyougouryou1_6,buzailotno2,buzaihinmei2,tyougouryou2_1,tyougouryou2_2,"
                + "tyougouryou2_3,tyougouryou2_4,tyougouryou2_5,tyougouryou2_6,torokunichiji,kosinnichiji,"
                + "revision, deleteflag "
                + ") SELECT "
                + "kojyo,lotno,edaban,zairyokubun,tyogouryoukikaku,tyogouzanryou,zairyohinmei,"
                + "buzailotno1,buzaihinmei1,tyougouryou1_1,tyougouryou1_2,tyougouryou1_3,tyougouryou1_4,"
                + "tyougouryou1_5,tyougouryou1_6,buzailotno2,buzaihinmei2,tyougouryou2_1,tyougouryou2_2,"
                + "tyougouryou2_3,tyougouryou2_4,tyougouryou2_5,tyougouryou2_6,?,?,?,? "
                + " FROM sub_sr_slip_slipkokeibunsokutei "
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
     * ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定入力_サブ画面仮登録(sub_sr_slip_slipkokeibunsokutei)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSubSrSlipSlipkokeibunsokutei(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal rev, String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sub_sr_slip_slipkokeibunsokutei "
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
     * 部材在庫の重量ﾃﾞｰﾀ連携
     *
     * @param processData 処理制御データ
     * @param tantoshaCd 更新者
     * @return レスポンスデータ
     */
    private String doPMLA0212Save(ProcessData processData, String tantoshaCd) {
        ArrayList<String> errorItemList = new ArrayList<>();
        // 溶剤1_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B033Const.YOUZAI1_BUZAIZAIKOLOTNO1, GXHDO102B033Const.YOUZAI1_TYOUGOURYOU1, errorItemList);
        // 溶剤1_部材在庫No2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B033Const.YOUZAI1_BUZAIZAIKOLOTNO2, GXHDO102B033Const.YOUZAI1_TYOUGOURYOU2, errorItemList);
        // 溶剤2_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B033Const.YOUZAI2_BUZAIZAIKOLOTNO1, GXHDO102B033Const.YOUZAI2_TYOUGOURYOU1, errorItemList);
        // 溶剤2_部材在庫No2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B033Const.YOUZAI2_BUZAIZAIKOLOTNO2, GXHDO102B033Const.YOUZAI2_TYOUGOURYOU2, errorItemList);

        // 上記の処理でｴﾗｰが発生した場合、画面にエラーダイアログを出力する。
        if (!errorItemList.isEmpty()) {
            ErrorListMessage errorListMessageList = new ErrorListMessage();
            errorListMessageList.setResultMessage(MessageUtil.getMessage("buzailotnoErrorList"));
            errorListMessageList.setResultMessageList(errorItemList);
            errorListMessageList.setTitleMessage(MessageUtil.getMessage("infoMsg"));
            processData.setErrorListMessage(errorListMessageList);
            return "error";
        }
        return "ok";
    }

    /**
     * 部材在庫管理を参照【PMLA0212_部材在庫ﾃﾞｰﾀ更新】
     *
     * @param processData 処理制御データ
     * @param tantoshaCd 更新者
     * @param sizailotnoStr 部材在庫No
     * @param tyougouryouStr 調合量
     * @return レスポンスデータ
     */
    private void doCallPmla0212Api(ProcessData processData, String tantoshaCd, String sizailotnoStr, String tyougouryouStr, ArrayList<String> errorItemList) {
        // 調合量X_Y
        String tyougouryouValue = "";
        // WIPﾛｯﾄNo
        String wiplotnoValue = "";
        // 部材在庫NoX_Yに値が入っている場合、以下の内容を元にAPIを呼び出す
        FXHDD01 itemFxhdd01Sizailotno = getItemRow(processData.getItemList(), sizailotnoStr);
        if (itemFxhdd01Sizailotno == null || StringUtil.isEmpty(itemFxhdd01Sizailotno.getValue())) {
            return;
        }
        // 部材在庫NoX_Y
        String sizailotnoValue = StringUtil.nullToBlank(itemFxhdd01Sizailotno.getValue());

        FXHDD01 itemFxhdd01Tyougouryou = getItemRow(processData.getItemList(), tyougouryouStr);
        if (itemFxhdd01Tyougouryou != null) {
            // 調合量X_Y
            tyougouryouValue = StringUtil.nullToBlank(itemFxhdd01Tyougouryou.getValue());
        }
        FXHDD01 itemFxhdd01Wiplotno = getItemRow(processData.getItemList(), GXHDO102B033Const.WIPLOTNO);
        if (itemFxhdd01Wiplotno != null) {
            // WIPﾛｯﾄNo
            wiplotnoValue = StringUtil.nullToBlank(itemFxhdd01Wiplotno.getValue());
        }
        ArrayList<String> paramsList = new ArrayList<>();
        paramsList.add(sizailotnoValue);
        paramsList.add(tantoshaCd);
        paramsList.add("PXHDO102");
        paramsList.add(tyougouryouValue);
        paramsList.add(null);
        paramsList.add(null);
        paramsList.add(null);
        paramsList.add(wiplotnoValue);

        try {
            QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
            // 「/api/PMLA0212/doSave」APIを呼び出す
            String responseResult = CommonUtil.doRequestPmla0212Save(queryRunnerDoc, paramsList);
            if (!"ok".equals(responseResult)) {
                errorItemList.add(itemFxhdd01Sizailotno.getLabel1());
            }
        } catch (Exception ex) {
            ErrUtil.outputErrorLog(itemFxhdd01Sizailotno.getLabel1() + "の重量ﾃﾞｰﾀ連携処理エラー発生", ex, LOGGER);
            errorItemList.add(itemFxhdd01Sizailotno.getLabel1());
        }
    }

    /**
     * 画面項目存在チェック(ﾌｫｰﾑﾊﾟﾗﾒｰﾀに対象の項目が存在していることを確認)
     *
     * @param processData 処理制御データ
     * @return エラー項目名リスト
     */
    private List<String> checkExistFormItem(ProcessData processData) {
        List<String> errorItemNameList = new ArrayList<>();
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String formId = StringUtil.nullToBlank(session.getAttribute("formId"));
        // 項目IDリスト取得
        List<String> itemIdList = getItemIdList(processData, formId);
        Map<String, String> allItemIdMap = new HashMap<>();
        allItemIdMap.put(GXHDO102B033Const.WIPLOTNO, "WIPﾛｯﾄNo");
        allItemIdMap.put(GXHDO102B033Const.SLIPHINMEI, "ｽﾘｯﾌﾟ品名");
        allItemIdMap.put(GXHDO102B033Const.SLIPLOTNO, "ｽﾘｯﾌﾟLotNo");
        allItemIdMap.put(GXHDO102B033Const.LOTKUBUN, "ﾛｯﾄ区分");
        allItemIdMap.put(GXHDO102B033Const.GENRYOUKIGOU, "原料記号");
        allItemIdMap.put(GXHDO102B033Const.GOKI, "秤量号機");
        allItemIdMap.put(GXHDO102B033Const.DASSIZARANOSYURUI, "脱脂皿の種類");
        allItemIdMap.put(GXHDO102B033Const.RUTUBONO, "ﾙﾂﾎﾞNo");
        allItemIdMap.put(GXHDO102B033Const.RUTUBOFUUTAIJYUURYOU, "ﾙﾂﾎﾞ風袋重量");
        allItemIdMap.put(GXHDO102B033Const.KANSOUMAESLIPJYUURYOU, "乾燥前ｽﾘｯﾌﾟ重量");
        allItemIdMap.put(GXHDO102B033Const.KANSOUKI1, "乾燥機①");
        allItemIdMap.put(GXHDO102B033Const.KANSOUONDO1, "乾燥温度①");
        allItemIdMap.put(GXHDO102B033Const.KANSOUJIKAN1, "乾燥時間①");
        allItemIdMap.put(GXHDO102B033Const.KANSOUKAISI1_DAY, "乾燥開始日①");
        allItemIdMap.put(GXHDO102B033Const.KANSOUKAISI1_TIME, "乾燥開始時間①");
        allItemIdMap.put(GXHDO102B033Const.KANSOUSYUURYOU1_DAY, "乾燥終了日①");
        allItemIdMap.put(GXHDO102B033Const.KANSOUSYUURYOU1_TIME, "乾燥終了時間①");
        allItemIdMap.put(GXHDO102B033Const.KANSOUKI2, "乾燥機②");
        allItemIdMap.put(GXHDO102B033Const.KANSOUONDO2, "乾燥温度②");
        allItemIdMap.put(GXHDO102B033Const.KANSOUJIKAN2, "乾燥時間②");
        allItemIdMap.put(GXHDO102B033Const.KANSOUKAISI2_DAY, "乾燥開始日②");
        allItemIdMap.put(GXHDO102B033Const.KANSOUKAISI2_TIME, "乾燥開始時間②");
        allItemIdMap.put(GXHDO102B033Const.KANSOUSYUURYOU2_DAY, "乾燥終了日②");
        allItemIdMap.put(GXHDO102B033Const.KANSOUSYUURYOU2_TIME, "乾燥終了時間②");
        allItemIdMap.put(GXHDO102B033Const.KANSOUGOSOUJYUURYOU, "乾燥後総重量");
        allItemIdMap.put(GXHDO102B033Const.KANSOUGOSYOUMIJYUURYOU, "乾燥後正味重量");
        allItemIdMap.put(GXHDO102B033Const.KOKEIBUNHIRITU, "固形分比率");
        allItemIdMap.put(GXHDO102B033Const.KOKEIBUNSOKUTEITANTOUSYA, "固形分測定担当者");
        allItemIdMap.put(GXHDO102B033Const.YOUZAITYOUSEIRYOU, "溶剤調整量");
        allItemIdMap.put(GXHDO102B033Const.TOLUENETYOUSEIRYOU, "ﾄﾙｴﾝ調整量");
        allItemIdMap.put(GXHDO102B033Const.SOLMIXTYOUSEIRYOU, "ｿﾙﾐｯｸｽ調整量");
        allItemIdMap.put(GXHDO102B033Const.YOUZAIKEIRYOU_DAY, "溶剤秤量日");
        allItemIdMap.put(GXHDO102B033Const.YOUZAIKEIRYOU_TIME, "溶剤秤量時間");
        allItemIdMap.put(GXHDO102B033Const.YOUZAI1_ZAIRYOUHINMEI, "溶剤①_材料品名");
        allItemIdMap.put(GXHDO102B033Const.YOUZAI1_TYOUGOURYOUKIKAKU, "溶剤①_調合量規格");
        allItemIdMap.put(GXHDO102B033Const.YOUZAI1_BUZAIZAIKOLOTNO1, "溶剤①_部材在庫No1");
        allItemIdMap.put(GXHDO102B033Const.YOUZAI1_TYOUGOURYOU1, "溶剤①_調合量1");
        allItemIdMap.put(GXHDO102B033Const.YOUZAI1_BUZAIZAIKOLOTNO2, "溶剤①_部材在庫No2");
        allItemIdMap.put(GXHDO102B033Const.YOUZAI1_TYOUGOURYOU2, "溶剤①_調合量2");
        allItemIdMap.put(GXHDO102B033Const.YOUZAI2_ZAIRYOUHINMEI, "溶剤②_材料品名");
        allItemIdMap.put(GXHDO102B033Const.YOUZAI2_TYOUGOURYOUKIKAKU, "溶剤②_調合量規格");
        allItemIdMap.put(GXHDO102B033Const.YOUZAI2_BUZAIZAIKOLOTNO1, "溶剤②_部材在庫No1");
        allItemIdMap.put(GXHDO102B033Const.YOUZAI2_TYOUGOURYOU1, "溶剤②_調合量1");
        allItemIdMap.put(GXHDO102B033Const.YOUZAI2_BUZAIZAIKOLOTNO2, "溶剤②_部材在庫No2");
        allItemIdMap.put(GXHDO102B033Const.YOUZAI2_TYOUGOURYOU2, "溶剤②_調合量2");
        allItemIdMap.put(GXHDO102B033Const.TANTOUSYA, "担当者");
        allItemIdMap.put(GXHDO102B033Const.KAKUHAN_KAKUHANMODE, "撹拌_撹拌ﾓｰﾄﾞ");
        allItemIdMap.put(GXHDO102B033Const.KAKUHAN_KAITENHOUKOU, "撹拌_回転方向");
        allItemIdMap.put(GXHDO102B033Const.KAKUHAN_KAITENSUU, "撹拌_回転数(rpm)");
        allItemIdMap.put(GXHDO102B033Const.KAKUHAN_KAKUHANJIKAN, "撹拌_撹拌時間");
        allItemIdMap.put(GXHDO102B033Const.KAKUHAN_KAKUHANKAISI_DAY, "撹拌_撹拌開始日");
        allItemIdMap.put(GXHDO102B033Const.KAKUHAN_KAKUHANKAISI_TIME, "撹拌_撹拌開始時間");
        allItemIdMap.put(GXHDO102B033Const.KAISIDENRYUUTI, "開始電流値(A)");
        allItemIdMap.put(GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOUYOTEI_DAY, "撹拌_撹拌終了予定日");
        allItemIdMap.put(GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOUYOTEI_TIME, "撹拌_撹拌終了予定時間");
        allItemIdMap.put(GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOU_DAY, "撹拌_撹拌終了日");
        allItemIdMap.put(GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOU_TIME, "撹拌_撹拌終了時間");
        allItemIdMap.put(GXHDO102B033Const.SYUURYOUDENRYUUTI, "終了電流値(A)");
        allItemIdMap.put(GXHDO102B033Const.KAKUHAN_KAKUHANTANTOUSYA, "撹拌_撹拌担当者");
        allItemIdMap.put(GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANMODE, "排出前撹拌_撹拌ﾓｰﾄﾞ");
        allItemIdMap.put(GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAITENHOUKOU, "排出前撹拌_回転方向");
        allItemIdMap.put(GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAITENSUU, "排出前撹拌_回転数(rpm)");
        allItemIdMap.put(GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANJIKAN, "排出前撹拌_撹拌時間");
        allItemIdMap.put(GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANKAISI_DAY, "排出前撹拌_撹拌開始日");
        allItemIdMap.put(GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANKAISI_TIME, "排出前撹拌_撹拌開始時間");
        allItemIdMap.put(GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOUYOTEI_DAY, "排出前撹拌_撹拌終了予定日");
        allItemIdMap.put(GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOUYOTEI_TIME, "排出前撹拌_撹拌終了予定時間");
        allItemIdMap.put(GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOU_DAY, "排出前撹拌_撹拌終了日");
        allItemIdMap.put(GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOU_TIME, "排出前撹拌_撹拌終了時間");
        allItemIdMap.put(GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANTANTOUSYA, "排出前撹拌_撹拌担当者");
        allItemIdMap.put(GXHDO102B033Const.HAISYUTUKAISI_DAY, "排出開始日");
        allItemIdMap.put(GXHDO102B033Const.HAISYUTUKAISI_TIME, "排出開始時間");
        allItemIdMap.put(GXHDO102B033Const.HAISYUTUSYUURYOU_DAY, "排出終了日");
        allItemIdMap.put(GXHDO102B033Const.HAISYUTUSYUURYOU_TIME, "排出終了時間");
        allItemIdMap.put(GXHDO102B033Const.HAISYUTUTANTOUSYA, "排出担当者");
        allItemIdMap.put(GXHDO102B033Const.BIKOU1, "備考1");
        allItemIdMap.put(GXHDO102B033Const.BIKOU2, "備考2");

        // 項目IDリストに存在しない画面項目を取得
        List<String> notExistItemidList = allItemIdMap.keySet().stream().filter(itemId -> !itemIdList.contains(itemId)).collect(Collectors.toList());
        notExistItemidList.forEach((notExistItemid) -> {
            errorItemNameList.add(allItemIdMap.get(notExistItemid));
        });

        List<String> errorMassageList = new ArrayList<>();
        if (0 < errorItemNameList.size()) {
            errorMassageList.add("以下の画面項目に対する情報が設定されていません。ｼｽﾃﾑに連絡してください。");
            errorMassageList.add("【対象項目】");
            errorMassageList.addAll(errorItemNameList);
        }

        return errorMassageList;
    }

    /**
     * 項目IDリスト取得
     *
     * @param processData 処理制御データ
     * @param formId 項目定義情報
     * @return 項目IDリスト
     */
    private List<String> getItemIdList(ProcessData processData, String formId) {
        try {
            QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
            String sql = "SELECT item_id itemId "
                    + " FROM fxhdd01 "
                    + " WHERE gamen_id = ? "
                    + " ORDER BY item_no ";

            List<Object> params = new ArrayList<>();
            params.add(formId);

            List<Map<String, Object>> mapList = queryRunnerDoc.query(sql, new MapListHandler(), params.toArray());
            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            return mapList.stream().map(n -> n.get("itemId").toString()).collect(Collectors.toList());
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
        return null;
    }

    /**
     * 項目の表示制御
     *
     * @param processData 処理制御データ
     * @param notShowItemList 画面非表示項目リスト
     */
    private void removeItemFromItemList(ProcessData processData, List<String> notShowItemList) {
        List<FXHDD01> itemList = processData.getItemList();
        GXHDO102B033A bean = (GXHDO102B033A) getFormBean("gXHDO102B033A");
        notShowItemList.forEach((notShowItem) -> {
            itemList.remove(getItemRow(itemList, notShowItem));
        });

        bean.setYouzaityouseiryou(null);
        bean.setToluenetyouseiryou(null);
        bean.setSolmixtyouseiryou(null);
        bean.setYouzaikeiryou_day(null);
        bean.setYouzaikeiryou_time(null);
        bean.setYouzai1_zairyouhinmei(null);
        bean.setYouzai1_tyougouryoukikaku(null);
        bean.setYouzai1_buzaizaikolotno1(null);
        bean.setYouzai1_tyougouryou1(null);
        bean.setYouzai1_buzaizaikolotno2(null);
        bean.setYouzai1_tyougouryou2(null);
        bean.setYouzai2_zairyouhinmei(null);
        bean.setYouzai2_tyougouryoukikaku(null);
        bean.setYouzai2_buzaizaikolotno1(null);
        bean.setYouzai2_tyougouryou1(null);
        bean.setYouzai2_buzaizaikolotno2(null);
        bean.setYouzai2_tyougouryou2(null);
        bean.setTantousya(null);
        bean.setKakuhan_kakuhanmode(null);
        bean.setKakuhan_kaitenhoukou(null);
        bean.setKakuhan_kaitensuu(null);
        bean.setKakuhan_kakuhanjikan(null);
        bean.setKakuhan_kakuhankaisi_day(null);
        bean.setKakuhan_kakuhankaisi_time(null);
        bean.setKaisidenryuuti(null);
        bean.setKakuhan_kakuhansyuuryouyotei_day(null);
        bean.setKakuhan_kakuhansyuuryouyotei_time(null);
        bean.setKakuhan_kakuhansyuuryou_day(null);
        bean.setKakuhan_kakuhansyuuryou_time(null);
        bean.setSyuuryoudenryuuti(null);
        bean.setKakuhan_kakuhantantousya(null);
        bean.setHaisyutumaekakuhan_kakuhanmode(null);
        bean.setHaisyutumaekakuhan_kaitenhoukou(null);
        bean.setHaisyutumaekakuhan_kaitensuu(null);
        bean.setHaisyutumaekakuhan_kakuhanjikan(null);
        bean.setHaisyutumaekakuhan_kakuhankaisi_day(null);
        bean.setHaisyutumaekakuhan_kakuhankaisi_time(null);
        bean.setHaisyutumaekakuhan_kakuhansyuuryouyotei_day(null);
        bean.setHaisyutumaekakuhan_kakuhansyuuryouyotei_time(null);
        bean.setHaisyutumaekakuhan_kakuhansyuuryou_day(null);
        bean.setHaisyutumaekakuhan_kakuhansyuuryou_time(null);
        bean.setHaisyutumaekakuhan_kakuhantantousya(null);
        bean.setHaisyutukaisi_day(null);
        bean.setHaisyutukaisi_time(null);
        bean.setHaisyutusyuuryou_day(null);
        bean.setHaisyutusyuuryou_time(null);
        bean.setHaisyututantousya(null);
    }

    /**
     * 項目の表示制御
     *
     * @param processData 処理制御データ
     * @param queryRunnerDoc QueryRunnerオブジェクト(DocServer)
     * @param queryRunnerQcdb QueryRunnerオブジェクト(Qcdb)
     * @param shikakariData 前工程WIPから仕掛情報
     * @param lotNo ﾛｯﾄNo
     * @throws SQLException 例外エラー
     */
    private void setItemRendered(ProcessData processData, QueryRunner queryRunnerDoc, QueryRunner queryRunnerQcdb, Map shikakariData, String lotNo) throws SQLException {
        String kojyo = lotNo.substring(0, 3);
        String lotNo9 = lotNo.substring(3, 12);
        String edaban = lotNo.substring(12, 15);
        String syurui = "ｽﾘｯﾌﾟ作製";
        // [ﾊﾟﾗﾒｰﾀﾏｽﾀ]から、ﾃﾞｰﾀを取得
        Map fxhbm03Data = loadFxhbm03Data(queryRunnerDoc, "ｽﾘｯﾌﾟ作製_ｽﾘｯﾌﾟ固形分測定_表示制御");
        // 画面非表示項目リスト
        List<String> notShowItemList = Arrays.asList(GXHDO102B033Const.YOUZAITYOUSEIRYOU, GXHDO102B033Const.TOLUENETYOUSEIRYOU, GXHDO102B033Const.SOLMIXTYOUSEIRYOU,
                GXHDO102B033Const.YOUZAIKEIRYOU_DAY, GXHDO102B033Const.YOUZAIKEIRYOU_TIME, GXHDO102B033Const.YOUZAI1_ZAIRYOUHINMEI, GXHDO102B033Const.YOUZAI1_TYOUGOURYOUKIKAKU,
                GXHDO102B033Const.YOUZAI1_BUZAIZAIKOLOTNO1, GXHDO102B033Const.YOUZAI1_TYOUGOURYOU1, GXHDO102B033Const.YOUZAI1_BUZAIZAIKOLOTNO2, GXHDO102B033Const.YOUZAI1_TYOUGOURYOU2,
                GXHDO102B033Const.YOUZAI2_ZAIRYOUHINMEI, GXHDO102B033Const.YOUZAI2_TYOUGOURYOUKIKAKU, GXHDO102B033Const.YOUZAI2_BUZAIZAIKOLOTNO1, GXHDO102B033Const.YOUZAI2_TYOUGOURYOU1,
                GXHDO102B033Const.YOUZAI2_BUZAIZAIKOLOTNO2, GXHDO102B033Const.YOUZAI2_TYOUGOURYOU2, GXHDO102B033Const.TANTOUSYA, GXHDO102B033Const.KAKUHAN_KAKUHANMODE,
                GXHDO102B033Const.KAKUHAN_KAITENHOUKOU, GXHDO102B033Const.KAKUHAN_KAITENSUU, GXHDO102B033Const.KAKUHAN_KAKUHANJIKAN, GXHDO102B033Const.KAKUHAN_KAKUHANKAISI_DAY,
                GXHDO102B033Const.KAKUHAN_KAKUHANKAISI_TIME, GXHDO102B033Const.KAISIDENRYUUTI, GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOUYOTEI_DAY, GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOUYOTEI_TIME,
                GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOU_DAY, GXHDO102B033Const.KAKUHAN_KAKUHANSYUURYOU_TIME, GXHDO102B033Const.SYUURYOUDENRYUUTI, GXHDO102B033Const.KAKUHAN_KAKUHANTANTOUSYA,
                GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANMODE, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAITENHOUKOU, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAITENSUU, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANJIKAN,
                GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANKAISI_DAY, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANKAISI_TIME, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOUYOTEI_DAY,
                GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOUYOTEI_TIME, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOU_DAY, GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANSYUURYOU_TIME,
                GXHDO102B033Const.HAISYUTUMAEKAKUHAN_KAKUHANTANTOUSYA, GXHDO102B033Const.HAISYUTUKAISI_DAY, GXHDO102B033Const.HAISYUTUKAISI_TIME, GXHDO102B033Const.HAISYUTUSYUURYOU_DAY,
                GXHDO102B033Const.HAISYUTUSYUURYOU_TIME, GXHDO102B033Const.HAISYUTUTANTOUSYA);
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
     * (19)[ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadSrSlipYouzaihyouryouTounyuuSutenyouki(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT yuudentaislurry_tyougouryou1, yuudentaislurry_tyougouryou2, zunsanzai1_tyougouryou1, zunsanzai1_tyougouryou2,"
                + " zunsanzai2_tyougouryou1, zunsanzai2_tyougouryou2, youzai1_tyougouryou1, youzai1_tyougouryou2, youzai2_tyougouryou1, youzai2_tyougouryou2 "
                + " FROM sr_slip_youzaihyouryou_tounyuu_sutenyouki "
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

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * (20)[ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadSrSlipSlurrykokeibuntyouseiSiropori(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT slurrygoukeijyuuryou"
                + " FROM sr_slip_slurrykokeibuntyousei_siropori "
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

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * (21)[ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadSrSlipBinderhyouryouTounyuu(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT bindertenkaryougoukei"
                + " FROM sr_slip_binderhyouryou_tounyuu "
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

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * (22)[ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadSrSlipSlurrykokeibuntyouseiSutenyouki(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT youzaityouseiryou, youzai2_tyougouryou1, youzai2_tyougouryou2, 2kaimeyouzai1_tyougouryou1 AS nikaimeyouzai1_tyougouryou1,"
                + " 2kaimeyouzai1_tyougouryou2 AS nikaimeyouzai1_tyougouryou2, 2kaimeyouzai2_tyougouryou1 AS nikaimeyouzai2_tyougouryou1,"
                + "2kaimeyouzai2_tyougouryou2 AS nikaimeyouzai2_tyougouryou2 "
                + " FROM sr_slip_slurrykokeibuntyousei_sutenyouki "
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

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * (23)[ｽﾘｯﾌﾟ作製・溶剤秤量・投入(白ﾎﾟﾘ)]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadSrSlipYouzaihyouryouTounyuuSiropori(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT youzai1_tyougouryou1, youzai1_tyougouryou2, youzai2_tyougouryou1, youzai2_tyougouryou2,"
                + " youzai3_tyougouryou1, youzai3_tyougouryou2, youzai4_tyougouryou1, youzai4_tyougouryou2"
                + " FROM sr_slip_youzaihyouryou_tounyuu_siropori "
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

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * ②「固形分目標値」の取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト(DocServer)
     * @param queryRunnerQcdb QueryRunnerオブジェクト(Qcdb)
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param tantoshaCd 担当者
     * @param resultMap 取得取得
     * @throws SQLException 例外エラー
     */
    private ErrorMessageInfo getKoukeibunnmokuhyochi(QueryRunner queryRunnerDoc, QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String tantoshaCd, HashMap<String, String> resultMap) throws SQLException {
        String syurui = "ｽﾘｯﾌﾟ作製";
        // 前工程WIPから仕掛情報を取得処理
        Map shikakariData = loadShikakariDataFromWip(queryRunnerDoc, tantoshaCd, kojyo + lotNo + edaban);
        // [ﾊﾟﾗﾒｰﾀﾏｽﾀ]から、ﾃﾞｰﾀを取得
        Map fxhbm03Data = loadFxhbm03Data(queryRunnerDoc, "ｽﾘｯﾌﾟ作製_ｽﾘｯﾌﾟ固形分測定_重量_表示制御");
        if (fxhbm03Data == null) {
            // 取得できなかった場合、ｴﾗｰ項目をﾘｽﾄに追加
            return MessageUtil.getErrorMessageInfo("XHD-000014", true, false, null);
        }
        // [前工程設計]から、ﾃﾞｰﾀを取得
        Map daMkSekKeiData = loadDaMkSekKeiData(queryRunnerQcdb, kojyo, lotNo, edaban, syurui);
        if (daMkSekKeiData == null || daMkSekKeiData.isEmpty()) {
            // 取得できなかった場合、ｴﾗｰ項目をﾘｽﾄに追加
            return MessageUtil.getErrorMessageInfo("XHD-000014", true, false, null);
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
                // ｴﾗｰ項目をﾘｽﾄに追加
                return MessageUtil.getErrorMessageInfo("XHD-000028", true, false, null, "規格情報");
            }
            // 前工程規格情報の規格値
            String kikakuti = StringUtil.nullToBlank(getMapData(daMkhYoJunJokenData, "kikakuti"));
            resultMap.put("kikakuti", kikakuti);
        } else {
            // 前工程規格情報の規格値
            String kikakuti = StringUtil.nullToBlank(getMapData(daMkJokenData, "kikakuti"));
            resultMap.put("kikakuti", kikakuti);
        }
        return null;
    }
}
