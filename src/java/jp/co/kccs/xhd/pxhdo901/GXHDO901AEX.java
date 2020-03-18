/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo901;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.db.model.FXHDM05;
import static jp.co.kccs.xhd.pxhdo901.GXHDO901A.LOGGER;
import jp.co.kccs.xhd.util.DBUtil;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.ValidateUtil;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.primefaces.component.datalist.DataList;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/12/05<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO901AEX(品質DB画面共通(入力画面拡張))
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2019/12/05
 */
public class GXHDO901AEX extends GXHDO901A {

    /**
     * 画面ID(拡張処理の複数IDの保持)
     */
    private String[] formIds;

    /**
     * 画面ID(拡張処理の複数IDの保持)
     *
     * @return the formIds
     */
    public String[] getFormIds() {
        return formIds;
    }

    /**
     * 画面ID(拡張処理の複数IDの保持)
     *
     * @param formIds the formIds to set
     */
    public void setFormIds(String[] formIds) {
        this.formIds = formIds;
    }

    /**
     * 項目定義情報取得(拡張)
     *
     * @param formId 画面ID
     * @param callerFormId 画面ID(呼出し元)
     * @return データ取得判定(true:データ取得有り、false：データ取得無し)
     */
    @Override
    protected boolean loadItemSettings(String formId, String callerFormId) {
        if (this.formIds != null && 0 < this.formIds.length) {
            return loadItemSettings(this.formIds, callerFormId);
        } else {
            return super.loadItemSettings(formId, callerFormId);
        }
    }

    /**
     * チェック処理情報取得(拡張)
     *
     * @param formId 画面ID
     */
    @Override
    protected void loadCheckList(String formId) {
        if (this.formIds != null && 0 < this.formIds.length) {
            loadCheckList(this.formIds);
        } else {
            super.loadCheckList(formId);
        }
    }

    /**
     * ボタンパラメータ情報取得(拡張)
     *
     * @param formId 画面ID
     * @return データ取得判定(true:データ取得有り、false：データ取得無し)
     */
    @Override
    protected boolean loadButtonSettings(String formId) {
        if (this.formIds != null && 0 < this.formIds.length) {
            return super.loadButtonSettings(this.formIds[0]);
        } else {
            return super.loadButtonSettings(formId);
        }
    }

    /**
     * 背景色をデフォルトの背景色に戻す(拡張)
     *
     * @param buttonId ボタンID
     */
    @Override
    protected void clearItemListBackColor(String buttonId) {
        // 背景色を戻さない特定の処理を除き背景色をデフォルトの背景色に戻す。
        if (this.noCheckButtonId == null || !this.noCheckButtonId.contains(buttonId)) {
            for (FXHDD01 fxhdd01 : this.itemList) {
                fxhdd01.setBackColorInput(fxhdd01.getBackColorInputDefault());
            }
            for (FXHDD01 fxhdd01 : this.itemListEx) {
                fxhdd01.setBackColorInput(fxhdd01.getBackColorInputDefault());
            }
        }
    }

    /**
     * 共通チェック(拡張)
     *
     * @param buttonId ボタンID
     * @return エラーメッセージ
     */
    @Override
    protected ErrorMessageInfo getCheckResult(String buttonId) {

        // リビジョンチェック
        ErrorMessageInfo checkRevErrorMessage = checkRevision(buttonId);
        if (checkRevErrorMessage != null) {
            return checkRevErrorMessage;
        }

        //共通ﾁｪｯｸ
        List<FXHDM05> itemRowCheckList
                = this.checkListHDM05.stream().filter(n -> buttonId.equals(n.getButtonId())).collect(Collectors.toList());

        ValidateUtil validateUtil = new ValidateUtil();
        QueryRunner queryRunnerWip = new QueryRunner(dataSourceWip);
        ErrorMessageInfo requireCheckErrorMessage = validateUtil.executeValidation(itemRowCheckList, this.itemList, queryRunnerWip);
        if (requireCheckErrorMessage == null) {
            requireCheckErrorMessage = validateUtil.executeValidation(itemRowCheckList, this.itemListEx, queryRunnerWip);
            if (requireCheckErrorMessage != null) {
                //this.itemListExの場合はページ遷移は無いので強制的に0を指定
                requireCheckErrorMessage.setPageChangeItemIndex(0);
            }
        }

        return requireCheckErrorMessage;
    }

