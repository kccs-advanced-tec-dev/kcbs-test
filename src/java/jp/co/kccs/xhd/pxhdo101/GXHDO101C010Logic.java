/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import jp.co.kccs.xhd.model.GXHDO101C010Model;
import jp.co.kccs.xhd.util.DBUtil;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.StringUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/01/08<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C010(被り量（μｍ）)ロジッククラス
 */
public class GXHDO101C010Logic {

    /**
     * 通常カラーコード(テキスト)
     */
    public static final String DEFAULT_BACK_COLOR = "#E0FFFF";

    public static GXHDO101C010Model createGXHDO101C010Model(String[] kaburiryou) {
        GXHDO101C010Model model = new GXHDO101C010Model();
        // 被り量（μｍ）1
        model.setKaburiryouData1(getInitKaburiryoData(model, kaburiryou[0], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "1"));
        // 被り量（μｍ）2
        model.setKaburiryouData2(getInitKaburiryoData(model, kaburiryou[1], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "2"));
        // 被り量（μｍ）3
        model.setKaburiryouData3(getInitKaburiryoData(model, kaburiryou[2], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "3"));
        // 被り量（μｍ）4
        model.setKaburiryouData4(getInitKaburiryoData(model, kaburiryou[3], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "4"));
        // 被り量（μｍ）5
        model.setKaburiryouData5(getInitKaburiryoData(model, kaburiryou[4], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "5"));
        // 被り量（μｍ）6
        model.setKaburiryouData6(getInitKaburiryoData(model, kaburiryou[5], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "6"));
        // 被り量（μｍ）7
        model.setKaburiryouData7(getInitKaburiryoData(model, kaburiryou[6], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "7"));
        // 被り量（μｍ）8
        model.setKaburiryouData8(getInitKaburiryoData(model, kaburiryou[7], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "8"));
        // 被り量（μｍ）9
        model.setKaburiryouData9(getInitKaburiryoData(model, kaburiryou[8], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "9"));
        // 被り量（μｍ）10
        model.setKaburiryouData10(getInitKaburiryoData(model, kaburiryou[9], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "10"));
        // 被り量（μｍ）11
        model.setKaburiryouData11(getInitKaburiryoData(model, kaburiryou[10], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "11"));
        // 被り量（μｍ）12
        model.setKaburiryouData12(getInitKaburiryoData(model, kaburiryou[11], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "12"));
        // 被り量（μｍ）13
        model.setKaburiryouData13(getInitKaburiryoData(model, kaburiryou[12], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "13"));
        // 被り量（μｍ）14
        model.setKaburiryouData14(getInitKaburiryoData(model, kaburiryou[13], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "14"));
        // 被り量（μｍ）15
        model.setKaburiryouData15(getInitKaburiryoData(model, kaburiryou[14], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "15"));
        // 被り量（μｍ）16
        model.setKaburiryouData16(getInitKaburiryoData(model, kaburiryou[15], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "16"));
        // 被り量（μｍ）17
        model.setKaburiryouData17(getInitKaburiryoData(model, kaburiryou[16], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "17"));
        // 被り量（μｍ）18
        model.setKaburiryouData18(getInitKaburiryoData(model, kaburiryou[17], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "18"));
        // 被り量（μｍ）19
        model.setKaburiryouData19(getInitKaburiryoData(model, kaburiryou[18], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "19"));
        // 被り量（μｍ）20
        model.setKaburiryouData20(getInitKaburiryoData(model, kaburiryou[19], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "20"));
        return model;
    }

    /**
     * 被り量（μｍ）データ初期化データ取得
     *
     * @param GXHDO101C010Model 被り量（μｍ）画面モデル
     * @param printWidth 被り量（μｍ）
     * @param startVal スタート項目(値)
     * @param startInputType　スタート項目(入力タイプ)
     * @param startTextMaxLength　スタート項目(テキストMaxLength)
     * @param startTextDecimalPlaces　スタート項目(テキストDecimalPlaces)
     * @param startTextMaxValue　スタート項目(テキストMaxValue)
     * @param startTextMinValue　スタート項目(テキストMinValue)
     * @param startTextBackColor　スタート項目(BackGround)
     * @param startTabIndex　スタート項目(TabIndex)
     * @return 被り量（μｍ）データ
     */
    private static GXHDO101C010Model.KaburiryouData getInitKaburiryoData(GXHDO101C010Model GXHDO101C010Model,
            String startVal, String startInputType, String startTextMaxLength, String startTextDecimalPlaces, String startTextMaxValue,
            String startTextMinValue, String startTextBackColor, String startTabIndex) {
        GXHDO101C010Model.KaburiryouData printWidthListData = GXHDO101C010Model.new KaburiryouData();
        // スタート
        printWidthListData.setValue(startVal);
        if ("TEXT".equals(startInputType)) {
            printWidthListData.setTextRendered(true);
            printWidthListData.setLabelRendered(false);
        } else {
            printWidthListData.setTextRendered(false);
            printWidthListData.setLabelRendered(true);
        }
        printWidthListData.setTextDecimalPlaces(startTextDecimalPlaces);
        printWidthListData.setTextMaxValue(startTextMaxValue);
        printWidthListData.setTextMinValue(startTextMinValue);
        printWidthListData.setTextMaxLength(startTextMaxLength);
        printWidthListData.setTextBackColor(startTextBackColor);
        printWidthListData.setTabIndex(startTabIndex);

        return printWidthListData;
    }

