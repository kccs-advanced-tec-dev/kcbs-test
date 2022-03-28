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
 * ｽﾘｯﾌﾟ作製・FP(バケツ)履歴検索画面のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/12/25
 */
public class GXHDO202B035Model implements Serializable {
    /** WIPﾛｯﾄNo */
    private String lotno = "";

    /** 実績No */
    private Integer jissekino = null;

    /** ｽﾘｯﾌﾟ品名 */
    private String sliphinmei = "";

    /** ｽﾘｯﾌﾟLotNo */
    private String sliplotno = "";

    /** ﾛｯﾄ区分 */
    private String lotkubun = "";

    /** 原料記号 */
    private String genryoukigou = "";

    /** 排出容器の内袋 */
    private String haisyutsuyouki = "";

    /** 風袋重量① */
    private Integer hutaijyuuryou1 = null;

    /** 風袋重量② */
    private Integer hutaijyuuryou2 = null;

    /** 風袋重量③ */
    private Integer hutaijyuuryou3 = null;

    /** 風袋重量④ */
    private Integer hutaijyuuryou4 = null;

    /** 風袋重量⑤ */
    private Integer hutaijyuuryou5 = null;

    /** 風袋重量⑥ */
    private Integer hutaijyuuryou6 = null;

    /** 保管容器準備_担当者 */
    private String hokanyoukijyunbitantousya = "";

    /** ﾌｨﾙﾀｰ連結 */
    private String filterrenketsu = "";

    /** ﾌｨﾙﾀｰ取り付け_1次ﾌｨﾙﾀｰ品名 */
    private String filtertoritsuke1filterhinmei = "";

    /** ﾌｨﾙﾀｰ取り付け_LotNo1 */
    private String filtertoritsukefilterlotno1 = "";

    /** ﾌｨﾙﾀｰ取り付け_取り付け本数1 */
    private Integer filtertoritsuketoritsukehonsuu1 = null;

    /** ﾌｨﾙﾀｰ取り付け_2次ﾌｨﾙﾀｰ品名 */
    private String filtertoritsuke2filterhinmei = "";

    /** ﾌｨﾙﾀｰ取り付け_LotNo2 */
    private String filtertoritsukefilterlotno2 = "";

    /** ﾌｨﾙﾀｰ取り付け_取り付け本数2 */
    private Integer filtertoritsuketoritsukehonsuu2 = null;

    /** ﾌｨﾙﾀｰ取り付け_担当者 */
    private String filtertoritsuketantousya = "";

    /** 圧送ﾀﾝｸNo */
    private Integer assoutankno = null;

    /** F/P開始日時 */
    private Timestamp fpkaishinichiji = null;

    /** 圧送ﾚｷﾞｭﾚｰﾀｰNo */
    private String assouregulatorno = "";

    /** 圧送圧力規格 */
    private String assouatsuryokukikaku = "";

    /** 圧送圧力 */
    private BigDecimal assouatsuryoku = null;

    /** ﾌｨﾙﾀｰﾊﾟｽ開始_担当者 */
    private String filterpasskaishitantousya = "";

    /** 保存用ｻﾝﾌﾟﾙ回収 */
    private String hozonyousamplekaisyuu = "";

    /** ﾌｨﾙﾀｰ交換①_F/P停止日時 */
    private Timestamp filterkoukan1fpteishinichiji = null;

    /** ﾌｨﾙﾀｰ交換①_1次ﾌｨﾙﾀｰ品名 */
    private String filterkoukan11filterhinmei = "";

    /** ﾌｨﾙﾀｰ交換①_LotNo1 */
    private String filterkoukan1lotno1 = "";

    /** ﾌｨﾙﾀｰ交換①_取り付け本数1 */
    private Integer filterkoukan1toritsukehonnsuu1 = null;

    /** ﾌｨﾙﾀｰ交換①_2次ﾌｨﾙﾀｰ品名 */
    private String filterkoukan12filterhinmei = "";

    /** ﾌｨﾙﾀｰ交換①_LotNo2 */
    private String filterkoukan1lotno2 = "";

    /** ﾌｨﾙﾀｰ交換①_取り付け本数2 */
    private Integer filterkoukan1toritsukehonnsuu2 = null;

    /** ﾌｨﾙﾀｰ交換①_3次ﾌｨﾙﾀｰ品名 */
    private String filterkoukan13filterhinmei = "";

