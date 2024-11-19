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
 * 変更日	2019/12/02<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	863 F.Zhang<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 外部電極・ﾒｯｷ品質検査履歴検索画面のモデルクラスです。
 *
 * @author 863 F.Zhang
 * @since 2019/12/02
 */
public class GXHDO201B038Model implements Serializable {

    /**
     * ﾛｯﾄNo
     */
    private String lotno = "";

    /**
     * KCPNO
     */
    private String kcpno = "";

    /**
     * 処理数
     */
    private Integer ukeiresuu = null;

    /**
     * ﾄﾞｰﾑ個数
     */
    private Integer domekosuu = null;

    /**
     * 号機
     */
    private String gouki = "";

    /**
     * 開始担当者
     */
    private String starttantosyacode = "";

    /**
     * ﾒｯｷ開始日時
     */
    private Timestamp mekkikaishinichiji = null;

    /**
     * 条件NI(A)
     */
    private BigDecimal mekkijyoukennia = null;

    /**
     * 条件NI(AM)
     */
    private Integer mekkijyoukenniam = null;

    /**
     * 条件SN(A)
     */
    private BigDecimal mekkijyoukensna = null;

    /**
     * 条件SN(AM)
     */
    private Integer mekkijyoukensnam = null;

    /**
     * 良品数
     */
    private Integer shukkakosuu = null;

    /**
     * 歩留まり
     */
    private BigDecimal budomari = null;

    /**
     * Ni膜厚(MIN)
     */
    private BigDecimal makuatsunimin = null;

    /**
     * Ni膜厚(MAX)
     */
    private BigDecimal makuatsunimax = null;

    /**
     * Ni膜厚(AVE)
     */
    private BigDecimal makuatsuniave = null;

    /**
     * Ni膜厚(STD)
     */
    private BigDecimal makuatsunistd = null;

    /**
     * Sn膜厚(MIN)
     */
    private BigDecimal makuatsusnmin = null;

    /**
     * Sn膜厚(MAX)
     */
    private BigDecimal makuatsusnmax = null;

    /**
     * Sn膜厚(AVE)
     */
    private BigDecimal makuatsusnave = null;

    /**
     * Sn膜厚(STD)
     */
    private BigDecimal makuatsusnstd = null;

    /**
     * 半田ﾇﾚ性
     */
    private String nurekensakekka = "";

    /**
     * 半田耐熱性
     */
    private String tainetsukensakekka = "";

    /**
     * 外観
     */
    private String gaikankensakekka = "";

    /**
     * 備考1
     */
    private String biko1 = "";

    /**
     * 備考2
     */
    private String biko2 = "";

    /**
     * 備考3
     */
    private String biko3 = "";

    /**
     * 備考4
     */
    private String biko4 = "";

    /**
     * 備考5
     */
    private String biko5 = "";

    /**
     * 回数
     */
    private Integer jissekino = null;

    /**
     * 使用ﾄﾞｰﾑ明細
     */
    private String domemeisai = "";

    /**
     * 1回目調整前PH値
     */
    private BigDecimal tyoseimaeph1 = null;

    /**
     * 1回目調整後PH値
     */
    private BigDecimal tyoseigoph1 = null;

    /**
     * 1回目調整時間
     */
    private Integer tyoseijikan1 = null;

    /**
     * 2回目調整前PH値
     */
    private BigDecimal tyoseimaeph2 = null;

    /**
     * 2回目調整後PH値
     */
    private BigDecimal tyoseigoph2 = null;

    /**
     * 2回目調整時間
     */
    private Integer tyoseijikan2 = null;

    /**
     * Ｔ寸法
     */
    private BigDecimal tsunpou = null;

    /**
     * ﾊﾞﾚﾙNo
     */
    private Integer barrelno = null;

    /**
     * Ni膜厚(CPL)
     */
    private BigDecimal makuatsunicpl = null;

    /**
     * Sn膜厚(CPL)
     */
    private BigDecimal makuatsusncpl = null;

    /**
     * 測定日時
     */
    private Timestamp sokuteinichiji = null;

    /**
     * Ni膜厚(CV)
     */
    private BigDecimal makuatsunicv = null;

    /**
     * Sn膜厚(CV)
     */
    private BigDecimal makuatsusncv = null;

    /**
     * 検査日時
     */
    private Timestamp kensanichiji = null;

    /**
     * 検査・外観担当者
     */
    private String kensatantousya = "";

    /**
     * 膜厚担当者
     */
    private String makuatsutantosya = "";

    /**
     * Sn開始日時
     */
    private Timestamp kaishinichiji_sn = null;

    /**
     * 客先
     */
    private String tokuisaki = "";

    /**
     * ﾛｯﾄ区分
     */
    private String lotkubuncode = "";

    /**
     * ｵｰﾅｰ
     */
    private String ownercode = "";

    /**
     * 受入れ単位重量
     */
    private BigDecimal ukeiretannijyuryo = null;

    /**
     * 受入れ総重量
     */
    private BigDecimal ukeiresoujyuryou = null;

    /**
     * ﾒｯｷ場所
     */
    private String mekkibasyo = "";

    /**
     * ﾒｯｷ場所設備
     */
    private String mekkibasyosetubi = "";

    /**
     * ﾒｯｷ終了日時
     */
    private Timestamp mekkisyuryounichiji = null;

    /**
     * 終了担当者
     */
    private String syuryousya = "";

    /**
     * 検査単位重量
     */
    private BigDecimal kensatannijyuryo = null;

    /**
     * 検査総重量
     */
    private BigDecimal kensasoujyuryou = null;

    /**
     * 熱処理条件
     */
    private String netusyorijyouken = "";

    /**
     * 熱処理開始日時
     */
    private Timestamp netusyorikaisinichiji = null;

    /**
     * 熱処理担当者
     */
    private String netusyoritantousya = "";

    /**
     * 磁石選別開始日時
     */
    private Timestamp jisyakusenbetukaisinichiji = null;

    /**
     * 磁石選別担当者
     */
    private String jisyakusenbetutantousya = "";

    /**
     * 異常発行
     */
    private String ijouhakkou = "";

    /**
     * 異常品ﾗﾝｸ
     */
    private String ijourank = "";

    /**
     * 膜厚確認
     */
    private String makuatsukakunin = "";

    /**
     * ﾃｽﾄ品
     */
    private String testhin = "";

    /**
     * T寸法AVE
     */
    private BigDecimal tsunpouave = null;

    /**
     * ﾒｯｷ種類
     */
    private String mekkisyurui = "";

