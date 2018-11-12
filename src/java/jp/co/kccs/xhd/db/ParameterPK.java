/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
 * ===============================================================================<br>
 */

/**
 * パラメータマスタのPrimaryKeyを表すクラスです。
 *
 * @author KCCS D.Yanagida
 * @since 2018/04/24
 */
@Embeddable
public class ParameterPK implements Serializable {
    /** ユーザー */
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "user_name")
    private String user_name;
    /** ユーザーグループ */
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "key")
    private String key;

    /**
     * デフォルトコンストラクタ
     *
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public ParameterPK() {
    }

    /**
     * コンストラクタ
     * 
     * @param user_name ユーザー
     * @param key パラメータキー
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public ParameterPK(String user_name, String key) {
        this.user_name = user_name;
        this.key = key;
    }
    
    /**
     * ユーザーを取得します。
     * 
     * @return ユーザー名
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public String getUser_name() {
        return user_name;
    }

    /**
     * ユーザーを設定します。
     * 
     * @param user_name ユーザー名
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    /**
     * パラメータキーを取得します。
     * 
     * @return ユーザー名
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public String getKey() {
        return key;
    }

    /**
     * パラメータキーを設定します。
     * 
     * @param key パラメータキー
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public void setKey(String key) {
        this.key = key;
    }
    
    /**
     * @inheritDoc
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (user_name != null ? user_name.hashCode() : 0);
        hash += (key != null ? key.hashCode() : 0);
        return hash;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ParameterPK)) {
            return false;
        }
        ParameterPK other = (ParameterPK) object;
        if ((!this.user_name.equals(other.user_name))) {
            return false;
        }
        return (this.key.equals(other.key));
    }

    /**
     * @inheritDoc
     */
    @Override
    public String toString() {
        return "jp.co.kccs.fmon.db.UserGroupsPK[ machineid=" + user_name + ", dataid=" + key + " ]";
    }
}
