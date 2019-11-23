/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd;

import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpSession;
import jp.co.kccs.xhd.db.Parameter;
import jp.co.kccs.xhd.db.ParameterEJB;

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
 * 変更日	2019/11/04<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	K.Hisanaga<br>
 * 変更理由	各種機能メニュー追加処理<br>
 * <br>
 * ===============================================================================<br>
 */

/**
 * パラメータ(マスタ)を操作するクラスです。
 */
@Stateless
public class SelectParam {
    @EJB
    private ParameterEJB parameterEJB;
    
    /**
     * パラメータマスタから、キー、ユーザー名が一致する情報を取得します。<BR>
     * ユーザー名はセッションコンテキストの情報(ログインユーザー情報)を参照します。<BR>
     * ユーザー名が一致するデータが存在しない場合、"common_user"のパラメータ参照を試行します。
     * 
     * @param key キー
     * @param session セッション
     * @return 値
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public String getValue(String key, HttpSession session) {
    
        //キーは大文字
        key = key.toUpperCase();
        
        //parameterMap
        HashMap<String, String> parameterMap;
        List<Parameter> paramListForUser;
        List<Parameter> paramListCommon;
        String masterKey;
        String masterValue;
        parameterMap = (HashMap<String, String>) session.getAttribute("parameter");
        //parameterMapがなければ作成
        if (parameterMap == null) {
            parameterMap = new HashMap<>();
        }
        
        String value = parameterMap.get(key);
        if (value == null) {
            //テーブルからパラメータを読むためにログインユーザ名が必要(starting.xhtmlでセッション変数に登録)
            String login_user_name = (String) session.getAttribute("login_user_name");
            
            //パラメータマスタを検索する
            paramListForUser = parameterEJB.findParameter(login_user_name);
            paramListCommon = parameterEJB.findParameter("common_user");
            
            for(Parameter param : paramListForUser) {
                //masterKey = param.getKey().toUpperCase();
                masterKey = param.getParameterPK().getKey().toUpperCase();
                masterValue = param.getData().toUpperCase();
                
                parameterMap.put(masterKey, masterValue);
            }
            
            for (Parameter param : paramListCommon) {
                //masterKey = param.getKey().toUpperCase();
                masterKey = param.getParameterPK().getKey().toUpperCase();
                masterValue = param.getData().toUpperCase();
                if (isEmpty(parameterMap.get(masterKey))) {
                    parameterMap.put(masterKey, masterValue);
                }
            }
            
            session.setAttribute("parameter", parameterMap);
            
            value = parameterMap.get(key);
        }
        return value;
    }
    
    /**
     * 取得したパラメータをbool型で返却します。<BR>
     * キー値が存在しない場合FALSEを返却します。
     * 
     * @param key パラメータキー
     * @param session セッション
     * @return boolean true or false
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public boolean getParameterBoolean(String key, HttpSession session) {
        String value = getValue(key, session);
        
        if (isEmpty(value)) {
            return false;
        } else {
            return "1".equals(value) || "ON".equalsIgnoreCase(value);
        }
    }
    
    /**
     * 空文字かどうかチェックします。
     * 
     * @param value 値
     * @return 空文字(true)/値あり(false)
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    private static boolean isEmpty(String value) {
        boolean bool = false;
        if (value == null || "".equals(value)) {
            bool = true;
        }
        return bool;
    }
    
    
    /**
     * パラメータマスタから、キー、ユーザー名が一致する情報を取得します。<BR>
     * 
     * @param key キー
     * @param session セッション
     * @return 値
     * @author SYSNAVI K.Hisanaga
     * @since 2019/11/06
     */
    public boolean existKey(String key, HttpSession session) {
        //キーは大文字
        key = key.toUpperCase();
        
        //parameterMap
        HashMap<String, String> parameterMap = (HashMap<String, String>) session.getAttribute("parameter");
        if (parameterMap == null || parameterMap.get(key) == null) {
            // パラメータの読み込み自体されていない可能性がある為、読み込み処理を実行して取得しなおす。
            getValue(key, session);
            parameterMap = (HashMap<String, String>) session.getAttribute("parameter");
        }
        return parameterMap.containsKey(key);
    }
}
