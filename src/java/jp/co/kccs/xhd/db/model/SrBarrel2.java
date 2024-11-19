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
 * 変更日	2020/09/10<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 外部電極洗浄(超音波)のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2020/09/10
 */
public class SrBarrel2 {

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
     * ﾛｯﾄﾌﾟﾚ
     */
    private String lotpre;

    /**
     * KCPNO
     */
    private String kcpno;

    /**
     * 処理数
     */
    private Integer suuryou;

    /**
     * 客先
     */
    private String kyakusaki;

    /**
     * ｽﾃﾝﾊﾞｯﾄ数
     */
    private Integer potsuu;

    /**
     * ﾎﾟｯﾄ投入時間
     */
    private String pottounyuujikan;

    /**
     * 超音波開始担当者
     */
    private String potkaitentantousya;

    /**
     * 洗浄号機1
     */
    private String potkaitengouki;

    /**
     * 洗浄時間
     */
    private String potkaitenjikan;

    /**
     * 超音波開始日時
     */
    private Timestamp potkaitenkaishinichiji;

    /**
     * 超音波終了日時
     */
    private Timestamp potkaitensyuuryounichiji;

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
     * ﾊﾞﾗｼ洗浄時間
     */
    private String barashisenjyoujikan;

    /**
     * ﾊﾞﾗｼ終了時間
     */
    private String barashisyuuryoujikan;

    /**
     * 乾燥号機
     */
    private String kansougouki;

    /**
     * 乾燥開始日時
     */
    private Timestamp kansoukaishinichiji;

    /**
     * 乾燥終了日時
     */
    private Timestamp kansousyuuryounichiji;

    /**
     * 乾燥開始担当者
     */
    private String kansoutantousya;

    /**
     * ｻﾝﾌﾟﾙ
     */
    private String sample;

    /**
     * 端面外観ﾁｪｯｸ ﾋﾟﾝﾎｰﾙ
     */
    private Integer tanmenpinholecheck;

    /**
     * 端面外観ﾁｪｯｸ ﾍﾟｰｽﾄ不足
     */
    private Integer tanmenpastefusokucheck;

    /**
     * 端面外観ﾁｪｯｸ ﾌﾞﾛｯﾄ無
     */
    private Integer tanmenbulotnashicheck;

    /**
     * 端面外観ﾁｪｯｸ DIP無	
     */
    private Integer tanmendipnashicheck;

    /**
     * 端面外観ﾁｪｯｸ ｸﾗｯｸ
     */
    private Integer tanmencrackcheck;

    /**
     * 端面外観ﾁｪｯｸ 端面ﾊｶﾞﾚ
     */
    private Integer tanmenhagarecheck;

    /**
     * 端面外観ﾁｪｯｸ 1次電極露出
     */
    private Integer tanmendenkyokurosyutsucheck;

    /**
     * 乾燥外観
     */
    private String hantei;

    /**
     * ﾊﾟﾀｰﾝNo
     */
    private String patternno;

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
     * ﾛｯﾄ区分
     */
    private String lotkubuncode;

    /**
     * ｵｰﾅｰ
     */
    private String ownercode;

    /**
     * 洗浄号機2
     */
    private String gouki2;

    /**
     * 洗浄号機3
     */
    private String gouki3;

    /**
     * 洗浄号機4	
     */
    private String gouki4;

    /**
     * 重量
     */
    private BigDecimal juryou;

    /**
     * ﾁｬｰｼﾞ量
     */
    private Integer chargeroyu;

    /**
     * 超音波開始確認者
     */
    private String startkakuninsyacode;

    /**
     * 超音波終了担当者
     */
    private String endtantosyacode;

    /**
     * 乾燥開始確認者
     */
    private String kansostartkakuninsyacode;

    /**
     * 乾燥終了担当者
     */
    private String kansoendtantosyacode;

    /**
     * 乾燥ﾌﾙｲ選別
     */
    private String furuisenbetu;

    /**
     * revision
     */
    private Long revision;

