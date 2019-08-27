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
 * 変更日       2019/08/07<br>
 * 計画書No     K1811-DS001<br>
 * 変更者       KCSS K.Jo<br>
 * 変更理由     新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * ｶｯﾄ・生品質検査履歴検索画面のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2019/08/07
 */
public class GXHDO201B012Model implements Serializable {

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
     * 担当者
     */
    private String tantousya = "";

    /**
     * 確認者
     */
    private String kakuninsya = "";

    /**
     * 厚みMIN
     */
    private Integer atumimin = null;

    /**
     * 厚みMAX
     */
    private Integer atumimax = null;

    /**
     * 厚み01
     */
    private Integer atumi01 = null;

    /**
     * 厚み02
     */
    private Integer atumi02 = null;

    /**
     * 厚み03
     */
    private Integer atumi03 = null;

    /**
     * 厚み04
     */
    private Integer atumi04 = null;

    /**
     * 厚み05
     */
    private Integer atumi05 = null;

    /**
     * 厚み06
     */
    private Integer atumi06 = null;

    /**
     * 厚み07
     */
    private Integer atumi07 = null;

    /**
     * 厚み08
     */
    private Integer atumi08 = null;

    /**
     * 厚み09
     */
    private Integer atumi09 = null;

    /**
     * 厚み10
     */
    private Integer atumi10 = null;

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
     * 総重量
     */
    private BigDecimal soujyuryo = null;

    /**
     * 単位重量
     */
    private BigDecimal tanijyuryo = null;

    /**
     * 外観検査終了担当者
     */
    private String gaikankensatantousya = "";

    /**
     * ばらし方法
     */
    private String barasshi = "";

    /**
     * 条件
     */
    private Integer joken = null;

    /**
     * ばらし開始日時
     */
    private Timestamp barashistartnichiji = null;

    /**
     * ばらし開始担当者
     */
    private String batashistarttantousya = "";

    /**
     * ばらし終了日時
     */
    private Timestamp barashiendnichiji = null;

    /**
     * ばらし終了担当者
     */
    private String barashiendtantousya = "";

    /**
     * 粉まぶし
     */
    private String konamabushi = "";

    /**
     * 処理個数
     */
    private Integer syorisetsuu = null;

    /**
     * 良品個数
     */
    private Integer ryouhinsetsuu = null;

    /**
     * 歩留まり
     */
    private BigDecimal budomari = null;

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
     * @return atumi01
     */
    public Integer getAtumi01() {
        return atumi01;
    }

    /**
     * @param atumi01 セットする atumi01
     */
    public void setAtumi01(Integer atumi01) {
        this.atumi01 = atumi01;
    }

    /**
     * @return atumi02
     */
    public Integer getAtumi02() {
        return atumi02;
    }

    /**
     * @param atumi02 セットする atumi02
     */
    public void setAtumi02(Integer atumi02) {
        this.atumi02 = atumi02;
    }

    /**
     * @return atumi03
     */
    public Integer getAtumi03() {
        return atumi03;
    }

    /**
     * @param atumi03 セットする atumi03
     */
    public void setAtumi03(Integer atumi03) {
        this.atumi03 = atumi03;
    }

    /**
     * @return atumi04
     */
    public Integer getAtumi04() {
        return atumi04;
    }

    /**
     * @param atumi04 セットする atumi04
     */
    public void setAtumi04(Integer atumi04) {
        this.atumi04 = atumi04;
    }

    /**
     * @return atumi05
     */
    public Integer getAtumi05() {
        return atumi05;
    }

    /**
     * @param atumi05 セットする atumi05
     */
    public void setAtumi05(Integer atumi05) {
        this.atumi05 = atumi05;
    }

    /**
     * @return atumi06
     */
    public Integer getAtumi06() {
        return atumi06;
    }

    /**
     * @param atumi06 セットする atumi06
     */
    public void setAtumi06(Integer atumi06) {
        this.atumi06 = atumi06;
    }

    /**
     * @return atumi07
     */
    public Integer getAtumi07() {
        return atumi07;
    }

