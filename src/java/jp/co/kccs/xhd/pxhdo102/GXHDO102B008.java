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
import jp.co.kccs.xhd.common.ErrorListMessage;
import jp.co.kccs.xhd.common.InitMessage;
import jp.co.kccs.xhd.common.KikakuError;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.db.model.FXHDD02;
import jp.co.kccs.xhd.db.model.SikakariJson;
import jp.co.kccs.xhd.db.model.SrTenkaTyogo;
import jp.co.kccs.xhd.db.model.SubSrTenkaTyogo;
import jp.co.kccs.xhd.model.GXHDO102C004Model;
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
 * 変更日	2021/10/15<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B008(添加材ｽﾗﾘｰ作製・添加材調合)
 *
 * @author KCSS K.Jo
 * @since 2021/10/15
 */
public class GXHDO102B008 implements IFormLogic {

    private static final Logger LOGGER = Logger.getLogger(GXHDO102B008.class.getName());
    private static final String JOTAI_FLG_KARI_TOROKU = "0";
    private static final String JOTAI_FLG_TOROKUZUMI = "1";
    private static final String JOTAI_FLG_SAKUJO = "9";
    private static final String SQL_STATE_RECORD_LOCK_ERR = "55P03";

    /**
     * コンストラクタ
     */
    public GXHDO102B008() {
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
            initGXHDO102B008A(processData);

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
                    GXHDO102B008Const.BTN_EDABAN_COPY_TOP,
                    GXHDO102B008Const.BTN_HYOURYOUKAISINICHIJI_TOP,
                    GXHDO102B008Const.BTN_HYOURYOUSYUURYOUNICHIJI_TOP,
                    GXHDO102B008Const.BTN_EDABAN_COPY_BOTTOM,
                    GXHDO102B008Const.BTN_HYOURYOUKAISINICHIJI_BOTTOM,
                    GXHDO102B008Const.BTN_HYOURYOUSYUURYOUNICHIJI_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO102B008Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO102B008Const.BTN_INSERT_TOP,
                    GXHDO102B008Const.BTN_DELETE_TOP,
                    GXHDO102B008Const.BTN_UPDATE_TOP,
                    GXHDO102B008Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO102B008Const.BTN_INSERT_BOTTOM,
                    GXHDO102B008Const.BTN_DELETE_BOTTOM,
                    GXHDO102B008Const.BTN_UPDATE_BOTTOM
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
            case GXHDO102B008Const.BTN_EDABAN_COPY_TOP:
            case GXHDO102B008Const.BTN_EDABAN_COPY_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 仮登録
            case GXHDO102B008Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO102B008Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO102B008Const.BTN_INSERT_TOP:
            case GXHDO102B008Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 修正
            case GXHDO102B008Const.BTN_UPDATE_TOP:
            case GXHDO102B008Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO102B008Const.BTN_DELETE_TOP:
            case GXHDO102B008Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 秤量開始日時
            case GXHDO102B008Const.BTN_HYOURYOUKAISINICHIJI_TOP:
            case GXHDO102B008Const.BTN_HYOURYOUKAISINICHIJI_BOTTOM:
                method = "setHyouryoukaisinichijiDateTime";
                break;
            // 秤量終了日時
            case GXHDO102B008Const.BTN_HYOURYOUSYUURYOUNICHIJI_TOP:
            case GXHDO102B008Const.BTN_HYOURYOUSYUURYOUNICHIJI_BOTTOM:
                method = "setHyouryousyuuryounichijiDateTime";
                break;
            // 添加材1_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B008Const.BTN_OPENC004SUBGAMEN1:
                method = "openC004SubGamen1";
                break;
            // 添加材2_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B008Const.BTN_OPENC004SUBGAMEN2:
                method = "openC004SubGamen2";
                break;
            // 添加材3_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B008Const.BTN_OPENC004SUBGAMEN3:
                method = "openC004SubGamen3";
                break;
            // 添加材4_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B008Const.BTN_OPENC004SUBGAMEN4:
                method = "openC004SubGamen4";
                break;
            // 添加材5_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B008Const.BTN_OPENC004SUBGAMEN5:
                method = "openC004SubGamen5";
                break;
            // 添加材6_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B008Const.BTN_OPENC004SUBGAMEN6:
                method = "openC004SubGamen6";
                break;
            // 添加材7_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B008Const.BTN_OPENC004SUBGAMEN7:
                method = "openC004SubGamen7";
                break;
            // 添加材8_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B008Const.BTN_OPENC004SUBGAMEN8:
                method = "openC004SubGamen8";
                break;
            // 添加材9_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B008Const.BTN_OPENC004SUBGAMEN9:
                method = "openC004SubGamen9";
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

            // 添加材ｽﾗﾘｰ作製・添加材調合の入力項目の登録データ(仮登録時は仮登録データ)を取得
            List<SrTenkaTyogo> srTenkaTyogoDataList = getSrTenkaTyogoData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo9, oyalotEdaban);
            if (srTenkaTyogoDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }
            // 添加材ｽﾗﾘｰ作製・添加材調合入力_ｻﾌﾞ画面データ取得
            List<SubSrTenkaTyogo> subSrTenkaTyogoDataList = getSubSrTenkaTyogoData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo9, oyalotEdaban);
            if (subSrTenkaTyogoDataList.isEmpty() || subSrTenkaTyogoDataList.size() != 9) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }
            // メイン画面データ設定
            setInputItemDataMainForm(processData, srTenkaTyogoDataList.get(0));
            // 添加材ｽﾗﾘｰ作製・添加材調合入力_ｻﾌﾞ画面データ設定
            setInputItemDataSubFormC004(processData, subSrTenkaTyogoDataList);

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
     * @return チェックの正常終了時はNULL、異常時は内容に応じたエラーメッセージ情報を返す。
     */
    private ErrorMessageInfo checkInputKikakuchi(ProcessData processData, List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList) {
        List<String> tyougouryouList = new ArrayList<>();
        List<String> tyogouryoukikaku = Arrays.asList(GXHDO102B008Const.TENKAZAI1_TYOUGOURYOUKIKAKU, GXHDO102B008Const.TENKAZAI2_TYOUGOURYOUKIKAKU,
                GXHDO102B008Const.TENKAZAI3_TYOUGOURYOUKIKAKU, GXHDO102B008Const.TENKAZAI4_TYOUGOURYOUKIKAKU, GXHDO102B008Const.TENKAZAI5_TYOUGOURYOUKIKAKU,
                GXHDO102B008Const.TENKAZAI6_TYOUGOURYOUKIKAKU, GXHDO102B008Const.TENKAZAI7_TYOUGOURYOUKIKAKU, GXHDO102B008Const.TENKAZAI8_TYOUGOURYOUKIKAKU,
                GXHDO102B008Const.TENKAZAI9_TYOUGOURYOUKIKAKU
        );
        List<String> tenkazai1_tyougouryouList = Arrays.asList(GXHDO102B008Const.TENKAZAI1_TYOUGOURYOU1, GXHDO102B008Const.TENKAZAI1_TYOUGOURYOU2); // 添加材1_調合量
        List<String> tenkazai2_tyougouryouList = Arrays.asList(GXHDO102B008Const.TENKAZAI2_TYOUGOURYOU1, GXHDO102B008Const.TENKAZAI2_TYOUGOURYOU2); // 添加材2_調合量
        List<String> tenkazai3_tyougouryouList = Arrays.asList(GXHDO102B008Const.TENKAZAI3_TYOUGOURYOU1, GXHDO102B008Const.TENKAZAI3_TYOUGOURYOU2); // 添加材3_調合量
        List<String> tenkazai4_tyougouryouList = Arrays.asList(GXHDO102B008Const.TENKAZAI4_TYOUGOURYOU1, GXHDO102B008Const.TENKAZAI4_TYOUGOURYOU2); // 添加材4_調合量
        List<String> tenkazai5_tyougouryouList = Arrays.asList(GXHDO102B008Const.TENKAZAI5_TYOUGOURYOU1, GXHDO102B008Const.TENKAZAI5_TYOUGOURYOU2); // 添加材5_調合量
        List<String> tenkazai6_tyougouryouList = Arrays.asList(GXHDO102B008Const.TENKAZAI6_TYOUGOURYOU1, GXHDO102B008Const.TENKAZAI6_TYOUGOURYOU2); // 添加材6_調合量
        List<String> tenkazai7_tyougouryouList = Arrays.asList(GXHDO102B008Const.TENKAZAI7_TYOUGOURYOU1, GXHDO102B008Const.TENKAZAI7_TYOUGOURYOU2); // 添加材7_調合量
        List<String> tenkazai8_tyougouryouList = Arrays.asList(GXHDO102B008Const.TENKAZAI8_TYOUGOURYOU1, GXHDO102B008Const.TENKAZAI8_TYOUGOURYOU2); // 添加材8_調合量
        List<String> tenkazai9_tyougouryouList = Arrays.asList(GXHDO102B008Const.TENKAZAI9_TYOUGOURYOU1, GXHDO102B008Const.TENKAZAI9_TYOUGOURYOU2); // 添加材9_調合量

        // 規格値の入力値チェック必要の項目リスト
        List<FXHDD01> itemList = new ArrayList<>();
        setKikakuValueAndLabel1(processData, itemList, tenkazai1_tyougouryouList, tyogouryoukikaku.get(0), "添加材①_調合量"); // 添加材①_調合量の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, tenkazai2_tyougouryouList, tyogouryoukikaku.get(1), "添加材②_調合量"); // 添加材②_調合量の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, tenkazai3_tyougouryouList, tyogouryoukikaku.get(2), "添加材③_調合量"); // 添加材③_調合量の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, tenkazai4_tyougouryouList, tyogouryoukikaku.get(3), "添加材④_調合量"); // 添加材④_調合量の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, tenkazai5_tyougouryouList, tyogouryoukikaku.get(4), "添加材⑤_調合量"); // 添加材⑤_調合量の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, tenkazai6_tyougouryouList, tyogouryoukikaku.get(5), "添加材⑥_調合量"); // 添加材⑥_調合量の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, tenkazai7_tyougouryouList, tyogouryoukikaku.get(6), "添加材⑦_調合量"); // 添加材⑦_調合量の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, tenkazai8_tyougouryouList, tyogouryoukikaku.get(7), "添加材⑧_調合量"); // 添加材⑧_調合量の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, tenkazai9_tyougouryouList, tyogouryoukikaku.get(8), "添加材⑨_調合量"); // 添加材⑨_調合量の規格値と表示ﾗﾍﾞﾙ1を設置

        tyougouryouList.addAll(tenkazai1_tyougouryouList);
        tyougouryouList.addAll(tenkazai2_tyougouryouList);
        tyougouryouList.addAll(tenkazai3_tyougouryouList);
        tyougouryouList.addAll(tenkazai4_tyougouryouList);
        tyougouryouList.addAll(tenkazai5_tyougouryouList);
        tyougouryouList.addAll(tenkazai6_tyougouryouList);
        tyougouryouList.addAll(tenkazai7_tyougouryouList);
        tyougouryouList.addAll(tenkazai8_tyougouryouList);
        tyougouryouList.addAll(tenkazai9_tyougouryouList);

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
        return errorMessageInfo;
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

                // 添加材ｽﾗﾘｰ作製・添加材調合_仮登録処理
                insertTmpSrTenkaTyogo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, strSystime, processData);
                // 添加材ｽﾗﾘｰ作製・添加材調合入力ｻﾌﾞ画面の仮登録処理
                for (int i = 1; i < 10; i++) {
                    insertTmpSubSrTenkaTyogo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, i, strSystime);
                }
            } else {

                // 添加材ｽﾗﾘｰ作製・添加材調合_仮登録更新処理
                updateTmpSrTenkaTyogo(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);
                // 添加材ｽﾗﾘｰ作製・添加材調合入力ｻﾌﾞ画面の仮登録更新処理
                for (int i = 1; i < 10; i++) {
                    updateTmpSubSrTenkaTyogo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, i, strSystime);
                }
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
        // 秤量開始日時、秤量終了日時前後チェック
        FXHDD01 itemHyouryoukaisiDay = getItemRow(processData.getItemList(), GXHDO102B008Const.HYOURYOUKAISI_DAY); // 秤量開始日
        FXHDD01 itemHyouryoukaisiTime = getItemRow(processData.getItemList(), GXHDO102B008Const.HYOURYOUKAISI_TIME); // 秤量開始時間
        Date hyouryoukaisiDate = DateUtil.convertStringToDate(itemHyouryoukaisiDay.getValue(), itemHyouryoukaisiTime.getValue());
        FXHDD01 itemHyouryousyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B008Const.HYOURYOUSYUURYOU_DAY); //秤量終了日
        FXHDD01 itemHyouryousyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B008Const.HYOURYOUSYUURYOU_TIME); //秤量終了時間
        Date hyouryousyuuryouDate = DateUtil.convertStringToDate(itemHyouryousyuuryouDay.getValue(), itemHyouryousyuuryouTime.getValue());
        //R001チェック呼出し
        String msgCheckR001 = validateUtil.checkR001(itemHyouryoukaisiDay.getLabel1(), hyouryoukaisiDate, itemHyouryoukaisiTime.getLabel1(), hyouryousyuuryouDate);
        if (!StringUtil.isEmpty(msgCheckR001)) {
            //エラー発生時
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemHyouryoukaisiDay, itemHyouryoukaisiTime, itemHyouryousyuuryouDay, itemHyouryousyuuryouTime);
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
            SrTenkaTyogo tmpSrTenkaTyogo = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrTenkaTyogo> srTenkaTyogoList = getSrTenkaTyogoData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo9, edaban);
                if (!srTenkaTyogoList.isEmpty()) {
                    tmpSrTenkaTyogo = srTenkaTyogoList.get(0);
                }

                deleteTmpSrTenkaTyogo(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);
                deleteTmpSubSrTenkaTyogo(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);
            }

            // 添加材ｽﾗﾘｰ作製・添加材調合_登録処理
            insertSrTenkaTyogo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, strSystime, processData, tmpSrTenkaTyogo);
            // 添加材ｽﾗﾘｰ作製・添加材調合入力ｻﾌﾞ画面の仮登録更新処理
            for (int i = 1; i < 10; i++) {
                insertSubSrTenkaTyogo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, i, strSystime);
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
        processData.setUserAuthParam(GXHDO102B008Const.USER_AUTH_UPDATE_PARAM);

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

            // 添加材ｽﾗﾘｰ作製・添加材調合_更新処理
            updateSrTenkaTyogo(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);
            // 添加材ｽﾗﾘｰ作製・添加材調合入力ｻﾌﾞ画面の更新処理
            for (int i = 1; i < 10; i++) {
                updateSubSrTenkaTyogo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, i, strSystime);
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
        processData.setUserAuthParam(GXHDO102B008Const.USER_AUTH_DELETE_PARAM);

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

            // 添加材ｽﾗﾘｰ作製・添加材調合_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo9, edaban, paramJissekino);
            insertDeleteDataTmpSrTenkaTyogo(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo9, edaban, strSystime);

            // 添加材ｽﾗﾘｰ作製・添加材調合入力_ｻﾌﾞ画面仮登録登録処理
            insertDeleteDataTmpSubSrTenkaTyogo(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo9, edaban, strSystime);

            // 添加材ｽﾗﾘｰ作製・添加材調合_削除処理
            deleteSrTenkaTyogo(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);

            // 添加材ｽﾗﾘｰ作製・添加材調合入力_ｻﾌﾞ画面削除処理
            deleteSubSrTenkaTyogo(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);

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
     * 秤量開始日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setHyouryoukaisinichijiDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B008Const.HYOURYOUKAISI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B008Const.HYOURYOUKAISI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 秤量終了日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setHyouryousyuuryounichijiDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B008Const.HYOURYOUSYUURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B008Const.HYOURYOUSYUURYOU_TIME);
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
                        GXHDO102B008Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B008Const.BTN_HYOURYOUKAISINICHIJI_TOP,
                        GXHDO102B008Const.BTN_HYOURYOUSYUURYOUNICHIJI_TOP,
                        GXHDO102B008Const.BTN_UPDATE_TOP,
                        GXHDO102B008Const.BTN_DELETE_TOP,
                        GXHDO102B008Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B008Const.BTN_HYOURYOUKAISINICHIJI_BOTTOM,
                        GXHDO102B008Const.BTN_HYOURYOUSYUURYOUNICHIJI_BOTTOM,
                        GXHDO102B008Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B008Const.BTN_DELETE_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B008Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B008Const.BTN_INSERT_TOP,
                        GXHDO102B008Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B008Const.BTN_INSERT_BOTTOM));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO102B008Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B008Const.BTN_HYOURYOUKAISINICHIJI_TOP,
                        GXHDO102B008Const.BTN_HYOURYOUSYUURYOUNICHIJI_TOP,
                        GXHDO102B008Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B008Const.BTN_INSERT_TOP,
                        GXHDO102B008Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B008Const.BTN_HYOURYOUKAISINICHIJI_BOTTOM,
                        GXHDO102B008Const.BTN_HYOURYOUSYUURYOUNICHIJI_BOTTOM,
                        GXHDO102B008Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B008Const.BTN_INSERT_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B008Const.BTN_UPDATE_TOP,
                        GXHDO102B008Const.BTN_DELETE_TOP,
                        GXHDO102B008Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B008Const.BTN_DELETE_BOTTOM
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
        } else {
            // ﾛｯﾄ区分チェック
            String lotkubun = (String) shikakariData.get("lotkubun");
            if (StringUtil.isEmpty(lotkubun)) {
                errorMessageList.add(MessageUtil.getMessage("XHD-000015"));
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
        setViewItemData(processData, shikakariData, lotNo);
        // 画面のラベル項目の値の背景色を取得できない場合、デフォルト値を設置
        GXHDO102C004Logic.setItemStyle(processData.getItemList());
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
        this.setItemData(processData, GXHDO102B008Const.WIPLOTNO, lotNo);
        // 添加材ｽﾗﾘｰ品名
        this.setItemData(processData, GXHDO102B008Const.TENKAZAISLURRYHINMEI, StringUtil.nullToBlank(getMapData(shikakariData, "hinmei")));
        // 添加材ｽﾗﾘｰLotNo
        this.setItemData(processData, GXHDO102B008Const.TENKAZAISLURRYLOTNO, StringUtil.nullToBlank(getMapData(shikakariData, "lotno")));
        // ﾛｯﾄ区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode"));
        // ﾛｯﾄ区分名称
        String lotkubun = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubun"));

        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO102B008Const.LOTKUBUN, "");
        } else {
            if (!StringUtil.isEmpty(lotkubun)) {
                lotkubuncode = lotkubuncode + ":" + lotkubun;
            }
            this.setItemData(processData, GXHDO102B008Const.LOTKUBUN, lotkubuncode);
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

        List<SrTenkaTyogo> srTenkaTyogoList = new ArrayList<>();
        List<SubSrTenkaTyogo> subSrTenkaTyogoList = new ArrayList<>();
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

                // 添加材ｽﾗﾘｰ作製・添加材調合入力_ｻﾌﾞ画面データ設定
                setInputItemDataSubFormC004(processData, null);
                return true;
            }

            // 添加材ｽﾗﾘｰ作製・添加材調合データ取得
            srTenkaTyogoList = getSrTenkaTyogoData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo9, edaban);
            if (srTenkaTyogoList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // 添加材ｽﾗﾘｰ作製・添加材調合入力_サブ画面データ取得
            subSrTenkaTyogoList = getSubSrTenkaTyogoData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo9, edaban);
            if (subSrTenkaTyogoList.isEmpty() || subSrTenkaTyogoList.size() != 9) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }
            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srTenkaTyogoList.isEmpty() || (subSrTenkaTyogoList.isEmpty() || subSrTenkaTyogoList.size() != 9)) {
            return false;
        }
        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srTenkaTyogoList.get(0));
        // 添加材ｽﾗﾘｰ作製・添加材調合入力_ｻﾌﾞ画面データ設定
        setInputItemDataSubFormC004(processData, subSrTenkaTyogoList);
        return true;

    }

    /**
     * データ設定処理
     *
     * @param processData 処理制御データ
     * @param srTenkaTyogo 添加材ｽﾗﾘｰ作製・添加材調合
     */
    private void setInputItemDataMainForm(ProcessData processData, SrTenkaTyogo srTenkaTyogo) {

        // 添加材ｽﾗﾘｰ品名
        this.setItemData(processData, GXHDO102B008Const.TENKAZAISLURRYHINMEI, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAISLURRYHINMEI, srTenkaTyogo));

        // 添加材ｽﾗﾘｰLotNo
        this.setItemData(processData, GXHDO102B008Const.TENKAZAISLURRYLOTNO, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAISLURRYLOTNO, srTenkaTyogo));

        // ﾛｯﾄ区分
        this.setItemData(processData, GXHDO102B008Const.LOTKUBUN, getSrTenkaTyogoItemData(GXHDO102B008Const.LOTKUBUN, srTenkaTyogo));

        // 秤量号機
        this.setItemData(processData, GXHDO102B008Const.HYOURYOUGOUKI, getSrTenkaTyogoItemData(GXHDO102B008Const.HYOURYOUGOUKI, srTenkaTyogo));

        // 風袋重量
        this.setItemData(processData, GXHDO102B008Const.FUUTAIJYUURYOU, getSrTenkaTyogoItemData(GXHDO102B008Const.FUUTAIJYUURYOU, srTenkaTyogo));

        // 秤量開始日
        this.setItemData(processData, GXHDO102B008Const.HYOURYOUKAISI_DAY, getSrTenkaTyogoItemData(GXHDO102B008Const.HYOURYOUKAISI_DAY, srTenkaTyogo));

        // 秤量開始時間
        this.setItemData(processData, GXHDO102B008Const.HYOURYOUKAISI_TIME, getSrTenkaTyogoItemData(GXHDO102B008Const.HYOURYOUKAISI_TIME, srTenkaTyogo));

        // 添加材①_材料品名
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI1_ZAIRYOUMEI, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI1_ZAIRYOUMEI, srTenkaTyogo));

        // 添加材①_調合量規格
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI1_TYOUGOURYOUKIKAKU, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI1_TYOUGOURYOUKIKAKU, srTenkaTyogo));

        // 添加材①_部材在庫No1
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI1_BUZAIZAIKOLOTNO1, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI1_BUZAIZAIKOLOTNO1, srTenkaTyogo));

        // 添加材①_調合量1
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI1_TYOUGOURYOU1, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI1_TYOUGOURYOU1, srTenkaTyogo));

        // 添加材①_部材在庫No2
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI1_BUZAIZAIKOLOTNO2, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI1_BUZAIZAIKOLOTNO2, srTenkaTyogo));

        // 添加材①_調合量2
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI1_TYOUGOURYOU2, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI1_TYOUGOURYOU2, srTenkaTyogo));

        // 添加材②_材料品名
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI2_ZAIRYOUMEI, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI2_ZAIRYOUMEI, srTenkaTyogo));

        // 添加材②_調合量規格
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI2_TYOUGOURYOUKIKAKU, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI2_TYOUGOURYOUKIKAKU, srTenkaTyogo));

        // 添加材②_部材在庫No1
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI2_BUZAIZAIKOLOTNO1, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI2_BUZAIZAIKOLOTNO1, srTenkaTyogo));

        // 添加材②_調合量1
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI2_TYOUGOURYOU1, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI2_TYOUGOURYOU1, srTenkaTyogo));

        // 添加材②_部材在庫No2
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI2_BUZAIZAIKOLOTNO2, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI2_BUZAIZAIKOLOTNO2, srTenkaTyogo));

        // 添加材②_調合量2
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI2_TYOUGOURYOU2, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI2_TYOUGOURYOU2, srTenkaTyogo));

        // 添加材③_材料品名
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI3_ZAIRYOUMEI, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI3_ZAIRYOUMEI, srTenkaTyogo));

        // 添加材③_調合量規格
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI3_TYOUGOURYOUKIKAKU, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI3_TYOUGOURYOUKIKAKU, srTenkaTyogo));

        // 添加材③_部材在庫No1
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI3_BUZAIZAIKOLOTNO1, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI3_BUZAIZAIKOLOTNO1, srTenkaTyogo));

        // 添加材③_調合量1
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI3_TYOUGOURYOU1, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI3_TYOUGOURYOU1, srTenkaTyogo));

        // 添加材③_部材在庫No2
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI3_BUZAIZAIKOLOTNO2, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI3_BUZAIZAIKOLOTNO2, srTenkaTyogo));

        // 添加材③_調合量2
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI3_TYOUGOURYOU2, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI3_TYOUGOURYOU2, srTenkaTyogo));

        // 添加材④_材料品名
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI4_ZAIRYOUMEI, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI4_ZAIRYOUMEI, srTenkaTyogo));

        // 添加材④_調合量規格
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI4_TYOUGOURYOUKIKAKU, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI4_TYOUGOURYOUKIKAKU, srTenkaTyogo));

        // 添加材④_部材在庫No1
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI4_BUZAIZAIKOLOTNO1, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI4_BUZAIZAIKOLOTNO1, srTenkaTyogo));

        // 添加材④_調合量1
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI4_TYOUGOURYOU1, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI4_TYOUGOURYOU1, srTenkaTyogo));

        // 添加材④_部材在庫No2
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI4_BUZAIZAIKOLOTNO2, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI4_BUZAIZAIKOLOTNO2, srTenkaTyogo));

        // 添加材④_調合量2
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI4_TYOUGOURYOU2, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI4_TYOUGOURYOU2, srTenkaTyogo));

        // 添加材⑤_材料品名
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI5_ZAIRYOUMEI, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI5_ZAIRYOUMEI, srTenkaTyogo));

        // 添加材⑤_調合量規格
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI5_TYOUGOURYOUKIKAKU, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI5_TYOUGOURYOUKIKAKU, srTenkaTyogo));

        // 添加材⑤_部材在庫No1
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI5_BUZAIZAIKOLOTNO1, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI5_BUZAIZAIKOLOTNO1, srTenkaTyogo));

        // 添加材⑤_調合量1
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI5_TYOUGOURYOU1, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI5_TYOUGOURYOU1, srTenkaTyogo));

        // 添加材⑤_部材在庫No2
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI5_BUZAIZAIKOLOTNO2, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI5_BUZAIZAIKOLOTNO2, srTenkaTyogo));

        // 添加材⑤_調合量2
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI5_TYOUGOURYOU2, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI5_TYOUGOURYOU2, srTenkaTyogo));

        // 添加材⑥_材料品名
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI6_ZAIRYOUMEI, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI6_ZAIRYOUMEI, srTenkaTyogo));

        // 添加材⑥_調合量規格
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI6_TYOUGOURYOUKIKAKU, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI6_TYOUGOURYOUKIKAKU, srTenkaTyogo));

        // 添加材⑥_部材在庫No1
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI6_BUZAIZAIKOLOTNO1, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI6_BUZAIZAIKOLOTNO1, srTenkaTyogo));

        // 添加材⑥_調合量1
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI6_TYOUGOURYOU1, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI6_TYOUGOURYOU1, srTenkaTyogo));

        // 添加材⑥_部材在庫No2
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI6_BUZAIZAIKOLOTNO2, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI6_BUZAIZAIKOLOTNO2, srTenkaTyogo));

        // 添加材⑥_調合量2
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI6_TYOUGOURYOU2, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI6_TYOUGOURYOU2, srTenkaTyogo));

        // 添加材⑦_材料品名
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI7_ZAIRYOUMEI, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI7_ZAIRYOUMEI, srTenkaTyogo));

        // 添加材⑦_調合量規格
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI7_TYOUGOURYOUKIKAKU, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI7_TYOUGOURYOUKIKAKU, srTenkaTyogo));

        // 添加材⑦_部材在庫No1
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI7_BUZAIZAIKOLOTNO1, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI7_BUZAIZAIKOLOTNO1, srTenkaTyogo));

        // 添加材⑦_調合量1
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI7_TYOUGOURYOU1, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI7_TYOUGOURYOU1, srTenkaTyogo));

        // 添加材⑦_部材在庫No2
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI7_BUZAIZAIKOLOTNO2, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI7_BUZAIZAIKOLOTNO2, srTenkaTyogo));

        // 添加材⑦_調合量2
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI7_TYOUGOURYOU2, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI7_TYOUGOURYOU2, srTenkaTyogo));

        // 添加材⑧_材料品名
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI8_ZAIRYOUMEI, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI8_ZAIRYOUMEI, srTenkaTyogo));

        // 添加材⑧_調合量規格
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI8_TYOUGOURYOUKIKAKU, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI8_TYOUGOURYOUKIKAKU, srTenkaTyogo));

        // 添加材⑧_部材在庫No1
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI8_BUZAIZAIKOLOTNO1, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI8_BUZAIZAIKOLOTNO1, srTenkaTyogo));

        // 添加材⑧_調合量1
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI8_TYOUGOURYOU1, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI8_TYOUGOURYOU1, srTenkaTyogo));

        // 添加材⑧_部材在庫No2
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI8_BUZAIZAIKOLOTNO2, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI8_BUZAIZAIKOLOTNO2, srTenkaTyogo));

        // 添加材⑧_調合量2
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI8_TYOUGOURYOU2, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI8_TYOUGOURYOU2, srTenkaTyogo));

        // 添加材⑨_材料品名
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI9_ZAIRYOUMEI, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI9_ZAIRYOUMEI, srTenkaTyogo));

        // 添加材⑨_調合量規格
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI9_TYOUGOURYOUKIKAKU, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI9_TYOUGOURYOUKIKAKU, srTenkaTyogo));

        // 添加材⑨_部材在庫No1
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI9_BUZAIZAIKOLOTNO1, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI9_BUZAIZAIKOLOTNO1, srTenkaTyogo));

        // 添加材⑨_調合量1
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI9_TYOUGOURYOU1, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI9_TYOUGOURYOU1, srTenkaTyogo));

        // 添加材⑨_部材在庫No2
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI9_BUZAIZAIKOLOTNO2, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI9_BUZAIZAIKOLOTNO2, srTenkaTyogo));

        // 添加材⑨_調合量2
        this.setItemData(processData, GXHDO102B008Const.TENKAZAI9_TYOUGOURYOU2, getSrTenkaTyogoItemData(GXHDO102B008Const.TENKAZAI9_TYOUGOURYOU2, srTenkaTyogo));

        // 秤量終了日
        this.setItemData(processData, GXHDO102B008Const.HYOURYOUSYUURYOU_DAY, getSrTenkaTyogoItemData(GXHDO102B008Const.HYOURYOUSYUURYOU_DAY, srTenkaTyogo));

        // 秤量終了時間
        this.setItemData(processData, GXHDO102B008Const.HYOURYOUSYUURYOU_TIME, getSrTenkaTyogoItemData(GXHDO102B008Const.HYOURYOUSYUURYOU_TIME, srTenkaTyogo));

        // 担当者
        this.setItemData(processData, GXHDO102B008Const.TANTOUSYA, getSrTenkaTyogoItemData(GXHDO102B008Const.TANTOUSYA, srTenkaTyogo));

        // 確認者
        this.setItemData(processData, GXHDO102B008Const.KAKUNINSYA, getSrTenkaTyogoItemData(GXHDO102B008Const.KAKUNINSYA, srTenkaTyogo));

        // 備考1
        this.setItemData(processData, GXHDO102B008Const.BIKOU1, getSrTenkaTyogoItemData(GXHDO102B008Const.BIKOU1, srTenkaTyogo));

        // 備考2
        this.setItemData(processData, GXHDO102B008Const.BIKOU2, getSrTenkaTyogoItemData(GXHDO102B008Const.BIKOU2, srTenkaTyogo));

    }

    /**
     * 添加材ｽﾗﾘｰ作製・添加材調合の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return 添加材ｽﾗﾘｰ作製・添加材調合データ
     * @throws SQLException 例外エラー
     */
    private List<SrTenkaTyogo> getSrTenkaTyogoData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrTenkaTyogo(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSrTenkaTyogo(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
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
     * [添加材ｽﾗﾘｰ作製・添加材調合]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrTenkaTyogo> loadSrTenkaTyogo(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + "kojyo,lotno,edaban,tenkazaislurryhinmei,tenkazaislurrylotno,lotkubun,hyouryougouki,fuutaijyuuryou,"
                + "hyouryoukaisinichiji,tenkazai1_zairyoumei,tenkazai1_tyougouryoukikaku,tenkazai1_buzaizaikolotno1,"
                + "tenkazai1_tyougouryou1,tenkazai1_buzaizaikolotno2,tenkazai1_tyougouryou2,tenkazai2_zairyoumei,"
                + "tenkazai2_tyougouryoukikaku,tenkazai2_buzaizaikolotno1,tenkazai2_tyougouryou1,tenkazai2_buzaizaikolotno2,"
                + "tenkazai2_tyougouryou2,tenkazai3_zairyoumei,tenkazai3_tyougouryoukikaku,tenkazai3_buzaizaikolotno1,"
                + "tenkazai3_tyougouryou1,tenkazai3_buzaizaikolotno2,tenkazai3_tyougouryou2,tenkazai4_zairyoumei,"
                + "tenkazai4_tyougouryoukikaku,tenkazai4_buzaizaikolotno1,tenkazai4_tyougouryou1,tenkazai4_buzaizaikolotno2,"
                + "tenkazai4_tyougouryou2,tenkazai5_zairyoumei,tenkazai5_tyougouryoukikaku,tenkazai5_buzaizaikolotno1,"
                + "tenkazai5_tyougouryou1,tenkazai5_buzaizaikolotno2,tenkazai5_tyougouryou2,tenkazai6_zairyoumei,"
                + "tenkazai6_tyougouryoukikaku,tenkazai6_buzaizaikolotno1,tenkazai6_tyougouryou1,tenkazai6_buzaizaikolotno2,"
                + "tenkazai6_tyougouryou2,tenkazai7_zairyoumei,tenkazai7_tyougouryoukikaku,tenkazai7_buzaizaikolotno1,"
                + "tenkazai7_tyougouryou1,tenkazai7_buzaizaikolotno2,tenkazai7_tyougouryou2,tenkazai8_zairyoumei,"
                + "tenkazai8_tyougouryoukikaku,tenkazai8_buzaizaikolotno1,tenkazai8_tyougouryou1,tenkazai8_buzaizaikolotno2,"
                + "tenkazai8_tyougouryou2,tenkazai9_zairyoumei,tenkazai9_tyougouryoukikaku,tenkazai9_buzaizaikolotno1,"
                + "tenkazai9_tyougouryou1,tenkazai9_buzaizaikolotno2,tenkazai9_tyougouryou2,hyouryousyuuryounichiji,"
                + "tantousya,kakuninsya,bikou1,bikou2,torokunichiji,kosinnichiji,revision"
                + " FROM sr_tenka_tyogo "
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
        mapping.put("kojyo", "kojyo");                                                 // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                                 // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                               // 枝番
        mapping.put("tenkazaislurryhinmei", "tenkazaislurryhinmei");                   // 添加材ｽﾗﾘｰ品名
        mapping.put("tenkazaislurrylotno", "tenkazaislurrylotno");                     // 添加材ｽﾗﾘｰLotNo
        mapping.put("lotkubun", "lotkubun");                                           // ﾛｯﾄ区分
        mapping.put("hyouryougouki", "hyouryougouki");                                 // 秤量号機
        mapping.put("fuutaijyuuryou", "fuutaijyuuryou");                               // 風袋重量
        mapping.put("hyouryoukaisinichiji", "hyouryoukaisinichiji");                   // 秤量開始日時
        mapping.put("tenkazai1_zairyoumei", "tenkazai1_zairyoumei");                   // 添加材①_材料品名
        mapping.put("tenkazai1_tyougouryoukikaku", "tenkazai1_tyougouryoukikaku");     // 添加材①_調合量規格
        mapping.put("tenkazai1_buzaizaikolotno1", "tenkazai1_buzaizaikolotno1");       // 添加材①_部材在庫No1
        mapping.put("tenkazai1_tyougouryou1", "tenkazai1_tyougouryou1");               // 添加材①_調合量1
        mapping.put("tenkazai1_buzaizaikolotno2", "tenkazai1_buzaizaikolotno2");       // 添加材①_部材在庫No2
        mapping.put("tenkazai1_tyougouryou2", "tenkazai1_tyougouryou2");               // 添加材①_調合量2
        mapping.put("tenkazai2_zairyoumei", "tenkazai2_zairyoumei");                   // 添加材②_材料品名
        mapping.put("tenkazai2_tyougouryoukikaku", "tenkazai2_tyougouryoukikaku");     // 添加材②_調合量規格
        mapping.put("tenkazai2_buzaizaikolotno1", "tenkazai2_buzaizaikolotno1");       // 添加材②_部材在庫No1
        mapping.put("tenkazai2_tyougouryou1", "tenkazai2_tyougouryou1");               // 添加材②_調合量1
        mapping.put("tenkazai2_buzaizaikolotno2", "tenkazai2_buzaizaikolotno2");       // 添加材②_部材在庫No2
        mapping.put("tenkazai2_tyougouryou2", "tenkazai2_tyougouryou2");               // 添加材②_調合量2
        mapping.put("tenkazai3_zairyoumei", "tenkazai3_zairyoumei");                   // 添加材③_材料品名
        mapping.put("tenkazai3_tyougouryoukikaku", "tenkazai3_tyougouryoukikaku");     // 添加材③_調合量規格
        mapping.put("tenkazai3_buzaizaikolotno1", "tenkazai3_buzaizaikolotno1");       // 添加材③_部材在庫No1
        mapping.put("tenkazai3_tyougouryou1", "tenkazai3_tyougouryou1");               // 添加材③_調合量1
        mapping.put("tenkazai3_buzaizaikolotno2", "tenkazai3_buzaizaikolotno2");       // 添加材③_部材在庫No2
        mapping.put("tenkazai3_tyougouryou2", "tenkazai3_tyougouryou2");               // 添加材③_調合量2
        mapping.put("tenkazai4_zairyoumei", "tenkazai4_zairyoumei");                   // 添加材④_材料品名
        mapping.put("tenkazai4_tyougouryoukikaku", "tenkazai4_tyougouryoukikaku");     // 添加材④_調合量規格
        mapping.put("tenkazai4_buzaizaikolotno1", "tenkazai4_buzaizaikolotno1");       // 添加材④_部材在庫No1
        mapping.put("tenkazai4_tyougouryou1", "tenkazai4_tyougouryou1");               // 添加材④_調合量1
        mapping.put("tenkazai4_buzaizaikolotno2", "tenkazai4_buzaizaikolotno2");       // 添加材④_部材在庫No2
        mapping.put("tenkazai4_tyougouryou2", "tenkazai4_tyougouryou2");               // 添加材④_調合量2
        mapping.put("tenkazai5_zairyoumei", "tenkazai5_zairyoumei");                   // 添加材⑤_材料品名
        mapping.put("tenkazai5_tyougouryoukikaku", "tenkazai5_tyougouryoukikaku");     // 添加材⑤_調合量規格
        mapping.put("tenkazai5_buzaizaikolotno1", "tenkazai5_buzaizaikolotno1");       // 添加材⑤_部材在庫No1
        mapping.put("tenkazai5_tyougouryou1", "tenkazai5_tyougouryou1");               // 添加材⑤_調合量1
        mapping.put("tenkazai5_buzaizaikolotno2", "tenkazai5_buzaizaikolotno2");       // 添加材⑤_部材在庫No2
        mapping.put("tenkazai5_tyougouryou2", "tenkazai5_tyougouryou2");               // 添加材⑤_調合量2
        mapping.put("tenkazai6_zairyoumei", "tenkazai6_zairyoumei");                   // 添加材⑥_材料品名
        mapping.put("tenkazai6_tyougouryoukikaku", "tenkazai6_tyougouryoukikaku");     // 添加材⑥_調合量規格
        mapping.put("tenkazai6_buzaizaikolotno1", "tenkazai6_buzaizaikolotno1");       // 添加材⑥_部材在庫No1
        mapping.put("tenkazai6_tyougouryou1", "tenkazai6_tyougouryou1");               // 添加材⑥_調合量1
        mapping.put("tenkazai6_buzaizaikolotno2", "tenkazai6_buzaizaikolotno2");       // 添加材⑥_部材在庫No2
        mapping.put("tenkazai6_tyougouryou2", "tenkazai6_tyougouryou2");               // 添加材⑥_調合量2
        mapping.put("tenkazai7_zairyoumei", "tenkazai7_zairyoumei");                   // 添加材⑦_材料品名
        mapping.put("tenkazai7_tyougouryoukikaku", "tenkazai7_tyougouryoukikaku");     // 添加材⑦_調合量規格
        mapping.put("tenkazai7_buzaizaikolotno1", "tenkazai7_buzaizaikolotno1");       // 添加材⑦_部材在庫No1
        mapping.put("tenkazai7_tyougouryou1", "tenkazai7_tyougouryou1");               // 添加材⑦_調合量1
        mapping.put("tenkazai7_buzaizaikolotno2", "tenkazai7_buzaizaikolotno2");       // 添加材⑦_部材在庫No2
        mapping.put("tenkazai7_tyougouryou2", "tenkazai7_tyougouryou2");               // 添加材⑦_調合量2
        mapping.put("tenkazai8_zairyoumei", "tenkazai8_zairyoumei");                   // 添加材⑧_材料品名
        mapping.put("tenkazai8_tyougouryoukikaku", "tenkazai8_tyougouryoukikaku");     // 添加材⑧_調合量規格
        mapping.put("tenkazai8_buzaizaikolotno1", "tenkazai8_buzaizaikolotno1");       // 添加材⑧_部材在庫No1
        mapping.put("tenkazai8_tyougouryou1", "tenkazai8_tyougouryou1");               // 添加材⑧_調合量1
        mapping.put("tenkazai8_buzaizaikolotno2", "tenkazai8_buzaizaikolotno2");       // 添加材⑧_部材在庫No2
        mapping.put("tenkazai8_tyougouryou2", "tenkazai8_tyougouryou2");               // 添加材⑧_調合量2
        mapping.put("tenkazai9_zairyoumei", "tenkazai9_zairyoumei");                   // 添加材⑨_材料品名
        mapping.put("tenkazai9_tyougouryoukikaku", "tenkazai9_tyougouryoukikaku");     // 添加材⑨_調合量規格
        mapping.put("tenkazai9_buzaizaikolotno1", "tenkazai9_buzaizaikolotno1");       // 添加材⑨_部材在庫No1
        mapping.put("tenkazai9_tyougouryou1", "tenkazai9_tyougouryou1");               // 添加材⑨_調合量1
        mapping.put("tenkazai9_buzaizaikolotno2", "tenkazai9_buzaizaikolotno2");       // 添加材⑨_部材在庫No2
        mapping.put("tenkazai9_tyougouryou2", "tenkazai9_tyougouryou2");               // 添加材⑨_調合量2
        mapping.put("hyouryousyuuryounichiji", "hyouryousyuuryounichiji");             // 秤量終了日時
        mapping.put("tantousya", "tantousya");                                         // 担当者
        mapping.put("kakuninsya", "kakuninsya");                                       // 確認者
        mapping.put("bikou1", "bikou1");                                               // 備考1
        mapping.put("bikou2", "bikou2");                                               // 備考2
        mapping.put("torokunichiji", "torokunichiji");                                 // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                                   // 更新日時
        mapping.put("revision", "revision");                                           // revision

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrTenkaTyogo>> beanHandler = new BeanListHandler<>(SrTenkaTyogo.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [添加材ｽﾗﾘｰ作製・添加材調合_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrTenkaTyogo> loadTmpSrTenkaTyogo(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + "kojyo,lotno,edaban,tenkazaislurryhinmei,tenkazaislurrylotno,lotkubun,hyouryougouki,fuutaijyuuryou,"
                + "hyouryoukaisinichiji,tenkazai1_zairyoumei,tenkazai1_tyougouryoukikaku,tenkazai1_buzaizaikolotno1,"
                + "tenkazai1_tyougouryou1,tenkazai1_buzaizaikolotno2,tenkazai1_tyougouryou2,tenkazai2_zairyoumei,"
                + "tenkazai2_tyougouryoukikaku,tenkazai2_buzaizaikolotno1,tenkazai2_tyougouryou1,tenkazai2_buzaizaikolotno2,"
                + "tenkazai2_tyougouryou2,tenkazai3_zairyoumei,tenkazai3_tyougouryoukikaku,tenkazai3_buzaizaikolotno1,"
                + "tenkazai3_tyougouryou1,tenkazai3_buzaizaikolotno2,tenkazai3_tyougouryou2,tenkazai4_zairyoumei,"
                + "tenkazai4_tyougouryoukikaku,tenkazai4_buzaizaikolotno1,tenkazai4_tyougouryou1,tenkazai4_buzaizaikolotno2,"
                + "tenkazai4_tyougouryou2,tenkazai5_zairyoumei,tenkazai5_tyougouryoukikaku,tenkazai5_buzaizaikolotno1,"
                + "tenkazai5_tyougouryou1,tenkazai5_buzaizaikolotno2,tenkazai5_tyougouryou2,tenkazai6_zairyoumei,"
                + "tenkazai6_tyougouryoukikaku,tenkazai6_buzaizaikolotno1,tenkazai6_tyougouryou1,tenkazai6_buzaizaikolotno2,"
                + "tenkazai6_tyougouryou2,tenkazai7_zairyoumei,tenkazai7_tyougouryoukikaku,tenkazai7_buzaizaikolotno1,"
                + "tenkazai7_tyougouryou1,tenkazai7_buzaizaikolotno2,tenkazai7_tyougouryou2,tenkazai8_zairyoumei,"
                + "tenkazai8_tyougouryoukikaku,tenkazai8_buzaizaikolotno1,tenkazai8_tyougouryou1,tenkazai8_buzaizaikolotno2,"
                + "tenkazai8_tyougouryou2,tenkazai9_zairyoumei,tenkazai9_tyougouryoukikaku,tenkazai9_buzaizaikolotno1,"
                + "tenkazai9_tyougouryou1,tenkazai9_buzaizaikolotno2,tenkazai9_tyougouryou2,hyouryousyuuryounichiji,"
                + "tantousya,kakuninsya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,sakujyoflg"
                + " FROM tmp_sr_tenka_tyogo "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND sakujyoflg = ? ";

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
        mapping.put("kojyo", "kojyo");                                                 // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                                 // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                               // 枝番
        mapping.put("tenkazaislurryhinmei", "tenkazaislurryhinmei");                   // 添加材ｽﾗﾘｰ品名
        mapping.put("tenkazaislurrylotno", "tenkazaislurrylotno");                     // 添加材ｽﾗﾘｰLotNo
        mapping.put("lotkubun", "lotkubun");                                           // ﾛｯﾄ区分
        mapping.put("hyouryougouki", "hyouryougouki");                                 // 秤量号機
        mapping.put("fuutaijyuuryou", "fuutaijyuuryou");                               // 風袋重量
        mapping.put("hyouryoukaisinichiji", "hyouryoukaisinichiji");                   // 秤量開始日時
        mapping.put("tenkazai1_zairyoumei", "tenkazai1_zairyoumei");                   // 添加材①_材料品名
        mapping.put("tenkazai1_tyougouryoukikaku", "tenkazai1_tyougouryoukikaku");     // 添加材①_調合量規格
        mapping.put("tenkazai1_buzaizaikolotno1", "tenkazai1_buzaizaikolotno1");       // 添加材①_部材在庫No1
        mapping.put("tenkazai1_tyougouryou1", "tenkazai1_tyougouryou1");               // 添加材①_調合量1
        mapping.put("tenkazai1_buzaizaikolotno2", "tenkazai1_buzaizaikolotno2");       // 添加材①_部材在庫No2
        mapping.put("tenkazai1_tyougouryou2", "tenkazai1_tyougouryou2");               // 添加材①_調合量2
        mapping.put("tenkazai2_zairyoumei", "tenkazai2_zairyoumei");                   // 添加材②_材料品名
        mapping.put("tenkazai2_tyougouryoukikaku", "tenkazai2_tyougouryoukikaku");     // 添加材②_調合量規格
        mapping.put("tenkazai2_buzaizaikolotno1", "tenkazai2_buzaizaikolotno1");       // 添加材②_部材在庫No1
        mapping.put("tenkazai2_tyougouryou1", "tenkazai2_tyougouryou1");               // 添加材②_調合量1
        mapping.put("tenkazai2_buzaizaikolotno2", "tenkazai2_buzaizaikolotno2");       // 添加材②_部材在庫No2
        mapping.put("tenkazai2_tyougouryou2", "tenkazai2_tyougouryou2");               // 添加材②_調合量2
        mapping.put("tenkazai3_zairyoumei", "tenkazai3_zairyoumei");                   // 添加材③_材料品名
        mapping.put("tenkazai3_tyougouryoukikaku", "tenkazai3_tyougouryoukikaku");     // 添加材③_調合量規格
        mapping.put("tenkazai3_buzaizaikolotno1", "tenkazai3_buzaizaikolotno1");       // 添加材③_部材在庫No1
        mapping.put("tenkazai3_tyougouryou1", "tenkazai3_tyougouryou1");               // 添加材③_調合量1
        mapping.put("tenkazai3_buzaizaikolotno2", "tenkazai3_buzaizaikolotno2");       // 添加材③_部材在庫No2
        mapping.put("tenkazai3_tyougouryou2", "tenkazai3_tyougouryou2");               // 添加材③_調合量2
        mapping.put("tenkazai4_zairyoumei", "tenkazai4_zairyoumei");                   // 添加材④_材料品名
        mapping.put("tenkazai4_tyougouryoukikaku", "tenkazai4_tyougouryoukikaku");     // 添加材④_調合量規格
        mapping.put("tenkazai4_buzaizaikolotno1", "tenkazai4_buzaizaikolotno1");       // 添加材④_部材在庫No1
        mapping.put("tenkazai4_tyougouryou1", "tenkazai4_tyougouryou1");               // 添加材④_調合量1
        mapping.put("tenkazai4_buzaizaikolotno2", "tenkazai4_buzaizaikolotno2");       // 添加材④_部材在庫No2
        mapping.put("tenkazai4_tyougouryou2", "tenkazai4_tyougouryou2");               // 添加材④_調合量2
        mapping.put("tenkazai5_zairyoumei", "tenkazai5_zairyoumei");                   // 添加材⑤_材料品名
        mapping.put("tenkazai5_tyougouryoukikaku", "tenkazai5_tyougouryoukikaku");     // 添加材⑤_調合量規格
        mapping.put("tenkazai5_buzaizaikolotno1", "tenkazai5_buzaizaikolotno1");       // 添加材⑤_部材在庫No1
        mapping.put("tenkazai5_tyougouryou1", "tenkazai5_tyougouryou1");               // 添加材⑤_調合量1
        mapping.put("tenkazai5_buzaizaikolotno2", "tenkazai5_buzaizaikolotno2");       // 添加材⑤_部材在庫No2
        mapping.put("tenkazai5_tyougouryou2", "tenkazai5_tyougouryou2");               // 添加材⑤_調合量2
        mapping.put("tenkazai6_zairyoumei", "tenkazai6_zairyoumei");                   // 添加材⑥_材料品名
        mapping.put("tenkazai6_tyougouryoukikaku", "tenkazai6_tyougouryoukikaku");     // 添加材⑥_調合量規格
        mapping.put("tenkazai6_buzaizaikolotno1", "tenkazai6_buzaizaikolotno1");       // 添加材⑥_部材在庫No1
        mapping.put("tenkazai6_tyougouryou1", "tenkazai6_tyougouryou1");               // 添加材⑥_調合量1
        mapping.put("tenkazai6_buzaizaikolotno2", "tenkazai6_buzaizaikolotno2");       // 添加材⑥_部材在庫No2
        mapping.put("tenkazai6_tyougouryou2", "tenkazai6_tyougouryou2");               // 添加材⑥_調合量2
        mapping.put("tenkazai7_zairyoumei", "tenkazai7_zairyoumei");                   // 添加材⑦_材料品名
        mapping.put("tenkazai7_tyougouryoukikaku", "tenkazai7_tyougouryoukikaku");     // 添加材⑦_調合量規格
        mapping.put("tenkazai7_buzaizaikolotno1", "tenkazai7_buzaizaikolotno1");       // 添加材⑦_部材在庫No1
        mapping.put("tenkazai7_tyougouryou1", "tenkazai7_tyougouryou1");               // 添加材⑦_調合量1
        mapping.put("tenkazai7_buzaizaikolotno2", "tenkazai7_buzaizaikolotno2");       // 添加材⑦_部材在庫No2
        mapping.put("tenkazai7_tyougouryou2", "tenkazai7_tyougouryou2");               // 添加材⑦_調合量2
        mapping.put("tenkazai8_zairyoumei", "tenkazai8_zairyoumei");                   // 添加材⑧_材料品名
        mapping.put("tenkazai8_tyougouryoukikaku", "tenkazai8_tyougouryoukikaku");     // 添加材⑧_調合量規格
        mapping.put("tenkazai8_buzaizaikolotno1", "tenkazai8_buzaizaikolotno1");       // 添加材⑧_部材在庫No1
        mapping.put("tenkazai8_tyougouryou1", "tenkazai8_tyougouryou1");               // 添加材⑧_調合量1
        mapping.put("tenkazai8_buzaizaikolotno2", "tenkazai8_buzaizaikolotno2");       // 添加材⑧_部材在庫No2
        mapping.put("tenkazai8_tyougouryou2", "tenkazai8_tyougouryou2");               // 添加材⑧_調合量2
        mapping.put("tenkazai9_zairyoumei", "tenkazai9_zairyoumei");                   // 添加材⑨_材料品名
        mapping.put("tenkazai9_tyougouryoukikaku", "tenkazai9_tyougouryoukikaku");     // 添加材⑨_調合量規格
        mapping.put("tenkazai9_buzaizaikolotno1", "tenkazai9_buzaizaikolotno1");       // 添加材⑨_部材在庫No1
        mapping.put("tenkazai9_tyougouryou1", "tenkazai9_tyougouryou1");               // 添加材⑨_調合量1
        mapping.put("tenkazai9_buzaizaikolotno2", "tenkazai9_buzaizaikolotno2");       // 添加材⑨_部材在庫No2
        mapping.put("tenkazai9_tyougouryou2", "tenkazai9_tyougouryou2");               // 添加材⑨_調合量2
        mapping.put("hyouryousyuuryounichiji", "hyouryousyuuryounichiji");             // 秤量終了日時
        mapping.put("tantousya", "tantousya");                                         // 担当者
        mapping.put("kakuninsya", "kakuninsya");                                       // 確認者
        mapping.put("bikou1", "bikou1");                                               // 備考1
        mapping.put("bikou2", "bikou2");                                               // 備考2
        mapping.put("torokunichiji", "torokunichiji");                                 // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                                   // 更新日時
        mapping.put("revision", "revision");                                           // revision
        mapping.put("sakujyoflg", "sakujyoflg");                                       // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrTenkaTyogo>> beanHandler = new BeanListHandler<>(SrTenkaTyogo.class, rowProcessor);

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
     * @param srTenkaTyogo 添加材ｽﾗﾘｰ作製・添加材調合データ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrTenkaTyogo srTenkaTyogo) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srTenkaTyogo != null) {
            // 元データが存在する場合元データより取得
            return getSrTenkaTyogoItemData(itemId, srTenkaTyogo);
        } else {
            return null;
        }
    }

    /**
     * 項目データ(入力値)取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @param srGlasshyoryo 添加材ｽﾗﾘｰ作製・添加材調合データ
     * @return 入力値
     */
    private String getItemKikakuchi(List<FXHDD01> listData, String itemId, SrTenkaTyogo srTenkaTyogo) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return StringUtil.nullToBlank(selectData.get(0).getKikakuChi()).replace("【", "").replace("】", "");
        } else if (srTenkaTyogo != null) {
            // 元データが存在する場合元データより取得
            return getSrTenkaTyogoItemData(itemId, srTenkaTyogo);
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
     * 添加材ｽﾗﾘｰ作製・添加材調合_仮登録(tmp_sr_tenka_tyogo)登録処理
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
    private void insertTmpSrTenkaTyogo(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "INSERT INTO tmp_sr_tenka_tyogo ( "
                + "kojyo,lotno,edaban,tenkazaislurryhinmei,tenkazaislurrylotno,lotkubun,hyouryougouki,fuutaijyuuryou,"
                + "hyouryoukaisinichiji,tenkazai1_zairyoumei,tenkazai1_tyougouryoukikaku,tenkazai1_buzaizaikolotno1,"
                + "tenkazai1_tyougouryou1,tenkazai1_buzaizaikolotno2,tenkazai1_tyougouryou2,tenkazai2_zairyoumei,"
                + "tenkazai2_tyougouryoukikaku,tenkazai2_buzaizaikolotno1,tenkazai2_tyougouryou1,tenkazai2_buzaizaikolotno2,"
                + "tenkazai2_tyougouryou2,tenkazai3_zairyoumei,tenkazai3_tyougouryoukikaku,tenkazai3_buzaizaikolotno1,"
                + "tenkazai3_tyougouryou1,tenkazai3_buzaizaikolotno2,tenkazai3_tyougouryou2,tenkazai4_zairyoumei,"
                + "tenkazai4_tyougouryoukikaku,tenkazai4_buzaizaikolotno1,tenkazai4_tyougouryou1,tenkazai4_buzaizaikolotno2,"
                + "tenkazai4_tyougouryou2,tenkazai5_zairyoumei,tenkazai5_tyougouryoukikaku,tenkazai5_buzaizaikolotno1,"
                + "tenkazai5_tyougouryou1,tenkazai5_buzaizaikolotno2,tenkazai5_tyougouryou2,tenkazai6_zairyoumei,"
                + "tenkazai6_tyougouryoukikaku,tenkazai6_buzaizaikolotno1,tenkazai6_tyougouryou1,tenkazai6_buzaizaikolotno2,"
                + "tenkazai6_tyougouryou2,tenkazai7_zairyoumei,tenkazai7_tyougouryoukikaku,tenkazai7_buzaizaikolotno1,"
                + "tenkazai7_tyougouryou1,tenkazai7_buzaizaikolotno2,tenkazai7_tyougouryou2,tenkazai8_zairyoumei,"
                + "tenkazai8_tyougouryoukikaku,tenkazai8_buzaizaikolotno1,tenkazai8_tyougouryou1,tenkazai8_buzaizaikolotno2,"
                + "tenkazai8_tyougouryou2,tenkazai9_zairyoumei,tenkazai9_tyougouryoukikaku,tenkazai9_buzaizaikolotno1,"
                + "tenkazai9_tyougouryou1,tenkazai9_buzaizaikolotno2,tenkazai9_tyougouryou2,hyouryousyuuryounichiji,"
                + "tantousya,kakuninsya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,sakujyoflg "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrTenkaTyogo(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, processData, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 添加材ｽﾗﾘｰ作製・添加材調合_仮登録(tmp_sr_tenka_tyogo)更新処理
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
    private void updateTmpSrTenkaTyogo(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "UPDATE tmp_sr_tenka_tyogo SET "
                + "tenkazaislurryhinmei = ?,tenkazaislurrylotno = ?,lotkubun = ?,hyouryougouki = ?,fuutaijyuuryou = ?,hyouryoukaisinichiji = ?,"
                + "tenkazai1_zairyoumei = ?,tenkazai1_tyougouryoukikaku = ?,tenkazai1_buzaizaikolotno1 = ?,tenkazai1_tyougouryou1 = ?,"
                + "tenkazai1_buzaizaikolotno2 = ?,tenkazai1_tyougouryou2 = ?,tenkazai2_zairyoumei = ?,tenkazai2_tyougouryoukikaku = ?,"
                + "tenkazai2_buzaizaikolotno1 = ?,tenkazai2_tyougouryou1 = ?,tenkazai2_buzaizaikolotno2 = ?,tenkazai2_tyougouryou2 = ?,"
                + "tenkazai3_zairyoumei = ?,tenkazai3_tyougouryoukikaku = ?,tenkazai3_buzaizaikolotno1 = ?,tenkazai3_tyougouryou1 = ?,"
                + "tenkazai3_buzaizaikolotno2 = ?,tenkazai3_tyougouryou2 = ?,tenkazai4_zairyoumei = ?,tenkazai4_tyougouryoukikaku = ?,"
                + "tenkazai4_buzaizaikolotno1 = ?,tenkazai4_tyougouryou1 = ?,tenkazai4_buzaizaikolotno2 = ?,tenkazai4_tyougouryou2 = ?,"
                + "tenkazai5_zairyoumei = ?,tenkazai5_tyougouryoukikaku = ?,tenkazai5_buzaizaikolotno1 = ?,tenkazai5_tyougouryou1 = ?,"
                + "tenkazai5_buzaizaikolotno2 = ?,tenkazai5_tyougouryou2 = ?,tenkazai6_zairyoumei = ?,tenkazai6_tyougouryoukikaku = ?,"
                + "tenkazai6_buzaizaikolotno1 = ?,tenkazai6_tyougouryou1 = ?,tenkazai6_buzaizaikolotno2 = ?,tenkazai6_tyougouryou2 = ?,"
                + "tenkazai7_zairyoumei = ?,tenkazai7_tyougouryoukikaku = ?,tenkazai7_buzaizaikolotno1 = ?,tenkazai7_tyougouryou1 = ?,"
                + "tenkazai7_buzaizaikolotno2 = ?,tenkazai7_tyougouryou2 = ?,tenkazai8_zairyoumei = ?,tenkazai8_tyougouryoukikaku = ?,"
                + "tenkazai8_buzaizaikolotno1 = ?,tenkazai8_tyougouryou1 = ?,tenkazai8_buzaizaikolotno2 = ?,tenkazai8_tyougouryou2 = ?,"
                + "tenkazai9_zairyoumei = ?,tenkazai9_tyougouryoukikaku = ?,tenkazai9_buzaizaikolotno1 = ?,tenkazai9_tyougouryou1 = ?,"
                + "tenkazai9_buzaizaikolotno2 = ?,tenkazai9_tyougouryou2 = ?,hyouryousyuuryounichiji = ?,tantousya = ?,kakuninsya = ?,"
                + "bikou1 = ?,bikou2 = ?,kosinnichiji = ?,revision = ?,sakujyoflg = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrTenkaTyogo> srTenkaTyogoList = getSrTenkaTyogoData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrTenkaTyogo srTenkaTyogo = null;
        if (!srTenkaTyogoList.isEmpty()) {
            srTenkaTyogo = srTenkaTyogoList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrTenkaTyogo(false, newRev, 0, "", "", "", systemTime, processData, srTenkaTyogo);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 添加材ｽﾗﾘｰ作製・添加材調合_仮登録(tmp_sr_tenka_tyogo)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrTenkaTyogo(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sr_tenka_tyogo "
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
     * 添加材ｽﾗﾘｰ作製・添加材調合_仮登録(tmp_sr_tenka_tyogo)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srTenkaTyogo 添加材ｽﾗﾘｰ作製・添加材調合データ
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrTenkaTyogo(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, String systemTime, ProcessData processData, SrTenkaTyogo srTenkaTyogo) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // 秤量開始日時
        String hyouryoukaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B008Const.HYOURYOUKAISI_TIME, srTenkaTyogo));
        // 秤量終了日時
        String hyouryousyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B008Const.HYOURYOUSYUURYOU_TIME, srTenkaTyogo));

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAISLURRYHINMEI, srTenkaTyogo))); // 添加材ｽﾗﾘｰ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAISLURRYLOTNO, srTenkaTyogo))); // 添加材ｽﾗﾘｰLotNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.LOTKUBUN, srTenkaTyogo))); // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.HYOURYOUGOUKI, srTenkaTyogo))); // 秤量号機
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.FUUTAIJYUURYOU, srTenkaTyogo))); // 風袋重量
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.HYOURYOUKAISI_DAY, srTenkaTyogo),
                "".equals(hyouryoukaisiTime) ? "0000" : hyouryoukaisiTime)); // 秤量開始日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI1_ZAIRYOUMEI, srTenkaTyogo))); // 添加材①_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI1_TYOUGOURYOUKIKAKU, srTenkaTyogo))); // 添加材①_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI1_BUZAIZAIKOLOTNO1, srTenkaTyogo))); // 添加材①_部材在庫No1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI1_TYOUGOURYOU1, srTenkaTyogo))); // 添加材①_調合量1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI1_BUZAIZAIKOLOTNO2, srTenkaTyogo))); // 添加材①_部材在庫No2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI1_TYOUGOURYOU2, srTenkaTyogo))); // 添加材①_調合量2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI2_ZAIRYOUMEI, srTenkaTyogo))); // 添加材②_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI2_TYOUGOURYOUKIKAKU, srTenkaTyogo))); // 添加材②_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI2_BUZAIZAIKOLOTNO1, srTenkaTyogo))); // 添加材②_部材在庫No1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI2_TYOUGOURYOU1, srTenkaTyogo))); // 添加材②_調合量1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI2_BUZAIZAIKOLOTNO2, srTenkaTyogo))); // 添加材②_部材在庫No2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI2_TYOUGOURYOU2, srTenkaTyogo))); // 添加材②_調合量2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI3_ZAIRYOUMEI, srTenkaTyogo))); // 添加材③_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI3_TYOUGOURYOUKIKAKU, srTenkaTyogo))); // 添加材③_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI3_BUZAIZAIKOLOTNO1, srTenkaTyogo))); // 添加材③_部材在庫No1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI3_TYOUGOURYOU1, srTenkaTyogo))); // 添加材③_調合量1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI3_BUZAIZAIKOLOTNO2, srTenkaTyogo))); // 添加材③_部材在庫No2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI3_TYOUGOURYOU2, srTenkaTyogo))); // 添加材③_調合量2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI4_ZAIRYOUMEI, srTenkaTyogo))); // 添加材④_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI4_TYOUGOURYOUKIKAKU, srTenkaTyogo))); // 添加材④_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI4_BUZAIZAIKOLOTNO1, srTenkaTyogo))); // 添加材④_部材在庫No1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI4_TYOUGOURYOU1, srTenkaTyogo))); // 添加材④_調合量1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI4_BUZAIZAIKOLOTNO2, srTenkaTyogo))); // 添加材④_部材在庫No2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI4_TYOUGOURYOU2, srTenkaTyogo))); // 添加材④_調合量2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI5_ZAIRYOUMEI, srTenkaTyogo))); // 添加材⑤_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI5_TYOUGOURYOUKIKAKU, srTenkaTyogo))); // 添加材⑤_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI5_BUZAIZAIKOLOTNO1, srTenkaTyogo))); // 添加材⑤_部材在庫No1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI5_TYOUGOURYOU1, srTenkaTyogo))); // 添加材⑤_調合量1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI5_BUZAIZAIKOLOTNO2, srTenkaTyogo))); // 添加材⑤_部材在庫No2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI5_TYOUGOURYOU2, srTenkaTyogo))); // 添加材⑤_調合量2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI6_ZAIRYOUMEI, srTenkaTyogo))); // 添加材⑥_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI6_TYOUGOURYOUKIKAKU, srTenkaTyogo))); // 添加材⑥_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI6_BUZAIZAIKOLOTNO1, srTenkaTyogo))); // 添加材⑥_部材在庫No1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI6_TYOUGOURYOU1, srTenkaTyogo))); // 添加材⑥_調合量1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI6_BUZAIZAIKOLOTNO2, srTenkaTyogo))); // 添加材⑥_部材在庫No2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI6_TYOUGOURYOU2, srTenkaTyogo))); // 添加材⑥_調合量2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI7_ZAIRYOUMEI, srTenkaTyogo))); // 添加材⑦_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI7_TYOUGOURYOUKIKAKU, srTenkaTyogo))); // 添加材⑦_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI7_BUZAIZAIKOLOTNO1, srTenkaTyogo))); // 添加材⑦_部材在庫No1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI7_TYOUGOURYOU1, srTenkaTyogo))); // 添加材⑦_調合量1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI7_BUZAIZAIKOLOTNO2, srTenkaTyogo))); // 添加材⑦_部材在庫No2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI7_TYOUGOURYOU2, srTenkaTyogo))); // 添加材⑦_調合量2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI8_ZAIRYOUMEI, srTenkaTyogo))); // 添加材⑧_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI8_TYOUGOURYOUKIKAKU, srTenkaTyogo))); // 添加材⑧_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI8_BUZAIZAIKOLOTNO1, srTenkaTyogo))); // 添加材⑧_部材在庫No1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI8_TYOUGOURYOU1, srTenkaTyogo))); // 添加材⑧_調合量1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI8_BUZAIZAIKOLOTNO2, srTenkaTyogo))); // 添加材⑧_部材在庫No2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI8_TYOUGOURYOU2, srTenkaTyogo))); // 添加材⑧_調合量2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI9_ZAIRYOUMEI, srTenkaTyogo))); // 添加材⑨_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI9_TYOUGOURYOUKIKAKU, srTenkaTyogo))); // 添加材⑨_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI9_BUZAIZAIKOLOTNO1, srTenkaTyogo))); // 添加材⑨_部材在庫No1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI9_TYOUGOURYOU1, srTenkaTyogo))); // 添加材⑨_調合量1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI9_BUZAIZAIKOLOTNO2, srTenkaTyogo))); // 添加材⑨_部材在庫No2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TENKAZAI9_TYOUGOURYOU2, srTenkaTyogo))); // 添加材⑨_調合量2
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.HYOURYOUSYUURYOU_DAY, srTenkaTyogo),
                "".equals(hyouryousyuuryouTime) ? "0000" : hyouryousyuuryouTime)); // 秤量終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.TANTOUSYA, srTenkaTyogo))); // 担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.KAKUNINSYA, srTenkaTyogo))); // 確認者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.BIKOU1, srTenkaTyogo))); // 備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B008Const.BIKOU2, srTenkaTyogo))); // 備考2

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
     * 添加材ｽﾗﾘｰ作製・添加材調合(sr_tenka_tyogo)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param tmpSrTenkaTyogo 仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrTenkaTyogo(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData, SrTenkaTyogo tmpSrTenkaTyogo) throws SQLException {

        String sql = "INSERT INTO sr_tenka_tyogo ( "
                + "kojyo,lotno,edaban,tenkazaislurryhinmei,tenkazaislurrylotno,lotkubun,hyouryougouki,fuutaijyuuryou,"
                + "hyouryoukaisinichiji,tenkazai1_zairyoumei,tenkazai1_tyougouryoukikaku,tenkazai1_buzaizaikolotno1,"
                + "tenkazai1_tyougouryou1,tenkazai1_buzaizaikolotno2,tenkazai1_tyougouryou2,tenkazai2_zairyoumei,"
                + "tenkazai2_tyougouryoukikaku,tenkazai2_buzaizaikolotno1,tenkazai2_tyougouryou1,tenkazai2_buzaizaikolotno2,"
                + "tenkazai2_tyougouryou2,tenkazai3_zairyoumei,tenkazai3_tyougouryoukikaku,tenkazai3_buzaizaikolotno1,"
                + "tenkazai3_tyougouryou1,tenkazai3_buzaizaikolotno2,tenkazai3_tyougouryou2,tenkazai4_zairyoumei,"
                + "tenkazai4_tyougouryoukikaku,tenkazai4_buzaizaikolotno1,tenkazai4_tyougouryou1,tenkazai4_buzaizaikolotno2,"
                + "tenkazai4_tyougouryou2,tenkazai5_zairyoumei,tenkazai5_tyougouryoukikaku,tenkazai5_buzaizaikolotno1,"
                + "tenkazai5_tyougouryou1,tenkazai5_buzaizaikolotno2,tenkazai5_tyougouryou2,tenkazai6_zairyoumei,"
                + "tenkazai6_tyougouryoukikaku,tenkazai6_buzaizaikolotno1,tenkazai6_tyougouryou1,tenkazai6_buzaizaikolotno2,"
                + "tenkazai6_tyougouryou2,tenkazai7_zairyoumei,tenkazai7_tyougouryoukikaku,tenkazai7_buzaizaikolotno1,"
                + "tenkazai7_tyougouryou1,tenkazai7_buzaizaikolotno2,tenkazai7_tyougouryou2,tenkazai8_zairyoumei,"
                + "tenkazai8_tyougouryoukikaku,tenkazai8_buzaizaikolotno1,tenkazai8_tyougouryou1,tenkazai8_buzaizaikolotno2,"
                + "tenkazai8_tyougouryou2,tenkazai9_zairyoumei,tenkazai9_tyougouryoukikaku,tenkazai9_buzaizaikolotno1,"
                + "tenkazai9_tyougouryou1,tenkazai9_buzaizaikolotno2,tenkazai9_tyougouryou2,hyouryousyuuryounichiji,"
                + "tantousya,kakuninsya,bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrTenkaTyogo(true, newRev, kojyo, lotNo, edaban, systemTime, processData, tmpSrTenkaTyogo);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 添加材ｽﾗﾘｰ作製・添加材調合(sr_tenka_tyogo)更新処理
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
    private void updateSrTenkaTyogo(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {
        String sql = "UPDATE sr_tenka_tyogo SET "
                + "tenkazaislurryhinmei = ?,tenkazaislurrylotno = ?,lotkubun = ?,hyouryougouki = ?,fuutaijyuuryou = ?,hyouryoukaisinichiji = ?,"
                + "tenkazai1_zairyoumei = ?,tenkazai1_tyougouryoukikaku = ?,tenkazai1_buzaizaikolotno1 = ?,tenkazai1_tyougouryou1 = ?,"
                + "tenkazai1_buzaizaikolotno2 = ?,tenkazai1_tyougouryou2 = ?,tenkazai2_zairyoumei = ?,tenkazai2_tyougouryoukikaku = ?,"
                + "tenkazai2_buzaizaikolotno1 = ?,tenkazai2_tyougouryou1 = ?,tenkazai2_buzaizaikolotno2 = ?,tenkazai2_tyougouryou2 = ?,"
                + "tenkazai3_zairyoumei = ?,tenkazai3_tyougouryoukikaku = ?,tenkazai3_buzaizaikolotno1 = ?,tenkazai3_tyougouryou1 = ?,"
                + "tenkazai3_buzaizaikolotno2 = ?,tenkazai3_tyougouryou2 = ?,tenkazai4_zairyoumei = ?,tenkazai4_tyougouryoukikaku = ?,"
                + "tenkazai4_buzaizaikolotno1 = ?,tenkazai4_tyougouryou1 = ?,tenkazai4_buzaizaikolotno2 = ?,tenkazai4_tyougouryou2 = ?,"
                + "tenkazai5_zairyoumei = ?,tenkazai5_tyougouryoukikaku = ?,tenkazai5_buzaizaikolotno1 = ?,tenkazai5_tyougouryou1 = ?,"
                + "tenkazai5_buzaizaikolotno2 = ?,tenkazai5_tyougouryou2 = ?,tenkazai6_zairyoumei = ?,tenkazai6_tyougouryoukikaku = ?,"
                + "tenkazai6_buzaizaikolotno1 = ?,tenkazai6_tyougouryou1 = ?,tenkazai6_buzaizaikolotno2 = ?,tenkazai6_tyougouryou2 = ?,"
                + "tenkazai7_zairyoumei = ?,tenkazai7_tyougouryoukikaku = ?,tenkazai7_buzaizaikolotno1 = ?,tenkazai7_tyougouryou1 = ?,"
                + "tenkazai7_buzaizaikolotno2 = ?,tenkazai7_tyougouryou2 = ?,tenkazai8_zairyoumei = ?,tenkazai8_tyougouryoukikaku = ?,"
                + "tenkazai8_buzaizaikolotno1 = ?,tenkazai8_tyougouryou1 = ?,tenkazai8_buzaizaikolotno2 = ?,tenkazai8_tyougouryou2 = ?,"
                + "tenkazai9_zairyoumei = ?,tenkazai9_tyougouryoukikaku = ?,tenkazai9_buzaizaikolotno1 = ?,tenkazai9_tyougouryou1 = ?,"
                + "tenkazai9_buzaizaikolotno2 = ?,tenkazai9_tyougouryou2 = ?,hyouryousyuuryounichiji = ?,tantousya = ?,kakuninsya = ?,"
                + "bikou1 = ?,bikou2 = ?,kosinnichiji = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrTenkaTyogo> srTenkaTyogoList = getSrTenkaTyogoData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrTenkaTyogo srTenkaTyogo = null;
        if (!srTenkaTyogoList.isEmpty()) {
            srTenkaTyogo = srTenkaTyogoList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrTenkaTyogo(false, newRev, "", "", "", systemTime, processData, srTenkaTyogo);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 添加材ｽﾗﾘｰ作製・添加材調合(sr_tenka_tyogo)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srTenkaTyogo 添加材ｽﾗﾘｰ作製・添加材調合データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrTenkaTyogo(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            String systemTime, ProcessData processData, SrTenkaTyogo srTenkaTyogo) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // 秤量開始日時
        String hyouryoukaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B008Const.HYOURYOUKAISI_TIME, srTenkaTyogo));
        // 秤量終了日時
        String hyouryousyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B008Const.HYOURYOUSYUURYOU_TIME, srTenkaTyogo));

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }

        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAISLURRYHINMEI, srTenkaTyogo))); // 添加材ｽﾗﾘｰ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAISLURRYLOTNO, srTenkaTyogo))); // 添加材ｽﾗﾘｰLotNo
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.LOTKUBUN, srTenkaTyogo))); // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.HYOURYOUGOUKI, srTenkaTyogo))); // 秤量号機
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B008Const.FUUTAIJYUURYOU, srTenkaTyogo))); // 風袋重量
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B008Const.HYOURYOUKAISI_DAY, srTenkaTyogo),
                "".equals(hyouryoukaisiTime) ? "0000" : hyouryoukaisiTime)); // 秤量開始日時
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI1_ZAIRYOUMEI, srTenkaTyogo))); // 添加材①_材料品名
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI1_TYOUGOURYOUKIKAKU, srTenkaTyogo))); // 添加材①_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI1_BUZAIZAIKOLOTNO1, srTenkaTyogo))); // 添加材①_部材在庫No1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI1_TYOUGOURYOU1, srTenkaTyogo))); // 添加材①_調合量1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI1_BUZAIZAIKOLOTNO2, srTenkaTyogo))); // 添加材①_部材在庫No2
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI1_TYOUGOURYOU2, srTenkaTyogo))); // 添加材①_調合量2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI2_ZAIRYOUMEI, srTenkaTyogo))); // 添加材②_材料品名
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI2_TYOUGOURYOUKIKAKU, srTenkaTyogo))); // 添加材②_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI2_BUZAIZAIKOLOTNO1, srTenkaTyogo))); // 添加材②_部材在庫No1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI2_TYOUGOURYOU1, srTenkaTyogo))); // 添加材②_調合量1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI2_BUZAIZAIKOLOTNO2, srTenkaTyogo))); // 添加材②_部材在庫No2
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI2_TYOUGOURYOU2, srTenkaTyogo))); // 添加材②_調合量2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI3_ZAIRYOUMEI, srTenkaTyogo))); // 添加材③_材料品名
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI3_TYOUGOURYOUKIKAKU, srTenkaTyogo))); // 添加材③_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI3_BUZAIZAIKOLOTNO1, srTenkaTyogo))); // 添加材③_部材在庫No1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI3_TYOUGOURYOU1, srTenkaTyogo))); // 添加材③_調合量1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI3_BUZAIZAIKOLOTNO2, srTenkaTyogo))); // 添加材③_部材在庫No2
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI3_TYOUGOURYOU2, srTenkaTyogo))); // 添加材③_調合量2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI4_ZAIRYOUMEI, srTenkaTyogo))); // 添加材④_材料品名
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI4_TYOUGOURYOUKIKAKU, srTenkaTyogo))); // 添加材④_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI4_BUZAIZAIKOLOTNO1, srTenkaTyogo))); // 添加材④_部材在庫No1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI4_TYOUGOURYOU1, srTenkaTyogo))); // 添加材④_調合量1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI4_BUZAIZAIKOLOTNO2, srTenkaTyogo))); // 添加材④_部材在庫No2
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI4_TYOUGOURYOU2, srTenkaTyogo))); // 添加材④_調合量2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI5_ZAIRYOUMEI, srTenkaTyogo))); // 添加材⑤_材料品名
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI5_TYOUGOURYOUKIKAKU, srTenkaTyogo))); // 添加材⑤_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI5_BUZAIZAIKOLOTNO1, srTenkaTyogo))); // 添加材⑤_部材在庫No1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI5_TYOUGOURYOU1, srTenkaTyogo))); // 添加材⑤_調合量1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI5_BUZAIZAIKOLOTNO2, srTenkaTyogo))); // 添加材⑤_部材在庫No2
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI5_TYOUGOURYOU2, srTenkaTyogo))); // 添加材⑤_調合量2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI6_ZAIRYOUMEI, srTenkaTyogo))); // 添加材⑥_材料品名
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI6_TYOUGOURYOUKIKAKU, srTenkaTyogo))); // 添加材⑥_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI6_BUZAIZAIKOLOTNO1, srTenkaTyogo))); // 添加材⑥_部材在庫No1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI6_TYOUGOURYOU1, srTenkaTyogo))); // 添加材⑥_調合量1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI6_BUZAIZAIKOLOTNO2, srTenkaTyogo))); // 添加材⑥_部材在庫No2
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI6_TYOUGOURYOU2, srTenkaTyogo))); // 添加材⑥_調合量2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI7_ZAIRYOUMEI, srTenkaTyogo))); // 添加材⑦_材料品名
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI7_TYOUGOURYOUKIKAKU, srTenkaTyogo))); // 添加材⑦_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI7_BUZAIZAIKOLOTNO1, srTenkaTyogo))); // 添加材⑦_部材在庫No1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI7_TYOUGOURYOU1, srTenkaTyogo))); // 添加材⑦_調合量1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI7_BUZAIZAIKOLOTNO2, srTenkaTyogo))); // 添加材⑦_部材在庫No2
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI7_TYOUGOURYOU2, srTenkaTyogo))); // 添加材⑦_調合量2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI8_ZAIRYOUMEI, srTenkaTyogo))); // 添加材⑧_材料品名
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI8_TYOUGOURYOUKIKAKU, srTenkaTyogo))); // 添加材⑧_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI8_BUZAIZAIKOLOTNO1, srTenkaTyogo))); // 添加材⑧_部材在庫No1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI8_TYOUGOURYOU1, srTenkaTyogo))); // 添加材⑧_調合量1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI8_BUZAIZAIKOLOTNO2, srTenkaTyogo))); // 添加材⑧_部材在庫No2
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI8_TYOUGOURYOU2, srTenkaTyogo))); // 添加材⑧_調合量2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI9_ZAIRYOUMEI, srTenkaTyogo))); // 添加材⑨_材料品名
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B008Const.TENKAZAI9_TYOUGOURYOUKIKAKU, srTenkaTyogo))); // 添加材⑨_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI9_BUZAIZAIKOLOTNO1, srTenkaTyogo))); // 添加材⑨_部材在庫No1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI9_TYOUGOURYOU1, srTenkaTyogo))); // 添加材⑨_調合量1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI9_BUZAIZAIKOLOTNO2, srTenkaTyogo))); // 添加材⑨_部材在庫No2
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TENKAZAI9_TYOUGOURYOU2, srTenkaTyogo))); // 添加材⑨_調合量2
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B008Const.HYOURYOUSYUURYOU_DAY, srTenkaTyogo),
                "".equals(hyouryousyuuryouTime) ? "0000" : hyouryousyuuryouTime)); // 秤量終了日時
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.TANTOUSYA, srTenkaTyogo))); // 担当者
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.KAKUNINSYA, srTenkaTyogo))); // 確認者
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.BIKOU1, srTenkaTyogo))); // 備考1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B008Const.BIKOU2, srTenkaTyogo))); // 備考2

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
     * 添加材ｽﾗﾘｰ作製・添加材調合(sr_tenka_tyogo)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSrTenkaTyogo(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sr_tenka_tyogo "
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
     * [添加材ｽﾗﾘｰ作製・添加材調合_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
        String sql = "SELECT MAX(sakujyoflg) AS deleteflag "
                + "FROM tmp_sr_tenka_tyogo "
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
     * @param srTenkaTyogo 添加材ｽﾗﾘｰ作製・添加材調合データ
     * @return DB値
     */
    private String getSrTenkaTyogoItemData(String itemId, SrTenkaTyogo srTenkaTyogo) {
        switch (itemId) {
            // 添加材ｽﾗﾘｰ品名
            case GXHDO102B008Const.TENKAZAISLURRYHINMEI:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazaislurryhinmei());

            // 添加材ｽﾗﾘｰLotNo
            case GXHDO102B008Const.TENKAZAISLURRYLOTNO:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazaislurrylotno());

            // ﾛｯﾄ区分
            case GXHDO102B008Const.LOTKUBUN:
                return StringUtil.nullToBlank(srTenkaTyogo.getLotkubun());

            // 秤量号機
            case GXHDO102B008Const.HYOURYOUGOUKI:
                return StringUtil.nullToBlank(srTenkaTyogo.getHyouryougouki());

            // 風袋重量
            case GXHDO102B008Const.FUUTAIJYUURYOU:
                return StringUtil.nullToBlank(srTenkaTyogo.getFuutaijyuuryou());

            // 秤量開始日
            case GXHDO102B008Const.HYOURYOUKAISI_DAY:
                return DateUtil.formattedTimestamp(srTenkaTyogo.getHyouryoukaisinichiji(), "yyMMdd");

            // 秤量開始時間
            case GXHDO102B008Const.HYOURYOUKAISI_TIME:
                return DateUtil.formattedTimestamp(srTenkaTyogo.getHyouryoukaisinichiji(), "HHmm");

            // 添加材①_材料品名
            case GXHDO102B008Const.TENKAZAI1_ZAIRYOUMEI:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai1_zairyoumei());

            // 添加材①_調合量規格
            case GXHDO102B008Const.TENKAZAI1_TYOUGOURYOUKIKAKU:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai1_tyougouryoukikaku());

            // 添加材①_部材在庫No1
            case GXHDO102B008Const.TENKAZAI1_BUZAIZAIKOLOTNO1:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai1_buzaizaikolotno1());

            // 添加材①_調合量1
            case GXHDO102B008Const.TENKAZAI1_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai1_tyougouryou1());

            // 添加材①_部材在庫No2
            case GXHDO102B008Const.TENKAZAI1_BUZAIZAIKOLOTNO2:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai1_buzaizaikolotno2());

            // 添加材①_調合量2
            case GXHDO102B008Const.TENKAZAI1_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai1_tyougouryou2());

            // 添加材②_材料品名
            case GXHDO102B008Const.TENKAZAI2_ZAIRYOUMEI:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai2_zairyoumei());

            // 添加材②_調合量規格
            case GXHDO102B008Const.TENKAZAI2_TYOUGOURYOUKIKAKU:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai2_tyougouryoukikaku());

            // 添加材②_部材在庫No1
            case GXHDO102B008Const.TENKAZAI2_BUZAIZAIKOLOTNO1:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai2_buzaizaikolotno1());

            // 添加材②_調合量1
            case GXHDO102B008Const.TENKAZAI2_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai2_tyougouryou1());

            // 添加材②_部材在庫No2
            case GXHDO102B008Const.TENKAZAI2_BUZAIZAIKOLOTNO2:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai2_buzaizaikolotno2());

            // 添加材②_調合量2
            case GXHDO102B008Const.TENKAZAI2_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai2_tyougouryou2());

            // 添加材③_材料品名
            case GXHDO102B008Const.TENKAZAI3_ZAIRYOUMEI:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai3_zairyoumei());

            // 添加材③_調合量規格
            case GXHDO102B008Const.TENKAZAI3_TYOUGOURYOUKIKAKU:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai3_tyougouryoukikaku());

            // 添加材③_部材在庫No1
            case GXHDO102B008Const.TENKAZAI3_BUZAIZAIKOLOTNO1:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai3_buzaizaikolotno1());

            // 添加材③_調合量1
            case GXHDO102B008Const.TENKAZAI3_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai3_tyougouryou1());

            // 添加材③_部材在庫No2
            case GXHDO102B008Const.TENKAZAI3_BUZAIZAIKOLOTNO2:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai3_buzaizaikolotno2());

            // 添加材③_調合量2
            case GXHDO102B008Const.TENKAZAI3_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai3_tyougouryou2());

            // 添加材④_材料品名
            case GXHDO102B008Const.TENKAZAI4_ZAIRYOUMEI:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai4_zairyoumei());

            // 添加材④_調合量規格
            case GXHDO102B008Const.TENKAZAI4_TYOUGOURYOUKIKAKU:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai4_tyougouryoukikaku());

            // 添加材④_部材在庫No1
            case GXHDO102B008Const.TENKAZAI4_BUZAIZAIKOLOTNO1:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai4_buzaizaikolotno1());

            // 添加材④_調合量1
            case GXHDO102B008Const.TENKAZAI4_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai4_tyougouryou1());

            // 添加材④_部材在庫No2
            case GXHDO102B008Const.TENKAZAI4_BUZAIZAIKOLOTNO2:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai4_buzaizaikolotno2());

            // 添加材④_調合量2
            case GXHDO102B008Const.TENKAZAI4_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai4_tyougouryou2());

            // 添加材⑤_材料品名
            case GXHDO102B008Const.TENKAZAI5_ZAIRYOUMEI:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai5_zairyoumei());

            // 添加材⑤_調合量規格
            case GXHDO102B008Const.TENKAZAI5_TYOUGOURYOUKIKAKU:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai5_tyougouryoukikaku());

            // 添加材⑤_部材在庫No1
            case GXHDO102B008Const.TENKAZAI5_BUZAIZAIKOLOTNO1:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai5_buzaizaikolotno1());

            // 添加材⑤_調合量1
            case GXHDO102B008Const.TENKAZAI5_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai5_tyougouryou1());

            // 添加材⑤_部材在庫No2
            case GXHDO102B008Const.TENKAZAI5_BUZAIZAIKOLOTNO2:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai5_buzaizaikolotno2());

            // 添加材⑤_調合量2
            case GXHDO102B008Const.TENKAZAI5_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai5_tyougouryou2());

            // 添加材⑥_材料品名
            case GXHDO102B008Const.TENKAZAI6_ZAIRYOUMEI:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai6_zairyoumei());

            // 添加材⑥_調合量規格
            case GXHDO102B008Const.TENKAZAI6_TYOUGOURYOUKIKAKU:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai6_tyougouryoukikaku());

            // 添加材⑥_部材在庫No1
            case GXHDO102B008Const.TENKAZAI6_BUZAIZAIKOLOTNO1:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai6_buzaizaikolotno1());

            // 添加材⑥_調合量1
            case GXHDO102B008Const.TENKAZAI6_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai6_tyougouryou1());

            // 添加材⑥_部材在庫No2
            case GXHDO102B008Const.TENKAZAI6_BUZAIZAIKOLOTNO2:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai6_buzaizaikolotno2());

            // 添加材⑥_調合量2
            case GXHDO102B008Const.TENKAZAI6_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai6_tyougouryou2());

            // 添加材⑦_材料品名
            case GXHDO102B008Const.TENKAZAI7_ZAIRYOUMEI:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai7_zairyoumei());

            // 添加材⑦_調合量規格
            case GXHDO102B008Const.TENKAZAI7_TYOUGOURYOUKIKAKU:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai7_tyougouryoukikaku());

            // 添加材⑦_部材在庫No1
            case GXHDO102B008Const.TENKAZAI7_BUZAIZAIKOLOTNO1:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai7_buzaizaikolotno1());

            // 添加材⑦_調合量1
            case GXHDO102B008Const.TENKAZAI7_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai7_tyougouryou1());

            // 添加材⑦_部材在庫No2
            case GXHDO102B008Const.TENKAZAI7_BUZAIZAIKOLOTNO2:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai7_buzaizaikolotno2());

            // 添加材⑦_調合量2
            case GXHDO102B008Const.TENKAZAI7_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai7_tyougouryou2());

            // 添加材⑧_材料品名
            case GXHDO102B008Const.TENKAZAI8_ZAIRYOUMEI:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai8_zairyoumei());

            // 添加材⑧_調合量規格
            case GXHDO102B008Const.TENKAZAI8_TYOUGOURYOUKIKAKU:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai8_tyougouryoukikaku());

            // 添加材⑧_部材在庫No1
            case GXHDO102B008Const.TENKAZAI8_BUZAIZAIKOLOTNO1:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai8_buzaizaikolotno1());

            // 添加材⑧_調合量1
            case GXHDO102B008Const.TENKAZAI8_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai8_tyougouryou1());

            // 添加材⑧_部材在庫No2
            case GXHDO102B008Const.TENKAZAI8_BUZAIZAIKOLOTNO2:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai8_buzaizaikolotno2());

            // 添加材⑧_調合量2
            case GXHDO102B008Const.TENKAZAI8_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai8_tyougouryou2());

            // 添加材⑨_材料品名
            case GXHDO102B008Const.TENKAZAI9_ZAIRYOUMEI:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai9_zairyoumei());

            // 添加材⑨_調合量規格
            case GXHDO102B008Const.TENKAZAI9_TYOUGOURYOUKIKAKU:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai9_tyougouryoukikaku());

            // 添加材⑨_部材在庫No1
            case GXHDO102B008Const.TENKAZAI9_BUZAIZAIKOLOTNO1:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai9_buzaizaikolotno1());

            // 添加材⑨_調合量1
            case GXHDO102B008Const.TENKAZAI9_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai9_tyougouryou1());

            // 添加材⑨_部材在庫No2
            case GXHDO102B008Const.TENKAZAI9_BUZAIZAIKOLOTNO2:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai9_buzaizaikolotno2());

            // 添加材⑨_調合量2
            case GXHDO102B008Const.TENKAZAI9_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srTenkaTyogo.getTenkazai9_tyougouryou2());

            // 秤量終了日
            case GXHDO102B008Const.HYOURYOUSYUURYOU_DAY:
                return DateUtil.formattedTimestamp(srTenkaTyogo.getHyouryousyuuryounichiji(), "yyMMdd");

            // 秤量終了時間
            case GXHDO102B008Const.HYOURYOUSYUURYOU_TIME:
                return DateUtil.formattedTimestamp(srTenkaTyogo.getHyouryousyuuryounichiji(), "HHmm");

            // 担当者
            case GXHDO102B008Const.TANTOUSYA:
                return StringUtil.nullToBlank(srTenkaTyogo.getTantousya());

            // 確認者
            case GXHDO102B008Const.KAKUNINSYA:
                return StringUtil.nullToBlank(srTenkaTyogo.getKakuninsya());

            // 備考1
            case GXHDO102B008Const.BIKOU1:
                return StringUtil.nullToBlank(srTenkaTyogo.getBikou1());

            // 備考2
            case GXHDO102B008Const.BIKOU2:
                return StringUtil.nullToBlank(srTenkaTyogo.getBikou2());

            default:
                return null;
        }
    }

    /**
     * 添加材ｽﾗﾘｰ作製・添加材調合_仮登録(tmp_sr_tenka_tyogo)登録処理(削除時)
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
    private void insertDeleteDataTmpSrTenkaTyogo(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_tenka_tyogo ("
                + "kojyo,lotno,edaban,tenkazaislurryhinmei,tenkazaislurrylotno,lotkubun,hyouryougouki,fuutaijyuuryou,"
                + "hyouryoukaisinichiji,tenkazai1_zairyoumei,tenkazai1_tyougouryoukikaku,tenkazai1_buzaizaikolotno1,"
                + "tenkazai1_tyougouryou1,tenkazai1_buzaizaikolotno2,tenkazai1_tyougouryou2,tenkazai2_zairyoumei,"
                + "tenkazai2_tyougouryoukikaku,tenkazai2_buzaizaikolotno1,tenkazai2_tyougouryou1,tenkazai2_buzaizaikolotno2,"
                + "tenkazai2_tyougouryou2,tenkazai3_zairyoumei,tenkazai3_tyougouryoukikaku,tenkazai3_buzaizaikolotno1,"
                + "tenkazai3_tyougouryou1,tenkazai3_buzaizaikolotno2,tenkazai3_tyougouryou2,tenkazai4_zairyoumei,"
                + "tenkazai4_tyougouryoukikaku,tenkazai4_buzaizaikolotno1,tenkazai4_tyougouryou1,tenkazai4_buzaizaikolotno2,"
                + "tenkazai4_tyougouryou2,tenkazai5_zairyoumei,tenkazai5_tyougouryoukikaku,tenkazai5_buzaizaikolotno1,"
                + "tenkazai5_tyougouryou1,tenkazai5_buzaizaikolotno2,tenkazai5_tyougouryou2,tenkazai6_zairyoumei,"
                + "tenkazai6_tyougouryoukikaku,tenkazai6_buzaizaikolotno1,tenkazai6_tyougouryou1,tenkazai6_buzaizaikolotno2,"
                + "tenkazai6_tyougouryou2,tenkazai7_zairyoumei,tenkazai7_tyougouryoukikaku,tenkazai7_buzaizaikolotno1,"
                + "tenkazai7_tyougouryou1,tenkazai7_buzaizaikolotno2,tenkazai7_tyougouryou2,tenkazai8_zairyoumei,"
                + "tenkazai8_tyougouryoukikaku,tenkazai8_buzaizaikolotno1,tenkazai8_tyougouryou1,tenkazai8_buzaizaikolotno2,"
                + "tenkazai8_tyougouryou2,tenkazai9_zairyoumei,tenkazai9_tyougouryoukikaku,tenkazai9_buzaizaikolotno1,"
                + "tenkazai9_tyougouryou1,tenkazai9_buzaizaikolotno2,tenkazai9_tyougouryou2,hyouryousyuuryounichiji,"
                + "tantousya,kakuninsya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,sakujyoflg "
                + ") SELECT "
                + "kojyo,lotno,edaban,tenkazaislurryhinmei,tenkazaislurrylotno,lotkubun,hyouryougouki,fuutaijyuuryou,"
                + "hyouryoukaisinichiji,tenkazai1_zairyoumei,tenkazai1_tyougouryoukikaku,tenkazai1_buzaizaikolotno1,"
                + "tenkazai1_tyougouryou1,tenkazai1_buzaizaikolotno2,tenkazai1_tyougouryou2,tenkazai2_zairyoumei,"
                + "tenkazai2_tyougouryoukikaku,tenkazai2_buzaizaikolotno1,tenkazai2_tyougouryou1,tenkazai2_buzaizaikolotno2,"
                + "tenkazai2_tyougouryou2,tenkazai3_zairyoumei,tenkazai3_tyougouryoukikaku,tenkazai3_buzaizaikolotno1,"
                + "tenkazai3_tyougouryou1,tenkazai3_buzaizaikolotno2,tenkazai3_tyougouryou2,tenkazai4_zairyoumei,"
                + "tenkazai4_tyougouryoukikaku,tenkazai4_buzaizaikolotno1,tenkazai4_tyougouryou1,tenkazai4_buzaizaikolotno2,"
                + "tenkazai4_tyougouryou2,tenkazai5_zairyoumei,tenkazai5_tyougouryoukikaku,tenkazai5_buzaizaikolotno1,"
                + "tenkazai5_tyougouryou1,tenkazai5_buzaizaikolotno2,tenkazai5_tyougouryou2,tenkazai6_zairyoumei,"
                + "tenkazai6_tyougouryoukikaku,tenkazai6_buzaizaikolotno1,tenkazai6_tyougouryou1,tenkazai6_buzaizaikolotno2,"
                + "tenkazai6_tyougouryou2,tenkazai7_zairyoumei,tenkazai7_tyougouryoukikaku,tenkazai7_buzaizaikolotno1,"
                + "tenkazai7_tyougouryou1,tenkazai7_buzaizaikolotno2,tenkazai7_tyougouryou2,tenkazai8_zairyoumei,"
                + "tenkazai8_tyougouryoukikaku,tenkazai8_buzaizaikolotno1,tenkazai8_tyougouryou1,tenkazai8_buzaizaikolotno2,"
                + "tenkazai8_tyougouryou2,tenkazai9_zairyoumei,tenkazai9_tyougouryoukikaku,tenkazai9_buzaizaikolotno1,"
                + "tenkazai9_tyougouryou1,tenkazai9_buzaizaikolotno2,tenkazai9_tyougouryou2,hyouryousyuuryounichiji,"
                + "tantousya,kakuninsya,bikou1,bikou2,?,?,?,? "
                + " FROM sr_tenka_tyogo "
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
    private void initGXHDO102B008A(ProcessData processData) {
        GXHDO102B008A bean = (GXHDO102B008A) getFormBean("gXHDO102B008A");
        bean.setWiplotno(getItemRow(processData.getItemList(), GXHDO102B008Const.WIPLOTNO));
        bean.setTenkazaislurryhinmei(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAISLURRYHINMEI));
        bean.setTenkazaislurrylotno(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAISLURRYLOTNO));
        bean.setLotkubun(getItemRow(processData.getItemList(), GXHDO102B008Const.LOTKUBUN));
        bean.setHyouryougouki(getItemRow(processData.getItemList(), GXHDO102B008Const.HYOURYOUGOUKI));
        bean.setFuutaijyuuryou(getItemRow(processData.getItemList(), GXHDO102B008Const.FUUTAIJYUURYOU));
        bean.setHyouryoukaisi_day(getItemRow(processData.getItemList(), GXHDO102B008Const.HYOURYOUKAISI_DAY));
        bean.setHyouryoukaisi_time(getItemRow(processData.getItemList(), GXHDO102B008Const.HYOURYOUKAISI_TIME));
        bean.setTenkazai1_zairyoumei(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI1_ZAIRYOUMEI));
        bean.setTenkazai1_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI1_TYOUGOURYOUKIKAKU));
        bean.setTenkazai1_buzaizaikolotno1(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI1_BUZAIZAIKOLOTNO1));
        bean.setTenkazai1_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI1_TYOUGOURYOU1));
        bean.setTenkazai1_buzaizaikolotno2(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI1_BUZAIZAIKOLOTNO2));
        bean.setTenkazai1_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI1_TYOUGOURYOU2));
        bean.setTenkazai2_zairyoumei(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI2_ZAIRYOUMEI));
        bean.setTenkazai2_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI2_TYOUGOURYOUKIKAKU));
        bean.setTenkazai2_buzaizaikolotno1(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI2_BUZAIZAIKOLOTNO1));
        bean.setTenkazai2_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI2_TYOUGOURYOU1));
        bean.setTenkazai2_buzaizaikolotno2(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI2_BUZAIZAIKOLOTNO2));
        bean.setTenkazai2_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI2_TYOUGOURYOU2));
        bean.setTenkazai3_zairyoumei(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI3_ZAIRYOUMEI));
        bean.setTenkazai3_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI3_TYOUGOURYOUKIKAKU));
        bean.setTenkazai3_buzaizaikolotno1(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI3_BUZAIZAIKOLOTNO1));
        bean.setTenkazai3_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI3_TYOUGOURYOU1));
        bean.setTenkazai3_buzaizaikolotno2(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI3_BUZAIZAIKOLOTNO2));
        bean.setTenkazai3_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI3_TYOUGOURYOU2));
        bean.setTenkazai4_zairyoumei(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI4_ZAIRYOUMEI));
        bean.setTenkazai4_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI4_TYOUGOURYOUKIKAKU));
        bean.setTenkazai4_buzaizaikolotno1(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI4_BUZAIZAIKOLOTNO1));
        bean.setTenkazai4_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI4_TYOUGOURYOU1));
        bean.setTenkazai4_buzaizaikolotno2(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI4_BUZAIZAIKOLOTNO2));
        bean.setTenkazai4_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI4_TYOUGOURYOU2));
        bean.setTenkazai5_zairyoumei(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI5_ZAIRYOUMEI));
        bean.setTenkazai5_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI5_TYOUGOURYOUKIKAKU));
        bean.setTenkazai5_buzaizaikolotno1(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI5_BUZAIZAIKOLOTNO1));
        bean.setTenkazai5_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI5_TYOUGOURYOU1));
        bean.setTenkazai5_buzaizaikolotno2(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI5_BUZAIZAIKOLOTNO2));
        bean.setTenkazai5_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI5_TYOUGOURYOU2));
        bean.setTenkazai6_zairyoumei(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI6_ZAIRYOUMEI));
        bean.setTenkazai6_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI6_TYOUGOURYOUKIKAKU));
        bean.setTenkazai6_buzaizaikolotno1(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI6_BUZAIZAIKOLOTNO1));
        bean.setTenkazai6_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI6_TYOUGOURYOU1));
        bean.setTenkazai6_buzaizaikolotno2(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI6_BUZAIZAIKOLOTNO2));
        bean.setTenkazai6_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI6_TYOUGOURYOU2));
        bean.setTenkazai7_zairyoumei(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI7_ZAIRYOUMEI));
        bean.setTenkazai7_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI7_TYOUGOURYOUKIKAKU));
        bean.setTenkazai7_buzaizaikolotno1(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI7_BUZAIZAIKOLOTNO1));
        bean.setTenkazai7_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI7_TYOUGOURYOU1));
        bean.setTenkazai7_buzaizaikolotno2(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI7_BUZAIZAIKOLOTNO2));
        bean.setTenkazai7_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI7_TYOUGOURYOU2));
        bean.setTenkazai8_zairyoumei(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI8_ZAIRYOUMEI));
        bean.setTenkazai8_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI8_TYOUGOURYOUKIKAKU));
        bean.setTenkazai8_buzaizaikolotno1(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI8_BUZAIZAIKOLOTNO1));
        bean.setTenkazai8_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI8_TYOUGOURYOU1));
        bean.setTenkazai8_buzaizaikolotno2(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI8_BUZAIZAIKOLOTNO2));
        bean.setTenkazai8_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI8_TYOUGOURYOU2));
        bean.setTenkazai9_zairyoumei(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI9_ZAIRYOUMEI));
        bean.setTenkazai9_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI9_TYOUGOURYOUKIKAKU));
        bean.setTenkazai9_buzaizaikolotno1(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI9_BUZAIZAIKOLOTNO1));
        bean.setTenkazai9_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI9_TYOUGOURYOU1));
        bean.setTenkazai9_buzaizaikolotno2(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI9_BUZAIZAIKOLOTNO2));
        bean.setTenkazai9_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI9_TYOUGOURYOU2));
        bean.setHyouryousyuuryou_day(getItemRow(processData.getItemList(), GXHDO102B008Const.HYOURYOUSYUURYOU_DAY));
        bean.setHyouryousyuuryou_time(getItemRow(processData.getItemList(), GXHDO102B008Const.HYOURYOUSYUURYOU_TIME));
        bean.setTantousya(getItemRow(processData.getItemList(), GXHDO102B008Const.TANTOUSYA));
        bean.setKakuninsya(getItemRow(processData.getItemList(), GXHDO102B008Const.KAKUNINSYA));
        bean.setBikou1(getItemRow(processData.getItemList(), GXHDO102B008Const.BIKOU1));
        bean.setBikou2(getItemRow(processData.getItemList(), GXHDO102B008Const.BIKOU2));
    }

    /**
     * 項目IDリスト取得
     *
     * @param processData 処理制御データ
     * @param formIdList 項目定義情報
     * @return 項目IDリスト
     */
    private List<String> getItemIdList(ProcessData processData, List<String> formIdList) {
        try {
            QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
            String sql = "SELECT item_id itemId "
                    + " FROM fxhdd01 "
                    + " WHERE "
                    + DBUtil.getInConditionPreparedStatement("gamen_id", formIdList.size())
                    + " ORDER BY gamen_id, item_no ";

            List<Object> params = new ArrayList<>();
            params.addAll(formIdList);

            List<Map<String, Object>> mapList = queryRunnerDoc.query(sql, new MapListHandler(), params.toArray());
            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            return mapList.stream().map(n -> n.get("itemId").toString()).collect(Collectors.toList());
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
        return null;
    }

    /**
     * 添加材ｽﾗﾘｰ作製・添加材調合入力_ｻﾌﾞ画面データの規格値取得処理
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
     * 添加材1_材料品名のﾘﾝｸ押下時、 添加材ｽﾗﾘｰ作製・添加材調合入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC004SubGamen1(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B008Const.TENKAZAI1_BUZAIZAIKOLOTNO1, GXHDO102B008Const.TENKAZAI1_TYOUGOURYOU1,
                GXHDO102B008Const.TENKAZAI1_BUZAIZAIKOLOTNO2, GXHDO102B008Const.TENKAZAI1_TYOUGOURYOU2);
        return openC004SubGamen(processData, 1, returnItemIdList);
    }

    /**
     * 添加材2_材料品名のﾘﾝｸ押下時、 添加材ｽﾗﾘｰ作製・添加材調合入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC004SubGamen2(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B008Const.TENKAZAI2_BUZAIZAIKOLOTNO1, GXHDO102B008Const.TENKAZAI2_TYOUGOURYOU1,
                GXHDO102B008Const.TENKAZAI2_BUZAIZAIKOLOTNO2, GXHDO102B008Const.TENKAZAI2_TYOUGOURYOU2);
        return openC004SubGamen(processData, 2, returnItemIdList);
    }

    /**
     * 添加材3_材料品名のﾘﾝｸ押下時、 添加材ｽﾗﾘｰ作製・添加材調合入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC004SubGamen3(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B008Const.TENKAZAI3_BUZAIZAIKOLOTNO1, GXHDO102B008Const.TENKAZAI3_TYOUGOURYOU1,
                GXHDO102B008Const.TENKAZAI3_BUZAIZAIKOLOTNO2, GXHDO102B008Const.TENKAZAI3_TYOUGOURYOU2);
        return openC004SubGamen(processData, 3, returnItemIdList);
    }

    /**
     * 添加材4_材料品名のﾘﾝｸ押下時、 添加材ｽﾗﾘｰ作製・添加材調合入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC004SubGamen4(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B008Const.TENKAZAI4_BUZAIZAIKOLOTNO1, GXHDO102B008Const.TENKAZAI4_TYOUGOURYOU1,
                GXHDO102B008Const.TENKAZAI4_BUZAIZAIKOLOTNO2, GXHDO102B008Const.TENKAZAI4_TYOUGOURYOU2);
        return openC004SubGamen(processData, 4, returnItemIdList);
    }

    /**
     * 添加材5_材料品名のﾘﾝｸ押下時、 添加材ｽﾗﾘｰ作製・添加材調合入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC004SubGamen5(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B008Const.TENKAZAI5_BUZAIZAIKOLOTNO1, GXHDO102B008Const.TENKAZAI5_TYOUGOURYOU1,
                GXHDO102B008Const.TENKAZAI5_BUZAIZAIKOLOTNO2, GXHDO102B008Const.TENKAZAI5_TYOUGOURYOU2);
        return openC004SubGamen(processData, 5, returnItemIdList);
    }

    /**
     * 添加材6_材料品名のﾘﾝｸ押下時、 添加材ｽﾗﾘｰ作製・添加材調合入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC004SubGamen6(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B008Const.TENKAZAI6_BUZAIZAIKOLOTNO1, GXHDO102B008Const.TENKAZAI6_TYOUGOURYOU1,
                GXHDO102B008Const.TENKAZAI6_BUZAIZAIKOLOTNO2, GXHDO102B008Const.TENKAZAI6_TYOUGOURYOU2);
        return openC004SubGamen(processData, 6, returnItemIdList);
    }

    /**
     * 添加材7_材料品名のﾘﾝｸ押下時、 添加材ｽﾗﾘｰ作製・添加材調合入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC004SubGamen7(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B008Const.TENKAZAI7_BUZAIZAIKOLOTNO1, GXHDO102B008Const.TENKAZAI7_TYOUGOURYOU1,
                GXHDO102B008Const.TENKAZAI7_BUZAIZAIKOLOTNO2, GXHDO102B008Const.TENKAZAI7_TYOUGOURYOU2);
        return openC004SubGamen(processData, 7, returnItemIdList);
    }

    /**
     * 添加材8_材料品名のﾘﾝｸ押下時、 添加材ｽﾗﾘｰ作製・添加材調合入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC004SubGamen8(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B008Const.TENKAZAI8_BUZAIZAIKOLOTNO1, GXHDO102B008Const.TENKAZAI8_TYOUGOURYOU1,
                GXHDO102B008Const.TENKAZAI8_BUZAIZAIKOLOTNO2, GXHDO102B008Const.TENKAZAI8_TYOUGOURYOU2);
        return openC004SubGamen(processData, 8, returnItemIdList);
    }

    /**
     * 添加材9_材料品名のﾘﾝｸ押下時、 添加材ｽﾗﾘｰ作製・添加材調合入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC004SubGamen9(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B008Const.TENKAZAI9_BUZAIZAIKOLOTNO1, GXHDO102B008Const.TENKAZAI9_TYOUGOURYOU1,
                GXHDO102B008Const.TENKAZAI9_BUZAIZAIKOLOTNO2, GXHDO102B008Const.TENKAZAI9_TYOUGOURYOU2);
        return openC004SubGamen(processData, 9, returnItemIdList);
    }

    /**
     * 添加材ｽﾗﾘｰ作製・添加材調合入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @param zairyokubun 材料区分
     * @param returnItemIdList サブ画面から戻ったときに値を設定必要項目リスト
     * @return 処理制御データ
     */
    public ProcessData openC004SubGamen(ProcessData processData, int zairyokubun, List<String> returnItemIdList) {
        try {
            // 「秤量号機」
            FXHDD01 itemGoki = getItemRow(processData.getItemList(), GXHDO102B008Const.HYOURYOUGOUKI);
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
            processData.setCollBackParam("gxhdo102c004");

            GXHDO102C004 beanGXHDO102C004 = (GXHDO102C004) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C004);
            GXHDO102C004Model gxhdo102c004model = beanGXHDO102C004.getGxhdO102c004Model();
            // 主画面からサブ画面に渡されたデータを設定
            setSubGamenInitData(gxhdo102c004model, zairyokubun, itemGoki, returnItemIdList);

            beanGXHDO102C004.setGxhdO102c004ModelView(gxhdo102c004model.clone());
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
     * @param gxhdo102c004model モデルデータ
     * @param zairyokubun 材料区分
     * @param itemGoki 秤量号機データ
     * @param returnItemIdList サブ画面から戻るデータリスト
     * @throws CloneNotSupportedException 例外エラー
     */
    private void setSubGamenInitData(GXHDO102C004Model gxhdo102c004model, int zairyokubun, FXHDD01 itemGoki, List<String> returnItemIdList) throws CloneNotSupportedException {
        GXHDO102C004Model.SubGamenData c004subgamendata = GXHDO102C004Logic.getC004subgamendata(gxhdo102c004model, zairyokubun);
        if (c004subgamendata == null) {
            return;
        }
        c004subgamendata.setSubDataGoki(StringUtil.nullToBlank(itemGoki.getValue()));
        c004subgamendata.setSubDataZairyokubun(zairyokubun);
        // サブ画面から戻ったときに値を設定する項目を指定する。
        c004subgamendata.setReturnItemIdBuzailotno1(returnItemIdList.get(0)); // 部材在庫No.X_1
        c004subgamendata.setReturnItemIdTyougouryou1(returnItemIdList.get(1)); // 調合量X_1
        c004subgamendata.setReturnItemIdBuzailotno2(returnItemIdList.get(2)); // 部材在庫NoX_2
        c004subgamendata.setReturnItemIdTyougouryou2(returnItemIdList.get(3)); // 調合量X_2
        gxhdo102c004model.setShowsubgamendata(c004subgamendata.clone());
        // サブ画面の調合残量の計算
        GXHDO102C004Logic.calcTyogouzanryou(gxhdo102c004model);
    }

    /**
     * 添加材ｽﾗﾘｰ作製・添加材調合入力_ｻﾌﾞ画面データ設定処理
     *
     * @param processData 処理制御データ
     * @param subSrTenkaTyogoList 添加材ｽﾗﾘｰ作製・添加材調合入力_ｻﾌﾞ画面データリスト
     */
    private void setInputItemDataSubFormC004(ProcessData processData, List<SubSrTenkaTyogo> subSrTenkaTyogoList) {
        // サブ画面の情報を取得
        GXHDO102C004 beanGXHDO102C004 = (GXHDO102C004) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C004);

        GXHDO102C004Model model;
        if (subSrTenkaTyogoList == null) {
            // 登録データが無い場合、主画面の材料品名1-2と調合量規格1-2はｻﾌﾞ画面の初期値にセットする。
            subSrTenkaTyogoList = new ArrayList<>();
            SubSrTenkaTyogo subgamen1 = new SubSrTenkaTyogo();
            SubSrTenkaTyogo subgamen2 = new SubSrTenkaTyogo();
            SubSrTenkaTyogo subgamen3 = new SubSrTenkaTyogo();
            SubSrTenkaTyogo subgamen4 = new SubSrTenkaTyogo();
            SubSrTenkaTyogo subgamen5 = new SubSrTenkaTyogo();
            SubSrTenkaTyogo subgamen6 = new SubSrTenkaTyogo();
            SubSrTenkaTyogo subgamen7 = new SubSrTenkaTyogo();
            SubSrTenkaTyogo subgamen8 = new SubSrTenkaTyogo();
            SubSrTenkaTyogo subgamen9 = new SubSrTenkaTyogo();

            subgamen1.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI1_ZAIRYOUMEI))); // 添加材1_材料品名
            subgamen1.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI1_TYOUGOURYOUKIKAKU))); // 添加材1_調合量規格
            subgamen2.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI2_ZAIRYOUMEI))); // 添加材2_材料品名
            subgamen2.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI2_TYOUGOURYOUKIKAKU))); // 添加材2_調合量規格
            subgamen3.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI3_ZAIRYOUMEI))); // 添加材3_材料品名
            subgamen3.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI3_TYOUGOURYOUKIKAKU))); // 添加材3_調合量規格
            subgamen4.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI4_ZAIRYOUMEI))); // 添加材4_材料品名
            subgamen4.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI4_TYOUGOURYOUKIKAKU))); // 添加材4_調合量規格
            subgamen5.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI5_ZAIRYOUMEI))); // 添加材5_材料品名
            subgamen5.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI5_TYOUGOURYOUKIKAKU))); // 添加材5_調合量規格
            subgamen6.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI6_ZAIRYOUMEI))); // 添加材6_材料品名
            subgamen6.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI6_TYOUGOURYOUKIKAKU))); // 添加材6_調合量規格
            subgamen7.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI7_ZAIRYOUMEI))); // 添加材7_材料品名
            subgamen7.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI7_TYOUGOURYOUKIKAKU))); // 添加材7_調合量規格
            subgamen8.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI8_ZAIRYOUMEI))); // 添加材8_材料品名
            subgamen8.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI8_TYOUGOURYOUKIKAKU))); // 添加材8_調合量規格
            subgamen9.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI9_ZAIRYOUMEI))); // 添加材9_材料品名
            subgamen9.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B008Const.TENKAZAI9_TYOUGOURYOUKIKAKU))); // 添加材9_調合量規格
            subSrTenkaTyogoList.add(subgamen1);
            subSrTenkaTyogoList.add(subgamen2);
            subSrTenkaTyogoList.add(subgamen3);
            subSrTenkaTyogoList.add(subgamen4);
            subSrTenkaTyogoList.add(subgamen5);
            subSrTenkaTyogoList.add(subgamen6);
            subSrTenkaTyogoList.add(subgamen7);
            subSrTenkaTyogoList.add(subgamen8);
            subSrTenkaTyogoList.add(subgamen9);
            model = GXHDO102C004Logic.createGXHDO102C004Model(subSrTenkaTyogoList);

        } else {
            // 登録データがあれば登録データをセットする。
            model = GXHDO102C004Logic.createGXHDO102C004Model(subSrTenkaTyogoList);
        }
        beanGXHDO102C004.setGxhdO102c004Model(model);
    }

    /**
     * 添加材ｽﾗﾘｰ作製・添加材調合入力_ｻﾌﾞ画面の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return 添加材ｽﾗﾘｰ作製・添加材調合入力_ｻﾌﾞ画面登録データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrTenkaTyogo> getSubSrTenkaTyogoData(QueryRunner queryRunnerQcdb,
            String rev, String jotaiFlg, String kojyo, String lotNo, String edaban) throws SQLException {
        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSubSrTenkaTyogo(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSubSrTenkaTyogo(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        }
    }

    /**
     * [添加材ｽﾗﾘｰ作製・添加材調合入力_ｻﾌﾞ画面]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrTenkaTyogo> loadSubSrTenkaTyogo(QueryRunner queryRunnerQcdb,
            String kojyo, String lotNo, String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + "kojyo,lotno,edaban,zairyokubun,tyogouryoukikaku,tyogouzanryou,zairyohinmei,"
                + "buzailotno1,buzaihinmei1,tyougouryou1_1,tyougouryou1_2,tyougouryou1_3,tyougouryou1_4,"
                + "tyougouryou1_5,tyougouryou1_6,buzailotno2,buzaihinmei2,tyougouryou2_1,tyougouryou2_2,"
                + "tyougouryou2_3,tyougouryou2_4,tyougouryou2_5,tyougouryou2_6,torokunichiji,kosinnichiji,"
                + "revision, '0' AS deleteflag "
                + " FROM sub_sr_tenka_tyogo "
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
        ResultSetHandler<List<SubSrTenkaTyogo>> beanHandler = new BeanListHandler<>(SubSrTenkaTyogo.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [添加材ｽﾗﾘｰ作製・添加材調合入力_ｻﾌﾞ画面_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrTenkaTyogo> loadTmpSubSrTenkaTyogo(QueryRunner queryRunnerQcdb,
            String kojyo, String lotNo, String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + "kojyo,lotno,edaban,zairyokubun,tyogouryoukikaku,tyogouzanryou,zairyohinmei,"
                + "buzailotno1,buzaihinmei1,tyougouryou1_1,tyougouryou1_2,tyougouryou1_3,tyougouryou1_4,"
                + "tyougouryou1_5,tyougouryou1_6,buzailotno2,buzaihinmei2,tyougouryou2_1,tyougouryou2_2,"
                + "tyougouryou2_3,tyougouryou2_4,tyougouryou2_5,tyougouryou2_6,torokunichiji,kosinnichiji,"
                + "revision, deleteflag "
                + " FROM tmp_sub_sr_tenka_tyogo "
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
        ResultSetHandler<List<SubSrTenkaTyogo>> beanHandler = new BeanListHandler<>(SubSrTenkaTyogo.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * 添加材ｽﾗﾘｰ作製・添加材調合入力_サブ画面_仮登録(tmp_sub_sr_tenka_tyogo)登録処理
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
    private void insertTmpSubSrTenkaTyogo(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal newRev, int deleteflag, String kojyo, String lotNo, String edaban, Integer zairyokubun,
            String systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sub_sr_tenka_tyogo ( "
                + "kojyo,lotno,edaban,zairyokubun,tyogouryoukikaku,tyogouzanryou,zairyohinmei,"
                + "buzailotno1,buzaihinmei1,tyougouryou1_1,tyougouryou1_2,tyougouryou1_3,tyougouryou1_4,"
                + "tyougouryou1_5,tyougouryou1_6,buzailotno2,buzaihinmei2,tyougouryou2_1,tyougouryou2_2,"
                + "tyougouryou2_3,tyougouryou2_4,tyougouryou2_5,tyougouryou2_6,torokunichiji,kosinnichiji,"
                + "revision, deleteflag "
                + " ) VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";

        List<Object> params = setUpdateParameterTmpSubSrTenkaTyogo(true, newRev, deleteflag, kojyo, lotNo, edaban, zairyokubun, systemTime);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 添加材ｽﾗﾘｰ作製・添加材調合入力_仮登録(tmp_sub_sr_tenka_tyogo)更新処理
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
    private void updateTmpSubSrTenkaTyogo(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal rev, BigDecimal newRev, String kojyo, String lotNo,
            String edaban, Integer zairyokubun, String systemTime) throws SQLException {

        String sql = "UPDATE tmp_sub_sr_tenka_tyogo SET "
                + "tyogouryoukikaku = ?,tyogouzanryou = ?,zairyohinmei = ?,"
                + "buzailotno1 = ?,buzaihinmei1 = ?,tyougouryou1_1 = ?,tyougouryou1_2 = ?,tyougouryou1_3 = ?,tyougouryou1_4 = ?,"
                + "tyougouryou1_5 = ?,tyougouryou1_6 = ?,buzailotno2 = ?,buzaihinmei2 = ?,tyougouryou2_1 = ?,tyougouryou2_2 = ?,"
                + "tyougouryou2_3 = ?,tyougouryou2_4 = ?,tyougouryou2_5 = ?,tyougouryou2_6 = ?,kosinnichiji = ?,revision = ?, deleteflag = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND zairyokubun = ? AND revision = ? ";

        List<Object> params = setUpdateParameterTmpSubSrTenkaTyogo(false, newRev, 0, kojyo, lotNo, edaban, zairyokubun, systemTime);

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
     * 添加材ｽﾗﾘｰ作製・添加材調合入力_サブ画面仮登録(tmp_sub_sr_tenka_tyogo)更新値パラメータ設定
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
    private List<Object> setUpdateParameterTmpSubSrTenkaTyogo(boolean isInsert, BigDecimal newRev,
            int deleteflag, String kojyo, String lotNo, String edaban, Integer zairyokubun, String systemTime) {

        List<Object> params = new ArrayList<>();

        // 子画面情報を取得
        GXHDO102C004 beanGXHDO102C004 = (GXHDO102C004) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C004);
        GXHDO102C004Model gxhdO102c004Model = beanGXHDO102C004.getGxhdO102c004Model();

        // 添加材ｽﾗﾘｰ作製・添加材調合入力_サブ画面から更新値を取得
        ArrayList<Object> subGamenDataList = getSubGamenData(gxhdO102c004Model, zairyokubun);
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
     * 添加材ｽﾗﾘｰ作製・添加材調合入力_サブ画面仮登録(tmp_sub_sr_tenka_tyogo)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSubSrTenkaTyogo(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal rev, String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sub_sr_tenka_tyogo "
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
     * 添加材ｽﾗﾘｰ作製・添加材調合入力_サブ画面(sub_sr_tenka_tyogo)登録処理
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
    private void insertSubSrTenkaTyogo(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal newRev, String kojyo, String lotNo, String edaban,
            Integer zairyokubun, String systemTime) throws SQLException {
        String sql = "INSERT INTO sub_sr_tenka_tyogo ( "
                + "kojyo,lotno,edaban,zairyokubun,tyogouryoukikaku,tyogouzanryou,zairyohinmei,"
                + "buzailotno1,buzaihinmei1,tyougouryou1_1,tyougouryou1_2,tyougouryou1_3,tyougouryou1_4,"
                + "tyougouryou1_5,tyougouryou1_6,buzailotno2,buzaihinmei2,tyougouryou2_1,tyougouryou2_2,"
                + "tyougouryou2_3,tyougouryou2_4,tyougouryou2_5,tyougouryou2_6,torokunichiji,kosinnichiji,"
                + "revision "
                + " ) VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";

        List<Object> params = setUpdateParameterSubSrTenkaTyogo(true, newRev, kojyo, lotNo, edaban, zairyokubun, systemTime);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 添加材ｽﾗﾘｰ作製・添加材調合入力_ｻﾌﾞ画面(sub_sr_tenka_tyogo)更新処理
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
    private void updateSubSrTenkaTyogo(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal rev, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            Integer zairyokubun, String systemTime) throws SQLException {

        String sql = "UPDATE sub_sr_tenka_tyogo SET "
                + "tyogouryoukikaku = ?,tyogouzanryou = ?,zairyohinmei = ?,"
                + "buzailotno1 = ?,buzaihinmei1 = ?,tyougouryou1_1 = ?,tyougouryou1_2 = ?,tyougouryou1_3 = ?,tyougouryou1_4 = ?,"
                + "tyougouryou1_5 = ?,tyougouryou1_6 = ?,buzailotno2 = ?,buzaihinmei2 = ?,tyougouryou2_1 = ?,tyougouryou2_2 = ?,"
                + "tyougouryou2_3 = ?,tyougouryou2_4 = ?,tyougouryou2_5 = ?,tyougouryou2_6 = ?,kosinnichiji = ?,revision = ?"
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND zairyokubun = ? AND revision = ? ";

        List<Object> params = setUpdateParameterSubSrTenkaTyogo(false, newRev, kojyo, lotNo, edaban, zairyokubun, systemTime);

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
     * 添加材ｽﾗﾘｰ作製・添加材調合入力_サブ画面から更新値を取得
     *
     * @param gxhdO102c004Model モデルデータ
     * @param zairyokubun 材料区分
     * @return 更新値情報
     */
    private ArrayList<Object> getSubGamenData(GXHDO102C004Model gxhdO102c004Model, Integer zairyokubun) {
        GXHDO102C004Model.SubGamenData c004subgamendata = GXHDO102C004Logic.getC004subgamendata(gxhdO102c004Model, zairyokubun);
        ArrayList<Object> returnList = new ArrayList<>();
        // 調合量規格
        FXHDD01 tyogouryoukikaku = c004subgamendata.getSubDataTyogouryoukikaku();
        // 調合残量
        FXHDD01 tyogouzanryou = c004subgamendata.getSubDataTyogouzanryou();
        // 部材①
        List<FXHDD01> buzaitab1DataList = c004subgamendata.getSubDataBuzaitab1();
        // 部材②
        List<FXHDD01> buzaitab2DataList = c004subgamendata.getSubDataBuzaitab2();
        returnList.add(tyogouryoukikaku);
        returnList.add(tyogouzanryou);
        returnList.add(buzaitab1DataList);
        returnList.add(buzaitab2DataList);
        return returnList;
    }

    /**
     * 添加材ｽﾗﾘｰ作製・添加材調合入力_サブ画面登録(tmp_sub_sr_tenka_tyogo)更新値パラメータ設定
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
    private List<Object> setUpdateParameterSubSrTenkaTyogo(boolean isInsert, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Integer zairyokubun, String systemTime) {

        List<Object> params = new ArrayList<>();

        // 子画面情報を取得
        GXHDO102C004 beanGXHDO102C004 = (GXHDO102C004) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C004);
        GXHDO102C004Model gxhdO102c004Model = beanGXHDO102C004.getGxhdO102c004Model();
        // 添加材ｽﾗﾘｰ作製・添加材調合入力_サブ画面から更新値を取得
        ArrayList<Object> subGamenDataList = getSubGamenData(gxhdO102c004Model, zairyokubun);
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
     * 添加材ｽﾗﾘｰ作製・添加材調合入力_サブ画面仮登録(tmp_sub_sr_tenka_tyogo)登録処理(削除時)
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
    private void insertDeleteDataTmpSubSrTenkaTyogo(QueryRunner queryRunnerQcdb,
            Connection conQcdb, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, String systemTime) throws SQLException {
        String sql = "INSERT INTO tmp_sub_sr_tenka_tyogo( "
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
                + " FROM sub_sr_tenka_tyogo "
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
     * 添加材ｽﾗﾘｰ作製・添加材調合入力_サブ画面仮登録(sub_sr_tenka_tyogo)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSubSrTenkaTyogo(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal rev, String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sub_sr_tenka_tyogo "
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
        // 添加材1_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B008Const.TENKAZAI1_BUZAIZAIKOLOTNO1, GXHDO102B008Const.TENKAZAI1_TYOUGOURYOU1, errorItemList);
        // 添加材1_部材在庫No2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B008Const.TENKAZAI1_BUZAIZAIKOLOTNO2, GXHDO102B008Const.TENKAZAI1_TYOUGOURYOU2, errorItemList);
        // 添加材2_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B008Const.TENKAZAI2_BUZAIZAIKOLOTNO1, GXHDO102B008Const.TENKAZAI2_TYOUGOURYOU1, errorItemList);
        // 添加材2_部材在庫No2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B008Const.TENKAZAI2_BUZAIZAIKOLOTNO2, GXHDO102B008Const.TENKAZAI2_TYOUGOURYOU2, errorItemList);
        // 添加材3_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B008Const.TENKAZAI3_BUZAIZAIKOLOTNO1, GXHDO102B008Const.TENKAZAI3_TYOUGOURYOU1, errorItemList);
        // 添加材3_部材在庫No2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B008Const.TENKAZAI3_BUZAIZAIKOLOTNO2, GXHDO102B008Const.TENKAZAI3_TYOUGOURYOU2, errorItemList);
        // 添加材4_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B008Const.TENKAZAI4_BUZAIZAIKOLOTNO1, GXHDO102B008Const.TENKAZAI4_TYOUGOURYOU1, errorItemList);
        // 添加材4_部材在庫No2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B008Const.TENKAZAI4_BUZAIZAIKOLOTNO2, GXHDO102B008Const.TENKAZAI4_TYOUGOURYOU2, errorItemList);
        // 添加材5_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B008Const.TENKAZAI5_BUZAIZAIKOLOTNO1, GXHDO102B008Const.TENKAZAI5_TYOUGOURYOU1, errorItemList);
        // 添加材5_部材在庫No2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B008Const.TENKAZAI5_BUZAIZAIKOLOTNO2, GXHDO102B008Const.TENKAZAI5_TYOUGOURYOU2, errorItemList);
        // 添加材6_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B008Const.TENKAZAI6_BUZAIZAIKOLOTNO1, GXHDO102B008Const.TENKAZAI6_TYOUGOURYOU1, errorItemList);
        // 添加材6_部材在庫No2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B008Const.TENKAZAI6_BUZAIZAIKOLOTNO2, GXHDO102B008Const.TENKAZAI6_TYOUGOURYOU2, errorItemList);
        // 添加材7_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B008Const.TENKAZAI7_BUZAIZAIKOLOTNO1, GXHDO102B008Const.TENKAZAI7_TYOUGOURYOU1, errorItemList);
        // 添加材7_部材在庫No2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B008Const.TENKAZAI7_BUZAIZAIKOLOTNO2, GXHDO102B008Const.TENKAZAI7_TYOUGOURYOU2, errorItemList);
        // 添加材8_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B008Const.TENKAZAI8_BUZAIZAIKOLOTNO1, GXHDO102B008Const.TENKAZAI8_TYOUGOURYOU1, errorItemList);
        // 添加材8_部材在庫No2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B008Const.TENKAZAI8_BUZAIZAIKOLOTNO2, GXHDO102B008Const.TENKAZAI8_TYOUGOURYOU2, errorItemList);
        // 添加材9_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B008Const.TENKAZAI9_BUZAIZAIKOLOTNO1, GXHDO102B008Const.TENKAZAI9_TYOUGOURYOU1, errorItemList);
        // 添加材9_部材在庫No2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B008Const.TENKAZAI9_BUZAIZAIKOLOTNO2, GXHDO102B008Const.TENKAZAI9_TYOUGOURYOU2, errorItemList);

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
        FXHDD01 itemFxhdd01Wiplotno = getItemRow(processData.getItemList(), GXHDO102B008Const.WIPLOTNO);
        if (itemFxhdd01Wiplotno != null) {
            // WIPﾛｯﾄNo
            wiplotnoValue = StringUtil.nullToBlank(itemFxhdd01Wiplotno.getValue());
        }
        ArrayList<String> paramsList = new ArrayList<>();
        paramsList.add(sizailotnoValue);
        paramsList.add(tantoshaCd);
        paramsList.add("PXHDO102");
        paramsList.add(tyougouryouValue);
        paramsList.add("");
        paramsList.add("");
        paramsList.add("");
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
        allItemIdMap.put(GXHDO102B008Const.WIPLOTNO, "WIPﾛｯﾄNo");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAISLURRYHINMEI, "添加材ｽﾗﾘｰ品名");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAISLURRYLOTNO, "添加材ｽﾗﾘｰLotNo");
        allItemIdMap.put(GXHDO102B008Const.LOTKUBUN, "ﾛｯﾄ区分");
        allItemIdMap.put(GXHDO102B008Const.HYOURYOUGOUKI, "秤量号機");
        allItemIdMap.put(GXHDO102B008Const.FUUTAIJYUURYOU, "風袋重量");
        allItemIdMap.put(GXHDO102B008Const.HYOURYOUKAISI_DAY, "秤量開始日");
        allItemIdMap.put(GXHDO102B008Const.HYOURYOUKAISI_TIME, "秤量開始時間");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI1_ZAIRYOUMEI, "添加材①_材料品名");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI1_TYOUGOURYOUKIKAKU, "添加材①_調合量規格");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI1_BUZAIZAIKOLOTNO1, "添加材①_部材在庫No1");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI1_TYOUGOURYOU1, "添加材①_調合量1");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI1_BUZAIZAIKOLOTNO2, "添加材①_部材在庫No2");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI1_TYOUGOURYOU2, "添加材①_調合量2");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI2_ZAIRYOUMEI, "添加材②_材料品名");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI2_TYOUGOURYOUKIKAKU, "添加材②_調合量規格");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI2_BUZAIZAIKOLOTNO1, "添加材②_部材在庫No1");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI2_TYOUGOURYOU1, "添加材②_調合量1");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI2_BUZAIZAIKOLOTNO2, "添加材②_部材在庫No2");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI2_TYOUGOURYOU2, "添加材②_調合量2");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI3_ZAIRYOUMEI, "添加材③_材料品名");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI3_TYOUGOURYOUKIKAKU, "添加材③_調合量規格");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI3_BUZAIZAIKOLOTNO1, "添加材③_部材在庫No1");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI3_TYOUGOURYOU1, "添加材③_調合量1");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI3_BUZAIZAIKOLOTNO2, "添加材③_部材在庫No2");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI3_TYOUGOURYOU2, "添加材③_調合量2");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI4_ZAIRYOUMEI, "添加材④_材料品名");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI4_TYOUGOURYOUKIKAKU, "添加材④_調合量規格");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI4_BUZAIZAIKOLOTNO1, "添加材④_部材在庫No1");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI4_TYOUGOURYOU1, "添加材④_調合量1");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI4_BUZAIZAIKOLOTNO2, "添加材④_部材在庫No2");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI4_TYOUGOURYOU2, "添加材④_調合量2");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI5_ZAIRYOUMEI, "添加材⑤_材料品名");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI5_TYOUGOURYOUKIKAKU, "添加材⑤_調合量規格");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI5_BUZAIZAIKOLOTNO1, "添加材⑤_部材在庫No1");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI5_TYOUGOURYOU1, "添加材⑤_調合量1");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI5_BUZAIZAIKOLOTNO2, "添加材⑤_部材在庫No2");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI5_TYOUGOURYOU2, "添加材⑤_調合量2");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI6_ZAIRYOUMEI, "添加材⑥_材料品名");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI6_TYOUGOURYOUKIKAKU, "添加材⑥_調合量規格");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI6_BUZAIZAIKOLOTNO1, "添加材⑥_部材在庫No1");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI6_TYOUGOURYOU1, "添加材⑥_調合量1");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI6_BUZAIZAIKOLOTNO2, "添加材⑥_部材在庫No2");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI6_TYOUGOURYOU2, "添加材⑥_調合量2");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI7_ZAIRYOUMEI, "添加材⑦_材料品名");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI7_TYOUGOURYOUKIKAKU, "添加材⑦_調合量規格");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI7_BUZAIZAIKOLOTNO1, "添加材⑦_部材在庫No1");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI7_TYOUGOURYOU1, "添加材⑦_調合量1");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI7_BUZAIZAIKOLOTNO2, "添加材⑦_部材在庫No2");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI7_TYOUGOURYOU2, "添加材⑦_調合量2");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI8_ZAIRYOUMEI, "添加材⑧_材料品名");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI8_TYOUGOURYOUKIKAKU, "添加材⑧_調合量規格");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI8_BUZAIZAIKOLOTNO1, "添加材⑧_部材在庫No1");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI8_TYOUGOURYOU1, "添加材⑧_調合量1");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI8_BUZAIZAIKOLOTNO2, "添加材⑧_部材在庫No2");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI8_TYOUGOURYOU2, "添加材⑧_調合量2");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI9_ZAIRYOUMEI, "添加材⑨_材料品名");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI9_TYOUGOURYOUKIKAKU, "添加材⑨_調合量規格");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI9_BUZAIZAIKOLOTNO1, "添加材⑨_部材在庫No1");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI9_TYOUGOURYOU1, "添加材⑨_調合量1");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI9_BUZAIZAIKOLOTNO2, "添加材⑨_部材在庫No2");
        allItemIdMap.put(GXHDO102B008Const.TENKAZAI9_TYOUGOURYOU2, "添加材⑨_調合量2");
        allItemIdMap.put(GXHDO102B008Const.HYOURYOUSYUURYOU_DAY, "秤量終了日");
        allItemIdMap.put(GXHDO102B008Const.HYOURYOUSYUURYOU_TIME, "秤量終了時間");
        allItemIdMap.put(GXHDO102B008Const.TANTOUSYA, "担当者");
        allItemIdMap.put(GXHDO102B008Const.KAKUNINSYA, "確認者");
        allItemIdMap.put(GXHDO102B008Const.BIKOU1, "備考1");
        allItemIdMap.put(GXHDO102B008Const.BIKOU2, "備考2");

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
}