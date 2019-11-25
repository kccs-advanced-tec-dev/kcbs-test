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
 * 変更日	2019/11/06<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 外部電極・外部電極焼成(焼成外観)履歴検索画面のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2019/11/06
 */
public class GXHDO201B030Model implements Serializable{

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

    /** 外観 */
    private String gaikan = "";

    /** ｱﾍﾞｯｸ選別後良品重量(g) */
    private BigDecimal ryohinjuryou = null;

    /** 不良重量(g) */
    private BigDecimal furyoujuryou = null;

    /** 不良率(%) */
    private BigDecimal furyouritsu = null;

    /** 外観確認日時 */
    private Timestamp gaikandatetime = null;

    /** 外観確認担当者 */
    private String gaikantantosyacode = "";

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
     * @return gaikan
     */
    public String getGaikan() {
        return gaikan;
    }

    /**
     * @param gaikan gaikan
     */
    public void setGaikan(String gaikan) {
        this.gaikan = gaikan;
    }

    /**
     * @return ryohinjuryou
     */
    public BigDecimal getRyohinjuryou() {
        return ryohinjuryou;
    }

    /**
     * @param ryohinjuryou ryohinjuryou
     */
    public void setRyohinjuryou(BigDecimal ryohinjuryou) {
        this.ryohinjuryou = ryohinjuryou;
    }

    /**
     * @return furyoujuryou
     */
    public BigDecimal getFuryoujuryou() {
        return furyoujuryou;
    }

    /**
     * @param furyoujuryou furyoujuryou
     */
    public void setFuryoujuryou(BigDecimal furyoujuryou) {
        this.furyoujuryou = furyoujuryou;
    }

    /**
     * @return furyouritsu
     */
    public BigDecimal getFuryouritsu() {
        return furyouritsu;
    }

    /**
     * @param furyouritsu furyouritsu
     */
    public void setFuryouritsu(BigDecimal furyouritsu) {
        this.furyouritsu = furyouritsu;
    }

    /**
     * @return gaikandatetime
     */
    public Timestamp getGaikandatetime() {
        return gaikandatetime;
    }

    /**
     * @param gaikandatetime gaikandatetime
     */
    public void setGaikandatetime(Timestamp gaikandatetime) {
        this.gaikandatetime = gaikandatetime;
    }

    /**
     * @return gaikantantosyacode
     */
    public String getGaikantantosyacode() {
        return gaikantantosyacode;
    }

    /**
     * @param gaikantantosyacode gaikantantosyacode
     */
    public void setGaikantantosyacode(String gaikantantosyacode) {
        this.gaikantantosyacode = gaikantantosyacode;
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
