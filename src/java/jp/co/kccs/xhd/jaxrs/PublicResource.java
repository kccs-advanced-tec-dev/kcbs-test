/*
 * Copyright 2017 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.jaxrs;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import jp.co.kccs.xhd.util.DBUtil;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.NumberUtil;
import jp.co.kccs.xhd.util.StringUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DBシステム(コンデンサ)<br>
 * <br>
 * 変更日	2017/02/18<br>
 * 計画書No	K1604-DS003<br>
 * 変更者	KCCS R.Fujimura<br>
 * 変更理由	新規作成<br>
 * <br>
 * 変更日	2018/11/13<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	ﾛｯﾄｶｰﾄﾞ電子化対応<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * ロケーション情報を操作するクラスです。
 *
 * @author KCCS R.Fujimura
 * @since 2017/02/18
 */
@Path("public")
public class PublicResource {

    private static final Logger LOGGER = Logger.getLogger(PublicResource.class.getName());

    @Context
    private UriInfo context;
    @Resource(mappedName = "jdbc/DocumentServer")
    DataSource dataSource;

    /**
     * コンストラクタ
     *
     * @author KCCS fujimura
     * @since 2017/02/18
     */
    public PublicResource() {
    }

    /**
     * ロケーション情報をロケーションログへ登録します。
     *
     * @param termNo MACアドレス
     * @param page 起動画面のXHTML
     * @param location ロケーション情報
     * @return 結果
     * @author KCCS fujimura
     * @since 2017/02/18
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("getLocation/{termNo}/{page}/{location}")
    public String getLocation(
            @PathParam("termNo") String termNo,
            @PathParam("page") String page,
            @PathParam("location") String location) {
        Date date = new Date();
        String msg = String.format("%s %s %s %s", date.toString(), termNo, page, location);
        System.out.println(msg);

        Connection conn = null;
        PreparedStatement ps = null;
        DateFormat dfDate = new SimpleDateFormat("yyMMdd");

        try {
            conn = dataSource.getConnection();

            String executeSQL = "INSERT INTO fxhdd91 (torokusha,toroku_date,koshinsha,koshin_date,mac_address,torokubi,koushin_kaisuu, "
                    + " location,gamen_mei,last_kousin_date,last_location,last_gamen_mei) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
                    + "  ON CONFLICT(mac_address,torokubi) DO UPDATE  SET "
                    + "    koshinsha = ? "
                    + "  , koshin_date = ? "
                    + "  , koushin_kaisuu = fxhdd91.koushin_kaisuu + 1 "
                    + "  , location = ? "
                    + "  , gamen_mei = ? ";

            // ﾛｹｰｼｮﾝが取得出来た場合のみ更新する。
            if (!"NA".equals(location) && !"TIMEOUT".equals(location)) {
                executeSQL += "  , last_kousin_date = ? "
                        + "  , last_location = ? "
                        + "  , last_gamen_mei = ? ";
            }

            ps = conn.prepareStatement(executeSQL);

            //INSERT
            ps.setString(1, "system");
            ps.setTimestamp(2, new java.sql.Timestamp(new Date().getTime()));
            ps.setString(3, null);
            ps.setTimestamp(4, null);
            ps.setString(5, escapeSQL(blankToNull(stringLeft(termNo, 255))));
            ps.setString(6, dfDate.format(date));
            ps.setInt(7, 1);
            ps.setString(9, escapeSQL(blankToNull(stringLeft(page, 255))));

            //UPDATE
            ps.setString(13, "system");
            ps.setTimestamp(14, new java.sql.Timestamp(new Date().getTime()));
            ps.setString(16, escapeSQL(blankToNull(stringLeft(page, 255))));

            if ("NA".equals(location) || "TIMEOUT".equals(location)) {
                // INSERT
                ps.setString(8, null);
                ps.setTimestamp(10, null);
                ps.setString(11, null);
                ps.setString(12, null);
                // UPDATE
                ps.setString(15, null);

            } else {
                // INSERT
                ps.setString(8, escapeSQL(blankToNull(stringLeft(location, 255))));
                ps.setTimestamp(10, new java.sql.Timestamp(new Date().getTime()));
                ps.setString(11, escapeSQL(blankToNull(stringLeft(location, 255))));
                ps.setString(12, escapeSQL(blankToNull(stringLeft(page, 255))));

                // UPDATE
                ps.setString(15, escapeSQL(blankToNull(stringLeft(location, 255))));
                ps.setTimestamp(17, new java.sql.Timestamp(new Date().getTime()));
                ps.setString(18, escapeSQL(blankToNull(stringLeft(location, 255))));
                ps.setString(19, escapeSQL(blankToNull(stringLeft(page, 255))));
            }

            //SQLを実行する
            ps.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, null, e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, null, e);
            }
        }

        return termNo + " ok";
    }

    /**
     * 設備データ登録処理
     *
     * @param param 設備データ
     * @return 結果
     */
    @POST
    @Path("pxhdo411")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response pxhdo411(FXHDD07Json param) {
        QueryRunner queryRunner = new QueryRunner(dataSource);
        Connection con = null;

        try {
            Timestamp registDate = new Timestamp(System.currentTimeMillis());

            // トランザクション開始
            con = DBUtil.transactionStart(queryRunner.getDataSource().getConnection());

            // データ検索
            String sql = "SELECT MAX(deleteflag) AS deleteflag FROM FXHDD07 "
                    + "WHERE kojyo = ? AND lotno = ? AND edaban = ? ";
            List<Object> params = new ArrayList<>(Arrays.asList(param.getKojyo(), param.getLotno(), param.getEdaban()));

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            Map fxhdd07_delflag = queryRunner.query(sql, new MapHandler(), params.toArray());

            // 削除フラグ + 1 を取得
            int deleteflag = 1;
            if (null != fxhdd07_delflag && null != fxhdd07_delflag.get("deleteflag")) {
                deleteflag = (int) fxhdd07_delflag.get("deleteflag") + 1;
            }

            // 削除フラグ = 0 のデータが存在する場合はUPDATE
            String sqlUpd = "UPDATE FXHDD07 SET koshin_date = ?, deleteflag = ? "
                    + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND deleteflag = 0";
            List<Object> paramsUpd
                    = new ArrayList<>(Arrays.asList(registDate, deleteflag, param.getKojyo(), param.getLotno(), param.getEdaban()));

            DBUtil.outputSQLLog(sqlUpd, paramsUpd.toArray(), LOGGER);
            queryRunner.update(con, sqlUpd, paramsUpd.toArray());

            // データ登録
            String sqlIns = "INSERT INTO FXHDD07 ("
                    + "kojyo, lotno, edaban, gouki, bunruiairatu, cdcontactatu, ircontactatu, tan, sokuteisyuhasuu, sokuteidenatu, pcdenatu1, pcjudenjikan1, "
                    + "pcdenatu2, pcjudenjikan2, pcdenatu3, pcjudenjikan3, pcdenatu4, pcjudenjikan4, irdenatu1, irhanteiti1_low, irhanteiti1, irjudenjikan1, "
                    + "irdenatu2, irhanteiti2_low, irhanteiti2, irjudenjikan2, irdenatu3, irhanteiti3_low, irhanteiti3, irjudenjikan3, "
                    + "irdenatu4, irhanteiti4_low, irhanteiti4, irjudenjikan4, irdenatu5, irhanteiti5_low, irhanteiti5, irjudenjikan5, "
                    + "irdenatu6, irhanteiti6_low, irhanteiti6, irjudenjikan6, irdenatu7, irhanteiti7_low, irhanteiti7, irjudenjikan7, "
                    + "irdenatu8, irhanteiti8_low, irhanteiti8, irjudenjikan8, rdcrange1, rdchantei1, rdcrange2, rdchantei2, "
                    + "bin1countersuu, bin2countersuu, bin3countersuu, bin4countersuu, bin5countersuu, bin6countersuu, bin7countersuu, "
                    + "bin8countersuu, bin5setteiti, bin6setteiti, bin7setteiti, bin8setteiti, toroku_date, koshin_date, deleteflag"
                    + ") VALUES ("
                    + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
                    + ")";

            // 計算処理
            BigDecimal tan; // Tanδ
            BigDecimal irHanteiti1Low; // 耐電圧設定条件 IR① 判定値(低)
            BigDecimal irHanteiti1; // 耐電圧設定条件 IR① 判定値
            BigDecimal irHanteiti2Low; // 耐電圧設定条件 IR② 判定値(低)
            BigDecimal irHanteiti2; // 耐電圧設定条件 IR② 判定値
            BigDecimal irHanteiti3Low; // 耐電圧設定条件 IR③ 判定値(低)
            BigDecimal irHanteiti3; // 耐電圧設定条件 IR③ 判定値
            BigDecimal irHanteiti4Low; // 耐電圧設定条件 IR④ 判定値(低)
            BigDecimal irHanteiti4; // 耐電圧設定条件 IR④ 判定値
            BigDecimal irHanteiti5Low; // 耐電圧設定条件 IR⑤ 判定値(低)
            BigDecimal irHanteiti5; // 耐電圧設定条件 IR⑤ 判定値
            BigDecimal irHanteiti6Low; // 耐電圧設定条件 IR⑥ 判定値(低)
            BigDecimal irHanteiti6; // 耐電圧設定条件 IR⑥ 判定値
            BigDecimal irHanteiti7Low; // 耐電圧設定条件 IR⑦ 判定値(低)
            BigDecimal irHanteiti7; // 耐電圧設定条件 IR⑦ 判定値
            BigDecimal irHanteiti8Low; // 耐電圧設定条件 IR⑧ 判定値(低)
            BigDecimal irHanteiti8; // 耐電圧設定条件 IR⑧ 判定値
            if ("ESI".equals(param.getMaker())) {
                tan = getCalcTan(param.getTan());
                irHanteiti1Low = getHanteichi(param.getIrhanteiti1_low());
                irHanteiti1 = getHanteichi(param.getIrhanteiti1());
                irHanteiti2Low = getHanteichi(param.getIrhanteiti2_low());
                irHanteiti2 = getHanteichi(param.getIrhanteiti2());
                irHanteiti3Low = getHanteichi(param.getIrhanteiti3_low());
                irHanteiti3 = getHanteichi(param.getIrhanteiti3());
                irHanteiti4Low = getHanteichi(param.getIrhanteiti4_low());
                irHanteiti4 = getHanteichi(param.getIrhanteiti4());
                irHanteiti5Low = getHanteichi(param.getIrhanteiti5_low());
                irHanteiti5 = getHanteichi(param.getIrhanteiti5());
                irHanteiti6Low = getHanteichi(param.getIrhanteiti6_low());
                irHanteiti6 = getHanteichi(param.getIrhanteiti6());
                irHanteiti7Low = getHanteichi(param.getIrhanteiti7_low());
                irHanteiti7 = getHanteichi(param.getIrhanteiti7());
                irHanteiti8Low = getHanteichi(param.getIrhanteiti8_low());
                irHanteiti8 = getHanteichi(param.getIrhanteiti8());
            } else {
                tan = (BigDecimal) getFormatData(param.getTan(), "3", "1", "BigDecimal");
                irHanteiti1Low = (BigDecimal) getFormatData(param.getIrhanteiti1_low(), "2", "12", "BigDecimal");
                irHanteiti1 = (BigDecimal) getFormatData(param.getIrhanteiti1(), "2", "12", "BigDecimal");
                irHanteiti2Low = (BigDecimal) getFormatData(param.getIrhanteiti2_low(), "2", "12", "BigDecimal");
                irHanteiti2 = (BigDecimal) getFormatData(param.getIrhanteiti2(), "2", "12", "BigDecimal");
                irHanteiti3Low = (BigDecimal) getFormatData(param.getIrhanteiti3_low(), "2", "12", "BigDecimal");
                irHanteiti3 = (BigDecimal) getFormatData(param.getIrhanteiti3(), "2", "12", "BigDecimal");
                irHanteiti4Low = (BigDecimal) getFormatData(param.getIrhanteiti4_low(), "2", "12", "BigDecimal");
                irHanteiti4 = (BigDecimal) getFormatData(param.getIrhanteiti4(), "2", "12", "BigDecimal");
                irHanteiti5Low = (BigDecimal) getFormatData(param.getIrhanteiti5_low(), "2", "12", "BigDecimal");
                irHanteiti5 = (BigDecimal) getFormatData(param.getIrhanteiti5(), "2", "12", "BigDecimal");
                irHanteiti6Low = (BigDecimal) getFormatData(param.getIrhanteiti6_low(), "2", "12", "BigDecimal");
                irHanteiti6 = (BigDecimal) getFormatData(param.getIrhanteiti6(), "2", "12", "BigDecimal");
                irHanteiti7Low = (BigDecimal) getFormatData(param.getIrhanteiti7_low(), "2", "12", "BigDecimal");
                irHanteiti7 = (BigDecimal) getFormatData(param.getIrhanteiti7(), "2", "12", "BigDecimal");
                irHanteiti8Low = (BigDecimal) getFormatData(param.getIrhanteiti8_low(), "2", "12", "BigDecimal");
                irHanteiti8 = (BigDecimal) getFormatData(param.getIrhanteiti8(), "2", "12", "BigDecimal");
            }

            List<Object> paramIns
                    = new ArrayList<>(Arrays.asList(
                            getFormatData(param.getKojyo(), "3", "", "String"),
                             getFormatData(param.getLotno(), "8", "", "String"),
                             getFormatData(param.getEdaban(), "3", "", "String"),
                             getFormatData(param.getGouki(), "4", "", "String"),
                             getFormatData(param.getBunruiairatu(), "3", "", "Integer"),
                             getFormatData(param.getCdcontactatu(), "3", "", "Integer"),
                             getFormatData(param.getIrcontactatu(), "3", "", "Integer"),
                             tan,
                             getFormatData(param.getSokuteisyuhasuu(), "10", "", "String"),
                             getFormatData(param.getSokuteidenatu(), "1", "1", "BigDecimal"),
                             getFormatData(param.getPcdenatu1(), "4", "1", "BigDecimal"),
                             getFormatData(param.getPcjudenjikan1(), "3", "", "Integer"),
                             getFormatData(param.getPcdenatu2(), "4", "1", "BigDecimal"),
                             getFormatData(param.getPcjudenjikan2(), "3", "", "Integer"),
                             getFormatData(param.getPcdenatu3(), "4", "1", "BigDecimal"),
                             getFormatData(param.getPcjudenjikan3(), "3", "", "Integer"),
                             getFormatData(param.getPcdenatu4(), "4", "1", "BigDecimal"),
                             getFormatData(param.getPcjudenjikan4(), "3", "", "Integer"),
                             getFormatData(param.getIrdenatu1(), "4", "1", "BigDecimal"),
                             irHanteiti1Low,
                             irHanteiti1,
                             getFormatData(param.getIrjudenjikan1(), "3", "", "Integer"),
                             getFormatData(param.getIrdenatu2(), "4", "1", "BigDecimal"),
                             irHanteiti2Low,
                             irHanteiti2,
                             getFormatData(param.getIrjudenjikan2(), "3", "", "Integer"),
                             getFormatData(param.getIrdenatu3(), "4", "1", "BigDecimal"),
                             irHanteiti3Low,
                             irHanteiti3,
                             getFormatData(param.getIrjudenjikan3(), "3", "", "Integer"),
                             getFormatData(param.getIrdenatu4(), "4", "1", "BigDecimal"),
                             irHanteiti4Low,
                             irHanteiti4,
                             getFormatData(param.getIrjudenjikan4(), "3", "", "Integer"),
                             getFormatData(param.getIrdenatu5(), "4", "1", "BigDecimal"),
                             irHanteiti5Low,
                             irHanteiti5,
                             getFormatData(param.getIrjudenjikan5(), "3", "", "Integer"),
                             getFormatData(param.getIrdenatu6(), "4", "1", "BigDecimal"),
                             irHanteiti6Low,
                             irHanteiti6,
                             getFormatData(param.getIrjudenjikan6(), "3", "", "Integer"),
                             getFormatData(param.getIrdenatu7(), "4", "1", "BigDecimal"),
                             irHanteiti7Low,
                             irHanteiti7,
                             getFormatData(param.getIrjudenjikan7(), "3", "", "Integer"),
                             getFormatData(param.getIrdenatu8(), "4", "1", "BigDecimal"),
                             irHanteiti8Low,
                             irHanteiti8,
                             getFormatData(param.getIrjudenjikan8(), "3", "", "Integer"),
                             getFormatData(param.getRdcrange1(), "4", "1", "BigDecimal"),
                             getFormatData(param.getRdchantei1(), "4", "1", "BigDecimal"),
                             getFormatData(param.getRdcrange2(), "4", "1", "BigDecimal"),
                             getFormatData(param.getRdchantei2(), "4", "1", "BigDecimal"),
                             getFormatData(param.getBin1countersuu(), "8", "", "Integer"),
                             getFormatData(param.getBin2countersuu(), "8", "", "Integer"),
                             getFormatData(param.getBin3countersuu(), "8", "", "Integer"),
                             getFormatData(param.getBin4countersuu(), "8", "", "Integer"),
                             getFormatData(param.getBin5countersuu(), "8", "", "Integer"),
                             getFormatData(param.getBin6countersuu(), "8", "", "Integer"),
                             getFormatData(param.getBin7countersuu(), "8", "", "Integer"),
                             getFormatData(param.getBin8countersuu(), "8", "", "Integer"),
                             getBinXsetteiti(param.getBin5setteiti()),
                             getBinXsetteiti(param.getBin6setteiti()),
                             getBinXsetteiti(param.getBin7setteiti()),
                             getBinXsetteiti(param.getBin8setteiti()),
                             registDate,
                             null,
                             0
                    ));

            DBUtil.outputSQLLog(sqlIns, paramIns.toArray(), LOGGER);
            queryRunner.update(con, sqlIns, paramIns.toArray());

            // commit
            DbUtils.commitAndCloseQuietly(con);

            return Response.ok().build();

        } catch (SQLException e) {
            ErrUtil.outputErrorLog("SQLException発生", e, LOGGER);
            DBUtil.rollbackConnection(con, LOGGER);
            return Response.serverError().build();
        } catch (Exception e) {
            ErrUtil.outputErrorLog("Exception発生", e, LOGGER);
            DBUtil.rollbackConnection(con, LOGGER);
            return Response.serverError().build();
        }
    }
    
