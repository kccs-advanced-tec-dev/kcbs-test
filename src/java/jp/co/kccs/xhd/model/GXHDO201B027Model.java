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
 * 変更日	2019/11/05<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * <br>
 * 変更日	2020/09/11<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	863 sujialiang<br>
 * 変更理由	項目追加・変更<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 外部電極・外部電極焼成(ｻﾔ詰め)履歴検索画面のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2019/11/05
 */
public class GXHDO201B027Model implements Serializable{

    /** ﾛｯﾄNo. */
    private String lotno = "";

    /** 作業回数 */
    private Integer kaisuu = null;

    /** KCPNO */
    private String kcpno = "";

    /** 客先 */
    private String kyakusaki = "";

    /** ﾛｯﾄ区分 */
    private String lotkubuncode = "";

    /** ｵｰﾅｰ */
    private String ownercode = "";

    /** ﾛｯﾄﾌﾟﾚ */
    private String lotpre = "";

    /** 処理数 */
    private Integer suuryou = null;

    /** ｻﾔ詰め方法 */
    private String sayadumehouhou = "";

    /** 粉まぶし */
    private String konamabushi = "";

    /** 塗布重量 */
    private BigDecimal dipjuryou = null;

    /** 製品重量 */
    private BigDecimal soujyuuryou = null;

    /** BN粉末量 */
    private BigDecimal bnfunmaturyou = null;

    /** BN粉末量確認 */
    private String bnfunmaturyoukakunin = "";

    /** ｻﾔ/SUS板種類 */
    private String sayasussyurui = "";

    /** ｻﾔ/SUS板枚数 計算値 */
    private Integer sayamaisuukeisan = null;

    /** ｻﾔ重量範囲(g)MIN */
    private BigDecimal sjyuuryourangemin = null;

    /** ｻﾔ重量範囲(g)MAX */
    private BigDecimal sjyuuryourangemax = null;

    /** ｻﾔ重量(g/枚) */
    private BigDecimal sayajyuuryou = null;

    /** ｻﾔ/SUS板枚数 */
    private Integer sayamaisuu = null;

    /** ｻﾔ/SUS板ﾁｬｰｼﾞ量 */
    private Integer sayachargeryou = null;

    /** ｻﾔ/SUS板詰め開始日時 */
    private Timestamp sayadumebi = null;

    /** ｻﾔ/SUS板詰め開始担当者 */
    private String sayadumetantousya = "";

    /** ｻﾔ/SUS板詰め開始確認者 */
    private String sayadumekakuninsya = "";

    /** ｻﾔ/SUS板詰め終了日時 */
    private Timestamp sayadumeendnichiji = null;

    /** ｻﾔ/SUS板詰め終了担当者 */
    private String sayadumesyuryosya = "";

    /** 脱ﾊﾞｲ号機 */
    private String datsubaigouki = "";

    /** 脱ﾊﾞｲ温度 */
    private String datsubaiondo = "";

    /** 脱ﾊﾞｲ時間 */
    private Integer datsubaijikan = null;

    /** 脱ﾊﾞｲPTNNO */
    private String datsubaiptnno = "";

    /** 脱ﾊﾞｲｻﾔ枚数 */
    private Integer datsubaisayamaisuu = null;

    /** 脱ﾊﾞｲ開始日時 */
    private Timestamp datsubaistartdatetime = null;

    /** 脱ﾊﾞｲ開始担当者 */
    private String datsubaistarttantosyacode = "";

    /** 脱ﾊﾞｲ開始確認者 */
    private String datsubaistartkakuninsyacode = "";

    /** 脱ﾊﾞｲ終了日時 */
    private Timestamp datsubaienddatetime = null;

    /** 脱ﾊﾞｲ終了担当者 */
    private String datsubaiendtantosyacode = "";

    /** 焼成号機 */
    private String gouro1 = "";

    /** 焼成温度 */
    private Integer peakondo = null;

    /** 焼成送りｽﾋﾟｰﾄﾞ */
    private String okurispeed = "";

    /** 焼成開始日時 */
    private Timestamp nyuuronichiji1 = null;

