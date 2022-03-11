/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/12/13<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SR_SLIP_SLIPKOKEIBUNSOKUTEI(ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定)のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/12/13
 */
public class SrSlipSlipkokeibunsokutei {
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
     * ｽﾘｯﾌﾟ品名
     */
    private String sliphinmei;

    /**
     * ｽﾘｯﾌﾟLotNo
     */
    private String sliplotno;

    /**
     * ﾛｯﾄ区分
     */
    private String lotkubun;

    /**
     * 原料記号
     */
    private String genryoukigou;

    /**
     * 秤量号機
     */
    private String goki;

    /**
     * 脱脂皿の種類
     */
    private String dassizaranosyurui;

    /**
     * ﾙﾂﾎﾞNo
     */
    private Integer rutubono;

    /**
     * ﾙﾂﾎﾞ風袋重量
     */
    private BigDecimal rutubofuutaijyuuryou;

    /**
     * 乾燥前ｽﾘｯﾌﾟ重量
     */
    private BigDecimal kansoumaeslipjyuuryou;

    /**
     * 乾燥機①
     */
    private String kansouki1;

    /**
     * 乾燥温度①
     */
    private String kansouondo1;

    /**
     * 乾燥時間①
     */
    private String kansoujikan1;

    /**
     * 乾燥開始日時①
     */
    private Timestamp kansoukaisinichiji1;

    /**
     * 乾燥終了日時①
     */
    private Timestamp kansousyuuryounichiji1;

    /**
     * 乾燥機②
     */
    private String kansouki2;

    /**
     * 乾燥温度②
     */
    private String kansouondo2;

    /**
     * 乾燥時間②
     */
    private String kansoujikan2;

    /**
     * 乾燥開始日時②
     */
    private Timestamp kansoukaisinichiji2;

    /**
     * 乾燥終了日時②
     */
    private Timestamp kansousyuuryounichiji2;

    /**
     * 乾燥後総重量
     */
    private BigDecimal kansougosoujyuuryou;

    /**
     * 乾燥後正味重量
     */
    private BigDecimal kansougosyoumijyuuryou;

    /**
     * 固形分比率
     */
    private BigDecimal kokeibunhiritu;

    /**
     * 固形分測定担当者
     */
    private String kokeibunsokuteitantousya;

    /**
     * 溶剤調整量
     */
    private Integer youzaityouseiryou;

    /**
     * ﾄﾙｴﾝ調整量
     */
    private Integer toluenetyouseiryou;

    /**
     * ｿﾙﾐｯｸｽ調整量
     */
    private Integer solmixtyouseiryou;

    /**
     * 溶剤秤量日時
     */
    private Timestamp youzaikeiryounichiji;

    /**
     * 溶剤①_材料品名
     */
    private String youzai1_zairyouhinmei;

    /**
     * 溶剤①_調合量規格
     */
    private String youzai1_tyougouryoukikaku;

    /**
     * 溶剤①_部材在庫No1
     */
    private String youzai1_buzaizaikolotno1;

    /**
     * 溶剤①_調合量1
     */
    private Integer youzai1_tyougouryou1;

    /**
     * 溶剤①_部材在庫No2
     */
    private String youzai1_buzaizaikolotno2;

    /**
     * 溶剤①_調合量2
     */
    private Integer youzai1_tyougouryou2;

    /**
     * 溶剤②_材料品名
     */
    private String youzai2_zairyouhinmei;

    /**
     * 溶剤②_調合量規格
     */
    private String youzai2_tyougouryoukikaku;

    /**
     * 溶剤②_部材在庫No1
     */
    private String youzai2_buzaizaikolotno1;

    /**
     * 溶剤②_調合量1
     */
    private Integer youzai2_tyougouryou1;

    /**
     * 溶剤②_部材在庫No2
     */
    private String youzai2_buzaizaikolotno2;

    /**
     * 溶剤②_調合量2
     */
    private Integer youzai2_tyougouryou2;

    /**
     * 担当者
     */
    private String tantousya;

    /**
     * 撹拌_撹拌ﾓｰﾄﾞ
     */
    private String kakuhan_kakuhanmode;

    /**
     * 撹拌_回転方向
     */
    private String kakuhan_kaitenhoukou;

    /**
     * 撹拌_回転数(rpm)
     */
    private String kakuhan_kaitensuu;

    /**
     * 撹拌_撹拌時間
     */
    private String kakuhan_kakuhanjikan;

