/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.util.Date;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/08/24<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	SRC K.Ijuin<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * sr_koteifuryo_siji(工程不良指示)のモデルクラスです。
 *
 * @author SRC K.Ijuin
 * @since 2021/08/24
 */
public class SrKoteifuryoSiji {

    /**
     * 登録No
     */
    private String torokuno;    
    /**
     * 指示No
     */
    private String sijino;
    /**
     * 不良No
     */
    private String furyono;
    /**
     * 指示者ｺｰﾄﾞ
     */
    private String sijisyacode;
    /**
     * 指示日
     */
    private java.sql.Date sijibi;
    /**
     * 処置工程
     */
    private String syochikotei;
    /**
     * 指示内容
     */
    private String sijinaiyo;
    /**
     * 更新日時
     */
    private Date kosinnichiji;
    /**
     * 実績No
     */
    private String jissekino;

    /**
     * @return the torokuno
     */
    public String getTorokuno() {
        return torokuno;
    }

    /**
     * @param torokuno the torokuno to set
     */
    public void setTorokuno(String torokuno) {
        this.torokuno = torokuno;
    }

    /**
     * @return the sijino
     */
    public String getSijino() {
        return sijino;
    }

    /**
     * @param sijino the sijino to set
     */
    public void setSijino(String sijino) {
        this.sijino = sijino;
    }

    /**
     * @return the furyono
     */
    public String getFuryono() {
        return furyono;
    }

    /**
     * @param furyono the furyono to set
     */
    public void setFuryono(String furyono) {
        this.furyono = furyono;
    }

    /**
     * @return the sijisyacode
     */
    public String getSijisyacode() {
        return sijisyacode;
    }

    /**
     * @param sijisyacode the sijisyacode to set
     */
    public void setSijisyacode(String sijisyacode) {
        this.sijisyacode = sijisyacode;
    }

    /**
     * @return the sijibi
     */
    public java.sql.Date getSijibi() {
        return sijibi;
    }

    /**
     * @param sijibi the sijibi to set
     */
    public void setSijibi(java.sql.Date sijibi) {
        this.sijibi = sijibi;
    }

    /**
     * @return the syochikotei
     */
    public String getSyochikotei() {
        return syochikotei;
    }

    /**
     * @param syochikotei the syochikotei to set
     */
    public void setSyochikotei(String syochikotei) {
        this.syochikotei = syochikotei;
    }

    /**
     * @return the sijinaiyo
     */
    public String getSijinaiyo() {
        return sijinaiyo;
    }

    /**
     * @param sijinaiyo the sijinaiyo to set
     */
    public void setSijinaiyo(String sijinaiyo) {
        this.sijinaiyo = sijinaiyo;
    }

    /**
     * @return the kosinnichiji
     */
    public Date getKosinnichiji() {
        return kosinnichiji;
    }

    /**
     * @param kosinnichiji the kosinnichiji to set
     */
    public void setKosinnichiji(Date kosinnichiji) {
        this.kosinnichiji = kosinnichiji;
    }

    /**
     * @return the jissekino
     */
    public String getJissekino() {
        return jissekino;
    }

    /**
     * @param jissekino the jissekino to set
     */
    public void setJissekino(String jissekino) {
        this.jissekino = jissekino;
    }

}
