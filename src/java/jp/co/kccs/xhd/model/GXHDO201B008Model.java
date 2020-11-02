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
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日       2019/08/06<br>
 * 計画書No     K1811-DS001<br>
 * 変更者       KCSS K.Jo<br>
 * 変更理由     新規作成<br>
 * <br>
 * <br>
 * 変更日	2020/09/17<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	863 sujialiang<br>
 * 変更理由	項目追加<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * ﾌﾟﾚｽ・真空脱気履歴検索画面のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2019/08/06
 */
public class GXHDO201B008Model implements Serializable {

    /**
     * ﾛｯﾄNo.
     */
    private String lotno = "";

    /**
     * KCPNO
     */
    private String kcpno = "";

    /**
     * 開始日時
     */
    private Timestamp startdatetime = null;

    /**
     * 終了日時
     */
    private Timestamp enddatetime = null;

    /**
     * 号機ｺｰﾄﾞ
     */
    private String gouki = "";

    /**
     * 温度
     */
    private BigDecimal ondo = null;

    /**
     * 担当者
     */
    private String tantousya = "";

    /**
     * 確認者
     */
    private String kakuninsya = "";

    /**
     * 備考1
     */
    private String bikou1 = "";

    /**
     * 備考2
     */
    private String bikou2 = "";

    /**
     * 備考3
     */
    private String bikou3 = "";

    /**
     * 備考4
     */
    private String bikou4 = "";

    /**
     * 備考5
     */
    private String bikou5 = "";

    /**
     * 室温
     */
    private BigDecimal situon = null;

    /**
     * 湿度
     */
    private BigDecimal situdo = null;

    /**
     * 圧力
     */
    private Integer aturyoku = null;

    /**
     * 時間1
     */
    private Integer jikan1 = null;

    /**
     * 時間2
     */
    private Integer jikan2 = null;

    /**
     * 厚みMIN
     */
    private Integer atumimin = null;

    /**
     * 厚みMAX
     */
    private Integer atumimax = null;

    /**
     * 真空ﾁｪｯｸ
     */
    private String shinkuuhojicheck = "";

    /**
     * ｾﾗﾋﾟｰﾙ/ﾌﾞﾗｽﾄ
     */
    private String cerapeel = "";

    /**
     * 緩衝材1
     */
    private String kansyouzai1 = "";

    /**
     * SUS板
     */
    private String susborad = "";

    /**
     * 緩衝材2
     */
    private String kansyouzai2 = "";

    /**
     * 静水圧ﾌﾟﾚｽ号機
     */
    private String seisuiatupressgouki = "";

    /**
     * 真空時間
     */
    private Integer sinkuujikan = null;

    /**
     * 予熱時間1
     */
    private Integer yonetujikan1 = null;

    /**
     * 予熱時間2
     */
    private Integer yonetujikan2 = null;

    /**
     * 予熱時間3
     */
    private Integer yonetujikan3 = null;

    /**
     * 1次最高圧力
     */
    private Integer aturyoku1max = null;

    /**
     * 2次最高圧力
     */
    private Integer aturyoku2max = null;

    /**
     * 水ﾇﾚｾｯﾄ数
     */
    private Integer mizunuresetsuu = null;

    /**
     * ﾌﾟﾚｽ後冷却時間
     */
    private Integer pressgoreikyakujikan = null;

    /**
     * 水系TC Niﾌﾟﾚｽ前ｴｰｼﾞﾝｸﾞ
     */
    private Integer pressmaeaging = null;

    /**
     * 終了担当者
     */
    private String endtantousyacode = "";

    /**
     * 処理ｾｯﾄ数
     */
    private Integer setsuu = null;

    /**
     * 良品ｾｯﾄ数
     */
    private Integer ryouhinsetsuu = null;

    /**
     * ﾌﾟﾚｽ回数
     */
    private Integer presskaisuu = null;

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
     * @return startdatetime
     */
    public Timestamp getStartdatetime() {
        return startdatetime;
    }

    /**
     * @param startdatetime セットする startdatetime
     */
    public void setStartdatetime(Timestamp startdatetime) {
        this.startdatetime = startdatetime;
    }

    /**
     * @return enddatetime
     */
    public Timestamp getEnddatetime() {
        return enddatetime;
    }

    /**
     * @param enddatetime セットする enddatetime
     */
    public void setEnddatetime(Timestamp enddatetime) {
        this.enddatetime = enddatetime;
    }

    /**
     * @return gouki
     */
    public String getGouki() {
        return gouki;
    }

    /**
     * @param gouki セットする gouki
     */
    public void setGouki(String gouki) {
        this.gouki = gouki;
    }

    /**
     * @return ondo
     */
    public BigDecimal getOndo() {
        return ondo;
    }

    /**
     * @param ondo セットする ondo
     */
    public void setOndo(BigDecimal ondo) {
        this.ondo = ondo;
    }

