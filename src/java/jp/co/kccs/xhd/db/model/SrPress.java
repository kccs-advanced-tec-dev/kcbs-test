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
 * 変更日	2019/05/22<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 真空脱気のモデルクラスです。
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2019/05/22
 */
public class SrPress {

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
     * 開始日時
     */
    private Timestamp startdatetime;

    /**
     * 終了日時
     */
    private Timestamp enddatetime;

    /**
     * 号機ｺｰﾄﾞ
     */
    private String gouki;

    /**
     * 温度
     */
    private BigDecimal ondo;

    /**
     * 担当者
     */
    private String tantousya;

    /**
     * 確認者
     */
    private String kakuninsya;

    /**
     * 備考1
     */
    private String bikou1;

    /**
     * 備考2
     */
    private String bikou2;

    /**
     * 備考3
     */
    private String bikou3;

    /**
     * 備考4
     */
    private String bikou4;

    /**
     * 備考5
     */
    private String bikou5;

    /**
     * 室温
     */
    private BigDecimal situon;

    /**
     * 湿度
     */
    private BigDecimal situdo;

    /**
     * 圧力
     */
    private Integer aturyoku;

    /**
     * 時間1
     */
    private Integer jikan1;

    /**
     * 時間2
     */
    private Integer jikan2;

    /**
     * 厚みMIN
     */
    private Integer atumimin;

    /**
     * 厚みMAX
     */
    private Integer atumimax;

    /**
     * 真空ﾁｪｯｸ
     */
    private Integer shinkuuhojicheck;

    /**
     * ｾﾗﾋﾟｰﾙ/ﾌﾞﾗｽﾄ
     */
    private String cerapeel;

    /**
     * 緩衝材1
     */
    private Integer kansyouzai1;

    /**
     * SUS板
     */
    private Integer susborad;

    /**
     * 緩衝材2
     */
    private Integer kansyouzai2;

    /**
     * 静水圧ﾌﾟﾚｽ号機
     */
    private String seisuiatupressgouki;

    /**
     * 予熱時間1
     */
    private Integer yonetujikan1;

    /**
     * 予熱時間2
     */
    private Integer yonetujikan2;

    /**
     * 予熱時間3
     */
    private Integer yonetujikan3;

    /**
     * 1次最高圧力
     */
    private Integer aturyoku1max;

    /**
     * 2次最高圧力
     */
    private Integer aturyoku2max;

    /**
     * 水ﾇﾚｾｯﾄ数
     */
    private Integer mizunureSetsuu;

    /**
     * ﾌﾟﾚｽ後冷却時間
     */
    private Integer pressgoreikyakujikan;

    /**
     * 水系TC Niﾌﾟﾚｽ前ｴｰｼﾞﾝｸﾞ
     */
    private Integer pressmaeaging;

    /**
     * 終了担当者
     */
    private String endtantousyacode;

    /**
     * 処理ｾｯﾄ数
     */
    private Integer setsuu;

    /**
     * 良品ｾｯﾄ数
     */
    private Integer ryouhinsetsuu;

    /**
     * ﾌﾟﾚｽ回数
     */
    private Integer presskaisuu;

    /**
     * 登録日時
     */
    private Timestamp torokunichiji;

    /**
     * 更新日時
     */
    private Timestamp kosinnichiji;

    /**
     * KCPNO
     */
    private String kcpno;
    
    /**
     * revision
     */
    private Long revision;

    /**
     * 削除ﾌﾗｸﾞ
     */
    private Integer deleteflag;

    /**
     * 工場ｺｰﾄﾞ
     *
     * @return the kojyo
     */
    public String getKojyo() {
        return kojyo;
    }

    /**
     * 工場ｺｰﾄﾞ
     *
     * @param kojyo the kojyo to set
     */
    public void setKojyo(String kojyo) {
        this.kojyo = kojyo;
    }

    /**
     * ﾛｯﾄNo
     *
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * ﾛｯﾄNo
     *
     * @param lotno the lotno to set
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * 枝番
     *
     * @return the edaban
     */
    public String getEdaban() {
        return edaban;
    }

    /**
     * 枝番
     *
     * @param edaban the edaban to set
     */
    public void setEdaban(String edaban) {
        this.edaban = edaban;
    }

    /**
     * 開始日時
     *
     * @return the startdatetime
     */
    public Timestamp getStartdatetime() {
        return startdatetime;
    }

    /**
     * 開始日時
     *
     * @param startdatetime the startdatetime to set
     */
    public void setStartdatetime(Timestamp startdatetime) {
        this.startdatetime = startdatetime;
    }

    /**
     * 終了日時
     *
     * @return the enddatetime
     */
    public Timestamp getEnddatetime() {
        return enddatetime;
    }

