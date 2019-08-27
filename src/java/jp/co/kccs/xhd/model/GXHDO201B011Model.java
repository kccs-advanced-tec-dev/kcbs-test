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
 * 変更日	2019/08/06<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	863 K.Zhang<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * ｶｯﾄ・ﾀﾞｲｼﾝｸﾞｶｯﾄ履歴検索画面のモデルクラスです。
 *
 * @author K
 * @since 2019/08/05
 */
public class GXHDO201B011Model implements Serializable {
    /** ﾛｯﾄNo. */
    private String lotno = "";
    /** KCPNO */
    private String kcpno = "";
    /** 開始日時 */
    private Timestamp startdatetime = null;
    /** 終了日時 */
    private Timestamp enddatetime = null;
    /** ｶｯﾄ刃使用回数(回) */
    private Integer cutbamaisuu = null;
    /** 号機ｺｰﾄﾞ */
    private String gouki = null;
    /** ｶｯﾄﾃｰﾌﾞﾙ温度 */
    private Integer cuttableondo = null;
    /** 開始担当者 */
    private String tantousya = null;
    /** 開始確認者 */
    private String kakuninsya = "";
    /** 備考1 */
    private String bikou1 = "";
    /** 備考2 */
    private String bikou2 = "";
    /** 備考3 */
    private String bikou3 = "";
    /** 備考4 */
    private String bikou4 = "";
    /** 備考5 */
    private String bikou5 = "";
    /** 方式 */
    private String housiki = "";
    /** 生T寸 Min(μm) */
    private Integer atumimin = null;
    /** 生T寸 Max(μm) */
    private Integer atumimax = null;
    /** ｶｯﾄ刃種類(μm) */
    private String cutbashurui = "";
    /** ｶｯﾄ向き */
    private String cutmuki = "";
    /** 発泡ｼｰﾄ色 */
    private String happosheetcolor = "";
    /** 乾燥(min) */
    private Integer kansou = null;
    /** 補正判定 */
    private Integer hoseihantei = null;
    /** 終了担当者 */
    private String endTantousyacode = "";
    /** 処理ｾｯﾄ数(SET) */
    private Integer syorisetsuu = null;
    /** 良品ｾｯﾄ数(SET) */
    private BigDecimal ryouhinsetsuu = null;

    /**
     * ﾛｯﾄNo.
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * ﾛｯﾄNo.
     * @param lotno the lotno to set
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }
    
    /**
     * KCPNO
     * @return the kcpno
     */
    public String getKcpno() {
        return kcpno;
    }

    /**
     * KCPNO
     * @param kcpno the kcpno to set
     */
    public void setKcpno(String kcpno) {
        this.kcpno = kcpno;
    }
    /**
     * 開始日時
     * @return the startdatetime
     */
    public Timestamp getStartdatetime() {
        return startdatetime;
    }

    /**
     * 開始日時
     * @param startdatetime the startdatetime to set
     */
    public void setStartdatetime(Timestamp startdatetime) {
        this.startdatetime = startdatetime;
    }

    /**
     * 終了日時
     * @return the enddatetime
     */
    public Timestamp getEnddatetime() {
        return enddatetime;
    }

    /**
     * 終了日時
     * @param enddatetime the enddatetime to set
     */
    public void setEnddatetime(Timestamp enddatetime) {
        this.enddatetime = enddatetime;
    }

    /**
     * ｶｯﾄ刃使用回数(回)
     * @return the cutbamaisuu
     */
    public Integer getCutbamaisuu() {
        return cutbamaisuu;
    }

    /**
     * ｶｯﾄ刃使用回数(回)
     * @param cutbamaisuu the cutbamaisuu to set
     */
    public void setCutbamaisuu(Integer cutbamaisuu) {
        this.cutbamaisuu = cutbamaisuu;
    }

    /**
     * 号機ｺｰﾄﾞ
     * @return the gouki
     */
    public String getGouki() {
        return gouki;
    }

    /**
     * 号機ｺｰﾄﾞ
     * @param gouki the gouki to set
     */
    public void setGouki(String gouki) {
        this.gouki = gouki;
    }

    /**
     * ｶｯﾄﾃｰﾌﾞﾙ温度
     * @return the cuttableondo
     */
    public Integer getCuttableondo() {
        return cuttableondo;
    }

    /**
     * ｶｯﾄﾃｰﾌﾞﾙ温度
     * @param cuttableondo the cuttableondo to set
     */
    public void setCuttableondo(Integer cuttableondo) {
        this.cuttableondo = cuttableondo;
    }

    /**
     * 開始担当者
     * @return the tantousya
     */
    public String getTantousya() {
        return tantousya;
    }

    /**
     * 開始担当者
     * @param tantousya the tantousya to set
     */
    public void setTantousya(String tantousya) {
        this.tantousya = tantousya;
    }

    /**
     * 開始確認者
     * @return the kakuninsya
     */
    public String getKakuninsya() {
        return kakuninsya;
    }

    /**
     * 開始確認者
     * @param kakuninsya the kakuninsya to set
     */
    public void setKakuninsya(String kakuninsya) {
        this.kakuninsya = kakuninsya;
    }

    /**
     * 備考1
     * @return the bikou1
     */
    public String getBikou1() {
        return bikou1;
    }