    /**
     * @return tantousya
     */
    public String getTantousya() {
        return tantousya;
    }

    /**
     * @param tantousya セットする tantousya
     */
    public void setTantousya(String tantousya) {
        this.tantousya = tantousya;
    }

    /**
     * @return kakuninsya
     */
    public String getKakuninsya() {
        return kakuninsya;
    }

    /**
     * @param kakuninsya セットする kakuninsya
     */
    public void setKakuninsya(String kakuninsya) {
        this.kakuninsya = kakuninsya;
    }

    /**
     * @return bikou1
     */
    public String getBikou1() {
        return bikou1;
    }

    /**
     * @param bikou1 セットする bikou1
     */
    public void setBikou1(String bikou1) {
        this.bikou1 = bikou1;
    }

    /**
     * @return bikou2
     */
    public String getBikou2() {
        return bikou2;
    }

    /**
     * @param bikou2 セットする bikou2
     */
    public void setBikou2(String bikou2) {
        this.bikou2 = bikou2;
    }

    /**
     * @return bikou3
     */
    public String getBikou3() {
        return bikou3;
    }

    /**
     * @param bikou3 セットする bikou3
     */
    public void setBikou3(String bikou3) {
        this.bikou3 = bikou3;
    }

    /**
     * @return bikou4
     */
    public String getBikou4() {
        return bikou4;
    }

    /**
     * @param bikou4 セットする bikou4
     */
    public void setBikou4(String bikou4) {
        this.bikou4 = bikou4;
    }

    /**
     * @return bikou5
     */
    public String getBikou5() {
        return bikou5;
    }

    /**
     * @param bikou5 セットする bikou5
     */
    public void setBikou5(String bikou5) {
        this.bikou5 = bikou5;
    }

    /**
     * @return situon
     */
    public BigDecimal getSituon() {
        return situon;
    }

    /**
     * @param situon セットする situon
     */
    public void setSituon(BigDecimal situon) {
        this.situon = situon;
    }

    /**
     * @return situdo
     */
    public BigDecimal getSitudo() {
        return situdo;
    }

    /**
     * @param situdo セットする situdo
     */
    public void setSitudo(BigDecimal situdo) {
        this.situdo = situdo;
    }

    /**
     * @return aturyoku
     */
    public Integer getAturyoku() {
        return aturyoku;
    }

    /**
     * @param aturyoku セットする aturyoku
     */
    public void setAturyoku(Integer aturyoku) {
        this.aturyoku = aturyoku;
    }

    /**
     * @return jikan1
     */
    public Integer getJikan1() {
        return jikan1;
    }

    /**
     * @param jikan1 セットする jikan1
     */
    public void setJikan1(Integer jikan1) {
        this.jikan1 = jikan1;
    }

    /**
     * @return jikan2
     */
    public Integer getJikan2() {
        return jikan2;
    }

    /**
     * @param jikan2 セットする jikan2
     */
    public void setJikan2(Integer jikan2) {
        this.jikan2 = jikan2;
    }

    /**
     * @return atumimin
     */
    public Integer getAtumimin() {
        return atumimin;
    }

    /**
     * @param atumimin セットする atumimin
     */
    public void setAtumimin(Integer atumimin) {
        this.atumimin = atumimin;
    }

    /**
     * @return atumimax
     */
    public Integer getAtumimax() {
        return atumimax;
    }

    /**
     * @param atumimax セットする atumimax
     */
    public void setAtumimax(Integer atumimax) {
        this.atumimax = atumimax;
    }

    /**
     * @return shinkuuhojicheck
     */
    public String getShinkuuhojicheck() {
        return shinkuuhojicheck;
    }

    /**
     * @param shinkuuhojicheck セットする shinkuuhojicheck
     */
    public void setShinkuuhojicheck(String shinkuuhojicheck) {
        this.shinkuuhojicheck = shinkuuhojicheck;
    }

    /**
     * @return cerapeel
     */
    public String getCerapeel() {
        return cerapeel;
    }

    /**
     * @param cerapeel セットする cerapeel
     */
    public void setCerapeel(String cerapeel) {
        this.cerapeel = cerapeel;
    }

    /**
     * @return kansyouzai1
     */
    public String getKansyouzai1() {
        return kansyouzai1;
    }

    /**
     * @param kansyouzai1 セットする kansyouzai1
     */
    public void setKansyouzai1(String kansyouzai1) {
        this.kansyouzai1 = kansyouzai1;
    }

    /**
     * @return susborad
     */
    public String getSusborad() {
        return susborad;
    }

    /**
     * @param susborad セットする susborad
     */
    public void setSusborad(String susborad) {
        this.susborad = susborad;
    }

    /**
     * @return kansyouzai2
     */
    public String getKansyouzai2() {
        return kansyouzai2;
    }