    /** 焼成開始担当者 */
    private String tantousya1 = "";

    /** 焼成開始確認者 */
    private String syoseistartkakuninsyacode = "";

    /** 焼成終了日時 */
    private Timestamp syutsuronichiji1 = null;

    /** 焼成終了担当者 */
    private String syoseiendtantosyacode = "";

    /** 外観 */
    private String gaikan = "";

    /** 良品重量 */
    private BigDecimal abeggryohinjyuryo = null;

    /** 不良重量 */
    private BigDecimal abeggfuryojyuryo = null;

    /** 不良率 */
    private BigDecimal abeggfuryoritu = null;

    /** 外観確認日時 */
    private Timestamp gaikankakuninnichiji = null;

    /** 外観確認担当者 */
    private String gaikantantosya = "";

    /** 備考1 */
    private String bikou1 = "";

    /** 備考2 */
    private String bikou2 = "";

    /**
     * @return lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * @param lotno lotno
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * @return kaisuu
     */
    public Integer getKaisuu() {
        return kaisuu;
    }

    /**
     * @param kaisuu kaisuu
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
     * @param kcpno kcpno
     */
    public void setKcpno(String kcpno) {
        this.kcpno = kcpno;
    }

    /**
     * @return kyakusaki
     */
    public String getKyakusaki() {
        return kyakusaki;
    }

    /**
     * @param kyakusaki kyakusaki
     */
    public void setKyakusaki(String kyakusaki) {
        this.kyakusaki = kyakusaki;
    }

    /**
     * @return lotkubuncode
     */
    public String getLotkubuncode() {
        return lotkubuncode;
    }

    /**
     * @param lotkubuncode lotkubuncode
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
     * @param ownercode ownercode
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
     * @param lotpre lotpre
     */
    public void setLotpre(String lotpre) {
        this.lotpre = lotpre;
    }

    /**
     * @return suuryou
     */
    public Integer getSuuryou() {
        return suuryou;
    }

    /**
     * @param suuryou suuryou
     */
    public void setSuuryou(Integer suuryou) {
        this.suuryou = suuryou;
    }

    /**
     * @return sayadumehouhou
     */
    public String getSayadumehouhou() {
        return sayadumehouhou;
    }

    /**
     * @param sayadumehouhou sayadumehouhou
     */
    public void setSayadumehouhou(String sayadumehouhou) {
        this.sayadumehouhou = sayadumehouhou;
    }

    /**
     * @return konamabushi
     */
    public String getKonamabushi() {
        return konamabushi;
    }

    /**
     * @param konamabushi konamabushi
     */
    public void setKonamabushi(String konamabushi) {
        this.konamabushi = konamabushi;
    }

    /**
     * @return dipjuryou
     */
    public BigDecimal getDipjuryou() {
        return dipjuryou;
    }

    /**
     * @param dipjuryou 
     */
    public void setDipjuryou(BigDecimal dipjuryou) {
        this.dipjuryou = dipjuryou;
    }

    /**
     * @return soujyuuryou
     */
    public BigDecimal getSoujyuuryou() {
        return soujyuuryou;
    }

    /**
     * @param soujyuuryou soujyuuryou
     */
    public void setSoujyuuryou(BigDecimal soujyuuryou) {
        this.soujyuuryou = soujyuuryou;
    }

    /**
     * @return bnfunmaturyou
     */
    public BigDecimal getBnfunmaturyou() {
        return bnfunmaturyou;
    }

    /**
     * @param bnfunmaturyou bnfunmaturyou
     */
    public void setBnfunmaturyou(BigDecimal bnfunmaturyou) {
        this.bnfunmaturyou = bnfunmaturyou;
    }

    /**
     * @return bnfunmaturyoukakunin
     */
    public String getBnfunmaturyoukakunin() {
        return bnfunmaturyoukakunin;
    }

    /**
     * @param bnfunmaturyoukakunin bnfunmaturyoukakunin
     */
    public void setBnfunmaturyoukakunin(String bnfunmaturyoukakunin) {
        this.bnfunmaturyoukakunin = bnfunmaturyoukakunin;
    }

