/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
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
        String sql = "SELECT ryouhinsetsuu "
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
        return StringUtil.nullToBlank(data.get("ryouhinsetsuu"));
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
        String sql = "SELECT ryouhinsetsuu "
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
        return StringUtil.nullToBlank(data.get("ryouhinsetsuu"));
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
        String sql = "SELECT ryouhinsetsuu "
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
        return StringUtil.nullToBlank(data.get("ryouhinsetsuu"));
    }

}
