/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/09/29<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	863 K.Zhang<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * sr_gddatubai:外部電極焼成(脱ﾊﾞｲ)のモデルクラスです。
 *
 * @author 863 K.Zhang
 * @since  2019/09/29
 */
public class SrGddatubai {
    /**
     * 工場ｺｰﾄﾞ
     */
    private String kojyo;

    /**
     * ﾛｯﾄNo
     */
    private String lotno;

    /**
     * 枝番
     */
    private String edaban;

    /**
     * 回数
     */
    private Integer kaisuu;

    /**
     * KCPNO
     */
    private String kcpno;

     /**
     * 客先
     */
    private String tokuisaki;
    
    /**
     * ﾛｯﾄ区分
     */
    private String lotkubuncode;
    
    /**
     * ｵｰﾅｰ
     */
    private String ownercode;
    
    /**
     * ﾛｯﾄﾌﾟﾚ
     */
    private String lotpre;
    
    /**
     * 処理数
     */
    private Integer syorisuu;
    
    /**
     * 号機
     */
    private String gouki;
    
    /**
     * 温度
     */
    private String ondo;
    
    /**
     * 時間
     */
    private Integer jikan;
    
    /**
     * PTNNO
     */
    private String ptnno;
    
    /**
     * ｻﾔ枚数
     */
    private Integer sayamaisuu;

    /**
     * 開始日時
     */
    private Timestamp startdatetime;

    /**
     * 開始担当者
     */
    private String starttantosyacode;
    
    /**
     * 開始確認者
     */
    private String startkakuninsyacode;
    
    /**
     * 終了日時
     */
    private Timestamp enddatetime;
    
    /**
     * 終了担当者
     */
    private String endtantosyacode;
    
    /**
     * 備考1
     */
    private String biko1;
    
    /**
     * 備考2
     */
    private String biko2;
    
    /**
     * 登録日時
     */
    private Timestamp torokunichiji;

    /**
     * 更新日時
     */
    private Timestamp kosinnichiji;

    /**
     * revision
     */
    private Integer revision;

    /**
     * 削除ﾌﾗｸﾞ
     */
    private Integer deleteflag;

    /**
     * @return kojyo
     */
    public String getKojyo() {
        return kojyo;
    }

    /**
     * @param kojyo セットする kojyo
     */
    public void setKojyo(String kojyo) {
        this.kojyo = kojyo;
    }

    /**
     * @return lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * @param lotno セットする lotno
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * @return edaban
     */
    public String getEdaban() {
        return edaban;
    }

    /**
     * @param edaban セットする edaban
     */
    public void setEdaban(String edaban) {
        this.edaban = edaban;
    }

    /**
     * @return kaisuu
     */
    public Integer getKaisuu() {
        return kaisuu;
    }

    /**
     * @param kaisuu セットする kaisuu
     */
    public void setKaisuu(Integer kaisuu) {
        this.kaisuu = kaisuu;
    }

    /**
     * @return kcpno
     */
    public String getKcpno() {
        return kcpno;
    }

    /**
     * @param kcpno セットする kcpno
     */
    public void setKcpno(String kcpno) {
        this.kcpno = kcpno;
    }
    
    /**
     * 
     * @return tokuisaki
     */
    public String getTokuisaki() {
        return tokuisaki;
    }

    /**
     * 
     * @param tokuisaki セットする tokuisaki
     */
    public void setTokuisaki(String tokuisaki) {
        this.tokuisaki = tokuisaki;
    }

    /**
     * 
     * @return lotkubuncode
     */
    public String getLotkubuncode() {
        return lotkubuncode;
    }

    /**
     * 
     * @param lotkubuncode セットする lotkubuncode
     */
    public void setLotkubuncode(String lotkubuncode) {
        this.lotkubuncode = lotkubuncode;
    }

    /**
     * 
     * @return ownercode
     */
    public String getOwnercode() {
        return ownercode;
    }

    /**
     * 
     * @param ownercode セットする ownercode
     */
    public void setOwnercode(String ownercode) {
        this.ownercode = ownercode;
    }

    /**
     * 
     * @return lotpre
     */
    public String getLotpre() {
        return lotpre;
    }

    /**
     * 
     * @param lotpre セットする lotpre
     */
    public void setLotpre(String lotpre) {
        this.lotpre = lotpre;
    }

    /**
     * 
     * @return syorisuu
     */
    public Integer getSyorisuu() {
        return syorisuu;
    }

    /**
     * 
     * @param syorisuu セットする syorisuu
     */
    public void setSyorisuu(Integer syorisuu) {
        this.syorisuu = syorisuu;
    }

