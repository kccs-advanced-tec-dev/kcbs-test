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
 * 変更日	2019/11/05<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	863 F.Zhang<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 外部電極(ﾒｯｷ品質検査)のモデルクラスです。
 *
 * @author 863 F.Zhang
 * @since 2019/11/05
 */
public class SrMekki {

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
     * 処理数
     */
    private Integer ukeiresuu;

    /**
     * KCPNO
     */
    private String kcpno;
    
    /**
     * ﾄﾞｰﾑ個数
     */
    private Integer domekosuu;
    
    /**
     * 号機
     */
    private String gouki;
    
     /**
     * 開始担当者
     */
    private String tantosyacode;
    
    /**
     * ﾒｯｷ開始日時
     */
    private Timestamp mekkikaishinichiji;

    /**
     * 条件NI(A)
     */
    private BigDecimal mekkijyoukennia;
    
    /**
     * 条件NI(AM)
     */
    private Integer mekkijyoukenniam;
    
    /**
     * 条件SN(A)
     */
    private BigDecimal mekkijyoukensna;
    
    /**
     * 条件SN(AM)
     */
    private Integer mekkijyoukensnam;
    
    /**
     * 良品数
     */
    private Integer shukkakosuu;
    
    /**
     * 歩留まり
     */
    private BigDecimal budomari;
    
    /**
     * Ni膜厚(MIN)
     */
    private BigDecimal makuatsunimin;
     
    /**
     * Ni膜厚(MAX)
     */
    private BigDecimal makuatsunimax;
    
    /**
     * Ni膜厚(AVE)
     */
    private BigDecimal makuatsuniave;
    
    /**
     * Ni膜厚(STD)
     */
    private BigDecimal makuatsunistd;
    
    /**
     * Sn膜厚(MIN)
     */
    private BigDecimal makuatsusnmin;
    
    /**
     * Sn膜厚(MAX)
     */
    private BigDecimal makuatsusnmax;
    
    /**
     * Sn膜厚(AVE)
     */
    private BigDecimal makuatsusnave;
    
    /**
     * Sn膜厚(STD)
     */
    private BigDecimal makuatsusnstd;
    
    /**
     * 半田ﾇﾚ性
     */
    private String nurekensakekka;
    
    /**
     * 半田耐熱性
     */
    private String tainetsukensakekka;
    
    /**
     * 外観
     */
    private String gaikankensakekka;
    
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
     * 回数
     */
    private Integer jissekino;
    
    /**
     * 登録日時
     */
    private Timestamp torokunichiji;

    /**
     * 更新日時
     */
    private Timestamp kosinnichiji;
    
    /**
     * 使用ﾄﾞｰﾑ明細
     */
    private String domemeisai;
    
    /**
     * 1回目調整前PH値
     */
    private BigDecimal tyoseimaeph1;
    
    /**
     * 1回目調整後PH値
     */
    private BigDecimal tyoseigoph1;
     
    /**
     * 1回目調整時間
     */
    private Integer tyoseijikan1;
    
    /**
     * 2回目調整前PH値
     */
    private BigDecimal tyoseimaeph2;
    
    /**
     * 2回目調整後PH値
     */
    private BigDecimal tyoseigoph2;
    
    /**
     * 2回目調整時間
     */
    private Integer tyoseijikan2;
    
    /**
     * Ｔ寸法
     */
    private BigDecimal tsunpou;
    
    /**
     * ﾊﾞﾚﾙNo
     */
    private Integer barrelno;
    
    /**
     * Ni膜厚(CPL)
     */
    private BigDecimal makuatsunicpl;
    
    /**
     * Sn膜厚(CPL)
     */
    private BigDecimal makuatsusncpl;
    
    /**
     * 測定日時
     */
    private Timestamp sokuteinichiji;
    
    /**
     * Ni膜厚(CV)
     */
    private BigDecimal makuatsunicv;
    
    /**
     * Sn膜厚(CV)
     */
    private BigDecimal makuatsusncv;
    
    /**
     * 検査日時
     */
    private Timestamp kensanichiji;
    
    /**
     * 検査・外観担当者
     */
    private String kensatantousya;
    
    /**
     * 膜厚担当者
     */
    private String makuatsutantosya;
    
    /**
     * Sn開始日時
     */
    private Timestamp kaishinichiji_sn;
    
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
     * 受入れ単位重量
     */
    private BigDecimal ukeiretannijyuryo;    
    
    /**
     * 受入れ総重量
     */
    private BigDecimal ukeiresoujyuryou;
    
    /**
     * ﾒｯｷ場所
     */
    private String mekkibasyo;
    
    /**
     * ﾒｯｷ場所設備
     */
    private String mekkibasyosetubi;
    
    /**
     * ﾒｯｷ終了日時
     */
    private Timestamp mekkisyuryounichiji;
    
    /**
     * 終了担当者
     */
    private String syuryousya;
    
    /**
     * 検査単位重量
     */
    private BigDecimal kensatannijyuryo;
    
    /**
     * 検査総重量
     */
    private BigDecimal kensasoujyuryou;
    
    /**
     * 熱処理条件
     */
    private String netusyorijyouken;
    
    /**
     * 熱処理開始日時
     */
    private Timestamp netusyorikaisinichiji;
    
    /**
     * 熱処理担当者
     */
    private String netusyoritantousya;
    
    /**
     * 磁石選別開始日時
     */
    private Timestamp jisyakusenbetukaisinichiji;
     
    /**
     * 磁石選別担当者
     */
    private String jisyakusenbetutantousya;
    
    /**
     * 異常発行
     */
    private String ijouhakkou;
     
    /**
     * 異常品ﾗﾝｸ
     */
    private String ijourank;
    
    /**
     * 膜厚確認
     */
    private String makuatsukakunin;
    
    /**
     * ﾃｽﾄ品
     */
    private String testhin;
    
    /**
     * T寸法AVE
     */
    private BigDecimal tsunpouave;
    
    /**
     * ﾒｯｷ種類
     */
    private String mekkisyurui;

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
     * 担当者ｺｰﾄﾞ
     *
     * @return the tantosyacode
     */
    public String getTantosyacode() {
        return tantosyacode;
    }

    /**
     * 担当者ｺｰﾄﾞ
     *
     * @param tantosyacode the tantosyacode to set
     */
    public void setTantosyacode(String tantosyacode) {
        this.tantosyacode = tantosyacode;
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

}
