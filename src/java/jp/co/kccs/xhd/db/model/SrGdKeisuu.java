/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2019/10/08<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	CTC H.Hagiuchi<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SR_GDKEISUU(計数)のモデルクラスです。
 *
 * @author CTC H.Hagiuchi
 * @since 2019/10/08
 */
public class SrGdKeisuu {

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
     * 単位重量
     */
    private BigDecimal tanijyuryo;

    /**
     * 総重量
     */
    private BigDecimal soujuryou;

    /**
     * 送り良品数
     */
    private Integer ryohinkosuu;

    /**
     * 計数日時
     */
    private Timestamp keinichiji;

    /**
     * 担当者
     */
    private String keitantosya;

    /**
     * 歩留まり
     */
    private BigDecimal budomari;

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
     * @return tokuisaki
     */
    public String getTokuisaki() {
        return tokuisaki;
    }

    /**
     * @param tokuisaki セットする tokuisaki
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
     * @param lotkubuncode セットする lotkubuncode
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
     * @param ownercode セットする ownercode
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
     * @param lotpre セットする lotpre
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
     * @param syorisuu セットする syorisuu
     */
    public void setSyorisuu(Integer syorisuu) {
        this.syorisuu = syorisuu;
    }

    /**
     * @return tanijyuryo
     */
    public BigDecimal getTanijyuryo() {
        return tanijyuryo;
    }

    /**
     * @param tanijyuryo セットする tanijyuryo
     */
    public void setTanijyuryo(BigDecimal tanijyuryo) {
        this.tanijyuryo = tanijyuryo;
    }

    /**
     * @return soujuryou
     */
    public BigDecimal getSoujuryou() {
        return soujuryou;
    }

    /**
     * @param soujuryou セットする soujuryou
     */
    public void setSoujuryou(BigDecimal soujuryou) {
        this.soujuryou = soujuryou;
    }

    /**
     * @return ryohinkosuu
     */
    public Integer getRyohinkosuu() {
        return ryohinkosuu;
    }

    /**
     * @param ryohinkosuu セットする ryohinkosuu
     */
    public void setRyohinkosuu(Integer ryohinkosuu) {
        this.ryohinkosuu = ryohinkosuu;
    }

    /**
     * @return keinichiji
     */
    public Timestamp getKeinichiji() {
        return keinichiji;
    }

    /**
     * @param keinichiji セットする keinichiji
     */
    public void setKeinichiji(Timestamp keinichiji) {
        this.keinichiji = keinichiji;
    }

    /**
     * @return keitantosya
     */
    public String getKeitantosya() {
        return keitantosya;
    }

    /**
     * @param keitantosya セットする keitantosya
     */
    public void setKeitantosya(String keitantosya) {
        this.keitantosya = keitantosya;
    }

    /**
     * @return budomari
     */
    public BigDecimal getBudomari() {
        return budomari;
    }

    /**
     * @param budomari セットする budomari
     */
    public void setBudomari(BigDecimal budomari) {
        this.budomari = budomari;
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
