/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/10/26<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SR_BINDER_POWDER(ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰ粉秤量)のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/10/26
 */
public class SrBinderPowder {
    /**
     * 工場ｺｰﾄﾞ
     */    private String kojyo;

    /**
     * ﾛｯﾄNo
     */    private String lotno;

    /**
     * 枝番
     */    private String edaban;

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液品名
     */    private String binderyouekihinmei;

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液LotNo
     */    private String binderyouekilotno;

    /**
     * ﾛｯﾄ区分
     */    private String lotkubun;

    /**
     * 秤量号機
     */    private String goki;

    /**
     * ﾃﾞｨｽﾊﾟ号機
     */    private String dispagouki;

    /**
     * 風袋重量
     */    private Integer fuutaijyuuryou;

    /**
     * 秤量開始日時
     */    private Timestamp hyouryoukaisinichiji;

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_材料品名
     */    private String binderjyusi1_zairyouhinmei;

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_調合量規格
     */    private String binderjyusi1_tyougouryoukikaku;

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_部材在庫No1
     */    private String binderjyusi1_buzaizaikono1;

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_調合量1
     */    private Integer binderjyusi1_tyougouryou1;

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_部材在庫No2
     */    private String binderjyusi1_buzaizaikono2;

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_調合量2
     */    private Integer binderjyusi1_tyougouryou2;

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_材料品名
     */    private String binderjyusi2_zairyouhinmei;

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_調合量規格
     */    private String binderjyusi2_tyougouryoukikaku;

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_部材在庫No1
     */    private String binderjyusi2_buzaizaikono1;

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_調合量1
     */    private Integer binderjyusi2_tyougouryou1;

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_部材在庫No2
     */    private String binderjyusi2_buzaizaikono2;

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_調合量2
     */    private Integer binderjyusi2_tyougouryou2;

    /**
     * 溶剤①_材料品名
     */    private String youzai1_zairyouhinmei;

    /**
     * 溶剤①_調合量規格
     */    private String youzai1_tyougouryoukikaku;

    /**
     * 溶剤①_部材在庫No1
     */    private String youzai1_buzaizaikono1;

    /**
     * 溶剤①_調合量1
     */    private Integer youzai1_tyougouryou1;

    /**
     * 溶剤①_部材在庫No2
     */    private String youzai1_buzaizaikono2;

    /**
     * 溶剤①_調合量2
     */    private Integer youzai1_tyougouryou2;

    /**
     * 溶剤②_材料品名
     */    private String youzai2_zairyouhinmei;

    /**
     * 溶剤②_調合量規格
     */    private String youzai2_tyougouryoukikaku;

    /**
     * 溶剤②_部材在庫No1
     */    private String youzai2_buzaizaikono1;

    /**
     * 溶剤②_調合量1
     */    private Integer youzai2_tyougouryou1;

    /**
     * 溶剤②_部材在庫No2
     */    private String youzai2_buzaizaikono2;

    /**
     * 溶剤②_調合量2
     */    private Integer youzai2_tyougouryou2;

    /**
     * 秤量終了日時
     */    private Timestamp hyouryousyuuryounichiji;

    /**
     * 総重量
     */    private Integer soujyuuryou;

    /**
     * 正味重量
     */    private Integer syoumijyuuryou;

    /**
     * 担当者
     */    private String tantousya;

    /**
     * 確認者
     */    private String kakuninsya;

    /**
     * 備考1
     */    private String bikou1;

    /**
     * 備考2
     */    private String bikou2;

    /**
     * 登録日時
     */    private Timestamp torokunichiji;

    /**
     * 更新日時
     */    private Timestamp kosinnichiji;

    /**
     * revision
     */    private Integer revision;

    /**
     * 削除ﾌﾗｸﾞ
     */    private Integer deleteflag;

    /**
     * 工場ｺｰﾄﾞ
     * @return the kojyo
     */
    public String getKojyo() {
        return kojyo;
    }

    /**
     * 工場ｺｰﾄﾞ
     * @param kojyo the kojyo to set
     */
    public void setKojyo(String kojyo) {
        this.kojyo = kojyo;
    }