    /**
     * Ni膜厚01
     */
    private BigDecimal makuatsuni01 = null;
    /**
     * Ni膜厚02
     */
    private BigDecimal makuatsuni02 = null;
    /**
     * Ni膜厚03
     */
    private BigDecimal makuatsuni03 = null;
    /**
     * Ni膜厚04
     */
    private BigDecimal makuatsuni04 = null;
    /**
     * Ni膜厚05
     */
    private BigDecimal makuatsuni05 = null;
    /**
     * Ni膜厚06
     */
    private BigDecimal makuatsuni06 = null;
    /**
     * Ni膜厚07
     */
    private BigDecimal makuatsuni07 = null;
    /**
     * Ni膜厚08
     */
    private BigDecimal makuatsuni08 = null;
    /**
     * Ni膜厚09
     */
    private BigDecimal makuatsuni09 = null;
    /**
     * Ni膜厚10
     */
    private BigDecimal makuatsuni10 = null;
    /**
     * Ni膜厚11
     */
    private BigDecimal makuatsuni11 = null;
    /**
     * Ni膜厚12
     */
    private BigDecimal makuatsuni12 = null;
    /**
     * Ni膜厚13
     */
    private BigDecimal makuatsuni13 = null;
    /**
     * Ni膜厚14
     */
    private BigDecimal makuatsuni14 = null;
    /**
     * Ni膜厚15
     */
    private BigDecimal makuatsuni15 = null;
    /**
     * Ni膜厚16
     */
    private BigDecimal makuatsuni16 = null;
    /**
     * Ni膜厚17
     */
    private BigDecimal makuatsuni17 = null;
    /**
     * Ni膜厚18
     */
    private BigDecimal makuatsuni18 = null;
    /**
     * Ni膜厚19
     */
    private BigDecimal makuatsuni19 = null;
    /**
     * Ni膜厚20
     */
    private BigDecimal makuatsuni20 = null;
    /**
     * Sn膜厚01
     */
    private BigDecimal makuatsusn01 = null;
    /**
     * Sn膜厚02
     */
    private BigDecimal makuatsusn02 = null;
    /**
     * Sn膜厚03
     */
    private BigDecimal makuatsusn03 = null;
    /**
     * Sn膜厚04
     */
    private BigDecimal makuatsusn04 = null;
    /**
     * Sn膜厚05
     */
    private BigDecimal makuatsusn05 = null;
    /**
     * Sn膜厚06
     */
    private BigDecimal makuatsusn06 = null;
    /**
     * Sn膜厚07
     */
    private BigDecimal makuatsusn07 = null;
    /**
     * Sn膜厚08
     */
    private BigDecimal makuatsusn08 = null;
    /**
     * Sn膜厚09
     */
    private BigDecimal makuatsusn09 = null;
    /**
     * Sn膜厚10
     */
    private BigDecimal makuatsusn10 = null;
    /**
     * Sn膜厚11
     */
    private BigDecimal makuatsusn11 = null;
    /**
     * Sn膜厚12
     */
    private BigDecimal makuatsusn12 = null;
    /**
     * Sn膜厚13
     */
    private BigDecimal makuatsusn13 = null;
    /**
     * Sn膜厚14
     */
    private BigDecimal makuatsusn14 = null;
    /**
     * Sn膜厚15
     */
    private BigDecimal makuatsusn15 = null;
    /**
     * Sn膜厚16
     */
    private BigDecimal makuatsusn16 = null;
    /**
     * Sn膜厚17
     */
    private BigDecimal makuatsusn17 = null;
    /**
     * Sn膜厚18
     */
    private BigDecimal makuatsusn18 = null;
    /**
     * Sn膜厚19
     */
    private BigDecimal makuatsusn19 = null;
    /**
     * Sn膜厚20
     */
    private BigDecimal makuatsusn20 = null;

    /**
     * 熱処理担当者
     */
    private String soujyuryoutantousya = "";

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
     * KCPNO
     *
     * @return the kcpno
     */
    public String getKcpno() {
        return kcpno;
    }

    /**
     * KCPNO
     *
     * @param kcpno the kcpno to set
     */
    public void setKcpno(String kcpno) {
        this.kcpno = kcpno;
    }

    /**
     * ﾒｯｷ開始日時
     *
     * @return the startdatetime
     */
    public Timestamp getMekkikaishinichiji() {
        return mekkikaishinichiji;
    }

    /**
     * ﾒｯｷ開始日時
     *
     * @param mekkikaishinichiji the mekkikaishinichiji to set
     */
    public void setMekkikaishinichiji(Timestamp mekkikaishinichiji) {
        this.mekkikaishinichiji = mekkikaishinichiji;
    }

    /**
     * 号機
     *
     * @return the gouki
     */
    public String getGouki() {
        return gouki;
    }

    /**
     * 号機
     *
     * @param gouki the gouki to set
     */
    public void setGouki(String gouki) {
        this.gouki = gouki;
    }

    /**
     * 備考1
     *
     * @return the biko1
     */
    public String getBiko1() {
        return biko1;
    }

    /**
     * 備考1
     *
     * @param biko1 the biko1 to set
     */
    public void setBiko1(String biko1) {
        this.biko1 = biko1;
    }

    /**
     * 備考2
     *
     * @return the biko2
     */
    public String getBiko2() {
        return biko2;
    }

    /**
     * 備考2
     *
     * @param biko2 the biko2 to set
     */
    public void setBiko2(String biko2) {
        this.biko2 = biko2;
    }

    /**
     * 開始担当者
     *
     * @return the starttantosyacode
     */
    public String getStarttantosyacode() {
        return starttantosyacode;
    }

    /**
     * 開始担当者
     *
     * @param starttantosyacode the starttantosyacode to set
     */
    public void setStarttantosyacode(String starttantosyacode) {
        this.starttantosyacode = starttantosyacode;
    }

    /**
     * 客先
     *
     * @return the tokuisaki
     */
    public String getTokuisaki() {
        return tokuisaki;
    }

    /**
     * 客先
     *
     * @param tokuisaki the tokuisaki to set
     */
    public void setTokuisaki(String tokuisaki) {
        this.tokuisaki = tokuisaki;
    }

    /**
     * ﾛｯﾄ区分
     *
     * @return the lotkubuncode
     */
    public String getLotkubuncode() {
        return lotkubuncode;
    }

    /**
     * ﾛｯﾄ区分
     *
     * @param lotkubuncode the lotkubuncode to set
     */
    public void setLotkubuncode(String lotkubuncode) {
        this.lotkubuncode = lotkubuncode;
    }

    /**
     * ｵｰﾅｰ
     *
     * @return the ownercode
     */
    public String getOwnercode() {
        return ownercode;
    }

    /**
     * ｵｰﾅｰ
     *
     * @param ownercode the ownercode to set
     */
    public void setOwnercode(String ownercode) {
        this.ownercode = ownercode;
    }

    /**
     * 回数
     *
     * @return the jissekino
     */
    public Integer getJissekino() {
        return jissekino;
    }

    /**
     * 回数
     *
     * @param jissekino the jissekino to set
     */
    public void setJissekino(Integer jissekino) {
        this.jissekino = jissekino;
    }

