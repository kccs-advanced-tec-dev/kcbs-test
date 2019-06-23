/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.util;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import jp.co.kccs.xhd.db.model.FXHDD01;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/05/22<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * Commonユーティリティ
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2019/05/24
 */
public class CommonUtil {

    private static final Logger LOGGER = Logger.getLogger(CommonUtil.class.getName());

    /**
     * 良品ｾｯﾄ数の取得
     *
     * @param lotNo ﾛｯﾄNo
     * @param gamenId 画面ID
     * @param queryRunnerDoc QueryRunner(Doc)
     * @param queryRunnerQcdb QueryRunner(QCDB)
     * @param rtnErrorMsgList
     * @return 良品ｾｯﾄ数
     * @throws SQLException 例外エラー
     */
    public static String getMaeKoteiRyouhinsetSu(String lotNo, String gamenId, QueryRunner queryRunnerDoc, QueryRunner queryRunnerQcdb, List<String> rtnErrorMsgList) throws SQLException {

        // 画面IDが指定されていない場合は前工程無しとしてそのままリターン
        if (StringUtil.isEmpty(gamenId)) {
            return "";
        }

        String kojyo = lotNo.substring(0, 3);
        String lotNo8 = lotNo.substring(3, 11);
        String edaban = lotNo.substring(11, 14);
        for (int i = 0; i < 5; i++) {
            Map fxhdd03 = loadFxhdd03(queryRunnerDoc, kojyo, lotNo8, edaban, gamenId);
            if (fxhdd03 == null || fxhdd03.isEmpty()) {
                String msg = MessageUtil.getMessage("XHD-000051");
                rtnErrorMsgList.add(msg);
                return msg;
            }

            // 状態フラグが1(登録済)以外の場合エラー
            if (!"1".equals(StringUtil.nullToBlank(fxhdd03.get("jotai_flg")))) {
                String msg = MessageUtil.getMessage("XHD-000052");
                rtnErrorMsgList.add(msg);
                return msg;
            }

            // 良品ｾｯﾄ数を取得
            String ryohinSetsu = getRyohinsetsuTargetGamenId(queryRunnerQcdb, gamenId, kojyo, lotNo8, edaban, StringUtil.nullToBlank(fxhdd03.get("rev")));

            // 戻り値が"nodata"以外の場合、値取得完了
            if (!"nodata".equals(ryohinSetsu)) {
                return ryohinSetsu;
            }
        }

        return "";

    }

    