    /**
     * 備考1
     * @param bikou1 the bikou1 to set
     */
    public void setBikou1(String bikou1) {
        this.bikou1 = bikou1;
    }

    /**
     * 備考2
     * @return the bikou2
     */
    public String getBikou2() {
        return bikou2;
    }

    /**
     * 備考2
     * @param bikou2 the bikou2 to set
     */
    public void setBikou2(String bikou2) {
        this.bikou2 = bikou2;
    }

    /**
     * 備考3
     * @return the bikou3
     */
    public String getBikou3() {
        return bikou3;
    }

    /**
     * 備考3
     * @param bikou3 the bikou3 to set
     */
    public void setBikou3(String bikou3) {
        this.bikou3 = bikou3;
    }

    /**
     * 備考4
     * @return the bikou4
     */
    public String getBikou4() {
        return bikou4;
    }

    /**
     * 備考4
     * @param bikou4 the bikou4 to set
     */
    public void setBikou4(String bikou4) {
        this.bikou4 = bikou4;
    }

    /**
     * 備考5
     * @return the bikou5
     */
    public String getBikou5() {
        return bikou5;
    }

    /**
     * 備考5
     * @param bikou5 the bikou5 to set
     */
    public void setBikou5(String bikou5) {
        this.bikou5 = bikou5;
    }

    /**
     * 方式
     * @return the housiki
     */
    public String getHousiki() {
        return housiki;
    }

    /**
     * 方式
     * @param housiki the housiki to set
     */
    public void setHousiki(String housiki) {
        this.housiki = housiki;
    }

    /**
     * 生T寸 Min(μm)
     * @return the atumimin
     */
    public Integer getAtumimin() {
        return atumimin;
    }

    /**
     * 生T寸 Min(μm)
     * @param atumimin the atumimin to set
     */
    public void setAtumimin(Integer atumimin) {
        this.atumimin = atumimin;
    }

    /**
     * 生T寸 Max(μm)
     * @return the atumimax
     */
    public Integer getAtumimax() {
        return atumimax;
    }

    /**
     * 生T寸 Max(μm)
     * @param atumimax the atumimax to set
     */
    public void setAtumimax(Integer atumimax) {
        this.atumimax = atumimax;
    }

    /**
     * ｶｯﾄ刃種類(μm)
     * @return the cutbashurui
     */
    public String getCutbashurui() {
        return cutbashurui;
    }

    /**
     * ｶｯﾄ刃種類(μm)
     * @param cutbashurui the cutbashurui to set
     */
    public void setCutbashurui(String cutbashurui) {
        this.cutbashurui = cutbashurui;
    }

    /**
     * ｶｯﾄ向き
     * @return the cutmuki
     */
    public String getCutmuki() {
        return cutmuki;
    }

    /**
     * ｶｯﾄ向き
     * @param cutmuki the cutmuki to set
     */
    public void setCutmuki(String cutmuki) {
        this.cutmuki = cutmuki;
    }

    /**
     * 発泡ｼｰﾄ色
     * @return the happosheetcolor
     */
    public String getHapposheetcolor() {
        return happosheetcolor;
    }

    /**
     * 発泡ｼｰﾄ色
     * @param happosheetcolor the happosheetcolor to set
     */
    public void setHapposheetcolor(String happosheetcolor) {
        this.happosheetcolor = happosheetcolor;
    }

    /**
     * 乾燥(min)
     * @return the kansou
     */
    public Integer getKansou() {
        return kansou;
    }

    /**
     * 乾燥(min)
     * @param kansou the kansou to set
     */
    public void setKansou(Integer kansou) {
        this.kansou = kansou;
    }

    /**
     * 補正判定
     * @return the hoseihantei
     */
    public Integer getHoseihantei() {
        return hoseihantei;
    }

    /**
     * 補正判定
     * @param hoseihantei the hoseihantei to set
     */
    public void setHoseihantei(Integer hoseihantei) {
        this.hoseihantei = hoseihantei;
    }

    /**
     * 終了担当者
     * @return the endTantousyacode
     */
    public String getEndTantousyacode() {
        return endTantousyacode;
    }

    /**
     * 終了担当者
     * @param endTantousyacode the endTantousyacode to set
     */
    public void setEndTantousyacode(String endTantousyacode) {
        this.endTantousyacode = endTantousyacode;
    }

    /**
     * 処理ｾｯﾄ数(SET)
     * @return the syorisetsuu
     */
    public Integer getSyorisetsuu() {
        return syorisetsuu;
    }

    /**
     * 処理ｾｯﾄ数(SET)
     * @param syorisetsuu the syorisetsuu to set
     */
    public void setSyorisetsuu(Integer syorisetsuu) {
        this.syorisetsuu = syorisetsuu;
    }

    /**
     * 良品ｾｯﾄ数(SET)
     * @return the ryouhinsetsuu
     */
    public BigDecimal getRyouhinsetsuu() {
        return ryouhinsetsuu;
    }

    /**
     * 良品ｾｯﾄ数(SET)
     * @param ryouhinsetsuu the ryouhinsetsuu to set
     */
    public void setRyouhinsetsuu(BigDecimal ryouhinsetsuu) {
        this.ryouhinsetsuu = ryouhinsetsuu;
    }
    
   
}
