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
 * 変更日	2021/12/06<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SR_SLIP_SLURRYKOKEIBUNTYOUSEI_SUTENYOUKI(ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器))のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since 2021/12/06
 */
public class SrSlipSlurrykokeibuntyouseiSutenyouki {

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
     * ｽﾗﾘｰ品名
     */
    private String slurryhinmei;

    /**
     * ｽﾗﾘｰLotNo
     */
    private Integer slurrylotno;

    /**
     * 容器No
     */
    private Integer youkino;

    /**
     * ｽﾗﾘｰ有効期限
     */
    private String slurryyuukoukigen;

    /**
     * 乾燥固形分
     */
    private BigDecimal kansoukokeibun;

    /**
     * 脱脂固形分
     */
    private BigDecimal dassikokeibun;

    /**
     * 粉砕終了日時
     */
    private Timestamp funsaisyuuryounichiji;

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合日時
     */
    private Timestamp binderkongounichij;

    /**
     * ｽﾗﾘｰｴｰｼﾞﾝｸﾞ
     */
    private String slurraging;

    /**
     * ｽﾗﾘｰ経過日数
     */
    private Integer slurrykeikanisuu;

    /**
     * ｽﾘｯﾌﾟ情報_風袋重量
     */
    private Integer slipjyouhou_fuutaijyuuryou;

    /**
     * 総重量
     */
    private Integer slurrysoujyuuryou;

    /**
     * ｽﾗﾘｰ重量
     */
    private Integer slurryjyuuryou;

    /**
     * 撹拌設備
     */
    private String kakuhansetubi;

    /**
     * 撹拌号機
     */
    private String kakuhangoki;

    /**
     * 撹拌回転数
     */
    private String kakuhankaitensuu;

    /**
     * 撹拌時間
     */
    private String kakuhanjikan;

    /**
     * 撹拌開始日時
     */
    private Timestamp kakuhankaisinichiji;

    /**
     * 撹拌終了日時
     */
    private Timestamp kakuhansyuuryounichiji;

    /**
     * 脱脂皿の種類
     */
    private String dassizaranosyurui;

    /**
     * 固形分測定_風袋重量
     */
    private BigDecimal kokeibunsokutei_fuutaijyuuryou;

    /**
     * 乾燥前ｽﾗﾘｰ重量
     */
    private BigDecimal kansoumaeslurryjyuuryou;

    /**
     * 乾燥機
     */
    private String kansouki;

    /**
     * 乾燥温度
     */
    private String kansouondo;

    /**
     * 乾燥時間
     */
    private String kansoujikan;

    /**
     * 乾燥開始日時
     */
    private Timestamp kansoukaisinichij;

    /**
     * 乾燥終了日時
     */
    private Timestamp kansousyuuryounichiji;

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
     * 2回目撹拌設備
     */
    private String nikaimekakuhansetubi;

    /**
     * 2回目撹拌時間
     */
    private String nikaimekakuhanjikan;

    /**
     * 2回目撹拌開始日時
     */
    private Timestamp nikaimekakuhankaisinichiji;

    /**
     * 2回目撹拌終了日時
     */
    private Timestamp nikaimekakuhansyuuryounichiji;

    /**
     * 2回目脱脂皿の種類
     */
    private String nikaimedassizaranosyurui;

    /**
     * 2回目ｱﾙﾐ皿風袋重量
     */
    private BigDecimal nikaimearumizarafuutaijyuuryou;

    /**
     * 2回目乾燥前ｽﾗﾘｰ重量
     */
    private BigDecimal nikaimekansoumaeslurryjyuuryou;

    /**
     * 2回目乾燥機
     */
    private String nikaimekansouki;

    /**
     * 2回目乾燥温度
     */
    private String nikaimekansouondo;

    /**
     * 2回目乾燥時間
     */
    private String nikaimekansoujikan;

    /**
     * 2回目乾燥開始日時
     */
    private Timestamp nikaimekansoukaisinichij;

    /**
     * 2回目乾燥終了日時
     */
    private Timestamp nikaimekansousyuuryounichiji;

    /**
     * 2回目乾燥後総重量
     */
    private BigDecimal nikaimekansougosoujyuuryou;

    /**
     * 2回目乾燥後正味重量
     */
    private BigDecimal nikaimekansougosyoumijyuuryou;

    /**
     * 2回目固形分比率
     */
    private BigDecimal nikaimekokeibunhiritu;

    /**
     * 2回目固形分測定担当者
     */
    private String nikaimekokeibunsokuteitantousya;

    /**
     * 2回目溶剤調整量
     */
    private Integer nikaimeyouzaityouseiryou;

    /**
     * 2回目ﾄﾙｴﾝ調整量
     */
    private Integer nikaimetoluenetyouseiryou;

    /**
     * 2回目ｿﾙﾐｯｸｽ調整量
     */
    private Integer nikaimesolmixtyouseiryou;

    /**
     * 2回目溶剤秤量日時
     */
    private Timestamp nikaimeyouzaikeiryounichiji;

    /**
     * 2回目溶剤①_材料品名
     */
    private String nikaimeyouzai1_zairyouhinmei;

    /**
     * 2回目溶剤①_調合量規格
     */
    private String nikaimeyouzai1_tyougouryoukikaku;

    /**
     * 2回目溶剤①_部材在庫No1
     */
    private String nikaimeyouzai1_buzaizaikolotno1;

    /**
     * 2回目溶剤①_調合量1
     */
    private Integer nikaimeyouzai1_tyougouryou1;

    /**
     * 2回目溶剤①_部材在庫No2
     */
    private String nikaimeyouzai1_buzaizaikolotno2;

    /**
     * 2回目溶剤①_調合量2
     */
    private Integer nikaimeyouzai1_tyougouryou2;

    /**
     * 2回目溶剤②_材料品名
     */
    private String nikaimeyouzai2_zairyouhinmei;

    /**
     * 2回目溶剤②_調合量規格
     */
    private String nikaimeyouzai2_tyougouryoukikaku;

    /**
     * 2回目溶剤②_部材在庫No1
     */
    private String nikaimeyouzai2_buzaizaikolotno1;

    /**
     * 2回目溶剤②_調合量1
     */
    private Integer nikaimeyouzai2_tyougouryou1;

    /**
     * 2回目溶剤②_部材在庫No2
     */
    private String nikaimeyouzai2_buzaizaikolotno2;

    /**
     * 2回目溶剤②_調合量2
     */
    private Integer nikaimeyouzai2_tyougouryou2;

    /**
     * 2回目担当者
     */
    private String nikaimetantousya;

    /**
     * 3回目撹拌設備
     */
    private String sankaimekakuhansetubi;

    /**
     * 3回目撹拌時間
     */
    private String sankaimekakuhanjikan;

    /**
     * 3回目撹拌開始日時
     */
    private Timestamp sankaimekakuhankaisinichiji;

    /**
     * 3回目撹拌終了日時
     */
    private Timestamp sankaimekakuhansyuuryounichiji;

    /**
     * 3回目脱脂皿の種類
     */
    private String sankaimedassizaranosyurui;

    /**
     * 3回目ｱﾙﾐ皿風袋重量
     */
    private BigDecimal sankaimearumizarafuutaijyuuryou;

    /**
     * 3回目乾燥前ｽﾗﾘｰ重量
     */
    private BigDecimal sankaimekansoumaeslurryjyuuryou;

    /**
     * 3回目乾燥機
     */
    private String sankaimekansouki;

    /**
     * 3回目乾燥温度
     */
    private String sankaimekansouondo;

    /**
     * 3回目乾燥時間
     */
    private String sankaimekansoujikan;

    /**
     * 3回目乾燥開始日時
     */
    private Timestamp sankaimekansoukaisinichij;

    /**
     * 3回目乾燥終了日時
     */
    private Timestamp sankaimekansousyuuryounichiji;

