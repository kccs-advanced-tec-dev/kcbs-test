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
import jp.co.kccs.xhd.common.ErrorListMessage;
import jp.co.kccs.xhd.common.InitMessage;
import jp.co.kccs.xhd.common.KikakuError;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.db.model.FXHDD02;
import jp.co.kccs.xhd.db.model.SikakariJson;
import jp.co.kccs.xhd.db.model.SrYuudentaiYouzai;
import jp.co.kccs.xhd.db.model.SubSrYuudentaiYouzai;
import jp.co.kccs.xhd.model.GXHDO102C009Model;
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
 * 変更日	2021/11/09<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B020(誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量)
 *
 * @author KCSS K.Jo
 * @since 2021/11/09
 */
public class GXHDO102B020 implements IFormLogic {

    private static final Logger LOGGER = Logger.getLogger(GXHDO102B014.class.getName());
    private static final String JOTAI_FLG_KARI_TOROKU = "0";
    private static final String JOTAI_FLG_TOROKUZUMI = "1";
    private static final String JOTAI_FLG_SAKUJO = "9";
    private static final String SQL_STATE_RECORD_LOCK_ERR = "55P03";

    /**
     * コンストラクタ
     */
    public GXHDO102B020() {
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
            initGXHDO102B020A(processData);

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
                    GXHDO102B020Const.BTN_EDABAN_COPY_TOP,
                    GXHDO102B020Const.BTN_YOUZAIHYOURYOUKAISI_TOP,
                    GXHDO102B020Const.BTN_YOUZAIHYORYOSYURYO_TOP,
                    GXHDO102B020Const.BTN_KAKUHANKAISI_TOP,
                    GXHDO102B020Const.BTN_KAKUHANSYUURYOU_TOP,
                    GXHDO102B020Const.BTN_TENKAZAISLURRYHRKAISI_TOP,
                    GXHDO102B020Const.BTN_TENKAZAISLYHRSYUURYOU_TOP,
                    GXHDO102B020Const.BTN_EDABAN_COPY_BOTTOM,
                    GXHDO102B020Const.BTN_YOUZAIHYOURYOUKAISI_BOTTOM,
                    GXHDO102B020Const.BTN_YOUZAIHYORYOSYURYO_BOTTOM,
                    GXHDO102B020Const.BTN_KAKUHANKAISI_BOTTOM,
                    GXHDO102B020Const.BTN_KAKUHANSYUURYOU_BOTTOM,
                    GXHDO102B020Const.BTN_TENKAZAISLURRYHRKAISI_BOTTOM,
                    GXHDO102B020Const.BTN_TENKAZAISLYHRSYUURYOU_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO102B020Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO102B020Const.BTN_INSERT_TOP,
                    GXHDO102B020Const.BTN_DELETE_TOP,
                    GXHDO102B020Const.BTN_UPDATE_TOP,
                    GXHDO102B020Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO102B020Const.BTN_INSERT_BOTTOM,
                    GXHDO102B020Const.BTN_DELETE_BOTTOM,
                    GXHDO102B020Const.BTN_UPDATE_BOTTOM
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
            case GXHDO102B020Const.BTN_EDABAN_COPY_TOP:
            case GXHDO102B020Const.BTN_EDABAN_COPY_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 仮登録
            case GXHDO102B020Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO102B020Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO102B020Const.BTN_INSERT_TOP:
            case GXHDO102B020Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 修正
            case GXHDO102B020Const.BTN_UPDATE_TOP:
            case GXHDO102B020Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO102B020Const.BTN_DELETE_TOP:
            case GXHDO102B020Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 溶剤秤量開始日時
            case GXHDO102B020Const.BTN_YOUZAIHYOURYOUKAISI_TOP:
            case GXHDO102B020Const.BTN_YOUZAIHYOURYOUKAISI_BOTTOM:
                method = "setYouzaihyouryoukaisiDateTime";
                break;
            // 溶剤④_調合量規格計算
            case GXHDO102B020Const.BTN_YZ4_TYOUGOURYOUKIKAKU_TOP:
            case GXHDO102B020Const.BTN_YZ4_TYOUGOURYOUKIKAKU_BOTTOM:
                method = "setYz4Tyougouryoukikaku";
                break;
            // 溶剤⑤_調合量規格計算
            case GXHDO102B020Const.BTN_YZ5_TYOUGOURYOUKIKAKU_TOP:
            case GXHDO102B020Const.BTN_YZ5_TYOUGOURYOUKIKAKU_BOTTOM:
                method = "setYz5Tyougouryoukikaku";
                break;
            // 溶剤⑥_調合量規格計算
            case GXHDO102B020Const.BTN_YZ6_TYOUGOURYOUKIKAKU_TOP:
            case GXHDO102B020Const.BTN_YZ6_TYOUGOURYOUKIKAKU_BOTTOM:
                method = "setYz6Tyougouryoukikaku";
                break;
            // 溶剤⑦_調合量規格計算
            case GXHDO102B020Const.BTN_YZ7_TYOUGOURYOUKIKAKU_TOP:
            case GXHDO102B020Const.BTN_YZ7_TYOUGOURYOUKIKAKU_BOTTOM:
                method = "setYz7Tyougouryoukikaku";
                break;
            // 溶剤⑧_調合量規格計算
            case GXHDO102B020Const.BTN_YZ8_TYOUGOURYOUKIKAKU_TOP:
            case GXHDO102B020Const.BTN_YZ8_TYOUGOURYOUKIKAKU_BOTTOM:
                method = "setYz8Tyougouryoukikaku";
                break;
            // 溶剤⑨_調合量規格計算
            case GXHDO102B020Const.BTN_YZ9_TYOUGOURYOUKIKAKU_TOP:
            case GXHDO102B020Const.BTN_YZ9_TYOUGOURYOUKIKAKU_BOTTOM:
                method = "setYz9Tyougouryoukikaku";
                break;
            // 溶剤秤量終了日時
            case GXHDO102B020Const.BTN_YOUZAIHYORYOSYURYO_TOP:
            case GXHDO102B020Const.BTN_YOUZAIHYORYOSYURYO_BOTTOM:
                method = "setYouzaihyoryosyuryoDateTime";
                break;
            // 撹拌開始日時
            case GXHDO102B020Const.BTN_KAKUHANKAISI_TOP:
            case GXHDO102B020Const.BTN_KAKUHANKAISI_BOTTOM:
                method = "setKakuhankaisiDateTime";
                break;
            // 撹拌終了日時
            case GXHDO102B020Const.BTN_KAKUHANSYUURYOU_TOP:
            case GXHDO102B020Const.BTN_KAKUHANSYUURYOU_BOTTOM:
                method = "setKakuhansyuuryouDateTime";
                break;
            // 添加材ｽﾗﾘｰ秤量開始日時
            case GXHDO102B020Const.BTN_TENKAZAISLURRYHRKAISI_TOP:
            case GXHDO102B020Const.BTN_TENKAZAISLURRYHRKAISI_BOTTOM:
                method = "setTenkazaislurryhrkaisiDateTime";
                break;
            // 添加材ｽﾗﾘｰ_調合量規格計算
            case GXHDO102B020Const.BTN_TENKAZAISLY_TGRKIKAKU_TOP:
            case GXHDO102B020Const.BTN_TENKAZAISLY_TGRKIKAKU_BOTTOM:
                method = "setTenkazaislytgrkikaku";
                break;
            // 添加材ｽﾗﾘｰ秤量終了日時
            case GXHDO102B020Const.BTN_TENKAZAISLYHRSYUURYOU_TOP:
            case GXHDO102B020Const.BTN_TENKAZAISLYHRSYUURYOU_BOTTOM:
                method = "setTenkazaislyhrsyuuryouDateTime";
                break;
            // 分散材1_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B020Const.BTN_OPENC009SUBGAMEN1:
                method = "openC009SubGamen1";
                break;
            // 分散材2_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B020Const.BTN_OPENC009SUBGAMEN2:
                method = "openC009SubGamen2";
                break;
            // 溶剤1_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B020Const.BTN_OPENC009SUBGAMEN3:
                method = "openC009SubGamen3";
                break;
            // 溶剤2_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B020Const.BTN_OPENC009SUBGAMEN4:
                method = "openC009SubGamen4";
                break;
            // 溶剤3_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B020Const.BTN_OPENC009SUBGAMEN5:
                method = "openC009SubGamen5";
                break;
            // 溶剤4_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B020Const.BTN_OPENC009SUBGAMEN6:
                method = "openC009SubGamen6";
                break;
            // 溶剤5_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B020Const.BTN_OPENC009SUBGAMEN7:
                method = "openC009SubGamen7";
                break;
            // 溶剤6_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B020Const.BTN_OPENC009SUBGAMEN8:
                method = "openC009SubGamen8";
                break;
            // 溶剤7_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B020Const.BTN_OPENC009SUBGAMEN9:
                method = "openC009SubGamen9";
                break;
            // 溶剤8_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B020Const.BTN_OPENC009SUBGAMEN10:
                method = "openC009SubGamen10";
                break;
            // 溶剤9_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B020Const.BTN_OPENC009SUBGAMEN11:
                method = "openC009SubGamen11";
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

            // 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量の入力項目の登録データ(仮登録時は仮登録データ)を取得
            List<SrYuudentaiYouzai> srYuudentaiYouzaiDataList = getSrYuudentaiYouzaiData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo9, oyalotEdaban);
            if (srYuudentaiYouzaiDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }
            // 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_ｻﾌﾞ画面データ取得
            List<SubSrYuudentaiYouzai> subSrYuudentaiYouzaiDataList = getSubSrYuudentaiYouzaiData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo9, oyalotEdaban);
            if (subSrYuudentaiYouzaiDataList.isEmpty() || subSrYuudentaiYouzaiDataList.size() != 11) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }
            // メイン画面データ設定
            setInputItemDataMainForm(processData, srYuudentaiYouzaiDataList.get(0));
            // 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_ｻﾌﾞ画面データ設定
            setInputItemDataSubFormC009(processData, subSrYuudentaiYouzaiDataList);

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

            List<String> tyogouryoukikaku = Arrays.asList(GXHDO102B020Const.YOUZAI4_TYOUGOURYOUKIKAKU, GXHDO102B020Const.YOUZAI5_TYOUGOURYOUKIKAKU,
                    GXHDO102B020Const.YOUZAI6_TYOUGOURYOUKIKAKU, GXHDO102B020Const.YOUZAI7_TYOUGOURYOUKIKAKU, GXHDO102B020Const.YOUZAI8_TYOUGOURYOUKIKAKU, 
                    GXHDO102B020Const.YOUZAI9_TYOUGOURYOUKIKAKU, GXHDO102B020Const.TENKAZAISLURRY_TGRKIKAKU
            );
            // 項目の規格値を設置
            if (tyogouryoukikaku.contains(kikakuFxhdd01.getItemId())) {
                itemFxhdd01Clone.setKikakuChi(kikakuFxhdd01.getValue());
            } else {
                itemFxhdd01Clone.setKikakuChi(kikakuFxhdd01.getKikakuChi());
            }
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
        List<String> tyogouryoukikaku = Arrays.asList(GXHDO102B020Const.ZUNSANZAI1_TYOUGOURYOUKIKAKU, GXHDO102B020Const.ZUNSANZAI2_TYOUGOURYOUKIKAKU,
                GXHDO102B020Const.YOUZAI1_TYOUGOURYOUKIKAKU, GXHDO102B020Const.YOUZAI2_TYOUGOURYOUKIKAKU, GXHDO102B020Const.YOUZAI3_TYOUGOURYOUKIKAKU,
                GXHDO102B020Const.YOUZAI4_TYOUGOURYOUKIKAKU, GXHDO102B020Const.YOUZAI5_TYOUGOURYOUKIKAKU, GXHDO102B020Const.YOUZAI6_TYOUGOURYOUKIKAKU,
                GXHDO102B020Const.YOUZAI7_TYOUGOURYOUKIKAKU, GXHDO102B020Const.YOUZAI8_TYOUGOURYOUKIKAKU, GXHDO102B020Const.YOUZAI9_TYOUGOURYOUKIKAKU,
                GXHDO102B020Const.TENKAZAISLURRY_TGRKIKAKU
        );
        List<String> zunsanzai1_tyougouryouList = Arrays.asList(GXHDO102B020Const.ZUNSANZAI1_TYOUGOURYOU1, GXHDO102B020Const.ZUNSANZAI1_TYOUGOURYOU2); // 分散材1_調合量
        List<String> zunsanzai2_tyougouryouList = Arrays.asList(GXHDO102B020Const.ZUNSANZAI2_TYOUGOURYOU1, GXHDO102B020Const.ZUNSANZAI2_TYOUGOURYOU2); // 分散材2_調合量
        List<String> youzai1_tyougouryouList = Arrays.asList(GXHDO102B020Const.YOUZAI1_TYOUGOURYOU1, GXHDO102B020Const.YOUZAI1_TYOUGOURYOU2); // 溶剤1_調合量
        List<String> youzai2_tyougouryouList = Arrays.asList(GXHDO102B020Const.YOUZAI2_TYOUGOURYOU1, GXHDO102B020Const.YOUZAI2_TYOUGOURYOU2); // 溶剤2_調合量
        List<String> youzai3_tyougouryouList = Arrays.asList(GXHDO102B020Const.YOUZAI3_TYOUGOURYOU1, GXHDO102B020Const.YOUZAI3_TYOUGOURYOU2); // 溶剤3_調合量
        List<String> youzai4_tyougouryouList = Arrays.asList(GXHDO102B020Const.YOUZAI4_TYOUGOURYOU1, GXHDO102B020Const.YOUZAI4_TYOUGOURYOU2); // 溶剤4_調合量
        List<String> youzai5_tyougouryouList = Arrays.asList(GXHDO102B020Const.YOUZAI5_TYOUGOURYOU1, GXHDO102B020Const.YOUZAI5_TYOUGOURYOU2); // 溶剤5_調合量
        List<String> youzai6_tyougouryouList = Arrays.asList(GXHDO102B020Const.YOUZAI6_TYOUGOURYOU1, GXHDO102B020Const.YOUZAI6_TYOUGOURYOU2); // 溶剤6_調合量
        List<String> youzai7_tyougouryouList = Arrays.asList(GXHDO102B020Const.YOUZAI7_TYOUGOURYOU1, GXHDO102B020Const.YOUZAI7_TYOUGOURYOU2); // 溶剤7_調合量
        List<String> youzai8_tyougouryouList = Arrays.asList(GXHDO102B020Const.YOUZAI8_TYOUGOURYOU1, GXHDO102B020Const.YOUZAI8_TYOUGOURYOU2); // 溶剤8_調合量
        List<String> youzai9_tyougouryouList = Arrays.asList(GXHDO102B020Const.YOUZAI9_TYOUGOURYOU1, GXHDO102B020Const.YOUZAI9_TYOUGOURYOU2); // 溶剤9_調合量
        List<String> tenkazaislurry_tyougouryouList = Arrays.asList(GXHDO102B020Const.TENKAZAISLURRY_TYOUGOURYOU1, GXHDO102B020Const.TENKAZAISLURRY_TYOUGOURYOU2); // 添加材ｽﾗﾘｰ_調合量

        // 規格値の入力値チェック必要の項目リスト
        List<FXHDD01> itemList = new ArrayList<>();
        setKikakuValueAndLabel1(processData, itemList, zunsanzai1_tyougouryouList, tyogouryoukikaku.get(0), "分散材①_調合量"); // 分散材①_調合量の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, zunsanzai2_tyougouryouList, tyogouryoukikaku.get(1), "分散材②_調合量"); // 分散材②_調合量の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, youzai1_tyougouryouList, tyogouryoukikaku.get(2), "溶剤①_調合量"); // 溶剤①_調合量の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, youzai2_tyougouryouList, tyogouryoukikaku.get(3), "溶剤②_調合量"); // 溶剤②_調合量の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, youzai3_tyougouryouList, tyogouryoukikaku.get(4), "溶剤③_調合量"); // 溶剤③_調合量の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, youzai4_tyougouryouList, tyogouryoukikaku.get(5), "溶剤④_調合量"); // 溶剤④_調合量の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, youzai5_tyougouryouList, tyogouryoukikaku.get(6), "溶剤⑤_調合量"); // 溶剤⑤_調合量の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, youzai6_tyougouryouList, tyogouryoukikaku.get(7), "溶剤⑥_調合量"); // 溶剤⑥_調合量の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, youzai7_tyougouryouList, tyogouryoukikaku.get(8), "溶剤⑦_調合量"); // 溶剤⑦_調合量の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, youzai8_tyougouryouList, tyogouryoukikaku.get(9), "溶剤⑧_調合量"); // 溶剤⑧_調合量の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, youzai9_tyougouryouList, tyogouryoukikaku.get(10), "溶剤⑨_調合量"); // 溶剤⑨_調合量の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, tenkazaislurry_tyougouryouList, tyogouryoukikaku.get(11), "添加材ｽﾗﾘｰ_調合量"); // 添加材ｽﾗﾘｰ_調合量の規格値と表示ﾗﾍﾞﾙ1を設置

        tyougouryouList.addAll(zunsanzai1_tyougouryouList);
        tyougouryouList.addAll(zunsanzai2_tyougouryouList);
        tyougouryouList.addAll(youzai1_tyougouryouList);
        tyougouryouList.addAll(youzai2_tyougouryouList);
        tyougouryouList.addAll(youzai3_tyougouryouList);
        tyougouryouList.addAll(youzai4_tyougouryouList);
        tyougouryouList.addAll(youzai5_tyougouryouList);
        tyougouryouList.addAll(youzai6_tyougouryouList);
        tyougouryouList.addAll(youzai7_tyougouryouList);
        tyougouryouList.addAll(youzai8_tyougouryouList);
        tyougouryouList.addAll(youzai9_tyougouryouList);
        tyougouryouList.addAll(tenkazaislurry_tyougouryouList);

        // 撹拌時間ﾁｪｯｸ
        List<String> kakuhanjikanList = Arrays.asList(GXHDO102B020Const.KAKUHANKAISI_DAY, GXHDO102B020Const.KAKUHANKAISI_TIME, GXHDO102B020Const.KAKUHANSYUURYOU_DAY, GXHDO102B020Const.KAKUHANSYUURYOU_TIME);
        if (diffMinutesCheckFlg) {
            // 撹拌時間の規格チェック用項目
            FXHDD01 kakuhanjikanDiffMinutes = getdiffMinutesKikakuItem(processData, GXHDO102B020Const.KAKUHANJIKAN, kakuhanjikanList);
            // 項目の項目名を設置
            if (kakuhanjikanDiffMinutes != null) {
                kakuhanjikanDiffMinutes.setLabel1("撹拌時間");
                itemList.add(kakuhanjikanDiffMinutes);
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
            setItemBackColor(processData, kakuhanjikanList, "撹拌時間", kikakuchiInputErrorInfoList, errorMessageInfo);
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

                // 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量_仮登録処理
                insertTmpSrYuudentaiYouzai(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, strSystime, processData);
                // 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力ｻﾌﾞ画面の仮登録処理
                for (int i = 1; i <= 11; i++) {
                    insertTmpSubSrYuudentaiYouzai(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, i, strSystime, processData);
                }
            } else {

                // 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量_仮登録更新処理
                SrYuudentaiYouzai srYuudentaiYouzai = updateTmpSrYuudentaiYouzai(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);
                // 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力ｻﾌﾞ画面の仮登録更新処理
                for (int i = 1; i <= 11; i++) {
                    updateTmpSubSrYuudentaiYouzai(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, i, strSystime, srYuudentaiYouzai, processData);
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
        ValidateUtil validateUtil = new ValidateUtil();
        // 溶剤秤量開始日時、溶剤秤量終了日時前後チェック
        FXHDD01 itemKaisiDay = getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAIHYOURYOUKAISI_DAY); // 溶剤秤量開始日
        FXHDD01 itemKaisiTime = getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAIHYOURYOUKAISI_TIME); // 溶剤秤量開始時間
        FXHDD01 itemSyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAIHYOURYOUSYUURYOU_DAY); // 溶剤秤量終了日
        FXHDD01 itemSyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAIHYOURYOUSYUURYOU_TIME); // 溶剤秤量終了時間
        if (itemKaisiDay != null && itemKaisiTime != null && itemSyuuryouDay != null && itemSyuuryouTime != null) {
            Date kaisiDate = DateUtil.convertStringToDate(itemKaisiDay.getValue(), itemKaisiTime.getValue());
            Date syuuryouDate = DateUtil.convertStringToDate(itemSyuuryouDay.getValue(), itemSyuuryouTime.getValue());
            //R001チェック呼出し
            String msgCheckR001 = validateUtil.checkR001(itemKaisiDay.getLabel1() + "時", kaisiDate, itemSyuuryouDay.getLabel1() + "時", syuuryouDate);
            if (!StringUtil.isEmpty(msgCheckR001)) {
                //エラー発生時
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemKaisiDay, itemKaisiTime, itemSyuuryouDay, itemSyuuryouTime);
                return MessageUtil.getErrorMessageInfo("", msgCheckR001, true, true, errFxhdd01List);
            }
        }

        // 撹拌開始日時、撹拌終了日時前後チェック
        itemKaisiDay = getItemRow(processData.getItemList(), GXHDO102B020Const.KAKUHANKAISI_DAY); // 撹拌開始日
        itemKaisiTime = getItemRow(processData.getItemList(), GXHDO102B020Const.KAKUHANKAISI_TIME); // 撹拌開始時間
        itemSyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B020Const.KAKUHANSYUURYOU_DAY); // 撹拌終了日
        itemSyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B020Const.KAKUHANSYUURYOU_TIME); // 撹拌終了時間
        if (itemKaisiDay != null && itemKaisiTime != null && itemSyuuryouDay != null && itemSyuuryouTime != null) {
            Date kaisiDate = DateUtil.convertStringToDate(itemKaisiDay.getValue(), itemKaisiTime.getValue());
            Date syuuryouDate = DateUtil.convertStringToDate(itemSyuuryouDay.getValue(), itemSyuuryouTime.getValue());
            //R001チェック呼出し
            String msgCheckR001 = validateUtil.checkR001(itemKaisiDay.getLabel1() + "時", kaisiDate, itemSyuuryouDay.getLabel1() + "時", syuuryouDate);
            if (!StringUtil.isEmpty(msgCheckR001)) {
                //エラー発生時
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemKaisiDay, itemKaisiTime, itemSyuuryouDay, itemSyuuryouTime);
                return MessageUtil.getErrorMessageInfo("", msgCheckR001, true, true, errFxhdd01List);
            }
        }

        // 添加材ｽﾗﾘｰ秤量開始日時、添加材ｽﾗﾘｰ秤量終了日時前後チェック
        itemKaisiDay = getItemRow(processData.getItemList(), GXHDO102B020Const.TENKAZAISLURRYHRKAISI_DAY); // 添加材ｽﾗﾘｰ秤量開始日
        itemKaisiTime = getItemRow(processData.getItemList(), GXHDO102B020Const.TENKAZAISLURRYHRKAISI_TIME); // 添加材ｽﾗﾘｰ秤量開始時間
        itemSyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B020Const.TENKAZAISLURRYHRSYURYO_DAY); // 添加材ｽﾗﾘｰ秤量終了日
        itemSyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B020Const.TENKAZAISLURRYHRSYURYO_TIME); // 添加材ｽﾗﾘｰ秤量終了時間
        if (itemKaisiDay != null && itemKaisiTime != null && itemSyuuryouDay != null && itemSyuuryouTime != null) {
            Date kaisiDate = DateUtil.convertStringToDate(itemKaisiDay.getValue(), itemKaisiTime.getValue());
            Date syuuryouDate = DateUtil.convertStringToDate(itemSyuuryouDay.getValue(), itemSyuuryouTime.getValue());
            //R001チェック呼出し
            String msgCheckR001 = validateUtil.checkR001(itemKaisiDay.getLabel1() + "時", kaisiDate, itemSyuuryouDay.getLabel1() + "時", syuuryouDate);
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
            SrYuudentaiYouzai tmpSrYuudentaiYouzai = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrYuudentaiYouzai> srYuudentaiYouzaiList = getSrYuudentaiYouzaiData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo9, edaban);
                if (!srYuudentaiYouzaiList.isEmpty()) {
                    tmpSrYuudentaiYouzai = srYuudentaiYouzaiList.get(0);
                }

                deleteTmpSrYuudentaiYouzai(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);
                deleteTmpSubSrYuudentaiYouzai(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);
            }

            // 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量_登録処理
            insertSrYuudentaiYouzai(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, strSystime, processData, tmpSrYuudentaiYouzai);
            // 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力ｻﾌﾞ画面の仮登録更新処理
            for (int i = 1; i <= 11; i++) {
                insertSubSrYuudentaiYouzai(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, i, strSystime, processData, tmpSrYuudentaiYouzai);
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
        processData.setUserAuthParam(GXHDO102B020Const.USER_AUTH_UPDATE_PARAM);

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

            // 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量_更新処理
            SrYuudentaiYouzai srYuudentaiYouzai = updateSrYuudentaiYouzai(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);
            // 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力ｻﾌﾞ画面の更新処理
            for (int i = 1; i <= 11; i++) {
                updateSubSrYuudentaiYouzai(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, i, strSystime, srYuudentaiYouzai, processData);
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
        processData.setUserAuthParam(GXHDO102B020Const.USER_AUTH_DELETE_PARAM);

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

            // 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo9, edaban, paramJissekino);
            insertDeleteDataTmpSrYuudentaiYouzai(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo9, edaban, strSystime);

            // 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_ｻﾌﾞ画面仮登録登録処理
            insertDeleteDataTmpSubSrYuudentaiYouzai(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo9, edaban, strSystime);

            // 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量_削除処理
            deleteSrYuudentaiYouzai(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);

            // 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_ｻﾌﾞ画面削除処理
            deleteSubSrYuudentaiYouzai(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);

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
     * 溶剤秤量開始日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setYouzaihyouryoukaisiDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAIHYOURYOUKAISI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAIHYOURYOUKAISI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 溶剤④_調合量規格計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setYz4Tyougouryoukikaku(ProcessData processData) {
        // 【溶剤④_調合量規格計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkYz4To9TyougouryoukikakuKeisan(processData, GXHDO102B020Const.YOUZAI4_TYOUGOURYOUKIKAKU);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        // 【溶剤④_調合量規格計算】ﾎﾞﾀﾝ押下時計算処理
        calcYz4To9Tyougouryoukikaku(processData, GXHDO102B020Const.YOUZAI4_TYOUGOURYOUKIKAKU);
        processData.setMethod("");
        return processData;
    }

    /**
     * 【溶剤④-⑨_調合量規格計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
     *
     * @param processData 処理制御データ
     * @param youzaiTyougouryoukikakuItemId 溶剤④-⑨_調合量規格項目ID
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkYz4To9TyougouryoukikakuKeisan(ProcessData processData, String youzaiTyougouryoukikakuItemId) {
        FXHDD01 itemTenkazaislurry_tyougouryou1 = getItemRow(processData.getItemList(), GXHDO102B020Const.TENKAZAISLURRY_TYOUGOURYOU1); // 添加材ｽﾗﾘｰ_調合量1
        FXHDD01 itemTenkazaislurry_tyougouryou2 = getItemRow(processData.getItemList(), GXHDO102B020Const.TENKAZAISLURRY_TYOUGOURYOU2); // 添加材ｽﾗﾘｰ_調合量2
        // 「添加材ｽﾗﾘｰ_調合量1」ﾁｪｯｸ
        if (itemTenkazaislurry_tyougouryou1 != null && StringUtil.isEmpty(itemTenkazaislurry_tyougouryou1.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemTenkazaislurry_tyougouryou1);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemTenkazaislurry_tyougouryou1.getLabel1());
        }
        // 「添加材ｽﾗﾘｰ_調合量2」ﾁｪｯｸ
        if (itemTenkazaislurry_tyougouryou2 != null && StringUtil.isEmpty(itemTenkazaislurry_tyougouryou2.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemTenkazaislurry_tyougouryou2);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemTenkazaislurry_tyougouryou2.getLabel1());
        }
        // 「溶剤X_調合量の規格値」ﾁｪｯｸ:数値以外の場合ｴﾗｰ
        FXHDD01 itemYouzaiTyougouryoukikaku = getItemRow(processData.getItemList(), youzaiTyougouryoukikakuItemId); // 溶剤④-⑨_調合量規格
        BigDecimal itemYouzaiTyougouryoukikakuVal = ValidateUtil.getItemKikakuChiCheckVal(itemYouzaiTyougouryoukikaku); // 溶剤④-⑨_調合量規格の規格値
        if (itemYouzaiTyougouryoukikakuVal == null) {
            return MessageUtil.getErrorMessageInfo("XHD-000028", true, false, Arrays.asList(itemYouzaiTyougouryoukikaku), itemYouzaiTyougouryoukikaku.getLabel1());
        }
        return null;
    }

    /**
     * 【溶剤④-⑨_調合量規格計算】ﾎﾞﾀﾝ押下時計算処理
     *
     * @param processData 処理制御データ
     * @param youzaiTyougouryoukikakuItemId 溶剤④-⑨_調合量規格項目ID
     */
    private void calcYz4To9Tyougouryoukikaku(ProcessData processData, String youzaiTyougouryoukikakuItemId) {
        FXHDD01 itemYouzaiTyougouryoukikaku = getItemRow(processData.getItemList(), youzaiTyougouryoukikakuItemId); // 溶剤④-⑨_調合量規格
        try {
            FXHDD01 itemTenkazaislurry_tyougouryou1 = getItemRow(processData.getItemList(), GXHDO102B020Const.TENKAZAISLURRY_TYOUGOURYOU1); // 添加材ｽﾗﾘｰ_調合量1
            FXHDD01 itemTenkazaislurry_tyougouryou2 = getItemRow(processData.getItemList(), GXHDO102B020Const.TENKAZAISLURRY_TYOUGOURYOU2); // 添加材ｽﾗﾘｰ_調合量2
            BigDecimal itemYouzaiTyougouryoukikakuVal = ValidateUtil.getItemKikakuChiCheckVal(itemYouzaiTyougouryoukikaku); // 溶剤④-⑨_調合量規格の規格値
            BigDecimal itemTenkazaislurry_tyougouryou1Val = BigDecimal.ZERO;  // 添加材ｽﾗﾘｰ_調合量1
            BigDecimal itemTenkazaislurry_tyougouryou2Val = BigDecimal.ZERO;  // 添加材ｽﾗﾘｰ_調合量2
            if(itemTenkazaislurry_tyougouryou1 != null){
                itemTenkazaislurry_tyougouryou1Val = new BigDecimal(itemTenkazaislurry_tyougouryou1.getValue()); // 添加材ｽﾗﾘｰ_調合量1
            }
            if(itemTenkazaislurry_tyougouryou2 != null){
                itemTenkazaislurry_tyougouryou2Val = new BigDecimal(itemTenkazaislurry_tyougouryou2.getValue()); // 添加材ｽﾗﾘｰ_調合量2
            }
            // (「規格値」 - (「添加材ｽﾗﾘｰ_調合量1」 + 「添加材ｽﾗﾘｰ_調合量2」)) ÷ 2  を算出する。
            BigDecimal itemYouzaiTyougouryouVal = itemYouzaiTyougouryoukikakuVal.subtract(itemTenkazaislurry_tyougouryou1Val.add(itemTenkazaislurry_tyougouryou2Val)).divide(
                    new BigDecimal(2), 0, RoundingMode.HALF_UP);
            itemYouzaiTyougouryoukikaku.setValue("【" + itemYouzaiTyougouryouVal.toPlainString() + "±0" + "】");

        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
            ErrUtil.outputErrorLog(itemYouzaiTyougouryoukikaku.getLabel1() + "計算処理にエラー発生", ex, LOGGER);
        }
    }

    /**
     * 溶剤⑤_調合量規格計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setYz5Tyougouryoukikaku(ProcessData processData) {
        // 【溶剤⑤_調合量規格計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkYz4To9TyougouryoukikakuKeisan(processData, GXHDO102B020Const.YOUZAI5_TYOUGOURYOUKIKAKU);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        // 【溶剤⑤_調合量規格計算】ﾎﾞﾀﾝ押下時計算処理
        calcYz4To9Tyougouryoukikaku(processData, GXHDO102B020Const.YOUZAI5_TYOUGOURYOUKIKAKU);
        processData.setMethod("");
        return processData;
    }

    /**
     * 溶剤⑥_調合量規格計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setYz6Tyougouryoukikaku(ProcessData processData) {
        // 【溶剤⑥_調合量規格計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkYz4To9TyougouryoukikakuKeisan(processData, GXHDO102B020Const.YOUZAI6_TYOUGOURYOUKIKAKU);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        // 【溶剤⑥_調合量規格計算】ﾎﾞﾀﾝ押下時計算処理
        calcYz4To9Tyougouryoukikaku(processData, GXHDO102B020Const.YOUZAI6_TYOUGOURYOUKIKAKU);
        processData.setMethod("");
        return processData;
    }

    /**
     * 溶剤⑦_調合量規格計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setYz7Tyougouryoukikaku(ProcessData processData) {
        // 【溶剤⑦_調合量規格計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkYz4To9TyougouryoukikakuKeisan(processData, GXHDO102B020Const.YOUZAI7_TYOUGOURYOUKIKAKU);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        // 【溶剤⑦_調合量規格計算】ﾎﾞﾀﾝ押下時計算処理
        calcYz4To9Tyougouryoukikaku(processData, GXHDO102B020Const.YOUZAI7_TYOUGOURYOUKIKAKU);
        processData.setMethod("");
        return processData;
    }

    /**
     * 溶剤⑧_調合量規格計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setYz8Tyougouryoukikaku(ProcessData processData) {
        // 【溶剤⑧_調合量規格計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkYz4To9TyougouryoukikakuKeisan(processData, GXHDO102B020Const.YOUZAI8_TYOUGOURYOUKIKAKU);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        // 【溶剤⑧_調合量規格計算】ﾎﾞﾀﾝ押下時計算処理
        calcYz4To9Tyougouryoukikaku(processData, GXHDO102B020Const.YOUZAI8_TYOUGOURYOUKIKAKU);
        processData.setMethod("");
        return processData;
    }

    /**
     * 溶剤⑨_調合量規格計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setYz9Tyougouryoukikaku(ProcessData processData) {
        // 【溶剤⑨_調合量規格計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkYz4To9TyougouryoukikakuKeisan(processData, GXHDO102B020Const.YOUZAI9_TYOUGOURYOUKIKAKU);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        // 【溶剤⑨_調合量規格計算】ﾎﾞﾀﾝ押下時計算処理
        calcYz4To9Tyougouryoukikaku(processData, GXHDO102B020Const.YOUZAI9_TYOUGOURYOUKIKAKU);
        processData.setMethod("");
        return processData;
    }

    /**
     * 溶剤秤量終了日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setYouzaihyoryosyuryoDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAIHYOURYOUSYUURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAIHYOURYOUSYUURYOU_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 撹拌開始日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKakuhankaisiDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B020Const.KAKUHANKAISI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B020Const.KAKUHANKAISI_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
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
    public ProcessData setKakuhansyuuryouDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B020Const.KAKUHANSYUURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B020Const.KAKUHANSYUURYOU_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 添加材ｽﾗﾘｰ秤量開始日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setTenkazaislurryhrkaisiDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B020Const.TENKAZAISLURRYHRKAISI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B020Const.TENKAZAISLURRYHRKAISI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 添加材ｽﾗﾘｰ_調合量規格計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     * @throws SQLException 例外エラー
     */
    public ProcessData setTenkazaislytgrkikaku(ProcessData processData) throws SQLException {
        Map sryuudentaitenkazaiData = new HashMap();
        // 【添加材ｽﾗﾘｰ_調合量規格計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkTenkazaislytgrkikakuKeisan(processData, sryuudentaitenkazaiData);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        // 【添加材ｽﾗﾘｰ_調合量規格計算】ﾎﾞﾀﾝ押下時計算処理
        calcTenkazaislytgrkikaku(processData, sryuudentaitenkazaiData);
        processData.setMethod("");
        return processData;
    }

    /**
     * 【添加材ｽﾗﾘｰ_調合量規格計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
     *
     * @param processData 処理制御データ
     * @param sryuudentaitenkazaiData 誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定ﾃﾞｰﾀ
     * @return エラーメッセージ情報
     * @throws SQLException 例外エラー
     */
    private ErrorMessageInfo checkTenkazaislytgrkikakuKeisan(ProcessData processData, Map sryuudentaitenkazaiData) throws SQLException {
        QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
        FXHDD01 itemTenkazaislurry_wiplotno = getItemRow(processData.getItemList(), GXHDO102B020Const.TENKAZAISLURRY_WIPLOTNO); // 添加材ｽﾗﾘｰ_WIPﾛｯﾄNo
        // 「添加材ｽﾗﾘｰ_WIPﾛｯﾄNo」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemTenkazaislurry_wiplotno.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemTenkazaislurry_wiplotno);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemTenkazaislurry_wiplotno.getLabel1());
        }

        // [誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定]から、ﾃﾞｰﾀを取得
        Map data = loadSryuudentaitenkazaiData(queryRunnerQcdb, itemTenkazaislurry_wiplotno.getValue());
        if (data == null || data.get("kokeibunhirituheikin") == null) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            return MessageUtil.getErrorMessageInfo("XHD-000223", true, false, null);
        }
        BigDecimal kokeibunhirituheikin = (BigDecimal) DBUtil.stringToBigDecimalObject(data.get("kokeibunhirituheikin").toString());
        if (BigDecimal.ZERO.compareTo(kokeibunhirituheikin) == 0) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            return MessageUtil.getErrorMessageInfo("XHD-000223", true, false, null);
        }
        sryuudentaitenkazaiData.put("kokeibunhirituheikin", data.get("kokeibunhirituheikin"));
        
        // 「添加材ｽﾗﾘｰ_調合量規格」ﾁｪｯｸ:数値以外の場合ｴﾗｰ
        FXHDD01 itemTenkazaislurry_tgrkikaku = getItemRow(processData.getItemList(), GXHDO102B020Const.TENKAZAISLURRY_TGRKIKAKU); // 添加材ｽﾗﾘｰ_調合量規格
        BigDecimal itemYouzaiTyougouryoukikakuVal = ValidateUtil.getItemKikakuChiCheckVal(itemTenkazaislurry_tgrkikaku); // 溶剤④-⑨_調合量規格の規格値
        if (itemYouzaiTyougouryoukikakuVal == null) {
            return MessageUtil.getErrorMessageInfo("XHD-000028", true, false, Arrays.asList(itemTenkazaislurry_tgrkikaku), itemTenkazaislurry_tgrkikaku.getLabel1());
        }
        return null;
    }

    /**
     * [誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb オブジェクト
     * @param tenkazaislurryWiplotno 添加材ｽﾗﾘｰ_WIPﾛｯﾄNo
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadSryuudentaitenkazaiData(QueryRunner queryRunnerQcdb, String tenkazaislurryWiplotno) throws SQLException {
        if (tenkazaislurryWiplotno.length() < 15) {
            return null;
        }
        // ﾊﾟﾗﾒｰﾀﾏｽﾀデータの取得
        String sql = "SELECT kokeibunhirituheikin "
                + " FROM sr_yuudentai_tenkazai "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ?  ";
        List<Object> params = new ArrayList<>();
        params.add(tenkazaislurryWiplotno.substring(0, 3));
        params.add(tenkazaislurryWiplotno.substring(3, 12));
        params.add(tenkazaislurryWiplotno.substring(12, 15));
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * 【添加材ｽﾗﾘｰ_調合量規格計算】ﾎﾞﾀﾝ押下時計算処理
     *
     * @param processData 処理制御データ
     */
    private void calcTenkazaislytgrkikaku(ProcessData processData, Map sryuudentaitenkazaiData) {
        try {
            FXHDD01 itemTenkazaislurry_tgrkikaku = getItemRow(processData.getItemList(), GXHDO102B020Const.TENKAZAISLURRY_TGRKIKAKU); // 添加材ｽﾗﾘｰ_調合量規格
            BigDecimal kokeibunhirituheikin = (BigDecimal) DBUtil.stringToBigDecimalObject(sryuudentaitenkazaiData.get("kokeibunhirituheikin").toString()); // 固形分比率平均
            BigDecimal itemTenkazaislurry_tgrkikakuVal = ValidateUtil.getItemKikakuChiCheckVal(itemTenkazaislurry_tgrkikaku); // 添加材ｽﾗﾘｰ_調合量規格の規格値
            // 「規格値」÷ 「固形分比率平均」 × 100 を算出する。(小数点第一位を四捨五入) → 式を変換して先に100を乗算
            BigDecimal budomari = itemTenkazaislurry_tgrkikakuVal.multiply(BigDecimal.valueOf(100)).divide(kokeibunhirituheikin, 0, RoundingMode.HALF_UP);

            //計算結果を添加材ｽﾗﾘｰ_調合量規格にセット
            itemTenkazaislurry_tgrkikaku.setValue("【" + budomari.toPlainString() + "±0" + "】");

        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
            ErrUtil.outputErrorLog("添加材ｽﾗﾘｰ_調合量規格計算にエラー発生", ex, LOGGER);
        }
    }

    /**
     * 添加材ｽﾗﾘｰ秤量終了日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setTenkazaislyhrsyuuryouDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B020Const.TENKAZAISLURRYHRSYURYO_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B020Const.TENKAZAISLURRYHRSYURYO_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
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
                        GXHDO102B020Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B020Const.BTN_YOUZAIHYOURYOUKAISI_TOP,
                        GXHDO102B020Const.BTN_YZ4_TYOUGOURYOUKIKAKU_TOP,
                        GXHDO102B020Const.BTN_YZ5_TYOUGOURYOUKIKAKU_TOP,
                        GXHDO102B020Const.BTN_YZ6_TYOUGOURYOUKIKAKU_TOP,
                        GXHDO102B020Const.BTN_YZ7_TYOUGOURYOUKIKAKU_TOP,
                        GXHDO102B020Const.BTN_YZ8_TYOUGOURYOUKIKAKU_TOP,
                        GXHDO102B020Const.BTN_YZ9_TYOUGOURYOUKIKAKU_TOP,
                        GXHDO102B020Const.BTN_YOUZAIHYORYOSYURYO_TOP,
                        GXHDO102B020Const.BTN_KAKUHANKAISI_TOP,
                        GXHDO102B020Const.BTN_KAKUHANSYUURYOU_TOP,
                        GXHDO102B020Const.BTN_TENKAZAISLURRYHRKAISI_TOP,
                        GXHDO102B020Const.BTN_TENKAZAISLY_TGRKIKAKU_TOP,
                        GXHDO102B020Const.BTN_TENKAZAISLYHRSYUURYOU_TOP,
                        GXHDO102B020Const.BTN_UPDATE_TOP,
                        GXHDO102B020Const.BTN_DELETE_TOP,
                        GXHDO102B020Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B020Const.BTN_YOUZAIHYOURYOUKAISI_BOTTOM,
                        GXHDO102B020Const.BTN_YZ4_TYOUGOURYOUKIKAKU_BOTTOM,
                        GXHDO102B020Const.BTN_YZ5_TYOUGOURYOUKIKAKU_BOTTOM,
                        GXHDO102B020Const.BTN_YZ6_TYOUGOURYOUKIKAKU_BOTTOM,
                        GXHDO102B020Const.BTN_YZ7_TYOUGOURYOUKIKAKU_BOTTOM,
                        GXHDO102B020Const.BTN_YZ8_TYOUGOURYOUKIKAKU_BOTTOM,
                        GXHDO102B020Const.BTN_YZ9_TYOUGOURYOUKIKAKU_BOTTOM,
                        GXHDO102B020Const.BTN_YOUZAIHYORYOSYURYO_BOTTOM,
                        GXHDO102B020Const.BTN_KAKUHANKAISI_BOTTOM,
                        GXHDO102B020Const.BTN_KAKUHANSYUURYOU_BOTTOM,
                        GXHDO102B020Const.BTN_TENKAZAISLURRYHRKAISI_BOTTOM,
                        GXHDO102B020Const.BTN_TENKAZAISLY_TGRKIKAKU_BOTTOM,
                        GXHDO102B020Const.BTN_TENKAZAISLYHRSYUURYOU_BOTTOM,
                        GXHDO102B020Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B020Const.BTN_DELETE_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B020Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B020Const.BTN_INSERT_TOP,
                        GXHDO102B020Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B020Const.BTN_INSERT_BOTTOM));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO102B020Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B020Const.BTN_YOUZAIHYOURYOUKAISI_TOP,
                        GXHDO102B020Const.BTN_YZ4_TYOUGOURYOUKIKAKU_TOP,
                        GXHDO102B020Const.BTN_YZ5_TYOUGOURYOUKIKAKU_TOP,
                        GXHDO102B020Const.BTN_YZ6_TYOUGOURYOUKIKAKU_TOP,
                        GXHDO102B020Const.BTN_YZ7_TYOUGOURYOUKIKAKU_TOP,
                        GXHDO102B020Const.BTN_YZ8_TYOUGOURYOUKIKAKU_TOP,
                        GXHDO102B020Const.BTN_YZ9_TYOUGOURYOUKIKAKU_TOP,
                        GXHDO102B020Const.BTN_YOUZAIHYORYOSYURYO_TOP,
                        GXHDO102B020Const.BTN_KAKUHANKAISI_TOP,
                        GXHDO102B020Const.BTN_KAKUHANSYUURYOU_TOP,
                        GXHDO102B020Const.BTN_TENKAZAISLURRYHRKAISI_TOP,
                        GXHDO102B020Const.BTN_TENKAZAISLY_TGRKIKAKU_TOP,
                        GXHDO102B020Const.BTN_TENKAZAISLYHRSYUURYOU_TOP,
                        GXHDO102B020Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B020Const.BTN_INSERT_TOP,
                        GXHDO102B020Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B020Const.BTN_YOUZAIHYOURYOUKAISI_BOTTOM,
                        GXHDO102B020Const.BTN_YZ4_TYOUGOURYOUKIKAKU_BOTTOM,
                        GXHDO102B020Const.BTN_YZ5_TYOUGOURYOUKIKAKU_BOTTOM,
                        GXHDO102B020Const.BTN_YZ6_TYOUGOURYOUKIKAKU_BOTTOM,
                        GXHDO102B020Const.BTN_YZ7_TYOUGOURYOUKIKAKU_BOTTOM,
                        GXHDO102B020Const.BTN_YZ8_TYOUGOURYOUKIKAKU_BOTTOM,
                        GXHDO102B020Const.BTN_YZ9_TYOUGOURYOUKIKAKU_BOTTOM,
                        GXHDO102B020Const.BTN_YOUZAIHYORYOSYURYO_BOTTOM,
                        GXHDO102B020Const.BTN_KAKUHANKAISI_BOTTOM,
                        GXHDO102B020Const.BTN_KAKUHANSYUURYOU_BOTTOM,
                        GXHDO102B020Const.BTN_TENKAZAISLURRYHRKAISI_BOTTOM,
                        GXHDO102B020Const.BTN_TENKAZAISLY_TGRKIKAKU_BOTTOM,
                        GXHDO102B020Const.BTN_TENKAZAISLYHRSYUURYOU_BOTTOM,
                        GXHDO102B020Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B020Const.BTN_INSERT_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B020Const.BTN_UPDATE_TOP,
                        GXHDO102B020Const.BTN_DELETE_TOP,
                        GXHDO102B020Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B020Const.BTN_DELETE_BOTTOM
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
        // 品名毎の項目の表示制御
        setHinmeiItemRendered(processData);
        // 画面に取得した情報をセットする。(入力項目以外)
        setViewItemData(processData, shikakariData, lotNo);
        // 画面のラベル項目の値の背景色を取得できない場合、デフォルト値を設置
        GXHDO102C009Logic.setItemStyle(processData.getItemList());
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
        this.setItemData(processData, GXHDO102B020Const.WIPLOTNO, lotNo);
        // 誘電体ｽﾗﾘｰ品名
        this.setItemData(processData, GXHDO102B020Const.YUUDENTAISLURRYHINMEI, StringUtil.nullToBlank(getMapData(shikakariData, "hinmei")));
        // 誘電体ｽﾗﾘｰLotNo
        this.setItemData(processData, GXHDO102B020Const.YUUDENTAISLURRYLOTNO, StringUtil.nullToBlank(getMapData(shikakariData, "lotno")));
        // ﾛｯﾄ区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode"));
        // ﾛｯﾄ区分名称
        String lotkubun = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubun"));

        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO102B020Const.LOTKUBUN, "");
        } else {
            if (!StringUtil.isEmpty(lotkubun)) {
                lotkubuncode = lotkubuncode + ":" + lotkubun;
            }
            this.setItemData(processData, GXHDO102B020Const.LOTKUBUN, lotkubuncode);
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

        List<SrYuudentaiYouzai> srYuudentaiYouzaiList = new ArrayList<>();
        List<SubSrYuudentaiYouzai> subSrYuudentaiYouzaiList = new ArrayList<>();
        String rev = "";
        String jotaiFlg = "";
        String kojyo = lotNo.substring(0, 3);
        String lotNo9 = lotNo.substring(3, 12);
        String edaban = lotNo.substring(12, 15);
        List<String> tyogouryoukikaku = Arrays.asList(GXHDO102B020Const.YOUZAI4_TYOUGOURYOUKIKAKU, GXHDO102B020Const.YOUZAI5_TYOUGOURYOUKIKAKU,
                GXHDO102B020Const.YOUZAI6_TYOUGOURYOUKIKAKU, GXHDO102B020Const.YOUZAI7_TYOUGOURYOUKIKAKU, GXHDO102B020Const.YOUZAI8_TYOUGOURYOUKIKAKU,
                GXHDO102B020Const.YOUZAI9_TYOUGOURYOUKIKAKU, GXHDO102B020Const.TENKAZAISLURRY_TGRKIKAKU
        );
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
                    String inputDefaultValue = StringUtil.nullToBlank(fxhdd001.getInputDefault());
                    // 溶剤④_調合量規格-溶剤⑨_調合量規格と添加材ｽﾗﾘｰ_調合量規格のデフォルト値に対して、【】を付ける
                    if (tyogouryoukikaku.contains(fxhdd001.getItemId()) && !inputDefaultValue.startsWith("【")) {
                       inputDefaultValue = "【" + inputDefaultValue + "】";
                    }
                    this.setItemData(processData, fxhdd001.getItemId(), inputDefaultValue);
                });

                // 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_ｻﾌﾞ画面データ設定
                setInputItemDataSubFormC009(processData, null);
                return true;
            }

            // 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量データ取得
            srYuudentaiYouzaiList = getSrYuudentaiYouzaiData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo9, edaban);
            if (srYuudentaiYouzaiList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_サブ画面データ取得
            subSrYuudentaiYouzaiList = getSubSrYuudentaiYouzaiData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo9, edaban);
            if (subSrYuudentaiYouzaiList.isEmpty() || subSrYuudentaiYouzaiList.size() != 11) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }
            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srYuudentaiYouzaiList.isEmpty() || (subSrYuudentaiYouzaiList.isEmpty() || subSrYuudentaiYouzaiList.size() != 11)) {
            return false;
        }
        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srYuudentaiYouzaiList.get(0));
        // 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_ｻﾌﾞ画面データ設定
        setInputItemDataSubFormC009(processData, subSrYuudentaiYouzaiList);
        return true;

    }

    /**
     * データ設定処理
     *
     * @param processData 処理制御データ
     * @param srYuudentaiYouzai 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量
     */
    private void setInputItemDataMainForm(ProcessData processData, SrYuudentaiYouzai srYuudentaiYouzai) {

        // 秤量号機
        this.setItemData(processData, GXHDO102B020Const.GOKI, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.GOKI, srYuudentaiYouzai));

        // 溶剤秤量開始日
        this.setItemData(processData, GXHDO102B020Const.YOUZAIHYOURYOUKAISI_DAY, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAIHYOURYOUKAISI_DAY, srYuudentaiYouzai));

        // 溶剤秤量開始時間
        this.setItemData(processData, GXHDO102B020Const.YOUZAIHYOURYOUKAISI_TIME, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAIHYOURYOUKAISI_TIME, srYuudentaiYouzai));

        // 分散材①_部材在庫No1
        this.setItemData(processData, GXHDO102B020Const.ZUNSANZAI1_BUZAIZAIKOLOTNO1, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.ZUNSANZAI1_BUZAIZAIKOLOTNO1, srYuudentaiYouzai));

        // 分散材①_調合量1
        this.setItemData(processData, GXHDO102B020Const.ZUNSANZAI1_TYOUGOURYOU1, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.ZUNSANZAI1_TYOUGOURYOU1, srYuudentaiYouzai));

        // 分散材①_部材在庫No2
        this.setItemData(processData, GXHDO102B020Const.ZUNSANZAI1_BUZAIZAIKOLOTNO2, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.ZUNSANZAI1_BUZAIZAIKOLOTNO2, srYuudentaiYouzai));

        // 分散材①_調合量2
        this.setItemData(processData, GXHDO102B020Const.ZUNSANZAI1_TYOUGOURYOU2, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.ZUNSANZAI1_TYOUGOURYOU2, srYuudentaiYouzai));

        // 分散材②_部材在庫No1
        this.setItemData(processData, GXHDO102B020Const.ZUNSANZAI2_BUZAIZAIKOLOTNO1, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.ZUNSANZAI2_BUZAIZAIKOLOTNO1, srYuudentaiYouzai));

        // 分散材②_調合量1
        this.setItemData(processData, GXHDO102B020Const.ZUNSANZAI2_TYOUGOURYOU1, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.ZUNSANZAI2_TYOUGOURYOU1, srYuudentaiYouzai));

        // 分散材②_部材在庫No2
        this.setItemData(processData, GXHDO102B020Const.ZUNSANZAI2_BUZAIZAIKOLOTNO2, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.ZUNSANZAI2_BUZAIZAIKOLOTNO2, srYuudentaiYouzai));

        // 分散材②_調合量2
        this.setItemData(processData, GXHDO102B020Const.ZUNSANZAI2_TYOUGOURYOU2, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.ZUNSANZAI2_TYOUGOURYOU2, srYuudentaiYouzai));

        // 溶剤①_部材在庫No1
        this.setItemData(processData, GXHDO102B020Const.YOUZAI1_BUZAIZAIKOLOTNO1, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI1_BUZAIZAIKOLOTNO1, srYuudentaiYouzai));

        // 溶剤①_調合量1
        this.setItemData(processData, GXHDO102B020Const.YOUZAI1_TYOUGOURYOU1, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI1_TYOUGOURYOU1, srYuudentaiYouzai));

        // 溶剤①_部材在庫No2
        this.setItemData(processData, GXHDO102B020Const.YOUZAI1_BUZAIZAIKOLOTNO2, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI1_BUZAIZAIKOLOTNO2, srYuudentaiYouzai));

        // 溶剤①_調合量2
        this.setItemData(processData, GXHDO102B020Const.YOUZAI1_TYOUGOURYOU2, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI1_TYOUGOURYOU2, srYuudentaiYouzai));

        // 溶剤②_部材在庫No1
        this.setItemData(processData, GXHDO102B020Const.YOUZAI2_BUZAIZAIKOLOTNO1, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI2_BUZAIZAIKOLOTNO1, srYuudentaiYouzai));

        // 溶剤②_調合量1
        this.setItemData(processData, GXHDO102B020Const.YOUZAI2_TYOUGOURYOU1, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI2_TYOUGOURYOU1, srYuudentaiYouzai));

        // 溶剤②_部材在庫No2
        this.setItemData(processData, GXHDO102B020Const.YOUZAI2_BUZAIZAIKOLOTNO2, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI2_BUZAIZAIKOLOTNO2, srYuudentaiYouzai));

        // 溶剤②_調合量2
        this.setItemData(processData, GXHDO102B020Const.YOUZAI2_TYOUGOURYOU2, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI2_TYOUGOURYOU2, srYuudentaiYouzai));

        // 溶剤③_部材在庫No1
        this.setItemData(processData, GXHDO102B020Const.YOUZAI3_BUZAIZAIKOLOTNO1, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI3_BUZAIZAIKOLOTNO1, srYuudentaiYouzai));

        // 溶剤③_調合量1
        this.setItemData(processData, GXHDO102B020Const.YOUZAI3_TYOUGOURYOU1, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI3_TYOUGOURYOU1, srYuudentaiYouzai));

        // 溶剤③_部材在庫No2
        this.setItemData(processData, GXHDO102B020Const.YOUZAI3_BUZAIZAIKOLOTNO2, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI3_BUZAIZAIKOLOTNO2, srYuudentaiYouzai));

        // 溶剤③_調合量2
        this.setItemData(processData, GXHDO102B020Const.YOUZAI3_TYOUGOURYOU2, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI3_TYOUGOURYOU2, srYuudentaiYouzai));

        // 溶剤④_調合量規格
        this.setItemData(processData, GXHDO102B020Const.YOUZAI4_TYOUGOURYOUKIKAKU, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI4_TYOUGOURYOUKIKAKU, srYuudentaiYouzai));

        // 溶剤④_部材在庫No1
        this.setItemData(processData, GXHDO102B020Const.YOUZAI4_BUZAIZAIKOLOTNO1, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI4_BUZAIZAIKOLOTNO1, srYuudentaiYouzai));

        // 溶剤④_調合量1
        this.setItemData(processData, GXHDO102B020Const.YOUZAI4_TYOUGOURYOU1, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI4_TYOUGOURYOU1, srYuudentaiYouzai));

        // 溶剤④_部材在庫No2
        this.setItemData(processData, GXHDO102B020Const.YOUZAI4_BUZAIZAIKOLOTNO2, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI4_BUZAIZAIKOLOTNO2, srYuudentaiYouzai));

        // 溶剤④_調合量2
        this.setItemData(processData, GXHDO102B020Const.YOUZAI4_TYOUGOURYOU2, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI4_TYOUGOURYOU2, srYuudentaiYouzai));

        // 溶剤⑤_調合量規格
        this.setItemData(processData, GXHDO102B020Const.YOUZAI5_TYOUGOURYOUKIKAKU, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI5_TYOUGOURYOUKIKAKU, srYuudentaiYouzai));

        // 溶剤⑤_部材在庫No1
        this.setItemData(processData, GXHDO102B020Const.YOUZAI5_BUZAIZAIKOLOTNO1, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI5_BUZAIZAIKOLOTNO1, srYuudentaiYouzai));

        // 溶剤⑤_調合量1
        this.setItemData(processData, GXHDO102B020Const.YOUZAI5_TYOUGOURYOU1, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI5_TYOUGOURYOU1, srYuudentaiYouzai));

        // 溶剤⑤_部材在庫No2
        this.setItemData(processData, GXHDO102B020Const.YOUZAI5_BUZAIZAIKOLOTNO2, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI5_BUZAIZAIKOLOTNO2, srYuudentaiYouzai));

        // 溶剤⑤_調合量2
        this.setItemData(processData, GXHDO102B020Const.YOUZAI5_TYOUGOURYOU2, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI5_TYOUGOURYOU2, srYuudentaiYouzai));

        // 溶剤⑥_調合量規格
        this.setItemData(processData, GXHDO102B020Const.YOUZAI6_TYOUGOURYOUKIKAKU, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI6_TYOUGOURYOUKIKAKU, srYuudentaiYouzai));

        // 溶剤⑥_部材在庫No1
        this.setItemData(processData, GXHDO102B020Const.YOUZAI6_BUZAIZAIKOLOTNO1, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI6_BUZAIZAIKOLOTNO1, srYuudentaiYouzai));

        // 溶剤⑥_調合量1
        this.setItemData(processData, GXHDO102B020Const.YOUZAI6_TYOUGOURYOU1, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI6_TYOUGOURYOU1, srYuudentaiYouzai));

        // 溶剤⑥_部材在庫No2
        this.setItemData(processData, GXHDO102B020Const.YOUZAI6_BUZAIZAIKOLOTNO2, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI6_BUZAIZAIKOLOTNO2, srYuudentaiYouzai));

        // 溶剤⑥_調合量2
        this.setItemData(processData, GXHDO102B020Const.YOUZAI6_TYOUGOURYOU2, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI6_TYOUGOURYOU2, srYuudentaiYouzai));

        // 溶剤⑦_調合量規格
        this.setItemData(processData, GXHDO102B020Const.YOUZAI7_TYOUGOURYOUKIKAKU, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI7_TYOUGOURYOUKIKAKU, srYuudentaiYouzai));

        // 溶剤⑦_部材在庫No1
        this.setItemData(processData, GXHDO102B020Const.YOUZAI7_BUZAIZAIKOLOTNO1, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI7_BUZAIZAIKOLOTNO1, srYuudentaiYouzai));

        // 溶剤⑦_調合量1
        this.setItemData(processData, GXHDO102B020Const.YOUZAI7_TYOUGOURYOU1, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI7_TYOUGOURYOU1, srYuudentaiYouzai));

        // 溶剤⑦_部材在庫No2
        this.setItemData(processData, GXHDO102B020Const.YOUZAI7_BUZAIZAIKOLOTNO2, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI7_BUZAIZAIKOLOTNO2, srYuudentaiYouzai));

        // 溶剤⑦_調合量2
        this.setItemData(processData, GXHDO102B020Const.YOUZAI7_TYOUGOURYOU2, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI7_TYOUGOURYOU2, srYuudentaiYouzai));

        // 溶剤⑧_調合量規格
        this.setItemData(processData, GXHDO102B020Const.YOUZAI8_TYOUGOURYOUKIKAKU, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI8_TYOUGOURYOUKIKAKU, srYuudentaiYouzai));

        // 溶剤⑧_部材在庫No1
        this.setItemData(processData, GXHDO102B020Const.YOUZAI8_BUZAIZAIKOLOTNO1, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI8_BUZAIZAIKOLOTNO1, srYuudentaiYouzai));

        // 溶剤⑧_調合量1
        this.setItemData(processData, GXHDO102B020Const.YOUZAI8_TYOUGOURYOU1, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI8_TYOUGOURYOU1, srYuudentaiYouzai));

        // 溶剤⑧_部材在庫No2
        this.setItemData(processData, GXHDO102B020Const.YOUZAI8_BUZAIZAIKOLOTNO2, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI8_BUZAIZAIKOLOTNO2, srYuudentaiYouzai));

        // 溶剤⑧_調合量2
        this.setItemData(processData, GXHDO102B020Const.YOUZAI8_TYOUGOURYOU2, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI8_TYOUGOURYOU2, srYuudentaiYouzai));

        // 溶剤⑨_調合量規格
        this.setItemData(processData, GXHDO102B020Const.YOUZAI9_TYOUGOURYOUKIKAKU, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI9_TYOUGOURYOUKIKAKU, srYuudentaiYouzai));

        // 溶剤⑨_部材在庫No1
        this.setItemData(processData, GXHDO102B020Const.YOUZAI9_BUZAIZAIKOLOTNO1, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI9_BUZAIZAIKOLOTNO1, srYuudentaiYouzai));

        // 溶剤⑨_調合量1
        this.setItemData(processData, GXHDO102B020Const.YOUZAI9_TYOUGOURYOU1, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI9_TYOUGOURYOU1, srYuudentaiYouzai));

        // 溶剤⑨_部材在庫No2
        this.setItemData(processData, GXHDO102B020Const.YOUZAI9_BUZAIZAIKOLOTNO2, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI9_BUZAIZAIKOLOTNO2, srYuudentaiYouzai));

        // 溶剤⑨_調合量2
        this.setItemData(processData, GXHDO102B020Const.YOUZAI9_TYOUGOURYOU2, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAI9_TYOUGOURYOU2, srYuudentaiYouzai));

        // 溶剤秤量終了日
        this.setItemData(processData, GXHDO102B020Const.YOUZAIHYOURYOUSYUURYOU_DAY, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAIHYOURYOUSYUURYOU_DAY, srYuudentaiYouzai));

        // 溶剤秤量終了時間
        this.setItemData(processData, GXHDO102B020Const.YOUZAIHYOURYOUSYUURYOU_TIME, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.YOUZAIHYOURYOUSYUURYOU_TIME, srYuudentaiYouzai));

        // 撹拌開始日
        this.setItemData(processData, GXHDO102B020Const.KAKUHANKAISI_DAY, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.KAKUHANKAISI_DAY, srYuudentaiYouzai));

        // 撹拌開始時間
        this.setItemData(processData, GXHDO102B020Const.KAKUHANKAISI_TIME, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.KAKUHANKAISI_TIME, srYuudentaiYouzai));

        // 撹拌終了日
        this.setItemData(processData, GXHDO102B020Const.KAKUHANSYUURYOU_DAY, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.KAKUHANSYUURYOU_DAY, srYuudentaiYouzai));

        // 撹拌終了時間
        this.setItemData(processData, GXHDO102B020Const.KAKUHANSYUURYOU_TIME, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.KAKUHANSYUURYOU_TIME, srYuudentaiYouzai));

        // 添加材ｽﾗﾘｰ秤量開始日
        this.setItemData(processData, GXHDO102B020Const.TENKAZAISLURRYHRKAISI_DAY, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.TENKAZAISLURRYHRKAISI_DAY, srYuudentaiYouzai));

        // 添加材ｽﾗﾘｰ秤量開始時間
        this.setItemData(processData, GXHDO102B020Const.TENKAZAISLURRYHRKAISI_TIME, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.TENKAZAISLURRYHRKAISI_TIME, srYuudentaiYouzai));

        // 添加材ｽﾗﾘｰ_WIPﾛｯﾄNo
        this.setItemData(processData, GXHDO102B020Const.TENKAZAISLURRY_WIPLOTNO, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.TENKAZAISLURRY_WIPLOTNO, srYuudentaiYouzai));

        // 添加材ｽﾗﾘｰ_調合量規格
        this.setItemData(processData, GXHDO102B020Const.TENKAZAISLURRY_TGRKIKAKU, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.TENKAZAISLURRY_TGRKIKAKU, srYuudentaiYouzai));

        // 添加材ｽﾗﾘｰ_風袋重量1
        this.setItemData(processData, GXHDO102B020Const.TENKAZAISLURRY_FTAIJYUURYOU1, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.TENKAZAISLURRY_FTAIJYUURYOU1, srYuudentaiYouzai));

        // 添加材ｽﾗﾘｰ_調合量1
        this.setItemData(processData, GXHDO102B020Const.TENKAZAISLURRY_TYOUGOURYOU1, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.TENKAZAISLURRY_TYOUGOURYOU1, srYuudentaiYouzai));

        // 添加材ｽﾗﾘｰ_風袋重量2
        this.setItemData(processData, GXHDO102B020Const.TENKAZAISLURRY_FTAIJYUURYOU2, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.TENKAZAISLURRY_FTAIJYUURYOU2, srYuudentaiYouzai));

        // 添加材ｽﾗﾘｰ_調合量2
        this.setItemData(processData, GXHDO102B020Const.TENKAZAISLURRY_TYOUGOURYOU2, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.TENKAZAISLURRY_TYOUGOURYOU2, srYuudentaiYouzai));

        // 添加材ｽﾗﾘｰ秤量終了日
        this.setItemData(processData, GXHDO102B020Const.TENKAZAISLURRYHRSYURYO_DAY, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.TENKAZAISLURRYHRSYURYO_DAY, srYuudentaiYouzai));

        // 添加材ｽﾗﾘｰ秤量終了時間
        this.setItemData(processData, GXHDO102B020Const.TENKAZAISLURRYHRSYURYO_TIME, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.TENKAZAISLURRYHRSYURYO_TIME, srYuudentaiYouzai));

        // 固形分測定担当者
        this.setItemData(processData, GXHDO102B020Const.KOKEIBUNSOKUTEITANTOUSYA, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.KOKEIBUNSOKUTEITANTOUSYA, srYuudentaiYouzai));

        // 備考1
        this.setItemData(processData, GXHDO102B020Const.BIKOU1, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.BIKOU1, srYuudentaiYouzai));

        // 備考2
        this.setItemData(processData, GXHDO102B020Const.BIKOU2, getSrYuudentaiYouzaiItemData(GXHDO102B020Const.BIKOU2, srYuudentaiYouzai));

    }

    /**
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量データ
     * @throws SQLException 例外エラー
     */
    private List<SrYuudentaiYouzai> getSrYuudentaiYouzaiData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrYuudentaiYouzai(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSrYuudentaiYouzai(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
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
     * [誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrYuudentaiYouzai> loadSrYuudentaiYouzai(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + " kojyo,lotno,edaban,yuudentaislurryhinmei,yuudentaislurrylotno,lotkubun,genryoulotno,genryoukigou,goki,"
                + "youzaihyouryoukaisinichiji,zunsanzai1_zairyouhinmei,zunsanzai1_tyougouryoukikaku,zunsanzai1_buzaizaikolotno1,"
                + "zunsanzai1_tyougouryou1,zunsanzai1_buzaizaikolotno2,zunsanzai1_tyougouryou2,zunsanzai2_zairyouhinmei,"
                + "zunsanzai2_tyougouryoukikaku,zunsanzai2_buzaizaikolotno1,zunsanzai2_tyougouryou1,zunsanzai2_buzaizaikolotno2,"
                + "zunsanzai2_tyougouryou2,youzai1_zairyouhinmei,youzai1_tyougouryoukikaku,youzai1_buzaizaikolotno1,youzai1_tyougouryou1,"
                + "youzai1_buzaizaikolotno2,youzai1_tyougouryou2,youzai2_zairyouhinmei,youzai2_tyougouryoukikaku,youzai2_buzaizaikolotno1,"
                + "youzai2_tyougouryou1,youzai2_buzaizaikolotno2,youzai2_tyougouryou2,youzai3_zairyouhinmei,youzai3_tyougouryoukikaku,"
                + "youzai3_buzaizaikolotno1,youzai3_tyougouryou1,youzai3_buzaizaikolotno2,youzai3_tyougouryou2,youzai4_zairyouhinmei,"
                + "youzai4_tyougouryoukikaku,youzai4_buzaizaikolotno1,youzai4_tyougouryou1,youzai4_buzaizaikolotno2,youzai4_tyougouryou2,"
                + "youzai5_zairyouhinmei,youzai5_tyougouryoukikaku,youzai5_buzaizaikolotno1,youzai5_tyougouryou1,youzai5_buzaizaikolotno2,"
                + "youzai5_tyougouryou2,youzai6_zairyouhinmei,youzai6_tyougouryoukikaku,youzai6_buzaizaikolotno1,youzai6_tyougouryou1,"
                + "youzai6_buzaizaikolotno2,youzai6_tyougouryou2,youzai7_zairyouhinmei,youzai7_tyougouryoukikaku,youzai7_buzaizaikolotno1,"
                + "youzai7_tyougouryou1,youzai7_buzaizaikolotno2,youzai7_tyougouryou2,youzai8_zairyouhinmei,youzai8_tyougouryoukikaku,"
                + "youzai8_buzaizaikolotno1,youzai8_tyougouryou1,youzai8_buzaizaikolotno2,youzai8_tyougouryou2,youzai9_zairyouhinmei,"
                + "youzai9_tyougouryoukikaku,youzai9_buzaizaikolotno1,youzai9_tyougouryou1,youzai9_buzaizaikolotno2,youzai9_tyougouryou2,"
                + "youzaihyouryousyuuryounichiji,kakuhanki,kaitensuu,kakuhanjikan,kakuhankaisinichiji,kakuhansyuuryounichiji,"
                + "tenkazaislurryhyouryoukaisinichiji,tenkazaislurry_zairyouhinmei,tenkazaislurry_WIPlotno,tenkazaislurry_tyougouryoukikaku,"
                + "tenkazaislurry_fuutaijyuuryou1,tenkazaislurry_tyougouryou1,tenkazaislurry_fuutaijyuuryou2,tenkazaislurry_tyougouryou2,"
                + "tenkazaislurryhyouryousyuuryounichiji,kokeibunsokuteitantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + " FROM sr_yuudentai_youzai "
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
        mapping.put("kojyo", "kojyo");                                                                   // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                                                   // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                                                 // 枝番
        mapping.put("yuudentaislurryhinmei", "yuudentaislurryhinmei");                                   // 誘電体ｽﾗﾘｰ品名
        mapping.put("yuudentaislurrylotno", "yuudentaislurrylotno");                                     // 誘電体ｽﾗﾘｰLotNo
        mapping.put("lotkubun", "lotkubun");                                                             // ﾛｯﾄ区分
        mapping.put("genryoulotno", "genryoulotno");                                                     // 原料LotNo
        mapping.put("genryoukigou", "genryoukigou");                                                     // 原料記号
        mapping.put("goki", "goki");                                                                     // 秤量号機
        mapping.put("youzaihyouryoukaisinichiji", "youzaihyouryoukaisinichiji");                         // 溶剤秤量開始日時
        mapping.put("zunsanzai1_zairyouhinmei", "zunsanzai1_zairyouhinmei");                             // 分散材①_材料品名
        mapping.put("zunsanzai1_tyougouryoukikaku", "zunsanzai1_tyougouryoukikaku");                     // 分散材①_調合量規格
        mapping.put("zunsanzai1_buzaizaikolotno1", "zunsanzai1_buzaizaikolotno1");                       // 分散材①_部材在庫No1
        mapping.put("zunsanzai1_tyougouryou1", "zunsanzai1_tyougouryou1");                               // 分散材①_調合量1
        mapping.put("zunsanzai1_buzaizaikolotno2", "zunsanzai1_buzaizaikolotno2");                       // 分散材①_部材在庫No2
        mapping.put("zunsanzai1_tyougouryou2", "zunsanzai1_tyougouryou2");                               // 分散材①_調合量2
        mapping.put("zunsanzai2_zairyouhinmei", "zunsanzai2_zairyouhinmei");                             // 分散材②_材料品名
        mapping.put("zunsanzai2_tyougouryoukikaku", "zunsanzai2_tyougouryoukikaku");                     // 分散材②_調合量規格
        mapping.put("zunsanzai2_buzaizaikolotno1", "zunsanzai2_buzaizaikolotno1");                       // 分散材②_部材在庫No1
        mapping.put("zunsanzai2_tyougouryou1", "zunsanzai2_tyougouryou1");                               // 分散材②_調合量1
        mapping.put("zunsanzai2_buzaizaikolotno2", "zunsanzai2_buzaizaikolotno2");                       // 分散材②_部材在庫No2
        mapping.put("zunsanzai2_tyougouryou2", "zunsanzai2_tyougouryou2");                               // 分散材②_調合量2
        mapping.put("youzai1_zairyouhinmei", "youzai1_zairyouhinmei");                                   // 溶剤①_材料品名
        mapping.put("youzai1_tyougouryoukikaku", "youzai1_tyougouryoukikaku");                           // 溶剤①_調合量規格
        mapping.put("youzai1_buzaizaikolotno1", "youzai1_buzaizaikolotno1");                             // 溶剤①_部材在庫No1
        mapping.put("youzai1_tyougouryou1", "youzai1_tyougouryou1");                                     // 溶剤①_調合量1
        mapping.put("youzai1_buzaizaikolotno2", "youzai1_buzaizaikolotno2");                             // 溶剤①_部材在庫No2
        mapping.put("youzai1_tyougouryou2", "youzai1_tyougouryou2");                                     // 溶剤①_調合量2
        mapping.put("youzai2_zairyouhinmei", "youzai2_zairyouhinmei");                                   // 溶剤②_材料品名
        mapping.put("youzai2_tyougouryoukikaku", "youzai2_tyougouryoukikaku");                           // 溶剤②_調合量規格
        mapping.put("youzai2_buzaizaikolotno1", "youzai2_buzaizaikolotno1");                             // 溶剤②_部材在庫No1
        mapping.put("youzai2_tyougouryou1", "youzai2_tyougouryou1");                                     // 溶剤②_調合量1
        mapping.put("youzai2_buzaizaikolotno2", "youzai2_buzaizaikolotno2");                             // 溶剤②_部材在庫No2
        mapping.put("youzai2_tyougouryou2", "youzai2_tyougouryou2");                                     // 溶剤②_調合量2
        mapping.put("youzai3_zairyouhinmei", "youzai3_zairyouhinmei");                                   // 溶剤③_材料品名
        mapping.put("youzai3_tyougouryoukikaku", "youzai3_tyougouryoukikaku");                           // 溶剤③_調合量規格
        mapping.put("youzai3_buzaizaikolotno1", "youzai3_buzaizaikolotno1");                             // 溶剤③_部材在庫No1
        mapping.put("youzai3_tyougouryou1", "youzai3_tyougouryou1");                                     // 溶剤③_調合量1
        mapping.put("youzai3_buzaizaikolotno2", "youzai3_buzaizaikolotno2");                             // 溶剤③_部材在庫No2
        mapping.put("youzai3_tyougouryou2", "youzai3_tyougouryou2");                                     // 溶剤③_調合量2
        mapping.put("youzai4_zairyouhinmei", "youzai4_zairyouhinmei");                                   // 溶剤④_材料品名
        mapping.put("youzai4_tyougouryoukikaku", "youzai4_tyougouryoukikaku");                           // 溶剤④_調合量規格
        mapping.put("youzai4_buzaizaikolotno1", "youzai4_buzaizaikolotno1");                             // 溶剤④_部材在庫No1
        mapping.put("youzai4_tyougouryou1", "youzai4_tyougouryou1");                                     // 溶剤④_調合量1
        mapping.put("youzai4_buzaizaikolotno2", "youzai4_buzaizaikolotno2");                             // 溶剤④_部材在庫No2
        mapping.put("youzai4_tyougouryou2", "youzai4_tyougouryou2");                                     // 溶剤④_調合量2
        mapping.put("youzai5_zairyouhinmei", "youzai5_zairyouhinmei");                                   // 溶剤⑤_材料品名
        mapping.put("youzai5_tyougouryoukikaku", "youzai5_tyougouryoukikaku");                           // 溶剤⑤_調合量規格
        mapping.put("youzai5_buzaizaikolotno1", "youzai5_buzaizaikolotno1");                             // 溶剤⑤_部材在庫No1
        mapping.put("youzai5_tyougouryou1", "youzai5_tyougouryou1");                                     // 溶剤⑤_調合量1
        mapping.put("youzai5_buzaizaikolotno2", "youzai5_buzaizaikolotno2");                             // 溶剤⑤_部材在庫No2
        mapping.put("youzai5_tyougouryou2", "youzai5_tyougouryou2");                                     // 溶剤⑤_調合量2
        mapping.put("youzai6_zairyouhinmei", "youzai6_zairyouhinmei");                                   // 溶剤⑥_材料品名
        mapping.put("youzai6_tyougouryoukikaku", "youzai6_tyougouryoukikaku");                           // 溶剤⑥_調合量規格
        mapping.put("youzai6_buzaizaikolotno1", "youzai6_buzaizaikolotno1");                             // 溶剤⑥_部材在庫No1
        mapping.put("youzai6_tyougouryou1", "youzai6_tyougouryou1");                                     // 溶剤⑥_調合量1
        mapping.put("youzai6_buzaizaikolotno2", "youzai6_buzaizaikolotno2");                             // 溶剤⑥_部材在庫No2
        mapping.put("youzai6_tyougouryou2", "youzai6_tyougouryou2");                                     // 溶剤⑥_調合量2
        mapping.put("youzai7_zairyouhinmei", "youzai7_zairyouhinmei");                                   // 溶剤⑦_材料品名
        mapping.put("youzai7_tyougouryoukikaku", "youzai7_tyougouryoukikaku");                           // 溶剤⑦_調合量規格
        mapping.put("youzai7_buzaizaikolotno1", "youzai7_buzaizaikolotno1");                             // 溶剤⑦_部材在庫No1
        mapping.put("youzai7_tyougouryou1", "youzai7_tyougouryou1");                                     // 溶剤⑦_調合量1
        mapping.put("youzai7_buzaizaikolotno2", "youzai7_buzaizaikolotno2");                             // 溶剤⑦_部材在庫No2
        mapping.put("youzai7_tyougouryou2", "youzai7_tyougouryou2");                                     // 溶剤⑦_調合量2
        mapping.put("youzai8_zairyouhinmei", "youzai8_zairyouhinmei");                                   // 溶剤⑧_材料品名
        mapping.put("youzai8_tyougouryoukikaku", "youzai8_tyougouryoukikaku");                           // 溶剤⑧_調合量規格
        mapping.put("youzai8_buzaizaikolotno1", "youzai8_buzaizaikolotno1");                             // 溶剤⑧_部材在庫No1
        mapping.put("youzai8_tyougouryou1", "youzai8_tyougouryou1");                                     // 溶剤⑧_調合量1
        mapping.put("youzai8_buzaizaikolotno2", "youzai8_buzaizaikolotno2");                             // 溶剤⑧_部材在庫No2
        mapping.put("youzai8_tyougouryou2", "youzai8_tyougouryou2");                                     // 溶剤⑧_調合量2
        mapping.put("youzai9_zairyouhinmei", "youzai9_zairyouhinmei");                                   // 溶剤⑨_材料品名
        mapping.put("youzai9_tyougouryoukikaku", "youzai9_tyougouryoukikaku");                           // 溶剤⑨_調合量規格
        mapping.put("youzai9_buzaizaikolotno1", "youzai9_buzaizaikolotno1");                             // 溶剤⑨_部材在庫No1
        mapping.put("youzai9_tyougouryou1", "youzai9_tyougouryou1");                                     // 溶剤⑨_調合量1
        mapping.put("youzai9_buzaizaikolotno2", "youzai9_buzaizaikolotno2");                             // 溶剤⑨_部材在庫No2
        mapping.put("youzai9_tyougouryou2", "youzai9_tyougouryou2");                                     // 溶剤⑨_調合量2
        mapping.put("youzaihyouryousyuuryounichiji", "youzaihyouryousyuuryounichiji");                   // 溶剤秤量終了日時
        mapping.put("kakuhanki", "kakuhanki");                                                           // 撹拌機
        mapping.put("kaitensuu", "kaitensuu");                                                           // 回転数
        mapping.put("kakuhanjikan", "kakuhanjikan");                                                     // 撹拌時間
        mapping.put("kakuhankaisinichiji", "kakuhankaisinichiji");                                       // 撹拌開始日時
        mapping.put("kakuhansyuuryounichiji", "kakuhansyuuryounichiji");                                 // 撹拌終了日時
        mapping.put("tenkazaislurryhyouryoukaisinichiji", "tenkazaislurryhyouryoukaisinichiji");         // 添加材ｽﾗﾘｰ秤量開始日時
        mapping.put("tenkazaislurry_zairyouhinmei", "tenkazaislurry_zairyouhinmei");                     // 添加材ｽﾗﾘｰ_材料品名
        mapping.put("tenkazaislurry_WIPlotno", "tenkazaislurry_WIPlotno");                               // 添加材ｽﾗﾘｰ_WIPﾛｯﾄNo
        mapping.put("tenkazaislurry_tyougouryoukikaku", "tenkazaislurry_tyougouryoukikaku");             // 添加材ｽﾗﾘｰ_調合量規格
        mapping.put("tenkazaislurry_fuutaijyuuryou1", "tenkazaislurry_fuutaijyuuryou1");                 // 添加材ｽﾗﾘｰ_風袋重量1
        mapping.put("tenkazaislurry_tyougouryou1", "tenkazaislurry_tyougouryou1");                       // 添加材ｽﾗﾘｰ_調合量1
        mapping.put("tenkazaislurry_fuutaijyuuryou2", "tenkazaislurry_fuutaijyuuryou2");                 // 添加材ｽﾗﾘｰ_風袋重量2
        mapping.put("tenkazaislurry_tyougouryou2", "tenkazaislurry_tyougouryou2");                       // 添加材ｽﾗﾘｰ_調合量2
        mapping.put("tenkazaislurryhyouryousyuuryounichiji", "tenkazaislurryhyouryousyuuryounichiji");   // 添加材ｽﾗﾘｰ秤量終了日時
        mapping.put("kokeibunsokuteitantousya", "kokeibunsokuteitantousya");                             // 固形分測定担当者
        mapping.put("bikou1", "bikou1");                                                                 // 備考1
        mapping.put("bikou2", "bikou2");                                                                 // 備考2
        mapping.put("torokunichiji", "torokunichiji");                                                   // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                                                     // 更新日時
        mapping.put("revision", "revision");                                                             // revision

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrYuudentaiYouzai>> beanHandler = new BeanListHandler<>(SrYuudentaiYouzai.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrYuudentaiYouzai> loadTmpSrYuudentaiYouzai(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + " kojyo,lotno,edaban,yuudentaislurryhinmei,yuudentaislurrylotno,lotkubun,genryoulotno,genryoukigou,goki,"
                + "youzaihyouryoukaisinichiji,zunsanzai1_zairyouhinmei,zunsanzai1_tyougouryoukikaku,zunsanzai1_buzaizaikolotno1,"
                + "zunsanzai1_tyougouryou1,zunsanzai1_buzaizaikolotno2,zunsanzai1_tyougouryou2,zunsanzai2_zairyouhinmei,"
                + "zunsanzai2_tyougouryoukikaku,zunsanzai2_buzaizaikolotno1,zunsanzai2_tyougouryou1,zunsanzai2_buzaizaikolotno2,"
                + "zunsanzai2_tyougouryou2,youzai1_zairyouhinmei,youzai1_tyougouryoukikaku,youzai1_buzaizaikolotno1,youzai1_tyougouryou1,"
                + "youzai1_buzaizaikolotno2,youzai1_tyougouryou2,youzai2_zairyouhinmei,youzai2_tyougouryoukikaku,youzai2_buzaizaikolotno1,"
                + "youzai2_tyougouryou1,youzai2_buzaizaikolotno2,youzai2_tyougouryou2,youzai3_zairyouhinmei,youzai3_tyougouryoukikaku,"
                + "youzai3_buzaizaikolotno1,youzai3_tyougouryou1,youzai3_buzaizaikolotno2,youzai3_tyougouryou2,youzai4_zairyouhinmei,"
                + "youzai4_tyougouryoukikaku,youzai4_buzaizaikolotno1,youzai4_tyougouryou1,youzai4_buzaizaikolotno2,youzai4_tyougouryou2,"
                + "youzai5_zairyouhinmei,youzai5_tyougouryoukikaku,youzai5_buzaizaikolotno1,youzai5_tyougouryou1,youzai5_buzaizaikolotno2,"
                + "youzai5_tyougouryou2,youzai6_zairyouhinmei,youzai6_tyougouryoukikaku,youzai6_buzaizaikolotno1,youzai6_tyougouryou1,"
                + "youzai6_buzaizaikolotno2,youzai6_tyougouryou2,youzai7_zairyouhinmei,youzai7_tyougouryoukikaku,youzai7_buzaizaikolotno1,"
                + "youzai7_tyougouryou1,youzai7_buzaizaikolotno2,youzai7_tyougouryou2,youzai8_zairyouhinmei,youzai8_tyougouryoukikaku,"
                + "youzai8_buzaizaikolotno1,youzai8_tyougouryou1,youzai8_buzaizaikolotno2,youzai8_tyougouryou2,youzai9_zairyouhinmei,"
                + "youzai9_tyougouryoukikaku,youzai9_buzaizaikolotno1,youzai9_tyougouryou1,youzai9_buzaizaikolotno2,youzai9_tyougouryou2,"
                + "youzaihyouryousyuuryounichiji,kakuhanki,kaitensuu,kakuhanjikan,kakuhankaisinichiji,kakuhansyuuryounichiji,"
                + "tenkazaislurryhyouryoukaisinichiji,tenkazaislurry_zairyouhinmei,tenkazaislurry_WIPlotno,tenkazaislurry_tyougouryoukikaku,"
                + "tenkazaislurry_fuutaijyuuryou1,tenkazaislurry_tyougouryou1,tenkazaislurry_fuutaijyuuryou2,tenkazaislurry_tyougouryou2,"
                + "tenkazaislurryhyouryousyuuryounichiji,kokeibunsokuteitantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + " FROM tmp_sr_yuudentai_youzai "
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
        mapping.put("kojyo", "kojyo");                                                                   // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                                                   // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                                                 // 枝番
        mapping.put("yuudentaislurryhinmei", "yuudentaislurryhinmei");                                   // 誘電体ｽﾗﾘｰ品名
        mapping.put("yuudentaislurrylotno", "yuudentaislurrylotno");                                     // 誘電体ｽﾗﾘｰLotNo
        mapping.put("lotkubun", "lotkubun");                                                             // ﾛｯﾄ区分
        mapping.put("genryoulotno", "genryoulotno");                                                     // 原料LotNo
        mapping.put("genryoukigou", "genryoukigou");                                                     // 原料記号
        mapping.put("goki", "goki");                                                                     // 秤量号機
        mapping.put("youzaihyouryoukaisinichiji", "youzaihyouryoukaisinichiji");                         // 溶剤秤量開始日時
        mapping.put("zunsanzai1_zairyouhinmei", "zunsanzai1_zairyouhinmei");                             // 分散材①_材料品名
        mapping.put("zunsanzai1_tyougouryoukikaku", "zunsanzai1_tyougouryoukikaku");                     // 分散材①_調合量規格
        mapping.put("zunsanzai1_buzaizaikolotno1", "zunsanzai1_buzaizaikolotno1");                       // 分散材①_部材在庫No1
        mapping.put("zunsanzai1_tyougouryou1", "zunsanzai1_tyougouryou1");                               // 分散材①_調合量1
        mapping.put("zunsanzai1_buzaizaikolotno2", "zunsanzai1_buzaizaikolotno2");                       // 分散材①_部材在庫No2
        mapping.put("zunsanzai1_tyougouryou2", "zunsanzai1_tyougouryou2");                               // 分散材①_調合量2
        mapping.put("zunsanzai2_zairyouhinmei", "zunsanzai2_zairyouhinmei");                             // 分散材②_材料品名
        mapping.put("zunsanzai2_tyougouryoukikaku", "zunsanzai2_tyougouryoukikaku");                     // 分散材②_調合量規格
        mapping.put("zunsanzai2_buzaizaikolotno1", "zunsanzai2_buzaizaikolotno1");                       // 分散材②_部材在庫No1
        mapping.put("zunsanzai2_tyougouryou1", "zunsanzai2_tyougouryou1");                               // 分散材②_調合量1
        mapping.put("zunsanzai2_buzaizaikolotno2", "zunsanzai2_buzaizaikolotno2");                       // 分散材②_部材在庫No2
        mapping.put("zunsanzai2_tyougouryou2", "zunsanzai2_tyougouryou2");                               // 分散材②_調合量2
        mapping.put("youzai1_zairyouhinmei", "youzai1_zairyouhinmei");                                   // 溶剤①_材料品名
        mapping.put("youzai1_tyougouryoukikaku", "youzai1_tyougouryoukikaku");                           // 溶剤①_調合量規格
        mapping.put("youzai1_buzaizaikolotno1", "youzai1_buzaizaikolotno1");                             // 溶剤①_部材在庫No1
        mapping.put("youzai1_tyougouryou1", "youzai1_tyougouryou1");                                     // 溶剤①_調合量1
        mapping.put("youzai1_buzaizaikolotno2", "youzai1_buzaizaikolotno2");                             // 溶剤①_部材在庫No2
        mapping.put("youzai1_tyougouryou2", "youzai1_tyougouryou2");                                     // 溶剤①_調合量2
        mapping.put("youzai2_zairyouhinmei", "youzai2_zairyouhinmei");                                   // 溶剤②_材料品名
        mapping.put("youzai2_tyougouryoukikaku", "youzai2_tyougouryoukikaku");                           // 溶剤②_調合量規格
        mapping.put("youzai2_buzaizaikolotno1", "youzai2_buzaizaikolotno1");                             // 溶剤②_部材在庫No1
        mapping.put("youzai2_tyougouryou1", "youzai2_tyougouryou1");                                     // 溶剤②_調合量1
        mapping.put("youzai2_buzaizaikolotno2", "youzai2_buzaizaikolotno2");                             // 溶剤②_部材在庫No2
        mapping.put("youzai2_tyougouryou2", "youzai2_tyougouryou2");                                     // 溶剤②_調合量2
        mapping.put("youzai3_zairyouhinmei", "youzai3_zairyouhinmei");                                   // 溶剤③_材料品名
        mapping.put("youzai3_tyougouryoukikaku", "youzai3_tyougouryoukikaku");                           // 溶剤③_調合量規格
        mapping.put("youzai3_buzaizaikolotno1", "youzai3_buzaizaikolotno1");                             // 溶剤③_部材在庫No1
        mapping.put("youzai3_tyougouryou1", "youzai3_tyougouryou1");                                     // 溶剤③_調合量1
        mapping.put("youzai3_buzaizaikolotno2", "youzai3_buzaizaikolotno2");                             // 溶剤③_部材在庫No2
        mapping.put("youzai3_tyougouryou2", "youzai3_tyougouryou2");                                     // 溶剤③_調合量2
        mapping.put("youzai4_zairyouhinmei", "youzai4_zairyouhinmei");                                   // 溶剤④_材料品名
        mapping.put("youzai4_tyougouryoukikaku", "youzai4_tyougouryoukikaku");                           // 溶剤④_調合量規格
        mapping.put("youzai4_buzaizaikolotno1", "youzai4_buzaizaikolotno1");                             // 溶剤④_部材在庫No1
        mapping.put("youzai4_tyougouryou1", "youzai4_tyougouryou1");                                     // 溶剤④_調合量1
        mapping.put("youzai4_buzaizaikolotno2", "youzai4_buzaizaikolotno2");                             // 溶剤④_部材在庫No2
        mapping.put("youzai4_tyougouryou2", "youzai4_tyougouryou2");                                     // 溶剤④_調合量2
        mapping.put("youzai5_zairyouhinmei", "youzai5_zairyouhinmei");                                   // 溶剤⑤_材料品名
        mapping.put("youzai5_tyougouryoukikaku", "youzai5_tyougouryoukikaku");                           // 溶剤⑤_調合量規格
        mapping.put("youzai5_buzaizaikolotno1", "youzai5_buzaizaikolotno1");                             // 溶剤⑤_部材在庫No1
        mapping.put("youzai5_tyougouryou1", "youzai5_tyougouryou1");                                     // 溶剤⑤_調合量1
        mapping.put("youzai5_buzaizaikolotno2", "youzai5_buzaizaikolotno2");                             // 溶剤⑤_部材在庫No2
        mapping.put("youzai5_tyougouryou2", "youzai5_tyougouryou2");                                     // 溶剤⑤_調合量2
        mapping.put("youzai6_zairyouhinmei", "youzai6_zairyouhinmei");                                   // 溶剤⑥_材料品名
        mapping.put("youzai6_tyougouryoukikaku", "youzai6_tyougouryoukikaku");                           // 溶剤⑥_調合量規格
        mapping.put("youzai6_buzaizaikolotno1", "youzai6_buzaizaikolotno1");                             // 溶剤⑥_部材在庫No1
        mapping.put("youzai6_tyougouryou1", "youzai6_tyougouryou1");                                     // 溶剤⑥_調合量1
        mapping.put("youzai6_buzaizaikolotno2", "youzai6_buzaizaikolotno2");                             // 溶剤⑥_部材在庫No2
        mapping.put("youzai6_tyougouryou2", "youzai6_tyougouryou2");                                     // 溶剤⑥_調合量2
        mapping.put("youzai7_zairyouhinmei", "youzai7_zairyouhinmei");                                   // 溶剤⑦_材料品名
        mapping.put("youzai7_tyougouryoukikaku", "youzai7_tyougouryoukikaku");                           // 溶剤⑦_調合量規格
        mapping.put("youzai7_buzaizaikolotno1", "youzai7_buzaizaikolotno1");                             // 溶剤⑦_部材在庫No1
        mapping.put("youzai7_tyougouryou1", "youzai7_tyougouryou1");                                     // 溶剤⑦_調合量1
        mapping.put("youzai7_buzaizaikolotno2", "youzai7_buzaizaikolotno2");                             // 溶剤⑦_部材在庫No2
        mapping.put("youzai7_tyougouryou2", "youzai7_tyougouryou2");                                     // 溶剤⑦_調合量2
        mapping.put("youzai8_zairyouhinmei", "youzai8_zairyouhinmei");                                   // 溶剤⑧_材料品名
        mapping.put("youzai8_tyougouryoukikaku", "youzai8_tyougouryoukikaku");                           // 溶剤⑧_調合量規格
        mapping.put("youzai8_buzaizaikolotno1", "youzai8_buzaizaikolotno1");                             // 溶剤⑧_部材在庫No1
        mapping.put("youzai8_tyougouryou1", "youzai8_tyougouryou1");                                     // 溶剤⑧_調合量1
        mapping.put("youzai8_buzaizaikolotno2", "youzai8_buzaizaikolotno2");                             // 溶剤⑧_部材在庫No2
        mapping.put("youzai8_tyougouryou2", "youzai8_tyougouryou2");                                     // 溶剤⑧_調合量2
        mapping.put("youzai9_zairyouhinmei", "youzai9_zairyouhinmei");                                   // 溶剤⑨_材料品名
        mapping.put("youzai9_tyougouryoukikaku", "youzai9_tyougouryoukikaku");                           // 溶剤⑨_調合量規格
        mapping.put("youzai9_buzaizaikolotno1", "youzai9_buzaizaikolotno1");                             // 溶剤⑨_部材在庫No1
        mapping.put("youzai9_tyougouryou1", "youzai9_tyougouryou1");                                     // 溶剤⑨_調合量1
        mapping.put("youzai9_buzaizaikolotno2", "youzai9_buzaizaikolotno2");                             // 溶剤⑨_部材在庫No2
        mapping.put("youzai9_tyougouryou2", "youzai9_tyougouryou2");                                     // 溶剤⑨_調合量2
        mapping.put("youzaihyouryousyuuryounichiji", "youzaihyouryousyuuryounichiji");                   // 溶剤秤量終了日時
        mapping.put("kakuhanki", "kakuhanki");                                                           // 撹拌機
        mapping.put("kaitensuu", "kaitensuu");                                                           // 回転数
        mapping.put("kakuhanjikan", "kakuhanjikan");                                                     // 撹拌時間
        mapping.put("kakuhankaisinichiji", "kakuhankaisinichiji");                                       // 撹拌開始日時
        mapping.put("kakuhansyuuryounichiji", "kakuhansyuuryounichiji");                                 // 撹拌終了日時
        mapping.put("tenkazaislurryhyouryoukaisinichiji", "tenkazaislurryhyouryoukaisinichiji");         // 添加材ｽﾗﾘｰ秤量開始日時
        mapping.put("tenkazaislurry_zairyouhinmei", "tenkazaislurry_zairyouhinmei");                     // 添加材ｽﾗﾘｰ_材料品名
        mapping.put("tenkazaislurry_WIPlotno", "tenkazaislurry_WIPlotno");                               // 添加材ｽﾗﾘｰ_WIPﾛｯﾄNo
        mapping.put("tenkazaislurry_tyougouryoukikaku", "tenkazaislurry_tyougouryoukikaku");             // 添加材ｽﾗﾘｰ_調合量規格
        mapping.put("tenkazaislurry_fuutaijyuuryou1", "tenkazaislurry_fuutaijyuuryou1");                 // 添加材ｽﾗﾘｰ_風袋重量1
        mapping.put("tenkazaislurry_tyougouryou1", "tenkazaislurry_tyougouryou1");                       // 添加材ｽﾗﾘｰ_調合量1
        mapping.put("tenkazaislurry_fuutaijyuuryou2", "tenkazaislurry_fuutaijyuuryou2");                 // 添加材ｽﾗﾘｰ_風袋重量2
        mapping.put("tenkazaislurry_tyougouryou2", "tenkazaislurry_tyougouryou2");                       // 添加材ｽﾗﾘｰ_調合量2
        mapping.put("tenkazaislurryhyouryousyuuryounichiji", "tenkazaislurryhyouryousyuuryounichiji");   // 添加材ｽﾗﾘｰ秤量終了日時
        mapping.put("kokeibunsokuteitantousya", "kokeibunsokuteitantousya");                             // 固形分測定担当者
        mapping.put("bikou1", "bikou1");                                                                 // 備考1
        mapping.put("bikou2", "bikou2");                                                                 // 備考2
        mapping.put("torokunichiji", "torokunichiji");                                                   // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                                                     // 更新日時
        mapping.put("revision", "revision");                                                             // revision
        mapping.put("deleteflag", "deleteflag");                                                         // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrYuudentaiYouzai>> beanHandler = new BeanListHandler<>(SrYuudentaiYouzai.class, rowProcessor);

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
     * ボタンデータ取得
     *
     * @param listData フォームデータ
     * @param buttonId ボタンID
     * @return 項目データ
     */
    private FXHDD02 getButtonRow(List<FXHDD02> buttonList, String buttonId) {
        return buttonList.stream().filter(n -> buttonId.equals(n.getButtonId())).findFirst().orElse(null);
    }

    /**
     * 項目データ(入力値)取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @param srYuudentaiYouzai 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量データ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrYuudentaiYouzai srYuudentaiYouzai) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srYuudentaiYouzai != null) {
            // 元データが存在する場合元データより取得
            return getSrYuudentaiYouzaiItemData(itemId, srYuudentaiYouzai);
        } else {
            return null;
        }
    }

    /**
     * 計算される規格値に対して、項目データ(入力値)取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @param srYuudentaiYouzai 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量データ
     * @return 入力値
     */
    private String getTyougouryoukikakuValue(List<FXHDD01> listData, String itemId, SrYuudentaiYouzai srYuudentaiYouzai) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return StringUtil.nullToBlank(selectData.get(0).getValue()).replace("【", "").replace("】", "");
        } else if (srYuudentaiYouzai != null) {
            // 元データが存在する場合元データより取得
            return getSrYuudentaiYouzaiItemData(itemId, srYuudentaiYouzai);
        } else {
            return null;
        }
    }

    
    /**
     * 項目データ(入力値)取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @param srGlasshyoryo 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量データ
     * @return 入力値
     */
    private String getItemKikakuchi(List<FXHDD01> listData, String itemId, SrYuudentaiYouzai srYuudentaiYouzai) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return StringUtil.nullToBlank(selectData.get(0).getKikakuChi()).replace("【", "").replace("】", "");
        } else if (srYuudentaiYouzai != null) {
            // 元データが存在する場合元データより取得
            return getSrYuudentaiYouzaiItemData(itemId, srYuudentaiYouzai);
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
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量_仮登録(tmp_sr_yuudentai_youzai)登録処理
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
    private void insertTmpSrYuudentaiYouzai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "INSERT INTO tmp_sr_yuudentai_youzai ( "
                + " kojyo,lotno,edaban,yuudentaislurryhinmei,yuudentaislurrylotno,lotkubun,genryoulotno,genryoukigou,goki,"
                + "youzaihyouryoukaisinichiji,zunsanzai1_zairyouhinmei,zunsanzai1_tyougouryoukikaku,zunsanzai1_buzaizaikolotno1,"
                + "zunsanzai1_tyougouryou1,zunsanzai1_buzaizaikolotno2,zunsanzai1_tyougouryou2,zunsanzai2_zairyouhinmei,"
                + "zunsanzai2_tyougouryoukikaku,zunsanzai2_buzaizaikolotno1,zunsanzai2_tyougouryou1,zunsanzai2_buzaizaikolotno2,"
                + "zunsanzai2_tyougouryou2,youzai1_zairyouhinmei,youzai1_tyougouryoukikaku,youzai1_buzaizaikolotno1,youzai1_tyougouryou1,"
                + "youzai1_buzaizaikolotno2,youzai1_tyougouryou2,youzai2_zairyouhinmei,youzai2_tyougouryoukikaku,youzai2_buzaizaikolotno1,"
                + "youzai2_tyougouryou1,youzai2_buzaizaikolotno2,youzai2_tyougouryou2,youzai3_zairyouhinmei,youzai3_tyougouryoukikaku,"
                + "youzai3_buzaizaikolotno1,youzai3_tyougouryou1,youzai3_buzaizaikolotno2,youzai3_tyougouryou2,youzai4_zairyouhinmei,"
                + "youzai4_tyougouryoukikaku,youzai4_buzaizaikolotno1,youzai4_tyougouryou1,youzai4_buzaizaikolotno2,youzai4_tyougouryou2,"
                + "youzai5_zairyouhinmei,youzai5_tyougouryoukikaku,youzai5_buzaizaikolotno1,youzai5_tyougouryou1,youzai5_buzaizaikolotno2,"
                + "youzai5_tyougouryou2,youzai6_zairyouhinmei,youzai6_tyougouryoukikaku,youzai6_buzaizaikolotno1,youzai6_tyougouryou1,"
                + "youzai6_buzaizaikolotno2,youzai6_tyougouryou2,youzai7_zairyouhinmei,youzai7_tyougouryoukikaku,youzai7_buzaizaikolotno1,"
                + "youzai7_tyougouryou1,youzai7_buzaizaikolotno2,youzai7_tyougouryou2,youzai8_zairyouhinmei,youzai8_tyougouryoukikaku,"
                + "youzai8_buzaizaikolotno1,youzai8_tyougouryou1,youzai8_buzaizaikolotno2,youzai8_tyougouryou2,youzai9_zairyouhinmei,"
                + "youzai9_tyougouryoukikaku,youzai9_buzaizaikolotno1,youzai9_tyougouryou1,youzai9_buzaizaikolotno2,youzai9_tyougouryou2,"
                + "youzaihyouryousyuuryounichiji,kakuhanki,kaitensuu,kakuhanjikan,kakuhankaisinichiji,kakuhansyuuryounichiji,"
                + "tenkazaislurryhyouryoukaisinichiji,tenkazaislurry_zairyouhinmei,tenkazaislurry_WIPlotno,tenkazaislurry_tyougouryoukikaku,"
                + "tenkazaislurry_fuutaijyuuryou1,tenkazaislurry_tyougouryou1,tenkazaislurry_fuutaijyuuryou2,tenkazaislurry_tyougouryou2,"
                + "tenkazaislurryhyouryousyuuryounichiji,kokeibunsokuteitantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrYuudentaiYouzai(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, processData, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量_仮登録(tmp_sr_yuudentai_youzai)更新処理
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
     * @return 更新前データ
     * @throws SQLException 例外エラー
     */
    private SrYuudentaiYouzai updateTmpSrYuudentaiYouzai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "UPDATE tmp_sr_yuudentai_youzai SET "
                + " yuudentaislurryhinmei = ?,yuudentaislurrylotno = ?,lotkubun = ?,genryoulotno = ?,genryoukigou = ?,goki = ?,youzaihyouryoukaisinichiji = ?,"
                + "zunsanzai1_zairyouhinmei = ?,zunsanzai1_tyougouryoukikaku = ?,zunsanzai1_buzaizaikolotno1 = ?,zunsanzai1_tyougouryou1 = ?,zunsanzai1_buzaizaikolotno2 = ?,"
                + "zunsanzai1_tyougouryou2 = ?,zunsanzai2_zairyouhinmei = ?,zunsanzai2_tyougouryoukikaku = ?,zunsanzai2_buzaizaikolotno1 = ?,zunsanzai2_tyougouryou1 = ?,"
                + "zunsanzai2_buzaizaikolotno2 = ?,zunsanzai2_tyougouryou2 = ?,youzai1_zairyouhinmei = ?,youzai1_tyougouryoukikaku = ?,youzai1_buzaizaikolotno1 = ?,"
                + "youzai1_tyougouryou1 = ?,youzai1_buzaizaikolotno2 = ?,youzai1_tyougouryou2 = ?,youzai2_zairyouhinmei = ?,youzai2_tyougouryoukikaku = ?,"
                + "youzai2_buzaizaikolotno1 = ?,youzai2_tyougouryou1 = ?,youzai2_buzaizaikolotno2 = ?,youzai2_tyougouryou2 = ?,youzai3_zairyouhinmei = ?,"
                + "youzai3_tyougouryoukikaku = ?,youzai3_buzaizaikolotno1 = ?,youzai3_tyougouryou1 = ?,youzai3_buzaizaikolotno2 = ?,youzai3_tyougouryou2 = ?,"
                + "youzai4_zairyouhinmei = ?,youzai4_tyougouryoukikaku = ?,youzai4_buzaizaikolotno1 = ?,youzai4_tyougouryou1 = ?,youzai4_buzaizaikolotno2 = ?,"
                + "youzai4_tyougouryou2 = ?,youzai5_zairyouhinmei = ?,youzai5_tyougouryoukikaku = ?,youzai5_buzaizaikolotno1 = ?,youzai5_tyougouryou1 = ?,"
                + "youzai5_buzaizaikolotno2 = ?,youzai5_tyougouryou2 = ?,youzai6_zairyouhinmei = ?,youzai6_tyougouryoukikaku = ?,youzai6_buzaizaikolotno1 = ?,"
                + "youzai6_tyougouryou1 = ?,youzai6_buzaizaikolotno2 = ?,youzai6_tyougouryou2 = ?,youzai7_zairyouhinmei = ?,youzai7_tyougouryoukikaku = ?,"
                + "youzai7_buzaizaikolotno1 = ?,youzai7_tyougouryou1 = ?,youzai7_buzaizaikolotno2 = ?,youzai7_tyougouryou2 = ?,youzai8_zairyouhinmei = ?,"
                + "youzai8_tyougouryoukikaku = ?,youzai8_buzaizaikolotno1 = ?,youzai8_tyougouryou1 = ?,youzai8_buzaizaikolotno2 = ?,youzai8_tyougouryou2 = ?,"
                + "youzai9_zairyouhinmei = ?,youzai9_tyougouryoukikaku = ?,youzai9_buzaizaikolotno1 = ?,youzai9_tyougouryou1 = ?,youzai9_buzaizaikolotno2 = ?,"
                + "youzai9_tyougouryou2 = ?,youzaihyouryousyuuryounichiji = ?,kakuhanki = ?,kaitensuu = ?,kakuhanjikan = ?,kakuhankaisinichiji = ?,kakuhansyuuryounichiji = ?,"
                + "tenkazaislurryhyouryoukaisinichiji = ?,tenkazaislurry_zairyouhinmei = ?,tenkazaislurry_WIPlotno = ?,tenkazaislurry_tyougouryoukikaku = ?,"
                + "tenkazaislurry_fuutaijyuuryou1 = ?,tenkazaislurry_tyougouryou1 = ?,tenkazaislurry_fuutaijyuuryou2 = ?,tenkazaislurry_tyougouryou2 = ?,"
                + "tenkazaislurryhyouryousyuuryounichiji = ?,kokeibunsokuteitantousya = ?,bikou1 = ?,bikou2 = ?,kosinnichiji = ?,revision = ?,deleteflag = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrYuudentaiYouzai> srYuudentaiYouzaiList = getSrYuudentaiYouzaiData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrYuudentaiYouzai srYuudentaiYouzai = null;
        if (!srYuudentaiYouzaiList.isEmpty()) {
            srYuudentaiYouzai = srYuudentaiYouzaiList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrYuudentaiYouzai(false, newRev, 0, "", "", "", systemTime, processData, srYuudentaiYouzai);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
        return srYuudentaiYouzai;
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量_仮登録(tmp_sr_yuudentai_youzai)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrYuudentaiYouzai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sr_yuudentai_youzai "
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
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量_仮登録(tmp_sr_yuudentai_youzai)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srYuudentaiYouzai 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量データ
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrYuudentaiYouzai(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, String systemTime, ProcessData processData, SrYuudentaiYouzai srYuudentaiYouzai) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // 溶剤秤量開始日時
        String youzaihyouryoukaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B020Const.YOUZAIHYOURYOUKAISI_TIME, srYuudentaiYouzai));
        // 溶剤秤量終了日時
        String youzaihyouryousyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B020Const.YOUZAIHYOURYOUSYUURYOU_TIME, srYuudentaiYouzai));
        // 撹拌開始日時
        String kakuhankaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B020Const.KAKUHANKAISI_TIME, srYuudentaiYouzai));
        // 撹拌終了日時
        String kakuhansyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B020Const.KAKUHANSYUURYOU_TIME, srYuudentaiYouzai));
        // 添加材ｽﾗﾘｰ秤量開始日時
        String tenkazaislurryhrkaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B020Const.TENKAZAISLURRYHRKAISI_TIME, srYuudentaiYouzai));
        // 添加材ｽﾗﾘｰ秤量終了日時
        String tenkazaislurryhrsyuryoTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B020Const.TENKAZAISLURRYHRSYURYO_TIME, srYuudentaiYouzai));

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YUUDENTAISLURRYHINMEI, srYuudentaiYouzai)));                // 誘電体ｽﾗﾘｰ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YUUDENTAISLURRYLOTNO, srYuudentaiYouzai)));                 // 誘電体ｽﾗﾘｰLotNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.LOTKUBUN, srYuudentaiYouzai)));                             // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B020Const.GENRYOULOTNO, srYuudentaiYouzai)));                    // 原料LotNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B020Const.GENRYOUKIGOU, srYuudentaiYouzai)));                    // 原料記号
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.GOKI, srYuudentaiYouzai)));                                 // 秤量号機
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAIHYOURYOUKAISI_DAY, srYuudentaiYouzai),
                "".equals(youzaihyouryoukaisiTime) ? "0000" : youzaihyouryoukaisiTime));                                                                       // 溶剤秤量開始日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B020Const.ZUNSANZAI1_ZAIRYOUHINMEI, srYuudentaiYouzai)));        // 分散材①_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B020Const.ZUNSANZAI1_TYOUGOURYOUKIKAKU, srYuudentaiYouzai)));    // 分散材①_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.ZUNSANZAI1_BUZAIZAIKOLOTNO1, srYuudentaiYouzai)));          // 分散材①_部材在庫No1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.ZUNSANZAI1_TYOUGOURYOU1, srYuudentaiYouzai)));                 // 分散材①_調合量1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.ZUNSANZAI1_BUZAIZAIKOLOTNO2, srYuudentaiYouzai)));          // 分散材①_部材在庫No2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.ZUNSANZAI1_TYOUGOURYOU2, srYuudentaiYouzai)));                 // 分散材①_調合量2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B020Const.ZUNSANZAI2_ZAIRYOUHINMEI, srYuudentaiYouzai)));        // 分散材②_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B020Const.ZUNSANZAI2_TYOUGOURYOUKIKAKU, srYuudentaiYouzai)));    // 分散材②_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.ZUNSANZAI2_BUZAIZAIKOLOTNO1, srYuudentaiYouzai)));          // 分散材②_部材在庫No1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.ZUNSANZAI2_TYOUGOURYOU1, srYuudentaiYouzai)));                 // 分散材②_調合量1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.ZUNSANZAI2_BUZAIZAIKOLOTNO2, srYuudentaiYouzai)));          // 分散材②_部材在庫No2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.ZUNSANZAI2_TYOUGOURYOU2, srYuudentaiYouzai)));                 // 分散材②_調合量2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B020Const.YOUZAI1_ZAIRYOUHINMEI, srYuudentaiYouzai)));           // 溶剤①_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B020Const.YOUZAI1_TYOUGOURYOUKIKAKU, srYuudentaiYouzai)));       // 溶剤①_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI1_BUZAIZAIKOLOTNO1, srYuudentaiYouzai)));             // 溶剤①_部材在庫No1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI1_TYOUGOURYOU1, srYuudentaiYouzai)));                    // 溶剤①_調合量1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI1_BUZAIZAIKOLOTNO2, srYuudentaiYouzai)));             // 溶剤①_部材在庫No2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI1_TYOUGOURYOU2, srYuudentaiYouzai)));                    // 溶剤①_調合量2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B020Const.YOUZAI2_ZAIRYOUHINMEI, srYuudentaiYouzai)));           // 溶剤②_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B020Const.YOUZAI2_TYOUGOURYOUKIKAKU, srYuudentaiYouzai)));       // 溶剤②_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI2_BUZAIZAIKOLOTNO1, srYuudentaiYouzai)));             // 溶剤②_部材在庫No1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI2_TYOUGOURYOU1, srYuudentaiYouzai)));                    // 溶剤②_調合量1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI2_BUZAIZAIKOLOTNO2, srYuudentaiYouzai)));             // 溶剤②_部材在庫No2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI2_TYOUGOURYOU2, srYuudentaiYouzai)));                    // 溶剤②_調合量2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B020Const.YOUZAI3_ZAIRYOUHINMEI, srYuudentaiYouzai)));           // 溶剤③_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B020Const.YOUZAI3_TYOUGOURYOUKIKAKU, srYuudentaiYouzai)));       // 溶剤③_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI3_BUZAIZAIKOLOTNO1, srYuudentaiYouzai)));             // 溶剤③_部材在庫No1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI3_TYOUGOURYOU1, srYuudentaiYouzai)));                    // 溶剤③_調合量1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI3_BUZAIZAIKOLOTNO2, srYuudentaiYouzai)));             // 溶剤③_部材在庫No2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI3_TYOUGOURYOU2, srYuudentaiYouzai)));                    // 溶剤③_調合量2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B020Const.YOUZAI4_ZAIRYOUHINMEI, srYuudentaiYouzai)));           // 溶剤④_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getTyougouryoukikakuValue(pItemList, GXHDO102B020Const.YOUZAI4_TYOUGOURYOUKIKAKU, srYuudentaiYouzai))); // 溶剤④_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI4_BUZAIZAIKOLOTNO1, srYuudentaiYouzai)));             // 溶剤④_部材在庫No1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI4_TYOUGOURYOU1, srYuudentaiYouzai)));                    // 溶剤④_調合量1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI4_BUZAIZAIKOLOTNO2, srYuudentaiYouzai)));             // 溶剤④_部材在庫No2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI4_TYOUGOURYOU2, srYuudentaiYouzai)));                    // 溶剤④_調合量2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B020Const.YOUZAI5_ZAIRYOUHINMEI, srYuudentaiYouzai)));           // 溶剤⑤_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getTyougouryoukikakuValue(pItemList, GXHDO102B020Const.YOUZAI5_TYOUGOURYOUKIKAKU, srYuudentaiYouzai))); // 溶剤⑤_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI5_BUZAIZAIKOLOTNO1, srYuudentaiYouzai)));             // 溶剤⑤_部材在庫No1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI5_TYOUGOURYOU1, srYuudentaiYouzai)));                    // 溶剤⑤_調合量1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI5_BUZAIZAIKOLOTNO2, srYuudentaiYouzai)));             // 溶剤⑤_部材在庫No2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI5_TYOUGOURYOU2, srYuudentaiYouzai)));                    // 溶剤⑤_調合量2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B020Const.YOUZAI6_ZAIRYOUHINMEI, srYuudentaiYouzai)));           // 溶剤⑥_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getTyougouryoukikakuValue(pItemList, GXHDO102B020Const.YOUZAI6_TYOUGOURYOUKIKAKU, srYuudentaiYouzai))); // 溶剤⑥_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI6_BUZAIZAIKOLOTNO1, srYuudentaiYouzai)));             // 溶剤⑥_部材在庫No1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI6_TYOUGOURYOU1, srYuudentaiYouzai)));                    // 溶剤⑥_調合量1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI6_BUZAIZAIKOLOTNO2, srYuudentaiYouzai)));             // 溶剤⑥_部材在庫No2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI6_TYOUGOURYOU2, srYuudentaiYouzai)));                    // 溶剤⑥_調合量2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B020Const.YOUZAI7_ZAIRYOUHINMEI, srYuudentaiYouzai)));           // 溶剤⑦_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getTyougouryoukikakuValue(pItemList, GXHDO102B020Const.YOUZAI7_TYOUGOURYOUKIKAKU, srYuudentaiYouzai))); // 溶剤⑦_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI7_BUZAIZAIKOLOTNO1, srYuudentaiYouzai)));             // 溶剤⑦_部材在庫No1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI7_TYOUGOURYOU1, srYuudentaiYouzai)));                    // 溶剤⑦_調合量1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI7_BUZAIZAIKOLOTNO2, srYuudentaiYouzai)));             // 溶剤⑦_部材在庫No2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI7_TYOUGOURYOU2, srYuudentaiYouzai)));                    // 溶剤⑦_調合量2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B020Const.YOUZAI8_ZAIRYOUHINMEI, srYuudentaiYouzai)));           // 溶剤⑧_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getTyougouryoukikakuValue(pItemList, GXHDO102B020Const.YOUZAI8_TYOUGOURYOUKIKAKU, srYuudentaiYouzai))); // 溶剤⑧_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI8_BUZAIZAIKOLOTNO1, srYuudentaiYouzai)));             // 溶剤⑧_部材在庫No1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI8_TYOUGOURYOU1, srYuudentaiYouzai)));                    // 溶剤⑧_調合量1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI8_BUZAIZAIKOLOTNO2, srYuudentaiYouzai)));             // 溶剤⑧_部材在庫No2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI8_TYOUGOURYOU2, srYuudentaiYouzai)));                    // 溶剤⑧_調合量2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B020Const.YOUZAI9_ZAIRYOUHINMEI, srYuudentaiYouzai)));           // 溶剤⑨_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getTyougouryoukikakuValue(pItemList, GXHDO102B020Const.YOUZAI9_TYOUGOURYOUKIKAKU, srYuudentaiYouzai))); // 溶剤⑨_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI9_BUZAIZAIKOLOTNO1, srYuudentaiYouzai)));             // 溶剤⑨_部材在庫No1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI9_TYOUGOURYOU1, srYuudentaiYouzai)));                    // 溶剤⑨_調合量1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI9_BUZAIZAIKOLOTNO2, srYuudentaiYouzai)));             // 溶剤⑨_部材在庫No2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAI9_TYOUGOURYOU2, srYuudentaiYouzai)));                    // 溶剤⑨_調合量2
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.YOUZAIHYOURYOUSYUURYOU_DAY, srYuudentaiYouzai),
                "".equals(youzaihyouryousyuuryouTime) ? "0000" : youzaihyouryousyuuryouTime));                                                                 // 溶剤秤量終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B020Const.KAKUHANKI, srYuudentaiYouzai)));                       // 撹拌機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B020Const.KAITENSUU, srYuudentaiYouzai)));                       // 回転数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B020Const.KAKUHANJIKAN, srYuudentaiYouzai)));                    // 撹拌時間
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.KAKUHANKAISI_DAY, srYuudentaiYouzai),
                "".equals(kakuhankaisiTime) ? "0000" : kakuhankaisiTime));                                                                                     // 撹拌開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.KAKUHANSYUURYOU_DAY, srYuudentaiYouzai),
                "".equals(kakuhansyuuryouTime) ? "0000" : kakuhansyuuryouTime));                                                                               // 撹拌終了日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.TENKAZAISLURRYHRKAISI_DAY, srYuudentaiYouzai),
                "".equals(tenkazaislurryhrkaisiTime) ? "0000" : tenkazaislurryhrkaisiTime));                                                                   // 添加材ｽﾗﾘｰ秤量開始日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B020Const.TENKAZAISLURRY_ZAIRYOUHINMEI, srYuudentaiYouzai)));    // 添加材ｽﾗﾘｰ_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.TENKAZAISLURRY_WIPLOTNO, srYuudentaiYouzai)));              // 添加材ｽﾗﾘｰ_WIPﾛｯﾄNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getTyougouryoukikakuValue(pItemList, GXHDO102B020Const.TENKAZAISLURRY_TGRKIKAKU, srYuudentaiYouzai))); // 添加材ｽﾗﾘｰ_調合量規格
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.TENKAZAISLURRY_FTAIJYUURYOU1, srYuudentaiYouzai)));            // 添加材ｽﾗﾘｰ_風袋重量1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.TENKAZAISLURRY_TYOUGOURYOU1, srYuudentaiYouzai)));             // 添加材ｽﾗﾘｰ_調合量1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.TENKAZAISLURRY_FTAIJYUURYOU2, srYuudentaiYouzai)));            // 添加材ｽﾗﾘｰ_風袋重量2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.TENKAZAISLURRY_TYOUGOURYOU2, srYuudentaiYouzai)));             // 添加材ｽﾗﾘｰ_調合量2
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.TENKAZAISLURRYHRSYURYO_DAY, srYuudentaiYouzai),
                "".equals(tenkazaislurryhrsyuryoTime) ? "0000" : tenkazaislurryhrsyuryoTime));                                                                 // 添加材ｽﾗﾘｰ秤量終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.KOKEIBUNSOKUTEITANTOUSYA, srYuudentaiYouzai)));             // 固形分測定担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.BIKOU1, srYuudentaiYouzai)));                               // 備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B020Const.BIKOU2, srYuudentaiYouzai)));                               // 備考2

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
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量(sr_yuudentai_youzai)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param tmpSrYuudentaiYouzai 仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrYuudentaiYouzai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData, SrYuudentaiYouzai tmpSrYuudentaiYouzai) throws SQLException {

        String sql = "INSERT INTO sr_yuudentai_youzai ( "
                + " kojyo,lotno,edaban,yuudentaislurryhinmei,yuudentaislurrylotno,lotkubun,genryoulotno,genryoukigou,goki,"
                + "youzaihyouryoukaisinichiji,zunsanzai1_zairyouhinmei,zunsanzai1_tyougouryoukikaku,zunsanzai1_buzaizaikolotno1,"
                + "zunsanzai1_tyougouryou1,zunsanzai1_buzaizaikolotno2,zunsanzai1_tyougouryou2,zunsanzai2_zairyouhinmei,"
                + "zunsanzai2_tyougouryoukikaku,zunsanzai2_buzaizaikolotno1,zunsanzai2_tyougouryou1,zunsanzai2_buzaizaikolotno2,"
                + "zunsanzai2_tyougouryou2,youzai1_zairyouhinmei,youzai1_tyougouryoukikaku,youzai1_buzaizaikolotno1,youzai1_tyougouryou1,"
                + "youzai1_buzaizaikolotno2,youzai1_tyougouryou2,youzai2_zairyouhinmei,youzai2_tyougouryoukikaku,youzai2_buzaizaikolotno1,"
                + "youzai2_tyougouryou1,youzai2_buzaizaikolotno2,youzai2_tyougouryou2,youzai3_zairyouhinmei,youzai3_tyougouryoukikaku,"
                + "youzai3_buzaizaikolotno1,youzai3_tyougouryou1,youzai3_buzaizaikolotno2,youzai3_tyougouryou2,youzai4_zairyouhinmei,"
                + "youzai4_tyougouryoukikaku,youzai4_buzaizaikolotno1,youzai4_tyougouryou1,youzai4_buzaizaikolotno2,youzai4_tyougouryou2,"
                + "youzai5_zairyouhinmei,youzai5_tyougouryoukikaku,youzai5_buzaizaikolotno1,youzai5_tyougouryou1,youzai5_buzaizaikolotno2,"
                + "youzai5_tyougouryou2,youzai6_zairyouhinmei,youzai6_tyougouryoukikaku,youzai6_buzaizaikolotno1,youzai6_tyougouryou1,"
                + "youzai6_buzaizaikolotno2,youzai6_tyougouryou2,youzai7_zairyouhinmei,youzai7_tyougouryoukikaku,youzai7_buzaizaikolotno1,"
                + "youzai7_tyougouryou1,youzai7_buzaizaikolotno2,youzai7_tyougouryou2,youzai8_zairyouhinmei,youzai8_tyougouryoukikaku,"
                + "youzai8_buzaizaikolotno1,youzai8_tyougouryou1,youzai8_buzaizaikolotno2,youzai8_tyougouryou2,youzai9_zairyouhinmei,"
                + "youzai9_tyougouryoukikaku,youzai9_buzaizaikolotno1,youzai9_tyougouryou1,youzai9_buzaizaikolotno2,youzai9_tyougouryou2,"
                + "youzaihyouryousyuuryounichiji,kakuhanki,kaitensuu,kakuhanjikan,kakuhankaisinichiji,kakuhansyuuryounichiji,"
                + "tenkazaislurryhyouryoukaisinichiji,tenkazaislurry_zairyouhinmei,tenkazaislurry_WIPlotno,tenkazaislurry_tyougouryoukikaku,"
                + "tenkazaislurry_fuutaijyuuryou1,tenkazaislurry_tyougouryou1,tenkazaislurry_fuutaijyuuryou2,tenkazaislurry_tyougouryou2,"
                + "tenkazaislurryhyouryousyuuryounichiji,kokeibunsokuteitantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrYuudentaiYouzai(true, newRev, kojyo, lotNo, edaban, systemTime, processData, tmpSrYuudentaiYouzai);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量(sr_yuudentai_youzai)更新処理
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
     * @return 更新前データ
     * @throws SQLException 例外エラー
     */
    private SrYuudentaiYouzai updateSrYuudentaiYouzai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {
        String sql = "UPDATE sr_yuudentai_youzai SET "
                + " yuudentaislurryhinmei = ?,yuudentaislurrylotno = ?,lotkubun = ?,genryoulotno = ?,genryoukigou = ?,goki = ?,youzaihyouryoukaisinichiji = ?,"
                + "zunsanzai1_zairyouhinmei = ?,zunsanzai1_tyougouryoukikaku = ?,zunsanzai1_buzaizaikolotno1 = ?,zunsanzai1_tyougouryou1 = ?,zunsanzai1_buzaizaikolotno2 = ?,"
                + "zunsanzai1_tyougouryou2 = ?,zunsanzai2_zairyouhinmei = ?,zunsanzai2_tyougouryoukikaku = ?,zunsanzai2_buzaizaikolotno1 = ?,zunsanzai2_tyougouryou1 = ?,"
                + "zunsanzai2_buzaizaikolotno2 = ?,zunsanzai2_tyougouryou2 = ?,youzai1_zairyouhinmei = ?,youzai1_tyougouryoukikaku = ?,youzai1_buzaizaikolotno1 = ?,"
                + "youzai1_tyougouryou1 = ?,youzai1_buzaizaikolotno2 = ?,youzai1_tyougouryou2 = ?,youzai2_zairyouhinmei = ?,youzai2_tyougouryoukikaku = ?,"
                + "youzai2_buzaizaikolotno1 = ?,youzai2_tyougouryou1 = ?,youzai2_buzaizaikolotno2 = ?,youzai2_tyougouryou2 = ?,youzai3_zairyouhinmei = ?,"
                + "youzai3_tyougouryoukikaku = ?,youzai3_buzaizaikolotno1 = ?,youzai3_tyougouryou1 = ?,youzai3_buzaizaikolotno2 = ?,youzai3_tyougouryou2 = ?,"
                + "youzai4_zairyouhinmei = ?,youzai4_tyougouryoukikaku = ?,youzai4_buzaizaikolotno1 = ?,youzai4_tyougouryou1 = ?,youzai4_buzaizaikolotno2 = ?,"
                + "youzai4_tyougouryou2 = ?,youzai5_zairyouhinmei = ?,youzai5_tyougouryoukikaku = ?,youzai5_buzaizaikolotno1 = ?,youzai5_tyougouryou1 = ?,"
                + "youzai5_buzaizaikolotno2 = ?,youzai5_tyougouryou2 = ?,youzai6_zairyouhinmei = ?,youzai6_tyougouryoukikaku = ?,youzai6_buzaizaikolotno1 = ?,"
                + "youzai6_tyougouryou1 = ?,youzai6_buzaizaikolotno2 = ?,youzai6_tyougouryou2 = ?,youzai7_zairyouhinmei = ?,youzai7_tyougouryoukikaku = ?,"
                + "youzai7_buzaizaikolotno1 = ?,youzai7_tyougouryou1 = ?,youzai7_buzaizaikolotno2 = ?,youzai7_tyougouryou2 = ?,youzai8_zairyouhinmei = ?,"
                + "youzai8_tyougouryoukikaku = ?,youzai8_buzaizaikolotno1 = ?,youzai8_tyougouryou1 = ?,youzai8_buzaizaikolotno2 = ?,youzai8_tyougouryou2 = ?,"
                + "youzai9_zairyouhinmei = ?,youzai9_tyougouryoukikaku = ?,youzai9_buzaizaikolotno1 = ?,youzai9_tyougouryou1 = ?,youzai9_buzaizaikolotno2 = ?,"
                + "youzai9_tyougouryou2 = ?,youzaihyouryousyuuryounichiji = ?,kakuhanki = ?,kaitensuu = ?,kakuhanjikan = ?,kakuhankaisinichiji = ?,kakuhansyuuryounichiji = ?,"
                + "tenkazaislurryhyouryoukaisinichiji = ?,tenkazaislurry_zairyouhinmei = ?,tenkazaislurry_WIPlotno = ?,tenkazaislurry_tyougouryoukikaku = ?,"
                + "tenkazaislurry_fuutaijyuuryou1 = ?,tenkazaislurry_tyougouryou1 = ?,tenkazaislurry_fuutaijyuuryou2 = ?,tenkazaislurry_tyougouryou2 = ?,"
                + "tenkazaislurryhyouryousyuuryounichiji = ?,kokeibunsokuteitantousya = ?,bikou1 = ?,bikou2 = ?,kosinnichiji = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrYuudentaiYouzai> srYuudentaiYouzaiList = getSrYuudentaiYouzaiData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrYuudentaiYouzai srYuudentaiYouzai = null;
        if (!srYuudentaiYouzaiList.isEmpty()) {
            srYuudentaiYouzai = srYuudentaiYouzaiList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrYuudentaiYouzai(false, newRev, "", "", "", systemTime, processData, srYuudentaiYouzai);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
        return srYuudentaiYouzai;
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量(sr_yuudentai_youzai)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srYuudentaiYouzai 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrYuudentaiYouzai(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            String systemTime, ProcessData processData, SrYuudentaiYouzai srYuudentaiYouzai) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // 溶剤秤量開始日時
        String youzaihyouryoukaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B020Const.YOUZAIHYOURYOUKAISI_TIME, srYuudentaiYouzai));
        // 溶剤秤量終了日時
        String youzaihyouryousyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B020Const.YOUZAIHYOURYOUSYUURYOU_TIME, srYuudentaiYouzai));
        // 撹拌開始日時
        String kakuhankaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B020Const.KAKUHANKAISI_TIME, srYuudentaiYouzai));
        // 撹拌終了日時
        String kakuhansyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B020Const.KAKUHANSYUURYOU_TIME, srYuudentaiYouzai));
        // 添加材ｽﾗﾘｰ秤量開始日時
        String tenkazaislurryhrkaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B020Const.TENKAZAISLURRYHRKAISI_TIME, srYuudentaiYouzai));
        // 添加材ｽﾗﾘｰ秤量終了日時
        String tenkazaislurryhrsyuryoTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B020Const.TENKAZAISLURRYHRSYURYO_TIME, srYuudentaiYouzai));

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B020Const.YUUDENTAISLURRYHINMEI, srYuudentaiYouzai)));                // 誘電体ｽﾗﾘｰ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B020Const.YUUDENTAISLURRYLOTNO, srYuudentaiYouzai)));                 // 誘電体ｽﾗﾘｰLotNo
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B020Const.LOTKUBUN, srYuudentaiYouzai)));                             // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B020Const.GENRYOULOTNO, srYuudentaiYouzai)));                    // 原料LotNo
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B020Const.GENRYOUKIGOU, srYuudentaiYouzai)));                    // 原料記号
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B020Const.GOKI, srYuudentaiYouzai)));                                 // 秤量号機
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B020Const.YOUZAIHYOURYOUKAISI_DAY, srYuudentaiYouzai),
                "".equals(youzaihyouryoukaisiTime) ? "0000" : youzaihyouryoukaisiTime));                                                            // 溶剤秤量開始日時
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B020Const.ZUNSANZAI1_ZAIRYOUHINMEI, srYuudentaiYouzai)));        // 分散材①_材料品名
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B020Const.ZUNSANZAI1_TYOUGOURYOUKIKAKU, srYuudentaiYouzai)));    // 分散材①_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B020Const.ZUNSANZAI1_BUZAIZAIKOLOTNO1, srYuudentaiYouzai)));          // 分散材①_部材在庫No1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B020Const.ZUNSANZAI1_TYOUGOURYOU1, srYuudentaiYouzai)));                 // 分散材①_調合量1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B020Const.ZUNSANZAI1_BUZAIZAIKOLOTNO2, srYuudentaiYouzai)));          // 分散材①_部材在庫No2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B020Const.ZUNSANZAI1_TYOUGOURYOU2, srYuudentaiYouzai)));                 // 分散材①_調合量2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B020Const.ZUNSANZAI2_ZAIRYOUHINMEI, srYuudentaiYouzai)));        // 分散材②_材料品名
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B020Const.ZUNSANZAI2_TYOUGOURYOUKIKAKU, srYuudentaiYouzai)));    // 分散材②_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B020Const.ZUNSANZAI2_BUZAIZAIKOLOTNO1, srYuudentaiYouzai)));          // 分散材②_部材在庫No1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B020Const.ZUNSANZAI2_TYOUGOURYOU1, srYuudentaiYouzai)));                 // 分散材②_調合量1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B020Const.ZUNSANZAI2_BUZAIZAIKOLOTNO2, srYuudentaiYouzai)));          // 分散材②_部材在庫No2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B020Const.ZUNSANZAI2_TYOUGOURYOU2, srYuudentaiYouzai)));                 // 分散材②_調合量2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B020Const.YOUZAI1_ZAIRYOUHINMEI, srYuudentaiYouzai)));           // 溶剤①_材料品名
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B020Const.YOUZAI1_TYOUGOURYOUKIKAKU, srYuudentaiYouzai)));       // 溶剤①_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI1_BUZAIZAIKOLOTNO1, srYuudentaiYouzai)));             // 溶剤①_部材在庫No1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI1_TYOUGOURYOU1, srYuudentaiYouzai)));                    // 溶剤①_調合量1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI1_BUZAIZAIKOLOTNO2, srYuudentaiYouzai)));             // 溶剤①_部材在庫No2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI1_TYOUGOURYOU2, srYuudentaiYouzai)));                    // 溶剤①_調合量2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B020Const.YOUZAI2_ZAIRYOUHINMEI, srYuudentaiYouzai)));           // 溶剤②_材料品名
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B020Const.YOUZAI2_TYOUGOURYOUKIKAKU, srYuudentaiYouzai)));       // 溶剤②_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI2_BUZAIZAIKOLOTNO1, srYuudentaiYouzai)));             // 溶剤②_部材在庫No1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI2_TYOUGOURYOU1, srYuudentaiYouzai)));                    // 溶剤②_調合量1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI2_BUZAIZAIKOLOTNO2, srYuudentaiYouzai)));             // 溶剤②_部材在庫No2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI2_TYOUGOURYOU2, srYuudentaiYouzai)));                    // 溶剤②_調合量2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B020Const.YOUZAI3_ZAIRYOUHINMEI, srYuudentaiYouzai)));           // 溶剤③_材料品名
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B020Const.YOUZAI3_TYOUGOURYOUKIKAKU, srYuudentaiYouzai)));       // 溶剤③_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI3_BUZAIZAIKOLOTNO1, srYuudentaiYouzai)));             // 溶剤③_部材在庫No1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI3_TYOUGOURYOU1, srYuudentaiYouzai)));                    // 溶剤③_調合量1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI3_BUZAIZAIKOLOTNO2, srYuudentaiYouzai)));             // 溶剤③_部材在庫No2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI3_TYOUGOURYOU2, srYuudentaiYouzai)));                    // 溶剤③_調合量2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B020Const.YOUZAI4_ZAIRYOUHINMEI, srYuudentaiYouzai)));           // 溶剤④_材料品名
        params.add(DBUtil.stringToStringObject(getTyougouryoukikakuValue(pItemList, GXHDO102B020Const.YOUZAI4_TYOUGOURYOUKIKAKU, srYuudentaiYouzai))); // 溶剤④_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI4_BUZAIZAIKOLOTNO1, srYuudentaiYouzai)));             // 溶剤④_部材在庫No1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI4_TYOUGOURYOU1, srYuudentaiYouzai)));                    // 溶剤④_調合量1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI4_BUZAIZAIKOLOTNO2, srYuudentaiYouzai)));             // 溶剤④_部材在庫No2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI4_TYOUGOURYOU2, srYuudentaiYouzai)));                    // 溶剤④_調合量2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B020Const.YOUZAI5_ZAIRYOUHINMEI, srYuudentaiYouzai)));           // 溶剤⑤_材料品名
        params.add(DBUtil.stringToStringObject(getTyougouryoukikakuValue(pItemList, GXHDO102B020Const.YOUZAI5_TYOUGOURYOUKIKAKU, srYuudentaiYouzai))); // 溶剤⑤_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI5_BUZAIZAIKOLOTNO1, srYuudentaiYouzai)));             // 溶剤⑤_部材在庫No1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI5_TYOUGOURYOU1, srYuudentaiYouzai)));                    // 溶剤⑤_調合量1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI5_BUZAIZAIKOLOTNO2, srYuudentaiYouzai)));             // 溶剤⑤_部材在庫No2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI5_TYOUGOURYOU2, srYuudentaiYouzai)));                    // 溶剤⑤_調合量2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B020Const.YOUZAI6_ZAIRYOUHINMEI, srYuudentaiYouzai)));           // 溶剤⑥_材料品名
        params.add(DBUtil.stringToStringObject(getTyougouryoukikakuValue(pItemList, GXHDO102B020Const.YOUZAI6_TYOUGOURYOUKIKAKU, srYuudentaiYouzai)));  // 溶剤⑥_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI6_BUZAIZAIKOLOTNO1, srYuudentaiYouzai)));             // 溶剤⑥_部材在庫No1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI6_TYOUGOURYOU1, srYuudentaiYouzai)));                    // 溶剤⑥_調合量1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI6_BUZAIZAIKOLOTNO2, srYuudentaiYouzai)));             // 溶剤⑥_部材在庫No2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI6_TYOUGOURYOU2, srYuudentaiYouzai)));                    // 溶剤⑥_調合量2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B020Const.YOUZAI7_ZAIRYOUHINMEI, srYuudentaiYouzai)));           // 溶剤⑦_材料品名
        params.add(DBUtil.stringToStringObject(getTyougouryoukikakuValue(pItemList, GXHDO102B020Const.YOUZAI7_TYOUGOURYOUKIKAKU, srYuudentaiYouzai))); // 溶剤⑦_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI7_BUZAIZAIKOLOTNO1, srYuudentaiYouzai)));             // 溶剤⑦_部材在庫No1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI7_TYOUGOURYOU1, srYuudentaiYouzai)));                    // 溶剤⑦_調合量1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI7_BUZAIZAIKOLOTNO2, srYuudentaiYouzai)));             // 溶剤⑦_部材在庫No2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI7_TYOUGOURYOU2, srYuudentaiYouzai)));                    // 溶剤⑦_調合量2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B020Const.YOUZAI8_ZAIRYOUHINMEI, srYuudentaiYouzai)));           // 溶剤⑧_材料品名
        params.add(DBUtil.stringToStringObject(getTyougouryoukikakuValue(pItemList, GXHDO102B020Const.YOUZAI8_TYOUGOURYOUKIKAKU, srYuudentaiYouzai))); // 溶剤⑧_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI8_BUZAIZAIKOLOTNO1, srYuudentaiYouzai)));             // 溶剤⑧_部材在庫No1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI8_TYOUGOURYOU1, srYuudentaiYouzai)));                    // 溶剤⑧_調合量1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI8_BUZAIZAIKOLOTNO2, srYuudentaiYouzai)));             // 溶剤⑧_部材在庫No2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI8_TYOUGOURYOU2, srYuudentaiYouzai)));                    // 溶剤⑧_調合量2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B020Const.YOUZAI9_ZAIRYOUHINMEI, srYuudentaiYouzai)));           // 溶剤⑨_材料品名
        params.add(DBUtil.stringToStringObject(getTyougouryoukikakuValue(pItemList, GXHDO102B020Const.YOUZAI9_TYOUGOURYOUKIKAKU, srYuudentaiYouzai))); // 溶剤⑨_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI9_BUZAIZAIKOLOTNO1, srYuudentaiYouzai)));             // 溶剤⑨_部材在庫No1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI9_TYOUGOURYOU1, srYuudentaiYouzai)));                    // 溶剤⑨_調合量1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI9_BUZAIZAIKOLOTNO2, srYuudentaiYouzai)));             // 溶剤⑨_部材在庫No2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B020Const.YOUZAI9_TYOUGOURYOU2, srYuudentaiYouzai)));                    // 溶剤⑨_調合量2
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B020Const.YOUZAIHYOURYOUSYUURYOU_DAY, srYuudentaiYouzai),
                "".equals(youzaihyouryousyuuryouTime) ? "0000" : youzaihyouryousyuuryouTime));                                                      // 溶剤秤量終了日時
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B020Const.KAKUHANKI, srYuudentaiYouzai)));                       // 撹拌機
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B020Const.KAITENSUU, srYuudentaiYouzai)));                       // 回転数
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B020Const.KAKUHANJIKAN, srYuudentaiYouzai)));                    // 撹拌時間
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B020Const.KAKUHANKAISI_DAY, srYuudentaiYouzai),
                "".equals(kakuhankaisiTime) ? "0000" : kakuhankaisiTime));                                                                          // 撹拌開始日時
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B020Const.KAKUHANSYUURYOU_DAY, srYuudentaiYouzai),
                "".equals(kakuhansyuuryouTime) ? "0000" : kakuhansyuuryouTime));                                                                    // 撹拌終了日時
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B020Const.TENKAZAISLURRYHRKAISI_DAY, srYuudentaiYouzai),
                "".equals(tenkazaislurryhrkaisiTime) ? "0000" : tenkazaislurryhrkaisiTime));                                                        // 添加材ｽﾗﾘｰ秤量開始日時
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B020Const.TENKAZAISLURRY_ZAIRYOUHINMEI, srYuudentaiYouzai)));    // 添加材ｽﾗﾘｰ_材料品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B020Const.TENKAZAISLURRY_WIPLOTNO, srYuudentaiYouzai)));              // 添加材ｽﾗﾘｰ_WIPﾛｯﾄNo
        params.add(DBUtil.stringToStringObject(getTyougouryoukikakuValue(pItemList, GXHDO102B020Const.TENKAZAISLURRY_TGRKIKAKU, srYuudentaiYouzai))); // 添加材ｽﾗﾘｰ_調合量規格
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B020Const.TENKAZAISLURRY_FTAIJYUURYOU1, srYuudentaiYouzai)));            // 添加材ｽﾗﾘｰ_風袋重量1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B020Const.TENKAZAISLURRY_TYOUGOURYOU1, srYuudentaiYouzai)));             // 添加材ｽﾗﾘｰ_調合量1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B020Const.TENKAZAISLURRY_FTAIJYUURYOU2, srYuudentaiYouzai)));            // 添加材ｽﾗﾘｰ_風袋重量2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B020Const.TENKAZAISLURRY_TYOUGOURYOU2, srYuudentaiYouzai)));             // 添加材ｽﾗﾘｰ_調合量2
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B020Const.TENKAZAISLURRYHRSYURYO_DAY, srYuudentaiYouzai),
                "".equals(tenkazaislurryhrsyuryoTime) ? "0000" : tenkazaislurryhrsyuryoTime));                                                      // 添加材ｽﾗﾘｰ秤量終了日時
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B020Const.KOKEIBUNSOKUTEITANTOUSYA, srYuudentaiYouzai)));             // 固形分測定担当者
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B020Const.BIKOU1, srYuudentaiYouzai)));                               // 備考1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B020Const.BIKOU2, srYuudentaiYouzai)));                               // 備考2

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
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量(sr_yuudentai_youzai)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSrYuudentaiYouzai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sr_yuudentai_youzai "
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
     * [誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_yuudentai_youzai "
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
     * @param srYuudentaiYouzai 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量データ
     * @return DB値
     */
    private String getSrYuudentaiYouzaiItemData(String itemId, SrYuudentaiYouzai srYuudentaiYouzai) {
        switch (itemId) {

            // 誘電体ｽﾗﾘｰ品名
            case GXHDO102B020Const.YUUDENTAISLURRYHINMEI:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYuudentaislurryhinmei());

            // 誘電体ｽﾗﾘｰLotNo
            case GXHDO102B020Const.YUUDENTAISLURRYLOTNO:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYuudentaislurrylotno());

            // ﾛｯﾄ区分
            case GXHDO102B020Const.LOTKUBUN:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getLotkubun());

            // 原料LotNo
            case GXHDO102B020Const.GENRYOULOTNO:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getGenryoulotno());

            // 原料記号
            case GXHDO102B020Const.GENRYOUKIGOU:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getGenryoukigou());

            // 秤量号機
            case GXHDO102B020Const.GOKI:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getGoki());

            // 溶剤秤量開始日
            case GXHDO102B020Const.YOUZAIHYOURYOUKAISI_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiYouzai.getYouzaihyouryoukaisinichiji(), "yyMMdd");

            // 溶剤秤量開始時間
            case GXHDO102B020Const.YOUZAIHYOURYOUKAISI_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiYouzai.getYouzaihyouryoukaisinichiji(), "HHmm");

            // 分散材①_材料品名
            case GXHDO102B020Const.ZUNSANZAI1_ZAIRYOUHINMEI:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getZunsanzai1_zairyouhinmei());

            // 分散材①_調合量規格
            case GXHDO102B020Const.ZUNSANZAI1_TYOUGOURYOUKIKAKU:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getZunsanzai1_tyougouryoukikaku());

            // 分散材①_部材在庫No1
            case GXHDO102B020Const.ZUNSANZAI1_BUZAIZAIKOLOTNO1:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getZunsanzai1_buzaizaikolotno1());

            // 分散材①_調合量1
            case GXHDO102B020Const.ZUNSANZAI1_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getZunsanzai1_tyougouryou1());

            // 分散材①_部材在庫No2
            case GXHDO102B020Const.ZUNSANZAI1_BUZAIZAIKOLOTNO2:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getZunsanzai1_buzaizaikolotno2());

            // 分散材①_調合量2
            case GXHDO102B020Const.ZUNSANZAI1_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getZunsanzai1_tyougouryou2());

            // 分散材②_材料品名
            case GXHDO102B020Const.ZUNSANZAI2_ZAIRYOUHINMEI:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getZunsanzai2_zairyouhinmei());

            // 分散材②_調合量規格
            case GXHDO102B020Const.ZUNSANZAI2_TYOUGOURYOUKIKAKU:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getZunsanzai2_tyougouryoukikaku());

            // 分散材②_部材在庫No1
            case GXHDO102B020Const.ZUNSANZAI2_BUZAIZAIKOLOTNO1:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getZunsanzai2_buzaizaikolotno1());

            // 分散材②_調合量1
            case GXHDO102B020Const.ZUNSANZAI2_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getZunsanzai2_tyougouryou1());

            // 分散材②_部材在庫No2
            case GXHDO102B020Const.ZUNSANZAI2_BUZAIZAIKOLOTNO2:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getZunsanzai2_buzaizaikolotno2());

            // 分散材②_調合量2
            case GXHDO102B020Const.ZUNSANZAI2_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getZunsanzai2_tyougouryou2());

            // 溶剤①_材料品名
            case GXHDO102B020Const.YOUZAI1_ZAIRYOUHINMEI:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai1_zairyouhinmei());

            // 溶剤①_調合量規格
            case GXHDO102B020Const.YOUZAI1_TYOUGOURYOUKIKAKU:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai1_tyougouryoukikaku());

            // 溶剤①_部材在庫No1
            case GXHDO102B020Const.YOUZAI1_BUZAIZAIKOLOTNO1:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai1_buzaizaikolotno1());

            // 溶剤①_調合量1
            case GXHDO102B020Const.YOUZAI1_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai1_tyougouryou1());

            // 溶剤①_部材在庫No2
            case GXHDO102B020Const.YOUZAI1_BUZAIZAIKOLOTNO2:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai1_buzaizaikolotno2());

            // 溶剤①_調合量2
            case GXHDO102B020Const.YOUZAI1_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai1_tyougouryou2());

            // 溶剤②_材料品名
            case GXHDO102B020Const.YOUZAI2_ZAIRYOUHINMEI:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai2_zairyouhinmei());

            // 溶剤②_調合量規格
            case GXHDO102B020Const.YOUZAI2_TYOUGOURYOUKIKAKU:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai2_tyougouryoukikaku());

            // 溶剤②_部材在庫No1
            case GXHDO102B020Const.YOUZAI2_BUZAIZAIKOLOTNO1:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai2_buzaizaikolotno1());

            // 溶剤②_調合量1
            case GXHDO102B020Const.YOUZAI2_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai2_tyougouryou1());

            // 溶剤②_部材在庫No2
            case GXHDO102B020Const.YOUZAI2_BUZAIZAIKOLOTNO2:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai2_buzaizaikolotno2());

            // 溶剤②_調合量2
            case GXHDO102B020Const.YOUZAI2_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai2_tyougouryou2());

            // 溶剤③_材料品名
            case GXHDO102B020Const.YOUZAI3_ZAIRYOUHINMEI:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai3_zairyouhinmei());

            // 溶剤③_調合量規格
            case GXHDO102B020Const.YOUZAI3_TYOUGOURYOUKIKAKU:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai3_tyougouryoukikaku());

            // 溶剤③_部材在庫No1
            case GXHDO102B020Const.YOUZAI3_BUZAIZAIKOLOTNO1:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai3_buzaizaikolotno1());

            // 溶剤③_調合量1
            case GXHDO102B020Const.YOUZAI3_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai3_tyougouryou1());

            // 溶剤③_部材在庫No2
            case GXHDO102B020Const.YOUZAI3_BUZAIZAIKOLOTNO2:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai3_buzaizaikolotno2());

            // 溶剤③_調合量2
            case GXHDO102B020Const.YOUZAI3_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai3_tyougouryou2());

            // 溶剤④_材料品名
            case GXHDO102B020Const.YOUZAI4_ZAIRYOUHINMEI:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai4_zairyouhinmei());

            // 溶剤④_調合量規格
            case GXHDO102B020Const.YOUZAI4_TYOUGOURYOUKIKAKU:
                return "【" + StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai4_tyougouryoukikaku()) + "】";

            // 溶剤④_部材在庫No1
            case GXHDO102B020Const.YOUZAI4_BUZAIZAIKOLOTNO1:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai4_buzaizaikolotno1());

            // 溶剤④_調合量1
            case GXHDO102B020Const.YOUZAI4_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai4_tyougouryou1());

            // 溶剤④_部材在庫No2
            case GXHDO102B020Const.YOUZAI4_BUZAIZAIKOLOTNO2:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai4_buzaizaikolotno2());

            // 溶剤④_調合量2
            case GXHDO102B020Const.YOUZAI4_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai4_tyougouryou2());

            // 溶剤⑤_材料品名
            case GXHDO102B020Const.YOUZAI5_ZAIRYOUHINMEI:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai5_zairyouhinmei());

            // 溶剤⑤_調合量規格
            case GXHDO102B020Const.YOUZAI5_TYOUGOURYOUKIKAKU:
                return "【" + StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai5_tyougouryoukikaku()) + "】";

            // 溶剤⑤_部材在庫No1
            case GXHDO102B020Const.YOUZAI5_BUZAIZAIKOLOTNO1:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai5_buzaizaikolotno1());

            // 溶剤⑤_調合量1
            case GXHDO102B020Const.YOUZAI5_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai5_tyougouryou1());

            // 溶剤⑤_部材在庫No2
            case GXHDO102B020Const.YOUZAI5_BUZAIZAIKOLOTNO2:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai5_buzaizaikolotno2());

            // 溶剤⑤_調合量2
            case GXHDO102B020Const.YOUZAI5_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai5_tyougouryou2());

            // 溶剤⑥_材料品名
            case GXHDO102B020Const.YOUZAI6_ZAIRYOUHINMEI:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai6_zairyouhinmei());

            // 溶剤⑥_調合量規格
            case GXHDO102B020Const.YOUZAI6_TYOUGOURYOUKIKAKU:
                return "【" + StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai6_tyougouryoukikaku()) + "】";

            // 溶剤⑥_部材在庫No1
            case GXHDO102B020Const.YOUZAI6_BUZAIZAIKOLOTNO1:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai6_buzaizaikolotno1());

            // 溶剤⑥_調合量1
            case GXHDO102B020Const.YOUZAI6_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai6_tyougouryou1());

            // 溶剤⑥_部材在庫No2
            case GXHDO102B020Const.YOUZAI6_BUZAIZAIKOLOTNO2:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai6_buzaizaikolotno2());

            // 溶剤⑥_調合量2
            case GXHDO102B020Const.YOUZAI6_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai6_tyougouryou2());

            // 溶剤⑦_材料品名
            case GXHDO102B020Const.YOUZAI7_ZAIRYOUHINMEI:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai7_zairyouhinmei());

            // 溶剤⑦_調合量規格
            case GXHDO102B020Const.YOUZAI7_TYOUGOURYOUKIKAKU:
                return "【" + StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai7_tyougouryoukikaku()) + "】";

            // 溶剤⑦_部材在庫No1
            case GXHDO102B020Const.YOUZAI7_BUZAIZAIKOLOTNO1:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai7_buzaizaikolotno1());

            // 溶剤⑦_調合量1
            case GXHDO102B020Const.YOUZAI7_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai7_tyougouryou1());

            // 溶剤⑦_部材在庫No2
            case GXHDO102B020Const.YOUZAI7_BUZAIZAIKOLOTNO2:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai7_buzaizaikolotno2());

            // 溶剤⑦_調合量2
            case GXHDO102B020Const.YOUZAI7_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai7_tyougouryou2());

            // 溶剤⑧_材料品名
            case GXHDO102B020Const.YOUZAI8_ZAIRYOUHINMEI:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai8_zairyouhinmei());

            // 溶剤⑧_調合量規格
            case GXHDO102B020Const.YOUZAI8_TYOUGOURYOUKIKAKU:
                return "【" + StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai8_tyougouryoukikaku()) + "】";

            // 溶剤⑧_部材在庫No1
            case GXHDO102B020Const.YOUZAI8_BUZAIZAIKOLOTNO1:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai8_buzaizaikolotno1());

            // 溶剤⑧_調合量1
            case GXHDO102B020Const.YOUZAI8_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai8_tyougouryou1());

            // 溶剤⑧_部材在庫No2
            case GXHDO102B020Const.YOUZAI8_BUZAIZAIKOLOTNO2:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai8_buzaizaikolotno2());

            // 溶剤⑧_調合量2
            case GXHDO102B020Const.YOUZAI8_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai8_tyougouryou2());

            // 溶剤⑨_材料品名
            case GXHDO102B020Const.YOUZAI9_ZAIRYOUHINMEI:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai9_zairyouhinmei());

            // 溶剤⑨_調合量規格
            case GXHDO102B020Const.YOUZAI9_TYOUGOURYOUKIKAKU:
                return "【" + StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai9_tyougouryoukikaku()) + "】";

            // 溶剤⑨_部材在庫No1
            case GXHDO102B020Const.YOUZAI9_BUZAIZAIKOLOTNO1:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai9_buzaizaikolotno1());

            // 溶剤⑨_調合量1
            case GXHDO102B020Const.YOUZAI9_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai9_tyougouryou1());

            // 溶剤⑨_部材在庫No2
            case GXHDO102B020Const.YOUZAI9_BUZAIZAIKOLOTNO2:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai9_buzaizaikolotno2());

            // 溶剤⑨_調合量2
            case GXHDO102B020Const.YOUZAI9_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getYouzai9_tyougouryou2());

            // 溶剤秤量終了日
            case GXHDO102B020Const.YOUZAIHYOURYOUSYUURYOU_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiYouzai.getYouzaihyouryousyuuryounichiji(), "yyMMdd");

            // 溶剤秤量終了時間
            case GXHDO102B020Const.YOUZAIHYOURYOUSYUURYOU_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiYouzai.getYouzaihyouryousyuuryounichiji(), "HHmm");

            // 撹拌機
            case GXHDO102B020Const.KAKUHANKI:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getKakuhanki());

            // 回転数
            case GXHDO102B020Const.KAITENSUU:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getKaitensuu());

            // 撹拌時間
            case GXHDO102B020Const.KAKUHANJIKAN:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getKakuhanjikan());

            // 撹拌開始日
            case GXHDO102B020Const.KAKUHANKAISI_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiYouzai.getKakuhankaisinichiji(), "yyMMdd");

            // 撹拌開始時間
            case GXHDO102B020Const.KAKUHANKAISI_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiYouzai.getKakuhankaisinichiji(), "HHmm");

            // 撹拌終了日
            case GXHDO102B020Const.KAKUHANSYUURYOU_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiYouzai.getKakuhansyuuryounichiji(), "yyMMdd");

            // 撹拌終了時間
            case GXHDO102B020Const.KAKUHANSYUURYOU_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiYouzai.getKakuhansyuuryounichiji(), "HHmm");

            // 添加材ｽﾗﾘｰ秤量開始日
            case GXHDO102B020Const.TENKAZAISLURRYHRKAISI_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiYouzai.getTenkazaislurryhyouryoukaisinichiji(), "yyMMdd");

            // 添加材ｽﾗﾘｰ秤量開始時間
            case GXHDO102B020Const.TENKAZAISLURRYHRKAISI_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiYouzai.getTenkazaislurryhyouryoukaisinichiji(), "HHmm");

            // 添加材ｽﾗﾘｰ_材料品名
            case GXHDO102B020Const.TENKAZAISLURRY_ZAIRYOUHINMEI:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getTenkazaislurry_zairyouhinmei());

            // 添加材ｽﾗﾘｰ_WIPﾛｯﾄNo
            case GXHDO102B020Const.TENKAZAISLURRY_WIPLOTNO:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getTenkazaislurry_WIPlotno());

            // 添加材ｽﾗﾘｰ_調合量規格
            case GXHDO102B020Const.TENKAZAISLURRY_TGRKIKAKU:
                return "【" + StringUtil.nullToBlank(srYuudentaiYouzai.getTenkazaislurry_tyougouryoukikaku()) + "】";

            // 添加材ｽﾗﾘｰ_風袋重量1
            case GXHDO102B020Const.TENKAZAISLURRY_FTAIJYUURYOU1:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getTenkazaislurry_fuutaijyuuryou1());

            // 添加材ｽﾗﾘｰ_調合量1
            case GXHDO102B020Const.TENKAZAISLURRY_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getTenkazaislurry_tyougouryou1());

            // 添加材ｽﾗﾘｰ_風袋重量2
            case GXHDO102B020Const.TENKAZAISLURRY_FTAIJYUURYOU2:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getTenkazaislurry_fuutaijyuuryou2());

            // 添加材ｽﾗﾘｰ_調合量2
            case GXHDO102B020Const.TENKAZAISLURRY_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getTenkazaislurry_tyougouryou2());

            // 添加材ｽﾗﾘｰ秤量終了日
            case GXHDO102B020Const.TENKAZAISLURRYHRSYURYO_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiYouzai.getTenkazaislurryhyouryousyuuryounichiji(), "yyMMdd");

            // 添加材ｽﾗﾘｰ秤量終了時間
            case GXHDO102B020Const.TENKAZAISLURRYHRSYURYO_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiYouzai.getTenkazaislurryhyouryousyuuryounichiji(), "HHmm");

            // 固形分測定担当者
            case GXHDO102B020Const.KOKEIBUNSOKUTEITANTOUSYA:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getKokeibunsokuteitantousya());

            // 備考1
            case GXHDO102B020Const.BIKOU1:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getBikou1());

            // 備考2
            case GXHDO102B020Const.BIKOU2:
                return StringUtil.nullToBlank(srYuudentaiYouzai.getBikou2());

            default:
                return null;
        }
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量_仮登録(tmp_sr_yuudentai_youzai)登録処理(削除時)
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
    private void insertDeleteDataTmpSrYuudentaiYouzai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_yuudentai_youzai ("
                + " kojyo,lotno,edaban,yuudentaislurryhinmei,yuudentaislurrylotno,lotkubun,genryoulotno,genryoukigou,goki,"
                + "youzaihyouryoukaisinichiji,zunsanzai1_zairyouhinmei,zunsanzai1_tyougouryoukikaku,zunsanzai1_buzaizaikolotno1,"
                + "zunsanzai1_tyougouryou1,zunsanzai1_buzaizaikolotno2,zunsanzai1_tyougouryou2,zunsanzai2_zairyouhinmei,"
                + "zunsanzai2_tyougouryoukikaku,zunsanzai2_buzaizaikolotno1,zunsanzai2_tyougouryou1,zunsanzai2_buzaizaikolotno2,"
                + "zunsanzai2_tyougouryou2,youzai1_zairyouhinmei,youzai1_tyougouryoukikaku,youzai1_buzaizaikolotno1,youzai1_tyougouryou1,"
                + "youzai1_buzaizaikolotno2,youzai1_tyougouryou2,youzai2_zairyouhinmei,youzai2_tyougouryoukikaku,youzai2_buzaizaikolotno1,"
                + "youzai2_tyougouryou1,youzai2_buzaizaikolotno2,youzai2_tyougouryou2,youzai3_zairyouhinmei,youzai3_tyougouryoukikaku,"
                + "youzai3_buzaizaikolotno1,youzai3_tyougouryou1,youzai3_buzaizaikolotno2,youzai3_tyougouryou2,youzai4_zairyouhinmei,"
                + "youzai4_tyougouryoukikaku,youzai4_buzaizaikolotno1,youzai4_tyougouryou1,youzai4_buzaizaikolotno2,youzai4_tyougouryou2,"
                + "youzai5_zairyouhinmei,youzai5_tyougouryoukikaku,youzai5_buzaizaikolotno1,youzai5_tyougouryou1,youzai5_buzaizaikolotno2,"
                + "youzai5_tyougouryou2,youzai6_zairyouhinmei,youzai6_tyougouryoukikaku,youzai6_buzaizaikolotno1,youzai6_tyougouryou1,"
                + "youzai6_buzaizaikolotno2,youzai6_tyougouryou2,youzai7_zairyouhinmei,youzai7_tyougouryoukikaku,youzai7_buzaizaikolotno1,"
                + "youzai7_tyougouryou1,youzai7_buzaizaikolotno2,youzai7_tyougouryou2,youzai8_zairyouhinmei,youzai8_tyougouryoukikaku,"
                + "youzai8_buzaizaikolotno1,youzai8_tyougouryou1,youzai8_buzaizaikolotno2,youzai8_tyougouryou2,youzai9_zairyouhinmei,"
                + "youzai9_tyougouryoukikaku,youzai9_buzaizaikolotno1,youzai9_tyougouryou1,youzai9_buzaizaikolotno2,youzai9_tyougouryou2,"
                + "youzaihyouryousyuuryounichiji,kakuhanki,kaitensuu,kakuhanjikan,kakuhankaisinichiji,kakuhansyuuryounichiji,"
                + "tenkazaislurryhyouryoukaisinichiji,tenkazaislurry_zairyouhinmei,tenkazaislurry_WIPlotno,tenkazaislurry_tyougouryoukikaku,"
                + "tenkazaislurry_fuutaijyuuryou1,tenkazaislurry_tyougouryou1,tenkazaislurry_fuutaijyuuryou2,tenkazaislurry_tyougouryou2,"
                + "tenkazaislurryhyouryousyuuryounichiji,kokeibunsokuteitantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") SELECT "
                + " kojyo,lotno,edaban,yuudentaislurryhinmei,yuudentaislurrylotno,lotkubun,genryoulotno,genryoukigou,goki,"
                + "youzaihyouryoukaisinichiji,zunsanzai1_zairyouhinmei,zunsanzai1_tyougouryoukikaku,zunsanzai1_buzaizaikolotno1,"
                + "zunsanzai1_tyougouryou1,zunsanzai1_buzaizaikolotno2,zunsanzai1_tyougouryou2,zunsanzai2_zairyouhinmei,"
                + "zunsanzai2_tyougouryoukikaku,zunsanzai2_buzaizaikolotno1,zunsanzai2_tyougouryou1,zunsanzai2_buzaizaikolotno2,"
                + "zunsanzai2_tyougouryou2,youzai1_zairyouhinmei,youzai1_tyougouryoukikaku,youzai1_buzaizaikolotno1,youzai1_tyougouryou1,"
                + "youzai1_buzaizaikolotno2,youzai1_tyougouryou2,youzai2_zairyouhinmei,youzai2_tyougouryoukikaku,youzai2_buzaizaikolotno1,"
                + "youzai2_tyougouryou1,youzai2_buzaizaikolotno2,youzai2_tyougouryou2,youzai3_zairyouhinmei,youzai3_tyougouryoukikaku,"
                + "youzai3_buzaizaikolotno1,youzai3_tyougouryou1,youzai3_buzaizaikolotno2,youzai3_tyougouryou2,youzai4_zairyouhinmei,"
                + "youzai4_tyougouryoukikaku,youzai4_buzaizaikolotno1,youzai4_tyougouryou1,youzai4_buzaizaikolotno2,youzai4_tyougouryou2,"
                + "youzai5_zairyouhinmei,youzai5_tyougouryoukikaku,youzai5_buzaizaikolotno1,youzai5_tyougouryou1,youzai5_buzaizaikolotno2,"
                + "youzai5_tyougouryou2,youzai6_zairyouhinmei,youzai6_tyougouryoukikaku,youzai6_buzaizaikolotno1,youzai6_tyougouryou1,"
                + "youzai6_buzaizaikolotno2,youzai6_tyougouryou2,youzai7_zairyouhinmei,youzai7_tyougouryoukikaku,youzai7_buzaizaikolotno1,"
                + "youzai7_tyougouryou1,youzai7_buzaizaikolotno2,youzai7_tyougouryou2,youzai8_zairyouhinmei,youzai8_tyougouryoukikaku,"
                + "youzai8_buzaizaikolotno1,youzai8_tyougouryou1,youzai8_buzaizaikolotno2,youzai8_tyougouryou2,youzai9_zairyouhinmei,"
                + "youzai9_tyougouryoukikaku,youzai9_buzaizaikolotno1,youzai9_tyougouryou1,youzai9_buzaizaikolotno2,youzai9_tyougouryou2,"
                + "youzaihyouryousyuuryounichiji,kakuhanki,kaitensuu,kakuhanjikan,kakuhankaisinichiji,kakuhansyuuryounichiji,"
                + "tenkazaislurryhyouryoukaisinichiji,tenkazaislurry_zairyouhinmei,tenkazaislurry_WIPlotno,tenkazaislurry_tyougouryoukikaku,"
                + "tenkazaislurry_fuutaijyuuryou1,tenkazaislurry_tyougouryou1,tenkazaislurry_fuutaijyuuryou2,tenkazaislurry_tyougouryou2,"
                + "tenkazaislurryhyouryousyuuryounichiji,kokeibunsokuteitantousya,bikou1,bikou2,?,?,?,? "
                + " FROM sr_yuudentai_youzai "
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
    private void initGXHDO102B020A(ProcessData processData) {
        GXHDO102B020A bean = (GXHDO102B020A) getFormBean("gXHDO102B020A");
        bean.setWiplotno(getItemRow(processData.getItemList(), GXHDO102B020Const.WIPLOTNO));
        bean.setYuudentaislurryhinmei(getItemRow(processData.getItemList(), GXHDO102B020Const.YUUDENTAISLURRYHINMEI));
        bean.setYuudentaislurrylotno(getItemRow(processData.getItemList(), GXHDO102B020Const.YUUDENTAISLURRYLOTNO));
        bean.setLotkubun(getItemRow(processData.getItemList(), GXHDO102B020Const.LOTKUBUN));
        bean.setGenryoulotno(getItemRow(processData.getItemList(), GXHDO102B020Const.GENRYOULOTNO));
        bean.setGenryoukigou(getItemRow(processData.getItemList(), GXHDO102B020Const.GENRYOUKIGOU));
        bean.setGoki(getItemRow(processData.getItemList(), GXHDO102B020Const.GOKI));
        bean.setYouzaihyouryoukaisi_day(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAIHYOURYOUKAISI_DAY));
        bean.setYouzaihyouryoukaisi_time(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAIHYOURYOUKAISI_TIME));
        bean.setZunsanzai1_zairyouhinmei(getItemRow(processData.getItemList(), GXHDO102B020Const.ZUNSANZAI1_ZAIRYOUHINMEI));
        bean.setZunsanzai1_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B020Const.ZUNSANZAI1_TYOUGOURYOUKIKAKU));
        bean.setZunsanzai1_buzaizaikolotno1(getItemRow(processData.getItemList(), GXHDO102B020Const.ZUNSANZAI1_BUZAIZAIKOLOTNO1));
        bean.setZunsanzai1_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B020Const.ZUNSANZAI1_TYOUGOURYOU1));
        bean.setZunsanzai1_buzaizaikolotno2(getItemRow(processData.getItemList(), GXHDO102B020Const.ZUNSANZAI1_BUZAIZAIKOLOTNO2));
        bean.setZunsanzai1_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B020Const.ZUNSANZAI1_TYOUGOURYOU2));
        bean.setZunsanzai2_zairyouhinmei(getItemRow(processData.getItemList(), GXHDO102B020Const.ZUNSANZAI2_ZAIRYOUHINMEI));
        bean.setZunsanzai2_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B020Const.ZUNSANZAI2_TYOUGOURYOUKIKAKU));
        bean.setZunsanzai2_buzaizaikolotno1(getItemRow(processData.getItemList(), GXHDO102B020Const.ZUNSANZAI2_BUZAIZAIKOLOTNO1));
        bean.setZunsanzai2_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B020Const.ZUNSANZAI2_TYOUGOURYOU1));
        bean.setZunsanzai2_buzaizaikolotno2(getItemRow(processData.getItemList(), GXHDO102B020Const.ZUNSANZAI2_BUZAIZAIKOLOTNO2));
        bean.setZunsanzai2_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B020Const.ZUNSANZAI2_TYOUGOURYOU2));
        bean.setYouzai1_zairyouhinmei(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI1_ZAIRYOUHINMEI));
        bean.setYouzai1_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI1_TYOUGOURYOUKIKAKU));
        bean.setYouzai1_buzaizaikolotno1(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI1_BUZAIZAIKOLOTNO1));
        bean.setYouzai1_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI1_TYOUGOURYOU1));
        bean.setYouzai1_buzaizaikolotno2(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI1_BUZAIZAIKOLOTNO2));
        bean.setYouzai1_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI1_TYOUGOURYOU2));
        bean.setYouzai2_zairyouhinmei(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI2_ZAIRYOUHINMEI));
        bean.setYouzai2_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI2_TYOUGOURYOUKIKAKU));
        bean.setYouzai2_buzaizaikolotno1(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI2_BUZAIZAIKOLOTNO1));
        bean.setYouzai2_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI2_TYOUGOURYOU1));
        bean.setYouzai2_buzaizaikolotno2(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI2_BUZAIZAIKOLOTNO2));
        bean.setYouzai2_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI2_TYOUGOURYOU2));
        bean.setYouzai3_zairyouhinmei(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI3_ZAIRYOUHINMEI));
        bean.setYouzai3_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI3_TYOUGOURYOUKIKAKU));
        bean.setYouzai3_buzaizaikolotno1(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI3_BUZAIZAIKOLOTNO1));
        bean.setYouzai3_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI3_TYOUGOURYOU1));
        bean.setYouzai3_buzaizaikolotno2(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI3_BUZAIZAIKOLOTNO2));
        bean.setYouzai3_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI3_TYOUGOURYOU2));
        bean.setYouzai4_zairyouhinmei(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI4_ZAIRYOUHINMEI));
        bean.setYouzai4_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI4_TYOUGOURYOUKIKAKU));
        bean.setYouzai4_buzaizaikolotno1(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI4_BUZAIZAIKOLOTNO1));
        bean.setYouzai4_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI4_TYOUGOURYOU1));
        bean.setYouzai4_buzaizaikolotno2(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI4_BUZAIZAIKOLOTNO2));
        bean.setYouzai4_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI4_TYOUGOURYOU2));
        bean.setYouzai5_zairyouhinmei(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI5_ZAIRYOUHINMEI));
        bean.setYouzai5_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI5_TYOUGOURYOUKIKAKU));
        bean.setYouzai5_buzaizaikolotno1(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI5_BUZAIZAIKOLOTNO1));
        bean.setYouzai5_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI5_TYOUGOURYOU1));
        bean.setYouzai5_buzaizaikolotno2(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI5_BUZAIZAIKOLOTNO2));
        bean.setYouzai5_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI5_TYOUGOURYOU2));
        bean.setYouzai6_zairyouhinmei(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI6_ZAIRYOUHINMEI));
        bean.setYouzai6_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI6_TYOUGOURYOUKIKAKU));
        bean.setYouzai6_buzaizaikolotno1(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI6_BUZAIZAIKOLOTNO1));
        bean.setYouzai6_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI6_TYOUGOURYOU1));
        bean.setYouzai6_buzaizaikolotno2(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI6_BUZAIZAIKOLOTNO2));
        bean.setYouzai6_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI6_TYOUGOURYOU2));
        bean.setYouzai7_zairyouhinmei(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI7_ZAIRYOUHINMEI));
        bean.setYouzai7_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI7_TYOUGOURYOUKIKAKU));
        bean.setYouzai7_buzaizaikolotno1(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI7_BUZAIZAIKOLOTNO1));
        bean.setYouzai7_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI7_TYOUGOURYOU1));
        bean.setYouzai7_buzaizaikolotno2(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI7_BUZAIZAIKOLOTNO2));
        bean.setYouzai7_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI7_TYOUGOURYOU2));
        bean.setYouzai8_zairyouhinmei(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI8_ZAIRYOUHINMEI));
        bean.setYouzai8_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI8_TYOUGOURYOUKIKAKU));
        bean.setYouzai8_buzaizaikolotno1(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI8_BUZAIZAIKOLOTNO1));
        bean.setYouzai8_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI8_TYOUGOURYOU1));
        bean.setYouzai8_buzaizaikolotno2(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI8_BUZAIZAIKOLOTNO2));
        bean.setYouzai8_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI8_TYOUGOURYOU2));
        bean.setYouzai9_zairyouhinmei(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI9_ZAIRYOUHINMEI));
        bean.setYouzai9_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI9_TYOUGOURYOUKIKAKU));
        bean.setYouzai9_buzaizaikolotno1(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI9_BUZAIZAIKOLOTNO1));
        bean.setYouzai9_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI9_TYOUGOURYOU1));
        bean.setYouzai9_buzaizaikolotno2(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI9_BUZAIZAIKOLOTNO2));
        bean.setYouzai9_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI9_TYOUGOURYOU2));
        bean.setYouzaihyouryousyuuryou_day(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAIHYOURYOUSYUURYOU_DAY));
        bean.setYouzaihyouryousyuuryou_time(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAIHYOURYOUSYUURYOU_TIME));
        bean.setKakuhanki(getItemRow(processData.getItemList(), GXHDO102B020Const.KAKUHANKI));
        bean.setKaitensuu(getItemRow(processData.getItemList(), GXHDO102B020Const.KAITENSUU));
        bean.setKakuhanjikan(getItemRow(processData.getItemList(), GXHDO102B020Const.KAKUHANJIKAN));
        bean.setKakuhankaisi_day(getItemRow(processData.getItemList(), GXHDO102B020Const.KAKUHANKAISI_DAY));
        bean.setKakuhankaisi_time(getItemRow(processData.getItemList(), GXHDO102B020Const.KAKUHANKAISI_TIME));
        bean.setKakuhansyuuryou_day(getItemRow(processData.getItemList(), GXHDO102B020Const.KAKUHANSYUURYOU_DAY));
        bean.setKakuhansyuuryou_time(getItemRow(processData.getItemList(), GXHDO102B020Const.KAKUHANSYUURYOU_TIME));
        bean.setTenkazaislurryhrkaisi_day(getItemRow(processData.getItemList(), GXHDO102B020Const.TENKAZAISLURRYHRKAISI_DAY));
        bean.setTenkazaislurryhrkaisi_time(getItemRow(processData.getItemList(), GXHDO102B020Const.TENKAZAISLURRYHRKAISI_TIME));
        bean.setTenkazaislurry_zairyouhinmei(getItemRow(processData.getItemList(), GXHDO102B020Const.TENKAZAISLURRY_ZAIRYOUHINMEI));
        bean.setTenkazaislurry_WIPlotno(getItemRow(processData.getItemList(), GXHDO102B020Const.TENKAZAISLURRY_WIPLOTNO));
        bean.setTenkazaislurry_tgrkikaku(getItemRow(processData.getItemList(), GXHDO102B020Const.TENKAZAISLURRY_TGRKIKAKU));
        bean.setTenkazaislurry_ftaijyuuryou1(getItemRow(processData.getItemList(), GXHDO102B020Const.TENKAZAISLURRY_FTAIJYUURYOU1));
        bean.setTenkazaislurry_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B020Const.TENKAZAISLURRY_TYOUGOURYOU1));
        bean.setTenkazaislurry_ftaijyuuryou2(getItemRow(processData.getItemList(), GXHDO102B020Const.TENKAZAISLURRY_FTAIJYUURYOU2));
        bean.setTenkazaislurry_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B020Const.TENKAZAISLURRY_TYOUGOURYOU2));
        bean.setTenkazaislurryhrsyuryo_day(getItemRow(processData.getItemList(), GXHDO102B020Const.TENKAZAISLURRYHRSYURYO_DAY));
        bean.setTenkazaislurryhrsyuryo_time(getItemRow(processData.getItemList(), GXHDO102B020Const.TENKAZAISLURRYHRSYURYO_TIME));
        bean.setKokeibunsokuteitantousya(getItemRow(processData.getItemList(), GXHDO102B020Const.KOKEIBUNSOKUTEITANTOUSYA));
        bean.setBikou1(getItemRow(processData.getItemList(), GXHDO102B020Const.BIKOU1));
        bean.setBikou2(getItemRow(processData.getItemList(), GXHDO102B020Const.BIKOU2));

    }

    /**
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_ｻﾌﾞ画面データの規格値取得処理
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
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_ｻﾌﾞ画面データの設定値取得処理
     *
     * @param item 項目情報
     * @return 項目値
     */
    private String getFXHDD01Value(FXHDD01 item) {
        if (item == null) {
            return "";
        }
        return StringUtil.nullToBlank(item.getValue());
    }

    /**
     * 分散材1_材料品名のﾘﾝｸ押下時、 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC009SubGamen1(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B020Const.ZUNSANZAI1_BUZAIZAIKOLOTNO1, GXHDO102B020Const.ZUNSANZAI1_TYOUGOURYOU1,
                GXHDO102B020Const.ZUNSANZAI1_BUZAIZAIKOLOTNO2, GXHDO102B020Const.ZUNSANZAI1_TYOUGOURYOU2);
        return openC009SubGamen(processData, 1, returnItemIdList, GXHDO102B020Const.ZUNSANZAI1_TYOUGOURYOUKIKAKU);
    }

    /**
     * 分散材2_材料品名のﾘﾝｸ押下時、 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC009SubGamen2(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B020Const.ZUNSANZAI2_BUZAIZAIKOLOTNO1, GXHDO102B020Const.ZUNSANZAI2_TYOUGOURYOU1,
                GXHDO102B020Const.ZUNSANZAI2_BUZAIZAIKOLOTNO2, GXHDO102B020Const.ZUNSANZAI2_TYOUGOURYOU2);
        return openC009SubGamen(processData, 2, returnItemIdList, GXHDO102B020Const.ZUNSANZAI2_TYOUGOURYOUKIKAKU);
    }

    /**
     * 溶剤1_材料品名のﾘﾝｸ押下時、 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC009SubGamen3(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B020Const.YOUZAI1_BUZAIZAIKOLOTNO1, GXHDO102B020Const.YOUZAI1_TYOUGOURYOU1,
                GXHDO102B020Const.YOUZAI1_BUZAIZAIKOLOTNO2, GXHDO102B020Const.YOUZAI1_TYOUGOURYOU2);
        return openC009SubGamen(processData, 3, returnItemIdList, GXHDO102B020Const.YOUZAI1_TYOUGOURYOUKIKAKU);
    }

    /**
     * 溶剤2_材料品名のﾘﾝｸ押下時、 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC009SubGamen4(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B020Const.YOUZAI2_BUZAIZAIKOLOTNO1, GXHDO102B020Const.YOUZAI2_TYOUGOURYOU1,
                GXHDO102B020Const.YOUZAI2_BUZAIZAIKOLOTNO2, GXHDO102B020Const.YOUZAI2_TYOUGOURYOU2);
        return openC009SubGamen(processData, 4, returnItemIdList, GXHDO102B020Const.YOUZAI2_TYOUGOURYOUKIKAKU);
    }

    /**
     * 溶剤3_材料品名のﾘﾝｸ押下時、 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC009SubGamen5(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B020Const.YOUZAI3_BUZAIZAIKOLOTNO1, GXHDO102B020Const.YOUZAI3_TYOUGOURYOU1,
                GXHDO102B020Const.YOUZAI3_BUZAIZAIKOLOTNO2, GXHDO102B020Const.YOUZAI3_TYOUGOURYOU2);
        return openC009SubGamen(processData, 5, returnItemIdList, GXHDO102B020Const.YOUZAI3_TYOUGOURYOUKIKAKU);
    }

    /**
     * 溶剤4_材料品名のﾘﾝｸ押下時、 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC009SubGamen6(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B020Const.YOUZAI4_BUZAIZAIKOLOTNO1, GXHDO102B020Const.YOUZAI4_TYOUGOURYOU1,
                GXHDO102B020Const.YOUZAI4_BUZAIZAIKOLOTNO2, GXHDO102B020Const.YOUZAI4_TYOUGOURYOU2);
        return openC009SubGamen(processData, 6, returnItemIdList, GXHDO102B020Const.YOUZAI4_TYOUGOURYOUKIKAKU);
    }

    /**
     * 溶剤5_材料品名のﾘﾝｸ押下時、 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC009SubGamen7(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B020Const.YOUZAI5_BUZAIZAIKOLOTNO1, GXHDO102B020Const.YOUZAI5_TYOUGOURYOU1,
                GXHDO102B020Const.YOUZAI5_BUZAIZAIKOLOTNO2, GXHDO102B020Const.YOUZAI5_TYOUGOURYOU2);
        return openC009SubGamen(processData, 7, returnItemIdList, GXHDO102B020Const.YOUZAI5_TYOUGOURYOUKIKAKU);
    }

    /**
     * 溶剤6_材料品名のﾘﾝｸ押下時、 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC009SubGamen8(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B020Const.YOUZAI6_BUZAIZAIKOLOTNO1, GXHDO102B020Const.YOUZAI6_TYOUGOURYOU1,
                GXHDO102B020Const.YOUZAI6_BUZAIZAIKOLOTNO2, GXHDO102B020Const.YOUZAI6_TYOUGOURYOU2);
        return openC009SubGamen(processData, 8, returnItemIdList, GXHDO102B020Const.YOUZAI6_TYOUGOURYOUKIKAKU);
    }

    /**
     * 溶剤7_材料品名のﾘﾝｸ押下時、 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC009SubGamen9(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B020Const.YOUZAI7_BUZAIZAIKOLOTNO1, GXHDO102B020Const.YOUZAI7_TYOUGOURYOU1,
                GXHDO102B020Const.YOUZAI7_BUZAIZAIKOLOTNO2, GXHDO102B020Const.YOUZAI7_TYOUGOURYOU2);
        return openC009SubGamen(processData, 9, returnItemIdList, GXHDO102B020Const.YOUZAI7_TYOUGOURYOUKIKAKU);
    }

    /**
     * 溶剤8_材料品名のﾘﾝｸ押下時、 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC009SubGamen10(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B020Const.YOUZAI8_BUZAIZAIKOLOTNO1, GXHDO102B020Const.YOUZAI8_TYOUGOURYOU1,
                GXHDO102B020Const.YOUZAI8_BUZAIZAIKOLOTNO2, GXHDO102B020Const.YOUZAI8_TYOUGOURYOU2);
        return openC009SubGamen(processData, 10, returnItemIdList, GXHDO102B020Const.YOUZAI8_TYOUGOURYOUKIKAKU);
    }

    /**
     * 溶剤9_材料品名のﾘﾝｸ押下時、 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC009SubGamen11(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B020Const.YOUZAI9_BUZAIZAIKOLOTNO1, GXHDO102B020Const.YOUZAI9_TYOUGOURYOU1,
                GXHDO102B020Const.YOUZAI9_BUZAIZAIKOLOTNO2, GXHDO102B020Const.YOUZAI9_TYOUGOURYOU2);
        return openC009SubGamen(processData, 11, returnItemIdList, GXHDO102B020Const.YOUZAI9_TYOUGOURYOUKIKAKU);
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @param zairyokubun 材料区分
     * @param returnItemIdList サブ画面から戻ったときに値を設定必要項目リスト
     * @param youzai_tyougouryoukikakuItemId 溶剤_調合量規格項目ID
     * @return 処理制御データ
     */
    public ProcessData openC009SubGamen(ProcessData processData, int zairyokubun, List<String> returnItemIdList, String youzai_tyougouryoukikakuItemId) {
        try {
            // 「秤量号機」
            FXHDD01 itemGoki = getItemRow(processData.getItemList(), GXHDO102B020Const.GOKI);
            // 「秤量号機」ﾁｪｯｸ処理
            ErrorMessageInfo checkItemErrorInfo = checkGoki(itemGoki);
            if (checkItemErrorInfo != null) {
                processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
                // エラーの場合はコールバック変数に"error0"をセット
                RequestContext context = RequestContext.getCurrentInstance();
                context.addCallbackParam("firstParam", "error0");
                return processData;
            }
            processData.setMethod("");
            // コールバックパラメータにてサブ画面起動用の値を設定
            processData.setCollBackParam("gxhdo102c009");

            GXHDO102C009 beanGXHDO102C009 = (GXHDO102C009) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C009);
            GXHDO102C009Model gxhdo102c009model = beanGXHDO102C009.getGxhdO102c009Model();
            // 主画面からサブ画面に渡されたデータを設定
            setSubGamenInitData(processData, gxhdo102c009model, zairyokubun, itemGoki, returnItemIdList, youzai_tyougouryoukikakuItemId);

            beanGXHDO102C009.setGxhdO102c009ModelView(gxhdo102c009model.clone());
        } catch (CloneNotSupportedException ex) {
            ErrUtil.outputErrorLog("CloneNotSupportedException発生", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            return processData;
        }

        return processData;
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
     * @param processData 処理制御データ
     * @param gxhdo102c009model モデルデータ
     * @param zairyokubun 材料区分
     * @param itemGoki 秤量号機データ
     * @param returnItemIdList サブ画面から戻るデータリスト
     * @param youzai_tyougouryoukikakuItemId 溶剤_調合量規格項目ID
     * @throws CloneNotSupportedException 例外エラー
     */
    private void setSubGamenInitData(ProcessData processData, GXHDO102C009Model gxhdo102c009model, int zairyokubun, FXHDD01 itemGoki, List<String> returnItemIdList,
            String youzai_tyougouryoukikakuItemId) throws CloneNotSupportedException {
        GXHDO102C009Model.SubGamenData c009subgamendata = GXHDO102C009Logic.getC009subgamendata(gxhdo102c009model, zairyokubun);
        if (c009subgamendata == null) {
            return;
        }
        c009subgamendata.setSubDataGoki(StringUtil.nullToBlank(itemGoki.getValue()));
        c009subgamendata.setSubDataZairyokubun(zairyokubun);

        if (zairyokubun >= 6) {
            String youzai_tyougouryoukikakuVal = getFXHDD01Value(getItemRow(processData.getItemList(), youzai_tyougouryoukikakuItemId)); // 調合規格
            c009subgamendata.getSubDataTyogouryoukikaku().setValue(youzai_tyougouryoukikakuVal.replace("【", "").replace("】", ""));
        }
        // サブ画面から戻ったときに値を設定する項目を指定する。
        c009subgamendata.setReturnItemIdBuzailotno1(returnItemIdList.get(0)); // 部材在庫No.X_1
        c009subgamendata.setReturnItemIdTyougouryou1(returnItemIdList.get(1)); // 調合量X_1
        c009subgamendata.setReturnItemIdBuzailotno2(returnItemIdList.get(2)); // 部材在庫NoX_2
        c009subgamendata.setReturnItemIdTyougouryou2(returnItemIdList.get(3)); // 調合量X_2
        gxhdo102c009model.setShowsubgamendata(c009subgamendata.clone());
        // サブ画面の調合残量の計算
        GXHDO102C009Logic.calcTyogouzanryou(gxhdo102c009model);
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_ｻﾌﾞ画面データ設定処理
     *
     * @param processData 処理制御データ
     * @param subSrYuudentaiYouzaiList 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_ｻﾌﾞ画面データリスト
     */
    private void setInputItemDataSubFormC009(ProcessData processData, List<SubSrYuudentaiYouzai> subSrYuudentaiYouzaiList) {
        // サブ画面の情報を取得
        GXHDO102C009 beanGXHDO102C009 = (GXHDO102C009) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C009);

        GXHDO102C009Model model;
        if (subSrYuudentaiYouzaiList == null) {
            // 登録データが無い場合、主画面の材料品名1-2と調合量規格1-2はｻﾌﾞ画面の初期値にセットする。
            subSrYuudentaiYouzaiList = new ArrayList<>();
            SubSrYuudentaiYouzai subgamen1 = new SubSrYuudentaiYouzai();
            SubSrYuudentaiYouzai subgamen2 = new SubSrYuudentaiYouzai();
            SubSrYuudentaiYouzai subgamen3 = new SubSrYuudentaiYouzai();
            SubSrYuudentaiYouzai subgamen4 = new SubSrYuudentaiYouzai();
            SubSrYuudentaiYouzai subgamen5 = new SubSrYuudentaiYouzai();
            SubSrYuudentaiYouzai subgamen6 = new SubSrYuudentaiYouzai();
            SubSrYuudentaiYouzai subgamen7 = new SubSrYuudentaiYouzai();
            SubSrYuudentaiYouzai subgamen8 = new SubSrYuudentaiYouzai();
            SubSrYuudentaiYouzai subgamen9 = new SubSrYuudentaiYouzai();
            SubSrYuudentaiYouzai subgamen10 = new SubSrYuudentaiYouzai();
            SubSrYuudentaiYouzai subgamen11 = new SubSrYuudentaiYouzai();

            subgamen1.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B020Const.ZUNSANZAI1_ZAIRYOUHINMEI))); // 分散材1_材料品名
            subgamen1.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B020Const.ZUNSANZAI1_TYOUGOURYOUKIKAKU))); // 分散材1_調合量規格
            subgamen2.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B020Const.ZUNSANZAI2_ZAIRYOUHINMEI))); // 分散材2_材料品名
            subgamen2.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B020Const.ZUNSANZAI2_TYOUGOURYOUKIKAKU))); // 分散材2_調合量規格
            subgamen3.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI1_ZAIRYOUHINMEI))); // 溶剤1_材料品名
            subgamen3.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI1_TYOUGOURYOUKIKAKU))); // 溶剤1_調合量規格
            subgamen4.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI2_ZAIRYOUHINMEI))); // 溶剤2_材料品名
            subgamen4.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI2_TYOUGOURYOUKIKAKU))); // 溶剤2_調合量規格
            subgamen5.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI3_ZAIRYOUHINMEI))); // 溶剤3_材料品名
            subgamen5.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI3_TYOUGOURYOUKIKAKU))); // 溶剤3_調合量規格
            subgamen6.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI4_ZAIRYOUHINMEI))); // 溶剤4_材料品名
            subgamen6.setTyogouryoukikaku(getFXHDD01Value(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI4_TYOUGOURYOUKIKAKU)).replace("【", "").replace("】", "")); // 溶剤4_調合量規格
            subgamen7.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI5_ZAIRYOUHINMEI))); // 溶剤5_材料品名
            subgamen7.setTyogouryoukikaku(getFXHDD01Value(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI5_TYOUGOURYOUKIKAKU)).replace("【", "").replace("】", "")); // 溶剤5_調合量規格
            subgamen8.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI6_ZAIRYOUHINMEI))); // 溶剤6_材料品名
            subgamen8.setTyogouryoukikaku(getFXHDD01Value(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI6_TYOUGOURYOUKIKAKU)).replace("【", "").replace("】", "")); // 溶剤6_調合量規格
            subgamen9.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI7_ZAIRYOUHINMEI))); // 溶剤7_材料品名
            subgamen9.setTyogouryoukikaku(getFXHDD01Value(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI7_TYOUGOURYOUKIKAKU)).replace("【", "").replace("】", "")); // 溶剤7_調合量規格
            subgamen10.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI8_ZAIRYOUHINMEI))); // 溶剤8_材料品名
            subgamen10.setTyogouryoukikaku(getFXHDD01Value(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI8_TYOUGOURYOUKIKAKU)).replace("【", "").replace("】", "")); // 溶剤8_調合量規格
            subgamen11.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI9_ZAIRYOUHINMEI))); // 溶剤9_材料品名
            subgamen11.setTyogouryoukikaku(getFXHDD01Value(getItemRow(processData.getItemList(), GXHDO102B020Const.YOUZAI9_TYOUGOURYOUKIKAKU)).replace("【", "").replace("】", "")); // 溶剤9_調合量規格
            subSrYuudentaiYouzaiList.add(subgamen1);
            subSrYuudentaiYouzaiList.add(subgamen2);
            subSrYuudentaiYouzaiList.add(subgamen3);
            subSrYuudentaiYouzaiList.add(subgamen4);
            subSrYuudentaiYouzaiList.add(subgamen5);
            subSrYuudentaiYouzaiList.add(subgamen6);
            subSrYuudentaiYouzaiList.add(subgamen7);
            subSrYuudentaiYouzaiList.add(subgamen8);
            subSrYuudentaiYouzaiList.add(subgamen9);
            subSrYuudentaiYouzaiList.add(subgamen10);
            subSrYuudentaiYouzaiList.add(subgamen11);
            model = GXHDO102C009Logic.createGXHDO102C009Model(subSrYuudentaiYouzaiList);

        } else {
            // 登録データがあれば登録データをセットする。
            model = GXHDO102C009Logic.createGXHDO102C009Model(subSrYuudentaiYouzaiList);
        }
        beanGXHDO102C009.setGxhdO102c009Model(model);
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_ｻﾌﾞ画面の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_ｻﾌﾞ画面登録データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrYuudentaiYouzai> getSubSrYuudentaiYouzaiData(QueryRunner queryRunnerQcdb,
            String rev, String jotaiFlg, String kojyo, String lotNo, String edaban) throws SQLException {
        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSubSrYuudentaiYouzai(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSubSrYuudentaiYouzai(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        }
    }

    /**
     * [誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_ｻﾌﾞ画面]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrYuudentaiYouzai> loadSubSrYuudentaiYouzai(QueryRunner queryRunnerQcdb,
            String kojyo, String lotNo, String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + "kojyo,lotno,edaban,zairyokubun,tyogouryoukikaku,tyogouzanryou,zairyohinmei,"
                + "buzailotno1,buzaihinmei1,fuutaijyuuryou1,tyougouryou1_1,tyougouryou1_2,tyougouryou1_3,tyougouryou1_4,"
                + "tyougouryou1_5,tyougouryou1_6,buzailotno2,buzaihinmei2,fuutaijyuuryou2,tyougouryou2_1,tyougouryou2_2,"
                + "tyougouryou2_3,tyougouryou2_4,tyougouryou2_5,tyougouryou2_6,torokunichiji,kosinnichiji,"
                + "revision, '0' AS deleteflag "
                + " FROM sub_sr_yuudentai_youzai "
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
        mapping.put("fuutaijyuuryou1", "fuutaijyuuryou1");       // 風袋重量1
        mapping.put("tyougouryou1_1", "tyougouryou1_1");         // 調合量1_1
        mapping.put("tyougouryou1_2", "tyougouryou1_2");         // 調合量1_2
        mapping.put("tyougouryou1_3", "tyougouryou1_3");         // 調合量1_3
        mapping.put("tyougouryou1_4", "tyougouryou1_4");         // 調合量1_4
        mapping.put("tyougouryou1_5", "tyougouryou1_5");         // 調合量1_5
        mapping.put("tyougouryou1_6", "tyougouryou1_6");         // 調合量1_6
        mapping.put("buzailotno2", "buzailotno2");               // 部材在庫No1
        mapping.put("buzaihinmei2", "buzaihinmei2");             // 部材在庫品名1
        mapping.put("fuutaijyuuryou2", "fuutaijyuuryou2");       // 風袋重量2
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
        ResultSetHandler<List<SubSrYuudentaiYouzai>> beanHandler = new BeanListHandler<>(SubSrYuudentaiYouzai.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_ｻﾌﾞ画面_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrYuudentaiYouzai> loadTmpSubSrYuudentaiYouzai(QueryRunner queryRunnerQcdb,
            String kojyo, String lotNo, String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + "kojyo,lotno,edaban,zairyokubun,tyogouryoukikaku,tyogouzanryou,zairyohinmei,"
                + "buzailotno1,buzaihinmei1,fuutaijyuuryou1,tyougouryou1_1,tyougouryou1_2,tyougouryou1_3,tyougouryou1_4,"
                + "tyougouryou1_5,tyougouryou1_6,buzailotno2,buzaihinmei2,fuutaijyuuryou2,tyougouryou2_1,tyougouryou2_2,"
                + "tyougouryou2_3,tyougouryou2_4,tyougouryou2_5,tyougouryou2_6,torokunichiji,kosinnichiji,"
                + "revision, deleteflag "
                + " FROM tmp_sub_sr_yuudentai_youzai "
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
        mapping.put("fuutaijyuuryou1", "fuutaijyuuryou1");       // 風袋重量1
        mapping.put("tyougouryou1_1", "tyougouryou1_1");         // 調合量1_1
        mapping.put("tyougouryou1_2", "tyougouryou1_2");         // 調合量1_2
        mapping.put("tyougouryou1_3", "tyougouryou1_3");         // 調合量1_3
        mapping.put("tyougouryou1_4", "tyougouryou1_4");         // 調合量1_4
        mapping.put("tyougouryou1_5", "tyougouryou1_5");         // 調合量1_5
        mapping.put("tyougouryou1_6", "tyougouryou1_6");         // 調合量1_6
        mapping.put("buzailotno2", "buzailotno2");               // 部材在庫No1
        mapping.put("buzaihinmei2", "buzaihinmei2");             // 部材在庫品名1
        mapping.put("fuutaijyuuryou2", "fuutaijyuuryou2");       // 風袋重量2
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
        ResultSetHandler<List<SubSrYuudentaiYouzai>> beanHandler = new BeanListHandler<>(SubSrYuudentaiYouzai.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_サブ画面_仮登録(tmp_sub_sr_yuudentai_youzai)登録処理
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
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void insertTmpSubSrYuudentaiYouzai(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal newRev, int deleteflag, String kojyo, String lotNo, String edaban, Integer zairyokubun,
            String systemTime, ProcessData processData) throws SQLException {

        String sql = "INSERT INTO tmp_sub_sr_yuudentai_youzai ( "
                + "kojyo,lotno,edaban,zairyokubun,tyogouryoukikaku,tyogouzanryou,zairyohinmei,"
                + "buzailotno1,buzaihinmei1,fuutaijyuuryou1,tyougouryou1_1,tyougouryou1_2,tyougouryou1_3,tyougouryou1_4,"
                + "tyougouryou1_5,tyougouryou1_6,buzailotno2,buzaihinmei2,fuutaijyuuryou2,tyougouryou2_1,tyougouryou2_2,"
                + "tyougouryou2_3,tyougouryou2_4,tyougouryou2_5,tyougouryou2_6,torokunichiji,kosinnichiji,"
                + "revision, deleteflag "
                + " ) VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";

        List<Object> params = setUpdateParameterTmpSubSrYuudentaiYouzai(true, newRev, deleteflag, kojyo, lotNo, edaban, zairyokubun, systemTime, null, processData);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_仮登録(tmp_sub_sr_yuudentai_youzai)更新処理
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
     * @param srYuudentaiYouzai 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量_仮登録更新前データ
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void updateTmpSubSrYuudentaiYouzai(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal rev, BigDecimal newRev, String kojyo, String lotNo,
            String edaban, Integer zairyokubun, String systemTime, SrYuudentaiYouzai srYuudentaiYouzai, ProcessData processData) throws SQLException {

        String sql = "UPDATE tmp_sub_sr_yuudentai_youzai SET "
                + "tyogouryoukikaku = ?,tyogouzanryou = ?,zairyohinmei = ?,"
                + "buzailotno1 = ?,buzaihinmei1 = ?,fuutaijyuuryou1 = ?,tyougouryou1_1 = ?,tyougouryou1_2 = ?,tyougouryou1_3 = ?,tyougouryou1_4 = ?,"
                + "tyougouryou1_5 = ?,tyougouryou1_6 = ?,buzailotno2 = ?,buzaihinmei2 = ?,fuutaijyuuryou2 = ?,tyougouryou2_1 = ?,tyougouryou2_2 = ?,"
                + "tyougouryou2_3 = ?,tyougouryou2_4 = ?,tyougouryou2_5 = ?,tyougouryou2_6 = ?,kosinnichiji = ?,revision = ?, deleteflag = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND zairyokubun = ? AND revision = ? ";

        List<Object> params = setUpdateParameterTmpSubSrYuudentaiYouzai(false, newRev, 0, kojyo, lotNo, edaban, zairyokubun, systemTime, srYuudentaiYouzai, processData);

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
     * 溶剤4_調合量規格-溶剤9_調合量規格の項目IDを取得
     *
     * @param zairyokubun 材料区分
     * @return 項目ID
     */
    private String getYouzai4To9TyougouryoukikakuItemId(Integer zairyokubun) {
        String tyogouryoukikakuItemId = "";
        switch (zairyokubun) {
            // 溶剤4_調合量規格
            case 6:
                tyogouryoukikakuItemId = GXHDO102B020Const.YOUZAI4_TYOUGOURYOUKIKAKU;
                break;
            //  溶剤5_調合量規格
            case 7:
                tyogouryoukikakuItemId = GXHDO102B020Const.YOUZAI5_TYOUGOURYOUKIKAKU;
                break;
            //  溶剤6_調合量規格
            case 8:
                tyogouryoukikakuItemId = GXHDO102B020Const.YOUZAI6_TYOUGOURYOUKIKAKU;
                break;
            //  溶剤7_調合量規格
            case 9:
                tyogouryoukikakuItemId = GXHDO102B020Const.YOUZAI7_TYOUGOURYOUKIKAKU;
                break;
            //  溶剤8_調合量規格
            case 10:
                tyogouryoukikakuItemId = GXHDO102B020Const.YOUZAI8_TYOUGOURYOUKIKAKU;
                break;
            //  溶剤9_調合量規格
            case 11:
                tyogouryoukikakuItemId = GXHDO102B020Const.YOUZAI9_TYOUGOURYOUKIKAKU;
                break;
        }
        return tyogouryoukikakuItemId;
    }
    
    /**
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_サブ画面仮登録(tmp_sub_sr_yuudentai_youzai)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param zairyokubun 材料区分
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param srYuudentaiYouzai 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量_仮登録更新前データ
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSubSrYuudentaiYouzai(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo, String lotNo, 
            String edaban, Integer zairyokubun, String systemTime, SrYuudentaiYouzai srYuudentaiYouzai, ProcessData processData) {
        List<FXHDD01> pItemList = processData.getItemList();
        List<Object> params = new ArrayList<>();

        // 子画面情報を取得
        GXHDO102C009 beanGXHDO102C009 = (GXHDO102C009) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C009);
        GXHDO102C009Model gxhdO102c009Model = beanGXHDO102C009.getGxhdO102c009Model();

        // 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_サブ画面から更新値を取得
        ArrayList<Object> subGamenDataList = getSubGamenData(gxhdO102c009Model, zairyokubun);
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
        
        String tyougouryoukikakuValue = tyogouryoukikaku.getValue(); // 調合量規格
        if (zairyokubun >= 6) {
            // 溶剤4_調合量規格-溶剤9_調合量規格からサブ画面の調合規格がメインの調合量規格と同じように更新する
            tyougouryoukikakuValue = getTyougouryoukikakuValue(pItemList, getYouzai4To9TyougouryoukikakuItemId(zairyokubun), srYuudentaiYouzai);
        }
        
        params.add(DBUtil.stringToStringObjectDefaultNull(tyougouryoukikakuValue)); // 調合量規格
        params.add(DBUtil.stringToIntObjectDefaultNull(tyogouzanryou.getValue())); // 調合残量
        params.add(DBUtil.stringToStringObjectDefaultNull(buzaitab1DataList.get(0).getValue())); // 材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(buzaitab1DataList.get(1).getValue())); // 部材在庫No1
        params.add(DBUtil.stringToStringObjectDefaultNull(buzaitab1DataList.get(2).getValue())); // 部材在庫品名1
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(3).getValue())); // 風袋重量1
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(4).getValue())); // 調合量1_1
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(5).getValue())); // 調合量1_2
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(6).getValue())); // 調合量1_3
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(7).getValue())); // 調合量1_4
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(8).getValue())); // 調合量1_5
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(9).getValue())); // 調合量1_6

        params.add(DBUtil.stringToStringObjectDefaultNull(buzaitab2DataList.get(1).getValue())); // 部材在庫No2
        params.add(DBUtil.stringToStringObjectDefaultNull(buzaitab2DataList.get(2).getValue())); // 部材在庫品名2
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab2DataList.get(3).getValue())); // 風袋重量2
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab2DataList.get(4).getValue())); // 調合量2_1
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab2DataList.get(5).getValue())); // 調合量2_2
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab2DataList.get(6).getValue())); // 調合量2_3
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab2DataList.get(7).getValue())); // 調合量2_4
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab2DataList.get(8).getValue())); // 調合量2_5
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab2DataList.get(9).getValue())); // 調合量2_6

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
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_サブ画面仮登録(tmp_sub_sr_yuudentai_youzai)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSubSrYuudentaiYouzai(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal rev, String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sub_sr_yuudentai_youzai "
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
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_サブ画面(sub_sr_yuudentai_youzai)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param zairyokubun 材料区分
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param tmpSrYuudentaiYouzai 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量_仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSubSrYuudentaiYouzai(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal newRev, String kojyo, String lotNo, String edaban,
            Integer zairyokubun, String systemTime, ProcessData processData, SrYuudentaiYouzai tmpSrYuudentaiYouzai) throws SQLException {
        String sql = "INSERT INTO sub_sr_yuudentai_youzai ( "
                + "kojyo,lotno,edaban,zairyokubun,tyogouryoukikaku,tyogouzanryou,zairyohinmei,"
                + "buzailotno1,buzaihinmei1,fuutaijyuuryou1,tyougouryou1_1,tyougouryou1_2,tyougouryou1_3,tyougouryou1_4,"
                + "tyougouryou1_5,tyougouryou1_6,buzailotno2,buzaihinmei2,fuutaijyuuryou2,tyougouryou2_1,tyougouryou2_2,"
                + "tyougouryou2_3,tyougouryou2_4,tyougouryou2_5,tyougouryou2_6,torokunichiji,kosinnichiji,"
                + "revision "
                + " ) VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";

        List<Object> params = setUpdateParameterSubSrYuudentaiYouzai(true, newRev, kojyo, lotNo, edaban, zairyokubun, systemTime, tmpSrYuudentaiYouzai, processData);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_ｻﾌﾞ画面(sub_sr_yuudentai_youzai)更新処理
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
     * @param srYuudentaiYouzai 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量更新前データ
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void updateSubSrYuudentaiYouzai(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal rev, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            Integer zairyokubun, String systemTime, SrYuudentaiYouzai srYuudentaiYouzai, ProcessData processData) throws SQLException {

        String sql = "UPDATE sub_sr_yuudentai_youzai SET "
                + "tyogouryoukikaku = ?,tyogouzanryou = ?,zairyohinmei = ?,"
                + "buzailotno1 = ?,buzaihinmei1 = ?,fuutaijyuuryou1 = ?,tyougouryou1_1 = ?,tyougouryou1_2 = ?,tyougouryou1_3 = ?,tyougouryou1_4 = ?,"
                + "tyougouryou1_5 = ?,tyougouryou1_6 = ?,buzailotno2 = ?,buzaihinmei2 = ?,fuutaijyuuryou2 = ?,tyougouryou2_1 = ?,tyougouryou2_2 = ?,"
                + "tyougouryou2_3 = ?,tyougouryou2_4 = ?,tyougouryou2_5 = ?,tyougouryou2_6 = ?,kosinnichiji = ?,revision = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND zairyokubun = ? AND revision = ? ";

        List<Object> params = setUpdateParameterSubSrYuudentaiYouzai(false, newRev, kojyo, lotNo, edaban, zairyokubun, systemTime, srYuudentaiYouzai, processData);

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
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_サブ画面から更新値を取得
     *
     * @param gxhdO102c009Model モデルデータ
     * @param zairyokubun 材料区分
     * @return 更新値情報
     */
    private ArrayList<Object> getSubGamenData(GXHDO102C009Model gxhdO102c009Model, Integer zairyokubun) {
        GXHDO102C009Model.SubGamenData c009subgamendata = GXHDO102C009Logic.getC009subgamendata(gxhdO102c009Model, zairyokubun);
        ArrayList<Object> returnList = new ArrayList<>();
        // 調合量規格
        FXHDD01 tyogouryoukikaku = c009subgamendata.getSubDataTyogouryoukikaku();
        // 調合残量
        FXHDD01 tyogouzanryou = c009subgamendata.getSubDataTyogouzanryou();
        // 部材①
        List<FXHDD01> buzaitab1DataList = c009subgamendata.getSubDataBuzaitab1();
        // 部材②
        List<FXHDD01> buzaitab2DataList = c009subgamendata.getSubDataBuzaitab2();
        returnList.add(tyogouryoukikaku);
        returnList.add(tyogouzanryou);
        returnList.add(buzaitab1DataList);
        returnList.add(buzaitab2DataList);
        return returnList;
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_サブ画面登録(tmp_sub_sr_yuudentai_youzai)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param zairyokubun 材料区分
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param srYuudentaiYouzai 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量_仮登録更新前データ
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSubSrYuudentaiYouzai(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, 
            String edaban, Integer zairyokubun, String systemTime, SrYuudentaiYouzai srYuudentaiYouzai, ProcessData processData) {
        List<FXHDD01> pItemList = processData.getItemList();
        List<Object> params = new ArrayList<>();

        // 子画面情報を取得
        GXHDO102C009 beanGXHDO102C009 = (GXHDO102C009) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C009);
        GXHDO102C009Model gxhdO102c009Model = beanGXHDO102C009.getGxhdO102c009Model();
        // 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_サブ画面から更新値を取得
        ArrayList<Object> subGamenDataList = getSubGamenData(gxhdO102c009Model, zairyokubun);
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

        String tyougouryoukikakuValue = tyogouryoukikaku.getValue(); // 調合量規格
        if (zairyokubun >= 6) {
            // 溶剤4_調合量規格-溶剤9_調合量規格からサブ画面の調合規格がメインの調合量規格と同じように更新する
            tyougouryoukikakuValue = getTyougouryoukikakuValue(pItemList, getYouzai4To9TyougouryoukikakuItemId(zairyokubun), srYuudentaiYouzai);
        }

        params.add(DBUtil.stringToStringObject(tyougouryoukikakuValue)); // 調合量規格
        params.add(DBUtil.stringToIntObject(tyogouzanryou.getValue())); // 調合残量
        params.add(DBUtil.stringToStringObject(buzaitab1DataList.get(0).getValue())); // 材料品名
        params.add(DBUtil.stringToStringObject(buzaitab1DataList.get(1).getValue())); // 部材在庫No1
        params.add(DBUtil.stringToStringObject(buzaitab1DataList.get(2).getValue())); // 部材在庫品名1
        params.add(DBUtil.stringToIntObject(buzaitab1DataList.get(3).getValue())); // 風袋重量1
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(4).getValue())); // 調合量1_1
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(5).getValue())); // 調合量1_2
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(6).getValue())); // 調合量1_3
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(7).getValue())); // 調合量1_4
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(8).getValue())); // 調合量1_5
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(9).getValue())); // 調合量1_6

        params.add(DBUtil.stringToStringObject(buzaitab2DataList.get(1).getValue())); // 部材在庫No2
        params.add(DBUtil.stringToStringObject(buzaitab2DataList.get(2).getValue())); // 部材在庫品名2
        params.add(DBUtil.stringToIntObject(buzaitab2DataList.get(3).getValue())); // 風袋重量2
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab2DataList.get(4).getValue())); // 調合量2_1
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab2DataList.get(5).getValue())); // 調合量2_2
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab2DataList.get(6).getValue())); // 調合量2_3
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab2DataList.get(7).getValue())); // 調合量2_4
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab2DataList.get(8).getValue())); // 調合量2_5
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab2DataList.get(9).getValue())); // 調合量2_6

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
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_サブ画面仮登録(tmp_sub_sr_yuudentai_youzai)登録処理(削除時)
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
    private void insertDeleteDataTmpSubSrYuudentaiYouzai(QueryRunner queryRunnerQcdb,
            Connection conQcdb, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, String systemTime) throws SQLException {
        String sql = "INSERT INTO tmp_sub_sr_yuudentai_youzai( "
                + "kojyo,lotno,edaban,zairyokubun,tyogouryoukikaku,tyogouzanryou,zairyohinmei,"
                + "buzailotno1,buzaihinmei1,fuutaijyuuryou1,tyougouryou1_1,tyougouryou1_2,tyougouryou1_3,tyougouryou1_4,"
                + "tyougouryou1_5,tyougouryou1_6,buzailotno2,buzaihinmei2,fuutaijyuuryou2,tyougouryou2_1,tyougouryou2_2,"
                + "tyougouryou2_3,tyougouryou2_4,tyougouryou2_5,tyougouryou2_6,torokunichiji,kosinnichiji,"
                + "revision, deleteflag "
                + ") SELECT "
                + "kojyo,lotno,edaban,zairyokubun,tyogouryoukikaku,tyogouzanryou,zairyohinmei,"
                + "buzailotno1,buzaihinmei1,fuutaijyuuryou1,tyougouryou1_1,tyougouryou1_2,tyougouryou1_3,tyougouryou1_4,"
                + "tyougouryou1_5,tyougouryou1_6,buzailotno2,buzaihinmei2,fuutaijyuuryou2,tyougouryou2_1,tyougouryou2_2,"
                + "tyougouryou2_3,tyougouryou2_4,tyougouryou2_5,tyougouryou2_6,?,?,?,? "
                + " FROM sub_sr_yuudentai_youzai "
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
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_サブ画面仮登録(sub_sr_yuudentai_youzai)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSubSrYuudentaiYouzai(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal rev, String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sub_sr_yuudentai_youzai "
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
        // 分散材1_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B020Const.ZUNSANZAI1_BUZAIZAIKOLOTNO1, GXHDO102B020Const.ZUNSANZAI1_TYOUGOURYOU1, errorItemList);
        // 分散材1_部材在庫No2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B020Const.ZUNSANZAI1_BUZAIZAIKOLOTNO2, GXHDO102B020Const.ZUNSANZAI1_TYOUGOURYOU2, errorItemList);
        // 分散材2_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B020Const.ZUNSANZAI2_BUZAIZAIKOLOTNO1, GXHDO102B020Const.ZUNSANZAI2_TYOUGOURYOU1, errorItemList);
        // 分散材2_部材在庫No2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B020Const.ZUNSANZAI2_BUZAIZAIKOLOTNO2, GXHDO102B020Const.ZUNSANZAI2_TYOUGOURYOU2, errorItemList);
        // 溶剤1_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B020Const.YOUZAI1_BUZAIZAIKOLOTNO1, GXHDO102B020Const.YOUZAI1_TYOUGOURYOU1, errorItemList);
        // 溶剤1_部材在庫No2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B020Const.YOUZAI1_BUZAIZAIKOLOTNO2, GXHDO102B020Const.YOUZAI1_TYOUGOURYOU2, errorItemList);
        // 溶剤2_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B020Const.YOUZAI2_BUZAIZAIKOLOTNO1, GXHDO102B020Const.YOUZAI2_TYOUGOURYOU1, errorItemList);
        // 溶剤2_部材在庫No2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B020Const.YOUZAI2_BUZAIZAIKOLOTNO2, GXHDO102B020Const.YOUZAI2_TYOUGOURYOU2, errorItemList);
        // 溶剤3_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B020Const.YOUZAI3_BUZAIZAIKOLOTNO1, GXHDO102B020Const.YOUZAI3_TYOUGOURYOU1, errorItemList);
        // 溶剤3_部材在庫No2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B020Const.YOUZAI3_BUZAIZAIKOLOTNO2, GXHDO102B020Const.YOUZAI3_TYOUGOURYOU2, errorItemList);
        // 溶剤4_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B020Const.YOUZAI4_BUZAIZAIKOLOTNO1, GXHDO102B020Const.YOUZAI4_TYOUGOURYOU1, errorItemList);
        // 溶剤4_部材在庫No2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B020Const.YOUZAI4_BUZAIZAIKOLOTNO2, GXHDO102B020Const.YOUZAI4_TYOUGOURYOU2, errorItemList);
        // 溶剤5_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B020Const.YOUZAI5_BUZAIZAIKOLOTNO1, GXHDO102B020Const.YOUZAI5_TYOUGOURYOU1, errorItemList);
        // 溶剤5_部材在庫No2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B020Const.YOUZAI5_BUZAIZAIKOLOTNO2, GXHDO102B020Const.YOUZAI5_TYOUGOURYOU2, errorItemList);
        // 溶剤6_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B020Const.YOUZAI6_BUZAIZAIKOLOTNO1, GXHDO102B020Const.YOUZAI6_TYOUGOURYOU1, errorItemList);
        // 溶剤6_部材在庫No2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B020Const.YOUZAI6_BUZAIZAIKOLOTNO2, GXHDO102B020Const.YOUZAI6_TYOUGOURYOU2, errorItemList);
        // 溶剤7_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B020Const.YOUZAI7_BUZAIZAIKOLOTNO1, GXHDO102B020Const.YOUZAI7_TYOUGOURYOU1, errorItemList);
        // 溶剤7_部材在庫No2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B020Const.YOUZAI7_BUZAIZAIKOLOTNO2, GXHDO102B020Const.YOUZAI7_TYOUGOURYOU2, errorItemList);
        // 溶剤8_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B020Const.YOUZAI8_BUZAIZAIKOLOTNO1, GXHDO102B020Const.YOUZAI8_TYOUGOURYOU1, errorItemList);
        // 溶剤8_部材在庫No2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B020Const.YOUZAI8_BUZAIZAIKOLOTNO2, GXHDO102B020Const.YOUZAI8_TYOUGOURYOU2, errorItemList);
        // 溶剤9_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B020Const.YOUZAI9_BUZAIZAIKOLOTNO1, GXHDO102B020Const.YOUZAI9_TYOUGOURYOU1, errorItemList);
        // 溶剤9_部材在庫No2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B020Const.YOUZAI9_BUZAIZAIKOLOTNO2, GXHDO102B020Const.YOUZAI9_TYOUGOURYOU2, errorItemList);

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
        FXHDD01 itemFxhdd01Wiplotno = getItemRow(processData.getItemList(), GXHDO102B020Const.WIPLOTNO);
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
        allItemIdMap.put(GXHDO102B020Const.WIPLOTNO, "WIPﾛｯﾄNo");
        allItemIdMap.put(GXHDO102B020Const.YUUDENTAISLURRYHINMEI, "誘電体ｽﾗﾘｰ品名");
        allItemIdMap.put(GXHDO102B020Const.YUUDENTAISLURRYLOTNO, "誘電体ｽﾗﾘｰLotNo");
        allItemIdMap.put(GXHDO102B020Const.LOTKUBUN, "ﾛｯﾄ区分");
        allItemIdMap.put(GXHDO102B020Const.GENRYOULOTNO, "原料LotNo");
        allItemIdMap.put(GXHDO102B020Const.GENRYOUKIGOU, "原料記号");
        allItemIdMap.put(GXHDO102B020Const.GOKI, "秤量号機");
        allItemIdMap.put(GXHDO102B020Const.YOUZAIHYOURYOUKAISI_DAY, "溶剤秤量開始日");
        allItemIdMap.put(GXHDO102B020Const.YOUZAIHYOURYOUKAISI_TIME, "溶剤秤量開始時間");
        allItemIdMap.put(GXHDO102B020Const.ZUNSANZAI1_ZAIRYOUHINMEI, "分散材①_材料品名");
        allItemIdMap.put(GXHDO102B020Const.ZUNSANZAI1_TYOUGOURYOUKIKAKU, "分散材①_調合量規格");
        allItemIdMap.put(GXHDO102B020Const.ZUNSANZAI1_BUZAIZAIKOLOTNO1, "分散材①_部材在庫No1");
        allItemIdMap.put(GXHDO102B020Const.ZUNSANZAI1_TYOUGOURYOU1, "分散材①_調合量1");
        allItemIdMap.put(GXHDO102B020Const.ZUNSANZAI1_BUZAIZAIKOLOTNO2, "分散材①_部材在庫No2");
        allItemIdMap.put(GXHDO102B020Const.ZUNSANZAI1_TYOUGOURYOU2, "分散材①_調合量2");
        allItemIdMap.put(GXHDO102B020Const.ZUNSANZAI2_ZAIRYOUHINMEI, "分散材②_材料品名");
        allItemIdMap.put(GXHDO102B020Const.ZUNSANZAI2_TYOUGOURYOUKIKAKU, "分散材②_調合量規格");
        allItemIdMap.put(GXHDO102B020Const.ZUNSANZAI2_BUZAIZAIKOLOTNO1, "分散材②_部材在庫No1");
        allItemIdMap.put(GXHDO102B020Const.ZUNSANZAI2_TYOUGOURYOU1, "分散材②_調合量1");
        allItemIdMap.put(GXHDO102B020Const.ZUNSANZAI2_BUZAIZAIKOLOTNO2, "分散材②_部材在庫No2");
        allItemIdMap.put(GXHDO102B020Const.ZUNSANZAI2_TYOUGOURYOU2, "分散材②_調合量2");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI1_ZAIRYOUHINMEI, "溶剤①_材料品名");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI1_TYOUGOURYOUKIKAKU, "溶剤①_調合量規格");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI1_BUZAIZAIKOLOTNO1, "溶剤①_部材在庫No1");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI1_TYOUGOURYOU1, "溶剤①_調合量1");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI1_BUZAIZAIKOLOTNO2, "溶剤①_部材在庫No2");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI1_TYOUGOURYOU2, "溶剤①_調合量2");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI2_ZAIRYOUHINMEI, "溶剤②_材料品名");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI2_TYOUGOURYOUKIKAKU, "溶剤②_調合量規格");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI2_BUZAIZAIKOLOTNO1, "溶剤②_部材在庫No1");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI2_TYOUGOURYOU1, "溶剤②_調合量1");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI2_BUZAIZAIKOLOTNO2, "溶剤②_部材在庫No2");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI2_TYOUGOURYOU2, "溶剤②_調合量2");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI3_ZAIRYOUHINMEI, "溶剤③_材料品名");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI3_TYOUGOURYOUKIKAKU, "溶剤③_調合量規格");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI3_BUZAIZAIKOLOTNO1, "溶剤③_部材在庫No1");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI3_TYOUGOURYOU1, "溶剤③_調合量1");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI3_BUZAIZAIKOLOTNO2, "溶剤③_部材在庫No2");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI3_TYOUGOURYOU2, "溶剤③_調合量2");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI4_ZAIRYOUHINMEI, "溶剤④_材料品名");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI4_TYOUGOURYOUKIKAKU, "溶剤④_調合量規格");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI4_BUZAIZAIKOLOTNO1, "溶剤④_部材在庫No1");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI4_TYOUGOURYOU1, "溶剤④_調合量1");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI4_BUZAIZAIKOLOTNO2, "溶剤④_部材在庫No2");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI4_TYOUGOURYOU2, "溶剤④_調合量2");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI5_ZAIRYOUHINMEI, "溶剤⑤_材料品名");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI5_TYOUGOURYOUKIKAKU, "溶剤⑤_調合量規格");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI5_BUZAIZAIKOLOTNO1, "溶剤⑤_部材在庫No1");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI5_TYOUGOURYOU1, "溶剤⑤_調合量1");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI5_BUZAIZAIKOLOTNO2, "溶剤⑤_部材在庫No2");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI5_TYOUGOURYOU2, "溶剤⑤_調合量2");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI6_ZAIRYOUHINMEI, "溶剤⑥_材料品名");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI6_TYOUGOURYOUKIKAKU, "溶剤⑥_調合量規格");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI6_BUZAIZAIKOLOTNO1, "溶剤⑥_部材在庫No1");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI6_TYOUGOURYOU1, "溶剤⑥_調合量1");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI6_BUZAIZAIKOLOTNO2, "溶剤⑥_部材在庫No2");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI6_TYOUGOURYOU2, "溶剤⑥_調合量2");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI7_ZAIRYOUHINMEI, "溶剤⑦_材料品名");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI7_TYOUGOURYOUKIKAKU, "溶剤⑦_調合量規格");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI7_BUZAIZAIKOLOTNO1, "溶剤⑦_部材在庫No1");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI7_TYOUGOURYOU1, "溶剤⑦_調合量1");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI7_BUZAIZAIKOLOTNO2, "溶剤⑦_部材在庫No2");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI7_TYOUGOURYOU2, "溶剤⑦_調合量2");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI8_ZAIRYOUHINMEI, "溶剤⑧_材料品名");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI8_TYOUGOURYOUKIKAKU, "溶剤⑧_調合量規格");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI8_BUZAIZAIKOLOTNO1, "溶剤⑧_部材在庫No1");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI8_TYOUGOURYOU1, "溶剤⑧_調合量1");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI8_BUZAIZAIKOLOTNO2, "溶剤⑧_部材在庫No2");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI8_TYOUGOURYOU2, "溶剤⑧_調合量2");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI9_ZAIRYOUHINMEI, "溶剤⑨_材料品名");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI9_TYOUGOURYOUKIKAKU, "溶剤⑨_調合量規格");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI9_BUZAIZAIKOLOTNO1, "溶剤⑨_部材在庫No1");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI9_TYOUGOURYOU1, "溶剤⑨_調合量1");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI9_BUZAIZAIKOLOTNO2, "溶剤⑨_部材在庫No2");
        allItemIdMap.put(GXHDO102B020Const.YOUZAI9_TYOUGOURYOU2, "溶剤⑨_調合量2");
        allItemIdMap.put(GXHDO102B020Const.YOUZAIHYOURYOUSYUURYOU_DAY, "溶剤秤量終了日");
        allItemIdMap.put(GXHDO102B020Const.YOUZAIHYOURYOUSYUURYOU_TIME, "溶剤秤量終了時間");
        allItemIdMap.put(GXHDO102B020Const.KAKUHANKI, "撹拌機");
        allItemIdMap.put(GXHDO102B020Const.KAITENSUU, "回転数");
        allItemIdMap.put(GXHDO102B020Const.KAKUHANJIKAN, "撹拌時間");
        allItemIdMap.put(GXHDO102B020Const.KAKUHANKAISI_DAY, "撹拌開始日");
        allItemIdMap.put(GXHDO102B020Const.KAKUHANKAISI_TIME, "撹拌開始時間");
        allItemIdMap.put(GXHDO102B020Const.KAKUHANSYUURYOU_DAY, "撹拌終了日");
        allItemIdMap.put(GXHDO102B020Const.KAKUHANSYUURYOU_TIME, "撹拌終了時間");
        allItemIdMap.put(GXHDO102B020Const.TENKAZAISLURRYHRKAISI_DAY, "添加材ｽﾗﾘｰ秤量開始日");
        allItemIdMap.put(GXHDO102B020Const.TENKAZAISLURRYHRKAISI_TIME, "添加材ｽﾗﾘｰ秤量開始時間");
        allItemIdMap.put(GXHDO102B020Const.TENKAZAISLURRY_ZAIRYOUHINMEI, "添加材ｽﾗﾘｰ_材料品名");
        allItemIdMap.put(GXHDO102B020Const.TENKAZAISLURRY_WIPLOTNO, "添加材ｽﾗﾘｰ_WIPﾛｯﾄNo");
        allItemIdMap.put(GXHDO102B020Const.TENKAZAISLURRY_TGRKIKAKU, "添加材ｽﾗﾘｰ_調合量規格");
        allItemIdMap.put(GXHDO102B020Const.TENKAZAISLURRY_FTAIJYUURYOU1, "添加材ｽﾗﾘｰ_風袋重量1");
        allItemIdMap.put(GXHDO102B020Const.TENKAZAISLURRY_TYOUGOURYOU1, "添加材ｽﾗﾘｰ_調合量1");
        allItemIdMap.put(GXHDO102B020Const.TENKAZAISLURRY_FTAIJYUURYOU2, "添加材ｽﾗﾘｰ_風袋重量2");
        allItemIdMap.put(GXHDO102B020Const.TENKAZAISLURRY_TYOUGOURYOU2, "添加材ｽﾗﾘｰ_調合量2");
        allItemIdMap.put(GXHDO102B020Const.TENKAZAISLURRYHRSYURYO_DAY, "添加材ｽﾗﾘｰ秤量終了日");
        allItemIdMap.put(GXHDO102B020Const.TENKAZAISLURRYHRSYURYO_TIME, "添加材ｽﾗﾘｰ秤量終了時間");
        allItemIdMap.put(GXHDO102B020Const.KOKEIBUNSOKUTEITANTOUSYA, "固形分測定担当者");
        allItemIdMap.put(GXHDO102B020Const.BIKOU1, "備考1");
        allItemIdMap.put(GXHDO102B020Const.BIKOU2, "備考2");

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
        GXHDO102B020A bean = (GXHDO102B020A) getFormBean("gXHDO102B020A");
        notShowItemList.forEach((notShowItem) -> {
            itemList.remove(getItemRow(itemList, notShowItem));
        });

        bean.setZunsanzai1_buzaizaikolotno2(null);
        bean.setZunsanzai1_tyougouryou2(null);
        bean.setKakuhanki(null);
        bean.setKaitensuu(null);
        bean.setKakuhanjikan(null);
        bean.setKakuhankaisi_day(null);
        bean.setKakuhankaisi_time(null);
        bean.setKakuhansyuuryou_day(null);
        bean.setKakuhansyuuryou_time(null);
        bean.setTenkazaislurry_ftaijyuuryou2(null);
        bean.setTenkazaislurry_tyougouryou2(null);
    }

    /**
     * 項目の表示制御
     *
     * @param processData 処理制御データ
     * @param notShowItemList 画面非表示項目リスト
     * @param yozaiNum 溶剤no
     */
    private void removeHinmeiItemsFromItemList(ProcessData processData, List<String> notShowItemList, int yozaiNum) {

        List<FXHDD01> itemList = processData.getItemList();
        GXHDO102B020A bean = (GXHDO102B020A) getFormBean("gXHDO102B020A");
        notShowItemList.forEach((notShowItem) -> {
            itemList.remove(getItemRow(itemList, notShowItem));
        });

        switch (yozaiNum) {
            // 「溶剤3_材料品名」が取得できなかった場合
            case 3:
                bean.setYouzai3_zairyouhinmei(null);
                bean.setYouzai3_tyougouryoukikaku(null);
                bean.setYouzai3_buzaizaikolotno1(null);
                bean.setYouzai3_tyougouryou1(null);
                bean.setYouzai3_buzaizaikolotno2(null);
                bean.setYouzai3_tyougouryou2(null);
                break;
            // 「溶剤8_材料品名」が取得できなかった場合
            case 8:
                bean.setYouzai8_zairyouhinmei(null);
                bean.setYouzai8_tyougouryoukikaku(null);
                bean.setYouzai8_buzaizaikolotno1(null);
                bean.setYouzai8_tyougouryou1(null);
                bean.setYouzai8_buzaizaikolotno2(null);
                bean.setYouzai8_tyougouryou2(null);
                break;
            // 「溶剤9_材料品名」が取得できなかった場合
            case 9:
                bean.setYouzai9_zairyouhinmei(null);
                bean.setYouzai9_tyougouryoukikaku(null);
                bean.setYouzai9_buzaizaikolotno1(null);
                bean.setYouzai9_tyougouryou1(null);
                bean.setYouzai9_buzaizaikolotno2(null);
                bean.setYouzai9_tyougouryou2(null);
                break;
        }
    }

    /**
     * 項目の表示制御
     *
     * @param processData 処理制御データ
     * @param yozaiNum 溶剤no
     */
    private void doHinmeiItemsRendered(ProcessData processData, int yozaiNum) {
        List<String> notShowYouzaiItemList = null;
        String itemId = "";
        switch (yozaiNum) {
            // 「溶剤3_材料品名」が取得できなかった場合
            case 3:
                notShowYouzaiItemList = Arrays.asList(GXHDO102B020Const.YOUZAI3_ZAIRYOUHINMEI, GXHDO102B020Const.YOUZAI3_TYOUGOURYOUKIKAKU, GXHDO102B020Const.YOUZAI3_BUZAIZAIKOLOTNO1,
                        GXHDO102B020Const.YOUZAI3_TYOUGOURYOU1, GXHDO102B020Const.YOUZAI3_BUZAIZAIKOLOTNO2, GXHDO102B020Const.YOUZAI3_TYOUGOURYOU2);
                itemId = GXHDO102B020Const.YOUZAI3_ZAIRYOUHINMEI;
                break;
            // 「溶剤8_材料品名」が取得できなかった場合
            case 8:
                notShowYouzaiItemList = Arrays.asList(GXHDO102B020Const.YOUZAI8_ZAIRYOUHINMEI, GXHDO102B020Const.YOUZAI8_TYOUGOURYOUKIKAKU, GXHDO102B020Const.YOUZAI8_BUZAIZAIKOLOTNO1,
                        GXHDO102B020Const.YOUZAI8_TYOUGOURYOU1, GXHDO102B020Const.YOUZAI8_BUZAIZAIKOLOTNO2, GXHDO102B020Const.YOUZAI8_TYOUGOURYOU2);
                itemId = GXHDO102B020Const.YOUZAI8_ZAIRYOUHINMEI;
                break;
            // 「溶剤9_材料品名」が取得できなかった場合
            case 9:
                notShowYouzaiItemList = Arrays.asList(GXHDO102B020Const.YOUZAI9_ZAIRYOUHINMEI, GXHDO102B020Const.YOUZAI9_TYOUGOURYOUKIKAKU, GXHDO102B020Const.YOUZAI9_BUZAIZAIKOLOTNO1,
                        GXHDO102B020Const.YOUZAI9_TYOUGOURYOU1, GXHDO102B020Const.YOUZAI9_BUZAIZAIKOLOTNO2, GXHDO102B020Const.YOUZAI9_TYOUGOURYOU2);
                itemId = GXHDO102B020Const.YOUZAI9_ZAIRYOUHINMEI;
                break;
        }

        if (StringUtil.isEmpty(itemId) || notShowYouzaiItemList == null) {
            return;
        }
        FXHDD01 itemYouzai_zairyouhinmei = getItemRow(processData.getItemList(), itemId);
        if (itemYouzai_zairyouhinmei == null) {
            removeHinmeiItemsFromItemList(processData, notShowYouzaiItemList, yozaiNum);
            return;
        }
        String youzai_zairyouhinmeiVal = getItemKikakuchi(processData.getItemList(), itemId, null);
        if (StringUtil.isEmpty(youzai_zairyouhinmeiVal)) {
            removeHinmeiItemsFromItemList(processData, notShowYouzaiItemList, yozaiNum);
        }
    }

    /**
     * 品名毎の項目の表示制御
     *
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void setHinmeiItemRendered(ProcessData processData) throws SQLException {
        // 「溶剤3_材料品名」が取得できなかった場合、項目を非表示にする。
        doHinmeiItemsRendered(processData, 3);
        // 「溶剤8_材料品名」が取得できなかった場合、項目を非表示にする。
        doHinmeiItemsRendered(processData, 8);
        // 「溶剤9_材料品名」が取得できなかった場合、項目を非表示にする。
        doHinmeiItemsRendered(processData, 9);
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
        String syurui = "誘電体ｽﾗﾘｰ作製";
        // [ﾊﾟﾗﾒｰﾀﾏｽﾀ]から、ﾃﾞｰﾀを取得
        Map fxhbm03Data = loadFxhbm03Data(queryRunnerDoc, "誘電体ｽﾗﾘｰ作製_溶剤・添加材ｽﾗﾘｰ秤量_表示制御");
        // 画面非表示項目リスト: 分散材①_部材在庫No2、分散材①_調合量2、撹拌機、回転数、撹拌時間、撹拌開始日、撹拌開始時間、撹拌終了日、撹拌終了時間、添加材ｽﾗﾘｰ_風袋重量2、添加材ｽﾗﾘｰ_調合量2
        List<String> notShowItemList = Arrays.asList(GXHDO102B020Const.ZUNSANZAI1_BUZAIZAIKOLOTNO2, GXHDO102B020Const.ZUNSANZAI1_TYOUGOURYOU2, GXHDO102B020Const.KAKUHANKI,
                GXHDO102B020Const.KAITENSUU, GXHDO102B020Const.KAKUHANJIKAN, GXHDO102B020Const.KAKUHANKAISI_DAY, GXHDO102B020Const.KAKUHANKAISI_TIME,
                GXHDO102B020Const.KAKUHANSYUURYOU_DAY, GXHDO102B020Const.KAKUHANSYUURYOU_TIME, GXHDO102B020Const.TENKAZAISLURRY_FTAIJYUURYOU2,
                GXHDO102B020Const.TENKAZAISLURRY_TYOUGOURYOU2);
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
}
