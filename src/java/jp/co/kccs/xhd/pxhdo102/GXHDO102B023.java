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
import jp.co.kccs.xhd.db.model.SrYuudentaiPremixing;
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
 * 変更日	2021/11/12<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B023(誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞ)
 *
 * @author KCSS K.Jo
 * @since 2021/11/12
 */
public class GXHDO102B023 implements IFormLogic {

    private static final Logger LOGGER = Logger.getLogger(GXHDO102B023.class.getName());
    private static final String JOTAI_FLG_KARI_TOROKU = "0";
    private static final String JOTAI_FLG_TOROKUZUMI = "1";
    private static final String JOTAI_FLG_SAKUJO = "9";
    private static final String SQL_STATE_RECORD_LOCK_ERR = "55P03";

    /**
     * コンストラクタ
     */
    public GXHDO102B023() {
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
            initGXHDO102B023A(processData);

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
            processData.setNoCheckButtonId(Arrays.asList(GXHDO102B023Const.BTN_EDABAN_COPY_TOP,
                    GXHDO102B023Const.BTN_TOUNYUUKAISI_TOP,
                    GXHDO102B023Const.BTN_KAKUHANKAISI1_TOP,
                    GXHDO102B023Const.BTN_KAKUHANSYUURYOU1_TOP,
                    GXHDO102B023Const.BTN_KAKUHANKAISI2_TOP,
                    GXHDO102B023Const.BTN_KAKUHANSYUURYOU2_TOP,
                    GXHDO102B023Const.BTN_KAKUHANKAISI3_TOP,
                    GXHDO102B023Const.BTN_KAKUHANSYUURYOU3_TOP,
                    GXHDO102B023Const.BTN_KAKUHANKAISI4_TOP,
                    GXHDO102B023Const.BTN_KAKUHANSYUURYOU4_TOP,
                    GXHDO102B023Const.BTN_KAKUHANKAISI5_TOP,
                    GXHDO102B023Const.BTN_KAKUHANSYUURYOU5_TOP,
                    GXHDO102B023Const.BTN_KAKUHANKAISI6_TOP,
                    GXHDO102B023Const.BTN_KAKUHANSYUURYOU6_TOP,
                    GXHDO102B023Const.BTN_KAKUHANKAISI7_TOP,
                    GXHDO102B023Const.BTN_KAKUHANSYUURYOU7_TOP,
                    GXHDO102B023Const.BTN_TOUNYUUSYUURYOU_TOP,
                    GXHDO102B023Const.BTN_EDABAN_COPY_BOTTOM,
                    GXHDO102B023Const.BTN_TOUNYUUKAISI_BOTTOM,
                    GXHDO102B023Const.BTN_KAKUHANKAISI1_BOTTOM,
                    GXHDO102B023Const.BTN_KAKUHANSYUURYOU1_BOTTOM,
                    GXHDO102B023Const.BTN_KAKUHANKAISI2_BOTTOM,
                    GXHDO102B023Const.BTN_KAKUHANSYUURYOU2_BOTTOM,
                    GXHDO102B023Const.BTN_KAKUHANKAISI3_BOTTOM,
                    GXHDO102B023Const.BTN_KAKUHANSYUURYOU3_BOTTOM,
                    GXHDO102B023Const.BTN_KAKUHANKAISI4_BOTTOM,
                    GXHDO102B023Const.BTN_KAKUHANSYUURYOU4_BOTTOM,
                    GXHDO102B023Const.BTN_KAKUHANKAISI5_BOTTOM,
                    GXHDO102B023Const.BTN_KAKUHANSYUURYOU5_BOTTOM,
                    GXHDO102B023Const.BTN_KAKUHANKAISI6_BOTTOM,
                    GXHDO102B023Const.BTN_KAKUHANSYUURYOU6_BOTTOM,
                    GXHDO102B023Const.BTN_KAKUHANKAISI7_BOTTOM,
                    GXHDO102B023Const.BTN_KAKUHANSYUURYOU7_BOTTOM,
                    GXHDO102B023Const.BTN_TOUNYUUSYUURYOU_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO102B023Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO102B023Const.BTN_INSERT_TOP,
                    GXHDO102B023Const.BTN_DELETE_TOP,
                    GXHDO102B023Const.BTN_UPDATE_TOP,
                    GXHDO102B023Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO102B023Const.BTN_INSERT_BOTTOM,
                    GXHDO102B023Const.BTN_DELETE_BOTTOM,
                    GXHDO102B023Const.BTN_UPDATE_BOTTOM
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
            case GXHDO102B023Const.BTN_EDABAN_COPY_TOP:
            case GXHDO102B023Const.BTN_EDABAN_COPY_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 仮登録
            case GXHDO102B023Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO102B023Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO102B023Const.BTN_INSERT_TOP:
            case GXHDO102B023Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 修正
            case GXHDO102B023Const.BTN_UPDATE_TOP:
            case GXHDO102B023Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO102B023Const.BTN_DELETE_TOP:
            case GXHDO102B023Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 投入開始日時
            case GXHDO102B023Const.BTN_TOUNYUUKAISI_TOP:
            case GXHDO102B023Const.BTN_TOUNYUUKAISI_BOTTOM:
                method = "setTounyuukaisiDateTime";
                break;
            // 投入終了日時
            case GXHDO102B023Const.BTN_TOUNYUUSYUURYOU_TOP:
            case GXHDO102B023Const.BTN_TOUNYUUSYUURYOU_BOTTOM:
                method = "setTounyuusyuuryouDateTime";
                break;
            // 撹拌開始日時①
            case GXHDO102B023Const.BTN_KAKUHANKAISI1_TOP:
            case GXHDO102B023Const.BTN_KAKUHANKAISI1_BOTTOM:
                method = "setKakuhankaisi1DateTime";
                break;
            // 撹拌終了日時①
            case GXHDO102B023Const.BTN_KAKUHANSYUURYOU1_TOP:
            case GXHDO102B023Const.BTN_KAKUHANSYUURYOU1_BOTTOM:
                method = "setKakuhansyuuryou1DateTime";
                break;
            // 撹拌開始日時②
            case GXHDO102B023Const.BTN_KAKUHANKAISI2_TOP:
            case GXHDO102B023Const.BTN_KAKUHANKAISI2_BOTTOM:
                method = "setKakuhankaisi2DateTime";
                break;
            // 撹拌終了日時②
            case GXHDO102B023Const.BTN_KAKUHANSYUURYOU2_TOP:
            case GXHDO102B023Const.BTN_KAKUHANSYUURYOU2_BOTTOM:
                method = "setKakuhansyuuryou2DateTime";
                break;
            // 撹拌開始日時③
            case GXHDO102B023Const.BTN_KAKUHANKAISI3_TOP:
            case GXHDO102B023Const.BTN_KAKUHANKAISI3_BOTTOM:
                method = "setKakuhankaisi3DateTime";
                break;
            // 撹拌終了日時③
            case GXHDO102B023Const.BTN_KAKUHANSYUURYOU3_TOP:
            case GXHDO102B023Const.BTN_KAKUHANSYUURYOU3_BOTTOM:
                method = "setKakuhansyuuryou3DateTime";
                break;
            // 撹拌開始日時④
            case GXHDO102B023Const.BTN_KAKUHANKAISI4_TOP:
            case GXHDO102B023Const.BTN_KAKUHANKAISI4_BOTTOM:
                method = "setKakuhankaisi4DateTime";
                break;
            // 撹拌終了日時④
            case GXHDO102B023Const.BTN_KAKUHANSYUURYOU4_TOP:
            case GXHDO102B023Const.BTN_KAKUHANSYUURYOU4_BOTTOM:
                method = "setKakuhansyuuryou4DateTime";
                break;
            // 撹拌開始日時⑤
            case GXHDO102B023Const.BTN_KAKUHANKAISI5_TOP:
            case GXHDO102B023Const.BTN_KAKUHANKAISI5_BOTTOM:
                method = "setKakuhankaisi5DateTime";
                break;
            // 撹拌終了日時⑤
            case GXHDO102B023Const.BTN_KAKUHANSYUURYOU5_TOP:
            case GXHDO102B023Const.BTN_KAKUHANSYUURYOU5_BOTTOM:
                method = "setKakuhansyuuryou5DateTime";
                break;
            // 撹拌開始日時⑥
            case GXHDO102B023Const.BTN_KAKUHANKAISI6_TOP:
            case GXHDO102B023Const.BTN_KAKUHANKAISI6_BOTTOM:
                method = "setKakuhankaisi6DateTime";
                break;
            // 撹拌終了日時⑥
            case GXHDO102B023Const.BTN_KAKUHANSYUURYOU6_TOP:
            case GXHDO102B023Const.BTN_KAKUHANSYUURYOU6_BOTTOM:
                method = "setKakuhansyuuryou6DateTime";
                break;
            // 撹拌開始日時⑦
            case GXHDO102B023Const.BTN_KAKUHANKAISI7_TOP:
            case GXHDO102B023Const.BTN_KAKUHANKAISI7_BOTTOM:
                method = "setKakuhankaisi7DateTime";
                break;
            // 撹拌終了日時⑦
            case GXHDO102B023Const.BTN_KAKUHANSYUURYOU7_TOP:
            case GXHDO102B023Const.BTN_KAKUHANSYUURYOU7_BOTTOM:
                method = "setKakuhansyuuryou7DateTime";
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

            // 誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞの入力項目の登録データ(仮登録時は仮登録データ)を取得
            List<SrYuudentaiPremixing> srBinderKakuhanDataList = getSrYuudentaiPremixingData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo9, oyalotEdaban);
            if (srBinderKakuhanDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }
            // メイン画面データ設定
            setInputItemDataMainForm(processData, srBinderKakuhanDataList.get(0));

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

                // 誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞ_仮登録処理
                insertTmpSrYuudentaiPremixing(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, strSystime, processData);
            } else {

                // 誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞ_仮登録更新処理
                updateTmpSrYuudentaiPremixing(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);
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

        // 撹拌時間規格①ﾁｪｯｸ
        List<String> kakuhankaisi1List = Arrays.asList(GXHDO102B023Const.KAKUHANKAISI1_DAY, GXHDO102B023Const.KAKUHANKAISI1_TIME);
        List<String> kakuhansyuuryou1List = Arrays.asList(GXHDO102B023Const.KAKUHANSYUURYOU1_DAY, GXHDO102B023Const.KAKUHANSYUURYOU1_TIME);

        // 撹拌時間規格②ﾁｪｯｸ
        List<String> kakuhankaisi2List = Arrays.asList(GXHDO102B023Const.KAKUHANKAISI2_DAY, GXHDO102B023Const.KAKUHANKAISI2_TIME);
        List<String> kakuhansyuuryou2List = Arrays.asList(GXHDO102B023Const.KAKUHANSYUURYOU2_DAY, GXHDO102B023Const.KAKUHANSYUURYOU2_TIME);

        // 撹拌時間規格③ﾁｪｯｸ
        List<String> kakuhankaisi3List = Arrays.asList(GXHDO102B023Const.KAKUHANKAISI3_DAY, GXHDO102B023Const.KAKUHANKAISI3_TIME);
        List<String> kakuhansyuuryou3List = Arrays.asList(GXHDO102B023Const.KAKUHANSYUURYOU3_DAY, GXHDO102B023Const.KAKUHANSYUURYOU3_TIME);

        //　撹拌時間規格④ﾁｪｯｸ
        List<String> kakuhankaisi4List = Arrays.asList(GXHDO102B023Const.KAKUHANKAISI4_DAY, GXHDO102B023Const.KAKUHANKAISI4_TIME);
        List<String> kakuhansyuuryou4List = Arrays.asList(GXHDO102B023Const.KAKUHANSYUURYOU4_DAY, GXHDO102B023Const.KAKUHANSYUURYOU4_TIME);

        // 撹拌時間規格⑤ﾁｪｯｸ
        List<String> kakuhankaisi5List = Arrays.asList(GXHDO102B023Const.KAKUHANKAISI5_DAY, GXHDO102B023Const.KAKUHANKAISI5_TIME);
        List<String> kakuhansyuuryou5List = Arrays.asList(GXHDO102B023Const.KAKUHANSYUURYOU5_DAY, GXHDO102B023Const.KAKUHANSYUURYOU5_TIME);

        // 撹拌時間規格⑥ﾁｪｯｸ
        List<String> kakuhankaisi6List = Arrays.asList(GXHDO102B023Const.KAKUHANKAISI6_DAY, GXHDO102B023Const.KAKUHANKAISI6_TIME);
        List<String> kakuhansyuuryou6List = Arrays.asList(GXHDO102B023Const.KAKUHANSYUURYOU6_DAY, GXHDO102B023Const.KAKUHANSYUURYOU6_TIME);

        // 撹拌時間規格⑦ﾁｪｯｸ
        List<String> kakuhankaisi7List = Arrays.asList(GXHDO102B023Const.KAKUHANKAISI7_DAY, GXHDO102B023Const.KAKUHANKAISI7_TIME);
        List<String> kakuhansyuuryou7List = Arrays.asList(GXHDO102B023Const.KAKUHANSYUURYOU7_DAY, GXHDO102B023Const.KAKUHANSYUURYOU7_TIME);
        // 規格値の入力値チェック必要の項目リスト
        List<FXHDD01> itemList = new ArrayList<>();
        // 撹拌時間①の規格チェック用項目
        FXHDD01 kakuhanjikan1DiffMinutes = getdiffMinutesKikakuItem(processData, GXHDO102B023Const.KAKUHANJIKANKIKAKU1, kakuhankaisi1List, kakuhansyuuryou1List);
        // 撹拌時間②の規格チェック用項目
        FXHDD01 kakuhanjikan2DiffMinutes = getdiffMinutesKikakuItem(processData, GXHDO102B023Const.KAKUHANJIKANKIKAKU2, kakuhankaisi2List, kakuhansyuuryou2List);
        // 撹拌時間③の規格チェック用項目
        FXHDD01 kakuhanjikan3DiffMinutes = getdiffMinutesKikakuItem(processData, GXHDO102B023Const.KAKUHANJIKANKIKAKU3, kakuhankaisi3List, kakuhansyuuryou3List);
        // 撹拌時間④の規格チェック用項目
        FXHDD01 kakuhanjikan4DiffMinutes = getdiffMinutesKikakuItem(processData, GXHDO102B023Const.KAKUHANJIKANKIKAKU4, kakuhankaisi4List, kakuhansyuuryou4List);
        // 撹拌時間⑤の規格チェック用項目
        FXHDD01 kakuhanjikan5DiffMinutes = getdiffMinutesKikakuItem(processData, GXHDO102B023Const.KAKUHANJIKANKIKAKU5, kakuhankaisi5List, kakuhansyuuryou5List);
        // 撹拌時間⑥の規格チェック用項目
        FXHDD01 kakuhanjikan6DiffMinutes = getdiffMinutesKikakuItem(processData, GXHDO102B023Const.KAKUHANJIKANKIKAKU6, kakuhankaisi6List, kakuhansyuuryou6List);
        // 撹拌時間⑦の規格チェック用項目
        FXHDD01 kakuhanjikan7DiffMinutes = getdiffMinutesKikakuItem(processData, GXHDO102B023Const.KAKUHANJIKANKIKAKU7, kakuhankaisi7List, kakuhansyuuryou7List);

        // 項目の項目名を設置
        if (kakuhanjikan1DiffMinutes != null) {
            kakuhanjikan1DiffMinutes.setLabel1("撹拌時間①");
            itemList.add(kakuhanjikan1DiffMinutes);
        }
        // 項目の項目名を設置
        if (kakuhanjikan2DiffMinutes != null) {
            kakuhanjikan2DiffMinutes.setLabel1("撹拌時間②");
            itemList.add(kakuhanjikan2DiffMinutes);
        }
        // 項目の項目名を設置
        if (kakuhanjikan3DiffMinutes != null) {
            kakuhanjikan3DiffMinutes.setLabel1("撹拌時間③");
            itemList.add(kakuhanjikan3DiffMinutes);
        }
        // 項目の項目名を設置
        if (kakuhanjikan4DiffMinutes != null) {
            kakuhanjikan4DiffMinutes.setLabel1("撹拌時間④");
            itemList.add(kakuhanjikan4DiffMinutes);
        }
        // 項目の項目名を設置
        if (kakuhanjikan5DiffMinutes != null) {
            kakuhanjikan5DiffMinutes.setLabel1("撹拌時間⑤");
            itemList.add(kakuhanjikan5DiffMinutes);
        }
        // 項目の項目名を設置
        if (kakuhanjikan6DiffMinutes != null) {
            kakuhanjikan6DiffMinutes.setLabel1("撹拌時間⑥");
            itemList.add(kakuhanjikan6DiffMinutes);
        }
        // 項目の項目名を設置
        if (kakuhanjikan7DiffMinutes != null) {
            kakuhanjikan7DiffMinutes.setLabel1("撹拌時間⑦");
            itemList.add(kakuhanjikan7DiffMinutes);
        }
        List<String> kikakuItemList = Arrays.asList(GXHDO102B023Const.KAKUHANJIKANKIKAKU1, GXHDO102B023Const.KAKUHANJIKANKIKAKU2, GXHDO102B023Const.KAKUHANJIKANKIKAKU3,
                GXHDO102B023Const.KAKUHANJIKANKIKAKU4, GXHDO102B023Const.KAKUHANJIKANKIKAKU5, GXHDO102B023Const.KAKUHANJIKANKIKAKU6, GXHDO102B023Const.KAKUHANJIKANKIKAKU7);
        processData.getItemList().stream().filter(
                (fxhdd01) -> !(kikakuItemList.contains(fxhdd01.getItemId()) || StringUtil.isEmpty(fxhdd01.getStandardPattern()) || "【-】".equals(fxhdd01.getKikakuChi()))).filter(
                        (fxhdd01) -> !(!ValidateUtil.isInputColumn(fxhdd01) || StringUtil.isEmpty(fxhdd01.getValue()))).forEachOrdered(
                        (fxhdd01) -> {
                            // 規格チェックの対象項目である、かつ入力項目かつ入力値がある項目はリストに追加
                            itemList.add(fxhdd01);
                        });
        ErrorMessageInfo errorMessageInfo = ValidateUtil.checkInputKikakuchi(itemList, kikakuchiInputErrorInfoList);

        // エラー項目の背景色を設定
        setItemBackColor(processData, kakuhankaisi1List, kakuhansyuuryou1List, "撹拌時間①", kikakuchiInputErrorInfoList, errorMessageInfo);
        setItemBackColor(processData, kakuhankaisi2List, kakuhansyuuryou2List, "撹拌時間②", kikakuchiInputErrorInfoList, errorMessageInfo);
        setItemBackColor(processData, kakuhankaisi3List, kakuhansyuuryou3List, "撹拌時間③", kikakuchiInputErrorInfoList, errorMessageInfo);
        setItemBackColor(processData, kakuhankaisi4List, kakuhansyuuryou4List, "撹拌時間④", kikakuchiInputErrorInfoList, errorMessageInfo);
        setItemBackColor(processData, kakuhankaisi5List, kakuhansyuuryou5List, "撹拌時間⑤", kikakuchiInputErrorInfoList, errorMessageInfo);
        setItemBackColor(processData, kakuhankaisi6List, kakuhansyuuryou6List, "撹拌時間⑥", kikakuchiInputErrorInfoList, errorMessageInfo);
        setItemBackColor(processData, kakuhankaisi7List, kakuhansyuuryou7List, "撹拌時間⑦", kikakuchiInputErrorInfoList, errorMessageInfo);

        return errorMessageInfo;
    }

    /**
     * 終了時間-開始時間で計算して、算出した差分分数を項目の規格値に設定する
     *
     * @param processData 処理データ
     * @param kikakuItem 規格値項目名
     * @param kakuhankaisiList 開始時間項目リスト
     * @param kakuhansyuuryouList 終了時間項目リスト
     * @return 項目データ
     */
    private FXHDD01 getdiffMinutesKikakuItem(ProcessData processData, String kikakuItem, List<String> kakuhankaisiList, List<String> kakuhansyuuryouList) {

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
     * @param kakuhankaisiList 開始時間項目リスト
     * @param kakuhansyuuryouList 終了時間項目リスト
     * @param label 項目の項目名
     * @param kikakuchiInputErrorInfoList 規格値入力エラー情報リスト
     * @param errorMessageInfo エラーメッセージ情報
     */
    private void setItemBackColor(ProcessData processData, List<String> kaisijikanList, List<String> syuuryoujikanList, String label, List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList,
            ErrorMessageInfo errorMessageInfo) {
        List<String> errorItemLabelList = new ArrayList<>();
        // エラー項目の背景色を設定
        kikakuchiInputErrorInfoList.stream().forEachOrdered((errorInfo) -> {
            errorItemLabelList.add(errorInfo.getItemLabel());
        });
        if (errorMessageInfo != null && !errorMessageInfo.getErrorMessage().contains(label)) {
            return;
        }
        if (errorItemLabelList.contains(label) || (errorMessageInfo != null && errorMessageInfo.getErrorMessage().contains(label))) {
            List<String> itemList = new ArrayList<>();
            itemList.addAll(kaisijikanList);
            itemList.addAll(syuuryoujikanList);

            itemList.stream().map((jikanName) -> getItemRow(processData.getItemList(), jikanName)).forEachOrdered((itemFxhdd01) -> {
                itemFxhdd01.setBackColorInput(ErrUtil.ERR_BACK_COLOR);
            });
        }
    }

    /**
     * 登録・修正項目チェック
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    private ErrorMessageInfo checkItemRegistCorrect(ProcessData processData) {
        ValidateUtil validateUtil = new ValidateUtil();
        // 投入開始日時、投入終了日時前後チェック
        FXHDD01 itemKaisiDay = getItemRow(processData.getItemList(), GXHDO102B023Const.TOUNYUUKAISI_DAY); //　投入開始日
        FXHDD01 itemKaisiTime = getItemRow(processData.getItemList(), GXHDO102B023Const.TOUNYUUKAISI_TIME); // 投入開始時間
        Date kaisiDate = DateUtil.convertStringToDate(itemKaisiDay.getValue(), itemKaisiTime.getValue());
        FXHDD01 itemSyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B023Const.TOUNYUUSYUURYOU_DAY); // 投入終了日
        FXHDD01 itemSyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B023Const.TOUNYUUSYUURYOU_TIME); // 投入終了時間
        Date syuuryouDate = DateUtil.convertStringToDate(itemSyuuryouDay.getValue(), itemSyuuryouTime.getValue());
        //R001チェック呼出し
        String msgCheckR001 = validateUtil.checkR001("投入開始日時", kaisiDate, "投入終了日時", syuuryouDate);
        if (!StringUtil.isEmpty(msgCheckR001)) {
            //エラー発生時
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemKaisiDay, itemKaisiTime, itemSyuuryouDay, itemSyuuryouTime);
            return MessageUtil.getErrorMessageInfo("", msgCheckR001, true, true, errFxhdd01List);
        }

        // 撹拌開始日時①、撹拌終了日時①前後チェック
        itemKaisiDay = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI1_DAY); //　撹拌開始日①
        itemKaisiTime = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI1_TIME); // 撹拌開始時間①
        itemSyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU1_DAY); // 撹拌終了日①
        itemSyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU1_TIME); // 撹拌終了時間①
        if (itemKaisiDay != null && itemKaisiTime != null && itemSyuuryouDay != null && itemSyuuryouTime != null) {
            kaisiDate = DateUtil.convertStringToDate(itemKaisiDay.getValue(), itemKaisiTime.getValue());
            syuuryouDate = DateUtil.convertStringToDate(itemSyuuryouDay.getValue(), itemSyuuryouTime.getValue());
            //R001チェック呼出し
            msgCheckR001 = validateUtil.checkR001("撹拌開始日時①", kaisiDate, "撹拌終了日時①", syuuryouDate);
            if (!StringUtil.isEmpty(msgCheckR001)) {
                //エラー発生時
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemKaisiDay, itemKaisiTime, itemSyuuryouDay, itemSyuuryouTime);
                return MessageUtil.getErrorMessageInfo("", msgCheckR001, true, true, errFxhdd01List);
            }
        }

        // 撹拌開始日時②、撹拌終了日時②前後チェック
        itemKaisiDay = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI2_DAY); //　撹拌開始日②
        itemKaisiTime = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI2_TIME); // 撹拌開始時間②
        itemSyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU2_DAY); // 撹拌終了日②
        itemSyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU2_TIME); // 撹拌終了時間②
        if (itemKaisiDay != null && itemKaisiTime != null && itemSyuuryouDay != null && itemSyuuryouTime != null) {
            kaisiDate = DateUtil.convertStringToDate(itemKaisiDay.getValue(), itemKaisiTime.getValue());
            syuuryouDate = DateUtil.convertStringToDate(itemSyuuryouDay.getValue(), itemSyuuryouTime.getValue());
            //R001チェック呼出し
            msgCheckR001 = validateUtil.checkR001("撹拌開始日時②", kaisiDate, "撹拌終了日時②", syuuryouDate);
            if (!StringUtil.isEmpty(msgCheckR001)) {
                //エラー発生時
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemKaisiDay, itemKaisiTime, itemSyuuryouDay, itemSyuuryouTime);
                return MessageUtil.getErrorMessageInfo("", msgCheckR001, true, true, errFxhdd01List);
            }
        }

        // 撹拌開始日時③、撹拌終了日時③前後チェック
        itemKaisiDay = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI3_DAY); //　撹拌開始日③
        itemKaisiTime = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI3_TIME); // 撹拌開始時間③
        itemSyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU3_DAY); // 撹拌終了日③
        itemSyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU3_TIME); // 撹拌終了時間③
        if (itemKaisiDay != null && itemKaisiTime != null && itemSyuuryouDay != null && itemSyuuryouTime != null) {
            kaisiDate = DateUtil.convertStringToDate(itemKaisiDay.getValue(), itemKaisiTime.getValue());
            syuuryouDate = DateUtil.convertStringToDate(itemSyuuryouDay.getValue(), itemSyuuryouTime.getValue());
            //R001チェック呼出し
            msgCheckR001 = validateUtil.checkR001("撹拌開始日時③", kaisiDate, "撹拌終了日時③", syuuryouDate);
            if (!StringUtil.isEmpty(msgCheckR001)) {
                //エラー発生時
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemKaisiDay, itemKaisiTime, itemSyuuryouDay, itemSyuuryouTime);
                return MessageUtil.getErrorMessageInfo("", msgCheckR001, true, true, errFxhdd01List);
            }
        }

        // 撹拌開始日時④、撹拌終了日時④前後チェック
        itemKaisiDay = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI4_DAY); //　撹拌開始日④
        itemKaisiTime = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI4_TIME); // 撹拌開始時間④
        itemSyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU4_DAY); // 撹拌終了日④
        itemSyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU4_TIME); // 撹拌終了時間④
        if (itemKaisiDay != null && itemKaisiTime != null && itemSyuuryouDay != null && itemSyuuryouTime != null) {
            kaisiDate = DateUtil.convertStringToDate(itemKaisiDay.getValue(), itemKaisiTime.getValue());
            syuuryouDate = DateUtil.convertStringToDate(itemSyuuryouDay.getValue(), itemSyuuryouTime.getValue());
            //R001チェック呼出し
            msgCheckR001 = validateUtil.checkR001("撹拌開始日時④", kaisiDate, "撹拌終了日時④", syuuryouDate);
            if (!StringUtil.isEmpty(msgCheckR001)) {
                //エラー発生時
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemKaisiDay, itemKaisiTime, itemSyuuryouDay, itemSyuuryouTime);
                return MessageUtil.getErrorMessageInfo("", msgCheckR001, true, true, errFxhdd01List);
            }
        }

        // 撹拌開始日時⑤、撹拌終了日時⑤前後チェック
        itemKaisiDay = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI5_DAY); //　撹拌開始日⑤
        itemKaisiTime = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI5_TIME); // 撹拌開始時間⑤
        itemSyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU5_DAY); // 撹拌終了日⑤
        itemSyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU5_TIME); // 撹拌終了時間⑤
        if (itemKaisiDay != null && itemKaisiTime != null && itemSyuuryouDay != null && itemSyuuryouTime != null) {
            kaisiDate = DateUtil.convertStringToDate(itemKaisiDay.getValue(), itemKaisiTime.getValue());
            syuuryouDate = DateUtil.convertStringToDate(itemSyuuryouDay.getValue(), itemSyuuryouTime.getValue());
            //R001チェック呼出し
            msgCheckR001 = validateUtil.checkR001("撹拌開始日時⑤", kaisiDate, "撹拌終了日時⑤", syuuryouDate);
            if (!StringUtil.isEmpty(msgCheckR001)) {
                //エラー発生時
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemKaisiDay, itemKaisiTime, itemSyuuryouDay, itemSyuuryouTime);
                return MessageUtil.getErrorMessageInfo("", msgCheckR001, true, true, errFxhdd01List);
            }
        }

        // 撹拌開始日時⑥、撹拌終了日時⑥前後チェック
        itemKaisiDay = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI6_DAY); //　撹拌開始日⑥
        itemKaisiTime = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI6_TIME); // 撹拌開始時間⑥
        itemSyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU6_DAY); // 撹拌終了日⑥
        itemSyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU6_TIME); // 撹拌終了時間⑥
        if (itemKaisiDay != null && itemKaisiTime != null && itemSyuuryouDay != null && itemSyuuryouTime != null) {
            kaisiDate = DateUtil.convertStringToDate(itemKaisiDay.getValue(), itemKaisiTime.getValue());
            syuuryouDate = DateUtil.convertStringToDate(itemSyuuryouDay.getValue(), itemSyuuryouTime.getValue());
            //R001チェック呼出し
            msgCheckR001 = validateUtil.checkR001("撹拌開始日時⑥", kaisiDate, "撹拌終了日時⑥", syuuryouDate);
            if (!StringUtil.isEmpty(msgCheckR001)) {
                //エラー発生時
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemKaisiDay, itemKaisiTime, itemSyuuryouDay, itemSyuuryouTime);
                return MessageUtil.getErrorMessageInfo("", msgCheckR001, true, true, errFxhdd01List);
            }
        }

        // 撹拌開始日時⑦、撹拌終了日時⑦前後チェック
        itemKaisiDay = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI7_DAY); //　撹拌開始日⑦
        itemKaisiTime = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI7_TIME); // 撹拌開始時間⑦
        itemSyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU7_DAY); // 撹拌終了日⑦
        itemSyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU7_TIME); // 撹拌終了時間⑦
        if (itemKaisiDay != null && itemKaisiTime != null && itemSyuuryouDay != null && itemSyuuryouTime != null) {
            kaisiDate = DateUtil.convertStringToDate(itemKaisiDay.getValue(), itemKaisiTime.getValue());
            syuuryouDate = DateUtil.convertStringToDate(itemSyuuryouDay.getValue(), itemSyuuryouTime.getValue());
            //R001チェック呼出し
            msgCheckR001 = validateUtil.checkR001("撹拌開始日時⑦", kaisiDate, "撹拌終了日時⑦", syuuryouDate);
            if (!StringUtil.isEmpty(msgCheckR001)) {
                //エラー発生時
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemKaisiDay, itemKaisiTime, itemSyuuryouDay, itemSyuuryouTime);
                return MessageUtil.getErrorMessageInfo("", msgCheckR001, true, true, errFxhdd01List);
            }
        }
        // ﾁｪｯｸﾎﾞｯｸｽがすべてﾁｪｯｸされているかﾁｪｯｸ：溶剤投入、自動運転、ﾀﾝｸAB循環、ｱｰｽｸﾞﾘｯﾌﾟ接続確認、回転体への接触の確認
        List<String> itemIdList = Arrays.asList(GXHDO102B023Const.YOUZAITOUNYUU, GXHDO102B023Const.JIDOUUNTEN, GXHDO102B023Const.TANKABJYUNKAN, GXHDO102B023Const.EARTHGRIPSETUZOKUKAKUNIN, GXHDO102B023Const.KAITENTAIHESESYOKUKAKUNIN);
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
            SrYuudentaiPremixing tmpSrYuudentaiPremixing = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrYuudentaiPremixing> srYuudentaiPremixingList = getSrYuudentaiPremixingData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo9, edaban);
                if (!srYuudentaiPremixingList.isEmpty()) {
                    tmpSrYuudentaiPremixing = srYuudentaiPremixingList.get(0);
                }

                deleteTmpSrYuudentaiPremixing(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);
            }

            // 誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞ_登録処理
            insertSrYuudentaiPremixing(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, strSystime, processData, tmpSrYuudentaiPremixing);
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
        processData.setUserAuthParam(GXHDO102B023Const.USER_AUTH_UPDATE_PARAM);

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

            // 誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞ_更新処理
            updateSrYuudentaiPremixing(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);

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
        processData.setUserAuthParam(GXHDO102B023Const.USER_AUTH_DELETE_PARAM);

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

            // 誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞ_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo9, edaban, paramJissekino);
            insertDeleteDataTmpSrYuudentaiPremixing(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo9, edaban, strSystime);

            // 誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞ_削除処理
            deleteSrYuudentaiPremixing(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);

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
     * 投入開始日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setTounyuukaisiDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B023Const.TOUNYUUKAISI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B023Const.TOUNYUUKAISI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 投入終了日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setTounyuusyuuryouDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B023Const.TOUNYUUSYUURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B023Const.TOUNYUUSYUURYOU_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 撹拌開始日時①設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKakuhankaisi1DateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI1_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI1_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 撹拌終了日時①設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKakuhansyuuryou1DateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU1_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU1_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 撹拌開始日時②設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKakuhankaisi2DateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI2_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI2_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 撹拌終了日時②設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKakuhansyuuryou2DateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU2_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU2_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 撹拌開始日時③設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKakuhankaisi3DateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI3_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI3_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 撹拌終了日時③設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKakuhansyuuryou3DateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU3_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU3_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 撹拌開始日時④設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKakuhankaisi4DateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI4_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI4_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 撹拌終了日時④設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKakuhansyuuryou4DateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU4_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU4_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 撹拌開始日時⑤設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKakuhankaisi5DateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI5_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI5_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 撹拌終了日時⑤設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKakuhansyuuryou5DateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU5_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU5_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 撹拌開始日時⑥設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKakuhankaisi6DateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI6_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI6_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 撹拌終了日時⑥設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKakuhansyuuryou6DateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU6_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU6_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 撹拌開始日時⑦設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKakuhankaisi7DateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI7_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI7_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 撹拌終了日時⑦設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKakuhansyuuryou7DateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU7_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU7_TIME);
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
                activeIdList.addAll(Arrays.asList(GXHDO102B023Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B023Const.BTN_TOUNYUUKAISI_TOP,
                        GXHDO102B023Const.BTN_KAKUHANKAISI1_TOP,
                        GXHDO102B023Const.BTN_KAKUHANSYUURYOU1_TOP,
                        GXHDO102B023Const.BTN_KAKUHANKAISI2_TOP,
                        GXHDO102B023Const.BTN_KAKUHANSYUURYOU2_TOP,
                        GXHDO102B023Const.BTN_KAKUHANKAISI3_TOP,
                        GXHDO102B023Const.BTN_KAKUHANSYUURYOU3_TOP,
                        GXHDO102B023Const.BTN_KAKUHANKAISI4_TOP,
                        GXHDO102B023Const.BTN_KAKUHANSYUURYOU4_TOP,
                        GXHDO102B023Const.BTN_KAKUHANKAISI5_TOP,
                        GXHDO102B023Const.BTN_KAKUHANSYUURYOU5_TOP,
                        GXHDO102B023Const.BTN_KAKUHANKAISI6_TOP,
                        GXHDO102B023Const.BTN_KAKUHANSYUURYOU6_TOP,
                        GXHDO102B023Const.BTN_KAKUHANKAISI7_TOP,
                        GXHDO102B023Const.BTN_KAKUHANSYUURYOU7_TOP,
                        GXHDO102B023Const.BTN_TOUNYUUSYUURYOU_TOP,
                        GXHDO102B023Const.BTN_UPDATE_TOP,
                        GXHDO102B023Const.BTN_DELETE_TOP,
                        GXHDO102B023Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B023Const.BTN_TOUNYUUKAISI_BOTTOM,
                        GXHDO102B023Const.BTN_KAKUHANKAISI1_BOTTOM,
                        GXHDO102B023Const.BTN_KAKUHANSYUURYOU1_BOTTOM,
                        GXHDO102B023Const.BTN_KAKUHANKAISI2_BOTTOM,
                        GXHDO102B023Const.BTN_KAKUHANSYUURYOU2_BOTTOM,
                        GXHDO102B023Const.BTN_KAKUHANKAISI3_BOTTOM,
                        GXHDO102B023Const.BTN_KAKUHANSYUURYOU3_BOTTOM,
                        GXHDO102B023Const.BTN_KAKUHANKAISI4_BOTTOM,
                        GXHDO102B023Const.BTN_KAKUHANSYUURYOU4_BOTTOM,
                        GXHDO102B023Const.BTN_KAKUHANKAISI5_BOTTOM,
                        GXHDO102B023Const.BTN_KAKUHANSYUURYOU5_BOTTOM,
                        GXHDO102B023Const.BTN_KAKUHANKAISI6_BOTTOM,
                        GXHDO102B023Const.BTN_KAKUHANSYUURYOU6_BOTTOM,
                        GXHDO102B023Const.BTN_KAKUHANKAISI7_BOTTOM,
                        GXHDO102B023Const.BTN_KAKUHANSYUURYOU7_BOTTOM,
                        GXHDO102B023Const.BTN_TOUNYUUSYUURYOU_BOTTOM,
                        GXHDO102B023Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B023Const.BTN_DELETE_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B023Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B023Const.BTN_INSERT_TOP,
                        GXHDO102B023Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B023Const.BTN_INSERT_BOTTOM));

                break;
            default:
                activeIdList.addAll(Arrays.asList(GXHDO102B023Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B023Const.BTN_TOUNYUUKAISI_TOP,
                        GXHDO102B023Const.BTN_KAKUHANKAISI1_TOP,
                        GXHDO102B023Const.BTN_KAKUHANSYUURYOU1_TOP,
                        GXHDO102B023Const.BTN_KAKUHANKAISI2_TOP,
                        GXHDO102B023Const.BTN_KAKUHANSYUURYOU2_TOP,
                        GXHDO102B023Const.BTN_KAKUHANKAISI3_TOP,
                        GXHDO102B023Const.BTN_KAKUHANSYUURYOU3_TOP,
                        GXHDO102B023Const.BTN_KAKUHANKAISI4_TOP,
                        GXHDO102B023Const.BTN_KAKUHANSYUURYOU4_TOP,
                        GXHDO102B023Const.BTN_KAKUHANKAISI5_TOP,
                        GXHDO102B023Const.BTN_KAKUHANSYUURYOU5_TOP,
                        GXHDO102B023Const.BTN_KAKUHANKAISI6_TOP,
                        GXHDO102B023Const.BTN_KAKUHANSYUURYOU6_TOP,
                        GXHDO102B023Const.BTN_KAKUHANKAISI7_TOP,
                        GXHDO102B023Const.BTN_KAKUHANSYUURYOU7_TOP,
                        GXHDO102B023Const.BTN_TOUNYUUSYUURYOU_TOP,
                        GXHDO102B023Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B023Const.BTN_INSERT_TOP,
                        GXHDO102B023Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B023Const.BTN_TOUNYUUKAISI_BOTTOM,
                        GXHDO102B023Const.BTN_KAKUHANKAISI1_BOTTOM,
                        GXHDO102B023Const.BTN_KAKUHANSYUURYOU1_BOTTOM,
                        GXHDO102B023Const.BTN_KAKUHANKAISI2_BOTTOM,
                        GXHDO102B023Const.BTN_KAKUHANSYUURYOU2_BOTTOM,
                        GXHDO102B023Const.BTN_KAKUHANKAISI3_BOTTOM,
                        GXHDO102B023Const.BTN_KAKUHANSYUURYOU3_BOTTOM,
                        GXHDO102B023Const.BTN_KAKUHANKAISI4_BOTTOM,
                        GXHDO102B023Const.BTN_KAKUHANSYUURYOU4_BOTTOM,
                        GXHDO102B023Const.BTN_KAKUHANKAISI5_BOTTOM,
                        GXHDO102B023Const.BTN_KAKUHANSYUURYOU5_BOTTOM,
                        GXHDO102B023Const.BTN_KAKUHANKAISI6_BOTTOM,
                        GXHDO102B023Const.BTN_KAKUHANSYUURYOU6_BOTTOM,
                        GXHDO102B023Const.BTN_KAKUHANKAISI7_BOTTOM,
                        GXHDO102B023Const.BTN_KAKUHANSYUURYOU7_BOTTOM,
                        GXHDO102B023Const.BTN_TOUNYUUSYUURYOU_BOTTOM,
                        GXHDO102B023Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B023Const.BTN_INSERT_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B023Const.BTN_UPDATE_TOP,
                        GXHDO102B023Const.BTN_DELETE_TOP,
                        GXHDO102B023Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B023Const.BTN_DELETE_BOTTOM
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
     * @param sekkeiData 設計データ
     * @param ownerMasData ｵｰﾅｰﾏｽﾀデータ
     * @param shikakariData 仕掛データ
     * @param lotNo ﾛｯﾄNo
     */
    private void setViewItemData(ProcessData processData, Map shikakariData, String lotNo) {

        // WIPﾛｯﾄNo
        this.setItemData(processData, GXHDO102B023Const.WIPLOTNO, lotNo);
        // 誘電体ｽﾗﾘｰ品名
        this.setItemData(processData, GXHDO102B023Const.YUUDENTAISLURRYHINMEI, StringUtil.nullToBlank(getMapData(shikakariData, "hinmei")));
        // 誘電体ｽﾗﾘｰLotNo
        this.setItemData(processData, GXHDO102B023Const.YUUDENTAISLURRYLOTNO, StringUtil.nullToBlank(getMapData(shikakariData, "lotno")));
        // ﾛｯﾄ区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode"));
        // ﾛｯﾄ区分名称
        String lotkubun = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubun"));

        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO102B023Const.LOTKUBUN, "");
        } else {
            if (!StringUtil.isEmpty(lotkubun)) {
                lotkubuncode = lotkubuncode + ":" + lotkubun;
            }
            this.setItemData(processData, GXHDO102B023Const.LOTKUBUN, lotkubuncode);
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

        List<SrYuudentaiPremixing> srYuudentaiPremixingList = new ArrayList<>();
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

            // 誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞデータ取得
            srYuudentaiPremixingList = getSrYuudentaiPremixingData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo9, edaban);
            if (srYuudentaiPremixingList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srYuudentaiPremixingList.isEmpty()) {
            return false;
        }
        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srYuudentaiPremixingList.get(0));
        return true;

    }

    /**
     * データ設定処理
     *
     * @param processData 処理制御データ
     * @param srYuudentaiPremixing 誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrYuudentaiPremixing srYuudentaiPremixing) {

        // 溶剤投入
        this.setItemData(processData, GXHDO102B023Const.YOUZAITOUNYUU, getSrYuudentaiPremixingItemData(GXHDO102B023Const.YOUZAITOUNYUU, srYuudentaiPremixing));

        // 自動運転
        this.setItemData(processData, GXHDO102B023Const.JIDOUUNTEN, getSrYuudentaiPremixingItemData(GXHDO102B023Const.JIDOUUNTEN, srYuudentaiPremixing));

        // ﾀﾝｸAB循環
        this.setItemData(processData, GXHDO102B023Const.TANKABJYUNKAN, getSrYuudentaiPremixingItemData(GXHDO102B023Const.TANKABJYUNKAN, srYuudentaiPremixing));

        // ﾗｯﾌﾟﾀｲﾑ
        this.setItemData(processData, GXHDO102B023Const.RAPTIME, getSrYuudentaiPremixingItemData(GXHDO102B023Const.RAPTIME, srYuudentaiPremixing));

        // ｱｰｽｸﾞﾘｯﾌﾟ接続確認
        this.setItemData(processData, GXHDO102B023Const.EARTHGRIPSETUZOKUKAKUNIN, getSrYuudentaiPremixingItemData(GXHDO102B023Const.EARTHGRIPSETUZOKUKAKUNIN, srYuudentaiPremixing));

        // 投入開始日
        this.setItemData(processData, GXHDO102B023Const.TOUNYUUKAISI_DAY, getSrYuudentaiPremixingItemData(GXHDO102B023Const.TOUNYUUKAISI_DAY, srYuudentaiPremixing));

        // 投入開始時間
        this.setItemData(processData, GXHDO102B023Const.TOUNYUUKAISI_TIME, getSrYuudentaiPremixingItemData(GXHDO102B023Const.TOUNYUUKAISI_TIME, srYuudentaiPremixing));

        // 撹拌開始日①
        this.setItemData(processData, GXHDO102B023Const.KAKUHANKAISI1_DAY, getSrYuudentaiPremixingItemData(GXHDO102B023Const.KAKUHANKAISI1_DAY, srYuudentaiPremixing));

        // 撹拌開始時間①
        this.setItemData(processData, GXHDO102B023Const.KAKUHANKAISI1_TIME, getSrYuudentaiPremixingItemData(GXHDO102B023Const.KAKUHANKAISI1_TIME, srYuudentaiPremixing));

        // 撹拌終了日①
        this.setItemData(processData, GXHDO102B023Const.KAKUHANSYUURYOU1_DAY, getSrYuudentaiPremixingItemData(GXHDO102B023Const.KAKUHANSYUURYOU1_DAY, srYuudentaiPremixing));

        // 撹拌終了時間①
        this.setItemData(processData, GXHDO102B023Const.KAKUHANSYUURYOU1_TIME, getSrYuudentaiPremixingItemData(GXHDO102B023Const.KAKUHANSYUURYOU1_TIME, srYuudentaiPremixing));

        // 撹拌開始日②
        this.setItemData(processData, GXHDO102B023Const.KAKUHANKAISI2_DAY, getSrYuudentaiPremixingItemData(GXHDO102B023Const.KAKUHANKAISI2_DAY, srYuudentaiPremixing));

        // 撹拌開始時間②
        this.setItemData(processData, GXHDO102B023Const.KAKUHANKAISI2_TIME, getSrYuudentaiPremixingItemData(GXHDO102B023Const.KAKUHANKAISI2_TIME, srYuudentaiPremixing));

        // 撹拌終了日②
        this.setItemData(processData, GXHDO102B023Const.KAKUHANSYUURYOU2_DAY, getSrYuudentaiPremixingItemData(GXHDO102B023Const.KAKUHANSYUURYOU2_DAY, srYuudentaiPremixing));

        // 撹拌終了時間②
        this.setItemData(processData, GXHDO102B023Const.KAKUHANSYUURYOU2_TIME, getSrYuudentaiPremixingItemData(GXHDO102B023Const.KAKUHANSYUURYOU2_TIME, srYuudentaiPremixing));

        // 撹拌開始日③
        this.setItemData(processData, GXHDO102B023Const.KAKUHANKAISI3_DAY, getSrYuudentaiPremixingItemData(GXHDO102B023Const.KAKUHANKAISI3_DAY, srYuudentaiPremixing));

        // 撹拌開始時間③
        this.setItemData(processData, GXHDO102B023Const.KAKUHANKAISI3_TIME, getSrYuudentaiPremixingItemData(GXHDO102B023Const.KAKUHANKAISI3_TIME, srYuudentaiPremixing));

        // 撹拌終了日③
        this.setItemData(processData, GXHDO102B023Const.KAKUHANSYUURYOU3_DAY, getSrYuudentaiPremixingItemData(GXHDO102B023Const.KAKUHANSYUURYOU3_DAY, srYuudentaiPremixing));

        // 撹拌終了時間③
        this.setItemData(processData, GXHDO102B023Const.KAKUHANSYUURYOU3_TIME, getSrYuudentaiPremixingItemData(GXHDO102B023Const.KAKUHANSYUURYOU3_TIME, srYuudentaiPremixing));

        // 撹拌開始日④
        this.setItemData(processData, GXHDO102B023Const.KAKUHANKAISI4_DAY, getSrYuudentaiPremixingItemData(GXHDO102B023Const.KAKUHANKAISI4_DAY, srYuudentaiPremixing));

        // 撹拌開始時間④
        this.setItemData(processData, GXHDO102B023Const.KAKUHANKAISI4_TIME, getSrYuudentaiPremixingItemData(GXHDO102B023Const.KAKUHANKAISI4_TIME, srYuudentaiPremixing));

        // 撹拌終了日④
        this.setItemData(processData, GXHDO102B023Const.KAKUHANSYUURYOU4_DAY, getSrYuudentaiPremixingItemData(GXHDO102B023Const.KAKUHANSYUURYOU4_DAY, srYuudentaiPremixing));

        // 撹拌終了時間④
        this.setItemData(processData, GXHDO102B023Const.KAKUHANSYUURYOU4_TIME, getSrYuudentaiPremixingItemData(GXHDO102B023Const.KAKUHANSYUURYOU4_TIME, srYuudentaiPremixing));

        // 撹拌開始日⑤
        this.setItemData(processData, GXHDO102B023Const.KAKUHANKAISI5_DAY, getSrYuudentaiPremixingItemData(GXHDO102B023Const.KAKUHANKAISI5_DAY, srYuudentaiPremixing));

        // 撹拌開始時間⑤
        this.setItemData(processData, GXHDO102B023Const.KAKUHANKAISI5_TIME, getSrYuudentaiPremixingItemData(GXHDO102B023Const.KAKUHANKAISI5_TIME, srYuudentaiPremixing));

        // 撹拌終了日⑤
        this.setItemData(processData, GXHDO102B023Const.KAKUHANSYUURYOU5_DAY, getSrYuudentaiPremixingItemData(GXHDO102B023Const.KAKUHANSYUURYOU5_DAY, srYuudentaiPremixing));

        // 撹拌終了時間⑤
        this.setItemData(processData, GXHDO102B023Const.KAKUHANSYUURYOU5_TIME, getSrYuudentaiPremixingItemData(GXHDO102B023Const.KAKUHANSYUURYOU5_TIME, srYuudentaiPremixing));

        // 撹拌開始日⑥
        this.setItemData(processData, GXHDO102B023Const.KAKUHANKAISI6_DAY, getSrYuudentaiPremixingItemData(GXHDO102B023Const.KAKUHANKAISI6_DAY, srYuudentaiPremixing));

        // 撹拌開始時間⑥
        this.setItemData(processData, GXHDO102B023Const.KAKUHANKAISI6_TIME, getSrYuudentaiPremixingItemData(GXHDO102B023Const.KAKUHANKAISI6_TIME, srYuudentaiPremixing));

        // 撹拌終了日⑥
        this.setItemData(processData, GXHDO102B023Const.KAKUHANSYUURYOU6_DAY, getSrYuudentaiPremixingItemData(GXHDO102B023Const.KAKUHANSYUURYOU6_DAY, srYuudentaiPremixing));

        // 撹拌終了時間⑥
        this.setItemData(processData, GXHDO102B023Const.KAKUHANSYUURYOU6_TIME, getSrYuudentaiPremixingItemData(GXHDO102B023Const.KAKUHANSYUURYOU6_TIME, srYuudentaiPremixing));

        // 投入終了日
        this.setItemData(processData, GXHDO102B023Const.TOUNYUUSYUURYOU_DAY, getSrYuudentaiPremixingItemData(GXHDO102B023Const.TOUNYUUSYUURYOU_DAY, srYuudentaiPremixing));

        // 投入終了時間
        this.setItemData(processData, GXHDO102B023Const.TOUNYUUSYUURYOU_TIME, getSrYuudentaiPremixingItemData(GXHDO102B023Const.TOUNYUUSYUURYOU_TIME, srYuudentaiPremixing));

        // 撹拌開始日⑦
        this.setItemData(processData, GXHDO102B023Const.KAKUHANKAISI7_DAY, getSrYuudentaiPremixingItemData(GXHDO102B023Const.KAKUHANKAISI7_DAY, srYuudentaiPremixing));

        // 撹拌開始時間⑦
        this.setItemData(processData, GXHDO102B023Const.KAKUHANKAISI7_TIME, getSrYuudentaiPremixingItemData(GXHDO102B023Const.KAKUHANKAISI7_TIME, srYuudentaiPremixing));

        // 回転体への接触の確認
        this.setItemData(processData, GXHDO102B023Const.KAITENTAIHESESYOKUKAKUNIN, getSrYuudentaiPremixingItemData(GXHDO102B023Const.KAITENTAIHESESYOKUKAKUNIN, srYuudentaiPremixing));

        // 撹拌終了日⑦
        this.setItemData(processData, GXHDO102B023Const.KAKUHANSYUURYOU7_DAY, getSrYuudentaiPremixingItemData(GXHDO102B023Const.KAKUHANSYUURYOU7_DAY, srYuudentaiPremixing));

        // 撹拌終了時間⑦
        this.setItemData(processData, GXHDO102B023Const.KAKUHANSYUURYOU7_TIME, getSrYuudentaiPremixingItemData(GXHDO102B023Const.KAKUHANSYUURYOU7_TIME, srYuudentaiPremixing));

        // 担当者
        this.setItemData(processData, GXHDO102B023Const.TANTOUSYA, getSrYuudentaiPremixingItemData(GXHDO102B023Const.TANTOUSYA, srYuudentaiPremixing));

        // 備考1
        this.setItemData(processData, GXHDO102B023Const.BIKOU1, getSrYuudentaiPremixingItemData(GXHDO102B023Const.BIKOU1, srYuudentaiPremixing));

        // 備考2
        this.setItemData(processData, GXHDO102B023Const.BIKOU2, getSrYuudentaiPremixingItemData(GXHDO102B023Const.BIKOU2, srYuudentaiPremixing));

    }

    /**
     * 誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞの入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return 誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞデータ
     * @throws SQLException 例外エラー
     */
    private List<SrYuudentaiPremixing> getSrYuudentaiPremixingData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrYuudentaiPremixing(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSrYuudentaiPremixing(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
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
     * [誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞ]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrYuudentaiPremixing> loadSrYuudentaiPremixing(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + " kojyo,lotno,edaban,yuudentaislurryhinmei,yuudentaislurrylotno,lotkubun,genryoulotno,genryoukigou,youzaisenjyou_kakuhanki,"
                + "youzaisenjyou_siyoutank,youzairyou,youzaitounyuu,syuziku,pump,senjyoujyouken_dispakaitensuu,separate,agitator,"
                + "jidouunten,tankABjyunkan,passsuu,senjyoujikan,raptime,premixing_kakuhanki,premixing_siyoutank,earthgripsetuzokukakunin,"
                + "premixing_dispakaitensuu,tounyuukaisinichiji,tounyuu1,tounyuu2,kakuhanjikankikaku1,kakuhankaisinichiji1,kakuhansyuuryounichiji1"
                + ",tounyuu3,kakuhanjikankikaku2,kakuhankaisinichiji2,kakuhansyuuryounichiji2,tounyuu4,kakuhanjikankikaku3,kakuhankaisinichiji3,"
                + "kakuhansyuuryounichiji3,tounyuu5,kakuhanjikankikaku4,kakuhankaisinichiji4,kakuhansyuuryounichiji4,tounyuu6,kakuhanjikankikaku5,"
                + "kakuhankaisinichiji5,kakuhansyuuryounichiji5,kaitensuuhenkou,tounyuu7,tounyuu8,tounyuu9,kakuhanjikankikaku6,kakuhankaisinichiji6,"
                + "kakuhansyuuryounichiji6,tounyuu10,tounyuu11,tounyuu12,tounyuu13,tounyuu14,tounyuu15,tounyuu16,tounyuu17,tounyuu18,tounyuu19,"
                + "tounyuusyuuryounichiji,kakuhanki,kaitensuu,kakuhanjikankikaku7,kakuhankaisinichiji7,kaitentaihenosessyokunokakunin,kakuhansyuuryounichiji7,"
                + "tantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + " FROM sr_yuudentai_premixing "
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
        mapping.put("kojyo", "kojyo");                                                    // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                                    // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                                  // 枝番
        mapping.put("yuudentaislurryhinmei", "yuudentaislurryhinmei");                    // 誘電体ｽﾗﾘｰ品名
        mapping.put("yuudentaislurrylotno", "yuudentaislurrylotno");                      // 誘電体ｽﾗﾘｰLotNo
        mapping.put("lotkubun", "lotkubun");                                              // ﾛｯﾄ区分
        mapping.put("genryoulotno", "genryoulotno");                                      // 原料LotNo
        mapping.put("genryoukigou", "genryoukigou");                                      // 原料記号
        mapping.put("youzaisenjyou_kakuhanki", "youzaisenjyou_kakuhanki");                // 溶剤洗浄_撹拌機
        mapping.put("youzaisenjyou_siyoutank", "youzaisenjyou_siyoutank");                // 溶剤洗浄_使用ﾀﾝｸ
        mapping.put("youzairyou", "youzairyou");                                          // 溶剤量
        mapping.put("youzaitounyuu", "youzaitounyuu");                                    // 溶剤投入
        mapping.put("syuziku", "syuziku");                                                // 主軸
        mapping.put("pump", "pump");                                                      // ﾎﾟﾝﾌﾟ
        mapping.put("senjyoujyouken_dispakaitensuu", "senjyoujyouken_dispakaitensuu");    // 洗浄条件_ﾃﾞｨｽﾊﾟ回転数
        mapping.put("separate", "separate");                                              // ｾﾊﾟﾚｰﾀ
        mapping.put("agitator", "agitator");                                              // ｱｼﾞﾃｰﾀ
        mapping.put("jidouunten", "jidouunten");                                          // 自動運転
        mapping.put("tankABjyunkan", "tankABjyunkan");                                    // ﾀﾝｸAB循環
        mapping.put("passsuu", "passsuu");                                                // ﾊﾟｽ数
        mapping.put("senjyoujikan", "senjyoujikan");                                      // 洗浄時間
        mapping.put("raptime", "raptime");                                                // ﾗｯﾌﾟﾀｲﾑ
        mapping.put("premixing_kakuhanki", "premixing_kakuhanki");                        // ﾌﾟﾚﾐｷ_撹拌機
        mapping.put("premixing_siyoutank", "premixing_siyoutank");                        // ﾌﾟﾚﾐｷ_使用ﾀﾝｸ
        mapping.put("earthgripsetuzokukakunin", "earthgripsetuzokukakunin");              // ｱｰｽｸﾞﾘｯﾌﾟ接続確認
        mapping.put("premixing_dispakaitensuu", "premixing_dispakaitensuu");              // ﾌﾟﾚﾐｷ_ﾃﾞｨｽﾊﾟ回転数
        mapping.put("tounyuukaisinichiji", "tounyuukaisinichiji");                        // 投入開始日時
        mapping.put("tounyuu1", "tounyuu1");                                              // 投入①
        mapping.put("tounyuu2", "tounyuu2");                                              // 投入②
        mapping.put("kakuhanjikankikaku1", "kakuhanjikankikaku1");                        // 撹拌時間規格①
        mapping.put("kakuhankaisinichiji1", "kakuhankaisinichiji1");                      // 撹拌開始日時①
        mapping.put("kakuhansyuuryounichiji1", "kakuhansyuuryounichiji1");                // 撹拌終了日時①
        mapping.put("tounyuu3", "tounyuu3");                                              // 投入③
        mapping.put("kakuhanjikankikaku2", "kakuhanjikankikaku2");                        // 撹拌時間規格②
        mapping.put("kakuhankaisinichiji2", "kakuhankaisinichiji2");                      // 撹拌開始日時②
        mapping.put("kakuhansyuuryounichiji2", "kakuhansyuuryounichiji2");                // 撹拌終了日時②
        mapping.put("tounyuu4", "tounyuu4");                                              // 投入④
        mapping.put("kakuhanjikankikaku3", "kakuhanjikankikaku3");                        // 撹拌時間規格③
        mapping.put("kakuhankaisinichiji3", "kakuhankaisinichiji3");                      // 撹拌開始日時③
        mapping.put("kakuhansyuuryounichiji3", "kakuhansyuuryounichiji3");                // 撹拌終了日時③
        mapping.put("tounyuu5", "tounyuu5");                                              // 投入⑤
        mapping.put("kakuhanjikankikaku4", "kakuhanjikankikaku4");                        // 撹拌時間規格④
        mapping.put("kakuhankaisinichiji4", "kakuhankaisinichiji4");                      // 撹拌開始日時④
        mapping.put("kakuhansyuuryounichiji4", "kakuhansyuuryounichiji4");                // 撹拌終了日時④
        mapping.put("tounyuu6", "tounyuu6");                                              // 投入⑥
        mapping.put("kakuhanjikankikaku5", "kakuhanjikankikaku5");                        // 撹拌時間規格⑤
        mapping.put("kakuhankaisinichiji5", "kakuhankaisinichiji5");                      // 撹拌開始日時⑤
        mapping.put("kakuhansyuuryounichiji5", "kakuhansyuuryounichiji5");                // 撹拌終了日時⑤
        mapping.put("kaitensuuhenkou", "kaitensuuhenkou");                                // 回転数変更
        mapping.put("tounyuu7", "tounyuu7");                                              // 投入⑦
        mapping.put("tounyuu8", "tounyuu8");                                              // 投入⑧
        mapping.put("tounyuu9", "tounyuu9");                                              // 投入⑨
        mapping.put("kakuhanjikankikaku6", "kakuhanjikankikaku6");                        // 撹拌時間規格⑥
        mapping.put("kakuhankaisinichiji6", "kakuhankaisinichiji6");                      // 撹拌開始日時⑥
        mapping.put("kakuhansyuuryounichiji6", "kakuhansyuuryounichiji6");                // 撹拌終了日時⑥
        mapping.put("tounyuu10", "tounyuu10");                                            // 投入⑩
        mapping.put("tounyuu11", "tounyuu11");                                            // 投入⑪
        mapping.put("tounyuu12", "tounyuu12");                                            // 投入⑫
        mapping.put("tounyuu13", "tounyuu13");                                            // 投入⑬
        mapping.put("tounyuu14", "tounyuu14");                                            // 投入⑭
        mapping.put("tounyuu15", "tounyuu15");                                            // 投入⑮
        mapping.put("tounyuu16", "tounyuu16");                                            // 投入⑯
        mapping.put("tounyuu17", "tounyuu17");                                            // 投入⑰
        mapping.put("tounyuu18", "tounyuu18");                                            // 投入⑱
        mapping.put("tounyuu19", "tounyuu19");                                            // 投入⑲
        mapping.put("tounyuusyuuryounichiji", "tounyuusyuuryounichiji");                  // 投入終了日時
        mapping.put("kakuhanki", "kakuhanki");                                            // 撹拌機
        mapping.put("kaitensuu", "kaitensuu");                                            // 回転数
        mapping.put("kakuhanjikankikaku7", "kakuhanjikankikaku7");                        // 撹拌時間規格⑦
        mapping.put("kakuhankaisinichiji7", "kakuhankaisinichiji7");                      // 撹拌開始日時⑦
        mapping.put("kaitentaihenosessyokunokakunin", "kaitentaihenosessyokunokakunin");  // 回転体への接触の確認
        mapping.put("kakuhansyuuryounichiji7", "kakuhansyuuryounichiji7");                // 撹拌終了日時⑦
        mapping.put("tantousya", "tantousya");                                            // 担当者
        mapping.put("bikou1", "bikou1");                                                  // 備考1
        mapping.put("bikou2", "bikou2");                                                  // 備考2
        mapping.put("torokunichiji", "torokunichiji");                                    // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                                      // 更新日時
        mapping.put("revision", "revision");                                              // revision

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrYuudentaiPremixing>> beanHandler = new BeanListHandler<>(SrYuudentaiPremixing.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞ_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrYuudentaiPremixing> loadTmpSrYuudentaiPremixing(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + " kojyo,lotno,edaban,yuudentaislurryhinmei,yuudentaislurrylotno,lotkubun,genryoulotno,genryoukigou,youzaisenjyou_kakuhanki,"
                + "youzaisenjyou_siyoutank,youzairyou,youzaitounyuu,syuziku,pump,senjyoujyouken_dispakaitensuu,separate,agitator,"
                + "jidouunten,tankABjyunkan,passsuu,senjyoujikan,raptime,premixing_kakuhanki,premixing_siyoutank,earthgripsetuzokukakunin,"
                + "premixing_dispakaitensuu,tounyuukaisinichiji,tounyuu1,tounyuu2,kakuhanjikankikaku1,kakuhankaisinichiji1,kakuhansyuuryounichiji1"
                + ",tounyuu3,kakuhanjikankikaku2,kakuhankaisinichiji2,kakuhansyuuryounichiji2,tounyuu4,kakuhanjikankikaku3,kakuhankaisinichiji3,"
                + "kakuhansyuuryounichiji3,tounyuu5,kakuhanjikankikaku4,kakuhankaisinichiji4,kakuhansyuuryounichiji4,tounyuu6,kakuhanjikankikaku5,"
                + "kakuhankaisinichiji5,kakuhansyuuryounichiji5,kaitensuuhenkou,tounyuu7,tounyuu8,tounyuu9,kakuhanjikankikaku6,kakuhankaisinichiji6,"
                + "kakuhansyuuryounichiji6,tounyuu10,tounyuu11,tounyuu12,tounyuu13,tounyuu14,tounyuu15,tounyuu16,tounyuu17,tounyuu18,tounyuu19,"
                + "tounyuusyuuryounichiji,kakuhanki,kaitensuu,kakuhanjikankikaku7,kakuhankaisinichiji7,kaitentaihenosessyokunokakunin,kakuhansyuuryounichiji7,"
                + "tantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + " FROM tmp_sr_yuudentai_premixing "
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
        mapping.put("kojyo", "kojyo");                                                    // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                                    // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                                  // 枝番
        mapping.put("yuudentaislurryhinmei", "yuudentaislurryhinmei");                    // 誘電体ｽﾗﾘｰ品名
        mapping.put("yuudentaislurrylotno", "yuudentaislurrylotno");                      // 誘電体ｽﾗﾘｰLotNo
        mapping.put("lotkubun", "lotkubun");                                              // ﾛｯﾄ区分
        mapping.put("genryoulotno", "genryoulotno");                                      // 原料LotNo
        mapping.put("genryoukigou", "genryoukigou");                                      // 原料記号
        mapping.put("youzaisenjyou_kakuhanki", "youzaisenjyou_kakuhanki");                // 溶剤洗浄_撹拌機
        mapping.put("youzaisenjyou_siyoutank", "youzaisenjyou_siyoutank");                // 溶剤洗浄_使用ﾀﾝｸ
        mapping.put("youzairyou", "youzairyou");                                          // 溶剤量
        mapping.put("youzaitounyuu", "youzaitounyuu");                                    // 溶剤投入
        mapping.put("syuziku", "syuziku");                                                // 主軸
        mapping.put("pump", "pump");                                                      // ﾎﾟﾝﾌﾟ
        mapping.put("senjyoujyouken_dispakaitensuu", "senjyoujyouken_dispakaitensuu");    // 洗浄条件_ﾃﾞｨｽﾊﾟ回転数
        mapping.put("separate", "separate");                                              // ｾﾊﾟﾚｰﾀ
        mapping.put("agitator", "agitator");                                              // ｱｼﾞﾃｰﾀ
        mapping.put("jidouunten", "jidouunten");                                          // 自動運転
        mapping.put("tankABjyunkan", "tankABjyunkan");                                    // ﾀﾝｸAB循環
        mapping.put("passsuu", "passsuu");                                                // ﾊﾟｽ数
        mapping.put("senjyoujikan", "senjyoujikan");                                      // 洗浄時間
        mapping.put("raptime", "raptime");                                                // ﾗｯﾌﾟﾀｲﾑ
        mapping.put("premixing_kakuhanki", "premixing_kakuhanki");                        // ﾌﾟﾚﾐｷ_撹拌機
        mapping.put("premixing_siyoutank", "premixing_siyoutank");                        // ﾌﾟﾚﾐｷ_使用ﾀﾝｸ
        mapping.put("earthgripsetuzokukakunin", "earthgripsetuzokukakunin");              // ｱｰｽｸﾞﾘｯﾌﾟ接続確認
        mapping.put("premixing_dispakaitensuu", "premixing_dispakaitensuu");              // ﾌﾟﾚﾐｷ_ﾃﾞｨｽﾊﾟ回転数
        mapping.put("tounyuukaisinichiji", "tounyuukaisinichiji");                        // 投入開始日時
        mapping.put("tounyuu1", "tounyuu1");                                              // 投入①
        mapping.put("tounyuu2", "tounyuu2");                                              // 投入②
        mapping.put("kakuhanjikankikaku1", "kakuhanjikankikaku1");                        // 撹拌時間規格①
        mapping.put("kakuhankaisinichiji1", "kakuhankaisinichiji1");                      // 撹拌開始日時①
        mapping.put("kakuhansyuuryounichiji1", "kakuhansyuuryounichiji1");                // 撹拌終了日時①
        mapping.put("tounyuu3", "tounyuu3");                                              // 投入③
        mapping.put("kakuhanjikankikaku2", "kakuhanjikankikaku2");                        // 撹拌時間規格②
        mapping.put("kakuhankaisinichiji2", "kakuhankaisinichiji2");                      // 撹拌開始日時②
        mapping.put("kakuhansyuuryounichiji2", "kakuhansyuuryounichiji2");                // 撹拌終了日時②
        mapping.put("tounyuu4", "tounyuu4");                                              // 投入④
        mapping.put("kakuhanjikankikaku3", "kakuhanjikankikaku3");                        // 撹拌時間規格③
        mapping.put("kakuhankaisinichiji3", "kakuhankaisinichiji3");                      // 撹拌開始日時③
        mapping.put("kakuhansyuuryounichiji3", "kakuhansyuuryounichiji3");                // 撹拌終了日時③
        mapping.put("tounyuu5", "tounyuu5");                                              // 投入⑤
        mapping.put("kakuhanjikankikaku4", "kakuhanjikankikaku4");                        // 撹拌時間規格④
        mapping.put("kakuhankaisinichiji4", "kakuhankaisinichiji4");                      // 撹拌開始日時④
        mapping.put("kakuhansyuuryounichiji4", "kakuhansyuuryounichiji4");                // 撹拌終了日時④
        mapping.put("tounyuu6", "tounyuu6");                                              // 投入⑥
        mapping.put("kakuhanjikankikaku5", "kakuhanjikankikaku5");                        // 撹拌時間規格⑤
        mapping.put("kakuhankaisinichiji5", "kakuhankaisinichiji5");                      // 撹拌開始日時⑤
        mapping.put("kakuhansyuuryounichiji5", "kakuhansyuuryounichiji5");                // 撹拌終了日時⑤
        mapping.put("kaitensuuhenkou", "kaitensuuhenkou");                                // 回転数変更
        mapping.put("tounyuu7", "tounyuu7");                                              // 投入⑦
        mapping.put("tounyuu8", "tounyuu8");                                              // 投入⑧
        mapping.put("tounyuu9", "tounyuu9");                                              // 投入⑨
        mapping.put("kakuhanjikankikaku6", "kakuhanjikankikaku6");                        // 撹拌時間規格⑥
        mapping.put("kakuhankaisinichiji6", "kakuhankaisinichiji6");                      // 撹拌開始日時⑥
        mapping.put("kakuhansyuuryounichiji6", "kakuhansyuuryounichiji6");                // 撹拌終了日時⑥
        mapping.put("tounyuu10", "tounyuu10");                                            // 投入⑩
        mapping.put("tounyuu11", "tounyuu11");                                            // 投入⑪
        mapping.put("tounyuu12", "tounyuu12");                                            // 投入⑫
        mapping.put("tounyuu13", "tounyuu13");                                            // 投入⑬
        mapping.put("tounyuu14", "tounyuu14");                                            // 投入⑭
        mapping.put("tounyuu15", "tounyuu15");                                            // 投入⑮
        mapping.put("tounyuu16", "tounyuu16");                                            // 投入⑯
        mapping.put("tounyuu17", "tounyuu17");                                            // 投入⑰
        mapping.put("tounyuu18", "tounyuu18");                                            // 投入⑱
        mapping.put("tounyuu19", "tounyuu19");                                            // 投入⑲
        mapping.put("tounyuusyuuryounichiji", "tounyuusyuuryounichiji");                  // 投入終了日時
        mapping.put("kakuhanki", "kakuhanki");                                            // 撹拌機
        mapping.put("kaitensuu", "kaitensuu");                                            // 回転数
        mapping.put("kakuhanjikankikaku7", "kakuhanjikankikaku7");                        // 撹拌時間規格⑦
        mapping.put("kakuhankaisinichiji7", "kakuhankaisinichiji7");                      // 撹拌開始日時⑦
        mapping.put("kaitentaihenosessyokunokakunin", "kaitentaihenosessyokunokakunin");  // 回転体への接触の確認
        mapping.put("kakuhansyuuryounichiji7", "kakuhansyuuryounichiji7");                // 撹拌終了日時⑦
        mapping.put("tantousya", "tantousya");                                            // 担当者
        mapping.put("bikou1", "bikou1");                                                  // 備考1
        mapping.put("bikou2", "bikou2");                                                  // 備考2
        mapping.put("torokunichiji", "torokunichiji");                                    // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                                      // 更新日時
        mapping.put("revision", "revision");                                              // revision
        mapping.put("deleteflag", "deleteflag");                                          // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrYuudentaiPremixing>> beanHandler = new BeanListHandler<>(SrYuudentaiPremixing.class, rowProcessor);

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
     * @param srYuudentaiPremixing 誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞデータ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrYuudentaiPremixing srYuudentaiPremixing) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srYuudentaiPremixing != null) {
            // 元データが存在する場合元データより取得
            return getSrYuudentaiPremixingItemData(itemId, srYuudentaiPremixing);
        } else {
            return null;
        }
    }

    /**
     * 項目データ(入力値)取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @param srGlasshyoryo 誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞデータ
     * @return 入力値
     */
    private String getItemKikakuchi(List<FXHDD01> listData, String itemId, SrYuudentaiPremixing srYuudentaiPremixing) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return StringUtil.nullToBlank(selectData.get(0).getKikakuChi()).replace("【", "").replace("】", "");
        } else if (srYuudentaiPremixing != null) {
            // 元データが存在する場合元データより取得
            return getSrYuudentaiPremixingItemData(itemId, srYuudentaiPremixing);
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
     * 誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞ_仮登録(tmp_sr_yuudentai_premixing)登録処理
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
    private void insertTmpSrYuudentaiPremixing(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "INSERT INTO tmp_sr_yuudentai_premixing ( "
                + " kojyo,lotno,edaban,yuudentaislurryhinmei,yuudentaislurrylotno,lotkubun,genryoulotno,genryoukigou,youzaisenjyou_kakuhanki,"
                + "youzaisenjyou_siyoutank,youzairyou,youzaitounyuu,syuziku,pump,senjyoujyouken_dispakaitensuu,separate,agitator,"
                + "jidouunten,tankABjyunkan,passsuu,senjyoujikan,raptime,premixing_kakuhanki,premixing_siyoutank,earthgripsetuzokukakunin,"
                + "premixing_dispakaitensuu,tounyuukaisinichiji,tounyuu1,tounyuu2,kakuhanjikankikaku1,kakuhankaisinichiji1,kakuhansyuuryounichiji1"
                + ",tounyuu3,kakuhanjikankikaku2,kakuhankaisinichiji2,kakuhansyuuryounichiji2,tounyuu4,kakuhanjikankikaku3,kakuhankaisinichiji3,"
                + "kakuhansyuuryounichiji3,tounyuu5,kakuhanjikankikaku4,kakuhankaisinichiji4,kakuhansyuuryounichiji4,tounyuu6,kakuhanjikankikaku5,"
                + "kakuhankaisinichiji5,kakuhansyuuryounichiji5,kaitensuuhenkou,tounyuu7,tounyuu8,tounyuu9,kakuhanjikankikaku6,kakuhankaisinichiji6,"
                + "kakuhansyuuryounichiji6,tounyuu10,tounyuu11,tounyuu12,tounyuu13,tounyuu14,tounyuu15,tounyuu16,tounyuu17,tounyuu18,tounyuu19,"
                + "tounyuusyuuryounichiji,kakuhanki,kaitensuu,kakuhanjikankikaku7,kakuhankaisinichiji7,kaitentaihenosessyokunokakunin,kakuhansyuuryounichiji7,"
                + "tantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrYuudentaiPremixing(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, processData, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞ_仮登録(tmp_sr_yuudentai_premixing)更新処理
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
    private void updateTmpSrYuudentaiPremixing(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "UPDATE tmp_sr_yuudentai_premixing SET "
                + " yuudentaislurryhinmei = ?,yuudentaislurrylotno = ?,lotkubun = ?,genryoulotno = ?,genryoukigou = ?,youzaisenjyou_kakuhanki = ?,youzaisenjyou_siyoutank = ?,"
                + "youzairyou = ?,youzaitounyuu = ?,syuziku = ?,pump = ?,senjyoujyouken_dispakaitensuu = ?,separate = ?,agitator = ?,jidouunten = ?,tankABjyunkan = ?,"
                + "passsuu = ?,senjyoujikan = ?,raptime = ?,premixing_kakuhanki = ?,premixing_siyoutank = ?,earthgripsetuzokukakunin = ?,premixing_dispakaitensuu = ?,"
                + "tounyuukaisinichiji = ?,tounyuu1 = ?,tounyuu2 = ?,kakuhanjikankikaku1 = ?,kakuhankaisinichiji1 = ?,kakuhansyuuryounichiji1 = ?,tounyuu3 = ?,"
                + "kakuhanjikankikaku2 = ?,kakuhankaisinichiji2 = ?,kakuhansyuuryounichiji2 = ?,tounyuu4 = ?,kakuhanjikankikaku3 = ?,kakuhankaisinichiji3 = ?,"
                + "kakuhansyuuryounichiji3 = ?,tounyuu5 = ?,kakuhanjikankikaku4 = ?,kakuhankaisinichiji4 = ?,kakuhansyuuryounichiji4 = ?,tounyuu6 = ?,kakuhanjikankikaku5 = ?,"
                + "kakuhankaisinichiji5 = ?,kakuhansyuuryounichiji5 = ?,kaitensuuhenkou = ?,tounyuu7 = ?,tounyuu8 = ?,tounyuu9 = ?,kakuhanjikankikaku6 = ?,kakuhankaisinichiji6 = ?,"
                + "kakuhansyuuryounichiji6 = ?,tounyuu10 = ?,tounyuu11 = ?,tounyuu12 = ?,tounyuu13 = ?,tounyuu14 = ?,tounyuu15 = ?,tounyuu16 = ?,tounyuu17 = ?,tounyuu18 = ?,"
                + "tounyuu19 = ?,tounyuusyuuryounichiji = ?,kakuhanki = ?,kaitensuu = ?,kakuhanjikankikaku7 = ?,kakuhankaisinichiji7 = ?,kaitentaihenosessyokunokakunin = ?,"
                + "kakuhansyuuryounichiji7 = ?,tantousya = ?,bikou1 = ?,bikou2 = ?,kosinnichiji = ?,revision = ?,deleteflag = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrYuudentaiPremixing> srYuudentaiPremixingList = getSrYuudentaiPremixingData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrYuudentaiPremixing srYuudentaiPremixing = null;
        if (!srYuudentaiPremixingList.isEmpty()) {
            srYuudentaiPremixing = srYuudentaiPremixingList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrYuudentaiPremixing(false, newRev, 0, "", "", "", systemTime, processData, srYuudentaiPremixing);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞ_仮登録(tmp_sr_yuudentai_premixing)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrYuudentaiPremixing(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sr_yuudentai_premixing "
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
     * 誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞ_仮登録(tmp_sr_yuudentai_premixing)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srYuudentaiPremixing 誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞデータ
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrYuudentaiPremixing(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, String systemTime, ProcessData processData, SrYuudentaiPremixing srYuudentaiPremixing) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // 投入開始日時
        String tounyuukaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B023Const.TOUNYUUKAISI_TIME, srYuudentaiPremixing));
        // 投入終了日時
        String tounyuusyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B023Const.TOUNYUUSYUURYOU_TIME, srYuudentaiPremixing));
        // 撹拌開始日時①
        String kakuhankaisi1Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B023Const.KAKUHANKAISI1_TIME, srYuudentaiPremixing));
        // 撹拌終了日時①
        String kakuhansyuuryou1Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B023Const.KAKUHANSYUURYOU1_TIME, srYuudentaiPremixing));
        // 撹拌開始日時②
        String kakuhankaisi2Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B023Const.KAKUHANKAISI2_TIME, srYuudentaiPremixing));
        // 撹拌終了日時②
        String kakuhansyuuryou2Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B023Const.KAKUHANSYUURYOU2_TIME, srYuudentaiPremixing));
        // 撹拌開始日時③
        String kakuhankaisi3Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B023Const.KAKUHANKAISI3_TIME, srYuudentaiPremixing));
        // 撹拌終了日時③
        String kakuhansyuuryou3Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B023Const.KAKUHANSYUURYOU3_TIME, srYuudentaiPremixing));
        // 撹拌開始日時④
        String kakuhankaisi4Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B023Const.KAKUHANKAISI4_TIME, srYuudentaiPremixing));
        // 撹拌終了日時④
        String kakuhansyuuryou4Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B023Const.KAKUHANSYUURYOU4_TIME, srYuudentaiPremixing));
        // 撹拌開始日時⑤
        String kakuhankaisi5Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B023Const.KAKUHANKAISI5_TIME, srYuudentaiPremixing));
        // 撹拌終了日時⑤
        String kakuhansyuuryou5Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B023Const.KAKUHANSYUURYOU5_TIME, srYuudentaiPremixing));
        // 撹拌開始日時⑥
        String kakuhankaisi6Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B023Const.KAKUHANKAISI6_TIME, srYuudentaiPremixing));
        // 撹拌終了日時⑥
        String kakuhansyuuryou6Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B023Const.KAKUHANSYUURYOU6_TIME, srYuudentaiPremixing));
        // 撹拌開始日時⑦
        String kakuhankaisi7Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B023Const.KAKUHANKAISI7_TIME, srYuudentaiPremixing));
        // 撹拌終了日時⑦
        String kakuhansyuuryou7Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B023Const.KAKUHANSYUURYOU7_TIME, srYuudentaiPremixing));

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B023Const.YUUDENTAISLURRYHINMEI, srYuudentaiPremixing)));              // 誘電体ｽﾗﾘｰ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B023Const.YUUDENTAISLURRYLOTNO, srYuudentaiPremixing)));               // 誘電体ｽﾗﾘｰLotNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B023Const.LOTKUBUN, srYuudentaiPremixing)));                           // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.GENRYOULOTNO, srYuudentaiPremixing)));                  // 原料LotNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.GENRYOUKIGOU, srYuudentaiPremixing)));                  // 原料記号
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.YOUZAISENJYOU_KAKUHANKI, srYuudentaiPremixing)));       // 溶剤洗浄_撹拌機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.YOUZAISENJYOU_SIYOUTANK, srYuudentaiPremixing)));       // 溶剤洗浄_使用ﾀﾝｸ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.YOUZAIRYOU, srYuudentaiPremixing)));                    // 溶剤量
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B023Const.YOUZAITOUNYUU, srYuudentaiPremixing), null));                         // 溶剤投入
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.SYUZIKU, srYuudentaiPremixing)));                       // 主軸
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.PUMP, srYuudentaiPremixing)));                          // ﾎﾟﾝﾌﾟ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.SJJYOUKEN_DISPAKAITENSUU, srYuudentaiPremixing)));      // 洗浄条件_ﾃﾞｨｽﾊﾟ回転数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.SEPARATE, srYuudentaiPremixing)));                      // ｾﾊﾟﾚｰﾀ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.AGITATOR, srYuudentaiPremixing)));                      // ｱｼﾞﾃｰﾀ
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B023Const.JIDOUUNTEN, srYuudentaiPremixing), null));                            // 自動運転
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B023Const.TANKABJYUNKAN, srYuudentaiPremixing), null));                         // ﾀﾝｸAB循環
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.PASSSUU, srYuudentaiPremixing)));                       // ﾊﾟｽ数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.SENJYOUJIKAN, srYuudentaiPremixing)));                  // 洗浄時間
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B023Const.RAPTIME, srYuudentaiPremixing)));                            // ﾗｯﾌﾟﾀｲﾑ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.PREMIXING_KAKUHANKI, srYuudentaiPremixing)));           // ﾌﾟﾚﾐｷ_撹拌機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.PREMIXING_SIYOUTANK, srYuudentaiPremixing)));           // ﾌﾟﾚﾐｷ_使用ﾀﾝｸ
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B023Const.EARTHGRIPSETUZOKUKAKUNIN, srYuudentaiPremixing), null));              // ｱｰｽｸﾞﾘｯﾌﾟ接続確認
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.PREMIXING_DISPAKAITENSUU, srYuudentaiPremixing)));      // ﾌﾟﾚﾐｷ_ﾃﾞｨｽﾊﾟ回転数
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B023Const.TOUNYUUKAISI_DAY, srYuudentaiPremixing),
                "".equals(tounyuukaisiTime) ? "0000" : tounyuukaisiTime));                                                                                      // 投入開始日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU1, srYuudentaiPremixing)));                      // 投入①
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU2, srYuudentaiPremixing)));                      // 投入②
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.KAKUHANJIKANKIKAKU1, srYuudentaiPremixing)));           // 撹拌時間規格①
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B023Const.KAKUHANKAISI1_DAY, srYuudentaiPremixing),
                "".equals(kakuhankaisi1Time) ? "0000" : kakuhankaisi1Time));                                                                                    // 撹拌開始日時①
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B023Const.KAKUHANSYUURYOU1_DAY, srYuudentaiPremixing),
                "".equals(kakuhansyuuryou1Time) ? "0000" : kakuhansyuuryou1Time));                                                                              // 撹拌終了日時①
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU3, srYuudentaiPremixing)));                      // 投入③
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.KAKUHANJIKANKIKAKU2, srYuudentaiPremixing)));           // 撹拌時間規格②
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B023Const.KAKUHANKAISI2_DAY, srYuudentaiPremixing),
                "".equals(kakuhankaisi2Time) ? "0000" : kakuhankaisi2Time));                                                                                    // 撹拌開始日時②
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B023Const.KAKUHANSYUURYOU2_DAY, srYuudentaiPremixing),
                "".equals(kakuhansyuuryou2Time) ? "0000" : kakuhansyuuryou2Time));                                                                              // 撹拌終了日時②
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU4, srYuudentaiPremixing)));                      // 投入④
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.KAKUHANJIKANKIKAKU3, srYuudentaiPremixing)));           // 撹拌時間規格③
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B023Const.KAKUHANKAISI3_DAY, srYuudentaiPremixing),
                "".equals(kakuhankaisi3Time) ? "0000" : kakuhankaisi3Time));                                                                                    // 撹拌開始日時③
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B023Const.KAKUHANSYUURYOU3_DAY, srYuudentaiPremixing),
                "".equals(kakuhansyuuryou3Time) ? "0000" : kakuhansyuuryou3Time));                                                                              // 撹拌終了日時③
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU5, srYuudentaiPremixing)));                      // 投入⑤
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.KAKUHANJIKANKIKAKU4, srYuudentaiPremixing)));           // 撹拌時間規格④
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B023Const.KAKUHANKAISI4_DAY, srYuudentaiPremixing),
                "".equals(kakuhankaisi4Time) ? "0000" : kakuhankaisi4Time));                                                                                    // 撹拌開始日時④
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B023Const.KAKUHANSYUURYOU4_DAY, srYuudentaiPremixing),
                "".equals(kakuhansyuuryou4Time) ? "0000" : kakuhansyuuryou4Time));                                                                              // 撹拌終了日時④
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU6, srYuudentaiPremixing)));                      // 投入⑥
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.KAKUHANJIKANKIKAKU5, srYuudentaiPremixing)));           // 撹拌時間規格⑤
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B023Const.KAKUHANKAISI5_DAY, srYuudentaiPremixing),
                "".equals(kakuhankaisi5Time) ? "0000" : kakuhankaisi5Time));                                                                                    // 撹拌開始日時⑤
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B023Const.KAKUHANSYUURYOU5_DAY, srYuudentaiPremixing),
                "".equals(kakuhansyuuryou5Time) ? "0000" : kakuhansyuuryou5Time));                                                                              // 撹拌終了日時⑤
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.KAITENSUUHENKOU, srYuudentaiPremixing)));               // 回転数変更
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU7, srYuudentaiPremixing)));                      // 投入⑦
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU8, srYuudentaiPremixing)));                      // 投入⑧
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU9, srYuudentaiPremixing)));                      // 投入⑨
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.KAKUHANJIKANKIKAKU6, srYuudentaiPremixing)));           // 撹拌時間規格⑥
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B023Const.KAKUHANKAISI6_DAY, srYuudentaiPremixing),
                "".equals(kakuhankaisi6Time) ? "0000" : kakuhankaisi6Time));                                                                                    // 撹拌開始日時⑥
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B023Const.KAKUHANSYUURYOU6_DAY, srYuudentaiPremixing),
                "".equals(kakuhansyuuryou6Time) ? "0000" : kakuhansyuuryou6Time));                                                                              // 撹拌終了日時⑥
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU10, srYuudentaiPremixing)));                     // 投入⑩
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU11, srYuudentaiPremixing)));                     // 投入⑪
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU12, srYuudentaiPremixing)));                     // 投入⑫
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU13, srYuudentaiPremixing)));                     // 投入⑬
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU14, srYuudentaiPremixing)));                     // 投入⑭
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU15, srYuudentaiPremixing)));                     // 投入⑮
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU16, srYuudentaiPremixing)));                     // 投入⑯
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU17, srYuudentaiPremixing)));                     // 投入⑰
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU18, srYuudentaiPremixing)));                     // 投入⑱
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU19, srYuudentaiPremixing)));                     // 投入⑲
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B023Const.TOUNYUUSYUURYOU_DAY, srYuudentaiPremixing),
                "".equals(tounyuusyuuryouTime) ? "0000" : tounyuusyuuryouTime));                                                                                // 投入終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.KAKUHANKI, srYuudentaiPremixing)));                     // 撹拌機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.KAITENSUU, srYuudentaiPremixing)));                     // 回転数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B023Const.KAKUHANJIKANKIKAKU7, srYuudentaiPremixing)));           // 撹拌時間規格⑦
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B023Const.KAKUHANKAISI7_DAY, srYuudentaiPremixing),
                "".equals(kakuhankaisi7Time) ? "0000" : kakuhankaisi7Time));                                                                                    // 撹拌開始日時⑦
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B023Const.KAITENTAIHESESYOKUKAKUNIN, srYuudentaiPremixing), null));             // 回転体への接触の確認
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B023Const.KAKUHANSYUURYOU7_DAY, srYuudentaiPremixing),
                "".equals(kakuhansyuuryou7Time) ? "0000" : kakuhansyuuryou7Time));                                                                              // 撹拌終了日時⑦
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B023Const.TANTOUSYA, srYuudentaiPremixing)));                          // 担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B023Const.BIKOU1, srYuudentaiPremixing)));                             // 備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B023Const.BIKOU2, srYuudentaiPremixing)));                             // 備考2

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
     * 誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞ(sr_yuudentai_premixing)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param tmpSrYuudentaiPremixing 仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrYuudentaiPremixing(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData, SrYuudentaiPremixing tmpSrYuudentaiPremixing) throws SQLException {

        String sql = "INSERT INTO sr_yuudentai_premixing ( "
                + " kojyo,lotno,edaban,yuudentaislurryhinmei,yuudentaislurrylotno,lotkubun,genryoulotno,genryoukigou,youzaisenjyou_kakuhanki,"
                + "youzaisenjyou_siyoutank,youzairyou,youzaitounyuu,syuziku,pump,senjyoujyouken_dispakaitensuu,separate,agitator,"
                + "jidouunten,tankABjyunkan,passsuu,senjyoujikan,raptime,premixing_kakuhanki,premixing_siyoutank,earthgripsetuzokukakunin,"
                + "premixing_dispakaitensuu,tounyuukaisinichiji,tounyuu1,tounyuu2,kakuhanjikankikaku1,kakuhankaisinichiji1,kakuhansyuuryounichiji1"
                + ",tounyuu3,kakuhanjikankikaku2,kakuhankaisinichiji2,kakuhansyuuryounichiji2,tounyuu4,kakuhanjikankikaku3,kakuhankaisinichiji3,"
                + "kakuhansyuuryounichiji3,tounyuu5,kakuhanjikankikaku4,kakuhankaisinichiji4,kakuhansyuuryounichiji4,tounyuu6,kakuhanjikankikaku5,"
                + "kakuhankaisinichiji5,kakuhansyuuryounichiji5,kaitensuuhenkou,tounyuu7,tounyuu8,tounyuu9,kakuhanjikankikaku6,kakuhankaisinichiji6,"
                + "kakuhansyuuryounichiji6,tounyuu10,tounyuu11,tounyuu12,tounyuu13,tounyuu14,tounyuu15,tounyuu16,tounyuu17,tounyuu18,tounyuu19,"
                + "tounyuusyuuryounichiji,kakuhanki,kaitensuu,kakuhanjikankikaku7,kakuhankaisinichiji7,kaitentaihenosessyokunokakunin,kakuhansyuuryounichiji7,"
                + "tantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrYuudentaiPremixing(true, newRev, kojyo, lotNo, edaban, systemTime, processData, tmpSrYuudentaiPremixing);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞ(sr_yuudentai_premixing)更新処理
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
    private void updateSrYuudentaiPremixing(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {
        String sql = "UPDATE sr_yuudentai_premixing SET "
                + " yuudentaislurryhinmei = ?,yuudentaislurrylotno = ?,lotkubun = ?,genryoulotno = ?,genryoukigou = ?,youzaisenjyou_kakuhanki = ?,youzaisenjyou_siyoutank = ?,"
                + "youzairyou = ?,youzaitounyuu = ?,syuziku = ?,pump = ?,senjyoujyouken_dispakaitensuu = ?,separate = ?,agitator = ?,jidouunten = ?,tankABjyunkan = ?,"
                + "passsuu = ?,senjyoujikan = ?,raptime = ?,premixing_kakuhanki = ?,premixing_siyoutank = ?,earthgripsetuzokukakunin = ?,premixing_dispakaitensuu = ?,"
                + "tounyuukaisinichiji = ?,tounyuu1 = ?,tounyuu2 = ?,kakuhanjikankikaku1 = ?,kakuhankaisinichiji1 = ?,kakuhansyuuryounichiji1 = ?,tounyuu3 = ?,"
                + "kakuhanjikankikaku2 = ?,kakuhankaisinichiji2 = ?,kakuhansyuuryounichiji2 = ?,tounyuu4 = ?,kakuhanjikankikaku3 = ?,kakuhankaisinichiji3 = ?,"
                + "kakuhansyuuryounichiji3 = ?,tounyuu5 = ?,kakuhanjikankikaku4 = ?,kakuhankaisinichiji4 = ?,kakuhansyuuryounichiji4 = ?,tounyuu6 = ?,kakuhanjikankikaku5 = ?,"
                + "kakuhankaisinichiji5 = ?,kakuhansyuuryounichiji5 = ?,kaitensuuhenkou = ?,tounyuu7 = ?,tounyuu8 = ?,tounyuu9 = ?,kakuhanjikankikaku6 = ?,kakuhankaisinichiji6 = ?,"
                + "kakuhansyuuryounichiji6 = ?,tounyuu10 = ?,tounyuu11 = ?,tounyuu12 = ?,tounyuu13 = ?,tounyuu14 = ?,tounyuu15 = ?,tounyuu16 = ?,tounyuu17 = ?,tounyuu18 = ?,"
                + "tounyuu19 = ?,tounyuusyuuryounichiji = ?,kakuhanki = ?,kaitensuu = ?,kakuhanjikankikaku7 = ?,kakuhankaisinichiji7 = ?,kaitentaihenosessyokunokakunin = ?,"
                + "kakuhansyuuryounichiji7 = ?,tantousya = ?,bikou1 = ?,bikou2 = ?,kosinnichiji = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrYuudentaiPremixing> srYuudentaiPremixingList = getSrYuudentaiPremixingData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrYuudentaiPremixing srYuudentaiPremixing = null;
        if (!srYuudentaiPremixingList.isEmpty()) {
            srYuudentaiPremixing = srYuudentaiPremixingList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrYuudentaiPremixing(false, newRev, "", "", "", systemTime, processData, srYuudentaiPremixing);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞ(sr_yuudentai_premixing)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srYuudentaiPremixing 誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞデータ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrYuudentaiPremixing(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            String systemTime, ProcessData processData, SrYuudentaiPremixing srYuudentaiPremixing) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // 投入開始日時
        String tounyuukaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B023Const.TOUNYUUKAISI_TIME, srYuudentaiPremixing));
        // 投入終了日時
        String tounyuusyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B023Const.TOUNYUUSYUURYOU_TIME, srYuudentaiPremixing));
        // 撹拌開始日時①
        String kakuhankaisi1Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B023Const.KAKUHANKAISI1_TIME, srYuudentaiPremixing));
        // 撹拌終了日時①
        String kakuhansyuuryou1Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B023Const.KAKUHANSYUURYOU1_TIME, srYuudentaiPremixing));
        // 撹拌開始日時②
        String kakuhankaisi2Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B023Const.KAKUHANKAISI2_TIME, srYuudentaiPremixing));
        // 撹拌終了日時②
        String kakuhansyuuryou2Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B023Const.KAKUHANSYUURYOU2_TIME, srYuudentaiPremixing));
        // 撹拌開始日時③
        String kakuhankaisi3Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B023Const.KAKUHANKAISI3_TIME, srYuudentaiPremixing));
        // 撹拌終了日時③
        String kakuhansyuuryou3Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B023Const.KAKUHANSYUURYOU3_TIME, srYuudentaiPremixing));
        // 撹拌開始日時④
        String kakuhankaisi4Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B023Const.KAKUHANKAISI4_TIME, srYuudentaiPremixing));
        // 撹拌終了日時④
        String kakuhansyuuryou4Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B023Const.KAKUHANSYUURYOU4_TIME, srYuudentaiPremixing));
        // 撹拌開始日時⑤
        String kakuhankaisi5Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B023Const.KAKUHANKAISI5_TIME, srYuudentaiPremixing));
        // 撹拌終了日時⑤
        String kakuhansyuuryou5Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B023Const.KAKUHANSYUURYOU5_TIME, srYuudentaiPremixing));
        // 撹拌開始日時⑥
        String kakuhankaisi6Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B023Const.KAKUHANKAISI6_TIME, srYuudentaiPremixing));
        // 撹拌終了日時⑥
        String kakuhansyuuryou6Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B023Const.KAKUHANSYUURYOU6_TIME, srYuudentaiPremixing));
        // 撹拌開始日時⑦
        String kakuhankaisi7Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B023Const.KAKUHANKAISI7_TIME, srYuudentaiPremixing));
        // 撹拌終了日時⑦
        String kakuhansyuuryou7Time = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B023Const.KAKUHANSYUURYOU7_TIME, srYuudentaiPremixing));
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B023Const.YUUDENTAISLURRYHINMEI, srYuudentaiPremixing)));              // 誘電体ｽﾗﾘｰ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B023Const.YUUDENTAISLURRYLOTNO, srYuudentaiPremixing)));               // 誘電体ｽﾗﾘｰLotNo
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B023Const.LOTKUBUN, srYuudentaiPremixing)));                           // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.GENRYOULOTNO, srYuudentaiPremixing)));                  // 原料LotNo
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.GENRYOUKIGOU, srYuudentaiPremixing)));                  // 原料記号
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.YOUZAISENJYOU_KAKUHANKI, srYuudentaiPremixing)));       // 溶剤洗浄_撹拌機
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.YOUZAISENJYOU_SIYOUTANK, srYuudentaiPremixing)));       // 溶剤洗浄_使用ﾀﾝｸ
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.YOUZAIRYOU, srYuudentaiPremixing)));                    // 溶剤量
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B023Const.YOUZAITOUNYUU, srYuudentaiPremixing), 9));                         // 溶剤投入
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.SYUZIKU, srYuudentaiPremixing)));                       // 主軸
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.PUMP, srYuudentaiPremixing)));                          // ﾎﾟﾝﾌﾟ
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.SJJYOUKEN_DISPAKAITENSUU, srYuudentaiPremixing)));      // 洗浄条件_ﾃﾞｨｽﾊﾟ回転数
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.SEPARATE, srYuudentaiPremixing)));                      // ｾﾊﾟﾚｰﾀ
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.AGITATOR, srYuudentaiPremixing)));                      // ｱｼﾞﾃｰﾀ
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B023Const.JIDOUUNTEN, srYuudentaiPremixing), 9));                            // 自動運転
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B023Const.TANKABJYUNKAN, srYuudentaiPremixing), 9));                         // ﾀﾝｸAB循環
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.PASSSUU, srYuudentaiPremixing)));                       // ﾊﾟｽ数
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.SENJYOUJIKAN, srYuudentaiPremixing)));                  // 洗浄時間
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B023Const.RAPTIME, srYuudentaiPremixing)));                            // ﾗｯﾌﾟﾀｲﾑ
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.PREMIXING_KAKUHANKI, srYuudentaiPremixing)));           // ﾌﾟﾚﾐｷ_撹拌機
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.PREMIXING_SIYOUTANK, srYuudentaiPremixing)));           // ﾌﾟﾚﾐｷ_使用ﾀﾝｸ
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B023Const.EARTHGRIPSETUZOKUKAKUNIN, srYuudentaiPremixing), 9));              // ｱｰｽｸﾞﾘｯﾌﾟ接続確認
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.PREMIXING_DISPAKAITENSUU, srYuudentaiPremixing)));      // ﾌﾟﾚﾐｷ_ﾃﾞｨｽﾊﾟ回転数
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B023Const.TOUNYUUKAISI_DAY, srYuudentaiPremixing),
                "".equals(tounyuukaisiTime) ? "0000" : tounyuukaisiTime));                                                                           // 投入開始日時
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU1, srYuudentaiPremixing)));                      // 投入①
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU2, srYuudentaiPremixing)));                      // 投入②
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.KAKUHANJIKANKIKAKU1, srYuudentaiPremixing)));           // 撹拌時間規格①
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B023Const.KAKUHANKAISI1_DAY, srYuudentaiPremixing),
                "".equals(kakuhankaisi1Time) ? "0000" : kakuhankaisi1Time));                                                                         // 撹拌開始日時①
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B023Const.KAKUHANSYUURYOU1_DAY, srYuudentaiPremixing),
                "".equals(kakuhansyuuryou1Time) ? "0000" : kakuhansyuuryou1Time));                                                                   // 撹拌終了日時①
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU3, srYuudentaiPremixing)));                      // 投入③
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.KAKUHANJIKANKIKAKU2, srYuudentaiPremixing)));           // 撹拌時間規格②
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B023Const.KAKUHANKAISI2_DAY, srYuudentaiPremixing),
                "".equals(kakuhankaisi2Time) ? "0000" : kakuhankaisi2Time));                                                                         // 撹拌開始日時②
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B023Const.KAKUHANSYUURYOU2_DAY, srYuudentaiPremixing),
                "".equals(kakuhansyuuryou2Time) ? "0000" : kakuhansyuuryou2Time));                                                                   // 撹拌終了日時②
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU4, srYuudentaiPremixing)));                      // 投入④
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.KAKUHANJIKANKIKAKU3, srYuudentaiPremixing)));           // 撹拌時間規格③
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B023Const.KAKUHANKAISI3_DAY, srYuudentaiPremixing),
                "".equals(kakuhankaisi3Time) ? "0000" : kakuhankaisi3Time));                                                                         // 撹拌開始日時③
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B023Const.KAKUHANSYUURYOU3_DAY, srYuudentaiPremixing),
                "".equals(kakuhansyuuryou3Time) ? "0000" : kakuhansyuuryou3Time));                                                                   // 撹拌終了日時③
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU5, srYuudentaiPremixing)));                      // 投入⑤
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.KAKUHANJIKANKIKAKU4, srYuudentaiPremixing)));           // 撹拌時間規格④
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B023Const.KAKUHANKAISI4_DAY, srYuudentaiPremixing),
                "".equals(kakuhankaisi4Time) ? "0000" : kakuhankaisi4Time));                                                                         // 撹拌開始日時④
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B023Const.KAKUHANSYUURYOU4_DAY, srYuudentaiPremixing),
                "".equals(kakuhansyuuryou4Time) ? "0000" : kakuhansyuuryou4Time));                                                                   // 撹拌終了日時④
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU6, srYuudentaiPremixing)));                      // 投入⑥
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.KAKUHANJIKANKIKAKU5, srYuudentaiPremixing)));           // 撹拌時間規格⑤
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B023Const.KAKUHANKAISI5_DAY, srYuudentaiPremixing),
                "".equals(kakuhankaisi5Time) ? "0000" : kakuhankaisi5Time));                                                                         // 撹拌開始日時⑤
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B023Const.KAKUHANSYUURYOU5_DAY, srYuudentaiPremixing),
                "".equals(kakuhansyuuryou5Time) ? "0000" : kakuhansyuuryou5Time));                                                                   // 撹拌終了日時⑤
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.KAITENSUUHENKOU, srYuudentaiPremixing)));               // 回転数変更
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU7, srYuudentaiPremixing)));                      // 投入⑦
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU8, srYuudentaiPremixing)));                      // 投入⑧
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU9, srYuudentaiPremixing)));                      // 投入⑨
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.KAKUHANJIKANKIKAKU6, srYuudentaiPremixing)));           // 撹拌時間規格⑥
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B023Const.KAKUHANKAISI6_DAY, srYuudentaiPremixing),
                "".equals(kakuhankaisi6Time) ? "0000" : kakuhankaisi6Time));                                                                         // 撹拌開始日時⑥
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B023Const.KAKUHANSYUURYOU6_DAY, srYuudentaiPremixing),
                "".equals(kakuhansyuuryou6Time) ? "0000" : kakuhansyuuryou6Time));                                                                   // 撹拌終了日時⑥
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU10, srYuudentaiPremixing)));                     // 投入⑩
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU11, srYuudentaiPremixing)));                     // 投入⑪
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU12, srYuudentaiPremixing)));                     // 投入⑫
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU13, srYuudentaiPremixing)));                     // 投入⑬
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU14, srYuudentaiPremixing)));                     // 投入⑭
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU15, srYuudentaiPremixing)));                     // 投入⑮
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU16, srYuudentaiPremixing)));                     // 投入⑯
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU17, srYuudentaiPremixing)));                     // 投入⑰
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU18, srYuudentaiPremixing)));                     // 投入⑱
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.TOUNYUU19, srYuudentaiPremixing)));                     // 投入⑲
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B023Const.TOUNYUUSYUURYOU_DAY, srYuudentaiPremixing),
                "".equals(tounyuusyuuryouTime) ? "0000" : tounyuusyuuryouTime));                                                                     // 投入終了日時
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.KAKUHANKI, srYuudentaiPremixing)));                     // 撹拌機
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.KAITENSUU, srYuudentaiPremixing)));                     // 回転数
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B023Const.KAKUHANJIKANKIKAKU7, srYuudentaiPremixing)));           // 撹拌時間規格⑦
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B023Const.KAKUHANKAISI7_DAY, srYuudentaiPremixing),
                "".equals(kakuhankaisi7Time) ? "0000" : kakuhankaisi7Time));                                                                         // 撹拌開始日時⑦
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B023Const.KAITENTAIHESESYOKUKAKUNIN, srYuudentaiPremixing), 9));             // 回転体への接触の確認
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B023Const.KAKUHANSYUURYOU7_DAY, srYuudentaiPremixing),
                "".equals(kakuhansyuuryou7Time) ? "0000" : kakuhansyuuryou7Time));                                                                   // 撹拌終了日時⑦
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B023Const.TANTOUSYA, srYuudentaiPremixing)));                          // 担当者
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B023Const.BIKOU1, srYuudentaiPremixing)));                             // 備考1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B023Const.BIKOU2, srYuudentaiPremixing)));                             // 備考2

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
     * 誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞ(sr_yuudentai_premixing)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSrYuudentaiPremixing(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sr_yuudentai_premixing "
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
     * [誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞ_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_yuudentai_premixing "
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
     * @param srYuudentaiPremixing 誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞデータ
     * @return DB値
     */
    private String getSrYuudentaiPremixingItemData(String itemId, SrYuudentaiPremixing srYuudentaiPremixing) {
        switch (itemId) {
            // 誘電体ｽﾗﾘｰ品名
            case GXHDO102B023Const.YUUDENTAISLURRYHINMEI:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getYuudentaislurryhinmei());

            // 誘電体ｽﾗﾘｰLotNo
            case GXHDO102B023Const.YUUDENTAISLURRYLOTNO:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getYuudentaislurrylotno());

            // ﾛｯﾄ区分
            case GXHDO102B023Const.LOTKUBUN:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getLotkubun());

            // 原料LotNo
            case GXHDO102B023Const.GENRYOULOTNO:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getGenryoulotno());

            // 原料記号
            case GXHDO102B023Const.GENRYOUKIGOU:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getGenryoukigou());

            // 溶剤洗浄_撹拌機
            case GXHDO102B023Const.YOUZAISENJYOU_KAKUHANKI:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getYouzaisenjyou_kakuhanki());

            // 溶剤洗浄_使用ﾀﾝｸ
            case GXHDO102B023Const.YOUZAISENJYOU_SIYOUTANK:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getYouzaisenjyou_siyoutank());

            // 溶剤量
            case GXHDO102B023Const.YOUZAIRYOU:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getYouzairyou());

            // 溶剤投入
            case GXHDO102B023Const.YOUZAITOUNYUU:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srYuudentaiPremixing.getYouzaitounyuu()));

            // 主軸
            case GXHDO102B023Const.SYUZIKU:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getSyuziku());

            // ﾎﾟﾝﾌﾟ
            case GXHDO102B023Const.PUMP:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getPump());

            // 洗浄条件_ﾃﾞｨｽﾊﾟ回転数
            case GXHDO102B023Const.SJJYOUKEN_DISPAKAITENSUU:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getSenjyoujyouken_dispakaitensuu());

            // ｾﾊﾟﾚｰﾀ
            case GXHDO102B023Const.SEPARATE:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getSeparate());

            // ｱｼﾞﾃｰﾀ
            case GXHDO102B023Const.AGITATOR:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getAgitator());

            // 自動運転
            case GXHDO102B023Const.JIDOUUNTEN:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srYuudentaiPremixing.getJidouunten()));

            // ﾀﾝｸAB循環
            case GXHDO102B023Const.TANKABJYUNKAN:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srYuudentaiPremixing.getTankABjyunkan()));

            // ﾊﾟｽ数
            case GXHDO102B023Const.PASSSUU:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getPasssuu());

            // 洗浄時間
            case GXHDO102B023Const.SENJYOUJIKAN:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getSenjyoujikan());

            // ﾗｯﾌﾟﾀｲﾑ
            case GXHDO102B023Const.RAPTIME:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getRaptime());

            // ﾌﾟﾚﾐｷ_撹拌機
            case GXHDO102B023Const.PREMIXING_KAKUHANKI:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getPremixing_kakuhanki());

            // ﾌﾟﾚﾐｷ_使用ﾀﾝｸ
            case GXHDO102B023Const.PREMIXING_SIYOUTANK:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getPremixing_siyoutank());

            // ｱｰｽｸﾞﾘｯﾌﾟ接続確認
            case GXHDO102B023Const.EARTHGRIPSETUZOKUKAKUNIN:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srYuudentaiPremixing.getEarthgripsetuzokukakunin()));

            // ﾌﾟﾚﾐｷ_ﾃﾞｨｽﾊﾟ回転数
            case GXHDO102B023Const.PREMIXING_DISPAKAITENSUU:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getPremixing_dispakaitensuu());

            // 投入開始日
            case GXHDO102B023Const.TOUNYUUKAISI_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiPremixing.getTounyuukaisinichiji(), "yyMMdd");

            // 投入開始時間
            case GXHDO102B023Const.TOUNYUUKAISI_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiPremixing.getTounyuukaisinichiji(), "HHmm");

            // 投入①
            case GXHDO102B023Const.TOUNYUU1:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getTounyuu1());

            // 投入②
            case GXHDO102B023Const.TOUNYUU2:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getTounyuu2());

            // 撹拌時間規格①
            case GXHDO102B023Const.KAKUHANJIKANKIKAKU1:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getKakuhanjikankikaku1());

            // 撹拌開始日①
            case GXHDO102B023Const.KAKUHANKAISI1_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiPremixing.getKakuhankaisinichiji1(), "yyMMdd");

            // 撹拌開始時間①
            case GXHDO102B023Const.KAKUHANKAISI1_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiPremixing.getKakuhankaisinichiji1(), "HHmm");

            // 撹拌終了日①
            case GXHDO102B023Const.KAKUHANSYUURYOU1_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiPremixing.getKakuhansyuuryounichiji1(), "yyMMdd");

            // 撹拌終了時間①
            case GXHDO102B023Const.KAKUHANSYUURYOU1_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiPremixing.getKakuhansyuuryounichiji1(), "HHmm");

            // 投入③
            case GXHDO102B023Const.TOUNYUU3:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getTounyuu3());

            // 撹拌時間規格②
            case GXHDO102B023Const.KAKUHANJIKANKIKAKU2:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getKakuhanjikankikaku2());

            // 撹拌開始日②
            case GXHDO102B023Const.KAKUHANKAISI2_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiPremixing.getKakuhankaisinichiji2(), "yyMMdd");

            // 撹拌開始時間②
            case GXHDO102B023Const.KAKUHANKAISI2_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiPremixing.getKakuhankaisinichiji2(), "HHmm");

            // 撹拌終了日②
            case GXHDO102B023Const.KAKUHANSYUURYOU2_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiPremixing.getKakuhansyuuryounichiji2(), "yyMMdd");

            // 撹拌終了時間②
            case GXHDO102B023Const.KAKUHANSYUURYOU2_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiPremixing.getKakuhansyuuryounichiji2(), "HHmm");

            // 投入④
            case GXHDO102B023Const.TOUNYUU4:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getTounyuu4());

            // 撹拌時間規格③
            case GXHDO102B023Const.KAKUHANJIKANKIKAKU3:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getKakuhanjikankikaku3());

            // 撹拌開始日③
            case GXHDO102B023Const.KAKUHANKAISI3_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiPremixing.getKakuhankaisinichiji3(), "yyMMdd");

            // 撹拌開始時間③
            case GXHDO102B023Const.KAKUHANKAISI3_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiPremixing.getKakuhankaisinichiji3(), "HHmm");

            // 撹拌終了日③
            case GXHDO102B023Const.KAKUHANSYUURYOU3_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiPremixing.getKakuhansyuuryounichiji3(), "yyMMdd");

            // 撹拌終了時間③
            case GXHDO102B023Const.KAKUHANSYUURYOU3_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiPremixing.getKakuhansyuuryounichiji3(), "HHmm");

            // 投入⑤
            case GXHDO102B023Const.TOUNYUU5:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getTounyuu5());

            // 撹拌時間規格④
            case GXHDO102B023Const.KAKUHANJIKANKIKAKU4:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getKakuhanjikankikaku4());

            // 撹拌開始日④
            case GXHDO102B023Const.KAKUHANKAISI4_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiPremixing.getKakuhankaisinichiji4(), "yyMMdd");

            // 撹拌開始時間④
            case GXHDO102B023Const.KAKUHANKAISI4_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiPremixing.getKakuhankaisinichiji4(), "HHmm");

            // 撹拌終了日④
            case GXHDO102B023Const.KAKUHANSYUURYOU4_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiPremixing.getKakuhansyuuryounichiji4(), "yyMMdd");

            // 撹拌終了時間④
            case GXHDO102B023Const.KAKUHANSYUURYOU4_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiPremixing.getKakuhansyuuryounichiji4(), "HHmm");

            // 投入⑥
            case GXHDO102B023Const.TOUNYUU6:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getTounyuu6());

            // 撹拌時間規格⑤
            case GXHDO102B023Const.KAKUHANJIKANKIKAKU5:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getKakuhanjikankikaku5());

            // 撹拌開始日⑤
            case GXHDO102B023Const.KAKUHANKAISI5_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiPremixing.getKakuhankaisinichiji5(), "yyMMdd");

            // 撹拌開始時間⑤
            case GXHDO102B023Const.KAKUHANKAISI5_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiPremixing.getKakuhankaisinichiji5(), "HHmm");

            // 撹拌終了日⑤
            case GXHDO102B023Const.KAKUHANSYUURYOU5_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiPremixing.getKakuhansyuuryounichiji5(), "yyMMdd");

            // 撹拌終了時間⑤
            case GXHDO102B023Const.KAKUHANSYUURYOU5_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiPremixing.getKakuhansyuuryounichiji5(), "HHmm");

            // 回転数変更
            case GXHDO102B023Const.KAITENSUUHENKOU:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getKaitensuuhenkou());

            // 投入⑦
            case GXHDO102B023Const.TOUNYUU7:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getTounyuu7());

            // 投入⑧
            case GXHDO102B023Const.TOUNYUU8:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getTounyuu8());

            // 投入⑨
            case GXHDO102B023Const.TOUNYUU9:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getTounyuu9());

            // 撹拌時間規格⑥
            case GXHDO102B023Const.KAKUHANJIKANKIKAKU6:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getKakuhanjikankikaku6());

            // 撹拌開始日⑥
            case GXHDO102B023Const.KAKUHANKAISI6_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiPremixing.getKakuhankaisinichiji6(), "yyMMdd");

            // 撹拌開始時間⑥
            case GXHDO102B023Const.KAKUHANKAISI6_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiPremixing.getKakuhankaisinichiji6(), "HHmm");

            // 撹拌終了日⑥
            case GXHDO102B023Const.KAKUHANSYUURYOU6_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiPremixing.getKakuhansyuuryounichiji6(), "yyMMdd");

            // 撹拌終了時間⑥
            case GXHDO102B023Const.KAKUHANSYUURYOU6_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiPremixing.getKakuhansyuuryounichiji6(), "HHmm");

            // 投入⑩
            case GXHDO102B023Const.TOUNYUU10:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getTounyuu10());

            // 投入⑪
            case GXHDO102B023Const.TOUNYUU11:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getTounyuu11());

            // 投入⑫
            case GXHDO102B023Const.TOUNYUU12:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getTounyuu12());

            // 投入⑬
            case GXHDO102B023Const.TOUNYUU13:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getTounyuu13());

            // 投入⑭
            case GXHDO102B023Const.TOUNYUU14:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getTounyuu14());

            // 投入⑮
            case GXHDO102B023Const.TOUNYUU15:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getTounyuu15());

            // 投入⑯
            case GXHDO102B023Const.TOUNYUU16:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getTounyuu16());

            // 投入⑰
            case GXHDO102B023Const.TOUNYUU17:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getTounyuu17());

            // 投入⑱
            case GXHDO102B023Const.TOUNYUU18:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getTounyuu18());

            // 投入⑲
            case GXHDO102B023Const.TOUNYUU19:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getTounyuu19());

            // 投入終了日
            case GXHDO102B023Const.TOUNYUUSYUURYOU_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiPremixing.getTounyuusyuuryounichiji(), "yyMMdd");

            // 投入終了時間
            case GXHDO102B023Const.TOUNYUUSYUURYOU_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiPremixing.getTounyuusyuuryounichiji(), "HHmm");

            // 撹拌機
            case GXHDO102B023Const.KAKUHANKI:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getKakuhanki());

            // 回転数
            case GXHDO102B023Const.KAITENSUU:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getKaitensuu());

            // 撹拌時間規格⑦
            case GXHDO102B023Const.KAKUHANJIKANKIKAKU7:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getKakuhanjikankikaku7());

            // 撹拌開始日⑦
            case GXHDO102B023Const.KAKUHANKAISI7_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiPremixing.getKakuhankaisinichiji7(), "yyMMdd");

            // 撹拌開始時間⑦
            case GXHDO102B023Const.KAKUHANKAISI7_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiPremixing.getKakuhankaisinichiji7(), "HHmm");

            // 回転体への接触の確認
            case GXHDO102B023Const.KAITENTAIHESESYOKUKAKUNIN:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srYuudentaiPremixing.getKaitentaihenosessyokunokakunin()));

            // 撹拌終了日⑦
            case GXHDO102B023Const.KAKUHANSYUURYOU7_DAY:
                return DateUtil.formattedTimestamp(srYuudentaiPremixing.getKakuhansyuuryounichiji7(), "yyMMdd");

            // 撹拌終了時間⑦
            case GXHDO102B023Const.KAKUHANSYUURYOU7_TIME:
                return DateUtil.formattedTimestamp(srYuudentaiPremixing.getKakuhansyuuryounichiji7(), "HHmm");

            // 担当者
            case GXHDO102B023Const.TANTOUSYA:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getTantousya());

            // 備考1
            case GXHDO102B023Const.BIKOU1:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getBikou1());

            // 備考2
            case GXHDO102B023Const.BIKOU2:
                return StringUtil.nullToBlank(srYuudentaiPremixing.getBikou2());

            default:
                return null;
        }
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞ_仮登録(tmp_sr_yuudentai_premixing)登録処理(削除時)
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
    private void insertDeleteDataTmpSrYuudentaiPremixing(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_yuudentai_premixing ("
                + " kojyo,lotno,edaban,yuudentaislurryhinmei,yuudentaislurrylotno,lotkubun,genryoulotno,genryoukigou,youzaisenjyou_kakuhanki,"
                + "youzaisenjyou_siyoutank,youzairyou,youzaitounyuu,syuziku,pump,senjyoujyouken_dispakaitensuu,separate,agitator,"
                + "jidouunten,tankABjyunkan,passsuu,senjyoujikan,raptime,premixing_kakuhanki,premixing_siyoutank,earthgripsetuzokukakunin,"
                + "premixing_dispakaitensuu,tounyuukaisinichiji,tounyuu1,tounyuu2,kakuhanjikankikaku1,kakuhankaisinichiji1,kakuhansyuuryounichiji1"
                + ",tounyuu3,kakuhanjikankikaku2,kakuhankaisinichiji2,kakuhansyuuryounichiji2,tounyuu4,kakuhanjikankikaku3,kakuhankaisinichiji3,"
                + "kakuhansyuuryounichiji3,tounyuu5,kakuhanjikankikaku4,kakuhankaisinichiji4,kakuhansyuuryounichiji4,tounyuu6,kakuhanjikankikaku5,"
                + "kakuhankaisinichiji5,kakuhansyuuryounichiji5,kaitensuuhenkou,tounyuu7,tounyuu8,tounyuu9,kakuhanjikankikaku6,kakuhankaisinichiji6,"
                + "kakuhansyuuryounichiji6,tounyuu10,tounyuu11,tounyuu12,tounyuu13,tounyuu14,tounyuu15,tounyuu16,tounyuu17,tounyuu18,tounyuu19,"
                + "tounyuusyuuryounichiji,kakuhanki,kaitensuu,kakuhanjikankikaku7,kakuhankaisinichiji7,kaitentaihenosessyokunokakunin,kakuhansyuuryounichiji7,"
                + "tantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") SELECT "
                + " kojyo,lotno,edaban,yuudentaislurryhinmei,yuudentaislurrylotno,lotkubun,genryoulotno,genryoukigou,youzaisenjyou_kakuhanki,"
                + "youzaisenjyou_siyoutank,youzairyou,youzaitounyuu,syuziku,pump,senjyoujyouken_dispakaitensuu,separate,agitator,"
                + "jidouunten,tankABjyunkan,passsuu,senjyoujikan,raptime,premixing_kakuhanki,premixing_siyoutank,earthgripsetuzokukakunin,"
                + "premixing_dispakaitensuu,tounyuukaisinichiji,tounyuu1,tounyuu2,kakuhanjikankikaku1,kakuhankaisinichiji1,kakuhansyuuryounichiji1"
                + ",tounyuu3,kakuhanjikankikaku2,kakuhankaisinichiji2,kakuhansyuuryounichiji2,tounyuu4,kakuhanjikankikaku3,kakuhankaisinichiji3,"
                + "kakuhansyuuryounichiji3,tounyuu5,kakuhanjikankikaku4,kakuhankaisinichiji4,kakuhansyuuryounichiji4,tounyuu6,kakuhanjikankikaku5,"
                + "kakuhankaisinichiji5,kakuhansyuuryounichiji5,kaitensuuhenkou,tounyuu7,tounyuu8,tounyuu9,kakuhanjikankikaku6,kakuhankaisinichiji6,"
                + "kakuhansyuuryounichiji6,tounyuu10,tounyuu11,tounyuu12,tounyuu13,tounyuu14,tounyuu15,tounyuu16,tounyuu17,tounyuu18,tounyuu19,"
                + "tounyuusyuuryounichiji,kakuhanki,kaitensuu,kakuhanjikankikaku7,kakuhankaisinichiji7,kaitentaihenosessyokunokakunin,kakuhansyuuryounichiji7,"
                + "tantousya,bikou1,bikou2,?,?,?,? "
                + " FROM sr_yuudentai_premixing "
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
    private void initGXHDO102B023A(ProcessData processData) {
        GXHDO102B023A bean = (GXHDO102B023A) getFormBean("gXHDO102B023A");
        bean.setWiplotno(getItemRow(processData.getItemList(), GXHDO102B023Const.WIPLOTNO));
        bean.setYuudentaislurryhinmei(getItemRow(processData.getItemList(), GXHDO102B023Const.YUUDENTAISLURRYHINMEI));
        bean.setYuudentaislurrylotno(getItemRow(processData.getItemList(), GXHDO102B023Const.YUUDENTAISLURRYLOTNO));
        bean.setLotkubun(getItemRow(processData.getItemList(), GXHDO102B023Const.LOTKUBUN));
        bean.setGenryoulotno(getItemRow(processData.getItemList(), GXHDO102B023Const.GENRYOULOTNO));
        bean.setGenryoukigou(getItemRow(processData.getItemList(), GXHDO102B023Const.GENRYOUKIGOU));
        bean.setYouzaisenjyou_kakuhanki(getItemRow(processData.getItemList(), GXHDO102B023Const.YOUZAISENJYOU_KAKUHANKI));
        bean.setYouzaisenjyou_siyoutank(getItemRow(processData.getItemList(), GXHDO102B023Const.YOUZAISENJYOU_SIYOUTANK));
        bean.setYouzairyou(getItemRow(processData.getItemList(), GXHDO102B023Const.YOUZAIRYOU));
        bean.setYouzaitounyuu(getItemRow(processData.getItemList(), GXHDO102B023Const.YOUZAITOUNYUU));
        bean.setSyuziku(getItemRow(processData.getItemList(), GXHDO102B023Const.SYUZIKU));
        bean.setPump(getItemRow(processData.getItemList(), GXHDO102B023Const.PUMP));
        bean.setSjjyouken_dispakaitensuu(getItemRow(processData.getItemList(), GXHDO102B023Const.SJJYOUKEN_DISPAKAITENSUU));
        bean.setSeparate(getItemRow(processData.getItemList(), GXHDO102B023Const.SEPARATE));
        bean.setAgitator(getItemRow(processData.getItemList(), GXHDO102B023Const.AGITATOR));
        bean.setJidouunten(getItemRow(processData.getItemList(), GXHDO102B023Const.JIDOUUNTEN));
        bean.setTankabjyunkan(getItemRow(processData.getItemList(), GXHDO102B023Const.TANKABJYUNKAN));
        bean.setPasssuu(getItemRow(processData.getItemList(), GXHDO102B023Const.PASSSUU));
        bean.setSenjyoujikan(getItemRow(processData.getItemList(), GXHDO102B023Const.SENJYOUJIKAN));
        bean.setRaptime(getItemRow(processData.getItemList(), GXHDO102B023Const.RAPTIME));
        bean.setPremixing_kakuhanki(getItemRow(processData.getItemList(), GXHDO102B023Const.PREMIXING_KAKUHANKI));
        bean.setPremixing_siyoutank(getItemRow(processData.getItemList(), GXHDO102B023Const.PREMIXING_SIYOUTANK));
        bean.setEarthgripsetuzokukakunin(getItemRow(processData.getItemList(), GXHDO102B023Const.EARTHGRIPSETUZOKUKAKUNIN));
        bean.setPremixing_dispakaitensuu(getItemRow(processData.getItemList(), GXHDO102B023Const.PREMIXING_DISPAKAITENSUU));
        bean.setTounyuukaisi_day(getItemRow(processData.getItemList(), GXHDO102B023Const.TOUNYUUKAISI_DAY));
        bean.setTounyuukaisi_time(getItemRow(processData.getItemList(), GXHDO102B023Const.TOUNYUUKAISI_TIME));
        bean.setTounyuu1(getItemRow(processData.getItemList(), GXHDO102B023Const.TOUNYUU1));
        bean.setTounyuu2(getItemRow(processData.getItemList(), GXHDO102B023Const.TOUNYUU2));
        bean.setKakuhanjikankikaku1(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANJIKANKIKAKU1));
        bean.setKakuhankaisi1_day(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI1_DAY));
        bean.setKakuhankaisi1_time(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI1_TIME));
        bean.setKakuhansyuuryou1_day(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU1_DAY));
        bean.setKakuhansyuuryou1_time(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU1_TIME));
        bean.setTounyuu3(getItemRow(processData.getItemList(), GXHDO102B023Const.TOUNYUU3));
        bean.setKakuhanjikankikaku2(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANJIKANKIKAKU2));
        bean.setKakuhankaisi2_day(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI2_DAY));
        bean.setKakuhankaisi2_time(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI2_TIME));
        bean.setKakuhansyuuryou2_day(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU2_DAY));
        bean.setKakuhansyuuryou2_time(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU2_TIME));
        bean.setTounyuu4(getItemRow(processData.getItemList(), GXHDO102B023Const.TOUNYUU4));
        bean.setKakuhanjikankikaku3(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANJIKANKIKAKU3));
        bean.setKakuhankaisi3_day(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI3_DAY));
        bean.setKakuhankaisi3_time(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI3_TIME));
        bean.setKakuhansyuuryou3_day(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU3_DAY));
        bean.setKakuhansyuuryou3_time(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU3_TIME));
        bean.setTounyuu5(getItemRow(processData.getItemList(), GXHDO102B023Const.TOUNYUU5));
        bean.setKakuhanjikankikaku4(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANJIKANKIKAKU4));
        bean.setKakuhankaisi4_day(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI4_DAY));
        bean.setKakuhankaisi4_time(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI4_TIME));
        bean.setKakuhansyuuryou4_day(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU4_DAY));
        bean.setKakuhansyuuryou4_time(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU4_TIME));
        bean.setTounyuu6(getItemRow(processData.getItemList(), GXHDO102B023Const.TOUNYUU6));
        bean.setKakuhanjikankikaku5(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANJIKANKIKAKU5));
        bean.setKakuhankaisi5_day(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI5_DAY));
        bean.setKakuhankaisi5_time(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI5_TIME));
        bean.setKakuhansyuuryou5_day(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU5_DAY));
        bean.setKakuhansyuuryou5_time(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU5_TIME));
        bean.setKaitensuuhenkou(getItemRow(processData.getItemList(), GXHDO102B023Const.KAITENSUUHENKOU));
        bean.setTounyuu7(getItemRow(processData.getItemList(), GXHDO102B023Const.TOUNYUU7));
        bean.setTounyuu8(getItemRow(processData.getItemList(), GXHDO102B023Const.TOUNYUU8));
        bean.setTounyuu9(getItemRow(processData.getItemList(), GXHDO102B023Const.TOUNYUU9));
        bean.setKakuhanjikankikaku6(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANJIKANKIKAKU6));
        bean.setKakuhankaisi6_day(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI6_DAY));
        bean.setKakuhankaisi6_time(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI6_TIME));
        bean.setKakuhansyuuryou6_day(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU6_DAY));
        bean.setKakuhansyuuryou6_time(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU6_TIME));
        bean.setTounyuu10(getItemRow(processData.getItemList(), GXHDO102B023Const.TOUNYUU10));
        bean.setTounyuu11(getItemRow(processData.getItemList(), GXHDO102B023Const.TOUNYUU11));
        bean.setTounyuu12(getItemRow(processData.getItemList(), GXHDO102B023Const.TOUNYUU12));
        bean.setTounyuu13(getItemRow(processData.getItemList(), GXHDO102B023Const.TOUNYUU13));
        bean.setTounyuu14(getItemRow(processData.getItemList(), GXHDO102B023Const.TOUNYUU14));
        bean.setTounyuu15(getItemRow(processData.getItemList(), GXHDO102B023Const.TOUNYUU15));
        bean.setTounyuu16(getItemRow(processData.getItemList(), GXHDO102B023Const.TOUNYUU16));
        bean.setTounyuu17(getItemRow(processData.getItemList(), GXHDO102B023Const.TOUNYUU17));
        bean.setTounyuu18(getItemRow(processData.getItemList(), GXHDO102B023Const.TOUNYUU18));
        bean.setTounyuu19(getItemRow(processData.getItemList(), GXHDO102B023Const.TOUNYUU19));
        bean.setTounyuusyuuryou_day(getItemRow(processData.getItemList(), GXHDO102B023Const.TOUNYUUSYUURYOU_DAY));
        bean.setTounyuusyuuryou_time(getItemRow(processData.getItemList(), GXHDO102B023Const.TOUNYUUSYUURYOU_TIME));
        bean.setKakuhanki(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKI));
        bean.setKaitensuu(getItemRow(processData.getItemList(), GXHDO102B023Const.KAITENSUU));
        bean.setKakuhanjikankikaku7(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANJIKANKIKAKU7));
        bean.setKakuhankaisi7_day(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI7_DAY));
        bean.setKakuhankaisi7_time(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANKAISI7_TIME));
        bean.setKaitentaihesesyokukakunin(getItemRow(processData.getItemList(), GXHDO102B023Const.KAITENTAIHESESYOKUKAKUNIN));
        bean.setKakuhansyuuryou7_day(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU7_DAY));
        bean.setKakuhansyuuryou7_time(getItemRow(processData.getItemList(), GXHDO102B023Const.KAKUHANSYUURYOU7_TIME));
        bean.setTantousya(getItemRow(processData.getItemList(), GXHDO102B023Const.TANTOUSYA));
        bean.setBikou1(getItemRow(processData.getItemList(), GXHDO102B023Const.BIKOU1));
        bean.setBikou2(getItemRow(processData.getItemList(), GXHDO102B023Const.BIKOU2));

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
        allItemIdMap.put(GXHDO102B023Const.WIPLOTNO, "WIPﾛｯﾄNo");
        allItemIdMap.put(GXHDO102B023Const.YUUDENTAISLURRYHINMEI, "誘電体ｽﾗﾘｰ品名");
        allItemIdMap.put(GXHDO102B023Const.YUUDENTAISLURRYLOTNO, "誘電体ｽﾗﾘｰLotNo");
        allItemIdMap.put(GXHDO102B023Const.LOTKUBUN, "ﾛｯﾄ区分");
        allItemIdMap.put(GXHDO102B023Const.GENRYOULOTNO, "原料LotNo");
        allItemIdMap.put(GXHDO102B023Const.GENRYOUKIGOU, "原料記号");
        allItemIdMap.put(GXHDO102B023Const.YOUZAISENJYOU_KAKUHANKI, "溶剤洗浄_撹拌機");
        allItemIdMap.put(GXHDO102B023Const.YOUZAISENJYOU_SIYOUTANK, "溶剤洗浄_使用ﾀﾝｸ");
        allItemIdMap.put(GXHDO102B023Const.YOUZAIRYOU, "溶剤量");
        allItemIdMap.put(GXHDO102B023Const.YOUZAITOUNYUU, "溶剤投入");
        allItemIdMap.put(GXHDO102B023Const.SYUZIKU, "主軸");
        allItemIdMap.put(GXHDO102B023Const.PUMP, "ﾎﾟﾝﾌﾟ");
        allItemIdMap.put(GXHDO102B023Const.SJJYOUKEN_DISPAKAITENSUU, "洗浄条件_ﾃﾞｨｽﾊﾟ回転数");
        allItemIdMap.put(GXHDO102B023Const.SEPARATE, "ｾﾊﾟﾚｰﾀ");
        allItemIdMap.put(GXHDO102B023Const.AGITATOR, "ｱｼﾞﾃｰﾀ");
        allItemIdMap.put(GXHDO102B023Const.JIDOUUNTEN, "自動運転");
        allItemIdMap.put(GXHDO102B023Const.TANKABJYUNKAN, "ﾀﾝｸAB循環");
        allItemIdMap.put(GXHDO102B023Const.PASSSUU, "ﾊﾟｽ数");
        allItemIdMap.put(GXHDO102B023Const.SENJYOUJIKAN, "洗浄時間");
        allItemIdMap.put(GXHDO102B023Const.RAPTIME, "ﾗｯﾌﾟﾀｲﾑ");
        allItemIdMap.put(GXHDO102B023Const.PREMIXING_KAKUHANKI, "ﾌﾟﾚﾐｷ_撹拌機");
        allItemIdMap.put(GXHDO102B023Const.PREMIXING_SIYOUTANK, "ﾌﾟﾚﾐｷ_使用ﾀﾝｸ");
        allItemIdMap.put(GXHDO102B023Const.EARTHGRIPSETUZOKUKAKUNIN, "ｱｰｽｸﾞﾘｯﾌﾟ接続確認");
        allItemIdMap.put(GXHDO102B023Const.PREMIXING_DISPAKAITENSUU, "ﾌﾟﾚﾐｷ_ﾃﾞｨｽﾊﾟ回転数");
        allItemIdMap.put(GXHDO102B023Const.TOUNYUUKAISI_DAY, "投入開始日");
        allItemIdMap.put(GXHDO102B023Const.TOUNYUUKAISI_TIME, "投入開始時間");
        allItemIdMap.put(GXHDO102B023Const.TOUNYUU1, "投入①");
        allItemIdMap.put(GXHDO102B023Const.TOUNYUU2, "投入②");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANJIKANKIKAKU1, "撹拌時間規格①");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANKAISI1_DAY, "撹拌開始日①");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANKAISI1_TIME, "撹拌開始時間①");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANSYUURYOU1_DAY, "撹拌終了日①");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANSYUURYOU1_TIME, "撹拌終了時間①");
        allItemIdMap.put(GXHDO102B023Const.TOUNYUU3, "投入③");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANJIKANKIKAKU2, "撹拌時間規格②");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANKAISI2_DAY, "撹拌開始日②");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANKAISI2_TIME, "撹拌開始時間②");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANSYUURYOU2_DAY, "撹拌終了日②");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANSYUURYOU2_TIME, "撹拌終了時間②");
        allItemIdMap.put(GXHDO102B023Const.TOUNYUU4, "投入④");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANJIKANKIKAKU3, "撹拌時間規格③");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANKAISI3_DAY, "撹拌開始日③");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANKAISI3_TIME, "撹拌開始時間③");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANSYUURYOU3_DAY, "撹拌終了日③");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANSYUURYOU3_TIME, "撹拌終了時間③");
        allItemIdMap.put(GXHDO102B023Const.TOUNYUU5, "投入⑤");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANJIKANKIKAKU4, "撹拌時間規格④");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANKAISI4_DAY, "撹拌開始日④");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANKAISI4_TIME, "撹拌開始時間④");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANSYUURYOU4_DAY, "撹拌終了日④");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANSYUURYOU4_TIME, "撹拌終了時間④");
        allItemIdMap.put(GXHDO102B023Const.TOUNYUU6, "投入⑥");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANJIKANKIKAKU5, "撹拌時間規格⑤");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANKAISI5_DAY, "撹拌開始日⑤");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANKAISI5_TIME, "撹拌開始時間⑤");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANSYUURYOU5_DAY, "撹拌終了日⑤");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANSYUURYOU5_TIME, "撹拌終了時間⑤");
        allItemIdMap.put(GXHDO102B023Const.KAITENSUUHENKOU, "回転数変更");
        allItemIdMap.put(GXHDO102B023Const.TOUNYUU7, "投入⑦");
        allItemIdMap.put(GXHDO102B023Const.TOUNYUU8, "投入⑧");
        allItemIdMap.put(GXHDO102B023Const.TOUNYUU9, "投入⑨");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANJIKANKIKAKU6, "撹拌時間規格⑥");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANKAISI6_DAY, "撹拌開始日⑥");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANKAISI6_TIME, "撹拌開始時間⑥");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANSYUURYOU6_DAY, "撹拌終了日⑥");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANSYUURYOU6_TIME, "撹拌終了時間⑥");
        allItemIdMap.put(GXHDO102B023Const.TOUNYUU10, "投入⑩");
        allItemIdMap.put(GXHDO102B023Const.TOUNYUU11, "投入⑪");
        allItemIdMap.put(GXHDO102B023Const.TOUNYUU12, "投入⑫");
        allItemIdMap.put(GXHDO102B023Const.TOUNYUU13, "投入⑬");
        allItemIdMap.put(GXHDO102B023Const.TOUNYUU14, "投入⑭");
        allItemIdMap.put(GXHDO102B023Const.TOUNYUU15, "投入⑮");
        allItemIdMap.put(GXHDO102B023Const.TOUNYUU16, "投入⑯");
        allItemIdMap.put(GXHDO102B023Const.TOUNYUU17, "投入⑰");
        allItemIdMap.put(GXHDO102B023Const.TOUNYUU18, "投入⑱");
        allItemIdMap.put(GXHDO102B023Const.TOUNYUU19, "投入⑲");
        allItemIdMap.put(GXHDO102B023Const.TOUNYUUSYUURYOU_DAY, "投入終了日");
        allItemIdMap.put(GXHDO102B023Const.TOUNYUUSYUURYOU_TIME, "投入終了時間");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANKI, "撹拌機");
        allItemIdMap.put(GXHDO102B023Const.KAITENSUU, "回転数");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANJIKANKIKAKU7, "撹拌時間規格⑦");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANKAISI7_DAY, "撹拌開始日⑦");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANKAISI7_TIME, "撹拌開始時間⑦");
        allItemIdMap.put(GXHDO102B023Const.KAITENTAIHESESYOKUKAKUNIN, "回転体への接触の確認");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANSYUURYOU7_DAY, "撹拌終了日⑦");
        allItemIdMap.put(GXHDO102B023Const.KAKUHANSYUURYOU7_TIME, "撹拌終了時間⑦");
        allItemIdMap.put(GXHDO102B023Const.TANTOUSYA, "担当者");
        allItemIdMap.put(GXHDO102B023Const.BIKOU1, "備考1");
        allItemIdMap.put(GXHDO102B023Const.BIKOU2, "備考2");

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
        Map fxhbm03Data = loadFxhbm03Data(queryRunnerDoc, "誘電体ｽﾗﾘｰ作製_ﾌﾟﾚﾐｷｼﾝｸﾞ_表示制御");
        // 画面非表示項目リスト
        List<String> notShowItemList = Arrays.asList(GXHDO102B023Const.YOUZAISENJYOU_KAKUHANKI, GXHDO102B023Const.YOUZAISENJYOU_SIYOUTANK, GXHDO102B023Const.YOUZAIRYOU, GXHDO102B023Const.YOUZAITOUNYUU,
                GXHDO102B023Const.SYUZIKU, GXHDO102B023Const.PUMP, GXHDO102B023Const.SJJYOUKEN_DISPAKAITENSUU, GXHDO102B023Const.SEPARATE,
                GXHDO102B023Const.AGITATOR, GXHDO102B023Const.JIDOUUNTEN, GXHDO102B023Const.TANKABJYUNKAN, GXHDO102B023Const.PASSSUU, GXHDO102B023Const.SENJYOUJIKAN,
                GXHDO102B023Const.RAPTIME, GXHDO102B023Const.PREMIXING_KAKUHANKI, GXHDO102B023Const.PREMIXING_SIYOUTANK, GXHDO102B023Const.EARTHGRIPSETUZOKUKAKUNIN,
                GXHDO102B023Const.PREMIXING_DISPAKAITENSUU, GXHDO102B023Const.TOUNYUU1, GXHDO102B023Const.TOUNYUU2, GXHDO102B023Const.KAKUHANJIKANKIKAKU1,
                GXHDO102B023Const.KAKUHANKAISI1_DAY, GXHDO102B023Const.KAKUHANKAISI1_TIME, GXHDO102B023Const.KAKUHANSYUURYOU1_DAY, GXHDO102B023Const.KAKUHANSYUURYOU1_TIME,
                GXHDO102B023Const.TOUNYUU3, GXHDO102B023Const.KAKUHANJIKANKIKAKU2, GXHDO102B023Const.KAKUHANKAISI2_DAY, GXHDO102B023Const.KAKUHANKAISI2_TIME,
                GXHDO102B023Const.KAKUHANSYUURYOU2_DAY, GXHDO102B023Const.KAKUHANSYUURYOU2_TIME, GXHDO102B023Const.TOUNYUU4, GXHDO102B023Const.KAKUHANJIKANKIKAKU3,
                GXHDO102B023Const.KAKUHANKAISI3_DAY, GXHDO102B023Const.KAKUHANKAISI3_TIME, GXHDO102B023Const.KAKUHANSYUURYOU3_DAY, GXHDO102B023Const.KAKUHANSYUURYOU3_TIME,
                GXHDO102B023Const.TOUNYUU5, GXHDO102B023Const.KAKUHANJIKANKIKAKU4, GXHDO102B023Const.KAKUHANKAISI4_DAY, GXHDO102B023Const.KAKUHANKAISI4_TIME,
                GXHDO102B023Const.KAKUHANSYUURYOU4_DAY, GXHDO102B023Const.KAKUHANSYUURYOU4_TIME, GXHDO102B023Const.TOUNYUU6, GXHDO102B023Const.KAKUHANJIKANKIKAKU5,
                GXHDO102B023Const.KAKUHANKAISI5_DAY, GXHDO102B023Const.KAKUHANKAISI5_TIME, GXHDO102B023Const.KAKUHANSYUURYOU5_DAY, GXHDO102B023Const.KAKUHANSYUURYOU5_TIME,
                GXHDO102B023Const.KAITENSUUHENKOU, GXHDO102B023Const.TOUNYUU7, GXHDO102B023Const.TOUNYUU8, GXHDO102B023Const.TOUNYUU9,
                GXHDO102B023Const.KAKUHANJIKANKIKAKU6, GXHDO102B023Const.KAKUHANKAISI6_DAY, GXHDO102B023Const.KAKUHANKAISI6_TIME, GXHDO102B023Const.KAKUHANSYUURYOU6_DAY,
                GXHDO102B023Const.KAKUHANSYUURYOU6_TIME, GXHDO102B023Const.TOUNYUU10, GXHDO102B023Const.TOUNYUU11, GXHDO102B023Const.TOUNYUU12,
                GXHDO102B023Const.TOUNYUU13, GXHDO102B023Const.TOUNYUU14, GXHDO102B023Const.TOUNYUU15, GXHDO102B023Const.TOUNYUU16,
                GXHDO102B023Const.TOUNYUU17, GXHDO102B023Const.TOUNYUU18, GXHDO102B023Const.TOUNYUU19, GXHDO102B023Const.KAKUHANKI,
                GXHDO102B023Const.KAITENSUU, GXHDO102B023Const.KAKUHANJIKANKIKAKU7, GXHDO102B023Const.KAKUHANKAISI7_DAY, GXHDO102B023Const.KAKUHANKAISI7_TIME,
                GXHDO102B023Const.KAITENTAIHESESYOKUKAKUNIN, GXHDO102B023Const.KAKUHANSYUURYOU7_DAY, GXHDO102B023Const.KAKUHANSYUURYOU7_TIME);
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
        GXHDO102B023A bean = (GXHDO102B023A) getFormBean("gXHDO102B023A");
        // 以下の項目を画面非表示にする。
        notShowItemList.forEach((notShowItem) -> {
            itemList.remove(getItemRow(itemList, notShowItem));
        });
        bean.setYouzaisenjyou_kakuhanki(null);
        bean.setYouzaisenjyou_siyoutank(null);
        bean.setYouzairyou(null);
        bean.setYouzaitounyuu(null);
        bean.setSyuziku(null);
        bean.setPump(null);
        bean.setSjjyouken_dispakaitensuu(null);
        bean.setSeparate(null);
        bean.setAgitator(null);
        bean.setJidouunten(null);
        bean.setTankabjyunkan(null);
        bean.setPasssuu(null);
        bean.setSenjyoujikan(null);
        bean.setRaptime(null);
        bean.setPremixing_kakuhanki(null);
        bean.setPremixing_siyoutank(null);
        bean.setEarthgripsetuzokukakunin(null);
        bean.setPremixing_dispakaitensuu(null);
        bean.setTounyuu1(null);
        bean.setTounyuu2(null);
        bean.setKakuhanjikankikaku1(null);
        bean.setKakuhankaisi1_day(null);
        bean.setKakuhankaisi1_time(null);
        bean.setKakuhansyuuryou1_day(null);
        bean.setKakuhansyuuryou1_time(null);
        bean.setTounyuu3(null);
        bean.setKakuhanjikankikaku2(null);
        bean.setKakuhankaisi2_day(null);
        bean.setKakuhankaisi2_time(null);
        bean.setKakuhansyuuryou2_day(null);
        bean.setKakuhansyuuryou2_time(null);
        bean.setTounyuu4(null);
        bean.setKakuhanjikankikaku3(null);
        bean.setKakuhankaisi3_day(null);
        bean.setKakuhankaisi3_time(null);
        bean.setKakuhansyuuryou3_day(null);
        bean.setKakuhansyuuryou3_time(null);
        bean.setTounyuu5(null);
        bean.setKakuhanjikankikaku4(null);
        bean.setKakuhankaisi4_day(null);
        bean.setKakuhankaisi4_time(null);
        bean.setKakuhansyuuryou4_day(null);
        bean.setKakuhansyuuryou4_time(null);
        bean.setTounyuu6(null);
        bean.setKakuhanjikankikaku5(null);
        bean.setKakuhankaisi5_day(null);
        bean.setKakuhankaisi5_time(null);
        bean.setKakuhansyuuryou5_day(null);
        bean.setKakuhansyuuryou5_time(null);
        bean.setKaitensuuhenkou(null);
        bean.setTounyuu7(null);
        bean.setTounyuu8(null);
        bean.setTounyuu9(null);
        bean.setKakuhanjikankikaku6(null);
        bean.setKakuhankaisi6_day(null);
        bean.setKakuhankaisi6_time(null);
        bean.setKakuhansyuuryou6_day(null);
        bean.setKakuhansyuuryou6_time(null);
        bean.setTounyuu10(null);
        bean.setTounyuu11(null);
        bean.setTounyuu12(null);
        bean.setTounyuu13(null);
        bean.setTounyuu14(null);
        bean.setTounyuu15(null);
        bean.setTounyuu16(null);
        bean.setTounyuu17(null);
        bean.setTounyuu18(null);
        bean.setTounyuu19(null);
        bean.setKakuhanki(null);
        bean.setKaitensuu(null);
        bean.setKakuhanjikankikaku7(null);
        bean.setKakuhankaisi7_day(null);
        bean.setKakuhankaisi7_time(null);
        bean.setKaitentaihesesyokukakunin(null);
        bean.setKakuhansyuuryou7_day(null);
        bean.setKakuhansyuuryou7_time(null);
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
