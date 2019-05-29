/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import jp.co.kccs.xhd.common.KikakuError;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.db.model.SrHapscut;
import jp.co.kccs.xhd.pxhdo901.ErrorMessageInfo;
import jp.co.kccs.xhd.pxhdo901.IFormLogic;
import jp.co.kccs.xhd.pxhdo901.KikakuchiInputErrorInfo;
import jp.co.kccs.xhd.pxhdo901.ProcessData;
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
 * 変更日	2019/05/25<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	KCCS D.Yanagida<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101B010(ｶｯﾄ・押切ｶｯﾄ)ロジック
 *
 * @author KCCS D.Yanagida
 * @since 2019/05/25
 */
public class GXHDO101B010 implements IFormLogic {
    private static final Logger LOGGER = Logger.getLogger(GXHDO101B010.class.getName());
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
    public ProcessData initial(ProcessData processData) {
        try
        {
            // 処理名を登録
            processData.setProcessName("initial");            
            
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
            processData = setButtonEnable(processData, processData.getInitJotaiFlg());
            
            // 押下時にチェック処理を実施しない(エラー背景色を戻さない)ボタンを定義
            processData.setNoCheckButtonId(Arrays.asList(
                    GXHDO101B010Const.BTN_STARTDATETIME_TOP,
                    GXHDO101B010Const.BTN_STARTDATETIME_BOTTOM,
                    GXHDO101B010Const.BTN_ENDDATETIME_TOP,
                    GXHDO101B010Const.BTN_ENDDATETIME_BOTTOM
            ));
            
            // リビジョンチェック対象のボタンを設定
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO101B010Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO101B010Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO101B010Const.BTN_INSERT_TOP,
                    GXHDO101B010Const.BTN_INSERT_BOTTOM,
                    GXHDO101B010Const.BTN_UPDATE_TOP,
                    GXHDO101B010Const.BTN_UPDATE_BOTTOM,
                    GXHDO101B010Const.BTN_DELETE_TOP,
                    GXHDO101B010Const.BTN_DELETE_BOTTOM
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
        String formId = StringUtil.nullToBlank(session.getAttribute("formId"));
        
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
        String pattern = StringUtil.nullToBlank(sekkeiData.get("PATTERN")); //電極製版名 
        
        // 設計情報のチェック(必須データが取得出来ていない場合エラー)
        Map<String, String> sekkeiChkMap = new LinkedHashMap<String, String>() {
            {
                put("PATTERN", "電極製版名");
            }
        };
        errorMessageList.addAll(ValidateUtil.checkSekkeiUnsetItems(sekkeiData, 
                new LinkedHashMap<String, String>(){{put("PATTERN", "電極製版名");}}));
        
        // [製版ﾏｽﾀ]情報取得
        Map daPatternMasData = loadDaPatternMas(queryRunnerQcdb, pattern);
        if (daPatternMasData == null || daPatternMasData.isEmpty()) {
            errorMessageList.add(MessageUtil.getMessage("XHD-000034"));
        }
        
        // [仕掛]情報取得
        Map shikakariData = loadShikakariData(queryRunnerWip, lotNo);
        if (shikakariData == null || shikakariData.isEmpty()) {
            errorMessageList.add(MessageUtil.getMessage("XHD-000029"));
        }
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); //ﾛｯﾄ区分ｺｰﾄﾞ
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));// ｵｰﾅｰｺｰﾄﾞ
        
        // [ﾛｯﾄ区分ﾏｽﾀ]情報取得
        Map lotKbnMasData = loadLotKbnMas(queryRunnerWip, lotkubuncode);
        if (lotKbnMasData == null || lotKbnMasData.isEmpty()) {
            errorMessageList.add(MessageUtil.getMessage("XHD-000015"));
        }
        
        // [ｵｰﾅｰﾏｽﾀ]情報取得
        Map ownerMasData = loadOwnerMas(queryRunnerWip, ownercode);
        if (ownerMasData == null || ownerMasData.isEmpty()) {
            errorMessageList.add(MessageUtil.getMessage("XHD-000016"));
        }
        
        // 入力項目の情報を画面にセットする。
        if (!setInputItemData(processData, queryRunnerDoc, queryRunnerQcdb, lotNo, formId)) {
            // エラー発生時は処理を中断
            processData.setFatalError(true);
            processData.setInitMessageList(Arrays.asList(MessageUtil.getMessage("XHD-000038")));
            return processData;
        }
        
        // 画面に取得した情報をセットする(入力項目以外)
        setViewItemData(processData, sekkeiData, lotKbnMasData, ownerMasData, daPatternMasData, shikakariData, lotNo);

        processData.setInitMessageList(errorMessageList);
        return processData;
    }
    
    /**
     * 入力項目以外のデータを画面項目に設定
     * 
     * @param processData 処理制御データ
     * @param sekkeiData 設計データ
     * @param lotKbnMasData ﾛｯﾄ区分ﾏｽﾀデータ
     * @param ownerMasData ｵｰﾅｰﾏｽﾀデータ
     * @param daPatternMasData 製版ﾏｽﾀデータ
     * @param shikakariData 仕掛データ
     * @param lotNo ﾛｯﾄNo
     */
    private void setViewItemData(ProcessData processData, Map sekkeiData, Map lotKbnMasData, Map ownerMasData, Map daPatternMasData,
            Map shikakariData, String lotNo) {
        
        // ロットNo
        this.setItemData(processData, GXHDO101B010Const.LOTNO, lotNo);
        // KCPNO
        this.setItemData(processData, GXHDO101B010Const.KCPNO, StringUtil.nullToBlank(getMapData(shikakariData, "kcpno")));
        // 客先
        this.setItemData(processData, GXHDO101B010Const.KYAKUSAKI, StringUtil.nullToBlank(getMapData(shikakariData, "tokuisaki")));
        // ロット区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode"));
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B010Const.LOT_KUBUN, "");
        } else {
            String lotKubun = StringUtil.nullToBlank(getMapData(lotKbnMasData, "lotkubun"));
            this.setItemData(processData, GXHDO101B010Const.LOT_KUBUN, lotkubuncode + ":" + lotKubun);
        }
        // オーナー
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B010Const.OWNER, "");
        } else {
            String owner = StringUtil.nullToBlank(getMapData(ownerMasData, "ownername"));
            this.setItemData(processData, GXHDO101B010Const.OWNER, ownercode + ":" + owner);
        }
        // セット数
        // 列 × 行
        String lRetsu = StringUtil.nullToBlank(getMapData(daPatternMasData, "LRETU")); //列
        String wRetsu = StringUtil.nullToBlank(getMapData(daPatternMasData, "WRETU")); //行
        this.setItemData(processData, GXHDO101B010Const.GYORETU, lRetsu + "×" + wRetsu);
        // ピッチ
        String lSun = StringUtil.nullToBlank(getMapData(daPatternMasData, "LSUN")); //LSUN
        String wSun = StringUtil.nullToBlank(getMapData(daPatternMasData, "WSUN")); //WSUN
        this.setItemData(processData, GXHDO101B010Const.PITCH, lSun + "×" + wSun);
        // 電極製版名
        this.setItemData(processData, GXHDO101B010Const.PATTERN, StringUtil.nullToBlank(sekkeiData.get("PATTERN")));
        // 取個数
        this.setItemData(processData, GXHDO101B010Const.TORIKOSU, StringUtil.nullToBlank(shikakariData.get("torikosuu")));
    }
    
    /**
     * 入力項目のデータを画面項目に設定
     *
     * @param processData 処理制御データ
     * @param sekkeiData 設計データ
     * @param lotKbnMasData ﾛｯﾄ区分ﾏｽﾀデータ
     * @param ownerMasData ｵｰﾅｰﾏｽﾀデータ
     * @param daPatternMasData 製版ﾏｽﾀデータ
     * @param shikakariData 仕掛データ
     * @param lotNo ﾛｯﾄNo
     * @return 設定結果(失敗時false)
     */
    private boolean setInputItemData(ProcessData processData, QueryRunner queryRunnerDoc, QueryRunner queryRunnerQcdb,
            String lotNo, String formId) throws SQLException {
        
        List<SrHapscut> srHapscutDataList = new ArrayList<>();
        
        String rev = "";
        String jotaiFlg = "";
        String kojyo = lotNo.substring(0, 3);
        String lotNo8 = lotNo.substring(3, 11);
        String edaban = lotNo.substring(11, 14);
        
        for (int i = 0; i < 5; i++) {
            // [品質DB登録実績]情報取得
            Map fxhdd03map = loadFxhdd03RevInfo(queryRunnerDoc, kojyo, lotNo8, edaban, formId);
            rev = StringUtil.nullToBlank(getMapData(fxhdd03map, "rev"));
            jotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd03map, "jotai_flg"));
            
            // revisionが空またはjotaiFlgが"0"(仮登録), "1"(本登録) 以外の場合、新規登録としてデフォルト値を設定
            if (StringUtil.isEmpty(rev) || !(JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg) || JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg))) {
                processData.setInitRev(rev);
                processData.setInitJotaiFlg(jotaiFlg);

                // メイン画面にデータを設定する(デフォルト値)
                processData.getItemList().forEach((fxhdd001) -> {
                    this.setItemData(processData, fxhdd001.getItemId(), fxhdd001.getInputDefault());
                });
                
                // 新規登録時は設定OKとしてReturn
                return true;
            }
            
            // [押切ｶｯﾄ]データ取得
            srHapscutDataList = loadSrHapscut(queryRunnerQcdb, kojyo, lotNo8, edaban, rev, jotaiFlg);
            if (!srHapscutDataList.isEmpty()) {
                break;
            }
        }
        
        // 規定回数内にデータが取得できなかった場合
        if (srHapscutDataList.isEmpty()) {
            return false;
        }
        
        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);
        
        // メイン画面データ設定(入力項目)
        setInputItemDataMainForm(processData, srHapscutDataList.get(0));
        
        return true;
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
                        GXHDO101B010Const.BTN_COPY_EDABAN_TOP,
                        GXHDO101B010Const.BTN_COPY_EDABAN_BOTTOM,
                        GXHDO101B010Const.BTN_UPDATE_TOP,
                        GXHDO101B010Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B010Const.BTN_DELETE_TOP,
                        GXHDO101B010Const.BTN_DELETE_BOTTOM,
                        GXHDO101B010Const.BTN_STARTDATETIME_TOP,
                        GXHDO101B010Const.BTN_STARTDATETIME_BOTTOM,
                        GXHDO101B010Const.BTN_ENDDATETIME_TOP,
                        GXHDO101B010Const.BTN_ENDDATETIME_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B010Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B010Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B010Const.BTN_INSERT_TOP,
                        GXHDO101B010Const.BTN_INSERT_BOTTOM
                ));
                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO101B010Const.BTN_COPY_EDABAN_TOP,
                        GXHDO101B010Const.BTN_COPY_EDABAN_BOTTOM,
                        GXHDO101B010Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B010Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B010Const.BTN_INSERT_TOP,
                        GXHDO101B010Const.BTN_INSERT_BOTTOM,
                        GXHDO101B010Const.BTN_STARTDATETIME_TOP,
                        GXHDO101B010Const.BTN_STARTDATETIME_BOTTOM,
                        GXHDO101B010Const.BTN_ENDDATETIME_TOP,
                        GXHDO101B010Const.BTN_ENDDATETIME_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B010Const.BTN_UPDATE_TOP,
                        GXHDO101B010Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B010Const.BTN_DELETE_TOP,
                        GXHDO101B010Const.BTN_DELETE_BOTTOM
                ));
                break;
        }
        processData.setActiveButtonId(activeIdList);
        processData.setInactiveButtonId(inactiveIdList);

        return processData;
    }
    
    /**
     * ボタンID→メソッド名変換
     * 
     * @param buttonId ボタンID
     * @return メソッド名
     */
    @Override
    public String convertButtonIdToMethod(String buttonId) {
        String method;
        switch (buttonId) {
            // 枝番コピー
            case GXHDO101B010Const.BTN_COPY_EDABAN_TOP:
            case GXHDO101B010Const.BTN_COPY_EDABAN_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 仮登録
            case GXHDO101B010Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO101B010Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempResist";
                break;
            // 登録
            case GXHDO101B010Const.BTN_INSERT_TOP:
            case GXHDO101B010Const.BTN_INSERT_BOTTOM:
                method = "checkDataResist";
                break;
            // 修正
            case GXHDO101B010Const.BTN_UPDATE_TOP:
            case GXHDO101B010Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO101B010Const.BTN_DELETE_TOP:
            case GXHDO101B010Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 開始日時
            case GXHDO101B010Const.BTN_STARTDATETIME_TOP:
            case GXHDO101B010Const.BTN_STARTDATETIME_BOTTOM:
                method = "setKaishiDateTime";
                break;
            // 終了日時
            case GXHDO101B010Const.BTN_ENDDATETIME_TOP:
            case GXHDO101B010Const.BTN_ENDDATETIME_BOTTOM:
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
     * @param srHapscut 押切カットデータ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrHapscut srHapscut) {
        // 号機
        this.setItemData(processData, GXHDO101B010Const.GOKI, getHapscutItemData(GXHDO101B010Const.GOKI, srHapscut));
        // ｶｯﾄ刃種類確認
        this.setItemData(processData, GXHDO101B010Const.CUTBA_SHURUI_CHECK, getHapscutItemData(GXHDO101B010Const.CUTBA_SHURUI_CHECK, srHapscut));
        // ｶｯﾄ刃直進度
        this.setItemData(processData, GXHDO101B010Const.CUTBA_CHOKUSHINDO, getHapscutItemData(GXHDO101B010Const.CUTBA_CHOKUSHINDO, srHapscut));
        // ｶｯﾄ刃使用回数ST1
        this.setItemData(processData, GXHDO101B010Const.CUTBA_SIYOUKAISUU_ST1, getHapscutItemData(GXHDO101B010Const.CUTBA_SIYOUKAISUU_ST1, srHapscut));
        // ｶｯﾄ刃使用回数ST2
        this.setItemData(processData, GXHDO101B010Const.CUTBA_SIYOUKAISUU_ST2, getHapscutItemData(GXHDO101B010Const.CUTBA_SIYOUKAISUU_ST2, srHapscut));
        // ｶｯﾄ刃使用数
        this.setItemData(processData, GXHDO101B010Const.CUTBA_MAISUU, getHapscutItemData(GXHDO101B010Const.CUTBA_MAISUU, srHapscut));
        // ﾌﾟﾛｸﾞﾗﾑ名
        this.setItemData(processData, GXHDO101B010Const.PROGRAM_MEI, getHapscutItemData(GXHDO101B010Const.PROGRAM_MEI, srHapscut));
        // 列×行確認
        this.setItemData(processData, GXHDO101B010Const.GYORETU_KAKUNIN, getHapscutItemData(GXHDO101B010Const.GYORETU_KAKUNIN, srHapscut));
        // ﾏｰｸ外取り数
        this.setItemData(processData, GXHDO101B010Const.MARKTORISU, getHapscutItemData(GXHDO101B010Const.MARKTORISU, srHapscut));
        // ｶｯﾄ補正量
        this.setItemData(processData, GXHDO101B010Const.CUT_HOSEI_RYOU, getHapscutItemData(GXHDO101B010Const.CUT_HOSEI_RYOU, srHapscut));
        // ﾃｰﾌﾞﾙ温度 設定
        this.setItemData(processData, GXHDO101B010Const.TABLEONDO_SET, getHapscutItemData(GXHDO101B010Const.TABLEONDO_SET, srHapscut));
        // ﾃｰﾌﾞﾙ温度 実測
        this.setItemData(processData, GXHDO101B010Const.TABLEONDO_SOKU, getHapscutItemData(GXHDO101B010Const.TABLEONDO_SOKU, srHapscut));
        // 外観確認
        this.setItemData(processData, GXHDO101B010Const.GAIKAN_CHECK, getHapscutItemData(GXHDO101B010Const.GAIKAN_CHECK, srHapscut));
        // 刃高さNG
        this.setItemData(processData, GXHDO101B010Const.HATAKASA_NG, getHapscutItemData(GXHDO101B010Const.HATAKASA_NG, srHapscut));
        // 開始日
        this.setItemData(processData, GXHDO101B010Const.KAISHI_DAY, getHapscutItemData(GXHDO101B010Const.KAISHI_DAY, srHapscut));
        // 開始時間
        this.setItemData(processData, GXHDO101B010Const.KAISHI_TIME, getHapscutItemData(GXHDO101B010Const.KAISHI_TIME, srHapscut));
        // 開始担当者
        this.setItemData(processData, GXHDO101B010Const.KAISHI_TANTOUSYA, getHapscutItemData(GXHDO101B010Const.KAISHI_TANTOUSYA, srHapscut));
        // 開始確認者
        this.setItemData(processData, GXHDO101B010Const.KAISHI_KAKUNINSYA, getHapscutItemData(GXHDO101B010Const.KAISHI_KAKUNINSYA, srHapscut));
        // 終了日
        this.setItemData(processData, GXHDO101B010Const.SHURYOU_DAY, getHapscutItemData(GXHDO101B010Const.SHURYOU_DAY, srHapscut));
        // 終了時間
        this.setItemData(processData, GXHDO101B010Const.SHURYOU_TIME, getHapscutItemData(GXHDO101B010Const.SHURYOU_TIME, srHapscut));
        // 終了担当者
        this.setItemData(processData, GXHDO101B010Const.SHURYOU_TANTOUSYA, getHapscutItemData(GXHDO101B010Const.SHURYOU_TANTOUSYA, srHapscut));
        // 処理ｾｯﾄ数
        this.setItemData(processData, GXHDO101B010Const.SHORI_SETSU, getHapscutItemData(GXHDO101B010Const.SHORI_SETSU, srHapscut));
        // 良品ｾｯﾄ数
        this.setItemData(processData, GXHDO101B010Const.RYOHIN_SETSU, getHapscutItemData(GXHDO101B010Const.RYOHIN_SETSU, srHapscut));
        // 作業場所
        this.setItemData(processData, GXHDO101B010Const.SAGYOU_BASYO, getHapscutItemData(GXHDO101B010Const.SAGYOU_BASYO, srHapscut));
        // 備考1
        this.setItemData(processData, GXHDO101B010Const.BIKOU1, getHapscutItemData(GXHDO101B010Const.BIKOU1, srHapscut));
        // 備考2
        this.setItemData(processData, GXHDO101B010Const.BIKOU2, getHapscutItemData(GXHDO101B010Const.BIKOU2, srHapscut));
    }
    
    /**
     * 項目IDに該当するDBの値を取得する。
     * 
     * @param itemId 項目ID
     * @param data 押切ｶｯﾄﾃﾞｰﾀ
     * @return  DB値
     */
    private String getHapscutItemData(String itemId, SrHapscut data) {
        switch (itemId) {
            // 号機
            case GXHDO101B010Const.GOKI:
                return StringUtil.nullToBlank(data.getGoki());
            // ｶｯﾄ刃種類確認
            case GXHDO101B010Const.CUTBA_SHURUI_CHECK:
                return getNgOkBlank(data.getCutbashuruicheck());
            // ｶｯﾄ刃直進度
            case GXHDO101B010Const.CUTBA_CHOKUSHINDO:
                return StringUtil.nullToBlank(data.getCutbachokushindo());
            // ｶｯﾄ刃使用回数ST1
            case GXHDO101B010Const.CUTBA_SIYOUKAISUU_ST1:
                return StringUtil.nullToBlank(data.getCutbasiyoukaisuust1());
            // ｶｯﾄ刃使用回数ST2
            case GXHDO101B010Const.CUTBA_SIYOUKAISUU_ST2:
                return StringUtil.nullToBlank(data.getCutbasiyoukaisuust2());
            // ｶｯﾄ刃使用数
            case GXHDO101B010Const.CUTBA_MAISUU:
                return StringUtil.nullToBlank(data.getCutbamaisuu());
            // ﾌﾟﾛｸﾞﾗﾑ名
            case GXHDO101B010Const.PROGRAM_MEI:
                return StringUtil.nullToBlank(data.getProgrammei());
            // 列×行確認
            case GXHDO101B010Const.GYORETU_KAKUNIN:
                return getNgOkBlank(data.getGyoretukakunin());
            // ﾏｰｸ外取り数
            case GXHDO101B010Const.MARKTORISU:
                return getNgOkBlank(data.getMarktorisuu());
            // ｶｯﾄ補正量
            case GXHDO101B010Const.CUT_HOSEI_RYOU:
                return StringUtil.nullToBlank(data.getCuthoseiryou());
            // ﾃｰﾌﾞﾙ温度 設定
            case GXHDO101B010Const.TABLEONDO_SET:
                return StringUtil.nullToBlank(data.getTableondoset());
            // ﾃｰﾌﾞﾙ温度 実測
            case GXHDO101B010Const.TABLEONDO_SOKU:
                return StringUtil.nullToBlank(data.getTableondosoku());
            // 外観確認
            case GXHDO101B010Const.GAIKAN_CHECK:
                return getNgOkBlank(data.getGaikancheck());
            // 刃高さNG
            case GXHDO101B010Const.HATAKASA_NG:
                return StringUtil.nullToBlank(data.getHatakasang());
            // 開始日
            case GXHDO101B010Const.KAISHI_DAY:
                return DateUtil.formattedTimestamp(data.getKaisinichiji(), "yyMMdd");
            // 開始時間
            case GXHDO101B010Const.KAISHI_TIME:
                return DateUtil.formattedTimestamp(data.getKaisinichiji(), "HHmm");
            // 開始担当者
            case GXHDO101B010Const.KAISHI_TANTOUSYA:
                return StringUtil.nullToBlank(data.getCuttantosya());
            // 開始確認者
            case GXHDO101B010Const.KAISHI_KAKUNINSYA:
                return StringUtil.nullToBlank(data.getKakuninsya());
            // 終了日
            case GXHDO101B010Const.SHURYOU_DAY:
                return DateUtil.formattedTimestamp(data.getSyuryonichiji(), "yyMMdd");
            // 終了時間
            case GXHDO101B010Const.SHURYOU_TIME:
                return DateUtil.formattedTimestamp(data.getSyuryonichiji(), "HHmm");
            // 終了担当者
            case GXHDO101B010Const.SHURYOU_TANTOUSYA:
                return StringUtil.nullToBlank(data.getChktantosya());
            // 処理ｾｯﾄ数
            case GXHDO101B010Const.SHORI_SETSU:
                return StringUtil.nullToBlank(data.getSyorisetsuu());
            // 良品ｾｯﾄ数
            case GXHDO101B010Const.RYOHIN_SETSU:
                return StringUtil.nullToBlank(data.getRyouhinsetsuu());
            // 作業場所
            case GXHDO101B010Const.SAGYOU_BASYO:
                return StringUtil.nullToBlank(data.getSagyoubasyo());
            // 備考1
            case GXHDO101B010Const.BIKOU1:
                return StringUtil.nullToBlank(data.getBiko1());
            // 備考2
            case GXHDO101B010Const.BIKOU2:
                return StringUtil.nullToBlank(data.getBiko2());
            default:
                return null;
        }
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

            //仕掛情報の取得
            Map shikakariData = loadShikakariData(queryRunnerWip, lotNo);
            String oyalotEdaban = StringUtil.nullToBlank(getMapData(shikakariData, "oyalotedaban")); //親ﾛｯﾄ枝番

            // 品質DB登録実績データ取得
            Map fxhdd03RevInfo = loadFxhdd03RevInfo(queryRunnerDoc, kojyo, lotNo8, oyalotEdaban, formId);
            if (fxhdd03RevInfo == null || fxhdd03RevInfo.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            String jotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd03RevInfo, "jotai_flg"));
            if (!(JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg) || JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg))) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }
            
            // 押切ｶｯﾄデータ取得
            List<SrHapscut> srHapscutDataList = loadSrHapscut(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban);
            if (srHapscutDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // メイン画面データ設定
            setInputItemDataMainForm(processData, srHapscutDataList.get(0));
            
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
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData checkDataTempResist(ProcessData processData) {
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
        
        // ｶｯﾄ刃種類確認
        FXHDD01 itemCutbaCheck = getItemRow(processData.getItemList(), GXHDO101B010Const.CUTBA_SHURUI_CHECK);
        if ("NG".equals(itemCutbaCheck.getValue())) {
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, Arrays.asList(itemCutbaCheck), itemCutbaCheck.getLabel1());
        }
        
        // 列×行確認
        FXHDD01 gyoretsuCheck = getItemRow(processData.getItemList(), GXHDO101B010Const.GYORETU_KAKUNIN);
        if ("NG".equals(gyoretsuCheck.getValue())) {
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, Arrays.asList(gyoretsuCheck), gyoretsuCheck.getLabel1());
        }
        
        // ﾏｰｸ取り数
        FXHDD01 markTorisu = getItemRow(processData.getItemList(), GXHDO101B010Const.MARKTORISU);
        if ("NG".equals(markTorisu.getValue())) {
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, Arrays.asList(markTorisu), markTorisu.getLabel1());
        }

        return null;
    }
    
    /**
     * 登録処理(データチェック処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData checkDataResist(ProcessData processData) {
        // 項目のチェック処理を行う。
        ErrorMessageInfo checkItemErrorInfo = checkItemResistCorrect(processData);
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
        ErrorMessageInfo checkItemErrorInfo = checkItemResistCorrect(processData);
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
        processData.setUserAuthParam(GXHDO101B010Const.USER_AUTH_UPDATE_PARAM);

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
    private ErrorMessageInfo checkItemResistCorrect(ProcessData processData) {
        // ｶｯﾄ刃種類確認
        FXHDD01 itemCutbaCheck = getItemRow(processData.getItemList(), GXHDO101B010Const.CUTBA_SHURUI_CHECK);
        if ("NG".equals(itemCutbaCheck.getValue())) {
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, Arrays.asList(itemCutbaCheck), itemCutbaCheck.getLabel1());
        }
        
        // 列×行確認
        FXHDD01 gyoretsuCheck = getItemRow(processData.getItemList(), GXHDO101B010Const.GYORETU_KAKUNIN);
        if ("NG".equals(gyoretsuCheck.getValue())) {
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, Arrays.asList(gyoretsuCheck), gyoretsuCheck.getLabel1());
        }
        
        // ﾏｰｸ取り数
        FXHDD01 markTorisu = getItemRow(processData.getItemList(), GXHDO101B010Const.MARKTORISU);
        if ("NG".equals(markTorisu.getValue())) {
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, Arrays.asList(markTorisu), markTorisu.getLabel1());
        }
        
        // 開始日時、終了日時前後チェック
        FXHDD01 itemKaishiDay = getItemRow(processData.getItemList(), GXHDO101B010Const.KAISHI_DAY);
        FXHDD01 itemKaishiTime = getItemRow(processData.getItemList(), GXHDO101B010Const.KAISHI_TIME);
        Date kaishiDate = DateUtil.convertStringToDate(itemKaishiDay.getValue(), itemKaishiTime.getValue());
        FXHDD01 itemShuryouDay = getItemRow(processData.getItemList(), GXHDO101B010Const.SHURYOU_DAY);
        FXHDD01 itemShuryouTime = getItemRow(processData.getItemList(), GXHDO101B010Const.SHURYOU_TIME);
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
        processData.setUserAuthParam(GXHDO101B010Const.USER_AUTH_DELETE_PARAM);

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
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B010Const.KAISHI_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B010Const.KAISHI_TIME);
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
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B010Const.SHURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B010Const.SHURYOU_TIME);
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
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhdd03RevInfo(QueryRunner queryRunnerDoc, String kojyo, String lotNo,
            String edaban, String formId) throws SQLException {
        // 登録実績データの取得
        String sql = "SELECT rev, jotai_flg "
                + "FROM fxhdd03 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND gamen_id = ?";

        List<Object> params = new ArrayList<>(Arrays.asList(kojyo, lotNo, edaban, formId));

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerDoc.query(sql, new MapHandler(), params.toArray());
    }
    
    /**
     * [品質DB登録実績]から、ﾃﾞｰﾀを取得(データ更新用)
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param formId 画面ID
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhdd03RevInfoWithLock(QueryRunner queryRunnerDoc, Connection conDoc, String kojyo, String lotNo,
            String edaban, String formId) throws SQLException {
        // 登録実績データの取得
        String sql = "SELECT rev, jotai_flg "
                + "FROM fxhdd03 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND gamen_id = ? "
                + "FOR UPDATE NOWAIT ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(formId);

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
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private BigDecimal getNewRev(QueryRunner queryRunnerDoc, Connection conDoc, String kojyo, String lotNo,
            String edaban, String formId) throws SQLException {
        BigDecimal newRev = BigDecimal.ONE;
        // 設計データの取得
        String sql = "SELECT MAX(rev) AS rev "
                + "FROM fxhdd03 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND gamen_id = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
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
     * [押切ｶｯﾄ]または[押切ｶｯﾄ_仮登録]からﾃﾞｰﾀを取得
     * 
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @param jotaiFlg 登録状態
     * @return 取得データ
     * @throws SQLException 例外
     */
    private List<SrHapscut> loadSrHapscut(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, 
            String edaban, String rev, String jotaiFlg) throws SQLException {
        
        boolean isTemporary = !JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg);
        
        String sql_deleteFlag = isTemporary ? "deleteflag " : "0 AS deleteflag ";
        String sql_tableName = isTemporary ? "tmp_sr_hapscut " : "sr_hapscut ";
        
        // SQL生成
        String sql = "SELECT KOJYO,LOTNO,EDABAN,KCPNO,KAISINICHIJI,SYURYONICHIJI,CUTBAMAISUU,GOKI,CUTTABLEONDO," 
                + "CUTTANTOSYA,KAKUNINSYA,CHKTANTOSYA,BTANTOSYA,ATANTOSYA,UKEIREKOSUU,RYOHINKOSUU," 
                + "Atumi01,Atumi02,Atumi03,Atumi04,Atumi05,Atumi06,Atumi07,Atumi08,Atumi09,Atumi10,ATUMIMIN,ATUMIMAX," 
                + "BIKO1,BIKO2,BIKO3,BIKO4,TOROKUNICHIJI,KOSINNICHIJI,TENSYASYA,NIJIMICNT,Soujyuryo,Tanijyuryo," 
                + "cutbashuruicheck,cutbachokushindo,cutbasiyoukaisuuST1,cutbasiyoukaisuuST2,programmei,gyoretukakunin," 
                + "marktorisuu,cuthoseiryou,tableondoset,tableondosoku,gaikancheck,hatakasang,syorisetsuu,ryouhinsetsuu," 
                + "sagyoubasyo,revision," + sql_deleteFlag
                + "FROM " + sql_tableName
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? ";
        if (!StringUtil.isEmpty(rev)) {
            sql += "AND revision = ? ";
        }
        if (isTemporary)
        {
            sql += "AND deleteflag = 0 ";
        }
        
        // パラメータ
        List<Object> params = new ArrayList<>(Arrays.asList(kojyo, lotNo, edaban));
        if (!StringUtil.isEmpty(rev)) {
            params.add(rev);
        }
        
        // SELECT結果とモデルクラスのマッピング
        Map<String, String> mapping = new HashMap<String, String>(){
            {
                put("KOJYO", "kojyo");
                put("LOTNO", "lotno");
                put("EDABAN", "edaban");
                put("KCPNO", "kcpno");
                put("KAISINICHIJI", "kaisinichiji");
                put("SYURYONICHIJI", "syuryonichiji");
                put("CUTBAMAISUU", "cutbamaisuu");
                put("GOKI", "goki");
                put("CUTTABLEONDO", "cuttableondo");
                put("CUTTANTOSYA", "cuttantosya");
                put("KAKUNINSYA", "kakuninsya");
                put("CHKTANTOSYA", "chktantosya");
                put("BTANTOSYA", "btantosya");
                put("ATANTOSYA", "atantosya");
                put("UKEIREKOSUU", "ukeirekosuu");
                put("RYOHINKOSUU", "ryohinkosuu");
                put("Atumi01", "atumi01");
                put("Atumi02", "atumi02");
                put("Atumi03", "atumi03");
                put("Atumi04", "atumi04");
                put("Atumi05", "atumi05");
                put("Atumi06", "atumi06");
                put("Atumi07", "atumi07");
                put("Atumi08", "atumi08");
                put("Atumi09", "atumi09");
                put("Atumi10", "atumi10");
                put("ATUMIMIN", "atumimin");
                put("ATUMIMAX", "atumimax");
                put("BIKO1", "biko1");
                put("BIKO2", "biko2");
                put("BIKO3", "biko3");
                put("BIKO4", "biko4");
                put("TOROKUNICHIJI", "torokunichiji");
                put("KOSINNICHIJI", "kosinnichiji");
                put("TENSYASYA", "tensyasya");
                put("NIJIMICNT", "nijimicnt");
                put("Soujyuryo", "soujyuryo");
                put("Tanijyuryo", "tanijyuryo");
                put("cutbashuruicheck", "cutbashuruicheck");
                put("cutbachokushindo", "cutbachokushindo");
                put("cutbasiyoukaisuuST1", "cutbasiyoukaisuust1");
                put("cutbasiyoukaisuuST2", "cutbasiyoukaisuust2");
                put("programmei", "programmei");
                put("gyoretukakunin", "gyoretukakunin");
                put("marktorisuu", "marktorisuu");
                put("cuthoseiryou", "cuthoseiryou");
                put("tableondoset", "tableondoset");
                put("tableondosoku", "tableondosoku");
                put("gaikancheck", "gaikancheck");
                put("hatakasang", "hatakasang");
                put("syorisetsuu", "syorisetsuu");
                put("ryouhinsetsuu", "ryouhinsetsuu");
                put("sagyoubasyo", "sagyoubasyo");
                put("revision", "revision");
                put("deleteflag", "deleteflag");
            }  
        };
        
        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrHapscut>> beanHandler = new BeanListHandler<>(SrHapscut.class, rowProcessor);
        
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }
    
    /**
     * [押切ｶｯﾄ_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_hapscut "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? ";
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
        
            // 品質DB登録実績データ取得・更新
            //ここでロックを掛ける
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId);
            ErrorMessageInfo checkRevMessageInfo = checkRevision(processData, fxhdd03RevInfo);
            // リビジョンエラー時はリターン
            if (checkRevMessageInfo != null) {
                processData.setErrorMessageInfoList(Arrays.asList(checkRevMessageInfo));
                //ロールバック処理
                DBUtil.rollbackConnection(conDoc, LOGGER);
                DBUtil.rollbackConnection(conQcdb, LOGGER);

                return processData;
            }
        
            BigDecimal rev = BigDecimal.ZERO;
            BigDecimal newRev = BigDecimal.ONE;
            int jissekiNo = 1;
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            
            if (StringUtil.isEmpty(processData.getInitRev())) {
                // 品質DB登録実績登録処理
                insertFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekiNo, JOTAI_FLG_TOROKUZUMI, systemTime);
            } else {
                rev = new BigDecimal(processData.getInitRev());
                // リビジョンを採番
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId);
                // 品質DB登録実績更新処理
                updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_TOROKUZUMI, systemTime);
            }
            
            // 仮登録状態の場合、仮登録のデータを削除
            SrHapscut tmpSrHapscut = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {
                // 更新前の値を取得
                List<SrHapscut> srHapscutList = 
                        loadSrHapscut(queryRunnerQcdb, kojyo, lotNo8, edaban, rev.toPlainString(), processData.getInitJotaiFlg());
                if (!srHapscutList.isEmpty()) {
                    tmpSrHapscut = srHapscutList.get(0);
                }
                
                deleteTmpHapscut(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);
            }
            
            // [押切ｶｯﾄ] 登録処理
            insertHapscut(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList(), tmpSrHapscut);
            
            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert(queryRunnerDoc,  conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, jissekiNo, "0", kikakuError.getKikakuchiInputErrorInfoList());
            }
            
            // 処理後はエラーリストをクリア
            kikakuError.setKikakuchiInputErrorInfoList(new ArrayList<>());
        
            // COMMIT
            DbUtils.commitAndCloseQuietly(conDoc);
            DbUtils.commitAndCloseQuietly(conQcdb);
        
            // 後続処理メソッド設定
            processData.setMethod("");

            // 完了メッセージとコールバックパラメータを設定
            processData.setCompMessage("登録しました。");
            processData.setCollBackParam("complete");

            return processData;
            
        } catch (SQLException e) {
            ErrUtil.outputErrorLog("SQLException発生", e, LOGGER);

            //ロールバック処理
            DBUtil.rollbackConnection(conDoc, LOGGER);
            DBUtil.rollbackConnection(conQcdb, LOGGER);

            if (SQL_STATE_RECORD_LOCK_ERR.equals(e.getSQLState())) {
                // レコードロックエラー時
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"))));
            } else {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            }
            
            return processData;
        }
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
        
            // 品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId);
            ErrorMessageInfo checkRevMessageInfo = checkRevision(processData, fxhdd03RevInfo);
            // リビジョンエラー時はリターン
            if (checkRevMessageInfo != null) {
                processData.setErrorMessageInfoList(Arrays.asList(checkRevMessageInfo));
                //ロールバック処理
                DBUtil.rollbackConnection(conDoc, LOGGER);
                DBUtil.rollbackConnection(conQcdb, LOGGER);

                return processData;
            }
            
            BigDecimal rev = new BigDecimal(processData.getInitRev());
            // 新しいリビジョンを採番
            BigDecimal newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId);

            int jissekiNo = 1;
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            
            // 品質DB登録実績更新処理
            updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_TOROKUZUMI, systemTime);
        
            // [押切ｶｯﾄ] 更新処理
            updateHapscut(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList());
        
            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, jissekiNo, "0", kikakuError.getKikakuchiInputErrorInfoList());
            }
            
            // 処理後はエラーリストをクリア
            kikakuError.setKikakuchiInputErrorInfoList(new ArrayList<>());

            // COMMIT
            DbUtils.commitAndCloseQuietly(conDoc);
            DbUtils.commitAndCloseQuietly(conQcdb);

            // 後続処理メソッド設定
            processData.setMethod("");

            // 完了メッセージとコールバックパラメータを設定
            processData.setCompMessage("修正しました。");
            processData.setCollBackParam("complete");

            return processData;
        } catch (SQLException e) {
            ErrUtil.outputErrorLog("SQLException発生", e, LOGGER);

            //ロールバック処理
            DBUtil.rollbackConnection(conDoc, LOGGER);
            DBUtil.rollbackConnection(conQcdb, LOGGER);

            if (SQL_STATE_RECORD_LOCK_ERR.equals(e.getSQLState())) {
                // レコードロックエラー時
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"))));
            } else {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            }
            
            return processData;
        }
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

            // 品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId);
            ErrorMessageInfo checkRevMessageInfo = checkRevision(processData, fxhdd03RevInfo);
            // リビジョンエラー時はリターン
            if (checkRevMessageInfo != null) {
                processData.setErrorMessageInfoList(Arrays.asList(checkRevMessageInfo));
                //ロールバック処理
                DBUtil.rollbackConnection(conDoc, LOGGER);
                DBUtil.rollbackConnection(conQcdb, LOGGER);

                return processData;
            }

            BigDecimal rev = new BigDecimal(processData.getInitRev());
            BigDecimal newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId);
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            // 品質DB登録実績更新処理
            updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_SAKUJO, systemTime);

            // 押切ｶｯﾄ_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo8, edaban);
            insertDeleteDataTmpSrHapscut(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, systemTime);

            // 押切ｶｯﾄ_削除処理
            deleteSrHapscut(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);
            
            // COMMIT
            DbUtils.commitAndCloseQuietly(conDoc);
            DbUtils.commitAndCloseQuietly(conQcdb);

            // 後続処理メソッド設定
            processData.setMethod("");

            // 完了メッセージとコールバックパラメータを設定
            processData.setCompMessage("削除しました。");
            processData.setCollBackParam("complete");

            return processData;
        } catch (SQLException e) {
            ErrUtil.outputErrorLog("SQLException発生", e, LOGGER);

            //ロールバック処理
            DBUtil.rollbackConnection(conDoc, LOGGER);
            DBUtil.rollbackConnection(conQcdb, LOGGER);

            if (SQL_STATE_RECORD_LOCK_ERR.equals(e.getSQLState())) {
                // レコードロックエラー時
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"))));
            } else {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            }
            
            return processData;
        }
    }
    
    /**
     * 仮登録処理(実処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doTempResist(ProcessData processData) {

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

            // 品質DB登録実績データ取得
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId);
            ErrorMessageInfo checkRevMessageInfo = checkRevision(processData, fxhdd03RevInfo);
            // リビジョンエラー時はリターン
            if (checkRevMessageInfo != null) {
                processData.setErrorMessageInfoList(Arrays.asList(checkRevMessageInfo));

                //ロールバック処理
                DBUtil.rollbackConnection(conDoc, LOGGER);
                DBUtil.rollbackConnection(conQcdb, LOGGER);
                return processData;
            }

            BigDecimal newRev = BigDecimal.ONE;
            int jissekiNo = 1;
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());

            BigDecimal rev = BigDecimal.ZERO;
            if (StringUtil.isEmpty(processData.getInitJotaiFlg())) {
                // 品質DB登録実績登録処理
                insertFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekiNo, JOTAI_FLG_KARI_TOROKU, systemTime);
            } else {
                rev = new BigDecimal(processData.getInitRev());
                // 新しいリビジョンを採番
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId);

                // 品質DB登録実績更新処理
                updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_KARI_TOROKU, systemTime);
            }

            if (StringUtil.isEmpty(processData.getInitJotaiFlg()) || JOTAI_FLG_SAKUJO.equals(processData.getInitJotaiFlg())) {
                // 押切ｶｯﾄ_仮登録登録処理
                insertTmpSrHapscut(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

            } else {
                // 押切ｶｯﾄ_仮登録更新処理
                updateTmpSrHapscut(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList());
            }

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, jissekiNo, "0", kikakuError.getKikakuchiInputErrorInfoList());
            }
            // 処理後はエラーリストをクリア
            kikakuError.setKikakuchiInputErrorInfoList(new ArrayList<>());
            
            // COMMIT
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
        } catch (SQLException e) {
            ErrUtil.outputErrorLog("SQLException発生", e, LOGGER);
            //ロールバック処理
            DBUtil.rollbackConnection(conDoc, LOGGER);
            DBUtil.rollbackConnection(conQcdb, LOGGER);
            if (SQL_STATE_RECORD_LOCK_ERR.equals(e.getSQLState())) {
                // レコードロックエラー時
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"))));
            } else {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            }
            
            return processData;
        }
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
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param systemTime システム日付
     * @throws SQLException 例外エラー
     */
    private void updateFxhdd03(QueryRunner queryRunnerDoc, Connection conDoc, String tantoshaCd, String formId, BigDecimal rev,
            String kojyo, String lotNo, String edaban, String jotaiFlg, Timestamp systemTime) throws SQLException {

        String sql = "UPDATE fxhdd03 SET "
                + "koshinsha = ?, koshin_date = ?,"
                + "rev = ?, jotai_flg = ? "
                + "WHERE gamen_id = ? AND kojyo = ? "
                + "  AND lotno = ? AND edaban = ? "
                + "  AND jissekino = 1  ";

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

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerDoc.update(conDoc, sql, params.toArray());
    }
    
    /**
     * 押切ｶｯﾄ_仮登録(tmp_sr_hapscut)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @throws SQLException 例外エラー
     */
    private void insertTmpSrHapscut(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {
        String sql = "INSERT INTO tmp_sr_hapscut ("
                + "KOJYO,LOTNO,EDABAN,KCPNO,KAISINICHIJI,SYURYONICHIJI,CUTBAMAISUU,GOKI,CUTTABLEONDO,CUTTANTOSYA,KAKUNINSYA,CHKTANTOSYA,BTANTOSYA,"
                + "ATANTOSYA,UKEIREKOSUU,RYOHINKOSUU,Atumi01,Atumi02,Atumi03,Atumi04,Atumi05,Atumi06,Atumi07,Atumi08,Atumi09,Atumi10,ATUMIMIN,ATUMIMAX," 
                + "BIKO1,BIKO2,BIKO3,BIKO4,TOROKUNICHIJI,KOSINNICHIJI,TENSYASYA,NIJIMICNT,Soujyuryo,Tanijyuryo,cutbashuruicheck,cutbachokushindo," 
                + "cutbasiyoukaisuuST1,cutbasiyoukaisuuST2,programmei,gyoretukakunin,marktorisuu,cuthoseiryou,tableondoset,tableondosoku," 
                + "gaikancheck,hatakasang,syorisetsuu,ryouhinsetsuu,sagyoubasyo,revision,deleteflag"
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        List<Object> params = setUpdateParametersTmpHapscut(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, itemList, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 押切ｶｯﾄ_仮登録(tmp_sr_hapscut)更新処理
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
     * @throws SQLException 例外エラー
     */
    private void updateTmpSrHapscut(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {
        String sql = "UPDATE tmp_sr_hapscut SET "
                + "KCPNO = ?,TAPELOTNO = ?,GENRYOKIGO = ?,KAISINICHIJI = ?,SYURYONICHIJI = ?,"
                + "GOKI = ?,SKEEGENO = ?,KANSOONDO = ?,SEIHANNO = ?,SEIHANMAISUU = ?,PASTELOTNO = ?,PASTENENDO = ?,PASTEONDO = ?,"
                + "INSATUROLLNO = ?,INSATUROLLNO2 = ?,INSATUROLLNO3 = ?,MLD = ?,BIKO1 = ?,BIKO2 = ?,TANTOSYA = ?,pkokeibun1 = ?,"
                + "pastelotno2 = ?,pastenendo2 = ?,pasteondo2 = ?,pkokeibun2 = ?,petfilmsyurui = ?,kansoondo2 = ?,kansoondo3 = ?,"
                + "kansoondo4 = ?,kansoondo5 = ?,seihanmei = ?,makuatsu_ave_start = ?,makuatsu_max_start = ?,makuatsu_min_start = ?,"
                + "makuatucv_start = ?,nijimikasure_start = ?,nijimikasure_end = ?,tanto_end = ?,printmaisuu = ?,kansouroatsu = ?,"
                + "printhaba = ?,table_clearrance = ?,kosinnichiji = ?,revision = ?,deleteflag = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrHapscut> srSrHapscutList = loadSrHapscut(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrHapscut srHapscut = null;
        if (!srSrHapscutList.isEmpty()) {
            srHapscut = srSrHapscutList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParametersTmpHapscut(false, newRev, 0, "", "", "", systemTime, itemList, srHapscut);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }
    
    /**
     * 押切ｶｯﾄ_仮登録(tmp_sr_hapscut)更新パラメータ設定
     * 
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param data 押切ｶｯﾄﾃﾞｰﾀ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParametersTmpHapscut(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo, String lotNo,
            String edaban, Timestamp systemTime, List<FXHDD01> itemList, SrHapscut data) {
        List<Object> params = new ArrayList<>();
        
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B010Const.KCPNO, data))); //KCPNo
        params.add(DBUtil.stringToDateObject(
                getItemData(itemList, GXHDO101B010Const.KAISHI_DAY, data),
                getItemData(itemList, GXHDO101B010Const.KAISHI_TIME, data))); //開始日時
        params.add(DBUtil.stringToDateObject(
                getItemData(itemList, GXHDO101B010Const.SHURYOU_DAY, data),
                getItemData(itemList, GXHDO101B010Const.SHURYOU_TIME, data))); //終了日時
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B010Const.CUTBA_MAISUU, data))); //ｶｯﾄ刃枚数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B010Const.GOKI, data))); //号機
        if (isInsert) {
            params.add(null); // ｶｯﾄﾃｰﾌﾞﾙ温度
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B010Const.KAISHI_TANTOUSYA, data))); //ｶｯﾄ担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B010Const.KAISHI_KAKUNINSYA, data))); //開始確認者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B010Const.SHURYOU_TANTOUSYA, data))); //終了担当者
        if (isInsert) {
            params.add(null); // B担当者
            params.add(null); // A担当者
            params.add(null); // 受入個数
            params.add(null); // 良品個数
            params.add(null); // 厚み01
            params.add(null); // 厚み02
            params.add(null); // 厚み03
            params.add(null); // 厚み04
            params.add(null); // 厚み05
            params.add(null); // 厚み06
            params.add(null); // 厚み07
            params.add(null); // 厚み08
            params.add(null); // 厚み09
            params.add(null); // 厚み10
            params.add(null); // 厚みMIN
            params.add(null); // 厚みMAX
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B010Const.BIKOU1, data))); //備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B010Const.BIKOU2, data))); //備考2
        if (isInsert) {
            params.add(null); // 備考3
            params.add(null); // 備考4
            params.add(systemTime); // 登録日時
        }
        params.add(systemTime); // 更新日時
        if (isInsert) {
            params.add(null); // 転写者
            params.add(null); // ﾆｼﾞﾐ回数
            params.add(null); // 総重量
            params.add(null); // 単位重量
        }
        params.add(getNgOkBlankToIntForTemp(
                StringUtil.nullToBlank(getItemData(itemList, GXHDO101B010Const.CUTBA_SHURUI_CHECK, data)))); // ｶｯﾄ刃種類確認
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B010Const.CUTBA_CHOKUSHINDO, data))); // ｶｯﾄ刃直進度
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B010Const.CUTBA_SIYOUKAISUU_ST1, data))); // ｶｯﾄ刃使用回数ST1
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B010Const.CUTBA_SIYOUKAISUU_ST2, data))); // ｶｯﾄ刃使用回数ST2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B010Const.PROGRAM_MEI, data))); // ﾌﾟﾛｸﾞﾗﾑ名
        params.add(getNgOkBlankToIntForTemp(
                StringUtil.nullToBlank(getItemData(itemList, GXHDO101B010Const.GYORETU_KAKUNIN, data)))); // 行×列確認
        params.add(getNgOkBlankToIntForTemp(
                StringUtil.nullToBlank(getItemData(itemList, GXHDO101B010Const.MARKTORISU, data)))); // ﾏｰｸ外取り数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B010Const.CUT_HOSEI_RYOU, data))); // ｶｯﾄ補正量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B010Const.TABLEONDO_SET, data))); // ﾃｰﾌﾞﾙ温度 設定
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B010Const.TABLEONDO_SOKU, data))); // ﾃｰﾌﾞﾙ温度 実測
        params.add(getNgOkBlankToIntForTemp(
                StringUtil.nullToBlank(getItemData(itemList, GXHDO101B010Const.GAIKAN_CHECK, data)))); // 外観確認
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B010Const.HATAKASA_NG, data))); // 刃高さNG
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B010Const.SHORI_SETSU, data))); // 処理ｾｯﾄ数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B010Const.RYOHIN_SETSU, data))); // 良品ｾｯﾄ数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B010Const.SAGYOU_BASYO, data))); // 作業場所
        params.add(newRev); // revision
        params.add(deleteflag); //削除ﾌﾗｸﾞ
        
        return params;
    }
    
    /**
     * 押切ｶｯﾄ_仮登録(tmp_sr_hapscut)登録処理 ※削除時の履歴登録
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
    private void insertDeleteDataTmpSrHapscut(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        String sql = "INSERT INTO tmp_sr_hapscut ("
                + "KOJYO,LOTNO,EDABAN,KCPNO,KAISINICHIJI,SYURYONICHIJI,CUTBAMAISUU,GOKI,CUTTABLEONDO,CUTTANTOSYA,KAKUNINSYA,CHKTANTOSYA,BTANTOSYA,"
                + "ATANTOSYA,UKEIREKOSUU,RYOHINKOSUU,Atumi01,Atumi02,Atumi03,Atumi04,Atumi05,Atumi06,Atumi07,Atumi08,Atumi09,Atumi10,ATUMIMIN,ATUMIMAX," 
                + "BIKO1,BIKO2,BIKO3,BIKO4,TOROKUNICHIJI,KOSINNICHIJI,TENSYASYA,NIJIMICNT,Soujyuryo,Tanijyuryo,cutbashuruicheck,cutbachokushindo," 
                + "cutbasiyoukaisuuST1,cutbasiyoukaisuuST2,programmei,gyoretukakunin,marktorisuu,cuthoseiryou,tableondoset,tableondosoku," 
                + "gaikancheck,hatakasang,syorisetsuu,ryouhinsetsuu,sagyoubasyo,revision,deleteflag"
                + ") SELECT "
                + "KOJYO,LOTNO,EDABAN,KCPNO,?,?,CUTBAMAISUU,GOKI,CUTTABLEONDO,CUTTANTOSYA,KAKUNINSYA,CHKTANTOSYA,BTANTOSYA,"
                + "ATANTOSYA,UKEIREKOSUU,RYOHINKOSUU,Atumi01,Atumi02,Atumi03,Atumi04,Atumi05,Atumi06,Atumi07,Atumi08,Atumi09,Atumi10,ATUMIMIN,ATUMIMAX," 
                + "BIKO1,BIKO2,BIKO3,BIKO4,TOROKUNICHIJI,KOSINNICHIJI,TENSYASYA,NIJIMICNT,Soujyuryo,Tanijyuryo,cutbashuruicheck,cutbachokushindo," 
                + "cutbasiyoukaisuuST1,cutbasiyoukaisuuST2,programmei,gyoretukakunin,marktorisuu,cuthoseiryou,tableondoset,tableondosoku," 
                + "gaikancheck,hatakasang,syorisetsuu,ryouhinsetsuu,sagyoubasyo,?,?"
                + "FROM sr_hapscut "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? ";

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
     * 押切ｶｯﾄ_仮登録(tmp_sr_hapscut)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpHapscut(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sr_hapscut "
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
     * 押切ｶｯﾄ(sr_hapscut)登録処理
     * 
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param hapscut 登録データ
     * @throws SQLException 例外エラー
     */
    private void insertHapscut(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList, SrHapscut hapscut) throws SQLException {
        String sql = "INSERT INTO sr_hapscut ("
                + "KOJYO, LOTNO, EDABAN, KCPNO, KAISINICHIJI, SYURYONICHIJI, CUTBAMAISUU, GOKI, CUTTABLEONDO, CUTTANTOSYA, KAKUNINSYA, CHKTANTOSYA,"
                + "BTANTOSYA, ATANTOSYA, UKEIREKOSUU, RYOHINKOSUU, Atumi01, Atumi02, Atumi03, Atumi04, Atumi05, Atumi06, Atumi07, Atumi08, Atumi09, Atumi10," 
                + "ATUMIMIN, ATUMIMAX, BIKO1, BIKO2, BIKO3, BIKO4, TOROKUNICHIJI, KOSINNICHIJI, TENSYASYA, NIJIMICNT, Soujyuryo, Tanijyuryo, cutbashuruicheck," 
                + "cutbachokushindo, cutbasiyoukaisuuST1, cutbasiyoukaisuuST2, programmei, gyoretukakunin, marktorisuu, cuthoseiryou, tableondoset, tableondosoku," 
                + "gaikancheck, hatakasang, syorisetsuu, ryouhinsetsuu, sagyoubasyo, revision"
                + ") VALUES ("
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," 
                + "?, ?, ?, ?, ?, ?, ?)";
        
        List<Object> params = setUpdateParametersHapscut(true, newRev, kojyo, lotNo, edaban, systemTime, itemList, hapscut);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }
    
    /**
     * 押切ｶｯﾄ(sr_hapscut)更新処理
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
     * @throws SQLException 例外エラー
     */
    private void updateHapscut(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {
        String sql = "UPDATE sr_hapscut SET "
                + "KCPNO = ?,KAISINICHIJI = ?,SYURYONICHIJI = ?,CUTBAMAISUU = ?,GOKI = ?,CUTTANTOSYA = ?,KAKUNINSYA = ?,CHKTANTOSYA = ?,"
                + "BIKO1 = ?,BIKO2 = ?,KOSINNICHIJI = ?,cutbashuruicheck = ?,cutbachokushindo = ?,cutbasiyoukaisuuST1 = ?,cutbasiyoukaisuuST2 = ?," 
                + "programmei = ?,gyoretukakunin = ?,marktorisuu = ?,cuthoseiryou = ?,tableondoset = ?,tableondosoku = ?,gaikancheck = ?," 
                + "hatakasang = ?,syorisetsuu = ?,ryouhinsetsuu = ?,sagyoubasyo = ?,revision = ?"
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ?";

        // 更新前の値を取得
        List<SrHapscut> srSrHapscutList = loadSrHapscut(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrHapscut srHapscut = null;
        if (!srSrHapscutList.isEmpty()) {
            srHapscut = srSrHapscutList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParametersHapscut(false, newRev, "", "", "", systemTime, itemList, srHapscut);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }
    
    /**
     * 押切ｶｯﾄ(sr_hapscut)更新パラメータ設定
     * 
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param data 押切ｶｯﾄﾃﾞｰﾀ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParametersHapscut(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo,
            String edaban, Timestamp systemTime, List<FXHDD01> itemList, SrHapscut data) {
        List<Object> params = new ArrayList<>();
        
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B010Const.KCPNO, data))); //KCPNo
        params.add(DBUtil.stringToDateObject(
                getItemData(itemList, GXHDO101B010Const.KAISHI_DAY, data),
                getItemData(itemList, GXHDO101B010Const.KAISHI_TIME, data))); //開始日時
        params.add(DBUtil.stringToDateObject(
                getItemData(itemList, GXHDO101B010Const.SHURYOU_DAY, data),
                getItemData(itemList, GXHDO101B010Const.SHURYOU_TIME, data))); //終了日時
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B010Const.CUTBA_MAISUU, data))); //ｶｯﾄ刃枚数
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B010Const.GOKI, data))); //号機
        if (isInsert) {
            params.add(""); // ｶｯﾄﾃｰﾌﾞﾙ温度
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B010Const.KAISHI_TANTOUSYA, data))); //ｶｯﾄ担当者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B010Const.KAISHI_KAKUNINSYA, data))); //開始確認者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B010Const.SHURYOU_TANTOUSYA, data))); //終了担当者
        if (isInsert) {
            params.add(""); // B担当者
            params.add(""); // A担当者
            params.add(0); // 受入個数
            params.add(0); // 良品個数
            params.add(0); // 厚み01
            params.add(0); // 厚み02
            params.add(0); // 厚み03
            params.add(0); // 厚み04
            params.add(0); // 厚み05
            params.add(0); // 厚み06
            params.add(0); // 厚み07
            params.add(0); // 厚み08
            params.add(0); // 厚み09
            params.add(0); // 厚み10
            params.add(0); // 厚みMIN
            params.add(0); // 厚みMAX
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B010Const.BIKOU1, data))); //備考1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B010Const.BIKOU2, data))); //備考2
        if (isInsert) {
            params.add(""); // 備考3
            params.add(""); // 備考4
            params.add(systemTime); // 登録日時
        }
        params.add(systemTime); // 更新日時
        if (isInsert) {
            params.add(0); // 転写者
            params.add(0); // ﾆｼﾞﾐ回数
            params.add(0); // 総重量
            params.add(0); // 単位重量
        }
        params.add(getNgOkBlankToInt(
                StringUtil.nullToBlank(getItemData(itemList, GXHDO101B010Const.CUTBA_SHURUI_CHECK, data)))); // ｶｯﾄ刃種類確認
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B010Const.CUTBA_CHOKUSHINDO, data))); // ｶｯﾄ刃直進度
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B010Const.CUTBA_SIYOUKAISUU_ST1, data))); // ｶｯﾄ刃使用回数ST1
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B010Const.CUTBA_SIYOUKAISUU_ST2, data))); // ｶｯﾄ刃使用回数ST2
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B010Const.PROGRAM_MEI, data))); // ﾌﾟﾛｸﾞﾗﾑ名
        params.add(getNgOkBlankToInt(
                StringUtil.nullToBlank(getItemData(itemList, GXHDO101B010Const.GYORETU_KAKUNIN, data)))); // 行×列確認
        params.add(getNgOkBlankToInt(
                StringUtil.nullToBlank(getItemData(itemList, GXHDO101B010Const.MARKTORISU, data)))); // ﾏｰｸ外取り数
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B010Const.CUT_HOSEI_RYOU, data))); // ｶｯﾄ補正量
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B010Const.TABLEONDO_SET, data))); // ﾃｰﾌﾞﾙ温度 設定
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B010Const.TABLEONDO_SOKU, data))); // ﾃｰﾌﾞﾙ温度 実測
        params.add(getNgOkBlankToInt(
                StringUtil.nullToBlank(getItemData(itemList, GXHDO101B010Const.GAIKAN_CHECK, data)))); // 外観確認
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B010Const.HATAKASA_NG, data))); // 刃高さNG
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B010Const.SHORI_SETSU, data))); // 処理ｾｯﾄ数
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B010Const.RYOHIN_SETSU, data))); // 良品ｾｯﾄ数
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B010Const.SAGYOU_BASYO, data))); // 作業場所
        params.add(newRev); // revision
        
        return params;
    }
    
    /**
     * 押切ｶｯﾄ(sr_hapscut)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSrHapscut(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sr_hapscut "
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
     * @param srHapscut 押切ｶｯﾄデータ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrHapscut srHapscut) {
        List<FXHDD01> selectData = 
                listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srHapscut != null) {
            return getHapscutItemData(itemId, srHapscut);
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
     * "NG"→0、"OK"→1、""→9 変換
     * @param value "NG" or "OK" or Blank
     * @return 変換後の数値
     */
    private Integer getNgOkBlankToInt(String value) {
        switch (value) {
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
    private Integer getNgOkBlankToIntForTemp(String value) {
        switch (value) {
            case "NG":
                return 0;
            case "OK":
                return 1;
            default:
                return null;
        }
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