    /**
     * @param atumi07 セットする atumi07
     */
    public void setAtumi07(Integer atumi07) {
        this.atumi07 = atumi07;
    }

    /**
     * @return atumi08
     */
    public Integer getAtumi08() {
        return atumi08;
    }

    /**
     * @param atumi08 セットする atumi08
     */
    public void setAtumi08(Integer atumi08) {
        this.atumi08 = atumi08;
    }

    /**
     * @return atumi09
     */
    public Integer getAtumi09() {
        return atumi09;
    }

    /**
     * @param atumi09 セットする atumi09
     */
    public void setAtumi09(Integer atumi09) {
        this.atumi09 = atumi09;
    }

    /**
     * @return atumi10
     */
    public Integer getAtumi10() {
        return atumi10;
    }

    /**
     * @param atumi10 セットする atumi10
     */
    public void setAtumi10(Integer atumi10) {
        this.atumi10 = atumi10;
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
     * @return soujyuryo
     */
    public BigDecimal getSoujyuryo() {
        return soujyuryo;
    }

    /**
     * @param soujyuryo セットする soujyuryo
     */
    public void setSoujyuryo(BigDecimal soujyuryo) {
        this.soujyuryo = soujyuryo;
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
     * @return gaikankensatantousya
     */
    public String getGaikankensatantousya() {
        return gaikankensatantousya;
    }

    /**
     * @param gaikankensatantousya セットする gaikankensatantousya
     */
    public void setGaikankensatantousya(String gaikankensatantousya) {
        this.gaikankensatantousya = gaikankensatantousya;
    }

    /**
     * @return barasshi
     */
    public String getBarasshi() {
        return barasshi;
    }

    /**
     * @param barasshi セットする barasshi
     */
    public void setBarasshi(String barasshi) {
        this.barasshi = barasshi;
    }

    /**
     * @return joken
     */
    public Integer getJoken() {
        return joken;
    }

    /**
     * @param joken セットする joken
     */
    public void setJoken(Integer joken) {
        this.joken = joken;
    }

    /**
     * @return barashistartnichiji
     */
    public Timestamp getBarashistartnichiji() {
        return barashistartnichiji;
    }

    /**
     * @param barashistartnichiji セットする barashistartnichiji
     */
    public void setBarashistartnichiji(Timestamp barashistartnichiji) {
        this.barashistartnichiji = barashistartnichiji;
    }

    /**
     * @return batashistarttantousya
     */
    public String getBatashistarttantousya() {
        return batashistarttantousya;
    }

    /**
     * @param batashistarttantousya セットする batashistarttantousya
     */
    public void setBatashistarttantousya(String batashistarttantousya) {
        this.batashistarttantousya = batashistarttantousya;
    }

    /**
     * @return barashiendnichiji
     */
    public Timestamp getBarashiendnichiji() {
        return barashiendnichiji;
    }

    /**
     * @param barashiendnichiji セットする barashiendnichiji
     */
    public void setBarashiendnichiji(Timestamp barashiendnichiji) {
        this.barashiendnichiji = barashiendnichiji;
    }

    /**
     * @return barashiendtantousya
     */
    public String getBarashiendtantousya() {
        return barashiendtantousya;
    }

    /**
     * @param barashiendtantousya セットする barashiendtantousya
     */
    public void setBarashiendtantousya(String barashiendtantousya) {
        this.barashiendtantousya = barashiendtantousya;
    }

    /**
     * @return konamabushi
     */
    public String getKonamabushi() {
        return konamabushi;
    }

    /**
     * @param konamabushi セットする konamabushi
     */
    public void setKonamabushi(String konamabushi) {
        this.konamabushi = konamabushi;
    }

    /**
     * @return syorisetsuu
     */
    public Integer getSyorisetsuu() {
        return syorisetsuu;
    }

    /**
     * @param syorisetsuu セットする syorisetsuu
     */
    public void setSyorisetsuu(Integer syorisetsuu) {
        this.syorisetsuu = syorisetsuu;
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




}
