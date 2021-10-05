/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo102;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
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
import jp.co.kccs.xhd.db.model.SrGlassslurryfunsai;
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
 * 変更日	2021/09/22<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B006(ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕)
 *
 * @author KCSS K.Jo
 * @since 2021/09/22
 */
public class GXHDO102B006 implements IFormLogic {

    private static final Logger LOGGER = Logger.getLogger(GXHDO102B006.class.getName());
    // 仮登録フラグ
    private static final String JOTAI_FLG_KARI_TOROKU = "0";
    // 登録済フラグ
    private static final String JOTAI_FLG_TOROKUZUMI = "1";
    // 削除フラグ
    private static final String JOTAI_FLG_SAKUJO = "9";
    // エラー背景色
    private static final String SQL_STATE_RECORD_LOCK_ERR = "55P03";

    /**
     * コンストラクタ
     */
    public GXHDO102B006() {
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
                    GXHDO102B006Const.BTN_EDABAN_COPY_TOP,
                    GXHDO102B006Const.BTN_FUNSAIKAISINICHIJI_TOP,
                    GXHDO102B006Const.BTN_FUNSAISYUURYOUNICHIJI_TOP,
                    GXHDO102B006Const.BTN_HOKANKAISINICHIJI_TOP,
                    GXHDO102B006Const.BTN_EDABAN_COPY_BOTTOM,
                    GXHDO102B006Const.BTN_FUNSAIKAISINICHIJI_BOTTOM,
                    GXHDO102B006Const.BTN_FUNSAISYUURYOUNICHIJI_BOTTOM,
                    GXHDO102B006Const.BTN_HOKANKAISINICHIJI_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO102B006Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO102B006Const.BTN_INSERT_TOP,
                    GXHDO102B006Const.BTN_DELETE_TOP,
                    GXHDO102B006Const.BTN_UPDATE_TOP,
                    GXHDO102B006Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO102B006Const.BTN_INSERT_BOTTOM,
                    GXHDO102B006Const.BTN_DELETE_BOTTOM,
                    GXHDO102B006Const.BTN_UPDATE_BOTTOM
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
            case GXHDO102B006Const.BTN_EDABAN_COPY_TOP:
            case GXHDO102B006Const.BTN_EDABAN_COPY_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 粉砕開始日時
            case GXHDO102B006Const.BTN_FUNSAIKAISINICHIJI_TOP:
            case GXHDO102B006Const.BTN_FUNSAIKAISINICHIJI_BOTTOM:
                method = "setFunsaikaisinichiji";
                break;
            // 粉砕終了日時
            case GXHDO102B006Const.BTN_FUNSAISYUURYOUNICHIJI_TOP:
            case GXHDO102B006Const.BTN_FUNSAISYUURYOUNICHIJI_BOTTOM:
                method = "setFunsaisyuuryounichiji";
                break;
            // 歩留まり計算
            case GXHDO102B006Const.BTN_BUDOMARI_KEISAN_TOP:
            case GXHDO102B006Const.BTN_BUDOMARI_KEISAN_BOTTOM:
                method = "doBudomariKeisan";
                break;
            // 保管開始日時
            case GXHDO102B006Const.BTN_HOKANKAISINICHIJI_TOP:
            case GXHDO102B006Const.BTN_HOKANKAISINICHIJI_BOTTOM:
                method = "setHokankaishinichiji";
                break;
            // 仮登録
            case GXHDO102B006Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO102B006Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO102B006Const.BTN_INSERT_TOP:
            case GXHDO102B006Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 修正
            case GXHDO102B006Const.BTN_UPDATE_TOP:
            case GXHDO102B006Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO102B006Const.BTN_DELETE_TOP:
            case GXHDO102B006Const.BTN_DELETE_BOTTOM:
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
            QueryRunner queryRunnerWip = new QueryRunner(processData.getDataSourceWip());

            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            String formId = StringUtil.nullToBlank(session.getAttribute("formId"));
            String lotNo = (String) session.getAttribute("lotNo");
            int paramJissekino = (Integer) session.getAttribute("jissekino");
            String kojyo = lotNo.substring(0, 3);
            String lotNo8 = lotNo.substring(3, 11);

            //仕掛情報の取得
            //Map shikakariData = loadShikakariData(queryRunnerWip, lotNo);
            //1.前工程WIPから仕掛情報を取得する。 
            //  仮データ
            Map<String, String> shikakariData = new HashMap<>();
            shikakariData.put("oyalotedaban", "006");
        
            //Map shikakariData = loadShikakariData(queryRunnerWip, lotNo);
            if (shikakariData == null || shikakariData.isEmpty() || !shikakariData.containsKey("oyalotedaban")) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }
            String oyalotEdaban = StringUtil.nullToBlank(getMapData(shikakariData, "oyalotedaban")); //親ﾛｯﾄ枝番

