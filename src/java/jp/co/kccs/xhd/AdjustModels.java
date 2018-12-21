/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import jp.co.kccs.xhd.util.DBUtil;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.StringUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2018/04/24<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	KCCS D.Yanagida<br>
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
 * タブレット画面に関する情報を纏めたクラスです。
 *
 * @author KCCS D.Yanagida
 * @since 2018/04/24
 */
@Named
@SessionScoped
//@Stateful
public class AdjustModels implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(AdjustModels.class.getName());
    
    private String uAgent;
    private String model;
    private String submodel;
    private String clientDevice;
    private Double fontSize = 20.0;
    private int menuWidth = 720;
    private Double menuFontSize = 32.0;
    private String external = "external";    
    
    @Resource(mappedName = "jdbc/DocumentServer")
    private transient DataSource dataSource;
    
    /**
     * コンストラクタ
     * 
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public AdjustModels() {
        // user-agent
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        uAgent = request.getHeader("user-agent");
        // model
        GetModel getModel = new GetModel(uAgent);
        model = getModel.getModel();
        submodel = getModel.getSubmodel();
    }
    
    /**
     * デバイス設定
     * @param macAddr MACアドレス
     */
    public void setClientDevice(String macAddr) {
        // MACアドレスをキーに表示設定用端末マスタからデバイス設定を読み込む
        // ※デバイスがマスタ未登録の場合、dp最小設定を適用
        String device = "W960H600";
        try {
            QueryRunner queryRunner = new QueryRunner(dataSource);
            String sql = "SELECT tanmatsu_id, tanmatsu_model, tanmatsu_mei, hyouji_pattern "
                       + "FROM fxhdm06 WHERE tanmatsu_id = ? ";
            List<Object> params = new ArrayList<>();
            params.add(macAddr);
            
            DBUtil.outputSQLLog(sql, params.toArray(), Logger.global);
            Map fxhdm06 = queryRunner.query(sql, new MapHandler(), params.toArray());
            if (null != fxhdm06 && !fxhdm06.isEmpty()) {
                device = StringUtil.nullToBlank(fxhdm06.get("hyouji_pattern"));
            }
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
        
        this.clientDevice = device;
        
        // デバイスに応じた画面スタイルを適用する
        if ("PC".equals(device)) {
            // PC(IE)の場合のスタイルを適用
            
        } else {
            // タブレットの場合のスタイルを適用
            
            // 共通
            fontSize = 32.0;
            menuFontSize = 32.0;
            menuWidth = 740;
            
            // 個別
            if ("W960H600".equals(device)) {
                menuFontSize = 23.0;
                menuWidth = 590;
            } else if ("W1024H768".equals(device)) {
                menuWidth = 540;
            } else if ("W1280H800".equals(device)) {
                menuWidth = 540;
            }
        }
    }

    /**
     * 表示パターンを取得します。
     * 
     * @return 表示パターン
     */
    public String getClientDevice() {
        return clientDevice;
    }

    /**
     * モデルを取得します。
     * 
     * @return モデル
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public String getModel() {
        return model;
    }
    
    /**
     * メニュー画面の幅を取得します。
     * 
     * @return 幅
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public int getMenuWidth() {
        return menuWidth;
    }

    /**
     * フォントサイズを取得します。
     * 
     * @return フォントサイズ
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public Double getFontSize() {
        return fontSize;
    }

    /**
     * Android操作で使用する値を取得します。
     * 
     * @return external
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public String getExternal() {
        return external;
    }

    /**
     * サブモデルを取得します。
     * 
     * @return パネル幅
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public String getSubmodel() {
        return submodel;
    }

    /**
     * メニューフォントサイズを取得します。
     * 
     * @return フォントサイズ
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public Double getMenuFontSize() {
        return menuFontSize;
    }
}