    /**
     * 入力チェック
     *
     * @param gXHDO101C010Model 被り量(µm)入力サブ画面用モデル
     * @return エラーリスト
     */
    public static List<String> checkInput(GXHDO101C010Model gXHDO101C010Model) {

        List<String> errorList = new ArrayList<>();

        // 1～20にデータが入力されているかどうか確認
        if (StringUtil.isEmpty(gXHDO101C010Model.getKaburiryouData1().getValue())) {
            errorList.add(MessageUtil.getMessage("XHD-000037", "被り量(µm)1"));
            gXHDO101C010Model.getKaburiryouData1().setTextBackColor(ErrUtil.ERR_BACK_COLOR);
            return errorList;
        }

        if (StringUtil.isEmpty(gXHDO101C010Model.getKaburiryouData2().getValue())) {
            errorList.add(MessageUtil.getMessage("XHD-000037", "被り量(µm)2"));
            gXHDO101C010Model.getKaburiryouData2().setTextBackColor(ErrUtil.ERR_BACK_COLOR);
            return errorList;
        }

        if (StringUtil.isEmpty(gXHDO101C010Model.getKaburiryouData3().getValue())) {
            errorList.add(MessageUtil.getMessage("XHD-000037", "被り量(µm)3"));
            gXHDO101C010Model.getKaburiryouData3().setTextBackColor(ErrUtil.ERR_BACK_COLOR);
            return errorList;
        }

        if (StringUtil.isEmpty(gXHDO101C010Model.getKaburiryouData4().getValue())) {
            errorList.add(MessageUtil.getMessage("XHD-000037", "被り量(µm)4"));
            gXHDO101C010Model.getKaburiryouData4().setTextBackColor(ErrUtil.ERR_BACK_COLOR);
            return errorList;
        }

        if (StringUtil.isEmpty(gXHDO101C010Model.getKaburiryouData5().getValue())) {
            errorList.add(MessageUtil.getMessage("XHD-000037", "被り量(µm)5"));
            gXHDO101C010Model.getKaburiryouData5().setTextBackColor(ErrUtil.ERR_BACK_COLOR);
            return errorList;
        }

        if (StringUtil.isEmpty(gXHDO101C010Model.getKaburiryouData6().getValue())) {
            errorList.add(MessageUtil.getMessage("XHD-000037", "被り量(µm)6"));
            gXHDO101C010Model.getKaburiryouData6().setTextBackColor(ErrUtil.ERR_BACK_COLOR);
            return errorList;
        }

        if (StringUtil.isEmpty(gXHDO101C010Model.getKaburiryouData7().getValue())) {
            errorList.add(MessageUtil.getMessage("XHD-000037", "被り量(µm)7"));
            gXHDO101C010Model.getKaburiryouData7().setTextBackColor(ErrUtil.ERR_BACK_COLOR);
            return errorList;
        }

        if (StringUtil.isEmpty(gXHDO101C010Model.getKaburiryouData8().getValue())) {
            errorList.add(MessageUtil.getMessage("XHD-000037", "被り量(µm)8"));
            gXHDO101C010Model.getKaburiryouData8().setTextBackColor(ErrUtil.ERR_BACK_COLOR);
            return errorList;
        }

        if (StringUtil.isEmpty(gXHDO101C010Model.getKaburiryouData9().getValue())) {
            errorList.add(MessageUtil.getMessage("XHD-000037", "被り量(µm)9"));
            gXHDO101C010Model.getKaburiryouData9().setTextBackColor(ErrUtil.ERR_BACK_COLOR);
            return errorList;
        }

        if (StringUtil.isEmpty(gXHDO101C010Model.getKaburiryouData10().getValue())) {
            errorList.add(MessageUtil.getMessage("XHD-000037", "被り量(µm)10"));
            gXHDO101C010Model.getKaburiryouData10().setTextBackColor(ErrUtil.ERR_BACK_COLOR);
            return errorList;
        }

        if (StringUtil.isEmpty(gXHDO101C010Model.getKaburiryouData11().getValue())) {
            errorList.add(MessageUtil.getMessage("XHD-000037", "被り量(µm)11"));
            gXHDO101C010Model.getKaburiryouData11().setTextBackColor(ErrUtil.ERR_BACK_COLOR);
            return errorList;
        }

        if (StringUtil.isEmpty(gXHDO101C010Model.getKaburiryouData12().getValue())) {
            errorList.add(MessageUtil.getMessage("XHD-000037", "被り量(µm)12"));
            gXHDO101C010Model.getKaburiryouData12().setTextBackColor(ErrUtil.ERR_BACK_COLOR);
            return errorList;
        }

        if (StringUtil.isEmpty(gXHDO101C010Model.getKaburiryouData13().getValue())) {
            errorList.add(MessageUtil.getMessage("XHD-000037", "被り量(µm)13"));
            gXHDO101C010Model.getKaburiryouData13().setTextBackColor(ErrUtil.ERR_BACK_COLOR);
            return errorList;
        }

        if (StringUtil.isEmpty(gXHDO101C010Model.getKaburiryouData14().getValue())) {
            errorList.add(MessageUtil.getMessage("XHD-000037", "被り量(µm)14"));
            gXHDO101C010Model.getKaburiryouData14().setTextBackColor(ErrUtil.ERR_BACK_COLOR);
            return errorList;
        }

        if (StringUtil.isEmpty(gXHDO101C010Model.getKaburiryouData15().getValue())) {
            errorList.add(MessageUtil.getMessage("XHD-000037", "被り量(µm)15"));
            gXHDO101C010Model.getKaburiryouData15().setTextBackColor(ErrUtil.ERR_BACK_COLOR);
            return errorList;
        }

        if (StringUtil.isEmpty(gXHDO101C010Model.getKaburiryouData16().getValue())) {
            errorList.add(MessageUtil.getMessage("XHD-000037", "被り量(µm)16"));
            gXHDO101C010Model.getKaburiryouData16().setTextBackColor(ErrUtil.ERR_BACK_COLOR);
            return errorList;
        }

        if (StringUtil.isEmpty(gXHDO101C010Model.getKaburiryouData17().getValue())) {
            errorList.add(MessageUtil.getMessage("XHD-000037", "被り量(µm)17"));
            gXHDO101C010Model.getKaburiryouData17().setTextBackColor(ErrUtil.ERR_BACK_COLOR);
            return errorList;
        }

        if (StringUtil.isEmpty(gXHDO101C010Model.getKaburiryouData18().getValue())) {
            errorList.add(MessageUtil.getMessage("XHD-000037", "被り量(µm)18"));
            gXHDO101C010Model.getKaburiryouData18().setTextBackColor(ErrUtil.ERR_BACK_COLOR);
            return errorList;
        }

        if (StringUtil.isEmpty(gXHDO101C010Model.getKaburiryouData19().getValue())) {
            errorList.add(MessageUtil.getMessage("XHD-000037", "被り量(µm)19"));
            gXHDO101C010Model.getKaburiryouData19().setTextBackColor(ErrUtil.ERR_BACK_COLOR);
            return errorList;
        }

        if (StringUtil.isEmpty(gXHDO101C010Model.getKaburiryouData20().getValue())) {
            errorList.add(MessageUtil.getMessage("XHD-000037", "被り量(µm)20"));
            gXHDO101C010Model.getKaburiryouData20().setTextBackColor(ErrUtil.ERR_BACK_COLOR);
            return errorList;
        }

        return errorList;
    }
    
