/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
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
 * 変更日	2018/11/13<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	ﾛｯﾄｶｰﾄﾞ電子化対応<br>
 * <br>
 * ===============================================================================<br>
 */

@Entity
@Table(name = "fxhbm03")
@NamedQueries({
    @NamedQuery(name = "Parameter.findParameter", 
            query = "SELECT p FROM Parameter p WHERE p.parameterPK.user_name = :name"),
    @NamedQuery(name = "Parameter.findParameter2", 
            query = "SELECT p FROM Parameter p WHERE p.parameterPK.user_name = :name AND p.parameterPK.key = :key")})
/**
 * パラメータマスタのBeanクラスです。
 *
 * @author KCCS D.Yanagida
 * @since 2018/04/24
 */
public class Parameter implements Serializable{

    @EmbeddedId
    private ParameterPK parameterPK;
    private static final long serialVersionUID = 1L;
    /** 登録者 */
    @Size(max = 20)
    @Column(name = "torokusha")
    private String torokusha;
    /** 登録日 */
    @Column(name = "toroku_date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date toroku_date;
    /** 更新者 */
    @Size(max = 20)
    @Column(name = "koshinsha")
    private String koshinsha;
    /** 更新日 */
    @Column(name = "koshin_date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date koshin_date;
    /** パラメータデータ */
    @Size(max = 255)
    @NotNull
    @Column(name = "data")
    private String data;

    /**
     * デフォルトコンストラクタ
     *
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public Parameter() {
    }
    
    /**
     * コンストラクタ
     * 
     * @param parameterPK {@link ParameterPK}オブジェクト
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public Parameter(ParameterPK parameterPK) {
        this.parameterPK = parameterPK;
    }
    
    public Parameter(String user_name, String key) {
        this.parameterPK = new ParameterPK(user_name, key);
    }
    
    /**
     * 登録者を取得します。
     * 
     * @return 登録者
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public String getTorokusha() {
        return torokusha;
    }

    /**
     * 登録者を設定します。
     * 
     * @param torokusha 登録者
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public void setTorokusha(String torokusha) {
        this.torokusha = torokusha;
    }

    /**
     * 登録日を取得します。
     * 
     * @return 登録日
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public Date getToroku_date() {
        return toroku_date;
    }

    /**
     * 登録日を設定します。
     * 
     * @param toroku_date 登録日
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public void setToroku_date(Date toroku_date) {
        this.toroku_date = toroku_date;
    }

    /**
     * 更新者を取得します。
     * 
     * @return 更新者
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public String getKoshinsha() {
        return koshinsha;
    }

    /**
     * 更新者を設定します。
     * 
     * @param koshinsha 更新者
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public void setKoshinsha(String koshinsha) {
        this.koshinsha = koshinsha;
    }

    /**
     * 更新日を取得します。
     * 
     * @return 更新日
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public Date getKoshin_date() {
        return koshin_date;
    }

    /**
     * 更新日を設定します。
     * 
     * @param koshin_date 更新日
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public void setKoshin_date(Date koshin_date) {
        this.koshin_date = koshin_date;
    }

    /**
     * パラメータデータを取得します。
     * 
     * @return ユーザー
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public String getData() {
        return data;
    }

    /**
     * パラメータデータを設定します。
     * 
     * @param data パラメータデータ
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public void setData(String data) {
        this.data = data;
    }
    
    /**
     * {@link ParameterPK}を取得します。
     * 
     * @return パラメータ
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public ParameterPK getParameterPK() {
        return parameterPK;
    }

    /**
     * {@link ParameterPK}を設定します。
     * 
     * @param parameterPK パラメータ
     * @author KCCS D.Yanagida
     * @since 2018/04/24
     */
    public void setParameterPK(ParameterPK parameterPK) {
        this.parameterPK = parameterPK;
    }
}
