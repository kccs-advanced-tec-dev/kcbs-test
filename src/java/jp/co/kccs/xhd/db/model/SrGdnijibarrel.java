
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
 * 変更日	2019/10/17<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SR_GDNIJIBARREL(外部電極洗浄(ﾊﾞﾚﾙ))のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2019/10/14
 */
public class SrGdnijibarrel {
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
     * 号機
     */
    private String gouki;

    /**
     * 処理時間
     */
    private String syorijikan;

    /**
     * 回転数
     */
    private BigDecimal kaitensuu;

    /**
     * ﾎﾟｯﾄ数
     */
    private Integer potsuu;

    /**
     * ﾒﾀﾉｰﾙ交換時間
     */
    private String methanolkoukanjikan;

    /**
     * ﾒﾀﾉｰﾙ交換担当者
     */
    private String methanolkoukantantousya;

    /**
     * ﾒﾀﾉｰﾙ交換ポット累計数
     */
    private Integer methanolkoukanpotruikeisuu;

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
     * 備考3
     */
    private String biko3;

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

    public String getGouki() {
        return gouki;
    }

    public void setGouki(String gouki) {
        this.gouki = gouki;
    }

    public String getSyorijikan() {
        return syorijikan;
    }

    public void setSyorijikan(String syorijikan) {
        this.syorijikan = syorijikan;
    }

    public BigDecimal getKaitensuu() {
        return kaitensuu;
    }

    public void setKaitensuu(BigDecimal kaitensuu) {
        this.kaitensuu = kaitensuu;
    }

    public Integer getPotsuu() {
        return potsuu;
    }

    public void setPotsuu(Integer potsuu) {
        this.potsuu = potsuu;
    }

    public String getMethanolkoukanjikan() {
        return methanolkoukanjikan;
    }

    public void setMethanolkoukanjikan(String methanolkoukanjikan) {
        this.methanolkoukanjikan = methanolkoukanjikan;
    }

    public String getMethanolkoukantantousya() {
        return methanolkoukantantousya;
    }

    public void setMethanolkoukantantousya(String methanolkoukantantousya) {
        this.methanolkoukantantousya = methanolkoukantantousya;
    }

    public Integer getMethanolkoukanpotruikeisuu() {
        return methanolkoukanpotruikeisuu;
    }

    public void setMethanolkoukanpotruikeisuu(Integer methanolkoukanpotruikeisuu) {
        this.methanolkoukanpotruikeisuu = methanolkoukanpotruikeisuu;
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

    public String getBiko3() {
        return biko3;
    }

    public void setBiko3(String biko3) {
        this.biko3 = biko3;
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