    /**
     * 
     * @return gouki
     */
    public String getGouki() {
        return gouki;
    }

    /**
     * 
     * @param gouki セットする gouki
     */
    public void setGouki(String gouki) {
        this.gouki = gouki;
    }

    /**
     * 
     * @return ondo
     */
    public String getOndo() {
        return ondo;
    }

    /**
     * 
     * @param ondo セットする ondo
     */
    public void setOndo(String ondo) {
        this.ondo = ondo;
    }

    /**
     * 
     * @return jikan
     */
    public Integer getJikan() {
        return jikan;
    }

    /**
     * 
     * @param jikan セットする jikan
     */
    public void setJikan(Integer jikan) {
        this.jikan = jikan;
    }

    /**
     * 
     * @return ptnno
     */
    public String getPtnno() {
        return ptnno;
    }

    /**
     * 
     * @param ptnno セットする ptnno
     */
    public void setPtnno(String ptnno) {
        this.ptnno = ptnno;
    }

    /**
     * 
     * @return sayamaisuu
     */
    public Integer getSayamaisuu() {
        return sayamaisuu;
    }

    /**
     * 
     * @param sayamaisuu セットする sayamaisuu
     */
    public void setSayamaisuu(Integer sayamaisuu) {
        this.sayamaisuu = sayamaisuu;
    }

    /**
     * 
     * @return startdatetime
     */
    public Timestamp getStartdatetime() {
        return startdatetime;
    }

    /**
     * 
     * @param startdatetime セットする startdatetime
     */
    public void setStartdatetime(Timestamp startdatetime) {
        this.startdatetime = startdatetime;
    }

    /**
     * 
     * @return starttantosyacode
     */
    public String getStarttantosyacode() {
        return starttantosyacode;
    }

    /**
     * 
     * @param starttantosyacode セットする starttantosyacode
     */
    public void setStarttantosyacode(String starttantosyacode) {
        this.starttantosyacode = starttantosyacode;
    }

    /**
     * 
     * @return startkakuninsyacode
     */
    public String getStartkakuninsyacode() {
        return startkakuninsyacode;
    }

    /**
     * 
     * @param startkakuninsyacode セットする startkakuninsyacode
     */
    public void setStartkakuninsyacode(String startkakuninsyacode) {
        this.startkakuninsyacode = startkakuninsyacode;
    }

    /**
     * 
     * @return enddatetime
     */
    public Timestamp getEnddatetime() {
        return enddatetime;
    }

    /**
     * 
     * @param enddatetime セットする enddatetime
     */
    public void setEnddatetime(Timestamp enddatetime) {
        this.enddatetime = enddatetime;
    }

    /**
     * 
     * @return endtantosyacode
     */
    public String getEndtantosyacode() {
        return endtantosyacode;
    }

    /**
     * 
     * @param endtantosyacode セットする endtantosyacode
     */
    public void setEndtantosyacode(String endtantosyacode) {
        this.endtantosyacode = endtantosyacode;
    }

    /**
     * @return biko1
     */
    public String getBiko1() {
        return biko1;
    }

    /**
     * @param biko1 セットする biko1
     */
    public void setBiko1(String biko1) {
        this.biko1 = biko1;
    }

    /**
     * @return biko2
     */
    public String getBiko2() {
        return biko2;
    }

    /**
     * @param biko2 セットする biko2
     */
    public void setBiko2(String biko2) {
        this.biko2 = biko2;
    }

    /**
     * @return torokunichiji
     */
    public Timestamp getTorokunichiji() {
        return torokunichiji;
    }

    /**
     * @param torokunichiji セットする torokunichiji
     */
    public void setTorokunichiji(Timestamp torokunichiji) {
        this.torokunichiji = torokunichiji;
    }

    /**
     * @return kosinnichiji
     */
    public Timestamp getKosinnichiji() {
        return kosinnichiji;
    }

    /**
     * @param kosinnichiji セットする kosinnichiji
     */
    public void setKosinnichiji(Timestamp kosinnichiji) {
        this.kosinnichiji = kosinnichiji;
    }

    /**
     * @return revision
     */
    public Integer getRevision() {
        return revision;
    }

    /**
     * @param revision セットする revision
     */
    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    /**
     * @return deleteflag
     */
    public Integer getDeleteflag() {
        return deleteflag;
    }

    /**
     * @param deleteflag セットする deleteflag
     */
    public void setDeleteflag(Integer deleteflag) {
        this.deleteflag = deleteflag;
    }

    
}