            // (6)[原材料品質DB登録実績]から、ﾃﾞｰﾀを取得
            Map fxhdd11RevInfo = loadFxhdd11RevInfo(queryRunnerDoc, kojyo, lotNo8, oyalotEdaban, paramJissekino, formId);
            if (fxhdd11RevInfo == null || fxhdd11RevInfo.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            String jotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd11RevInfo, "jotai_flg"));

            if (!(JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg) || JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg))) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕の入力項目の登録データ(仮登録時は仮登録データ)を取得
            List<SrGlassslurryfunsai> srGlassslurryfunsaiDataList = getSrGlassslurryfunsaiData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban);
            if (srGlassslurryfunsaiDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // メイン画面データ設定
            setInputItemDataMainForm(processData, srGlassslurryfunsaiDataList.get(0));

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
     * 粉砕開始日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setFunsaikaisinichiji(ProcessData processData) {
        // 粉砕開始日
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B006Const.FUNSAIKAISI_DAY);
        // 粉砕開始時間
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B006Const.FUNSAIKAISI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());

            //「粉砕時間」
            FXHDD01 funsaijikan = getItemRow(processData.getItemList(), GXHDO102B006Const.FUNSAIJIKAN);
            String strFunsaijikan = funsaijikan.getValue();

            if("".equals(strFunsaijikan) || strFunsaijikan == null){
                strFunsaijikan = "0";
            }

            // 粉砕終了予定日    粉砕開始日+粉砕開始時間+粉砕時間(YYMMDD)
            // 粉砕終了予定時間  粉砕開始日+粉砕開始時間+粉砕時間(HHMM)
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmm");
                String strFunsaiKaishiDay = itemDay.getValue();
                String strFunsaiKaishiTime = itemTime.getValue();
                String strFunsaiDatetime = strFunsaiKaishiDay + strFunsaiKaishiTime;
                Date dtFunsaiDatetime = dateFormat.parse(strFunsaiDatetime);
                Calendar calFunsai = Calendar.getInstance();
                calFunsai.setTime(dtFunsaiDatetime);
                calFunsai.add(Calendar.HOUR, Integer.parseInt(strFunsaijikan));
                Date resultDT = calFunsai.getTime();
                String retFunsaiDateTime = dateFormat.format(resultDT);

                //「粉砕終了予定日」を設定する
                this.setItemData(processData, GXHDO102B006Const.FUNSAIYOTEISYUURYOU_DAY, retFunsaiDateTime.substring(0, 6));
                //「粉砕終了予定時間」を設定する
                this.setItemData(processData, GXHDO102B006Const.FUNSAIYOTEISYUURYOU_TIME, retFunsaiDateTime.substring(6, 10));
            } catch (ParseException ex) {
                ErrUtil.outputErrorLog("ParseException発生", ex, LOGGER);
            }
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 粉砕終了日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setFunsaisyuuryounichiji(ProcessData processData) {
        //粉砕終了日
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B006Const.FUNSAISYUURYOU_DAY);
        //粉砕終了時間
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B006Const.FUNSAISYUURYOU_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());         
            
            //(粉砕終了日+粉砕終了時間)-(粉砕開始日+粉砕開始時間)(HH)
            // 粉砕開始日
            FXHDD01 itemFunsaikaisiDay = getItemRow(processData.getItemList(), GXHDO102B006Const.FUNSAIKAISI_DAY);
            // 粉砕開始時間
            FXHDD01 itemFunsaikaisiTime = getItemRow(processData.getItemList(), GXHDO102B006Const.FUNSAIKAISI_TIME);
            if (!StringUtil.isEmpty(itemFunsaikaisiDay.getValue()) && !StringUtil.isEmpty(itemFunsaikaisiTime.getValue())) {
                String strFunsaisyuuryouDay = itemDay.getValue();
                String strFunsaisyuuryouTime = itemTime.getValue();
                String strFunsaikaisiDay = itemFunsaikaisiDay.getValue();
                String strFunsaikaisiTime = itemFunsaikaisiTime.getValue();
                String strFunsaisyuuryouDatetime = strFunsaisyuuryouDay + strFunsaisyuuryouTime;
                String strFunsaikaisiDatetime = strFunsaikaisiDay + strFunsaikaisiTime;

                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmm");
                    Date dtFunsaisyuuryouDatetime = dateFormat.parse(strFunsaisyuuryouDatetime);
                    Date dtFunsaikaisiDatetime = dateFormat.parse(strFunsaikaisiDatetime);

                    long dif = dtFunsaisyuuryouDatetime.getTime() - dtFunsaikaisiDatetime.getTime();
                    long day = dif /(24*60*60*1000);
                    BigDecimal divideVal = new BigDecimal((60*60*1000)-day*24);

                    BigDecimal funsaijikan = new BigDecimal(dif).divide(divideVal, 0, RoundingMode.HALF_UP);

                    //「粉砕時間」を設定する
                    this.setItemData(processData, GXHDO102B006Const.FUNSAIJIKAN, funsaijikan.toPlainString());

                } catch (ParseException ex) {
                    ErrUtil.outputErrorLog("ParseException発生", ex, LOGGER);
                }
            }
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 保管開始日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setHokankaishinichiji(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B006Const.HOKANKAISI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B006Const.HOKANKAISI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }
    
    /**
     * 歩留まり計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doBudomariKeisan(ProcessData processData) {

        // 「総重量」
        FXHDD01 itemSoujyuuryou = getItemRow(processData.getItemList(), GXHDO102B006Const.SOUJYUURYOU);
        // 「風袋重量」
        FXHDD01 itemFuutaijyuuryou = getItemRow(processData.getItemList(), GXHDO102B006Const.FUUTAIJYUURYOU);
        // 「調合量」
        FXHDD01 itemTyougouryou = getItemRow(processData.getItemList(), GXHDO102B006Const.TYOUGOURYOU);
        // 歩留まり計算チェック処理
        ErrorMessageInfo checkItemErrorInfo = checkBudomariKeisan(itemSoujyuuryou, itemFuutaijyuuryou, itemTyougouryou);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        //歩留まり計算処理
        calcBudomari(processData, itemSoujyuuryou, itemFuutaijyuuryou, itemTyougouryou);
        return processData;
    }

    /**
     * 【歩留まり計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
     *
     * @param itemSoujyuuryou 総重量
     * @param itemFuutaijyuuryou 風袋重量
     * @param itemTyougouryou 調合量
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkBudomariKeisan(FXHDD01 itemSoujyuuryou, FXHDD01 itemFuutaijyuuryou, FXHDD01 itemTyougouryou) {
        
        //「総重量」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemSoujyuuryou.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemSoujyuuryou);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, true, errFxhdd01List, itemSoujyuuryou.getLabel1());
        }
        
        //「風袋重量」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemFuutaijyuuryou.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemFuutaijyuuryou);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, true, errFxhdd01List, itemFuutaijyuuryou.getLabel1());
        }
        
        // [総重量]<[風袋重量]場合
        Integer itemSoujyuuryouIntVal = Integer.parseInt(itemSoujyuuryou.getValue());
        Integer itemFuutaijyuuryouIntVal = Integer.parseInt(itemFuutaijyuuryou.getValue());
        if (itemSoujyuuryouIntVal < itemFuutaijyuuryouIntVal) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemSoujyuuryou,itemFuutaijyuuryou);
            return MessageUtil.getErrorMessageInfo("XHD-000023", true, true, errFxhdd01List, itemSoujyuuryou.getLabel1(), itemFuutaijyuuryou.getLabel1());
        }
        
        //「調合量」ﾁｪｯｸ
        String itemTyougouryouVal = itemTyougouryou.getValue();
        if (StringUtil.isEmpty(itemTyougouryouVal) || !NumberUtil.isIntegerNumeric(itemTyougouryouVal) || 0 == Integer.parseInt(itemTyougouryouVal)) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemTyougouryou);
            return MessageUtil.getErrorMessageInfo("XHD-000181", true, true, errFxhdd01List, itemTyougouryou.getLabel1());
        }

        return null;
    }

    /**
     * 歩留まり計算
     *
     * @param processData 処理制御データ
     * @param itemSoujyuuryou 総重量
     * @param itemFuutaijyuuryou 風袋重量
     * @param itemTyougouryou 調合量
     */
    private void calcBudomari(ProcessData processData, FXHDD01 itemSoujyuuryou, FXHDD01 itemFuutaijyuuryou, FXHDD01 itemTyougouryou) {
        try {
            FXHDD01 itemSyoumijyuuryou = getItemRow(processData.getItemList(), GXHDO102B006Const.SYOUMIJYUURYOU); // 正味重量
            FXHDD01 itemBudomari = getItemRow(processData.getItemList(), GXHDO102B006Const.BUDOMARI);             // 歩留まり
            BigDecimal itemSoujyuuryouVal = new BigDecimal(itemSoujyuuryou.getValue());                           // 総重量
            BigDecimal itemFuutaijyuuryouVal = new BigDecimal(itemFuutaijyuuryou.getValue());                     // 風袋重量
            BigDecimal itemTyougouryouVal = new BigDecimal(itemTyougouryou.getValue());                           // 調合量
            
            // ②「正味重量」計算処理  1.「総重量」 - 「風袋重量」 を算出する。
            BigDecimal itemSyoumijyuuryouDecVal = itemSoujyuuryouVal.subtract(itemFuutaijyuuryouVal);
            
            // ③「歩留まり」計算処理
            // 1.「正味重量」 ÷ 「調合量」 を算出する。
            // 2.1の計算結果に100をかける。
            // 3.2の計算結果の小数点第三位を四捨五入する。
            BigDecimal budomari = itemSyoumijyuuryouDecVal.multiply(BigDecimal.valueOf(100)).divide(itemTyougouryouVal, 2, RoundingMode.HALF_UP);

            //正味重量
            itemSyoumijyuuryou.setValue(itemSyoumijyuuryouDecVal.toPlainString());
            //計算結果を誤差率にセット
            itemBudomari.setValue(budomari.toPlainString());

        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
            ErrUtil.outputErrorLog("歩留まり計算にエラー発生", ex, LOGGER);
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
            int jissekiNo = (Integer) session.getAttribute("jissekino");
            String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
            String lotNo8 = lotNo.substring(3, 11); //ﾛｯﾄNo(8桁)
            String edaban = lotNo.substring(11, 14); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // 原材料品質DB登録実績データ取得
            Map fxhdd11RevInfo = loadFxhdd11RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekiNo, formId);
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
                // 原材料品質DB登録実績登録処理
                insertFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekiNo, JOTAI_FLG_KARI_TOROKU, systemTime);
            } else {
                rev = new BigDecimal(processData.getInitRev());
                // 最新のリビジョンを採番
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekiNo, formId);

                // 原材料品質DB登録実績更新処理
                updateFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_KARI_TOROKU, systemTime, jissekiNo);
            }

            if (StringUtil.isEmpty(processData.getInitJotaiFlg()) || JOTAI_FLG_SAKUJO.equals(processData.getInitJotaiFlg())) {

                // ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕_仮登録登録処理
                insertTmpSrGlassslurryfunsai(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, strSystime, processData, formId);
            } else {

                // ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕_仮登録更新処理
                updateTmpSrGlassslurryfunsai(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, strSystime, processData);
            }

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, jissekiNo, "0", kikakuError.getKikakuchiInputErrorInfoList());
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
        processData.setMethod("doResist");

        return processData;
    }

    /**
     * 登録処理(実処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doResist(ProcessData processData) {
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
            int jissekiNo = (Integer) session.getAttribute("jissekino");
            String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
            String lotNo8 = lotNo.substring(3, 11); //ﾛｯﾄNo(8桁)
            String edaban = lotNo.substring(11, 14); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // 原材料品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd11RevInfo = loadFxhdd11RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekiNo, formId);
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
                // 原材料品質DB登録実績登録処理
                insertFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekiNo, JOTAI_FLG_TOROKUZUMI, systemTime);
            } else {
                rev = new BigDecimal(processData.getInitRev());
                // 最新のリビジョンを採番
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekiNo, formId);

                // 原材料品質DB登録実績更新処理
                updateFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_TOROKUZUMI, systemTime, jissekiNo);
            }

            // 仮登録状態の場合、仮登録のデータを削除する。
            SrGlassslurryfunsai tmpSrGlassslurryfunsai = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrGlassslurryfunsai> srGlassslurryfunsaiList = getSrGlassslurryfunsaiData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo8, edaban);
                if (!srGlassslurryfunsaiList.isEmpty()) {
                    tmpSrGlassslurryfunsai = srGlassslurryfunsaiList.get(0);
                }

                deleteTmpSrGlassslurryfunsai(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);
            }

            // ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕_登録処理
            insertSrGlassslurryfunsai(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, strSystime, processData, tmpSrGlassslurryfunsai, formId);

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, jissekiNo, "0", kikakuError.getKikakuchiInputErrorInfoList());
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
        processData.setUserAuthParam(GXHDO102B006Const.USER_AUTH_UPDATE_PARAM);

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
            int jissekiNo = (Integer) session.getAttribute("jissekino");
            String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
            String lotNo8 = lotNo.substring(3, 11); //ﾛｯﾄNo(8桁)
            String edaban = lotNo.substring(11, 14); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // 原材料品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd11RevInfo = loadFxhdd11RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekiNo, formId);
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
            BigDecimal newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekiNo, formId);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            String strSystime = sdf.format(systemTime);
            // 原材料品質DB登録実績更新処理
            updateFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_TOROKUZUMI, systemTime, jissekiNo);

            // ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕_更新処理
            updateSrGlassslurryfunsai(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, strSystime, processData);

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, jissekiNo, "0", kikakuError.getKikakuchiInputErrorInfoList());
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
        processData.setUserAuthParam(GXHDO102B006Const.USER_AUTH_DELETE_PARAM);

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
            String lotNo8 = lotNo.substring(3, 11); //ﾛｯﾄNo(8桁)
            String edaban = lotNo.substring(11, 14); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));

            // 原材料品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd11RevInfo = loadFxhdd11RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, paramJissekino, formId);
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
            BigDecimal newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, paramJissekino, formId);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            String strSystime = sdf.format(systemTime);
            // 原材料品質DB登録実績更新処理
            updateFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_SAKUJO, systemTime, paramJissekino);

            // ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo8, edaban);
            insertDeleteDataTmpSrGlassslurryfunsai(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, strSystime);

            // ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕_削除処理
            deleteSrGlassslurryfunsai(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);

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
                        GXHDO102B006Const.BTN_UPDATE_TOP,
                        GXHDO102B006Const.BTN_DELETE_TOP,
                        GXHDO102B006Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B006Const.BTN_FUNSAIKAISINICHIJI_TOP,
                        GXHDO102B006Const.BTN_FUNSAISYUURYOUNICHIJI_TOP,
                        GXHDO102B006Const.BTN_BUDOMARI_KEISAN_TOP,
                        GXHDO102B006Const.BTN_HOKANKAISINICHIJI_TOP,
                        GXHDO102B006Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B006Const.BTN_DELETE_BOTTOM,
                        GXHDO102B006Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B006Const.BTN_FUNSAIKAISINICHIJI_BOTTOM,
                        GXHDO102B006Const.BTN_FUNSAISYUURYOUNICHIJI_BOTTOM,
                        GXHDO102B006Const.BTN_BUDOMARI_KEISAN_BOTTOM,
                        GXHDO102B006Const.BTN_HOKANKAISINICHIJI_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B006Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B006Const.BTN_INSERT_TOP,
                        GXHDO102B006Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B006Const.BTN_INSERT_BOTTOM));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO102B006Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B006Const.BTN_INSERT_TOP,
                        GXHDO102B006Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B006Const.BTN_FUNSAIKAISINICHIJI_TOP,
                        GXHDO102B006Const.BTN_FUNSAISYUURYOUNICHIJI_TOP,
                        GXHDO102B006Const.BTN_BUDOMARI_KEISAN_TOP,
                        GXHDO102B006Const.BTN_HOKANKAISINICHIJI_TOP,
                        GXHDO102B006Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B006Const.BTN_INSERT_BOTTOM,
                        GXHDO102B006Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B006Const.BTN_FUNSAIKAISINICHIJI_BOTTOM,
                        GXHDO102B006Const.BTN_FUNSAISYUURYOUNICHIJI_BOTTOM,
                        GXHDO102B006Const.BTN_BUDOMARI_KEISAN_BOTTOM,
                        GXHDO102B006Const.BTN_HOKANKAISINICHIJI_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B006Const.BTN_UPDATE_TOP,
                        GXHDO102B006Const.BTN_DELETE_TOP,
                        GXHDO102B006Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B006Const.BTN_DELETE_BOTTOM
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

        // ②仕掛情報取得処理
        //Map shikakariData = loadShikakariData(queryRunnerWip, lotNo);
        //1.前工程WIPから仕掛情報を取得する。 
        //  仮データ
        Map<String, String> shikakariData = new HashMap<>();
        shikakariData.put("hinmei", "品名123");
        shikakariData.put("lotkubuncode", "2002");
        shikakariData.put("lotno", "82001240");
        shikakariData.put("oyalotedaban", "006");

        if (shikakariData == null || shikakariData.isEmpty()) {
            errorMessageList.add(MessageUtil.getMessage("XHD-000029"));
        }
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); // ﾛｯﾄ区分ｺｰﾄﾞ
        String hinmei = StringUtil.nullToBlank(getMapData(shikakariData, "hinmei"));// 品名
        String oyalotedaban = StringUtil.nullToBlank(getMapData(shikakariData, "oyalotedaban"));// 親ﾛｯﾄ枝番
        String lotno = StringUtil.nullToBlank(getMapData(shikakariData, "lotno"));// LotNo
        Map hiddenMap = processData.getHiddenDataMap();
        hiddenMap.put("lotkubuncode", lotkubuncode);
        hiddenMap.put("hinmei", hinmei);
        hiddenMap.put("oyalotedaban", oyalotedaban);
        hiddenMap.put("lotno", lotno);

        // ﾛｯﾄ区分ﾏｽﾀ情報の取得
        Map lotKbnMasData = loadLotKbnMas(queryRunnerWip, lotkubuncode);
        if (lotKbnMasData == null || lotKbnMasData.isEmpty()) {
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
        setViewItemData(processData, lotKbnMasData, shikakariData, lotNo);
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
        String lotNo2 = lotNo.substring(3, 11);
        // 設計データの取得
        return CommonUtil.getMkSekkeiInfo(queryRunnerQcdb, queryRunnerWip, lotNo1, lotNo2, "001");
    }

    /**
     * 入力項目以外のデータを画面項目に設定
     *
     * @param processData 処理制御データ
     * @param sekkeiData 設計データ
     * @param lotKbnMasData ﾛｯﾄ区分ﾏｽﾀデータ
     * @param ownerMasData ｵｰﾅｰﾏｽﾀデータ
     * @param shikakariData 仕掛データ
     * @param lotNo ﾛｯﾄNo
     */
    private void setViewItemData(ProcessData processData, Map lotKbnMasData, Map shikakariData, String lotNo) {
        // WIPﾛｯﾄNo
        this.setItemData(processData, GXHDO102B006Const.WIPLOTNO, lotNo);
        // ｶﾞﾗｽｽﾗﾘｰ品名
        this.setItemData(processData, GXHDO102B006Const.GLASSSLURRYHINMEI, StringUtil.nullToBlank(getMapData(shikakariData, "hinmei")));
        // ｶﾞﾗｽｽﾗﾘｰLotNo
        this.setItemData(processData, GXHDO102B006Const.GLASSSLURRYLOTNO, StringUtil.nullToBlank(getMapData(shikakariData, "lotno")));
        // ﾛｯﾄ区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode"));
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO102B006Const.LOTKUBUN, "");
        } else {
            String lotKubun = StringUtil.nullToBlank(getMapData(lotKbnMasData, "lotkubun"));
            this.setItemData(processData, GXHDO102B006Const.LOTKUBUN, lotkubuncode + ":" + lotKubun);
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

        List<SrGlassslurryfunsai> srGlassslurryfunsaiList = new ArrayList<>();
        String rev = "";
        String jotaiFlg = "";
        String kojyo = lotNo.substring(0, 3);
        String lotNo8 = lotNo.substring(3, 11);
        String edaban = lotNo.substring(11, 14);

        for (int i = 0; i < 5; i++) {
            // (3)[原材料品質DB登録実績]から、ﾃﾞｰﾀを取得
            Map fxhdd11RevInfo = loadFxhdd11RevInfo(queryRunnerDoc, kojyo, lotNo8, edaban, jissekino, formId);
            rev = StringUtil.nullToBlank(getMapData(fxhdd11RevInfo, "rev"));
            jotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd11RevInfo, "jotai_flg"));

            // revisionが空のまたはjotaiFlgが"0"でも"1"でもない場合、新規としてデフォルト値を設定してリターンする。
            if (StringUtil.isEmpty(rev) || !(JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg) || JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg))) {
                // 【Ⅵ.画面項目制御・出力仕様.初期表示時①】を元に画面表示を行う。
                processData.setInitRev(rev);
                processData.setInitJotaiFlg(jotaiFlg);

                // 画面にデータを設定する(デフォルト値)
                processData.getItemList().forEach((fxhdd001) -> {
                    this.setItemData(processData, fxhdd001.getItemId(), fxhdd001.getInputDefault());
                });

                return true;
            }

            // ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕データ取得
            srGlassslurryfunsaiList = getSrGlassslurryfunsaiData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban);
            if (srGlassslurryfunsaiList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srGlassslurryfunsaiList.isEmpty()) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srGlassslurryfunsaiList.get(0));

        return true;

    }

    /**
     * データ設定処理
     *
     * @param processData 処理制御データ
     * @param srGlassslurryfunsai ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕
     */
    private void setInputItemDataMainForm(ProcessData processData, SrGlassslurryfunsai srGlassslurryfunsai) {
        // ｶﾞﾗｽｽﾗﾘｰ品名
        this.setItemData(processData, GXHDO102B006Const.GLASSSLURRYHINMEI, getSrGlassslurryfunsaiItemData(GXHDO102B006Const.GLASSSLURRYHINMEI, srGlassslurryfunsai));

        // ｶﾞﾗｽｽﾗﾘｰLotNo
        this.setItemData(processData, GXHDO102B006Const.GLASSSLURRYLOTNO, getSrGlassslurryfunsaiItemData(GXHDO102B006Const.GLASSSLURRYLOTNO, srGlassslurryfunsai));

        // ﾛｯﾄ区分
        this.setItemData(processData, GXHDO102B006Const.LOTKUBUN, getSrGlassslurryfunsaiItemData(GXHDO102B006Const.LOTKUBUN, srGlassslurryfunsai));

        // 粉砕回転台号機
        this.setItemData(processData, GXHDO102B006Const.KAITENDAIGOUKI, getSrGlassslurryfunsaiItemData(GXHDO102B006Const.KAITENDAIGOUKI, srGlassslurryfunsai));

        // 粉砕開始日
        this.setItemData(processData, GXHDO102B006Const.FUNSAIKAISI_DAY, getSrGlassslurryfunsaiItemData(GXHDO102B006Const.FUNSAIKAISI_DAY, srGlassslurryfunsai));

        // 粉砕開始時間
        this.setItemData(processData, GXHDO102B006Const.FUNSAIKAISI_TIME, getSrGlassslurryfunsaiItemData(GXHDO102B006Const.FUNSAIKAISI_TIME, srGlassslurryfunsai));

        // 粉砕終了予定日
        this.setItemData(processData, GXHDO102B006Const.FUNSAIYOTEISYUURYOU_DAY, getSrGlassslurryfunsaiItemData(GXHDO102B006Const.FUNSAIYOTEISYUURYOU_DAY, srGlassslurryfunsai));

        // 粉砕終了予定時間
        this.setItemData(processData, GXHDO102B006Const.FUNSAIYOTEISYUURYOU_TIME, getSrGlassslurryfunsaiItemData(GXHDO102B006Const.FUNSAIYOTEISYUURYOU_TIME, srGlassslurryfunsai));

        // 粉砕開始担当者
        this.setItemData(processData, GXHDO102B006Const.KAISITANTOSYA, getSrGlassslurryfunsaiItemData(GXHDO102B006Const.KAISITANTOSYA, srGlassslurryfunsai));

        // 粉砕終了日
        this.setItemData(processData, GXHDO102B006Const.FUNSAISYUURYOU_DAY, getSrGlassslurryfunsaiItemData(GXHDO102B006Const.FUNSAISYUURYOU_DAY, srGlassslurryfunsai));

        // 粉砕終了時間
        this.setItemData(processData, GXHDO102B006Const.FUNSAISYUURYOU_TIME, getSrGlassslurryfunsaiItemData(GXHDO102B006Const.FUNSAISYUURYOU_TIME, srGlassslurryfunsai));

        // 粉砕終了担当者
        this.setItemData(processData, GXHDO102B006Const.SYURYOTANTOSYA, getSrGlassslurryfunsaiItemData(GXHDO102B006Const.SYURYOTANTOSYA, srGlassslurryfunsai));

        // 粉砕時間
        this.setItemData(processData, GXHDO102B006Const.FUNSAIJIKAN, getSrGlassslurryfunsaiItemData(GXHDO102B006Const.FUNSAIJIKAN, srGlassslurryfunsai));

        // 部材在庫No
        this.setItemData(processData, GXHDO102B006Const.BUZAIZAIKONO, getSrGlassslurryfunsaiItemData(GXHDO102B006Const.BUZAIZAIKONO, srGlassslurryfunsai));

        // 調合量
        this.setItemData(processData, GXHDO102B006Const.TYOUGOURYOU, getSrGlassslurryfunsaiItemData(GXHDO102B006Const.TYOUGOURYOU, srGlassslurryfunsai));

        // 風袋重量
        this.setItemData(processData, GXHDO102B006Const.FUUTAIJYUURYOU, getSrGlassslurryfunsaiItemData(GXHDO102B006Const.FUUTAIJYUURYOU, srGlassslurryfunsai));

        // 総重量
        this.setItemData(processData, GXHDO102B006Const.SOUJYUURYOU, getSrGlassslurryfunsaiItemData(GXHDO102B006Const.SOUJYUURYOU, srGlassslurryfunsai));

        // 正味重量
        this.setItemData(processData, GXHDO102B006Const.SYOUMIJYUURYOU, getSrGlassslurryfunsaiItemData(GXHDO102B006Const.SYOUMIJYUURYOU, srGlassslurryfunsai));

        // 歩留まり
        this.setItemData(processData, GXHDO102B006Const.BUDOMARI, getSrGlassslurryfunsaiItemData(GXHDO102B006Const.BUDOMARI, srGlassslurryfunsai));

        // 排出担当者
        this.setItemData(processData, GXHDO102B006Const.HAISYUTUTANTOUSYA, getSrGlassslurryfunsaiItemData(GXHDO102B006Const.HAISYUTUTANTOUSYA, srGlassslurryfunsai));

        // 保管開始日
        this.setItemData(processData, GXHDO102B006Const.HOKANKAISI_DAY, getSrGlassslurryfunsaiItemData(GXHDO102B006Const.HOKANKAISI_DAY, srGlassslurryfunsai));

        // 保管開始時間
        this.setItemData(processData, GXHDO102B006Const.HOKANKAISI_TIME, getSrGlassslurryfunsaiItemData(GXHDO102B006Const.HOKANKAISI_TIME, srGlassslurryfunsai));

        // 保管担当者
        this.setItemData(processData, GXHDO102B006Const.HOKANTANTOSYA, getSrGlassslurryfunsaiItemData(GXHDO102B006Const.HOKANTANTOSYA, srGlassslurryfunsai));

        // 備考1
        this.setItemData(processData, GXHDO102B006Const.BIKOU1, getSrGlassslurryfunsaiItemData(GXHDO102B006Const.BIKOU1, srGlassslurryfunsai));

        // 備考2
        this.setItemData(processData, GXHDO102B006Const.BIKOU2, getSrGlassslurryfunsaiItemData(GXHDO102B006Const.BIKOU2, srGlassslurryfunsai));

    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrGlassslurryfunsai> getSrGlassslurryfunsaiData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrGlassslurryfunsai(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSrGlassslurryfunsai(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        }
    }

    /**
     * [ﾛｯﾄ区分ﾏｽﾀｰ]から、ﾛｯﾄ区分を取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param lotKubunCode ﾛｯﾄ区分ｺｰﾄﾞ(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadLotKbnMas(QueryRunner queryRunnerDoc, String lotKubunCode) throws SQLException {

        // 設計データの取得
        String sql = " SELECT lotkubun "
                + "      FROM lotkumas "
                + "     WHERE lotkubuncode = ? ";

        List<Object> params = new ArrayList<>();
        params.add(lotKubunCode);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerDoc.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * 前工程WIPから仕掛情報を取得する。
     *
     * @param queryRunnerWip QueryRunnerオブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadShikakariData(QueryRunner queryRunnerWip, String lotNo) throws SQLException {
        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 11);
        String lotNo3 = lotNo.substring(11, 14);

        // 仕掛情報データの取得
        String sql = "SELECT kcpno, lotno, oyalotedaban, tokuisaki, lotkubuncode, ownercode, tanijuryo "
                + " FROM sikakari WHERE kojyo = ? AND lotno = ? AND edaban = ? ";

        List<Object> params = new ArrayList<>();
        params.add(lotNo1);
        params.add(lotNo2);
        params.add(lotNo3);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map shikakariData = queryRunnerWip.query(sql, new MapHandler(), params.toArray());
        // TODO
        // 前工程WIPから取得した品名
        if (shikakariData != null) {
            shikakariData.put("hinmei", "品名123");
        }

        return shikakariData;
    }

    /**
     * [原材料品質DB登録実績]から、ﾘﾋﾞｼﾞｮﾝ,状態ﾌﾗｸﾞを取得
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
        // 原材料品質DB登録実績データの取得
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
     * [原材料品質DB登録実績]から、ﾃﾞｰﾀを取得
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
        // 原材料品質DB登録実績データの取得
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
        // 原材料品質DB登録実績データの取得
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
     * [ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrGlassslurryfunsai> loadSrGlassslurryfunsai(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = " SELECT "
                + " kojyo,lotno,edaban,glassslurryhinmei,glassslurrylotno,lotkubun,syuusoku,kaitendaigouki,funsaikaisinichiji, "
                + " funsaiyoteisyuuryounichiji,kaisitantosya,funsaisyuuryounichiji,syuryotantosya,funsaijikan,zairyohinmei, "
                + " buzaizaikono,tyougouryou,fuutaijyuuryou,soujyuuryou,syoumijyuuryou,budomari,haisyututantousya,hokankaisinichiji, "
                + " hokanbasyo,hokankaitengouki,kaitensuu,hokantantosya,bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + " FROM sr_glassslurryfunsai "
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
        mapping.put("kojyo", "kojyo");                                             // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                             // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                           // 枝番
        mapping.put("glassslurryhinmei", "glassslurryhinmei");                     // ｶﾞﾗｽｽﾗﾘｰ品名
        mapping.put("glassslurrylotno", "glassslurrylotno");                       // ｶﾞﾗｽｽﾗﾘｰ品名LotNo
        mapping.put("lotkubun", "lotkubun");                                       // ﾛｯﾄ区分
        mapping.put("syuusoku", "syuusoku");                                       // 周速
        mapping.put("kaitendaigouki", "kaitendaigouki");                           // 粉砕回転台号機
        mapping.put("funsaikaisinichiji", "funsaikaisinichiji");                   // 粉砕開始日時
        mapping.put("funsaiyoteisyuuryounichiji", "funsaiyoteisyuuryounichiji");   // 粉砕終了予定日時
        mapping.put("kaisitantosya", "kaisitantosya");                             // 粉砕開始担当者
        mapping.put("funsaisyuuryounichiji", "funsaisyuuryounichiji");             // 粉砕終了日時
        mapping.put("syuryotantosya", "syuryotantosya");                           // 粉砕終了担当者
        mapping.put("funsaijikan", "funsaijikan");                                 // 粉砕時間
        mapping.put("zairyohinmei", "zairyohinmei");                               // 材料品名
        mapping.put("buzaizaikono", "buzaizaikono");                               // 部材在庫No
        mapping.put("tyougouryou", "tyougouryou");                                 // 調合量
        mapping.put("fuutaijyuuryou", "fuutaijyuuryou");                           // 風袋重量
        mapping.put("soujyuuryou", "soujyuuryou");                                 // 総重量
        mapping.put("syoumijyuuryou", "syoumijyuuryou");                           // 正味重量
        mapping.put("budomari", "budomari");                                       // 歩留まり
        mapping.put("haisyututantousya", "haisyututantousya");                     // 排出担当者
        mapping.put("hokankaisinichiji", "hokankaisinichiji");                     // 保管開始日時
        mapping.put("hokanbasyo", "hokanbasyo");                                   // 保管場所
        mapping.put("hokankaitengouki", "hokankaitengouki");                       // 保管回転台号機
        mapping.put("kaitensuu", "kaitensuu");                                     // 回転数
        mapping.put("hokantantosya", "hokantantosya");                             // 保管担当者
        mapping.put("bikou1", "bikou1");                                           // 備考1
        mapping.put("bikou2", "bikou2");                                           // 備考2
        mapping.put("torokunichiji", "torokunichiji");                             // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                               // 更新日時
        mapping.put("revision", "revision");                                       // revision

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrGlassslurryfunsai>> beanHandler = new BeanListHandler<>(SrGlassslurryfunsai.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrGlassslurryfunsai> loadTmpSrGlassslurryfunsai(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = " SELECT "
                + " kojyo,lotno,edaban,glassslurryhinmei,glassslurrylotno,lotkubun,syuusoku,kaitendaigouki,funsaikaisinichiji, "
                + " funsaiyoteisyuuryounichiji,kaisitantosya,funsaisyuuryounichiji,syuryotantosya,funsaijikan,zairyohinmei, "
                + " buzaizaikono,tyougouryou,fuutaijyuuryou,soujyuuryou,syoumijyuuryou,budomari,haisyututantousya,hokankaisinichiji, "
                + " hokanbasyo,hokankaitengouki,kaitensuu,hokantantosya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + " FROM tmp_sr_glassslurryfunsai "
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
        mapping.put("kojyo", "kojyo");                                             // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                             // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                           // 枝番
        mapping.put("glassslurryhinmei", "glassslurryhinmei");                     // ｶﾞﾗｽｽﾗﾘｰ品名
        mapping.put("glassslurrylotno", "glassslurrylotno");                       // ｶﾞﾗｽｽﾗﾘｰ品名LotNo
        mapping.put("lotkubun", "lotkubun");                                       // ﾛｯﾄ区分
        mapping.put("syuusoku", "syuusoku");                                       // 周速
        mapping.put("kaitendaigouki", "kaitendaigouki");                           // 粉砕回転台号機
        mapping.put("funsaikaisinichiji", "funsaikaisinichiji");                   // 粉砕開始日時
        mapping.put("funsaiyoteisyuuryounichiji", "funsaiyoteisyuuryounichiji");   // 粉砕終了予定日時
        mapping.put("kaisitantosya", "kaisitantosya");                             // 粉砕開始担当者
        mapping.put("funsaisyuuryounichiji", "funsaisyuuryounichiji");             // 粉砕終了日時
        mapping.put("syuryotantosya", "syuryotantosya");                           // 粉砕終了担当者
        mapping.put("funsaijikan", "funsaijikan");                                 // 粉砕時間
        mapping.put("zairyohinmei", "zairyohinmei");                               // 材料品名
        mapping.put("buzaizaikono", "buzaizaikono");                               // 部材在庫No
        mapping.put("tyougouryou", "tyougouryou");                                 // 調合量
        mapping.put("fuutaijyuuryou", "fuutaijyuuryou");                           // 風袋重量
        mapping.put("soujyuuryou", "soujyuuryou");                                 // 総重量
        mapping.put("syoumijyuuryou", "syoumijyuuryou");                           // 正味重量
        mapping.put("budomari", "budomari");                                       // 歩留まり
        mapping.put("haisyututantousya", "haisyututantousya");                     // 排出担当者
        mapping.put("hokankaisinichiji", "hokankaisinichiji");                     // 保管開始日時
        mapping.put("hokanbasyo", "hokanbasyo");                                   // 保管場所
        mapping.put("hokankaitengouki", "hokankaitengouki");                       // 保管回転台号機
        mapping.put("kaitensuu", "kaitensuu");                                     // 回転数
        mapping.put("hokantantosya", "hokantantosya");                             // 保管担当者
        mapping.put("bikou1", "bikou1");                                           // 備考1
        mapping.put("bikou2", "bikou2");                                           // 備考2
        mapping.put("torokunichiji", "torokunichiji");                             // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                               // 更新日時
        mapping.put("revision", "revision");                                       // revision
        mapping.put("deleteflag", "deleteflag");                                   // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrGlassslurryfunsai>> beanHandler = new BeanListHandler<>(SrGlassslurryfunsai.class, rowProcessor);

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
     * @param srGlassslurryfunsai ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕データ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrGlassslurryfunsai srGlassslurryfunsai) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srGlassslurryfunsai != null) {
            // 元データが存在する場合元データより取得
            return getSrGlassslurryfunsaiItemData(itemId, srGlassslurryfunsai);
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
    private String getItemKikakuchi(List<FXHDD01> listData, String itemId, SrGlassslurryfunsai srGlassslurryfunsai) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return StringUtil.nullToBlank(selectData.get(0).getKikakuChi()).replace("【", "").replace("】", "");
        } else if (srGlassslurryfunsai != null) {
            // 元データが存在する場合元データより取得
            return getSrGlassslurryfunsaiItemData(itemId, srGlassslurryfunsai);
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
        String sql = " INSERT INTO fxhdd11 ( "
                + " torokusha ,toroku_date ,koshinsha ,koshin_date ,gamen_id ,rev ,kojyo ,lotno , "
                + " edaban ,jissekino ,jotai_flg ,tsuika_kotei_flg "
                + " ) VALUES ( "
                + " ?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

        List<Object> params = new ArrayList<>();
        params.add(tantoshaCd); //登録者
        params.add(systemTime); //登録日
        params.add(null);       //更新者
        params.add(null);       //更新日
        params.add(formId);     //画面ID
        params.add(rev);        //revision
        params.add(kojyo);      //工場ｺｰﾄﾞ
        params.add(lotNo);      //ﾛｯﾄNo
        params.add(edaban);     //枝番
        params.add(jissekino);  //実績No
        params.add(jotaiFlg);   //状態ﾌﾗｸﾞ
        params.add("0");        //追加工程ﾌﾗｸﾞ

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
        String sql = "UPDATE fxhdd11 "
                + "      SET koshinsha = ? "
                + "         ,koshin_date = ? "
                + "         ,rev = ? "
                + "         ,jotai_flg = ? "
                + "    WHERE gamen_id = ? "
                + "      AND kojyo = ? "
                + "      AND lotno = ? "
                + "      AND edaban = ? "
                + "      AND jissekino = ? ";

        List<Object> params = new ArrayList<>();
        // 更新内容
        params.add(tantoshaCd); //更新者
        params.add(systemTime); //更新日
        params.add(rev);        //revision
        params.add(jotaiFlg);   //状態ﾌﾗｸﾞ

        // 検索条件
        params.add(formId);     //画面ID
        params.add(kojyo);      //工場ｺｰﾄﾞ
        params.add(lotNo);      //ﾛｯﾄNo
        params.add(edaban);     //枝番
        params.add(jissekino);  //実績No

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerDoc.update(conDoc, sql, params.toArray());
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕_仮登録(tmp_sr_glassslurryfunsai)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void insertTmpSrGlassslurryfunsai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData, String formId) throws SQLException {

        String sql = "INSERT INTO tmp_sr_glassslurryfunsai ( "
                + " kojyo,lotno,edaban,glassslurryhinmei,glassslurrylotno,lotkubun,syuusoku,kaitendaigouki,funsaikaisinichiji, "
                + " funsaiyoteisyuuryounichiji,kaisitantosya,funsaisyuuryounichiji,syuryotantosya,funsaijikan,zairyohinmei, "
                + " buzaizaikono,tyougouryou,fuutaijyuuryou,soujyuuryou,syoumijyuuryou,budomari,haisyututantousya,hokankaisinichiji, "
                + " hokanbasyo,hokankaitengouki,kaitensuu,hokantantosya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + " ) VALUES ( "
                + " ? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,?, ?) ";

        List<Object> params = setUpdateParameterTmpSrGlassslurryfunsai(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, processData, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕_仮登録(tmp_sr_glassslurryfunsai)更新処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void updateTmpSrGlassslurryfunsai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "UPDATE tmp_sr_glassslurryfunsai SET "
                + " glassslurryhinmei = ?,glassslurrylotno = ?,lotkubun = ?,syuusoku = ?,kaitendaigouki = ?,funsaikaisinichiji = ?,funsaiyoteisyuuryounichiji = ?, "
                + " kaisitantosya = ?,funsaisyuuryounichiji = ?,syuryotantosya = ?,funsaijikan = ?,zairyohinmei = ?,buzaizaikono = ?,tyougouryou = ?, "
                + " fuutaijyuuryou = ?,soujyuuryou = ?,syoumijyuuryou = ?,budomari = ?,haisyututantousya = ?,hokankaisinichiji = ?,hokanbasyo = ?, "
                + " hokankaitengouki = ?,kaitensuu = ?,hokantantosya = ?,bikou1 = ?,bikou2 = ?,kosinnichiji = ?,revision = ?,deleteflag = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrGlassslurryfunsai> srGlassslurryfunsaiList = getSrGlassslurryfunsaiData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrGlassslurryfunsai srGlassslurryfunsai = null;
        if (!srGlassslurryfunsaiList.isEmpty()) {
            srGlassslurryfunsai = srGlassslurryfunsaiList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrGlassslurryfunsai(false, newRev, 0, "", "", "", systemTime, processData, srGlassslurryfunsai);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕_仮登録(tmp_sr_glassslurryfunsai)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrGlassslurryfunsai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sr_glassslurryfunsai "
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
     * ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕_仮登録(tmp_sr_glassslurryfunsai)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srGlassslurryfunsai ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrGlassslurryfunsai(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, String systemTime, ProcessData processData, SrGlassslurryfunsai srGlassslurryfunsai) {

        List<FXHDD01> pItemList = processData.getItemList();
        List<Object> params = new ArrayList<>();
        // 粉砕開始時間
        String funsaikaisinichijiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B006Const.FUNSAIKAISI_TIME, srGlassslurryfunsai));
        // 粉砕終了予定時間
        String funsaiyoteisyuuryounichijiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B006Const.FUNSAIYOTEISYUURYOU_TIME, srGlassslurryfunsai));
        // 粉砕終了時間
        String funsaisyuuryounichijiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B006Const.FUNSAISYUURYOU_TIME, srGlassslurryfunsai));
        // 保管開始時間
        String hokankaisinichijiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B006Const.HOKANKAISI_TIME, srGlassslurryfunsai));
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B006Const.GLASSSLURRYHINMEI, srGlassslurryfunsai))); // ｶﾞﾗｽｽﾗﾘｰ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B006Const.GLASSSLURRYLOTNO, srGlassslurryfunsai))); // ｶﾞﾗｽｽﾗﾘｰ品名LotNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B006Const.LOTKUBUN, srGlassslurryfunsai))); // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B006Const.SYUUSOKU, srGlassslurryfunsai))); // 周速
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B006Const.KAITENDAIGOUKI, srGlassslurryfunsai))); // 粉砕回転台号機
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B006Const.FUNSAIKAISI_DAY, srGlassslurryfunsai),
                "".equals(funsaikaisinichijiTime) ? "0000" : funsaikaisinichijiTime)); // 粉砕開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B006Const.FUNSAIYOTEISYUURYOU_DAY, srGlassslurryfunsai),
                "".equals(funsaiyoteisyuuryounichijiTime) ? "0000" : funsaiyoteisyuuryounichijiTime)); // 粉砕終了予定日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B006Const.KAISITANTOSYA, srGlassslurryfunsai))); // 粉砕開始担当者
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B006Const.FUNSAISYUURYOU_DAY, srGlassslurryfunsai),
                "".equals(funsaisyuuryounichijiTime) ? "0000" : funsaisyuuryounichijiTime)); // 粉砕終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B006Const.SYURYOTANTOSYA, srGlassslurryfunsai))); // 粉砕終了担当者
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B006Const.FUNSAIJIKAN, srGlassslurryfunsai))); // 粉砕時間
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B006Const.ZAIRYOHINMEI, srGlassslurryfunsai))); // 材料品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B006Const.BUZAIZAIKONO, srGlassslurryfunsai))); // 部材在庫No
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B006Const.TYOUGOURYOU, srGlassslurryfunsai))); // 調合量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B006Const.FUUTAIJYUURYOU, srGlassslurryfunsai))); // 風袋重量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B006Const.SOUJYUURYOU, srGlassslurryfunsai))); // 総重量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B006Const.SYOUMIJYUURYOU, srGlassslurryfunsai))); // 正味重量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B006Const.BUDOMARI, srGlassslurryfunsai))); // 歩留まり
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B006Const.HAISYUTUTANTOUSYA, srGlassslurryfunsai))); // 排出担当者
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B006Const.HOKANKAISI_DAY, srGlassslurryfunsai),
                "".equals(hokankaisinichijiTime) ? "0000" : hokankaisinichijiTime)); // 保管開始日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B006Const.HOKANBASYO, srGlassslurryfunsai))); // 保管場所
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B006Const.HOKANKAITENGOUKI, srGlassslurryfunsai))); // 保管回転台号機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B006Const.KAITENSUU, srGlassslurryfunsai))); // 回転数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B006Const.HOKANTANTOSYA, srGlassslurryfunsai))); // 保管担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B006Const.BIKOU1, srGlassslurryfunsai))); // 備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B006Const.BIKOU2, srGlassslurryfunsai))); // 備考2

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
     * ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕(sr_glassslurryfunsai)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param tmpSrGlassslurryfunsai 仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrGlassslurryfunsai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData, SrGlassslurryfunsai tmpSrGlassslurryfunsai, String formId) throws SQLException {

        String sql = "INSERT INTO sr_glassslurryfunsai ( "
                + " kojyo,lotno,edaban,glassslurryhinmei,glassslurrylotno,lotkubun,syuusoku,kaitendaigouki,funsaikaisinichiji, "
                + " funsaiyoteisyuuryounichiji,kaisitantosya,funsaisyuuryounichiji,syuryotantosya,funsaijikan,zairyohinmei, "
                + " buzaizaikono,tyougouryou,fuutaijyuuryou,soujyuuryou,syoumijyuuryou,budomari,haisyututantousya,hokankaisinichiji, "
                + " hokanbasyo,hokankaitengouki,kaitensuu,hokantantosya,bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + " ) VALUES ( "
                + " ? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,?) ";

        List<Object> params = setUpdateParameterSrGlassslurryfunsai(true, newRev, kojyo, lotNo, edaban, systemTime, processData, tmpSrGlassslurryfunsai);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕(sr_glassslurryfunsai)更新処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void updateSrGlassslurryfunsai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "UPDATE sr_glassslurryfunsai SET "
                + " glassslurryhinmei = ?,glassslurrylotno = ?,lotkubun = ?,syuusoku = ?,kaitendaigouki = ?,funsaikaisinichiji = ?,funsaiyoteisyuuryounichiji = ?, "
                + " kaisitantosya = ?,funsaisyuuryounichiji = ?,syuryotantosya = ?,funsaijikan = ?,zairyohinmei = ?,buzaizaikono = ?,tyougouryou = ?, "
                + " fuutaijyuuryou = ?,soujyuuryou = ?,syoumijyuuryou = ?,budomari = ?,haisyututantousya = ?,hokankaisinichiji = ?,hokanbasyo = ?, "
                + " hokankaitengouki = ?,kaitensuu = ?,hokantantosya = ?,bikou1 = ?,bikou2 = ?,kosinnichiji = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrGlassslurryfunsai> srGlassslurryfunsaiList = getSrGlassslurryfunsaiData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrGlassslurryfunsai srGlassslurryfunsai = null;
        if (!srGlassslurryfunsaiList.isEmpty()) {
            srGlassslurryfunsai = srGlassslurryfunsaiList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrGlassslurryfunsai(false, newRev, "", "", "", systemTime, processData, srGlassslurryfunsai);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕(sr_glassslurryfunsai)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srGlassslurryfunsai ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrGlassslurryfunsai(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            String systemTime, ProcessData processData, SrGlassslurryfunsai srGlassslurryfunsai) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // 粉砕開始時間
        String funsaikaisinichijiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B006Const.FUNSAIKAISI_TIME, srGlassslurryfunsai));
        // 粉砕終了予定時間
        String funsaiyoteisyuuryounichijiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B006Const.FUNSAIYOTEISYUURYOU_TIME, srGlassslurryfunsai));
        // 粉砕終了時間
        String funsaisyuuryounichijiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B006Const.FUNSAISYUURYOU_TIME, srGlassslurryfunsai));
        // 保管開始時間
        String hokankaisinichijiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B006Const.HOKANKAISI_TIME, srGlassslurryfunsai));
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }

        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B006Const.GLASSSLURRYHINMEI, srGlassslurryfunsai))); // ｶﾞﾗｽｽﾗﾘｰ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B006Const.GLASSSLURRYLOTNO, srGlassslurryfunsai))); // ｶﾞﾗｽｽﾗﾘｰ品名LotNo
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B006Const.LOTKUBUN, srGlassslurryfunsai))); // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B006Const.SYUUSOKU, srGlassslurryfunsai))); // 周速
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B006Const.KAITENDAIGOUKI, srGlassslurryfunsai))); // 粉砕回転台号機
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B006Const.FUNSAIKAISI_DAY, srGlassslurryfunsai),
                "".equals(funsaikaisinichijiTime) ? "0000" : funsaikaisinichijiTime)); // 粉砕開始日時
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B006Const.FUNSAIYOTEISYUURYOU_DAY, srGlassslurryfunsai),
                "".equals(funsaiyoteisyuuryounichijiTime) ? "0000" : funsaiyoteisyuuryounichijiTime)); // 粉砕終了予定日時
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B006Const.KAISITANTOSYA, srGlassslurryfunsai))); // 粉砕開始担当者
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B006Const.FUNSAISYUURYOU_DAY, srGlassslurryfunsai),
                "".equals(funsaisyuuryounichijiTime) ? "0000" : funsaisyuuryounichijiTime)); // 粉砕終了日時
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B006Const.SYURYOTANTOSYA, srGlassslurryfunsai))); // 粉砕終了担当者
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B006Const.FUNSAIJIKAN, srGlassslurryfunsai))); // 粉砕時間
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B006Const.ZAIRYOHINMEI, srGlassslurryfunsai))); // 材料品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B006Const.BUZAIZAIKONO, srGlassslurryfunsai))); // 部材在庫No
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B006Const.TYOUGOURYOU, srGlassslurryfunsai))); // 調合量
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B006Const.FUUTAIJYUURYOU, srGlassslurryfunsai))); // 風袋重量
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B006Const.SOUJYUURYOU, srGlassslurryfunsai))); // 総重量
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B006Const.SYOUMIJYUURYOU, srGlassslurryfunsai))); // 正味重量
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B006Const.BUDOMARI, srGlassslurryfunsai))); // 歩留まり
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B006Const.HAISYUTUTANTOUSYA, srGlassslurryfunsai))); // 排出担当者
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B006Const.HOKANKAISI_DAY, srGlassslurryfunsai),
                "".equals(hokankaisinichijiTime) ? "0000" : hokankaisinichijiTime)); // 保管開始日時
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B006Const.HOKANBASYO, srGlassslurryfunsai))); // 保管場所
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B006Const.HOKANKAITENGOUKI, srGlassslurryfunsai))); // 保管回転台号機
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B006Const.KAITENSUU, srGlassslurryfunsai))); // 回転数
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B006Const.HOKANTANTOSYA, srGlassslurryfunsai))); // 保管担当者
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B006Const.BIKOU1, srGlassslurryfunsai))); // 備考1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B006Const.BIKOU2, srGlassslurryfunsai))); // 備考2

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
     * ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕(sr_glassslurryfunsai)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSrGlassslurryfunsai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = " DELETE FROM sr_glassslurryfunsai "
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
     * [ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "     FROM tmp_sr_glassslurryfunsai "
                + "    WHERE kojyo = ? AND lotno = ? AND edaban = ? ";
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
     * @param fxhdd11RevInfo 原材料品質DB登録実績データ
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
            // 原材料品質DB登録実績データが取得出来ていない場合エラー
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
     * @param srGlassslurryfunsai ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕データ
     * @return DB値
     */
    private String getSrGlassslurryfunsaiItemData(String itemId, SrGlassslurryfunsai srGlassslurryfunsai) {
        switch (itemId) {
            // ｶﾞﾗｽｽﾗﾘｰ品名
            case GXHDO102B006Const.GLASSSLURRYHINMEI:
                return StringUtil.nullToBlank(srGlassslurryfunsai.getGlassslurryhinmei());

            // ｶﾞﾗｽｽﾗﾘｰLotNo
            case GXHDO102B006Const.GLASSSLURRYLOTNO:
                return StringUtil.nullToBlank(srGlassslurryfunsai.getGlassslurrylotno());

            // ﾛｯﾄ区分
            case GXHDO102B006Const.LOTKUBUN:
                return StringUtil.nullToBlank(srGlassslurryfunsai.getLotkubun());

            // 周速
            case GXHDO102B006Const.SYUUSOKU:
                return StringUtil.nullToBlank(srGlassslurryfunsai.getSyuusoku());

            // 粉砕回転台号機
            case GXHDO102B006Const.KAITENDAIGOUKI:
                return StringUtil.nullToBlank(srGlassslurryfunsai.getKaitendaigouki());

            // 粉砕開始日
            case GXHDO102B006Const.FUNSAIKAISI_DAY:
                return DateUtil.formattedTimestamp(srGlassslurryfunsai.getFunsaikaisinichiji(), "yyMMdd");

            // 粉砕開始時間
            case GXHDO102B006Const.FUNSAIKAISI_TIME:
                return DateUtil.formattedTimestamp(srGlassslurryfunsai.getFunsaikaisinichiji(), "HHmm");

            // 粉砕終了予定日
            case GXHDO102B006Const.FUNSAIYOTEISYUURYOU_DAY:
                return DateUtil.formattedTimestamp(srGlassslurryfunsai.getFunsaiyoteisyuuryounichiji(), "yyMMdd");

            // 粉砕終了予定時間
            case GXHDO102B006Const.FUNSAIYOTEISYUURYOU_TIME:
                return DateUtil.formattedTimestamp(srGlassslurryfunsai.getFunsaiyoteisyuuryounichiji(), "HHmm");

            // 粉砕開始担当者
            case GXHDO102B006Const.KAISITANTOSYA:
                return StringUtil.nullToBlank(srGlassslurryfunsai.getKaisitantosya());

            // 粉砕終了日
            case GXHDO102B006Const.FUNSAISYUURYOU_DAY:
                return DateUtil.formattedTimestamp(srGlassslurryfunsai.getFunsaisyuuryounichiji(), "yyMMdd");

            // 粉砕終了時間
            case GXHDO102B006Const.FUNSAISYUURYOU_TIME:
                return DateUtil.formattedTimestamp(srGlassslurryfunsai.getFunsaisyuuryounichiji(), "HHmm");

            // 粉砕終了担当者
            case GXHDO102B006Const.SYURYOTANTOSYA:
                return StringUtil.nullToBlank(srGlassslurryfunsai.getSyuryotantosya());

            // 粉砕時間
            case GXHDO102B006Const.FUNSAIJIKAN:
                return StringUtil.nullToBlank(srGlassslurryfunsai.getFunsaijikan());

            // 材料品名
            case GXHDO102B006Const.ZAIRYOHINMEI:
                return StringUtil.nullToBlank(srGlassslurryfunsai.getZairyohinmei());

            // 部材在庫No
            case GXHDO102B006Const.BUZAIZAIKONO:
                return StringUtil.nullToBlank(srGlassslurryfunsai.getBuzaizaikono());

            // 調合量
            case GXHDO102B006Const.TYOUGOURYOU:
                return StringUtil.nullToBlank(srGlassslurryfunsai.getTyougouryou());

            // 風袋重量
            case GXHDO102B006Const.FUUTAIJYUURYOU:
                return StringUtil.nullToBlank(srGlassslurryfunsai.getFuutaijyuuryou());

            // 総重量
            case GXHDO102B006Const.SOUJYUURYOU:
                return StringUtil.nullToBlank(srGlassslurryfunsai.getSoujyuuryou());

            // 正味重量
            case GXHDO102B006Const.SYOUMIJYUURYOU:
                return StringUtil.nullToBlank(srGlassslurryfunsai.getSyoumijyuuryou());

            // 歩留まり
            case GXHDO102B006Const.BUDOMARI:
                return StringUtil.nullToBlank(srGlassslurryfunsai.getBudomari());

            // 排出担当者
            case GXHDO102B006Const.HAISYUTUTANTOUSYA:
                return StringUtil.nullToBlank(srGlassslurryfunsai.getHaisyututantousya());

            // 保管場所
            case GXHDO102B006Const.HOKANBASYO:
                return StringUtil.nullToBlank(srGlassslurryfunsai.getHokanbasyo());

            // 保管回転台号機
            case GXHDO102B006Const.HOKANKAITENGOUKI:
                return StringUtil.nullToBlank(srGlassslurryfunsai.getHokankaitengouki());

            // 回転数
            case GXHDO102B006Const.KAITENSUU:
                return StringUtil.nullToBlank(srGlassslurryfunsai.getKaitensuu());

            // 保管開始日
            case GXHDO102B006Const.HOKANKAISI_DAY:
                return DateUtil.formattedTimestamp(srGlassslurryfunsai.getHokankaisinichiji(), "yyMMdd");

            // 保管開始時間
            case GXHDO102B006Const.HOKANKAISI_TIME:
                return DateUtil.formattedTimestamp(srGlassslurryfunsai.getHokankaisinichiji(), "HHmm");

            // 保管担当者
            case GXHDO102B006Const.HOKANTANTOSYA:
                return StringUtil.nullToBlank(srGlassslurryfunsai.getHokantantosya());

            // 備考1
            case GXHDO102B006Const.BIKOU1:
                return StringUtil.nullToBlank(srGlassslurryfunsai.getBikou1());

            // 備考2
            case GXHDO102B006Const.BIKOU2:
                return StringUtil.nullToBlank(srGlassslurryfunsai.getBikou2());

            default:
                return null;
        }
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕_仮登録(tmp_sr_glassslurryfunsai)登録処理(削除時)
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
    private void insertDeleteDataTmpSrGlassslurryfunsai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime) throws SQLException {

        String sql = " INSERT INTO tmp_sr_glassslurryfunsai ( "
                + " kojyo,lotno,edaban,glassslurryhinmei,glassslurrylotno,lotkubun,syuusoku,kaitendaigouki,funsaikaisinichiji, "
                + " funsaiyoteisyuuryounichiji,kaisitantosya,funsaisyuuryounichiji,syuryotantosya,funsaijikan,zairyohinmei, "
                + " buzaizaikono,tyougouryou,fuutaijyuuryou,soujyuuryou,syoumijyuuryou,budomari,haisyututantousya,hokankaisinichiji, "
                + " hokanbasyo,hokankaitengouki,kaitensuu,hokantantosya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") SELECT "
                + " kojyo,lotno,edaban,glassslurryhinmei,glassslurrylotno,lotkubun,syuusoku,kaitendaigouki,funsaikaisinichiji, "
                + " funsaiyoteisyuuryounichiji,kaisitantosya,funsaisyuuryounichiji,syuryotantosya,funsaijikan,zairyohinmei, "
                + " buzaizaikono,tyougouryou,fuutaijyuuryou,soujyuuryou,syoumijyuuryou,budomari,haisyututantousya,hokankaisinichiji, "
                + " hokanbasyo,hokankaitengouki,kaitensuu,hokantantosya,bikou1,bikou2,?,?,?,? "
                + " FROM sr_glassslurryfunsai "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? ";

        List<Object> params = new ArrayList<>();
        // 更新値
        params.add(systemTime); //登録日時
        params.add(systemTime); //更新日時
        params.add(newRev);     //revision
        params.add(deleteflag); //削除ﾌﾗｸﾞ

        // 検索値
        params.add(kojyo);      //工場ｺｰﾄﾞ
        params.add(lotNo);      //ﾛｯﾄNo
        params.add(edaban);     //枝番

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
}