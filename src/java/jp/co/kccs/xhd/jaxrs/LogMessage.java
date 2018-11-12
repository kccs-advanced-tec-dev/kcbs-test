/*
 * Copyright 2017 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.jaxrs;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DBシステム(コンデンサ)<br>
 * <br>
 * 変更日	2017/07/13<br>
 * 計画書No	MB1703-DS019<br>
 * 変更者	KCCS R.Fujimura<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */

/**
 * ログ出力用のメッセージを構成するクラスです。
 *
 * @author KCCS R.Fujimura
 * @since 2017/07/07
 */
public class LogMessage {
    
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