    /**
     * 設定値を取得
     * @param value 値
     * @return 設定値
     */
    private String getBinXsetteiti(String value){
        String setteici = getFormatData(value, "20", "", "String").toString();
        //"Reject Bin"の場合は空にする。
        if("Reject Bin".equals(setteici)){
            return "";
        }
        return setteici;
    }

    /**
     * Tanδ計算処理
     * @param value 値
     * @return 計算結果
     */
    private BigDecimal getCalcTan(String value) {
        BigDecimal tan = BigDecimal.ZERO;
        String[] spTan = value.split("~", -1);
        if (1 < spTan.length) {
            tan = new BigDecimal(getNumberData(spTan[1], "", "")[0]).multiply(BigDecimal.valueOf(100));
        }
        return new BigDecimal(NumberUtil.getTruncatData(tan.toPlainString(), "3", "1"));
    }

    /**
     * 判定値計算処理
     * @param value 値
     * @return 計算結果
     */
    private BigDecimal getHanteichi(String value) {
        // 値の中から数値部、単位を取得する。
        String[] hanteichi = getNumberData(value, "", "");
        // 数値部をBigDecimalに変換(小数点3位以下切り捨て)
        BigDecimal decHanteichi = new BigDecimal(hanteichi[0]).setScale(2, RoundingMode.DOWN);
        BigDecimal devValue = null;
        switch (hanteichi[1]) {
            case "mA":
                devValue = new BigDecimal("1000");
                break;
            case "µA":
                devValue = new BigDecimal("1000000");
                break;
            case "uA":
                devValue = new BigDecimal("1000000");
                break;
            case "nA":
                devValue = new BigDecimal("1000000000");
                break;
            default:
                break;
        }

        if (devValue != null) {
            decHanteichi = decHanteichi.divide(devValue, 12, RoundingMode.DOWN);
        }

        return new BigDecimal(NumberUtil.getTruncatData(decHanteichi.toPlainString(), "2", "12"));
    }

