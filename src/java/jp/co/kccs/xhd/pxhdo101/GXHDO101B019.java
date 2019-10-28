/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

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
import jp.co.kccs.xhd.common.InitMessage;
import jp.co.kccs.xhd.common.KikakuError;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.db.model.SrSaisanka;
import jp.co.kccs.xhd.pxhdo901.ErrorMessageInfo;
import jp.co.kccs.xhd.pxhdo901.IFormLogic;
import jp.co.kccs.xhd.pxhdo901.ProcessData;
import jp.co.kccs.xhd.util.DBUtil;
import jp.co.kccs.xhd.util.DateUtil;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.StringUtil;
import jp.co.kccs.xhd.util.ValidateUtil;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import jp.co.kccs.xhd.pxhdo901.KikakuchiInputErrorInfo;
import jp.co.kccs.xhd.util.CommonUtil;
import jp.co.kccs.xhd.util.SubFormUtil;
import org.apache.commons.dbutils.DbUtils;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/06/20<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCCS D.Yanagida<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101B019(焼成・再酸化)ロジック
 *
 * @author KCCS D.Yanagida
 * @since 2019/06/20
 */
public class GXHDO101B019 implements IFormLogic {
    private static final Logger LOGGER = Logger.getLogger(GXHDO101B019.class.getName());
    private static final String JOTAI_FLG_KARI_TOROKU = "0";
    private static final String JOTAI_FLG_TOROKUZUMI = "1";
    private static final String JOTAI_FLG_SAKUJO = "9";
    private static final String SQL_STATE_RECORD_LOCK_ERR = "55P03";
    
//<editor-fold defaultstate="collapsed" desc="#INITIAL">
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

            // 初期表示データ設定処理
            processData = setInitDate(processData);
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

            //日付入力をチェック処理なし(処理時にエラーの背景色を戻さない機能として登録)
            processData.setNoCheckButtonId(Arrays.asList(
                    GXHDO101B019Const.BTN_STARTDATETIME_TOP,
                    GXHDO101B019Const.BTN_STARTDATETIME_BOTTOM,
                    GXHDO101B019Const.BTN_ENDDATETIME_TOP,
                    GXHDO101B019Const.BTN_ENDDATETIME_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO101B019Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO101B019Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO101B019Const.BTN_INSERT_TOP,
                    GXHDO101B019Const.BTN_INSERT_BOTTOM,
                    GXHDO101B019Const.BTN_DELETE_TOP,
                    GXHDO101B019Const.BTN_DELETE_BOTTOM,
                    GXHDO101B019Const.BTN_UPDATE_TOP,
                    GXHDO101B019Const.BTN_UPDATE_BOTTOM));

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

        } catch (NumberFormatException ex) {
            ErrUtil.outputErrorLog("NumberFormatException", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            return processData;
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            return processData;
        }

        return processData;
    }
    
    /**
     * 初期表示データ設定
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     * @throws SQLException 例外エラー
     * @throws NumberFormatException 例外エラー
     */
    private ProcessData setInitDate(ProcessData processData) throws SQLException, NumberFormatException {

        QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
        QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
        QueryRunner queryRunnerWip = new QueryRunner(processData.getDataSourceWip());

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String lotNo = (String) session.getAttribute("lotNo");
        String formId = StringUtil.nullToBlank(session.getAttribute("formId"));
        int jissekino = Integer.parseInt(StringUtil.nullToBlank(session.getAttribute("jissekino")));

        // エラーメッセージリスト
        List<String> errorMessageList = processData.getInitMessageList();

        // 設計情報の取得
        Map sekkeiData = this.loadSekkeiData(queryRunnerQcdb, lotNo);
        if (sekkeiData == null || sekkeiData.isEmpty()) {
            errorMessageList.clear();
            errorMessageList.add(MessageUtil.getMessage("XHD-000014"));
            processData.setFatalError(true);
            processData.setInitMessageList(errorMessageList);
            return processData;
        }

        // 設計情報チェック(対象のデータが取得出来ていない場合エラー)
        errorMessageList.addAll(ValidateUtil.checkSekkeiUnsetItems(sekkeiData, getMapSekkeiAssociation()));

        //仕掛情報の取得
        Map shikakariData = loadShikakariData(queryRunnerWip, lotNo);
        if (shikakariData == null || shikakariData.isEmpty()) {
            errorMessageList.add(MessageUtil.getMessage("XHD-000029"));
        }
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); //ﾛｯﾄ区分ｺｰﾄﾞ
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));// ｵｰﾅｰｺｰﾄﾞ

        // ﾛｯﾄ区分ﾏｽﾀ情報の取得
        Map lotKbnMasData = loadLotKbnMas(queryRunnerWip, lotkubuncode);
        if (lotKbnMasData == null || lotKbnMasData.isEmpty()) {
            errorMessageList.add(MessageUtil.getMessage("XHD-000015"));
        }

        // ｵｰﾅｰﾏｽﾀ情報の取得
        Map ownerMasData = loadOwnerMas(queryRunnerWip, ownercode);
        if (ownerMasData == null || ownerMasData.isEmpty()) {
            errorMessageList.add(MessageUtil.getMessage("XHD-000016"));
        }

        // 入力項目の情報を画面にセットする。
        if (!setInputItemData(processData, queryRunnerDoc, queryRunnerQcdb, lotNo, formId, jissekino)) {
            // エラー発生時は処理を中断
            processData.setFatalError(true);
            processData.setInitMessageList(Arrays.asList(MessageUtil.getMessage("XHD-000038")));
            return processData;
        }

        // 画面に取得した情報をセットする。(入力項目以外)
        setViewItemData(processData, lotKbnMasData, ownerMasData, shikakariData, lotNo);

        processData.setInitMessageList(errorMessageList);
        return processData;
    }
    
    /**
     * 入力項目以外のデータを画面項目に設定
     *
     * @param processData 処理制御データ
     * @param lotKbnMasData ﾛｯﾄ区分ﾏｽﾀデータ
     * @param ownerMasData ｵｰﾅｰﾏｽﾀデータ
     * @param shikakariData 仕掛データ
     * @param lotNo ﾛｯﾄNo
     */
    private void setViewItemData(ProcessData processData, Map lotKbnMasData, Map ownerMasData, Map shikakariData, String lotNo) {

        // ロットNo
        this.setItemData(processData, GXHDO101B019Const.LOTNO, lotNo);
        // KCPNO
        this.setItemData(processData, GXHDO101B019Const.KCPNO, StringUtil.nullToBlank(getMapData(shikakariData, "kcpno")));
        // 客先
        this.setItemData(processData, GXHDO101B019Const.KYAKUSAKI, StringUtil.nullToBlank(getMapData(shikakariData, "tokuisaki")));

        // ロット区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); //ﾛｯﾄ区分ｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B019Const.LOT_KUBUN, "");
        } else {
            String lotKubun = StringUtil.nullToBlank(getMapData(lotKbnMasData, "lotkubun"));
            this.setItemData(processData, GXHDO101B019Const.LOT_KUBUN, lotkubuncode + ":" + lotKubun);
        }

        // オーナー
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));// ｵｰﾅｰｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B019Const.OWNER, "");
        } else {
            String owner = StringUtil.nullToBlank(getMapData(ownerMasData, "ownername"));
            this.setItemData(processData, GXHDO101B019Const.OWNER, ownercode + ":" + owner);
        }

        // 指示
        this.setItemData(processData, GXHDO101B019Const.SIJI, "");
        
        // 後工程指示
        this.setItemData(processData, GXHDO101B019Const.ATO_KOTEI_SIJI, "");
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

        List<SrSaisanka> srSaisanka = new ArrayList<>();
        String rev = "";
        String jotaiFlg = "";
        String kojyo = lotNo.substring(0, 3);
        String lotNo8 = lotNo.substring(3, 11);
        String edaban = lotNo.substring(11, 14);

        for (int i = 0; i < 5; i++) {
            // 品質DB実績登録Revision情報取得
            Map fxhdd03RevInfo = loadFxhdd03RevInfo(queryRunnerDoc, kojyo, lotNo8, edaban, formId, jissekino);
            rev = StringUtil.nullToBlank(getMapData(fxhdd03RevInfo, "rev"));
            jotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd03RevInfo, "jotai_flg"));

            // revisionが空のまたはjotaiFlgが"0"でも"1"でもない場合、新規としてデフォルト値を設定してリターンする。
            if (StringUtil.isEmpty(rev) || !(JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg) || JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg))) {
                processData.setInitRev(rev);
                processData.setInitJotaiFlg(jotaiFlg);

                // メイン画面にデータを設定する(デフォルト値)
                for (FXHDD01 fxhdd001 : processData.getItemList()) {
                    this.setItemData(processData, fxhdd001.getItemId(), fxhdd001.getInputDefault());
                }
                
                // 前工程情報取得
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                HttpSession session = (HttpSession) externalContext.getSession(false);
                Map maekoteiInfo = (Map) session.getAttribute("maekoteiInfo");
                String maekoteiFormId = StringUtil.nullToBlank(session.getAttribute("maekoteiFormId"));
                
                // 受入ｾｯﾀ枚数(前工程情報がある場合は前工程情報の値をセットする。)
                FXHDD01 itemSettaMaisu = this.getItemRow(processData.getItemList(), GXHDO101B019Const.UKEIRE_SETTA_MAISU);
                if ("GXHDO101B013".equals(maekoteiFormId)) {
                    //前工程がｾｯﾀ詰めの場合、ｾｯﾀ枚数をセット
                    CommonUtil.setMaekoteiInfo(itemSettaMaisu, maekoteiInfo, "sayasuu", false, true);
                } else if ("GXHDO101B014".equals(maekoteiFormId) ||
                           "GXHDO101B015".equals(maekoteiFormId) ||
                           "GXHDO101B016".equals(maekoteiFormId) ||
                           "GXHDO101B017".equals(maekoteiFormId) ||
                           "GXHDO101B018".equals(maekoteiFormId) ||
                           "GXHDO101B019".equals(maekoteiFormId)) {
                    //前工程がAir脱脂、窒素脱脂、2次脱脂(ﾍﾞﾙﾄ)、焼成、RHK焼成、再酸化の場合、回収ｾｯﾀ枚数をセット
                    if ("GXHDO101B014".equals(maekoteiFormId) ||
                        "GXHDO101B015".equals(maekoteiFormId)) {
                        CommonUtil.setMaekoteiInfo(itemSettaMaisu, maekoteiInfo, "kaisyusettersuu", false, true);
                    } else if ("GXHDO101B016".equals(maekoteiFormId)) {
                        CommonUtil.setMaekoteiInfo(itemSettaMaisu, maekoteiInfo, "kaishuusettasuu", false, true);
                    } else if ("GXHDO101B017".equals(maekoteiFormId) ||
                               "GXHDO101B018".equals(maekoteiFormId) ||
                               "GXHDO101B019".equals(maekoteiFormId)) {
                        CommonUtil.setMaekoteiInfo(itemSettaMaisu, maekoteiInfo, "kaishusettasuu", false, true);
                    }
                }

                return true;
            }

            // 焼成データ取得
            srSaisanka = getSrSaisankaData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban, jissekino);
            if (srSaisanka.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srSaisanka.isEmpty()) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srSaisanka.get(0));

        return true;
    }
    
    /**
     * 設計データ関連付けマップ取得
     *
     * @return 設計データ関連付けリスト
     */
    private List<String[]> getMapSekkeiAssociation() {

        // 対象無し(共通的なチェック処理の為、ロジックは残しておく)
        List<String[]> list = new ArrayList<>();
        return list;
    }
