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
 * sr_koteifuryo_kekka(工程不良結果)のモデルクラスです。
 *
 * @author SRC K.Ijuin
 * @since 2021/08/24
 */
public class SrKoteifuryoKekka {
    
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
     * 処置者ｺｰﾄﾞ
     */
    private String syotisyacode;
    /**
     * 処置日
     */
    private java.sql.Date syotibi;
    /**
     * 処置内容
     */
    private String syotinaiyo;
    /**
     * 更新日時
     */
    private Date kosinnichiji;
    /**
     * 実績No
     */
    private String jissekino;
    /**
     * 判定
     */
    private String hantei;

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
     * @return the syotisyacode
     */
    public String getSyotisyacode() {
        return syotisyacode;
    }

    /**
     * @param syotisyacode the syotisyacode to set
     */
    public void setSyotisyacode(String syotisyacode) {
        this.syotisyacode = syotisyacode;
    }

    /**
     * @return the syotibi
     */
    public java.sql.Date getSyotibi() {
        return syotibi;
    }

    /**
     * @param syotibi the syotibi to set
     */
    public void setSyotibi(java.sql.Date syotibi) {
        this.syotibi = syotibi;
    }

    /**
     * @return the syotinaiyo
     */
    public String getSyotinaiyo() {
        return syotinaiyo;
    }

    /**
     * @param syotinaiyo the syotinaiyo to set
     */
    public void setSyotinaiyo(String syotinaiyo) {
        this.syotinaiyo = syotinaiyo;
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

    /**
     * @return the hantei
     */
    public String getHantei() {
        return hantei;
    }

    /**
     * @param hantei the hantei to set
     */
    public void setHantei(String hantei) {
        this.hantei = hantei;
    }
    
}
