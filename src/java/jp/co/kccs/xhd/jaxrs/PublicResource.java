/*
 * Copyright 2017 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.jaxrs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
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
    @Resource(mappedName="jdbc/DocumentServer")
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
        
        try {
            conn = dataSource.getConnection();
            String executeSQL = "INSERT INTO fxhbd91 (torokusha, toroku_date, koshinsha, "
                    + "koshin_date, mac_address, gamen, ichi_info) VALUES(?, ?, ?, ?, ?, ?, ?)";
            
            ps = conn.prepareStatement(executeSQL);
            ps.setString(1, null);
            ps.setTimestamp(2, new java.sql.Timestamp(new Date().getTime()));
            ps.setString(3, null);
            ps.setTimestamp(4, null);
            ps.setString(5, escapeSQL(blankToNull(stringLeft(termNo, 255))));
            ps.setString(6, escapeSQL(blankToNull(stringLeft(page, 255))));
            ps.setString(7, escapeSQL(blankToNull(stringLeft(location, 255))));

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