    /** ﾌｨﾙﾀｰ交換①_LotNo3 */
    private String filterkoukan1lotno3 = "";

    /** ﾌｨﾙﾀｰ交換①_取り付け本数3 */
    private Integer filterkoukan1toritsukehonnsuu3 = null;

    /** ﾌｨﾙﾀｰ交換①_F/P再開日時 */
    private Timestamp filterkoukan1fpsaikainichiji = null;

    /** ﾌｨﾙﾀｰ交換①_担当者 */
    private String filterkoukan1tantousya = "";

    /** F/P終了日時 */
    private Timestamp fpsyuryounichiji = null;

    /** F/P時間 */
    private String fpzikan = "";

    /** ﾌｨﾙﾀｰﾊﾟｽ終了_担当者 */
    private String filterpasssyuuryoutantousya = "";

    /** 総重量1 */
    private Integer soujyuryou1 = null;

    /** 総重量2 */
    private Integer soujyuryou2 = null;

    /** 総重量3 */
    private Integer soujyuryou3 = null;

    /** 総重量4 */
    private Integer soujyuryou4 = null;

    /** 総重量5 */
    private Integer soujyuryou5 = null;

    /** 総重量6 */
    private Integer soujyuryou6 = null;

    /** 正味重量1 */
    private Integer syoumijyuuryou1 = null;

    /** 正味重量2 */
    private Integer syoumijyuuryou2 = null;

    /** 正味重量3 */
    private Integer syoumijyuuryou3 = null;

    /** 正味重量4 */
    private Integer syoumijyuuryou4 = null;

    /** 正味重量5 */
    private Integer syoumijyuuryou5 = null;

    /** 正味重量6 */
    private Integer syoumijyuuryou6 = null;

    /** ｽﾘｯﾌﾟ重量合計 */
    private Integer slipjyuuryougoukei = null;

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
     * 実績No
     * @return the jissekino
     */
    public Integer getJissekino() {
        return jissekino;
    }