    /**
     * 対象ﾃﾞｰﾀのﾌｫｰﾏｯﾄ(型,桁数)結果を取得
     * @param value 値
     * @param length 桁数(整数部)
     * @param decLength 桁数(小数部)
     * @param type 型(String,Integer,BigDecimal指定)
     * @return 
     */
    private Object getFormatData(String value, String length, String decLength, String type) {
        switch (type) {
            case "String":
                return StringUtil.left(StringUtil.trimAll(value), Integer.parseInt(length));

            case "Integer":
                return Integer.parseInt(getNumberData(value, length, decLength)[0]);

            case "BigDecimal":
                return new BigDecimal(getNumberData(value, length, decLength)[0]);

            default:
                return StringUtil.left(StringUtil.trimAll(value), Integer.parseInt(length));
        }
    }

    /**
     * 数値ﾃﾞｰﾀ取得(数値部,単位をそれぞれ取得)
     * @param value 値
     * @param length 桁数(整数部)
     * @param decLength 桁数(小数部)
     * @return 数値ﾃﾞｰﾀ(数値部,"単位")
     */
    private String[] getNumberData(String value, String length, String decLength) {
        String getValue = StringUtil.trimAll(value);

        // 値が空の場合は0を返却
        if (StringUtil.isEmpty(getValue)) {
            return new String[]{"0", ""};
        }

        Pattern p = Pattern.compile("[0-9]");
        StringBuilder sb = new StringBuilder();
        int decPointCnt = 0;

        // 文字数
        int valueLength = getValue.length();
        int lastIndex = 0;
        for (int i = 0; i < valueLength; i++) {
            String str = getValue.substring(i, i + 1);
            if ("-".equals(str)) {
                // -(ﾏｲﾅｽ)の場合

                // 先頭以外の場合ループを抜ける
                if (0 < i) {
                    // 最終位置を保持
                    lastIndex = i;
                    break;
                }

            } else if (".".equals(str)) {
                // .(小数点)の場合

                // 小数点が複数の場合
                if (0 < decPointCnt) {
                    // 最終位置を保持
                    lastIndex = i;
                    break;
                }
                decPointCnt++;
            } else if (!p.matcher(str).find()) {
                // 最終位置を保持
                lastIndex = i;
                break;
            }
            sb.append(str);
        }

        if (NumberUtil.isNumeric(sb.toString())) {
            // 単位を取得
            String tani = StringUtil.trimAll(getValue.substring(lastIndex));

            // 桁指定なしの場合は切り捨てせずにリターン
            if (StringUtil.isEmpty(length)) {
                return new String[]{sb.toString(), tani};
            }

            return new String[]{NumberUtil.getTruncatData(sb.toString(), length, decLength), tani};
        }

        return new String[]{"0", ""};

    }

