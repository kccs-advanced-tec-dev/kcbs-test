/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
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
 * 変更日       2021/12/25<br>
 * 計画書No     MB2101-DK002<br>
 * 変更者       KCSS K.Jo<br>
 * 変更理由     新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)履歴検索画面のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/12/25
 */
public class GXHDO202B027Model implements Serializable {
    /** WIPﾛｯﾄNo */
    private String lotno = "";

    /** ｽﾘｯﾌﾟ品名 */
    private String sliphinmei = "";

    /** ｽﾘｯﾌﾟLotNo */
    private String sliplotno = "";

    /** ﾛｯﾄ区分 */
    private String lotkubun = "";

    /** 原料記号 */
    private String genryoukigou = "";

    /** ｽﾗﾘｰ品名 */
    private String slurryhinmei = "";

    /** ｽﾗﾘｰLotNo① */
    private Integer slurrylotno1 = null;

    /** ｽﾗﾘｰLotNo② */
    private Integer slurrylotno2 = null;

    /** ｽﾗﾘｰLotNo③ */
    private Integer slurrylotno3 = null;

    /** ｽﾗﾘｰ有効期限 */
    private String slurryyuukoukigen = "";

    /** 乾燥固形分 */
    private BigDecimal kansoukokeibun = null;

    /** 脱脂固形分 */
    private BigDecimal dassikokeibun = null;

    /** 粉砕終了日時 */
    private Timestamp funsaisyuuryounichiji = null;

    /** ﾊﾞｲﾝﾀﾞｰ混合日時 */
    private Timestamp binderkongounichij = null;

    /** ｽﾗﾘｰ経過日数 */
    private Integer slurrykeikanisuu = null;

    /** ｽﾗﾘｰ重量① */
    private Integer slurryjyuuryou1 = null;

    /** ｽﾗﾘｰ重量② */
    private Integer slurryjyuuryou2 = null;

    /** ｽﾗﾘｰ重量③ */
    private Integer slurryjyuuryou3 = null;

    /** ｽﾗﾘｰ重量④ */
    private Integer slurryjyuuryou4 = null;

    /** ｽﾗﾘｰ重量⑤ */
    private Integer slurryjyuuryou5 = null;

    /** ｽﾗﾘｰ重量⑥ */
    private Integer slurryjyuuryou6 = null;

    /** ｽﾗﾘｰ合計重量 */
    private Integer slurrygoukeijyuuryou = null;

    /** 固形分比率 */
    private BigDecimal kokeibunhiritu = null;

    /** 固形分調整量➀ */
    private Integer kokeibuntyouseiryou1 = null;

    /** 固形分調整量➁ */
    private Integer kokeibuntyouseiryou2 = null;

    /** 固形分調整量 */
    private Integer kokeibuntyouseiryou = null;

    /** ﾄﾙｴﾝ添加量 */
    private Integer toluenetenkaryou = null;

    /** ｿﾙﾐｯｸｽ添加量 */
    private Integer solmixtenkaryou = null;

    /** 担当者 */
    private String tantousya = "";

    /** 備考1 */
    private String bikou1 = "";

    /** 備考2 */
    private String bikou2 = "";

    /**
     * WIPﾛｯﾄNo
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * WIPﾛｯﾄNo
     * @param lotno the lotno to set
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * ｽﾘｯﾌﾟ品名
     * @return the sliphinmei
     */
    public String getSliphinmei() {
        return sliphinmei;
    }

    /**
     * ｽﾘｯﾌﾟ品名
     * @param sliphinmei the sliphinmei to set
     */
    public void setSliphinmei(String sliphinmei) {
        this.sliphinmei = sliphinmei;
    }

    /**
     * ｽﾘｯﾌﾟLotNo
     * @return the sliplotno
     */
    public String getSliplotno() {
        return sliplotno;
    }