    /**
     * 実績No
     * @param jissekino the jissekino to set
     */
    public void setJissekino(Integer jissekino) {
        this.jissekino = jissekino;
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
     * 排出容器の内袋
     * @return the haisyutsuyouki
     */
    public String getHaisyutsuyouki() {
        return haisyutsuyouki;
    }

    /**
     * 排出容器の内袋
     * @param haisyutsuyouki the haisyutsuyouki to set
     */
    public void setHaisyutsuyouki(String haisyutsuyouki) {
        this.haisyutsuyouki = haisyutsuyouki;
    }

    /**
     * 風袋重量①
     * @return the hutaijyuuryou1
     */
    public Integer getHutaijyuuryou1() {
        return hutaijyuuryou1;
    }

    /**
     * 風袋重量①
     * @param hutaijyuuryou1 the hutaijyuuryou1 to set
     */
    public void setHutaijyuuryou1(Integer hutaijyuuryou1) {
        this.hutaijyuuryou1 = hutaijyuuryou1;
    }

    /**
     * 風袋重量②
     * @return the hutaijyuuryou2
     */
    public Integer getHutaijyuuryou2() {
        return hutaijyuuryou2;
    }

    /**
     * 風袋重量②
     * @param hutaijyuuryou2 the hutaijyuuryou2 to set
     */
    public void setHutaijyuuryou2(Integer hutaijyuuryou2) {
        this.hutaijyuuryou2 = hutaijyuuryou2;
    }

    /**
     * 風袋重量③
     * @return the hutaijyuuryou3
     */
    public Integer getHutaijyuuryou3() {
        return hutaijyuuryou3;
    }

    /**
     * 風袋重量③
     * @param hutaijyuuryou3 the hutaijyuuryou3 to set
     */
    public void setHutaijyuuryou3(Integer hutaijyuuryou3) {
        this.hutaijyuuryou3 = hutaijyuuryou3;
    }

    /**
     * 風袋重量④
     * @return the hutaijyuuryou4
     */
    public Integer getHutaijyuuryou4() {
        return hutaijyuuryou4;
    }

    /**
     * 風袋重量④
     * @param hutaijyuuryou4 the hutaijyuuryou4 to set
     */
    public void setHutaijyuuryou4(Integer hutaijyuuryou4) {
        this.hutaijyuuryou4 = hutaijyuuryou4;
    }

    /**
     * 風袋重量⑤
     * @return the hutaijyuuryou5
     */
    public Integer getHutaijyuuryou5() {
        return hutaijyuuryou5;
    }

    /**
     * 風袋重量⑤
     * @param hutaijyuuryou5 the hutaijyuuryou5 to set
     */
    public void setHutaijyuuryou5(Integer hutaijyuuryou5) {
        this.hutaijyuuryou5 = hutaijyuuryou5;
    }

    /**
     * 風袋重量⑥
     * @return the hutaijyuuryou6
     */
    public Integer getHutaijyuuryou6() {
        return hutaijyuuryou6;
    }

    /**
     * 風袋重量⑥
     * @param hutaijyuuryou6 the hutaijyuuryou6 to set
     */
    public void setHutaijyuuryou6(Integer hutaijyuuryou6) {
        this.hutaijyuuryou6 = hutaijyuuryou6;
    }

    /**
     * 保管容器準備_担当者
     * @return the hokanyoukijyunbitantousya
     */
    public String getHokanyoukijyunbitantousya() {
        return hokanyoukijyunbitantousya;
    }

    /**
     * 保管容器準備_担当者
     * @param hokanyoukijyunbitantousya the hokanyoukijyunbitantousya to set
     */
    public void setHokanyoukijyunbitantousya(String hokanyoukijyunbitantousya) {
        this.hokanyoukijyunbitantousya = hokanyoukijyunbitantousya;
    }

    /**
     * ﾌｨﾙﾀｰ連結
     * @return the filterrenketsu
     */
    public String getFilterrenketsu() {
        return filterrenketsu;
    }

    /**
     * ﾌｨﾙﾀｰ連結
     * @param filterrenketsu the filterrenketsu to set
     */
    public void setFilterrenketsu(String filterrenketsu) {
        this.filterrenketsu = filterrenketsu;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_1次ﾌｨﾙﾀｰ品名
     * @return the filtertoritsuke1filterhinmei
     */
    public String getFiltertoritsuke1filterhinmei() {
        return filtertoritsuke1filterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_1次ﾌｨﾙﾀｰ品名
     * @param filtertoritsuke1filterhinmei the filtertoritsuke1filterhinmei to set
     */
    public void setFiltertoritsuke1filterhinmei(String filtertoritsuke1filterhinmei) {
        this.filtertoritsuke1filterhinmei = filtertoritsuke1filterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_LotNo1
     * @return the filtertoritsukefilterlotno1
     */
    public String getFiltertoritsukefilterlotno1() {
        return filtertoritsukefilterlotno1;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_LotNo1
     * @param filtertoritsukefilterlotno1 the filtertoritsukefilterlotno1 to set
     */
    public void setFiltertoritsukefilterlotno1(String filtertoritsukefilterlotno1) {
        this.filtertoritsukefilterlotno1 = filtertoritsukefilterlotno1;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_取り付け本数1
     * @return the filtertoritsuketoritsukehonsuu1
     */
    public Integer getFiltertoritsuketoritsukehonsuu1() {
        return filtertoritsuketoritsukehonsuu1;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_取り付け本数1
     * @param filtertoritsuketoritsukehonsuu1 the filtertoritsuketoritsukehonsuu1 to set
     */
    public void setFiltertoritsuketoritsukehonsuu1(Integer filtertoritsuketoritsukehonsuu1) {
        this.filtertoritsuketoritsukehonsuu1 = filtertoritsuketoritsukehonsuu1;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_2次ﾌｨﾙﾀｰ品名
     * @return the filtertoritsuke2filterhinmei
     */
    public String getFiltertoritsuke2filterhinmei() {
        return filtertoritsuke2filterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_2次ﾌｨﾙﾀｰ品名
     * @param filtertoritsuke2filterhinmei the filtertoritsuke2filterhinmei to set
     */
    public void setFiltertoritsuke2filterhinmei(String filtertoritsuke2filterhinmei) {
        this.filtertoritsuke2filterhinmei = filtertoritsuke2filterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_LotNo2
     * @return the filtertoritsukefilterlotno2
     */
    public String getFiltertoritsukefilterlotno2() {
        return filtertoritsukefilterlotno2;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_LotNo2
     * @param filtertoritsukefilterlotno2 the filtertoritsukefilterlotno2 to set
     */
    public void setFiltertoritsukefilterlotno2(String filtertoritsukefilterlotno2) {
        this.filtertoritsukefilterlotno2 = filtertoritsukefilterlotno2;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_取り付け本数2
     * @return the filtertoritsuketoritsukehonsuu2
     */
    public Integer getFiltertoritsuketoritsukehonsuu2() {
        return filtertoritsuketoritsukehonsuu2;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_取り付け本数2
     * @param filtertoritsuketoritsukehonsuu2 the filtertoritsuketoritsukehonsuu2 to set
     */
    public void setFiltertoritsuketoritsukehonsuu2(Integer filtertoritsuketoritsukehonsuu2) {
        this.filtertoritsuketoritsukehonsuu2 = filtertoritsuketoritsukehonsuu2;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_担当者
     * @return the filtertoritsuketantousya
     */
    public String getFiltertoritsuketantousya() {
        return filtertoritsuketantousya;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_担当者
     * @param filtertoritsuketantousya the filtertoritsuketantousya to set
     */
    public void setFiltertoritsuketantousya(String filtertoritsuketantousya) {
        this.filtertoritsuketantousya = filtertoritsuketantousya;
    }

    /**
     * 圧送ﾀﾝｸNo
     * @return the assoutankno
     */
    public Integer getAssoutankno() {
        return assoutankno;
    }

    /**
     * 圧送ﾀﾝｸNo
     * @param assoutankno the assoutankno to set
     */
    public void setAssoutankno(Integer assoutankno) {
        this.assoutankno = assoutankno;
    }

    /**
     * F/P開始日時
     * @return the fpkaishinichiji
     */
    public Timestamp getFpkaishinichiji() {
        return fpkaishinichiji;
    }

    /**
     * F/P開始日時
     * @param fpkaishinichiji the fpkaishinichiji to set
     */
    public void setFpkaishinichiji(Timestamp fpkaishinichiji) {
        this.fpkaishinichiji = fpkaishinichiji;
    }

    /**
     * 圧送ﾚｷﾞｭﾚｰﾀｰNo
     * @return the assouregulatorno
     */
    public String getAssouregulatorno() {
        return assouregulatorno;
    }

    /**
     * 圧送ﾚｷﾞｭﾚｰﾀｰNo
     * @param assouregulatorno the assouregulatorno to set
     */
    public void setAssouregulatorno(String assouregulatorno) {
        this.assouregulatorno = assouregulatorno;
    }

    /**
     * 圧送圧力規格
     * @return the assouatsuryokukikaku
     */
    public String getAssouatsuryokukikaku() {
        return assouatsuryokukikaku;
    }

    /**
     * 圧送圧力規格
     * @param assouatsuryokukikaku the assouatsuryokukikaku to set
     */
    public void setAssouatsuryokukikaku(String assouatsuryokukikaku) {
        this.assouatsuryokukikaku = assouatsuryokukikaku;
    }

    /**
     * 圧送圧力
     * @return the assouatsuryoku
     */
    public BigDecimal getAssouatsuryoku() {
        return assouatsuryoku;
    }

    /**
     * 圧送圧力
     * @param assouatsuryoku the assouatsuryoku to set
     */
    public void setAssouatsuryoku(BigDecimal assouatsuryoku) {
        this.assouatsuryoku = assouatsuryoku;
    }

    /**
     * ﾌｨﾙﾀｰﾊﾟｽ開始_担当者
     * @return the filterpasskaishitantousya
     */
    public String getFilterpasskaishitantousya() {
        return filterpasskaishitantousya;
    }

    /**
     * ﾌｨﾙﾀｰﾊﾟｽ開始_担当者
     * @param filterpasskaishitantousya the filterpasskaishitantousya to set
     */
    public void setFilterpasskaishitantousya(String filterpasskaishitantousya) {
        this.filterpasskaishitantousya = filterpasskaishitantousya;
    }

    /**
     * 保存用ｻﾝﾌﾟﾙ回収
     * @return the hozonyousamplekaisyuu
     */
    public String getHozonyousamplekaisyuu() {
        return hozonyousamplekaisyuu;
    }

    /**
     * 保存用ｻﾝﾌﾟﾙ回収
     * @param hozonyousamplekaisyuu the hozonyousamplekaisyuu to set
     */
    public void setHozonyousamplekaisyuu(String hozonyousamplekaisyuu) {
        this.hozonyousamplekaisyuu = hozonyousamplekaisyuu;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_F/P停止日時
     * @return the filterkoukan1fpteishinichiji
     */
    public Timestamp getFilterkoukan1fpteishinichiji() {
        return filterkoukan1fpteishinichiji;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_F/P停止日時
     * @param filterkoukan1fpteishinichiji the filterkoukan1fpteishinichiji to set
     */
    public void setFilterkoukan1fpteishinichiji(Timestamp filterkoukan1fpteishinichiji) {
        this.filterkoukan1fpteishinichiji = filterkoukan1fpteishinichiji;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_1次ﾌｨﾙﾀｰ品名
     * @return the filterkoukan11filterhinmei
     */
    public String getFilterkoukan11filterhinmei() {
        return filterkoukan11filterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_1次ﾌｨﾙﾀｰ品名
     * @param filterkoukan11filterhinmei the filterkoukan11filterhinmei to set
     */
    public void setFilterkoukan11filterhinmei(String filterkoukan11filterhinmei) {
        this.filterkoukan11filterhinmei = filterkoukan11filterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_LotNo1
     * @return the filterkoukan1lotno1
     */
    public String getFilterkoukan1lotno1() {
        return filterkoukan1lotno1;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_LotNo1
     * @param filterkoukan1lotno1 the filterkoukan1lotno1 to set
     */
    public void setFilterkoukan1lotno1(String filterkoukan1lotno1) {
        this.filterkoukan1lotno1 = filterkoukan1lotno1;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_取り付け本数1
     * @return the filterkoukan1toritsukehonnsuu1
     */
    public Integer getFilterkoukan1toritsukehonnsuu1() {
        return filterkoukan1toritsukehonnsuu1;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_取り付け本数1
     * @param filterkoukan1toritsukehonnsuu1 the filterkoukan1toritsukehonnsuu1 to set
     */
    public void setFilterkoukan1toritsukehonnsuu1(Integer filterkoukan1toritsukehonnsuu1) {
        this.filterkoukan1toritsukehonnsuu1 = filterkoukan1toritsukehonnsuu1;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_2次ﾌｨﾙﾀｰ品名
     * @return the filterkoukan12filterhinmei
     */
    public String getFilterkoukan12filterhinmei() {
        return filterkoukan12filterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_2次ﾌｨﾙﾀｰ品名
     * @param filterkoukan12filterhinmei the filterkoukan12filterhinmei to set
     */
    public void setFilterkoukan12filterhinmei(String filterkoukan12filterhinmei) {
        this.filterkoukan12filterhinmei = filterkoukan12filterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_LotNo2
     * @return the filterkoukan1lotno2
     */
    public String getFilterkoukan1lotno2() {
        return filterkoukan1lotno2;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_LotNo2
     * @param filterkoukan1lotno2 the filterkoukan1lotno2 to set
     */
    public void setFilterkoukan1lotno2(String filterkoukan1lotno2) {
        this.filterkoukan1lotno2 = filterkoukan1lotno2;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_取り付け本数2
     * @return the filterkoukan1toritsukehonnsuu2
     */
    public Integer getFilterkoukan1toritsukehonnsuu2() {
        return filterkoukan1toritsukehonnsuu2;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_取り付け本数2
     * @param filterkoukan1toritsukehonnsuu2 the filterkoukan1toritsukehonnsuu2 to set
     */
    public void setFilterkoukan1toritsukehonnsuu2(Integer filterkoukan1toritsukehonnsuu2) {
        this.filterkoukan1toritsukehonnsuu2 = filterkoukan1toritsukehonnsuu2;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_3次ﾌｨﾙﾀｰ品名
     * @return the filterkoukan13filterhinmei
     */
    public String getFilterkoukan13filterhinmei() {
        return filterkoukan13filterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_3次ﾌｨﾙﾀｰ品名
     * @param filterkoukan13filterhinmei the filterkoukan13filterhinmei to set
     */
    public void setFilterkoukan13filterhinmei(String filterkoukan13filterhinmei) {
        this.filterkoukan13filterhinmei = filterkoukan13filterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_LotNo3
     * @return the filterkoukan1lotno3
     */
    public String getFilterkoukan1lotno3() {
        return filterkoukan1lotno3;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_LotNo3
     * @param filterkoukan1lotno3 the filterkoukan1lotno3 to set
     */
    public void setFilterkoukan1lotno3(String filterkoukan1lotno3) {
        this.filterkoukan1lotno3 = filterkoukan1lotno3;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_取り付け本数3
     * @return the filterkoukan1toritsukehonnsuu3
     */
    public Integer getFilterkoukan1toritsukehonnsuu3() {
        return filterkoukan1toritsukehonnsuu3;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_取り付け本数3
     * @param filterkoukan1toritsukehonnsuu3 the filterkoukan1toritsukehonnsuu3 to set
     */
    public void setFilterkoukan1toritsukehonnsuu3(Integer filterkoukan1toritsukehonnsuu3) {
        this.filterkoukan1toritsukehonnsuu3 = filterkoukan1toritsukehonnsuu3;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_F/P再開日時
     * @return the filterkoukan1fpsaikainichiji
     */
    public Timestamp getFilterkoukan1fpsaikainichiji() {
        return filterkoukan1fpsaikainichiji;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_F/P再開日時
     * @param filterkoukan1fpsaikainichiji the filterkoukan1fpsaikainichiji to set
     */
    public void setFilterkoukan1fpsaikainichiji(Timestamp filterkoukan1fpsaikainichiji) {
        this.filterkoukan1fpsaikainichiji = filterkoukan1fpsaikainichiji;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_担当者
     * @return the filterkoukan1tantousya
     */
    public String getFilterkoukan1tantousya() {
        return filterkoukan1tantousya;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_担当者
     * @param filterkoukan1tantousya the filterkoukan1tantousya to set
     */
    public void setFilterkoukan1tantousya(String filterkoukan1tantousya) {
        this.filterkoukan1tantousya = filterkoukan1tantousya;
    }

    /**
     * F/P終了日時
     * @return the fpsyuryounichiji
     */
    public Timestamp getFpsyuryounichiji() {
        return fpsyuryounichiji;
    }

    /**
     * F/P終了日時
     * @param fpsyuryounichiji the fpsyuryounichiji to set
     */
    public void setFpsyuryounichiji(Timestamp fpsyuryounichiji) {
        this.fpsyuryounichiji = fpsyuryounichiji;
    }

    /**
     * F/P時間
     * @return the fpzikan
     */
    public String getFpzikan() {
        return fpzikan;
    }

    /**
     * F/P時間
     * @param fpzikan the fpzikan to set
     */
    public void setFpzikan(String fpzikan) {
        this.fpzikan = fpzikan;
    }

    /**
     * ﾌｨﾙﾀｰﾊﾟｽ終了_担当者
     * @return the filterpasssyuuryoutantousya
     */
    public String getFilterpasssyuuryoutantousya() {
        return filterpasssyuuryoutantousya;
    }

    /**
     * ﾌｨﾙﾀｰﾊﾟｽ終了_担当者
     * @param filterpasssyuuryoutantousya the filterpasssyuuryoutantousya to set
     */
    public void setFilterpasssyuuryoutantousya(String filterpasssyuuryoutantousya) {
        this.filterpasssyuuryoutantousya = filterpasssyuuryoutantousya;
    }

    /**
     * 総重量1
     * @return the soujyuryou1
     */
    public Integer getSoujyuryou1() {
        return soujyuryou1;
    }

    /**
     * 総重量1
     * @param soujyuryou1 the soujyuryou1 to set
     */
    public void setSoujyuryou1(Integer soujyuryou1) {
        this.soujyuryou1 = soujyuryou1;
    }

    /**
     * 総重量2
     * @return the soujyuryou2
     */
    public Integer getSoujyuryou2() {
        return soujyuryou2;
    }

    /**
     * 総重量2
     * @param soujyuryou2 the soujyuryou2 to set
     */
    public void setSoujyuryou2(Integer soujyuryou2) {
        this.soujyuryou2 = soujyuryou2;
    }

    /**
     * 総重量3
     * @return the soujyuryou3
     */
    public Integer getSoujyuryou3() {
        return soujyuryou3;
    }

    /**
     * 総重量3
     * @param soujyuryou3 the soujyuryou3 to set
     */
    public void setSoujyuryou3(Integer soujyuryou3) {
        this.soujyuryou3 = soujyuryou3;
    }

    /**
     * 総重量4
     * @return the soujyuryou4
     */
    public Integer getSoujyuryou4() {
        return soujyuryou4;
    }

    /**
     * 総重量4
     * @param soujyuryou4 the soujyuryou4 to set
     */
    public void setSoujyuryou4(Integer soujyuryou4) {
        this.soujyuryou4 = soujyuryou4;
    }

    /**
     * 総重量5
     * @return the soujyuryou5
     */
    public Integer getSoujyuryou5() {
        return soujyuryou5;
    }

    /**
     * 総重量5
     * @param soujyuryou5 the soujyuryou5 to set
     */
    public void setSoujyuryou5(Integer soujyuryou5) {
        this.soujyuryou5 = soujyuryou5;
    }

    /**
     * 総重量6
     * @return the soujyuryou6
     */
    public Integer getSoujyuryou6() {
        return soujyuryou6;
    }

    /**
     * 総重量6
     * @param soujyuryou6 the soujyuryou6 to set
     */
    public void setSoujyuryou6(Integer soujyuryou6) {
        this.soujyuryou6 = soujyuryou6;
    }

    /**
     * 正味重量1
     * @return the syoumijyuuryou1
     */
    public Integer getSyoumijyuuryou1() {
        return syoumijyuuryou1;
    }

    /**
     * 正味重量1
     * @param syoumijyuuryou1 the syoumijyuuryou1 to set
     */
    public void setSyoumijyuuryou1(Integer syoumijyuuryou1) {
        this.syoumijyuuryou1 = syoumijyuuryou1;
    }

    /**
     * 正味重量2
     * @return the syoumijyuuryou2
     */
    public Integer getSyoumijyuuryou2() {
        return syoumijyuuryou2;
    }

    /**
     * 正味重量2
     * @param syoumijyuuryou2 the syoumijyuuryou2 to set
     */
    public void setSyoumijyuuryou2(Integer syoumijyuuryou2) {
        this.syoumijyuuryou2 = syoumijyuuryou2;
    }

    /**
     * 正味重量3
     * @return the syoumijyuuryou3
     */
    public Integer getSyoumijyuuryou3() {
        return syoumijyuuryou3;
    }

    /**
     * 正味重量3
     * @param syoumijyuuryou3 the syoumijyuuryou3 to set
     */
    public void setSyoumijyuuryou3(Integer syoumijyuuryou3) {
        this.syoumijyuuryou3 = syoumijyuuryou3;
    }

    /**
     * 正味重量4
     * @return the syoumijyuuryou4
     */
    public Integer getSyoumijyuuryou4() {
        return syoumijyuuryou4;
    }

    /**
     * 正味重量4
     * @param syoumijyuuryou4 the syoumijyuuryou4 to set
     */
    public void setSyoumijyuuryou4(Integer syoumijyuuryou4) {
        this.syoumijyuuryou4 = syoumijyuuryou4;
    }

    /**
     * 正味重量5
     * @return the syoumijyuuryou5
     */
    public Integer getSyoumijyuuryou5() {
        return syoumijyuuryou5;
    }

    /**
     * 正味重量5
     * @param syoumijyuuryou5 the syoumijyuuryou5 to set
     */
    public void setSyoumijyuuryou5(Integer syoumijyuuryou5) {
        this.syoumijyuuryou5 = syoumijyuuryou5;
    }

    /**
     * 正味重量6
     * @return the syoumijyuuryou6
     */
    public Integer getSyoumijyuuryou6() {
        return syoumijyuuryou6;
    }

    /**
     * 正味重量6
     * @param syoumijyuuryou6 the syoumijyuuryou6 to set
     */
    public void setSyoumijyuuryou6(Integer syoumijyuuryou6) {
        this.syoumijyuuryou6 = syoumijyuuryou6;
    }

    /**
     * ｽﾘｯﾌﾟ重量合計
     * @return the slipjyuuryougoukei
     */
    public Integer getSlipjyuuryougoukei() {
        return slipjyuuryougoukei;
    }

    /**
     * ｽﾘｯﾌﾟ重量合計
     * @param slipjyuuryougoukei the slipjyuuryougoukei to set
     */
    public void setSlipjyuuryougoukei(Integer slipjyuuryougoukei) {
        this.slipjyuuryougoukei = slipjyuuryougoukei;
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