    /**
     * Macアドレス登録用のログを出力します。
     *
     * @param msg ログメッセージ
     * @return レスポンス
     * @author KCCS fujimura
     * @since 2017/07/07
     */
    @POST
    @Path("log")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postLog(LogMessage msg) {
        String temp = msg.getMessage();
        if (temp != null) {
            String[] message = temp.split(",");
            if (2 == message.length) {
                String mode = message[0];
                String macAddr = message[1];
                if ("R".equals(mode)) {
                    LOGGER.log(Level.INFO, "Save MacAddress:{0}", macAddr);
                } else if ("D".equals(mode)) {
                    LOGGER.log(Level.INFO, "Delete MacAddress:{0}", macAddr);
                }
            }
        }
        return Response.ok().build();
    }

    /**
     * 文字列の左側から指定文字数取得します。
     *
     * @param src 対象文字列
     * @param len 取得文字数
     * @return 取得した文字列
     * @author KCCS R.Fujimura
     * @since 2017/02/18
     */
    private String stringLeft(String src, int len) {
        int length;
        if (src != null) {
            length = src.length();
        } else {
            length = 0;
        }
        if (length <= len) {
            return src;
        }
        return src.substring(0, len);
    }

    /**
     * 文字列が空文字の場合、nullを返します。
     *
     * @param src 対象文字列
     * @return 文字列
     * @author KCCS R.Fujimura
     * @since 2017/02/18
     */
    private String blankToNull(String src) {

        if ("".equals(src)) {
            src = null;
        }
        return src;
    }