    /**
     * 撹拌_撹拌開始日時
     */
    private Timestamp kakuhan_kakuhankaisinichiji;

    /**
     * 開始電流値(A)
     */
    private BigDecimal kaisidenryuuti;

    /**
     * 撹拌_撹拌終了予定日時
     */
    private Timestamp kakuhan_kakuhansyuuryouyoteinichiji;

    /**
     * 撹拌_撹拌終了日時
     */
    private Timestamp kakuhan_kakuhansyuuryounichiji;

    /**
     * 終了電流値(A)
     */
    private BigDecimal syuuryoudenryuuti;

    /**
     * 撹拌_撹拌担当者
     */
    private String kakuhan_kakuhantantousya;

    /**
     * 排出前撹拌_撹拌ﾓｰﾄﾞ
     */
    private String haisyutumaekakuhan_kakuhanmode;

    /**
     * 排出前撹拌_回転方向
     */
    private String haisyutumaekakuhan_kaitenhoukou;

    /**
     * 排出前撹拌_回転数(rpm)
     */
    private String haisyutumaekakuhan_kaitensuu;

    /**
     * 排出前撹拌_撹拌時間
     */
    private String haisyutumaekakuhan_kakuhanjikan;

    /**
     * 排出前撹拌_撹拌開始日時
     */
    private Timestamp haisyutumaekakuhan_kakuhankaisinichiji;

    /**
     * 排出前撹拌_撹拌終了予定日時
     */
    private Timestamp haisyutumaekakuhan_kakuhansyuuryouyoteinichiji;

    /**
     * 排出前撹拌_撹拌終了日時
     */
    private Timestamp haisyutumaekakuhan_kakuhansyuuryounichiji;

    /**
     * 排出前撹拌_撹拌担当者
     */
    private String haisyutumaekakuhan_kakuhantantousya;

    /**
     * 排出開始日時
     */
    private Timestamp haisyutukaisinichiji;

    /**
     * 排出終了日時
     */
    private Timestamp haisyutusyuuryounichiji;

    /**
     * 排出担当者
     */
    private String haisyututantousya;

    /**
     * 備考1
     */
    private String bikou1;

    /**
     * 備考2
     */
    private String bikou2;

    /**
     * 登録日時
     */
    private Timestamp torokunichiji;

    /**
     * 更新日時
     */
    private Timestamp kosinnichiji;

    /**
     * revision
     */
    private Integer revision;

    /**
     * 削除ﾌﾗｸﾞ
     */
    private Integer deleteflag;

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
     * 脱脂皿の種類
     * @return the dassizaranosyurui
     */
    public String getDassizaranosyurui() {
        return dassizaranosyurui;
    }

    /**
     * 脱脂皿の種類
     * @param dassizaranosyurui the dassizaranosyurui to set
     */
    public void setDassizaranosyurui(String dassizaranosyurui) {
        this.dassizaranosyurui = dassizaranosyurui;
    }

    /**
     * ﾙﾂﾎﾞNo
     * @return the rutubono
     */
    public Integer getRutubono() {
        return rutubono;
    }

    /**
     * ﾙﾂﾎﾞNo
     * @param rutubono the rutubono to set
     */
    public void setRutubono(Integer rutubono) {
        this.rutubono = rutubono;
    }

    /**
     * ﾙﾂﾎﾞ風袋重量
     * @return the rutubofuutaijyuuryou
     */
    public BigDecimal getRutubofuutaijyuuryou() {
        return rutubofuutaijyuuryou;
    }

    /**
     * ﾙﾂﾎﾞ風袋重量
     * @param rutubofuutaijyuuryou the rutubofuutaijyuuryou to set
     */
    public void setRutubofuutaijyuuryou(BigDecimal rutubofuutaijyuuryou) {
        this.rutubofuutaijyuuryou = rutubofuutaijyuuryou;
    }

    /**
     * 乾燥前ｽﾘｯﾌﾟ重量
     * @return the kansoumaeslipjyuuryou
     */
    public BigDecimal getKansoumaeslipjyuuryou() {
        return kansoumaeslipjyuuryou;
    }

    /**
     * 乾燥前ｽﾘｯﾌﾟ重量
     * @param kansoumaeslipjyuuryou the kansoumaeslipjyuuryou to set
     */
    public void setKansoumaeslipjyuuryou(BigDecimal kansoumaeslipjyuuryou) {
        this.kansoumaeslipjyuuryou = kansoumaeslipjyuuryou;
    }

