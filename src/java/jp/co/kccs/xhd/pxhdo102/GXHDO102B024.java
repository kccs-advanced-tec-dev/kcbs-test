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
import jp.co.kccs.xhd.db.model.DaMkJoken;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.db.model.SikakariJson;
import jp.co.kccs.xhd.db.model.SrYuudentaiFunsai;
import jp.co.kccs.xhd.db.model.SubSrYuudentaiFunsai;
import jp.co.kccs.xhd.model.GXHDO102B024Model;
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

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/12/26<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B024(誘電体ｽﾗﾘｰ作製・粉砕)
 *
 * @author KCSS K.Jo
 * @since 2021/12/26
 */
public class GXHDO102B024 implements IFormLogic {

    private static final Logger LOGGER = Logger.getLogger(GXHDO102B024.class.getName());
    private static final String JOTAI_FLG_KARI_TOROKU = "0";
    private static final String JOTAI_FLG_TOROKUZUMI = "1";
    private static final String JOTAI_FLG_SAKUJO = "9";
    private static final String SQL_STATE_RECORD_LOCK_ERR = "55P03";

    /**
     * コンストラクタ
     */
    public GXHDO102B024() {
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

            // 誘電体ｽﾗﾘｰ作製・粉砕画面テーブル初期設定
            initGXHDO102B024A(processData);
            // 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞ画面テーブル初期設定
            initGXHDO102B024B(processData);
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
                    GXHDO102B024Const.BTN_EDABAN_COPY_TOP,
                    GXHDO102B024Const.BTN_EDABAN_COPY_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO102B024Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO102B024Const.BTN_INSERT_TOP,
                    GXHDO102B024Const.BTN_DELETE_TOP,
                    GXHDO102B024Const.BTN_UPDATE_TOP,
                    GXHDO102B024Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO102B024Const.BTN_INSERT_BOTTOM,
                    GXHDO102B024Const.BTN_DELETE_BOTTOM,
                    GXHDO102B024Const.BTN_UPDATE_BOTTOM
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
            case GXHDO102B024Const.BTN_EDABAN_COPY_TOP:
            case GXHDO102B024Const.BTN_EDABAN_COPY_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 仮登録
            case GXHDO102B024Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO102B024Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO102B024Const.BTN_INSERT_TOP:
            case GXHDO102B024Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 修正
            case GXHDO102B024Const.BTN_UPDATE_TOP:
            case GXHDO102B024Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO102B024Const.BTN_DELETE_TOP:
            case GXHDO102B024Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 行追加
            case GXHDO102B024Const.BTN_GYOUTUYIKA_TOP:
            case GXHDO102B024Const.BTN_GYOUTUYIKA_BOTTOM:
                method = "doGyoutuyika";
                break;
            // 行削除
            case GXHDO102B024Const.BTN_GYOUSAKUJYO_TOP:
            case GXHDO102B024Const.BTN_GYOUSAKUJYO_BOTTOM:
                method = "doGyousakujyo";
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

            // 誘電体ｽﾗﾘｰ作製・粉砕の入力項目の登録データ(仮登録時は仮登録データ)を取得
            List<SrYuudentaiFunsai> srYuudentaiFunsaiDataList = getSrYuudentaiFunsaiData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo9, oyalotEdaban);
            if (srYuudentaiFunsaiDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }
            // 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞ画面データ取得
            List<SubSrYuudentaiFunsai> subSrYuudentaiFunsaiDataList = getSubSrYuudentaiFunsaiData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo9, oyalotEdaban);
            if (subSrYuudentaiFunsaiDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }
            // メイン画面データ設定
            setInputItemDataMainForm(processData, srYuudentaiFunsaiDataList.get(0));
            // 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞ画面データ設定
            setInputItemPassListDataMainForm(subSrYuudentaiFunsaiDataList);

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
     * 規格値の入力値チェックを行う。
     * 規格値のエラー対象は引数のリスト(kikakuchiInputErrorInfoList)に項目情報を詰めて返される。
     *
     * @param kikakuchiInputErrorInfoList 規格値入力エラー情報リスト
     * @return チェックの正常終了時はNULL、異常時は内容に応じたエラーメッセージ情報を返す。
     */
    private ErrorMessageInfo checkInputKikakuchi(List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList) {

        // 規格値の入力値チェック必要の項目リスト
        List<FXHDD01> itemList = new ArrayList<>();
        GXHDO102B024B bean = (GXHDO102B024B) getFormBean("beanGXHDO102B024B");
        List<GXHDO102B024Model> listdata = bean.getListdata();
        for (int i = 0; i < listdata.size(); i++) {
            GXHDO102B024Model gxhdo102b024model = listdata.get(i);
            // D50（μm）
            FXHDD01 d50Item = gxhdo102b024model.getD50();
            if (!d50Item.getLabel1().contains("行目: ")) {
                d50Item.setLabel1((i + 1) + "行目: " + d50Item.getLabel1());
            }
            itemList.add(d50Item);
            // BET（㎡/g・%）
            FXHDD01 betItem = gxhdo102b024model.getBet();
            if (!betItem.getLabel1().contains("行目: ")) {
                betItem.setLabel1((i + 1) + "行目: " + betItem.getLabel1());
            }
            itemList.add(betItem);
            // 流量 kg/min or L
            FXHDD01 ryuuryouItem = gxhdo102b024model.getRyuuryou();
            if (!ryuuryouItem.getLabel1().contains("行目: ")) {
                ryuuryouItem.setLabel1((i + 1) + "行目: " + ryuuryouItem.getLabel1());
            }
            FXHDD01 ryuuryoukikakuItem = gxhdo102b024model.getRyuuryoukikaku();
            if (!StringUtil.isEmpty(ryuuryoukikakuItem.getKikakuChi())) {
                ryuuryouItem.setKikakuChi(ryuuryoukikakuItem.getKikakuChi());
                ryuuryouItem.setStandardPattern(ryuuryoukikakuItem.getStandardPattern());
                itemList.add(ryuuryouItem);
            }
            // 停止ﾊﾟｽ
            FXHDD01 teishipassItem = gxhdo102b024model.getTeishipass();
            if (!teishipassItem.getLabel1().contains("行目: ")) {
                teishipassItem.setLabel1((i + 1) + "行目: " + teishipassItem.getLabel1());
            }
            if (i != (listdata.size() - 1)) {
                teishipassItem.setStandardPattern("");
            }
            itemList.add(teishipassItem);
        }
        ErrorMessageInfo errorMessageInfo = ValidateUtil.checkInputKikakuchi(itemList, kikakuchiInputErrorInfoList);

        // エラー項目の背景色を設定
        if (errorMessageInfo == null) {
            kikakuchiInputErrorInfoList.stream().map((kikakuchiinputerrorinfo) -> {
                String itemId = kikakuchiinputerrorinfo.getItemId();
                int kaisuu = Integer.parseInt(kikakuchiinputerrorinfo.getItemLabel().substring(0, kikakuchiinputerrorinfo.getItemLabel().indexOf("行目"))) - 1;
                GXHDO102B024Model gxhdo102b024model = listdata.get(kaisuu);
                FXHDD01 item = getGXHDO102B024ModelItem(itemId, gxhdo102b024model);
                return item;
            }).forEachOrdered((item) -> {
                item.setBackColorInput(ErrUtil.ERR_BACK_COLOR);
            });
        } else {
            errorMessageInfo.setPageChangeItemIndex(-1);
            int kaisuu = Integer.parseInt(errorMessageInfo.getErrorMessage().substring(0, errorMessageInfo.getErrorMessage().indexOf("行目"))) - 1;
            GXHDO102B024Model gxhdo102b024model = listdata.get(kaisuu);
            String itemId = errorMessageInfo.getErrorItemInfoList().get(0).getItemId();
            FXHDD01 item = getGXHDO102B024ModelItem(itemId, gxhdo102b024model);
            if (item != null) {
                item.setBackColorInput(ErrUtil.ERR_BACK_COLOR);
            }
        }
        return errorMessageInfo;
    }

    /**
     * モデルから項目データを取得する。
     *
     * @param itemId 項目ID
     * @param gxhdo102b024model モデルデータ
     * @return 項目データ
     */
    private FXHDD01 getGXHDO102B024ModelItem(String itemId, GXHDO102B024Model gxhdo102b024model) {
        switch (itemId) {
            // D50（μm）
            case GXHDO102B024Const.D50:
                return gxhdo102b024model.getD50();
            // BET（㎡/g・%）
            case GXHDO102B024Const.BET:
                return gxhdo102b024model.getBet();
            // 流量 kg/min or L
            case GXHDO102B024Const.SUB_RYUURYOU:
                return gxhdo102b024model.getRyuuryou();
            // 停止ﾊﾟｽ
            case GXHDO102B024Const.TEISHIPASS:
                return gxhdo102b024model.getTeishipass();
        }
        return null;
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
        ErrorMessageInfo errorMessageInfo = checkInputKikakuchi(kikakuchiInputErrorInfoList);

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

                // 誘電体ｽﾗﾘｰ作製・粉砕_仮登録処理
                insertTmpSrYuudentaiFunsai(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, strSystime, processData);
                // 誘電体ｽﾗﾘｰ作製・粉砕入力ｻﾌﾞ画面の仮登録処理
                insertTmpSubSrYuudentaiFunsaiDataList(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, strSystime);
            } else {

                // 誘電体ｽﾗﾘｰ作製・粉砕_仮登録更新処理
                updateTmpSrYuudentaiFunsai(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);
                // 誘電体ｽﾗﾘｰ作製・粉砕入力ｻﾌﾞ画面の仮登録更新処理
                deleteAndInsertTmpSubSrYuudentaiFunsaiDataList(queryRunnerQcdb, conQcdb, rev, newRev, 0, kojyo, lotNo9, edaban, strSystime);
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
            // 背景色をクリア
            processData.getItemList().forEach((fxhdd01) -> {
                fxhdd01.setBackColorInput(fxhdd01.getBackColorInputDefault());
            });
            GXHDO102B024A.clearListDataBackColor();
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
        ErrorMessageInfo checkItemErrorInfo = checkItemRegistCorrect();
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }

        // 規格チェック
        List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList = new ArrayList<>();
        ErrorMessageInfo errorMessageInfo = checkInputKikakuchi(kikakuchiInputErrorInfoList);

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
     * @return 処理制御データ
     */
    private ErrorMessageInfo checkItemRegistCorrect() {
        ValidateUtil validateUtil = new ValidateUtil();
        GXHDO102B024B bean = (GXHDO102B024B) getFormBean("beanGXHDO102B024B");
        List<GXHDO102B024Model> listdata = bean.getListdata();
        for (GXHDO102B024Model gxhdo102b024model : listdata) {
            // 時刻前後ﾁｪｯｸ
            FXHDD01 itemDay = gxhdo102b024model.getKaishi_day(); // 日付
            FXHDD01 itemKaisiTime = gxhdo102b024model.getKaishi_time(); // 開始時刻
            FXHDD01 itemSyuuryouTime = gxhdo102b024model.getTeishiyotei_time(); // 停止予定時刻
            Date kaisiDate = DateUtil.convertStringToDate(itemDay.getValue(), itemKaisiTime.getValue());
            Date syuuryouDate = DateUtil.convertStringToDate(itemDay.getValue(), itemSyuuryouTime.getValue());
            //R001チェック呼出し
            String msgCheckR001 = validateUtil.checkR001(itemKaisiTime.getLabel1(), kaisiDate, itemSyuuryouTime.getLabel1(), syuuryouDate);
            if (!StringUtil.isEmpty(msgCheckR001)) {
                //エラー発生時
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemKaisiTime, itemSyuuryouTime);
                itemKaisiTime.setBackColorInput(ErrUtil.ERR_BACK_COLOR);
                itemSyuuryouTime.setBackColorInput(ErrUtil.ERR_BACK_COLOR);
                return MessageUtil.getErrorMessageInfo("", msgCheckR001, true, false, errFxhdd01List);
            }

            // 時刻前後ﾁｪｯｸ
            itemSyuuryouTime = gxhdo102b024model.getTeishi_time(); // 停止時刻
            syuuryouDate = DateUtil.convertStringToDate(itemDay.getValue(), itemSyuuryouTime.getValue());
            //R001チェック呼出し
            msgCheckR001 = validateUtil.checkR001(itemKaisiTime.getLabel1(), kaisiDate, itemSyuuryouTime.getLabel1(), syuuryouDate);
            if (!StringUtil.isEmpty(msgCheckR001)) {
                //エラー発生時
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemKaisiTime, itemSyuuryouTime);
                itemKaisiTime.setBackColorInput(ErrUtil.ERR_BACK_COLOR);
                itemSyuuryouTime.setBackColorInput(ErrUtil.ERR_BACK_COLOR);
                return MessageUtil.getErrorMessageInfo("", msgCheckR001, true, false, errFxhdd01List);
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
            SrYuudentaiFunsai tmpSrYuudentaiFunsai = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrYuudentaiFunsai> srYuudentaiFunsaiList = getSrYuudentaiFunsaiData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo9, edaban);
                if (!srYuudentaiFunsaiList.isEmpty()) {
                    tmpSrYuudentaiFunsai = srYuudentaiFunsaiList.get(0);
                }

                deleteTmpSrYuudentaiFunsai(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);
                deleteTmpSubSrYuudentaiFunsai(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);
            }

            // 誘電体ｽﾗﾘｰ作製・粉砕_登録処理
            insertSrYuudentaiFunsai(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, strSystime, processData, tmpSrYuudentaiFunsai);
            // 誘電体ｽﾗﾘｰ作製・粉砕入力ｻﾌﾞ画面の登録処理
            insertSubSrYuudentaiFunsaiDataList(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, strSystime);
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
        ErrorMessageInfo checkItemErrorInfo = checkItemRegistCorrect();
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }

        // 規格チェック
        List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList = new ArrayList<>();
        ErrorMessageInfo errorMessageInfo = checkInputKikakuchi(kikakuchiInputErrorInfoList);

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
        processData.setUserAuthParam(GXHDO102B024Const.USER_AUTH_UPDATE_PARAM);

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

            // 誘電体ｽﾗﾘｰ作製・粉砕_更新処理
            updateSrYuudentaiFunsai(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);
            // 誘電体ｽﾗﾘｰ作製・粉砕入力ｻﾌﾞ画面の更新処理
            deleteAndInsertSubSrYuudentaiFunsaiDataList(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, strSystime);
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
        processData.setUserAuthParam(GXHDO102B024Const.USER_AUTH_DELETE_PARAM);

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

            // 誘電体ｽﾗﾘｰ作製・粉砕_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo9, edaban);
            insertDeleteDataTmpSrYuudentaiFunsai(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo9, edaban, strSystime);

            // 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞ画面仮登録登録処理
            insertDeleteDataTmpSubSrYuudentaiFunsai(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo9, edaban, strSystime);

            // 誘電体ｽﾗﾘｰ作製・粉砕_削除処理
            deleteSrYuudentaiFunsai(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);

            // 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞ画面削除処理
            deleteSubSrYuudentaiFunsai(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);

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
     * 行追加処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doGyoutuyika(ProcessData processData) {
        GXHDO102B024B bean = (GXHDO102B024B) getFormBean("beanGXHDO102B024B");
        List<GXHDO102B024Model> listdata = bean.getListdata();
        int tuyikaRowNo = listdata.size() + 1;
        QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String hinmei = (String) session.getAttribute("hinmei");
        String lotNo = (String) session.getAttribute("lotNo");

        // 流量規格値取得ﾃﾞｰﾀ
        List<DaMkJoken> ryuuryoukikakuListData = new ArrayList<>();
        // ﾊﾟｽ規格値取得ﾃﾞｰﾀ
        List<DaMkJoken> passkikakuListData = new ArrayList<>();
        // 規格値(回数)取得
        try {
            getKaisuuValue(queryRunnerQcdb, hinmei, lotNo, ryuuryoukikakuListData, passkikakuListData);
            listdata.add(createGXHDO102B024Model(processData, tuyikaRowNo, ryuuryoukikakuListData, passkikakuListData));
        } catch (CloneNotSupportedException | SQLException ex) {
            ErrUtil.outputErrorLog("行追加にエラー発生", ex, LOGGER);
        }

        processData.setMethod("");
        return processData;
    }

    /**
     * 行削除処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doGyousakujyo(ProcessData processData) {
        GXHDO102B024B bean = (GXHDO102B024B) getFormBean("beanGXHDO102B024B");
        List<GXHDO102B024Model> listdata = bean.getListdata();
        List<GXHDO102B024Model> removeListdata = new ArrayList();
        listdata.forEach((gxhdo102b024model) -> {
            String checkboxValue = gxhdo102b024model.getDeleterow_checkbox().getValue();
            if ("true".equals(checkboxValue)) {
                if (StringUtil.isEmpty(gxhdo102b024model.getKaishi_day().getValue()) && StringUtil.isEmpty(gxhdo102b024model.getKaishi_time().getValue())
                        && StringUtil.isEmpty(gxhdo102b024model.getTeishiyotei_time().getValue()) && StringUtil.isEmpty(gxhdo102b024model.getTeishi_time().getValue())
                        && StringUtil.isEmpty(gxhdo102b024model.getSyujikudenryuu().getValue()) && StringUtil.isEmpty(gxhdo102b024model.getDeguchiondo().getValue())
                        && StringUtil.isEmpty(gxhdo102b024model.getSealondo().getValue()) && StringUtil.isEmpty(gxhdo102b024model.getPumpmemori().getValue())
                        && StringUtil.isEmpty(gxhdo102b024model.getPumpatsu().getValue()) && StringUtil.isEmpty(gxhdo102b024model.getD50().getValue())
                        && StringUtil.isEmpty(gxhdo102b024model.getBet().getValue()) && StringUtil.isEmpty(gxhdo102b024model.getRyuuryou().getValue())
                        && StringUtil.isEmpty(gxhdo102b024model.getKaishipass().getValue()) && StringUtil.isEmpty(gxhdo102b024model.getTeishipass().getValue())
                        && StringUtil.isEmpty(gxhdo102b024model.getBikou1().getValue()) && StringUtil.isEmpty(gxhdo102b024model.getBikou2().getValue())) {
                    removeListdata.add(gxhdo102b024model);
                }
            }
        });
        removeListdata.forEach((gxhdo102b024model) -> {
            listdata.remove(gxhdo102b024model);
        });
        for (int i = 0; i < listdata.size(); i++) {
            listdata.get(i).setKaisuu(i + 1);
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
                        GXHDO102B024Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B024Const.BTN_UPDATE_TOP,
                        GXHDO102B024Const.BTN_DELETE_TOP,
                        GXHDO102B024Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B024Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B024Const.BTN_DELETE_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B024Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B024Const.BTN_INSERT_TOP,
                        GXHDO102B024Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B024Const.BTN_INSERT_BOTTOM));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO102B024Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B024Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B024Const.BTN_INSERT_TOP,
                        GXHDO102B024Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B024Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B024Const.BTN_INSERT_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B024Const.BTN_UPDATE_TOP,
                        GXHDO102B024Const.BTN_DELETE_TOP,
                        GXHDO102B024Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B024Const.BTN_DELETE_BOTTOM
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
        GXHDO102B024A bean = (GXHDO102B024A) getFormBean("gXHDO102B024A");
        bean.setStyleDisplayNone("display: block;");
        bean.setDisplayStyle("block");
        // 設計情報の取得
        Map mkSekkeiData = this.loadMkSekkeiData(queryRunnerQcdb, queryRunnerWip, lotNo);
        if (mkSekkeiData == null || mkSekkeiData.isEmpty()) {
            errorMessageList.clear();
            errorMessageList.add(MessageUtil.getMessage("XHD-000014"));
            processData.setFatalError(true);
            processData.setInitMessageList(errorMessageList);
            bean.setStyleDisplayNone("display: none;");
            bean.setDisplayStyle("none");
            return processData;
        }

        // 前工程WIPから仕掛情報を取得処理
        Map shikakariData = loadShikakariDataFromWip(queryRunnerDoc, tantoshaCd, lotNo);
        String hinmei = "";
        if (shikakariData == null || shikakariData.isEmpty()) {
            errorMessageList.add(MessageUtil.getMessage("XHD-000029"));
        } else {
            hinmei = StringUtil.nullToBlank(shikakariData.get("hinmei"));
        }
        session.setAttribute("hinmei", hinmei);
        // 流量規格値取得ﾃﾞｰﾀ
        List<DaMkJoken> ryuuryoukikakuListData = new ArrayList<>();
        // ﾊﾟｽ規格値取得ﾃﾞｰﾀ
        List<DaMkJoken> passkikakuListData = new ArrayList<>();

        // 規格値(回数)取得
        ErrorMessageInfo errorMessageInfo = getKaisuuValue(queryRunnerQcdb, hinmei, lotNo, ryuuryoukikakuListData, passkikakuListData);
        if (errorMessageInfo != null) {
            errorMessageList.clear();
            errorMessageList.add(errorMessageInfo.getErrorMessage());
            processData.setFatalError(true);
            processData.setInitMessageList(errorMessageList);
            bean.setStyleDisplayNone("display: none;");
            bean.setDisplayStyle("none");
            return processData;
        }

        // 入力項目の情報を画面にセットする。
        if (!setInputItemData(processData, queryRunnerDoc, queryRunnerQcdb, lotNo, formId, paramJissekino, ryuuryoukikakuListData, passkikakuListData)) {
            // エラー発生時は処理を中断
            processData.setFatalError(true);
            processData.setInitMessageList(Arrays.asList(MessageUtil.getMessage("XHD-000038")));
            bean.setStyleDisplayNone("display: none;");
            bean.setDisplayStyle("none");
            return processData;
        }

        // 画面に取得した情報をセットする。(入力項目以外)
        setViewItemData(processData, shikakariData, lotNo);
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
        this.setItemData(processData, GXHDO102B024Const.WIPLOTNO, lotNo);
        // 誘電体ｽﾗﾘｰ品名
        this.setItemData(processData, GXHDO102B024Const.YUUDENTAIHINMEI, StringUtil.nullToBlank(getMapData(shikakariData, "hinmei")));
        // 誘電体ｽﾗﾘｰLotNo
        this.setItemData(processData, GXHDO102B024Const.YUUDENTAILOTNO, StringUtil.nullToBlank(getMapData(shikakariData, "lotno")));
        // ﾛｯﾄ区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode"));
        // ﾛｯﾄ区分名称
        String lotkubun = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubun"));

        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO102B024Const.LOTKUBUN, "");
        } else {
            if (!StringUtil.isEmpty(lotkubun)) {
                lotkubuncode = lotkubuncode + ":" + lotkubun;
            }
            this.setItemData(processData, GXHDO102B024Const.LOTKUBUN, lotkubuncode);
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
     * @param ryuuryoukikakuListData 流量規格値取得ﾃﾞｰﾀ
     * @param passkikakuListData ﾊﾟｽ規格値取得ﾃﾞｰﾀ
     * @return 設定結果(失敗時false)
     * @throws SQLException 例外エラー
     */
    private boolean setInputItemData(ProcessData processData, QueryRunner queryRunnerDoc, QueryRunner queryRunnerQcdb,
            String lotNo, String formId, int jissekino, List<DaMkJoken> ryuuryoukikakuListData, List<DaMkJoken> passkikakuListData) throws SQLException, CloneNotSupportedException {
        List<SrYuudentaiFunsai> srYuudentaiFunsaiList = new ArrayList<>();
        List<SubSrYuudentaiFunsai> subSrYuudentaiFunsaiList = new ArrayList<>();
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

                // 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞ画面テーブルデータ初期設定
                initGXHDO102B024BData(processData, null, ryuuryoukikakuListData, passkikakuListData);
                // 画面にデータを設定する(デフォルト値)
                processData.getItemList().forEach((fxhdd001) -> {
                    this.setItemData(processData, fxhdd001.getItemId(), fxhdd001.getInputDefault());
                });
                GXHDO102B024B bean = (GXHDO102B024B) getFormBean("beanGXHDO102B024B");
                List<GXHDO102B024Model> listdata = bean.getListdata();
                listdata.forEach((gxhdo102b024model) -> {
                    List<String> kikakuItemList = Arrays.asList(GXHDO102B024Const.RYUURYOUKIKAKU, GXHDO102B024Const.KAISHIPASS, GXHDO102B024Const.TEISHIPASS);
                    processData.getItemListEx().stream().filter((fxhdd001) -> (!kikakuItemList.contains(fxhdd001.getItemId()))).forEachOrdered((fxhdd001) -> {
                        setGXHDO102B024ModelData(gxhdo102b024model, fxhdd001.getItemId(), fxhdd001.getInputDefault());
                    });
                });
                return true;
            }

            // 誘電体ｽﾗﾘｰ作製・粉砕データ取得
            srYuudentaiFunsaiList = getSrYuudentaiFunsaiData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo9, edaban);
            if (srYuudentaiFunsaiList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // 誘電体ｽﾗﾘｰ作製・粉砕入力_サブ画面データ取得
            subSrYuudentaiFunsaiList = getSubSrYuudentaiFunsaiData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo9, edaban);
            if (subSrYuudentaiFunsaiList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }
            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srYuudentaiFunsaiList.isEmpty() || subSrYuudentaiFunsaiList.isEmpty()) {
            return false;
        }
        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);
        // 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞ画面テーブルデータ初期設定
        initGXHDO102B024BData(processData, subSrYuudentaiFunsaiList, ryuuryoukikakuListData, passkikakuListData);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srYuudentaiFunsaiList.get(0));
        // 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞ画面データ設定
        setInputItemPassListDataMainForm(subSrYuudentaiFunsaiList);
        return true;
    }

    /**
     * データ設定処理
     *
     * @param processData 処理制御データ
     * @param srYuudentaiFunsai 誘電体ｽﾗﾘｰ作製・粉砕
     */
    private void setInputItemDataMainForm(ProcessData processData, SrYuudentaiFunsai srYuudentaiFunsai) {
        // 粉砕機
        this.setItemKikuchiData(processData.getItemList(), GXHDO102B024Const.FUNSAIKI, getSrYuudentaiFunsaiItemData(GXHDO102B024Const.FUNSAIKI, srYuudentaiFunsai));
        // 粉砕機洗浄①
        this.setItemKikuchiData(processData.getItemList(), GXHDO102B024Const.FUNSAIKISENJYOU1, getSrYuudentaiFunsaiItemData(GXHDO102B024Const.FUNSAIKISENJYOU1, srYuudentaiFunsai));
        // 粉砕機洗浄②
        this.setItemKikuchiData(processData.getItemList(), GXHDO102B024Const.FUNSAIKISENJYOU2, getSrYuudentaiFunsaiItemData(GXHDO102B024Const.FUNSAIKISENJYOU2, srYuudentaiFunsai));
        // 連続運転回数①
        this.setItemKikuchiData(processData.getItemList(), GXHDO102B024Const.RENZOKUUNTEN1, getSrYuudentaiFunsaiItemData(GXHDO102B024Const.RENZOKUUNTEN1, srYuudentaiFunsai));
        // 連続運転回数②
        this.setItemKikuchiData(processData.getItemList(), GXHDO102B024Const.RENZOKUUNTEN2, getSrYuudentaiFunsaiItemData(GXHDO102B024Const.RENZOKUUNTEN2, srYuudentaiFunsai));
        // 玉石_重量
        this.setItemKikuchiData(processData.getItemList(), GXHDO102B024Const.GYOKUSEKIJYURYOU, getSrYuudentaiFunsaiItemData(GXHDO102B024Const.GYOKUSEKIJYURYOU, srYuudentaiFunsai));
        // 玉石_ﾛｯﾄ
        this.setItemKikuchiData(processData.getItemList(), GXHDO102B024Const.GYOKUSEKILOT, getSrYuudentaiFunsaiItemData(GXHDO102B024Const.GYOKUSEKILOT, srYuudentaiFunsai));
        // 玉石_ﾒﾃﾞｨｱ径
        this.setItemKikuchiData(processData.getItemList(), GXHDO102B024Const.GYOKUSEKIMEDIAKEI, getSrYuudentaiFunsaiItemData(GXHDO102B024Const.GYOKUSEKIMEDIAKEI, srYuudentaiFunsai));
        // 投入量
        this.setItemKikuchiData(processData.getItemList(), GXHDO102B024Const.TOUNYUURYOU, getSrYuudentaiFunsaiItemData(GXHDO102B024Const.TOUNYUURYOU, srYuudentaiFunsai));
        // 時間/回数
        this.setItemKikuchiData(processData.getItemList(), GXHDO102B024Const.ZIKANPASS, getSrYuudentaiFunsaiItemData(GXHDO102B024Const.ZIKANPASS, srYuudentaiFunsai));
        // ｽｸﾘｰﾝ
        this.setItemKikuchiData(processData.getItemList(), GXHDO102B024Const.SCREEN, getSrYuudentaiFunsaiItemData(GXHDO102B024Const.SCREEN, srYuudentaiFunsai));
        // 回転数_ﾃﾞｨｽﾊﾟ
        this.setItemKikuchiData(processData.getItemList(), GXHDO102B024Const.KAITENSUUDISP, getSrYuudentaiFunsaiItemData(GXHDO102B024Const.KAITENSUUDISP, srYuudentaiFunsai));
        // 回転数_主軸
        this.setItemKikuchiData(processData.getItemList(), GXHDO102B024Const.KAITENSUUSYUJIKU, getSrYuudentaiFunsaiItemData(GXHDO102B024Const.KAITENSUUSYUJIKU, srYuudentaiFunsai));
        // ﾎﾟﾝﾌﾟ出力
        this.setItemKikuchiData(processData.getItemList(), GXHDO102B024Const.POMPSYUTSURYOKU, getSrYuudentaiFunsaiItemData(GXHDO102B024Const.POMPSYUTSURYOKU, srYuudentaiFunsai));
        // 流量
        this.setItemKikuchiData(processData.getItemList(), GXHDO102B024Const.RYUURYOU, getSrYuudentaiFunsaiItemData(GXHDO102B024Const.RYUURYOU, srYuudentaiFunsai));
        // 回数
        this.setItemKikuchiData(processData.getItemList(), GXHDO102B024Const.PASSKAISUU, getSrYuudentaiFunsaiItemData(GXHDO102B024Const.PASSKAISUU, srYuudentaiFunsai));
    }

    /**
     * 項目データ設定
     *
     * @param itemList 項目データ
     * @param itemId 項目ID
     * @param value 設定値
     * @return 処理制御データ
     */
    private void setItemKikuchiData(List<FXHDD01> itemList, String itemId, String value) {
        List<FXHDD01> selectData
                = itemList.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            selectData.get(0).setKikakuChi("【" + value + "】");
        }
    }

    /**
     * データリストからデータ設定処理
     *
     * @param subSrYuudentaiFunsaiList 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞデータ
     */
    private void setInputItemPassListDataMainForm(List<SubSrYuudentaiFunsai> subSrYuudentaiFunsaiList) {
        GXHDO102B024B bean = (GXHDO102B024B) getFormBean("beanGXHDO102B024B");
        List<GXHDO102B024Model> listdata = bean.getListdata();
        for (int i = 0; i < listdata.size(); i++) {
            GXHDO102B024Model gxhdo102b024model = listdata.get(i);
            Integer kaisuu = gxhdo102b024model.getKaisuu();
            SubSrYuudentaiFunsai subSrYuudentaiFunsai = getSubSrYuudentaiFunsaiData(subSrYuudentaiFunsaiList, kaisuu);
            setInputItemSubDataMainForm(gxhdo102b024model, subSrYuudentaiFunsai);
        }
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞデータ取得
     *
     * @param subSrYuudentaiFunsaiList 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞデータリスト
     * @param pass 回数
     * @return 項目データ
     */
    private SubSrYuudentaiFunsai getSubSrYuudentaiFunsaiData(List<SubSrYuudentaiFunsai> subSrYuudentaiFunsaiList, Integer kaisuu) {
        List<SubSrYuudentaiFunsai> selectData
                = subSrYuudentaiFunsaiList.stream().filter(n -> kaisuu.compareTo(n.getKaisuu()) == 0).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0);
        } else {
            return null;
        }
    }

    /**
     * データ設定処理
     *
     * @param gxhdo102b024model 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞモデルデータ
     * @param subSrYuudentaiFunsai 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞデータ
     */
    private void setInputItemSubDataMainForm(GXHDO102B024Model gxhdo102b024model, SubSrYuudentaiFunsai subSrYuudentaiFunsai) {
        if (subSrYuudentaiFunsai == null) {
            return;
        }
        // 仮登録・登録済みの場合、テーブルからデータを取得して表示
        // 流量規格
        this.setGXHDO102B024ModelData(gxhdo102b024model, GXHDO102B024Const.RYUURYOUKIKAKU, getSubSrYuudentaiFunsaiItemData(GXHDO102B024Const.RYUURYOUKIKAKU, subSrYuudentaiFunsai));
        // 開始ﾊﾟｽ
        this.setGXHDO102B024ModelData(gxhdo102b024model, GXHDO102B024Const.KAISHIPASS, getSubSrYuudentaiFunsaiItemData(GXHDO102B024Const.KAISHIPASS, subSrYuudentaiFunsai));
        // 停止ﾊﾟｽ
        this.setGXHDO102B024ModelData(gxhdo102b024model, GXHDO102B024Const.TEISHIPASS, getSubSrYuudentaiFunsaiItemData(GXHDO102B024Const.TEISHIPASS, subSrYuudentaiFunsai));

        // 回数
        gxhdo102b024model.setKaisuu(Integer.parseInt(StringUtil.nullToBlank(subSrYuudentaiFunsai.getKaisuu())));
        //日付
        this.setGXHDO102B024ModelData(gxhdo102b024model, GXHDO102B024Const.KAISHI_DAY, getSubSrYuudentaiFunsaiItemData(GXHDO102B024Const.KAISHI_DAY, subSrYuudentaiFunsai));
        // 開始時刻      
        this.setGXHDO102B024ModelData(gxhdo102b024model, GXHDO102B024Const.KAISHI_TIME, getSubSrYuudentaiFunsaiItemData(GXHDO102B024Const.KAISHI_TIME, subSrYuudentaiFunsai));
        // 停止予定時刻           
        this.setGXHDO102B024ModelData(gxhdo102b024model, GXHDO102B024Const.TEISHIYOTEI_TIME, getSubSrYuudentaiFunsaiItemData(GXHDO102B024Const.TEISHIYOTEI_TIME, subSrYuudentaiFunsai));
        // 停止時刻           
        this.setGXHDO102B024ModelData(gxhdo102b024model, GXHDO102B024Const.TEISHI_TIME, getSubSrYuudentaiFunsaiItemData(GXHDO102B024Const.TEISHI_TIME, subSrYuudentaiFunsai));
        // 主軸電流（A）           
        this.setGXHDO102B024ModelData(gxhdo102b024model, GXHDO102B024Const.SYUJIKUDENRYUU, getSubSrYuudentaiFunsaiItemData(GXHDO102B024Const.SYUJIKUDENRYUU, subSrYuudentaiFunsai));
        // 出口温度（℃）           
        this.setGXHDO102B024ModelData(gxhdo102b024model, GXHDO102B024Const.DEGUCHIONDO, getSubSrYuudentaiFunsaiItemData(GXHDO102B024Const.DEGUCHIONDO, subSrYuudentaiFunsai));
        // ｼｰﾙ温度（℃）           
        this.setGXHDO102B024ModelData(gxhdo102b024model, GXHDO102B024Const.SEALONDO, getSubSrYuudentaiFunsaiItemData(GXHDO102B024Const.SEALONDO, subSrYuudentaiFunsai));
        // ﾎﾟﾝﾌﾟ目盛（rpm）           
        this.setGXHDO102B024ModelData(gxhdo102b024model, GXHDO102B024Const.PUMPMEMORI, getSubSrYuudentaiFunsaiItemData(GXHDO102B024Const.PUMPMEMORI, subSrYuudentaiFunsai));
        // ﾎﾟﾝﾌﾟ圧（Mpa）           
        this.setGXHDO102B024ModelData(gxhdo102b024model, GXHDO102B024Const.PUMPATSU, getSubSrYuudentaiFunsaiItemData(GXHDO102B024Const.PUMPATSU, subSrYuudentaiFunsai));
        // D50（μm）           
        this.setGXHDO102B024ModelData(gxhdo102b024model, GXHDO102B024Const.D50, getSubSrYuudentaiFunsaiItemData(GXHDO102B024Const.D50, subSrYuudentaiFunsai));
        // BET（㎡/g・%）           
        this.setGXHDO102B024ModelData(gxhdo102b024model, GXHDO102B024Const.BET, getSubSrYuudentaiFunsaiItemData(GXHDO102B024Const.BET, subSrYuudentaiFunsai));
        // 流量 kg/min or  L           
        this.setGXHDO102B024ModelData(gxhdo102b024model, GXHDO102B024Const.SUB_RYUURYOU, getSubSrYuudentaiFunsaiItemData(GXHDO102B024Const.SUB_RYUURYOU, subSrYuudentaiFunsai));
        // 備考1           
        this.setGXHDO102B024ModelData(gxhdo102b024model, GXHDO102B024Const.BIKOU1, getSubSrYuudentaiFunsaiItemData(GXHDO102B024Const.BIKOU1, subSrYuudentaiFunsai));
        // 備考2           
        this.setGXHDO102B024ModelData(gxhdo102b024model, GXHDO102B024Const.BIKOU2, getSubSrYuudentaiFunsaiItemData(GXHDO102B024Const.BIKOU2, subSrYuudentaiFunsai));
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・粉砕の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return 誘電体ｽﾗﾘｰ作製・粉砕データ
     * @throws SQLException 例外エラー
     */
    private List<SrYuudentaiFunsai> getSrYuudentaiFunsaiData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrYuudentaiFunsai(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSrYuudentaiFunsai(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
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
     * [誘電体ｽﾗﾘｰ作製・粉砕]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrYuudentaiFunsai> loadSrYuudentaiFunsai(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + " kojyo,lotno,edaban,yuudentaihinmei,yuudentailotno,lotkubun,genryoulotno,genryoukigou,funsaiki,funsaikisenjyou1,funsaikisenjyou2,"
                + "renzokuunten1,renzokuunten2,gyokusekijyuryou,gyokusekilot,gyokusekimediakei,tounyuuryou,zikanpass,screen,kaitensuudisp,"
                + "kaitensuusyujiku,pompsyutsuryoku,ryuuryou,passkaisuu,kaishinichiji,syuuryounichiji,bikou1,bikou2,"
                + "torokunichiji,kosinnichiji,revision "
                + " FROM sr_yuudentai_funsai "
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
        mapping.put("kojyo", "kojyo");                          // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                          // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                        // 枝番
        mapping.put("yuudentaihinmei", "yuudentaihinmei");      // 誘電体ｽﾗﾘｰ品名
        mapping.put("yuudentailotno", "yuudentailotno");        // 誘電体ｽﾗﾘｰLotNo
        mapping.put("lotkubun", "lotkubun");                    // ﾛｯﾄ区分
        mapping.put("genryoulotno", "genryoulotno");            // 原料LotNo
        mapping.put("genryoukigou", "genryoukigou");            // 原料記号
        mapping.put("funsaiki", "funsaiki");                    // 粉砕機
        mapping.put("funsaikisenjyou1", "funsaikisenjyou1");    // 粉砕機洗浄①
        mapping.put("funsaikisenjyou2", "funsaikisenjyou2");    // 粉砕機洗浄②
        mapping.put("renzokuunten1", "renzokuunten1");          // 連続運転回数①
        mapping.put("renzokuunten2", "renzokuunten2");          // 連続運転回数②
        mapping.put("gyokusekijyuryou", "gyokusekijyuryou");    // 玉石_重量
        mapping.put("gyokusekilot", "gyokusekilot");            // 玉石_ﾛｯﾄ
        mapping.put("gyokusekimediakei", "gyokusekimediakei");  // 玉石_ﾒﾃﾞｨｱ径
        mapping.put("tounyuuryou", "tounyuuryou");              // 投入量
        mapping.put("zikanpass", "zikanpass");                  // 時間/回数
        mapping.put("screen", "screen");                        // ｽｸﾘｰﾝ
        mapping.put("kaitensuudisp", "kaitensuudisp");          // 回転数_ﾃﾞｨｽﾊﾟ
        mapping.put("kaitensuusyujiku", "kaitensuusyujiku");    // 回転数_主軸
        mapping.put("pompsyutsuryoku", "pompsyutsuryoku");      // ﾎﾟﾝﾌﾟ出力
        mapping.put("ryuuryou", "ryuuryou");                    // 流量
        mapping.put("passkaisuu", "passkaisuu");                // 回数
        mapping.put("kaishinichiji", "kaishinichiji");          // 開始日時
        mapping.put("syuuryounichiji", "syuuryounichiji");      // 終了日時
        mapping.put("bikou1", "bikou1");                        // 備考1
        mapping.put("bikou2", "bikou2");                        // 備考2
        mapping.put("torokunichiji", "torokunichiji");          // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");            // 更新日時
        mapping.put("revision", "revision");                    // revision

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrYuudentaiFunsai>> beanHandler = new BeanListHandler<>(SrYuudentaiFunsai.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [誘電体ｽﾗﾘｰ作製・粉砕_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrYuudentaiFunsai> loadTmpSrYuudentaiFunsai(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + " kojyo,lotno,edaban,yuudentaihinmei,yuudentailotno,lotkubun,genryoulotno,genryoukigou,funsaiki,funsaikisenjyou1,funsaikisenjyou2,"
                + "renzokuunten1,renzokuunten2,gyokusekijyuryou,gyokusekilot,gyokusekimediakei,tounyuuryou,zikanpass,screen,kaitensuudisp,"
                + "kaitensuusyujiku,pompsyutsuryoku,ryuuryou,passkaisuu,kaishinichiji,syuuryounichiji,bikou1,bikou2,"
                + "torokunichiji,kosinnichiji,revision,deleteflag "
                + " FROM tmp_sr_yuudentai_funsai "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND deleteflag = ?  ";

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
        mapping.put("kojyo", "kojyo");                          // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                          // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                        // 枝番
        mapping.put("yuudentaihinmei", "yuudentaihinmei");      // 誘電体ｽﾗﾘｰ品名
        mapping.put("yuudentailotno", "yuudentailotno");        // 誘電体ｽﾗﾘｰLotNo
        mapping.put("lotkubun", "lotkubun");                    // ﾛｯﾄ区分
        mapping.put("genryoulotno", "genryoulotno");            // 原料LotNo
        mapping.put("genryoukigou", "genryoukigou");            // 原料記号
        mapping.put("funsaiki", "funsaiki");                    // 粉砕機
        mapping.put("funsaikisenjyou1", "funsaikisenjyou1");    // 粉砕機洗浄①
        mapping.put("funsaikisenjyou2", "funsaikisenjyou2");    // 粉砕機洗浄②
        mapping.put("renzokuunten1", "renzokuunten1");          // 連続運転回数①
        mapping.put("renzokuunten2", "renzokuunten2");          // 連続運転回数②
        mapping.put("gyokusekijyuryou", "gyokusekijyuryou");    // 玉石_重量
        mapping.put("gyokusekilot", "gyokusekilot");            // 玉石_ﾛｯﾄ
        mapping.put("gyokusekimediakei", "gyokusekimediakei");  // 玉石_ﾒﾃﾞｨｱ径
        mapping.put("tounyuuryou", "tounyuuryou");              // 投入量
        mapping.put("zikanpass", "zikanpass");                  // 時間/回数
        mapping.put("screen", "screen");                        // ｽｸﾘｰﾝ
        mapping.put("kaitensuudisp", "kaitensuudisp");          // 回転数_ﾃﾞｨｽﾊﾟ
        mapping.put("kaitensuusyujiku", "kaitensuusyujiku");    // 回転数_主軸
        mapping.put("pompsyutsuryoku", "pompsyutsuryoku");      // ﾎﾟﾝﾌﾟ出力
        mapping.put("ryuuryou", "ryuuryou");                    // 流量
        mapping.put("passkaisuu", "passkaisuu");                // 回数
        mapping.put("kaishinichiji", "kaishinichiji");          // 開始日時
        mapping.put("syuuryounichiji", "syuuryounichiji");      // 終了日時
        mapping.put("bikou1", "bikou1");                        // 備考1
        mapping.put("bikou2", "bikou2");                        // 備考2
        mapping.put("torokunichiji", "torokunichiji");          // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");            // 更新日時
        mapping.put("revision", "revision");                    // revision
        mapping.put("deleteflag", "deleteflag");                // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrYuudentaiFunsai>> beanHandler = new BeanListHandler<>(SrYuudentaiFunsai.class, rowProcessor);

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
     * @param gxhdo102b024model 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞモデルデータ
     * @param itemId 項目ID
     * @param value 設定値
     * @return 処理制御データ
     */
    private void setGXHDO102B024ModelData(GXHDO102B024Model gxhdo102b024model, String itemId, String value) {
        switch (itemId) {
            //日付
            case GXHDO102B024Const.KAISHI_DAY:
                gxhdo102b024model.getKaishi_day().setValue(value);
                break;
            // 開始時刻           
            case GXHDO102B024Const.KAISHI_TIME:
                gxhdo102b024model.getKaishi_time().setValue(value);
                break;
            // 停止予定時刻           
            case GXHDO102B024Const.TEISHIYOTEI_TIME:
                gxhdo102b024model.getTeishiyotei_time().setValue(value);
                break;
            // 停止時刻           
            case GXHDO102B024Const.TEISHI_TIME:
                gxhdo102b024model.getTeishi_time().setValue(value);
                break;
            // 主軸電流（A）           
            case GXHDO102B024Const.SYUJIKUDENRYUU:
                gxhdo102b024model.getSyujikudenryuu().setValue(value);
                break;
            // 出口温度（℃）           
            case GXHDO102B024Const.DEGUCHIONDO:
                gxhdo102b024model.getDeguchiondo().setValue(value);
                break;
            // ｼｰﾙ温度（℃）           
            case GXHDO102B024Const.SEALONDO:
                gxhdo102b024model.getSealondo().setValue(value);
                break;
            // ﾎﾟﾝﾌﾟ目盛（rpm）           
            case GXHDO102B024Const.PUMPMEMORI:
                gxhdo102b024model.getPumpmemori().setValue(value);
                break;
            // ﾎﾟﾝﾌﾟ圧（Mpa）           
            case GXHDO102B024Const.PUMPATSU:
                gxhdo102b024model.getPumpatsu().setValue(value);
                break;
            // D50（μm）           
            case GXHDO102B024Const.D50:
                gxhdo102b024model.getD50().setValue(value);
                break;
            // BET（㎡/g・%）           
            case GXHDO102B024Const.BET:
                gxhdo102b024model.getBet().setValue(value);
                break;
            // 流量規格        
            case GXHDO102B024Const.RYUURYOUKIKAKU:
                gxhdo102b024model.getRyuuryoukikaku().setKikakuChi(value);
                break;
            // 流量 kg/min or  L           
            case GXHDO102B024Const.SUB_RYUURYOU:
                gxhdo102b024model.getRyuuryou().setValue(value);
                break;
            // 開始ﾊﾟｽ        
            case GXHDO102B024Const.KAISHIPASS:
                gxhdo102b024model.getKaishipass().setValue(value);
                break;
            // 停止ﾊﾟｽ        
            case GXHDO102B024Const.TEISHIPASS:
                gxhdo102b024model.getTeishipass().setValue(value);
                break;
            // 備考1           
            case GXHDO102B024Const.BIKOU1:
                gxhdo102b024model.getBikou1().setValue(value);
                break;
            // 備考2           
            case GXHDO102B024Const.BIKOU2:
                gxhdo102b024model.getBikou2().setValue(value);
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
     * @param srYuudentaiFunsai 誘電体ｽﾗﾘｰ作製・粉砕データ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrYuudentaiFunsai srYuudentaiFunsai) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srYuudentaiFunsai != null) {
            // 元データが存在する場合元データより取得
            return getSrYuudentaiFunsaiItemData(itemId, srYuudentaiFunsai);
        } else {
            return null;
        }
    }

    /**
     * 項目データ(入力値)取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @param srYuudentaiFunsai 誘電体ｽﾗﾘｰ作製・粉砕データ
     * @return 入力値
     */
    private String getItemKikakuchi(List<FXHDD01> listData, String itemId, SrYuudentaiFunsai srYuudentaiFunsai) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return StringUtil.nullToBlank(selectData.get(0).getKikakuChi()).replace("【", "").replace("】", "");
        } else if (srYuudentaiFunsai != null) {
            // 元データが存在する場合元データより取得
            return getSrYuudentaiFunsaiItemData(itemId, srYuudentaiFunsai);
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
    public static Object getMapData(Map map, String mapId) {
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
     * 誘電体ｽﾗﾘｰ作製・粉砕_仮登録(tmp_sr_yuudentai_funsai)登録処理
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
    private void insertTmpSrYuudentaiFunsai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "INSERT INTO tmp_sr_yuudentai_funsai ( "
                + " kojyo,lotno,edaban,yuudentaihinmei,yuudentailotno,lotkubun,genryoulotno,genryoukigou,funsaiki,funsaikisenjyou1,funsaikisenjyou2,"
                + "renzokuunten1,renzokuunten2,gyokusekijyuryou,gyokusekilot,gyokusekimediakei,tounyuuryou,zikanpass,screen,kaitensuudisp,"
                + "kaitensuusyujiku,pompsyutsuryoku,ryuuryou,passkaisuu,kaishinichiji,syuuryounichiji,bikou1,bikou2,"
                + "torokunichiji,kosinnichiji,revision,deleteflag "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrYuudentaiFunsai(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, processData, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・粉砕_仮登録(tmp_sr_yuudentai_funsai)更新処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void updateTmpSrYuudentaiFunsai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "UPDATE tmp_sr_yuudentai_funsai SET "
                + " yuudentaihinmei = ?,yuudentailotno = ?,lotkubun = ?,genryoulotno = ?,genryoukigou = ?,funsaiki = ?,funsaikisenjyou1 = ?,funsaikisenjyou2 = ?,renzokuunten1 = ?,"
                + "renzokuunten2 = ?,gyokusekijyuryou = ?,gyokusekilot = ?,gyokusekimediakei = ?,tounyuuryou = ?,zikanpass = ?,screen = ?,kaitensuudisp = ?,"
                + "kaitensuusyujiku = ?,pompsyutsuryoku = ?,ryuuryou = ?,passkaisuu = ?,kaishinichiji = ?,syuuryounichiji = ?,bikou1 = ?,bikou2 = ?,"
                + "kosinnichiji = ?,revision = ?,deleteflag = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrYuudentaiFunsai> srYuudentaiFunsaiList = getSrYuudentaiFunsaiData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrYuudentaiFunsai srYuudentaiFunsai = null;
        if (!srYuudentaiFunsaiList.isEmpty()) {
            srYuudentaiFunsai = srYuudentaiFunsaiList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrYuudentaiFunsai(false, newRev, 0, "", "", "", systemTime, processData, srYuudentaiFunsai);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・粉砕_仮登録(tmp_sr_yuudentai_funsai)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrYuudentaiFunsai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sr_yuudentai_funsai "
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
     * 誘電体ｽﾗﾘｰ作製・粉砕_仮登録(tmp_sr_yuudentai_funsai)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srYuudentaiFunsai 誘電体ｽﾗﾘｰ作製・粉砕データ
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrYuudentaiFunsai(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, String systemTime, ProcessData processData, SrYuudentaiFunsai srYuudentaiFunsai) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B024Const.YUUDENTAIHINMEI, srYuudentaiFunsai)));          // 誘電体ｽﾗﾘｰ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B024Const.YUUDENTAILOTNO, srYuudentaiFunsai)));           // 誘電体ｽﾗﾘｰLotNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B024Const.LOTKUBUN, srYuudentaiFunsai)));                 // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B024Const.GENRYOULOTNO, srYuudentaiFunsai)));        // 原料LotNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B024Const.GENRYOUKIGOU, srYuudentaiFunsai)));        // 原料記号
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B024Const.FUNSAIKI, srYuudentaiFunsai)));            // 粉砕機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B024Const.FUNSAIKISENJYOU1, srYuudentaiFunsai)));    // 粉砕機洗浄①
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B024Const.FUNSAIKISENJYOU2, srYuudentaiFunsai)));    // 粉砕機洗浄②
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B024Const.RENZOKUUNTEN1, srYuudentaiFunsai)));       // 連続運転回数①
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B024Const.RENZOKUUNTEN2, srYuudentaiFunsai)));       // 連続運転回数②
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B024Const.GYOKUSEKIJYURYOU, srYuudentaiFunsai)));    // 玉石_重量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B024Const.GYOKUSEKILOT, srYuudentaiFunsai)));        // 玉石_ﾛｯﾄ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B024Const.GYOKUSEKIMEDIAKEI, srYuudentaiFunsai)));   // 玉石_ﾒﾃﾞｨｱ径
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B024Const.TOUNYUURYOU, srYuudentaiFunsai)));         // 投入量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B024Const.ZIKANPASS, srYuudentaiFunsai)));           // 時間/回数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B024Const.SCREEN, srYuudentaiFunsai)));              // ｽｸﾘｰﾝ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B024Const.KAITENSUUDISP, srYuudentaiFunsai)));       // 回転数_ﾃﾞｨｽﾊﾟ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B024Const.KAITENSUUSYUJIKU, srYuudentaiFunsai)));    // 回転数_主軸
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B024Const.POMPSYUTSURYOKU, srYuudentaiFunsai)));     // ﾎﾟﾝﾌﾟ出力
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B024Const.RYUURYOU, srYuudentaiFunsai)));            // 流量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B024Const.PASSKAISUU, srYuudentaiFunsai)));          // 回数
        params.add(getkaishiAndTeishiDate(null).get(0));                                                                                           // 開始日時
        params.add(getkaishiAndTeishiDate(null).get(1));                                                                                           // 終了日時
        params.add(null);                                                                                                                          // 備考1
        params.add(null);                                                                                                                          // 備考2

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
     * 【誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞ.開始日時】で一番小さい日時と【誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞ.停止日時】で一番大きい日時を取得
     *
     * @param defaultValue デフォルト値
     * @return 一番小さい開始日時と一番大きい停止日時
     */
    private ArrayList<String> getkaishiAndTeishiDate(String defaultValue) {
        ArrayList<String> kaishiAndTeishiDateList = new ArrayList<>();
        GXHDO102B024B bean = (GXHDO102B024B) getFormBean("beanGXHDO102B024B");
        List<GXHDO102B024Model> listdata = bean.getListdata();
        List<Date> kaishiDateList = new ArrayList<>();
        List<Date> teishiDateList = new ArrayList<>();
        listdata.forEach((gxhdo102b024model) -> {
            String kaishiDayStr = StringUtil.nullToBlank(gxhdo102b024model.getKaishi_day().getValue()); // 開始日
            String kaishiTimeStr = StringUtil.nullToBlank(gxhdo102b024model.getKaishi_time().getValue()); // 開始時刻
            String teishiTimeStr = StringUtil.nullToBlank(gxhdo102b024model.getTeishi_time().getValue()); // 停止時刻
            Date kaishiDate = DateUtil.convertStringToDate(kaishiDayStr, kaishiTimeStr); // 開始日時
            Date teishiDate = DateUtil.convertStringToDate(kaishiDayStr, teishiTimeStr); // 停止日時
            if (kaishiDate != null) {
                kaishiDateList.add(kaishiDate);
            }
            if (teishiDate != null) {
                teishiDateList.add(teishiDate);
            }
        });
        kaishiDateList.sort((date1, date2) -> date1.compareTo(date2));
        teishiDateList.sort((date1, date2) -> date2.compareTo(date1));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String kaishiDateValue = defaultValue;
        String teishiDateValue = defaultValue;
        if (kaishiDateList.size() > 0 && kaishiDateList.get(0) != null) {
            kaishiDateValue = sdf.format(kaishiDateList.get(0));
        }
        if (teishiDateList.size() > 0 && teishiDateList.get(0) != null) {
            teishiDateValue = sdf.format(teishiDateList.get(0));
        }

        kaishiAndTeishiDateList.add(kaishiDateValue);
        kaishiAndTeishiDateList.add(teishiDateValue);
        return kaishiAndTeishiDateList;
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・粉砕(sr_yuudentai_funsai)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param tmpSrYuudentaiFunsai 仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrYuudentaiFunsai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData, SrYuudentaiFunsai tmpSrYuudentaiFunsai) throws SQLException {

        String sql = "INSERT INTO sr_yuudentai_funsai ( "
                + " kojyo,lotno,edaban,yuudentaihinmei,yuudentailotno,lotkubun,genryoulotno,genryoukigou,funsaiki,funsaikisenjyou1,funsaikisenjyou2,"
                + "renzokuunten1,renzokuunten2,gyokusekijyuryou,gyokusekilot,gyokusekimediakei,tounyuuryou,zikanpass,screen,kaitensuudisp,"
                + "kaitensuusyujiku,pompsyutsuryoku,ryuuryou,passkaisuu,kaishinichiji,syuuryounichiji,bikou1,bikou2,"
                + "torokunichiji,kosinnichiji,revision "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrYuudentaiFunsai(true, newRev, kojyo, lotNo, edaban, systemTime, processData, tmpSrYuudentaiFunsai);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・粉砕(sr_yuudentai_funsai)更新処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void updateSrYuudentaiFunsai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {
        String sql = "UPDATE sr_yuudentai_funsai SET "
                + " yuudentaihinmei = ?,yuudentailotno = ?,lotkubun = ?,genryoulotno = ?,genryoukigou = ?,funsaiki = ?,funsaikisenjyou1 = ?,funsaikisenjyou2 = ?,renzokuunten1 = ?,"
                + "renzokuunten2 = ?,gyokusekijyuryou = ?,gyokusekilot = ?,gyokusekimediakei = ?,tounyuuryou = ?,zikanpass = ?,screen = ?,kaitensuudisp = ?,"
                + "kaitensuusyujiku = ?,pompsyutsuryoku = ?,ryuuryou = ?,passkaisuu = ?,kaishinichiji = ?,syuuryounichiji = ?,bikou1 = ?,bikou2 = ?,"
                + "kosinnichiji = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrYuudentaiFunsai> srYuudentaiFunsaiList = getSrYuudentaiFunsaiData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrYuudentaiFunsai srYuudentaiFunsai = null;
        if (!srYuudentaiFunsaiList.isEmpty()) {
            srYuudentaiFunsai = srYuudentaiFunsaiList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrYuudentaiFunsai(false, newRev, "", "", "", systemTime, processData, srYuudentaiFunsai);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・粉砕(sr_yuudentai_funsai)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srYuudentaiFunsai 誘電体ｽﾗﾘｰ作製・粉砕データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrYuudentaiFunsai(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            String systemTime, ProcessData processData, SrYuudentaiFunsai srYuudentaiFunsai) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B024Const.YUUDENTAIHINMEI, srYuudentaiFunsai)));          // 誘電体ｽﾗﾘｰ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B024Const.YUUDENTAILOTNO, srYuudentaiFunsai)));           // 誘電体ｽﾗﾘｰLotNo
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B024Const.LOTKUBUN, srYuudentaiFunsai)));                 // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B024Const.GENRYOULOTNO, srYuudentaiFunsai)));        // 原料LotNo
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B024Const.GENRYOUKIGOU, srYuudentaiFunsai)));        // 原料記号
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B024Const.FUNSAIKI, srYuudentaiFunsai)));            // 粉砕機
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B024Const.FUNSAIKISENJYOU1, srYuudentaiFunsai)));    // 粉砕機洗浄①
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B024Const.FUNSAIKISENJYOU2, srYuudentaiFunsai)));    // 粉砕機洗浄②
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B024Const.RENZOKUUNTEN1, srYuudentaiFunsai)));       // 連続運転回数①
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B024Const.RENZOKUUNTEN2, srYuudentaiFunsai)));       // 連続運転回数②
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B024Const.GYOKUSEKIJYURYOU, srYuudentaiFunsai)));    // 玉石_重量
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B024Const.GYOKUSEKILOT, srYuudentaiFunsai)));        // 玉石_ﾛｯﾄ
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B024Const.GYOKUSEKIMEDIAKEI, srYuudentaiFunsai)));   // 玉石_ﾒﾃﾞｨｱ径
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B024Const.TOUNYUURYOU, srYuudentaiFunsai)));         // 投入量
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B024Const.ZIKANPASS, srYuudentaiFunsai)));           // 時間/回数
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B024Const.SCREEN, srYuudentaiFunsai)));              // ｽｸﾘｰﾝ
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B024Const.KAITENSUUDISP, srYuudentaiFunsai)));       // 回転数_ﾃﾞｨｽﾊﾟ
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B024Const.KAITENSUUSYUJIKU, srYuudentaiFunsai)));    // 回転数_主軸
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B024Const.POMPSYUTSURYOKU, srYuudentaiFunsai)));     // ﾎﾟﾝﾌﾟ出力
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B024Const.RYUURYOU, srYuudentaiFunsai)));            // 流量
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B024Const.PASSKAISUU, srYuudentaiFunsai)));          // 回数
        params.add(getkaishiAndTeishiDate("0000-00-00 00:00:00").get(0));                                                               // 開始日時
        params.add(getkaishiAndTeishiDate("0000-00-00 00:00:00").get(1));                                                               // 終了日時
        params.add("");                                                                                                                 // 備考1
        params.add("");                                                                                                                 // 備考2
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
     * 誘電体ｽﾗﾘｰ作製・粉砕(sr_yuudentai_funsai)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSrYuudentaiFunsai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sr_yuudentai_funsai "
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
     * [誘電体ｽﾗﾘｰ作製・粉砕_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @return 削除ﾌﾗｸﾞ最大値 + 1
     * @throws SQLException 例外エラー
     */
    private int getNewDeleteflag(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban) throws SQLException {
        String sql = "SELECT MAX(deleteflag) AS deleteflag "
                + "FROM tmp_sr_yuudentai_funsai "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ?  ";
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
     * 項目IDに該当するDBの値を取得する。
     *
     * @param itemId 項目ID
     * @param srYuudentaiFunsai 誘電体ｽﾗﾘｰ作製・粉砕データ
     * @return DB値
     */
    private String getSrYuudentaiFunsaiItemData(String itemId, SrYuudentaiFunsai srYuudentaiFunsai) {
        switch (itemId) {

            // 誘電体ｽﾗﾘｰ品名
            case GXHDO102B024Const.YUUDENTAIHINMEI:
                return StringUtil.nullToBlank(srYuudentaiFunsai.getYuudentaihinmei());

            // 誘電体ｽﾗﾘｰLotNo
            case GXHDO102B024Const.YUUDENTAILOTNO:
                return StringUtil.nullToBlank(srYuudentaiFunsai.getYuudentailotno());

            // ﾛｯﾄ区分
            case GXHDO102B024Const.LOTKUBUN:
                return StringUtil.nullToBlank(srYuudentaiFunsai.getLotkubun());

            // 原料LotNo
            case GXHDO102B024Const.GENRYOULOTNO:
                return StringUtil.nullToBlank(srYuudentaiFunsai.getGenryoulotno());

            // 原料記号
            case GXHDO102B024Const.GENRYOUKIGOU:
                return StringUtil.nullToBlank(srYuudentaiFunsai.getGenryoukigou());

            // 粉砕機
            case GXHDO102B024Const.FUNSAIKI:
                return StringUtil.nullToBlank(srYuudentaiFunsai.getFunsaiki());

            // 粉砕機洗浄①
            case GXHDO102B024Const.FUNSAIKISENJYOU1:
                return StringUtil.nullToBlank(srYuudentaiFunsai.getFunsaikisenjyou1());

            // 粉砕機洗浄②
            case GXHDO102B024Const.FUNSAIKISENJYOU2:
                return StringUtil.nullToBlank(srYuudentaiFunsai.getFunsaikisenjyou2());

            // 連続運転回数①
            case GXHDO102B024Const.RENZOKUUNTEN1:
                return StringUtil.nullToBlank(srYuudentaiFunsai.getRenzokuunten1());

            // 連続運転回数②
            case GXHDO102B024Const.RENZOKUUNTEN2:
                return StringUtil.nullToBlank(srYuudentaiFunsai.getRenzokuunten2());

            // 玉石_重量
            case GXHDO102B024Const.GYOKUSEKIJYURYOU:
                return StringUtil.nullToBlank(srYuudentaiFunsai.getGyokusekijyuryou());

            // 玉石_ﾛｯﾄ
            case GXHDO102B024Const.GYOKUSEKILOT:
                return StringUtil.nullToBlank(srYuudentaiFunsai.getGyokusekilot());

            // 玉石_ﾒﾃﾞｨｱ径
            case GXHDO102B024Const.GYOKUSEKIMEDIAKEI:
                return StringUtil.nullToBlank(srYuudentaiFunsai.getGyokusekimediakei());

            // 投入量
            case GXHDO102B024Const.TOUNYUURYOU:
                return StringUtil.nullToBlank(srYuudentaiFunsai.getTounyuuryou());

            // 時間/回数
            case GXHDO102B024Const.ZIKANPASS:
                return StringUtil.nullToBlank(srYuudentaiFunsai.getZikanpass());

            // ｽｸﾘｰﾝ
            case GXHDO102B024Const.SCREEN:
                return StringUtil.nullToBlank(srYuudentaiFunsai.getScreen());

            // 回転数_ﾃﾞｨｽﾊﾟ
            case GXHDO102B024Const.KAITENSUUDISP:
                return StringUtil.nullToBlank(srYuudentaiFunsai.getKaitensuudisp());

            // 回転数_主軸
            case GXHDO102B024Const.KAITENSUUSYUJIKU:
                return StringUtil.nullToBlank(srYuudentaiFunsai.getKaitensuusyujiku());

            // ﾎﾟﾝﾌﾟ出力
            case GXHDO102B024Const.POMPSYUTSURYOKU:
                return StringUtil.nullToBlank(srYuudentaiFunsai.getPompsyutsuryoku());

            // 流量
            case GXHDO102B024Const.RYUURYOU:
                return StringUtil.nullToBlank(srYuudentaiFunsai.getRyuuryou());

            // 回数
            case GXHDO102B024Const.PASSKAISUU:
                return StringUtil.nullToBlank(srYuudentaiFunsai.getPasskaisuu());

            default:
                return null;
        }
    }

    /**
     * 項目IDに該当するDBの値を取得する。
     *
     * @param itemId 項目ID
     * @param subSrYuudentaiFunsai 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞデータ
     * @return DB値
     */
    private String getSubSrYuudentaiFunsaiItemData(String itemId, SubSrYuudentaiFunsai subSrYuudentaiFunsai) {
        switch (itemId) {
            // 日付
            case GXHDO102B024Const.KAISHI_DAY:
                return DateUtil.formattedTimestamp(subSrYuudentaiFunsai.getKaishinichiji(), "yyMMdd");

            // 開始時刻
            case GXHDO102B024Const.KAISHI_TIME:
                return DateUtil.formattedTimestamp(subSrYuudentaiFunsai.getKaishinichiji(), "HHmm");

            // 停止予定時刻
            case GXHDO102B024Const.TEISHIYOTEI_TIME:
                return DateUtil.formattedTimestamp(subSrYuudentaiFunsai.getTeishiyoteinichiji(), "HHmm");

            // 停止時刻
            case GXHDO102B024Const.TEISHI_TIME:
                return DateUtil.formattedTimestamp(subSrYuudentaiFunsai.getTeishinichiji(), "HHmm");

            // 主軸電流（A）
            case GXHDO102B024Const.SYUJIKUDENRYUU:
                return StringUtil.nullToBlank(subSrYuudentaiFunsai.getSyujikudenryuu());

            // 出口温度（℃）
            case GXHDO102B024Const.DEGUCHIONDO:
                return StringUtil.nullToBlank(subSrYuudentaiFunsai.getDeguchiondo());

            // ｼｰﾙ温度（℃）
            case GXHDO102B024Const.SEALONDO:
                return StringUtil.nullToBlank(subSrYuudentaiFunsai.getSealondo());

            // ﾎﾟﾝﾌﾟ目盛（rpm）
            case GXHDO102B024Const.PUMPMEMORI:
                return StringUtil.nullToBlank(subSrYuudentaiFunsai.getPumpmemori());

            // ﾎﾟﾝﾌﾟ圧（Mpa）
            case GXHDO102B024Const.PUMPATSU:
                return StringUtil.nullToBlank(subSrYuudentaiFunsai.getPumpatsu());

            // D50（μm）
            case GXHDO102B024Const.D50:
                return StringUtil.nullToBlank(subSrYuudentaiFunsai.getD50());

            // BET（㎡/g・%）
            case GXHDO102B024Const.BET:
                return StringUtil.nullToBlank(subSrYuudentaiFunsai.getBet());

            // 流量規格
            case GXHDO102B024Const.RYUURYOUKIKAKU:
                String ryuuryoukikaku = StringUtil.nullToBlank(subSrYuudentaiFunsai.getRyuuryoukikaku());
                if (StringUtil.isEmpty(ryuuryoukikaku)) {
                    return "";
                }

                return "【" + StringUtil.nullToBlank(subSrYuudentaiFunsai.getRyuuryoukikaku()) + "】";

            // 流量 kg/min or  L
            case GXHDO102B024Const.SUB_RYUURYOU:
                return StringUtil.nullToBlank(subSrYuudentaiFunsai.getRyuuryou());

            // 開始ﾊﾟｽ
            case GXHDO102B024Const.KAISHIPASS:
                return StringUtil.nullToBlank(subSrYuudentaiFunsai.getKaishipass());

            // 停止ﾊﾟｽ
            case GXHDO102B024Const.TEISHIPASS:
                return StringUtil.nullToBlank(subSrYuudentaiFunsai.getTeishipass());

            // 備考1
            case GXHDO102B024Const.BIKOU1:
                return StringUtil.nullToBlank(subSrYuudentaiFunsai.getBikou1());

            // 備考2
            case GXHDO102B024Const.BIKOU2:
                return StringUtil.nullToBlank(subSrYuudentaiFunsai.getBikou2());

            default:
                return null;
        }
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・粉砕_仮登録(tmp_sr_yuudentai_funsai)登録処理(削除時)
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
    private void insertDeleteDataTmpSrYuudentaiFunsai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_yuudentai_funsai ("
                + " kojyo,lotno,edaban,yuudentaihinmei,yuudentailotno,lotkubun,genryoulotno,genryoukigou,funsaiki,funsaikisenjyou1,funsaikisenjyou2,"
                + "renzokuunten1,renzokuunten2,gyokusekijyuryou,gyokusekilot,gyokusekimediakei,tounyuuryou,zikanpass,screen,kaitensuudisp,"
                + "kaitensuusyujiku,pompsyutsuryoku,ryuuryou,passkaisuu,kaishinichiji,syuuryounichiji,bikou1,bikou2,"
                + "torokunichiji,kosinnichiji,revision,deleteflag "
                + ") SELECT "
                + " kojyo,lotno,edaban,yuudentaihinmei,yuudentailotno,lotkubun,genryoulotno,genryoukigou,funsaiki,funsaikisenjyou1,funsaikisenjyou2,"
                + "renzokuunten1,renzokuunten2,gyokusekijyuryou,gyokusekilot,gyokusekimediakei,tounyuuryou,zikanpass,screen,kaitensuudisp,"
                + "kaitensuusyujiku,pompsyutsuryoku,ryuuryou,passkaisuu,kaishinichiji,syuuryounichiji,bikou1,bikou2,?,?,?,? "
                + " FROM sr_yuudentai_funsai "
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
    public static Object getFormBean(String beanId) {
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
     * 誘電体ｽﾗﾘｰ作製・粉砕画面テーブル初期設定
     *
     * @param processData 処理制御データ
     */
    private void initGXHDO102B024A(ProcessData processData) {
        GXHDO102B024A bean = (GXHDO102B024A) getFormBean("gXHDO102B024A");
        bean.setDisplayStyle("block");
        bean.setWiplotno(getItemRow(processData.getItemList(), GXHDO102B024Const.WIPLOTNO));
        bean.setYuudentaihinmei(getItemRow(processData.getItemList(), GXHDO102B024Const.YUUDENTAIHINMEI));
        bean.setYuudentailotno(getItemRow(processData.getItemList(), GXHDO102B024Const.YUUDENTAILOTNO));
        bean.setLotkubun(getItemRow(processData.getItemList(), GXHDO102B024Const.LOTKUBUN));
        bean.setGenryoulotno(getItemRow(processData.getItemList(), GXHDO102B024Const.GENRYOULOTNO));
        bean.setGenryoukigou(getItemRow(processData.getItemList(), GXHDO102B024Const.GENRYOUKIGOU));
        bean.setFunsaiki(getItemRow(processData.getItemList(), GXHDO102B024Const.FUNSAIKI));
        bean.setFunsaikisenjyou1(getItemRow(processData.getItemList(), GXHDO102B024Const.FUNSAIKISENJYOU1));
        bean.setFunsaikisenjyou2(getItemRow(processData.getItemList(), GXHDO102B024Const.FUNSAIKISENJYOU2));
        bean.setRenzokuunten1(getItemRow(processData.getItemList(), GXHDO102B024Const.RENZOKUUNTEN1));
        bean.setRenzokuunten2(getItemRow(processData.getItemList(), GXHDO102B024Const.RENZOKUUNTEN2));
        bean.setGyokusekijyuryou(getItemRow(processData.getItemList(), GXHDO102B024Const.GYOKUSEKIJYURYOU));
        bean.setGyokusekilot(getItemRow(processData.getItemList(), GXHDO102B024Const.GYOKUSEKILOT));
        bean.setGyokusekimediakei(getItemRow(processData.getItemList(), GXHDO102B024Const.GYOKUSEKIMEDIAKEI));
        bean.setTounyuuryou(getItemRow(processData.getItemList(), GXHDO102B024Const.TOUNYUURYOU));
        bean.setZikanpass(getItemRow(processData.getItemList(), GXHDO102B024Const.ZIKANPASS));
        bean.setScreen(getItemRow(processData.getItemList(), GXHDO102B024Const.SCREEN));
        bean.setKaitensuudisp(getItemRow(processData.getItemList(), GXHDO102B024Const.KAITENSUUDISP));
        bean.setKaitensuusyujiku(getItemRow(processData.getItemList(), GXHDO102B024Const.KAITENSUUSYUJIKU));
        bean.setPompsyutsuryoku(getItemRow(processData.getItemList(), GXHDO102B024Const.POMPSYUTSURYOKU));
        bean.setRyuuryou(getItemRow(processData.getItemList(), GXHDO102B024Const.RYUURYOU));
        bean.setPasskaisuu(getItemRow(processData.getItemList(), GXHDO102B024Const.PASSKAISUU));
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞ画面テーブル初期設定
     *
     * @param processData 処理制御データ
     */
    private void initGXHDO102B024B(ProcessData processData) throws CloneNotSupportedException {
        GXHDO102B024B bean = (GXHDO102B024B) getFormBean("beanGXHDO102B024B");
        // 行削除チェックボックス	
        bean.setDeleterow_checkbox(new FXHDD01());
        // 日付	
        bean.setKaishi_day(getItemRow(processData.getItemListEx(), GXHDO102B024Const.KAISHI_DAY).clone());
        // 開始時刻	
        bean.setKaishi_time(getItemRow(processData.getItemListEx(), GXHDO102B024Const.KAISHI_TIME).clone());
        // 停止予定時刻	
        bean.setTeishiyotei_time(getItemRow(processData.getItemListEx(), GXHDO102B024Const.TEISHIYOTEI_TIME).clone());
        // 停止時刻	
        bean.setTeishi_time(getItemRow(processData.getItemListEx(), GXHDO102B024Const.TEISHI_TIME).clone());
        // 主軸電流（A）	
        bean.setSyujikudenryuu(getItemRow(processData.getItemListEx(), GXHDO102B024Const.SYUJIKUDENRYUU).clone());
        // 出口温度（℃）	
        bean.setDeguchiondo(getItemRow(processData.getItemListEx(), GXHDO102B024Const.DEGUCHIONDO).clone());
        // ｼｰﾙ温度（℃）	
        bean.setSealondo(getItemRow(processData.getItemListEx(), GXHDO102B024Const.SEALONDO).clone());
        // ﾎﾟﾝﾌﾟ目盛（rpm）	
        bean.setPumpmemori(getItemRow(processData.getItemListEx(), GXHDO102B024Const.PUMPMEMORI).clone());
        // ﾎﾟﾝﾌﾟ圧（Mpa）	
        bean.setPumpatsu(getItemRow(processData.getItemListEx(), GXHDO102B024Const.PUMPATSU).clone());
        // D50（μm）	
        bean.setD50(getItemRow(processData.getItemListEx(), GXHDO102B024Const.D50).clone());
        // BET（㎡/g・%）	
        bean.setBet(getItemRow(processData.getItemListEx(), GXHDO102B024Const.BET).clone());
        // 流量規格	
        bean.setRyuuryoukikaku(getItemRow(processData.getItemListEx(), GXHDO102B024Const.RYUURYOUKIKAKU).clone());
        // 流量 kg/min or  L	
        bean.setRyuuryou(getItemRow(processData.getItemListEx(), GXHDO102B024Const.SUB_RYUURYOU).clone());
        // 開始ﾊﾟｽ	
        bean.setKaishipass(getItemRow(processData.getItemListEx(), GXHDO102B024Const.KAISHIPASS).clone());
        // 停止ﾊﾟｽ	
        bean.setTeishipass(getItemRow(processData.getItemListEx(), GXHDO102B024Const.TEISHIPASS).clone());
        // 備考1	
        bean.setBikou1(getItemRow(processData.getItemListEx(), GXHDO102B024Const.BIKOU1).clone());
        // 備考2	
        bean.setBikou2(getItemRow(processData.getItemListEx(), GXHDO102B024Const.BIKOU2).clone());
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞ画面テーブルデータ初期設定
     *
     * @param processData 処理制御データ
     * @param subSrYuudentaiFunsaiList 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞデータ
     * @param ryuuryoukikakuListData 流量規格値取得ﾃﾞｰﾀ
     * @param passkikakuListData ﾊﾟｽ規格値取得ﾃﾞｰﾀ
     */
    private void initGXHDO102B024BData(ProcessData processData, List<SubSrYuudentaiFunsai> subSrYuudentaiFunsaiList, List<DaMkJoken> ryuuryoukikakuListData,
            List<DaMkJoken> passkikakuListData) throws CloneNotSupportedException {
        // 表示行数
        int hyoujirow = ryuuryoukikakuListData.size() > passkikakuListData.size() ? ryuuryoukikakuListData.size() : passkikakuListData.size();
        if (subSrYuudentaiFunsaiList != null && subSrYuudentaiFunsaiList.size() > hyoujirow) {
            hyoujirow = subSrYuudentaiFunsaiList.size();
        }
        List<GXHDO102B024Model> listdata = new ArrayList<>();
        for (int i = 1; i <= hyoujirow; i++) {
            listdata.add(createGXHDO102B024Model(processData, i, ryuuryoukikakuListData, passkikakuListData));
        }
        GXHDO102B024B bean = (GXHDO102B024B) getFormBean("beanGXHDO102B024B");
        bean.setListdata(listdata);
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞのモデルデータを作成する
     *
     * @param processData 処理制御データ
     * @param pass 回数
     * @param ryuuryoukikakuListData 流量規格値取得ﾃﾞｰﾀ
     * @param passkikakuListData ﾊﾟｽ規格値取得ﾃﾞｰﾀ
     */
    private GXHDO102B024Model createGXHDO102B024Model(ProcessData processData, Integer kaisuu, List<DaMkJoken> ryuuryoukikakuListData,
            List<DaMkJoken> passkikakuListData) throws CloneNotSupportedException {
        GXHDO102B024Model gxhdo102b024model = new GXHDO102B024Model();
        // 回数
        gxhdo102b024model.setKaisuu(kaisuu);
        // 行削除チェックボックス	
        gxhdo102b024model.setDeleterow_checkbox(new FXHDD01());
        // 日付	
        gxhdo102b024model.setKaishi_day(getItemRow(processData.getItemListEx(), GXHDO102B024Const.KAISHI_DAY).clone());
        // 開始時刻	
        gxhdo102b024model.setKaishi_time(getItemRow(processData.getItemListEx(), GXHDO102B024Const.KAISHI_TIME).clone());
        // 停止予定時刻	
        gxhdo102b024model.setTeishiyotei_time(getItemRow(processData.getItemListEx(), GXHDO102B024Const.TEISHIYOTEI_TIME).clone());
        // 停止時刻	
        gxhdo102b024model.setTeishi_time(getItemRow(processData.getItemListEx(), GXHDO102B024Const.TEISHI_TIME).clone());
        // 主軸電流（A）	
        gxhdo102b024model.setSyujikudenryuu(getItemRow(processData.getItemListEx(), GXHDO102B024Const.SYUJIKUDENRYUU).clone());
        // 出口温度（℃）	
        gxhdo102b024model.setDeguchiondo(getItemRow(processData.getItemListEx(), GXHDO102B024Const.DEGUCHIONDO).clone());
        // ｼｰﾙ温度（℃）	
        gxhdo102b024model.setSealondo(getItemRow(processData.getItemListEx(), GXHDO102B024Const.SEALONDO).clone());
        // ﾎﾟﾝﾌﾟ目盛（rpm）	
        gxhdo102b024model.setPumpmemori(getItemRow(processData.getItemListEx(), GXHDO102B024Const.PUMPMEMORI).clone());
        // ﾎﾟﾝﾌﾟ圧（Mpa）	
        gxhdo102b024model.setPumpatsu(getItemRow(processData.getItemListEx(), GXHDO102B024Const.PUMPATSU).clone());
        // D50（μm）	
        gxhdo102b024model.setD50(getItemRow(processData.getItemListEx(), GXHDO102B024Const.D50).clone());
        // BET（㎡/g・%）	
        gxhdo102b024model.setBet(getItemRow(processData.getItemListEx(), GXHDO102B024Const.BET).clone());
        // 流量規格
        gxhdo102b024model.setRyuuryoukikaku(getItemRow(processData.getItemListEx(), GXHDO102B024Const.RYUURYOUKIKAKU).clone());
        if (kaisuu <= ryuuryoukikakuListData.size()) {
            String kikakuti = StringUtil.nullToBlank(ryuuryoukikakuListData.get(kaisuu - 1).getKikakuti());
            if (!kikakuti.startsWith("【")) {
                kikakuti = "【" + kikakuti + "】";
            }
            gxhdo102b024model.getRyuuryoukikaku().setKikakuChi(kikakuti);
        } else {
            gxhdo102b024model.getRyuuryoukikaku().setKikakuChi("");
        }
        // 流量 kg/min or  L	
        gxhdo102b024model.setRyuuryou(getItemRow(processData.getItemListEx(), GXHDO102B024Const.SUB_RYUURYOU).clone());
        // 開始ﾊﾟｽ
        FXHDD01 kaishipassFXHDD01 = getItemRow(processData.getItemListEx(), GXHDO102B024Const.KAISHIPASS).clone();
        // 停止ﾊﾟｽ	
        FXHDD01 teishipassFXHDD01 = getItemRow(processData.getItemListEx(), GXHDO102B024Const.TEISHIPASS).clone();
        if (kaisuu <= passkikakuListData.size()) {
            String defaultStr = StringUtil.nullToBlank(passkikakuListData.get(kaisuu - 1).getKikakuti());
            String kaishipassDefaultStr = "";
            String teishipassDefaultStr = "";
            if (defaultStr.contains("～")) {
                kaishipassDefaultStr = defaultStr.substring(0, defaultStr.indexOf("～"));
                teishipassDefaultStr = defaultStr.substring(defaultStr.indexOf("～") + 1);
                BigDecimal kaishipassDefaultBigVal = ValidateUtil.numberExtraction(kaishipassDefaultStr);
                BigDecimal teishipassDefaultBigVal = ValidateUtil.numberExtraction(teishipassDefaultStr);
                kaishipassDefaultStr = kaishipassDefaultBigVal == null ? "" : kaishipassDefaultBigVal.toPlainString();
                teishipassDefaultStr = teishipassDefaultBigVal == null ? "" : teishipassDefaultBigVal.toPlainString();
            }

            kaishipassFXHDD01.setValue(kaishipassDefaultStr);
            teishipassFXHDD01.setValue(teishipassDefaultStr);
        }
        gxhdo102b024model.setKaishipass(kaishipassFXHDD01);
        gxhdo102b024model.setTeishipass(teishipassFXHDD01);

        //備考1
        gxhdo102b024model.setBikou1(getItemRow(processData.getItemListEx(), GXHDO102B024Const.BIKOU1).clone());
        //備考2
        gxhdo102b024model.setBikou2(getItemRow(processData.getItemListEx(), GXHDO102B024Const.BIKOU2).clone());
        return gxhdo102b024model;
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞ画面の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞ画面登録データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrYuudentaiFunsai> getSubSrYuudentaiFunsaiData(QueryRunner queryRunnerQcdb,
            String rev, String jotaiFlg, String kojyo, String lotNo, String edaban) throws SQLException {
        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSubSrYuudentaiFunsai(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSubSrYuudentaiFunsai(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        }
    }

    /**
     * [誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞ画面]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrYuudentaiFunsai> loadSubSrYuudentaiFunsai(QueryRunner queryRunnerQcdb,
            String kojyo, String lotNo, String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + " kojyo,lotno,edaban,kaisuu,kaishinichiji,teishiyoteinichiji,teishinichiji,syujikudenryuu,deguchiondo,"
                + "sealondo,pumpmemori,pumpatsu,d50kikaku,d50,betkikaku,bet,ryuuryoukikaku,ryuuryou,passkikaku,kaishipass,"
                + "teishipass,bikou1,bikou2,torokunichiji,kosinnichiji,revision, '0' AS deleteflag "
                + " FROM sub_sr_yuudentai_funsai "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? ";

        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            sql += "AND revision = ? ";
        }
        sql += " order by kaisuu ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);

        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            params.add(rev);
        }

        Map<String, String> mapping = new HashMap<>();
        mapping.put("kojyo", "kojyo");                             // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                             // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                           // 枝番
        mapping.put("kaisuu", "kaisuu");                           // 回数
        mapping.put("kaishinichiji", "kaishinichiji");             // 開始日時
        mapping.put("teishiyoteinichiji", "teishiyoteinichiji");   // 停止予定日時
        mapping.put("teishinichiji", "teishinichiji");             // 停止日時
        mapping.put("syujikudenryuu", "syujikudenryuu");           // 主軸電流
        mapping.put("deguchiondo", "deguchiondo");                 // 出口温度
        mapping.put("sealondo", "sealondo");                       // ｼｰﾙ温度
        mapping.put("pumpmemori", "pumpmemori");                   // ﾎﾟﾝﾌﾟ目盛
        mapping.put("pumpatsu", "pumpatsu");                       // ﾎﾟﾝﾌﾟ圧
        mapping.put("d50kikaku", "d50kikaku");                     // D50規格
        mapping.put("d50", "d50");                                 // D50
        mapping.put("betkikaku", "betkikaku");                     // BET規格
        mapping.put("bet", "bet");                                 // BET
        mapping.put("ryuuryoukikaku", "ryuuryoukikaku");           // 流量規格
        mapping.put("ryuuryou", "ryuuryou");                       // 流量 
        mapping.put("passkikaku", "passkikaku");                   // ﾊﾟｽ規格
        mapping.put("kaishipass", "kaishipass");                   // 開始ﾊﾟｽ
        mapping.put("teishipass", "teishipass");                   // 停止ﾊﾟｽ
        mapping.put("bikou1", "bikou1");                           // 備考1
        mapping.put("bikou2", "bikou2");                           // 備考2
        mapping.put("torokunichiji", "torokunichiji");             // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");               // 更新日時
        mapping.put("revision", "revision");                       // revision
        mapping.put("deleteflag", "deleteflag");                   // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SubSrYuudentaiFunsai>> beanHandler = new BeanListHandler<>(SubSrYuudentaiFunsai.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞ画面_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrYuudentaiFunsai> loadTmpSubSrYuudentaiFunsai(QueryRunner queryRunnerQcdb,
            String kojyo, String lotNo, String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + " kojyo,lotno,edaban,kaisuu,kaishinichiji,teishiyoteinichiji,teishinichiji,syujikudenryuu,deguchiondo,"
                + "sealondo,pumpmemori,pumpatsu,d50kikaku,d50,betkikaku,bet,ryuuryoukikaku,ryuuryou,passkikaku,kaishipass,"
                + "teishipass,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + " FROM tmp_sub_sr_yuudentai_funsai "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND deleteflag = ?  ";

        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            sql += "AND revision = ? ";
        }
        sql += " order by kaisuu ";

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
        mapping.put("kojyo", "kojyo");                             // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                             // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                           // 枝番
        mapping.put("kaisuu", "kaisuu");                           // 回数
        mapping.put("kaishinichiji", "kaishinichiji");             // 開始日時
        mapping.put("teishiyoteinichiji", "teishiyoteinichiji");   // 停止予定日時
        mapping.put("teishinichiji", "teishinichiji");             // 停止日時
        mapping.put("syujikudenryuu", "syujikudenryuu");           // 主軸電流
        mapping.put("deguchiondo", "deguchiondo");                 // 出口温度
        mapping.put("sealondo", "sealondo");                       // ｼｰﾙ温度
        mapping.put("pumpmemori", "pumpmemori");                   // ﾎﾟﾝﾌﾟ目盛
        mapping.put("pumpatsu", "pumpatsu");                       // ﾎﾟﾝﾌﾟ圧
        mapping.put("d50kikaku", "d50kikaku");                     // D50規格
        mapping.put("d50", "d50");                                 // D50
        mapping.put("betkikaku", "betkikaku");                     // BET規格
        mapping.put("bet", "bet");                                 // BET
        mapping.put("ryuuryoukikaku", "ryuuryoukikaku");           // 流量規格
        mapping.put("ryuuryou", "ryuuryou");                       // 流量 
        mapping.put("passkikaku", "passkikaku");                   // ﾊﾟｽ規格
        mapping.put("kaishipass", "kaishipass");                   // 開始ﾊﾟｽ
        mapping.put("teishipass", "teishipass");                   // 停止ﾊﾟｽ
        mapping.put("bikou1", "bikou1");                           // 備考1
        mapping.put("bikou2", "bikou2");                           // 備考2
        mapping.put("torokunichiji", "torokunichiji");             // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");               // 更新日時
        mapping.put("revision", "revision");                       // revision
        mapping.put("deleteflag", "deleteflag");                   // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SubSrYuudentaiFunsai>> beanHandler = new BeanListHandler<>(SubSrYuudentaiFunsai.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞ画面_仮登録(tmp_sub_sr_yuudentai_funsai)登録処理:更新前のデータを削除した後、画面一覧のデータを利用してInsertする
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void deleteAndInsertTmpSubSrYuudentaiFunsaiDataList(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            BigDecimal newRev, int deleteflag, String kojyo, String lotNo, String edaban, String systemTime) throws SQLException {
        // 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞ画面仮登録(tmp_sub_sr_yuudentai_funsai)削除処理
        deleteTmpSubSrYuudentaiFunsai(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo, edaban);
        // 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞ画面_仮登録(tmp_sub_sr_yuudentai_funsai)登録処理:画面一覧のデータを利用してInsertする
        insertTmpSubSrYuudentaiFunsaiDataList(queryRunnerQcdb, conQcdb, newRev, deleteflag, kojyo, lotNo, edaban, systemTime);
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞ画面_仮登録(tmp_sub_sr_yuudentai_funsai)登録処理:画面一覧のデータを利用してInsertする
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void insertTmpSubSrYuudentaiFunsaiDataList(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal newRev, int deleteflag, String kojyo, String lotNo, String edaban,
            String systemTime) throws SQLException {
        GXHDO102B024B bean = (GXHDO102B024B) getFormBean("beanGXHDO102B024B");
        List<GXHDO102B024Model> listdata = bean.getListdata();
        for (GXHDO102B024Model gxhdo102b024model : listdata) {
            insertTmpSubSrYuudentaiFunsai(queryRunnerQcdb, conQcdb, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, gxhdo102b024model);
        }
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞ画面_仮登録(tmp_sub_sr_yuudentai_funsai)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param gxhdo102b024model 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞデータ
     * @throws SQLException 例外エラー
     */
    private void insertTmpSubSrYuudentaiFunsai(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal newRev, int deleteflag, String kojyo, String lotNo, String edaban,
            String systemTime, GXHDO102B024Model gxhdo102b024model) throws SQLException {

        String sql = "INSERT INTO tmp_sub_sr_yuudentai_funsai ( "
                + " kojyo,lotno,edaban,kaisuu,kaishinichiji,teishiyoteinichiji,teishinichiji,syujikudenryuu,deguchiondo,"
                + "sealondo,pumpmemori,pumpatsu,d50kikaku,d50,betkikaku,bet,ryuuryoukikaku,ryuuryou,passkikaku,kaishipass,"
                + "teishipass,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + " ) VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";

        List<Object> params = setInsertParameterTmpSubSrYuudentaiFunsai(newRev, deleteflag, kojyo, lotNo, edaban, systemTime, gxhdo102b024model);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞ画面仮登録(tmp_sub_sr_yuudentai_funsai)更新値パラメータ設定
     *
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param gxhdo102b024model 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞデータ
     * @return 更新パラメータ
     */
    private List<Object> setInsertParameterTmpSubSrYuudentaiFunsai(BigDecimal newRev,
            int deleteflag, String kojyo, String lotNo, String edaban, String systemTime, GXHDO102B024Model gxhdo102b024model) {
        List<Object> params = new ArrayList<>();
        // 開始日
        String kaishi_day = StringUtil.nullToBlank(gxhdo102b024model.getKaishi_day().getValue());
        // 開始時間
        String kaishi_time = StringUtil.nullToBlank(gxhdo102b024model.getKaishi_time().getValue());
        // 停止予定時刻
        String teishiyotei_time = StringUtil.nullToBlank(gxhdo102b024model.getTeishiyotei_time().getValue());
        // 停止時刻
        String teishi_time = StringUtil.nullToBlank(gxhdo102b024model.getTeishi_time().getValue());

        params.add(kojyo); // 工場ｺｰﾄﾞ
        params.add(lotNo); // ﾛｯﾄNo
        params.add(edaban); // 枝番
        params.add(DBUtil.stringToIntObjectDefaultNull(gxhdo102b024model.getKaisuu().toString())); // 回数
        params.add(DBUtil.stringToDateObjectDefaultNull(kaishi_day, "".equals(kaishi_time) ? "0000" : kaishi_time)); // 開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(kaishi_day, "".equals(teishiyotei_time) ? "0000" : teishiyotei_time)); // 停止予定日時
        params.add(DBUtil.stringToDateObjectDefaultNull(kaishi_day, "".equals(teishi_time) ? "0000" : teishi_time)); // 停止日時
        params.add(DBUtil.stringToIntObjectDefaultNull(gxhdo102b024model.getSyujikudenryuu().getValue())); // 主軸電流
        params.add(DBUtil.stringToIntObjectDefaultNull(gxhdo102b024model.getDeguchiondo().getValue())); // 出口温度
        params.add(DBUtil.stringToIntObjectDefaultNull(gxhdo102b024model.getSealondo().getValue())); // ｼｰﾙ温度
        params.add(DBUtil.stringToIntObjectDefaultNull(gxhdo102b024model.getPumpmemori().getValue())); // ﾎﾟﾝﾌﾟ目盛
        params.add(DBUtil.stringToIntObjectDefaultNull(gxhdo102b024model.getPumpatsu().getValue())); // ﾎﾟﾝﾌﾟ圧
        params.add(DBUtil.stringToStringObjectDefaultNull(StringUtil.nullToBlank(gxhdo102b024model.getD50().getKikakuChi()).replace("【", "").replace("】", ""))); // D50規格
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(gxhdo102b024model.getD50().getValue())); // D50
        params.add(DBUtil.stringToStringObjectDefaultNull(StringUtil.nullToBlank(gxhdo102b024model.getBet().getKikakuChi()).replace("【", "").replace("】", ""))); // BET規格
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(gxhdo102b024model.getBet().getValue())); // BET
        params.add(DBUtil.stringToStringObjectDefaultNull(StringUtil.nullToBlank(gxhdo102b024model.getRyuuryoukikaku().getKikakuChi()).replace("【", "").replace("】", ""))); // 流量規格
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(gxhdo102b024model.getRyuuryou().getValue())); // 流量
        params.add(DBUtil.stringToStringObjectDefaultNull(StringUtil.nullToBlank(gxhdo102b024model.getTeishipass().getKikakuChi()).replace("【", "").replace("】", ""))); // ﾊﾟｽ規格
        params.add(DBUtil.stringToIntObjectDefaultNull(StringUtil.nullToBlank(gxhdo102b024model.getKaishipass().getValue()).replace("【", "").replace("】", ""))); // 開始ﾊﾟｽ
        params.add(DBUtil.stringToIntObjectDefaultNull(StringUtil.nullToBlank(gxhdo102b024model.getTeishipass().getValue()).replace("【", "").replace("】", ""))); // 停止ﾊﾟｽ
        params.add(DBUtil.stringToStringObjectDefaultNull(gxhdo102b024model.getBikou1().getValue())); // 備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(gxhdo102b024model.getBikou2().getValue())); // 備考2
        params.add(systemTime); //登録日時
        params.add(systemTime); //更新日時
        params.add(newRev); //revision
        params.add(deleteflag); //削除ﾌﾗｸﾞ

        return params;
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞ画面仮登録(tmp_sub_sr_yuudentai_funsai)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSubSrYuudentaiFunsai(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal rev, String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sub_sr_yuudentai_funsai "
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
     * 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞ画面(sub_sr_yuudentai_funsai)登録処理:更新前のデータを削除した後、画面一覧のデータを利用してInsertする
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void deleteAndInsertSubSrYuudentaiFunsaiDataList(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            BigDecimal newRev, String kojyo, String lotNo, String edaban, String systemTime) throws SQLException {
        // 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞ画面仮登録(sub_sr_yuudentai_funsai)削除処理
        deleteSubSrYuudentaiFunsai(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo, edaban);
        // 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞ画面(sub_sr_yuudentai_funsai)登録処理:画面一覧のデータを利用してInsertする
        insertSubSrYuudentaiFunsaiDataList(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo, edaban, systemTime);
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞ画面(sub_sr_yuudentai_funsai)登録処理:画面一覧のデータを利用してInsertする
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void insertSubSrYuudentaiFunsaiDataList(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal newRev, String kojyo, String lotNo, String edaban,
            String systemTime) throws SQLException {
        GXHDO102B024B bean = (GXHDO102B024B) getFormBean("beanGXHDO102B024B");
        List<GXHDO102B024Model> listdata = bean.getListdata();
        for (GXHDO102B024Model gxhdo102b024model : listdata) {
            insertSubSrYuudentaiFunsai(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo, edaban, systemTime, gxhdo102b024model);
        }
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞ画面(sub_sr_yuudentai_funsai)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param gxhdo102b024model 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞデータ
     * @throws SQLException 例外エラー
     */
    private void insertSubSrYuudentaiFunsai(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal newRev, String kojyo, String lotNo, String edaban,
            String systemTime, GXHDO102B024Model gxhdo102b024model) throws SQLException {
        String sql = "INSERT INTO sub_sr_yuudentai_funsai ( "
                + " kojyo,lotno,edaban,kaisuu,kaishinichiji,teishiyoteinichiji,teishinichiji,syujikudenryuu,deguchiondo,"
                + "sealondo,pumpmemori,pumpatsu,d50kikaku,d50,betkikaku,bet,ryuuryoukikaku,ryuuryou,passkikaku,kaishipass,"
                + "teishipass,bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + " ) VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";

        List<Object> params = setInsertParameterSubSrYuudentaiFunsai(newRev, kojyo, lotNo, edaban, systemTime, gxhdo102b024model);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞ画面登録(tmp_sub_sr_yuudentai_funsai)更新値パラメータ設定
     *
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param pass 回数
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param gxhdo102b024model 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞデータ
     * @return 更新パラメータ
     */
    private List<Object> setInsertParameterSubSrYuudentaiFunsai(BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, GXHDO102B024Model gxhdo102b024model) {
        List<Object> params = new ArrayList<>();
        // 開始日
        String kaishi_day = StringUtil.nullToBlank(gxhdo102b024model.getKaishi_day().getValue());
        // 開始時間
        String kaishi_time = StringUtil.nullToBlank(gxhdo102b024model.getKaishi_time().getValue());
        // 停止予定時刻
        String teishiyotei_time = StringUtil.nullToBlank(gxhdo102b024model.getTeishiyotei_time().getValue());
        // 停止時刻
        String teishi_time = StringUtil.nullToBlank(gxhdo102b024model.getTeishi_time().getValue());

        params.add(kojyo); // 工場ｺｰﾄﾞ
        params.add(lotNo); // ﾛｯﾄNo
        params.add(edaban); // 枝番
        params.add(DBUtil.stringToIntObject(gxhdo102b024model.getKaisuu().toString())); // 回数
        params.add(DBUtil.stringToDateObject(kaishi_day, "".equals(kaishi_time) ? "0000" : kaishi_time)); // 開始日時
        params.add(DBUtil.stringToDateObject(kaishi_day, "".equals(teishiyotei_time) ? "0000" : teishiyotei_time)); // 停止予定日時
        params.add(DBUtil.stringToDateObject(kaishi_day, "".equals(teishi_time) ? "0000" : teishi_time)); // 停止日時
        params.add(DBUtil.stringToIntObject(gxhdo102b024model.getSyujikudenryuu().getValue())); // 主軸電流
        params.add(DBUtil.stringToIntObject(gxhdo102b024model.getDeguchiondo().getValue())); // 出口温度
        params.add(DBUtil.stringToIntObject(gxhdo102b024model.getSealondo().getValue())); // ｼｰﾙ温度
        params.add(DBUtil.stringToIntObject(gxhdo102b024model.getPumpmemori().getValue())); // ﾎﾟﾝﾌﾟ目盛
        params.add(DBUtil.stringToIntObject(gxhdo102b024model.getPumpatsu().getValue())); // ﾎﾟﾝﾌﾟ圧
        params.add(DBUtil.stringToStringObject(StringUtil.nullToBlank(gxhdo102b024model.getD50().getKikakuChi()).replace("【", "").replace("】", ""))); // D50規格
        params.add(DBUtil.stringToBigDecimalObject(gxhdo102b024model.getD50().getValue())); // D50
        params.add(DBUtil.stringToStringObject(StringUtil.nullToBlank(gxhdo102b024model.getBet().getKikakuChi()).replace("【", "").replace("】", ""))); // BET規格
        params.add(DBUtil.stringToBigDecimalObject(gxhdo102b024model.getBet().getValue())); // BET
        params.add(DBUtil.stringToStringObject(StringUtil.nullToBlank(gxhdo102b024model.getRyuuryoukikaku().getKikakuChi()).replace("【", "").replace("】", ""))); // 流量規格
        params.add(DBUtil.stringToBigDecimalObject(gxhdo102b024model.getRyuuryou().getValue())); // 流量
        params.add(DBUtil.stringToStringObject(StringUtil.nullToBlank(gxhdo102b024model.getTeishipass().getKikakuChi()).replace("【", "").replace("】", ""))); // ﾊﾟｽ規格
        params.add(DBUtil.stringToIntObject(StringUtil.nullToBlank(gxhdo102b024model.getKaishipass().getValue()).replace("【", "").replace("】", ""))); // 開始ﾊﾟｽ
        params.add(DBUtil.stringToIntObject(StringUtil.nullToBlank(gxhdo102b024model.getTeishipass().getValue()).replace("【", "").replace("】", ""))); // 停止ﾊﾟｽ
        params.add(DBUtil.stringToStringObject(gxhdo102b024model.getBikou1().getValue())); // 備考1
        params.add(DBUtil.stringToStringObject(gxhdo102b024model.getBikou2().getValue())); // 備考2
        params.add(systemTime); //登録日時
        params.add(systemTime); //更新日時
        params.add(newRev); //revision
        return params;
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞ画面仮登録(tmp_sub_sr_yuudentai_funsai)登録処理(削除時)
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
    private void insertDeleteDataTmpSubSrYuudentaiFunsai(QueryRunner queryRunnerQcdb,
            Connection conQcdb, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, String systemTime) throws SQLException {
        String sql = "INSERT INTO tmp_sub_sr_yuudentai_funsai( "
                + " kojyo,lotno,edaban,kaisuu,kaishinichiji,teishiyoteinichiji,teishinichiji,syujikudenryuu,deguchiondo,"
                + "sealondo,pumpmemori,pumpatsu,d50kikaku,d50,betkikaku,bet,ryuuryoukikaku,ryuuryou,passkikaku,kaishipass,"
                + "teishipass,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") SELECT "
                + " kojyo,lotno,edaban,kaisuu,kaishinichiji,teishiyoteinichiji,teishinichiji,syujikudenryuu,deguchiondo,"
                + "sealondo,pumpmemori,pumpatsu,d50kikaku,d50,betkikaku,bet,ryuuryoukikaku,ryuuryou,passkikaku,kaishipass,"
                + "teishipass,bikou1,bikou2,?,?,?,? "
                + " FROM sub_sr_yuudentai_funsai "
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
     * 誘電体ｽﾗﾘｰ作製・粉砕ｻﾌﾞ画面仮登録(sub_sr_yuudentai_funsai)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSubSrYuudentaiFunsai(QueryRunner queryRunnerQcdb, Connection conQcdb,
            BigDecimal rev, String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sub_sr_yuudentai_funsai "
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
     * 画面項目存在チェック(ﾌｫｰﾑﾊﾟﾗﾒｰﾀに対象の項目が存在していることを確認)
     *
     * @param processData 処理制御データ
     * @return エラー項目名リスト
     */
    private List<String> checkExistFormItem(ProcessData processData) {
        List<String> errorItemNameList = new ArrayList<>();
        checkExistItem(errorItemNameList, processData.getItemList(), GXHDO102B024Const.WIPLOTNO, "WIPﾛｯﾄNo");
        checkExistItem(errorItemNameList, processData.getItemList(), GXHDO102B024Const.YUUDENTAIHINMEI, "誘電体ｽﾗﾘｰ品名");
        checkExistItem(errorItemNameList, processData.getItemList(), GXHDO102B024Const.YUUDENTAILOTNO, "誘電体ｽﾗﾘｰLotNo");
        checkExistItem(errorItemNameList, processData.getItemList(), GXHDO102B024Const.LOTKUBUN, "ﾛｯﾄ区分");
        checkExistItem(errorItemNameList, processData.getItemList(), GXHDO102B024Const.GENRYOULOTNO, "原料LotNo");
        checkExistItem(errorItemNameList, processData.getItemList(), GXHDO102B024Const.GENRYOUKIGOU, "原料記号");
        checkExistItem(errorItemNameList, processData.getItemList(), GXHDO102B024Const.FUNSAIKI, "粉砕機");
        checkExistItem(errorItemNameList, processData.getItemList(), GXHDO102B024Const.FUNSAIKISENJYOU1, "粉砕機洗浄①");
        checkExistItem(errorItemNameList, processData.getItemList(), GXHDO102B024Const.FUNSAIKISENJYOU2, "粉砕機洗浄②");
        checkExistItem(errorItemNameList, processData.getItemList(), GXHDO102B024Const.RENZOKUUNTEN1, "連続運転回数①");
        checkExistItem(errorItemNameList, processData.getItemList(), GXHDO102B024Const.RENZOKUUNTEN2, "連続運転回数②");
        checkExistItem(errorItemNameList, processData.getItemList(), GXHDO102B024Const.GYOKUSEKIJYURYOU, "玉石_重量");
        checkExistItem(errorItemNameList, processData.getItemList(), GXHDO102B024Const.GYOKUSEKILOT, "玉石_ﾛｯﾄ");
        checkExistItem(errorItemNameList, processData.getItemList(), GXHDO102B024Const.GYOKUSEKIMEDIAKEI, "玉石_ﾒﾃﾞｨｱ径");
        checkExistItem(errorItemNameList, processData.getItemList(), GXHDO102B024Const.TOUNYUURYOU, "投入量");
        checkExistItem(errorItemNameList, processData.getItemList(), GXHDO102B024Const.ZIKANPASS, "時間/ﾊﾟｽ回数");
        checkExistItem(errorItemNameList, processData.getItemList(), GXHDO102B024Const.SCREEN, "ｽｸﾘｰﾝ");
        checkExistItem(errorItemNameList, processData.getItemList(), GXHDO102B024Const.KAITENSUUDISP, "回転数_ﾃﾞｨｽﾊﾟ");
        checkExistItem(errorItemNameList, processData.getItemList(), GXHDO102B024Const.KAITENSUUSYUJIKU, "回転数_主軸");
        checkExistItem(errorItemNameList, processData.getItemList(), GXHDO102B024Const.POMPSYUTSURYOKU, "ﾎﾟﾝﾌﾟ出力");
        checkExistItem(errorItemNameList, processData.getItemList(), GXHDO102B024Const.RYUURYOU, "流量");
        checkExistItem(errorItemNameList, processData.getItemList(), GXHDO102B024Const.PASSKAISUU, "ﾊﾟｽ回数");

        checkExistItem(errorItemNameList, processData.getItemListEx(), GXHDO102B024Const.KAISHI_DAY, "日付");
        checkExistItem(errorItemNameList, processData.getItemListEx(), GXHDO102B024Const.KAISHI_TIME, "開始時刻");
        checkExistItem(errorItemNameList, processData.getItemListEx(), GXHDO102B024Const.TEISHIYOTEI_TIME, "停止予定時刻");
        checkExistItem(errorItemNameList, processData.getItemListEx(), GXHDO102B024Const.TEISHI_TIME, "停止時刻");
        checkExistItem(errorItemNameList, processData.getItemListEx(), GXHDO102B024Const.SYUJIKUDENRYUU, "主軸電流（A）");
        checkExistItem(errorItemNameList, processData.getItemListEx(), GXHDO102B024Const.DEGUCHIONDO, "出口温度（℃）");
        checkExistItem(errorItemNameList, processData.getItemListEx(), GXHDO102B024Const.SEALONDO, "ｼｰﾙ温度（℃）");
        checkExistItem(errorItemNameList, processData.getItemListEx(), GXHDO102B024Const.PUMPMEMORI, "ﾎﾟﾝﾌﾟ目盛（rpm）");
        checkExistItem(errorItemNameList, processData.getItemListEx(), GXHDO102B024Const.PUMPATSU, "ﾎﾟﾝﾌﾟ圧（Mpa）");
        checkExistItem(errorItemNameList, processData.getItemListEx(), GXHDO102B024Const.D50, "D50（μm）");
        checkExistItem(errorItemNameList, processData.getItemListEx(), GXHDO102B024Const.BET, "BET（㎡/g・%）");
        checkExistItem(errorItemNameList, processData.getItemListEx(), GXHDO102B024Const.RYUURYOUKIKAKU, "流量規格");
        checkExistItem(errorItemNameList, processData.getItemListEx(), GXHDO102B024Const.SUB_RYUURYOU, "流量 kg/min or  L");
        checkExistItem(errorItemNameList, processData.getItemListEx(), GXHDO102B024Const.KAISHIPASS, "開始ﾊﾟｽ");
        checkExistItem(errorItemNameList, processData.getItemListEx(), GXHDO102B024Const.TEISHIPASS, "停止ﾊﾟｽ");
        checkExistItem(errorItemNameList, processData.getItemListEx(), GXHDO102B024Const.BIKOU1, "備考1");
        checkExistItem(errorItemNameList, processData.getItemListEx(), GXHDO102B024Const.BIKOU2, "備考2");
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
     * 規格値(回数)取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト(Qcdb)
     * @param hinmei 品名
     * @param lotNo ﾛｯﾄNo
     * @param ryuuryoukikakuListData 流量規格値取得ﾃﾞｰﾀ
     * @param passkikakuListData ﾊﾟｽ規格値取得ﾃﾞｰﾀ
     * @throws SQLException 例外エラー
     */
    private ErrorMessageInfo getKaisuuValue(QueryRunner queryRunnerQcdb, String hinmei, String lotNo, List<DaMkJoken> ryuuryoukikakuListData, List<DaMkJoken> passkikakuListData) throws SQLException {
        String kojyo = lotNo.substring(0, 3);
        String lotNo9 = lotNo.substring(3, 12);
        String edaban = lotNo.substring(12, 15);
        String syurui = "誘電体ｽﾗﾘｰ作製";
        String kanrikoumokumeiRyuuryou = "流量%";
        String kanrikoumokumeiPass = "ﾊﾟｽ%";
        // [前工程設計]から、ﾃﾞｰﾀを取得
        Map daMkSekKeiData = loadDaMkSekKeiData(queryRunnerQcdb, kojyo, lotNo9, edaban, syurui);
        if (daMkSekKeiData == null || daMkSekKeiData.isEmpty()) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            return MessageUtil.getErrorMessageInfo("XHD-000014", true, false, null);
        }

        // 設計No
        String sekkeiNo = StringUtil.nullToBlank(getMapData(daMkSekKeiData, "sekkeiNo"));
        // ﾊﾟﾀｰﾝ
        String pattern = StringUtil.nullToBlank(getMapData(daMkSekKeiData, "pattern"));
        // (15)[前工程規格情報]から、ﾃﾞｰﾀを取得
        List<DaMkJoken> listRyuuryouData = loadDaMkJokenData(queryRunnerQcdb, sekkeiNo, syurui, kanrikoumokumeiRyuuryou);
        // (16)[前工程規格情報]から、ﾃﾞｰﾀを取得
        List<DaMkJoken> listPassData = loadDaMkJokenData(queryRunnerQcdb, sekkeiNo, syurui, kanrikoumokumeiPass);
        // Ⅲ.画面表示仕様(15)(16)はどちらも取得出来なかった場合
        if ((listRyuuryouData == null || listRyuuryouData.isEmpty())
                && (listPassData == null || listPassData.isEmpty())) {
            // (17)[前工程標準規格情報]から、ﾃﾞｰﾀを取得
            listRyuuryouData = loadDaMkhYoJunJokenData(queryRunnerQcdb, hinmei, pattern, syurui, kanrikoumokumeiRyuuryou);

            // (18)[前工程標準規格情報]から、ﾃﾞｰﾀを取得
            listPassData = loadDaMkhYoJunJokenData(queryRunnerQcdb, hinmei, pattern, syurui, kanrikoumokumeiPass);
        }

        if (listRyuuryouData != null) {
            for (DaMkJoken daMkJoken : listRyuuryouData) {
                ryuuryoukikakuListData.add(daMkJoken);
            }
        }
        if (listPassData != null) {
            for (DaMkJoken daMkJoken : listPassData) {
                passkikakuListData.add(daMkJoken);
            }
        }

        return null;
    }

    /**
     * [ﾊﾟﾗﾒｰﾀﾏｽﾀ]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerDoc オブジェクト
     * @param key キー
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    public static Map loadFxhbm03Data(QueryRunner queryRunnerDoc, String key) throws SQLException {
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
    public static Map loadDaMkSekKeiData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban, String syurui) throws SQLException {
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
     * 【停止予定時刻】自動入力処理用[前工程規格情報]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb オブジェクト
     * @param sekkeiNo 設計No
     * @param data ﾊﾟﾗﾒｰﾀﾃﾞｰﾀ
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    public static Map loadJidounyuuryoukuDaMkJokenData(QueryRunner queryRunnerQcdb, String sekkeiNo, String[] data) throws SQLException {
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
     * [前工程規格情報]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb オブジェクト
     * @param sekkeiNo 設計No
     * @param koumokumei 工程名
     * @param kanrikoumokumei 管理項目名
     * @return 前工程規格情報データ
     * @throws SQLException 例外エラー
     */
    private List<DaMkJoken> loadDaMkJokenData(QueryRunner queryRunnerQcdb, String sekkeiNo, String koumokumei, String kanrikoumokumei) throws SQLException {
        // 前工程規格情報データの取得
        String sql = "SELECT kikakuti kikakuti FROM da_mkjoken"
                + " WHERE sekkeino = ? AND kouteimei = ? AND koumokumei = ? AND kanrikoumokumei like ? "
                + " order by  kanrikoumokumei";
        List<Object> params = new ArrayList<>();
        params.add(sekkeiNo);
        params.add(koumokumei);
        params.add("粉砕");
        params.add(kanrikoumokumei);

        Map<String, String> mapping = new HashMap<>();
        mapping.put("kikakuti", "kikakuti");

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<DaMkJoken>> beanHandler = new BeanListHandler<>(DaMkJoken.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * 【停止予定時刻】自動入力処理用[前工程標準規格情報]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb オブジェクト
     * @param hinmei 品名
     * @param pattern ﾊﾟﾀｰﾝ
     * @param syurui 種類
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    public static Map loadJidounyuuryoukuDaMkhYoJunJokenData(QueryRunner queryRunnerQcdb, String hinmei, String pattern, String syurui) throws SQLException {
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
     * [前工程標準規格情報]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb オブジェクト
     * @param hinmei 品名
     * @param pattern ﾊﾟﾀｰﾝ
     * @param koumokumei 工程名
     * @param kanrikoumokumei 管理項目名
     * @return 前工程標準規格情報データ
     * @throws SQLException 例外エラー
     */
    private List<DaMkJoken> loadDaMkhYoJunJokenData(QueryRunner queryRunnerQcdb, String hinmei, String pattern, String koumokumei,
            String kanrikoumokumei) throws SQLException {
        // 前工程標準規格情報データの取得
        String sql = "SELECT kikakuti FROM da_mkhyojunjoken"
                + " WHERE hinmei = ? AND pattern = ? AND syurui = ?  AND kouteimei = ? AND koumokumei = ? AND kanrikoumokumei like ?"
                + " order by  kanrikoumokumei";
        List<Object> params = new ArrayList<>();
        params.add(hinmei);
        params.add(pattern);
        params.add(koumokumei);
        params.add(koumokumei);
        params.add("粉砕");
        params.add(kanrikoumokumei);

        Map<String, String> mapping = new HashMap<>();
        mapping.put("kikakuti", "kikakuti");

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<DaMkJoken>> beanHandler = new BeanListHandler<>(DaMkJoken.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }
}