//</editor-fold>
    
//<editor-fold defaultstate="collapsed" desc="#PRIVATE LOGIC">
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
                        GXHDO101B019Const.BTN_COPY_EDABAN_TOP,
                        GXHDO101B019Const.BTN_COPY_EDABAN_BOTTOM,
                        GXHDO101B019Const.BTN_UPDATE_TOP,
                        GXHDO101B019Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B019Const.BTN_DELETE_TOP,
                        GXHDO101B019Const.BTN_DELETE_BOTTOM,
                        GXHDO101B019Const.BTN_STARTDATETIME_TOP,
                        GXHDO101B019Const.BTN_STARTDATETIME_BOTTOM,
                        GXHDO101B019Const.BTN_ENDDATETIME_TOP,
                        GXHDO101B019Const.BTN_ENDDATETIME_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B019Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B019Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B019Const.BTN_INSERT_TOP,
                        GXHDO101B019Const.BTN_INSERT_BOTTOM
                ));
                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO101B019Const.BTN_COPY_EDABAN_TOP,
                        GXHDO101B019Const.BTN_COPY_EDABAN_BOTTOM,
                        GXHDO101B019Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B019Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B019Const.BTN_INSERT_TOP,
                        GXHDO101B019Const.BTN_INSERT_BOTTOM,
                        GXHDO101B019Const.BTN_STARTDATETIME_TOP,
                        GXHDO101B019Const.BTN_STARTDATETIME_BOTTOM,
                        GXHDO101B019Const.BTN_ENDDATETIME_TOP,
                        GXHDO101B019Const.BTN_ENDDATETIME_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B019Const.BTN_UPDATE_TOP,
                        GXHDO101B019Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B019Const.BTN_DELETE_TOP,
                        GXHDO101B019Const.BTN_DELETE_BOTTOM
                ));
                break;
        }
        processData.setActiveButtonId(activeIdList);
        processData.setInactiveButtonId(inactiveIdList);

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
            case GXHDO101B019Const.BTN_COPY_EDABAN_TOP:
            case GXHDO101B019Const.BTN_COPY_EDABAN_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 仮登録
            case GXHDO101B019Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO101B019Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO101B019Const.BTN_INSERT_TOP:
            case GXHDO101B019Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 修正
            case GXHDO101B019Const.BTN_UPDATE_TOP:
            case GXHDO101B019Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO101B019Const.BTN_DELETE_TOP:
            case GXHDO101B019Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 開始日時
            case GXHDO101B019Const.BTN_STARTDATETIME_TOP:
            case GXHDO101B019Const.BTN_STARTDATETIME_BOTTOM:
                method = "setKaishiDateTime";
                break;
            // 終了日時
            case GXHDO101B019Const.BTN_ENDDATETIME_TOP:
            case GXHDO101B019Const.BTN_ENDDATETIME_BOTTOM:
                method = "setShuryouDateTime";
                break;
            default:
                method = "error";
                break;
        }
        
        return method;
    }
    
    /**
     * メイン画面データ設定処理(入力項目のみ)
     * 
     * @param processData 処理制御データ
     * @param srSaisanka 再酸化データ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrSaisanka srSaisanka) {
        // 受入ｾｯﾀ枚数
        this.setItemData(processData, GXHDO101B019Const.UKEIRE_SETTA_MAISU, getSaisankaItemData(GXHDO101B019Const.UKEIRE_SETTA_MAISU, srSaisanka));
        // 指定再酸化回数
        this.setItemData(processData, GXHDO101B019Const.SITEI_SAISANKA_KAISU, getSaisankaItemData(GXHDO101B019Const.SITEI_SAISANKA_KAISU, srSaisanka));
        // 投入ｾｯﾀ枚数
        this.setItemData(processData, GXHDO101B019Const.TOUNYU_SETTA_MAISU, getSaisankaItemData(GXHDO101B019Const.TOUNYU_SETTA_MAISU, srSaisanka));
        // 号機
        this.setItemData(processData, GXHDO101B019Const.GOKI, getSaisankaItemData(GXHDO101B019Const.GOKI, srSaisanka));
        // 設定ﾊﾟﾀｰﾝ
        this.setItemData(processData, GXHDO101B019Const.SETTEI_PATTERN, getSaisankaItemData(GXHDO101B019Const.SETTEI_PATTERN, srSaisanka));
        // ｷｰﾌﾟ温度
        this.setItemData(processData, GXHDO101B019Const.KEEP_ONDO, getSaisankaItemData(GXHDO101B019Const.KEEP_ONDO, srSaisanka));
        // 後外観確認
        this.setItemData(processData, GXHDO101B019Const.ATO_GAIKAN_CHECK, getSaisankaItemData(GXHDO101B019Const.ATO_GAIKAN_CHECK, srSaisanka));
        // 回収ｾｯﾀ枚数
        this.setItemData(processData, GXHDO101B019Const.KAISHU_SETTA_MAISU, getSaisankaItemData(GXHDO101B019Const.KAISHU_SETTA_MAISU, srSaisanka));
        // 開始日
        this.setItemData(processData, GXHDO101B019Const.KAISHI_DAY, getSaisankaItemData(GXHDO101B019Const.KAISHI_DAY, srSaisanka));
        // 開始時間
        this.setItemData(processData, GXHDO101B019Const.KAISHI_TIME, getSaisankaItemData(GXHDO101B019Const.KAISHI_TIME, srSaisanka));
        // 開始担当者
        this.setItemData(processData, GXHDO101B019Const.KAISHI_TANTOUSYA, getSaisankaItemData(GXHDO101B019Const.KAISHI_TANTOUSYA, srSaisanka));
        // 開始確認者
        this.setItemData(processData, GXHDO101B019Const.KAISHI_KAKUNINSYA, getSaisankaItemData(GXHDO101B019Const.KAISHI_KAKUNINSYA, srSaisanka));
        // 終了日
        this.setItemData(processData, GXHDO101B019Const.SHURYOU_DAY, getSaisankaItemData(GXHDO101B019Const.SHURYOU_DAY, srSaisanka));
        // 終了時間
        this.setItemData(processData, GXHDO101B019Const.SHURYOU_TIME, getSaisankaItemData(GXHDO101B019Const.SHURYOU_TIME, srSaisanka));
        // 終了担当者
        this.setItemData(processData, GXHDO101B019Const.SHURYOU_TANTOUSYA, getSaisankaItemData(GXHDO101B019Const.SHURYOU_TANTOUSYA, srSaisanka));
        // 備考1
        this.setItemData(processData, GXHDO101B019Const.BIKOU1, getSaisankaItemData(GXHDO101B019Const.BIKOU1, srSaisanka));
        // 備考2
        this.setItemData(processData, GXHDO101B019Const.BIKOU2, getSaisankaItemData(GXHDO101B019Const.BIKOU2, srSaisanka));
    }
    
    /**
     * 項目IDに該当するDBの値を取得する。
     * 
     * @param itemId 項目ID
     * @param data 押切ｶｯﾄﾃﾞｰﾀ
     * @return  DB値
     */
    private String getSaisankaItemData(String itemId, SrSaisanka data) {
        switch (itemId) {
            // KCPNo
            case GXHDO101B019Const.KCPNO:
                return StringUtil.nullToBlank(data.getKCPNO());
            // 受入ｾｯﾀ枚数
            case GXHDO101B019Const.UKEIRE_SETTA_MAISU:
                return StringUtil.nullToBlank(data.getUkeiresettamaisuu());
            // 指定再酸化回数
            case GXHDO101B019Const.SITEI_SAISANKA_KAISU:
                return StringUtil.nullToBlank(data.getSiteisaisaka());
            // 投入ｾｯﾀ枚数
            case GXHDO101B019Const.TOUNYU_SETTA_MAISU:
                return StringUtil.nullToBlank(data.getTounyusettasuu());
            // 号機
            case GXHDO101B019Const.GOKI:
                return StringUtil.nullToBlank(data.getGouki());
            // 設定ﾊﾟﾀｰﾝ
            case GXHDO101B019Const.SETTEI_PATTERN:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(data.getSetteipattern()));
            // ｷｰﾌﾟ温度
            case GXHDO101B019Const.KEEP_ONDO:
                return StringUtil.nullToBlank(data.getKeepondo());
            // 後外観確認
            case GXHDO101B019Const.ATO_GAIKAN_CHECK:
                return getNgOkBlank(data.getAtogaikan());
            // 回収ｾｯﾀ枚数
            case GXHDO101B019Const.KAISHU_SETTA_MAISU:
                return StringUtil.nullToBlank(data.getKaishusettasuu());
            // 開始日
            case GXHDO101B019Const.KAISHI_DAY:
                return DateUtil.formattedTimestamp(data.getKaisinichiji(), "yyMMdd");
            // 開始時間
            case GXHDO101B019Const.KAISHI_TIME:
                return DateUtil.formattedTimestamp(data.getKaisinichiji(), "HHmm");
            // 開始担当者
            case GXHDO101B019Const.KAISHI_TANTOUSYA:
                return StringUtil.nullToBlank(data.getStartTantosyacode());
            // 開始確認者
            case GXHDO101B019Const.KAISHI_KAKUNINSYA:
                return StringUtil.nullToBlank(data.getStartKakuninsyacode());
            // 終了日
            case GXHDO101B019Const.SHURYOU_DAY:
                return DateUtil.formattedTimestamp(data.getSyuuryounichiji(), "yyMMdd");
            // 終了時間
            case GXHDO101B019Const.SHURYOU_TIME:
                return DateUtil.formattedTimestamp(data.getSyuuryounichiji(), "HHmm");
            // 終了担当者
            case GXHDO101B019Const.SHURYOU_TANTOUSYA:
                return StringUtil.nullToBlank(data.getEndTantosyacode());
            // 備考1
            case GXHDO101B019Const.BIKOU1:
                return StringUtil.nullToBlank(data.getBikou1());
            // 備考2
            case GXHDO101B019Const.BIKOU2:
                return StringUtil.nullToBlank(data.getBikou2());
            default:
                return null;
        }
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
//</editor-fold>    
    
