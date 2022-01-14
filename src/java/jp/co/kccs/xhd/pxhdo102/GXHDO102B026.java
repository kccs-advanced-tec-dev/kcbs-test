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
import jp.co.kccs.xhd.common.InitMessage;
import jp.co.kccs.xhd.common.KikakuError;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.db.model.SikakariJson;
import jp.co.kccs.xhd.db.model.SrYuudentaiFp;
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
import org.apache.commons.dbutils.handlers.MapListHandler;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/11/14<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B026(誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管)
 *
 * @author KCSS K.Jo
 * @since 2021/11/14
 */
public class GXHDO102B026 implements IFormLogic {

    private static final Logger LOGGER = Logger.getLogger(GXHDO102B026.class.getName());
    private static final String JOTAI_FLG_KARI_TOROKU = "0";
    private static final String JOTAI_FLG_TOROKUZUMI = "1";
    private static final String JOTAI_FLG_SAKUJO = "9";
    private static final String SQL_STATE_RECORD_LOCK_ERR = "55P03";

    /**
     * コンストラクタ
     */
    public GXHDO102B026() {
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
            initGXHDO102B026A(processData);

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
            processData.setNoCheckButtonId(Arrays.asList(GXHDO102B026Const.BTN_EDABAN_COPY_TOP,
                    GXHDO102B026Const.BTN_FPKAISINICHIJI_TOP,
                    GXHDO102B026Const.BTN_FILTERKOUKAN1_FPTEISI_TOP,
                    GXHDO102B026Const.BTN_FILTERKOUKAN1_FPSAIKAI_TOP,
                    GXHDO102B026Const.BTN_KANSOUKAISI_TOP,
                    GXHDO102B026Const.BTN_KANSOUSYUURYOU_TOP,
                    GXHDO102B026Const.BTN_EDABAN_COPY_BOTTOM,
                    GXHDO102B026Const.BTN_FPKAISINICHIJI_BOTTOM,
                    GXHDO102B026Const.BTN_FILTERKOUKAN1_FPTEISI_BOTTOM,
                    GXHDO102B026Const.BTN_FILTERKOUKAN1_FPSAIKAI_BOTTOM,
                    GXHDO102B026Const.BTN_KANSOUKAISI_BOTTOM,
                    GXHDO102B026Const.BTN_KANSOUSYUURYOU_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO102B026Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO102B026Const.BTN_INSERT_TOP,
                    GXHDO102B026Const.BTN_DELETE_TOP,
                    GXHDO102B026Const.BTN_UPDATE_TOP,
                    GXHDO102B026Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO102B026Const.BTN_INSERT_BOTTOM,
                    GXHDO102B026Const.BTN_DELETE_BOTTOM,
                    GXHDO102B026Const.BTN_UPDATE_BOTTOM
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
            case GXHDO102B026Const.BTN_EDABAN_COPY_TOP:
            case GXHDO102B026Const.BTN_EDABAN_COPY_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 仮登録
            case GXHDO102B026Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO102B026Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO102B026Const.BTN_INSERT_TOP:
            case GXHDO102B026Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 修正
            case GXHDO102B026Const.BTN_UPDATE_TOP:
            case GXHDO102B026Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO102B026Const.BTN_DELETE_TOP:
            case GXHDO102B026Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // F/P開始日時
            case GXHDO102B026Const.BTN_FPKAISINICHIJI_TOP:
            case GXHDO102B026Const.BTN_FPKAISINICHIJI_BOTTOM:
                method = "setFpkaisinichijiDateTime";
                break;
            // F/P停止日時
            case GXHDO102B026Const.BTN_FILTERKOUKAN1_FPTEISI_TOP:
            case GXHDO102B026Const.BTN_FILTERKOUKAN1_FPTEISI_BOTTOM:
                method = "setFilterkoukan1fpteisiDateTime";
                break;
            // F/P再開日時
            case GXHDO102B026Const.BTN_FILTERKOUKAN1_FPSAIKAI_TOP:
            case GXHDO102B026Const.BTN_FILTERKOUKAN1_FPSAIKAI_BOTTOM:
                method = "setFilterkoukan1fpsaikaiDateTime";
                break;
            // F/P終了日時
            case GXHDO102B026Const.BTN_FPSYUURYOUNICHIJI_TOP:
            case GXHDO102B026Const.BTN_FPSYUURYOUNICHIJI_BOTTOM:
                method = "setFpsyuuryounichijiDateTime";
                break;
            // 乾燥開始日時
            case GXHDO102B026Const.BTN_KANSOUKAISI_TOP:
            case GXHDO102B026Const.BTN_KANSOUKAISI_BOTTOM:
                method = "setKansoukaisiDateTime";
                break;
            // 乾燥終了日時
            case GXHDO102B026Const.BTN_KANSOUSYUURYOU_TOP:
            case GXHDO102B026Const.BTN_KANSOUSYUURYOU_BOTTOM:
                method = "setKansousyuuryouDateTime";
                break;
            // 1次ﾌｨﾙﾀｰ総使用本数計算
            case GXHDO102B026Const.BTN_ITIJIFILTERSOUSIYOUHONSUU_TOP:
            case GXHDO102B026Const.BTN_ITIJIFILTERSOUSIYOUHONSUU_BOTTOM:
                method = "setItijifiltersousiyouhonsuu";
                break;
            // 2次ﾌｨﾙﾀｰ総使用本数計算
            case GXHDO102B026Const.BTN_NIJIFILTERSOUSIYOUHONSUU_TOP:
            case GXHDO102B026Const.BTN_NIJIFILTERSOUSIYOUHONSUU_BOTTOM:
                method = "setNijifiltersousiyouhonsuu";
                break;
            // 3次ﾌｨﾙﾀｰ総使用本数計算
            case GXHDO102B026Const.BTN_SANJIFILTERSOUSIYOUHONSUU_TOP:
            case GXHDO102B026Const.BTN_SANJIFILTERSOUSIYOUHONSUU_BOTTOM:
                method = "setSanjifiltersousiyouhonsuu";
                break;
            // 誘電体ｽﾗﾘｰ重量1計算
            case GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU1_TOP:
            case GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU1_BOTTOM:
                method = "setYuudentaislurryjyuurou1";
                break;
            // 誘電体ｽﾗﾘｰ重量2計算
            case GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU2_TOP:
            case GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU2_BOTTOM:
                method = "setYuudentaislurryjyuurou2";
                break;
            // 誘電体ｽﾗﾘｰ重量3計算
            case GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU3_TOP:
            case GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU3_BOTTOM:
                method = "setYuudentaislurryjyuurou3";
                break;
            // 誘電体ｽﾗﾘｰ重量4計算
            case GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU4_TOP:
            case GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU4_BOTTOM:
                method = "setYuudentaislurryjyuurou4";
                break;
            // 誘電体ｽﾗﾘｰ重量5計算
            case GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU5_TOP:
            case GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU5_BOTTOM:
                method = "setYuudentaislurryjyuurou5";
                break;
            // 誘電体ｽﾗﾘｰ重量6計算
            case GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU6_TOP:
            case GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU6_BOTTOM:
                method = "setYuudentaislurryjyuurou6";
                break;
            // 誘電体ｽﾗﾘｰ重量7計算
            case GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU7_TOP:
            case GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU7_BOTTOM:
                method = "setYuudentaislurryjyuurou7";
                break;
            // 誘電体ｽﾗﾘｰ重量8計算
            case GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU8_TOP:
            case GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU8_BOTTOM:
                method = "setYuudentaislurryjyuurou8";
                break;
            // 誘電体ｽﾗﾘｰ重量9計算
            case GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU9_TOP:
            case GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU9_BOTTOM:
                method = "setYuudentaislurryjyuurou9";
                break;
            // 誘電体ｽﾗﾘｰ重量10計算
            case GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU10_TOP:
            case GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU10_BOTTOM:
                method = "setYuudentaislurryjyuurou10";
                break;
            // 誘電体ｽﾗﾘｰ重量11計算
            case GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU11_TOP:
            case GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU11_BOTTOM:
                method = "setYuudentaislurryjyuurou11";
                break;
            // 誘電体ｽﾗﾘｰ重量12計算
            case GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU12_TOP:
            case GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU12_BOTTOM:
                method = "setYuudentaislurryjyuurou12";
                break;
            // 歩留まり計算
            case GXHDO102B026Const.BTN_BUDOMARIKEISAN_TOP:
            case GXHDO102B026Const.BTN_BUDOMARIKEISAN_BOTTOM:
                method = "setBudomarikeisan";
                break;
            // 誘電体ｽﾗﾘｰ有効期限計算
            case GXHDO102B026Const.BTN_UUDENTAISLURRYYUUKOUKIGEN_TOP:
            case GXHDO102B026Const.BTN_UUDENTAISLURRYYUUKOUKIGEN_BOTTOM:
                method = "setUudentaislurryyuukoukigen";
                break;
            // 乾燥後正味重量計算
            case GXHDO102B026Const.BTN_KANSOUGOSYOUMIJYUURYOU_TOP:
            case GXHDO102B026Const.BTN_KANSOUGOSYOUMIJYUURYOU_BOTTOM:
                method = "setKansougosyoumijyuuryou";
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

            // 誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管の入力項目の登録データ(仮登録時は仮登録データ)を取得
            List<SrYuudentaiFp> srYuudentaiFpDataList = getSrYuudentaiFpData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo9, oyalotEdaban);
            if (srYuudentaiFpDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }
            // メイン画面データ設定
            setInputItemDataMainForm(processData, srYuudentaiFpDataList.get(0));

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

                // 誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管_仮登録処理
                insertTmpSrYuudentaiFp(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, strSystime, processData);
            } else {

                // 誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管_仮登録更新処理
                updateTmpSrYuudentaiFp(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);
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
     * 規格値の入力値チェックを行う。
     * 規格値のエラー対象は引数のリスト(kikakuchiInputErrorInfoList)に項目情報を詰めて返される。
     *
     * @param processData 処理データ
     * @param kikakuchiInputErrorInfoList 規格値入力エラー情報リスト
     * @return チェックの正常終了時はNULL、異常時は内容に応じたエラーメッセージ情報を返す。
     */
    private ErrorMessageInfo checkInputKikakuchi(ProcessData processData, List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList) {
        // 規格値の入力値チェック必要の項目リスト
        List<FXHDD01> itemList = new ArrayList<>();
        // 乾燥時間ﾄｰﾀﾙに規格値を設定する
        setKikakuItem(processData, GXHDO102B026Const.KANSOUJIKANKIKAKU, GXHDO102B026Const.KANSOUJIKANTOTAL);
        List<String> kikakuItemList = Arrays.asList(GXHDO102B026Const.KANSOUJIKANKIKAKU);
        processData.getItemList().stream().filter(
                (fxhdd01) -> !(kikakuItemList.contains(fxhdd01.getItemId()) || StringUtil.isEmpty(fxhdd01.getStandardPattern()) || "【-】".equals(fxhdd01.getKikakuChi()))).filter(
                        (fxhdd01) -> !(!ValidateUtil.isInputColumn(fxhdd01) || StringUtil.isEmpty(fxhdd01.getValue()))).forEachOrdered(
                        (fxhdd01) -> {
                            // 規格チェックの対象項目である、かつ入力項目かつ入力値がある項目はリストに追加
                            itemList.add(fxhdd01);
                        });
        ErrorMessageInfo errorMessageInfo = ValidateUtil.checkInputKikakuchi(itemList, kikakuchiInputErrorInfoList);

        return errorMessageInfo;
    }

    /**
     * 規格値チェック項目に規格値を設定する
     *
     * @param processData 処理データ
     * @param kikakuItem 規格値項目名
     * @param checkItem 規格値チェック項目
     */
    private void setKikakuItem(ProcessData processData, String kikakuItem, String checkItem) {

        FXHDD01 kikakuItemFxhdd01 = getItemRow(processData.getItemList(), kikakuItem);
        FXHDD01 checkItemFxhdd01 = getItemRow(processData.getItemList(), checkItem);
        if (kikakuItemFxhdd01 == null || checkItemFxhdd01 == null) {
            return;
        }
        // 項目の規格値を設置
        checkItemFxhdd01.setKikakuChi(kikakuItemFxhdd01.getKikakuChi());
        checkItemFxhdd01.setStandardPattern(kikakuItemFxhdd01.getStandardPattern());
    }

    /**
     * 登録・修正項目チェック
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    private ErrorMessageInfo checkItemRegistCorrect(ProcessData processData) {
        ValidateUtil validateUtil = new ValidateUtil();
        // F/P開始日時、F/P終了日時前後チェック
        FXHDD01 itemKaisiDay = getItemRow(processData.getItemList(), GXHDO102B026Const.FPKAISI_DAY); // F/P開始日
        FXHDD01 itemKaisiTime = getItemRow(processData.getItemList(), GXHDO102B026Const.FPKAISI_TIME); // F/P開始時間
        FXHDD01 itemSyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B026Const.FPSYUURYOU_DAY); // F/P終了日
        FXHDD01 itemSyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B026Const.FPSYUURYOU_TIME); // F/P終了時間
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

        // F/P停止日時、F/P再開日時前後チェック
        itemKaisiDay = getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERKOUKAN1_FPTEISI_DAY); // F/P停止日
        itemKaisiTime = getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERKOUKAN1_FPTEISI_TIME); // F/P停止時間
        itemSyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERKOUKAN1_FPSAIKAI_DAY); // F/P再開日
        itemSyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERKOUKAN1_FPSAIKAI_TIME); // F/P再開時間
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

        // 乾燥開始日時、乾燥終了日時前後チェック
        itemKaisiDay = getItemRow(processData.getItemList(), GXHDO102B026Const.KANSOUKAISI_DAY); // 乾燥開始日
        itemKaisiTime = getItemRow(processData.getItemList(), GXHDO102B026Const.KANSOUKAISI_TIME); // 乾燥開始時間
        itemSyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B026Const.KANSOUSYUURYOU_DAY); // 乾燥終了日
        itemSyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B026Const.KANSOUSYUURYOU_TIME); // 乾燥終了時間
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

        // ﾁｪｯｸﾎﾞｯｸｽがすべてﾁｪｯｸされているかﾁｪｯｸ：排出容器の内袋、洗浄確認、保存用ｻﾝﾌﾟﾙ回収、分析用ｻﾝﾌﾟﾙ回収
        List<String> itemIdList = Arrays.asList(GXHDO102B026Const.HAISYUTUYOUKINOUTIBUKURO, GXHDO102B026Const.SENJYOUKAKUNIN, GXHDO102B026Const.HOZONYOUSAMPLEKAISYU, GXHDO102B026Const.ZUNSEKIYOUSAMPLEKAISYU);
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
            SrYuudentaiFp tmpSrYuudentaiFp = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrYuudentaiFp> srYuudentaiFpList = getSrYuudentaiFpData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo9, edaban);
                if (!srYuudentaiFpList.isEmpty()) {
                    tmpSrYuudentaiFp = srYuudentaiFpList.get(0);
                }

                deleteTmpSrYuudentaiFp(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);
            }

            // 誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管_登録処理
            insertSrYuudentaiFp(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, strSystime, processData, tmpSrYuudentaiFp);
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
        processData.setUserAuthParam(GXHDO102B026Const.USER_AUTH_UPDATE_PARAM);

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

            // 誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管_更新処理
            updateSrYuudentaiFp(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);

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
        processData.setUserAuthParam(GXHDO102B026Const.USER_AUTH_DELETE_PARAM);

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

            // 誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo9, edaban, paramJissekino);
            insertDeleteDataTmpSrYuudentaiFp(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo9, edaban, strSystime);

            // 誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管_削除処理
            deleteSrYuudentaiFp(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);

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
     * F/P開始日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setFpkaisinichijiDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B026Const.FPKAISI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B026Const.FPKAISI_TIME);
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
    public ProcessData setFilterkoukan1fpteisiDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERKOUKAN1_FPTEISI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERKOUKAN1_FPTEISI_TIME);
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
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERKOUKAN1_FPSAIKAI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERKOUKAN1_FPSAIKAI_TIME);
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
    public ProcessData setFpsyuuryounichijiDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B026Const.FPSYUURYOU_DAY); // F/P終了日
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B026Const.FPSYUURYOU_TIME); // F/P終了時間
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            // 【F/P終了日時】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
            ErrorMessageInfo checkItemErrorInfo = checkFpsyuuryounichijiDateTime(processData);
            if (checkItemErrorInfo != null) {
                processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
                return processData;
            }
            // F/P終了日時設定
            setDateTimeItem(itemDay, itemTime, new Date());
            // 「F/Pﾄｰﾀﾙ時間」計算処理
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
        FXHDD01 itemFpkaisiDay = getItemRow(processData.getItemList(), GXHDO102B026Const.FPKAISI_DAY); // F/P開始日
        FXHDD01 itemFpkaisiTime = getItemRow(processData.getItemList(), GXHDO102B026Const.FPKAISI_TIME); // F/P開始時間

        // 「F/P開始日」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemFpkaisiDay.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemFpkaisiDay);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemFpkaisiDay.getLabel1());
        }
        // 「F/P開始時間」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemFpkaisiTime.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemFpkaisiTime);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemFpkaisiTime.getLabel1());
        }

        FXHDD01 itemFpteisiDay = getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERKOUKAN1_FPTEISI_DAY); // F/P停止日
        FXHDD01 itemFpteisiTime = getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERKOUKAN1_FPTEISI_TIME); // F/P停止時間
        FXHDD01 itemFpsaikaiDay = getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERKOUKAN1_FPSAIKAI_DAY); // F/P再開日
        FXHDD01 itemFpsaikaiTime = getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERKOUKAN1_FPSAIKAI_TIME); // F/P再開時間
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
     * 「F/Pﾄｰﾀﾙ時間」計算処理
     *
     * @param processData 処理制御データ
     */
    private void setFpjikanItem(ProcessData processData) {
        FXHDD01 itemFptotaljikan = getItemRow(processData.getItemList(), GXHDO102B026Const.FPTOTALJIKAN); // F/Pﾄｰﾀﾙ時間
        FXHDD01 itemFpteisiDay = getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERKOUKAN1_FPTEISI_DAY); // F/P停止日
        FXHDD01 itemFpteisiTime = getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERKOUKAN1_FPTEISI_TIME); // F/P停止時間
        FXHDD01 itemFpsaikaiDay = getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERKOUKAN1_FPSAIKAI_DAY); // F/P再開日
        FXHDD01 itemFpsaikaiTime = getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERKOUKAN1_FPSAIKAI_TIME); // F/P再開時間
        if (itemFptotaljikan == null) {
            return;
        }
        // Dateオブジェクト変換
        Date fpsyuuryouDate = getDateTimeItem(processData, GXHDO102B026Const.FPSYUURYOU_DAY, GXHDO102B026Const.FPSYUURYOU_TIME); // F/P終了日時
        Date fpkaisiDate = getDateTimeItem(processData, GXHDO102B026Const.FPKAISI_DAY, GXHDO102B026Const.FPKAISI_TIME); // F/P開始日時
        Date fpteisiDate = getDateTimeItem(processData, GXHDO102B026Const.FILTERKOUKAN1_FPTEISI_DAY, GXHDO102B026Const.FILTERKOUKAN1_FPTEISI_TIME); // F/P停止日時
        Date fpsaikaiDate = getDateTimeItem(processData, GXHDO102B026Const.FILTERKOUKAN1_FPSAIKAI_DAY, GXHDO102B026Const.FILTERKOUKAN1_FPSAIKAI_TIME); // F/P再開日時
        itemFptotaljikan.setValue("");
        if (StringUtil.isEmpty(itemFpteisiDay.getValue()) && StringUtil.isEmpty(itemFpteisiTime.getValue()) && StringUtil.isEmpty(itemFpsaikaiDay.getValue())
                && StringUtil.isEmpty(itemFpsaikaiTime.getValue())) {
            if (fpsyuuryouDate == null || fpkaisiDate == null) {
                return;
            }
            // 「F/P終了日+F/P終了時間」 - 「F/P開始日+F/P開始時間」(　時間　分)
            BigDecimal diffMinutes = BigDecimal.valueOf(DateUtil.diffMinutes(fpkaisiDate, fpsyuuryouDate));
            itemFptotaljikan.setValue(diffMinutes.toPlainString());
        } else {
            if (fpsyuuryouDate == null || fpkaisiDate == null || fpteisiDate == null || fpsaikaiDate == null) {
                return;
            }
            // (「F/P終了日+F/P終了時間」 - 「F/P開始日+F/P開始時間」) - (「F/P再開日+F/P再開時間」 - 「F/P停止日+F/P停止時間」)(　時間　分)
            BigDecimal diffMinutes = BigDecimal.valueOf(DateUtil.diffMinutes(fpkaisiDate, fpsyuuryouDate)).subtract(BigDecimal.valueOf(DateUtil.diffMinutes(fpteisiDate, fpsaikaiDate)));
            itemFptotaljikan.setValue(diffMinutes.toPlainString());
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
     * 乾燥開始日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKansoukaisiDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B026Const.KANSOUKAISI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B026Const.KANSOUKAISI_TIME);
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
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B026Const.KANSOUSYUURYOU_DAY); // 乾燥終了日
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B026Const.KANSOUSYUURYOU_TIME); // 乾燥終了時間
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());

            Date kansoukaisiDate = getDateTimeItem(processData, GXHDO102B026Const.KANSOUKAISI_DAY, GXHDO102B026Const.KANSOUKAISI_TIME); // 乾燥開始日時
            Date kansousyuuryouDate = getDateTimeItem(processData, GXHDO102B026Const.KANSOUSYUURYOU_DAY, GXHDO102B026Const.KANSOUSYUURYOU_TIME); // 乾燥終了日時
            FXHDD01 itemKansoujikantotal = getItemRow(processData.getItemList(), GXHDO102B026Const.KANSOUJIKANTOTAL); // 乾燥時間ﾄｰﾀﾙ
            if (itemKansoujikantotal != null && kansoukaisiDate != null && kansousyuuryouDate != null) {
                // (乾燥終了日 + 乾燥終了時間) - (乾燥開始日 + 乾燥開始時間)(　時間　分)
                BigDecimal diffMinutes = BigDecimal.valueOf(DateUtil.diffMinutes(kansoukaisiDate, kansousyuuryouDate));
                // 乾燥時間ﾄｰﾀﾙ
                itemKansoujikantotal.setValue(diffMinutes.toPlainString());
            }
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 1次ﾌｨﾙﾀｰ総使用本数計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setItijifiltersousiyouhonsuu(ProcessData processData) {
        FXHDD01 itemFiltertorituke_toritukehonsuu = getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERTORITUKE_TORITUKEHONSUU1); // ﾌｨﾙﾀｰ取り付け_取り付け本数1
        FXHDD01 itemFilterkoukan1_toritukehonsuu = getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERKOUKAN1_TORITUKEHONSUU1); // ﾌｨﾙﾀｰ交換①_取り付け本数1
        FXHDD01 itemFiltersousiyouhonsuu = getItemRow(processData.getItemList(), GXHDO102B026Const.ITIJIFILTERSOUSIYOUHONSUU); // 1次ﾌｨﾙﾀｰ総使用本数
        if (itemFiltertorituke_toritukehonsuu == null || itemFilterkoukan1_toritukehonsuu == null || itemFiltersousiyouhonsuu == null) {
            processData.setMethod("");
            return processData;
        }
        // 【1次ﾌｨﾙﾀｰ総使用本数計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkFiltersousiyouhonsuuKeisan(itemFiltertorituke_toritukehonsuu, itemFilterkoukan1_toritukehonsuu);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        // 1次ﾌｨﾙﾀｰ総使用本数計算処理
        calcFiltersousiyouhonsuu(itemFiltersousiyouhonsuu, itemFiltertorituke_toritukehonsuu, itemFilterkoukan1_toritukehonsuu);
        return processData;
    }

    /**
     * 【X次ﾌｨﾙﾀｰ総使用本数計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
     *
     * @param itemFiltertorituke_toritukehonsuu ﾌｨﾙﾀｰ取り付け_取り付け本数X
     * @param itemFilterkoukan1_toritukehonsuu ﾌｨﾙﾀｰ交換①_取り付け本数X
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkFiltersousiyouhonsuuKeisan(FXHDD01 itemFiltertorituke_toritukehonsuu, FXHDD01 itemFilterkoukan1_toritukehonsuu) {

        // 「ﾌｨﾙﾀｰ取り付け_取り付け本数X」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemFiltertorituke_toritukehonsuu.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemFiltertorituke_toritukehonsuu);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemFiltertorituke_toritukehonsuu.getLabel1());
        }
        // 「ﾌｨﾙﾀｰ交換①_取り付け本数X」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemFilterkoukan1_toritukehonsuu.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemFilterkoukan1_toritukehonsuu);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemFilterkoukan1_toritukehonsuu.getLabel1());
        }
        return null;
    }

    /**
     * 【X次ﾌｨﾙﾀｰ総使用本数計算】ﾎﾞﾀﾝ押下時計算処理
     *
     * @param itemFiltersousiyouhonsuu X次ﾌｨﾙﾀｰ総使用本数
     * @param itemFiltertorituke_toritukehonsuu ﾌｨﾙﾀｰ取り付け_取り付け本数X
     * @param itemFilterkoukan1_toritukehonsuu ﾌｨﾙﾀｰ交換①_取り付け本数X
     */
    private void calcFiltersousiyouhonsuu(FXHDD01 itemFiltersousiyouhonsuu, FXHDD01 itemFiltertorituke_toritukehonsuu, FXHDD01 itemFilterkoukan1_toritukehonsuu) {
        try {
            // ﾌｨﾙﾀｰ取り付け_取り付け本数X
            BigDecimal itemFiltertorituke_toritukehonsuuVal = new BigDecimal(itemFiltertorituke_toritukehonsuu.getValue());
            BigDecimal itemFilterkoukan1_toritukehonsuuVal = new BigDecimal(itemFilterkoukan1_toritukehonsuu.getValue()); // ﾌｨﾙﾀｰ交換①_取り付け本数X
            // ﾌｨﾙﾀｰ取り付け_取り付け本数X ＋ ﾌｨﾙﾀｰ交換①_取り付け本数X
            itemFiltersousiyouhonsuu.setValue(itemFiltertorituke_toritukehonsuuVal.add(itemFilterkoukan1_toritukehonsuuVal).toPlainString());
        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
            ErrUtil.outputErrorLog(itemFiltersousiyouhonsuu.getLabel1() + "にエラー発生", ex, LOGGER);
        }
    }

    /**
     * 2次ﾌｨﾙﾀｰ総使用本数計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setNijifiltersousiyouhonsuu(ProcessData processData) {
        FXHDD01 itemFiltertorituke_toritukehonsuu = getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERTORITUKE_TORITUKEHONSUU2); // ﾌｨﾙﾀｰ取り付け_取り付け本数2
        FXHDD01 itemFilterkoukan1_toritukehonsuu = getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERKOUKAN1_TORITUKEHONSUU2); // ﾌｨﾙﾀｰ交換①_取り付け本数2
        FXHDD01 itemFiltersousiyouhonsuu = getItemRow(processData.getItemList(), GXHDO102B026Const.NIJIFILTERSOUSIYOUHONSUU); // 2次ﾌｨﾙﾀｰ総使用本数
        if (itemFiltertorituke_toritukehonsuu == null || itemFilterkoukan1_toritukehonsuu == null || itemFiltersousiyouhonsuu == null) {
            processData.setMethod("");
            return processData;
        }
        // 【2次ﾌｨﾙﾀｰ総使用本数計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkFiltersousiyouhonsuuKeisan(itemFiltertorituke_toritukehonsuu, itemFilterkoukan1_toritukehonsuu);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        // 2次ﾌｨﾙﾀｰ総使用本数計算処理
        calcFiltersousiyouhonsuu(itemFiltersousiyouhonsuu, itemFiltertorituke_toritukehonsuu, itemFilterkoukan1_toritukehonsuu);
        return processData;
    }

    /**
     * 3次ﾌｨﾙﾀｰ総使用本数計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setSanjifiltersousiyouhonsuu(ProcessData processData) {
        FXHDD01 itemFiltertorituke_toritukehonsuu = getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERTORITUKE_TORITUKEHONSUU3); // ﾌｨﾙﾀｰ取り付け_取り付け本数3
        FXHDD01 itemFilterkoukan1_toritukehonsuu = getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERKOUKAN1_TORITUKEHONSUU3); // ﾌｨﾙﾀｰ交換①_取り付け本数3
        FXHDD01 itemFiltersousiyouhonsuu = getItemRow(processData.getItemList(), GXHDO102B026Const.SANJIFILTERSOUSIYOUHONSUU); // 3次ﾌｨﾙﾀｰ総使用本数
        if (itemFiltertorituke_toritukehonsuu == null || itemFilterkoukan1_toritukehonsuu == null || itemFiltersousiyouhonsuu == null) {
            processData.setMethod("");
            return processData;
        }
        // 【3次ﾌｨﾙﾀｰ総使用本数計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkFiltersousiyouhonsuuKeisan(itemFiltertorituke_toritukehonsuu, itemFilterkoukan1_toritukehonsuu);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        // 3次ﾌｨﾙﾀｰ総使用本数計算処理
        calcFiltersousiyouhonsuu(itemFiltersousiyouhonsuu, itemFiltertorituke_toritukehonsuu, itemFilterkoukan1_toritukehonsuu);
        return processData;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量1計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setYuudentaislurryjyuurou1(ProcessData processData) {
        FXHDD01 itemYuudentaislurryjyuurou = getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU1); // 誘電体ｽﾗﾘｰ重量1
        FXHDD01 itemSoujyuurousokutei = getItemRow(processData.getItemList(), GXHDO102B026Const.SOUJYUUROUSOKUTEI1); // 総重量測定1
        FXHDD01 itemFuutaijyuuryousokutei_sutenyouki = getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI1); // 風袋重量測定_ｽﾃﾝ容器1
        FXHDD01 itemFuutaijyuuryousokutei_siropori = getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI1); // 風袋重量測定_白ﾎﾟﾘ容器1
        if (itemYuudentaislurryjyuurou == null || itemSoujyuurousokutei == null || itemFuutaijyuuryousokutei_sutenyouki == null || itemFuutaijyuuryousokutei_siropori == null) {
            processData.setMethod("");
            return processData;
        }
        // 【誘電体ｽﾗﾘｰ重量1計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkYuudentaislurryjyuurouKeisan(itemSoujyuurousokutei, itemFuutaijyuuryousokutei_sutenyouki, itemFuutaijyuuryousokutei_siropori);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        // 誘電体ｽﾗﾘｰ重量1計算処理
        calcYuudentaislurryjyuurou(processData, itemYuudentaislurryjyuurou, itemSoujyuurousokutei, itemFuutaijyuuryousokutei_sutenyouki, itemFuutaijyuuryousokutei_siropori);
        return processData;
    }

    /**
     * 【誘電体ｽﾗﾘｰ重量計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
     *
     * @param itemSoujyuurousokutei 総重量測定
     * @param itemFuutaijyuuryousokutei_sutenyouki 風袋重量測定_ｽﾃﾝ容器
     * @param itemFuutaijyuuryousokutei_siropori 風袋重量測定_白ﾎﾟﾘ容器
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkYuudentaislurryjyuurouKeisan(FXHDD01 itemSoujyuurousokutei, FXHDD01 itemFuutaijyuuryousokutei_sutenyouki, FXHDD01 itemFuutaijyuuryousokutei_siropori) {
        // 「総重量測定」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemSoujyuurousokutei.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemSoujyuurousokutei);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemSoujyuurousokutei.getLabel1());
        }
        // 「風袋重量測定_ｽﾃﾝ容器」ﾁｪｯｸ
        if (itemFuutaijyuuryousokutei_sutenyouki == null) {
            if (StringUtil.isEmpty(itemFuutaijyuuryousokutei_siropori.getValue())) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemFuutaijyuuryousokutei_siropori);
                return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemFuutaijyuuryousokutei_siropori.getLabel1());
            }
        } else {
            if (StringUtil.isEmpty(itemFuutaijyuuryousokutei_sutenyouki.getValue())) {
                if (StringUtil.isEmpty(itemFuutaijyuuryousokutei_siropori.getValue())) {
                    // ｴﾗｰ項目をﾘｽﾄに追加
                    List<FXHDD01> errFxhdd01List = Arrays.asList(itemFuutaijyuuryousokutei_siropori);
                    return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemFuutaijyuuryousokutei_siropori.getLabel1());
                }
            }
        }

        return null;
    }

    /**
     * 【誘電体ｽﾗﾘｰ重量計算】ﾎﾞﾀﾝ押下時計算処理
     *
     * @param processData 処理制御データ
     *
     * @param itemYuudentaislurryjyuurou 誘電体ｽﾗﾘｰ重量
     * @param itemSoujyuurousokutei 総重量測定
     * @param itemFuutaijyuuryousokutei_sutenyouki 風袋重量測定_ｽﾃﾝ容器
     * @param itemFuutaijyuuryousokutei_siropori 風袋重量測定_白ﾎﾟﾘ容器
     */
    private void calcYuudentaislurryjyuurou(ProcessData processData, FXHDD01 itemYuudentaislurryjyuurou, FXHDD01 itemSoujyuurousokutei,
            FXHDD01 itemFuutaijyuuryousokutei_sutenyouki, FXHDD01 itemFuutaijyuuryousokutei_siropori) {
        try {
            FXHDD01 itemYuudentaislurryjyuurougoukei = getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROUGOUKEI); // 誘電体ｽﾗﾘｰ重量合計
            FXHDD01 itemYuudentaislurryjyuurou1 = getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU1); // 誘電体ｽﾗﾘｰ重量1
            FXHDD01 itemYuudentaislurryjyuurou2 = getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU2); // 誘電体ｽﾗﾘｰ重量2
            FXHDD01 itemYuudentaislurryjyuurou3 = getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU3); // 誘電体ｽﾗﾘｰ重量3
            FXHDD01 itemYuudentaislurryjyuurou4 = getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU4); // 誘電体ｽﾗﾘｰ重量4
            FXHDD01 itemYuudentaislurryjyuurou5 = getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU5); // 誘電体ｽﾗﾘｰ重量5
            FXHDD01 itemYuudentaislurryjyuurou6 = getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU6); // 誘電体ｽﾗﾘｰ重量6
            FXHDD01 itemYuudentaislurryjyuurou7 = getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU7); // 誘電体ｽﾗﾘｰ重量7
            FXHDD01 itemYuudentaislurryjyuurou8 = getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU8); // 誘電体ｽﾗﾘｰ重量8
            FXHDD01 itemYuudentaislurryjyuurou9 = getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU9); // 誘電体ｽﾗﾘｰ重量9
            FXHDD01 itemYuudentaislurryjyuurou10 = getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU10); // 誘電体ｽﾗﾘｰ重量10
            FXHDD01 itemYuudentaislurryjyuurou11 = getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU11); // 誘電体ｽﾗﾘｰ重量11
            FXHDD01 itemYuudentaislurryjyuurou12 = getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU12); // 誘電体ｽﾗﾘｰ重量12

            BigDecimal itemSoujyuurousokuteiVal = new BigDecimal(itemSoujyuurousokutei.getValue()); // 総重量測定
            if (itemFuutaijyuuryousokutei_sutenyouki == null) {
                if (!StringUtil.isEmpty(itemFuutaijyuuryousokutei_siropori.getValue())) {
                    BigDecimal itemFuutaijyuuryousokutei_siroporiVal = new BigDecimal(itemFuutaijyuuryousokutei_siropori.getValue()); // 風袋重量測定_白ﾎﾟﾘ容器
                    // 「誘電体ｽﾗﾘｰ重量X」計算処理: 総重量測定-風袋重量測定_白ﾎﾟﾘ容器
                    itemYuudentaislurryjyuurou.setValue(itemSoujyuurousokuteiVal.subtract(itemFuutaijyuuryousokutei_siroporiVal).toPlainString());
                }
            } else {
                if (!StringUtil.isEmpty(itemFuutaijyuuryousokutei_sutenyouki.getValue())) {
                    BigDecimal itemFuutaijyuuryousokutei_sutenyoukiVal = new BigDecimal(itemFuutaijyuuryousokutei_sutenyouki.getValue()); // 風袋重量測定_ｽﾃﾝ容器
                    // 「誘電体ｽﾗﾘｰ重量X」計算処理: 総重量測定-風袋重量測定_ｽﾃﾝ容器
                    itemYuudentaislurryjyuurou.setValue(itemSoujyuurousokuteiVal.subtract(itemFuutaijyuuryousokutei_sutenyoukiVal).toPlainString());
                } else {
                    if (!StringUtil.isEmpty(itemFuutaijyuuryousokutei_siropori.getValue())) {
                        BigDecimal itemFuutaijyuuryousokutei_siroporiVal = new BigDecimal(itemFuutaijyuuryousokutei_siropori.getValue()); // 風袋重量測定_白ﾎﾟﾘ容器
                        // 「誘電体ｽﾗﾘｰ重量X」計算処理: 総重量測定-風袋重量測定_白ﾎﾟﾘ容器
                        itemYuudentaislurryjyuurou.setValue(itemSoujyuurousokuteiVal.subtract(itemFuutaijyuuryousokutei_siroporiVal).toPlainString());
                    }
                }
            }

            BigDecimal itemYuudentaislurryjyuurou1Va1 = (BigDecimal) DBUtil.stringToBigDecimalObject(itemYuudentaislurryjyuurou1.getValue()); // 誘電体ｽﾗﾘｰ重量1
            BigDecimal itemYuudentaislurryjyuurou1Va2 = (BigDecimal) DBUtil.stringToBigDecimalObject(itemYuudentaislurryjyuurou2.getValue()); // 誘電体ｽﾗﾘｰ重量2
            BigDecimal itemYuudentaislurryjyuurou1Va3 = (BigDecimal) DBUtil.stringToBigDecimalObject(itemYuudentaislurryjyuurou3.getValue()); // 誘電体ｽﾗﾘｰ重量3
            BigDecimal itemYuudentaislurryjyuurou1Va4 = (BigDecimal) DBUtil.stringToBigDecimalObject(itemYuudentaislurryjyuurou4.getValue()); // 誘電体ｽﾗﾘｰ重量4
            BigDecimal itemYuudentaislurryjyuurou1Va5 = (BigDecimal) DBUtil.stringToBigDecimalObject(itemYuudentaislurryjyuurou5.getValue()); // 誘電体ｽﾗﾘｰ重量5
            BigDecimal itemYuudentaislurryjyuurou1Va6 = (BigDecimal) DBUtil.stringToBigDecimalObject(itemYuudentaislurryjyuurou6.getValue()); // 誘電体ｽﾗﾘｰ重量6
            BigDecimal itemYuudentaislurryjyuurou1Va7 = (BigDecimal) DBUtil.stringToBigDecimalObject(itemYuudentaislurryjyuurou7.getValue()); // 誘電体ｽﾗﾘｰ重量7
            BigDecimal itemYuudentaislurryjyuurou1Va8 = (BigDecimal) DBUtil.stringToBigDecimalObject(itemYuudentaislurryjyuurou8.getValue()); // 誘電体ｽﾗﾘｰ重量8
            BigDecimal itemYuudentaislurryjyuurou1Va9 = (BigDecimal) DBUtil.stringToBigDecimalObject(itemYuudentaislurryjyuurou9.getValue()); // 誘電体ｽﾗﾘｰ重量9
            BigDecimal itemYuudentaislurryjyuurou1Va10 = (BigDecimal) DBUtil.stringToBigDecimalObject(itemYuudentaislurryjyuurou10.getValue()); // 誘電体ｽﾗﾘｰ重量10
            BigDecimal itemYuudentaislurryjyuurou1Va11 = (BigDecimal) DBUtil.stringToBigDecimalObject(itemYuudentaislurryjyuurou11.getValue()); // 誘電体ｽﾗﾘｰ重量11
            BigDecimal itemYuudentaislurryjyuurou1Va12 = (BigDecimal) DBUtil.stringToBigDecimalObject(itemYuudentaislurryjyuurou12.getValue()); // 誘電体ｽﾗﾘｰ重量12
            // 「誘電体ｽﾗﾘｰ重量合計」計算処理:「誘電体ｽﾗﾘｰ重量1」 ～ 「誘電体ｽﾗﾘｰ重量12」 の和を算出する。
            if (itemYuudentaislurryjyuurougoukei != null) {
                BigDecimal itemYuudentaislurryjyuurougoukeiVal = itemYuudentaislurryjyuurou1Va1.add(itemYuudentaislurryjyuurou1Va2).add(itemYuudentaislurryjyuurou1Va3).add(
                        itemYuudentaislurryjyuurou1Va4).add(itemYuudentaislurryjyuurou1Va5).add(itemYuudentaislurryjyuurou1Va6).add(itemYuudentaislurryjyuurou1Va7).add(
                        itemYuudentaislurryjyuurou1Va8).add(itemYuudentaislurryjyuurou1Va9).add(itemYuudentaislurryjyuurou1Va10).add(itemYuudentaislurryjyuurou1Va11).add(
                        itemYuudentaislurryjyuurou1Va12);
                itemYuudentaislurryjyuurougoukei.setValue(itemYuudentaislurryjyuurougoukeiVal.toPlainString());
            }
        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
            ErrUtil.outputErrorLog(itemYuudentaislurryjyuurou.getLabel1() + "にエラー発生", ex, LOGGER);
        }
    }

    /**
     * 誘電体ｽﾗﾘｰ重量2計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setYuudentaislurryjyuurou2(ProcessData processData) {
        FXHDD01 itemYuudentaislurryjyuurou = getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU2); // 誘電体ｽﾗﾘｰ重量2
        FXHDD01 itemSoujyuurousokutei = getItemRow(processData.getItemList(), GXHDO102B026Const.SOUJYUUROUSOKUTEI2); // 総重量測定2
        FXHDD01 itemFuutaijyuuryousokutei_sutenyouki = getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI2); // 風袋重量測定_ｽﾃﾝ容器2
        FXHDD01 itemFuutaijyuuryousokutei_siropori = getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI2); // 風袋重量測定_白ﾎﾟﾘ容器2
        if (itemYuudentaislurryjyuurou == null || itemSoujyuurousokutei == null || itemFuutaijyuuryousokutei_sutenyouki == null || itemFuutaijyuuryousokutei_siropori == null) {
            processData.setMethod("");
            return processData;
        }
        // 【誘電体ｽﾗﾘｰ重量2計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkYuudentaislurryjyuurouKeisan(itemSoujyuurousokutei, itemFuutaijyuuryousokutei_sutenyouki, itemFuutaijyuuryousokutei_siropori);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        // 誘電体ｽﾗﾘｰ重量2計算処理
        calcYuudentaislurryjyuurou(processData, itemYuudentaislurryjyuurou, itemSoujyuurousokutei, itemFuutaijyuuryousokutei_sutenyouki, itemFuutaijyuuryousokutei_siropori);
        return processData;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量3計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setYuudentaislurryjyuurou3(ProcessData processData) {
        FXHDD01 itemYuudentaislurryjyuurou = getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU3); // 誘電体ｽﾗﾘｰ重量3
        FXHDD01 itemSoujyuurousokutei = getItemRow(processData.getItemList(), GXHDO102B026Const.SOUJYUUROUSOKUTEI3); // 総重量測定3
        FXHDD01 itemFuutaijyuuryousokutei_sutenyouki = getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI3); // 風袋重量測定_ｽﾃﾝ容器3
        FXHDD01 itemFuutaijyuuryousokutei_siropori = getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI3); // 風袋重量測定_白ﾎﾟﾘ容器3
        if (itemYuudentaislurryjyuurou == null || itemSoujyuurousokutei == null || itemFuutaijyuuryousokutei_sutenyouki == null || itemFuutaijyuuryousokutei_siropori == null) {
            processData.setMethod("");
            return processData;
        }
        // 【誘電体ｽﾗﾘｰ重量3計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkYuudentaislurryjyuurouKeisan(itemSoujyuurousokutei, itemFuutaijyuuryousokutei_sutenyouki, itemFuutaijyuuryousokutei_siropori);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        // 誘電体ｽﾗﾘｰ重量3計算処理
        calcYuudentaislurryjyuurou(processData, itemYuudentaislurryjyuurou, itemSoujyuurousokutei, itemFuutaijyuuryousokutei_sutenyouki, itemFuutaijyuuryousokutei_siropori);
        return processData;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量4計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setYuudentaislurryjyuurou4(ProcessData processData) {
        FXHDD01 itemYuudentaislurryjyuurou = getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU4); // 誘電体ｽﾗﾘｰ重量4
        FXHDD01 itemSoujyuurousokutei = getItemRow(processData.getItemList(), GXHDO102B026Const.SOUJYUUROUSOKUTEI4); // 総重量測定4
        FXHDD01 itemFuutaijyuuryousokutei_sutenyouki = getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI4); // 風袋重量測定_ｽﾃﾝ容器4
        FXHDD01 itemFuutaijyuuryousokutei_siropori = getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI4); // 風袋重量測定_白ﾎﾟﾘ容器4
        if (itemYuudentaislurryjyuurou == null || itemSoujyuurousokutei == null || itemFuutaijyuuryousokutei_sutenyouki == null || itemFuutaijyuuryousokutei_siropori == null) {
            processData.setMethod("");
            return processData;
        }
        // 【誘電体ｽﾗﾘｰ重量4計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkYuudentaislurryjyuurouKeisan(itemSoujyuurousokutei, itemFuutaijyuuryousokutei_sutenyouki, itemFuutaijyuuryousokutei_siropori);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        // 誘電体ｽﾗﾘｰ重量4計算処理
        calcYuudentaislurryjyuurou(processData, itemYuudentaislurryjyuurou, itemSoujyuurousokutei, itemFuutaijyuuryousokutei_sutenyouki, itemFuutaijyuuryousokutei_siropori);
        return processData;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量5計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setYuudentaislurryjyuurou5(ProcessData processData) {
        FXHDD01 itemYuudentaislurryjyuurou = getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU5); // 誘電体ｽﾗﾘｰ重量5
        FXHDD01 itemSoujyuurousokutei = getItemRow(processData.getItemList(), GXHDO102B026Const.SOUJYUUROUSOKUTEI5); // 総重量測定5
        FXHDD01 itemFuutaijyuuryousokutei_sutenyouki = getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI5); // 風袋重量測定_ｽﾃﾝ容器5
        FXHDD01 itemFuutaijyuuryousokutei_siropori = getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI5); // 風袋重量測定_白ﾎﾟﾘ容器5
        if (itemYuudentaislurryjyuurou == null || itemSoujyuurousokutei == null || itemFuutaijyuuryousokutei_sutenyouki == null || itemFuutaijyuuryousokutei_siropori == null) {
            processData.setMethod("");
            return processData;
        }
        // 【誘電体ｽﾗﾘｰ重量5計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkYuudentaislurryjyuurouKeisan(itemSoujyuurousokutei, itemFuutaijyuuryousokutei_sutenyouki, itemFuutaijyuuryousokutei_siropori);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        // 誘電体ｽﾗﾘｰ重量5計算処理
        calcYuudentaislurryjyuurou(processData, itemYuudentaislurryjyuurou, itemSoujyuurousokutei, itemFuutaijyuuryousokutei_sutenyouki, itemFuutaijyuuryousokutei_siropori);
        return processData;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量6計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setYuudentaislurryjyuurou6(ProcessData processData) {
        FXHDD01 itemYuudentaislurryjyuurou = getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU6); // 誘電体ｽﾗﾘｰ重量6
        FXHDD01 itemSoujyuurousokutei = getItemRow(processData.getItemList(), GXHDO102B026Const.SOUJYUUROUSOKUTEI6); // 総重量測定6
        FXHDD01 itemFuutaijyuuryousokutei_sutenyouki = getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI6); // 風袋重量測定_ｽﾃﾝ容器6
        FXHDD01 itemFuutaijyuuryousokutei_siropori = getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI6); // 風袋重量測定_白ﾎﾟﾘ容器6
        if (itemYuudentaislurryjyuurou == null || itemSoujyuurousokutei == null || itemFuutaijyuuryousokutei_sutenyouki == null || itemFuutaijyuuryousokutei_siropori == null) {
            processData.setMethod("");
            return processData;
        }
        // 【誘電体ｽﾗﾘｰ重量6計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkYuudentaislurryjyuurouKeisan(itemSoujyuurousokutei, itemFuutaijyuuryousokutei_sutenyouki, itemFuutaijyuuryousokutei_siropori);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        // 誘電体ｽﾗﾘｰ重量6計算処理
        calcYuudentaislurryjyuurou(processData, itemYuudentaislurryjyuurou, itemSoujyuurousokutei, itemFuutaijyuuryousokutei_sutenyouki, itemFuutaijyuuryousokutei_siropori);
        return processData;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量7計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setYuudentaislurryjyuurou7(ProcessData processData) {
        FXHDD01 itemYuudentaislurryjyuurou = getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU7); // 誘電体ｽﾗﾘｰ重量7
        FXHDD01 itemSoujyuurousokutei = getItemRow(processData.getItemList(), GXHDO102B026Const.SOUJYUUROUSOKUTEI7); // 総重量測定7
        FXHDD01 itemFuutaijyuuryousokutei_siropori = getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI7); // 風袋重量測定_白ﾎﾟﾘ容器7
        if (itemYuudentaislurryjyuurou == null || itemSoujyuurousokutei == null || itemFuutaijyuuryousokutei_siropori == null) {
            processData.setMethod("");
            return processData;
        }
        // 【誘電体ｽﾗﾘｰ重量7計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkYuudentaislurryjyuurouKeisan(itemSoujyuurousokutei, null, itemFuutaijyuuryousokutei_siropori);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        // 誘電体ｽﾗﾘｰ重量7計算処理
        calcYuudentaislurryjyuurou(processData, itemYuudentaislurryjyuurou, itemSoujyuurousokutei, null, itemFuutaijyuuryousokutei_siropori);
        return processData;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量8計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setYuudentaislurryjyuurou8(ProcessData processData) {
        FXHDD01 itemYuudentaislurryjyuurou = getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU8); // 誘電体ｽﾗﾘｰ重量8
        FXHDD01 itemSoujyuurousokutei = getItemRow(processData.getItemList(), GXHDO102B026Const.SOUJYUUROUSOKUTEI8); // 総重量測定8
        FXHDD01 itemFuutaijyuuryousokutei_siropori = getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI8); // 風袋重量測定_白ﾎﾟﾘ容器8
        if (itemYuudentaislurryjyuurou == null || itemSoujyuurousokutei == null || itemFuutaijyuuryousokutei_siropori == null) {
            processData.setMethod("");
            return processData;
        }
        // 【誘電体ｽﾗﾘｰ重量8計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkYuudentaislurryjyuurouKeisan(itemSoujyuurousokutei, null, itemFuutaijyuuryousokutei_siropori);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        // 誘電体ｽﾗﾘｰ重量8計算処理
        calcYuudentaislurryjyuurou(processData, itemYuudentaislurryjyuurou, itemSoujyuurousokutei, null, itemFuutaijyuuryousokutei_siropori);
        return processData;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量9計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setYuudentaislurryjyuurou9(ProcessData processData) {
        FXHDD01 itemYuudentaislurryjyuurou = getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU9); // 誘電体ｽﾗﾘｰ重量9
        FXHDD01 itemSoujyuurousokutei = getItemRow(processData.getItemList(), GXHDO102B026Const.SOUJYUUROUSOKUTEI9); // 総重量測定9
        FXHDD01 itemFuutaijyuuryousokutei_siropori = getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI9); // 風袋重量測定_白ﾎﾟﾘ容器9
        if (itemYuudentaislurryjyuurou == null || itemSoujyuurousokutei == null || itemFuutaijyuuryousokutei_siropori == null) {
            processData.setMethod("");
            return processData;
        }
        // 【誘電体ｽﾗﾘｰ重量9計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkYuudentaislurryjyuurouKeisan(itemSoujyuurousokutei, null, itemFuutaijyuuryousokutei_siropori);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        // 誘電体ｽﾗﾘｰ重量9計算処理
        calcYuudentaislurryjyuurou(processData, itemYuudentaislurryjyuurou, itemSoujyuurousokutei, null, itemFuutaijyuuryousokutei_siropori);
        return processData;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量10計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setYuudentaislurryjyuurou10(ProcessData processData) {
        FXHDD01 itemYuudentaislurryjyuurou = getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU10); // 誘電体ｽﾗﾘｰ重量10
        FXHDD01 itemSoujyuurousokutei = getItemRow(processData.getItemList(), GXHDO102B026Const.SOUJYUUROUSOKUTEI10); // 総重量測定10
        FXHDD01 itemFuutaijyuuryousokutei_siropori = getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI10); // 風袋重量測定_白ﾎﾟﾘ容器10
        if (itemYuudentaislurryjyuurou == null || itemSoujyuurousokutei == null || itemFuutaijyuuryousokutei_siropori == null) {
            processData.setMethod("");
            return processData;
        }
        // 【誘電体ｽﾗﾘｰ重量10計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkYuudentaislurryjyuurouKeisan(itemSoujyuurousokutei, null, itemFuutaijyuuryousokutei_siropori);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        // 誘電体ｽﾗﾘｰ重量10計算処理
        calcYuudentaislurryjyuurou(processData, itemYuudentaislurryjyuurou, itemSoujyuurousokutei, null, itemFuutaijyuuryousokutei_siropori);
        return processData;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量11計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setYuudentaislurryjyuurou11(ProcessData processData) {
        FXHDD01 itemYuudentaislurryjyuurou = getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU11); // 誘電体ｽﾗﾘｰ重量11
        FXHDD01 itemSoujyuurousokutei = getItemRow(processData.getItemList(), GXHDO102B026Const.SOUJYUUROUSOKUTEI11); // 総重量測定11
        FXHDD01 itemFuutaijyuuryousokutei_siropori = getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI11); // 風袋重量測定_白ﾎﾟﾘ容器11
        if (itemYuudentaislurryjyuurou == null || itemSoujyuurousokutei == null || itemFuutaijyuuryousokutei_siropori == null) {
            processData.setMethod("");
            return processData;
        }
        // 【誘電体ｽﾗﾘｰ重量11計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkYuudentaislurryjyuurouKeisan(itemSoujyuurousokutei, null, itemFuutaijyuuryousokutei_siropori);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        // 誘電体ｽﾗﾘｰ重量11計算処理
        calcYuudentaislurryjyuurou(processData, itemYuudentaislurryjyuurou, itemSoujyuurousokutei, null, itemFuutaijyuuryousokutei_siropori);
        return processData;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量12計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setYuudentaislurryjyuurou12(ProcessData processData) {
        FXHDD01 itemYuudentaislurryjyuurou = getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU12); // 誘電体ｽﾗﾘｰ重量12
        FXHDD01 itemSoujyuurousokutei = getItemRow(processData.getItemList(), GXHDO102B026Const.SOUJYUUROUSOKUTEI12); // 総重量測定12
        FXHDD01 itemFuutaijyuuryousokutei_siropori = getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI12); // 風袋重量測定_白ﾎﾟﾘ容器12
        if (itemYuudentaislurryjyuurou == null || itemSoujyuurousokutei == null || itemFuutaijyuuryousokutei_siropori == null) {
            processData.setMethod("");
            return processData;
        }
        // 【誘電体ｽﾗﾘｰ重量12計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkYuudentaislurryjyuurouKeisan(itemSoujyuurousokutei, null, itemFuutaijyuuryousokutei_siropori);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        // 誘電体ｽﾗﾘｰ重量12計算処理
        calcYuudentaislurryjyuurou(processData, itemYuudentaislurryjyuurou, itemSoujyuurousokutei, null, itemFuutaijyuuryousokutei_siropori);
        return processData;
    }

    /**
     * 歩留まり計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setBudomarikeisan(ProcessData processData) {
        // 「誘電体ｽﾗﾘｰ重量合計」
        FXHDD01 itemYuudentaislurryjyuurougoukei = getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROUGOUKEI);
        // 投入量
        FXHDD01 itemTounyuuryou = getItemRow(processData.getItemList(), GXHDO102B026Const.TOUNYUURYOU);
        if (itemYuudentaislurryjyuurougoukei != null && itemTounyuuryou != null) {
            // 歩留まり計算チェック処理
            ErrorMessageInfo checkItemErrorInfo = checkBudomariKeisan(itemYuudentaislurryjyuurougoukei, itemTounyuuryou);
            if (checkItemErrorInfo != null) {
                processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
                return processData;
            }
            //歩留まり計算処理
            calcBudomari(processData, itemYuudentaislurryjyuurougoukei, itemTounyuuryou);
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 【歩留まり計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
     *
     * @param itemYuudentaislurryjyuurougoukei 誘電体ｽﾗﾘｰ重量合計
     * @param itemTounyuuryou 投入量
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkBudomariKeisan(FXHDD01 itemYuudentaislurryjyuurougoukei, FXHDD01 itemTounyuuryou) {
        // 投入量の規格値
        BigDecimal itemTounyuuryouVal = ValidateUtil.getItemKikakuChiCheckVal(itemTounyuuryou);
        // 「誘電体ｽﾗﾘｰ重量合計」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemYuudentaislurryjyuurougoukei.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemYuudentaislurryjyuurougoukei);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, true, errFxhdd01List, itemYuudentaislurryjyuurougoukei.getLabel1());
        }
        // 「投入量の規格値」ﾁｪｯｸ
        if (itemTounyuuryouVal == null || BigDecimal.ZERO.compareTo(itemTounyuuryouVal) == 0) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemTounyuuryou);
            itemTounyuuryou.setBackColor3(ErrUtil.ERR_BACK_COLOR);
            return MessageUtil.getErrorMessageInfo("XHD-000181", true, true, errFxhdd01List, itemTounyuuryou.getLabel1());
        }
        return null;
    }

    /**
     * 歩留まり計算
     *
     * @param processData 処理制御データ
     * @param itemYuudentaislurryjyuurougoukei 誘電体ｽﾗﾘｰ重量合計
     * @param itemTounyuuryou 投入量
     */
    private void calcBudomari(ProcessData processData, FXHDD01 itemYuudentaislurryjyuurougoukei, FXHDD01 itemTounyuuryou) {
        try {
            FXHDD01 itemBudomarikeisan = getItemRow(processData.getItemList(), GXHDO102B026Const.BUDOMARIKEISAN); // 歩留まり
            BigDecimal itemGarasukaisyuujyuuryouVal = new BigDecimal(itemYuudentaislurryjyuurougoukei.getValue()); // 誘電体ｽﾗﾘｰ重量合計
            // 投入量の規格値
            BigDecimal itemTounyuuryouVal = ValidateUtil.getItemKikakuChiCheckVal(itemTounyuuryou);

            // 「誘電体ｽﾗﾘｰ重量合計」 ÷ 「投入量の規格値」 * 100(小数点第三位を四捨五入) → 式を変換して先に100を乗算
            BigDecimal budomari = itemGarasukaisyuujyuuryouVal.multiply(BigDecimal.valueOf(100)).divide(itemTounyuuryouVal, 2, RoundingMode.HALF_UP);

            //計算結果を歩留まりにセット
            itemBudomarikeisan.setValue(budomari.toPlainString());

        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
            ErrUtil.outputErrorLog("歩留まり計算にエラー発生", ex, LOGGER);
        }
    }

    /**
     * 誘電体ｽﾗﾘｰ有効期限計算
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setUudentaislurryyuukoukigen(ProcessData processData) {
        Map sryuudentaifunsaiData = new HashMap<>();
        // 誘電体ｽﾗﾘｰ有効期限計算ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkUudentaislurryyuukoukigenKeisan(processData, sryuudentaifunsaiData);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        // 誘電体ｽﾗﾘｰ有効期限計算処理
        calcUudentaislurryyuukoukigen(processData, sryuudentaifunsaiData);
        return processData;
    }

    /**
     * 誘電体ｽﾗﾘｰ有効期限計算ﾁｪｯｸ処理
     *
     * @param processData 処理制御データ
     * @param sryuudentaifunsaiData 粉砕終了日
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkUudentaislurryyuukoukigenKeisan(ProcessData processData, Map sryuudentaifunsaiData) {
        QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
        try {
            // セッションから情報を取得
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            String lotNo = (String) session.getAttribute("lotNo");
            String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
            String lotNo9 = lotNo.substring(3, 12); //ﾛｯﾄNo
            String edaban = lotNo.substring(12, 15); //枝番
            // [誘電体ｽﾗﾘｰ作製・粉砕]から、ﾃﾞｰﾀの取得
            Map sryuudentaifunsaiMap = loadSryuudentaifunsaiData(queryRunnerQcdb, kojyo, lotNo9, edaban);
            if (sryuudentaifunsaiMap == null || sryuudentaifunsaiMap.isEmpty() || !sryuudentaifunsaiMap.containsKey("syuuryounichiji")
                    || ((Timestamp) sryuudentaifunsaiMap.get("syuuryounichiji")) == null) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                return MessageUtil.getErrorMessageInfo("XHD-000181", true, true, null, "粉砕終了日取得");
            } else {
                sryuudentaifunsaiData.put("syuuryounichiji", sryuudentaifunsaiMap.get("syuuryounichiji"));
            }
        } catch (SQLException e) {
            ErrUtil.outputErrorLog("SQLException発生", e, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
        }
        return null;
    }

    /**
     * 誘電体ｽﾗﾘｰ有効期限計算処理
     *
     * @param processData 処理制御データ
     * @param sryuudentaifunsaiData 粉砕終了日
     */
    private void calcUudentaislurryyuukoukigen(ProcessData processData, Map sryuudentaifunsaiData) {
        try {
            FXHDD01 itemUudentaislurryyuukoukigen = getItemRow(processData.getItemList(), GXHDO102B026Const.UUDENTAISLURRYYUUKOUKIGEN); // 誘電体ｽﾗﾘｰ有効期限
            BigDecimal itemUudentaislurryyuukoukigenKikakuChi = ValidateUtil.numberExtraction(StringUtil.nullToBlank(itemUudentaislurryyuukoukigen.getKikakuChi()).replace("【", "").replace("】", ""));
            String syuuryounichijiVal = DateUtil.formattedTimestamp((Timestamp) sryuudentaifunsaiData.get("syuuryounichiji"), "yyMMdd");
            // 粉砕終了日 ＋ 規格値
            Date dateTime = DateUtil.addJikan(syuuryounichijiVal, "0000", itemUudentaislurryyuukoukigenKikakuChi.intValue(), Calendar.DATE);
            if (dateTime != null) {
                itemUudentaislurryyuukoukigen.setValue(new SimpleDateFormat("yyMMdd").format(dateTime));
            }
        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
            ErrUtil.outputErrorLog("誘電体ｽﾗﾘｰ有効期限計算にエラー発生", ex, LOGGER);
        }
    }

    /**
     * 乾燥後正味重量計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKansougosyoumijyuuryou(ProcessData processData) {
        // 乾燥後総重量
        FXHDD01 kansougosoujyuuryou = getItemRow(processData.getItemList(), GXHDO102B026Const.KANSOUGOSOUJYUURYOU);
        // ｱﾙﾐ皿風袋重量
        FXHDD01 arumizarafuutaijyuuryou = getItemRow(processData.getItemList(), GXHDO102B026Const.ARUMIZARAFUUTAIJYUURYOU);
        // 乾燥前ｽﾗﾘｰ重量
        FXHDD01 kansoumaeslurryjyuuryou = getItemRow(processData.getItemList(), GXHDO102B026Const.KANSOUMAESLURRYJYUURYOU);
        if (kansougosoujyuuryou == null || arumizarafuutaijyuuryou == null || kansoumaeslurryjyuuryou == null) {
            processData.setMethod("");
            return processData;
        }
        ErrorMessageInfo checkItemErrorInfo = checkSyoumijyuuryouKeisan(kansougosoujyuuryou, arumizarafuutaijyuuryou, kansoumaeslurryjyuuryou);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        syoumijyuuryouKeisan(processData, kansougosoujyuuryou, arumizarafuutaijyuuryou, kansoumaeslurryjyuuryou);
        return processData;
    }

    /**
     * 乾燥後正味重量計算
     *
     * @param processData 処理制御データ
     * @param kansougosoujyuuryou 乾燥後総重量
     * @param arumizarafuutaijyuuryou ｱﾙﾐ皿風袋重量
     * @param kansoumaeslurryjyuuryou 乾燥前ｽﾗﾘｰ重量
     */
    private void syoumijyuuryouKeisan(ProcessData processData, FXHDD01 kansougosoujyuuryou, FXHDD01 arumizarafuutaijyuuryou, FXHDD01 kansoumaeslurryjyuuryou) {
        try {
            // 乾燥後正味重量
            FXHDD01 kansougosyoumijyuuryou = getItemRow(processData.getItemList(), GXHDO102B026Const.KANSOUGOSYOUMIJYUURYOU);
            //「乾燥後正味重量」= 「乾燥後総重量」 - 「ｱﾙﾐ皿風袋重量」 を算出する。
            BigDecimal itemKansougosoujyuuryouVal = new BigDecimal(kansougosoujyuuryou.getValue());
            BigDecimal itemArumizarafuutaijyuuryouVal = new BigDecimal(arumizarafuutaijyuuryou.getValue());
            BigDecimal itemSyoumijyuuryouVal = itemKansougosoujyuuryouVal.subtract(itemArumizarafuutaijyuuryouVal);
            //計算結果の設定
            kansougosyoumijyuuryou.setValue(itemSyoumijyuuryouVal.toPlainString());

            // 「固形分比率」計算処理
            // 固形分比率
            FXHDD01 kokeibunhiritu = getItemRow(processData.getItemList(), GXHDO102B026Const.KOKEIBUNHIRITU);
            BigDecimal itemKansoumaeslurryjyuuryouVal = new BigDecimal(kansoumaeslurryjyuuryou.getValue());
            // 「固形分比率」= 「乾燥後正味重量」 ÷ 「乾燥前ｽﾗﾘｰ重量」 * 100(小数点第四位を四捨五入) → 式を変換して先に100を乗算
            BigDecimal kokeibunhirituVal = itemSyoumijyuuryouVal.multiply(BigDecimal.valueOf(100)).divide(itemKansoumaeslurryjyuuryouVal, 3, RoundingMode.HALF_UP);
            //計算結果の設定
            kokeibunhiritu.setValue(kokeibunhirituVal.toPlainString());
        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
            ErrUtil.outputErrorLog("乾燥後正味重量計算にエラー発生", ex, LOGGER);
        }
    }

    /**
     * 乾燥後正味重量計算ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
     *
     * @param kansougosoujyuuryou 乾燥後総重量
     * @param arumizarafuutaijyuuryou ｱﾙﾐ皿風袋重量
     * @param kansoumaeslurryjyuuryou 乾燥前ｽﾗﾘｰ重量
     * @return エラーメッセージ情報
     */
    public ErrorMessageInfo checkSyoumijyuuryouKeisan(FXHDD01 kansougosoujyuuryou, FXHDD01 arumizarafuutaijyuuryou, FXHDD01 kansoumaeslurryjyuuryou) {

        //「乾燥後総重量」ﾁｪｯｸ
        if (StringUtil.isEmpty(kansougosoujyuuryou.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(kansougosoujyuuryou);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, true, errFxhdd01List, kansougosoujyuuryou.getLabel1());
        }
        //「ｱﾙﾐ皿風袋重量」ﾁｪｯｸ
        if (StringUtil.isEmpty(arumizarafuutaijyuuryou.getValue())) {
            List<FXHDD01> errFxhdd01List = Arrays.asList(arumizarafuutaijyuuryou);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, true, errFxhdd01List, arumizarafuutaijyuuryou.getLabel1());
        }
        // [乾燥後総重量]<[ｱﾙﾐ皿風袋重量]場合
        BigDecimal kansougosoujyuuryouVal = new BigDecimal(kansougosoujyuuryou.getValue());
        BigDecimal arumizarafuutaijyuuryouVal = new BigDecimal(arumizarafuutaijyuuryou.getValue());
        if (kansougosoujyuuryouVal.compareTo(arumizarafuutaijyuuryouVal) < 0) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(kansougosoujyuuryou, arumizarafuutaijyuuryou);
            return MessageUtil.getErrorMessageInfo("XHD-000023", true, true, errFxhdd01List, kansougosoujyuuryou.getLabel1(), arumizarafuutaijyuuryou.getLabel1());
        }
        // 「乾燥前ｽﾗﾘｰ重量」ﾁｪｯｸ
        if ("".equals(StringUtil.nullToBlank(kansoumaeslurryjyuuryou.getValue())) || BigDecimal.ZERO.compareTo(new BigDecimal(kansoumaeslurryjyuuryou.getValue())) == 0) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(kansoumaeslurryjyuuryou);
            return MessageUtil.getErrorMessageInfo("XHD-000181", true, true, errFxhdd01List, kansoumaeslurryjyuuryou.getLabel1());
        }
        return null;
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
                activeIdList.addAll(Arrays.asList(GXHDO102B026Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B026Const.BTN_FPKAISINICHIJI_TOP,
                        GXHDO102B026Const.BTN_FILTERKOUKAN1_FPTEISI_TOP,
                        GXHDO102B026Const.BTN_FILTERKOUKAN1_FPSAIKAI_TOP,
                        GXHDO102B026Const.BTN_FPSYUURYOUNICHIJI_TOP,
                        GXHDO102B026Const.BTN_ITIJIFILTERSOUSIYOUHONSUU_TOP,
                        GXHDO102B026Const.BTN_NIJIFILTERSOUSIYOUHONSUU_TOP,
                        GXHDO102B026Const.BTN_SANJIFILTERSOUSIYOUHONSUU_TOP,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU1_TOP,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU2_TOP,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU3_TOP,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU4_TOP,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU5_TOP,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU6_TOP,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU7_TOP,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU8_TOP,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU9_TOP,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU10_TOP,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU11_TOP,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU12_TOP,
                        GXHDO102B026Const.BTN_BUDOMARIKEISAN_TOP,
                        GXHDO102B026Const.BTN_UUDENTAISLURRYYUUKOUKIGEN_TOP,
                        GXHDO102B026Const.BTN_KANSOUKAISI_TOP,
                        GXHDO102B026Const.BTN_KANSOUSYUURYOU_TOP,
                        GXHDO102B026Const.BTN_KANSOUGOSYOUMIJYUURYOU_TOP,
                        GXHDO102B026Const.BTN_UPDATE_TOP,
                        GXHDO102B026Const.BTN_DELETE_TOP,
                        GXHDO102B026Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B026Const.BTN_FPKAISINICHIJI_BOTTOM,
                        GXHDO102B026Const.BTN_FILTERKOUKAN1_FPTEISI_BOTTOM,
                        GXHDO102B026Const.BTN_FILTERKOUKAN1_FPSAIKAI_BOTTOM,
                        GXHDO102B026Const.BTN_FPSYUURYOUNICHIJI_BOTTOM,
                        GXHDO102B026Const.BTN_ITIJIFILTERSOUSIYOUHONSUU_BOTTOM,
                        GXHDO102B026Const.BTN_NIJIFILTERSOUSIYOUHONSUU_BOTTOM,
                        GXHDO102B026Const.BTN_SANJIFILTERSOUSIYOUHONSUU_BOTTOM,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU1_BOTTOM,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU2_BOTTOM,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU3_BOTTOM,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU4_BOTTOM,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU5_BOTTOM,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU6_BOTTOM,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU7_BOTTOM,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU8_BOTTOM,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU9_BOTTOM,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU10_BOTTOM,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU11_BOTTOM,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU12_BOTTOM,
                        GXHDO102B026Const.BTN_BUDOMARIKEISAN_BOTTOM,
                        GXHDO102B026Const.BTN_UUDENTAISLURRYYUUKOUKIGEN_BOTTOM,
                        GXHDO102B026Const.BTN_KANSOUKAISI_BOTTOM,
                        GXHDO102B026Const.BTN_KANSOUSYUURYOU_BOTTOM,
                        GXHDO102B026Const.BTN_KANSOUGOSYOUMIJYUURYOU_BOTTOM,
                        GXHDO102B026Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B026Const.BTN_DELETE_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B026Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B026Const.BTN_INSERT_TOP,
                        GXHDO102B026Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B026Const.BTN_INSERT_BOTTOM));

                break;
            default:
                activeIdList.addAll(Arrays.asList(GXHDO102B026Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B026Const.BTN_FPKAISINICHIJI_TOP,
                        GXHDO102B026Const.BTN_FILTERKOUKAN1_FPTEISI_TOP,
                        GXHDO102B026Const.BTN_FILTERKOUKAN1_FPSAIKAI_TOP,
                        GXHDO102B026Const.BTN_FPSYUURYOUNICHIJI_TOP,
                        GXHDO102B026Const.BTN_ITIJIFILTERSOUSIYOUHONSUU_TOP,
                        GXHDO102B026Const.BTN_NIJIFILTERSOUSIYOUHONSUU_TOP,
                        GXHDO102B026Const.BTN_SANJIFILTERSOUSIYOUHONSUU_TOP,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU1_TOP,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU2_TOP,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU3_TOP,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU4_TOP,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU5_TOP,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU6_TOP,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU7_TOP,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU8_TOP,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU9_TOP,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU10_TOP,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU11_TOP,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU12_TOP,
                        GXHDO102B026Const.BTN_BUDOMARIKEISAN_TOP,
                        GXHDO102B026Const.BTN_UUDENTAISLURRYYUUKOUKIGEN_TOP,
                        GXHDO102B026Const.BTN_KANSOUKAISI_TOP,
                        GXHDO102B026Const.BTN_KANSOUSYUURYOU_TOP,
                        GXHDO102B026Const.BTN_KANSOUGOSYOUMIJYUURYOU_TOP,
                        GXHDO102B026Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B026Const.BTN_INSERT_TOP,
                        GXHDO102B026Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B026Const.BTN_FPKAISINICHIJI_BOTTOM,
                        GXHDO102B026Const.BTN_FILTERKOUKAN1_FPTEISI_BOTTOM,
                        GXHDO102B026Const.BTN_FILTERKOUKAN1_FPSAIKAI_BOTTOM,
                        GXHDO102B026Const.BTN_FPSYUURYOUNICHIJI_BOTTOM,
                        GXHDO102B026Const.BTN_ITIJIFILTERSOUSIYOUHONSUU_BOTTOM,
                        GXHDO102B026Const.BTN_NIJIFILTERSOUSIYOUHONSUU_BOTTOM,
                        GXHDO102B026Const.BTN_SANJIFILTERSOUSIYOUHONSUU_BOTTOM,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU1_BOTTOM,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU2_BOTTOM,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU3_BOTTOM,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU4_BOTTOM,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU5_BOTTOM,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU6_BOTTOM,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU7_BOTTOM,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU8_BOTTOM,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU9_BOTTOM,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU10_BOTTOM,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU11_BOTTOM,
                        GXHDO102B026Const.BTN_YUUDENTAISLURRYJYUUROU12_BOTTOM,
                        GXHDO102B026Const.BTN_BUDOMARIKEISAN_BOTTOM,
                        GXHDO102B026Const.BTN_UUDENTAISLURRYYUUKOUKIGEN_BOTTOM,
                        GXHDO102B026Const.BTN_KANSOUKAISI_BOTTOM,
                        GXHDO102B026Const.BTN_KANSOUSYUURYOU_BOTTOM,
                        GXHDO102B026Const.BTN_KANSOUGOSYOUMIJYUURYOU_BOTTOM,
                        GXHDO102B026Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B026Const.BTN_INSERT_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B026Const.BTN_UPDATE_TOP,
                        GXHDO102B026Const.BTN_DELETE_TOP,
                        GXHDO102B026Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B026Const.BTN_DELETE_BOTTOM
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
        // 画面に取得した情報をセットする。(入力項目以外)
        setViewItemData(processData, shikakariData, lotNo);
        // 画面のラベル項目の値の背景色を取得できない場合、デフォルト値を設置
        processData.getItemList().stream().map((item) -> {
            if (item.isRender1() || item.isRenderLinkButton()) {
                if ("".equals(StringUtil.nullToBlank(item.getBackColor3()))) {
                    item.setBackColor3("#EEEEEE");
                }
                if (0 == item.getFontSize3()) {
                    item.setFontSize3(16);
                }
            }
            return item;
        }).filter((item) -> (item.isRenderOutputLabel() && !item.isRenderInputText())).map((item) -> {
            if ("".equals(StringUtil.nullToBlank(item.getBackColorInput()))) {
                item.setBackColorInput("#EEEEEE");
                item.setBackColorInputDefault("#EEEEEE");
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
     * @param sekkeiData 設計データ
     * @param ownerMasData ｵｰﾅｰﾏｽﾀデータ
     * @param shikakariData 仕掛データ
     * @param lotNo ﾛｯﾄNo
     */
    private void setViewItemData(ProcessData processData, Map shikakariData, String lotNo) {

        // WIPﾛｯﾄNo
        this.setItemData(processData, GXHDO102B026Const.WIPLOTNO, lotNo);
        // 誘電体ｽﾗﾘｰ品名
        this.setItemData(processData, GXHDO102B026Const.YUUDENTAISLURRYHINMEI, StringUtil.nullToBlank(getMapData(shikakariData, "hinmei")));
        // 誘電体ｽﾗﾘｰLotNo
        this.setItemData(processData, GXHDO102B026Const.YUUDENTAISLURRYLOTNO, StringUtil.nullToBlank(getMapData(shikakariData, "lotno")));
        // ﾛｯﾄ区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode"));
        // ﾛｯﾄ区分名称
        String lotkubun = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubun"));

        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO102B026Const.LOTKUBUN, "");
        } else {
            if (!StringUtil.isEmpty(lotkubun)) {
                lotkubuncode = lotkubuncode + ":" + lotkubun;
            }
            this.setItemData(processData, GXHDO102B026Const.LOTKUBUN, lotkubuncode);
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

        List<SrYuudentaiFp> srYuudentaiFpList = new ArrayList<>();
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

                return true;
            }

            // 誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管データ取得
            srYuudentaiFpList = getSrYuudentaiFpData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo9, edaban);
            if (srYuudentaiFpList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srYuudentaiFpList.isEmpty()) {
            return false;
        }
        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srYuudentaiFpList.get(0));
        return true;

    }

    /**
     * データ設定処理
     *
     * @param processData 処理制御データ
     * @param srYuudentaiFp 誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管
     */
    private void setInputItemDataMainForm(ProcessData processData, SrYuudentaiFp srYuudentaiFp) {
        // 風袋重量測定_ｽﾃﾝ容器1
        this.setItemData(processData, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI1, getSrYuudentaiFpItemData(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI1, srYuudentaiFp));

        // 風袋重量測定_ｽﾃﾝ容器2
        this.setItemData(processData, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI2, getSrYuudentaiFpItemData(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI2, srYuudentaiFp));

        // 風袋重量測定_ｽﾃﾝ容器3
        this.setItemData(processData, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI3, getSrYuudentaiFpItemData(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI3, srYuudentaiFp));

        // 風袋重量測定_ｽﾃﾝ容器4
        this.setItemData(processData, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI4, getSrYuudentaiFpItemData(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI4, srYuudentaiFp));

        // 風袋重量測定_ｽﾃﾝ容器5
        this.setItemData(processData, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI5, getSrYuudentaiFpItemData(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI5, srYuudentaiFp));

        // 風袋重量測定_ｽﾃﾝ容器6
        this.setItemData(processData, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI6, getSrYuudentaiFpItemData(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI6, srYuudentaiFp));

        // 風袋重量測定_白ﾎﾟﾘ容器1
        this.setItemData(processData, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI1, getSrYuudentaiFpItemData(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI1, srYuudentaiFp));

        // 風袋重量測定_白ﾎﾟﾘ容器2
        this.setItemData(processData, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI2, getSrYuudentaiFpItemData(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI2, srYuudentaiFp));

        // 風袋重量測定_白ﾎﾟﾘ容器3
        this.setItemData(processData, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI3, getSrYuudentaiFpItemData(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI3, srYuudentaiFp));

        // 風袋重量測定_白ﾎﾟﾘ容器4
        this.setItemData(processData, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI4, getSrYuudentaiFpItemData(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI4, srYuudentaiFp));

        // 風袋重量測定_白ﾎﾟﾘ容器5
        this.setItemData(processData, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI5, getSrYuudentaiFpItemData(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI5, srYuudentaiFp));

        // 風袋重量測定_白ﾎﾟﾘ容器6
        this.setItemData(processData, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI6, getSrYuudentaiFpItemData(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI6, srYuudentaiFp));

        // 風袋重量測定_白ﾎﾟﾘ容器7
        this.setItemData(processData, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI7, getSrYuudentaiFpItemData(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI7, srYuudentaiFp));

        // 風袋重量測定_白ﾎﾟﾘ容器8
        this.setItemData(processData, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI8, getSrYuudentaiFpItemData(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI8, srYuudentaiFp));

        // 風袋重量測定_白ﾎﾟﾘ容器9
        this.setItemData(processData, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI9, getSrYuudentaiFpItemData(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI9, srYuudentaiFp));

        // 風袋重量測定_白ﾎﾟﾘ容器10
        this.setItemData(processData, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI10, getSrYuudentaiFpItemData(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI10, srYuudentaiFp));

        // 風袋重量測定_白ﾎﾟﾘ容器11
        this.setItemData(processData, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI11, getSrYuudentaiFpItemData(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI11, srYuudentaiFp));

        // 風袋重量測定_白ﾎﾟﾘ容器12
        this.setItemData(processData, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI12, getSrYuudentaiFpItemData(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI12, srYuudentaiFp));

        // 保管容器準備_担当者
        this.setItemData(processData, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_TANTOUSYA, getSrYuudentaiFpItemData(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_TANTOUSYA, srYuudentaiFp));

        // ﾌｨﾙﾀｰ取り付け_LotNo1
        this.setItemData(processData, GXHDO102B026Const.FILTERTORITUKE_LOTNO1, getSrYuudentaiFpItemData(GXHDO102B026Const.FILTERTORITUKE_LOTNO1, srYuudentaiFp));

        // ﾌｨﾙﾀｰ取り付け_取り付け本数1
        this.setItemData(processData, GXHDO102B026Const.FILTERTORITUKE_TORITUKEHONSUU1, getSrYuudentaiFpItemData(GXHDO102B026Const.FILTERTORITUKE_TORITUKEHONSUU1, srYuudentaiFp));

        // ﾌｨﾙﾀｰ取り付け_LotNo2
        this.setItemData(processData, GXHDO102B026Const.FILTERTORITUKE_LOTNO2, getSrYuudentaiFpItemData(GXHDO102B026Const.FILTERTORITUKE_LOTNO2, srYuudentaiFp));

        // ﾌｨﾙﾀｰ取り付け_取り付け本数2
        this.setItemData(processData, GXHDO102B026Const.FILTERTORITUKE_TORITUKEHONSUU2, getSrYuudentaiFpItemData(GXHDO102B026Const.FILTERTORITUKE_TORITUKEHONSUU2, srYuudentaiFp));

        // ﾌｨﾙﾀｰ取り付け_LotNo3
        this.setItemData(processData, GXHDO102B026Const.FILTERTORITUKE_LOTNO3, getSrYuudentaiFpItemData(GXHDO102B026Const.FILTERTORITUKE_LOTNO3, srYuudentaiFp));

        // ﾌｨﾙﾀｰ取り付け_取り付け本数3
        this.setItemData(processData, GXHDO102B026Const.FILTERTORITUKE_TORITUKEHONSUU3, getSrYuudentaiFpItemData(GXHDO102B026Const.FILTERTORITUKE_TORITUKEHONSUU3, srYuudentaiFp));

        // ﾌｨﾙﾀｰ取り付け_担当者
        this.setItemData(processData, GXHDO102B026Const.FILTERTORITUKE_TANTOUSYA, getSrYuudentaiFpItemData(GXHDO102B026Const.FILTERTORITUKE_TANTOUSYA, srYuudentaiFp));

        // 排出容器の内袋
        this.setItemData(processData, GXHDO102B026Const.HAISYUTUYOUKINOUTIBUKURO, getSrYuudentaiFpItemData(GXHDO102B026Const.HAISYUTUYOUKINOUTIBUKURO, srYuudentaiFp));

        // F/PﾀﾝｸNo
        this.setItemData(processData, GXHDO102B026Const.FPTANKNO, getSrYuudentaiFpItemData(GXHDO102B026Const.FPTANKNO, srYuudentaiFp));

        // 洗浄確認
        this.setItemData(processData, GXHDO102B026Const.SENJYOUKAKUNIN, getSrYuudentaiFpItemData(GXHDO102B026Const.SENJYOUKAKUNIN, srYuudentaiFp));

        // F/P開始日
        this.setItemData(processData, GXHDO102B026Const.FPKAISI_DAY, getSrYuudentaiFpItemData(GXHDO102B026Const.FPKAISI_DAY, srYuudentaiFp));

        // F/P開始時間
        this.setItemData(processData, GXHDO102B026Const.FPKAISI_TIME, getSrYuudentaiFpItemData(GXHDO102B026Const.FPKAISI_TIME, srYuudentaiFp));

        // 圧送ﾚｷﾞｭﾚｰﾀｰNo
        this.setItemData(processData, GXHDO102B026Const.ASSOUREGULATORNO, getSrYuudentaiFpItemData(GXHDO102B026Const.ASSOUREGULATORNO, srYuudentaiFp));

        // 圧送圧力
        this.setItemData(processData, GXHDO102B026Const.ASSOUATURYOKU, getSrYuudentaiFpItemData(GXHDO102B026Const.ASSOUATURYOKU, srYuudentaiFp));

        // ﾌｨﾙﾀｰﾊﾟｽ開始_担当者
        this.setItemData(processData, GXHDO102B026Const.FILTERPASSKAISI_TANTOUSYA, getSrYuudentaiFpItemData(GXHDO102B026Const.FILTERPASSKAISI_TANTOUSYA, srYuudentaiFp));

        // ﾌｨﾙﾀｰ交換①_F/P停止日
        this.setItemData(processData, GXHDO102B026Const.FILTERKOUKAN1_FPTEISI_DAY, getSrYuudentaiFpItemData(GXHDO102B026Const.FILTERKOUKAN1_FPTEISI_DAY, srYuudentaiFp));

        // ﾌｨﾙﾀｰ交換①_F/P停止時間
        this.setItemData(processData, GXHDO102B026Const.FILTERKOUKAN1_FPTEISI_TIME, getSrYuudentaiFpItemData(GXHDO102B026Const.FILTERKOUKAN1_FPTEISI_TIME, srYuudentaiFp));

        // ﾌｨﾙﾀｰ交換①_LotNo1
        this.setItemData(processData, GXHDO102B026Const.FILTERKOUKAN1_LOTNO1, getSrYuudentaiFpItemData(GXHDO102B026Const.FILTERKOUKAN1_LOTNO1, srYuudentaiFp));

        // ﾌｨﾙﾀｰ交換①_取り付け本数1
        this.setItemData(processData, GXHDO102B026Const.FILTERKOUKAN1_TORITUKEHONSUU1, getSrYuudentaiFpItemData(GXHDO102B026Const.FILTERKOUKAN1_TORITUKEHONSUU1, srYuudentaiFp));

        // ﾌｨﾙﾀｰ交換①_LotNo2
        this.setItemData(processData, GXHDO102B026Const.FILTERKOUKAN1_LOTNO2, getSrYuudentaiFpItemData(GXHDO102B026Const.FILTERKOUKAN1_LOTNO2, srYuudentaiFp));

        // ﾌｨﾙﾀｰ交換①_取り付け本数2
        this.setItemData(processData, GXHDO102B026Const.FILTERKOUKAN1_TORITUKEHONSUU2, getSrYuudentaiFpItemData(GXHDO102B026Const.FILTERKOUKAN1_TORITUKEHONSUU2, srYuudentaiFp));

        // ﾌｨﾙﾀｰ交換①_LotNo3
        this.setItemData(processData, GXHDO102B026Const.FILTERKOUKAN1_LOTNO3, getSrYuudentaiFpItemData(GXHDO102B026Const.FILTERKOUKAN1_LOTNO3, srYuudentaiFp));

        // ﾌｨﾙﾀｰ交換①_取り付け本数3
        this.setItemData(processData, GXHDO102B026Const.FILTERKOUKAN1_TORITUKEHONSUU3, getSrYuudentaiFpItemData(GXHDO102B026Const.FILTERKOUKAN1_TORITUKEHONSUU3, srYuudentaiFp));

        // ﾌｨﾙﾀｰ交換①_F/P再開日
        this.setItemData(processData, GXHDO102B026Const.FILTERKOUKAN1_FPSAIKAI_DAY, getSrYuudentaiFpItemData(GXHDO102B026Const.FILTERKOUKAN1_FPSAIKAI_DAY, srYuudentaiFp));

        // ﾌｨﾙﾀｰ交換①_F/P再開時間
        this.setItemData(processData, GXHDO102B026Const.FILTERKOUKAN1_FPSAIKAI_TIME, getSrYuudentaiFpItemData(GXHDO102B026Const.FILTERKOUKAN1_FPSAIKAI_TIME, srYuudentaiFp));

        // ﾌｨﾙﾀｰ交換①_担当者
        this.setItemData(processData, GXHDO102B026Const.FILTERKOUKAN1_TANTOUSYA, getSrYuudentaiFpItemData(GXHDO102B026Const.FILTERKOUKAN1_TANTOUSYA, srYuudentaiFp));

        // F/P終了日
        this.setItemData(processData, GXHDO102B026Const.FPSYUURYOU_DAY, getSrYuudentaiFpItemData(GXHDO102B026Const.FPSYUURYOU_DAY, srYuudentaiFp));

        // F/P終了時間
        this.setItemData(processData, GXHDO102B026Const.FPSYUURYOU_TIME, getSrYuudentaiFpItemData(GXHDO102B026Const.FPSYUURYOU_TIME, srYuudentaiFp));

        // F/Pﾄｰﾀﾙ時間
        this.setItemData(processData, GXHDO102B026Const.FPTOTALJIKAN, getSrYuudentaiFpItemData(GXHDO102B026Const.FPTOTALJIKAN, srYuudentaiFp));

        // 1次ﾌｨﾙﾀｰ総使用本数
        this.setItemData(processData, GXHDO102B026Const.ITIJIFILTERSOUSIYOUHONSUU, getSrYuudentaiFpItemData(GXHDO102B026Const.ITIJIFILTERSOUSIYOUHONSUU, srYuudentaiFp));

        // 2次ﾌｨﾙﾀｰ総使用本数
        this.setItemData(processData, GXHDO102B026Const.NIJIFILTERSOUSIYOUHONSUU, getSrYuudentaiFpItemData(GXHDO102B026Const.NIJIFILTERSOUSIYOUHONSUU, srYuudentaiFp));

        // 3次ﾌｨﾙﾀｰ総使用本数
        this.setItemData(processData, GXHDO102B026Const.SANJIFILTERSOUSIYOUHONSUU, getSrYuudentaiFpItemData(GXHDO102B026Const.SANJIFILTERSOUSIYOUHONSUU, srYuudentaiFp));

        // ﾌｨﾙﾀｰﾊﾟｽ終了_担当者
        this.setItemData(processData, GXHDO102B026Const.FILTERPASSSYUURYOU_TANTOUSYA, getSrYuudentaiFpItemData(GXHDO102B026Const.FILTERPASSSYUURYOU_TANTOUSYA, srYuudentaiFp));

        // 総重量測定1
        this.setItemData(processData, GXHDO102B026Const.SOUJYUUROUSOKUTEI1, getSrYuudentaiFpItemData(GXHDO102B026Const.SOUJYUUROUSOKUTEI1, srYuudentaiFp));

        // 総重量測定2
        this.setItemData(processData, GXHDO102B026Const.SOUJYUUROUSOKUTEI2, getSrYuudentaiFpItemData(GXHDO102B026Const.SOUJYUUROUSOKUTEI2, srYuudentaiFp));

        // 総重量測定3
        this.setItemData(processData, GXHDO102B026Const.SOUJYUUROUSOKUTEI3, getSrYuudentaiFpItemData(GXHDO102B026Const.SOUJYUUROUSOKUTEI3, srYuudentaiFp));

        // 総重量測定4
        this.setItemData(processData, GXHDO102B026Const.SOUJYUUROUSOKUTEI4, getSrYuudentaiFpItemData(GXHDO102B026Const.SOUJYUUROUSOKUTEI4, srYuudentaiFp));

        // 総重量測定5
        this.setItemData(processData, GXHDO102B026Const.SOUJYUUROUSOKUTEI5, getSrYuudentaiFpItemData(GXHDO102B026Const.SOUJYUUROUSOKUTEI5, srYuudentaiFp));

        // 総重量測定6
        this.setItemData(processData, GXHDO102B026Const.SOUJYUUROUSOKUTEI6, getSrYuudentaiFpItemData(GXHDO102B026Const.SOUJYUUROUSOKUTEI6, srYuudentaiFp));

        // 総重量測定7
        this.setItemData(processData, GXHDO102B026Const.SOUJYUUROUSOKUTEI7, getSrYuudentaiFpItemData(GXHDO102B026Const.SOUJYUUROUSOKUTEI7, srYuudentaiFp));

        // 総重量測定8
        this.setItemData(processData, GXHDO102B026Const.SOUJYUUROUSOKUTEI8, getSrYuudentaiFpItemData(GXHDO102B026Const.SOUJYUUROUSOKUTEI8, srYuudentaiFp));

        // 総重量測定9
        this.setItemData(processData, GXHDO102B026Const.SOUJYUUROUSOKUTEI9, getSrYuudentaiFpItemData(GXHDO102B026Const.SOUJYUUROUSOKUTEI9, srYuudentaiFp));

        // 総重量測定10
        this.setItemData(processData, GXHDO102B026Const.SOUJYUUROUSOKUTEI10, getSrYuudentaiFpItemData(GXHDO102B026Const.SOUJYUUROUSOKUTEI10, srYuudentaiFp));

        // 総重量測定11
        this.setItemData(processData, GXHDO102B026Const.SOUJYUUROUSOKUTEI11, getSrYuudentaiFpItemData(GXHDO102B026Const.SOUJYUUROUSOKUTEI11, srYuudentaiFp));

        // 総重量測定12
        this.setItemData(processData, GXHDO102B026Const.SOUJYUUROUSOKUTEI12, getSrYuudentaiFpItemData(GXHDO102B026Const.SOUJYUUROUSOKUTEI12, srYuudentaiFp));

        // 誘電体ｽﾗﾘｰ重量1
        this.setItemData(processData, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU1, getSrYuudentaiFpItemData(GXHDO102B026Const.YUUDENTAISLURRYJYUUROU1, srYuudentaiFp));

        // 誘電体ｽﾗﾘｰ重量2
        this.setItemData(processData, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU2, getSrYuudentaiFpItemData(GXHDO102B026Const.YUUDENTAISLURRYJYUUROU2, srYuudentaiFp));

        // 誘電体ｽﾗﾘｰ重量3
        this.setItemData(processData, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU3, getSrYuudentaiFpItemData(GXHDO102B026Const.YUUDENTAISLURRYJYUUROU3, srYuudentaiFp));

        // 誘電体ｽﾗﾘｰ重量4
        this.setItemData(processData, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU4, getSrYuudentaiFpItemData(GXHDO102B026Const.YUUDENTAISLURRYJYUUROU4, srYuudentaiFp));

        // 誘電体ｽﾗﾘｰ重量5
        this.setItemData(processData, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU5, getSrYuudentaiFpItemData(GXHDO102B026Const.YUUDENTAISLURRYJYUUROU5, srYuudentaiFp));

        // 誘電体ｽﾗﾘｰ重量6
        this.setItemData(processData, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU6, getSrYuudentaiFpItemData(GXHDO102B026Const.YUUDENTAISLURRYJYUUROU6, srYuudentaiFp));

        // 誘電体ｽﾗﾘｰ重量7
        this.setItemData(processData, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU7, getSrYuudentaiFpItemData(GXHDO102B026Const.YUUDENTAISLURRYJYUUROU7, srYuudentaiFp));

        // 誘電体ｽﾗﾘｰ重量8
        this.setItemData(processData, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU8, getSrYuudentaiFpItemData(GXHDO102B026Const.YUUDENTAISLURRYJYUUROU8, srYuudentaiFp));

        // 誘電体ｽﾗﾘｰ重量9
        this.setItemData(processData, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU9, getSrYuudentaiFpItemData(GXHDO102B026Const.YUUDENTAISLURRYJYUUROU9, srYuudentaiFp));

        // 誘電体ｽﾗﾘｰ重量10
        this.setItemData(processData, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU10, getSrYuudentaiFpItemData(GXHDO102B026Const.YUUDENTAISLURRYJYUUROU10, srYuudentaiFp));

        // 誘電体ｽﾗﾘｰ重量11
        this.setItemData(processData, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU11, getSrYuudentaiFpItemData(GXHDO102B026Const.YUUDENTAISLURRYJYUUROU11, srYuudentaiFp));

        // 誘電体ｽﾗﾘｰ重量12
        this.setItemData(processData, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU12, getSrYuudentaiFpItemData(GXHDO102B026Const.YUUDENTAISLURRYJYUUROU12, srYuudentaiFp));

        // 誘電体ｽﾗﾘｰ重量合計
        this.setItemData(processData, GXHDO102B026Const.YUUDENTAISLURRYJYUUROUGOUKEI, getSrYuudentaiFpItemData(GXHDO102B026Const.YUUDENTAISLURRYJYUUROUGOUKEI, srYuudentaiFp));

        // 歩留まり計算
        this.setItemData(processData, GXHDO102B026Const.BUDOMARIKEISAN, getSrYuudentaiFpItemData(GXHDO102B026Const.BUDOMARIKEISAN, srYuudentaiFp));

        // 誘電体ｽﾗﾘｰ有効期限
        this.setItemData(processData, GXHDO102B026Const.UUDENTAISLURRYYUUKOUKIGEN, getSrYuudentaiFpItemData(GXHDO102B026Const.UUDENTAISLURRYYUUKOUKIGEN, srYuudentaiFp));

        // 粉砕判定
        this.setItemData(processData, GXHDO102B026Const.FUNSAIHANTEI, getSrYuudentaiFpItemData(GXHDO102B026Const.FUNSAIHANTEI, srYuudentaiFp));

        // 製品重量確認_担当者
        this.setItemData(processData, GXHDO102B026Const.SEIHINJYUURYOUKAKUNIN_TANTOUSYA, getSrYuudentaiFpItemData(GXHDO102B026Const.SEIHINJYUURYOUKAKUNIN_TANTOUSYA, srYuudentaiFp));

        // 保存用ｻﾝﾌﾟﾙ回収
        this.setItemData(processData, GXHDO102B026Const.HOZONYOUSAMPLEKAISYU, getSrYuudentaiFpItemData(GXHDO102B026Const.HOZONYOUSAMPLEKAISYU, srYuudentaiFp));

        // 分析用ｻﾝﾌﾟﾙ回収
        this.setItemData(processData, GXHDO102B026Const.ZUNSEKIYOUSAMPLEKAISYU, getSrYuudentaiFpItemData(GXHDO102B026Const.ZUNSEKIYOUSAMPLEKAISYU, srYuudentaiFp));

        // ｱﾙﾐ皿風袋重量
        this.setItemData(processData, GXHDO102B026Const.ARUMIZARAFUUTAIJYUURYOU, getSrYuudentaiFpItemData(GXHDO102B026Const.ARUMIZARAFUUTAIJYUURYOU, srYuudentaiFp));

        // 乾燥前ｽﾗﾘｰ重量
        this.setItemData(processData, GXHDO102B026Const.KANSOUMAESLURRYJYUURYOU, getSrYuudentaiFpItemData(GXHDO102B026Const.KANSOUMAESLURRYJYUURYOU, srYuudentaiFp));

        // 乾燥開始日
        this.setItemData(processData, GXHDO102B026Const.KANSOUKAISI_DAY, getSrYuudentaiFpItemData(GXHDO102B026Const.KANSOUKAISI_DAY, srYuudentaiFp));

        // 乾燥開始時間
        this.setItemData(processData, GXHDO102B026Const.KANSOUKAISI_TIME, getSrYuudentaiFpItemData(GXHDO102B026Const.KANSOUKAISI_TIME, srYuudentaiFp));

        // 乾燥終了日
        this.setItemData(processData, GXHDO102B026Const.KANSOUSYUURYOU_DAY, getSrYuudentaiFpItemData(GXHDO102B026Const.KANSOUSYUURYOU_DAY, srYuudentaiFp));

        // 乾燥終了時間
        this.setItemData(processData, GXHDO102B026Const.KANSOUSYUURYOU_TIME, getSrYuudentaiFpItemData(GXHDO102B026Const.KANSOUSYUURYOU_TIME, srYuudentaiFp));

        // 乾燥時間ﾄｰﾀﾙ
        this.setItemData(processData, GXHDO102B026Const.KANSOUJIKANTOTAL, getSrYuudentaiFpItemData(GXHDO102B026Const.KANSOUJIKANTOTAL, srYuudentaiFp));

        // 乾燥後総重量
        this.setItemData(processData, GXHDO102B026Const.KANSOUGOSOUJYUURYOU, getSrYuudentaiFpItemData(GXHDO102B026Const.KANSOUGOSOUJYUURYOU, srYuudentaiFp));

        // 乾燥後正味重量
        this.setItemData(processData, GXHDO102B026Const.KANSOUGOSYOUMIJYUURYOU, getSrYuudentaiFpItemData(GXHDO102B026Const.KANSOUGOSYOUMIJYUURYOU, srYuudentaiFp));

        // 固形分比率
        this.setItemData(processData, GXHDO102B026Const.KOKEIBUNHIRITU, getSrYuudentaiFpItemData(GXHDO102B026Const.KOKEIBUNHIRITU, srYuudentaiFp));

        // 備考1
        this.setItemData(processData, GXHDO102B026Const.BIKOU1, getSrYuudentaiFpItemData(GXHDO102B026Const.BIKOU1, srYuudentaiFp));

        // 備考2
        this.setItemData(processData, GXHDO102B026Const.BIKOU2, getSrYuudentaiFpItemData(GXHDO102B026Const.BIKOU2, srYuudentaiFp));

    }

    /**
     * 誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return 誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管データ
     * @throws SQLException 例外エラー
     */
    private List<SrYuudentaiFp> getSrYuudentaiFpData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrYuudentaiFp(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSrYuudentaiFp(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
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
     * [誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrYuudentaiFp> loadSrYuudentaiFp(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + " kojyo,lotno,edaban,yuudentaislurryhinmei,yuudentaislurrylotno,lotkubun,genryoulotno,genryoukigou,"
                + "fuutaijyuuryousokutei_sutenyouki1,fuutaijyuuryousokutei_sutenyouki2,fuutaijyuuryousokutei_sutenyouki3,"
                + "fuutaijyuuryousokutei_sutenyouki4,fuutaijyuuryousokutei_sutenyouki5,fuutaijyuuryousokutei_sutenyouki6,"
                + "fuutaijyuuryousokutei_siropori1,fuutaijyuuryousokutei_siropori2,fuutaijyuuryousokutei_siropori3,fuutaijyuuryousokutei_siropori4,"
                + "fuutaijyuuryousokutei_siropori5,fuutaijyuuryousokutei_siropori6,fuutaijyuuryousokutei_siropori7,fuutaijyuuryousokutei_siropori8,"
                + "fuutaijyuuryousokutei_siropori9,fuutaijyuuryousokutei_siropori10,fuutaijyuuryousokutei_siropori11,fuutaijyuuryousokutei_siropori12,"
                + "fuutaijyuuryousokutei_tantousya,filterrenketu,filtertorituke_itijifilterhinmei,filtertorituke_lotno1,filtertorituke_toritukehonsuu1,"
                + "filtertorituke_nijifilterhinmei,filtertorituke_lotno2,filtertorituke_toritukehonsuu2,filtertorituke_sanjifilterhinmei,"
                + "filtertorituke_lotno3,filtertorituke_toritukehonsuu3,filtertorituke_tantousya,haisyutuyoukinoutibukuro,filtersiyoukaisuu,"
                + "Fptankno,senjyoukakunin,Fpkaisinichiji,assouregulatorNo,assouaturyoku,filterpasskaisi_tantousya,filterkoukan1_Fpteisinichiji,"
                + "filterkoukan1_itijifilterhinmei,filterkoukan1_lotno1,filterkoukan1_toritukehonsuu1,filterkoukan1_nijifilterhinmei,filterkoukan1_lotno2,"
                + "filterkoukan1_toritukehonsuu2,filterkoukan1_sanjifilterhinmei,filterkoukan1_lotno3,filterkoukan1_toritukehonsuu3,"
                + "filterkoukan1_Fpsaikainichiji,filterkoukan1_tantousya,FPsyuuryounichiji,FPtotaljikan,itijifiltersousiyouhonsuu,"
                + "nijifiltersousiyouhonsuu,sanjifiltersousiyouhonsuu,filterpasssyuuryou_tantousya,soujyuurousokutei1,soujyuurousokutei2,"
                + "soujyuurousokutei3,soujyuurousokutei4,soujyuurousokutei5,soujyuurousokutei6,soujyuurousokutei7,soujyuurousokutei8,"
                + "soujyuurousokutei9,soujyuurousokutei10,soujyuurousokutei11,soujyuurousokutei12,yuudentaislurryjyuurou1,yuudentaislurryjyuurou2,"
                + "yuudentaislurryjyuurou3,yuudentaislurryjyuurou4,yuudentaislurryjyuurou5,yuudentaislurryjyuurou6,yuudentaislurryjyuurou7,"
                + "yuudentaislurryjyuurou8,yuudentaislurryjyuurou9,yuudentaislurryjyuurou10,yuudentaislurryjyuurou11,yuudentaislurryjyuurou12,"
                + "yuudentaislurryjyuurougoukei,tounyuuryou,budomarikeisan,uudentaislurryyuukoukigen,funsaihantei,seihinjyuuryoukakunin_tantousya,"
                + "hozonyousamplekaisyu,zunsekiyousamplekaisyu,kansouzara,arumizarafuutaijyuuryou,slurrysamplejyuuryoukikaku,kansoumaeslurryjyuuryou,"
                + "kansouki,kansouondokikaku,kansoujikankikaku,kansoukaisinichiji,kansousyuuryounichiji,kansoujikantotal,kansougosoujyuuryou,"
                + "kansougosyoumijyuuryou,kokeibunhiritu,bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + " FROM sr_yuudentai_fp "
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
        mapping.put("kojyo", "kojyo");                                                           // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                                           // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                                         // 枝番
        mapping.put("yuudentaislurryhinmei", "yuudentaislurryhinmei");                           // 誘電体ｽﾗﾘｰ品名
        mapping.put("yuudentaislurrylotno", "yuudentaislurrylotno");                             // 誘電体ｽﾗﾘｰLotNo
        mapping.put("lotkubun", "lotkubun");                                                     // ﾛｯﾄ区分
        mapping.put("genryoulotno", "genryoulotno");                                             // 原料LotNo
        mapping.put("genryoukigou", "genryoukigou");                                             // 原料記号
        mapping.put("fuutaijyuuryousokutei_sutenyouki1", "fuutaijyuuryousokutei_sutenyouki1");   // 風袋重量測定_ｽﾃﾝ容器1
        mapping.put("fuutaijyuuryousokutei_sutenyouki2", "fuutaijyuuryousokutei_sutenyouki2");   // 風袋重量測定_ｽﾃﾝ容器2
        mapping.put("fuutaijyuuryousokutei_sutenyouki3", "fuutaijyuuryousokutei_sutenyouki3");   // 風袋重量測定_ｽﾃﾝ容器3
        mapping.put("fuutaijyuuryousokutei_sutenyouki4", "fuutaijyuuryousokutei_sutenyouki4");   // 風袋重量測定_ｽﾃﾝ容器4
        mapping.put("fuutaijyuuryousokutei_sutenyouki5", "fuutaijyuuryousokutei_sutenyouki5");   // 風袋重量測定_ｽﾃﾝ容器5
        mapping.put("fuutaijyuuryousokutei_sutenyouki6", "fuutaijyuuryousokutei_sutenyouki6");   // 風袋重量測定_ｽﾃﾝ容器6
        mapping.put("fuutaijyuuryousokutei_siropori1", "fuutaijyuuryousokutei_siropori1");       // 風袋重量測定_白ﾎﾟﾘ容器1
        mapping.put("fuutaijyuuryousokutei_siropori2", "fuutaijyuuryousokutei_siropori2");       // 風袋重量測定_白ﾎﾟﾘ容器2
        mapping.put("fuutaijyuuryousokutei_siropori3", "fuutaijyuuryousokutei_siropori3");       // 風袋重量測定_白ﾎﾟﾘ容器3
        mapping.put("fuutaijyuuryousokutei_siropori4", "fuutaijyuuryousokutei_siropori4");       // 風袋重量測定_白ﾎﾟﾘ容器4
        mapping.put("fuutaijyuuryousokutei_siropori5", "fuutaijyuuryousokutei_siropori5");       // 風袋重量測定_白ﾎﾟﾘ容器5
        mapping.put("fuutaijyuuryousokutei_siropori6", "fuutaijyuuryousokutei_siropori6");       // 風袋重量測定_白ﾎﾟﾘ容器6
        mapping.put("fuutaijyuuryousokutei_siropori7", "fuutaijyuuryousokutei_siropori7");       // 風袋重量測定_白ﾎﾟﾘ容器7
        mapping.put("fuutaijyuuryousokutei_siropori8", "fuutaijyuuryousokutei_siropori8");       // 風袋重量測定_白ﾎﾟﾘ容器8
        mapping.put("fuutaijyuuryousokutei_siropori9", "fuutaijyuuryousokutei_siropori9");       // 風袋重量測定_白ﾎﾟﾘ容器9
        mapping.put("fuutaijyuuryousokutei_siropori10", "fuutaijyuuryousokutei_siropori10");     // 風袋重量測定_白ﾎﾟﾘ容器10
        mapping.put("fuutaijyuuryousokutei_siropori11", "fuutaijyuuryousokutei_siropori11");     // 風袋重量測定_白ﾎﾟﾘ容器11
        mapping.put("fuutaijyuuryousokutei_siropori12", "fuutaijyuuryousokutei_siropori12");     // 風袋重量測定_白ﾎﾟﾘ容器12
        mapping.put("fuutaijyuuryousokutei_tantousya", "fuutaijyuuryousokutei_tantousya");       // 保管容器準備_担当者
        mapping.put("filterrenketu", "filterrenketu");                                           // ﾌｨﾙﾀｰ連結
        mapping.put("filtertorituke_itijifilterhinmei", "filtertorituke_itijifilterhinmei");     // ﾌｨﾙﾀｰ取り付け_1次ﾌｨﾙﾀｰ品名
        mapping.put("filtertorituke_lotno1", "filtertorituke_lotno1");                           // ﾌｨﾙﾀｰ取り付け_LotNo1
        mapping.put("filtertorituke_toritukehonsuu1", "filtertorituke_toritukehonsuu1");         // ﾌｨﾙﾀｰ取り付け_取り付け本数1
        mapping.put("filtertorituke_nijifilterhinmei", "filtertorituke_nijifilterhinmei");       // ﾌｨﾙﾀｰ取り付け_2次ﾌｨﾙﾀｰ品名
        mapping.put("filtertorituke_lotno2", "filtertorituke_lotno2");                           // ﾌｨﾙﾀｰ取り付け_LotNo2
        mapping.put("filtertorituke_toritukehonsuu2", "filtertorituke_toritukehonsuu2");         // ﾌｨﾙﾀｰ取り付け_取り付け本数2
        mapping.put("filtertorituke_sanjifilterhinmei", "filtertorituke_sanjifilterhinmei");     // ﾌｨﾙﾀｰ取り付け_3次ﾌｨﾙﾀｰ品名
        mapping.put("filtertorituke_lotno3", "filtertorituke_lotno3");                           // ﾌｨﾙﾀｰ取り付け_LotNo3
        mapping.put("filtertorituke_toritukehonsuu3", "filtertorituke_toritukehonsuu3");         // ﾌｨﾙﾀｰ取り付け_取り付け本数3
        mapping.put("filtertorituke_tantousya", "filtertorituke_tantousya");                     // ﾌｨﾙﾀｰ取り付け_担当者
        mapping.put("haisyutuyoukinoutibukuro", "haisyutuyoukinoutibukuro");                     // 排出容器の内袋
        mapping.put("filtersiyoukaisuu", "filtersiyoukaisuu");                                   // ﾌｨﾙﾀｰ使用回数
        mapping.put("Fptankno", "Fptankno");                                                     // F/PﾀﾝｸNo
        mapping.put("senjyoukakunin", "senjyoukakunin");                                         // 洗浄確認
        mapping.put("Fpkaisinichiji", "Fpkaisinichiji");                                         // F/P開始日時
        mapping.put("assouregulatorNo", "assouregulatorNo");                                     // 圧送ﾚｷﾞｭﾚｰﾀｰNo
        mapping.put("assouaturyoku", "assouaturyoku");                                           // 圧送圧力
        mapping.put("filterpasskaisi_tantousya", "filterpasskaisi_tantousya");                   // ﾌｨﾙﾀｰﾊﾟｽ開始_担当者
        mapping.put("filterkoukan1_Fpteisinichiji", "filterkoukan1_Fpteisinichiji");             // ﾌｨﾙﾀｰ交換①_F/P停止日時
        mapping.put("filterkoukan1_itijifilterhinmei", "filterkoukan1_itijifilterhinmei");       // ﾌｨﾙﾀｰ交換①_1次ﾌｨﾙﾀｰ品名
        mapping.put("filterkoukan1_lotno1", "filterkoukan1_lotno1");                             // ﾌｨﾙﾀｰ交換①_LotNo1
        mapping.put("filterkoukan1_toritukehonsuu1", "filterkoukan1_toritukehonsuu1");           // ﾌｨﾙﾀｰ交換①_取り付け本数1
        mapping.put("filterkoukan1_nijifilterhinmei", "filterkoukan1_nijifilterhinmei");         // ﾌｨﾙﾀｰ交換①_2次ﾌｨﾙﾀｰ品名
        mapping.put("filterkoukan1_lotno2", "filterkoukan1_lotno2");                             // ﾌｨﾙﾀｰ交換①_LotNo2
        mapping.put("filterkoukan1_toritukehonsuu2", "filterkoukan1_toritukehonsuu2");           // ﾌｨﾙﾀｰ交換①_取り付け本数2
        mapping.put("filterkoukan1_sanjifilterhinmei", "filterkoukan1_sanjifilterhinmei");       // ﾌｨﾙﾀｰ交換①_3次ﾌｨﾙﾀｰ品名
        mapping.put("filterkoukan1_lotno3", "filterkoukan1_lotno3");                             // ﾌｨﾙﾀｰ交換①_LotNo3
        mapping.put("filterkoukan1_toritukehonsuu3", "filterkoukan1_toritukehonsuu3");           // ﾌｨﾙﾀｰ交換①_取り付け本数3
        mapping.put("filterkoukan1_Fpsaikainichiji", "filterkoukan1_Fpsaikainichiji");           // ﾌｨﾙﾀｰ交換①_F/P再開日時
        mapping.put("filterkoukan1_tantousya", "filterkoukan1_tantousya");                       // ﾌｨﾙﾀｰ交換①_担当者
        mapping.put("FPsyuuryounichiji", "FPsyuuryounichiji");                                   // F/P終了日時
        mapping.put("FPtotaljikan", "FPtotaljikan");                                             // F/Pﾄｰﾀﾙ時間
        mapping.put("itijifiltersousiyouhonsuu", "itijifiltersousiyouhonsuu");                   // 1次ﾌｨﾙﾀｰ総使用本数
        mapping.put("nijifiltersousiyouhonsuu", "nijifiltersousiyouhonsuu");                     // 2次ﾌｨﾙﾀｰ総使用本数
        mapping.put("sanjifiltersousiyouhonsuu", "sanjifiltersousiyouhonsuu");                   // 3次ﾌｨﾙﾀｰ総使用本数
        mapping.put("filterpasssyuuryou_tantousya", "filterpasssyuuryou_tantousya");             // ﾌｨﾙﾀｰﾊﾟｽ終了_担当者
        mapping.put("soujyuurousokutei1", "soujyuurousokutei1");                                 // 総重量測定1
        mapping.put("soujyuurousokutei2", "soujyuurousokutei2");                                 // 総重量測定2
        mapping.put("soujyuurousokutei3", "soujyuurousokutei3");                                 // 総重量測定3
        mapping.put("soujyuurousokutei4", "soujyuurousokutei4");                                 // 総重量測定4
        mapping.put("soujyuurousokutei5", "soujyuurousokutei5");                                 // 総重量測定5
        mapping.put("soujyuurousokutei6", "soujyuurousokutei6");                                 // 総重量測定6
        mapping.put("soujyuurousokutei7", "soujyuurousokutei7");                                 // 総重量測定7
        mapping.put("soujyuurousokutei8", "soujyuurousokutei8");                                 // 総重量測定8
        mapping.put("soujyuurousokutei9", "soujyuurousokutei9");                                 // 総重量測定9
        mapping.put("soujyuurousokutei10", "soujyuurousokutei10");                               // 総重量測定10
        mapping.put("soujyuurousokutei11", "soujyuurousokutei11");                               // 総重量測定11
        mapping.put("soujyuurousokutei12", "soujyuurousokutei12");                               // 総重量測定12
        mapping.put("yuudentaislurryjyuurou1", "yuudentaislurryjyuurou1");                       // 誘電体ｽﾗﾘｰ重量1
        mapping.put("yuudentaislurryjyuurou2", "yuudentaislurryjyuurou2");                       // 誘電体ｽﾗﾘｰ重量2
        mapping.put("yuudentaislurryjyuurou3", "yuudentaislurryjyuurou3");                       // 誘電体ｽﾗﾘｰ重量3
        mapping.put("yuudentaislurryjyuurou4", "yuudentaislurryjyuurou4");                       // 誘電体ｽﾗﾘｰ重量4
        mapping.put("yuudentaislurryjyuurou5", "yuudentaislurryjyuurou5");                       // 誘電体ｽﾗﾘｰ重量5
        mapping.put("yuudentaislurryjyuurou6", "yuudentaislurryjyuurou6");                       // 誘電体ｽﾗﾘｰ重量6
        mapping.put("yuudentaislurryjyuurou7", "yuudentaislurryjyuurou7");                       // 誘電体ｽﾗﾘｰ重量7
        mapping.put("yuudentaislurryjyuurou8", "yuudentaislurryjyuurou8");                       // 誘電体ｽﾗﾘｰ重量8
        mapping.put("yuudentaislurryjyuurou9", "yuudentaislurryjyuurou9");                       // 誘電体ｽﾗﾘｰ重量9
        mapping.put("yuudentaislurryjyuurou10", "yuudentaislurryjyuurou10");                     // 誘電体ｽﾗﾘｰ重量10
        mapping.put("yuudentaislurryjyuurou11", "yuudentaislurryjyuurou11");                     // 誘電体ｽﾗﾘｰ重量11
        mapping.put("yuudentaislurryjyuurou12", "yuudentaislurryjyuurou12");                     // 誘電体ｽﾗﾘｰ重量12
        mapping.put("yuudentaislurryjyuurougoukei", "yuudentaislurryjyuurougoukei");             // 誘電体ｽﾗﾘｰ重量合計
        mapping.put("tounyuuryou", "tounyuuryou");                                               // 投入量
        mapping.put("budomarikeisan", "budomarikeisan");                                         // 歩留まり計算
        mapping.put("uudentaislurryyuukoukigen", "uudentaislurryyuukoukigen");                   // 誘電体ｽﾗﾘｰ有効期限
        mapping.put("funsaihantei", "funsaihantei");                                             // 粉砕判定
        mapping.put("seihinjyuuryoukakunin_tantousya", "seihinjyuuryoukakunin_tantousya");       // 製品重量確認_担当者
        mapping.put("hozonyousamplekaisyu", "hozonyousamplekaisyu");                             // 保存用ｻﾝﾌﾟﾙ回収
        mapping.put("zunsekiyousamplekaisyu", "zunsekiyousamplekaisyu");                         // 分析用ｻﾝﾌﾟﾙ回収
        mapping.put("kansouzara", "kansouzara");                                                 // 乾燥皿
        mapping.put("arumizarafuutaijyuuryou", "arumizarafuutaijyuuryou");                       // ｱﾙﾐ皿風袋重量
        mapping.put("slurrysamplejyuuryoukikaku", "slurrysamplejyuuryoukikaku");                 // ｽﾗﾘｰｻﾝﾌﾟﾙ重量規格
        mapping.put("kansoumaeslurryjyuuryou", "kansoumaeslurryjyuuryou");                       // 乾燥前ｽﾗﾘｰ重量
        mapping.put("kansouki", "kansouki");                                                     // 乾燥機
        mapping.put("kansouondokikaku", "kansouondokikaku");                                     // 乾燥温度規格
        mapping.put("kansoujikankikaku", "kansoujikankikaku");                                   // 乾燥時間規格
        mapping.put("kansoukaisinichiji", "kansoukaisinichiji");                                 // 乾燥開始日時
        mapping.put("kansousyuuryounichiji", "kansousyuuryounichiji");                           // 乾燥終了日時
        mapping.put("kansoujikantotal", "kansoujikantotal");                                     // 乾燥時間ﾄｰﾀﾙ
        mapping.put("kansougosoujyuuryou", "kansougosoujyuuryou");                               // 乾燥後総重量
        mapping.put("kansougosyoumijyuuryou", "kansougosyoumijyuuryou");                         // 乾燥後正味重量
        mapping.put("kokeibunhiritu", "kokeibunhiritu");                                         // 固形分比率
        mapping.put("bikou1", "bikou1");                                                         // 備考1
        mapping.put("bikou2", "bikou2");                                                         // 備考2
        mapping.put("torokunichiji", "torokunichiji");                                           // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                                             // 更新日時
        mapping.put("revision", "revision");                                                     // revision

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrYuudentaiFp>> beanHandler = new BeanListHandler<>(SrYuudentaiFp.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrYuudentaiFp> loadTmpSrYuudentaiFp(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + " kojyo,lotno,edaban,yuudentaislurryhinmei,yuudentaislurrylotno,lotkubun,genryoulotno,genryoukigou,"
                + "fuutaijyuuryousokutei_sutenyouki1,fuutaijyuuryousokutei_sutenyouki2,fuutaijyuuryousokutei_sutenyouki3,"
                + "fuutaijyuuryousokutei_sutenyouki4,fuutaijyuuryousokutei_sutenyouki5,fuutaijyuuryousokutei_sutenyouki6,"
                + "fuutaijyuuryousokutei_siropori1,fuutaijyuuryousokutei_siropori2,fuutaijyuuryousokutei_siropori3,fuutaijyuuryousokutei_siropori4,"
                + "fuutaijyuuryousokutei_siropori5,fuutaijyuuryousokutei_siropori6,fuutaijyuuryousokutei_siropori7,fuutaijyuuryousokutei_siropori8,"
                + "fuutaijyuuryousokutei_siropori9,fuutaijyuuryousokutei_siropori10,fuutaijyuuryousokutei_siropori11,fuutaijyuuryousokutei_siropori12,"
                + "fuutaijyuuryousokutei_tantousya,filterrenketu,filtertorituke_itijifilterhinmei,filtertorituke_lotno1,filtertorituke_toritukehonsuu1,"
                + "filtertorituke_nijifilterhinmei,filtertorituke_lotno2,filtertorituke_toritukehonsuu2,filtertorituke_sanjifilterhinmei,"
                + "filtertorituke_lotno3,filtertorituke_toritukehonsuu3,filtertorituke_tantousya,haisyutuyoukinoutibukuro,filtersiyoukaisuu,"
                + "Fptankno,senjyoukakunin,Fpkaisinichiji,assouregulatorNo,assouaturyoku,filterpasskaisi_tantousya,filterkoukan1_Fpteisinichiji,"
                + "filterkoukan1_itijifilterhinmei,filterkoukan1_lotno1,filterkoukan1_toritukehonsuu1,filterkoukan1_nijifilterhinmei,filterkoukan1_lotno2,"
                + "filterkoukan1_toritukehonsuu2,filterkoukan1_sanjifilterhinmei,filterkoukan1_lotno3,filterkoukan1_toritukehonsuu3,"
                + "filterkoukan1_Fpsaikainichiji,filterkoukan1_tantousya,FPsyuuryounichiji,FPtotaljikan,itijifiltersousiyouhonsuu,"
                + "nijifiltersousiyouhonsuu,sanjifiltersousiyouhonsuu,filterpasssyuuryou_tantousya,soujyuurousokutei1,soujyuurousokutei2,"
                + "soujyuurousokutei3,soujyuurousokutei4,soujyuurousokutei5,soujyuurousokutei6,soujyuurousokutei7,soujyuurousokutei8,"
                + "soujyuurousokutei9,soujyuurousokutei10,soujyuurousokutei11,soujyuurousokutei12,yuudentaislurryjyuurou1,yuudentaislurryjyuurou2,"
                + "yuudentaislurryjyuurou3,yuudentaislurryjyuurou4,yuudentaislurryjyuurou5,yuudentaislurryjyuurou6,yuudentaislurryjyuurou7,"
                + "yuudentaislurryjyuurou8,yuudentaislurryjyuurou9,yuudentaislurryjyuurou10,yuudentaislurryjyuurou11,yuudentaislurryjyuurou12,"
                + "yuudentaislurryjyuurougoukei,tounyuuryou,budomarikeisan,uudentaislurryyuukoukigen,funsaihantei,seihinjyuuryoukakunin_tantousya,"
                + "hozonyousamplekaisyu,zunsekiyousamplekaisyu,kansouzara,arumizarafuutaijyuuryou,slurrysamplejyuuryoukikaku,kansoumaeslurryjyuuryou,"
                + "kansouki,kansouondokikaku,kansoujikankikaku,kansoukaisinichiji,kansousyuuryounichiji,kansoujikantotal,kansougosoujyuuryou,"
                + "kansougosyoumijyuuryou,kokeibunhiritu,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + " FROM tmp_sr_yuudentai_fp "
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
        mapping.put("kojyo", "kojyo");                                                           // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                                           // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                                         // 枝番
        mapping.put("yuudentaislurryhinmei", "yuudentaislurryhinmei");                           // 誘電体ｽﾗﾘｰ品名
        mapping.put("yuudentaislurrylotno", "yuudentaislurrylotno");                             // 誘電体ｽﾗﾘｰLotNo
        mapping.put("lotkubun", "lotkubun");                                                     // ﾛｯﾄ区分
        mapping.put("genryoulotno", "genryoulotno");                                             // 原料LotNo
        mapping.put("genryoukigou", "genryoukigou");                                             // 原料記号
        mapping.put("fuutaijyuuryousokutei_sutenyouki1", "fuutaijyuuryousokutei_sutenyouki1");   // 風袋重量測定_ｽﾃﾝ容器1
        mapping.put("fuutaijyuuryousokutei_sutenyouki2", "fuutaijyuuryousokutei_sutenyouki2");   // 風袋重量測定_ｽﾃﾝ容器2
        mapping.put("fuutaijyuuryousokutei_sutenyouki3", "fuutaijyuuryousokutei_sutenyouki3");   // 風袋重量測定_ｽﾃﾝ容器3
        mapping.put("fuutaijyuuryousokutei_sutenyouki4", "fuutaijyuuryousokutei_sutenyouki4");   // 風袋重量測定_ｽﾃﾝ容器4
        mapping.put("fuutaijyuuryousokutei_sutenyouki5", "fuutaijyuuryousokutei_sutenyouki5");   // 風袋重量測定_ｽﾃﾝ容器5
        mapping.put("fuutaijyuuryousokutei_sutenyouki6", "fuutaijyuuryousokutei_sutenyouki6");   // 風袋重量測定_ｽﾃﾝ容器6
        mapping.put("fuutaijyuuryousokutei_siropori1", "fuutaijyuuryousokutei_siropori1");       // 風袋重量測定_白ﾎﾟﾘ容器1
        mapping.put("fuutaijyuuryousokutei_siropori2", "fuutaijyuuryousokutei_siropori2");       // 風袋重量測定_白ﾎﾟﾘ容器2
        mapping.put("fuutaijyuuryousokutei_siropori3", "fuutaijyuuryousokutei_siropori3");       // 風袋重量測定_白ﾎﾟﾘ容器3
        mapping.put("fuutaijyuuryousokutei_siropori4", "fuutaijyuuryousokutei_siropori4");       // 風袋重量測定_白ﾎﾟﾘ容器4
        mapping.put("fuutaijyuuryousokutei_siropori5", "fuutaijyuuryousokutei_siropori5");       // 風袋重量測定_白ﾎﾟﾘ容器5
        mapping.put("fuutaijyuuryousokutei_siropori6", "fuutaijyuuryousokutei_siropori6");       // 風袋重量測定_白ﾎﾟﾘ容器6
        mapping.put("fuutaijyuuryousokutei_siropori7", "fuutaijyuuryousokutei_siropori7");       // 風袋重量測定_白ﾎﾟﾘ容器7
        mapping.put("fuutaijyuuryousokutei_siropori8", "fuutaijyuuryousokutei_siropori8");       // 風袋重量測定_白ﾎﾟﾘ容器8
        mapping.put("fuutaijyuuryousokutei_siropori9", "fuutaijyuuryousokutei_siropori9");       // 風袋重量測定_白ﾎﾟﾘ容器9
        mapping.put("fuutaijyuuryousokutei_siropori10", "fuutaijyuuryousokutei_siropori10");     // 風袋重量測定_白ﾎﾟﾘ容器10
        mapping.put("fuutaijyuuryousokutei_siropori11", "fuutaijyuuryousokutei_siropori11");     // 風袋重量測定_白ﾎﾟﾘ容器11
        mapping.put("fuutaijyuuryousokutei_siropori12", "fuutaijyuuryousokutei_siropori12");     // 風袋重量測定_白ﾎﾟﾘ容器12
        mapping.put("fuutaijyuuryousokutei_tantousya", "fuutaijyuuryousokutei_tantousya");       // 保管容器準備_担当者
        mapping.put("filterrenketu", "filterrenketu");                                           // ﾌｨﾙﾀｰ連結
        mapping.put("filtertorituke_itijifilterhinmei", "filtertorituke_itijifilterhinmei");     // ﾌｨﾙﾀｰ取り付け_1次ﾌｨﾙﾀｰ品名
        mapping.put("filtertorituke_lotno1", "filtertorituke_lotno1");                           // ﾌｨﾙﾀｰ取り付け_LotNo1
        mapping.put("filtertorituke_toritukehonsuu1", "filtertorituke_toritukehonsuu1");         // ﾌｨﾙﾀｰ取り付け_取り付け本数1
        mapping.put("filtertorituke_nijifilterhinmei", "filtertorituke_nijifilterhinmei");       // ﾌｨﾙﾀｰ取り付け_2次ﾌｨﾙﾀｰ品名
        mapping.put("filtertorituke_lotno2", "filtertorituke_lotno2");                           // ﾌｨﾙﾀｰ取り付け_LotNo2
        mapping.put("filtertorituke_toritukehonsuu2", "filtertorituke_toritukehonsuu2");         // ﾌｨﾙﾀｰ取り付け_取り付け本数2
        mapping.put("filtertorituke_sanjifilterhinmei", "filtertorituke_sanjifilterhinmei");     // ﾌｨﾙﾀｰ取り付け_3次ﾌｨﾙﾀｰ品名
        mapping.put("filtertorituke_lotno3", "filtertorituke_lotno3");                           // ﾌｨﾙﾀｰ取り付け_LotNo3
        mapping.put("filtertorituke_toritukehonsuu3", "filtertorituke_toritukehonsuu3");         // ﾌｨﾙﾀｰ取り付け_取り付け本数3
        mapping.put("filtertorituke_tantousya", "filtertorituke_tantousya");                     // ﾌｨﾙﾀｰ取り付け_担当者
        mapping.put("haisyutuyoukinoutibukuro", "haisyutuyoukinoutibukuro");                     // 排出容器の内袋
        mapping.put("filtersiyoukaisuu", "filtersiyoukaisuu");                                   // ﾌｨﾙﾀｰ使用回数
        mapping.put("Fptankno", "Fptankno");                                                     // F/PﾀﾝｸNo
        mapping.put("senjyoukakunin", "senjyoukakunin");                                         // 洗浄確認
        mapping.put("Fpkaisinichiji", "Fpkaisinichiji");                                         // F/P開始日時
        mapping.put("assouregulatorNo", "assouregulatorNo");                                     // 圧送ﾚｷﾞｭﾚｰﾀｰNo
        mapping.put("assouaturyoku", "assouaturyoku");                                           // 圧送圧力
        mapping.put("filterpasskaisi_tantousya", "filterpasskaisi_tantousya");                   // ﾌｨﾙﾀｰﾊﾟｽ開始_担当者
        mapping.put("filterkoukan1_Fpteisinichiji", "filterkoukan1_Fpteisinichiji");             // ﾌｨﾙﾀｰ交換①_F/P停止日時
        mapping.put("filterkoukan1_itijifilterhinmei", "filterkoukan1_itijifilterhinmei");       // ﾌｨﾙﾀｰ交換①_1次ﾌｨﾙﾀｰ品名
        mapping.put("filterkoukan1_lotno1", "filterkoukan1_lotno1");                             // ﾌｨﾙﾀｰ交換①_LotNo1
        mapping.put("filterkoukan1_toritukehonsuu1", "filterkoukan1_toritukehonsuu1");           // ﾌｨﾙﾀｰ交換①_取り付け本数1
        mapping.put("filterkoukan1_nijifilterhinmei", "filterkoukan1_nijifilterhinmei");         // ﾌｨﾙﾀｰ交換①_2次ﾌｨﾙﾀｰ品名
        mapping.put("filterkoukan1_lotno2", "filterkoukan1_lotno2");                             // ﾌｨﾙﾀｰ交換①_LotNo2
        mapping.put("filterkoukan1_toritukehonsuu2", "filterkoukan1_toritukehonsuu2");           // ﾌｨﾙﾀｰ交換①_取り付け本数2
        mapping.put("filterkoukan1_sanjifilterhinmei", "filterkoukan1_sanjifilterhinmei");       // ﾌｨﾙﾀｰ交換①_3次ﾌｨﾙﾀｰ品名
        mapping.put("filterkoukan1_lotno3", "filterkoukan1_lotno3");                             // ﾌｨﾙﾀｰ交換①_LotNo3
        mapping.put("filterkoukan1_toritukehonsuu3", "filterkoukan1_toritukehonsuu3");           // ﾌｨﾙﾀｰ交換①_取り付け本数3
        mapping.put("filterkoukan1_Fpsaikainichiji", "filterkoukan1_Fpsaikainichiji");           // ﾌｨﾙﾀｰ交換①_F/P再開日時
        mapping.put("filterkoukan1_tantousya", "filterkoukan1_tantousya");                       // ﾌｨﾙﾀｰ交換①_担当者
        mapping.put("FPsyuuryounichiji", "FPsyuuryounichiji");                                   // F/P終了日時
        mapping.put("FPtotaljikan", "FPtotaljikan");                                             // F/Pﾄｰﾀﾙ時間
        mapping.put("itijifiltersousiyouhonsuu", "itijifiltersousiyouhonsuu");                   // 1次ﾌｨﾙﾀｰ総使用本数
        mapping.put("nijifiltersousiyouhonsuu", "nijifiltersousiyouhonsuu");                     // 2次ﾌｨﾙﾀｰ総使用本数
        mapping.put("sanjifiltersousiyouhonsuu", "sanjifiltersousiyouhonsuu");                   // 3次ﾌｨﾙﾀｰ総使用本数
        mapping.put("filterpasssyuuryou_tantousya", "filterpasssyuuryou_tantousya");             // ﾌｨﾙﾀｰﾊﾟｽ終了_担当者
        mapping.put("soujyuurousokutei1", "soujyuurousokutei1");                                 // 総重量測定1
        mapping.put("soujyuurousokutei2", "soujyuurousokutei2");                                 // 総重量測定2
        mapping.put("soujyuurousokutei3", "soujyuurousokutei3");                                 // 総重量測定3
        mapping.put("soujyuurousokutei4", "soujyuurousokutei4");                                 // 総重量測定4
        mapping.put("soujyuurousokutei5", "soujyuurousokutei5");                                 // 総重量測定5
        mapping.put("soujyuurousokutei6", "soujyuurousokutei6");                                 // 総重量測定6
        mapping.put("soujyuurousokutei7", "soujyuurousokutei7");                                 // 総重量測定7
        mapping.put("soujyuurousokutei8", "soujyuurousokutei8");                                 // 総重量測定8
        mapping.put("soujyuurousokutei9", "soujyuurousokutei9");                                 // 総重量測定9
        mapping.put("soujyuurousokutei10", "soujyuurousokutei10");                               // 総重量測定10
        mapping.put("soujyuurousokutei11", "soujyuurousokutei11");                               // 総重量測定11
        mapping.put("soujyuurousokutei12", "soujyuurousokutei12");                               // 総重量測定12
        mapping.put("yuudentaislurryjyuurou1", "yuudentaislurryjyuurou1");                       // 誘電体ｽﾗﾘｰ重量1
        mapping.put("yuudentaislurryjyuurou2", "yuudentaislurryjyuurou2");                       // 誘電体ｽﾗﾘｰ重量2
        mapping.put("yuudentaislurryjyuurou3", "yuudentaislurryjyuurou3");                       // 誘電体ｽﾗﾘｰ重量3
        mapping.put("yuudentaislurryjyuurou4", "yuudentaislurryjyuurou4");                       // 誘電体ｽﾗﾘｰ重量4
        mapping.put("yuudentaislurryjyuurou5", "yuudentaislurryjyuurou5");                       // 誘電体ｽﾗﾘｰ重量5
        mapping.put("yuudentaislurryjyuurou6", "yuudentaislurryjyuurou6");                       // 誘電体ｽﾗﾘｰ重量6
        mapping.put("yuudentaislurryjyuurou7", "yuudentaislurryjyuurou7");                       // 誘電体ｽﾗﾘｰ重量7
        mapping.put("yuudentaislurryjyuurou8", "yuudentaislurryjyuurou8");                       // 誘電体ｽﾗﾘｰ重量8
        mapping.put("yuudentaislurryjyuurou9", "yuudentaislurryjyuurou9");                       // 誘電体ｽﾗﾘｰ重量9
        mapping.put("yuudentaislurryjyuurou10", "yuudentaislurryjyuurou10");                     // 誘電体ｽﾗﾘｰ重量10
        mapping.put("yuudentaislurryjyuurou11", "yuudentaislurryjyuurou11");                     // 誘電体ｽﾗﾘｰ重量11
        mapping.put("yuudentaislurryjyuurou12", "yuudentaislurryjyuurou12");                     // 誘電体ｽﾗﾘｰ重量12
        mapping.put("yuudentaislurryjyuurougoukei", "yuudentaislurryjyuurougoukei");             // 誘電体ｽﾗﾘｰ重量合計
        mapping.put("tounyuuryou", "tounyuuryou");                                               // 投入量
        mapping.put("budomarikeisan", "budomarikeisan");                                         // 歩留まり計算
        mapping.put("uudentaislurryyuukoukigen", "uudentaislurryyuukoukigen");                   // 誘電体ｽﾗﾘｰ有効期限
        mapping.put("funsaihantei", "funsaihantei");                                             // 粉砕判定
        mapping.put("seihinjyuuryoukakunin_tantousya", "seihinjyuuryoukakunin_tantousya");       // 製品重量確認_担当者
        mapping.put("hozonyousamplekaisyu", "hozonyousamplekaisyu");                             // 保存用ｻﾝﾌﾟﾙ回収
        mapping.put("zunsekiyousamplekaisyu", "zunsekiyousamplekaisyu");                         // 分析用ｻﾝﾌﾟﾙ回収
        mapping.put("kansouzara", "kansouzara");                                                 // 乾燥皿
        mapping.put("arumizarafuutaijyuuryou", "arumizarafuutaijyuuryou");                       // ｱﾙﾐ皿風袋重量
        mapping.put("slurrysamplejyuuryoukikaku", "slurrysamplejyuuryoukikaku");                 // ｽﾗﾘｰｻﾝﾌﾟﾙ重量規格
        mapping.put("kansoumaeslurryjyuuryou", "kansoumaeslurryjyuuryou");                       // 乾燥前ｽﾗﾘｰ重量
        mapping.put("kansouki", "kansouki");                                                     // 乾燥機
        mapping.put("kansouondokikaku", "kansouondokikaku");                                     // 乾燥温度規格
        mapping.put("kansoujikankikaku", "kansoujikankikaku");                                   // 乾燥時間規格
        mapping.put("kansoukaisinichiji", "kansoukaisinichiji");                                 // 乾燥開始日時
        mapping.put("kansousyuuryounichiji", "kansousyuuryounichiji");                           // 乾燥終了日時
        mapping.put("kansoujikantotal", "kansoujikantotal");                                     // 乾燥時間ﾄｰﾀﾙ
        mapping.put("kansougosoujyuuryou", "kansougosoujyuuryou");                               // 乾燥後総重量
        mapping.put("kansougosyoumijyuuryou", "kansougosyoumijyuuryou");                         // 乾燥後正味重量
        mapping.put("kokeibunhiritu", "kokeibunhiritu");                                         // 固形分比率
        mapping.put("bikou1", "bikou1");                                                         // 備考1
        mapping.put("bikou2", "bikou2");                                                         // 備考2
        mapping.put("torokunichiji", "torokunichiji");                                           // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                                             // 更新日時
        mapping.put("revision", "revision");                                                     // revision
        mapping.put("deleteflag", "deleteflag");                                                 // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrYuudentaiFp>> beanHandler = new BeanListHandler<>(SrYuudentaiFp.class, rowProcessor);

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
     * @param srYuudentaiFp 誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管データ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrYuudentaiFp srYuudentaiFp) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srYuudentaiFp != null) {
            // 元データが存在する場合元データより取得
            return getSrYuudentaiFpItemData(itemId, srYuudentaiFp);
        } else {
            return null;
        }
    }

    /**
     * 項目データ(入力値)取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @param srGlasshyoryo 誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管データ
     * @return 入力値
     */
    private String getItemKikakuchi(List<FXHDD01> listData, String itemId, SrYuudentaiFp srYuudentaiFp) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return StringUtil.nullToBlank(selectData.get(0).getKikakuChi()).replace("【", "").replace("】", "");
        } else if (srYuudentaiFp != null) {
            // 元データが存在する場合元データより取得
            return getSrYuudentaiFpItemData(itemId, srYuudentaiFp);
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
     * 誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管_仮登録(tmp_sr_yuudentai_fp)登録処理
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
    private void insertTmpSrYuudentaiFp(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "INSERT INTO tmp_sr_yuudentai_fp ( "
                + " kojyo,lotno,edaban,yuudentaislurryhinmei,yuudentaislurrylotno,lotkubun,genryoulotno,genryoukigou,"
                + "fuutaijyuuryousokutei_sutenyouki1,fuutaijyuuryousokutei_sutenyouki2,fuutaijyuuryousokutei_sutenyouki3,"
                + "fuutaijyuuryousokutei_sutenyouki4,fuutaijyuuryousokutei_sutenyouki5,fuutaijyuuryousokutei_sutenyouki6,"
                + "fuutaijyuuryousokutei_siropori1,fuutaijyuuryousokutei_siropori2,fuutaijyuuryousokutei_siropori3,fuutaijyuuryousokutei_siropori4,"
                + "fuutaijyuuryousokutei_siropori5,fuutaijyuuryousokutei_siropori6,fuutaijyuuryousokutei_siropori7,fuutaijyuuryousokutei_siropori8,"
                + "fuutaijyuuryousokutei_siropori9,fuutaijyuuryousokutei_siropori10,fuutaijyuuryousokutei_siropori11,fuutaijyuuryousokutei_siropori12,"
                + "fuutaijyuuryousokutei_tantousya,filterrenketu,filtertorituke_itijifilterhinmei,filtertorituke_lotno1,filtertorituke_toritukehonsuu1,"
                + "filtertorituke_nijifilterhinmei,filtertorituke_lotno2,filtertorituke_toritukehonsuu2,filtertorituke_sanjifilterhinmei,"
                + "filtertorituke_lotno3,filtertorituke_toritukehonsuu3,filtertorituke_tantousya,haisyutuyoukinoutibukuro,filtersiyoukaisuu,"
                + "Fptankno,senjyoukakunin,Fpkaisinichiji,assouregulatorNo,assouaturyoku,filterpasskaisi_tantousya,filterkoukan1_Fpteisinichiji,"
                + "filterkoukan1_itijifilterhinmei,filterkoukan1_lotno1,filterkoukan1_toritukehonsuu1,filterkoukan1_nijifilterhinmei,filterkoukan1_lotno2,"
                + "filterkoukan1_toritukehonsuu2,filterkoukan1_sanjifilterhinmei,filterkoukan1_lotno3,filterkoukan1_toritukehonsuu3,"
                + "filterkoukan1_Fpsaikainichiji,filterkoukan1_tantousya,FPsyuuryounichiji,FPtotaljikan,itijifiltersousiyouhonsuu,"
                + "nijifiltersousiyouhonsuu,sanjifiltersousiyouhonsuu,filterpasssyuuryou_tantousya,soujyuurousokutei1,soujyuurousokutei2,"
                + "soujyuurousokutei3,soujyuurousokutei4,soujyuurousokutei5,soujyuurousokutei6,soujyuurousokutei7,soujyuurousokutei8,"
                + "soujyuurousokutei9,soujyuurousokutei10,soujyuurousokutei11,soujyuurousokutei12,yuudentaislurryjyuurou1,yuudentaislurryjyuurou2,"
                + "yuudentaislurryjyuurou3,yuudentaislurryjyuurou4,yuudentaislurryjyuurou5,yuudentaislurryjyuurou6,yuudentaislurryjyuurou7,"
                + "yuudentaislurryjyuurou8,yuudentaislurryjyuurou9,yuudentaislurryjyuurou10,yuudentaislurryjyuurou11,yuudentaislurryjyuurou12,"
                + "yuudentaislurryjyuurougoukei,tounyuuryou,budomarikeisan,uudentaislurryyuukoukigen,funsaihantei,seihinjyuuryoukakunin_tantousya,"
                + "hozonyousamplekaisyu,zunsekiyousamplekaisyu,kansouzara,arumizarafuutaijyuuryou,slurrysamplejyuuryoukikaku,kansoumaeslurryjyuuryou,"
                + "kansouki,kansouondokikaku,kansoujikankikaku,kansoukaisinichiji,kansousyuuryounichiji,kansoujikantotal,kansougosoujyuuryou,"
                + "kansougosyoumijyuuryou,kokeibunhiritu,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrYuudentaiFp(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, processData, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管_仮登録(tmp_sr_yuudentai_fp)更新処理
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
    private void updateTmpSrYuudentaiFp(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "UPDATE tmp_sr_yuudentai_fp SET "
                + "yuudentaislurryhinmei = ?,yuudentaislurrylotno = ?,lotkubun = ?,genryoulotno = ?,genryoukigou = ?,fuutaijyuuryousokutei_sutenyouki1 = ?,"
                + "fuutaijyuuryousokutei_sutenyouki2 = ?,fuutaijyuuryousokutei_sutenyouki3 = ?,fuutaijyuuryousokutei_sutenyouki4 = ?,fuutaijyuuryousokutei_sutenyouki5 = ?,"
                + "fuutaijyuuryousokutei_sutenyouki6 = ?,fuutaijyuuryousokutei_siropori1 = ?,fuutaijyuuryousokutei_siropori2 = ?,fuutaijyuuryousokutei_siropori3 = ?,"
                + "fuutaijyuuryousokutei_siropori4 = ?,fuutaijyuuryousokutei_siropori5 = ?,fuutaijyuuryousokutei_siropori6 = ?,fuutaijyuuryousokutei_siropori7 = ?,"
                + "fuutaijyuuryousokutei_siropori8 = ?,fuutaijyuuryousokutei_siropori9 = ?,fuutaijyuuryousokutei_siropori10 = ?,fuutaijyuuryousokutei_siropori11 = ?,"
                + "fuutaijyuuryousokutei_siropori12 = ?,fuutaijyuuryousokutei_tantousya = ?,filterrenketu = ?,filtertorituke_itijifilterhinmei = ?,filtertorituke_lotno1 = ?,"
                + "filtertorituke_toritukehonsuu1 = ?,filtertorituke_nijifilterhinmei = ?,filtertorituke_lotno2 = ?,filtertorituke_toritukehonsuu2 = ?,"
                + "filtertorituke_sanjifilterhinmei = ?,filtertorituke_lotno3 = ?,filtertorituke_toritukehonsuu3 = ?,filtertorituke_tantousya = ?,haisyutuyoukinoutibukuro = ?,"
                + "filtersiyoukaisuu = ?,Fptankno = ?,senjyoukakunin = ?,Fpkaisinichiji = ?,assouregulatorNo = ?,assouaturyoku = ?,filterpasskaisi_tantousya = ?,"
                + "filterkoukan1_Fpteisinichiji = ?,filterkoukan1_itijifilterhinmei = ?,filterkoukan1_lotno1 = ?,filterkoukan1_toritukehonsuu1 = ?,"
                + "filterkoukan1_nijifilterhinmei = ?,filterkoukan1_lotno2 = ?,filterkoukan1_toritukehonsuu2 = ?,filterkoukan1_sanjifilterhinmei = ?,filterkoukan1_lotno3 = ?,"
                + "filterkoukan1_toritukehonsuu3 = ?,filterkoukan1_Fpsaikainichiji = ?,filterkoukan1_tantousya = ?,FPsyuuryounichiji = ?,FPtotaljikan = ?,"
                + "itijifiltersousiyouhonsuu = ?,nijifiltersousiyouhonsuu = ?,sanjifiltersousiyouhonsuu = ?,filterpasssyuuryou_tantousya = ?,soujyuurousokutei1 = ?,"
                + "soujyuurousokutei2 = ?,soujyuurousokutei3 = ?,soujyuurousokutei4 = ?,soujyuurousokutei5 = ?,soujyuurousokutei6 = ?,soujyuurousokutei7 = ?,"
                + "soujyuurousokutei8 = ?,soujyuurousokutei9 = ?,soujyuurousokutei10 = ?,soujyuurousokutei11 = ?,soujyuurousokutei12 = ?,yuudentaislurryjyuurou1 = ?,"
                + "yuudentaislurryjyuurou2 = ?,yuudentaislurryjyuurou3 = ?,yuudentaislurryjyuurou4 = ?,yuudentaislurryjyuurou5 = ?,yuudentaislurryjyuurou6 = ?,"
                + "yuudentaislurryjyuurou7 = ?,yuudentaislurryjyuurou8 = ?,yuudentaislurryjyuurou9 = ?,yuudentaislurryjyuurou10 = ?,yuudentaislurryjyuurou11 = ?,"
                + "yuudentaislurryjyuurou12 = ?,yuudentaislurryjyuurougoukei = ?,tounyuuryou = ?,budomarikeisan = ?,uudentaislurryyuukoukigen = ?,funsaihantei = ?,"
                + "seihinjyuuryoukakunin_tantousya = ?,hozonyousamplekaisyu = ?,zunsekiyousamplekaisyu = ?,kansouzara = ?,arumizarafuutaijyuuryou = ?,"
                + "slurrysamplejyuuryoukikaku = ?,kansoumaeslurryjyuuryou = ?,kansouki = ?,kansouondokikaku = ?,kansoujikankikaku = ?,kansoukaisinichiji = ?,"
                + "kansousyuuryounichiji = ?,kansoujikantotal = ?,kansougosoujyuuryou = ?,kansougosyoumijyuuryou = ?,kokeibunhiritu = ?,bikou1 = ?,bikou2 = ?,"
                + "kosinnichiji = ?,revision = ?,deleteflag = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrYuudentaiFp> srYuudentaiFpList = getSrYuudentaiFpData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrYuudentaiFp srYuudentaiFp = null;
        if (!srYuudentaiFpList.isEmpty()) {
            srYuudentaiFp = srYuudentaiFpList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrYuudentaiFp(false, newRev, 0, "", "", "", systemTime, processData, srYuudentaiFp);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管_仮登録(tmp_sr_yuudentai_fp)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrYuudentaiFp(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sr_yuudentai_fp "
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
     * 誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管_仮登録(tmp_sr_yuudentai_fp)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srYuudentaiFp 誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管データ
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrYuudentaiFp(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, String systemTime, ProcessData processData, SrYuudentaiFp srYuudentaiFp) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // F/P開始日時
        String fpkaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B026Const.FPKAISI_TIME, srYuudentaiFp));
        // F/P終了日時
        String fpsyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B026Const.FPSYUURYOU_TIME, srYuudentaiFp));
        // F/P停止日時
        String filterkoukan1fpteisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B026Const.FILTERKOUKAN1_FPTEISI_TIME, srYuudentaiFp));
        // F/P再開日時
        String filterkoukan1fpsaikaiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B026Const.FILTERKOUKAN1_FPSAIKAI_TIME, srYuudentaiFp));
        // 乾燥開始日時
        String kansoukaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B026Const.KANSOUKAISI_TIME, srYuudentaiFp));
        // 乾燥終了日時
        String kansousyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B026Const.KANSOUSYUURYOU_TIME, srYuudentaiFp));
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.YUUDENTAISLURRYHINMEI, srYuudentaiFp)));                    // 誘電体ｽﾗﾘｰ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.YUUDENTAISLURRYLOTNO, srYuudentaiFp)));                     // 誘電体ｽﾗﾘｰLotNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.LOTKUBUN, srYuudentaiFp)));                                 // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B026Const.GENRYOULOTNO, srYuudentaiFp)));                        // 原料LotNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B026Const.GENRYOUKIGOU, srYuudentaiFp)));                        // 原料記号
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI1, srYuudentaiFp)));           // 風袋重量測定_ｽﾃﾝ容器1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI2, srYuudentaiFp)));           // 風袋重量測定_ｽﾃﾝ容器2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI3, srYuudentaiFp)));           // 風袋重量測定_ｽﾃﾝ容器3
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI4, srYuudentaiFp)));           // 風袋重量測定_ｽﾃﾝ容器4
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI5, srYuudentaiFp)));           // 風袋重量測定_ｽﾃﾝ容器5
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI6, srYuudentaiFp)));           // 風袋重量測定_ｽﾃﾝ容器6
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI1, srYuudentaiFp)));             // 風袋重量測定_白ﾎﾟﾘ容器1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI2, srYuudentaiFp)));             // 風袋重量測定_白ﾎﾟﾘ容器2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI3, srYuudentaiFp)));             // 風袋重量測定_白ﾎﾟﾘ容器3
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI4, srYuudentaiFp)));             // 風袋重量測定_白ﾎﾟﾘ容器4
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI5, srYuudentaiFp)));             // 風袋重量測定_白ﾎﾟﾘ容器5
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI6, srYuudentaiFp)));             // 風袋重量測定_白ﾎﾟﾘ容器6
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI7, srYuudentaiFp)));             // 風袋重量測定_白ﾎﾟﾘ容器7
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI8, srYuudentaiFp)));             // 風袋重量測定_白ﾎﾟﾘ容器8
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI9, srYuudentaiFp)));             // 風袋重量測定_白ﾎﾟﾘ容器9
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI10, srYuudentaiFp)));            // 風袋重量測定_白ﾎﾟﾘ容器10
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI11, srYuudentaiFp)));            // 風袋重量測定_白ﾎﾟﾘ容器11
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI12, srYuudentaiFp)));            // 風袋重量測定_白ﾎﾟﾘ容器12
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_TANTOUSYA, srYuudentaiFp)));          // 保管容器準備_担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B026Const.FILTERRENKETU, srYuudentaiFp)));                       // ﾌｨﾙﾀｰ連結
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B026Const.FILTERTORITUKE_ITIJIFILTERHINMEI, srYuudentaiFp)));    // ﾌｨﾙﾀｰ取り付け_1次ﾌｨﾙﾀｰ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FILTERTORITUKE_LOTNO1, srYuudentaiFp)));                    // ﾌｨﾙﾀｰ取り付け_LotNo1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FILTERTORITUKE_TORITUKEHONSUU1, srYuudentaiFp)));              // ﾌｨﾙﾀｰ取り付け_取り付け本数1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B026Const.FILTERTORITUKE_NIJIFILTERHINMEI, srYuudentaiFp)));     // ﾌｨﾙﾀｰ取り付け_2次ﾌｨﾙﾀｰ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FILTERTORITUKE_LOTNO2, srYuudentaiFp)));                    // ﾌｨﾙﾀｰ取り付け_LotNo2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FILTERTORITUKE_TORITUKEHONSUU2, srYuudentaiFp)));              // ﾌｨﾙﾀｰ取り付け_取り付け本数2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B026Const.FILTERTORITUKE_SANJIFILTERHINMEI, srYuudentaiFp)));    // ﾌｨﾙﾀｰ取り付け_3次ﾌｨﾙﾀｰ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FILTERTORITUKE_LOTNO3, srYuudentaiFp)));                    // ﾌｨﾙﾀｰ取り付け_LotNo3
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FILTERTORITUKE_TORITUKEHONSUU3, srYuudentaiFp)));              // ﾌｨﾙﾀｰ取り付け_取り付け本数3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FILTERTORITUKE_TANTOUSYA, srYuudentaiFp)));                 // ﾌｨﾙﾀｰ取り付け_担当者
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B026Const.HAISYUTUYOUKINOUTIBUKURO, srYuudentaiFp), null));                               // 排出容器の内袋
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B026Const.FILTERSIYOUKAISUU, srYuudentaiFp)));                   // ﾌｨﾙﾀｰ使用回数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FPTANKNO, srYuudentaiFp)));                                 // F/PﾀﾝｸNo
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B026Const.SENJYOUKAKUNIN, srYuudentaiFp), null));                                         // 洗浄確認
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FPKAISI_DAY, srYuudentaiFp),
                "".equals(fpkaisiTime) ? "0000" : fpkaisiTime));                                                                                               // F/P開始日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.ASSOUREGULATORNO, srYuudentaiFp)));                         // 圧送ﾚｷﾞｭﾚｰﾀｰNo
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.ASSOUATURYOKU, srYuudentaiFp)));                        // 圧送圧力
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FILTERPASSKAISI_TANTOUSYA, srYuudentaiFp)));                // ﾌｨﾙﾀｰﾊﾟｽ開始_担当者
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FILTERKOUKAN1_FPTEISI_DAY, srYuudentaiFp),
                "".equals(filterkoukan1fpteisiTime) ? "0000" : filterkoukan1fpteisiTime));                                                                     // ﾌｨﾙﾀｰ交換①_F/P停止日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B026Const.FILTERKOUKAN1_ITIJIFILTERHINMEI, srYuudentaiFp)));     // ﾌｨﾙﾀｰ交換①_1次ﾌｨﾙﾀｰ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FILTERKOUKAN1_LOTNO1, srYuudentaiFp)));                     // ﾌｨﾙﾀｰ交換①_LotNo1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FILTERKOUKAN1_TORITUKEHONSUU1, srYuudentaiFp)));               // ﾌｨﾙﾀｰ交換①_取り付け本数1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B026Const.FILTERKOUKAN1_NIJIFILTERHINMEI, srYuudentaiFp)));      // ﾌｨﾙﾀｰ交換①_2次ﾌｨﾙﾀｰ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FILTERKOUKAN1_LOTNO2, srYuudentaiFp)));                     // ﾌｨﾙﾀｰ交換①_LotNo2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FILTERKOUKAN1_TORITUKEHONSUU2, srYuudentaiFp)));               // ﾌｨﾙﾀｰ交換①_取り付け本数2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B026Const.FILTERKOUKAN1_SANJIFILTERHINMEI, srYuudentaiFp)));     // ﾌｨﾙﾀｰ交換①_3次ﾌｨﾙﾀｰ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FILTERKOUKAN1_LOTNO3, srYuudentaiFp)));                     // ﾌｨﾙﾀｰ交換①_LotNo3
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FILTERKOUKAN1_TORITUKEHONSUU3, srYuudentaiFp)));               // ﾌｨﾙﾀｰ交換①_取り付け本数3
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FILTERKOUKAN1_FPSAIKAI_DAY, srYuudentaiFp),
                "".equals(filterkoukan1fpsaikaiTime) ? "0000" : filterkoukan1fpsaikaiTime));                                                                   // ﾌｨﾙﾀｰ交換①_F/P再開日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FILTERKOUKAN1_TANTOUSYA, srYuudentaiFp)));                  // ﾌｨﾙﾀｰ交換①_担当者
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FPSYUURYOU_DAY, srYuudentaiFp),
                "".equals(fpsyuuryouTime) ? "0000" : fpsyuuryouTime));                                                                                         // F/P終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FPTOTALJIKAN, srYuudentaiFp)));                             // F/Pﾄｰﾀﾙ時間
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.ITIJIFILTERSOUSIYOUHONSUU, srYuudentaiFp)));                   // 1次ﾌｨﾙﾀｰ総使用本数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.NIJIFILTERSOUSIYOUHONSUU, srYuudentaiFp)));                    // 2次ﾌｨﾙﾀｰ総使用本数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.SANJIFILTERSOUSIYOUHONSUU, srYuudentaiFp)));                   // 3次ﾌｨﾙﾀｰ総使用本数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.FILTERPASSSYUURYOU_TANTOUSYA, srYuudentaiFp)));             // ﾌｨﾙﾀｰﾊﾟｽ終了_担当者
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.SOUJYUUROUSOKUTEI1, srYuudentaiFp)));                          // 総重量測定1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.SOUJYUUROUSOKUTEI2, srYuudentaiFp)));                          // 総重量測定2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.SOUJYUUROUSOKUTEI3, srYuudentaiFp)));                          // 総重量測定3
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.SOUJYUUROUSOKUTEI4, srYuudentaiFp)));                          // 総重量測定4
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.SOUJYUUROUSOKUTEI5, srYuudentaiFp)));                          // 総重量測定5
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.SOUJYUUROUSOKUTEI6, srYuudentaiFp)));                          // 総重量測定6
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.SOUJYUUROUSOKUTEI7, srYuudentaiFp)));                          // 総重量測定7
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.SOUJYUUROUSOKUTEI8, srYuudentaiFp)));                          // 総重量測定8
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.SOUJYUUROUSOKUTEI9, srYuudentaiFp)));                          // 総重量測定9
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.SOUJYUUROUSOKUTEI10, srYuudentaiFp)));                         // 総重量測定10
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.SOUJYUUROUSOKUTEI11, srYuudentaiFp)));                         // 総重量測定11
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.SOUJYUUROUSOKUTEI12, srYuudentaiFp)));                         // 総重量測定12
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU1, srYuudentaiFp)));                     // 誘電体ｽﾗﾘｰ重量1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU2, srYuudentaiFp)));                     // 誘電体ｽﾗﾘｰ重量2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU3, srYuudentaiFp)));                     // 誘電体ｽﾗﾘｰ重量3
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU4, srYuudentaiFp)));                     // 誘電体ｽﾗﾘｰ重量4
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU5, srYuudentaiFp)));                     // 誘電体ｽﾗﾘｰ重量5
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU6, srYuudentaiFp)));                     // 誘電体ｽﾗﾘｰ重量6
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU7, srYuudentaiFp)));                     // 誘電体ｽﾗﾘｰ重量7
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU8, srYuudentaiFp)));                     // 誘電体ｽﾗﾘｰ重量8
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU9, srYuudentaiFp)));                     // 誘電体ｽﾗﾘｰ重量9
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU10, srYuudentaiFp)));                    // 誘電体ｽﾗﾘｰ重量10
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU11, srYuudentaiFp)));                    // 誘電体ｽﾗﾘｰ重量11
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU12, srYuudentaiFp)));                    // 誘電体ｽﾗﾘｰ重量12
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.YUUDENTAISLURRYJYUUROUGOUKEI, srYuudentaiFp)));                // 誘電体ｽﾗﾘｰ重量合計
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B026Const.TOUNYUURYOU, srYuudentaiFp)));                         // 投入量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.BUDOMARIKEISAN, srYuudentaiFp)));                       // 歩留まり計算
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.UUDENTAISLURRYYUUKOUKIGEN, srYuudentaiFp)));                // 誘電体ｽﾗﾘｰ有効期限
        params.add(getComboBoxDbValue(getItemData(pItemList, GXHDO102B026Const.FUNSAIHANTEI, srYuudentaiFp), null));                                           // 粉砕判定
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.SEIHINJYUURYOUKAKUNIN_TANTOUSYA, srYuudentaiFp)));          // 製品重量確認_担当者
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B026Const.HOZONYOUSAMPLEKAISYU, srYuudentaiFp), null));                                   // 保存用ｻﾝﾌﾟﾙ回収
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B026Const.ZUNSEKIYOUSAMPLEKAISYU, srYuudentaiFp), null));                                 // 分析用ｻﾝﾌﾟﾙ回収
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B026Const.KANSOUZARA, srYuudentaiFp)));                          // 乾燥皿
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.ARUMIZARAFUUTAIJYUURYOU, srYuudentaiFp)));                     // ｱﾙﾐ皿風袋重量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B026Const.SLURRYSAMPLEJYUURYOUKIKAKU, srYuudentaiFp)));          // ｽﾗﾘｰｻﾝﾌﾟﾙ重量規格
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.KANSOUMAESLURRYJYUURYOU, srYuudentaiFp)));                     // 乾燥前ｽﾗﾘｰ重量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B026Const.KANSOUKI, srYuudentaiFp)));                            // 乾燥機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B026Const.KANSOUONDOKIKAKU, srYuudentaiFp)));                    // 乾燥温度規格
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B026Const.KANSOUJIKANKIKAKU, srYuudentaiFp)));                   // 乾燥時間規格
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.KANSOUKAISI_DAY, srYuudentaiFp),
                "".equals(kansoukaisiTime) ? "0000" : kansoukaisiTime));                                                                                       // 乾燥開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.KANSOUSYUURYOU_DAY, srYuudentaiFp),
                "".equals(kansousyuuryouTime) ? "0000" : kansousyuuryouTime));                                                                                 // 乾燥終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.KANSOUJIKANTOTAL, srYuudentaiFp)));                         // 乾燥時間ﾄｰﾀﾙ
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.KANSOUGOSOUJYUURYOU, srYuudentaiFp)));                  // 乾燥後総重量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.KANSOUGOSYOUMIJYUURYOU, srYuudentaiFp)));               // 乾燥後正味重量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.KOKEIBUNHIRITU, srYuudentaiFp)));                       // 固形分比率
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.BIKOU1, srYuudentaiFp)));                                   // 備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B026Const.BIKOU2, srYuudentaiFp)));                                   // 備考2

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
     * 誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管(sr_yuudentai_fp)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param tmpSrYuudentaiFp 仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrYuudentaiFp(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData, SrYuudentaiFp tmpSrYuudentaiFp) throws SQLException {

        String sql = "INSERT INTO sr_yuudentai_fp ( "
                + " kojyo,lotno,edaban,yuudentaislurryhinmei,yuudentaislurrylotno,lotkubun,genryoulotno,genryoukigou,"
                + "fuutaijyuuryousokutei_sutenyouki1,fuutaijyuuryousokutei_sutenyouki2,fuutaijyuuryousokutei_sutenyouki3,"
                + "fuutaijyuuryousokutei_sutenyouki4,fuutaijyuuryousokutei_sutenyouki5,fuutaijyuuryousokutei_sutenyouki6,"
                + "fuutaijyuuryousokutei_siropori1,fuutaijyuuryousokutei_siropori2,fuutaijyuuryousokutei_siropori3,fuutaijyuuryousokutei_siropori4,"
                + "fuutaijyuuryousokutei_siropori5,fuutaijyuuryousokutei_siropori6,fuutaijyuuryousokutei_siropori7,fuutaijyuuryousokutei_siropori8,"
                + "fuutaijyuuryousokutei_siropori9,fuutaijyuuryousokutei_siropori10,fuutaijyuuryousokutei_siropori11,fuutaijyuuryousokutei_siropori12,"
                + "fuutaijyuuryousokutei_tantousya,filterrenketu,filtertorituke_itijifilterhinmei,filtertorituke_lotno1,filtertorituke_toritukehonsuu1,"
                + "filtertorituke_nijifilterhinmei,filtertorituke_lotno2,filtertorituke_toritukehonsuu2,filtertorituke_sanjifilterhinmei,"
                + "filtertorituke_lotno3,filtertorituke_toritukehonsuu3,filtertorituke_tantousya,haisyutuyoukinoutibukuro,filtersiyoukaisuu,"
                + "Fptankno,senjyoukakunin,Fpkaisinichiji,assouregulatorNo,assouaturyoku,filterpasskaisi_tantousya,filterkoukan1_Fpteisinichiji,"
                + "filterkoukan1_itijifilterhinmei,filterkoukan1_lotno1,filterkoukan1_toritukehonsuu1,filterkoukan1_nijifilterhinmei,filterkoukan1_lotno2,"
                + "filterkoukan1_toritukehonsuu2,filterkoukan1_sanjifilterhinmei,filterkoukan1_lotno3,filterkoukan1_toritukehonsuu3,"
                + "filterkoukan1_Fpsaikainichiji,filterkoukan1_tantousya,FPsyuuryounichiji,FPtotaljikan,itijifiltersousiyouhonsuu,"
                + "nijifiltersousiyouhonsuu,sanjifiltersousiyouhonsuu,filterpasssyuuryou_tantousya,soujyuurousokutei1,soujyuurousokutei2,"
                + "soujyuurousokutei3,soujyuurousokutei4,soujyuurousokutei5,soujyuurousokutei6,soujyuurousokutei7,soujyuurousokutei8,"
                + "soujyuurousokutei9,soujyuurousokutei10,soujyuurousokutei11,soujyuurousokutei12,yuudentaislurryjyuurou1,yuudentaislurryjyuurou2,"
                + "yuudentaislurryjyuurou3,yuudentaislurryjyuurou4,yuudentaislurryjyuurou5,yuudentaislurryjyuurou6,yuudentaislurryjyuurou7,"
                + "yuudentaislurryjyuurou8,yuudentaislurryjyuurou9,yuudentaislurryjyuurou10,yuudentaislurryjyuurou11,yuudentaislurryjyuurou12,"
                + "yuudentaislurryjyuurougoukei,tounyuuryou,budomarikeisan,uudentaislurryyuukoukigen,funsaihantei,seihinjyuuryoukakunin_tantousya,"
                + "hozonyousamplekaisyu,zunsekiyousamplekaisyu,kansouzara,arumizarafuutaijyuuryou,slurrysamplejyuuryoukikaku,kansoumaeslurryjyuuryou,"
                + "kansouki,kansouondokikaku,kansoujikankikaku,kansoukaisinichiji,kansousyuuryounichiji,kansoujikantotal,kansougosoujyuuryou,"
                + "kansougosyoumijyuuryou,kokeibunhiritu,bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrYuudentaiFp(true, newRev, kojyo, lotNo, edaban, systemTime, processData, tmpSrYuudentaiFp);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管(sr_yuudentai_fp)更新処理
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
    private void updateSrYuudentaiFp(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {
        String sql = "UPDATE sr_yuudentai_fp SET "
                + "yuudentaislurryhinmei = ?,yuudentaislurrylotno = ?,lotkubun = ?,genryoulotno = ?,genryoukigou = ?,fuutaijyuuryousokutei_sutenyouki1 = ?,"
                + "fuutaijyuuryousokutei_sutenyouki2 = ?,fuutaijyuuryousokutei_sutenyouki3 = ?,fuutaijyuuryousokutei_sutenyouki4 = ?,fuutaijyuuryousokutei_sutenyouki5 = ?,"
                + "fuutaijyuuryousokutei_sutenyouki6 = ?,fuutaijyuuryousokutei_siropori1 = ?,fuutaijyuuryousokutei_siropori2 = ?,fuutaijyuuryousokutei_siropori3 = ?,"
                + "fuutaijyuuryousokutei_siropori4 = ?,fuutaijyuuryousokutei_siropori5 = ?,fuutaijyuuryousokutei_siropori6 = ?,fuutaijyuuryousokutei_siropori7 = ?,"
                + "fuutaijyuuryousokutei_siropori8 = ?,fuutaijyuuryousokutei_siropori9 = ?,fuutaijyuuryousokutei_siropori10 = ?,fuutaijyuuryousokutei_siropori11 = ?,"
                + "fuutaijyuuryousokutei_siropori12 = ?,fuutaijyuuryousokutei_tantousya = ?,filterrenketu = ?,filtertorituke_itijifilterhinmei = ?,filtertorituke_lotno1 = ?,"
                + "filtertorituke_toritukehonsuu1 = ?,filtertorituke_nijifilterhinmei = ?,filtertorituke_lotno2 = ?,filtertorituke_toritukehonsuu2 = ?,"
                + "filtertorituke_sanjifilterhinmei = ?,filtertorituke_lotno3 = ?,filtertorituke_toritukehonsuu3 = ?,filtertorituke_tantousya = ?,haisyutuyoukinoutibukuro = ?,"
                + "filtersiyoukaisuu = ?,Fptankno = ?,senjyoukakunin = ?,Fpkaisinichiji = ?,assouregulatorNo = ?,assouaturyoku = ?,filterpasskaisi_tantousya = ?,"
                + "filterkoukan1_Fpteisinichiji = ?,filterkoukan1_itijifilterhinmei = ?,filterkoukan1_lotno1 = ?,filterkoukan1_toritukehonsuu1 = ?,"
                + "filterkoukan1_nijifilterhinmei = ?,filterkoukan1_lotno2 = ?,filterkoukan1_toritukehonsuu2 = ?,filterkoukan1_sanjifilterhinmei = ?,filterkoukan1_lotno3 = ?,"
                + "filterkoukan1_toritukehonsuu3 = ?,filterkoukan1_Fpsaikainichiji = ?,filterkoukan1_tantousya = ?,FPsyuuryounichiji = ?,FPtotaljikan = ?,"
                + "itijifiltersousiyouhonsuu = ?,nijifiltersousiyouhonsuu = ?,sanjifiltersousiyouhonsuu = ?,filterpasssyuuryou_tantousya = ?,soujyuurousokutei1 = ?,"
                + "soujyuurousokutei2 = ?,soujyuurousokutei3 = ?,soujyuurousokutei4 = ?,soujyuurousokutei5 = ?,soujyuurousokutei6 = ?,soujyuurousokutei7 = ?,"
                + "soujyuurousokutei8 = ?,soujyuurousokutei9 = ?,soujyuurousokutei10 = ?,soujyuurousokutei11 = ?,soujyuurousokutei12 = ?,yuudentaislurryjyuurou1 = ?,"
                + "yuudentaislurryjyuurou2 = ?,yuudentaislurryjyuurou3 = ?,yuudentaislurryjyuurou4 = ?,yuudentaislurryjyuurou5 = ?,yuudentaislurryjyuurou6 = ?,"
                + "yuudentaislurryjyuurou7 = ?,yuudentaislurryjyuurou8 = ?,yuudentaislurryjyuurou9 = ?,yuudentaislurryjyuurou10 = ?,yuudentaislurryjyuurou11 = ?,"
                + "yuudentaislurryjyuurou12 = ?,yuudentaislurryjyuurougoukei = ?,tounyuuryou = ?,budomarikeisan = ?,uudentaislurryyuukoukigen = ?,funsaihantei = ?,"
                + "seihinjyuuryoukakunin_tantousya = ?,hozonyousamplekaisyu = ?,zunsekiyousamplekaisyu = ?,kansouzara = ?,arumizarafuutaijyuuryou = ?,"
                + "slurrysamplejyuuryoukikaku = ?,kansoumaeslurryjyuuryou = ?,kansouki = ?,kansouondokikaku = ?,kansoujikankikaku = ?,kansoukaisinichiji = ?,"
                + "kansousyuuryounichiji = ?,kansoujikantotal = ?,kansougosoujyuuryou = ?,kansougosyoumijyuuryou = ?,kokeibunhiritu = ?,bikou1 = ?,bikou2 = ?,"
                + "kosinnichiji = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrYuudentaiFp> srYuudentaiFpList = getSrYuudentaiFpData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrYuudentaiFp srYuudentaiFp = null;
        if (!srYuudentaiFpList.isEmpty()) {
            srYuudentaiFp = srYuudentaiFpList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrYuudentaiFp(false, newRev, "", "", "", systemTime, processData, srYuudentaiFp);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管(sr_yuudentai_fp)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srYuudentaiFp 誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrYuudentaiFp(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            String systemTime, ProcessData processData, SrYuudentaiFp srYuudentaiFp) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // F/P開始日時
        String fpkaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B026Const.FPKAISI_TIME, srYuudentaiFp));
        // F/P終了日時
        String fpsyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B026Const.FPSYUURYOU_TIME, srYuudentaiFp));
        // F/P停止日時
        String filterkoukan1fpteisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B026Const.FILTERKOUKAN1_FPTEISI_TIME, srYuudentaiFp));
        // F/P再開日時
        String filterkoukan1fpsaikaiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B026Const.FILTERKOUKAN1_FPSAIKAI_TIME, srYuudentaiFp));
        // 乾燥開始日時
        String kansoukaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B026Const.KANSOUKAISI_TIME, srYuudentaiFp));
        // 乾燥終了日時
        String kansousyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B026Const.KANSOUSYUURYOU_TIME, srYuudentaiFp));

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B026Const.YUUDENTAISLURRYHINMEI, srYuudentaiFp)));                    // 誘電体ｽﾗﾘｰ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B026Const.YUUDENTAISLURRYLOTNO, srYuudentaiFp)));                     // 誘電体ｽﾗﾘｰLotNo
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B026Const.LOTKUBUN, srYuudentaiFp)));                                 // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B026Const.GENRYOULOTNO, srYuudentaiFp)));                        // 原料LotNo
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B026Const.GENRYOUKIGOU, srYuudentaiFp)));                        // 原料記号
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI1, srYuudentaiFp)));           // 風袋重量測定_ｽﾃﾝ容器1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI2, srYuudentaiFp)));           // 風袋重量測定_ｽﾃﾝ容器2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI3, srYuudentaiFp)));           // 風袋重量測定_ｽﾃﾝ容器3
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI4, srYuudentaiFp)));           // 風袋重量測定_ｽﾃﾝ容器4
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI5, srYuudentaiFp)));           // 風袋重量測定_ｽﾃﾝ容器5
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI6, srYuudentaiFp)));           // 風袋重量測定_ｽﾃﾝ容器6
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI1, srYuudentaiFp)));             // 風袋重量測定_白ﾎﾟﾘ容器1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI2, srYuudentaiFp)));             // 風袋重量測定_白ﾎﾟﾘ容器2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI3, srYuudentaiFp)));             // 風袋重量測定_白ﾎﾟﾘ容器3
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI4, srYuudentaiFp)));             // 風袋重量測定_白ﾎﾟﾘ容器4
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI5, srYuudentaiFp)));             // 風袋重量測定_白ﾎﾟﾘ容器5
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI6, srYuudentaiFp)));             // 風袋重量測定_白ﾎﾟﾘ容器6
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI7, srYuudentaiFp)));             // 風袋重量測定_白ﾎﾟﾘ容器7
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI8, srYuudentaiFp)));             // 風袋重量測定_白ﾎﾟﾘ容器8
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI9, srYuudentaiFp)));             // 風袋重量測定_白ﾎﾟﾘ容器9
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI10, srYuudentaiFp)));            // 風袋重量測定_白ﾎﾟﾘ容器10
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI11, srYuudentaiFp)));            // 風袋重量測定_白ﾎﾟﾘ容器11
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI12, srYuudentaiFp)));            // 風袋重量測定_白ﾎﾟﾘ容器12
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_TANTOUSYA, srYuudentaiFp)));          // 保管容器準備_担当者
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B026Const.FILTERRENKETU, srYuudentaiFp)));                       // ﾌｨﾙﾀｰ連結
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B026Const.FILTERTORITUKE_ITIJIFILTERHINMEI, srYuudentaiFp)));    // ﾌｨﾙﾀｰ取り付け_1次ﾌｨﾙﾀｰ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B026Const.FILTERTORITUKE_LOTNO1, srYuudentaiFp)));                    // ﾌｨﾙﾀｰ取り付け_LotNo1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.FILTERTORITUKE_TORITUKEHONSUU1, srYuudentaiFp)));              // ﾌｨﾙﾀｰ取り付け_取り付け本数1
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B026Const.FILTERTORITUKE_NIJIFILTERHINMEI, srYuudentaiFp)));     // ﾌｨﾙﾀｰ取り付け_2次ﾌｨﾙﾀｰ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B026Const.FILTERTORITUKE_LOTNO2, srYuudentaiFp)));                    // ﾌｨﾙﾀｰ取り付け_LotNo2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.FILTERTORITUKE_TORITUKEHONSUU2, srYuudentaiFp)));              // ﾌｨﾙﾀｰ取り付け_取り付け本数2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B026Const.FILTERTORITUKE_SANJIFILTERHINMEI, srYuudentaiFp)));    // ﾌｨﾙﾀｰ取り付け_3次ﾌｨﾙﾀｰ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B026Const.FILTERTORITUKE_LOTNO3, srYuudentaiFp)));                    // ﾌｨﾙﾀｰ取り付け_LotNo3
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.FILTERTORITUKE_TORITUKEHONSUU3, srYuudentaiFp)));              // ﾌｨﾙﾀｰ取り付け_取り付け本数3
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B026Const.FILTERTORITUKE_TANTOUSYA, srYuudentaiFp)));                 // ﾌｨﾙﾀｰ取り付け_担当者
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B026Const.HAISYUTUYOUKINOUTIBUKURO, srYuudentaiFp), 9));                       // 排出容器の内袋
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B026Const.FILTERSIYOUKAISUU, srYuudentaiFp)));                   // ﾌｨﾙﾀｰ使用回数
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B026Const.FPTANKNO, srYuudentaiFp)));                                 // F/PﾀﾝｸNo
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B026Const.SENJYOUKAKUNIN, srYuudentaiFp), 9));                                 // 洗浄確認
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B026Const.FPKAISI_DAY, srYuudentaiFp),
                "".equals(fpkaisiTime) ? "0000" : fpkaisiTime));                                                                                    // F/P開始日時
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B026Const.ASSOUREGULATORNO, srYuudentaiFp)));                            // 圧送ﾚｷﾞｭﾚｰﾀｰNo
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B026Const.ASSOUATURYOKU, srYuudentaiFp)));                        // 圧送圧力
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B026Const.FILTERPASSKAISI_TANTOUSYA, srYuudentaiFp)));                // ﾌｨﾙﾀｰﾊﾟｽ開始_担当者
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B026Const.FILTERKOUKAN1_FPTEISI_DAY, srYuudentaiFp),
                "".equals(filterkoukan1fpteisiTime) ? "0000" : filterkoukan1fpteisiTime));                                                          // ﾌｨﾙﾀｰ交換①_F/P停止日時
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B026Const.FILTERKOUKAN1_ITIJIFILTERHINMEI, srYuudentaiFp)));     // ﾌｨﾙﾀｰ交換①_1次ﾌｨﾙﾀｰ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B026Const.FILTERKOUKAN1_LOTNO1, srYuudentaiFp)));                        // ﾌｨﾙﾀｰ交換①_LotNo1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.FILTERKOUKAN1_TORITUKEHONSUU1, srYuudentaiFp)));               // ﾌｨﾙﾀｰ交換①_取り付け本数1
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B026Const.FILTERKOUKAN1_NIJIFILTERHINMEI, srYuudentaiFp)));      // ﾌｨﾙﾀｰ交換①_2次ﾌｨﾙﾀｰ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B026Const.FILTERKOUKAN1_LOTNO2, srYuudentaiFp)));                        // ﾌｨﾙﾀｰ交換①_LotNo2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.FILTERKOUKAN1_TORITUKEHONSUU2, srYuudentaiFp)));               // ﾌｨﾙﾀｰ交換①_取り付け本数2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B026Const.FILTERKOUKAN1_SANJIFILTERHINMEI, srYuudentaiFp)));     // ﾌｨﾙﾀｰ交換①_3次ﾌｨﾙﾀｰ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B026Const.FILTERKOUKAN1_LOTNO3, srYuudentaiFp)));                        // ﾌｨﾙﾀｰ交換①_LotNo3
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.FILTERKOUKAN1_TORITUKEHONSUU3, srYuudentaiFp)));               // ﾌｨﾙﾀｰ交換①_取り付け本数3
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B026Const.FILTERKOUKAN1_FPSAIKAI_DAY, srYuudentaiFp),
                "".equals(filterkoukan1fpsaikaiTime) ? "0000" : filterkoukan1fpsaikaiTime));                                                        // ﾌｨﾙﾀｰ交換①_F/P再開日時
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B026Const.FILTERKOUKAN1_TANTOUSYA, srYuudentaiFp)));                  // ﾌｨﾙﾀｰ交換①_担当者
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B026Const.FPSYUURYOU_DAY, srYuudentaiFp),
                "".equals(fpsyuuryouTime) ? "0000" : fpsyuuryouTime));                                                                              // F/P終了日時
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B026Const.FPTOTALJIKAN, srYuudentaiFp)));                             // F/Pﾄｰﾀﾙ時間
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.ITIJIFILTERSOUSIYOUHONSUU, srYuudentaiFp)));                   // 1次ﾌｨﾙﾀｰ総使用本数
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.NIJIFILTERSOUSIYOUHONSUU, srYuudentaiFp)));                    // 2次ﾌｨﾙﾀｰ総使用本数
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.SANJIFILTERSOUSIYOUHONSUU, srYuudentaiFp)));                   // 3次ﾌｨﾙﾀｰ総使用本数
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B026Const.FILTERPASSSYUURYOU_TANTOUSYA, srYuudentaiFp)));             // ﾌｨﾙﾀｰﾊﾟｽ終了_担当者
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.SOUJYUUROUSOKUTEI1, srYuudentaiFp)));                          // 総重量測定1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.SOUJYUUROUSOKUTEI2, srYuudentaiFp)));                          // 総重量測定2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.SOUJYUUROUSOKUTEI3, srYuudentaiFp)));                          // 総重量測定3
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.SOUJYUUROUSOKUTEI4, srYuudentaiFp)));                          // 総重量測定4
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.SOUJYUUROUSOKUTEI5, srYuudentaiFp)));                          // 総重量測定5
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.SOUJYUUROUSOKUTEI6, srYuudentaiFp)));                          // 総重量測定6
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.SOUJYUUROUSOKUTEI7, srYuudentaiFp)));                          // 総重量測定7
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.SOUJYUUROUSOKUTEI8, srYuudentaiFp)));                          // 総重量測定8
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.SOUJYUUROUSOKUTEI9, srYuudentaiFp)));                          // 総重量測定9
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.SOUJYUUROUSOKUTEI10, srYuudentaiFp)));                         // 総重量測定10
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.SOUJYUUROUSOKUTEI11, srYuudentaiFp)));                         // 総重量測定11
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.SOUJYUUROUSOKUTEI12, srYuudentaiFp)));                         // 総重量測定12
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU1, srYuudentaiFp)));                     // 誘電体ｽﾗﾘｰ重量1
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU2, srYuudentaiFp)));                     // 誘電体ｽﾗﾘｰ重量2
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU3, srYuudentaiFp)));                     // 誘電体ｽﾗﾘｰ重量3
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU4, srYuudentaiFp)));                     // 誘電体ｽﾗﾘｰ重量4
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU5, srYuudentaiFp)));                     // 誘電体ｽﾗﾘｰ重量5
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU6, srYuudentaiFp)));                     // 誘電体ｽﾗﾘｰ重量6
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU7, srYuudentaiFp)));                     // 誘電体ｽﾗﾘｰ重量7
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU8, srYuudentaiFp)));                     // 誘電体ｽﾗﾘｰ重量8
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU9, srYuudentaiFp)));                     // 誘電体ｽﾗﾘｰ重量9
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU10, srYuudentaiFp)));                    // 誘電体ｽﾗﾘｰ重量10
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU11, srYuudentaiFp)));                    // 誘電体ｽﾗﾘｰ重量11
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU12, srYuudentaiFp)));                    // 誘電体ｽﾗﾘｰ重量12
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.YUUDENTAISLURRYJYUUROUGOUKEI, srYuudentaiFp)));                // 誘電体ｽﾗﾘｰ重量合計
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B026Const.TOUNYUURYOU, srYuudentaiFp)));                            // 投入量
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B026Const.BUDOMARIKEISAN, srYuudentaiFp)));                       // 歩留まり計算
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B026Const.UUDENTAISLURRYYUUKOUKIGEN, srYuudentaiFp)));                // 誘電体ｽﾗﾘｰ有効期限
        params.add(getComboBoxDbValue(getItemData(pItemList, GXHDO102B026Const.FUNSAIHANTEI, srYuudentaiFp), 9));                                   // 粉砕判定
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B026Const.SEIHINJYUURYOUKAKUNIN_TANTOUSYA, srYuudentaiFp)));          // 製品重量確認_担当者
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B026Const.HOZONYOUSAMPLEKAISYU, srYuudentaiFp), 9));                           // 保存用ｻﾝﾌﾟﾙ回収
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B026Const.ZUNSEKIYOUSAMPLEKAISYU, srYuudentaiFp), 9));                         // 分析用ｻﾝﾌﾟﾙ回収
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B026Const.KANSOUZARA, srYuudentaiFp)));                          // 乾燥皿
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.ARUMIZARAFUUTAIJYUURYOU, srYuudentaiFp)));                     // ｱﾙﾐ皿風袋重量
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B026Const.SLURRYSAMPLEJYUURYOUKIKAKU, srYuudentaiFp)));          // ｽﾗﾘｰｻﾝﾌﾟﾙ重量規格
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B026Const.KANSOUMAESLURRYJYUURYOU, srYuudentaiFp)));                     // 乾燥前ｽﾗﾘｰ重量
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B026Const.KANSOUKI, srYuudentaiFp)));                            // 乾燥機
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B026Const.KANSOUONDOKIKAKU, srYuudentaiFp)));                    // 乾燥温度規格
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B026Const.KANSOUJIKANKIKAKU, srYuudentaiFp)));                   // 乾燥時間規格
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B026Const.KANSOUKAISI_DAY, srYuudentaiFp),
                "".equals(kansoukaisiTime) ? "0000" : kansoukaisiTime));                                                                            // 乾燥開始日時
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B026Const.KANSOUSYUURYOU_DAY, srYuudentaiFp),
                "".equals(kansousyuuryouTime) ? "0000" : kansousyuuryouTime));                                                                      // 乾燥終了日時
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B026Const.KANSOUJIKANTOTAL, srYuudentaiFp)));                         // 乾燥時間ﾄｰﾀﾙ
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B026Const.KANSOUGOSOUJYUURYOU, srYuudentaiFp)));                  // 乾燥後総重量
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B026Const.KANSOUGOSYOUMIJYUURYOU, srYuudentaiFp)));               // 乾燥後正味重量
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B026Const.KOKEIBUNHIRITU, srYuudentaiFp)));                       // 固形分比率
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B026Const.BIKOU1, srYuudentaiFp)));                                   // 備考1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B026Const.BIKOU2, srYuudentaiFp)));                                   // 備考2

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
     * 誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管(sr_yuudentai_fp)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSrYuudentaiFp(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sr_yuudentai_fp "
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
     * [誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_yuudentai_fp "
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
     * @param srYuudentaiFp 誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管データ
     * @return DB値
     */
    private String getSrYuudentaiFpItemData(String itemId, SrYuudentaiFp srYuudentaiFp) {
        switch (itemId) {
            // 誘電体ｽﾗﾘｰ品名
            case GXHDO102B026Const.YUUDENTAISLURRYHINMEI:
                return StringUtil.nullToBlank(srYuudentaiFp.getYuudentaislurryhinmei());

            // 誘電体ｽﾗﾘｰLotNo
            case GXHDO102B026Const.YUUDENTAISLURRYLOTNO:
                return StringUtil.nullToBlank(srYuudentaiFp.getYuudentaislurrylotno());

            // ﾛｯﾄ区分
            case GXHDO102B026Const.LOTKUBUN:
                return StringUtil.nullToBlank(srYuudentaiFp.getLotkubun());

            // 原料LotNo
            case GXHDO102B026Const.GENRYOULOTNO:
                return StringUtil.nullToBlank(srYuudentaiFp.getGenryoulotno());

            // 原料記号
            case GXHDO102B026Const.GENRYOUKIGOU:
                return StringUtil.nullToBlank(srYuudentaiFp.getGenryoukigou());

            // 風袋重量測定_ｽﾃﾝ容器1
            case GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI1:
                return StringUtil.nullToBlank(srYuudentaiFp.getFuutaijyuuryousokutei_sutenyouki1());

            // 風袋重量測定_ｽﾃﾝ容器2
            case GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI2:
                return StringUtil.nullToBlank(srYuudentaiFp.getFuutaijyuuryousokutei_sutenyouki2());

            // 風袋重量測定_ｽﾃﾝ容器3
            case GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI3:
                return StringUtil.nullToBlank(srYuudentaiFp.getFuutaijyuuryousokutei_sutenyouki3());

            // 風袋重量測定_ｽﾃﾝ容器4
            case GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI4:
                return StringUtil.nullToBlank(srYuudentaiFp.getFuutaijyuuryousokutei_sutenyouki4());

            // 風袋重量測定_ｽﾃﾝ容器5
            case GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI5:
                return StringUtil.nullToBlank(srYuudentaiFp.getFuutaijyuuryousokutei_sutenyouki5());

            // 風袋重量測定_ｽﾃﾝ容器6
            case GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI6:
                return StringUtil.nullToBlank(srYuudentaiFp.getFuutaijyuuryousokutei_sutenyouki6());

            // 風袋重量測定_白ﾎﾟﾘ容器1
            case GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI1:
                return StringUtil.nullToBlank(srYuudentaiFp.getFuutaijyuuryousokutei_siropori1());

            // 風袋重量測定_白ﾎﾟﾘ容器2
            case GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI2:
                return StringUtil.nullToBlank(srYuudentaiFp.getFuutaijyuuryousokutei_siropori2());

            // 風袋重量測定_白ﾎﾟﾘ容器3
            case GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI3:
                return StringUtil.nullToBlank(srYuudentaiFp.getFuutaijyuuryousokutei_siropori3());

            // 風袋重量測定_白ﾎﾟﾘ容器4
            case GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI4:
                return StringUtil.nullToBlank(srYuudentaiFp.getFuutaijyuuryousokutei_siropori4());

            // 風袋重量測定_白ﾎﾟﾘ容器5
            case GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI5:
                return StringUtil.nullToBlank(srYuudentaiFp.getFuutaijyuuryousokutei_siropori5());

            // 風袋重量測定_白ﾎﾟﾘ容器6
            case GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI6:
                return StringUtil.nullToBlank(srYuudentaiFp.getFuutaijyuuryousokutei_siropori6());

            // 風袋重量測定_白ﾎﾟﾘ容器7
            case GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI7:
                return StringUtil.nullToBlank(srYuudentaiFp.getFuutaijyuuryousokutei_siropori7());

            // 風袋重量測定_白ﾎﾟﾘ容器8
            case GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI8:
                return StringUtil.nullToBlank(srYuudentaiFp.getFuutaijyuuryousokutei_siropori8());

            // 風袋重量測定_白ﾎﾟﾘ容器9
            case GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI9:
                return StringUtil.nullToBlank(srYuudentaiFp.getFuutaijyuuryousokutei_siropori9());

            // 風袋重量測定_白ﾎﾟﾘ容器10
            case GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI10:
                return StringUtil.nullToBlank(srYuudentaiFp.getFuutaijyuuryousokutei_siropori10());

            // 風袋重量測定_白ﾎﾟﾘ容器11
            case GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI11:
                return StringUtil.nullToBlank(srYuudentaiFp.getFuutaijyuuryousokutei_siropori11());

            // 風袋重量測定_白ﾎﾟﾘ容器12
            case GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI12:
                return StringUtil.nullToBlank(srYuudentaiFp.getFuutaijyuuryousokutei_siropori12());

            // 保管容器準備_担当者
            case GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_TANTOUSYA:
                return StringUtil.nullToBlank(srYuudentaiFp.getFuutaijyuuryousokutei_tantousya());

            // ﾌｨﾙﾀｰ連結
            case GXHDO102B026Const.FILTERRENKETU:
                return StringUtil.nullToBlank(srYuudentaiFp.getFilterrenketu());

            // ﾌｨﾙﾀｰ取り付け_1次ﾌｨﾙﾀｰ品名
            case GXHDO102B026Const.FILTERTORITUKE_ITIJIFILTERHINMEI:
                return StringUtil.nullToBlank(srYuudentaiFp.getFiltertorituke_itijifilterhinmei());

            // ﾌｨﾙﾀｰ取り付け_LotNo1
            case GXHDO102B026Const.FILTERTORITUKE_LOTNO1:
                return StringUtil.nullToBlank(srYuudentaiFp.getFiltertorituke_lotno1());

            // ﾌｨﾙﾀｰ取り付け_取り付け本数1
            case GXHDO102B026Const.FILTERTORITUKE_TORITUKEHONSUU1:
                return StringUtil.nullToBlank(srYuudentaiFp.getFiltertorituke_toritukehonsuu1());

            // ﾌｨﾙﾀｰ取り付け_2次ﾌｨﾙﾀｰ品名
            case GXHDO102B026Const.FILTERTORITUKE_NIJIFILTERHINMEI:
                return StringUtil.nullToBlank(srYuudentaiFp.getFiltertorituke_nijifilterhinmei());

            // ﾌｨﾙﾀｰ取り付け_LotNo2
            case GXHDO102B026Const.FILTERTORITUKE_LOTNO2:
                return StringUtil.nullToBlank(srYuudentaiFp.getFiltertorituke_lotno2());

            // ﾌｨﾙﾀｰ取り付け_取り付け本数2
            case GXHDO102B026Const.FILTERTORITUKE_TORITUKEHONSUU2:
                return StringUtil.nullToBlank(srYuudentaiFp.getFiltertorituke_toritukehonsuu2());

            // ﾌｨﾙﾀｰ取り付け_3次ﾌｨﾙﾀｰ品名
            case GXHDO102B026Const.FILTERTORITUKE_SANJIFILTERHINMEI:
                return StringUtil.nullToBlank(srYuudentaiFp.getFiltertorituke_sanjifilterhinmei());

            // ﾌｨﾙﾀｰ取り付け_LotNo3
            case GXHDO102B026Const.FILTERTORITUKE_LOTNO3:
                return StringUtil.nullToBlank(srYuudentaiFp.getFiltertorituke_lotno3());

            // ﾌｨﾙﾀｰ取り付け_取り付け本数3
            case GXHDO102B026Const.FILTERTORITUKE_TORITUKEHONSUU3:
                return StringUtil.nullToBlank(srYuudentaiFp.getFiltertorituke_toritukehonsuu3());

            // ﾌｨﾙﾀｰ取り付け_担当者
            case GXHDO102B026Const.FILTERTORITUKE_TANTOUSYA:
                return StringUtil.nullToBlank(srYuudentaiFp.getFiltertorituke_tantousya());

            // 排出容器の内袋
            case GXHDO102B026Const.HAISYUTUYOUKINOUTIBUKURO:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srYuudentaiFp.getHaisyutuyoukinoutibukuro()));

            // ﾌｨﾙﾀｰ使用回数
            case GXHDO102B026Const.FILTERSIYOUKAISUU:
                return StringUtil.nullToBlank(srYuudentaiFp.getFiltersiyoukaisuu());

            // F/PﾀﾝｸNo
            case GXHDO102B026Const.FPTANKNO:
                return StringUtil.nullToBlank(srYuudentaiFp.getFptankno());

            // 洗浄確認
            case GXHDO102B026Const.SENJYOUKAKUNIN:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srYuudentaiFp.getSenjyoukakunin()));

            // F/P開始日
            case GXHDO102B026Const.FPKAISI_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiFp.getFpkaisinichiji(), "yyMMdd");

            // F/P開始時間
            case GXHDO102B026Const.FPKAISI_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiFp.getFpkaisinichiji(), "HHmm");

            // 圧送ﾚｷﾞｭﾚｰﾀｰNo
            case GXHDO102B026Const.ASSOUREGULATORNO:
                return StringUtil.nullToBlank(srYuudentaiFp.getAssouregulatorNo());

            // 圧送圧力
            case GXHDO102B026Const.ASSOUATURYOKU:
                return StringUtil.nullToBlank(srYuudentaiFp.getAssouaturyoku());

            // ﾌｨﾙﾀｰﾊﾟｽ開始_担当者
            case GXHDO102B026Const.FILTERPASSKAISI_TANTOUSYA:
                return StringUtil.nullToBlank(srYuudentaiFp.getFilterpasskaisi_tantousya());

            // ﾌｨﾙﾀｰ交換①_F/P停止日
            case GXHDO102B026Const.FILTERKOUKAN1_FPTEISI_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiFp.getFilterkoukan1_Fpteisinichiji(), "yyMMdd");

            // ﾌｨﾙﾀｰ交換①_F/P停止時間
            case GXHDO102B026Const.FILTERKOUKAN1_FPTEISI_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiFp.getFilterkoukan1_Fpteisinichiji(), "HHmm");

            // ﾌｨﾙﾀｰ交換①_1次ﾌｨﾙﾀｰ品名
            case GXHDO102B026Const.FILTERKOUKAN1_ITIJIFILTERHINMEI:
                return StringUtil.nullToBlank(srYuudentaiFp.getFilterkoukan1_itijifilterhinmei());

            // ﾌｨﾙﾀｰ交換①_LotNo1
            case GXHDO102B026Const.FILTERKOUKAN1_LOTNO1:
                return StringUtil.nullToBlank(srYuudentaiFp.getFilterkoukan1_lotno1());

            // ﾌｨﾙﾀｰ交換①_取り付け本数1
            case GXHDO102B026Const.FILTERKOUKAN1_TORITUKEHONSUU1:
                return StringUtil.nullToBlank(srYuudentaiFp.getFilterkoukan1_toritukehonsuu1());

            // ﾌｨﾙﾀｰ交換①_2次ﾌｨﾙﾀｰ品名
            case GXHDO102B026Const.FILTERKOUKAN1_NIJIFILTERHINMEI:
                return StringUtil.nullToBlank(srYuudentaiFp.getFilterkoukan1_nijifilterhinmei());

            // ﾌｨﾙﾀｰ交換①_LotNo2
            case GXHDO102B026Const.FILTERKOUKAN1_LOTNO2:
                return StringUtil.nullToBlank(srYuudentaiFp.getFilterkoukan1_lotno2());

            // ﾌｨﾙﾀｰ交換①_取り付け本数2
            case GXHDO102B026Const.FILTERKOUKAN1_TORITUKEHONSUU2:
                return StringUtil.nullToBlank(srYuudentaiFp.getFilterkoukan1_toritukehonsuu2());

            // ﾌｨﾙﾀｰ交換①_3次ﾌｨﾙﾀｰ品名
            case GXHDO102B026Const.FILTERKOUKAN1_SANJIFILTERHINMEI:
                return StringUtil.nullToBlank(srYuudentaiFp.getFilterkoukan1_sanjifilterhinmei());

            // ﾌｨﾙﾀｰ交換①_LotNo3
            case GXHDO102B026Const.FILTERKOUKAN1_LOTNO3:
                return StringUtil.nullToBlank(srYuudentaiFp.getFilterkoukan1_lotno3());

            // ﾌｨﾙﾀｰ交換①_取り付け本数3
            case GXHDO102B026Const.FILTERKOUKAN1_TORITUKEHONSUU3:
                return StringUtil.nullToBlank(srYuudentaiFp.getFilterkoukan1_toritukehonsuu3());

            // ﾌｨﾙﾀｰ交換①_F/P再開日
            case GXHDO102B026Const.FILTERKOUKAN1_FPSAIKAI_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiFp.getFilterkoukan1_Fpsaikainichiji(), "yyMMdd");

            // ﾌｨﾙﾀｰ交換①_F/P再開時間
            case GXHDO102B026Const.FILTERKOUKAN1_FPSAIKAI_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiFp.getFilterkoukan1_Fpsaikainichiji(), "HHmm");

            // ﾌｨﾙﾀｰ交換①_担当者
            case GXHDO102B026Const.FILTERKOUKAN1_TANTOUSYA:
                return StringUtil.nullToBlank(srYuudentaiFp.getFilterkoukan1_tantousya());

            // F/P終了日
            case GXHDO102B026Const.FPSYUURYOU_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiFp.getFPsyuuryounichiji(), "yyMMdd");

            // F/P終了時間
            case GXHDO102B026Const.FPSYUURYOU_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiFp.getFPsyuuryounichiji(), "HHmm");

            // F/Pﾄｰﾀﾙ時間
            case GXHDO102B026Const.FPTOTALJIKAN:
                return StringUtil.nullToBlank(srYuudentaiFp.getFPtotaljikan());

            // 1次ﾌｨﾙﾀｰ総使用本数
            case GXHDO102B026Const.ITIJIFILTERSOUSIYOUHONSUU:
                return StringUtil.nullToBlank(srYuudentaiFp.getItijifiltersousiyouhonsuu());

            // 2次ﾌｨﾙﾀｰ総使用本数
            case GXHDO102B026Const.NIJIFILTERSOUSIYOUHONSUU:
                return StringUtil.nullToBlank(srYuudentaiFp.getNijifiltersousiyouhonsuu());

            // 3次ﾌｨﾙﾀｰ総使用本数
            case GXHDO102B026Const.SANJIFILTERSOUSIYOUHONSUU:
                return StringUtil.nullToBlank(srYuudentaiFp.getSanjifiltersousiyouhonsuu());

            // ﾌｨﾙﾀｰﾊﾟｽ終了_担当者
            case GXHDO102B026Const.FILTERPASSSYUURYOU_TANTOUSYA:
                return StringUtil.nullToBlank(srYuudentaiFp.getFilterpasssyuuryou_tantousya());

            // 総重量測定1
            case GXHDO102B026Const.SOUJYUUROUSOKUTEI1:
                return StringUtil.nullToBlank(srYuudentaiFp.getSoujyuurousokutei1());

            // 総重量測定2
            case GXHDO102B026Const.SOUJYUUROUSOKUTEI2:
                return StringUtil.nullToBlank(srYuudentaiFp.getSoujyuurousokutei2());

            // 総重量測定3
            case GXHDO102B026Const.SOUJYUUROUSOKUTEI3:
                return StringUtil.nullToBlank(srYuudentaiFp.getSoujyuurousokutei3());

            // 総重量測定4
            case GXHDO102B026Const.SOUJYUUROUSOKUTEI4:
                return StringUtil.nullToBlank(srYuudentaiFp.getSoujyuurousokutei4());

            // 総重量測定5
            case GXHDO102B026Const.SOUJYUUROUSOKUTEI5:
                return StringUtil.nullToBlank(srYuudentaiFp.getSoujyuurousokutei5());

            // 総重量測定6
            case GXHDO102B026Const.SOUJYUUROUSOKUTEI6:
                return StringUtil.nullToBlank(srYuudentaiFp.getSoujyuurousokutei6());

            // 総重量測定7
            case GXHDO102B026Const.SOUJYUUROUSOKUTEI7:
                return StringUtil.nullToBlank(srYuudentaiFp.getSoujyuurousokutei7());

            // 総重量測定8
            case GXHDO102B026Const.SOUJYUUROUSOKUTEI8:
                return StringUtil.nullToBlank(srYuudentaiFp.getSoujyuurousokutei8());

            // 総重量測定9
            case GXHDO102B026Const.SOUJYUUROUSOKUTEI9:
                return StringUtil.nullToBlank(srYuudentaiFp.getSoujyuurousokutei9());

            // 総重量測定10
            case GXHDO102B026Const.SOUJYUUROUSOKUTEI10:
                return StringUtil.nullToBlank(srYuudentaiFp.getSoujyuurousokutei10());

            // 総重量測定11
            case GXHDO102B026Const.SOUJYUUROUSOKUTEI11:
                return StringUtil.nullToBlank(srYuudentaiFp.getSoujyuurousokutei11());

            // 総重量測定12
            case GXHDO102B026Const.SOUJYUUROUSOKUTEI12:
                return StringUtil.nullToBlank(srYuudentaiFp.getSoujyuurousokutei12());

            // 誘電体ｽﾗﾘｰ重量1
            case GXHDO102B026Const.YUUDENTAISLURRYJYUUROU1:
                return StringUtil.nullToBlank(srYuudentaiFp.getYuudentaislurryjyuurou1());

            // 誘電体ｽﾗﾘｰ重量2
            case GXHDO102B026Const.YUUDENTAISLURRYJYUUROU2:
                return StringUtil.nullToBlank(srYuudentaiFp.getYuudentaislurryjyuurou2());

            // 誘電体ｽﾗﾘｰ重量3
            case GXHDO102B026Const.YUUDENTAISLURRYJYUUROU3:
                return StringUtil.nullToBlank(srYuudentaiFp.getYuudentaislurryjyuurou3());

            // 誘電体ｽﾗﾘｰ重量4
            case GXHDO102B026Const.YUUDENTAISLURRYJYUUROU4:
                return StringUtil.nullToBlank(srYuudentaiFp.getYuudentaislurryjyuurou4());

            // 誘電体ｽﾗﾘｰ重量5
            case GXHDO102B026Const.YUUDENTAISLURRYJYUUROU5:
                return StringUtil.nullToBlank(srYuudentaiFp.getYuudentaislurryjyuurou5());

            // 誘電体ｽﾗﾘｰ重量6
            case GXHDO102B026Const.YUUDENTAISLURRYJYUUROU6:
                return StringUtil.nullToBlank(srYuudentaiFp.getYuudentaislurryjyuurou6());

            // 誘電体ｽﾗﾘｰ重量7
            case GXHDO102B026Const.YUUDENTAISLURRYJYUUROU7:
                return StringUtil.nullToBlank(srYuudentaiFp.getYuudentaislurryjyuurou7());

            // 誘電体ｽﾗﾘｰ重量8
            case GXHDO102B026Const.YUUDENTAISLURRYJYUUROU8:
                return StringUtil.nullToBlank(srYuudentaiFp.getYuudentaislurryjyuurou8());

            // 誘電体ｽﾗﾘｰ重量9
            case GXHDO102B026Const.YUUDENTAISLURRYJYUUROU9:
                return StringUtil.nullToBlank(srYuudentaiFp.getYuudentaislurryjyuurou9());

            // 誘電体ｽﾗﾘｰ重量10
            case GXHDO102B026Const.YUUDENTAISLURRYJYUUROU10:
                return StringUtil.nullToBlank(srYuudentaiFp.getYuudentaislurryjyuurou10());

            // 誘電体ｽﾗﾘｰ重量11
            case GXHDO102B026Const.YUUDENTAISLURRYJYUUROU11:
                return StringUtil.nullToBlank(srYuudentaiFp.getYuudentaislurryjyuurou11());

            // 誘電体ｽﾗﾘｰ重量12
            case GXHDO102B026Const.YUUDENTAISLURRYJYUUROU12:
                return StringUtil.nullToBlank(srYuudentaiFp.getYuudentaislurryjyuurou12());

            // 誘電体ｽﾗﾘｰ重量合計
            case GXHDO102B026Const.YUUDENTAISLURRYJYUUROUGOUKEI:
                return StringUtil.nullToBlank(srYuudentaiFp.getYuudentaislurryjyuurougoukei());

            // 投入量
            case GXHDO102B026Const.TOUNYUURYOU:
                return StringUtil.nullToBlank(srYuudentaiFp.getTounyuuryou());

            // 歩留まり計算
            case GXHDO102B026Const.BUDOMARIKEISAN:
                return StringUtil.nullToBlank(srYuudentaiFp.getBudomarikeisan());

            // 誘電体ｽﾗﾘｰ有効期限
            case GXHDO102B026Const.UUDENTAISLURRYYUUKOUKIGEN:
                return StringUtil.nullToBlank(srYuudentaiFp.getUudentaislurryyuukoukigen());

            // 粉砕判定
            case GXHDO102B026Const.FUNSAIHANTEI:
                return getComboBoxCheckValue(StringUtil.nullToBlank(srYuudentaiFp.getFunsaihantei()));

            // 製品重量確認_担当者
            case GXHDO102B026Const.SEIHINJYUURYOUKAKUNIN_TANTOUSYA:
                return StringUtil.nullToBlank(srYuudentaiFp.getSeihinjyuuryoukakunin_tantousya());

            // 保存用ｻﾝﾌﾟﾙ回収
            case GXHDO102B026Const.HOZONYOUSAMPLEKAISYU:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srYuudentaiFp.getHozonyousamplekaisyu()));

            // 分析用ｻﾝﾌﾟﾙ回収
            case GXHDO102B026Const.ZUNSEKIYOUSAMPLEKAISYU:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srYuudentaiFp.getZunsekiyousamplekaisyu()));

            // 乾燥皿
            case GXHDO102B026Const.KANSOUZARA:
                return StringUtil.nullToBlank(srYuudentaiFp.getKansouzara());

            // ｱﾙﾐ皿風袋重量
            case GXHDO102B026Const.ARUMIZARAFUUTAIJYUURYOU:
                return StringUtil.nullToBlank(srYuudentaiFp.getArumizarafuutaijyuuryou());

            // ｽﾗﾘｰｻﾝﾌﾟﾙ重量規格
            case GXHDO102B026Const.SLURRYSAMPLEJYUURYOUKIKAKU:
                return StringUtil.nullToBlank(srYuudentaiFp.getSlurrysamplejyuuryoukikaku());

            // 乾燥前ｽﾗﾘｰ重量
            case GXHDO102B026Const.KANSOUMAESLURRYJYUURYOU:
                return StringUtil.nullToBlank(srYuudentaiFp.getKansoumaeslurryjyuuryou());

            // 乾燥機
            case GXHDO102B026Const.KANSOUKI:
                return StringUtil.nullToBlank(srYuudentaiFp.getKansouki());

            // 乾燥温度規格
            case GXHDO102B026Const.KANSOUONDOKIKAKU:
                return StringUtil.nullToBlank(srYuudentaiFp.getKansouondokikaku());

            // 乾燥時間規格
            case GXHDO102B026Const.KANSOUJIKANKIKAKU:
                return StringUtil.nullToBlank(srYuudentaiFp.getKansoujikankikaku());

            // 乾燥開始日
            case GXHDO102B026Const.KANSOUKAISI_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiFp.getKansoukaisinichiji(), "yyMMdd");

            // 乾燥開始時間
            case GXHDO102B026Const.KANSOUKAISI_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiFp.getKansoukaisinichiji(), "HHmm");

            // 乾燥終了日
            case GXHDO102B026Const.KANSOUSYUURYOU_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiFp.getKansousyuuryounichiji(), "yyMMdd");

            // 乾燥終了時間
            case GXHDO102B026Const.KANSOUSYUURYOU_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiFp.getKansousyuuryounichiji(), "HHmm");

            // 乾燥時間ﾄｰﾀﾙ
            case GXHDO102B026Const.KANSOUJIKANTOTAL:
                return StringUtil.nullToBlank(srYuudentaiFp.getKansoujikantotal());

            // 乾燥後総重量
            case GXHDO102B026Const.KANSOUGOSOUJYUURYOU:
                return StringUtil.nullToBlank(srYuudentaiFp.getKansougosoujyuuryou());

            // 乾燥後正味重量
            case GXHDO102B026Const.KANSOUGOSYOUMIJYUURYOU:
                return StringUtil.nullToBlank(srYuudentaiFp.getKansougosyoumijyuuryou());

            // 固形分比率
            case GXHDO102B026Const.KOKEIBUNHIRITU:
                return StringUtil.nullToBlank(srYuudentaiFp.getKokeibunhiritu());

            // 備考1
            case GXHDO102B026Const.BIKOU1:
                return StringUtil.nullToBlank(srYuudentaiFp.getBikou1());

            // 備考2
            case GXHDO102B026Const.BIKOU2:
                return StringUtil.nullToBlank(srYuudentaiFp.getBikou2());

            default:
                return null;
        }
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管_仮登録(tmp_sr_yuudentai_fp)登録処理(削除時)
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
    private void insertDeleteDataTmpSrYuudentaiFp(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_yuudentai_fp ("
                + " kojyo,lotno,edaban,yuudentaislurryhinmei,yuudentaislurrylotno,lotkubun,genryoulotno,genryoukigou,"
                + "fuutaijyuuryousokutei_sutenyouki1,fuutaijyuuryousokutei_sutenyouki2,fuutaijyuuryousokutei_sutenyouki3,"
                + "fuutaijyuuryousokutei_sutenyouki4,fuutaijyuuryousokutei_sutenyouki5,fuutaijyuuryousokutei_sutenyouki6,"
                + "fuutaijyuuryousokutei_siropori1,fuutaijyuuryousokutei_siropori2,fuutaijyuuryousokutei_siropori3,fuutaijyuuryousokutei_siropori4,"
                + "fuutaijyuuryousokutei_siropori5,fuutaijyuuryousokutei_siropori6,fuutaijyuuryousokutei_siropori7,fuutaijyuuryousokutei_siropori8,"
                + "fuutaijyuuryousokutei_siropori9,fuutaijyuuryousokutei_siropori10,fuutaijyuuryousokutei_siropori11,fuutaijyuuryousokutei_siropori12,"
                + "fuutaijyuuryousokutei_tantousya,filterrenketu,filtertorituke_itijifilterhinmei,filtertorituke_lotno1,filtertorituke_toritukehonsuu1,"
                + "filtertorituke_nijifilterhinmei,filtertorituke_lotno2,filtertorituke_toritukehonsuu2,filtertorituke_sanjifilterhinmei,"
                + "filtertorituke_lotno3,filtertorituke_toritukehonsuu3,filtertorituke_tantousya,haisyutuyoukinoutibukuro,filtersiyoukaisuu,"
                + "Fptankno,senjyoukakunin,Fpkaisinichiji,assouregulatorNo,assouaturyoku,filterpasskaisi_tantousya,filterkoukan1_Fpteisinichiji,"
                + "filterkoukan1_itijifilterhinmei,filterkoukan1_lotno1,filterkoukan1_toritukehonsuu1,filterkoukan1_nijifilterhinmei,filterkoukan1_lotno2,"
                + "filterkoukan1_toritukehonsuu2,filterkoukan1_sanjifilterhinmei,filterkoukan1_lotno3,filterkoukan1_toritukehonsuu3,"
                + "filterkoukan1_Fpsaikainichiji,filterkoukan1_tantousya,FPsyuuryounichiji,FPtotaljikan,itijifiltersousiyouhonsuu,"
                + "nijifiltersousiyouhonsuu,sanjifiltersousiyouhonsuu,filterpasssyuuryou_tantousya,soujyuurousokutei1,soujyuurousokutei2,"
                + "soujyuurousokutei3,soujyuurousokutei4,soujyuurousokutei5,soujyuurousokutei6,soujyuurousokutei7,soujyuurousokutei8,"
                + "soujyuurousokutei9,soujyuurousokutei10,soujyuurousokutei11,soujyuurousokutei12,yuudentaislurryjyuurou1,yuudentaislurryjyuurou2,"
                + "yuudentaislurryjyuurou3,yuudentaislurryjyuurou4,yuudentaislurryjyuurou5,yuudentaislurryjyuurou6,yuudentaislurryjyuurou7,"
                + "yuudentaislurryjyuurou8,yuudentaislurryjyuurou9,yuudentaislurryjyuurou10,yuudentaislurryjyuurou11,yuudentaislurryjyuurou12,"
                + "yuudentaislurryjyuurougoukei,tounyuuryou,budomarikeisan,uudentaislurryyuukoukigen,funsaihantei,seihinjyuuryoukakunin_tantousya,"
                + "hozonyousamplekaisyu,zunsekiyousamplekaisyu,kansouzara,arumizarafuutaijyuuryou,slurrysamplejyuuryoukikaku,kansoumaeslurryjyuuryou,"
                + "kansouki,kansouondokikaku,kansoujikankikaku,kansoukaisinichiji,kansousyuuryounichiji,kansoujikantotal,kansougosoujyuuryou,"
                + "kansougosyoumijyuuryou,kokeibunhiritu,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") SELECT "
                + " kojyo,lotno,edaban,yuudentaislurryhinmei,yuudentaislurrylotno,lotkubun,genryoulotno,genryoukigou,"
                + "fuutaijyuuryousokutei_sutenyouki1,fuutaijyuuryousokutei_sutenyouki2,fuutaijyuuryousokutei_sutenyouki3,"
                + "fuutaijyuuryousokutei_sutenyouki4,fuutaijyuuryousokutei_sutenyouki5,fuutaijyuuryousokutei_sutenyouki6,"
                + "fuutaijyuuryousokutei_siropori1,fuutaijyuuryousokutei_siropori2,fuutaijyuuryousokutei_siropori3,fuutaijyuuryousokutei_siropori4,"
                + "fuutaijyuuryousokutei_siropori5,fuutaijyuuryousokutei_siropori6,fuutaijyuuryousokutei_siropori7,fuutaijyuuryousokutei_siropori8,"
                + "fuutaijyuuryousokutei_siropori9,fuutaijyuuryousokutei_siropori10,fuutaijyuuryousokutei_siropori11,fuutaijyuuryousokutei_siropori12,"
                + "fuutaijyuuryousokutei_tantousya,filterrenketu,filtertorituke_itijifilterhinmei,filtertorituke_lotno1,filtertorituke_toritukehonsuu1,"
                + "filtertorituke_nijifilterhinmei,filtertorituke_lotno2,filtertorituke_toritukehonsuu2,filtertorituke_sanjifilterhinmei,"
                + "filtertorituke_lotno3,filtertorituke_toritukehonsuu3,filtertorituke_tantousya,haisyutuyoukinoutibukuro,filtersiyoukaisuu,"
                + "Fptankno,senjyoukakunin,Fpkaisinichiji,assouregulatorNo,assouaturyoku,filterpasskaisi_tantousya,filterkoukan1_Fpteisinichiji,"
                + "filterkoukan1_itijifilterhinmei,filterkoukan1_lotno1,filterkoukan1_toritukehonsuu1,filterkoukan1_nijifilterhinmei,filterkoukan1_lotno2,"
                + "filterkoukan1_toritukehonsuu2,filterkoukan1_sanjifilterhinmei,filterkoukan1_lotno3,filterkoukan1_toritukehonsuu3,"
                + "filterkoukan1_Fpsaikainichiji,filterkoukan1_tantousya,FPsyuuryounichiji,FPtotaljikan,itijifiltersousiyouhonsuu,"
                + "nijifiltersousiyouhonsuu,sanjifiltersousiyouhonsuu,filterpasssyuuryou_tantousya,soujyuurousokutei1,soujyuurousokutei2,"
                + "soujyuurousokutei3,soujyuurousokutei4,soujyuurousokutei5,soujyuurousokutei6,soujyuurousokutei7,soujyuurousokutei8,"
                + "soujyuurousokutei9,soujyuurousokutei10,soujyuurousokutei11,soujyuurousokutei12,yuudentaislurryjyuurou1,yuudentaislurryjyuurou2,"
                + "yuudentaislurryjyuurou3,yuudentaislurryjyuurou4,yuudentaislurryjyuurou5,yuudentaislurryjyuurou6,yuudentaislurryjyuurou7,"
                + "yuudentaislurryjyuurou8,yuudentaislurryjyuurou9,yuudentaislurryjyuurou10,yuudentaislurryjyuurou11,yuudentaislurryjyuurou12,"
                + "yuudentaislurryjyuurougoukei,tounyuuryou,budomarikeisan,uudentaislurryyuukoukigen,funsaihantei,seihinjyuuryoukakunin_tantousya,"
                + "hozonyousamplekaisyu,zunsekiyousamplekaisyu,kansouzara,arumizarafuutaijyuuryou,slurrysamplejyuuryoukikaku,kansoumaeslurryjyuuryou,"
                + "kansouki,kansouondokikaku,kansoujikankikaku,kansoukaisinichiji,kansousyuuryounichiji,kansoujikantotal,kansougosoujyuuryou,"
                + "kansougosyoumijyuuryou,kokeibunhiritu,bikou1,bikou2,?,?,?,? "
                + " FROM sr_yuudentai_fp "
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
    private void initGXHDO102B026A(ProcessData processData) {
        GXHDO102B026A bean = (GXHDO102B026A) getFormBean("gXHDO102B026A");
        bean.setWiplotno(getItemRow(processData.getItemList(), GXHDO102B026Const.WIPLOTNO));
        bean.setYuudentaislurryhinmei(getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYHINMEI));
        bean.setYuudentaislurrylotno(getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYLOTNO));
        bean.setLotkubun(getItemRow(processData.getItemList(), GXHDO102B026Const.LOTKUBUN));
        bean.setGenryoulotno(getItemRow(processData.getItemList(), GXHDO102B026Const.GENRYOULOTNO));
        bean.setGenryoukigou(getItemRow(processData.getItemList(), GXHDO102B026Const.GENRYOUKIGOU));
        bean.setFuutaijyuuryousokutei_sutenyouki1(getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI1));
        bean.setFuutaijyuuryousokutei_sutenyouki2(getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI2));
        bean.setFuutaijyuuryousokutei_sutenyouki3(getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI3));
        bean.setFuutaijyuuryousokutei_sutenyouki4(getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI4));
        bean.setFuutaijyuuryousokutei_sutenyouki5(getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI5));
        bean.setFuutaijyuuryousokutei_sutenyouki6(getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI6));
        bean.setFuutaijyuuryousokutei_siropori1(getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI1));
        bean.setFuutaijyuuryousokutei_siropori2(getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI2));
        bean.setFuutaijyuuryousokutei_siropori3(getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI3));
        bean.setFuutaijyuuryousokutei_siropori4(getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI4));
        bean.setFuutaijyuuryousokutei_siropori5(getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI5));
        bean.setFuutaijyuuryousokutei_siropori6(getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI6));
        bean.setFuutaijyuuryousokutei_siropori7(getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI7));
        bean.setFuutaijyuuryousokutei_siropori8(getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI8));
        bean.setFuutaijyuuryousokutei_siropori9(getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI9));
        bean.setFuutaijyuuryousokutei_siropori10(getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI10));
        bean.setFuutaijyuuryousokutei_siropori11(getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI11));
        bean.setFuutaijyuuryousokutei_siropori12(getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI12));
        bean.setFuutaijyuuryousokutei_tantousya(getItemRow(processData.getItemList(), GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_TANTOUSYA));
        bean.setFilterrenketu(getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERRENKETU));
        bean.setFiltertorituke_itijifilterhinmei(getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERTORITUKE_ITIJIFILTERHINMEI));
        bean.setFiltertorituke_lotno1(getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERTORITUKE_LOTNO1));
        bean.setFiltertorituke_toritukehonsuu1(getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERTORITUKE_TORITUKEHONSUU1));
        bean.setFiltertorituke_nijifilterhinmei(getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERTORITUKE_NIJIFILTERHINMEI));
        bean.setFiltertorituke_lotno2(getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERTORITUKE_LOTNO2));
        bean.setFiltertorituke_toritukehonsuu2(getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERTORITUKE_TORITUKEHONSUU2));
        bean.setFiltertorituke_sanjifilterhinmei(getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERTORITUKE_SANJIFILTERHINMEI));
        bean.setFiltertorituke_lotno3(getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERTORITUKE_LOTNO3));
        bean.setFiltertorituke_toritukehonsuu3(getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERTORITUKE_TORITUKEHONSUU3));
        bean.setFiltertorituke_tantousya(getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERTORITUKE_TANTOUSYA));
        bean.setHaisyutuyoukinoutibukuro(getItemRow(processData.getItemList(), GXHDO102B026Const.HAISYUTUYOUKINOUTIBUKURO));
        bean.setFiltersiyoukaisuu(getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERSIYOUKAISUU));
        bean.setFptankno(getItemRow(processData.getItemList(), GXHDO102B026Const.FPTANKNO));
        bean.setSenjyoukakunin(getItemRow(processData.getItemList(), GXHDO102B026Const.SENJYOUKAKUNIN));
        bean.setFpkaisi_day(getItemRow(processData.getItemList(), GXHDO102B026Const.FPKAISI_DAY));
        bean.setFpkaisi_time(getItemRow(processData.getItemList(), GXHDO102B026Const.FPKAISI_TIME));
        bean.setAssouregulatorno(getItemRow(processData.getItemList(), GXHDO102B026Const.ASSOUREGULATORNO));
        bean.setAssouaturyoku(getItemRow(processData.getItemList(), GXHDO102B026Const.ASSOUATURYOKU));
        bean.setFilterpasskaisi_tantousya(getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERPASSKAISI_TANTOUSYA));
        bean.setFilterkoukan1_fpteisi_day(getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERKOUKAN1_FPTEISI_DAY));
        bean.setFilterkoukan1_fpteisi_time(getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERKOUKAN1_FPTEISI_TIME));
        bean.setFilterkoukan1_itijifilterhinmei(getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERKOUKAN1_ITIJIFILTERHINMEI));
        bean.setFilterkoukan1_lotno1(getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERKOUKAN1_LOTNO1));
        bean.setFilterkoukan1_toritukehonsuu1(getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERKOUKAN1_TORITUKEHONSUU1));
        bean.setFilterkoukan1_nijifilterhinmei(getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERKOUKAN1_NIJIFILTERHINMEI));
        bean.setFilterkoukan1_lotno2(getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERKOUKAN1_LOTNO2));
        bean.setFilterkoukan1_toritukehonsuu2(getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERKOUKAN1_TORITUKEHONSUU2));
        bean.setFilterkoukan1_sanjifilterhinmei(getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERKOUKAN1_SANJIFILTERHINMEI));
        bean.setFilterkoukan1_lotno3(getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERKOUKAN1_LOTNO3));
        bean.setFilterkoukan1_toritukehonsuu3(getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERKOUKAN1_TORITUKEHONSUU3));
        bean.setFilterkoukan1_fpsaikai_day(getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERKOUKAN1_FPSAIKAI_DAY));
        bean.setFilterkoukan1_fpsaikai_time(getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERKOUKAN1_FPSAIKAI_TIME));
        bean.setFilterkoukan1_tantousya(getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERKOUKAN1_TANTOUSYA));
        bean.setFpsyuuryou_day(getItemRow(processData.getItemList(), GXHDO102B026Const.FPSYUURYOU_DAY));
        bean.setFpsyuuryou_time(getItemRow(processData.getItemList(), GXHDO102B026Const.FPSYUURYOU_TIME));
        bean.setFptotaljikan(getItemRow(processData.getItemList(), GXHDO102B026Const.FPTOTALJIKAN));
        bean.setItijifiltersousiyouhonsuu(getItemRow(processData.getItemList(), GXHDO102B026Const.ITIJIFILTERSOUSIYOUHONSUU));
        bean.setNijifiltersousiyouhonsuu(getItemRow(processData.getItemList(), GXHDO102B026Const.NIJIFILTERSOUSIYOUHONSUU));
        bean.setSanjifiltersousiyouhonsuu(getItemRow(processData.getItemList(), GXHDO102B026Const.SANJIFILTERSOUSIYOUHONSUU));
        bean.setFilterpasssyuuryou_tantousya(getItemRow(processData.getItemList(), GXHDO102B026Const.FILTERPASSSYUURYOU_TANTOUSYA));
        bean.setSoujyuurousokutei1(getItemRow(processData.getItemList(), GXHDO102B026Const.SOUJYUUROUSOKUTEI1));
        bean.setSoujyuurousokutei2(getItemRow(processData.getItemList(), GXHDO102B026Const.SOUJYUUROUSOKUTEI2));
        bean.setSoujyuurousokutei3(getItemRow(processData.getItemList(), GXHDO102B026Const.SOUJYUUROUSOKUTEI3));
        bean.setSoujyuurousokutei4(getItemRow(processData.getItemList(), GXHDO102B026Const.SOUJYUUROUSOKUTEI4));
        bean.setSoujyuurousokutei5(getItemRow(processData.getItemList(), GXHDO102B026Const.SOUJYUUROUSOKUTEI5));
        bean.setSoujyuurousokutei6(getItemRow(processData.getItemList(), GXHDO102B026Const.SOUJYUUROUSOKUTEI6));
        bean.setSoujyuurousokutei7(getItemRow(processData.getItemList(), GXHDO102B026Const.SOUJYUUROUSOKUTEI7));
        bean.setSoujyuurousokutei8(getItemRow(processData.getItemList(), GXHDO102B026Const.SOUJYUUROUSOKUTEI8));
        bean.setSoujyuurousokutei9(getItemRow(processData.getItemList(), GXHDO102B026Const.SOUJYUUROUSOKUTEI9));
        bean.setSoujyuurousokutei10(getItemRow(processData.getItemList(), GXHDO102B026Const.SOUJYUUROUSOKUTEI10));
        bean.setSoujyuurousokutei11(getItemRow(processData.getItemList(), GXHDO102B026Const.SOUJYUUROUSOKUTEI11));
        bean.setSoujyuurousokutei12(getItemRow(processData.getItemList(), GXHDO102B026Const.SOUJYUUROUSOKUTEI12));
        bean.setYuudentaislurryjyuurou1(getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU1));
        bean.setYuudentaislurryjyuurou2(getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU2));
        bean.setYuudentaislurryjyuurou3(getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU3));
        bean.setYuudentaislurryjyuurou4(getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU4));
        bean.setYuudentaislurryjyuurou5(getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU5));
        bean.setYuudentaislurryjyuurou6(getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU6));
        bean.setYuudentaislurryjyuurou7(getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU7));
        bean.setYuudentaislurryjyuurou8(getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU8));
        bean.setYuudentaislurryjyuurou9(getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU9));
        bean.setYuudentaislurryjyuurou10(getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU10));
        bean.setYuudentaislurryjyuurou11(getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU11));
        bean.setYuudentaislurryjyuurou12(getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROU12));
        bean.setYuudentaislurryjyuurougoukei(getItemRow(processData.getItemList(), GXHDO102B026Const.YUUDENTAISLURRYJYUUROUGOUKEI));
        bean.setTounyuuryou(getItemRow(processData.getItemList(), GXHDO102B026Const.TOUNYUURYOU));
        bean.setBudomarikeisan(getItemRow(processData.getItemList(), GXHDO102B026Const.BUDOMARIKEISAN));
        bean.setUudentaislurryyuukoukigen(getItemRow(processData.getItemList(), GXHDO102B026Const.UUDENTAISLURRYYUUKOUKIGEN));
        bean.setFunsaihantei(getItemRow(processData.getItemList(), GXHDO102B026Const.FUNSAIHANTEI));
        bean.setSeihinjyuuryoukakunin_tantousya(getItemRow(processData.getItemList(), GXHDO102B026Const.SEIHINJYUURYOUKAKUNIN_TANTOUSYA));
        bean.setHozonyousamplekaisyu(getItemRow(processData.getItemList(), GXHDO102B026Const.HOZONYOUSAMPLEKAISYU));
        bean.setZunsekiyousamplekaisyu(getItemRow(processData.getItemList(), GXHDO102B026Const.ZUNSEKIYOUSAMPLEKAISYU));
        bean.setKansouzara(getItemRow(processData.getItemList(), GXHDO102B026Const.KANSOUZARA));
        bean.setArumizarafuutaijyuuryou(getItemRow(processData.getItemList(), GXHDO102B026Const.ARUMIZARAFUUTAIJYUURYOU));
        bean.setSlurrysamplejyuuryoukikaku(getItemRow(processData.getItemList(), GXHDO102B026Const.SLURRYSAMPLEJYUURYOUKIKAKU));
        bean.setKansoumaeslurryjyuuryou(getItemRow(processData.getItemList(), GXHDO102B026Const.KANSOUMAESLURRYJYUURYOU));
        bean.setKansouki(getItemRow(processData.getItemList(), GXHDO102B026Const.KANSOUKI));
        bean.setKansouondokikaku(getItemRow(processData.getItemList(), GXHDO102B026Const.KANSOUONDOKIKAKU));
        bean.setKansoujikankikaku(getItemRow(processData.getItemList(), GXHDO102B026Const.KANSOUJIKANKIKAKU));
        bean.setKansoukaisi_day(getItemRow(processData.getItemList(), GXHDO102B026Const.KANSOUKAISI_DAY));
        bean.setKansoukaisi_time(getItemRow(processData.getItemList(), GXHDO102B026Const.KANSOUKAISI_TIME));
        bean.setKansousyuuryou_day(getItemRow(processData.getItemList(), GXHDO102B026Const.KANSOUSYUURYOU_DAY));
        bean.setKansousyuuryou_time(getItemRow(processData.getItemList(), GXHDO102B026Const.KANSOUSYUURYOU_TIME));
        bean.setKansoujikantotal(getItemRow(processData.getItemList(), GXHDO102B026Const.KANSOUJIKANTOTAL));
        bean.setKansougosoujyuuryou(getItemRow(processData.getItemList(), GXHDO102B026Const.KANSOUGOSOUJYUURYOU));
        bean.setKansougosyoumijyuuryou(getItemRow(processData.getItemList(), GXHDO102B026Const.KANSOUGOSYOUMIJYUURYOU));
        bean.setKokeibunhiritu(getItemRow(processData.getItemList(), GXHDO102B026Const.KOKEIBUNHIRITU));
        bean.setBikou1(getItemRow(processData.getItemList(), GXHDO102B026Const.BIKOU1));
        bean.setBikou2(getItemRow(processData.getItemList(), GXHDO102B026Const.BIKOU2));

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
        allItemIdMap.put(GXHDO102B026Const.WIPLOTNO, "WIPﾛｯﾄNo");
        allItemIdMap.put(GXHDO102B026Const.YUUDENTAISLURRYHINMEI, "誘電体ｽﾗﾘｰ品名");
        allItemIdMap.put(GXHDO102B026Const.YUUDENTAISLURRYLOTNO, "誘電体ｽﾗﾘｰLotNo");
        allItemIdMap.put(GXHDO102B026Const.LOTKUBUN, "ﾛｯﾄ区分");
        allItemIdMap.put(GXHDO102B026Const.GENRYOULOTNO, "原料LotNo");
        allItemIdMap.put(GXHDO102B026Const.GENRYOUKIGOU, "原料記号");
        allItemIdMap.put(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI1, "風袋重量測定_ｽﾃﾝ容器1");
        allItemIdMap.put(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI2, "風袋重量測定_ｽﾃﾝ容器2");
        allItemIdMap.put(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI3, "風袋重量測定_ｽﾃﾝ容器3");
        allItemIdMap.put(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI4, "風袋重量測定_ｽﾃﾝ容器4");
        allItemIdMap.put(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI5, "風袋重量測定_ｽﾃﾝ容器5");
        allItemIdMap.put(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI6, "風袋重量測定_ｽﾃﾝ容器6");
        allItemIdMap.put(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI1, "風袋重量測定_白ﾎﾟﾘ容器1");
        allItemIdMap.put(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI2, "風袋重量測定_白ﾎﾟﾘ容器2");
        allItemIdMap.put(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI3, "風袋重量測定_白ﾎﾟﾘ容器3");
        allItemIdMap.put(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI4, "風袋重量測定_白ﾎﾟﾘ容器4");
        allItemIdMap.put(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI5, "風袋重量測定_白ﾎﾟﾘ容器5");
        allItemIdMap.put(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI6, "風袋重量測定_白ﾎﾟﾘ容器6");
        allItemIdMap.put(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI7, "風袋重量測定_白ﾎﾟﾘ容器7");
        allItemIdMap.put(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI8, "風袋重量測定_白ﾎﾟﾘ容器8");
        allItemIdMap.put(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI9, "風袋重量測定_白ﾎﾟﾘ容器9");
        allItemIdMap.put(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI10, "風袋重量測定_白ﾎﾟﾘ容器10");
        allItemIdMap.put(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI11, "風袋重量測定_白ﾎﾟﾘ容器11");
        allItemIdMap.put(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SIROPORI12, "風袋重量測定_白ﾎﾟﾘ容器12");
        allItemIdMap.put(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_TANTOUSYA, "保管容器準備_担当者");
        allItemIdMap.put(GXHDO102B026Const.FILTERRENKETU, "ﾌｨﾙﾀｰ連結");
        allItemIdMap.put(GXHDO102B026Const.FILTERTORITUKE_ITIJIFILTERHINMEI, "ﾌｨﾙﾀｰ取り付け_1次ﾌｨﾙﾀｰ品名");
        allItemIdMap.put(GXHDO102B026Const.FILTERTORITUKE_LOTNO1, "ﾌｨﾙﾀｰ取り付け_LotNo1");
        allItemIdMap.put(GXHDO102B026Const.FILTERTORITUKE_TORITUKEHONSUU1, "ﾌｨﾙﾀｰ取り付け_取り付け本数1");
        allItemIdMap.put(GXHDO102B026Const.FILTERTORITUKE_NIJIFILTERHINMEI, "ﾌｨﾙﾀｰ取り付け_2次ﾌｨﾙﾀｰ品名");
        allItemIdMap.put(GXHDO102B026Const.FILTERTORITUKE_LOTNO2, "ﾌｨﾙﾀｰ取り付け_LotNo2");
        allItemIdMap.put(GXHDO102B026Const.FILTERTORITUKE_TORITUKEHONSUU2, "ﾌｨﾙﾀｰ取り付け_取り付け本数2");
        allItemIdMap.put(GXHDO102B026Const.FILTERTORITUKE_SANJIFILTERHINMEI, "ﾌｨﾙﾀｰ取り付け_3次ﾌｨﾙﾀｰ品名");
        allItemIdMap.put(GXHDO102B026Const.FILTERTORITUKE_LOTNO3, "ﾌｨﾙﾀｰ取り付け_LotNo3");
        allItemIdMap.put(GXHDO102B026Const.FILTERTORITUKE_TORITUKEHONSUU3, "ﾌｨﾙﾀｰ取り付け_取り付け本数3");
        allItemIdMap.put(GXHDO102B026Const.FILTERTORITUKE_TANTOUSYA, "ﾌｨﾙﾀｰ取り付け_担当者");
        allItemIdMap.put(GXHDO102B026Const.HAISYUTUYOUKINOUTIBUKURO, "排出容器の内袋");
        allItemIdMap.put(GXHDO102B026Const.FILTERSIYOUKAISUU, "ﾌｨﾙﾀｰ使用回数");
        allItemIdMap.put(GXHDO102B026Const.FPTANKNO, "F/PﾀﾝｸNo");
        allItemIdMap.put(GXHDO102B026Const.SENJYOUKAKUNIN, "洗浄確認");
        allItemIdMap.put(GXHDO102B026Const.FPKAISI_DAY, "F/P開始日");
        allItemIdMap.put(GXHDO102B026Const.FPKAISI_TIME, "F/P開始時間");
        allItemIdMap.put(GXHDO102B026Const.ASSOUREGULATORNO, "圧送ﾚｷﾞｭﾚｰﾀｰNo");
        allItemIdMap.put(GXHDO102B026Const.ASSOUATURYOKU, "圧送圧力");
        allItemIdMap.put(GXHDO102B026Const.FILTERPASSKAISI_TANTOUSYA, "ﾌｨﾙﾀｰﾊﾟｽ開始_担当者");
        allItemIdMap.put(GXHDO102B026Const.FILTERKOUKAN1_FPTEISI_DAY, "ﾌｨﾙﾀｰ交換①_F/P停止日");
        allItemIdMap.put(GXHDO102B026Const.FILTERKOUKAN1_FPTEISI_TIME, "ﾌｨﾙﾀｰ交換①_F/P停止時間");
        allItemIdMap.put(GXHDO102B026Const.FILTERKOUKAN1_ITIJIFILTERHINMEI, "ﾌｨﾙﾀｰ交換①_1次ﾌｨﾙﾀｰ品名");
        allItemIdMap.put(GXHDO102B026Const.FILTERKOUKAN1_LOTNO1, "ﾌｨﾙﾀｰ交換①_LotNo1");
        allItemIdMap.put(GXHDO102B026Const.FILTERKOUKAN1_TORITUKEHONSUU1, "ﾌｨﾙﾀｰ交換①_取り付け本数1");
        allItemIdMap.put(GXHDO102B026Const.FILTERKOUKAN1_NIJIFILTERHINMEI, "ﾌｨﾙﾀｰ交換①_2次ﾌｨﾙﾀｰ品名");
        allItemIdMap.put(GXHDO102B026Const.FILTERKOUKAN1_LOTNO2, "ﾌｨﾙﾀｰ交換①_LotNo2");
        allItemIdMap.put(GXHDO102B026Const.FILTERKOUKAN1_TORITUKEHONSUU2, "ﾌｨﾙﾀｰ交換①_取り付け本数2");
        allItemIdMap.put(GXHDO102B026Const.FILTERKOUKAN1_SANJIFILTERHINMEI, "ﾌｨﾙﾀｰ交換①_3次ﾌｨﾙﾀｰ品名");
        allItemIdMap.put(GXHDO102B026Const.FILTERKOUKAN1_LOTNO3, "ﾌｨﾙﾀｰ交換①_LotNo3");
        allItemIdMap.put(GXHDO102B026Const.FILTERKOUKAN1_TORITUKEHONSUU3, "ﾌｨﾙﾀｰ交換①_取り付け本数3");
        allItemIdMap.put(GXHDO102B026Const.FILTERKOUKAN1_FPSAIKAI_DAY, "ﾌｨﾙﾀｰ交換①_F/P再開日");
        allItemIdMap.put(GXHDO102B026Const.FILTERKOUKAN1_FPSAIKAI_TIME, "ﾌｨﾙﾀｰ交換①_F/P再開時間");
        allItemIdMap.put(GXHDO102B026Const.FILTERKOUKAN1_TANTOUSYA, "ﾌｨﾙﾀｰ交換①_担当者");
        allItemIdMap.put(GXHDO102B026Const.FPSYUURYOU_DAY, "F/P終了日");
        allItemIdMap.put(GXHDO102B026Const.FPSYUURYOU_TIME, "F/P終了時間");
        allItemIdMap.put(GXHDO102B026Const.FPTOTALJIKAN, "F/Pﾄｰﾀﾙ時間");
        allItemIdMap.put(GXHDO102B026Const.ITIJIFILTERSOUSIYOUHONSUU, "1次ﾌｨﾙﾀｰ総使用本数");
        allItemIdMap.put(GXHDO102B026Const.NIJIFILTERSOUSIYOUHONSUU, "2次ﾌｨﾙﾀｰ総使用本数");
        allItemIdMap.put(GXHDO102B026Const.SANJIFILTERSOUSIYOUHONSUU, "3次ﾌｨﾙﾀｰ総使用本数");
        allItemIdMap.put(GXHDO102B026Const.FILTERPASSSYUURYOU_TANTOUSYA, "ﾌｨﾙﾀｰﾊﾟｽ終了_担当者");
        allItemIdMap.put(GXHDO102B026Const.SOUJYUUROUSOKUTEI1, "総重量測定1");
        allItemIdMap.put(GXHDO102B026Const.SOUJYUUROUSOKUTEI2, "総重量測定2");
        allItemIdMap.put(GXHDO102B026Const.SOUJYUUROUSOKUTEI3, "総重量測定3");
        allItemIdMap.put(GXHDO102B026Const.SOUJYUUROUSOKUTEI4, "総重量測定4");
        allItemIdMap.put(GXHDO102B026Const.SOUJYUUROUSOKUTEI5, "総重量測定5");
        allItemIdMap.put(GXHDO102B026Const.SOUJYUUROUSOKUTEI6, "総重量測定6");
        allItemIdMap.put(GXHDO102B026Const.SOUJYUUROUSOKUTEI7, "総重量測定7");
        allItemIdMap.put(GXHDO102B026Const.SOUJYUUROUSOKUTEI8, "総重量測定8");
        allItemIdMap.put(GXHDO102B026Const.SOUJYUUROUSOKUTEI9, "総重量測定9");
        allItemIdMap.put(GXHDO102B026Const.SOUJYUUROUSOKUTEI10, "総重量測定10");
        allItemIdMap.put(GXHDO102B026Const.SOUJYUUROUSOKUTEI11, "総重量測定11");
        allItemIdMap.put(GXHDO102B026Const.SOUJYUUROUSOKUTEI12, "総重量測定12");
        allItemIdMap.put(GXHDO102B026Const.YUUDENTAISLURRYJYUUROU1, "誘電体ｽﾗﾘｰ重量1");
        allItemIdMap.put(GXHDO102B026Const.YUUDENTAISLURRYJYUUROU2, "誘電体ｽﾗﾘｰ重量2");
        allItemIdMap.put(GXHDO102B026Const.YUUDENTAISLURRYJYUUROU3, "誘電体ｽﾗﾘｰ重量3");
        allItemIdMap.put(GXHDO102B026Const.YUUDENTAISLURRYJYUUROU4, "誘電体ｽﾗﾘｰ重量4");
        allItemIdMap.put(GXHDO102B026Const.YUUDENTAISLURRYJYUUROU5, "誘電体ｽﾗﾘｰ重量5");
        allItemIdMap.put(GXHDO102B026Const.YUUDENTAISLURRYJYUUROU6, "誘電体ｽﾗﾘｰ重量6");
        allItemIdMap.put(GXHDO102B026Const.YUUDENTAISLURRYJYUUROU7, "誘電体ｽﾗﾘｰ重量7");
        allItemIdMap.put(GXHDO102B026Const.YUUDENTAISLURRYJYUUROU8, "誘電体ｽﾗﾘｰ重量8");
        allItemIdMap.put(GXHDO102B026Const.YUUDENTAISLURRYJYUUROU9, "誘電体ｽﾗﾘｰ重量9");
        allItemIdMap.put(GXHDO102B026Const.YUUDENTAISLURRYJYUUROU10, "誘電体ｽﾗﾘｰ重量10");
        allItemIdMap.put(GXHDO102B026Const.YUUDENTAISLURRYJYUUROU11, "誘電体ｽﾗﾘｰ重量11");
        allItemIdMap.put(GXHDO102B026Const.YUUDENTAISLURRYJYUUROU12, "誘電体ｽﾗﾘｰ重量12");
        allItemIdMap.put(GXHDO102B026Const.YUUDENTAISLURRYJYUUROUGOUKEI, "誘電体ｽﾗﾘｰ重量合計");
        allItemIdMap.put(GXHDO102B026Const.TOUNYUURYOU, "投入量");
        allItemIdMap.put(GXHDO102B026Const.BUDOMARIKEISAN, "歩留まり計算");
        allItemIdMap.put(GXHDO102B026Const.UUDENTAISLURRYYUUKOUKIGEN, "誘電体ｽﾗﾘｰ有効期限");
        allItemIdMap.put(GXHDO102B026Const.FUNSAIHANTEI, "粉砕判定");
        allItemIdMap.put(GXHDO102B026Const.SEIHINJYUURYOUKAKUNIN_TANTOUSYA, "製品重量確認_担当者");
        allItemIdMap.put(GXHDO102B026Const.HOZONYOUSAMPLEKAISYU, "保存用ｻﾝﾌﾟﾙ回収");
        allItemIdMap.put(GXHDO102B026Const.ZUNSEKIYOUSAMPLEKAISYU, "分析用ｻﾝﾌﾟﾙ回収");
        allItemIdMap.put(GXHDO102B026Const.KANSOUZARA, "乾燥皿");
        allItemIdMap.put(GXHDO102B026Const.ARUMIZARAFUUTAIJYUURYOU, "ｱﾙﾐ皿風袋重量");
        allItemIdMap.put(GXHDO102B026Const.SLURRYSAMPLEJYUURYOUKIKAKU, "ｽﾗﾘｰｻﾝﾌﾟﾙ重量規格");
        allItemIdMap.put(GXHDO102B026Const.KANSOUMAESLURRYJYUURYOU, "乾燥前ｽﾗﾘｰ重量");
        allItemIdMap.put(GXHDO102B026Const.KANSOUKI, "乾燥機");
        allItemIdMap.put(GXHDO102B026Const.KANSOUONDOKIKAKU, "乾燥温度規格");
        allItemIdMap.put(GXHDO102B026Const.KANSOUJIKANKIKAKU, "乾燥時間規格");
        allItemIdMap.put(GXHDO102B026Const.KANSOUKAISI_DAY, "乾燥開始日");
        allItemIdMap.put(GXHDO102B026Const.KANSOUKAISI_TIME, "乾燥開始時間");
        allItemIdMap.put(GXHDO102B026Const.KANSOUSYUURYOU_DAY, "乾燥終了日");
        allItemIdMap.put(GXHDO102B026Const.KANSOUSYUURYOU_TIME, "乾燥終了時間");
        allItemIdMap.put(GXHDO102B026Const.KANSOUJIKANTOTAL, "乾燥時間ﾄｰﾀﾙ");
        allItemIdMap.put(GXHDO102B026Const.KANSOUGOSOUJYUURYOU, "乾燥後総重量");
        allItemIdMap.put(GXHDO102B026Const.KANSOUGOSYOUMIJYUURYOU, "乾燥後正味重量");
        allItemIdMap.put(GXHDO102B026Const.KOKEIBUNHIRITU, "固形分比率");
        allItemIdMap.put(GXHDO102B026Const.BIKOU1, "備考1");
        allItemIdMap.put(GXHDO102B026Const.BIKOU2, "備考2");

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
        GXHDO102B026A bean = (GXHDO102B026A) getFormBean("gXHDO102B026A");
        notShowItemList.forEach((notShowItem) -> {
            itemList.remove(getItemRow(itemList, notShowItem));
        });

        bean.setFuutaijyuuryousokutei_sutenyouki1(null);
        bean.setFuutaijyuuryousokutei_sutenyouki2(null);
        bean.setFuutaijyuuryousokutei_sutenyouki3(null);
        bean.setFuutaijyuuryousokutei_sutenyouki4(null);
        bean.setFuutaijyuuryousokutei_sutenyouki5(null);
        bean.setFuutaijyuuryousokutei_sutenyouki6(null);
        bean.setYuudentaislurryjyuurou1(null);
        bean.setYuudentaislurryjyuurou2(null);
        bean.setYuudentaislurryjyuurou3(null);
        bean.setYuudentaislurryjyuurou4(null);
        bean.setYuudentaislurryjyuurou5(null);
        bean.setYuudentaislurryjyuurou6(null);
        bean.setYuudentaislurryjyuurou7(null);
        bean.setYuudentaislurryjyuurou8(null);
        bean.setYuudentaislurryjyuurou9(null);
        bean.setYuudentaislurryjyuurou10(null);
        bean.setYuudentaislurryjyuurou11(null);
        bean.setYuudentaislurryjyuurou12(null);
        bean.setYuudentaislurryjyuurougoukei(null);
        bean.setTounyuuryou(null);
        bean.setBudomarikeisan(null);
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
        Map fxhbm03Data = loadFxhbm03Data(queryRunnerDoc, "誘電体ｽﾗﾘｰ作製_ﾌｨﾙﾀｰﾊﾟｽ_保管_表示制御");
        // 画面非表示項目リスト: 風袋重量測定_ｽﾃﾝ容器1、風袋重量測定_ｽﾃﾝ容器2、風袋重量測定_ｽﾃﾝ容器3、風袋重量測定_ｽﾃﾝ容器4、風袋重量測定_ｽﾃﾝ容器5、風袋重量測定_ｽﾃﾝ容器6、
        // 誘電体ｽﾗﾘｰ重量1、誘電体ｽﾗﾘｰ重量2、誘電体ｽﾗﾘｰ重量3、誘電体ｽﾗﾘｰ重量4、誘電体ｽﾗﾘｰ重量5、誘電体ｽﾗﾘｰ重量6、誘電体ｽﾗﾘｰ重量7、誘電体ｽﾗﾘｰ重量8、
        // 誘電体ｽﾗﾘｰ重量9、誘電体ｽﾗﾘｰ重量10、誘電体ｽﾗﾘｰ重量11、誘電体ｽﾗﾘｰ重量12、誘電体ｽﾗﾘｰ重量合計、投入量、歩留まり計算、
        List<String> notShowItemList = Arrays.asList(GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI1, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI2,
                GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI3, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI4, GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI5,
                GXHDO102B026Const.FUUTAIJYUURYOUSOKUTEI_SUTENYOUKI6, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU1, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU2, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU3,
                GXHDO102B026Const.YUUDENTAISLURRYJYUUROU4, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU5, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU6, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU7,
                GXHDO102B026Const.YUUDENTAISLURRYJYUUROU8, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU9, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU10, GXHDO102B026Const.YUUDENTAISLURRYJYUUROU11,
                GXHDO102B026Const.YUUDENTAISLURRYJYUUROU12, GXHDO102B026Const.YUUDENTAISLURRYJYUUROUGOUKEI, GXHDO102B026Const.TOUNYUURYOU, GXHDO102B026Const.BUDOMARIKEISAN);

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

    /**
     * [誘電体ｽﾗﾘｰ作製・粉砕]から、ﾃﾞｰﾀの取得
     *
     * @param queryRunnerQcdb オブジェクト
     * @param hinmei 品名
     * @param pattern ﾊﾟﾀｰﾝ
     * @param syurui 種類
     * @param sryuudentaifunsaiData 粉砕終了日
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadSryuudentaifunsaiData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban) throws SQLException {
        String sql = "SELECT syuuryounichiji FROM sr_yuudentai_funsai"
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? ";
        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }
}
