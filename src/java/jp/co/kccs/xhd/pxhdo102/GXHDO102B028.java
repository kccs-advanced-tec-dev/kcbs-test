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
import jp.co.kccs.xhd.db.model.SrSlipSlurrykokeibuntyouseiSutenyouki;
import jp.co.kccs.xhd.db.model.SubSrSlipSlurrykokeibuntyouseiSutenyouki;
import jp.co.kccs.xhd.model.GXHDO102C013Model;
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
 * 変更日	2021/12/06<br>
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
 * GXHDO102B028(ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器))
 *
 * @author KCSS K.Jo
 * @since 2021/12/06
 */
public class GXHDO102B028 implements IFormLogic {

    private static final Logger LOGGER = Logger.getLogger(GXHDO102B028.class.getName());
    private static final String JOTAI_FLG_KARI_TOROKU = "0";
    private static final String JOTAI_FLG_TOROKUZUMI = "1";
    private static final String JOTAI_FLG_SAKUJO = "9";
    private static final String SQL_STATE_RECORD_LOCK_ERR = "55P03";

    /**
     * コンストラクタ
     */
    public GXHDO102B028() {
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
            initGXHDO102B028A(processData);

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
                    GXHDO102B028Const.BTN_EDABAN_COPY_TOP,
                    GXHDO102B028Const.BTN_BINDERKONGOU_TOP,
                    GXHDO102B028Const.BTN_KAKUHANKAISI_TOP,
                    GXHDO102B028Const.BTN_KAKUHANSYUURYOU_TOP,
                    GXHDO102B028Const.BTN_KANSOUKAISI_TOP,
                    GXHDO102B028Const.BTN_KANSOUSYUURYOU_TOP,
                    GXHDO102B028Const.BTN_YOUZAIKEIRYOU_TOP,
                    GXHDO102B028Const.BTN_NIKAIMEKAKUHANKAISI_TOP,
                    GXHDO102B028Const.BTN_NKMKKHSR_TOP,
                    GXHDO102B028Const.BTN_NIKAIMEKANSOUKAISI_TOP,
                    GXHDO102B028Const.BTN_NIKAIMEKANSOUSYUURYOU_TOP,
                    GXHDO102B028Const.BTN_NIKAIMEYOUZAIKEIRYOU_TOP,
                    GXHDO102B028Const.BTN_SANKAIMEKAKUHANKAISI_TOP,
                    GXHDO102B028Const.BTN_SKMKKHSR_TOP,
                    GXHDO102B028Const.BTN_SANKAIMEKANSOUKAISI_TOP,
                    GXHDO102B028Const.BTN_SANKAIMEKANSOUSYUURYOU_TOP,
                    GXHDO102B028Const.BTN_FPKAISI_TOP,
                    GXHDO102B028Const.BTN_FPTEISI_TOP,
                    GXHDO102B028Const.BTN_FPSAIKAI_TOP,
                    GXHDO102B028Const.BTN_EDABAN_COPY_BOTTOM,
                    GXHDO102B028Const.BTN_BINDERKONGOU_BOTTOM,
                    GXHDO102B028Const.BTN_KAKUHANKAISI_BOTTOM,
                    GXHDO102B028Const.BTN_KAKUHANSYUURYOU_BOTTOM,
                    GXHDO102B028Const.BTN_KANSOUKAISI_BOTTOM,
                    GXHDO102B028Const.BTN_KANSOUSYUURYOU_BOTTOM,
                    GXHDO102B028Const.BTN_YOUZAIKEIRYOU_BOTTOM,
                    GXHDO102B028Const.BTN_NIKAIMEKAKUHANKAISI_BOTTOM,
                    GXHDO102B028Const.BTN_NKMKKHSR_BOTTOM,
                    GXHDO102B028Const.BTN_NIKAIMEKANSOUKAISI_BOTTOM,
                    GXHDO102B028Const.BTN_NIKAIMEKANSOUSYUURYOU_BOTTOM,
                    GXHDO102B028Const.BTN_NIKAIMEYOUZAIKEIRYOU_BOTTOM,
                    GXHDO102B028Const.BTN_SANKAIMEKAKUHANKAISI_BOTTOM,
                    GXHDO102B028Const.BTN_SKMKKHSR_BOTTOM,
                    GXHDO102B028Const.BTN_SANKAIMEKANSOUKAISI_BOTTOM,
                    GXHDO102B028Const.BTN_SANKAIMEKANSOUSYUURYOU_BOTTOM,
                    GXHDO102B028Const.BTN_FPKAISI_BOTTOM,
                    GXHDO102B028Const.BTN_FPTEISI_BOTTOM,
                    GXHDO102B028Const.BTN_FPSAIKAI_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO102B028Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO102B028Const.BTN_INSERT_TOP,
                    GXHDO102B028Const.BTN_DELETE_TOP,
                    GXHDO102B028Const.BTN_UPDATE_TOP,
                    GXHDO102B028Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO102B028Const.BTN_INSERT_BOTTOM,
                    GXHDO102B028Const.BTN_DELETE_BOTTOM,
                    GXHDO102B028Const.BTN_UPDATE_BOTTOM
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
            case GXHDO102B028Const.BTN_EDABAN_COPY_TOP:
            case GXHDO102B028Const.BTN_EDABAN_COPY_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 仮登録
            case GXHDO102B028Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO102B028Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO102B028Const.BTN_INSERT_TOP:
            case GXHDO102B028Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 修正
            case GXHDO102B028Const.BTN_UPDATE_TOP:
            case GXHDO102B028Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO102B028Const.BTN_DELETE_TOP:
            case GXHDO102B028Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // ﾊﾞｲﾝﾀﾞｰ混合日時
            case GXHDO102B028Const.BTN_BINDERKONGOU_TOP:
            case GXHDO102B028Const.BTN_BINDERKONGOU_BOTTOM:
                method = "setBinderkongouDateTime";
                break;
            // ｽﾗﾘｰ重量計算処理
            case GXHDO102B028Const.BTN_SLURRYJYUURYOU_TOP:
            case GXHDO102B028Const.BTN_SLURRYJYUURYOU_BOTTOM:
                method = "setSlurryjyuuryou";
                break;
            // 撹拌開始日時
            case GXHDO102B028Const.BTN_KAKUHANKAISI_TOP:
            case GXHDO102B028Const.BTN_KAKUHANKAISI_BOTTOM:
                method = "setKakuhankaisiDateTime";
                break;
            // 撹拌終了日時
            case GXHDO102B028Const.BTN_KAKUHANSYUURYOU_TOP:
            case GXHDO102B028Const.BTN_KAKUHANSYUURYOU_BOTTOM:
                method = "setKakuhansyuuryouDateTime";
                break;
            // 乾燥開始日時
            case GXHDO102B028Const.BTN_KANSOUKAISI_TOP:
            case GXHDO102B028Const.BTN_KANSOUKAISI_BOTTOM:
                method = "setKansoukaisiDateTime";
                break;
            // 乾燥終了日時
            case GXHDO102B028Const.BTN_KANSOUSYUURYOU_TOP:
            case GXHDO102B028Const.BTN_KANSOUSYUURYOU_BOTTOM:
                method = "setKansousyuuryouDateTime";
                break;
            // 溶剤秤量日時
            case GXHDO102B028Const.BTN_YOUZAIKEIRYOU_TOP:
            case GXHDO102B028Const.BTN_YOUZAIKEIRYOU_BOTTOM:
                method = "setYouzaikeiryouDateTime";
                break;
            // 2回目撹拌開始日時
            case GXHDO102B028Const.BTN_NIKAIMEKAKUHANKAISI_TOP:
            case GXHDO102B028Const.BTN_NIKAIMEKAKUHANKAISI_BOTTOM:
                method = "setNikaimekakuhankaisiDateTime";
                break;
            // 2回目撹拌終了日時
            case GXHDO102B028Const.BTN_NKMKKHSR_TOP:
            case GXHDO102B028Const.BTN_NKMKKHSR_BOTTOM:
                method = "setNkmkkhsrDateTime";
                break;
            // 2回目乾燥開始日時
            case GXHDO102B028Const.BTN_NIKAIMEKANSOUKAISI_TOP:
            case GXHDO102B028Const.BTN_NIKAIMEKANSOUKAISI_BOTTOM:
                method = "setNikaimekansoukaisiDateTime";
                break;
            // 2回目乾燥終了日時
            case GXHDO102B028Const.BTN_NIKAIMEKANSOUSYUURYOU_TOP:
            case GXHDO102B028Const.BTN_NIKAIMEKANSOUSYUURYOU_BOTTOM:
                method = "setNikaimekansousyuuryouDateTime";
                break;
            // 2回目溶剤秤量日時
            case GXHDO102B028Const.BTN_NIKAIMEYOUZAIKEIRYOU_TOP:
            case GXHDO102B028Const.BTN_NIKAIMEYOUZAIKEIRYOU_BOTTOM:
                method = "setNikaimeyouzaikeiryouDateTime";
                break;
            // 3回目撹拌開始日時
            case GXHDO102B028Const.BTN_SANKAIMEKAKUHANKAISI_TOP:
            case GXHDO102B028Const.BTN_SANKAIMEKAKUHANKAISI_BOTTOM:
                method = "setSankaimekakuhankaisiDateTime";
                break;
            // 3回目撹拌終了日時
            case GXHDO102B028Const.BTN_SKMKKHSR_TOP:
            case GXHDO102B028Const.BTN_SKMKKHSR_BOTTOM:
                method = "setSkmkkhsrDateTime";
                break;
            // 3回目乾燥開始日時
            case GXHDO102B028Const.BTN_SANKAIMEKANSOUKAISI_TOP:
            case GXHDO102B028Const.BTN_SANKAIMEKANSOUKAISI_BOTTOM:
                method = "setSankaimekansoukaisiDateTime";
                break;
            // 3回目乾燥終了日時
            case GXHDO102B028Const.BTN_SANKAIMEKANSOUSYUURYOU_TOP:
            case GXHDO102B028Const.BTN_SANKAIMEKANSOUSYUURYOU_BOTTOM:
                method = "setSankaimekansousyuuryouDateTime";
                break;
            // F/P開始日時
            case GXHDO102B028Const.BTN_FPKAISI_TOP:
            case GXHDO102B028Const.BTN_FPKAISI_BOTTOM:
                method = "setFpkaisiDateTime";
                break;
            // F/P停止日時
            case GXHDO102B028Const.BTN_FPTEISI_TOP:
            case GXHDO102B028Const.BTN_FPTEISI_BOTTOM:
                method = "setFpteisiDateTime";
                break;
            // F/P再開日時
            case GXHDO102B028Const.BTN_FPSAIKAI_TOP:
            case GXHDO102B028Const.BTN_FPSAIKAI_BOTTOM:
                method = "setFpsaikaiDateTime";
                break;
            // F/P終了日時
            case GXHDO102B028Const.BTN_FPSYUURYOU_TOP:
            case GXHDO102B028Const.BTN_FPSYUURYOU_BOTTOM:
                method = "setFpsyuuryouDateTime";
                break;
            // 乾燥後正味重量計算
            case GXHDO102B028Const.BTN_KANSOUGOSYOUMIJYUURYOU_TOP:
            case GXHDO102B028Const.BTN_KANSOUGOSYOUMIJYUURYOU_BOTTOM:
                method = "setKansougosyoumijyuuryou";
                break;
            // 2回目乾燥後正味重量計算
            case GXHDO102B028Const.BTN_NKMKSGSMJR_TOP:
            case GXHDO102B028Const.BTN_NKMKSGSMJR_BOTTOM:
                method = "setNikaimekansougosyoumijyuuryou";
                break;
            // 3回目乾燥後正味重量計算
            case GXHDO102B028Const.BTN_SKMKSGSMJR_TOP:
            case GXHDO102B028Const.BTN_SKMKSGSMJR_BOTTOM:
                method = "setSankaimekansougosyoumijyuuryou";
                break;
            // 溶剤調整量計算
            case GXHDO102B028Const.BTN_YOUZAITYOUSEIRYOU_TOP:
            case GXHDO102B028Const.BTN_YOUZAITYOUSEIRYOU_BOTTOM:
                method = "setYouzaityouseiryou";
                break;
            // 2回目溶剤調整量計算
            case GXHDO102B028Const.BTN_NKMYZTSR_TOP:
            case GXHDO102B028Const.BTN_NKMYZTSR_BOTTOM:
                method = "setNikaimeyouzaityouseiryou";
                break;
            // 正味重量計算
            case GXHDO102B028Const.BTN_SYOUMIJYUURYOU_TOP:
            case GXHDO102B028Const.BTN_SYOUMIJYUURYOU_BOTTOM:
                method = "setSyoumijyuuryou";
                break;
            // 溶剤1_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B028Const.BTN_OPENC013SUBGAMEN1:
                method = "openC013SubGamen1";
                break;
            // 溶剤2_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B028Const.BTN_OPENC013SUBGAMEN2:
                method = "openC013SubGamen2";
                break;
            // 2回目溶剤1_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B028Const.BTN_OPENC013SUBGAMEN3:
                method = "openC013SubGamen3";
                break;
            // 2回目溶剤2_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面Open用非表示ボタン
            case GXHDO102B028Const.BTN_OPENC013SUBGAMEN4:
                method = "openC013SubGamen4";
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

            // ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)の入力項目の登録データ(仮登録時は仮登録データ)を取得
            List<SrSlipSlurrykokeibuntyouseiSutenyouki> srSlipSlurrykokeibuntyouseiSutenyoukiDataList = getSrSlipSlurrykokeibuntyouseiSutenyoukiData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo9, oyalotEdaban);
            if (srSlipSlurrykokeibuntyouseiSutenyoukiDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }
            // ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力_ｻﾌﾞ画面データ取得
            List<SubSrSlipSlurrykokeibuntyouseiSutenyouki> subSrSlipSlrkkbtsSutenyoukiDataList = getSubSrSlipSlurrykokeibuntyouseiSutenyoukiData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo9, oyalotEdaban);
            if (subSrSlipSlrkkbtsSutenyoukiDataList.isEmpty() || subSrSlipSlrkkbtsSutenyoukiDataList.size() != 4) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }
            // メイン画面データ設定
            setInputItemDataMainForm(processData, srSlipSlurrykokeibuntyouseiSutenyoukiDataList.get(0), jotaiFlg);
            // ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力_ｻﾌﾞ画面データ設定
            setInputItemDataSubFormC013(processData, subSrSlipSlrkkbtsSutenyoukiDataList);

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
        List<String> tyogouryoukikaku = Arrays.asList(GXHDO102B028Const.YOUZAI1_TYOUGOURYOUKIKAKU, GXHDO102B028Const.YOUZAI2_TYOUGOURYOUKIKAKU,
                GXHDO102B028Const.NKMYZ1_TYOUGOURYOUKIKAKU, GXHDO102B028Const.NKMYZ2_TYOUGOURYOUKIKAKU
        );
        List<String> youzai1_tyougouryouList = Arrays.asList(GXHDO102B028Const.YOUZAI1_TYOUGOURYOU1, GXHDO102B028Const.YOUZAI1_TYOUGOURYOU2); // 溶剤1_調合量
        List<String> youzai2_tyougouryouList = Arrays.asList(GXHDO102B028Const.YOUZAI2_TYOUGOURYOU1, GXHDO102B028Const.YOUZAI2_TYOUGOURYOU2); // 溶剤2_調合量
        List<String> nkmyz1_tyougouryouList = Arrays.asList(GXHDO102B028Const.NKMYZ1_TYOUGOURYOU1, GXHDO102B028Const.NKMYZ1_TYOUGOURYOU2); // 2回目溶剤1_調合量
        List<String> nkmyz2_tyougouryouList = Arrays.asList(GXHDO102B028Const.NKMYZ2_TYOUGOURYOU1, GXHDO102B028Const.NKMYZ2_TYOUGOURYOU2); // 2回目溶剤2_調合量

        // 規格値の入力値チェック必要の項目リスト
        List<FXHDD01> itemList = new ArrayList<>();
        setKikakuValueAndLabel1(processData, itemList, youzai1_tyougouryouList, tyogouryoukikaku.get(0), "溶剤①_調合量"); // 溶剤①_調合量の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, youzai2_tyougouryouList, tyogouryoukikaku.get(1), "溶剤②_調合量"); // 溶剤②_調合量の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, nkmyz1_tyougouryouList, tyogouryoukikaku.get(2), "2回目溶剤①_調合量"); // 2回目溶剤①_調合量の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, nkmyz2_tyougouryouList, tyogouryoukikaku.get(3), "2回目溶剤②_調合量"); // 2回目溶剤②_調合量の規格値と表示ﾗﾍﾞﾙ1を設置

        tyougouryouList.addAll(youzai1_tyougouryouList);
        tyougouryouList.addAll(youzai2_tyougouryouList);
        tyougouryouList.addAll(nkmyz1_tyougouryouList);
        tyougouryouList.addAll(nkmyz2_tyougouryouList);

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

                // ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)_仮登録処理
                insertTmpSrSlipSlurrykokeibuntyouseiSutenyouki(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, strSystime, processData);
                // ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力ｻﾌﾞ画面の仮登録処理
                for (int i = 1; i <= 4; i++) {
                    insertTmpSubSrSlipSlurrykokeibuntyouseiSutenyouki(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, i, strSystime, processData);
                }
            } else {

                // ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)_仮登録更新処理
                SrSlipSlurrykokeibuntyouseiSutenyouki srSlipSlurrykokeibuntyouseiSutenyouki = updateTmpSrSlipSlurrykokeibuntyouseiSutenyouki(queryRunnerQcdb, conQcdb, rev, 
                        processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);
                // ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力ｻﾌﾞ画面の仮登録更新処理
                for (int i = 1; i <= 4; i++) {
                    updateTmpSubSrSlipSlurrykokeibuntyouseiSutenyouki(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, i, strSystime, srSlipSlurrykokeibuntyouseiSutenyouki, processData);
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

        // 粉砕終了日時、ﾊﾞｲﾝﾀﾞｰ混合前後チェック
        FXHDD01 itemKaisiDay = getItemRow(processData.getItemList(), GXHDO102B028Const.FUNSAISYUURYOU_DAY); // 粉砕終日
        FXHDD01 itemKaisiTime = getItemRow(processData.getItemList(), GXHDO102B028Const.FUNSAISYUURYOU_TIME); // 粉砕終時間
        FXHDD01 itemSyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B028Const.BINDERKONGOU_DAY); // ﾊﾞｲﾝﾀﾞｰ混合日
        FXHDD01 itemSyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B028Const.BINDERKONGOU_TIME); // ﾊﾞｲﾝﾀﾞｰ混合時間
        ErrorMessageInfo errorMsg = checkItemR001(itemKaisiDay, itemKaisiTime, itemSyuuryouDay, itemSyuuryouTime);
        if (errorMsg != null) {
            return errorMsg;
        }

        // 撹拌開始日時、撹拌終了日時前後チェック
        itemKaisiDay = getItemRow(processData.getItemList(), GXHDO102B028Const.KAKUHANKAISI_DAY); // 撹拌開始日
        itemKaisiTime = getItemRow(processData.getItemList(), GXHDO102B028Const.KAKUHANKAISI_TIME); // 撹拌開始時間
        itemSyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B028Const.KAKUHANSYUURYOU_DAY); // 撹拌終了日
        itemSyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B028Const.KAKUHANSYUURYOU_TIME); // 撹拌終了時間
        errorMsg = checkItemR001(itemKaisiDay, itemKaisiTime, itemSyuuryouDay, itemSyuuryouTime);
        if (errorMsg != null) {
            return errorMsg;
        }

        // 乾燥開始日時、乾燥終了日時前後チェック
        itemKaisiDay = getItemRow(processData.getItemList(), GXHDO102B028Const.KANSOUKAISI_DAY); // 乾燥開始日
        itemKaisiTime = getItemRow(processData.getItemList(), GXHDO102B028Const.KANSOUKAISI_TIME); // 乾燥開始時間
        itemSyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B028Const.KANSOUSYUURYOU_DAY); // 乾燥終了日
        itemSyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B028Const.KANSOUSYUURYOU_TIME); // 乾燥終了時間
        errorMsg = checkItemR001(itemKaisiDay, itemKaisiTime, itemSyuuryouDay, itemSyuuryouTime);
        if (errorMsg != null) {
            return errorMsg;
        }

        // 2回目撹拌開始日時、2回目撹拌終了日時前後チェック
        itemKaisiDay = getItemRow(processData.getItemList(), GXHDO102B028Const.NIKAIMEKAKUHANKAISI_DAY); // 2回目撹拌開始日
        itemKaisiTime = getItemRow(processData.getItemList(), GXHDO102B028Const.NIKAIMEKAKUHANKAISI_TIME); // 2回目撹拌開始時間
        itemSyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B028Const.NKMKKHSR_DAY); // 2回目撹拌終了日
        itemSyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B028Const.NKMKKHSR_TIME); // 2回目撹拌終了時間
        errorMsg = checkItemR001(itemKaisiDay, itemKaisiTime, itemSyuuryouDay, itemSyuuryouTime);
        if (errorMsg != null) {
            return errorMsg;
        }

        // 2回目乾燥開始日時、2回目乾燥終了日時前後チェック
        itemKaisiDay = getItemRow(processData.getItemList(), GXHDO102B028Const.NIKAIMEKANSOUKAISI_DAY); // 2回目乾燥開始日
        itemKaisiTime = getItemRow(processData.getItemList(), GXHDO102B028Const.NIKAIMEKANSOUKAISI_TIME); // 2回目乾燥開始時間
        itemSyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B028Const.NIKAIMEKANSOUSYUURYOU_DAY); // 2回目乾燥終了日
        itemSyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B028Const.NIKAIMEKANSOUSYUURYOU_TIME); // 2回目乾燥終了時間
        errorMsg = checkItemR001(itemKaisiDay, itemKaisiTime, itemSyuuryouDay, itemSyuuryouTime);
        if (errorMsg != null) {
            return errorMsg;
        }

        // 3回目撹拌開始日時、3回目撹拌終了日時前後チェック
        itemKaisiDay = getItemRow(processData.getItemList(), GXHDO102B028Const.SANKAIMEKAKUHANKAISI_DAY); // 3回目撹拌開始日
        itemKaisiTime = getItemRow(processData.getItemList(), GXHDO102B028Const.SANKAIMEKAKUHANKAISI_TIME); // 3回目撹拌開始時間
        itemSyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B028Const.SKMKKHSR_DAY); // 3回目撹拌終了日
        itemSyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B028Const.SKMKKHSR_TIME); // 3回目撹拌終了時間
        errorMsg = checkItemR001(itemKaisiDay, itemKaisiTime, itemSyuuryouDay, itemSyuuryouTime);
        if (errorMsg != null) {
            return errorMsg;
        }

        // 3回目乾燥開始日時、3回目乾燥終了日時前後チェック
        itemKaisiDay = getItemRow(processData.getItemList(), GXHDO102B028Const.SANKAIMEKANSOUKAISI_DAY); // 3回目乾燥開始日
        itemKaisiTime = getItemRow(processData.getItemList(), GXHDO102B028Const.SANKAIMEKANSOUKAISI_TIME); // 3回目乾燥開始時間
        itemSyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B028Const.SANKAIMEKANSOUSYUURYOU_DAY); // 3回目乾燥終了日
        itemSyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B028Const.SANKAIMEKANSOUSYUURYOU_TIME); // 3回目乾燥終了時間
        errorMsg = checkItemR001(itemKaisiDay, itemKaisiTime, itemSyuuryouDay, itemSyuuryouTime);
        if (errorMsg != null) {
            return errorMsg;
        }

        // F/P開始日時、F/P終了日時前後チェック
        itemKaisiDay = getItemRow(processData.getItemList(), GXHDO102B028Const.FPKAISI_DAY); // F/P開始日
        itemKaisiTime = getItemRow(processData.getItemList(), GXHDO102B028Const.FPKAISI_TIME); // F/P開始時間
        itemSyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B028Const.FPSYUURYOU_DAY); // F/P終了日
        itemSyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B028Const.FPSYUURYOU_TIME); // F/P終了時間
        errorMsg = checkItemR001(itemKaisiDay, itemKaisiTime, itemSyuuryouDay, itemSyuuryouTime);
        if (errorMsg != null) {
            return errorMsg;
        }

        // F/P停止日時、F/P再開日時前後チェック
        itemKaisiDay = getItemRow(processData.getItemList(), GXHDO102B028Const.FPTEISI_DAY); // F/P停止日
        itemKaisiTime = getItemRow(processData.getItemList(), GXHDO102B028Const.FPTEISI_TIME); // F/P停止時間
        itemSyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B028Const.FPSAIKAI_DAY); // F/P再開日
        itemSyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B028Const.FPSAIKAI_TIME); // F/P再開時間
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
            SrSlipSlurrykokeibuntyouseiSutenyouki tmpSrSlipSlurrykokeibuntyouseiSutenyouki = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrSlipSlurrykokeibuntyouseiSutenyouki> srSlipSlurrykokeibuntyouseiSutenyoukiList = getSrSlipSlurrykokeibuntyouseiSutenyoukiData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo9, edaban);
                if (!srSlipSlurrykokeibuntyouseiSutenyoukiList.isEmpty()) {
                    tmpSrSlipSlurrykokeibuntyouseiSutenyouki = srSlipSlurrykokeibuntyouseiSutenyoukiList.get(0);
                }

                deleteTmpSrSlipSlurrykokeibuntyouseiSutenyouki(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);
                deleteTmpSubSrSlipSlurrykokeibuntyouseiSutenyouki(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);
            }

            // ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)_登録処理
            insertSrSlipSlurrykokeibuntyouseiSutenyouki(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, strSystime, processData, tmpSrSlipSlurrykokeibuntyouseiSutenyouki);
            // ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力ｻﾌﾞ画面の仮登録更新処理
            for (int i = 1; i <= 4; i++) {
                insertSubSrSlipSlurrykokeibuntyouseiSutenyouki(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, i, strSystime, processData, tmpSrSlipSlurrykokeibuntyouseiSutenyouki);
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
        processData.setUserAuthParam(GXHDO102B028Const.USER_AUTH_UPDATE_PARAM);

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

            // ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)_更新処理
            SrSlipSlurrykokeibuntyouseiSutenyouki srSlipSlurrykokeibuntyouseiSutenyouki = updateSrSlipSlurrykokeibuntyouseiSutenyouki(queryRunnerQcdb, conQcdb, rev, 
                    processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);
            // ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力ｻﾌﾞ画面の更新処理
            for (int i = 1; i <= 4; i++) {
                updateSubSrSlipSlurrykokeibuntyouseiSutenyouki(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, i, strSystime, srSlipSlurrykokeibuntyouseiSutenyouki, processData);
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
        processData.setUserAuthParam(GXHDO102B028Const.USER_AUTH_DELETE_PARAM);

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

            // ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo9, edaban, paramJissekino);
            insertDeleteDataTmpSrSlipSlurrykokeibuntyouseiSutenyouki(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo9, edaban, strSystime);

            // ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力_ｻﾌﾞ画面仮登録登録処理
            insertDeleteDataTmpSubSrSlipSlurrykokeibuntyouseiSutenyouki(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo9, edaban, strSystime);

            // ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)_削除処理
            deleteSrSlipSlurrykokeibuntyouseiSutenyouki(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);

            // ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力_ｻﾌﾞ画面削除処理
            deleteSubSrSlipSlurrykokeibuntyouseiSutenyouki(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);

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
     * ﾊﾞｲﾝﾀﾞｰ混合日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setBinderkongouDateTime(ProcessData processData) {
        Date syuuryounichijiVal = null;
        Date binderkongounichijiVal = null;  
        
        FXHDD01 binderkongouDay = getItemRow(processData.getItemList(), GXHDO102B028Const.BINDERKONGOU_DAY);
        FXHDD01 binderkongouTime = getItemRow(processData.getItemList(), GXHDO102B028Const.BINDERKONGOU_TIME);
        FXHDD01 slurrykeikanisuu = getItemRow(processData.getItemList(), GXHDO102B028Const.SLURRYKEIKANISUU);
        if (binderkongouDay == null || binderkongouTime == null || slurrykeikanisuu == null) {
            processData.setMethod("");
            return processData;
        }
        if (StringUtil.isEmpty(binderkongouDay.getValue()) && StringUtil.isEmpty(binderkongouTime.getValue())) {
            // ﾊﾞｲﾝﾀﾞｰ混合日時
            setDateTimeItem(binderkongouDay, binderkongouTime, new Date());
        }

        binderkongounichijiVal =  DateUtil.convertStringToDate(binderkongouDay.getValue(), binderkongouTime.getValue());
        
        FXHDD01 funsaisyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B028Const.FUNSAISYUURYOU_DAY);
        FXHDD01 funsaisyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B028Const.FUNSAISYUURYOU_TIME);
        if (funsaisyuuryouDay == null || funsaisyuuryouTime == null) {
            processData.setMethod("");
            return processData;
        }
        if (!StringUtil.isEmpty(funsaisyuuryouDay.getValue()) && !StringUtil.isEmpty(funsaisyuuryouTime.getValue())) {
            // 粉砕終了日時        
            syuuryounichijiVal = DateUtil.convertStringToDate(funsaisyuuryouDay.getValue(), funsaisyuuryouTime.getValue());           

            if (binderkongounichijiVal != null && syuuryounichijiVal != null) {
                // 日付の差分日数取得処理
                int diffDays = DateUtil.diffDaysRoundingMode(syuuryounichijiVal,binderkongounichijiVal, RoundingMode.CEILING);
                // ｽﾗﾘｰ経過日数の設定
                slurrykeikanisuu.setValue(BigDecimal.valueOf(diffDays).toPlainString());
            }
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 画面表示仕様(19)を発行する。
     *
     * @param queryRunnerQcdb オブジェクト
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo LotNo
     * @param edaban 枝番
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    public static Map getSyuuryounichiji(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban) throws SQLException {
        // データの取得
        String sql = "SELECT syuuryounichiji FROM sr_yuudentai_funsai"
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? ";
        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * 撹拌開始日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKakuhankaisiDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B028Const.KAKUHANKAISI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B028Const.KAKUHANKAISI_TIME);
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
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B028Const.KAKUHANSYUURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B028Const.KAKUHANSYUURYOU_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 乾燥開始日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKansoukaisiDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B028Const.KANSOUKAISI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B028Const.KANSOUKAISI_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 乾燥終了日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKansousyuuryouDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B028Const.KANSOUSYUURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B028Const.KANSOUSYUURYOU_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 溶剤秤量日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setYouzaikeiryouDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B028Const.YOUZAIKEIRYOU_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B028Const.YOUZAIKEIRYOU_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 2回目撹拌開始日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setNikaimekakuhankaisiDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B028Const.NIKAIMEKAKUHANKAISI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B028Const.NIKAIMEKAKUHANKAISI_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 2回目撹拌終了日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setNkmkkhsrDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B028Const.NKMKKHSR_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B028Const.NKMKKHSR_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 2回目乾燥開始日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setNikaimekansoukaisiDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B028Const.NIKAIMEKANSOUKAISI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B028Const.NIKAIMEKANSOUKAISI_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 2回目乾燥終了日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setNikaimekansousyuuryouDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B028Const.NIKAIMEKANSOUSYUURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B028Const.NIKAIMEKANSOUSYUURYOU_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 2回目溶剤秤量日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setNikaimeyouzaikeiryouDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B028Const.NKMYZKEIRYOU_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B028Const.NKMYZKEIRYOU_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 3回目撹拌開始日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setSankaimekakuhankaisiDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B028Const.SANKAIMEKAKUHANKAISI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B028Const.SANKAIMEKAKUHANKAISI_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 3回目撹拌終了日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setSkmkkhsrDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B028Const.SKMKKHSR_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B028Const.SKMKKHSR_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 3回目乾燥開始日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setSankaimekansoukaisiDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B028Const.SANKAIMEKANSOUKAISI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B028Const.SANKAIMEKANSOUKAISI_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 3回目乾燥終了日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setSankaimekansousyuuryouDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B028Const.SANKAIMEKANSOUSYUURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B028Const.SANKAIMEKANSOUSYUURYOU_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * F/P開始日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setFpkaisiDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B028Const.FPKAISI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B028Const.FPKAISI_TIME);
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
    public ProcessData setFpteisiDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B028Const.FPTEISI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B028Const.FPTEISI_TIME);
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
    public ProcessData setFpsaikaiDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B028Const.FPSAIKAI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B028Const.FPSAIKAI_TIME);
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
    public ProcessData setFpsyuuryouDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B028Const.FPSYUURYOU_DAY); // F/P終了日
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B028Const.FPSYUURYOU_TIME); // F/P終了時間
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
        FXHDD01 itemFpkaisiDay = getItemRow(processData.getItemList(), GXHDO102B028Const.FPKAISI_DAY); // F/P開始日
        FXHDD01 itemFpkaisiTime = getItemRow(processData.getItemList(), GXHDO102B028Const.FPKAISI_TIME); // F/P開始時間

        // 「F/P開始日」ﾁｪｯｸ
        if (itemFpkaisiDay != null && StringUtil.isEmpty(itemFpkaisiDay.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemFpkaisiDay);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemFpkaisiDay.getLabel1());
        }
        // 「F/P開始時間」ﾁｪｯｸ
        if (itemFpkaisiDay != null && StringUtil.isEmpty(itemFpkaisiTime.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemFpkaisiTime);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemFpkaisiTime.getLabel1());
        }

        FXHDD01 itemFpteisiDay = getItemRow(processData.getItemList(), GXHDO102B028Const.FPTEISI_DAY); // F/P停止日
        FXHDD01 itemFpteisiTime = getItemRow(processData.getItemList(), GXHDO102B028Const.FPTEISI_TIME); // F/P停止時間
        FXHDD01 itemFpsaikaiDay = getItemRow(processData.getItemList(), GXHDO102B028Const.FPSAIKAI_DAY); // F/P再開日
        FXHDD01 itemFpsaikaiTime = getItemRow(processData.getItemList(), GXHDO102B028Const.FPSAIKAI_TIME); // F/P再開時間
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
                return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errorFxhdd01List, errorMessageParams.toString());
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
        FXHDD01 itemFpjikan = getItemRow(processData.getItemList(), GXHDO102B028Const.FPJIKAN); // F/P時間
        FXHDD01 itemFpteisiDay = getItemRow(processData.getItemList(), GXHDO102B028Const.FPTEISI_DAY); // F/P停止日
        FXHDD01 itemFpteisiTime = getItemRow(processData.getItemList(), GXHDO102B028Const.FPTEISI_TIME); // F/P停止時間
        FXHDD01 itemFpsaikaiDay = getItemRow(processData.getItemList(), GXHDO102B028Const.FPSAIKAI_DAY); // F/P再開日
        FXHDD01 itemFpsaikaiTime = getItemRow(processData.getItemList(), GXHDO102B028Const.FPSAIKAI_TIME); // F/P再開時間
        if (itemFpjikan == null) {
            return;
        }
        // Dateオブジェクト変換
        Date fpsyuuryouDate = getDateTimeItem(processData, GXHDO102B028Const.FPSYUURYOU_DAY, GXHDO102B028Const.FPSYUURYOU_TIME); // F/P終了日時
        Date fpkaisiDate = getDateTimeItem(processData, GXHDO102B028Const.FPKAISI_DAY, GXHDO102B028Const.FPKAISI_TIME); // F/P開始日時
        Date fpteisiDate = getDateTimeItem(processData, GXHDO102B028Const.FPTEISI_DAY, GXHDO102B028Const.FPTEISI_TIME); // F/P停止日時
        Date fpsaikaiDate = getDateTimeItem(processData, GXHDO102B028Const.FPSAIKAI_DAY, GXHDO102B028Const.FPSAIKAI_TIME); // F/P再開日時
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
     * ｽﾗﾘｰ重量計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setSlurryjyuuryou(ProcessData processData) {
        FXHDD01 itemSoujyuuryou = getItemRow(processData.getItemList(), GXHDO102B028Const.SLURRYSOUJYUURYOU); // 総重量
        FXHDD01 itemSlipjyouhou_fuutaijyuuryou = getItemRow(processData.getItemList(), GXHDO102B028Const.SLIPJYOUHOU_FUUTAIJYUURYOU); // ｽﾘｯﾌﾟ情報_風袋重量
        FXHDD01 itemSlurryjyuuryou = getItemRow(processData.getItemList(), GXHDO102B028Const.SLURRYJYUURYOU); // ｽﾗﾘｰ重量
        if (itemSoujyuuryou == null || itemSlipjyouhou_fuutaijyuuryou == null || itemSlurryjyuuryou == null) {
            processData.setMethod("");
            return processData;
        }
        // 【ｽﾗﾘｰ重量計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkSlurryjyuuryouKeisan(itemSoujyuuryou, itemSlipjyouhou_fuutaijyuuryou);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        // 【ｽﾗﾘｰ重量計算】ﾎﾞﾀﾝ押下時計算処理
        calcSlurryjyuuryou(itemSoujyuuryou, itemSlipjyouhou_fuutaijyuuryou, itemSlurryjyuuryou);
        processData.setMethod("");
        return processData;
    }

    /**
     * 【ｽﾗﾘｰ重量計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
     *
     * @param processData 処理制御データ
     * @param itemSoujyuuryou 総重量
     * @param itemSlipjyouhou_fuutaijyuuryou ｽﾘｯﾌﾟ情報_風袋重量
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkSlurryjyuuryouKeisan(FXHDD01 itemSoujyuuryou, FXHDD01 itemSlipjyouhou_fuutaijyuuryou) {
        // 「総重量」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemSoujyuuryou.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemSoujyuuryou);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemSoujyuuryou.getLabel1());
        }
        
        // 「ｽﾘｯﾌﾟ情報_風袋重量」ﾁｪｯｸ
        if (itemSlipjyouhou_fuutaijyuuryou != null) {
            BigDecimal slipjyouhou_fuutaijyuuryouVal = ValidateUtil.getItemKikakuChiCheckVal(itemSlipjyouhou_fuutaijyuuryou); // ｽﾘｯﾌﾟ情報_風袋重量の規格値
            if (slipjyouhou_fuutaijyuuryouVal == null) {
                itemSlipjyouhou_fuutaijyuuryou.setBackColor3(ErrUtil.ERR_BACK_COLOR);
                // ｴﾗｰ項目をﾘｽﾄに追加
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemSlipjyouhou_fuutaijyuuryou);
                return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemSlipjyouhou_fuutaijyuuryou.getLabel1());
            }
        }
        return null;
    }

    /**
     * 【ｽﾗﾘｰ重量計算】ﾎﾞﾀﾝ押下時計算処理
     *
     * @param processData 処理制御データ
     * @param itemSoujyuuryou 総重量
     * @param itemSlipjyouhou_fuutaijyuuryou ｽﾘｯﾌﾟ情報_風袋重量
     * @param itemSlurryjyuuryou ｽﾗﾘｰ重量
     */
    private void calcSlurryjyuuryou(FXHDD01 itemSoujyuuryou, FXHDD01 itemSlipjyouhou_fuutaijyuuryou, FXHDD01 itemSlurryjyuuryou) {
        try {
            BigDecimal itemSoujyuuryouVal = new BigDecimal(itemSoujyuuryou.getValue()); // 総重量の値
            BigDecimal itemSlipjyouhou_fuutaijyuuryouVal = ValidateUtil.getItemKikakuChiCheckVal(itemSlipjyouhou_fuutaijyuuryou); // ｽﾘｯﾌﾟ情報_風袋重量の規格値

            // 「ｽﾗﾘｰ重量」計算処理:「総重量」 - 「ｽﾘｯﾌﾟ情報_風袋重量」 を算出する。
            BigDecimal itemSlurryjyuuryouVal = itemSoujyuuryouVal.subtract(itemSlipjyouhou_fuutaijyuuryouVal);
            itemSlurryjyuuryou.setValue(itemSlurryjyuuryouVal.toPlainString());

        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
            ErrUtil.outputErrorLog(itemSlurryjyuuryou.getLabel1() + "計算処理にエラー発生", ex, LOGGER);
        }
    }

    /**
     * 乾燥後正味重量計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKansougosyoumijyuuryou(ProcessData processData) {
        ErrorMessageInfo checkItemErrorInfo = checkKansougosyoumijyuuryouKeisan(processData, GXHDO102B028Const.KANSOUGOSOUJYUURYOU, GXHDO102B028Const.KKBSKT_FUUTAIJYUURYOU, GXHDO102B028Const.KANSOUMAESLURRYJYUURYOU);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        calcKansougosyoumijyuuryou(processData, GXHDO102B028Const.KANSOUGOSOUJYUURYOU, GXHDO102B028Const.KKBSKT_FUUTAIJYUURYOU, GXHDO102B028Const.KANSOUGOSYOUMIJYUURYOU,
                GXHDO102B028Const.KOKEIBUNHIRITU, GXHDO102B028Const.KANSOUMAESLURRYJYUURYOU);
        return processData;
    }

    /**
     * 2回目乾燥後正味重量計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setNikaimekansougosyoumijyuuryou(ProcessData processData) {
        ErrorMessageInfo checkItemErrorInfo = checkKansougosyoumijyuuryouKeisan(processData, GXHDO102B028Const.NIKAIMEKANSOUGOSOUJYUURYOU, GXHDO102B028Const.NKMARMZFTJR, GXHDO102B028Const.NKMKSMSLRJR);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        calcKansougosyoumijyuuryou(processData, GXHDO102B028Const.NIKAIMEKANSOUGOSOUJYUURYOU, GXHDO102B028Const.NKMARMZFTJR, GXHDO102B028Const.NKMKSGSMJR,
                GXHDO102B028Const.NIKAIMEKOKEIBUNHIRITU, GXHDO102B028Const.NKMKSMSLRJR);
        return processData;
    }

    /**
     * 3回目乾燥後正味重量計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setSankaimekansougosyoumijyuuryou(ProcessData processData) {
        ErrorMessageInfo checkItemErrorInfo = checkKansougosyoumijyuuryouKeisan(processData, GXHDO102B028Const.SANKAIMEKANSOUGOSOUJYUURYOU, GXHDO102B028Const.SKMARMZFTJR, GXHDO102B028Const.SKMKSMSLRJR);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        calcKansougosyoumijyuuryou(processData, GXHDO102B028Const.SANKAIMEKANSOUGOSOUJYUURYOU, GXHDO102B028Const.SKMARMZFTJR, GXHDO102B028Const.SKMKSGSMJR,
                GXHDO102B028Const.SANKAIMEKOKEIBUNHIRITU, GXHDO102B028Const.SKMKSMSLRJR);
        return processData;
    }

    /**
     * 乾燥後正味重量、2回目乾燥後正味重量、3回目乾燥後正味重量計算ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
     *
     * @param processData 処理制御データ
     * @param kansougosoujyuuryouItemid 乾燥後総重量
     * @param fuutaijyuuryouItemid 風袋重量
     * @param kansoumaeslurryjyuuryouItemid 乾燥前ｽﾗﾘｰ重量
     * @return エラーメッセージ情報
     */
    public ErrorMessageInfo checkKansougosyoumijyuuryouKeisan(ProcessData processData, String kansougosoujyuuryouItemid, String fuutaijyuuryouItemid, String kansoumaeslurryjyuuryouItemid) {
        // 乾燥後総重量
        FXHDD01 kansougosoujyuuryou = getItemRow(processData.getItemList(), kansougosoujyuuryouItemid);
        // 風袋重量
        FXHDD01 fuutaijyuuryou = getItemRow(processData.getItemList(), fuutaijyuuryouItemid);
        // 乾燥前ｽﾗﾘｰ重量
        FXHDD01 kansoumaeslurryjyuuryou = getItemRow(processData.getItemList(), kansoumaeslurryjyuuryouItemid);
        //「乾燥後総重量」ﾁｪｯｸ
        if (kansougosoujyuuryou != null && StringUtil.isEmpty(kansougosoujyuuryou.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(kansougosoujyuuryou);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, true, errFxhdd01List, kansougosoujyuuryou.getLabel1());
        }
        //「風袋重量」ﾁｪｯｸ
        if (fuutaijyuuryou != null && StringUtil.isEmpty(fuutaijyuuryou.getValue())) {
            List<FXHDD01> errFxhdd01List = Arrays.asList(fuutaijyuuryou);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, true, errFxhdd01List, fuutaijyuuryou.getLabel1());
        }
        if (kansougosoujyuuryou != null && fuutaijyuuryou != null) {
            // [乾燥後総重量]<[風袋重量]場合
            BigDecimal kansougosoujyuuryouVal = new BigDecimal(kansougosoujyuuryou.getValue());
            BigDecimal fuutaijyuuryouVal = new BigDecimal(fuutaijyuuryou.getValue());
            if (kansougosoujyuuryouVal.compareTo(fuutaijyuuryouVal) < 0) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                List<FXHDD01> errFxhdd01List = Arrays.asList(kansougosoujyuuryou, fuutaijyuuryou);
                return MessageUtil.getErrorMessageInfo("XHD-000023", true, true, errFxhdd01List, kansougosoujyuuryou.getLabel1(), fuutaijyuuryou.getLabel1());
            }
        }
        // 「乾燥前ｽﾗﾘｰ重量」ﾁｪｯｸ
        if (kansoumaeslurryjyuuryou != null) {
            if ("".equals(StringUtil.nullToBlank(kansoumaeslurryjyuuryou.getValue())) || BigDecimal.ZERO.compareTo(new BigDecimal(kansoumaeslurryjyuuryou.getValue())) == 0) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                List<FXHDD01> errFxhdd01List = Arrays.asList(kansoumaeslurryjyuuryou);
                return MessageUtil.getErrorMessageInfo("XHD-000181", true, true, errFxhdd01List, kansoumaeslurryjyuuryou.getLabel1());
            }
        }
        return null;
    }

    /**
     * 乾燥後正味重量、2回目乾燥後正味重量、3回目乾燥後正味重量の計算処理
     *
     * @param processData 処理制御データ
     * @param kansougosoujyuuryouItemid 乾燥後総重量
     * @param fuutaijyuuryouItemid 風袋重量
     * @param kansougosyoumijyuuryouItemid 乾燥後正味重量
     * @param kokeibunhirituItemid 固形分比率
     * @param kansoumaeslurryjyuuryouItemid 乾燥前ｽﾗﾘｰ重量
     */
    private void calcKansougosyoumijyuuryou(ProcessData processData, String kansougosoujyuuryouItemid, String fuutaijyuuryouItemid, String kansougosyoumijyuuryouItemid,
            String kokeibunhirituItemid, String kansoumaeslurryjyuuryouItemid) {
        // 乾燥後総重量
        FXHDD01 kansougosoujyuuryou = getItemRow(processData.getItemList(), kansougosoujyuuryouItemid);
        // 風袋重量
        FXHDD01 fuutaijyuuryou = getItemRow(processData.getItemList(), fuutaijyuuryouItemid);
        // 乾燥後正味重量
        FXHDD01 kansougosyoumijyuuryou = getItemRow(processData.getItemList(), kansougosyoumijyuuryouItemid);
        // 固形分比率
        FXHDD01 kokeibunhiritu = getItemRow(processData.getItemList(), kokeibunhirituItemid);
        // 乾燥前ｽﾗﾘｰ重量
        FXHDD01 kansoumaeslurryjyuuryou = getItemRow(processData.getItemList(), kansoumaeslurryjyuuryouItemid);
        try {
            if (kansougosoujyuuryou != null && fuutaijyuuryou != null && kansougosyoumijyuuryou != null) {
                //「乾燥後正味重量」= 「乾燥後総重量」 - 「風袋重量」 を算出する。
                BigDecimal itemKansougosoujyuuryouVal = new BigDecimal(kansougosoujyuuryou.getValue());
                BigDecimal itemKkbsktfuutaijyuuryouVal = new BigDecimal(fuutaijyuuryou.getValue());
                BigDecimal itemKansougosyoumijyuuryouVal = itemKansougosoujyuuryouVal.subtract(itemKkbsktfuutaijyuuryouVal);
                //計算結果の設定
                kansougosyoumijyuuryou.setValue(itemKansougosyoumijyuuryouVal.toPlainString());

                if (kokeibunhiritu != null && kansoumaeslurryjyuuryou != null) {
                    // 「固形分比率」計算処理
                    BigDecimal itemKansoumaeslurryjyuuryouVal = new BigDecimal(kansoumaeslurryjyuuryou.getValue());
                    // 「固形分比率」= 「乾燥後正味重量」 ÷ 「乾燥前ｽﾗﾘｰ重量」 * 100(小数点第3位を四捨五入) → 式を変換して先に100を乗算
                    BigDecimal kokeibunhirituVal = itemKansougosyoumijyuuryouVal.multiply(BigDecimal.valueOf(100)).divide(itemKansoumaeslurryjyuuryouVal, 2, RoundingMode.HALF_UP);
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
        ErrorMessageInfo checkItemErrorInfo = checkYouzaityouseiryouKeisan(processData, GXHDO102B028Const.SLURRYJYUURYOU, GXHDO102B028Const.KOKEIBUNHIRITU, GXHDO102B028Const.KANSOUKOKEIBUN);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        calcYouzaityouseiryou(processData, GXHDO102B028Const.SLURRYJYUURYOU, GXHDO102B028Const.KOKEIBUNHIRITU, GXHDO102B028Const.KANSOUKOKEIBUN,
                GXHDO102B028Const.YOUZAITYOUSEIRYOU, GXHDO102B028Const.TOLUENETYOUSEIRYOU, GXHDO102B028Const.SOLMIXTYOUSEIRYOU,
                GXHDO102B028Const.YOUZAI1_TYOUGOURYOUKIKAKU, GXHDO102B028Const.YOUZAI2_TYOUGOURYOUKIKAKU);
        return processData;
    }

    /**
     * 2回目溶剤調整量計算
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setNikaimeyouzaityouseiryou(ProcessData processData) {
        ErrorMessageInfo checkItemErrorInfo = checkYouzaityouseiryouKeisan(processData, GXHDO102B028Const.SLURRYJYUURYOU, GXHDO102B028Const.NIKAIMEKOKEIBUNHIRITU, GXHDO102B028Const.KANSOUKOKEIBUN);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        calcYouzaityouseiryou(processData, GXHDO102B028Const.SLURRYJYUURYOU, GXHDO102B028Const.NIKAIMEKOKEIBUNHIRITU, GXHDO102B028Const.KANSOUKOKEIBUN,
                GXHDO102B028Const.NKMYZTSR, GXHDO102B028Const.NIKAIMETOLUENETYOUSEIRYOU, GXHDO102B028Const.NIKAIMESOLMIXTYOUSEIRYOU,
                GXHDO102B028Const.NKMYZ1_TYOUGOURYOUKIKAKU, GXHDO102B028Const.NKMYZ2_TYOUGOURYOUKIKAKU);
        return processData;
    }

    /**
     * 溶剤調整量計算ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
     *
     * @param processData 処理制御データ
     * @param slurryjyuuryouItemid ｽﾗﾘｰ重量
     * @param kokeibunhirituItemid 固形分比率
     * @param kansoukokeibunItemid 乾燥固形分
     * @return エラーメッセージ情報
     */
    public ErrorMessageInfo checkYouzaityouseiryouKeisan(ProcessData processData, String slurryjyuuryouItemid, String kokeibunhirituItemid, String kansoukokeibunItemid) {
        // ｽﾗﾘｰ重量
        FXHDD01 slurryjyuuryou = getItemRow(processData.getItemList(), slurryjyuuryouItemid);
        // 固形分比率
        FXHDD01 kokeibunhiritu = getItemRow(processData.getItemList(), kokeibunhirituItemid);
        // 乾燥固形分
        FXHDD01 kansoukokeibun = getItemRow(processData.getItemList(), kansoukokeibunItemid);
        //「ｽﾗﾘｰ重量」ﾁｪｯｸ
        if (slurryjyuuryou != null && StringUtil.isEmpty(slurryjyuuryou.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(slurryjyuuryou);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, true, errFxhdd01List, slurryjyuuryou.getLabel1());
        }
        //「固形分比率」ﾁｪｯｸ
        if (kokeibunhiritu != null) {
            // 「固形分比率」ﾁｪｯｸ
            String itemKokeibunhirituVal = StringUtil.nullToBlank(kokeibunhiritu.getValue());
            if ("".equals(itemKokeibunhirituVal) || new BigDecimal(itemKokeibunhirituVal).compareTo(BigDecimal.ZERO) == 0) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                List<FXHDD01> errFxhdd01List = Arrays.asList(kokeibunhiritu);
                return MessageUtil.getErrorMessageInfo("XHD-000181", true, true, errFxhdd01List, kokeibunhiritu.getLabel1());
            }
        }
        //「乾燥固形分」ﾁｪｯｸ
        if (kansoukokeibun != null) {
            // 「乾燥固形分」ﾁｪｯｸ
            BigDecimal itemKansoukokeibunVal = ValidateUtil.getItemKikakuChiCheckVal(kansoukokeibun); // 乾燥固形分の規格値
            if (itemKansoukokeibunVal == null || BigDecimal.ZERO.compareTo(itemKansoukokeibunVal) == 0) {
                kansoukokeibun.setBackColor3(ErrUtil.ERR_BACK_COLOR);
                // ｴﾗｰ項目をﾘｽﾄに追加
                List<FXHDD01> errFxhdd01List = Arrays.asList(kansoukokeibun);
                return MessageUtil.getErrorMessageInfo("XHD-000181", true, true, errFxhdd01List, kansoukokeibun.getLabel1());
            }
        }

        return null;
    }

    /**
     * 溶剤調整量計算
     *
     * @param processData 処理制御データ
     * @param slurryjyuuryouItemid ｽﾗﾘｰ重量
     * @param kokeibunhirituItemid 固形分比率
     * @param kansoukokeibunItemid 乾燥固形分
     * @param youzaityouseiryouItemid 溶剤調整量
     * @param toluenetyouseiryouItemid ﾄﾙｴﾝ調整量
     * @param solmixtyouseiryouItemid ｿﾙﾐｯｸｽ調整量
     * @param youzai1_tyougouryoukikakuItemid 溶剤1_調合量規格
     * @param youzai2_tyougouryoukikakuItemid 溶剤2_調合量規格
     */
    private void calcYouzaityouseiryou(ProcessData processData, String slurryjyuuryouItemid, String kokeibunhirituItemid, String kansoukokeibunItemid,
            String youzaityouseiryouItemid, String toluenetyouseiryouItemid, String solmixtyouseiryouItemid, String youzai1_tyougouryoukikakuItemid,
            String youzai2_tyougouryoukikakuItemid) {
        // ｽﾗﾘｰ重量
        FXHDD01 slurryjyuuryou = getItemRow(processData.getItemList(), slurryjyuuryouItemid);
        // 固形分比率
        FXHDD01 kokeibunhiritu = getItemRow(processData.getItemList(), kokeibunhirituItemid);
        // 乾燥固形分
        FXHDD01 kansoukokeibun = getItemRow(processData.getItemList(), kansoukokeibunItemid);
        // 溶剤調整量
        FXHDD01 youzaityouseiryou = getItemRow(processData.getItemList(), youzaityouseiryouItemid);
        // ﾄﾙｴﾝ調整量
        FXHDD01 toluenetyouseiryou = getItemRow(processData.getItemList(), toluenetyouseiryouItemid);
        // ｿﾙﾐｯｸｽ調整量
        FXHDD01 solmixtyouseiryou = getItemRow(processData.getItemList(), solmixtyouseiryouItemid);
        // 溶剤1_調合量規格
        FXHDD01 youzai1Tyougouryoukikaku = getItemRow(processData.getItemList(), youzai1_tyougouryoukikakuItemid);
        // 溶剤2_調合量規格
        FXHDD01 youzai2Tyougouryoukikaku = getItemRow(processData.getItemList(), youzai2_tyougouryoukikakuItemid);
        try {
            if (slurryjyuuryou != null && kokeibunhiritu != null && kansoukokeibun != null && youzaityouseiryou != null) {
                //「溶剤調整量」= ｽﾗﾘｰ重量 ×固形分比率 ×乾燥固形分 -ｽﾗﾘｰ重量 を算出する。
                BigDecimal slurryjyuuryouVal = new BigDecimal(slurryjyuuryou.getValue());
                BigDecimal kokeibunhirituVal = new BigDecimal(kokeibunhiritu.getValue());
                BigDecimal kansoukokeibunVal = ValidateUtil.getItemKikakuChiCheckVal(kansoukokeibun); // 溶乾燥固形分の規格値
                BigDecimal youzaityouseiryouVal = slurryjyuuryouVal.multiply(kokeibunhirituVal.divide(new BigDecimal(100))).multiply(kansoukokeibunVal.divide(new BigDecimal(100))).subtract(slurryjyuuryouVal);
                // 計算結果の設定
                youzaityouseiryou.setValue(youzaityouseiryouVal.setScale(0, RoundingMode.HALF_UP).toPlainString());
                // 溶剤調整量 ÷ 2
                BigDecimal youzaityouseiryou2Val = youzaityouseiryouVal.divide(new BigDecimal(2), 0, RoundingMode.HALF_UP);
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
                // 溶剤①_調合量規格、溶剤①_調合量規格に調整量の計算結果後ろに「±0」を付けて設定する。
                String youzaityouseiryou2KikakuChiStr = "【" + youzaityouseiryou2Val.toPlainString() + "±0" + "】";
                // 溶剤1_調合量規格
                if (youzai1Tyougouryoukikaku != null) {
                    //計算結果の設定
                    youzai1Tyougouryoukikaku.setKikakuChi(youzaityouseiryou2KikakuChiStr);
                }
                // 溶剤2_調合量規格
                if (youzai2Tyougouryoukikaku != null) {
                    //計算結果の設定
                    youzai2Tyougouryoukikaku.setKikakuChi(youzaityouseiryou2KikakuChiStr);
                }
            }
        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
            ErrUtil.outputErrorLog(youzaityouseiryou.getLabel1() + "計算にエラー発生", ex, LOGGER);
        }
    }

    /**
     * 正味重量計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setSyoumijyuuryou(ProcessData processData) {
        ErrorMessageInfo checkItemErrorInfo = checkSyoumijyuuryouKeisan(processData, GXHDO102B028Const.SOUJYUURYOU, GXHDO102B028Const.FPJYUNBI_FUUTAIJYUURYOU);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        calcSyoumijyuuryou(processData, GXHDO102B028Const.SOUJYUURYOU, GXHDO102B028Const.FPJYUNBI_FUUTAIJYUURYOU, GXHDO102B028Const.SYOUMIJYUURYOU);
        return processData;
    }

    /**
     * 正味重量計算ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
     *
     * @param processData 処理制御データ
     * @param soujyuuryouItemid 総重量
     * @param fuutaijyuuryouItemid 風袋重量
     * @return エラーメッセージ情報
     */
    public ErrorMessageInfo checkSyoumijyuuryouKeisan(ProcessData processData, String soujyuuryouItemid, String fuutaijyuuryouItemid) {
        // 総重量
        FXHDD01 soujyuuryou = getItemRow(processData.getItemList(), soujyuuryouItemid);
        // F/P準備_風袋重量
        FXHDD01 fuutaijyuuryou = getItemRow(processData.getItemList(), fuutaijyuuryouItemid);
        //「総重量」ﾁｪｯｸ
        if (soujyuuryou != null && StringUtil.isEmpty(soujyuuryou.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(soujyuuryou);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, true, errFxhdd01List, soujyuuryou.getLabel1());
        }
        //「F/P準備_風袋重量」ﾁｪｯｸ
        if (fuutaijyuuryou != null && StringUtil.isEmpty(fuutaijyuuryou.getValue())) {
            List<FXHDD01> errFxhdd01List = Arrays.asList(fuutaijyuuryou);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, true, errFxhdd01List, fuutaijyuuryou.getLabel1());
        }
        if (soujyuuryou != null && fuutaijyuuryou != null) {
            // [総重量]<[F/P準備_風袋重量]場合
            BigDecimal kansougosoujyuuryouVal = new BigDecimal(soujyuuryou.getValue());
            BigDecimal fuutaijyuuryouVal = new BigDecimal(fuutaijyuuryou.getValue());
            if (kansougosoujyuuryouVal.compareTo(fuutaijyuuryouVal) < 0) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                List<FXHDD01> errFxhdd01List = Arrays.asList(soujyuuryou, fuutaijyuuryou);
                return MessageUtil.getErrorMessageInfo("XHD-000023", true, true, errFxhdd01List, soujyuuryou.getLabel1(), fuutaijyuuryou.getLabel1());
            }
        }
        return null;
    }

    /**
     * 正味重量計算
     *
     * @param processData 処理制御データ
     * @param soujyuuryouItemid 総重量
     * @param fuutaijyuuryouItemid 風袋重量
     * @param syoumijyuuryouItemid 正味重量
     */
    private void calcSyoumijyuuryou(ProcessData processData, String soujyuuryouItemid, String fuutaijyuuryouItemid, String syoumijyuuryouItemid) {
        // 総重量
        FXHDD01 kansougosoujyuuryou = getItemRow(processData.getItemList(), soujyuuryouItemid);
        // F/P準備_風袋重量
        FXHDD01 FPjyunbi_fuutaijyuuryou = getItemRow(processData.getItemList(), fuutaijyuuryouItemid);
        // 正味重量
        FXHDD01 syoumijyuuryou = getItemRow(processData.getItemList(), syoumijyuuryouItemid);
        try {
            if (kansougosoujyuuryou != null && FPjyunbi_fuutaijyuuryou != null && syoumijyuuryou != null) {
                //「正味重量」= 「総重量」 - 「F/P準備_風袋重量」 を算出する。
                BigDecimal itemKansougosoujyuuryouVal = new BigDecimal(kansougosoujyuuryou.getValue());
                BigDecimal itemFPjyunbi_fuutaijyuuryouVal = new BigDecimal(FPjyunbi_fuutaijyuuryou.getValue());
                BigDecimal itemKansougosyoumijyuuryouVal = itemKansougosoujyuuryouVal.subtract(itemFPjyunbi_fuutaijyuuryouVal);
                //計算結果の設定
                syoumijyuuryou.setValue(itemKansougosyoumijyuuryouVal.toPlainString());
            }
        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
            ErrUtil.outputErrorLog("正味重量計算にエラー発生", ex, LOGGER);
        }
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
                        GXHDO102B028Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B028Const.BTN_BINDERKONGOU_TOP,
                        GXHDO102B028Const.BTN_SLURRYJYUURYOU_TOP,
                        GXHDO102B028Const.BTN_KAKUHANKAISI_TOP,
                        GXHDO102B028Const.BTN_KAKUHANSYUURYOU_TOP,
                        GXHDO102B028Const.BTN_KANSOUKAISI_TOP,
                        GXHDO102B028Const.BTN_KANSOUSYUURYOU_TOP,
                        GXHDO102B028Const.BTN_KANSOUGOSYOUMIJYUURYOU_TOP,
                        GXHDO102B028Const.BTN_YOUZAITYOUSEIRYOU_TOP,
                        GXHDO102B028Const.BTN_YOUZAIKEIRYOU_TOP,
                        GXHDO102B028Const.BTN_NIKAIMEKAKUHANKAISI_TOP,
                        GXHDO102B028Const.BTN_NKMKKHSR_TOP,
                        GXHDO102B028Const.BTN_NIKAIMEKANSOUKAISI_TOP,
                        GXHDO102B028Const.BTN_NIKAIMEKANSOUSYUURYOU_TOP,
                        GXHDO102B028Const.BTN_NKMKSGSMJR_TOP,
                        GXHDO102B028Const.BTN_NKMYZTSR_TOP,
                        GXHDO102B028Const.BTN_NIKAIMEYOUZAIKEIRYOU_TOP,
                        GXHDO102B028Const.BTN_SANKAIMEKAKUHANKAISI_TOP,
                        GXHDO102B028Const.BTN_SKMKKHSR_TOP,
                        GXHDO102B028Const.BTN_SANKAIMEKANSOUKAISI_TOP,
                        GXHDO102B028Const.BTN_SANKAIMEKANSOUSYUURYOU_TOP,
                        GXHDO102B028Const.BTN_SKMKSGSMJR_TOP,
                        GXHDO102B028Const.BTN_FPKAISI_TOP,
                        GXHDO102B028Const.BTN_FPTEISI_TOP,
                        GXHDO102B028Const.BTN_FPSAIKAI_TOP,
                        GXHDO102B028Const.BTN_FPSYUURYOU_TOP,
                        GXHDO102B028Const.BTN_SYOUMIJYUURYOU_TOP,
                        GXHDO102B028Const.BTN_UPDATE_TOP,
                        GXHDO102B028Const.BTN_DELETE_TOP,
                        GXHDO102B028Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B028Const.BTN_BINDERKONGOU_BOTTOM,
                        GXHDO102B028Const.BTN_SLURRYJYUURYOU_BOTTOM,
                        GXHDO102B028Const.BTN_KAKUHANKAISI_BOTTOM,
                        GXHDO102B028Const.BTN_KAKUHANSYUURYOU_BOTTOM,
                        GXHDO102B028Const.BTN_KANSOUKAISI_BOTTOM,
                        GXHDO102B028Const.BTN_KANSOUSYUURYOU_BOTTOM,
                        GXHDO102B028Const.BTN_KANSOUGOSYOUMIJYUURYOU_BOTTOM,
                        GXHDO102B028Const.BTN_YOUZAITYOUSEIRYOU_BOTTOM,
                        GXHDO102B028Const.BTN_YOUZAIKEIRYOU_BOTTOM,
                        GXHDO102B028Const.BTN_NIKAIMEKAKUHANKAISI_BOTTOM,
                        GXHDO102B028Const.BTN_NKMKKHSR_BOTTOM,
                        GXHDO102B028Const.BTN_NIKAIMEKANSOUKAISI_BOTTOM,
                        GXHDO102B028Const.BTN_NIKAIMEKANSOUSYUURYOU_BOTTOM,
                        GXHDO102B028Const.BTN_NKMKSGSMJR_BOTTOM,
                        GXHDO102B028Const.BTN_NKMYZTSR_BOTTOM,
                        GXHDO102B028Const.BTN_NIKAIMEYOUZAIKEIRYOU_BOTTOM,
                        GXHDO102B028Const.BTN_SANKAIMEKAKUHANKAISI_BOTTOM,
                        GXHDO102B028Const.BTN_SKMKKHSR_BOTTOM,
                        GXHDO102B028Const.BTN_SANKAIMEKANSOUKAISI_BOTTOM,
                        GXHDO102B028Const.BTN_SANKAIMEKANSOUSYUURYOU_BOTTOM,
                        GXHDO102B028Const.BTN_SKMKSGSMJR_BOTTOM,
                        GXHDO102B028Const.BTN_FPKAISI_BOTTOM,
                        GXHDO102B028Const.BTN_FPTEISI_BOTTOM,
                        GXHDO102B028Const.BTN_FPSAIKAI_BOTTOM,
                        GXHDO102B028Const.BTN_FPSYUURYOU_BOTTOM,
                        GXHDO102B028Const.BTN_SYOUMIJYUURYOU_BOTTOM,
                        GXHDO102B028Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B028Const.BTN_DELETE_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B028Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B028Const.BTN_INSERT_TOP,
                        GXHDO102B028Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B028Const.BTN_INSERT_BOTTOM));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO102B028Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B028Const.BTN_BINDERKONGOU_TOP,
                        GXHDO102B028Const.BTN_SLURRYJYUURYOU_TOP,
                        GXHDO102B028Const.BTN_KAKUHANKAISI_TOP,
                        GXHDO102B028Const.BTN_KAKUHANSYUURYOU_TOP,
                        GXHDO102B028Const.BTN_KANSOUKAISI_TOP,
                        GXHDO102B028Const.BTN_KANSOUSYUURYOU_TOP,
                        GXHDO102B028Const.BTN_KANSOUGOSYOUMIJYUURYOU_TOP,
                        GXHDO102B028Const.BTN_YOUZAITYOUSEIRYOU_TOP,
                        GXHDO102B028Const.BTN_YOUZAIKEIRYOU_TOP,
                        GXHDO102B028Const.BTN_NIKAIMEKAKUHANKAISI_TOP,
                        GXHDO102B028Const.BTN_NKMKKHSR_TOP,
                        GXHDO102B028Const.BTN_NIKAIMEKANSOUKAISI_TOP,
                        GXHDO102B028Const.BTN_NIKAIMEKANSOUSYUURYOU_TOP,
                        GXHDO102B028Const.BTN_NKMKSGSMJR_TOP,
                        GXHDO102B028Const.BTN_NKMYZTSR_TOP,
                        GXHDO102B028Const.BTN_NIKAIMEYOUZAIKEIRYOU_TOP,
                        GXHDO102B028Const.BTN_SANKAIMEKAKUHANKAISI_TOP,
                        GXHDO102B028Const.BTN_SKMKKHSR_TOP,
                        GXHDO102B028Const.BTN_SANKAIMEKANSOUKAISI_TOP,
                        GXHDO102B028Const.BTN_SANKAIMEKANSOUSYUURYOU_TOP,
                        GXHDO102B028Const.BTN_SKMKSGSMJR_TOP,
                        GXHDO102B028Const.BTN_FPKAISI_TOP,
                        GXHDO102B028Const.BTN_FPTEISI_TOP,
                        GXHDO102B028Const.BTN_FPSAIKAI_TOP,
                        GXHDO102B028Const.BTN_FPSYUURYOU_TOP,
                        GXHDO102B028Const.BTN_SYOUMIJYUURYOU_TOP,
                        GXHDO102B028Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B028Const.BTN_INSERT_TOP,
                        GXHDO102B028Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B028Const.BTN_BINDERKONGOU_BOTTOM,
                        GXHDO102B028Const.BTN_SLURRYJYUURYOU_BOTTOM,
                        GXHDO102B028Const.BTN_KAKUHANKAISI_BOTTOM,
                        GXHDO102B028Const.BTN_KAKUHANSYUURYOU_BOTTOM,
                        GXHDO102B028Const.BTN_KANSOUKAISI_BOTTOM,
                        GXHDO102B028Const.BTN_KANSOUSYUURYOU_BOTTOM,
                        GXHDO102B028Const.BTN_KANSOUGOSYOUMIJYUURYOU_BOTTOM,
                        GXHDO102B028Const.BTN_YOUZAITYOUSEIRYOU_BOTTOM,
                        GXHDO102B028Const.BTN_YOUZAIKEIRYOU_BOTTOM,
                        GXHDO102B028Const.BTN_NIKAIMEKAKUHANKAISI_BOTTOM,
                        GXHDO102B028Const.BTN_NKMKKHSR_BOTTOM,
                        GXHDO102B028Const.BTN_NIKAIMEKANSOUKAISI_BOTTOM,
                        GXHDO102B028Const.BTN_NIKAIMEKANSOUSYUURYOU_BOTTOM,
                        GXHDO102B028Const.BTN_NKMKSGSMJR_BOTTOM,
                        GXHDO102B028Const.BTN_NKMYZTSR_BOTTOM,
                        GXHDO102B028Const.BTN_NIKAIMEYOUZAIKEIRYOU_BOTTOM,
                        GXHDO102B028Const.BTN_SANKAIMEKAKUHANKAISI_BOTTOM,
                        GXHDO102B028Const.BTN_SKMKKHSR_BOTTOM,
                        GXHDO102B028Const.BTN_SANKAIMEKANSOUKAISI_BOTTOM,
                        GXHDO102B028Const.BTN_SANKAIMEKANSOUSYUURYOU_BOTTOM,
                        GXHDO102B028Const.BTN_SKMKSGSMJR_BOTTOM,
                        GXHDO102B028Const.BTN_FPKAISI_BOTTOM,
                        GXHDO102B028Const.BTN_FPTEISI_BOTTOM,
                        GXHDO102B028Const.BTN_FPSAIKAI_BOTTOM,
                        GXHDO102B028Const.BTN_FPSYUURYOU_BOTTOM,
                        GXHDO102B028Const.BTN_SYOUMIJYUURYOU_BOTTOM,
                        GXHDO102B028Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B028Const.BTN_INSERT_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B028Const.BTN_UPDATE_TOP,
                        GXHDO102B028Const.BTN_DELETE_TOP,
                        GXHDO102B028Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B028Const.BTN_DELETE_BOTTOM
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
        
        String syuuryouDate = "";
        String syuuryouTime = "";
        String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
        String lotNo9 = lotNo.substring(3, 12); //ﾛｯﾄNo
        String edaban = lotNo.substring(12, 15); //枝番
        Map shuryonichijiData = getSyuuryounichiji(queryRunnerQcdb, kojyo, lotNo9, edaban);
        
        if (shuryonichijiData != null && !shuryonichijiData.isEmpty()) {
            if (!StringUtil.isEmpty(StringUtil.nullToBlank(getMapData(shuryonichijiData, "syuuryounichiji")))) {
                // 終了日時
                syuuryouDate = DateUtil.formattedTimestamp((Timestamp) getMapData(shuryonichijiData, "syuuryounichiji"), "yyMMdd");
                syuuryouTime = DateUtil.formattedTimestamp((Timestamp) getMapData(shuryonichijiData, "syuuryounichiji"), "HHmm");
            }
        }

        // 入力項目の情報を画面にセットする。
        if (!setInputItemData(processData, queryRunnerDoc, queryRunnerQcdb, lotNo, formId, paramJissekino, syuuryouDate, syuuryouTime)) {
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
        GXHDO102C013Logic.setItemStyle(processData.getItemList());
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
        this.setItemData(processData, GXHDO102B028Const.WIPLOTNO, lotNo);
        // ｽﾘｯﾌﾟ品名
        this.setItemData(processData, GXHDO102B028Const.SLIPHINMEI, StringUtil.nullToBlank(getMapData(shikakariData, "hinmei")));
        // ｽﾘｯﾌﾟLotNo
        this.setItemData(processData, GXHDO102B028Const.SLIPLOTNO, StringUtil.nullToBlank(getMapData(shikakariData, "lotno")));
        // ﾛｯﾄ区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode"));
        // ﾛｯﾄ区分名称
        String lotkubun = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubun"));

        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO102B028Const.LOTKUBUN, "");
        } else {
            if (!StringUtil.isEmpty(lotkubun)) {
                lotkubuncode = lotkubuncode + ":" + lotkubun;
            }
            this.setItemData(processData, GXHDO102B028Const.LOTKUBUN, lotkubuncode);
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
            String lotNo, String formId, int jissekino, String syuuryouDate, String syuuryouTime) throws SQLException {

        List<SrSlipSlurrykokeibuntyouseiSutenyouki> srSlipSlurrykokeibuntyouseiSutenyoukiList = new ArrayList<>();
        List<SubSrSlipSlurrykokeibuntyouseiSutenyouki> subSrSlipSlrkkbtsSutenyoukiList = new ArrayList<>();
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

                // ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力_ｻﾌﾞ画面データ設定
                setInputItemDataSubFormC013(processData, null);
                
                // 粉砕終了日
                this.setItemData(processData, GXHDO102B028Const.FUNSAISYUURYOU_DAY, syuuryouDate);
                // 粉砕終了時間
                this.setItemData(processData, GXHDO102B028Const.FUNSAISYUURYOU_TIME, syuuryouTime);
                return true;
            }

            // ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)データ取得
            srSlipSlurrykokeibuntyouseiSutenyoukiList = getSrSlipSlurrykokeibuntyouseiSutenyoukiData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo9, edaban);
            if (srSlipSlurrykokeibuntyouseiSutenyoukiList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力_サブ画面データ取得
            subSrSlipSlrkkbtsSutenyoukiList = getSubSrSlipSlurrykokeibuntyouseiSutenyoukiData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo9, edaban);
            if (subSrSlipSlrkkbtsSutenyoukiList.isEmpty() || subSrSlipSlrkkbtsSutenyoukiList.size() != 4) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }
            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srSlipSlurrykokeibuntyouseiSutenyoukiList.isEmpty() || (subSrSlipSlrkkbtsSutenyoukiList.isEmpty() || subSrSlipSlrkkbtsSutenyoukiList.size() != 4)) {
            return false;
        }
        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srSlipSlurrykokeibuntyouseiSutenyoukiList.get(0), jotaiFlg);
        // ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力_ｻﾌﾞ画面データ設定
        setInputItemDataSubFormC013(processData, subSrSlipSlrkkbtsSutenyoukiList);
        return true;

    }

    /**
     * データ設定処理
     *
     * @param processData 処理制御データ
     * @param srSlipSlurrykokeibuntyouseiSutenyouki ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrSlipSlurrykokeibuntyouseiSutenyouki srSlipSlurrykokeibuntyouseiSutenyouki, String jotaiFlg) {

        // 秤量号機
        this.setItemData(processData, GXHDO102B028Const.GOKI, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.GOKI, srSlipSlurrykokeibuntyouseiSutenyouki));

        // ｽﾗﾘｰ有効期限
        this.setItemData(processData, GXHDO102B028Const.SLURRYYUUKOUKIGEN, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.SLURRYYUUKOUKIGEN, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 粉砕終了日
        this.setItemData(processData, GXHDO102B028Const.FUNSAISYUURYOU_DAY, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.FUNSAISYUURYOU_DAY, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 粉砕終了時間
        this.setItemData(processData, GXHDO102B028Const.FUNSAISYUURYOU_TIME, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.FUNSAISYUURYOU_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));

        // ﾊﾞｲﾝﾀﾞｰ混合日
        this.setItemData(processData, GXHDO102B028Const.BINDERKONGOU_DAY, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.BINDERKONGOU_DAY, srSlipSlurrykokeibuntyouseiSutenyouki));

        // ﾊﾞｲﾝﾀﾞｰ混合時間
        this.setItemData(processData, GXHDO102B028Const.BINDERKONGOU_TIME, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.BINDERKONGOU_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));

        // ｽﾗﾘｰ経過日数
        this.setItemData(processData, GXHDO102B028Const.SLURRYKEIKANISUU, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.SLURRYKEIKANISUU, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 総重量
        this.setItemData(processData, GXHDO102B028Const.SLURRYSOUJYUURYOU, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.SLURRYSOUJYUURYOU, srSlipSlurrykokeibuntyouseiSutenyouki));

        // ｽﾗﾘｰ重量
        this.setItemData(processData, GXHDO102B028Const.SLURRYJYUURYOU, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.SLURRYJYUURYOU, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 撹拌開始日
        this.setItemData(processData, GXHDO102B028Const.KAKUHANKAISI_DAY, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.KAKUHANKAISI_DAY, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 撹拌開始時間
        this.setItemData(processData, GXHDO102B028Const.KAKUHANKAISI_TIME, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.KAKUHANKAISI_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 撹拌終了日
        this.setItemData(processData, GXHDO102B028Const.KAKUHANSYUURYOU_DAY, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.KAKUHANSYUURYOU_DAY, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 撹拌終了時間
        this.setItemData(processData, GXHDO102B028Const.KAKUHANSYUURYOU_TIME, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.KAKUHANSYUURYOU_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 固形分測定_風袋重量
        this.setItemData(processData, GXHDO102B028Const.KKBSKT_FUUTAIJYUURYOU, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.KKBSKT_FUUTAIJYUURYOU, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 乾燥前ｽﾗﾘｰ重量
        this.setItemData(processData, GXHDO102B028Const.KANSOUMAESLURRYJYUURYOU, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.KANSOUMAESLURRYJYUURYOU, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 乾燥開始日
        this.setItemData(processData, GXHDO102B028Const.KANSOUKAISI_DAY, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.KANSOUKAISI_DAY, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 乾燥開始時間
        this.setItemData(processData, GXHDO102B028Const.KANSOUKAISI_TIME, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.KANSOUKAISI_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 乾燥終了日
        this.setItemData(processData, GXHDO102B028Const.KANSOUSYUURYOU_DAY, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.KANSOUSYUURYOU_DAY, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 乾燥終了時間
        this.setItemData(processData, GXHDO102B028Const.KANSOUSYUURYOU_TIME, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.KANSOUSYUURYOU_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 乾燥後総重量
        this.setItemData(processData, GXHDO102B028Const.KANSOUGOSOUJYUURYOU, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.KANSOUGOSOUJYUURYOU, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 乾燥後正味重量
        this.setItemData(processData, GXHDO102B028Const.KANSOUGOSYOUMIJYUURYOU, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.KANSOUGOSYOUMIJYUURYOU, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 固形分比率
        this.setItemData(processData, GXHDO102B028Const.KOKEIBUNHIRITU, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.KOKEIBUNHIRITU, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 固形分測定担当者
        this.setItemData(processData, GXHDO102B028Const.KOKEIBUNSOKUTEITANTOUSYA, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.KOKEIBUNSOKUTEITANTOUSYA, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 溶剤調整量
        this.setItemData(processData, GXHDO102B028Const.YOUZAITYOUSEIRYOU, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.YOUZAITYOUSEIRYOU, srSlipSlurrykokeibuntyouseiSutenyouki));

        // ﾄﾙｴﾝ調整量
        this.setItemData(processData, GXHDO102B028Const.TOLUENETYOUSEIRYOU, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.TOLUENETYOUSEIRYOU, srSlipSlurrykokeibuntyouseiSutenyouki));

        // ｿﾙﾐｯｸｽ調整量
        this.setItemData(processData, GXHDO102B028Const.SOLMIXTYOUSEIRYOU, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.SOLMIXTYOUSEIRYOU, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 溶剤秤量日
        this.setItemData(processData, GXHDO102B028Const.YOUZAIKEIRYOU_DAY, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.YOUZAIKEIRYOU_DAY, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 溶剤秤量時間
        this.setItemData(processData, GXHDO102B028Const.YOUZAIKEIRYOU_TIME, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.YOUZAIKEIRYOU_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 仮登録・登録済みの場合、XXX_調合量規格が初期値で表示
        if (JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg) || JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            // 溶剤1_調合量規格
            this.setItemKikuchiData(processData, GXHDO102B028Const.YOUZAI1_TYOUGOURYOUKIKAKU,
                    getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.YOUZAI1_TYOUGOURYOUKIKAKU, srSlipSlurrykokeibuntyouseiSutenyouki));

            // 溶剤2_調合量規格
            this.setItemKikuchiData(processData, GXHDO102B028Const.YOUZAI2_TYOUGOURYOUKIKAKU,
                    getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.YOUZAI2_TYOUGOURYOUKIKAKU, srSlipSlurrykokeibuntyouseiSutenyouki));

            // 2回目溶剤1_調合量規格
            this.setItemKikuchiData(processData, GXHDO102B028Const.NKMYZ1_TYOUGOURYOUKIKAKU,
                    getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.NKMYZ1_TYOUGOURYOUKIKAKU, srSlipSlurrykokeibuntyouseiSutenyouki));

            // 2回目溶剤2_調合量規格
            this.setItemKikuchiData(processData, GXHDO102B028Const.NKMYZ2_TYOUGOURYOUKIKAKU,
                    getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.NKMYZ2_TYOUGOURYOUKIKAKU, srSlipSlurrykokeibuntyouseiSutenyouki));
        }
        // 溶剤①_部材在庫No1
        this.setItemData(processData, GXHDO102B028Const.YOUZAI1_BUZAIZAIKOLOTNO1, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.YOUZAI1_BUZAIZAIKOLOTNO1, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 溶剤①_調合量1
        this.setItemData(processData, GXHDO102B028Const.YOUZAI1_TYOUGOURYOU1, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.YOUZAI1_TYOUGOURYOU1, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 溶剤①_部材在庫No2
        this.setItemData(processData, GXHDO102B028Const.YOUZAI1_BUZAIZAIKOLOTNO2, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.YOUZAI1_BUZAIZAIKOLOTNO2, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 溶剤①_調合量2
        this.setItemData(processData, GXHDO102B028Const.YOUZAI1_TYOUGOURYOU2, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.YOUZAI1_TYOUGOURYOU2, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 溶剤②_部材在庫No1
        this.setItemData(processData, GXHDO102B028Const.YOUZAI2_BUZAIZAIKOLOTNO1, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.YOUZAI2_BUZAIZAIKOLOTNO1, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 溶剤②_調合量1
        this.setItemData(processData, GXHDO102B028Const.YOUZAI2_TYOUGOURYOU1, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.YOUZAI2_TYOUGOURYOU1, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 溶剤②_部材在庫No2
        this.setItemData(processData, GXHDO102B028Const.YOUZAI2_BUZAIZAIKOLOTNO2, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.YOUZAI2_BUZAIZAIKOLOTNO2, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 溶剤②_調合量2
        this.setItemData(processData, GXHDO102B028Const.YOUZAI2_TYOUGOURYOU2, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.YOUZAI2_TYOUGOURYOU2, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 担当者
        this.setItemData(processData, GXHDO102B028Const.TANTOUSYA, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.TANTOUSYA, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 2回目撹拌開始日
        this.setItemData(processData, GXHDO102B028Const.NIKAIMEKAKUHANKAISI_DAY, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.NIKAIMEKAKUHANKAISI_DAY, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 2回目撹拌開始時間
        this.setItemData(processData, GXHDO102B028Const.NIKAIMEKAKUHANKAISI_TIME, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.NIKAIMEKAKUHANKAISI_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 2回目撹拌終了日
        this.setItemData(processData, GXHDO102B028Const.NKMKKHSR_DAY, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.NKMKKHSR_DAY, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 2回目撹拌終了時間
        this.setItemData(processData, GXHDO102B028Const.NKMKKHSR_TIME, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.NKMKKHSR_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 2回目ｱﾙﾐ皿風袋重量
        this.setItemData(processData, GXHDO102B028Const.NKMARMZFTJR, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.NKMARMZFTJR, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 2回目乾燥前ｽﾗﾘｰ重量
        this.setItemData(processData, GXHDO102B028Const.NKMKSMSLRJR, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.NKMKSMSLRJR, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 2回目乾燥開始日
        this.setItemData(processData, GXHDO102B028Const.NIKAIMEKANSOUKAISI_DAY, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.NIKAIMEKANSOUKAISI_DAY, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 2回目乾燥開始時間
        this.setItemData(processData, GXHDO102B028Const.NIKAIMEKANSOUKAISI_TIME, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.NIKAIMEKANSOUKAISI_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 2回目乾燥終了日
        this.setItemData(processData, GXHDO102B028Const.NIKAIMEKANSOUSYUURYOU_DAY, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.NIKAIMEKANSOUSYUURYOU_DAY, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 2回目乾燥終了時間
        this.setItemData(processData, GXHDO102B028Const.NIKAIMEKANSOUSYUURYOU_TIME, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.NIKAIMEKANSOUSYUURYOU_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 2回目乾燥後総重量
        this.setItemData(processData, GXHDO102B028Const.NIKAIMEKANSOUGOSOUJYUURYOU, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.NIKAIMEKANSOUGOSOUJYUURYOU, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 2回目乾燥後正味重量
        this.setItemData(processData, GXHDO102B028Const.NKMKSGSMJR, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.NKMKSGSMJR, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 2回目固形分比率
        this.setItemData(processData, GXHDO102B028Const.NIKAIMEKOKEIBUNHIRITU, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.NIKAIMEKOKEIBUNHIRITU, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 2回目固形分測定担当者
        this.setItemData(processData, GXHDO102B028Const.NKMKKBSKTTTS, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.NKMKKBSKTTTS, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 2回目溶剤調整量
        this.setItemData(processData, GXHDO102B028Const.NKMYZTSR, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.NKMYZTSR, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 2回目ﾄﾙｴﾝ調整量
        this.setItemData(processData, GXHDO102B028Const.NIKAIMETOLUENETYOUSEIRYOU, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.NIKAIMETOLUENETYOUSEIRYOU, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 2回目ｿﾙﾐｯｸｽ調整量
        this.setItemData(processData, GXHDO102B028Const.NIKAIMESOLMIXTYOUSEIRYOU, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.NIKAIMESOLMIXTYOUSEIRYOU, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 2回目溶剤秤量日
        this.setItemData(processData, GXHDO102B028Const.NKMYZKEIRYOU_DAY, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.NKMYZKEIRYOU_DAY, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 2回目溶剤秤量時間
        this.setItemData(processData, GXHDO102B028Const.NKMYZKEIRYOU_TIME, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.NKMYZKEIRYOU_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 2回目溶剤①_部材在庫No1
        this.setItemData(processData, GXHDO102B028Const.NKMYZ1_BUZAIZAIKOLOTNO1, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.NKMYZ1_BUZAIZAIKOLOTNO1, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 2回目溶剤①_調合量1
        this.setItemData(processData, GXHDO102B028Const.NKMYZ1_TYOUGOURYOU1, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.NKMYZ1_TYOUGOURYOU1, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 2回目溶剤①_部材在庫No2
        this.setItemData(processData, GXHDO102B028Const.NKMYZ1_BUZAIZAIKOLOTNO2, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.NKMYZ1_BUZAIZAIKOLOTNO2, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 2回目溶剤①_調合量2
        this.setItemData(processData, GXHDO102B028Const.NKMYZ1_TYOUGOURYOU2, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.NKMYZ1_TYOUGOURYOU2, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 2回目溶剤②_部材在庫No1
        this.setItemData(processData, GXHDO102B028Const.NKMYZ2_BUZAIZAIKOLOTNO1, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.NKMYZ2_BUZAIZAIKOLOTNO1, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 2回目溶剤②_調合量1
        this.setItemData(processData, GXHDO102B028Const.NKMYZ2_TYOUGOURYOU1, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.NKMYZ2_TYOUGOURYOU1, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 2回目溶剤②_部材在庫No2
        this.setItemData(processData, GXHDO102B028Const.NKMYZ2_BUZAIZAIKOLOTNO2, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.NKMYZ2_BUZAIZAIKOLOTNO2, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 2回目溶剤②_調合量2
        this.setItemData(processData, GXHDO102B028Const.NKMYZ2_TYOUGOURYOU2, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.NKMYZ2_TYOUGOURYOU2, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 2回目担当者
        this.setItemData(processData, GXHDO102B028Const.NIKAIMETANTOUSYA, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.NIKAIMETANTOUSYA, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 3回目撹拌開始日
        this.setItemData(processData, GXHDO102B028Const.SANKAIMEKAKUHANKAISI_DAY, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.SANKAIMEKAKUHANKAISI_DAY, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 3回目撹拌開始時間
        this.setItemData(processData, GXHDO102B028Const.SANKAIMEKAKUHANKAISI_TIME, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.SANKAIMEKAKUHANKAISI_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 3回目撹拌終了日
        this.setItemData(processData, GXHDO102B028Const.SKMKKHSR_DAY, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.SKMKKHSR_DAY, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 3回目撹拌終了時間
        this.setItemData(processData, GXHDO102B028Const.SKMKKHSR_TIME, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.SKMKKHSR_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 3回目ｱﾙﾐ皿風袋重量
        this.setItemData(processData, GXHDO102B028Const.SKMARMZFTJR, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.SKMARMZFTJR, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 3回目乾燥前ｽﾗﾘｰ重量
        this.setItemData(processData, GXHDO102B028Const.SKMKSMSLRJR, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.SKMKSMSLRJR, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 3回目乾燥開始日
        this.setItemData(processData, GXHDO102B028Const.SANKAIMEKANSOUKAISI_DAY, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.SANKAIMEKANSOUKAISI_DAY, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 3回目乾燥開始時間
        this.setItemData(processData, GXHDO102B028Const.SANKAIMEKANSOUKAISI_TIME, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.SANKAIMEKANSOUKAISI_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 3回目乾燥終了日
        this.setItemData(processData, GXHDO102B028Const.SANKAIMEKANSOUSYUURYOU_DAY, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.SANKAIMEKANSOUSYUURYOU_DAY, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 3回目乾燥終了時間
        this.setItemData(processData, GXHDO102B028Const.SANKAIMEKANSOUSYUURYOU_TIME, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.SANKAIMEKANSOUSYUURYOU_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 3回目乾燥後総重量
        this.setItemData(processData, GXHDO102B028Const.SANKAIMEKANSOUGOSOUJYUURYOU, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.SANKAIMEKANSOUGOSOUJYUURYOU, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 3回目乾燥後正味重量
        this.setItemData(processData, GXHDO102B028Const.SKMKSGSMJR, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.SKMKSGSMJR, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 3回目固形分比率
        this.setItemData(processData, GXHDO102B028Const.SANKAIMEKOKEIBUNHIRITU, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.SANKAIMEKOKEIBUNHIRITU, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 3回目固形分測定担当者
        this.setItemData(processData, GXHDO102B028Const.SKMKKBSKTTTS, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.SKMKKBSKTTTS, srSlipSlurrykokeibuntyouseiSutenyouki));

        // F/P準備_風袋重量
        this.setItemData(processData, GXHDO102B028Const.FPJYUNBI_FUUTAIJYUURYOU, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.FPJYUNBI_FUUTAIJYUURYOU, srSlipSlurrykokeibuntyouseiSutenyouki));

        // F/P準備_担当者
        this.setItemData(processData, GXHDO102B028Const.FPJYUNBI_TANTOUSYA, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.FPJYUNBI_TANTOUSYA, srSlipSlurrykokeibuntyouseiSutenyouki));

        // F/P準備_LotNo①
        this.setItemData(processData, GXHDO102B028Const.FPJYUNBI_LOTNO1, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.FPJYUNBI_LOTNO1, srSlipSlurrykokeibuntyouseiSutenyouki));

        // F/P準備_取り付け本数①
        this.setItemData(processData, GXHDO102B028Const.FPJYUNBI_TORITUKEHONSUU1, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.FPJYUNBI_TORITUKEHONSUU1, srSlipSlurrykokeibuntyouseiSutenyouki));

        // F/P準備_LotNo②
        this.setItemData(processData, GXHDO102B028Const.FPJYUNBI_LOTNO2, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.FPJYUNBI_LOTNO2, srSlipSlurrykokeibuntyouseiSutenyouki));

        // F/P準備_取り付け本数②
        this.setItemData(processData, GXHDO102B028Const.FPJYUNBI_TORITUKEHONSUU2, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.FPJYUNBI_TORITUKEHONSUU2, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 圧送ﾀﾝｸの洗浄
        this.setItemData(processData, GXHDO102B028Const.ASSOUTANKNOSENJYOU, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.ASSOUTANKNOSENJYOU, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 排出容器の内袋
        this.setItemData(processData, GXHDO102B028Const.HAISYUTUYOUKINOUTIBUKURO, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.HAISYUTUYOUKINOUTIBUKURO, srSlipSlurrykokeibuntyouseiSutenyouki));

        // F/PﾀﾝｸNo
        this.setItemData(processData, GXHDO102B028Const.FPTANKNO, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.FPTANKNO, srSlipSlurrykokeibuntyouseiSutenyouki));

        // F/P開始日
        this.setItemData(processData, GXHDO102B028Const.FPKAISI_DAY, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.FPKAISI_DAY, srSlipSlurrykokeibuntyouseiSutenyouki));

        // F/P開始時間
        this.setItemData(processData, GXHDO102B028Const.FPKAISI_TIME, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.FPKAISI_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 圧送ﾚｷﾞｭﾚｰﾀｰNo
        this.setItemData(processData, GXHDO102B028Const.ASSOUREGULATORNO, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.ASSOUREGULATORNO, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 圧送圧力
        this.setItemData(processData, GXHDO102B028Const.ASSOUATURYOKU, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.ASSOUATURYOKU, srSlipSlurrykokeibuntyouseiSutenyouki));

        // F/P開始_担当者
        this.setItemData(processData, GXHDO102B028Const.FPKAISI_TANTOUSYA, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.FPKAISI_TANTOUSYA, srSlipSlurrykokeibuntyouseiSutenyouki));

        // F/P停止日
        this.setItemData(processData, GXHDO102B028Const.FPTEISI_DAY, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.FPTEISI_DAY, srSlipSlurrykokeibuntyouseiSutenyouki));

        // F/P停止時間
        this.setItemData(processData, GXHDO102B028Const.FPTEISI_TIME, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.FPTEISI_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));

        // F/P交換_LotNo①
        this.setItemData(processData, GXHDO102B028Const.FPKOUKAN_LOTNO1, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.FPKOUKAN_LOTNO1, srSlipSlurrykokeibuntyouseiSutenyouki));

        // F/P交換_取り付け本数①
        this.setItemData(processData, GXHDO102B028Const.FPKOUKAN_TORITUKEHONSUU1, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.FPKOUKAN_TORITUKEHONSUU1, srSlipSlurrykokeibuntyouseiSutenyouki));

        // F/P交換_LotNo②
        this.setItemData(processData, GXHDO102B028Const.FPKOUKAN_LOTNO2, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.FPKOUKAN_LOTNO2, srSlipSlurrykokeibuntyouseiSutenyouki));

        // F/P交換_取り付け本数②
        this.setItemData(processData, GXHDO102B028Const.FPKOUKAN_TORITUKEHONSUU2, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.FPKOUKAN_TORITUKEHONSUU2, srSlipSlurrykokeibuntyouseiSutenyouki));

        // F/P再開日
        this.setItemData(processData, GXHDO102B028Const.FPSAIKAI_DAY, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.FPSAIKAI_DAY, srSlipSlurrykokeibuntyouseiSutenyouki));

        // F/P再開時間
        this.setItemData(processData, GXHDO102B028Const.FPSAIKAI_TIME, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.FPSAIKAI_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));

        // F/P交換_担当者
        this.setItemData(processData, GXHDO102B028Const.FPKOUKAN_TANTOUSYA, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.FPKOUKAN_TANTOUSYA, srSlipSlurrykokeibuntyouseiSutenyouki));

        // F/P終了日
        this.setItemData(processData, GXHDO102B028Const.FPSYUURYOU_DAY, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.FPSYUURYOU_DAY, srSlipSlurrykokeibuntyouseiSutenyouki));

        // F/P終了時間
        this.setItemData(processData, GXHDO102B028Const.FPSYUURYOU_TIME, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.FPSYUURYOU_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));

        // F/P時間
        this.setItemData(processData, GXHDO102B028Const.FPJIKAN, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.FPJIKAN, srSlipSlurrykokeibuntyouseiSutenyouki));

        // F/P終了_担当者
        this.setItemData(processData, GXHDO102B028Const.FPSYUUROU_TANTOUSYA, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.FPSYUUROU_TANTOUSYA, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 総重量
        this.setItemData(processData, GXHDO102B028Const.SOUJYUURYOU, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.SOUJYUURYOU, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 正味重量
        this.setItemData(processData, GXHDO102B028Const.SYOUMIJYUURYOU, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.SYOUMIJYUURYOU, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 備考1
        this.setItemData(processData, GXHDO102B028Const.BIKOU1, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.BIKOU1, srSlipSlurrykokeibuntyouseiSutenyouki));

        // 備考2
        this.setItemData(processData, GXHDO102B028Const.BIKOU2, getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(GXHDO102B028Const.BIKOU2, srSlipSlurrykokeibuntyouseiSutenyouki));

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
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)データ
     * @throws SQLException 例外エラー
     */
    private List<SrSlipSlurrykokeibuntyouseiSutenyouki> getSrSlipSlurrykokeibuntyouseiSutenyoukiData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrSlipSlurrykokeibuntyouseiSutenyouki(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSrSlipSlurrykokeibuntyouseiSutenyouki(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
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
     * [ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrSlipSlurrykokeibuntyouseiSutenyouki> loadSrSlipSlurrykokeibuntyouseiSutenyouki(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,goki,slurryhinmei,slurrylotno,youkino,slurryyuukoukigen,kansoukokeibun,dassikokeibun,"
                + "funsaisyuuryounichiji,binderkongounichij,slurraging,slurrykeikanisuu,slipjyouhou_fuutaijyuuryou,slurrysoujyuuryou,slurryjyuuryou,kakuhansetubi,kakuhangoki,"
                + "kakuhankaitensuu,kakuhanjikan,kakuhankaisinichiji,kakuhansyuuryounichiji,dassizaranosyurui,kokeibunsokutei_fuutaijyuuryou,kansoumaeslurryjyuuryou,kansouki,"
                + "kansouondo,kansoujikan,kansoukaisinichij,kansousyuuryounichiji,kansougosoujyuuryou,kansougosyoumijyuuryou,kokeibunhiritu,kokeibunsokuteitantousya,youzaityouseiryou,"
                + "toluenetyouseiryou,solmixtyouseiryou,youzaikeiryounichiji,youzai1_zairyouhinmei,youzai1_tyougouryoukikaku,youzai1_buzaizaikolotno1,youzai1_tyougouryou1,youzai1_buzaizaikolotno2,"
                + "youzai1_tyougouryou2,youzai2_zairyouhinmei,youzai2_tyougouryoukikaku,youzai2_buzaizaikolotno1,youzai2_tyougouryou1,youzai2_buzaizaikolotno2,youzai2_tyougouryou2,"
                + "tantousya,2kaimekakuhansetubi AS nikaimekakuhansetubi,2kaimekakuhanjikan AS nikaimekakuhanjikan,2kaimekakuhankaisinichiji AS nikaimekakuhankaisinichiji,"
                + "2kaimekakuhansyuuryounichiji AS nikaimekakuhansyuuryounichiji,2kaimedassizaranosyurui AS nikaimedassizaranosyurui,2kaimearumizarafuutaijyuuryou AS nikaimearumizarafuutaijyuuryou,"
                + "2kaimekansoumaeslurryjyuuryou AS nikaimekansoumaeslurryjyuuryou,2kaimekansouki AS nikaimekansouki,2kaimekansouondo AS nikaimekansouondo,2kaimekansoujikan AS nikaimekansoujikan,"
                + "2kaimekansoukaisinichij AS nikaimekansoukaisinichij,2kaimekansousyuuryounichiji AS nikaimekansousyuuryounichiji,2kaimekansougosoujyuuryou AS nikaimekansougosoujyuuryou,"
                + "2kaimekansougosyoumijyuuryou AS nikaimekansougosyoumijyuuryou,2kaimekokeibunhiritu AS nikaimekokeibunhiritu,2kaimekokeibunsokuteitantousya AS nikaimekokeibunsokuteitantousya,"
                + "2kaimeyouzaityouseiryou AS nikaimeyouzaityouseiryou,2kaimetoluenetyouseiryou AS nikaimetoluenetyouseiryou,2kaimesolmixtyouseiryou AS nikaimesolmixtyouseiryou,"
                + "2kaimeyouzaikeiryounichiji AS nikaimeyouzaikeiryounichiji,2kaimeyouzai1_zairyouhinmei AS nikaimeyouzai1_zairyouhinmei,2kaimeyouzai1_tyougouryoukikaku AS nikaimeyouzai1_tyougouryoukikaku,"
                + "2kaimeyouzai1_buzaizaikolotno1 AS nikaimeyouzai1_buzaizaikolotno1,2kaimeyouzai1_tyougouryou1 AS nikaimeyouzai1_tyougouryou1,"
                + "2kaimeyouzai1_buzaizaikolotno2 AS nikaimeyouzai1_buzaizaikolotno2,2kaimeyouzai1_tyougouryou2 AS nikaimeyouzai1_tyougouryou2,2kaimeyouzai2_zairyouhinmei AS nikaimeyouzai2_zairyouhinmei,"
                + "2kaimeyouzai2_tyougouryoukikaku AS nikaimeyouzai2_tyougouryoukikaku,2kaimeyouzai2_buzaizaikolotno1 AS nikaimeyouzai2_buzaizaikolotno1,"
                + "2kaimeyouzai2_tyougouryou1 AS nikaimeyouzai2_tyougouryou1,2kaimeyouzai2_buzaizaikolotno2 AS nikaimeyouzai2_buzaizaikolotno2,"
                + "2kaimeyouzai2_tyougouryou2 AS nikaimeyouzai2_tyougouryou2,2kaimetantousya AS nikaimetantousya,3kaimekakuhansetubi AS sankaimekakuhansetubi,3kaimekakuhanjikan AS sankaimekakuhanjikan,"
                + "3kaimekakuhankaisinichiji AS sankaimekakuhankaisinichiji,3kaimekakuhansyuuryounichiji AS sankaimekakuhansyuuryounichiji,3kaimedassizaranosyurui AS sankaimedassizaranosyurui,"
                + "3kaimearumizarafuutaijyuuryou AS sankaimearumizarafuutaijyuuryou,3kaimekansoumaeslurryjyuuryou AS sankaimekansoumaeslurryjyuuryou,3kaimekansouki AS sankaimekansouki,"
                + "3kaimekansouondo AS sankaimekansouondo,3kaimekansoujikan AS sankaimekansoujikan,3kaimekansoukaisinichij AS sankaimekansoukaisinichij,"
                + "3kaimekansousyuuryounichiji AS sankaimekansousyuuryounichiji,3kaimekansougosoujyuuryou AS sankaimekansougosoujyuuryou,3kaimekansougosyoumijyuuryou AS sankaimekansougosyoumijyuuryou,"
                + "3kaimekokeibunhiritu AS sankaimekokeibunhiritu,3kaimekokeibunsokuteitantousya AS sankaimekokeibunsokuteitantousya,FPjyunbi_fuutaijyuuryou AS fpjyunbi_fuutaijyuuryou,"
                + "FPjyunbi_tantousya AS fpjyunbi_tantousya,filterrenketu,FPjyunbi_filterhinmei1 AS fpjyunbi_filterhinmei1,FPjyunbi_lotno1 AS fpjyunbi_lotno1,"
                + "FPjyunbi_toritukehonsuu1 AS fpjyunbi_toritukehonsuu1,FPjyunbi_filterhinmei2 AS fpjyunbi_filterhinmei2,FPjyunbi_lotno2 AS fpjyunbi_lotno2,"
                + "FPjyunbi_toritukehonsuu2 AS fpjyunbi_toritukehonsuu2,assoutanknosenjyou,haisyutuyoukinoutibukuro,Fptankno AS fptankno,Fpkaisinichiji AS fpkaisinichiji,"
                + "assouregulatorNo AS assouregulatorno,assouaturyoku,FPkaisi_tantousya AS fpkaisi_tantousya,Fpteisinichiji AS fpteisinichiji,FPkoukan_filterhinmei1 AS fpkoukan_filterhinmei1,"
                + "FPkoukan_lotno1 AS fpkoukan_lotno1,FPkoukan_toritukehonsuu1 AS fpkoukan_toritukehonsuu1,FPkoukan_filterhinmei2 AS fpkoukan_filterhinmei2,FPkoukan_lotno2 AS fpkoukan_lotno2,"
                + "FPkoukan_toritukehonsuu2 AS fpkoukan_toritukehonsuu2,FPsaikainichiji AS fpsaikainichiji,FPkoukan_tantousya AS fpkoukan_tantousya,FPsyuuryounichiji AS fpsyuuryounichiji,"
                + "FPjikan AS fpjikan,FPsyuurou_tantousya AS fpsyuurou_tantousya,soujyuuryou,syoumijyuuryou,bikou1,bikou2,torokunichiji,kosinnichiji,revision"
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

        Map<String, String> mapping = new HashMap<>();
        mapping.put("kojyo", "kojyo");                                                              // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                                              // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                                            // 枝番
        mapping.put("sliphinmei", "sliphinmei");                                                    // ｽﾘｯﾌﾟ品名
        mapping.put("sliplotno", "sliplotno");                                                      // ｽﾘｯﾌﾟLotNo
        mapping.put("lotkubun", "lotkubun");                                                        // ﾛｯﾄ区分
        mapping.put("genryoukigou", "genryoukigou");                                                // 原料記号
        mapping.put("goki", "goki");                                                                // 秤量号機
        mapping.put("slurryhinmei", "slurryhinmei");                                                // ｽﾗﾘｰ品名
        mapping.put("slurrylotno", "slurrylotno");                                                  // ｽﾗﾘｰLotNo
        mapping.put("youkino", "youkino");                                                          // 容器No
        mapping.put("slurryyuukoukigen", "slurryyuukoukigen");                                      // ｽﾗﾘｰ有効期限
        mapping.put("kansoukokeibun", "kansoukokeibun");                                            // 乾燥固形分
        mapping.put("dassikokeibun", "dassikokeibun");                                              // 脱脂固形分
        mapping.put("funsaisyuuryounichiji", "funsaisyuuryounichiji");                              // 粉砕終了日時
        mapping.put("binderkongounichij", "binderkongounichij");                                    // ﾊﾞｲﾝﾀﾞｰ混合日時
        mapping.put("slurraging", "slurraging");                                                    // ｽﾗﾘｰｴｰｼﾞﾝｸﾞ
        mapping.put("slurrykeikanisuu", "slurrykeikanisuu");                                        // ｽﾗﾘｰ経過日数
        mapping.put("slipjyouhou_fuutaijyuuryou", "slipjyouhou_fuutaijyuuryou");                    // ｽﾘｯﾌﾟ情報_風袋重量
        mapping.put("slurrysoujyuuryou", "slurrysoujyuuryou");                                                  // 総重量
        mapping.put("slurryjyuuryou", "slurryjyuuryou");                                            // ｽﾗﾘｰ重量
        mapping.put("kakuhansetubi", "kakuhansetubi");                                              // 撹拌設備
        mapping.put("kakuhangoki", "kakuhangoki");                                                  // 撹拌号機
        mapping.put("kakuhankaitensuu", "kakuhankaitensuu");                                        // 撹拌回転数
        mapping.put("kakuhanjikan", "kakuhanjikan");                                                // 撹拌時間
        mapping.put("kakuhankaisinichiji", "kakuhankaisinichiji");                                  // 撹拌開始日時
        mapping.put("kakuhansyuuryounichiji", "kakuhansyuuryounichiji");                            // 撹拌終了日時
        mapping.put("dassizaranosyurui", "dassizaranosyurui");                                      // 脱脂皿の種類
        mapping.put("kokeibunsokutei_fuutaijyuuryou", "kokeibunsokutei_fuutaijyuuryou");            // 固形分測定_風袋重量
        mapping.put("kansoumaeslurryjyuuryou", "kansoumaeslurryjyuuryou");                          // 乾燥前ｽﾗﾘｰ重量
        mapping.put("kansouki", "kansouki");                                                        // 乾燥機
        mapping.put("kansouondo", "kansouondo");                                                    // 乾燥温度
        mapping.put("kansoujikan", "kansoujikan");                                                  // 乾燥時間
        mapping.put("kansoukaisinichij", "kansoukaisinichij");                                      // 乾燥開始日時
        mapping.put("kansousyuuryounichiji", "kansousyuuryounichiji");                              // 乾燥終了日時
        mapping.put("kansougosoujyuuryou", "kansougosoujyuuryou");                                  // 乾燥後総重量
        mapping.put("kansougosyoumijyuuryou", "kansougosyoumijyuuryou");                            // 乾燥後正味重量
        mapping.put("kokeibunhiritu", "kokeibunhiritu");                                            // 固形分比率
        mapping.put("kokeibunsokuteitantousya", "kokeibunsokuteitantousya");                        // 固形分測定担当者
        mapping.put("youzaityouseiryou", "youzaityouseiryou");                                      // 溶剤調整量
        mapping.put("toluenetyouseiryou", "toluenetyouseiryou");                                    // ﾄﾙｴﾝ調整量
        mapping.put("solmixtyouseiryou", "solmixtyouseiryou");                                      // ｿﾙﾐｯｸｽ調整量
        mapping.put("youzaikeiryounichiji", "youzaikeiryounichiji");                                // 溶剤秤量日時
        mapping.put("youzai1_zairyouhinmei", "youzai1_zairyouhinmei");                              // 溶剤①_材料品名
        mapping.put("youzai1_tyougouryoukikaku", "youzai1_tyougouryoukikaku");                      // 溶剤①_調合量規格
        mapping.put("youzai1_buzaizaikolotno1", "youzai1_buzaizaikolotno1");                        // 溶剤①_部材在庫No1
        mapping.put("youzai1_tyougouryou1", "youzai1_tyougouryou1");                                // 溶剤①_調合量1
        mapping.put("youzai1_buzaizaikolotno2", "youzai1_buzaizaikolotno2");                        // 溶剤①_部材在庫No2
        mapping.put("youzai1_tyougouryou2", "youzai1_tyougouryou2");                                // 溶剤①_調合量2
        mapping.put("youzai2_zairyouhinmei", "youzai2_zairyouhinmei");                              // 溶剤②_材料品名
        mapping.put("youzai2_tyougouryoukikaku", "youzai2_tyougouryoukikaku");                      // 溶剤②_調合量規格
        mapping.put("youzai2_buzaizaikolotno1", "youzai2_buzaizaikolotno1");                        // 溶剤②_部材在庫No1
        mapping.put("youzai2_tyougouryou1", "youzai2_tyougouryou1");                                // 溶剤②_調合量1
        mapping.put("youzai2_buzaizaikolotno2", "youzai2_buzaizaikolotno2");                        // 溶剤②_部材在庫No2
        mapping.put("youzai2_tyougouryou2", "youzai2_tyougouryou2");                                // 溶剤②_調合量2
        mapping.put("tantousya", "tantousya");                                                      // 担当者
        mapping.put("nikaimekakuhansetubi", "nikaimekakuhansetubi");                                // 2回目撹拌設備
        mapping.put("nikaimekakuhanjikan", "nikaimekakuhanjikan");                                  // 2回目撹拌時間
        mapping.put("nikaimekakuhankaisinichiji", "nikaimekakuhankaisinichiji");                    // 2回目撹拌開始日時
        mapping.put("nikaimekakuhansyuuryounichiji", "nikaimekakuhansyuuryounichiji");              // 2回目撹拌終了日時
        mapping.put("nikaimedassizaranosyurui", "nikaimedassizaranosyurui");                        // 2回目脱脂皿の種類
        mapping.put("nikaimearumizarafuutaijyuuryou", "nikaimearumizarafuutaijyuuryou");            // 2回目ｱﾙﾐ皿風袋重量
        mapping.put("nikaimekansoumaeslurryjyuuryou", "nikaimekansoumaeslurryjyuuryou");            // 2回目乾燥前ｽﾗﾘｰ重量
        mapping.put("nikaimekansouki", "nikaimekansouki");                                          // 2回目乾燥機
        mapping.put("nikaimekansouondo", "nikaimekansouondo");                                      // 2回目乾燥温度
        mapping.put("nikaimekansoujikan", "nikaimekansoujikan");                                    // 2回目乾燥時間
        mapping.put("nikaimekansoukaisinichij", "nikaimekansoukaisinichij");                        // 2回目乾燥開始日時
        mapping.put("nikaimekansousyuuryounichiji", "nikaimekansousyuuryounichiji");                // 2回目乾燥終了日時
        mapping.put("nikaimekansougosoujyuuryou", "nikaimekansougosoujyuuryou");                    // 2回目乾燥後総重量
        mapping.put("nikaimekansougosyoumijyuuryou", "nikaimekansougosyoumijyuuryou");              // 2回目乾燥後正味重量
        mapping.put("nikaimekokeibunhiritu", "nikaimekokeibunhiritu");                              // 2回目固形分比率
        mapping.put("nikaimekokeibunsokuteitantousya", "nikaimekokeibunsokuteitantousya");          // 2回目固形分測定担当者
        mapping.put("nikaimeyouzaityouseiryou", "nikaimeyouzaityouseiryou");                        // 2回目溶剤調整量
        mapping.put("nikaimetoluenetyouseiryou", "nikaimetoluenetyouseiryou");                      // 2回目ﾄﾙｴﾝ調整量
        mapping.put("nikaimesolmixtyouseiryou", "nikaimesolmixtyouseiryou");                        // 2回目ｿﾙﾐｯｸｽ調整量
        mapping.put("nikaimeyouzaikeiryounichiji", "nikaimeyouzaikeiryounichiji");                  // 2回目溶剤秤量日時
        mapping.put("nikaimeyouzai1_zairyouhinmei", "nikaimeyouzai1_zairyouhinmei");                // 2回目溶剤①_材料品名
        mapping.put("nikaimeyouzai1_tyougouryoukikaku", "nikaimeyouzai1_tyougouryoukikaku");        // 2回目溶剤①_調合量規格
        mapping.put("nikaimeyouzai1_buzaizaikolotno1", "nikaimeyouzai1_buzaizaikolotno1");          // 2回目溶剤①_部材在庫No1
        mapping.put("nikaimeyouzai1_tyougouryou1", "nikaimeyouzai1_tyougouryou1");                  // 2回目溶剤①_調合量1
        mapping.put("nikaimeyouzai1_buzaizaikolotno2", "nikaimeyouzai1_buzaizaikolotno2");          // 2回目溶剤①_部材在庫No2
        mapping.put("nikaimeyouzai1_tyougouryou2", "nikaimeyouzai1_tyougouryou2");                  // 2回目溶剤①_調合量2
        mapping.put("nikaimeyouzai2_zairyouhinmei", "nikaimeyouzai2_zairyouhinmei");                // 2回目溶剤②_材料品名
        mapping.put("nikaimeyouzai2_tyougouryoukikaku", "nikaimeyouzai2_tyougouryoukikaku");        // 2回目溶剤②_調合量規格
        mapping.put("nikaimeyouzai2_buzaizaikolotno1", "nikaimeyouzai2_buzaizaikolotno1");          // 2回目溶剤②_部材在庫No1
        mapping.put("nikaimeyouzai2_tyougouryou1", "nikaimeyouzai2_tyougouryou1");                  // 2回目溶剤②_調合量1
        mapping.put("nikaimeyouzai2_buzaizaikolotno2", "nikaimeyouzai2_buzaizaikolotno2");          // 2回目溶剤②_部材在庫No2
        mapping.put("nikaimeyouzai2_tyougouryou2", "nikaimeyouzai2_tyougouryou2");                  // 2回目溶剤②_調合量2
        mapping.put("nikaimetantousya", "nikaimetantousya");                                        // 2回目担当者
        mapping.put("sankaimekakuhansetubi", "sankaimekakuhansetubi");                              // 3回目撹拌設備
        mapping.put("sankaimekakuhanjikan", "sankaimekakuhanjikan");                                // 3回目撹拌時間
        mapping.put("sankaimekakuhankaisinichiji", "sankaimekakuhankaisinichiji");                  // 3回目撹拌開始日時
        mapping.put("sankaimekakuhansyuuryounichiji", "sankaimekakuhansyuuryounichiji");            // 3回目撹拌終了日時
        mapping.put("sankaimedassizaranosyurui", "sankaimedassizaranosyurui");                      // 3回目脱脂皿の種類
        mapping.put("sankaimearumizarafuutaijyuuryou", "sankaimearumizarafuutaijyuuryou");          // 3回目ｱﾙﾐ皿風袋重量
        mapping.put("sankaimekansoumaeslurryjyuuryou", "sankaimekansoumaeslurryjyuuryou");          // 3回目乾燥前ｽﾗﾘｰ重量
        mapping.put("sankaimekansouki", "sankaimekansouki");                                        // 3回目乾燥機
        mapping.put("sankaimekansouondo", "sankaimekansouondo");                                    // 3回目乾燥温度
        mapping.put("sankaimekansoujikan", "sankaimekansoujikan");                                  // 3回目乾燥時間
        mapping.put("sankaimekansoukaisinichij", "sankaimekansoukaisinichij");                      // 3回目乾燥開始日時
        mapping.put("sankaimekansousyuuryounichiji", "sankaimekansousyuuryounichiji");              // 3回目乾燥終了日時
        mapping.put("sankaimekansougosoujyuuryou", "sankaimekansougosoujyuuryou");                  // 3回目乾燥後総重量
        mapping.put("sankaimekansougosyoumijyuuryou", "sankaimekansougosyoumijyuuryou");            // 3回目乾燥後正味重量
        mapping.put("sankaimekokeibunhiritu", "sankaimekokeibunhiritu");                            // 3回目固形分比率
        mapping.put("sankaimekokeibunsokuteitantousya", "sankaimekokeibunsokuteitantousya");        // 3回目固形分測定担当者
        mapping.put("fpjyunbi_fuutaijyuuryou", "fpjyunbi_fuutaijyuuryou");                          // F/P準備_風袋重量
        mapping.put("fpjyunbi_tantousya", "fpjyunbi_tantousya");                                    // F/P準備_担当者
        mapping.put("filterrenketu", "filterrenketu");                                              // ﾌｨﾙﾀｰ連結
        mapping.put("fpjyunbi_filterhinmei1", "fpjyunbi_filterhinmei1");                            // F/P準備_ﾌｨﾙﾀｰ品名①
        mapping.put("fpjyunbi_lotno1", "fpjyunbi_lotno1");                                          // F/P準備_LotNo①
        mapping.put("fpjyunbi_toritukehonsuu1", "fpjyunbi_toritukehonsuu1");                        // F/P準備_取り付け本数①
        mapping.put("fpjyunbi_filterhinmei2", "fpjyunbi_filterhinmei2");                            // F/P準備_ﾌｨﾙﾀｰ品名②
        mapping.put("fpjyunbi_lotno2", "fpjyunbi_lotno2");                                          // F/P準備_LotNo②
        mapping.put("fpjyunbi_toritukehonsuu2", "fpjyunbi_toritukehonsuu2");                        // F/P準備_取り付け本数②
        mapping.put("assoutanknosenjyou", "assoutanknosenjyou");                                    // 圧送ﾀﾝｸの洗浄
        mapping.put("haisyutuyoukinoutibukuro", "haisyutuyoukinoutibukuro");                        // 排出容器の内袋
        mapping.put("fptankno", "fptankno");                                                        // F/PﾀﾝｸNo
        mapping.put("fpkaisinichiji", "fpkaisinichiji");                                            // F/P開始日時
        mapping.put("assouregulatorno", "assouregulatorno");                                        // 圧送ﾚｷﾞｭﾚｰﾀｰNo
        mapping.put("assouaturyoku", "assouaturyoku");                                              // 圧送圧力
        mapping.put("fpkaisi_tantousya", "fpkaisi_tantousya");                                      // F/P開始_担当者
        mapping.put("fpteisinichiji", "fpteisinichiji");                                            // F/P停止日時
        mapping.put("fpkoukan_filterhinmei1", "fpkoukan_filterhinmei1");                            // F/P交換_ﾌｨﾙﾀｰ品名①
        mapping.put("fpkoukan_lotno1", "fpkoukan_lotno1");                                          // F/P交換_LotNo①
        mapping.put("fpkoukan_toritukehonsuu1", "fpkoukan_toritukehonsuu1");                        // F/P交換_取り付け本数①
        mapping.put("fpkoukan_filterhinmei2", "fpkoukan_filterhinmei2");                            // F/P交換_ﾌｨﾙﾀｰ品名②
        mapping.put("fpkoukan_lotno2", "fpkoukan_lotno2");                                          // F/P交換_LotNo②
        mapping.put("fpkoukan_toritukehonsuu2", "fpkoukan_toritukehonsuu2");                        // F/P交換_取り付け本数②
        mapping.put("fpsaikainichiji", "fpsaikainichiji");                                          // F/P再開日時
        mapping.put("fpkoukan_tantousya", "fpkoukan_tantousya");                                    // F/P交換_担当者
        mapping.put("fpsyuuryounichiji", "fpsyuuryounichiji");                                      // F/P終了日時
        mapping.put("fpjikan", "fpjikan");                                                          // F/P時間
        mapping.put("fpsyuurou_tantousya", "fpsyuurou_tantousya");                                  // F/P終了_担当者
        mapping.put("soujyuuryou", "soujyuuryou");                                                  // 総重量
        mapping.put("syoumijyuuryou", "syoumijyuuryou");                                            // 正味重量
        mapping.put("bikou1", "bikou1");                                                            // 備考1
        mapping.put("bikou2", "bikou2");                                                            // 備考2
        mapping.put("torokunichiji", "torokunichiji");                                              // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                                                // 更新日時
        mapping.put("revision", "revision");                                                        // revision

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrSlipSlurrykokeibuntyouseiSutenyouki>> beanHandler = new BeanListHandler<>(SrSlipSlurrykokeibuntyouseiSutenyouki.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrSlipSlurrykokeibuntyouseiSutenyouki> loadTmpSrSlipSlurrykokeibuntyouseiSutenyouki(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,goki,slurryhinmei,slurrylotno,youkino,slurryyuukoukigen,kansoukokeibun,dassikokeibun,"
                + "funsaisyuuryounichiji,binderkongounichij,slurraging,slurrykeikanisuu,slipjyouhou_fuutaijyuuryou,slurrysoujyuuryou,slurryjyuuryou,kakuhansetubi,kakuhangoki,"
                + "kakuhankaitensuu,kakuhanjikan,kakuhankaisinichiji,kakuhansyuuryounichiji,dassizaranosyurui,kokeibunsokutei_fuutaijyuuryou,kansoumaeslurryjyuuryou,kansouki,"
                + "kansouondo,kansoujikan,kansoukaisinichij,kansousyuuryounichiji,kansougosoujyuuryou,kansougosyoumijyuuryou,kokeibunhiritu,kokeibunsokuteitantousya,youzaityouseiryou,"
                + "toluenetyouseiryou,solmixtyouseiryou,youzaikeiryounichiji,youzai1_zairyouhinmei,youzai1_tyougouryoukikaku,youzai1_buzaizaikolotno1,youzai1_tyougouryou1,youzai1_buzaizaikolotno2,"
                + "youzai1_tyougouryou2,youzai2_zairyouhinmei,youzai2_tyougouryoukikaku,youzai2_buzaizaikolotno1,youzai2_tyougouryou1,youzai2_buzaizaikolotno2,youzai2_tyougouryou2,"
                + "tantousya,2kaimekakuhansetubi AS nikaimekakuhansetubi,2kaimekakuhanjikan AS nikaimekakuhanjikan,2kaimekakuhankaisinichiji AS nikaimekakuhankaisinichiji,"
                + "2kaimekakuhansyuuryounichiji AS nikaimekakuhansyuuryounichiji,2kaimedassizaranosyurui AS nikaimedassizaranosyurui,2kaimearumizarafuutaijyuuryou AS nikaimearumizarafuutaijyuuryou,"
                + "2kaimekansoumaeslurryjyuuryou AS nikaimekansoumaeslurryjyuuryou,2kaimekansouki AS nikaimekansouki,2kaimekansouondo AS nikaimekansouondo,2kaimekansoujikan AS nikaimekansoujikan,"
                + "2kaimekansoukaisinichij AS nikaimekansoukaisinichij,2kaimekansousyuuryounichiji AS nikaimekansousyuuryounichiji,2kaimekansougosoujyuuryou AS nikaimekansougosoujyuuryou,"
                + "2kaimekansougosyoumijyuuryou AS nikaimekansougosyoumijyuuryou,2kaimekokeibunhiritu AS nikaimekokeibunhiritu,2kaimekokeibunsokuteitantousya AS nikaimekokeibunsokuteitantousya,"
                + "2kaimeyouzaityouseiryou AS nikaimeyouzaityouseiryou,2kaimetoluenetyouseiryou AS nikaimetoluenetyouseiryou,2kaimesolmixtyouseiryou AS nikaimesolmixtyouseiryou,"
                + "2kaimeyouzaikeiryounichiji AS nikaimeyouzaikeiryounichiji,2kaimeyouzai1_zairyouhinmei AS nikaimeyouzai1_zairyouhinmei,2kaimeyouzai1_tyougouryoukikaku AS nikaimeyouzai1_tyougouryoukikaku,"
                + "2kaimeyouzai1_buzaizaikolotno1 AS nikaimeyouzai1_buzaizaikolotno1,2kaimeyouzai1_tyougouryou1 AS nikaimeyouzai1_tyougouryou1,"
                + "2kaimeyouzai1_buzaizaikolotno2 AS nikaimeyouzai1_buzaizaikolotno2,2kaimeyouzai1_tyougouryou2 AS nikaimeyouzai1_tyougouryou2,2kaimeyouzai2_zairyouhinmei AS nikaimeyouzai2_zairyouhinmei,"
                + "2kaimeyouzai2_tyougouryoukikaku AS nikaimeyouzai2_tyougouryoukikaku,2kaimeyouzai2_buzaizaikolotno1 AS nikaimeyouzai2_buzaizaikolotno1,"
                + "2kaimeyouzai2_tyougouryou1 AS nikaimeyouzai2_tyougouryou1,2kaimeyouzai2_buzaizaikolotno2 AS nikaimeyouzai2_buzaizaikolotno2,"
                + "2kaimeyouzai2_tyougouryou2 AS nikaimeyouzai2_tyougouryou2,2kaimetantousya AS nikaimetantousya,3kaimekakuhansetubi AS sankaimekakuhansetubi,3kaimekakuhanjikan AS sankaimekakuhanjikan,"
                + "3kaimekakuhankaisinichiji AS sankaimekakuhankaisinichiji,3kaimekakuhansyuuryounichiji AS sankaimekakuhansyuuryounichiji,3kaimedassizaranosyurui AS sankaimedassizaranosyurui,"
                + "3kaimearumizarafuutaijyuuryou AS sankaimearumizarafuutaijyuuryou,3kaimekansoumaeslurryjyuuryou AS sankaimekansoumaeslurryjyuuryou,3kaimekansouki AS sankaimekansouki,"
                + "3kaimekansouondo AS sankaimekansouondo,3kaimekansoujikan AS sankaimekansoujikan,3kaimekansoukaisinichij AS sankaimekansoukaisinichij,"
                + "3kaimekansousyuuryounichiji AS sankaimekansousyuuryounichiji,3kaimekansougosoujyuuryou AS sankaimekansougosoujyuuryou,3kaimekansougosyoumijyuuryou AS sankaimekansougosyoumijyuuryou,"
                + "3kaimekokeibunhiritu AS sankaimekokeibunhiritu,3kaimekokeibunsokuteitantousya AS sankaimekokeibunsokuteitantousya,FPjyunbi_fuutaijyuuryou AS fpjyunbi_fuutaijyuuryou,"
                + "FPjyunbi_tantousya AS fpjyunbi_tantousya,filterrenketu,FPjyunbi_filterhinmei1 AS fpjyunbi_filterhinmei1,FPjyunbi_lotno1 AS fpjyunbi_lotno1,"
                + "FPjyunbi_toritukehonsuu1 AS fpjyunbi_toritukehonsuu1,FPjyunbi_filterhinmei2 AS fpjyunbi_filterhinmei2,FPjyunbi_lotno2 AS fpjyunbi_lotno2,"
                + "FPjyunbi_toritukehonsuu2 AS fpjyunbi_toritukehonsuu2,assoutanknosenjyou,haisyutuyoukinoutibukuro,Fptankno AS fptankno,Fpkaisinichiji AS fpkaisinichiji,"
                + "assouregulatorNo AS assouregulatorno,assouaturyoku,FPkaisi_tantousya AS fpkaisi_tantousya,Fpteisinichiji AS fpteisinichiji,FPkoukan_filterhinmei1 AS fpkoukan_filterhinmei1,"
                + "FPkoukan_lotno1 AS fpkoukan_lotno1,FPkoukan_toritukehonsuu1 AS fpkoukan_toritukehonsuu1,FPkoukan_filterhinmei2 AS fpkoukan_filterhinmei2,FPkoukan_lotno2 AS fpkoukan_lotno2,"
                + "FPkoukan_toritukehonsuu2 AS fpkoukan_toritukehonsuu2,FPsaikainichiji AS fpsaikainichiji,FPkoukan_tantousya AS fpkoukan_tantousya,FPsyuuryounichiji AS fpsyuuryounichiji,"
                + "FPjikan AS fpjikan,FPsyuurou_tantousya AS fpsyuurou_tantousya,soujyuuryou,syoumijyuuryou,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + " FROM tmp_sr_slip_slurrykokeibuntyousei_sutenyouki "
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
        mapping.put("kojyo", "kojyo");                                                              // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                                              // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                                            // 枝番
        mapping.put("sliphinmei", "sliphinmei");                                                    // ｽﾘｯﾌﾟ品名
        mapping.put("sliplotno", "sliplotno");                                                      // ｽﾘｯﾌﾟLotNo
        mapping.put("lotkubun", "lotkubun");                                                        // ﾛｯﾄ区分
        mapping.put("genryoukigou", "genryoukigou");                                                // 原料記号
        mapping.put("goki", "goki");                                                                // 秤量号機
        mapping.put("slurryhinmei", "slurryhinmei");                                                // ｽﾗﾘｰ品名
        mapping.put("slurrylotno", "slurrylotno");                                                  // ｽﾗﾘｰLotNo
        mapping.put("youkino", "youkino");                                                          // 容器No
        mapping.put("slurryyuukoukigen", "slurryyuukoukigen");                                      // ｽﾗﾘｰ有効期限
        mapping.put("kansoukokeibun", "kansoukokeibun");                                            // 乾燥固形分
        mapping.put("dassikokeibun", "dassikokeibun");                                              // 脱脂固形分
        mapping.put("funsaisyuuryounichiji", "funsaisyuuryounichiji");                              // 粉砕終了日時
        mapping.put("binderkongounichij", "binderkongounichij");                                    // ﾊﾞｲﾝﾀﾞｰ混合日時
        mapping.put("slurraging", "slurraging");                                                    // ｽﾗﾘｰｴｰｼﾞﾝｸﾞ
        mapping.put("slurrykeikanisuu", "slurrykeikanisuu");                                        // ｽﾗﾘｰ経過日数
        mapping.put("slipjyouhou_fuutaijyuuryou", "slipjyouhou_fuutaijyuuryou");                    // ｽﾘｯﾌﾟ情報_風袋重量
        mapping.put("slurrysoujyuuryou", "slurrysoujyuuryou");                                                  // 総重量
        mapping.put("slurryjyuuryou", "slurryjyuuryou");                                            // ｽﾗﾘｰ重量
        mapping.put("kakuhansetubi", "kakuhansetubi");                                              // 撹拌設備
        mapping.put("kakuhangoki", "kakuhangoki");                                                  // 撹拌号機
        mapping.put("kakuhankaitensuu", "kakuhankaitensuu");                                        // 撹拌回転数
        mapping.put("kakuhanjikan", "kakuhanjikan");                                                // 撹拌時間
        mapping.put("kakuhankaisinichiji", "kakuhankaisinichiji");                                  // 撹拌開始日時
        mapping.put("kakuhansyuuryounichiji", "kakuhansyuuryounichiji");                            // 撹拌終了日時
        mapping.put("dassizaranosyurui", "dassizaranosyurui");                                      // 脱脂皿の種類
        mapping.put("kokeibunsokutei_fuutaijyuuryou", "kokeibunsokutei_fuutaijyuuryou");            // 固形分測定_風袋重量
        mapping.put("kansoumaeslurryjyuuryou", "kansoumaeslurryjyuuryou");                          // 乾燥前ｽﾗﾘｰ重量
        mapping.put("kansouki", "kansouki");                                                        // 乾燥機
        mapping.put("kansouondo", "kansouondo");                                                    // 乾燥温度
        mapping.put("kansoujikan", "kansoujikan");                                                  // 乾燥時間
        mapping.put("kansoukaisinichij", "kansoukaisinichij");                                      // 乾燥開始日時
        mapping.put("kansousyuuryounichiji", "kansousyuuryounichiji");                              // 乾燥終了日時
        mapping.put("kansougosoujyuuryou", "kansougosoujyuuryou");                                  // 乾燥後総重量
        mapping.put("kansougosyoumijyuuryou", "kansougosyoumijyuuryou");                            // 乾燥後正味重量
        mapping.put("kokeibunhiritu", "kokeibunhiritu");                                            // 固形分比率
        mapping.put("kokeibunsokuteitantousya", "kokeibunsokuteitantousya");                        // 固形分測定担当者
        mapping.put("youzaityouseiryou", "youzaityouseiryou");                                      // 溶剤調整量
        mapping.put("toluenetyouseiryou", "toluenetyouseiryou");                                    // ﾄﾙｴﾝ調整量
        mapping.put("solmixtyouseiryou", "solmixtyouseiryou");                                      // ｿﾙﾐｯｸｽ調整量
        mapping.put("youzaikeiryounichiji", "youzaikeiryounichiji");                                // 溶剤秤量日時
        mapping.put("youzai1_zairyouhinmei", "youzai1_zairyouhinmei");                              // 溶剤①_材料品名
        mapping.put("youzai1_tyougouryoukikaku", "youzai1_tyougouryoukikaku");                      // 溶剤①_調合量規格
        mapping.put("youzai1_buzaizaikolotno1", "youzai1_buzaizaikolotno1");                        // 溶剤①_部材在庫No1
        mapping.put("youzai1_tyougouryou1", "youzai1_tyougouryou1");                                // 溶剤①_調合量1
        mapping.put("youzai1_buzaizaikolotno2", "youzai1_buzaizaikolotno2");                        // 溶剤①_部材在庫No2
        mapping.put("youzai1_tyougouryou2", "youzai1_tyougouryou2");                                // 溶剤①_調合量2
        mapping.put("youzai2_zairyouhinmei", "youzai2_zairyouhinmei");                              // 溶剤②_材料品名
        mapping.put("youzai2_tyougouryoukikaku", "youzai2_tyougouryoukikaku");                      // 溶剤②_調合量規格
        mapping.put("youzai2_buzaizaikolotno1", "youzai2_buzaizaikolotno1");                        // 溶剤②_部材在庫No1
        mapping.put("youzai2_tyougouryou1", "youzai2_tyougouryou1");                                // 溶剤②_調合量1
        mapping.put("youzai2_buzaizaikolotno2", "youzai2_buzaizaikolotno2");                        // 溶剤②_部材在庫No2
        mapping.put("youzai2_tyougouryou2", "youzai2_tyougouryou2");                                // 溶剤②_調合量2
        mapping.put("tantousya", "tantousya");                                                      // 担当者
        mapping.put("nikaimekakuhansetubi", "nikaimekakuhansetubi");                                // 2回目撹拌設備
        mapping.put("nikaimekakuhanjikan", "nikaimekakuhanjikan");                                  // 2回目撹拌時間
        mapping.put("nikaimekakuhankaisinichiji", "nikaimekakuhankaisinichiji");                    // 2回目撹拌開始日時
        mapping.put("nikaimekakuhansyuuryounichiji", "nikaimekakuhansyuuryounichiji");              // 2回目撹拌終了日時
        mapping.put("nikaimedassizaranosyurui", "nikaimedassizaranosyurui");                        // 2回目脱脂皿の種類
        mapping.put("nikaimearumizarafuutaijyuuryou", "nikaimearumizarafuutaijyuuryou");            // 2回目ｱﾙﾐ皿風袋重量
        mapping.put("nikaimekansoumaeslurryjyuuryou", "nikaimekansoumaeslurryjyuuryou");            // 2回目乾燥前ｽﾗﾘｰ重量
        mapping.put("nikaimekansouki", "nikaimekansouki");                                          // 2回目乾燥機
        mapping.put("nikaimekansouondo", "nikaimekansouondo");                                      // 2回目乾燥温度
        mapping.put("nikaimekansoujikan", "nikaimekansoujikan");                                    // 2回目乾燥時間
        mapping.put("nikaimekansoukaisinichij", "nikaimekansoukaisinichij");                        // 2回目乾燥開始日時
        mapping.put("nikaimekansousyuuryounichiji", "nikaimekansousyuuryounichiji");                // 2回目乾燥終了日時
        mapping.put("nikaimekansougosoujyuuryou", "nikaimekansougosoujyuuryou");                    // 2回目乾燥後総重量
        mapping.put("nikaimekansougosyoumijyuuryou", "nikaimekansougosyoumijyuuryou");              // 2回目乾燥後正味重量
        mapping.put("nikaimekokeibunhiritu", "nikaimekokeibunhiritu");                              // 2回目固形分比率
        mapping.put("nikaimekokeibunsokuteitantousya", "nikaimekokeibunsokuteitantousya");          // 2回目固形分測定担当者
        mapping.put("nikaimeyouzaityouseiryou", "nikaimeyouzaityouseiryou");                        // 2回目溶剤調整量
        mapping.put("nikaimetoluenetyouseiryou", "nikaimetoluenetyouseiryou");                      // 2回目ﾄﾙｴﾝ調整量
        mapping.put("nikaimesolmixtyouseiryou", "nikaimesolmixtyouseiryou");                        // 2回目ｿﾙﾐｯｸｽ調整量
        mapping.put("nikaimeyouzaikeiryounichiji", "nikaimeyouzaikeiryounichiji");                  // 2回目溶剤秤量日時
        mapping.put("nikaimeyouzai1_zairyouhinmei", "nikaimeyouzai1_zairyouhinmei");                // 2回目溶剤①_材料品名
        mapping.put("nikaimeyouzai1_tyougouryoukikaku", "nikaimeyouzai1_tyougouryoukikaku");        // 2回目溶剤①_調合量規格
        mapping.put("nikaimeyouzai1_buzaizaikolotno1", "nikaimeyouzai1_buzaizaikolotno1");          // 2回目溶剤①_部材在庫No1
        mapping.put("nikaimeyouzai1_tyougouryou1", "nikaimeyouzai1_tyougouryou1");                  // 2回目溶剤①_調合量1
        mapping.put("nikaimeyouzai1_buzaizaikolotno2", "nikaimeyouzai1_buzaizaikolotno2");          // 2回目溶剤①_部材在庫No2
        mapping.put("nikaimeyouzai1_tyougouryou2", "nikaimeyouzai1_tyougouryou2");                  // 2回目溶剤①_調合量2
        mapping.put("nikaimeyouzai2_zairyouhinmei", "nikaimeyouzai2_zairyouhinmei");                // 2回目溶剤②_材料品名
        mapping.put("nikaimeyouzai2_tyougouryoukikaku", "nikaimeyouzai2_tyougouryoukikaku");        // 2回目溶剤②_調合量規格
        mapping.put("nikaimeyouzai2_buzaizaikolotno1", "nikaimeyouzai2_buzaizaikolotno1");          // 2回目溶剤②_部材在庫No1
        mapping.put("nikaimeyouzai2_tyougouryou1", "nikaimeyouzai2_tyougouryou1");                  // 2回目溶剤②_調合量1
        mapping.put("nikaimeyouzai2_buzaizaikolotno2", "nikaimeyouzai2_buzaizaikolotno2");          // 2回目溶剤②_部材在庫No2
        mapping.put("nikaimeyouzai2_tyougouryou2", "nikaimeyouzai2_tyougouryou2");                  // 2回目溶剤②_調合量2
        mapping.put("nikaimetantousya", "nikaimetantousya");                                        // 2回目担当者
        mapping.put("sankaimekakuhansetubi", "sankaimekakuhansetubi");                              // 3回目撹拌設備
        mapping.put("sankaimekakuhanjikan", "sankaimekakuhanjikan");                                // 3回目撹拌時間
        mapping.put("sankaimekakuhankaisinichiji", "sankaimekakuhankaisinichiji");                  // 3回目撹拌開始日時
        mapping.put("sankaimekakuhansyuuryounichiji", "sankaimekakuhansyuuryounichiji");            // 3回目撹拌終了日時
        mapping.put("sankaimedassizaranosyurui", "sankaimedassizaranosyurui");                      // 3回目脱脂皿の種類
        mapping.put("sankaimearumizarafuutaijyuuryou", "sankaimearumizarafuutaijyuuryou");          // 3回目ｱﾙﾐ皿風袋重量
        mapping.put("sankaimekansoumaeslurryjyuuryou", "sankaimekansoumaeslurryjyuuryou");          // 3回目乾燥前ｽﾗﾘｰ重量
        mapping.put("sankaimekansouki", "sankaimekansouki");                                        // 3回目乾燥機
        mapping.put("sankaimekansouondo", "sankaimekansouondo");                                    // 3回目乾燥温度
        mapping.put("sankaimekansoujikan", "sankaimekansoujikan");                                  // 3回目乾燥時間
        mapping.put("sankaimekansoukaisinichij", "sankaimekansoukaisinichij");                      // 3回目乾燥開始日時
        mapping.put("sankaimekansousyuuryounichiji", "sankaimekansousyuuryounichiji");              // 3回目乾燥終了日時
        mapping.put("sankaimekansougosoujyuuryou", "sankaimekansougosoujyuuryou");                  // 3回目乾燥後総重量
        mapping.put("sankaimekansougosyoumijyuuryou", "sankaimekansougosyoumijyuuryou");            // 3回目乾燥後正味重量
        mapping.put("sankaimekokeibunhiritu", "sankaimekokeibunhiritu");                            // 3回目固形分比率
        mapping.put("sankaimekokeibunsokuteitantousya", "sankaimekokeibunsokuteitantousya");        // 3回目固形分測定担当者
        mapping.put("fpjyunbi_fuutaijyuuryou", "fpjyunbi_fuutaijyuuryou");                          // F/P準備_風袋重量
        mapping.put("fpjyunbi_tantousya", "fpjyunbi_tantousya");                                    // F/P準備_担当者
        mapping.put("filterrenketu", "filterrenketu");                                              // ﾌｨﾙﾀｰ連結
        mapping.put("fpjyunbi_filterhinmei1", "fpjyunbi_filterhinmei1");                            // F/P準備_ﾌｨﾙﾀｰ品名①
        mapping.put("fpjyunbi_lotno1", "fpjyunbi_lotno1");                                          // F/P準備_LotNo①
        mapping.put("fpjyunbi_toritukehonsuu1", "fpjyunbi_toritukehonsuu1");                        // F/P準備_取り付け本数①
        mapping.put("fpjyunbi_filterhinmei2", "fpjyunbi_filterhinmei2");                            // F/P準備_ﾌｨﾙﾀｰ品名②
        mapping.put("fpjyunbi_lotno2", "fpjyunbi_lotno2");                                          // F/P準備_LotNo②
        mapping.put("fpjyunbi_toritukehonsuu2", "fpjyunbi_toritukehonsuu2");                        // F/P準備_取り付け本数②
        mapping.put("assoutanknosenjyou", "assoutanknosenjyou");                                    // 圧送ﾀﾝｸの洗浄
        mapping.put("haisyutuyoukinoutibukuro", "haisyutuyoukinoutibukuro");                        // 排出容器の内袋
        mapping.put("fptankno", "fptankno");                                                        // F/PﾀﾝｸNo
        mapping.put("fpkaisinichiji", "fpkaisinichiji");                                            // F/P開始日時
        mapping.put("assouregulatorno", "assouregulatorno");                                        // 圧送ﾚｷﾞｭﾚｰﾀｰNo
        mapping.put("assouaturyoku", "assouaturyoku");                                              // 圧送圧力
        mapping.put("fpkaisi_tantousya", "fpkaisi_tantousya");                                      // F/P開始_担当者
        mapping.put("fpteisinichiji", "fpteisinichiji");                                            // F/P停止日時
        mapping.put("fpkoukan_filterhinmei1", "fpkoukan_filterhinmei1");                            // F/P交換_ﾌｨﾙﾀｰ品名①
        mapping.put("fpkoukan_lotno1", "fpkoukan_lotno1");                                          // F/P交換_LotNo①
        mapping.put("fpkoukan_toritukehonsuu1", "fpkoukan_toritukehonsuu1");                        // F/P交換_取り付け本数①
        mapping.put("fpkoukan_filterhinmei2", "fpkoukan_filterhinmei2");                            // F/P交換_ﾌｨﾙﾀｰ品名②
        mapping.put("fpkoukan_lotno2", "fpkoukan_lotno2");                                          // F/P交換_LotNo②
        mapping.put("fpkoukan_toritukehonsuu2", "fpkoukan_toritukehonsuu2");                        // F/P交換_取り付け本数②
        mapping.put("fpsaikainichiji", "fpsaikainichiji");                                          // F/P再開日時
        mapping.put("fpkoukan_tantousya", "fpkoukan_tantousya");                                    // F/P交換_担当者
        mapping.put("fpsyuuryounichiji", "fpsyuuryounichiji");                                      // F/P終了日時
        mapping.put("fpjikan", "fpjikan");                                                          // F/P時間
        mapping.put("fpsyuurou_tantousya", "fpsyuurou_tantousya");                                  // F/P終了_担当者
        mapping.put("soujyuuryou", "soujyuuryou");                                                  // 総重量
        mapping.put("syoumijyuuryou", "syoumijyuuryou");                                            // 正味重量
        mapping.put("bikou1", "bikou1");                                                            // 備考1
        mapping.put("bikou2", "bikou2");                                                            // 備考2
        mapping.put("torokunichiji", "torokunichiji");                                              // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                                                // 更新日時
        mapping.put("revision", "revision");                                                        // revision
        mapping.put("deleteflag", "deleteflag");                                                    // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrSlipSlurrykokeibuntyouseiSutenyouki>> beanHandler = new BeanListHandler<>(SrSlipSlurrykokeibuntyouseiSutenyouki.class, rowProcessor);

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
     * @param srSlipSlurrykokeibuntyouseiSutenyouki ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)データ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrSlipSlurrykokeibuntyouseiSutenyouki srSlipSlurrykokeibuntyouseiSutenyouki) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srSlipSlurrykokeibuntyouseiSutenyouki != null) {
            // 元データが存在する場合元データより取得
            return getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(itemId, srSlipSlurrykokeibuntyouseiSutenyouki);
        } else {
            return null;
        }
    }

    /**
     * 項目データ(入力値)取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @param srGlasshyoryo ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)データ
     * @return 入力値
     */
    private String getItemKikakuchi(List<FXHDD01> listData, String itemId, SrSlipSlurrykokeibuntyouseiSutenyouki srSlipSlurrykokeibuntyouseiSutenyouki) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return StringUtil.nullToBlank(selectData.get(0).getKikakuChi()).replace("【", "").replace("】", "");
        } else if (srSlipSlurrykokeibuntyouseiSutenyouki != null) {
            // 元データが存在する場合元データより取得
            return getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(itemId, srSlipSlurrykokeibuntyouseiSutenyouki);
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
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)_仮登録(tmp_sr_slip_slurrykokeibuntyousei_sutenyouki)登録処理
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
    private void insertTmpSrSlipSlurrykokeibuntyouseiSutenyouki(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "INSERT INTO tmp_sr_slip_slurrykokeibuntyousei_sutenyouki ( "
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,goki,slurryhinmei,slurrylotno,youkino,slurryyuukoukigen,kansoukokeibun,dassikokeibun,"
                + "funsaisyuuryounichiji,binderkongounichij,slurraging,slurrykeikanisuu,slipjyouhou_fuutaijyuuryou,slurrysoujyuuryou,slurryjyuuryou,kakuhansetubi,kakuhangoki,"
                + "kakuhankaitensuu,kakuhanjikan,kakuhankaisinichiji,kakuhansyuuryounichiji,dassizaranosyurui,kokeibunsokutei_fuutaijyuuryou,kansoumaeslurryjyuuryou,kansouki,"
                + "kansouondo,kansoujikan,kansoukaisinichij,kansousyuuryounichiji,kansougosoujyuuryou,kansougosyoumijyuuryou,kokeibunhiritu,kokeibunsokuteitantousya,"
                + "youzaityouseiryou,toluenetyouseiryou,solmixtyouseiryou,youzaikeiryounichiji,youzai1_zairyouhinmei,youzai1_tyougouryoukikaku,youzai1_buzaizaikolotno1,"
                + "youzai1_tyougouryou1,youzai1_buzaizaikolotno2,youzai1_tyougouryou2,youzai2_zairyouhinmei,youzai2_tyougouryoukikaku,youzai2_buzaizaikolotno1,youzai2_tyougouryou1,"
                + "youzai2_buzaizaikolotno2,youzai2_tyougouryou2,tantousya,2kaimekakuhansetubi,2kaimekakuhanjikan,2kaimekakuhankaisinichiji,2kaimekakuhansyuuryounichiji,"
                + "2kaimedassizaranosyurui,2kaimearumizarafuutaijyuuryou,2kaimekansoumaeslurryjyuuryou,2kaimekansouki,2kaimekansouondo,2kaimekansoujikan,2kaimekansoukaisinichij,"
                + "2kaimekansousyuuryounichiji,2kaimekansougosoujyuuryou,2kaimekansougosyoumijyuuryou,2kaimekokeibunhiritu,2kaimekokeibunsokuteitantousya,2kaimeyouzaityouseiryou,"
                + "2kaimetoluenetyouseiryou,2kaimesolmixtyouseiryou,2kaimeyouzaikeiryounichiji,2kaimeyouzai1_zairyouhinmei,2kaimeyouzai1_tyougouryoukikaku,2kaimeyouzai1_buzaizaikolotno1,"
                + "2kaimeyouzai1_tyougouryou1,2kaimeyouzai1_buzaizaikolotno2,2kaimeyouzai1_tyougouryou2,2kaimeyouzai2_zairyouhinmei,2kaimeyouzai2_tyougouryoukikaku,"
                + "2kaimeyouzai2_buzaizaikolotno1,2kaimeyouzai2_tyougouryou1,2kaimeyouzai2_buzaizaikolotno2,2kaimeyouzai2_tyougouryou2,2kaimetantousya,3kaimekakuhansetubi,3kaimekakuhanjikan,"
                + "3kaimekakuhankaisinichiji,3kaimekakuhansyuuryounichiji,3kaimedassizaranosyurui,3kaimearumizarafuutaijyuuryou,3kaimekansoumaeslurryjyuuryou,3kaimekansouki,"
                + "3kaimekansouondo,3kaimekansoujikan,3kaimekansoukaisinichij,3kaimekansousyuuryounichiji,3kaimekansougosoujyuuryou,3kaimekansougosyoumijyuuryou,3kaimekokeibunhiritu,"
                + "3kaimekokeibunsokuteitantousya,FPjyunbi_fuutaijyuuryou,FPjyunbi_tantousya,filterrenketu,FPjyunbi_filterhinmei1,FPjyunbi_lotno1,FPjyunbi_toritukehonsuu1,FPjyunbi_filterhinmei2,"
                + "FPjyunbi_lotno2,FPjyunbi_toritukehonsuu2,assoutanknosenjyou,haisyutuyoukinoutibukuro,Fptankno,Fpkaisinichiji,assouregulatorNo,assouaturyoku,FPkaisi_tantousya,Fpteisinichiji,"
                + "FPkoukan_filterhinmei1,FPkoukan_lotno1,FPkoukan_toritukehonsuu1,FPkoukan_filterhinmei2,FPkoukan_lotno2,FPkoukan_toritukehonsuu2,FPsaikainichiji,FPkoukan_tantousya,"
                + "FPsyuuryounichiji,FPjikan,FPsyuurou_tantousya,soujyuuryou,syoumijyuuryou,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag"
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrSlipSlurrykokeibuntyouseiSutenyouki(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, processData, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)_仮登録(tmp_sr_slip_slurrykokeibuntyousei_sutenyouki)更新処理
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
    private SrSlipSlurrykokeibuntyouseiSutenyouki updateTmpSrSlipSlurrykokeibuntyouseiSutenyouki(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "UPDATE tmp_sr_slip_slurrykokeibuntyousei_sutenyouki SET "
                + " sliphinmei = ?,sliplotno = ?,lotkubun = ?,genryoukigou = ?,goki = ?,slurryhinmei = ?,slurrylotno = ?,youkino = ?,slurryyuukoukigen = ?,kansoukokeibun = ?,dassikokeibun = ?,"
                + "funsaisyuuryounichiji = ?,binderkongounichij = ?,slurraging = ?,slurrykeikanisuu = ?,slipjyouhou_fuutaijyuuryou = ?,slurrysoujyuuryou = ?,slurryjyuuryou = ?,kakuhansetubi = ?,"
                + "kakuhangoki = ?,kakuhankaitensuu = ?,kakuhanjikan = ?,kakuhankaisinichiji = ?,kakuhansyuuryounichiji = ?,dassizaranosyurui = ?,kokeibunsokutei_fuutaijyuuryou = ?,"
                + "kansoumaeslurryjyuuryou = ?,kansouki = ?,kansouondo = ?,kansoujikan = ?,kansoukaisinichij = ?,kansousyuuryounichiji = ?,kansougosoujyuuryou = ?,kansougosyoumijyuuryou = ?,"
                + "kokeibunhiritu = ?,kokeibunsokuteitantousya = ?,youzaityouseiryou = ?,toluenetyouseiryou = ?,solmixtyouseiryou = ?,youzaikeiryounichiji = ?,youzai1_zairyouhinmei = ?,"
                + "youzai1_tyougouryoukikaku = ?,youzai1_buzaizaikolotno1 = ?,youzai1_tyougouryou1 = ?,youzai1_buzaizaikolotno2 = ?,youzai1_tyougouryou2 = ?,youzai2_zairyouhinmei = ?,"
                + "youzai2_tyougouryoukikaku = ?,youzai2_buzaizaikolotno1 = ?,youzai2_tyougouryou1 = ?,youzai2_buzaizaikolotno2 = ?,youzai2_tyougouryou2 = ?,tantousya = ?,2kaimekakuhansetubi = ?,"
                + "2kaimekakuhanjikan = ?,2kaimekakuhankaisinichiji = ?,2kaimekakuhansyuuryounichiji = ?,2kaimedassizaranosyurui = ?,2kaimearumizarafuutaijyuuryou = ?,2kaimekansoumaeslurryjyuuryou = ?,"
                + "2kaimekansouki = ?,2kaimekansouondo = ?,2kaimekansoujikan = ?,2kaimekansoukaisinichij = ?,2kaimekansousyuuryounichiji = ?,2kaimekansougosoujyuuryou = ?,"
                + "2kaimekansougosyoumijyuuryou = ?,2kaimekokeibunhiritu = ?,2kaimekokeibunsokuteitantousya = ?,2kaimeyouzaityouseiryou = ?,2kaimetoluenetyouseiryou = ?,2kaimesolmixtyouseiryou = ?,"
                + "2kaimeyouzaikeiryounichiji = ?,2kaimeyouzai1_zairyouhinmei = ?,2kaimeyouzai1_tyougouryoukikaku = ?,2kaimeyouzai1_buzaizaikolotno1 = ?,2kaimeyouzai1_tyougouryou1 = ?,"
                + "2kaimeyouzai1_buzaizaikolotno2 = ?,2kaimeyouzai1_tyougouryou2 = ?,2kaimeyouzai2_zairyouhinmei = ?,2kaimeyouzai2_tyougouryoukikaku = ?,2kaimeyouzai2_buzaizaikolotno1 = ?,"
                + "2kaimeyouzai2_tyougouryou1 = ?,2kaimeyouzai2_buzaizaikolotno2 = ?,2kaimeyouzai2_tyougouryou2 = ?,2kaimetantousya = ?,3kaimekakuhansetubi = ?,3kaimekakuhanjikan = ?,"
                + "3kaimekakuhankaisinichiji = ?,3kaimekakuhansyuuryounichiji = ?,3kaimedassizaranosyurui = ?,3kaimearumizarafuutaijyuuryou = ?,3kaimekansoumaeslurryjyuuryou = ?,"
                + "3kaimekansouki = ?,3kaimekansouondo = ?,3kaimekansoujikan = ?,3kaimekansoukaisinichij = ?,3kaimekansousyuuryounichiji = ?,3kaimekansougosoujyuuryou = ?,"
                + "3kaimekansougosyoumijyuuryou = ?,3kaimekokeibunhiritu = ?,3kaimekokeibunsokuteitantousya = ?,FPjyunbi_fuutaijyuuryou = ?,FPjyunbi_tantousya = ?,filterrenketu = ?,"
                + "FPjyunbi_filterhinmei1 = ?,FPjyunbi_lotno1 = ?,FPjyunbi_toritukehonsuu1 = ?,FPjyunbi_filterhinmei2 = ?,FPjyunbi_lotno2 = ?,FPjyunbi_toritukehonsuu2 = ?,assoutanknosenjyou = ?,"
                + "haisyutuyoukinoutibukuro = ?,Fptankno = ?,Fpkaisinichiji = ?,assouregulatorNo = ?,assouaturyoku = ?,FPkaisi_tantousya = ?,Fpteisinichiji = ?,FPkoukan_filterhinmei1 = ?,"
                + "FPkoukan_lotno1 = ?,FPkoukan_toritukehonsuu1 = ?,FPkoukan_filterhinmei2 = ?,FPkoukan_lotno2 = ?,FPkoukan_toritukehonsuu2 = ?,FPsaikainichiji = ?,FPkoukan_tantousya = ?,"
                + "FPsyuuryounichiji = ?,FPjikan = ?,FPsyuurou_tantousya = ?,soujyuuryou = ?,syoumijyuuryou = ?,bikou1 = ?,bikou2 = ?,kosinnichiji = ?,revision = ?,deleteflag = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrSlipSlurrykokeibuntyouseiSutenyouki> srSlipSlurrykokeibuntyouseiSutenyoukiList = getSrSlipSlurrykokeibuntyouseiSutenyoukiData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrSlipSlurrykokeibuntyouseiSutenyouki srSlipSlurrykokeibuntyouseiSutenyouki = null;
        if (!srSlipSlurrykokeibuntyouseiSutenyoukiList.isEmpty()) {
            srSlipSlurrykokeibuntyouseiSutenyouki = srSlipSlurrykokeibuntyouseiSutenyoukiList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrSlipSlurrykokeibuntyouseiSutenyouki(false, newRev, 0, "", "", "", systemTime, processData, srSlipSlurrykokeibuntyouseiSutenyouki);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
        return srSlipSlurrykokeibuntyouseiSutenyouki;
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)_仮登録(tmp_sr_slip_slurrykokeibuntyousei_sutenyouki)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrSlipSlurrykokeibuntyouseiSutenyouki(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sr_slip_slurrykokeibuntyousei_sutenyouki "
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
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)_仮登録(tmp_sr_slip_slurrykokeibuntyousei_sutenyouki)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srSlipSlurrykokeibuntyouseiSutenyouki ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)データ
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrSlipSlurrykokeibuntyouseiSutenyouki(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, String systemTime, ProcessData processData, SrSlipSlurrykokeibuntyouseiSutenyouki srSlipSlurrykokeibuntyouseiSutenyouki) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // 粉砕終了日時
        String funsaisyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.FUNSAISYUURYOU_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // ﾊﾞｲﾝﾀﾞｰ混合日時
        String binderkongouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.BINDERKONGOU_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // 撹拌開始日時
        String kakuhankaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.KAKUHANKAISI_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // 撹拌終了日時
        String kakuhansyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.KAKUHANSYUURYOU_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // 乾燥開始日時
        String kansoukaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.KANSOUKAISI_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // 乾燥終了日時
        String kansousyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.KANSOUSYUURYOU_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // 溶剤秤量日時
        String youzaikeiryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.YOUZAIKEIRYOU_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // 2回目撹拌開始日時
        String nikaimekakuhankaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.NIKAIMEKAKUHANKAISI_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // 2回目撹拌終了日時
        String nkmkkhsrTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.NKMKKHSR_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // 2回目乾燥開始日時
        String nikaimekansoukaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.NIKAIMEKANSOUKAISI_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // 2回目乾燥終了日時
        String nikaimekansousyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.NIKAIMEKANSOUSYUURYOU_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // 2回目溶剤秤量日時
        String nkmyzkeiryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.NKMYZKEIRYOU_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // 3回目撹拌開始日時
        String sankaimekakuhankaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.SANKAIMEKAKUHANKAISI_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // 3回目撹拌終了日時
        String skmkkhsrTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.SKMKKHSR_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // 3回目乾燥開始日時
        String sankaimekansoukaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.SANKAIMEKANSOUKAISI_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // 3回目乾燥終了日時
        String sankaimekansousyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.SANKAIMEKANSOUSYUURYOU_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // F/P開始日時
        String fpkaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.FPKAISI_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // F/P停止日時
        String fpteisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.FPTEISI_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // F/P再開日時
        String fpsaikaiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.FPSAIKAI_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // F/P終了日時
        String fpsyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.FPSYUURYOU_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.SLIPHINMEI, srSlipSlurrykokeibuntyouseiSutenyouki)));                           // ｽﾘｯﾌﾟ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.SLIPLOTNO, srSlipSlurrykokeibuntyouseiSutenyouki)));                            // ｽﾘｯﾌﾟLotNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.LOTKUBUN, srSlipSlurrykokeibuntyouseiSutenyouki)));                             // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.GENRYOUKIGOU, srSlipSlurrykokeibuntyouseiSutenyouki)));                    // 原料記号
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.GOKI, srSlipSlurrykokeibuntyouseiSutenyouki)));                                 // 秤量号機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.SLURRYHINMEI, srSlipSlurrykokeibuntyouseiSutenyouki)));                    // ｽﾗﾘｰ品名
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.SLURRYLOTNO, srSlipSlurrykokeibuntyouseiSutenyouki)));                        // ｽﾗﾘｰLotNo
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.YOUKINO, srSlipSlurrykokeibuntyouseiSutenyouki)));                            // 容器No
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.SLURRYYUUKOUKIGEN, srSlipSlurrykokeibuntyouseiSutenyouki)));                    // ｽﾗﾘｰ有効期限
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.KANSOUKOKEIBUN, srSlipSlurrykokeibuntyouseiSutenyouki)));              // 乾燥固形分
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.DASSIKOKEIBUN, srSlipSlurrykokeibuntyouseiSutenyouki)));               // 脱脂固形分
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.FUNSAISYUURYOU_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(funsaisyuuryouTime) ? "0000" : funsaisyuuryouTime));                                                                                                     // 粉砕終了日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.BINDERKONGOU_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(binderkongouTime) ? "0000" : binderkongouTime));                                                                                                         // ﾊﾞｲﾝﾀﾞｰ混合日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.SLURRAGING, srSlipSlurrykokeibuntyouseiSutenyouki)));                      // ｽﾗﾘｰｴｰｼﾞﾝｸﾞ
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.SLURRYKEIKANISUU, srSlipSlurrykokeibuntyouseiSutenyouki)));                        // ｽﾗﾘｰ経過日数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.SLIPJYOUHOU_FUUTAIJYUURYOU, srSlipSlurrykokeibuntyouseiSutenyouki)));         // ｽﾘｯﾌﾟ情報_風袋重量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.SLURRYSOUJYUURYOU, srSlipSlurrykokeibuntyouseiSutenyouki)));                             // 総重量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.SLURRYJYUURYOU, srSlipSlurrykokeibuntyouseiSutenyouki)));                          // ｽﾗﾘｰ重量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.KAKUHANSETUBI, srSlipSlurrykokeibuntyouseiSutenyouki)));                   // 撹拌設備
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.KAKUHANGOKI, srSlipSlurrykokeibuntyouseiSutenyouki)));                     // 撹拌号機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.KAKUHANKAITENSUU, srSlipSlurrykokeibuntyouseiSutenyouki)));                // 撹拌回転数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.KAKUHANJIKAN, srSlipSlurrykokeibuntyouseiSutenyouki)));                    // 撹拌時間
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.KAKUHANKAISI_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(kakuhankaisiTime) ? "0000" : kakuhankaisiTime));                                                                                                         // 撹拌開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.KAKUHANSYUURYOU_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(kakuhansyuuryouTime) ? "0000" : kakuhansyuuryouTime));                                                                                                   // 撹拌終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.DASSIZARANOSYURUI, srSlipSlurrykokeibuntyouseiSutenyouki)));               // 脱脂皿の種類
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.KKBSKT_FUUTAIJYUURYOU, srSlipSlurrykokeibuntyouseiSutenyouki)));            // 固形分測定_風袋重量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.KANSOUMAESLURRYJYUURYOU, srSlipSlurrykokeibuntyouseiSutenyouki)));          // 乾燥前ｽﾗﾘｰ重量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.KANSOUKI, srSlipSlurrykokeibuntyouseiSutenyouki)));                        // 乾燥機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.KANSOUONDO, srSlipSlurrykokeibuntyouseiSutenyouki)));                      // 乾燥温度
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.KANSOUJIKAN, srSlipSlurrykokeibuntyouseiSutenyouki)));                     // 乾燥時間
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.KANSOUKAISI_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(kansoukaisiTime) ? "0000" : kansoukaisiTime));                                                                                                           // 乾燥開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.KANSOUSYUURYOU_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(kansousyuuryouTime) ? "0000" : kansousyuuryouTime));                                                                                                     // 乾燥終了日時
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.KANSOUGOSOUJYUURYOU, srSlipSlurrykokeibuntyouseiSutenyouki)));              // 乾燥後総重量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.KANSOUGOSYOUMIJYUURYOU, srSlipSlurrykokeibuntyouseiSutenyouki)));           // 乾燥後正味重量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.KOKEIBUNHIRITU, srSlipSlurrykokeibuntyouseiSutenyouki)));                   // 固形分比率
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.KOKEIBUNSOKUTEITANTOUSYA, srSlipSlurrykokeibuntyouseiSutenyouki)));             // 固形分測定担当者
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.YOUZAITYOUSEIRYOU, srSlipSlurrykokeibuntyouseiSutenyouki)));                       // 溶剤調整量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.TOLUENETYOUSEIRYOU, srSlipSlurrykokeibuntyouseiSutenyouki)));                      // ﾄﾙｴﾝ調整量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.SOLMIXTYOUSEIRYOU, srSlipSlurrykokeibuntyouseiSutenyouki)));                       // ｿﾙﾐｯｸｽ調整量
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.YOUZAIKEIRYOU_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(youzaikeiryouTime) ? "0000" : youzaikeiryouTime));                                                                                                       // 溶剤秤量日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.YOUZAI1_ZAIRYOUHINMEI, srSlipSlurrykokeibuntyouseiSutenyouki)));           // 溶剤①_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.YOUZAI1_TYOUGOURYOUKIKAKU, srSlipSlurrykokeibuntyouseiSutenyouki)));       // 溶剤①_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.YOUZAI1_BUZAIZAIKOLOTNO1, srSlipSlurrykokeibuntyouseiSutenyouki)));             // 溶剤①_部材在庫No1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.YOUZAI1_TYOUGOURYOU1, srSlipSlurrykokeibuntyouseiSutenyouki)));                    // 溶剤①_調合量1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.YOUZAI1_BUZAIZAIKOLOTNO2, srSlipSlurrykokeibuntyouseiSutenyouki)));             // 溶剤①_部材在庫No2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.YOUZAI1_TYOUGOURYOU2, srSlipSlurrykokeibuntyouseiSutenyouki)));                    // 溶剤①_調合量2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.YOUZAI2_ZAIRYOUHINMEI, srSlipSlurrykokeibuntyouseiSutenyouki)));           // 溶剤②_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.YOUZAI2_TYOUGOURYOUKIKAKU, srSlipSlurrykokeibuntyouseiSutenyouki)));       // 溶剤②_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.YOUZAI2_BUZAIZAIKOLOTNO1, srSlipSlurrykokeibuntyouseiSutenyouki)));             // 溶剤②_部材在庫No1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.YOUZAI2_TYOUGOURYOU1, srSlipSlurrykokeibuntyouseiSutenyouki)));                    // 溶剤②_調合量1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.YOUZAI2_BUZAIZAIKOLOTNO2, srSlipSlurrykokeibuntyouseiSutenyouki)));             // 溶剤②_部材在庫No2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.YOUZAI2_TYOUGOURYOU2, srSlipSlurrykokeibuntyouseiSutenyouki)));                    // 溶剤②_調合量2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.TANTOUSYA, srSlipSlurrykokeibuntyouseiSutenyouki)));                            // 担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.NIKAIMEKAKUHANSETUBI, srSlipSlurrykokeibuntyouseiSutenyouki)));            // 2回目撹拌設備
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.NIKAIMEKAKUHANJIKAN, srSlipSlurrykokeibuntyouseiSutenyouki)));             // 2回目撹拌時間
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.NIKAIMEKAKUHANKAISI_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(nikaimekakuhankaisiTime) ? "0000" : nikaimekakuhankaisiTime));                                                                                           // 2回目撹拌開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.NKMKKHSR_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(nkmkkhsrTime) ? "0000" : nkmkkhsrTime));                                                                                                                 // 2回目撹拌終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.NIKAIMEDASSIZARANOSYURUI, srSlipSlurrykokeibuntyouseiSutenyouki)));        // 2回目脱脂皿の種類
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.NKMARMZFTJR, srSlipSlurrykokeibuntyouseiSutenyouki)));                      // 2回目ｱﾙﾐ皿風袋重量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.NKMKSMSLRJR, srSlipSlurrykokeibuntyouseiSutenyouki)));                      // 2回目乾燥前ｽﾗﾘｰ重量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.NIKAIMEKANSOUKI, srSlipSlurrykokeibuntyouseiSutenyouki)));                 // 2回目乾燥機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.NIKAIMEKANSOUONDO, srSlipSlurrykokeibuntyouseiSutenyouki)));               // 2回目乾燥温度
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.NIKAIMEKANSOUJIKAN, srSlipSlurrykokeibuntyouseiSutenyouki)));              // 2回目乾燥時間
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.NIKAIMEKANSOUKAISI_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(nikaimekansoukaisiTime) ? "0000" : nikaimekansoukaisiTime));                                                                                             // 2回目乾燥開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.NIKAIMEKANSOUSYUURYOU_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(nikaimekansousyuuryouTime) ? "0000" : nikaimekansousyuuryouTime));                                                                                       // 2回目乾燥終了日時
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.NIKAIMEKANSOUGOSOUJYUURYOU, srSlipSlurrykokeibuntyouseiSutenyouki)));       // 2回目乾燥後総重量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.NKMKSGSMJR, srSlipSlurrykokeibuntyouseiSutenyouki)));                       // 2回目乾燥後正味重量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.NIKAIMEKOKEIBUNHIRITU, srSlipSlurrykokeibuntyouseiSutenyouki)));            // 2回目固形分比率
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.NKMKKBSKTTTS, srSlipSlurrykokeibuntyouseiSutenyouki)));                         // 2回目固形分測定担当者
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.NKMYZTSR, srSlipSlurrykokeibuntyouseiSutenyouki)));                                // 2回目溶剤調整量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.NIKAIMETOLUENETYOUSEIRYOU, srSlipSlurrykokeibuntyouseiSutenyouki)));               // 2回目ﾄﾙｴﾝ調整量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.NIKAIMESOLMIXTYOUSEIRYOU, srSlipSlurrykokeibuntyouseiSutenyouki)));                // 2回目ｿﾙﾐｯｸｽ調整量
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.NKMYZKEIRYOU_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(nkmyzkeiryouTime) ? "0000" : nkmyzkeiryouTime));                                                                                                         // 2回目溶剤秤量日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.NKMYZ1_ZAIRYOUHINMEI, srSlipSlurrykokeibuntyouseiSutenyouki)));            // 2回目溶剤①_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.NKMYZ1_TYOUGOURYOUKIKAKU, srSlipSlurrykokeibuntyouseiSutenyouki)));        // 2回目溶剤①_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.NKMYZ1_BUZAIZAIKOLOTNO1, srSlipSlurrykokeibuntyouseiSutenyouki)));              // 2回目溶剤①_部材在庫No1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.NKMYZ1_TYOUGOURYOU1, srSlipSlurrykokeibuntyouseiSutenyouki)));                     // 2回目溶剤①_調合量1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.NKMYZ1_BUZAIZAIKOLOTNO2, srSlipSlurrykokeibuntyouseiSutenyouki)));              // 2回目溶剤①_部材在庫No2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.NKMYZ1_TYOUGOURYOU2, srSlipSlurrykokeibuntyouseiSutenyouki)));                     // 2回目溶剤①_調合量2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.NKMYZ2_ZAIRYOUHINMEI, srSlipSlurrykokeibuntyouseiSutenyouki)));            // 2回目溶剤②_材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.NKMYZ2_TYOUGOURYOUKIKAKU, srSlipSlurrykokeibuntyouseiSutenyouki)));        // 2回目溶剤②_調合量規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.NKMYZ2_BUZAIZAIKOLOTNO1, srSlipSlurrykokeibuntyouseiSutenyouki)));              // 2回目溶剤②_部材在庫No1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.NKMYZ2_TYOUGOURYOU1, srSlipSlurrykokeibuntyouseiSutenyouki)));                     // 2回目溶剤②_調合量1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.NKMYZ2_BUZAIZAIKOLOTNO2, srSlipSlurrykokeibuntyouseiSutenyouki)));              // 2回目溶剤②_部材在庫No2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.NKMYZ2_TYOUGOURYOU2, srSlipSlurrykokeibuntyouseiSutenyouki)));                     // 2回目溶剤②_調合量2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.NIKAIMETANTOUSYA, srSlipSlurrykokeibuntyouseiSutenyouki)));                     // 2回目担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.SANKAIMEKAKUHANSETUBI, srSlipSlurrykokeibuntyouseiSutenyouki)));           // 3回目撹拌設備
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.SANKAIMEKAKUHANJIKAN, srSlipSlurrykokeibuntyouseiSutenyouki)));            // 3回目撹拌時間
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.SANKAIMEKAKUHANKAISI_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(sankaimekakuhankaisiTime) ? "0000" : sankaimekakuhankaisiTime));                                                                                         // 3回目撹拌開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.SKMKKHSR_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(skmkkhsrTime) ? "0000" : skmkkhsrTime));                                                                                                                 // 3回目撹拌終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.SANKAIMEDASSIZARANOSYURUI, srSlipSlurrykokeibuntyouseiSutenyouki)));       // 3回目脱脂皿の種類
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.SKMARMZFTJR, srSlipSlurrykokeibuntyouseiSutenyouki)));                      // 3回目ｱﾙﾐ皿風袋重量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.SKMKSMSLRJR, srSlipSlurrykokeibuntyouseiSutenyouki)));                      // 3回目乾燥前ｽﾗﾘｰ重量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.SANKAIMEKANSOUKI, srSlipSlurrykokeibuntyouseiSutenyouki)));                // 3回目乾燥機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.SANKAIMEKANSOUONDO, srSlipSlurrykokeibuntyouseiSutenyouki)));              // 3回目乾燥温度
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.SANKAIMEKANSOUJIKAN, srSlipSlurrykokeibuntyouseiSutenyouki)));             // 3回目乾燥時間
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.SANKAIMEKANSOUKAISI_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(sankaimekansoukaisiTime) ? "0000" : sankaimekansoukaisiTime));                                                                                           // 3回目乾燥開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.SANKAIMEKANSOUSYUURYOU_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(sankaimekansousyuuryouTime) ? "0000" : sankaimekansousyuuryouTime));                                                                                     // 3回目乾燥終了日時
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.SANKAIMEKANSOUGOSOUJYUURYOU, srSlipSlurrykokeibuntyouseiSutenyouki)));      // 3回目乾燥後総重量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.SKMKSGSMJR, srSlipSlurrykokeibuntyouseiSutenyouki)));                       // 3回目乾燥後正味重量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.SANKAIMEKOKEIBUNHIRITU, srSlipSlurrykokeibuntyouseiSutenyouki)));           // 3回目固形分比率
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.SKMKKBSKTTTS, srSlipSlurrykokeibuntyouseiSutenyouki)));                         // 3回目固形分測定担当者
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.FPJYUNBI_FUUTAIJYUURYOU, srSlipSlurrykokeibuntyouseiSutenyouki)));                 // F/P準備_風袋重量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.FPJYUNBI_TANTOUSYA, srSlipSlurrykokeibuntyouseiSutenyouki)));                   // F/P準備_担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.FILTERRENKETU, srSlipSlurrykokeibuntyouseiSutenyouki)));                   // ﾌｨﾙﾀｰ連結
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.FPJYUNBI_FILTERHINMEI1, srSlipSlurrykokeibuntyouseiSutenyouki)));          // F/P準備_ﾌｨﾙﾀｰ品名①
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.FPJYUNBI_LOTNO1, srSlipSlurrykokeibuntyouseiSutenyouki)));                      // F/P準備_LotNo①
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.FPJYUNBI_TORITUKEHONSUU1, srSlipSlurrykokeibuntyouseiSutenyouki)));                // F/P準備_取り付け本数①
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.FPJYUNBI_FILTERHINMEI2, srSlipSlurrykokeibuntyouseiSutenyouki)));          // F/P準備_ﾌｨﾙﾀｰ品名②
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.FPJYUNBI_LOTNO2, srSlipSlurrykokeibuntyouseiSutenyouki)));                      // F/P準備_LotNo②
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.FPJYUNBI_TORITUKEHONSUU2, srSlipSlurrykokeibuntyouseiSutenyouki)));                // F/P準備_取り付け本数②
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B028Const.ASSOUTANKNOSENJYOU, srSlipSlurrykokeibuntyouseiSutenyouki), null));                               // 圧送ﾀﾝｸの洗浄
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B028Const.HAISYUTUYOUKINOUTIBUKURO, srSlipSlurrykokeibuntyouseiSutenyouki), null));                               // 排出容器の内袋
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.FPTANKNO, srSlipSlurrykokeibuntyouseiSutenyouki)));                                // F/PﾀﾝｸNo
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.FPKAISI_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(fpkaisiTime) ? "0000" : fpkaisiTime));                                                                                                                   // F/P開始日時
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.ASSOUREGULATORNO, srSlipSlurrykokeibuntyouseiSutenyouki)));                        // 圧送ﾚｷﾞｭﾚｰﾀｰNo
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.ASSOUATURYOKU, srSlipSlurrykokeibuntyouseiSutenyouki)));                    // 圧送圧力
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.FPKAISI_TANTOUSYA, srSlipSlurrykokeibuntyouseiSutenyouki)));                    // F/P開始_担当者
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.FPTEISI_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(fpteisiTime) ? "0000" : fpteisiTime));                                                                                                                   // F/P停止日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.FPKOUKAN_FILTERHINMEI1, srSlipSlurrykokeibuntyouseiSutenyouki)));          // F/P交換_ﾌｨﾙﾀｰ品名①
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.FPKOUKAN_LOTNO1, srSlipSlurrykokeibuntyouseiSutenyouki)));                      // F/P交換_LotNo①
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.FPKOUKAN_TORITUKEHONSUU1, srSlipSlurrykokeibuntyouseiSutenyouki)));                // F/P交換_取り付け本数①
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B028Const.FPKOUKAN_FILTERHINMEI2, srSlipSlurrykokeibuntyouseiSutenyouki)));          // F/P交換_ﾌｨﾙﾀｰ品名②
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.FPKOUKAN_LOTNO2, srSlipSlurrykokeibuntyouseiSutenyouki)));                      // F/P交換_LotNo②
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.FPKOUKAN_TORITUKEHONSUU2, srSlipSlurrykokeibuntyouseiSutenyouki)));                // F/P交換_取り付け本数②
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.FPSAIKAI_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(fpsaikaiTime) ? "0000" : fpsaikaiTime));                                                                                                                 // F/P再開日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.FPKOUKAN_TANTOUSYA, srSlipSlurrykokeibuntyouseiSutenyouki)));                   // F/P交換_担当者
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.FPSYUURYOU_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(fpsyuuryouTime) ? "0000" : fpsyuuryouTime));                                                                                                             // F/P終了日時
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.FPJIKAN, srSlipSlurrykokeibuntyouseiSutenyouki)));                                 // F/P時間
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.FPSYUUROU_TANTOUSYA, srSlipSlurrykokeibuntyouseiSutenyouki)));                  // F/P終了_担当者
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.SOUJYUURYOU, srSlipSlurrykokeibuntyouseiSutenyouki)));                  // 総重量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.SYOUMIJYUURYOU, srSlipSlurrykokeibuntyouseiSutenyouki)));                   // 正味重量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.BIKOU1, srSlipSlurrykokeibuntyouseiSutenyouki)));                               // 備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B028Const.BIKOU2, srSlipSlurrykokeibuntyouseiSutenyouki)));                               // 備考2

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
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)(sr_slip_slurrykokeibuntyousei_sutenyouki)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param tmpSrSlipSlurrykokeibuntyouseiSutenyouki 仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrSlipSlurrykokeibuntyouseiSutenyouki(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData, SrSlipSlurrykokeibuntyouseiSutenyouki tmpSrSlipSlurrykokeibuntyouseiSutenyouki) throws SQLException {

        String sql = "INSERT INTO sr_slip_slurrykokeibuntyousei_sutenyouki ( "
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,goki,slurryhinmei,slurrylotno,youkino,slurryyuukoukigen,kansoukokeibun,dassikokeibun,"
                + "funsaisyuuryounichiji,binderkongounichij,slurraging,slurrykeikanisuu,slipjyouhou_fuutaijyuuryou,slurrysoujyuuryou,slurryjyuuryou,kakuhansetubi,kakuhangoki,"
                + "kakuhankaitensuu,kakuhanjikan,kakuhankaisinichiji,kakuhansyuuryounichiji,dassizaranosyurui,kokeibunsokutei_fuutaijyuuryou,kansoumaeslurryjyuuryou,kansouki,"
                + "kansouondo,kansoujikan,kansoukaisinichij,kansousyuuryounichiji,kansougosoujyuuryou,kansougosyoumijyuuryou,kokeibunhiritu,kokeibunsokuteitantousya,"
                + "youzaityouseiryou,toluenetyouseiryou,solmixtyouseiryou,youzaikeiryounichiji,youzai1_zairyouhinmei,youzai1_tyougouryoukikaku,youzai1_buzaizaikolotno1,"
                + "youzai1_tyougouryou1,youzai1_buzaizaikolotno2,youzai1_tyougouryou2,youzai2_zairyouhinmei,youzai2_tyougouryoukikaku,youzai2_buzaizaikolotno1,youzai2_tyougouryou1,"
                + "youzai2_buzaizaikolotno2,youzai2_tyougouryou2,tantousya,2kaimekakuhansetubi,2kaimekakuhanjikan,2kaimekakuhankaisinichiji,2kaimekakuhansyuuryounichiji,"
                + "2kaimedassizaranosyurui,2kaimearumizarafuutaijyuuryou,2kaimekansoumaeslurryjyuuryou,2kaimekansouki,2kaimekansouondo,2kaimekansoujikan,2kaimekansoukaisinichij,"
                + "2kaimekansousyuuryounichiji,2kaimekansougosoujyuuryou,2kaimekansougosyoumijyuuryou,2kaimekokeibunhiritu,2kaimekokeibunsokuteitantousya,2kaimeyouzaityouseiryou,"
                + "2kaimetoluenetyouseiryou,2kaimesolmixtyouseiryou,2kaimeyouzaikeiryounichiji,2kaimeyouzai1_zairyouhinmei,2kaimeyouzai1_tyougouryoukikaku,2kaimeyouzai1_buzaizaikolotno1,"
                + "2kaimeyouzai1_tyougouryou1,2kaimeyouzai1_buzaizaikolotno2,2kaimeyouzai1_tyougouryou2,2kaimeyouzai2_zairyouhinmei,2kaimeyouzai2_tyougouryoukikaku,"
                + "2kaimeyouzai2_buzaizaikolotno1,2kaimeyouzai2_tyougouryou1,2kaimeyouzai2_buzaizaikolotno2,2kaimeyouzai2_tyougouryou2,2kaimetantousya,3kaimekakuhansetubi,3kaimekakuhanjikan,"
                + "3kaimekakuhankaisinichiji,3kaimekakuhansyuuryounichiji,3kaimedassizaranosyurui,3kaimearumizarafuutaijyuuryou,3kaimekansoumaeslurryjyuuryou,3kaimekansouki,"
                + "3kaimekansouondo,3kaimekansoujikan,3kaimekansoukaisinichij,3kaimekansousyuuryounichiji,3kaimekansougosoujyuuryou,3kaimekansougosyoumijyuuryou,3kaimekokeibunhiritu,"
                + "3kaimekokeibunsokuteitantousya,FPjyunbi_fuutaijyuuryou,FPjyunbi_tantousya,filterrenketu,FPjyunbi_filterhinmei1,FPjyunbi_lotno1,FPjyunbi_toritukehonsuu1,FPjyunbi_filterhinmei2,"
                + "FPjyunbi_lotno2,FPjyunbi_toritukehonsuu2,assoutanknosenjyou,haisyutuyoukinoutibukuro,Fptankno,Fpkaisinichiji,assouregulatorNo,assouaturyoku,FPkaisi_tantousya,Fpteisinichiji,"
                + "FPkoukan_filterhinmei1,FPkoukan_lotno1,FPkoukan_toritukehonsuu1,FPkoukan_filterhinmei2,FPkoukan_lotno2,FPkoukan_toritukehonsuu2,FPsaikainichiji,FPkoukan_tantousya,"
                + "FPsyuuryounichiji,FPjikan,FPsyuurou_tantousya,soujyuuryou,syoumijyuuryou,bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrSlipSlurrykokeibuntyouseiSutenyouki(true, newRev, kojyo, lotNo, edaban, systemTime, processData, tmpSrSlipSlurrykokeibuntyouseiSutenyouki);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)(sr_slip_slurrykokeibuntyousei_sutenyouki)更新処理
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
    private SrSlipSlurrykokeibuntyouseiSutenyouki updateSrSlipSlurrykokeibuntyouseiSutenyouki(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {
        String sql = "UPDATE sr_slip_slurrykokeibuntyousei_sutenyouki SET "
                + " sliphinmei = ?,sliplotno = ?,lotkubun = ?,genryoukigou = ?,goki = ?,slurryhinmei = ?,slurrylotno = ?,youkino = ?,slurryyuukoukigen = ?,kansoukokeibun = ?,dassikokeibun = ?,"
                + "funsaisyuuryounichiji = ?,binderkongounichij = ?,slurraging = ?,slurrykeikanisuu = ?,slipjyouhou_fuutaijyuuryou = ?,slurrysoujyuuryou = ?,slurryjyuuryou = ?,kakuhansetubi = ?,"
                + "kakuhangoki = ?,kakuhankaitensuu = ?,kakuhanjikan = ?,kakuhankaisinichiji = ?,kakuhansyuuryounichiji = ?,dassizaranosyurui = ?,kokeibunsokutei_fuutaijyuuryou = ?,"
                + "kansoumaeslurryjyuuryou = ?,kansouki = ?,kansouondo = ?,kansoujikan = ?,kansoukaisinichij = ?,kansousyuuryounichiji = ?,kansougosoujyuuryou = ?,kansougosyoumijyuuryou = ?,"
                + "kokeibunhiritu = ?,kokeibunsokuteitantousya = ?,youzaityouseiryou = ?,toluenetyouseiryou = ?,solmixtyouseiryou = ?,youzaikeiryounichiji = ?,youzai1_zairyouhinmei = ?,"
                + "youzai1_tyougouryoukikaku = ?,youzai1_buzaizaikolotno1 = ?,youzai1_tyougouryou1 = ?,youzai1_buzaizaikolotno2 = ?,youzai1_tyougouryou2 = ?,youzai2_zairyouhinmei = ?,"
                + "youzai2_tyougouryoukikaku = ?,youzai2_buzaizaikolotno1 = ?,youzai2_tyougouryou1 = ?,youzai2_buzaizaikolotno2 = ?,youzai2_tyougouryou2 = ?,tantousya = ?,2kaimekakuhansetubi = ?,"
                + "2kaimekakuhanjikan = ?,2kaimekakuhankaisinichiji = ?,2kaimekakuhansyuuryounichiji = ?,2kaimedassizaranosyurui = ?,2kaimearumizarafuutaijyuuryou = ?,2kaimekansoumaeslurryjyuuryou = ?,"
                + "2kaimekansouki = ?,2kaimekansouondo = ?,2kaimekansoujikan = ?,2kaimekansoukaisinichij = ?,2kaimekansousyuuryounichiji = ?,2kaimekansougosoujyuuryou = ?,"
                + "2kaimekansougosyoumijyuuryou = ?,2kaimekokeibunhiritu = ?,2kaimekokeibunsokuteitantousya = ?,2kaimeyouzaityouseiryou = ?,2kaimetoluenetyouseiryou = ?,2kaimesolmixtyouseiryou = ?,"
                + "2kaimeyouzaikeiryounichiji = ?,2kaimeyouzai1_zairyouhinmei = ?,2kaimeyouzai1_tyougouryoukikaku = ?,2kaimeyouzai1_buzaizaikolotno1 = ?,2kaimeyouzai1_tyougouryou1 = ?,"
                + "2kaimeyouzai1_buzaizaikolotno2 = ?,2kaimeyouzai1_tyougouryou2 = ?,2kaimeyouzai2_zairyouhinmei = ?,2kaimeyouzai2_tyougouryoukikaku = ?,2kaimeyouzai2_buzaizaikolotno1 = ?,"
                + "2kaimeyouzai2_tyougouryou1 = ?,2kaimeyouzai2_buzaizaikolotno2 = ?,2kaimeyouzai2_tyougouryou2 = ?,2kaimetantousya = ?,3kaimekakuhansetubi = ?,3kaimekakuhanjikan = ?,"
                + "3kaimekakuhankaisinichiji = ?,3kaimekakuhansyuuryounichiji = ?,3kaimedassizaranosyurui = ?,3kaimearumizarafuutaijyuuryou = ?,3kaimekansoumaeslurryjyuuryou = ?,"
                + "3kaimekansouki = ?,3kaimekansouondo = ?,3kaimekansoujikan = ?,3kaimekansoukaisinichij = ?,3kaimekansousyuuryounichiji = ?,3kaimekansougosoujyuuryou = ?,"
                + "3kaimekansougosyoumijyuuryou = ?,3kaimekokeibunhiritu = ?,3kaimekokeibunsokuteitantousya = ?,FPjyunbi_fuutaijyuuryou = ?,FPjyunbi_tantousya = ?,filterrenketu = ?,"
                + "FPjyunbi_filterhinmei1 = ?,FPjyunbi_lotno1 = ?,FPjyunbi_toritukehonsuu1 = ?,FPjyunbi_filterhinmei2 = ?,FPjyunbi_lotno2 = ?,FPjyunbi_toritukehonsuu2 = ?,assoutanknosenjyou = ?,"
                + "haisyutuyoukinoutibukuro = ?,Fptankno = ?,Fpkaisinichiji = ?,assouregulatorNo = ?,assouaturyoku = ?,FPkaisi_tantousya = ?,Fpteisinichiji = ?,FPkoukan_filterhinmei1 = ?,"
                + "FPkoukan_lotno1 = ?,FPkoukan_toritukehonsuu1 = ?,FPkoukan_filterhinmei2 = ?,FPkoukan_lotno2 = ?,FPkoukan_toritukehonsuu2 = ?,FPsaikainichiji = ?,FPkoukan_tantousya = ?,"
                + "FPsyuuryounichiji = ?,FPjikan = ?,FPsyuurou_tantousya = ?,soujyuuryou = ?,syoumijyuuryou = ?,bikou1 = ?,bikou2 = ?,kosinnichiji = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrSlipSlurrykokeibuntyouseiSutenyouki> srSlipSlurrykokeibuntyouseiSutenyoukiList = getSrSlipSlurrykokeibuntyouseiSutenyoukiData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrSlipSlurrykokeibuntyouseiSutenyouki srSlipSlurrykokeibuntyouseiSutenyouki = null;
        if (!srSlipSlurrykokeibuntyouseiSutenyoukiList.isEmpty()) {
            srSlipSlurrykokeibuntyouseiSutenyouki = srSlipSlurrykokeibuntyouseiSutenyoukiList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrSlipSlurrykokeibuntyouseiSutenyouki(false, newRev, "", "", "", systemTime, processData, srSlipSlurrykokeibuntyouseiSutenyouki);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
        return srSlipSlurrykokeibuntyouseiSutenyouki;
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)(sr_slip_slurrykokeibuntyousei_sutenyouki)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srSlipSlurrykokeibuntyouseiSutenyouki ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrSlipSlurrykokeibuntyouseiSutenyouki(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            String systemTime, ProcessData processData, SrSlipSlurrykokeibuntyouseiSutenyouki srSlipSlurrykokeibuntyouseiSutenyouki) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();

        // 粉砕終了日時
        String funsaisyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.FUNSAISYUURYOU_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // ﾊﾞｲﾝﾀﾞｰ混合日時
        String binderkongouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.BINDERKONGOU_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // 撹拌開始日時
        String kakuhankaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.KAKUHANKAISI_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // 撹拌終了日時
        String kakuhansyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.KAKUHANSYUURYOU_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // 乾燥開始日時
        String kansoukaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.KANSOUKAISI_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // 乾燥終了日時
        String kansousyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.KANSOUSYUURYOU_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // 溶剤秤量日時
        String youzaikeiryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.YOUZAIKEIRYOU_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // 2回目撹拌開始日時
        String nikaimekakuhankaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.NIKAIMEKAKUHANKAISI_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // 2回目撹拌終了日時
        String nkmkkhsrTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.NKMKKHSR_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // 2回目乾燥開始日時
        String nikaimekansoukaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.NIKAIMEKANSOUKAISI_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // 2回目乾燥終了日時
        String nikaimekansousyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.NIKAIMEKANSOUSYUURYOU_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // 2回目溶剤秤量日時
        String nkmyzkeiryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.NKMYZKEIRYOU_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // 3回目撹拌開始日時
        String sankaimekakuhankaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.SANKAIMEKAKUHANKAISI_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // 3回目撹拌終了日時
        String skmkkhsrTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.SKMKKHSR_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // 3回目乾燥開始日時
        String sankaimekansoukaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.SANKAIMEKANSOUKAISI_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // 3回目乾燥終了日時
        String sankaimekansousyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.SANKAIMEKANSOUSYUURYOU_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // F/P開始日時
        String fpkaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.FPKAISI_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // F/P停止日時
        String fpteisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.FPTEISI_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // F/P再開日時
        String fpsaikaiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.FPSAIKAI_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        // F/P終了日時
        String fpsyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B028Const.FPSYUURYOU_TIME, srSlipSlurrykokeibuntyouseiSutenyouki));
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }

        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B028Const.SLIPHINMEI, srSlipSlurrykokeibuntyouseiSutenyouki)));                           // ｽﾘｯﾌﾟ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B028Const.SLIPLOTNO, srSlipSlurrykokeibuntyouseiSutenyouki)));                            // ｽﾘｯﾌﾟLotNo
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B028Const.LOTKUBUN, srSlipSlurrykokeibuntyouseiSutenyouki)));                             // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.GENRYOUKIGOU, srSlipSlurrykokeibuntyouseiSutenyouki)));                    // 原料記号
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B028Const.GOKI, srSlipSlurrykokeibuntyouseiSutenyouki)));                                 // 秤量号機
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.SLURRYHINMEI, srSlipSlurrykokeibuntyouseiSutenyouki)));                    // ｽﾗﾘｰ品名
        params.add(DBUtil.stringToIntObject(getItemKikakuchi(pItemList, GXHDO102B028Const.SLURRYLOTNO, srSlipSlurrykokeibuntyouseiSutenyouki)));                        // ｽﾗﾘｰLotNo
        params.add(DBUtil.stringToIntObject(getItemKikakuchi(pItemList, GXHDO102B028Const.YOUKINO, srSlipSlurrykokeibuntyouseiSutenyouki)));                            // 容器No
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B028Const.SLURRYYUUKOUKIGEN, srSlipSlurrykokeibuntyouseiSutenyouki)));                    // ｽﾗﾘｰ有効期限
        params.add(DBUtil.stringToBigDecimalObject(getItemKikakuchi(pItemList, GXHDO102B028Const.KANSOUKOKEIBUN, srSlipSlurrykokeibuntyouseiSutenyouki)));              // 乾燥固形分
        params.add(DBUtil.stringToBigDecimalObject(getItemKikakuchi(pItemList, GXHDO102B028Const.DASSIKOKEIBUN, srSlipSlurrykokeibuntyouseiSutenyouki)));               // 脱脂固形分
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B028Const.FUNSAISYUURYOU_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(funsaisyuuryouTime) ? "0000" : funsaisyuuryouTime));                                                                                          // 粉砕終了日時
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B028Const.BINDERKONGOU_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(binderkongouTime) ? "0000" : binderkongouTime));                                                                                              // ﾊﾞｲﾝﾀﾞｰ混合日時
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.SLURRAGING, srSlipSlurrykokeibuntyouseiSutenyouki)));                      // ｽﾗﾘｰｴｰｼﾞﾝｸﾞ
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B028Const.SLURRYKEIKANISUU, srSlipSlurrykokeibuntyouseiSutenyouki)));                        // ｽﾗﾘｰ経過日数
        params.add(DBUtil.stringToIntObject(getItemKikakuchi(pItemList, GXHDO102B028Const.SLIPJYOUHOU_FUUTAIJYUURYOU, srSlipSlurrykokeibuntyouseiSutenyouki)));         // ｽﾘｯﾌﾟ情報_風袋重量
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B028Const.SLURRYSOUJYUURYOU, srSlipSlurrykokeibuntyouseiSutenyouki)));                             // 総重量
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B028Const.SLURRYJYUURYOU, srSlipSlurrykokeibuntyouseiSutenyouki)));                          // ｽﾗﾘｰ重量
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.KAKUHANSETUBI, srSlipSlurrykokeibuntyouseiSutenyouki)));                   // 撹拌設備
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.KAKUHANGOKI, srSlipSlurrykokeibuntyouseiSutenyouki)));                     // 撹拌号機
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.KAKUHANKAITENSUU, srSlipSlurrykokeibuntyouseiSutenyouki)));                // 撹拌回転数
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.KAKUHANJIKAN, srSlipSlurrykokeibuntyouseiSutenyouki)));                    // 撹拌時間
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B028Const.KAKUHANKAISI_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(kakuhankaisiTime) ? "0000" : kakuhankaisiTime));                                                                                              // 撹拌開始日時
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B028Const.KAKUHANSYUURYOU_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(kakuhansyuuryouTime) ? "0000" : kakuhansyuuryouTime));                                                                                        // 撹拌終了日時
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.DASSIZARANOSYURUI, srSlipSlurrykokeibuntyouseiSutenyouki)));               // 脱脂皿の種類
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B028Const.KKBSKT_FUUTAIJYUURYOU, srSlipSlurrykokeibuntyouseiSutenyouki)));            // 固形分測定_風袋重量
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B028Const.KANSOUMAESLURRYJYUURYOU, srSlipSlurrykokeibuntyouseiSutenyouki)));          // 乾燥前ｽﾗﾘｰ重量
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.KANSOUKI, srSlipSlurrykokeibuntyouseiSutenyouki)));                        // 乾燥機
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.KANSOUONDO, srSlipSlurrykokeibuntyouseiSutenyouki)));                      // 乾燥温度
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.KANSOUJIKAN, srSlipSlurrykokeibuntyouseiSutenyouki)));                     // 乾燥時間
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B028Const.KANSOUKAISI_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(kansoukaisiTime) ? "0000" : kansoukaisiTime));                                                                                                // 乾燥開始日時
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B028Const.KANSOUSYUURYOU_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(kansousyuuryouTime) ? "0000" : kansousyuuryouTime));                                                                                          // 乾燥終了日時
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B028Const.KANSOUGOSOUJYUURYOU, srSlipSlurrykokeibuntyouseiSutenyouki)));              // 乾燥後総重量
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B028Const.KANSOUGOSYOUMIJYUURYOU, srSlipSlurrykokeibuntyouseiSutenyouki)));           // 乾燥後正味重量
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B028Const.KOKEIBUNHIRITU, srSlipSlurrykokeibuntyouseiSutenyouki)));                   // 固形分比率
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B028Const.KOKEIBUNSOKUTEITANTOUSYA, srSlipSlurrykokeibuntyouseiSutenyouki)));             // 固形分測定担当者
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B028Const.YOUZAITYOUSEIRYOU, srSlipSlurrykokeibuntyouseiSutenyouki)));                       // 溶剤調整量
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B028Const.TOLUENETYOUSEIRYOU, srSlipSlurrykokeibuntyouseiSutenyouki)));                      // ﾄﾙｴﾝ調整量
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B028Const.SOLMIXTYOUSEIRYOU, srSlipSlurrykokeibuntyouseiSutenyouki)));                       // ｿﾙﾐｯｸｽ調整量
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B028Const.YOUZAIKEIRYOU_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(youzaikeiryouTime) ? "0000" : youzaikeiryouTime));                                                                                            // 溶剤秤量日時
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.YOUZAI1_ZAIRYOUHINMEI, srSlipSlurrykokeibuntyouseiSutenyouki)));           // 溶剤①_材料品名
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.YOUZAI1_TYOUGOURYOUKIKAKU, srSlipSlurrykokeibuntyouseiSutenyouki)));       // 溶剤①_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B028Const.YOUZAI1_BUZAIZAIKOLOTNO1, srSlipSlurrykokeibuntyouseiSutenyouki)));             // 溶剤①_部材在庫No1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B028Const.YOUZAI1_TYOUGOURYOU1, srSlipSlurrykokeibuntyouseiSutenyouki)));                    // 溶剤①_調合量1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B028Const.YOUZAI1_BUZAIZAIKOLOTNO2, srSlipSlurrykokeibuntyouseiSutenyouki)));             // 溶剤①_部材在庫No2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B028Const.YOUZAI1_TYOUGOURYOU2, srSlipSlurrykokeibuntyouseiSutenyouki)));                    // 溶剤①_調合量2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.YOUZAI2_ZAIRYOUHINMEI, srSlipSlurrykokeibuntyouseiSutenyouki)));           // 溶剤②_材料品名
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.YOUZAI2_TYOUGOURYOUKIKAKU, srSlipSlurrykokeibuntyouseiSutenyouki)));       // 溶剤②_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B028Const.YOUZAI2_BUZAIZAIKOLOTNO1, srSlipSlurrykokeibuntyouseiSutenyouki)));             // 溶剤②_部材在庫No1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B028Const.YOUZAI2_TYOUGOURYOU1, srSlipSlurrykokeibuntyouseiSutenyouki)));                    // 溶剤②_調合量1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B028Const.YOUZAI2_BUZAIZAIKOLOTNO2, srSlipSlurrykokeibuntyouseiSutenyouki)));             // 溶剤②_部材在庫No2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B028Const.YOUZAI2_TYOUGOURYOU2, srSlipSlurrykokeibuntyouseiSutenyouki)));                    // 溶剤②_調合量2
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B028Const.TANTOUSYA, srSlipSlurrykokeibuntyouseiSutenyouki)));                            // 担当者
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.NIKAIMEKAKUHANSETUBI, srSlipSlurrykokeibuntyouseiSutenyouki)));            // 2回目撹拌設備
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.NIKAIMEKAKUHANJIKAN, srSlipSlurrykokeibuntyouseiSutenyouki)));             // 2回目撹拌時間
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B028Const.NIKAIMEKAKUHANKAISI_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(nikaimekakuhankaisiTime) ? "0000" : nikaimekakuhankaisiTime));                                                                                // 2回目撹拌開始日時
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B028Const.NKMKKHSR_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(nkmkkhsrTime) ? "0000" : nkmkkhsrTime));                                                                                                      // 2回目撹拌終了日時
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.NIKAIMEDASSIZARANOSYURUI, srSlipSlurrykokeibuntyouseiSutenyouki)));        // 2回目脱脂皿の種類
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B028Const.NKMARMZFTJR, srSlipSlurrykokeibuntyouseiSutenyouki)));                      // 2回目ｱﾙﾐ皿風袋重量
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B028Const.NKMKSMSLRJR, srSlipSlurrykokeibuntyouseiSutenyouki)));                      // 2回目乾燥前ｽﾗﾘｰ重量
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.NIKAIMEKANSOUKI, srSlipSlurrykokeibuntyouseiSutenyouki)));                 // 2回目乾燥機
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.NIKAIMEKANSOUONDO, srSlipSlurrykokeibuntyouseiSutenyouki)));               // 2回目乾燥温度
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.NIKAIMEKANSOUJIKAN, srSlipSlurrykokeibuntyouseiSutenyouki)));              // 2回目乾燥時間
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B028Const.NIKAIMEKANSOUKAISI_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(nikaimekansoukaisiTime) ? "0000" : nikaimekansoukaisiTime));                                                                                  // 2回目乾燥開始日時
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B028Const.NIKAIMEKANSOUSYUURYOU_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(nikaimekansousyuuryouTime) ? "0000" : nikaimekansousyuuryouTime));                                                                            // 2回目乾燥終了日時
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B028Const.NIKAIMEKANSOUGOSOUJYUURYOU, srSlipSlurrykokeibuntyouseiSutenyouki)));       // 2回目乾燥後総重量
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B028Const.NKMKSGSMJR, srSlipSlurrykokeibuntyouseiSutenyouki)));                       // 2回目乾燥後正味重量
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B028Const.NIKAIMEKOKEIBUNHIRITU, srSlipSlurrykokeibuntyouseiSutenyouki)));            // 2回目固形分比率
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B028Const.NKMKKBSKTTTS, srSlipSlurrykokeibuntyouseiSutenyouki)));                         // 2回目固形分測定担当者
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B028Const.NKMYZTSR, srSlipSlurrykokeibuntyouseiSutenyouki)));                                // 2回目溶剤調整量
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B028Const.NIKAIMETOLUENETYOUSEIRYOU, srSlipSlurrykokeibuntyouseiSutenyouki)));               // 2回目ﾄﾙｴﾝ調整量
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B028Const.NIKAIMESOLMIXTYOUSEIRYOU, srSlipSlurrykokeibuntyouseiSutenyouki)));                // 2回目ｿﾙﾐｯｸｽ調整量
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B028Const.NKMYZKEIRYOU_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(nkmyzkeiryouTime) ? "0000" : nkmyzkeiryouTime));                                                                                              // 2回目溶剤秤量日時
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.NKMYZ1_ZAIRYOUHINMEI, srSlipSlurrykokeibuntyouseiSutenyouki)));            // 2回目溶剤①_材料品名
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.NKMYZ1_TYOUGOURYOUKIKAKU, srSlipSlurrykokeibuntyouseiSutenyouki)));        // 2回目溶剤①_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B028Const.NKMYZ1_BUZAIZAIKOLOTNO1, srSlipSlurrykokeibuntyouseiSutenyouki)));              // 2回目溶剤①_部材在庫No1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B028Const.NKMYZ1_TYOUGOURYOU1, srSlipSlurrykokeibuntyouseiSutenyouki)));                     // 2回目溶剤①_調合量1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B028Const.NKMYZ1_BUZAIZAIKOLOTNO2, srSlipSlurrykokeibuntyouseiSutenyouki)));              // 2回目溶剤①_部材在庫No2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B028Const.NKMYZ1_TYOUGOURYOU2, srSlipSlurrykokeibuntyouseiSutenyouki)));                     // 2回目溶剤①_調合量2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.NKMYZ2_ZAIRYOUHINMEI, srSlipSlurrykokeibuntyouseiSutenyouki)));            // 2回目溶剤②_材料品名
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.NKMYZ2_TYOUGOURYOUKIKAKU, srSlipSlurrykokeibuntyouseiSutenyouki)));        // 2回目溶剤②_調合量規格
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B028Const.NKMYZ2_BUZAIZAIKOLOTNO1, srSlipSlurrykokeibuntyouseiSutenyouki)));              // 2回目溶剤②_部材在庫No1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B028Const.NKMYZ2_TYOUGOURYOU1, srSlipSlurrykokeibuntyouseiSutenyouki)));                     // 2回目溶剤②_調合量1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B028Const.NKMYZ2_BUZAIZAIKOLOTNO2, srSlipSlurrykokeibuntyouseiSutenyouki)));              // 2回目溶剤②_部材在庫No2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B028Const.NKMYZ2_TYOUGOURYOU2, srSlipSlurrykokeibuntyouseiSutenyouki)));                     // 2回目溶剤②_調合量2
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B028Const.NIKAIMETANTOUSYA, srSlipSlurrykokeibuntyouseiSutenyouki)));                     // 2回目担当者
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.SANKAIMEKAKUHANSETUBI, srSlipSlurrykokeibuntyouseiSutenyouki)));           // 3回目撹拌設備
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.SANKAIMEKAKUHANJIKAN, srSlipSlurrykokeibuntyouseiSutenyouki)));            // 3回目撹拌時間
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B028Const.SANKAIMEKAKUHANKAISI_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(sankaimekakuhankaisiTime) ? "0000" : sankaimekakuhankaisiTime));                                                                              // 3回目撹拌開始日時
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B028Const.SKMKKHSR_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(skmkkhsrTime) ? "0000" : skmkkhsrTime));                                                                                                      // 3回目撹拌終了日時
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.SANKAIMEDASSIZARANOSYURUI, srSlipSlurrykokeibuntyouseiSutenyouki)));       // 3回目脱脂皿の種類
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B028Const.SKMARMZFTJR, srSlipSlurrykokeibuntyouseiSutenyouki)));                      // 3回目ｱﾙﾐ皿風袋重量
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B028Const.SKMKSMSLRJR, srSlipSlurrykokeibuntyouseiSutenyouki)));                      // 3回目乾燥前ｽﾗﾘｰ重量
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.SANKAIMEKANSOUKI, srSlipSlurrykokeibuntyouseiSutenyouki)));                // 3回目乾燥機
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.SANKAIMEKANSOUONDO, srSlipSlurrykokeibuntyouseiSutenyouki)));              // 3回目乾燥温度
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.SANKAIMEKANSOUJIKAN, srSlipSlurrykokeibuntyouseiSutenyouki)));             // 3回目乾燥時間
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B028Const.SANKAIMEKANSOUKAISI_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(sankaimekansoukaisiTime) ? "0000" : sankaimekansoukaisiTime));                                                                                // 3回目乾燥開始日時
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B028Const.SANKAIMEKANSOUSYUURYOU_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(sankaimekansousyuuryouTime) ? "0000" : sankaimekansousyuuryouTime));                                                                          // 3回目乾燥終了日時
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B028Const.SANKAIMEKANSOUGOSOUJYUURYOU, srSlipSlurrykokeibuntyouseiSutenyouki)));      // 3回目乾燥後総重量
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B028Const.SKMKSGSMJR, srSlipSlurrykokeibuntyouseiSutenyouki)));                       // 3回目乾燥後正味重量
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B028Const.SANKAIMEKOKEIBUNHIRITU, srSlipSlurrykokeibuntyouseiSutenyouki)));           // 3回目固形分比率
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B028Const.SKMKKBSKTTTS, srSlipSlurrykokeibuntyouseiSutenyouki)));                         // 3回目固形分測定担当者
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B028Const.FPJYUNBI_FUUTAIJYUURYOU, srSlipSlurrykokeibuntyouseiSutenyouki)));                 // F/P準備_風袋重量
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B028Const.FPJYUNBI_TANTOUSYA, srSlipSlurrykokeibuntyouseiSutenyouki)));                   // F/P準備_担当者
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.FILTERRENKETU, srSlipSlurrykokeibuntyouseiSutenyouki)));                   // ﾌｨﾙﾀｰ連結
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.FPJYUNBI_FILTERHINMEI1, srSlipSlurrykokeibuntyouseiSutenyouki)));          // F/P準備_ﾌｨﾙﾀｰ品名①
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B028Const.FPJYUNBI_LOTNO1, srSlipSlurrykokeibuntyouseiSutenyouki)));                      // F/P準備_LotNo①
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B028Const.FPJYUNBI_TORITUKEHONSUU1, srSlipSlurrykokeibuntyouseiSutenyouki)));                // F/P準備_取り付け本数①
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.FPJYUNBI_FILTERHINMEI2, srSlipSlurrykokeibuntyouseiSutenyouki)));          // F/P準備_ﾌｨﾙﾀｰ品名②
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B028Const.FPJYUNBI_LOTNO2, srSlipSlurrykokeibuntyouseiSutenyouki)));                      // F/P準備_LotNo②
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B028Const.FPJYUNBI_TORITUKEHONSUU2, srSlipSlurrykokeibuntyouseiSutenyouki)));                // F/P準備_取り付け本数②
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B028Const.ASSOUTANKNOSENJYOU, srSlipSlurrykokeibuntyouseiSutenyouki), 9));                       // 圧送ﾀﾝｸの洗浄
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B028Const.HAISYUTUYOUKINOUTIBUKURO, srSlipSlurrykokeibuntyouseiSutenyouki), 9));                       // 排出容器の内袋
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B028Const.FPTANKNO, srSlipSlurrykokeibuntyouseiSutenyouki)));                                // F/PﾀﾝｸNo
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B028Const.FPKAISI_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(fpkaisiTime) ? "0000" : fpkaisiTime));                                                                                                        // F/P開始日時
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B028Const.ASSOUREGULATORNO, srSlipSlurrykokeibuntyouseiSutenyouki)));                        // 圧送ﾚｷﾞｭﾚｰﾀｰNo
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B028Const.ASSOUATURYOKU, srSlipSlurrykokeibuntyouseiSutenyouki)));                    // 圧送圧力
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B028Const.FPKAISI_TANTOUSYA, srSlipSlurrykokeibuntyouseiSutenyouki)));                    // F/P開始_担当者
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B028Const.FPTEISI_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(fpteisiTime) ? "0000" : fpteisiTime));                                                                                                        // F/P停止日時
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.FPKOUKAN_FILTERHINMEI1, srSlipSlurrykokeibuntyouseiSutenyouki)));          // F/P交換_ﾌｨﾙﾀｰ品名①
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B028Const.FPKOUKAN_LOTNO1, srSlipSlurrykokeibuntyouseiSutenyouki)));                      // F/P交換_LotNo①
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B028Const.FPKOUKAN_TORITUKEHONSUU1, srSlipSlurrykokeibuntyouseiSutenyouki)));                // F/P交換_取り付け本数①
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B028Const.FPKOUKAN_FILTERHINMEI2, srSlipSlurrykokeibuntyouseiSutenyouki)));          // F/P交換_ﾌｨﾙﾀｰ品名②
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B028Const.FPKOUKAN_LOTNO2, srSlipSlurrykokeibuntyouseiSutenyouki)));                      // F/P交換_LotNo②
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B028Const.FPKOUKAN_TORITUKEHONSUU2, srSlipSlurrykokeibuntyouseiSutenyouki)));                // F/P交換_取り付け本数②
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B028Const.FPSAIKAI_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(fpsaikaiTime) ? "0000" : fpsaikaiTime));                                                                                                      // F/P再開日時
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B028Const.FPKOUKAN_TANTOUSYA, srSlipSlurrykokeibuntyouseiSutenyouki)));                   // F/P交換_担当者
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B028Const.FPSYUURYOU_DAY, srSlipSlurrykokeibuntyouseiSutenyouki),
                "".equals(fpsyuuryouTime) ? "0000" : fpsyuuryouTime));                                                                                                  // F/P終了日時
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B028Const.FPJIKAN, srSlipSlurrykokeibuntyouseiSutenyouki)));                                 // F/P時間
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B028Const.FPSYUUROU_TANTOUSYA, srSlipSlurrykokeibuntyouseiSutenyouki)));                  // F/P終了_担当者
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B028Const.SOUJYUURYOU, srSlipSlurrykokeibuntyouseiSutenyouki)));                  // 総重量
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B028Const.SYOUMIJYUURYOU, srSlipSlurrykokeibuntyouseiSutenyouki)));                   // 正味重量
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B028Const.BIKOU1, srSlipSlurrykokeibuntyouseiSutenyouki)));                               // 備考1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B028Const.BIKOU2, srSlipSlurrykokeibuntyouseiSutenyouki)));                               // 備考2

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
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)(sr_slip_slurrykokeibuntyousei_sutenyouki)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSrSlipSlurrykokeibuntyouseiSutenyouki(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sr_slip_slurrykokeibuntyousei_sutenyouki "
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
     * [ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_slip_slurrykokeibuntyousei_sutenyouki "
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
     * @param srSlipSlurrykokeibuntyouseiSutenyouki ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)データ
     * @return DB値
     */
    private String getSrSlipSlurrykokeibuntyouseiSutenyoukiItemData(String itemId, SrSlipSlurrykokeibuntyouseiSutenyouki srSlipSlurrykokeibuntyouseiSutenyouki) {
        switch (itemId) {
            // ｽﾘｯﾌﾟ品名
            case GXHDO102B028Const.SLIPHINMEI:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getSliphinmei());

            // ｽﾘｯﾌﾟLotNo
            case GXHDO102B028Const.SLIPLOTNO:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getSliplotno());

            // ﾛｯﾄ区分
            case GXHDO102B028Const.LOTKUBUN:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getLotkubun());

            // 原料記号
            case GXHDO102B028Const.GENRYOUKIGOU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getGenryoukigou());

            // 秤量号機
            case GXHDO102B028Const.GOKI:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getGoki());

            // ｽﾗﾘｰ品名
            case GXHDO102B028Const.SLURRYHINMEI:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getSlurryhinmei());

            // ｽﾗﾘｰLotNo
            case GXHDO102B028Const.SLURRYLOTNO:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getSlurrylotno());

            // 容器No
            case GXHDO102B028Const.YOUKINO:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getYoukino());

            // ｽﾗﾘｰ有効期限
            case GXHDO102B028Const.SLURRYYUUKOUKIGEN:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getSlurryyuukoukigen());

            // 乾燥固形分
            case GXHDO102B028Const.KANSOUKOKEIBUN:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getKansoukokeibun());

            // 脱脂固形分
            case GXHDO102B028Const.DASSIKOKEIBUN:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getDassikokeibun());

            // 粉砕終了日
            case GXHDO102B028Const.FUNSAISYUURYOU_DAY:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getFunsaisyuuryounichiji(), "yyMMdd");

            // 粉砕終了時間
            case GXHDO102B028Const.FUNSAISYUURYOU_TIME:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getFunsaisyuuryounichiji(), "HHmm");

            // ﾊﾞｲﾝﾀﾞｰ混合日
            case GXHDO102B028Const.BINDERKONGOU_DAY:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getBinderkongounichij(), "yyMMdd");

            // ﾊﾞｲﾝﾀﾞｰ混合時間
            case GXHDO102B028Const.BINDERKONGOU_TIME:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getBinderkongounichij(), "HHmm");

            // ｽﾗﾘｰｴｰｼﾞﾝｸﾞ
            case GXHDO102B028Const.SLURRAGING:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getSlurraging());

            // ｽﾗﾘｰ経過日数
            case GXHDO102B028Const.SLURRYKEIKANISUU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getSlurrykeikanisuu());

            // ｽﾘｯﾌﾟ情報_風袋重量
            case GXHDO102B028Const.SLIPJYOUHOU_FUUTAIJYUURYOU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getSlipjyouhou_fuutaijyuuryou());

            // 総重量
            case GXHDO102B028Const.SLURRYSOUJYUURYOU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getSlurrysoujyuuryou());

            // ｽﾗﾘｰ重量
            case GXHDO102B028Const.SLURRYJYUURYOU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getSlurryjyuuryou());

            // 撹拌設備
            case GXHDO102B028Const.KAKUHANSETUBI:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getKakuhansetubi());

            // 撹拌号機
            case GXHDO102B028Const.KAKUHANGOKI:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getKakuhangoki());

            // 撹拌回転数
            case GXHDO102B028Const.KAKUHANKAITENSUU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getKakuhankaitensuu());

            // 撹拌時間
            case GXHDO102B028Const.KAKUHANJIKAN:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getKakuhanjikan());

            // 撹拌開始日
            case GXHDO102B028Const.KAKUHANKAISI_DAY:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getKakuhankaisinichiji(), "yyMMdd");

            // 撹拌開始時間
            case GXHDO102B028Const.KAKUHANKAISI_TIME:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getKakuhankaisinichiji(), "HHmm");

            // 撹拌終了日
            case GXHDO102B028Const.KAKUHANSYUURYOU_DAY:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getKakuhansyuuryounichiji(), "yyMMdd");

            // 撹拌終了時間
            case GXHDO102B028Const.KAKUHANSYUURYOU_TIME:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getKakuhansyuuryounichiji(), "HHmm");

            // 脱脂皿の種類
            case GXHDO102B028Const.DASSIZARANOSYURUI:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getDassizaranosyurui());

            // 固形分測定_風袋重量
            case GXHDO102B028Const.KKBSKT_FUUTAIJYUURYOU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getKokeibunsokutei_fuutaijyuuryou());

            // 乾燥前ｽﾗﾘｰ重量
            case GXHDO102B028Const.KANSOUMAESLURRYJYUURYOU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getKansoumaeslurryjyuuryou());

            // 乾燥機
            case GXHDO102B028Const.KANSOUKI:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getKansouki());

            // 乾燥温度
            case GXHDO102B028Const.KANSOUONDO:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getKansouondo());

            // 乾燥時間
            case GXHDO102B028Const.KANSOUJIKAN:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getKansoujikan());

            // 乾燥開始日
            case GXHDO102B028Const.KANSOUKAISI_DAY:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getKansoukaisinichij(), "yyMMdd");

            // 乾燥開始時間
            case GXHDO102B028Const.KANSOUKAISI_TIME:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getKansoukaisinichij(), "HHmm");

            // 乾燥終了日
            case GXHDO102B028Const.KANSOUSYUURYOU_DAY:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getKansousyuuryounichiji(), "yyMMdd");

            // 乾燥終了時間
            case GXHDO102B028Const.KANSOUSYUURYOU_TIME:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getKansousyuuryounichiji(), "HHmm");

            // 乾燥後総重量
            case GXHDO102B028Const.KANSOUGOSOUJYUURYOU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getKansougosoujyuuryou());

            // 乾燥後正味重量
            case GXHDO102B028Const.KANSOUGOSYOUMIJYUURYOU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getKansougosyoumijyuuryou());

            // 固形分比率
            case GXHDO102B028Const.KOKEIBUNHIRITU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getKokeibunhiritu());

            // 固形分測定担当者
            case GXHDO102B028Const.KOKEIBUNSOKUTEITANTOUSYA:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getKokeibunsokuteitantousya());

            // 溶剤調整量
            case GXHDO102B028Const.YOUZAITYOUSEIRYOU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getYouzaityouseiryou());

            // ﾄﾙｴﾝ調整量
            case GXHDO102B028Const.TOLUENETYOUSEIRYOU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getToluenetyouseiryou());

            // ｿﾙﾐｯｸｽ調整量
            case GXHDO102B028Const.SOLMIXTYOUSEIRYOU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getSolmixtyouseiryou());

            // 溶剤秤量日
            case GXHDO102B028Const.YOUZAIKEIRYOU_DAY:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getYouzaikeiryounichiji(), "yyMMdd");

            // 溶剤秤量時間
            case GXHDO102B028Const.YOUZAIKEIRYOU_TIME:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getYouzaikeiryounichiji(), "HHmm");

            // 溶剤①_材料品名
            case GXHDO102B028Const.YOUZAI1_ZAIRYOUHINMEI:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getYouzai1_zairyouhinmei());

            // 溶剤①_調合量規格
            case GXHDO102B028Const.YOUZAI1_TYOUGOURYOUKIKAKU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getYouzai1_tyougouryoukikaku());

            // 溶剤①_部材在庫No1
            case GXHDO102B028Const.YOUZAI1_BUZAIZAIKOLOTNO1:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getYouzai1_buzaizaikolotno1());

            // 溶剤①_調合量1
            case GXHDO102B028Const.YOUZAI1_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getYouzai1_tyougouryou1());

            // 溶剤①_部材在庫No2
            case GXHDO102B028Const.YOUZAI1_BUZAIZAIKOLOTNO2:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getYouzai1_buzaizaikolotno2());

            // 溶剤①_調合量2
            case GXHDO102B028Const.YOUZAI1_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getYouzai1_tyougouryou2());

            // 溶剤②_材料品名
            case GXHDO102B028Const.YOUZAI2_ZAIRYOUHINMEI:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getYouzai2_zairyouhinmei());

            // 溶剤②_調合量規格
            case GXHDO102B028Const.YOUZAI2_TYOUGOURYOUKIKAKU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getYouzai2_tyougouryoukikaku());

            // 溶剤②_部材在庫No1
            case GXHDO102B028Const.YOUZAI2_BUZAIZAIKOLOTNO1:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getYouzai2_buzaizaikolotno1());

            // 溶剤②_調合量1
            case GXHDO102B028Const.YOUZAI2_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getYouzai2_tyougouryou1());

            // 溶剤②_部材在庫No2
            case GXHDO102B028Const.YOUZAI2_BUZAIZAIKOLOTNO2:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getYouzai2_buzaizaikolotno2());

            // 溶剤②_調合量2
            case GXHDO102B028Const.YOUZAI2_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getYouzai2_tyougouryou2());

            // 担当者
            case GXHDO102B028Const.TANTOUSYA:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getTantousya());

            // 2回目撹拌設備
            case GXHDO102B028Const.NIKAIMEKAKUHANSETUBI:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimekakuhansetubi());

            // 2回目撹拌時間
            case GXHDO102B028Const.NIKAIMEKAKUHANJIKAN:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimekakuhanjikan());

            // 2回目撹拌開始日
            case GXHDO102B028Const.NIKAIMEKAKUHANKAISI_DAY:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimekakuhankaisinichiji(), "yyMMdd");

            // 2回目撹拌開始時間
            case GXHDO102B028Const.NIKAIMEKAKUHANKAISI_TIME:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimekakuhankaisinichiji(), "HHmm");

            // 2回目撹拌終了日
            case GXHDO102B028Const.NKMKKHSR_DAY:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimekakuhansyuuryounichiji(), "yyMMdd");

            // 2回目撹拌終了時間
            case GXHDO102B028Const.NKMKKHSR_TIME:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimekakuhansyuuryounichiji(), "HHmm");

            // 2回目脱脂皿の種類
            case GXHDO102B028Const.NIKAIMEDASSIZARANOSYURUI:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimedassizaranosyurui());

            // 2回目ｱﾙﾐ皿風袋重量
            case GXHDO102B028Const.NKMARMZFTJR:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimearumizarafuutaijyuuryou());

            // 2回目乾燥前ｽﾗﾘｰ重量
            case GXHDO102B028Const.NKMKSMSLRJR:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimekansoumaeslurryjyuuryou());

            // 2回目乾燥機
            case GXHDO102B028Const.NIKAIMEKANSOUKI:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimekansouki());

            // 2回目乾燥温度
            case GXHDO102B028Const.NIKAIMEKANSOUONDO:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimekansouondo());

            // 2回目乾燥時間
            case GXHDO102B028Const.NIKAIMEKANSOUJIKAN:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimekansoujikan());

            // 2回目乾燥開始日
            case GXHDO102B028Const.NIKAIMEKANSOUKAISI_DAY:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimekansoukaisinichij(), "yyMMdd");

            // 2回目乾燥開始時間
            case GXHDO102B028Const.NIKAIMEKANSOUKAISI_TIME:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimekansoukaisinichij(), "HHmm");

            // 2回目乾燥終了日
            case GXHDO102B028Const.NIKAIMEKANSOUSYUURYOU_DAY:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimekansousyuuryounichiji(), "yyMMdd");

            // 2回目乾燥終了時間
            case GXHDO102B028Const.NIKAIMEKANSOUSYUURYOU_TIME:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimekansousyuuryounichiji(), "HHmm");

            // 2回目乾燥後総重量
            case GXHDO102B028Const.NIKAIMEKANSOUGOSOUJYUURYOU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimekansougosoujyuuryou());

            // 2回目乾燥後正味重量
            case GXHDO102B028Const.NKMKSGSMJR:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimekansougosyoumijyuuryou());

            // 2回目固形分比率
            case GXHDO102B028Const.NIKAIMEKOKEIBUNHIRITU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimekokeibunhiritu());

            // 2回目固形分測定担当者
            case GXHDO102B028Const.NKMKKBSKTTTS:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimekokeibunsokuteitantousya());

            // 2回目溶剤調整量
            case GXHDO102B028Const.NKMYZTSR:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimeyouzaityouseiryou());

            // 2回目ﾄﾙｴﾝ調整量
            case GXHDO102B028Const.NIKAIMETOLUENETYOUSEIRYOU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimetoluenetyouseiryou());

            // 2回目ｿﾙﾐｯｸｽ調整量
            case GXHDO102B028Const.NIKAIMESOLMIXTYOUSEIRYOU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimesolmixtyouseiryou());

            // 2回目溶剤秤量日
            case GXHDO102B028Const.NKMYZKEIRYOU_DAY:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimeyouzaikeiryounichiji(), "yyMMdd");

            // 2回目溶剤秤量時間
            case GXHDO102B028Const.NKMYZKEIRYOU_TIME:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimeyouzaikeiryounichiji(), "HHmm");

            // 2回目溶剤①_材料品名
            case GXHDO102B028Const.NKMYZ1_ZAIRYOUHINMEI:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimeyouzai1_zairyouhinmei());

            // 2回目溶剤①_調合量規格
            case GXHDO102B028Const.NKMYZ1_TYOUGOURYOUKIKAKU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimeyouzai1_tyougouryoukikaku());

            // 2回目溶剤①_部材在庫No1
            case GXHDO102B028Const.NKMYZ1_BUZAIZAIKOLOTNO1:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimeyouzai1_buzaizaikolotno1());

            // 2回目溶剤①_調合量1
            case GXHDO102B028Const.NKMYZ1_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimeyouzai1_tyougouryou1());

            // 2回目溶剤①_部材在庫No2
            case GXHDO102B028Const.NKMYZ1_BUZAIZAIKOLOTNO2:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimeyouzai1_buzaizaikolotno2());

            // 2回目溶剤①_調合量2
            case GXHDO102B028Const.NKMYZ1_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimeyouzai1_tyougouryou2());

            // 2回目溶剤②_材料品名
            case GXHDO102B028Const.NKMYZ2_ZAIRYOUHINMEI:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimeyouzai2_zairyouhinmei());

            // 2回目溶剤②_調合量規格
            case GXHDO102B028Const.NKMYZ2_TYOUGOURYOUKIKAKU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimeyouzai2_tyougouryoukikaku());

            // 2回目溶剤②_部材在庫No1
            case GXHDO102B028Const.NKMYZ2_BUZAIZAIKOLOTNO1:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimeyouzai2_buzaizaikolotno1());

            // 2回目溶剤②_調合量1
            case GXHDO102B028Const.NKMYZ2_TYOUGOURYOU1:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimeyouzai2_tyougouryou1());

            // 2回目溶剤②_部材在庫No2
            case GXHDO102B028Const.NKMYZ2_BUZAIZAIKOLOTNO2:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimeyouzai2_buzaizaikolotno2());

            // 2回目溶剤②_調合量2
            case GXHDO102B028Const.NKMYZ2_TYOUGOURYOU2:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimeyouzai2_tyougouryou2());

            // 2回目担当者
            case GXHDO102B028Const.NIKAIMETANTOUSYA:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getNikaimetantousya());

            // 3回目撹拌設備
            case GXHDO102B028Const.SANKAIMEKAKUHANSETUBI:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getSankaimekakuhansetubi());

            // 3回目撹拌時間
            case GXHDO102B028Const.SANKAIMEKAKUHANJIKAN:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getSankaimekakuhanjikan());

            // 3回目撹拌開始日
            case GXHDO102B028Const.SANKAIMEKAKUHANKAISI_DAY:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getSankaimekakuhankaisinichiji(), "yyMMdd");

            // 3回目撹拌開始時間
            case GXHDO102B028Const.SANKAIMEKAKUHANKAISI_TIME:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getSankaimekakuhankaisinichiji(), "HHmm");

            // 3回目撹拌終了日
            case GXHDO102B028Const.SKMKKHSR_DAY:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getSankaimekakuhansyuuryounichiji(), "yyMMdd");

            // 3回目撹拌終了時間
            case GXHDO102B028Const.SKMKKHSR_TIME:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getSankaimekakuhansyuuryounichiji(), "HHmm");

            // 3回目脱脂皿の種類
            case GXHDO102B028Const.SANKAIMEDASSIZARANOSYURUI:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getSankaimedassizaranosyurui());

            // 3回目ｱﾙﾐ皿風袋重量
            case GXHDO102B028Const.SKMARMZFTJR:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getSankaimearumizarafuutaijyuuryou());

            // 3回目乾燥前ｽﾗﾘｰ重量
            case GXHDO102B028Const.SKMKSMSLRJR:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getSankaimekansoumaeslurryjyuuryou());

            // 3回目乾燥機
            case GXHDO102B028Const.SANKAIMEKANSOUKI:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getSankaimekansouki());

            // 3回目乾燥温度
            case GXHDO102B028Const.SANKAIMEKANSOUONDO:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getSankaimekansouondo());

            // 3回目乾燥時間
            case GXHDO102B028Const.SANKAIMEKANSOUJIKAN:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getSankaimekansoujikan());

            // 3回目乾燥開始日
            case GXHDO102B028Const.SANKAIMEKANSOUKAISI_DAY:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getSankaimekansoukaisinichij(), "yyMMdd");

            // 3回目乾燥開始時間
            case GXHDO102B028Const.SANKAIMEKANSOUKAISI_TIME:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getSankaimekansoukaisinichij(), "HHmm");

            // 3回目乾燥終了日
            case GXHDO102B028Const.SANKAIMEKANSOUSYUURYOU_DAY:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getSankaimekansousyuuryounichiji(), "yyMMdd");

            // 3回目乾燥終了時間
            case GXHDO102B028Const.SANKAIMEKANSOUSYUURYOU_TIME:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getSankaimekansousyuuryounichiji(), "HHmm");

            // 3回目乾燥後総重量
            case GXHDO102B028Const.SANKAIMEKANSOUGOSOUJYUURYOU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getSankaimekansougosoujyuuryou());

            // 3回目乾燥後正味重量
            case GXHDO102B028Const.SKMKSGSMJR:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getSankaimekansougosyoumijyuuryou());

            // 3回目固形分比率
            case GXHDO102B028Const.SANKAIMEKOKEIBUNHIRITU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getSankaimekokeibunhiritu());

            // 3回目固形分測定担当者
            case GXHDO102B028Const.SKMKKBSKTTTS:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getSankaimekokeibunsokuteitantousya());

            // F/P準備_風袋重量
            case GXHDO102B028Const.FPJYUNBI_FUUTAIJYUURYOU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getFpjyunbi_fuutaijyuuryou());

            // F/P準備_担当者
            case GXHDO102B028Const.FPJYUNBI_TANTOUSYA:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getFpjyunbi_tantousya());

            // ﾌｨﾙﾀｰ連結
            case GXHDO102B028Const.FILTERRENKETU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getFilterrenketu());

            // F/P準備_ﾌｨﾙﾀｰ品名①
            case GXHDO102B028Const.FPJYUNBI_FILTERHINMEI1:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getFpjyunbi_filterhinmei1());

            // F/P準備_LotNo①
            case GXHDO102B028Const.FPJYUNBI_LOTNO1:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getFpjyunbi_lotno1());

            // F/P準備_取り付け本数①
            case GXHDO102B028Const.FPJYUNBI_TORITUKEHONSUU1:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getFpjyunbi_toritukehonsuu1());

            // F/P準備_ﾌｨﾙﾀｰ品名②
            case GXHDO102B028Const.FPJYUNBI_FILTERHINMEI2:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getFpjyunbi_filterhinmei2());

            // F/P準備_LotNo②
            case GXHDO102B028Const.FPJYUNBI_LOTNO2:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getFpjyunbi_lotno2());

            // F/P準備_取り付け本数②
            case GXHDO102B028Const.FPJYUNBI_TORITUKEHONSUU2:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getFpjyunbi_toritukehonsuu2());

            // 圧送ﾀﾝｸの洗浄
            case GXHDO102B028Const.ASSOUTANKNOSENJYOU:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getAssoutanknosenjyou()));

            // 排出容器の内袋
            case GXHDO102B028Const.HAISYUTUYOUKINOUTIBUKURO:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getHaisyutuyoukinoutibukuro()));

            // F/PﾀﾝｸNo
            case GXHDO102B028Const.FPTANKNO:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getFptankno());

            // F/P開始日
            case GXHDO102B028Const.FPKAISI_DAY:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getFpkaisinichiji(), "yyMMdd");

            // F/P開始時間
            case GXHDO102B028Const.FPKAISI_TIME:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getFpkaisinichiji(), "HHmm");

            // 圧送ﾚｷﾞｭﾚｰﾀｰNo
            case GXHDO102B028Const.ASSOUREGULATORNO:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getAssouregulatorno());

            // 圧送圧力
            case GXHDO102B028Const.ASSOUATURYOKU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getAssouaturyoku());

            // F/P開始_担当者
            case GXHDO102B028Const.FPKAISI_TANTOUSYA:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getFpkaisi_tantousya());

            // F/P停止日
            case GXHDO102B028Const.FPTEISI_DAY:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getFpteisinichiji(), "yyMMdd");

            // F/P停止時間
            case GXHDO102B028Const.FPTEISI_TIME:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getFpteisinichiji(), "HHmm");

            // F/P交換_ﾌｨﾙﾀｰ品名①
            case GXHDO102B028Const.FPKOUKAN_FILTERHINMEI1:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getFpkoukan_filterhinmei1());

            // F/P交換_LotNo①
            case GXHDO102B028Const.FPKOUKAN_LOTNO1:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getFpkoukan_lotno1());

            // F/P交換_取り付け本数①
            case GXHDO102B028Const.FPKOUKAN_TORITUKEHONSUU1:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getFpkoukan_toritukehonsuu1());

            // F/P交換_ﾌｨﾙﾀｰ品名②
            case GXHDO102B028Const.FPKOUKAN_FILTERHINMEI2:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getFpkoukan_filterhinmei2());

            // F/P交換_LotNo②
            case GXHDO102B028Const.FPKOUKAN_LOTNO2:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getFpkoukan_lotno2());

            // F/P交換_取り付け本数②
            case GXHDO102B028Const.FPKOUKAN_TORITUKEHONSUU2:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getFpkoukan_toritukehonsuu2());

            // F/P再開日
            case GXHDO102B028Const.FPSAIKAI_DAY:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getFpsaikainichiji(), "yyMMdd");

            // F/P再開時間
            case GXHDO102B028Const.FPSAIKAI_TIME:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getFpsaikainichiji(), "HHmm");

            // F/P交換_担当者
            case GXHDO102B028Const.FPKOUKAN_TANTOUSYA:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getFpkoukan_tantousya());

            // F/P終了日
            case GXHDO102B028Const.FPSYUURYOU_DAY:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getFpsyuuryounichiji(), "yyMMdd");

            // F/P終了時間
            case GXHDO102B028Const.FPSYUURYOU_TIME:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSutenyouki.getFpsyuuryounichiji(), "HHmm");

            // F/P時間
            case GXHDO102B028Const.FPJIKAN:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getFpjikan());

            // F/P終了_担当者
            case GXHDO102B028Const.FPSYUUROU_TANTOUSYA:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getFpsyuurou_tantousya());

            // 総重量
            case GXHDO102B028Const.SOUJYUURYOU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getSoujyuuryou());

            // 正味重量
            case GXHDO102B028Const.SYOUMIJYUURYOU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getSyoumijyuuryou());

            // 備考1
            case GXHDO102B028Const.BIKOU1:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getBikou1());

            // 備考2
            case GXHDO102B028Const.BIKOU2:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSutenyouki.getBikou2());

            default:
                return null;
        }
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)_仮登録(tmp_sr_slip_slurrykokeibuntyousei_sutenyouki)登録処理(削除時)
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
    private void insertDeleteDataTmpSrSlipSlurrykokeibuntyouseiSutenyouki(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_slip_slurrykokeibuntyousei_sutenyouki ("
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,goki,slurryhinmei,slurrylotno,youkino,slurryyuukoukigen,kansoukokeibun,dassikokeibun,"
                + "funsaisyuuryounichiji,binderkongounichij,slurraging,slurrykeikanisuu,slipjyouhou_fuutaijyuuryou,slurrysoujyuuryou,slurryjyuuryou,kakuhansetubi,kakuhangoki,"
                + "kakuhankaitensuu,kakuhanjikan,kakuhankaisinichiji,kakuhansyuuryounichiji,dassizaranosyurui,kokeibunsokutei_fuutaijyuuryou,kansoumaeslurryjyuuryou,kansouki,"
                + "kansouondo,kansoujikan,kansoukaisinichij,kansousyuuryounichiji,kansougosoujyuuryou,kansougosyoumijyuuryou,kokeibunhiritu,kokeibunsokuteitantousya,"
                + "youzaityouseiryou,toluenetyouseiryou,solmixtyouseiryou,youzaikeiryounichiji,youzai1_zairyouhinmei,youzai1_tyougouryoukikaku,youzai1_buzaizaikolotno1,"
                + "youzai1_tyougouryou1,youzai1_buzaizaikolotno2,youzai1_tyougouryou2,youzai2_zairyouhinmei,youzai2_tyougouryoukikaku,youzai2_buzaizaikolotno1,youzai2_tyougouryou1,"
                + "youzai2_buzaizaikolotno2,youzai2_tyougouryou2,tantousya,2kaimekakuhansetubi,2kaimekakuhanjikan,2kaimekakuhankaisinichiji,2kaimekakuhansyuuryounichiji,"
                + "2kaimedassizaranosyurui,2kaimearumizarafuutaijyuuryou,2kaimekansoumaeslurryjyuuryou,2kaimekansouki,2kaimekansouondo,2kaimekansoujikan,2kaimekansoukaisinichij,"
                + "2kaimekansousyuuryounichiji,2kaimekansougosoujyuuryou,2kaimekansougosyoumijyuuryou,2kaimekokeibunhiritu,2kaimekokeibunsokuteitantousya,2kaimeyouzaityouseiryou,"
                + "2kaimetoluenetyouseiryou,2kaimesolmixtyouseiryou,2kaimeyouzaikeiryounichiji,2kaimeyouzai1_zairyouhinmei,2kaimeyouzai1_tyougouryoukikaku,2kaimeyouzai1_buzaizaikolotno1,"
                + "2kaimeyouzai1_tyougouryou1,2kaimeyouzai1_buzaizaikolotno2,2kaimeyouzai1_tyougouryou2,2kaimeyouzai2_zairyouhinmei,2kaimeyouzai2_tyougouryoukikaku,"
                + "2kaimeyouzai2_buzaizaikolotno1,2kaimeyouzai2_tyougouryou1,2kaimeyouzai2_buzaizaikolotno2,2kaimeyouzai2_tyougouryou2,2kaimetantousya,3kaimekakuhansetubi,3kaimekakuhanjikan,"
                + "3kaimekakuhankaisinichiji,3kaimekakuhansyuuryounichiji,3kaimedassizaranosyurui,3kaimearumizarafuutaijyuuryou,3kaimekansoumaeslurryjyuuryou,3kaimekansouki,"
                + "3kaimekansouondo,3kaimekansoujikan,3kaimekansoukaisinichij,3kaimekansousyuuryounichiji,3kaimekansougosoujyuuryou,3kaimekansougosyoumijyuuryou,3kaimekokeibunhiritu,"
                + "3kaimekokeibunsokuteitantousya,FPjyunbi_fuutaijyuuryou,FPjyunbi_tantousya,filterrenketu,FPjyunbi_filterhinmei1,FPjyunbi_lotno1,FPjyunbi_toritukehonsuu1,FPjyunbi_filterhinmei2,"
                + "FPjyunbi_lotno2,FPjyunbi_toritukehonsuu2,assoutanknosenjyou,haisyutuyoukinoutibukuro,Fptankno,Fpkaisinichiji,assouregulatorNo,assouaturyoku,FPkaisi_tantousya,Fpteisinichiji,"
                + "FPkoukan_filterhinmei1,FPkoukan_lotno1,FPkoukan_toritukehonsuu1,FPkoukan_filterhinmei2,FPkoukan_lotno2,FPkoukan_toritukehonsuu2,FPsaikainichiji,FPkoukan_tantousya,"
                + "FPsyuuryounichiji,FPjikan,FPsyuurou_tantousya,soujyuuryou,syoumijyuuryou,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") SELECT "
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,goki,slurryhinmei,slurrylotno,youkino,slurryyuukoukigen,kansoukokeibun,dassikokeibun,"
                + "funsaisyuuryounichiji,binderkongounichij,slurraging,slurrykeikanisuu,slipjyouhou_fuutaijyuuryou,slurrysoujyuuryou,slurryjyuuryou,kakuhansetubi,kakuhangoki,"
                + "kakuhankaitensuu,kakuhanjikan,kakuhankaisinichiji,kakuhansyuuryounichiji,dassizaranosyurui,kokeibunsokutei_fuutaijyuuryou,kansoumaeslurryjyuuryou,kansouki,"
                + "kansouondo,kansoujikan,kansoukaisinichij,kansousyuuryounichiji,kansougosoujyuuryou,kansougosyoumijyuuryou,kokeibunhiritu,kokeibunsokuteitantousya,"
                + "youzaityouseiryou,toluenetyouseiryou,solmixtyouseiryou,youzaikeiryounichiji,youzai1_zairyouhinmei,youzai1_tyougouryoukikaku,youzai1_buzaizaikolotno1,"
                + "youzai1_tyougouryou1,youzai1_buzaizaikolotno2,youzai1_tyougouryou2,youzai2_zairyouhinmei,youzai2_tyougouryoukikaku,youzai2_buzaizaikolotno1,youzai2_tyougouryou1,"
                + "youzai2_buzaizaikolotno2,youzai2_tyougouryou2,tantousya,2kaimekakuhansetubi,2kaimekakuhanjikan,2kaimekakuhankaisinichiji,2kaimekakuhansyuuryounichiji,"
                + "2kaimedassizaranosyurui,2kaimearumizarafuutaijyuuryou,2kaimekansoumaeslurryjyuuryou,2kaimekansouki,2kaimekansouondo,2kaimekansoujikan,2kaimekansoukaisinichij,"
                + "2kaimekansousyuuryounichiji,2kaimekansougosoujyuuryou,2kaimekansougosyoumijyuuryou,2kaimekokeibunhiritu,2kaimekokeibunsokuteitantousya,2kaimeyouzaityouseiryou,"
                + "2kaimetoluenetyouseiryou,2kaimesolmixtyouseiryou,2kaimeyouzaikeiryounichiji,2kaimeyouzai1_zairyouhinmei,2kaimeyouzai1_tyougouryoukikaku,2kaimeyouzai1_buzaizaikolotno1,"
                + "2kaimeyouzai1_tyougouryou1,2kaimeyouzai1_buzaizaikolotno2,2kaimeyouzai1_tyougouryou2,2kaimeyouzai2_zairyouhinmei,2kaimeyouzai2_tyougouryoukikaku,"
                + "2kaimeyouzai2_buzaizaikolotno1,2kaimeyouzai2_tyougouryou1,2kaimeyouzai2_buzaizaikolotno2,2kaimeyouzai2_tyougouryou2,2kaimetantousya,3kaimekakuhansetubi,3kaimekakuhanjikan,"
                + "3kaimekakuhankaisinichiji,3kaimekakuhansyuuryounichiji,3kaimedassizaranosyurui,3kaimearumizarafuutaijyuuryou,3kaimekansoumaeslurryjyuuryou,3kaimekansouki,"
                + "3kaimekansouondo,3kaimekansoujikan,3kaimekansoukaisinichij,3kaimekansousyuuryounichiji,3kaimekansougosoujyuuryou,3kaimekansougosyoumijyuuryou,3kaimekokeibunhiritu,"
                + "3kaimekokeibunsokuteitantousya,FPjyunbi_fuutaijyuuryou,FPjyunbi_tantousya,filterrenketu,FPjyunbi_filterhinmei1,FPjyunbi_lotno1,FPjyunbi_toritukehonsuu1,FPjyunbi_filterhinmei2,"
                + "FPjyunbi_lotno2,FPjyunbi_toritukehonsuu2,assoutanknosenjyou,haisyutuyoukinoutibukuro,Fptankno,Fpkaisinichiji,assouregulatorNo,assouaturyoku,FPkaisi_tantousya,Fpteisinichiji,"
                + "FPkoukan_filterhinmei1,FPkoukan_lotno1,FPkoukan_toritukehonsuu1,FPkoukan_filterhinmei2,FPkoukan_lotno2,FPkoukan_toritukehonsuu2,FPsaikainichiji,FPkoukan_tantousya,"
                + "FPsyuuryounichiji,FPjikan,FPsyuurou_tantousya,soujyuuryou,syoumijyuuryou,bikou1,bikou2,?,?,?,? "
                + " FROM sr_slip_slurrykokeibuntyousei_sutenyouki "
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
    private void initGXHDO102B028A(ProcessData processData) {
        GXHDO102B028A bean = (GXHDO102B028A) getFormBean("gXHDO102B028A");
        bean.setWiplotno(getItemRow(processData.getItemList(), GXHDO102B028Const.WIPLOTNO));
        bean.setSliphinmei(getItemRow(processData.getItemList(), GXHDO102B028Const.SLIPHINMEI));
        bean.setSliplotno(getItemRow(processData.getItemList(), GXHDO102B028Const.SLIPLOTNO));
        bean.setLotkubun(getItemRow(processData.getItemList(), GXHDO102B028Const.LOTKUBUN));
        bean.setGenryoukigou(getItemRow(processData.getItemList(), GXHDO102B028Const.GENRYOUKIGOU));
        bean.setGoki(getItemRow(processData.getItemList(), GXHDO102B028Const.GOKI));
        bean.setSlurryhinmei(getItemRow(processData.getItemList(), GXHDO102B028Const.SLURRYHINMEI));
        bean.setSlurrylotno(getItemRow(processData.getItemList(), GXHDO102B028Const.SLURRYLOTNO));
        bean.setYoukino(getItemRow(processData.getItemList(), GXHDO102B028Const.YOUKINO));
        bean.setSlurryyuukoukigen(getItemRow(processData.getItemList(), GXHDO102B028Const.SLURRYYUUKOUKIGEN));
        bean.setKansoukokeibun(getItemRow(processData.getItemList(), GXHDO102B028Const.KANSOUKOKEIBUN));
        bean.setDassikokeibun(getItemRow(processData.getItemList(), GXHDO102B028Const.DASSIKOKEIBUN));
        bean.setFunsaisyuuryou_day(getItemRow(processData.getItemList(), GXHDO102B028Const.FUNSAISYUURYOU_DAY));
        bean.setFunsaisyuuryou_time(getItemRow(processData.getItemList(), GXHDO102B028Const.FUNSAISYUURYOU_TIME));
        bean.setBinderkongou_day(getItemRow(processData.getItemList(), GXHDO102B028Const.BINDERKONGOU_DAY));
        bean.setBinderkongou_time(getItemRow(processData.getItemList(), GXHDO102B028Const.BINDERKONGOU_TIME));
        bean.setSlurraging(getItemRow(processData.getItemList(), GXHDO102B028Const.SLURRAGING));
        bean.setSlurrykeikanisuu(getItemRow(processData.getItemList(), GXHDO102B028Const.SLURRYKEIKANISUU));
        bean.setSlipjyouhou_fuutaijyuuryou(getItemRow(processData.getItemList(), GXHDO102B028Const.SLIPJYOUHOU_FUUTAIJYUURYOU));
        bean.setSlurrysoujyuuryou(getItemRow(processData.getItemList(), GXHDO102B028Const.SLURRYSOUJYUURYOU));
        bean.setSlurryjyuuryou(getItemRow(processData.getItemList(), GXHDO102B028Const.SLURRYJYUURYOU));
        bean.setKakuhansetubi(getItemRow(processData.getItemList(), GXHDO102B028Const.KAKUHANSETUBI));
        bean.setKakuhangoki(getItemRow(processData.getItemList(), GXHDO102B028Const.KAKUHANGOKI));
        bean.setKakuhankaitensuu(getItemRow(processData.getItemList(), GXHDO102B028Const.KAKUHANKAITENSUU));
        bean.setKakuhanjikan(getItemRow(processData.getItemList(), GXHDO102B028Const.KAKUHANJIKAN));
        bean.setKakuhankaisi_day(getItemRow(processData.getItemList(), GXHDO102B028Const.KAKUHANKAISI_DAY));
        bean.setKakuhankaisi_time(getItemRow(processData.getItemList(), GXHDO102B028Const.KAKUHANKAISI_TIME));
        bean.setKakuhansyuuryou_day(getItemRow(processData.getItemList(), GXHDO102B028Const.KAKUHANSYUURYOU_DAY));
        bean.setKakuhansyuuryou_time(getItemRow(processData.getItemList(), GXHDO102B028Const.KAKUHANSYUURYOU_TIME));
        bean.setDassizaranosyurui(getItemRow(processData.getItemList(), GXHDO102B028Const.DASSIZARANOSYURUI));
        bean.setKkbskt_fuutaijyuuryou(getItemRow(processData.getItemList(), GXHDO102B028Const.KKBSKT_FUUTAIJYUURYOU));
        bean.setKansoumaeslurryjyuuryou(getItemRow(processData.getItemList(), GXHDO102B028Const.KANSOUMAESLURRYJYUURYOU));
        bean.setKansouki(getItemRow(processData.getItemList(), GXHDO102B028Const.KANSOUKI));
        bean.setKansouondo(getItemRow(processData.getItemList(), GXHDO102B028Const.KANSOUONDO));
        bean.setKansoujikan(getItemRow(processData.getItemList(), GXHDO102B028Const.KANSOUJIKAN));
        bean.setKansoukaisi_day(getItemRow(processData.getItemList(), GXHDO102B028Const.KANSOUKAISI_DAY));
        bean.setKansoukaisi_time(getItemRow(processData.getItemList(), GXHDO102B028Const.KANSOUKAISI_TIME));
        bean.setKansousyuuryou_day(getItemRow(processData.getItemList(), GXHDO102B028Const.KANSOUSYUURYOU_DAY));
        bean.setKansousyuuryou_time(getItemRow(processData.getItemList(), GXHDO102B028Const.KANSOUSYUURYOU_TIME));
        bean.setKansougosoujyuuryou(getItemRow(processData.getItemList(), GXHDO102B028Const.KANSOUGOSOUJYUURYOU));
        bean.setKansougosyoumijyuuryou(getItemRow(processData.getItemList(), GXHDO102B028Const.KANSOUGOSYOUMIJYUURYOU));
        bean.setKokeibunhiritu(getItemRow(processData.getItemList(), GXHDO102B028Const.KOKEIBUNHIRITU));
        bean.setKokeibunsokuteitantousya(getItemRow(processData.getItemList(), GXHDO102B028Const.KOKEIBUNSOKUTEITANTOUSYA));
        bean.setYouzaityouseiryou(getItemRow(processData.getItemList(), GXHDO102B028Const.YOUZAITYOUSEIRYOU));
        bean.setToluenetyouseiryou(getItemRow(processData.getItemList(), GXHDO102B028Const.TOLUENETYOUSEIRYOU));
        bean.setSolmixtyouseiryou(getItemRow(processData.getItemList(), GXHDO102B028Const.SOLMIXTYOUSEIRYOU));
        bean.setYouzaikeiryou_day(getItemRow(processData.getItemList(), GXHDO102B028Const.YOUZAIKEIRYOU_DAY));
        bean.setYouzaikeiryou_time(getItemRow(processData.getItemList(), GXHDO102B028Const.YOUZAIKEIRYOU_TIME));
        bean.setYouzai1_zairyouhinmei(getItemRow(processData.getItemList(), GXHDO102B028Const.YOUZAI1_ZAIRYOUHINMEI));
        bean.setYouzai1_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B028Const.YOUZAI1_TYOUGOURYOUKIKAKU));
        bean.setYouzai1_buzaizaikolotno1(getItemRow(processData.getItemList(), GXHDO102B028Const.YOUZAI1_BUZAIZAIKOLOTNO1));
        bean.setYouzai1_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B028Const.YOUZAI1_TYOUGOURYOU1));
        bean.setYouzai1_buzaizaikolotno2(getItemRow(processData.getItemList(), GXHDO102B028Const.YOUZAI1_BUZAIZAIKOLOTNO2));
        bean.setYouzai1_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B028Const.YOUZAI1_TYOUGOURYOU2));
        bean.setYouzai2_zairyouhinmei(getItemRow(processData.getItemList(), GXHDO102B028Const.YOUZAI2_ZAIRYOUHINMEI));
        bean.setYouzai2_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B028Const.YOUZAI2_TYOUGOURYOUKIKAKU));
        bean.setYouzai2_buzaizaikolotno1(getItemRow(processData.getItemList(), GXHDO102B028Const.YOUZAI2_BUZAIZAIKOLOTNO1));
        bean.setYouzai2_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B028Const.YOUZAI2_TYOUGOURYOU1));
        bean.setYouzai2_buzaizaikolotno2(getItemRow(processData.getItemList(), GXHDO102B028Const.YOUZAI2_BUZAIZAIKOLOTNO2));
        bean.setYouzai2_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B028Const.YOUZAI2_TYOUGOURYOU2));
        bean.setTantousya(getItemRow(processData.getItemList(), GXHDO102B028Const.TANTOUSYA));
        bean.setNikaimekakuhansetubi(getItemRow(processData.getItemList(), GXHDO102B028Const.NIKAIMEKAKUHANSETUBI));
        bean.setNikaimekakuhanjikan(getItemRow(processData.getItemList(), GXHDO102B028Const.NIKAIMEKAKUHANJIKAN));
        bean.setNikaimekakuhankaisi_day(getItemRow(processData.getItemList(), GXHDO102B028Const.NIKAIMEKAKUHANKAISI_DAY));
        bean.setNikaimekakuhankaisi_time(getItemRow(processData.getItemList(), GXHDO102B028Const.NIKAIMEKAKUHANKAISI_TIME));
        bean.setNkmkkhsr_day(getItemRow(processData.getItemList(), GXHDO102B028Const.NKMKKHSR_DAY));
        bean.setNkmkkhsr_time(getItemRow(processData.getItemList(), GXHDO102B028Const.NKMKKHSR_TIME));
        bean.setNikaimedassizaranosyurui(getItemRow(processData.getItemList(), GXHDO102B028Const.NIKAIMEDASSIZARANOSYURUI));
        bean.setNkmarmzftjr(getItemRow(processData.getItemList(), GXHDO102B028Const.NKMARMZFTJR));
        bean.setNkmksmslrjr(getItemRow(processData.getItemList(), GXHDO102B028Const.NKMKSMSLRJR));
        bean.setNikaimekansouki(getItemRow(processData.getItemList(), GXHDO102B028Const.NIKAIMEKANSOUKI));
        bean.setNikaimekansouondo(getItemRow(processData.getItemList(), GXHDO102B028Const.NIKAIMEKANSOUONDO));
        bean.setNikaimekansoujikan(getItemRow(processData.getItemList(), GXHDO102B028Const.NIKAIMEKANSOUJIKAN));
        bean.setNikaimekansoukaisi_day(getItemRow(processData.getItemList(), GXHDO102B028Const.NIKAIMEKANSOUKAISI_DAY));
        bean.setNikaimekansoukaisi_time(getItemRow(processData.getItemList(), GXHDO102B028Const.NIKAIMEKANSOUKAISI_TIME));
        bean.setNikaimekansousyuuryou_day(getItemRow(processData.getItemList(), GXHDO102B028Const.NIKAIMEKANSOUSYUURYOU_DAY));
        bean.setNikaimekansousyuuryou_time(getItemRow(processData.getItemList(), GXHDO102B028Const.NIKAIMEKANSOUSYUURYOU_TIME));
        bean.setNikaimekansougosoujyuuryou(getItemRow(processData.getItemList(), GXHDO102B028Const.NIKAIMEKANSOUGOSOUJYUURYOU));
        bean.setNkmksgsmjr(getItemRow(processData.getItemList(), GXHDO102B028Const.NKMKSGSMJR));
        bean.setNikaimekokeibunhiritu(getItemRow(processData.getItemList(), GXHDO102B028Const.NIKAIMEKOKEIBUNHIRITU));
        bean.setNkmkkbskttts(getItemRow(processData.getItemList(), GXHDO102B028Const.NKMKKBSKTTTS));
        bean.setNkmyztsr(getItemRow(processData.getItemList(), GXHDO102B028Const.NKMYZTSR));
        bean.setNikaimetoluenetyouseiryou(getItemRow(processData.getItemList(), GXHDO102B028Const.NIKAIMETOLUENETYOUSEIRYOU));
        bean.setNikaimesolmixtyouseiryou(getItemRow(processData.getItemList(), GXHDO102B028Const.NIKAIMESOLMIXTYOUSEIRYOU));
        bean.setNkmyzkeiryou_day(getItemRow(processData.getItemList(), GXHDO102B028Const.NKMYZKEIRYOU_DAY));
        bean.setNkmyzkeiryou_time(getItemRow(processData.getItemList(), GXHDO102B028Const.NKMYZKEIRYOU_TIME));
        bean.setNkmyz1_zairyouhinmei(getItemRow(processData.getItemList(), GXHDO102B028Const.NKMYZ1_ZAIRYOUHINMEI));
        bean.setNkmyz1_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B028Const.NKMYZ1_TYOUGOURYOUKIKAKU));
        bean.setNkmyz1_buzaizaikolotno1(getItemRow(processData.getItemList(), GXHDO102B028Const.NKMYZ1_BUZAIZAIKOLOTNO1));
        bean.setNkmyz1_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B028Const.NKMYZ1_TYOUGOURYOU1));
        bean.setNkmyz1_buzaizaikolotno2(getItemRow(processData.getItemList(), GXHDO102B028Const.NKMYZ1_BUZAIZAIKOLOTNO2));
        bean.setNkmyz1_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B028Const.NKMYZ1_TYOUGOURYOU2));
        bean.setNkmyz2_zairyouhinmei(getItemRow(processData.getItemList(), GXHDO102B028Const.NKMYZ2_ZAIRYOUHINMEI));
        bean.setNkmyz2_tyougouryoukikaku(getItemRow(processData.getItemList(), GXHDO102B028Const.NKMYZ2_TYOUGOURYOUKIKAKU));
        bean.setNkmyz2_buzaizaikolotno1(getItemRow(processData.getItemList(), GXHDO102B028Const.NKMYZ2_BUZAIZAIKOLOTNO1));
        bean.setNkmyz2_tyougouryou1(getItemRow(processData.getItemList(), GXHDO102B028Const.NKMYZ2_TYOUGOURYOU1));
        bean.setNkmyz2_buzaizaikolotno2(getItemRow(processData.getItemList(), GXHDO102B028Const.NKMYZ2_BUZAIZAIKOLOTNO2));
        bean.setNkmyz2_tyougouryou2(getItemRow(processData.getItemList(), GXHDO102B028Const.NKMYZ2_TYOUGOURYOU2));
        bean.setNikaimetantousya(getItemRow(processData.getItemList(), GXHDO102B028Const.NIKAIMETANTOUSYA));
        bean.setSankaimekakuhansetubi(getItemRow(processData.getItemList(), GXHDO102B028Const.SANKAIMEKAKUHANSETUBI));
        bean.setSankaimekakuhanjikan(getItemRow(processData.getItemList(), GXHDO102B028Const.SANKAIMEKAKUHANJIKAN));
        bean.setSankaimekakuhankaisi_day(getItemRow(processData.getItemList(), GXHDO102B028Const.SANKAIMEKAKUHANKAISI_DAY));
        bean.setSankaimekakuhankaisi_time(getItemRow(processData.getItemList(), GXHDO102B028Const.SANKAIMEKAKUHANKAISI_TIME));
        bean.setSkmkkhsr_day(getItemRow(processData.getItemList(), GXHDO102B028Const.SKMKKHSR_DAY));
        bean.setSkmkkhsr_time(getItemRow(processData.getItemList(), GXHDO102B028Const.SKMKKHSR_TIME));
        bean.setSankaimedassizaranosyurui(getItemRow(processData.getItemList(), GXHDO102B028Const.SANKAIMEDASSIZARANOSYURUI));
        bean.setSkmarmzftjr(getItemRow(processData.getItemList(), GXHDO102B028Const.SKMARMZFTJR));
        bean.setSkmksmslrjr(getItemRow(processData.getItemList(), GXHDO102B028Const.SKMKSMSLRJR));
        bean.setSankaimekansouki(getItemRow(processData.getItemList(), GXHDO102B028Const.SANKAIMEKANSOUKI));
        bean.setSankaimekansouondo(getItemRow(processData.getItemList(), GXHDO102B028Const.SANKAIMEKANSOUONDO));
        bean.setSankaimekansoujikan(getItemRow(processData.getItemList(), GXHDO102B028Const.SANKAIMEKANSOUJIKAN));
        bean.setSankaimekansoukaisi_day(getItemRow(processData.getItemList(), GXHDO102B028Const.SANKAIMEKANSOUKAISI_DAY));
        bean.setSankaimekansoukaisi_time(getItemRow(processData.getItemList(), GXHDO102B028Const.SANKAIMEKANSOUKAISI_TIME));
        bean.setSankaimekansousyuuryou_day(getItemRow(processData.getItemList(), GXHDO102B028Const.SANKAIMEKANSOUSYUURYOU_DAY));
        bean.setSankaimekansousyuuryou_time(getItemRow(processData.getItemList(), GXHDO102B028Const.SANKAIMEKANSOUSYUURYOU_TIME));
        bean.setSankaimekansougosoujyuuryou(getItemRow(processData.getItemList(), GXHDO102B028Const.SANKAIMEKANSOUGOSOUJYUURYOU));
        bean.setSkmksgsmjr(getItemRow(processData.getItemList(), GXHDO102B028Const.SKMKSGSMJR));
        bean.setSankaimekokeibunhiritu(getItemRow(processData.getItemList(), GXHDO102B028Const.SANKAIMEKOKEIBUNHIRITU));
        bean.setSkmkkbskttts(getItemRow(processData.getItemList(), GXHDO102B028Const.SKMKKBSKTTTS));
        bean.setFpjyunbi_fuutaijyuuryou(getItemRow(processData.getItemList(), GXHDO102B028Const.FPJYUNBI_FUUTAIJYUURYOU));
        bean.setFpjyunbi_tantousya(getItemRow(processData.getItemList(), GXHDO102B028Const.FPJYUNBI_TANTOUSYA));
        bean.setFilterrenketu(getItemRow(processData.getItemList(), GXHDO102B028Const.FILTERRENKETU));
        bean.setFpjyunbi_filterhinmei1(getItemRow(processData.getItemList(), GXHDO102B028Const.FPJYUNBI_FILTERHINMEI1));
        bean.setFpjyunbi_lotno1(getItemRow(processData.getItemList(), GXHDO102B028Const.FPJYUNBI_LOTNO1));
        bean.setFpjyunbi_toritukehonsuu1(getItemRow(processData.getItemList(), GXHDO102B028Const.FPJYUNBI_TORITUKEHONSUU1));
        bean.setFpjyunbi_filterhinmei2(getItemRow(processData.getItemList(), GXHDO102B028Const.FPJYUNBI_FILTERHINMEI2));
        bean.setFpjyunbi_lotno2(getItemRow(processData.getItemList(), GXHDO102B028Const.FPJYUNBI_LOTNO2));
        bean.setFpjyunbi_toritukehonsuu2(getItemRow(processData.getItemList(), GXHDO102B028Const.FPJYUNBI_TORITUKEHONSUU2));
        bean.setAssoutanknosenjyou(getItemRow(processData.getItemList(), GXHDO102B028Const.ASSOUTANKNOSENJYOU));
        bean.setHaisyutuyoukinoutibukuro(getItemRow(processData.getItemList(), GXHDO102B028Const.HAISYUTUYOUKINOUTIBUKURO));
        bean.setFptankno(getItemRow(processData.getItemList(), GXHDO102B028Const.FPTANKNO));
        bean.setFpkaisi_day(getItemRow(processData.getItemList(), GXHDO102B028Const.FPKAISI_DAY));
        bean.setFpkaisi_time(getItemRow(processData.getItemList(), GXHDO102B028Const.FPKAISI_TIME));
        bean.setAssouregulatorno(getItemRow(processData.getItemList(), GXHDO102B028Const.ASSOUREGULATORNO));
        bean.setAssouaturyoku(getItemRow(processData.getItemList(), GXHDO102B028Const.ASSOUATURYOKU));
        bean.setFpkaisi_tantousya(getItemRow(processData.getItemList(), GXHDO102B028Const.FPKAISI_TANTOUSYA));
        bean.setFpteisi_day(getItemRow(processData.getItemList(), GXHDO102B028Const.FPTEISI_DAY));
        bean.setFpteisi_time(getItemRow(processData.getItemList(), GXHDO102B028Const.FPTEISI_TIME));
        bean.setFpkoukan_filterhinmei1(getItemRow(processData.getItemList(), GXHDO102B028Const.FPKOUKAN_FILTERHINMEI1));
        bean.setFpkoukan_lotno1(getItemRow(processData.getItemList(), GXHDO102B028Const.FPKOUKAN_LOTNO1));
        bean.setFpkoukan_toritukehonsuu1(getItemRow(processData.getItemList(), GXHDO102B028Const.FPKOUKAN_TORITUKEHONSUU1));
        bean.setFpkoukan_filterhinmei2(getItemRow(processData.getItemList(), GXHDO102B028Const.FPKOUKAN_FILTERHINMEI2));
        bean.setFpkoukan_lotno2(getItemRow(processData.getItemList(), GXHDO102B028Const.FPKOUKAN_LOTNO2));
        bean.setFpkoukan_toritukehonsuu2(getItemRow(processData.getItemList(), GXHDO102B028Const.FPKOUKAN_TORITUKEHONSUU2));
        bean.setFpsaikai_day(getItemRow(processData.getItemList(), GXHDO102B028Const.FPSAIKAI_DAY));
        bean.setFpsaikai_time(getItemRow(processData.getItemList(), GXHDO102B028Const.FPSAIKAI_TIME));
        bean.setFpkoukan_tantousya(getItemRow(processData.getItemList(), GXHDO102B028Const.FPKOUKAN_TANTOUSYA));
        bean.setFpsyuuryou_day(getItemRow(processData.getItemList(), GXHDO102B028Const.FPSYUURYOU_DAY));
        bean.setFpsyuuryou_time(getItemRow(processData.getItemList(), GXHDO102B028Const.FPSYUURYOU_TIME));
        bean.setFpjikan(getItemRow(processData.getItemList(), GXHDO102B028Const.FPJIKAN));
        bean.setFpsyuurou_tantousya(getItemRow(processData.getItemList(), GXHDO102B028Const.FPSYUUROU_TANTOUSYA));
        bean.setSoujyuuryou(getItemRow(processData.getItemList(), GXHDO102B028Const.SOUJYUURYOU));
        bean.setSyoumijyuuryou(getItemRow(processData.getItemList(), GXHDO102B028Const.SYOUMIJYUURYOU));
        bean.setBikou1(getItemRow(processData.getItemList(), GXHDO102B028Const.BIKOU1));
        bean.setBikou2(getItemRow(processData.getItemList(), GXHDO102B028Const.BIKOU2));
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力_ｻﾌﾞ画面データの規格値取得処理
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
     * 溶剤1_材料品名のﾘﾝｸ押下時、 ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC013SubGamen1(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B028Const.YOUZAI1_BUZAIZAIKOLOTNO1, GXHDO102B028Const.YOUZAI1_TYOUGOURYOU1,
                GXHDO102B028Const.YOUZAI1_BUZAIZAIKOLOTNO2, GXHDO102B028Const.YOUZAI1_TYOUGOURYOU2);
        return openC013SubGamen(processData, 1, returnItemIdList, GXHDO102B028Const.YOUZAI1_TYOUGOURYOUKIKAKU);
    }

    /**
     * 溶剤2_材料品名のﾘﾝｸ押下時、 ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC013SubGamen2(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B028Const.YOUZAI2_BUZAIZAIKOLOTNO1, GXHDO102B028Const.YOUZAI2_TYOUGOURYOU1,
                GXHDO102B028Const.YOUZAI2_BUZAIZAIKOLOTNO2, GXHDO102B028Const.YOUZAI2_TYOUGOURYOU2);
        return openC013SubGamen(processData, 2, returnItemIdList, GXHDO102B028Const.YOUZAI2_TYOUGOURYOUKIKAKU);
    }

    /**
     * 2回目溶剤1_材料品名のﾘﾝｸ押下時、 ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC013SubGamen3(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B028Const.NKMYZ1_BUZAIZAIKOLOTNO1, GXHDO102B028Const.NKMYZ1_TYOUGOURYOU1,
                GXHDO102B028Const.NKMYZ1_BUZAIZAIKOLOTNO2, GXHDO102B028Const.NKMYZ1_TYOUGOURYOU2);
        return openC013SubGamen(processData, 3, returnItemIdList, GXHDO102B028Const.NKMYZ1_TYOUGOURYOUKIKAKU);
    }

    /**
     * 2回目溶剤2_材料品名のﾘﾝｸ押下時、 ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC013SubGamen4(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B028Const.NKMYZ2_BUZAIZAIKOLOTNO1, GXHDO102B028Const.NKMYZ2_TYOUGOURYOU1,
                GXHDO102B028Const.NKMYZ2_BUZAIZAIKOLOTNO2, GXHDO102B028Const.NKMYZ2_TYOUGOURYOU2);
        return openC013SubGamen(processData, 4, returnItemIdList, GXHDO102B028Const.NKMYZ2_TYOUGOURYOUKIKAKU);
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @param zairyokubun 材料区分
     * @param returnItemIdList サブ画面から戻ったときに値を設定必要項目リスト
     * @param youzai_tyougouryoukikakuItemId 溶剤_調合量規格項目ID
     * @return 処理制御データ
     */
    public ProcessData openC013SubGamen(ProcessData processData, int zairyokubun, List<String> returnItemIdList, String youzai_tyougouryoukikakuItemId) {
        try {
            // 「秤量号機」
            FXHDD01 itemGoki = getItemRow(processData.getItemList(), GXHDO102B028Const.GOKI);
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
            FXHDD01 itemTyogouryoukikaku = getItemRow(processData.getItemList(), youzai_tyougouryoukikakuItemId);
            // 「調合量規格」ﾁｪｯｸ処理
            ErrorMessageInfo checkItemTyogouryoukikaku1ErrorInfo = checkTyogouryoukikaku(itemTyogouryoukikaku);
            if (checkItemTyogouryoukikaku1ErrorInfo != null) {
                processData.setErrorMessageInfoList(Arrays.asList(checkItemTyogouryoukikaku1ErrorInfo));
                return processData;
            }
            processData.setMethod("");
            // コールバックパラメータにてサブ画面起動用の値を設定
            processData.setCollBackParam("gxhdo102c013");

            GXHDO102C013 beanGXHDO102C013 = (GXHDO102C013) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C013);
            GXHDO102C013Model gxhdo102c013model = beanGXHDO102C013.getGxhdO102c013Model();
            // 主画面からサブ画面に渡されたデータを設定
            setSubGamenInitData(processData, gxhdo102c013model, zairyokubun, itemGoki, returnItemIdList, youzai_tyougouryoukikakuItemId);

            beanGXHDO102C013.setGxhdO102c013ModelView(gxhdo102c013model.clone());
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
     * @param processData 処理制御データ
     * @param gxhdo102c013model モデルデータ
     * @param zairyokubun 材料区分
     * @param itemGoki 秤量号機データ
     * @param returnItemIdList サブ画面から戻るデータリスト
     * @param tyougouryoukikakuItemId 調合量規格項目ID
     * @throws CloneNotSupportedException 例外エラー
     */
    private void setSubGamenInitData(ProcessData processData, GXHDO102C013Model gxhdo102c013model, int zairyokubun, FXHDD01 itemGoki, List<String> returnItemIdList,
            String tyougouryoukikakuItemId) throws CloneNotSupportedException {
        GXHDO102C013Model.SubGamenData c013subgamendata = GXHDO102C013Logic.getC013subgamendata(gxhdo102c013model, zairyokubun);
        if (c013subgamendata == null) {
            return;
        }
        c013subgamendata.setSubDataGoki(StringUtil.nullToBlank(itemGoki.getValue()));
        c013subgamendata.setSubDataZairyokubun(zairyokubun);

        String tyougouryoukikakuVal = getFXHDD01KikakuChiValue(getItemRow(processData.getItemList(), tyougouryoukikakuItemId)); // 調合規格
        c013subgamendata.getSubDataTyogouryoukikaku().setValue(tyougouryoukikakuVal.replace("【", "").replace("】", ""));
        // サブ画面から戻ったときに値を設定する項目を指定する。
        c013subgamendata.setReturnItemIdBuzailotno1(returnItemIdList.get(0)); // 部材在庫No.X_1
        c013subgamendata.setReturnItemIdTyougouryou1(returnItemIdList.get(1)); // 調合量X_1
        c013subgamendata.setReturnItemIdBuzailotno2(returnItemIdList.get(2)); // 部材在庫NoX_2
        c013subgamendata.setReturnItemIdTyougouryou2(returnItemIdList.get(3)); // 調合量X_2
        gxhdo102c013model.setShowsubgamendata(c013subgamendata.clone());
        // サブ画面の調合残量の計算
        GXHDO102C013Logic.calcTyogouzanryou(gxhdo102c013model);
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力_ｻﾌﾞ画面データの設定値取得処理
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
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力_ｻﾌﾞ画面データ設定処理
     *
     * @param processData 処理制御データ
     * @param SubSrSlipSlrkkbtsSutenyoukiList
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力_ｻﾌﾞ画面データリスト
     */
    private void setInputItemDataSubFormC013(ProcessData processData, List<SubSrSlipSlurrykokeibuntyouseiSutenyouki> SubSrSlipSlrkkbtsSutenyoukiList) {
        // サブ画面の情報を取得
        GXHDO102C013 beanGXHDO102C013 = (GXHDO102C013) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C013);

        GXHDO102C013Model model;
        if (SubSrSlipSlrkkbtsSutenyoukiList == null) {
            // 登録データが無い場合、主画面の材料品名1-2と調合量規格1-2はｻﾌﾞ画面の初期値にセットする。
            SubSrSlipSlrkkbtsSutenyoukiList = new ArrayList<>();
            SubSrSlipSlurrykokeibuntyouseiSutenyouki subgamen1 = new SubSrSlipSlurrykokeibuntyouseiSutenyouki();
            SubSrSlipSlurrykokeibuntyouseiSutenyouki subgamen2 = new SubSrSlipSlurrykokeibuntyouseiSutenyouki();
            SubSrSlipSlurrykokeibuntyouseiSutenyouki subgamen3 = new SubSrSlipSlurrykokeibuntyouseiSutenyouki();
            SubSrSlipSlurrykokeibuntyouseiSutenyouki subgamen4 = new SubSrSlipSlurrykokeibuntyouseiSutenyouki();

            subgamen1.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B028Const.YOUZAI1_ZAIRYOUHINMEI))); // 溶剤1_材料品名
            subgamen1.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B028Const.YOUZAI1_TYOUGOURYOUKIKAKU))); // 溶剤1_調合量規格
            subgamen1.setStandardpattern(getItemRow(processData.getItemList(), GXHDO102B028Const.YOUZAI1_TYOUGOURYOUKIKAKU).getStandardPattern()); // 溶剤1_調合量規格情報ﾊﾟﾀｰﾝ
            subgamen2.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B028Const.YOUZAI2_ZAIRYOUHINMEI))); // 溶剤2_材料品名
            subgamen2.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B028Const.YOUZAI2_TYOUGOURYOUKIKAKU))); // 溶剤2_調合量規格
            subgamen2.setStandardpattern(getItemRow(processData.getItemList(), GXHDO102B028Const.YOUZAI2_TYOUGOURYOUKIKAKU).getStandardPattern()); // 溶剤2_調合量規格情報ﾊﾟﾀｰﾝ
            subgamen3.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B028Const.NKMYZ1_ZAIRYOUHINMEI))); // 2回目溶剤1_材料品名
            subgamen3.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B028Const.NKMYZ1_TYOUGOURYOUKIKAKU))); // 2回目溶剤1_調合量規格
            subgamen3.setStandardpattern(getItemRow(processData.getItemList(), GXHDO102B028Const.NKMYZ1_TYOUGOURYOUKIKAKU).getStandardPattern()); // 2回目溶剤1_調合量規格情報ﾊﾟﾀｰﾝ
            subgamen4.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B028Const.NKMYZ2_ZAIRYOUHINMEI))); // 2回目溶剤2_材料品名
            subgamen4.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemList(), GXHDO102B028Const.NKMYZ2_TYOUGOURYOUKIKAKU))); // 2回目溶剤2_調合量規格
            subgamen4.setStandardpattern(getItemRow(processData.getItemList(), GXHDO102B028Const.NKMYZ2_TYOUGOURYOUKIKAKU).getStandardPattern()); // 2回目溶剤2_調合量規格情報ﾊﾟﾀｰﾝ
            SubSrSlipSlrkkbtsSutenyoukiList.add(subgamen1);
            SubSrSlipSlrkkbtsSutenyoukiList.add(subgamen2);
            SubSrSlipSlrkkbtsSutenyoukiList.add(subgamen3);
            SubSrSlipSlrkkbtsSutenyoukiList.add(subgamen4);
            model = GXHDO102C013Logic.createGXHDO102C013Model(SubSrSlipSlrkkbtsSutenyoukiList);

        } else {
            // 登録データがあれば登録データをセットする。
            model = GXHDO102C013Logic.createGXHDO102C013Model(SubSrSlipSlrkkbtsSutenyoukiList);
        }
        beanGXHDO102C013.setGxhdO102c013Model(model);
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力_ｻﾌﾞ画面の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力_ｻﾌﾞ画面登録データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrSlipSlurrykokeibuntyouseiSutenyouki> getSubSrSlipSlurrykokeibuntyouseiSutenyoukiData(QueryRunner queryRunnerQcdb,
            String rev, String jotaiFlg, String kojyo, String lotNo, String edaban) throws SQLException {
        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSubSrSlipSlurrykokeibuntyouseiSutenyouki(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSubSrSlipSlurrykokeibuntyouseiSutenyouki(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        }
    }

    /**
     * [ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力_ｻﾌﾞ画面]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrSlipSlurrykokeibuntyouseiSutenyouki> loadSubSrSlipSlurrykokeibuntyouseiSutenyouki(QueryRunner queryRunnerQcdb,
            String kojyo, String lotNo, String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + "kojyo,lotno,edaban,zairyokubun,tyogouryoukikaku,tyogouzanryou,zairyohinmei,"
                + "buzailotno1,buzaihinmei1,tyougouryou1_1,tyougouryou1_2,tyougouryou1_3,tyougouryou1_4,"
                + "tyougouryou1_5,tyougouryou1_6,buzailotno2,buzaihinmei2,tyougouryou2_1,tyougouryou2_2,"
                + "tyougouryou2_3,tyougouryou2_4,tyougouryou2_5,tyougouryou2_6,torokunichiji,kosinnichiji,"
                + "revision, '0' AS deleteflag "
                + " FROM sub_sr_slip_slurrykokeibuntyousei_sutenyouki "
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
        ResultSetHandler<List<SubSrSlipSlurrykokeibuntyouseiSutenyouki>> beanHandler = new BeanListHandler<>(SubSrSlipSlurrykokeibuntyouseiSutenyouki.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力_ｻﾌﾞ画面_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrSlipSlurrykokeibuntyouseiSutenyouki> loadTmpSubSrSlipSlurrykokeibuntyouseiSutenyouki(QueryRunner queryRunnerQcdb,
            String kojyo, String lotNo, String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + "kojyo,lotno,edaban,zairyokubun,tyogouryoukikaku,tyogouzanryou,zairyohinmei,"
                + "buzailotno1,buzaihinmei1,tyougouryou1_1,tyougouryou1_2,tyougouryou1_3,tyougouryou1_4,"
                + "tyougouryou1_5,tyougouryou1_6,buzailotno2,buzaihinmei2,tyougouryou2_1,tyougouryou2_2,"
                + "tyougouryou2_3,tyougouryou2_4,tyougouryou2_5,tyougouryou2_6,torokunichiji,kosinnichiji,"
                + "revision, deleteflag "
                + " FROM tmp_sub_sr_slip_slurrykokeibuntyousei_sutenyouki "
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
        ResultSetHandler<List<SubSrSlipSlurrykokeibuntyouseiSutenyouki>> beanHandler = new BeanListHandler<>(SubSrSlipSlurrykokeibuntyouseiSutenyouki.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力_サブ画面_仮登録(tmp_sub_sr_slip_slurrykokeibuntyousei_sutenyouki)登録処理
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
    private void insertTmpSubSrSlipSlurrykokeibuntyouseiSutenyouki(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal newRev, int deleteflag, String kojyo, String lotNo, String edaban, Integer zairyokubun,
            String systemTime, ProcessData processData) throws SQLException {

        String sql = "INSERT INTO tmp_sub_sr_slip_slurrykokeibuntyousei_sutenyouki ( "
                + "kojyo,lotno,edaban,zairyokubun,tyogouryoukikaku,tyogouzanryou,zairyohinmei,"
                + "buzailotno1,buzaihinmei1,tyougouryou1_1,tyougouryou1_2,tyougouryou1_3,tyougouryou1_4,"
                + "tyougouryou1_5,tyougouryou1_6,buzailotno2,buzaihinmei2,tyougouryou2_1,tyougouryou2_2,"
                + "tyougouryou2_3,tyougouryou2_4,tyougouryou2_5,tyougouryou2_6,torokunichiji,kosinnichiji,"
                + "revision, deleteflag "
                + " ) VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";

        List<Object> params = setUpdateParameterTmpSubSrSlipSlurrykokeibuntyouseiSutenyouki(true, newRev, deleteflag, kojyo, lotNo, edaban, zairyokubun, systemTime, null, processData);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力_仮登録(tmp_sub_sr_slip_slurrykokeibuntyousei_sutenyouki)更新処理
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
     * @param srSlipSlurrykokeibuntyouseiSutenyouki
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)_仮登録更新前データ
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void updateTmpSubSrSlipSlurrykokeibuntyouseiSutenyouki(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal rev, BigDecimal newRev, String kojyo, String lotNo, String edaban, Integer zairyokubun, String systemTime,
            SrSlipSlurrykokeibuntyouseiSutenyouki srSlipSlurrykokeibuntyouseiSutenyouki, ProcessData processData) throws SQLException {

        String sql = "UPDATE tmp_sub_sr_slip_slurrykokeibuntyousei_sutenyouki SET "
                + "tyogouryoukikaku = ?,tyogouzanryou = ?,zairyohinmei = ?,"
                + "buzailotno1 = ?,buzaihinmei1 = ?,tyougouryou1_1 = ?,tyougouryou1_2 = ?,tyougouryou1_3 = ?,tyougouryou1_4 = ?,"
                + "tyougouryou1_5 = ?,tyougouryou1_6 = ?,buzailotno2 = ?,buzaihinmei2 = ?,tyougouryou2_1 = ?,tyougouryou2_2 = ?,"
                + "tyougouryou2_3 = ?,tyougouryou2_4 = ?,tyougouryou2_5 = ?,tyougouryou2_6 = ?,kosinnichiji = ?,revision = ?, deleteflag = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND zairyokubun = ? AND revision = ? ";

        List<Object> params = setUpdateParameterTmpSubSrSlipSlurrykokeibuntyouseiSutenyouki(false, newRev, 0, kojyo, lotNo, edaban, zairyokubun, systemTime, srSlipSlurrykokeibuntyouseiSutenyouki, processData);

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
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力_サブ画面仮登録(tmp_sub_sr_slip_slurrykokeibuntyousei_sutenyouki)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param zairyokubun 材料区分
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param srSlipSlurrykokeibuntyouseiSutenyouki
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)_仮登録更新前データ
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSubSrSlipSlurrykokeibuntyouseiSutenyouki(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo, String lotNo,
            String edaban, Integer zairyokubun, String systemTime, SrSlipSlurrykokeibuntyouseiSutenyouki srSlipSlurrykokeibuntyouseiSutenyouki, ProcessData processData) {
        List<FXHDD01> pItemList = processData.getItemList();
        List<Object> params = new ArrayList<>();

        // 子画面情報を取得
        GXHDO102C013 beanGXHDO102C013 = (GXHDO102C013) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C013);
        GXHDO102C013Model gxhdO102c013Model = beanGXHDO102C013.getGxhdO102c013Model();

        // ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力_サブ画面から更新値を取得
        ArrayList<Object> subGamenDataList = getSubGamenData(gxhdO102c013Model, zairyokubun);
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
        String tyougouryoukikakuValue = getItemKikakuchi(pItemList, getTyougouryoukikakuItemId(zairyokubun), srSlipSlurrykokeibuntyouseiSutenyouki);

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
            // 溶剤1_調合量規格
            case 1:
                tyogouryoukikakuItemId = GXHDO102B028Const.YOUZAI1_TYOUGOURYOUKIKAKU;
                break;
            //  溶剤2_調合量規格
            case 2:
                tyogouryoukikakuItemId = GXHDO102B028Const.YOUZAI2_TYOUGOURYOUKIKAKU;
                break;
            //  2回目溶剤1_調合量規格
            case 3:
                tyogouryoukikakuItemId = GXHDO102B028Const.NKMYZ1_TYOUGOURYOUKIKAKU;
                break;
            //  2回目溶剤2_調合量規格
            case 4:
                tyogouryoukikakuItemId = GXHDO102B028Const.NKMYZ2_TYOUGOURYOUKIKAKU;
                break;
        }
        return tyogouryoukikakuItemId;
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力_サブ画面仮登録(tmp_sub_sr_slip_slurrykokeibuntyousei_sutenyouki)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSubSrSlipSlurrykokeibuntyouseiSutenyouki(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal rev, String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sub_sr_slip_slurrykokeibuntyousei_sutenyouki "
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
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力_サブ画面(sub_sr_slip_slurrykokeibuntyousei_sutenyouki)登録処理
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
     * @param tmpSrSlipSlurrykokeibuntyouseiSutenyouki
     * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)入力_仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSubSrSlipSlurrykokeibuntyouseiSutenyouki(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal newRev, String kojyo, String lotNo, String edaban, Integer zairyokubun, String systemTime,
            ProcessData processData, SrSlipSlurrykokeibuntyouseiSutenyouki tmpSrSlipSlurrykokeibuntyouseiSutenyouki) throws SQLException {
        String sql = "INSERT INTO sub_sr_slip_slurrykokeibuntyousei_sutenyouki ( "
                + "kojyo,lotno,edaban,zairyokubun,tyogouryoukikaku,tyogouzanryou,zairyohinmei,"
                + "buzailotno1,buzaihinmei1,tyougouryou1_1,tyougouryou1_2,tyougouryou1_3,tyougouryou1_4,"
                + "tyougouryou1_5,tyougouryou1_6,buzailotno2,buzaihinmei2,tyougouryou2_1,tyougouryou2_2,"
                + "tyougouryou2_3,tyougouryou2_4,tyougouryou2_5,tyougouryou2_6,torokunichiji,kosinnichiji,"
                + "revision "
                + " ) VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";

        List<Object> params = setUpdateParameterSubSrSlipSlurrykokeibuntyouseiSutenyouki(true, newRev, kojyo, lotNo, edaban, zairyokubun, systemTime, tmpSrSlipSlurrykokeibuntyouseiSutenyouki, processData);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力_ｻﾌﾞ画面(sub_sr_slip_slurrykokeibuntyousei_sutenyouki)更新処理
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
     * @param srSlipSlurrykokeibuntyouseiSutenyouki
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)更新前データ
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void updateSubSrSlipSlurrykokeibuntyouseiSutenyouki(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal rev, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            Integer zairyokubun, String systemTime, SrSlipSlurrykokeibuntyouseiSutenyouki srSlipSlurrykokeibuntyouseiSutenyouki, ProcessData processData) throws SQLException {

        String sql = "UPDATE sub_sr_slip_slurrykokeibuntyousei_sutenyouki SET "
                + "tyogouryoukikaku = ?,tyogouzanryou = ?,zairyohinmei = ?,"
                + "buzailotno1 = ?,buzaihinmei1 = ?,tyougouryou1_1 = ?,tyougouryou1_2 = ?,tyougouryou1_3 = ?,tyougouryou1_4 = ?,"
                + "tyougouryou1_5 = ?,tyougouryou1_6 = ?,buzailotno2 = ?,buzaihinmei2 = ?,tyougouryou2_1 = ?,tyougouryou2_2 = ?,"
                + "tyougouryou2_3 = ?,tyougouryou2_4 = ?,tyougouryou2_5 = ?,tyougouryou2_6 = ?,kosinnichiji = ?,revision = ?"
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND zairyokubun = ? AND revision = ? ";

        List<Object> params = setUpdateParameterSubSrSlipSlurrykokeibuntyouseiSutenyouki(false, newRev, kojyo, lotNo, edaban, zairyokubun, systemTime, srSlipSlurrykokeibuntyouseiSutenyouki, processData);

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
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力_サブ画面から更新値を取得
     *
     * @param gxhdO102c013Model モデルデータ
     * @param zairyokubun 材料区分
     * @return 更新値情報
     */
    private ArrayList<Object> getSubGamenData(GXHDO102C013Model gxhdO102c013Model, Integer zairyokubun) {
        GXHDO102C013Model.SubGamenData c013subgamendata = GXHDO102C013Logic.getC013subgamendata(gxhdO102c013Model, zairyokubun);
        ArrayList<Object> returnList = new ArrayList<>();
        // 調合量規格
        FXHDD01 tyogouryoukikaku = c013subgamendata.getSubDataTyogouryoukikaku();
        // 調合残量
        FXHDD01 tyogouzanryou = c013subgamendata.getSubDataTyogouzanryou();
        // 部材①
        List<FXHDD01> buzaitab1DataList = c013subgamendata.getSubDataBuzaitab1();
        // 部材②
        List<FXHDD01> buzaitab2DataList = c013subgamendata.getSubDataBuzaitab2();
        returnList.add(tyogouryoukikaku);
        returnList.add(tyogouzanryou);
        returnList.add(buzaitab1DataList);
        returnList.add(buzaitab2DataList);
        return returnList;
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力_サブ画面登録(tmp_sub_sr_slip_slurrykokeibuntyousei_sutenyouki)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param zairyokubun 材料区分
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param srSlipSlurrykokeibuntyouseiSutenyouki
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)_仮登録更新前データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSubSrSlipSlurrykokeibuntyouseiSutenyouki(boolean isInsert, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Integer zairyokubun, String systemTime,
            SrSlipSlurrykokeibuntyouseiSutenyouki srSlipSlurrykokeibuntyouseiSutenyouki, ProcessData processData) {
        List<FXHDD01> pItemList = processData.getItemList();
        List<Object> params = new ArrayList<>();

        // 子画面情報を取得
        GXHDO102C013 beanGXHDO102C013 = (GXHDO102C013) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C013);
        GXHDO102C013Model gxhdO102c013Model = beanGXHDO102C013.getGxhdO102c013Model();
        // ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力_サブ画面から更新値を取得
        ArrayList<Object> subGamenDataList = getSubGamenData(gxhdO102c013Model, zairyokubun);
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
        String tyougouryoukikakuValue = getItemKikakuchi(pItemList, getTyougouryoukikakuItemId(zairyokubun), srSlipSlurrykokeibuntyouseiSutenyouki);

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
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力_サブ画面仮登録(tmp_sub_sr_slip_slurrykokeibuntyousei_sutenyouki)登録処理(削除時)
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
    private void insertDeleteDataTmpSubSrSlipSlurrykokeibuntyouseiSutenyouki(QueryRunner queryRunnerQcdb,
            Connection conQcdb, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, String systemTime) throws SQLException {
        String sql = "INSERT INTO tmp_sub_sr_slip_slurrykokeibuntyousei_sutenyouki( "
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
                + " FROM sub_sr_slip_slurrykokeibuntyousei_sutenyouki "
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
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)入力_サブ画面仮登録(sub_sr_slip_slurrykokeibuntyousei_sutenyouki)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSubSrSlipSlurrykokeibuntyouseiSutenyouki(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal rev, String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sub_sr_slip_slurrykokeibuntyousei_sutenyouki "
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
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B028Const.YOUZAI1_BUZAIZAIKOLOTNO1, GXHDO102B028Const.YOUZAI1_TYOUGOURYOU1, errorItemList);
        // 溶剤1_部材在庫No2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B028Const.YOUZAI1_BUZAIZAIKOLOTNO2, GXHDO102B028Const.YOUZAI1_TYOUGOURYOU2, errorItemList);
        // 溶剤2_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B028Const.YOUZAI2_BUZAIZAIKOLOTNO1, GXHDO102B028Const.YOUZAI2_TYOUGOURYOU1, errorItemList);
        // 溶剤2_部材在庫No2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B028Const.YOUZAI2_BUZAIZAIKOLOTNO2, GXHDO102B028Const.YOUZAI2_TYOUGOURYOU2, errorItemList);
        // 2回目溶剤1_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B028Const.NKMYZ1_BUZAIZAIKOLOTNO1, GXHDO102B028Const.NKMYZ1_TYOUGOURYOU1, errorItemList);
        // 2回目溶剤1_部材在庫No2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B028Const.NKMYZ1_BUZAIZAIKOLOTNO2, GXHDO102B028Const.NKMYZ1_TYOUGOURYOU2, errorItemList);
        // 2回目溶剤2_部材在庫No1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B028Const.NKMYZ2_BUZAIZAIKOLOTNO1, GXHDO102B028Const.NKMYZ2_TYOUGOURYOU1, errorItemList);
        // 2回目溶剤2_部材在庫No2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B028Const.NKMYZ2_BUZAIZAIKOLOTNO2, GXHDO102B028Const.NKMYZ2_TYOUGOURYOU2, errorItemList);

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
        FXHDD01 itemFxhdd01Wiplotno = getItemRow(processData.getItemList(), GXHDO102B028Const.WIPLOTNO);
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
        allItemIdMap.put(GXHDO102B028Const.WIPLOTNO, "WIPﾛｯﾄNo");
        allItemIdMap.put(GXHDO102B028Const.SLIPHINMEI, "ｽﾘｯﾌﾟ品名");
        allItemIdMap.put(GXHDO102B028Const.SLIPLOTNO, "ｽﾘｯﾌﾟLotNo");
        allItemIdMap.put(GXHDO102B028Const.LOTKUBUN, "ﾛｯﾄ区分");
        allItemIdMap.put(GXHDO102B028Const.GENRYOUKIGOU, "原料記号");
        allItemIdMap.put(GXHDO102B028Const.GOKI, "秤量号機");
        allItemIdMap.put(GXHDO102B028Const.SLURRYHINMEI, "ｽﾗﾘｰ品名");
        allItemIdMap.put(GXHDO102B028Const.SLURRYLOTNO, "ｽﾗﾘｰLotNo");
        allItemIdMap.put(GXHDO102B028Const.YOUKINO, "容器No");
        allItemIdMap.put(GXHDO102B028Const.SLURRYYUUKOUKIGEN, "ｽﾗﾘｰ有効期限");
        allItemIdMap.put(GXHDO102B028Const.KANSOUKOKEIBUN, "乾燥固形分");
        allItemIdMap.put(GXHDO102B028Const.DASSIKOKEIBUN, "脱脂固形分");
        allItemIdMap.put(GXHDO102B028Const.FUNSAISYUURYOU_DAY, "粉砕終了日");
        allItemIdMap.put(GXHDO102B028Const.FUNSAISYUURYOU_TIME, "粉砕終了時間");
        allItemIdMap.put(GXHDO102B028Const.BINDERKONGOU_DAY, "ﾊﾞｲﾝﾀﾞｰ混合日");
        allItemIdMap.put(GXHDO102B028Const.BINDERKONGOU_TIME, "ﾊﾞｲﾝﾀﾞｰ混合時間");
        allItemIdMap.put(GXHDO102B028Const.SLURRAGING, "ｽﾗﾘｰｴｰｼﾞﾝｸﾞ");
        allItemIdMap.put(GXHDO102B028Const.SLURRYKEIKANISUU, "ｽﾗﾘｰ経過日数");
        allItemIdMap.put(GXHDO102B028Const.SLIPJYOUHOU_FUUTAIJYUURYOU, "ｽﾘｯﾌﾟ情報_風袋重量");
        allItemIdMap.put(GXHDO102B028Const.SLURRYSOUJYUURYOU, "総重量");
        allItemIdMap.put(GXHDO102B028Const.SLURRYJYUURYOU, "ｽﾗﾘｰ重量");
        allItemIdMap.put(GXHDO102B028Const.KAKUHANSETUBI, "撹拌設備");
        allItemIdMap.put(GXHDO102B028Const.KAKUHANGOKI, "撹拌号機");
        allItemIdMap.put(GXHDO102B028Const.KAKUHANKAITENSUU, "撹拌回転数");
        allItemIdMap.put(GXHDO102B028Const.KAKUHANJIKAN, "撹拌時間");
        allItemIdMap.put(GXHDO102B028Const.KAKUHANKAISI_DAY, "撹拌開始日");
        allItemIdMap.put(GXHDO102B028Const.KAKUHANKAISI_TIME, "撹拌開始時間");
        allItemIdMap.put(GXHDO102B028Const.KAKUHANSYUURYOU_DAY, "撹拌終了日");
        allItemIdMap.put(GXHDO102B028Const.KAKUHANSYUURYOU_TIME, "撹拌終了時間");
        allItemIdMap.put(GXHDO102B028Const.DASSIZARANOSYURUI, "脱脂皿の種類");
        allItemIdMap.put(GXHDO102B028Const.KKBSKT_FUUTAIJYUURYOU, "ｱﾙﾐ皿風袋重量");
        allItemIdMap.put(GXHDO102B028Const.KANSOUMAESLURRYJYUURYOU, "乾燥前ｽﾗﾘｰ重量");
        allItemIdMap.put(GXHDO102B028Const.KANSOUKI, "乾燥機");
        allItemIdMap.put(GXHDO102B028Const.KANSOUONDO, "乾燥温度");
        allItemIdMap.put(GXHDO102B028Const.KANSOUJIKAN, "乾燥時間");
        allItemIdMap.put(GXHDO102B028Const.KANSOUKAISI_DAY, "乾燥開始日");
        allItemIdMap.put(GXHDO102B028Const.KANSOUKAISI_TIME, "乾燥開始時間");
        allItemIdMap.put(GXHDO102B028Const.KANSOUSYUURYOU_DAY, "乾燥終了日");
        allItemIdMap.put(GXHDO102B028Const.KANSOUSYUURYOU_TIME, "乾燥終了時間");
        allItemIdMap.put(GXHDO102B028Const.KANSOUGOSOUJYUURYOU, "乾燥後総重量");
        allItemIdMap.put(GXHDO102B028Const.KANSOUGOSYOUMIJYUURYOU, "乾燥後正味重量");
        allItemIdMap.put(GXHDO102B028Const.KOKEIBUNHIRITU, "固形分比率");
        allItemIdMap.put(GXHDO102B028Const.KOKEIBUNSOKUTEITANTOUSYA, "固形分測定担当者");
        allItemIdMap.put(GXHDO102B028Const.YOUZAITYOUSEIRYOU, "溶剤調整量");
        allItemIdMap.put(GXHDO102B028Const.TOLUENETYOUSEIRYOU, "ﾄﾙｴﾝ調整量");
        allItemIdMap.put(GXHDO102B028Const.SOLMIXTYOUSEIRYOU, "ｿﾙﾐｯｸｽ調整量");
        allItemIdMap.put(GXHDO102B028Const.YOUZAIKEIRYOU_DAY, "溶剤秤量日");
        allItemIdMap.put(GXHDO102B028Const.YOUZAIKEIRYOU_TIME, "溶剤秤量時間");
        allItemIdMap.put(GXHDO102B028Const.YOUZAI1_ZAIRYOUHINMEI, "溶剤①_材料品名");
        allItemIdMap.put(GXHDO102B028Const.YOUZAI1_TYOUGOURYOUKIKAKU, "溶剤①_調合量規格");
        allItemIdMap.put(GXHDO102B028Const.YOUZAI1_BUZAIZAIKOLOTNO1, "溶剤①_部材在庫No1");
        allItemIdMap.put(GXHDO102B028Const.YOUZAI1_TYOUGOURYOU1, "溶剤①_調合量1");
        allItemIdMap.put(GXHDO102B028Const.YOUZAI1_BUZAIZAIKOLOTNO2, "溶剤①_部材在庫No2");
        allItemIdMap.put(GXHDO102B028Const.YOUZAI1_TYOUGOURYOU2, "溶剤①_調合量2");
        allItemIdMap.put(GXHDO102B028Const.YOUZAI2_ZAIRYOUHINMEI, "溶剤②_材料品名");
        allItemIdMap.put(GXHDO102B028Const.YOUZAI2_TYOUGOURYOUKIKAKU, "溶剤②_調合量規格");
        allItemIdMap.put(GXHDO102B028Const.YOUZAI2_BUZAIZAIKOLOTNO1, "溶剤②_部材在庫No1");
        allItemIdMap.put(GXHDO102B028Const.YOUZAI2_TYOUGOURYOU1, "溶剤②_調合量1");
        allItemIdMap.put(GXHDO102B028Const.YOUZAI2_BUZAIZAIKOLOTNO2, "溶剤②_部材在庫No2");
        allItemIdMap.put(GXHDO102B028Const.YOUZAI2_TYOUGOURYOU2, "溶剤②_調合量2");
        allItemIdMap.put(GXHDO102B028Const.TANTOUSYA, "担当者");
        allItemIdMap.put(GXHDO102B028Const.NIKAIMEKAKUHANSETUBI, "2回目撹拌設備");
        allItemIdMap.put(GXHDO102B028Const.NIKAIMEKAKUHANJIKAN, "2回目撹拌時間");
        allItemIdMap.put(GXHDO102B028Const.NIKAIMEKAKUHANKAISI_DAY, "2回目撹拌開始日");
        allItemIdMap.put(GXHDO102B028Const.NIKAIMEKAKUHANKAISI_TIME, "2回目撹拌開始時間");
        allItemIdMap.put(GXHDO102B028Const.NKMKKHSR_DAY, "2回目撹拌終了日");
        allItemIdMap.put(GXHDO102B028Const.NKMKKHSR_TIME, "2回目撹拌終了時間");
        allItemIdMap.put(GXHDO102B028Const.NIKAIMEDASSIZARANOSYURUI, "2回目脱脂皿の種類");
        allItemIdMap.put(GXHDO102B028Const.NKMARMZFTJR, "2回目ｱﾙﾐ皿風袋重量");
        allItemIdMap.put(GXHDO102B028Const.NKMKSMSLRJR, "2回目乾燥前ｽﾗﾘｰ重量");
        allItemIdMap.put(GXHDO102B028Const.NIKAIMEKANSOUKI, "2回目乾燥機");
        allItemIdMap.put(GXHDO102B028Const.NIKAIMEKANSOUONDO, "2回目乾燥温度");
        allItemIdMap.put(GXHDO102B028Const.NIKAIMEKANSOUJIKAN, "2回目乾燥時間");
        allItemIdMap.put(GXHDO102B028Const.NIKAIMEKANSOUKAISI_DAY, "2回目乾燥開始日");
        allItemIdMap.put(GXHDO102B028Const.NIKAIMEKANSOUKAISI_TIME, "2回目乾燥開始時間");
        allItemIdMap.put(GXHDO102B028Const.NIKAIMEKANSOUSYUURYOU_DAY, "2回目乾燥終了日");
        allItemIdMap.put(GXHDO102B028Const.NIKAIMEKANSOUSYUURYOU_TIME, "2回目乾燥終了時間");
        allItemIdMap.put(GXHDO102B028Const.NIKAIMEKANSOUGOSOUJYUURYOU, "2回目乾燥後総重量");
        allItemIdMap.put(GXHDO102B028Const.NKMKSGSMJR, "2回目乾燥後正味重量");
        allItemIdMap.put(GXHDO102B028Const.NIKAIMEKOKEIBUNHIRITU, "2回目固形分比率");
        allItemIdMap.put(GXHDO102B028Const.NKMKKBSKTTTS, "2回目固形分測定担当者");
        allItemIdMap.put(GXHDO102B028Const.NKMYZTSR, "2回目溶剤調整量");
        allItemIdMap.put(GXHDO102B028Const.NIKAIMETOLUENETYOUSEIRYOU, "2回目ﾄﾙｴﾝ調整量");
        allItemIdMap.put(GXHDO102B028Const.NIKAIMESOLMIXTYOUSEIRYOU, "2回目ｿﾙﾐｯｸｽ調整量");
        allItemIdMap.put(GXHDO102B028Const.NKMYZKEIRYOU_DAY, "2回目溶剤秤量日");
        allItemIdMap.put(GXHDO102B028Const.NKMYZKEIRYOU_TIME, "2回目溶剤秤量時間");
        allItemIdMap.put(GXHDO102B028Const.NKMYZ1_ZAIRYOUHINMEI, "2回目溶剤①_材料品名");
        allItemIdMap.put(GXHDO102B028Const.NKMYZ1_TYOUGOURYOUKIKAKU, "2回目溶剤①_調合量規格");
        allItemIdMap.put(GXHDO102B028Const.NKMYZ1_BUZAIZAIKOLOTNO1, "2回目溶剤①_部材在庫No1");
        allItemIdMap.put(GXHDO102B028Const.NKMYZ1_TYOUGOURYOU1, "2回目溶剤①_調合量1");
        allItemIdMap.put(GXHDO102B028Const.NKMYZ1_BUZAIZAIKOLOTNO2, "2回目溶剤①_部材在庫No2");
        allItemIdMap.put(GXHDO102B028Const.NKMYZ1_TYOUGOURYOU2, "2回目溶剤①_調合量2");
        allItemIdMap.put(GXHDO102B028Const.NKMYZ2_ZAIRYOUHINMEI, "2回目溶剤②_材料品名");
        allItemIdMap.put(GXHDO102B028Const.NKMYZ2_TYOUGOURYOUKIKAKU, "2回目溶剤②_調合量規格");
        allItemIdMap.put(GXHDO102B028Const.NKMYZ2_BUZAIZAIKOLOTNO1, "2回目溶剤②_部材在庫No1");
        allItemIdMap.put(GXHDO102B028Const.NKMYZ2_TYOUGOURYOU1, "2回目溶剤②_調合量1");
        allItemIdMap.put(GXHDO102B028Const.NKMYZ2_BUZAIZAIKOLOTNO2, "2回目溶剤②_部材在庫No2");
        allItemIdMap.put(GXHDO102B028Const.NKMYZ2_TYOUGOURYOU2, "2回目溶剤②_調合量2");
        allItemIdMap.put(GXHDO102B028Const.NIKAIMETANTOUSYA, "2回目担当者");
        allItemIdMap.put(GXHDO102B028Const.SANKAIMEKAKUHANSETUBI, "3回目撹拌設備");
        allItemIdMap.put(GXHDO102B028Const.SANKAIMEKAKUHANJIKAN, "3回目撹拌時間");
        allItemIdMap.put(GXHDO102B028Const.SANKAIMEKAKUHANKAISI_DAY, "3回目撹拌開始日");
        allItemIdMap.put(GXHDO102B028Const.SANKAIMEKAKUHANKAISI_TIME, "3回目撹拌開始時間");
        allItemIdMap.put(GXHDO102B028Const.SKMKKHSR_DAY, "3回目撹拌終了日");
        allItemIdMap.put(GXHDO102B028Const.SKMKKHSR_TIME, "3回目撹拌終了時間");
        allItemIdMap.put(GXHDO102B028Const.SANKAIMEDASSIZARANOSYURUI, "3回目脱脂皿の種類");
        allItemIdMap.put(GXHDO102B028Const.SKMARMZFTJR, "3回目ｱﾙﾐ皿風袋重量");
        allItemIdMap.put(GXHDO102B028Const.SKMKSMSLRJR, "3回目乾燥前ｽﾗﾘｰ重量");
        allItemIdMap.put(GXHDO102B028Const.SANKAIMEKANSOUKI, "3回目乾燥機");
        allItemIdMap.put(GXHDO102B028Const.SANKAIMEKANSOUONDO, "3回目乾燥温度");
        allItemIdMap.put(GXHDO102B028Const.SANKAIMEKANSOUJIKAN, "3回目乾燥時間");
        allItemIdMap.put(GXHDO102B028Const.SANKAIMEKANSOUKAISI_DAY, "3回目乾燥開始日");
        allItemIdMap.put(GXHDO102B028Const.SANKAIMEKANSOUKAISI_TIME, "3回目乾燥開始時間");
        allItemIdMap.put(GXHDO102B028Const.SANKAIMEKANSOUSYUURYOU_DAY, "3回目乾燥終了日");
        allItemIdMap.put(GXHDO102B028Const.SANKAIMEKANSOUSYUURYOU_TIME, "3回目乾燥終了時間");
        allItemIdMap.put(GXHDO102B028Const.SANKAIMEKANSOUGOSOUJYUURYOU, "3回目乾燥後総重量");
        allItemIdMap.put(GXHDO102B028Const.SKMKSGSMJR, "3回目乾燥後正味重量");
        allItemIdMap.put(GXHDO102B028Const.SANKAIMEKOKEIBUNHIRITU, "3回目固形分比率");
        allItemIdMap.put(GXHDO102B028Const.SKMKKBSKTTTS, "3回目固形分測定担当者");
        allItemIdMap.put(GXHDO102B028Const.FPJYUNBI_FUUTAIJYUURYOU, "F/P準備_風袋重量");
        allItemIdMap.put(GXHDO102B028Const.FPJYUNBI_TANTOUSYA, "F/P準備_担当者");
        allItemIdMap.put(GXHDO102B028Const.FILTERRENKETU, "ﾌｨﾙﾀｰ連結");
        allItemIdMap.put(GXHDO102B028Const.FPJYUNBI_FILTERHINMEI1, "F/P準備_ﾌｨﾙﾀｰ品名①");
        allItemIdMap.put(GXHDO102B028Const.FPJYUNBI_LOTNO1, "F/P準備_LotNo①");
        allItemIdMap.put(GXHDO102B028Const.FPJYUNBI_TORITUKEHONSUU1, "F/P準備_取り付け本数①");
        allItemIdMap.put(GXHDO102B028Const.FPJYUNBI_FILTERHINMEI2, "F/P準備_ﾌｨﾙﾀｰ品名②");
        allItemIdMap.put(GXHDO102B028Const.FPJYUNBI_LOTNO2, "F/P準備_LotNo②");
        allItemIdMap.put(GXHDO102B028Const.FPJYUNBI_TORITUKEHONSUU2, "F/P準備_取り付け本数②");
        allItemIdMap.put(GXHDO102B028Const.ASSOUTANKNOSENJYOU, "圧送ﾀﾝｸの洗浄");
        allItemIdMap.put(GXHDO102B028Const.HAISYUTUYOUKINOUTIBUKURO, "排出容器の内袋");
        allItemIdMap.put(GXHDO102B028Const.FPTANKNO, "F/PﾀﾝｸNo");
        allItemIdMap.put(GXHDO102B028Const.FPKAISI_DAY, "F/P開始日");
        allItemIdMap.put(GXHDO102B028Const.FPKAISI_TIME, "F/P開始時間");
        allItemIdMap.put(GXHDO102B028Const.ASSOUREGULATORNO, "圧送ﾚｷﾞｭﾚｰﾀｰNo");
        allItemIdMap.put(GXHDO102B028Const.ASSOUATURYOKU, "圧送圧力");
        allItemIdMap.put(GXHDO102B028Const.FPKAISI_TANTOUSYA, "F/P開始_担当者");
        allItemIdMap.put(GXHDO102B028Const.FPTEISI_DAY, "F/P停止日");
        allItemIdMap.put(GXHDO102B028Const.FPTEISI_TIME, "F/P停止時間");
        allItemIdMap.put(GXHDO102B028Const.FPKOUKAN_FILTERHINMEI1, "F/P交換_ﾌｨﾙﾀｰ品名①");
        allItemIdMap.put(GXHDO102B028Const.FPKOUKAN_LOTNO1, "F/P交換_LotNo①");
        allItemIdMap.put(GXHDO102B028Const.FPKOUKAN_TORITUKEHONSUU1, "F/P交換_取り付け本数①");
        allItemIdMap.put(GXHDO102B028Const.FPKOUKAN_FILTERHINMEI2, "F/P交換_ﾌｨﾙﾀｰ品名②");
        allItemIdMap.put(GXHDO102B028Const.FPKOUKAN_LOTNO2, "F/P交換_LotNo②");
        allItemIdMap.put(GXHDO102B028Const.FPKOUKAN_TORITUKEHONSUU2, "F/P交換_取り付け本数②");
        allItemIdMap.put(GXHDO102B028Const.FPSAIKAI_DAY, "F/P再開日");
        allItemIdMap.put(GXHDO102B028Const.FPSAIKAI_TIME, "F/P再開時間");
        allItemIdMap.put(GXHDO102B028Const.FPKOUKAN_TANTOUSYA, "F/P交換_担当者");
        allItemIdMap.put(GXHDO102B028Const.FPSYUURYOU_DAY, "F/P終了日");
        allItemIdMap.put(GXHDO102B028Const.FPSYUURYOU_TIME, "F/P終了時間");
        allItemIdMap.put(GXHDO102B028Const.FPJIKAN, "F/P時間");
        allItemIdMap.put(GXHDO102B028Const.FPSYUUROU_TANTOUSYA, "F/P終了_担当者");
        allItemIdMap.put(GXHDO102B028Const.SOUJYUURYOU, "総重量");
        allItemIdMap.put(GXHDO102B028Const.SYOUMIJYUURYOU, "正味重量");
        allItemIdMap.put(GXHDO102B028Const.BIKOU1, "備考1");
        allItemIdMap.put(GXHDO102B028Const.BIKOU2, "備考2");

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
        GXHDO102B028A bean = (GXHDO102B028A) getFormBean("gXHDO102B028A");
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
        bean.setNikaimekakuhansetubi(null);
        bean.setNikaimekakuhanjikan(null);
        bean.setNikaimekakuhankaisi_day(null);
        bean.setNikaimekakuhankaisi_time(null);
        bean.setNkmkkhsr_day(null);
        bean.setNkmkkhsr_time(null);
        bean.setNikaimedassizaranosyurui(null);
        bean.setNkmarmzftjr(null);
        bean.setNkmksmslrjr(null);
        bean.setNikaimekansouki(null);
        bean.setNikaimekansouondo(null);
        bean.setNikaimekansoujikan(null);
        bean.setNikaimekansoukaisi_day(null);
        bean.setNikaimekansoukaisi_time(null);
        bean.setNikaimekansousyuuryou_day(null);
        bean.setNikaimekansousyuuryou_time(null);
        bean.setNikaimekansougosoujyuuryou(null);
        bean.setNkmksgsmjr(null);
        bean.setNikaimekokeibunhiritu(null);
        bean.setNkmkkbskttts(null);
        bean.setNkmyztsr(null);
        bean.setNikaimetoluenetyouseiryou(null);
        bean.setNikaimesolmixtyouseiryou(null);
        bean.setNkmyzkeiryou_day(null);
        bean.setNkmyzkeiryou_time(null);
        bean.setNkmyz1_zairyouhinmei(null);
        bean.setNkmyz1_tyougouryoukikaku(null);
        bean.setNkmyz1_buzaizaikolotno1(null);
        bean.setNkmyz1_tyougouryou1(null);
        bean.setNkmyz1_buzaizaikolotno2(null);
        bean.setNkmyz1_tyougouryou2(null);
        bean.setNkmyz2_zairyouhinmei(null);
        bean.setNkmyz2_tyougouryoukikaku(null);
        bean.setNkmyz2_buzaizaikolotno1(null);
        bean.setNkmyz2_tyougouryou1(null);
        bean.setNkmyz2_buzaizaikolotno2(null);
        bean.setNkmyz2_tyougouryou2(null);
        bean.setNikaimetantousya(null);
        bean.setSankaimekakuhansetubi(null);
        bean.setSankaimekakuhanjikan(null);
        bean.setSankaimekakuhankaisi_day(null);
        bean.setSankaimekakuhankaisi_time(null);
        bean.setSkmkkhsr_day(null);
        bean.setSkmkkhsr_time(null);
        bean.setSankaimedassizaranosyurui(null);
        bean.setSkmarmzftjr(null);
        bean.setSkmksmslrjr(null);
        bean.setSankaimekansouki(null);
        bean.setSankaimekansouondo(null);
        bean.setSankaimekansoujikan(null);
        bean.setSankaimekansoukaisi_day(null);
        bean.setSankaimekansoukaisi_time(null);
        bean.setSankaimekansousyuuryou_day(null);
        bean.setSankaimekansousyuuryou_time(null);
        bean.setSankaimekansougosoujyuuryou(null);
        bean.setSkmksgsmjr(null);
        bean.setSankaimekokeibunhiritu(null);
        bean.setSkmkkbskttts(null);
        bean.setFpjyunbi_fuutaijyuuryou(null);
        bean.setFpjyunbi_tantousya(null);
        bean.setFilterrenketu(null);
        bean.setFpjyunbi_filterhinmei1(null);
        bean.setFpjyunbi_lotno1(null);
        bean.setFpjyunbi_toritukehonsuu1(null);
        bean.setFpjyunbi_filterhinmei2(null);
        bean.setFpjyunbi_lotno2(null);
        bean.setFpjyunbi_toritukehonsuu2(null);
        bean.setAssoutanknosenjyou(null);
        bean.setHaisyutuyoukinoutibukuro(null);
        bean.setFptankno(null);
        bean.setFpkaisi_day(null);
        bean.setFpkaisi_time(null);
        bean.setAssouregulatorno(null);
        bean.setAssouaturyoku(null);
        bean.setFpkaisi_tantousya(null);
        bean.setFpteisi_day(null);
        bean.setFpteisi_time(null);
        bean.setFpkoukan_filterhinmei1(null);
        bean.setFpkoukan_lotno1(null);
        bean.setFpkoukan_toritukehonsuu1(null);
        bean.setFpkoukan_filterhinmei2(null);
        bean.setFpkoukan_lotno2(null);
        bean.setFpkoukan_toritukehonsuu2(null);
        bean.setFpsaikai_day(null);
        bean.setFpsaikai_time(null);
        bean.setFpkoukan_tantousya(null);
        bean.setFpsyuuryou_day(null);
        bean.setFpsyuuryou_time(null);
        bean.setFpjikan(null);
        bean.setFpsyuurou_tantousya(null);
        bean.setSoujyuuryou(null);
        bean.setSyoumijyuuryou(null);
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
        Map fxhbm03Data = loadFxhbm03Data(queryRunnerDoc, "ｽﾘｯﾌﾟ作製_ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)_表示制御");
        // 画面非表示項目リスト: 
        List<String> notShowItemList = Arrays.asList(GXHDO102B028Const.YOUZAITYOUSEIRYOU, GXHDO102B028Const.TOLUENETYOUSEIRYOU, GXHDO102B028Const.SOLMIXTYOUSEIRYOU,
                GXHDO102B028Const.YOUZAIKEIRYOU_DAY, GXHDO102B028Const.YOUZAIKEIRYOU_TIME, GXHDO102B028Const.YOUZAI1_ZAIRYOUHINMEI, GXHDO102B028Const.YOUZAI1_TYOUGOURYOUKIKAKU,
                GXHDO102B028Const.YOUZAI1_BUZAIZAIKOLOTNO1, GXHDO102B028Const.YOUZAI1_TYOUGOURYOU1, GXHDO102B028Const.YOUZAI1_BUZAIZAIKOLOTNO2,
                GXHDO102B028Const.YOUZAI1_TYOUGOURYOU2, GXHDO102B028Const.YOUZAI2_ZAIRYOUHINMEI, GXHDO102B028Const.YOUZAI2_TYOUGOURYOUKIKAKU,
                GXHDO102B028Const.YOUZAI2_BUZAIZAIKOLOTNO1, GXHDO102B028Const.YOUZAI2_TYOUGOURYOU1, GXHDO102B028Const.YOUZAI2_BUZAIZAIKOLOTNO2,
                GXHDO102B028Const.YOUZAI2_TYOUGOURYOU2, GXHDO102B028Const.TANTOUSYA, GXHDO102B028Const.NIKAIMEKAKUHANSETUBI,
                GXHDO102B028Const.NIKAIMEKAKUHANJIKAN, GXHDO102B028Const.NIKAIMEKAKUHANKAISI_DAY, GXHDO102B028Const.NIKAIMEKAKUHANKAISI_TIME,
                GXHDO102B028Const.NKMKKHSR_DAY, GXHDO102B028Const.NKMKKHSR_TIME, GXHDO102B028Const.NIKAIMEDASSIZARANOSYURUI,
                GXHDO102B028Const.NKMARMZFTJR, GXHDO102B028Const.NKMKSMSLRJR, GXHDO102B028Const.NIKAIMEKANSOUKI, GXHDO102B028Const.NIKAIMEKANSOUONDO,
                GXHDO102B028Const.NIKAIMEKANSOUJIKAN, GXHDO102B028Const.NIKAIMEKANSOUKAISI_DAY, GXHDO102B028Const.NIKAIMEKANSOUKAISI_TIME,
                GXHDO102B028Const.NIKAIMEKANSOUSYUURYOU_DAY, GXHDO102B028Const.NIKAIMEKANSOUSYUURYOU_TIME, GXHDO102B028Const.NIKAIMEKANSOUGOSOUJYUURYOU,
                GXHDO102B028Const.NKMKSGSMJR, GXHDO102B028Const.NIKAIMEKOKEIBUNHIRITU, GXHDO102B028Const.NKMKKBSKTTTS, GXHDO102B028Const.NKMYZTSR,
                GXHDO102B028Const.NIKAIMETOLUENETYOUSEIRYOU, GXHDO102B028Const.NIKAIMESOLMIXTYOUSEIRYOU, GXHDO102B028Const.NKMYZKEIRYOU_DAY,
                GXHDO102B028Const.NKMYZKEIRYOU_TIME, GXHDO102B028Const.NKMYZ1_ZAIRYOUHINMEI, GXHDO102B028Const.NKMYZ1_TYOUGOURYOUKIKAKU,
                GXHDO102B028Const.NKMYZ1_BUZAIZAIKOLOTNO1, GXHDO102B028Const.NKMYZ1_TYOUGOURYOU1, GXHDO102B028Const.NKMYZ1_BUZAIZAIKOLOTNO2,
                GXHDO102B028Const.NKMYZ1_TYOUGOURYOU2, GXHDO102B028Const.NKMYZ2_ZAIRYOUHINMEI, GXHDO102B028Const.NKMYZ2_TYOUGOURYOUKIKAKU,
                GXHDO102B028Const.NKMYZ2_BUZAIZAIKOLOTNO1, GXHDO102B028Const.NKMYZ2_TYOUGOURYOU1, GXHDO102B028Const.NKMYZ2_BUZAIZAIKOLOTNO2,
                GXHDO102B028Const.NKMYZ2_TYOUGOURYOU2, GXHDO102B028Const.NIKAIMETANTOUSYA, GXHDO102B028Const.SANKAIMEKAKUHANSETUBI,
                GXHDO102B028Const.SANKAIMEKAKUHANJIKAN, GXHDO102B028Const.SANKAIMEKAKUHANKAISI_DAY, GXHDO102B028Const.SANKAIMEKAKUHANKAISI_TIME,
                GXHDO102B028Const.SKMKKHSR_DAY, GXHDO102B028Const.SKMKKHSR_TIME, GXHDO102B028Const.SANKAIMEDASSIZARANOSYURUI, GXHDO102B028Const.SKMARMZFTJR,
                GXHDO102B028Const.SKMKSMSLRJR, GXHDO102B028Const.SANKAIMEKANSOUKI, GXHDO102B028Const.SANKAIMEKANSOUONDO, GXHDO102B028Const.SANKAIMEKANSOUJIKAN,
                GXHDO102B028Const.SANKAIMEKANSOUKAISI_DAY, GXHDO102B028Const.SANKAIMEKANSOUKAISI_TIME, GXHDO102B028Const.SANKAIMEKANSOUSYUURYOU_DAY,
                GXHDO102B028Const.SANKAIMEKANSOUSYUURYOU_TIME, GXHDO102B028Const.SANKAIMEKANSOUGOSOUJYUURYOU, GXHDO102B028Const.SKMKSGSMJR,
                GXHDO102B028Const.SANKAIMEKOKEIBUNHIRITU, GXHDO102B028Const.SKMKKBSKTTTS, GXHDO102B028Const.FPJYUNBI_FUUTAIJYUURYOU,
                GXHDO102B028Const.FPJYUNBI_TANTOUSYA, GXHDO102B028Const.FILTERRENKETU, GXHDO102B028Const.FPJYUNBI_FILTERHINMEI1,
                GXHDO102B028Const.FPJYUNBI_LOTNO1, GXHDO102B028Const.FPJYUNBI_TORITUKEHONSUU1, GXHDO102B028Const.FPJYUNBI_FILTERHINMEI2,
                GXHDO102B028Const.FPJYUNBI_LOTNO2, GXHDO102B028Const.FPJYUNBI_TORITUKEHONSUU2, GXHDO102B028Const.ASSOUTANKNOSENJYOU,
                GXHDO102B028Const.HAISYUTUYOUKINOUTIBUKURO, GXHDO102B028Const.FPTANKNO, GXHDO102B028Const.FPKAISI_DAY, GXHDO102B028Const.FPKAISI_TIME,
                GXHDO102B028Const.ASSOUREGULATORNO, GXHDO102B028Const.ASSOUATURYOKU, GXHDO102B028Const.FPKAISI_TANTOUSYA, GXHDO102B028Const.FPTEISI_DAY,
                GXHDO102B028Const.FPTEISI_TIME, GXHDO102B028Const.FPKOUKAN_FILTERHINMEI1, GXHDO102B028Const.FPKOUKAN_LOTNO1, GXHDO102B028Const.FPKOUKAN_TORITUKEHONSUU1,
                GXHDO102B028Const.FPKOUKAN_FILTERHINMEI2, GXHDO102B028Const.FPKOUKAN_LOTNO2, GXHDO102B028Const.FPKOUKAN_TORITUKEHONSUU2, GXHDO102B028Const.FPSAIKAI_DAY,
                GXHDO102B028Const.FPSAIKAI_TIME, GXHDO102B028Const.FPKOUKAN_TANTOUSYA, GXHDO102B028Const.FPSYUURYOU_DAY, GXHDO102B028Const.FPSYUURYOU_TIME,
                GXHDO102B028Const.FPJIKAN, GXHDO102B028Const.FPSYUUROU_TANTOUSYA, GXHDO102B028Const.SOUJYUURYOU, GXHDO102B028Const.SYOUMIJYUURYOU);
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
