/*
 * Copyright 2020 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2020/09/09<br>
 * 計画書No	K2008-DS002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * ｾｯﾀ詰めのモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2020/09/09
 */
public class SrDouyaki {

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
     * ﾛｯﾄﾌﾟﾚ
     */
    private String lotpre;

    /**
     * 作業回数
     */
    private Integer kaisuu;

    /**
     * KCPNO
     */
    private String kcpno;

    /**
     * 処理数
     */
    private Integer suuryou;

    /**
     * 塗布日時
     */
    private Timestamp dipbi;

    /**
     * ｻﾔ/SUS板詰め開始日時
     */
    private Timestamp sayadumebi;

    /**
     * ｻﾔ詰め方法
     */
    private String sayadumehouhou;

    /**
     * ｻﾔ/SUS板枚数
     */
    private Integer sayamaisuu;

    /**
     * ｻﾔ重量(g/枚)
     */
    private BigDecimal sayajyuuryou;

    /**
     * ｻﾔ/SUS板ﾁｬｰｼﾞ量
     */
    private Integer sayachargeryou;

    /**
     * ｻﾔ/SUS板詰め開始担当者
     */
    private String sayadumetantousya;

    /**
     * 酸化
     */
    private String sanka;

    /**
     * 焼成開始日時
     */
    private Timestamp nyuuronichiji1;

    /**
     * 焼成終了日時
     */
    private Timestamp syutsuronichiji1;

    /**
     * 焼成号機
     */
    private String gouro1;

    /**
     * 焼成開始担当者
     */
    private String tantousya1;

    /**
     * 焼成開始日時2
     */
    private Timestamp nyuuronichiji2;

    /**
     * 焼成終了日時2
     */
    private Timestamp syutsuronichiji2;

    /**
     * 焼成号機2
     */
    private String gouro2;

    /**
     * 焼成開始担当者2
     */
    private String tantousya2;

    /**
     * 焼成開始日時3
     */
    private Timestamp nyuuronichiji3;

    /**
     * 焼成終了日時3
     */
    private Timestamp syutsuronichiji3;

    /**
     * 焼成号機3
     */
    private String gouro3;

    /**
     * 焼成開始担当者3
     */
    private String tantousya3;

    /**
     * 焼成開始日時4
     */
    private Timestamp nyuuronichiji4;

    /**
     * 焼成終了日時4
     */
    private Timestamp syutsuronichiji4;

    /**
     * 焼成号機4
     */
    private String gouro4;

    /**
     * 焼成開始担当者4
     */
    private String tantousya4;

    /**
     * 歩留まり
     */
    private BigDecimal budomari;

    /**
     * 歩留まり合否
     */
    private String budomarigouhi;

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
     * 実績No
     */
    private Integer jissekino;

    /**
     * 登録日時
     */
    private Timestamp tourokunichiji;

    /**
     * 更新日時
     */
    private Timestamp koushinnichiji;

    /**
     * 焼成温度
     */
    private Integer peakondo;

    /**
     * 良品重量
     */
    private BigDecimal abeggryohinjyuryo;

    /**
     * 不良重量
     */
    private BigDecimal abeggfuryojyuryo;

    /**
     * 不良率
     */
    private BigDecimal abeggfuryoritu;
    /**
     * 製品重量
     */
    private BigDecimal soujyuuryou;

    /**
     * ｻﾔ重量範囲(g)MIN
     */
    private BigDecimal sjyuuryourangemin;

    /**
     * ｻﾔ重量範囲(g)MAX
     */
    private BigDecimal sjyuuryourangemax;

    /**
     * 客先
     */
    private String kyakusaki;

    /**
     * ﾛｯﾄ区分
     */
    private String lotkubuncode;

    /**
     * ｵｰﾅｰ
     */
    private String ownercode;

    /**
     * 粉まぶし
     */
    private String konamabushi;

    /**
     * 塗布重量
     */
    private BigDecimal dipjuryou;

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
     * ｻﾔ/SUS板詰め開始確認者
     */
    private String sayadumekakuninsya;

    /**
     * ｻﾔ/SUS板詰め終了日時
     */
    private Timestamp sayadumeendnichiji;

    /**
     * ｻﾔ/SUS板詰め終了担当者
     */
    private String sayadumesyuryosya;

    /**
     * 脱ﾊﾞｲ号機
     */
    private String datsubaigouki;

    /**
     * 脱ﾊﾞｲ温度
     */
    private String datsubaiondo;

    /**
     * 脱ﾊﾞｲ時間
     */
    private Integer datsubaijikan;