    /**
     * 終了日時
     *
     * @param enddatetime the enddatetime to set
     */
    public void setEnddatetime(Timestamp enddatetime) {
        this.enddatetime = enddatetime;
    }

    /**
     * 号機ｺｰﾄﾞ
     *
     * @return the gouki
     */
    public String getGouki() {
        return gouki;
    }

    /**
     * 号機ｺｰﾄﾞ
     *
     * @param gouki the gouki to set
     */
    public void setGouki(String gouki) {
        this.gouki = gouki;
    }

    /**
     * 温度
     *
     * @return the ondo
     */
    public BigDecimal getOndo() {
        return ondo;
    }

    /**
     * 温度
     *
     * @param ondo the ondo to set
     */
    public void setOndo(BigDecimal ondo) {
        this.ondo = ondo;
    }

    /**
     * 担当者
     *
     * @return the tantousya
     */
    public String getTantousya() {
        return tantousya;
    }

    /**
     * 担当者
     *
     * @param tantousya the tantousya to set
     */
    public void setTantousya(String tantousya) {
        this.tantousya = tantousya;
    }

    /**
     * 確認者
     *
     * @return the kakuninsya
     */
    public String getKakuninsya() {
        return kakuninsya;
    }

    /**
     * 確認者
     *
     * @param kakuninsya the kakuninsya to set
     */
    public void setKakuninsya(String kakuninsya) {
        this.kakuninsya = kakuninsya;
    }

    /**
     * 備考1
     *
     * @return the bikou1
     */
    public String getBikou1() {
        return bikou1;
    }

    /**
     * 備考1
     *
     * @param bikou1 the bikou1 to set
     */
    public void setBikou1(String bikou1) {
        this.bikou1 = bikou1;
    }

    /**
     * 備考2
     *
     * @return the bikou2
     */
    public String getBikou2() {
        return bikou2;
    }

    /**
     * 備考2
     *
     * @param bikou2 the bikou2 to set
     */
    public void setBikou2(String bikou2) {
        this.bikou2 = bikou2;
    }

    /**
     * 備考3
     *
     * @return the bikou3
     */
    public String getBikou3() {
        return bikou3;
    }

    /**
     * 備考3
     *
     * @param bikou3 the bikou3 to set
     */
    public void setBikou3(String bikou3) {
        this.bikou3 = bikou3;
    }

    /**
     * 備考4
     *
     * @return the bikou4
     */
    public String getBikou4() {
        return bikou4;
    }

    /**
     * 備考4
     *
     * @param bikou4 the bikou4 to set
     */
    public void setBikou4(String bikou4) {
        this.bikou4 = bikou4;
    }

    /**
     * 備考5
     *
     * @return the bikou5
     */
    public String getBikou5() {
        return bikou5;
    }

    /**
     * 備考5
     *
     * @param bikou5 the bikou5 to set
     */
    public void setBikou5(String bikou5) {
        this.bikou5 = bikou5;
    }

    /**
     * 室温
     *
     * @return the situon
     */
    public BigDecimal getSituon() {
        return situon;
    }

    /**
     * 室温
     *
     * @param situon the situon to set
     */
    public void setSituon(BigDecimal situon) {
        this.situon = situon;
    }

    /**
     * 湿度
     *
     * @return the situdo
     */
    public BigDecimal getSitudo() {
        return situdo;
    }

    /**
     * 湿度
     *
     * @param situdo the situdo to set
     */
    public void setSitudo(BigDecimal situdo) {
        this.situdo = situdo;
    }

    /**
     * 圧力
     *
     * @return the aturyoku
     */
    public Integer getAturyoku() {
        return aturyoku;
    }

    /**
     * 圧力
     *
     * @param aturyoku the aturyoku to set
     */
    public void setAturyoku(Integer aturyoku) {
        this.aturyoku = aturyoku;
    }

    /**
     * 時間1
     *
     * @return the jikan1
     */
    public Integer getJikan1() {
        return jikan1;
    }

    /**
     * 時間1
     *
     * @param jikan1 the jikan1 to set
     */
    public void setJikan1(Integer jikan1) {
        this.jikan1 = jikan1;
    }

    /**
     * 時間2
     *
     * @return the jikan2
     */
    public Integer getJikan2() {
        return jikan2;
    }

    /**
     * 時間2
     *
     * @param jikan2 the jikan2 to set
     */
    public void setJikan2(Integer jikan2) {
        this.jikan2 = jikan2;
    }

    /**
     * 厚みMIN
     *
     * @return the atumimin
     */
    public Integer getAtumimin() {
        return atumimin;
    }

    /**
     * 厚みMIN
     *
     * @param atumimin the atumimin to set
     */
    public void setAtumimin(Integer atumimin) {
        this.atumimin = atumimin;
    }

