/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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
 * パラメータマスタのEJBクラスです。
 *
 * @author KCCS D.Yanagida
 * @since 2018/04/24
 */
@Stateless
public class ParameterEJB {
    @PersistenceContext
    private EntityManager entityManager;
    
    /**
     * パラメータを取得します。
     * 
     * @param username ユーザー名
     * @return パラメータデータ
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public List<Parameter> findParameter(String username) {
        TypedQuery<Parameter> query = entityManager.createNamedQuery("Parameter.findParameter", Parameter.class)
                .setParameter("name", username);
        return query.getResultList();
    }
    /**
     * パラメータを取得します。
     * 
     * @param username ユーザー名
     * @param key キー名
     * @return パラメータデータ
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public List<Parameter> findParameter(String username, String key) {
        TypedQuery<Parameter> query = entityManager.createNamedQuery("Parameter.findParameter2", Parameter.class)
                .setParameter("name", username).setParameter("key", key);
        return query.getResultList();
    }
}
