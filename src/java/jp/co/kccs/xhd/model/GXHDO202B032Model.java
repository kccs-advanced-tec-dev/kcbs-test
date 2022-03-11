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
 * ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ混合履歴検索画面のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/12/25
 */
public class GXHDO202B032Model implements Serializable {
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

    /** 撹拌ﾓｰﾄﾞ */
    private String kakuhanmode = "";

    /** 冷却水ﾊﾞﾙﾌﾞ開 */
    private String reikyakusuivalvekai = "";

    /** 回転方向 */
    private String kaitenhoukou = "";

    /** 回転数(rpm) */
    private Integer kaitensuu = null;

    /** ﾊﾞｲﾝﾀﾞｰ混合時間 */
    private String binderkongoujikan = "";

    /** ﾊﾞｲﾝﾀﾞｰ混合開始日時 */
    private Timestamp binderkongoukaisinichiji = null;

    /** 開始電流値(A) */
    private BigDecimal kaisidenryuuti = null;

    /** 温度(往) */
    private Integer ondo_ou = null;

    /** 温度(還) */
    private Integer ondo_kan = null;

    /** 圧力(往) */
    private BigDecimal aturyoku_ou = null;

    /** 圧力(還) */
    private BigDecimal aturyoku_kan = null;

    /** ﾊﾞｲﾝﾀﾞｰ混合終了予定日時 */
    private Timestamp binderkongousyuuryouyoteinichiji = null;

    /** 1ﾛｯﾄ当たりｽﾗﾘｰ重量 */
    private Integer ichilotatarislurryjyuuryou = null;

    /** ｽﾘｯﾌﾟ予定重量 */
    private Integer slipyoteijyuuryou = null;

    /** ﾊﾞｲﾝﾀﾞｰ混合開始担当者 */
    private String binderkongoukaisitantousya = "";

    /** ｽﾘｯﾌﾟ固形分測定 */
    private String slipkokeibunsokutei = "";

    /** ﾊﾞｲﾝﾀﾞｰ混合終了日時 */
    private Timestamp binderkongousyuuryounichiji = null;

    /** 終了電流値(A) */
    private BigDecimal syuuryoudenryuuti = null;

    /** ﾊﾞｲﾝﾀﾞｰ混合終了担当者 */
    private String binderkongousyuuryoutantousya = "";

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
     * 撹拌ﾓｰﾄﾞ
     * @return the kakuhanmode
     */
    public String getKakuhanmode() {
        return kakuhanmode;
    }

    /**
     * 撹拌ﾓｰﾄﾞ
     * @param kakuhanmode the kakuhanmode to set
     */
    public void setKakuhanmode(String kakuhanmode) {
        this.kakuhanmode = kakuhanmode;
    }

    /**
     * 冷却水ﾊﾞﾙﾌﾞ開
     * @return the reikyakusuivalvekai
     */
    public String getReikyakusuivalvekai() {
        return reikyakusuivalvekai;
    }

    /**
     * 冷却水ﾊﾞﾙﾌﾞ開
     * @param reikyakusuivalvekai the reikyakusuivalvekai to set
     */
    public void setReikyakusuivalvekai(String reikyakusuivalvekai) {
        this.reikyakusuivalvekai = reikyakusuivalvekai;
    }

    /**
     * 回転方向
     * @return the kaitenhoukou
     */
    public String getKaitenhoukou() {
        return kaitenhoukou;
    }

    /**
     * 回転方向
     * @param kaitenhoukou the kaitenhoukou to set
     */
    public void setKaitenhoukou(String kaitenhoukou) {
        this.kaitenhoukou = kaitenhoukou;
    }

    /**
     * 回転数(rpm)
     * @return the kaitensuu
     */
    public Integer getKaitensuu() {
        return kaitensuu;
    }

