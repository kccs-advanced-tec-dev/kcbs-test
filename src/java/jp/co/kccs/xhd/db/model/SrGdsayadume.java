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
 * 変更日	2019/10/08<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SR_GDSAYADUME(外部電極焼成(ｻﾔ詰め))のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2019/10/08
 */
public class SrGdsayadume {
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
     * ｻﾔ詰め方法
     */
    private String sayadumehouhou;

    /**
     * 粉まぶし
     */
    private String konamabushi;

    /**
     * 製品重量
     */
    private BigDecimal juryou;

    /**
     * BN粉末量
     */
    private BigDecimal bnfunmaturyou;

    /**
     * BN粉末量確認
     */
    private String bnfunmaturyoukakunin;

    /**
     * ｻﾔ/SUS板種類
     */
    private String sayasussyurui;

    /**
     * ｻﾔ/SUS板枚数 計算値
     */
    private Integer sayamaisuukeisan;

    /**
     * ｻﾔ重量範囲(g)MIN
     */
    private BigDecimal sjyuuryourangemin;

    /**
     * ｻﾔ重量範囲(g)MAX	
     */
    private BigDecimal sjyuuryourangemax;

    /**
     * ｻﾔ重量(g/枚)
     */
    private BigDecimal sayajyuuryou;

    /**
     * ｻﾔ/SUS板枚数
     */
    private Integer sayamaisuu;

    /**
     * ｻﾔ/SUS板ﾁｬｰｼﾞ量
     */
    private Integer saysusacharge;

    /**
     * ｻﾔ/SUS板詰め開始日時
     */
    private Timestamp startdatetime;

    /**
     * ｻﾔ/SUS板詰め開始担当者
     */
    private String starttantosyacode;

    /**
     * ｻﾔ/SUS板詰め開始確認者
     */
    private String startkakuninsyacode;

    /**
     * ｻﾔ/SUS板詰め終了日時
     */
    private Timestamp enddatetime;

    /**
     * ｻﾔ/SUS板詰め終了担当者
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

    public String getSayadumehouhou() {
        return sayadumehouhou;
    }

    public void setSayadumehouhou(String sayadumehouhou) {
        this.sayadumehouhou = sayadumehouhou;
    }

    public String getKonamabushi() {
        return konamabushi;
    }

    public void setKonamabushi(String konamabushi) {
        this.konamabushi = konamabushi;
    }

    public BigDecimal getJuryou() {
        return juryou;
    }

    public void setJuryou(BigDecimal juryou) {
        this.juryou = juryou;
    }

    public BigDecimal getBnfunmaturyou() {
        return bnfunmaturyou;
    }

    public void setBnfunmaturyou(BigDecimal bnfunmaturyou) {
        this.bnfunmaturyou = bnfunmaturyou;
    }

    public String getBnfunmaturyoukakunin() {
        return bnfunmaturyoukakunin;
    }

    public void setBnfunmaturyoukakunin(String bnfunmaturyoukakunin) {
        this.bnfunmaturyoukakunin = bnfunmaturyoukakunin;
    }

    public String getSayasussyurui() {
        return sayasussyurui;
    }

    public void setSayasussyurui(String sayasussyurui) {
        this.sayasussyurui = sayasussyurui;
    }

    public Integer getSayamaisuukeisan() {
        return sayamaisuukeisan;
    }

    public void setSayamaisuukeisan(Integer sayamaisuukeisan) {
        this.sayamaisuukeisan = sayamaisuukeisan;
    }

    public BigDecimal getSjyuuryourangemin() {
        return sjyuuryourangemin;
    }

    public void setSjyuuryourangemin(BigDecimal sjyuuryourangemin) {
        this.sjyuuryourangemin = sjyuuryourangemin;
    }

    public BigDecimal getSjyuuryourangemax() {
        return sjyuuryourangemax;
    }

    public void setSjyuuryourangemax(BigDecimal sjyuuryourangemax) {
        this.sjyuuryourangemax = sjyuuryourangemax;
    }

    public BigDecimal getSayajyuuryou() {
        return sayajyuuryou;
    }

    public void setSayajyuuryou(BigDecimal sayajyuuryou) {
        this.sayajyuuryou = sayajyuuryou;
    }

    public Integer getSayamaisuu() {
        return sayamaisuu;
    }

    public void setSayamaisuu(Integer sayamaisuu) {
        this.sayamaisuu = sayamaisuu;
    }

    public Integer getSaysusacharge() {
        return saysusacharge;
    }

    public void setSaysusacharge(Integer saysusacharge) {
        this.saysusacharge = saysusacharge;
    }

    public Timestamp getStartdatetime() {
        return startdatetime;
    }

    public void setStartdatetime(Timestamp startdatetime) {
        this.startdatetime = startdatetime;
    }

    public String getStarttantosyacode() {
        return starttantosyacode;
    }

    public void setStarttantosyacode(String starttantosyacode) {
        this.starttantosyacode = starttantosyacode;
    }

    public String getStartkakuninsyacode() {
        return startkakuninsyacode;
    }

    public void setStartkakuninsyacode(String startkakuninsyacode) {
        this.startkakuninsyacode = startkakuninsyacode;
    }

    public Timestamp getEnddatetime() {
        return enddatetime;
    }

    public void setEnddatetime(Timestamp enddatetime) {
        this.enddatetime = enddatetime;
    }

    public String getEndtantosyacode() {
        return endtantosyacode;
    }

    public void setEndtantosyacode(String endtantosyacode) {
        this.endtantosyacode = endtantosyacode;
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