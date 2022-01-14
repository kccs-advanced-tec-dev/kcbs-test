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
 * 変更日       2021/11/22<br>
 * 計画書No     MB2101-DK002<br>
 * 変更者       KCSS K.Jo<br>
 * 変更理由     新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰﾌｨﾙﾀｰﾊﾟｽ履歴検索画面のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/11/22
 */
public class GXHDO202B018Model implements Serializable {
    /** WIPﾛｯﾄNo */
    private String lotno = "";

    /** ﾊﾞｲﾝﾀﾞｰ溶液品名 */
    private String binderyouekihinmei = "";

    /** ﾊﾞｲﾝﾀﾞｰ溶液LotNo */
    private String binderyouekilotno = "";

    /** ﾛｯﾄ区分 */
    private String lotkubun = "";

    /** ﾃﾞｨｽﾊﾟ号機 */
    private String dispagouki = "";

    /** 風袋重量 */
    private Integer fuutaijyuuryou = null;

    /** ﾌｨﾙﾀｰ品名 */
    private String filterhinmei = "";

    /** ﾌｨﾙﾀｰLotNo */
    private String filterlotno = "";

    /** 圧力 */
    private BigDecimal aturyoku = null;

    /** 圧送開始日時 */
    private Timestamp assoukaisinichiji = null;

    /** 圧送終了日時 */
    private Timestamp assousyuuryounichiji = null;

    /** 圧送時間 */
    private Integer assoujikan = null;

    /** ﾌｨﾙﾀｰ使用本数 */
    private Integer filtersiyouhonsuu = null;

    /** 総重量測定 */
    private Integer soujyuuryousokutei = null;

    /** 正味重量 */
    private Integer syoumijyuuryou = null;

    /** ﾊﾞｲﾝﾀﾞｰ有効期限 */
    private String binderyuukoukigen = "";

    /** 測定日時 */
    private Timestamp sokuteinichiji = null;

    /** 粘度測定値 */
    private Integer nendosokuteiti = null;

    /** 温度 */
    private BigDecimal ondo = null;

    /** 合否判定 */
    private String gouhihantei = "";

    /** 担当者 */
    private String tantousya = "";