    /**
     * @return sayasussyurui
     */
    public String getSayasussyurui() {
        return sayasussyurui;
    }

    /**
     * @param sayasussyurui sayasussyurui
     */
    public void setSayasussyurui(String sayasussyurui) {
        this.sayasussyurui = sayasussyurui;
    }

    /**
     * @return sayamaisuukeisan
     */
    public Integer getSayamaisuukeisan() {
        return sayamaisuukeisan;
    }

    /**
     * @param sayamaisuukeisan sayamaisuukeisan
     */
    public void setSayamaisuukeisan(Integer sayamaisuukeisan) {
        this.sayamaisuukeisan = sayamaisuukeisan;
    }

    /**
     * @return sjyuuryourangemin
     */
    public BigDecimal getSjyuuryourangemin() {
        return sjyuuryourangemin;
    }

    /**
     * @param sjyuuryourangemin sjyuuryourangemin
     */
    public void setSjyuuryourangemin(BigDecimal sjyuuryourangemin) {
        this.sjyuuryourangemin = sjyuuryourangemin;
    }

    /**
     * @return sjyuuryourangemax
     */
    public BigDecimal getSjyuuryourangemax() {
        return sjyuuryourangemax;
    }

    /**
     * @param sjyuuryourangemax sjyuuryourangemax
     */
    public void setSjyuuryourangemax(BigDecimal sjyuuryourangemax) {
        this.sjyuuryourangemax = sjyuuryourangemax;
    }

    /**
     * @return sayajyuuryou
     */
    public BigDecimal getSayajyuuryou() {
        return sayajyuuryou;
    }

    /**
     * @param sayajyuuryou sayajyuuryou
     */
    public void setSayajyuuryou(BigDecimal sayajyuuryou) {
        this.sayajyuuryou = sayajyuuryou;
    }

    /**
     * @return sayamaisuu
     */
    public Integer getSayamaisuu() {
        return sayamaisuu;
    }

    /**
     * @param sayamaisuu sayamaisuu
     */
    public void setSayamaisuu(Integer sayamaisuu) {
        this.sayamaisuu = sayamaisuu;
    }

    /**
     * @return sayachargeryou
     */
    public Integer getSayachargeryou() {
        return sayachargeryou;
    }

    /**
     * @param sayachargeryou sayachargeryou
     */
    public void setSayachargeryou(Integer sayachargeryou) {
        this.sayachargeryou = sayachargeryou;
    }

    /**
     * @return sayadumebi
     */
    public Timestamp getSayadumebi() {
        return sayadumebi;
    }

    /**
     * @param sayadumebi sayadumebi
     */
    public void setSayadumebi(Timestamp sayadumebi) {
        this.sayadumebi = sayadumebi;
    }

    /**
     * @return sayadumetantousya
     */
    public String getSayadumetantousya() {
        return sayadumetantousya;
    }

    /**
     * @param sayadumetantousya sayadumetantousya
     */
    public void setSayadumetantousya(String sayadumetantousya) {
        this.sayadumetantousya = sayadumetantousya;
    }

    /**
     * @return sayadumekakuninsya
     */
    public String getSayadumekakuninsya() {
        return sayadumekakuninsya;
    }

    /**
     * @param sayadumekakuninsya sayadumekakuninsya
     */
    public void setSayadumekakuninsya(String sayadumekakuninsya) {
        this.sayadumekakuninsya = sayadumekakuninsya;
    }

    /**
     * @return sayadumeendnichiji
     */
    public Timestamp getSayadumeendnichiji() {
        return sayadumeendnichiji;
    }

    /**
     * @param sayadumeendnichiji sayadumeendnichiji
     */
    public void setSayadumeendnichiji(Timestamp sayadumeendnichiji) {
        this.sayadumeendnichiji = sayadumeendnichiji;
    }

    /**
     * @return sayadumesyuryosya
     */
    public String getSayadumesyuryosya() {
        return sayadumesyuryosya;
    }

    /**
     * @param sayadumesyuryosya sayadumesyuryosya
     */
    public void setSayadumesyuryosya(String sayadumesyuryosya) {
        this.sayadumesyuryosya = sayadumesyuryosya;
    }