    /**
     * 回転数(rpm)
     * @param kaitensuu the kaitensuu to set
     */
    public void setKaitensuu(Integer kaitensuu) {
        this.kaitensuu = kaitensuu;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合時間
     * @return the binderkongoujikan
     */
    public String getBinderkongoujikan() {
        return binderkongoujikan;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合時間
     * @param binderkongoujikan the binderkongoujikan to set
     */
    public void setBinderkongoujikan(String binderkongoujikan) {
        this.binderkongoujikan = binderkongoujikan;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合開始日時
     * @return the binderkongoukaisinichiji
     */
    public Timestamp getBinderkongoukaisinichiji() {
        return binderkongoukaisinichiji;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合開始日時
     * @param binderkongoukaisinichiji the binderkongoukaisinichiji to set
     */
    public void setBinderkongoukaisinichiji(Timestamp binderkongoukaisinichiji) {
        this.binderkongoukaisinichiji = binderkongoukaisinichiji;
    }

    /**
     * 開始電流値(A)
     * @return the kaisidenryuuti
     */
    public BigDecimal getKaisidenryuuti() {
        return kaisidenryuuti;
    }

    /**
     * 開始電流値(A)
     * @param kaisidenryuuti the kaisidenryuuti to set
     */
    public void setKaisidenryuuti(BigDecimal kaisidenryuuti) {
        this.kaisidenryuuti = kaisidenryuuti;
    }

    /**
     * 温度(往)
     * @return the ondo_ou
     */
    public Integer getOndo_ou() {
        return ondo_ou;
    }

    /**
     * 温度(往)
     * @param ondo_ou the ondo_ou to set
     */
    public void setOndo_ou(Integer ondo_ou) {
        this.ondo_ou = ondo_ou;
    }

    /**
     * 温度(還)
     * @return the ondo_kan
     */
    public Integer getOndo_kan() {
        return ondo_kan;
    }

    /**
     * 温度(還)
     * @param ondo_kan the ondo_kan to set
     */
    public void setOndo_kan(Integer ondo_kan) {
        this.ondo_kan = ondo_kan;
    }

    /**
     * 圧力(往)
     * @return the aturyoku_ou
     */
    public BigDecimal getAturyoku_ou() {
        return aturyoku_ou;
    }

    /**
     * 圧力(往)
     * @param aturyoku_ou the aturyoku_ou to set
     */
    public void setAturyoku_ou(BigDecimal aturyoku_ou) {
        this.aturyoku_ou = aturyoku_ou;
    }

    /**
     * 圧力(還)
     * @return the aturyoku_kan
     */
    public BigDecimal getAturyoku_kan() {
        return aturyoku_kan;
    }

    /**
     * 圧力(還)
     * @param aturyoku_kan the aturyoku_kan to set
     */
    public void setAturyoku_kan(BigDecimal aturyoku_kan) {
        this.aturyoku_kan = aturyoku_kan;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合終了予定日時
     * @return the binderkongousyuuryouyoteinichiji
     */
    public Timestamp getBinderkongousyuuryouyoteinichiji() {
        return binderkongousyuuryouyoteinichiji;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合終了予定日時
     * @param binderkongousyuuryouyoteinichiji the binderkongousyuuryouyoteinichiji to set
     */
    public void setBinderkongousyuuryouyoteinichiji(Timestamp binderkongousyuuryouyoteinichiji) {
        this.binderkongousyuuryouyoteinichiji = binderkongousyuuryouyoteinichiji;
    }

    /**
     * 1ﾛｯﾄ当たりｽﾗﾘｰ重量
     * @return the ichilotatarislurryjyuuryou
     */
    public Integer getIchilotatarislurryjyuuryou() {
        return ichilotatarislurryjyuuryou;
    }

    /**
     * 1ﾛｯﾄ当たりｽﾗﾘｰ重量
     * @param ichilotatarislurryjyuuryou the ichilotatarislurryjyuuryou to set
     */
    public void setIchilotatarislurryjyuuryou(Integer ichilotatarislurryjyuuryou) {
        this.ichilotatarislurryjyuuryou = ichilotatarislurryjyuuryou;
    }

    /**
     * ｽﾘｯﾌﾟ予定重量
     * @return the slipyoteijyuuryou
     */
    public Integer getSlipyoteijyuuryou() {
        return slipyoteijyuuryou;
    }

    /**
     * ｽﾘｯﾌﾟ予定重量
     * @param slipyoteijyuuryou the slipyoteijyuuryou to set
     */
    public void setSlipyoteijyuuryou(Integer slipyoteijyuuryou) {
        this.slipyoteijyuuryou = slipyoteijyuuryou;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合開始担当者
     * @return the binderkongoukaisitantousya
     */
    public String getBinderkongoukaisitantousya() {
        return binderkongoukaisitantousya;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合開始担当者
     * @param binderkongoukaisitantousya the binderkongoukaisitantousya to set
     */
    public void setBinderkongoukaisitantousya(String binderkongoukaisitantousya) {
        this.binderkongoukaisitantousya = binderkongoukaisitantousya;
    }

    /**
     * ｽﾘｯﾌﾟ固形分測定
     * @return the slipkokeibunsokutei
     */
    public String getSlipkokeibunsokutei() {
        return slipkokeibunsokutei;
    }

    /**
     * ｽﾘｯﾌﾟ固形分測定
     * @param slipkokeibunsokutei the slipkokeibunsokutei to set
     */
    public void setSlipkokeibunsokutei(String slipkokeibunsokutei) {
        this.slipkokeibunsokutei = slipkokeibunsokutei;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合終了日時
     * @return the binderkongousyuuryounichiji
     */
    public Timestamp getBinderkongousyuuryounichiji() {
        return binderkongousyuuryounichiji;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合終了日時
     * @param binderkongousyuuryounichiji the binderkongousyuuryounichiji to set
     */
    public void setBinderkongousyuuryounichiji(Timestamp binderkongousyuuryounichiji) {
        this.binderkongousyuuryounichiji = binderkongousyuuryounichiji;
    }

    /**
     * 終了電流値(A)
     * @return the syuuryoudenryuuti
     */
    public BigDecimal getSyuuryoudenryuuti() {
        return syuuryoudenryuuti;
    }

    /**
     * 終了電流値(A)
     * @param syuuryoudenryuuti the syuuryoudenryuuti to set
     */
    public void setSyuuryoudenryuuti(BigDecimal syuuryoudenryuuti) {
        this.syuuryoudenryuuti = syuuryoudenryuuti;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合終了担当者
     * @return the binderkongousyuuryoutantousya
     */
    public String getBinderkongousyuuryoutantousya() {
        return binderkongousyuuryoutantousya;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合終了担当者
     * @param binderkongousyuuryoutantousya the binderkongousyuuryoutantousya to set
     */
    public void setBinderkongousyuuryoutantousya(String binderkongousyuuryoutantousya) {
        this.binderkongousyuuryoutantousya = binderkongousyuuryoutantousya;
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