//<editor-fold defaultstate="collapsed" desc="#BUTTON EVENT">
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
            QueryRunner queryRunnerWip = new QueryRunner(processData.getDataSourceWip());
            QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());

            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            String formId = StringUtil.nullToBlank(session.getAttribute("formId"));
            String lotNo = (String) session.getAttribute("lotNo");
            String kojyo = lotNo.substring(0, 3);
            String lotNo8 = lotNo.substring(3, 11);
            int jissekino = Integer.parseInt(StringUtil.nullToBlank(session.getAttribute("jissekino")));

            //仕掛情報の取得
            Map shikakariData = loadShikakariData(queryRunnerWip, lotNo);
            String oyalotEdaban = StringUtil.nullToBlank(getMapData(shikakariData, "oyalotedaban")); //親ﾛｯﾄ枝番

            // 品質DB登録実績データ取得
            Map fxhdd03RevInfo = loadFxhdd03RevInfo(queryRunnerDoc, kojyo, lotNo8, oyalotEdaban, formId, jissekino);
            if (fxhdd03RevInfo == null || fxhdd03RevInfo.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            String jotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd03RevInfo, "jotai_flg"));

            if (!(JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg) || JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg))) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // [再酸化]データ取得
            List<SrSaisanka> srSaisankaDataList = getSrSaisankaData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban, jissekino);
            if (srSaisankaDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // メイン画面データ設定
            setInputItemDataMainForm(processData, srSaisankaDataList.get(0));

            // 次呼出しメソッドをクリア
            processData.setMethod("");

            return processData;
        } catch (NumberFormatException ex) {
            ErrUtil.outputErrorLog("NumberFormatException発生", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
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
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData checkDataTempRegist(ProcessData processData) {
        // 項目のチェック処理を行う。
        ErrorMessageInfo checkItemErrorInfo = checkItemTempRegist(processData);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }

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
     * 仮登録項目チェック
     *
     * @param processData 処理制御データ
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkItemTempRegist(ProcessData processData) {
        // 仮登録時の個別チェックを記述(共通的なチェック処理の為、ロジックは残しておく)
        return null;
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
        processData.setMethod("doRegist");

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

        // 警告メッセージの設定
        processData.setWarnMessage("修正します。よろしいですか？");

        // ユーザ認証用のパラメータをセットする。
        processData.setRquireAuth(true);
        processData.setUserAuthParam(GXHDO101B019Const.USER_AUTH_UPDATE_PARAM);

        // 後続処理メソッド設定
        processData.setMethod("doCorrect");

        return processData;
    }
    
    /**
     * 登録・修正項目チェック
     *
     * @param processData 処理制御データ
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkItemRegistCorrect(ProcessData processData) {
        // 設定ﾊﾟﾀｰﾝ
        FXHDD01 setteiPattern = getItemRow(processData.getItemList(), GXHDO101B019Const.SETTEI_PATTERN);
        if (!"true".equals(setteiPattern.getValue())) {
            List<FXHDD01> errFxhdd01List = Arrays.asList(setteiPattern);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, setteiPattern.getLabel1());
        }
        
        // 開始日時、終了日時前後チェック
        FXHDD01 itemKaishiDay = getItemRow(processData.getItemList(), GXHDO101B019Const.KAISHI_DAY);
        FXHDD01 itemKaishiTime = getItemRow(processData.getItemList(), GXHDO101B019Const.KAISHI_TIME);
        Date kaishiDate = DateUtil.convertStringToDate(itemKaishiDay.getValue(), itemKaishiTime.getValue());
        FXHDD01 itemShuryouDay = getItemRow(processData.getItemList(), GXHDO101B019Const.SHURYOU_DAY);
        FXHDD01 itemShuryouTime = getItemRow(processData.getItemList(), GXHDO101B019Const.SHURYOU_TIME);
        Date shuryoDate = DateUtil.convertStringToDate(itemShuryouDay.getValue(), itemShuryouTime.getValue());
        // R001チェック呼び出し
        ValidateUtil validateUtil = new ValidateUtil();
        String msgCheckR001 = validateUtil.checkR001(itemKaishiDay.getLabel1(), kaishiDate, itemShuryouDay.getLabel1(), shuryoDate);
        if (!StringUtil.isEmpty(msgCheckR001)) {
            //エラー発生時
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemKaishiDay, itemKaishiTime, itemShuryouDay, itemShuryouTime);
            return MessageUtil.getErrorMessageInfo("", msgCheckR001, true, true, errFxhdd01List);
        }
        
        return null;
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
        processData.setUserAuthParam(GXHDO101B019Const.USER_AUTH_DELETE_PARAM);

        // 後続処理メソッド設定
        processData.setMethod("doDelete");

        return processData;
    }
    
    /**
     * 開始時間設定処理
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKaishiDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B019Const.KAISHI_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B019Const.KAISHI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processDate.setMethod("");
        return processDate;
    }

    /**
     * 終了時間設定処理
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setShuryouDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B019Const.SHURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B019Const.SHURYOU_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }

        processDate.setMethod("");
        return processDate;
    }

    /**
     * 日付(日、時間)の項目にフォーマットの日付(yyMMdd,HHmm)をセットする
     *
     * @param itemDay 日付項目(日)
     * @param itemTime 日付項目(時間)
     * @param setDateTime 設定時間
     */
    private void setDateTimeItem(FXHDD01 itemDay, FXHDD01 itemTime, Date setDateTime) {
        itemDay.setValue(new SimpleDateFormat("yyMMdd").format(setDateTime));
        itemTime.setValue(new SimpleDateFormat("HHmm").format(setDateTime));
    }
//</editor-fold>    
    
//<editor-fold defaultstate="collapsed" desc="#DB SELECT">
    /**
     * [設計]から、初期表示する情報を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param lotNo ロットNo(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadSekkeiData(QueryRunner queryRunnerQcdb, String lotNo) throws SQLException {
        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 11);
        // 設計データの取得
        String sql = "SELECT SEKKEINO,"
                + "GENRYOU,ETAPE,EATUMI,SOUSUU,EMAISUU,SYURUI2,ATUMI2,"
                + "MAISUU2,SYURUI3,ATUMI3,MAISUU3,PATTERN,LASTLAYERSLIDERYO "
                + "FROM da_sekkei "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = '001'";
        
        List<Object> params = new ArrayList<>(Arrays.asList(lotNo1, lotNo2));
        
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }
    
    /**
     * [製版ﾏｽﾀ]からデータを取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param pattern 電極製版名(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadDaPatternMas(QueryRunner queryRunnerQcdb, String pattern) throws SQLException {

        // 製版ﾏｽﾀデータの取得
        String sql = "SELECT LRETU, WRETU, LSUN, WSUN "
                + " FROM da_patternmas WHERE PATTERN = ? ";

        List<Object> params = new ArrayList<>();
        params.add(pattern);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }
    
    /**
     * [仕掛]からデータを取得
     *
     * @param queryRunnerWip QueryRunnerオブジェクト
     * @param lotNo ロットNo(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadShikakariData(QueryRunner queryRunnerWip, String lotNo) throws SQLException {
        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 11);
        String lotNo3 = lotNo.substring(11, 14);

        // 仕掛情報データの取得
        String sql = "SELECT kcpno, oyalotedaban, suuryo, torikosuu, lotkubuncode, ownercode, tokuisaki"
                + " FROM sikakari WHERE kojyo = ? AND lotno = ? AND edaban = ? ";

        List<Object> params = new ArrayList<>(Arrays.asList(lotNo1, lotNo2, lotNo3));

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerWip.query(sql, new MapHandler(), params.toArray());
    }
    
    /**
     * [ﾛｯﾄ区分ﾏｽﾀｰ]から、ﾛｯﾄ区分を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param lotKubunCode ﾛｯﾄ区分ｺｰﾄﾞ(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadLotKbnMas(QueryRunner queryRunnerDoc, String lotKubunCode) throws SQLException {

        // 設計データの取得
        String sql = "SELECT lotkubun "
                + "FROM lotkumas "
                + "WHERE lotkubuncode = ?";

        List<Object> params = new ArrayList<>();
        params.add(lotKubunCode);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerDoc.query(sql, new MapHandler(), params.toArray());
    }
    
    /**
     * [ｵｰﾅｰｺｰﾄﾞﾏｽﾀｰ]から、ｵｰﾅｰ名を取得
     *
     * @param queryRunnerWip QueryRunnerオブジェクト
     * @param ownerCode ｵｰﾅｰｺｰﾄﾞ(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadOwnerMas(QueryRunner queryRunnerWip, String ownerCode) throws SQLException {
        // オーナーデータの取得
        String sql = "SELECT \"owner\" AS ownername "
                + "FROM ownermas "
                + "WHERE ownercode = ?";

        List<Object> params = new ArrayList<>();
        params.add(ownerCode);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerWip.query(sql, new MapHandler(), params.toArray());
    }
    
    /**
     * [品質DB登録実績]から、リビジョン,状態フラグを取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param formId 画面ID(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhdd03RevInfo(QueryRunner queryRunnerDoc, String kojyo, String lotNo,
            String edaban, String formId, int jissekino) throws SQLException {
        // 登録実績データの取得
        String sql = "SELECT rev, jotai_flg "
                + "FROM fxhdd03 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND gamen_id = ? "
                + "AND jissekino = ?";

        List<Object> params = new ArrayList<>(Arrays.asList(kojyo, lotNo, edaban, formId, jissekino));

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
     * @param formId 画面ID(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhdd03RevInfoWithLock(QueryRunner queryRunnerDoc, Connection conDoc, String kojyo, String lotNo,
            String edaban, String formId, int jissekino) throws SQLException {
        // 設計データの取得
        String sql = "SELECT rev, jotai_flg "
                + "FROM fxhdd03 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND gamen_id = ? "
                + "AND jissekino = ? "
                + "FOR UPDATE NOWAIT ";

        List<Object> params = new ArrayList<>(Arrays.asList(kojyo, lotNo, edaban, formId, jissekino));

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerDoc.query(conDoc, sql, new MapHandler(), params.toArray());
    }
    
    /**
     * [品質DB登録実績]から、最大リビジョン+1のデータを取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param formId 画面ID(検索キー)
     * @param jissekino 実績NO(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private BigDecimal getNewRev(QueryRunner queryRunnerDoc, Connection conDoc, String kojyo, String lotNo,
            String edaban, String formId, int jissekino) throws SQLException {
        BigDecimal newRev = BigDecimal.ONE;
        // 設計データの取得
        String sql = "SELECT MAX(rev) AS rev "
                + "FROM fxhdd03 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND gamen_id = ? "
                + "AND jissekino = ? ";

        List<Object> params = new ArrayList<>(Arrays.asList(kojyo, lotNo, edaban, formId, jissekino));
        Map map = queryRunnerDoc.query(conDoc, sql, new MapHandler(), params.toArray());
        if (map != null && !map.isEmpty()) {
            newRev = new BigDecimal(String.valueOf(map.get("rev")));
            newRev = newRev.add(BigDecimal.ONE);
        }

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return newRev;
    }
    
    /**
     * 焼成の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @param jissekino 実績No
     * @return 焼成登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrSaisanka> getSrSaisankaData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrSaisanka(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
        } else {
            return loadTmpSrSaisanka(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
        }
    }
    
    /**
     * [再酸化]から、ﾃﾞｰﾀを取得
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
    private List<SrSaisanka> loadSrSaisanka(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, String rev) throws SQLException {

        String sql = "SELECT "
                + "kojyo, lotno, edaban, jissekino, KCPNO, ukeiresettamaisuu, siteisaisaka, tounyusettasuu, gouki, setteipattern, " 
                + "keepondo, atogaikan, kaishusettasuu, kaisinichiji, StartTantosyacode, StartKakuninsyacode, syuuryounichiji, " 
                + "EndTantosyacode, bikou1, bikou2, tourokunichiji, koushinnichiji, revision, '0' AS deleteflag "
                + "FROM sr_saisanka "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? ";
        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            sql += "AND revision = ? ";
        }

        List<Object> params = new ArrayList<>(Arrays.asList(kojyo, lotNo, edaban, jissekino));
        if (!StringUtil.isEmpty(rev)) {
            params.add(rev);
        }
        
        Map<String, String> mapping = new HashMap<>();
        mapping.put("kojyo", "kojyo");
        mapping.put("lotno", "lotno");
        mapping.put("edaban", "edaban");
        mapping.put("jissekino", "jissekino");
        mapping.put("KCPNO", "KCPNO");
        mapping.put("ukeiresettamaisuu", "ukeiresettamaisuu");
        mapping.put("siteisaisaka", "siteisaisaka");
        mapping.put("tounyusettasuu", "tounyusettasuu");
        mapping.put("gouki", "gouki");
        mapping.put("setteipattern", "setteipattern");
        mapping.put("keepondo", "keepondo");
        mapping.put("atogaikan", "atogaikan");
        mapping.put("kaishusettasuu", "kaishusettasuu");
        mapping.put("kaisinichiji", "kaisinichiji");
        mapping.put("StartTantosyacode", "StartTantosyacode");
        mapping.put("StartKakuninsyacode", "StartKakuninsyacode");
        mapping.put("syuuryounichiji", "syuuryounichiji");
        mapping.put("EndTantosyacode", "EndTantosyacode");
        mapping.put("bikou1", "bikou1");
        mapping.put("bikou2", "bikou2");
        mapping.put("tourokunichiji", "tourokunichiji");
        mapping.put("koushinnichiji", "koushinnichiji");
        mapping.put("revision", "revision");
        mapping.put("deleteflag", "deleteflag");

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrSaisanka>> beanHandler = new BeanListHandler<>(SrSaisanka.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [再酸化_仮登録]から、ﾃﾞｰﾀを取得
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
    private List<SrSaisanka> loadTmpSrSaisanka(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, String rev) throws SQLException {
        String sql = "SELECT "
                + "kojyo, lotno, edaban, jissekino, KCPNO, ukeiresettamaisuu, siteisaisaka, tounyusettasuu, gouki, setteipattern, " 
                + "keepondo, atogaikan, kaishusettasuu, kaisinichiji, StartTantosyacode, StartKakuninsyacode, syuuryounichiji, " 
                + "EndTantosyacode, bikou1, bikou2, tourokunichiji, koushinnichiji, revision, deleteflag "
                + "FROM tmp_sr_saisanka "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? AND deleteflag = ? ";
        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            sql += "AND revision = ? ";
        }

        List<Object> params = new ArrayList<>(Arrays.asList(kojyo, lotNo, edaban, jissekino, 0));
        if (!StringUtil.isEmpty(rev)) {
            params.add(rev);
        }

        Map<String, String> mapping = new HashMap<>();
        mapping.put("kojyo", "kojyo");
        mapping.put("lotno", "lotno");
        mapping.put("edaban", "edaban");
        mapping.put("jissekino", "jissekino");
        mapping.put("KCPNO", "KCPNO");
        mapping.put("ukeiresettamaisuu", "ukeiresettamaisuu");
        mapping.put("siteisaisaka", "siteisaisaka");
        mapping.put("tounyusettasuu", "tounyusettasuu");
        mapping.put("gouki", "gouki");
        mapping.put("setteipattern", "setteipattern");
        mapping.put("keepondo", "keepondo");
        mapping.put("atogaikan", "atogaikan");
        mapping.put("kaishusettasuu", "kaishusettasuu");
        mapping.put("kaisinichiji", "kaisinichiji");
        mapping.put("StartTantosyacode", "StartTantosyacode");
        mapping.put("StartKakuninsyacode", "StartKakuninsyacode");
        mapping.put("syuuryounichiji", "syuuryounichiji");
        mapping.put("EndTantosyacode", "EndTantosyacode");
        mapping.put("bikou1", "bikou1");
        mapping.put("bikou2", "bikou2");
        mapping.put("tourokunichiji", "tourokunichiji");
        mapping.put("koushinnichiji", "koushinnichiji");
        mapping.put("revision", "revision");
        mapping.put("deleteflag", "deleteflag");

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrSaisanka>> beanHandler = new BeanListHandler<>(SrSaisanka.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }
    
    /**
     * [再酸化_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績NO
     * @return 削除ﾌﾗｸﾞ最大値 + 1
     * @throws SQLException 例外エラー
     */
    private int getNewDeleteflag(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {
        String sql = "SELECT MAX(deleteflag) AS deleteflag "
                + "FROM tmp_sr_saisanka "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? AND jissekino = ? ";
        List<Object> params = new ArrayList<>(Arrays.asList(kojyo, lotNo, edaban, jissekino));

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map resultMap = queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
        int newDeleteFlg = 0;
        if (!StringUtil.isEmpty(StringUtil.nullToBlank(resultMap.get("deleteflag")))) {
            newDeleteFlg = Integer.parseInt(StringUtil.nullToBlank(resultMap.get("deleteflag")));
        }
        newDeleteFlg++;

        return newDeleteFlg;
    }
//</editor-fold>
    
//<editor-fold defaultstate="collapsed" desc="#DB REGIST">
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
            String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
            String lotNo8 = lotNo.substring(3, 11); //ﾛｯﾄNo(8桁)
            String edaban = lotNo.substring(11, 14); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));
            int jissekino = Integer.parseInt(StringUtil.nullToBlank(session.getAttribute("jissekino")));

            // 品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId, jissekino);
            ErrorMessageInfo checkRevMessageInfo = checkRevision(processData, fxhdd03RevInfo);
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
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());

            if (StringUtil.isEmpty(processData.getInitRev())) {
                // 品質DB登録実績登録処理
                insertFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekino, JOTAI_FLG_TOROKUZUMI, systemTime);
            } else {
                rev = new BigDecimal(processData.getInitRev());
                // 最新のリビジョンを採番
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId, jissekino);

                // 品質DB登録実績更新処理
                updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekino, JOTAI_FLG_TOROKUZUMI, systemTime);
            }

            // 仮登録状態の場合、仮登録のデータを削除する。
            SrSaisanka tmpSrSaisanka = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {
                // 更新前の値を取得
                List<SrSaisanka> srSaisankaList = 
                        getSrSaisankaData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo8, edaban, jissekino);
                if (!srSaisankaList.isEmpty()) {
                    tmpSrSaisanka = srSaisankaList.get(0);
                }

                deleteTmpSrSaisanka(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban, jissekino);
            }

            // [再酸化]_登録処理
            insertSrSaisanka(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, jissekino, systemTime, processData.getItemList(), tmpSrSaisanka);

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, jissekino, "0", kikakuError.getKikakuchiInputErrorInfoList());
            }

            // 処理後はエラーリストをクリア
            kikakuError.setKikakuchiInputErrorInfoList(new ArrayList<>());

            DbUtils.commitAndCloseQuietly(conDoc);
            DbUtils.commitAndCloseQuietly(conQcdb);

            // 後続処理メソッド設定
            processData.setMethod("");

            // 完了メッセージとコールバックパラメータを設定
            processData.setCompMessage("登録しました。");
            processData.setCollBackParam("complete");

            return processData;
        } catch (NumberFormatException e) {
            ErrUtil.outputErrorLog("NumberFormatException発生", e, LOGGER);

            // コネクションロールバック処理
            DBUtil.rollbackConnection(conDoc, LOGGER);
            DBUtil.rollbackConnection(conQcdb, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));

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
            String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
            String lotNo8 = lotNo.substring(3, 11); //ﾛｯﾄNo(8桁)
            String edaban = lotNo.substring(11, 14); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));
            int jissekino = Integer.parseInt(StringUtil.nullToBlank(session.getAttribute("jissekino")));

            // 品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId, jissekino);
            ErrorMessageInfo checkRevMessageInfo = checkRevision(processData, fxhdd03RevInfo);
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
            BigDecimal newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId, jissekino);

            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            // 品質DB登録実績更新処理
            updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekino, JOTAI_FLG_TOROKUZUMI, systemTime);

            // [再酸化]_更新処理
            updateSrSaisanka(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, jissekino, systemTime, processData.getItemList());

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, jissekino, "0", kikakuError.getKikakuchiInputErrorInfoList());
            }

            // 処理後はエラーリストをクリア
            kikakuError.setKikakuchiInputErrorInfoList(new ArrayList<>());

            DbUtils.commitAndCloseQuietly(conDoc);
            DbUtils.commitAndCloseQuietly(conQcdb);

            // 後続処理メソッド設定
            processData.setMethod("");

            // 完了メッセージとコールバックパラメータを設定
            processData.setCompMessage("修正しました。");
            processData.setCollBackParam("complete");

            return processData;

        } catch (NumberFormatException e) {
            ErrUtil.outputErrorLog("NumberFormatException発生", e, LOGGER);

            // コネクションロールバック処理
            DBUtil.rollbackConnection(conDoc, LOGGER);
            DBUtil.rollbackConnection(conQcdb, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));

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
            String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
            String lotNo8 = lotNo.substring(3, 11); //ﾛｯﾄNo(8桁)
            String edaban = lotNo.substring(11, 14); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            int jissekino = Integer.parseInt(StringUtil.nullToBlank(session.getAttribute("jissekino")));

            // 品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId, jissekino);
            ErrorMessageInfo checkRevMessageInfo = checkRevision(processData, fxhdd03RevInfo);
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
            BigDecimal newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId, jissekino);

            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            // 品質DB登録実績更新処理
            updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekino, JOTAI_FLG_SAKUJO, systemTime);

            // 焼成_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo8, edaban, jissekino);
            insertDeleteDataTmpSrSaisanka(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, jissekino, systemTime);

            // 焼成_削除処理
            deleteSrSaisanka(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban, jissekino);

            DbUtils.commitAndCloseQuietly(conDoc);
            DbUtils.commitAndCloseQuietly(conQcdb);

            // 後続処理メソッド設定
            processData.setMethod("");

            // 完了メッセージとコールバックパラメータを設定
            processData.setCompMessage("削除しました。");
            processData.setCollBackParam("complete");

            return processData;

        } catch (NumberFormatException e) {
            ErrUtil.outputErrorLog("NumberFormatException発生", e, LOGGER);

            // コネクションロールバック処理
            DBUtil.rollbackConnection(conDoc, LOGGER);
            DBUtil.rollbackConnection(conQcdb, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));

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
            String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
            String lotNo8 = lotNo.substring(3, 11); //ﾛｯﾄNo(8桁)
            String edaban = lotNo.substring(11, 14); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));
            int jissekino = Integer.parseInt(StringUtil.nullToBlank(session.getAttribute("jissekino")));

            // 品質DB登録実績データ取得
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId, jissekino);
            ErrorMessageInfo checkRevMessageInfo = checkRevision(processData, fxhdd03RevInfo);
            // リビジョンエラー時はリターン
            if (checkRevMessageInfo != null) {
                processData.setErrorMessageInfoList(Arrays.asList(checkRevMessageInfo));
                // コネクションロールバック処理
                DBUtil.rollbackConnection(conDoc, LOGGER);
                DBUtil.rollbackConnection(conQcdb, LOGGER);
                return processData;
            }

            BigDecimal newRev = BigDecimal.ONE;
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());

            BigDecimal rev = BigDecimal.ZERO;
            if (StringUtil.isEmpty(processData.getInitJotaiFlg())) {
                // 品質DB登録実績登録処理
                insertFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekino, JOTAI_FLG_KARI_TOROKU, systemTime);
            } else {
                rev = new BigDecimal(processData.getInitRev());
                // 最新のリビジョンを採番
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId, jissekino);

                // 品質DB登録実績更新処理
                updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekino, JOTAI_FLG_KARI_TOROKU, systemTime);
            }

            if (StringUtil.isEmpty(processData.getInitJotaiFlg()) || JOTAI_FLG_SAKUJO.equals(processData.getInitJotaiFlg())) {

                // 再酸化_仮登録登録処理
                insertTmpSrSaisanka(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, jissekino, systemTime, processData.getItemList());

            } else {

                // 再酸化_仮登録更新処理
                updateTmpSrSaisanka(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, jissekino, systemTime, processData.getItemList());
            }

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, jissekino, "0", kikakuError.getKikakuchiInputErrorInfoList());
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
            for (FXHDD01 fxhdd01 : processData.getItemList()) {
                fxhdd01.setBackColorInput(fxhdd01.getBackColorInputDefault());
            }

            // 状態ﾌﾗｸﾞ、revisionを設定する。
            processData.setInitJotaiFlg(JOTAI_FLG_KARI_TOROKU);
            processData.setInitRev(newRev.toPlainString());

            return processData;
        } catch (NumberFormatException e) {
            ErrUtil.outputErrorLog("NumberFormatException発生", e, LOGGER);

            // コネクションロールバック処理
            DBUtil.rollbackConnection(conDoc, LOGGER);
            DBUtil.rollbackConnection(conQcdb, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
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
     * 再酸化(sr_saisanka)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param tmpSrSaisanka 仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrSaisanka(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, SrSaisanka tmpSrSaisanka) throws SQLException {

        String sql = "INSERT INTO sr_saisanka ("
                + "kojyo, lotno, edaban, jissekino, KCPNO, ukeiresettamaisuu, siteisaisaka, tounyusettasuu, gouki, setteipattern, keepondo, " 
                + "atogaikan, kaishusettasuu, kaisinichiji, StartTantosyacode, StartKakuninsyacode, syuuryounichiji, EndTantosyacode, " 
                + "bikou1, bikou2, tourokunichiji, koushinnichiji, revision "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
                + ") ";

        List<Object> params = setUpdateParameterSrSaisanka(true, newRev, kojyo, lotNo, edaban, jissekino, systemTime, itemList, tmpSrSaisanka);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }
    
    /**
     * 再酸化(sr_saisanka)更新処理
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
     * @throws SQLException 例外エラー
     */
    private void updateSrSaisanka(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {
        String sql = "UPDATE sr_saisanka SET "
                + "KCPNO = ?, ukeiresettamaisuu = ?, siteisaisaka = ?, tounyusettasuu = ?, gouki = ?, setteipattern = ?, keepondo = ?, " 
                + "atogaikan = ?, kaishusettasuu = ?, kaisinichiji = ?, StartTantosyacode = ?, StartKakuninsyacode = ?, syuuryounichiji = ?, " 
                + "EndTantosyacode = ?, bikou1 = ?, bikou2 = ?, koushinnichiji = ?, revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? AND jissekino = ? ";

        // 更新前の値を取得
        List<SrSaisanka> srSaisankaList = getSrSaisankaData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekino);
        SrSaisanka srSaisanka = null;
        if (!srSaisankaList.isEmpty()) {
            srSaisanka = srSaisankaList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrSaisanka(false, newRev, "", "", "", jissekino, systemTime, itemList, srSaisanka);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        params.add(jissekino);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }
    
    /**
     * 再酸化(sr_saisanka)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srSaisankaData 再酸化データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrSaisanka(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban, int jissekino,
            Timestamp systemTime, List<FXHDD01> itemList, SrSaisanka srSaisankaData) {
        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
            params.add(jissekino); //実績No
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B019Const.KCPNO, srSaisankaData)));  //KCPNO
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B019Const.UKEIRE_SETTA_MAISU, srSaisankaData)));  //受入ｾｯﾀ枚数
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B019Const.SITEI_SAISANKA_KAISU, srSaisankaData)));  //指定再酸化回数
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B019Const.TOUNYU_SETTA_MAISU, srSaisankaData)));  //投入ｾｯﾀ枚数
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B019Const.GOKI, srSaisankaData)));  //号機
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B019Const.SETTEI_PATTERN, srSaisankaData), 9));  //設定ﾊﾟﾀｰﾝ
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B019Const.KEEP_ONDO, srSaisankaData)));  //ｷｰﾌﾟ温度
        params.add(getNgOkBlankToIntDbValue(getItemData(itemList, GXHDO101B019Const.ATO_GAIKAN_CHECK, srSaisankaData)));  //後外観確認
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B019Const.KAISHU_SETTA_MAISU, srSaisankaData)));  //回収ｾｯﾀ枚数
        params.add(DBUtil.stringToDateObject(
                getItemData(itemList, GXHDO101B019Const.KAISHI_DAY, srSaisankaData),
                getItemData(itemList, GXHDO101B019Const.KAISHI_TIME, srSaisankaData))); //開始日時
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B019Const.KAISHI_TANTOUSYA, srSaisankaData)));  //開始担当者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B019Const.KAISHI_KAKUNINSYA, srSaisankaData)));  //開始確認者
        params.add(DBUtil.stringToDateObject(
                getItemData(itemList, GXHDO101B019Const.SHURYOU_DAY, srSaisankaData),
                getItemData(itemList, GXHDO101B019Const.SHURYOU_TIME, srSaisankaData))); //終了日時
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B019Const.SHURYOU_TANTOUSYA, srSaisankaData)));  //終了担当者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B019Const.BIKOU1, srSaisankaData)));  //備考1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B019Const.BIKOU2, srSaisankaData)));  //備考2
        if (isInsert) {
            params.add(systemTime); //登録日時
        }
        params.add(systemTime); //更新日時
        params.add(newRev); //revision
        return params;
    }
    
    /**
     * 再酸化(sr_saisanka)削除処理
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
    private void deleteSrSaisanka(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        String sql = "DELETE FROM sr_saisanka "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ?  AND jissekino = ? AND revision = ?";

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
     * 再酸化_仮登録(tmp_sr_saisanka)登録処理 ※削除時の履歴登録
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
    private void insertDeleteDataTmpSrSaisanka(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_saisanka ("
                + "kojyo,lotno,edaban,jissekino,KCPNO,ukeiresettamaisuu,siteisaisaka,tounyusettasuu,gouki,setteipattern,keepondo,atogaikan," 
                + "kaishusettasuu,kaisinichiji,StartTantosyacode,StartKakuninsyacode,syuuryounichiji,EndTantosyacode,bikou1,bikou2," 
                + "tourokunichiji,koushinnichiji,revision,deleteflag "
                + ") SELECT "
                + "kojyo,lotno,edaban,jissekino,KCPNO,ukeiresettamaisuu,siteisaisaka,tounyusettasuu,gouki,setteipattern,keepondo,atogaikan," 
                + "kaishusettasuu,kaisinichiji,StartTantosyacode,StartKakuninsyacode,syuuryounichiji,EndTantosyacode,bikou1,bikou2," 
                + "?,?,?,? "
                + "FROM sr_saisanka "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? ";

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
     * 再酸化_仮登録(tmp_sr_saisanka)登録処理
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
     * @param itemList 項目リスト
     * @throws SQLException 例外エラー
     */
    private void insertTmpSrSaisanka(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {

        String sql = "INSERT INTO tmp_sr_saisanka ("
                + "kojyo, lotno, edaban, jissekino, KCPNO, ukeiresettamaisuu, siteisaisaka, tounyusettasuu, gouki, setteipattern, keepondo, " 
                + "atogaikan, kaishusettasuu, kaisinichiji, StartTantosyacode, StartKakuninsyacode, syuuryounichiji, EndTantosyacode, " 
                + "bikou1, bikou2, tourokunichiji, koushinnichiji, revision, deleteflag "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
                + ") ";

        List<Object> params = setUpdateParameterTmpSrSaisanka(true, newRev, deleteflag, kojyo, lotNo, edaban, jissekino, systemTime, itemList, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 再酸化_仮登録(tmp_sr_saisanka)更新処理
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
     * @throws SQLException 例外エラー
     */
    private void updateTmpSrSaisanka(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {

        String sql = "UPDATE tmp_sr_saisanka SET "
                + "KCPNO = ?, ukeiresettamaisuu = ?, siteisaisaka = ?, tounyusettasuu = ?, gouki = ?, setteipattern = ?, keepondo = ?, " 
                + "atogaikan = ?, kaishusettasuu = ?, kaisinichiji = ?, StartTantosyacode = ?, StartKakuninsyacode = ?, syuuryounichiji = ?, " 
                + "EndTantosyacode = ?, bikou1 = ?, bikou2 = ?, koushinnichiji = ?, revision = ?, deleteflag = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? AND jissekino = ? ";

        // 更新前の値を取得
        List<SrSaisanka> srSaisankaList = getSrSaisankaData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekino);
        SrSaisanka srSaisanka = null;
        if (!srSaisankaList.isEmpty()) {
            srSaisanka = srSaisankaList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrSaisanka(false, newRev, 0, "", "", "", jissekino, systemTime, itemList, srSaisanka);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        params.add(jissekino);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }
    
    /**
     * 再酸化_仮登録(tmp_sr_saisanka)更新値パラメータ設定
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
     * @param srSaisankaData 再酸化データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrSaisanka(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, SrSaisanka srSaisankaData) {
        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
            params.add(jissekino); //実績No
        }
        
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B019Const.KCPNO, srSaisankaData)));  //KCPNO
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B019Const.UKEIRE_SETTA_MAISU, srSaisankaData)));  //受入ｾｯﾀ枚数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B019Const.SITEI_SAISANKA_KAISU, srSaisankaData)));  //指定再酸化回数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B019Const.TOUNYU_SETTA_MAISU, srSaisankaData)));  //投入ｾｯﾀ枚数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B019Const.GOKI, srSaisankaData)));  //号機
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B019Const.SETTEI_PATTERN, srSaisankaData), null));  //設定ﾊﾟﾀｰﾝ
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B019Const.KEEP_ONDO, srSaisankaData)));  //ｷｰﾌﾟ温度
        params.add(getNgOkBlankToIntDbValueForTemp(getItemData(itemList, GXHDO101B019Const.ATO_GAIKAN_CHECK, srSaisankaData)));  //後外観確認
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B019Const.KAISHU_SETTA_MAISU, srSaisankaData)));  //回収ｾｯﾀ枚数
        params.add(DBUtil.stringToDateObjectDefaultNull(
                getItemData(itemList, GXHDO101B019Const.KAISHI_DAY, srSaisankaData),
                getItemData(itemList, GXHDO101B019Const.KAISHI_TIME, srSaisankaData))); //開始日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B019Const.KAISHI_TANTOUSYA, srSaisankaData)));  //開始担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B019Const.KAISHI_KAKUNINSYA, srSaisankaData)));  //開始確認者
        params.add(DBUtil.stringToDateObjectDefaultNull(
                getItemData(itemList, GXHDO101B019Const.SHURYOU_DAY, srSaisankaData),
                getItemData(itemList, GXHDO101B019Const.SHURYOU_TIME, srSaisankaData))); //終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B019Const.SHURYOU_TANTOUSYA, srSaisankaData)));  //終了担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B019Const.BIKOU1, srSaisankaData)));  //備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B019Const.BIKOU2, srSaisankaData)));  //備考2
        if (isInsert) {
            params.add(systemTime); //登録日時
        }
        params.add(systemTime); //更新日時
        params.add(newRev); //revision
        params.add(deleteflag); //削除ﾌﾗｸﾞ
        return params;
    }
    
    /**
     * 再酸化_仮登録(tmp_sr_saisanka)削除処理
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
    private void deleteTmpSrSaisanka(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        String sql = "DELETE FROM tmp_sr_saisanka "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ?  AND jissekino = ? AND revision = ?";

        //更新値設定
        List<Object> params = new ArrayList<>(Arrays.asList(kojyo, lotNo, edaban, jissekino, rev));

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }
    
    /**
     * 品質DB登録実績(fxhdd03)登録処理
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
    private void insertFxhdd03(QueryRunner queryRunnerDoc, Connection conDoc, String tantoshaCd, String formId, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino, String jotaiFlg, Timestamp systemTime) throws SQLException {
        String sql = "INSERT INTO fxhdd03 ("
                + "torokusha,toroku_date,koshinsha,koshin_date,gamen_id,rev,kojyo,lotno,"
                + "edaban,jissekino,jotai_flg,tsuika_kotei_flg"
                + ") VALUES ("
                + "?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

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
     * 品質DB登録実績(fxhdd03)更新処理
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param tantoshaCd 担当者ｺｰﾄﾞ
     * @param formId 画面ID
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param systemTime システム日付
     * @throws SQLException 例外ｴﾗｰ
     */
    private void updateFxhdd03(QueryRunner queryRunnerDoc, Connection conDoc, String tantoshaCd, String formId, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino, String jotaiFlg, Timestamp systemTime) throws SQLException {
        String sql = "UPDATE fxhdd03 SET "
                + "koshinsha = ?, koshin_date = ?,"
                + "rev = ?, jotai_flg = ? "
                + "WHERE gamen_id = ? AND kojyo = ? "
                + "  AND lotno = ? AND edaban = ? "
                + "  AND jissekino = ? ";

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
//</editor-fold>
    
//<editor-fold defaultstate="collapsed" desc="#PRIVATE COMMON">
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
     * @param listData 項目リスト
     * @param itemId 項目ID
     * @return 項目データ
     */
    private FXHDD01 getItemRow(List<FXHDD01> listData, String itemId) {
        List<FXHDD01> selectData = 
                listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0);
        } else {
            return null;
        }
    }

    /**
     * 項目データ(入力値)取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @param srSaisanka 再酸化データ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrSaisanka srSaisanka) {
        List<FXHDD01> selectData = 
                listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srSaisanka != null) {
            return getSaisankaItemData(itemId, srSaisanka);
        } else {
            return null;
        }
    }
    
    /**
     * 0→"NG"、1→"OK"、その他→""　変換
     * @param kbn 変換前数値
     * @return "NG" or "OK" or Blank
     */
    private String getNgOkBlank(Integer kbn) {
        if(kbn == null){
            return "";
        }
        
        switch (kbn) {
            case 0:
                return "NG";
            case 1:
                return "OK";
            default:
                return "";
        }
    }
    
    /**
     * "NG"→0、"OK"→1、""→9 変換 (DB登録データ)
     * @param value "NG" or "OK" or Blank
     * @return 変換後の数値
     */
    private Integer getNgOkBlankToIntDbValue(String value) {
        switch (StringUtil.nullToBlank(value)) {
            case "NG":
                return 0;
            case "OK":
                return 1;
            default:
                return 9;
        }
    }
    
    /**
     * "NG"→0、"OK"→1、""→null 変換
     * @param value "NG" or "OK" or Blank
     * @return 変換後の数値
     */
    private Integer getNgOkBlankToIntDbValueForTemp(String value) {
        switch (StringUtil.nullToBlank(value)) {
            case "NG":
                return 0;
            case "OK":
                return 1;
            default:
                return null;
        }
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
     * リビジョンチェック
     *
     * @param processData 処理制御データ
     * @param fxhdd03RevInfo 品質DB登録実績データ
     * @return エラーメッセージ情報
     * @throws SQLException 例外エラー
     */
    private ErrorMessageInfo checkRevision(ProcessData processData, Map fxhdd03RevInfo) throws SQLException {

        if (StringUtil.isEmpty(processData.getInitJotaiFlg())) {
            // 新規の場合、既にデータが存在する場合エラー
            if (fxhdd03RevInfo != null && !fxhdd03RevInfo.isEmpty()) {
                return new ErrorMessageInfo(MessageUtil.getMessage("XHD-000026"));
            }
        } else {
            // 品質DB登録実績データが取得出来ていない場合エラー
            if (fxhdd03RevInfo == null || fxhdd03RevInfo.isEmpty()) {
                return new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"));
            }

            // revisionが更新されていた場合エラー
            if (!processData.getInitRev().equals(StringUtil.nullToBlank(getMapData(fxhdd03RevInfo, "rev")))) {
                return new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"));
            }
        }

        return null;
    }
//</editor-fold>
}