    /**
     * 処理数
     *
     * @return the ukeiresuu
     */
    public Integer getUkeiresuu() {
        return ukeiresuu;
    }

    /**
     * 処理数
     *
     * @param ukeiresuu the ukeiresuu to set
     */
    public void setUkeiresuu(Integer ukeiresuu) {
        this.ukeiresuu = ukeiresuu;
    }

    /**
     * ﾄﾞｰﾑ個数
     *
     * @return the domekosuu
     */
    public Integer getDomekosuu() {
        return domekosuu;
    }

    /**
     * ﾄﾞｰﾑ個数
     *
     * @param domekosuu the domekosuu to set
     */
    public void setDomekosuu(Integer domekosuu) {
        this.domekosuu = domekosuu;
    }

    /**
     * 条件NI(A)
     *
     * @return the mekkijyoukennia
     */
    public BigDecimal getMekkijyoukennia() {
        return mekkijyoukennia;
    }

    /**
     * 条件NI(A)
     *
     * @param mekkijyoukennia the mekkijyoukennia to set
     */
    public void setMekkijyoukennia(BigDecimal mekkijyoukennia) {
        this.mekkijyoukennia = mekkijyoukennia;
    }

    /**
     * 条件NI(AM)
     *
     * @return the mekkijyoukenniam
     */
    public Integer getMekkijyoukenniam() {
        return mekkijyoukenniam;
    }

    /**
     * 条件NI(AM)
     *
     * @param mekkijyoukenniam the mekkijyoukenniam to set
     */
    public void setMekkijyoukenniam(Integer mekkijyoukenniam) {
        this.mekkijyoukenniam = mekkijyoukenniam;
    }

    /**
     * 条件SN(A)
     *
     * @return the mekkijyoukensna
     */
    public BigDecimal getMekkijyoukensna() {
        return mekkijyoukensna;
    }

    /**
     * 条件SN(A)
     *
     * @param mekkijyoukensna the mekkijyoukensna to set
     */
    public void setMekkijyoukensna(BigDecimal mekkijyoukensna) {
        this.mekkijyoukensna = mekkijyoukensna;
    }

    /**
     * 条件SN(AM)
     *
     * @return the mekkijyoukensnam
     */
    public Integer getMekkijyoukensnam() {
        return mekkijyoukensnam;
    }

    /**
     * 条件SN(AM)
     *
     * @param mekkijyoukensnam the mekkijyoukensnam to set
     */
    public void setMekkijyoukensnam(Integer mekkijyoukensnam) {
        this.mekkijyoukensnam = mekkijyoukensnam;
    }

    /**
     * 良品数
     *
     * @return the shukkakosuu
     */
    public Integer getShukkakosuu() {
        return shukkakosuu;
    }

    /**
     * 良品数
     *
     * @param shukkakosuu the shukkakosuu to set
     */
    public void setShukkakosuu(Integer shukkakosuu) {
        this.shukkakosuu = shukkakosuu;
    }

    /**
     * 歩留まり
     *
     * @return the budomari
     */
    public BigDecimal getBudomari() {
        return budomari;
    }

    /**
     * 歩留まり
     *
     * @param budomari the budomari to set
     */
    public void setBudomari(BigDecimal budomari) {
        this.budomari = budomari;
    }

    /**
     * Ni膜厚(MIN)
     *
     * @return the makuatsunimin
     */
    public BigDecimal getMakuatsunimin() {
        return makuatsunimin;
    }

    /**
     * Ni膜厚(MIN)
     *
     * @param makuatsunimin the makuatsunimin to set
     */
    public void setMakuatsunimin(BigDecimal makuatsunimin) {
        this.makuatsunimin = makuatsunimin;
    }

    /**
     * Ni膜厚(MAX)
     *
     * @return the makuatsunimax
     */
    public BigDecimal getMakuatsunimax() {
        return makuatsunimax;
    }

    /**
     * Ni膜厚(MAX)
     *
     * @param makuatsunimax the makuatsunimax to set
     */
    public void setMakuatsunimax(BigDecimal makuatsunimax) {
        this.makuatsunimax = makuatsunimax;
    }

    /**
     * Ni膜厚(AVE)
     *
     * @return the makuatsuniave
     */
    public BigDecimal getMakuatsuniave() {
        return makuatsuniave;
    }

    /**
     * Ni膜厚(AVE)
     *
     * @param makuatsuniave the makuatsuniave to set
     */
    public void setMakuatsuniave(BigDecimal makuatsuniave) {
        this.makuatsuniave = makuatsuniave;
    }

    /**
     * Ni膜厚(STD)
     *
     * @return the makuatsunistd
     */
    public BigDecimal getMakuatsunistd() {
        return makuatsunistd;
    }

    /**
     * Ni膜厚(STD)
     *
     * @param makuatsunistd the makuatsunistd to set
     */
    public void setMakuatsunistd(BigDecimal makuatsunistd) {
        this.makuatsunistd = makuatsunistd;
    }

    /**
     * Sn膜厚(MIN)
     *
     * @return the makuatsusnmin
     */
    public BigDecimal getMakuatsusnmin() {
        return makuatsusnmin;
    }

    /**
     * Sn膜厚(MIN)
     *
     * @param makuatsusnmin the makuatsusnmin to set
     */
    public void setMakuatsusnmin(BigDecimal makuatsusnmin) {
        this.makuatsusnmin = makuatsusnmin;
    }

    /**
     * Sn膜厚(MAX)
     *
     * @return the makuatsusnmax
     */
    public BigDecimal getMakuatsusnmax() {
        return makuatsusnmax;
    }

    /**
     * Sn膜厚(MAX)
     *
     * @param makuatsusnmax the makuatsusnmax to set
     */
    public void setMakuatsusnmax(BigDecimal makuatsusnmax) {
        this.makuatsusnmax = makuatsusnmax;
    }

    /**
     * Sn膜厚(AVE)
     *
     * @return the makuatsusnave
     */
    public BigDecimal getMakuatsusnave() {
        return makuatsusnave;
    }

    /**
     * Sn膜厚(AVE)
     *
     * @param makuatsusnave the makuatsusnave to set
     */
    public void setMakuatsusnave(BigDecimal makuatsusnave) {
        this.makuatsusnave = makuatsusnave;
    }

    /**
     * Sn膜厚(STD)
     *
     * @return the makuatsusnstd
     */
    public BigDecimal getMakuatsusnstd() {
        return makuatsusnstd;
    }

    /**
     * Sn膜厚(STD)
     *
     * @param makuatsusnstd the makuatsusnstd to set
     */
    public void setMakuatsusnstd(BigDecimal makuatsusnstd) {
        this.makuatsusnstd = makuatsusnstd;
    }

    /**
     * 半田ﾇﾚ性
     *
     * @return the nurekensakekka
     */
    public String getNurekensakekka() {
        return nurekensakekka;
    }