    /**
     * @return datsubaigouki
     */
    public String getDatsubaigouki() {
        return datsubaigouki;
    }

    /**
     * @param datsubaigouki 
     */
    public void setDatsubaigouki(String datsubaigouki) {
        this.datsubaigouki = datsubaigouki;
    }

    /**
     * @return datsubaiondo
     */
    public String getDatsubaiondo() {
        return datsubaiondo;
    }

    /**
     * @param datsubaiondo
     */
    public void setDatsubaiondo(String datsubaiondo) {
        this.datsubaiondo = datsubaiondo;
    }

    /**
     * @return datsubaijikan
     */
    public Integer getDatsubaijikan() {
        return datsubaijikan;
    }

    /**
     * @param datsubaijikan
     */
    public void setDatsubaijikan(Integer datsubaijikan) {
        this.datsubaijikan = datsubaijikan;
    }

    /**
     * @return datsubaiptnno
     */
    public String getDatsubaiptnno() {
        return datsubaiptnno;
    }

    /**
     * @param datsubaiptnno
     */
    public void setDatsubaiptnno(String datsubaiptnno) {
        this.datsubaiptnno = datsubaiptnno;
    }

    /**
     * @return datsubaisayamaisuu
     */
    public Integer getDatsubaisayamaisuu() {
        return datsubaisayamaisuu;
    }

    /**
     * @param datsubaisayamaisuu
     */
    public void setDatsubaisayamaisuu(Integer datsubaisayamaisuu) {
        this.datsubaisayamaisuu = datsubaisayamaisuu;
    }

    /**
     * @return datsubaistartdatetime
     */
    public Timestamp getDatsubaistartdatetime() {
        return datsubaistartdatetime;
    }

    /**
     * @param datsubaistartdatetime
     */
    public void setDatsubaistartdatetime(Timestamp datsubaistartdatetime) {
        this.datsubaistartdatetime = datsubaistartdatetime;
    }

    /**
     * @return datsubaistarttantosyacode
     */
    public String getDatsubaistarttantosyacode() {
        return datsubaistarttantosyacode;
    }

    /**
     * @param datsubaistarttantosyacode
     */
    public void setDatsubaistarttantosyacode(String datsubaistarttantosyacode) {
        this.datsubaistarttantosyacode = datsubaistarttantosyacode;
    }

    /**
     * @return datsubaistartkakuninsyacode
     */
    public String getDatsubaistartkakuninsyacode() {
        return datsubaistartkakuninsyacode;
    }

    /**
     * @param datsubaistartkakuninsyacode
     */
    public void setDatsubaistartkakuninsyacode(String datsubaistartkakuninsyacode) {
        this.datsubaistartkakuninsyacode = datsubaistartkakuninsyacode;
    }

    /**
     * @return datsubaienddatetime
     */
    public Timestamp getDatsubaienddatetime() {
        return datsubaienddatetime;
    }

    /**
     * @param datsubaienddatetime
     */
    public void setDatsubaienddatetime(Timestamp datsubaienddatetime) {
        this.datsubaienddatetime = datsubaienddatetime;
    }

    /**
     * @return datsubaiendtantosyacode
     */
    public String getDatsubaiendtantosyacode() {
        return datsubaiendtantosyacode;
    }

    /**
     * @param datsubaiendtantosyacode
     */
    public void setDatsubaiendtantosyacode(String datsubaiendtantosyacode) {
        this.datsubaiendtantosyacode = datsubaiendtantosyacode;
    }

    /**
     * @return gouro1
     */
    public String getGouro1() {
        return gouro1;
    }

    /**
     * @param gouro1
     */
    public void setGouro1(String gouro1) {
        this.gouro1 = gouro1;
    }

    /**
     * @return peakondo
     */
    public Integer getPeakondo() {
        return peakondo;
    }

    /**
     * @param peakondo
     */
    public void setPeakondo(Integer peakondo) {
        this.peakondo = peakondo;
    }

    /**
     * @return okurispeed
     */
    public String getOkurispeed() {
        return okurispeed;
    }

