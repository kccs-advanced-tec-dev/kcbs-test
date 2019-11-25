/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2019/11/06<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 外部電極・外部電極焼成(脱ﾊﾞｲ)履歴検索画面のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2019/11/06
 */
public class GXHDO201B028Model implements Serializable{

    /** ﾛｯﾄNo. */
    private String lotno = "";

    /** 回数 */
    private Integer kaisuu = null;

    /** KCPNO */
    private String kcpno = "";

    /** 客先 */
    private String tokuisaki = "";

    /** ﾛｯﾄ区分 */
    private String lotkubuncode = "";

    /** ｵｰﾅｰ */
    private String ownercode = "";

    /** ﾛｯﾄﾌﾟﾚ */
    private String lotpre = "";

    /** 処理数 */
    private Integer syorisuu = null;

    /** 号機 */
    private String gouki = "";

    /** 温度 */
    private String ondo = "";

    /** 時間 */
    private Integer jikan = null;

    /** PTNNO */
    private String ptnno = "";

    /** ｻﾔ枚数 */
    private Integer sayamaisuu = null;

    /** 開始日時 */
    private Timestamp startdatetime = null;

    /** 開始担当者 */
    private String starttantosyacode = "";

    /** 開始確認者 */
    private String startkakuninsyacode = "";

    /** 終了日時 */
    private Timestamp enddatetime = null;

    /** 終了担当者 */
    private String endtantosyacode = "";

    /** 備考1 */
    private String biko1 = "";

    /** 備考2 */
    private String biko2 = "";

    /**
     * @return lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * @param lotno lotno
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * @return kaisuu
     */
    public Integer getKaisuu() {
        return kaisuu;
    }

    /**
     * @param kaisuu kaisuu
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
     * @param kcpno kcpno
     */
    public void setKcpno(String kcpno) {
        this.kcpno = kcpno;
    }

    /**
     * @return tokuisaki
     */
    public String getTokuisaki() {
        return tokuisaki;
    }

    /**
     * @param tokuisaki tokuisaki
     */
    public void setTokuisaki(String tokuisaki) {
        this.tokuisaki = tokuisaki;
    }

    /**
     * @return lotkubuncode
     */
    public String getLotkubuncode() {
        return lotkubuncode;
    }

    /**
     * @param lotkubuncode lotkubuncode
     */
    public void setLotkubuncode(String lotkubuncode) {
        this.lotkubuncode = lotkubuncode;
    }

    /**
     * @return ownercode
     */
    public String getOwnercode() {
        return ownercode;
    }

    /**
     * @param ownercode ownercode
     */
    public void setOwnercode(String ownercode) {
        this.ownercode = ownercode;
    }

    /**
     * @return lotpre
     */
    public String getLotpre() {
        return lotpre;
    }

    /**
     * @param lotpre lotpre
     */
    public void setLotpre(String lotpre) {
        this.lotpre = lotpre;
    }

    /**
     * @return syorisuu
     */
    public Integer getSyorisuu() {
        return syorisuu;
    }

    /**
     * @param syorisuu syorisuu
     */
    public void setSyorisuu(Integer syorisuu) {
        this.syorisuu = syorisuu;
    }

    /**
     * @return gouki
     */
    public String getGouki() {
        return gouki;
    }

    /**
     * @param gouki gouki
     */
    public void setGouki(String gouki) {
        this.gouki = gouki;
    }

    /**
     * @return ondo
     */
    public String getOndo() {
        return ondo;
    }

    /**
     * @param ondo ondo
     */
    public void setOndo(String ondo) {
        this.ondo = ondo;
    }

    /**
     * @return jikan
     */
    public Integer getJikan() {
        return jikan;
    }

    /**
     * @param jikan jikan
     */
    public void setJikan(Integer jikan) {
        this.jikan = jikan;
    }

    /**
     * @return ptnno
     */
    public String getPtnno() {
        return ptnno;
    }

    /**
     * @param ptnno ptnno
     */
    public void setPtnno(String ptnno) {
        this.ptnno = ptnno;
    }

    /**
     * @return sayamaisuu
     */
    public Integer getSayamaisuu() {
        return sayamaisuu;
    }

    /**
     * @param sayamaisuu sayamaisuu
     */
    public void setSayamaisuu(Integer sayamaisuu) {
        this.sayamaisuu = sayamaisuu;
    }

    /**
     * @return startdatetime
     */
    public Timestamp getStartdatetime() {
        return startdatetime;
    }

    /**
     * @param startdatetime startdatetime
     */
    public void setStartdatetime(Timestamp startdatetime) {
        this.startdatetime = startdatetime;
    }

    /**
     * @return starttantosyacode
     */
    public String getStarttantosyacode() {
        return starttantosyacode;
    }

    /**
     * @param starttantosyacode starttantosyacode
     */
    public void setStarttantosyacode(String starttantosyacode) {
        this.starttantosyacode = starttantosyacode;
    }

    /**
     * @return startkakuninsyacode
     */
    public String getStartkakuninsyacode() {
        return startkakuninsyacode;
    }

    /**
     * @param startkakuninsyacode startkakuninsyacode
     */
    public void setStartkakuninsyacode(String startkakuninsyacode) {
        this.startkakuninsyacode = startkakuninsyacode;
    }

    /**
     * @return enddatetime
     */
    public Timestamp getEnddatetime() {
        return enddatetime;
    }

    /**
     * @param enddatetime enddatetime
     */
    public void setEnddatetime(Timestamp enddatetime) {
        this.enddatetime = enddatetime;
    }

    /**
     * @return endtantosyacode
     */
    public String getEndtantosyacode() {
        return endtantosyacode;
    }

    /**
     * @param endtantosyacode endtantosyacode
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
     * @param biko1 biko1
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
     * @param biko2 biko2
     */
    public void setBiko2(String biko2) {
        this.biko2 = biko2;
    }

}