    /**
     * 半田ﾇﾚ性
     *
     * @param nurekensakekka the nurekensakekka to set
     */
    public void setNurekensakekka(String nurekensakekka) {
        this.nurekensakekka = nurekensakekka;
    }

    /**
     * 半田耐熱性
     *
     * @return the tainetsukensakekka
     */
    public String getTainetsukensakekka() {
        return tainetsukensakekka;
    }

    /**
     * 半田耐熱性
     *
     * @param tainetsukensakekka the tainetsukensakekka to set
     */
    public void setTainetsukensakekka(String tainetsukensakekka) {
        this.tainetsukensakekka = tainetsukensakekka;
    }

    /**
     * 外観
     *
     * @return the gaikankensakekka
     */
    public String getGaikankensakekka() {
        return gaikankensakekka;
    }

    /**
     * 外観
     *
     * @param gaikankensakekka the gaikankensakekka to set
     */
    public void setGaikankensakekka(String gaikankensakekka) {
        this.gaikankensakekka = gaikankensakekka;
    }

    /**
     * 備考3
     *
     * @return the biko3
     */
    public String getBiko3() {
        return biko3;
    }

    /**
     * 備考3
     *
     * @param biko3 the biko3 to set
     */
    public void setBiko3(String biko3) {
        this.biko3 = biko3;
    }

    /**
     * 使用ﾄﾞｰﾑ明細
     *
     * @return the domemeisai
     */
    public String getDomemeisai() {
        return domemeisai;
    }

    /**
     * 使用ﾄﾞｰﾑ明細
     *
     * @param domemeisai the domemeisai to set
     */
    public void setDomemeisai(String domemeisai) {
        this.domemeisai = domemeisai;
    }

    /**
     * 1回目調整前PH値
     *
     * @return the tyoseimaeph1
     */
    public BigDecimal getTyoseimaeph1() {
        return tyoseimaeph1;
    }

    /**
     * 1回目調整前PH値
     *
     * @param tyoseimaeph1 the tyoseimaeph1 to set
     */
    public void setTyoseimaeph1(BigDecimal tyoseimaeph1) {
        this.tyoseimaeph1 = tyoseimaeph1;
    }

    /**
     * 1回目調整後PH値
     *
     * @return the tyoseigoph1
     */
    public BigDecimal getTyoseigoph1() {
        return tyoseigoph1;
    }

    /**
     * 1回目調整後PH値
     *
     * @param tyoseigoph1 the tyoseigoph1 to set
     */
    public void setTyoseigoph1(BigDecimal tyoseigoph1) {
        this.tyoseigoph1 = tyoseigoph1;
    }

    /**
     * 1回目調整時間
     *
     * @return the tyoseijikan1
     */
    public Integer getTyoseijikan1() {
        return tyoseijikan1;
    }

    /**
     * 1回目調整時間
     *
     * @param tyoseijikan1 the tyoseijikan1 to set
     */
    public void setTyoseijikan1(Integer tyoseijikan1) {
        this.tyoseijikan1 = tyoseijikan1;
    }

    /**
     * 2回目調整前PH値
     *
     * @return the tyoseimaeph2
     */
    public BigDecimal getTyoseimaeph2() {
        return tyoseimaeph2;
    }

    /**
     * 2回目調整前PH値
     *
     * @param tyoseimaeph2 the tyoseimaeph2 to set
     */
    public void setTyoseimaeph2(BigDecimal tyoseimaeph2) {
        this.tyoseimaeph2 = tyoseimaeph2;
    }

    /**
     * 2回目調整後PH値
     *
     * @return the tyoseigoph2
     */
    public BigDecimal getTyoseigoph2() {
        return tyoseigoph2;
    }

    /**
     * 2回目調整後PH値
     *
     * @param tyoseigoph2 the tyoseigoph2 to set
     */
    public void setTyoseigoph2(BigDecimal tyoseigoph2) {
        this.tyoseigoph2 = tyoseigoph2;
    }

    /**
     * 2回目調整時間
     *
     * @return the tyoseijikan2
     */
    public Integer getTyoseijikan2() {
        return tyoseijikan2;
    }

    /**
     * 2回目調整時間
     *
     * @param tyoseijikan2 the tyoseijikan2 to set
     */
    public void setTyoseijikan2(Integer tyoseijikan2) {
        this.tyoseijikan2 = tyoseijikan2;
    }

    /**
     * Ｔ寸法
     *
     * @return the tsunpou
     */
    public BigDecimal getTsunpou() {
        return tsunpou;
    }

    /**
     * Ｔ寸法
     *
     * @param tsunpou the tsunpou to set
     */
    public void setTsunpou(BigDecimal tsunpou) {
        this.tsunpou = tsunpou;
    }

    /**
     * ﾊﾞﾚﾙNo
     *
     * @return the barrelno
     */
    public Integer getBarrelno() {
        return barrelno;
    }

    /**
     * ﾊﾞﾚﾙNo
     *
     * @param barrelno the barrelno to set
     */
    public void setBarrelno(Integer barrelno) {
        this.barrelno = barrelno;
    }

    /**
     * Ni膜厚(CPL)
     *
     * @return the makuatsunicpl
     */
    public BigDecimal getMakuatsunicpl() {
        return makuatsunicpl;
    }

    /**
     * Ni膜厚(CPL)
     *
     * @param makuatsunicpl the makuatsunicpl to set
     */
    public void setMakuatsunicpl(BigDecimal makuatsunicpl) {
        this.makuatsunicpl = makuatsunicpl;
    }

    /**
     * Sn膜厚(CPL)
     *
     * @return the makuatsusncpl
     */
    public BigDecimal getMakuatsusncpl() {
        return makuatsusncpl;
    }

    /**
     * Sn膜厚(CPL)
     *
     * @param makuatsusncpl the makuatsusncpl to set
     */
    public void setMakuatsusncpl(BigDecimal makuatsusncpl) {
        this.makuatsusncpl = makuatsusncpl;
    }

    /**
     * 測定日時
     *
     * @return the sokuteinichiji
     */
    public Timestamp getSokuteinichiji() {
        return sokuteinichiji;
    }

    /**
     * 測定日時
     *
     * @param sokuteinichiji the sokuteinichiji to set
     */
    public void setSokuteinichiji(Timestamp sokuteinichiji) {
        this.sokuteinichiji = sokuteinichiji;
    }

    /**
     * Ni膜厚(CV)
     *
     * @return the makuatsunicv
     */
    public BigDecimal getMakuatsunicv() {
        return makuatsunicv;
    }

    /**
     * Ni膜厚(CV)
     *
     * @param makuatsunicv the makuatsunicv to set
     */
    public void setMakuatsunicv(BigDecimal makuatsunicv) {
        this.makuatsunicv = makuatsunicv;
    }

    /**
     * Sn膜厚(CV)
     *
     * @return the makuatsusncv
     */
    public BigDecimal getMakuatsusncv() {
        return makuatsusncv;
    }