    /**
     * 3回目乾燥後総重量
     */
    private BigDecimal sankaimekansougosoujyuuryou;

    /**
     * 3回目乾燥後正味重量
     */
    private BigDecimal sankaimekansougosyoumijyuuryou;

    /**
     * 3回目固形分比率
     */
    private BigDecimal sankaimekokeibunhiritu;

    /**
     * 3回目固形分測定担当者
     */
    private String sankaimekokeibunsokuteitantousya;

    /**
     * F/P準備_風袋重量
     */
    private Integer fpjyunbi_fuutaijyuuryou;

    /**
     * F/P準備_担当者
     */
    private String fpjyunbi_tantousya;

    /**
     * ﾌｨﾙﾀｰ連結
     */
    private String filterrenketu;

    /**
     * F/P準備_ﾌｨﾙﾀｰ品名①
     */
    private String fpjyunbi_filterhinmei1;

    /**
     * F/P準備_LotNo①
     */
    private String fpjyunbi_lotno1;

    /**
     * F/P準備_取り付け本数①
     */
    private Integer fpjyunbi_toritukehonsuu1;

    /**
     * F/P準備_ﾌｨﾙﾀｰ品名②
     */
    private String fpjyunbi_filterhinmei2;

    /**
     * F/P準備_LotNo②
     */
    private String fpjyunbi_lotno2;

    /**
     * F/P準備_取り付け本数②
     */
    private Integer fpjyunbi_toritukehonsuu2;

    /**
     * 圧送ﾀﾝｸの洗浄
     */
    private Integer assoutanknosenjyou;

    /**
     * 排出容器の内袋
     */
    private Integer haisyutuyoukinoutibukuro;

    /**
     * F/PﾀﾝｸNo
     */
    private Integer fptankno;

    /**
     * F/P開始日時
     */
    private Timestamp fpkaisinichiji;

    /**
     * 圧送ﾚｷﾞｭﾚｰﾀｰNo
     */
    private Integer assouregulatorno;

    /**
     * 圧送圧力
     */
    private BigDecimal assouaturyoku;

    /**
     * F/P開始_担当者
     */
    private String fpkaisi_tantousya;

    /**
     * F/P停止日時
     */
    private Timestamp fpteisinichiji;

    /**
     * F/P交換_ﾌｨﾙﾀｰ品名①
     */
    private String fpkoukan_filterhinmei1;

    /**
     * F/P交換_LotNo①
     */
    private String fpkoukan_lotno1;

    /**
     * F/P交換_取り付け本数①
     */
    private Integer fpkoukan_toritukehonsuu1;

    /**
     * F/P交換_ﾌｨﾙﾀｰ品名②
     */
    private String fpkoukan_filterhinmei2;

    /**
     * F/P交換_LotNo②
     */
    private String fpkoukan_lotno2;

    /**
     * F/P交換_取り付け本数②
     */
    private Integer fpkoukan_toritukehonsuu2;

    /**
     * F/P再開日時
     */
    private Timestamp fpsaikainichiji;

    /**
     * F/P交換_担当者
     */
    private String fpkoukan_tantousya;

    /**
     * F/P終了日時
     */
    private Timestamp fpsyuuryounichiji;

    /**
     * F/P時間
     */
    private Integer fpjikan;

    /**
     * F/P終了_担当者
     */
    private String fpsyuurou_tantousya;

    /**
     * 総重量
     */
    private Integer soujyuuryou;

    /**
     * 正味重量
     */
    private BigDecimal syoumijyuuryou;

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
     * ｽﾗﾘｰLotNo
     * @return the slurrylotno
     */
    public Integer getSlurrylotno() {
        return slurrylotno;
    }

    /**
     * ｽﾗﾘｰLotNo
     * @param slurrylotno the slurrylotno to set
     */
    public void setSlurrylotno(Integer slurrylotno) {
        this.slurrylotno = slurrylotno;
    }

    /**
     * 容器No
     * @return the youkino
     */
    public Integer getYoukino() {
        return youkino;
    }

