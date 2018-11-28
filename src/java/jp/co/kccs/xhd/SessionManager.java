/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * プログラム名	メニュー<br>
 * <br>
 * 変更日	2018/04/23<br>
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
 * セッション情報を制御するクラスです。
 *
 * @author KCCS D.Yanagida
 * @since 2018/04/24
 */
@Named
@RequestScoped
public class SessionManager {
    private static final Logger LOGGER = Logger.getLogger(SessionManager.class.getName());
    //MAC Address
    private String macAddr;
    private String autoLogin;
    
    @Inject
    private SessionInfo sessionInfo;
    @Inject
    private SelectParam selectParam;

    private  HashMap<String, String> parameterMap;
    
    /**
     * DataSource
     */
    @Resource(mappedName = "jdbc/DocumentServer")
    private transient DataSource dataSource;
 
    /**
     * ログアウトします。
     * 
     * @return ログイン用URL文字列
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public String logOut() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        if (session != null) {
            try {
                session.invalidate();
            } catch(IllegalStateException e) {
                LOGGER.log(Level.SEVERE, null, e);
            }
        }
        // goto login
        return "/login.xhtml?faces-redirect=true";
    }
    
    /**
     * ログアウトします。
     * 
     * @return ログイン用URL文字列
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public String getLogOut() {
        return logOut();
    }
    
    /**
     * ユーザー権限を取得します。
     * 
     * @param role ロール
     * @return メニューリスト
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    private boolean isUserInRole(String role) {
        return FacesContext.getCurrentInstance().getExternalContext().isUserInRole(role);
    }
    
    /**
     * フラッシュにキーと値を追加します。
     * 
     * @param key キー
     * @param obj 値
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public void putFlash(String key, Object obj) {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.put(key, obj);
    }

    /**
     * レルム認証後のログインチェックを行います。
     * 
     * @return URL
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public String validateUser() {
        if (sessionInfo.isFirstTime()) {
            
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            String login_user_name = (String) session.getAttribute("login_user_name");

            if (null == login_user_name || "".equals(login_user_name)) {
                // ユーザー情報をセッションに設定
                if (!this.setUser2session()) {
                    // ユーザー情報のセッション設定に失敗した場合エラー終了
                    return "/error.xhtml?faces-redirect=true";
                }
            } 
            
            LOGGER.log(Level.INFO, "[{0}]:Realm success", macAddr);
            sessionInfo.setFirstTime(false);
            //MAC Address
            sessionInfo.setMacAddr(macAddr);
            FacesContext context = FacesContext.getCurrentInstance();
            String userName = context.getExternalContext().getUserPrincipal().getName();
            userName = userName.toUpperCase();
            //社内or社外判定
            //myParam
            ResourceBundle myParam = context.getApplication().getResourceBundle(context, "myParam");
            String in_out_flag = myParam.getString("in_out_flag");
            sessionInfo.setInOutFlag(in_out_flag);

            parameterMap = (HashMap<String, String>)session.getAttribute("parameter");
            if (parameterMap == null) {
                selectParam.getValue("key", session);
                parameterMap = (HashMap<String, String>)session.getAttribute("parameter");
            }
            String strUserList = "";
            String userAllow = "";
            String strMacList = "";
            String macAllow = "";
            if ("in".equals(in_out_flag)) {
                //社内
                //接続ユーザーリスト取得
                strUserList = parameterMap.get("IN_USER_LIST");
                //許可or不許可ユーザー判定
                userAllow = parameterMap.get("IN_USER_ALLOW");
                //Macアドレスリスト取得
                strMacList = parameterMap.get("IN_MAC_LIST");
                //接続許可or不許可Macアドレス
                macAllow = parameterMap.get("IN_MAC_ALLOW");
            } else if ("out".equals(in_out_flag)) {
                //社外
                //接続ユーザーリスト取得
                strUserList = parameterMap.get("OUT_USER_LIST");
                //許可or不許可ユーザー判定
                userAllow = parameterMap.get("OUT_USER_ALLOW");
                //Macアドレスリスト取得
                strMacList = parameterMap.get("OUT_MAC_LIST");
                //接続許可or不許可Macアドレス
                macAllow = parameterMap.get("OUT_MAC_ALLOW");
            }
                        
            //許可ユーザーチェック
            String login_users[] = strUserList.split(",");
            for (int i = 0; i < login_users.length; i++) {
                login_users[i] = login_users[i].trim();
            }
            userAllow = userAllow.toUpperCase();
            boolean isError = checkAllowUserORMacAddress(userAllow, login_users, userName);
            if (isError) {
                LOGGER.log(Level.INFO, "[{0}]:Allow user check error", macAddr);
                logOut();
                return "/error.xhtml?faces-redirect=true";
            }
            LOGGER.log(Level.INFO, "[{0}]:Allow user check ok", macAddr);
            
             //Insert Start[MB1703-DS019] D.Yanagida 2017.07.13
            if ("NA".equals(macAddr) 
                    && context.getExternalContext().isUserInRole("admins")) {
                //return "/admin/manage.xhtml?faces-redirect=true";
                return "/secure/manage.xhtml?faces-redirect=true";
            }
            //Insert End[MB1703-DS019] D.Yanagida 2017.07.13
            
            //許可Macアドレスチェック
            String login_MacAddress[] = strMacList.split(",");
            for (int i = 0; i < login_MacAddress.length; i++) {
                login_MacAddress[i] = login_MacAddress[i].trim();
            }
            macAllow = macAllow.toUpperCase();
            isError = checkAllowUserORMacAddress(macAllow, login_MacAddress, macAddr);
            if (isError) {
                LOGGER.log(Level.INFO, "[{0}]:Allow macAddr check error", macAddr);
                logOut();
                return "/error.xhtml?faces-redirect=true";
            }
            LOGGER.log(Level.INFO, "[{0}]:Allow macAddr check ok", macAddr);
            LOGGER.log(Level.INFO, "[{0}]:Login OK", macAddr);
        } else {
            return null;
        }
        return null;
    }

    /**
     * Macアドレスを取得します。
     * 
     * @return Macアドレス
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public String getMacAddr() {
        return macAddr;
    }

    /**
     * Macアドレスを設定します。
     * 
     * @param macAddr Macアドレス
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    /**
     * Autoログインを取得します。
     * 
     * @return autoログイン
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public String getAutoLogin() {
        return autoLogin;
    }

    /**
     * Autoログインを設定します。
     * 
     * @param autoLogin 自動ログイン
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public void setAutoLogin(String autoLogin) {
        this.autoLogin = autoLogin;
    }
    
    /**
     * ユーザー情報をセッションに設定します。
     * 
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public boolean setUser2session() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            //login user name
            String userName = context.getExternalContext().getUserPrincipal().getName();
            //session変数にuserNameを設定する
            HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
            session.setAttribute("login_user_name", userName);

            // 前回のログインユーザのパラメータが残っている場合はクリアする。
            HashMap<String, String> map = (HashMap<String, String>) session.getAttribute("parameter");
            if (map != null) {
                session.setAttribute("parameter", null);
            }
        
            // ユーザーグループを取得する
            List<String> groups = new ArrayList<>();
            
            QueryRunner queryRunner = new QueryRunner(dataSource);
            String sql = "SELECT usergroup FROM fxhbm02 WHERE user_name = ?";
            List<Object> params = new ArrayList<>();
            params.add(userName);

            ResultSetHandler rsh = new MapListHandler();
            List result =  (List)queryRunner.query(sql, rsh, params.toArray());
            for (Iterator i = result.iterator(); i.hasNext();) {
                HashMap m = (HashMap)i.next();
                groups.add(m.get("usergroup").toString());
            }
            
            // session変数にusergroupを設定する
            session.setAttribute("login_user_group", groups);
        
            return true;
            
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            logOut();
            return false;
        }
    }
    
    /**
     * 許可ユーザー、接続許可Macアドレスかどうかを判定します。
     * 
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    private boolean checkAllowUserORMacAddress(String allow, String[] list, String data) {
        
        if ("TRUE".equals(allow) || "YES".equals(allow)
                || "ON".equals(allow)) {
            //allow
            boolean errorFlag = true;
            for (int i = 0; i < list.length; i++) {
                if (list[i].equals(data)) {
                    errorFlag = false;
                }
            }
            //許可ユーザリストに一致しなければ、ログアウト処理後、エラー画面へ遷移
            return errorFlag;
        } else if ("FALSE".equals(allow) || "NO".equals(allow)
                || "OFF".equals(allow)) {
            //deny
            boolean errorFlag = false;
            for (int i = 0; i < list.length; i++) {
                if (list[i].equals(data)) {
                    //不許可ユーザリストに一致していれば、ログアウト処理後、エラー画面へ遷移
                    errorFlag = true;
                }
            }
            return errorFlag;
        } else {
            //設定値ミスの為、エラーとする
            return true;
        }
    }
}
