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
 * 変更日	2019/11/12<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 外部電極・計数履歴検索画面のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2019/11/12
 */
public class GXHDO201B037Model implements Serializable{

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

    /** 単位重量 */
    private BigDecimal tanijyuryo = null;

    /** 総重量 */
    private BigDecimal soujuryou = null;

    /** 送り良品数 */
    private Integer ryohinkosuu = null;

    /** 計数日時 */
    private Timestamp keinichiji = null;

    /** 担当者 */
    private String keitantosya = "";

    /** 歩留まり */
    private BigDecimal budomari = null;

    /** 備考1 */
    private String biko1 = "";

    /** 備考2 */
    private String biko2 = "";

    public String getLotno() {
        return lotno;
    }

    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    public Integer getKaisuu() {
        return kaisuu;
    }

    public void setKaisuu(Integer kaisuu) {
        this.kaisuu = kaisuu;
    }

    public String getKcpno() {
        return kcpno;
    }

    public void setKcpno(String kcpno) {
        this.kcpno = kcpno;
    }

    public String getTokuisaki() {
        return tokuisaki;
    }

    public void setTokuisaki(String tokuisaki) {
        this.tokuisaki = tokuisaki;
    }

    public String getLotkubuncode() {
        return lotkubuncode;
    }

    public void setLotkubuncode(String lotkubuncode) {
        this.lotkubuncode = lotkubuncode;
    }

    public String getOwnercode() {
        return ownercode;
    }

    public void setOwnercode(String ownercode) {
        this.ownercode = ownercode;
    }

    public String getLotpre() {
        return lotpre;
    }

    public void setLotpre(String lotpre) {
        this.lotpre = lotpre;
    }

    public Integer getSyorisuu() {
        return syorisuu;
    }

    public void setSyorisuu(Integer syorisuu) {
        this.syorisuu = syorisuu;
    }

    public BigDecimal getTanijyuryo() {
        return tanijyuryo;
    }

    public void setTanijyuryo(BigDecimal tanijyuryo) {
        this.tanijyuryo = tanijyuryo;
    }

    public BigDecimal getSoujuryou() {
        return soujuryou;
    }

    public void setSoujuryou(BigDecimal soujuryou) {
        this.soujuryou = soujuryou;
    }

    public Integer getRyohinkosuu() {
        return ryohinkosuu;
    }

    public void setRyohinkosuu(Integer ryohinkosuu) {
        this.ryohinkosuu = ryohinkosuu;
    }

    public Timestamp getKeinichiji() {
        return keinichiji;
    }

    public void setKeinichiji(Timestamp keinichiji) {
        this.keinichiji = keinichiji;
    }

    public String getKeitantosya() {
        return keitantosya;
    }

    public void setKeitantosya(String keitantosya) {
        this.keitantosya = keitantosya;
    }

    public BigDecimal getBudomari() {
        return budomari;
    }

    public void setBudomari(BigDecimal budomari) {
        this.budomari = budomari;
    }

    public String getBiko1() {
        return biko1;
    }

    public void setBiko1(String biko1) {
        this.biko1 = biko1;
    }

    public String getBiko2() {
        return biko2;
    }

    public void setBiko2(String biko2) {
        this.biko2 = biko2;
    }

}