    /**
     * ｽﾘｯﾌﾟLotNo
     * @param sliplotno the sliplotno to set
     */
    public void setSliplotno(String sliplotno) {
        this.sliplotno = sliplotno;
    }

    /**
     * ﾛｯﾄ区分
     * @return the lotkubun
     */
    public String getLotkubun() {
        return lotkubun;
    }

    /**
     * ﾛｯﾄ区分
     * @param lotkubun the lotkubun to set
     */
    public void setLotkubun(String lotkubun) {
        this.lotkubun = lotkubun;
    }

    /**
     * 原料記号
     * @return the genryoukigou
     */
    public String getGenryoukigou() {
        return genryoukigou;
    }

    /**
     * 原料記号
     * @param genryoukigou the genryoukigou to set
     */
    public void setGenryoukigou(String genryoukigou) {
        this.genryoukigou = genryoukigou;
    }

    /**
     * ｽﾗﾘｰ品名
     * @return the slurryhinmei
     */
    public String getSlurryhinmei() {
        return slurryhinmei;
    }

    /**
     * ｽﾗﾘｰ品名
     * @param slurryhinmei the slurryhinmei to set
     */
    public void setSlurryhinmei(String slurryhinmei) {
        this.slurryhinmei = slurryhinmei;
    }

    /**
     * ｽﾗﾘｰLotNo①
     * @return the slurrylotno1
     */
    public Integer getSlurrylotno1() {
        return slurrylotno1;
    }

    /**
     * ｽﾗﾘｰLotNo①
     * @param slurrylotno1 the slurrylotno1 to set
     */
    public void setSlurrylotno1(Integer slurrylotno1) {
        this.slurrylotno1 = slurrylotno1;
    }

    /**
     * ｽﾗﾘｰLotNo②
     * @return the slurrylotno2
     */
    public Integer getSlurrylotno2() {
        return slurrylotno2;
    }

    /**
     * ｽﾗﾘｰLotNo②
     * @param slurrylotno2 the slurrylotno2 to set
     */
    public void setSlurrylotno2(Integer slurrylotno2) {
        this.slurrylotno2 = slurrylotno2;
    }

    /**
     * ｽﾗﾘｰLotNo③
     * @return the slurrylotno3
     */
    public Integer getSlurrylotno3() {
        return slurrylotno3;
    }

    /**
     * ｽﾗﾘｰLotNo③
     * @param slurrylotno3 the slurrylotno3 to set
     */
    public void setSlurrylotno3(Integer slurrylotno3) {
        this.slurrylotno3 = slurrylotno3;
    }

    /**
     * ｽﾗﾘｰ有効期限
     * @return the slurryyuukoukigen
     */
    public String getSlurryyuukoukigen() {
        return slurryyuukoukigen;
    }

    /**
     * ｽﾗﾘｰ有効期限
     * @param slurryyuukoukigen the slurryyuukoukigen to set
     */
    public void setSlurryyuukoukigen(String slurryyuukoukigen) {
        this.slurryyuukoukigen = slurryyuukoukigen;
    }

    /**
     * 乾燥固形分
     * @return the kansoukokeibun
     */
    public BigDecimal getKansoukokeibun() {
        return kansoukokeibun;
    }

    /**
     * 乾燥固形分
     * @param kansoukokeibun the kansoukokeibun to set
     */
    public void setKansoukokeibun(BigDecimal kansoukokeibun) {
        this.kansoukokeibun = kansoukokeibun;
    }

    /**
     * 脱脂固形分
     * @return the dassikokeibun
     */
    public BigDecimal getDassikokeibun() {
        return dassikokeibun;
    }

    /**
     * 脱脂固形分
     * @param dassikokeibun the dassikokeibun to set
     */
    public void setDassikokeibun(BigDecimal dassikokeibun) {
        this.dassikokeibun = dassikokeibun;
    }

    /**
     * 粉砕終了日時
     * @return the funsaisyuuryounichiji
     */
    public Timestamp getFunsaisyuuryounichiji() {
        return funsaisyuuryounichiji;
    }