    /**
     * ﾛｯﾄNo
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * ﾛｯﾄNo
     * @param lotno the lotno to set
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * 枝番
     * @return the edaban
     */
    public String getEdaban() {
        return edaban;
    }

    /**
     * 枝番
     * @param edaban the edaban to set
     */
    public void setEdaban(String edaban) {
        this.edaban = edaban;
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
     * 秤量号機
     * @return the goki
     */
    public String getGoki() {
        return goki;
    }

    /**
     * 秤量号機
     * @param goki the goki to set
     */
    public void setGoki(String goki) {
        this.goki = goki;
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
     * 秤量開始日時
     * @return the hyouryoukaisinichiji
     */
    public Timestamp getHyouryoukaisinichiji() {
        return hyouryoukaisinichiji;
    }

    /**
     * 秤量開始日時
     * @param hyouryoukaisinichiji the hyouryoukaisinichiji to set
     */
    public void setHyouryoukaisinichiji(Timestamp hyouryoukaisinichiji) {
        this.hyouryoukaisinichiji = hyouryoukaisinichiji;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_材料品名
     * @return the binderjyusi1_zairyouhinmei
     */
    public String getBinderjyusi1_zairyouhinmei() {
        return binderjyusi1_zairyouhinmei;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_材料品名
     * @param binderjyusi1_zairyouhinmei the binderjyusi1_zairyouhinmei to set
     */
    public void setBinderjyusi1_zairyouhinmei(String binderjyusi1_zairyouhinmei) {
        this.binderjyusi1_zairyouhinmei = binderjyusi1_zairyouhinmei;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_調合量規格
     * @return the binderjyusi1_tyougouryoukikaku
     */
    public String getBinderjyusi1_tyougouryoukikaku() {
        return binderjyusi1_tyougouryoukikaku;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_調合量規格
     * @param binderjyusi1_tyougouryoukikaku the binderjyusi1_tyougouryoukikaku to set
     */
    public void setBinderjyusi1_tyougouryoukikaku(String binderjyusi1_tyougouryoukikaku) {
        this.binderjyusi1_tyougouryoukikaku = binderjyusi1_tyougouryoukikaku;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_部材在庫No1
     * @return the binderjyusi1_buzaizaikono1
     */
    public String getBinderjyusi1_buzaizaikono1() {
        return binderjyusi1_buzaizaikono1;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_部材在庫No1
     * @param binderjyusi1_buzaizaikono1 the binderjyusi1_buzaizaikono1 to set
     */
    public void setBinderjyusi1_buzaizaikono1(String binderjyusi1_buzaizaikono1) {
        this.binderjyusi1_buzaizaikono1 = binderjyusi1_buzaizaikono1;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_調合量1
     * @return the binderjyusi1_tyougouryou1
     */
    public Integer getBinderjyusi1_tyougouryou1() {
        return binderjyusi1_tyougouryou1;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_調合量1
     * @param binderjyusi1_tyougouryou1 the binderjyusi1_tyougouryou1 to set
     */
    public void setBinderjyusi1_tyougouryou1(Integer binderjyusi1_tyougouryou1) {
        this.binderjyusi1_tyougouryou1 = binderjyusi1_tyougouryou1;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_部材在庫No2
     * @return the binderjyusi1_buzaizaikono2
     */
    public String getBinderjyusi1_buzaizaikono2() {
        return binderjyusi1_buzaizaikono2;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_部材在庫No2
     * @param binderjyusi1_buzaizaikono2 the binderjyusi1_buzaizaikono2 to set
     */
    public void setBinderjyusi1_buzaizaikono2(String binderjyusi1_buzaizaikono2) {
        this.binderjyusi1_buzaizaikono2 = binderjyusi1_buzaizaikono2;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_調合量2
     * @return the binderjyusi1_tyougouryou2
     */
    public Integer getBinderjyusi1_tyougouryou2() {
        return binderjyusi1_tyougouryou2;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_調合量2
     * @param binderjyusi1_tyougouryou2 the binderjyusi1_tyougouryou2 to set
     */
    public void setBinderjyusi1_tyougouryou2(Integer binderjyusi1_tyougouryou2) {
        this.binderjyusi1_tyougouryou2 = binderjyusi1_tyougouryou2;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_材料品名
     * @return the binderjyusi2_zairyouhinmei
     */
    public String getBinderjyusi2_zairyouhinmei() {
        return binderjyusi2_zairyouhinmei;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_材料品名
     * @param binderjyusi2_zairyouhinmei the binderjyusi2_zairyouhinmei to set
     */
    public void setBinderjyusi2_zairyouhinmei(String binderjyusi2_zairyouhinmei) {
        this.binderjyusi2_zairyouhinmei = binderjyusi2_zairyouhinmei;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_調合量規格
     * @return the binderjyusi2_tyougouryoukikaku
     */
    public String getBinderjyusi2_tyougouryoukikaku() {
        return binderjyusi2_tyougouryoukikaku;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_調合量規格
     * @param binderjyusi2_tyougouryoukikaku the binderjyusi2_tyougouryoukikaku to set
     */
    public void setBinderjyusi2_tyougouryoukikaku(String binderjyusi2_tyougouryoukikaku) {
        this.binderjyusi2_tyougouryoukikaku = binderjyusi2_tyougouryoukikaku;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_部材在庫No1
     * @return the binderjyusi2_buzaizaikono1
     */
    public String getBinderjyusi2_buzaizaikono1() {
        return binderjyusi2_buzaizaikono1;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_部材在庫No1
     * @param binderjyusi2_buzaizaikono1 the binderjyusi2_buzaizaikono1 to set
     */
    public void setBinderjyusi2_buzaizaikono1(String binderjyusi2_buzaizaikono1) {
        this.binderjyusi2_buzaizaikono1 = binderjyusi2_buzaizaikono1;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_調合量1
     * @return the binderjyusi2_tyougouryou1
     */
    public Integer getBinderjyusi2_tyougouryou1() {
        return binderjyusi2_tyougouryou1;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_調合量1
     * @param binderjyusi2_tyougouryou1 the binderjyusi2_tyougouryou1 to set
     */
    public void setBinderjyusi2_tyougouryou1(Integer binderjyusi2_tyougouryou1) {
        this.binderjyusi2_tyougouryou1 = binderjyusi2_tyougouryou1;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_部材在庫No2
     * @return the binderjyusi2_buzaizaikono2
     */
    public String getBinderjyusi2_buzaizaikono2() {
        return binderjyusi2_buzaizaikono2;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_部材在庫No2
     * @param binderjyusi2_buzaizaikono2 the binderjyusi2_buzaizaikono2 to set
     */
    public void setBinderjyusi2_buzaizaikono2(String binderjyusi2_buzaizaikono2) {
        this.binderjyusi2_buzaizaikono2 = binderjyusi2_buzaizaikono2;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_調合量2
     * @return the binderjyusi2_tyougouryou2
     */
    public Integer getBinderjyusi2_tyougouryou2() {
        return binderjyusi2_tyougouryou2;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_調合量2
     * @param binderjyusi2_tyougouryou2 the binderjyusi2_tyougouryou2 to set
     */
    public void setBinderjyusi2_tyougouryou2(Integer binderjyusi2_tyougouryou2) {
        this.binderjyusi2_tyougouryou2 = binderjyusi2_tyougouryou2;
    }

    /**
     * 溶剤①_材料品名
     * @return the youzai1_zairyouhinmei
     */
    public String getYouzai1_zairyouhinmei() {
        return youzai1_zairyouhinmei;
    }

    /**
     * 溶剤①_材料品名
     * @param youzai1_zairyouhinmei the youzai1_zairyouhinmei to set
     */
    public void setYouzai1_zairyouhinmei(String youzai1_zairyouhinmei) {
        this.youzai1_zairyouhinmei = youzai1_zairyouhinmei;
    }

    /**
     * 溶剤①_調合量規格
     * @return the youzai1_tyougouryoukikaku
     */
    public String getYouzai1_tyougouryoukikaku() {
        return youzai1_tyougouryoukikaku;
    }

    /**
     * 溶剤①_調合量規格
     * @param youzai1_tyougouryoukikaku the youzai1_tyougouryoukikaku to set
     */
    public void setYouzai1_tyougouryoukikaku(String youzai1_tyougouryoukikaku) {
        this.youzai1_tyougouryoukikaku = youzai1_tyougouryoukikaku;
    }

    /**
     * 溶剤①_部材在庫No1
     * @return the youzai1_buzaizaikono1
     */
    public String getYouzai1_buzaizaikono1() {
        return youzai1_buzaizaikono1;
    }

    /**
     * 溶剤①_部材在庫No1
     * @param youzai1_buzaizaikono1 the youzai1_buzaizaikono1 to set
     */
    public void setYouzai1_buzaizaikono1(String youzai1_buzaizaikono1) {
        this.youzai1_buzaizaikono1 = youzai1_buzaizaikono1;
    }

    /**
     * 溶剤①_調合量1
     * @return the youzai1_tyougouryou1
     */
    public Integer getYouzai1_tyougouryou1() {
        return youzai1_tyougouryou1;
    }

    /**
     * 溶剤①_調合量1
     * @param youzai1_tyougouryou1 the youzai1_tyougouryou1 to set
     */
    public void setYouzai1_tyougouryou1(Integer youzai1_tyougouryou1) {
        this.youzai1_tyougouryou1 = youzai1_tyougouryou1;
    }

    /**
     * 溶剤①_部材在庫No2
     * @return the youzai1_buzaizaikono2
     */
    public String getYouzai1_buzaizaikono2() {
        return youzai1_buzaizaikono2;
    }

    /**
     * 溶剤①_部材在庫No2
     * @param youzai1_buzaizaikono2 the youzai1_buzaizaikono2 to set
     */
    public void setYouzai1_buzaizaikono2(String youzai1_buzaizaikono2) {
        this.youzai1_buzaizaikono2 = youzai1_buzaizaikono2;
    }

    /**
     * 溶剤①_調合量2
     * @return the youzai1_tyougouryou2
     */
    public Integer getYouzai1_tyougouryou2() {
        return youzai1_tyougouryou2;
    }

    /**
     * 溶剤①_調合量2
     * @param youzai1_tyougouryou2 the youzai1_tyougouryou2 to set
     */
    public void setYouzai1_tyougouryou2(Integer youzai1_tyougouryou2) {
        this.youzai1_tyougouryou2 = youzai1_tyougouryou2;
    }

    /**
     * 溶剤②_材料品名
     * @return the youzai2_zairyouhinmei
     */
    public String getYouzai2_zairyouhinmei() {
        return youzai2_zairyouhinmei;
    }

    /**
     * 溶剤②_材料品名
     * @param youzai2_zairyouhinmei the youzai2_zairyouhinmei to set
     */
    public void setYouzai2_zairyouhinmei(String youzai2_zairyouhinmei) {
        this.youzai2_zairyouhinmei = youzai2_zairyouhinmei;
    }

    /**
     * 溶剤②_調合量規格
     * @return the youzai2_tyougouryoukikaku
     */
    public String getYouzai2_tyougouryoukikaku() {
        return youzai2_tyougouryoukikaku;
    }

    /**
     * 溶剤②_調合量規格
     * @param youzai2_tyougouryoukikaku the youzai2_tyougouryoukikaku to set
     */
    public void setYouzai2_tyougouryoukikaku(String youzai2_tyougouryoukikaku) {
        this.youzai2_tyougouryoukikaku = youzai2_tyougouryoukikaku;
    }

    /**
     * 溶剤②_部材在庫No1
     * @return the youzai2_buzaizaikono1
     */
    public String getYouzai2_buzaizaikono1() {
        return youzai2_buzaizaikono1;
    }

    /**
     * 溶剤②_部材在庫No1
     * @param youzai2_buzaizaikono1 the youzai2_buzaizaikono1 to set
     */
    public void setYouzai2_buzaizaikono1(String youzai2_buzaizaikono1) {
        this.youzai2_buzaizaikono1 = youzai2_buzaizaikono1;
    }

    /**
     * 溶剤②_調合量1
     * @return the youzai2_tyougouryou1
     */
    public Integer getYouzai2_tyougouryou1() {
        return youzai2_tyougouryou1;
    }

    /**
     * 溶剤②_調合量1
     * @param youzai2_tyougouryou1 the youzai2_tyougouryou1 to set
     */
    public void setYouzai2_tyougouryou1(Integer youzai2_tyougouryou1) {
        this.youzai2_tyougouryou1 = youzai2_tyougouryou1;
    }

    /**
     * 溶剤②_部材在庫No2
     * @return the youzai2_buzaizaikono2
     */
    public String getYouzai2_buzaizaikono2() {
        return youzai2_buzaizaikono2;
    }

    /**
     * 溶剤②_部材在庫No2
     * @param youzai2_buzaizaikono2 the youzai2_buzaizaikono2 to set
     */
    public void setYouzai2_buzaizaikono2(String youzai2_buzaizaikono2) {
        this.youzai2_buzaizaikono2 = youzai2_buzaizaikono2;
    }

    /**
     * 溶剤②_調合量2
     * @return the youzai2_tyougouryou2
     */
    public Integer getYouzai2_tyougouryou2() {
        return youzai2_tyougouryou2;
    }

    /**
     * 溶剤②_調合量2
     * @param youzai2_tyougouryou2 the youzai2_tyougouryou2 to set
     */
    public void setYouzai2_tyougouryou2(Integer youzai2_tyougouryou2) {
        this.youzai2_tyougouryou2 = youzai2_tyougouryou2;
    }

    /**
     * 秤量終了日時
     * @return the hyouryousyuuryounichiji
     */
    public Timestamp getHyouryousyuuryounichiji() {
        return hyouryousyuuryounichiji;
    }

    /**
     * 秤量終了日時
     * @param hyouryousyuuryounichiji the hyouryousyuuryounichiji to set
     */
    public void setHyouryousyuuryounichiji(Timestamp hyouryousyuuryounichiji) {
        this.hyouryousyuuryounichiji = hyouryousyuuryounichiji;
    }

    /**
     * 総重量
     * @return the soujyuuryou
     */
    public Integer getSoujyuuryou() {
        return soujyuuryou;
    }

    /**
     * 総重量
     * @param soujyuuryou the soujyuuryou to set
     */
    public void setSoujyuuryou(Integer soujyuuryou) {
        this.soujyuuryou = soujyuuryou;
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

    /**
     * 登録日時
     * @return the torokunichiji
     */
    public Timestamp getTorokunichiji() {
        return torokunichiji;
    }

    /**
     * 登録日時
     * @param torokunichiji the torokunichiji to set
     */
    public void setTorokunichiji(Timestamp torokunichiji) {
        this.torokunichiji = torokunichiji;
    }

    /**
     * 更新日時
     * @return the kosinnichiji
     */
    public Timestamp getKosinnichiji() {
        return kosinnichiji;
    }

    /**
     * 更新日時
     * @param kosinnichiji the kosinnichiji to set
     */
    public void setKosinnichiji(Timestamp kosinnichiji) {
        this.kosinnichiji = kosinnichiji;
    }

    /**
     * revision
     * @return the revision
     */
    public Integer getRevision() {
        return revision;
    }

    /**
     * revision
     * @param revision the revision to set
     */
    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    /**
     * 削除ﾌﾗｸﾞ
     * @return the deleteflag
     */
    public Integer getDeleteflag() {
        return deleteflag;
    }

    /**
     * 削除ﾌﾗｸﾞ
     * @param deleteflag the deleteflag to set
     */
    public void setDeleteflag(Integer deleteflag) {
        this.deleteflag = deleteflag;
    }

}