/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日       2019/08/05<br>
 * 計画書No     K1811-DS001<br>
 * 変更者       KCSS K.Jo<br>
 * 変更理由     新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 焼成・再酸化履歴検索画面のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2019/08/05
 */
public class GXHDO201B019Model implements Serializable {

    /**
     * ﾛｯﾄNo.
     */
    private String lotno = "";

    /**
     * KCPNO
     */
    private String kcpno = "";

    /**
     * 実績No
     */
    private Integer jissekino = null;

    /**
     * 受入セッタ枚数
     */
    private Integer ukeiresettamaisuu = null;

    /**
     * 指定再酸化回数
     */
    private Integer siteisaisaka = null;

    /**
     * 投入セッタ枚数
     */
    private Integer tounyusettasuu = null;

    /**
     * 号機
     */
    private String gouki = "";

    /**
     * 設定ﾊﾟﾀｰﾝ
     */
    private String setteipattern = "";

    /**
     * ｷｰﾌﾟ温度
     */
    private Integer keepondo = null;

    /**
     * 後外観確認
     */
    private String atogaikan = "";

    /**
     * 回収セッタ枚数
     */
    private Integer kaishusettasuu = null;

    /**
     * 開始日時
     */
    private Timestamp kaisinichiji = null;

    /**
     * 開始担当者
     */
    private String starttantosyacode = "";

    /**
     * 開始確認者
     */
    private String startkakuninsyacode = "";

    /**
     * 終了日時
     */
    private Timestamp syuuryounichiji = null;

    /**
     * 終了担当者
     */
    private String endtantosyacode = "";

    /**
     * 備考1
     */
    private String bikou1 = "";

    /**
     * 備考2
     */
    private String bikou2 = "";

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
     * @return jissekino
     */
    public Integer getJissekino() {
        return jissekino;
    }

    /**
     * @param jissekino セットする jissekino
     */
    public void setJissekino(Integer jissekino) {
        this.jissekino = jissekino;
    }

    /**
     * @return ukeiresettamaisuu
     */
    public Integer getUkeiresettamaisuu() {
        return ukeiresettamaisuu;
    }

    /**
     * @param ukeiresettamaisuu セットする ukeiresettamaisuu
     */
    public void setUkeiresettamaisuu(Integer ukeiresettamaisuu) {
        this.ukeiresettamaisuu = ukeiresettamaisuu;
    }

    /**
     * @return siteisaisaka
     */
    public Integer getSiteisaisaka() {
        return siteisaisaka;
    }

    /**
     * @param siteisaisaka セットする siteisaisaka
     */
    public void setSiteisaisaka(Integer siteisaisaka) {
        this.siteisaisaka = siteisaisaka;
    }

    /**
     * @return tounyusettasuu
     */
    public Integer getTounyusettasuu() {
        return tounyusettasuu;
    }

    /**
     * @param tounyusettasuu セットする tounyusettasuu
     */
    public void setTounyusettasuu(Integer tounyusettasuu) {
        this.tounyusettasuu = tounyusettasuu;
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
     * @return setteipattern
     */
    public String getSetteipattern() {
        return setteipattern;
    }

    /**
     * @param setteipattern セットする setteipattern
     */
    public void setSetteipattern(String setteipattern) {
        this.setteipattern = setteipattern;
    }

    /**
     * @return keepondo
     */
    public Integer getKeepondo() {
        return keepondo;
    }

    /**
     * @param keepondo セットする keepondo
     */
    public void setKeepondo(Integer keepondo) {
        this.keepondo = keepondo;
    }

    /**
     * @return atogaikan
     */
    public String getAtogaikan() {
        return atogaikan;
    }

    /**
     * @param atogaikan セットする atogaikan
     */
    public void setAtogaikan(String atogaikan) {
        this.atogaikan = atogaikan;
    }

    /**
     * @return kaishusettasuu
     */
    public Integer getKaishusettasuu() {
        return kaishusettasuu;
    }

    /**
     * @param kaishusettasuu セットする kaishusettasuu
     */
    public void setKaishusettasuu(Integer kaishusettasuu) {
        this.kaishusettasuu = kaishusettasuu;
    }

    /**
     * @return kaisinichiji
     */
    public Timestamp getKaisinichiji() {
        return kaisinichiji;
    }

    /**
     * @param kaisinichiji セットする kaisinichiji
     */
    public void setKaisinichiji(Timestamp kaisinichiji) {
        this.kaisinichiji = kaisinichiji;
    }

    /**
     * @return starttantosyacode
     */
    public String getStarttantosyacode() {
        return starttantosyacode;
    }

    /**
     * @param starttantosyacode セットする starttantosyacode
     */
    public void setStarttantosyacode(String starttantosyacode) {
        this.starttantosyacode = starttantosyacode;
    }

    /**
     * @return startkakuninsyacode
     */
    public String getStartkakuninsyacode() {
        return startkakuninsyacode;
    }

    /**
     * @param startkakuninsyacode セットする startkakuninsyacode
     */
    public void setStartkakuninsyacode(String startkakuninsyacode) {
        this.startkakuninsyacode = startkakuninsyacode;
    }

    /**
     * @return syuuryounichiji
     */
    public Timestamp getSyuuryounichiji() {
        return syuuryounichiji;
    }

    /**
     * @param syuuryounichiji セットする syuuryounichiji
     */
    public void setSyuuryounichiji(Timestamp syuuryounichiji) {
        this.syuuryounichiji = syuuryounichiji;
    }

    /**
     * @return endtantosyacode
     */
    public String getEndtantosyacode() {
        return endtantosyacode;
    }

    /**
     * @param endtantosyacode セットする endtantosyacode
     */
    public void setEndtantosyacode(String endtantosyacode) {
        this.endtantosyacode = endtantosyacode;
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
}