    /**
     * Sn膜厚(CV)
     *
     * @param makuatsusncv the makuatsusncv to set
     */
    public void setMakuatsusncv(BigDecimal makuatsusncv) {
        this.makuatsusncv = makuatsusncv;
    }

    /**
     * 検査日時
     *
     * @return the kensanichiji
     */
    public Timestamp getKensanichiji() {
        return kensanichiji;
    }

    /**
     * 検査日時
     *
     * @param kensanichiji the kensanichiji to set
     */
    public void setKensanichiji(Timestamp kensanichiji) {
        this.kensanichiji = kensanichiji;
    }

    /**
     * 検査・外観担当者
     *
     * @return the kensatantousya
     */
    public String getKensatantousya() {
        return kensatantousya;
    }

    /**
     * 検査・外観担当者
     *
     * @param kensatantousya the kensatantousya to set
     */
    public void setKensatantousya(String kensatantousya) {
        this.kensatantousya = kensatantousya;
    }

    /**
     * 膜厚担当者
     *
     * @return the makuatsutantosya
     */
    public String getMakuatsutantosya() {
        return makuatsutantosya;
    }

    /**
     * 膜厚担当者
     *
     * @param makuatsutantosya the makuatsutantosya to set
     */
    public void setMakuatsutantosya(String makuatsutantosya) {
        this.makuatsutantosya = makuatsutantosya;
    }

    /**
     * Sn開始日時
     *
     * @return the kaishinichiji_sn
     */
    public Timestamp getKaishinichiji_sn() {
        return kaishinichiji_sn;
    }

    /**
     * Sn開始日時
     *
     * @param kaishinichiji_sn the kaishinichiji_sn to set
     */
    public void setKaishinichiji_sn(Timestamp kaishinichiji_sn) {
        this.kaishinichiji_sn = kaishinichiji_sn;
    }

    /**
     * 受入れ単位重量
     *
     * @return the ukeiretannijyuryo
     */
    public BigDecimal getUkeiretannijyuryo() {
        return ukeiretannijyuryo;
    }

    /**
     * 受入れ単位重量
     *
     * @param ukeiretannijyuryo the ukeiretannijyuryo to set
     */
    public void setUkeiretannijyuryo(BigDecimal ukeiretannijyuryo) {
        this.ukeiretannijyuryo = ukeiretannijyuryo;
    }

    /**
     * 受入れ総重量
     *
     * @return the ukeiresoujyuryou
     */
    public BigDecimal getUkeiresoujyuryou() {
        return ukeiresoujyuryou;
    }

    /**
     * 受入れ総重量
     *
     * @param ukeiresoujyuryou the ukeiresoujyuryou to set
     */
    public void setUkeiresoujyuryou(BigDecimal ukeiresoujyuryou) {
        this.ukeiresoujyuryou = ukeiresoujyuryou;
    }

    /**
     * ﾒｯｷ場所
     *
     * @return the mekkibasyo
     */
    public String getMekkibasyo() {
        return mekkibasyo;
    }

    /**
     * ﾒｯｷ場所
     *
     * @param mekkibasyo the mekkibasyo to set
     */
    public void setMekkibasyo(String mekkibasyo) {
        this.mekkibasyo = mekkibasyo;
    }

    /**
     * ﾒｯｷ場所設備
     *
     * @return the mekkibasyosetubi
     */
    public String getMekkibasyosetubi() {
        return mekkibasyosetubi;
    }

    /**
     * ﾒｯｷ場所設備
     *
     * @param mekkibasyosetubi the mekkibasyosetubi to set
     */
    public void setMekkibasyosetubi(String mekkibasyosetubi) {
        this.mekkibasyosetubi = mekkibasyosetubi;
    }

    /**
     * ﾒｯｷ終了日時
     *
     * @return the mekkisyuryounichiji
     */
    public Timestamp getMekkisyuryounichiji() {
        return mekkisyuryounichiji;
    }

    /**
     * ﾒｯｷ終了日時
     *
     * @param mekkisyuryounichiji the mekkisyuryounichiji to set
     */
    public void setMekkisyuryounichiji(Timestamp mekkisyuryounichiji) {
        this.mekkisyuryounichiji = mekkisyuryounichiji;
    }

    /**
     * 終了担当者
     *
     * @return the syuryousya
     */
    public String getSyuryousya() {
        return syuryousya;
    }

    /**
     * 終了担当者
     *
     * @param syuryousya the syuryousya to set
     */
    public void setSyuryousya(String syuryousya) {
        this.syuryousya = syuryousya;
    }

    /**
     * 検査単位重量
     *
     * @return the kensatannijyuryo
     */
    public BigDecimal getKensatannijyuryo() {
        return kensatannijyuryo;
    }

    /**
     * 検査単位重量
     *
     * @param kensatannijyuryo the kensatannijyuryo to set
     */
    public void setKensatannijyuryo(BigDecimal kensatannijyuryo) {
        this.kensatannijyuryo = kensatannijyuryo;
    }

    /**
     * 検査総重量
     *
     * @return the kensasoujyuryou
     */
    public BigDecimal getKensasoujyuryou() {
        return kensasoujyuryou;
    }

    /**
     * 検査総重量
     *
     * @param kensasoujyuryou the kensasoujyuryou to set
     */
    public void setKensasoujyuryou(BigDecimal kensasoujyuryou) {
        this.kensasoujyuryou = kensasoujyuryou;
    }

    /**
     * 熱処理条件
     *
     * @return the netusyorijyouken
     */
    public String getNetusyorijyouken() {
        return netusyorijyouken;
    }

    /**
     * 熱処理条件
     *
     * @param netusyorijyouken the netusyorijyouken to set
     */
    public void setNetusyorijyouken(String netusyorijyouken) {
        this.netusyorijyouken = netusyorijyouken;
    }

    /**
     * 熱処理開始日時
     *
     * @return the netusyorikaisinichiji
     */
    public Timestamp getNetusyorikaisinichiji() {
        return netusyorikaisinichiji;
    }

    /**
     * 熱処理開始日時
     *
     * @param netusyorikaisinichiji the netusyorikaisinichiji to set
     */
    public void setNetusyorikaisinichiji(Timestamp netusyorikaisinichiji) {
        this.netusyorikaisinichiji = netusyorikaisinichiji;
    }

    /**
     * 熱処理担当者
     *
     * @return the netusyoritantousya
     */
    public String getNetusyoritantousya() {
        return netusyoritantousya;
    }

    /**
     * 熱処理担当者
     *
     * @param netusyoritantousya the netusyoritantousya to set
     */
    public void setNetusyoritantousya(String netusyoritantousya) {
        this.netusyoritantousya = netusyoritantousya;
    }

    /**
     * 磁石選別開始日時
     *
     * @return the jisyakusenbetukaisinichiji
     */
    public Timestamp getJisyakusenbetukaisinichiji() {
        return jisyakusenbetukaisinichiji;
    }

