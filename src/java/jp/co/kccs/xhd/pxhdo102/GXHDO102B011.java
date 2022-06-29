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
import jp.co.kccs.xhd.db.model.SrTenkaFunsai;
import jp.co.kccs.xhd.db.model.SubSrTenkaFunsaiHyoryo;
import jp.co.kccs.xhd.jaxrs.TCPClient;
import jp.co.kccs.xhd.model.GXHDO102C006Model;
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
 * 変更日	2021/10/18<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * 変更日	2022/05/16<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	材料品名ﾘﾝｸ押下時、調合量規格チェックの追加<br>
 * <br>
 * 変更日	2022/06/22<br>
 * 計画書No	MB2205-D010<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	PLC書き込みの追加<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B011(添加材ｽﾗﾘｰ作製・粉砕)
 *
 * @author KCSS K.Jo
 * @since 2021/10/18
 */
public class GXHDO102B011 implements IFormLogic {

    private static final Logger LOGGER = Logger.getLogger(GXHDO102B011.class.getName());
    private static final String JOTAI_FLG_KARI_TOROKU = "0";
    private static final String JOTAI_FLG_TOROKUZUMI = "1";
    private static final String JOTAI_FLG_SAKUJO = "9";
    private static final String SQL_STATE_RECORD_LOCK_ERR = "55P03";

