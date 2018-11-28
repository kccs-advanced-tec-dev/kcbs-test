/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd;

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
 * 端末モデルを操作するクラスです。
 *
 * @author KCCS D.Yanagida
 * @since 2018/04/24
 */
public class GetModel {    
    private String model = null;
    private String submodel = null;
    
    /**
     * コンストラクタ
     * 
     * @param uAgent エージェント
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public GetModel(String uAgent) {
        if (uAgent.contains("Windows NT") && uAgent.contains("Trident/7.0; rv:11")) {
            model = "ie11";
        } else if (uAgent.contains("Windows NT") && uAgent.contains("Firefox")) {
            model = "firefox";
        } else if (uAgent.contains("Windows NT") && uAgent.contains("Chrome")) {
            model = "chrome";
        } else if (uAgent.contains("iPad")) {
            model = "ipad";
        } else if (uAgent.contains("Android")) {
            model = "android";            
            if (uAgent.contains("P01T_1")) {
                submodel = "P01T_1";
                //downloadFolder = "/sdcard/Download/docs/";
            } else if (uAgent.contains("PC-TE508BA")) {
                submodel = "PC-TE508BA";
                //downloadFolder = "/Download/docs/";
            } else if (uAgent.contains("Nexus 7")) {
                submodel = "Nexus 7";
                //downloadFolder = "/sdcard/Download/docs/";
            } else if (uAgent.contains("HuaweiMediaPad")) {
                submodel = "HuaweiMediaPad";
            } else {
                //downloadFolder = "/sdcard/Download/docs/";
            }
        } else if (uAgent.contains("Linux") && uAgent.contains("Chrome")) {
            model = "linux_chrome";
        } else if (uAgent.contains("Linux") && uAgent.contains("Firefox")) {
            model = "linux_firefox";
        } else {
            model = "ie11";
        }
    } 
    
    /**
     * モデル(端末)を取得します。
     * 
     * @return 端末情報
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public String getModel() {
        return model;
    }
    
    /**
     * サブモデルを取得します。
     * 
     * @return 端末情報
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public String getSubmodel() {
        return submodel;
    }    
}
