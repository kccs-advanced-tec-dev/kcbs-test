/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd;

import java.io.Serializable;
import java.util.HashMap;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
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
 * セッション情報を表すクラスです。
 *
 * @author KCCS D.Yanagida
 * @since 2018/04/24
 */
@Named
@SessionScoped
public class SessionInfo implements Serializable {

    private final HashMap map = new HashMap();
    
    /**
     * 初期化を行います。
     * 
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    @PostConstruct
    public void Init() {
        //remote IP address
        setFirstTime(true);
        setIpAddress(getRemoteAddress());
        //NA
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle messages = context.getApplication().getResourceBundle(context, "myMsg");
        setMacAddr(messages.getString("NotAvailable"));
    }

    /**
     * マップを取得します。
     * 
     * @return マップ
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public HashMap getMap() {
        return map;
    }

    /**
     * メニューパラメータを取得します。
     * 
     * @author KCCS D.Yanagida
     * @return 送りパラメータ
     * @since 2018/04/24
     */
    public String getMenuParam() {
        return (String) map.get("menu_param");
    }
    
    /**
     * 送りパラメータを設定します。
     * 
     * @param okuri_param 送りパラメータ
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public void setMenuParam(String okuri_param) {
        map.put("menu_param", okuri_param);
    }
    
    /**
     * 初回かどうかを判定します。
     * 
     * @author KCCS D.Yanagida
     * @return 初回判定
     * @since 2018/04/24
     */
    public boolean isFirstTime() {
        return (boolean) map.get("first_time");
    }
    
    /**
     * 初回かどうかを判定します。
     * 
     * @param first_time 初回フラグ
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public void setFirstTime(boolean first_time) {
        map.put("first_time", first_time);
    }

    /**
     * IPアドレスを取得します。
     * 
     * @author KCCS D.Yanagida
     * @return IPアドレス
     * @since 2018/04/24
     */
    public String getIpAddress() {
        return (String) map.get("ipAddress");
    }

    /**
     * IPアドレスを設定します。
     * 
     * @param ipAddress IPアドレス
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    private void setIpAddress(String ipAddress) {
        map.put("ipAddress", ipAddress);
    }
    
    /**
     * クライアントのIPアドレスを取得します。
     * http://stackoverflow.com/questions/12324466/finding-user-ip-address
     * 
     * @return IPアドレス
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public static String getRemoteAddress() {
        
        HttpServletRequest req = (HttpServletRequest) FacesContext
                            .getCurrentInstance()
                            .getExternalContext()
                            .getRequest();
        
        String ipAddress = req.getHeader("X-FORWARDED-FOR");
        if (ipAddress != null) {
            ipAddress = ipAddress.replaceFirst(",.*", "");  // cares only about the first IP if there is a list
        } else {
            ipAddress = req.getRemoteAddr();
        }
        return ipAddress;
    }
    
    /**
     * Macアドレスを取得します。
     * 
     * @author KCCS D.Yanagida
     * @return Macアドレス
     * @since 2018/04/24
     */
    public String getMacAddr() {
        return (String) map.get("macAddr");
    }

    /**
     * Macアドレスを設定します。
     * 
     * @param macAddr Macアドレス
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public void setMacAddr(String macAddr) {
        map.put("macAddr", macAddr);
    }
    
    /**
     * 社内/社外フラグを取得します。
     * 
     * @author KCCS D.Yanagida
     * @return 社内/社外フラグ
     * @since 2018/04/24
     */
    public String getInOutFlag() {
        return (String) map.get("in_out_flag");
    }

    /**
     * 社内/社外フラグを設定します。
     * 
     * @param in_out_flag 社内/社外フラグ
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public void setInOutFlag(String in_out_flag) {
        map.put("in_out_flag", in_out_flag);
    }
}