    /**
     * 乾燥機①
     * @return the kansouki1
     */
    public String getKansouki1() {
        return kansouki1;
    }

    /**
     * 乾燥機①
     * @param kansouki1 the kansouki1 to set
     */
    public void setKansouki1(String kansouki1) {
        this.kansouki1 = kansouki1;
    }

    /**
     * 乾燥温度①
     * @return the kansouondo1
     */
    public String getKansouondo1() {
        return kansouondo1;
    }

    /**
     * 乾燥温度①
     * @param kansouondo1 the kansouondo1 to set
     */
    public void setKansouondo1(String kansouondo1) {
        this.kansouondo1 = kansouondo1;
    }

    /**
     * 乾燥時間①
     * @return the kansoujikan1
     */
    public String getKansoujikan1() {
        return kansoujikan1;
    }

    /**
     * 乾燥時間①
     * @param kansoujikan1 the kansoujikan1 to set
     */
    public void setKansoujikan1(String kansoujikan1) {
        this.kansoujikan1 = kansoujikan1;
    }

    /**
     * 乾燥開始日時①
     * @return the kansoukaisinichiji1
     */
    public Timestamp getKansoukaisinichiji1() {
        return kansoukaisinichiji1;
    }

    /**
     * 乾燥開始日時①
     * @param kansoukaisinichiji1 the kansoukaisinichiji1 to set
     */
    public void setKansoukaisinichiji1(Timestamp kansoukaisinichiji1) {
        this.kansoukaisinichiji1 = kansoukaisinichiji1;
    }

    /**
     * 乾燥終了日時①
     * @return the kansousyuuryounichiji1
     */
    public Timestamp getKansousyuuryounichiji1() {
        return kansousyuuryounichiji1;
    }

    /**
     * 乾燥終了日時①
     * @param kansousyuuryounichiji1 the kansousyuuryounichiji1 to set
     */
    public void setKansousyuuryounichiji1(Timestamp kansousyuuryounichiji1) {
        this.kansousyuuryounichiji1 = kansousyuuryounichiji1;
    }

    /**
     * 乾燥機②
     * @return the kansouki2
     */
    public String getKansouki2() {
        return kansouki2;
    }

    /**
     * 乾燥機②
     * @param kansouki2 the kansouki2 to set
     */
    public void setKansouki2(String kansouki2) {
        this.kansouki2 = kansouki2;
    }

    /**
     * 乾燥温度②
     * @return the kansouondo2
     */
    public String getKansouondo2() {
        return kansouondo2;
    }

    /**
     * 乾燥温度②
     * @param kansouondo2 the kansouondo2 to set
     */
    public void setKansouondo2(String kansouondo2) {
        this.kansouondo2 = kansouondo2;
    }

    /**
     * 乾燥時間②
     * @return the kansoujikan2
     */
    public String getKansoujikan2() {
        return kansoujikan2;
    }

    /**
     * 乾燥時間②
     * @param kansoujikan2 the kansoujikan2 to set
     */
    public void setKansoujikan2(String kansoujikan2) {
        this.kansoujikan2 = kansoujikan2;
    }

    /**
     * 乾燥開始日時②
     * @return the kansoukaisinichiji2
     */
    public Timestamp getKansoukaisinichiji2() {
        return kansoukaisinichiji2;
    }

    /**
     * 乾燥開始日時②
     * @param kansoukaisinichiji2 the kansoukaisinichiji2 to set
     */
    public void setKansoukaisinichiji2(Timestamp kansoukaisinichiji2) {
        this.kansoukaisinichiji2 = kansoukaisinichiji2;
    }

    /**
     * 乾燥終了日時②
     * @return the kansousyuuryounichiji2
     */
    public Timestamp getKansousyuuryounichiji2() {
        return kansousyuuryounichiji2;
    }

    /**
     * 乾燥終了日時②
     * @param kansousyuuryounichiji2 the kansousyuuryounichiji2 to set
     */
    public void setKansousyuuryounichiji2(Timestamp kansousyuuryounichiji2) {
        this.kansousyuuryounichiji2 = kansousyuuryounichiji2;
    }

    /**
     * 乾燥後総重量
     * @return the kansougosoujyuuryou
     */
    public BigDecimal getKansougosoujyuuryou() {
        return kansougosoujyuuryou;
    }