    /**
     * 脱ﾊﾞｲPTNNO
     */
    private String datsubaiptnno;

    /**
     * 脱ﾊﾞｲｻﾔ枚数
     */
    private Integer datsubaisayamaisuu;

    /**
     * 脱ﾊﾞｲ開始日時
     */
    private Timestamp datsubaistartdatetime;

    /**
     * 脱ﾊﾞｲ開始担当者
     */
    private String datsubaistarttantosyacode;

    /**
     * 脱ﾊﾞｲ開始確認者
     */
    private String datsubaistartkakuninsyacode;

    /**
     * 脱ﾊﾞｲ終了日時
     */
    private Timestamp datsubaienddatetime;

    /**
     * 脱ﾊﾞｲ終了担当者
     */
    private String datsubaiendtantosyacode;

    /**
     * 焼成送りｽﾋﾟｰﾄﾞ
     */
    private String okurispeed;

    /**
     * 焼成開始確認者
     */
    private String syoseistartkakuninsyacode;

    /**
     * 焼成終了担当者
     */
    private String syoseiendtantosyacode;

    /**
     * 外観
     */
    private String gaikan;

    /**
     * 外観確認日時
     */
    private Timestamp gaikankakuninnichiji;

    /**
     * 外観担当者
     */
    private String gaikantantosya;

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

    public String getLotpre() {
        return lotpre;
    }

