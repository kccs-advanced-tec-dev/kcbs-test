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
 * 変更日	2019/11/07<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 外部電極・外部電極洗浄(撥水処理)履歴検索画面のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2019/11/07
 */
public class GXHDO201B032Model implements Serializable{

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

    /** 浸漬時間 */
    private Integer sinsekijikan = null;

    /** 洗浄時間 */
    private Integer senjoujikan = null;

    /** ﾒﾀﾉｰﾙ交換時間 */
    private String methanolkoukanjikan = "";

    /** ﾒﾀﾉｰﾙ交換担当者 */
    private String methanolkoukantantousya = "";

    /** 撥水処理日時 */
    private Timestamp hassuidatetime = null;

    /** 撥水処理担当者 */
    private String hassuitantosyacode = "";

    /** 乾燥時間 */
    private Integer kansoujikan = null;

    /** 乾燥温度 */
    private String kansouondo = "";

    /** 乾燥処理日時 */
    private Timestamp kansoudatetime = null;

    /** 乾燥処理担当者 */
    private String kansoutantosyacode = "";

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
     * @return sinsekijikan
     */
    public Integer getSinsekijikan() {
        return sinsekijikan;
    }

    /**
     * @param sinsekijikan sinsekijikan
     */
    public void setSinsekijikan(Integer sinsekijikan) {
        this.sinsekijikan = sinsekijikan;
    }

    /**
     * @return senjoujikan
     */
    public Integer getSenjoujikan() {
        return senjoujikan;
    }

    /**
     * @param senjoujikan senjoujikan
     */
    public void setSenjoujikan(Integer senjoujikan) {
        this.senjoujikan = senjoujikan;
    }

    /**
     * @return methanolkoukanjikan
     */
    public String getMethanolkoukanjikan() {
        return methanolkoukanjikan;
    }

    /**
     * @param methanolkoukanjikan methanolkoukanjikan
     */
    public void setMethanolkoukanjikan(String methanolkoukanjikan) {
        this.methanolkoukanjikan = methanolkoukanjikan;
    }

    /**
     * @return methanolkoukantantousya
     */
    public String getMethanolkoukantantousya() {
        return methanolkoukantantousya;
    }

    /**
     * @param methanolkoukantantousya methanolkoukantantousya
     */
    public void setMethanolkoukantantousya(String methanolkoukantantousya) {
        this.methanolkoukantantousya = methanolkoukantantousya;
    }

    /**
     * @return hassuidatetime
     */
    public Timestamp getHassuidatetime() {
        return hassuidatetime;
    }

    /**
     * @param hassuidatetime hassuidatetime
     */
    public void setHassuidatetime(Timestamp hassuidatetime) {
        this.hassuidatetime = hassuidatetime;
    }

    /**
     * @return hassuitantosyacode
     */
    public String getHassuitantosyacode() {
        return hassuitantosyacode;
    }

    /**
     * @param hassuitantosyacode hassuitantosyacode
     */
    public void setHassuitantosyacode(String hassuitantosyacode) {
        this.hassuitantosyacode = hassuitantosyacode;
    }

    /**
     * @return kansoujikan
     */
    public Integer getKansoujikan() {
        return kansoujikan;
    }

    /**
     * @param kansoujikan kansoujikan
     */
    public void setKansoujikan(Integer kansoujikan) {
        this.kansoujikan = kansoujikan;
    }

    /**
     * @return kansouondo
     */
    public String getKansouondo() {
        return kansouondo;
    }

    /**
     * @param kansouondo kansouondo
     */
    public void setKansouondo(String kansouondo) {
        this.kansouondo = kansouondo;
    }

    /**
     * @return kansoudatetime
     */
    public Timestamp getKansoudatetime() {
        return kansoudatetime;
    }

    /**
     * @param kansoudatetime kansoudatetime
     */
    public void setKansoudatetime(Timestamp kansoudatetime) {
        this.kansoudatetime = kansoudatetime;
    }

    /**
     * @return kansoutantosyacode
     */
    public String getKansoutantosyacode() {
        return kansoutantosyacode;
    }

    /**
     * @param kansoutantosyacode kansoutantosyacode
     */
    public void setKansoutantosyacode(String kansoutantosyacode) {
        this.kansoutantosyacode = kansoutantosyacode;
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
