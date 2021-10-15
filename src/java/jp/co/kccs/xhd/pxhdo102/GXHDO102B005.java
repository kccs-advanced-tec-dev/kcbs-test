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
import jp.co.kccs.xhd.db.model.SrGlassslurryhyoryo;
import jp.co.kccs.xhd.db.model.SubSrGlassslurryhyoryo;
import jp.co.kccs.xhd.model.GXHDO102C002Model;
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
 * 変更日	2021/09/08<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B005(ｶﾞﾗｽｽﾗﾘｰ作製・秤量)
 *
 * @author KCSS K.Jo
 * @since 2021/09/08
 */
public class GXHDO102B005 implements IFormLogic {

    private static final Logger LOGGER = Logger.getLogger(GXHDO102B005.class.getName());
    private static final String JOTAI_FLG_KARI_TOROKU = "0";
    private static final String JOTAI_FLG_TOROKUZUMI = "1";
    private static final String JOTAI_FLG_SAKUJO = "9";
    private static final String SQL_STATE_RECORD_LOCK_ERR = "55P03";

    /**
     * コンストラクタ
     */
    public GXHDO102B005() {
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

            // 「ﾎﾟｯﾄ1」初期設定
            initGXHDO102B005B(processData);
            // 「ﾎﾟｯﾄ2」初期設定
            initGXHDO102B005C(processData);
            // 「ﾎﾟｯﾄ3」初期設定
            initGXHDO102B005D(processData);
            // 「ﾎﾟｯﾄ4」初期設定
            initGXHDO102B005E(processData);
            // 「ﾎﾟｯﾄ5」初期設定
            initGXHDO102B005F(processData);
            // 「ﾎﾟｯﾄ6」初期設定
            initGXHDO102B005G(processData);
            // 「ﾎﾟｯﾄ7」初期設定
            initGXHDO102B005H(processData);
            // 「ﾎﾟｯﾄ8」初期設定
            initGXHDO102B005I(processData);

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
                    GXHDO102B005Const.BTN_EDABAN_COPY_TOP,
                    GXHDO102B005Const.BTN_HYOURYOUNICHIJI_TOP,
                    GXHDO102B005Const.BTN_EDABAN_COPY_BOTTOM,
                    GXHDO102B005Const.BTN_HYOURYOUNICHIJI_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO102B005Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO102B005Const.BTN_INSERT_TOP,
                    GXHDO102B005Const.BTN_DELETE_TOP,
                    GXHDO102B005Const.BTN_UPDATE_TOP,
                    GXHDO102B005Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO102B005Const.BTN_INSERT_BOTTOM,
                    GXHDO102B005Const.BTN_DELETE_BOTTOM,
                    GXHDO102B005Const.BTN_UPDATE_BOTTOM
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
            case GXHDO102B005Const.BTN_EDABAN_COPY_TOP:
            case GXHDO102B005Const.BTN_EDABAN_COPY_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 仮登録
            case GXHDO102B005Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO102B005Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO102B005Const.BTN_INSERT_TOP:
            case GXHDO102B005Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 修正
            case GXHDO102B005Const.BTN_UPDATE_TOP:
            case GXHDO102B005Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO102B005Const.BTN_DELETE_TOP:
            case GXHDO102B005Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 秤量日時
            case GXHDO102B005Const.BTN_HYOURYOUNICHIJI_TOP:
            case GXHDO102B005Const.BTN_HYOURYOUNICHIJI_BOTTOM:
                method = "setHyouryounichijiDateTime";
                break;
            // ﾎﾟｯﾄ1タブの【材料品名1】ﾘﾝｸ押下時、サブ画面Open用非表示ボタン
            case GXHDO102B005Const.BTN_OPENC002POTTO1SUBGAMEN1:
                method = "openC002potto1subgamen1";
                break;
            // ﾎﾟｯﾄ1タブの【材料品名2】ﾘﾝｸ押下時、サブ画面Open用非表示ボタン
            case GXHDO102B005Const.BTN_OPENC002POTTO1SUBGAMEN2:
                method = "openC002potto1subgamen2";
                break;
            // ﾎﾟｯﾄ1タブの【材料品名3】ﾘﾝｸ押下時、サブ画面Open用非表示ボタン
            case GXHDO102B005Const.BTN_OPENC002POTTO1SUBGAMEN3:
                method = "openC002potto1subgamen3";
                break;
            // ﾎﾟｯﾄ1タブの【材料品名4】ﾘﾝｸ押下時、サブ画面Open用非表示ボタン
            case GXHDO102B005Const.BTN_OPENC002POTTO1SUBGAMEN4:
                method = "openC002potto1subgamen4";
                break;
            // ﾎﾟｯﾄ2タブの【材料品名1】ﾘﾝｸ押下時、サブ画面Open用非表示ボタン
            case GXHDO102B005Const.BTN_OPENC002POTTO2SUBGAMEN1:
                method = "openC002potto2subgamen1";
                break;
            // ﾎﾟｯﾄ2タブの【材料品名2】ﾘﾝｸ押下時、サブ画面Open用非表示ボタン
            case GXHDO102B005Const.BTN_OPENC002POTTO2SUBGAMEN2:
                method = "openC002potto2subgamen2";
                break;
            // ﾎﾟｯﾄ2タブの【材料品名3】ﾘﾝｸ押下時、サブ画面Open用非表示ボタン
            case GXHDO102B005Const.BTN_OPENC002POTTO2SUBGAMEN3:
                method = "openC002potto2subgamen3";
                break;
            // ﾎﾟｯﾄ2タブの【材料品名4】ﾘﾝｸ押下時、サブ画面Open用非表示ボタン
            case GXHDO102B005Const.BTN_OPENC002POTTO2SUBGAMEN4:
                method = "openC002potto2subgamen4";
                break;
            // ﾎﾟｯﾄ3タブの【材料品名1】ﾘﾝｸ押下時、サブ画面Open用非表示ボタン
            case GXHDO102B005Const.BTN_OPENC002POTTO3SUBGAMEN1:
                method = "openC002potto3subgamen1";
                break;
            // ﾎﾟｯﾄ3タブの【材料品名2】ﾘﾝｸ押下時、サブ画面Open用非表示ボタン
            case GXHDO102B005Const.BTN_OPENC002POTTO3SUBGAMEN2:
                method = "openC002potto3subgamen2";
                break;
            // ﾎﾟｯﾄ3タブの【材料品名3】ﾘﾝｸ押下時、サブ画面Open用非表示ボタン
            case GXHDO102B005Const.BTN_OPENC002POTTO3SUBGAMEN3:
                method = "openC002potto3subgamen3";
                break;
            // ﾎﾟｯﾄ3タブの【材料品名4】ﾘﾝｸ押下時、サブ画面Open用非表示ボタン
            case GXHDO102B005Const.BTN_OPENC002POTTO3SUBGAMEN4:
                method = "openC002potto3subgamen4";
                break;
            // ﾎﾟｯﾄ4タブの【材料品名1】ﾘﾝｸ押下時、サブ画面Open用非表示ボタン
            case GXHDO102B005Const.BTN_OPENC002POTTO4SUBGAMEN1:
                method = "openC002potto4subgamen1";
                break;
            // ﾎﾟｯﾄ4タブの【材料品名2】ﾘﾝｸ押下時、サブ画面Open用非表示ボタン
            case GXHDO102B005Const.BTN_OPENC002POTTO4SUBGAMEN2:
                method = "openC002potto4subgamen2";
                break;
            // ﾎﾟｯﾄ4タブの【材料品名3】ﾘﾝｸ押下時、サブ画面Open用非表示ボタン
            case GXHDO102B005Const.BTN_OPENC002POTTO4SUBGAMEN3:
                method = "openC002potto4subgamen3";
                break;
            // ﾎﾟｯﾄ4タブの【材料品名4】ﾘﾝｸ押下時、サブ画面Open用非表示ボタン
            case GXHDO102B005Const.BTN_OPENC002POTTO4SUBGAMEN4:
                method = "openC002potto4subgamen4";
                break;
            // ﾎﾟｯﾄ5タブの【材料品名1】ﾘﾝｸ押下時、サブ画面Open用非表示ボタン
            case GXHDO102B005Const.BTN_OPENC002POTTO5SUBGAMEN1:
                method = "openC002potto5subgamen1";
                break;
            // ﾎﾟｯﾄ5タブの【材料品名2】ﾘﾝｸ押下時、サブ画面Open用非表示ボタン
            case GXHDO102B005Const.BTN_OPENC002POTTO5SUBGAMEN2:
                method = "openC002potto5subgamen2";
                break;
            // ﾎﾟｯﾄ5タブの【材料品名3】ﾘﾝｸ押下時、サブ画面Open用非表示ボタン
            case GXHDO102B005Const.BTN_OPENC002POTTO5SUBGAMEN3:
                method = "openC002potto5subgamen3";
                break;
            // ﾎﾟｯﾄ5タブの【材料品名4】ﾘﾝｸ押下時、サブ画面Open用非表示ボタン
            case GXHDO102B005Const.BTN_OPENC002POTTO5SUBGAMEN4:
                method = "openC002potto5subgamen4";
                break;
            // ﾎﾟｯﾄ6タブの【材料品名1】ﾘﾝｸ押下時、サブ画面Open用非表示ボタン
            case GXHDO102B005Const.BTN_OPENC002POTTO6SUBGAMEN1:
                method = "openC002potto6subgamen1";
                break;
            // ﾎﾟｯﾄ6タブの【材料品名2】ﾘﾝｸ押下時、サブ画面Open用非表示ボタン
            case GXHDO102B005Const.BTN_OPENC002POTTO6SUBGAMEN2:
                method = "openC002potto6subgamen2";
                break;
            // ﾎﾟｯﾄ6タブの【材料品名3】ﾘﾝｸ押下時、サブ画面Open用非表示ボタン
            case GXHDO102B005Const.BTN_OPENC002POTTO6SUBGAMEN3:
                method = "openC002potto6subgamen3";
                break;
            // ﾎﾟｯﾄ6タブの【材料品名4】ﾘﾝｸ押下時、サブ画面Open用非表示ボタン
            case GXHDO102B005Const.BTN_OPENC002POTTO6SUBGAMEN4:
                method = "openC002potto6subgamen4";
                break;
            // ﾎﾟｯﾄ7タブの【材料品名1】ﾘﾝｸ押下時、サブ画面Open用非表示ボタン
            case GXHDO102B005Const.BTN_OPENC002POTTO7SUBGAMEN1:
                method = "openC002potto7subgamen1";
                break;
            // ﾎﾟｯﾄ7タブの【材料品名2】ﾘﾝｸ押下時、サブ画面Open用非表示ボタン
            case GXHDO102B005Const.BTN_OPENC002POTTO7SUBGAMEN2:
                method = "openC002potto7subgamen2";
                break;
            // ﾎﾟｯﾄ7タブの【材料品名3】ﾘﾝｸ押下時、サブ画面Open用非表示ボタン
            case GXHDO102B005Const.BTN_OPENC002POTTO7SUBGAMEN3:
                method = "openC002potto7subgamen3";
                break;
            // ﾎﾟｯﾄ7タブの【材料品名4】ﾘﾝｸ押下時、サブ画面Open用非表示ボタン
            case GXHDO102B005Const.BTN_OPENC002POTTO7SUBGAMEN4:
                method = "openC002potto7subgamen4";
                break;
            // ﾎﾟｯﾄ8タブの【材料品名1】ﾘﾝｸ押下時、サブ画面Open用非表示ボタン
            case GXHDO102B005Const.BTN_OPENC002POTTO8SUBGAMEN1:
                method = "openC002potto8subgamen1";
                break;
            // ﾎﾟｯﾄ8タブの【材料品名2】ﾘﾝｸ押下時、サブ画面Open用非表示ボタン
            case GXHDO102B005Const.BTN_OPENC002POTTO8SUBGAMEN2:
                method = "openC002potto8subgamen2";
                break;
            // ﾎﾟｯﾄ8タブの【材料品名3】ﾘﾝｸ押下時、サブ画面Open用非表示ボタン
            case GXHDO102B005Const.BTN_OPENC002POTTO8SUBGAMEN3:
                method = "openC002potto8subgamen3";
                break;
            // ﾎﾟｯﾄ8タブの【材料品名4】ﾘﾝｸ押下時、サブ画面Open用非表示ボタン
            case GXHDO102B005Const.BTN_OPENC002POTTO8SUBGAMEN4:
                method = "openC002potto8subgamen4";
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

            // ｶﾞﾗｽｽﾗﾘｰ作製・秤量の入力項目の登録データ(仮登録時は仮登録データ)を取得
            List<SrGlassslurryhyoryo> srGlassslurryhyoDataList = getSrGlassslurryhyoryoData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo9, oyalotEdaban);
            if (srGlassslurryhyoDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }
            // ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力_ｻﾌﾞ画面データ取得
            List<SubSrGlassslurryhyoryo> subSrGlassslurryhyoryoDataList = getSubSrGlassslurryhyoryoData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo9, oyalotEdaban);
            if (subSrGlassslurryhyoryoDataList.isEmpty() || subSrGlassslurryhyoryoDataList.size() != 32) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }
            // メイン画面データ設定
            setInputItemDataMainForm(processData, srGlassslurryhyoDataList.get(0));
            // ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力_ｻﾌﾞ画面データ設定
            setInputItemDataSubFormC002(processData, subSrGlassslurryhyoryoDataList);

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
        FXHDD01 kikakuFxhdd01 = getItemRow(processData.getItemListEx(), kikakuItem);
        if (kikakuFxhdd01 == null) {
            return null;
        }
        int tyougouryouTotalVal = 0;
        FXHDD01 itemFxhdd01Clone = null;
        try {
            for (String tyougouryouItem : tyougouryouItemList) {
                FXHDD01 itemFxhdd01 = getItemRow(processData.getItemListEx(), tyougouryouItem);
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
     * @param potto_tyougouryouList 調合量リスト
     * @param potto_tyogouryoukikaku 調合量規格
     * @param label1 表示ﾗﾍﾞﾙ1
     */
    private void setKikakuValueAndLabel1(ProcessData processData, List<FXHDD01> itemList, List<String> potto_tyougouryouList, String potto_tyogouryoukikaku, String label1) {
        // 調合量規格Xの規格は調合量X_1と調合量X_2に設置
        FXHDD01 potto_tyougouryouKikakuItemFxhdd01 = setKikakuValue(processData, potto_tyogouryoukikaku, potto_tyougouryouList);
        // 項目の項目名を設置
        if(potto_tyougouryouKikakuItemFxhdd01 != null){
            potto_tyougouryouKikakuItemFxhdd01.setLabel1(label1);
            itemList.add(potto_tyougouryouKikakuItemFxhdd01);
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
        List<String> potto_tyougouryouList = new ArrayList<>();
        List<String> potto_tyogouryoukikaku = Arrays.asList(GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU1, GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU2,
                GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU3, GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU4, GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU1,
                GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU2, GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU3, GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU4,
                GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU1, GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU2, GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU3,
                GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU4, GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU1, GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU2,
                GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU3, GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU4, GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU1,
                GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU2, GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU3, GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU4,
                GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU1, GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU2, GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU3,
                GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU4, GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU1, GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU2,
                GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU3, GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU4, GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU1,
                GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU2, GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU3, GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU4
        );
        List<String> potto1_tyougouryou1List = Arrays.asList(GXHDO102B005Const.POTTO1_TYOUGOURYOU1_1, GXHDO102B005Const.POTTO1_TYOUGOURYOU1_2); // ﾎﾟｯﾄ1_調合量1リスト
        List<String> potto1_tyougouryou2List = Arrays.asList(GXHDO102B005Const.POTTO1_TYOUGOURYOU2_1, GXHDO102B005Const.POTTO1_TYOUGOURYOU2_2); // ﾎﾟｯﾄ1_調合量2リスト
        List<String> potto1_tyougouryou3List = Arrays.asList(GXHDO102B005Const.POTTO1_TYOUGOURYOU3_1, GXHDO102B005Const.POTTO1_TYOUGOURYOU3_2); // ﾎﾟｯﾄ1_調合量3リスト
        List<String> potto1_tyougouryou4List = Arrays.asList(GXHDO102B005Const.POTTO1_TYOUGOURYOU4_1, GXHDO102B005Const.POTTO1_TYOUGOURYOU4_2); // ﾎﾟｯﾄ1_調合量4リスト
        
        List<String> potto2_tyougouryou1List = Arrays.asList(GXHDO102B005Const.POTTO2_TYOUGOURYOU1_1, GXHDO102B005Const.POTTO2_TYOUGOURYOU1_2); // ﾎﾟｯﾄ2_調合量1リスト
        List<String> potto2_tyougouryou2List = Arrays.asList(GXHDO102B005Const.POTTO2_TYOUGOURYOU2_1, GXHDO102B005Const.POTTO2_TYOUGOURYOU2_2); // ﾎﾟｯﾄ2_調合量2リスト
        List<String> potto2_tyougouryou3List = Arrays.asList(GXHDO102B005Const.POTTO2_TYOUGOURYOU3_1, GXHDO102B005Const.POTTO2_TYOUGOURYOU3_2); // ﾎﾟｯﾄ2_調合量3リスト
        List<String> potto2_tyougouryou4List = Arrays.asList(GXHDO102B005Const.POTTO2_TYOUGOURYOU4_1, GXHDO102B005Const.POTTO2_TYOUGOURYOU4_2); // ﾎﾟｯﾄ2_調合量4リスト
        
        List<String> potto3_tyougouryou1List = Arrays.asList(GXHDO102B005Const.POTTO3_TYOUGOURYOU1_1, GXHDO102B005Const.POTTO3_TYOUGOURYOU1_2); // ﾎﾟｯﾄ3_調合量1リスト
        List<String> potto3_tyougouryou2List = Arrays.asList(GXHDO102B005Const.POTTO3_TYOUGOURYOU2_1, GXHDO102B005Const.POTTO3_TYOUGOURYOU2_2); // ﾎﾟｯﾄ3_調合量2リスト
        List<String> potto3_tyougouryou3List = Arrays.asList(GXHDO102B005Const.POTTO3_TYOUGOURYOU3_1, GXHDO102B005Const.POTTO3_TYOUGOURYOU3_2); // ﾎﾟｯﾄ3_調合量3リスト
        List<String> potto3_tyougouryou4List = Arrays.asList(GXHDO102B005Const.POTTO3_TYOUGOURYOU4_1, GXHDO102B005Const.POTTO3_TYOUGOURYOU4_2); // ﾎﾟｯﾄ3_調合量4リスト
        
        List<String> potto4_tyougouryou1List = Arrays.asList(GXHDO102B005Const.POTTO4_TYOUGOURYOU1_1, GXHDO102B005Const.POTTO4_TYOUGOURYOU1_2); // ﾎﾟｯﾄ4_調合量1リスト
        List<String> potto4_tyougouryou2List = Arrays.asList(GXHDO102B005Const.POTTO4_TYOUGOURYOU2_1, GXHDO102B005Const.POTTO4_TYOUGOURYOU2_2); // ﾎﾟｯﾄ4_調合量2リスト
        List<String> potto4_tyougouryou3List = Arrays.asList(GXHDO102B005Const.POTTO4_TYOUGOURYOU3_1, GXHDO102B005Const.POTTO4_TYOUGOURYOU3_2); // ﾎﾟｯﾄ4_調合量3リスト
        List<String> potto4_tyougouryou4List = Arrays.asList(GXHDO102B005Const.POTTO4_TYOUGOURYOU4_1, GXHDO102B005Const.POTTO4_TYOUGOURYOU4_2); // ﾎﾟｯﾄ4_調合量4リスト
        
        List<String> potto5_tyougouryou1List = Arrays.asList(GXHDO102B005Const.POTTO5_TYOUGOURYOU1_1, GXHDO102B005Const.POTTO5_TYOUGOURYOU1_2); // ﾎﾟｯﾄ5_調合量1リスト
        List<String> potto5_tyougouryou2List = Arrays.asList(GXHDO102B005Const.POTTO5_TYOUGOURYOU2_1, GXHDO102B005Const.POTTO5_TYOUGOURYOU2_2); // ﾎﾟｯﾄ5_調合量2リスト
        List<String> potto5_tyougouryou3List = Arrays.asList(GXHDO102B005Const.POTTO5_TYOUGOURYOU3_1, GXHDO102B005Const.POTTO5_TYOUGOURYOU3_2); // ﾎﾟｯﾄ5_調合量3リスト
        List<String> potto5_tyougouryou4List = Arrays.asList(GXHDO102B005Const.POTTO5_TYOUGOURYOU4_1, GXHDO102B005Const.POTTO5_TYOUGOURYOU4_2); // ﾎﾟｯﾄ5_調合量4リスト
        
        List<String> potto6_tyougouryou1List = Arrays.asList(GXHDO102B005Const.POTTO6_TYOUGOURYOU1_1, GXHDO102B005Const.POTTO6_TYOUGOURYOU1_2); // ﾎﾟｯﾄ6_調合量1リスト
        List<String> potto6_tyougouryou2List = Arrays.asList(GXHDO102B005Const.POTTO6_TYOUGOURYOU2_1, GXHDO102B005Const.POTTO6_TYOUGOURYOU2_2); // ﾎﾟｯﾄ6_調合量2リスト
        List<String> potto6_tyougouryou3List = Arrays.asList(GXHDO102B005Const.POTTO6_TYOUGOURYOU3_1, GXHDO102B005Const.POTTO6_TYOUGOURYOU3_2); // ﾎﾟｯﾄ6_調合量3リスト
        List<String> potto6_tyougouryou4List = Arrays.asList(GXHDO102B005Const.POTTO6_TYOUGOURYOU4_1, GXHDO102B005Const.POTTO6_TYOUGOURYOU4_2); // ﾎﾟｯﾄ6_調合量4リスト
        
        List<String> potto7_tyougouryou1List = Arrays.asList(GXHDO102B005Const.POTTO7_TYOUGOURYOU1_1, GXHDO102B005Const.POTTO7_TYOUGOURYOU1_2); // ﾎﾟｯﾄ7_調合量1リスト
        List<String> potto7_tyougouryou2List = Arrays.asList(GXHDO102B005Const.POTTO7_TYOUGOURYOU2_1, GXHDO102B005Const.POTTO7_TYOUGOURYOU2_2); // ﾎﾟｯﾄ7_調合量2リスト
        List<String> potto7_tyougouryou3List = Arrays.asList(GXHDO102B005Const.POTTO7_TYOUGOURYOU3_1, GXHDO102B005Const.POTTO7_TYOUGOURYOU3_2); // ﾎﾟｯﾄ7_調合量3リスト
        List<String> potto7_tyougouryou4List = Arrays.asList(GXHDO102B005Const.POTTO7_TYOUGOURYOU4_1, GXHDO102B005Const.POTTO7_TYOUGOURYOU4_2); // ﾎﾟｯﾄ7_調合量4リスト
        
        List<String> potto8_tyougouryou1List = Arrays.asList(GXHDO102B005Const.POTTO8_TYOUGOURYOU1_1, GXHDO102B005Const.POTTO8_TYOUGOURYOU1_2); // ﾎﾟｯﾄ8_調合量1リスト
        List<String> potto8_tyougouryou2List = Arrays.asList(GXHDO102B005Const.POTTO8_TYOUGOURYOU2_1, GXHDO102B005Const.POTTO8_TYOUGOURYOU2_2); // ﾎﾟｯﾄ8_調合量2リスト
        List<String> potto8_tyougouryou3List = Arrays.asList(GXHDO102B005Const.POTTO8_TYOUGOURYOU3_1, GXHDO102B005Const.POTTO8_TYOUGOURYOU3_2); // ﾎﾟｯﾄ8_調合量3リスト
        List<String> potto8_tyougouryou4List = Arrays.asList(GXHDO102B005Const.POTTO8_TYOUGOURYOU4_1, GXHDO102B005Const.POTTO8_TYOUGOURYOU4_2); // ﾎﾟｯﾄ8_調合量4リスト
        // 規格値の入力値チェック必要の項目リスト
        List<FXHDD01> itemList = new ArrayList<>();
        processData.getItemList().forEach((fxhdd01) -> {
            itemList.add(fxhdd01);
        });
        setKikakuValueAndLabel1(processData, itemList, potto1_tyougouryou1List, potto_tyogouryoukikaku.get(0), "ﾎﾟｯﾄ1_調合量1"); // ﾎﾟｯﾄ1_調合量1の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, potto1_tyougouryou2List, potto_tyogouryoukikaku.get(1), "ﾎﾟｯﾄ1_調合量2"); // ﾎﾟｯﾄ1_調合量2の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, potto1_tyougouryou3List, potto_tyogouryoukikaku.get(2), "ﾎﾟｯﾄ1_調合量3"); // ﾎﾟｯﾄ1_調合量3の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, potto1_tyougouryou4List, potto_tyogouryoukikaku.get(3), "ﾎﾟｯﾄ1_調合量4"); // ﾎﾟｯﾄ1_調合量4の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, potto2_tyougouryou1List, potto_tyogouryoukikaku.get(4), "ﾎﾟｯﾄ2_調合量1"); // ﾎﾟｯﾄ2_調合量1の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, potto2_tyougouryou2List, potto_tyogouryoukikaku.get(5), "ﾎﾟｯﾄ2_調合量2"); // ﾎﾟｯﾄ2_調合量2の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, potto2_tyougouryou3List, potto_tyogouryoukikaku.get(6), "ﾎﾟｯﾄ2_調合量3"); // ﾎﾟｯﾄ2_調合量3の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, potto2_tyougouryou4List, potto_tyogouryoukikaku.get(7), "ﾎﾟｯﾄ2_調合量4"); // ﾎﾟｯﾄ2_調合量4の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, potto3_tyougouryou1List, potto_tyogouryoukikaku.get(8), "ﾎﾟｯﾄ3_調合量1"); // ﾎﾟｯﾄ3_調合量1の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, potto3_tyougouryou2List, potto_tyogouryoukikaku.get(9), "ﾎﾟｯﾄ3_調合量2"); // ﾎﾟｯﾄ3_調合量2の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, potto3_tyougouryou3List, potto_tyogouryoukikaku.get(10), "ﾎﾟｯﾄ3_調合量3"); // ﾎﾟｯﾄ3_調合量3の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, potto3_tyougouryou4List, potto_tyogouryoukikaku.get(11), "ﾎﾟｯﾄ3_調合量4"); // ﾎﾟｯﾄ3_調合量4の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, potto4_tyougouryou1List, potto_tyogouryoukikaku.get(12), "ﾎﾟｯﾄ4_調合量1"); // ﾎﾟｯﾄ4_調合量1の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, potto4_tyougouryou2List, potto_tyogouryoukikaku.get(13), "ﾎﾟｯﾄ4_調合量2"); // ﾎﾟｯﾄ4_調合量2の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, potto4_tyougouryou3List, potto_tyogouryoukikaku.get(14), "ﾎﾟｯﾄ4_調合量3"); // ﾎﾟｯﾄ4_調合量3の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, potto4_tyougouryou4List, potto_tyogouryoukikaku.get(15), "ﾎﾟｯﾄ4_調合量4"); // ﾎﾟｯﾄ4_調合量4の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, potto5_tyougouryou1List, potto_tyogouryoukikaku.get(16), "ﾎﾟｯﾄ5_調合量1"); // ﾎﾟｯﾄ5_調合量1の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, potto5_tyougouryou2List, potto_tyogouryoukikaku.get(17), "ﾎﾟｯﾄ5_調合量2"); // ﾎﾟｯﾄ5_調合量2の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, potto5_tyougouryou3List, potto_tyogouryoukikaku.get(18), "ﾎﾟｯﾄ5_調合量3"); // ﾎﾟｯﾄ5_調合量3の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, potto5_tyougouryou4List, potto_tyogouryoukikaku.get(19), "ﾎﾟｯﾄ5_調合量4"); // ﾎﾟｯﾄ5_調合量4の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, potto6_tyougouryou1List, potto_tyogouryoukikaku.get(20), "ﾎﾟｯﾄ6_調合量1"); // ﾎﾟｯﾄ6_調合量1の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, potto6_tyougouryou2List, potto_tyogouryoukikaku.get(21), "ﾎﾟｯﾄ6_調合量2"); // ﾎﾟｯﾄ6_調合量2の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, potto6_tyougouryou3List, potto_tyogouryoukikaku.get(22), "ﾎﾟｯﾄ6_調合量3"); // ﾎﾟｯﾄ6_調合量3の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, potto6_tyougouryou4List, potto_tyogouryoukikaku.get(23), "ﾎﾟｯﾄ6_調合量4"); // ﾎﾟｯﾄ6_調合量4の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, potto7_tyougouryou1List, potto_tyogouryoukikaku.get(24), "ﾎﾟｯﾄ7_調合量1"); // ﾎﾟｯﾄ7_調合量1の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, potto7_tyougouryou2List, potto_tyogouryoukikaku.get(25), "ﾎﾟｯﾄ7_調合量2"); // ﾎﾟｯﾄ7_調合量2の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, potto7_tyougouryou3List, potto_tyogouryoukikaku.get(26), "ﾎﾟｯﾄ7_調合量3"); // ﾎﾟｯﾄ7_調合量3の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, potto7_tyougouryou4List, potto_tyogouryoukikaku.get(27), "ﾎﾟｯﾄ7_調合量4"); // ﾎﾟｯﾄ7_調合量4の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, potto8_tyougouryou1List, potto_tyogouryoukikaku.get(28), "ﾎﾟｯﾄ8_調合量1"); // ﾎﾟｯﾄ8_調合量1の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, potto8_tyougouryou2List, potto_tyogouryoukikaku.get(29), "ﾎﾟｯﾄ8_調合量2"); // ﾎﾟｯﾄ8_調合量2の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, potto8_tyougouryou3List, potto_tyogouryoukikaku.get(30), "ﾎﾟｯﾄ8_調合量3"); // ﾎﾟｯﾄ8_調合量3の規格値と表示ﾗﾍﾞﾙ1を設置
        setKikakuValueAndLabel1(processData, itemList, potto8_tyougouryou4List, potto_tyogouryoukikaku.get(31), "ﾎﾟｯﾄ8_調合量4"); // ﾎﾟｯﾄ8_調合量4の規格値と表示ﾗﾍﾞﾙ1を設置

        potto_tyougouryouList.addAll(potto1_tyougouryou1List);
        potto_tyougouryouList.addAll(potto1_tyougouryou2List);
        potto_tyougouryouList.addAll(potto1_tyougouryou3List);
        potto_tyougouryouList.addAll(potto1_tyougouryou4List);
        
        potto_tyougouryouList.addAll(potto2_tyougouryou1List);
        potto_tyougouryouList.addAll(potto2_tyougouryou2List);
        potto_tyougouryouList.addAll(potto2_tyougouryou3List);
        potto_tyougouryouList.addAll(potto2_tyougouryou4List);
        
        potto_tyougouryouList.addAll(potto3_tyougouryou1List);
        potto_tyougouryouList.addAll(potto3_tyougouryou2List);
        potto_tyougouryouList.addAll(potto3_tyougouryou3List);
        potto_tyougouryouList.addAll(potto3_tyougouryou4List);
        
        potto_tyougouryouList.addAll(potto4_tyougouryou1List);
        potto_tyougouryouList.addAll(potto4_tyougouryou2List);
        potto_tyougouryouList.addAll(potto4_tyougouryou3List);
        potto_tyougouryouList.addAll(potto4_tyougouryou4List);
        
        potto_tyougouryouList.addAll(potto5_tyougouryou1List);
        potto_tyougouryouList.addAll(potto5_tyougouryou2List);
        potto_tyougouryouList.addAll(potto5_tyougouryou3List);
        potto_tyougouryouList.addAll(potto5_tyougouryou4List);
        
        potto_tyougouryouList.addAll(potto6_tyougouryou1List);
        potto_tyougouryouList.addAll(potto6_tyougouryou2List);
        potto_tyougouryouList.addAll(potto6_tyougouryou3List);
        potto_tyougouryouList.addAll(potto6_tyougouryou4List);
        
        potto_tyougouryouList.addAll(potto7_tyougouryou1List);
        potto_tyougouryouList.addAll(potto7_tyougouryou2List);
        potto_tyougouryouList.addAll(potto7_tyougouryou3List);
        potto_tyougouryouList.addAll(potto7_tyougouryou4List);
        
        potto_tyougouryouList.addAll(potto8_tyougouryou1List);
        potto_tyougouryouList.addAll(potto8_tyougouryou2List);
        potto_tyougouryouList.addAll(potto8_tyougouryou3List);
        potto_tyougouryouList.addAll(potto8_tyougouryou4List);

        processData.getItemListEx().stream().filter(
                (fxhdd01) -> !(potto_tyougouryouList.contains(fxhdd01.getItemId()) || potto_tyogouryoukikaku.contains(fxhdd01.getItemId()))).filter(
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
                -> (potto_tyougouryouList.contains(errorInfo.getItemId()))).forEachOrdered((errorInfo) -> {
            errorTyougouryouList.add(errorInfo.getItemId());
        });
        if (errorMessageInfo == null) {
            errorTyougouryouList.stream().map((itemId) -> potto_tyougouryouList.indexOf(itemId)).map((index) -> {
                if ((index + 1) % 2 == 0) {
                    index--;
                } else {
                    index++;
                }
                return index;
            }).map((index)
                    -> getItemRow(processData.getItemListEx(), potto_tyougouryouList.get(index))).filter((itemFxhdd01)
                    -> (itemFxhdd01 != null)).forEachOrdered((itemFxhdd01)
                    -> {
                itemFxhdd01.setBackColorInput(ErrUtil.ERR_BACK_COLOR);
            });
        } else {
            String itemId = errorMessageInfo.getErrorItemInfoList().get(0).getItemId();
            int index = potto_tyougouryouList.indexOf(itemId);
            if (index != -1) {
                if ((index + 1) % 2 == 0) {
                    index--;
                } else {
                    index++;
                }
                FXHDD01 itemFxhdd01 = getItemRow(processData.getItemListEx(), potto_tyougouryouList.get(index));
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

                // ｶﾞﾗｽｽﾗﾘｰ作製・秤量_仮登録処理
                insertTmpSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, strSystime, processData);
                // ﾎﾟｯﾄ1タブの【材料品名1】のｻﾌﾞ画面の仮登録処理
                insertTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, 1, 1, strSystime);
                // ﾎﾟｯﾄ1タブの【材料品名2】のｻﾌﾞ画面の仮登録処理
                insertTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, 1, 2, strSystime);
                // ﾎﾟｯﾄ1タブの【材料品名3】のｻﾌﾞ画面の仮登録処理
                insertTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, 1, 3, strSystime);
                // ﾎﾟｯﾄ1タブの【材料品名4】のｻﾌﾞ画面の仮登録処理
                insertTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, 1, 4, strSystime);
                
                // ﾎﾟｯﾄ2タブの【材料品名1】のｻﾌﾞ画面の仮登録処理
                insertTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, 2, 1, strSystime);
                // ﾎﾟｯﾄ2タブの【材料品名2】のｻﾌﾞ画面の仮登録処理
                insertTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, 2, 2, strSystime);
                // ﾎﾟｯﾄ2タブの【材料品名3】のｻﾌﾞ画面の仮登録処理
                insertTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, 2, 3, strSystime);
                // ﾎﾟｯﾄ2タブの【材料品名4】のｻﾌﾞ画面の仮登録処理
                insertTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, 2, 4, strSystime);
                
                // ﾎﾟｯﾄ3タブの【材料品名1】のｻﾌﾞ画面の仮登録処理
                insertTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, 3, 1, strSystime);
                // ﾎﾟｯﾄ3タブの【材料品名2】のｻﾌﾞ画面の仮登録処理
                insertTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, 3, 2, strSystime);
                // ﾎﾟｯﾄ3タブの【材料品名3】のｻﾌﾞ画面の仮登録処理
                insertTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, 3, 3, strSystime);
                // ﾎﾟｯﾄ3タブの【材料品名4】のｻﾌﾞ画面の仮登録処理
                insertTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, 3, 4, strSystime);
                
                // ﾎﾟｯﾄ4タブの【材料品名1】のｻﾌﾞ画面の仮登録処理
                insertTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, 4, 1, strSystime);
                // ﾎﾟｯﾄ4タブの【材料品名2】のｻﾌﾞ画面の仮登録処理
                insertTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, 4, 2, strSystime);
                // ﾎﾟｯﾄ4タブの【材料品名3】のｻﾌﾞ画面の仮登録処理
                insertTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, 4, 3, strSystime);
                // ﾎﾟｯﾄ4タブの【材料品名4】のｻﾌﾞ画面の仮登録処理
                insertTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, 4, 4, strSystime);
                
                // ﾎﾟｯﾄ5タブの【材料品名1】のｻﾌﾞ画面の仮登録処理
                insertTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, 5, 1, strSystime);
                // ﾎﾟｯﾄ5タブの【材料品名2】のｻﾌﾞ画面の仮登録処理
                insertTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, 5, 2, strSystime);
                // ﾎﾟｯﾄ5タブの【材料品名3】のｻﾌﾞ画面の仮登録処理
                insertTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, 5, 3, strSystime);
                // ﾎﾟｯﾄ5タブの【材料品名4】のｻﾌﾞ画面の仮登録処理
                insertTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, 5, 4, strSystime);
                
                // ﾎﾟｯﾄ6タブの【材料品名1】のｻﾌﾞ画面の仮登録処理
                insertTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, 6, 1, strSystime);
                // ﾎﾟｯﾄ6タブの【材料品名2】のｻﾌﾞ画面の仮登録処理
                insertTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, 6, 2, strSystime);
                // ﾎﾟｯﾄ6タブの【材料品名3】のｻﾌﾞ画面の仮登録処理
                insertTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, 6, 3, strSystime);
                // ﾎﾟｯﾄ6タブの【材料品名4】のｻﾌﾞ画面の仮登録処理
                insertTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, 6, 4, strSystime);
                
                // ﾎﾟｯﾄ7タブの【材料品名1】のｻﾌﾞ画面の仮登録処理
                insertTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, 7, 1, strSystime);
                // ﾎﾟｯﾄ7タブの【材料品名2】のｻﾌﾞ画面の仮登録処理
                insertTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, 7, 2, strSystime);
                // ﾎﾟｯﾄ7タブの【材料品名3】のｻﾌﾞ画面の仮登録処理
                insertTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, 7, 3, strSystime);
                // ﾎﾟｯﾄ7タブの【材料品名4】のｻﾌﾞ画面の仮登録処理
                insertTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, 7, 4, strSystime);
                
                // ﾎﾟｯﾄ8タブの【材料品名1】のｻﾌﾞ画面の仮登録処理
                insertTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, 8, 1, strSystime);
                // ﾎﾟｯﾄ8タブの【材料品名2】のｻﾌﾞ画面の仮登録処理
                insertTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, 8, 2, strSystime);
                // ﾎﾟｯﾄ8タブの【材料品名3】のｻﾌﾞ画面の仮登録処理
                insertTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, 8, 3, strSystime);
                // ﾎﾟｯﾄ8タブの【材料品名4】のｻﾌﾞ画面の仮登録処理
                insertTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, 8, 4, strSystime);
            } else {

                // ｶﾞﾗｽｽﾗﾘｰ作製・秤量_仮登録更新処理
                updateTmpSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);
                // ﾎﾟｯﾄ1タブの【材料品名1】のｻﾌﾞ画面の仮登録更新処理
                updateTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 1, 1, strSystime);
                // ﾎﾟｯﾄ1タブの【材料品名2】のｻﾌﾞ画面の仮登録更新処理
                updateTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 1, 2, strSystime);
                // ﾎﾟｯﾄ1タブの【材料品名3】のｻﾌﾞ画面の仮登録更新処理
                updateTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 1, 3, strSystime);
                // ﾎﾟｯﾄ1タブの【材料品名4】のｻﾌﾞ画面の仮登録更新処理
                updateTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 1, 4, strSystime);
                
                // ﾎﾟｯﾄ2タブの【材料品名1】のｻﾌﾞ画面の仮登録更新処理
                updateTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 2, 1, strSystime);
                // ﾎﾟｯﾄ2タブの【材料品名2】のｻﾌﾞ画面の仮登録更新処理
                updateTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 2, 2, strSystime);
                // ﾎﾟｯﾄ2タブの【材料品名3】のｻﾌﾞ画面の仮登録更新処理
                updateTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 2, 3, strSystime);
                // ﾎﾟｯﾄ2タブの【材料品名4】のｻﾌﾞ画面の仮登録更新処理
                updateTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 2, 4, strSystime);
                
                // ﾎﾟｯﾄ3タブの【材料品名1】のｻﾌﾞ画面の仮登録更新処理
                updateTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 3, 1, strSystime);
                // ﾎﾟｯﾄ3タブの【材料品名2】のｻﾌﾞ画面の仮登録更新処理
                updateTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 3, 2, strSystime);
                // ﾎﾟｯﾄ3タブの【材料品名3】のｻﾌﾞ画面の仮登録更新処理
                updateTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 3, 3, strSystime);
                // ﾎﾟｯﾄ3タブの【材料品名4】のｻﾌﾞ画面の仮登録更新処理
                updateTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 3, 4, strSystime);
                
                // ﾎﾟｯﾄ4タブの【材料品名1】のｻﾌﾞ画面の仮登録更新処理
                updateTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 4, 1, strSystime);
                // ﾎﾟｯﾄ4タブの【材料品名2】のｻﾌﾞ画面の仮登録更新処理
                updateTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 4, 2, strSystime);
                // ﾎﾟｯﾄ4タブの【材料品名3】のｻﾌﾞ画面の仮登録更新処理
                updateTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 4, 3, strSystime);
                // ﾎﾟｯﾄ4タブの【材料品名4】のｻﾌﾞ画面の仮登録更新処理
                updateTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 4, 4, strSystime);
                
                // ﾎﾟｯﾄ5タブの【材料品名1】のｻﾌﾞ画面の仮登録更新処理
                updateTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 5, 1, strSystime);
                // ﾎﾟｯﾄ5タブの【材料品名2】のｻﾌﾞ画面の仮登録更新処理
                updateTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 5, 2, strSystime);
                // ﾎﾟｯﾄ5タブの【材料品名3】のｻﾌﾞ画面の仮登録更新処理
                updateTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 5, 3, strSystime);
                // ﾎﾟｯﾄ5タブの【材料品名4】のｻﾌﾞ画面の仮登録更新処理
                updateTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 5, 4, strSystime);
                
                // ﾎﾟｯﾄ6タブの【材料品名1】のｻﾌﾞ画面の仮登録更新処理
                updateTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 6, 1, strSystime);
                // ﾎﾟｯﾄ6タブの【材料品名2】のｻﾌﾞ画面の仮登録更新処理
                updateTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 6, 2, strSystime);
                // ﾎﾟｯﾄ6タブの【材料品名3】のｻﾌﾞ画面の仮登録更新処理
                updateTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 6, 3, strSystime);
                // ﾎﾟｯﾄ6タブの【材料品名4】のｻﾌﾞ画面の仮登録更新処理
                updateTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 6, 4, strSystime);
                
                // ﾎﾟｯﾄ7タブの【材料品名1】のｻﾌﾞ画面の仮登録更新処理
                updateTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 7, 1, strSystime);
                // ﾎﾟｯﾄ7タブの【材料品名2】のｻﾌﾞ画面の仮登録更新処理
                updateTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 7, 2, strSystime);
                // ﾎﾟｯﾄ7タブの【材料品名3】のｻﾌﾞ画面の仮登録更新処理
                updateTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 7, 3, strSystime);
                // ﾎﾟｯﾄ7タブの【材料品名4】のｻﾌﾞ画面の仮登録更新処理
                updateTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 7, 4, strSystime);
                
                // ﾎﾟｯﾄ8タブの【材料品名1】のｻﾌﾞ画面の仮登録更新処理
                updateTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 8, 1, strSystime);
                // ﾎﾟｯﾄ8タブの【材料品名2】のｻﾌﾞ画面の仮登録更新処理
                updateTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 8, 2, strSystime);
                // ﾎﾟｯﾄ8タブの【材料品名3】のｻﾌﾞ画面の仮登録更新処理
                updateTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 8, 3, strSystime);
                // ﾎﾟｯﾄ8タブの【材料品名4】のｻﾌﾞ画面の仮登録更新処理
                updateTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 8, 4, strSystime);
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
            processData.getItemListEx().forEach((fxhdd01) -> {
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
            SrGlassslurryhyoryo tmpSrGlassslurryhyoryo = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrGlassslurryhyoryo> srGlassslurryhyoryoList = getSrGlassslurryhyoryoData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo9, edaban);
                if (!srGlassslurryhyoryoList.isEmpty()) {
                    tmpSrGlassslurryhyoryo = srGlassslurryhyoryoList.get(0);
                }

                deleteTmpSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);
                deleteTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);
            }

            // ｶﾞﾗｽｽﾗﾘｰ作製・秤量_登録処理
            insertSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, strSystime, processData, tmpSrGlassslurryhyoryo);
            // ﾎﾟｯﾄ1タブの【材料品名1】のｻﾌﾞ画面の登録処理
            insertSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, 1, 1, strSystime);
            // ﾎﾟｯﾄ1タブの【材料品名2】のｻﾌﾞ画面の登録処理
            insertSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, 1, 2, strSystime);
            // ﾎﾟｯﾄ1タブの【材料品名3】のｻﾌﾞ画面の登録処理
            insertSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, 1, 3, strSystime);
            // ﾎﾟｯﾄ1タブの【材料品名4】のｻﾌﾞ画面の登録処理
            insertSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, 1, 4, strSystime);
            
            // ﾎﾟｯﾄ2タブの【材料品名1】のｻﾌﾞ画面の登録処理
            insertSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, 2, 1, strSystime);
            // ﾎﾟｯﾄ2タブの【材料品名2】のｻﾌﾞ画面の登録処理
            insertSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, 2, 2, strSystime);
            // ﾎﾟｯﾄ2タブの【材料品名3】のｻﾌﾞ画面の登録処理
            insertSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, 2, 3, strSystime);
            // ﾎﾟｯﾄ2タブの【材料品名4】のｻﾌﾞ画面の登録処理
            insertSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, 2, 4, strSystime);
            
            // ﾎﾟｯﾄ3タブの【材料品名1】のｻﾌﾞ画面の登録処理
            insertSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, 3, 1, strSystime);
            // ﾎﾟｯﾄ3タブの【材料品名2】のｻﾌﾞ画面の登録処理
            insertSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, 3, 2, strSystime);
            // ﾎﾟｯﾄ3タブの【材料品名3】のｻﾌﾞ画面の登録処理
            insertSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, 3, 3, strSystime);
            // ﾎﾟｯﾄ3タブの【材料品名4】のｻﾌﾞ画面の登録処理
            insertSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, 3, 4, strSystime);
            
            // ﾎﾟｯﾄ4タブの【材料品名1】のｻﾌﾞ画面の登録処理
            insertSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, 4, 1, strSystime);
            // ﾎﾟｯﾄ4タブの【材料品名2】のｻﾌﾞ画面の登録処理
            insertSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, 4, 2, strSystime);
            // ﾎﾟｯﾄ4タブの【材料品名3】のｻﾌﾞ画面の登録処理
            insertSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, 4, 3, strSystime);
            // ﾎﾟｯﾄ4タブの【材料品名4】のｻﾌﾞ画面の登録処理
            insertSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, 4, 4, strSystime);
            
            // ﾎﾟｯﾄ5タブの【材料品名1】のｻﾌﾞ画面の登録処理
            insertSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, 5, 1, strSystime);
            // ﾎﾟｯﾄ5タブの【材料品名2】のｻﾌﾞ画面の登録処理
            insertSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, 5, 2, strSystime);
            // ﾎﾟｯﾄ5タブの【材料品名3】のｻﾌﾞ画面の登録処理
            insertSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, 5, 3, strSystime);
            // ﾎﾟｯﾄ5タブの【材料品名4】のｻﾌﾞ画面の登録処理
            insertSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, 5, 4, strSystime);
            
            // ﾎﾟｯﾄ6タブの【材料品名1】のｻﾌﾞ画面の登録処理
            insertSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, 6, 1, strSystime);
            // ﾎﾟｯﾄ6タブの【材料品名2】のｻﾌﾞ画面の登録処理
            insertSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, 6, 2, strSystime);
            // ﾎﾟｯﾄ6タブの【材料品名3】のｻﾌﾞ画面の登録処理
            insertSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, 6, 3, strSystime);
            // ﾎﾟｯﾄ6タブの【材料品名4】のｻﾌﾞ画面の登録処理
            insertSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, 6, 4, strSystime);
            
            // ﾎﾟｯﾄ7タブの【材料品名1】のｻﾌﾞ画面の登録処理
            insertSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, 7, 1, strSystime);
            // ﾎﾟｯﾄ7タブの【材料品名2】のｻﾌﾞ画面の登録処理
            insertSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, 7, 2, strSystime);
            // ﾎﾟｯﾄ7タブの【材料品名3】のｻﾌﾞ画面の登録処理
            insertSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, 7, 3, strSystime);
            // ﾎﾟｯﾄ7タブの【材料品名4】のｻﾌﾞ画面の登録処理
            insertSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, 7, 4, strSystime);
            
            // ﾎﾟｯﾄ8タブの【材料品名1】のｻﾌﾞ画面の登録処理
            insertSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, 8, 1, strSystime);
            // ﾎﾟｯﾄ8タブの【材料品名2】のｻﾌﾞ画面の登録処理
            insertSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, 8, 2, strSystime);
            // ﾎﾟｯﾄ8タブの【材料品名3】のｻﾌﾞ画面の登録処理
            insertSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, 8, 3, strSystime);
            // ﾎﾟｯﾄ8タブの【材料品名4】のｻﾌﾞ画面の登録処理
            insertSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, 8, 4, strSystime);
            
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
        processData.setUserAuthParam(GXHDO102B005Const.USER_AUTH_UPDATE_PARAM);

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

            // ｶﾞﾗｽｽﾗﾘｰ作製・秤量_更新処理
            updateSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);
            // ﾎﾟｯﾄ1タブの【材料品名1】のｻﾌﾞ画面の更新処理
            updateSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 1, 1, strSystime);
            // ﾎﾟｯﾄ1タブの【材料品名2】のｻﾌﾞ画面の更新処理
            updateSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 1, 2, strSystime);
            // ﾎﾟｯﾄ1タブの【材料品名3】のｻﾌﾞ画面の更新処理
            updateSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 1, 3, strSystime);
            // ﾎﾟｯﾄ1タブの【材料品名4】のｻﾌﾞ画面の更新処理
            updateSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 1, 4, strSystime);
            
            // ﾎﾟｯﾄ2タブの【材料品名1】のｻﾌﾞ画面の更新処理
            updateSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 2, 1, strSystime);
            // ﾎﾟｯﾄ2タブの【材料品名2】のｻﾌﾞ画面の更新処理
            updateSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 2, 2, strSystime);
            // ﾎﾟｯﾄ2タブの【材料品名3】のｻﾌﾞ画面の更新処理
            updateSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 2, 3, strSystime);
            // ﾎﾟｯﾄ2タブの【材料品名4】のｻﾌﾞ画面の更新処理
            updateSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 2, 4, strSystime);

            // ﾎﾟｯﾄ3タブの【材料品名1】のｻﾌﾞ画面の更新処理
            updateSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 3, 1, strSystime);
            // ﾎﾟｯﾄ3タブの【材料品名2】のｻﾌﾞ画面の更新処理
            updateSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 3, 2, strSystime);
            // ﾎﾟｯﾄ3タブの【材料品名3】のｻﾌﾞ画面の更新処理
            updateSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 3, 3, strSystime);
            // ﾎﾟｯﾄ3タブの【材料品名4】のｻﾌﾞ画面の更新処理
            updateSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 3, 4, strSystime);

            // ﾎﾟｯﾄ4タブの【材料品名1】のｻﾌﾞ画面の更新処理
            updateSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 4, 1, strSystime);
            // ﾎﾟｯﾄ4タブの【材料品名2】のｻﾌﾞ画面の更新処理
            updateSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 4, 2, strSystime);
            // ﾎﾟｯﾄ4タブの【材料品名3】のｻﾌﾞ画面の更新処理
            updateSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 4, 3, strSystime);
            // ﾎﾟｯﾄ4タブの【材料品名4】のｻﾌﾞ画面の更新処理
            updateSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 4, 4, strSystime);

            // ﾎﾟｯﾄ5タブの【材料品名1】のｻﾌﾞ画面の更新処理
            updateSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 5, 1, strSystime);
            // ﾎﾟｯﾄ5タブの【材料品名2】のｻﾌﾞ画面の更新処理
            updateSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 5, 2, strSystime);
            // ﾎﾟｯﾄ5タブの【材料品名3】のｻﾌﾞ画面の更新処理
            updateSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 5, 3, strSystime);
            // ﾎﾟｯﾄ5タブの【材料品名4】のｻﾌﾞ画面の更新処理
            updateSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 5, 4, strSystime);

            // ﾎﾟｯﾄ6タブの【材料品名1】のｻﾌﾞ画面の更新処理
            updateSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 6, 1, strSystime);
            // ﾎﾟｯﾄ6タブの【材料品名2】のｻﾌﾞ画面の更新処理
            updateSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 6, 2, strSystime);
            // ﾎﾟｯﾄ6タブの【材料品名3】のｻﾌﾞ画面の更新処理
            updateSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 6, 3, strSystime);
            // ﾎﾟｯﾄ6タブの【材料品名4】のｻﾌﾞ画面の更新処理
            updateSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 6, 4, strSystime);

            // ﾎﾟｯﾄ7タブの【材料品名1】のｻﾌﾞ画面の更新処理
            updateSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 7, 1, strSystime);
            // ﾎﾟｯﾄ7タブの【材料品名2】のｻﾌﾞ画面の更新処理
            updateSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 7, 2, strSystime);
            // ﾎﾟｯﾄ7タブの【材料品名3】のｻﾌﾞ画面の更新処理
            updateSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 7, 3, strSystime);
            // ﾎﾟｯﾄ7タブの【材料品名4】のｻﾌﾞ画面の更新処理
            updateSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 7, 4, strSystime);

            // ﾎﾟｯﾄ8タブの【材料品名1】のｻﾌﾞ画面の更新処理
            updateSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 8, 1, strSystime);
            // ﾎﾟｯﾄ8タブの【材料品名2】のｻﾌﾞ画面の更新処理
            updateSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 8, 2, strSystime);
            // ﾎﾟｯﾄ8タブの【材料品名3】のｻﾌﾞ画面の更新処理
            updateSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 8, 3, strSystime);
            // ﾎﾟｯﾄ8タブの【材料品名4】のｻﾌﾞ画面の更新処理
            updateSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo9, edaban, 8, 4, strSystime);

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
        processData.setUserAuthParam(GXHDO102B005Const.USER_AUTH_DELETE_PARAM);

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

            // ｶﾞﾗｽｽﾗﾘｰ作製・秤量_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo9, edaban, paramJissekino);
            insertDeleteDataTmpSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo9, edaban, paramJissekino, strSystime);

            // ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力_ｻﾌﾞ画面仮登録登録処理
            insertDeleteDataTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo9, edaban, strSystime);
            
            // ｶﾞﾗｽｽﾗﾘｰ作製・秤量_削除処理
            deleteSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);

            // ｶﾞﾗｽｽﾗﾘｰ・秤量入力_ｻﾌﾞ画面削除処理
            deleteSubSrGlassslurryhyoryo(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);

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
     * 秤量日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setHyouryounichijiDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B005Const.SEIHIN_HYOURYOUNICHIJI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B005Const.SEIHIN_HYOURYOUNICHIJI_TIME);
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
                        GXHDO102B005Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B005Const.BTN_HYOURYOUNICHIJI_TOP,
                        GXHDO102B005Const.BTN_UPDATE_TOP,
                        GXHDO102B005Const.BTN_DELETE_TOP,
                        GXHDO102B005Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B005Const.BTN_HYOURYOUNICHIJI_BOTTOM,
                        GXHDO102B005Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B005Const.BTN_DELETE_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B005Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B005Const.BTN_INSERT_TOP,
                        GXHDO102B005Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B005Const.BTN_INSERT_BOTTOM));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO102B005Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B005Const.BTN_HYOURYOUNICHIJI_TOP,
                        GXHDO102B005Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B005Const.BTN_INSERT_TOP,
                        GXHDO102B005Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B005Const.BTN_HYOURYOUNICHIJI_BOTTOM,
                        GXHDO102B005Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B005Const.BTN_INSERT_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B005Const.BTN_UPDATE_TOP,
                        GXHDO102B005Const.BTN_DELETE_TOP,
                        GXHDO102B005Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B005Const.BTN_DELETE_BOTTOM
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
        // ﾛｯﾄ区分チェック
        String lotkubun = (String) shikakariData.get("lotkubun");
        if (StringUtil.isEmpty(lotkubun)) {
            errorMessageList.add(MessageUtil.getMessage("XHD-000015"));
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
        GXHDO102C002Logic.set102B005ItemStyle(processData.getItemList());
        // 画面のラベル項目の値の背景色を取得できない場合、デフォルト値を設置
        GXHDO102C002Logic.set102B005ItemStyle(processData.getItemListEx());
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
        this.setItemData(processData, GXHDO102B005Const.SEIHIN_WIPLOTNO, lotNo);
        // ｶﾞﾗｽｽﾗﾘｰ品名
        this.setItemData(processData, GXHDO102B005Const.SEIHIN_GLASSSLURRYHINMEI, StringUtil.nullToBlank(getMapData(shikakariData, "hinmei")));
        // ｶﾞﾗｽｽﾗﾘｰ品名LotNo
        this.setItemData(processData, GXHDO102B005Const.SEIHIN_GLASSSLURRYLOTNO, StringUtil.nullToBlank(getMapData(shikakariData, "lotno")));
        // ﾛｯﾄ区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode"));
        // ﾛｯﾄ区分名称
        String lotkubun = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubun"));
        
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO102B005Const.SEIHIN_LOT_KUBUN, "");
        } else {
            if (!StringUtil.isEmpty(lotkubun)) {
                lotkubuncode = lotkubuncode + ":" + lotkubun;
            }
            this.setItemData(processData, GXHDO102B005Const.SEIHIN_LOT_KUBUN, lotkubuncode);
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

        List<SrGlassslurryhyoryo> srGlassslurryhyoryoList = new ArrayList<>();
        List<SubSrGlassslurryhyoryo> subSrGlassslurryhyoryoList = new ArrayList<>();
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
                processData.getItemListEx().forEach((fxhdd001) -> {
                    this.setItemDataEx(processData, fxhdd001.getItemId(), fxhdd001.getInputDefault());
                });

                // ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力_ｻﾌﾞ画面データ設定
                setInputItemDataSubFormC002(processData, null);
                return true;
            }

            // ｶﾞﾗｽｽﾗﾘｰ作製・秤量データ取得
            srGlassslurryhyoryoList = getSrGlassslurryhyoryoData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo9, edaban);
            if (srGlassslurryhyoryoList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力_サブ画面データ取得
            subSrGlassslurryhyoryoList = getSubSrGlassslurryhyoryoData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo9, edaban);
            if (subSrGlassslurryhyoryoList.isEmpty() || subSrGlassslurryhyoryoList.size() != 32) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }
            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srGlassslurryhyoryoList.isEmpty() || (subSrGlassslurryhyoryoList.isEmpty() || subSrGlassslurryhyoryoList.size() != 32)) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srGlassslurryhyoryoList.get(0));
        // ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力_ｻﾌﾞ画面データ設定
        setInputItemDataSubFormC002(processData, subSrGlassslurryhyoryoList);
        return true;

    }

    /**
     * データ設定処理
     *
     * @param processData 処理制御データ
     * @param srGlassslurryhyoryo ｶﾞﾗｽｽﾗﾘｰ作製・秤量データ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrGlassslurryhyoryo srGlassslurryhyoryo) {
        //製品情報
        setInputItemDataFormA(processData, srGlassslurryhyoryo);
        //ﾎﾟｯﾄ1
        setInputItemDataFormB(processData, srGlassslurryhyoryo);
        //ﾎﾟｯﾄ2
        setInputItemDataFormC(processData, srGlassslurryhyoryo);
        //ﾎﾟｯﾄ3
        setInputItemDataFormD(processData, srGlassslurryhyoryo);
        //ﾎﾟｯﾄ4
        setInputItemDataFormE(processData, srGlassslurryhyoryo);
        //ﾎﾟｯﾄ5
        setInputItemDataFormF(processData, srGlassslurryhyoryo);
        //ﾎﾟｯﾄ6
        setInputItemDataFormG(processData, srGlassslurryhyoryo);
        //ﾎﾟｯﾄ7
        setInputItemDataFormH(processData, srGlassslurryhyoryo);
        //ﾎﾟｯﾄ8
        setInputItemDataFormI(processData, srGlassslurryhyoryo);
    }

    /**
     * 製品情報データ設定処理
     *
     * @param processData 処理制御データ
     * @param srGlassslurryhyoryo ｶﾞﾗｽｽﾗﾘｰ作製・秤量データ
     */
    private void setInputItemDataFormA(ProcessData processData, SrGlassslurryhyoryo srGlassslurryhyoryo) {
        // 秤量号機
        this.setItemData(processData, GXHDO102B005Const.SEIHIN_GOKI, getGlassslurryhyoryoItemData(GXHDO102B005Const.SEIHIN_GOKI, srGlassslurryhyoryo));
        //秤量日
        this.setItemData(processData, GXHDO102B005Const.SEIHIN_HYOURYOUNICHIJI_DAY, getGlassslurryhyoryoItemData(GXHDO102B005Const.SEIHIN_HYOURYOUNICHIJI_DAY, srGlassslurryhyoryo));
        //秤量時間
        this.setItemData(processData, GXHDO102B005Const.SEIHIN_HYOURYOUNICHIJI_TIME, getGlassslurryhyoryoItemData(GXHDO102B005Const.SEIHIN_HYOURYOUNICHIJI_TIME, srGlassslurryhyoryo));
        //担当者
        this.setItemData(processData, GXHDO102B005Const.SEIHIN_TANTOUSYA, getGlassslurryhyoryoItemData(GXHDO102B005Const.SEIHIN_TANTOUSYA, srGlassslurryhyoryo));
        //確認者
        this.setItemData(processData, GXHDO102B005Const.SEIHIN_KAKUNINSYA, getGlassslurryhyoryoItemData(GXHDO102B005Const.SEIHIN_KAKUNINSYA, srGlassslurryhyoryo));
        //備考1
        this.setItemData(processData, GXHDO102B005Const.SEIHIN_BIKOU1, getGlassslurryhyoryoItemData(GXHDO102B005Const.SEIHIN_BIKOU1, srGlassslurryhyoryo));
        //備考2
        this.setItemData(processData, GXHDO102B005Const.SEIHIN_BIKOU2, getGlassslurryhyoryoItemData(GXHDO102B005Const.SEIHIN_BIKOU2, srGlassslurryhyoryo));
    }

    /**
     * ﾎﾟｯﾄ1データ設定処理
     *
     * @param processData 処理制御データ
     * @param srGlassslurryhyoryo ｶﾞﾗｽｽﾗﾘｰ作製・秤量データ
     */
    private void setInputItemDataFormB(ProcessData processData, SrGlassslurryhyoryo srGlassslurryhyoryo) {

        // ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_材料品名1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO1_ZAIRYOHINMEI1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO1_ZAIRYOHINMEI1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_調合量規格1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_資材ﾛｯﾄNo.1_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO1_SIZAILOTNO1_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO1_SIZAILOTNO1_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_調合量1_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO1_TYOUGOURYOU1_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO1_TYOUGOURYOU1_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_資材ﾛｯﾄNo.1_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO1_SIZAILOTNO1_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO1_SIZAILOTNO1_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_調合量1_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO1_TYOUGOURYOU1_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO1_TYOUGOURYOU1_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_材料品名2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO1_ZAIRYOHINMEI2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO1_ZAIRYOHINMEI2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_調合量規格2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_資材ﾛｯﾄNo.2_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO1_SIZAILOTNO2_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO1_SIZAILOTNO2_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_調合量2_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO1_TYOUGOURYOU2_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO1_TYOUGOURYOU2_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_資材ﾛｯﾄNo.2_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO1_SIZAILOTNO2_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO1_SIZAILOTNO2_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_調合量2_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO1_TYOUGOURYOU2_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO1_TYOUGOURYOU2_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_材料品名3
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO1_ZAIRYOHINMEI3, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO1_ZAIRYOHINMEI3, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_調合量規格3
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU3, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU3, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_資材ﾛｯﾄNo.3_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO1_SIZAILOTNO3_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO1_SIZAILOTNO3_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_調合量3_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO1_TYOUGOURYOU3_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO1_TYOUGOURYOU3_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_資材ﾛｯﾄNo.3_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO1_SIZAILOTNO3_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO1_SIZAILOTNO3_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_調合量3_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO1_TYOUGOURYOU3_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO1_TYOUGOURYOU3_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_材料品名4
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO1_ZAIRYOHINMEI4, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO1_ZAIRYOHINMEI4, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_調合量規格4
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU4, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU4, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_資材ﾛｯﾄNo.4_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO1_SIZAILOTNO4_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO1_SIZAILOTNO4_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_調合量4_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO1_TYOUGOURYOU4_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO1_TYOUGOURYOU4_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_資材ﾛｯﾄNo.4_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO1_SIZAILOTNO4_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO1_SIZAILOTNO4_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_調合量4_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO1_TYOUGOURYOU4_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO1_TYOUGOURYOU4_2, srGlassslurryhyoryo));

    }

    /**
     * ﾎﾟｯﾄ2データ設定処理
     *
     * @param processData 処理制御データ
     * @param srGlassslurryhyoryo ｶﾞﾗｽｽﾗﾘｰ作製・秤量データ
     */
    private void setInputItemDataFormC(ProcessData processData, SrGlassslurryhyoryo srGlassslurryhyoryo) {
        // ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_材料品名1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO2_ZAIRYOHINMEI1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO2_ZAIRYOHINMEI1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_調合量規格1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_資材ﾛｯﾄNo.1_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO2_SIZAILOTNO1_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO2_SIZAILOTNO1_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_調合量1_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO2_TYOUGOURYOU1_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO2_TYOUGOURYOU1_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_資材ﾛｯﾄNo.1_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO2_SIZAILOTNO1_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO2_SIZAILOTNO1_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_調合量1_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO2_TYOUGOURYOU1_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO2_TYOUGOURYOU1_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_材料品名2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO2_ZAIRYOHINMEI2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO2_ZAIRYOHINMEI2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_調合量規格2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_資材ﾛｯﾄNo.2_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO2_SIZAILOTNO2_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO2_SIZAILOTNO2_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_調合量2_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO2_TYOUGOURYOU2_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO2_TYOUGOURYOU2_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_資材ﾛｯﾄNo.2_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO2_SIZAILOTNO2_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO2_SIZAILOTNO2_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_調合量2_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO2_TYOUGOURYOU2_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO2_TYOUGOURYOU2_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_材料品名3
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO2_ZAIRYOHINMEI3, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO2_ZAIRYOHINMEI3, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_調合量規格3
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU3, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU3, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_資材ﾛｯﾄNo.3_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO2_SIZAILOTNO3_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO2_SIZAILOTNO3_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_調合量3_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO2_TYOUGOURYOU3_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO2_TYOUGOURYOU3_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_資材ﾛｯﾄNo.3_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO2_SIZAILOTNO3_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO2_SIZAILOTNO3_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_調合量3_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO2_TYOUGOURYOU3_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO2_TYOUGOURYOU3_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_材料品名4
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO2_ZAIRYOHINMEI4, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO2_ZAIRYOHINMEI4, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_調合量規格4
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU4, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU4, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_資材ﾛｯﾄNo.4_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO2_SIZAILOTNO4_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO2_SIZAILOTNO4_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_調合量4_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO2_TYOUGOURYOU4_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO2_TYOUGOURYOU4_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_資材ﾛｯﾄNo.4_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO2_SIZAILOTNO4_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO2_SIZAILOTNO4_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_調合量4_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO2_TYOUGOURYOU4_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO2_TYOUGOURYOU4_2, srGlassslurryhyoryo));
    }

    /**
     * ﾎﾟｯﾄ3データ設定処理
     *
     * @param processData 処理制御データ
     * @param srGlassslurryhyoryo ｶﾞﾗｽｽﾗﾘｰ作製・秤量データ
     */
    private void setInputItemDataFormD(ProcessData processData, SrGlassslurryhyoryo srGlassslurryhyoryo) {
        // ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_材料品名1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO3_ZAIRYOHINMEI1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO3_ZAIRYOHINMEI1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_調合量規格1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_資材ﾛｯﾄNo.1_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO3_SIZAILOTNO1_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO3_SIZAILOTNO1_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_調合量1_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO3_TYOUGOURYOU1_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO3_TYOUGOURYOU1_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_資材ﾛｯﾄNo.1_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO3_SIZAILOTNO1_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO3_SIZAILOTNO1_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_調合量1_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO3_TYOUGOURYOU1_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO3_TYOUGOURYOU1_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_材料品名2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO3_ZAIRYOHINMEI2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO3_ZAIRYOHINMEI2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_調合量規格2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_資材ﾛｯﾄNo.2_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO3_SIZAILOTNO2_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO3_SIZAILOTNO2_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_調合量2_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO3_TYOUGOURYOU2_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO3_TYOUGOURYOU2_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_資材ﾛｯﾄNo.2_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO3_SIZAILOTNO2_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO3_SIZAILOTNO2_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_調合量2_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO3_TYOUGOURYOU2_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO3_TYOUGOURYOU2_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_材料品名3
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO3_ZAIRYOHINMEI3, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO3_ZAIRYOHINMEI3, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_調合量規格3
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU3, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU3, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_資材ﾛｯﾄNo.3_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO3_SIZAILOTNO3_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO3_SIZAILOTNO3_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_調合量3_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO3_TYOUGOURYOU3_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO3_TYOUGOURYOU3_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_資材ﾛｯﾄNo.3_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO3_SIZAILOTNO3_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO3_SIZAILOTNO3_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_調合量3_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO3_TYOUGOURYOU3_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO3_TYOUGOURYOU3_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_材料品名4
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO3_ZAIRYOHINMEI4, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO3_ZAIRYOHINMEI4, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_調合量規格4
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU4, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU4, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_資材ﾛｯﾄNo.4_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO3_SIZAILOTNO4_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO3_SIZAILOTNO4_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_調合量4_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO3_TYOUGOURYOU4_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO3_TYOUGOURYOU4_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_資材ﾛｯﾄNo.4_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO3_SIZAILOTNO4_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO3_SIZAILOTNO4_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_調合量4_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO3_TYOUGOURYOU4_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO3_TYOUGOURYOU4_2, srGlassslurryhyoryo));
    }

    /**
     * ﾎﾟｯﾄ4データ設定処理
     *
     * @param processData 処理制御データ
     * @param srGlassslurryhyoryo ｶﾞﾗｽｽﾗﾘｰ作製・秤量データ
     */
    private void setInputItemDataFormE(ProcessData processData, SrGlassslurryhyoryo srGlassslurryhyoryo) {
        // ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_材料品名1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO4_ZAIRYOHINMEI1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO4_ZAIRYOHINMEI1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_調合量規格1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_資材ﾛｯﾄNo.1_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO4_SIZAILOTNO1_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO4_SIZAILOTNO1_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_調合量1_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO4_TYOUGOURYOU1_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO4_TYOUGOURYOU1_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_資材ﾛｯﾄNo.1_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO4_SIZAILOTNO1_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO4_SIZAILOTNO1_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_調合量1_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO4_TYOUGOURYOU1_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO4_TYOUGOURYOU1_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_材料品名2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO4_ZAIRYOHINMEI2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO4_ZAIRYOHINMEI2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_調合量規格2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_資材ﾛｯﾄNo.2_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO4_SIZAILOTNO2_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO4_SIZAILOTNO2_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_調合量2_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO4_TYOUGOURYOU2_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO4_TYOUGOURYOU2_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_資材ﾛｯﾄNo.2_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO4_SIZAILOTNO2_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO4_SIZAILOTNO2_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_調合量2_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO4_TYOUGOURYOU2_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO4_TYOUGOURYOU2_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_材料品名3
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO4_ZAIRYOHINMEI3, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO4_ZAIRYOHINMEI3, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_調合量規格3
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU3, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU3, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_資材ﾛｯﾄNo.3_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO4_SIZAILOTNO3_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO4_SIZAILOTNO3_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_調合量3_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO4_TYOUGOURYOU3_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO4_TYOUGOURYOU3_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_資材ﾛｯﾄNo.3_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO4_SIZAILOTNO3_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO4_SIZAILOTNO3_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_調合量3_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO4_TYOUGOURYOU3_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO4_TYOUGOURYOU3_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_材料品名4
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO4_ZAIRYOHINMEI4, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO4_ZAIRYOHINMEI4, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_調合量規格4
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU4, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU4, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_資材ﾛｯﾄNo.4_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO4_SIZAILOTNO4_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO4_SIZAILOTNO4_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_調合量4_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO4_TYOUGOURYOU4_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO4_TYOUGOURYOU4_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_資材ﾛｯﾄNo.4_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO4_SIZAILOTNO4_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO4_SIZAILOTNO4_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_調合量4_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO4_TYOUGOURYOU4_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO4_TYOUGOURYOU4_2, srGlassslurryhyoryo));
    }

    /**
     * ﾎﾟｯﾄ5データ設定処理
     *
     * @param processData 処理制御データ
     * @param srGlassslurryhyoryo ｶﾞﾗｽｽﾗﾘｰ作製・秤量データ
     */
    private void setInputItemDataFormF(ProcessData processData, SrGlassslurryhyoryo srGlassslurryhyoryo) {
        // ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_材料品名1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO5_ZAIRYOHINMEI1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO5_ZAIRYOHINMEI1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_調合量規格1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_資材ﾛｯﾄNo.1_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO5_SIZAILOTNO1_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO5_SIZAILOTNO1_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_調合量1_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO5_TYOUGOURYOU1_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO5_TYOUGOURYOU1_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_資材ﾛｯﾄNo.1_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO5_SIZAILOTNO1_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO5_SIZAILOTNO1_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_調合量1_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO5_TYOUGOURYOU1_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO5_TYOUGOURYOU1_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_材料品名2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO5_ZAIRYOHINMEI2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO5_ZAIRYOHINMEI2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_調合量規格2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_資材ﾛｯﾄNo.2_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO5_SIZAILOTNO2_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO5_SIZAILOTNO2_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_調合量2_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO5_TYOUGOURYOU2_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO5_TYOUGOURYOU2_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_資材ﾛｯﾄNo.2_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO5_SIZAILOTNO2_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO5_SIZAILOTNO2_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_調合量2_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO5_TYOUGOURYOU2_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO5_TYOUGOURYOU2_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_材料品名3
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO5_ZAIRYOHINMEI3, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO5_ZAIRYOHINMEI3, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_調合量規格3
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU3, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU3, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_資材ﾛｯﾄNo.3_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO5_SIZAILOTNO3_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO5_SIZAILOTNO3_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_調合量3_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO5_TYOUGOURYOU3_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO5_TYOUGOURYOU3_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_資材ﾛｯﾄNo.3_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO5_SIZAILOTNO3_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO5_SIZAILOTNO3_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_調合量3_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO5_TYOUGOURYOU3_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO5_TYOUGOURYOU3_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_材料品名4
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO5_ZAIRYOHINMEI4, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO5_ZAIRYOHINMEI4, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_調合量規格4
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU4, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU4, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_資材ﾛｯﾄNo.4_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO5_SIZAILOTNO4_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO5_SIZAILOTNO4_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_調合量4_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO5_TYOUGOURYOU4_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO5_TYOUGOURYOU4_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_資材ﾛｯﾄNo.4_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO5_SIZAILOTNO4_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO5_SIZAILOTNO4_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_調合量4_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO5_TYOUGOURYOU4_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO5_TYOUGOURYOU4_2, srGlassslurryhyoryo));

    }

    /**
     * ﾎﾟｯﾄ6データ設定処理
     *
     * @param processData 処理制御データ
     * @param srGlassslurryhyoryo ｶﾞﾗｽｽﾗﾘｰ作製・秤量データ
     */
    private void setInputItemDataFormG(ProcessData processData, SrGlassslurryhyoryo srGlassslurryhyoryo) {
        // ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_材料品名1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO6_ZAIRYOHINMEI1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO6_ZAIRYOHINMEI1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_調合量規格1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_資材ﾛｯﾄNo.1_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO6_SIZAILOTNO1_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO6_SIZAILOTNO1_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_調合量1_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO6_TYOUGOURYOU1_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO6_TYOUGOURYOU1_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_資材ﾛｯﾄNo.1_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO6_SIZAILOTNO1_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO6_SIZAILOTNO1_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_調合量1_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO6_TYOUGOURYOU1_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO6_TYOUGOURYOU1_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_材料品名2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO6_ZAIRYOHINMEI2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO6_ZAIRYOHINMEI2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_調合量規格2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_資材ﾛｯﾄNo.2_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO6_SIZAILOTNO2_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO6_SIZAILOTNO2_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_調合量2_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO6_TYOUGOURYOU2_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO6_TYOUGOURYOU2_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_資材ﾛｯﾄNo.2_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO6_SIZAILOTNO2_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO6_SIZAILOTNO2_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_調合量2_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO6_TYOUGOURYOU2_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO6_TYOUGOURYOU2_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_材料品名3
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO6_ZAIRYOHINMEI3, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO6_ZAIRYOHINMEI3, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_調合量規格3
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU3, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU3, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_資材ﾛｯﾄNo.3_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO6_SIZAILOTNO3_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO6_SIZAILOTNO3_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_調合量3_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO6_TYOUGOURYOU3_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO6_TYOUGOURYOU3_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_資材ﾛｯﾄNo.3_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO6_SIZAILOTNO3_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO6_SIZAILOTNO3_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_調合量3_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO6_TYOUGOURYOU3_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO6_TYOUGOURYOU3_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_材料品名4
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO6_ZAIRYOHINMEI4, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO6_ZAIRYOHINMEI4, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_調合量規格4
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU4, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU4, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_資材ﾛｯﾄNo.4_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO6_SIZAILOTNO4_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO6_SIZAILOTNO4_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_調合量4_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO6_TYOUGOURYOU4_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO6_TYOUGOURYOU4_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_資材ﾛｯﾄNo.4_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO6_SIZAILOTNO4_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO6_SIZAILOTNO4_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_調合量4_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO6_TYOUGOURYOU4_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO6_TYOUGOURYOU4_2, srGlassslurryhyoryo));

    }

    /**
     * ﾎﾟｯﾄ7データ設定処理
     *
     * @param processData 処理制御データ
     * @param srGlassslurryhyoryo ｶﾞﾗｽｽﾗﾘｰ作製・秤量データ
     */
    private void setInputItemDataFormH(ProcessData processData, SrGlassslurryhyoryo srGlassslurryhyoryo) {
        // ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_材料品名1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO7_ZAIRYOHINMEI1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO7_ZAIRYOHINMEI1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_調合量規格1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_資材ﾛｯﾄNo.1_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO7_SIZAILOTNO1_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO7_SIZAILOTNO1_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_調合量1_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO7_TYOUGOURYOU1_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO7_TYOUGOURYOU1_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_資材ﾛｯﾄNo.1_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO7_SIZAILOTNO1_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO7_SIZAILOTNO1_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_調合量1_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO7_TYOUGOURYOU1_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO7_TYOUGOURYOU1_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_材料品名2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO7_ZAIRYOHINMEI2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO7_ZAIRYOHINMEI2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_調合量規格2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_資材ﾛｯﾄNo.2_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO7_SIZAILOTNO2_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO7_SIZAILOTNO2_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_調合量2_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO7_TYOUGOURYOU2_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO7_TYOUGOURYOU2_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_資材ﾛｯﾄNo.2_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO7_SIZAILOTNO2_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO7_SIZAILOTNO2_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_調合量2_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO7_TYOUGOURYOU2_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO7_TYOUGOURYOU2_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_材料品名3
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO7_ZAIRYOHINMEI3, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO7_ZAIRYOHINMEI3, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_調合量規格3
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU3, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU3, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_資材ﾛｯﾄNo.3_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO7_SIZAILOTNO3_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO7_SIZAILOTNO3_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_調合量3_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO7_TYOUGOURYOU3_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO7_TYOUGOURYOU3_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_資材ﾛｯﾄNo.3_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO7_SIZAILOTNO3_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO7_SIZAILOTNO3_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_調合量3_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO7_TYOUGOURYOU3_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO7_TYOUGOURYOU3_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_材料品名4
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO7_ZAIRYOHINMEI4, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO7_ZAIRYOHINMEI4, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_調合量規格4
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU4, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU4, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_資材ﾛｯﾄNo.4_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO7_SIZAILOTNO4_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO7_SIZAILOTNO4_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_調合量4_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO7_TYOUGOURYOU4_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO7_TYOUGOURYOU4_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_資材ﾛｯﾄNo.4_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO7_SIZAILOTNO4_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO7_SIZAILOTNO4_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_調合量4_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO7_TYOUGOURYOU4_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO7_TYOUGOURYOU4_2, srGlassslurryhyoryo));

    }

    /**
     * ﾎﾟｯﾄ8データ設定処理
     *
     * @param processData 処理制御データ
     * @param srGlassslurryhyoryo ｶﾞﾗｽｽﾗﾘｰ作製・秤量データ
     */
    private void setInputItemDataFormI(ProcessData processData, SrGlassslurryhyoryo srGlassslurryhyoryo) {
        // ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_材料品名1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO8_ZAIRYOHINMEI1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO8_ZAIRYOHINMEI1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_調合量規格1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_資材ﾛｯﾄNo.1_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO8_SIZAILOTNO1_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO8_SIZAILOTNO1_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_調合量1_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO8_TYOUGOURYOU1_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO8_TYOUGOURYOU1_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_資材ﾛｯﾄNo.1_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO8_SIZAILOTNO1_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO8_SIZAILOTNO1_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_調合量1_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO8_TYOUGOURYOU1_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO8_TYOUGOURYOU1_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_材料品名2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO8_ZAIRYOHINMEI2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO8_ZAIRYOHINMEI2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_調合量規格2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_資材ﾛｯﾄNo.2_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO8_SIZAILOTNO2_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO8_SIZAILOTNO2_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_調合量2_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO8_TYOUGOURYOU2_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO8_TYOUGOURYOU2_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_資材ﾛｯﾄNo.2_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO8_SIZAILOTNO2_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO8_SIZAILOTNO2_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_調合量2_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO8_TYOUGOURYOU2_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO8_TYOUGOURYOU2_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_材料品名3
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO8_ZAIRYOHINMEI3, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO8_ZAIRYOHINMEI3, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_調合量規格3
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU3, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU3, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_資材ﾛｯﾄNo.3_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO8_SIZAILOTNO3_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO8_SIZAILOTNO3_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_調合量3_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO8_TYOUGOURYOU3_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO8_TYOUGOURYOU3_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_資材ﾛｯﾄNo.3_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO8_SIZAILOTNO3_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO8_SIZAILOTNO3_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_調合量3_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO8_TYOUGOURYOU3_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO8_TYOUGOURYOU3_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_材料品名4
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO8_ZAIRYOHINMEI4, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO8_ZAIRYOHINMEI4, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_調合量規格4
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU4, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU4, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_資材ﾛｯﾄNo.4_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO8_SIZAILOTNO4_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO8_SIZAILOTNO4_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_調合量4_1
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO8_TYOUGOURYOU4_1, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO8_TYOUGOURYOU4_1, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_資材ﾛｯﾄNo.4_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO8_SIZAILOTNO4_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO8_SIZAILOTNO4_2, srGlassslurryhyoryo));
        // ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_調合量4_2
        this.setItemDataEx(processData, GXHDO102B005Const.POTTO8_TYOUGOURYOU4_2, getGlassslurryhyoryoItemData(GXHDO102B005Const.POTTO8_TYOUGOURYOU4_2, srGlassslurryhyoryo));

    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ作製・秤量の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return ｶﾞﾗｽｽﾗﾘｰ作製・秤量データ
     * @throws SQLException 例外エラー
     */
    private List<SrGlassslurryhyoryo> getSrGlassslurryhyoryoData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrGlassslurryhyoryo(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSrGlassslurryhyoryo(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
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
        SikakariJson sikakariObj = null;
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
     * [ｶﾞﾗｽｽﾗﾘｰ作製・秤量]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrGlassslurryhyoryo> loadSrGlassslurryhyoryo(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + "kojyo,lotno,edaban,glassslurryhinmei,glassslurrylotno,lotkubun,fusaipottosize,tamaishikei,goki,potto1_zairyohinmei1,"
                + "potto1_tyogouryoukikaku1,potto1_sizailotno1_1,potto1_tyougouryou1_1,potto1_sizailotno1_2,potto1_tyougouryou1_2,potto1_zairyohinmei2,"
                + "potto1_tyogouryoukikaku2,potto1_sizailotno2_1,potto1_tyougouryou2_1,potto1_sizailotno2_2,potto1_tyougouryou2_2,potto1_zairyohinmei3,"
                + "potto1_tyogouryoukikaku3,potto1_sizailotno3_1,potto1_tyougouryou3_1,potto1_sizailotno3_2,potto1_tyougouryou3_2,potto1_zairyohinmei4,"
                + "potto1_tyogouryoukikaku4,potto1_sizailotno4_1,potto1_tyougouryou4_1,potto1_sizailotno4_2,potto1_tyougouryou4_2,potto2_zairyohinmei1,"
                + "potto2_tyogouryoukikaku1,potto2_sizailotno1_1,potto2_tyougouryou1_1,potto2_sizailotno1_2,potto2_tyougouryou1_2,potto2_zairyohinmei2,"
                + "potto2_tyogouryoukikaku2,potto2_sizailotno2_1,potto2_tyougouryou2_1,potto2_sizailotno2_2,potto2_tyougouryou2_2,potto2_zairyohinmei3,"
                + "potto2_tyogouryoukikaku3,potto2_sizailotno3_1,potto2_tyougouryou3_1,potto2_sizailotno3_2,potto2_tyougouryou3_2,potto2_zairyohinmei4,"
                + "potto2_tyogouryoukikaku4,potto2_sizailotno4_1,potto2_tyougouryou4_1,potto2_sizailotno4_2,potto2_tyougouryou4_2,potto3_zairyohinmei1,"
                + "potto3_tyogouryoukikaku1,potto3_sizailotno1_1,potto3_tyougouryou1_1,potto3_sizailotno1_2,potto3_tyougouryou1_2,potto3_zairyohinmei2,"
                + "potto3_tyogouryoukikaku2,potto3_sizailotno2_1,potto3_tyougouryou2_1,potto3_sizailotno2_2,potto3_tyougouryou2_2,potto3_zairyohinmei3,"
                + "potto3_tyogouryoukikaku3,potto3_sizailotno3_1,potto3_tyougouryou3_1,potto3_sizailotno3_2,potto3_tyougouryou3_2,potto3_zairyohinmei4,"
                + "potto3_tyogouryoukikaku4,potto3_sizailotno4_1,potto3_tyougouryou4_1,potto3_sizailotno4_2,potto3_tyougouryou4_2,potto4_zairyohinmei1,"
                + "potto4_tyogouryoukikaku1,potto4_sizailotno1_1,potto4_tyougouryou1_1,potto4_sizailotno1_2,potto4_tyougouryou1_2,potto4_zairyohinmei2,"
                + "potto4_tyogouryoukikaku2,potto4_sizailotno2_1,potto4_tyougouryou2_1,potto4_sizailotno2_2,potto4_tyougouryou2_2,potto4_zairyohinmei3,"
                + "potto4_tyogouryoukikaku3,potto4_sizailotno3_1,potto4_tyougouryou3_1,potto4_sizailotno3_2,potto4_tyougouryou3_2,potto4_zairyohinmei4,"
                + "potto4_tyogouryoukikaku4,potto4_sizailotno4_1,potto4_tyougouryou4_1,potto4_sizailotno4_2,potto4_tyougouryou4_2,potto5_zairyohinmei1,"
                + "potto5_tyogouryoukikaku1,potto5_sizailotno1_1,potto5_tyougouryou1_1,potto5_sizailotno1_2,potto5_tyougouryou1_2,potto5_zairyohinmei2,"
                + "potto5_tyogouryoukikaku2,potto5_sizailotno2_1,potto5_tyougouryou2_1,potto5_sizailotno2_2,potto5_tyougouryou2_2,potto5_zairyohinmei3,"
                + "potto5_tyogouryoukikaku3,potto5_sizailotno3_1,potto5_tyougouryou3_1,potto5_sizailotno3_2,potto5_tyougouryou3_2,potto5_zairyohinmei4,"
                + "potto5_tyogouryoukikaku4,potto5_sizailotno4_1,potto5_tyougouryou4_1,potto5_sizailotno4_2,potto5_tyougouryou4_2,potto6_zairyohinmei1,"
                + "potto6_tyogouryoukikaku1,potto6_sizailotno1_1,potto6_tyougouryou1_1,potto6_sizailotno1_2,potto6_tyougouryou1_2,potto6_zairyohinmei2,"
                + "potto6_tyogouryoukikaku2,potto6_sizailotno2_1,potto6_tyougouryou2_1,potto6_sizailotno2_2,potto6_tyougouryou2_2,potto6_zairyohinmei3,"
                + "potto6_tyogouryoukikaku3,potto6_sizailotno3_1,potto6_tyougouryou3_1,potto6_sizailotno3_2,potto6_tyougouryou3_2,potto6_zairyohinmei4,"
                + "potto6_tyogouryoukikaku4,potto6_sizailotno4_1,potto6_tyougouryou4_1,potto6_sizailotno4_2,potto6_tyougouryou4_2,potto7_zairyohinmei1,"
                + "potto7_tyogouryoukikaku1,potto7_sizailotno1_1,potto7_tyougouryou1_1,potto7_sizailotno1_2,potto7_tyougouryou1_2,potto7_zairyohinmei2,"
                + "potto7_tyogouryoukikaku2,potto7_sizailotno2_1,potto7_tyougouryou2_1,potto7_sizailotno2_2,potto7_tyougouryou2_2,potto7_zairyohinmei3,"
                + "potto7_tyogouryoukikaku3,potto7_sizailotno3_1,potto7_tyougouryou3_1,potto7_sizailotno3_2,potto7_tyougouryou3_2,potto7_zairyohinmei4,"
                + "potto7_tyogouryoukikaku4,potto7_sizailotno4_1,potto7_tyougouryou4_1,potto7_sizailotno4_2,potto7_tyougouryou4_2,potto8_zairyohinmei1,"
                + "potto8_tyogouryoukikaku1,potto8_sizailotno1_1,potto8_tyougouryou1_1,potto8_sizailotno1_2,potto8_tyougouryou1_2,potto8_zairyohinmei2,"
                + "potto8_tyogouryoukikaku2,potto8_sizailotno2_1,potto8_tyougouryou2_1,potto8_sizailotno2_2,potto8_tyougouryou2_2,potto8_zairyohinmei3,"
                + "potto8_tyogouryoukikaku3,potto8_sizailotno3_1,potto8_tyougouryou3_1,potto8_sizailotno3_2,potto8_tyougouryou3_2,potto8_zairyohinmei4,"
                + "potto8_tyogouryoukikaku4,potto8_sizailotno4_1,potto8_tyougouryou4_1,potto8_sizailotno4_2,potto8_tyougouryou4_2,hyouryounichiji,tantousya,"
                + "kakuninsya,bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + " FROM sr_glassslurryhyoryo "
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
        mapping.put("kojyo", "kojyo");                                           // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                           // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                         // 枝番
        mapping.put("glassslurryhinmei", "glassslurryhinmei");                   // ｶﾞﾗｽｽﾗﾘｰ品名
        mapping.put("glassslurrylotno", "glassslurrylotno");                     // ｶﾞﾗｽｽﾗﾘｰ品名LotNo
        mapping.put("lotkubun", "lotkubun");                                     // ﾛｯﾄ区分
        mapping.put("fusaipottosize", "fusaipottosize");                         // 粉砕ﾎﾟｯﾄｻｲｽﾞ
        mapping.put("tamaishikei", "tamaishikei");                               // 玉石径
        mapping.put("goki", "goki");                                             // 秤量号機
        mapping.put("potto1_zairyohinmei1", "potto1_zairyohinmei1");             // ﾎﾟｯﾄ1_材料品名1
        mapping.put("potto1_tyogouryoukikaku1", "potto1_tyogouryoukikaku1");     // ﾎﾟｯﾄ1_調合量規格1
        mapping.put("potto1_sizailotno1_1", "potto1_sizailotno1_1");             // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.1_1
        mapping.put("potto1_tyougouryou1_1", "potto1_tyougouryou1_1");           // ﾎﾟｯﾄ1_調合量1_1
        mapping.put("potto1_sizailotno1_2", "potto1_sizailotno1_2");             // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.1_2
        mapping.put("potto1_tyougouryou1_2", "potto1_tyougouryou1_2");           // ﾎﾟｯﾄ1_調合量1_2
        mapping.put("potto1_zairyohinmei2", "potto1_zairyohinmei2");             // ﾎﾟｯﾄ1_材料品名2
        mapping.put("potto1_tyogouryoukikaku2", "potto1_tyogouryoukikaku2");     // ﾎﾟｯﾄ1_調合量規格2
        mapping.put("potto1_sizailotno2_1", "potto1_sizailotno2_1");             // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.2_1
        mapping.put("potto1_tyougouryou2_1", "potto1_tyougouryou2_1");           // ﾎﾟｯﾄ1_調合量2_1
        mapping.put("potto1_sizailotno2_2", "potto1_sizailotno2_2");             // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.2_2
        mapping.put("potto1_tyougouryou2_2", "potto1_tyougouryou2_2");           // ﾎﾟｯﾄ1_調合量2_2
        mapping.put("potto1_zairyohinmei3", "potto1_zairyohinmei3");             // ﾎﾟｯﾄ1_材料品名3
        mapping.put("potto1_tyogouryoukikaku3", "potto1_tyogouryoukikaku3");     // ﾎﾟｯﾄ1_調合量規格3
        mapping.put("potto1_sizailotno3_1", "potto1_sizailotno3_1");             // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.3_1
        mapping.put("potto1_tyougouryou3_1", "potto1_tyougouryou3_1");           // ﾎﾟｯﾄ1_調合量3_1
        mapping.put("potto1_sizailotno3_2", "potto1_sizailotno3_2");             // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.3_2
        mapping.put("potto1_tyougouryou3_2", "potto1_tyougouryou3_2");           // ﾎﾟｯﾄ1_調合量3_2
        mapping.put("potto1_zairyohinmei4", "potto1_zairyohinmei4");             // ﾎﾟｯﾄ1_材料品名4
        mapping.put("potto1_tyogouryoukikaku4", "potto1_tyogouryoukikaku4");     // ﾎﾟｯﾄ1_調合量規格4
        mapping.put("potto1_sizailotno4_1", "potto1_sizailotno4_1");             // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.4_1
        mapping.put("potto1_tyougouryou4_1", "potto1_tyougouryou4_1");           // ﾎﾟｯﾄ1_調合量4_1
        mapping.put("potto1_sizailotno4_2", "potto1_sizailotno4_2");             // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.4_2
        mapping.put("potto1_tyougouryou4_2", "potto1_tyougouryou4_2");           // ﾎﾟｯﾄ1_調合量4_2
        mapping.put("potto2_zairyohinmei1", "potto2_zairyohinmei1");             // ﾎﾟｯﾄ2_材料品名1
        mapping.put("potto2_tyogouryoukikaku1", "potto2_tyogouryoukikaku1");     // ﾎﾟｯﾄ2_調合量規格1
        mapping.put("potto2_sizailotno1_1", "potto2_sizailotno1_1");             // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.1_1
        mapping.put("potto2_tyougouryou1_1", "potto2_tyougouryou1_1");           // ﾎﾟｯﾄ2_調合量1_1
        mapping.put("potto2_sizailotno1_2", "potto2_sizailotno1_2");             // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.1_2
        mapping.put("potto2_tyougouryou1_2", "potto2_tyougouryou1_2");           // ﾎﾟｯﾄ2_調合量1_2
        mapping.put("potto2_zairyohinmei2", "potto2_zairyohinmei2");             // ﾎﾟｯﾄ2_材料品名2
        mapping.put("potto2_tyogouryoukikaku2", "potto2_tyogouryoukikaku2");     // ﾎﾟｯﾄ2_調合量規格2
        mapping.put("potto2_sizailotno2_1", "potto2_sizailotno2_1");             // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.2_1
        mapping.put("potto2_tyougouryou2_1", "potto2_tyougouryou2_1");           // ﾎﾟｯﾄ2_調合量2_1
        mapping.put("potto2_sizailotno2_2", "potto2_sizailotno2_2");             // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.2_2
        mapping.put("potto2_tyougouryou2_2", "potto2_tyougouryou2_2");           // ﾎﾟｯﾄ2_調合量2_2
        mapping.put("potto2_zairyohinmei3", "potto2_zairyohinmei3");             // ﾎﾟｯﾄ2_材料品名3
        mapping.put("potto2_tyogouryoukikaku3", "potto2_tyogouryoukikaku3");     // ﾎﾟｯﾄ2_調合量規格3
        mapping.put("potto2_sizailotno3_1", "potto2_sizailotno3_1");             // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.3_1
        mapping.put("potto2_tyougouryou3_1", "potto2_tyougouryou3_1");           // ﾎﾟｯﾄ2_調合量3_1
        mapping.put("potto2_sizailotno3_2", "potto2_sizailotno3_2");             // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.3_2
        mapping.put("potto2_tyougouryou3_2", "potto2_tyougouryou3_2");           // ﾎﾟｯﾄ2_調合量3_2
        mapping.put("potto2_zairyohinmei4", "potto2_zairyohinmei4");             // ﾎﾟｯﾄ2_材料品名4
        mapping.put("potto2_tyogouryoukikaku4", "potto2_tyogouryoukikaku4");     // ﾎﾟｯﾄ2_調合量規格4
        mapping.put("potto2_sizailotno4_1", "potto2_sizailotno4_1");             // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.4_1
        mapping.put("potto2_tyougouryou4_1", "potto2_tyougouryou4_1");           // ﾎﾟｯﾄ2_調合量4_1
        mapping.put("potto2_sizailotno4_2", "potto2_sizailotno4_2");             // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.4_2
        mapping.put("potto2_tyougouryou4_2", "potto2_tyougouryou4_2");           // ﾎﾟｯﾄ2_調合量4_2
        mapping.put("potto3_zairyohinmei1", "potto3_zairyohinmei1");             // ﾎﾟｯﾄ3_材料品名1
        mapping.put("potto3_tyogouryoukikaku1", "potto3_tyogouryoukikaku1");     // ﾎﾟｯﾄ3_調合量規格1
        mapping.put("potto3_sizailotno1_1", "potto3_sizailotno1_1");             // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.1_1
        mapping.put("potto3_tyougouryou1_1", "potto3_tyougouryou1_1");           // ﾎﾟｯﾄ3_調合量1_1
        mapping.put("potto3_sizailotno1_2", "potto3_sizailotno1_2");             // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.1_2
        mapping.put("potto3_tyougouryou1_2", "potto3_tyougouryou1_2");           // ﾎﾟｯﾄ3_調合量1_2
        mapping.put("potto3_zairyohinmei2", "potto3_zairyohinmei2");             // ﾎﾟｯﾄ3_材料品名2
        mapping.put("potto3_tyogouryoukikaku2", "potto3_tyogouryoukikaku2");     // ﾎﾟｯﾄ3_調合量規格2
        mapping.put("potto3_sizailotno2_1", "potto3_sizailotno2_1");             // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.2_1
        mapping.put("potto3_tyougouryou2_1", "potto3_tyougouryou2_1");           // ﾎﾟｯﾄ3_調合量2_1
        mapping.put("potto3_sizailotno2_2", "potto3_sizailotno2_2");             // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.2_2
        mapping.put("potto3_tyougouryou2_2", "potto3_tyougouryou2_2");           // ﾎﾟｯﾄ3_調合量2_2
        mapping.put("potto3_zairyohinmei3", "potto3_zairyohinmei3");             // ﾎﾟｯﾄ3_材料品名3
        mapping.put("potto3_tyogouryoukikaku3", "potto3_tyogouryoukikaku3");     // ﾎﾟｯﾄ3_調合量規格3
        mapping.put("potto3_sizailotno3_1", "potto3_sizailotno3_1");             // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.3_1
        mapping.put("potto3_tyougouryou3_1", "potto3_tyougouryou3_1");           // ﾎﾟｯﾄ3_調合量3_1
        mapping.put("potto3_sizailotno3_2", "potto3_sizailotno3_2");             // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.3_2
        mapping.put("potto3_tyougouryou3_2", "potto3_tyougouryou3_2");           // ﾎﾟｯﾄ3_調合量3_2
        mapping.put("potto3_zairyohinmei4", "potto3_zairyohinmei4");             // ﾎﾟｯﾄ3_材料品名4
        mapping.put("potto3_tyogouryoukikaku4", "potto3_tyogouryoukikaku4");     // ﾎﾟｯﾄ3_調合量規格4
        mapping.put("potto3_sizailotno4_1", "potto3_sizailotno4_1");             // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.4_1
        mapping.put("potto3_tyougouryou4_1", "potto3_tyougouryou4_1");           // ﾎﾟｯﾄ3_調合量4_1
        mapping.put("potto3_sizailotno4_2", "potto3_sizailotno4_2");             // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.4_2
        mapping.put("potto3_tyougouryou4_2", "potto3_tyougouryou4_2");           // ﾎﾟｯﾄ3_調合量4_2
        mapping.put("potto4_zairyohinmei1", "potto4_zairyohinmei1");             // ﾎﾟｯﾄ4_材料品名1
        mapping.put("potto4_tyogouryoukikaku1", "potto4_tyogouryoukikaku1");     // ﾎﾟｯﾄ4_調合量規格1
        mapping.put("potto4_sizailotno1_1", "potto4_sizailotno1_1");             // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.1_1
        mapping.put("potto4_tyougouryou1_1", "potto4_tyougouryou1_1");           // ﾎﾟｯﾄ4_調合量1_1
        mapping.put("potto4_sizailotno1_2", "potto4_sizailotno1_2");             // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.1_2
        mapping.put("potto4_tyougouryou1_2", "potto4_tyougouryou1_2");           // ﾎﾟｯﾄ4_調合量1_2
        mapping.put("potto4_zairyohinmei2", "potto4_zairyohinmei2");             // ﾎﾟｯﾄ4_材料品名2
        mapping.put("potto4_tyogouryoukikaku2", "potto4_tyogouryoukikaku2");     // ﾎﾟｯﾄ4_調合量規格2
        mapping.put("potto4_sizailotno2_1", "potto4_sizailotno2_1");             // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.2_1
        mapping.put("potto4_tyougouryou2_1", "potto4_tyougouryou2_1");           // ﾎﾟｯﾄ4_調合量2_1
        mapping.put("potto4_sizailotno2_2", "potto4_sizailotno2_2");             // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.2_2
        mapping.put("potto4_tyougouryou2_2", "potto4_tyougouryou2_2");           // ﾎﾟｯﾄ4_調合量2_2
        mapping.put("potto4_zairyohinmei3", "potto4_zairyohinmei3");             // ﾎﾟｯﾄ4_材料品名3
        mapping.put("potto4_tyogouryoukikaku3", "potto4_tyogouryoukikaku3");     // ﾎﾟｯﾄ4_調合量規格3
        mapping.put("potto4_sizailotno3_1", "potto4_sizailotno3_1");             // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.3_1
        mapping.put("potto4_tyougouryou3_1", "potto4_tyougouryou3_1");           // ﾎﾟｯﾄ4_調合量3_1
        mapping.put("potto4_sizailotno3_2", "potto4_sizailotno3_2");             // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.3_2
        mapping.put("potto4_tyougouryou3_2", "potto4_tyougouryou3_2");           // ﾎﾟｯﾄ4_調合量3_2
        mapping.put("potto4_zairyohinmei4", "potto4_zairyohinmei4");             // ﾎﾟｯﾄ4_材料品名4
        mapping.put("potto4_tyogouryoukikaku4", "potto4_tyogouryoukikaku4");     // ﾎﾟｯﾄ4_調合量規格4
        mapping.put("potto4_sizailotno4_1", "potto4_sizailotno4_1");             // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.4_1
        mapping.put("potto4_tyougouryou4_1", "potto4_tyougouryou4_1");           // ﾎﾟｯﾄ4_調合量4_1
        mapping.put("potto4_sizailotno4_2", "potto4_sizailotno4_2");             // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.4_2
        mapping.put("potto4_tyougouryou4_2", "potto4_tyougouryou4_2");           // ﾎﾟｯﾄ4_調合量4_2
        mapping.put("potto5_zairyohinmei1", "potto5_zairyohinmei1");             // ﾎﾟｯﾄ5_材料品名1
        mapping.put("potto5_tyogouryoukikaku1", "potto5_tyogouryoukikaku1");     // ﾎﾟｯﾄ5_調合量規格1
        mapping.put("potto5_sizailotno1_1", "potto5_sizailotno1_1");             // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.1_1
        mapping.put("potto5_tyougouryou1_1", "potto5_tyougouryou1_1");           // ﾎﾟｯﾄ5_調合量1_1
        mapping.put("potto5_sizailotno1_2", "potto5_sizailotno1_2");             // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.1_2
        mapping.put("potto5_tyougouryou1_2", "potto5_tyougouryou1_2");           // ﾎﾟｯﾄ5_調合量1_2
        mapping.put("potto5_zairyohinmei2", "potto5_zairyohinmei2");             // ﾎﾟｯﾄ5_材料品名2
        mapping.put("potto5_tyogouryoukikaku2", "potto5_tyogouryoukikaku2");     // ﾎﾟｯﾄ5_調合量規格2
        mapping.put("potto5_sizailotno2_1", "potto5_sizailotno2_1");             // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.2_1
        mapping.put("potto5_tyougouryou2_1", "potto5_tyougouryou2_1");           // ﾎﾟｯﾄ5_調合量2_1
        mapping.put("potto5_sizailotno2_2", "potto5_sizailotno2_2");             // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.2_2
        mapping.put("potto5_tyougouryou2_2", "potto5_tyougouryou2_2");           // ﾎﾟｯﾄ5_調合量2_2
        mapping.put("potto5_zairyohinmei3", "potto5_zairyohinmei3");             // ﾎﾟｯﾄ5_材料品名3
        mapping.put("potto5_tyogouryoukikaku3", "potto5_tyogouryoukikaku3");     // ﾎﾟｯﾄ5_調合量規格3
        mapping.put("potto5_sizailotno3_1", "potto5_sizailotno3_1");             // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.3_1
        mapping.put("potto5_tyougouryou3_1", "potto5_tyougouryou3_1");           // ﾎﾟｯﾄ5_調合量3_1
        mapping.put("potto5_sizailotno3_2", "potto5_sizailotno3_2");             // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.3_2
        mapping.put("potto5_tyougouryou3_2", "potto5_tyougouryou3_2");           // ﾎﾟｯﾄ5_調合量3_2
        mapping.put("potto5_zairyohinmei4", "potto5_zairyohinmei4");             // ﾎﾟｯﾄ5_材料品名4
        mapping.put("potto5_tyogouryoukikaku4", "potto5_tyogouryoukikaku4");     // ﾎﾟｯﾄ5_調合量規格4
        mapping.put("potto5_sizailotno4_1", "potto5_sizailotno4_1");             // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.4_1
        mapping.put("potto5_tyougouryou4_1", "potto5_tyougouryou4_1");           // ﾎﾟｯﾄ5_調合量4_1
        mapping.put("potto5_sizailotno4_2", "potto5_sizailotno4_2");             // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.4_2
        mapping.put("potto5_tyougouryou4_2", "potto5_tyougouryou4_2");           // ﾎﾟｯﾄ5_調合量4_2
        mapping.put("potto6_zairyohinmei1", "potto6_zairyohinmei1");             // ﾎﾟｯﾄ6_材料品名1
        mapping.put("potto6_tyogouryoukikaku1", "potto6_tyogouryoukikaku1");     // ﾎﾟｯﾄ6_調合量規格1
        mapping.put("potto6_sizailotno1_1", "potto6_sizailotno1_1");             // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.1_1
        mapping.put("potto6_tyougouryou1_1", "potto6_tyougouryou1_1");           // ﾎﾟｯﾄ6_調合量1_1
        mapping.put("potto6_sizailotno1_2", "potto6_sizailotno1_2");             // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.1_2
        mapping.put("potto6_tyougouryou1_2", "potto6_tyougouryou1_2");           // ﾎﾟｯﾄ6_調合量1_2
        mapping.put("potto6_zairyohinmei2", "potto6_zairyohinmei2");             // ﾎﾟｯﾄ6_材料品名2
        mapping.put("potto6_tyogouryoukikaku2", "potto6_tyogouryoukikaku2");     // ﾎﾟｯﾄ6_調合量規格2
        mapping.put("potto6_sizailotno2_1", "potto6_sizailotno2_1");             // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.2_1
        mapping.put("potto6_tyougouryou2_1", "potto6_tyougouryou2_1");           // ﾎﾟｯﾄ6_調合量2_1
        mapping.put("potto6_sizailotno2_2", "potto6_sizailotno2_2");             // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.2_2
        mapping.put("potto6_tyougouryou2_2", "potto6_tyougouryou2_2");           // ﾎﾟｯﾄ6_調合量2_2
        mapping.put("potto6_zairyohinmei3", "potto6_zairyohinmei3");             // ﾎﾟｯﾄ6_材料品名3
        mapping.put("potto6_tyogouryoukikaku3", "potto6_tyogouryoukikaku3");     // ﾎﾟｯﾄ6_調合量規格3
        mapping.put("potto6_sizailotno3_1", "potto6_sizailotno3_1");             // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.3_1
        mapping.put("potto6_tyougouryou3_1", "potto6_tyougouryou3_1");           // ﾎﾟｯﾄ6_調合量3_1
        mapping.put("potto6_sizailotno3_2", "potto6_sizailotno3_2");             // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.3_2
        mapping.put("potto6_tyougouryou3_2", "potto6_tyougouryou3_2");           // ﾎﾟｯﾄ6_調合量3_2
        mapping.put("potto6_zairyohinmei4", "potto6_zairyohinmei4");             // ﾎﾟｯﾄ6_材料品名4
        mapping.put("potto6_tyogouryoukikaku4", "potto6_tyogouryoukikaku4");     // ﾎﾟｯﾄ6_調合量規格4
        mapping.put("potto6_sizailotno4_1", "potto6_sizailotno4_1");             // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.4_1
        mapping.put("potto6_tyougouryou4_1", "potto6_tyougouryou4_1");           // ﾎﾟｯﾄ6_調合量4_1
        mapping.put("potto6_sizailotno4_2", "potto6_sizailotno4_2");             // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.4_2
        mapping.put("potto6_tyougouryou4_2", "potto6_tyougouryou4_2");           // ﾎﾟｯﾄ6_調合量4_2
        mapping.put("potto7_zairyohinmei1", "potto7_zairyohinmei1");             // ﾎﾟｯﾄ7_材料品名1
        mapping.put("potto7_tyogouryoukikaku1", "potto7_tyogouryoukikaku1");     // ﾎﾟｯﾄ7_調合量規格1
        mapping.put("potto7_sizailotno1_1", "potto7_sizailotno1_1");             // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.1_1
        mapping.put("potto7_tyougouryou1_1", "potto7_tyougouryou1_1");           // ﾎﾟｯﾄ7_調合量1_1
        mapping.put("potto7_sizailotno1_2", "potto7_sizailotno1_2");             // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.1_2
        mapping.put("potto7_tyougouryou1_2", "potto7_tyougouryou1_2");           // ﾎﾟｯﾄ7_調合量1_2
        mapping.put("potto7_zairyohinmei2", "potto7_zairyohinmei2");             // ﾎﾟｯﾄ7_材料品名2
        mapping.put("potto7_tyogouryoukikaku2", "potto7_tyogouryoukikaku2");     // ﾎﾟｯﾄ7_調合量規格2
        mapping.put("potto7_sizailotno2_1", "potto7_sizailotno2_1");             // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.2_1
        mapping.put("potto7_tyougouryou2_1", "potto7_tyougouryou2_1");           // ﾎﾟｯﾄ7_調合量2_1
        mapping.put("potto7_sizailotno2_2", "potto7_sizailotno2_2");             // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.2_2
        mapping.put("potto7_tyougouryou2_2", "potto7_tyougouryou2_2");           // ﾎﾟｯﾄ7_調合量2_2
        mapping.put("potto7_zairyohinmei3", "potto7_zairyohinmei3");             // ﾎﾟｯﾄ7_材料品名3
        mapping.put("potto7_tyogouryoukikaku3", "potto7_tyogouryoukikaku3");     // ﾎﾟｯﾄ7_調合量規格3
        mapping.put("potto7_sizailotno3_1", "potto7_sizailotno3_1");             // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.3_1
        mapping.put("potto7_tyougouryou3_1", "potto7_tyougouryou3_1");           // ﾎﾟｯﾄ7_調合量3_1
        mapping.put("potto7_sizailotno3_2", "potto7_sizailotno3_2");             // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.3_2
        mapping.put("potto7_tyougouryou3_2", "potto7_tyougouryou3_2");           // ﾎﾟｯﾄ7_調合量3_2
        mapping.put("potto7_zairyohinmei4", "potto7_zairyohinmei4");             // ﾎﾟｯﾄ7_材料品名4
        mapping.put("potto7_tyogouryoukikaku4", "potto7_tyogouryoukikaku4");     // ﾎﾟｯﾄ7_調合量規格4
        mapping.put("potto7_sizailotno4_1", "potto7_sizailotno4_1");             // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.4_1
        mapping.put("potto7_tyougouryou4_1", "potto7_tyougouryou4_1");           // ﾎﾟｯﾄ7_調合量4_1
        mapping.put("potto7_sizailotno4_2", "potto7_sizailotno4_2");             // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.4_2
        mapping.put("potto7_tyougouryou4_2", "potto7_tyougouryou4_2");           // ﾎﾟｯﾄ7_調合量4_2
        mapping.put("potto8_zairyohinmei1", "potto8_zairyohinmei1");             // ﾎﾟｯﾄ8_材料品名1
        mapping.put("potto8_tyogouryoukikaku1", "potto8_tyogouryoukikaku1");     // ﾎﾟｯﾄ8_調合量規格1
        mapping.put("potto8_sizailotno1_1", "potto8_sizailotno1_1");             // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.1_1
        mapping.put("potto8_tyougouryou1_1", "potto8_tyougouryou1_1");           // ﾎﾟｯﾄ8_調合量1_1
        mapping.put("potto8_sizailotno1_2", "potto8_sizailotno1_2");             // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.1_2
        mapping.put("potto8_tyougouryou1_2", "potto8_tyougouryou1_2");           // ﾎﾟｯﾄ8_調合量1_2
        mapping.put("potto8_zairyohinmei2", "potto8_zairyohinmei2");             // ﾎﾟｯﾄ8_材料品名2
        mapping.put("potto8_tyogouryoukikaku2", "potto8_tyogouryoukikaku2");     // ﾎﾟｯﾄ8_調合量規格2
        mapping.put("potto8_sizailotno2_1", "potto8_sizailotno2_1");             // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.2_1
        mapping.put("potto8_tyougouryou2_1", "potto8_tyougouryou2_1");           // ﾎﾟｯﾄ8_調合量2_1
        mapping.put("potto8_sizailotno2_2", "potto8_sizailotno2_2");             // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.2_2
        mapping.put("potto8_tyougouryou2_2", "potto8_tyougouryou2_2");           // ﾎﾟｯﾄ8_調合量2_2
        mapping.put("potto8_zairyohinmei3", "potto8_zairyohinmei3");             // ﾎﾟｯﾄ8_材料品名3
        mapping.put("potto8_tyogouryoukikaku3", "potto8_tyogouryoukikaku3");     // ﾎﾟｯﾄ8_調合量規格3
        mapping.put("potto8_sizailotno3_1", "potto8_sizailotno3_1");             // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.3_1
        mapping.put("potto8_tyougouryou3_1", "potto8_tyougouryou3_1");           // ﾎﾟｯﾄ8_調合量3_1
        mapping.put("potto8_sizailotno3_2", "potto8_sizailotno3_2");             // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.3_2
        mapping.put("potto8_tyougouryou3_2", "potto8_tyougouryou3_2");           // ﾎﾟｯﾄ8_調合量3_2
        mapping.put("potto8_zairyohinmei4", "potto8_zairyohinmei4");             // ﾎﾟｯﾄ8_材料品名4
        mapping.put("potto8_tyogouryoukikaku4", "potto8_tyogouryoukikaku4");     // ﾎﾟｯﾄ8_調合量規格4
        mapping.put("potto8_sizailotno4_1", "potto8_sizailotno4_1");             // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.4_1
        mapping.put("potto8_tyougouryou4_1", "potto8_tyougouryou4_1");           // ﾎﾟｯﾄ8_調合量4_1
        mapping.put("potto8_sizailotno4_2", "potto8_sizailotno4_2");             // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.4_2
        mapping.put("potto8_tyougouryou4_2", "potto8_tyougouryou4_2");           // ﾎﾟｯﾄ8_調合量4_2
        mapping.put("hyouryounichiji", "hyouryounichiji");                       // 秤量日時
        mapping.put("tantousya", "tantousya");                                   // 担当者
        mapping.put("kakuninsya", "kakuninsya");                                 // 確認者
        mapping.put("bikou1", "bikou1");                                         // 備考1
        mapping.put("bikou2", "bikou2");                                         // 備考2
        mapping.put("torokunichiji", "torokunichiji");                           // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                             // 更新日時
        mapping.put("revision", "revision");                                     // revision

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrGlassslurryhyoryo>> beanHandler = new BeanListHandler<>(SrGlassslurryhyoryo.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [ｶﾞﾗｽｽﾗﾘｰ作製・秤量_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrGlassslurryhyoryo> loadTmpSrGlassslurryhyoryo(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + "kojyo,lotno,edaban,glassslurryhinmei,glassslurrylotno,lotkubun,fusaipottosize,tamaishikei,goki,potto1_zairyohinmei1,"
                + "potto1_tyogouryoukikaku1,potto1_sizailotno1_1,potto1_tyougouryou1_1,potto1_sizailotno1_2,potto1_tyougouryou1_2,potto1_zairyohinmei2,"
                + "potto1_tyogouryoukikaku2,potto1_sizailotno2_1,potto1_tyougouryou2_1,potto1_sizailotno2_2,potto1_tyougouryou2_2,potto1_zairyohinmei3,"
                + "potto1_tyogouryoukikaku3,potto1_sizailotno3_1,potto1_tyougouryou3_1,potto1_sizailotno3_2,potto1_tyougouryou3_2,potto1_zairyohinmei4,"
                + "potto1_tyogouryoukikaku4,potto1_sizailotno4_1,potto1_tyougouryou4_1,potto1_sizailotno4_2,potto1_tyougouryou4_2,potto2_zairyohinmei1,"
                + "potto2_tyogouryoukikaku1,potto2_sizailotno1_1,potto2_tyougouryou1_1,potto2_sizailotno1_2,potto2_tyougouryou1_2,potto2_zairyohinmei2,"
                + "potto2_tyogouryoukikaku2,potto2_sizailotno2_1,potto2_tyougouryou2_1,potto2_sizailotno2_2,potto2_tyougouryou2_2,potto2_zairyohinmei3,"
                + "potto2_tyogouryoukikaku3,potto2_sizailotno3_1,potto2_tyougouryou3_1,potto2_sizailotno3_2,potto2_tyougouryou3_2,potto2_zairyohinmei4,"
                + "potto2_tyogouryoukikaku4,potto2_sizailotno4_1,potto2_tyougouryou4_1,potto2_sizailotno4_2,potto2_tyougouryou4_2,potto3_zairyohinmei1,"
                + "potto3_tyogouryoukikaku1,potto3_sizailotno1_1,potto3_tyougouryou1_1,potto3_sizailotno1_2,potto3_tyougouryou1_2,potto3_zairyohinmei2,"
                + "potto3_tyogouryoukikaku2,potto3_sizailotno2_1,potto3_tyougouryou2_1,potto3_sizailotno2_2,potto3_tyougouryou2_2,potto3_zairyohinmei3,"
                + "potto3_tyogouryoukikaku3,potto3_sizailotno3_1,potto3_tyougouryou3_1,potto3_sizailotno3_2,potto3_tyougouryou3_2,potto3_zairyohinmei4,"
                + "potto3_tyogouryoukikaku4,potto3_sizailotno4_1,potto3_tyougouryou4_1,potto3_sizailotno4_2,potto3_tyougouryou4_2,potto4_zairyohinmei1,"
                + "potto4_tyogouryoukikaku1,potto4_sizailotno1_1,potto4_tyougouryou1_1,potto4_sizailotno1_2,potto4_tyougouryou1_2,potto4_zairyohinmei2,"
                + "potto4_tyogouryoukikaku2,potto4_sizailotno2_1,potto4_tyougouryou2_1,potto4_sizailotno2_2,potto4_tyougouryou2_2,potto4_zairyohinmei3,"
                + "potto4_tyogouryoukikaku3,potto4_sizailotno3_1,potto4_tyougouryou3_1,potto4_sizailotno3_2,potto4_tyougouryou3_2,potto4_zairyohinmei4,"
                + "potto4_tyogouryoukikaku4,potto4_sizailotno4_1,potto4_tyougouryou4_1,potto4_sizailotno4_2,potto4_tyougouryou4_2,potto5_zairyohinmei1,"
                + "potto5_tyogouryoukikaku1,potto5_sizailotno1_1,potto5_tyougouryou1_1,potto5_sizailotno1_2,potto5_tyougouryou1_2,potto5_zairyohinmei2,"
                + "potto5_tyogouryoukikaku2,potto5_sizailotno2_1,potto5_tyougouryou2_1,potto5_sizailotno2_2,potto5_tyougouryou2_2,potto5_zairyohinmei3,"
                + "potto5_tyogouryoukikaku3,potto5_sizailotno3_1,potto5_tyougouryou3_1,potto5_sizailotno3_2,potto5_tyougouryou3_2,potto5_zairyohinmei4,"
                + "potto5_tyogouryoukikaku4,potto5_sizailotno4_1,potto5_tyougouryou4_1,potto5_sizailotno4_2,potto5_tyougouryou4_2,potto6_zairyohinmei1,"
                + "potto6_tyogouryoukikaku1,potto6_sizailotno1_1,potto6_tyougouryou1_1,potto6_sizailotno1_2,potto6_tyougouryou1_2,potto6_zairyohinmei2,"
                + "potto6_tyogouryoukikaku2,potto6_sizailotno2_1,potto6_tyougouryou2_1,potto6_sizailotno2_2,potto6_tyougouryou2_2,potto6_zairyohinmei3,"
                + "potto6_tyogouryoukikaku3,potto6_sizailotno3_1,potto6_tyougouryou3_1,potto6_sizailotno3_2,potto6_tyougouryou3_2,potto6_zairyohinmei4,"
                + "potto6_tyogouryoukikaku4,potto6_sizailotno4_1,potto6_tyougouryou4_1,potto6_sizailotno4_2,potto6_tyougouryou4_2,potto7_zairyohinmei1,"
                + "potto7_tyogouryoukikaku1,potto7_sizailotno1_1,potto7_tyougouryou1_1,potto7_sizailotno1_2,potto7_tyougouryou1_2,potto7_zairyohinmei2,"
                + "potto7_tyogouryoukikaku2,potto7_sizailotno2_1,potto7_tyougouryou2_1,potto7_sizailotno2_2,potto7_tyougouryou2_2,potto7_zairyohinmei3,"
                + "potto7_tyogouryoukikaku3,potto7_sizailotno3_1,potto7_tyougouryou3_1,potto7_sizailotno3_2,potto7_tyougouryou3_2,potto7_zairyohinmei4,"
                + "potto7_tyogouryoukikaku4,potto7_sizailotno4_1,potto7_tyougouryou4_1,potto7_sizailotno4_2,potto7_tyougouryou4_2,potto8_zairyohinmei1,"
                + "potto8_tyogouryoukikaku1,potto8_sizailotno1_1,potto8_tyougouryou1_1,potto8_sizailotno1_2,potto8_tyougouryou1_2,potto8_zairyohinmei2,"
                + "potto8_tyogouryoukikaku2,potto8_sizailotno2_1,potto8_tyougouryou2_1,potto8_sizailotno2_2,potto8_tyougouryou2_2,potto8_zairyohinmei3,"
                + "potto8_tyogouryoukikaku3,potto8_sizailotno3_1,potto8_tyougouryou3_1,potto8_sizailotno3_2,potto8_tyougouryou3_2,potto8_zairyohinmei4,"
                + "potto8_tyogouryoukikaku4,potto8_sizailotno4_1,potto8_tyougouryou4_1,potto8_sizailotno4_2,potto8_tyougouryou4_2,hyouryounichiji,tantousya,"
                + "kakuninsya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + " FROM tmp_sr_glassslurryhyoryo "
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
        mapping.put("kojyo", "kojyo");                                           // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                           // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                         // 枝番
        mapping.put("glassslurryhinmei", "glassslurryhinmei");                   // ｶﾞﾗｽｽﾗﾘｰ品名
        mapping.put("glassslurrylotno", "glassslurrylotno");                     // ｶﾞﾗｽｽﾗﾘｰ品名LotNo
        mapping.put("lotkubun", "lotkubun");                                     // ﾛｯﾄ区分
        mapping.put("fusaipottosize", "fusaipottosize");                         // 粉砕ﾎﾟｯﾄｻｲｽﾞ
        mapping.put("tamaishikei", "tamaishikei");                               // 玉石径
        mapping.put("goki", "goki");                                             // 秤量号機
        mapping.put("potto1_zairyohinmei1", "potto1_zairyohinmei1");             // ﾎﾟｯﾄ1_材料品名1
        mapping.put("potto1_tyogouryoukikaku1", "potto1_tyogouryoukikaku1");     // ﾎﾟｯﾄ1_調合量規格1
        mapping.put("potto1_sizailotno1_1", "potto1_sizailotno1_1");             // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.1_1
        mapping.put("potto1_tyougouryou1_1", "potto1_tyougouryou1_1");           // ﾎﾟｯﾄ1_調合量1_1
        mapping.put("potto1_sizailotno1_2", "potto1_sizailotno1_2");             // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.1_2
        mapping.put("potto1_tyougouryou1_2", "potto1_tyougouryou1_2");           // ﾎﾟｯﾄ1_調合量1_2
        mapping.put("potto1_zairyohinmei2", "potto1_zairyohinmei2");             // ﾎﾟｯﾄ1_材料品名2
        mapping.put("potto1_tyogouryoukikaku2", "potto1_tyogouryoukikaku2");     // ﾎﾟｯﾄ1_調合量規格2
        mapping.put("potto1_sizailotno2_1", "potto1_sizailotno2_1");             // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.2_1
        mapping.put("potto1_tyougouryou2_1", "potto1_tyougouryou2_1");           // ﾎﾟｯﾄ1_調合量2_1
        mapping.put("potto1_sizailotno2_2", "potto1_sizailotno2_2");             // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.2_2
        mapping.put("potto1_tyougouryou2_2", "potto1_tyougouryou2_2");           // ﾎﾟｯﾄ1_調合量2_2
        mapping.put("potto1_zairyohinmei3", "potto1_zairyohinmei3");             // ﾎﾟｯﾄ1_材料品名3
        mapping.put("potto1_tyogouryoukikaku3", "potto1_tyogouryoukikaku3");     // ﾎﾟｯﾄ1_調合量規格3
        mapping.put("potto1_sizailotno3_1", "potto1_sizailotno3_1");             // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.3_1
        mapping.put("potto1_tyougouryou3_1", "potto1_tyougouryou3_1");           // ﾎﾟｯﾄ1_調合量3_1
        mapping.put("potto1_sizailotno3_2", "potto1_sizailotno3_2");             // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.3_2
        mapping.put("potto1_tyougouryou3_2", "potto1_tyougouryou3_2");           // ﾎﾟｯﾄ1_調合量3_2
        mapping.put("potto1_zairyohinmei4", "potto1_zairyohinmei4");             // ﾎﾟｯﾄ1_材料品名4
        mapping.put("potto1_tyogouryoukikaku4", "potto1_tyogouryoukikaku4");     // ﾎﾟｯﾄ1_調合量規格4
        mapping.put("potto1_sizailotno4_1", "potto1_sizailotno4_1");             // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.4_1
        mapping.put("potto1_tyougouryou4_1", "potto1_tyougouryou4_1");           // ﾎﾟｯﾄ1_調合量4_1
        mapping.put("potto1_sizailotno4_2", "potto1_sizailotno4_2");             // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.4_2
        mapping.put("potto1_tyougouryou4_2", "potto1_tyougouryou4_2");           // ﾎﾟｯﾄ1_調合量4_2
        mapping.put("potto2_zairyohinmei1", "potto2_zairyohinmei1");             // ﾎﾟｯﾄ2_材料品名1
        mapping.put("potto2_tyogouryoukikaku1", "potto2_tyogouryoukikaku1");     // ﾎﾟｯﾄ2_調合量規格1
        mapping.put("potto2_sizailotno1_1", "potto2_sizailotno1_1");             // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.1_1
        mapping.put("potto2_tyougouryou1_1", "potto2_tyougouryou1_1");           // ﾎﾟｯﾄ2_調合量1_1
        mapping.put("potto2_sizailotno1_2", "potto2_sizailotno1_2");             // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.1_2
        mapping.put("potto2_tyougouryou1_2", "potto2_tyougouryou1_2");           // ﾎﾟｯﾄ2_調合量1_2
        mapping.put("potto2_zairyohinmei2", "potto2_zairyohinmei2");             // ﾎﾟｯﾄ2_材料品名2
        mapping.put("potto2_tyogouryoukikaku2", "potto2_tyogouryoukikaku2");     // ﾎﾟｯﾄ2_調合量規格2
        mapping.put("potto2_sizailotno2_1", "potto2_sizailotno2_1");             // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.2_1
        mapping.put("potto2_tyougouryou2_1", "potto2_tyougouryou2_1");           // ﾎﾟｯﾄ2_調合量2_1
        mapping.put("potto2_sizailotno2_2", "potto2_sizailotno2_2");             // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.2_2
        mapping.put("potto2_tyougouryou2_2", "potto2_tyougouryou2_2");           // ﾎﾟｯﾄ2_調合量2_2
        mapping.put("potto2_zairyohinmei3", "potto2_zairyohinmei3");             // ﾎﾟｯﾄ2_材料品名3
        mapping.put("potto2_tyogouryoukikaku3", "potto2_tyogouryoukikaku3");     // ﾎﾟｯﾄ2_調合量規格3
        mapping.put("potto2_sizailotno3_1", "potto2_sizailotno3_1");             // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.3_1
        mapping.put("potto2_tyougouryou3_1", "potto2_tyougouryou3_1");           // ﾎﾟｯﾄ2_調合量3_1
        mapping.put("potto2_sizailotno3_2", "potto2_sizailotno3_2");             // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.3_2
        mapping.put("potto2_tyougouryou3_2", "potto2_tyougouryou3_2");           // ﾎﾟｯﾄ2_調合量3_2
        mapping.put("potto2_zairyohinmei4", "potto2_zairyohinmei4");             // ﾎﾟｯﾄ2_材料品名4
        mapping.put("potto2_tyogouryoukikaku4", "potto2_tyogouryoukikaku4");     // ﾎﾟｯﾄ2_調合量規格4
        mapping.put("potto2_sizailotno4_1", "potto2_sizailotno4_1");             // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.4_1
        mapping.put("potto2_tyougouryou4_1", "potto2_tyougouryou4_1");           // ﾎﾟｯﾄ2_調合量4_1
        mapping.put("potto2_sizailotno4_2", "potto2_sizailotno4_2");             // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.4_2
        mapping.put("potto2_tyougouryou4_2", "potto2_tyougouryou4_2");           // ﾎﾟｯﾄ2_調合量4_2
        mapping.put("potto3_zairyohinmei1", "potto3_zairyohinmei1");             // ﾎﾟｯﾄ3_材料品名1
        mapping.put("potto3_tyogouryoukikaku1", "potto3_tyogouryoukikaku1");     // ﾎﾟｯﾄ3_調合量規格1
        mapping.put("potto3_sizailotno1_1", "potto3_sizailotno1_1");             // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.1_1
        mapping.put("potto3_tyougouryou1_1", "potto3_tyougouryou1_1");           // ﾎﾟｯﾄ3_調合量1_1
        mapping.put("potto3_sizailotno1_2", "potto3_sizailotno1_2");             // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.1_2
        mapping.put("potto3_tyougouryou1_2", "potto3_tyougouryou1_2");           // ﾎﾟｯﾄ3_調合量1_2
        mapping.put("potto3_zairyohinmei2", "potto3_zairyohinmei2");             // ﾎﾟｯﾄ3_材料品名2
        mapping.put("potto3_tyogouryoukikaku2", "potto3_tyogouryoukikaku2");     // ﾎﾟｯﾄ3_調合量規格2
        mapping.put("potto3_sizailotno2_1", "potto3_sizailotno2_1");             // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.2_1
        mapping.put("potto3_tyougouryou2_1", "potto3_tyougouryou2_1");           // ﾎﾟｯﾄ3_調合量2_1
        mapping.put("potto3_sizailotno2_2", "potto3_sizailotno2_2");             // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.2_2
        mapping.put("potto3_tyougouryou2_2", "potto3_tyougouryou2_2");           // ﾎﾟｯﾄ3_調合量2_2
        mapping.put("potto3_zairyohinmei3", "potto3_zairyohinmei3");             // ﾎﾟｯﾄ3_材料品名3
        mapping.put("potto3_tyogouryoukikaku3", "potto3_tyogouryoukikaku3");     // ﾎﾟｯﾄ3_調合量規格3
        mapping.put("potto3_sizailotno3_1", "potto3_sizailotno3_1");             // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.3_1
        mapping.put("potto3_tyougouryou3_1", "potto3_tyougouryou3_1");           // ﾎﾟｯﾄ3_調合量3_1
        mapping.put("potto3_sizailotno3_2", "potto3_sizailotno3_2");             // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.3_2
        mapping.put("potto3_tyougouryou3_2", "potto3_tyougouryou3_2");           // ﾎﾟｯﾄ3_調合量3_2
        mapping.put("potto3_zairyohinmei4", "potto3_zairyohinmei4");             // ﾎﾟｯﾄ3_材料品名4
        mapping.put("potto3_tyogouryoukikaku4", "potto3_tyogouryoukikaku4");     // ﾎﾟｯﾄ3_調合量規格4
        mapping.put("potto3_sizailotno4_1", "potto3_sizailotno4_1");             // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.4_1
        mapping.put("potto3_tyougouryou4_1", "potto3_tyougouryou4_1");           // ﾎﾟｯﾄ3_調合量4_1
        mapping.put("potto3_sizailotno4_2", "potto3_sizailotno4_2");             // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.4_2
        mapping.put("potto3_tyougouryou4_2", "potto3_tyougouryou4_2");           // ﾎﾟｯﾄ3_調合量4_2
        mapping.put("potto4_zairyohinmei1", "potto4_zairyohinmei1");             // ﾎﾟｯﾄ4_材料品名1
        mapping.put("potto4_tyogouryoukikaku1", "potto4_tyogouryoukikaku1");     // ﾎﾟｯﾄ4_調合量規格1
        mapping.put("potto4_sizailotno1_1", "potto4_sizailotno1_1");             // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.1_1
        mapping.put("potto4_tyougouryou1_1", "potto4_tyougouryou1_1");           // ﾎﾟｯﾄ4_調合量1_1
        mapping.put("potto4_sizailotno1_2", "potto4_sizailotno1_2");             // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.1_2
        mapping.put("potto4_tyougouryou1_2", "potto4_tyougouryou1_2");           // ﾎﾟｯﾄ4_調合量1_2
        mapping.put("potto4_zairyohinmei2", "potto4_zairyohinmei2");             // ﾎﾟｯﾄ4_材料品名2
        mapping.put("potto4_tyogouryoukikaku2", "potto4_tyogouryoukikaku2");     // ﾎﾟｯﾄ4_調合量規格2
        mapping.put("potto4_sizailotno2_1", "potto4_sizailotno2_1");             // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.2_1
        mapping.put("potto4_tyougouryou2_1", "potto4_tyougouryou2_1");           // ﾎﾟｯﾄ4_調合量2_1
        mapping.put("potto4_sizailotno2_2", "potto4_sizailotno2_2");             // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.2_2
        mapping.put("potto4_tyougouryou2_2", "potto4_tyougouryou2_2");           // ﾎﾟｯﾄ4_調合量2_2
        mapping.put("potto4_zairyohinmei3", "potto4_zairyohinmei3");             // ﾎﾟｯﾄ4_材料品名3
        mapping.put("potto4_tyogouryoukikaku3", "potto4_tyogouryoukikaku3");     // ﾎﾟｯﾄ4_調合量規格3
        mapping.put("potto4_sizailotno3_1", "potto4_sizailotno3_1");             // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.3_1
        mapping.put("potto4_tyougouryou3_1", "potto4_tyougouryou3_1");           // ﾎﾟｯﾄ4_調合量3_1
        mapping.put("potto4_sizailotno3_2", "potto4_sizailotno3_2");             // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.3_2
        mapping.put("potto4_tyougouryou3_2", "potto4_tyougouryou3_2");           // ﾎﾟｯﾄ4_調合量3_2
        mapping.put("potto4_zairyohinmei4", "potto4_zairyohinmei4");             // ﾎﾟｯﾄ4_材料品名4
        mapping.put("potto4_tyogouryoukikaku4", "potto4_tyogouryoukikaku4");     // ﾎﾟｯﾄ4_調合量規格4
        mapping.put("potto4_sizailotno4_1", "potto4_sizailotno4_1");             // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.4_1
        mapping.put("potto4_tyougouryou4_1", "potto4_tyougouryou4_1");           // ﾎﾟｯﾄ4_調合量4_1
        mapping.put("potto4_sizailotno4_2", "potto4_sizailotno4_2");             // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.4_2
        mapping.put("potto4_tyougouryou4_2", "potto4_tyougouryou4_2");           // ﾎﾟｯﾄ4_調合量4_2
        mapping.put("potto5_zairyohinmei1", "potto5_zairyohinmei1");             // ﾎﾟｯﾄ5_材料品名1
        mapping.put("potto5_tyogouryoukikaku1", "potto5_tyogouryoukikaku1");     // ﾎﾟｯﾄ5_調合量規格1
        mapping.put("potto5_sizailotno1_1", "potto5_sizailotno1_1");             // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.1_1
        mapping.put("potto5_tyougouryou1_1", "potto5_tyougouryou1_1");           // ﾎﾟｯﾄ5_調合量1_1
        mapping.put("potto5_sizailotno1_2", "potto5_sizailotno1_2");             // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.1_2
        mapping.put("potto5_tyougouryou1_2", "potto5_tyougouryou1_2");           // ﾎﾟｯﾄ5_調合量1_2
        mapping.put("potto5_zairyohinmei2", "potto5_zairyohinmei2");             // ﾎﾟｯﾄ5_材料品名2
        mapping.put("potto5_tyogouryoukikaku2", "potto5_tyogouryoukikaku2");     // ﾎﾟｯﾄ5_調合量規格2
        mapping.put("potto5_sizailotno2_1", "potto5_sizailotno2_1");             // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.2_1
        mapping.put("potto5_tyougouryou2_1", "potto5_tyougouryou2_1");           // ﾎﾟｯﾄ5_調合量2_1
        mapping.put("potto5_sizailotno2_2", "potto5_sizailotno2_2");             // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.2_2
        mapping.put("potto5_tyougouryou2_2", "potto5_tyougouryou2_2");           // ﾎﾟｯﾄ5_調合量2_2
        mapping.put("potto5_zairyohinmei3", "potto5_zairyohinmei3");             // ﾎﾟｯﾄ5_材料品名3
        mapping.put("potto5_tyogouryoukikaku3", "potto5_tyogouryoukikaku3");     // ﾎﾟｯﾄ5_調合量規格3
        mapping.put("potto5_sizailotno3_1", "potto5_sizailotno3_1");             // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.3_1
        mapping.put("potto5_tyougouryou3_1", "potto5_tyougouryou3_1");           // ﾎﾟｯﾄ5_調合量3_1
        mapping.put("potto5_sizailotno3_2", "potto5_sizailotno3_2");             // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.3_2
        mapping.put("potto5_tyougouryou3_2", "potto5_tyougouryou3_2");           // ﾎﾟｯﾄ5_調合量3_2
        mapping.put("potto5_zairyohinmei4", "potto5_zairyohinmei4");             // ﾎﾟｯﾄ5_材料品名4
        mapping.put("potto5_tyogouryoukikaku4", "potto5_tyogouryoukikaku4");     // ﾎﾟｯﾄ5_調合量規格4
        mapping.put("potto5_sizailotno4_1", "potto5_sizailotno4_1");             // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.4_1
        mapping.put("potto5_tyougouryou4_1", "potto5_tyougouryou4_1");           // ﾎﾟｯﾄ5_調合量4_1
        mapping.put("potto5_sizailotno4_2", "potto5_sizailotno4_2");             // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.4_2
        mapping.put("potto5_tyougouryou4_2", "potto5_tyougouryou4_2");           // ﾎﾟｯﾄ5_調合量4_2
        mapping.put("potto6_zairyohinmei1", "potto6_zairyohinmei1");             // ﾎﾟｯﾄ6_材料品名1
        mapping.put("potto6_tyogouryoukikaku1", "potto6_tyogouryoukikaku1");     // ﾎﾟｯﾄ6_調合量規格1
        mapping.put("potto6_sizailotno1_1", "potto6_sizailotno1_1");             // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.1_1
        mapping.put("potto6_tyougouryou1_1", "potto6_tyougouryou1_1");           // ﾎﾟｯﾄ6_調合量1_1
        mapping.put("potto6_sizailotno1_2", "potto6_sizailotno1_2");             // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.1_2
        mapping.put("potto6_tyougouryou1_2", "potto6_tyougouryou1_2");           // ﾎﾟｯﾄ6_調合量1_2
        mapping.put("potto6_zairyohinmei2", "potto6_zairyohinmei2");             // ﾎﾟｯﾄ6_材料品名2
        mapping.put("potto6_tyogouryoukikaku2", "potto6_tyogouryoukikaku2");     // ﾎﾟｯﾄ6_調合量規格2
        mapping.put("potto6_sizailotno2_1", "potto6_sizailotno2_1");             // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.2_1
        mapping.put("potto6_tyougouryou2_1", "potto6_tyougouryou2_1");           // ﾎﾟｯﾄ6_調合量2_1
        mapping.put("potto6_sizailotno2_2", "potto6_sizailotno2_2");             // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.2_2
        mapping.put("potto6_tyougouryou2_2", "potto6_tyougouryou2_2");           // ﾎﾟｯﾄ6_調合量2_2
        mapping.put("potto6_zairyohinmei3", "potto6_zairyohinmei3");             // ﾎﾟｯﾄ6_材料品名3
        mapping.put("potto6_tyogouryoukikaku3", "potto6_tyogouryoukikaku3");     // ﾎﾟｯﾄ6_調合量規格3
        mapping.put("potto6_sizailotno3_1", "potto6_sizailotno3_1");             // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.3_1
        mapping.put("potto6_tyougouryou3_1", "potto6_tyougouryou3_1");           // ﾎﾟｯﾄ6_調合量3_1
        mapping.put("potto6_sizailotno3_2", "potto6_sizailotno3_2");             // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.3_2
        mapping.put("potto6_tyougouryou3_2", "potto6_tyougouryou3_2");           // ﾎﾟｯﾄ6_調合量3_2
        mapping.put("potto6_zairyohinmei4", "potto6_zairyohinmei4");             // ﾎﾟｯﾄ6_材料品名4
        mapping.put("potto6_tyogouryoukikaku4", "potto6_tyogouryoukikaku4");     // ﾎﾟｯﾄ6_調合量規格4
        mapping.put("potto6_sizailotno4_1", "potto6_sizailotno4_1");             // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.4_1
        mapping.put("potto6_tyougouryou4_1", "potto6_tyougouryou4_1");           // ﾎﾟｯﾄ6_調合量4_1
        mapping.put("potto6_sizailotno4_2", "potto6_sizailotno4_2");             // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.4_2
        mapping.put("potto6_tyougouryou4_2", "potto6_tyougouryou4_2");           // ﾎﾟｯﾄ6_調合量4_2
        mapping.put("potto7_zairyohinmei1", "potto7_zairyohinmei1");             // ﾎﾟｯﾄ7_材料品名1
        mapping.put("potto7_tyogouryoukikaku1", "potto7_tyogouryoukikaku1");     // ﾎﾟｯﾄ7_調合量規格1
        mapping.put("potto7_sizailotno1_1", "potto7_sizailotno1_1");             // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.1_1
        mapping.put("potto7_tyougouryou1_1", "potto7_tyougouryou1_1");           // ﾎﾟｯﾄ7_調合量1_1
        mapping.put("potto7_sizailotno1_2", "potto7_sizailotno1_2");             // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.1_2
        mapping.put("potto7_tyougouryou1_2", "potto7_tyougouryou1_2");           // ﾎﾟｯﾄ7_調合量1_2
        mapping.put("potto7_zairyohinmei2", "potto7_zairyohinmei2");             // ﾎﾟｯﾄ7_材料品名2
        mapping.put("potto7_tyogouryoukikaku2", "potto7_tyogouryoukikaku2");     // ﾎﾟｯﾄ7_調合量規格2
        mapping.put("potto7_sizailotno2_1", "potto7_sizailotno2_1");             // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.2_1
        mapping.put("potto7_tyougouryou2_1", "potto7_tyougouryou2_1");           // ﾎﾟｯﾄ7_調合量2_1
        mapping.put("potto7_sizailotno2_2", "potto7_sizailotno2_2");             // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.2_2
        mapping.put("potto7_tyougouryou2_2", "potto7_tyougouryou2_2");           // ﾎﾟｯﾄ7_調合量2_2
        mapping.put("potto7_zairyohinmei3", "potto7_zairyohinmei3");             // ﾎﾟｯﾄ7_材料品名3
        mapping.put("potto7_tyogouryoukikaku3", "potto7_tyogouryoukikaku3");     // ﾎﾟｯﾄ7_調合量規格3
        mapping.put("potto7_sizailotno3_1", "potto7_sizailotno3_1");             // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.3_1
        mapping.put("potto7_tyougouryou3_1", "potto7_tyougouryou3_1");           // ﾎﾟｯﾄ7_調合量3_1
        mapping.put("potto7_sizailotno3_2", "potto7_sizailotno3_2");             // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.3_2
        mapping.put("potto7_tyougouryou3_2", "potto7_tyougouryou3_2");           // ﾎﾟｯﾄ7_調合量3_2
        mapping.put("potto7_zairyohinmei4", "potto7_zairyohinmei4");             // ﾎﾟｯﾄ7_材料品名4
        mapping.put("potto7_tyogouryoukikaku4", "potto7_tyogouryoukikaku4");     // ﾎﾟｯﾄ7_調合量規格4
        mapping.put("potto7_sizailotno4_1", "potto7_sizailotno4_1");             // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.4_1
        mapping.put("potto7_tyougouryou4_1", "potto7_tyougouryou4_1");           // ﾎﾟｯﾄ7_調合量4_1
        mapping.put("potto7_sizailotno4_2", "potto7_sizailotno4_2");             // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.4_2
        mapping.put("potto7_tyougouryou4_2", "potto7_tyougouryou4_2");           // ﾎﾟｯﾄ7_調合量4_2
        mapping.put("potto8_zairyohinmei1", "potto8_zairyohinmei1");             // ﾎﾟｯﾄ8_材料品名1
        mapping.put("potto8_tyogouryoukikaku1", "potto8_tyogouryoukikaku1");     // ﾎﾟｯﾄ8_調合量規格1
        mapping.put("potto8_sizailotno1_1", "potto8_sizailotno1_1");             // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.1_1
        mapping.put("potto8_tyougouryou1_1", "potto8_tyougouryou1_1");           // ﾎﾟｯﾄ8_調合量1_1
        mapping.put("potto8_sizailotno1_2", "potto8_sizailotno1_2");             // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.1_2
        mapping.put("potto8_tyougouryou1_2", "potto8_tyougouryou1_2");           // ﾎﾟｯﾄ8_調合量1_2
        mapping.put("potto8_zairyohinmei2", "potto8_zairyohinmei2");             // ﾎﾟｯﾄ8_材料品名2
        mapping.put("potto8_tyogouryoukikaku2", "potto8_tyogouryoukikaku2");     // ﾎﾟｯﾄ8_調合量規格2
        mapping.put("potto8_sizailotno2_1", "potto8_sizailotno2_1");             // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.2_1
        mapping.put("potto8_tyougouryou2_1", "potto8_tyougouryou2_1");           // ﾎﾟｯﾄ8_調合量2_1
        mapping.put("potto8_sizailotno2_2", "potto8_sizailotno2_2");             // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.2_2
        mapping.put("potto8_tyougouryou2_2", "potto8_tyougouryou2_2");           // ﾎﾟｯﾄ8_調合量2_2
        mapping.put("potto8_zairyohinmei3", "potto8_zairyohinmei3");             // ﾎﾟｯﾄ8_材料品名3
        mapping.put("potto8_tyogouryoukikaku3", "potto8_tyogouryoukikaku3");     // ﾎﾟｯﾄ8_調合量規格3
        mapping.put("potto8_sizailotno3_1", "potto8_sizailotno3_1");             // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.3_1
        mapping.put("potto8_tyougouryou3_1", "potto8_tyougouryou3_1");           // ﾎﾟｯﾄ8_調合量3_1
        mapping.put("potto8_sizailotno3_2", "potto8_sizailotno3_2");             // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.3_2
        mapping.put("potto8_tyougouryou3_2", "potto8_tyougouryou3_2");           // ﾎﾟｯﾄ8_調合量3_2
        mapping.put("potto8_zairyohinmei4", "potto8_zairyohinmei4");             // ﾎﾟｯﾄ8_材料品名4
        mapping.put("potto8_tyogouryoukikaku4", "potto8_tyogouryoukikaku4");     // ﾎﾟｯﾄ8_調合量規格4
        mapping.put("potto8_sizailotno4_1", "potto8_sizailotno4_1");             // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.4_1
        mapping.put("potto8_tyougouryou4_1", "potto8_tyougouryou4_1");           // ﾎﾟｯﾄ8_調合量4_1
        mapping.put("potto8_sizailotno4_2", "potto8_sizailotno4_2");             // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.4_2
        mapping.put("potto8_tyougouryou4_2", "potto8_tyougouryou4_2");           // ﾎﾟｯﾄ8_調合量4_2
        mapping.put("hyouryounichiji", "hyouryounichiji");                       // 秤量日時
        mapping.put("tantousya", "tantousya");                                   // 担当者
        mapping.put("kakuninsya", "kakuninsya");                                 // 確認者
        mapping.put("bikou1", "bikou1");                                         // 備考1
        mapping.put("bikou2", "bikou2");                                         // 備考2
        mapping.put("torokunichiji", "torokunichiji");                           // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                             // 更新日時
        mapping.put("revision", "revision");                                     // revision
        mapping.put("deleteflag", "deleteflag");                                 // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrGlassslurryhyoryo>> beanHandler = new BeanListHandler<>(SrGlassslurryhyoryo.class, rowProcessor);

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
    private ProcessData setItemDataEx(ProcessData processData, String itemId, String value) {
        List<FXHDD01> selectData
                = processData.getItemListEx().stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
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
     * @param srGlassslurryhyoryo ｶﾞﾗｽｽﾗﾘｰ作製・秤量データ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrGlassslurryhyoryo srGlassslurryhyoryo) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srGlassslurryhyoryo != null) {
            // 元データが存在する場合元データより取得
            return getGlassslurryhyoryoItemData(itemId, srGlassslurryhyoryo);
        } else {
            return null;
        }
    }

    /**
     * 項目データ(入力値)取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @param srGlasshyoryo ｶﾞﾗｽ作製・秤量データ
     * @return 入力値
     */
    private String getItemKikakuchi(List<FXHDD01> listData, String itemId, SrGlassslurryhyoryo srGlassslurryhyoryo) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return StringUtil.nullToBlank(selectData.get(0).getKikakuChi()).replace("【", "").replace("】", "");
        } else if (srGlassslurryhyoryo != null) {
            // 元データが存在する場合元データより取得
            return getGlassslurryhyoryoItemData(itemId, srGlassslurryhyoryo);
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
     * 品質DB登録実績(fxhdd11)登録処理
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
     * 品質DB登録実績(fxhdd11)更新処理
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
     * ｶﾞﾗｽｽﾗﾘｰ作製・秤量_仮登録(tmp_sr_glassslurryhyoryo)登録処理
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
    private void insertTmpSrGlassslurryhyoryo(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "INSERT INTO tmp_sr_glassslurryhyoryo ( "
                + "kojyo,lotno,edaban,glassslurryhinmei,glassslurrylotno,lotkubun,fusaipottosize,tamaishikei,goki,potto1_zairyohinmei1,"
                + "potto1_tyogouryoukikaku1,potto1_sizailotno1_1,potto1_tyougouryou1_1,potto1_sizailotno1_2,potto1_tyougouryou1_2,potto1_zairyohinmei2,"
                + "potto1_tyogouryoukikaku2,potto1_sizailotno2_1,potto1_tyougouryou2_1,potto1_sizailotno2_2,potto1_tyougouryou2_2,potto1_zairyohinmei3,"
                + "potto1_tyogouryoukikaku3,potto1_sizailotno3_1,potto1_tyougouryou3_1,potto1_sizailotno3_2,potto1_tyougouryou3_2,potto1_zairyohinmei4,"
                + "potto1_tyogouryoukikaku4,potto1_sizailotno4_1,potto1_tyougouryou4_1,potto1_sizailotno4_2,potto1_tyougouryou4_2,potto2_zairyohinmei1,"
                + "potto2_tyogouryoukikaku1,potto2_sizailotno1_1,potto2_tyougouryou1_1,potto2_sizailotno1_2,potto2_tyougouryou1_2,potto2_zairyohinmei2,"
                + "potto2_tyogouryoukikaku2,potto2_sizailotno2_1,potto2_tyougouryou2_1,potto2_sizailotno2_2,potto2_tyougouryou2_2,potto2_zairyohinmei3,"
                + "potto2_tyogouryoukikaku3,potto2_sizailotno3_1,potto2_tyougouryou3_1,potto2_sizailotno3_2,potto2_tyougouryou3_2,potto2_zairyohinmei4,"
                + "potto2_tyogouryoukikaku4,potto2_sizailotno4_1,potto2_tyougouryou4_1,potto2_sizailotno4_2,potto2_tyougouryou4_2,potto3_zairyohinmei1,"
                + "potto3_tyogouryoukikaku1,potto3_sizailotno1_1,potto3_tyougouryou1_1,potto3_sizailotno1_2,potto3_tyougouryou1_2,potto3_zairyohinmei2,"
                + "potto3_tyogouryoukikaku2,potto3_sizailotno2_1,potto3_tyougouryou2_1,potto3_sizailotno2_2,potto3_tyougouryou2_2,potto3_zairyohinmei3,"
                + "potto3_tyogouryoukikaku3,potto3_sizailotno3_1,potto3_tyougouryou3_1,potto3_sizailotno3_2,potto3_tyougouryou3_2,potto3_zairyohinmei4,"
                + "potto3_tyogouryoukikaku4,potto3_sizailotno4_1,potto3_tyougouryou4_1,potto3_sizailotno4_2,potto3_tyougouryou4_2,potto4_zairyohinmei1,"
                + "potto4_tyogouryoukikaku1,potto4_sizailotno1_1,potto4_tyougouryou1_1,potto4_sizailotno1_2,potto4_tyougouryou1_2,potto4_zairyohinmei2,"
                + "potto4_tyogouryoukikaku2,potto4_sizailotno2_1,potto4_tyougouryou2_1,potto4_sizailotno2_2,potto4_tyougouryou2_2,potto4_zairyohinmei3,"
                + "potto4_tyogouryoukikaku3,potto4_sizailotno3_1,potto4_tyougouryou3_1,potto4_sizailotno3_2,potto4_tyougouryou3_2,potto4_zairyohinmei4,"
                + "potto4_tyogouryoukikaku4,potto4_sizailotno4_1,potto4_tyougouryou4_1,potto4_sizailotno4_2,potto4_tyougouryou4_2,potto5_zairyohinmei1,"
                + "potto5_tyogouryoukikaku1,potto5_sizailotno1_1,potto5_tyougouryou1_1,potto5_sizailotno1_2,potto5_tyougouryou1_2,potto5_zairyohinmei2,"
                + "potto5_tyogouryoukikaku2,potto5_sizailotno2_1,potto5_tyougouryou2_1,potto5_sizailotno2_2,potto5_tyougouryou2_2,potto5_zairyohinmei3,"
                + "potto5_tyogouryoukikaku3,potto5_sizailotno3_1,potto5_tyougouryou3_1,potto5_sizailotno3_2,potto5_tyougouryou3_2,potto5_zairyohinmei4,"
                + "potto5_tyogouryoukikaku4,potto5_sizailotno4_1,potto5_tyougouryou4_1,potto5_sizailotno4_2,potto5_tyougouryou4_2,potto6_zairyohinmei1,"
                + "potto6_tyogouryoukikaku1,potto6_sizailotno1_1,potto6_tyougouryou1_1,potto6_sizailotno1_2,potto6_tyougouryou1_2,potto6_zairyohinmei2,"
                + "potto6_tyogouryoukikaku2,potto6_sizailotno2_1,potto6_tyougouryou2_1,potto6_sizailotno2_2,potto6_tyougouryou2_2,potto6_zairyohinmei3,"
                + "potto6_tyogouryoukikaku3,potto6_sizailotno3_1,potto6_tyougouryou3_1,potto6_sizailotno3_2,potto6_tyougouryou3_2,potto6_zairyohinmei4,"
                + "potto6_tyogouryoukikaku4,potto6_sizailotno4_1,potto6_tyougouryou4_1,potto6_sizailotno4_2,potto6_tyougouryou4_2,potto7_zairyohinmei1,"
                + "potto7_tyogouryoukikaku1,potto7_sizailotno1_1,potto7_tyougouryou1_1,potto7_sizailotno1_2,potto7_tyougouryou1_2,potto7_zairyohinmei2,"
                + "potto7_tyogouryoukikaku2,potto7_sizailotno2_1,potto7_tyougouryou2_1,potto7_sizailotno2_2,potto7_tyougouryou2_2,potto7_zairyohinmei3,"
                + "potto7_tyogouryoukikaku3,potto7_sizailotno3_1,potto7_tyougouryou3_1,potto7_sizailotno3_2,potto7_tyougouryou3_2,potto7_zairyohinmei4,"
                + "potto7_tyogouryoukikaku4,potto7_sizailotno4_1,potto7_tyougouryou4_1,potto7_sizailotno4_2,potto7_tyougouryou4_2,potto8_zairyohinmei1,"
                + "potto8_tyogouryoukikaku1,potto8_sizailotno1_1,potto8_tyougouryou1_1,potto8_sizailotno1_2,potto8_tyougouryou1_2,potto8_zairyohinmei2,"
                + "potto8_tyogouryoukikaku2,potto8_sizailotno2_1,potto8_tyougouryou2_1,potto8_sizailotno2_2,potto8_tyougouryou2_2,potto8_zairyohinmei3,"
                + "potto8_tyogouryoukikaku3,potto8_sizailotno3_1,potto8_tyougouryou3_1,potto8_sizailotno3_2,potto8_tyougouryou3_2,potto8_zairyohinmei4,"
                + "potto8_tyogouryoukikaku4,potto8_sizailotno4_1,potto8_tyougouryou4_1,potto8_sizailotno4_2,potto8_tyougouryou4_2,hyouryounichiji,tantousya,"
                + "kakuninsya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrGlassslurryhyoryo(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, processData, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ作製・秤量_仮登録(tmp_sr_glassslurryhyoryo)更新処理
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
    private void updateTmpSrGlassslurryhyoryo(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "UPDATE tmp_sr_glassslurryhyoryo SET "
                + "glassslurryhinmei = ?,glassslurrylotno = ?,lotkubun = ?,fusaipottosize = ?,tamaishikei = ?,goki = ?,potto1_zairyohinmei1 = ?,potto1_tyogouryoukikaku1 = ?,"
                + "potto1_sizailotno1_1 = ?,potto1_tyougouryou1_1 = ?,potto1_sizailotno1_2 = ?,potto1_tyougouryou1_2 = ?,potto1_zairyohinmei2 = ?,potto1_tyogouryoukikaku2 = ?,"
                + "potto1_sizailotno2_1 = ?,potto1_tyougouryou2_1 = ?,potto1_sizailotno2_2 = ?,potto1_tyougouryou2_2 = ?,potto1_zairyohinmei3 = ?,potto1_tyogouryoukikaku3 = ?,"
                + "potto1_sizailotno3_1 = ?,potto1_tyougouryou3_1 = ?,potto1_sizailotno3_2 = ?,potto1_tyougouryou3_2 = ?,potto1_zairyohinmei4 = ?,potto1_tyogouryoukikaku4 = ?,"
                + "potto1_sizailotno4_1 = ?,potto1_tyougouryou4_1 = ?,potto1_sizailotno4_2 = ?,potto1_tyougouryou4_2 = ?,potto2_zairyohinmei1 = ?,potto2_tyogouryoukikaku1 = ?,"
                + "potto2_sizailotno1_1 = ?,potto2_tyougouryou1_1 = ?,potto2_sizailotno1_2 = ?,potto2_tyougouryou1_2 = ?,potto2_zairyohinmei2 = ?,potto2_tyogouryoukikaku2 = ?,"
                + "potto2_sizailotno2_1 = ?,potto2_tyougouryou2_1 = ?,potto2_sizailotno2_2 = ?,potto2_tyougouryou2_2 = ?,potto2_zairyohinmei3 = ?,potto2_tyogouryoukikaku3 = ?,"
                + "potto2_sizailotno3_1 = ?,potto2_tyougouryou3_1 = ?,potto2_sizailotno3_2 = ?,potto2_tyougouryou3_2 = ?,potto2_zairyohinmei4 = ?,potto2_tyogouryoukikaku4 = ?,"
                + "potto2_sizailotno4_1 = ?,potto2_tyougouryou4_1 = ?,potto2_sizailotno4_2 = ?,potto2_tyougouryou4_2 = ?,potto3_zairyohinmei1 = ?,potto3_tyogouryoukikaku1 = ?,"
                + "potto3_sizailotno1_1 = ?,potto3_tyougouryou1_1 = ?,potto3_sizailotno1_2 = ?,potto3_tyougouryou1_2 = ?,potto3_zairyohinmei2 = ?,potto3_tyogouryoukikaku2 = ?,"
                + "potto3_sizailotno2_1 = ?,potto3_tyougouryou2_1 = ?,potto3_sizailotno2_2 = ?,potto3_tyougouryou2_2 = ?,potto3_zairyohinmei3 = ?,potto3_tyogouryoukikaku3 = ?,"
                + "potto3_sizailotno3_1 = ?,potto3_tyougouryou3_1 = ?,potto3_sizailotno3_2 = ?,potto3_tyougouryou3_2 = ?,potto3_zairyohinmei4 = ?,potto3_tyogouryoukikaku4 = ?,"
                + "potto3_sizailotno4_1 = ?,potto3_tyougouryou4_1 = ?,potto3_sizailotno4_2 = ?,potto3_tyougouryou4_2 = ?,potto4_zairyohinmei1 = ?,potto4_tyogouryoukikaku1 = ?,"
                + "potto4_sizailotno1_1 = ?,potto4_tyougouryou1_1 = ?,potto4_sizailotno1_2 = ?,potto4_tyougouryou1_2 = ?,potto4_zairyohinmei2 = ?,potto4_tyogouryoukikaku2 = ?,"
                + "potto4_sizailotno2_1 = ?,potto4_tyougouryou2_1 = ?,potto4_sizailotno2_2 = ?,potto4_tyougouryou2_2 = ?,potto4_zairyohinmei3 = ?,potto4_tyogouryoukikaku3 = ?,"
                + "potto4_sizailotno3_1 = ?,potto4_tyougouryou3_1 = ?,potto4_sizailotno3_2 = ?,potto4_tyougouryou3_2 = ?,potto4_zairyohinmei4 = ?,potto4_tyogouryoukikaku4 = ?,"
                + "potto4_sizailotno4_1 = ?,potto4_tyougouryou4_1 = ?,potto4_sizailotno4_2 = ?,potto4_tyougouryou4_2 = ?,potto5_zairyohinmei1 = ?,potto5_tyogouryoukikaku1 = ?,"
                + "potto5_sizailotno1_1 = ?,potto5_tyougouryou1_1 = ?,potto5_sizailotno1_2 = ?,potto5_tyougouryou1_2 = ?,potto5_zairyohinmei2 = ?,potto5_tyogouryoukikaku2 = ?,"
                + "potto5_sizailotno2_1 = ?,potto5_tyougouryou2_1 = ?,potto5_sizailotno2_2 = ?,potto5_tyougouryou2_2 = ?,potto5_zairyohinmei3 = ?,potto5_tyogouryoukikaku3 = ?,"
                + "potto5_sizailotno3_1 = ?,potto5_tyougouryou3_1 = ?,potto5_sizailotno3_2 = ?,potto5_tyougouryou3_2 = ?,potto5_zairyohinmei4 = ?,potto5_tyogouryoukikaku4 = ?,"
                + "potto5_sizailotno4_1 = ?,potto5_tyougouryou4_1 = ?,potto5_sizailotno4_2 = ?,potto5_tyougouryou4_2 = ?,potto6_zairyohinmei1 = ?,potto6_tyogouryoukikaku1 = ?,"
                + "potto6_sizailotno1_1 = ?,potto6_tyougouryou1_1 = ?,potto6_sizailotno1_2 = ?,potto6_tyougouryou1_2 = ?,potto6_zairyohinmei2 = ?,potto6_tyogouryoukikaku2 = ?,"
                + "potto6_sizailotno2_1 = ?,potto6_tyougouryou2_1 = ?,potto6_sizailotno2_2 = ?,potto6_tyougouryou2_2 = ?,potto6_zairyohinmei3 = ?,potto6_tyogouryoukikaku3 = ?,"
                + "potto6_sizailotno3_1 = ?,potto6_tyougouryou3_1 = ?,potto6_sizailotno3_2 = ?,potto6_tyougouryou3_2 = ?,potto6_zairyohinmei4 = ?,potto6_tyogouryoukikaku4 = ?,"
                + "potto6_sizailotno4_1 = ?,potto6_tyougouryou4_1 = ?,potto6_sizailotno4_2 = ?,potto6_tyougouryou4_2 = ?,potto7_zairyohinmei1 = ?,potto7_tyogouryoukikaku1 = ?,"
                + "potto7_sizailotno1_1 = ?,potto7_tyougouryou1_1 = ?,potto7_sizailotno1_2 = ?,potto7_tyougouryou1_2 = ?,potto7_zairyohinmei2 = ?,potto7_tyogouryoukikaku2 = ?,"
                + "potto7_sizailotno2_1 = ?,potto7_tyougouryou2_1 = ?,potto7_sizailotno2_2 = ?,potto7_tyougouryou2_2 = ?,potto7_zairyohinmei3 = ?,potto7_tyogouryoukikaku3 = ?,"
                + "potto7_sizailotno3_1 = ?,potto7_tyougouryou3_1 = ?,potto7_sizailotno3_2 = ?,potto7_tyougouryou3_2 = ?,potto7_zairyohinmei4 = ?,potto7_tyogouryoukikaku4 = ?,"
                + "potto7_sizailotno4_1 = ?,potto7_tyougouryou4_1 = ?,potto7_sizailotno4_2 = ?,potto7_tyougouryou4_2 = ?,potto8_zairyohinmei1 = ?,potto8_tyogouryoukikaku1 = ?,"
                + "potto8_sizailotno1_1 = ?,potto8_tyougouryou1_1 = ?,potto8_sizailotno1_2 = ?,potto8_tyougouryou1_2 = ?,potto8_zairyohinmei2 = ?,potto8_tyogouryoukikaku2 = ?,"
                + "potto8_sizailotno2_1 = ?,potto8_tyougouryou2_1 = ?,potto8_sizailotno2_2 = ?,potto8_tyougouryou2_2 = ?,potto8_zairyohinmei3 = ?,potto8_tyogouryoukikaku3 = ?,"
                + "potto8_sizailotno3_1 = ?,potto8_tyougouryou3_1 = ?,potto8_sizailotno3_2 = ?,potto8_tyougouryou3_2 = ?,potto8_zairyohinmei4 = ?,potto8_tyogouryoukikaku4 = ?,"
                + "potto8_sizailotno4_1 = ?,potto8_tyougouryou4_1 = ?,potto8_sizailotno4_2 = ?,potto8_tyougouryou4_2 = ?,hyouryounichiji = ?,tantousya = ?,kakuninsya = ?,bikou1 = ?,"
                + "bikou2 = ?,kosinnichiji = ?,revision = ?,deleteflag = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrGlassslurryhyoryo> srGlassslurryhyoryoList = getSrGlassslurryhyoryoData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrGlassslurryhyoryo srGlassslurryhyoryo = null;
        if (!srGlassslurryhyoryoList.isEmpty()) {
            srGlassslurryhyoryo = srGlassslurryhyoryoList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrGlassslurryhyoryo(false, newRev, 0, "", "", "", systemTime, processData, srGlassslurryhyoryo);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ作製・秤量_仮登録(tmp_sr_glassslurryhyoryo)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrGlassslurryhyoryo(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sr_glassslurryhyoryo "
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
     * ｶﾞﾗｽｽﾗﾘｰ作製・秤量_仮登録(tmp_sr_glassslurryhyoryo)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srGlassslurryhyoryo ｶﾞﾗｽｽﾗﾘｰ作製・秤量データ
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrGlassslurryhyoryo(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, String systemTime, ProcessData processData, SrGlassslurryhyoryo srGlassslurryhyoryo) {

        List<FXHDD01> pItemList = processData.getItemList();
        List<FXHDD01> pItemListEx = processData.getItemListEx();

        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B005Const.SEIHIN_GLASSSLURRYHINMEI, srGlassslurryhyoryo)));       // ｶﾞﾗｽｽﾗﾘｰ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B005Const.SEIHIN_GLASSSLURRYLOTNO, srGlassslurryhyoryo)));        // ｶﾞﾗｽｽﾗﾘｰ品名LotNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B005Const.SEIHIN_LOT_KUBUN, srGlassslurryhyoryo)));               // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B005Const.SEIHIN_FUSAIPOTTOSIZE, srGlassslurryhyoryo)));     // 粉砕ﾎﾟｯﾄｻｲｽﾞ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B005Const.SEIHIN_TAMAISHIKEI, srGlassslurryhyoryo)));        // 玉石径
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B005Const.SEIHIN_GOKI, srGlassslurryhyoryo)));                    // 秤量号機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO1_ZAIRYOHINMEI1, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ1_材料品名1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU1, srGlassslurryhyoryo)));// ﾎﾟｯﾄ1_調合量規格1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO1_SIZAILOTNO1_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.1_1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO1_TYOUGOURYOU1_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ1_調合量1_1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO1_SIZAILOTNO1_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.1_2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO1_TYOUGOURYOU1_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ1_調合量1_2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO1_ZAIRYOHINMEI2, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ1_材料品名2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU2, srGlassslurryhyoryo)));// ﾎﾟｯﾄ1_調合量規格2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO1_SIZAILOTNO2_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.2_1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO1_TYOUGOURYOU2_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ1_調合量2_1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO1_SIZAILOTNO2_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.2_2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO1_TYOUGOURYOU2_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ1_調合量2_2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO1_ZAIRYOHINMEI3, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ1_材料品名3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU3, srGlassslurryhyoryo)));// ﾎﾟｯﾄ1_調合量規格3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO1_SIZAILOTNO3_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.3_1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO1_TYOUGOURYOU3_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ1_調合量3_1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO1_SIZAILOTNO3_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.3_2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO1_TYOUGOURYOU3_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ1_調合量3_2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO1_ZAIRYOHINMEI4, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ1_材料品名4
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU4, srGlassslurryhyoryo)));// ﾎﾟｯﾄ1_調合量規格4
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO1_SIZAILOTNO4_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.4_1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO1_TYOUGOURYOU4_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ1_調合量4_1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO1_SIZAILOTNO4_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.4_2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO1_TYOUGOURYOU4_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ1_調合量4_2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO2_ZAIRYOHINMEI1, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ2_材料品名1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU1, srGlassslurryhyoryo)));// ﾎﾟｯﾄ2_調合量規格1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO2_SIZAILOTNO1_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.1_1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO2_TYOUGOURYOU1_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ2_調合量1_1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO2_SIZAILOTNO1_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.1_2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO2_TYOUGOURYOU1_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ2_調合量1_2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO2_ZAIRYOHINMEI2, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ2_材料品名2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU2, srGlassslurryhyoryo)));// ﾎﾟｯﾄ2_調合量規格2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO2_SIZAILOTNO2_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.2_1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO2_TYOUGOURYOU2_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ2_調合量2_1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO2_SIZAILOTNO2_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.2_2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO2_TYOUGOURYOU2_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ2_調合量2_2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO2_ZAIRYOHINMEI3, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ2_材料品名3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU3, srGlassslurryhyoryo)));// ﾎﾟｯﾄ2_調合量規格3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO2_SIZAILOTNO3_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.3_1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO2_TYOUGOURYOU3_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ2_調合量3_1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO2_SIZAILOTNO3_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.3_2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO2_TYOUGOURYOU3_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ2_調合量3_2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO2_ZAIRYOHINMEI4, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ2_材料品名4
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU4, srGlassslurryhyoryo)));// ﾎﾟｯﾄ2_調合量規格4
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO2_SIZAILOTNO4_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.4_1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO2_TYOUGOURYOU4_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ2_調合量4_1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO2_SIZAILOTNO4_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.4_2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO2_TYOUGOURYOU4_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ2_調合量4_2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO3_ZAIRYOHINMEI1, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ3_材料品名1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU1, srGlassslurryhyoryo)));// ﾎﾟｯﾄ3_調合量規格1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO3_SIZAILOTNO1_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.1_1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO3_TYOUGOURYOU1_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ3_調合量1_1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO3_SIZAILOTNO1_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.1_2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO3_TYOUGOURYOU1_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ3_調合量1_2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO3_ZAIRYOHINMEI2, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ3_材料品名2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU2, srGlassslurryhyoryo)));// ﾎﾟｯﾄ3_調合量規格2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO3_SIZAILOTNO2_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.2_1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO3_TYOUGOURYOU2_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ3_調合量2_1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO3_SIZAILOTNO2_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.2_2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO3_TYOUGOURYOU2_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ3_調合量2_2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO3_ZAIRYOHINMEI3, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ3_材料品名3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU3, srGlassslurryhyoryo)));// ﾎﾟｯﾄ3_調合量規格3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO3_SIZAILOTNO3_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.3_1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO3_TYOUGOURYOU3_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ3_調合量3_1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO3_SIZAILOTNO3_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.3_2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO3_TYOUGOURYOU3_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ3_調合量3_2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO3_ZAIRYOHINMEI4, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ3_材料品名4
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU4, srGlassslurryhyoryo)));// ﾎﾟｯﾄ3_調合量規格4
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO3_SIZAILOTNO4_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.4_1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO3_TYOUGOURYOU4_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ3_調合量4_1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO3_SIZAILOTNO4_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.4_2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO3_TYOUGOURYOU4_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ3_調合量4_2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO4_ZAIRYOHINMEI1, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ4_材料品名1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU1, srGlassslurryhyoryo)));// ﾎﾟｯﾄ4_調合量規格1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO4_SIZAILOTNO1_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.1_1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO4_TYOUGOURYOU1_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ4_調合量1_1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO4_SIZAILOTNO1_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.1_2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO4_TYOUGOURYOU1_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ4_調合量1_2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO4_ZAIRYOHINMEI2, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ4_材料品名2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU2, srGlassslurryhyoryo)));// ﾎﾟｯﾄ4_調合量規格2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO4_SIZAILOTNO2_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.2_1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO4_TYOUGOURYOU2_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ4_調合量2_1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO4_SIZAILOTNO2_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.2_2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO4_TYOUGOURYOU2_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ4_調合量2_2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO4_ZAIRYOHINMEI3, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ4_材料品名3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU3, srGlassslurryhyoryo)));// ﾎﾟｯﾄ4_調合量規格3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO4_SIZAILOTNO3_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.3_1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO4_TYOUGOURYOU3_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ4_調合量3_1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO4_SIZAILOTNO3_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.3_2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO4_TYOUGOURYOU3_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ4_調合量3_2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO4_ZAIRYOHINMEI4, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ4_材料品名4
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU4, srGlassslurryhyoryo)));// ﾎﾟｯﾄ4_調合量規格4
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO4_SIZAILOTNO4_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.4_1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO4_TYOUGOURYOU4_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ4_調合量4_1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO4_SIZAILOTNO4_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.4_2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO4_TYOUGOURYOU4_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ4_調合量4_2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO5_ZAIRYOHINMEI1, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ5_材料品名1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU1, srGlassslurryhyoryo)));// ﾎﾟｯﾄ5_調合量規格1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO5_SIZAILOTNO1_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.1_1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO5_TYOUGOURYOU1_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ5_調合量1_1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO5_SIZAILOTNO1_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.1_2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO5_TYOUGOURYOU1_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ5_調合量1_2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO5_ZAIRYOHINMEI2, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ5_材料品名2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU2, srGlassslurryhyoryo)));// ﾎﾟｯﾄ5_調合量規格2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO5_SIZAILOTNO2_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.2_1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO5_TYOUGOURYOU2_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ5_調合量2_1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO5_SIZAILOTNO2_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.2_2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO5_TYOUGOURYOU2_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ5_調合量2_2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO5_ZAIRYOHINMEI3, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ5_材料品名3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU3, srGlassslurryhyoryo)));// ﾎﾟｯﾄ5_調合量規格3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO5_SIZAILOTNO3_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.3_1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO5_TYOUGOURYOU3_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ5_調合量3_1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO5_SIZAILOTNO3_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.3_2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO5_TYOUGOURYOU3_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ5_調合量3_2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO5_ZAIRYOHINMEI4, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ5_材料品名4
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU4, srGlassslurryhyoryo)));// ﾎﾟｯﾄ5_調合量規格4
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO5_SIZAILOTNO4_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.4_1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO5_TYOUGOURYOU4_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ5_調合量4_1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO5_SIZAILOTNO4_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.4_2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO5_TYOUGOURYOU4_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ5_調合量4_2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO6_ZAIRYOHINMEI1, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ6_材料品名1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU1, srGlassslurryhyoryo)));// ﾎﾟｯﾄ6_調合量規格1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO6_SIZAILOTNO1_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.1_1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO6_TYOUGOURYOU1_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ6_調合量1_1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO6_SIZAILOTNO1_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.1_2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO6_TYOUGOURYOU1_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ6_調合量1_2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO6_ZAIRYOHINMEI2, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ6_材料品名2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU2, srGlassslurryhyoryo)));// ﾎﾟｯﾄ6_調合量規格2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO6_SIZAILOTNO2_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.2_1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO6_TYOUGOURYOU2_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ6_調合量2_1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO6_SIZAILOTNO2_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.2_2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO6_TYOUGOURYOU2_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ6_調合量2_2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO6_ZAIRYOHINMEI3, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ6_材料品名3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU3, srGlassslurryhyoryo)));// ﾎﾟｯﾄ6_調合量規格3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO6_SIZAILOTNO3_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.3_1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO6_TYOUGOURYOU3_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ6_調合量3_1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO6_SIZAILOTNO3_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.3_2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO6_TYOUGOURYOU3_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ6_調合量3_2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO6_ZAIRYOHINMEI4, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ6_材料品名4
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU4, srGlassslurryhyoryo)));// ﾎﾟｯﾄ6_調合量規格4
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO6_SIZAILOTNO4_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.4_1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO6_TYOUGOURYOU4_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ6_調合量4_1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO6_SIZAILOTNO4_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.4_2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO6_TYOUGOURYOU4_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ6_調合量4_2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO7_ZAIRYOHINMEI1, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ7_材料品名1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU1, srGlassslurryhyoryo)));// ﾎﾟｯﾄ7_調合量規格1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO7_SIZAILOTNO1_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.1_1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO7_TYOUGOURYOU1_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ7_調合量1_1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO7_SIZAILOTNO1_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.1_2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO7_TYOUGOURYOU1_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ7_調合量1_2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO7_ZAIRYOHINMEI2, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ7_材料品名2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU2, srGlassslurryhyoryo)));// ﾎﾟｯﾄ7_調合量規格2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO7_SIZAILOTNO2_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.2_1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO7_TYOUGOURYOU2_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ7_調合量2_1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO7_SIZAILOTNO2_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.2_2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO7_TYOUGOURYOU2_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ7_調合量2_2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO7_ZAIRYOHINMEI3, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ7_材料品名3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU3, srGlassslurryhyoryo)));// ﾎﾟｯﾄ7_調合量規格3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO7_SIZAILOTNO3_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.3_1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO7_TYOUGOURYOU3_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ7_調合量3_1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO7_SIZAILOTNO3_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.3_2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO7_TYOUGOURYOU3_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ7_調合量3_2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO7_ZAIRYOHINMEI4, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ7_材料品名4
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU4, srGlassslurryhyoryo)));// ﾎﾟｯﾄ7_調合量規格4
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO7_SIZAILOTNO4_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.4_1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO7_TYOUGOURYOU4_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ7_調合量4_1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO7_SIZAILOTNO4_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.4_2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO7_TYOUGOURYOU4_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ7_調合量4_2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO8_ZAIRYOHINMEI1, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ8_材料品名1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU1, srGlassslurryhyoryo)));// ﾎﾟｯﾄ8_調合量規格1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO8_SIZAILOTNO1_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.1_1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO8_TYOUGOURYOU1_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ8_調合量1_1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO8_SIZAILOTNO1_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.1_2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO8_TYOUGOURYOU1_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ8_調合量1_2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO8_ZAIRYOHINMEI2, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ8_材料品名2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU2, srGlassslurryhyoryo)));// ﾎﾟｯﾄ8_調合量規格2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO8_SIZAILOTNO2_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.2_1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO8_TYOUGOURYOU2_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ8_調合量2_1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO8_SIZAILOTNO2_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.2_2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO8_TYOUGOURYOU2_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ8_調合量2_2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO8_ZAIRYOHINMEI3, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ8_材料品名3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU3, srGlassslurryhyoryo)));// ﾎﾟｯﾄ8_調合量規格3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO8_SIZAILOTNO3_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.3_1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO8_TYOUGOURYOU3_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ8_調合量3_1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO8_SIZAILOTNO3_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.3_2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO8_TYOUGOURYOU3_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ8_調合量3_2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO8_ZAIRYOHINMEI4, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ8_材料品名4
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU4, srGlassslurryhyoryo)));// ﾎﾟｯﾄ8_調合量規格4
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO8_SIZAILOTNO4_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.4_1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO8_TYOUGOURYOU4_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ8_調合量4_1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO8_SIZAILOTNO4_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.4_2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemListEx, GXHDO102B005Const.POTTO8_TYOUGOURYOU4_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ8_調合量4_2
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B005Const.SEIHIN_HYOURYOUNICHIJI_DAY, srGlassslurryhyoryo),
                getItemData(pItemList, GXHDO102B005Const.SEIHIN_HYOURYOUNICHIJI_TIME, srGlassslurryhyoryo)));                                   // 秤量日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B005Const.SEIHIN_TANTOUSYA, srGlassslurryhyoryo)));               // 担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B005Const.SEIHIN_KAKUNINSYA, srGlassslurryhyoryo)));              // 確認者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B005Const.SEIHIN_BIKOU1, srGlassslurryhyoryo)));                  // 備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B005Const.SEIHIN_BIKOU2, srGlassslurryhyoryo)));                  // 備考2

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
     * ｶﾞﾗｽｽﾗﾘｰ作製・秤量(sr_glassslurryhyoryo)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param tmpSrGlassslurryhyoryo 仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrGlassslurryhyoryo(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData, SrGlassslurryhyoryo tmpSrGlassslurryhyoryo) throws SQLException {

        String sql = "INSERT INTO sr_glassslurryhyoryo ( "
                + "kojyo,lotno,edaban,glassslurryhinmei,glassslurrylotno,lotkubun,fusaipottosize,tamaishikei,goki,potto1_zairyohinmei1,"
                + "potto1_tyogouryoukikaku1,potto1_sizailotno1_1,potto1_tyougouryou1_1,potto1_sizailotno1_2,potto1_tyougouryou1_2,potto1_zairyohinmei2,"
                + "potto1_tyogouryoukikaku2,potto1_sizailotno2_1,potto1_tyougouryou2_1,potto1_sizailotno2_2,potto1_tyougouryou2_2,potto1_zairyohinmei3,"
                + "potto1_tyogouryoukikaku3,potto1_sizailotno3_1,potto1_tyougouryou3_1,potto1_sizailotno3_2,potto1_tyougouryou3_2,potto1_zairyohinmei4,"
                + "potto1_tyogouryoukikaku4,potto1_sizailotno4_1,potto1_tyougouryou4_1,potto1_sizailotno4_2,potto1_tyougouryou4_2,potto2_zairyohinmei1,"
                + "potto2_tyogouryoukikaku1,potto2_sizailotno1_1,potto2_tyougouryou1_1,potto2_sizailotno1_2,potto2_tyougouryou1_2,potto2_zairyohinmei2,"
                + "potto2_tyogouryoukikaku2,potto2_sizailotno2_1,potto2_tyougouryou2_1,potto2_sizailotno2_2,potto2_tyougouryou2_2,potto2_zairyohinmei3,"
                + "potto2_tyogouryoukikaku3,potto2_sizailotno3_1,potto2_tyougouryou3_1,potto2_sizailotno3_2,potto2_tyougouryou3_2,potto2_zairyohinmei4,"
                + "potto2_tyogouryoukikaku4,potto2_sizailotno4_1,potto2_tyougouryou4_1,potto2_sizailotno4_2,potto2_tyougouryou4_2,potto3_zairyohinmei1,"
                + "potto3_tyogouryoukikaku1,potto3_sizailotno1_1,potto3_tyougouryou1_1,potto3_sizailotno1_2,potto3_tyougouryou1_2,potto3_zairyohinmei2,"
                + "potto3_tyogouryoukikaku2,potto3_sizailotno2_1,potto3_tyougouryou2_1,potto3_sizailotno2_2,potto3_tyougouryou2_2,potto3_zairyohinmei3,"
                + "potto3_tyogouryoukikaku3,potto3_sizailotno3_1,potto3_tyougouryou3_1,potto3_sizailotno3_2,potto3_tyougouryou3_2,potto3_zairyohinmei4,"
                + "potto3_tyogouryoukikaku4,potto3_sizailotno4_1,potto3_tyougouryou4_1,potto3_sizailotno4_2,potto3_tyougouryou4_2,potto4_zairyohinmei1,"
                + "potto4_tyogouryoukikaku1,potto4_sizailotno1_1,potto4_tyougouryou1_1,potto4_sizailotno1_2,potto4_tyougouryou1_2,potto4_zairyohinmei2,"
                + "potto4_tyogouryoukikaku2,potto4_sizailotno2_1,potto4_tyougouryou2_1,potto4_sizailotno2_2,potto4_tyougouryou2_2,potto4_zairyohinmei3,"
                + "potto4_tyogouryoukikaku3,potto4_sizailotno3_1,potto4_tyougouryou3_1,potto4_sizailotno3_2,potto4_tyougouryou3_2,potto4_zairyohinmei4,"
                + "potto4_tyogouryoukikaku4,potto4_sizailotno4_1,potto4_tyougouryou4_1,potto4_sizailotno4_2,potto4_tyougouryou4_2,potto5_zairyohinmei1,"
                + "potto5_tyogouryoukikaku1,potto5_sizailotno1_1,potto5_tyougouryou1_1,potto5_sizailotno1_2,potto5_tyougouryou1_2,potto5_zairyohinmei2,"
                + "potto5_tyogouryoukikaku2,potto5_sizailotno2_1,potto5_tyougouryou2_1,potto5_sizailotno2_2,potto5_tyougouryou2_2,potto5_zairyohinmei3,"
                + "potto5_tyogouryoukikaku3,potto5_sizailotno3_1,potto5_tyougouryou3_1,potto5_sizailotno3_2,potto5_tyougouryou3_2,potto5_zairyohinmei4,"
                + "potto5_tyogouryoukikaku4,potto5_sizailotno4_1,potto5_tyougouryou4_1,potto5_sizailotno4_2,potto5_tyougouryou4_2,potto6_zairyohinmei1,"
                + "potto6_tyogouryoukikaku1,potto6_sizailotno1_1,potto6_tyougouryou1_1,potto6_sizailotno1_2,potto6_tyougouryou1_2,potto6_zairyohinmei2,"
                + "potto6_tyogouryoukikaku2,potto6_sizailotno2_1,potto6_tyougouryou2_1,potto6_sizailotno2_2,potto6_tyougouryou2_2,potto6_zairyohinmei3,"
                + "potto6_tyogouryoukikaku3,potto6_sizailotno3_1,potto6_tyougouryou3_1,potto6_sizailotno3_2,potto6_tyougouryou3_2,potto6_zairyohinmei4,"
                + "potto6_tyogouryoukikaku4,potto6_sizailotno4_1,potto6_tyougouryou4_1,potto6_sizailotno4_2,potto6_tyougouryou4_2,potto7_zairyohinmei1,"
                + "potto7_tyogouryoukikaku1,potto7_sizailotno1_1,potto7_tyougouryou1_1,potto7_sizailotno1_2,potto7_tyougouryou1_2,potto7_zairyohinmei2,"
                + "potto7_tyogouryoukikaku2,potto7_sizailotno2_1,potto7_tyougouryou2_1,potto7_sizailotno2_2,potto7_tyougouryou2_2,potto7_zairyohinmei3,"
                + "potto7_tyogouryoukikaku3,potto7_sizailotno3_1,potto7_tyougouryou3_1,potto7_sizailotno3_2,potto7_tyougouryou3_2,potto7_zairyohinmei4,"
                + "potto7_tyogouryoukikaku4,potto7_sizailotno4_1,potto7_tyougouryou4_1,potto7_sizailotno4_2,potto7_tyougouryou4_2,potto8_zairyohinmei1,"
                + "potto8_tyogouryoukikaku1,potto8_sizailotno1_1,potto8_tyougouryou1_1,potto8_sizailotno1_2,potto8_tyougouryou1_2,potto8_zairyohinmei2,"
                + "potto8_tyogouryoukikaku2,potto8_sizailotno2_1,potto8_tyougouryou2_1,potto8_sizailotno2_2,potto8_tyougouryou2_2,potto8_zairyohinmei3,"
                + "potto8_tyogouryoukikaku3,potto8_sizailotno3_1,potto8_tyougouryou3_1,potto8_sizailotno3_2,potto8_tyougouryou3_2,potto8_zairyohinmei4,"
                + "potto8_tyogouryoukikaku4,potto8_sizailotno4_1,potto8_tyougouryou4_1,potto8_sizailotno4_2,potto8_tyougouryou4_2,hyouryounichiji,tantousya,"
                + "kakuninsya,bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrGlassslurryhyoryo(true, newRev, kojyo, lotNo, edaban, systemTime, processData, tmpSrGlassslurryhyoryo);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ作製・秤量(sr_glassslurryhyoryo)更新処理
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
    private void updateSrGlassslurryhyoryo(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {
        String sql = "UPDATE sr_glassslurryhyoryo SET "
                + "glassslurryhinmei = ?,glassslurrylotno = ?,lotkubun = ?,fusaipottosize = ?,tamaishikei = ?,goki = ?,potto1_zairyohinmei1 = ?,potto1_tyogouryoukikaku1 = ?,"
                + "potto1_sizailotno1_1 = ?,potto1_tyougouryou1_1 = ?,potto1_sizailotno1_2 = ?,potto1_tyougouryou1_2 = ?,potto1_zairyohinmei2 = ?,potto1_tyogouryoukikaku2 = ?,"
                + "potto1_sizailotno2_1 = ?,potto1_tyougouryou2_1 = ?,potto1_sizailotno2_2 = ?,potto1_tyougouryou2_2 = ?,potto1_zairyohinmei3 = ?,potto1_tyogouryoukikaku3 = ?,"
                + "potto1_sizailotno3_1 = ?,potto1_tyougouryou3_1 = ?,potto1_sizailotno3_2 = ?,potto1_tyougouryou3_2 = ?,potto1_zairyohinmei4 = ?,potto1_tyogouryoukikaku4 = ?,"
                + "potto1_sizailotno4_1 = ?,potto1_tyougouryou4_1 = ?,potto1_sizailotno4_2 = ?,potto1_tyougouryou4_2 = ?,potto2_zairyohinmei1 = ?,potto2_tyogouryoukikaku1 = ?,"
                + "potto2_sizailotno1_1 = ?,potto2_tyougouryou1_1 = ?,potto2_sizailotno1_2 = ?,potto2_tyougouryou1_2 = ?,potto2_zairyohinmei2 = ?,potto2_tyogouryoukikaku2 = ?,"
                + "potto2_sizailotno2_1 = ?,potto2_tyougouryou2_1 = ?,potto2_sizailotno2_2 = ?,potto2_tyougouryou2_2 = ?,potto2_zairyohinmei3 = ?,potto2_tyogouryoukikaku3 = ?,"
                + "potto2_sizailotno3_1 = ?,potto2_tyougouryou3_1 = ?,potto2_sizailotno3_2 = ?,potto2_tyougouryou3_2 = ?,potto2_zairyohinmei4 = ?,potto2_tyogouryoukikaku4 = ?,"
                + "potto2_sizailotno4_1 = ?,potto2_tyougouryou4_1 = ?,potto2_sizailotno4_2 = ?,potto2_tyougouryou4_2 = ?,potto3_zairyohinmei1 = ?,potto3_tyogouryoukikaku1 = ?,"
                + "potto3_sizailotno1_1 = ?,potto3_tyougouryou1_1 = ?,potto3_sizailotno1_2 = ?,potto3_tyougouryou1_2 = ?,potto3_zairyohinmei2 = ?,potto3_tyogouryoukikaku2 = ?,"
                + "potto3_sizailotno2_1 = ?,potto3_tyougouryou2_1 = ?,potto3_sizailotno2_2 = ?,potto3_tyougouryou2_2 = ?,potto3_zairyohinmei3 = ?,potto3_tyogouryoukikaku3 = ?,"
                + "potto3_sizailotno3_1 = ?,potto3_tyougouryou3_1 = ?,potto3_sizailotno3_2 = ?,potto3_tyougouryou3_2 = ?,potto3_zairyohinmei4 = ?,potto3_tyogouryoukikaku4 = ?,"
                + "potto3_sizailotno4_1 = ?,potto3_tyougouryou4_1 = ?,potto3_sizailotno4_2 = ?,potto3_tyougouryou4_2 = ?,potto4_zairyohinmei1 = ?,potto4_tyogouryoukikaku1 = ?,"
                + "potto4_sizailotno1_1 = ?,potto4_tyougouryou1_1 = ?,potto4_sizailotno1_2 = ?,potto4_tyougouryou1_2 = ?,potto4_zairyohinmei2 = ?,potto4_tyogouryoukikaku2 = ?,"
                + "potto4_sizailotno2_1 = ?,potto4_tyougouryou2_1 = ?,potto4_sizailotno2_2 = ?,potto4_tyougouryou2_2 = ?,potto4_zairyohinmei3 = ?,potto4_tyogouryoukikaku3 = ?,"
                + "potto4_sizailotno3_1 = ?,potto4_tyougouryou3_1 = ?,potto4_sizailotno3_2 = ?,potto4_tyougouryou3_2 = ?,potto4_zairyohinmei4 = ?,potto4_tyogouryoukikaku4 = ?,"
                + "potto4_sizailotno4_1 = ?,potto4_tyougouryou4_1 = ?,potto4_sizailotno4_2 = ?,potto4_tyougouryou4_2 = ?,potto5_zairyohinmei1 = ?,potto5_tyogouryoukikaku1 = ?,"
                + "potto5_sizailotno1_1 = ?,potto5_tyougouryou1_1 = ?,potto5_sizailotno1_2 = ?,potto5_tyougouryou1_2 = ?,potto5_zairyohinmei2 = ?,potto5_tyogouryoukikaku2 = ?,"
                + "potto5_sizailotno2_1 = ?,potto5_tyougouryou2_1 = ?,potto5_sizailotno2_2 = ?,potto5_tyougouryou2_2 = ?,potto5_zairyohinmei3 = ?,potto5_tyogouryoukikaku3 = ?,"
                + "potto5_sizailotno3_1 = ?,potto5_tyougouryou3_1 = ?,potto5_sizailotno3_2 = ?,potto5_tyougouryou3_2 = ?,potto5_zairyohinmei4 = ?,potto5_tyogouryoukikaku4 = ?,"
                + "potto5_sizailotno4_1 = ?,potto5_tyougouryou4_1 = ?,potto5_sizailotno4_2 = ?,potto5_tyougouryou4_2 = ?,potto6_zairyohinmei1 = ?,potto6_tyogouryoukikaku1 = ?,"
                + "potto6_sizailotno1_1 = ?,potto6_tyougouryou1_1 = ?,potto6_sizailotno1_2 = ?,potto6_tyougouryou1_2 = ?,potto6_zairyohinmei2 = ?,potto6_tyogouryoukikaku2 = ?,"
                + "potto6_sizailotno2_1 = ?,potto6_tyougouryou2_1 = ?,potto6_sizailotno2_2 = ?,potto6_tyougouryou2_2 = ?,potto6_zairyohinmei3 = ?,potto6_tyogouryoukikaku3 = ?,"
                + "potto6_sizailotno3_1 = ?,potto6_tyougouryou3_1 = ?,potto6_sizailotno3_2 = ?,potto6_tyougouryou3_2 = ?,potto6_zairyohinmei4 = ?,potto6_tyogouryoukikaku4 = ?,"
                + "potto6_sizailotno4_1 = ?,potto6_tyougouryou4_1 = ?,potto6_sizailotno4_2 = ?,potto6_tyougouryou4_2 = ?,potto7_zairyohinmei1 = ?,potto7_tyogouryoukikaku1 = ?,"
                + "potto7_sizailotno1_1 = ?,potto7_tyougouryou1_1 = ?,potto7_sizailotno1_2 = ?,potto7_tyougouryou1_2 = ?,potto7_zairyohinmei2 = ?,potto7_tyogouryoukikaku2 = ?,"
                + "potto7_sizailotno2_1 = ?,potto7_tyougouryou2_1 = ?,potto7_sizailotno2_2 = ?,potto7_tyougouryou2_2 = ?,potto7_zairyohinmei3 = ?,potto7_tyogouryoukikaku3 = ?,"
                + "potto7_sizailotno3_1 = ?,potto7_tyougouryou3_1 = ?,potto7_sizailotno3_2 = ?,potto7_tyougouryou3_2 = ?,potto7_zairyohinmei4 = ?,potto7_tyogouryoukikaku4 = ?,"
                + "potto7_sizailotno4_1 = ?,potto7_tyougouryou4_1 = ?,potto7_sizailotno4_2 = ?,potto7_tyougouryou4_2 = ?,potto8_zairyohinmei1 = ?,potto8_tyogouryoukikaku1 = ?,"
                + "potto8_sizailotno1_1 = ?,potto8_tyougouryou1_1 = ?,potto8_sizailotno1_2 = ?,potto8_tyougouryou1_2 = ?,potto8_zairyohinmei2 = ?,potto8_tyogouryoukikaku2 = ?,"
                + "potto8_sizailotno2_1 = ?,potto8_tyougouryou2_1 = ?,potto8_sizailotno2_2 = ?,potto8_tyougouryou2_2 = ?,potto8_zairyohinmei3 = ?,potto8_tyogouryoukikaku3 = ?,"
                + "potto8_sizailotno3_1 = ?,potto8_tyougouryou3_1 = ?,potto8_sizailotno3_2 = ?,potto8_tyougouryou3_2 = ?,potto8_zairyohinmei4 = ?,potto8_tyogouryoukikaku4 = ?,"
                + "potto8_sizailotno4_1 = ?,potto8_tyougouryou4_1 = ?,potto8_sizailotno4_2 = ?,potto8_tyougouryou4_2 = ?,hyouryounichiji = ?,tantousya = ?,kakuninsya = ?,bikou1 = ?,"
                + "bikou2 = ?,kosinnichiji = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrGlassslurryhyoryo> srGlassslurryhyoryoList = getSrGlassslurryhyoryoData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrGlassslurryhyoryo srGlassslurryhyoryo = null;
        if (!srGlassslurryhyoryoList.isEmpty()) {
            srGlassslurryhyoryo = srGlassslurryhyoryoList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrGlassslurryhyoryo(false, newRev, "", "", "", systemTime, processData, srGlassslurryhyoryo);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ作製・秤量(sr_glassslurryhyoryo)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srGlassslurryhyoryo ｶﾞﾗｽｽﾗﾘｰ作製・秤量データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrGlassslurryhyoryo(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            String systemTime, ProcessData processData, SrGlassslurryhyoryo srGlassslurryhyoryo) {

        List<FXHDD01> pItemList = processData.getItemList();
        List<FXHDD01> pItemListEx = processData.getItemListEx();

        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }

        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B005Const.SEIHIN_GLASSSLURRYHINMEI, srGlassslurryhyoryo)));       // ｶﾞﾗｽｽﾗﾘｰ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B005Const.SEIHIN_GLASSSLURRYLOTNO, srGlassslurryhyoryo)));        // ｶﾞﾗｽｽﾗﾘｰ品名LotNo
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B005Const.SEIHIN_LOT_KUBUN, srGlassslurryhyoryo)));               // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B005Const.SEIHIN_FUSAIPOTTOSIZE, srGlassslurryhyoryo)));     // 粉砕ﾎﾟｯﾄｻｲｽﾞ
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B005Const.SEIHIN_TAMAISHIKEI, srGlassslurryhyoryo)));        // 玉石径
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B005Const.SEIHIN_GOKI, srGlassslurryhyoryo)));                    // 秤量号機
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO1_ZAIRYOHINMEI1, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ1_材料品名1
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU1, srGlassslurryhyoryo)));// ﾎﾟｯﾄ1_調合量規格1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO1_SIZAILOTNO1_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.1_1
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO1_TYOUGOURYOU1_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ1_調合量1_1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO1_SIZAILOTNO1_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.1_2
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO1_TYOUGOURYOU1_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ1_調合量1_2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO1_ZAIRYOHINMEI2, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ1_材料品名2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU2, srGlassslurryhyoryo)));// ﾎﾟｯﾄ1_調合量規格2
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO1_SIZAILOTNO2_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.2_1
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO1_TYOUGOURYOU2_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ1_調合量2_1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO1_SIZAILOTNO2_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.2_2
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO1_TYOUGOURYOU2_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ1_調合量2_2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO1_ZAIRYOHINMEI3, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ1_材料品名3
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU3, srGlassslurryhyoryo)));// ﾎﾟｯﾄ1_調合量規格3
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO1_SIZAILOTNO3_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.3_1
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO1_TYOUGOURYOU3_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ1_調合量3_1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO1_SIZAILOTNO3_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.3_2
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO1_TYOUGOURYOU3_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ1_調合量3_2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO1_ZAIRYOHINMEI4, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ1_材料品名4
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU4, srGlassslurryhyoryo)));// ﾎﾟｯﾄ1_調合量規格4
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO1_SIZAILOTNO4_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.4_1
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO1_TYOUGOURYOU4_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ1_調合量4_1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO1_SIZAILOTNO4_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.4_2
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO1_TYOUGOURYOU4_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ1_調合量4_2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO2_ZAIRYOHINMEI1, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ2_材料品名1
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU1, srGlassslurryhyoryo)));// ﾎﾟｯﾄ2_調合量規格1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO2_SIZAILOTNO1_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.1_1
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO2_TYOUGOURYOU1_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ2_調合量1_1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO2_SIZAILOTNO1_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.1_2
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO2_TYOUGOURYOU1_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ2_調合量1_2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO2_ZAIRYOHINMEI2, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ2_材料品名2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU2, srGlassslurryhyoryo)));// ﾎﾟｯﾄ2_調合量規格2
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO2_SIZAILOTNO2_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.2_1
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO2_TYOUGOURYOU2_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ2_調合量2_1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO2_SIZAILOTNO2_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.2_2
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO2_TYOUGOURYOU2_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ2_調合量2_2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO2_ZAIRYOHINMEI3, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ2_材料品名3
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU3, srGlassslurryhyoryo)));// ﾎﾟｯﾄ2_調合量規格3
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO2_SIZAILOTNO3_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.3_1
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO2_TYOUGOURYOU3_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ2_調合量3_1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO2_SIZAILOTNO3_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.3_2
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO2_TYOUGOURYOU3_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ2_調合量3_2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO2_ZAIRYOHINMEI4, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ2_材料品名4
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU4, srGlassslurryhyoryo)));// ﾎﾟｯﾄ2_調合量規格4
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO2_SIZAILOTNO4_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.4_1
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO2_TYOUGOURYOU4_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ2_調合量4_1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO2_SIZAILOTNO4_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.4_2
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO2_TYOUGOURYOU4_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ2_調合量4_2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO3_ZAIRYOHINMEI1, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ3_材料品名1
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU1, srGlassslurryhyoryo)));// ﾎﾟｯﾄ3_調合量規格1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO3_SIZAILOTNO1_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.1_1
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO3_TYOUGOURYOU1_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ3_調合量1_1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO3_SIZAILOTNO1_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.1_2
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO3_TYOUGOURYOU1_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ3_調合量1_2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO3_ZAIRYOHINMEI2, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ3_材料品名2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU2, srGlassslurryhyoryo)));// ﾎﾟｯﾄ3_調合量規格2
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO3_SIZAILOTNO2_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.2_1
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO3_TYOUGOURYOU2_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ3_調合量2_1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO3_SIZAILOTNO2_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.2_2
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO3_TYOUGOURYOU2_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ3_調合量2_2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO3_ZAIRYOHINMEI3, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ3_材料品名3
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU3, srGlassslurryhyoryo)));// ﾎﾟｯﾄ3_調合量規格3
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO3_SIZAILOTNO3_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.3_1
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO3_TYOUGOURYOU3_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ3_調合量3_1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO3_SIZAILOTNO3_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.3_2
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO3_TYOUGOURYOU3_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ3_調合量3_2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO3_ZAIRYOHINMEI4, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ3_材料品名4
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU4, srGlassslurryhyoryo)));// ﾎﾟｯﾄ3_調合量規格4
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO3_SIZAILOTNO4_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.4_1
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO3_TYOUGOURYOU4_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ3_調合量4_1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO3_SIZAILOTNO4_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.4_2
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO3_TYOUGOURYOU4_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ3_調合量4_2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO4_ZAIRYOHINMEI1, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ4_材料品名1
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU1, srGlassslurryhyoryo)));// ﾎﾟｯﾄ4_調合量規格1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO4_SIZAILOTNO1_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.1_1
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO4_TYOUGOURYOU1_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ4_調合量1_1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO4_SIZAILOTNO1_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.1_2
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO4_TYOUGOURYOU1_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ4_調合量1_2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO4_ZAIRYOHINMEI2, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ4_材料品名2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU2, srGlassslurryhyoryo)));// ﾎﾟｯﾄ4_調合量規格2
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO4_SIZAILOTNO2_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.2_1
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO4_TYOUGOURYOU2_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ4_調合量2_1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO4_SIZAILOTNO2_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.2_2
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO4_TYOUGOURYOU2_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ4_調合量2_2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO4_ZAIRYOHINMEI3, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ4_材料品名3
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU3, srGlassslurryhyoryo)));// ﾎﾟｯﾄ4_調合量規格3
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO4_SIZAILOTNO3_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.3_1
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO4_TYOUGOURYOU3_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ4_調合量3_1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO4_SIZAILOTNO3_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.3_2
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO4_TYOUGOURYOU3_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ4_調合量3_2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO4_ZAIRYOHINMEI4, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ4_材料品名4
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU4, srGlassslurryhyoryo)));// ﾎﾟｯﾄ4_調合量規格4
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO4_SIZAILOTNO4_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.4_1
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO4_TYOUGOURYOU4_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ4_調合量4_1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO4_SIZAILOTNO4_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.4_2
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO4_TYOUGOURYOU4_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ4_調合量4_2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO5_ZAIRYOHINMEI1, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ5_材料品名1
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU1, srGlassslurryhyoryo)));// ﾎﾟｯﾄ5_調合量規格1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO5_SIZAILOTNO1_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.1_1
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO5_TYOUGOURYOU1_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ5_調合量1_1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO5_SIZAILOTNO1_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.1_2
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO5_TYOUGOURYOU1_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ5_調合量1_2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO5_ZAIRYOHINMEI2, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ5_材料品名2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU2, srGlassslurryhyoryo)));// ﾎﾟｯﾄ5_調合量規格2
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO5_SIZAILOTNO2_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.2_1
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO5_TYOUGOURYOU2_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ5_調合量2_1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO5_SIZAILOTNO2_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.2_2
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO5_TYOUGOURYOU2_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ5_調合量2_2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO5_ZAIRYOHINMEI3, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ5_材料品名3
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU3, srGlassslurryhyoryo)));// ﾎﾟｯﾄ5_調合量規格3
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO5_SIZAILOTNO3_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.3_1
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO5_TYOUGOURYOU3_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ5_調合量3_1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO5_SIZAILOTNO3_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.3_2
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO5_TYOUGOURYOU3_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ5_調合量3_2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO5_ZAIRYOHINMEI4, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ5_材料品名4
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU4, srGlassslurryhyoryo)));// ﾎﾟｯﾄ5_調合量規格4
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO5_SIZAILOTNO4_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.4_1
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO5_TYOUGOURYOU4_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ5_調合量4_1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO5_SIZAILOTNO4_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.4_2
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO5_TYOUGOURYOU4_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ5_調合量4_2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO6_ZAIRYOHINMEI1, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ6_材料品名1
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU1, srGlassslurryhyoryo)));// ﾎﾟｯﾄ6_調合量規格1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO6_SIZAILOTNO1_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.1_1
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO6_TYOUGOURYOU1_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ6_調合量1_1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO6_SIZAILOTNO1_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.1_2
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO6_TYOUGOURYOU1_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ6_調合量1_2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO6_ZAIRYOHINMEI2, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ6_材料品名2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU2, srGlassslurryhyoryo)));// ﾎﾟｯﾄ6_調合量規格2
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO6_SIZAILOTNO2_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.2_1
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO6_TYOUGOURYOU2_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ6_調合量2_1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO6_SIZAILOTNO2_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.2_2
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO6_TYOUGOURYOU2_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ6_調合量2_2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO6_ZAIRYOHINMEI3, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ6_材料品名3
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU3, srGlassslurryhyoryo)));// ﾎﾟｯﾄ6_調合量規格3
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO6_SIZAILOTNO3_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.3_1
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO6_TYOUGOURYOU3_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ6_調合量3_1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO6_SIZAILOTNO3_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.3_2
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO6_TYOUGOURYOU3_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ6_調合量3_2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO6_ZAIRYOHINMEI4, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ6_材料品名4
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU4, srGlassslurryhyoryo)));// ﾎﾟｯﾄ6_調合量規格4
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO6_SIZAILOTNO4_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.4_1
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO6_TYOUGOURYOU4_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ6_調合量4_1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO6_SIZAILOTNO4_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.4_2
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO6_TYOUGOURYOU4_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ6_調合量4_2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO7_ZAIRYOHINMEI1, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ7_材料品名1
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU1, srGlassslurryhyoryo)));// ﾎﾟｯﾄ7_調合量規格1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO7_SIZAILOTNO1_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.1_1
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO7_TYOUGOURYOU1_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ7_調合量1_1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO7_SIZAILOTNO1_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.1_2
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO7_TYOUGOURYOU1_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ7_調合量1_2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO7_ZAIRYOHINMEI2, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ7_材料品名2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU2, srGlassslurryhyoryo)));// ﾎﾟｯﾄ7_調合量規格2
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO7_SIZAILOTNO2_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.2_1
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO7_TYOUGOURYOU2_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ7_調合量2_1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO7_SIZAILOTNO2_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.2_2
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO7_TYOUGOURYOU2_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ7_調合量2_2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO7_ZAIRYOHINMEI3, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ7_材料品名3
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU3, srGlassslurryhyoryo)));// ﾎﾟｯﾄ7_調合量規格3
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO7_SIZAILOTNO3_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.3_1
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO7_TYOUGOURYOU3_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ7_調合量3_1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO7_SIZAILOTNO3_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.3_2
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO7_TYOUGOURYOU3_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ7_調合量3_2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO7_ZAIRYOHINMEI4, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ7_材料品名4
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU4, srGlassslurryhyoryo)));// ﾎﾟｯﾄ7_調合量規格4
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO7_SIZAILOTNO4_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.4_1
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO7_TYOUGOURYOU4_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ7_調合量4_1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO7_SIZAILOTNO4_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.4_2
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO7_TYOUGOURYOU4_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ7_調合量4_2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO8_ZAIRYOHINMEI1, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ8_材料品名1
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU1, srGlassslurryhyoryo)));// ﾎﾟｯﾄ8_調合量規格1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO8_SIZAILOTNO1_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.1_1
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO8_TYOUGOURYOU1_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ8_調合量1_1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO8_SIZAILOTNO1_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.1_2
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO8_TYOUGOURYOU1_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ8_調合量1_2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO8_ZAIRYOHINMEI2, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ8_材料品名2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU2, srGlassslurryhyoryo)));// ﾎﾟｯﾄ8_調合量規格2
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO8_SIZAILOTNO2_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.2_1
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO8_TYOUGOURYOU2_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ8_調合量2_1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO8_SIZAILOTNO2_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.2_2
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO8_TYOUGOURYOU2_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ8_調合量2_2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO8_ZAIRYOHINMEI3, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ8_材料品名3
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU3, srGlassslurryhyoryo)));// ﾎﾟｯﾄ8_調合量規格3
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO8_SIZAILOTNO3_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.3_1
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO8_TYOUGOURYOU3_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ8_調合量3_1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO8_SIZAILOTNO3_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.3_2
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO8_TYOUGOURYOU3_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ8_調合量3_2
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO8_ZAIRYOHINMEI4, srGlassslurryhyoryo)));    // ﾎﾟｯﾄ8_材料品名4
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemListEx, GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU4, srGlassslurryhyoryo)));// ﾎﾟｯﾄ8_調合量規格4
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO8_SIZAILOTNO4_1, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.4_1
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO8_TYOUGOURYOU4_1, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ8_調合量4_1
        params.add(DBUtil.stringToStringObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO8_SIZAILOTNO4_2, srGlassslurryhyoryo)));         // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.4_2
        params.add(DBUtil.stringToIntObject(getItemData(pItemListEx, GXHDO102B005Const.POTTO8_TYOUGOURYOU4_2, srGlassslurryhyoryo)));           // ﾎﾟｯﾄ8_調合量4_2
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B005Const.SEIHIN_HYOURYOUNICHIJI_DAY, srGlassslurryhyoryo),
                getItemData(pItemList, GXHDO102B005Const.SEIHIN_HYOURYOUNICHIJI_TIME, srGlassslurryhyoryo)));                                   // 秤量日時
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B005Const.SEIHIN_TANTOUSYA, srGlassslurryhyoryo)));               // 担当者
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B005Const.SEIHIN_KAKUNINSYA, srGlassslurryhyoryo)));              // 確認者
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B005Const.SEIHIN_BIKOU1, srGlassslurryhyoryo)));                  // 備考1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B005Const.SEIHIN_BIKOU2, srGlassslurryhyoryo)));                  // 備考2

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
     * ｶﾞﾗｽｽﾗﾘｰ作製・秤量(sr_glassslurryhyoryo)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSrGlassslurryhyoryo(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sr_glassslurryhyoryo "
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
     * [ｶﾞﾗｽｽﾗﾘｰ作製・秤量_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_glassslurryhyoryo "
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
     * @param srGlassslurryhyoryo ｶﾞﾗｽｽﾗﾘｰ作製・秤量データ
     * @return DB値
     */
    private String getGlassslurryhyoryoItemData(String itemId, SrGlassslurryhyoryo srGlassslurryhyoryo) {
        switch (itemId) {
            //製品情報:ｶﾞﾗｽｽﾗﾘｰ品名
            case GXHDO102B005Const.SEIHIN_GLASSSLURRYHINMEI:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getGlassslurryhinmei());

            //製品情報:ｶﾞﾗｽｽﾗﾘｰ品名LotNo
            case GXHDO102B005Const.SEIHIN_GLASSSLURRYLOTNO:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getGlassslurrylotno());

            //製品情報:ﾛｯﾄ区分
            case GXHDO102B005Const.SEIHIN_LOT_KUBUN:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getLotkubun());

            //製品情報:粉砕ﾎﾟｯﾄｻｲｽﾞ
            case GXHDO102B005Const.SEIHIN_FUSAIPOTTOSIZE:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getFusaipottosize());

            //製品情報:玉石径
            case GXHDO102B005Const.SEIHIN_TAMAISHIKEI:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getTamaishikei());

            //製品情報:秤量号機
            case GXHDO102B005Const.SEIHIN_GOKI:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getGoki());

            //製品情報:秤量日
            case GXHDO102B005Const.SEIHIN_HYOURYOUNICHIJI_DAY:
                return DateUtil.formattedTimestamp(srGlassslurryhyoryo.getHyouryounichiji(), "yyMMdd");

            //製品情報:秤量時間
            case GXHDO102B005Const.SEIHIN_HYOURYOUNICHIJI_TIME:
                return DateUtil.formattedTimestamp(srGlassslurryhyoryo.getHyouryounichiji(), "HHmm");

            //製品情報:担当者
            case GXHDO102B005Const.SEIHIN_TANTOUSYA:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getTantousya());

            //製品情報:確認者
            case GXHDO102B005Const.SEIHIN_KAKUNINSYA:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getKakuninsya());

            //製品情報:備考1
            case GXHDO102B005Const.SEIHIN_BIKOU1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getBikou1());

            //製品情報:備考2
            case GXHDO102B005Const.SEIHIN_BIKOU2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getBikou2());

            //ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_材料品名1
            case GXHDO102B005Const.POTTO1_ZAIRYOHINMEI1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto1_zairyohinmei1());

            //ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_調合量規格1
            case GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto1_tyogouryoukikaku1());

            //ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_資材ﾛｯﾄNo.1_1
            case GXHDO102B005Const.POTTO1_SIZAILOTNO1_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto1_sizailotno1_1());

            //ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_調合量1_1
            case GXHDO102B005Const.POTTO1_TYOUGOURYOU1_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto1_tyougouryou1_1());

            //ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_資材ﾛｯﾄNo.1_2
            case GXHDO102B005Const.POTTO1_SIZAILOTNO1_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto1_sizailotno1_2());

            //ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_調合量1_2
            case GXHDO102B005Const.POTTO1_TYOUGOURYOU1_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto1_tyougouryou1_2());

            //ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_材料品名2
            case GXHDO102B005Const.POTTO1_ZAIRYOHINMEI2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto1_zairyohinmei2());

            //ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_調合量規格2
            case GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto1_tyogouryoukikaku2());

            //ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_資材ﾛｯﾄNo.2_1
            case GXHDO102B005Const.POTTO1_SIZAILOTNO2_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto1_sizailotno2_1());

            //ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_調合量2_1
            case GXHDO102B005Const.POTTO1_TYOUGOURYOU2_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto1_tyougouryou2_1());

            //ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_資材ﾛｯﾄNo.2_2
            case GXHDO102B005Const.POTTO1_SIZAILOTNO2_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto1_sizailotno2_2());

            //ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_調合量2_2
            case GXHDO102B005Const.POTTO1_TYOUGOURYOU2_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto1_tyougouryou2_2());

            //ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_材料品名3
            case GXHDO102B005Const.POTTO1_ZAIRYOHINMEI3:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto1_zairyohinmei3());

            //ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_調合量規格3
            case GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU3:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto1_tyogouryoukikaku3());

            //ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_資材ﾛｯﾄNo.3_1
            case GXHDO102B005Const.POTTO1_SIZAILOTNO3_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto1_sizailotno3_1());

            //ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_調合量3_1
            case GXHDO102B005Const.POTTO1_TYOUGOURYOU3_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto1_tyougouryou3_1());

            //ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_資材ﾛｯﾄNo.3_2
            case GXHDO102B005Const.POTTO1_SIZAILOTNO3_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto1_sizailotno3_2());

            //ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_調合量3_2
            case GXHDO102B005Const.POTTO1_TYOUGOURYOU3_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto1_tyougouryou3_2());

            //ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_材料品名4
            case GXHDO102B005Const.POTTO1_ZAIRYOHINMEI4:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto1_zairyohinmei4());

            //ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_調合量規格4
            case GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU4:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto1_tyogouryoukikaku4());

            //ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_資材ﾛｯﾄNo.4_1
            case GXHDO102B005Const.POTTO1_SIZAILOTNO4_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto1_sizailotno4_1());

            //ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_調合量4_1
            case GXHDO102B005Const.POTTO1_TYOUGOURYOU4_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto1_tyougouryou4_1());

            //ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_資材ﾛｯﾄNo.4_2
            case GXHDO102B005Const.POTTO1_SIZAILOTNO4_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto1_sizailotno4_2());

            //ﾎﾟｯﾄ1:ﾎﾟｯﾄ1_調合量4_2
            case GXHDO102B005Const.POTTO1_TYOUGOURYOU4_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto1_tyougouryou4_2());

            //ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_材料品名1
            case GXHDO102B005Const.POTTO2_ZAIRYOHINMEI1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto2_zairyohinmei1());

            //ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_調合量規格1
            case GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto2_tyogouryoukikaku1());

            //ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_資材ﾛｯﾄNo.1_1
            case GXHDO102B005Const.POTTO2_SIZAILOTNO1_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto2_sizailotno1_1());

            //ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_調合量1_1
            case GXHDO102B005Const.POTTO2_TYOUGOURYOU1_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto2_tyougouryou1_1());

            //ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_資材ﾛｯﾄNo.1_2
            case GXHDO102B005Const.POTTO2_SIZAILOTNO1_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto2_sizailotno1_2());

            //ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_調合量1_2
            case GXHDO102B005Const.POTTO2_TYOUGOURYOU1_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto2_tyougouryou1_2());

            //ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_材料品名2
            case GXHDO102B005Const.POTTO2_ZAIRYOHINMEI2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto2_zairyohinmei2());

            //ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_調合量規格2
            case GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto2_tyogouryoukikaku2());

            //ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_資材ﾛｯﾄNo.2_1
            case GXHDO102B005Const.POTTO2_SIZAILOTNO2_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto2_sizailotno2_1());

            //ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_調合量2_1
            case GXHDO102B005Const.POTTO2_TYOUGOURYOU2_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto2_tyougouryou2_1());

            //ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_資材ﾛｯﾄNo.2_2
            case GXHDO102B005Const.POTTO2_SIZAILOTNO2_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto2_sizailotno2_2());

            //ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_調合量2_2
            case GXHDO102B005Const.POTTO2_TYOUGOURYOU2_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto2_tyougouryou2_2());

            //ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_材料品名3
            case GXHDO102B005Const.POTTO2_ZAIRYOHINMEI3:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto2_zairyohinmei3());

            //ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_調合量規格3
            case GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU3:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto2_tyogouryoukikaku3());

            //ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_資材ﾛｯﾄNo.3_1
            case GXHDO102B005Const.POTTO2_SIZAILOTNO3_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto2_sizailotno3_1());

            //ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_調合量3_1
            case GXHDO102B005Const.POTTO2_TYOUGOURYOU3_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto2_tyougouryou3_1());

            //ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_資材ﾛｯﾄNo.3_2
            case GXHDO102B005Const.POTTO2_SIZAILOTNO3_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto2_sizailotno3_2());

            //ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_調合量3_2
            case GXHDO102B005Const.POTTO2_TYOUGOURYOU3_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto2_tyougouryou3_2());

            //ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_材料品名4
            case GXHDO102B005Const.POTTO2_ZAIRYOHINMEI4:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto2_zairyohinmei4());

            //ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_調合量規格4
            case GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU4:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto2_tyogouryoukikaku4());

            //ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_資材ﾛｯﾄNo.4_1
            case GXHDO102B005Const.POTTO2_SIZAILOTNO4_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto2_sizailotno4_1());

            //ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_調合量4_1
            case GXHDO102B005Const.POTTO2_TYOUGOURYOU4_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto2_tyougouryou4_1());

            //ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_資材ﾛｯﾄNo.4_2
            case GXHDO102B005Const.POTTO2_SIZAILOTNO4_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto2_sizailotno4_2());

            //ﾎﾟｯﾄ2:ﾎﾟｯﾄ2_調合量4_2
            case GXHDO102B005Const.POTTO2_TYOUGOURYOU4_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto2_tyougouryou4_2());

            //ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_材料品名1
            case GXHDO102B005Const.POTTO3_ZAIRYOHINMEI1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto3_zairyohinmei1());

            //ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_調合量規格1
            case GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto3_tyogouryoukikaku1());

            //ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_資材ﾛｯﾄNo.1_1
            case GXHDO102B005Const.POTTO3_SIZAILOTNO1_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto3_sizailotno1_1());

            //ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_調合量1_1
            case GXHDO102B005Const.POTTO3_TYOUGOURYOU1_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto3_tyougouryou1_1());

            //ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_資材ﾛｯﾄNo.1_2
            case GXHDO102B005Const.POTTO3_SIZAILOTNO1_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto3_sizailotno1_2());

            //ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_調合量1_2
            case GXHDO102B005Const.POTTO3_TYOUGOURYOU1_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto3_tyougouryou1_2());

            //ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_材料品名2
            case GXHDO102B005Const.POTTO3_ZAIRYOHINMEI2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto3_zairyohinmei2());

            //ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_調合量規格2
            case GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto3_tyogouryoukikaku2());

            //ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_資材ﾛｯﾄNo.2_1
            case GXHDO102B005Const.POTTO3_SIZAILOTNO2_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto3_sizailotno2_1());

            //ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_調合量2_1
            case GXHDO102B005Const.POTTO3_TYOUGOURYOU2_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto3_tyougouryou2_1());

            //ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_資材ﾛｯﾄNo.2_2
            case GXHDO102B005Const.POTTO3_SIZAILOTNO2_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto3_sizailotno2_2());

            //ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_調合量2_2
            case GXHDO102B005Const.POTTO3_TYOUGOURYOU2_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto3_tyougouryou2_2());

            //ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_材料品名3
            case GXHDO102B005Const.POTTO3_ZAIRYOHINMEI3:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto3_zairyohinmei3());

            //ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_調合量規格3
            case GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU3:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto3_tyogouryoukikaku3());

            //ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_資材ﾛｯﾄNo.3_1
            case GXHDO102B005Const.POTTO3_SIZAILOTNO3_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto3_sizailotno3_1());

            //ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_調合量3_1
            case GXHDO102B005Const.POTTO3_TYOUGOURYOU3_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto3_tyougouryou3_1());

            //ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_資材ﾛｯﾄNo.3_2
            case GXHDO102B005Const.POTTO3_SIZAILOTNO3_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto3_sizailotno3_2());

            //ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_調合量3_2
            case GXHDO102B005Const.POTTO3_TYOUGOURYOU3_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto3_tyougouryou3_2());

            //ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_材料品名4
            case GXHDO102B005Const.POTTO3_ZAIRYOHINMEI4:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto3_zairyohinmei4());

            //ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_調合量規格4
            case GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU4:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto3_tyogouryoukikaku4());

            //ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_資材ﾛｯﾄNo.4_1
            case GXHDO102B005Const.POTTO3_SIZAILOTNO4_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto3_sizailotno4_1());

            //ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_調合量4_1
            case GXHDO102B005Const.POTTO3_TYOUGOURYOU4_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto3_tyougouryou4_1());

            //ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_資材ﾛｯﾄNo.4_2
            case GXHDO102B005Const.POTTO3_SIZAILOTNO4_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto3_sizailotno4_2());

            //ﾎﾟｯﾄ3:ﾎﾟｯﾄ3_調合量4_2
            case GXHDO102B005Const.POTTO3_TYOUGOURYOU4_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto3_tyougouryou4_2());

            //ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_材料品名1
            case GXHDO102B005Const.POTTO4_ZAIRYOHINMEI1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto4_zairyohinmei1());

            //ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_調合量規格1
            case GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto4_tyogouryoukikaku1());

            //ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_資材ﾛｯﾄNo.1_1
            case GXHDO102B005Const.POTTO4_SIZAILOTNO1_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto4_sizailotno1_1());

            //ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_調合量1_1
            case GXHDO102B005Const.POTTO4_TYOUGOURYOU1_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto4_tyougouryou1_1());

            //ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_資材ﾛｯﾄNo.1_2
            case GXHDO102B005Const.POTTO4_SIZAILOTNO1_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto4_sizailotno1_2());

            //ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_調合量1_2
            case GXHDO102B005Const.POTTO4_TYOUGOURYOU1_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto4_tyougouryou1_2());

            //ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_材料品名2
            case GXHDO102B005Const.POTTO4_ZAIRYOHINMEI2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto4_zairyohinmei2());

            //ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_調合量規格2
            case GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto4_tyogouryoukikaku2());

            //ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_資材ﾛｯﾄNo.2_1
            case GXHDO102B005Const.POTTO4_SIZAILOTNO2_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto4_sizailotno2_1());

            //ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_調合量2_1
            case GXHDO102B005Const.POTTO4_TYOUGOURYOU2_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto4_tyougouryou2_1());

            //ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_資材ﾛｯﾄNo.2_2
            case GXHDO102B005Const.POTTO4_SIZAILOTNO2_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto4_sizailotno2_2());

            //ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_調合量2_2
            case GXHDO102B005Const.POTTO4_TYOUGOURYOU2_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto4_tyougouryou2_2());

            //ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_材料品名3
            case GXHDO102B005Const.POTTO4_ZAIRYOHINMEI3:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto4_zairyohinmei3());

            //ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_調合量規格3
            case GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU3:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto4_tyogouryoukikaku3());

            //ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_資材ﾛｯﾄNo.3_1
            case GXHDO102B005Const.POTTO4_SIZAILOTNO3_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto4_sizailotno3_1());

            //ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_調合量3_1
            case GXHDO102B005Const.POTTO4_TYOUGOURYOU3_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto4_tyougouryou3_1());

            //ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_資材ﾛｯﾄNo.3_2
            case GXHDO102B005Const.POTTO4_SIZAILOTNO3_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto4_sizailotno3_2());

            //ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_調合量3_2
            case GXHDO102B005Const.POTTO4_TYOUGOURYOU3_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto4_tyougouryou3_2());

            //ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_材料品名4
            case GXHDO102B005Const.POTTO4_ZAIRYOHINMEI4:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto4_zairyohinmei4());

            //ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_調合量規格4
            case GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU4:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto4_tyogouryoukikaku4());

            //ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_資材ﾛｯﾄNo.4_1
            case GXHDO102B005Const.POTTO4_SIZAILOTNO4_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto4_sizailotno4_1());

            //ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_調合量4_1
            case GXHDO102B005Const.POTTO4_TYOUGOURYOU4_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto4_tyougouryou4_1());

            //ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_資材ﾛｯﾄNo.4_2
            case GXHDO102B005Const.POTTO4_SIZAILOTNO4_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto4_sizailotno4_2());

            //ﾎﾟｯﾄ4:ﾎﾟｯﾄ4_調合量4_2
            case GXHDO102B005Const.POTTO4_TYOUGOURYOU4_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto4_tyougouryou4_2());

            //ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_材料品名1
            case GXHDO102B005Const.POTTO5_ZAIRYOHINMEI1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto5_zairyohinmei1());

            //ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_調合量規格1
            case GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto5_tyogouryoukikaku1());

            //ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_資材ﾛｯﾄNo.1_1
            case GXHDO102B005Const.POTTO5_SIZAILOTNO1_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto5_sizailotno1_1());

            //ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_調合量1_1
            case GXHDO102B005Const.POTTO5_TYOUGOURYOU1_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto5_tyougouryou1_1());

            //ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_資材ﾛｯﾄNo.1_2
            case GXHDO102B005Const.POTTO5_SIZAILOTNO1_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto5_sizailotno1_2());

            //ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_調合量1_2
            case GXHDO102B005Const.POTTO5_TYOUGOURYOU1_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto5_tyougouryou1_2());

            //ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_材料品名2
            case GXHDO102B005Const.POTTO5_ZAIRYOHINMEI2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto5_zairyohinmei2());

            //ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_調合量規格2
            case GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto5_tyogouryoukikaku2());

            //ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_資材ﾛｯﾄNo.2_1
            case GXHDO102B005Const.POTTO5_SIZAILOTNO2_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto5_sizailotno2_1());

            //ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_調合量2_1
            case GXHDO102B005Const.POTTO5_TYOUGOURYOU2_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto5_tyougouryou2_1());

            //ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_資材ﾛｯﾄNo.2_2
            case GXHDO102B005Const.POTTO5_SIZAILOTNO2_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto5_sizailotno2_2());

            //ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_調合量2_2
            case GXHDO102B005Const.POTTO5_TYOUGOURYOU2_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto5_tyougouryou2_2());

            //ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_材料品名3
            case GXHDO102B005Const.POTTO5_ZAIRYOHINMEI3:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto5_zairyohinmei3());

            //ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_調合量規格3
            case GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU3:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto5_tyogouryoukikaku3());

            //ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_資材ﾛｯﾄNo.3_1
            case GXHDO102B005Const.POTTO5_SIZAILOTNO3_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto5_sizailotno3_1());

            //ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_調合量3_1
            case GXHDO102B005Const.POTTO5_TYOUGOURYOU3_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto5_tyougouryou3_1());

            //ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_資材ﾛｯﾄNo.3_2
            case GXHDO102B005Const.POTTO5_SIZAILOTNO3_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto5_sizailotno3_2());

            //ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_調合量3_2
            case GXHDO102B005Const.POTTO5_TYOUGOURYOU3_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto5_tyougouryou3_2());

            //ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_材料品名4
            case GXHDO102B005Const.POTTO5_ZAIRYOHINMEI4:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto5_zairyohinmei4());

            //ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_調合量規格4
            case GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU4:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto5_tyogouryoukikaku4());

            //ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_資材ﾛｯﾄNo.4_1
            case GXHDO102B005Const.POTTO5_SIZAILOTNO4_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto5_sizailotno4_1());

            //ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_調合量4_1
            case GXHDO102B005Const.POTTO5_TYOUGOURYOU4_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto5_tyougouryou4_1());

            //ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_資材ﾛｯﾄNo.4_2
            case GXHDO102B005Const.POTTO5_SIZAILOTNO4_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto5_sizailotno4_2());

            //ﾎﾟｯﾄ5:ﾎﾟｯﾄ5_調合量4_2
            case GXHDO102B005Const.POTTO5_TYOUGOURYOU4_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto5_tyougouryou4_2());

            //ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_材料品名1
            case GXHDO102B005Const.POTTO6_ZAIRYOHINMEI1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto6_zairyohinmei1());

            //ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_調合量規格1
            case GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto6_tyogouryoukikaku1());

            //ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_資材ﾛｯﾄNo.1_1
            case GXHDO102B005Const.POTTO6_SIZAILOTNO1_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto6_sizailotno1_1());

            //ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_調合量1_1
            case GXHDO102B005Const.POTTO6_TYOUGOURYOU1_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto6_tyougouryou1_1());

            //ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_資材ﾛｯﾄNo.1_2
            case GXHDO102B005Const.POTTO6_SIZAILOTNO1_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto6_sizailotno1_2());

            //ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_調合量1_2
            case GXHDO102B005Const.POTTO6_TYOUGOURYOU1_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto6_tyougouryou1_2());

            //ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_材料品名2
            case GXHDO102B005Const.POTTO6_ZAIRYOHINMEI2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto6_zairyohinmei2());

            //ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_調合量規格2
            case GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto6_tyogouryoukikaku2());

            //ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_資材ﾛｯﾄNo.2_1
            case GXHDO102B005Const.POTTO6_SIZAILOTNO2_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto6_sizailotno2_1());

            //ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_調合量2_1
            case GXHDO102B005Const.POTTO6_TYOUGOURYOU2_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto6_tyougouryou2_1());

            //ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_資材ﾛｯﾄNo.2_2
            case GXHDO102B005Const.POTTO6_SIZAILOTNO2_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto6_sizailotno2_2());

            //ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_調合量2_2
            case GXHDO102B005Const.POTTO6_TYOUGOURYOU2_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto6_tyougouryou2_2());

            //ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_材料品名3
            case GXHDO102B005Const.POTTO6_ZAIRYOHINMEI3:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto6_zairyohinmei3());

            //ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_調合量規格3
            case GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU3:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto6_tyogouryoukikaku3());

            //ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_資材ﾛｯﾄNo.3_1
            case GXHDO102B005Const.POTTO6_SIZAILOTNO3_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto6_sizailotno3_1());

            //ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_調合量3_1
            case GXHDO102B005Const.POTTO6_TYOUGOURYOU3_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto6_tyougouryou3_1());

            //ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_資材ﾛｯﾄNo.3_2
            case GXHDO102B005Const.POTTO6_SIZAILOTNO3_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto6_sizailotno3_2());

            //ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_調合量3_2
            case GXHDO102B005Const.POTTO6_TYOUGOURYOU3_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto6_tyougouryou3_2());

            //ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_材料品名4
            case GXHDO102B005Const.POTTO6_ZAIRYOHINMEI4:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto6_zairyohinmei4());

            //ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_調合量規格4
            case GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU4:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto6_tyogouryoukikaku4());

            //ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_資材ﾛｯﾄNo.4_1
            case GXHDO102B005Const.POTTO6_SIZAILOTNO4_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto6_sizailotno4_1());

            //ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_調合量4_1
            case GXHDO102B005Const.POTTO6_TYOUGOURYOU4_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto6_tyougouryou4_1());

            //ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_資材ﾛｯﾄNo.4_2
            case GXHDO102B005Const.POTTO6_SIZAILOTNO4_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto6_sizailotno4_2());

            //ﾎﾟｯﾄ6:ﾎﾟｯﾄ6_調合量4_2
            case GXHDO102B005Const.POTTO6_TYOUGOURYOU4_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto6_tyougouryou4_2());

            //ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_材料品名1
            case GXHDO102B005Const.POTTO7_ZAIRYOHINMEI1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto7_zairyohinmei1());

            //ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_調合量規格1
            case GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto7_tyogouryoukikaku1());

            //ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_資材ﾛｯﾄNo.1_1
            case GXHDO102B005Const.POTTO7_SIZAILOTNO1_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto7_sizailotno1_1());

            //ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_調合量1_1
            case GXHDO102B005Const.POTTO7_TYOUGOURYOU1_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto7_tyougouryou1_1());

            //ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_資材ﾛｯﾄNo.1_2
            case GXHDO102B005Const.POTTO7_SIZAILOTNO1_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto7_sizailotno1_2());

            //ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_調合量1_2
            case GXHDO102B005Const.POTTO7_TYOUGOURYOU1_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto7_tyougouryou1_2());

            //ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_材料品名2
            case GXHDO102B005Const.POTTO7_ZAIRYOHINMEI2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto7_zairyohinmei2());

            //ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_調合量規格2
            case GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto7_tyogouryoukikaku2());

            //ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_資材ﾛｯﾄNo.2_1
            case GXHDO102B005Const.POTTO7_SIZAILOTNO2_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto7_sizailotno2_1());

            //ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_調合量2_1
            case GXHDO102B005Const.POTTO7_TYOUGOURYOU2_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto7_tyougouryou2_1());

            //ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_資材ﾛｯﾄNo.2_2
            case GXHDO102B005Const.POTTO7_SIZAILOTNO2_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto7_sizailotno2_2());

            //ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_調合量2_2
            case GXHDO102B005Const.POTTO7_TYOUGOURYOU2_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto7_tyougouryou2_2());

            //ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_材料品名3
            case GXHDO102B005Const.POTTO7_ZAIRYOHINMEI3:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto7_zairyohinmei3());

            //ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_調合量規格3
            case GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU3:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto7_tyogouryoukikaku3());

            //ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_資材ﾛｯﾄNo.3_1
            case GXHDO102B005Const.POTTO7_SIZAILOTNO3_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto7_sizailotno3_1());

            //ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_調合量3_1
            case GXHDO102B005Const.POTTO7_TYOUGOURYOU3_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto7_tyougouryou3_1());

            //ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_資材ﾛｯﾄNo.3_2
            case GXHDO102B005Const.POTTO7_SIZAILOTNO3_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto7_sizailotno3_2());

            //ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_調合量3_2
            case GXHDO102B005Const.POTTO7_TYOUGOURYOU3_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto7_tyougouryou3_2());

            //ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_材料品名4
            case GXHDO102B005Const.POTTO7_ZAIRYOHINMEI4:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto7_zairyohinmei4());

            //ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_調合量規格4
            case GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU4:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto7_tyogouryoukikaku4());

            //ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_資材ﾛｯﾄNo.4_1
            case GXHDO102B005Const.POTTO7_SIZAILOTNO4_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto7_sizailotno4_1());

            //ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_調合量4_1
            case GXHDO102B005Const.POTTO7_TYOUGOURYOU4_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto7_tyougouryou4_1());

            //ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_資材ﾛｯﾄNo.4_2
            case GXHDO102B005Const.POTTO7_SIZAILOTNO4_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto7_sizailotno4_2());

            //ﾎﾟｯﾄ7:ﾎﾟｯﾄ7_調合量4_2
            case GXHDO102B005Const.POTTO7_TYOUGOURYOU4_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto7_tyougouryou4_2());

            //ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_材料品名1
            case GXHDO102B005Const.POTTO8_ZAIRYOHINMEI1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto8_zairyohinmei1());

            //ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_調合量規格1
            case GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto8_tyogouryoukikaku1());

            //ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_資材ﾛｯﾄNo.1_1
            case GXHDO102B005Const.POTTO8_SIZAILOTNO1_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto8_sizailotno1_1());

            //ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_調合量1_1
            case GXHDO102B005Const.POTTO8_TYOUGOURYOU1_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto8_tyougouryou1_1());

            //ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_資材ﾛｯﾄNo.1_2
            case GXHDO102B005Const.POTTO8_SIZAILOTNO1_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto8_sizailotno1_2());

            //ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_調合量1_2
            case GXHDO102B005Const.POTTO8_TYOUGOURYOU1_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto8_tyougouryou1_2());

            //ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_材料品名2
            case GXHDO102B005Const.POTTO8_ZAIRYOHINMEI2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto8_zairyohinmei2());

            //ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_調合量規格2
            case GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto8_tyogouryoukikaku2());

            //ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_資材ﾛｯﾄNo.2_1
            case GXHDO102B005Const.POTTO8_SIZAILOTNO2_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto8_sizailotno2_1());

            //ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_調合量2_1
            case GXHDO102B005Const.POTTO8_TYOUGOURYOU2_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto8_tyougouryou2_1());

            //ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_資材ﾛｯﾄNo.2_2
            case GXHDO102B005Const.POTTO8_SIZAILOTNO2_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto8_sizailotno2_2());

            //ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_調合量2_2
            case GXHDO102B005Const.POTTO8_TYOUGOURYOU2_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto8_tyougouryou2_2());

            //ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_材料品名3
            case GXHDO102B005Const.POTTO8_ZAIRYOHINMEI3:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto8_zairyohinmei3());

            //ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_調合量規格3
            case GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU3:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto8_tyogouryoukikaku3());

            //ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_資材ﾛｯﾄNo.3_1
            case GXHDO102B005Const.POTTO8_SIZAILOTNO3_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto8_sizailotno3_1());

            //ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_調合量3_1
            case GXHDO102B005Const.POTTO8_TYOUGOURYOU3_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto8_tyougouryou3_1());

            //ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_資材ﾛｯﾄNo.3_2
            case GXHDO102B005Const.POTTO8_SIZAILOTNO3_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto8_sizailotno3_2());

            //ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_調合量3_2
            case GXHDO102B005Const.POTTO8_TYOUGOURYOU3_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto8_tyougouryou3_2());

            //ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_材料品名4
            case GXHDO102B005Const.POTTO8_ZAIRYOHINMEI4:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto8_zairyohinmei4());

            //ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_調合量規格4
            case GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU4:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto8_tyogouryoukikaku4());

            //ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_資材ﾛｯﾄNo.4_1
            case GXHDO102B005Const.POTTO8_SIZAILOTNO4_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto8_sizailotno4_1());

            //ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_調合量4_1
            case GXHDO102B005Const.POTTO8_TYOUGOURYOU4_1:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto8_tyougouryou4_1());

            //ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_資材ﾛｯﾄNo.4_2
            case GXHDO102B005Const.POTTO8_SIZAILOTNO4_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto8_sizailotno4_2());

            //ﾎﾟｯﾄ8:ﾎﾟｯﾄ8_調合量4_2
            case GXHDO102B005Const.POTTO8_TYOUGOURYOU4_2:
                return StringUtil.nullToBlank(srGlassslurryhyoryo.getPotto8_tyougouryou4_2());

            default:
                return null;
        }
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ作製・秤量_仮登録(tmp_sr_glassslurryhyoryo)登録処理(削除時)
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
    private void insertDeleteDataTmpSrGlassslurryhyoryo(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, String systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_glassslurryhyoryo ("
                + "kojyo,lotno,edaban,glassslurryhinmei,glassslurrylotno,lotkubun,fusaipottosize,tamaishikei,goki,potto1_zairyohinmei1,"
                + "potto1_tyogouryoukikaku1,potto1_sizailotno1_1,potto1_tyougouryou1_1,potto1_sizailotno1_2,potto1_tyougouryou1_2,potto1_zairyohinmei2,"
                + "potto1_tyogouryoukikaku2,potto1_sizailotno2_1,potto1_tyougouryou2_1,potto1_sizailotno2_2,potto1_tyougouryou2_2,potto1_zairyohinmei3,"
                + "potto1_tyogouryoukikaku3,potto1_sizailotno3_1,potto1_tyougouryou3_1,potto1_sizailotno3_2,potto1_tyougouryou3_2,potto1_zairyohinmei4,"
                + "potto1_tyogouryoukikaku4,potto1_sizailotno4_1,potto1_tyougouryou4_1,potto1_sizailotno4_2,potto1_tyougouryou4_2,potto2_zairyohinmei1,"
                + "potto2_tyogouryoukikaku1,potto2_sizailotno1_1,potto2_tyougouryou1_1,potto2_sizailotno1_2,potto2_tyougouryou1_2,potto2_zairyohinmei2,"
                + "potto2_tyogouryoukikaku2,potto2_sizailotno2_1,potto2_tyougouryou2_1,potto2_sizailotno2_2,potto2_tyougouryou2_2,potto2_zairyohinmei3,"
                + "potto2_tyogouryoukikaku3,potto2_sizailotno3_1,potto2_tyougouryou3_1,potto2_sizailotno3_2,potto2_tyougouryou3_2,potto2_zairyohinmei4,"
                + "potto2_tyogouryoukikaku4,potto2_sizailotno4_1,potto2_tyougouryou4_1,potto2_sizailotno4_2,potto2_tyougouryou4_2,potto3_zairyohinmei1,"
                + "potto3_tyogouryoukikaku1,potto3_sizailotno1_1,potto3_tyougouryou1_1,potto3_sizailotno1_2,potto3_tyougouryou1_2,potto3_zairyohinmei2,"
                + "potto3_tyogouryoukikaku2,potto3_sizailotno2_1,potto3_tyougouryou2_1,potto3_sizailotno2_2,potto3_tyougouryou2_2,potto3_zairyohinmei3,"
                + "potto3_tyogouryoukikaku3,potto3_sizailotno3_1,potto3_tyougouryou3_1,potto3_sizailotno3_2,potto3_tyougouryou3_2,potto3_zairyohinmei4,"
                + "potto3_tyogouryoukikaku4,potto3_sizailotno4_1,potto3_tyougouryou4_1,potto3_sizailotno4_2,potto3_tyougouryou4_2,potto4_zairyohinmei1,"
                + "potto4_tyogouryoukikaku1,potto4_sizailotno1_1,potto4_tyougouryou1_1,potto4_sizailotno1_2,potto4_tyougouryou1_2,potto4_zairyohinmei2,"
                + "potto4_tyogouryoukikaku2,potto4_sizailotno2_1,potto4_tyougouryou2_1,potto4_sizailotno2_2,potto4_tyougouryou2_2,potto4_zairyohinmei3,"
                + "potto4_tyogouryoukikaku3,potto4_sizailotno3_1,potto4_tyougouryou3_1,potto4_sizailotno3_2,potto4_tyougouryou3_2,potto4_zairyohinmei4,"
                + "potto4_tyogouryoukikaku4,potto4_sizailotno4_1,potto4_tyougouryou4_1,potto4_sizailotno4_2,potto4_tyougouryou4_2,potto5_zairyohinmei1,"
                + "potto5_tyogouryoukikaku1,potto5_sizailotno1_1,potto5_tyougouryou1_1,potto5_sizailotno1_2,potto5_tyougouryou1_2,potto5_zairyohinmei2,"
                + "potto5_tyogouryoukikaku2,potto5_sizailotno2_1,potto5_tyougouryou2_1,potto5_sizailotno2_2,potto5_tyougouryou2_2,potto5_zairyohinmei3,"
                + "potto5_tyogouryoukikaku3,potto5_sizailotno3_1,potto5_tyougouryou3_1,potto5_sizailotno3_2,potto5_tyougouryou3_2,potto5_zairyohinmei4,"
                + "potto5_tyogouryoukikaku4,potto5_sizailotno4_1,potto5_tyougouryou4_1,potto5_sizailotno4_2,potto5_tyougouryou4_2,potto6_zairyohinmei1,"
                + "potto6_tyogouryoukikaku1,potto6_sizailotno1_1,potto6_tyougouryou1_1,potto6_sizailotno1_2,potto6_tyougouryou1_2,potto6_zairyohinmei2,"
                + "potto6_tyogouryoukikaku2,potto6_sizailotno2_1,potto6_tyougouryou2_1,potto6_sizailotno2_2,potto6_tyougouryou2_2,potto6_zairyohinmei3,"
                + "potto6_tyogouryoukikaku3,potto6_sizailotno3_1,potto6_tyougouryou3_1,potto6_sizailotno3_2,potto6_tyougouryou3_2,potto6_zairyohinmei4,"
                + "potto6_tyogouryoukikaku4,potto6_sizailotno4_1,potto6_tyougouryou4_1,potto6_sizailotno4_2,potto6_tyougouryou4_2,potto7_zairyohinmei1,"
                + "potto7_tyogouryoukikaku1,potto7_sizailotno1_1,potto7_tyougouryou1_1,potto7_sizailotno1_2,potto7_tyougouryou1_2,potto7_zairyohinmei2,"
                + "potto7_tyogouryoukikaku2,potto7_sizailotno2_1,potto7_tyougouryou2_1,potto7_sizailotno2_2,potto7_tyougouryou2_2,potto7_zairyohinmei3,"
                + "potto7_tyogouryoukikaku3,potto7_sizailotno3_1,potto7_tyougouryou3_1,potto7_sizailotno3_2,potto7_tyougouryou3_2,potto7_zairyohinmei4,"
                + "potto7_tyogouryoukikaku4,potto7_sizailotno4_1,potto7_tyougouryou4_1,potto7_sizailotno4_2,potto7_tyougouryou4_2,potto8_zairyohinmei1,"
                + "potto8_tyogouryoukikaku1,potto8_sizailotno1_1,potto8_tyougouryou1_1,potto8_sizailotno1_2,potto8_tyougouryou1_2,potto8_zairyohinmei2,"
                + "potto8_tyogouryoukikaku2,potto8_sizailotno2_1,potto8_tyougouryou2_1,potto8_sizailotno2_2,potto8_tyougouryou2_2,potto8_zairyohinmei3,"
                + "potto8_tyogouryoukikaku3,potto8_sizailotno3_1,potto8_tyougouryou3_1,potto8_sizailotno3_2,potto8_tyougouryou3_2,potto8_zairyohinmei4,"
                + "potto8_tyogouryoukikaku4,potto8_sizailotno4_1,potto8_tyougouryou4_1,potto8_sizailotno4_2,potto8_tyougouryou4_2,hyouryounichiji,tantousya,"
                + "kakuninsya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") SELECT "
                + "kojyo,lotno,edaban,glassslurryhinmei,glassslurrylotno,lotkubun,fusaipottosize,tamaishikei,goki,potto1_zairyohinmei1,"
                + "potto1_tyogouryoukikaku1,potto1_sizailotno1_1,potto1_tyougouryou1_1,potto1_sizailotno1_2,potto1_tyougouryou1_2,potto1_zairyohinmei2,"
                + "potto1_tyogouryoukikaku2,potto1_sizailotno2_1,potto1_tyougouryou2_1,potto1_sizailotno2_2,potto1_tyougouryou2_2,potto1_zairyohinmei3,"
                + "potto1_tyogouryoukikaku3,potto1_sizailotno3_1,potto1_tyougouryou3_1,potto1_sizailotno3_2,potto1_tyougouryou3_2,potto1_zairyohinmei4,"
                + "potto1_tyogouryoukikaku4,potto1_sizailotno4_1,potto1_tyougouryou4_1,potto1_sizailotno4_2,potto1_tyougouryou4_2,potto2_zairyohinmei1,"
                + "potto2_tyogouryoukikaku1,potto2_sizailotno1_1,potto2_tyougouryou1_1,potto2_sizailotno1_2,potto2_tyougouryou1_2,potto2_zairyohinmei2,"
                + "potto2_tyogouryoukikaku2,potto2_sizailotno2_1,potto2_tyougouryou2_1,potto2_sizailotno2_2,potto2_tyougouryou2_2,potto2_zairyohinmei3,"
                + "potto2_tyogouryoukikaku3,potto2_sizailotno3_1,potto2_tyougouryou3_1,potto2_sizailotno3_2,potto2_tyougouryou3_2,potto2_zairyohinmei4,"
                + "potto2_tyogouryoukikaku4,potto2_sizailotno4_1,potto2_tyougouryou4_1,potto2_sizailotno4_2,potto2_tyougouryou4_2,potto3_zairyohinmei1,"
                + "potto3_tyogouryoukikaku1,potto3_sizailotno1_1,potto3_tyougouryou1_1,potto3_sizailotno1_2,potto3_tyougouryou1_2,potto3_zairyohinmei2,"
                + "potto3_tyogouryoukikaku2,potto3_sizailotno2_1,potto3_tyougouryou2_1,potto3_sizailotno2_2,potto3_tyougouryou2_2,potto3_zairyohinmei3,"
                + "potto3_tyogouryoukikaku3,potto3_sizailotno3_1,potto3_tyougouryou3_1,potto3_sizailotno3_2,potto3_tyougouryou3_2,potto3_zairyohinmei4,"
                + "potto3_tyogouryoukikaku4,potto3_sizailotno4_1,potto3_tyougouryou4_1,potto3_sizailotno4_2,potto3_tyougouryou4_2,potto4_zairyohinmei1,"
                + "potto4_tyogouryoukikaku1,potto4_sizailotno1_1,potto4_tyougouryou1_1,potto4_sizailotno1_2,potto4_tyougouryou1_2,potto4_zairyohinmei2,"
                + "potto4_tyogouryoukikaku2,potto4_sizailotno2_1,potto4_tyougouryou2_1,potto4_sizailotno2_2,potto4_tyougouryou2_2,potto4_zairyohinmei3,"
                + "potto4_tyogouryoukikaku3,potto4_sizailotno3_1,potto4_tyougouryou3_1,potto4_sizailotno3_2,potto4_tyougouryou3_2,potto4_zairyohinmei4,"
                + "potto4_tyogouryoukikaku4,potto4_sizailotno4_1,potto4_tyougouryou4_1,potto4_sizailotno4_2,potto4_tyougouryou4_2,potto5_zairyohinmei1,"
                + "potto5_tyogouryoukikaku1,potto5_sizailotno1_1,potto5_tyougouryou1_1,potto5_sizailotno1_2,potto5_tyougouryou1_2,potto5_zairyohinmei2,"
                + "potto5_tyogouryoukikaku2,potto5_sizailotno2_1,potto5_tyougouryou2_1,potto5_sizailotno2_2,potto5_tyougouryou2_2,potto5_zairyohinmei3,"
                + "potto5_tyogouryoukikaku3,potto5_sizailotno3_1,potto5_tyougouryou3_1,potto5_sizailotno3_2,potto5_tyougouryou3_2,potto5_zairyohinmei4,"
                + "potto5_tyogouryoukikaku4,potto5_sizailotno4_1,potto5_tyougouryou4_1,potto5_sizailotno4_2,potto5_tyougouryou4_2,potto6_zairyohinmei1,"
                + "potto6_tyogouryoukikaku1,potto6_sizailotno1_1,potto6_tyougouryou1_1,potto6_sizailotno1_2,potto6_tyougouryou1_2,potto6_zairyohinmei2,"
                + "potto6_tyogouryoukikaku2,potto6_sizailotno2_1,potto6_tyougouryou2_1,potto6_sizailotno2_2,potto6_tyougouryou2_2,potto6_zairyohinmei3,"
                + "potto6_tyogouryoukikaku3,potto6_sizailotno3_1,potto6_tyougouryou3_1,potto6_sizailotno3_2,potto6_tyougouryou3_2,potto6_zairyohinmei4,"
                + "potto6_tyogouryoukikaku4,potto6_sizailotno4_1,potto6_tyougouryou4_1,potto6_sizailotno4_2,potto6_tyougouryou4_2,potto7_zairyohinmei1,"
                + "potto7_tyogouryoukikaku1,potto7_sizailotno1_1,potto7_tyougouryou1_1,potto7_sizailotno1_2,potto7_tyougouryou1_2,potto7_zairyohinmei2,"
                + "potto7_tyogouryoukikaku2,potto7_sizailotno2_1,potto7_tyougouryou2_1,potto7_sizailotno2_2,potto7_tyougouryou2_2,potto7_zairyohinmei3,"
                + "potto7_tyogouryoukikaku3,potto7_sizailotno3_1,potto7_tyougouryou3_1,potto7_sizailotno3_2,potto7_tyougouryou3_2,potto7_zairyohinmei4,"
                + "potto7_tyogouryoukikaku4,potto7_sizailotno4_1,potto7_tyougouryou4_1,potto7_sizailotno4_2,potto7_tyougouryou4_2,potto8_zairyohinmei1,"
                + "potto8_tyogouryoukikaku1,potto8_sizailotno1_1,potto8_tyougouryou1_1,potto8_sizailotno1_2,potto8_tyougouryou1_2,potto8_zairyohinmei2,"
                + "potto8_tyogouryoukikaku2,potto8_sizailotno2_1,potto8_tyougouryou2_1,potto8_sizailotno2_2,potto8_tyougouryou2_2,potto8_zairyohinmei3,"
                + "potto8_tyogouryoukikaku3,potto8_sizailotno3_1,potto8_tyougouryou3_1,potto8_sizailotno3_2,potto8_tyougouryou3_2,potto8_zairyohinmei4,"
                + "potto8_tyogouryoukikaku4,potto8_sizailotno4_1,potto8_tyougouryou4_1,potto8_sizailotno4_2,potto8_tyougouryou4_2,hyouryounichiji,tantousya,"
                + "kakuninsya,bikou1,bikou2,?,?,?,? "
                + " FROM sr_glassslurryhyoryo "
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
     * ﾎﾟｯﾄ1 画面データ設定処理
     *
     * @param processData 処理制御データ
     */
    private void initGXHDO102B005B(ProcessData processData) {
        GXHDO102B005B bean = (GXHDO102B005B) getFormBean("beanGXHDO102C005B");
        bean.setPotto1_zairyohinmei1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO1_ZAIRYOHINMEI1));
        bean.setPotto1_tyogouryoukikaku1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU1));
        bean.setPotto1_sizailotno1_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO1_SIZAILOTNO1_1));
        bean.setPotto1_tyougouryou1_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO1_TYOUGOURYOU1_1));
        bean.setPotto1_sizailotno1_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO1_SIZAILOTNO1_2));
        bean.setPotto1_tyougouryou1_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO1_TYOUGOURYOU1_2));
        bean.setPotto1_zairyohinmei2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO1_ZAIRYOHINMEI2));
        bean.setPotto1_tyogouryoukikaku2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU2));
        bean.setPotto1_sizailotno2_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO1_SIZAILOTNO2_1));
        bean.setPotto1_tyougouryou2_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO1_TYOUGOURYOU2_1));
        bean.setPotto1_sizailotno2_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO1_SIZAILOTNO2_2));
        bean.setPotto1_tyougouryou2_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO1_TYOUGOURYOU2_2));
        bean.setPotto1_zairyohinmei3(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO1_ZAIRYOHINMEI3));
        bean.setPotto1_tyogouryoukikaku3(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU3));
        bean.setPotto1_sizailotno3_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO1_SIZAILOTNO3_1));
        bean.setPotto1_tyougouryou3_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO1_TYOUGOURYOU3_1));
        bean.setPotto1_sizailotno3_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO1_SIZAILOTNO3_2));
        bean.setPotto1_tyougouryou3_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO1_TYOUGOURYOU3_2));
        bean.setPotto1_zairyohinmei4(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO1_ZAIRYOHINMEI4));
        bean.setPotto1_tyogouryoukikaku4(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU4));
        bean.setPotto1_sizailotno4_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO1_SIZAILOTNO4_1));
        bean.setPotto1_tyougouryou4_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO1_TYOUGOURYOU4_1));
        bean.setPotto1_sizailotno4_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO1_SIZAILOTNO4_2));
        bean.setPotto1_tyougouryou4_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO1_TYOUGOURYOU4_2));
    }

    /**
     * ﾎﾟｯﾄ2 画面データ設定処理
     *
     * @param processData 処理制御データ
     */
    private void initGXHDO102B005C(ProcessData processData) {
        GXHDO102B005C bean = (GXHDO102B005C) getFormBean("beanGXHDO102C005C");
        bean.setPotto2_zairyohinmei1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO2_ZAIRYOHINMEI1));
        bean.setPotto2_tyogouryoukikaku1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU1));
        bean.setPotto2_sizailotno1_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO2_SIZAILOTNO1_1));
        bean.setPotto2_tyougouryou1_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO2_TYOUGOURYOU1_1));
        bean.setPotto2_sizailotno1_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO2_SIZAILOTNO1_2));
        bean.setPotto2_tyougouryou1_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO2_TYOUGOURYOU1_2));
        bean.setPotto2_zairyohinmei2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO2_ZAIRYOHINMEI2));
        bean.setPotto2_tyogouryoukikaku2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU2));
        bean.setPotto2_sizailotno2_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO2_SIZAILOTNO2_1));
        bean.setPotto2_tyougouryou2_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO2_TYOUGOURYOU2_1));
        bean.setPotto2_sizailotno2_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO2_SIZAILOTNO2_2));
        bean.setPotto2_tyougouryou2_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO2_TYOUGOURYOU2_2));
        bean.setPotto2_zairyohinmei3(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO2_ZAIRYOHINMEI3));
        bean.setPotto2_tyogouryoukikaku3(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU3));
        bean.setPotto2_sizailotno3_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO2_SIZAILOTNO3_1));
        bean.setPotto2_tyougouryou3_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO2_TYOUGOURYOU3_1));
        bean.setPotto2_sizailotno3_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO2_SIZAILOTNO3_2));
        bean.setPotto2_tyougouryou3_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO2_TYOUGOURYOU3_2));
        bean.setPotto2_zairyohinmei4(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO2_ZAIRYOHINMEI4));
        bean.setPotto2_tyogouryoukikaku4(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU4));
        bean.setPotto2_sizailotno4_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO2_SIZAILOTNO4_1));
        bean.setPotto2_tyougouryou4_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO2_TYOUGOURYOU4_1));
        bean.setPotto2_sizailotno4_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO2_SIZAILOTNO4_2));
        bean.setPotto2_tyougouryou4_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO2_TYOUGOURYOU4_2));
    }

    /**
     * ﾎﾟｯﾄ3 画面データ設定処理
     *
     * @param processData 処理制御データ
     */
    private void initGXHDO102B005D(ProcessData processData) {
        GXHDO102B005D bean = (GXHDO102B005D) getFormBean("beanGXHDO102C005D");
        bean.setPotto3_zairyohinmei1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO3_ZAIRYOHINMEI1));
        bean.setPotto3_tyogouryoukikaku1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU1));
        bean.setPotto3_sizailotno1_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO3_SIZAILOTNO1_1));
        bean.setPotto3_tyougouryou1_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO3_TYOUGOURYOU1_1));
        bean.setPotto3_sizailotno1_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO3_SIZAILOTNO1_2));
        bean.setPotto3_tyougouryou1_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO3_TYOUGOURYOU1_2));
        bean.setPotto3_zairyohinmei2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO3_ZAIRYOHINMEI2));
        bean.setPotto3_tyogouryoukikaku2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU2));
        bean.setPotto3_sizailotno2_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO3_SIZAILOTNO2_1));
        bean.setPotto3_tyougouryou2_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO3_TYOUGOURYOU2_1));
        bean.setPotto3_sizailotno2_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO3_SIZAILOTNO2_2));
        bean.setPotto3_tyougouryou2_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO3_TYOUGOURYOU2_2));
        bean.setPotto3_zairyohinmei3(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO3_ZAIRYOHINMEI3));
        bean.setPotto3_tyogouryoukikaku3(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU3));
        bean.setPotto3_sizailotno3_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO3_SIZAILOTNO3_1));
        bean.setPotto3_tyougouryou3_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO3_TYOUGOURYOU3_1));
        bean.setPotto3_sizailotno3_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO3_SIZAILOTNO3_2));
        bean.setPotto3_tyougouryou3_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO3_TYOUGOURYOU3_2));
        bean.setPotto3_zairyohinmei4(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO3_ZAIRYOHINMEI4));
        bean.setPotto3_tyogouryoukikaku4(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU4));
        bean.setPotto3_sizailotno4_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO3_SIZAILOTNO4_1));
        bean.setPotto3_tyougouryou4_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO3_TYOUGOURYOU4_1));
        bean.setPotto3_sizailotno4_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO3_SIZAILOTNO4_2));
        bean.setPotto3_tyougouryou4_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO3_TYOUGOURYOU4_2));
    }

    /**
     * ﾎﾟｯﾄ4 画面データ設定処理
     *
     * @param processData 処理制御データ
     */
    private void initGXHDO102B005E(ProcessData processData) {
        GXHDO102B005E bean = (GXHDO102B005E) getFormBean("beanGXHDO102C005E");
        bean.setPotto4_zairyohinmei1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO4_ZAIRYOHINMEI1));
        bean.setPotto4_tyogouryoukikaku1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU1));
        bean.setPotto4_sizailotno1_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO4_SIZAILOTNO1_1));
        bean.setPotto4_tyougouryou1_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO4_TYOUGOURYOU1_1));
        bean.setPotto4_sizailotno1_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO4_SIZAILOTNO1_2));
        bean.setPotto4_tyougouryou1_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO4_TYOUGOURYOU1_2));
        bean.setPotto4_zairyohinmei2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO4_ZAIRYOHINMEI2));
        bean.setPotto4_tyogouryoukikaku2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU2));
        bean.setPotto4_sizailotno2_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO4_SIZAILOTNO2_1));
        bean.setPotto4_tyougouryou2_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO4_TYOUGOURYOU2_1));
        bean.setPotto4_sizailotno2_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO4_SIZAILOTNO2_2));
        bean.setPotto4_tyougouryou2_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO4_TYOUGOURYOU2_2));
        bean.setPotto4_zairyohinmei3(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO4_ZAIRYOHINMEI3));
        bean.setPotto4_tyogouryoukikaku3(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU3));
        bean.setPotto4_sizailotno3_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO4_SIZAILOTNO3_1));
        bean.setPotto4_tyougouryou3_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO4_TYOUGOURYOU3_1));
        bean.setPotto4_sizailotno3_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO4_SIZAILOTNO3_2));
        bean.setPotto4_tyougouryou3_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO4_TYOUGOURYOU3_2));
        bean.setPotto4_zairyohinmei4(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO4_ZAIRYOHINMEI4));
        bean.setPotto4_tyogouryoukikaku4(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU4));
        bean.setPotto4_sizailotno4_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO4_SIZAILOTNO4_1));
        bean.setPotto4_tyougouryou4_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO4_TYOUGOURYOU4_1));
        bean.setPotto4_sizailotno4_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO4_SIZAILOTNO4_2));
        bean.setPotto4_tyougouryou4_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO4_TYOUGOURYOU4_2));
    }

    /**
     * ﾎﾟｯﾄ5 画面データ設定処理
     *
     * @param processData 処理制御データ
     */
    private void initGXHDO102B005F(ProcessData processData) {
        GXHDO102B005F bean = (GXHDO102B005F) getFormBean("beanGXHDO102C005F");
        bean.setPotto5_zairyohinmei1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO5_ZAIRYOHINMEI1));
        bean.setPotto5_tyogouryoukikaku1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU1));
        bean.setPotto5_sizailotno1_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO5_SIZAILOTNO1_1));
        bean.setPotto5_tyougouryou1_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO5_TYOUGOURYOU1_1));
        bean.setPotto5_sizailotno1_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO5_SIZAILOTNO1_2));
        bean.setPotto5_tyougouryou1_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO5_TYOUGOURYOU1_2));
        bean.setPotto5_zairyohinmei2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO5_ZAIRYOHINMEI2));
        bean.setPotto5_tyogouryoukikaku2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU2));
        bean.setPotto5_sizailotno2_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO5_SIZAILOTNO2_1));
        bean.setPotto5_tyougouryou2_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO5_TYOUGOURYOU2_1));
        bean.setPotto5_sizailotno2_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO5_SIZAILOTNO2_2));
        bean.setPotto5_tyougouryou2_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO5_TYOUGOURYOU2_2));
        bean.setPotto5_zairyohinmei3(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO5_ZAIRYOHINMEI3));
        bean.setPotto5_tyogouryoukikaku3(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU3));
        bean.setPotto5_sizailotno3_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO5_SIZAILOTNO3_1));
        bean.setPotto5_tyougouryou3_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO5_TYOUGOURYOU3_1));
        bean.setPotto5_sizailotno3_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO5_SIZAILOTNO3_2));
        bean.setPotto5_tyougouryou3_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO5_TYOUGOURYOU3_2));
        bean.setPotto5_zairyohinmei4(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO5_ZAIRYOHINMEI4));
        bean.setPotto5_tyogouryoukikaku4(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU4));
        bean.setPotto5_sizailotno4_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO5_SIZAILOTNO4_1));
        bean.setPotto5_tyougouryou4_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO5_TYOUGOURYOU4_1));
        bean.setPotto5_sizailotno4_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO5_SIZAILOTNO4_2));
        bean.setPotto5_tyougouryou4_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO5_TYOUGOURYOU4_2));
    }

    /**
     * ﾎﾟｯﾄ6 画面データ設定処理
     *
     * @param processData 処理制御データ
     */
    private void initGXHDO102B005G(ProcessData processData) {
        GXHDO102B005G bean = (GXHDO102B005G) getFormBean("beanGXHDO102C005G");
        bean.setPotto6_zairyohinmei1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO6_ZAIRYOHINMEI1));
        bean.setPotto6_tyogouryoukikaku1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU1));
        bean.setPotto6_sizailotno1_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO6_SIZAILOTNO1_1));
        bean.setPotto6_tyougouryou1_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO6_TYOUGOURYOU1_1));
        bean.setPotto6_sizailotno1_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO6_SIZAILOTNO1_2));
        bean.setPotto6_tyougouryou1_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO6_TYOUGOURYOU1_2));
        bean.setPotto6_zairyohinmei2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO6_ZAIRYOHINMEI2));
        bean.setPotto6_tyogouryoukikaku2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU2));
        bean.setPotto6_sizailotno2_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO6_SIZAILOTNO2_1));
        bean.setPotto6_tyougouryou2_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO6_TYOUGOURYOU2_1));
        bean.setPotto6_sizailotno2_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO6_SIZAILOTNO2_2));
        bean.setPotto6_tyougouryou2_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO6_TYOUGOURYOU2_2));
        bean.setPotto6_zairyohinmei3(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO6_ZAIRYOHINMEI3));
        bean.setPotto6_tyogouryoukikaku3(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU3));
        bean.setPotto6_sizailotno3_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO6_SIZAILOTNO3_1));
        bean.setPotto6_tyougouryou3_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO6_TYOUGOURYOU3_1));
        bean.setPotto6_sizailotno3_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO6_SIZAILOTNO3_2));
        bean.setPotto6_tyougouryou3_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO6_TYOUGOURYOU3_2));
        bean.setPotto6_zairyohinmei4(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO6_ZAIRYOHINMEI4));
        bean.setPotto6_tyogouryoukikaku4(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU4));
        bean.setPotto6_sizailotno4_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO6_SIZAILOTNO4_1));
        bean.setPotto6_tyougouryou4_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO6_TYOUGOURYOU4_1));
        bean.setPotto6_sizailotno4_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO6_SIZAILOTNO4_2));
        bean.setPotto6_tyougouryou4_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO6_TYOUGOURYOU4_2));
    }

    /**
     * ﾎﾟｯﾄ7 画面データ設定処理
     *
     * @param processData 処理制御データ
     */
    private void initGXHDO102B005H(ProcessData processData) {
        GXHDO102B005H bean = (GXHDO102B005H) getFormBean("beanGXHDO102C005H");
        bean.setPotto7_zairyohinmei1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO7_ZAIRYOHINMEI1));
        bean.setPotto7_tyogouryoukikaku1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU1));
        bean.setPotto7_sizailotno1_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO7_SIZAILOTNO1_1));
        bean.setPotto7_tyougouryou1_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO7_TYOUGOURYOU1_1));
        bean.setPotto7_sizailotno1_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO7_SIZAILOTNO1_2));
        bean.setPotto7_tyougouryou1_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO7_TYOUGOURYOU1_2));
        bean.setPotto7_zairyohinmei2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO7_ZAIRYOHINMEI2));
        bean.setPotto7_tyogouryoukikaku2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU2));
        bean.setPotto7_sizailotno2_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO7_SIZAILOTNO2_1));
        bean.setPotto7_tyougouryou2_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO7_TYOUGOURYOU2_1));
        bean.setPotto7_sizailotno2_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO7_SIZAILOTNO2_2));
        bean.setPotto7_tyougouryou2_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO7_TYOUGOURYOU2_2));
        bean.setPotto7_zairyohinmei3(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO7_ZAIRYOHINMEI3));
        bean.setPotto7_tyogouryoukikaku3(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU3));
        bean.setPotto7_sizailotno3_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO7_SIZAILOTNO3_1));
        bean.setPotto7_tyougouryou3_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO7_TYOUGOURYOU3_1));
        bean.setPotto7_sizailotno3_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO7_SIZAILOTNO3_2));
        bean.setPotto7_tyougouryou3_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO7_TYOUGOURYOU3_2));
        bean.setPotto7_zairyohinmei4(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO7_ZAIRYOHINMEI4));
        bean.setPotto7_tyogouryoukikaku4(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU4));
        bean.setPotto7_sizailotno4_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO7_SIZAILOTNO4_1));
        bean.setPotto7_tyougouryou4_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO7_TYOUGOURYOU4_1));
        bean.setPotto7_sizailotno4_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO7_SIZAILOTNO4_2));
        bean.setPotto7_tyougouryou4_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO7_TYOUGOURYOU4_2));
    }

    /**
     * ﾎﾟｯﾄ8 画面データ設定処理
     *
     * @param processData 処理制御データ
     */
    private void initGXHDO102B005I(ProcessData processData) {
        GXHDO102B005I bean = (GXHDO102B005I) getFormBean("beanGXHDO102C005I");
        bean.setPotto8_zairyohinmei1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO8_ZAIRYOHINMEI1));
        bean.setPotto8_tyogouryoukikaku1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU1));
        bean.setPotto8_sizailotno1_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO8_SIZAILOTNO1_1));
        bean.setPotto8_tyougouryou1_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO8_TYOUGOURYOU1_1));
        bean.setPotto8_sizailotno1_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO8_SIZAILOTNO1_2));
        bean.setPotto8_tyougouryou1_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO8_TYOUGOURYOU1_2));
        bean.setPotto8_zairyohinmei2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO8_ZAIRYOHINMEI2));
        bean.setPotto8_tyogouryoukikaku2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU2));
        bean.setPotto8_sizailotno2_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO8_SIZAILOTNO2_1));
        bean.setPotto8_tyougouryou2_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO8_TYOUGOURYOU2_1));
        bean.setPotto8_sizailotno2_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO8_SIZAILOTNO2_2));
        bean.setPotto8_tyougouryou2_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO8_TYOUGOURYOU2_2));
        bean.setPotto8_zairyohinmei3(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO8_ZAIRYOHINMEI3));
        bean.setPotto8_tyogouryoukikaku3(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU3));
        bean.setPotto8_sizailotno3_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO8_SIZAILOTNO3_1));
        bean.setPotto8_tyougouryou3_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO8_TYOUGOURYOU3_1));
        bean.setPotto8_sizailotno3_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO8_SIZAILOTNO3_2));
        bean.setPotto8_tyougouryou3_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO8_TYOUGOURYOU3_2));
        bean.setPotto8_zairyohinmei4(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO8_ZAIRYOHINMEI4));
        bean.setPotto8_tyogouryoukikaku4(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU4));
        bean.setPotto8_sizailotno4_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO8_SIZAILOTNO4_1));
        bean.setPotto8_tyougouryou4_1(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO8_TYOUGOURYOU4_1));
        bean.setPotto8_sizailotno4_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO8_SIZAILOTNO4_2));
        bean.setPotto8_tyougouryou4_2(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO8_TYOUGOURYOU4_2));
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
        List<String> formIdList = Arrays.asList("GXHDO102B005B", "GXHDO102B005C", "GXHDO102B005D", "GXHDO102B005E", "GXHDO102B005F", "GXHDO102B005G", "GXHDO102B005H", "GXHDO102B005I");
        // 項目IDリスト取得
        List<String> itemIdList = getItemIdList(processData, formIdList);
        Map<String, String> allItemIdMap = new HashMap<>();
        allItemIdMap.put(GXHDO102B005Const.POTTO1_ZAIRYOHINMEI1, "ﾎﾟｯﾄ1_材料品名1");
        allItemIdMap.put(GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU1, "ﾎﾟｯﾄ1_調合量規格1");
        allItemIdMap.put(GXHDO102B005Const.POTTO1_SIZAILOTNO1_1, "ﾎﾟｯﾄ1_部材在庫No1_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO1_TYOUGOURYOU1_1, "ﾎﾟｯﾄ1_調合量1_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO1_SIZAILOTNO1_2, "ﾎﾟｯﾄ1_部材在庫No1_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO1_TYOUGOURYOU1_2, "ﾎﾟｯﾄ1_調合量1_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO1_ZAIRYOHINMEI2, "ﾎﾟｯﾄ1_材料品名2");
        allItemIdMap.put(GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU2, "ﾎﾟｯﾄ1_調合量規格2");
        allItemIdMap.put(GXHDO102B005Const.POTTO1_SIZAILOTNO2_1, "ﾎﾟｯﾄ1_部材在庫No2_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO1_TYOUGOURYOU2_1, "ﾎﾟｯﾄ1_調合量2_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO1_SIZAILOTNO2_2, "ﾎﾟｯﾄ1_部材在庫No2_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO1_TYOUGOURYOU2_2, "ﾎﾟｯﾄ1_調合量2_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO1_ZAIRYOHINMEI3, "ﾎﾟｯﾄ1_材料品名3");
        allItemIdMap.put(GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU3, "ﾎﾟｯﾄ1_調合量規格3");
        allItemIdMap.put(GXHDO102B005Const.POTTO1_SIZAILOTNO3_1, "ﾎﾟｯﾄ1_部材在庫No3_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO1_TYOUGOURYOU3_1, "ﾎﾟｯﾄ1_調合量3_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO1_SIZAILOTNO3_2, "ﾎﾟｯﾄ1_部材在庫No3_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO1_TYOUGOURYOU3_2, "ﾎﾟｯﾄ1_調合量3_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO1_ZAIRYOHINMEI4, "ﾎﾟｯﾄ1_材料品名4");
        allItemIdMap.put(GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU4, "ﾎﾟｯﾄ1_調合量規格4");
        allItemIdMap.put(GXHDO102B005Const.POTTO1_SIZAILOTNO4_1, "ﾎﾟｯﾄ1_部材在庫No4_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO1_TYOUGOURYOU4_1, "ﾎﾟｯﾄ1_調合量4_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO1_SIZAILOTNO4_2, "ﾎﾟｯﾄ1_部材在庫No4_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO1_TYOUGOURYOU4_2, "ﾎﾟｯﾄ1_調合量4_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO2_ZAIRYOHINMEI1, "ﾎﾟｯﾄ2_材料品名1");
        allItemIdMap.put(GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU1, "ﾎﾟｯﾄ2_調合量規格1");
        allItemIdMap.put(GXHDO102B005Const.POTTO2_SIZAILOTNO1_1, "ﾎﾟｯﾄ2_部材在庫No1_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO2_TYOUGOURYOU1_1, "ﾎﾟｯﾄ2_調合量1_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO2_SIZAILOTNO1_2, "ﾎﾟｯﾄ2_部材在庫No1_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO2_TYOUGOURYOU1_2, "ﾎﾟｯﾄ2_調合量1_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO2_ZAIRYOHINMEI2, "ﾎﾟｯﾄ2_材料品名2");
        allItemIdMap.put(GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU2, "ﾎﾟｯﾄ2_調合量規格2");
        allItemIdMap.put(GXHDO102B005Const.POTTO2_SIZAILOTNO2_1, "ﾎﾟｯﾄ2_部材在庫No2_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO2_TYOUGOURYOU2_1, "ﾎﾟｯﾄ2_調合量2_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO2_SIZAILOTNO2_2, "ﾎﾟｯﾄ2_部材在庫No2_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO2_TYOUGOURYOU2_2, "ﾎﾟｯﾄ2_調合量2_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO2_ZAIRYOHINMEI3, "ﾎﾟｯﾄ2_材料品名3");
        allItemIdMap.put(GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU3, "ﾎﾟｯﾄ2_調合量規格3");
        allItemIdMap.put(GXHDO102B005Const.POTTO2_SIZAILOTNO3_1, "ﾎﾟｯﾄ2_部材在庫No3_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO2_TYOUGOURYOU3_1, "ﾎﾟｯﾄ2_調合量3_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO2_SIZAILOTNO3_2, "ﾎﾟｯﾄ2_部材在庫No3_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO2_TYOUGOURYOU3_2, "ﾎﾟｯﾄ2_調合量3_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO2_ZAIRYOHINMEI4, "ﾎﾟｯﾄ2_材料品名4");
        allItemIdMap.put(GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU4, "ﾎﾟｯﾄ2_調合量規格4");
        allItemIdMap.put(GXHDO102B005Const.POTTO2_SIZAILOTNO4_1, "ﾎﾟｯﾄ2_部材在庫No4_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO2_TYOUGOURYOU4_1, "ﾎﾟｯﾄ2_調合量4_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO2_SIZAILOTNO4_2, "ﾎﾟｯﾄ2_部材在庫No4_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO2_TYOUGOURYOU4_2, "ﾎﾟｯﾄ2_調合量4_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO3_ZAIRYOHINMEI1, "ﾎﾟｯﾄ3_材料品名1");
        allItemIdMap.put(GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU1, "ﾎﾟｯﾄ3_調合量規格1");
        allItemIdMap.put(GXHDO102B005Const.POTTO3_SIZAILOTNO1_1, "ﾎﾟｯﾄ3_部材在庫No1_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO3_TYOUGOURYOU1_1, "ﾎﾟｯﾄ3_調合量1_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO3_SIZAILOTNO1_2, "ﾎﾟｯﾄ3_部材在庫No1_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO3_TYOUGOURYOU1_2, "ﾎﾟｯﾄ3_調合量1_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO3_ZAIRYOHINMEI2, "ﾎﾟｯﾄ3_材料品名2");
        allItemIdMap.put(GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU2, "ﾎﾟｯﾄ3_調合量規格2");
        allItemIdMap.put(GXHDO102B005Const.POTTO3_SIZAILOTNO2_1, "ﾎﾟｯﾄ3_部材在庫No2_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO3_TYOUGOURYOU2_1, "ﾎﾟｯﾄ3_調合量2_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO3_SIZAILOTNO2_2, "ﾎﾟｯﾄ3_部材在庫No2_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO3_TYOUGOURYOU2_2, "ﾎﾟｯﾄ3_調合量2_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO3_ZAIRYOHINMEI3, "ﾎﾟｯﾄ3_材料品名3");
        allItemIdMap.put(GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU3, "ﾎﾟｯﾄ3_調合量規格3");
        allItemIdMap.put(GXHDO102B005Const.POTTO3_SIZAILOTNO3_1, "ﾎﾟｯﾄ3_部材在庫No3_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO3_TYOUGOURYOU3_1, "ﾎﾟｯﾄ3_調合量3_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO3_SIZAILOTNO3_2, "ﾎﾟｯﾄ3_部材在庫No3_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO3_TYOUGOURYOU3_2, "ﾎﾟｯﾄ3_調合量3_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO3_ZAIRYOHINMEI4, "ﾎﾟｯﾄ3_材料品名4");
        allItemIdMap.put(GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU4, "ﾎﾟｯﾄ3_調合量規格4");
        allItemIdMap.put(GXHDO102B005Const.POTTO3_SIZAILOTNO4_1, "ﾎﾟｯﾄ3_部材在庫No4_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO3_TYOUGOURYOU4_1, "ﾎﾟｯﾄ3_調合量4_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO3_SIZAILOTNO4_2, "ﾎﾟｯﾄ3_部材在庫No4_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO3_TYOUGOURYOU4_2, "ﾎﾟｯﾄ3_調合量4_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO4_ZAIRYOHINMEI1, "ﾎﾟｯﾄ4_材料品名1");
        allItemIdMap.put(GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU1, "ﾎﾟｯﾄ4_調合量規格1");
        allItemIdMap.put(GXHDO102B005Const.POTTO4_SIZAILOTNO1_1, "ﾎﾟｯﾄ4_部材在庫No1_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO4_TYOUGOURYOU1_1, "ﾎﾟｯﾄ4_調合量1_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO4_SIZAILOTNO1_2, "ﾎﾟｯﾄ4_部材在庫No1_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO4_TYOUGOURYOU1_2, "ﾎﾟｯﾄ4_調合量1_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO4_ZAIRYOHINMEI2, "ﾎﾟｯﾄ4_材料品名2");
        allItemIdMap.put(GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU2, "ﾎﾟｯﾄ4_調合量規格2");
        allItemIdMap.put(GXHDO102B005Const.POTTO4_SIZAILOTNO2_1, "ﾎﾟｯﾄ4_部材在庫No2_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO4_TYOUGOURYOU2_1, "ﾎﾟｯﾄ4_調合量2_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO4_SIZAILOTNO2_2, "ﾎﾟｯﾄ4_部材在庫No2_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO4_TYOUGOURYOU2_2, "ﾎﾟｯﾄ4_調合量2_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO4_ZAIRYOHINMEI3, "ﾎﾟｯﾄ4_材料品名3");
        allItemIdMap.put(GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU3, "ﾎﾟｯﾄ4_調合量規格3");
        allItemIdMap.put(GXHDO102B005Const.POTTO4_SIZAILOTNO3_1, "ﾎﾟｯﾄ4_部材在庫No3_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO4_TYOUGOURYOU3_1, "ﾎﾟｯﾄ4_調合量3_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO4_SIZAILOTNO3_2, "ﾎﾟｯﾄ4_部材在庫No3_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO4_TYOUGOURYOU3_2, "ﾎﾟｯﾄ4_調合量3_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO4_ZAIRYOHINMEI4, "ﾎﾟｯﾄ4_材料品名4");
        allItemIdMap.put(GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU4, "ﾎﾟｯﾄ4_調合量規格4");
        allItemIdMap.put(GXHDO102B005Const.POTTO4_SIZAILOTNO4_1, "ﾎﾟｯﾄ4_部材在庫No4_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO4_TYOUGOURYOU4_1, "ﾎﾟｯﾄ4_調合量4_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO4_SIZAILOTNO4_2, "ﾎﾟｯﾄ4_部材在庫No4_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO4_TYOUGOURYOU4_2, "ﾎﾟｯﾄ4_調合量4_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO5_ZAIRYOHINMEI1, "ﾎﾟｯﾄ5_材料品名1");
        allItemIdMap.put(GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU1, "ﾎﾟｯﾄ5_調合量規格1");
        allItemIdMap.put(GXHDO102B005Const.POTTO5_SIZAILOTNO1_1, "ﾎﾟｯﾄ5_部材在庫No1_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO5_TYOUGOURYOU1_1, "ﾎﾟｯﾄ5_調合量1_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO5_SIZAILOTNO1_2, "ﾎﾟｯﾄ5_部材在庫No1_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO5_TYOUGOURYOU1_2, "ﾎﾟｯﾄ5_調合量1_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO5_ZAIRYOHINMEI2, "ﾎﾟｯﾄ5_材料品名2");
        allItemIdMap.put(GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU2, "ﾎﾟｯﾄ5_調合量規格2");
        allItemIdMap.put(GXHDO102B005Const.POTTO5_SIZAILOTNO2_1, "ﾎﾟｯﾄ5_部材在庫No2_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO5_TYOUGOURYOU2_1, "ﾎﾟｯﾄ5_調合量2_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO5_SIZAILOTNO2_2, "ﾎﾟｯﾄ5_部材在庫No2_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO5_TYOUGOURYOU2_2, "ﾎﾟｯﾄ5_調合量2_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO5_ZAIRYOHINMEI3, "ﾎﾟｯﾄ5_材料品名3");
        allItemIdMap.put(GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU3, "ﾎﾟｯﾄ5_調合量規格3");
        allItemIdMap.put(GXHDO102B005Const.POTTO5_SIZAILOTNO3_1, "ﾎﾟｯﾄ5_部材在庫No3_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO5_TYOUGOURYOU3_1, "ﾎﾟｯﾄ5_調合量3_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO5_SIZAILOTNO3_2, "ﾎﾟｯﾄ5_部材在庫No3_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO5_TYOUGOURYOU3_2, "ﾎﾟｯﾄ5_調合量3_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO5_ZAIRYOHINMEI4, "ﾎﾟｯﾄ5_材料品名4");
        allItemIdMap.put(GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU4, "ﾎﾟｯﾄ5_調合量規格4");
        allItemIdMap.put(GXHDO102B005Const.POTTO5_SIZAILOTNO4_1, "ﾎﾟｯﾄ5_部材在庫No4_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO5_TYOUGOURYOU4_1, "ﾎﾟｯﾄ5_調合量4_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO5_SIZAILOTNO4_2, "ﾎﾟｯﾄ5_部材在庫No4_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO5_TYOUGOURYOU4_2, "ﾎﾟｯﾄ5_調合量4_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO6_ZAIRYOHINMEI1, "ﾎﾟｯﾄ6_材料品名1");
        allItemIdMap.put(GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU1, "ﾎﾟｯﾄ6_調合量規格1");
        allItemIdMap.put(GXHDO102B005Const.POTTO6_SIZAILOTNO1_1, "ﾎﾟｯﾄ6_部材在庫No1_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO6_TYOUGOURYOU1_1, "ﾎﾟｯﾄ6_調合量1_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO6_SIZAILOTNO1_2, "ﾎﾟｯﾄ6_部材在庫No1_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO6_TYOUGOURYOU1_2, "ﾎﾟｯﾄ6_調合量1_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO6_ZAIRYOHINMEI2, "ﾎﾟｯﾄ6_材料品名2");
        allItemIdMap.put(GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU2, "ﾎﾟｯﾄ6_調合量規格2");
        allItemIdMap.put(GXHDO102B005Const.POTTO6_SIZAILOTNO2_1, "ﾎﾟｯﾄ6_部材在庫No2_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO6_TYOUGOURYOU2_1, "ﾎﾟｯﾄ6_調合量2_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO6_SIZAILOTNO2_2, "ﾎﾟｯﾄ6_部材在庫No2_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO6_TYOUGOURYOU2_2, "ﾎﾟｯﾄ6_調合量2_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO6_ZAIRYOHINMEI3, "ﾎﾟｯﾄ6_材料品名3");
        allItemIdMap.put(GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU3, "ﾎﾟｯﾄ6_調合量規格3");
        allItemIdMap.put(GXHDO102B005Const.POTTO6_SIZAILOTNO3_1, "ﾎﾟｯﾄ6_部材在庫No3_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO6_TYOUGOURYOU3_1, "ﾎﾟｯﾄ6_調合量3_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO6_SIZAILOTNO3_2, "ﾎﾟｯﾄ6_部材在庫No3_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO6_TYOUGOURYOU3_2, "ﾎﾟｯﾄ6_調合量3_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO6_ZAIRYOHINMEI4, "ﾎﾟｯﾄ6_材料品名4");
        allItemIdMap.put(GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU4, "ﾎﾟｯﾄ6_調合量規格4");
        allItemIdMap.put(GXHDO102B005Const.POTTO6_SIZAILOTNO4_1, "ﾎﾟｯﾄ6_部材在庫No4_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO6_TYOUGOURYOU4_1, "ﾎﾟｯﾄ6_調合量4_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO6_SIZAILOTNO4_2, "ﾎﾟｯﾄ6_部材在庫No4_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO6_TYOUGOURYOU4_2, "ﾎﾟｯﾄ6_調合量4_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO7_ZAIRYOHINMEI1, "ﾎﾟｯﾄ7_材料品名1");
        allItemIdMap.put(GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU1, "ﾎﾟｯﾄ7_調合量規格1");
        allItemIdMap.put(GXHDO102B005Const.POTTO7_SIZAILOTNO1_1, "ﾎﾟｯﾄ7_部材在庫No1_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO7_TYOUGOURYOU1_1, "ﾎﾟｯﾄ7_調合量1_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO7_SIZAILOTNO1_2, "ﾎﾟｯﾄ7_部材在庫No1_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO7_TYOUGOURYOU1_2, "ﾎﾟｯﾄ7_調合量1_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO7_ZAIRYOHINMEI2, "ﾎﾟｯﾄ7_材料品名2");
        allItemIdMap.put(GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU2, "ﾎﾟｯﾄ7_調合量規格2");
        allItemIdMap.put(GXHDO102B005Const.POTTO7_SIZAILOTNO2_1, "ﾎﾟｯﾄ7_部材在庫No2_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO7_TYOUGOURYOU2_1, "ﾎﾟｯﾄ7_調合量2_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO7_SIZAILOTNO2_2, "ﾎﾟｯﾄ7_部材在庫No2_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO7_TYOUGOURYOU2_2, "ﾎﾟｯﾄ7_調合量2_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO7_ZAIRYOHINMEI3, "ﾎﾟｯﾄ7_材料品名3");
        allItemIdMap.put(GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU3, "ﾎﾟｯﾄ7_調合量規格3");
        allItemIdMap.put(GXHDO102B005Const.POTTO7_SIZAILOTNO3_1, "ﾎﾟｯﾄ7_部材在庫No3_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO7_TYOUGOURYOU3_1, "ﾎﾟｯﾄ7_調合量3_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO7_SIZAILOTNO3_2, "ﾎﾟｯﾄ7_部材在庫No3_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO7_TYOUGOURYOU3_2, "ﾎﾟｯﾄ7_調合量3_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO7_ZAIRYOHINMEI4, "ﾎﾟｯﾄ7_材料品名4");
        allItemIdMap.put(GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU4, "ﾎﾟｯﾄ7_調合量規格4");
        allItemIdMap.put(GXHDO102B005Const.POTTO7_SIZAILOTNO4_1, "ﾎﾟｯﾄ7_部材在庫No4_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO7_TYOUGOURYOU4_1, "ﾎﾟｯﾄ7_調合量4_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO7_SIZAILOTNO4_2, "ﾎﾟｯﾄ7_部材在庫No4_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO7_TYOUGOURYOU4_2, "ﾎﾟｯﾄ7_調合量4_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO8_ZAIRYOHINMEI1, "ﾎﾟｯﾄ8_材料品名1");
        allItemIdMap.put(GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU1, "ﾎﾟｯﾄ8_調合量規格1");
        allItemIdMap.put(GXHDO102B005Const.POTTO8_SIZAILOTNO1_1, "ﾎﾟｯﾄ8_部材在庫No1_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO8_TYOUGOURYOU1_1, "ﾎﾟｯﾄ8_調合量1_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO8_SIZAILOTNO1_2, "ﾎﾟｯﾄ8_部材在庫No1_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO8_TYOUGOURYOU1_2, "ﾎﾟｯﾄ8_調合量1_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO8_ZAIRYOHINMEI2, "ﾎﾟｯﾄ8_材料品名2");
        allItemIdMap.put(GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU2, "ﾎﾟｯﾄ8_調合量規格2");
        allItemIdMap.put(GXHDO102B005Const.POTTO8_SIZAILOTNO2_1, "ﾎﾟｯﾄ8_部材在庫No2_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO8_TYOUGOURYOU2_1, "ﾎﾟｯﾄ8_調合量2_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO8_SIZAILOTNO2_2, "ﾎﾟｯﾄ8_部材在庫No2_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO8_TYOUGOURYOU2_2, "ﾎﾟｯﾄ8_調合量2_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO8_ZAIRYOHINMEI3, "ﾎﾟｯﾄ8_材料品名3");
        allItemIdMap.put(GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU3, "ﾎﾟｯﾄ8_調合量規格3");
        allItemIdMap.put(GXHDO102B005Const.POTTO8_SIZAILOTNO3_1, "ﾎﾟｯﾄ8_部材在庫No3_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO8_TYOUGOURYOU3_1, "ﾎﾟｯﾄ8_調合量3_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO8_SIZAILOTNO3_2, "ﾎﾟｯﾄ8_部材在庫No3_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO8_TYOUGOURYOU3_2, "ﾎﾟｯﾄ8_調合量3_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO8_ZAIRYOHINMEI4, "ﾎﾟｯﾄ8_材料品名4");
        allItemIdMap.put(GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU4, "ﾎﾟｯﾄ8_調合量規格4");
        allItemIdMap.put(GXHDO102B005Const.POTTO8_SIZAILOTNO4_1, "ﾎﾟｯﾄ8_部材在庫No4_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO8_TYOUGOURYOU4_1, "ﾎﾟｯﾄ8_調合量4_1");
        allItemIdMap.put(GXHDO102B005Const.POTTO8_SIZAILOTNO4_2, "ﾎﾟｯﾄ8_部材在庫No4_2");
        allItemIdMap.put(GXHDO102B005Const.POTTO8_TYOUGOURYOU4_2, "ﾎﾟｯﾄ8_調合量4_2");
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
     * ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力_ｻﾌﾞ画面データの規格値取得処理
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
     * ﾎﾟｯﾄ1タブの【材料品名1】ﾘﾝｸ押下時、 ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC002potto1subgamen1(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B005Const.POTTO1_SIZAILOTNO1_1, GXHDO102B005Const.POTTO1_TYOUGOURYOU1_1, 
                GXHDO102B005Const.POTTO1_SIZAILOTNO1_2, GXHDO102B005Const.POTTO1_TYOUGOURYOU1_2);
        return openC002pottosubgamen(processData, 1, 1, returnItemIdList);
    }

    /**
     * ﾎﾟｯﾄ1タブの【材料品名2】ﾘﾝｸ押下時、 ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC002potto1subgamen2(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B005Const.POTTO1_SIZAILOTNO2_1, GXHDO102B005Const.POTTO1_TYOUGOURYOU2_1, 
                GXHDO102B005Const.POTTO1_SIZAILOTNO2_2, GXHDO102B005Const.POTTO1_TYOUGOURYOU2_2);
        return openC002pottosubgamen(processData, 1, 2, returnItemIdList);
    }

    /**
     * ﾎﾟｯﾄ1タブの【材料品名3】ﾘﾝｸ押下時、 ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC002potto1subgamen3(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B005Const.POTTO1_SIZAILOTNO3_1, GXHDO102B005Const.POTTO1_TYOUGOURYOU3_1, 
                GXHDO102B005Const.POTTO1_SIZAILOTNO3_2, GXHDO102B005Const.POTTO1_TYOUGOURYOU3_2);
        return openC002pottosubgamen(processData, 1, 3, returnItemIdList);
    }

    /**
     * ﾎﾟｯﾄ1タブの【材料品名4】ﾘﾝｸ押下時、 ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC002potto1subgamen4(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B005Const.POTTO1_SIZAILOTNO4_1, GXHDO102B005Const.POTTO1_TYOUGOURYOU4_1, 
                GXHDO102B005Const.POTTO1_SIZAILOTNO4_2, GXHDO102B005Const.POTTO1_TYOUGOURYOU4_2);
        return openC002pottosubgamen(processData, 1, 4, returnItemIdList);
    }

    /**
     * ﾎﾟｯﾄ2タブの【材料品名1】ﾘﾝｸ押下時、 ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC002potto2subgamen1(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B005Const.POTTO2_SIZAILOTNO1_1, GXHDO102B005Const.POTTO2_TYOUGOURYOU1_1, 
                GXHDO102B005Const.POTTO2_SIZAILOTNO1_2, GXHDO102B005Const.POTTO2_TYOUGOURYOU1_2);
        return openC002pottosubgamen(processData, 2, 1, returnItemIdList);
    }

    /**
     * ﾎﾟｯﾄ2タブの【材料品名2】ﾘﾝｸ押下時、 ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC002potto2subgamen2(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B005Const.POTTO2_SIZAILOTNO2_1, GXHDO102B005Const.POTTO2_TYOUGOURYOU2_1, 
                GXHDO102B005Const.POTTO2_SIZAILOTNO2_2, GXHDO102B005Const.POTTO2_TYOUGOURYOU2_2);
        return openC002pottosubgamen(processData, 2, 2, returnItemIdList);
    }

    /**
     * ﾎﾟｯﾄ2タブの【材料品名3】ﾘﾝｸ押下時、 ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC002potto2subgamen3(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B005Const.POTTO2_SIZAILOTNO3_1, GXHDO102B005Const.POTTO2_TYOUGOURYOU3_1, 
                GXHDO102B005Const.POTTO2_SIZAILOTNO3_2, GXHDO102B005Const.POTTO2_TYOUGOURYOU3_2);
        return openC002pottosubgamen(processData, 2, 3, returnItemIdList);
    }

    /**
     * ﾎﾟｯﾄ2タブの【材料品名4】ﾘﾝｸ押下時、 ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC002potto2subgamen4(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B005Const.POTTO2_SIZAILOTNO4_1, GXHDO102B005Const.POTTO2_TYOUGOURYOU4_1, 
                GXHDO102B005Const.POTTO2_SIZAILOTNO4_2, GXHDO102B005Const.POTTO2_TYOUGOURYOU4_2);
        return openC002pottosubgamen(processData, 2, 4, returnItemIdList);
    }

    /**
     * ﾎﾟｯﾄ3タブの【材料品名1】ﾘﾝｸ押下時、 ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC002potto3subgamen1(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B005Const.POTTO3_SIZAILOTNO1_1, GXHDO102B005Const.POTTO3_TYOUGOURYOU1_1, 
                GXHDO102B005Const.POTTO3_SIZAILOTNO1_2, GXHDO102B005Const.POTTO3_TYOUGOURYOU1_2);
        return openC002pottosubgamen(processData, 3, 1, returnItemIdList);
    }

    /**
     * ﾎﾟｯﾄ3タブの【材料品名2】ﾘﾝｸ押下時、 ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC002potto3subgamen2(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B005Const.POTTO3_SIZAILOTNO2_1, GXHDO102B005Const.POTTO3_TYOUGOURYOU2_1, 
                GXHDO102B005Const.POTTO3_SIZAILOTNO2_2, GXHDO102B005Const.POTTO3_TYOUGOURYOU2_2);
        return openC002pottosubgamen(processData, 3, 2, returnItemIdList);
    }

    /**
     * ﾎﾟｯﾄ3タブの【材料品名3】ﾘﾝｸ押下時、 ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC002potto3subgamen3(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B005Const.POTTO3_SIZAILOTNO3_1, GXHDO102B005Const.POTTO3_TYOUGOURYOU3_1, 
                GXHDO102B005Const.POTTO3_SIZAILOTNO3_2, GXHDO102B005Const.POTTO3_TYOUGOURYOU3_2);
        return openC002pottosubgamen(processData, 3, 3, returnItemIdList);
    }

    /**
     * ﾎﾟｯﾄ3タブの【材料品名4】ﾘﾝｸ押下時、 ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC002potto3subgamen4(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B005Const.POTTO3_SIZAILOTNO4_1, GXHDO102B005Const.POTTO3_TYOUGOURYOU4_1, 
                GXHDO102B005Const.POTTO3_SIZAILOTNO4_2, GXHDO102B005Const.POTTO3_TYOUGOURYOU4_2);
        return openC002pottosubgamen(processData, 3, 4, returnItemIdList);
    }

    /**
     * ﾎﾟｯﾄ4タブの【材料品名1】ﾘﾝｸ押下時、 ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC002potto4subgamen1(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B005Const.POTTO4_SIZAILOTNO1_1, GXHDO102B005Const.POTTO4_TYOUGOURYOU1_1, 
                GXHDO102B005Const.POTTO4_SIZAILOTNO1_2, GXHDO102B005Const.POTTO4_TYOUGOURYOU1_2);
        return openC002pottosubgamen(processData, 4, 1, returnItemIdList);
    }

    /**
     * ﾎﾟｯﾄ4タブの【材料品名2】ﾘﾝｸ押下時、 ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC002potto4subgamen2(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B005Const.POTTO4_SIZAILOTNO2_1, GXHDO102B005Const.POTTO4_TYOUGOURYOU2_1, 
                GXHDO102B005Const.POTTO4_SIZAILOTNO2_2, GXHDO102B005Const.POTTO4_TYOUGOURYOU2_2);
        return openC002pottosubgamen(processData, 4, 2, returnItemIdList);
    }

    /**
     * ﾎﾟｯﾄ4タブの【材料品名3】ﾘﾝｸ押下時、 ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC002potto4subgamen3(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B005Const.POTTO4_SIZAILOTNO3_1, GXHDO102B005Const.POTTO4_TYOUGOURYOU3_1, 
                GXHDO102B005Const.POTTO4_SIZAILOTNO3_2, GXHDO102B005Const.POTTO4_TYOUGOURYOU3_2);
        return openC002pottosubgamen(processData, 4, 3, returnItemIdList);
    }

    /**
     * ﾎﾟｯﾄ4タブの【材料品名4】ﾘﾝｸ押下時、 ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC002potto4subgamen4(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B005Const.POTTO4_SIZAILOTNO4_1, GXHDO102B005Const.POTTO4_TYOUGOURYOU4_1, 
                GXHDO102B005Const.POTTO4_SIZAILOTNO4_2, GXHDO102B005Const.POTTO4_TYOUGOURYOU4_2);
        return openC002pottosubgamen(processData, 4, 4, returnItemIdList);
    }

    /**
     * ﾎﾟｯﾄ5タブの【材料品名1】ﾘﾝｸ押下時、 ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC002potto5subgamen1(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B005Const.POTTO5_SIZAILOTNO1_1, GXHDO102B005Const.POTTO5_TYOUGOURYOU1_1, 
                GXHDO102B005Const.POTTO5_SIZAILOTNO1_2, GXHDO102B005Const.POTTO5_TYOUGOURYOU1_2);
        return openC002pottosubgamen(processData, 5, 1, returnItemIdList);
    }

    /**
     * ﾎﾟｯﾄ5タブの【材料品名2】ﾘﾝｸ押下時、 ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC002potto5subgamen2(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B005Const.POTTO5_SIZAILOTNO2_1, GXHDO102B005Const.POTTO5_TYOUGOURYOU2_1, 
                GXHDO102B005Const.POTTO5_SIZAILOTNO2_2, GXHDO102B005Const.POTTO5_TYOUGOURYOU2_2);
        return openC002pottosubgamen(processData, 5, 2, returnItemIdList);
    }

    /**
     * ﾎﾟｯﾄ5タブの【材料品名3】ﾘﾝｸ押下時、 ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC002potto5subgamen3(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B005Const.POTTO5_SIZAILOTNO3_1, GXHDO102B005Const.POTTO5_TYOUGOURYOU3_1, 
                GXHDO102B005Const.POTTO5_SIZAILOTNO3_2, GXHDO102B005Const.POTTO5_TYOUGOURYOU3_2);
        return openC002pottosubgamen(processData, 5, 3, returnItemIdList);
    }

    /**
     * ﾎﾟｯﾄ5タブの【材料品名4】ﾘﾝｸ押下時、 ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC002potto5subgamen4(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B005Const.POTTO5_SIZAILOTNO4_1, GXHDO102B005Const.POTTO5_TYOUGOURYOU4_1, 
                GXHDO102B005Const.POTTO5_SIZAILOTNO4_2, GXHDO102B005Const.POTTO5_TYOUGOURYOU4_2);
        return openC002pottosubgamen(processData, 5, 4, returnItemIdList);
    }

    /**
     * ﾎﾟｯﾄ6タブの【材料品名1】ﾘﾝｸ押下時、 ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC002potto6subgamen1(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B005Const.POTTO6_SIZAILOTNO1_1, GXHDO102B005Const.POTTO6_TYOUGOURYOU1_1, 
                GXHDO102B005Const.POTTO6_SIZAILOTNO1_2, GXHDO102B005Const.POTTO6_TYOUGOURYOU1_2);
        return openC002pottosubgamen(processData, 6, 1, returnItemIdList);
    }

    /**
     * ﾎﾟｯﾄ6タブの【材料品名2】ﾘﾝｸ押下時、 ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC002potto6subgamen2(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B005Const.POTTO6_SIZAILOTNO2_1, GXHDO102B005Const.POTTO6_TYOUGOURYOU2_1, 
                GXHDO102B005Const.POTTO6_SIZAILOTNO2_2, GXHDO102B005Const.POTTO6_TYOUGOURYOU2_2);
        return openC002pottosubgamen(processData, 6, 2, returnItemIdList);
    }

    /**
     * ﾎﾟｯﾄ6タブの【材料品名3】ﾘﾝｸ押下時、 ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC002potto6subgamen3(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B005Const.POTTO6_SIZAILOTNO3_1, GXHDO102B005Const.POTTO6_TYOUGOURYOU3_1, 
                GXHDO102B005Const.POTTO6_SIZAILOTNO3_2, GXHDO102B005Const.POTTO6_TYOUGOURYOU3_2);
        return openC002pottosubgamen(processData, 6, 3, returnItemIdList);
    }

    /**
     * ﾎﾟｯﾄ6タブの【材料品名4】ﾘﾝｸ押下時、 ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC002potto6subgamen4(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B005Const.POTTO6_SIZAILOTNO4_1, GXHDO102B005Const.POTTO6_TYOUGOURYOU4_1, 
                GXHDO102B005Const.POTTO6_SIZAILOTNO4_2, GXHDO102B005Const.POTTO6_TYOUGOURYOU4_2);
        return openC002pottosubgamen(processData, 6, 4, returnItemIdList);
    }

    /**
     * ﾎﾟｯﾄ7タブの【材料品名1】ﾘﾝｸ押下時、 ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC002potto7subgamen1(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B005Const.POTTO7_SIZAILOTNO1_1, GXHDO102B005Const.POTTO7_TYOUGOURYOU1_1, 
                GXHDO102B005Const.POTTO7_SIZAILOTNO1_2, GXHDO102B005Const.POTTO7_TYOUGOURYOU1_2);
        return openC002pottosubgamen(processData, 7, 1, returnItemIdList);
    }

    /**
     * ﾎﾟｯﾄ7タブの【材料品名2】ﾘﾝｸ押下時、 ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC002potto7subgamen2(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B005Const.POTTO7_SIZAILOTNO2_1, GXHDO102B005Const.POTTO7_TYOUGOURYOU2_1, 
                GXHDO102B005Const.POTTO7_SIZAILOTNO2_2, GXHDO102B005Const.POTTO7_TYOUGOURYOU2_2);
        return openC002pottosubgamen(processData, 7, 2, returnItemIdList);
    }

    /**
     * ﾎﾟｯﾄ7タブの【材料品名3】ﾘﾝｸ押下時、 ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC002potto7subgamen3(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B005Const.POTTO7_SIZAILOTNO3_1, GXHDO102B005Const.POTTO7_TYOUGOURYOU3_1, 
                GXHDO102B005Const.POTTO7_SIZAILOTNO3_2, GXHDO102B005Const.POTTO7_TYOUGOURYOU3_2);
        return openC002pottosubgamen(processData, 7, 3, returnItemIdList);
    }

    /**
     * ﾎﾟｯﾄ7タブの【材料品名4】ﾘﾝｸ押下時、 ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC002potto7subgamen4(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B005Const.POTTO7_SIZAILOTNO4_1, GXHDO102B005Const.POTTO7_TYOUGOURYOU4_1, 
                GXHDO102B005Const.POTTO7_SIZAILOTNO4_2, GXHDO102B005Const.POTTO7_TYOUGOURYOU4_2);
        return openC002pottosubgamen(processData, 7, 4, returnItemIdList);
    }

    /**
     * ﾎﾟｯﾄ8タブの【材料品名1】ﾘﾝｸ押下時、 ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC002potto8subgamen1(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B005Const.POTTO8_SIZAILOTNO1_1, GXHDO102B005Const.POTTO8_TYOUGOURYOU1_1, 
                GXHDO102B005Const.POTTO8_SIZAILOTNO1_2, GXHDO102B005Const.POTTO8_TYOUGOURYOU1_2);
        return openC002pottosubgamen(processData, 8, 1, returnItemIdList);
    }

    /**
     * ﾎﾟｯﾄ8タブの【材料品名2】ﾘﾝｸ押下時、 ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC002potto8subgamen2(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B005Const.POTTO8_SIZAILOTNO2_1, GXHDO102B005Const.POTTO8_TYOUGOURYOU2_1, 
                GXHDO102B005Const.POTTO8_SIZAILOTNO2_2, GXHDO102B005Const.POTTO8_TYOUGOURYOU2_2);
        return openC002pottosubgamen(processData, 8, 2, returnItemIdList);
    }

    /**
     * ﾎﾟｯﾄ8タブの【材料品名3】ﾘﾝｸ押下時、 ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC002potto8subgamen3(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B005Const.POTTO8_SIZAILOTNO3_1, GXHDO102B005Const.POTTO8_TYOUGOURYOU3_1, 
                GXHDO102B005Const.POTTO8_SIZAILOTNO3_2, GXHDO102B005Const.POTTO8_TYOUGOURYOU3_2);
        return openC002pottosubgamen(processData, 8, 3, returnItemIdList);
    }

    /**
     * ﾎﾟｯﾄ8タブの【材料品名4】ﾘﾝｸ押下時、 ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openC002potto8subgamen4(ProcessData processData) {
        List<String> returnItemIdList = Arrays.asList(GXHDO102B005Const.POTTO8_SIZAILOTNO4_1, GXHDO102B005Const.POTTO8_TYOUGOURYOU4_1, 
                GXHDO102B005Const.POTTO8_SIZAILOTNO4_2, GXHDO102B005Const.POTTO8_TYOUGOURYOU4_2);
        return openC002pottosubgamen(processData, 8, 4, returnItemIdList);
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面Open
     *
     * @param processData 処理制御データ
     * @param pot ﾎﾟｯﾄ
     * @param zairyokubun 材料区分
     * @param returnItemIdList サブ画面から戻ったときに値を設定必要項目リスト
     * @return 処理制御データ
     */
    public ProcessData openC002pottosubgamen(ProcessData processData, int pot, int zairyokubun, List<String> returnItemIdList) {
        try {
            // 「秤量号機」
            FXHDD01 itemGoki = getItemRow(processData.getItemList(), GXHDO102B005Const.SEIHIN_GOKI);
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
            processData.setCollBackParam("gxhdo102c002");

            GXHDO102C002 beanGXHDO102C002 = (GXHDO102C002) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C002);
            GXHDO102C002Model gxhdo102c002model = beanGXHDO102C002.getGxhdO102c002Model();
            // 主画面からサブ画面に渡されたデータを設定
            setSubGamenInitData(gxhdo102c002model, pot, zairyokubun, itemGoki, returnItemIdList);

            beanGXHDO102C002.setGxhdO102c002ModelView(gxhdo102c002model.clone());
        } catch (CloneNotSupportedException ex) {
            ErrUtil.outputErrorLog("CloneNotSupportedException発生", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            return processData;
        }

        return processData;
    }

    /**
     * 主画面からサブ画面に渡されたデータを設定
     *
     * @param gxhdo102c002model モデルデータ
     * @param pot ﾎﾟｯﾄ
     * @param zairyokubun 材料区分
     * @param itemGoki 秤量号機データ
     * @param returnItemIdList サブ画面から戻るデータリスト
     * @throws CloneNotSupportedException 例外エラー
     */
    private void setSubGamenInitData(GXHDO102C002Model gxhdo102c002model, int pot, int zairyokubun, FXHDD01 itemGoki, List<String> returnItemIdList) throws CloneNotSupportedException {
        GXHDO102C002Model.C002SubGamenData c002subgamendata = GXHDO102C002Logic.getC002subgamendata(gxhdo102c002model, pot, zairyokubun);
        if(c002subgamendata == null){
            return;
        }
        c002subgamendata.setSubDataPot(pot);
        c002subgamendata.setSubDataGoki(StringUtil.nullToBlank(itemGoki.getValue()));
        c002subgamendata.setSubDataZairyokubun(zairyokubun);
        // サブ画面から戻ったときに値を設定する項目を指定する。
        c002subgamendata.setReturnItemIdBuzailotno1(returnItemIdList.get(0)); // 部材在庫No.X_1
        c002subgamendata.setReturnItemIdTyougouryou1(returnItemIdList.get(1)); // 調合量X_1
        c002subgamendata.setReturnItemIdBuzailotno2(returnItemIdList.get(2)); // 部材在庫NoX_2
        c002subgamendata.setReturnItemIdTyougouryou2(returnItemIdList.get(3)); // 調合量X_2
        gxhdo102c002model.setShowsubgamendata(c002subgamendata.clone());
        // サブ画面の調合残量の計算
        GXHDO102C002Logic.calcTyogouzanryou(gxhdo102c002model);
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
     * ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力_ｻﾌﾞ画面データ設定処理
     *
     * @param processData 処理制御データ
     * @param subSrGlassslurryhyoryoList ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力_ｻﾌﾞ画面データリスト
     */
    private void setInputItemDataSubFormC002(ProcessData processData, List<SubSrGlassslurryhyoryo> subSrGlassslurryhyoryoList) {
        // サブ画面の情報を取得
        GXHDO102C002 beanGXHDO102C002 = (GXHDO102C002) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C002);

        GXHDO102C002Model model;
        if (subSrGlassslurryhyoryoList == null) {
            // 登録データが無い場合、主画面の材料品名1-2調合量規格1-2はｻﾌﾞ画面の初期値をセットする。
            subSrGlassslurryhyoryoList = new ArrayList<>();
            SubSrGlassslurryhyoryo potto1subgamen1 = new SubSrGlassslurryhyoryo();
            SubSrGlassslurryhyoryo potto1subgamen2 = new SubSrGlassslurryhyoryo();
            SubSrGlassslurryhyoryo potto1subgamen3 = new SubSrGlassslurryhyoryo();
            SubSrGlassslurryhyoryo potto1subgamen4 = new SubSrGlassslurryhyoryo();
            SubSrGlassslurryhyoryo potto2subgamen1 = new SubSrGlassslurryhyoryo();
            SubSrGlassslurryhyoryo potto2subgamen2 = new SubSrGlassslurryhyoryo();
            SubSrGlassslurryhyoryo potto2subgamen3 = new SubSrGlassslurryhyoryo();
            SubSrGlassslurryhyoryo potto2subgamen4 = new SubSrGlassslurryhyoryo();
            SubSrGlassslurryhyoryo potto3subgamen1 = new SubSrGlassslurryhyoryo();
            SubSrGlassslurryhyoryo potto3subgamen2 = new SubSrGlassslurryhyoryo();
            SubSrGlassslurryhyoryo potto3subgamen3 = new SubSrGlassslurryhyoryo();
            SubSrGlassslurryhyoryo potto3subgamen4 = new SubSrGlassslurryhyoryo();
            SubSrGlassslurryhyoryo potto4subgamen1 = new SubSrGlassslurryhyoryo();
            SubSrGlassslurryhyoryo potto4subgamen2 = new SubSrGlassslurryhyoryo();
            SubSrGlassslurryhyoryo potto4subgamen3 = new SubSrGlassslurryhyoryo();
            SubSrGlassslurryhyoryo potto4subgamen4 = new SubSrGlassslurryhyoryo();
            SubSrGlassslurryhyoryo potto5subgamen1 = new SubSrGlassslurryhyoryo();
            SubSrGlassslurryhyoryo potto5subgamen2 = new SubSrGlassslurryhyoryo();
            SubSrGlassslurryhyoryo potto5subgamen3 = new SubSrGlassslurryhyoryo();
            SubSrGlassslurryhyoryo potto5subgamen4 = new SubSrGlassslurryhyoryo();
            SubSrGlassslurryhyoryo potto6subgamen1 = new SubSrGlassslurryhyoryo();
            SubSrGlassslurryhyoryo potto6subgamen2 = new SubSrGlassslurryhyoryo();
            SubSrGlassslurryhyoryo potto6subgamen3 = new SubSrGlassslurryhyoryo();
            SubSrGlassslurryhyoryo potto6subgamen4 = new SubSrGlassslurryhyoryo();
            SubSrGlassslurryhyoryo potto7subgamen1 = new SubSrGlassslurryhyoryo();
            SubSrGlassslurryhyoryo potto7subgamen2 = new SubSrGlassslurryhyoryo();
            SubSrGlassslurryhyoryo potto7subgamen3 = new SubSrGlassslurryhyoryo();
            SubSrGlassslurryhyoryo potto7subgamen4 = new SubSrGlassslurryhyoryo();
            SubSrGlassslurryhyoryo potto8subgamen1 = new SubSrGlassslurryhyoryo();
            SubSrGlassslurryhyoryo potto8subgamen2 = new SubSrGlassslurryhyoryo();
            SubSrGlassslurryhyoryo potto8subgamen3 = new SubSrGlassslurryhyoryo();
            SubSrGlassslurryhyoryo potto8subgamen4 = new SubSrGlassslurryhyoryo();

            potto1subgamen1.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO1_ZAIRYOHINMEI1))); // ﾎﾟｯﾄ1の材料品名1
            potto1subgamen1.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU1))); // ﾎﾟｯﾄ1の調合量規格1
            potto1subgamen2.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO1_ZAIRYOHINMEI2))); // ﾎﾟｯﾄ1の材料品名2
            potto1subgamen2.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU2))); // ﾎﾟｯﾄ1の調合量規格2
            potto1subgamen3.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO1_ZAIRYOHINMEI3))); // ﾎﾟｯﾄ1の材料品名3
            potto1subgamen3.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU3))); // ﾎﾟｯﾄ1の調合量規格3
            potto1subgamen4.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO1_ZAIRYOHINMEI4))); // ﾎﾟｯﾄ1の材料品名4
            potto1subgamen4.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO1_TYOGOURYOUKIKAKU4))); // ﾎﾟｯﾄ1の調合量規格4

            potto2subgamen1.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO2_ZAIRYOHINMEI1))); // ﾎﾟｯﾄ2の材料品名1
            potto2subgamen1.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU1))); // ﾎﾟｯﾄ2の調合量規格1
            potto2subgamen2.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO2_ZAIRYOHINMEI2))); // ﾎﾟｯﾄ2の材料品名2
            potto2subgamen2.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU2))); // ﾎﾟｯﾄ2の調合量規格2
            potto2subgamen3.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO2_ZAIRYOHINMEI3))); // ﾎﾟｯﾄ2の材料品名3
            potto2subgamen3.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU3))); // ﾎﾟｯﾄ2の調合量規格3
            potto2subgamen4.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO2_ZAIRYOHINMEI4))); // ﾎﾟｯﾄ2の材料品名4
            potto2subgamen4.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO2_TYOGOURYOUKIKAKU4))); // ﾎﾟｯﾄ2の調合量規格4

            potto3subgamen1.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO3_ZAIRYOHINMEI1))); // ﾎﾟｯﾄ3の材料品名1
            potto3subgamen1.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU1))); // ﾎﾟｯﾄ3の調合量規格1
            potto3subgamen2.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO3_ZAIRYOHINMEI2))); // ﾎﾟｯﾄ3の材料品名2
            potto3subgamen2.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU2))); // ﾎﾟｯﾄ3の調合量規格2
            potto3subgamen3.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO3_ZAIRYOHINMEI3))); // ﾎﾟｯﾄ3の材料品名3
            potto3subgamen3.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU3))); // ﾎﾟｯﾄ3の調合量規格3
            potto3subgamen4.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO3_ZAIRYOHINMEI4))); // ﾎﾟｯﾄ3の材料品名4
            potto3subgamen4.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO3_TYOGOURYOUKIKAKU4))); // ﾎﾟｯﾄ3の調合量規格4

            potto4subgamen1.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO4_ZAIRYOHINMEI1))); // ﾎﾟｯﾄ4の材料品名1
            potto4subgamen1.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU1))); // ﾎﾟｯﾄ4の調合量規格1
            potto4subgamen2.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO4_ZAIRYOHINMEI2))); // ﾎﾟｯﾄ4の材料品名2
            potto4subgamen2.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU2))); // ﾎﾟｯﾄ4の調合量規格2
            potto4subgamen3.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO4_ZAIRYOHINMEI3))); // ﾎﾟｯﾄ4の材料品名3
            potto4subgamen3.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU3))); // ﾎﾟｯﾄ4の調合量規格3
            potto4subgamen4.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO4_ZAIRYOHINMEI4))); // ﾎﾟｯﾄ4の材料品名4
            potto4subgamen4.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO4_TYOGOURYOUKIKAKU4))); // ﾎﾟｯﾄ4の調合量規格4

            potto5subgamen1.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO5_ZAIRYOHINMEI1))); // ﾎﾟｯﾄ5の材料品名1
            potto5subgamen1.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU1))); // ﾎﾟｯﾄ5の調合量規格1
            potto5subgamen2.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO5_ZAIRYOHINMEI2))); // ﾎﾟｯﾄ5の材料品名2
            potto5subgamen2.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU2))); // ﾎﾟｯﾄ5の調合量規格2
            potto5subgamen3.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO5_ZAIRYOHINMEI3))); // ﾎﾟｯﾄ5の材料品名3
            potto5subgamen3.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU3))); // ﾎﾟｯﾄ5の調合量規格3
            potto5subgamen4.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO5_ZAIRYOHINMEI4))); // ﾎﾟｯﾄ5の材料品名4
            potto5subgamen4.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO5_TYOGOURYOUKIKAKU4))); // ﾎﾟｯﾄ5の調合量規格4

            potto6subgamen1.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO6_ZAIRYOHINMEI1))); // ﾎﾟｯﾄ6の材料品名1
            potto6subgamen1.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU1))); // ﾎﾟｯﾄ6の調合量規格1
            potto6subgamen2.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO6_ZAIRYOHINMEI2))); // ﾎﾟｯﾄ6の材料品名2
            potto6subgamen2.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU2))); // ﾎﾟｯﾄ6の調合量規格2
            potto6subgamen3.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO6_ZAIRYOHINMEI3))); // ﾎﾟｯﾄ6の材料品名3
            potto6subgamen3.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU3))); // ﾎﾟｯﾄ6の調合量規格3
            potto6subgamen4.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO6_ZAIRYOHINMEI4))); // ﾎﾟｯﾄ6の材料品名4
            potto6subgamen4.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO6_TYOGOURYOUKIKAKU4))); // ﾎﾟｯﾄ6の調合量規格4

            potto7subgamen1.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO7_ZAIRYOHINMEI1))); // ﾎﾟｯﾄ7の材料品名1
            potto7subgamen1.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU1))); // ﾎﾟｯﾄ7の調合量規格1
            potto7subgamen2.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO7_ZAIRYOHINMEI2))); // ﾎﾟｯﾄ7の材料品名2
            potto7subgamen2.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU2))); // ﾎﾟｯﾄ7の調合量規格2
            potto7subgamen3.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO7_ZAIRYOHINMEI3))); // ﾎﾟｯﾄ7の材料品名3
            potto7subgamen3.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU3))); // ﾎﾟｯﾄ7の調合量規格3
            potto7subgamen4.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO7_ZAIRYOHINMEI4))); // ﾎﾟｯﾄ7の材料品名4
            potto7subgamen4.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO7_TYOGOURYOUKIKAKU4))); // ﾎﾟｯﾄ7の調合量規格4

            potto8subgamen1.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO8_ZAIRYOHINMEI1))); // ﾎﾟｯﾄ8の材料品名1
            potto8subgamen1.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU1))); // ﾎﾟｯﾄ8の調合量規格1
            potto8subgamen2.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO8_ZAIRYOHINMEI2))); // ﾎﾟｯﾄ8の材料品名2
            potto8subgamen2.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU2))); // ﾎﾟｯﾄ8の調合量規格2
            potto8subgamen3.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO8_ZAIRYOHINMEI3))); // ﾎﾟｯﾄ8の材料品名3
            potto8subgamen3.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU3))); // ﾎﾟｯﾄ8の調合量規格3
            potto8subgamen4.setZairyohinmei(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO8_ZAIRYOHINMEI4))); // ﾎﾟｯﾄ8の材料品名4
            potto8subgamen4.setTyogouryoukikaku(getFXHDD01KikakuChi(getItemRow(processData.getItemListEx(), GXHDO102B005Const.POTTO8_TYOGOURYOUKIKAKU4))); // ﾎﾟｯﾄ8の調合量規格4
            subSrGlassslurryhyoryoList.add(potto1subgamen1);
            subSrGlassslurryhyoryoList.add(potto1subgamen2);
            subSrGlassslurryhyoryoList.add(potto1subgamen3);
            subSrGlassslurryhyoryoList.add(potto1subgamen4);
            subSrGlassslurryhyoryoList.add(potto2subgamen1);
            subSrGlassslurryhyoryoList.add(potto2subgamen2);
            subSrGlassslurryhyoryoList.add(potto2subgamen3);
            subSrGlassslurryhyoryoList.add(potto2subgamen4);
            subSrGlassslurryhyoryoList.add(potto3subgamen1);
            subSrGlassslurryhyoryoList.add(potto3subgamen2);
            subSrGlassslurryhyoryoList.add(potto3subgamen3);
            subSrGlassslurryhyoryoList.add(potto3subgamen4);
            subSrGlassslurryhyoryoList.add(potto4subgamen1);
            subSrGlassslurryhyoryoList.add(potto4subgamen2);
            subSrGlassslurryhyoryoList.add(potto4subgamen3);
            subSrGlassslurryhyoryoList.add(potto4subgamen4);
            subSrGlassslurryhyoryoList.add(potto5subgamen1);
            subSrGlassslurryhyoryoList.add(potto5subgamen2);
            subSrGlassslurryhyoryoList.add(potto5subgamen3);
            subSrGlassslurryhyoryoList.add(potto5subgamen4);
            subSrGlassslurryhyoryoList.add(potto6subgamen1);
            subSrGlassslurryhyoryoList.add(potto6subgamen2);
            subSrGlassslurryhyoryoList.add(potto6subgamen3);
            subSrGlassslurryhyoryoList.add(potto6subgamen4);
            subSrGlassslurryhyoryoList.add(potto7subgamen1);
            subSrGlassslurryhyoryoList.add(potto7subgamen2);
            subSrGlassslurryhyoryoList.add(potto7subgamen3);
            subSrGlassslurryhyoryoList.add(potto7subgamen4);
            subSrGlassslurryhyoryoList.add(potto8subgamen1);
            subSrGlassslurryhyoryoList.add(potto8subgamen2);
            subSrGlassslurryhyoryoList.add(potto8subgamen3);
            subSrGlassslurryhyoryoList.add(potto8subgamen4);
            model = GXHDO102C002Logic.createGXHDO102C002Model(subSrGlassslurryhyoryoList);

        } else {
            // 登録データがあれば登録データをセットする。
            model = GXHDO102C002Logic.createGXHDO102C002Model(subSrGlassslurryhyoryoList);
        }

        beanGXHDO102C002.setGxhdO102c002Model(model);
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力_ｻﾌﾞ画面の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力_ｻﾌﾞ画面登録データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrGlassslurryhyoryo> getSubSrGlassslurryhyoryoData(QueryRunner queryRunnerQcdb,
            String rev, String jotaiFlg, String kojyo, String lotNo, String edaban) throws SQLException {
        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSubSrGlassslurryhyoryo(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSubSrGlassslurryhyoryo(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        }
    }

    /**
     * [ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力_ｻﾌﾞ画面]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrGlassslurryhyoryo> loadSubSrGlassslurryhyoryo(QueryRunner queryRunnerQcdb,
            String kojyo, String lotNo, String edaban, String rev) throws SQLException {

        String sql = "SELECT kojyo, lotno, edaban, pot, zairyokubun, tyogouryoukikaku, tyogouzanryou, "
                + " zairyohinmei, buzailotno1, buzaihinmei1, tyougouryou1_1, tyougouryou1_2, "
                + " tyougouryou1_3, tyougouryou1_4, tyougouryou1_5, tyougouryou1_6, buzailotno2, "
                + " buzaihinmei2, tyougouryou2_1, tyougouryou2_2, tyougouryou2_3, tyougouryou2_4, "
                + " tyougouryou2_5, tyougouryou2_6, torokunichiji, kosinnichiji, revision, '0' AS deleteflag "
                + " FROM sub_sr_glassslurryhyoryo "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? ";

        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            sql += "AND revision = ? ";
        }
        sql += " order by pot, zairyokubun ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);

        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            params.add(rev);
        }

        Map<String, String> mapping = new HashMap<>();
        mapping.put("kojyo", "kojyo"); // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno"); // ﾛｯﾄNo
        mapping.put("edaban", "edaban"); // 枝番
        mapping.put("pot", "pot"); // ﾎﾟｯﾄ
        mapping.put("zairyokubun", "zairyokubun"); // 材料区分
        mapping.put("tyogouryoukikaku", "tyogouryoukikaku"); // 調合量規格
        mapping.put("tyogouzanryou", "tyogouzanryou"); // 調合残量
        mapping.put("zairyohinmei", "zairyohinmei"); // 材料品名
        mapping.put("buzailotno1", "buzailotno1"); // 部材在庫No1
        mapping.put("buzaihinmei1", "buzaihinmei1"); // 部材在庫品名1
        mapping.put("tyougouryou1_1", "tyougouryou1_1"); // 調合量1_1
        mapping.put("tyougouryou1_2", "tyougouryou1_2"); // 調合量1_2
        mapping.put("tyougouryou1_3", "tyougouryou1_3"); // 調合量1_3
        mapping.put("tyougouryou1_4", "tyougouryou1_4"); // 調合量1_4
        mapping.put("tyougouryou1_5", "tyougouryou1_5"); // 調合量1_5
        mapping.put("tyougouryou1_6", "tyougouryou1_6"); // 調合量1_6
        mapping.put("buzailotno2", "buzailotno2"); // 部材在庫No2
        mapping.put("buzaihinmei2", "buzaihinmei2"); // 部材在庫品名2
        mapping.put("tyougouryou2_1", "tyougouryou2_1"); // 調合量2_1
        mapping.put("tyougouryou2_2", "tyougouryou2_2"); // 調合量2_2
        mapping.put("tyougouryou2_3", "tyougouryou2_3"); // 調合量2_3
        mapping.put("tyougouryou2_4", "tyougouryou2_4"); // 調合量2_4
        mapping.put("tyougouryou2_5", "tyougouryou2_5"); // 調合量2_5
        mapping.put("tyougouryou2_6", "tyougouryou2_6"); // 調合量2_6
        mapping.put("torokunichiji", "torokunichiji"); // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); // 更新日時
        mapping.put("revision", "revision"); // revision
        mapping.put("deleteflag", "deleteflag"); // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SubSrGlassslurryhyoryo>> beanHandler = new BeanListHandler<>(SubSrGlassslurryhyoryo.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力_ｻﾌﾞ画面_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrGlassslurryhyoryo> loadTmpSubSrGlassslurryhyoryo(QueryRunner queryRunnerQcdb,
            String kojyo, String lotNo, String edaban, String rev) throws SQLException {

        String sql = "SELECT kojyo, lotno, edaban, pot, zairyokubun, tyogouryoukikaku, tyogouzanryou, "
                + " zairyohinmei, buzailotno1, buzaihinmei1, tyougouryou1_1, tyougouryou1_2, "
                + " tyougouryou1_3, tyougouryou1_4, tyougouryou1_5, tyougouryou1_6, buzailotno2, "
                + " buzaihinmei2, tyougouryou2_1, tyougouryou2_2, tyougouryou2_3, tyougouryou2_4, "
                + " tyougouryou2_5, tyougouryou2_6, torokunichiji, kosinnichiji, revision, deleteflag "
                + " FROM tmp_sub_sr_glassslurryhyoryo "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND deleteflag = ? ";

        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            sql += "AND revision = ? ";
        }
        sql += " order by pot, zairyokubun ";

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
        mapping.put("kojyo", "kojyo"); // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno"); // ﾛｯﾄNo
        mapping.put("edaban", "edaban"); // 枝番
        mapping.put("pot", "pot"); // ﾎﾟｯﾄ
        mapping.put("zairyokubun", "zairyokubun"); // 材料区分
        mapping.put("tyogouryoukikaku", "tyogouryoukikaku"); // 調合量規格
        mapping.put("tyogouzanryou", "tyogouzanryou"); // 調合残量
        mapping.put("zairyohinmei", "zairyohinmei"); // 材料品名
        mapping.put("buzailotno1", "buzailotno1"); // 部材在庫No1
        mapping.put("buzaihinmei1", "buzaihinmei1"); // 部材在庫品名1
        mapping.put("tyougouryou1_1", "tyougouryou1_1"); // 調合量1_1
        mapping.put("tyougouryou1_2", "tyougouryou1_2"); // 調合量1_2
        mapping.put("tyougouryou1_3", "tyougouryou1_3"); // 調合量1_3
        mapping.put("tyougouryou1_4", "tyougouryou1_4"); // 調合量1_4
        mapping.put("tyougouryou1_5", "tyougouryou1_5"); // 調合量1_5
        mapping.put("tyougouryou1_6", "tyougouryou1_6"); // 調合量1_6
        mapping.put("buzailotno2", "buzailotno2"); // 部材在庫No2
        mapping.put("buzaihinmei2", "buzaihinmei2"); // 部材在庫品名2
        mapping.put("tyougouryou2_1", "tyougouryou2_1"); // 調合量2_1
        mapping.put("tyougouryou2_2", "tyougouryou2_2"); // 調合量2_2
        mapping.put("tyougouryou2_3", "tyougouryou2_3"); // 調合量2_3
        mapping.put("tyougouryou2_4", "tyougouryou2_4"); // 調合量2_4
        mapping.put("tyougouryou2_5", "tyougouryou2_5"); // 調合量2_5
        mapping.put("tyougouryou2_6", "tyougouryou2_6"); // 調合量2_6
        mapping.put("torokunichiji", "torokunichiji"); // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); // 更新日時
        mapping.put("revision", "revision"); // revision
        mapping.put("deleteflag", "deleteflag"); // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SubSrGlassslurryhyoryo>> beanHandler = new BeanListHandler<>(SubSrGlassslurryhyoryo.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }
    
    /**
     * ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力_サブ画面_仮登録(tmp_sub_sr_glassslurryhyoryo)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param pot ﾎﾟｯﾄ
     * @param zairyokubun 材料区分
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void insertTmpSubSrGlassslurryhyoryo(QueryRunner queryRunnerQcdb, Connection conQcdb, 
            BigDecimal newRev, int deleteflag, String kojyo, String lotNo, String edaban, Integer pot, Integer zairyokubun, 
            String systemTime) throws SQLException {
        
        String sql = "INSERT INTO tmp_sub_sr_glassslurryhyoryo ( "
                + " kojyo, lotno, edaban, pot, zairyokubun, tyogouryoukikaku, tyogouzanryou, "
                + " zairyohinmei, buzailotno1, buzaihinmei1, tyougouryou1_1, tyougouryou1_2, "
                + " tyougouryou1_3, tyougouryou1_4, tyougouryou1_5, tyougouryou1_6, buzailotno2, "
                + " buzaihinmei2, tyougouryou2_1, tyougouryou2_2, tyougouryou2_3, tyougouryou2_4, "
                + " tyougouryou2_5, tyougouryou2_6, torokunichiji, kosinnichiji, revision, deleteflag "
                + " ) VALUES ("
                + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

        List<Object> params = setUpdateParameterTmpSubSrSrGlassslurryhyoryo(true, newRev, deleteflag, kojyo, lotNo, edaban, pot, zairyokubun, systemTime);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力_仮登録(tmp_sub_sr_glassslurryhyoryo)更新処理
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
    private void updateTmpSubSrGlassslurryhyoryo(QueryRunner queryRunnerQcdb, Connection conQcdb, 
            BigDecimal rev, BigDecimal newRev, String kojyo, String lotNo, 
            String edaban, Integer pot, Integer zairyokubun, String systemTime) throws SQLException {
        
        String sql = "UPDATE tmp_sub_sr_glassslurryhyoryo SET "
                + " tyogouryoukikaku = ?, tyogouzanryou = ?, "
                + " zairyohinmei = ?, buzailotno1 = ?, buzaihinmei1 = ?, tyougouryou1_1 = ?, tyougouryou1_2 = ?, "
                + " tyougouryou1_3 = ?, tyougouryou1_4 = ?, tyougouryou1_5 = ?, tyougouryou1_6 = ?, buzailotno2 = ?, "
                + " buzaihinmei2 = ?, tyougouryou2_1 = ?, tyougouryou2_2 = ?, tyougouryou2_3 = ?, tyougouryou2_4 = ?, "
                + " tyougouryou2_5 = ?, tyougouryou2_6 = ?, kosinnichiji = ?, revision = ?, deleteflag = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND pot = ? AND zairyokubun = ? AND revision = ? ";
        
        List<Object> params = setUpdateParameterTmpSubSrSrGlassslurryhyoryo(false, newRev, 0, kojyo, lotNo, edaban, pot, zairyokubun, systemTime);

        // 検索条件
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(pot);
        params.add(zairyokubun);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }
    
    /**
     * ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力_サブ画面仮登録(tmp_sub_sr_glassslurryhyoryo)更新値パラメータ設定
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
    private List<Object> setUpdateParameterTmpSubSrSrGlassslurryhyoryo(boolean isInsert, BigDecimal newRev, 
            int deleteflag, String kojyo, String lotNo, String edaban, Integer pot, Integer zairyokubun, String systemTime) {
        
        List<Object> params = new ArrayList<>();

        // 子画面情報を取得
        GXHDO102C002 beanGXHDO102C002 = (GXHDO102C002) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C002);
        GXHDO102C002Model gxhdO102c002Model = beanGXHDO102C002.getGxhdO102c002Model();

        // ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力_サブ画面から更新値を取得
        ArrayList<Object> subGamenDataList = getSubGamenData(gxhdO102c002Model, pot, zairyokubun);
        // 調合量規格
        FXHDD01 tyogouryoukikaku = (FXHDD01)subGamenDataList.get(0);
        // 調合残量
        FXHDD01 tyogouzanryou = (FXHDD01)subGamenDataList.get(1);
        // 部材①
        List<FXHDD01> buzaitab1DataList = (List<FXHDD01>)subGamenDataList.get(2);
        // 部材②
        List<FXHDD01> buzaitab2DataList = (List<FXHDD01>)subGamenDataList.get(3);
        
        if (isInsert) {
            params.add(kojyo); // 工場ｺｰﾄﾞ
            params.add(lotNo); // ﾛｯﾄNo
            params.add(edaban); // 枝番
            params.add(pot); // ﾎﾟｯﾄ
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
     * ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力_サブ画面仮登録(tmp_sub_sr_glassslurryhyoryo)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSubSrGlassslurryhyoryo(QueryRunner queryRunnerQcdb, Connection conQcdb, 
            BigDecimal rev, String kojyo, String lotNo, String edaban) throws SQLException {
        
        String sql = "DELETE FROM tmp_sub_sr_glassslurryhyoryo "
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
     * ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力_サブ画面(sub_sr_glassslurryhyoryo)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param pot ﾎﾟｯﾄ
     * @param zairyokubun 材料区分
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void insertSubSrGlassslurryhyoryo(QueryRunner queryRunnerQcdb, Connection conQcdb, 
            BigDecimal newRev, String kojyo, String lotNo, String edaban, 
            Integer pot, Integer zairyokubun, String systemTime) throws SQLException {
        String sql = "INSERT INTO sub_sr_glassslurryhyoryo ( "
                + " kojyo, lotno, edaban, pot, zairyokubun, tyogouryoukikaku, tyogouzanryou, "
                + " zairyohinmei, buzailotno1, buzaihinmei1, tyougouryou1_1, tyougouryou1_2, "
                + " tyougouryou1_3, tyougouryou1_4, tyougouryou1_5, tyougouryou1_6, buzailotno2, "
                + " buzaihinmei2, tyougouryou2_1, tyougouryou2_2, tyougouryou2_3, tyougouryou2_4, "
                + " tyougouryou2_5, tyougouryou2_6, torokunichiji, kosinnichiji, revision "
                + " ) VALUES ("
                + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
        
        List<Object> params = setUpdateParameterSubSrSrGlassslurryhyoryo(true, newRev, kojyo, lotNo, edaban, pot, zairyokubun, systemTime);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力_ｻﾌﾞ画面(sub_sr_glassslurryhyoryo)更新処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param pot ﾎﾟｯﾄ
     * @param zairyokubun 材料区分
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void updateSubSrGlassslurryhyoryo(QueryRunner queryRunnerQcdb, Connection conQcdb, 
            BigDecimal rev, BigDecimal newRev, String kojyo, String lotNo, String edaban, 
            Integer pot, Integer zairyokubun, String systemTime) throws SQLException {
        
        String sql = "UPDATE sub_sr_glassslurryhyoryo SET "
                + " tyogouryoukikaku = ?, tyogouzanryou = ?, "
                + " zairyohinmei = ?, buzailotno1 = ?, buzaihinmei1 = ?, tyougouryou1_1 = ?, tyougouryou1_2 = ?, "
                + " tyougouryou1_3 = ?, tyougouryou1_4 = ?, tyougouryou1_5 = ?, tyougouryou1_6 = ?, buzailotno2 = ?, "
                + " buzaihinmei2 = ?, tyougouryou2_1 = ?, tyougouryou2_2 = ?, tyougouryou2_3 = ?, tyougouryou2_4 = ?, "
                + " tyougouryou2_5 = ?, tyougouryou2_6 = ?, kosinnichiji = ?, revision = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND pot = ? AND zairyokubun = ? AND revision = ? ";
        
        List<Object> params = setUpdateParameterSubSrSrGlassslurryhyoryo(false, newRev, kojyo, lotNo, edaban, pot, zairyokubun, systemTime);

        // 検索条件
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(pot);
        params.add(zairyokubun);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }
    
    /**
     * ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力_サブ画面から更新値を取得
     *
     * @param gxhdO102c002Model モデルデータ
     * @param pot ﾎﾟｯﾄ
     * @param zairyokubun 材料区分
     * @return 更新値情報
     */
    private ArrayList<Object> getSubGamenData(GXHDO102C002Model gxhdO102c002Model, Integer pot, Integer zairyokubun) {
        GXHDO102C002Model.C002SubGamenData c002subgamendata = GXHDO102C002Logic.getC002subgamendata(gxhdO102c002Model, pot, zairyokubun);
        ArrayList<Object> returnList = new ArrayList<>();
        // 調合量規格
        FXHDD01 tyogouryoukikaku = c002subgamendata.getSubDataTyogouryoukikaku();
        // 調合残量
        FXHDD01 tyogouzanryou = c002subgamendata.getSubDataTyogouzanryou();
        // 部材①
        List<FXHDD01> buzaitab1DataList = c002subgamendata.getSubDataBuzaitab1();
        // 部材②
        List<FXHDD01> buzaitab2DataList = c002subgamendata.getSubDataBuzaitab2();
        returnList.add(tyogouryoukikaku);
        returnList.add(tyogouzanryou);
        returnList.add(buzaitab1DataList);
        returnList.add(buzaitab2DataList);
        return returnList;
    }
    
    /**
     * ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力_サブ画面登録(tmp_sub_sr_glassslurryhyoryo)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param pot ﾎﾟｯﾄ
     * @param zairyokubun 材料区分
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSubSrSrGlassslurryhyoryo(boolean isInsert, BigDecimal newRev, 
            String kojyo, String lotNo, String edaban, Integer pot, Integer zairyokubun, String systemTime) {
        
        List<Object> params = new ArrayList<>();

        // 子画面情報を取得
        GXHDO102C002 beanGXHDO102C002 = (GXHDO102C002) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO102C002);
        GXHDO102C002Model gxhdO102c002Model = beanGXHDO102C002.getGxhdO102c002Model();
        // ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力_サブ画面から更新値を取得
        ArrayList<Object> subGamenDataList = getSubGamenData(gxhdO102c002Model, pot, zairyokubun);
        // 調合量規格
        FXHDD01 tyogouryoukikaku = (FXHDD01)subGamenDataList.get(0);
        // 調合残量
        FXHDD01 tyogouzanryou = (FXHDD01)subGamenDataList.get(1);
        // 部材①
        List<FXHDD01> buzaitab1DataList = (List<FXHDD01>)subGamenDataList.get(2);
        // 部材②
        List<FXHDD01> buzaitab2DataList = (List<FXHDD01>)subGamenDataList.get(3);
        
        if (isInsert) {
            params.add(kojyo); // 工場ｺｰﾄﾞ
            params.add(lotNo); // ﾛｯﾄNo
            params.add(edaban); // 枝番
            params.add(pot); // ﾎﾟｯﾄ
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
     * ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力_サブ画面仮登録(tmp_sub_sr_glassslurryhyoryo)登録処理(削除時)
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
    private void insertDeleteDataTmpSubSrGlassslurryhyoryo(QueryRunner queryRunnerQcdb, 
            Connection conQcdb, BigDecimal newRev, int deleteflag, String kojyo, 
            String lotNo, String edaban, String systemTime) throws SQLException {
        String sql = "INSERT INTO tmp_sub_sr_glassslurryhyoryo( kojyo, lotno, edaban, pot, zairyokubun, tyogouryoukikaku, tyogouzanryou, "
                + "zairyohinmei, buzailotno1, buzaihinmei1, tyougouryou1_1, tyougouryou1_2, "
                + "tyougouryou1_3, tyougouryou1_4, tyougouryou1_5, tyougouryou1_6, buzailotno2, "
                + "buzaihinmei2, tyougouryou2_1, tyougouryou2_2, tyougouryou2_3, tyougouryou2_4, "
                + "tyougouryou2_5, tyougouryou2_6, torokunichiji, kosinnichiji, revision, deleteflag"
                + ") SELECT kojyo, lotno, edaban, pot, zairyokubun, tyogouryoukikaku, tyogouzanryou, "
                + "zairyohinmei, buzailotno1, buzaihinmei1, tyougouryou1_1, tyougouryou1_2, "
                + "tyougouryou1_3, tyougouryou1_4, tyougouryou1_5, tyougouryou1_6, buzailotno2, "
                + "buzaihinmei2, tyougouryou2_1, tyougouryou2_2, tyougouryou2_3, tyougouryou2_4, "
                + "tyougouryou2_5, tyougouryou2_6, ?, ?, ?, ? "
                + " FROM sub_sr_glassslurryhyoryo "
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
     * ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力_サブ画面仮登録(sub_sr_glassslurryhyoryo)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSubSrGlassslurryhyoryo(QueryRunner queryRunnerQcdb, Connection conQcdb, 
            BigDecimal rev, String kojyo, String lotNo, String edaban) throws SQLException {
        
        String sql = "DELETE FROM sub_sr_glassslurryhyoryo "
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
        // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.1_1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO1_SIZAILOTNO1_1, GXHDO102B005Const.POTTO1_TYOUGOURYOU1_1, errorItemList);
        // ﾎﾟｯﾄ1_部材在庫No1_2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO1_SIZAILOTNO1_2, GXHDO102B005Const.POTTO1_TYOUGOURYOU1_2, errorItemList);
        // ﾎﾟｯﾄ1_部材在庫No2_1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO1_SIZAILOTNO2_1, GXHDO102B005Const.POTTO1_TYOUGOURYOU2_1, errorItemList);
        // ﾎﾟｯﾄ1_部材在庫No2_2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO1_SIZAILOTNO2_2, GXHDO102B005Const.POTTO1_TYOUGOURYOU2_2, errorItemList);
        // ﾎﾟｯﾄ1_資材ﾛｯﾄNo.3_1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO1_SIZAILOTNO3_1, GXHDO102B005Const.POTTO1_TYOUGOURYOU3_1, errorItemList);
        // ﾎﾟｯﾄ1_部材在庫No3_2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO1_SIZAILOTNO3_2, GXHDO102B005Const.POTTO1_TYOUGOURYOU3_2, errorItemList);
        // ﾎﾟｯﾄ1_部材在庫No4_1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO1_SIZAILOTNO4_1, GXHDO102B005Const.POTTO1_TYOUGOURYOU4_1, errorItemList);
        // ﾎﾟｯﾄ1_部材在庫No4_2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO1_SIZAILOTNO4_2, GXHDO102B005Const.POTTO1_TYOUGOURYOU4_2, errorItemList);
        
        // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.1_1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO2_SIZAILOTNO1_1, GXHDO102B005Const.POTTO2_TYOUGOURYOU1_1, errorItemList);
        // ﾎﾟｯﾄ2_部材在庫No1_2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO2_SIZAILOTNO1_2, GXHDO102B005Const.POTTO2_TYOUGOURYOU1_2, errorItemList);
        // ﾎﾟｯﾄ2_部材在庫No2_1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO2_SIZAILOTNO2_1, GXHDO102B005Const.POTTO2_TYOUGOURYOU2_1, errorItemList);
        // ﾎﾟｯﾄ2_部材在庫No2_2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO2_SIZAILOTNO2_2, GXHDO102B005Const.POTTO2_TYOUGOURYOU2_2, errorItemList);
        // ﾎﾟｯﾄ2_資材ﾛｯﾄNo.3_1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO2_SIZAILOTNO3_1, GXHDO102B005Const.POTTO2_TYOUGOURYOU3_1, errorItemList);
        // ﾎﾟｯﾄ2_部材在庫No3_2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO2_SIZAILOTNO3_2, GXHDO102B005Const.POTTO2_TYOUGOURYOU3_2, errorItemList);
        // ﾎﾟｯﾄ2_部材在庫No4_1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO2_SIZAILOTNO4_1, GXHDO102B005Const.POTTO2_TYOUGOURYOU4_1, errorItemList);
        // ﾎﾟｯﾄ2_部材在庫No4_2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO2_SIZAILOTNO4_2, GXHDO102B005Const.POTTO2_TYOUGOURYOU4_2, errorItemList);
        
        // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.1_1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO3_SIZAILOTNO1_1, GXHDO102B005Const.POTTO3_TYOUGOURYOU1_1, errorItemList);
        // ﾎﾟｯﾄ3_部材在庫No1_2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO3_SIZAILOTNO1_2, GXHDO102B005Const.POTTO3_TYOUGOURYOU1_2, errorItemList);
        // ﾎﾟｯﾄ3_部材在庫No2_1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO3_SIZAILOTNO2_1, GXHDO102B005Const.POTTO3_TYOUGOURYOU2_1, errorItemList);
        // ﾎﾟｯﾄ3_部材在庫No2_2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO3_SIZAILOTNO2_2, GXHDO102B005Const.POTTO3_TYOUGOURYOU2_2, errorItemList);
        // ﾎﾟｯﾄ3_資材ﾛｯﾄNo.3_1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO3_SIZAILOTNO3_1, GXHDO102B005Const.POTTO3_TYOUGOURYOU3_1, errorItemList);
        // ﾎﾟｯﾄ3_部材在庫No3_2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO3_SIZAILOTNO3_2, GXHDO102B005Const.POTTO3_TYOUGOURYOU3_2, errorItemList);
        // ﾎﾟｯﾄ3_部材在庫No4_1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO3_SIZAILOTNO4_1, GXHDO102B005Const.POTTO3_TYOUGOURYOU4_1, errorItemList);
        // ﾎﾟｯﾄ3_部材在庫No4_2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO3_SIZAILOTNO4_2, GXHDO102B005Const.POTTO3_TYOUGOURYOU4_2, errorItemList);
        
        // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.1_1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO4_SIZAILOTNO1_1, GXHDO102B005Const.POTTO4_TYOUGOURYOU1_1, errorItemList);
        // ﾎﾟｯﾄ4_部材在庫No1_2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO4_SIZAILOTNO1_2, GXHDO102B005Const.POTTO4_TYOUGOURYOU1_2, errorItemList);
        // ﾎﾟｯﾄ4_部材在庫No2_1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO4_SIZAILOTNO2_1, GXHDO102B005Const.POTTO4_TYOUGOURYOU2_1, errorItemList);
        // ﾎﾟｯﾄ4_部材在庫No2_2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO4_SIZAILOTNO2_2, GXHDO102B005Const.POTTO4_TYOUGOURYOU2_2, errorItemList);
        // ﾎﾟｯﾄ4_資材ﾛｯﾄNo.3_1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO4_SIZAILOTNO3_1, GXHDO102B005Const.POTTO4_TYOUGOURYOU3_1, errorItemList);
        // ﾎﾟｯﾄ4_部材在庫No3_2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO4_SIZAILOTNO3_2, GXHDO102B005Const.POTTO4_TYOUGOURYOU3_2, errorItemList);
        // ﾎﾟｯﾄ4_部材在庫No4_1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO4_SIZAILOTNO4_1, GXHDO102B005Const.POTTO4_TYOUGOURYOU4_1, errorItemList);
        // ﾎﾟｯﾄ4_部材在庫No4_2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO4_SIZAILOTNO4_2, GXHDO102B005Const.POTTO4_TYOUGOURYOU4_2, errorItemList);
        
        // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.1_1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO5_SIZAILOTNO1_1, GXHDO102B005Const.POTTO5_TYOUGOURYOU1_1, errorItemList);
        // ﾎﾟｯﾄ5_部材在庫No1_2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO5_SIZAILOTNO1_2, GXHDO102B005Const.POTTO5_TYOUGOURYOU1_2, errorItemList);
        // ﾎﾟｯﾄ5_部材在庫No2_1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO5_SIZAILOTNO2_1, GXHDO102B005Const.POTTO5_TYOUGOURYOU2_1, errorItemList);
        // ﾎﾟｯﾄ5_部材在庫No2_2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO5_SIZAILOTNO2_2, GXHDO102B005Const.POTTO5_TYOUGOURYOU2_2, errorItemList);
        // ﾎﾟｯﾄ5_資材ﾛｯﾄNo.3_1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO5_SIZAILOTNO3_1, GXHDO102B005Const.POTTO5_TYOUGOURYOU3_1, errorItemList);
        // ﾎﾟｯﾄ5_部材在庫No3_2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO5_SIZAILOTNO3_2, GXHDO102B005Const.POTTO5_TYOUGOURYOU3_2, errorItemList);
        // ﾎﾟｯﾄ5_部材在庫No4_1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO5_SIZAILOTNO4_1, GXHDO102B005Const.POTTO5_TYOUGOURYOU4_1, errorItemList);
        // ﾎﾟｯﾄ5_部材在庫No4_2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO5_SIZAILOTNO4_2, GXHDO102B005Const.POTTO5_TYOUGOURYOU4_2, errorItemList);
        
        // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.1_1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO6_SIZAILOTNO1_1, GXHDO102B005Const.POTTO6_TYOUGOURYOU1_1, errorItemList);
        // ﾎﾟｯﾄ6_部材在庫No1_2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO6_SIZAILOTNO1_2, GXHDO102B005Const.POTTO6_TYOUGOURYOU1_2, errorItemList);
        // ﾎﾟｯﾄ6_部材在庫No2_1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO6_SIZAILOTNO2_1, GXHDO102B005Const.POTTO6_TYOUGOURYOU2_1, errorItemList);
        // ﾎﾟｯﾄ6_部材在庫No2_2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO6_SIZAILOTNO2_2, GXHDO102B005Const.POTTO6_TYOUGOURYOU2_2, errorItemList);
        // ﾎﾟｯﾄ6_資材ﾛｯﾄNo.3_1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO6_SIZAILOTNO3_1, GXHDO102B005Const.POTTO6_TYOUGOURYOU3_1, errorItemList);
        // ﾎﾟｯﾄ6_部材在庫No3_2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO6_SIZAILOTNO3_2, GXHDO102B005Const.POTTO6_TYOUGOURYOU3_2, errorItemList);
        // ﾎﾟｯﾄ6_部材在庫No4_1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO6_SIZAILOTNO4_1, GXHDO102B005Const.POTTO6_TYOUGOURYOU4_1, errorItemList);
        // ﾎﾟｯﾄ6_部材在庫No4_2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO6_SIZAILOTNO4_2, GXHDO102B005Const.POTTO6_TYOUGOURYOU4_2, errorItemList);
        
        // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.1_1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO7_SIZAILOTNO1_1, GXHDO102B005Const.POTTO7_TYOUGOURYOU1_1, errorItemList);
        // ﾎﾟｯﾄ7_部材在庫No1_2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO7_SIZAILOTNO1_2, GXHDO102B005Const.POTTO7_TYOUGOURYOU1_2, errorItemList);
        // ﾎﾟｯﾄ7_部材在庫No2_1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO7_SIZAILOTNO2_1, GXHDO102B005Const.POTTO7_TYOUGOURYOU2_1, errorItemList);
        // ﾎﾟｯﾄ7_部材在庫No2_2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO7_SIZAILOTNO2_2, GXHDO102B005Const.POTTO7_TYOUGOURYOU2_2, errorItemList);
        // ﾎﾟｯﾄ7_資材ﾛｯﾄNo.3_1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO7_SIZAILOTNO3_1, GXHDO102B005Const.POTTO7_TYOUGOURYOU3_1, errorItemList);
        // ﾎﾟｯﾄ7_部材在庫No3_2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO7_SIZAILOTNO3_2, GXHDO102B005Const.POTTO7_TYOUGOURYOU3_2, errorItemList);
        // ﾎﾟｯﾄ7_部材在庫No4_1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO7_SIZAILOTNO4_1, GXHDO102B005Const.POTTO7_TYOUGOURYOU4_1, errorItemList);
        // ﾎﾟｯﾄ7_部材在庫No4_2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO7_SIZAILOTNO4_2, GXHDO102B005Const.POTTO7_TYOUGOURYOU4_2, errorItemList);
        
        // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.1_1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO8_SIZAILOTNO1_1, GXHDO102B005Const.POTTO8_TYOUGOURYOU1_1, errorItemList);
        // ﾎﾟｯﾄ8_部材在庫No1_2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO8_SIZAILOTNO1_2, GXHDO102B005Const.POTTO8_TYOUGOURYOU1_2, errorItemList);
        // ﾎﾟｯﾄ8_部材在庫No2_1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO8_SIZAILOTNO2_1, GXHDO102B005Const.POTTO8_TYOUGOURYOU2_1, errorItemList);
        // ﾎﾟｯﾄ8_部材在庫No2_2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO8_SIZAILOTNO2_2, GXHDO102B005Const.POTTO8_TYOUGOURYOU2_2, errorItemList);
        // ﾎﾟｯﾄ8_資材ﾛｯﾄNo.3_1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO8_SIZAILOTNO3_1, GXHDO102B005Const.POTTO8_TYOUGOURYOU3_1, errorItemList);
        // ﾎﾟｯﾄ8_部材在庫No3_2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO8_SIZAILOTNO3_2, GXHDO102B005Const.POTTO8_TYOUGOURYOU3_2, errorItemList);
        // ﾎﾟｯﾄ8_部材在庫No4_1に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO8_SIZAILOTNO4_1, GXHDO102B005Const.POTTO8_TYOUGOURYOU4_1, errorItemList);
        // ﾎﾟｯﾄ8_部材在庫No4_2に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO102B005Const.POTTO8_SIZAILOTNO4_2, GXHDO102B005Const.POTTO8_TYOUGOURYOU4_2, errorItemList);
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
        FXHDD01 itemFxhdd01Sizailotno = getItemRow(processData.getItemListEx(), sizailotnoStr);
        if (itemFxhdd01Sizailotno == null || StringUtil.isEmpty(itemFxhdd01Sizailotno.getValue())) {
            return;
        }
        // 部材在庫NoX_Y
        String sizailotnoValue = StringUtil.nullToBlank(itemFxhdd01Sizailotno.getValue());

        FXHDD01 itemFxhdd01Tyougouryou = getItemRow(processData.getItemListEx(), tyougouryouStr);
        if (itemFxhdd01Tyougouryou != null) {
            // 調合量X_Y
            tyougouryouValue = StringUtil.nullToBlank(itemFxhdd01Tyougouryou.getValue());
        }
        FXHDD01 itemFxhdd01Wiplotno = getItemRow(processData.getItemList(), GXHDO102B005Const.SEIHIN_WIPLOTNO);
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
}
