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
import jp.co.kccs.xhd.db.model.SrSlipKouatsubunsan;
import jp.co.kccs.xhd.db.model.SubSrSlipKouatsubunsan;
import jp.co.kccs.xhd.model.GXHDO102B034Model;
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

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/12/20<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B034(ｽﾘｯﾌﾟ作製・高圧分散)
 *
 * @author KCSS K.Jo
 * @since 2021/12/20
 */
public class GXHDO102B034 implements IFormLogic {

    private static final Logger LOGGER = Logger.getLogger(GXHDO102B034.class.getName());
    private static final String JOTAI_FLG_KARI_TOROKU = "0";
    private static final String JOTAI_FLG_TOROKUZUMI = "1";
    private static final String JOTAI_FLG_SAKUJO = "9";
    private static final String SQL_STATE_RECORD_LOCK_ERR = "55P03";

    /**
     * コンストラクタ
     */
    public GXHDO102B034() {
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

            // 「ﾊﾟｽ」初期設定
            initGXHDO102B034B(processData);
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
                    GXHDO102B034Const.BTN_EDABAN_COPY_TOP,
                    GXHDO102B034Const.BTN_EDABAN_COPY_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO102B034Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO102B034Const.BTN_INSERT_TOP,
                    GXHDO102B034Const.BTN_DELETE_TOP,
                    GXHDO102B034Const.BTN_UPDATE_TOP,
                    GXHDO102B034Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO102B034Const.BTN_INSERT_BOTTOM,
                    GXHDO102B034Const.BTN_DELETE_BOTTOM,
                    GXHDO102B034Const.BTN_UPDATE_BOTTOM
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
        } catch (CloneNotSupportedException ex) {
            ErrUtil.outputErrorLog("CloneNotSupportedException発生", ex, LOGGER);
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
            case GXHDO102B034Const.BTN_EDABAN_COPY_TOP:
            case GXHDO102B034Const.BTN_EDABAN_COPY_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 仮登録
            case GXHDO102B034Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO102B034Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO102B034Const.BTN_INSERT_TOP:
            case GXHDO102B034Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 修正
            case GXHDO102B034Const.BTN_UPDATE_TOP:
            case GXHDO102B034Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO102B034Const.BTN_DELETE_TOP:
            case GXHDO102B034Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
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

            // ｽﾘｯﾌﾟ作製・高圧分散の入力項目の登録データ(仮登録時は仮登録データ)を取得
            List<SrSlipKouatsubunsan> srSlipKouatsubunsanDataList = getSrSlipKouatsubunsanData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo9, oyalotEdaban, paramJissekino);
            if (srSlipKouatsubunsanDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }
            // ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞ画面データ取得
            List<SubSrSlipKouatsubunsan> subSrSlipKouatsubunsanDataList = getSubSrSlipKouatsubunsanData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo9, oyalotEdaban, paramJissekino);
            if (subSrSlipKouatsubunsanDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }
            // メイン画面データ設定
            setInputItemDataMainForm(processData, srSlipKouatsubunsanDataList.get(0));
            // ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞ画面データ設定
            setInputItemPassListDataMainForm(subSrSlipKouatsubunsanDataList);

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

                // ｽﾘｯﾌﾟ作製・高圧分散_仮登録処理
                insertTmpSrSlipKouatsubunsan(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, paramJissekino, strSystime, processData);
                // ｽﾘｯﾌﾟ作製・高圧分散入力ｻﾌﾞ画面の仮登録処理
                insertTmpSubSrSlipKouatsubunsanDataList(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, paramJissekino, strSystime);
            } else {

                // ｽﾘｯﾌﾟ作製・高圧分散_仮登録更新処理
                updateTmpSrSlipKouatsubunsan(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, paramJissekino, strSystime, processData);
                // ｽﾘｯﾌﾟ作製・高圧分散入力ｻﾌﾞ画面の仮登録更新処理
                deleteAndInsertTmpSubSrSlipKouatsubunsanDataList(queryRunnerQcdb, conQcdb, rev, newRev, 0, kojyo, lotNo9, edaban, paramJissekino, strSystime);
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
        processData.getItemList().stream().filter(
                (fxhdd01) -> !(StringUtil.isEmpty(fxhdd01.getStandardPattern()) || "【-】".equals(fxhdd01.getKikakuChi()))).filter(
                        (fxhdd01) -> !(!ValidateUtil.isInputColumn(fxhdd01) || StringUtil.isEmpty(fxhdd01.getValue()))).forEachOrdered(
                        (fxhdd01) -> {
                            // 規格チェックの対象項目である、かつ入力項目かつ入力値がある項目はリストに追加
                            itemList.add(fxhdd01);
                        });
        GXHDO102B034B bean = (GXHDO102B034B) getFormBean("beanGXHDO102B034B");
        List<GXHDO102B034Model> listdata = bean.getPasslistdata();
        FXHDD01 jitsuatsuryoku = bean.getJitsuatsuryoku();
        for (int i = 0; i < listdata.size(); i++) {
            GXHDO102B034Model gxhdo102b034model = listdata.get(i);
            // 実圧力(最大値)
            FXHDD01 jitsuatsuryokuItem = gxhdo102b034model.getJitsuatsuryoku();
            // 設定圧力
            FXHDD01 setteiatsuryokuItem = gxhdo102b034model.getSetteiatsuryoku();
            jitsuatsuryokuItem.setKikakuChi(setteiatsuryokuItem.getKikakuChi());
            jitsuatsuryokuItem.setStandardPattern(setteiatsuryokuItem.getStandardPattern());
            itemList.add(jitsuatsuryokuItem);
        }
        ErrorMessageInfo errorMessageInfo = ValidateUtil.checkInputKikakuchi(itemList, kikakuchiInputErrorInfoList);

        // エラー項目の背景色を設定
        if (errorMessageInfo == null) {
            kikakuchiInputErrorInfoList.stream().filter((kikakuchiinputerrorinfo)
                    -> (jitsuatsuryoku.getItemId().equals(kikakuchiinputerrorinfo.getItemId()))
            ).map((kikakuchiinputerrorinfo) -> {
                String itemId = kikakuchiinputerrorinfo.getItemId();
                int pass = Integer.parseInt(kikakuchiinputerrorinfo.getItemLabel().substring(0, kikakuchiinputerrorinfo.getItemLabel().indexOf("行目"))) - 1;
                GXHDO102B034Model gxhdo102b034model = listdata.get(pass);
                FXHDD01 item = gxhdo102b034model.getJitsuatsuryoku();
                return item;
            }).forEachOrdered((item) -> {
                item.setBackColorInput(ErrUtil.ERR_BACK_COLOR);
            });
        } else {
            String itemId = errorMessageInfo.getErrorItemInfoList().get(0).getItemId();
            if(itemId.equals(jitsuatsuryoku.getItemId())){
                errorMessageInfo.setPageChangeItemIndex(-1);
                int pass = Integer.parseInt(errorMessageInfo.getErrorMessage().substring(0, errorMessageInfo.getErrorMessage().indexOf("行目"))) - 1;
                GXHDO102B034Model gxhdo102b034model = listdata.get(pass);
                gxhdo102b034model.getJitsuatsuryoku().setBackColorInput(ErrUtil.ERR_BACK_COLOR);
            }
        }
        return errorMessageInfo;
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
        // ﾁｪｯｸﾎﾞｯｸｽがすべてﾁｪｯｸされているかﾁｪｯｸ：排出容器内袋確認、排出容器洗浄確認、設備内洗浄確認、ﾀﾝｸにｱｰｽｸﾞﾘｯﾌﾟ接続
        List<String> itemIdList = Arrays.asList(GXHDO102B034Const.HAISYUTSUYOUKIUCHIBUKURO, GXHDO102B034Const.HAISYUTSUYOUKISENJYOU, GXHDO102B034Const.SETSUBINAISENJYOU, GXHDO102B034Const.TANKEARTHGRIPSETSUZOKU);
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
            SrSlipKouatsubunsan tmpSrSlipKouatsubunsan = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrSlipKouatsubunsan> srSlipKouatsubunsanList = getSrSlipKouatsubunsanData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo9, edaban, paramJissekino);
                if (!srSlipKouatsubunsanList.isEmpty()) {
                    tmpSrSlipKouatsubunsan = srSlipKouatsubunsanList.get(0);
                }

                deleteTmpSrSlipKouatsubunsan(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban, paramJissekino);
                deleteTmpSubSrSlipKouatsubunsan(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban, paramJissekino);
            }

            // ｽﾘｯﾌﾟ作製・高圧分散_登録処理
            insertSrSlipKouatsubunsan(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, paramJissekino, strSystime, processData, tmpSrSlipKouatsubunsan);
            // ｽﾘｯﾌﾟ作製・高圧分散入力ｻﾌﾞ画面の登録処理
            insertSubSrSlipKouatsubunsanDataList(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, paramJissekino, strSystime);
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
        processData.setUserAuthParam(GXHDO102B034Const.USER_AUTH_UPDATE_PARAM);

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

            // ｽﾘｯﾌﾟ作製・高圧分散_更新処理
            updateSrSlipKouatsubunsan(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, paramJissekino, strSystime, processData);
            // ｽﾘｯﾌﾟ作製・高圧分散入力ｻﾌﾞ画面の更新処理
            deleteAndInsertSubSrSlipKouatsubunsanDataList(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, paramJissekino, strSystime);
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
        processData.setUserAuthParam(GXHDO102B034Const.USER_AUTH_DELETE_PARAM);

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

            // ｽﾘｯﾌﾟ作製・高圧分散_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo9, edaban, paramJissekino);
            insertDeleteDataTmpSrSlipKouatsubunsan(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo9, edaban, paramJissekino, strSystime);

            // ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞ画面仮登録登録処理
            insertDeleteDataTmpSubSrSlipKouatsubunsan(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo9, edaban, paramJissekino, strSystime);

            // ｽﾘｯﾌﾟ作製・高圧分散_削除処理
            deleteSrSlipKouatsubunsan(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban, paramJissekino);

            // ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞ画面削除処理
            deleteSubSrSlipKouatsubunsan(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban, paramJissekino);

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
                        GXHDO102B034Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B034Const.BTN_UPDATE_TOP,
                        GXHDO102B034Const.BTN_DELETE_TOP,
                        GXHDO102B034Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B034Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B034Const.BTN_DELETE_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B034Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B034Const.BTN_INSERT_TOP,
                        GXHDO102B034Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B034Const.BTN_INSERT_BOTTOM));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO102B034Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B034Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B034Const.BTN_INSERT_TOP,
                        GXHDO102B034Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B034Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B034Const.BTN_INSERT_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B034Const.BTN_UPDATE_TOP,
                        GXHDO102B034Const.BTN_DELETE_TOP,
                        GXHDO102B034Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B034Const.BTN_DELETE_BOTTOM
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
    private ProcessData setInitData(ProcessData processData) throws SQLException, CloneNotSupportedException {

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
        HashMap<String, String> passMap = new HashMap<>();
        // 規格値(ﾊﾟｽ回数)取得
        getPassValue(queryRunnerQcdb, shikakariData, lotNo, passMap);
        if (!StringUtil.isEmpty(StringUtil.nullToBlank(passMap.get("errorMessage")))) {
            errorMessageList.clear();
            errorMessageList.add(passMap.get("errorMessage"));
            processData.setFatalError(true);
            processData.setInitMessageList(errorMessageList);
            return processData;
        }

        // 入力項目の情報を画面にセットする。
        if (!setInputItemData(processData, queryRunnerDoc, queryRunnerQcdb, lotNo, formId, paramJissekino, Integer.parseInt(passMap.get("pass")))) {
            // エラー発生時は処理を中断
            processData.setFatalError(true);
            processData.setInitMessageList(Arrays.asList(MessageUtil.getMessage("XHD-000038")));
            return processData;
        }

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
        this.setItemData(processData, GXHDO102B034Const.WIPLOTNO, lotNo);
        // ｽﾘｯﾌﾟ品名
        this.setItemData(processData, GXHDO102B034Const.SLIPHINMEI, StringUtil.nullToBlank(getMapData(shikakariData, "hinmei")));
        // ｽﾘｯﾌﾟLotNo
        this.setItemData(processData, GXHDO102B034Const.SLIPLOTNO, StringUtil.nullToBlank(getMapData(shikakariData, "lotno")));
        // ﾛｯﾄ区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode"));
        // ﾛｯﾄ区分名称
        String lotkubun = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubun"));

        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO102B034Const.LOTKUBUN, "");
        } else {
            if (!StringUtil.isEmpty(lotkubun)) {
                lotkubuncode = lotkubuncode + ":" + lotkubun;
            }
            this.setItemData(processData, GXHDO102B034Const.LOTKUBUN, lotkubuncode);
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
     * @param pass ﾊﾟｽ回数
     * @return 設定結果(失敗時false)
     * @throws SQLException 例外エラー
     */
    private boolean setInputItemData(ProcessData processData, QueryRunner queryRunnerDoc, QueryRunner queryRunnerQcdb,
            String lotNo, String formId, int jissekino, int pass) throws SQLException, CloneNotSupportedException {

        List<SrSlipKouatsubunsan> srSlipKouatsubunsanList = new ArrayList<>();
        List<SubSrSlipKouatsubunsan> subSrSlipKouatsubunsanList = new ArrayList<>();
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

                // ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞ画面テーブルデータ初期設定
                initGXHDO102B034BData(processData, null, pass);
                // 画面にデータを設定する(デフォルト値)
                processData.getItemList().forEach((fxhdd001) -> {
                    this.setItemData(processData, fxhdd001.getItemId(), fxhdd001.getInputDefault());
                });
                GXHDO102B034B bean = (GXHDO102B034B) getFormBean("beanGXHDO102B034B");
                List<GXHDO102B034Model> passlistdata = bean.getPasslistdata();
                passlistdata.forEach((gxhdo102b034model) -> {
                    processData.getItemListEx().stream().filter((fxhdd001) -> (!GXHDO102B034Const.PASS.equals(fxhdd001.getItemId()))).forEachOrdered((fxhdd001) -> {
                        this.setGXHDO102B034ModelData(gxhdo102b034model, fxhdd001.getItemId(), fxhdd001.getInputDefault());
                    });
                });
                return true;
            }

            // ｽﾘｯﾌﾟ作製・高圧分散データ取得
            srSlipKouatsubunsanList = getSrSlipKouatsubunsanData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo9, edaban, jissekino);
            if (srSlipKouatsubunsanList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // ｽﾘｯﾌﾟ作製・高圧分散入力_サブ画面データ取得
            subSrSlipKouatsubunsanList = getSubSrSlipKouatsubunsanData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo9, edaban, jissekino);
            if (subSrSlipKouatsubunsanList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }
            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srSlipKouatsubunsanList.isEmpty() || subSrSlipKouatsubunsanList.isEmpty()) {
            return false;
        }
        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);
        // ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞ画面テーブルデータ初期設定
        initGXHDO102B034BData(processData, subSrSlipKouatsubunsanList, pass);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srSlipKouatsubunsanList.get(0));
        // ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞ画面データ設定
        setInputItemPassListDataMainForm(subSrSlipKouatsubunsanList);
        return true;
    }

    /**
     * データ設定処理
     *
     * @param processData 処理制御データ
     * @param srSlipKouatsubunsan ｽﾘｯﾌﾟ作製・高圧分散
     */
    private void setInputItemDataMainForm(ProcessData processData, SrSlipKouatsubunsan srSlipKouatsubunsan) {

        // 排出容器内袋確認
        this.setItemData(processData, GXHDO102B034Const.HAISYUTSUYOUKIUCHIBUKURO, getSrSlipKouatsubunsanItemData(GXHDO102B034Const.HAISYUTSUYOUKIUCHIBUKURO, srSlipKouatsubunsan));

        // 排出容器洗浄確認
        this.setItemData(processData, GXHDO102B034Const.HAISYUTSUYOUKISENJYOU, getSrSlipKouatsubunsanItemData(GXHDO102B034Const.HAISYUTSUYOUKISENJYOU, srSlipKouatsubunsan));

        // 設備内洗浄確認
        this.setItemData(processData, GXHDO102B034Const.SETSUBINAISENJYOU, getSrSlipKouatsubunsanItemData(GXHDO102B034Const.SETSUBINAISENJYOU, srSlipKouatsubunsan));

        // 冷却水温度
        this.setItemData(processData, GXHDO102B034Const.REIKYAKUSUIONDO, getSrSlipKouatsubunsanItemData(GXHDO102B034Const.REIKYAKUSUIONDO, srSlipKouatsubunsan));

        // ﾀﾝｸにｱｰｽｸﾞﾘｯﾌﾟ接続
        this.setItemData(processData, GXHDO102B034Const.TANKEARTHGRIPSETSUZOKU, getSrSlipKouatsubunsanItemData(GXHDO102B034Const.TANKEARTHGRIPSETSUZOKU, srSlipKouatsubunsan));

        // 備考1
        this.setItemData(processData, GXHDO102B034Const.BIKOU1, getSrSlipKouatsubunsanItemData(GXHDO102B034Const.BIKOU1, srSlipKouatsubunsan));

        // 備考2
        this.setItemData(processData, GXHDO102B034Const.BIKOU2, getSrSlipKouatsubunsanItemData(GXHDO102B034Const.BIKOU2, srSlipKouatsubunsan));

    }

    /**
     * データリストからデータ設定処理
     *
     * @param subSrSlipKouatsubunsanList ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞデータ
     */
    private void setInputItemPassListDataMainForm(List<SubSrSlipKouatsubunsan> subSrSlipKouatsubunsanList) {
        GXHDO102B034B bean = (GXHDO102B034B) getFormBean("beanGXHDO102B034B");
        List<GXHDO102B034Model> passlistdata = bean.getPasslistdata();
        for (int i = 0; i < passlistdata.size(); i++) {
            GXHDO102B034Model gxhdo102b034model = passlistdata.get(i);
            Integer pass = Integer.parseInt(gxhdo102b034model.getPass().getValue());
            SubSrSlipKouatsubunsan subSrSlipKouatsubunsan = getSubSrSlipKouatsubunsanData(subSrSlipKouatsubunsanList, pass);
            setInputItemPassDataMainForm(gxhdo102b034model, subSrSlipKouatsubunsan);
        }
    }

    /**
     * ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞデータ取得
     *
     * @param pass ﾊﾟｽ回数
     * @param subSrSlipKouatsubunsanList ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞデータリスト
     * @return 項目データ
     */
    private SubSrSlipKouatsubunsan getSubSrSlipKouatsubunsanData(List<SubSrSlipKouatsubunsan> subSrSlipKouatsubunsanList, Integer pass) {
        List<SubSrSlipKouatsubunsan> selectData
                = subSrSlipKouatsubunsanList.stream().filter(n -> pass.compareTo(n.getPass()) == 0).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0);
        } else {
            return null;
        }
    }

    /**
     * データ設定処理
     *
     * @param gxhdo102b034model ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞモデルデータ
     * @param subSrSlipKouatsubunsan ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞデータ
     */
    private void setInputItemPassDataMainForm(GXHDO102B034Model gxhdo102b034model, SubSrSlipKouatsubunsan subSrSlipKouatsubunsan) {
        if (subSrSlipKouatsubunsan == null) {
            return;
        }
        // ﾊﾟｽ回数
        this.setGXHDO102B034ModelData(gxhdo102b034model, GXHDO102B034Const.PASS, getSubSrSlipKouatsubunsanItemData(GXHDO102B034Const.PASS, subSrSlipKouatsubunsan));

        // 送液側ﾀﾝｸNo.
        this.setGXHDO102B034ModelData(gxhdo102b034model, GXHDO102B034Const.SOUEKIGAWATANKNO, getSubSrSlipKouatsubunsanItemData(GXHDO102B034Const.SOUEKIGAWATANKNO, subSrSlipKouatsubunsan));

        // 排出側ﾀﾝｸNo.
        this.setGXHDO102B034ModelData(gxhdo102b034model, GXHDO102B034Const.HAISYUTSUGAWATANKNO, getSubSrSlipKouatsubunsanItemData(GXHDO102B034Const.HAISYUTSUGAWATANKNO, subSrSlipKouatsubunsan));

        // 高圧分散開始日
        this.setGXHDO102B034ModelData(gxhdo102b034model, GXHDO102B034Const.KOUATSUBUNSANKAISHI_DAY, getSubSrSlipKouatsubunsanItemData(GXHDO102B034Const.KOUATSUBUNSANKAISHI_DAY, subSrSlipKouatsubunsan));

        // 高圧分散開始時間
        this.setGXHDO102B034ModelData(gxhdo102b034model, GXHDO102B034Const.KOUATSUBUNSANKAISHI_TIME, getSubSrSlipKouatsubunsanItemData(GXHDO102B034Const.KOUATSUBUNSANKAISHI_TIME, subSrSlipKouatsubunsan));

        // 廃棄確認
        this.setGXHDO102B034ModelData(gxhdo102b034model, GXHDO102B034Const.HAIKIKAKUNIN, getSubSrSlipKouatsubunsanItemData(GXHDO102B034Const.HAIKIKAKUNIN, subSrSlipKouatsubunsan));

        // 実圧力(最大値)
        this.setGXHDO102B034ModelData(gxhdo102b034model, GXHDO102B034Const.JITSUATSURYOKU, getSubSrSlipKouatsubunsanItemData(GXHDO102B034Const.JITSUATSURYOKU, subSrSlipKouatsubunsan));

        // ｽﾘｯﾌﾟ流量
        this.setGXHDO102B034ModelData(gxhdo102b034model, GXHDO102B034Const.SLIPRYUURYOU, getSubSrSlipKouatsubunsanItemData(GXHDO102B034Const.SLIPRYUURYOU, subSrSlipKouatsubunsan));

        // ｽﾘｯﾌﾟ温度(IN)
        this.setGXHDO102B034ModelData(gxhdo102b034model, GXHDO102B034Const.SLIPONDOIN, getSubSrSlipKouatsubunsanItemData(GXHDO102B034Const.SLIPONDOIN, subSrSlipKouatsubunsan));

        // ｽﾘｯﾌﾟ温度(OUT)
        this.setGXHDO102B034ModelData(gxhdo102b034model, GXHDO102B034Const.SLIPONDOOUT, getSubSrSlipKouatsubunsanItemData(GXHDO102B034Const.SLIPONDOOUT, subSrSlipKouatsubunsan));

        // 高圧分散開始担当者
        this.setGXHDO102B034ModelData(gxhdo102b034model, GXHDO102B034Const.KOUATSUBUNSANKAISHITANTOUSYA, getSubSrSlipKouatsubunsanItemData(GXHDO102B034Const.KOUATSUBUNSANKAISHITANTOUSYA, subSrSlipKouatsubunsan));

        // 高圧分散終了日
        this.setGXHDO102B034ModelData(gxhdo102b034model, GXHDO102B034Const.KOUATSUBUNSANSYUURYOU_DAY, getSubSrSlipKouatsubunsanItemData(GXHDO102B034Const.KOUATSUBUNSANSYUURYOU_DAY, subSrSlipKouatsubunsan));

        // 高圧分散終了時間
        this.setGXHDO102B034ModelData(gxhdo102b034model, GXHDO102B034Const.KOUATSUBUNSANSYUURYOU_TIME, getSubSrSlipKouatsubunsanItemData(GXHDO102B034Const.KOUATSUBUNSANSYUURYOU_TIME, subSrSlipKouatsubunsan));

        // 高圧分散停止担当者
        this.setGXHDO102B034ModelData(gxhdo102b034model, GXHDO102B034Const.KOUATSUBUNSANTEISHITANTOUSYA, getSubSrSlipKouatsubunsanItemData(GXHDO102B034Const.KOUATSUBUNSANTEISHITANTOUSYA, subSrSlipKouatsubunsan));

        // 備考1
        this.setGXHDO102B034ModelData(gxhdo102b034model, GXHDO102B034Const.PASSBIKOU1, getSubSrSlipKouatsubunsanItemData(GXHDO102B034Const.PASSBIKOU1, subSrSlipKouatsubunsan));

        // 備考2
        this.setGXHDO102B034ModelData(gxhdo102b034model, GXHDO102B034Const.PASSBIKOU2, getSubSrSlipKouatsubunsanItemData(GXHDO102B034Const.PASSBIKOU2, subSrSlipKouatsubunsan));
    }

    /**
     * ｽﾘｯﾌﾟ作製・高圧分散の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @param jissekino 実績No
     * @return ｽﾘｯﾌﾟ作製・高圧分散データ
     * @throws SQLException 例外エラー
     */
    private List<SrSlipKouatsubunsan> getSrSlipKouatsubunsanData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrSlipKouatsubunsan(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
        } else {
            return loadTmpSrSlipKouatsubunsan(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
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
     * [ｽﾘｯﾌﾟ作製・高圧分散]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param jissekino 実績No(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrSlipKouatsubunsan> loadSrSlipKouatsubunsan(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, String rev) throws SQLException {

        String sql = "SELECT "
                + " kojyo,lotno,edaban,jissekino,sliphinmei,sliplotno,lotkubun,genryoukigou,kouatsubunsanki,kouatsubunsangouki,"
                + "haisyutsuyoukiuchibukuro,haisyutsuyoukisenjyou,setsubinaisenjyou,nozzlekei,reikyakusuivalve,reikyakusuiondokikaku,reikyakusuiondo,"
                + "kouatsubunsankaisuu,tankearthgripsetsuzoku,bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + " FROM sr_slip_kouatsubunsan "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? ";

        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            sql += "AND revision = ? ";
        }

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);

        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            params.add(rev);
        }

        Map<String, String> mapping = new HashMap<>();
        mapping.put("kojyo", "kojyo");                                         // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                         // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                       // 枝番
        mapping.put("jissekino", "jissekino");                                 // 実績No
        mapping.put("sliphinmei", "sliphinmei");                               // ｽﾘｯﾌﾟ品名
        mapping.put("sliplotno", "sliplotno");                                 // ｽﾘｯﾌﾟLotNo
        mapping.put("lotkubun", "lotkubun");                                   // ﾛｯﾄ区分
        mapping.put("genryoukigou", "genryoukigou");                           // 原料記号
        mapping.put("kouatsubunsanki", "kouatsubunsanki");                     // 高圧分散機
        mapping.put("kouatsubunsangouki", "kouatsubunsangouki");               // 高圧分散号機
        mapping.put("haisyutsuyoukiuchibukuro", "haisyutsuyoukiuchibukuro");   // 排出容器内袋確認
        mapping.put("haisyutsuyoukisenjyou", "haisyutsuyoukisenjyou");         // 排出容器洗浄確認
        mapping.put("setsubinaisenjyou", "setsubinaisenjyou");                 // 設備内洗浄確認
        mapping.put("nozzlekei", "nozzlekei");                                 // ﾉｽﾞﾙ径
        mapping.put("reikyakusuivalve", "reikyakusuivalve");                   // 冷却水ﾊﾞﾙﾌﾞ開
        mapping.put("reikyakusuiondokikaku", "reikyakusuiondokikaku");         // 冷却水温度規格
        mapping.put("reikyakusuiondo", "reikyakusuiondo");                     // 冷却水温度
        mapping.put("kouatsubunsankaisuu", "kouatsubunsankaisuu");             // 高圧分散回数
        mapping.put("tankearthgripsetsuzoku", "tankearthgripsetsuzoku");       // ﾀﾝｸにｱｰｽｸﾞﾘｯﾌﾟ接続
        mapping.put("bikou1", "bikou1");                                       // 備考1
        mapping.put("bikou2", "bikou2");                                       // 備考2
        mapping.put("torokunichiji", "torokunichiji");                         // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                           // 更新日時
        mapping.put("revision", "revision");                                   // revision

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrSlipKouatsubunsan>> beanHandler = new BeanListHandler<>(SrSlipKouatsubunsan.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [ｽﾘｯﾌﾟ作製・高圧分散_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param jissekino 実績No(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrSlipKouatsubunsan> loadTmpSrSlipKouatsubunsan(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, String rev) throws SQLException {

        String sql = "SELECT "
                + " kojyo,lotno,edaban,jissekino,sliphinmei,sliplotno,lotkubun,genryoukigou,kouatsubunsanki,kouatsubunsangouki,"
                + "haisyutsuyoukiuchibukuro,haisyutsuyoukisenjyou,setsubinaisenjyou,nozzlekei,reikyakusuivalve,reikyakusuiondokikaku,reikyakusuiondo,"
                + "kouatsubunsankaisuu,tankearthgripsetsuzoku,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + " FROM tmp_sr_slip_kouatsubunsan "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? AND deleteflag = ?  ";

        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            sql += "AND revision = ? ";
        }

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(0);

        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            params.add(rev);
        }

        Map<String, String> mapping = new HashMap<>();
        mapping.put("kojyo", "kojyo");                                         // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                         // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                       // 枝番
        mapping.put("jissekino", "jissekino");                                 // 実績No
        mapping.put("sliphinmei", "sliphinmei");                               // ｽﾘｯﾌﾟ品名
        mapping.put("sliplotno", "sliplotno");                                 // ｽﾘｯﾌﾟLotNo
        mapping.put("lotkubun", "lotkubun");                                   // ﾛｯﾄ区分
        mapping.put("genryoukigou", "genryoukigou");                           // 原料記号
        mapping.put("kouatsubunsanki", "kouatsubunsanki");                     // 高圧分散機
        mapping.put("kouatsubunsangouki", "kouatsubunsangouki");               // 高圧分散号機
        mapping.put("haisyutsuyoukiuchibukuro", "haisyutsuyoukiuchibukuro");   // 排出容器内袋確認
        mapping.put("haisyutsuyoukisenjyou", "haisyutsuyoukisenjyou");         // 排出容器洗浄確認
        mapping.put("setsubinaisenjyou", "setsubinaisenjyou");                 // 設備内洗浄確認
        mapping.put("nozzlekei", "nozzlekei");                                 // ﾉｽﾞﾙ径
        mapping.put("reikyakusuivalve", "reikyakusuivalve");                   // 冷却水ﾊﾞﾙﾌﾞ開
        mapping.put("reikyakusuiondokikaku", "reikyakusuiondokikaku");         // 冷却水温度規格
        mapping.put("reikyakusuiondo", "reikyakusuiondo");                     // 冷却水温度
        mapping.put("kouatsubunsankaisuu", "kouatsubunsankaisuu");             // 高圧分散回数
        mapping.put("tankearthgripsetsuzoku", "tankearthgripsetsuzoku");       // ﾀﾝｸにｱｰｽｸﾞﾘｯﾌﾟ接続
        mapping.put("bikou1", "bikou1");                                       // 備考1
        mapping.put("bikou2", "bikou2");                                       // 備考2
        mapping.put("torokunichiji", "torokunichiji");                         // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                           // 更新日時
        mapping.put("revision", "revision");                                   // revision
        mapping.put("deleteflag", "deleteflag");                               // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrSlipKouatsubunsan>> beanHandler = new BeanListHandler<>(SrSlipKouatsubunsan.class, rowProcessor);

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
    private void setGXHDO102B034ModelData(GXHDO102B034Model gxhdo102b034model, String itemId, String value) {
        switch (itemId) {
            // ﾊﾟｽ回数
            case GXHDO102B034Const.PASS:
                gxhdo102b034model.getPass().setValue(value);
                break;
            //送液側ﾀﾝｸNo.
            case GXHDO102B034Const.SOUEKIGAWATANKNO:
                gxhdo102b034model.getSouekigawatankno().setValue(value);
                break;
            //排出側ﾀﾝｸNo.
            case GXHDO102B034Const.HAISYUTSUGAWATANKNO:
                gxhdo102b034model.getHaisyutsugawatankno().setValue(value);
                break;
            //設定圧力
            case GXHDO102B034Const.SETTEIATSURYOKU:
                gxhdo102b034model.getSetteiatsuryoku().setValue(value);
                break;
            //開始直後排気量
            case GXHDO102B034Const.KAISHITYOKUGOHAIKIRYOU:
                gxhdo102b034model.getKaishityokugohaikiryou().setValue(value);
                break;
            //高圧分散開始日
            case GXHDO102B034Const.KOUATSUBUNSANKAISHI_DAY:
                gxhdo102b034model.getKouatsubunsankaishi_day().setValue(value);
                break;
            //高圧分散開始時間
            case GXHDO102B034Const.KOUATSUBUNSANKAISHI_TIME:
                gxhdo102b034model.getKouatsubunsankaishi_time().setValue(value);
                break;
            //廃棄確認
            case GXHDO102B034Const.HAIKIKAKUNIN:
                gxhdo102b034model.getHaikikakunin().setValue(value);
                break;
            //実圧力(最大値)
            case GXHDO102B034Const.JITSUATSURYOKU:
                gxhdo102b034model.getJitsuatsuryoku().setValue(value);
                break;
            //ｽﾘｯﾌﾟ流量
            case GXHDO102B034Const.SLIPRYUURYOU:
                gxhdo102b034model.getSlipryuuryou().setValue(value);
                break;
            //ｽﾘｯﾌﾟ温度(IN)
            case GXHDO102B034Const.SLIPONDOIN:
                gxhdo102b034model.getSlipondoin().setValue(value);
                break;
            //ｽﾘｯﾌﾟ温度(OUT)
            case GXHDO102B034Const.SLIPONDOOUT:
                gxhdo102b034model.getSlipondoout().setValue(value);
                break;
            //高圧分散開始担当者
            case GXHDO102B034Const.KOUATSUBUNSANKAISHITANTOUSYA:
                gxhdo102b034model.getKouatsubunsankaishitantousya().setValue(value);
                break;
            //高圧分散終了日
            case GXHDO102B034Const.KOUATSUBUNSANSYUURYOU_DAY:
                gxhdo102b034model.getKouatsubunsansyuuryou_day().setValue(value);
                break;
            //高圧分散終了時間
            case GXHDO102B034Const.KOUATSUBUNSANSYUURYOU_TIME:
                gxhdo102b034model.getKouatsubunsansyuuryou_time().setValue(value);
                break;
            //高圧分散停止担当者
            case GXHDO102B034Const.KOUATSUBUNSANTEISHITANTOUSYA:
                gxhdo102b034model.getKouatsubunsanteishitantousya().setValue(value);
                break;
            //備考1
            case GXHDO102B034Const.PASSBIKOU1:
                gxhdo102b034model.getBikou1().setValue(value);
                break;
            //備考2
            case GXHDO102B034Const.PASSBIKOU2:
                gxhdo102b034model.getBikou2().setValue(value);
                break;
        }
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
     * @param srSlipKouatsubunsan ｽﾘｯﾌﾟ作製・高圧分散データ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrSlipKouatsubunsan srSlipKouatsubunsan) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srSlipKouatsubunsan != null) {
            // 元データが存在する場合元データより取得
            return getSrSlipKouatsubunsanItemData(itemId, srSlipKouatsubunsan);
        } else {
            return null;
        }
    }

    /**
     * 項目データ(入力値)取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @param srGlasshyoryo ｽﾘｯﾌﾟ作製・高圧分散データ
     * @return 入力値
     */
    private String getItemKikakuchi(List<FXHDD01> listData, String itemId, SrSlipKouatsubunsan srSlipKouatsubunsan) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return StringUtil.nullToBlank(selectData.get(0).getKikakuChi()).replace("【", "").replace("】", "");
        } else if (srSlipKouatsubunsan != null) {
            // 元データが存在する場合元データより取得
            return getSrSlipKouatsubunsanItemData(itemId, srSlipKouatsubunsan);
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
     * ｽﾘｯﾌﾟ作製・高圧分散_仮登録(tmp_sr_slip_kouatsubunsan)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void insertTmpSrSlipKouatsubunsan(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, String systemTime, ProcessData processData) throws SQLException {

        String sql = "INSERT INTO tmp_sr_slip_kouatsubunsan ( "
                + " kojyo,lotno,edaban,jissekino,sliphinmei,sliplotno,lotkubun,genryoukigou,kouatsubunsanki,kouatsubunsangouki,"
                + "haisyutsuyoukiuchibukuro,haisyutsuyoukisenjyou,setsubinaisenjyou,nozzlekei,reikyakusuivalve,reikyakusuiondokikaku,reikyakusuiondo,"
                + "kouatsubunsankaisuu,tankearthgripsetsuzoku,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrSlipKouatsubunsan(true, newRev, deleteflag, kojyo, lotNo, edaban, jissekino, systemTime, processData, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・高圧分散_仮登録(tmp_sr_slip_kouatsubunsan)更新処理
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
    private void updateTmpSrSlipKouatsubunsan(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, String systemTime, ProcessData processData) throws SQLException {

        String sql = "UPDATE tmp_sr_slip_kouatsubunsan SET "
                + " sliphinmei = ?,sliplotno = ?,lotkubun = ?,genryoukigou = ?,kouatsubunsanki = ?,kouatsubunsangouki = ?,haisyutsuyoukiuchibukuro = ?,"
                + "haisyutsuyoukisenjyou = ?,setsubinaisenjyou = ?,nozzlekei = ?,reikyakusuivalve = ?,reikyakusuiondokikaku = ?,reikyakusuiondo = ?,kouatsubunsankaisuu = ?,"
                + "tankearthgripsetsuzoku = ?,bikou1 = ?,bikou2 = ?,kosinnichiji = ?,revision = ?,deleteflag = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrSlipKouatsubunsan> srSlipKouatsubunsanList = getSrSlipKouatsubunsanData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekino);
        SrSlipKouatsubunsan srSlipKouatsubunsan = null;
        if (!srSlipKouatsubunsanList.isEmpty()) {
            srSlipKouatsubunsan = srSlipKouatsubunsanList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrSlipKouatsubunsan(false, newRev, 0, "", "", "", jissekino, systemTime, processData, srSlipKouatsubunsan);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・高圧分散_仮登録(tmp_sr_slip_kouatsubunsan)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrSlipKouatsubunsan(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        String sql = "DELETE FROM tmp_sr_slip_kouatsubunsan "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? AND revision = ? ";

        //更新値設定
        List<Object> params = new ArrayList<>();

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・高圧分散_仮登録(tmp_sr_slip_kouatsubunsan)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srSlipKouatsubunsan ｽﾘｯﾌﾟ作製・高圧分散データ
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrSlipKouatsubunsan(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, int jissekino, String systemTime, ProcessData processData, SrSlipKouatsubunsan srSlipKouatsubunsan) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
            params.add(jissekino); //枝番
        }

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B034Const.SLIPHINMEI, srSlipKouatsubunsan)));                 // ｽﾘｯﾌﾟ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B034Const.SLIPLOTNO, srSlipKouatsubunsan)));                  // ｽﾘｯﾌﾟLotNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B034Const.LOTKUBUN, srSlipKouatsubunsan)));                   // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B034Const.GENRYOUKIGOU, srSlipKouatsubunsan)));          // 原料記号
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B034Const.KOUATSUBUNSANKI, srSlipKouatsubunsan)));       // 高圧分散機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B034Const.KOUATSUBUNSANGOUKI, srSlipKouatsubunsan)));    // 高圧分散号機
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B034Const.HAISYUTSUYOUKIUCHIBUKURO, srSlipKouatsubunsan), null));                 // 排出容器内袋確認
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B034Const.HAISYUTSUYOUKISENJYOU, srSlipKouatsubunsan), null));                    // 排出容器洗浄確認
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B034Const.SETSUBINAISENJYOU, srSlipKouatsubunsan), null));                        // 設備内洗浄確認
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B034Const.NOZZLEKEI, srSlipKouatsubunsan)));             // ﾉｽﾞﾙ径
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B034Const.REIKYAKUSUIVALVE, srSlipKouatsubunsan)));      // 冷却水ﾊﾞﾙﾌﾞ開
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B034Const.REIKYAKUSUIONDO, srSlipKouatsubunsan)));       // 冷却水温度規格
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B034Const.REIKYAKUSUIONDO, srSlipKouatsubunsan)));               // 冷却水温度
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B034Const.KOUATSUBUNSANKAISUU, srSlipKouatsubunsan)));   // 高圧分散回数
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B034Const.TANKEARTHGRIPSETSUZOKU, srSlipKouatsubunsan), null));                   // ﾀﾝｸにｱｰｽｸﾞﾘｯﾌﾟ接続
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B034Const.BIKOU1, srSlipKouatsubunsan)));                     // 備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B034Const.BIKOU2, srSlipKouatsubunsan)));                     // 備考2

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
     * ｽﾘｯﾌﾟ作製・高圧分散(sr_slip_kouatsubunsan)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param tmpSrSlipKouatsubunsan 仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrSlipKouatsubunsan(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, String systemTime, ProcessData processData, SrSlipKouatsubunsan tmpSrSlipKouatsubunsan) throws SQLException {

        String sql = "INSERT INTO sr_slip_kouatsubunsan ( "
                + " kojyo,lotno,edaban,jissekino,sliphinmei,sliplotno,lotkubun,genryoukigou,kouatsubunsanki,kouatsubunsangouki,"
                + "haisyutsuyoukiuchibukuro,haisyutsuyoukisenjyou,setsubinaisenjyou,nozzlekei,reikyakusuivalve,reikyakusuiondokikaku,reikyakusuiondo,"
                + "kouatsubunsankaisuu,tankearthgripsetsuzoku,bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrSlipKouatsubunsan(true, newRev, kojyo, lotNo, edaban, jissekino, systemTime, processData, tmpSrSlipKouatsubunsan);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・高圧分散(sr_slip_kouatsubunsan)更新処理
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
    private void updateSrSlipKouatsubunsan(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, String systemTime, ProcessData processData) throws SQLException {
        String sql = "UPDATE sr_slip_kouatsubunsan SET "
                + " sliphinmei = ?,sliplotno = ?,lotkubun = ?,genryoukigou = ?,kouatsubunsanki = ?,kouatsubunsangouki = ?,haisyutsuyoukiuchibukuro = ?,"
                + "haisyutsuyoukisenjyou = ?,setsubinaisenjyou = ?,nozzlekei = ?,reikyakusuivalve = ?,reikyakusuiondokikaku = ?,reikyakusuiondo = ?,kouatsubunsankaisuu = ?,"
                + "tankearthgripsetsuzoku = ?,bikou1 = ?,bikou2 = ?,kosinnichiji = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrSlipKouatsubunsan> srSlipKouatsubunsanList = getSrSlipKouatsubunsanData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekino);
        SrSlipKouatsubunsan srSlipKouatsubunsan = null;
        if (!srSlipKouatsubunsanList.isEmpty()) {
            srSlipKouatsubunsan = srSlipKouatsubunsanList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrSlipKouatsubunsan(false, newRev, "", "", "", jissekino, systemTime, processData, srSlipKouatsubunsan);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・高圧分散(sr_slip_kouatsubunsan)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srSlipKouatsubunsan ｽﾘｯﾌﾟ作製・高圧分散データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrSlipKouatsubunsan(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban, int jissekino,
            String systemTime, ProcessData processData, SrSlipKouatsubunsan srSlipKouatsubunsan) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
            params.add(jissekino); //実績No
        }
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B034Const.SLIPHINMEI, srSlipKouatsubunsan)));                 // ｽﾘｯﾌﾟ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B034Const.SLIPLOTNO, srSlipKouatsubunsan)));                  // ｽﾘｯﾌﾟLotNo
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B034Const.LOTKUBUN, srSlipKouatsubunsan)));                   // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B034Const.GENRYOUKIGOU, srSlipKouatsubunsan)));          // 原料記号
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B034Const.KOUATSUBUNSANKI, srSlipKouatsubunsan)));       // 高圧分散機
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B034Const.KOUATSUBUNSANGOUKI, srSlipKouatsubunsan)));    // 高圧分散号機
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B034Const.HAISYUTSUYOUKIUCHIBUKURO, srSlipKouatsubunsan), 9));         // 排出容器内袋確認
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B034Const.HAISYUTSUYOUKISENJYOU, srSlipKouatsubunsan), 9));            // 排出容器洗浄確認
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B034Const.SETSUBINAISENJYOU, srSlipKouatsubunsan), 9));                // 設備内洗浄確認
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B034Const.NOZZLEKEI, srSlipKouatsubunsan)));             // ﾉｽﾞﾙ径
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B034Const.REIKYAKUSUIVALVE, srSlipKouatsubunsan)));      // 冷却水ﾊﾞﾙﾌﾞ開
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B034Const.REIKYAKUSUIONDO, srSlipKouatsubunsan)));       // 冷却水温度規格
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B034Const.REIKYAKUSUIONDO, srSlipKouatsubunsan)));               // 冷却水温度
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B034Const.KOUATSUBUNSANKAISUU, srSlipKouatsubunsan)));   // 高圧分散回数
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B034Const.TANKEARTHGRIPSETSUZOKU, srSlipKouatsubunsan), 9));           // ﾀﾝｸにｱｰｽｸﾞﾘｯﾌﾟ接続
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B034Const.BIKOU1, srSlipKouatsubunsan)));                     // 備考1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B034Const.BIKOU2, srSlipKouatsubunsan)));                     // 備考2

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
     * ｽﾘｯﾌﾟ作製・高圧分散(sr_slip_kouatsubunsan)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @throws SQLException 例外エラー
     */
    private void deleteSrSlipKouatsubunsan(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        String sql = "DELETE FROM sr_slip_kouatsubunsan "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? AND revision = ? ";

        //更新値設定
        List<Object> params = new ArrayList<>();

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * [ｽﾘｯﾌﾟ作製・高圧分散_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_slip_kouatsubunsan "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? ";
        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);

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
     * 項目IDに該当するDBの値を取得する。
     *
     * @param itemId 項目ID
     * @param srSlipKouatsubunsan ｽﾘｯﾌﾟ作製・高圧分散データ
     * @return DB値
     */
    private String getSrSlipKouatsubunsanItemData(String itemId, SrSlipKouatsubunsan srSlipKouatsubunsan) {
        switch (itemId) {

            // ｽﾘｯﾌﾟ品名
            case GXHDO102B034Const.SLIPHINMEI:
                return StringUtil.nullToBlank(srSlipKouatsubunsan.getSliphinmei());

            // ｽﾘｯﾌﾟLotNo
            case GXHDO102B034Const.SLIPLOTNO:
                return StringUtil.nullToBlank(srSlipKouatsubunsan.getSliplotno());

            // ﾛｯﾄ区分
            case GXHDO102B034Const.LOTKUBUN:
                return StringUtil.nullToBlank(srSlipKouatsubunsan.getLotkubun());

            // 原料記号
            case GXHDO102B034Const.GENRYOUKIGOU:
                return StringUtil.nullToBlank(srSlipKouatsubunsan.getGenryoukigou());

            // 高圧分散機
            case GXHDO102B034Const.KOUATSUBUNSANKI:
                return StringUtil.nullToBlank(srSlipKouatsubunsan.getKouatsubunsanki());

            // 高圧分散号機
            case GXHDO102B034Const.KOUATSUBUNSANGOUKI:
                return StringUtil.nullToBlank(srSlipKouatsubunsan.getKouatsubunsangouki());

            // 排出容器内袋確認
            case GXHDO102B034Const.HAISYUTSUYOUKIUCHIBUKURO:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSlipKouatsubunsan.getHaisyutsuyoukiuchibukuro()));

            // 排出容器洗浄確認
            case GXHDO102B034Const.HAISYUTSUYOUKISENJYOU:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSlipKouatsubunsan.getHaisyutsuyoukisenjyou()));

            // 設備内洗浄確認
            case GXHDO102B034Const.SETSUBINAISENJYOU:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSlipKouatsubunsan.getSetsubinaisenjyou()));

            // ﾉｽﾞﾙ径
            case GXHDO102B034Const.NOZZLEKEI:
                return StringUtil.nullToBlank(srSlipKouatsubunsan.getNozzlekei());

            // 冷却水ﾊﾞﾙﾌﾞ開
            case GXHDO102B034Const.REIKYAKUSUIVALVE:
                return StringUtil.nullToBlank(srSlipKouatsubunsan.getReikyakusuivalve());

            // 冷却水温度
            case GXHDO102B034Const.REIKYAKUSUIONDO:
                return StringUtil.nullToBlank(srSlipKouatsubunsan.getReikyakusuiondo());

            // 高圧分散回数
            case GXHDO102B034Const.KOUATSUBUNSANKAISUU:
                return StringUtil.nullToBlank(srSlipKouatsubunsan.getKouatsubunsankaisuu());

            // ﾀﾝｸにｱｰｽｸﾞﾘｯﾌﾟ接続
            case GXHDO102B034Const.TANKEARTHGRIPSETSUZOKU:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSlipKouatsubunsan.getTankearthgripsetsuzoku()));

            // 備考1
            case GXHDO102B034Const.BIKOU1:
                return StringUtil.nullToBlank(srSlipKouatsubunsan.getBikou1());

            // 備考2
            case GXHDO102B034Const.BIKOU2:
                return StringUtil.nullToBlank(srSlipKouatsubunsan.getBikou2());

            default:
                return null;
        }
    }

    /**
     * 項目IDに該当するDBの値を取得する。
     *
     * @param itemId 項目ID
     * @param subSrSlipKouatsubunsan ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞデータ
     * @return DB値
     */
    private String getSubSrSlipKouatsubunsanItemData(String itemId, SubSrSlipKouatsubunsan subSrSlipKouatsubunsan) {
        switch (itemId) {

            // ﾊﾟｽ回数
            case GXHDO102B034Const.PASS:
                return StringUtil.nullToBlank(subSrSlipKouatsubunsan.getPass());
            //送液側ﾀﾝｸNo.
            case GXHDO102B034Const.SOUEKIGAWATANKNO:
                return StringUtil.nullToBlank(subSrSlipKouatsubunsan.getSouekigawatankno());
            //排出側ﾀﾝｸNo.
            case GXHDO102B034Const.HAISYUTSUGAWATANKNO:
                return StringUtil.nullToBlank(subSrSlipKouatsubunsan.getHaisyutsugawatankno());
            //設定圧力
            case GXHDO102B034Const.SETTEIATSURYOKU:
                return StringUtil.nullToBlank(subSrSlipKouatsubunsan.getSetteiatsuryoku());
            //開始直後排気量
            case GXHDO102B034Const.KAISHITYOKUGOHAIKIRYOU:
                return StringUtil.nullToBlank(subSrSlipKouatsubunsan.getKaishityokugohaikiryou());
            //高圧分散開始日
            case GXHDO102B034Const.KOUATSUBUNSANKAISHI_DAY:
                return DateUtil.formattedTimestamp(subSrSlipKouatsubunsan.getKouatsubunsankaishinichiji(), "yyMMdd");
            //高圧分散開始時間
            case GXHDO102B034Const.KOUATSUBUNSANKAISHI_TIME:
                return DateUtil.formattedTimestamp(subSrSlipKouatsubunsan.getKouatsubunsankaishinichiji(), "HHmm");
            //廃棄確認
            case GXHDO102B034Const.HAIKIKAKUNIN:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(subSrSlipKouatsubunsan.getHaikikakunin()));
            //実圧力(最大値)
            case GXHDO102B034Const.JITSUATSURYOKU:
                return StringUtil.nullToBlank(subSrSlipKouatsubunsan.getJitsuatsuryoku());
            //ｽﾘｯﾌﾟ流量
            case GXHDO102B034Const.SLIPRYUURYOU:
                return StringUtil.nullToBlank(subSrSlipKouatsubunsan.getSlipryuuryou());
            //ｽﾘｯﾌﾟ温度(IN)
            case GXHDO102B034Const.SLIPONDOIN:
                return StringUtil.nullToBlank(subSrSlipKouatsubunsan.getSlipondoin());
            //ｽﾘｯﾌﾟ温度(OUT)
            case GXHDO102B034Const.SLIPONDOOUT:
                return StringUtil.nullToBlank(subSrSlipKouatsubunsan.getSlipondoout());
            //高圧分散開始担当者
            case GXHDO102B034Const.KOUATSUBUNSANKAISHITANTOUSYA:
                return StringUtil.nullToBlank(subSrSlipKouatsubunsan.getKouatsubunsankaishitantousya());
            //高圧分散終了日
            case GXHDO102B034Const.KOUATSUBUNSANSYUURYOU_DAY:
                return DateUtil.formattedTimestamp(subSrSlipKouatsubunsan.getKouatsubunsansyuuryounichiji(), "yyMMdd");
            //高圧分散終了時間
            case GXHDO102B034Const.KOUATSUBUNSANSYUURYOU_TIME:
                return DateUtil.formattedTimestamp(subSrSlipKouatsubunsan.getKouatsubunsansyuuryounichiji(), "HHmm");
            //高圧分散停止担当者
            case GXHDO102B034Const.KOUATSUBUNSANTEISHITANTOUSYA:
                return StringUtil.nullToBlank(subSrSlipKouatsubunsan.getKouatsubunsanteishitantousya());
            //備考1
            case GXHDO102B034Const.PASSBIKOU1:
                return StringUtil.nullToBlank(subSrSlipKouatsubunsan.getBikou1());
            //備考2
            case GXHDO102B034Const.PASSBIKOU2:
                return StringUtil.nullToBlank(subSrSlipKouatsubunsan.getBikou2());
            default:
                return null;
        }
    }

    /**
     * ｽﾘｯﾌﾟ作製・高圧分散_仮登録(tmp_sr_slip_kouatsubunsan)登録処理(削除時)
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void insertDeleteDataTmpSrSlipKouatsubunsan(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, String systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_slip_kouatsubunsan ("
                + " kojyo,lotno,edaban,jissekino,sliphinmei,sliplotno,lotkubun,genryoukigou,kouatsubunsanki,kouatsubunsangouki,"
                + "haisyutsuyoukiuchibukuro,haisyutsuyoukisenjyou,setsubinaisenjyou,nozzlekei,reikyakusuivalve,reikyakusuiondokikaku,reikyakusuiondo,"
                + "kouatsubunsankaisuu,tankearthgripsetsuzoku,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") SELECT "
                + " kojyo,lotno,edaban,jissekino,sliphinmei,sliplotno,lotkubun,genryoukigou,kouatsubunsanki,kouatsubunsangouki,"
                + "haisyutsuyoukiuchibukuro,haisyutsuyoukisenjyou,setsubinaisenjyou,nozzlekei,reikyakusuivalve,reikyakusuiondokikaku,reikyakusuiondo,"
                + "kouatsubunsankaisuu,tankearthgripsetsuzoku,bikou1,bikou2,?,?,?,? "
                + " FROM sr_slip_kouatsubunsan "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? ";

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
        params.add(jissekino); //実績No

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
     * ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞ画面テーブルヘッダー初期設定
     *
     * @param processData 処理制御データ
     */
    private void initGXHDO102B034B(ProcessData processData) throws CloneNotSupportedException {
        GXHDO102B034B bean = (GXHDO102B034B) getFormBean("beanGXHDO102B034B");
        // ﾊﾟｽ回数
        bean.setPass(getItemRow(processData.getItemListEx(), GXHDO102B034Const.PASS).clone());
        //送液側ﾀﾝｸNo.
        bean.setSouekigawatankno(getItemRow(processData.getItemListEx(), GXHDO102B034Const.SOUEKIGAWATANKNO).clone());
        //排出側ﾀﾝｸNo.
        bean.setHaisyutsugawatankno(getItemRow(processData.getItemListEx(), GXHDO102B034Const.HAISYUTSUGAWATANKNO).clone());
        //設定圧力
        bean.setSetteiatsuryoku(getItemRow(processData.getItemListEx(), GXHDO102B034Const.SETTEIATSURYOKU).clone());
        //開始直後排気量
        bean.setKaishityokugohaikiryou(getItemRow(processData.getItemListEx(), GXHDO102B034Const.KAISHITYOKUGOHAIKIRYOU).clone());
        //高圧分散開始日
        bean.setKouatsubunsankaishi_day(getItemRow(processData.getItemListEx(), GXHDO102B034Const.KOUATSUBUNSANKAISHI_DAY).clone());
        //高圧分散開始時間
        bean.setKouatsubunsankaishi_time(getItemRow(processData.getItemListEx(), GXHDO102B034Const.KOUATSUBUNSANKAISHI_TIME).clone());
        //廃棄確認
        bean.setHaikikakunin(getItemRow(processData.getItemListEx(), GXHDO102B034Const.HAIKIKAKUNIN).clone());
        //実圧力(最大値)
        bean.setJitsuatsuryoku(getItemRow(processData.getItemListEx(), GXHDO102B034Const.JITSUATSURYOKU).clone());
        //ｽﾘｯﾌﾟ流量
        bean.setSlipryuuryou(getItemRow(processData.getItemListEx(), GXHDO102B034Const.SLIPRYUURYOU).clone());
        //ｽﾘｯﾌﾟ温度(IN)
        bean.setSlipondoin(getItemRow(processData.getItemListEx(), GXHDO102B034Const.SLIPONDOIN).clone());
        //ｽﾘｯﾌﾟ温度(OUT)
        bean.setSlipondoout(getItemRow(processData.getItemListEx(), GXHDO102B034Const.SLIPONDOOUT).clone());
        //高圧分散開始担当者
        bean.setKouatsubunsankaishitantousya(getItemRow(processData.getItemListEx(), GXHDO102B034Const.KOUATSUBUNSANKAISHITANTOUSYA).clone());
        //高圧分散終了日
        bean.setKouatsubunsansyuuryou_day(getItemRow(processData.getItemListEx(), GXHDO102B034Const.KOUATSUBUNSANSYUURYOU_DAY).clone());
        //高圧分散終了時間
        bean.setKouatsubunsansyuuryou_time(getItemRow(processData.getItemListEx(), GXHDO102B034Const.KOUATSUBUNSANSYUURYOU_TIME).clone());
        //高圧分散停止担当者
        bean.setKouatsubunsanteishitantousya(getItemRow(processData.getItemListEx(), GXHDO102B034Const.KOUATSUBUNSANTEISHITANTOUSYA).clone());
        //備考1
        bean.setBikou1(getItemRow(processData.getItemListEx(), GXHDO102B034Const.PASSBIKOU1).clone());
        //備考2
        bean.setBikou2(getItemRow(processData.getItemListEx(), GXHDO102B034Const.PASSBIKOU2).clone());
    }

    /**
     * ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞ画面テーブルデータ初期設定
     *
     * @param processData 処理制御データ
     * @param subSrSlipKouatsubunsanList ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞデータ
     * @param pass ﾊﾟｽ回数
     */
    private void initGXHDO102B034BData(ProcessData processData, List<SubSrSlipKouatsubunsan> subSrSlipKouatsubunsanList, int pass) throws CloneNotSupportedException {
        int hyoujirow = pass;
        if (subSrSlipKouatsubunsanList != null && subSrSlipKouatsubunsanList.size() > pass) {
            hyoujirow = subSrSlipKouatsubunsanList.size();
        }
        List<GXHDO102B034Model> passlistdata = new ArrayList<>();
        for (int i = 1; i <= hyoujirow; i++) {
            passlistdata.add(createGXHDO102B034Model(processData, i));
        }
        GXHDO102B034B bean = (GXHDO102B034B) getFormBean("beanGXHDO102B034B");
        bean.setPasslistdata(passlistdata);
    }

    /**
     * ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞのモデルデータを作成する
     *
     * @param processData 処理制御データ
     * @param pass ﾊﾟｽ回数
     */
    private GXHDO102B034Model createGXHDO102B034Model(ProcessData processData, Integer pass) throws CloneNotSupportedException {
        GXHDO102B034Model gxhdo102b034model = new GXHDO102B034Model();
        FXHDD01 itemPass = getItemRow(processData.getItemListEx(), GXHDO102B034Const.PASS).clone();
        itemPass.setValue(pass.toString());
        // ﾊﾟｽ回数
        gxhdo102b034model.setPass(itemPass);
        //送液側ﾀﾝｸNo.
        gxhdo102b034model.setSouekigawatankno(getItemRow(processData.getItemListEx(), GXHDO102B034Const.SOUEKIGAWATANKNO).clone());
        //排出側ﾀﾝｸNo.
        gxhdo102b034model.setHaisyutsugawatankno(getItemRow(processData.getItemListEx(), GXHDO102B034Const.HAISYUTSUGAWATANKNO).clone());
        //設定圧力
        gxhdo102b034model.setSetteiatsuryoku(getItemRow(processData.getItemListEx(), GXHDO102B034Const.SETTEIATSURYOKU).clone());
        //開始直後排気量
        gxhdo102b034model.setKaishityokugohaikiryou(getItemRow(processData.getItemListEx(), GXHDO102B034Const.KAISHITYOKUGOHAIKIRYOU).clone());
        //高圧分散開始日
        gxhdo102b034model.setKouatsubunsankaishi_day(getItemRow(processData.getItemListEx(), GXHDO102B034Const.KOUATSUBUNSANKAISHI_DAY).clone());
        //高圧分散開始時間
        gxhdo102b034model.setKouatsubunsankaishi_time(getItemRow(processData.getItemListEx(), GXHDO102B034Const.KOUATSUBUNSANKAISHI_TIME).clone());
        //廃棄確認
        gxhdo102b034model.setHaikikakunin(getItemRow(processData.getItemListEx(), GXHDO102B034Const.HAIKIKAKUNIN).clone());
        //実圧力(最大値)
        gxhdo102b034model.setJitsuatsuryoku(getItemRow(processData.getItemListEx(), GXHDO102B034Const.JITSUATSURYOKU).clone());
        //ｽﾘｯﾌﾟ流量
        gxhdo102b034model.setSlipryuuryou(getItemRow(processData.getItemListEx(), GXHDO102B034Const.SLIPRYUURYOU).clone());
        //ｽﾘｯﾌﾟ温度(IN)
        gxhdo102b034model.setSlipondoin(getItemRow(processData.getItemListEx(), GXHDO102B034Const.SLIPONDOIN).clone());
        //ｽﾘｯﾌﾟ温度(OUT)
        gxhdo102b034model.setSlipondoout(getItemRow(processData.getItemListEx(), GXHDO102B034Const.SLIPONDOOUT).clone());
        //高圧分散開始担当者
        gxhdo102b034model.setKouatsubunsankaishitantousya(getItemRow(processData.getItemListEx(), GXHDO102B034Const.KOUATSUBUNSANKAISHITANTOUSYA).clone());
        //高圧分散終了日
        gxhdo102b034model.setKouatsubunsansyuuryou_day(getItemRow(processData.getItemListEx(), GXHDO102B034Const.KOUATSUBUNSANSYUURYOU_DAY).clone());
        //高圧分散終了時間
        gxhdo102b034model.setKouatsubunsansyuuryou_time(getItemRow(processData.getItemListEx(), GXHDO102B034Const.KOUATSUBUNSANSYUURYOU_TIME).clone());
        //高圧分散停止担当者
        gxhdo102b034model.setKouatsubunsanteishitantousya(getItemRow(processData.getItemListEx(), GXHDO102B034Const.KOUATSUBUNSANTEISHITANTOUSYA).clone());
        //備考1
        gxhdo102b034model.setBikou1(getItemRow(processData.getItemListEx(), GXHDO102B034Const.PASSBIKOU1).clone());
        //備考2
        gxhdo102b034model.setBikou2(getItemRow(processData.getItemListEx(), GXHDO102B034Const.PASSBIKOU2).clone());
        return gxhdo102b034model;
    }

    /**
     * ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞ画面の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @param jissekino 実績No
     * @return ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞ画面登録データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrSlipKouatsubunsan> getSubSrSlipKouatsubunsanData(QueryRunner queryRunnerQcdb,
            String rev, String jotaiFlg, String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {
        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSubSrSlipKouatsubunsan(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
        } else {
            return loadTmpSubSrSlipKouatsubunsan(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
        }
    }

    /**
     * [ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞ画面]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param jissekino 実績No
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrSlipKouatsubunsan> loadSubSrSlipKouatsubunsan(QueryRunner queryRunnerQcdb,
            String kojyo, String lotNo, String edaban, int jissekino, String rev) throws SQLException {

        String sql = "SELECT "
                + " kojyo,lotno,edaban,jissekino,pass,souekigawatankno,haisyutsugawatankno,setteiatsuryoku,kaishityokugohaikiryou,"
                + "kouatsubunsankaishinichiji,haikikakunin,jitsuatsuryoku,slipryuuryou,slipondoin,slipondoout,kouatsubunsankaishitantousya,"
                + "kouatsubunsansyuuryounichiji,kouatsubunsanteishitantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision, '0' AS deleteflag "
                + " FROM sub_sr_slip_kouatsubunsan "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? ";

        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            sql += "AND revision = ? ";
        }
        sql += " order by pass ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);

        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            params.add(rev);
        }

        Map<String, String> mapping = new HashMap<>();
        mapping.put("kojyo", "kojyo");                                                 // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                                 // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                               // 枝番
        mapping.put("jissekino", "jissekino");                                         // 実績No
        mapping.put("pass", "pass");                                                   // ﾊﾟｽ回数
        mapping.put("souekigawatankno", "souekigawatankno");                           // 送液側ﾀﾝｸNo.
        mapping.put("haisyutsugawatankno", "haisyutsugawatankno");                     // 排出側ﾀﾝｸNo.
        mapping.put("setteiatsuryoku", "setteiatsuryoku");                             // 設定圧力
        mapping.put("kaishityokugohaikiryou", "kaishityokugohaikiryou");               // 開始直後排気量
        mapping.put("kouatsubunsankaishinichiji", "kouatsubunsankaishinichiji");       // 高圧分散開始日時
        mapping.put("haikikakunin", "haikikakunin");                                   // 廃棄確認
        mapping.put("jitsuatsuryoku", "jitsuatsuryoku");                               // 実圧力(最大値)
        mapping.put("slipryuuryou", "slipryuuryou");                                   // ｽﾘｯﾌﾟ流量
        mapping.put("slipondoin", "slipondoin");                                       // ｽﾘｯﾌﾟ温度(IN)
        mapping.put("slipondoout", "slipondoout");                                     // ｽﾘｯﾌﾟ温度(OUT)
        mapping.put("kouatsubunsankaishitantousya", "kouatsubunsankaishitantousya");   // 高圧分散開始担当者
        mapping.put("kouatsubunsansyuuryounichiji", "kouatsubunsansyuuryounichiji");   // 高圧分散終了日時
        mapping.put("kouatsubunsanteishitantousya", "kouatsubunsanteishitantousya");   // 高圧分散停止担当者
        mapping.put("bikou1", "bikou1");                                               // 備考1
        mapping.put("bikou2", "bikou2");                                               // 備考2
        mapping.put("torokunichiji", "torokunichiji");                                 // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                                   // 更新日時
        mapping.put("revision", "revision");                                           // revision
        mapping.put("deleteflag", "deleteflag");                                       // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SubSrSlipKouatsubunsan>> beanHandler = new BeanListHandler<>(SubSrSlipKouatsubunsan.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞ画面_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param jissekino 実績No
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrSlipKouatsubunsan> loadTmpSubSrSlipKouatsubunsan(QueryRunner queryRunnerQcdb,
            String kojyo, String lotNo, String edaban, int jissekino, String rev) throws SQLException {

        String sql = "SELECT "
                + " kojyo,lotno,edaban,jissekino,pass,souekigawatankno,haisyutsugawatankno,setteiatsuryoku,kaishityokugohaikiryou,"
                + "kouatsubunsankaishinichiji,haikikakunin,jitsuatsuryoku,slipryuuryou,slipondoin,slipondoout,kouatsubunsankaishitantousya,"
                + "kouatsubunsansyuuryounichiji,kouatsubunsanteishitantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + " FROM tmp_sub_sr_slip_kouatsubunsan "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? AND deleteflag = ?  ";

        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            sql += "AND revision = ? ";
        }
        sql += " order by pass ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(0);

        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            params.add(rev);
        }

        Map<String, String> mapping = new HashMap<>();
        mapping.put("kojyo", "kojyo");                                                 // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                                 // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                               // 枝番
        mapping.put("jissekino", "jissekino");                                         // 実績No
        mapping.put("pass", "pass");                                                   // ﾊﾟｽ回数
        mapping.put("souekigawatankno", "souekigawatankno");                           // 送液側ﾀﾝｸNo.
        mapping.put("haisyutsugawatankno", "haisyutsugawatankno");                     // 排出側ﾀﾝｸNo.
        mapping.put("setteiatsuryoku", "setteiatsuryoku");                             // 設定圧力
        mapping.put("kaishityokugohaikiryou", "kaishityokugohaikiryou");               // 開始直後排気量
        mapping.put("kouatsubunsankaishinichiji", "kouatsubunsankaishinichiji");       // 高圧分散開始日時
        mapping.put("haikikakunin", "haikikakunin");                                   // 廃棄確認
        mapping.put("jitsuatsuryoku", "jitsuatsuryoku");                               // 実圧力(最大値)
        mapping.put("slipryuuryou", "slipryuuryou");                                   // ｽﾘｯﾌﾟ流量
        mapping.put("slipondoin", "slipondoin");                                       // ｽﾘｯﾌﾟ温度(IN)
        mapping.put("slipondoout", "slipondoout");                                     // ｽﾘｯﾌﾟ温度(OUT)
        mapping.put("kouatsubunsankaishitantousya", "kouatsubunsankaishitantousya");   // 高圧分散開始担当者
        mapping.put("kouatsubunsansyuuryounichiji", "kouatsubunsansyuuryounichiji");   // 高圧分散終了日時
        mapping.put("kouatsubunsanteishitantousya", "kouatsubunsanteishitantousya");   // 高圧分散停止担当者
        mapping.put("bikou1", "bikou1");                                               // 備考1
        mapping.put("bikou2", "bikou2");                                               // 備考2
        mapping.put("torokunichiji", "torokunichiji");                                 // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                                   // 更新日時
        mapping.put("revision", "revision");                                           // revision
        mapping.put("deleteflag", "deleteflag");                                       // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SubSrSlipKouatsubunsan>> beanHandler = new BeanListHandler<>(SubSrSlipKouatsubunsan.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞ画面_仮登録(tmp_sub_sr_slip_kouatsubunsan)登録処理:更新前のデータを削除した後、画面一覧のデータを利用してInsertする
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void deleteAndInsertTmpSubSrSlipKouatsubunsanDataList(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            BigDecimal newRev, int deleteflag, String kojyo, String lotNo, String edaban, int jissekino, String systemTime) throws SQLException {
        // ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞ画面仮登録(tmp_sub_sr_slip_kouatsubunsan)削除処理
        deleteTmpSubSrSlipKouatsubunsan(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo, edaban, jissekino);
        // ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞ画面_仮登録(tmp_sub_sr_slip_kouatsubunsan)登録処理:画面一覧のデータを利用してInsertする
        insertTmpSubSrSlipKouatsubunsanDataList(queryRunnerQcdb, conQcdb, newRev, deleteflag, kojyo, lotNo, edaban, jissekino, systemTime);
    }

    /**
     * ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞ画面_仮登録(tmp_sub_sr_slip_kouatsubunsan)登録処理:画面一覧のデータを利用してInsertする
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void insertTmpSubSrSlipKouatsubunsanDataList(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal newRev, int deleteflag, String kojyo, String lotNo, String edaban, int jissekino,
            String systemTime) throws SQLException {
        GXHDO102B034B bean = (GXHDO102B034B) getFormBean("beanGXHDO102B034B");
        List<GXHDO102B034Model> passlistdata = bean.getPasslistdata();
        for (GXHDO102B034Model gxhdo102b034model : passlistdata) {
            insertTmpSubSrSlipKouatsubunsan(queryRunnerQcdb, conQcdb, newRev, deleteflag, kojyo, lotNo, edaban, jissekino, systemTime, gxhdo102b034model);
        }
    }

    /**
     * ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞ画面_仮登録(tmp_sub_sr_slip_kouatsubunsan)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param gxhdo102b034model ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞデータ
     * @throws SQLException 例外エラー
     */
    private void insertTmpSubSrSlipKouatsubunsan(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal newRev, int deleteflag, String kojyo, String lotNo, String edaban, int jissekino,
            String systemTime, GXHDO102B034Model gxhdo102b034model) throws SQLException {

        String sql = "INSERT INTO tmp_sub_sr_slip_kouatsubunsan ( "
                + " kojyo,lotno,edaban,jissekino,pass,souekigawatankno,haisyutsugawatankno,setteiatsuryoku,kaishityokugohaikiryou,"
                + "kouatsubunsankaishinichiji,haikikakunin,jitsuatsuryoku,slipryuuryou,slipondoin,slipondoout,kouatsubunsankaishitantousya,"
                + "kouatsubunsansyuuryounichiji,kouatsubunsanteishitantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + " ) VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";

        List<Object> params = setInsertParameterTmpSubSrSlipKouatsubunsan(newRev, deleteflag, kojyo, lotNo, edaban, jissekino, systemTime, gxhdo102b034model);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞ画面仮登録(tmp_sub_sr_slip_kouatsubunsan)更新値パラメータ設定
     *
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param gxhdo102b034model ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞデータ
     * @return 更新パラメータ
     */
    private List<Object> setInsertParameterTmpSubSrSlipKouatsubunsan(BigDecimal newRev,
            int deleteflag, String kojyo, String lotNo, String edaban, int jissekino, String systemTime, GXHDO102B034Model gxhdo102b034model) {
        List<Object> params = new ArrayList<>();
        // 高圧分散開始日
        String kouatsubunsankaishi_day = StringUtil.nullToBlank(gxhdo102b034model.getKouatsubunsankaishi_day().getValue());
        // 高圧分散開始時間
        String kouatsubunsankaishi_time = StringUtil.nullToBlank(gxhdo102b034model.getKouatsubunsankaishi_time().getValue());
        // 高圧分散終了日
        String kouatsubunsansyuuryou_day = StringUtil.nullToBlank(gxhdo102b034model.getKouatsubunsansyuuryou_day().getValue());
        // 高圧分散終了時間
        String kouatsubunsansyuuryou_time = StringUtil.nullToBlank(gxhdo102b034model.getKouatsubunsansyuuryou_time().getValue());

        params.add(kojyo); // 工場ｺｰﾄﾞ
        params.add(lotNo); // ﾛｯﾄNo
        params.add(edaban); // 枝番
        params.add(jissekino); // 実績No
        params.add(DBUtil.stringToStringObjectDefaultNull(gxhdo102b034model.getPass().getValue())); // ﾊﾟｽ回数
        params.add(DBUtil.stringToIntObjectDefaultNull(gxhdo102b034model.getSouekigawatankno().getValue())); // 送液側ﾀﾝｸNo.
        params.add(DBUtil.stringToIntObjectDefaultNull(gxhdo102b034model.getHaisyutsugawatankno().getValue())); // 排出側ﾀﾝｸNo.
        params.add(DBUtil.stringToStringObjectDefaultNull(StringUtil.nullToBlank(gxhdo102b034model.getSetteiatsuryoku().getKikakuChi()).replace("【", "").replace("】", ""))); // 設定圧力
        params.add(DBUtil.stringToStringObjectDefaultNull(StringUtil.nullToBlank(gxhdo102b034model.getKaishityokugohaikiryou().getKikakuChi()).replace("【", "").replace("】", ""))); // 開始直後排気量
        params.add(DBUtil.stringToDateObjectDefaultNull(kouatsubunsankaishi_day, "".equals(kouatsubunsankaishi_time) ? "0000" : kouatsubunsankaishi_time)); // 高圧分散開始日時
        params.add(getCheckBoxDbValue(gxhdo102b034model.getHaikikakunin().getValue(), null)); // 廃棄確認
        params.add(DBUtil.stringToIntObjectDefaultNull(gxhdo102b034model.getJitsuatsuryoku().getValue())); // 実圧力(最大値)
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(gxhdo102b034model.getSlipryuuryou().getValue())); // ｽﾘｯﾌﾟ流量
        params.add(DBUtil.stringToIntObjectDefaultNull(gxhdo102b034model.getSlipondoin().getValue())); // ｽﾘｯﾌﾟ温度(IN)
        params.add(DBUtil.stringToIntObjectDefaultNull(gxhdo102b034model.getSlipondoout().getValue())); // ｽﾘｯﾌﾟ温度(OUT)
        params.add(DBUtil.stringToStringObjectDefaultNull(gxhdo102b034model.getKouatsubunsankaishitantousya().getValue())); // 高圧分散開始担当者
        params.add(DBUtil.stringToDateObjectDefaultNull(kouatsubunsansyuuryou_day, "".equals(kouatsubunsansyuuryou_time) ? "0000" : kouatsubunsansyuuryou_time)); // 高圧分散終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(gxhdo102b034model.getKouatsubunsanteishitantousya().getValue())); // 高圧分散停止担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(gxhdo102b034model.getBikou1().getValue())); // 備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(gxhdo102b034model.getBikou2().getValue())); // 備考2

        params.add(systemTime); //登録日時
        params.add(systemTime); //更新日時
        params.add(newRev); //revision
        params.add(deleteflag); //削除ﾌﾗｸﾞ

        return params;
    }

    /**
     * ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞ画面仮登録(tmp_sub_sr_slip_kouatsubunsan)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSubSrSlipKouatsubunsan(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal rev, String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        String sql = "DELETE FROM tmp_sub_sr_slip_kouatsubunsan "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? AND revision = ?";

        //更新値設定
        List<Object> params = new ArrayList<>();

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞ画面(sub_sr_slip_kouatsubunsan)登録処理:更新前のデータを削除した後、画面一覧のデータを利用してInsertする
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void deleteAndInsertSubSrSlipKouatsubunsanDataList(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            BigDecimal newRev, String kojyo, String lotNo, String edaban, int jissekino, String systemTime) throws SQLException {
        // ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞ画面仮登録(sub_sr_slip_kouatsubunsan)削除処理
        deleteSubSrSlipKouatsubunsan(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo, edaban, jissekino);
        // ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞ画面(sub_sr_slip_kouatsubunsan)登録処理:画面一覧のデータを利用してInsertする
        insertSubSrSlipKouatsubunsanDataList(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo, edaban, jissekino, systemTime);
    }

    /**
     * ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞ画面(sub_sr_slip_kouatsubunsan)登録処理:画面一覧のデータを利用してInsertする
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void insertSubSrSlipKouatsubunsanDataList(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal newRev, String kojyo, String lotNo, String edaban, int jissekino,
            String systemTime) throws SQLException {
        GXHDO102B034B bean = (GXHDO102B034B) getFormBean("beanGXHDO102B034B");
        List<GXHDO102B034Model> passlistdata = bean.getPasslistdata();
        for (GXHDO102B034Model gxhdo102b034model : passlistdata) {
            insertSubSrSlipKouatsubunsan(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo, edaban, jissekino, systemTime, gxhdo102b034model);
        }
    }

    /**
     * ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞ画面(sub_sr_slip_kouatsubunsan)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param gxhdo102b034model ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞデータ
     * @throws SQLException 例外エラー
     */
    private void insertSubSrSlipKouatsubunsan(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal newRev, String kojyo, String lotNo, String edaban, int jissekino,
            String systemTime, GXHDO102B034Model gxhdo102b034model) throws SQLException {
        String sql = "INSERT INTO sub_sr_slip_kouatsubunsan ( "
                + " kojyo,lotno,edaban,jissekino,pass,souekigawatankno,haisyutsugawatankno,setteiatsuryoku,kaishityokugohaikiryou,"
                + "kouatsubunsankaishinichiji,haikikakunin,jitsuatsuryoku,slipryuuryou,slipondoin,slipondoout,kouatsubunsankaishitantousya,"
                + "kouatsubunsansyuuryounichiji,kouatsubunsanteishitantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + " ) VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";

        List<Object> params = setInsertParameterSubSrSlipKouatsubunsan(newRev, kojyo, lotNo, edaban, jissekino, systemTime, gxhdo102b034model);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞ画面登録(tmp_sub_sr_slip_kouatsubunsan)更新値パラメータ設定
     *
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param pass ﾊﾟｽ回数
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param gxhdo102b034model ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞデータ
     * @return 更新パラメータ
     */
    private List<Object> setInsertParameterSubSrSlipKouatsubunsan(BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, String systemTime, GXHDO102B034Model gxhdo102b034model) {
        List<Object> params = new ArrayList<>();
        // 高圧分散開始日
        String kouatsubunsankaishi_day = StringUtil.nullToBlank(gxhdo102b034model.getKouatsubunsankaishi_day().getValue());
        // 高圧分散開始時間
        String kouatsubunsankaishi_time = StringUtil.nullToBlank(gxhdo102b034model.getKouatsubunsankaishi_time().getValue());
        // 高圧分散終了日
        String kouatsubunsansyuuryou_day = StringUtil.nullToBlank(gxhdo102b034model.getKouatsubunsansyuuryou_day().getValue());
        // 高圧分散終了時間
        String kouatsubunsansyuuryou_time = StringUtil.nullToBlank(gxhdo102b034model.getKouatsubunsansyuuryou_time().getValue());

        params.add(kojyo); // 工場ｺｰﾄﾞ
        params.add(lotNo); // ﾛｯﾄNo
        params.add(edaban); // 枝番
        params.add(jissekino); // 実績No
        params.add(DBUtil.stringToStringObject(gxhdo102b034model.getPass().getValue())); // ﾊﾟｽ回数
        params.add(DBUtil.stringToIntObject(gxhdo102b034model.getSouekigawatankno().getValue())); // 送液側ﾀﾝｸNo.
        params.add(DBUtil.stringToIntObject(gxhdo102b034model.getHaisyutsugawatankno().getValue())); // 排出側ﾀﾝｸNo.
        params.add(DBUtil.stringToStringObject(StringUtil.nullToBlank(gxhdo102b034model.getSetteiatsuryoku().getKikakuChi()).replace("【", "").replace("】", ""))); // 設定圧力
        params.add(DBUtil.stringToStringObject(StringUtil.nullToBlank(gxhdo102b034model.getKaishityokugohaikiryou().getKikakuChi()).replace("【", "").replace("】", ""))); // 開始直後排気量
        params.add(DBUtil.stringToDateObject(kouatsubunsankaishi_day, "".equals(kouatsubunsankaishi_time) ? "0000" : kouatsubunsankaishi_time)); // 高圧分散開始日時
        params.add(getCheckBoxDbValue(gxhdo102b034model.getHaikikakunin().getValue(), 9)); // 廃棄確認
        params.add(DBUtil.stringToIntObject(gxhdo102b034model.getJitsuatsuryoku().getValue())); // 実圧力(最大値)
        params.add(DBUtil.stringToBigDecimalObject(gxhdo102b034model.getSlipryuuryou().getValue())); // ｽﾘｯﾌﾟ流量
        params.add(DBUtil.stringToIntObject(gxhdo102b034model.getSlipondoin().getValue())); // ｽﾘｯﾌﾟ温度(IN)
        params.add(DBUtil.stringToIntObject(gxhdo102b034model.getSlipondoout().getValue())); // ｽﾘｯﾌﾟ温度(OUT)
        params.add(DBUtil.stringToStringObject(gxhdo102b034model.getKouatsubunsankaishitantousya().getValue())); // 高圧分散開始担当者
        params.add(DBUtil.stringToDateObject(kouatsubunsansyuuryou_day, "".equals(kouatsubunsansyuuryou_time) ? "0000" : kouatsubunsansyuuryou_time)); // 高圧分散終了日時
        params.add(DBUtil.stringToStringObject(gxhdo102b034model.getKouatsubunsanteishitantousya().getValue())); // 高圧分散停止担当者
        params.add(DBUtil.stringToStringObject(gxhdo102b034model.getBikou1().getValue())); // 備考1
        params.add(DBUtil.stringToStringObject(gxhdo102b034model.getBikou2().getValue())); // 備考2

        params.add(systemTime); //登録日時
        params.add(systemTime); //更新日時
        params.add(newRev); //revision
        return params;
    }

    /**
     * ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞ画面仮登録(tmp_sub_sr_slip_kouatsubunsan)登録処理(削除時)
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void insertDeleteDataTmpSubSrSlipKouatsubunsan(QueryRunner queryRunnerQcdb,
            Connection conQcdb, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, int jissekino, String systemTime) throws SQLException {
        String sql = "INSERT INTO tmp_sub_sr_slip_kouatsubunsan( "
                + " kojyo,lotno,edaban,jissekino,pass,souekigawatankno,haisyutsugawatankno,setteiatsuryoku,kaishityokugohaikiryou,"
                + "kouatsubunsankaishinichiji,haikikakunin,jitsuatsuryoku,slipryuuryou,slipondoin,slipondoout,kouatsubunsankaishitantousya,"
                + "kouatsubunsansyuuryounichiji,kouatsubunsanteishitantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") SELECT "
                + " kojyo,lotno,edaban,jissekino,pass,souekigawatankno,haisyutsugawatankno,setteiatsuryoku,kaishityokugohaikiryou,"
                + "kouatsubunsankaishinichiji,haikikakunin,jitsuatsuryoku,slipryuuryou,slipondoin,slipondoout,kouatsubunsankaishitantousya,"
                + "kouatsubunsansyuuryounichiji,kouatsubunsanteishitantousya,bikou1,bikou2,?,?,?,? "
                + " FROM sub_sr_slip_kouatsubunsan "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ?";

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
        params.add(jissekino); //枝番

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞ画面仮登録(sub_sr_slip_kouatsubunsan)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @throws SQLException 例外エラー
     */
    private void deleteSubSrSlipKouatsubunsan(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal rev, String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        String sql = "DELETE FROM sub_sr_slip_kouatsubunsan "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? AND revision = ?";

        //更新値設定
        List<Object> params = new ArrayList<>();

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 画面項目存在チェック(ﾌｫｰﾑﾊﾟﾗﾒｰﾀに対象の項目が存在していることを確認)
     *
     * @param processData 処理制御データ
     * @return エラー項目名リスト
     */
    private List<String> checkExistFormItem(ProcessData processData) {
        List<String> errorItemNameList = new ArrayList<>();

        checkExistItem(errorItemNameList, processData.getItemListEx(), GXHDO102B034Const.PASS, "ﾊﾟｽ回数");
        checkExistItem(errorItemNameList, processData.getItemListEx(), GXHDO102B034Const.SOUEKIGAWATANKNO, "送液側ﾀﾝｸNo.");
        checkExistItem(errorItemNameList, processData.getItemListEx(), GXHDO102B034Const.HAISYUTSUGAWATANKNO, "排出側ﾀﾝｸNo.");
        checkExistItem(errorItemNameList, processData.getItemListEx(), GXHDO102B034Const.SETTEIATSURYOKU, "設定圧力");
        checkExistItem(errorItemNameList, processData.getItemListEx(), GXHDO102B034Const.KAISHITYOKUGOHAIKIRYOU, "開始直後排気量");
        checkExistItem(errorItemNameList, processData.getItemListEx(), GXHDO102B034Const.KOUATSUBUNSANKAISHI_DAY, "高圧分散開始日");
        checkExistItem(errorItemNameList, processData.getItemListEx(), GXHDO102B034Const.KOUATSUBUNSANKAISHI_TIME, "高圧分散開始時間");
        checkExistItem(errorItemNameList, processData.getItemListEx(), GXHDO102B034Const.HAIKIKAKUNIN, "廃棄確認");
        checkExistItem(errorItemNameList, processData.getItemListEx(), GXHDO102B034Const.JITSUATSURYOKU, "実圧力(最大値)");
        checkExistItem(errorItemNameList, processData.getItemListEx(), GXHDO102B034Const.SLIPRYUURYOU, "ｽﾘｯﾌﾟ流量");
        checkExistItem(errorItemNameList, processData.getItemListEx(), GXHDO102B034Const.SLIPONDOIN, "ｽﾘｯﾌﾟ温度(IN)");
        checkExistItem(errorItemNameList, processData.getItemListEx(), GXHDO102B034Const.SLIPONDOOUT, "ｽﾘｯﾌﾟ温度(OUT)");
        checkExistItem(errorItemNameList, processData.getItemListEx(), GXHDO102B034Const.KOUATSUBUNSANKAISHITANTOUSYA, "高圧分散開始担当者");
        checkExistItem(errorItemNameList, processData.getItemListEx(), GXHDO102B034Const.KOUATSUBUNSANSYUURYOU_DAY, "高圧分散終了日");
        checkExistItem(errorItemNameList, processData.getItemListEx(), GXHDO102B034Const.KOUATSUBUNSANSYUURYOU_TIME, "高圧分散終了時間");
        checkExistItem(errorItemNameList, processData.getItemListEx(), GXHDO102B034Const.KOUATSUBUNSANTEISHITANTOUSYA, "高圧分散停止担当者");
        checkExistItem(errorItemNameList, processData.getItemListEx(), GXHDO102B034Const.PASSBIKOU1, "備考1");
        checkExistItem(errorItemNameList, processData.getItemListEx(), GXHDO102B034Const.PASSBIKOU2, "備考2");

        List<String> errorMassageList = new ArrayList<>();
        if (0 < errorItemNameList.size()) {
            errorMassageList.add("以下の画面項目に対する情報が設定されていません。ｼｽﾃﾑに連絡してください。");
            errorMassageList.add("【対象項目】");
            errorMassageList.addAll(errorItemNameList);
        }

        return errorMassageList;
    }

    /**
     * 対象のIDの項目データがあるか判定(無ければエラー項目名リストに項目名を追加)
     *
     * @param errorItemNameList エラー項目名リスト
     * @param itemList 項目リスト
     * @param itemId 項目ID
     * @param itemName 項目名
     */
    private void checkExistItem(List<String> errorItemNameList, List<FXHDD01> itemList, String itemId, String itemName) {
        if (getItemRow(itemList, itemId) == null) {
            errorItemNameList.add(itemName);
        }
    }

    /**
     * 規格値(ﾊﾟｽ回数)取得
     *
     * @param processData 処理制御データ
     * @param queryRunnerQcdb QueryRunnerオブジェクト(Qcdb)
     * @param shikakariData 前工程WIPから仕掛情報
     * @param lotNo ﾛｯﾄNo
     * @param passMap ﾊﾟｽ回数マップ
     * @throws SQLException 例外エラー
     */
    private void getPassValue(QueryRunner queryRunnerQcdb, Map shikakariData, String lotNo, HashMap<String, String> passMap) throws SQLException {
        String kojyo = lotNo.substring(0, 3);
        String lotNo9 = lotNo.substring(3, 12);
        String edaban = lotNo.substring(12, 15);
        String syurui = "ｽﾘｯﾌﾟ作製";
        passMap.put("errorMessage", "");
        passMap.put("pass", "0");
        // [前工程設計]から、ﾃﾞｰﾀを取得
        Map daMkSekKeiData = loadDaMkSekKeiData(queryRunnerQcdb, kojyo, lotNo9, edaban, syurui);
        if (daMkSekKeiData == null || daMkSekKeiData.isEmpty()) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            passMap.put("errorMessage", MessageUtil.getErrorMessageInfo("XHD-000014", true, false, null).getErrorMessage());
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
            Map daMkhYoJunJokenData = loadDaMkhYoJunJokenData(queryRunnerQcdb, (String) shikakariData.get("hinmei"), pattern, syurui);
            if (daMkhYoJunJokenData == null || daMkhYoJunJokenData.isEmpty()) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                passMap.put("errorMessage", MessageUtil.getErrorMessageInfo("XHD-000028", true, false, null, "規格情報").getErrorMessage());
                return;
            }
            // 前工程規格情報の規格値
            kikakuti = StringUtil.nullToBlank(getMapData(daMkhYoJunJokenData, "kikakuti"));
            if (!NumberUtil.isIntegerNumeric(kikakuti)) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                passMap.put("errorMessage", MessageUtil.getErrorMessageInfo("XHD-000028", true, false, null, "規格情報").getErrorMessage());
                return;
            }
        } else {
            // 前工程規格情報の規格値
            kikakuti = StringUtil.nullToBlank(getMapData(daMkJokenData, "kikakuti"));
            if (!NumberUtil.isIntegerNumeric(kikakuti)) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                passMap.put("errorMessage", MessageUtil.getErrorMessageInfo("XHD-000028", true, false, null, "規格情報").getErrorMessage());
                return;
            }
        }
        passMap.put("pass", kikakuti);
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
        params.add("ｽﾘｯﾌﾟ");
        params.add("高圧分散");
        params.add("ﾊﾟｽ回数");
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
