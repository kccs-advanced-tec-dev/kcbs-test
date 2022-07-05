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
import jp.co.kccs.xhd.db.model.SikakariJson;
import jp.co.kccs.xhd.db.model.SrYuudentaiSyugenryou;
import jp.co.kccs.xhd.db.model.SubSrYuudentaiSyugenryou;
import jp.co.kccs.xhd.model.GXHDO102C011Model;
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
 * 変更日	2021/11/12<br>
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
 * GXHDO102B022(誘電体ｽﾗﾘｰ作製・主原料秤量)
 *
 * @author KCSS K.Jo
 * @since 2021/11/12
 */
public class GXHDO102B022 implements IFormLogic {

    private static final Logger LOGGER = Logger.getLogger(GXHDO102B022.class.getName());
    private static final String JOTAI_FLG_KARI_TOROKU = "0";
    private static final String JOTAI_FLG_TOROKUZUMI = "1";
    private static final String JOTAI_FLG_SAKUJO = "9";
    private static final String SQL_STATE_RECORD_LOCK_ERR = "55P03";

    /**
     * コンストラクタ
     */
    public GXHDO102B022() {
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
            initGXHDO102B022A(processData);

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
                    GXHDO102B022Const.BTN_EDABAN_COPY_TOP,
                    GXHDO102B022Const.BTN_HRKAISINICHIJI_TOP,
                    GXHDO102B022Const.BTN_HRSYUURYOUNICHIJI_TOP,
                    GXHDO102B022Const.BTN_EDABAN_COPY_BOTTOM,
                    GXHDO102B022Const.BTN_HRKAISINICHIJI_BOTTOM,
                    GXHDO102B022Const.BTN_HRSYUURYOUNICHIJI_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO102B022Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO102B022Const.BTN_INSERT_TOP,
                    GXHDO102B022Const.BTN_DELETE_TOP,
                    GXHDO102B022Const.BTN_UPDATE_TOP,
                    GXHDO102B022Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO102B022Const.BTN_INSERT_BOTTOM,
                    GXHDO102B022Const.BTN_DELETE_BOTTOM,
                    GXHDO102B022Const.BTN_UPDATE_BOTTOM
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
            case GXHDO102B022Const.BTN_EDABAN_COPY_TOP:
            case GXHDO102B022Const.BTN_EDABAN_COPY_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 仮登録
            case GXHDO102B022Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO102B022Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO102B022Const.BTN_INSERT_TOP:
            case GXHDO102B022Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 修正
            case GXHDO102B022Const.BTN_UPDATE_TOP:
            case GXHDO102B022Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO102B022Const.BTN_DELETE_TOP:
            case GXHDO102B022Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 主原料秤量開始日時
            case GXHDO102B022Const.BTN_HRKAISINICHIJI_TOP:
            case GXHDO102B022Const.BTN_HRKAISINICHIJI_BOTTOM:
                method = "setHyouryoukaisinichijiDateTime";
                break;
            // 主原料秤量終了日時
            case GXHDO102B022Const.BTN_HRSYUURYOUNICHIJI_TOP:
            case GXHDO102B022Const.BTN_HRSYUURYOUNICHIJI_BOTTOM:
                method = "setHyouryousyuuryounichijiDateTime";
                break;
            // 調合量合計計算
            case GXHDO102B022Const.BTN_TGRGOUKEI_TOP:
            case GXHDO102B022Const.BTN_TGRGOUKEI_BOTTOM:
                method = "setTyougouryougoukei";
                break;
            // 主原料1_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B022Const.BTN_OPENC011SUBGAMEN1:
                method = "openC011SubGamen1";
                break;
            // 主原料2_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B022Const.BTN_OPENC011SUBGAMEN2:
                method = "openC011SubGamen2";
                break;
            // 主原料3_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B022Const.BTN_OPENC011SUBGAMEN3:
                method = "openC011SubGamen3";
                break;
            // 主原料4_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B022Const.BTN_OPENC011SUBGAMEN4:
                method = "openC011SubGamen4";
                break;
            // 主原料5_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B022Const.BTN_OPENC011SUBGAMEN5:
                method = "openC011SubGamen5";
                break;
            // 主原料6_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B022Const.BTN_OPENC011SUBGAMEN6:
                method = "openC011SubGamen6";
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
            int paramJissekino = 1;
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

            // 誘電体ｽﾗﾘｰ作製・主原料秤量の入力項目の登録データ(仮登録時は仮登録データ)を取得
            List<SrYuudentaiSyugenryou> srBinderYouzaiDataList = getSrYuudentaiSyugenryouData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo9, oyalotEdaban);
            if (srBinderYouzaiDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }
            // 誘電体ｽﾗﾘｰ作製・主原料秤量入力_ｻﾌﾞ画面データ取得
            List<SubSrYuudentaiSyugenryou> subSrYuudentaiSyugenryouDataList = getSubSrYuudentaiSyugenryouData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo9, oyalotEdaban);
            if (subSrYuudentaiSyugenryouDataList.isEmpty() || subSrYuudentaiSyugenryouDataList.size() != 6) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }
            // メイン画面データ設定
            setInputItemDataMainForm(processData, srBinderYouzaiDataList.get(0));
            // 誘電体ｽﾗﾘｰ作製・主原料秤量入力_ｻﾌﾞ画面データ設定
            setInputItemDataSubFormC011(processData, subSrYuudentaiSyugenryouDataList);

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
                    // 規格値ﾁｪｯｸに調合量X_1と調合量X_7の合計値を利用するため、該当項目をCloneする
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
        List<String> tyogouryoukikaku = Arrays.asList(GXHDO102B022Const.SGR1_TYOUGOURYOUKIKAKU, GXHDO102B022Const.SGR2_TYOUGOURYOUKIKAKU,
                GXHDO102B022Const.SGR3_TYOUGOURYOUKIKAKU, GXHDO102B022Const.SGR4_TYOUGOURYOUKIKAKU, GXHDO102B022Const.SGR5_TYOUGOURYOUKIKAKU,
                GXHDO102B022Const.SGR6_TYOUGOURYOUKIKAKU);
        List<String> sgr1_tyougouryouList = Arrays.asList(GXHDO102B022Const.SGR1_TYOUGOURYOU1, GXHDO102B022Const.SGR1_TYOUGOURYOU2,
                GXHDO102B022Const.SGR1_TYOUGOURYOU3, GXHDO102B022Const.SGR1_TYOUGOURYOU4, GXHDO102B022Const.SGR1_TYOUGOURYOU5,
                GXHDO102B022Const.SGR1_TYOUGOURYOU6, GXHDO102B022Const.SGR1_TYOUGOURYOU7); // 主原料1_調合量
        List<String> sgr2_tyougouryouList = Arrays.asList(GXHDO102B022Const.SGR2_TYOUGOURYOU1, GXHDO102B022Const.SGR2_TYOUGOURYOU2,
                GXHDO102B022Const.SGR2_TYOUGOURYOU3); // 主原料2_調合量
        List<String> sgr3_tyougouryouList = Arrays.asList(GXHDO102B022Const.SGR3_TYOUGOURYOU1, GXHDO102B022Const.SGR3_TYOUGOURYOU2,
                GXHDO102B022Const.SGR3_TYOUGOURYOU3); // 主原料3_調合量
        List<String> sgr4_tyougouryouList = Arrays.asList(GXHDO102B022Const.SGR4_TYOUGOURYOU1, GXHDO102B022Const.SGR4_TYOUGOURYOU2); // 主原料4_調合量
        List<String> sgr5_tyougouryouList = Arrays.asList(GXHDO102B022Const.SGR5_TYOUGOURYOU1, GXHDO102B022Const.SGR5_TYOUGOURYOU2); // 主原料5_調合量
        List<String> sgr6_tyougouryouList = Arrays.asList(GXHDO102B022Const.SGR6_TYOUGOURYOU1); // 主原料6_調合量

        // 規格値の入力値チェック必要の項目リスト
        List<FXHDD01> itemList = new ArrayList<>();
        setKikakuValueAndLabel1(processData, itemList, sgr1_tyougouryouList, tyogouryoukikaku.get(0), "主原料①_調合量"); // 主原料①_調合量の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, sgr2_tyougouryouList, tyogouryoukikaku.get(1), "主原料②_調合量"); // 主原料②_調合量の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, sgr3_tyougouryouList, tyogouryoukikaku.get(2), "主原料③_調合量"); // 主原料③_調合量の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, sgr4_tyougouryouList, tyogouryoukikaku.get(3), "主原料④_調合量"); // 主原料④_調合量の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, sgr5_tyougouryouList, tyogouryoukikaku.get(4), "主原料⑤_調合量"); // 主原料⑤_調合量の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, sgr6_tyougouryouList, tyogouryoukikaku.get(5), "主原料⑥_調合量"); // 主原料⑥_調合量の規格値と表示ﾗﾍﾞﾙ1を設置

        tyougouryouList.addAll(sgr1_tyougouryouList);
        tyougouryouList.addAll(sgr2_tyougouryouList);
        tyougouryouList.addAll(sgr3_tyougouryouList);
        tyougouryouList.addAll(sgr4_tyougouryouList);
        tyougouryouList.addAll(sgr5_tyougouryouList);
        tyougouryouList.addAll(sgr6_tyougouryouList);

        processData.getItemList().stream().filter(
                (fxhdd01) -> !(tyougouryouList.contains(fxhdd01.getItemId()) || tyogouryoukikaku.contains(fxhdd01.getItemId()))).filter(
                        (fxhdd01) -> !(StringUtil.isEmpty(fxhdd01.getStandardPattern()) || "【-】".equals(fxhdd01.getKikakuChi()))).filter(
                        (fxhdd01) -> !(!ValidateUtil.isInputColumn(fxhdd01) || StringUtil.isEmpty(fxhdd01.getValue()))).forEachOrdered(
                        (fxhdd01) -> {
                            // 規格チェックの対象項目である、かつ入力項目かつ入力値がある項目はリストに追加
                            itemList.add(fxhdd01);
                        });
        ErrorMessageInfo errorMessageInfo = ValidateUtil.checkInputKikakuchi(itemList, kikakuchiInputErrorInfoList);
        // 主原料1の調合量エラー項目の背景色を設定
        setItemBackColor(processData, sgr1_tyougouryouList, errorMessageInfo, kikakuchiInputErrorInfoList);
        // 主原料2の調合量エラー項目の背景色を設定
        setItemBackColor(processData, sgr2_tyougouryouList, errorMessageInfo, kikakuchiInputErrorInfoList);
        // 主原料3の調合量エラー項目の背景色を設定
        setItemBackColor(processData, sgr3_tyougouryouList, errorMessageInfo, kikakuchiInputErrorInfoList);
        // 主原料4の調合量エラー項目の背景色を設定
        setItemBackColor(processData, sgr4_tyougouryouList, errorMessageInfo, kikakuchiInputErrorInfoList);
        // 主原料5の調合量エラー項目の背景色を設定
        setItemBackColor(processData, sgr5_tyougouryouList, errorMessageInfo, kikakuchiInputErrorInfoList);
        // 主原料6の調合量エラー項目の背景色を設定
        setItemBackColor(processData, sgr6_tyougouryouList, errorMessageInfo, kikakuchiInputErrorInfoList);
        return errorMessageInfo;
    }

    /**
     * 調合量エラー項目の背景色を設定
     *
     * @param processData 処理データ
     * @param tyougouryouList 調合量項目リスト
     * @param errorMessageInfo エラーメッセージ情報
     * @param kikakuchiInputErrorInfoList 規格値入力エラー情報リスト
     */
    private void setItemBackColor(ProcessData processData, List<String> tyougouryouList, ErrorMessageInfo errorMessageInfo, List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList) {
        boolean errorColorFlg = false;
        if (errorMessageInfo == null) {
            // エラー項目の背景色を設定
            List<String> errorTyougouryouList = new ArrayList<>();
            kikakuchiInputErrorInfoList.stream().filter((errorInfo)
                    -> (tyougouryouList.contains(errorInfo.getItemId()))).forEachOrdered((errorInfo) -> {
                errorTyougouryouList.add(errorInfo.getItemId());
            });
            if (errorTyougouryouList.size() > 0) {
                errorColorFlg = true;
            }
        } else {
            String itemId = errorMessageInfo.getErrorItemInfoList().get(0).getItemId();
            int index = tyougouryouList.indexOf(itemId);
            if (index != -1) {
                errorColorFlg = true;
            }
        }
        if (errorColorFlg) {
            tyougouryouList.stream().map((itemId) -> getItemRow(processData.getItemList(), itemId)).filter((itemFxhdd01)
                    -> (itemFxhdd01 != null)).forEachOrdered((itemFxhdd01) -> {
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
            int paramJissekino = 1;
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

                // 誘電体ｽﾗﾘｰ作製・主原料秤量_仮登録処理
                insertTmpSrYuudentaiSyugenryou(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, strSystime, processData);
                // 誘電体ｽﾗﾘｰ作製・主原料秤量入力ｻﾌﾞ画面の仮登録処理
                for (int i = 1; i <= 6; i++) {
                    insertTmpSubSrYuudentaiSyugenryou(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, i, strSystime);
                }
            } else {

                // 誘電体ｽﾗﾘｰ作製・主原料秤量_仮登録更新処理
                updateTmpSrYuudentaiSyugenryou(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);
                // 誘電体ｽﾗﾘｰ作製・主原料秤量入力ｻﾌﾞ画面の仮登録更新処理
                for (int i = 1; i <= 6; i++) {
                    updateTmpSubSrYuudentaiSyugenryou(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, i, strSystime);
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
        // 秤量開始日時、秤量終了日時前後チェック
        FXHDD01 itemHyouryoukaisiDay = getItemRow(processData.getItemList(), GXHDO102B022Const.SGRHYOURYOUKAISI_DAY); // 秤量開始日
        FXHDD01 itemHyouryoukaisiTime = getItemRow(processData.getItemList(), GXHDO102B022Const.SGRHYOURYOUKAISI_TIME); // 秤量開始時間
        FXHDD01 itemHyouryousyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B022Const.SGRHYOURYOUSYUURYOU_DAY); //秤量終了日
        FXHDD01 itemHyouryousyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B022Const.SGRHYOURYOUSYUURYOU_TIME); //秤量終了時間
        if (itemHyouryoukaisiDay != null && itemHyouryoukaisiTime != null && itemHyouryousyuuryouDay != null && itemHyouryousyuuryouTime != null) {
            Date hyouryoukaisiDate = DateUtil.convertStringToDate(itemHyouryoukaisiDay.getValue(), itemHyouryoukaisiTime.getValue());
            Date hyouryousyuuryouDate = DateUtil.convertStringToDate(itemHyouryousyuuryouDay.getValue(), itemHyouryousyuuryouTime.getValue());
            //R001チェック呼出し
            String msgCheckR001 = validateUtil.checkR001(itemHyouryoukaisiDay.getLabel1() + "時", hyouryoukaisiDate, itemHyouryousyuuryouDay.getLabel1() + "時", hyouryousyuuryouDate);
            if (!StringUtil.isEmpty(msgCheckR001)) {
                //エラー発生時
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemHyouryoukaisiDay, itemHyouryoukaisiTime, itemHyouryousyuuryouDay, itemHyouryousyuuryouTime);
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
            int paramJissekino = 1;
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
            SrYuudentaiSyugenryou tmpSrYuudentaiSyugenryou = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrYuudentaiSyugenryou> srYuudentaiSyugenryouList = getSrYuudentaiSyugenryouData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo9, edaban);
                if (!srYuudentaiSyugenryouList.isEmpty()) {
                    tmpSrYuudentaiSyugenryou = srYuudentaiSyugenryouList.get(0);
                }

                deleteTmpSrYuudentaiSyugenryou(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);
                deleteTmpSubSrYuudentaiSyugenryou(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);
            }

            // 誘電体ｽﾗﾘｰ作製・主原料秤量_登録処理
            insertSrYuudentaiSyugenryou(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, strSystime, processData, tmpSrYuudentaiSyugenryou);
            // 誘電体ｽﾗﾘｰ作製・主原料秤量入力ｻﾌﾞ画面の仮登録更新処理
            for (int i = 1; i <= 6; i++) {
                insertSubSrYuudentaiSyugenryou(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, i, strSystime);
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
        processData.setUserAuthParam(GXHDO102B022Const.USER_AUTH_UPDATE_PARAM);

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
            int paramJissekino = 1;
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

            // 誘電体ｽﾗﾘｰ作製・主原料秤量_更新処理
            updateSrYuudentaiSyugenryou(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);
            // 誘電体ｽﾗﾘｰ作製・主原料秤量入力ｻﾌﾞ画面の更新処理
            for (int i = 1; i <= 6; i++) {
                updateSubSrYuudentaiSyugenryou(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, i, strSystime);
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
        processData.setUserAuthParam(GXHDO102B022Const.USER_AUTH_DELETE_PARAM);

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
            int paramJissekino = 1;
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

            // 誘電体ｽﾗﾘｰ作製・主原料秤量_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo9, edaban, paramJissekino);
            insertDeleteDataTmpSrYuudentaiSyugenryou(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo9, edaban, strSystime);

            // 誘電体ｽﾗﾘｰ作製・主原料秤量入力_ｻﾌﾞ画面仮登録登録処理
            insertDeleteDataTmpSubSrYuudentaiSyugenryou(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo9, edaban, strSystime);

            // 誘電体ｽﾗﾘｰ作製・主原料秤量_削除処理
            deleteSrYuudentaiSyugenryou(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);

            // 誘電体ｽﾗﾘｰ作製・主原料秤量入力_ｻﾌﾞ画面削除処理
            deleteSubSrYuudentaiSyugenryou(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);

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
     * 主原料秤量開始日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setHyouryoukaisinichijiDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B022Const.SGRHYOURYOUKAISI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B022Const.SGRHYOURYOUKAISI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 主原料秤量終了日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setHyouryousyuuryounichijiDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B022Const.SGRHYOURYOUSYUURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B022Const.SGRHYOURYOUSYUURYOU_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 調合量合計計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     * @throws SQLException 例外エラー
     */
    public ProcessData setTyougouryougoukei(ProcessData processData) throws SQLException {
        FXHDD01 itemTyougouryougoukei = getItemRow(processData.getItemList(), GXHDO102B022Const.TYOUGOURYOUGOUKEI); // 調合量合計
        if (itemTyougouryougoukei != null) {
            // 調合量X_Yリストの合計値を取得
            String sumTyogouzanryouStrVal = getSumTyogouzanryouVal(processData, 0);
            //計算結果を調合量合計にセット
            itemTyougouryougoukei.setValue(sumTyogouzanryouStrVal);
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
                        GXHDO102B022Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B022Const.BTN_HRKAISINICHIJI_TOP,
                        GXHDO102B022Const.BTN_HRSYUURYOUNICHIJI_TOP,
                        GXHDO102B022Const.BTN_TGRGOUKEI_TOP,
                        GXHDO102B022Const.BTN_UPDATE_TOP,
                        GXHDO102B022Const.BTN_DELETE_TOP,
                        GXHDO102B022Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B022Const.BTN_HRKAISINICHIJI_BOTTOM,
                        GXHDO102B022Const.BTN_HRSYUURYOUNICHIJI_BOTTOM,
                        GXHDO102B022Const.BTN_TGRGOUKEI_BOTTOM,
                        GXHDO102B022Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B022Const.BTN_DELETE_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B022Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B022Const.BTN_INSERT_TOP,
                        GXHDO102B022Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B022Const.BTN_INSERT_BOTTOM));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO102B022Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B022Const.BTN_HRKAISINICHIJI_TOP,
                        GXHDO102B022Const.BTN_HRSYUURYOUNICHIJI_TOP,
                        GXHDO102B022Const.BTN_TGRGOUKEI_TOP,
                        GXHDO102B022Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B022Const.BTN_INSERT_TOP,
                        GXHDO102B022Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B022Const.BTN_HRKAISINICHIJI_BOTTOM,
                        GXHDO102B022Const.BTN_HRSYUURYOUNICHIJI_BOTTOM,
                        GXHDO102B022Const.BTN_TGRGOUKEI_BOTTOM,
                        GXHDO102B022Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B022Const.BTN_INSERT_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B022Const.BTN_UPDATE_TOP,
                        GXHDO102B022Const.BTN_DELETE_TOP,
                        GXHDO102B022Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B022Const.BTN_DELETE_BOTTOM
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
        // 項目の表示制御
        setItemRendered(processData, queryRunnerDoc, queryRunnerQcdb, shikakariData, lotNo);
        // 品名毎の項目の表示制御
        setHinmeiItemRendered(processData);
        // 画面に取得した情報をセットする。(入力項目以外)
        setViewItemData(processData, shikakariData, lotNo);
        // 画面のラベル項目の値の背景色を取得できない場合、デフォルト値を設置
        GXHDO102C011Logic.setItemStyle(processData.getItemList());
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
        this.setItemData(processData, GXHDO102B022Const.WIPLOTNO, lotNo);
        // 誘電体ｽﾗﾘｰ品名
        this.setItemData(processData, GXHDO102B022Const.YUUDENTAISLURRYHINMEI, StringUtil.nullToBlank(getMapData(shikakariData, "hinmei")));
        // 誘電体ｽﾗﾘｰLotNo
        this.setItemData(processData, GXHDO102B022Const.YUUDENTAISLURRYLOTNO, StringUtil.nullToBlank(getMapData(shikakariData, "lotno")));
        // ﾛｯﾄ区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode"));
        // ﾛｯﾄ区分名称
        String lotkubun = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubun"));

        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO102B022Const.LOTKUBUN, "");
        } else {
            if (!StringUtil.isEmpty(lotkubun)) {
                lotkubuncode = lotkubuncode + ":" + lotkubun;
            }
            this.setItemData(processData, GXHDO102B022Const.LOTKUBUN, lotkubuncode);
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

        List<SrYuudentaiSyugenryou> srYuudentaiSyugenryouList = new ArrayList<>();
        List<SubSrYuudentaiSyugenryou> subSrYuudentaiSyugenryouList = new ArrayList<>();
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

                // 誘電体ｽﾗﾘｰ作製・主原料秤量入力_ｻﾌﾞ画面データ設定
                setInputItemDataSubFormC011(processData, null);
                return true;
            }

            // 誘電体ｽﾗﾘｰ作製・主原料秤量データ取得
            srYuudentaiSyugenryouList = getSrYuudentaiSyugenryouData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo9, edaban);
            if (srYuudentaiSyugenryouList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // 誘電体ｽﾗﾘｰ作製・主原料秤量入力_サブ画面データ取得
            subSrYuudentaiSyugenryouList = getSubSrYuudentaiSyugenryouData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo9, edaban);
            if (subSrYuudentaiSyugenryouList.isEmpty() || subSrYuudentaiSyugenryouList.size() != 6) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }
            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srYuudentaiSyugenryouList.isEmpty() || (subSrYuudentaiSyugenryouList.isEmpty() || subSrYuudentaiSyugenryouList.size() != 6)) {
            return false;
        }
        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srYuudentaiSyugenryouList.get(0));
        // 誘電体ｽﾗﾘｰ作製・主原料秤量入力_ｻﾌﾞ画面データ設定
        setInputItemDataSubFormC011(processData, subSrYuudentaiSyugenryouList);
        return true;

    }

    /**
     * データ設定処理
     *
     * @param processData 処理制御データ
     * @param srYuudentaiSyugenryou 誘電体ｽﾗﾘｰ作製・主原料秤量
     */
    private void setInputItemDataMainForm(ProcessData processData, SrYuudentaiSyugenryou srYuudentaiSyugenryou) {
        // 秤量号機
        this.setItemData(processData, GXHDO102B022Const.GOKI, getSrYuudentaiSyugenryouItemData(GXHDO102B022Const.GOKI, srYuudentaiSyugenryou));

        // 主原料秤量開始日
        this.setItemData(processData, GXHDO102B022Const.SGRHYOURYOUKAISI_DAY, getSrYuudentaiSyugenryouItemData(GXHDO102B022Const.SGRHYOURYOUKAISI_DAY, srYuudentaiSyugenryou));

        // 主原料秤量開始時間
        this.setItemData(processData, GXHDO102B022Const.SGRHYOURYOUKAISI_TIME, getSrYuudentaiSyugenryouItemData(GXHDO102B022Const.SGRHYOURYOUKAISI_TIME, srYuudentaiSyugenryou));

        // 主原料①_部材在庫No
        this.setItemData(processData, GXHDO102B022Const.SGR1_BUZAIZAIKOLOTNO, getSrYuudentaiSyugenryouItemData(GXHDO102B022Const.SGR1_BUZAIZAIKOLOTNO, srYuudentaiSyugenryou));

        // 主原料①_調合量1
        this.setItemData(processData, GXHDO102B022Const.SGR1_TYOUGOURYOU1, getSrYuudentaiSyugenryouItemData(GXHDO102B022Const.SGR1_TYOUGOURYOU1, srYuudentaiSyugenryou));

        // 主原料①_調合量2
        this.setItemData(processData, GXHDO102B022Const.SGR1_TYOUGOURYOU2, getSrYuudentaiSyugenryouItemData(GXHDO102B022Const.SGR1_TYOUGOURYOU2, srYuudentaiSyugenryou));

        // 主原料①_調合量3
        this.setItemData(processData, GXHDO102B022Const.SGR1_TYOUGOURYOU3, getSrYuudentaiSyugenryouItemData(GXHDO102B022Const.SGR1_TYOUGOURYOU3, srYuudentaiSyugenryou));

        // 主原料①_調合量4
        this.setItemData(processData, GXHDO102B022Const.SGR1_TYOUGOURYOU4, getSrYuudentaiSyugenryouItemData(GXHDO102B022Const.SGR1_TYOUGOURYOU4, srYuudentaiSyugenryou));

        // 主原料①_調合量5
        this.setItemData(processData, GXHDO102B022Const.SGR1_TYOUGOURYOU5, getSrYuudentaiSyugenryouItemData(GXHDO102B022Const.SGR1_TYOUGOURYOU5, srYuudentaiSyugenryou));

        // 主原料①_調合量6
        this.setItemData(processData, GXHDO102B022Const.SGR1_TYOUGOURYOU6, getSrYuudentaiSyugenryouItemData(GXHDO102B022Const.SGR1_TYOUGOURYOU6, srYuudentaiSyugenryou));

        // 主原料①_調合量7
        this.setItemData(processData, GXHDO102B022Const.SGR1_TYOUGOURYOU7, getSrYuudentaiSyugenryouItemData(GXHDO102B022Const.SGR1_TYOUGOURYOU7, srYuudentaiSyugenryou));

        // 主原料②_部材在庫No
        this.setItemData(processData, GXHDO102B022Const.SGR2_BUZAIZAIKOLOTNO, getSrYuudentaiSyugenryouItemData(GXHDO102B022Const.SGR2_BUZAIZAIKOLOTNO, srYuudentaiSyugenryou));

        // 主原料②_調合量1
        this.setItemData(processData, GXHDO102B022Const.SGR2_TYOUGOURYOU1, getSrYuudentaiSyugenryouItemData(GXHDO102B022Const.SGR2_TYOUGOURYOU1, srYuudentaiSyugenryou));

        // 主原料②_調合量2
        this.setItemData(processData, GXHDO102B022Const.SGR2_TYOUGOURYOU2, getSrYuudentaiSyugenryouItemData(GXHDO102B022Const.SGR2_TYOUGOURYOU2, srYuudentaiSyugenryou));

        // 主原料②_調合量3
        this.setItemData(processData, GXHDO102B022Const.SGR2_TYOUGOURYOU3, getSrYuudentaiSyugenryouItemData(GXHDO102B022Const.SGR2_TYOUGOURYOU3, srYuudentaiSyugenryou));

        // 主原料③_部材在庫No
        this.setItemData(processData, GXHDO102B022Const.SGR3_BUZAIZAIKOLOTNO, getSrYuudentaiSyugenryouItemData(GXHDO102B022Const.SGR3_BUZAIZAIKOLOTNO, srYuudentaiSyugenryou));

        // 主原料③_調合量1
        this.setItemData(processData, GXHDO102B022Const.SGR3_TYOUGOURYOU1, getSrYuudentaiSyugenryouItemData(GXHDO102B022Const.SGR3_TYOUGOURYOU1, srYuudentaiSyugenryou));

        // 主原料③_調合量2
        this.setItemData(processData, GXHDO102B022Const.SGR3_TYOUGOURYOU2, getSrYuudentaiSyugenryouItemData(GXHDO102B022Const.SGR3_TYOUGOURYOU2, srYuudentaiSyugenryou));

        // 主原料③_調合量3
        this.setItemData(processData, GXHDO102B022Const.SGR3_TYOUGOURYOU3, getSrYuudentaiSyugenryouItemData(GXHDO102B022Const.SGR3_TYOUGOURYOU3, srYuudentaiSyugenryou));

        // 主原料④_材料品名
        this.setItemData(processData, GXHDO102B022Const.SGR4_ZAIRYOUHINMEI, getSrYuudentaiSyugenryouItemData(GXHDO102B022Const.SGR4_ZAIRYOUHINMEI, srYuudentaiSyugenryou));

        // 主原料④_調合量規格
        this.setItemData(processData, GXHDO102B022Const.SGR4_TYOUGOURYOUKIKAKU, getSrYuudentaiSyugenryouItemData(GXHDO102B022Const.SGR4_TYOUGOURYOUKIKAKU, srYuudentaiSyugenryou));

        // 主原料④_部材在庫No
        this.setItemData(processData, GXHDO102B022Const.SGR4_BUZAIZAIKOLOTNO, getSrYuudentaiSyugenryouItemData(GXHDO102B022Const.SGR4_BUZAIZAIKOLOTNO, srYuudentaiSyugenryou));

        // 主原料④_調合量1
        this.setItemData(processData, GXHDO102B022Const.SGR4_TYOUGOURYOU1, getSrYuudentaiSyugenryouItemData(GXHDO102B022Const.SGR4_TYOUGOURYOU1, srYuudentaiSyugenryou));

        // 主原料④_調合量2
        this.setItemData(processData, GXHDO102B022Const.SGR4_TYOUGOURYOU2, getSrYuudentaiSyugenryouItemData(GXHDO102B022Const.SGR4_TYOUGOURYOU2, srYuudentaiSyugenryou));

        // 主原料⑤_部材在庫No
        this.setItemData(processData, GXHDO102B022Const.SGR5_BUZAIZAIKOLOTNO, getSrYuudentaiSyugenryouItemData(GXHDO102B022Const.SGR5_BUZAIZAIKOLOTNO, srYuudentaiSyugenryou));

        // 主原料⑤_調合量1
        this.setItemData(processData, GXHDO102B022Const.SGR5_TYOUGOURYOU1, getSrYuudentaiSyugenryouItemData(GXHDO102B022Const.SGR5_TYOUGOURYOU1, srYuudentaiSyugenryou));

        // 主原料⑤_調合量2
        this.setItemData(processData, GXHDO102B022Const.SGR5_TYOUGOURYOU2, getSrYuudentaiSyugenryouItemData(GXHDO102B022Const.SGR5_TYOUGOURYOU2, srYuudentaiSyugenryou));

        // 主原料⑥_部材在庫No
        this.setItemData(processData, GXHDO102B022Const.SGR6_BUZAIZAIKOLOTNO, getSrYuudentaiSyugenryouItemData(GXHDO102B022Const.SGR6_BUZAIZAIKOLOTNO, srYuudentaiSyugenryou));

        // 主原料⑥_調合量1
        this.setItemData(processData, GXHDO102B022Const.SGR6_TYOUGOURYOU1, getSrYuudentaiSyugenryouItemData(GXHDO102B022Const.SGR6_TYOUGOURYOU1, srYuudentaiSyugenryou));

        // 調合量合計
        this.setItemData(processData, GXHDO102B022Const.TYOUGOURYOUGOUKEI, getSrYuudentaiSyugenryouItemData(GXHDO102B022Const.TYOUGOURYOUGOUKEI, srYuudentaiSyugenryou));

        // 主原料秤量終了日
        this.setItemData(processData, GXHDO102B022Const.SGRHYOURYOUSYUURYOU_DAY, getSrYuudentaiSyugenryouItemData(GXHDO102B022Const.SGRHYOURYOUSYUURYOU_DAY, srYuudentaiSyugenryou));

        // 主原料秤量終了時間
        this.setItemData(processData, GXHDO102B022Const.SGRHYOURYOUSYUURYOU_TIME, getSrYuudentaiSyugenryouItemData(GXHDO102B022Const.SGRHYOURYOUSYUURYOU_TIME, srYuudentaiSyugenryou));

        // 固形分測定担当者
        this.setItemData(processData, GXHDO102B022Const.KOKEIBUNSOKUTEITANTOUSYA, getSrYuudentaiSyugenryouItemData(GXHDO102B022Const.KOKEIBUNSOKUTEITANTOUSYA, srYuudentaiSyugenryou));

        // 備考1
        this.setItemData(processData, GXHDO102B022Const.BIKOU1, getSrYuudentaiSyugenryouItemData(GXHDO102B022Const.BIKOU1, srYuudentaiSyugenryou));

        // 備考2
        this.setItemData(processData, GXHDO102B022Const.BIKOU2, getSrYuudentaiSyugenryouItemData(GXHDO102B022Const.BIKOU2, srYuudentaiSyugenryou));

    }

    /**
     * 誘電体ｽﾗﾘｰ作製・主原料秤量の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return 誘電体ｽﾗﾘｰ作製・主原料秤量データ
     * @throws SQLException 例外エラー
     */
    private List<SrYuudentaiSyugenryou> getSrYuudentaiSyugenryouData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrYuudentaiSyugenryou(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSrYuudentaiSyugenryou(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
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
     * [誘電体ｽﾗﾘｰ作製・主原料秤量]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrYuudentaiSyugenryou> loadSrYuudentaiSyugenryou(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + " kojyo,lotno,edaban,yuudentaislurryhinmei,yuudentaislurrylotno,lotkubun,genryoulotno,genryoukigou,goki,syugenryouhyouryoukaisinichiji,"
                + "syugenryou1_zairyouhinmei,syugenryou1_tyougouryoukikaku,syugenryou1_buzaizaikolotno,syugenryou1_tyougouryou1,syugenryou1_tyougouryou2,"
                + "syugenryou1_tyougouryou3,syugenryou1_tyougouryou4,syugenryou1_tyougouryou5,syugenryou1_tyougouryou6,syugenryou1_tyougouryou7,"
                + "syugenryou2_zairyouhinmei,syugenryou2_tyougouryoukikaku,syugenryou2_buzaizaikolotno,syugenryou2_tyougouryou1,syugenryou2_tyougouryou2,"
                + "syugenryou2_tyougouryou3,syugenryou3_zairyouhinmei,syugenryou3_tyougouryoukikaku,syugenryou3_buzaizaikolotno,syugenryou3_tyougouryou1,"
                + "syugenryou3_tyougouryou2,syugenryou3_tyougouryou3,syugenryou4_zairyouhinmei,syugenryou4_tyougouryoukikaku,syugenryou4_buzaizaikolotno,"
                + "syugenryou4_tyougouryou1,syugenryou4_tyougouryou2,syugenryou5_zairyouhinmei,syugenryou5_tyougouryoukikaku,syugenryou5_buzaizaikolotno,"
                + "syugenryou5_tyougouryou1,syugenryou5_tyougouryou2,syugenryou6_zairyouhinmei,syugenryou6_tyougouryoukikaku,syugenryou6_buzaizaikolotno,"
                + "syugenryou6_tyougouryou1,tyougouryougoukei,syugenryouhyouryousyuuryounichiji,kokeibunsokuteitantousya,bikou1,bikou2,torokunichiji,"
                + "kosinnichiji,revision "
                + " FROM sr_yuudentai_syugenryou "
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
        mapping.put("yuudentaislurryhinmei", "yuudentaislurryhinmei");                          // 誘電体ｽﾗﾘｰ品名
        mapping.put("yuudentaislurrylotno", "yuudentaislurrylotno");                            // 誘電体ｽﾗﾘｰLotNo
        mapping.put("lotkubun", "lotkubun");                                                    // ﾛｯﾄ区分
        mapping.put("genryoulotno", "genryoulotno");                                            // 原料LotNo
        mapping.put("genryoukigou", "genryoukigou");                                            // 原料記号
        mapping.put("goki", "goki");                                                            // 秤量号機
        mapping.put("syugenryouhyouryoukaisinichiji", "syugenryouhyouryoukaisinichiji");        // 主原料秤量開始日時
        mapping.put("syugenryou1_zairyouhinmei", "syugenryou1_zairyouhinmei");                  // 主原料①_材料品名
        mapping.put("syugenryou1_tyougouryoukikaku", "syugenryou1_tyougouryoukikaku");          // 主原料①_調合量規格
        mapping.put("syugenryou1_buzaizaikolotno", "syugenryou1_buzaizaikolotno");              // 主原料①_部材在庫No
        mapping.put("syugenryou1_tyougouryou1", "syugenryou1_tyougouryou1");                    // 主原料①_調合量1
        mapping.put("syugenryou1_tyougouryou2", "syugenryou1_tyougouryou2");                    // 主原料①_調合量2
        mapping.put("syugenryou1_tyougouryou3", "syugenryou1_tyougouryou3");                    // 主原料①_調合量3
        mapping.put("syugenryou1_tyougouryou4", "syugenryou1_tyougouryou4");                    // 主原料①_調合量4
        mapping.put("syugenryou1_tyougouryou5", "syugenryou1_tyougouryou5");                    // 主原料①_調合量5
        mapping.put("syugenryou1_tyougouryou6", "syugenryou1_tyougouryou6");                    // 主原料①_調合量6
        mapping.put("syugenryou1_tyougouryou7", "syugenryou1_tyougouryou7");                    // 主原料①_調合量7
        mapping.put("syugenryou2_zairyouhinmei", "syugenryou2_zairyouhinmei");                  // 主原料②_材料品名
        mapping.put("syugenryou2_tyougouryoukikaku", "syugenryou2_tyougouryoukikaku");          // 主原料②_調合量規格
        mapping.put("syugenryou2_buzaizaikolotno", "syugenryou2_buzaizaikolotno");              // 主原料②_部材在庫No
        mapping.put("syugenryou2_tyougouryou1", "syugenryou2_tyougouryou1");                    // 主原料②_調合量1
        mapping.put("syugenryou2_tyougouryou2", "syugenryou2_tyougouryou2");                    // 主原料②_調合量2
        mapping.put("syugenryou2_tyougouryou3", "syugenryou2_tyougouryou3");                    // 主原料②_調合量3
        mapping.put("syugenryou3_zairyouhinmei", "syugenryou3_zairyouhinmei");                  // 主原料③_材料品名
        mapping.put("syugenryou3_tyougouryoukikaku", "syugenryou3_tyougouryoukikaku");          // 主原料③_調合量規格
        mapping.put("syugenryou3_buzaizaikolotno", "syugenryou3_buzaizaikolotno");              // 主原料③_部材在庫No
        mapping.put("syugenryou3_tyougouryou1", "syugenryou3_tyougouryou1");                    // 主原料③_調合量1
        mapping.put("syugenryou3_tyougouryou2", "syugenryou3_tyougouryou2");                    // 主原料③_調合量2
        mapping.put("syugenryou3_tyougouryou3", "syugenryou3_tyougouryou3");                    // 主原料③_調合量3
        mapping.put("syugenryou4_zairyouhinmei", "syugenryou4_zairyouhinmei");                  // 主原料④_材料品名
        mapping.put("syugenryou4_tyougouryoukikaku", "syugenryou4_tyougouryoukikaku");          // 主原料④_調合量規格
        mapping.put("syugenryou4_buzaizaikolotno", "syugenryou4_buzaizaikolotno");              // 主原料④_部材在庫No
        mapping.put("syugenryou4_tyougouryou1", "syugenryou4_tyougouryou1");                    // 主原料④_調合量1
        mapping.put("syugenryou4_tyougouryou2", "syugenryou4_tyougouryou2");                    // 主原料④_調合量2
        mapping.put("syugenryou5_zairyouhinmei", "syugenryou5_zairyouhinmei");                  // 主原料⑤_材料品名
        mapping.put("syugenryou5_tyougouryoukikaku", "syugenryou5_tyougouryoukikaku");          // 主原料⑤_調合量規格
        mapping.put("syugenryou5_buzaizaikolotno", "syugenryou5_buzaizaikolotno");              // 主原料⑤_部材在庫No
        mapping.put("syugenryou5_tyougouryou1", "syugenryou5_tyougouryou1");                    // 主原料⑤_調合量1
        mapping.put("syugenryou5_tyougouryou2", "syugenryou5_tyougouryou2");                    // 主原料⑤_調合量2
        mapping.put("syugenryou6_zairyouhinmei", "syugenryou6_zairyouhinmei");                  // 主原料⑥_材料品名
        mapping.put("syugenryou6_tyougouryoukikaku", "syugenryou6_tyougouryoukikaku");          // 主原料⑥_調合量規格
        mapping.put("syugenryou6_buzaizaikolotno", "syugenryou6_buzaizaikolotno");              // 主原料⑥_部材在庫No
        mapping.put("syugenryou6_tyougouryou1", "syugenryou6_tyougouryou1");                    // 主原料⑥_調合量1
        mapping.put("tyougouryougoukei", "tyougouryougoukei");                                  // 調合量合計
        mapping.put("syugenryouhyouryousyuuryounichiji", "syugenryouhyouryousyuuryounichiji");  // 主原料秤量終了日時
        mapping.put("kokeibunsokuteitantousya", "kokeibunsokuteitantousya");                    // 固形分測定担当者
        mapping.put("bikou1", "bikou1");                                                        // 備考1
        mapping.put("bikou2", "bikou2");                                                        // 備考2
        mapping.put("torokunichiji", "torokunichiji");                                          // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                                            // 更新日時
        mapping.put("revision", "revision");                                                    // revision

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrYuudentaiSyugenryou>> beanHandler = new BeanListHandler<>(SrYuudentaiSyugenryou.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [誘電体ｽﾗﾘｰ作製・主原料秤量_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrYuudentaiSyugenryou> loadTmpSrYuudentaiSyugenryou(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + " kojyo,lotno,edaban,yuudentaislurryhinmei,yuudentaislurrylotno,lotkubun,genryoulotno,genryoukigou,goki,syugenryouhyouryoukaisinichiji,"
                + "syugenryou1_zairyouhinmei,syugenryou1_tyougouryoukikaku,syugenryou1_buzaizaikolotno,syugenryou1_tyougouryou1,syugenryou1_tyougouryou2,"
                + "syugenryou1_tyougouryou3,syugenryou1_tyougouryou4,syugenryou1_tyougouryou5,syugenryou1_tyougouryou6,syugenryou1_tyougouryou7,"
                + "syugenryou2_zairyouhinmei,syugenryou2_tyougouryoukikaku,syugenryou2_buzaizaikolotno,syugenryou2_tyougouryou1,syugenryou2_tyougouryou2,"
                + "syugenryou2_tyougouryou3,syugenryou3_zairyouhinmei,syugenryou3_tyougouryoukikaku,syugenryou3_buzaizaikolotno,syugenryou3_tyougouryou1,"
                + "syugenryou3_tyougouryou2,syugenryou3_tyougouryou3,syugenryou4_zairyouhinmei,syugenryou4_tyougouryoukikaku,syugenryou4_buzaizaikolotno,"
                + "syugenryou4_tyougouryou1,syugenryou4_tyougouryou2,syugenryou5_zairyouhinmei,syugenryou5_tyougouryoukikaku,syugenryou5_buzaizaikolotno,"
                + "syugenryou5_tyougouryou1,syugenryou5_tyougouryou2,syugenryou6_zairyouhinmei,syugenryou6_tyougouryoukikaku,syugenryou6_buzaizaikolotno,"
                + "syugenryou6_tyougouryou1,tyougouryougoukei,syugenryouhyouryousyuuryounichiji,kokeibunsokuteitantousya,bikou1,bikou2,torokunichiji,"
                + "kosinnichiji,revision,deleteflag "
                + " FROM tmp_sr_yuudentai_syugenryou "
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
        mapping.put("yuudentaislurryhinmei", "yuudentaislurryhinmei");                          // 誘電体ｽﾗﾘｰ品名
        mapping.put("yuudentaislurrylotno", "yuudentaislurrylotno");                            // 誘電体ｽﾗﾘｰLotNo
        mapping.put("lotkubun", "lotkubun");                                                    // ﾛｯﾄ区分
        mapping.put("genryoulotno", "genryoulotno");                                            // 原料LotNo
        mapping.put("genryoukigou", "genryoukigou");                                            // 原料記号
        mapping.put("goki", "goki");                                                            // 秤量号機
        mapping.put("syugenryouhyouryoukaisinichiji", "syugenryouhyouryoukaisinichiji");        // 主原料秤量開始日時
        mapping.put("syugenryou1_zairyouhinmei", "syugenryou1_zairyouhinmei");                  // 主原料①_材料品名
        mapping.put("syugenryou1_tyougouryoukikaku", "syugenryou1_tyougouryoukikaku");          // 主原料①_調合量規格
        mapping.put("syugenryou1_buzaizaikolotno", "syugenryou1_buzaizaikolotno");              // 主原料①_部材在庫No
        mapping.put("syugenryou1_tyougouryou1", "syugenryou1_tyougouryou1");                    // 主原料①_調合量1
        mapping.put("syugenryou1_tyougouryou2", "syugenryou1_tyougouryou2");                    // 主原料①_調合量2
        mapping.put("syugenryou1_tyougouryou3", "syugenryou1_tyougouryou3");                    // 主原料①_調合量3
        mapping.put("syugenryou1_tyougouryou4", "syugenryou1_tyougouryou4");                    // 主原料①_調合量4
        mapping.put("syugenryou1_tyougouryou5", "syugenryou1_tyougouryou5");                    // 主原料①_調合量5
        mapping.put("syugenryou1_tyougouryou6", "syugenryou1_tyougouryou6");                    // 主原料①_調合量6
        mapping.put("syugenryou1_tyougouryou7", "syugenryou1_tyougouryou7");                    // 主原料①_調合量7
        mapping.put("syugenryou2_zairyouhinmei", "syugenryou2_zairyouhinmei");                  // 主原料②_材料品名
        mapping.put("syugenryou2_tyougouryoukikaku", "syugenryou2_tyougouryoukikaku");          // 主原料②_調合量規格
        mapping.put("syugenryou2_buzaizaikolotno", "syugenryou2_buzaizaikolotno");              // 主原料②_部材在庫No
        mapping.put("syugenryou2_tyougouryou1", "syugenryou2_tyougouryou1");                    // 主原料②_調合量1
        mapping.put("syugenryou2_tyougouryou2", "syugenryou2_tyougouryou2");                    // 主原料②_調合量2
        mapping.put("syugenryou2_tyougouryou3", "syugenryou2_tyougouryou3");                    // 主原料②_調合量3
        mapping.put("syugenryou3_zairyouhinmei", "syugenryou3_zairyouhinmei");                  // 主原料③_材料品名
        mapping.put("syugenryou3_tyougouryoukikaku", "syugenryou3_tyougouryoukikaku");          // 主原料③_調合量規格
        mapping.put("syugenryou3_buzaizaikolotno", "syugenryou3_buzaizaikolotno");              // 主原料③_部材在庫No
        mapping.put("syugenryou3_tyougouryou1", "syugenryou3_tyougouryou1");                    // 主原料③_調合量1
        mapping.put("syugenryou3_tyougouryou2", "syugenryou3_tyougouryou2");                    // 主原料③_調合量2
        mapping.put("syugenryou3_tyougouryou3", "syugenryou3_tyougouryou3");                    // 主原料③_調合量3
        mapping.put("syugenryou4_zairyouhinmei", "syugenryou4_zairyouhinmei");                  // 主原料④_材料品名
        mapping.put("syugenryou4_tyougouryoukikaku", "syugenryou4_tyougouryoukikaku");          // 主原料④_調合量規格
        mapping.put("syugenryou4_buzaizaikolotno", "syugenryou4_buzaizaikolotno");              // 主原料④_部材在庫No
        mapping.put("syugenryou4_tyougouryou1", "syugenryou4_tyougouryou1");                    // 主原料④_調合量1
        mapping.put("syugenryou4_tyougouryou2", "syugenryou4_tyougouryou2");                    // 主原料④_調合量2
        mapping.put("syugenryou5_zairyouhinmei", "syugenryou5_zairyouhinmei");                  // 主原料⑤_材料品名
        mapping.put("syugenryou5_tyougouryoukikaku", "syugenryou5_tyougouryoukikaku");          // 主原料⑤_調合量規格
        mapping.put("syugenryou5_buzaizaikolotno", "syugenryou5_buzaizaikolotno");              // 主原料⑤_部材在庫No
        mapping.put("syugenryou5_tyougouryou1", "syugenryou5_tyougouryou1");                    // 主原料⑤_調合量1
        mapping.put("syugenryou5_tyougouryou2", "syugenryou5_tyougouryou2");                    // 主原料⑤_調合量2
        mapping.put("syugenryou6_zairyouhinmei", "syugenryou6_zairyouhinmei");                  // 主原料⑥_材料品名
        mapping.put("syugenryou6_tyougouryoukikaku", "syugenryou6_tyougouryoukikaku");          // 主原料⑥_調合量規格
        mapping.put("syugenryou6_buzaizaikolotno", "syugenryou6_buzaizaikolotno");              // 主原料⑥_部材在庫No
        mapping.put("syugenryou6_tyougouryou1", "syugenryou6_tyougouryou1");                    // 主原料⑥_調合量1
        mapping.put("tyougouryougoukei", "tyougouryougoukei");                                  // 調合量合計
        mapping.put("syugenryouhyouryousyuuryounichiji", "syugenryouhyouryousyuuryounichiji");  // 主原料秤量終了日時
        mapping.put("kokeibunsokuteitantousya", "kokeibunsokuteitantousya");                    // 固形分測定担当者
        mapping.put("bikou1", "bikou1");                                                        // 備考1
        mapping.put("bikou2", "bikou2");                                                        // 備考2
        mapping.put("torokunichiji", "torokunichiji");                                          // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                                            // 更新日時
        mapping.put("revision", "revision");                                                    // revision
        mapping.put("deleteflag", "deleteflag");                                                // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrYuudentaiSyugenryou>> beanHandler = new BeanListHandler<>(SrYuudentaiSyugenryou.class, rowProcessor);

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
     * @param srYuudentaiSyugenryou 誘電体ｽﾗﾘｰ作製・主原料秤量データ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrYuudentaiSyugenryou srYuudentaiSyugenryou) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srYuudentaiSyugenryou != null) {
            // 元データが存在する場合元データより取得
            return getSrYuudentaiSyugenryouItemData(itemId, srYuudentaiSyugenryou);
        } else {
            return null;
        }
    }

    /**
     * 項目データ(入力値)取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @param srGlasshyoryo 誘電体ｽﾗﾘｰ作製・主原料秤量データ
     * @return 入力値
     */
    private String getItemKikakuchi(List<FXHDD01> listData, String itemId, SrYuudentaiSyugenryou srYuudentaiSyugenryou) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return StringUtil.nullToBlank(selectData.get(0).getKikakuChi()).replace("【", "").replace("】", "");
        } else if (srYuudentaiSyugenryou != null) {
            // 元データが存在する場合元データより取得
            return getSrYuudentaiSyugenryouItemData(itemId, srYuudentaiSyugenryou);
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
     * 誘電体ｽﾗﾘｰ作製・主原料秤量_仮登録(tmp_sr_yuudentai_syugenryou)登録処理
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
    private void insertTmpSrYuudentaiSyugenryou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "INSERT INTO tmp_sr_yuudentai_syugenryou ( "
                + " kojyo,lotno,edaban,yuudentaislurryhinmei,yuudentaislurrylotno,lotkubun,genryoulotno,genryoukigou,goki,syugenryouhyouryoukaisinichiji,"
                + "syugenryou1_zairyouhinmei,syugenryou1_tyougouryoukikaku,syugenryou1_buzaizaikolotno,syugenryou1_tyougouryou1,syugenryou1_tyougouryou2,"
                + "syugenryou1_tyougouryou3,syugenryou1_tyougouryou4,syugenryou1_tyougouryou5,syugenryou1_tyougouryou6,syugenryou1_tyougouryou7,"
                + "syugenryou2_zairyouhinmei,syugenryou2_tyougouryoukikaku,syugenryou2_buzaizaikolotno,syugenryou2_tyougouryou1,syugenryou2_tyougouryou2,"
                + "syugenryou2_tyougouryou3,syugenryou3_zairyouhinmei,syugenryou3_tyougouryoukikaku,syugenryou3_buzaizaikolotno,syugenryou3_tyougouryou1,"
                + "syugenryou3_tyougouryou2,syugenryou3_tyougouryou3,syugenryou4_zairyouhinmei,syugenryou4_tyougouryoukikaku,syugenryou4_buzaizaikolotno,"
                + "syugenryou4_tyougouryou1,syugenryou4_tyougouryou2,syugenryou5_zairyouhinmei,syugenryou5_tyougouryoukikaku,syugenryou5_buzaizaikolotno,"
                + "syugenryou5_tyougouryou1,syugenryou5_tyougouryou2,syugenryou6_zairyouhinmei,syugenryou6_tyougouryoukikaku,syugenryou6_buzaizaikolotno,"
                + "syugenryou6_tyougouryou1,tyougouryougoukei,syugenryouhyouryousyuuryounichiji,kokeibunsokuteitantousya,bikou1,bikou2,torokunichiji,"
                + "kosinnichiji,revision,deleteflag "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrYuudentaiSyugenryou(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, processData, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・主原料秤量_仮登録(tmp_sr_yuudentai_syugenryou)更新処理
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
    private void updateTmpSrYuudentaiSyugenryou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "UPDATE tmp_sr_yuudentai_syugenryou SET "
                + " yuudentaislurryhinmei = ?,yuudentaislurrylotno = ?,lotkubun = ?,genryoulotno = ?,genryoukigou = ?,goki = ?,syugenryouhyouryoukaisinichiji = ?,"
                + "syugenryou1_zairyouhinmei = ?,syugenryou1_tyougouryoukikaku = ?,syugenryou1_buzaizaikolotno = ?,syugenryou1_tyougouryou1 = ?,syugenryou1_tyougouryou2 = ?,"
                + "syugenryou1_tyougouryou3 = ?,syugenryou1_tyougouryou4 = ?,syugenryou1_tyougouryou5 = ?,syugenryou1_tyougouryou6 = ?,syugenryou1_tyougouryou7 = ?,"
                + "syugenryou2_zairyouhinmei = ?,syugenryou2_tyougouryoukikaku = ?,syugenryou2_buzaizaikolotno = ?,syugenryou2_tyougouryou1 = ?,syugenryou2_tyougouryou2 = ?,"
                + "syugenryou2_tyougouryou3 = ?,syugenryou3_zairyouhinmei = ?,syugenryou3_tyougouryoukikaku = ?,syugenryou3_buzaizaikolotno = ?,syugenryou3_tyougouryou1 = ?,"
                + "syugenryou3_tyougouryou2 = ?,syugenryou3_tyougouryou3 = ?,syugenryou4_zairyouhinmei = ?,syugenryou4_tyougouryoukikaku = ?,syugenryou4_buzaizaikolotno = ?,"
                + "syugenryou4_tyougouryou1 = ?,syugenryou4_tyougouryou2 = ?,syugenryou5_zairyouhinmei = ?,syugenryou5_tyougouryoukikaku = ?,syugenryou5_buzaizaikolotno = ?,"
                + "syugenryou5_tyougouryou1 = ?,syugenryou5_tyougouryou2 = ?,syugenryou6_zairyouhinmei = ?,syugenryou6_tyougouryoukikaku = ?,syugenryou6_buzaizaikolotno = ?,"
                + "syugenryou6_tyougouryou1 = ?,tyougouryougoukei = ?,syugenryouhyouryousyuuryounichiji = ?,kokeibunsokuteitantousya = ?,bikou1 = ?,bikou2 = ?,"
                + "kosinnichiji = ?,revision = ?,deleteflag = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrYuudentaiSyugenryou> srYuudentaiSyugenryouList = getSrYuudentaiSyugenryouData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrYuudentaiSyugenryou srYuudentaiSyugenryou = null;
        if (!srYuudentaiSyugenryouList.isEmpty()) {
            srYuudentaiSyugenryou = srYuudentaiSyugenryouList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrYuudentaiSyugenryou(false, newRev, 0, "", "", "", systemTime, processData, srYuudentaiSyugenryou);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・主原料秤量_仮登録(tmp_sr_yuudentai_syugenryou)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrYuudentaiSyugenryou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sr_yuudentai_syugenryou "
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
     * 誘電体ｽﾗﾘｰ作製・主原料秤量_仮登録(tmp_sr_yuudentai_syugenryou)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srYuudentaiSyugenryou 誘電体ｽﾗﾘｰ作製・主原料秤量データ
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrYuudentaiSyugenryou(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, String systemTime, ProcessData processData, SrYuudentaiSyugenryou srYuudentaiSyugenryou) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // 主原料秤量開始日時
        String sgrhyouryoukaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B022Const.SGRHYOURYOUKAISI_TIME, srYuudentaiSyugenryou));
        // 主原料秤量終了日時
        String sgrhyouryousyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B022Const.SGRHYOURYOUSYUURYOU_TIME, srYuudentaiSyugenryou));

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B022Const.YUUDENTAISLURRYHINMEI, srYuudentaiSyugenryou)));      // 誘電体ｽﾗﾘｰ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B022Const.YUUDENTAISLURRYLOTNO, srYuudentaiSyugenryou)));       // 誘電体ｽﾗﾘｰLotNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B022Const.LOTKUBUN, srYuudentaiSyugenryou)));                   // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B022Const.GENRYOULOTNO, srYuudentaiSyugenryou)));          // 原料LotNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B022Const.GENRYOUKIGOU, srYuudentaiSyugenryou)));          // 原料記号
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B022Const.GOKI, srYuudentaiSyugenryou)));                       // 秤量号機
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B022Const.SGRHYOURYOUKAISI_DAY, srYuudentaiSyugenryou),
                "".equals(sgrhyouryoukaisiTime) ? "0000" : sgrhyouryoukaisiTime));                                                                       // 主原料秤量開始日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B022Const.SGR1_ZAIRYOUHINMEI, srYuudentaiSyugenryou)));    // 主原料①_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B022Const.SGR1_TYOUGOURYOUKIKAKU, srYuudentaiSyugenryou)));// 主原料①_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B022Const.SGR1_BUZAIZAIKOLOTNO, srYuudentaiSyugenryou)));       // 主原料①_部材在庫No
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B022Const.SGR1_TYOUGOURYOU1, srYuudentaiSyugenryou)));             // 主原料①_調合量1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B022Const.SGR1_TYOUGOURYOU2, srYuudentaiSyugenryou)));             // 主原料①_調合量2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B022Const.SGR1_TYOUGOURYOU3, srYuudentaiSyugenryou)));             // 主原料①_調合量3
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B022Const.SGR1_TYOUGOURYOU4, srYuudentaiSyugenryou)));             // 主原料①_調合量4
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B022Const.SGR1_TYOUGOURYOU5, srYuudentaiSyugenryou)));             // 主原料①_調合量5
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B022Const.SGR1_TYOUGOURYOU6, srYuudentaiSyugenryou)));             // 主原料①_調合量6
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B022Const.SGR1_TYOUGOURYOU7, srYuudentaiSyugenryou)));             // 主原料①_調合量7
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B022Const.SGR2_ZAIRYOUHINMEI, srYuudentaiSyugenryou)));    // 主原料②_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B022Const.SGR2_TYOUGOURYOUKIKAKU, srYuudentaiSyugenryou)));// 主原料②_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B022Const.SGR2_BUZAIZAIKOLOTNO, srYuudentaiSyugenryou)));       // 主原料②_部材在庫No
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B022Const.SGR2_TYOUGOURYOU1, srYuudentaiSyugenryou)));             // 主原料②_調合量1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B022Const.SGR2_TYOUGOURYOU2, srYuudentaiSyugenryou)));             // 主原料②_調合量2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B022Const.SGR2_TYOUGOURYOU3, srYuudentaiSyugenryou)));             // 主原料②_調合量3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B022Const.SGR3_ZAIRYOUHINMEI, srYuudentaiSyugenryou)));    // 主原料③_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B022Const.SGR3_TYOUGOURYOUKIKAKU, srYuudentaiSyugenryou)));// 主原料③_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B022Const.SGR3_BUZAIZAIKOLOTNO, srYuudentaiSyugenryou)));       // 主原料③_部材在庫No
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B022Const.SGR3_TYOUGOURYOU1, srYuudentaiSyugenryou)));             // 主原料③_調合量1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B022Const.SGR3_TYOUGOURYOU2, srYuudentaiSyugenryou)));             // 主原料③_調合量2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B022Const.SGR3_TYOUGOURYOU3, srYuudentaiSyugenryou)));             // 主原料③_調合量3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B022Const.SGR4_ZAIRYOUHINMEI, srYuudentaiSyugenryou)));    // 主原料④_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B022Const.SGR4_TYOUGOURYOUKIKAKU, srYuudentaiSyugenryou)));// 主原料④_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B022Const.SGR4_BUZAIZAIKOLOTNO, srYuudentaiSyugenryou)));       // 主原料④_部材在庫No
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B022Const.SGR4_TYOUGOURYOU1, srYuudentaiSyugenryou)));             // 主原料④_調合量1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B022Const.SGR4_TYOUGOURYOU2, srYuudentaiSyugenryou)));             // 主原料④_調合量2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B022Const.SGR5_ZAIRYOUHINMEI, srYuudentaiSyugenryou)));    // 主原料⑤_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B022Const.SGR5_TYOUGOURYOUKIKAKU, srYuudentaiSyugenryou)));// 主原料⑤_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B022Const.SGR5_BUZAIZAIKOLOTNO, srYuudentaiSyugenryou)));       // 主原料⑤_部材在庫No
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B022Const.SGR5_TYOUGOURYOU1, srYuudentaiSyugenryou)));             // 主原料⑤_調合量1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B022Const.SGR5_TYOUGOURYOU2, srYuudentaiSyugenryou)));             // 主原料⑤_調合量2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B022Const.SGR6_ZAIRYOUHINMEI, srYuudentaiSyugenryou)));    // 主原料⑥_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B022Const.SGR6_TYOUGOURYOUKIKAKU, srYuudentaiSyugenryou)));// 主原料⑥_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B022Const.SGR6_BUZAIZAIKOLOTNO, srYuudentaiSyugenryou)));       // 主原料⑥_部材在庫No
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B022Const.SGR6_TYOUGOURYOU1, srYuudentaiSyugenryou)));             // 主原料⑥_調合量1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B022Const.TYOUGOURYOUGOUKEI, srYuudentaiSyugenryou)));             // 調合量合計
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B022Const.SGRHYOURYOUSYUURYOU_DAY, srYuudentaiSyugenryou),
                "".equals(sgrhyouryousyuuryouTime) ? "0000" : sgrhyouryousyuuryouTime));                                                                 // 主原料秤量終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B022Const.KOKEIBUNSOKUTEITANTOUSYA, srYuudentaiSyugenryou)));   // 固形分測定担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B022Const.BIKOU1, srYuudentaiSyugenryou)));                     // 備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B022Const.BIKOU2, srYuudentaiSyugenryou)));                     // 備考2

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
     * 誘電体ｽﾗﾘｰ作製・主原料秤量(sr_yuudentai_syugenryou)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param tmpSrYuudentaiSyugenryou 仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrYuudentaiSyugenryou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData, SrYuudentaiSyugenryou tmpSrYuudentaiSyugenryou) throws SQLException {

        String sql = "INSERT INTO sr_yuudentai_syugenryou ( "
                + " kojyo,lotno,edaban,yuudentaislurryhinmei,yuudentaislurrylotno,lotkubun,genryoulotno,genryoukigou,goki,syugenryouhyouryoukaisinichiji,"
                + "syugenryou1_zairyouhinmei,syugenryou1_tyougouryoukikaku,syugenryou1_buzaizaikolotno,syugenryou1_tyougouryou1,syugenryou1_tyougouryou2,"
                + "syugenryou1_tyougouryou3,syugenryou1_tyougouryou4,syugenryou1_tyougouryou5,syugenryou1_tyougouryou6,syugenryou1_tyougouryou7,"
                + "syugenryou2_zairyouhinmei,syugenryou2_tyougouryoukikaku,syugenryou2_buzaizaikolotno,syugenryou2_tyougouryou1,syugenryou2_tyougouryou2,"
                + "syugenryou2_tyougouryou3,syugenryou3_zairyouhinmei,syugenryou3_tyougouryoukikaku,syugenryou3_buzaizaikolotno,syugenryou3_tyougouryou1,"
                + "syugenryou3_tyougouryou2,syugenryou3_tyougouryou3,syugenryou4_zairyouhinmei,syugenryou4_tyougouryoukikaku,syugenryou4_buzaizaikolotno,"
                + "syugenryou4_tyougouryou1,syugenryou4_tyougouryou2,syugenryou5_zairyouhinmei,syugenryou5_tyougouryoukikaku,syugenryou5_buzaizaikolotno,"
                + "syugenryou5_tyougouryou1,syugenryou5_tyougouryou2,syugenryou6_zairyouhinmei,syugenryou6_tyougouryoukikaku,syugenryou6_buzaizaikolotno,"
                + "syugenryou6_tyougouryou1,tyougouryougoukei,syugenryouhyouryousyuuryounichiji,kokeibunsokuteitantousya,bikou1,bikou2,torokunichiji,"
                + "kosinnichiji,revision "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrYuudentaiSyugenryou(true, newRev, kojyo, lotNo, edaban, systemTime, processData, tmpSrYuudentaiSyugenryou);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・主原料秤量(sr_yuudentai_syugenryou)更新処理
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
    private void updateSrYuudentaiSyugenryou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {
        String sql = "UPDATE sr_yuudentai_syugenryou SET "
                + " yuudentaislurryhinmei = ?,yuudentaislurrylotno = ?,lotkubun = ?,genryoulotno = ?,genryoukigou = ?,goki = ?,syugenryouhyouryoukaisinichiji = ?,"
                + "syugenryou1_zairyouhinmei = ?,syugenryou1_tyougouryoukikaku = ?,syugenryou1_buzaizaikolotno = ?,syugenryou1_tyougouryou1 = ?,syugenryou1_tyougouryou2 = ?,"
                + "syugenryou1_tyougouryou3 = ?,syugenryou1_tyougouryou4 = ?,syugenryou1_tyougouryou5 = ?,syugenryou1_tyougouryou6 = ?,syugenryou1_tyougouryou7 = ?,"
                + "syugenryou2_zairyouhinmei = ?,syugenryou2_tyougouryoukikaku = ?,syugenryou2_buzaizaikolotno = ?,syugenryou2_tyougouryou1 = ?,syugenryou2_tyougouryou2 = ?,"
                + "syugenryou2_tyougouryou3 = ?,syugenryou3_zairyouhinmei = ?,syugenryou3_tyougouryoukikaku = ?,syugenryou3_buzaizaikolotno = ?,syugenryou3_tyougouryou1 = ?,"
                + "syugenryou3_tyougouryou2 = ?,syugenryou3_tyougouryou3 = ?,syugenryou4_zairyouhinmei = ?,syugenryou4_tyougouryoukikaku = ?,syugenryou4_buzaizaikolotno = ?,"
                + "syugenryou4_tyougouryou1 = ?,syugenryou4_tyougouryou2 = ?,syugenryou5_zairyouhinmei = ?,syugenryou5_tyougouryoukikaku = ?,syugenryou5_buzaizaikolotno = ?,"
                + "syugenryou5_tyougouryou1 = ?,syugenryou5_tyougouryou2 = ?,syugenryou6_zairyouhinmei = ?,syugenryou6_tyougouryoukikaku = ?,syugenryou6_buzaizaikolotno = ?,"
                + "syugenryou6_tyougouryou1 = ?,tyougouryougoukei = ?,syugenryouhyouryousyuuryounichiji = ?,kokeibunsokuteitantousya = ?,bikou1 = ?,bikou2 = ?,"
                + "kosinnichiji = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrYuudentaiSyugenryou> srYuudentaiSyugenryouList = getSrYuudentaiSyugenryouData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrYuudentaiSyugenryou srYuudentaiSyugenryou = null;
        if (!srYuudentaiSyugenryouList.isEmpty()) {
            srYuudentaiSyugenryou = srYuudentaiSyugenryouList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrYuudentaiSyugenryou(false, newRev, "", "", "", systemTime, processData, srYuudentaiSyugenryou);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・主原料秤量(sr_yuudentai_syugenryou)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srYuudentaiSyugenryou 誘電体ｽﾗﾘｰ作製・主原料秤量データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrYuudentaiSyugenryou(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            String systemTime, ProcessData processData, SrYuudentaiSyugenryou srYuudentaiSyugenryou) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // 主原料秤量開始日時
        String sgrhyouryoukaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B022Const.SGRHYOURYOUKAISI_TIME, srYuudentaiSyugenryou));
        // 主原料秤量終了日時
        String sgrhyouryousyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B022Const.SGRHYOURYOUSYUURYOU_TIME, srYuudentaiSyugenryou));

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }

        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B022Const.YUUDENTAISLURRYHINMEI, srYuudentaiSyugenryou)));      // 誘電体ｽﾗﾘｰ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B022Const.YUUDENTAISLURRYLOTNO, srYuudentaiSyugenryou)));       // 誘電体ｽﾗﾘｰLotNo
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B022Const.LOTKUBUN, srYuudentaiSyugenryou)));                   // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B022Const.GENRYOULOTNO, srYuudentaiSyugenryou)));          // 原料LotNo
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B022Const.GENRYOUKIGOU, srYuudentaiSyugenryou)));          // 原料記号
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B022Const.GOKI, srYuudentaiSyugenryou)));                       // 秤量号機
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B022Const.SGRHYOURYOUKAISI_DAY, srYuudentaiSyugenryou),
                "".equals(sgrhyouryoukaisiTime) ? "0000" : sgrhyouryoukaisiTime));                                                            // 主原料秤量開始日時
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B022Const.SGR1_ZAIRYOUHINMEI, srYuudentaiSyugenryou)));    // 主原料①_材料品名
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B022Const.SGR1_TYOUGOURYOUKIKAKU, srYuudentaiSyugenryou)));// 主原料①_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B022Const.SGR1_BUZAIZAIKOLOTNO, srYuudentaiSyugenryou)));       // 主原料①_部材在庫No
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B022Const.SGR1_TYOUGOURYOU1, srYuudentaiSyugenryou)));             // 主原料①_調合量1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B022Const.SGR1_TYOUGOURYOU2, srYuudentaiSyugenryou)));             // 主原料①_調合量2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B022Const.SGR1_TYOUGOURYOU3, srYuudentaiSyugenryou)));             // 主原料①_調合量3
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B022Const.SGR1_TYOUGOURYOU4, srYuudentaiSyugenryou)));             // 主原料①_調合量4
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B022Const.SGR1_TYOUGOURYOU5, srYuudentaiSyugenryou)));             // 主原料①_調合量5
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B022Const.SGR1_TYOUGOURYOU6, srYuudentaiSyugenryou)));             // 主原料①_調合量6
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B022Const.SGR1_TYOUGOURYOU7, srYuudentaiSyugenryou)));             // 主原料①_調合量7
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B022Const.SGR2_ZAIRYOUHINMEI, srYuudentaiSyugenryou)));    // 主原料②_材料品名
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B022Const.SGR2_TYOUGOURYOUKIKAKU, srYuudentaiSyugenryou)));// 主原料②_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B022Const.SGR2_BUZAIZAIKOLOTNO, srYuudentaiSyugenryou)));       // 主原料②_部材在庫No
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B022Const.SGR2_TYOUGOURYOU1, srYuudentaiSyugenryou)));             // 主原料②_調合量1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B022Const.SGR2_TYOUGOURYOU2, srYuudentaiSyugenryou)));             // 主原料②_調合量2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B022Const.SGR2_TYOUGOURYOU3, srYuudentaiSyugenryou)));             // 主原料②_調合量3
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B022Const.SGR3_ZAIRYOUHINMEI, srYuudentaiSyugenryou)));    // 主原料③_材料品名
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B022Const.SGR3_TYOUGOURYOUKIKAKU, srYuudentaiSyugenryou)));// 主原料③_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B022Const.SGR3_BUZAIZAIKOLOTNO, srYuudentaiSyugenryou)));       // 主原料③_部材在庫No
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B022Const.SGR3_TYOUGOURYOU1, srYuudentaiSyugenryou)));             // 主原料③_調合量1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B022Const.SGR3_TYOUGOURYOU2, srYuudentaiSyugenryou)));             // 主原料③_調合量2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B022Const.SGR3_TYOUGOURYOU3, srYuudentaiSyugenryou)));             // 主原料③_調合量3
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B022Const.SGR4_ZAIRYOUHINMEI, srYuudentaiSyugenryou)));    // 主原料④_材料品名
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B022Const.SGR4_TYOUGOURYOUKIKAKU, srYuudentaiSyugenryou)));// 主原料④_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B022Const.SGR4_BUZAIZAIKOLOTNO, srYuudentaiSyugenryou)));       // 主原料④_部材在庫No
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B022Const.SGR4_TYOUGOURYOU1, srYuudentaiSyugenryou)));             // 主原料④_調合量1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B022Const.SGR4_TYOUGOURYOU2, srYuudentaiSyugenryou)));             // 主原料④_調合量2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B022Const.SGR5_ZAIRYOUHINMEI, srYuudentaiSyugenryou)));    // 主原料⑤_材料品名
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B022Const.SGR5_TYOUGOURYOUKIKAKU, srYuudentaiSyugenryou)));// 主原料⑤_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B022Const.SGR5_BUZAIZAIKOLOTNO, srYuudentaiSyugenryou)));       // 主原料⑤_部材在庫No
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B022Const.SGR5_TYOUGOURYOU1, srYuudentaiSyugenryou)));             // 主原料⑤_調合量1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B022Const.SGR5_TYOUGOURYOU2, srYuudentaiSyugenryou)));             // 主原料⑤_調合量2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B022Const.SGR6_ZAIRYOUHINMEI, srYuudentaiSyugenryou)));    // 主原料⑥_材料品名
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B022Const.SGR6_TYOUGOURYOUKIKAKU, srYuudentaiSyugenryou)));// 主原料⑥_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B022Const.SGR6_BUZAIZAIKOLOTNO, srYuudentaiSyugenryou)));       // 主原料⑥_部材在庫No
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B022Const.SGR6_TYOUGOURYOU1, srYuudentaiSyugenryou)));             // 主原料⑥_調合量1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B022Const.TYOUGOURYOUGOUKEI, srYuudentaiSyugenryou)));             // 調合量合計
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B022Const.SGRHYOURYOUSYUURYOU_DAY, srYuudentaiSyugenryou),
                "".equals(sgrhyouryousyuuryouTime) ? "0000" : sgrhyouryousyuuryouTime));                                                      // 主原料秤量終了日時
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B022Const.KOKEIBUNSOKUTEITANTOUSYA, srYuudentaiSyugenryou)));   // 固形分測定担当者
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B022Const.BIKOU1, srYuudentaiSyugenryou)));                     // 備考1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B022Const.BIKOU2, srYuudentaiSyugenryou)));                     // 備考2

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
     * 誘電体ｽﾗﾘｰ作製・主原料秤量(sr_yuudentai_syugenryou)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSrYuudentaiSyugenryou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sr_yuudentai_syugenryou "
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
     * [誘電体ｽﾗﾘｰ作製・主原料秤量_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_yuudentai_syugenryou "
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
     * @param srYuudentaiSyugenryou 誘電体ｽﾗﾘｰ作製・主原料秤量データ
     * @return DB値
     */
    private String getSrYuudentaiSyugenryouItemData(String itemId, SrYuudentaiSyugenryou srYuudentaiSyugenryou) {
        switch (itemId) {
            // 誘電体ｽﾗﾘｰ品名
            case GXHDO102B022Const.YUUDENTAISLURRYHINMEI:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getYuudentaislurryhinmei());

            // 誘電体ｽﾗﾘｰLotNo
            case GXHDO102B022Const.YUUDENTAISLURRYLOTNO:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getYuudentaislurrylotno());

            // ﾛｯﾄ区分
            case GXHDO102B022Const.LOTKUBUN:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getLotkubun());

            // 原料LotNo
            case GXHDO102B022Const.GENRYOULOTNO:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getGenryoulotno());

            // 原料記号
            case GXHDO102B022Const.GENRYOUKIGOU:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getGenryoukigou());

            // 秤量号機
            case GXHDO102B022Const.GOKI:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getGoki());

            // 主原料秤量開始日
            case GXHDO102B022Const.SGRHYOURYOUKAISI_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiSyugenryou.getSyugenryouhyouryoukaisinichiji(), "yyMMdd");

            // 主原料秤量開始時間
            case GXHDO102B022Const.SGRHYOURYOUKAISI_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiSyugenryou.getSyugenryouhyouryoukaisinichiji(), "HHmm");

            // 主原料①_材料品名
            case GXHDO102B022Const.SGR1_ZAIRYOUHINMEI:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou1_zairyouhinmei());

            // 主原料①_調合量規格
            case GXHDO102B022Const.SGR1_TYOUGOURYOUKIKAKU:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou1_tyougouryoukikaku());

            // 主原料①_部材在庫No
            case GXHDO102B022Const.SGR1_BUZAIZAIKOLOTNO:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou1_buzaizaikolotno());

            // 主原料①_調合量1
            case GXHDO102B022Const.SGR1_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou1_tyougouryou1());

            // 主原料①_調合量2
            case GXHDO102B022Const.SGR1_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou1_tyougouryou2());

            // 主原料①_調合量3
            case GXHDO102B022Const.SGR1_TYOUGOURYOU3:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou1_tyougouryou3());

            // 主原料①_調合量4
            case GXHDO102B022Const.SGR1_TYOUGOURYOU4:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou1_tyougouryou4());

            // 主原料①_調合量5
            case GXHDO102B022Const.SGR1_TYOUGOURYOU5:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou1_tyougouryou5());

            // 主原料①_調合量6
            case GXHDO102B022Const.SGR1_TYOUGOURYOU6:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou1_tyougouryou6());

            // 主原料①_調合量7
            case GXHDO102B022Const.SGR1_TYOUGOURYOU7:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou1_tyougouryou7());

            // 主原料②_材料品名
            case GXHDO102B022Const.SGR2_ZAIRYOUHINMEI:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou2_zairyouhinmei());

            // 主原料②_調合量規格
            case GXHDO102B022Const.SGR2_TYOUGOURYOUKIKAKU:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou2_tyougouryoukikaku());

            // 主原料②_部材在庫No
            case GXHDO102B022Const.SGR2_BUZAIZAIKOLOTNO:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou2_buzaizaikolotno());

            // 主原料②_調合量1
            case GXHDO102B022Const.SGR2_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou2_tyougouryou1());

            // 主原料②_調合量2
            case GXHDO102B022Const.SGR2_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou2_tyougouryou2());

            // 主原料②_調合量3
            case GXHDO102B022Const.SGR2_TYOUGOURYOU3:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou2_tyougouryou3());

            // 主原料③_材料品名
            case GXHDO102B022Const.SGR3_ZAIRYOUHINMEI:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou3_zairyouhinmei());

            // 主原料③_調合量規格
            case GXHDO102B022Const.SGR3_TYOUGOURYOUKIKAKU:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou3_tyougouryoukikaku());

            // 主原料③_部材在庫No
            case GXHDO102B022Const.SGR3_BUZAIZAIKOLOTNO:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou3_buzaizaikolotno());

            // 主原料③_調合量1
            case GXHDO102B022Const.SGR3_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou3_tyougouryou1());

            // 主原料③_調合量2
            case GXHDO102B022Const.SGR3_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou3_tyougouryou2());

            // 主原料③_調合量3
            case GXHDO102B022Const.SGR3_TYOUGOURYOU3:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou3_tyougouryou3());

            // 主原料④_材料品名
            case GXHDO102B022Const.SGR4_ZAIRYOUHINMEI:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou4_zairyouhinmei());

            // 主原料④_調合量規格
            case GXHDO102B022Const.SGR4_TYOUGOURYOUKIKAKU:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou4_tyougouryoukikaku());

            // 主原料④_部材在庫No
            case GXHDO102B022Const.SGR4_BUZAIZAIKOLOTNO:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou4_buzaizaikolotno());

            // 主原料④_調合量1
            case GXHDO102B022Const.SGR4_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou4_tyougouryou1());

            // 主原料④_調合量2
            case GXHDO102B022Const.SGR4_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou4_tyougouryou2());

            // 主原料⑤_材料品名
            case GXHDO102B022Const.SGR5_ZAIRYOUHINMEI:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou5_zairyouhinmei());

            // 主原料⑤_調合量規格
            case GXHDO102B022Const.SGR5_TYOUGOURYOUKIKAKU:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou5_tyougouryoukikaku());

            // 主原料⑤_部材在庫No
            case GXHDO102B022Const.SGR5_BUZAIZAIKOLOTNO:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou5_buzaizaikolotno());

            // 主原料⑤_調合量1
            case GXHDO102B022Const.SGR5_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou5_tyougouryou1());

            // 主原料⑤_調合量2
            case GXHDO102B022Const.SGR5_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou5_tyougouryou2());

            // 主原料⑥_材料品名
            case GXHDO102B022Const.SGR6_ZAIRYOUHINMEI:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou6_zairyouhinmei());

            // 主原料⑥_調合量規格
            case GXHDO102B022Const.SGR6_TYOUGOURYOUKIKAKU:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou6_tyougouryoukikaku());

            // 主原料⑥_部材在庫No
            case GXHDO102B022Const.SGR6_BUZAIZAIKOLOTNO:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou6_buzaizaikolotno());

            // 主原料⑥_調合量1
            case GXHDO102B022Const.SGR6_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getSyugenryou6_tyougouryou1());

            // 調合量合計
            case GXHDO102B022Const.TYOUGOURYOUGOUKEI:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getTyougouryougoukei());

            // 主原料秤量終了日
            case GXHDO102B022Const.SGRHYOURYOUSYUURYOU_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiSyugenryou.getSyugenryouhyouryousyuuryounichiji(), "yyMMdd");

            // 主原料秤量終了時間
            case GXHDO102B022Const.SGRHYOURYOUSYUURYOU_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiSyugenryou.getSyugenryouhyouryousyuuryounichiji(), "HHmm");

            // 固形分測定担当者
            case GXHDO102B022Const.KOKEIBUNSOKUTEITANTOUSYA:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getKokeibunsokuteitantousya());

            // 備考1
            case GXHDO102B022Const.BIKOU1:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getBikou1());

            // 備考2
            case GXHDO102B022Const.BIKOU2:
                return StringUtil.nullToBlank(srYuudentaiSyugenryou.getBikou2());

            default:
                return null;
        }
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・主原料秤量_仮登録(tmp_sr_yuudentai_syugenryou)登録処理(削除時)
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
    private void insertDeleteDataTmpSrYuudentaiSyugenryou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_yuudentai_syugenryou ("
                + " kojyo,lotno,edaban,yuudentaislurryhinmei,yuudentaislurrylotno,lotkubun,genryoulotno,genryoukigou,goki,syugenryouhyouryoukaisinichiji,"
                + "syugenryou1_zairyouhinmei,syugenryou1_tyougouryoukikaku,syugenryou1_buzaizaikolotno,syugenryou1_tyougouryou1,syugenryou1_tyougouryou2,"
                + "syugenryou1_tyougouryou3,syugenryou1_tyougouryou4,syugenryou1_tyougouryou5,syugenryou1_tyougouryou6,syugenryou1_tyougouryou7,"
                + "syugenryou2_zairyouhinmei,syugenryou2_tyougouryoukikaku,syugenryou2_buzaizaikolotno,syugenryou2_tyougouryou1,syugenryou2_tyougouryou2,"
                + "syugenryou2_tyougouryou3,syugenryou3_zairyouhinmei,syugenryou3_tyougouryoukikaku,syugenryou3_buzaizaikolotno,syugenryou3_tyougouryou1,"
                + "syugenryou3_tyougouryou2,syugenryou3_tyougouryou3,syugenryou4_zairyouhinmei,syugenryou4_tyougouryoukikaku,syugenryou4_buzaizaikolotno,"
                + "syugenryou4_tyougouryou1,syugenryou4_tyougouryou2,syugenryou5_zairyouhinmei,syugenryou5_tyougouryoukikaku,syugenryou5_buzaizaikolotno,"
                + "syugenryou5_tyougouryou1,syugenryou5_tyougouryou2,syugenryou6_zairyouhinmei,syugenryou6_tyougouryoukikaku,syugenryou6_buzaizaikolotno,"
                + "syugenryou6_tyougouryou1,tyougouryougoukei,syugenryouhyouryousyuuryounichiji,kokeibunsokuteitantousya,bikou1,bikou2,torokunichiji,"
                + "kosinnichiji,revision,deleteflag "
                + ") SELECT "
                + " kojyo,lotno,edaban,yuudentaislurryhinmei,yuudentaislurrylotno,lotkubun,genryoulotno,genryoukigou,goki,syugenryouhyouryoukaisinichiji,"
                + "syugenryou1_zairyouhinmei,syugenryou1_tyougouryoukikaku,syugenryou1_buzaizaikolotno,syugenryou1_tyougouryou1,syugenryou1_tyougouryou2,"
                + "syugenryou1_tyougouryou3,syugenryou1_tyougouryou4,syugenryou1_tyougouryou5,syugenryou1_tyougouryou6,syugenryou1_tyougouryou7,"
                + "syugenryou2_zairyouhinmei,syugenryou2_tyougouryoukikaku,syugenryou2_buzaizaikolotno,syugenryou2_tyougouryou1,syugenryou2_tyougouryou2,"
                + "syugenryou2_tyougouryou3,syugenryou3_zairyouhinmei,syugenryou3_tyougouryoukikaku,syugenryou3_buzaizaikolotno,syugenryou3_tyougouryou1,"
                + "syugenryou3_tyougouryou2,syugenryou3_tyougouryou3,syugenryou4_zairyouhinmei,syugenryou4_tyougouryoukikaku,syugenryou4_buzaizaikolotno,"
                + "syugenryou4_tyougouryou1,syugenryou4_tyougouryou2,syugenryou5_zairyouhinmei,syugenryou5_tyougouryoukikaku,syugenryou5_buzaizaikolotno,"
                + "syugenryou5_tyougouryou1,syugenryou5_tyougouryou2,syugenryou6_zairyouhinmei,syugenryou6_tyougouryoukikaku,syugenryou6_buzaizaikolotno,"
                + "syugenryou6_tyougouryou1,tyougouryougoukei,syugenryouhyouryousyuuryounichiji,kokeibunsokuteitantousya,bikou1,bikou2,?,?,?,? "
                + " FROM sr_yuudentai_syugenryou "
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
    private void initGXHDO102B022A(ProcessData processData) {
        GXHDO102B022A bean = (GXHDO102B022A) getFormBean("gXHDO102B022A");
        bean.setWiplotno(getItemRow(processData.getItemList(), GXHDO102B022Const.WIPLOTNO));
        bean.setYuudentaislurryhinmei(getItemRow(processData.getItemList(), GXHDO102B022Const.YUUDENTAISLURRYHINMEI));
        bean.setYuudentaislurrylotno(getItemRow(processData.getItemList(), GXHDO102B022Const.YUUDENTAISLURRYLOTNO));
        bean.setLotkubun(getItemRow(processData.getItemList(), GXHDO102B022Const.LOTKUBUN));
        bean.setGenryoulotno(getItemRow(processData.getItemList(), GXHDO102B022Const.GENRYOULOTNO));
        bean.setGenryoukigou(getItemRow(processData.getItemList(), GXHDO102B022Const.GENRYOUKIGOU));
        bean.setGoki(getItemRow(processData.getItemList(), GXHDO102B022Const.GOKI));
        bean.setSgrhyouryoukaisi_day(getItemRow(processData.getItemList(), GXHDO102B022Const.SGRHYOURYOUKAISI_DAY));
        bean.setSgrhyouryoukaisi_time(getItemRow(processData.getItemList(), GXHDO102B022Const.SGRHYOURYOUKAISI_TIME));
        bean.setSgr1_zairyouhinmei(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR1_ZAIRYOUHINMEI));
        bean.setSgr1_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR1_TYOUGOURYOUKIKAKU));
        bean.setSgr1_buzaizaikolotno(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR1_BUZAIZAIKOLOTNO));
        bean.setSgr1_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR1_TYOUGOURYOU1));
        bean.setSgr1_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR1_TYOUGOURYOU2));
        bean.setSgr1_tyougouryou3(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR1_TYOUGOURYOU3));
        bean.setSgr1_tyougouryou4(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR1_TYOUGOURYOU4));
        bean.setSgr1_tyougouryou5(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR1_TYOUGOURYOU5));
        bean.setSgr1_tyougouryou6(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR1_TYOUGOURYOU6));
        bean.setSgr1_tyougouryou7(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR1_TYOUGOURYOU7));
        bean.setSgr2_zairyouhinmei(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR2_ZAIRYOUHINMEI));
        bean.setSgr2_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR2_TYOUGOURYOUKIKAKU));
        bean.setSgr2_buzaizaikolotno(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR2_BUZAIZAIKOLOTNO));
        bean.setSgr2_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR2_TYOUGOURYOU1));
        bean.setSgr2_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR2_TYOUGOURYOU2));
        bean.setSgr2_tyougouryou3(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR2_TYOUGOURYOU3));
        bean.setSgr3_zairyouhinmei(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR3_ZAIRYOUHINMEI));
        bean.setSgr3_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR3_TYOUGOURYOUKIKAKU));
        bean.setSgr3_buzaizaikolotno(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR3_BUZAIZAIKOLOTNO));
        bean.setSgr3_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR3_TYOUGOURYOU1));
        bean.setSgr3_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR3_TYOUGOURYOU2));
        bean.setSgr3_tyougouryou3(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR3_TYOUGOURYOU3));
        bean.setSgr4_zairyouhinmei(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR4_ZAIRYOUHINMEI));
        bean.setSgr4_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR4_TYOUGOURYOUKIKAKU));
        bean.setSgr4_buzaizaikolotno(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR4_BUZAIZAIKOLOTNO));
        bean.setSgr4_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR4_TYOUGOURYOU1));
        bean.setSgr4_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR4_TYOUGOURYOU2));
        bean.setSgr5_zairyouhinmei(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR5_ZAIRYOUHINMEI));
        bean.setSgr5_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR5_TYOUGOURYOUKIKAKU));
        bean.setSgr5_buzaizaikolotno(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR5_BUZAIZAIKOLOTNO));
        bean.setSgr5_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR5_TYOUGOURYOU1));
        bean.setSgr5_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR5_TYOUGOURYOU2));
        bean.setSgr6_zairyouhinmei(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR6_ZAIRYOUHINMEI));
        bean.setSgr6_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR6_TYOUGOURYOUKIKAKU));
        bean.setSgr6_buzaizaikolotno(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR6_BUZAIZAIKOLOTNO));
        bean.setSgr6_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR6_TYOUGOURYOU1));
        bean.setTyougouryougoukei(getItemRow(processData.getItemList(), GXHDO102B022Const.TYOUGOURYOUGOUKEI));
        bean.setSgrhyouryousyuuryou_day(getItemRow(processData.getItemList(), GXHDO102B022Const.SGRHYOURYOUSYUURYOU_DAY));
        bean.setSgrhyouryousyuuryou_time(getItemRow(processData.getItemList(), GXHDO102B022Const.SGRHYOURYOUSYUURYOU_TIME));
        bean.setKokeibunsokuteitantousya(getItemRow(processData.getItemList(), GXHDO102B022Const.KOKEIBUNSOKUTEITANTOUSYA));
        bean.setBikou1(getItemRow(processData.getItemList(), GXHDO102B022Const.BIKOU1));
        bean.setBikou2(getItemRow(processData.getItemList(), GXHDO102B022Const.BIKOU2));

    }

    /**
     * 誘電体ｽﾗﾘｰ作製・主原料秤量入力_ｻﾌﾞ画面データの規格値取得処理
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
     * 主原料1_材料品名のﾘﾝｸ押下時、 誘電体ｽﾗﾘｰ作製・主原料秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC011SubGamen1(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B022Const.SGR1_BUZAIZAIKOLOTNO, GXHDO102B022Const.SGR1_TYOUGOURYOU1,
                GXHDO102B022Const.SGR1_TYOUGOURYOU2, GXHDO102B022Const.SGR1_TYOUGOURYOU3, GXHDO102B022Const.SGR1_TYOUGOURYOU4,
                GXHDO102B022Const.SGR1_TYOUGOURYOU5, GXHDO102B022Const.SGR1_TYOUGOURYOU6, GXHDO102B022Const.SGR1_TYOUGOURYOU7);
        return openC011SubGamen(processData, 1, returnItemIdList, GXHDO102B022Const.SGR1_TYOUGOURYOUKIKAKU);
    }

    /**
     * 主原料2_材料品名のﾘﾝｸ押下時、 誘電体ｽﾗﾘｰ作製・主原料秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC011SubGamen2(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B022Const.SGR2_BUZAIZAIKOLOTNO, GXHDO102B022Const.SGR2_TYOUGOURYOU1,
                GXHDO102B022Const.SGR2_TYOUGOURYOU2, GXHDO102B022Const.SGR2_TYOUGOURYOU3);
        return openC011SubGamen(processData, 2, returnItemIdList, GXHDO102B022Const.SGR2_TYOUGOURYOUKIKAKU);
    }

    /**
     * 主原料3_材料品名のﾘﾝｸ押下時、 誘電体ｽﾗﾘｰ作製・主原料秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC011SubGamen3(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B022Const.SGR3_BUZAIZAIKOLOTNO, GXHDO102B022Const.SGR3_TYOUGOURYOU1,
                GXHDO102B022Const.SGR3_TYOUGOURYOU2, GXHDO102B022Const.SGR3_TYOUGOURYOU3);
        return openC011SubGamen(processData, 3, returnItemIdList, GXHDO102B022Const.SGR3_TYOUGOURYOUKIKAKU);
    }

    /**
     * 主原料4_材料品名のﾘﾝｸ押下時、 誘電体ｽﾗﾘｰ作製・主原料秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC011SubGamen4(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B022Const.SGR4_BUZAIZAIKOLOTNO, GXHDO102B022Const.SGR4_TYOUGOURYOU1, GXHDO102B022Const.SGR4_TYOUGOURYOU2);
        return openC011SubGamen(processData, 4, returnItemIdList, GXHDO102B022Const.SGR4_TYOUGOURYOUKIKAKU);
    }

    /**
     * 主原料5_材料品名のﾘﾝｸ押下時、 誘電体ｽﾗﾘｰ作製・主原料秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC011SubGamen5(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B022Const.SGR5_BUZAIZAIKOLOTNO, GXHDO102B022Const.SGR5_TYOUGOURYOU1, GXHDO102B022Const.SGR5_TYOUGOURYOU2);
        return openC011SubGamen(processData, 5, returnItemIdList, GXHDO102B022Const.SGR5_TYOUGOURYOUKIKAKU);
    }

    /**
     * 主原料6_材料品名のﾘﾝｸ押下時、 誘電体ｽﾗﾘｰ作製・主原料秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC011SubGamen6(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B022Const.SGR6_BUZAIZAIKOLOTNO, GXHDO102B022Const.SGR6_TYOUGOURYOU1);
        return openC011SubGamen(processData, 6, returnItemIdList, GXHDO102B022Const.SGR6_TYOUGOURYOUKIKAKU);
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・主原料秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @param zairyokubun 材料区分
     * @param returnItemIdList サブ画面から戻ったときに値を設定必要項目リスト
     * @param itemTyougouryoukikaku 調合量規格
     * @return 処理制御データ
     */
    public ProcessData openC011SubGamen(ProcessData processData, int zairyokubun, List<String> returnItemIdList,String itemTyougouryoukikaku) {
        try {
            // 「秤量号機」
            FXHDD01 itemGoki = getItemRow(processData.getItemList(), GXHDO102B022Const.GOKI);
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
            processData.setCollBackParam("gxhdo102c011");

            GXHDO102C011 beanGXHDO102C011 = (GXHDO102C011) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C011);
            GXHDO102C011Model gxhdo102c011model = beanGXHDO102C011.getGxhdO102c011Model();
            // 主画面からサブ画面に渡されたデータを設定
            setSubGamenInitData(gxhdo102c011model, zairyokubun, itemGoki, returnItemIdList);

            beanGXHDO102C011.setGxhdO102c011ModelView(gxhdo102c011model.clone());
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
     * @param gxhdo102c011model モデルデータ
     * @param zairyokubun 材料区分
     * @param itemGoki 秤量号機データ
     * @param returnItemIdList サブ画面から戻るデータリスト
     * @throws CloneNotSupportedException 例外エラー
     */
    private void setSubGamenInitData(GXHDO102C011Model gxhdo102c011model, int zairyokubun, FXHDD01 itemGoki, List<String> returnItemIdList) throws CloneNotSupportedException {
        GXHDO102C011Model.SubGamenData c011subgamendata = GXHDO102C011Logic.getC011subgamendata(gxhdo102c011model, zairyokubun);
        if (c011subgamendata == null) {
            return;
        }
        c011subgamendata.setSubDataGoki(StringUtil.nullToBlank(itemGoki.getValue()));
        c011subgamendata.setSubDataZairyokubun(zairyokubun);
        // サブ画面から戻ったときに値を設定する項目を指定する。
        c011subgamendata.setReturnItemIdBuzailotno(returnItemIdList.get(0)); // 部材在庫No
        c011subgamendata.setReturnItemIdTyougouryou1(returnItemIdList.get(1)); // 調合量1
        if (zairyokubun != 6) {
            c011subgamendata.setReturnItemIdTyougouryou2(returnItemIdList.get(2)); // 調合量2
            if (zairyokubun != 4 && zairyokubun != 5) {
                c011subgamendata.setReturnItemIdTyougouryou3(returnItemIdList.get(3)); // 調合量3
                if (zairyokubun != 2 && zairyokubun != 3) {
                    c011subgamendata.setReturnItemIdTyougouryou4(returnItemIdList.get(4)); // 調合量4
                    c011subgamendata.setReturnItemIdTyougouryou5(returnItemIdList.get(5)); // 調合量5
                    c011subgamendata.setReturnItemIdTyougouryou6(returnItemIdList.get(6)); // 調合量6
                    c011subgamendata.setReturnItemIdTyougouryou7(returnItemIdList.get(7)); // 調合量7
                }
            }
        }
        gxhdo102c011model.setShowsubgamendata(c011subgamendata.clone());
        // サブ画面の調合残量の計算
        GXHDO102C011Logic.calcTyogouzanryou(gxhdo102c011model);
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・主原料秤量入力_ｻﾌﾞ画面データ設定処理
     *
     * @param processData 処理制御データ
     * @param subSrYuudentaiSyugenryouList 誘電体ｽﾗﾘｰ作製・主原料秤量入力_ｻﾌﾞ画面データリスト
     */
    private void setInputItemDataSubFormC011(ProcessData processData, List<SubSrYuudentaiSyugenryou> subSrYuudentaiSyugenryouList) {
        // サブ画面の情報を取得
        GXHDO102C011 beanGXHDO102C011 = (GXHDO102C011) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C011);

        GXHDO102C011Model model;
        if (subSrYuudentaiSyugenryouList == null) {
            // 登録データが無い場合、主画面の材料品名1-2と調合量規格1-2はｻﾌﾞ画面の初期値にセットする。
            subSrYuudentaiSyugenryouList = new ArrayList<>();
            SubSrYuudentaiSyugenryou subgamen1 = new SubSrYuudentaiSyugenryou();
            SubSrYuudentaiSyugenryou subgamen2 = new SubSrYuudentaiSyugenryou();
            SubSrYuudentaiSyugenryou subgamen3 = new SubSrYuudentaiSyugenryou();
            SubSrYuudentaiSyugenryou subgamen4 = new SubSrYuudentaiSyugenryou();
            SubSrYuudentaiSyugenryou subgamen5 = new SubSrYuudentaiSyugenryou();
            SubSrYuudentaiSyugenryou subgamen6 = new SubSrYuudentaiSyugenryou();

            subgamen1.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR1_ZAIRYOUHINMEI))); // 主原料1_材料品名
            subgamen1.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR1_TYOUGOURYOUKIKAKU))); // 主原料1_調合量規格
            subgamen1.setStandardpattern(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR1_TYOUGOURYOUKIKAKU).getStandardPattern()); // 主原料1_調合量規格情報ﾊﾟﾀｰﾝ
            subgamen1.setZairyokubun(1); // 材料区分
            subgamen2.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR2_ZAIRYOUHINMEI))); // 主原料2_材料品名
            subgamen2.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR2_TYOUGOURYOUKIKAKU))); // 主原料2_調合量規格
            subgamen2.setStandardpattern(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR2_TYOUGOURYOUKIKAKU).getStandardPattern()); // 主原料2_調合量規格情報ﾊﾟﾀｰﾝ
            subgamen2.setZairyokubun(2); // 材料区分
            subgamen3.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR3_ZAIRYOUHINMEI))); // 主原料3_材料品名
            subgamen3.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR3_TYOUGOURYOUKIKAKU))); // 主原料3_調合量規格
            subgamen3.setStandardpattern(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR3_TYOUGOURYOUKIKAKU).getStandardPattern()); // 主原料3_調合量規格情報ﾊﾟﾀｰﾝ
            subgamen3.setZairyokubun(3); // 材料区分
            subgamen4.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR4_ZAIRYOUHINMEI))); // 主原料4_材料品名
            subgamen4.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR4_TYOUGOURYOUKIKAKU))); // 主原料4_調合量規格
            subgamen4.setStandardpattern(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR4_TYOUGOURYOUKIKAKU).getStandardPattern()); // 主原料4_調合量規格情報ﾊﾟﾀｰﾝ
            subgamen4.setZairyokubun(4); // 材料区分
            subgamen5.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR5_ZAIRYOUHINMEI))); // 主原料5_材料品名
            subgamen5.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR5_TYOUGOURYOUKIKAKU))); // 主原料5_調合量規格
            subgamen5.setStandardpattern(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR5_TYOUGOURYOUKIKAKU).getStandardPattern()); // 主原料5_調合量規格情報ﾊﾟﾀｰﾝ
            subgamen5.setZairyokubun(5); // 材料区分
            subgamen6.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR6_ZAIRYOUHINMEI))); // 主原料6_材料品名
            subgamen6.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR6_TYOUGOURYOUKIKAKU))); // 主原料6_調合量規格
            subgamen6.setStandardpattern(getItemRow(processData.getItemList(), GXHDO102B022Const.SGR6_TYOUGOURYOUKIKAKU).getStandardPattern()); // 主原料6_調合量規格情報ﾊﾟﾀｰﾝ
            subgamen6.setZairyokubun(6); // 材料区分
            subSrYuudentaiSyugenryouList.add(subgamen1);
            subSrYuudentaiSyugenryouList.add(subgamen2);
            subSrYuudentaiSyugenryouList.add(subgamen3);
            subSrYuudentaiSyugenryouList.add(subgamen4);
            subSrYuudentaiSyugenryouList.add(subgamen5);
            subSrYuudentaiSyugenryouList.add(subgamen6);
            model = GXHDO102C011Logic.createGXHDO102C011Model(subSrYuudentaiSyugenryouList);

        } else {
            // 登録データがあれば登録データをセットする。
            model = GXHDO102C011Logic.createGXHDO102C011Model(subSrYuudentaiSyugenryouList);
        }
        beanGXHDO102C011.setGxhdO102c011Model(model);
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・主原料秤量入力_ｻﾌﾞ画面の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return 誘電体ｽﾗﾘｰ作製・主原料秤量入力_ｻﾌﾞ画面登録データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrYuudentaiSyugenryou> getSubSrYuudentaiSyugenryouData(QueryRunner queryRunnerQcdb,
            String rev, String jotaiFlg, String kojyo, String lotNo, String edaban) throws SQLException {
        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSubSrYuudentaiSyugenryou(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSubSrYuudentaiSyugenryou(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        }
    }

    /**
     * [誘電体ｽﾗﾘｰ作製・主原料秤量入力_ｻﾌﾞ画面]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrYuudentaiSyugenryou> loadSubSrYuudentaiSyugenryou(QueryRunner queryRunnerQcdb,
            String kojyo, String lotNo, String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + " kojyo,lotno,edaban,zairyokubun,tyogouryoukikaku,tyogouzanryou,zairyohinmei,buzailotno,buzaihinmei,"
                + "tyougouryou1_1,tyougouryou1_2,tyougouryou2_1,tyougouryou2_2,tyougouryou3_1,tyougouryou3_2,tyougouryou4_1,"
                + "tyougouryou4_2,tyougouryou5_1,tyougouryou5_2,tyougouryou6_1,tyougouryou6_2,tyougouryou7_1,"
                + "tyougouryou7_2,torokunichiji,kosinnichiji,revision, '0' AS deleteflag "
                + " FROM sub_sr_yuudentai_syugenryou "
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
        mapping.put("kojyo", "kojyo");                          // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                          // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                        // 枝番
        mapping.put("zairyokubun", "zairyokubun");              // 材料区分
        mapping.put("tyogouryoukikaku", "tyogouryoukikaku");    // 調合規格
        mapping.put("tyogouzanryou", "tyogouzanryou");          // 調合残量
        mapping.put("zairyohinmei", "zairyohinmei");            // 材料品名
        mapping.put("buzailotno", "buzailotno");                // 部材在庫No
        mapping.put("buzaihinmei", "buzaihinmei");              // 部材在庫品名
        mapping.put("tyougouryou1_1", "tyougouryou1_1");        // 調合量1_1
        mapping.put("tyougouryou1_2", "tyougouryou1_2");        // 調合量1_2
        mapping.put("tyougouryou2_1", "tyougouryou2_1");        // 調合量2_1
        mapping.put("tyougouryou2_2", "tyougouryou2_2");        // 調合量2_2
        mapping.put("tyougouryou3_1", "tyougouryou3_1");        // 調合量3_1
        mapping.put("tyougouryou3_2", "tyougouryou3_2");        // 調合量3_2
        mapping.put("tyougouryou4_1", "tyougouryou4_1");        // 調合量4_1
        mapping.put("tyougouryou4_2", "tyougouryou4_2");        // 調合量4_2
        mapping.put("tyougouryou5_1", "tyougouryou5_1");        // 調合量5_1
        mapping.put("tyougouryou5_2", "tyougouryou5_2");        // 調合量5_2
        mapping.put("tyougouryou6_1", "tyougouryou6_1");        // 調合量6_1
        mapping.put("tyougouryou6_2", "tyougouryou6_2");        // 調合量6_2
        mapping.put("tyougouryou7_1", "tyougouryou7_1");        // 調合量7_1
        mapping.put("tyougouryou7_2", "tyougouryou7_2");        // 調合量7_2
        mapping.put("torokunichiji", "torokunichiji");          // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");            // 更新日時
        mapping.put("revision", "revision");                    // revision
        mapping.put("deleteflag", "deleteflag");                // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SubSrYuudentaiSyugenryou>> beanHandler = new BeanListHandler<>(SubSrYuudentaiSyugenryou.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [誘電体ｽﾗﾘｰ作製・主原料秤量入力_ｻﾌﾞ画面_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrYuudentaiSyugenryou> loadTmpSubSrYuudentaiSyugenryou(QueryRunner queryRunnerQcdb,
            String kojyo, String lotNo, String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + " kojyo,lotno,edaban,zairyokubun,tyogouryoukikaku,tyogouzanryou,zairyohinmei,buzailotno,buzaihinmei,"
                + "tyougouryou1_1,tyougouryou1_2,tyougouryou2_1,tyougouryou2_2,tyougouryou3_1,tyougouryou3_2,tyougouryou4_1,"
                + "tyougouryou4_2,tyougouryou5_1,tyougouryou5_2,tyougouryou6_1,tyougouryou6_2,tyougouryou7_1,"
                + "tyougouryou7_2,torokunichiji,kosinnichiji,revision,deleteflag "
                + " FROM tmp_sub_sr_yuudentai_syugenryou "
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
        mapping.put("kojyo", "kojyo");                          // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                          // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                        // 枝番
        mapping.put("zairyokubun", "zairyokubun");              // 材料区分
        mapping.put("tyogouryoukikaku", "tyogouryoukikaku");    // 調合規格
        mapping.put("tyogouzanryou", "tyogouzanryou");          // 調合残量
        mapping.put("zairyohinmei", "zairyohinmei");            // 材料品名
        mapping.put("buzailotno", "buzailotno");                // 部材在庫No
        mapping.put("buzaihinmei", "buzaihinmei");              // 部材在庫品名
        mapping.put("tyougouryou1_1", "tyougouryou1_1");        // 調合量1_1
        mapping.put("tyougouryou1_2", "tyougouryou1_2");        // 調合量1_2
        mapping.put("tyougouryou2_1", "tyougouryou2_1");        // 調合量2_1
        mapping.put("tyougouryou2_2", "tyougouryou2_2");        // 調合量2_2
        mapping.put("tyougouryou3_1", "tyougouryou3_1");        // 調合量3_1
        mapping.put("tyougouryou3_2", "tyougouryou3_2");        // 調合量3_2
        mapping.put("tyougouryou4_1", "tyougouryou4_1");        // 調合量4_1
        mapping.put("tyougouryou4_2", "tyougouryou4_2");        // 調合量4_2
        mapping.put("tyougouryou5_1", "tyougouryou5_1");        // 調合量5_1
        mapping.put("tyougouryou5_2", "tyougouryou5_2");        // 調合量5_2
        mapping.put("tyougouryou6_1", "tyougouryou6_1");        // 調合量6_1
        mapping.put("tyougouryou6_2", "tyougouryou6_2");        // 調合量6_2
        mapping.put("tyougouryou7_1", "tyougouryou7_1");        // 調合量7_1
        mapping.put("tyougouryou7_2", "tyougouryou7_2");        // 調合量7_2
        mapping.put("torokunichiji", "torokunichiji");          // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");            // 更新日時
        mapping.put("revision", "revision");                    // revision
        mapping.put("deleteflag", "deleteflag");                // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SubSrYuudentaiSyugenryou>> beanHandler = new BeanListHandler<>(SubSrYuudentaiSyugenryou.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・主原料秤量入力_サブ画面_仮登録(tmp_sub_sr_yuudentai_syugenryou)登録処理
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
    private void insertTmpSubSrYuudentaiSyugenryou(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal newRev, int deleteflag, String kojyo, String lotNo, String edaban, Integer zairyokubun,
            String systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sub_sr_yuudentai_syugenryou ( "
                + " kojyo,lotno,edaban,zairyokubun,tyogouryoukikaku,tyogouzanryou,zairyohinmei,buzailotno,buzaihinmei,"
                + "tyougouryou1_1,tyougouryou1_2,tyougouryou2_1,tyougouryou2_2,tyougouryou3_1,tyougouryou3_2,tyougouryou4_1,"
                + "tyougouryou4_2,tyougouryou5_1,tyougouryou5_2,tyougouryou6_1,tyougouryou6_2,tyougouryou7_1,"
                + "tyougouryou7_2,torokunichiji,kosinnichiji,revision,deleteflag "
                + " ) VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";

        List<Object> params = setUpdateParameterTmpSubSrYuudentaiSyugenryou(true, newRev, deleteflag, kojyo, lotNo, edaban, zairyokubun, systemTime);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・主原料秤量入力_仮登録(tmp_sub_sr_yuudentai_syugenryou)更新処理
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
    private void updateTmpSubSrYuudentaiSyugenryou(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal rev, BigDecimal newRev, String kojyo, String lotNo,
            String edaban, Integer zairyokubun, String systemTime) throws SQLException {

        String sql = "UPDATE tmp_sub_sr_yuudentai_syugenryou SET "
                + " tyogouryoukikaku = ?,tyogouzanryou = ?,zairyohinmei = ?,buzailotno = ?,buzaihinmei = ?,"
                + "tyougouryou1_1 = ?,tyougouryou1_2 = ?,tyougouryou2_1 = ?,tyougouryou2_2 = ?,tyougouryou3_1 = ?,"
                + "tyougouryou3_2 = ?,tyougouryou4_1 = ?,tyougouryou4_2 = ?,tyougouryou5_1 = ?,tyougouryou5_2 = ?,"
                + "tyougouryou6_1 = ?,tyougouryou6_2 = ?,tyougouryou7_1 = ?,tyougouryou7_2 = ?,"
                + "kosinnichiji = ?,revision = ?, deleteflag = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND zairyokubun = ? AND revision = ? ";

        List<Object> params = setUpdateParameterTmpSubSrYuudentaiSyugenryou(false, newRev, 0, kojyo, lotNo, edaban, zairyokubun, systemTime);

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
     * 誘電体ｽﾗﾘｰ作製・主原料秤量入力_サブ画面仮登録(tmp_sub_sr_yuudentai_syugenryou)更新値パラメータ設定
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
    private List<Object> setUpdateParameterTmpSubSrYuudentaiSyugenryou(boolean isInsert, BigDecimal newRev,
            int deleteflag, String kojyo, String lotNo, String edaban, Integer zairyokubun, String systemTime) {

        List<Object> params = new ArrayList<>();

        // 子画面情報を取得
        GXHDO102C011 beanGXHDO102C011 = (GXHDO102C011) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C011);
        GXHDO102C011Model gxhdO102c011Model = beanGXHDO102C011.getGxhdO102c011Model();

        // 誘電体ｽﾗﾘｰ作製・主原料秤量入力_サブ画面から更新値を取得
        ArrayList<Object> subGamenDataList = getSubGamenData(gxhdO102c011Model, zairyokubun);
        // 調合量規格
        FXHDD01 tyogouryoukikaku = (FXHDD01) subGamenDataList.get(0);
        // 調合残量
        FXHDD01 tyogouzanryou = (FXHDD01) subGamenDataList.get(1);
        // 部材①
        List<FXHDD01> buzaitab1DataList = (List<FXHDD01>) subGamenDataList.get(2);

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
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(5).getValue())); // 調合量2_1
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(6).getValue())); // 調合量2_2
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(7).getValue())); // 調合量3_1
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(8).getValue())); // 調合量3_2
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(9).getValue())); // 調合量4_1
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(10).getValue())); // 調合量4_2
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(11).getValue())); // 調合量5_1
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(12).getValue())); // 調合量5_2
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(13).getValue())); // 調合量6_1
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(14).getValue())); // 調合量6_2
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(15).getValue())); // 調合量7_1
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(16).getValue())); // 調合量7_2
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
     * 誘電体ｽﾗﾘｰ作製・主原料秤量入力_サブ画面仮登録(tmp_sub_sr_yuudentai_syugenryou)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSubSrYuudentaiSyugenryou(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal rev, String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sub_sr_yuudentai_syugenryou "
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
     * 誘電体ｽﾗﾘｰ作製・主原料秤量入力_サブ画面(sub_sr_yuudentai_syugenryou)登録処理
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
    private void insertSubSrYuudentaiSyugenryou(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal newRev, String kojyo, String lotNo, String edaban,
            Integer zairyokubun, String systemTime) throws SQLException {
        String sql = "INSERT INTO sub_sr_yuudentai_syugenryou ( "
                + " kojyo,lotno,edaban,zairyokubun,tyogouryoukikaku,tyogouzanryou,zairyohinmei,buzailotno,buzaihinmei,"
                + "tyougouryou1_1,tyougouryou1_2,tyougouryou2_1,tyougouryou2_2,tyougouryou3_1,tyougouryou3_2,tyougouryou4_1,"
                + "tyougouryou4_2,tyougouryou5_1,tyougouryou5_2,tyougouryou6_1,tyougouryou6_2,tyougouryou7_1,"
                + "tyougouryou7_2,torokunichiji,kosinnichiji,revision "
                + " ) VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";

        List<Object> params = setUpdateParameterSubSrYuudentaiSyugenryou(true, newRev, kojyo, lotNo, edaban, zairyokubun, systemTime);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・主原料秤量入力_ｻﾌﾞ画面(sub_sr_yuudentai_syugenryou)更新処理
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
    private void updateSubSrYuudentaiSyugenryou(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal rev, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            Integer zairyokubun, String systemTime) throws SQLException {

        String sql = "UPDATE sub_sr_yuudentai_syugenryou SET "
                + " tyogouryoukikaku = ?,tyogouzanryou = ?,zairyohinmei = ?,buzailotno = ?,buzaihinmei = ?,"
                + "tyougouryou1_1 = ?,tyougouryou1_2 = ?,tyougouryou2_1 = ?,tyougouryou2_2 = ?,tyougouryou3_1 = ?,"
                + "tyougouryou3_2 = ?,tyougouryou4_1 = ?,tyougouryou4_2 = ?,tyougouryou5_1 = ?,tyougouryou5_2 = ?,"
                + "tyougouryou6_1 = ?,tyougouryou6_2 = ?,tyougouryou7_1 = ?,tyougouryou7_2 = ?,"
                + "kosinnichiji = ?,revision = ?"
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND zairyokubun = ? AND revision = ? ";

        List<Object> params = setUpdateParameterSubSrYuudentaiSyugenryou(false, newRev, kojyo, lotNo, edaban, zairyokubun, systemTime);

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
     * 誘電体ｽﾗﾘｰ作製・主原料秤量入力_サブ画面から更新値を取得
     *
     * @param gxhdO102c011Model モデルデータ
     * @param zairyokubun 材料区分
     * @return 更新値情報
     */
    private ArrayList<Object> getSubGamenData(GXHDO102C011Model gxhdO102c011Model, Integer zairyokubun) {
        GXHDO102C011Model.SubGamenData c011subgamendata = GXHDO102C011Logic.getC011subgamendata(gxhdO102c011Model, zairyokubun);
        ArrayList<Object> returnList = new ArrayList<>();
        // 調合量規格
        FXHDD01 tyogouryoukikaku = c011subgamendata.getSubDataTyogouryoukikaku();
        // 調合残量
        FXHDD01 tyogouzanryou = c011subgamendata.getSubDataTyogouzanryou();
        // 部材①
        List<FXHDD01> buzaitab1DataList = c011subgamendata.getSubDataBuzaitab1();
        returnList.add(tyogouryoukikaku);
        returnList.add(tyogouzanryou);
        returnList.add(buzaitab1DataList);
        return returnList;
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・主原料秤量入力_サブ画面登録(tmp_sub_sr_yuudentai_syugenryou)更新値パラメータ設定
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
    private List<Object> setUpdateParameterSubSrYuudentaiSyugenryou(boolean isInsert, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Integer zairyokubun, String systemTime) {

        List<Object> params = new ArrayList<>();

        // 子画面情報を取得
        GXHDO102C011 beanGXHDO102C011 = (GXHDO102C011) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C011);
        GXHDO102C011Model gxhdO102c011Model = beanGXHDO102C011.getGxhdO102c011Model();
        // 誘電体ｽﾗﾘｰ作製・主原料秤量入力_サブ画面から更新値を取得
        ArrayList<Object> subGamenDataList = getSubGamenData(gxhdO102c011Model, zairyokubun);
        // 調合量規格
        FXHDD01 tyogouryoukikaku = (FXHDD01) subGamenDataList.get(0);
        // 調合残量
        FXHDD01 tyogouzanryou = (FXHDD01) subGamenDataList.get(1);
        // 部材①
        List<FXHDD01> buzaitab1DataList = (List<FXHDD01>) subGamenDataList.get(2);

        if (isInsert) {
            params.add(kojyo); // 工場ｺｰﾄﾞ
            params.add(lotNo); // ﾛｯﾄNo
            params.add(edaban); // 枝番
            params.add(zairyokubun); // 材料区分
        }

        params.add(DBUtil.stringToStringObject(tyogouryoukikaku.getValue())); // 調合量規格
        params.add(DBUtil.stringToIntObject(tyogouzanryou.getValue())); // 調合残量
        params.add(DBUtil.stringToStringObject(buzaitab1DataList.get(0).getValue())); // 材料品名
        params.add(DBUtil.stringToStringObject(buzaitab1DataList.get(1).getValue())); // 部材在庫No
        params.add(DBUtil.stringToStringObject(buzaitab1DataList.get(2).getValue())); // 部材在庫品名
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(3).getValue())); // 調合量1_1
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(4).getValue())); // 調合量1_2
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(5).getValue())); // 調合量2_1
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(6).getValue())); // 調合量2_2
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(7).getValue())); // 調合量3_1
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(8).getValue())); // 調合量3_2
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(9).getValue())); // 調合量4_1
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(10).getValue())); // 調合量4_2
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(11).getValue())); // 調合量5_1
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(12).getValue())); // 調合量5_2
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(13).getValue())); // 調合量6_1
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(14).getValue())); // 調合量6_2
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(15).getValue())); // 調合量7_1
        params.add(DBUtil.stringToIntObjectDefaultNull(buzaitab1DataList.get(16).getValue())); // 調合量7_2

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
     * 誘電体ｽﾗﾘｰ作製・主原料秤量入力_サブ画面仮登録(tmp_sub_sr_yuudentai_syugenryou)登録処理(削除時)
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
    private void insertDeleteDataTmpSubSrYuudentaiSyugenryou(QueryRunner queryRunnerQcdb,
            Connection conQcdb, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, String systemTime) throws SQLException {
        String sql = "INSERT INTO tmp_sub_sr_yuudentai_syugenryou( "
                + " kojyo,lotno,edaban,zairyokubun,tyogouryoukikaku,tyogouzanryou,zairyohinmei,buzailotno,buzaihinmei,"
                + "tyougouryou1_1,tyougouryou1_2,tyougouryou2_1,tyougouryou2_2,tyougouryou3_1,tyougouryou3_2,tyougouryou4_1,"
                + "tyougouryou4_2,tyougouryou5_1,tyougouryou5_2,tyougouryou6_1,tyougouryou6_2,tyougouryou7_1,"
                + "tyougouryou7_2,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") SELECT "
                + " kojyo,lotno,edaban,zairyokubun,tyogouryoukikaku,tyogouzanryou,zairyohinmei,buzailotno,buzaihinmei,"
                + "tyougouryou1_1,tyougouryou1_2,tyougouryou2_1,tyougouryou2_2,tyougouryou3_1,tyougouryou3_2,tyougouryou4_1,"
                + "tyougouryou4_2,tyougouryou5_1,tyougouryou5_2,tyougouryou6_1,tyougouryou6_2,tyougouryou7_1,"
                + "tyougouryou7_2,?,?,?,? "
                + " FROM sub_sr_yuudentai_syugenryou "
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
     * 誘電体ｽﾗﾘｰ作製・主原料秤量入力_サブ画面仮登録(sub_sr_yuudentai_syugenryou)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSubSrYuudentaiSyugenryou(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal rev, String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sub_sr_yuudentai_syugenryou "
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
        // 主原料1_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B022Const.SGR1_BUZAIZAIKOLOTNO, 1, errorItemList);
        // 主原料2_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B022Const.SGR2_BUZAIZAIKOLOTNO, 2, errorItemList);
        // 主原料3_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B022Const.SGR3_BUZAIZAIKOLOTNO, 3, errorItemList);
        // 主原料4_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B022Const.SGR4_BUZAIZAIKOLOTNO, 4, errorItemList);
        // 主原料5_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B022Const.SGR5_BUZAIZAIKOLOTNO, 5, errorItemList);
        // 主原料6_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B022Const.SGR6_BUZAIZAIKOLOTNO, 6, errorItemList);

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
     * 調合量X_Yリストの合計値を取得
     *
     * @param processData 処理制御データ
     * @param sgrNo 主原料No
     * @return レスポンスデータ
     */
    private String getSumTyogouzanryouVal(ProcessData processData, int sgrNo) {
        List<String> tyougouryouList = null;
        switch (sgrNo) {
            case 1:
                tyougouryouList = Arrays.asList(GXHDO102B022Const.SGR1_TYOUGOURYOU1, GXHDO102B022Const.SGR1_TYOUGOURYOU2,
                        GXHDO102B022Const.SGR1_TYOUGOURYOU3, GXHDO102B022Const.SGR1_TYOUGOURYOU4, GXHDO102B022Const.SGR1_TYOUGOURYOU5,
                        GXHDO102B022Const.SGR1_TYOUGOURYOU6, GXHDO102B022Const.SGR1_TYOUGOURYOU7);
                break;
            case 2:
                tyougouryouList = Arrays.asList(GXHDO102B022Const.SGR2_TYOUGOURYOU1, GXHDO102B022Const.SGR2_TYOUGOURYOU2,
                        GXHDO102B022Const.SGR2_TYOUGOURYOU3);
                break;
            case 3:
                tyougouryouList = Arrays.asList(GXHDO102B022Const.SGR3_TYOUGOURYOU1, GXHDO102B022Const.SGR3_TYOUGOURYOU2,
                        GXHDO102B022Const.SGR3_TYOUGOURYOU3);
                break;
            case 4:
                tyougouryouList = Arrays.asList(GXHDO102B022Const.SGR4_TYOUGOURYOU1, GXHDO102B022Const.SGR4_TYOUGOURYOU2);
                break;
            case 5:
                tyougouryouList = Arrays.asList(GXHDO102B022Const.SGR5_TYOUGOURYOU1, GXHDO102B022Const.SGR5_TYOUGOURYOU2);
                break;
            case 6:
                tyougouryouList = Arrays.asList(GXHDO102B022Const.SGR6_TYOUGOURYOU1);
                break;
            default:
                tyougouryouList = Arrays.asList(GXHDO102B022Const.SGR1_TYOUGOURYOU1, GXHDO102B022Const.SGR1_TYOUGOURYOU2,
                        GXHDO102B022Const.SGR1_TYOUGOURYOU3, GXHDO102B022Const.SGR1_TYOUGOURYOU4, GXHDO102B022Const.SGR1_TYOUGOURYOU5,
                        GXHDO102B022Const.SGR1_TYOUGOURYOU6, GXHDO102B022Const.SGR1_TYOUGOURYOU7, GXHDO102B022Const.SGR2_TYOUGOURYOU1,
                        GXHDO102B022Const.SGR2_TYOUGOURYOU2, GXHDO102B022Const.SGR2_TYOUGOURYOU3, GXHDO102B022Const.SGR3_TYOUGOURYOU1,
                        GXHDO102B022Const.SGR3_TYOUGOURYOU2, GXHDO102B022Const.SGR3_TYOUGOURYOU3, GXHDO102B022Const.SGR4_TYOUGOURYOU1,
                        GXHDO102B022Const.SGR4_TYOUGOURYOU2, GXHDO102B022Const.SGR5_TYOUGOURYOU1, GXHDO102B022Const.SGR5_TYOUGOURYOU2,
                        GXHDO102B022Const.SGR6_TYOUGOURYOU1);
        }
        String sumTyogouzanryouStrVal = "";
        // 調合量X_Yリスト
        ArrayList<String> tyougouryouValueList = new ArrayList<>();
        tyougouryouList.stream().map((tyougouryouItemId) -> getItemRow(processData.getItemList(), tyougouryouItemId)).filter(
                (itemFxhdd01Tyougouryou) -> (itemFxhdd01Tyougouryou != null)).forEachOrdered((itemFxhdd01Tyougouryou) -> {
                    tyougouryouValueList.add(StringUtil.nullToBlank(itemFxhdd01Tyougouryou.getValue()));
                });
        BigDecimal sumTyogouzanryou = GXHDO102C011Logic.sumTyogouzanryou(tyougouryouValueList);
        if (sumTyogouzanryou != null) {
            sumTyogouzanryouStrVal = sumTyogouzanryou.toPlainString();
        }
        return sumTyogouzanryouStrVal;
    }

    /**
     * 部材在庫管理を参照【PMLA0212_部材在庫ﾃﾞｰﾀ更新】
     *
     * @param processData 処理制御データ
     * @param tantoshaCd 更新者
     * @param sizailotnoStr 部材在庫No
     * @param sgrNo 主原料No
     * @param errorItemList エラー情報リスト
     * @return レスポンスデータ
     */
    private void doCallPmla0212Api(ProcessData processData, String tantoshaCd, String sizailotnoStr, int sgrNo, ArrayList<String> errorItemList) {
        // WIPﾛｯﾄNo
        String wiplotnoValue = "";
        // 部材在庫NoX_Yに値が入っている場合、以下の内容を元にAPIを呼び出す
        FXHDD01 itemFxhdd01Sizailotno = getItemRow(processData.getItemList(), sizailotnoStr);
        if (itemFxhdd01Sizailotno == null || StringUtil.isEmpty(itemFxhdd01Sizailotno.getValue())) {
            return;
        }
        // 部材在庫NoX_Y
        String sizailotnoValue = StringUtil.nullToBlank(itemFxhdd01Sizailotno.getValue());
        String sumTyogouzanryouStrVal = getSumTyogouzanryouVal(processData, sgrNo);
        FXHDD01 itemFxhdd01Wiplotno = getItemRow(processData.getItemList(), GXHDO102B022Const.WIPLOTNO);
        if (itemFxhdd01Wiplotno != null) {
            // WIPﾛｯﾄNo
            wiplotnoValue = StringUtil.nullToBlank(itemFxhdd01Wiplotno.getValue());
        }
        ArrayList<String> paramsList = new ArrayList<>();
        paramsList.add(sizailotnoValue);
        paramsList.add(tantoshaCd);
        paramsList.add("PXHDO102");
        paramsList.add(sumTyogouzanryouStrVal);
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
        allItemIdMap.put(GXHDO102B022Const.WIPLOTNO, "WIPﾛｯﾄNo");
        allItemIdMap.put(GXHDO102B022Const.YUUDENTAISLURRYHINMEI, "誘電体ｽﾗﾘｰ品名");
        allItemIdMap.put(GXHDO102B022Const.YUUDENTAISLURRYLOTNO, "誘電体ｽﾗﾘｰLotNo");
        allItemIdMap.put(GXHDO102B022Const.LOTKUBUN, "ﾛｯﾄ区分");
        allItemIdMap.put(GXHDO102B022Const.GENRYOULOTNO, "原料LotNo");
        allItemIdMap.put(GXHDO102B022Const.GENRYOUKIGOU, "原料記号");
        allItemIdMap.put(GXHDO102B022Const.GOKI, "秤量号機");
        allItemIdMap.put(GXHDO102B022Const.SGRHYOURYOUKAISI_DAY, "主原料秤量開始日");
        allItemIdMap.put(GXHDO102B022Const.SGRHYOURYOUKAISI_TIME, "主原料秤量開始時間");
        allItemIdMap.put(GXHDO102B022Const.SGR1_ZAIRYOUHINMEI, "主原料①_材料品名");
        allItemIdMap.put(GXHDO102B022Const.SGR1_TYOUGOURYOUKIKAKU, "主原料①_調合量規格");
        allItemIdMap.put(GXHDO102B022Const.SGR1_BUZAIZAIKOLOTNO, "主原料①_部材在庫No");
        allItemIdMap.put(GXHDO102B022Const.SGR1_TYOUGOURYOU1, "主原料①_調合量1");
        allItemIdMap.put(GXHDO102B022Const.SGR1_TYOUGOURYOU2, "主原料①_調合量2");
        allItemIdMap.put(GXHDO102B022Const.SGR1_TYOUGOURYOU3, "主原料①_調合量3");
        allItemIdMap.put(GXHDO102B022Const.SGR1_TYOUGOURYOU4, "主原料①_調合量4");
        allItemIdMap.put(GXHDO102B022Const.SGR1_TYOUGOURYOU5, "主原料①_調合量5");
        allItemIdMap.put(GXHDO102B022Const.SGR1_TYOUGOURYOU6, "主原料①_調合量6");
        allItemIdMap.put(GXHDO102B022Const.SGR1_TYOUGOURYOU7, "主原料①_調合量7");
        allItemIdMap.put(GXHDO102B022Const.SGR2_ZAIRYOUHINMEI, "主原料②_材料品名");
        allItemIdMap.put(GXHDO102B022Const.SGR2_TYOUGOURYOUKIKAKU, "主原料②_調合量規格");
        allItemIdMap.put(GXHDO102B022Const.SGR2_BUZAIZAIKOLOTNO, "主原料②_部材在庫No");
        allItemIdMap.put(GXHDO102B022Const.SGR2_TYOUGOURYOU1, "主原料②_調合量1");
        allItemIdMap.put(GXHDO102B022Const.SGR2_TYOUGOURYOU2, "主原料②_調合量2");
        allItemIdMap.put(GXHDO102B022Const.SGR2_TYOUGOURYOU3, "主原料②_調合量3");
        allItemIdMap.put(GXHDO102B022Const.SGR3_ZAIRYOUHINMEI, "主原料③_材料品名");
        allItemIdMap.put(GXHDO102B022Const.SGR3_TYOUGOURYOUKIKAKU, "主原料③_調合量規格");
        allItemIdMap.put(GXHDO102B022Const.SGR3_BUZAIZAIKOLOTNO, "主原料③_部材在庫No");
        allItemIdMap.put(GXHDO102B022Const.SGR3_TYOUGOURYOU1, "主原料③_調合量1");
        allItemIdMap.put(GXHDO102B022Const.SGR3_TYOUGOURYOU2, "主原料③_調合量2");
        allItemIdMap.put(GXHDO102B022Const.SGR3_TYOUGOURYOU3, "主原料③_調合量3");
        allItemIdMap.put(GXHDO102B022Const.SGR4_ZAIRYOUHINMEI, "主原料④_材料品名");
        allItemIdMap.put(GXHDO102B022Const.SGR4_TYOUGOURYOUKIKAKU, "主原料④_調合量規格");
        allItemIdMap.put(GXHDO102B022Const.SGR4_BUZAIZAIKOLOTNO, "主原料④_部材在庫No");
        allItemIdMap.put(GXHDO102B022Const.SGR4_TYOUGOURYOU1, "主原料④_調合量1");
        allItemIdMap.put(GXHDO102B022Const.SGR4_TYOUGOURYOU2, "主原料④_調合量2");
        allItemIdMap.put(GXHDO102B022Const.SGR5_ZAIRYOUHINMEI, "主原料⑤_材料品名");
        allItemIdMap.put(GXHDO102B022Const.SGR5_TYOUGOURYOUKIKAKU, "主原料⑤_調合量規格");
        allItemIdMap.put(GXHDO102B022Const.SGR5_BUZAIZAIKOLOTNO, "主原料⑤_部材在庫No");
        allItemIdMap.put(GXHDO102B022Const.SGR5_TYOUGOURYOU1, "主原料⑤_調合量1");
        allItemIdMap.put(GXHDO102B022Const.SGR5_TYOUGOURYOU2, "主原料⑤_調合量2");
        allItemIdMap.put(GXHDO102B022Const.SGR6_ZAIRYOUHINMEI, "主原料⑥_材料品名");
        allItemIdMap.put(GXHDO102B022Const.SGR6_TYOUGOURYOUKIKAKU, "主原料⑥_調合量規格");
        allItemIdMap.put(GXHDO102B022Const.SGR6_BUZAIZAIKOLOTNO, "主原料⑥_部材在庫No");
        allItemIdMap.put(GXHDO102B022Const.SGR6_TYOUGOURYOU1, "主原料⑥_調合量1");
        allItemIdMap.put(GXHDO102B022Const.TYOUGOURYOUGOUKEI, "調合量合計");
        allItemIdMap.put(GXHDO102B022Const.SGRHYOURYOUSYUURYOU_DAY, "主原料秤量終了日");
        allItemIdMap.put(GXHDO102B022Const.SGRHYOURYOUSYUURYOU_TIME, "主原料秤量終了時間");
        allItemIdMap.put(GXHDO102B022Const.KOKEIBUNSOKUTEITANTOUSYA, "固形分測定担当者");
        allItemIdMap.put(GXHDO102B022Const.BIKOU1, "備考1");
        allItemIdMap.put(GXHDO102B022Const.BIKOU2, "備考2");

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
        GXHDO102B022A bean = (GXHDO102B022A) getFormBean("gXHDO102B022A");
        notShowItemList.forEach((notShowItem) -> {
            itemList.remove(getItemRow(itemList, notShowItem));
        });

        bean.setSgr1_tyougouryou2(null);
        bean.setSgr1_tyougouryou3(null);
        bean.setSgr1_tyougouryou4(null);
        bean.setSgr1_tyougouryou5(null);
        bean.setSgr1_tyougouryou6(null);
        bean.setSgr1_tyougouryou7(null);
        bean.setTyougouryougoukei(null);
    }

    /**
     * 項目の表示制御
     *
     * @param processData 処理制御データ
     * @param notShowItemList 画面非表示項目リスト
     * @param sgrNum 主原料no
     */
    private void removeHinmeiItemsFromItemList(ProcessData processData, List<String> notShowItemList, int sgrNum) {

        List<FXHDD01> itemList = processData.getItemList();
        GXHDO102B022A bean = (GXHDO102B022A) getFormBean("gXHDO102B022A");
        notShowItemList.forEach((notShowItem) -> {
            itemList.remove(getItemRow(itemList, notShowItem));
        });

        switch (sgrNum) {
            // 「主原料2_材料品名」が取得できなかった場合
            case 2:
                bean.setSgr2_zairyouhinmei(null);
                bean.setSgr2_tyougouryoukikaku(null);
                bean.setSgr2_buzaizaikolotno(null);
                bean.setSgr2_tyougouryou1(null);
                bean.setSgr2_tyougouryou2(null);
                bean.setSgr2_tyougouryou3(null);
                break;
            // 「主原料3_材料品名」が取得できなかった場合
            case 3:
                bean.setSgr3_zairyouhinmei(null);
                bean.setSgr3_tyougouryoukikaku(null);
                bean.setSgr3_buzaizaikolotno(null);
                bean.setSgr3_tyougouryou1(null);
                bean.setSgr3_tyougouryou2(null);
                bean.setSgr3_tyougouryou3(null);
                break;
            // 「主原料4_材料品名」が取得できなかった場合
            case 4:
                bean.setSgr4_zairyouhinmei(null);
                bean.setSgr4_tyougouryoukikaku(null);
                bean.setSgr4_buzaizaikolotno(null);
                bean.setSgr4_tyougouryou1(null);
                bean.setSgr4_tyougouryou2(null);
                break;
            // 「主原料5_材料品名」が取得できなかった場合
            case 5:
                bean.setSgr5_zairyouhinmei(null);
                bean.setSgr5_tyougouryoukikaku(null);
                bean.setSgr5_buzaizaikolotno(null);
                bean.setSgr5_tyougouryou1(null);
                bean.setSgr5_tyougouryou2(null);
                break;
            // 「主原料6_材料品名」が取得できなかった場合
            case 6:
                bean.setSgr6_zairyouhinmei(null);
                bean.setSgr6_tyougouryoukikaku(null);
                bean.setSgr6_buzaizaikolotno(null);
                bean.setSgr6_tyougouryou1(null);
                break;
        }
    }

    /**
     * 項目の表示制御
     *
     * @param processData 処理制御データ
     * @param sgrNum 主原料no
     */
    private void doHinmeiItemsRendered(ProcessData processData, int sgrNum) {
        List<String> notShowYouzaiItemList = null;
        String itemId = "";
        switch (sgrNum) {
            // 「主原料2_材料品名」が取得できなかった場合
            case 2:
                notShowYouzaiItemList = Arrays.asList(GXHDO102B022Const.SGR2_ZAIRYOUHINMEI, GXHDO102B022Const.SGR2_TYOUGOURYOUKIKAKU, GXHDO102B022Const.SGR2_BUZAIZAIKOLOTNO,
                        GXHDO102B022Const.SGR2_TYOUGOURYOU1, GXHDO102B022Const.SGR2_TYOUGOURYOU2, GXHDO102B022Const.SGR2_TYOUGOURYOU3);
                itemId = GXHDO102B022Const.SGR2_ZAIRYOUHINMEI;
                break;
            // 「主原料3_材料品名」が取得できなかった場合
            case 3:
                notShowYouzaiItemList = Arrays.asList(GXHDO102B022Const.SGR3_ZAIRYOUHINMEI, GXHDO102B022Const.SGR3_TYOUGOURYOUKIKAKU, GXHDO102B022Const.SGR3_BUZAIZAIKOLOTNO,
                        GXHDO102B022Const.SGR3_TYOUGOURYOU1, GXHDO102B022Const.SGR3_TYOUGOURYOU2, GXHDO102B022Const.SGR3_TYOUGOURYOU3);
                itemId = GXHDO102B022Const.SGR3_ZAIRYOUHINMEI;
                break;
            // 「主原料4_材料品名」が取得できなかった場合
            case 4:
                notShowYouzaiItemList = Arrays.asList(GXHDO102B022Const.SGR4_ZAIRYOUHINMEI, GXHDO102B022Const.SGR4_TYOUGOURYOUKIKAKU, GXHDO102B022Const.SGR4_BUZAIZAIKOLOTNO,
                        GXHDO102B022Const.SGR4_TYOUGOURYOU1, GXHDO102B022Const.SGR4_TYOUGOURYOU2);
                itemId = GXHDO102B022Const.SGR4_ZAIRYOUHINMEI;
                break; 
            // 「主原料5_材料品名」が取得できなかった場合
            case 5:
                notShowYouzaiItemList = Arrays.asList(GXHDO102B022Const.SGR5_ZAIRYOUHINMEI, GXHDO102B022Const.SGR5_TYOUGOURYOUKIKAKU, GXHDO102B022Const.SGR5_BUZAIZAIKOLOTNO,
                        GXHDO102B022Const.SGR5_TYOUGOURYOU1, GXHDO102B022Const.SGR5_TYOUGOURYOU2);
                itemId = GXHDO102B022Const.SGR5_ZAIRYOUHINMEI;
                break;
            // 「主原料6_材料品名」が取得できなかった場合
            case 6:
                notShowYouzaiItemList = Arrays.asList(GXHDO102B022Const.SGR6_ZAIRYOUHINMEI, GXHDO102B022Const.SGR6_TYOUGOURYOUKIKAKU, GXHDO102B022Const.SGR6_BUZAIZAIKOLOTNO,
                        GXHDO102B022Const.SGR6_TYOUGOURYOU1);
                itemId = GXHDO102B022Const.SGR6_ZAIRYOUHINMEI;
                break;
        }

        if (StringUtil.isEmpty(itemId) || notShowYouzaiItemList == null) {
            return;
        }
        FXHDD01 itemYouzai_zairyouhinmei = getItemRow(processData.getItemList(), itemId);
        if (itemYouzai_zairyouhinmei == null) {
            removeHinmeiItemsFromItemList(processData, notShowYouzaiItemList, sgrNum);
            return;
        }
        String youzai_zairyouhinmeiVal = getItemKikakuchi(processData.getItemList(), itemId, null);
        if (StringUtil.isEmpty(youzai_zairyouhinmeiVal)) {
            removeHinmeiItemsFromItemList(processData, notShowYouzaiItemList, sgrNum);
        }
    }

    /**
     * 品名毎の項目の表示制御
     *
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void setHinmeiItemRendered(ProcessData processData) throws SQLException {
        // 「主原料2_材料品名」が取得できなかった場合、項目を非表示にする。
        doHinmeiItemsRendered(processData, 2);
        // 「主原料3_材料品名」が取得できなかった場合、項目を非表示にする。
        doHinmeiItemsRendered(processData, 3);
        // 「主原料4_材料品名」が取得できなかった場合、項目を非表示にする。
        doHinmeiItemsRendered(processData, 4);
        // 「主原料5_材料品名」が取得できなかった場合、項目を非表示にする。
        doHinmeiItemsRendered(processData, 5);
        // 「主原料6_材料品名」が取得できなかった場合、項目を非表示にする。
        doHinmeiItemsRendered(processData, 6);
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
        Map fxhbm03Data = loadFxhbm03Data(queryRunnerDoc, "誘電体ｽﾗﾘｰ作製_主原料秤量_表示制御");
        // 画面非表示項目リスト: 主原料①_調合量2、主原料①_調合量3、主原料①_調合量4、主原料①_調合量5、主原料①_調合量6、主原料①_調合量7、調合量合計
        List<String> notShowItemList = Arrays.asList(GXHDO102B022Const.SGR1_TYOUGOURYOU2, GXHDO102B022Const.SGR1_TYOUGOURYOU3, GXHDO102B022Const.SGR1_TYOUGOURYOU4,
                GXHDO102B022Const.SGR1_TYOUGOURYOU5, GXHDO102B022Const.SGR1_TYOUGOURYOU6, GXHDO102B022Const.SGR1_TYOUGOURYOU7, GXHDO102B022Const.TYOUGOURYOUGOUKEI);
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