    /**
     * 厚みMAX
     *
     * @return the atumimax
     */
    public Integer getAtumimax() {
        return atumimax;
    }

    /**
     * 厚みMAX
     *
     * @param atumimax the atumimax to set
     */
    public void setAtumimax(Integer atumimax) {
        this.atumimax = atumimax;
    }

    /**
     * 真空ﾁｪｯｸ
     *
     * @return the shinkuuhojicheck
     */
    public Integer getShinkuuhojicheck() {
        return shinkuuhojicheck;
    }

    /**
     * 真空ﾁｪｯｸ
     *
     * @param shinkuuhojicheck the shinkuuhojicheck to set
     */
    public void setShinkuuhojicheck(Integer shinkuuhojicheck) {
        this.shinkuuhojicheck = shinkuuhojicheck;
    }

    /**
     * ｾﾗﾋﾟｰﾙ/ﾌﾞﾗｽﾄ
     *
     * @return the cerapeel
     */
    public String getCerapeel() {
        return cerapeel;
    }

    /**
     * ｾﾗﾋﾟｰﾙ/ﾌﾞﾗｽﾄ
     *
     * @param cerapeel the cerapeel to set
     */
    public void setCerapeel(String cerapeel) {
        this.cerapeel = cerapeel;
    }

    /**
     * 緩衝材1
     *
     * @return the kansyouzai1
     */
    public Integer getKansyouzai1() {
        return kansyouzai1;
    }

    /**
     * 緩衝材1
     *
     * @param kansyouzai1 the kansyouzai1 to set
     */
    public void setKansyouzai1(Integer kansyouzai1) {
        this.kansyouzai1 = kansyouzai1;
    }

    /**
     * SUS板
     *
     * @return the susborad
     */
    public Integer getSusborad() {
        return susborad;
    }

    /**
     * SUS板
     *
     * @param susborad the susborad to set
     */
    public void setSusborad(Integer susborad) {
        this.susborad = susborad;
    }

    /**
     * 緩衝材2
     *
     * @return the kansyouzai2
     */
    public Integer getKansyouzai2() {
        return kansyouzai2;
    }

    /**
     * 緩衝材2
     *
     * @param kansyouzai2 the kansyouzai2 to set
     */
    public void setKansyouzai2(Integer kansyouzai2) {
        this.kansyouzai2 = kansyouzai2;
    }

    /**
     * 静水圧ﾌﾟﾚｽ号機
     *
     * @return the seisuiatupressgouki
     */
    public String getSeisuiatupressgouki() {
        return seisuiatupressgouki;
    }

    /**
     * 静水圧ﾌﾟﾚｽ号機
     *
     * @param seisuiatupressgouki the seisuiatupressgouki to set
     */
    public void setSeisuiatupressgouki(String seisuiatupressgouki) {
        this.seisuiatupressgouki = seisuiatupressgouki;
    }

    /**
     * 予熱時間1
     *
     * @return the yonetujikan1
     */
    public Integer getYonetujikan1() {
        return yonetujikan1;
    }

    /**
     * 予熱時間1
     *
     * @param yonetujikan1 the yonetujikan1 to set
     */
    public void setYonetujikan1(Integer yonetujikan1) {
        this.yonetujikan1 = yonetujikan1;
    }

    /**
     * 予熱時間2
     *
     * @return the yonetujikan2
     */
    public Integer getYonetujikan2() {
        return yonetujikan2;
    }

    /**
     * 予熱時間2
     *
     * @param yonetujikan2 the yonetujikan2 to set
     */
    public void setYonetujikan2(Integer yonetujikan2) {
        this.yonetujikan2 = yonetujikan2;
    }

    /**
     * 予熱時間3
     *
     * @return the yonetujikan3
     */
    public Integer getYonetujikan3() {
        return yonetujikan3;
    }

    /**
     * 予熱時間3
     *
     * @param yonetujikan3 the yonetujikan3 to set
     */
    public void setYonetujikan3(Integer yonetujikan3) {
        this.yonetujikan3 = yonetujikan3;
    }

    /**
     * 1次最高圧力
     *
     * @return the aturyoku1max
     */
    public Integer getAturyoku1max() {
        return aturyoku1max;
    }

    /**
     * 1次最高圧力
     *
     * @param aturyoku1max the aturyoku1max to set
     */
    public void setAturyoku1max(Integer aturyoku1max) {
        this.aturyoku1max = aturyoku1max;
    }

    /**
     * 2次最高圧力
     *
     * @return the aturyoku2max
     */
    public Integer getAturyoku2max() {
        return aturyoku2max;
    }

    /**
     * 2次最高圧力
     *
     * @param aturyoku2max the aturyoku2max to set
     */
    public void setAturyoku2max(Integer aturyoku2max) {
        this.aturyoku2max = aturyoku2max;
    }