    /**
     * 磁石選別開始日時
     *
     * @param jisyakusenbetukaisinichiji the jisyakusenbetukaisinichiji to set
     */
    public void setJisyakusenbetukaisinichiji(Timestamp jisyakusenbetukaisinichiji) {
        this.jisyakusenbetukaisinichiji = jisyakusenbetukaisinichiji;
    }

    /**
     * 磁石選別担当者
     *
     * @return the jisyakusenbetutantousya
     */
    public String getJisyakusenbetutantousya() {
        return jisyakusenbetutantousya;
    }

    /**
     * 磁石選別担当者
     *
     * @param jisyakusenbetutantousya the jisyakusenbetutantousya to set
     */
    public void setJisyakusenbetutantousya(String jisyakusenbetutantousya) {
        this.jisyakusenbetutantousya = jisyakusenbetutantousya;
    }

    /**
     * 異常発行
     *
     * @return the ijouhakkou
     */
    public String getIjouhakkou() {
        return ijouhakkou;
    }

    /**
     * 異常発行
     *
     * @param ijouhakkou the ijouhakkou to set
     */
    public void setIjouhakkou(String ijouhakkou) {
        this.ijouhakkou = ijouhakkou;
    }

    /**
     * 異常品ﾗﾝｸ
     *
     * @return the ijourank
     */
    public String getIjourank() {
        return ijourank;
    }

    /**
     * 異常品ﾗﾝｸ
     *
     * @param ijourank the ijourank to set
     */
    public void setIjourank(String ijourank) {
        this.ijourank = ijourank;
    }

    /**
     * 膜厚確認
     *
     * @return the makuatsukakunin
     */
    public String getMakuatsukakunin() {
        return makuatsukakunin;
    }

    /**
     * 膜厚確認
     *
     * @param makuatsukakunin the makuatsukakunin to set
     */
    public void setMakuatsukakunin(String makuatsukakunin) {
        this.makuatsukakunin = makuatsukakunin;
    }

    /**
     * ﾃｽﾄ品
     *
     * @return the testhin
     */
    public String getTesthin() {
        return testhin;
    }

    /**
     * ﾃｽﾄ品
     *
     * @param testhin the testhin to set
     */
    public void setTesthin(String testhin) {
        this.testhin = testhin;
    }

    /**
     * T寸法AVE
     *
     * @return the tsunpouave
     */
    public BigDecimal getTsunpouave() {
        return tsunpouave;
    }

    /**
     * T寸法AVE
     *
     * @param tsunpouave the tsunpouave to set
     */
    public void setTsunpouave(BigDecimal tsunpouave) {
        this.tsunpouave = tsunpouave;
    }

    /**
     * ﾒｯｷ種類
     *
     * @return the mekkisyurui
     */
    public String getMekkisyurui() {
        return mekkisyurui;
    }

    /**
     * ﾒｯｷ種類
     *
     * @param mekkisyurui the mekkisyurui to set
     */
    public void setMekkisyurui(String mekkisyurui) {
        this.mekkisyurui = mekkisyurui;
    }

    /**
     * Ni膜厚01
     *
     * @return the makuatsuni01
     */
    public BigDecimal getMakuatsuni01() {
        return makuatsuni01;
    }

    /**
     * Ni膜厚01
     *
     * @param makuatsuni01 the makuatsuni01 to set
     */
    public void setMakuatsuni01(BigDecimal makuatsuni01) {
        this.makuatsuni01 = makuatsuni01;
    }

    /**
     * Ni膜厚02
     *
     * @return the makuatsuni02
     */
    public BigDecimal getMakuatsuni02() {
        return makuatsuni02;
    }

    /**
     * Ni膜厚02
     *
     * @param makuatsuni02 the makuatsuni02 to set
     */
    public void setMakuatsuni02(BigDecimal makuatsuni02) {
        this.makuatsuni02 = makuatsuni02;
    }

    /**
     * Ni膜厚03
     *
     * @return the makuatsuni03
     */
    public BigDecimal getMakuatsuni03() {
        return makuatsuni03;
    }

    /**
     * Ni膜厚03
     *
     * @param makuatsuni03 the makuatsuni03 to set
     */
    public void setMakuatsuni03(BigDecimal makuatsuni03) {
        this.makuatsuni03 = makuatsuni03;
    }

    /**
     * Ni膜厚04
     *
     * @return the makuatsuni04
     */
    public BigDecimal getMakuatsuni04() {
        return makuatsuni04;
    }

    /**
     * Ni膜厚04
     *
     * @param makuatsuni04 the makuatsuni04 to set
     */
    public void setMakuatsuni04(BigDecimal makuatsuni04) {
        this.makuatsuni04 = makuatsuni04;
    }

    /**
     * Ni膜厚05
     *
     * @return the makuatsuni05
     */
    public BigDecimal getMakuatsuni05() {
        return makuatsuni05;
    }

    /**
     * Ni膜厚05
     *
     * @param makuatsuni05 the makuatsuni05 to set
     */
    public void setMakuatsuni05(BigDecimal makuatsuni05) {
        this.makuatsuni05 = makuatsuni05;
    }

    /**
     * Ni膜厚06
     *
     * @return the makuatsuni06
     */
    public BigDecimal getMakuatsuni06() {
        return makuatsuni06;
    }

    /**
     * Ni膜厚06
     *
     * @param makuatsuni06 the makuatsuni06 to set
     */
    public void setMakuatsuni06(BigDecimal makuatsuni06) {
        this.makuatsuni06 = makuatsuni06;
    }

    /**
     * Ni膜厚07
     *
     * @return the makuatsuni07
     */
    public BigDecimal getMakuatsuni07() {
        return makuatsuni07;
    }

    /**
     * Ni膜厚07
     *
     * @param makuatsuni07 the makuatsuni07 to set
     */
    public void setMakuatsuni07(BigDecimal makuatsuni07) {
        this.makuatsuni07 = makuatsuni07;
    }

    /**
     * Ni膜厚08
     *
     * @return the makuatsuni08
     */
    public BigDecimal getMakuatsuni08() {
        return makuatsuni08;
    }

    /**
     * Ni膜厚08
     *
     * @param makuatsuni08 the makuatsuni08 to set
     */
    public void setMakuatsuni08(BigDecimal makuatsuni08) {
        this.makuatsuni08 = makuatsuni08;
    }

    /**
     * Ni膜厚09
     *
     * @return the makuatsuni09
     */
    public BigDecimal getMakuatsuni09() {
        return makuatsuni09;
    }

    /**
     * Ni膜厚09
     *
     * @param makuatsuni09 the makuatsuni09 to set
     */
    public void setMakuatsuni09(BigDecimal makuatsuni09) {
        this.makuatsuni09 = makuatsuni09;
    }