    /**
     * 乾燥後総重量
     * @param kansougosoujyuuryou the kansougosoujyuuryou to set
     */
    public void setKansougosoujyuuryou(BigDecimal kansougosoujyuuryou) {
        this.kansougosoujyuuryou = kansougosoujyuuryou;
    }

    /**
     * 乾燥後正味重量
     * @return the kansougosyoumijyuuryou
     */
    public BigDecimal getKansougosyoumijyuuryou() {
        return kansougosyoumijyuuryou;
    }

    /**
     * 乾燥後正味重量
     * @param kansougosyoumijyuuryou the kansougosyoumijyuuryou to set
     */
    public void setKansougosyoumijyuuryou(BigDecimal kansougosyoumijyuuryou) {
        this.kansougosyoumijyuuryou = kansougosyoumijyuuryou;
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
     * 固形分測定担当者
     * @return the kokeibunsokuteitantousya
     */
    public String getKokeibunsokuteitantousya() {
        return kokeibunsokuteitantousya;
    }

    /**
     * 固形分測定担当者
     * @param kokeibunsokuteitantousya the kokeibunsokuteitantousya to set
     */
    public void setKokeibunsokuteitantousya(String kokeibunsokuteitantousya) {
        this.kokeibunsokuteitantousya = kokeibunsokuteitantousya;
    }

    /**
     * 溶剤調整量
     * @return the youzaityouseiryou
     */
    public Integer getYouzaityouseiryou() {
        return youzaityouseiryou;
    }

    /**
     * 溶剤調整量
     * @param youzaityouseiryou the youzaityouseiryou to set
     */
    public void setYouzaityouseiryou(Integer youzaityouseiryou) {
        this.youzaityouseiryou = youzaityouseiryou;
    }

    /**
     * ﾄﾙｴﾝ調整量
     * @return the toluenetyouseiryou
     */
    public Integer getToluenetyouseiryou() {
        return toluenetyouseiryou;
    }

    /**
     * ﾄﾙｴﾝ調整量
     * @param toluenetyouseiryou the toluenetyouseiryou to set
     */
    public void setToluenetyouseiryou(Integer toluenetyouseiryou) {
        this.toluenetyouseiryou = toluenetyouseiryou;
    }

    /**
     * ｿﾙﾐｯｸｽ調整量
     * @return the solmixtyouseiryou
     */
    public Integer getSolmixtyouseiryou() {
        return solmixtyouseiryou;
    }

    /**
     * ｿﾙﾐｯｸｽ調整量
     * @param solmixtyouseiryou the solmixtyouseiryou to set
     */
    public void setSolmixtyouseiryou(Integer solmixtyouseiryou) {
        this.solmixtyouseiryou = solmixtyouseiryou;
    }

    /**
     * 溶剤秤量日時
     * @return the youzaikeiryounichiji
     */
    public Timestamp getYouzaikeiryounichiji() {
        return youzaikeiryounichiji;
    }

    /**
     * 溶剤秤量日時
     * @param youzaikeiryounichiji the youzaikeiryounichiji to set
     */
    public void setYouzaikeiryounichiji(Timestamp youzaikeiryounichiji) {
        this.youzaikeiryounichiji = youzaikeiryounichiji;
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
     * @return the youzai1_buzaizaikolotno1
     */
    public String getYouzai1_buzaizaikolotno1() {
        return youzai1_buzaizaikolotno1;
    }

    /**
     * 溶剤①_部材在庫No1
     * @param youzai1_buzaizaikolotno1 the youzai1_buzaizaikolotno1 to set
     */
    public void setYouzai1_buzaizaikolotno1(String youzai1_buzaizaikolotno1) {
        this.youzai1_buzaizaikolotno1 = youzai1_buzaizaikolotno1;
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
     * @return the youzai1_buzaizaikolotno2
     */
    public String getYouzai1_buzaizaikolotno2() {
        return youzai1_buzaizaikolotno2;
    }

    /**
     * 溶剤①_部材在庫No2
     * @param youzai1_buzaizaikolotno2 the youzai1_buzaizaikolotno2 to set
     */
    public void setYouzai1_buzaizaikolotno2(String youzai1_buzaizaikolotno2) {
        this.youzai1_buzaizaikolotno2 = youzai1_buzaizaikolotno2;
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
     * @return the youzai2_buzaizaikolotno1
     */
    public String getYouzai2_buzaizaikolotno1() {
        return youzai2_buzaizaikolotno1;
    }

    /**
     * 溶剤②_部材在庫No1
     * @param youzai2_buzaizaikolotno1 the youzai2_buzaizaikolotno1 to set
     */
    public void setYouzai2_buzaizaikolotno1(String youzai2_buzaizaikolotno1) {
        this.youzai2_buzaizaikolotno1 = youzai2_buzaizaikolotno1;
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
     * @return the youzai2_buzaizaikolotno2
     */
    public String getYouzai2_buzaizaikolotno2() {
        return youzai2_buzaizaikolotno2;
    }

    /**
     * 溶剤②_部材在庫No2
     * @param youzai2_buzaizaikolotno2 the youzai2_buzaizaikolotno2 to set
     */
    public void setYouzai2_buzaizaikolotno2(String youzai2_buzaizaikolotno2) {
        this.youzai2_buzaizaikolotno2 = youzai2_buzaizaikolotno2;
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
     * 撹拌_撹拌ﾓｰﾄﾞ
     * @return the kakuhan_kakuhanmode
     */
    public String getKakuhan_kakuhanmode() {
        return kakuhan_kakuhanmode;
    }

    /**
     * 撹拌_撹拌ﾓｰﾄﾞ
     * @param kakuhan_kakuhanmode the kakuhan_kakuhanmode to set
     */
    public void setKakuhan_kakuhanmode(String kakuhan_kakuhanmode) {
        this.kakuhan_kakuhanmode = kakuhan_kakuhanmode;
    }

    /**
     * 撹拌_回転方向
     * @return the kakuhan_kaitenhoukou
     */
    public String getKakuhan_kaitenhoukou() {
        return kakuhan_kaitenhoukou;
    }

    /**
     * 撹拌_回転方向
     * @param kakuhan_kaitenhoukou the kakuhan_kaitenhoukou to set
     */
    public void setKakuhan_kaitenhoukou(String kakuhan_kaitenhoukou) {
        this.kakuhan_kaitenhoukou = kakuhan_kaitenhoukou;
    }

    /**
     * 撹拌_回転数(rpm)
     * @return the kakuhan_kaitensuu
     */
    public String getKakuhan_kaitensuu() {
        return kakuhan_kaitensuu;
    }

    /**
     * 撹拌_回転数(rpm)
     * @param kakuhan_kaitensuu the kakuhan_kaitensuu to set
     */
    public void setKakuhan_kaitensuu(String kakuhan_kaitensuu) {
        this.kakuhan_kaitensuu = kakuhan_kaitensuu;
    }

    /**
     * 撹拌_撹拌時間
     * @return the kakuhan_kakuhanjikan
     */
    public String getKakuhan_kakuhanjikan() {
        return kakuhan_kakuhanjikan;
    }

    /**
     * 撹拌_撹拌時間
     * @param kakuhan_kakuhanjikan the kakuhan_kakuhanjikan to set
     */
    public void setKakuhan_kakuhanjikan(String kakuhan_kakuhanjikan) {
        this.kakuhan_kakuhanjikan = kakuhan_kakuhanjikan;
    }

    /**
     * 撹拌_撹拌開始日時
     * @return the kakuhan_kakuhankaisinichiji
     */
    public Timestamp getKakuhan_kakuhankaisinichiji() {
        return kakuhan_kakuhankaisinichiji;
    }

    /**
     * 撹拌_撹拌開始日時
     * @param kakuhan_kakuhankaisinichiji the kakuhan_kakuhankaisinichiji to set
     */
    public void setKakuhan_kakuhankaisinichiji(Timestamp kakuhan_kakuhankaisinichiji) {
        this.kakuhan_kakuhankaisinichiji = kakuhan_kakuhankaisinichiji;
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
     * 撹拌_撹拌終了予定日時
     * @return the kakuhan_kakuhansyuuryouyoteinichiji
     */
    public Timestamp getKakuhan_kakuhansyuuryouyoteinichiji() {
        return kakuhan_kakuhansyuuryouyoteinichiji;
    }

    /**
     * 撹拌_撹拌終了予定日時
     * @param kakuhan_kakuhansyuuryouyoteinichiji the kakuhan_kakuhansyuuryouyoteinichiji to set
     */
    public void setKakuhan_kakuhansyuuryouyoteinichiji(Timestamp kakuhan_kakuhansyuuryouyoteinichiji) {
        this.kakuhan_kakuhansyuuryouyoteinichiji = kakuhan_kakuhansyuuryouyoteinichiji;
    }

    /**
     * 撹拌_撹拌終了日時
     * @return the kakuhan_kakuhansyuuryounichiji
     */
    public Timestamp getKakuhan_kakuhansyuuryounichiji() {
        return kakuhan_kakuhansyuuryounichiji;
    }

    /**
     * 撹拌_撹拌終了日時
     * @param kakuhan_kakuhansyuuryounichiji the kakuhan_kakuhansyuuryounichiji to set
     */
    public void setKakuhan_kakuhansyuuryounichiji(Timestamp kakuhan_kakuhansyuuryounichiji) {
        this.kakuhan_kakuhansyuuryounichiji = kakuhan_kakuhansyuuryounichiji;
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
     * 撹拌_撹拌担当者
     * @return the kakuhan_kakuhantantousya
     */
    public String getKakuhan_kakuhantantousya() {
        return kakuhan_kakuhantantousya;
    }

    /**
     * 撹拌_撹拌担当者
     * @param kakuhan_kakuhantantousya the kakuhan_kakuhantantousya to set
     */
    public void setKakuhan_kakuhantantousya(String kakuhan_kakuhantantousya) {
        this.kakuhan_kakuhantantousya = kakuhan_kakuhantantousya;
    }

    /**
     * 排出前撹拌_撹拌ﾓｰﾄﾞ
     * @return the haisyutumaekakuhan_kakuhanmode
     */
    public String getHaisyutumaekakuhan_kakuhanmode() {
        return haisyutumaekakuhan_kakuhanmode;
    }

    /**
     * 排出前撹拌_撹拌ﾓｰﾄﾞ
     * @param haisyutumaekakuhan_kakuhanmode the haisyutumaekakuhan_kakuhanmode to set
     */
    public void setHaisyutumaekakuhan_kakuhanmode(String haisyutumaekakuhan_kakuhanmode) {
        this.haisyutumaekakuhan_kakuhanmode = haisyutumaekakuhan_kakuhanmode;
    }

    /**
     * 排出前撹拌_回転方向
     * @return the haisyutumaekakuhan_kaitenhoukou
     */
    public String getHaisyutumaekakuhan_kaitenhoukou() {
        return haisyutumaekakuhan_kaitenhoukou;
    }

    /**
     * 排出前撹拌_回転方向
     * @param haisyutumaekakuhan_kaitenhoukou the haisyutumaekakuhan_kaitenhoukou to set
     */
    public void setHaisyutumaekakuhan_kaitenhoukou(String haisyutumaekakuhan_kaitenhoukou) {
        this.haisyutumaekakuhan_kaitenhoukou = haisyutumaekakuhan_kaitenhoukou;
    }

    /**
     * 排出前撹拌_回転数(rpm)
     * @return the haisyutumaekakuhan_kaitensuu
     */
    public String getHaisyutumaekakuhan_kaitensuu() {
        return haisyutumaekakuhan_kaitensuu;
    }

    /**
     * 排出前撹拌_回転数(rpm)
     * @param haisyutumaekakuhan_kaitensuu the haisyutumaekakuhan_kaitensuu to set
     */
    public void setHaisyutumaekakuhan_kaitensuu(String haisyutumaekakuhan_kaitensuu) {
        this.haisyutumaekakuhan_kaitensuu = haisyutumaekakuhan_kaitensuu;
    }

    /**
     * 排出前撹拌_撹拌時間
     * @return the haisyutumaekakuhan_kakuhanjikan
     */
    public String getHaisyutumaekakuhan_kakuhanjikan() {
        return haisyutumaekakuhan_kakuhanjikan;
    }

    /**
     * 排出前撹拌_撹拌時間
     * @param haisyutumaekakuhan_kakuhanjikan the haisyutumaekakuhan_kakuhanjikan to set
     */
    public void setHaisyutumaekakuhan_kakuhanjikan(String haisyutumaekakuhan_kakuhanjikan) {
        this.haisyutumaekakuhan_kakuhanjikan = haisyutumaekakuhan_kakuhanjikan;
    }

    /**
     * 排出前撹拌_撹拌開始日時
     * @return the haisyutumaekakuhan_kakuhankaisinichiji
     */
    public Timestamp getHaisyutumaekakuhan_kakuhankaisinichiji() {
        return haisyutumaekakuhan_kakuhankaisinichiji;
    }

    /**
     * 排出前撹拌_撹拌開始日時
     * @param haisyutumaekakuhan_kakuhankaisinichiji the haisyutumaekakuhan_kakuhankaisinichiji to set
     */
    public void setHaisyutumaekakuhan_kakuhankaisinichiji(Timestamp haisyutumaekakuhan_kakuhankaisinichiji) {
        this.haisyutumaekakuhan_kakuhankaisinichiji = haisyutumaekakuhan_kakuhankaisinichiji;
    }

    /**
     * 排出前撹拌_撹拌終了予定日時
     * @return the haisyutumaekakuhan_kakuhansyuuryouyoteinichiji
     */
    public Timestamp getHaisyutumaekakuhan_kakuhansyuuryouyoteinichiji() {
        return haisyutumaekakuhan_kakuhansyuuryouyoteinichiji;
    }

    /**
     * 排出前撹拌_撹拌終了予定日時
     * @param haisyutumaekakuhan_kakuhansyuuryouyoteinichiji the haisyutumaekakuhan_kakuhansyuuryouyoteinichiji to set
     */
    public void setHaisyutumaekakuhan_kakuhansyuuryouyoteinichiji(Timestamp haisyutumaekakuhan_kakuhansyuuryouyoteinichiji) {
        this.haisyutumaekakuhan_kakuhansyuuryouyoteinichiji = haisyutumaekakuhan_kakuhansyuuryouyoteinichiji;
    }

    /**
     * 排出前撹拌_撹拌終了日時
     * @return the haisyutumaekakuhan_kakuhansyuuryounichiji
     */
    public Timestamp getHaisyutumaekakuhan_kakuhansyuuryounichiji() {
        return haisyutumaekakuhan_kakuhansyuuryounichiji;
    }

    /**
     * 排出前撹拌_撹拌終了日時
     * @param haisyutumaekakuhan_kakuhansyuuryounichiji the haisyutumaekakuhan_kakuhansyuuryounichiji to set
     */
    public void setHaisyutumaekakuhan_kakuhansyuuryounichiji(Timestamp haisyutumaekakuhan_kakuhansyuuryounichiji) {
        this.haisyutumaekakuhan_kakuhansyuuryounichiji = haisyutumaekakuhan_kakuhansyuuryounichiji;
    }

    /**
     * 排出前撹拌_撹拌担当者
     * @return the haisyutumaekakuhan_kakuhantantousya
     */
    public String getHaisyutumaekakuhan_kakuhantantousya() {
        return haisyutumaekakuhan_kakuhantantousya;
    }

    /**
     * 排出前撹拌_撹拌担当者
     * @param haisyutumaekakuhan_kakuhantantousya the haisyutumaekakuhan_kakuhantantousya to set
     */
    public void setHaisyutumaekakuhan_kakuhantantousya(String haisyutumaekakuhan_kakuhantantousya) {
        this.haisyutumaekakuhan_kakuhantantousya = haisyutumaekakuhan_kakuhantantousya;
    }

    /**
     * 排出開始日時
     * @return the haisyutukaisinichiji
     */
    public Timestamp getHaisyutukaisinichiji() {
        return haisyutukaisinichiji;
    }

    /**
     * 排出開始日時
     * @param haisyutukaisinichiji the haisyutukaisinichiji to set
     */
    public void setHaisyutukaisinichiji(Timestamp haisyutukaisinichiji) {
        this.haisyutukaisinichiji = haisyutukaisinichiji;
    }

    /**
     * 排出終了日時
     * @return the haisyutusyuuryounichiji
     */
    public Timestamp getHaisyutusyuuryounichiji() {
        return haisyutusyuuryounichiji;
    }

    /**
     * 排出終了日時
     * @param haisyutusyuuryounichiji the haisyutusyuuryounichiji to set
     */
    public void setHaisyutusyuuryounichiji(Timestamp haisyutusyuuryounichiji) {
        this.haisyutusyuuryounichiji = haisyutusyuuryounichiji;
    }

    /**
     * 排出担当者
     * @return the haisyututantousya
     */
    public String getHaisyututantousya() {
        return haisyututantousya;
    }

    /**
     * 排出担当者
     * @param haisyututantousya the haisyututantousya to set
     */
    public void setHaisyututantousya(String haisyututantousya) {
        this.haisyututantousya = haisyututantousya;
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