    /**
     * SQL文出力用に、次の置換を行います。<BR>
     * [置換される文字]<BR>
     * <CODE> ' -> '' \ -> \\ " -> "" </CODE>
     *
     * @param input 置換対象の文字列
     * @return 置換処理後の文字列
     * @author KCCS fujimura
     * @since 2017/02/15
     */
    private String escapeSQL(String input) {
        if (input == null) {
            return "";
        }
        input = substitute(input, "'", "''");
        return input;
    }

    /**
     * 文字列の全置換を行います。<BR>
     *
     * @return String 置換処理後の文字列
     * @param input 処理の対象の文字列
     * @param pattern 置換前の文字列
     * @param replacement 置換後の文字列
     * @author KCCS fujimura
     * @since 2017/02/15
     */
    private String substitute(final String input, final String pattern, final String replacement) {
        // このAPIを使用する前提条件を定義
        if (input == null) {
            throw new IllegalArgumentException("input is 'null'!");
        }
        if (pattern == null) {
            throw new IllegalArgumentException("pattern is 'null'!");
        }
        if (replacement == null) {
            throw new IllegalArgumentException("replacement is 'null'!");
        }

        // 置換対象文字列が存在する場所を取得
        int index = input.indexOf(pattern);

        // 置換対象文字列が存在しなければ終了
        if (index == -1) {
            return input;
        }

        // 処理を行うための StringBuffer
        StringBuilder buffer = new StringBuilder();

        buffer.append(input.substring(0, index)).append(replacement);

        if (index + pattern.length() < input.length()) {
            // 残りの文字列を再帰的に置換
            String rest = input.substring(index + pattern.length(), input.length());
            buffer.append(substitute(rest, pattern, replacement));
        }

        return buffer.toString();
    }
}
