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
import jp.co.kccs.xhd.db.model.SikakariJson;
import jp.co.kccs.xhd.db.model.SrSlipYouzaihyouryouTounyuuSutenyouki;
import jp.co.kccs.xhd.db.model.SubSrSlipYouzaihyouryouTounyuuSutenyouki;
import jp.co.kccs.xhd.model.GXHDO102C015Model;
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
 * 変更日	2021/12/10<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B030(ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器))
 *
 * @author KCSS K.Jo
 * @since 2021/12/10
 */
public class GXHDO102B030 implements IFormLogic {

    private static final Logger LOGGER = Logger.getLogger(GXHDO102B030.class.getName());
    private static final String JOTAI_FLG_KARI_TOROKU = "0";
    private static final String JOTAI_FLG_TOROKUZUMI = "1";
    private static final String JOTAI_FLG_SAKUJO = "9";
    private static final String SQL_STATE_RECORD_LOCK_ERR = "55P03";

    /**
     * コンストラクタ
     */
    public GXHDO102B030() {
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
            initGXHDO102B030A(processData);

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
                    GXHDO102B030Const.BTN_EDABAN_COPY_TOP,
                    GXHDO102B030Const.BTN_YOUZAIKEIRYOU_TOP,
                    GXHDO102B030Const.BTN_KAKUHANKAISI_TOP,
                    GXHDO102B030Const.BTN_KAKUHANSYUURYOU_TOP,
                    GXHDO102B030Const.BTN_EDABAN_COPY_BOTTOM,
                    GXHDO102B030Const.BTN_YOUZAIKEIRYOU_BOTTOM,
                    GXHDO102B030Const.BTN_KAKUHANKAISI_BOTTOM,
                    GXHDO102B030Const.BTN_KAKUHANSYUURYOU_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO102B030Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO102B030Const.BTN_INSERT_TOP,
                    GXHDO102B030Const.BTN_DELETE_TOP,
                    GXHDO102B030Const.BTN_UPDATE_TOP,
                    GXHDO102B030Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO102B030Const.BTN_INSERT_BOTTOM,
                    GXHDO102B030Const.BTN_DELETE_BOTTOM,
                    GXHDO102B030Const.BTN_UPDATE_BOTTOM
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
            case GXHDO102B030Const.BTN_EDABAN_COPY_TOP:
            case GXHDO102B030Const.BTN_EDABAN_COPY_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 仮登録
            case GXHDO102B030Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO102B030Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO102B030Const.BTN_INSERT_TOP:
            case GXHDO102B030Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 修正
            case GXHDO102B030Const.BTN_UPDATE_TOP:
            case GXHDO102B030Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO102B030Const.BTN_DELETE_TOP:
            case GXHDO102B030Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 溶剤調整量計算
            case GXHDO102B030Const.BTN_YOUZAITYOUSEIRYOU_TOP:
            case GXHDO102B030Const.BTN_YOUZAITYOUSEIRYOU_BOTTOM:
                method = "setYouzaityouseiryou";
                break;
            // 溶剤秤量日時
            case GXHDO102B030Const.BTN_YOUZAIKEIRYOU_TOP:
            case GXHDO102B030Const.BTN_YOUZAIKEIRYOU_BOTTOM:
                method = "setYouzaikeiryouDateTime";
                break;
            // 撹拌開始日時
            case GXHDO102B030Const.BTN_KAKUHANKAISI_TOP:
            case GXHDO102B030Const.BTN_KAKUHANKAISI_BOTTOM:
                method = "setKakuhankaisiDateTime";
                break;
            // 撹拌終了日時
            case GXHDO102B030Const.BTN_KAKUHANSYUURYOU_TOP:
            case GXHDO102B030Const.BTN_KAKUHANSYUURYOU_BOTTOM:
                method = "setKakuhansyuuryouDateTime";
                break;
            // 誘電体ｽﾗﾘｰ_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B030Const.BTN_OPENC015SUBGAMEN1:
                method = "openC015SubGamen1";
                break;
            // 分散材1_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B030Const.BTN_OPENC015SUBGAMEN2:
                method = "openC015SubGamen2";
                break;
            // 分散材2_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B030Const.BTN_OPENC015SUBGAMEN3:
                method = "openC015SubGamen3";
                break;
            // 溶剤1_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B030Const.BTN_OPENC015SUBGAMEN4:
                method = "openC015SubGamen4";
                break;
            // 溶剤2_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B030Const.BTN_OPENC015SUBGAMEN5:
                method = "openC015SubGamen5";
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

            // ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)の入力項目の登録データ(仮登録時は仮登録データ)を取得
            List<SrSlipYouzaihyouryouTounyuuSutenyouki> srSlipYouzaihyouryouTounyuuSutenyoukiDataList = getSrSlipYouzaihyouryouTounyuuSutenyoukiData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo9, oyalotEdaban);
            if (srSlipYouzaihyouryouTounyuuSutenyoukiDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }
            // ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力_ｻﾌﾞ画面データ取得
            List<SubSrSlipYouzaihyouryouTounyuuSutenyouki> subSrSlipYouzaihyouryouTounyuuSutenyoukiDataList = getSubSrSlipYouzaihyouryouTounyuuSutenyoukiData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo9, oyalotEdaban);
            if (subSrSlipYouzaihyouryouTounyuuSutenyoukiDataList.isEmpty() || subSrSlipYouzaihyouryouTounyuuSutenyoukiDataList.size() != 5) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }
            // メイン画面データ設定
            setInputItemDataMainForm(processData, srSlipYouzaihyouryouTounyuuSutenyoukiDataList.get(0), jotaiFlg);
            // ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力_ｻﾌﾞ画面データ設定
            setInputItemDataSubFormC015(processData, subSrSlipYouzaihyouryouTounyuuSutenyoukiDataList);

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
        List<String> tyogouryoukikaku = Arrays.asList(GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOUKIKAKU, GXHDO102B030Const.ZUNSANZAI1_TYOUGOURYOUKIKAKU,
                GXHDO102B030Const.ZUNSANZAI2_TYOUGOURYOUKIKAKU, GXHDO102B030Const.YOUZAI1_TYOUGOURYOUKIKAKU, GXHDO102B030Const.YOUZAI2_TYOUGOURYOUKIKAKU);
        List<String> tyougouryouList1 = Arrays.asList(GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOU1, GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOU2); // 誘電体ｽﾗﾘｰ_調合量
        List<String> tyougouryouList2 = Arrays.asList(GXHDO102B030Const.ZUNSANZAI1_TYOUGOURYOU1, GXHDO102B030Const.ZUNSANZAI1_TYOUGOURYOU2); // 分散材1_調合量
        List<String> tyougouryouList3 = Arrays.asList(GXHDO102B030Const.ZUNSANZAI2_TYOUGOURYOU1, GXHDO102B030Const.ZUNSANZAI2_TYOUGOURYOU2); // 分散材2_調合量
        List<String> tyougouryouList4 = Arrays.asList(GXHDO102B030Const.YOUZAI1_TYOUGOURYOU1, GXHDO102B030Const.YOUZAI1_TYOUGOURYOU2); // 溶剤1_調合量
        List<String> tyougouryouList5 = Arrays.asList(GXHDO102B030Const.YOUZAI2_TYOUGOURYOU1, GXHDO102B030Const.YOUZAI2_TYOUGOURYOU2); // 溶剤2_調合量

        // 規格値の入力値チェック必要の項目リスト
        List<FXHDD01> itemList = new ArrayList<>();
        setKikakuValueAndLabel1(processData, itemList, tyougouryouList1, tyogouryoukikaku.get(0), "誘電体ｽﾗﾘｰ_調合量"); // 誘電体ｽﾗﾘｰ_調合量の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, tyougouryouList2, tyogouryoukikaku.get(1), "分散材①_調合量"); // 分散材①_調合量の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, tyougouryouList3, tyogouryoukikaku.get(2), "分散材②_調合量"); // 分散材②_調合量の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, tyougouryouList4, tyogouryoukikaku.get(3), "溶剤①_調合量"); // 溶剤①_調合量の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, tyougouryouList5, tyogouryoukikaku.get(4), "溶剤②_調合量"); // 溶剤②_調合量の規格値と表示ﾗﾍﾞﾙ1を設置

        tyougouryouList.addAll(tyougouryouList1);
        tyougouryouList.addAll(tyougouryouList2);
        tyougouryouList.addAll(tyougouryouList3);
        tyougouryouList.addAll(tyougouryouList4);
        tyougouryouList.addAll(tyougouryouList5);

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

                // ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)_仮登録処理
                insertTmpSrSlipYouzaihyouryouTounyuuSutenyouki(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, strSystime, processData);
                // ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力ｻﾌﾞ画面の仮登録処理
                for (int i = 1; i <= 5; i++) {
                    insertTmpSubSrSlipYouzaihyouryouTounyuuSutenyouki(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, i, strSystime, processData);
                }
            } else {

                // ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)_仮登録更新処理
                SrSlipYouzaihyouryouTounyuuSutenyouki srSlipYouzaihyouryouTounyuuSutenyouki = updateTmpSrSlipYouzaihyouryouTounyuuSutenyouki(queryRunnerQcdb, conQcdb, rev,
                        processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);
                // ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力ｻﾌﾞ画面の仮登録更新処理
                for (int i = 1; i <= 5; i++) {
                    updateTmpSubSrSlipYouzaihyouryouTounyuuSutenyouki(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, i, strSystime, srSlipYouzaihyouryouTounyuuSutenyouki, processData);
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
        // 撹拌開始日時、撹拌終了日時前後チェック
        FXHDD01 itemkaisiDay = getItemRow(processData.getItemList(), GXHDO102B030Const.KAKUHANKAISI_DAY); // 撹拌開始日
        FXHDD01 itemkaisiTime = getItemRow(processData.getItemList(), GXHDO102B030Const.KAKUHANKAISI_TIME); // 撹拌開始時間
        FXHDD01 itemsyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B030Const.KAKUHANSYUURYOU_DAY); //撹拌終了日
        FXHDD01 itemsyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B030Const.KAKUHANSYUURYOU_TIME); //撹拌終了時間
        if (itemkaisiDay != null && itemkaisiTime != null && itemsyuuryouDay != null && itemsyuuryouTime != null) {
            Date kaisiDate = DateUtil.convertStringToDate(itemkaisiDay.getValue(), itemkaisiTime.getValue());
            Date syuuryouDate = DateUtil.convertStringToDate(itemsyuuryouDay.getValue(), itemsyuuryouTime.getValue());
            //R001チェック呼出し
            String msgCheckR001 = validateUtil.checkR001(itemkaisiDay.getLabel1() + "時", kaisiDate, itemsyuuryouDay.getLabel1() + "時", syuuryouDate);
            if (!StringUtil.isEmpty(msgCheckR001)) {
                //エラー発生時
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemkaisiDay, itemkaisiTime, itemsyuuryouDay, itemsyuuryouTime);
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
            SrSlipYouzaihyouryouTounyuuSutenyouki tmpSrSlipYouzaihyouryouTounyuuSutenyouki = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrSlipYouzaihyouryouTounyuuSutenyouki> srSlipYouzaihyouryouTounyuuSutenyoukiList = getSrSlipYouzaihyouryouTounyuuSutenyoukiData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo9, edaban);
                if (!srSlipYouzaihyouryouTounyuuSutenyoukiList.isEmpty()) {
                    tmpSrSlipYouzaihyouryouTounyuuSutenyouki = srSlipYouzaihyouryouTounyuuSutenyoukiList.get(0);
                }

                deleteTmpSrSlipYouzaihyouryouTounyuuSutenyouki(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);
                deleteTmpSubSrSlipYouzaihyouryouTounyuuSutenyouki(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);
            }

            // ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)_登録処理
            insertSrSlipYouzaihyouryouTounyuuSutenyouki(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, strSystime, processData, tmpSrSlipYouzaihyouryouTounyuuSutenyouki);
            // ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力ｻﾌﾞ画面の仮登録更新処理
            for (int i = 1; i <= 5; i++) {
                insertSubSrSlipYouzaihyouryouTounyuuSutenyouki(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, i, strSystime, processData, tmpSrSlipYouzaihyouryouTounyuuSutenyouki);
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
        processData.setUserAuthParam(GXHDO102B030Const.USER_AUTH_UPDATE_PARAM);

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

            // ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)_更新処理
            SrSlipYouzaihyouryouTounyuuSutenyouki srSlipYouzaihyouryouTounyuuSutenyouki = updateSrSlipYouzaihyouryouTounyuuSutenyouki(queryRunnerQcdb, conQcdb, rev,
                    processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);
            // ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力ｻﾌﾞ画面の更新処理
            for (int i = 1; i <= 5; i++) {
                updateSubSrSlipYouzaihyouryouTounyuuSutenyouki(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, i, strSystime, srSlipYouzaihyouryouTounyuuSutenyouki, processData);
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
        processData.setUserAuthParam(GXHDO102B030Const.USER_AUTH_DELETE_PARAM);

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

            // ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo9, edaban, paramJissekino);
            insertDeleteDataTmpSrSlipYouzaihyouryouTounyuuSutenyouki(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo9, edaban, strSystime);

            // ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力_ｻﾌﾞ画面仮登録登録処理
            insertDeleteDataTmpSubSrSlipYouzaihyouryouTounyuuSutenyouki(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo9, edaban, strSystime);

            // ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)_削除処理
            deleteSrSlipYouzaihyouryouTounyuuSutenyouki(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);

            // ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力_ｻﾌﾞ画面削除処理
            deleteSubSrSlipYouzaihyouryouTounyuuSutenyouki(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);

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
     * 溶剤調整量計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setYouzaityouseiryou(ProcessData processData) {
        ErrorMessageInfo checkItemErrorInfo = checkYouzaityouseiryouKeisan(processData);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        clearItemListBackColor(processData);
        // 溶剤調整量
        FXHDD01 youzaityouseiryou = getItemRow(processData.getItemList(), GXHDO102B030Const.YOUZAITYOUSEIRYOU);
        try {
            QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
            QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
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
            String oyalotEdaban = StringUtil.nullToBlank(getMapData(shikakariData, "oyalotedaban")); //親ﾛｯﾄ枝番
            // [原材料品質DB登録実績]から、ﾃﾞｰﾀを取得
            Map fxhdd11RevInfo = loadFxhdd11RevInfo(queryRunnerDoc, kojyo, lotNo9, oyalotEdaban, paramJissekino, formId);
            String rev = StringUtil.nullToBlank(getMapData(fxhdd11RevInfo, "rev")); //親ﾛｯﾄ枝番
            // (19)[ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)]から、ﾃﾞｰﾀを取得
            Map srSlipSlurrykokeibuntyouseiSutenyoukiData = loadSrSlipSlurrykokeibuntyouseiSutenyouki(queryRunnerQcdb, kojyo, lotNo9, oyalotEdaban, rev);
            if (srSlipSlurrykokeibuntyouseiSutenyoukiData == null || srSlipSlurrykokeibuntyouseiSutenyoukiData.isEmpty()) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                ErrorMessageInfo checkItemError = MessageUtil.getErrorMessageInfo("XHD-000210", true, true, null, "ﾃﾞｰﾀ");
                if (checkItemError != null) {
                    processData.setErrorMessageInfoList(Arrays.asList(checkItemError));
                    return processData;
                }
            } else {
                String kokeibunhiritu = StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyoukiData.get("kokeibunhiritu"));// 固形分比率
                //「固形分比率」が0またはNULLだった場合
                if (NumberUtil.isZeroOrEmpty(kokeibunhiritu)) {
                    // ｴﾗｰ項目をﾘｽﾄに追加
                    ErrorMessageInfo checkItemError = MessageUtil.getErrorMessageInfo("XHD-000210", true, true, null, "固形分比率");
                    if (checkItemError != null) {
                        processData.setErrorMessageInfoList(Arrays.asList(checkItemError));
                        return processData;
                    }
                }
            }

            HashMap<String, String> kikakuMap = new HashMap<>();
            int kikakuti = 0;
            // 規格値取得
            getKikakuValue(queryRunnerQcdb, shikakariData, lotNo, kikakuMap);
            if (!StringUtil.isEmpty(StringUtil.nullToBlank(kikakuMap.get("errorMessage")))) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                ErrorMessageInfo checkItemError = MessageUtil.getErrorMessageInfo("XHD-000028", true, false, null, "規格情報");
                if (checkItemError != null) {
                    processData.setErrorMessageInfoList(Arrays.asList(checkItemError));
                    return processData;
                }
            }else{
                kikakuti = Integer.parseInt(kikakuMap.get("kikakuti"));
            }

            calcYouzaityouseiryou(processData, srSlipSlurrykokeibuntyouseiSutenyoukiData, kikakuti);
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
     * 規格値取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト(Qcdb)
     * @param shikakariData 前工程WIPから仕掛情報
     * @param lotNo ﾛｯﾄNo
     * @param kikakutiMap 規格値マップ
     * @throws SQLException 例外エラー
     */
    protected void getKikakuValue(QueryRunner queryRunnerQcdb, Map shikakariData, String lotNo, HashMap<String, String> kikakutiMap) throws SQLException {
        String kojyo = lotNo.substring(0, 3);
        String lotNo9 = lotNo.substring(3, 12);
        String edaban = lotNo.substring(12, 15);
        String syurui = "ｽﾘｯﾌﾟ作製";
        kikakutiMap.put("errorMessage", "");
        kikakutiMap.put("kikakuti", "0");
        // [前工程設計]から、ﾃﾞｰﾀを取得
        Map daMkSekKeiData = loadDaMkSekKeiData(queryRunnerQcdb, kojyo, lotNo9, edaban, syurui);
        if (daMkSekKeiData == null || daMkSekKeiData.isEmpty()) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            kikakutiMap.put("errorMessage", "error");
            return;
        }

        // 設計No
        String sekkeiNo = StringUtil.nullToBlank(getMapData(daMkSekKeiData, "sekkeiNo"));
        // ﾊﾟﾀｰﾝ
        String pattern = StringUtil.nullToBlank(getMapData(daMkSekKeiData, "pattern"));
        String kikakuti;
        // [前工程規格情報]から、ﾃﾞｰﾀを取得
        Map daMkJokenData = loadDaMkJokenData(queryRunnerQcdb, sekkeiNo);
        if (daMkJokenData == null || daMkJokenData.isEmpty()) {
            // [前工程標準規格情報]から、ﾃﾞｰﾀを取得
            Map daMkhYoJunJokenData = loadDaMkhYoJunJokenData(queryRunnerQcdb, (String) shikakariData.get("hinmei"), pattern);
            if (daMkhYoJunJokenData == null || daMkhYoJunJokenData.isEmpty()) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                kikakutiMap.put("errorMessage", "error");
                return;
            }
            // 前工程規格情報の規格値
            kikakuti = StringUtil.nullToBlank(getMapData(daMkhYoJunJokenData, "kikakuti"));
            if (!NumberUtil.isIntegerNumeric(kikakuti)) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                kikakutiMap.put("errorMessage", "error");
                return;
            }
        } else {
            // 前工程規格情報の規格値
            kikakuti = StringUtil.nullToBlank(getMapData(daMkJokenData, "kikakuti"));
            if (!NumberUtil.isIntegerNumeric(kikakuti)) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                kikakutiMap.put("errorMessage", "error");
                return;
            }
        }
        kikakutiMap.put("kikakuti", kikakuti);
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
        params.add("ｽﾘｯﾌﾟ作製");
        params.add("ﾊﾞｲﾝﾀﾞｰ秤量・投入");
        params.add("ﾊﾞｲﾝﾀﾞｰ添加量規格");
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
    private Map loadDaMkhYoJunJokenData(QueryRunner queryRunnerQcdb, String hinmei, String pattern) throws SQLException {
        // 前工程標準規格情報データの取得
        String sql = "SELECT kikakuti FROM da_mkhyojunjoken"
                + " WHERE hinmei = ? AND pattern = ? AND kouteimei = ?  AND koumokumei = ? AND kanrikoumokumei = ?";
        List<Object> params = new ArrayList<>();
        params.add(hinmei);
        params.add(pattern);
        params.add("ｽﾘｯﾌﾟ作製");
        params.add("ﾊﾞｲﾝﾀﾞｰ秤量・投入");
        params.add("ﾊﾞｲﾝﾀﾞｰ添加量規格");
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * 溶剤調整量計算ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
     *
     * @param processData 処理制御データ
     * @return エラーメッセージ情報
     */
    public ErrorMessageInfo checkYouzaityouseiryouKeisan(ProcessData processData) {
        // 誘電体ｽﾗﾘｰ_調合量1
        FXHDD01 tyougouryou1 = getItemRow(processData.getItemList(), GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOU1);
        // 誘電体ｽﾗﾘｰ_調合量2
        FXHDD01 tyougouryou2 = getItemRow(processData.getItemList(), GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOU2);
        //「誘電体ｽﾗﾘｰ_調合量1」「誘電体ｽﾗﾘｰ_調合量2」ﾁｪｯｸ
        if (tyougouryou1 != null && tyougouryou2 != null && NumberUtil.isZeroOrEmpty(tyougouryou1.getValue())
                && NumberUtil.isZeroOrEmpty(tyougouryou2.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(tyougouryou1, tyougouryou2);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, true, errFxhdd01List, "「誘電体ｽﾗﾘｰ_調合量1」「誘電体ｽﾗﾘｰ_調合量2」");
        }
        return null;
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
     * 溶剤調整量計算
     *
     * @param processData 処理制御データ
     * @param srSlipSlurrykokeibuntyouseiSutenyoukiData ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)ﾃﾞｰﾀ
     * @param srSlipBinderhyouryouTounyuuData ﾊﾞｲﾝﾀﾞｰ秤量・投入ﾃﾞｰﾀ
     */
    private void calcYouzaityouseiryou(ProcessData processData, Map srSlipSlurrykokeibuntyouseiSutenyoukiData, int kikakuti) {
        // 誘電体ｽﾗﾘｰ_調合量1
        FXHDD01 tyougouryou1 = getItemRow(processData.getItemList(), GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOU1);
        // 誘電体ｽﾗﾘｰ_調合量2
        FXHDD01 tyougouryou2 = getItemRow(processData.getItemList(), GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOU2);
        // 溶剤調整量
        FXHDD01 youzaityouseiryou = getItemRow(processData.getItemList(), GXHDO102B030Const.YOUZAITYOUSEIRYOU);
        // 誘電体ｽﾗﾘｰ_調合量規格
        FXHDD01 yuudentaislurryTyougouryoukikaku = getItemRow(processData.getItemList(), GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOUKIKAKU);
        // ﾄﾙｴﾝ調整量
        FXHDD01 toluenetyouseiryou = getItemRow(processData.getItemList(), GXHDO102B030Const.TOLUENETYOUSEIRYOU);
        // ｿﾙﾐｯｸｽ調整量
        FXHDD01 solmixtyouseiryou = getItemRow(processData.getItemList(), GXHDO102B030Const.SOLMIXTYOUSEIRYOU);
        // 分散材1_調合量規格
        FXHDD01 zunsanzai1Tyougouryoukikaku = getItemRow(processData.getItemList(), GXHDO102B030Const.ZUNSANZAI1_TYOUGOURYOUKIKAKU);
        // 分散材2_調合量規格
        FXHDD01 zunsanzai2Tyougouryoukikaku = getItemRow(processData.getItemList(), GXHDO102B030Const.ZUNSANZAI2_TYOUGOURYOUKIKAKU);
        // 溶剤1_調合量規格
        FXHDD01 youzai1Tyougouryoukikaku = getItemRow(processData.getItemList(), GXHDO102B030Const.YOUZAI1_TYOUGOURYOUKIKAKU);
        // 溶剤2_調合量規格
        FXHDD01 youzai2Tyougouryoukikaku = getItemRow(processData.getItemList(), GXHDO102B030Const.YOUZAI2_TYOUGOURYOUKIKAKU);
        try {
            if (youzaityouseiryou != null) {
                BigDecimal tyougouryou1Val = BigDecimal.ZERO; //誘電体ｽﾗﾘｰ_調合量1
                BigDecimal tyougouryou2Val = BigDecimal.ZERO; //誘電体ｽﾗﾘｰ_調合量2
                // 誘電体ｽﾗﾘｰ_調合量1の取得値
                if (tyougouryou1 != null && !NumberUtil.isZeroOrEmpty(tyougouryou1.getValue())) {
                    tyougouryou1Val = new BigDecimal(tyougouryou1.getValue());
                }
                // 誘電体ｽﾗﾘｰ_調合量2の取得値
                if (tyougouryou2 != null && !NumberUtil.isZeroOrEmpty(tyougouryou2.getValue())) {
                    tyougouryou2Val = new BigDecimal(tyougouryou2.getValue());
                }
                // 「ｽﾗﾘｰ重量」 = 「誘電体ｽﾗﾘｰ_調合量1」＋「誘電体ｽﾗﾘｰ_調合量2」
                BigDecimal slurryjyuuryou = tyougouryou1Val.add(tyougouryou2Val);
                BigDecimal dassikokeibun = new BigDecimal(String.valueOf(srSlipSlurrykokeibuntyouseiSutenyoukiData.get("dassikokeibun")));// 脱脂固形分
                BigDecimal slipyouzaityouseiryouVal = new BigDecimal(String.valueOf(srSlipSlurrykokeibuntyouseiSutenyoukiData.get("youzaityouseiryou")));// 溶剤調整量
                BigDecimal kokeibunhiritu = new BigDecimal(String.valueOf(srSlipSlurrykokeibuntyouseiSutenyoukiData.get("kokeibunhiritu")));// 固形分比率
                BigDecimal bindertenkaryoukikaku = new BigDecimal(kikakuti);// ﾊﾞｲﾝﾀﾞｰ添加量規格
                //「溶剤調整量」= 「脱脂固形分」 × (「ｽﾗﾘｰ重量」 + 「溶剤調整量」) ÷ 「固形分比率」 - 「ｽﾗﾘｰ重量」 - 「ﾊﾞｲﾝﾀﾞｰ添加量規格」
                BigDecimal youzaityouseiryouVal = dassikokeibun.multiply(slurryjyuuryou.add(slipyouzaityouseiryouVal))
                        .divide(kokeibunhiritu, 2, RoundingMode.DOWN).subtract(slurryjyuuryou).subtract(bindertenkaryoukikaku);
                // 計算結果の設定
                youzaityouseiryou.setValue(youzaityouseiryouVal.setScale(0, RoundingMode.HALF_UP).toPlainString());
                // 溶剤調整量 ÷ 2
                BigDecimal youzaityouseiryou2Val = youzaityouseiryouVal.divide(new BigDecimal(2), 0, RoundingMode.HALF_UP);
                // 誘電体ｽﾗﾘｰ_調合量規格、分散材1_調合量規格、分散材2_調合量規格、溶剤1_調合量規格、溶剤2_調合量規格に調合量の計算結果後ろに「±0」を付けて設定する。
                String youzaityouseiryou2ValStr = "【" + youzaityouseiryou2Val.toPlainString() + "±0" + "】";
                // 誘電体ｽﾗﾘｰ_調合量規格
                if (yuudentaislurryTyougouryoukikaku != null) {
                    //計算結果の設定
                    yuudentaislurryTyougouryoukikaku.setKikakuChi(youzaityouseiryou2ValStr);
                }
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
                // 分散材1_調合量規格
                if (zunsanzai1Tyougouryoukikaku != null) {
                    //計算結果の設定
                    zunsanzai1Tyougouryoukikaku.setKikakuChi(youzaityouseiryou2ValStr);
                }
                // 分散材2_調合量規格
                if (zunsanzai2Tyougouryoukikaku != null) {
                    //計算結果の設定
                    zunsanzai2Tyougouryoukikaku.setKikakuChi(youzaityouseiryou2ValStr);
                }
                // 溶剤1_調合量規格
                if (youzai1Tyougouryoukikaku != null) {
                    //計算結果の設定
                    youzai1Tyougouryoukikaku.setKikakuChi(youzaityouseiryou2ValStr);
                }
                // 溶剤2_調合量規格
                if (youzai2Tyougouryoukikaku != null) {
                    //計算結果の設定
                    youzai2Tyougouryoukikaku.setKikakuChi(youzaityouseiryou2ValStr);
                }
            }
        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
            ErrUtil.outputErrorLog(youzaityouseiryou.getLabel1() + "計算にエラー発生", ex, LOGGER);
        }
    }

    /**
     * 溶剤秤量日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setYouzaikeiryouDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B030Const.YOUZAIKEIRYOU_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B030Const.YOUZAIKEIRYOU_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
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
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B030Const.KAKUHANKAISI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B030Const.KAKUHANKAISI_TIME);
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
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B030Const.KAKUHANSYUURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B030Const.KAKUHANSYUURYOU_TIME);
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
                        GXHDO102B030Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B030Const.BTN_YOUZAITYOUSEIRYOU_TOP,
                        GXHDO102B030Const.BTN_YOUZAIKEIRYOU_TOP,
                        GXHDO102B030Const.BTN_KAKUHANKAISI_TOP,
                        GXHDO102B030Const.BTN_KAKUHANSYUURYOU_TOP,
                        GXHDO102B030Const.BTN_UPDATE_TOP,
                        GXHDO102B030Const.BTN_DELETE_TOP,
                        GXHDO102B030Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B030Const.BTN_YOUZAITYOUSEIRYOU_BOTTOM,
                        GXHDO102B030Const.BTN_YOUZAIKEIRYOU_BOTTOM,
                        GXHDO102B030Const.BTN_KAKUHANKAISI_BOTTOM,
                        GXHDO102B030Const.BTN_KAKUHANSYUURYOU_BOTTOM,
                        GXHDO102B030Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B030Const.BTN_DELETE_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B030Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B030Const.BTN_INSERT_TOP,
                        GXHDO102B030Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B030Const.BTN_INSERT_BOTTOM));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO102B030Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B030Const.BTN_YOUZAITYOUSEIRYOU_TOP,
                        GXHDO102B030Const.BTN_YOUZAIKEIRYOU_TOP,
                        GXHDO102B030Const.BTN_KAKUHANKAISI_TOP,
                        GXHDO102B030Const.BTN_KAKUHANSYUURYOU_TOP,
                        GXHDO102B030Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B030Const.BTN_INSERT_TOP,
                        GXHDO102B030Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B030Const.BTN_YOUZAITYOUSEIRYOU_BOTTOM,
                        GXHDO102B030Const.BTN_YOUZAIKEIRYOU_BOTTOM,
                        GXHDO102B030Const.BTN_KAKUHANKAISI_BOTTOM,
                        GXHDO102B030Const.BTN_KAKUHANSYUURYOU_BOTTOM,
                        GXHDO102B030Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B030Const.BTN_INSERT_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B030Const.BTN_UPDATE_TOP,
                        GXHDO102B030Const.BTN_DELETE_TOP,
                        GXHDO102B030Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B030Const.BTN_DELETE_BOTTOM
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
        GXHDO102C015Logic.setItemStyle(processData.getItemList());
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
        this.setItemData(processData, GXHDO102B030Const.WIPLOTNO, lotNo);
        // ｽﾘｯﾌﾟ品名
        this.setItemData(processData, GXHDO102B030Const.SLIPHINMEI, StringUtil.nullToBlank(getMapData(shikakariData, "hinmei")));
        // ｽﾘｯﾌﾟLotNo
        this.setItemData(processData, GXHDO102B030Const.SLIPLOTNO, StringUtil.nullToBlank(getMapData(shikakariData, "lotno")));
        // ﾛｯﾄ区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode"));
        // ﾛｯﾄ区分名称
        String lotkubun = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubun"));

        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO102B030Const.LOTKUBUN, "");
        } else {
            if (!StringUtil.isEmpty(lotkubun)) {
                lotkubuncode = lotkubuncode + ":" + lotkubun;
            }
            this.setItemData(processData, GXHDO102B030Const.LOTKUBUN, lotkubuncode);
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

        List<SrSlipYouzaihyouryouTounyuuSutenyouki> srSlipYouzaihyouryouTounyuuSutenyoukiList = new ArrayList<>();
        List<SubSrSlipYouzaihyouryouTounyuuSutenyouki> subSrSlipYouzaihyouryouTounyuuSutenyoukiList = new ArrayList<>();
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

                // ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力_ｻﾌﾞ画面データ設定
                setInputItemDataSubFormC015(processData, null);
                return true;
            }

            // ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)データ取得
            srSlipYouzaihyouryouTounyuuSutenyoukiList = getSrSlipYouzaihyouryouTounyuuSutenyoukiData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo9, edaban);
            if (srSlipYouzaihyouryouTounyuuSutenyoukiList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力_サブ画面データ取得
            subSrSlipYouzaihyouryouTounyuuSutenyoukiList = getSubSrSlipYouzaihyouryouTounyuuSutenyoukiData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo9, edaban);
            if (subSrSlipYouzaihyouryouTounyuuSutenyoukiList.isEmpty() || subSrSlipYouzaihyouryouTounyuuSutenyoukiList.size() != 5) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }
            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srSlipYouzaihyouryouTounyuuSutenyoukiList.isEmpty() || (subSrSlipYouzaihyouryouTounyuuSutenyoukiList.isEmpty() || subSrSlipYouzaihyouryouTounyuuSutenyoukiList.size() != 5)) {
            return false;
        }
        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srSlipYouzaihyouryouTounyuuSutenyoukiList.get(0), jotaiFlg);
        // ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力_ｻﾌﾞ画面データ設定
        setInputItemDataSubFormC015(processData, subSrSlipYouzaihyouryouTounyuuSutenyoukiList);
        return true;

    }

    /**
     * データ設定処理
     *
     * @param processData 処理制御データ
     * @param srSlipYouzaihyouryouTounyuuSutenyouki ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrSlipYouzaihyouryouTounyuuSutenyouki srSlipYouzaihyouryouTounyuuSutenyouki, String jotaiFlg) {

        // 秤量号機
        this.setItemData(processData, GXHDO102B030Const.GOKI, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.GOKI, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 誘電体ｽﾗﾘｰ_部材在庫No1
        this.setItemData(processData, GXHDO102B030Const.YUUDENTAISLURRY_BUZAIZAIKOLOTNO1,
                getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.YUUDENTAISLURRY_BUZAIZAIKOLOTNO1, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 誘電体ｽﾗﾘｰ_調合量1
        this.setItemData(processData, GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOU1,
                getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOU1, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 仮登録・登録済みの場合、XXX_調合量規格が初期値で表示
        if (JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg) || JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            // 誘電体ｽﾗﾘｰ_調合量規格
            this.setItemKikuchiData(processData, GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOUKIKAKU,
                    getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOUKIKAKU, srSlipYouzaihyouryouTounyuuSutenyouki));

            // 分散材1_調合量規格
            this.setItemKikuchiData(processData, GXHDO102B030Const.ZUNSANZAI1_TYOUGOURYOUKIKAKU,
                    getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.ZUNSANZAI1_TYOUGOURYOUKIKAKU, srSlipYouzaihyouryouTounyuuSutenyouki));

            // 分散材2_調合量規格
            this.setItemKikuchiData(processData, GXHDO102B030Const.ZUNSANZAI2_TYOUGOURYOUKIKAKU,
                    getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.ZUNSANZAI2_TYOUGOURYOUKIKAKU, srSlipYouzaihyouryouTounyuuSutenyouki));

            // 溶剤1_調合量規格
            this.setItemKikuchiData(processData, GXHDO102B030Const.YOUZAI1_TYOUGOURYOUKIKAKU,
                    getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.YOUZAI1_TYOUGOURYOUKIKAKU, srSlipYouzaihyouryouTounyuuSutenyouki));

            // 溶剤2_調合量規格
            this.setItemKikuchiData(processData, GXHDO102B030Const.YOUZAI2_TYOUGOURYOUKIKAKU,
                    getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.YOUZAI2_TYOUGOURYOUKIKAKU, srSlipYouzaihyouryouTounyuuSutenyouki));

        }
        // 誘電体ｽﾗﾘｰ_部材在庫No2
        this.setItemData(processData, GXHDO102B030Const.YUUDENTAISLURRY_BUZAIZAIKOLOTNO2,
                getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.YUUDENTAISLURRY_BUZAIZAIKOLOTNO2, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 誘電体ｽﾗﾘｰ_調合量2
        this.setItemData(processData, GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOU2,
                getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOU2, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 担当者
        this.setItemData(processData, GXHDO102B030Const.TANTOUSYA, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.TANTOUSYA, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 誘電体ｽﾗﾘｰ投入①
        this.setItemData(processData, GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU1, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU1, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 誘電体ｽﾗﾘｰ投入②
        this.setItemData(processData, GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU2, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU2, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 誘電体ｽﾗﾘｰ投入③
        this.setItemData(processData, GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU3, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU3, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 誘電体ｽﾗﾘｰ投入④
        this.setItemData(processData, GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU4, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU4, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 誘電体ｽﾗﾘｰ投入⑤
        this.setItemData(processData, GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU5, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU5, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 誘電体ｽﾗﾘｰ投入⑥
        this.setItemData(processData, GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU6, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU6, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 誘電体ｽﾗﾘｰ投入担当者
        this.setItemData(processData, GXHDO102B030Const.YUUDENTAISLURRYTOUNYUUTANTOUSYA,
                getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.YUUDENTAISLURRYTOUNYUUTANTOUSYA, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 溶剤調整量
        this.setItemData(processData, GXHDO102B030Const.YOUZAITYOUSEIRYOU, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.YOUZAITYOUSEIRYOU, srSlipYouzaihyouryouTounyuuSutenyouki));

        // ﾄﾙｴﾝ調整量
        this.setItemData(processData, GXHDO102B030Const.TOLUENETYOUSEIRYOU, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.TOLUENETYOUSEIRYOU, srSlipYouzaihyouryouTounyuuSutenyouki));

        // ｿﾙﾐｯｸｽ調整量
        this.setItemData(processData, GXHDO102B030Const.SOLMIXTYOUSEIRYOU, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.SOLMIXTYOUSEIRYOU, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 溶剤秤量日
        this.setItemData(processData, GXHDO102B030Const.YOUZAIKEIRYOU_DAY, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.YOUZAIKEIRYOU_DAY, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 溶剤秤量時間
        this.setItemData(processData, GXHDO102B030Const.YOUZAIKEIRYOU_TIME, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.YOUZAIKEIRYOU_TIME, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 分散材①_部材在庫No1
        this.setItemData(processData, GXHDO102B030Const.ZUNSANZAI1_BUZAIZAIKOLOTNO1, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.ZUNSANZAI1_BUZAIZAIKOLOTNO1, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 分散材①_調合量1
        this.setItemData(processData, GXHDO102B030Const.ZUNSANZAI1_TYOUGOURYOU1, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.ZUNSANZAI1_TYOUGOURYOU1, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 分散材①_部材在庫No2
        this.setItemData(processData, GXHDO102B030Const.ZUNSANZAI1_BUZAIZAIKOLOTNO2, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.ZUNSANZAI1_BUZAIZAIKOLOTNO2, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 分散材①_調合量2
        this.setItemData(processData, GXHDO102B030Const.ZUNSANZAI1_TYOUGOURYOU2, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.ZUNSANZAI1_TYOUGOURYOU2, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 分散材②_部材在庫No1
        this.setItemData(processData, GXHDO102B030Const.ZUNSANZAI2_BUZAIZAIKOLOTNO1, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.ZUNSANZAI2_BUZAIZAIKOLOTNO1, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 分散材②_調合量1
        this.setItemData(processData, GXHDO102B030Const.ZUNSANZAI2_TYOUGOURYOU1, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.ZUNSANZAI2_TYOUGOURYOU1, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 分散材②_部材在庫No2
        this.setItemData(processData, GXHDO102B030Const.ZUNSANZAI2_BUZAIZAIKOLOTNO2, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.ZUNSANZAI2_BUZAIZAIKOLOTNO2, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 分散材②_調合量2
        this.setItemData(processData, GXHDO102B030Const.ZUNSANZAI2_TYOUGOURYOU2, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.ZUNSANZAI2_TYOUGOURYOU2, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 溶剤①_部材在庫No1
        this.setItemData(processData, GXHDO102B030Const.YOUZAI1_BUZAIZAIKOLOTNO1, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.YOUZAI1_BUZAIZAIKOLOTNO1, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 溶剤①_調合量1
        this.setItemData(processData, GXHDO102B030Const.YOUZAI1_TYOUGOURYOU1, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.YOUZAI1_TYOUGOURYOU1, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 溶剤①_部材在庫No2
        this.setItemData(processData, GXHDO102B030Const.YOUZAI1_BUZAIZAIKOLOTNO2, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.YOUZAI1_BUZAIZAIKOLOTNO2, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 溶剤①_調合量2
        this.setItemData(processData, GXHDO102B030Const.YOUZAI1_TYOUGOURYOU2, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.YOUZAI1_TYOUGOURYOU2, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 溶剤②_部材在庫No1
        this.setItemData(processData, GXHDO102B030Const.YOUZAI2_BUZAIZAIKOLOTNO1, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.YOUZAI2_BUZAIZAIKOLOTNO1, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 溶剤②_調合量1
        this.setItemData(processData, GXHDO102B030Const.YOUZAI2_TYOUGOURYOU1, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.YOUZAI2_TYOUGOURYOU1, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 溶剤②_部材在庫No2
        this.setItemData(processData, GXHDO102B030Const.YOUZAI2_BUZAIZAIKOLOTNO2, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.YOUZAI2_BUZAIZAIKOLOTNO2, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 溶剤②_調合量2
        this.setItemData(processData, GXHDO102B030Const.YOUZAI2_TYOUGOURYOU2, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.YOUZAI2_TYOUGOURYOU2, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 撹拌開始日
        this.setItemData(processData, GXHDO102B030Const.KAKUHANKAISI_DAY, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.KAKUHANKAISI_DAY, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 撹拌開始時間
        this.setItemData(processData, GXHDO102B030Const.KAKUHANKAISI_TIME, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.KAKUHANKAISI_TIME, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 撹拌終了日
        this.setItemData(processData, GXHDO102B030Const.KAKUHANSYUURYOU_DAY, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.KAKUHANSYUURYOU_DAY, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 撹拌終了時間
        this.setItemData(processData, GXHDO102B030Const.KAKUHANSYUURYOU_TIME, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.KAKUHANSYUURYOU_TIME, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 混合ﾀﾝｸNo
        this.setItemData(processData, GXHDO102B030Const.KONGOUTANKNO, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.KONGOUTANKNO, srSlipYouzaihyouryouTounyuuSutenyouki));

        // ﾀﾝｸ内洗浄確認
        this.setItemData(processData, GXHDO102B030Const.TANKNAISENJYOUKAKUNIN, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.TANKNAISENJYOUKAKUNIN, srSlipYouzaihyouryouTounyuuSutenyouki));

        // ﾀﾝｸ内内袋確認
        this.setItemData(processData, GXHDO102B030Const.TANKNAIUTIBUKUROKAKUNIN, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.TANKNAIUTIBUKUROKAKUNIN, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 撹拌羽根洗浄確認
        this.setItemData(processData, GXHDO102B030Const.KAKUHANHANESENJYOUKAKUNIN, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.KAKUHANHANESENJYOUKAKUNIN, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 撹拌軸洗浄確認
        this.setItemData(processData, GXHDO102B030Const.KAKUHANJIKUSENJYOUKAKUNIN, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.KAKUHANJIKUSENJYOUKAKUNIN, srSlipYouzaihyouryouTounyuuSutenyouki));

        // ﾀﾝｸにｱｰｽｸﾞﾘｯﾌﾟ接続
        this.setItemData(processData, GXHDO102B030Const.TANKNIEARTHGRIPSETUZOKU, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.TANKNIEARTHGRIPSETUZOKU, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 分散材溶剤投入
        this.setItemData(processData, GXHDO102B030Const.ZUNSANZAIYOUZAITOUNYUU, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.ZUNSANZAIYOUZAITOUNYUU, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 溶剤投入①
        this.setItemData(processData, GXHDO102B030Const.YOUZAITOUNYUU1, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.YOUZAITOUNYUU1, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 溶剤投入②
        this.setItemData(processData, GXHDO102B030Const.YOUZAITOUNYUU2, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.YOUZAITOUNYUU2, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 溶剤投入担当者
        this.setItemData(processData, GXHDO102B030Const.YOUZAITOUNYUUTANTOUSYA, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.YOUZAITOUNYUUTANTOUSYA, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 備考1
        this.setItemData(processData, GXHDO102B030Const.BIKOU1, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.BIKOU1, srSlipYouzaihyouryouTounyuuSutenyouki));

        // 備考2
        this.setItemData(processData, GXHDO102B030Const.BIKOU2, getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(GXHDO102B030Const.BIKOU2, srSlipYouzaihyouryouTounyuuSutenyouki));

    }

    /**
     * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)データ
     * @throws SQLException 例外エラー
     */
    private List<SrSlipYouzaihyouryouTounyuuSutenyouki> getSrSlipYouzaihyouryouTounyuuSutenyoukiData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrSlipYouzaihyouryouTounyuuSutenyouki(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSrSlipYouzaihyouryouTounyuuSutenyouki(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
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
     * [ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrSlipYouzaihyouryouTounyuuSutenyouki> loadSrSlipYouzaihyouryouTounyuuSutenyouki(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,goki,yuudentaislurryjyuuryou,yuudentaislurry_zairyouhinmei,yuudentaislurry_tyougouryoukikaku,"
                + "yuudentaislurry_buzaizaikolotno1,yuudentaislurry_tyougouryou1,yuudentaislurry_buzaizaikolotno2,yuudentaislurry_tyougouryou2,tantousya,yuudentaislurrytounyuu1,"
                + "yuudentaislurrytounyuu2,yuudentaislurrytounyuu3,yuudentaislurrytounyuu4,yuudentaislurrytounyuu5,yuudentaislurrytounyuu6,yuudentaislurrytounyuutantousya,"
                + "youzaityouseiryou,toluenetyouseiryou,solmixtyouseiryou,youzaikeiryounichiji,zunsanzai1_zairyouhinmei,zunsanzai1_tyougouryoukikaku,zunsanzai1_buzaizaikolotno1,"
                + "zunsanzai1_tyougouryou1,zunsanzai1_buzaizaikolotno2,zunsanzai1_tyougouryou2,zunsanzai2_zairyouhinmei,zunsanzai2_tyougouryoukikaku,zunsanzai2_buzaizaikolotno1,"
                + "zunsanzai2_tyougouryou1,zunsanzai2_buzaizaikolotno2,zunsanzai2_tyougouryou2,youzai1_zairyouhinmei,youzai1_tyougouryoukikaku,youzai1_buzaizaikolotno1,"
                + "youzai1_tyougouryou1,youzai1_buzaizaikolotno2,youzai1_tyougouryou2,youzai2_zairyouhinmei,youzai2_tyougouryoukikaku,youzai2_buzaizaikolotno1,youzai2_tyougouryou1,"
                + "youzai2_buzaizaikolotno2,youzai2_tyougouryou2,kakuhanki,kakuhanjikan,kakuhankaisinichiji,kakuhansyuuryounichiji,binderkongousetub,setubisize,binderkongougoki,"
                + "kongoutanksyurui,kongoutankno,tanknaisenjyoukakunin,tanknaiutibukurokakunin,kakuhanhanesenjyoukakunin,kakuhanjikusenjyoukakunin,tenryuubannotakasa,"
                + "tankniearthgripsetuzoku,zunsanzaiyouzaitounyuu,youzaitounyuu1,youzaitounyuu2,youzaitounyuutantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision "
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

        Map<String, String> mapping = new HashMap<>();
        mapping.put("kojyo", "kojyo");                                                               // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                                               // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                                             // 枝番
        mapping.put("sliphinmei", "sliphinmei");                                                     // ｽﾘｯﾌﾟ品名
        mapping.put("sliplotno", "sliplotno");                                                       // ｽﾘｯﾌﾟLotNo
        mapping.put("lotkubun", "lotkubun");                                                         // ﾛｯﾄ区分
        mapping.put("genryoukigou", "genryoukigou");                                                 // 原料記号
        mapping.put("goki", "goki");                                                                 // 秤量号機
        mapping.put("yuudentaislurryjyuuryou", "yuudentaislurryjyuuryou");                           // 誘電体ｽﾗﾘｰ重量
        mapping.put("yuudentaislurry_zairyouhinmei", "yuudentaislurry_zairyouhinmei");               // 誘電体ｽﾗﾘｰ_材料品名
        mapping.put("yuudentaislurry_tyougouryoukikaku", "yuudentaislurry_tyougouryoukikaku");       // 誘電体ｽﾗﾘｰ_調合量規格
        mapping.put("yuudentaislurry_buzaizaikolotno1", "yuudentaislurry_buzaizaikolotno1");         // 誘電体ｽﾗﾘｰ_部材在庫No1
        mapping.put("yuudentaislurry_tyougouryou1", "yuudentaislurry_tyougouryou1");                 // 誘電体ｽﾗﾘｰ_調合量1
        mapping.put("yuudentaislurry_buzaizaikolotno2", "yuudentaislurry_buzaizaikolotno2");         // 誘電体ｽﾗﾘｰ_部材在庫No2
        mapping.put("yuudentaislurry_tyougouryou2", "yuudentaislurry_tyougouryou2");                 // 誘電体ｽﾗﾘｰ_調合量2
        mapping.put("tantousya", "tantousya");                                                       // 担当者
        mapping.put("yuudentaislurrytounyuu1", "yuudentaislurrytounyuu1");                           // 誘電体ｽﾗﾘｰ投入①
        mapping.put("yuudentaislurrytounyuu2", "yuudentaislurrytounyuu2");                           // 誘電体ｽﾗﾘｰ投入②
        mapping.put("yuudentaislurrytounyuu3", "yuudentaislurrytounyuu3");                           // 誘電体ｽﾗﾘｰ投入③
        mapping.put("yuudentaislurrytounyuu4", "yuudentaislurrytounyuu4");                           // 誘電体ｽﾗﾘｰ投入④
        mapping.put("yuudentaislurrytounyuu5", "yuudentaislurrytounyuu5");                           // 誘電体ｽﾗﾘｰ投入⑤
        mapping.put("yuudentaislurrytounyuu6", "yuudentaislurrytounyuu6");                           // 誘電体ｽﾗﾘｰ投入⑥
        mapping.put("yuudentaislurrytounyuutantousya", "yuudentaislurrytounyuutantousya");           // 誘電体ｽﾗﾘｰ投入担当者
        mapping.put("youzaityouseiryou", "youzaityouseiryou");                                       // 溶剤調整量
        mapping.put("toluenetyouseiryou", "toluenetyouseiryou");                                     // ﾄﾙｴﾝ調整量
        mapping.put("solmixtyouseiryou", "solmixtyouseiryou");                                       // ｿﾙﾐｯｸｽ調整量
        mapping.put("youzaikeiryounichiji", "youzaikeiryounichiji");                                 // 溶剤秤量日時
        mapping.put("zunsanzai1_zairyouhinmei", "zunsanzai1_zairyouhinmei");                         // 分散材①_材料品名
        mapping.put("zunsanzai1_tyougouryoukikaku", "zunsanzai1_tyougouryoukikaku");                 // 分散材①_調合量規格
        mapping.put("zunsanzai1_buzaizaikolotno1", "zunsanzai1_buzaizaikolotno1");                   // 分散材①_部材在庫No1
        mapping.put("zunsanzai1_tyougouryou1", "zunsanzai1_tyougouryou1");                           // 分散材①_調合量1
        mapping.put("zunsanzai1_buzaizaikolotno2", "zunsanzai1_buzaizaikolotno2");                   // 分散材①_部材在庫No2
        mapping.put("zunsanzai1_tyougouryou2", "zunsanzai1_tyougouryou2");                           // 分散材①_調合量2
        mapping.put("zunsanzai2_zairyouhinmei", "zunsanzai2_zairyouhinmei");                         // 分散材②_材料品名
        mapping.put("zunsanzai2_tyougouryoukikaku", "zunsanzai2_tyougouryoukikaku");                 // 分散材②_調合量規格
        mapping.put("zunsanzai2_buzaizaikolotno1", "zunsanzai2_buzaizaikolotno1");                   // 分散材②_部材在庫No1
        mapping.put("zunsanzai2_tyougouryou1", "zunsanzai2_tyougouryou1");                           // 分散材②_調合量1
        mapping.put("zunsanzai2_buzaizaikolotno2", "zunsanzai2_buzaizaikolotno2");                   // 分散材②_部材在庫No2
        mapping.put("zunsanzai2_tyougouryou2", "zunsanzai2_tyougouryou2");                           // 分散材②_調合量2
        mapping.put("youzai1_zairyouhinmei", "youzai1_zairyouhinmei");                               // 溶剤①_材料品名
        mapping.put("youzai1_tyougouryoukikaku", "youzai1_tyougouryoukikaku");                       // 溶剤①_調合量規格
        mapping.put("youzai1_buzaizaikolotno1", "youzai1_buzaizaikolotno1");                         // 溶剤①_部材在庫No1
        mapping.put("youzai1_tyougouryou1", "youzai1_tyougouryou1");                                 // 溶剤①_調合量1
        mapping.put("youzai1_buzaizaikolotno2", "youzai1_buzaizaikolotno2");                         // 溶剤①_部材在庫No2
        mapping.put("youzai1_tyougouryou2", "youzai1_tyougouryou2");                                 // 溶剤①_調合量2
        mapping.put("youzai2_zairyouhinmei", "youzai2_zairyouhinmei");                               // 溶剤②_材料品名
        mapping.put("youzai2_tyougouryoukikaku", "youzai2_tyougouryoukikaku");                       // 溶剤②_調合量規格
        mapping.put("youzai2_buzaizaikolotno1", "youzai2_buzaizaikolotno1");                         // 溶剤②_部材在庫No1
        mapping.put("youzai2_tyougouryou1", "youzai2_tyougouryou1");                                 // 溶剤②_調合量1
        mapping.put("youzai2_buzaizaikolotno2", "youzai2_buzaizaikolotno2");                         // 溶剤②_部材在庫No2
        mapping.put("youzai2_tyougouryou2", "youzai2_tyougouryou2");                                 // 溶剤②_調合量2
        mapping.put("kakuhanki", "kakuhanki");                                                       // 撹拌機
        mapping.put("kakuhanjikan", "kakuhanjikan");                                                 // 撹拌時間
        mapping.put("kakuhankaisinichiji", "kakuhankaisinichiji");                                   // 撹拌開始日時
        mapping.put("kakuhansyuuryounichiji", "kakuhansyuuryounichiji");                             // 撹拌終了日時
        mapping.put("binderkongousetub", "binderkongousetub");                                       // ﾊﾞｲﾝﾀﾞｰ混合設備
        mapping.put("setubisize", "setubisize");                                                     // 設備ｻｲｽﾞ
        mapping.put("binderkongougoki", "binderkongougoki");                                         // ﾊﾞｲﾝﾀﾞｰ混合号機
        mapping.put("kongoutanksyurui", "kongoutanksyurui");                                         // 混合ﾀﾝｸ種類
        mapping.put("kongoutankno", "kongoutankno");                                                 // 混合ﾀﾝｸNo
        mapping.put("tanknaisenjyoukakunin", "tanknaisenjyoukakunin");                               // ﾀﾝｸ内洗浄確認
        mapping.put("tanknaiutibukurokakunin", "tanknaiutibukurokakunin");                           // ﾀﾝｸ内内袋確認
        mapping.put("kakuhanhanesenjyoukakunin", "kakuhanhanesenjyoukakunin");                       // 撹拌羽根洗浄確認
        mapping.put("kakuhanjikusenjyoukakunin", "kakuhanjikusenjyoukakunin");                       // 撹拌軸洗浄確認
        mapping.put("tenryuubannotakasa", "tenryuubannotakasa");                                     // 転流板の高さ
        mapping.put("tankniearthgripsetuzoku", "tankniearthgripsetuzoku");                           // ﾀﾝｸにｱｰｽｸﾞﾘｯﾌﾟ接続
        mapping.put("zunsanzaiyouzaitounyuu", "zunsanzaiyouzaitounyuu");                             // 分散材溶剤投入
        mapping.put("youzaitounyuu1", "youzaitounyuu1");                                             // 溶剤投入①
        mapping.put("youzaitounyuu2", "youzaitounyuu2");                                             // 溶剤投入②
        mapping.put("youzaitounyuutantousya", "youzaitounyuutantousya");                             // 溶剤投入担当者
        mapping.put("bikou1", "bikou1");                                                             // 備考1
        mapping.put("bikou2", "bikou2");                                                             // 備考2
        mapping.put("torokunichiji", "torokunichiji");                                               // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                                                 // 更新日時
        mapping.put("revision", "revision");                                                         // revision

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrSlipYouzaihyouryouTounyuuSutenyouki>> beanHandler = new BeanListHandler<>(SrSlipYouzaihyouryouTounyuuSutenyouki.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrSlipYouzaihyouryouTounyuuSutenyouki> loadTmpSrSlipYouzaihyouryouTounyuuSutenyouki(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,goki,yuudentaislurryjyuuryou,yuudentaislurry_zairyouhinmei,yuudentaislurry_tyougouryoukikaku,"
                + "yuudentaislurry_buzaizaikolotno1,yuudentaislurry_tyougouryou1,yuudentaislurry_buzaizaikolotno2,yuudentaislurry_tyougouryou2,tantousya,yuudentaislurrytounyuu1,"
                + "yuudentaislurrytounyuu2,yuudentaislurrytounyuu3,yuudentaislurrytounyuu4,yuudentaislurrytounyuu5,yuudentaislurrytounyuu6,yuudentaislurrytounyuutantousya,"
                + "youzaityouseiryou,toluenetyouseiryou,solmixtyouseiryou,youzaikeiryounichiji,zunsanzai1_zairyouhinmei,zunsanzai1_tyougouryoukikaku,zunsanzai1_buzaizaikolotno1,"
                + "zunsanzai1_tyougouryou1,zunsanzai1_buzaizaikolotno2,zunsanzai1_tyougouryou2,zunsanzai2_zairyouhinmei,zunsanzai2_tyougouryoukikaku,zunsanzai2_buzaizaikolotno1,"
                + "zunsanzai2_tyougouryou1,zunsanzai2_buzaizaikolotno2,zunsanzai2_tyougouryou2,youzai1_zairyouhinmei,youzai1_tyougouryoukikaku,youzai1_buzaizaikolotno1,"
                + "youzai1_tyougouryou1,youzai1_buzaizaikolotno2,youzai1_tyougouryou2,youzai2_zairyouhinmei,youzai2_tyougouryoukikaku,youzai2_buzaizaikolotno1,youzai2_tyougouryou1,"
                + "youzai2_buzaizaikolotno2,youzai2_tyougouryou2,kakuhanki,kakuhanjikan,kakuhankaisinichiji,kakuhansyuuryounichiji,binderkongousetub,setubisize,binderkongougoki,"
                + "kongoutanksyurui,kongoutankno,tanknaisenjyoukakunin,tanknaiutibukurokakunin,kakuhanhanesenjyoukakunin,kakuhanjikusenjyoukakunin,tenryuubannotakasa,"
                + "tankniearthgripsetuzoku,zunsanzaiyouzaitounyuu,youzaitounyuu1,youzaitounyuu2,youzaitounyuutantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + " FROM tmp_sr_slip_youzaihyouryou_tounyuu_sutenyouki "
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
        mapping.put("kojyo", "kojyo");                                                               // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                                               // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                                             // 枝番
        mapping.put("sliphinmei", "sliphinmei");                                                     // ｽﾘｯﾌﾟ品名
        mapping.put("sliplotno", "sliplotno");                                                       // ｽﾘｯﾌﾟLotNo
        mapping.put("lotkubun", "lotkubun");                                                         // ﾛｯﾄ区分
        mapping.put("genryoukigou", "genryoukigou");                                                 // 原料記号
        mapping.put("goki", "goki");                                                                 // 秤量号機
        mapping.put("yuudentaislurryjyuuryou", "yuudentaislurryjyuuryou");                           // 誘電体ｽﾗﾘｰ重量
        mapping.put("yuudentaislurry_zairyouhinmei", "yuudentaislurry_zairyouhinmei");               // 誘電体ｽﾗﾘｰ_材料品名
        mapping.put("yuudentaislurry_tyougouryoukikaku", "yuudentaislurry_tyougouryoukikaku");       // 誘電体ｽﾗﾘｰ_調合量規格
        mapping.put("yuudentaislurry_buzaizaikolotno1", "yuudentaislurry_buzaizaikolotno1");         // 誘電体ｽﾗﾘｰ_部材在庫No1
        mapping.put("yuudentaislurry_tyougouryou1", "yuudentaislurry_tyougouryou1");                 // 誘電体ｽﾗﾘｰ_調合量1
        mapping.put("yuudentaislurry_buzaizaikolotno2", "yuudentaislurry_buzaizaikolotno2");         // 誘電体ｽﾗﾘｰ_部材在庫No2
        mapping.put("yuudentaislurry_tyougouryou2", "yuudentaislurry_tyougouryou2");                 // 誘電体ｽﾗﾘｰ_調合量2
        mapping.put("tantousya", "tantousya");                                                       // 担当者
        mapping.put("yuudentaislurrytounyuu1", "yuudentaislurrytounyuu1");                           // 誘電体ｽﾗﾘｰ投入①
        mapping.put("yuudentaislurrytounyuu2", "yuudentaislurrytounyuu2");                           // 誘電体ｽﾗﾘｰ投入②
        mapping.put("yuudentaislurrytounyuu3", "yuudentaislurrytounyuu3");                           // 誘電体ｽﾗﾘｰ投入③
        mapping.put("yuudentaislurrytounyuu4", "yuudentaislurrytounyuu4");                           // 誘電体ｽﾗﾘｰ投入④
        mapping.put("yuudentaislurrytounyuu5", "yuudentaislurrytounyuu5");                           // 誘電体ｽﾗﾘｰ投入⑤
        mapping.put("yuudentaislurrytounyuu6", "yuudentaislurrytounyuu6");                           // 誘電体ｽﾗﾘｰ投入⑥
        mapping.put("yuudentaislurrytounyuutantousya", "yuudentaislurrytounyuutantousya");           // 誘電体ｽﾗﾘｰ投入担当者
        mapping.put("youzaityouseiryou", "youzaityouseiryou");                                       // 溶剤調整量
        mapping.put("toluenetyouseiryou", "toluenetyouseiryou");                                     // ﾄﾙｴﾝ調整量
        mapping.put("solmixtyouseiryou", "solmixtyouseiryou");                                       // ｿﾙﾐｯｸｽ調整量
        mapping.put("youzaikeiryounichiji", "youzaikeiryounichiji");                                 // 溶剤秤量日時
        mapping.put("zunsanzai1_zairyouhinmei", "zunsanzai1_zairyouhinmei");                         // 分散材①_材料品名
        mapping.put("zunsanzai1_tyougouryoukikaku", "zunsanzai1_tyougouryoukikaku");                 // 分散材①_調合量規格
        mapping.put("zunsanzai1_buzaizaikolotno1", "zunsanzai1_buzaizaikolotno1");                   // 分散材①_部材在庫No1
        mapping.put("zunsanzai1_tyougouryou1", "zunsanzai1_tyougouryou1");                           // 分散材①_調合量1
        mapping.put("zunsanzai1_buzaizaikolotno2", "zunsanzai1_buzaizaikolotno2");                   // 分散材①_部材在庫No2
        mapping.put("zunsanzai1_tyougouryou2", "zunsanzai1_tyougouryou2");                           // 分散材①_調合量2
        mapping.put("zunsanzai2_zairyouhinmei", "zunsanzai2_zairyouhinmei");                         // 分散材②_材料品名
        mapping.put("zunsanzai2_tyougouryoukikaku", "zunsanzai2_tyougouryoukikaku");                 // 分散材②_調合量規格
        mapping.put("zunsanzai2_buzaizaikolotno1", "zunsanzai2_buzaizaikolotno1");                   // 分散材②_部材在庫No1
        mapping.put("zunsanzai2_tyougouryou1", "zunsanzai2_tyougouryou1");                           // 分散材②_調合量1
        mapping.put("zunsanzai2_buzaizaikolotno2", "zunsanzai2_buzaizaikolotno2");                   // 分散材②_部材在庫No2
        mapping.put("zunsanzai2_tyougouryou2", "zunsanzai2_tyougouryou2");                           // 分散材②_調合量2
        mapping.put("youzai1_zairyouhinmei", "youzai1_zairyouhinmei");                               // 溶剤①_材料品名
        mapping.put("youzai1_tyougouryoukikaku", "youzai1_tyougouryoukikaku");                       // 溶剤①_調合量規格
        mapping.put("youzai1_buzaizaikolotno1", "youzai1_buzaizaikolotno1");                         // 溶剤①_部材在庫No1
        mapping.put("youzai1_tyougouryou1", "youzai1_tyougouryou1");                                 // 溶剤①_調合量1
        mapping.put("youzai1_buzaizaikolotno2", "youzai1_buzaizaikolotno2");                         // 溶剤①_部材在庫No2
        mapping.put("youzai1_tyougouryou2", "youzai1_tyougouryou2");                                 // 溶剤①_調合量2
        mapping.put("youzai2_zairyouhinmei", "youzai2_zairyouhinmei");                               // 溶剤②_材料品名
        mapping.put("youzai2_tyougouryoukikaku", "youzai2_tyougouryoukikaku");                       // 溶剤②_調合量規格
        mapping.put("youzai2_buzaizaikolotno1", "youzai2_buzaizaikolotno1");                         // 溶剤②_部材在庫No1
        mapping.put("youzai2_tyougouryou1", "youzai2_tyougouryou1");                                 // 溶剤②_調合量1
        mapping.put("youzai2_buzaizaikolotno2", "youzai2_buzaizaikolotno2");                         // 溶剤②_部材在庫No2
        mapping.put("youzai2_tyougouryou2", "youzai2_tyougouryou2");                                 // 溶剤②_調合量2
        mapping.put("kakuhanki", "kakuhanki");                                                       // 撹拌機
        mapping.put("kakuhanjikan", "kakuhanjikan");                                                 // 撹拌時間
        mapping.put("kakuhankaisinichiji", "kakuhankaisinichiji");                                   // 撹拌開始日時
        mapping.put("kakuhansyuuryounichiji", "kakuhansyuuryounichiji");                             // 撹拌終了日時
        mapping.put("binderkongousetub", "binderkongousetub");                                       // ﾊﾞｲﾝﾀﾞｰ混合設備
        mapping.put("setubisize", "setubisize");                                                     // 設備ｻｲｽﾞ
        mapping.put("binderkongougoki", "binderkongougoki");                                         // ﾊﾞｲﾝﾀﾞｰ混合号機
        mapping.put("kongoutanksyurui", "kongoutanksyurui");                                         // 混合ﾀﾝｸ種類
        mapping.put("kongoutankno", "kongoutankno");                                                 // 混合ﾀﾝｸNo
        mapping.put("tanknaisenjyoukakunin", "tanknaisenjyoukakunin");                               // ﾀﾝｸ内洗浄確認
        mapping.put("tanknaiutibukurokakunin", "tanknaiutibukurokakunin");                           // ﾀﾝｸ内内袋確認
        mapping.put("kakuhanhanesenjyoukakunin", "kakuhanhanesenjyoukakunin");                       // 撹拌羽根洗浄確認
        mapping.put("kakuhanjikusenjyoukakunin", "kakuhanjikusenjyoukakunin");                       // 撹拌軸洗浄確認
        mapping.put("tenryuubannotakasa", "tenryuubannotakasa");                                     // 転流板の高さ
        mapping.put("tankniearthgripsetuzoku", "tankniearthgripsetuzoku");                           // ﾀﾝｸにｱｰｽｸﾞﾘｯﾌﾟ接続
        mapping.put("zunsanzaiyouzaitounyuu", "zunsanzaiyouzaitounyuu");                             // 分散材溶剤投入
        mapping.put("youzaitounyuu1", "youzaitounyuu1");                                             // 溶剤投入①
        mapping.put("youzaitounyuu2", "youzaitounyuu2");                                             // 溶剤投入②
        mapping.put("youzaitounyuutantousya", "youzaitounyuutantousya");                             // 溶剤投入担当者
        mapping.put("bikou1", "bikou1");                                                             // 備考1
        mapping.put("bikou2", "bikou2");                                                             // 備考2
        mapping.put("torokunichiji", "torokunichiji");                                               // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                                                 // 更新日時
        mapping.put("revision", "revision");                                                         // revision
        mapping.put("deleteflag", "deleteflag");                                                     // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrSlipYouzaihyouryouTounyuuSutenyouki>> beanHandler = new BeanListHandler<>(SrSlipYouzaihyouryouTounyuuSutenyouki.class, rowProcessor);

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
     * 項目データ設定
     *
     * @param processData 処理制御データ
     * @param itemId 項目ID
     * @param value 設定値
     * @return 処理制御データ
     */
    private ProcessData setItemKikuchiData(ProcessData processData, String itemId, String value) {
        List<FXHDD01> selectData
                = processData.getItemList().stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            selectData.get(0).setKikakuChi("【" + value + "】");
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
     * @param srSlipYouzaihyouryouTounyuuSutenyouki ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)データ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrSlipYouzaihyouryouTounyuuSutenyouki srSlipYouzaihyouryouTounyuuSutenyouki) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srSlipYouzaihyouryouTounyuuSutenyouki != null) {
            // 元データが存在する場合元データより取得
            return getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(itemId, srSlipYouzaihyouryouTounyuuSutenyouki);
        } else {
            return null;
        }
    }

    /**
     * 項目データ(入力値)取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @param srSlipYouzaihyouryouTounyuuSutenyouki ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)データ
     * @return 入力値
     */
    private String getItemKikakuchi(List<FXHDD01> listData, String itemId, SrSlipYouzaihyouryouTounyuuSutenyouki srSlipYouzaihyouryouTounyuuSutenyouki) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return StringUtil.nullToBlank(selectData.get(0).getKikakuChi()).replace("【", "").replace("】", "");
        } else if (srSlipYouzaihyouryouTounyuuSutenyouki != null) {
            // 元データが存在する場合元データより取得
            return getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(itemId, srSlipYouzaihyouryouTounyuuSutenyouki);
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
     * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)_仮登録(tmp_sr_slip_youzaihyouryou_tounyuu_sutenyouki)登録処理
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
    private void insertTmpSrSlipYouzaihyouryouTounyuuSutenyouki(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "INSERT INTO tmp_sr_slip_youzaihyouryou_tounyuu_sutenyouki ( "
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,goki,yuudentaislurryjyuuryou,yuudentaislurry_zairyouhinmei,yuudentaislurry_tyougouryoukikaku,"
                + "yuudentaislurry_buzaizaikolotno1,yuudentaislurry_tyougouryou1,yuudentaislurry_buzaizaikolotno2,yuudentaislurry_tyougouryou2,tantousya,yuudentaislurrytounyuu1,"
                + "yuudentaislurrytounyuu2,yuudentaislurrytounyuu3,yuudentaislurrytounyuu4,yuudentaislurrytounyuu5,yuudentaislurrytounyuu6,yuudentaislurrytounyuutantousya,"
                + "youzaityouseiryou,toluenetyouseiryou,solmixtyouseiryou,youzaikeiryounichiji,zunsanzai1_zairyouhinmei,zunsanzai1_tyougouryoukikaku,zunsanzai1_buzaizaikolotno1,"
                + "zunsanzai1_tyougouryou1,zunsanzai1_buzaizaikolotno2,zunsanzai1_tyougouryou2,zunsanzai2_zairyouhinmei,zunsanzai2_tyougouryoukikaku,zunsanzai2_buzaizaikolotno1,"
                + "zunsanzai2_tyougouryou1,zunsanzai2_buzaizaikolotno2,zunsanzai2_tyougouryou2,youzai1_zairyouhinmei,youzai1_tyougouryoukikaku,youzai1_buzaizaikolotno1,"
                + "youzai1_tyougouryou1,youzai1_buzaizaikolotno2,youzai1_tyougouryou2,youzai2_zairyouhinmei,youzai2_tyougouryoukikaku,youzai2_buzaizaikolotno1,youzai2_tyougouryou1,"
                + "youzai2_buzaizaikolotno2,youzai2_tyougouryou2,kakuhanki,kakuhanjikan,kakuhankaisinichiji,kakuhansyuuryounichiji,binderkongousetub,setubisize,binderkongougoki,"
                + "kongoutanksyurui,kongoutankno,tanknaisenjyoukakunin,tanknaiutibukurokakunin,kakuhanhanesenjyoukakunin,kakuhanjikusenjyoukakunin,tenryuubannotakasa,"
                + "tankniearthgripsetuzoku,zunsanzaiyouzaitounyuu,youzaitounyuu1,youzaitounyuu2,youzaitounyuutantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrSlipYouzaihyouryouTounyuuSutenyouki(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, processData, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)_仮登録(tmp_sr_slip_youzaihyouryou_tounyuu_sutenyouki)更新処理
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
    private SrSlipYouzaihyouryouTounyuuSutenyouki updateTmpSrSlipYouzaihyouryouTounyuuSutenyouki(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "UPDATE tmp_sr_slip_youzaihyouryou_tounyuu_sutenyouki SET "
                + " sliphinmei = ?,sliplotno = ?,lotkubun = ?,genryoukigou = ?,goki = ?,yuudentaislurryjyuuryou = ?,yuudentaislurry_zairyouhinmei = ?,yuudentaislurry_tyougouryoukikaku = ?,"
                + "yuudentaislurry_buzaizaikolotno1 = ?,yuudentaislurry_tyougouryou1 = ?,yuudentaislurry_buzaizaikolotno2 = ?,yuudentaislurry_tyougouryou2 = ?,tantousya = ?,"
                + "yuudentaislurrytounyuu1 = ?,yuudentaislurrytounyuu2 = ?,yuudentaislurrytounyuu3 = ?,yuudentaislurrytounyuu4 = ?,yuudentaislurrytounyuu5 = ?,yuudentaislurrytounyuu6 = ?,"
                + "yuudentaislurrytounyuutantousya = ?,youzaityouseiryou = ?,toluenetyouseiryou = ?,solmixtyouseiryou = ?,youzaikeiryounichiji = ?,zunsanzai1_zairyouhinmei = ?,"
                + "zunsanzai1_tyougouryoukikaku = ?,zunsanzai1_buzaizaikolotno1 = ?,zunsanzai1_tyougouryou1 = ?,zunsanzai1_buzaizaikolotno2 = ?,zunsanzai1_tyougouryou2 = ?,"
                + "zunsanzai2_zairyouhinmei = ?,zunsanzai2_tyougouryoukikaku = ?,zunsanzai2_buzaizaikolotno1 = ?,zunsanzai2_tyougouryou1 = ?,zunsanzai2_buzaizaikolotno2 = ?,"
                + "zunsanzai2_tyougouryou2 = ?,youzai1_zairyouhinmei = ?,youzai1_tyougouryoukikaku = ?,youzai1_buzaizaikolotno1 = ?,youzai1_tyougouryou1 = ?,youzai1_buzaizaikolotno2 = ?,"
                + "youzai1_tyougouryou2 = ?,youzai2_zairyouhinmei = ?,youzai2_tyougouryoukikaku = ?,youzai2_buzaizaikolotno1 = ?,youzai2_tyougouryou1 = ?,youzai2_buzaizaikolotno2 = ?,"
                + "youzai2_tyougouryou2 = ?,kakuhanki = ?,kakuhanjikan = ?,kakuhankaisinichiji = ?,kakuhansyuuryounichiji = ?,binderkongousetub = ?,setubisize = ?,binderkongougoki = ?,"
                + "kongoutanksyurui = ?,kongoutankno = ?,tanknaisenjyoukakunin = ?,tanknaiutibukurokakunin = ?,kakuhanhanesenjyoukakunin = ?,kakuhanjikusenjyoukakunin = ?,tenryuubannotakasa = ?,"
                + "tankniearthgripsetuzoku = ?,zunsanzaiyouzaitounyuu = ?,youzaitounyuu1 = ?,youzaitounyuu2 = ?,youzaitounyuutantousya = ?,bikou1 = ?,bikou2 = ?,kosinnichiji = ?,"
                + "revision = ?,deleteflag = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrSlipYouzaihyouryouTounyuuSutenyouki> srSlipYouzaihyouryouTounyuuSutenyoukiList = getSrSlipYouzaihyouryouTounyuuSutenyoukiData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrSlipYouzaihyouryouTounyuuSutenyouki srSlipYouzaihyouryouTounyuuSutenyouki = null;
        if (!srSlipYouzaihyouryouTounyuuSutenyoukiList.isEmpty()) {
            srSlipYouzaihyouryouTounyuuSutenyouki = srSlipYouzaihyouryouTounyuuSutenyoukiList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrSlipYouzaihyouryouTounyuuSutenyouki(false, newRev, 0, "", "", "", systemTime, processData, srSlipYouzaihyouryouTounyuuSutenyouki);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
        return srSlipYouzaihyouryouTounyuuSutenyouki;
    }

    /**
     * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)_仮登録(tmp_sr_slip_youzaihyouryou_tounyuu_sutenyouki)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrSlipYouzaihyouryouTounyuuSutenyouki(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sr_slip_youzaihyouryou_tounyuu_sutenyouki "
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
     * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)_仮登録(tmp_sr_slip_youzaihyouryou_tounyuu_sutenyouki)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srSlipYouzaihyouryouTounyuuSutenyouki ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)データ
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrSlipYouzaihyouryouTounyuuSutenyouki(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, String systemTime, ProcessData processData, SrSlipYouzaihyouryouTounyuuSutenyouki srSlipYouzaihyouryouTounyuuSutenyouki) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // 溶剤秤量日時
        String youzaikeiryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B030Const.YOUZAIKEIRYOU_TIME, srSlipYouzaihyouryouTounyuuSutenyouki));
        // 撹拌開始日時
        String kakuhankaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B030Const.KAKUHANKAISI_TIME, srSlipYouzaihyouryouTounyuuSutenyouki));
        // 撹拌終了日時
        String kakuhansyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B030Const.KAKUHANSYUURYOU_TIME, srSlipYouzaihyouryouTounyuuSutenyouki));

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.SLIPHINMEI, srSlipYouzaihyouryouTounyuuSutenyouki)));                                // ｽﾘｯﾌﾟ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.SLIPLOTNO, srSlipYouzaihyouryouTounyuuSutenyouki)));                                 // ｽﾘｯﾌﾟLotNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.LOTKUBUN, srSlipYouzaihyouryouTounyuuSutenyouki)));                                  // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B030Const.GENRYOUKIGOU, srSlipYouzaihyouryouTounyuuSutenyouki)));                         // 原料記号
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.GOKI, srSlipYouzaihyouryouTounyuuSutenyouki)));                                      // 秤量号機
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B030Const.YUUDENTAISLURRYJYUURYOU, srSlipYouzaihyouryouTounyuuSutenyouki)));                 // 誘電体ｽﾗﾘｰ重量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B030Const.YUUDENTAISLURRY_ZAIRYOUHINMEI, srSlipYouzaihyouryouTounyuuSutenyouki)));        // 誘電体ｽﾗﾘｰ_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOUKIKAKU, srSlipYouzaihyouryouTounyuuSutenyouki)));    // 誘電体ｽﾗﾘｰ_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.YUUDENTAISLURRY_BUZAIZAIKOLOTNO1, srSlipYouzaihyouryouTounyuuSutenyouki)));          // 誘電体ｽﾗﾘｰ_部材在庫No1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOU1, srSlipYouzaihyouryouTounyuuSutenyouki)));                 // 誘電体ｽﾗﾘｰ_調合量1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.YUUDENTAISLURRY_BUZAIZAIKOLOTNO2, srSlipYouzaihyouryouTounyuuSutenyouki)));          // 誘電体ｽﾗﾘｰ_部材在庫No2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOU2, srSlipYouzaihyouryouTounyuuSutenyouki)));                 // 誘電体ｽﾗﾘｰ_調合量2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.TANTOUSYA, srSlipYouzaihyouryouTounyuuSutenyouki)));                                 // 担当者
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU1, srSlipYouzaihyouryouTounyuuSutenyouki), null));                                 // 誘電体ｽﾗﾘｰ投入①
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU2, srSlipYouzaihyouryouTounyuuSutenyouki), null));                                 // 誘電体ｽﾗﾘｰ投入②
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU3, srSlipYouzaihyouryouTounyuuSutenyouki), null));                                 // 誘電体ｽﾗﾘｰ投入③
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU4, srSlipYouzaihyouryouTounyuuSutenyouki), null));                                 // 誘電体ｽﾗﾘｰ投入④
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU5, srSlipYouzaihyouryouTounyuuSutenyouki), null));                                 // 誘電体ｽﾗﾘｰ投入⑤
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU6, srSlipYouzaihyouryouTounyuuSutenyouki), null));                                 // 誘電体ｽﾗﾘｰ投入⑥
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.YUUDENTAISLURRYTOUNYUUTANTOUSYA, srSlipYouzaihyouryouTounyuuSutenyouki)));           // 誘電体ｽﾗﾘｰ投入担当者
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.YOUZAITYOUSEIRYOU, srSlipYouzaihyouryouTounyuuSutenyouki)));                            // 溶剤調整量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.TOLUENETYOUSEIRYOU, srSlipYouzaihyouryouTounyuuSutenyouki)));                           // ﾄﾙｴﾝ調整量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.SOLMIXTYOUSEIRYOU, srSlipYouzaihyouryouTounyuuSutenyouki)));                            // ｿﾙﾐｯｸｽ調整量
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.YOUZAIKEIRYOU_DAY, srSlipYouzaihyouryouTounyuuSutenyouki),
                "".equals(youzaikeiryouTime) ? "0000" : youzaikeiryouTime));                                                                                                            // 溶剤秤量日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B030Const.ZUNSANZAI1_ZAIRYOUHINMEI, srSlipYouzaihyouryouTounyuuSutenyouki)));             // 分散材①_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B030Const.ZUNSANZAI1_TYOUGOURYOUKIKAKU, srSlipYouzaihyouryouTounyuuSutenyouki)));         // 分散材①_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.ZUNSANZAI1_BUZAIZAIKOLOTNO1, srSlipYouzaihyouryouTounyuuSutenyouki)));               // 分散材①_部材在庫No1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.ZUNSANZAI1_TYOUGOURYOU1, srSlipYouzaihyouryouTounyuuSutenyouki)));                      // 分散材①_調合量1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.ZUNSANZAI1_BUZAIZAIKOLOTNO2, srSlipYouzaihyouryouTounyuuSutenyouki)));               // 分散材①_部材在庫No2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.ZUNSANZAI1_TYOUGOURYOU2, srSlipYouzaihyouryouTounyuuSutenyouki)));                      // 分散材①_調合量2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B030Const.ZUNSANZAI2_ZAIRYOUHINMEI, srSlipYouzaihyouryouTounyuuSutenyouki)));             // 分散材②_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B030Const.ZUNSANZAI2_TYOUGOURYOUKIKAKU, srSlipYouzaihyouryouTounyuuSutenyouki)));         // 分散材②_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.ZUNSANZAI2_BUZAIZAIKOLOTNO1, srSlipYouzaihyouryouTounyuuSutenyouki)));               // 分散材②_部材在庫No1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.ZUNSANZAI2_TYOUGOURYOU1, srSlipYouzaihyouryouTounyuuSutenyouki)));                      // 分散材②_調合量1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.ZUNSANZAI2_BUZAIZAIKOLOTNO2, srSlipYouzaihyouryouTounyuuSutenyouki)));               // 分散材②_部材在庫No2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.ZUNSANZAI2_TYOUGOURYOU2, srSlipYouzaihyouryouTounyuuSutenyouki)));                      // 分散材②_調合量2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B030Const.YOUZAI1_ZAIRYOUHINMEI, srSlipYouzaihyouryouTounyuuSutenyouki)));                // 溶剤①_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B030Const.YOUZAI1_TYOUGOURYOUKIKAKU, srSlipYouzaihyouryouTounyuuSutenyouki)));            // 溶剤①_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.YOUZAI1_BUZAIZAIKOLOTNO1, srSlipYouzaihyouryouTounyuuSutenyouki)));                  // 溶剤①_部材在庫No1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.YOUZAI1_TYOUGOURYOU1, srSlipYouzaihyouryouTounyuuSutenyouki)));                         // 溶剤①_調合量1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.YOUZAI1_BUZAIZAIKOLOTNO2, srSlipYouzaihyouryouTounyuuSutenyouki)));                  // 溶剤①_部材在庫No2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.YOUZAI1_TYOUGOURYOU2, srSlipYouzaihyouryouTounyuuSutenyouki)));                         // 溶剤①_調合量2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B030Const.YOUZAI2_ZAIRYOUHINMEI, srSlipYouzaihyouryouTounyuuSutenyouki)));                // 溶剤②_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B030Const.YOUZAI2_TYOUGOURYOUKIKAKU, srSlipYouzaihyouryouTounyuuSutenyouki)));            // 溶剤②_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.YOUZAI2_BUZAIZAIKOLOTNO1, srSlipYouzaihyouryouTounyuuSutenyouki)));                  // 溶剤②_部材在庫No1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.YOUZAI2_TYOUGOURYOU1, srSlipYouzaihyouryouTounyuuSutenyouki)));                         // 溶剤②_調合量1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.YOUZAI2_BUZAIZAIKOLOTNO2, srSlipYouzaihyouryouTounyuuSutenyouki)));                  // 溶剤②_部材在庫No2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.YOUZAI2_TYOUGOURYOU2, srSlipYouzaihyouryouTounyuuSutenyouki)));                         // 溶剤②_調合量2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B030Const.KAKUHANKI, srSlipYouzaihyouryouTounyuuSutenyouki)));                            // 撹拌機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B030Const.KAKUHANJIKAN, srSlipYouzaihyouryouTounyuuSutenyouki)));                         // 撹拌時間
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.KAKUHANKAISI_DAY, srSlipYouzaihyouryouTounyuuSutenyouki),
                "".equals(kakuhankaisiTime) ? "0000" : kakuhankaisiTime));                                                                                                              // 撹拌開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.KAKUHANSYUURYOU_DAY, srSlipYouzaihyouryouTounyuuSutenyouki),
                "".equals(kakuhansyuuryouTime) ? "0000" : kakuhansyuuryouTime));                                                                                                        // 撹拌終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B030Const.BINDERKONGOUSETUB, srSlipYouzaihyouryouTounyuuSutenyouki)));                    // ﾊﾞｲﾝﾀﾞｰ混合設備
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B030Const.SETUBISIZE, srSlipYouzaihyouryouTounyuuSutenyouki)));                           // 設備ｻｲｽﾞ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B030Const.BINDERKONGOUGOKI, srSlipYouzaihyouryouTounyuuSutenyouki)));                     // ﾊﾞｲﾝﾀﾞｰ混合号機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B030Const.KONGOUTANKSYURUI, srSlipYouzaihyouryouTounyuuSutenyouki)));                     // 混合ﾀﾝｸ種類
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.KONGOUTANKNO, srSlipYouzaihyouryouTounyuuSutenyouki)));                                 // 混合ﾀﾝｸNo
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B030Const.TANKNAISENJYOUKAKUNIN, srSlipYouzaihyouryouTounyuuSutenyouki), null));                                   // ﾀﾝｸ内洗浄確認
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B030Const.TANKNAIUTIBUKUROKAKUNIN, srSlipYouzaihyouryouTounyuuSutenyouki), null));                                 // ﾀﾝｸ内内袋確認
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B030Const.KAKUHANHANESENJYOUKAKUNIN, srSlipYouzaihyouryouTounyuuSutenyouki), null));                               // 撹拌羽根洗浄確認
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B030Const.KAKUHANJIKUSENJYOUKAKUNIN, srSlipYouzaihyouryouTounyuuSutenyouki), null));                               // 撹拌軸洗浄確認
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B030Const.TENRYUUBANNOTAKASA, srSlipYouzaihyouryouTounyuuSutenyouki)));                   // 転流板の高さ
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B030Const.TANKNIEARTHGRIPSETUZOKU, srSlipYouzaihyouryouTounyuuSutenyouki), null));                                 // ﾀﾝｸにｱｰｽｸﾞﾘｯﾌﾟ接続
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B030Const.ZUNSANZAIYOUZAITOUNYUU, srSlipYouzaihyouryouTounyuuSutenyouki), null));                                  // 分散材溶剤投入
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B030Const.YOUZAITOUNYUU1, srSlipYouzaihyouryouTounyuuSutenyouki), null));                                          // 溶剤投入①
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B030Const.YOUZAITOUNYUU2, srSlipYouzaihyouryouTounyuuSutenyouki), null));                                          // 溶剤投入②
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.YOUZAITOUNYUUTANTOUSYA, srSlipYouzaihyouryouTounyuuSutenyouki)));                    // 溶剤投入担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.BIKOU1, srSlipYouzaihyouryouTounyuuSutenyouki)));                                    // 備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B030Const.BIKOU2, srSlipYouzaihyouryouTounyuuSutenyouki)));                                    // 備考2

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
     * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)(sr_slip_youzaihyouryou_tounyuu_sutenyouki)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param tmpSrSlipYouzaihyouryouTounyuuSutenyouki 仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrSlipYouzaihyouryouTounyuuSutenyouki(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData, SrSlipYouzaihyouryouTounyuuSutenyouki tmpSrSlipYouzaihyouryouTounyuuSutenyouki) throws SQLException {

        String sql = "INSERT INTO sr_slip_youzaihyouryou_tounyuu_sutenyouki ( "
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,goki,yuudentaislurryjyuuryou,yuudentaislurry_zairyouhinmei,yuudentaislurry_tyougouryoukikaku,"
                + "yuudentaislurry_buzaizaikolotno1,yuudentaislurry_tyougouryou1,yuudentaislurry_buzaizaikolotno2,yuudentaislurry_tyougouryou2,tantousya,yuudentaislurrytounyuu1,"
                + "yuudentaislurrytounyuu2,yuudentaislurrytounyuu3,yuudentaislurrytounyuu4,yuudentaislurrytounyuu5,yuudentaislurrytounyuu6,yuudentaislurrytounyuutantousya,"
                + "youzaityouseiryou,toluenetyouseiryou,solmixtyouseiryou,youzaikeiryounichiji,zunsanzai1_zairyouhinmei,zunsanzai1_tyougouryoukikaku,zunsanzai1_buzaizaikolotno1,"
                + "zunsanzai1_tyougouryou1,zunsanzai1_buzaizaikolotno2,zunsanzai1_tyougouryou2,zunsanzai2_zairyouhinmei,zunsanzai2_tyougouryoukikaku,zunsanzai2_buzaizaikolotno1,"
                + "zunsanzai2_tyougouryou1,zunsanzai2_buzaizaikolotno2,zunsanzai2_tyougouryou2,youzai1_zairyouhinmei,youzai1_tyougouryoukikaku,youzai1_buzaizaikolotno1,"
                + "youzai1_tyougouryou1,youzai1_buzaizaikolotno2,youzai1_tyougouryou2,youzai2_zairyouhinmei,youzai2_tyougouryoukikaku,youzai2_buzaizaikolotno1,youzai2_tyougouryou1,"
                + "youzai2_buzaizaikolotno2,youzai2_tyougouryou2,kakuhanki,kakuhanjikan,kakuhankaisinichiji,kakuhansyuuryounichiji,binderkongousetub,setubisize,binderkongougoki,"
                + "kongoutanksyurui,kongoutankno,tanknaisenjyoukakunin,tanknaiutibukurokakunin,kakuhanhanesenjyoukakunin,kakuhanjikusenjyoukakunin,tenryuubannotakasa,"
                + "tankniearthgripsetuzoku,zunsanzaiyouzaitounyuu,youzaitounyuu1,youzaitounyuu2,youzaitounyuutantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrSlipYouzaihyouryouTounyuuSutenyouki(true, newRev, kojyo, lotNo, edaban, systemTime, processData, tmpSrSlipYouzaihyouryouTounyuuSutenyouki);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)(sr_slip_youzaihyouryou_tounyuu_sutenyouki)更新処理
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
    private SrSlipYouzaihyouryouTounyuuSutenyouki updateSrSlipYouzaihyouryouTounyuuSutenyouki(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {
        String sql = "UPDATE sr_slip_youzaihyouryou_tounyuu_sutenyouki SET "
                + " sliphinmei = ?,sliplotno = ?,lotkubun = ?,genryoukigou = ?,goki = ?,yuudentaislurryjyuuryou = ?,yuudentaislurry_zairyouhinmei = ?,yuudentaislurry_tyougouryoukikaku = ?,"
                + "yuudentaislurry_buzaizaikolotno1 = ?,yuudentaislurry_tyougouryou1 = ?,yuudentaislurry_buzaizaikolotno2 = ?,yuudentaislurry_tyougouryou2 = ?,tantousya = ?,"
                + "yuudentaislurrytounyuu1 = ?,yuudentaislurrytounyuu2 = ?,yuudentaislurrytounyuu3 = ?,yuudentaislurrytounyuu4 = ?,yuudentaislurrytounyuu5 = ?,yuudentaislurrytounyuu6 = ?,"
                + "yuudentaislurrytounyuutantousya = ?,youzaityouseiryou = ?,toluenetyouseiryou = ?,solmixtyouseiryou = ?,youzaikeiryounichiji = ?,zunsanzai1_zairyouhinmei = ?,"
                + "zunsanzai1_tyougouryoukikaku = ?,zunsanzai1_buzaizaikolotno1 = ?,zunsanzai1_tyougouryou1 = ?,zunsanzai1_buzaizaikolotno2 = ?,zunsanzai1_tyougouryou2 = ?,"
                + "zunsanzai2_zairyouhinmei = ?,zunsanzai2_tyougouryoukikaku = ?,zunsanzai2_buzaizaikolotno1 = ?,zunsanzai2_tyougouryou1 = ?,zunsanzai2_buzaizaikolotno2 = ?,"
                + "zunsanzai2_tyougouryou2 = ?,youzai1_zairyouhinmei = ?,youzai1_tyougouryoukikaku = ?,youzai1_buzaizaikolotno1 = ?,youzai1_tyougouryou1 = ?,youzai1_buzaizaikolotno2 = ?,"
                + "youzai1_tyougouryou2 = ?,youzai2_zairyouhinmei = ?,youzai2_tyougouryoukikaku = ?,youzai2_buzaizaikolotno1 = ?,youzai2_tyougouryou1 = ?,youzai2_buzaizaikolotno2 = ?,"
                + "youzai2_tyougouryou2 = ?,kakuhanki = ?,kakuhanjikan = ?,kakuhankaisinichiji = ?,kakuhansyuuryounichiji = ?,binderkongousetub = ?,setubisize = ?,binderkongougoki = ?,"
                + "kongoutanksyurui = ?,kongoutankno = ?,tanknaisenjyoukakunin = ?,tanknaiutibukurokakunin = ?,kakuhanhanesenjyoukakunin = ?,kakuhanjikusenjyoukakunin = ?,tenryuubannotakasa = ?,"
                + "tankniearthgripsetuzoku = ?,zunsanzaiyouzaitounyuu = ?,youzaitounyuu1 = ?,youzaitounyuu2 = ?,youzaitounyuutantousya = ?,bikou1 = ?,bikou2 = ?,kosinnichiji = ?,"
                + "revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrSlipYouzaihyouryouTounyuuSutenyouki> srSlipYouzaihyouryouTounyuuSutenyoukiList = getSrSlipYouzaihyouryouTounyuuSutenyoukiData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrSlipYouzaihyouryouTounyuuSutenyouki srSlipYouzaihyouryouTounyuuSutenyouki = null;
        if (!srSlipYouzaihyouryouTounyuuSutenyoukiList.isEmpty()) {
            srSlipYouzaihyouryouTounyuuSutenyouki = srSlipYouzaihyouryouTounyuuSutenyoukiList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrSlipYouzaihyouryouTounyuuSutenyouki(false, newRev, "", "", "", systemTime, processData, srSlipYouzaihyouryouTounyuuSutenyouki);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
        return srSlipYouzaihyouryouTounyuuSutenyouki;
    }

    /**
     * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)(sr_slip_youzaihyouryou_tounyuu_sutenyouki)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srSlipYouzaihyouryouTounyuuSutenyouki ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrSlipYouzaihyouryouTounyuuSutenyouki(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            String systemTime, ProcessData processData, SrSlipYouzaihyouryouTounyuuSutenyouki srSlipYouzaihyouryouTounyuuSutenyouki) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // 溶剤秤量日時
        String youzaikeiryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B030Const.YOUZAIKEIRYOU_TIME, srSlipYouzaihyouryouTounyuuSutenyouki));
        // 撹拌開始日時
        String kakuhankaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B030Const.KAKUHANKAISI_TIME, srSlipYouzaihyouryouTounyuuSutenyouki));
        // 撹拌終了日時
        String kakuhansyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B030Const.KAKUHANSYUURYOU_TIME, srSlipYouzaihyouryouTounyuuSutenyouki));

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B030Const.SLIPHINMEI, srSlipYouzaihyouryouTounyuuSutenyouki)));                                // ｽﾘｯﾌﾟ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B030Const.SLIPLOTNO, srSlipYouzaihyouryouTounyuuSutenyouki)));                                 // ｽﾘｯﾌﾟLotNo
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B030Const.LOTKUBUN, srSlipYouzaihyouryouTounyuuSutenyouki)));                                  // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B030Const.GENRYOUKIGOU, srSlipYouzaihyouryouTounyuuSutenyouki)));                         // 原料記号
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B030Const.GOKI, srSlipYouzaihyouryouTounyuuSutenyouki)));                                      // 秤量号機
        params.add(DBUtil.stringToIntObject(getItemKikakuchi(pItemList, GXHDO102B030Const.YUUDENTAISLURRYJYUURYOU, srSlipYouzaihyouryouTounyuuSutenyouki)));                 // 誘電体ｽﾗﾘｰ重量
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B030Const.YUUDENTAISLURRY_ZAIRYOUHINMEI, srSlipYouzaihyouryouTounyuuSutenyouki)));        // 誘電体ｽﾗﾘｰ_材料品名
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOUKIKAKU, srSlipYouzaihyouryouTounyuuSutenyouki)));    // 誘電体ｽﾗﾘｰ_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B030Const.YUUDENTAISLURRY_BUZAIZAIKOLOTNO1, srSlipYouzaihyouryouTounyuuSutenyouki)));          // 誘電体ｽﾗﾘｰ_部材在庫No1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOU1, srSlipYouzaihyouryouTounyuuSutenyouki)));                 // 誘電体ｽﾗﾘｰ_調合量1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B030Const.YUUDENTAISLURRY_BUZAIZAIKOLOTNO2, srSlipYouzaihyouryouTounyuuSutenyouki)));          // 誘電体ｽﾗﾘｰ_部材在庫No2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOU2, srSlipYouzaihyouryouTounyuuSutenyouki)));                 // 誘電体ｽﾗﾘｰ_調合量2
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B030Const.TANTOUSYA, srSlipYouzaihyouryouTounyuuSutenyouki)));                                 // 担当者
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU1, srSlipYouzaihyouryouTounyuuSutenyouki), 9));                         // 誘電体ｽﾗﾘｰ投入①
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU2, srSlipYouzaihyouryouTounyuuSutenyouki), 9));                         // 誘電体ｽﾗﾘｰ投入②
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU3, srSlipYouzaihyouryouTounyuuSutenyouki), 9));                         // 誘電体ｽﾗﾘｰ投入③
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU4, srSlipYouzaihyouryouTounyuuSutenyouki), 9));                         // 誘電体ｽﾗﾘｰ投入④
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU5, srSlipYouzaihyouryouTounyuuSutenyouki), 9));                         // 誘電体ｽﾗﾘｰ投入⑤
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU6, srSlipYouzaihyouryouTounyuuSutenyouki), 9));                         // 誘電体ｽﾗﾘｰ投入⑥
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B030Const.YUUDENTAISLURRYTOUNYUUTANTOUSYA, srSlipYouzaihyouryouTounyuuSutenyouki)));           // 誘電体ｽﾗﾘｰ投入担当者
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B030Const.YOUZAITYOUSEIRYOU, srSlipYouzaihyouryouTounyuuSutenyouki)));                            // 溶剤調整量
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B030Const.TOLUENETYOUSEIRYOU, srSlipYouzaihyouryouTounyuuSutenyouki)));                           // ﾄﾙｴﾝ調整量
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B030Const.SOLMIXTYOUSEIRYOU, srSlipYouzaihyouryouTounyuuSutenyouki)));                            // ｿﾙﾐｯｸｽ調整量
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B030Const.YOUZAIKEIRYOU_DAY, srSlipYouzaihyouryouTounyuuSutenyouki),
                "".equals(youzaikeiryouTime) ? "0000" : youzaikeiryouTime));                                                                                                 // 溶剤秤量日時
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B030Const.ZUNSANZAI1_ZAIRYOUHINMEI, srSlipYouzaihyouryouTounyuuSutenyouki)));             // 分散材①_材料品名
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B030Const.ZUNSANZAI1_TYOUGOURYOUKIKAKU, srSlipYouzaihyouryouTounyuuSutenyouki)));         // 分散材①_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B030Const.ZUNSANZAI1_BUZAIZAIKOLOTNO1, srSlipYouzaihyouryouTounyuuSutenyouki)));               // 分散材①_部材在庫No1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B030Const.ZUNSANZAI1_TYOUGOURYOU1, srSlipYouzaihyouryouTounyuuSutenyouki)));                      // 分散材①_調合量1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B030Const.ZUNSANZAI1_BUZAIZAIKOLOTNO2, srSlipYouzaihyouryouTounyuuSutenyouki)));               // 分散材①_部材在庫No2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B030Const.ZUNSANZAI1_TYOUGOURYOU2, srSlipYouzaihyouryouTounyuuSutenyouki)));                      // 分散材①_調合量2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B030Const.ZUNSANZAI2_ZAIRYOUHINMEI, srSlipYouzaihyouryouTounyuuSutenyouki)));             // 分散材②_材料品名
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B030Const.ZUNSANZAI2_TYOUGOURYOUKIKAKU, srSlipYouzaihyouryouTounyuuSutenyouki)));         // 分散材②_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B030Const.ZUNSANZAI2_BUZAIZAIKOLOTNO1, srSlipYouzaihyouryouTounyuuSutenyouki)));               // 分散材②_部材在庫No1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B030Const.ZUNSANZAI2_TYOUGOURYOU1, srSlipYouzaihyouryouTounyuuSutenyouki)));                      // 分散材②_調合量1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B030Const.ZUNSANZAI2_BUZAIZAIKOLOTNO2, srSlipYouzaihyouryouTounyuuSutenyouki)));               // 分散材②_部材在庫No2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B030Const.ZUNSANZAI2_TYOUGOURYOU2, srSlipYouzaihyouryouTounyuuSutenyouki)));                      // 分散材②_調合量2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B030Const.YOUZAI1_ZAIRYOUHINMEI, srSlipYouzaihyouryouTounyuuSutenyouki)));                // 溶剤①_材料品名
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B030Const.YOUZAI1_TYOUGOURYOUKIKAKU, srSlipYouzaihyouryouTounyuuSutenyouki)));            // 溶剤①_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B030Const.YOUZAI1_BUZAIZAIKOLOTNO1, srSlipYouzaihyouryouTounyuuSutenyouki)));                  // 溶剤①_部材在庫No1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B030Const.YOUZAI1_TYOUGOURYOU1, srSlipYouzaihyouryouTounyuuSutenyouki)));                         // 溶剤①_調合量1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B030Const.YOUZAI1_BUZAIZAIKOLOTNO2, srSlipYouzaihyouryouTounyuuSutenyouki)));                  // 溶剤①_部材在庫No2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B030Const.YOUZAI1_TYOUGOURYOU2, srSlipYouzaihyouryouTounyuuSutenyouki)));                         // 溶剤①_調合量2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B030Const.YOUZAI2_ZAIRYOUHINMEI, srSlipYouzaihyouryouTounyuuSutenyouki)));                // 溶剤②_材料品名
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B030Const.YOUZAI2_TYOUGOURYOUKIKAKU, srSlipYouzaihyouryouTounyuuSutenyouki)));            // 溶剤②_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B030Const.YOUZAI2_BUZAIZAIKOLOTNO1, srSlipYouzaihyouryouTounyuuSutenyouki)));                  // 溶剤②_部材在庫No1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B030Const.YOUZAI2_TYOUGOURYOU1, srSlipYouzaihyouryouTounyuuSutenyouki)));                         // 溶剤②_調合量1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B030Const.YOUZAI2_BUZAIZAIKOLOTNO2, srSlipYouzaihyouryouTounyuuSutenyouki)));                  // 溶剤②_部材在庫No2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B030Const.YOUZAI2_TYOUGOURYOU2, srSlipYouzaihyouryouTounyuuSutenyouki)));                         // 溶剤②_調合量2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B030Const.KAKUHANKI, srSlipYouzaihyouryouTounyuuSutenyouki)));                            // 撹拌機
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B030Const.KAKUHANJIKAN, srSlipYouzaihyouryouTounyuuSutenyouki)));                         // 撹拌時間
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B030Const.KAKUHANKAISI_DAY, srSlipYouzaihyouryouTounyuuSutenyouki),
                "".equals(kakuhankaisiTime) ? "0000" : kakuhankaisiTime));                                                                                                   // 撹拌開始日時
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B030Const.KAKUHANSYUURYOU_DAY, srSlipYouzaihyouryouTounyuuSutenyouki),
                "".equals(kakuhansyuuryouTime) ? "0000" : kakuhansyuuryouTime));                                                                                             // 撹拌終了日時
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B030Const.BINDERKONGOUSETUB, srSlipYouzaihyouryouTounyuuSutenyouki)));                    // ﾊﾞｲﾝﾀﾞｰ混合設備
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B030Const.SETUBISIZE, srSlipYouzaihyouryouTounyuuSutenyouki)));                           // 設備ｻｲｽﾞ
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B030Const.BINDERKONGOUGOKI, srSlipYouzaihyouryouTounyuuSutenyouki)));                     // ﾊﾞｲﾝﾀﾞｰ混合号機
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B030Const.KONGOUTANKSYURUI, srSlipYouzaihyouryouTounyuuSutenyouki)));                     // 混合ﾀﾝｸ種類
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B030Const.KONGOUTANKNO, srSlipYouzaihyouryouTounyuuSutenyouki)));                                 // 混合ﾀﾝｸNo
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B030Const.TANKNAISENJYOUKAKUNIN, srSlipYouzaihyouryouTounyuuSutenyouki), 9));                           // ﾀﾝｸ内洗浄確認
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B030Const.TANKNAIUTIBUKUROKAKUNIN, srSlipYouzaihyouryouTounyuuSutenyouki), 9));                         // ﾀﾝｸ内内袋確認
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B030Const.KAKUHANHANESENJYOUKAKUNIN, srSlipYouzaihyouryouTounyuuSutenyouki), 9));                       // 撹拌羽根洗浄確認
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B030Const.KAKUHANJIKUSENJYOUKAKUNIN, srSlipYouzaihyouryouTounyuuSutenyouki), 9));                       // 撹拌軸洗浄確認
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B030Const.TENRYUUBANNOTAKASA, srSlipYouzaihyouryouTounyuuSutenyouki)));                   // 転流板の高さ
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B030Const.TANKNIEARTHGRIPSETUZOKU, srSlipYouzaihyouryouTounyuuSutenyouki), 9));                         // ﾀﾝｸにｱｰｽｸﾞﾘｯﾌﾟ接続
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B030Const.ZUNSANZAIYOUZAITOUNYUU, srSlipYouzaihyouryouTounyuuSutenyouki), 9));                          // 分散材溶剤投入
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B030Const.YOUZAITOUNYUU1, srSlipYouzaihyouryouTounyuuSutenyouki), 9));                                  // 溶剤投入①
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B030Const.YOUZAITOUNYUU2, srSlipYouzaihyouryouTounyuuSutenyouki), 9));                                  // 溶剤投入②
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B030Const.YOUZAITOUNYUUTANTOUSYA, srSlipYouzaihyouryouTounyuuSutenyouki)));                    // 溶剤投入担当者
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B030Const.BIKOU1, srSlipYouzaihyouryouTounyuuSutenyouki)));                                    // 備考1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B030Const.BIKOU2, srSlipYouzaihyouryouTounyuuSutenyouki)));                                    // 備考2

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
     * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)(sr_slip_youzaihyouryou_tounyuu_sutenyouki)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSrSlipYouzaihyouryouTounyuuSutenyouki(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sr_slip_youzaihyouryou_tounyuu_sutenyouki "
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
     * [ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_slip_youzaihyouryou_tounyuu_sutenyouki "
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
     * @param srSlipYouzaihyouryouTounyuuSutenyouki ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)データ
     * @return DB値
     */
    private String getSrSlipYouzaihyouryouTounyuuSutenyoukiItemData(String itemId, SrSlipYouzaihyouryouTounyuuSutenyouki srSlipYouzaihyouryouTounyuuSutenyouki) {
        switch (itemId) {

            // ｽﾘｯﾌﾟ品名
            case GXHDO102B030Const.SLIPHINMEI:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getSliphinmei());

            // ｽﾘｯﾌﾟLotNo
            case GXHDO102B030Const.SLIPLOTNO:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getSliplotno());

            // ﾛｯﾄ区分
            case GXHDO102B030Const.LOTKUBUN:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getLotkubun());

            // 原料記号
            case GXHDO102B030Const.GENRYOUKIGOU:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getGenryoukigou());

            // 秤量号機
            case GXHDO102B030Const.GOKI:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getGoki());

            // 誘電体ｽﾗﾘｰ重量
            case GXHDO102B030Const.YUUDENTAISLURRYJYUURYOU:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getYuudentaislurryjyuuryou());

            // 誘電体ｽﾗﾘｰ_材料品名
            case GXHDO102B030Const.YUUDENTAISLURRY_ZAIRYOUHINMEI:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getYuudentaislurry_zairyouhinmei());

            // 誘電体ｽﾗﾘｰ_調合量規格
            case GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOUKIKAKU:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getYuudentaislurry_tyougouryoukikaku());

            // 誘電体ｽﾗﾘｰ_部材在庫No1
            case GXHDO102B030Const.YUUDENTAISLURRY_BUZAIZAIKOLOTNO1:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getYuudentaislurry_buzaizaikolotno1());

            // 誘電体ｽﾗﾘｰ_調合量1
            case GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getYuudentaislurry_tyougouryou1());

            // 誘電体ｽﾗﾘｰ_部材在庫No2
            case GXHDO102B030Const.YUUDENTAISLURRY_BUZAIZAIKOLOTNO2:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getYuudentaislurry_buzaizaikolotno2());

            // 誘電体ｽﾗﾘｰ_調合量2
            case GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getYuudentaislurry_tyougouryou2());

            // 担当者
            case GXHDO102B030Const.TANTOUSYA:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getTantousya());

            // 誘電体ｽﾗﾘｰ投入①
            case GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU1:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getYuudentaislurrytounyuu1()));

            // 誘電体ｽﾗﾘｰ投入②
            case GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU2:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getYuudentaislurrytounyuu2()));

            // 誘電体ｽﾗﾘｰ投入③
            case GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU3:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getYuudentaislurrytounyuu3()));

            // 誘電体ｽﾗﾘｰ投入④
            case GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU4:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getYuudentaislurrytounyuu4()));

            // 誘電体ｽﾗﾘｰ投入⑤
            case GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU5:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getYuudentaislurrytounyuu5()));

            // 誘電体ｽﾗﾘｰ投入⑥
            case GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU6:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getYuudentaislurrytounyuu6()));

            // 誘電体ｽﾗﾘｰ投入担当者
            case GXHDO102B030Const.YUUDENTAISLURRYTOUNYUUTANTOUSYA:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getYuudentaislurrytounyuutantousya());

            // 溶剤調整量
            case GXHDO102B030Const.YOUZAITYOUSEIRYOU:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getYouzaityouseiryou());

            // ﾄﾙｴﾝ調整量
            case GXHDO102B030Const.TOLUENETYOUSEIRYOU:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getToluenetyouseiryou());

            // ｿﾙﾐｯｸｽ調整量
            case GXHDO102B030Const.SOLMIXTYOUSEIRYOU:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getSolmixtyouseiryou());

            // 溶剤秤量日
            case GXHDO102B030Const.YOUZAIKEIRYOU_DAY:
                return DateUtil.formattedTimestamp(srSlipYouzaihyouryouTounyuuSutenyouki.getYouzaikeiryounichiji(), "yyMMdd");

            // 溶剤秤量時間
            case GXHDO102B030Const.YOUZAIKEIRYOU_TIME:
                return DateUtil.formattedTimestamp(srSlipYouzaihyouryouTounyuuSutenyouki.getYouzaikeiryounichiji(), "HHmm");

            // 分散材①_材料品名
            case GXHDO102B030Const.ZUNSANZAI1_ZAIRYOUHINMEI:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getZunsanzai1_zairyouhinmei());

            // 分散材①_調合量規格
            case GXHDO102B030Const.ZUNSANZAI1_TYOUGOURYOUKIKAKU:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getZunsanzai1_tyougouryoukikaku());

            // 分散材①_部材在庫No1
            case GXHDO102B030Const.ZUNSANZAI1_BUZAIZAIKOLOTNO1:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getZunsanzai1_buzaizaikolotno1());

            // 分散材①_調合量1
            case GXHDO102B030Const.ZUNSANZAI1_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getZunsanzai1_tyougouryou1());

            // 分散材①_部材在庫No2
            case GXHDO102B030Const.ZUNSANZAI1_BUZAIZAIKOLOTNO2:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getZunsanzai1_buzaizaikolotno2());

            // 分散材①_調合量2
            case GXHDO102B030Const.ZUNSANZAI1_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getZunsanzai1_tyougouryou2());

            // 分散材②_材料品名
            case GXHDO102B030Const.ZUNSANZAI2_ZAIRYOUHINMEI:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getZunsanzai2_zairyouhinmei());

            // 分散材②_調合量規格
            case GXHDO102B030Const.ZUNSANZAI2_TYOUGOURYOUKIKAKU:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getZunsanzai2_tyougouryoukikaku());

            // 分散材②_部材在庫No1
            case GXHDO102B030Const.ZUNSANZAI2_BUZAIZAIKOLOTNO1:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getZunsanzai2_buzaizaikolotno1());

            // 分散材②_調合量1
            case GXHDO102B030Const.ZUNSANZAI2_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getZunsanzai2_tyougouryou1());

            // 分散材②_部材在庫No2
            case GXHDO102B030Const.ZUNSANZAI2_BUZAIZAIKOLOTNO2:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getZunsanzai2_buzaizaikolotno2());

            // 分散材②_調合量2
            case GXHDO102B030Const.ZUNSANZAI2_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getZunsanzai2_tyougouryou2());

            // 溶剤①_材料品名
            case GXHDO102B030Const.YOUZAI1_ZAIRYOUHINMEI:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getYouzai1_zairyouhinmei());

            // 溶剤①_調合量規格
            case GXHDO102B030Const.YOUZAI1_TYOUGOURYOUKIKAKU:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getYouzai1_tyougouryoukikaku());

            // 溶剤①_部材在庫No1
            case GXHDO102B030Const.YOUZAI1_BUZAIZAIKOLOTNO1:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getYouzai1_buzaizaikolotno1());

            // 溶剤①_調合量1
            case GXHDO102B030Const.YOUZAI1_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getYouzai1_tyougouryou1());

            // 溶剤①_部材在庫No2
            case GXHDO102B030Const.YOUZAI1_BUZAIZAIKOLOTNO2:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getYouzai1_buzaizaikolotno2());

            // 溶剤①_調合量2
            case GXHDO102B030Const.YOUZAI1_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getYouzai1_tyougouryou2());

            // 溶剤②_材料品名
            case GXHDO102B030Const.YOUZAI2_ZAIRYOUHINMEI:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getYouzai2_zairyouhinmei());

            // 溶剤②_調合量規格
            case GXHDO102B030Const.YOUZAI2_TYOUGOURYOUKIKAKU:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getYouzai2_tyougouryoukikaku());

            // 溶剤②_部材在庫No1
            case GXHDO102B030Const.YOUZAI2_BUZAIZAIKOLOTNO1:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getYouzai2_buzaizaikolotno1());

            // 溶剤②_調合量1
            case GXHDO102B030Const.YOUZAI2_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getYouzai2_tyougouryou1());

            // 溶剤②_部材在庫No2
            case GXHDO102B030Const.YOUZAI2_BUZAIZAIKOLOTNO2:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getYouzai2_buzaizaikolotno2());

            // 溶剤②_調合量2
            case GXHDO102B030Const.YOUZAI2_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getYouzai2_tyougouryou2());

            // 撹拌機
            case GXHDO102B030Const.KAKUHANKI:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getKakuhanki());

            // 撹拌時間
            case GXHDO102B030Const.KAKUHANJIKAN:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getKakuhanjikan());

            // 撹拌開始日
            case GXHDO102B030Const.KAKUHANKAISI_DAY:
                return DateUtil.formattedTimestamp(srSlipYouzaihyouryouTounyuuSutenyouki.getKakuhankaisinichiji(), "yyMMdd");

            // 撹拌開始時間
            case GXHDO102B030Const.KAKUHANKAISI_TIME:
                return DateUtil.formattedTimestamp(srSlipYouzaihyouryouTounyuuSutenyouki.getKakuhankaisinichiji(), "HHmm");

            // 撹拌終了日
            case GXHDO102B030Const.KAKUHANSYUURYOU_DAY:
                return DateUtil.formattedTimestamp(srSlipYouzaihyouryouTounyuuSutenyouki.getKakuhansyuuryounichiji(), "yyMMdd");

            // 撹拌終了時間
            case GXHDO102B030Const.KAKUHANSYUURYOU_TIME:
                return DateUtil.formattedTimestamp(srSlipYouzaihyouryouTounyuuSutenyouki.getKakuhansyuuryounichiji(), "HHmm");

            // ﾊﾞｲﾝﾀﾞｰ混合設備
            case GXHDO102B030Const.BINDERKONGOUSETUB:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getBinderkongousetub());

            // 設備ｻｲｽﾞ
            case GXHDO102B030Const.SETUBISIZE:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getSetubisize());

            // ﾊﾞｲﾝﾀﾞｰ混合号機
            case GXHDO102B030Const.BINDERKONGOUGOKI:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getBinderkongougoki());

            // 混合ﾀﾝｸ種類
            case GXHDO102B030Const.KONGOUTANKSYURUI:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getKongoutanksyurui());

            // 混合ﾀﾝｸNo
            case GXHDO102B030Const.KONGOUTANKNO:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getKongoutankno());

            // ﾀﾝｸ内洗浄確認
            case GXHDO102B030Const.TANKNAISENJYOUKAKUNIN:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getTanknaisenjyoukakunin()));

            // ﾀﾝｸ内内袋確認
            case GXHDO102B030Const.TANKNAIUTIBUKUROKAKUNIN:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getTanknaiutibukurokakunin()));

            // 撹拌羽根洗浄確認
            case GXHDO102B030Const.KAKUHANHANESENJYOUKAKUNIN:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getKakuhanhanesenjyoukakunin()));

            // 撹拌軸洗浄確認
            case GXHDO102B030Const.KAKUHANJIKUSENJYOUKAKUNIN:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getKakuhanjikusenjyoukakunin()));

            // 転流板の高さ
            case GXHDO102B030Const.TENRYUUBANNOTAKASA:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getTenryuubannotakasa());

            // ﾀﾝｸにｱｰｽｸﾞﾘｯﾌﾟ接続
            case GXHDO102B030Const.TANKNIEARTHGRIPSETUZOKU:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getTankniearthgripsetuzoku()));

            // 分散材溶剤投入
            case GXHDO102B030Const.ZUNSANZAIYOUZAITOUNYUU:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getZunsanzaiyouzaitounyuu()));

            // 溶剤投入①
            case GXHDO102B030Const.YOUZAITOUNYUU1:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getYouzaitounyuu1()));

            // 溶剤投入②
            case GXHDO102B030Const.YOUZAITOUNYUU2:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getYouzaitounyuu2()));

            // 溶剤投入担当者
            case GXHDO102B030Const.YOUZAITOUNYUUTANTOUSYA:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getYouzaitounyuutantousya());

            // 備考1
            case GXHDO102B030Const.BIKOU1:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getBikou1());

            // 備考2
            case GXHDO102B030Const.BIKOU2:
                return StringUtil.nullToBlank(srSlipYouzaihyouryouTounyuuSutenyouki.getBikou2());

            default:
                return null;
        }
    }

    /**
     * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)_仮登録(tmp_sr_slip_youzaihyouryou_tounyuu_sutenyouki)登録処理(削除時)
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
    private void insertDeleteDataTmpSrSlipYouzaihyouryouTounyuuSutenyouki(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_slip_youzaihyouryou_tounyuu_sutenyouki ("
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,goki,yuudentaislurryjyuuryou,yuudentaislurry_zairyouhinmei,yuudentaislurry_tyougouryoukikaku,"
                + "yuudentaislurry_buzaizaikolotno1,yuudentaislurry_tyougouryou1,yuudentaislurry_buzaizaikolotno2,yuudentaislurry_tyougouryou2,tantousya,yuudentaislurrytounyuu1,"
                + "yuudentaislurrytounyuu2,yuudentaislurrytounyuu3,yuudentaislurrytounyuu4,yuudentaislurrytounyuu5,yuudentaislurrytounyuu6,yuudentaislurrytounyuutantousya,"
                + "youzaityouseiryou,toluenetyouseiryou,solmixtyouseiryou,youzaikeiryounichiji,zunsanzai1_zairyouhinmei,zunsanzai1_tyougouryoukikaku,zunsanzai1_buzaizaikolotno1,"
                + "zunsanzai1_tyougouryou1,zunsanzai1_buzaizaikolotno2,zunsanzai1_tyougouryou2,zunsanzai2_zairyouhinmei,zunsanzai2_tyougouryoukikaku,zunsanzai2_buzaizaikolotno1,"
                + "zunsanzai2_tyougouryou1,zunsanzai2_buzaizaikolotno2,zunsanzai2_tyougouryou2,youzai1_zairyouhinmei,youzai1_tyougouryoukikaku,youzai1_buzaizaikolotno1,"
                + "youzai1_tyougouryou1,youzai1_buzaizaikolotno2,youzai1_tyougouryou2,youzai2_zairyouhinmei,youzai2_tyougouryoukikaku,youzai2_buzaizaikolotno1,youzai2_tyougouryou1,"
                + "youzai2_buzaizaikolotno2,youzai2_tyougouryou2,kakuhanki,kakuhanjikan,kakuhankaisinichiji,kakuhansyuuryounichiji,binderkongousetub,setubisize,binderkongougoki,"
                + "kongoutanksyurui,kongoutankno,tanknaisenjyoukakunin,tanknaiutibukurokakunin,kakuhanhanesenjyoukakunin,kakuhanjikusenjyoukakunin,tenryuubannotakasa,"
                + "tankniearthgripsetuzoku,zunsanzaiyouzaitounyuu,youzaitounyuu1,youzaitounyuu2,youzaitounyuutantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") SELECT "
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,goki,yuudentaislurryjyuuryou,yuudentaislurry_zairyouhinmei,yuudentaislurry_tyougouryoukikaku,"
                + "yuudentaislurry_buzaizaikolotno1,yuudentaislurry_tyougouryou1,yuudentaislurry_buzaizaikolotno2,yuudentaislurry_tyougouryou2,tantousya,yuudentaislurrytounyuu1,"
                + "yuudentaislurrytounyuu2,yuudentaislurrytounyuu3,yuudentaislurrytounyuu4,yuudentaislurrytounyuu5,yuudentaislurrytounyuu6,yuudentaislurrytounyuutantousya,"
                + "youzaityouseiryou,toluenetyouseiryou,solmixtyouseiryou,youzaikeiryounichiji,zunsanzai1_zairyouhinmei,zunsanzai1_tyougouryoukikaku,zunsanzai1_buzaizaikolotno1,"
                + "zunsanzai1_tyougouryou1,zunsanzai1_buzaizaikolotno2,zunsanzai1_tyougouryou2,zunsanzai2_zairyouhinmei,zunsanzai2_tyougouryoukikaku,zunsanzai2_buzaizaikolotno1,"
                + "zunsanzai2_tyougouryou1,zunsanzai2_buzaizaikolotno2,zunsanzai2_tyougouryou2,youzai1_zairyouhinmei,youzai1_tyougouryoukikaku,youzai1_buzaizaikolotno1,"
                + "youzai1_tyougouryou1,youzai1_buzaizaikolotno2,youzai1_tyougouryou2,youzai2_zairyouhinmei,youzai2_tyougouryoukikaku,youzai2_buzaizaikolotno1,youzai2_tyougouryou1,"
                + "youzai2_buzaizaikolotno2,youzai2_tyougouryou2,kakuhanki,kakuhanjikan,kakuhankaisinichiji,kakuhansyuuryounichiji,binderkongousetub,setubisize,binderkongougoki,"
                + "kongoutanksyurui,kongoutankno,tanknaisenjyoukakunin,tanknaiutibukurokakunin,kakuhanhanesenjyoukakunin,kakuhanjikusenjyoukakunin,tenryuubannotakasa,"
                + "tankniearthgripsetuzoku,zunsanzaiyouzaitounyuu,youzaitounyuu1,youzaitounyuu2,youzaitounyuutantousya,bikou1,bikou2,?,?,?,? "
                + " FROM sr_slip_youzaihyouryou_tounyuu_sutenyouki "
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
    private void initGXHDO102B030A(ProcessData processData) {
        GXHDO102B030A bean = (GXHDO102B030A) getFormBean("gXHDO102B030A");
        bean.setWiplotno(getItemRow(processData.getItemList(), GXHDO102B030Const.WIPLOTNO));
        bean.setSliphinmei(getItemRow(processData.getItemList(), GXHDO102B030Const.SLIPHINMEI));
        bean.setSliplotno(getItemRow(processData.getItemList(), GXHDO102B030Const.SLIPLOTNO));
        bean.setLotkubun(getItemRow(processData.getItemList(), GXHDO102B030Const.LOTKUBUN));
        bean.setGenryoukigou(getItemRow(processData.getItemList(), GXHDO102B030Const.GENRYOUKIGOU));
        bean.setGoki(getItemRow(processData.getItemList(), GXHDO102B030Const.GOKI));
        bean.setYuudentaislurryjyuuryou(getItemRow(processData.getItemList(), GXHDO102B030Const.YUUDENTAISLURRYJYUURYOU));
        bean.setYuudentaislurry_zairyouhinmei(getItemRow(processData.getItemList(), GXHDO102B030Const.YUUDENTAISLURRY_ZAIRYOUHINMEI));
        bean.setYuudentaislurry_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOUKIKAKU));
        bean.setYuudentaislurry_buzaizaikolotno1(getItemRow(processData.getItemList(), GXHDO102B030Const.YUUDENTAISLURRY_BUZAIZAIKOLOTNO1));
        bean.setYuudentaislurry_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOU1));
        bean.setYuudentaislurry_buzaizaikolotno2(getItemRow(processData.getItemList(), GXHDO102B030Const.YUUDENTAISLURRY_BUZAIZAIKOLOTNO2));
        bean.setYuudentaislurry_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOU2));
        bean.setTantousya(getItemRow(processData.getItemList(), GXHDO102B030Const.TANTOUSYA));
        bean.setYuudentaislurrytounyuu1(getItemRow(processData.getItemList(), GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU1));
        bean.setYuudentaislurrytounyuu2(getItemRow(processData.getItemList(), GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU2));
        bean.setYuudentaislurrytounyuu3(getItemRow(processData.getItemList(), GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU3));
        bean.setYuudentaislurrytounyuu4(getItemRow(processData.getItemList(), GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU4));
        bean.setYuudentaislurrytounyuu5(getItemRow(processData.getItemList(), GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU5));
        bean.setYuudentaislurrytounyuu6(getItemRow(processData.getItemList(), GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU6));
        bean.setYuudentaislurrytounyuutantousya(getItemRow(processData.getItemList(), GXHDO102B030Const.YUUDENTAISLURRYTOUNYUUTANTOUSYA));
        bean.setYouzaityouseiryou(getItemRow(processData.getItemList(), GXHDO102B030Const.YOUZAITYOUSEIRYOU));
        bean.setToluenetyouseiryou(getItemRow(processData.getItemList(), GXHDO102B030Const.TOLUENETYOUSEIRYOU));
        bean.setSolmixtyouseiryou(getItemRow(processData.getItemList(), GXHDO102B030Const.SOLMIXTYOUSEIRYOU));
        bean.setYouzaikeiryou_day(getItemRow(processData.getItemList(), GXHDO102B030Const.YOUZAIKEIRYOU_DAY));
        bean.setYouzaikeiryou_time(getItemRow(processData.getItemList(), GXHDO102B030Const.YOUZAIKEIRYOU_TIME));
        bean.setZunsanzai1_zairyouhinmei(getItemRow(processData.getItemList(), GXHDO102B030Const.ZUNSANZAI1_ZAIRYOUHINMEI));
        bean.setZunsanzai1_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B030Const.ZUNSANZAI1_TYOUGOURYOUKIKAKU));
        bean.setZunsanzai1_buzaizaikolotno1(getItemRow(processData.getItemList(), GXHDO102B030Const.ZUNSANZAI1_BUZAIZAIKOLOTNO1));
        bean.setZunsanzai1_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B030Const.ZUNSANZAI1_TYOUGOURYOU1));
        bean.setZunsanzai1_buzaizaikolotno2(getItemRow(processData.getItemList(), GXHDO102B030Const.ZUNSANZAI1_BUZAIZAIKOLOTNO2));
        bean.setZunsanzai1_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B030Const.ZUNSANZAI1_TYOUGOURYOU2));
        bean.setZunsanzai2_zairyouhinmei(getItemRow(processData.getItemList(), GXHDO102B030Const.ZUNSANZAI2_ZAIRYOUHINMEI));
        bean.setZunsanzai2_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B030Const.ZUNSANZAI2_TYOUGOURYOUKIKAKU));
        bean.setZunsanzai2_buzaizaikolotno1(getItemRow(processData.getItemList(), GXHDO102B030Const.ZUNSANZAI2_BUZAIZAIKOLOTNO1));
        bean.setZunsanzai2_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B030Const.ZUNSANZAI2_TYOUGOURYOU1));
        bean.setZunsanzai2_buzaizaikolotno2(getItemRow(processData.getItemList(), GXHDO102B030Const.ZUNSANZAI2_BUZAIZAIKOLOTNO2));
        bean.setZunsanzai2_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B030Const.ZUNSANZAI2_TYOUGOURYOU2));
        bean.setYouzai1_zairyouhinmei(getItemRow(processData.getItemList(), GXHDO102B030Const.YOUZAI1_ZAIRYOUHINMEI));
        bean.setYouzai1_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B030Const.YOUZAI1_TYOUGOURYOUKIKAKU));
        bean.setYouzai1_buzaizaikolotno1(getItemRow(processData.getItemList(), GXHDO102B030Const.YOUZAI1_BUZAIZAIKOLOTNO1));
        bean.setYouzai1_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B030Const.YOUZAI1_TYOUGOURYOU1));
        bean.setYouzai1_buzaizaikolotno2(getItemRow(processData.getItemList(), GXHDO102B030Const.YOUZAI1_BUZAIZAIKOLOTNO2));
        bean.setYouzai1_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B030Const.YOUZAI1_TYOUGOURYOU2));
        bean.setYouzai2_zairyouhinmei(getItemRow(processData.getItemList(), GXHDO102B030Const.YOUZAI2_ZAIRYOUHINMEI));
        bean.setYouzai2_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B030Const.YOUZAI2_TYOUGOURYOUKIKAKU));
        bean.setYouzai2_buzaizaikolotno1(getItemRow(processData.getItemList(), GXHDO102B030Const.YOUZAI2_BUZAIZAIKOLOTNO1));
        bean.setYouzai2_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B030Const.YOUZAI2_TYOUGOURYOU1));
        bean.setYouzai2_buzaizaikolotno2(getItemRow(processData.getItemList(), GXHDO102B030Const.YOUZAI2_BUZAIZAIKOLOTNO2));
        bean.setYouzai2_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B030Const.YOUZAI2_TYOUGOURYOU2));
        bean.setKakuhanki(getItemRow(processData.getItemList(), GXHDO102B030Const.KAKUHANKI));
        bean.setKakuhanjikan(getItemRow(processData.getItemList(), GXHDO102B030Const.KAKUHANJIKAN));
        bean.setKakuhankaisi_day(getItemRow(processData.getItemList(), GXHDO102B030Const.KAKUHANKAISI_DAY));
        bean.setKakuhankaisi_time(getItemRow(processData.getItemList(), GXHDO102B030Const.KAKUHANKAISI_TIME));
        bean.setKakuhansyuuryou_day(getItemRow(processData.getItemList(), GXHDO102B030Const.KAKUHANSYUURYOU_DAY));
        bean.setKakuhansyuuryou_time(getItemRow(processData.getItemList(), GXHDO102B030Const.KAKUHANSYUURYOU_TIME));
        bean.setBinderkongousetub(getItemRow(processData.getItemList(), GXHDO102B030Const.BINDERKONGOUSETUB));
        bean.setSetubisize(getItemRow(processData.getItemList(), GXHDO102B030Const.SETUBISIZE));
        bean.setBinderkongougoki(getItemRow(processData.getItemList(), GXHDO102B030Const.BINDERKONGOUGOKI));
        bean.setKongoutanksyurui(getItemRow(processData.getItemList(), GXHDO102B030Const.KONGOUTANKSYURUI));
        bean.setKongoutankno(getItemRow(processData.getItemList(), GXHDO102B030Const.KONGOUTANKNO));
        bean.setTanknaisenjyoukakunin(getItemRow(processData.getItemList(), GXHDO102B030Const.TANKNAISENJYOUKAKUNIN));
        bean.setTanknaiutibukurokakunin(getItemRow(processData.getItemList(), GXHDO102B030Const.TANKNAIUTIBUKUROKAKUNIN));
        bean.setKakuhanhanesenjyoukakunin(getItemRow(processData.getItemList(), GXHDO102B030Const.KAKUHANHANESENJYOUKAKUNIN));
        bean.setKakuhanjikusenjyoukakunin(getItemRow(processData.getItemList(), GXHDO102B030Const.KAKUHANJIKUSENJYOUKAKUNIN));
        bean.setTenryuubannotakasa(getItemRow(processData.getItemList(), GXHDO102B030Const.TENRYUUBANNOTAKASA));
        bean.setTankniearthgripsetuzoku(getItemRow(processData.getItemList(), GXHDO102B030Const.TANKNIEARTHGRIPSETUZOKU));
        bean.setZunsanzaiyouzaitounyuu(getItemRow(processData.getItemList(), GXHDO102B030Const.ZUNSANZAIYOUZAITOUNYUU));
        bean.setYouzaitounyuu1(getItemRow(processData.getItemList(), GXHDO102B030Const.YOUZAITOUNYUU1));
        bean.setYouzaitounyuu2(getItemRow(processData.getItemList(), GXHDO102B030Const.YOUZAITOUNYUU2));
        bean.setYouzaitounyuutantousya(getItemRow(processData.getItemList(), GXHDO102B030Const.YOUZAITOUNYUUTANTOUSYA));
        bean.setBikou1(getItemRow(processData.getItemList(), GXHDO102B030Const.BIKOU1));
        bean.setBikou2(getItemRow(processData.getItemList(), GXHDO102B030Const.BIKOU2));

    }

    /**
     * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力_ｻﾌﾞ画面データの規格値取得処理
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
     * 誘電体ｽﾗﾘｰ_材料品名のﾘﾝｸ押下時、 ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC015SubGamen1(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B030Const.YUUDENTAISLURRY_BUZAIZAIKOLOTNO1, GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOU1,
                GXHDO102B030Const.YUUDENTAISLURRY_BUZAIZAIKOLOTNO2, GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOU2);
        return openC015SubGamen(processData, 1, returnItemIdList, GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOUKIKAKU);
    }

    /**
     * 分散材1_材料品名のﾘﾝｸ押下時、 ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC015SubGamen2(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B030Const.ZUNSANZAI1_BUZAIZAIKOLOTNO1, GXHDO102B030Const.ZUNSANZAI1_TYOUGOURYOU1,
                GXHDO102B030Const.ZUNSANZAI1_BUZAIZAIKOLOTNO2, GXHDO102B030Const.ZUNSANZAI1_TYOUGOURYOU2);
        return openC015SubGamen(processData, 2, returnItemIdList, GXHDO102B030Const.ZUNSANZAI1_TYOUGOURYOUKIKAKU);
    }

    /**
     * 分散材2_材料品名のﾘﾝｸ押下時、 ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC015SubGamen3(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B030Const.ZUNSANZAI2_BUZAIZAIKOLOTNO1, GXHDO102B030Const.ZUNSANZAI2_TYOUGOURYOU1,
                GXHDO102B030Const.ZUNSANZAI2_BUZAIZAIKOLOTNO2, GXHDO102B030Const.ZUNSANZAI2_TYOUGOURYOU2);
        return openC015SubGamen(processData, 3, returnItemIdList, GXHDO102B030Const.ZUNSANZAI2_TYOUGOURYOUKIKAKU);
    }

    /**
     * 溶剤1_材料品名のﾘﾝｸ押下時、 ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC015SubGamen4(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B030Const.YOUZAI1_BUZAIZAIKOLOTNO1, GXHDO102B030Const.YOUZAI1_TYOUGOURYOU1,
                GXHDO102B030Const.YOUZAI1_BUZAIZAIKOLOTNO2, GXHDO102B030Const.YOUZAI1_TYOUGOURYOU2);
        return openC015SubGamen(processData, 4, returnItemIdList, GXHDO102B030Const.YOUZAI1_TYOUGOURYOUKIKAKU);
    }

    /**
     * 溶剤2_材料品名のﾘﾝｸ押下時、 ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC015SubGamen5(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B030Const.YOUZAI2_BUZAIZAIKOLOTNO1, GXHDO102B030Const.YOUZAI2_TYOUGOURYOU1,
                GXHDO102B030Const.YOUZAI2_BUZAIZAIKOLOTNO2, GXHDO102B030Const.YOUZAI2_TYOUGOURYOU2);
        return openC015SubGamen(processData, 5, returnItemIdList, GXHDO102B030Const.YOUZAI2_TYOUGOURYOUKIKAKU);
    }

    /**
     * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @param zairyokubun 材料区分
     * @param returnItemIdList サブ画面から戻ったときに値を設定必要項目リスト
     * @param tyougouryoukikakuItemId 調合量規格項目ID
     * @return 処理制御データ
     */
    public ProcessData openC015SubGamen(ProcessData processData, int zairyokubun, List<String> returnItemIdList, String tyougouryoukikakuItemId) {
        try {
            // 「秤量号機」
            FXHDD01 itemGoki = getItemRow(processData.getItemList(), GXHDO102B030Const.GOKI);
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
            processData.setCollBackParam("gxhdo102c015");

            GXHDO102C015 beanGXHDO102C015 = (GXHDO102C015) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C015);
            GXHDO102C015Model gxhdo102c015model = beanGXHDO102C015.getGxhdO102c015Model();
            // 主画面からサブ画面に渡されたデータを設定
            setSubGamenInitData(processData, gxhdo102c015model, zairyokubun, itemGoki, returnItemIdList, tyougouryoukikakuItemId);

            beanGXHDO102C015.setGxhdO102c015ModelView(gxhdo102c015model.clone());
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
     * @param gxhdo102c015model モデルデータ
     * @param zairyokubun 材料区分
     * @param itemGoki 秤量号機データ
     * @param returnItemIdList サブ画面から戻るデータリスト
     * @param tyougouryoukikakuItemId 調合量規格項目ID
     * @throws CloneNotSupportedException 例外エラー
     */
    private void setSubGamenInitData(ProcessData processData, GXHDO102C015Model gxhdo102c015model, int zairyokubun, FXHDD01 itemGoki, List<String> returnItemIdList,
            String tyougouryoukikakuItemId) throws CloneNotSupportedException {
        GXHDO102C015Model.SubGamenData c015subgamendata = GXHDO102C015Logic.getC015subgamendata(gxhdo102c015model, zairyokubun);
        if (c015subgamendata == null) {
            return;
        }
        c015subgamendata.setSubDataGoki(StringUtil.nullToBlank(itemGoki.getValue()));
        c015subgamendata.setSubDataZairyokubun(zairyokubun);

        String youzai_tyougouryoukikakuVal = getFXHDD01KikakuChiValue(getItemRow(processData.getItemList(), tyougouryoukikakuItemId)); // 調合規格
        c015subgamendata.getSubDataTyogouryoukikaku().setValue(youzai_tyougouryoukikakuVal.replace("【", "").replace("】", ""));
        // サブ画面から戻ったときに値を設定する項目を指定する。
        c015subgamendata.setReturnItemIdBuzailotno1(returnItemIdList.get(0)); // 部材在庫No.X_1
        c015subgamendata.setReturnItemIdTyougouryou1(returnItemIdList.get(1)); // 調合量X_1
        c015subgamendata.setReturnItemIdBuzailotno2(returnItemIdList.get(2)); // 部材在庫NoX_2
        c015subgamendata.setReturnItemIdTyougouryou2(returnItemIdList.get(3)); // 調合量X_2
        gxhdo102c015model.setShowsubgamendata(c015subgamendata.clone());
        // サブ画面の調合残量の計算
        GXHDO102C015Logic.calcTyogouzanryou(gxhdo102c015model);
    }

    /**
     * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力_ｻﾌﾞ画面データの設定値取得処理
     *
     * @param item 項目情報
     * @return 項目値
     */
    private String getFXHDD01KikakuChiValue(FXHDD01 item) {
        if (item == null) {
            return "";
        }
        return StringUtil.nullToBlank(item.getKikakuChi());
    }

    /**
     * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力_ｻﾌﾞ画面データ設定処理
     *
     * @param processData 処理制御データ
     * @param subSrSlipYouzaihyouryouTounyuuSutenyoukiList
     * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力_ｻﾌﾞ画面データリスト
     */
    private void setInputItemDataSubFormC015(ProcessData processData, List<SubSrSlipYouzaihyouryouTounyuuSutenyouki> subSrSlipYouzaihyouryouTounyuuSutenyoukiList) {
        // サブ画面の情報を取得
        GXHDO102C015 beanGXHDO102C015 = (GXHDO102C015) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C015);

        GXHDO102C015Model model;
        if (subSrSlipYouzaihyouryouTounyuuSutenyoukiList == null) {
            // 登録データが無い場合、主画面の材料品名1-2と調合量規格1-2はｻﾌﾞ画面の初期値にセットする。
            subSrSlipYouzaihyouryouTounyuuSutenyoukiList = new ArrayList<>();
            SubSrSlipYouzaihyouryouTounyuuSutenyouki subgamen1 = new SubSrSlipYouzaihyouryouTounyuuSutenyouki();
            SubSrSlipYouzaihyouryouTounyuuSutenyouki subgamen2 = new SubSrSlipYouzaihyouryouTounyuuSutenyouki();
            SubSrSlipYouzaihyouryouTounyuuSutenyouki subgamen3 = new SubSrSlipYouzaihyouryouTounyuuSutenyouki();
            SubSrSlipYouzaihyouryouTounyuuSutenyouki subgamen4 = new SubSrSlipYouzaihyouryouTounyuuSutenyouki();
            SubSrSlipYouzaihyouryouTounyuuSutenyouki subgamen5 = new SubSrSlipYouzaihyouryouTounyuuSutenyouki();

            subgamen1.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B030Const.YUUDENTAISLURRY_ZAIRYOUHINMEI))); // 誘電体ｽﾗﾘｰ_材料品名
            subgamen1.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOUKIKAKU))); // 誘電体ｽﾗﾘｰ_調合量規格
            subgamen2.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B030Const.ZUNSANZAI1_ZAIRYOUHINMEI))); // 分散材1_材料品名
            subgamen2.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B030Const.ZUNSANZAI1_TYOUGOURYOUKIKAKU))); // 分散材1_調合量規格
            subgamen3.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B030Const.ZUNSANZAI2_ZAIRYOUHINMEI))); // 分散材2_材料品名
            subgamen3.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B030Const.ZUNSANZAI2_TYOUGOURYOUKIKAKU))); // 分散材2_調合量規格
            subgamen4.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B030Const.YOUZAI1_ZAIRYOUHINMEI))); // 溶剤1_材料品名
            subgamen4.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B030Const.YOUZAI1_TYOUGOURYOUKIKAKU))); // 溶剤1_調合量規格
            subgamen5.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B030Const.YOUZAI2_ZAIRYOUHINMEI))); // 溶剤2_材料品名
            subgamen5.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B030Const.YOUZAI2_TYOUGOURYOUKIKAKU))); // 溶剤2_調合量規格
            subSrSlipYouzaihyouryouTounyuuSutenyoukiList.add(subgamen1);
            subSrSlipYouzaihyouryouTounyuuSutenyoukiList.add(subgamen2);
            subSrSlipYouzaihyouryouTounyuuSutenyoukiList.add(subgamen3);
            subSrSlipYouzaihyouryouTounyuuSutenyoukiList.add(subgamen4);
            subSrSlipYouzaihyouryouTounyuuSutenyoukiList.add(subgamen5);
            model = GXHDO102C015Logic.createGXHDO102C015Model(subSrSlipYouzaihyouryouTounyuuSutenyoukiList);

        } else {
            // 登録データがあれば登録データをセットする。
            model = GXHDO102C015Logic.createGXHDO102C015Model(subSrSlipYouzaihyouryouTounyuuSutenyoukiList);
        }
        beanGXHDO102C015.setGxhdO102c015Model(model);
    }

    /**
     * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力_ｻﾌﾞ画面の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力_ｻﾌﾞ画面登録データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrSlipYouzaihyouryouTounyuuSutenyouki> getSubSrSlipYouzaihyouryouTounyuuSutenyoukiData(QueryRunner queryRunnerQcdb,
            String rev, String jotaiFlg, String kojyo, String lotNo, String edaban) throws SQLException {
        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSubSrSlipYouzaihyouryouTounyuuSutenyouki(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSubSrSlipYouzaihyouryouTounyuuSutenyouki(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        }
    }

    /**
     * [ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力_ｻﾌﾞ画面]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrSlipYouzaihyouryouTounyuuSutenyouki> loadSubSrSlipYouzaihyouryouTounyuuSutenyouki(QueryRunner queryRunnerQcdb,
            String kojyo, String lotNo, String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + "kojyo,lotno,edaban,zairyokubun,tyogouryoukikaku,tyogouzanryou,zairyohinmei,"
                + "buzailotno1,buzaihinmei1,tyougouryou1_1,tyougouryou1_2,tyougouryou1_3,tyougouryou1_4,"
                + "tyougouryou1_5,tyougouryou1_6,buzailotno2,buzaihinmei2,tyougouryou2_1,tyougouryou2_2,"
                + "tyougouryou2_3,tyougouryou2_4,tyougouryou2_5,tyougouryou2_6,torokunichiji,kosinnichiji,"
                + "revision, '0' AS deleteflag "
                + " FROM sub_sr_slip_youzaihyouryou_tounyuu_sutenyouki "
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
        ResultSetHandler<List<SubSrSlipYouzaihyouryouTounyuuSutenyouki>> beanHandler = new BeanListHandler<>(SubSrSlipYouzaihyouryouTounyuuSutenyouki.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力_ｻﾌﾞ画面_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrSlipYouzaihyouryouTounyuuSutenyouki> loadTmpSubSrSlipYouzaihyouryouTounyuuSutenyouki(QueryRunner queryRunnerQcdb,
            String kojyo, String lotNo, String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + "kojyo,lotno,edaban,zairyokubun,tyogouryoukikaku,tyogouzanryou,zairyohinmei,"
                + "buzailotno1,buzaihinmei1,tyougouryou1_1,tyougouryou1_2,tyougouryou1_3,tyougouryou1_4,"
                + "tyougouryou1_5,tyougouryou1_6,buzailotno2,buzaihinmei2,tyougouryou2_1,tyougouryou2_2,"
                + "tyougouryou2_3,tyougouryou2_4,tyougouryou2_5,tyougouryou2_6,torokunichiji,kosinnichiji,"
                + "revision, deleteflag "
                + " FROM tmp_sub_sr_slip_youzaihyouryou_tounyuu_sutenyouki "
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
        ResultSetHandler<List<SubSrSlipYouzaihyouryouTounyuuSutenyouki>> beanHandler = new BeanListHandler<>(SubSrSlipYouzaihyouryouTounyuuSutenyouki.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力_サブ画面_仮登録(tmp_sub_sr_slip_youzaihyouryou_tounyuu_sutenyouki)登録処理
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
    private void insertTmpSubSrSlipYouzaihyouryouTounyuuSutenyouki(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal newRev, int deleteflag, String kojyo, String lotNo, String edaban, Integer zairyokubun,
            String systemTime, ProcessData processData) throws SQLException {

        String sql = "INSERT INTO tmp_sub_sr_slip_youzaihyouryou_tounyuu_sutenyouki ( "
                + "kojyo,lotno,edaban,zairyokubun,tyogouryoukikaku,tyogouzanryou,zairyohinmei,"
                + "buzailotno1,buzaihinmei1,tyougouryou1_1,tyougouryou1_2,tyougouryou1_3,tyougouryou1_4,"
                + "tyougouryou1_5,tyougouryou1_6,buzailotno2,buzaihinmei2,tyougouryou2_1,tyougouryou2_2,"
                + "tyougouryou2_3,tyougouryou2_4,tyougouryou2_5,tyougouryou2_6,torokunichiji,kosinnichiji,"
                + "revision, deleteflag "
                + " ) VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";

        List<Object> params = setUpdateParameterTmpSubSrSlipYouzaihyouryouTounyuuSutenyouki(true, newRev, deleteflag, kojyo, lotNo, edaban, zairyokubun, systemTime, null, processData);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力_仮登録(tmp_sub_sr_slip_youzaihyouryou_tounyuu_sutenyouki)更新処理
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
     * @param srSlipYouzaihyouryouTounyuuSutenyouki
     * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)_仮登録更新前データ
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void updateTmpSubSrSlipYouzaihyouryouTounyuuSutenyouki(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal rev, BigDecimal newRev, String kojyo, String lotNo, String edaban, Integer zairyokubun, String systemTime,
            SrSlipYouzaihyouryouTounyuuSutenyouki srSlipYouzaihyouryouTounyuuSutenyouki, ProcessData processData) throws SQLException {

        String sql = "UPDATE tmp_sub_sr_slip_youzaihyouryou_tounyuu_sutenyouki SET "
                + "tyogouryoukikaku = ?,tyogouzanryou = ?,zairyohinmei = ?,"
                + "buzailotno1 = ?,buzaihinmei1 = ?,tyougouryou1_1 = ?,tyougouryou1_2 = ?,tyougouryou1_3 = ?,tyougouryou1_4 = ?,"
                + "tyougouryou1_5 = ?,tyougouryou1_6 = ?,buzailotno2 = ?,buzaihinmei2 = ?,tyougouryou2_1 = ?,tyougouryou2_2 = ?,"
                + "tyougouryou2_3 = ?,tyougouryou2_4 = ?,tyougouryou2_5 = ?,tyougouryou2_6 = ?,kosinnichiji = ?,revision = ?, deleteflag = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND zairyokubun = ? AND revision = ? ";

        List<Object> params = setUpdateParameterTmpSubSrSlipYouzaihyouryouTounyuuSutenyouki(false, newRev, 0, kojyo, lotNo, edaban, zairyokubun, systemTime, srSlipYouzaihyouryouTounyuuSutenyouki, processData);

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
     * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力_サブ画面仮登録(tmp_sub_sr_slip_youzaihyouryou_tounyuu_sutenyouki)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param zairyokubun 材料区分
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param srSlipYouzaihyouryouTounyuuSutenyouki
     * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)_仮登録更新前データ
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSubSrSlipYouzaihyouryouTounyuuSutenyouki(boolean isInsert, BigDecimal newRev,
            int deleteflag, String kojyo, String lotNo, String edaban, Integer zairyokubun, String systemTime,
            SrSlipYouzaihyouryouTounyuuSutenyouki srSlipYouzaihyouryouTounyuuSutenyouki, ProcessData processData) {
        List<FXHDD01> pItemList = processData.getItemList();
        List<Object> params = new ArrayList<>();

        // 子画面情報を取得
        GXHDO102C015 beanGXHDO102C015 = (GXHDO102C015) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C015);
        GXHDO102C015Model gxhdO102c015Model = beanGXHDO102C015.getGxhdO102c015Model();

        // ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力_サブ画面から更新値を取得
        ArrayList<Object> subGamenDataList = getSubGamenData(gxhdO102c015Model, zairyokubun);
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

        // XXX_調合量規格からサブ画面の調合規格がメインの調合量規格と同じように更新する
        String tyougouryoukikakuValue = getItemKikakuchi(pItemList, getTyougouryoukikakuItemId(zairyokubun), srSlipYouzaihyouryouTounyuuSutenyouki);

        params.add(DBUtil.stringToStringObjectDefaultNull(tyougouryoukikakuValue)); // 調合量規格
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
     * 材料区分によってXXX_調合量規格の項目IDを取得
     *
     * @param zairyokubun 材料区分
     * @return 項目ID
     */
    private String getTyougouryoukikakuItemId(Integer zairyokubun) {
        String tyogouryoukikakuItemId = "";
        switch (zairyokubun) {
            // 誘電体ｽﾗﾘｰ_調合量規格
            case 1:
                tyogouryoukikakuItemId = GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOUKIKAKU;
                break;
            //  分散材1_調合量規格
            case 2:
                tyogouryoukikakuItemId = GXHDO102B030Const.ZUNSANZAI1_TYOUGOURYOUKIKAKU;
                break;
            //  分散材2_調合量規格
            case 3:
                tyogouryoukikakuItemId = GXHDO102B030Const.ZUNSANZAI2_TYOUGOURYOUKIKAKU;
                break;
            //  溶剤1_調合量規格
            case 4:
                tyogouryoukikakuItemId = GXHDO102B030Const.YOUZAI1_TYOUGOURYOUKIKAKU;
                break;
            //  溶剤2_調合量規格
            case 5:
                tyogouryoukikakuItemId = GXHDO102B030Const.YOUZAI2_TYOUGOURYOUKIKAKU;
                break;
        }
        return tyogouryoukikakuItemId;
    }

    /**
     * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力_サブ画面仮登録(tmp_sub_sr_slip_youzaihyouryou_tounyuu_sutenyouki)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSubSrSlipYouzaihyouryouTounyuuSutenyouki(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal rev, String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sub_sr_slip_youzaihyouryou_tounyuu_sutenyouki "
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
     * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力_サブ画面(sub_sr_slip_youzaihyouryou_tounyuu_sutenyouki)登録処理
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
     * @param tmpSrSlipYouzaihyouryouTounyuuSutenyouki
     * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力_仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSubSrSlipYouzaihyouryouTounyuuSutenyouki(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal newRev, String kojyo, String lotNo, String edaban, Integer zairyokubun, String systemTime,
            ProcessData processData, SrSlipYouzaihyouryouTounyuuSutenyouki tmpSrSlipYouzaihyouryouTounyuuSutenyouki) throws SQLException {
        String sql = "INSERT INTO sub_sr_slip_youzaihyouryou_tounyuu_sutenyouki ( "
                + "kojyo,lotno,edaban,zairyokubun,tyogouryoukikaku,tyogouzanryou,zairyohinmei,"
                + "buzailotno1,buzaihinmei1,tyougouryou1_1,tyougouryou1_2,tyougouryou1_3,tyougouryou1_4,"
                + "tyougouryou1_5,tyougouryou1_6,buzailotno2,buzaihinmei2,tyougouryou2_1,tyougouryou2_2,"
                + "tyougouryou2_3,tyougouryou2_4,tyougouryou2_5,tyougouryou2_6,torokunichiji,kosinnichiji,"
                + "revision "
                + " ) VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";

        List<Object> params = setUpdateParameterSubSrSlipYouzaihyouryouTounyuuSutenyouki(true, newRev, kojyo, lotNo, edaban, zairyokubun, systemTime, tmpSrSlipYouzaihyouryouTounyuuSutenyouki, processData);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力_ｻﾌﾞ画面(sub_sr_slip_youzaihyouryou_tounyuu_sutenyouki)更新処理
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
     * @param srSlipYouzaihyouryouTounyuuSutenyouki ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)更新前データ
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void updateSubSrSlipYouzaihyouryouTounyuuSutenyouki(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal rev, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            Integer zairyokubun, String systemTime, SrSlipYouzaihyouryouTounyuuSutenyouki srSlipYouzaihyouryouTounyuuSutenyouki, ProcessData processData) throws SQLException {

        String sql = "UPDATE sub_sr_slip_youzaihyouryou_tounyuu_sutenyouki SET "
                + "tyogouryoukikaku = ?,tyogouzanryou = ?,zairyohinmei = ?,"
                + "buzailotno1 = ?,buzaihinmei1 = ?,tyougouryou1_1 = ?,tyougouryou1_2 = ?,tyougouryou1_3 = ?,tyougouryou1_4 = ?,"
                + "tyougouryou1_5 = ?,tyougouryou1_6 = ?,buzailotno2 = ?,buzaihinmei2 = ?,tyougouryou2_1 = ?,tyougouryou2_2 = ?,"
                + "tyougouryou2_3 = ?,tyougouryou2_4 = ?,tyougouryou2_5 = ?,tyougouryou2_6 = ?,kosinnichiji = ?,revision = ?"
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND zairyokubun = ? AND revision = ? ";

        List<Object> params = setUpdateParameterSubSrSlipYouzaihyouryouTounyuuSutenyouki(false, newRev, kojyo, lotNo, edaban, zairyokubun, systemTime, srSlipYouzaihyouryouTounyuuSutenyouki, processData);

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
     * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力_サブ画面から更新値を取得
     *
     * @param gxhdO102c015Model モデルデータ
     * @param zairyokubun 材料区分
     * @return 更新値情報
     */
    private ArrayList<Object> getSubGamenData(GXHDO102C015Model gxhdO102c015Model, Integer zairyokubun) {
        GXHDO102C015Model.SubGamenData c015subgamendata = GXHDO102C015Logic.getC015subgamendata(gxhdO102c015Model, zairyokubun);
        ArrayList<Object> returnList = new ArrayList<>();
        // 調合量規格
        FXHDD01 tyogouryoukikaku = c015subgamendata.getSubDataTyogouryoukikaku();
        // 調合残量
        FXHDD01 tyogouzanryou = c015subgamendata.getSubDataTyogouzanryou();
        // 部材①
        List<FXHDD01> buzaitab1DataList = c015subgamendata.getSubDataBuzaitab1();
        // 部材②
        List<FXHDD01> buzaitab2DataList = c015subgamendata.getSubDataBuzaitab2();
        returnList.add(tyogouryoukikaku);
        returnList.add(tyogouzanryou);
        returnList.add(buzaitab1DataList);
        returnList.add(buzaitab2DataList);
        return returnList;
    }

    /**
     * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力_サブ画面登録(tmp_sub_sr_slip_youzaihyouryou_tounyuu_sutenyouki)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param zairyokubun 材料区分
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param srSlipYouzaihyouryouTounyuuSutenyouki
     * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)_仮登録更新前データ
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSubSrSlipYouzaihyouryouTounyuuSutenyouki(boolean isInsert, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Integer zairyokubun, String systemTime,
            SrSlipYouzaihyouryouTounyuuSutenyouki srSlipYouzaihyouryouTounyuuSutenyouki, ProcessData processData) {
        List<FXHDD01> pItemList = processData.getItemList();
        List<Object> params = new ArrayList<>();

        // 子画面情報を取得
        GXHDO102C015 beanGXHDO102C015 = (GXHDO102C015) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C015);
        GXHDO102C015Model gxhdO102c015Model = beanGXHDO102C015.getGxhdO102c015Model();
        // ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力_サブ画面から更新値を取得
        ArrayList<Object> subGamenDataList = getSubGamenData(gxhdO102c015Model, zairyokubun);
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

        // XXX_調合量規格からサブ画面の調合規格がメインの調合量規格と同じように更新する
        String tyougouryoukikakuValue = getItemKikakuchi(pItemList, getTyougouryoukikakuItemId(zairyokubun), srSlipYouzaihyouryouTounyuuSutenyouki);

        params.add(DBUtil.stringToStringObject(tyougouryoukikakuValue)); // 調合量規格
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
     * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力_サブ画面仮登録(tmp_sub_sr_slip_youzaihyouryou_tounyuu_sutenyouki)登録処理(削除時)
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
    private void insertDeleteDataTmpSubSrSlipYouzaihyouryouTounyuuSutenyouki(QueryRunner queryRunnerQcdb,
            Connection conQcdb, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, String systemTime) throws SQLException {
        String sql = "INSERT INTO tmp_sub_sr_slip_youzaihyouryou_tounyuu_sutenyouki( "
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
                + " FROM sub_sr_slip_youzaihyouryou_tounyuu_sutenyouki "
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
     * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力_サブ画面仮登録(sub_sr_slip_youzaihyouryou_tounyuu_sutenyouki)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSubSrSlipYouzaihyouryouTounyuuSutenyouki(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal rev, String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sub_sr_slip_youzaihyouryou_tounyuu_sutenyouki "
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
        // 誘電体ｽﾗﾘｰ_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B030Const.YUUDENTAISLURRY_BUZAIZAIKOLOTNO1, GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOU1, errorItemList);
        // 誘電体ｽﾗﾘｰ_部材在庫No2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B030Const.YUUDENTAISLURRY_BUZAIZAIKOLOTNO2, GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOU2, errorItemList);
        // 分散材1_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B030Const.ZUNSANZAI1_BUZAIZAIKOLOTNO1, GXHDO102B030Const.ZUNSANZAI1_TYOUGOURYOU1, errorItemList);
        // 分散材1_部材在庫No2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B030Const.ZUNSANZAI1_BUZAIZAIKOLOTNO2, GXHDO102B030Const.ZUNSANZAI1_TYOUGOURYOU2, errorItemList);
        // 分散材2_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B030Const.ZUNSANZAI2_BUZAIZAIKOLOTNO1, GXHDO102B030Const.ZUNSANZAI2_TYOUGOURYOU1, errorItemList);
        // 分散材2_部材在庫No2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B030Const.ZUNSANZAI2_BUZAIZAIKOLOTNO2, GXHDO102B030Const.ZUNSANZAI2_TYOUGOURYOU2, errorItemList);
        // 溶剤1_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B030Const.YOUZAI1_BUZAIZAIKOLOTNO1, GXHDO102B030Const.YOUZAI1_TYOUGOURYOU1, errorItemList);
        // 溶剤1_部材在庫No2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B030Const.YOUZAI1_BUZAIZAIKOLOTNO2, GXHDO102B030Const.YOUZAI1_TYOUGOURYOU2, errorItemList);
        // 溶剤2_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B030Const.YOUZAI2_BUZAIZAIKOLOTNO1, GXHDO102B030Const.YOUZAI2_TYOUGOURYOU1, errorItemList);
        // 溶剤2_部材在庫No2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B030Const.YOUZAI2_BUZAIZAIKOLOTNO2, GXHDO102B030Const.YOUZAI2_TYOUGOURYOU2, errorItemList);

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
        FXHDD01 itemFxhdd01Wiplotno = getItemRow(processData.getItemList(), GXHDO102B030Const.WIPLOTNO);
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
        allItemIdMap.put(GXHDO102B030Const.WIPLOTNO, "WIPﾛｯﾄNo");
        allItemIdMap.put(GXHDO102B030Const.SLIPHINMEI, "ｽﾘｯﾌﾟ品名");
        allItemIdMap.put(GXHDO102B030Const.SLIPLOTNO, "ｽﾘｯﾌﾟLotNo");
        allItemIdMap.put(GXHDO102B030Const.LOTKUBUN, "ﾛｯﾄ区分");
        allItemIdMap.put(GXHDO102B030Const.GENRYOUKIGOU, "原料記号");
        allItemIdMap.put(GXHDO102B030Const.GOKI, "秤量号機");
        allItemIdMap.put(GXHDO102B030Const.YUUDENTAISLURRYJYUURYOU, "誘電体ｽﾗﾘｰ重量");
        allItemIdMap.put(GXHDO102B030Const.YUUDENTAISLURRY_ZAIRYOUHINMEI, "誘電体ｽﾗﾘｰ_材料品名");
        allItemIdMap.put(GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOUKIKAKU, "誘電体ｽﾗﾘｰ_調合量規格");
        allItemIdMap.put(GXHDO102B030Const.YUUDENTAISLURRY_BUZAIZAIKOLOTNO1, "誘電体ｽﾗﾘｰ_部材在庫No1");
        allItemIdMap.put(GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOU1, "誘電体ｽﾗﾘｰ_調合量1");
        allItemIdMap.put(GXHDO102B030Const.YUUDENTAISLURRY_BUZAIZAIKOLOTNO2, "誘電体ｽﾗﾘｰ_部材在庫No2");
        allItemIdMap.put(GXHDO102B030Const.YUUDENTAISLURRY_TYOUGOURYOU2, "誘電体ｽﾗﾘｰ_調合量2");
        allItemIdMap.put(GXHDO102B030Const.TANTOUSYA, "担当者");
        allItemIdMap.put(GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU1, "誘電体ｽﾗﾘｰ投入①");
        allItemIdMap.put(GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU2, "誘電体ｽﾗﾘｰ投入②");
        allItemIdMap.put(GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU3, "誘電体ｽﾗﾘｰ投入③");
        allItemIdMap.put(GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU4, "誘電体ｽﾗﾘｰ投入④");
        allItemIdMap.put(GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU5, "誘電体ｽﾗﾘｰ投入⑤");
        allItemIdMap.put(GXHDO102B030Const.YUUDENTAISLURRYTOUNYUU6, "誘電体ｽﾗﾘｰ投入⑥");
        allItemIdMap.put(GXHDO102B030Const.YUUDENTAISLURRYTOUNYUUTANTOUSYA, "誘電体ｽﾗﾘｰ投入担当者");
        allItemIdMap.put(GXHDO102B030Const.YOUZAITYOUSEIRYOU, "溶剤調整量");
        allItemIdMap.put(GXHDO102B030Const.TOLUENETYOUSEIRYOU, "ﾄﾙｴﾝ調整量");
        allItemIdMap.put(GXHDO102B030Const.SOLMIXTYOUSEIRYOU, "ｿﾙﾐｯｸｽ調整量");
        allItemIdMap.put(GXHDO102B030Const.YOUZAIKEIRYOU_DAY, "溶剤秤量日");
        allItemIdMap.put(GXHDO102B030Const.YOUZAIKEIRYOU_TIME, "溶剤秤量時間");
        allItemIdMap.put(GXHDO102B030Const.ZUNSANZAI1_ZAIRYOUHINMEI, "分散材①_材料品名");
        allItemIdMap.put(GXHDO102B030Const.ZUNSANZAI1_TYOUGOURYOUKIKAKU, "分散材①_調合量規格");
        allItemIdMap.put(GXHDO102B030Const.ZUNSANZAI1_BUZAIZAIKOLOTNO1, "分散材①_部材在庫No1");
        allItemIdMap.put(GXHDO102B030Const.ZUNSANZAI1_TYOUGOURYOU1, "分散材①_調合量1");
        allItemIdMap.put(GXHDO102B030Const.ZUNSANZAI1_BUZAIZAIKOLOTNO2, "分散材①_部材在庫No2");
        allItemIdMap.put(GXHDO102B030Const.ZUNSANZAI1_TYOUGOURYOU2, "分散材①_調合量2");
        allItemIdMap.put(GXHDO102B030Const.ZUNSANZAI2_ZAIRYOUHINMEI, "分散材②_材料品名");
        allItemIdMap.put(GXHDO102B030Const.ZUNSANZAI2_TYOUGOURYOUKIKAKU, "分散材②_調合量規格");
        allItemIdMap.put(GXHDO102B030Const.ZUNSANZAI2_BUZAIZAIKOLOTNO1, "分散材②_部材在庫No1");
        allItemIdMap.put(GXHDO102B030Const.ZUNSANZAI2_TYOUGOURYOU1, "分散材②_調合量1");
        allItemIdMap.put(GXHDO102B030Const.ZUNSANZAI2_BUZAIZAIKOLOTNO2, "分散材②_部材在庫No2");
        allItemIdMap.put(GXHDO102B030Const.ZUNSANZAI2_TYOUGOURYOU2, "分散材②_調合量2");
        allItemIdMap.put(GXHDO102B030Const.YOUZAI1_ZAIRYOUHINMEI, "溶剤①_材料品名");
        allItemIdMap.put(GXHDO102B030Const.YOUZAI1_TYOUGOURYOUKIKAKU, "溶剤①_調合量規格");
        allItemIdMap.put(GXHDO102B030Const.YOUZAI1_BUZAIZAIKOLOTNO1, "溶剤①_部材在庫No1");
        allItemIdMap.put(GXHDO102B030Const.YOUZAI1_TYOUGOURYOU1, "溶剤①_調合量1");
        allItemIdMap.put(GXHDO102B030Const.YOUZAI1_BUZAIZAIKOLOTNO2, "溶剤①_部材在庫No2");
        allItemIdMap.put(GXHDO102B030Const.YOUZAI1_TYOUGOURYOU2, "溶剤①_調合量2");
        allItemIdMap.put(GXHDO102B030Const.YOUZAI2_ZAIRYOUHINMEI, "溶剤②_材料品名");
        allItemIdMap.put(GXHDO102B030Const.YOUZAI2_TYOUGOURYOUKIKAKU, "溶剤②_調合量規格");
        allItemIdMap.put(GXHDO102B030Const.YOUZAI2_BUZAIZAIKOLOTNO1, "溶剤②_部材在庫No1");
        allItemIdMap.put(GXHDO102B030Const.YOUZAI2_TYOUGOURYOU1, "溶剤②_調合量1");
        allItemIdMap.put(GXHDO102B030Const.YOUZAI2_BUZAIZAIKOLOTNO2, "溶剤②_部材在庫No2");
        allItemIdMap.put(GXHDO102B030Const.YOUZAI2_TYOUGOURYOU2, "溶剤②_調合量2");
        allItemIdMap.put(GXHDO102B030Const.KAKUHANKI, "撹拌機");
        allItemIdMap.put(GXHDO102B030Const.KAKUHANJIKAN, "撹拌時間");
        allItemIdMap.put(GXHDO102B030Const.KAKUHANKAISI_DAY, "撹拌開始日");
        allItemIdMap.put(GXHDO102B030Const.KAKUHANKAISI_TIME, "撹拌開始時間");
        allItemIdMap.put(GXHDO102B030Const.KAKUHANSYUURYOU_DAY, "撹拌終了日");
        allItemIdMap.put(GXHDO102B030Const.KAKUHANSYUURYOU_TIME, "撹拌終了時間");
        allItemIdMap.put(GXHDO102B030Const.BINDERKONGOUSETUB, "ﾊﾞｲﾝﾀﾞｰ混合設備");
        allItemIdMap.put(GXHDO102B030Const.SETUBISIZE, "設備ｻｲｽﾞ");
        allItemIdMap.put(GXHDO102B030Const.BINDERKONGOUGOKI, "ﾊﾞｲﾝﾀﾞｰ混合号機");
        allItemIdMap.put(GXHDO102B030Const.KONGOUTANKSYURUI, "混合ﾀﾝｸ種類");
        allItemIdMap.put(GXHDO102B030Const.KONGOUTANKNO, "混合ﾀﾝｸNo");
        allItemIdMap.put(GXHDO102B030Const.TANKNAISENJYOUKAKUNIN, "ﾀﾝｸ内洗浄確認");
        allItemIdMap.put(GXHDO102B030Const.TANKNAIUTIBUKUROKAKUNIN, "ﾀﾝｸ内内袋確認");
        allItemIdMap.put(GXHDO102B030Const.KAKUHANHANESENJYOUKAKUNIN, "撹拌羽根洗浄確認");
        allItemIdMap.put(GXHDO102B030Const.KAKUHANJIKUSENJYOUKAKUNIN, "撹拌軸洗浄確認");
        allItemIdMap.put(GXHDO102B030Const.TENRYUUBANNOTAKASA, "転流板の高さ");
        allItemIdMap.put(GXHDO102B030Const.TANKNIEARTHGRIPSETUZOKU, "ﾀﾝｸにｱｰｽｸﾞﾘｯﾌﾟ接続");
        allItemIdMap.put(GXHDO102B030Const.ZUNSANZAIYOUZAITOUNYUU, "分散材溶剤投入");
        allItemIdMap.put(GXHDO102B030Const.YOUZAITOUNYUU1, "溶剤投入①");
        allItemIdMap.put(GXHDO102B030Const.YOUZAITOUNYUU2, "溶剤投入②");
        allItemIdMap.put(GXHDO102B030Const.YOUZAITOUNYUUTANTOUSYA, "溶剤投入担当者");
        allItemIdMap.put(GXHDO102B030Const.BIKOU1, "備考1");
        allItemIdMap.put(GXHDO102B030Const.BIKOU2, "備考2");

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

    /**
     * (19)[ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)]から、ﾃﾞｰﾀを取得
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

        String sql = "SELECT dassikokeibun, youzaityouseiryou, kokeibunhiritu"
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
     * (20)[ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入]から、ﾃﾞｰﾀを取得
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
}