    /**
     * 容器No
     * @param youkino the youkino to set
     */
    public void setYoukino(Integer youkino) {
        this.youkino = youkino;
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
     * ｽﾗﾘｰｴｰｼﾞﾝｸﾞ
     * @return the slurraging
     */
    public String getSlurraging() {
        return slurraging;
    }

    /**
     * ｽﾗﾘｰｴｰｼﾞﾝｸﾞ
     * @param slurraging the slurraging to set
     */
    public void setSlurraging(String slurraging) {
        this.slurraging = slurraging;
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
     * ｽﾘｯﾌﾟ情報_風袋重量
     * @return the slipjyouhou_fuutaijyuuryou
     */
    public Integer getSlipjyouhou_fuutaijyuuryou() {
        return slipjyouhou_fuutaijyuuryou;
    }

    /**
     * ｽﾘｯﾌﾟ情報_風袋重量
     * @param slipjyouhou_fuutaijyuuryou the slipjyouhou_fuutaijyuuryou to set
     */
    public void setSlipjyouhou_fuutaijyuuryou(Integer slipjyouhou_fuutaijyuuryou) {
        this.slipjyouhou_fuutaijyuuryou = slipjyouhou_fuutaijyuuryou;
    }

    /**
     * 総重量
     * @return the slurrysoujyuuryou
     */
    public Integer getSlurrysoujyuuryou() {
        return slurrysoujyuuryou;
    }

    /**
     * 総重量
     * @param slurrysoujyuuryou the slurrysoujyuuryou to set
     */
    public void setSlurrysoujyuuryou(Integer slurrysoujyuuryou) {
        this.slurrysoujyuuryou = slurrysoujyuuryou;
    }

    /**
     * ｽﾗﾘｰ重量
     * @return the slurryjyuuryou
     */
    public Integer getSlurryjyuuryou() {
        return slurryjyuuryou;
    }

    /**
     * ｽﾗﾘｰ重量
     * @param slurryjyuuryou the slurryjyuuryou to set
     */
    public void setSlurryjyuuryou(Integer slurryjyuuryou) {
        this.slurryjyuuryou = slurryjyuuryou;
    }

    /**
     * 撹拌設備
     * @return the kakuhansetubi
     */
    public String getKakuhansetubi() {
        return kakuhansetubi;
    }

    /**
     * 撹拌設備
     * @param kakuhansetubi the kakuhansetubi to set
     */
    public void setKakuhansetubi(String kakuhansetubi) {
        this.kakuhansetubi = kakuhansetubi;
    }

    /**
     * 撹拌号機
     * @return the kakuhangoki
     */
    public String getKakuhangoki() {
        return kakuhangoki;
    }

    /**
     * 撹拌号機
     * @param kakuhangoki the kakuhangoki to set
     */
    public void setKakuhangoki(String kakuhangoki) {
        this.kakuhangoki = kakuhangoki;
    }

    /**
     * 撹拌回転数
     * @return the kakuhankaitensuu
     */
    public String getKakuhankaitensuu() {
        return kakuhankaitensuu;
    }

    /**
     * 撹拌回転数
     * @param kakuhankaitensuu the kakuhankaitensuu to set
     */
    public void setKakuhankaitensuu(String kakuhankaitensuu) {
        this.kakuhankaitensuu = kakuhankaitensuu;
    }

    /**
     * 撹拌時間
     * @return the kakuhanjikan
     */
    public String getKakuhanjikan() {
        return kakuhanjikan;
    }

    /**
     * 撹拌時間
     * @param kakuhanjikan the kakuhanjikan to set
     */
    public void setKakuhanjikan(String kakuhanjikan) {
        this.kakuhanjikan = kakuhanjikan;
    }

    /**
     * 撹拌開始日時
     * @return the kakuhankaisinichiji
     */
    public Timestamp getKakuhankaisinichiji() {
        return kakuhankaisinichiji;
    }

    /**
     * 撹拌開始日時
     * @param kakuhankaisinichiji the kakuhankaisinichiji to set
     */
    public void setKakuhankaisinichiji(Timestamp kakuhankaisinichiji) {
        this.kakuhankaisinichiji = kakuhankaisinichiji;
    }

    /**
     * 撹拌終了日時
     * @return the kakuhansyuuryounichiji
     */
    public Timestamp getKakuhansyuuryounichiji() {
        return kakuhansyuuryounichiji;
    }

    /**
     * 撹拌終了日時
     * @param kakuhansyuuryounichiji the kakuhansyuuryounichiji to set
     */
    public void setKakuhansyuuryounichiji(Timestamp kakuhansyuuryounichiji) {
        this.kakuhansyuuryounichiji = kakuhansyuuryounichiji;
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
     * 固形分測定_風袋重量
     * @return the kokeibunsokutei_fuutaijyuuryou
     */
    public BigDecimal getKokeibunsokutei_fuutaijyuuryou() {
        return kokeibunsokutei_fuutaijyuuryou;
    }

    /**
     * 固形分測定_風袋重量
     * @param kokeibunsokutei_fuutaijyuuryou the kokeibunsokutei_fuutaijyuuryou to set
     */
    public void setKokeibunsokutei_fuutaijyuuryou(BigDecimal kokeibunsokutei_fuutaijyuuryou) {
        this.kokeibunsokutei_fuutaijyuuryou = kokeibunsokutei_fuutaijyuuryou;
    }

    /**
     * 乾燥前ｽﾗﾘｰ重量
     * @return the kansoumaeslurryjyuuryou
     */
    public BigDecimal getKansoumaeslurryjyuuryou() {
        return kansoumaeslurryjyuuryou;
    }

    /**
     * 乾燥前ｽﾗﾘｰ重量
     * @param kansoumaeslurryjyuuryou the kansoumaeslurryjyuuryou to set
     */
    public void setKansoumaeslurryjyuuryou(BigDecimal kansoumaeslurryjyuuryou) {
        this.kansoumaeslurryjyuuryou = kansoumaeslurryjyuuryou;
    }

    /**
     * 乾燥機
     * @return the kansouki
     */
    public String getKansouki() {
        return kansouki;
    }

    /**
     * 乾燥機
     * @param kansouki the kansouki to set
     */
    public void setKansouki(String kansouki) {
        this.kansouki = kansouki;
    }

    /**
     * 乾燥温度
     * @return the kansouondo
     */
    public String getKansouondo() {
        return kansouondo;
    }

    /**
     * 乾燥温度
     * @param kansouondo the kansouondo to set
     */
    public void setKansouondo(String kansouondo) {
        this.kansouondo = kansouondo;
    }

    /**
     * 乾燥時間
     * @return the kansoujikan
     */
    public String getKansoujikan() {
        return kansoujikan;
    }

    /**
     * 乾燥時間
     * @param kansoujikan the kansoujikan to set
     */
    public void setKansoujikan(String kansoujikan) {
        this.kansoujikan = kansoujikan;
    }

    /**
     * 乾燥開始日時
     * @return the kansoukaisinichij
     */
    public Timestamp getKansoukaisinichij() {
        return kansoukaisinichij;
    }

    /**
     * 乾燥開始日時
     * @param kansoukaisinichij the kansoukaisinichij to set
     */
    public void setKansoukaisinichij(Timestamp kansoukaisinichij) {
        this.kansoukaisinichij = kansoukaisinichij;
    }

    /**
     * 乾燥終了日時
     * @return the kansousyuuryounichiji
     */
    public Timestamp getKansousyuuryounichiji() {
        return kansousyuuryounichiji;
    }

    /**
     * 乾燥終了日時
     * @param kansousyuuryounichiji the kansousyuuryounichiji to set
     */
    public void setKansousyuuryounichiji(Timestamp kansousyuuryounichiji) {
        this.kansousyuuryounichiji = kansousyuuryounichiji;
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
     * 2回目撹拌設備
     * @return the nikaimekakuhansetubi
     */
    public String getNikaimekakuhansetubi() {
        return nikaimekakuhansetubi;
    }

    /**
     * 2回目撹拌設備
     * @param nikaimekakuhansetubi the nikaimekakuhansetubi to set
     */
    public void setNikaimekakuhansetubi(String nikaimekakuhansetubi) {
        this.nikaimekakuhansetubi = nikaimekakuhansetubi;
    }

    /**
     * 2回目撹拌時間
     * @return the nikaimekakuhanjikan
     */
    public String getNikaimekakuhanjikan() {
        return nikaimekakuhanjikan;
    }

    /**
     * 2回目撹拌時間
     * @param nikaimekakuhanjikan the nikaimekakuhanjikan to set
     */
    public void setNikaimekakuhanjikan(String nikaimekakuhanjikan) {
        this.nikaimekakuhanjikan = nikaimekakuhanjikan;
    }

    /**
     * 2回目撹拌開始日時
     * @return the nikaimekakuhankaisinichiji
     */
    public Timestamp getNikaimekakuhankaisinichiji() {
        return nikaimekakuhankaisinichiji;
    }

    /**
     * 2回目撹拌開始日時
     * @param nikaimekakuhankaisinichiji the nikaimekakuhankaisinichiji to set
     */
    public void setNikaimekakuhankaisinichiji(Timestamp nikaimekakuhankaisinichiji) {
        this.nikaimekakuhankaisinichiji = nikaimekakuhankaisinichiji;
    }

    /**
     * 2回目撹拌終了日時
     * @return the nikaimekakuhansyuuryounichiji
     */
    public Timestamp getNikaimekakuhansyuuryounichiji() {
        return nikaimekakuhansyuuryounichiji;
    }

    /**
     * 2回目撹拌終了日時
     * @param nikaimekakuhansyuuryounichiji the nikaimekakuhansyuuryounichiji to set
     */
    public void setNikaimekakuhansyuuryounichiji(Timestamp nikaimekakuhansyuuryounichiji) {
        this.nikaimekakuhansyuuryounichiji = nikaimekakuhansyuuryounichiji;
    }

    /**
     * 2回目脱脂皿の種類
     * @return the nikaimedassizaranosyurui
     */
    public String getNikaimedassizaranosyurui() {
        return nikaimedassizaranosyurui;
    }

    /**
     * 2回目脱脂皿の種類
     * @param nikaimedassizaranosyurui the nikaimedassizaranosyurui to set
     */
    public void setNikaimedassizaranosyurui(String nikaimedassizaranosyurui) {
        this.nikaimedassizaranosyurui = nikaimedassizaranosyurui;
    }

    /**
     * 2回目ｱﾙﾐ皿風袋重量
     * @return the nikaimearumizarafuutaijyuuryou
     */
    public BigDecimal getNikaimearumizarafuutaijyuuryou() {
        return nikaimearumizarafuutaijyuuryou;
    }

    /**
     * 2回目ｱﾙﾐ皿風袋重量
     * @param nikaimearumizarafuutaijyuuryou the nikaimearumizarafuutaijyuuryou to set
     */
    public void setNikaimearumizarafuutaijyuuryou(BigDecimal nikaimearumizarafuutaijyuuryou) {
        this.nikaimearumizarafuutaijyuuryou = nikaimearumizarafuutaijyuuryou;
    }

    /**
     * 2回目乾燥前ｽﾗﾘｰ重量
     * @return the nikaimekansoumaeslurryjyuuryou
     */
    public BigDecimal getNikaimekansoumaeslurryjyuuryou() {
        return nikaimekansoumaeslurryjyuuryou;
    }

    /**
     * 2回目乾燥前ｽﾗﾘｰ重量
     * @param nikaimekansoumaeslurryjyuuryou the nikaimekansoumaeslurryjyuuryou to set
     */
    public void setNikaimekansoumaeslurryjyuuryou(BigDecimal nikaimekansoumaeslurryjyuuryou) {
        this.nikaimekansoumaeslurryjyuuryou = nikaimekansoumaeslurryjyuuryou;
    }

    /**
     * 2回目乾燥機
     * @return the nikaimekansouki
     */
    public String getNikaimekansouki() {
        return nikaimekansouki;
    }

    /**
     * 2回目乾燥機
     * @param nikaimekansouki the nikaimekansouki to set
     */
    public void setNikaimekansouki(String nikaimekansouki) {
        this.nikaimekansouki = nikaimekansouki;
    }

    /**
     * 2回目乾燥温度
     * @return the nikaimekansouondo
     */
    public String getNikaimekansouondo() {
        return nikaimekansouondo;
    }

    /**
     * 2回目乾燥温度
     * @param nikaimekansouondo the nikaimekansouondo to set
     */
    public void setNikaimekansouondo(String nikaimekansouondo) {
        this.nikaimekansouondo = nikaimekansouondo;
    }

    /**
     * 2回目乾燥時間
     * @return the nikaimekansoujikan
     */
    public String getNikaimekansoujikan() {
        return nikaimekansoujikan;
    }

    /**
     * 2回目乾燥時間
     * @param nikaimekansoujikan the nikaimekansoujikan to set
     */
    public void setNikaimekansoujikan(String nikaimekansoujikan) {
        this.nikaimekansoujikan = nikaimekansoujikan;
    }

    /**
     * 2回目乾燥開始日時
     * @return the nikaimekansoukaisinichij
     */
    public Timestamp getNikaimekansoukaisinichij() {
        return nikaimekansoukaisinichij;
    }

    /**
     * 2回目乾燥開始日時
     * @param nikaimekansoukaisinichij the nikaimekansoukaisinichij to set
     */
    public void setNikaimekansoukaisinichij(Timestamp nikaimekansoukaisinichij) {
        this.nikaimekansoukaisinichij = nikaimekansoukaisinichij;
    }

    /**
     * 2回目乾燥終了日時
     * @return the nikaimekansousyuuryounichiji
     */
    public Timestamp getNikaimekansousyuuryounichiji() {
        return nikaimekansousyuuryounichiji;
    }

    /**
     * 2回目乾燥終了日時
     * @param nikaimekansousyuuryounichiji the nikaimekansousyuuryounichiji to set
     */
    public void setNikaimekansousyuuryounichiji(Timestamp nikaimekansousyuuryounichiji) {
        this.nikaimekansousyuuryounichiji = nikaimekansousyuuryounichiji;
    }

    /**
     * 2回目乾燥後総重量
     * @return the nikaimekansougosoujyuuryou
     */
    public BigDecimal getNikaimekansougosoujyuuryou() {
        return nikaimekansougosoujyuuryou;
    }

    /**
     * 2回目乾燥後総重量
     * @param nikaimekansougosoujyuuryou the nikaimekansougosoujyuuryou to set
     */
    public void setNikaimekansougosoujyuuryou(BigDecimal nikaimekansougosoujyuuryou) {
        this.nikaimekansougosoujyuuryou = nikaimekansougosoujyuuryou;
    }

    /**
     * 2回目乾燥後正味重量
     * @return the nikaimekansougosyoumijyuuryou
     */
    public BigDecimal getNikaimekansougosyoumijyuuryou() {
        return nikaimekansougosyoumijyuuryou;
    }

    /**
     * 2回目乾燥後正味重量
     * @param nikaimekansougosyoumijyuuryou the nikaimekansougosyoumijyuuryou to set
     */
    public void setNikaimekansougosyoumijyuuryou(BigDecimal nikaimekansougosyoumijyuuryou) {
        this.nikaimekansougosyoumijyuuryou = nikaimekansougosyoumijyuuryou;
    }

    /**
     * 2回目固形分比率
     * @return the nikaimekokeibunhiritu
     */
    public BigDecimal getNikaimekokeibunhiritu() {
        return nikaimekokeibunhiritu;
    }

    /**
     * 2回目固形分比率
     * @param nikaimekokeibunhiritu the nikaimekokeibunhiritu to set
     */
    public void setNikaimekokeibunhiritu(BigDecimal nikaimekokeibunhiritu) {
        this.nikaimekokeibunhiritu = nikaimekokeibunhiritu;
    }

    /**
     * 2回目固形分測定担当者
     * @return the nikaimekokeibunsokuteitantousya
     */
    public String getNikaimekokeibunsokuteitantousya() {
        return nikaimekokeibunsokuteitantousya;
    }

    /**
     * 2回目固形分測定担当者
     * @param nikaimekokeibunsokuteitantousya the nikaimekokeibunsokuteitantousya to set
     */
    public void setNikaimekokeibunsokuteitantousya(String nikaimekokeibunsokuteitantousya) {
        this.nikaimekokeibunsokuteitantousya = nikaimekokeibunsokuteitantousya;
    }

    /**
     * 2回目溶剤調整量
     * @return the nikaimeyouzaityouseiryou
     */
    public Integer getNikaimeyouzaityouseiryou() {
        return nikaimeyouzaityouseiryou;
    }

    /**
     * 2回目溶剤調整量
     * @param nikaimeyouzaityouseiryou the nikaimeyouzaityouseiryou to set
     */
    public void setNikaimeyouzaityouseiryou(Integer nikaimeyouzaityouseiryou) {
        this.nikaimeyouzaityouseiryou = nikaimeyouzaityouseiryou;
    }

    /**
     * 2回目ﾄﾙｴﾝ調整量
     * @return the nikaimetoluenetyouseiryou
     */
    public Integer getNikaimetoluenetyouseiryou() {
        return nikaimetoluenetyouseiryou;
    }

    /**
     * 2回目ﾄﾙｴﾝ調整量
     * @param nikaimetoluenetyouseiryou the nikaimetoluenetyouseiryou to set
     */
    public void setNikaimetoluenetyouseiryou(Integer nikaimetoluenetyouseiryou) {
        this.nikaimetoluenetyouseiryou = nikaimetoluenetyouseiryou;
    }

    /**
     * 2回目ｿﾙﾐｯｸｽ調整量
     * @return the nikaimesolmixtyouseiryou
     */
    public Integer getNikaimesolmixtyouseiryou() {
        return nikaimesolmixtyouseiryou;
    }

    /**
     * 2回目ｿﾙﾐｯｸｽ調整量
     * @param nikaimesolmixtyouseiryou the nikaimesolmixtyouseiryou to set
     */
    public void setNikaimesolmixtyouseiryou(Integer nikaimesolmixtyouseiryou) {
        this.nikaimesolmixtyouseiryou = nikaimesolmixtyouseiryou;
    }

    /**
     * 2回目溶剤秤量日時
     * @return the nikaimeyouzaikeiryounichiji
     */
    public Timestamp getNikaimeyouzaikeiryounichiji() {
        return nikaimeyouzaikeiryounichiji;
    }

    /**
     * 2回目溶剤秤量日時
     * @param nikaimeyouzaikeiryounichiji the nikaimeyouzaikeiryounichiji to set
     */
    public void setNikaimeyouzaikeiryounichiji(Timestamp nikaimeyouzaikeiryounichiji) {
        this.nikaimeyouzaikeiryounichiji = nikaimeyouzaikeiryounichiji;
    }

    /**
     * 2回目溶剤①_材料品名
     * @return the nikaimeyouzai1_zairyouhinmei
     */
    public String getNikaimeyouzai1_zairyouhinmei() {
        return nikaimeyouzai1_zairyouhinmei;
    }

    /**
     * 2回目溶剤①_材料品名
     * @param nikaimeyouzai1_zairyouhinmei the nikaimeyouzai1_zairyouhinmei to set
     */
    public void setNikaimeyouzai1_zairyouhinmei(String nikaimeyouzai1_zairyouhinmei) {
        this.nikaimeyouzai1_zairyouhinmei = nikaimeyouzai1_zairyouhinmei;
    }

    /**
     * 2回目溶剤①_調合量規格
     * @return the nikaimeyouzai1_tyougouryoukikaku
     */
    public String getNikaimeyouzai1_tyougouryoukikaku() {
        return nikaimeyouzai1_tyougouryoukikaku;
    }

    /**
     * 2回目溶剤①_調合量規格
     * @param nikaimeyouzai1_tyougouryoukikaku the nikaimeyouzai1_tyougouryoukikaku to set
     */
    public void setNikaimeyouzai1_tyougouryoukikaku(String nikaimeyouzai1_tyougouryoukikaku) {
        this.nikaimeyouzai1_tyougouryoukikaku = nikaimeyouzai1_tyougouryoukikaku;
    }

    /**
     * 2回目溶剤①_部材在庫No1
     * @return the nikaimeyouzai1_buzaizaikolotno1
     */
    public String getNikaimeyouzai1_buzaizaikolotno1() {
        return nikaimeyouzai1_buzaizaikolotno1;
    }

    /**
     * 2回目溶剤①_部材在庫No1
     * @param nikaimeyouzai1_buzaizaikolotno1 the nikaimeyouzai1_buzaizaikolotno1 to set
     */
    public void setNikaimeyouzai1_buzaizaikolotno1(String nikaimeyouzai1_buzaizaikolotno1) {
        this.nikaimeyouzai1_buzaizaikolotno1 = nikaimeyouzai1_buzaizaikolotno1;
    }

    /**
     * 2回目溶剤①_調合量1
     * @return the nikaimeyouzai1_tyougouryou1
     */
    public Integer getNikaimeyouzai1_tyougouryou1() {
        return nikaimeyouzai1_tyougouryou1;
    }

    /**
     * 2回目溶剤①_調合量1
     * @param nikaimeyouzai1_tyougouryou1 the nikaimeyouzai1_tyougouryou1 to set
     */
    public void setNikaimeyouzai1_tyougouryou1(Integer nikaimeyouzai1_tyougouryou1) {
        this.nikaimeyouzai1_tyougouryou1 = nikaimeyouzai1_tyougouryou1;
    }

    /**
     * 2回目溶剤①_部材在庫No2
     * @return the nikaimeyouzai1_buzaizaikolotno2
     */
    public String getNikaimeyouzai1_buzaizaikolotno2() {
        return nikaimeyouzai1_buzaizaikolotno2;
    }

    /**
     * 2回目溶剤①_部材在庫No2
     * @param nikaimeyouzai1_buzaizaikolotno2 the nikaimeyouzai1_buzaizaikolotno2 to set
     */
    public void setNikaimeyouzai1_buzaizaikolotno2(String nikaimeyouzai1_buzaizaikolotno2) {
        this.nikaimeyouzai1_buzaizaikolotno2 = nikaimeyouzai1_buzaizaikolotno2;
    }

    /**
     * 2回目溶剤①_調合量2
     * @return the nikaimeyouzai1_tyougouryou2
     */
    public Integer getNikaimeyouzai1_tyougouryou2() {
        return nikaimeyouzai1_tyougouryou2;
    }

    /**
     * 2回目溶剤①_調合量2
     * @param nikaimeyouzai1_tyougouryou2 the nikaimeyouzai1_tyougouryou2 to set
     */
    public void setNikaimeyouzai1_tyougouryou2(Integer nikaimeyouzai1_tyougouryou2) {
        this.nikaimeyouzai1_tyougouryou2 = nikaimeyouzai1_tyougouryou2;
    }

    /**
     * 2回目溶剤②_材料品名
     * @return the nikaimeyouzai2_zairyouhinmei
     */
    public String getNikaimeyouzai2_zairyouhinmei() {
        return nikaimeyouzai2_zairyouhinmei;
    }

    /**
     * 2回目溶剤②_材料品名
     * @param nikaimeyouzai2_zairyouhinmei the nikaimeyouzai2_zairyouhinmei to set
     */
    public void setNikaimeyouzai2_zairyouhinmei(String nikaimeyouzai2_zairyouhinmei) {
        this.nikaimeyouzai2_zairyouhinmei = nikaimeyouzai2_zairyouhinmei;
    }

    /**
     * 2回目溶剤②_調合量規格
     * @return the nikaimeyouzai2_tyougouryoukikaku
     */
    public String getNikaimeyouzai2_tyougouryoukikaku() {
        return nikaimeyouzai2_tyougouryoukikaku;
    }

    /**
     * 2回目溶剤②_調合量規格
     * @param nikaimeyouzai2_tyougouryoukikaku the nikaimeyouzai2_tyougouryoukikaku to set
     */
    public void setNikaimeyouzai2_tyougouryoukikaku(String nikaimeyouzai2_tyougouryoukikaku) {
        this.nikaimeyouzai2_tyougouryoukikaku = nikaimeyouzai2_tyougouryoukikaku;
    }

    /**
     * 2回目溶剤②_部材在庫No1
     * @return the nikaimeyouzai2_buzaizaikolotno1
     */
    public String getNikaimeyouzai2_buzaizaikolotno1() {
        return nikaimeyouzai2_buzaizaikolotno1;
    }

    /**
     * 2回目溶剤②_部材在庫No1
     * @param nikaimeyouzai2_buzaizaikolotno1 the nikaimeyouzai2_buzaizaikolotno1 to set
     */
    public void setNikaimeyouzai2_buzaizaikolotno1(String nikaimeyouzai2_buzaizaikolotno1) {
        this.nikaimeyouzai2_buzaizaikolotno1 = nikaimeyouzai2_buzaizaikolotno1;
    }

    /**
     * 2回目溶剤②_調合量1
     * @return the nikaimeyouzai2_tyougouryou1
     */
    public Integer getNikaimeyouzai2_tyougouryou1() {
        return nikaimeyouzai2_tyougouryou1;
    }

    /**
     * 2回目溶剤②_調合量1
     * @param nikaimeyouzai2_tyougouryou1 the nikaimeyouzai2_tyougouryou1 to set
     */
    public void setNikaimeyouzai2_tyougouryou1(Integer nikaimeyouzai2_tyougouryou1) {
        this.nikaimeyouzai2_tyougouryou1 = nikaimeyouzai2_tyougouryou1;
    }

    /**
     * 2回目溶剤②_部材在庫No2
     * @return the nikaimeyouzai2_buzaizaikolotno2
     */
    public String getNikaimeyouzai2_buzaizaikolotno2() {
        return nikaimeyouzai2_buzaizaikolotno2;
    }

    /**
     * 2回目溶剤②_部材在庫No2
     * @param nikaimeyouzai2_buzaizaikolotno2 the nikaimeyouzai2_buzaizaikolotno2 to set
     */
    public void setNikaimeyouzai2_buzaizaikolotno2(String nikaimeyouzai2_buzaizaikolotno2) {
        this.nikaimeyouzai2_buzaizaikolotno2 = nikaimeyouzai2_buzaizaikolotno2;
    }

    /**
     * 2回目溶剤②_調合量2
     * @return the nikaimeyouzai2_tyougouryou2
     */
    public Integer getNikaimeyouzai2_tyougouryou2() {
        return nikaimeyouzai2_tyougouryou2;
    }

    /**
     * 2回目溶剤②_調合量2
     * @param nikaimeyouzai2_tyougouryou2 the nikaimeyouzai2_tyougouryou2 to set
     */
    public void setNikaimeyouzai2_tyougouryou2(Integer nikaimeyouzai2_tyougouryou2) {
        this.nikaimeyouzai2_tyougouryou2 = nikaimeyouzai2_tyougouryou2;
    }

    /**
     * 2回目担当者
     * @return the nikaimetantousya
     */
    public String getNikaimetantousya() {
        return nikaimetantousya;
    }

    /**
     * 2回目担当者
     * @param nikaimetantousya the nikaimetantousya to set
     */
    public void setNikaimetantousya(String nikaimetantousya) {
        this.nikaimetantousya = nikaimetantousya;
    }

    /**
     * 3回目撹拌設備
     * @return the sankaimekakuhansetubi
     */
    public String getSankaimekakuhansetubi() {
        return sankaimekakuhansetubi;
    }

    /**
     * 3回目撹拌設備
     * @param sankaimekakuhansetubi the sankaimekakuhansetubi to set
     */
    public void setSankaimekakuhansetubi(String sankaimekakuhansetubi) {
        this.sankaimekakuhansetubi = sankaimekakuhansetubi;
    }

    /**
     * 3回目撹拌時間
     * @return the sankaimekakuhanjikan
     */
    public String getSankaimekakuhanjikan() {
        return sankaimekakuhanjikan;
    }

    /**
     * 3回目撹拌時間
     * @param sankaimekakuhanjikan the sankaimekakuhanjikan to set
     */
    public void setSankaimekakuhanjikan(String sankaimekakuhanjikan) {
        this.sankaimekakuhanjikan = sankaimekakuhanjikan;
    }

    /**
     * 3回目撹拌開始日時
     * @return the sankaimekakuhankaisinichiji
     */
    public Timestamp getSankaimekakuhankaisinichiji() {
        return sankaimekakuhankaisinichiji;
    }

    /**
     * 3回目撹拌開始日時
     * @param sankaimekakuhankaisinichiji the sankaimekakuhankaisinichiji to set
     */
    public void setSankaimekakuhankaisinichiji(Timestamp sankaimekakuhankaisinichiji) {
        this.sankaimekakuhankaisinichiji = sankaimekakuhankaisinichiji;
    }

    /**
     * 3回目撹拌終了日時
     * @return the sankaimekakuhansyuuryounichiji
     */
    public Timestamp getSankaimekakuhansyuuryounichiji() {
        return sankaimekakuhansyuuryounichiji;
    }

    /**
     * 3回目撹拌終了日時
     * @param sankaimekakuhansyuuryounichiji the sankaimekakuhansyuuryounichiji to set
     */
    public void setSankaimekakuhansyuuryounichiji(Timestamp sankaimekakuhansyuuryounichiji) {
        this.sankaimekakuhansyuuryounichiji = sankaimekakuhansyuuryounichiji;
    }

    /**
     * 3回目脱脂皿の種類
     * @return the sankaimedassizaranosyurui
     */
    public String getSankaimedassizaranosyurui() {
        return sankaimedassizaranosyurui;
    }

    /**
     * 3回目脱脂皿の種類
     * @param sankaimedassizaranosyurui the sankaimedassizaranosyurui to set
     */
    public void setSankaimedassizaranosyurui(String sankaimedassizaranosyurui) {
        this.sankaimedassizaranosyurui = sankaimedassizaranosyurui;
    }

    /**
     * 3回目ｱﾙﾐ皿風袋重量
     * @return the sankaimearumizarafuutaijyuuryou
     */
    public BigDecimal getSankaimearumizarafuutaijyuuryou() {
        return sankaimearumizarafuutaijyuuryou;
    }

    /**
     * 3回目ｱﾙﾐ皿風袋重量
     * @param sankaimearumizarafuutaijyuuryou the sankaimearumizarafuutaijyuuryou to set
     */
    public void setSankaimearumizarafuutaijyuuryou(BigDecimal sankaimearumizarafuutaijyuuryou) {
        this.sankaimearumizarafuutaijyuuryou = sankaimearumizarafuutaijyuuryou;
    }

    /**
     * 3回目乾燥前ｽﾗﾘｰ重量
     * @return the sankaimekansoumaeslurryjyuuryou
     */
    public BigDecimal getSankaimekansoumaeslurryjyuuryou() {
        return sankaimekansoumaeslurryjyuuryou;
    }

    /**
     * 3回目乾燥前ｽﾗﾘｰ重量
     * @param sankaimekansoumaeslurryjyuuryou the sankaimekansoumaeslurryjyuuryou to set
     */
    public void setSankaimekansoumaeslurryjyuuryou(BigDecimal sankaimekansoumaeslurryjyuuryou) {
        this.sankaimekansoumaeslurryjyuuryou = sankaimekansoumaeslurryjyuuryou;
    }

    /**
     * 3回目乾燥機
     * @return the sankaimekansouki
     */
    public String getSankaimekansouki() {
        return sankaimekansouki;
    }

    /**
     * 3回目乾燥機
     * @param sankaimekansouki the sankaimekansouki to set
     */
    public void setSankaimekansouki(String sankaimekansouki) {
        this.sankaimekansouki = sankaimekansouki;
    }

    /**
     * 3回目乾燥温度
     * @return the sankaimekansouondo
     */
    public String getSankaimekansouondo() {
        return sankaimekansouondo;
    }

    /**
     * 3回目乾燥温度
     * @param sankaimekansouondo the sankaimekansouondo to set
     */
    public void setSankaimekansouondo(String sankaimekansouondo) {
        this.sankaimekansouondo = sankaimekansouondo;
    }

    /**
     * 3回目乾燥時間
     * @return the sankaimekansoujikan
     */
    public String getSankaimekansoujikan() {
        return sankaimekansoujikan;
    }

    /**
     * 3回目乾燥時間
     * @param sankaimekansoujikan the sankaimekansoujikan to set
     */
    public void setSankaimekansoujikan(String sankaimekansoujikan) {
        this.sankaimekansoujikan = sankaimekansoujikan;
    }

    /**
     * 3回目乾燥開始日時
     * @return the sankaimekansoukaisinichij
     */
    public Timestamp getSankaimekansoukaisinichij() {
        return sankaimekansoukaisinichij;
    }

    /**
     * 3回目乾燥開始日時
     * @param sankaimekansoukaisinichij the sankaimekansoukaisinichij to set
     */
    public void setSankaimekansoukaisinichij(Timestamp sankaimekansoukaisinichij) {
        this.sankaimekansoukaisinichij = sankaimekansoukaisinichij;
    }

    /**
     * 3回目乾燥終了日時
     * @return the sankaimekansousyuuryounichiji
     */
    public Timestamp getSankaimekansousyuuryounichiji() {
        return sankaimekansousyuuryounichiji;
    }

    /**
     * 3回目乾燥終了日時
     * @param sankaimekansousyuuryounichiji the sankaimekansousyuuryounichiji to set
     */
    public void setSankaimekansousyuuryounichiji(Timestamp sankaimekansousyuuryounichiji) {
        this.sankaimekansousyuuryounichiji = sankaimekansousyuuryounichiji;
    }

    /**
     * 3回目乾燥後総重量
     * @return the sankaimekansougosoujyuuryou
     */
    public BigDecimal getSankaimekansougosoujyuuryou() {
        return sankaimekansougosoujyuuryou;
    }

    /**
     * 3回目乾燥後総重量
     * @param sankaimekansougosoujyuuryou the sankaimekansougosoujyuuryou to set
     */
    public void setSankaimekansougosoujyuuryou(BigDecimal sankaimekansougosoujyuuryou) {
        this.sankaimekansougosoujyuuryou = sankaimekansougosoujyuuryou;
    }

    /**
     * 3回目乾燥後正味重量
     * @return the sankaimekansougosyoumijyuuryou
     */
    public BigDecimal getSankaimekansougosyoumijyuuryou() {
        return sankaimekansougosyoumijyuuryou;
    }

    /**
     * 3回目乾燥後正味重量
     * @param sankaimekansougosyoumijyuuryou the sankaimekansougosyoumijyuuryou to set
     */
    public void setSankaimekansougosyoumijyuuryou(BigDecimal sankaimekansougosyoumijyuuryou) {
        this.sankaimekansougosyoumijyuuryou = sankaimekansougosyoumijyuuryou;
    }

    /**
     * 3回目固形分比率
     * @return the sankaimekokeibunhiritu
     */
    public BigDecimal getSankaimekokeibunhiritu() {
        return sankaimekokeibunhiritu;
    }

    /**
     * 3回目固形分比率
     * @param sankaimekokeibunhiritu the sankaimekokeibunhiritu to set
     */
    public void setSankaimekokeibunhiritu(BigDecimal sankaimekokeibunhiritu) {
        this.sankaimekokeibunhiritu = sankaimekokeibunhiritu;
    }

    /**
     * 3回目固形分測定担当者
     * @return the sankaimekokeibunsokuteitantousya
     */
    public String getSankaimekokeibunsokuteitantousya() {
        return sankaimekokeibunsokuteitantousya;
    }

    /**
     * 3回目固形分測定担当者
     * @param sankaimekokeibunsokuteitantousya the sankaimekokeibunsokuteitantousya to set
     */
    public void setSankaimekokeibunsokuteitantousya(String sankaimekokeibunsokuteitantousya) {
        this.sankaimekokeibunsokuteitantousya = sankaimekokeibunsokuteitantousya;
    }

    /**
     * F/P準備_風袋重量
     * @return the fpjyunbi_fuutaijyuuryou
     */
    public Integer getFpjyunbi_fuutaijyuuryou() {
        return fpjyunbi_fuutaijyuuryou;
    }

    /**
     * F/P準備_風袋重量
     * @param fpjyunbi_fuutaijyuuryou the fpjyunbi_fuutaijyuuryou to set
     */
    public void setFpjyunbi_fuutaijyuuryou(Integer fpjyunbi_fuutaijyuuryou) {
        this.fpjyunbi_fuutaijyuuryou = fpjyunbi_fuutaijyuuryou;
    }

    /**
     * F/P準備_担当者
     * @return the fpjyunbi_tantousya
     */
    public String getFpjyunbi_tantousya() {
        return fpjyunbi_tantousya;
    }

    /**
     * F/P準備_担当者
     * @param fpjyunbi_tantousya the fpjyunbi_tantousya to set
     */
    public void setFpjyunbi_tantousya(String fpjyunbi_tantousya) {
        this.fpjyunbi_tantousya = fpjyunbi_tantousya;
    }

    /**
     * ﾌｨﾙﾀｰ連結
     * @return the filterrenketu
     */
    public String getFilterrenketu() {
        return filterrenketu;
    }

    /**
     * ﾌｨﾙﾀｰ連結
     * @param filterrenketu the filterrenketu to set
     */
    public void setFilterrenketu(String filterrenketu) {
        this.filterrenketu = filterrenketu;
    }

    /**
     * F/P準備_ﾌｨﾙﾀｰ品名①
     * @return the fpjyunbi_filterhinmei1
     */
    public String getFpjyunbi_filterhinmei1() {
        return fpjyunbi_filterhinmei1;
    }

    /**
     * F/P準備_ﾌｨﾙﾀｰ品名①
     * @param fpjyunbi_filterhinmei1 the fpjyunbi_filterhinmei1 to set
     */
    public void setFpjyunbi_filterhinmei1(String fpjyunbi_filterhinmei1) {
        this.fpjyunbi_filterhinmei1 = fpjyunbi_filterhinmei1;
    }

    /**
     * F/P準備_LotNo①
     * @return the fpjyunbi_lotno1
     */
    public String getFpjyunbi_lotno1() {
        return fpjyunbi_lotno1;
    }

    /**
     * F/P準備_LotNo①
     * @param fpjyunbi_lotno1 the fpjyunbi_lotno1 to set
     */
    public void setFpjyunbi_lotno1(String fpjyunbi_lotno1) {
        this.fpjyunbi_lotno1 = fpjyunbi_lotno1;
    }

    /**
     * F/P準備_取り付け本数①
     * @return the fpjyunbi_toritukehonsuu1
     */
    public Integer getFpjyunbi_toritukehonsuu1() {
        return fpjyunbi_toritukehonsuu1;
    }

    /**
     * F/P準備_取り付け本数①
     * @param fpjyunbi_toritukehonsuu1 the fpjyunbi_toritukehonsuu1 to set
     */
    public void setFpjyunbi_toritukehonsuu1(Integer fpjyunbi_toritukehonsuu1) {
        this.fpjyunbi_toritukehonsuu1 = fpjyunbi_toritukehonsuu1;
    }

    /**
     * F/P準備_ﾌｨﾙﾀｰ品名②
     * @return the fpjyunbi_filterhinmei2
     */
    public String getFpjyunbi_filterhinmei2() {
        return fpjyunbi_filterhinmei2;
    }

    /**
     * F/P準備_ﾌｨﾙﾀｰ品名②
     * @param fpjyunbi_filterhinmei2 the fpjyunbi_filterhinmei2 to set
     */
    public void setFpjyunbi_filterhinmei2(String fpjyunbi_filterhinmei2) {
        this.fpjyunbi_filterhinmei2 = fpjyunbi_filterhinmei2;
    }

    /**
     * F/P準備_LotNo②
     * @return the fpjyunbi_lotno2
     */
    public String getFpjyunbi_lotno2() {
        return fpjyunbi_lotno2;
    }

    /**
     * F/P準備_LotNo②
     * @param fpjyunbi_lotno2 the fpjyunbi_lotno2 to set
     */
    public void setFpjyunbi_lotno2(String fpjyunbi_lotno2) {
        this.fpjyunbi_lotno2 = fpjyunbi_lotno2;
    }

    /**
     * F/P準備_取り付け本数②
     * @return the fpjyunbi_toritukehonsuu2
     */
    public Integer getFpjyunbi_toritukehonsuu2() {
        return fpjyunbi_toritukehonsuu2;
    }

    /**
     * F/P準備_取り付け本数②
     * @param fpjyunbi_toritukehonsuu2 the fpjyunbi_toritukehonsuu2 to set
     */
    public void setFpjyunbi_toritukehonsuu2(Integer fpjyunbi_toritukehonsuu2) {
        this.fpjyunbi_toritukehonsuu2 = fpjyunbi_toritukehonsuu2;
    }

    /**
     * 圧送ﾀﾝｸの洗浄
     * @return the assoutanknosenjyou
     */
    public Integer getAssoutanknosenjyou() {
        return assoutanknosenjyou;
    }

    /**
     * 圧送ﾀﾝｸの洗浄
     * @param assoutanknosenjyou the assoutanknosenjyou to set
     */
    public void setAssoutanknosenjyou(Integer assoutanknosenjyou) {
        this.assoutanknosenjyou = assoutanknosenjyou;
    }

    /**
     * 排出容器の内袋
     * @return the haisyutuyoukinoutibukuro
     */
    public Integer getHaisyutuyoukinoutibukuro() {
        return haisyutuyoukinoutibukuro;
    }

    /**
     * 排出容器の内袋
     * @param haisyutuyoukinoutibukuro the haisyutuyoukinoutibukuro to set
     */
    public void setHaisyutuyoukinoutibukuro(Integer haisyutuyoukinoutibukuro) {
        this.haisyutuyoukinoutibukuro = haisyutuyoukinoutibukuro;
    }

    /**
     * F/PﾀﾝｸNo
     * @return the fptankno
     */
    public Integer getFptankno() {
        return fptankno;
    }

    /**
     * F/PﾀﾝｸNo
     * @param fptankno the fptankno to set
     */
    public void setFptankno(Integer fptankno) {
        this.fptankno = fptankno;
    }

    /**
     * F/P開始日時
     * @return the fpkaisinichiji
     */
    public Timestamp getFpkaisinichiji() {
        return fpkaisinichiji;
    }

    /**
     * F/P開始日時
     * @param fpkaisinichiji the fpkaisinichiji to set
     */
    public void setFpkaisinichiji(Timestamp fpkaisinichiji) {
        this.fpkaisinichiji = fpkaisinichiji;
    }

    /**
     * 圧送ﾚｷﾞｭﾚｰﾀｰNo
     * @return the assouregulatorno
     */
    public Integer getAssouregulatorno() {
        return assouregulatorno;
    }

    /**
     * 圧送ﾚｷﾞｭﾚｰﾀｰNo
     * @param assouregulatorno the assouregulatorno to set
     */
    public void setAssouregulatorno(Integer assouregulatorno) {
        this.assouregulatorno = assouregulatorno;
    }

    /**
     * 圧送圧力
     * @return the assouaturyoku
     */
    public BigDecimal getAssouaturyoku() {
        return assouaturyoku;
    }

    /**
     * 圧送圧力
     * @param assouaturyoku the assouaturyoku to set
     */
    public void setAssouaturyoku(BigDecimal assouaturyoku) {
        this.assouaturyoku = assouaturyoku;
    }

    /**
     * F/P開始_担当者
     * @return the fpkaisi_tantousya
     */
    public String getFpkaisi_tantousya() {
        return fpkaisi_tantousya;
    }

    /**
     * F/P開始_担当者
     * @param fpkaisi_tantousya the fpkaisi_tantousya to set
     */
    public void setFpkaisi_tantousya(String fpkaisi_tantousya) {
        this.fpkaisi_tantousya = fpkaisi_tantousya;
    }

    /**
     * F/P停止日時
     * @return the fpteisinichiji
     */
    public Timestamp getFpteisinichiji() {
        return fpteisinichiji;
    }

    /**
     * F/P停止日時
     * @param fpteisinichiji the fpteisinichiji to set
     */
    public void setFpteisinichiji(Timestamp fpteisinichiji) {
        this.fpteisinichiji = fpteisinichiji;
    }

    /**
     * F/P交換_ﾌｨﾙﾀｰ品名①
     * @return the fpkoukan_filterhinmei1
     */
    public String getFpkoukan_filterhinmei1() {
        return fpkoukan_filterhinmei1;
    }

    /**
     * F/P交換_ﾌｨﾙﾀｰ品名①
     * @param fpkoukan_filterhinmei1 the fpkoukan_filterhinmei1 to set
     */
    public void setFpkoukan_filterhinmei1(String fpkoukan_filterhinmei1) {
        this.fpkoukan_filterhinmei1 = fpkoukan_filterhinmei1;
    }

    /**
     * F/P交換_LotNo①
     * @return the fpkoukan_lotno1
     */
    public String getFpkoukan_lotno1() {
        return fpkoukan_lotno1;
    }

    /**
     * F/P交換_LotNo①
     * @param fpkoukan_lotno1 the fpkoukan_lotno1 to set
     */
    public void setFpkoukan_lotno1(String fpkoukan_lotno1) {
        this.fpkoukan_lotno1 = fpkoukan_lotno1;
    }

    /**
     * F/P交換_取り付け本数①
     * @return the fpkoukan_toritukehonsuu1
     */
    public Integer getFpkoukan_toritukehonsuu1() {
        return fpkoukan_toritukehonsuu1;
    }

    /**
     * F/P交換_取り付け本数①
     * @param fpkoukan_toritukehonsuu1 the fpkoukan_toritukehonsuu1 to set
     */
    public void setFpkoukan_toritukehonsuu1(Integer fpkoukan_toritukehonsuu1) {
        this.fpkoukan_toritukehonsuu1 = fpkoukan_toritukehonsuu1;
    }

    /**
     * F/P交換_ﾌｨﾙﾀｰ品名②
     * @return the fpkoukan_filterhinmei2
     */
    public String getFpkoukan_filterhinmei2() {
        return fpkoukan_filterhinmei2;
    }

    /**
     * F/P交換_ﾌｨﾙﾀｰ品名②
     * @param fpkoukan_filterhinmei2 the fpkoukan_filterhinmei2 to set
     */
    public void setFpkoukan_filterhinmei2(String fpkoukan_filterhinmei2) {
        this.fpkoukan_filterhinmei2 = fpkoukan_filterhinmei2;
    }

    /**
     * F/P交換_LotNo②
     * @return the fpkoukan_lotno2
     */
    public String getFpkoukan_lotno2() {
        return fpkoukan_lotno2;
    }

    /**
     * F/P交換_LotNo②
     * @param fpkoukan_lotno2 the fpkoukan_lotno2 to set
     */
    public void setFpkoukan_lotno2(String fpkoukan_lotno2) {
        this.fpkoukan_lotno2 = fpkoukan_lotno2;
    }

    /**
     * F/P交換_取り付け本数②
     * @return the fpkoukan_toritukehonsuu2
     */
    public Integer getFpkoukan_toritukehonsuu2() {
        return fpkoukan_toritukehonsuu2;
    }

    /**
     * F/P交換_取り付け本数②
     * @param fpkoukan_toritukehonsuu2 the fpkoukan_toritukehonsuu2 to set
     */
    public void setFpkoukan_toritukehonsuu2(Integer fpkoukan_toritukehonsuu2) {
        this.fpkoukan_toritukehonsuu2 = fpkoukan_toritukehonsuu2;
    }

    /**
     * F/P再開日時
     * @return the fpsaikainichiji
     */
    public Timestamp getFpsaikainichiji() {
        return fpsaikainichiji;
    }

    /**
     * F/P再開日時
     * @param fpsaikainichiji the fpsaikainichiji to set
     */
    public void setFpsaikainichiji(Timestamp fpsaikainichiji) {
        this.fpsaikainichiji = fpsaikainichiji;
    }

    /**
     * F/P交換_担当者
     * @return the fpkoukan_tantousya
     */
    public String getFpkoukan_tantousya() {
        return fpkoukan_tantousya;
    }

    /**
     * F/P交換_担当者
     * @param fpkoukan_tantousya the fpkoukan_tantousya to set
     */
    public void setFpkoukan_tantousya(String fpkoukan_tantousya) {
        this.fpkoukan_tantousya = fpkoukan_tantousya;
    }

    /**
     * F/P終了日時
     * @return the fpsyuuryounichiji
     */
    public Timestamp getFpsyuuryounichiji() {
        return fpsyuuryounichiji;
    }

    /**
     * F/P終了日時
     * @param fpsyuuryounichiji the fpsyuuryounichiji to set
     */
    public void setFpsyuuryounichiji(Timestamp fpsyuuryounichiji) {
        this.fpsyuuryounichiji = fpsyuuryounichiji;
    }

    /**
     * F/P時間
     * @return the fpjikan
     */
    public Integer getFpjikan() {
        return fpjikan;
    }

    /**
     * F/P時間
     * @param fpjikan the fpjikan to set
     */
    public void setFpjikan(Integer fpjikan) {
        this.fpjikan = fpjikan;
    }

    /**
     * F/P終了_担当者
     * @return the fpsyuurou_tantousya
     */
    public String getFpsyuurou_tantousya() {
        return fpsyuurou_tantousya;
    }

    /**
     * F/P終了_担当者
     * @param fpsyuurou_tantousya the fpsyuurou_tantousya to set
     */
    public void setFpsyuurou_tantousya(String fpsyuurou_tantousya) {
        this.fpsyuurou_tantousya = fpsyuurou_tantousya;
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
    public BigDecimal getSyoumijyuuryou() {
        return syoumijyuuryou;
    }

    /**
     * 正味重量
     * @param syoumijyuuryou the syoumijyuuryou to set
     */
    public void setSyoumijyuuryou(BigDecimal syoumijyuuryou) {
        this.syoumijyuuryou = syoumijyuuryou;
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