    /**
     * @param okurispeed
     */
    public void setOkurispeed(String okurispeed) {
        this.okurispeed = okurispeed;
    }

    /**
     * @return nyuuronichiji1
     */
    public Timestamp getNyuuronichiji1() {
        return nyuuronichiji1;
    }

    /**
     * @param nyuuronichiji1
     */
    public void setNyuuronichiji1(Timestamp nyuuronichiji1) {
        this.nyuuronichiji1 = nyuuronichiji1;
    }

    /**
     * @return tantousya1
     */
    public String getTantousya1() {
        return tantousya1;
    }

    /**
     * @param tantousya1
     */
    public void setTantousya1(String tantousya1) {
        this.tantousya1 = tantousya1;
    }

    /**
     * @return syoseistartkakuninsyacode
     */
    public String getSyoseistartkakuninsyacode() {
        return syoseistartkakuninsyacode;
    }

    /**
     * @param syoseistartkakuninsyacode
     */
    public void setSyoseistartkakuninsyacode(String syoseistartkakuninsyacode) {
        this.syoseistartkakuninsyacode = syoseistartkakuninsyacode;
    }

    /**
     * @return syutsuronichiji1
     */
    public Timestamp getSyutsuronichiji1() {
        return syutsuronichiji1;
    }

    /**
     * @param syutsuronichiji1
     */
    public void setSyutsuronichiji1(Timestamp syutsuronichiji1) {
        this.syutsuronichiji1 = syutsuronichiji1;
    }

    /**
     * @return syoseiendtantosyacode
     */
    public String getSyoseiendtantosyacode() {
        return syoseiendtantosyacode;
    }

    /**
     * @param syoseiendtantosyacode
     */
    public void setSyoseiendtantosyacode(String syoseiendtantosyacode) {
        this.syoseiendtantosyacode = syoseiendtantosyacode;
    }

    /**
     * @return gaikan
     */
    public String getGaikan() {
        return gaikan;
    }

    /**
     * @param gaikan
     */
    public void setGaikan(String gaikan) {
        this.gaikan = gaikan;
    }

    /**
     * @return abeggryohinjyuryo
     */
    public BigDecimal getAbeggryohinjyuryo() {
        return abeggryohinjyuryo;
    }

    /**
     * @param abeggryohinjyuryo
     */
    public void setAbeggryohinjyuryo(BigDecimal abeggryohinjyuryo) {
        this.abeggryohinjyuryo = abeggryohinjyuryo;
    }

    /**
     * @return abeggfuryojyuryo
     */
    public BigDecimal getAbeggfuryojyuryo() {
        return abeggfuryojyuryo;
    }

    /**
     * @param abeggfuryojyuryo
     */
    public void setAbeggfuryojyuryo(BigDecimal abeggfuryojyuryo) {
        this.abeggfuryojyuryo = abeggfuryojyuryo;
    }

    /**
     * @return abeggfuryoritu
     */
    public BigDecimal getAbeggfuryoritu() {
        return abeggfuryoritu;
    }

    /**
     * @param abeggfuryoritu
     */
    public void setAbeggfuryoritu(BigDecimal abeggfuryoritu) {
        this.abeggfuryoritu = abeggfuryoritu;
    }

    /**
     * @return gaikankakuninnichiji
     */
    public Timestamp getGaikankakuninnichiji() {
        return gaikankakuninnichiji;
    }

    /**
     * @param gaikankakuninnichiji
     */
    public void setGaikankakuninnichiji(Timestamp gaikankakuninnichiji) {
        this.gaikankakuninnichiji = gaikankakuninnichiji;
    }

    /**
     * @return gaikantantosya
     */
    public String getGaikantantosya() {
        return gaikantantosya;
    }

    /**
     * @param gaikantantosya
     */
    public void setGaikantantosya(String gaikantantosya) {
        this.gaikantantosya = gaikantantosya;
    }

    /**
     * @return bikou1
     */
    public String getBikou1() {
        return bikou1;
    }

    /**
     * @param bikou1 bikou1
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
     * @param bikou2 bikou2
     */
    public void setBikou2(String bikou2) {
        this.bikou2 = bikou2;
    }

}