    /**
     * ページ選択処理(拡張)
     *
     * @param itemIndex 表示項目のインデックス
     */
    @Override
    protected void setPageItemDataList(int itemIndex) {

        // Indexが0未満の場合はリターン
        if (itemIndex <= 0) {
            return;
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();

        DataList itemDataList
                = (DataList) facesContext.getViewRoot().findComponent(":form:tabMain:itemDataList");

        // 項目インデックス
        BigDecimal decItemIndex = BigDecimal.valueOf(itemIndex);
        // 一覧の表示件数
        BigDecimal decHyojiKensu = new BigDecimal(this.hyojiKensu);
        // ページ数(インデックス / 表示件数)の切り上げ
        BigDecimal decPage = decItemIndex.divide(decHyojiKensu, RoundingMode.UP);
        // 開始インデックス = 表示件数 * (ページ数 - 1)
        BigDecimal startIdx = decHyojiKensu.multiply(decPage.subtract(BigDecimal.ONE));
        itemDataList.setFirst(startIdx.intValue());

    }

    /**
     * 項目定義情報取得(拡張)
     *
     * @param formIds 画面ID
     * @param callerFormId 画面ID(呼出し元)
     * @return データ取得判定(true:データ取得有り、false：データ取得無し)
     */
    private boolean loadItemSettings(String[] formIds, String callerFormId) {
        boolean result = false;
        // ユーザーグループ取得
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        List<String> userGrpList = (List<String>) session.getAttribute("login_user_group");

        try {

            QueryRunner queryRunner = new QueryRunner(dataSourceDocServer);

            String inputItemInfo;
            if (FORM_ID_LOT_CORD_SHOKAI.equals(callerFormId)) {
                // ロットカード照会から遷移した場合、各項目はラベル表示とする。
                inputItemInfo = "'black' AS font_color_input, null AS bg_color_input, "
                        + "'false' AS render_iput_text, "
                        + "'false' AS render_iput_number, "
                        + "'false' AS render_iput_date, "
                        + "'false' AS render_iput_select, "
                        + "'false' AS render_iput_radio, "
                        + "'false' AS render_iput_time, "
                        + "'false' AS render_iput_checkbox, "
                        + "'true' AS render_output_label, ";
            } else {
                inputItemInfo = "hdm02_3.font_color AS font_color_input, hdm02_3.bg_color AS bg_color_input, "
                        + "CASE WHEN hdd01.input_setting IS NULL OR hdd01.input_item_mold != '1' THEN 'false' ELSE 'true' END AS render_iput_text, "
                        + "CASE WHEN hdd01.input_setting IS NULL OR hdd01.input_item_mold != '2' THEN 'false' ELSE 'true' END AS render_iput_number, "
                        + "CASE WHEN hdd01.input_setting IS NULL OR hdd01.input_item_mold != '3' THEN 'false' ELSE 'true' END AS render_iput_date, "
                        + "CASE WHEN hdd01.input_setting IS NULL OR hdd01.input_item_mold != '4' THEN 'false' ELSE 'true' END AS render_iput_select, "
                        + "CASE WHEN hdd01.input_setting IS NULL OR hdd01.input_item_mold != '5' THEN 'false' ELSE 'true' END AS render_iput_radio, "
                        + "CASE WHEN hdd01.input_setting IS NULL OR hdd01.input_item_mold != '6' THEN 'false' ELSE 'true' END AS render_iput_time, "
                        + "CASE WHEN hdd01.input_setting IS NULL OR hdd01.input_item_mold != '8' THEN 'false' ELSE 'true' END AS render_iput_checkbox, "
                        + "CASE WHEN hdd01.input_setting IS NULL OR hdd01.input_item_mold != '7' THEN 'false' ELSE 'true' END AS render_output_label, ";
            }

            String sql = "SELECT A.*, row_number() over() AS item_index FROM ("
                    + "SELECT hdd01.gamen_id, hdd01.item_id, hdd01.item_no, hdd01.input_item_mold, hdd01.label1, hdd01.label2, "
                    + "hdd01.input_list, hdd01.input_default, hdd01.input_length, hdd01.input_length_dec, "
                    + "hdm02_1.font_size AS font_size_1, hdm02_1.font_color AS font_color_1, hdm02_1.bg_color AS bg_color_1, "
                    + "hcm02_4.font_size AS font_size_3, hcm02_4.font_color AS font_color_3, hcm02_4.bg_color AS bg_color_3, "
                    + "CASE WHEN hdd01.label1_setting IS NULL OR hdd01.link_button_id IS NOT NULL THEN 'false' ELSE 'true' END AS render_1, "
                    + "hdm02_2.font_size AS font_size_2, hdm02_2.font_color AS font_color_2, hdm02_2.bg_color AS bg_color_2, "
                    + "CASE WHEN hdd01.label2_setting IS NULL THEN 'false' ELSE 'true' END AS render_2, "
                    + "CASE WHEN hdd01.link_button_id IS NULL THEN 'false' ELSE 'true' END AS render_link_button,"
                    + "hdm02_3.font_size AS font_size_input, "
                    + inputItemInfo
                    + "hdd01.joken_kotei_mei,hdd01.joken_komoku_mei,hdd01.joken_kanri_komoku,hdd01.standard_pattern, "
                    + "hdd01.link_button_id, "
                    + "hdm02_3.bg_color AS bg_color_input_default "
                    + "FROM fxhdd01 hdd01 "
                    + "LEFT JOIN fxhdm02 hdm02_1 ON (hdd01.label1_setting = hdm02_1.setting_id) "
                    + "LEFT JOIN fxhdm02 hdm02_2 ON (hdd01.label2_setting = hdm02_2.setting_id) "
                    + "LEFT JOIN fxhdm02 hdm02_3 ON (hdd01.input_setting = hdm02_3.setting_id) "
                    + "LEFT JOIN fxhdm02 hcm02_4 ON (hdd01.standard_setting = hcm02_4.setting_id) "
                    + "WHERE "
                    + DBUtil.getInConditionPreparedStatement("hdd01.gamen_id", formIds.length) + " "
                    + " AND (hdd01.item_authority IS NULL OR "
                    + DBUtil.getInConditionPreparedStatement("hdd01.item_authority", userGrpList.size()) + ") "
                    + "ORDER BY hdd01.gamen_id, hdd01.item_no "
                    + ") A ";

            List<Object> params = new ArrayList<>();
            params.addAll(Arrays.asList(formIds));
            params.addAll(userGrpList);

            Map<String, String> mapping = new HashMap<>();
            mapping.put("gamen_id", "gamenId");
            mapping.put("item_id", "itemId");
            mapping.put("item_no", "itemNo");
            mapping.put("input_item_mold", "inputItemType");
            mapping.put("label1", "label1");
            mapping.put("label2", "label2");
            mapping.put("input_list", "inputList");
            mapping.put("input_default", "inputDefault");
            mapping.put("input_length", "inputLength");
            mapping.put("input_length_dec", "inputLengthDec");
            mapping.put("font_size_1", "fontSize1");
            mapping.put("font_color_1", "fontColor1");
            mapping.put("bg_color_1", "backColor1");
            mapping.put("render_1", "render1");
            mapping.put("font_size_2", "fontSize2");
            mapping.put("font_color_2", "fontColor2");
            mapping.put("bg_color_2", "backColor2");
            mapping.put("render_2", "render2");
            mapping.put("render_link_button", "renderLinkButton");
            mapping.put("font_size_input", "fontSizeInput");
            mapping.put("font_color_input", "fontColorInput");
            mapping.put("bg_color_input", "backColorInput");
            mapping.put("render_iput_text", "renderInputText");
            mapping.put("render_iput_number", "renderInputNumber");
            mapping.put("render_iput_date", "renderInputDate");
            mapping.put("render_iput_select", "renderInputSelect");
            mapping.put("render_iput_radio", "renderInputRadio");
            mapping.put("render_iput_time", "renderInputTime");
            mapping.put("render_iput_checkbox", "renderInputCheckBox");
            mapping.put("render_output_label", "renderOutputLabel");
            mapping.put("font_size_3", "fontSize3");
            mapping.put("font_color_3", "fontColor3");
            mapping.put("bg_color_3", "backColor3");
            mapping.put("joken_kotei_mei", "jokenKoteiMei");
            mapping.put("joken_komoku_mei", "jokenKomokuMei");
            mapping.put("joken_kanri_komoku", "jokenKanriKomoku");
            mapping.put("standard_pattern", "standardPattern");
            mapping.put("link_button_id", "linkButtonId");
            mapping.put("bg_color_input_default", "backColorInputDefault");
            mapping.put("item_index", "itemIndex");

            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<FXHDD01>> beanHandler = new BeanListHandler<>(FXHDD01.class, rowProcessor);

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            List<FXHDD01> list = queryRunner.query(sql, beanHandler, params.toArray());

            // メインデータ(通常入力画面データ)
            this.itemListEx = list.stream().filter(n -> !n.getGamenId().equals(formIds[0])).collect(Collectors.toList());
            // 拡張画面データ
            this.itemList = list.stream().filter(n -> n.getGamenId().equals(formIds[0])).collect(Collectors.toList());
            if (this.itemList != null && !this.itemList.isEmpty()) {
                result = true;
            }

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("項目情報取得失敗", ex, LOGGER);
        }
        return result;
    }

    /**
     * チェック処理情報取得
     *
     * @param formIds 画面ID
     */
    private void loadCheckList(String[] formIds) {

        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceDocServer);
            String sql = "SELECT gamen_id,button_id,item_id,check_pattern,check_no "
                    + "  FROM FXHDM05 "
                    + " WHERE "
                    + DBUtil.getInConditionPreparedStatement("gamen_id", formIds.length) + " "
                    + " ORDER BY gamen_id,button_id,item_id,check_no ";

            List<Object> params = new ArrayList<>();
            params.addAll(Arrays.asList(formIds));

            Map<String, String> mapping = new HashMap<>();
            mapping.put("gamen_id", "gamenId");
            mapping.put("button_id", "buttonId");
            mapping.put("item_id", "itemId");
            mapping.put("check_pattern", "checkPattern");
            mapping.put("check_no", "checkNo");

            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<FXHDM05>> beanHandler = new BeanListHandler<>(FXHDM05.class, rowProcessor);

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            this.checkListHDM05 = queryRunner.query(sql, beanHandler, params.toArray());

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("チェック処理項目取得失敗", ex, LOGGER);
        }
    }
}