    /**
     * 削除ﾌﾗｸﾞ
     */
    private Integer deleteflag;

    /**
     * 外観担当者
     */
    private String gaikantantousya;

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

    public String getLotpre() {
        return lotpre;
    }

    public void setLotpre(String lotpre) {
        this.lotpre = lotpre;
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

    public String getKyakusaki() {
        return kyakusaki;
    }

    public void setKyakusaki(String kyakusaki) {
        this.kyakusaki = kyakusaki;
    }

    public Integer getPotsuu() {
        return potsuu;
    }

    public void setPotsuu(Integer potsuu) {
        this.potsuu = potsuu;
    }

    public String getPottounyuujikan() {
        return pottounyuujikan;
    }

    public void setPottounyuujikan(String pottounyuujikan) {
        this.pottounyuujikan = pottounyuujikan;
    }

    public String getPotkaitentantousya() {
        return potkaitentantousya;
    }

    public void setPotkaitentantousya(String potkaitentantousya) {
        this.potkaitentantousya = potkaitentantousya;
    }

    public String getPotkaitengouki() {
        return potkaitengouki;
    }

    public void setPotkaitengouki(String potkaitengouki) {
        this.potkaitengouki = potkaitengouki;
    }

    public String getPotkaitenjikan() {
        return potkaitenjikan;
    }

    public void setPotkaitenjikan(String potkaitenjikan) {
        this.potkaitenjikan = potkaitenjikan;
    }

    public Timestamp getPotkaitenkaishinichiji() {
        return potkaitenkaishinichiji;
    }

    public void setPotkaitenkaishinichiji(Timestamp potkaitenkaishinichiji) {
        this.potkaitenkaishinichiji = potkaitenkaishinichiji;
    }

    public Timestamp getPotkaitensyuuryounichiji() {
        return potkaitensyuuryounichiji;
    }

    public void setPotkaitensyuuryounichiji(Timestamp potkaitensyuuryounichiji) {
        this.potkaitensyuuryounichiji = potkaitensyuuryounichiji;
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

    public String getBarashisenjyoujikan() {
        return barashisenjyoujikan;
    }

    public void setBarashisenjyoujikan(String barashisenjyoujikan) {
        this.barashisenjyoujikan = barashisenjyoujikan;
    }

    public String getBarashisyuuryoujikan() {
        return barashisyuuryoujikan;
    }

    public void setBarashisyuuryoujikan(String barashisyuuryoujikan) {
        this.barashisyuuryoujikan = barashisyuuryoujikan;
    }

    public String getKansougouki() {
        return kansougouki;
    }

    public void setKansougouki(String kansougouki) {
        this.kansougouki = kansougouki;
    }

    public Timestamp getKansoukaishinichiji() {
        return kansoukaishinichiji;
    }

    public void setKansoukaishinichiji(Timestamp kansoukaishinichiji) {
        this.kansoukaishinichiji = kansoukaishinichiji;
    }

    public Timestamp getKansousyuuryounichiji() {
        return kansousyuuryounichiji;
    }

    public void setKansousyuuryounichiji(Timestamp kansousyuuryounichiji) {
        this.kansousyuuryounichiji = kansousyuuryounichiji;
    }

    public String getKansoutantousya() {
        return kansoutantousya;
    }

    public void setKansoutantousya(String kansoutantousya) {
        this.kansoutantousya = kansoutantousya;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public Integer getTanmenpinholecheck() {
        return tanmenpinholecheck;
    }

    public void setTanmenpinholecheck(Integer tanmenpinholecheck) {
        this.tanmenpinholecheck = tanmenpinholecheck;
    }

    public Integer getTanmenpastefusokucheck() {
        return tanmenpastefusokucheck;
    }

    public void setTanmenpastefusokucheck(Integer tanmenpastefusokucheck) {
        this.tanmenpastefusokucheck = tanmenpastefusokucheck;
    }

    public Integer getTanmenbulotnashicheck() {
        return tanmenbulotnashicheck;
    }

    public void setTanmenbulotnashicheck(Integer tanmenbulotnashicheck) {
        this.tanmenbulotnashicheck = tanmenbulotnashicheck;
    }

    public Integer getTanmendipnashicheck() {
        return tanmendipnashicheck;
    }

    public void setTanmendipnashicheck(Integer tanmendipnashicheck) {
        this.tanmendipnashicheck = tanmendipnashicheck;
    }

    public Integer getTanmencrackcheck() {
        return tanmencrackcheck;
    }

    public void setTanmencrackcheck(Integer tanmencrackcheck) {
        this.tanmencrackcheck = tanmencrackcheck;
    }

    public Integer getTanmenhagarecheck() {
        return tanmenhagarecheck;
    }

    public void setTanmenhagarecheck(Integer tanmenhagarecheck) {
        this.tanmenhagarecheck = tanmenhagarecheck;
    }

    public Integer getTanmendenkyokurosyutsucheck() {
        return tanmendenkyokurosyutsucheck;
    }

    public void setTanmendenkyokurosyutsucheck(Integer tanmendenkyokurosyutsucheck) {
        this.tanmendenkyokurosyutsucheck = tanmendenkyokurosyutsucheck;
    }

    public String getHantei() {
        return hantei;
    }

    public void setHantei(String hantei) {
        this.hantei = hantei;
    }

    public String getPatternno() {
        return patternno;
    }

    public void setPatternno(String patternno) {
        this.patternno = patternno;
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

    public String getGouki2() {
        return gouki2;
    }

    public void setGouki2(String gouki2) {
        this.gouki2 = gouki2;
    }

    public String getGouki3() {
        return gouki3;
    }

    public void setGouki3(String gouki3) {
        this.gouki3 = gouki3;
    }

    public String getGouki4() {
        return gouki4;
    }

    public void setGouki4(String gouki4) {
        this.gouki4 = gouki4;
    }

    public BigDecimal getJuryou() {
        return juryou;
    }

    public void setJuryou(BigDecimal juryou) {
        this.juryou = juryou;
    }

    public Integer getChargeroyu() {
        return chargeroyu;
    }

    public void setChargeroyu(Integer chargeroyu) {
        this.chargeroyu = chargeroyu;
    }

    public String getStartkakuninsyacode() {
        return startkakuninsyacode;
    }

    public void setStartkakuninsyacode(String startkakuninsyacode) {
        this.startkakuninsyacode = startkakuninsyacode;
    }

    public String getEndtantosyacode() {
        return endtantosyacode;
    }

    public void setEndtantosyacode(String endtantosyacode) {
        this.endtantosyacode = endtantosyacode;
    }

    public String getKansostartkakuninsyacode() {
        return kansostartkakuninsyacode;
    }

    public void setKansostartkakuninsyacode(String kansostartkakuninsyacode) {
        this.kansostartkakuninsyacode = kansostartkakuninsyacode;
    }

    public String getKansoendtantosyacode() {
        return kansoendtantosyacode;
    }

    public void setKansoendtantosyacode(String kansoendtantosyacode) {
        this.kansoendtantosyacode = kansoendtantosyacode;
    }

    public String getFuruisenbetu() {
        return furuisenbetu;
    }

    public void setFuruisenbetu(String furuisenbetu) {
        this.furuisenbetu = furuisenbetu;
    }

    public Long getRevision() {
        return revision;
    }

    public void setRevision(Long revision) {
        this.revision = revision;
    }

    public Integer getDeleteflag() {
        return deleteflag;
    }

    public void setDeleteflag(Integer deleteflag) {
        this.deleteflag = deleteflag;
    }

    /**
     * 外観担当者
     * @return the gaikantantousya
     */
    public String getGaikantantousya() {
        return gaikantantousya;
    }

    /**
     * 外観担当者
     * @param gaikantantousya the gaikantantousya to set
     */
    public void setGaikantantousya(String gaikantantousya) {
        this.gaikantantousya = gaikantantousya;
    }

}
