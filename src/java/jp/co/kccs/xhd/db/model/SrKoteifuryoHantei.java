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
 * sr_koteifuryo_Hantei(工程不良判定)のモデルクラスです。
 *
 * @author Ichiki
 * @since 2022/02/21
 */
public class SrKoteifuryoHantei {
    
    
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
     * 判定者ｺｰﾄﾞ
     */
    private String hanteisyacode;
    /**
     * 判定日
     */
    private java.sql.Date hanteibi;
    /**
     * 判定
     */
    private String hantei;
    /**
     * コメント
     */
    private String comment;
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
     * @return the hanteisyacode
     */
    public String getHanteisyacode() {
        return hanteisyacode;
    }

    /**
     * @param hanteisyacode the hanteisyacode to set
     */
    public void setHanteisyacode(String hanteisyacode) {
        this.hanteisyacode = hanteisyacode;
    }

    /**
     * @return the hanteibi
     */
    public java.sql.Date getHanteibi() {
        return hanteibi;
    }

    /**
     * @param hanteibi the hanteibi to set
     */
    public void setHanteibi(java.sql.Date hanteibi) {
        this.hanteibi = hanteibi;
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

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
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