    /**
     * @param kansyouzai2 セットする kansyouzai2
     */
    public void setKansyouzai2(String kansyouzai2) {
        this.kansyouzai2 = kansyouzai2;
    }

    /**
     * @return seisuiatupressgouki
     */
    public String getSeisuiatupressgouki() {
        return seisuiatupressgouki;
    }

    /**
     * @param seisuiatupressgouki セットする seisuiatupressgouki
     */
    public void setSeisuiatupressgouki(String seisuiatupressgouki) {
        this.seisuiatupressgouki = seisuiatupressgouki;
    }

    /**
     * @return sinkuujikan
     */
    public Integer getSinkuujikan() {
        return sinkuujikan;
    }

    /**
     * @param sinkuujikan セットする sinkuujikan
     */
    public void setSinkuujikan(Integer sinkuujikan) {
        this.sinkuujikan = sinkuujikan;
    }

    /**
     * @return yonetujikan1
     */
    public Integer getYonetujikan1() {
        return yonetujikan1;
    }

    /**
     * @param yonetujikan1 セットする yonetujikan1
     */
    public void setYonetujikan1(Integer yonetujikan1) {
        this.yonetujikan1 = yonetujikan1;
    }

    /**
     * @return yonetujikan2
     */
    public Integer getYonetujikan2() {
        return yonetujikan2;
    }

    /**
     * @param yonetujikan2 セットする yonetujikan2
     */
    public void setYonetujikan2(Integer yonetujikan2) {
        this.yonetujikan2 = yonetujikan2;
    }

    /**
     * @return yonetujikan3
     */
    public Integer getYonetujikan3() {
        return yonetujikan3;
    }

    /**
     * @param yonetujikan3 セットする yonetujikan3
     */
    public void setYonetujikan3(Integer yonetujikan3) {
        this.yonetujikan3 = yonetujikan3;
    }

    /**
     * @return aturyoku1max
     */
    public Integer getAturyoku1max() {
        return aturyoku1max;
    }

    /**
     * @param aturyoku1max セットする aturyoku1max
     */
    public void setAturyoku1max(Integer aturyoku1max) {
        this.aturyoku1max = aturyoku1max;
    }

    /**
     * @return aturyoku2max
     */
    public Integer getAturyoku2max() {
        return aturyoku2max;
    }

    /**
     * @param aturyoku2max セットする aturyoku2max
     */
    public void setAturyoku2max(Integer aturyoku2max) {
        this.aturyoku2max = aturyoku2max;
    }

    /**
     * @return mizunuresetsuu
     */
    public Integer getMizunuresetsuu() {
        return mizunuresetsuu;
    }

    /**
     * @param mizunuresetsuu セットする mizunuresetsuu
     */
    public void setMizunuresetsuu(Integer mizunuresetsuu) {
        this.mizunuresetsuu = mizunuresetsuu;
    }

    /**
     * @return pressgoreikyakujikan
     */
    public Integer getPressgoreikyakujikan() {
        return pressgoreikyakujikan;
    }

    /**
     * @param pressgoreikyakujikan セットする pressgoreikyakujikan
     */
    public void setPressgoreikyakujikan(Integer pressgoreikyakujikan) {
        this.pressgoreikyakujikan = pressgoreikyakujikan;
    }

    /**
     * @return pressmaeaging
     */
    public Integer getPressmaeaging() {
        return pressmaeaging;
    }

    /**
     * @param pressmaeaging セットする pressmaeaging
     */
    public void setPressmaeaging(Integer pressmaeaging) {
        this.pressmaeaging = pressmaeaging;
    }

    /**
     * @return endtantousyacode
     */
    public String getEndtantousyacode() {
        return endtantousyacode;
    }

    /**
     * @param endtantousyacode セットする endtantousyacode
     */
    public void setEndtantousyacode(String endtantousyacode) {
        this.endtantousyacode = endtantousyacode;
    }

    /**
     * @return setsuu
     */
    public Integer getSetsuu() {
        return setsuu;
    }

    /**
     * @param setsuu セットする setsuu
     */
    public void setSetsuu(Integer setsuu) {
        this.setsuu = setsuu;
    }

    /**
     * @return ryouhinsetsuu
     */
    public Integer getRyouhinsetsuu() {
        return ryouhinsetsuu;
    }

    /**
     * @param ryouhinsetsuu セットする ryouhinsetsuu
     */
    public void setRyouhinsetsuu(Integer ryouhinsetsuu) {
        this.ryouhinsetsuu = ryouhinsetsuu;
    }

    /**
     * @return presskaisuu
     */
    public Integer getPresskaisuu() {
        return presskaisuu;
    }

    /**
     * @param presskaisuu セットする presskaisuu
     */
    public void setPresskaisuu(Integer presskaisuu) {
        this.presskaisuu = presskaisuu;
    }

}
