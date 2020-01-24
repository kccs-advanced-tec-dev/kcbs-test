/*
 * Copyright 2017 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.jaxrs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
            java.sql.Date sqlNow = new java.sql.Date(System.currentTimeMillis());
            java.util.Date utilDate = sqlNow;
            java.sql.Date registDate = new java.sql.Date(utilDate.getTime());
            
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
                deleteflag = (int)fxhdd07_delflag.get("deleteflag") + 1;
            }
            
            // 削除フラグ = 0 のデータが存在する場合はUPDATE
            String sqlUpd = "UPDATE FXHDD07 SET koshin_date = ?, deleteflag = ? "
                          + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND deleteflag = 0";
            List<Object> paramsUpd = 
                    new ArrayList<>(Arrays.asList(registDate, deleteflag, param.getKojyo(), param.getLotno(), param.getEdaban()));
            
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
            
            List<Object> paramIns = 
                    new ArrayList<>(Arrays.asList(
                            param.getKojyo()
                            , param.getLotno()
                            , param.getEdaban()
                            , param.getGouki()
                            , param.getBunruiairatu()
                            , param.getCdcontactatu()
                            , param.getIrcontactatu()
                            , param.getTan()
                            , param.getSokuteisyuhasuu()
                            , param.getSokuteidenatu()
                            , param.getPcdenatu1()
                            , param.getPcjudenjikan1()
                            , param.getPcdenatu2()
                            , param.getPcjudenjikan2()
                            , param.getPcdenatu3()
                            , param.getPcjudenjikan3()
                            , param.getPcdenatu4()
                            , param.getPcjudenjikan4()
                            , param.getIrdenatu1()
                            , param.getIrhanteiti1_low()
                            , param.getIrhanteiti1()
                            , param.getIrjudenjikan1()
                            , param.getIrdenatu2()
                            , param.getIrhanteiti2_low()
                            , param.getIrhanteiti2()
                            , param.getIrjudenjikan2()
                            , param.getIrdenatu3()
                            , param.getIrhanteiti3_low()
                            , param.getIrhanteiti3()
                            , param.getIrjudenjikan3()
                            , param.getIrdenatu4()
                            , param.getIrhanteiti4_low()
                            , param.getIrhanteiti4()
                            , param.getIrjudenjikan4()
                            , param.getIrdenatu5()
                            , param.getIrhanteiti5_low()
                            , param.getIrhanteiti5()
                            , param.getIrjudenjikan5()
                            , param.getIrdenatu6()
                            , param.getIrhanteiti6_low()
                            , param.getIrhanteiti6()
                            , param.getIrjudenjikan6()
                            , param.getIrdenatu7()
                            , param.getIrhanteiti7_low()
                            , param.getIrhanteiti7()
                            , param.getIrjudenjikan7()
                            , param.getIrdenatu8()
                            , param.getIrhanteiti8_low()
                            , param.getIrhanteiti8()
                            , param.getIrjudenjikan8()
                            , param.getRdcrange1()
                            , param.getRdchantei1()
                            , param.getRdcrange2()
                            , param.getRdchantei2()
                            , param.getBin1countersuu()
                            , param.getBin2countersuu()
                            , param.getBin3countersuu()
                            , param.getBin4countersuu()
                            , param.getBin5countersuu()
                            , param.getBin6countersuu()
                            , param.getBin7countersuu()
                            , param.getBin8countersuu()
                            , param.getBin5setteiti()
                            , param.getBin6setteiti()
                            , param.getBin7setteiti()
                            , param.getBin8setteiti()
                            , registDate
                            , null
                            , 0
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