    /**
     * 粉砕終了日時
     * @param funsaisyuuryounichiji the funsaisyuuryounichiji to set
     */
    public void setFunsaisyuuryounichiji(Timestamp funsaisyuuryounichiji) {
        this.funsaisyuuryounichiji = funsaisyuuryounichiji;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合日時
     * @return the binderkongounichij
     */
    public Timestamp getBinderkongounichij() {
        return binderkongounichij;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合日時
     * @param binderkongounichij the binderkongounichij to set
     */
    public void setBinderkongounichij(Timestamp binderkongounichij) {
        this.binderkongounichij = binderkongounichij;
    }

    /**
     * ｽﾗﾘｰ経過日数
     * @return the slurrykeikanisuu
     */
    public Integer getSlurrykeikanisuu() {
        return slurrykeikanisuu;
    }

    /**
     * ｽﾗﾘｰ経過日数
     * @param slurrykeikanisuu the slurrykeikanisuu to set
     */
    public void setSlurrykeikanisuu(Integer slurrykeikanisuu) {
        this.slurrykeikanisuu = slurrykeikanisuu;
    }

    /**
     * ｽﾗﾘｰ重量①
     * @return the slurryjyuuryou1
     */
    public Integer getSlurryjyuuryou1() {
        return slurryjyuuryou1;
    }

    /**
     * ｽﾗﾘｰ重量①
     * @param slurryjyuuryou1 the slurryjyuuryou1 to set
     */
    public void setSlurryjyuuryou1(Integer slurryjyuuryou1) {
        this.slurryjyuuryou1 = slurryjyuuryou1;
    }

    /**
     * ｽﾗﾘｰ重量②
     * @return the slurryjyuuryou2
     */
    public Integer getSlurryjyuuryou2() {
        return slurryjyuuryou2;
    }

    /**
     * ｽﾗﾘｰ重量②
     * @param slurryjyuuryou2 the slurryjyuuryou2 to set
     */
    public void setSlurryjyuuryou2(Integer slurryjyuuryou2) {
        this.slurryjyuuryou2 = slurryjyuuryou2;
    }

    /**
     * ｽﾗﾘｰ重量③
     * @return the slurryjyuuryou3
     */
    public Integer getSlurryjyuuryou3() {
        return slurryjyuuryou3;
    }

    /**
     * ｽﾗﾘｰ重量③
     * @param slurryjyuuryou3 the slurryjyuuryou3 to set
     */
    public void setSlurryjyuuryou3(Integer slurryjyuuryou3) {
        this.slurryjyuuryou3 = slurryjyuuryou3;
    }

    /**
     * ｽﾗﾘｰ重量④
     * @return the slurryjyuuryou4
     */
    public Integer getSlurryjyuuryou4() {
        return slurryjyuuryou4;
    }

    /**
     * ｽﾗﾘｰ重量④
     * @param slurryjyuuryou4 the slurryjyuuryou4 to set
     */
    public void setSlurryjyuuryou4(Integer slurryjyuuryou4) {
        this.slurryjyuuryou4 = slurryjyuuryou4;
    }

    /**
     * ｽﾗﾘｰ重量⑤
     * @return the slurryjyuuryou5
     */
    public Integer getSlurryjyuuryou5() {
        return slurryjyuuryou5;
    }

    /**
     * ｽﾗﾘｰ重量⑤
     * @param slurryjyuuryou5 the slurryjyuuryou5 to set
     */
    public void setSlurryjyuuryou5(Integer slurryjyuuryou5) {
        this.slurryjyuuryou5 = slurryjyuuryou5;
    }

    /**
     * ｽﾗﾘｰ重量⑥
     * @return the slurryjyuuryou6
     */
    public Integer getSlurryjyuuryou6() {
        return slurryjyuuryou6;
    }

    /**
     * ｽﾗﾘｰ重量⑥
     * @param slurryjyuuryou6 the slurryjyuuryou6 to set
     */
    public void setSlurryjyuuryou6(Integer slurryjyuuryou6) {
        this.slurryjyuuryou6 = slurryjyuuryou6;
    }

    /**
     * ｽﾗﾘｰ合計重量
     * @return the slurrygoukeijyuuryou
     */
    public Integer getSlurrygoukeijyuuryou() {
        return slurrygoukeijyuuryou;
    }

    /**
     * ｽﾗﾘｰ合計重量
     * @param slurrygoukeijyuuryou the slurrygoukeijyuuryou to set
     */
    public void setSlurrygoukeijyuuryou(Integer slurrygoukeijyuuryou) {
        this.slurrygoukeijyuuryou = slurrygoukeijyuuryou;
    }

    /**
     * 固形分比率
     * @return the kokeibunhiritu
     */
    public BigDecimal getKokeibunhiritu() {
        return kokeibunhiritu;
    }

    /**
     * 固形分比率
     * @param kokeibunhiritu the kokeibunhiritu to set
     */
    public void setKokeibunhiritu(BigDecimal kokeibunhiritu) {
        this.kokeibunhiritu = kokeibunhiritu;
    }

    /**
     * 固形分調整量➀
     * @return the kokeibuntyouseiryou1
     */
    public Integer getKokeibuntyouseiryou1() {
        return kokeibuntyouseiryou1;
    }

    /**
     * 固形分調整量➀
     * @param kokeibuntyouseiryou1 the kokeibuntyouseiryou1 to set
     */
    public void setKokeibuntyouseiryou1(Integer kokeibuntyouseiryou1) {
        this.kokeibuntyouseiryou1 = kokeibuntyouseiryou1;
    }

    /**
     * 固形分調整量➁
     * @return the kokeibuntyouseiryou2
     */
    public Integer getKokeibuntyouseiryou2() {
        return kokeibuntyouseiryou2;
    }

    /**
     * 固形分調整量➁
     * @param kokeibuntyouseiryou2 the kokeibuntyouseiryou2 to set
     */
    public void setKokeibuntyouseiryou2(Integer kokeibuntyouseiryou2) {
        this.kokeibuntyouseiryou2 = kokeibuntyouseiryou2;
    }

    /**
     * 固形分調整量
     * @return the kokeibuntyouseiryou
     */
    public Integer getKokeibuntyouseiryou() {
        return kokeibuntyouseiryou;
    }

    /**
     * 固形分調整量
     * @param kokeibuntyouseiryou the kokeibuntyouseiryou to set
     */
    public void setKokeibuntyouseiryou(Integer kokeibuntyouseiryou) {
        this.kokeibuntyouseiryou = kokeibuntyouseiryou;
    }

    /**
     * ﾄﾙｴﾝ添加量
     * @return the toluenetenkaryou
     */
    public Integer getToluenetenkaryou() {
        return toluenetenkaryou;
    }

    /**
     * ﾄﾙｴﾝ添加量
     * @param toluenetenkaryou the toluenetenkaryou to set
     */
    public void setToluenetenkaryou(Integer toluenetenkaryou) {
        this.toluenetenkaryou = toluenetenkaryou;
    }

    /**
     * ｿﾙﾐｯｸｽ添加量
     * @return the solmixtenkaryou
     */
    public Integer getSolmixtenkaryou() {
        return solmixtenkaryou;
    }

    /**
     * ｿﾙﾐｯｸｽ添加量
     * @param solmixtenkaryou the solmixtenkaryou to set
     */
    public void setSolmixtenkaryou(Integer solmixtenkaryou) {
        this.solmixtenkaryou = solmixtenkaryou;
    }

    /**
     * 担当者
     * @return the tantousya
     */
    public String getTantousya() {
        return tantousya;
    }

    /**
     * 担当者
     * @param tantousya the tantousya to set
     */
    public void setTantousya(String tantousya) {
        this.tantousya = tantousya;
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

}