    /** 確認者 */
    private String kakuninsya = "";

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
     * ﾊﾞｲﾝﾀﾞｰ溶液品名
     * @return the binderyouekihinmei
     */
    public String getBinderyouekihinmei() {
        return binderyouekihinmei;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液品名
     * @param binderyouekihinmei the binderyouekihinmei to set
     */
    public void setBinderyouekihinmei(String binderyouekihinmei) {
        this.binderyouekihinmei = binderyouekihinmei;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液LotNo
     * @return the binderyouekilotno
     */
    public String getBinderyouekilotno() {
        return binderyouekilotno;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液LotNo
     * @param binderyouekilotno the binderyouekilotno to set
     */
    public void setBinderyouekilotno(String binderyouekilotno) {
        this.binderyouekilotno = binderyouekilotno;
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
     * ﾃﾞｨｽﾊﾟ号機
     * @return the dispagouki
     */
    public String getDispagouki() {
        return dispagouki;
    }

    /**
     * ﾃﾞｨｽﾊﾟ号機
     * @param dispagouki the dispagouki to set
     */
    public void setDispagouki(String dispagouki) {
        this.dispagouki = dispagouki;
    }

    /**
     * 風袋重量
     * @return the fuutaijyuuryou
     */
    public Integer getFuutaijyuuryou() {
        return fuutaijyuuryou;
    }

    /**
     * 風袋重量
     * @param fuutaijyuuryou the fuutaijyuuryou to set
     */
    public void setFuutaijyuuryou(Integer fuutaijyuuryou) {
        this.fuutaijyuuryou = fuutaijyuuryou;
    }

    /**
     * ﾌｨﾙﾀｰ品名
     * @return the filterhinmei
     */
    public String getFilterhinmei() {
        return filterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ品名
     * @param filterhinmei the filterhinmei to set
     */
    public void setFilterhinmei(String filterhinmei) {
        this.filterhinmei = filterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰLotNo
     * @return the filterlotno
     */
    public String getFilterlotno() {
        return filterlotno;
    }

    /**
     * ﾌｨﾙﾀｰLotNo
     * @param filterlotno the filterlotno to set
     */
    public void setFilterlotno(String filterlotno) {
        this.filterlotno = filterlotno;
    }

    /**
     * 圧力
     * @return the aturyoku
     */
    public BigDecimal getAturyoku() {
        return aturyoku;
    }

    /**
     * 圧力
     * @param aturyoku the aturyoku to set
     */
    public void setAturyoku(BigDecimal aturyoku) {
        this.aturyoku = aturyoku;
    }

    /**
     * 圧送開始日時
     * @return the assoukaisinichiji
     */
    public Timestamp getAssoukaisinichiji() {
        return assoukaisinichiji;
    }

    /**
     * 圧送開始日時
     * @param assoukaisinichiji the assoukaisinichiji to set
     */
    public void setAssoukaisinichiji(Timestamp assoukaisinichiji) {
        this.assoukaisinichiji = assoukaisinichiji;
    }

    /**
     * 圧送終了日時
     * @return the assousyuuryounichiji
     */
    public Timestamp getAssousyuuryounichiji() {
        return assousyuuryounichiji;
    }

    /**
     * 圧送終了日時
     * @param assousyuuryounichiji the assousyuuryounichiji to set
     */
    public void setAssousyuuryounichiji(Timestamp assousyuuryounichiji) {
        this.assousyuuryounichiji = assousyuuryounichiji;
    }

    /**
     * 圧送時間
     * @return the assoujikan
     */
    public Integer getAssoujikan() {
        return assoujikan;
    }

    /**
     * 圧送時間
     * @param assoujikan the assoujikan to set
     */
    public void setAssoujikan(Integer assoujikan) {
        this.assoujikan = assoujikan;
    }

    /**
     * ﾌｨﾙﾀｰ使用本数
     * @return the filtersiyouhonsuu
     */
    public Integer getFiltersiyouhonsuu() {
        return filtersiyouhonsuu;
    }

    /**
     * ﾌｨﾙﾀｰ使用本数
     * @param filtersiyouhonsuu the filtersiyouhonsuu to set
     */
    public void setFiltersiyouhonsuu(Integer filtersiyouhonsuu) {
        this.filtersiyouhonsuu = filtersiyouhonsuu;
    }

    /**
     * 総重量測定
     * @return the soujyuuryousokutei
     */
    public Integer getSoujyuuryousokutei() {
        return soujyuuryousokutei;
    }

    /**
     * 総重量測定
     * @param soujyuuryousokutei the soujyuuryousokutei to set
     */
    public void setSoujyuuryousokutei(Integer soujyuuryousokutei) {
        this.soujyuuryousokutei = soujyuuryousokutei;
    }

    /**
     * 正味重量
     * @return the syoumijyuuryou
     */
    public Integer getSyoumijyuuryou() {
        return syoumijyuuryou;
    }

    /**
     * 正味重量
     * @param syoumijyuuryou the syoumijyuuryou to set
     */
    public void setSyoumijyuuryou(Integer syoumijyuuryou) {
        this.syoumijyuuryou = syoumijyuuryou;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ有効期限
     * @return the binderyuukoukigen
     */
    public String getBinderyuukoukigen() {
        return binderyuukoukigen;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ有効期限
     * @param binderyuukoukigen the binderyuukoukigen to set
     */
    public void setBinderyuukoukigen(String binderyuukoukigen) {
        this.binderyuukoukigen = binderyuukoukigen;
    }

    /**
     * 測定日時
     * @return the sokuteinichiji
     */
    public Timestamp getSokuteinichiji() {
        return sokuteinichiji;
    }

    /**
     * 測定日時
     * @param sokuteinichiji the sokuteinichiji to set
     */
    public void setSokuteinichiji(Timestamp sokuteinichiji) {
        this.sokuteinichiji = sokuteinichiji;
    }

    /**
     * 粘度測定値
     * @return the nendosokuteiti
     */
    public Integer getNendosokuteiti() {
        return nendosokuteiti;
    }

    /**
     * 粘度測定値
     * @param nendosokuteiti the nendosokuteiti to set
     */
    public void setNendosokuteiti(Integer nendosokuteiti) {
        this.nendosokuteiti = nendosokuteiti;
    }

    /**
     * 温度
     * @return the ondo
     */
    public BigDecimal getOndo() {
        return ondo;
    }

    /**
     * 温度
     * @param ondo the ondo to set
     */
    public void setOndo(BigDecimal ondo) {
        this.ondo = ondo;
    }

    /**
     * 合否判定
     * @return the gouhihantei
     */
    public String getGouhihantei() {
        return gouhihantei;
    }

    /**
     * 合否判定
     * @param gouhihantei the gouhihantei to set
     */
    public void setGouhihantei(String gouhihantei) {
        this.gouhihantei = gouhihantei;
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
     * 確認者
     * @return the kakuninsya
     */
    public String getKakuninsya() {
        return kakuninsya;
    }

    /**
     * 確認者
     * @param kakuninsya the kakuninsya to set
     */
    public void setKakuninsya(String kakuninsya) {
        this.kakuninsya = kakuninsya;
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