    /**
     * [条件]から初期表示する情報を取得
     *
     * @param logger ロガー
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param sekkeino 設計No
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    public static String[] getKaburiryo(Logger logger, QueryRunner queryRunnerQcdb, String sekkeino) throws SQLException {
        String kaburiryoX = "";
        String kaburiryoY = "";

        // 条件データの取得
        String sql = "SELECT KIKAKUCHI,KANRIKOUMOKU "
                + "FROM da_joken "
                + "WHERE SEKKEINO = ? "
                + "AND KOUTEIMEI = '印刷/積層' "
                + "AND KOUMOKUMEI = '誘電体被り量測定' "
                + "AND KANRIKOUMOKU IN('合わせ5点1', '合わせ5点2')";

        List<Object> params = new ArrayList<>();
        params.add(sekkeino);

        DBUtil.outputSQLLog(sql, params.toArray(), logger);
        List<Map<String, Object>> dataList = queryRunnerQcdb.query(sql, new MapListHandler(), params.toArray());
        for (Map<String, Object> map : dataList) {
            if ("合わせ5点1".equals(map.get("KANRIKOUMOKU"))) {
                kaburiryoX = StringUtil.nullToBlank(map.get("KIKAKUCHI"));
            }
            if ("合わせ5点2".equals(map.get("KANRIKOUMOKU"))) {
                kaburiryoY = StringUtil.nullToBlank(map.get("KIKAKUCHI"));
            }
        }

        return new String[]{kaburiryoX, kaburiryoY};
    }
}