    /**
     * Ni膜厚10
     *
     * @return the makuatsuni10
     */
    public BigDecimal getMakuatsuni10() {
        return makuatsuni10;
    }

    /**
     * Ni膜厚10
     *
     * @param makuatsuni10 the makuatsuni10 to set
     */
    public void setMakuatsuni10(BigDecimal makuatsuni10) {
        this.makuatsuni10 = makuatsuni10;
    }

    /**
     * Ni膜厚11
     *
     * @return the makuatsuni11
     */
    public BigDecimal getMakuatsuni11() {
        return makuatsuni11;
    }

    /**
     * Ni膜厚11
     *
     * @param makuatsuni11 the makuatsuni11 to set
     */
    public void setMakuatsuni11(BigDecimal makuatsuni11) {
        this.makuatsuni11 = makuatsuni11;
    }

    /**
     * Ni膜厚12
     *
     * @return the makuatsuni12
     */
    public BigDecimal getMakuatsuni12() {
        return makuatsuni12;
    }

    /**
     * Ni膜厚12
     *
     * @param makuatsuni12 the makuatsuni12 to set
     */
    public void setMakuatsuni12(BigDecimal makuatsuni12) {
        this.makuatsuni12 = makuatsuni12;
    }

    /**
     * Ni膜厚13
     *
     * @return the makuatsuni13
     */
    public BigDecimal getMakuatsuni13() {
        return makuatsuni13;
    }

    /**
     * Ni膜厚13
     *
     * @param makuatsuni13 the makuatsuni13 to set
     */
    public void setMakuatsuni13(BigDecimal makuatsuni13) {
        this.makuatsuni13 = makuatsuni13;
    }

    /**
     * Ni膜厚14
     *
     * @return the makuatsuni14
     */
    public BigDecimal getMakuatsuni14() {
        return makuatsuni14;
    }

    /**
     * Ni膜厚14
     *
     * @param makuatsuni14 the makuatsuni14 to set
     */
    public void setMakuatsuni14(BigDecimal makuatsuni14) {
        this.makuatsuni14 = makuatsuni14;
    }

    /**
     * Ni膜厚15
     *
     * @return the makuatsuni15
     */
    public BigDecimal getMakuatsuni15() {
        return makuatsuni15;
    }

    /**
     * Ni膜厚15
     *
     * @param makuatsuni15 the makuatsuni15 to set
     */
    public void setMakuatsuni15(BigDecimal makuatsuni15) {
        this.makuatsuni15 = makuatsuni15;
    }

    /**
     * Ni膜厚16
     *
     * @return the makuatsuni16
     */
    public BigDecimal getMakuatsuni16() {
        return makuatsuni16;
    }

    /**
     * Ni膜厚16
     *
     * @param makuatsuni16 the makuatsuni16 to set
     */
    public void setMakuatsuni16(BigDecimal makuatsuni16) {
        this.makuatsuni16 = makuatsuni16;
    }

    /**
     * Ni膜厚17
     *
     * @return the makuatsuni17
     */
    public BigDecimal getMakuatsuni17() {
        return makuatsuni17;
    }

    /**
     * Ni膜厚17
     *
     * @param makuatsuni17 the makuatsuni17 to set
     */
    public void setMakuatsuni17(BigDecimal makuatsuni17) {
        this.makuatsuni17 = makuatsuni17;
    }

    /**
     * Ni膜厚18
     *
     * @return the makuatsuni18
     */
    public BigDecimal getMakuatsuni18() {
        return makuatsuni18;
    }

    /**
     * Ni膜厚18
     *
     * @param makuatsuni18 the makuatsuni18 to set
     */
    public void setMakuatsuni18(BigDecimal makuatsuni18) {
        this.makuatsuni18 = makuatsuni18;
    }

    /**
     * Ni膜厚19
     *
     * @return the makuatsuni19
     */
    public BigDecimal getMakuatsuni19() {
        return makuatsuni19;
    }

    /**
     * Ni膜厚19
     *
     * @param makuatsuni19 the makuatsuni19 to set
     */
    public void setMakuatsuni19(BigDecimal makuatsuni19) {
        this.makuatsuni19 = makuatsuni19;
    }

    /**
     * Ni膜厚20
     *
     * @return the makuatsuni20
     */
    public BigDecimal getMakuatsuni20() {
        return makuatsuni20;
    }

    /**
     * Ni膜厚20
     *
     * @param makuatsuni20 the makuatsuni20 to set
     */
    public void setMakuatsuni20(BigDecimal makuatsuni20) {
        this.makuatsuni20 = makuatsuni20;
    }

    /**
     * Sn膜厚01
     *
     * @return the makuatsusn01
     */
    public BigDecimal getMakuatsusn01() {
        return makuatsusn01;
    }

    /**
     * Sn膜厚01
     *
     * @param makuatsusn01 the makuatsusn01 to set
     */
    public void setMakuatsusn01(BigDecimal makuatsusn01) {
        this.makuatsusn01 = makuatsusn01;
    }

    /**
     * Sn膜厚02
     *
     * @return the makuatsusn02
     */
    public BigDecimal getMakuatsusn02() {
        return makuatsusn02;
    }

    /**
     * Sn膜厚02
     *
     * @param makuatsusn02 the makuatsusn02 to set
     */
    public void setMakuatsusn02(BigDecimal makuatsusn02) {
        this.makuatsusn02 = makuatsusn02;
    }

    /**
     * Sn膜厚03
     *
     * @return the makuatsusn03
     */
    public BigDecimal getMakuatsusn03() {
        return makuatsusn03;
    }

    /**
     * Sn膜厚03
     *
     * @param makuatsusn03 the makuatsusn03 to set
     */
    public void setMakuatsusn03(BigDecimal makuatsusn03) {
        this.makuatsusn03 = makuatsusn03;
    }

    /**
     * Sn膜厚04
     *
     * @return the makuatsusn04
     */
    public BigDecimal getMakuatsusn04() {
        return makuatsusn04;
    }

    /**
     * Sn膜厚04
     *
     * @param makuatsusn04 the makuatsusn04 to set
     */
    public void setMakuatsusn04(BigDecimal makuatsusn04) {
        this.makuatsusn04 = makuatsusn04;
    }

    /**
     * Sn膜厚05
     *
     * @return the makuatsusn05
     */
    public BigDecimal getMakuatsusn05() {
        return makuatsusn05;
    }

    /**
     * Sn膜厚05
     *
     * @param makuatsusn05 the makuatsusn05 to set
     */
    public void setMakuatsusn05(BigDecimal makuatsusn05) {
        this.makuatsusn05 = makuatsusn05;
    }

    /**
     * Sn膜厚06
     *
     * @return the makuatsusn06
     */
    public BigDecimal getMakuatsusn06() {
        return makuatsusn06;
    }

    /**
     * Sn膜厚06
     *
     * @param makuatsusn06 the makuatsusn06 to set
     */
    public void setMakuatsusn06(BigDecimal makuatsusn06) {
        this.makuatsusn06 = makuatsusn06;
    }

