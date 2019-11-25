/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2019/11/05<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 外部電極・外部電極焼成(ｻﾔ詰め)履歴検索画面のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2019/11/05
 */
public class GXHDO201B027Model implements Serializable{

    /** ﾛｯﾄNo. */
    private String lotno = "";

    /** 作業回数 */
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

    /** ｻﾔ詰め方法 */
    private String sayadumehouhou = "";

    /** 粉まぶし */
    private String konamabushi = "";

    /** 製品重量 */
    private BigDecimal juryou = null;

    /** BN粉末量 */
    private BigDecimal bnfunmaturyou = null;

    /** BN粉末量確認 */
    private String bnfunmaturyoukakunin = "";

    /** ｻﾔ/SUS板種類 */
    private String sayasussyurui = "";

    /** ｻﾔ/SUS板枚数 計算値 */
    private Integer sayamaisuukeisan = null;

    /** ｻﾔ重量範囲(g)MIN */
    private BigDecimal sjyuuryourangemin = null;

    /** ｻﾔ重量範囲(g)MAX */
    private BigDecimal sjyuuryourangemax = null;

    /** ｻﾔ重量(g/枚) */
    private BigDecimal sayajyuuryou = null;

    /** ｻﾔ/SUS板枚数 */
    private Integer sayamaisuu = null;

    /** ｻﾔ/SUS板ﾁｬｰｼﾞ量 */
    private Integer saysusacharge = null;

    /** ｻﾔ/SUS板詰め開始日時 */
    private Timestamp startdatetime = null;

    /** ｻﾔ/SUS板詰め開始担当者 */
    private String starttantosyacode = "";

    /** ｻﾔ/SUS板詰め開始確認者 */
    private String startkakuninsyacode = "";

    /** ｻﾔ/SUS板詰め終了日時 */
    private Timestamp enddatetime = null;

    /** ｻﾔ/SUS板詰め終了担当者 */
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
     * @return sayadumehouhou
     */
    public String getSayadumehouhou() {
        return sayadumehouhou;
    }

    /**
     * @param sayadumehouhou sayadumehouhou
     */
    public void setSayadumehouhou(String sayadumehouhou) {
        this.sayadumehouhou = sayadumehouhou;
    }

    /**
     * @return konamabushi
     */
    public String getKonamabushi() {
        return konamabushi;
    }

    /**
     * @param konamabushi konamabushi
     */
    public void setKonamabushi(String konamabushi) {
        this.konamabushi = konamabushi;
    }

    /**
     * @return juryou
     */
    public BigDecimal getJuryou() {
        return juryou;
    }

    /**
     * @param juryou juryou
     */
    public void setJuryou(BigDecimal juryou) {
        this.juryou = juryou;
    }

    /**
     * @return bnfunmaturyou
     */
    public BigDecimal getBnfunmaturyou() {
        return bnfunmaturyou;
    }

    /**
     * @param bnfunmaturyou bnfunmaturyou
     */
    public void setBnfunmaturyou(BigDecimal bnfunmaturyou) {
        this.bnfunmaturyou = bnfunmaturyou;
    }

    /**
     * @return bnfunmaturyoukakunin
     */
    public String getBnfunmaturyoukakunin() {
        return bnfunmaturyoukakunin;
    }

    /**
     * @param bnfunmaturyoukakunin bnfunmaturyoukakunin
     */
    public void setBnfunmaturyoukakunin(String bnfunmaturyoukakunin) {
        this.bnfunmaturyoukakunin = bnfunmaturyoukakunin;
    }

    /**
     * @return sayasussyurui
     */
    public String getSayasussyurui() {
        return sayasussyurui;
    }

    /**
     * @param sayasussyurui sayasussyurui
     */
    public void setSayasussyurui(String sayasussyurui) {
        this.sayasussyurui = sayasussyurui;
    }

    /**
     * @return sayamaisuukeisan
     */
    public Integer getSayamaisuukeisan() {
        return sayamaisuukeisan;
    }

    /**
     * @param sayamaisuukeisan sayamaisuukeisan
     */
    public void setSayamaisuukeisan(Integer sayamaisuukeisan) {
        this.sayamaisuukeisan = sayamaisuukeisan;
    }

    /**
     * @return sjyuuryourangemin
     */
    public BigDecimal getSjyuuryourangemin() {
        return sjyuuryourangemin;
    }

    /**
     * @param sjyuuryourangemin sjyuuryourangemin
     */
    public void setSjyuuryourangemin(BigDecimal sjyuuryourangemin) {
        this.sjyuuryourangemin = sjyuuryourangemin;
    }

    /**
     * @return sjyuuryourangemax
     */
    public BigDecimal getSjyuuryourangemax() {
        return sjyuuryourangemax;
    }

    /**
     * @param sjyuuryourangemax sjyuuryourangemax
     */
    public void setSjyuuryourangemax(BigDecimal sjyuuryourangemax) {
        this.sjyuuryourangemax = sjyuuryourangemax;
    }

    /**
     * @return sayajyuuryou
     */
    public BigDecimal getSayajyuuryou() {
        return sayajyuuryou;
    }

    /**
     * @param sayajyuuryou sayajyuuryou
     */
    public void setSayajyuuryou(BigDecimal sayajyuuryou) {
        this.sayajyuuryou = sayajyuuryou;
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
     * @return saysusacharge
     */
    public Integer getSaysusacharge() {
        return saysusacharge;
    }

    /**
     * @param saysusacharge saysusacharge
     */
    public void setSaysusacharge(Integer saysusacharge) {
        this.saysusacharge = saysusacharge;
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