    /**
     * 対象画面のﾃｰﾌﾞﾙ情報を取得(該当データがない場合はNULLを返却)
     *
     * @param queryRunnerQcdb queryRunner
     * @param gamenId 画面ID
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param rev ﾘﾋﾞｼﾞｮﾝ
     * @param jissekino 実績No
     * @return 良品ｾｯﾄ数
     * @throws SQLException
     */
    public static Map getMaeKoteiData(QueryRunner queryRunnerQcdb, String gamenId, String kojyo, String lotNo, String edaban, String rev, int jissekino) throws SQLException {
        switch (gamenId) {
            // 印刷SPSｸﾞﾗﾋﾞｱ
            case "GXHDO101B001":
                return getSrSpsprintGraData(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            // 印刷SPSｽｸﾘｰﾝ
            case "GXHDO101B002":
                return getSrSpsprintData(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            // 印刷RSUS
            case "GXHDO101B003":
                return getSrRsusprnData(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            // 積層・SPS
            case "GXHDO101B004":
                return getSrSpssekisouData(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            // 積層・RSUS
            case "GXHDO101B005":
                return getSrRsussekData(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            // 印刷積層・RHAPS
            case "GXHDO101B006":
                return getSrRhapsData(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            // ﾌﾟﾚｽ・仮ﾌﾟﾚｽ
            case "GXHDO101B007":
                return getSrPrepressData(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            // ﾌﾟﾚｽ・真空脱気
            case "GXHDO101B008":
                return getSrPressData(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            // ﾌﾟﾚｽ・ﾒｶﾌﾟﾚｽ
            case "GXHDO101B009":
                return getSrMekapressData(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            // ｶｯﾄ・押切ｶｯﾄ
            case "GXHDO101B010":
                return getSrHapscutData(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            // ｶｯﾄ・ﾀﾞｲｼﾝｸﾞｶｯﾄ
            case "GXHDO101B011":
                return getSrCutData(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            // 生品質検査
            case "GXHDO101B012":
                return getSrCutcheckData(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            // 焼成・ｾｯﾀ詰め
            case "GXHDO101B013":
                return getSrSayadumeData(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            case "GXHDO101B014":// 焼成・Air脱脂
            case "GXHDO101B015":// 焼成・窒素脱脂
                return getSrYobikanData(queryRunnerQcdb, kojyo, lotNo, edaban, rev, jissekino);
            // 焼成・ﾍﾞﾙﾄ脱脂
            case "GXHDO101B016":
                return getSrNijidasshiData(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            default:
                break;
        }
        return null;
    }
    
    /**
     * 前工程情報設定処理
     * @param itemData 項目データ
     * @param maekoteiInfo 前工程情報
     * @param setDataId データID(前工程情報の項目ID)
     * @param showNoDataMessage データなし時にメッセージを表示するか(表示する場合、true)
     * @param checkZero 値が0の場合データなしとして扱うか(扱う場合、true)
     */
    public static void setMaekoteiInfo(FXHDD01 itemData, Map maekoteiInfo, String setDataId, boolean showNoDataMessage, boolean checkZero) {
        if (itemData == null) {
            return;
        }

        if (existKeyData(maekoteiInfo, setDataId, checkZero)) {
            //値が入っている場合は値をセットする。
            itemData.setValue(StringUtil.nullToBlank(maekoteiInfo.get(setDataId)));
        } else if (showNoDataMessage) {
            itemData.setValue(MessageUtil.getMessage("XHD-000051"));
            itemData.setFontColorInput("Red");
            itemData.setLabel2("");
            itemData.setCustomStyleInput("font-weight: bold;");
        }
    }

    /**
     * マップ内に指定のKeyデータが存在するかチェックする。
     * @param mapInfo マップ情報
     * @param key キー
     * @param checkZero 0値チェック
     * @return true:値あり,false:値なし
     */
    public static boolean existKeyData(Map mapInfo, String key, boolean checkZero){
        if(mapInfo == null || mapInfo.get(key) == null){
            return false;
        }
        
        // 値が0かどうかチェックする(0の場合データなしとする。)
        if(checkZero){
            BigDecimal checkValue;
            try{
                checkValue = new BigDecimal(StringUtil.nullToBlank(mapInfo.get(key)));
            }catch(NumberFormatException e){
                checkValue = BigDecimal.ZERO;
            }
            if(BigDecimal.ZERO.compareTo(checkValue) == 0){
                return false;
            }
        }
        
        return true;
    }
    

    /**
     * [品質DB登録実績]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト(Doc)
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param formId 画面ID(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private static Map loadFxhdd03(QueryRunner queryRunnerDoc, String kojyo, String lotNo,
            String edaban, String gamenId) throws SQLException {
        // 設計データの取得
        String sql = "SELECT rev, jotai_flg "
                + "FROM fxhdd03 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND gamen_id = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(gamenId);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerDoc.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * 対象画面の良品セット数を取得
     *
     * @param queryRunnerQcdb queryRunner
     * @param gamenId 画面ID
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param rev ﾘﾋﾞｼﾞｮﾝ
     * @return 良品ｾｯﾄ数
     * @throws SQLException
     */
    private static String getRyohinsetsuTargetGamenId(QueryRunner queryRunnerQcdb, String gamenId, String kojyo, String lotNo, String edaban, String rev) throws SQLException {
        switch (gamenId) {
            // 積層・SPS
            case "GXHDO101B004":
                return getRyohinsetsuSrSpssekisou(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            // 積層・RSUS
            case "GXHDO101B005":
                return getRyohinsetsuSrRsussek(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            // 印刷積層・RHAPS
            case "GXHDO101B006":
                return getRyohinsetsuSrRhaps(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            // ﾌﾟﾚｽ・仮ﾌﾟﾚｽ
            case "GXHDO101B007":
                return getRyohinsetsuSrPrepress(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            // ﾌﾟﾚｽ・真空脱気
            case "GXHDO101B008":
                return getRyohinsetsuSrPress(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            // ﾌﾟﾚｽ・ﾒｶﾌﾟﾚｽ
            case "GXHDO101B009":
                return getRyohinsetsuSrMekapress(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            // ｶｯﾄ・押切ｶｯﾄ
            case "GXHDO101B010":
                return getRyohinsetsuSrHapscut(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            // ｶｯﾄ・ﾀﾞｲｼﾝｸﾞｶｯﾄ
            case "GXHDO101B011":
                return getRyohinsetsuSrCut(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            // 生品質検査
            case "GXHDO101B012":
                return getRyohinsetsuSrCutcheck(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
            // 焼成・ｾｯﾀ詰め
            case "GXHDO101B013":

                //TODO テーブル未定
                break;
            // 焼成・Air脱脂
            case "GXHDO101B014":
                //TODO テーブル未定
                break;
            // 焼成・窒素脱脂
            case "GXHDO101B015":
                //TODO テーブル未定
                break;
            // 焼成・ﾍﾞﾙﾄ脱脂
            case "GXHDO101B016":
                //TODO テーブル未定
                break;
            default:
                break;
        }
        return "";
    }

    /**
     * 積層・SPS工程の良品ｾｯﾄ数を取得する。 ※データ無し時は"nodata"の文字列を返す。
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private static String getRyohinsetsuSrSpssekisou(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        String sql = "SELECT RyouhinSetsuu "
                + "FROM sr_spssekisou "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map data = queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
        if (null == data || data.isEmpty()) {
            return "nodata";
        }
        return StringUtil.nullToBlank(data.get("RyouhinSetsuu"));
    }

    /**
     * 積層・RSUS工程の良品ｾｯﾄ数を取得する。 ※データ無し時は"nodata"の文字列を返す。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 良品ｾｯﾄ数
     * @throws SQLException 例外エラー
     */
    private static String getRyohinsetsuSrRsussek(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        String sql = "SELECT RyouhinSetsuu "
                + "FROM sr_rsussek "
                + "WHERE KOJYO = ? AND LOTNO = ? "
                + "AND EDABAN = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map data = queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
        if (null == data || data.isEmpty()) {
            return "nodata";
        }
        return StringUtil.nullToBlank(data.get("RyouhinSetsuu"));
    }

    /**
     * 印刷積層・RHAPS工程の良品ｾｯﾄ数を取得する。 ※データ無し時は"nodata"の文字列を返す。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 良品ｾｯﾄ数
     * @throws SQLException 例外エラー
     */
    private static String getRyohinsetsuSrRhaps(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        String sql = "SELECT RYOUHINSETSUU "
                + "FROM sr_rhaps "
                + "WHERE KOJYO = ? AND LOTNO = ? "
                + "AND EDABAN = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map data = queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
        if (null == data || data.isEmpty()) {
            return "nodata";
        }
        return StringUtil.nullToBlank(data.get("RYOUHINSETSUU"));
    }

    /**
     * ﾌﾟﾚｽ・仮ﾌﾟﾚｽ工程の良品ｾｯﾄ数を取得する。 ※データ無し時は"nodata"の文字列を返す。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 良品ｾｯﾄ数
     * @throws SQLException 例外エラー
     */
    private static String getRyohinsetsuSrPrepress(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        String sql = "SELECT RYOUHINSETSUU "
                + "FROM sr_prepress "
                + "WHERE KOJYO = ? AND LOTNO = ? "
                + "AND EDABAN = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map data = queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
        if (null == data || data.isEmpty()) {
            return "nodata";
        }
        return StringUtil.nullToBlank(data.get("RYOUHINSETSUU"));
    }

    /**
     * ﾌﾟﾚｽ・真空脱気工程の良品ｾｯﾄ数を取得する。 ※データ無し時は"nodata"の文字列を返す。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 良品ｾｯﾄ数
     * @throws SQLException 例外エラー
     */
    private static String getRyohinsetsuSrPress(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        String sql = "SELECT RyouhinSetsuu "
                + "FROM sr_press "
                + "WHERE KOJYO = ? AND LOTNO = ? "
                + "AND EDABAN = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map data = queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
        if (null == data || data.isEmpty()) {
            return "nodata";
        }
        return StringUtil.nullToBlank(data.get("RyouhinSetsuu"));
    }

    /**
     * ﾌﾟﾚｽ・ﾒｶﾌﾟﾚｽ工程の良品ｾｯﾄ数を取得する。 ※データ無し時は"nodata"の文字列を返す。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 良品ｾｯﾄ数
     * @throws SQLException 例外エラー
     */
    private static String getRyohinsetsuSrMekapress(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        String sql = "SELECT RYOHINSETSUU "
                + "FROM sr_mekapress "
                + "WHERE KOJYO = ? AND LOTNO = ? "
                + "AND EDABAN = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map data = queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
        if (null == data || data.isEmpty()) {
            return "nodata";
        }
        return StringUtil.nullToBlank(data.get("RYOHINSETSUU"));
    }

    /**
     * ｶｯﾄ・押切ｶｯﾄ工程の良品ｾｯﾄ数を取得する。 ※データ無し時は"nodata"の文字列を返す。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 良品ｾｯﾄ数
     * @throws SQLException 例外エラー
     */
    private static String getRyohinsetsuSrHapscut(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        String sql = "SELECT RyouhinSetsuu "
                + "FROM sr_hapscut "
                + "WHERE KOJYO = ? AND LOTNO = ? "
                + "AND EDABAN = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map data = queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
        if (null == data || data.isEmpty()) {
            return "nodata";
        }
        return StringUtil.nullToBlank(data.get("RyouhinSetsuu"));
    }

    /**
     * ｶｯﾄ・ﾀﾞｲｼﾝｸﾞｶｯﾄ工程の良品ｾｯﾄ数を取得する。 ※データ無し時は"nodata"の文字列を返す。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 良品ｾｯﾄ数
     * @throws SQLException 例外エラー
     */
    private static String getRyohinsetsuSrCut(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        String sql = "SELECT RyouhinSetsuu "
                + "FROM sr_cut "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map data = queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
        if (null == data || data.isEmpty()) {
            return "nodata";
        }
        return StringUtil.nullToBlank(data.get("RyouhinSetsuu"));
    }

    /**
     * 生品質検査工程の良品ｾｯﾄ数を取得する。 ※データ無し時は"nodata"の文字列を返す。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 良品ｾｯﾄ数
     * @throws SQLException 例外エラー
     */
    private static String getRyohinsetsuSrCutcheck(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        String sql = "SELECT RyouhinSetsuu "
                + "FROM sr_cutcheck "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map data = queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
        if (null == data || data.isEmpty()) {
            return "nodata";
        }
        return StringUtil.nullToBlank(data.get("RyouhinSetsuu"));
    }

        /**
     * 印刷・SPSｸﾞﾗﾋﾞｱ工程のデータを取得する
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    private static Map getSrSpsprintGraData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT kojyo,lotno,edaban,tapelotno,petfilmsyurui,taperollno1,taperollno2,taperollno3,pastelotno,pastenendo,"
                + "pasteondo,pkokeibun1,pastelotno2,pastenendo2,pasteondo2,pkokeibun2,handoumei,handouno,handoumaisuu,bladeno,"
                + "bladegaikan,BladeATu,AtudoNo,AtudoMaisuu,AtuDoATu,gouki,kansouondo,kansouondo2,kansouondo3,kansouondo4,kansouondo5,"
                + "hansouspeed,startdatetime,tantousya,makuatuave_start,makuatumax_start,makuatumin_start,makuatucv_start,nijimikasure_start,"
                + "start_ptn_dist_x,start_ptn_dist_y,TensionS_sum,TensionStemae,TensionSoku,enddatetime,tanto_end,printmaisuu,makuatuave_end,"
                + "makuatumax_end,makuatumin_end,makuatucv_end,nijimikasure_end,end_ptn_dist_x,end_ptn_dist_y,TensionE_sum,TensionEtemae,"
                + "TensionEoku,printzure1_surihajime_start,printzure2_center_start,printzure3_suriowari_start,abzure_heikin_start,"
                + "printzure1_surihajime_end,printzure2_center_end,printzure3_suriowari_end,abzure_heikin_end,genryoukigou,bikou1,"
                + "bikou2,torokunichiji,kosinnichiji,revision,kcpno "
                + "FROM sr_spsprint_gra "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());

    }
    
            /**
     * 印刷・SPSｽｸﾘｰﾝ工程のデータを取得する
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    private static Map getSrSpsprintData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT kojyo,lotno,edaban,tapesyurui,tapelotno,TapeSlipKigo,genryoukigou,pastelotno,pastenendo,pasteondo,"
                + "seihanno,seihanmaisuu,startdatetime,enddatetime,skeegeno,skeegemaisuu,gouki,tantousya,kakuninsya,kansouondo,prnprofile,"
                + "kansoutime,saatu,skeegespeed,skeegeangle,mld,clearrance,bikou1,bikou2,makuatu1,makuatu2,makuatu3,makuatu4,makuatu5,"
                + "pastelotno2,pastelotno3,pastelotno4,pastelotno5,pastenendo2,pastenendo3,pastenendo4,pastenendo5,pasteondo2,pasteondo3,"
                + "pasteondo4,pasteondo5,bikou3,bikou4,bikou5,kansouondo2,kansouondo3,kansouondo4,kansouondo5,skeegemaisuu2,taperollno1,"
                + "taperollno2,taperollno3,taperollno4,taperollno5,pastehinmei,seihanmei,makuatuave_start,makuatumax_start,makuatumin_start,"
                + "start_ptn_dist_x,start_ptn_dist_y,tanto_setting,makuatuave_end,makuatumax_end,makuatumin_end,end_ptn_dist_x,end_ptn_dist_y,"
                + "tanto_end,kcpno,sijiondo,sijiondo2,sijiondo3,sijiondo4,sijiondo5,TensionS,TensionE,TensionStemae,TensionEtemae,TensionSoku,"
                + "TensionEoku,AtuDoATu,BladeATu,AtuDoKeiLEnd,AtuDoKeiLSide,AtuDoKeiCenter,AtuDoKeiRSide,AtuDoKeiREnd,pkokeibun1,pkokeibun2,"
                + "petfilmsyurui,makuatucv_start,nijimikasure_start,makuatucv_end,nijimikasure_end,printmaisuu,table_clearrance,scraperspeed,"
                + "skeegegaikan,torokunichiji,kosinnichiji,revision "
                + "FROM sr_spsprint "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());

    }
    
    /**
     * 印刷・SPSｽｸﾘｰﾝ工程のデータを取得する
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    private static Map getSrRsusprnData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT KOJYO,LOTNO,EDABAN,KCPNO,TAPESYURUI,TAPELOTNO,TapeSlipKigo,GENRYOKIGO,KAISINICHIJI,SYURYONICHIJI,"
                + "GOKI,SKEEGENO,SKEEGEMAISUU,SKEEGESPEED,KANSOONDO,CLEARANCE,SAATU,MAKUATU1,SEIHANNO,SEIHANMAISUU,PASTELOTNO,"
                + "PASTENENDO,PASTEONDO,INSATUROLLNO,INSATUROLLNO2,INSATUROLLNO3,INSATUROLLNO4,INSATUROLLNO5,INSATUHABASAVE,"
                + "INSATUHABAEAVE,MLD,BIKO1,BIKO2,TOROKUNICHIJI,KOSINNICHIJI,TANTOSYA,pkokeibun1,pastelotno2,pastenendo2,pasteondo2,"
                + "pkokeibun2,petfilmsyurui,kansoondo2,kansoondo3,kansoondo4,kansoondo5,seihanmei,makuatsu_ave_start,makuatsu_max_start,"
                + "makuatsu_min_start,makuatucv_start,nijimikasure_start,nijimikasure_end,tanto_end,printmaisuu,kansouroatsu,printhaba,"
                + "table_clearrance,revision "
                + "FROM sr_rsusprn "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());

    }

    
    
    /**
     * 積層・SPS工程のデータを取得する
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    private static Map getSrSpssekisouData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT kojyo,lotno,edaban,tntapesyurui,tntapeno,tntapegenryou,gouki,startdatetime,enddatetime,sekisouzure,"
                + "tantousya,kakuninsya,sekisouzure2,bikou1,bikou2,bikou3,bikou4,bikou5,KCPNO,GoukiCode,TpLot,HakuriSpeed,KanaOndo,"
                + "DAturyoku,DKaatuJikan,CPressAturyoku,CPressKaatuJikan,CPressKankakuSosuu,LastKaaturyoku,LastKaatuJikan,FourSplitTantou,"
                + "STaoreryo1,STaoreryo2,STaoreryo3,STaoreryo4,STsunAve,STsunMax,STsunMin,STsunSiguma,HNGKaisuu,GaikanTantou,CPressKaisuu,"
                + "HNGKaisuuAve,GNGKaisuu,GNGKaisuuAve,tapelotno,taperollno1,taperollno2,taperollno3,genryoukigou,petfilmsyurui,Kotyakugouki,"
                + "Kotyakusheet,ShitaTanshigouki,UwaTanshigouki,ShitaTanshiBukunuki,ShitaTanshi,HakuriKyuin,HakuriClearrance,HakuriCutSpeed,"
                + "ShitaPanchiOndo,UwaPanchiOndo,KaatuJikan,KaatuAturyoku,UwaTanshi,GaikanKakunin1,GaikanKakunin2,GaikanKakunin3,GaikanKakunin4,"
                + "SyoriSetsuu,RyouhinSetsuu,StartTantosyacode,EndTantousyacode,TanshiTapeSyurui,torokunichiji,kosinnichiji,revision "
                + "FROM sr_spssekisou "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());

    }

    /**
     * 積層・RSUS工程のデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    private static Map getSrRsussekData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        String sql = "SELECT KOJYO,LOTNO,EDABAN,KCPNO,TNTAPESYURUI,TNTAPENO,TNTAPEGENRYO,KAISINICHIJI,SYURYONICHIJI,GOKI,"
                + "JITUATURYOKU,SEKISOZURE2,TANTOSYA,KAKUNINSYA,INSATUROLLNO,HAPPOSHEETNO,SKJIKAN,TAKUTO,BIKO1,TOROKUNICHIJI,"
                + "KOSINNICHIJI,SKOJYO,SLOTNO,SEDABAN,tapelotno,taperollno1,taperollno2,taperollno3,genryoukigou,petfilmsyurui,"
                + "Kotyakugouki,Kotyakusheet,ShitaTanshigouki,UwaTanshigouki,ShitaTanshiBukunuki,ShitaTanshi,UwaTanshi,"
                + "SyoriSetsuu,RyouhinSetsuu,GaikanKakunin1,GaikanKakunin2,GaikanKakunin3,GaikanKakunin4,EndTantousyacode,"
                + "TanshiTapeSyurui,HNGKaisuu,HNGKaisuuAve,GNGKaisuu,GNGKaisuuAve,bikou2,revision "
                + "FROM sr_rsussek "
                + "WHERE KOJYO = ? AND LOTNO = ? "
                + "AND EDABAN = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * 印刷積層・RHAPS工程のデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    private static Map getSrRhapsData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        String sql = "SELECT KOJYO,LOTNO,EDABAN,KCPNO,KAISINICHIJI,SYURYONICHIJI,TTAPESYURUI,TTAPELOTNO,TTapeSlipKigo,TTapeRollNo1,"
                + "TTapeRollNo2,TTapeRollNo3,TTapeRollNo4,TTapeRollNo5,TGENRYOKIGO,STSIYO,ESEKISOSIYO,ETAPESYURUI,ETAPEGLOT,ETAPELOT,ETapeSlipKigo,"
                + "ETapeRollNo1,ETapeRollNo2,ETapeRollNo3,ETapeRollNo4,ETapeRollNo5,SPTUDENJIKAN,SKAATURYOKU,SKHEADNO,SUSSKAISUU,ECPASTEMEI,EPASTELOTNO,"
                + "EPASTENENDO,EPASTEONDO,ESEIHANMEI,ESEIHANNO,ESEIMAISUU,ECLEARANCE,ESAATU,ESKEEGENO,ESKMAISUU,ESKSPEED,ESCCLEARANCE,ESKKMJIKAN,ELDSTART,"
                + "ESEIMENSEKI,EMAKUATU,ESLIDERYO,EKANSOONDO,EKANSOJIKAN,CPASTELOTNO,CPASTENENDO,CPASTEONDO,CSEIHANMEI,CSEIHANNO,CSEIMAISUU,CCLEARANCE,CSAATU,"
                + "CSKEEGENO,CSKMAISUU,CSCCLEARANCE,CSKKMJIKAN,CSHIFTINSATU,CLDSTART,CSEIMENSEKI,CSLIDERYO,CKANSOONDO,CKANSOJIKAN,CMAKUATU,AINSATUSRZ1,"
                + "AINSATUSRZ2,AINSATUSRZ3,AINSATUSRZ4,AINSATUSRZ5,AINSATUSRZAVE,UTSIYO,UTTUDENJIKAN,UTKAATURYOKU,STAHOSEI,TICLEARANCE,TISAATU,TISKSPEED,FSTHUX1,"
                + "FSTHUX2,FSTHUY1,FSTHUY2,FSTHSX1,FSTHSX2,FSTHSY1,FSTHSY2,FSTCX1,FSTCX2,FSTCY1,FSTCY2,FSTMUX1,FSTMUX2,FSTMUY1,FSTMUY2,FSTMSX1,FSTMSX2,FSTMSY1,"
                + "FSTMSY2,LSTHUX1,LSTHUX2,LSTHUY1,LSTHUY2,LSTHSX1,LSTHSX2,LSTHSY1,LSTHSY2,LSTCX1,LSTCX2,LSTCY1,LSTCY2,LSTMUX1,LSTMUX2,LSTMUY1,LSTMUY2,LSTMSX1,"
                + "LSTMSX2,LSTMSY1,LSTMSY2,BIKO1,BIKO2,TOROKUNICHIJI,KOSINNICHIJI,GOKI,TTANSISUUU,TTANSISUUS,SHUNKANKANETSUJIKAN,PETFILMSYURUI,KAATURYOKU,"
                + "GAIKANKAKUNIN,SEKIJSSKIRIKAEICHI,SEKIKKSKIRIKAEICHI,KAATUJIKAN,TAPEHANSOUPITCH,TAPEHANSOUKAKUNIN,EMAKUATSUSETTEI,ENEPPUFURYOU,EMAKUATSUAVE,"
                + "EMAKUATSUMAX,EMAKUATSUMIN,NIJIMISOKUTEIPTN,PRNSAMPLEGAIKAN,PRNICHIYOHAKUNAGASA,CTABLECLEARANCE,CMAKUATSUSETTEI,CSKSPEED,CNEPPUFURYOU,KABURIRYOU,"
                + "SGAIKAN,NIJIMISOKUTEISEKISOUGO,SEKISOUHINGAIKAN,SEKISOUZURE,UWAJSSKIRIKAEICHI,SHITAKKSKIRIKAEICHI,TINKSYURYUI,TINKLOT,TGAIKAN,STARTTANTOU,ENDTANTOU,"
                + "TENDDAY,TENDTANTOU,SYORISETSUU,RYOUHINSETSUU,HEADKOUKANTANTOU,SEKISOUJOUKENTANTOU,ESEIHANSETTANTOU,CSEIHANSETTANTOU,DANSASOKUTEITANTOU,revision "
                + "FROM sr_rhaps "
                + "WHERE KOJYO = ? AND LOTNO = ? "
                + "AND EDABAN = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * ﾌﾟﾚｽ・仮ﾌﾟﾚｽ工程のデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    private static Map getSrPrepressData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,startdatetime,enddatetime,ondohyoji,aturyokusetteiti,kaatujikan,goki,"
                + "tantosya,kakuninsya,biko1,torokunichiji,kosinnichiji,sagyokubun,setsuu,tansimaisuu,aturyokuhyouji,"
                + "pressside,jissekino,koteicode,kansyouzai,shinkuuhoji,aturyokusetteiti2,kaatujikan2,aturyokusetteiti3,"
                + "kaatujikan3,shinkuudo,Hokanjouken,EndTantousyacode,RyouhinSetsuu,biko2,revision "
                + "FROM sr_prepress "
                + "WHERE KOJYO = ? AND LOTNO = ? "
                + "AND EDABAN = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * ﾌﾟﾚｽ・真空脱気工程の良品ｾｯﾄ数を取得する。 ※データ無し時は"nodata"の文字列を返す。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    private static Map getSrPressData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,startdatetime,enddatetime,gouki,ondo,tantousya,kakuninsya,bikou1,"
                + "bikou2,bikou3,bikou4,bikou5,situon,situdo,aturyoku,jikan1,jikan2,atumimin,atumimax,shinkuuhojicheck,"
                + "cerapeel,kansyouzai1,susborad,kansyouzai2,seisuiatupressgouki,yonetujikan1,yonetujikan2,yonetujikan3,"
                + "aturyoku1max,aturyoku2max,mizunureSetsuu,Pressgoreikyakujikan,pressmaeaging,EndTantousyacode,Setsuu,"
                + "RyouhinSetsuu,Presskaisuu,torokunichiji,kosinnichiji,revision "
                + "FROM sr_press "
                + "WHERE KOJYO = ? AND LOTNO = ? "
                + "AND EDABAN = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());

    }

    /**
     * ﾌﾟﾚｽ・ﾒｶﾌﾟﾚｽ工程のデータを取得する
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    private static Map getSrMekapressData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        String sql = "SELECT KOJYO,LOTNO,EDABAN,KCPNO,KAISINICHIJI,SYURYONICHIJI,UKEIRESETSUU,RyouhinSetsuu,GOKI,ONDO,ATURYOKU,"
                + "SITUON,SITUDO,ATUMIMIN,ATUMIMAX,ATUMITANTOSYA,TANTOSYA,KAKUNINSYA,BIKO1,BIKO2,TOROKUNICHIJI,KOSINNICHIJI,"
                + "airnuki,dakkijikan,dakkiaturyoku,EndTantousyacode,revision "
                + "FROM sr_mekapress "
                + "WHERE KOJYO = ? AND LOTNO = ? "
                + "AND EDABAN = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * ｶｯﾄ・押切ｶｯﾄ工程のデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    private static Map getSrHapscutData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        String sql = "SELECT KOJYO,LOTNO,EDABAN,KCPNO,KAISINICHIJI,SYURYONICHIJI,CUTBAMAISUU,GOKI,CUTTABLEONDO,CUTTANTOSYA,KAKUNINSYA,CHKTANTOSYA,"
                + "BTANTOSYA,ATANTOSYA,UKEIREKOSUU,RYOHINKOSUU,Atumi01,Atumi02,Atumi03,Atumi04,Atumi05,Atumi06,Atumi07,Atumi08,Atumi09,Atumi10,"
                + "ATUMIMIN,ATUMIMAX,BIKO1,BIKO2,BIKO3,BIKO4,TOROKUNICHIJI,KOSINNICHIJI,TENSYASYA,NIJIMICNT,Soujyuryo,Tanijyuryo,cutbashuruicheck,"
                + "cutbachokushindo,cutbasiyoukaisuuST1,cutbasiyoukaisuuST2,programmei,gyoretukakunin,marktorisuu,cuthoseiryou,tableondoset,tableondosoku,"
                + "gaikancheck,hatakasang,syorisetsuu,RyouhinSetsuu,sagyoubasyo,revision "
                + "FROM sr_hapscut "
                + "WHERE KOJYO = ? AND LOTNO = ? "
                + "AND EDABAN = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * ｶｯﾄ・ﾀﾞｲｼﾝｸﾞｶｯﾄ工程の良品ｾｯﾄ数を取得する。 ※データ無し時は"nodata"の文字列を返す。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    private static Map getSrCutData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,startdatetime,enddatetime,cutbamaisuu,gouki,cuttableondo,tantousya,kakuninsya,"
                + "bikou1,bikou2,bikou3,bikou4,bikou5,housiki,atumimin,atumimax,cutbashurui,cutmuki,happosheetcolor,kansou,"
                + "hoseihantei,EndTantousyacode,syorisetsuu,RyouhinSetsuu,torokunichiji,kosinnichiji,revision "
                + "FROM sr_cut "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * 生品質検査工程のデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    private static Map getSrCutcheckData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,startdatetime,enddatetime,tantousya,kakuninsya,AtumiMin,AtumiMax,Atumi01,"
                + "Atumi02,Atumi03,Atumi04,Atumi05,Atumi06,Atumi07,Atumi08,Atumi09,Atumi10,bikou1,bikou2,bikou3,bikou4,bikou5,"
                + "Soujyuryo,Tanijyuryo,gaikankensatantousya,barasshi,joken,barashistartnichiji,batashistarttantousya,"
                + "barashiendnichiji,barashiendtantousya,konamabushi,syorisetsuu,RyouhinSetsuu,budomari,torokunichiji,"
                + "kosinnichiji,revision "
                + "FROM sr_cutcheck "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * ｾｯﾀ詰めのデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    private static Map getSrSayadumeData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,kcpno,kaishinichiji,syuuryounichiji,sayadumegouki,sayasuu,kosuu,tantousya,"
                + "jissekino,bikou1,bikou2,bikou3,bikou4,bikou5,tourokunichiji,koushinnichiji,settertumeryou,settertumehouhou,"
                + "settersyurui,konamabushi,StartKakuninsyacode,EndTantousyacode,revision "
                + "FROM sr_sayadume "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * 1次脱脂(Air_窒素)のデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    private static Map getSrYobikanData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev, int jissekino) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,kcpno,kosuu,kaisinichiji,syuryoyoteinichiji,syuryonichiji,"
                + "yobikangoki,sayasuu,yobikanjikan,yobikanondo,peakjikan,kaisitantosya,syuryotantosya,jissekino,"
                + "biko1,biko2,biko3,biko4,biko5,torokunichiji,kosinnichiji,NijiKaishiNichiji,NijiGoki,programno,"
                + "tounyusettersuu,StartKakuninsyacode,kaisyusettersuu,dasshisyurui,revision "
                + "FROM sr_yobikan "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? "
                + "AND jissekino = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        params.add(jissekino);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }
    
        /**
     * 2次脱脂(ﾍﾞﾙﾄ)のデータを取得する。
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev ﾘﾋﾞｼﾞｮﾝ(検索キー)
     * @return 工程データ
     * @throws SQLException 例外エラー
     */
    private static Map getSrNijidasshiData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        
        // 画面実装時に再確認
        String sql = "SELECT kojyo,lotno,edaban,kcpno,settasuu,kaisinichiji,StartTantosyacode,StartKakuninsyacode,tounyusettasuu,"
                + "Nijidasshigouki,NijidasshisetteiPT,Nijidasshikeepondo,Nijidasshispeed,syuuryounichiji,EndTantosyacode,kaishuusettasuu,"
                + "bikou1,bikou2,tourokunichiji,koushinnichiji,revision "
                + "FROM sr_nijidasshi "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND revision = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
    
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

}