    /**
     * 水ﾇﾚｾｯﾄ数
     *
     * @return the mizunureSetsuu
     */
    public Integer getMizunureSetsuu() {
        return mizunureSetsuu;
    }

    /**
     * 水ﾇﾚｾｯﾄ数
     *
     * @param mizunureSetsuu the mizunureSetsuu to set
     */
    public void setMizunureSetsuu(Integer mizunureSetsuu) {
        this.mizunureSetsuu = mizunureSetsuu;
    }

    /**
     * ﾌﾟﾚｽ後冷却時間
     *
     * @return the pressgoreikyakujikan
     */
    public Integer getPressgoreikyakujikan() {
        return pressgoreikyakujikan;
    }

    /**
     * ﾌﾟﾚｽ後冷却時間
     *
     * @param pressgoreikyakujikan the pressgoreikyakujikan to set
     */
    public void setPressgoreikyakujikan(Integer pressgoreikyakujikan) {
        this.pressgoreikyakujikan = pressgoreikyakujikan;
    }

    /**
     * 水系TC Niﾌﾟﾚｽ前ｴｰｼﾞﾝｸﾞ
     *
     * @return the pressmaeaging
     */
    public Integer getPressmaeaging() {
        return pressmaeaging;
    }

    /**
     * 水系TC Niﾌﾟﾚｽ前ｴｰｼﾞﾝｸﾞ
     *
     * @param pressmaeaging the pressmaeaging to set
     */
    public void setPressmaeaging(Integer pressmaeaging) {
        this.pressmaeaging = pressmaeaging;
    }

    /**
     * 終了担当者
     *
     * @return the endtantousyacode
     */
    public String getEndtantousyacode() {
        return endtantousyacode;
    }

    /**
     * 終了担当者
     *
     * @param endtantousyacode the endtantousyacode to set
     */
    public void setEndtantousyacode(String endtantousyacode) {
        this.endtantousyacode = endtantousyacode;
    }

    /**
     * 処理ｾｯﾄ数
     *
     * @return the setsuu
     */
    public Integer getSetsuu() {
        return setsuu;
    }

    /**
     * 処理ｾｯﾄ数
     *
     * @param setsuu the setsuu to set
     */
    public void setSetsuu(Integer setsuu) {
        this.setsuu = setsuu;
    }

    /**
     * 良品ｾｯﾄ数
     *
     * @return the ryouhinsetsuu
     */
    public Integer getRyouhinsetsuu() {
        return ryouhinsetsuu;
    }

    /**
     * 良品ｾｯﾄ数
     *
     * @param ryouhinsetsuu the ryouhinsetsuu to set
     */
    public void setRyouhinsetsuu(Integer ryouhinsetsuu) {
        this.ryouhinsetsuu = ryouhinsetsuu;
    }

    /**
     * ﾌﾟﾚｽ回数
     *
     * @return the presskaisuu
     */
    public Integer getPresskaisuu() {
        return presskaisuu;
    }

    /**
     * ﾌﾟﾚｽ回数
     *
     * @param presskaisuu the presskaisuu to set
     */
    public void setPresskaisuu(Integer presskaisuu) {
        this.presskaisuu = presskaisuu;
    }

    /**
     * 登録日時
     *
     * @return the torokunichiji
     */
    public Timestamp getTorokunichiji() {
        return torokunichiji;
    }

    /**
     * 登録日時
     *
     * @param torokunichiji the torokunichiji to set
     */
    public void setTorokunichiji(Timestamp torokunichiji) {
        this.torokunichiji = torokunichiji;
    }

    /**
     * 更新日時
     *
     * @return the kosinnichiji
     */
    public Timestamp getKosinnichiji() {
        return kosinnichiji;
    }

    /**
     * 更新日時
     *
     * @param kosinnichiji the kosinnichiji to set
     */
    public void setKosinnichiji(Timestamp kosinnichiji) {
        this.kosinnichiji = kosinnichiji;
    }

    /**
     * @return the kcpno
     */
    public String getKcpno() {
        return kcpno;
    }

    /**
     * @param kcpno the kcpno to set
     */
    public void setKcpno(String kcpno) {
        this.kcpno = kcpno;
    }

    /**
     * revision
     *
     * @return the revision
     */
    public Long getRevision() {
        return revision;
    }

    /**
     * revision
     *
     * @param revision the revision to set
     */
    public void setRevision(Long revision) {
        this.revision = revision;
    }

    /**
     * 削除ﾌﾗｸﾞ
     *
     * @return the deleteflag
     */
    public Integer getDeleteflag() {
        return deleteflag;
    }

    /**
     * 削除ﾌﾗｸﾞ
     *
     * @param deleteflag the deleteflag to set
     */
    public void setDeleteflag(Integer deleteflag) {
        this.deleteflag = deleteflag;
    }

}