    /**
     * Sn膜厚07
     *
     * @return the makuatsusn07
     */
    public BigDecimal getMakuatsusn07() {
        return makuatsusn07;
    }

    /**
     * Sn膜厚07
     *
     * @param makuatsusn07 the makuatsusn07 to set
     */
    public void setMakuatsusn07(BigDecimal makuatsusn07) {
        this.makuatsusn07 = makuatsusn07;
    }

    /**
     * Sn膜厚08
     *
     * @return the makuatsusn08
     */
    public BigDecimal getMakuatsusn08() {
        return makuatsusn08;
    }

    /**
     * Sn膜厚08
     *
     * @param makuatsusn08 the makuatsusn08 to set
     */
    public void setMakuatsusn08(BigDecimal makuatsusn08) {
        this.makuatsusn08 = makuatsusn08;
    }

    /**
     * Sn膜厚09
     *
     * @return the makuatsusn09
     */
    public BigDecimal getMakuatsusn09() {
        return makuatsusn09;
    }

    /**
     * Sn膜厚09
     *
     * @param makuatsusn09 the makuatsusn09 to set
     */
    public void setMakuatsusn09(BigDecimal makuatsusn09) {
        this.makuatsusn09 = makuatsusn09;
    }

    /**
     * Sn膜厚10
     *
     * @return the makuatsusn10
     */
    public BigDecimal getMakuatsusn10() {
        return makuatsusn10;
    }

    /**
     * Sn膜厚10
     *
     * @param makuatsusn10 the makuatsusn10 to set
     */
    public void setMakuatsusn10(BigDecimal makuatsusn10) {
        this.makuatsusn10 = makuatsusn10;
    }

    /**
     * Sn膜厚11
     *
     * @return the makuatsusn11
     */
    public BigDecimal getMakuatsusn11() {
        return makuatsusn11;
    }

    /**
     * Sn膜厚11
     *
     * @param makuatsusn11 the makuatsusn11 to set
     */
    public void setMakuatsusn11(BigDecimal makuatsusn11) {
        this.makuatsusn11 = makuatsusn11;
    }

    /**
     * Sn膜厚12
     *
     * @return the makuatsusn12
     */
    public BigDecimal getMakuatsusn12() {
        return makuatsusn12;
    }

    /**
     * Sn膜厚12
     *
     * @param makuatsusn12 the makuatsusn12 to set
     */
    public void setMakuatsusn12(BigDecimal makuatsusn12) {
        this.makuatsusn12 = makuatsusn12;
    }

    /**
     * Sn膜厚13
     *
     * @return the makuatsusn13
     */
    public BigDecimal getMakuatsusn13() {
        return makuatsusn13;
    }

    /**
     * Sn膜厚13
     *
     * @param makuatsusn13 the makuatsusn13 to set
     */
    public void setMakuatsusn13(BigDecimal makuatsusn13) {
        this.makuatsusn13 = makuatsusn13;
    }

    /**
     * Sn膜厚14
     *
     * @return the makuatsusn14
     */
    public BigDecimal getMakuatsusn14() {
        return makuatsusn14;
    }

    /**
     * Sn膜厚14
     *
     * @param makuatsusn14 the makuatsusn14 to set
     */
    public void setMakuatsusn14(BigDecimal makuatsusn14) {
        this.makuatsusn14 = makuatsusn14;
    }

    /**
     * Sn膜厚15
     *
     * @return the makuatsusn15
     */
    public BigDecimal getMakuatsusn15() {
        return makuatsusn15;
    }

    /**
     * Sn膜厚15
     *
     * @param makuatsusn15 the makuatsusn15 to set
     */
    public void setMakuatsusn15(BigDecimal makuatsusn15) {
        this.makuatsusn15 = makuatsusn15;
    }

    /**
     * Sn膜厚16
     *
     * @return the makuatsusn16
     */
    public BigDecimal getMakuatsusn16() {
        return makuatsusn16;
    }

    /**
     * Sn膜厚16
     *
     * @param makuatsusn16 the makuatsusn16 to set
     */
    public void setMakuatsusn16(BigDecimal makuatsusn16) {
        this.makuatsusn16 = makuatsusn16;
    }

    /**
     * Sn膜厚17
     *
     * @return the makuatsusn17
     */
    public BigDecimal getMakuatsusn17() {
        return makuatsusn17;
    }

    /**
     * Sn膜厚17
     *
     * @param makuatsusn17 the makuatsusn17 to set
     */
    public void setMakuatsusn17(BigDecimal makuatsusn17) {
        this.makuatsusn17 = makuatsusn17;
    }

    /**
     * Sn膜厚18
     *
     * @return the makuatsusn18
     */
    public BigDecimal getMakuatsusn18() {
        return makuatsusn18;
    }

    /**
     * Sn膜厚18
     *
     * @param makuatsusn18 the makuatsusn18 to set
     */
    public void setMakuatsusn18(BigDecimal makuatsusn18) {
        this.makuatsusn18 = makuatsusn18;
    }

    /**
     * Sn膜厚19
     *
     * @return the makuatsusn19
     */
    public BigDecimal getMakuatsusn19() {
        return makuatsusn19;
    }

    /**
     * Sn膜厚19
     *
     * @param makuatsusn19 the makuatsusn19 to set
     */
    public void setMakuatsusn19(BigDecimal makuatsusn19) {
        this.makuatsusn19 = makuatsusn19;
    }

    /**
     * Sn膜厚20
     *
     * @return the makuatsusn20
     */
    public BigDecimal getMakuatsusn20() {
        return makuatsusn20;
    }

    /**
     * Sn膜厚20
     *
     * @param makuatsusn20 the makuatsusn20 to set
     */
    public void setMakuatsusn20(BigDecimal makuatsusn20) {
        this.makuatsusn20 = makuatsusn20;
    }

    /**
     * 熱処理担当者
     * @return the soujyuryoutantousya
     */
    public String getSoujyuryoutantousya() {
        return soujyuryoutantousya;
    }

    /**
     * 熱処理担当者
     * @param soujyuryoutantousya the soujyuryoutantousya to set
     */
    public void setSoujyuryoutantousya(String soujyuryoutantousya) {
        this.soujyuryoutantousya = soujyuryoutantousya;
    }

    /**
     * 備考4
     * @return the biko4
     */
    public String getBiko4() {
        return biko4;
    }

    /**
     * 備考4
     * @param biko4 the biko4 to set
     */
    public void setBiko4(String biko4) {
        this.biko4 = biko4;
    }

    /**
     * 備考5
     * @return the biko5
     */
    public String getBiko5() {
        return biko5;
    }

    /**
     * 備考5
     * @param biko5 the biko5 to set
     */
    public void setBiko5(String biko5) {
        this.biko5 = biko5;
    }

}