    /**
     * コンストラクタ
     */
    public GXHDO102B011() {
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
            initGXHDO102B011A(processData);

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
                    GXHDO102B011Const.BTN_EDABAN_COPY_TOP,
                    GXHDO102B011Const.BTN_KAISINICHIJI_TOP,
                    GXHDO102B011Const.BTN_SYUURYOUNICHIJI_TOP,
                    GXHDO102B011Const.BTN_JYUNKANKAISINICHIJI_TOP,
                    GXHDO102B011Const.BTN_JYUNKANSYUURYOUNICHIJI_TOP,
                    GXHDO102B011Const.BTN_EDABAN_COPY_BOTTOM,
                    GXHDO102B011Const.BTN_KAISINICHIJI_BOTTOM,
                    GXHDO102B011Const.BTN_SYUURYOUNICHIJI_BOTTOM,
                    GXHDO102B011Const.BTN_JYUNKANKAISINICHIJI_BOTTOM,
                    GXHDO102B011Const.BTN_JYUNKANSYUURYOUNICHIJI_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO102B011Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO102B011Const.BTN_INSERT_TOP,
                    GXHDO102B011Const.BTN_DELETE_TOP,
                    GXHDO102B011Const.BTN_UPDATE_TOP,
                    GXHDO102B011Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO102B011Const.BTN_INSERT_BOTTOM,
                    GXHDO102B011Const.BTN_DELETE_BOTTOM,
                    GXHDO102B011Const.BTN_UPDATE_BOTTOM
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
            case GXHDO102B011Const.BTN_EDABAN_COPY_TOP:
            case GXHDO102B011Const.BTN_EDABAN_COPY_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 仮登録
            case GXHDO102B011Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO102B011Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO102B011Const.BTN_INSERT_TOP:
            case GXHDO102B011Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 修正
            case GXHDO102B011Const.BTN_UPDATE_TOP:
            case GXHDO102B011Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO102B011Const.BTN_DELETE_TOP:
            case GXHDO102B011Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 開始日時
            case GXHDO102B011Const.BTN_KAISINICHIJI_TOP:
            case GXHDO102B011Const.BTN_KAISINICHIJI_BOTTOM:
                method = "setKaisinichijiDateTime";
                break;
            // 終了日時
            case GXHDO102B011Const.BTN_SYUURYOUNICHIJI_TOP:
            case GXHDO102B011Const.BTN_SYUURYOUNICHIJI_BOTTOM:
                method = "setSyuuryounichijiDateTime";
                break;
            // 循環開始日時
            case GXHDO102B011Const.BTN_JYUNKANKAISINICHIJI_TOP:
            case GXHDO102B011Const.BTN_JYUNKANKAISINICHIJI_BOTTOM:
                method = "setJyunkankaisinichijiDateTime";
                break;
            // 循環終了日時
            case GXHDO102B011Const.BTN_JYUNKANSYUURYOUNICHIJI_TOP:
            case GXHDO102B011Const.BTN_JYUNKANSYUURYOUNICHIJI_BOTTOM:
                method = "setJyunkansyuuryounichijiDateTime";
                break;
            // 溶剤1_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B011Const.BTN_OPENC006SUBGAMEN1:
                method = "openC006SubGamen1";
                break;
            // 溶剤2_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B011Const.BTN_OPENC006SUBGAMEN2:
                method = "openC006SubGamen2";
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

            // 添加材ｽﾗﾘｰ作製・粉砕の入力項目の登録データ(仮登録時は仮登録データ)を取得
            List<SrTenkaFunsai> srTenkaFunsaiDataList = getSrTenkaFunsaiData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo9, oyalotEdaban);
            if (srTenkaFunsaiDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }
            // 添加材ｽﾗﾘｰ作製・粉砕入力_ｻﾌﾞ画面データ取得
            List<SubSrTenkaFunsaiHyoryo> subSrTenkaFunsaiHyoryoDataList = getSubSrTenkaFunsaiHyoryoData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo9, oyalotEdaban);
            if (subSrTenkaFunsaiHyoryoDataList.isEmpty() || subSrTenkaFunsaiHyoryoDataList.size() != 2) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }
            // メイン画面データ設定
            setInputItemDataMainForm(processData, srTenkaFunsaiDataList.get(0));
            // 添加材ｽﾗﾘｰ作製・粉砕入力_ｻﾌﾞ画面データ設定
            setInputItemDataSubFormC006(processData, subSrTenkaFunsaiHyoryoDataList);

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
        List<String> tyogouryoukikaku = Arrays.asList(GXHDO102B011Const.YOUZAI1_TYOUGOURYOUKIKAKU, GXHDO102B011Const.YOUZAI2_TYOUGOURYOUKIKAKU);
        List<String> youzai1_tyougouryouList = Arrays.asList(GXHDO102B011Const.YOUZAI1_TYOUGOURYOU1, GXHDO102B011Const.YOUZAI1_TYOUGOURYOU2); // 溶剤1_調合量
        List<String> youzai2_tyougouryouList = Arrays.asList(GXHDO102B011Const.YOUZAI2_TYOUGOURYOU1, GXHDO102B011Const.YOUZAI2_TYOUGOURYOU2); // 溶剤2_調合量

        // 規格値の入力値チェック必要の項目リスト
        List<FXHDD01> itemList = new ArrayList<>();
        setKikakuValueAndLabel1(processData, itemList, youzai1_tyougouryouList, tyogouryoukikaku.get(0), "溶剤①_調合量"); // 溶剤1_調合量の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, youzai2_tyougouryouList, tyogouryoukikaku.get(1), "溶剤②_調合量"); // 溶剤2_調合量の規格値と表示ﾗﾍﾞﾙ1を設置

        tyougouryouList.addAll(youzai1_tyougouryouList);
        tyougouryouList.addAll(youzai2_tyougouryouList);

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

                // 添加材ｽﾗﾘｰ作製・粉砕_仮登録処理
                insertTmpSrTenkaFunsai(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, strSystime, processData);
                // 添加材ｽﾗﾘｰ作製・粉砕ﾊﾟｽ_仮登録処理
                insertTmpSubSrTenkaFunsai(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, strSystime, processData);
                // 添加材ｽﾗﾘｰ作製・粉砕ﾊﾟｽ_仮登録処理
                insertTmpSubSrTenkaFunsai2(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, strSystime, processData);
                // 添加材ｽﾗﾘｰ作製・粉砕入力ｻﾌﾞ画面の仮登録処理
                for (int i = 1; i < 3; i++) {
                    insertTmpSubSrTenkaFunsaiHyoryo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, i, strSystime);
                }
            } else {

                String strPasskaisuu_sf1 = getPasskaisuuValue(processData, GXHDO102B011Const.PASSKAISUU_SF1);
                String strPasskaisuu_sf2 = getPasskaisuuValue(processData, GXHDO102B011Const.PASSKAISUU_SF2);
                // 添加材ｽﾗﾘｰ作製・粉砕_仮登録更新処理
                updateTmpSrTenkaFunsai(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);
                // 添加材ｽﾗﾘｰ作製・粉砕ﾊﾟｽ_仮登録更新処理
                updateTmpSubSrTenkaFunsai(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strPasskaisuu_sf1, strSystime, processData);
                // 添加材ｽﾗﾘｰ作製・粉砕ﾊﾟｽ_仮登録更新処理
                updateTmpSubSrTenkaFunsai2(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strPasskaisuu_sf2, strSystime, processData);
                // 添加材ｽﾗﾘｰ作製・粉砕入力ｻﾌﾞ画面の仮登録更新処理
                for (int i = 1; i < 3; i++) {
                    updateTmpSubSrTenkaFunsaiHyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, i, strSystime);
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
            
            // PLCコマンド送信を実行
            sendPlcCommand(processData,queryRunnerDoc);
            
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
     * PLCコマンド送信
     *
     * @param processData 処理制御データ
     * @param queryRunnerDoc
     * @throws java.sql.SQLException
     */
    public void sendPlcCommand(ProcessData processData, QueryRunner queryRunnerDoc) throws SQLException  {

        String serviceIP = "";
        int servicePort;
        String device = "";
        //画面.【粉砕号機】の規格値
        FXHDD01 funsaigouki = getItemRow(processData.getItemList(), GXHDO102B011Const.FUNSAIGOUKI);
        String funsaigoukiKikakuchi = funsaigouki.getKikakuChi().replace("【", "").replace("】", "");

        // [ﾊﾟﾗﾒｰﾀﾏｽﾀ]から、ﾃﾞｰﾀを取得
        Map fxhbm03Data = loadFxhbm03DataForPLC(queryRunnerDoc, funsaigoukiKikakuchi);

        if (fxhbm03Data != null) {
            for (int i = 0; i < 3; i++) {
                // ﾊﾟﾗﾒｰﾀﾃﾞｰﾀ
                String data = StringUtil.nullToBlank(getMapData(fxhbm03Data, "data"));
                String[] dataSplitList = data.split(",");
                //データ例：10.23.44.27,DM1100,8501
                //項目①：IPアドレス
                //項目②：書込DM
                //項目③：ポート番号
                if(dataSplitList.length == 3){
                    serviceIP = dataSplitList[0];
                    device = dataSplitList[1];
                    servicePort = Integer.parseInt(dataSplitList[2]);
                    //上記の条件でPLCに画面「WIPﾛｯﾄNo」の書き込みを行う。
                    //ｱ.書き込みに成功した場合、⑤の処理を行う。
                    //ｲ.書き込みに失敗した場合、3回までリトライ処理を行う。
                    // WIPﾛｯﾄNo
                    FXHDD01 wipLotNo = getItemRow(processData.getItemList(), GXHDO102B011Const.WIPLOTNO);
                    String wipLotNoValue = wipLotNo.getValue();

                    TCPClient client = new TCPClient();
                    String sendCommand = "WRS " + device + ".H" + " 8 ";
                    String receiveMsg = client.executeCommand(serviceIP, servicePort, sendCommand, wipLotNoValue);

                    if (!"".equals(receiveMsg)) {
                        ErrUtil.outputErrorLog("PLCコマンド送信処理結果:" + receiveMsg, null, LOGGER);
                        //ｱ.書き込みに成功した場合、⑤の処理を行う。
                        //ｲ.書き込みに失敗した場合、3回までリトライ処理を行う。
                        if("OK".equals(receiveMsg.toUpperCase())){
                            break;
                        }
                    }
                }
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
    private Map loadFxhbm03DataForPLC(QueryRunner queryRunnerDoc, String key) throws SQLException {
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
        // 開始日時、終了日時前後チェック
        FXHDD01 itemKaisiDay = getItemRow(processData.getItemList(), GXHDO102B011Const.KAISI_SF1_DAY); // 開始日
        FXHDD01 itemKaisiTime = getItemRow(processData.getItemList(), GXHDO102B011Const.KAISI_SF1_TIME); // 開始時間
        Date kaisiDate = DateUtil.convertStringToDate(itemKaisiDay.getValue(), itemKaisiTime.getValue());
        FXHDD01 itemSyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B011Const.SYUURYOU_SF1_DAY); // 終了日
        FXHDD01 itemSyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B011Const.SYUURYOU_SF1_TIME); // 終了時間
        Date syuuryouDate = DateUtil.convertStringToDate(itemSyuuryouDay.getValue(), itemSyuuryouTime.getValue());
        //R001チェック呼出し
        String msgCheckR001 = validateUtil.checkR001("開始日時", kaisiDate, "終了日時", syuuryouDate);
        if (!StringUtil.isEmpty(msgCheckR001)) {
            //エラー発生時
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemKaisiDay, itemKaisiTime, itemSyuuryouDay, itemSyuuryouTime);
            return MessageUtil.getErrorMessageInfo("", msgCheckR001, true, true, errFxhdd01List);
        }

        // 循環開始日時、循環終了日時前後チェック
        itemKaisiDay = getItemRow(processData.getItemList(), GXHDO102B011Const.JYUNKANKAISI_DAY); // 循環開始日
        itemKaisiTime = getItemRow(processData.getItemList(), GXHDO102B011Const.JYUNKANKAISI_TIME); // 循環開始時間
        itemSyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B011Const.JYUNKANSYUURYOU_DAY); // 循環終了日
        itemSyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B011Const.JYUNKANSYUURYOU_TIME); // 循環終了時間
        if (itemKaisiDay != null && itemKaisiTime != null && itemSyuuryouDay != null && itemSyuuryouTime != null) {
            kaisiDate = DateUtil.convertStringToDate(itemKaisiDay.getValue(), itemKaisiTime.getValue());
            syuuryouDate = DateUtil.convertStringToDate(itemSyuuryouDay.getValue(), itemSyuuryouTime.getValue());
            //R001チェック呼出し
            msgCheckR001 = validateUtil.checkR001("循環開始日時", kaisiDate, "循環終了日時", syuuryouDate);
            if (!StringUtil.isEmpty(msgCheckR001)) {
                //エラー発生時
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemKaisiDay, itemKaisiTime, itemSyuuryouDay, itemSyuuryouTime);
                return MessageUtil.getErrorMessageInfo("", msgCheckR001, true, true, errFxhdd01List);
            }
        }
        // ﾁｪｯｸﾎﾞｯｸｽがすべてﾁｪｯｸされているかﾁｪｯｸ。
        List<String> itemIdList = Arrays.asList(GXHDO102B011Const.FUNSAIKI, GXHDO102B011Const.FUNSAIGOUKI, GXHDO102B011Const.RENZOKUUNTENKAISUU, GXHDO102B011Const.TOUNYUURYOU,
                GXHDO102B011Const.JIKAN_PASSKAISUU, GXHDO102B011Const.MILLSYUUHASUU, GXHDO102B011Const.SYUUSOKU, GXHDO102B011Const.PUMPSYUTURYOKCHECK, GXHDO102B011Const.RYUURYOU,
                GXHDO102B011Const.PASSKAISUU, GXHDO102B011Const.DISPANOSYURUI, GXHDO102B011Const.DISPAKAITENSUU, GXHDO102B011Const.KISYAKUYOUZAITENKA);
                
        for (String itemId : itemIdList) {
            FXHDD01 itemFxhdd01 = getItemRow(processData.getItemList(), itemId);
            if (!hasCheck(itemFxhdd01)) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemFxhdd01);
                return MessageUtil.getErrorMessageInfo("XHD-000199", true, true, errFxhdd01List, itemFxhdd01.getLabel1());
            }
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
            SrTenkaFunsai tmpSrTenkaFunsai = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrTenkaFunsai> srTenkaFunsaiList = getSrTenkaFunsaiData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo9, edaban);
                Integer passkaisuu1 = null;
                Integer passkaisuu2 = null;
                if (!srTenkaFunsaiList.isEmpty()) {
                    tmpSrTenkaFunsai = srTenkaFunsaiList.get(0);
                    passkaisuu1 = tmpSrTenkaFunsai.getPasskaisuu_sf1();
                    passkaisuu2 = tmpSrTenkaFunsai.getPasskaisuu_sf2();
                }

                deleteTmpSrTenkaFunsai(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);
                deleteTmpSubSrTenkaFunsai(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban, passkaisuu1);
                deleteTmpSubSrTenkaFunsai(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban, passkaisuu2);

                deleteTmpSubSrTenkaFunsaiHyoryo(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);
            }

            // 添加材ｽﾗﾘｰ作製・粉砕_登録処理
            insertSrTenkaFunsai(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, strSystime, processData, tmpSrTenkaFunsai);
            // 添加材ｽﾗﾘｰ作製・粉砕ﾊﾟｽ_登録処理
            insertSubSrTenkaFunsai(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, strSystime, processData, tmpSrTenkaFunsai);
            // 添加材ｽﾗﾘｰ作製・粉砕ﾊﾟｽ_登録処理
            insertSubSrTenkaFunsai2(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, strSystime, processData, tmpSrTenkaFunsai);
            // 添加材ｽﾗﾘｰ作製・粉砕入力ｻﾌﾞ画面の仮登録更新処理
            for (int i = 1; i < 3; i++) {
                insertSubSrTenkaFunsaiHyoryo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, i, strSystime);
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
        processData.setUserAuthParam(GXHDO102B011Const.USER_AUTH_UPDATE_PARAM);

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

            String strPasskaisuu_sf1 = getPasskaisuuValue(processData, GXHDO102B011Const.PASSKAISUU_SF1);
            String strPasskaisuu_sf2 = getPasskaisuuValue(processData, GXHDO102B011Const.PASSKAISUU_SF2);

            // 添加材ｽﾗﾘｰ作製・粉砕_更新処理
            updateSrTenkaFunsai(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);
            // 添加材ｽﾗﾘｰ作製・粉砕ﾊﾟｽ_更新処理
            updateSubSrTenkaFunsai(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, strPasskaisuu_sf1, processData);
            // 添加材ｽﾗﾘｰ作製・粉砕ﾊﾟｽ_更新処理
            updateSubSrTenkaFunsai2(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, strPasskaisuu_sf2, processData);
            // 添加材ｽﾗﾘｰ作製・粉砕入力ｻﾌﾞ画面の更新処理
            for (int i = 1; i < 3; i++) {
                updateSubSrTenkaFunsaiHyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, i, strSystime);
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
        processData.setUserAuthParam(GXHDO102B011Const.USER_AUTH_DELETE_PARAM);

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

            // ﾊﾟｽ回数を取得する。
            String strPasskaisuu_sf1 = getPasskaisuuValue(processData, GXHDO102B011Const.PASSKAISUU_SF1);
            String strPasskaisuu_sf2 = getPasskaisuuValue(processData, GXHDO102B011Const.PASSKAISUU_SF2);

            // 添加材ｽﾗﾘｰ作製・粉砕_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo9, edaban, paramJissekino);
            insertDeleteDataTmpSrTenkaFunsai(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo9, edaban, strSystime);

            // 添加材ｽﾗﾘｰ作製・粉砕ﾊﾟｽ_仮登録登録処理
            insertDeleteDataTmpSubSrTenkaFunsai(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo9, edaban, strPasskaisuu_sf1, strSystime);

            // 添加材ｽﾗﾘｰ作製・粉砕ﾊﾟｽ_仮登録登録処理
            insertDeleteDataTmpSubSrTenkaFunsai(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo9, edaban, strPasskaisuu_sf2, strSystime);

            // 添加材ｽﾗﾘｰ作製・粉砕入力_ｻﾌﾞ画面仮登録登録処理
            insertDeleteDataTmpSubSrTenkaFunsaiHyoryo(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo9, edaban, strSystime);

            // 添加材ｽﾗﾘｰ作製・粉砕_削除処理
            deleteSrTenkaFunsai(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);

            // 添加材ｽﾗﾘｰ作製・粉砕ﾊﾟｽ_削除処理
            deleteSubSrTenkaFunsai(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban, strPasskaisuu_sf1);

            // 添加材ｽﾗﾘｰ作製・粉砕ﾊﾟｽ_削除処理
            deleteSubSrTenkaFunsai(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban, strPasskaisuu_sf2);

            // 添加材ｽﾗﾘｰ作製・粉砕入力_ｻﾌﾞ画面削除処理
            deleteSubSrTenkaFunsaiHyoryo(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);

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
     * ﾊﾟｽ回数の規格値を取得
     *
     * @param processData 処理制御データ
     * @param itemId 項目ID
     * @return ﾊﾟｽ回数の規格値
     * @throws SQLException 例外エラー
     */
    public String getPasskaisuuValue(ProcessData processData, String itemId) throws SQLException {
        FXHDD01 itemPasskaisuu = getItemRow(processData.getItemList(), itemId); // ﾊﾟｽ回数
        if (itemPasskaisuu == null) {
            return "";
        }
        return StringUtil.nullToBlank(itemPasskaisuu.getKikakuChi()).replace("【", "").replace("】", "");
    }

    /**
     * 開始日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKaisinichijiDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B011Const.KAISI_SF1_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B011Const.KAISI_SF1_TIME);

        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            // 開始日時の設定
            setDateTimeItem(itemDay, itemTime, new Date());
            // 終了予定時の設定
            setYoteiDateTimeItem(processData, itemDay, itemTime);
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 終了日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setSyuuryounichijiDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B011Const.SYUURYOU_SF1_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B011Const.SYUURYOU_SF1_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 循環開始日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setJyunkankaisinichijiDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B011Const.JYUNKANKAISI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B011Const.JYUNKANKAISI_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 循環終了日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setJyunkansyuuryounichijiDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B011Const.JYUNKANSYUURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B011Const.JYUNKANSYUURYOU_TIME);
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
                        GXHDO102B011Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B011Const.BTN_KAISINICHIJI_TOP,
                        GXHDO102B011Const.BTN_SYUURYOUNICHIJI_TOP,
                        GXHDO102B011Const.BTN_JYUNKANKAISINICHIJI_TOP,
                        GXHDO102B011Const.BTN_JYUNKANSYUURYOUNICHIJI_TOP,
                        GXHDO102B011Const.BTN_UPDATE_TOP,
                        GXHDO102B011Const.BTN_DELETE_TOP,
                        GXHDO102B011Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B011Const.BTN_KAISINICHIJI_BOTTOM,
                        GXHDO102B011Const.BTN_SYUURYOUNICHIJI_BOTTOM,
                        GXHDO102B011Const.BTN_JYUNKANKAISINICHIJI_BOTTOM,
                        GXHDO102B011Const.BTN_JYUNKANSYUURYOUNICHIJI_BOTTOM,
                        GXHDO102B011Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B011Const.BTN_DELETE_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B011Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B011Const.BTN_INSERT_TOP,
                        GXHDO102B011Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B011Const.BTN_INSERT_BOTTOM));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO102B011Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B011Const.BTN_KAISINICHIJI_TOP,
                        GXHDO102B011Const.BTN_SYUURYOUNICHIJI_TOP,
                        GXHDO102B011Const.BTN_JYUNKANKAISINICHIJI_TOP,
                        GXHDO102B011Const.BTN_JYUNKANSYUURYOUNICHIJI_TOP,
                        GXHDO102B011Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B011Const.BTN_INSERT_TOP,
                        GXHDO102B011Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B011Const.BTN_KAISINICHIJI_BOTTOM,
                        GXHDO102B011Const.BTN_SYUURYOUNICHIJI_BOTTOM,
                        GXHDO102B011Const.BTN_JYUNKANKAISINICHIJI_BOTTOM,
                        GXHDO102B011Const.BTN_JYUNKANSYUURYOUNICHIJI_BOTTOM,
                        GXHDO102B011Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B011Const.BTN_INSERT_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B011Const.BTN_UPDATE_TOP,
                        GXHDO102B011Const.BTN_DELETE_TOP,
                        GXHDO102B011Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B011Const.BTN_DELETE_BOTTOM
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
        }

        // 入力項目の情報を画面にセットする。
        if (!setInputItemData(processData, queryRunnerDoc, queryRunnerQcdb, lotNo, formId, paramJissekino)) {
            // エラー発生時は処理を中断
            processData.setFatalError(true);
            processData.setInitMessageList(Arrays.asList(MessageUtil.getMessage("XHD-000038")));
            return processData;
        }
        // 撹拌機項目の表示制御
        setSrtenkafunsaiItemRendered(processData, queryRunnerDoc, queryRunnerQcdb, shikakariData, lotNo);
        // ﾊﾟｽ回数により表示の有無あり
        setSubsrtenkafunsaiItemRendered(processData);
        // 画面に取得した情報をセットする。(入力項目以外)
        setViewItemData(processData, shikakariData, lotNo);
        // 画面のラベル項目の値の背景色を取得できない場合、デフォルト値を設置
        GXHDO102C006Logic.setItemStyle(processData.getItemList());
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
        this.setItemData(processData, GXHDO102B011Const.WIPLOTNO, lotNo);
        // 添加材ｽﾗﾘｰ品名
        this.setItemData(processData, GXHDO102B011Const.TENKAZAISLURRYHINMEI, StringUtil.nullToBlank(getMapData(shikakariData, "hinmei")));
        // 添加材ｽﾗﾘｰLotNo
        this.setItemData(processData, GXHDO102B011Const.TENKAZAISLURRYLOTNO, StringUtil.nullToBlank(getMapData(shikakariData, "lotno")));
        // ﾛｯﾄ区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode"));
        // ﾛｯﾄ区分名称
        String lotkubun = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubun"));

        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO102B011Const.LOTKUBUN, "");
        } else {
            if (!StringUtil.isEmpty(lotkubun)) {
                lotkubuncode = lotkubuncode + ":" + lotkubun;
            }
            this.setItemData(processData, GXHDO102B011Const.LOTKUBUN, lotkubuncode);
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

        List<SrTenkaFunsai> srTenkaFunsaiList = new ArrayList<>();
        List<SubSrTenkaFunsaiHyoryo> subSrTenkaFunsaiHyoryoList = new ArrayList<>();
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

                // 添加材ｽﾗﾘｰ作製・粉砕入力_ｻﾌﾞ画面データ設定
                setInputItemDataSubFormC006(processData, null);
                return true;
            }

            // 添加材ｽﾗﾘｰ作製・粉砕データ取得
            srTenkaFunsaiList = getSrTenkaFunsaiData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo9, edaban);
            if (srTenkaFunsaiList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // 添加材ｽﾗﾘｰ作製・粉砕入力_サブ画面データ取得
            subSrTenkaFunsaiHyoryoList = getSubSrTenkaFunsaiHyoryoData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo9, edaban);
            if (subSrTenkaFunsaiHyoryoList.isEmpty() || subSrTenkaFunsaiHyoryoList.size() != 2) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }
            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srTenkaFunsaiList.isEmpty() || (subSrTenkaFunsaiHyoryoList.isEmpty() || subSrTenkaFunsaiHyoryoList.size() != 2)) {
            return false;
        }
        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srTenkaFunsaiList.get(0));
        // 添加材ｽﾗﾘｰ作製・粉砕入力_ｻﾌﾞ画面データ設定
        setInputItemDataSubFormC006(processData, subSrTenkaFunsaiHyoryoList);
        return true;

    }

    /**
     * データ設定処理
     *
     * @param processData 処理制御データ
     * @param SrTenkaFunsai 添加材ｽﾗﾘｰ作製・粉砕
     */
    private void setInputItemDataMainForm(ProcessData processData, SrTenkaFunsai srTenkaFunsai) {

        // 秤量号機
        this.setItemData(processData, GXHDO102B011Const.HYOURYOUGOUKI, getSrTenkaFunsaiItemData(GXHDO102B011Const.HYOURYOUGOUKI, srTenkaFunsai));

        // 粉砕機
        this.setItemData(processData, GXHDO102B011Const.FUNSAIKI, getSrTenkaFunsaiItemData(GXHDO102B011Const.FUNSAIKI, srTenkaFunsai));

        // 粉砕号機
        this.setItemData(processData, GXHDO102B011Const.FUNSAIGOUKI, getSrTenkaFunsaiItemData(GXHDO102B011Const.FUNSAIGOUKI, srTenkaFunsai));

        // ﾒﾃﾞｨｱLotNo
        this.setItemData(processData, GXHDO102B011Const.MEDIALOTNO, getSrTenkaFunsaiItemData(GXHDO102B011Const.MEDIALOTNO, srTenkaFunsai));

        // 連続運転回数
        this.setItemData(processData, GXHDO102B011Const.RENZOKUUNTENKAISUU, getSrTenkaFunsaiItemData(GXHDO102B011Const.RENZOKUUNTENKAISUU, srTenkaFunsai));

        // 投入量
        this.setItemData(processData, GXHDO102B011Const.TOUNYUURYOU, getSrTenkaFunsaiItemData(GXHDO102B011Const.TOUNYUURYOU, srTenkaFunsai));

        // 時間/ﾊﾟｽ回数
        this.setItemData(processData, GXHDO102B011Const.JIKAN_PASSKAISUU, getSrTenkaFunsaiItemData(GXHDO102B011Const.JIKAN_PASSKAISUU, srTenkaFunsai));

        // ﾐﾙ周波数
        this.setItemData(processData, GXHDO102B011Const.MILLSYUUHASUU, getSrTenkaFunsaiItemData(GXHDO102B011Const.MILLSYUUHASUU, srTenkaFunsai));

        // 周速
        this.setItemData(processData, GXHDO102B011Const.SYUUSOKU, getSrTenkaFunsaiItemData(GXHDO102B011Const.SYUUSOKU, srTenkaFunsai));

        // ﾎﾟﾝﾌﾟ出力
        this.setItemData(processData, GXHDO102B011Const.PUMPSYUTURYOKCHECK, getSrTenkaFunsaiItemData(GXHDO102B011Const.PUMPSYUTURYOKCHECK, srTenkaFunsai));

        // 流量
        this.setItemData(processData, GXHDO102B011Const.RYUURYOU, getSrTenkaFunsaiItemData(GXHDO102B011Const.RYUURYOU, srTenkaFunsai));

        // ﾊﾟｽ回数
        this.setItemData(processData, GXHDO102B011Const.PASSKAISUU, getSrTenkaFunsaiItemData(GXHDO102B011Const.PASSKAISUU, srTenkaFunsai));

        // ﾃﾞｨｽﾊﾟの種類
        this.setItemData(processData, GXHDO102B011Const.DISPANOSYURUI, getSrTenkaFunsaiItemData(GXHDO102B011Const.DISPANOSYURUI, srTenkaFunsai));

        // ﾃﾞｨｽﾊﾟ回転数
        this.setItemData(processData, GXHDO102B011Const.DISPAKAITENSUU, getSrTenkaFunsaiItemData(GXHDO102B011Const.DISPAKAITENSUU, srTenkaFunsai));

        // 開始日
        this.setItemData(processData, GXHDO102B011Const.KAISI_SF1_DAY, getSrTenkaFunsaiItemData(GXHDO102B011Const.KAISI_SF1_DAY, srTenkaFunsai));

        // 開始時間
        this.setItemData(processData, GXHDO102B011Const.KAISI_SF1_TIME, getSrTenkaFunsaiItemData(GXHDO102B011Const.KAISI_SF1_TIME, srTenkaFunsai));

        // 終了予定日
        this.setItemData(processData, GXHDO102B011Const.SYUURYOUYOTEI_SF1_DAY, getSrTenkaFunsaiItemData(GXHDO102B011Const.SYUURYOUYOTEI_SF1_DAY, srTenkaFunsai));

        // 終了予定時間
        this.setItemData(processData, GXHDO102B011Const.SYUURYOUYOTEI_SF1_TIME, getSrTenkaFunsaiItemData(GXHDO102B011Const.SYUURYOUYOTEI_SF1_TIME, srTenkaFunsai));

        // 負荷電流値
        this.setItemData(processData, GXHDO102B011Const.FUKADENRYUUTI_SF1, getSrTenkaFunsaiItemData(GXHDO102B011Const.FUKADENRYUUTI_SF1, srTenkaFunsai));

        // 製品温度
        this.setItemData(processData, GXHDO102B011Const.SEIHINONDO_SF1, getSrTenkaFunsaiItemData(GXHDO102B011Const.SEIHINONDO_SF1, srTenkaFunsai));

        // ｼｰﾙ温度
        this.setItemData(processData, GXHDO102B011Const.STICKERONDO_SF1, getSrTenkaFunsaiItemData(GXHDO102B011Const.STICKERONDO_SF1, srTenkaFunsai));

        // ﾎﾟﾝﾌﾟ目盛
        this.setItemData(processData, GXHDO102B011Const.PUMPMEMORI_SF1, getSrTenkaFunsaiItemData(GXHDO102B011Const.PUMPMEMORI_SF1, srTenkaFunsai));

        // ﾎﾟﾝﾌﾟ圧
        this.setItemData(processData, GXHDO102B011Const.PUMPATU_SF1, getSrTenkaFunsaiItemData(GXHDO102B011Const.PUMPATU_SF1, srTenkaFunsai));

        // 流量
        this.setItemData(processData, GXHDO102B011Const.RYUURYOU_SF1, getSrTenkaFunsaiItemData(GXHDO102B011Const.RYUURYOU_SF1, srTenkaFunsai));

        // 備考1
        this.setItemData(processData, GXHDO102B011Const.BIKOU1_SF1, getSrTenkaFunsaiItemData(GXHDO102B011Const.BIKOU1_SF1, srTenkaFunsai));

        // 備考2
        this.setItemData(processData, GXHDO102B011Const.BIKOU2_SF1, getSrTenkaFunsaiItemData(GXHDO102B011Const.BIKOU2_SF1, srTenkaFunsai));

        // 温度(往)
        this.setItemData(processData, GXHDO102B011Const.ONDO_OU_SF1, getSrTenkaFunsaiItemData(GXHDO102B011Const.ONDO_OU_SF1, srTenkaFunsai));

        // 温度(還)
        this.setItemData(processData, GXHDO102B011Const.ONDO_KAN_SF1, getSrTenkaFunsaiItemData(GXHDO102B011Const.ONDO_KAN_SF1, srTenkaFunsai));

        // 圧力(往)
        this.setItemData(processData, GXHDO102B011Const.ATURYOKU_OU_SF1, getSrTenkaFunsaiItemData(GXHDO102B011Const.ATURYOKU_OU_SF1, srTenkaFunsai));

        // 圧力(還)
        this.setItemData(processData, GXHDO102B011Const.ATURYOKU_KAN_SF1, getSrTenkaFunsaiItemData(GXHDO102B011Const.ATURYOKU_KAN_SF1, srTenkaFunsai));

        // 終了日
        this.setItemData(processData, GXHDO102B011Const.SYUURYOU_SF1_DAY, getSrTenkaFunsaiItemData(GXHDO102B011Const.SYUURYOU_SF1_DAY, srTenkaFunsai));

        // 終了時間
        this.setItemData(processData, GXHDO102B011Const.SYUURYOU_SF1_TIME, getSrTenkaFunsaiItemData(GXHDO102B011Const.SYUURYOU_SF1_TIME, srTenkaFunsai));

        // 担当者
        this.setItemData(processData, GXHDO102B011Const.TANTOUSYA_SF1, getSrTenkaFunsaiItemData(GXHDO102B011Const.TANTOUSYA_SF1, srTenkaFunsai));

        // 開始日
        this.setItemData(processData, GXHDO102B011Const.KAISI_SF2_DAY, getSrTenkaFunsaiItemData(GXHDO102B011Const.KAISI_SF2_DAY, srTenkaFunsai));

        // 開始時間
        this.setItemData(processData, GXHDO102B011Const.KAISI_SF2_TIME, getSrTenkaFunsaiItemData(GXHDO102B011Const.KAISI_SF2_TIME, srTenkaFunsai));

        // 終了予定日
        this.setItemData(processData, GXHDO102B011Const.SYUURYOUYOTEI_SF2_DAY, getSrTenkaFunsaiItemData(GXHDO102B011Const.SYUURYOUYOTEI_SF2_DAY, srTenkaFunsai));

        // 終了予定時間
        this.setItemData(processData, GXHDO102B011Const.SYUURYOUYOTEI_SF2_TIME, getSrTenkaFunsaiItemData(GXHDO102B011Const.SYUURYOUYOTEI_SF2_TIME, srTenkaFunsai));

        // 負荷電流値
        this.setItemData(processData, GXHDO102B011Const.FUKADENRYUUTI_SF2, getSrTenkaFunsaiItemData(GXHDO102B011Const.FUKADENRYUUTI_SF2, srTenkaFunsai));

        // 製品温度
        this.setItemData(processData, GXHDO102B011Const.SEIHINONDO_SF2, getSrTenkaFunsaiItemData(GXHDO102B011Const.SEIHINONDO_SF2, srTenkaFunsai));

        // ｼｰﾙ温度
        this.setItemData(processData, GXHDO102B011Const.STICKERONDO_SF2, getSrTenkaFunsaiItemData(GXHDO102B011Const.STICKERONDO_SF2, srTenkaFunsai));

        // ﾎﾟﾝﾌﾟ目盛
        this.setItemData(processData, GXHDO102B011Const.PUMPMEMORI_SF2, getSrTenkaFunsaiItemData(GXHDO102B011Const.PUMPMEMORI_SF2, srTenkaFunsai));

        // ﾎﾟﾝﾌﾟ圧
        this.setItemData(processData, GXHDO102B011Const.PUMPATU_SF2, getSrTenkaFunsaiItemData(GXHDO102B011Const.PUMPATU_SF2, srTenkaFunsai));

        // 終了日
        this.setItemData(processData, GXHDO102B011Const.SYUURYOU_SF2_DAY, getSrTenkaFunsaiItemData(GXHDO102B011Const.SYUURYOU_SF2_DAY, srTenkaFunsai));

        // 終了時間
        this.setItemData(processData, GXHDO102B011Const.SYUURYOU_SF2_TIME, getSrTenkaFunsaiItemData(GXHDO102B011Const.SYUURYOU_SF2_TIME, srTenkaFunsai));

        // 流量
        this.setItemData(processData, GXHDO102B011Const.RYUURYOU_SF2, getSrTenkaFunsaiItemData(GXHDO102B011Const.RYUURYOU_SF2, srTenkaFunsai));

        // 備考1
        this.setItemData(processData, GXHDO102B011Const.BIKOU1_SF2, getSrTenkaFunsaiItemData(GXHDO102B011Const.BIKOU1_SF2, srTenkaFunsai));

        // 備考2
        this.setItemData(processData, GXHDO102B011Const.BIKOU2_SF2, getSrTenkaFunsaiItemData(GXHDO102B011Const.BIKOU2_SF2, srTenkaFunsai));

        // 担当者
        this.setItemData(processData, GXHDO102B011Const.TANTOUSYA_SF2, getSrTenkaFunsaiItemData(GXHDO102B011Const.TANTOUSYA_SF2, srTenkaFunsai));

        // 溶剤①_部材在庫No1
        this.setItemData(processData, GXHDO102B011Const.YOUZAI1_BUZAIZAIKOLOTNO1, getSrTenkaFunsaiItemData(GXHDO102B011Const.YOUZAI1_BUZAIZAIKOLOTNO1, srTenkaFunsai));

        // 溶剤①_調合量1
        this.setItemData(processData, GXHDO102B011Const.YOUZAI1_TYOUGOURYOU1, getSrTenkaFunsaiItemData(GXHDO102B011Const.YOUZAI1_TYOUGOURYOU1, srTenkaFunsai));

        // 溶剤①_部材在庫No2
        this.setItemData(processData, GXHDO102B011Const.YOUZAI1_BUZAIZAIKOLOTNO2, getSrTenkaFunsaiItemData(GXHDO102B011Const.YOUZAI1_BUZAIZAIKOLOTNO2, srTenkaFunsai));

        // 溶剤①_調合量2
        this.setItemData(processData, GXHDO102B011Const.YOUZAI1_TYOUGOURYOU2, getSrTenkaFunsaiItemData(GXHDO102B011Const.YOUZAI1_TYOUGOURYOU2, srTenkaFunsai));

        // 溶剤②_部材在庫No1
        this.setItemData(processData, GXHDO102B011Const.YOUZAI2_BUZAIZAIKOLOTNO1, getSrTenkaFunsaiItemData(GXHDO102B011Const.YOUZAI2_BUZAIZAIKOLOTNO1, srTenkaFunsai));

        // 溶剤②_調合量1
        this.setItemData(processData, GXHDO102B011Const.YOUZAI2_TYOUGOURYOU1, getSrTenkaFunsaiItemData(GXHDO102B011Const.YOUZAI2_TYOUGOURYOU1, srTenkaFunsai));

        // 溶剤②_部材在庫No2
        this.setItemData(processData, GXHDO102B011Const.YOUZAI2_BUZAIZAIKOLOTNO2, getSrTenkaFunsaiItemData(GXHDO102B011Const.YOUZAI2_BUZAIZAIKOLOTNO2, srTenkaFunsai));

        // 溶剤②_調合量2
        this.setItemData(processData, GXHDO102B011Const.YOUZAI2_TYOUGOURYOU2, getSrTenkaFunsaiItemData(GXHDO102B011Const.YOUZAI2_TYOUGOURYOU2, srTenkaFunsai));

        // 担当者
        this.setItemData(processData, GXHDO102B011Const.TANTOUSYA, getSrTenkaFunsaiItemData(GXHDO102B011Const.TANTOUSYA, srTenkaFunsai));

        // 希釈溶剤添加
        this.setItemData(processData, GXHDO102B011Const.KISYAKUYOUZAITENKA, getSrTenkaFunsaiItemData(GXHDO102B011Const.KISYAKUYOUZAITENKA, srTenkaFunsai));

        // 循環開始日
        this.setItemData(processData, GXHDO102B011Const.JYUNKANKAISI_DAY, getSrTenkaFunsaiItemData(GXHDO102B011Const.JYUNKANKAISI_DAY, srTenkaFunsai));

        // 循環開始時間
        this.setItemData(processData, GXHDO102B011Const.JYUNKANKAISI_TIME, getSrTenkaFunsaiItemData(GXHDO102B011Const.JYUNKANKAISI_TIME, srTenkaFunsai));

        // 循環終了日
        this.setItemData(processData, GXHDO102B011Const.JYUNKANSYUURYOU_DAY, getSrTenkaFunsaiItemData(GXHDO102B011Const.JYUNKANSYUURYOU_DAY, srTenkaFunsai));

        // 循環終了時間
        this.setItemData(processData, GXHDO102B011Const.JYUNKANSYUURYOU_TIME, getSrTenkaFunsaiItemData(GXHDO102B011Const.JYUNKANSYUURYOU_TIME, srTenkaFunsai));

        // 担当者
        this.setItemData(processData, GXHDO102B011Const.JYUNKANTANTOUSYA, getSrTenkaFunsaiItemData(GXHDO102B011Const.JYUNKANTANTOUSYA, srTenkaFunsai));

    }

    /**
     * 添加材ｽﾗﾘｰ作製・粉砕の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return 添加材ｽﾗﾘｰ作製・粉砕データ
     * @throws SQLException 例外エラー
     */
    private List<SrTenkaFunsai> getSrTenkaFunsaiData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {
        List<SrTenkaFunsai> srTenkaFunsaiList;
        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            srTenkaFunsaiList = loadSrTenkaFunsai(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            srTenkaFunsaiList = loadTmpSrTenkaFunsai(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        }
        if (!srTenkaFunsaiList.isEmpty()) {
            if (srTenkaFunsaiList.size() > 1) {
                SrTenkaFunsai srTenkaFunsai1 = srTenkaFunsaiList.get(0);
                SrTenkaFunsai srTenkaFunsai2 = srTenkaFunsaiList.get(1);
                srTenkaFunsai1.setPasskaisuu_sf2(srTenkaFunsai2.getPasskaisuu_sf1());
                srTenkaFunsai1.setKaisinichiji_sf2(srTenkaFunsai2.getKaisinichiji_sf1());
                srTenkaFunsai1.setSyuuryouyoteinichiji_sf2(srTenkaFunsai2.getSyuuryouyoteinichiji_sf1());
                srTenkaFunsai1.setFukadenryuuti_sf2(srTenkaFunsai2.getFukadenryuuti_sf1());
                srTenkaFunsai1.setSeihinondo_sf2(srTenkaFunsai2.getSeihinondo_sf1());
                srTenkaFunsai1.setStickerondo_sf2(srTenkaFunsai2.getStickerondo_sf1());
                srTenkaFunsai1.setPumpmemori_sf2(srTenkaFunsai2.getPumpmemori_sf1());
                srTenkaFunsai1.setPumpatu_sf2(srTenkaFunsai2.getPumpatu_sf1());
                srTenkaFunsai1.setSyuuryounichiji_sf2(srTenkaFunsai2.getSyuuryounichiji_sf1());
                srTenkaFunsai1.setRyuuryou_sf2(srTenkaFunsai2.getRyuuryou_sf1());
                srTenkaFunsai1.setBikou1_sf2(srTenkaFunsai2.getBikou1_sf1());
                srTenkaFunsai1.setBikou2_sf2(srTenkaFunsai2.getBikou2_sf1());
                srTenkaFunsai1.setTantousya_sf2(srTenkaFunsai2.getTantousya_sf1());
            }
        }
        return srTenkaFunsaiList;
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
     * [添加材ｽﾗﾘｰ作製・粉砕]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrTenkaFunsai> loadSrTenkaFunsai(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + " F.kojyo "
                + ",F.lotno "
                + ",F.edaban "
                + ",F.tenkazaislurryhinmei "
                + ",F.tenkazaislurrylotno "
                + ",F.lotkubun "
                + ",F.hyouryougouki "
                + ",F.funsaiki "
                + ",F.funsaigouki "
                + ",F.medialotno "
                + ",F.renzokuuntenkaisuu "
                + ",F.tounyuuryou "
                + ",F.jikan_passkaisuu "
                + ",F.millsyuuhasuu "
                + ",F.syuusoku "
                + ",F.pumpsyuturyokcheck "
                + ",F.ryuuryou "
                + ",F.passkaisuu "
                + ",F.dispanosyurui "
                + ",F.dispakaitensuu "
                + ",SF.passkaisuu passkaisuu_sf1 "
                + ",SF.kaisinichiji kaisinichiji_sf1 "
                + ",SF.syuuryouyoteinichiji syuuryouyoteinichiji_sf1 "
                + ",SF.fukadenryuuti fukadenryuuti_sf1 "
                + ",SF.seihinondo seihinondo_sf1 "
                + ",SF.stickerondo stickerondo_sf1 "
                + ",SF.pumpmemori pumpmemori_sf1 "
                + ",SF.pumpatu pumpatu_sf1 "
                + ",SF.ryuuryou ryuuryou_sf1 "
                + ",SF.bikou1 bikou1_sf1 "
                + ",SF.bikou2 bikou2_sf1 "
                + ",SF.ondo_ou ondo_ou_sf1 "
                + ",SF.ondo_kan ondo_kan_sf1 "
                + ",SF.aturyoku_ou aturyoku_ou_sf1 "
                + ",SF.aturyoku_kan aturyoku_kan_sf1 "
                + ",SF.syuuryounichiji syuuryounichiji_sf1 "
                + ",SF.tantousya tantousya_sf1 "
                + ",F.youzai1_zairyouhinmei "
                + ",F.youzai1_tyougouryoukikaku "
                + ",F.youzai1_buzaizaikolotno1 "
                + ",F.youzai1_tyougouryou1 "
                + ",F.youzai1_buzaizaikolotno2 "
                + ",F.youzai1_tyougouryou2 "
                + ",F.youzai2_zairyouhinmei "
                + ",F.youzai2_tyougouryoukikaku "
                + ",F.youzai2_buzaizaikolotno1 "
                + ",F.youzai2_tyougouryou1 "
                + ",F.youzai2_buzaizaikolotno2 "
                + ",F.youzai2_tyougouryou2 "
                + ",F.tantousya "
                + ",F.pumpsyuturyoku "
                + ",F.millsyuuhasuu2 "
                + ",F.kisyakuyouzaitenka "
                + ",F.youzaijyunkanjikan "
                + ",F.jyunkankaisinichiji "
                + ",F.jyunkansyuuryounichiji "
                + ",F.jyunkantantousya "
                + ",F.torokunichiji "
                + ",F.kosinnichiji "
                + ",F.revision "
                + "      FROM sr_tenka_funsai F "
                + "LEFT OUTER JOIN sub_sr_tenka_funsai SF "
                + "        ON F.kojyo = SF.kojyo  "
                + "       AND F.lotno  = SF.lotno   "
                + "       AND F.edaban =  SF.edaban  "
                + "       AND F.revision =  SF.revision  "
                + " WHERE F.kojyo = ? AND F.lotno = ? AND F.edaban = ? ";

        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            sql += "AND F.revision = ? ";
        }
        sql += "ORDER BY SF.passkaisuu ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);

        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            params.add(rev);
        }

        Map<String, String> mapping = new HashMap<>();
        mapping.put("kojyo", "kojyo");                                            // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                            // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                          // 枝番
        mapping.put("tenkazaislurryhinmei", "tenkazaislurryhinmei");              // 添加材ｽﾗﾘｰ品名
        mapping.put("tenkazaislurrylotno", "tenkazaislurrylotno");                // 添加材ｽﾗﾘｰLotNo
        mapping.put("lotkubun", "lotkubun");                                      // ﾛｯﾄ区分
        mapping.put("hyouryougouki", "hyouryougouki");                            // 秤量号機
        mapping.put("funsaiki", "funsaiki");                                      // 粉砕機
        mapping.put("funsaigouki", "funsaigouki");                                // 粉砕号機
        mapping.put("medialotno", "medialotno");                                  // ﾒﾃﾞｨｱLotNo
        mapping.put("renzokuuntenkaisuu", "renzokuuntenkaisuu");                  // 連続運転回数
        mapping.put("tounyuuryou", "tounyuuryou");                                // 投入量
        mapping.put("jikan_passkaisuu", "jikan_passkaisuu");                      // 時間/ﾊﾟｽ回数
        mapping.put("millsyuuhasuu", "millsyuuhasuu");                            // ﾐﾙ周波数
        mapping.put("syuusoku", "syuusoku");                                      // 周速
        mapping.put("pumpsyuturyokcheck", "pumpsyuturyokcheck");                  // ﾎﾟﾝﾌﾟ出力
        mapping.put("ryuuryou", "ryuuryou");                                      // 流量
        mapping.put("passkaisuu", "passkaisuu");                                  // ﾊﾟｽ回数
        mapping.put("dispanosyurui", "dispanosyurui");                            // ﾃﾞｨｽﾊﾟの種類
        mapping.put("dispakaitensuu", "dispakaitensuu");                          // ﾃﾞｨｽﾊﾟ回転数
        mapping.put("passkaisuu_sf1", "passkaisuu_sf1");                          // ﾊﾟｽ回数
        mapping.put("kaisinichiji_sf1", "kaisinichiji_sf1");                      // 開始日時
        mapping.put("syuuryouyoteinichiji_sf1", "syuuryouyoteinichiji_sf1");      // 終了予定日時
        mapping.put("fukadenryuuti_sf1", "fukadenryuuti_sf1");                    // 負荷電流値
        mapping.put("seihinondo_sf1", "seihinondo_sf1");                          // 製品温度
        mapping.put("stickerondo_sf1", "stickerondo_sf1");                        // ｼｰﾙ温度
        mapping.put("pumpmemori_sf1", "pumpmemori_sf1");                          // ﾎﾟﾝﾌﾟ目盛
        mapping.put("pumpatu_sf1", "pumpatu_sf1");                                // ﾎﾟﾝﾌﾟ圧
        mapping.put("ryuuryou_sf1", "ryuuryou_sf1");                              // 流量
        mapping.put("bikou1_sf1", "bikou1_sf1");                                  // 備考1
        mapping.put("bikou2_sf1", "bikou2_sf1");                                  // 備考2
        mapping.put("ondo_ou_sf1", "ondo_ou_sf1");                                // 温度(往)
        mapping.put("ondo_kan_sf1", "ondo_kan_sf1");                              // 温度(還)
        mapping.put("aturyoku_ou_sf1", "aturyoku_ou_sf1");                        // 圧力(往)
        mapping.put("aturyoku_kan_sf1", "aturyoku_kan_sf1");                      // 圧力(還)
        mapping.put("syuuryounichiji_sf1", "syuuryounichiji_sf1");                // 終了日時
        mapping.put("tantousya_sf1", "tantousya_sf1");                            // 担当者
        mapping.put("youzai1_zairyouhinmei", "youzai1_zairyouhinmei");            // 溶剤①_材料品名
        mapping.put("youzai1_tyougouryoukikaku", "youzai1_tyougouryoukikaku");    // 溶剤①_調合量規格
        mapping.put("youzai1_buzaizaikolotno1", "youzai1_buzaizaikolotno1");      // 溶剤①_部材在庫No1
        mapping.put("youzai1_tyougouryou1", "youzai1_tyougouryou1");              // 溶剤①_調合量1
        mapping.put("youzai1_buzaizaikolotno2", "youzai1_buzaizaikolotno2");      // 溶剤①_部材在庫No2
        mapping.put("youzai1_tyougouryou2", "youzai1_tyougouryou2");              // 溶剤①_調合量2
        mapping.put("youzai2_zairyouhinmei", "youzai2_zairyouhinmei");            // 溶剤②_材料品名
        mapping.put("youzai2_tyougouryoukikaku", "youzai2_tyougouryoukikaku");    // 溶剤②_調合量規格
        mapping.put("youzai2_buzaizaikolotno1", "youzai2_buzaizaikolotno1");      // 溶剤②_部材在庫No1
        mapping.put("youzai2_tyougouryou1", "youzai2_tyougouryou1");              // 溶剤②_調合量1
        mapping.put("youzai2_buzaizaikolotno2", "youzai2_buzaizaikolotno2");      // 溶剤②_部材在庫No2
        mapping.put("youzai2_tyougouryou2", "youzai2_tyougouryou2");              // 溶剤②_調合量2
        mapping.put("tantousya", "tantousya");                                    // 担当者
        mapping.put("pumpsyuturyoku", "pumpsyuturyoku");                          // ﾎﾟﾝﾌﾟ出力
        mapping.put("millsyuuhasuu2", "millsyuuhasuu2");                          // ﾐﾙ周波数
        mapping.put("kisyakuyouzaitenka", "kisyakuyouzaitenka");                  // 希釈溶剤添加
        mapping.put("youzaijyunkanjikan", "youzaijyunkanjikan");                  // 溶剤循環時間
        mapping.put("jyunkankaisinichiji", "jyunkankaisinichiji");                // 循環開始日時
        mapping.put("jyunkansyuuryounichiji", "jyunkansyuuryounichiji");          // 循環終了日時
        mapping.put("jyunkantantousya", "jyunkantantousya");                      // 担当者
        mapping.put("torokunichiji", "torokunichiji");                            // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                              // 更新日時
        mapping.put("revision", "revision");                                      // revision

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrTenkaFunsai>> beanHandler = new BeanListHandler<>(SrTenkaFunsai.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [添加材ｽﾗﾘｰ作製・粉砕]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrTenkaFunsai> getSubSrTenkaFunsaiData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev, String passkaisuu) throws SQLException {

        String sql = "SELECT "
                + "  SF.passkaisuu passkaisuu_sf2 "
                + " ,SF.kaisinichiji kaisinichiji_sf2 "
                + " ,SF.syuuryouyoteinichiji syuuryouyoteinichiji_sf2 "
                + " ,SF.fukadenryuuti fukadenryuuti_sf2 "
                + " ,SF.seihinondo seihinondo_sf2 "
                + " ,SF.stickerondo stickerondo_sf2 "
                + " ,SF.pumpmemori pumpmemori_sf2 "
                + " ,SF.pumpatu pumpatu_sf2 "
                + " ,SF.syuuryounichiji syuuryounichiji_sf2 "
                + " ,SF.ryuuryou ryuuryou_sf2 "
                + " ,SF.bikou1 bikou1_sf2 "
                + " ,SF.bikou2 bikou2_sf2 "
                + " ,SF.tantousya tantousya_sf2 "
                + "      FROM sub_sr_tenka_funsai SF "
                + " WHERE SF.kojyo = ? AND SF.lotno = ? AND SF.edaban = ? AND SF.passkaisuu = ? ";

        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            sql += "AND SF.revision = ? ";
        }

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(passkaisuu);

        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            params.add(rev);
        }

        Map<String, String> mapping = new HashMap<>();
        mapping.put("passkaisuu_sf2 ", "passkaisuu_sf2");                      // ﾊﾟｽ回数
        mapping.put("kaisinichiji_sf2 ", "kaisinichiji_sf2");                  // 開始日時
        mapping.put("syuuryouyoteinichiji_sf2 ", "syuuryouyoteinichiji_sf2");  // 終了予定日時
        mapping.put("fukadenryuuti_sf2 ", "fukadenryuuti_sf2");                // 負荷電流値
        mapping.put("seihinondo_sf2 ", "seihinondo_sf2");                      // 製品温度
        mapping.put("stickerondo_sf2 ", "stickerondo_sf2");                    // ｼｰﾙ温度
        mapping.put("pumpmemori_sf2 ", "pumpmemori_sf2");                      // ﾎﾟﾝﾌﾟ目盛
        mapping.put("pumpatu_sf2 ", "pumpatu_sf2");                            // ﾎﾟﾝﾌﾟ圧
        mapping.put("syuuryounichiji_sf2 ", "syuuryounichiji_sf2");            // 終了日時
        mapping.put("ryuuryou_sf2 ", "ryuuryou_sf2");                          // 流量
        mapping.put("bikou1_sf2 ", "bikou1_sf2");                              // 備考1
        mapping.put("bikou2_sf2 ", "bikou2_sf2");                              // 備考2
        mapping.put("tantousya_sf2 ", "tantousya_sf2");                        // 担当者

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrTenkaFunsai>> beanHandler = new BeanListHandler<>(SrTenkaFunsai.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [添加材ｽﾗﾘｰ作製・粉砕_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrTenkaFunsai> loadTmpSrTenkaFunsai(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + " F.kojyo "
                + ",F.lotno "
                + ",F.edaban "
                + ",F.tenkazaislurryhinmei "
                + ",F.tenkazaislurrylotno "
                + ",F.lotkubun "
                + ",F.hyouryougouki "
                + ",F.funsaiki "
                + ",F.funsaigouki "
                + ",F.medialotno "
                + ",F.renzokuuntenkaisuu "
                + ",F.tounyuuryou "
                + ",F.jikan_passkaisuu "
                + ",F.millsyuuhasuu "
                + ",F.syuusoku "
                + ",F.pumpsyuturyokcheck "
                + ",F.ryuuryou "
                + ",F.passkaisuu "
                + ",F.dispanosyurui "
                + ",F.dispakaitensuu "
                + ",SF.passkaisuu passkaisuu_sf1 "
                + ",SF.kaisinichiji kaisinichiji_sf1 "
                + ",SF.syuuryouyoteinichiji syuuryouyoteinichiji_sf1 "
                + ",SF.fukadenryuuti fukadenryuuti_sf1 "
                + ",SF.seihinondo seihinondo_sf1 "
                + ",SF.stickerondo stickerondo_sf1 "
                + ",SF.pumpmemori pumpmemori_sf1 "
                + ",SF.pumpatu pumpatu_sf1 "
                + ",SF.ryuuryou ryuuryou_sf1 "
                + ",SF.bikou1 bikou1_sf1 "
                + ",SF.bikou2 bikou2_sf1 "
                + ",SF.ondo_ou ondo_ou_sf1 "
                + ",SF.ondo_kan ondo_kan_sf1 "
                + ",SF.aturyoku_ou aturyoku_ou_sf1 "
                + ",SF.aturyoku_kan aturyoku_kan_sf1 "
                + ",SF.syuuryounichiji syuuryounichiji_sf1 "
                + ",SF.tantousya tantousya_sf1 "
                + ",F.youzai1_zairyouhinmei "
                + ",F.youzai1_tyougouryoukikaku "
                + ",F.youzai1_buzaizaikolotno1 "
                + ",F.youzai1_tyougouryou1 "
                + ",F.youzai1_buzaizaikolotno2 "
                + ",F.youzai1_tyougouryou2 "
                + ",F.youzai2_zairyouhinmei "
                + ",F.youzai2_tyougouryoukikaku "
                + ",F.youzai2_buzaizaikolotno1 "
                + ",F.youzai2_tyougouryou1 "
                + ",F.youzai2_buzaizaikolotno2 "
                + ",F.youzai2_tyougouryou2 "
                + ",F.tantousya "
                + ",F.pumpsyuturyoku "
                + ",F.millsyuuhasuu2 "
                + ",F.kisyakuyouzaitenka "
                + ",F.youzaijyunkanjikan "
                + ",F.jyunkankaisinichiji "
                + ",F.jyunkansyuuryounichiji "
                + ",F.jyunkantantousya "
                + ",F.torokunichiji "
                + ",F.kosinnichiji "
                + ",F.revision "
                + ",F.sakujyoflg "
                + " FROM tmp_sr_tenka_funsai F "
                + "LEFT OUTER JOIN tmp_sub_sr_tenka_funsai SF "
                + "        ON F.kojyo = SF.kojyo  "
                + "       AND F.lotno  = SF.lotno   "
                + "       AND F.edaban =  SF.edaban  "
                + "       AND F.revision =  SF.revision  "
                + "       AND F.sakujyoflg =  SF.sakujyoflg  "
                + " WHERE F.kojyo = ? AND F.lotno = ? AND F.edaban = ? AND F.sakujyoflg = ? ";

        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            sql += "AND F.revision = ? ";
        }
        sql += "ORDER BY SF.passkaisuu ";

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
        mapping.put("kojyo", "kojyo");                                            // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                            // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                          // 枝番
        mapping.put("tenkazaislurryhinmei", "tenkazaislurryhinmei");              // 添加材ｽﾗﾘｰ品名
        mapping.put("tenkazaislurrylotno", "tenkazaislurrylotno");                // 添加材ｽﾗﾘｰLotNo
        mapping.put("lotkubun", "lotkubun");                                      // ﾛｯﾄ区分
        mapping.put("hyouryougouki", "hyouryougouki");                            // 秤量号機
        mapping.put("funsaiki", "funsaiki");                                      // 粉砕機
        mapping.put("funsaigouki", "funsaigouki");                                // 粉砕号機
        mapping.put("medialotno", "medialotno");                                  // ﾒﾃﾞｨｱLotNo
        mapping.put("renzokuuntenkaisuu", "renzokuuntenkaisuu");                  // 連続運転回数
        mapping.put("tounyuuryou", "tounyuuryou");                                // 投入量
        mapping.put("jikan_passkaisuu", "jikan_passkaisuu");                      // 時間/ﾊﾟｽ回数
        mapping.put("millsyuuhasuu", "millsyuuhasuu");                            // ﾐﾙ周波数
        mapping.put("syuusoku", "syuusoku");                                      // 周速
        mapping.put("pumpsyuturyokcheck", "pumpsyuturyokcheck");                  // ﾎﾟﾝﾌﾟ出力
        mapping.put("ryuuryou", "ryuuryou");                                      // 流量
        mapping.put("passkaisuu", "passkaisuu");                                  // ﾊﾟｽ回数
        mapping.put("dispanosyurui", "dispanosyurui");                            // ﾃﾞｨｽﾊﾟの種類
        mapping.put("dispakaitensuu", "dispakaitensuu");                          // ﾃﾞｨｽﾊﾟ回転数
        mapping.put("passkaisuu_sf1", "passkaisuu_sf1");                          // ﾊﾟｽ回数
        mapping.put("kaisinichiji_sf1", "kaisinichiji_sf1");                      // 開始日時
        mapping.put("syuuryouyoteinichiji_sf1", "syuuryouyoteinichiji_sf1");      // 終了予定日時
        mapping.put("fukadenryuuti_sf1", "fukadenryuuti_sf1");                    // 負荷電流値
        mapping.put("seihinondo_sf1", "seihinondo_sf1");                          // 製品温度
        mapping.put("stickerondo_sf1", "stickerondo_sf1");                        // ｼｰﾙ温度
        mapping.put("pumpmemori_sf1", "pumpmemori_sf1");                          // ﾎﾟﾝﾌﾟ目盛
        mapping.put("pumpatu_sf1", "pumpatu_sf1");                                // ﾎﾟﾝﾌﾟ圧
        mapping.put("ryuuryou_sf1", "ryuuryou_sf1");                              // 流量
        mapping.put("bikou1_sf1", "bikou1_sf1");                                  // 備考1
        mapping.put("bikou2_sf1", "bikou2_sf1");                                  // 備考2
        mapping.put("ondo_ou_sf1", "ondo_ou_sf1");                                // 温度(往)
        mapping.put("ondo_kan_sf1", "ondo_kan_sf1");                              // 温度(還)
        mapping.put("aturyoku_ou_sf1", "aturyoku_ou_sf1");                        // 圧力(往)
        mapping.put("aturyoku_kan_sf1", "aturyoku_kan_sf1");                      // 圧力(還)
        mapping.put("syuuryounichiji_sf1", "syuuryounichiji_sf1");                // 終了日時
        mapping.put("tantousya_sf1", "tantousya_sf1");                            // 担当者
        mapping.put("youzai1_zairyouhinmei", "youzai1_zairyouhinmei");            // 溶剤①_材料品名
        mapping.put("youzai1_tyougouryoukikaku", "youzai1_tyougouryoukikaku");    // 溶剤①_調合量規格
        mapping.put("youzai1_buzaizaikolotno1", "youzai1_buzaizaikolotno1");      // 溶剤①_部材在庫No1
        mapping.put("youzai1_tyougouryou1", "youzai1_tyougouryou1");              // 溶剤①_調合量1
        mapping.put("youzai1_buzaizaikolotno2", "youzai1_buzaizaikolotno2");      // 溶剤①_部材在庫No2
        mapping.put("youzai1_tyougouryou2", "youzai1_tyougouryou2");              // 溶剤①_調合量2
        mapping.put("youzai2_zairyouhinmei", "youzai2_zairyouhinmei");            // 溶剤②_材料品名
        mapping.put("youzai2_tyougouryoukikaku", "youzai2_tyougouryoukikaku");    // 溶剤②_調合量規格
        mapping.put("youzai2_buzaizaikolotno1", "youzai2_buzaizaikolotno1");      // 溶剤②_部材在庫No1
        mapping.put("youzai2_tyougouryou1", "youzai2_tyougouryou1");              // 溶剤②_調合量1
        mapping.put("youzai2_buzaizaikolotno2", "youzai2_buzaizaikolotno2");      // 溶剤②_部材在庫No2
        mapping.put("youzai2_tyougouryou2", "youzai2_tyougouryou2");              // 溶剤②_調合量2
        mapping.put("tantousya", "tantousya");                                    // 担当者
        mapping.put("pumpsyuturyoku", "pumpsyuturyoku");                          // ﾎﾟﾝﾌﾟ出力
        mapping.put("millsyuuhasuu2", "millsyuuhasuu2");                          // ﾐﾙ周波数
        mapping.put("kisyakuyouzaitenka", "kisyakuyouzaitenka");                  // 希釈溶剤添加
        mapping.put("youzaijyunkanjikan", "youzaijyunkanjikan");                  // 溶剤循環時間
        mapping.put("jyunkankaisinichiji", "jyunkankaisinichiji");                // 循環開始日時
        mapping.put("jyunkansyuuryounichiji", "jyunkansyuuryounichiji");          // 循環終了日時
        mapping.put("jyunkantantousya", "jyunkantantousya");                      // 担当者
        mapping.put("torokunichiji", "torokunichiji");                            // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                              // 更新日時
        mapping.put("revision", "revision");                                      // revision
        mapping.put("sakujyoflg", "sakujyoflg");                                  // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrTenkaFunsai>> beanHandler = new BeanListHandler<>(SrTenkaFunsai.class, rowProcessor);

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
     * @param srTenkaFunsai 添加材ｽﾗﾘｰ作製・粉砕データ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrTenkaFunsai srTenkaFunsai) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srTenkaFunsai != null) {
            // 元データが存在する場合元データより取得
            return getSrTenkaFunsaiItemData(itemId, srTenkaFunsai);
        } else {
            return null;
        }
    }

    /**
     * 項目データ(入力値)取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @param srTenkaFunsai 添加材ｽﾗﾘｰ作製・粉砕データ
     * @return 入力値
     */
    private String getItemKikakuchi(List<FXHDD01> listData, String itemId, SrTenkaFunsai srTenkaFunsai) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return StringUtil.nullToBlank(selectData.get(0).getKikakuChi()).replace("【", "").replace("】", "");
        } else if (srTenkaFunsai != null) {
            // 元データが存在する場合元データより取得
            return getSrTenkaFunsaiItemData(itemId, srTenkaFunsai);
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
     * 添加材ｽﾗﾘｰ作製・粉砕_仮登録(tmp_sr_tenka_funsai)登録処理
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
    private void insertTmpSrTenkaFunsai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "INSERT INTO tmp_sr_tenka_funsai ( "
                + "kojyo,lotno,edaban,tenkazaislurryhinmei,tenkazaislurrylotno,lotkubun,hyouryougouki,funsaiki,funsaigouki,"
                + "medialotno,renzokuuntenkaisuu,tounyuuryou,jikan_passkaisuu,millsyuuhasuu,syuusoku,pumpsyuturyokcheck,"
                + "ryuuryou,passkaisuu,dispanosyurui,dispakaitensuu,youzai1_zairyouhinmei,youzai1_tyougouryoukikaku,"
                + "youzai1_buzaizaikolotno1,youzai1_tyougouryou1,youzai1_buzaizaikolotno2,youzai1_tyougouryou2,youzai2_zairyouhinmei,"
                + "youzai2_tyougouryoukikaku,youzai2_buzaizaikolotno1,youzai2_tyougouryou1,youzai2_buzaizaikolotno2,"
                + "youzai2_tyougouryou2,tantousya,pumpsyuturyoku,millsyuuhasuu2,kisyakuyouzaitenka,youzaijyunkanjikan,"
                + "jyunkankaisinichiji,jyunkansyuuryounichiji,jyunkantantousya,torokunichiji,kosinnichiji,revision,sakujyoflg "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrTenkaFunsai(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, processData, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 添加材ｽﾗﾘｰ作製・粉砕_仮登録(tmp_sub_sr_tenka_funsai)登録処理
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
    private void insertTmpSubSrTenkaFunsai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String passkaisuu = StringUtil.nullToBlank(getItemKikakuchi(processData.getItemList(), GXHDO102B011Const.PASSKAISUU_SF1, null));
        if ("".equals(passkaisuu)) {
            return;
        }
        String sql = "INSERT INTO tmp_sub_sr_tenka_funsai ( "
                + "kojyo,lotno,edaban,passkaisuu,kaisinichiji,syuuryouyoteinichiji,fukadenryuuti,seihinondo,stickerondo,pumpmemori,"
                + "pumpatu,ryuuryou,bikou1,bikou2,ondo_ou,ondo_kan,aturyoku_ou,aturyoku_kan,syuuryounichiji,tantousya,torokunichiji,"
                + "kosinnichiji,revision,sakujyoflg"
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSubSrTenkaFunsai(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, processData, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 添加材ｽﾗﾘｰ作製・粉砕_仮登録(tmp_sub_sr_tenka_funsai)登録処理
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
    private void insertTmpSubSrTenkaFunsai2(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String passkaisuu = StringUtil.nullToBlank(getItemKikakuchi(processData.getItemList(), GXHDO102B011Const.PASSKAISUU_SF2, null));
        if ("".equals(passkaisuu)) {
            return;
        }
        String sql = "INSERT INTO tmp_sub_sr_tenka_funsai ( "
                + "kojyo,lotno,edaban,passkaisuu,kaisinichiji,syuuryouyoteinichiji,fukadenryuuti,seihinondo,stickerondo,pumpmemori,"
                + "pumpatu,ryuuryou,bikou1,bikou2,ondo_ou,ondo_kan,aturyoku_ou,aturyoku_kan,syuuryounichiji,tantousya,torokunichiji,"
                + "kosinnichiji,revision,sakujyoflg"
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSubSrTenkaFunsai2(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, processData, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 添加材ｽﾗﾘｰ作製・粉砕_仮登録(tmp_sr_tenka_funsai)更新処理
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
    private void updateTmpSrTenkaFunsai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "UPDATE tmp_sr_tenka_funsai SET "
                + "tenkazaislurryhinmei = ?,tenkazaislurrylotno = ?,lotkubun = ?,hyouryougouki = ?,funsaiki = ?,funsaigouki = ?,medialotno = ?,"
                + "renzokuuntenkaisuu = ?,tounyuuryou = ?,jikan_passkaisuu = ?,millsyuuhasuu = ?,syuusoku = ?,pumpsyuturyokcheck = ?,ryuuryou = ?,"
                + "passkaisuu = ?,dispanosyurui = ?,dispakaitensuu = ?,youzai1_zairyouhinmei = ?,youzai1_tyougouryoukikaku = ?,youzai1_buzaizaikolotno1 = ?,"
                + "youzai1_tyougouryou1 = ?,youzai1_buzaizaikolotno2 = ?,youzai1_tyougouryou2 = ?,youzai2_zairyouhinmei = ?,youzai2_tyougouryoukikaku = ?,"
                + "youzai2_buzaizaikolotno1 = ?,youzai2_tyougouryou1 = ?,youzai2_buzaizaikolotno2 = ?,youzai2_tyougouryou2 = ?,tantousya = ?,"
                + "pumpsyuturyoku = ?,millsyuuhasuu2 = ?,kisyakuyouzaitenka = ?,youzaijyunkanjikan = ?,jyunkankaisinichiji = ?,jyunkansyuuryounichiji = ?,"
                + "jyunkantantousya = ?,kosinnichiji = ?,revision = ?,sakujyoflg = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrTenkaFunsai> srTenkaFunsaiList = getSrTenkaFunsaiData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrTenkaFunsai srTenkaFunsai = null;
        if (!srTenkaFunsaiList.isEmpty()) {
            srTenkaFunsai = srTenkaFunsaiList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrTenkaFunsai(false, newRev, 0, "", "", "", systemTime, processData, srTenkaFunsai);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 添加材ｽﾗﾘｰ作製・粉砕_仮登録(tmp_sub_sr_tenka_funsai)更新処理
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
    private void updateTmpSubSrTenkaFunsai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String passkaisuu, String systemTime, ProcessData processData) throws SQLException {
        if ("".equals(passkaisuu)) {
            return;
        }
        String sql = "UPDATE tmp_sub_sr_tenka_funsai SET "
                + "passkaisuu = ?,kaisinichiji = ?,syuuryouyoteinichiji = ?,fukadenryuuti = ?,seihinondo = ?,stickerondo = ?,pumpmemori = ?,pumpatu = ?,"
                + "ryuuryou = ?,bikou1 = ?,bikou2 = ?,ondo_ou = ?,ondo_kan = ?,aturyoku_ou = ?,aturyoku_kan = ?,syuuryounichiji = ?,tantousya = ?,"
                + "kosinnichiji = ?,revision = ?,sakujyoflg = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND passkaisuu = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrTenkaFunsai> srTenkaFunsaiList = getSrTenkaFunsaiData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrTenkaFunsai srTenkaFunsai = null;
        if (!srTenkaFunsaiList.isEmpty()) {
            srTenkaFunsai = srTenkaFunsaiList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSubSrTenkaFunsai(false, newRev, 0, "", "", "", systemTime, processData, srTenkaFunsai);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(passkaisuu);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 添加材ｽﾗﾘｰ作製・粉砕_仮登録(tmp_sub_sr_tenka_funsai)更新処理
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
    private void updateTmpSubSrTenkaFunsai2(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String passkaisuu, String systemTime, ProcessData processData) throws SQLException {

        if ("".equals(passkaisuu)) {
            return;
        }
        String sql = "UPDATE tmp_sub_sr_tenka_funsai SET "
                + "passkaisuu = ?,kaisinichiji = ?,syuuryouyoteinichiji = ?,fukadenryuuti = ?,seihinondo = ?,stickerondo = ?,pumpmemori = ?,pumpatu = ?,"
                + "ryuuryou = ?,bikou1 = ?,bikou2 = ?,ondo_ou = ?,ondo_kan = ?,aturyoku_ou = ?,aturyoku_kan = ?,syuuryounichiji = ?,tantousya = ?,"
                + "kosinnichiji = ?,revision = ?,sakujyoflg = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND passkaisuu = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrTenkaFunsai> srTenkaFunsaiList = getSrTenkaFunsaiData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrTenkaFunsai srTenkaFunsai = null;
        if (!srTenkaFunsaiList.isEmpty()) {
            srTenkaFunsai = srTenkaFunsaiList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSubSrTenkaFunsai2(false, newRev, 0, "", "", "", systemTime, processData, srTenkaFunsai);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(passkaisuu);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 添加材ｽﾗﾘｰ作製・粉砕_仮登録(tmp_sr_tenka_funsai)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrTenkaFunsai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sr_tenka_funsai "
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
     * 添加材ｽﾗﾘｰ作製・粉砕_仮登録(tmp_sr_tenka_funsai)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSubSrTenkaFunsai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, Integer passkaisuu) throws SQLException {

        if (passkaisuu == null) {
            return;
        }
        String sql = "DELETE FROM tmp_sub_sr_tenka_funsai "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND passkaisuu = ? AND revision = ? ";

        //更新値設定
        List<Object> params = new ArrayList<>();

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(passkaisuu);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 添加材ｽﾗﾘｰ作製・粉砕_仮登録(tmp_sr_tenka_funsai)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srTenkaFunsai 添加材ｽﾗﾘｰ作製・粉砕データ
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrTenkaFunsai(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, String systemTime, ProcessData processData, SrTenkaFunsai srTenkaFunsai) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // 循環開始日時
        String jyunkankaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B011Const.JYUNKANKAISI_TIME, srTenkaFunsai));
        // 循環終了日時
        String jyunkansyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B011Const.JYUNKANSYUURYOU_TIME, srTenkaFunsai));

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.TENKAZAISLURRYHINMEI, srTenkaFunsai))); // 添加材ｽﾗﾘｰ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.TENKAZAISLURRYLOTNO, srTenkaFunsai))); // 添加材ｽﾗﾘｰLotNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.LOTKUBUN, srTenkaFunsai))); // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.HYOURYOUGOUKI, srTenkaFunsai))); // 秤量号機
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B011Const.FUNSAIKI, srTenkaFunsai), null)); // 粉砕機
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B011Const.FUNSAIGOUKI, srTenkaFunsai), null)); // 粉砕号機
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.MEDIALOTNO, srTenkaFunsai))); // ﾒﾃﾞｨｱLotNo
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B011Const.RENZOKUUNTENKAISUU, srTenkaFunsai), null)); // 連続運転回数
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B011Const.TOUNYUURYOU, srTenkaFunsai), null)); // 投入量
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B011Const.JIKAN_PASSKAISUU, srTenkaFunsai), null)); // 時間/ﾊﾟｽ回数
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B011Const.MILLSYUUHASUU, srTenkaFunsai), null)); // ﾐﾙ周波数
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B011Const.SYUUSOKU, srTenkaFunsai), null)); // 周速
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B011Const.PUMPSYUTURYOKCHECK, srTenkaFunsai), null)); // ﾎﾟﾝﾌﾟ出力
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B011Const.RYUURYOU, srTenkaFunsai), null)); // 流量
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B011Const.PASSKAISUU, srTenkaFunsai), null)); // ﾊﾟｽ回数
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B011Const.DISPANOSYURUI, srTenkaFunsai), null)); // ﾃﾞｨｽﾊﾟの種類
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B011Const.DISPAKAITENSUU, srTenkaFunsai), null)); // ﾃﾞｨｽﾊﾟ回転数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B011Const.YOUZAI1_ZAIRYOUHINMEI, srTenkaFunsai))); // 溶剤①_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B011Const.YOUZAI1_TYOUGOURYOUKIKAKU, srTenkaFunsai))); // 溶剤①_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.YOUZAI1_BUZAIZAIKOLOTNO1, srTenkaFunsai))); // 溶剤①_部材在庫No1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.YOUZAI1_TYOUGOURYOU1, srTenkaFunsai))); // 溶剤①_調合量1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.YOUZAI1_BUZAIZAIKOLOTNO2, srTenkaFunsai))); // 溶剤①_部材在庫No2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.YOUZAI1_TYOUGOURYOU2, srTenkaFunsai))); // 溶剤①_調合量2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B011Const.YOUZAI2_ZAIRYOUHINMEI, srTenkaFunsai))); // 溶剤②_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B011Const.YOUZAI2_TYOUGOURYOUKIKAKU, srTenkaFunsai))); // 溶剤②_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.YOUZAI2_BUZAIZAIKOLOTNO1, srTenkaFunsai))); // 溶剤②_部材在庫No1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.YOUZAI2_TYOUGOURYOU1, srTenkaFunsai))); // 溶剤②_調合量1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.YOUZAI2_BUZAIZAIKOLOTNO2, srTenkaFunsai))); // 溶剤②_部材在庫No2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.YOUZAI2_TYOUGOURYOU2, srTenkaFunsai))); // 溶剤②_調合量2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.TANTOUSYA, srTenkaFunsai))); // 担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B011Const.PUMPSYUTURYOKU, srTenkaFunsai))); // ﾎﾟﾝﾌﾟ出力
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B011Const.MILLSYUUHASUU2, srTenkaFunsai))); // ﾐﾙ周波数
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B011Const.KISYAKUYOUZAITENKA, srTenkaFunsai), null)); // 希釈溶剤添加
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B011Const.YOUZAIJYUNKANJIKAN, srTenkaFunsai))); // 溶剤循環時間
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.JYUNKANKAISI_DAY, srTenkaFunsai),
                "".equals(jyunkankaisiTime) ? "0000" : jyunkankaisiTime)); // 循環開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.JYUNKANSYUURYOU_DAY, srTenkaFunsai),
                "".equals(jyunkansyuuryouTime) ? "0000" : jyunkansyuuryouTime)); // 循環終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.JYUNKANTANTOUSYA, srTenkaFunsai))); // 担当者

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
     * 添加材ｽﾗﾘｰ作製・粉砕_仮登録(tmp_sub_sr_tenka_funsai)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srTenkaFunsai 添加材ｽﾗﾘｰ作製・粉砕データ
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSubSrTenkaFunsai(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, String systemTime, ProcessData processData, SrTenkaFunsai srTenkaFunsai) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // 開始日時
        String kaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B011Const.KAISI_SF1_TIME, srTenkaFunsai));
        // 終了日時
        String syuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B011Const.SYUURYOU_SF1_TIME, srTenkaFunsai));
        // 終了予定日時
        String syuuryouyoteiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B011Const.SYUURYOUYOTEI_SF1_TIME, srTenkaFunsai));

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B011Const.PASSKAISUU_SF1, srTenkaFunsai))); // ﾊﾟｽ回数
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.KAISI_SF1_DAY, srTenkaFunsai),
                "".equals(kaisiTime) ? "0000" : kaisiTime)); // 開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.SYUURYOUYOTEI_SF1_DAY, srTenkaFunsai),
                "".equals(syuuryouyoteiTime) ? "0000" : syuuryouyoteiTime)); // 終了予定日時
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.FUKADENRYUUTI_SF1, srTenkaFunsai))); // 負荷電流値
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.SEIHINONDO_SF1, srTenkaFunsai))); // 製品温度
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.STICKERONDO_SF1, srTenkaFunsai))); // ｼｰﾙ温度
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.PUMPMEMORI_SF1, srTenkaFunsai))); // ﾎﾟﾝﾌﾟ目盛
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.PUMPATU_SF1, srTenkaFunsai))); // ﾎﾟﾝﾌﾟ圧
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.RYUURYOU_SF1, srTenkaFunsai))); // 流量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.BIKOU1_SF1, srTenkaFunsai))); // 備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.BIKOU2_SF1, srTenkaFunsai))); // 備考2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.ONDO_OU_SF1, srTenkaFunsai))); // 温度(往)
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.ONDO_KAN_SF1, srTenkaFunsai))); // 温度(還)
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.ATURYOKU_OU_SF1, srTenkaFunsai))); // 圧力(往)
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.ATURYOKU_KAN_SF1, srTenkaFunsai))); // 圧力(還)
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.SYUURYOU_SF1_DAY, srTenkaFunsai),
                "".equals(syuuryouTime) ? "0000" : syuuryouTime)); // 終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.TANTOUSYA_SF1, srTenkaFunsai))); // 担当者

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
     * 添加材ｽﾗﾘｰ作製・粉砕_仮登録(tmp_sub_sr_tenka_funsai)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srTenkaFunsai 添加材ｽﾗﾘｰ作製・粉砕データ
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSubSrTenkaFunsai2(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, String systemTime, ProcessData processData, SrTenkaFunsai srTenkaFunsai) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // 開始日時
        String kaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B011Const.KAISI_SF2_TIME, srTenkaFunsai));
        // 終了日時
        String syuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B011Const.SYUURYOU_SF2_TIME, srTenkaFunsai));
        // 終了予定日時
        String syuuryouyoteiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B011Const.SYUURYOUYOTEI_SF2_TIME, srTenkaFunsai));

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B011Const.PASSKAISUU_SF2, srTenkaFunsai))); // ﾊﾟｽ回数
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.KAISI_SF2_DAY, srTenkaFunsai),
                "".equals(kaisiTime) ? "0000" : kaisiTime)); // 開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.SYUURYOUYOTEI_SF2_DAY, srTenkaFunsai),
                "".equals(syuuryouyoteiTime) ? "0000" : syuuryouyoteiTime)); // 終了予定日時
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.FUKADENRYUUTI_SF2, srTenkaFunsai))); // 負荷電流値
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.SEIHINONDO_SF2, srTenkaFunsai))); // 製品温度
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.STICKERONDO_SF2, srTenkaFunsai))); // ｼｰﾙ温度
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.PUMPMEMORI_SF2, srTenkaFunsai))); // ﾎﾟﾝﾌﾟ目盛
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.PUMPATU_SF2, srTenkaFunsai))); // ﾎﾟﾝﾌﾟ圧
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.RYUURYOU_SF2, srTenkaFunsai))); // 流量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.BIKOU1_SF2, srTenkaFunsai))); // 備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.BIKOU2_SF2, srTenkaFunsai))); // 備考2
        params.add(null); // 温度(往)
        params.add(null); // 温度(還)
        params.add(null); // 圧力(往)
        params.add(null); // 圧力(還)
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.SYUURYOU_SF2_DAY, srTenkaFunsai),
                "".equals(syuuryouTime) ? "0000" : syuuryouTime)); // 終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B011Const.TANTOUSYA_SF2, srTenkaFunsai))); // 担当者

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
     * 添加材ｽﾗﾘｰ作製・粉砕(sr_tenka_funsai)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param tmpSrTenkaFunsai 仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrTenkaFunsai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData, SrTenkaFunsai tmpSrTenkaFunsai) throws SQLException {

        String sql = "INSERT INTO sr_tenka_funsai ( "
                + "kojyo,lotno,edaban,tenkazaislurryhinmei,tenkazaislurrylotno,lotkubun,hyouryougouki,funsaiki,funsaigouki,"
                + "medialotno,renzokuuntenkaisuu,tounyuuryou,jikan_passkaisuu,millsyuuhasuu,syuusoku,pumpsyuturyokcheck,"
                + "ryuuryou,passkaisuu,dispanosyurui,dispakaitensuu,youzai1_zairyouhinmei,youzai1_tyougouryoukikaku,"
                + "youzai1_buzaizaikolotno1,youzai1_tyougouryou1,youzai1_buzaizaikolotno2,youzai1_tyougouryou2,youzai2_zairyouhinmei,"
                + "youzai2_tyougouryoukikaku,youzai2_buzaizaikolotno1,youzai2_tyougouryou1,youzai2_buzaizaikolotno2,"
                + "youzai2_tyougouryou2,tantousya,pumpsyuturyoku,millsyuuhasuu2,kisyakuyouzaitenka,youzaijyunkanjikan,"
                + "jyunkankaisinichiji,jyunkansyuuryounichiji,jyunkantantousya,torokunichiji,kosinnichiji,revision"
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrTenkaFunsai(true, newRev, kojyo, lotNo, edaban, systemTime, processData, tmpSrTenkaFunsai);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 添加材ｽﾗﾘｰ作製・粉砕(sub_sr_tenka_funsai)登録処理
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
    private void insertSubSrTenkaFunsai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData, SrTenkaFunsai tmpSrTenkaFunsai) throws SQLException {

        String passkaisuu = StringUtil.nullToBlank(getItemKikakuchi(processData.getItemList(), GXHDO102B011Const.PASSKAISUU_SF1, tmpSrTenkaFunsai));
        if ("".equals(passkaisuu)) {
            return;
        }
        String sql = "INSERT INTO sub_sr_tenka_funsai ( "
                + "kojyo,lotno,edaban,passkaisuu,kaisinichiji,syuuryouyoteinichiji,fukadenryuuti,seihinondo,stickerondo,pumpmemori,"
                + "pumpatu,ryuuryou,bikou1,bikou2,ondo_ou,ondo_kan,aturyoku_ou,aturyoku_kan,syuuryounichiji,tantousya,torokunichiji,"
                + "kosinnichiji,revision"
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSubSrTenkaFunsai(true, newRev, kojyo, lotNo, edaban, systemTime, processData, tmpSrTenkaFunsai);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 添加材ｽﾗﾘｰ作製・粉砕(sub_sr_tenka_funsai)登録処理
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
    private void insertSubSrTenkaFunsai2(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData, SrTenkaFunsai tmpSrTenkaFunsai) throws SQLException {

        String passkaisuu = StringUtil.nullToBlank(getItemKikakuchi(processData.getItemList(), GXHDO102B011Const.PASSKAISUU_SF2, tmpSrTenkaFunsai));
        if ("".equals(passkaisuu)) {
            return;
        }
        String sql = "INSERT INTO sub_sr_tenka_funsai ( "
                + "kojyo,lotno,edaban,passkaisuu,kaisinichiji,syuuryouyoteinichiji,fukadenryuuti,seihinondo,stickerondo,pumpmemori,"
                + "pumpatu,ryuuryou,bikou1,bikou2,ondo_ou,ondo_kan,aturyoku_ou,aturyoku_kan,syuuryounichiji,tantousya,torokunichiji,"
                + "kosinnichiji,revision"
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSubSrTenkaFunsai2(true, newRev, kojyo, lotNo, edaban, systemTime, processData, tmpSrTenkaFunsai);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 添加材ｽﾗﾘｰ作製・粉砕(sr_tenka_funsai)更新処理
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
    private void updateSrTenkaFunsai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {
        String sql = "UPDATE sr_tenka_funsai SET "
                + "tenkazaislurryhinmei = ?,tenkazaislurrylotno = ?,lotkubun = ?,hyouryougouki = ?,funsaiki = ?,funsaigouki = ?,medialotno = ?,"
                + "renzokuuntenkaisuu = ?,tounyuuryou = ?,jikan_passkaisuu = ?,millsyuuhasuu = ?,syuusoku = ?,pumpsyuturyokcheck = ?,ryuuryou = ?,"
                + "passkaisuu = ?,dispanosyurui = ?,dispakaitensuu = ?,youzai1_zairyouhinmei = ?,youzai1_tyougouryoukikaku = ?,youzai1_buzaizaikolotno1 = ?,"
                + "youzai1_tyougouryou1 = ?,youzai1_buzaizaikolotno2 = ?,youzai1_tyougouryou2 = ?,youzai2_zairyouhinmei = ?,youzai2_tyougouryoukikaku = ?,"
                + "youzai2_buzaizaikolotno1 = ?,youzai2_tyougouryou1 = ?,youzai2_buzaizaikolotno2 = ?,youzai2_tyougouryou2 = ?,tantousya = ?,"
                + "pumpsyuturyoku = ?,millsyuuhasuu2 = ?,kisyakuyouzaitenka = ?,youzaijyunkanjikan = ?,jyunkankaisinichiji = ?,jyunkansyuuryounichiji = ?,"
                + "jyunkantantousya = ?,kosinnichiji = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrTenkaFunsai> srTenkaFunsaiList = getSrTenkaFunsaiData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrTenkaFunsai srTenkaFunsai = null;
        if (!srTenkaFunsaiList.isEmpty()) {
            srTenkaFunsai = srTenkaFunsaiList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrTenkaFunsai(false, newRev, "", "", "", systemTime, processData, srTenkaFunsai);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 添加材ｽﾗﾘｰ作製・粉砕(sub_sr_tenka_funsai)更新処理
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
    private void updateSubSrTenkaFunsai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, String passkaisuu, ProcessData processData) throws SQLException {

        if ("".equals(passkaisuu)) {
            return;
        }
        String sql = "UPDATE sub_sr_tenka_funsai SET "
                + "passkaisuu = ?,kaisinichiji = ?,syuuryouyoteinichiji = ?,fukadenryuuti = ?,seihinondo = ?,stickerondo = ?,pumpmemori = ?,pumpatu = ?,"
                + "ryuuryou = ?,bikou1 = ?,bikou2 = ?,ondo_ou = ?,ondo_kan = ?,aturyoku_ou = ?,aturyoku_kan = ?,syuuryounichiji = ?,tantousya = ?,"
                + "kosinnichiji = ?,revision = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND passkaisuu = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrTenkaFunsai> srTenkaFunsaiList = getSrTenkaFunsaiData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrTenkaFunsai srTenkaFunsai = null;
        if (!srTenkaFunsaiList.isEmpty()) {
            srTenkaFunsai = srTenkaFunsaiList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSubSrTenkaFunsai(false, newRev, "", "", "", systemTime, processData, srTenkaFunsai);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(passkaisuu);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 添加材ｽﾗﾘｰ作製・粉砕(sub_sr_tenka_funsai)更新処理
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
    private void updateSubSrTenkaFunsai2(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, String passkaisuu, ProcessData processData) throws SQLException {

        if ("".equals(passkaisuu)) {
            return;
        }
        String sql = "UPDATE sub_sr_tenka_funsai SET "
                + "passkaisuu = ?,kaisinichiji = ?,syuuryouyoteinichiji = ?,fukadenryuuti = ?,seihinondo = ?,stickerondo = ?,pumpmemori = ?,pumpatu = ?,"
                + "ryuuryou = ?,bikou1 = ?,bikou2 = ?,ondo_ou = ?,ondo_kan = ?,aturyoku_ou = ?,aturyoku_kan = ?,syuuryounichiji = ?,tantousya = ?,"
                + "kosinnichiji = ?,revision = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND passkaisuu = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrTenkaFunsai> srTenkaFunsaiList = getSubSrTenkaFunsaiData(queryRunnerQcdb, kojyo, lotNo, edaban, rev.toPlainString(), passkaisuu);
        SrTenkaFunsai srTenkaFunsai = null;
        if (!srTenkaFunsaiList.isEmpty()) {
            srTenkaFunsai = srTenkaFunsaiList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSubSrTenkaFunsai2(false, newRev, "", "", "", systemTime, processData, srTenkaFunsai);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(passkaisuu);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 添加材ｽﾗﾘｰ作製・粉砕(sr_tenka_funsai)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srTenkaFunsai 添加材ｽﾗﾘｰ作製・粉砕データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrTenkaFunsai(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            String systemTime, ProcessData processData, SrTenkaFunsai srTenkaFunsai) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // 循環開始日時
        String jyunkankaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B011Const.JYUNKANKAISI_TIME, srTenkaFunsai));
        // 循環終了日時
        String jyunkansyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B011Const.JYUNKANSYUURYOU_TIME, srTenkaFunsai));

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B011Const.TENKAZAISLURRYHINMEI, srTenkaFunsai))); // 添加材ｽﾗﾘｰ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B011Const.TENKAZAISLURRYLOTNO, srTenkaFunsai))); // 添加材ｽﾗﾘｰLotNo
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B011Const.LOTKUBUN, srTenkaFunsai))); // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B011Const.HYOURYOUGOUKI, srTenkaFunsai))); // 秤量号機
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B011Const.FUNSAIKI, srTenkaFunsai), 9)); // 粉砕機
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B011Const.FUNSAIGOUKI, srTenkaFunsai), 9)); // 粉砕号機
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B011Const.MEDIALOTNO, srTenkaFunsai))); // ﾒﾃﾞｨｱLotNo
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B011Const.RENZOKUUNTENKAISUU, srTenkaFunsai), 9)); // 連続運転回数
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B011Const.TOUNYUURYOU, srTenkaFunsai), 9)); // 投入量
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B011Const.JIKAN_PASSKAISUU, srTenkaFunsai), 9)); // 時間/ﾊﾟｽ回数
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B011Const.MILLSYUUHASUU, srTenkaFunsai), 9)); // ﾐﾙ周波数
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B011Const.SYUUSOKU, srTenkaFunsai), 9)); // 周速
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B011Const.PUMPSYUTURYOKCHECK, srTenkaFunsai), 9)); // ﾎﾟﾝﾌﾟ出力
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B011Const.RYUURYOU, srTenkaFunsai), 9)); // 流量
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B011Const.PASSKAISUU, srTenkaFunsai), 9)); // ﾊﾟｽ回数
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B011Const.DISPANOSYURUI, srTenkaFunsai), 9)); // ﾃﾞｨｽﾊﾟの種類
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B011Const.DISPAKAITENSUU, srTenkaFunsai), 9)); // ﾃﾞｨｽﾊﾟ回転数
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B011Const.YOUZAI1_ZAIRYOUHINMEI, srTenkaFunsai))); // 溶剤①_材料品名
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B011Const.YOUZAI1_TYOUGOURYOUKIKAKU, srTenkaFunsai))); // 溶剤①_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B011Const.YOUZAI1_BUZAIZAIKOLOTNO1, srTenkaFunsai))); // 溶剤①_部材在庫No1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B011Const.YOUZAI1_TYOUGOURYOU1, srTenkaFunsai))); // 溶剤①_調合量1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B011Const.YOUZAI1_BUZAIZAIKOLOTNO2, srTenkaFunsai))); // 溶剤①_部材在庫No2
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B011Const.YOUZAI1_TYOUGOURYOU2, srTenkaFunsai))); // 溶剤①_調合量2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B011Const.YOUZAI2_ZAIRYOUHINMEI, srTenkaFunsai))); // 溶剤②_材料品名
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B011Const.YOUZAI2_TYOUGOURYOUKIKAKU, srTenkaFunsai))); // 溶剤②_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B011Const.YOUZAI2_BUZAIZAIKOLOTNO1, srTenkaFunsai))); // 溶剤②_部材在庫No1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B011Const.YOUZAI2_TYOUGOURYOU1, srTenkaFunsai))); // 溶剤②_調合量1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B011Const.YOUZAI2_BUZAIZAIKOLOTNO2, srTenkaFunsai))); // 溶剤②_部材在庫No2
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B011Const.YOUZAI2_TYOUGOURYOU2, srTenkaFunsai))); // 溶剤②_調合量2
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B011Const.TANTOUSYA, srTenkaFunsai))); // 担当者
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B011Const.PUMPSYUTURYOKU, srTenkaFunsai))); // ﾎﾟﾝﾌﾟ出力
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B011Const.MILLSYUUHASUU2, srTenkaFunsai))); // ﾐﾙ周波数
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B011Const.KISYAKUYOUZAITENKA, srTenkaFunsai), 9)); // 希釈溶剤添加
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B011Const.YOUZAIJYUNKANJIKAN, srTenkaFunsai))); // 溶剤循環時間
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B011Const.JYUNKANKAISI_DAY, srTenkaFunsai),
                "".equals(jyunkankaisiTime) ? "0000" : jyunkankaisiTime)); // 循環開始日時
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B011Const.JYUNKANSYUURYOU_DAY, srTenkaFunsai),
                "".equals(jyunkansyuuryouTime) ? "0000" : jyunkansyuuryouTime)); // 循環終了日時
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B011Const.JYUNKANTANTOUSYA, srTenkaFunsai))); // 担当者

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
     * 添加材ｽﾗﾘｰ作製・粉砕_登録(sub_sr_tenka_funsai)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srTenkaFunsai 添加材ｽﾗﾘｰ作製・粉砕データ
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSubSrTenkaFunsai(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            String systemTime, ProcessData processData, SrTenkaFunsai srTenkaFunsai) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // 開始日時
        String kaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B011Const.KAISI_SF1_TIME, srTenkaFunsai));
        // 終了日時
        String syuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B011Const.SYUURYOU_SF1_TIME, srTenkaFunsai));
        // 終了予定日時
        String syuuryouyoteiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B011Const.SYUURYOUYOTEI_SF1_TIME, srTenkaFunsai));

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToIntObject(getItemKikakuchi(pItemList, GXHDO102B011Const.PASSKAISUU_SF1, srTenkaFunsai))); // ﾊﾟｽ回数
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B011Const.KAISI_SF1_DAY, srTenkaFunsai),
                "".equals(kaisiTime) ? "0000" : kaisiTime)); // 開始日時
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B011Const.SYUURYOUYOTEI_SF1_DAY, srTenkaFunsai),
                "".equals(syuuryouyoteiTime) ? "0000" : syuuryouyoteiTime)); // 終了予定日時
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B011Const.FUKADENRYUUTI_SF1, srTenkaFunsai))); // 負荷電流値
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B011Const.SEIHINONDO_SF1, srTenkaFunsai))); // 製品温度
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B011Const.STICKERONDO_SF1, srTenkaFunsai))); // ｼｰﾙ温度
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B011Const.PUMPMEMORI_SF1, srTenkaFunsai))); // ﾎﾟﾝﾌﾟ目盛
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B011Const.PUMPATU_SF1, srTenkaFunsai))); // ﾎﾟﾝﾌﾟ圧
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B011Const.RYUURYOU_SF1, srTenkaFunsai))); // 流量
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B011Const.BIKOU1_SF1, srTenkaFunsai))); // 備考1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B011Const.BIKOU2_SF1, srTenkaFunsai))); // 備考2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B011Const.ONDO_OU_SF1, srTenkaFunsai))); // 温度(往)
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B011Const.ONDO_KAN_SF1, srTenkaFunsai))); // 温度(還)
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B011Const.ATURYOKU_OU_SF1, srTenkaFunsai))); // 圧力(往)
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B011Const.ATURYOKU_KAN_SF1, srTenkaFunsai))); // 圧力(還)
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B011Const.SYUURYOU_SF1_DAY, srTenkaFunsai),
                "".equals(syuuryouTime) ? "0000" : syuuryouTime)); // 終了日時
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B011Const.TANTOUSYA_SF1, srTenkaFunsai))); // 担当者

        if (isInsert) {
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時
        } else {
            params.add(systemTime); //更新日時
        }
        params.add(newRev);         //revision

        return params;
    }

    /**
     * 添加材ｽﾗﾘｰ作製・粉砕_登録(sub_sr_tenka_funsai)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srTenkaFunsai 添加材ｽﾗﾘｰ作製・粉砕データ
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSubSrTenkaFunsai2(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            String systemTime, ProcessData processData, SrTenkaFunsai srTenkaFunsai) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // 開始日時
        String kaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B011Const.KAISI_SF2_TIME, srTenkaFunsai));
        // 終了日時
        String syuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B011Const.SYUURYOU_SF2_TIME, srTenkaFunsai));
        // 終了予定日時
        String syuuryouyoteiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B011Const.SYUURYOUYOTEI_SF2_TIME, srTenkaFunsai));

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToIntObject(getItemKikakuchi(pItemList, GXHDO102B011Const.PASSKAISUU_SF2, srTenkaFunsai))); // ﾊﾟｽ回数
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B011Const.KAISI_SF2_DAY, srTenkaFunsai),
                "".equals(kaisiTime) ? "0000" : kaisiTime)); // 開始日時
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B011Const.SYUURYOUYOTEI_SF2_DAY, srTenkaFunsai),
                "".equals(syuuryouyoteiTime) ? "0000" : syuuryouyoteiTime)); // 終了予定日時
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B011Const.FUKADENRYUUTI_SF2, srTenkaFunsai))); // 負荷電流値
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B011Const.SEIHINONDO_SF2, srTenkaFunsai))); // 製品温度
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B011Const.STICKERONDO_SF2, srTenkaFunsai))); // ｼｰﾙ温度
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B011Const.PUMPMEMORI_SF2, srTenkaFunsai))); // ﾎﾟﾝﾌﾟ目盛
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B011Const.PUMPATU_SF2, srTenkaFunsai))); // ﾎﾟﾝﾌﾟ圧
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B011Const.RYUURYOU_SF2, srTenkaFunsai))); // 流量
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B011Const.BIKOU1_SF2, srTenkaFunsai))); // 備考1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B011Const.BIKOU2_SF2, srTenkaFunsai))); // 備考2
        params.add(0); // 温度(往)
        params.add(0); // 温度(還)
        params.add(0); // 圧力(往)
        params.add(0); // 圧力(還)
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B011Const.SYUURYOU_SF2_DAY, srTenkaFunsai),
                "".equals(syuuryouTime) ? "0000" : syuuryouTime)); // 終了日時
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B011Const.TANTOUSYA_SF2, srTenkaFunsai))); // 担当者

        if (isInsert) {
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時
        } else {
            params.add(systemTime); //更新日時
        }
        params.add(newRev);         //revision

        return params;
    }

    /**
     * 添加材ｽﾗﾘｰ作製・粉砕(sr_tenka_funsai)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSrTenkaFunsai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sr_tenka_funsai "
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
     * 添加材ｽﾗﾘｰ作製・粉砕(sr_tenka_funsai)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSubSrTenkaFunsai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, String passkaisuu) throws SQLException {

        String sql = "DELETE FROM sub_sr_tenka_funsai "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND passkaisuu = ? AND revision = ? ";

        //更新値設定
        List<Object> params = new ArrayList<>();

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(passkaisuu);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * [添加材ｽﾗﾘｰ作製・粉砕_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_tenka_funsai "
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
     * 終了予定日付(日、時間)の項目にフォーマットの日付(yyMMdd,HHmm)をセットする
     *
     * @param processData 処理制御データ
     * @param itemDay 項目日付(日)
     * @param itemTime 項目日付(時間)
     */
    private void setYoteiDateTimeItem(ProcessData processData, FXHDD01 itemDay, FXHDD01 itemTime) {
        try {
            FXHDD01 itemJikan_passkaisuu = getItemRow(processData.getItemList(), GXHDO102B011Const.JIKAN_PASSKAISUU); // 時間/ﾊﾟｽ回数
            FXHDD01 itemPasskaisuu_sf1 = getItemRow(processData.getItemList(), GXHDO102B011Const.PASSKAISUU_SF1); // ﾊﾟｽ回数
            FXHDD01 itemSyuuryouyotei_sf1_day = getItemRow(processData.getItemList(), GXHDO102B011Const.SYUURYOUYOTEI_SF1_DAY); // 終了予定日
            FXHDD01 itemSyuuryouyotei_sf1_time = getItemRow(processData.getItemList(), GXHDO102B011Const.SYUURYOUYOTEI_SF1_TIME); // 終了予定時間
            BigDecimal jikan_passkaisuuBdVal = ValidateUtil.getItemKikakuChiCheckVal(itemJikan_passkaisuu); // 時間/ﾊﾟｽ回数の規格値
            BigDecimal passkaisuu_sf1BdVal = ValidateUtil.getItemKikakuChiCheckVal(itemPasskaisuu_sf1); // ﾊﾟｽ回数の規格値
            if (jikan_passkaisuuBdVal == null || passkaisuu_sf1BdVal == null) {
                return;
            }
            int jikan_passkaisuuVal = jikan_passkaisuuBdVal.intValue();
            int passkaisuu_sf1Val = passkaisuu_sf1BdVal.intValue();
            Date dateTime = DateUtil.addJikan(itemDay.getValue(), itemTime.getValue(), jikan_passkaisuuVal * passkaisuu_sf1Val, Calendar.MINUTE);
            if (dateTime != null) {
                setDateTimeItem(itemSyuuryouyotei_sf1_day, itemSyuuryouyotei_sf1_time, dateTime);
            }
        } catch (NumberFormatException ex) {
            ErrUtil.outputErrorLog("ParseException発生", ex, LOGGER);
        }
    }

    /**
     * 項目IDに該当するDBの値を取得する。
     *
     * @param itemId 項目ID
     * @param SrTenkaFunsai 添加材ｽﾗﾘｰ作製・粉砕データ
     * @return DB値
     */
    private String getSrTenkaFunsaiItemData(String itemId, SrTenkaFunsai srTenkaFunsai) {
        switch (itemId) {
            // 添加材ｽﾗﾘｰ品名
            case GXHDO102B011Const.TENKAZAISLURRYHINMEI:
                return StringUtil.nullToBlank(srTenkaFunsai.getTenkazaislurryhinmei());
            // 添加材ｽﾗﾘｰLotNo
            case GXHDO102B011Const.TENKAZAISLURRYLOTNO:
                return StringUtil.nullToBlank(srTenkaFunsai.getTenkazaislurrylotno());
            // ﾛｯﾄ区分
            case GXHDO102B011Const.LOTKUBUN:
                return StringUtil.nullToBlank(srTenkaFunsai.getLotkubun());
            // 秤量号機
            case GXHDO102B011Const.HYOURYOUGOUKI:
                return StringUtil.nullToBlank(srTenkaFunsai.getHyouryougouki());
            // 粉砕機
            case GXHDO102B011Const.FUNSAIKI:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srTenkaFunsai.getFunsaiki()));
            // 粉砕号機
            case GXHDO102B011Const.FUNSAIGOUKI:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srTenkaFunsai.getFunsaigouki()));
            // ﾒﾃﾞｨｱLotNo
            case GXHDO102B011Const.MEDIALOTNO:
                return StringUtil.nullToBlank(srTenkaFunsai.getMedialotno());
            // 連続運転回数
            case GXHDO102B011Const.RENZOKUUNTENKAISUU:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srTenkaFunsai.getRenzokuuntenkaisuu()));
            // 投入量
            case GXHDO102B011Const.TOUNYUURYOU:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srTenkaFunsai.getTounyuuryou()));
            // 時間/ﾊﾟｽ回数
            case GXHDO102B011Const.JIKAN_PASSKAISUU:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srTenkaFunsai.getJikan_passkaisuu()));
            // ﾐﾙ周波数
            case GXHDO102B011Const.MILLSYUUHASUU:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srTenkaFunsai.getMillsyuuhasuu()));
            // 周速
            case GXHDO102B011Const.SYUUSOKU:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srTenkaFunsai.getSyuusoku()));
            // ﾎﾟﾝﾌﾟ出力
            case GXHDO102B011Const.PUMPSYUTURYOKCHECK:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srTenkaFunsai.getPumpsyuturyokcheck()));
            // 流量
            case GXHDO102B011Const.RYUURYOU:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srTenkaFunsai.getRyuuryou()));
            // ﾊﾟｽ回数
            case GXHDO102B011Const.PASSKAISUU:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srTenkaFunsai.getPasskaisuu()));
            // ﾃﾞｨｽﾊﾟの種類
            case GXHDO102B011Const.DISPANOSYURUI:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srTenkaFunsai.getDispanosyurui()));
            // ﾃﾞｨｽﾊﾟ回転数
            case GXHDO102B011Const.DISPAKAITENSUU:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srTenkaFunsai.getDispakaitensuu()));
            // ﾊﾟｽ回数
            case GXHDO102B011Const.PASSKAISUU_SF1:
                return StringUtil.nullToBlank(srTenkaFunsai.getPasskaisuu_sf1());
            // 開始日
            case GXHDO102B011Const.KAISI_SF1_DAY:
                return DateUtil.formattedTimestamp(srTenkaFunsai.getKaisinichiji_sf1(), "yyMMdd");
            // 開始時間
            case GXHDO102B011Const.KAISI_SF1_TIME:
                return DateUtil.formattedTimestamp(srTenkaFunsai.getKaisinichiji_sf1(), "HHmm");
            // 終了予定日
            case GXHDO102B011Const.SYUURYOUYOTEI_SF1_DAY:
                return DateUtil.formattedTimestamp(srTenkaFunsai.getSyuuryouyoteinichiji_sf1(), "yyMMdd");
            // 終了予定時間
            case GXHDO102B011Const.SYUURYOUYOTEI_SF1_TIME:
                return DateUtil.formattedTimestamp(srTenkaFunsai.getSyuuryouyoteinichiji_sf1(), "HHmm");
            // 負荷電流値
            case GXHDO102B011Const.FUKADENRYUUTI_SF1:
                return StringUtil.nullToBlank(srTenkaFunsai.getFukadenryuuti_sf1());
            // 製品温度
            case GXHDO102B011Const.SEIHINONDO_SF1:
                return StringUtil.nullToBlank(srTenkaFunsai.getSeihinondo_sf1());
            // ｼｰﾙ温度
            case GXHDO102B011Const.STICKERONDO_SF1:
                return StringUtil.nullToBlank(srTenkaFunsai.getStickerondo_sf1());
            // ﾎﾟﾝﾌﾟ目盛
            case GXHDO102B011Const.PUMPMEMORI_SF1:
                return StringUtil.nullToBlank(srTenkaFunsai.getPumpmemori_sf1());
            // ﾎﾟﾝﾌﾟ圧
            case GXHDO102B011Const.PUMPATU_SF1:
                return StringUtil.nullToBlank(srTenkaFunsai.getPumpatu_sf1());
            // 流量
            case GXHDO102B011Const.RYUURYOU_SF1:
                return StringUtil.nullToBlank(srTenkaFunsai.getRyuuryou_sf1());
            // 備考1
            case GXHDO102B011Const.BIKOU1_SF1:
                return StringUtil.nullToBlank(srTenkaFunsai.getBikou1_sf1());
            // 備考2
            case GXHDO102B011Const.BIKOU2_SF1:
                return StringUtil.nullToBlank(srTenkaFunsai.getBikou2_sf1());
            // 温度(往)
            case GXHDO102B011Const.ONDO_OU_SF1:
                return StringUtil.nullToBlank(srTenkaFunsai.getOndo_ou_sf1());
            // 温度(還)
            case GXHDO102B011Const.ONDO_KAN_SF1:
                return StringUtil.nullToBlank(srTenkaFunsai.getOndo_kan_sf1());
            // 圧力(往)
            case GXHDO102B011Const.ATURYOKU_OU_SF1:
                return StringUtil.nullToBlank(srTenkaFunsai.getAturyoku_ou_sf1());
            // 圧力(還)
            case GXHDO102B011Const.ATURYOKU_KAN_SF1:
                return StringUtil.nullToBlank(srTenkaFunsai.getAturyoku_kan_sf1());
            // 終了日
            case GXHDO102B011Const.SYUURYOU_SF1_DAY:
                return DateUtil.formattedTimestamp(srTenkaFunsai.getSyuuryounichiji_sf1(), "yyMMdd");
            // 終了時間
            case GXHDO102B011Const.SYUURYOU_SF1_TIME:
                return DateUtil.formattedTimestamp(srTenkaFunsai.getSyuuryounichiji_sf1(), "HHmm");
            // 担当者
            case GXHDO102B011Const.TANTOUSYA_SF1:
                return StringUtil.nullToBlank(srTenkaFunsai.getTantousya_sf1());
            // ﾊﾟｽ回数
            case GXHDO102B011Const.PASSKAISUU_SF2:
                return StringUtil.nullToBlank(srTenkaFunsai.getPasskaisuu_sf2());
            // 開始日
            case GXHDO102B011Const.KAISI_SF2_DAY:
                return DateUtil.formattedTimestamp(srTenkaFunsai.getKaisinichiji_sf2(), "yyMMdd");
            // 開始時間
            case GXHDO102B011Const.KAISI_SF2_TIME:
                return DateUtil.formattedTimestamp(srTenkaFunsai.getKaisinichiji_sf2(), "HHmm");
            // 終了予定日
            case GXHDO102B011Const.SYUURYOUYOTEI_SF2_DAY:
                return DateUtil.formattedTimestamp(srTenkaFunsai.getSyuuryouyoteinichiji_sf2(), "yyMMdd");
            // 終了予定時間
            case GXHDO102B011Const.SYUURYOUYOTEI_SF2_TIME:
                return DateUtil.formattedTimestamp(srTenkaFunsai.getSyuuryouyoteinichiji_sf2(), "HHmm");
            // 負荷電流値
            case GXHDO102B011Const.FUKADENRYUUTI_SF2:
                return StringUtil.nullToBlank(srTenkaFunsai.getFukadenryuuti_sf2());
            // 製品温度
            case GXHDO102B011Const.SEIHINONDO_SF2:
                return StringUtil.nullToBlank(srTenkaFunsai.getSeihinondo_sf2());
            // ｼｰﾙ温度
            case GXHDO102B011Const.STICKERONDO_SF2:
                return StringUtil.nullToBlank(srTenkaFunsai.getStickerondo_sf2());
            // ﾎﾟﾝﾌﾟ目盛
            case GXHDO102B011Const.PUMPMEMORI_SF2:
                return StringUtil.nullToBlank(srTenkaFunsai.getPumpmemori_sf2());
            // ﾎﾟﾝﾌﾟ圧
            case GXHDO102B011Const.PUMPATU_SF2:
                return StringUtil.nullToBlank(srTenkaFunsai.getPumpatu_sf2());
            // 終了日
            case GXHDO102B011Const.SYUURYOU_SF2_DAY:
                return DateUtil.formattedTimestamp(srTenkaFunsai.getSyuuryounichiji_sf2(), "yyMMdd");
            // 終了時間
            case GXHDO102B011Const.SYUURYOU_SF2_TIME:
                return DateUtil.formattedTimestamp(srTenkaFunsai.getSyuuryounichiji_sf2(), "HHmm");
            // 流量
            case GXHDO102B011Const.RYUURYOU_SF2:
                return StringUtil.nullToBlank(srTenkaFunsai.getRyuuryou_sf2());
            // 備考1
            case GXHDO102B011Const.BIKOU1_SF2:
                return StringUtil.nullToBlank(srTenkaFunsai.getBikou1_sf2());
            // 備考2
            case GXHDO102B011Const.BIKOU2_SF2:
                return StringUtil.nullToBlank(srTenkaFunsai.getBikou2_sf2());
            // 担当者
            case GXHDO102B011Const.TANTOUSYA_SF2:
                return StringUtil.nullToBlank(srTenkaFunsai.getTantousya_sf2());
            // 溶剤①_材料品名
            case GXHDO102B011Const.YOUZAI1_ZAIRYOUHINMEI:
                return StringUtil.nullToBlank(srTenkaFunsai.getYouzai1_zairyouhinmei());
            // 溶剤①_調合量規格
            case GXHDO102B011Const.YOUZAI1_TYOUGOURYOUKIKAKU:
                return StringUtil.nullToBlank(srTenkaFunsai.getYouzai1_tyougouryoukikaku());
            // 溶剤①_部材在庫No1
            case GXHDO102B011Const.YOUZAI1_BUZAIZAIKOLOTNO1:
                return StringUtil.nullToBlank(srTenkaFunsai.getYouzai1_buzaizaikolotno1());
            // 溶剤①_調合量1
            case GXHDO102B011Const.YOUZAI1_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srTenkaFunsai.getYouzai1_tyougouryou1());
            // 溶剤①_部材在庫No2
            case GXHDO102B011Const.YOUZAI1_BUZAIZAIKOLOTNO2:
                return StringUtil.nullToBlank(srTenkaFunsai.getYouzai1_buzaizaikolotno2());
            // 溶剤①_調合量2
            case GXHDO102B011Const.YOUZAI1_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srTenkaFunsai.getYouzai1_tyougouryou2());
            // 溶剤②_材料品名
            case GXHDO102B011Const.YOUZAI2_ZAIRYOUHINMEI:
                return StringUtil.nullToBlank(srTenkaFunsai.getYouzai2_zairyouhinmei());
            // 溶剤②_調合量規格
            case GXHDO102B011Const.YOUZAI2_TYOUGOURYOUKIKAKU:
                return StringUtil.nullToBlank(srTenkaFunsai.getYouzai2_tyougouryoukikaku());
            // 溶剤②_部材在庫No1
            case GXHDO102B011Const.YOUZAI2_BUZAIZAIKOLOTNO1:
                return StringUtil.nullToBlank(srTenkaFunsai.getYouzai2_buzaizaikolotno1());
            // 溶剤②_調合量1
            case GXHDO102B011Const.YOUZAI2_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srTenkaFunsai.getYouzai2_tyougouryou1());
            // 溶剤②_部材在庫No2
            case GXHDO102B011Const.YOUZAI2_BUZAIZAIKOLOTNO2:
                return StringUtil.nullToBlank(srTenkaFunsai.getYouzai2_buzaizaikolotno2());
            // 溶剤②_調合量2
            case GXHDO102B011Const.YOUZAI2_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srTenkaFunsai.getYouzai2_tyougouryou2());
            // 担当者
            case GXHDO102B011Const.TANTOUSYA:
                return StringUtil.nullToBlank(srTenkaFunsai.getTantousya());
            // ﾎﾟﾝﾌﾟ出力
            case GXHDO102B011Const.PUMPSYUTURYOKU:
                return StringUtil.nullToBlank(srTenkaFunsai.getPumpsyuturyoku());
            // ﾐﾙ周波数
            case GXHDO102B011Const.MILLSYUUHASUU2:
                return StringUtil.nullToBlank(srTenkaFunsai.getMillsyuuhasuu2());
            // 希釈溶剤添加
            case GXHDO102B011Const.KISYAKUYOUZAITENKA:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srTenkaFunsai.getKisyakuyouzaitenka()));
            // 溶剤循環時間
            case GXHDO102B011Const.YOUZAIJYUNKANJIKAN:
                return StringUtil.nullToBlank(srTenkaFunsai.getYouzaijyunkanjikan());
            // 循環開始日
            case GXHDO102B011Const.JYUNKANKAISI_DAY:
                return DateUtil.formattedTimestamp(srTenkaFunsai.getJyunkankaisinichiji(), "yyMMdd");
            // 循環開始時間
            case GXHDO102B011Const.JYUNKANKAISI_TIME:
                return DateUtil.formattedTimestamp(srTenkaFunsai.getJyunkankaisinichiji(), "HHmm");
            // 循環終了日
            case GXHDO102B011Const.JYUNKANSYUURYOU_DAY:
                return DateUtil.formattedTimestamp(srTenkaFunsai.getJyunkansyuuryounichiji(), "yyMMdd");
            // 循環終了時間
            case GXHDO102B011Const.JYUNKANSYUURYOU_TIME:
                return DateUtil.formattedTimestamp(srTenkaFunsai.getJyunkansyuuryounichiji(), "HHmm");
            // 担当者
            case GXHDO102B011Const.JYUNKANTANTOUSYA:
                return StringUtil.nullToBlank(srTenkaFunsai.getJyunkantantousya());
            default:
                return null;
        }
    }

    /**
     * 添加材ｽﾗﾘｰ作製・粉砕_仮登録(tmp_sr_tenka_funsai)登録処理(削除時)
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
    private void insertDeleteDataTmpSrTenkaFunsai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_tenka_funsai ("
                + "kojyo,lotno,edaban,tenkazaislurryhinmei,tenkazaislurrylotno,lotkubun,hyouryougouki,funsaiki,funsaigouki,"
                + "medialotno,renzokuuntenkaisuu,tounyuuryou,jikan_passkaisuu,millsyuuhasuu,syuusoku,pumpsyuturyokcheck,"
                + "ryuuryou,passkaisuu,dispanosyurui,dispakaitensuu,youzai1_zairyouhinmei,youzai1_tyougouryoukikaku,"
                + "youzai1_buzaizaikolotno1,youzai1_tyougouryou1,youzai1_buzaizaikolotno2,youzai1_tyougouryou2,youzai2_zairyouhinmei,"
                + "youzai2_tyougouryoukikaku,youzai2_buzaizaikolotno1,youzai2_tyougouryou1,youzai2_buzaizaikolotno2,"
                + "youzai2_tyougouryou2,tantousya,pumpsyuturyoku,millsyuuhasuu2,kisyakuyouzaitenka,youzaijyunkanjikan,"
                + "jyunkankaisinichiji,jyunkansyuuryounichiji,jyunkantantousya,torokunichiji,kosinnichiji,revision,sakujyoflg "
                + ") SELECT "
                + "kojyo,lotno,edaban,tenkazaislurryhinmei,tenkazaislurrylotno,lotkubun,hyouryougouki,funsaiki,funsaigouki,"
                + "medialotno,renzokuuntenkaisuu,tounyuuryou,jikan_passkaisuu,millsyuuhasuu,syuusoku,pumpsyuturyokcheck,"
                + "ryuuryou,passkaisuu,dispanosyurui,dispakaitensuu,youzai1_zairyouhinmei,youzai1_tyougouryoukikaku,"
                + "youzai1_buzaizaikolotno1,youzai1_tyougouryou1,youzai1_buzaizaikolotno2,youzai1_tyougouryou2,youzai2_zairyouhinmei,"
                + "youzai2_tyougouryoukikaku,youzai2_buzaizaikolotno1,youzai2_tyougouryou1,youzai2_buzaizaikolotno2,"
                + "youzai2_tyougouryou2,tantousya,pumpsyuturyoku,millsyuuhasuu2,kisyakuyouzaitenka,youzaijyunkanjikan,"
                + "jyunkankaisinichiji,jyunkansyuuryounichiji,jyunkantantousya,?,?,?,?"
                + " FROM sr_tenka_funsai "
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
     * 添加材ｽﾗﾘｰ作製・粉砕入力_仮登録(tmp_sub_sr_tenka_funsai)登録処理(削除時)
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param passkaisuu ﾊﾟｽ回数
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void insertDeleteDataTmpSubSrTenkaFunsai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String passkaisuu, String systemTime) throws SQLException {

        if ("".equals(passkaisuu)) {
            return;
        }
        String sql = "INSERT INTO tmp_sub_sr_tenka_funsai ("
                + "kojyo,lotno,edaban,passkaisuu,kaisinichiji,syuuryouyoteinichiji,fukadenryuuti,seihinondo,stickerondo,"
                + "pumpmemori,pumpatu,ryuuryou,bikou1,bikou2,ondo_ou,ondo_kan,aturyoku_ou,aturyoku_kan,syuuryounichiji,"
                + "tantousya,torokunichiji,kosinnichiji,revision,sakujyoflg "
                + ") SELECT "
                + "kojyo,lotno,edaban,passkaisuu,kaisinichiji,syuuryouyoteinichiji,fukadenryuuti,seihinondo,stickerondo,"
                + "pumpmemori,pumpatu,ryuuryou,bikou1,bikou2,ondo_ou,ondo_kan,aturyoku_ou,aturyoku_kan,syuuryounichiji,"
                + "tantousya,?,?,?,? "
                + " FROM sub_sr_tenka_funsai "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND passkaisuu = ? ";

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
        params.add(passkaisuu); //ﾊﾟｽ回数

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
    private void initGXHDO102B011A(ProcessData processData) {
        GXHDO102B011A bean = (GXHDO102B011A) getFormBean("gXHDO102B011A");
        bean.setWiplotno(getItemRow(processData.getItemList(), GXHDO102B011Const.WIPLOTNO));
        bean.setTenkazaislurryhinmei(getItemRow(processData.getItemList(), GXHDO102B011Const.TENKAZAISLURRYHINMEI));
        bean.setTenkazaislurrylotno(getItemRow(processData.getItemList(), GXHDO102B011Const.TENKAZAISLURRYLOTNO));
        bean.setLotkubun(getItemRow(processData.getItemList(), GXHDO102B011Const.LOTKUBUN));
        bean.setHyouryougouki(getItemRow(processData.getItemList(), GXHDO102B011Const.HYOURYOUGOUKI));
        bean.setFunsaiki(getItemRow(processData.getItemList(), GXHDO102B011Const.FUNSAIKI));
        bean.setFunsaigouki(getItemRow(processData.getItemList(), GXHDO102B011Const.FUNSAIGOUKI));
        bean.setMedialotno(getItemRow(processData.getItemList(), GXHDO102B011Const.MEDIALOTNO));
        bean.setRenzokuuntenkaisuu(getItemRow(processData.getItemList(), GXHDO102B011Const.RENZOKUUNTENKAISUU));
        bean.setTounyuuryou(getItemRow(processData.getItemList(), GXHDO102B011Const.TOUNYUURYOU));
        bean.setJikan_passkaisuu(getItemRow(processData.getItemList(), GXHDO102B011Const.JIKAN_PASSKAISUU));
        bean.setMillsyuuhasuu(getItemRow(processData.getItemList(), GXHDO102B011Const.MILLSYUUHASUU));
        bean.setSyuusoku(getItemRow(processData.getItemList(), GXHDO102B011Const.SYUUSOKU));
        bean.setPumpsyuturyokcheck(getItemRow(processData.getItemList(), GXHDO102B011Const.PUMPSYUTURYOKCHECK));
        bean.setRyuuryou(getItemRow(processData.getItemList(), GXHDO102B011Const.RYUURYOU));
        bean.setPasskaisuu(getItemRow(processData.getItemList(), GXHDO102B011Const.PASSKAISUU));
        bean.setDispanosyurui(getItemRow(processData.getItemList(), GXHDO102B011Const.DISPANOSYURUI));
        bean.setDispakaitensuu(getItemRow(processData.getItemList(), GXHDO102B011Const.DISPAKAITENSUU));
        bean.setPasskaisuu_sf1(getItemRow(processData.getItemList(), GXHDO102B011Const.PASSKAISUU_SF1));
        bean.setKaisi_sf1_day(getItemRow(processData.getItemList(), GXHDO102B011Const.KAISI_SF1_DAY));
        bean.setKaisi_sf1_time(getItemRow(processData.getItemList(), GXHDO102B011Const.KAISI_SF1_TIME));
        bean.setSyuuryouyotei_sf1_day(getItemRow(processData.getItemList(), GXHDO102B011Const.SYUURYOUYOTEI_SF1_DAY));
        bean.setSyuuryouyotei_sf1_time(getItemRow(processData.getItemList(), GXHDO102B011Const.SYUURYOUYOTEI_SF1_TIME));
        bean.setFukadenryuuti_sf1(getItemRow(processData.getItemList(), GXHDO102B011Const.FUKADENRYUUTI_SF1));
        bean.setSeihinondo_sf1(getItemRow(processData.getItemList(), GXHDO102B011Const.SEIHINONDO_SF1));
        bean.setStickerondo_sf1(getItemRow(processData.getItemList(), GXHDO102B011Const.STICKERONDO_SF1));
        bean.setPumpmemori_sf1(getItemRow(processData.getItemList(), GXHDO102B011Const.PUMPMEMORI_SF1));
        bean.setPumpatu_sf1(getItemRow(processData.getItemList(), GXHDO102B011Const.PUMPATU_SF1));
        bean.setRyuuryou_sf1(getItemRow(processData.getItemList(), GXHDO102B011Const.RYUURYOU_SF1));
        bean.setBikou1_sf1(getItemRow(processData.getItemList(), GXHDO102B011Const.BIKOU1_SF1));
        bean.setBikou2_sf1(getItemRow(processData.getItemList(), GXHDO102B011Const.BIKOU2_SF1));
        bean.setOndo_ou_sf1(getItemRow(processData.getItemList(), GXHDO102B011Const.ONDO_OU_SF1));
        bean.setOndo_kan_sf1(getItemRow(processData.getItemList(), GXHDO102B011Const.ONDO_KAN_SF1));
        bean.setAturyoku_ou_sf1(getItemRow(processData.getItemList(), GXHDO102B011Const.ATURYOKU_OU_SF1));
        bean.setAturyoku_kan_sf1(getItemRow(processData.getItemList(), GXHDO102B011Const.ATURYOKU_KAN_SF1));
        bean.setSyuuryou_sf1_day(getItemRow(processData.getItemList(), GXHDO102B011Const.SYUURYOU_SF1_DAY));
        bean.setSyuuryou_sf1_time(getItemRow(processData.getItemList(), GXHDO102B011Const.SYUURYOU_SF1_TIME));
        bean.setTantousya_sf1(getItemRow(processData.getItemList(), GXHDO102B011Const.TANTOUSYA_SF1));
        bean.setPasskaisuu_sf2(getItemRow(processData.getItemList(), GXHDO102B011Const.PASSKAISUU_SF2));
        bean.setKaisi_sf2_day(getItemRow(processData.getItemList(), GXHDO102B011Const.KAISI_SF2_DAY));
        bean.setKaisi_sf2_time(getItemRow(processData.getItemList(), GXHDO102B011Const.KAISI_SF2_TIME));
        bean.setSyuuryouyotei_sf2_day(getItemRow(processData.getItemList(), GXHDO102B011Const.SYUURYOUYOTEI_SF2_DAY));
        bean.setSyuuryouyotei_sf2_time(getItemRow(processData.getItemList(), GXHDO102B011Const.SYUURYOUYOTEI_SF2_TIME));
        bean.setFukadenryuuti_sf2(getItemRow(processData.getItemList(), GXHDO102B011Const.FUKADENRYUUTI_SF2));
        bean.setSeihinondo_sf2(getItemRow(processData.getItemList(), GXHDO102B011Const.SEIHINONDO_SF2));
        bean.setStickerondo_sf2(getItemRow(processData.getItemList(), GXHDO102B011Const.STICKERONDO_SF2));
        bean.setPumpmemori_sf2(getItemRow(processData.getItemList(), GXHDO102B011Const.PUMPMEMORI_SF2));
        bean.setPumpatu_sf2(getItemRow(processData.getItemList(), GXHDO102B011Const.PUMPATU_SF2));
        bean.setSyuuryou_sf2_day(getItemRow(processData.getItemList(), GXHDO102B011Const.SYUURYOU_SF2_DAY));
        bean.setSyuuryou_sf2_time(getItemRow(processData.getItemList(), GXHDO102B011Const.SYUURYOU_SF2_TIME));
        bean.setRyuuryou_sf2(getItemRow(processData.getItemList(), GXHDO102B011Const.RYUURYOU_SF2));
        bean.setBikou1_sf2(getItemRow(processData.getItemList(), GXHDO102B011Const.BIKOU1_SF2));
        bean.setBikou2_sf2(getItemRow(processData.getItemList(), GXHDO102B011Const.BIKOU2_SF2));
        bean.setTantousya_sf2(getItemRow(processData.getItemList(), GXHDO102B011Const.TANTOUSYA_SF2));
        bean.setYouzai1_zairyouhinmei(getItemRow(processData.getItemList(), GXHDO102B011Const.YOUZAI1_ZAIRYOUHINMEI));
        bean.setYouzai1_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B011Const.YOUZAI1_TYOUGOURYOUKIKAKU));
        bean.setYouzai1_buzaizaikolotno1(getItemRow(processData.getItemList(), GXHDO102B011Const.YOUZAI1_BUZAIZAIKOLOTNO1));
        bean.setYouzai1_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B011Const.YOUZAI1_TYOUGOURYOU1));
        bean.setYouzai1_buzaizaikolotno2(getItemRow(processData.getItemList(), GXHDO102B011Const.YOUZAI1_BUZAIZAIKOLOTNO2));
        bean.setYouzai1_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B011Const.YOUZAI1_TYOUGOURYOU2));
        bean.setYouzai2_zairyouhinmei(getItemRow(processData.getItemList(), GXHDO102B011Const.YOUZAI2_ZAIRYOUHINMEI));
        bean.setYouzai2_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B011Const.YOUZAI2_TYOUGOURYOUKIKAKU));
        bean.setYouzai2_buzaizaikolotno1(getItemRow(processData.getItemList(), GXHDO102B011Const.YOUZAI2_BUZAIZAIKOLOTNO1));
        bean.setYouzai2_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B011Const.YOUZAI2_TYOUGOURYOU1));
        bean.setYouzai2_buzaizaikolotno2(getItemRow(processData.getItemList(), GXHDO102B011Const.YOUZAI2_BUZAIZAIKOLOTNO2));
        bean.setYouzai2_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B011Const.YOUZAI2_TYOUGOURYOU2));
        bean.setTantousya(getItemRow(processData.getItemList(), GXHDO102B011Const.TANTOUSYA));
        bean.setPumpsyuturyoku(getItemRow(processData.getItemList(), GXHDO102B011Const.PUMPSYUTURYOKU));
        bean.setMillsyuuhasuu2(getItemRow(processData.getItemList(), GXHDO102B011Const.MILLSYUUHASUU2));
        bean.setKisyakuyouzaitenka(getItemRow(processData.getItemList(), GXHDO102B011Const.KISYAKUYOUZAITENKA));
        bean.setYouzaijyunkanjikan(getItemRow(processData.getItemList(), GXHDO102B011Const.YOUZAIJYUNKANJIKAN));
        bean.setJyunkankaisi_day(getItemRow(processData.getItemList(), GXHDO102B011Const.JYUNKANKAISI_DAY));
        bean.setJyunkankaisi_time(getItemRow(processData.getItemList(), GXHDO102B011Const.JYUNKANKAISI_TIME));
        bean.setJyunkansyuuryou_day(getItemRow(processData.getItemList(), GXHDO102B011Const.JYUNKANSYUURYOU_DAY));
        bean.setJyunkansyuuryou_time(getItemRow(processData.getItemList(), GXHDO102B011Const.JYUNKANSYUURYOU_TIME));
        bean.setJyunkantantousya(getItemRow(processData.getItemList(), GXHDO102B011Const.JYUNKANTANTOUSYA));
    }

    /**
     * 添加材ｽﾗﾘｰ作製・粉砕入力_ｻﾌﾞ画面データの規格値取得処理
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
     * 溶剤1_材料品名のﾘﾝｸ押下時、 添加材ｽﾗﾘｰ作製・粉砕入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC006SubGamen1(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B011Const.YOUZAI1_BUZAIZAIKOLOTNO1, GXHDO102B011Const.YOUZAI1_TYOUGOURYOU1,
                GXHDO102B011Const.YOUZAI1_BUZAIZAIKOLOTNO2, GXHDO102B011Const.YOUZAI1_TYOUGOURYOU2);
        return openC006SubGamen(processData, 1, returnItemIdList, GXHDO102B011Const.YOUZAI1_TYOUGOURYOUKIKAKU);
    }

    /**
     * 溶剤2_材料品名のﾘﾝｸ押下時、 添加材ｽﾗﾘｰ作製・粉砕入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC006SubGamen2(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B011Const.YOUZAI2_BUZAIZAIKOLOTNO1, GXHDO102B011Const.YOUZAI2_TYOUGOURYOU1,
                GXHDO102B011Const.YOUZAI2_BUZAIZAIKOLOTNO2, GXHDO102B011Const.YOUZAI2_TYOUGOURYOU2);
        return openC006SubGamen(processData, 2, returnItemIdList, GXHDO102B011Const.YOUZAI2_TYOUGOURYOUKIKAKU);
    }

    /**
     * 添加材ｽﾗﾘｰ作製・粉砕入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @param zairyokubun 材料区分
     * @param returnItemIdList サブ画面から戻ったときに値を設定必要項目リスト
     * @param itemTyougouryoukikaku 調合量規格
     * @return 処理制御データ
     */
    public ProcessData openC006SubGamen(ProcessData processData, int zairyokubun, List<String> returnItemIdList,String itemTyougouryoukikaku) {
        try {
            // 「秤量号機」
            FXHDD01 itemGoki = getItemRow(processData.getItemList(), GXHDO102B011Const.HYOURYOUGOUKI);
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
            processData.setCollBackParam("gxhdo102c006");

            GXHDO102C006 beanGXHDO102C006 = (GXHDO102C006) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C006);
            GXHDO102C006Model gxhdo102c006model = beanGXHDO102C006.getGxhdO102c006Model();
            // 主画面からサブ画面に渡されたデータを設定
            setSubGamenInitData(gxhdo102c006model, zairyokubun, itemGoki, returnItemIdList);

            beanGXHDO102C006.setGxhdO102c006ModelView(gxhdo102c006model.clone());
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
     * @param gxhdo102c006model モデルデータ
     * @param zairyokubun 材料区分
     * @param itemGoki 秤量号機データ
     * @param returnItemIdList サブ画面から戻るデータリスト
     * @throws CloneNotSupportedException 例外エラー
     */
    private void setSubGamenInitData(GXHDO102C006Model gxhdo102c006model, int zairyokubun, FXHDD01 itemGoki, List<String> returnItemIdList) throws CloneNotSupportedException {
        GXHDO102C006Model.SubGamenData c006subgamendata = GXHDO102C006Logic.getC006subgamendata(gxhdo102c006model, zairyokubun);
        if (c006subgamendata == null) {
            return;
        }
        c006subgamendata.setSubDataGoki(StringUtil.nullToBlank(itemGoki.getValue()));
        c006subgamendata.setSubDataZairyokubun(zairyokubun);
        // サブ画面から戻ったときに値を設定する項目を指定する。
        c006subgamendata.setReturnItemIdBuzailotno1(returnItemIdList.get(0)); // 部材在庫No.X_1
        c006subgamendata.setReturnItemIdTyougouryou1(returnItemIdList.get(1)); // 調合量X_1
        c006subgamendata.setReturnItemIdBuzailotno2(returnItemIdList.get(2)); // 部材在庫NoX_2
        c006subgamendata.setReturnItemIdTyougouryou2(returnItemIdList.get(3)); // 調合量X_2
        gxhdo102c006model.setShowsubgamendata(c006subgamendata.clone());
        // サブ画面の調合残量の計算
        GXHDO102C006Logic.calcTyogouzanryou(gxhdo102c006model);
    }

    /**
     * 添加材ｽﾗﾘｰ作製・粉砕入力_ｻﾌﾞ画面データ設定処理
     *
     * @param processData 処理制御データ
     * @param subSrTenkaFunsaiHyoryoList 添加材ｽﾗﾘｰ作製・粉砕入力_ｻﾌﾞ画面データリスト
     */
    private void setInputItemDataSubFormC006(ProcessData processData, List<SubSrTenkaFunsaiHyoryo> subSrTenkaFunsaiHyoryoList) {
        // サブ画面の情報を取得
        GXHDO102C006 beanGXHDO102C006 = (GXHDO102C006) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C006);

        GXHDO102C006Model model;
        if (subSrTenkaFunsaiHyoryoList == null) {
            // 登録データが無い場合、主画面の材料品名1-2と調合量規格1-2はｻﾌﾞ画面の初期値にセットする。
            subSrTenkaFunsaiHyoryoList = new ArrayList<>();
            SubSrTenkaFunsaiHyoryo subgamen1 = new SubSrTenkaFunsaiHyoryo();
            SubSrTenkaFunsaiHyoryo subgamen2 = new SubSrTenkaFunsaiHyoryo();

            subgamen1.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B011Const.YOUZAI1_ZAIRYOUHINMEI))); // 溶剤1_材料品名
            subgamen1.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B011Const.YOUZAI1_TYOUGOURYOUKIKAKU))); // 溶剤1_調合量規格
            subgamen1.setStandardpattern(getItemRow(processData.getItemList(), GXHDO102B011Const.YOUZAI1_TYOUGOURYOUKIKAKU).getStandardPattern()); // 溶剤1_調合量規格情報ﾊﾟﾀｰﾝ
            subgamen2.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B011Const.YOUZAI2_ZAIRYOUHINMEI))); // 溶剤2_材料品名
            subgamen2.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B011Const.YOUZAI2_TYOUGOURYOUKIKAKU))); // 溶剤2_調合量規格
            subgamen2.setStandardpattern(getItemRow(processData.getItemList(), GXHDO102B011Const.YOUZAI2_TYOUGOURYOUKIKAKU).getStandardPattern()); // 溶剤2_調合量規格情報ﾊﾟﾀｰﾝ
            subSrTenkaFunsaiHyoryoList.add(subgamen1);
            subSrTenkaFunsaiHyoryoList.add(subgamen2);
            model = GXHDO102C006Logic.createGXHDO102C006Model(subSrTenkaFunsaiHyoryoList);

        } else {
            // 登録データがあれば登録データをセットする。
            model = GXHDO102C006Logic.createGXHDO102C006Model(subSrTenkaFunsaiHyoryoList);
        }
        beanGXHDO102C006.setGxhdO102c006Model(model);
    }

    /**
     * 添加材ｽﾗﾘｰ作製・粉砕入力_ｻﾌﾞ画面の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return 添加材ｽﾗﾘｰ作製・粉砕入力_ｻﾌﾞ画面登録データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrTenkaFunsaiHyoryo> getSubSrTenkaFunsaiHyoryoData(QueryRunner queryRunnerQcdb,
            String rev, String jotaiFlg, String kojyo, String lotNo, String edaban) throws SQLException {
        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSubSrTenkaFunsaiHyoryo(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSubSrTenkaFunsaiHyoryo(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        }
    }

    /**
     * [添加材ｽﾗﾘｰ作製・粉砕入力_ｻﾌﾞ画面]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrTenkaFunsaiHyoryo> loadSubSrTenkaFunsaiHyoryo(QueryRunner queryRunnerQcdb,
            String kojyo, String lotNo, String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + "kojyo,lotno,edaban,zairyokubun,tyogouryoukikaku,tyogouzanryou,zairyohinmei,"
                + "buzailotno1,buzaihinmei1,tyougouryou1_1,tyougouryou1_2,tyougouryou1_3,tyougouryou1_4,"
                + "tyougouryou1_5,tyougouryou1_6,buzailotno2,buzaihinmei2,tyougouryou2_1,tyougouryou2_2,"
                + "tyougouryou2_3,tyougouryou2_4,tyougouryou2_5,tyougouryou2_6,torokunichiji,kosinnichiji,"
                + "revision, '0' AS deleteflag "
                + " FROM sub_sr_tenka_funsai_hyoryo "
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
        ResultSetHandler<List<SubSrTenkaFunsaiHyoryo>> beanHandler = new BeanListHandler<>(SubSrTenkaFunsaiHyoryo.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [添加材ｽﾗﾘｰ作製・粉砕入力_ｻﾌﾞ画面_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrTenkaFunsaiHyoryo> loadTmpSubSrTenkaFunsaiHyoryo(QueryRunner queryRunnerQcdb,
            String kojyo, String lotNo, String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + "kojyo,lotno,edaban,zairyokubun,tyogouryoukikaku,tyogouzanryou,zairyohinmei,"
                + "buzailotno1,buzaihinmei1,tyougouryou1_1,tyougouryou1_2,tyougouryou1_3,tyougouryou1_4,"
                + "tyougouryou1_5,tyougouryou1_6,buzailotno2,buzaihinmei2,tyougouryou2_1,tyougouryou2_2,"
                + "tyougouryou2_3,tyougouryou2_4,tyougouryou2_5,tyougouryou2_6,torokunichiji,kosinnichiji,"
                + "revision, deleteflag "
                + " FROM tmp_sub_sr_tenka_funsai_hyoryo "
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
        ResultSetHandler<List<SubSrTenkaFunsaiHyoryo>> beanHandler = new BeanListHandler<>(SubSrTenkaFunsaiHyoryo.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * 添加材ｽﾗﾘｰ作製・粉砕入力_サブ画面_仮登録(tmp_sub_sr_tenka_funsai_hyoryo)登録処理
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
    private void insertTmpSubSrTenkaFunsaiHyoryo(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal newRev, int deleteflag, String kojyo, String lotNo, String edaban, Integer zairyokubun,
            String systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sub_sr_tenka_funsai_hyoryo ( "
                + "kojyo,lotno,edaban,zairyokubun,tyogouryoukikaku,tyogouzanryou,zairyohinmei,"
                + "buzailotno1,buzaihinmei1,tyougouryou1_1,tyougouryou1_2,tyougouryou1_3,tyougouryou1_4,"
                + "tyougouryou1_5,tyougouryou1_6,buzailotno2,buzaihinmei2,tyougouryou2_1,tyougouryou2_2,"
                + "tyougouryou2_3,tyougouryou2_4,tyougouryou2_5,tyougouryou2_6,torokunichiji,kosinnichiji,"
                + "revision, deleteflag "
                + " ) VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";

        List<Object> params = setUpdateParameterTmpSubSrTenkaFunsaiHyoryo(true, newRev, deleteflag, kojyo, lotNo, edaban, zairyokubun, systemTime);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 添加材ｽﾗﾘｰ作製・粉砕入力_仮登録(tmp_sub_sr_tenka_funsai_hyoryo)更新処理
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
    private void updateTmpSubSrTenkaFunsaiHyoryo(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal rev, BigDecimal newRev, String kojyo, String lotNo,
            String edaban, Integer zairyokubun, String systemTime) throws SQLException {

        String sql = "UPDATE tmp_sub_sr_tenka_funsai_hyoryo SET "
                + "tyogouryoukikaku = ?,tyogouzanryou = ?,zairyohinmei = ?,"
                + "buzailotno1 = ?,buzaihinmei1 = ?,tyougouryou1_1 = ?,tyougouryou1_2 = ?,tyougouryou1_3 = ?,tyougouryou1_4 = ?,"
                + "tyougouryou1_5 = ?,tyougouryou1_6 = ?,buzailotno2 = ?,buzaihinmei2 = ?,tyougouryou2_1 = ?,tyougouryou2_2 = ?,"
                + "tyougouryou2_3 = ?,tyougouryou2_4 = ?,tyougouryou2_5 = ?,tyougouryou2_6 = ?,kosinnichiji = ?,revision = ?, deleteflag = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND zairyokubun = ? AND revision = ? ";

        List<Object> params = setUpdateParameterTmpSubSrTenkaFunsaiHyoryo(false, newRev, 0, kojyo, lotNo, edaban, zairyokubun, systemTime);

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
     * 添加材ｽﾗﾘｰ作製・粉砕入力_サブ画面仮登録(tmp_sub_sr_tenka_funsai_hyoryo)更新値パラメータ設定
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
    private List<Object> setUpdateParameterTmpSubSrTenkaFunsaiHyoryo(boolean isInsert, BigDecimal newRev,
            int deleteflag, String kojyo, String lotNo, String edaban, Integer zairyokubun, String systemTime) {

        List<Object> params = new ArrayList<>();

        // 子画面情報を取得
        GXHDO102C006 beanGXHDO102C006 = (GXHDO102C006) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C006);
        GXHDO102C006Model gxhdO102c006Model = beanGXHDO102C006.getGxhdO102c006Model();

        // 添加材ｽﾗﾘｰ作製・粉砕入力_サブ画面から更新値を取得
        ArrayList<Object> subGamenDataList = getSubGamenData(gxhdO102c006Model, zairyokubun);
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
     * 添加材ｽﾗﾘｰ作製・粉砕入力_サブ画面仮登録(tmp_sub_sr_tenka_funsai_hyoryo)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSubSrTenkaFunsaiHyoryo(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal rev, String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sub_sr_tenka_funsai_hyoryo "
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
     * 添加材ｽﾗﾘｰ作製・粉砕入力_サブ画面(sub_sr_tenka_funsai_hyoryo)登録処理
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
    private void insertSubSrTenkaFunsaiHyoryo(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal newRev, String kojyo, String lotNo, String edaban,
            Integer zairyokubun, String systemTime) throws SQLException {
        String sql = "INSERT INTO sub_sr_tenka_funsai_hyoryo ( "
                + "kojyo,lotno,edaban,zairyokubun,tyogouryoukikaku,tyogouzanryou,zairyohinmei,"
                + "buzailotno1,buzaihinmei1,tyougouryou1_1,tyougouryou1_2,tyougouryou1_3,tyougouryou1_4,"
                + "tyougouryou1_5,tyougouryou1_6,buzailotno2,buzaihinmei2,tyougouryou2_1,tyougouryou2_2,"
                + "tyougouryou2_3,tyougouryou2_4,tyougouryou2_5,tyougouryou2_6,torokunichiji,kosinnichiji,"
                + "revision "
                + " ) VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";

        List<Object> params = setUpdateParameterSubSrTenkaFunsaiHyoryo(true, newRev, kojyo, lotNo, edaban, zairyokubun, systemTime);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 添加材ｽﾗﾘｰ作製・粉砕入力_ｻﾌﾞ画面(sub_sr_tenka_funsai_hyoryo)更新処理
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
    private void updateSubSrTenkaFunsaiHyoryo(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal rev, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            Integer zairyokubun, String systemTime) throws SQLException {

        String sql = "UPDATE sub_sr_tenka_funsai_hyoryo SET "
                + "tyogouryoukikaku = ?,tyogouzanryou = ?,zairyohinmei = ?,"
                + "buzailotno1 = ?,buzaihinmei1 = ?,tyougouryou1_1 = ?,tyougouryou1_2 = ?,tyougouryou1_3 = ?,tyougouryou1_4 = ?,"
                + "tyougouryou1_5 = ?,tyougouryou1_6 = ?,buzailotno2 = ?,buzaihinmei2 = ?,tyougouryou2_1 = ?,tyougouryou2_2 = ?,"
                + "tyougouryou2_3 = ?,tyougouryou2_4 = ?,tyougouryou2_5 = ?,tyougouryou2_6 = ?,kosinnichiji = ?,revision = ?"
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND zairyokubun = ? AND revision = ? ";

        List<Object> params = setUpdateParameterSubSrTenkaFunsaiHyoryo(false, newRev, kojyo, lotNo, edaban, zairyokubun, systemTime);

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
     * 添加材ｽﾗﾘｰ作製・粉砕入力_サブ画面から更新値を取得
     *
     * @param gxhdO102c006Model モデルデータ
     * @param zairyokubun 材料区分
     * @return 更新値情報
     */
    private ArrayList<Object> getSubGamenData(GXHDO102C006Model gxhdO102c006Model, Integer zairyokubun) {
        GXHDO102C006Model.SubGamenData c006subgamendata = GXHDO102C006Logic.getC006subgamendata(gxhdO102c006Model, zairyokubun);
        ArrayList<Object> returnList = new ArrayList<>();
        // 調合量規格
        FXHDD01 tyogouryoukikaku = c006subgamendata.getSubDataTyogouryoukikaku();
        // 調合残量
        FXHDD01 tyogouzanryou = c006subgamendata.getSubDataTyogouzanryou();
        // 部材①
        List<FXHDD01> buzaitab1DataList = c006subgamendata.getSubDataBuzaitab1();
        // 部材②
        List<FXHDD01> buzaitab2DataList = c006subgamendata.getSubDataBuzaitab2();
        returnList.add(tyogouryoukikaku);
        returnList.add(tyogouzanryou);
        returnList.add(buzaitab1DataList);
        returnList.add(buzaitab2DataList);
        return returnList;
    }

    /**
     * 添加材ｽﾗﾘｰ作製・粉砕入力_サブ画面登録(tmp_sub_sr_tenka_funsai_hyoryo)更新値パラメータ設定
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
    private List<Object> setUpdateParameterSubSrTenkaFunsaiHyoryo(boolean isInsert, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Integer zairyokubun, String systemTime) {

        List<Object> params = new ArrayList<>();

        // 子画面情報を取得
        GXHDO102C006 beanGXHDO102C006 = (GXHDO102C006) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C006);
        GXHDO102C006Model gxhdO102c006Model = beanGXHDO102C006.getGxhdO102c006Model();
        // 添加材ｽﾗﾘｰ作製・粉砕入力_サブ画面から更新値を取得
        ArrayList<Object> subGamenDataList = getSubGamenData(gxhdO102c006Model, zairyokubun);
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
     * 添加材ｽﾗﾘｰ作製・粉砕入力_サブ画面仮登録(tmp_sub_sr_tenka_funsai_hyoryo)登録処理(削除時)
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
    private void insertDeleteDataTmpSubSrTenkaFunsaiHyoryo(QueryRunner queryRunnerQcdb,
            Connection conQcdb, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, String systemTime) throws SQLException {
        String sql = "INSERT INTO tmp_sub_sr_tenka_funsai_hyoryo( "
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
                + " FROM sub_sr_tenka_funsai_hyoryo "
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
     * 添加材ｽﾗﾘｰ作製・粉砕入力_サブ画面仮登録(sub_sr_tenka_funsai_hyoryo)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSubSrTenkaFunsaiHyoryo(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal rev, String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sub_sr_tenka_funsai_hyoryo "
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
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B011Const.YOUZAI1_BUZAIZAIKOLOTNO1, GXHDO102B011Const.YOUZAI1_TYOUGOURYOU1, errorItemList);
        // 溶剤1_部材在庫No2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B011Const.YOUZAI1_BUZAIZAIKOLOTNO2, GXHDO102B011Const.YOUZAI1_TYOUGOURYOU2, errorItemList);
        // 溶剤2_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B011Const.YOUZAI2_BUZAIZAIKOLOTNO1, GXHDO102B011Const.YOUZAI2_TYOUGOURYOU1, errorItemList);
        // 溶剤2_部材在庫No2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B011Const.YOUZAI2_BUZAIZAIKOLOTNO2, GXHDO102B011Const.YOUZAI2_TYOUGOURYOU2, errorItemList);

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
        FXHDD01 itemFxhdd01Wiplotno = getItemRow(processData.getItemList(), GXHDO102B011Const.WIPLOTNO);
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
        allItemIdMap.put(GXHDO102B011Const.WIPLOTNO, "WIPﾛｯﾄNo");
        allItemIdMap.put(GXHDO102B011Const.TENKAZAISLURRYHINMEI, "添加材ｽﾗﾘｰ品名");
        allItemIdMap.put(GXHDO102B011Const.TENKAZAISLURRYLOTNO, "添加材ｽﾗﾘｰLotNo");
        allItemIdMap.put(GXHDO102B011Const.LOTKUBUN, "ﾛｯﾄ区分");
        allItemIdMap.put(GXHDO102B011Const.HYOURYOUGOUKI, "秤量号機");
        allItemIdMap.put(GXHDO102B011Const.FUNSAIKI, "粉砕機");
        allItemIdMap.put(GXHDO102B011Const.FUNSAIGOUKI, "粉砕号機");
        allItemIdMap.put(GXHDO102B011Const.MEDIALOTNO, "ﾒﾃﾞｨｱLotNo");
        allItemIdMap.put(GXHDO102B011Const.RENZOKUUNTENKAISUU, "連続運転回数");
        allItemIdMap.put(GXHDO102B011Const.TOUNYUURYOU, "投入量");
        allItemIdMap.put(GXHDO102B011Const.JIKAN_PASSKAISUU, "時間/ﾊﾟｽ回数");
        allItemIdMap.put(GXHDO102B011Const.MILLSYUUHASUU, "ﾐﾙ周波数");
        allItemIdMap.put(GXHDO102B011Const.SYUUSOKU, "周速");
        allItemIdMap.put(GXHDO102B011Const.PUMPSYUTURYOKCHECK, "ﾎﾟﾝﾌﾟ出力");
        allItemIdMap.put(GXHDO102B011Const.RYUURYOU, "流量");
        allItemIdMap.put(GXHDO102B011Const.PASSKAISUU, "ﾊﾟｽ回数");
        allItemIdMap.put(GXHDO102B011Const.DISPANOSYURUI, "ﾃﾞｨｽﾊﾟの種類");
        allItemIdMap.put(GXHDO102B011Const.DISPAKAITENSUU, "ﾃﾞｨｽﾊﾟ回転数");
        allItemIdMap.put(GXHDO102B011Const.PASSKAISUU_SF1, "ﾊﾟｽ回数");
        allItemIdMap.put(GXHDO102B011Const.KAISI_SF1_DAY, "開始日");
        allItemIdMap.put(GXHDO102B011Const.KAISI_SF1_TIME, "開始時間");
        allItemIdMap.put(GXHDO102B011Const.SYUURYOUYOTEI_SF1_DAY, "終了予定日");
        allItemIdMap.put(GXHDO102B011Const.SYUURYOUYOTEI_SF1_TIME, "終了予定時間");
        allItemIdMap.put(GXHDO102B011Const.FUKADENRYUUTI_SF1, "負荷電流値");
        allItemIdMap.put(GXHDO102B011Const.SEIHINONDO_SF1, "製品温度");
        allItemIdMap.put(GXHDO102B011Const.STICKERONDO_SF1, "ｼｰﾙ温度");
        allItemIdMap.put(GXHDO102B011Const.PUMPMEMORI_SF1, "ﾎﾟﾝﾌﾟ目盛");
        allItemIdMap.put(GXHDO102B011Const.PUMPATU_SF1, "ﾎﾟﾝﾌﾟ圧");
        allItemIdMap.put(GXHDO102B011Const.RYUURYOU_SF1, "流量");
        allItemIdMap.put(GXHDO102B011Const.BIKOU1_SF1, "備考1");
        allItemIdMap.put(GXHDO102B011Const.BIKOU2_SF1, "備考2");
        allItemIdMap.put(GXHDO102B011Const.ONDO_OU_SF1, "温度(往)");
        allItemIdMap.put(GXHDO102B011Const.ONDO_KAN_SF1, "温度(還)");
        allItemIdMap.put(GXHDO102B011Const.ATURYOKU_OU_SF1, "圧力(往)");
        allItemIdMap.put(GXHDO102B011Const.ATURYOKU_KAN_SF1, "圧力(還)");
        allItemIdMap.put(GXHDO102B011Const.SYUURYOU_SF1_DAY, "終了日");
        allItemIdMap.put(GXHDO102B011Const.SYUURYOU_SF1_TIME, "終了時間");
        allItemIdMap.put(GXHDO102B011Const.TANTOUSYA_SF1, "担当者");
        allItemIdMap.put(GXHDO102B011Const.PASSKAISUU_SF2, "ﾊﾟｽ回数");
        allItemIdMap.put(GXHDO102B011Const.KAISI_SF2_DAY, "開始日");
        allItemIdMap.put(GXHDO102B011Const.KAISI_SF2_TIME, "開始時間");
        allItemIdMap.put(GXHDO102B011Const.SYUURYOUYOTEI_SF2_DAY, "終了予定日");
        allItemIdMap.put(GXHDO102B011Const.SYUURYOUYOTEI_SF2_TIME, "終了予定時間");
        allItemIdMap.put(GXHDO102B011Const.FUKADENRYUUTI_SF2, "負荷電流値");
        allItemIdMap.put(GXHDO102B011Const.SEIHINONDO_SF2, "製品温度");
        allItemIdMap.put(GXHDO102B011Const.STICKERONDO_SF2, "ｼｰﾙ温度");
        allItemIdMap.put(GXHDO102B011Const.PUMPMEMORI_SF2, "ﾎﾟﾝﾌﾟ目盛");
        allItemIdMap.put(GXHDO102B011Const.PUMPATU_SF2, "ﾎﾟﾝﾌﾟ圧");
        allItemIdMap.put(GXHDO102B011Const.SYUURYOU_SF2_DAY, "終了日");
        allItemIdMap.put(GXHDO102B011Const.SYUURYOU_SF2_TIME, "終了時間");
        allItemIdMap.put(GXHDO102B011Const.RYUURYOU_SF2, "流量");
        allItemIdMap.put(GXHDO102B011Const.BIKOU1_SF2, "備考1");
        allItemIdMap.put(GXHDO102B011Const.BIKOU2_SF2, "備考2");
        allItemIdMap.put(GXHDO102B011Const.TANTOUSYA_SF2, "担当者");
        allItemIdMap.put(GXHDO102B011Const.YOUZAI1_ZAIRYOUHINMEI, "溶剤①_材料品名");
        allItemIdMap.put(GXHDO102B011Const.YOUZAI1_TYOUGOURYOUKIKAKU, "溶剤①_調合量規格");
        allItemIdMap.put(GXHDO102B011Const.YOUZAI1_BUZAIZAIKOLOTNO1, "溶剤①_部材在庫No1");
        allItemIdMap.put(GXHDO102B011Const.YOUZAI1_TYOUGOURYOU1, "溶剤①_調合量1");
        allItemIdMap.put(GXHDO102B011Const.YOUZAI1_BUZAIZAIKOLOTNO2, "溶剤①_部材在庫No2");
        allItemIdMap.put(GXHDO102B011Const.YOUZAI1_TYOUGOURYOU2, "溶剤①_調合量2");
        allItemIdMap.put(GXHDO102B011Const.YOUZAI2_ZAIRYOUHINMEI, "溶剤②_材料品名");
        allItemIdMap.put(GXHDO102B011Const.YOUZAI2_TYOUGOURYOUKIKAKU, "溶剤②_調合量規格");
        allItemIdMap.put(GXHDO102B011Const.YOUZAI2_BUZAIZAIKOLOTNO1, "溶剤②_部材在庫No1");
        allItemIdMap.put(GXHDO102B011Const.YOUZAI2_TYOUGOURYOU1, "溶剤②_調合量1");
        allItemIdMap.put(GXHDO102B011Const.YOUZAI2_BUZAIZAIKOLOTNO2, "溶剤②_部材在庫No2");
        allItemIdMap.put(GXHDO102B011Const.YOUZAI2_TYOUGOURYOU2, "溶剤②_調合量2");
        allItemIdMap.put(GXHDO102B011Const.TANTOUSYA, "担当者");
        allItemIdMap.put(GXHDO102B011Const.PUMPSYUTURYOKU, "ﾎﾟﾝﾌﾟ出力");
        allItemIdMap.put(GXHDO102B011Const.MILLSYUUHASUU2, "ﾐﾙ周波数");
        allItemIdMap.put(GXHDO102B011Const.KISYAKUYOUZAITENKA, "希釈溶剤添加");
        allItemIdMap.put(GXHDO102B011Const.YOUZAIJYUNKANJIKAN, "溶剤循環時間");
        allItemIdMap.put(GXHDO102B011Const.JYUNKANKAISI_DAY, "循環開始日");
        allItemIdMap.put(GXHDO102B011Const.JYUNKANKAISI_TIME, "循環開始時間");
        allItemIdMap.put(GXHDO102B011Const.JYUNKANSYUURYOU_DAY, "循環終了日");
        allItemIdMap.put(GXHDO102B011Const.JYUNKANSYUURYOU_TIME, "循環終了時間");
        allItemIdMap.put(GXHDO102B011Const.JYUNKANTANTOUSYA, "担当者");

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
     * 撹拌機項目の表示制御
     *
     * @param processData 処理制御データ
     * @param notShowItemList 画面非表示項目リスト
     */
    private void removeItemFromItemList(ProcessData processData, List<String> notShowItemList) {
        List<FXHDD01> itemList = processData.getItemList();
        GXHDO102B011A bean = (GXHDO102B011A) getFormBean("gXHDO102B011A");
        notShowItemList.forEach((notShowItem) -> {
            itemList.remove(getItemRow(itemList, notShowItem));
        });

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
        bean.setPumpsyuturyoku(null);
        bean.setMillsyuuhasuu2(null);
        bean.setKisyakuyouzaitenka(null);
        bean.setYouzaijyunkanjikan(null);
        bean.setJyunkankaisi_day(null);
        bean.setJyunkankaisi_time(null);
        bean.setJyunkansyuuryou_day(null);
        bean.setJyunkansyuuryou_time(null);
        bean.setJyunkantantousya(null);
    }

    /**
     * 撹拌機項目の表示制御
     *
     * @param processData 処理制御データ
     * @param queryRunnerDoc QueryRunnerオブジェクト(DocServer)
     * @param queryRunnerQcdb QueryRunnerオブジェクト(Qcdb)
     * @param shikakariData 前工程WIPから仕掛情報
     * @param lotNo ﾛｯﾄNo
     * @throws SQLException 例外エラー
     */
    private void setSrtenkafunsaiItemRendered(ProcessData processData, QueryRunner queryRunnerDoc, QueryRunner queryRunnerQcdb, Map shikakariData, String lotNo) throws SQLException {
        String kojyo = lotNo.substring(0, 3);
        String lotNo9 = lotNo.substring(3, 12);
        String edaban = lotNo.substring(12, 15);
        String syurui = "添加材ｽﾗﾘｰ作製";
        // [ﾊﾟﾗﾒｰﾀﾏｽﾀ]から、ﾃﾞｰﾀを取得
        Map fxhbm03Data = loadFxhbm03Data(queryRunnerDoc, "添加剤ｽﾗﾘｰ_粉砕_希釈溶剤_表示制御");
        /* 画面非表示項目リスト: 溶剤①_材料品名、溶剤①_調合量規格、溶剤①_部材在庫No1、溶剤①_調合量1、
                                 溶剤①_部材在庫No2、溶剤①_調合量2、溶剤②_材料品名、溶剤②_調合量規格、
                                 溶剤②_部材在庫No1、溶剤②_調合量1、溶剤②_部材在庫No2、溶剤②_調合量2、
                                 担当者、ﾎﾟﾝﾌﾟ出力、ﾐﾙ周波数、希釈溶剤添加、溶剤循環時間、循環開始日、
                                 循環開始時間、循環終了日、循環終了時間、担当者 */
        List<String> notShowItemList = Arrays.asList(GXHDO102B011Const.YOUZAI1_ZAIRYOUHINMEI, GXHDO102B011Const.YOUZAI1_TYOUGOURYOUKIKAKU, GXHDO102B011Const.YOUZAI1_BUZAIZAIKOLOTNO1,
                GXHDO102B011Const.YOUZAI1_TYOUGOURYOU1, GXHDO102B011Const.YOUZAI1_BUZAIZAIKOLOTNO2, GXHDO102B011Const.YOUZAI1_TYOUGOURYOU2, GXHDO102B011Const.YOUZAI2_ZAIRYOUHINMEI,
                GXHDO102B011Const.YOUZAI2_TYOUGOURYOUKIKAKU, GXHDO102B011Const.YOUZAI2_BUZAIZAIKOLOTNO1, GXHDO102B011Const.YOUZAI2_TYOUGOURYOU1, GXHDO102B011Const.YOUZAI2_BUZAIZAIKOLOTNO2,
                GXHDO102B011Const.YOUZAI2_TYOUGOURYOU2, GXHDO102B011Const.TANTOUSYA, GXHDO102B011Const.PUMPSYUTURYOKU, GXHDO102B011Const.MILLSYUUHASUU2, GXHDO102B011Const.KISYAKUYOUZAITENKA,
                GXHDO102B011Const.YOUZAIJYUNKANJIKAN, GXHDO102B011Const.JYUNKANKAISI_DAY, GXHDO102B011Const.JYUNKANKAISI_TIME, GXHDO102B011Const.JYUNKANSYUURYOU_DAY, GXHDO102B011Const.JYUNKANSYUURYOU_TIME,
                GXHDO102B011Const.JYUNKANTANTOUSYA);
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
     * チェックボックス値(チェックボックス内のValue値)取得
     *
     * @param dbValue コンボボックス(DB内)Value値
     * @return コンボボックステキスト値
     */
    private String getCheckBoxCheckValue(String dbValue) {
        if ("1".equals(dbValue)) {
            return "true";
        }
        return "false";
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
     * ﾊﾟｽ回数により表示の有無あり
     *
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void setSubsrtenkafunsaiItemRendered(ProcessData processData) throws SQLException {

        String passkaisuu = StringUtil.nullToBlank(getItemKikakuchi(processData.getItemList(), GXHDO102B011Const.PASSKAISUU_SF2, null));
        if (!"".equals(passkaisuu)) {
            return;
        }
        List<String> notShowItemList = Arrays.asList(GXHDO102B011Const.PASSKAISUU_SF2, GXHDO102B011Const.KAISI_SF2_DAY, GXHDO102B011Const.KAISI_SF2_TIME, GXHDO102B011Const.SYUURYOUYOTEI_SF2_DAY,
                GXHDO102B011Const.SYUURYOUYOTEI_SF2_TIME, GXHDO102B011Const.FUKADENRYUUTI_SF2, GXHDO102B011Const.SEIHINONDO_SF2, GXHDO102B011Const.STICKERONDO_SF2, GXHDO102B011Const.PUMPMEMORI_SF2,
                GXHDO102B011Const.PUMPATU_SF2, GXHDO102B011Const.SYUURYOU_SF2_DAY, GXHDO102B011Const.SYUURYOU_SF2_TIME, GXHDO102B011Const.RYUURYOU_SF2, GXHDO102B011Const.BIKOU1_SF2,
                GXHDO102B011Const.BIKOU2_SF2, GXHDO102B011Const.TANTOUSYA_SF2);
        List<FXHDD01> itemList = processData.getItemList();
        GXHDO102B011A bean = (GXHDO102B011A) getFormBean("gXHDO102B011A");
        notShowItemList.forEach((notShowItem) -> {
            itemList.remove(getItemRow(itemList, notShowItem));
        });

        bean.setPasskaisuu_sf2(null);
        bean.setKaisi_sf2_day(null);
        bean.setKaisi_sf2_time(null);
        bean.setSyuuryouyotei_sf2_day(null);
        bean.setSyuuryouyotei_sf2_time(null);
        bean.setFukadenryuuti_sf2(null);
        bean.setSeihinondo_sf2(null);
        bean.setStickerondo_sf2(null);
        bean.setPumpmemori_sf2(null);
        bean.setPumpatu_sf2(null);
        bean.setSyuuryou_sf2_day(null);
        bean.setSyuuryou_sf2_time(null);
        bean.setRyuuryou_sf2(null);
        bean.setBikou1_sf2(null);
        bean.setBikou2_sf2(null);
        bean.setTantousya_sf2(null);
    }
}
