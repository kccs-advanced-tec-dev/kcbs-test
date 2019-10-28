/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/10/14<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SR_GDYAKITUKEGAIKAN(外部電極焼成(焼成外観))のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2019/10/14
 */
public class SrGdyakitukegaikan {
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
     * 作業回数
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
     * 外観
     */
    private String gaikan;

    /**
     * ｱﾍﾞｯｸ選別後良品重量
     */
    private BigDecimal ryohinjuryou;

    /**
     * 不良重量
     */
    private BigDecimal furyoujuryou;

    /**
     * 不良率
     */
    private BigDecimal furyouritsu;

    /**
     * 外観確認日時
     */
    private Timestamp gaikandatetime;

    /**
     * 外観確認担当者
     */
    private String gaikantantosyacode;

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

    public String getKojyo() {
        return kojyo;
    }

    public void setKojyo(String kojyo) {
        this.kojyo = kojyo;
    }

    public String getLotno() {
        return lotno;
    }

    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    public String getEdaban() {
        return edaban;
    }

    public void setEdaban(String edaban) {
        this.edaban = edaban;
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

    public String getGaikan() {
        return gaikan;
    }

    public void setGaikan(String gaikan) {
        this.gaikan = gaikan;
    }

    public BigDecimal getRyohinjuryou() {
        return ryohinjuryou;
    }

    public void setRyohinjuryou(BigDecimal ryohinjuryou) {
        this.ryohinjuryou = ryohinjuryou;
    }

    public BigDecimal getFuryoujuryou() {
        return furyoujuryou;
    }

    public void setFuryoujuryou(BigDecimal furyoujuryou) {
        this.furyoujuryou = furyoujuryou;
    }

    public BigDecimal getFuryouritsu() {
        return furyouritsu;
    }

    public void setFuryouritsu(BigDecimal furyouritsu) {
        this.furyouritsu = furyouritsu;
    }

    public Timestamp getGaikandatetime() {
        return gaikandatetime;
    }

    public void setGaikandatetime(Timestamp gaikandatetime) {
        this.gaikandatetime = gaikandatetime;
    }

    public String getGaikantantosyacode() {
        return gaikantantosyacode;
    }

    public void setGaikantantosyacode(String gaikantantosyacode) {
        this.gaikantantosyacode = gaikantantosyacode;
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

    public Timestamp getTorokunichiji() {
        return torokunichiji;
    }

    public void setTorokunichiji(Timestamp torokunichiji) {
        this.torokunichiji = torokunichiji;
    }

    public Timestamp getKosinnichiji() {
        return kosinnichiji;
    }

    public void setKosinnichiji(Timestamp kosinnichiji) {
        this.kosinnichiji = kosinnichiji;
    }

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public Integer getDeleteflag() {
        return deleteflag;
    }

    public void setDeleteflag(Integer deleteflag) {
        this.deleteflag = deleteflag;
    }
    
}