    public void setLotpre(String lotpre) {
        this.lotpre = lotpre;
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

    public Integer getSuuryou() {
        return suuryou;
    }

    public void setSuuryou(Integer suuryou) {
        this.suuryou = suuryou;
    }

    public Timestamp getDipbi() {
        return dipbi;
    }

    public void setDipbi(Timestamp dipbi) {
        this.dipbi = dipbi;
    }

    public Timestamp getSayadumebi() {
        return sayadumebi;
    }

    public void setSayadumebi(Timestamp sayadumebi) {
        this.sayadumebi = sayadumebi;
    }

    public String getSayadumehouhou() {
        return sayadumehouhou;
    }

    public void setSayadumehouhou(String sayadumehouhou) {
        this.sayadumehouhou = sayadumehouhou;
    }

    public Integer getSayamaisuu() {
        return sayamaisuu;
    }

    public void setSayamaisuu(Integer sayamaisuu) {
        this.sayamaisuu = sayamaisuu;
    }

    public BigDecimal getSayajyuuryou() {
        return sayajyuuryou;
    }

    public void setSayajyuuryou(BigDecimal sayajyuuryou) {
        this.sayajyuuryou = sayajyuuryou;
    }

    public Integer getSayachargeryou() {
        return sayachargeryou;
    }

    public void setSayachargeryou(Integer sayachargeryou) {
        this.sayachargeryou = sayachargeryou;
    }

    public String getSayadumetantousya() {
        return sayadumetantousya;
    }

    public void setSayadumetantousya(String sayadumetantousya) {
        this.sayadumetantousya = sayadumetantousya;
    }

    public String getSanka() {
        return sanka;
    }

    public void setSanka(String sanka) {
        this.sanka = sanka;
    }

    public Timestamp getNyuuronichiji1() {
        return nyuuronichiji1;
    }

    public void setNyuuronichiji1(Timestamp nyuuronichiji1) {
        this.nyuuronichiji1 = nyuuronichiji1;
    }

    public Timestamp getSyutsuronichiji1() {
        return syutsuronichiji1;
    }

    public void setSyutsuronichiji1(Timestamp syutsuronichiji1) {
        this.syutsuronichiji1 = syutsuronichiji1;
    }

    public String getGouro1() {
        return gouro1;
    }

    public void setGouro1(String gouro1) {
        this.gouro1 = gouro1;
    }

    public String getTantousya1() {
        return tantousya1;
    }

    public void setTantousya1(String tantousya1) {
        this.tantousya1 = tantousya1;
    }

    public Timestamp getNyuuronichiji2() {
        return nyuuronichiji2;
    }

    public void setNyuuronichiji2(Timestamp nyuuronichiji2) {
        this.nyuuronichiji2 = nyuuronichiji2;
    }

    public Timestamp getSyutsuronichiji2() {
        return syutsuronichiji2;
    }

    public void setSyutsuronichiji2(Timestamp syutsuronichiji2) {
        this.syutsuronichiji2 = syutsuronichiji2;
    }

    public String getGouro2() {
        return gouro2;
    }

    public void setGouro2(String gouro2) {
        this.gouro2 = gouro2;
    }

    public String getTantousya2() {
        return tantousya2;
    }

    public void setTantousya2(String tantousya2) {
        this.tantousya2 = tantousya2;
    }

    public Timestamp getNyuuronichiji3() {
        return nyuuronichiji3;
    }

    public void setNyuuronichiji3(Timestamp nyuuronichiji3) {
        this.nyuuronichiji3 = nyuuronichiji3;
    }

    public Timestamp getSyutsuronichiji3() {
        return syutsuronichiji3;
    }

    public void setSyutsuronichiji3(Timestamp syutsuronichiji3) {
        this.syutsuronichiji3 = syutsuronichiji3;
    }

    public String getGouro3() {
        return gouro3;
    }

    public void setGouro3(String gouro3) {
        this.gouro3 = gouro3;
    }

    public String getTantousya3() {
        return tantousya3;
    }

    public void setTantousya3(String tantousya3) {
        this.tantousya3 = tantousya3;
    }

    public Timestamp getNyuuronichiji4() {
        return nyuuronichiji4;
    }

    public void setNyuuronichiji4(Timestamp nyuuronichiji4) {
        this.nyuuronichiji4 = nyuuronichiji4;
    }

    public Timestamp getSyutsuronichiji4() {
        return syutsuronichiji4;
    }

    public void setSyutsuronichiji4(Timestamp syutsuronichiji4) {
        this.syutsuronichiji4 = syutsuronichiji4;
    }

    public String getGouro4() {
        return gouro4;
    }

    public void setGouro4(String gouro4) {
        this.gouro4 = gouro4;
    }

    public String getTantousya4() {
        return tantousya4;
    }

    public void setTantousya4(String tantousya4) {
        this.tantousya4 = tantousya4;
    }

    public BigDecimal getBudomari() {
        return budomari;
    }

    public void setBudomari(BigDecimal budomari) {
        this.budomari = budomari;
    }

    public String getBudomarigouhi() {
        return budomarigouhi;
    }

    public void setBudomarigouhi(String budomarigouhi) {
        this.budomarigouhi = budomarigouhi;
    }

    public String getBikou1() {
        return bikou1;
    }

    public void setBikou1(String bikou1) {
        this.bikou1 = bikou1;
    }

    public String getBikou2() {
        return bikou2;
    }

    public void setBikou2(String bikou2) {
        this.bikou2 = bikou2;
    }

    public String getBikou3() {
        return bikou3;
    }

    public void setBikou3(String bikou3) {
        this.bikou3 = bikou3;
    }

    public Integer getJissekino() {
        return jissekino;
    }

    public void setJissekino(Integer jissekino) {
        this.jissekino = jissekino;
    }

    public Timestamp getTourokunichiji() {
        return tourokunichiji;
    }

    public void setTourokunichiji(Timestamp tourokunichiji) {
        this.tourokunichiji = tourokunichiji;
    }

    public Timestamp getKoushinnichiji() {
        return koushinnichiji;
    }

    public void setKoushinnichiji(Timestamp koushinnichiji) {
        this.koushinnichiji = koushinnichiji;
    }

    public Integer getPeakondo() {
        return peakondo;
    }

    public void setPeakondo(Integer peakondo) {
        this.peakondo = peakondo;
    }

    public BigDecimal getAbeggryohinjyuryo() {
        return abeggryohinjyuryo;
    }

    public void setAbeggryohinjyuryo(BigDecimal abeggryohinjyuryo) {
        this.abeggryohinjyuryo = abeggryohinjyuryo;
    }

    public BigDecimal getAbeggfuryojyuryo() {
        return abeggfuryojyuryo;
    }

    public void setAbeggfuryojyuryo(BigDecimal abeggfuryojyuryo) {
        this.abeggfuryojyuryo = abeggfuryojyuryo;
    }

    public BigDecimal getAbeggfuryoritu() {
        return abeggfuryoritu;
    }

    public void setAbeggfuryoritu(BigDecimal abeggfuryoritu) {
        this.abeggfuryoritu = abeggfuryoritu;
    }

    public BigDecimal getSoujyuuryou() {
        return soujyuuryou;
    }

    public void setSoujyuuryou(BigDecimal soujyuuryou) {
        this.soujyuuryou = soujyuuryou;
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

    public String getKyakusaki() {
        return kyakusaki;
    }

    public void setKyakusaki(String kyakusaki) {
        this.kyakusaki = kyakusaki;
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

    public String getKonamabushi() {
        return konamabushi;
    }

    public void setKonamabushi(String konamabushi) {
        this.konamabushi = konamabushi;
    }

    public BigDecimal getDipjuryou() {
        return dipjuryou;
    }

    public void setDipjuryou(BigDecimal dipjuryou) {
        this.dipjuryou = dipjuryou;
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

    public String getSayadumekakuninsya() {
        return sayadumekakuninsya;
    }

    public void setSayadumekakuninsya(String sayadumekakuninsya) {
        this.sayadumekakuninsya = sayadumekakuninsya;
    }

    public Timestamp getSayadumeendnichiji() {
        return sayadumeendnichiji;
    }

    public void setSayadumeendnichiji(Timestamp sayadumeendnichiji) {
        this.sayadumeendnichiji = sayadumeendnichiji;
    }

    public String getSayadumesyuryosya() {
        return sayadumesyuryosya;
    }

    public void setSayadumesyuryosya(String sayadumesyuryosya) {
        this.sayadumesyuryosya = sayadumesyuryosya;
    }

    public String getDatsubaigouki() {
        return datsubaigouki;
    }

    public void setDatsubaigouki(String datsubaigouki) {
        this.datsubaigouki = datsubaigouki;
    }

    public String getDatsubaiondo() {
        return datsubaiondo;
    }

    public void setDatsubaiondo(String datsubaiondo) {
        this.datsubaiondo = datsubaiondo;
    }

    public Integer getDatsubaijikan() {
        return datsubaijikan;
    }

    public void setDatsubaijikan(Integer datsubaijikan) {
        this.datsubaijikan = datsubaijikan;
    }

    public String getDatsubaiptnno() {
        return datsubaiptnno;
    }

    public void setDatsubaiptnno(String datsubaiptnno) {
        this.datsubaiptnno = datsubaiptnno;
    }

    public Integer getDatsubaisayamaisuu() {
        return datsubaisayamaisuu;
    }

    public void setDatsubaisayamaisuu(Integer datsubaisayamaisuu) {
        this.datsubaisayamaisuu = datsubaisayamaisuu;
    }

    public Timestamp getDatsubaistartdatetime() {
        return datsubaistartdatetime;
    }

    public void setDatsubaistartdatetime(Timestamp datsubaistartdatetime) {
        this.datsubaistartdatetime = datsubaistartdatetime;
    }

    public String getDatsubaistarttantosyacode() {
        return datsubaistarttantosyacode;
    }

    public void setDatsubaistarttantosyacode(String datsubaistarttantosyacode) {
        this.datsubaistarttantosyacode = datsubaistarttantosyacode;
    }

    public String getDatsubaistartkakuninsyacode() {
        return datsubaistartkakuninsyacode;
    }

    public void setDatsubaistartkakuninsyacode(String datsubaistartkakuninsyacode) {
        this.datsubaistartkakuninsyacode = datsubaistartkakuninsyacode;
    }

    public Timestamp getDatsubaienddatetime() {
        return datsubaienddatetime;
    }

    public void setDatsubaienddatetime(Timestamp datsubaienddatetime) {
        this.datsubaienddatetime = datsubaienddatetime;
    }

    public String getDatsubaiendtantosyacode() {
        return datsubaiendtantosyacode;
    }

    public void setDatsubaiendtantosyacode(String datsubaiendtantosyacode) {
        this.datsubaiendtantosyacode = datsubaiendtantosyacode;
    }

    public String getOkurispeed() {
        return okurispeed;
    }

    public void setOkurispeed(String okurispeed) {
        this.okurispeed = okurispeed;
    }

    public String getSyoseistartkakuninsyacode() {
        return syoseistartkakuninsyacode;
    }

    public void setSyoseistartkakuninsyacode(String syoseistartkakuninsyacode) {
        this.syoseistartkakuninsyacode = syoseistartkakuninsyacode;
    }

    public String getSyoseiendtantosyacode() {
        return syoseiendtantosyacode;
    }

    public void setSyoseiendtantosyacode(String syoseiendtantosyacode) {
        this.syoseiendtantosyacode = syoseiendtantosyacode;
    }

    public String getGaikan() {
        return gaikan;
    }

    public void setGaikan(String gaikan) {
        this.gaikan = gaikan;
    }

    public Timestamp getGaikankakuninnichiji() {
        return gaikankakuninnichiji;
    }

    public void setGaikankakuninnichiji(Timestamp gaikankakuninnichiji) {
        this.gaikankakuninnichiji = gaikankakuninnichiji;
    }

    public String getGaikantantosya() {
        return gaikantantosya;
    }

    public void setGaikantantosya(String gaikantantosya) {
        this.gaikantantosya = gaikantantosya;
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
