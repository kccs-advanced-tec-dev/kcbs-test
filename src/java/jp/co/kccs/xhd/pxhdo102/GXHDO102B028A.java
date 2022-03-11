/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo102;

import java.io.Serializable;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import jp.co.kccs.xhd.db.model.FXHDD01;

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
 * GXHDO102B028(ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(ｽﾃﾝ容器)))
 *
 * @author KCSS K.Jo
 * @since 2021/12/06
 */
@ViewScoped
@Named
public class GXHDO102B028A implements Serializable {

    /**
     * WIPﾛｯﾄNo
     */
    private FXHDD01 wiplotno;

    /**
     * ｽﾘｯﾌﾟ品名
     */
    private FXHDD01 sliphinmei;

    /**
     * ｽﾘｯﾌﾟLotNo
     */
    private FXHDD01 sliplotno;

    /**
     * ﾛｯﾄ区分
     */
    private FXHDD01 lotkubun;

    /**
     * 原料記号
     */
    private FXHDD01 genryoukigou;

    /**
     * 秤量号機
     */
    private FXHDD01 goki;

    /**
     * ｽﾗﾘｰ品名
     */
    private FXHDD01 slurryhinmei;

    /**
     * ｽﾗﾘｰLotNo
     */
    private FXHDD01 slurrylotno;

    /**
     * 容器No
     */
    private FXHDD01 youkino;

    /**
     * ｽﾗﾘｰ有効期限
     */
    private FXHDD01 slurryyuukoukigen;

    /**
     * 乾燥固形分
     */
    private FXHDD01 kansoukokeibun;

    /**
     * 脱脂固形分
     */
    private FXHDD01 dassikokeibun;

    /**
     * 粉砕終了日
     */
    private FXHDD01 funsaisyuuryou_day;

    /**
     * 粉砕終了時間
     */
    private FXHDD01 funsaisyuuryou_time;

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合日
     */
    private FXHDD01 binderkongou_day;

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合時間
     */
    private FXHDD01 binderkongou_time;

    /**
     * ｽﾗﾘｰｴｰｼﾞﾝｸﾞ
     */
    private FXHDD01 slurraging;

    /**
     * ｽﾗﾘｰ経過日数
     */
    private FXHDD01 slurrykeikanisuu;

    /**
     * ｽﾘｯﾌﾟ情報_風袋重量
     */
    private FXHDD01 slipjyouhou_fuutaijyuuryou;

    /**
     * 総重量
     */
    private FXHDD01 slurrysoujyuuryou;

    /**
     * ｽﾗﾘｰ重量
     */
    private FXHDD01 slurryjyuuryou;

    /**
     * 撹拌設備
     */
    private FXHDD01 kakuhansetubi;

    /**
     * 撹拌号機
     */
    private FXHDD01 kakuhangoki;

    /**
     * 撹拌回転数
     */
    private FXHDD01 kakuhankaitensuu;

    /**
     * 撹拌時間
     */
    private FXHDD01 kakuhanjikan;

    /**
     * 撹拌開始日
     */
    private FXHDD01 kakuhankaisi_day;

    /**
     * 撹拌開始時間
     */
    private FXHDD01 kakuhankaisi_time;

    /**
     * 撹拌終了日
     */
    private FXHDD01 kakuhansyuuryou_day;

    /**
     * 撹拌終了時間
     */
    private FXHDD01 kakuhansyuuryou_time;

    /**
     * 脱脂皿の種類
     */
    private FXHDD01 dassizaranosyurui;

    /**
     * 固形分測定_風袋重量
     */
    private FXHDD01 kkbskt_fuutaijyuuryou;

    /**
     * 乾燥前ｽﾗﾘｰ重量
     */
    private FXHDD01 kansoumaeslurryjyuuryou;

    /**
     * 乾燥機
     */
    private FXHDD01 kansouki;

    /**
     * 乾燥温度
     */
    private FXHDD01 kansouondo;

    /**
     * 乾燥時間
     */
    private FXHDD01 kansoujikan;

    /**
     * 乾燥開始日
     */
    private FXHDD01 kansoukaisi_day;

    /**
     * 乾燥開始時間
     */
    private FXHDD01 kansoukaisi_time;

    /**
     * 乾燥終了日
     */
    private FXHDD01 kansousyuuryou_day;

    /**
     * 乾燥終了時間
     */
    private FXHDD01 kansousyuuryou_time;

    /**
     * 乾燥後総重量
     */
    private FXHDD01 kansougosoujyuuryou;

    /**
     * 乾燥後正味重量
     */
    private FXHDD01 kansougosyoumijyuuryou;

    /**
     * 固形分比率
     */
    private FXHDD01 kokeibunhiritu;

    /**
     * 固形分測定担当者
     */
    private FXHDD01 kokeibunsokuteitantousya;

    /**
     * 溶剤調整量
     */
    private FXHDD01 youzaityouseiryou;

    /**
     * ﾄﾙｴﾝ調整量
     */
    private FXHDD01 toluenetyouseiryou;

    /**
     * ｿﾙﾐｯｸｽ調整量
     */
    private FXHDD01 solmixtyouseiryou;

    /**
     * 溶剤秤量日
     */
    private FXHDD01 youzaikeiryou_day;

    /**
     * 溶剤秤量時間
     */
    private FXHDD01 youzaikeiryou_time;

    /**
     * 溶剤①_材料品名
     */
    private FXHDD01 youzai1_zairyouhinmei;

    /**
     * 溶剤①_調合量規格
     */
    private FXHDD01 youzai1_tyougouryoukikaku;

    /**
     * 溶剤①_部材在庫No1
     */
    private FXHDD01 youzai1_buzaizaikolotno1;

    /**
     * 溶剤①_調合量1
     */
    private FXHDD01 youzai1_tyougouryou1;

    /**
     * 溶剤①_部材在庫No2
     */
    private FXHDD01 youzai1_buzaizaikolotno2;

    /**
     * 溶剤①_調合量2
     */
    private FXHDD01 youzai1_tyougouryou2;

    /**
     * 溶剤②_材料品名
     */
    private FXHDD01 youzai2_zairyouhinmei;

    /**
     * 溶剤②_調合量規格
     */
    private FXHDD01 youzai2_tyougouryoukikaku;

    /**
     * 溶剤②_部材在庫No1
     */
    private FXHDD01 youzai2_buzaizaikolotno1;

    /**
     * 溶剤②_調合量1
     */
    private FXHDD01 youzai2_tyougouryou1;

    /**
     * 溶剤②_部材在庫No2
     */
    private FXHDD01 youzai2_buzaizaikolotno2;

    /**
     * 溶剤②_調合量2
     */
    private FXHDD01 youzai2_tyougouryou2;

    /**
     * 担当者
     */
    private FXHDD01 tantousya;

    /**
     * 2回目撹拌設備
     */
    private FXHDD01 nikaimekakuhansetubi;

    /**
     * 2回目撹拌時間
     */
    private FXHDD01 nikaimekakuhanjikan;

    /**
     * 2回目撹拌開始日
     */
    private FXHDD01 nikaimekakuhankaisi_day;

    /**
     * 2回目撹拌開始時間
     */
    private FXHDD01 nikaimekakuhankaisi_time;

    /**
     * 2回目撹拌終了日
     */
    private FXHDD01 nkmkkhsr_day;

    /**
     * 2回目撹拌終了時間
     */
    private FXHDD01 nkmkkhsr_time;

    /**
     * 2回目脱脂皿の種類
     */
    private FXHDD01 nikaimedassizaranosyurui;

    /**
     * 2回目ｱﾙﾐ皿風袋重量
     */
    private FXHDD01 nkmarmzftjr;

    /**
     * 2回目乾燥前ｽﾗﾘｰ重量
     */
    private FXHDD01 nkmksmslrjr;

    /**
     * 2回目乾燥機
     */
    private FXHDD01 nikaimekansouki;

    /**
     * 2回目乾燥温度
     */
    private FXHDD01 nikaimekansouondo;

    /**
     * 2回目乾燥時間
     */
    private FXHDD01 nikaimekansoujikan;

    /**
     * 2回目乾燥開始日
     */
    private FXHDD01 nikaimekansoukaisi_day;

    /**
     * 2回目乾燥開始時間
     */
    private FXHDD01 nikaimekansoukaisi_time;

    /**
     * 2回目乾燥終了日
     */
    private FXHDD01 nikaimekansousyuuryou_day;

    /**
     * 2回目乾燥終了時間
     */
    private FXHDD01 nikaimekansousyuuryou_time;

    /**
     * 2回目乾燥後総重量
     */
    private FXHDD01 nikaimekansougosoujyuuryou;

    /**
     * 2回目乾燥後正味重量
     */
    private FXHDD01 nkmksgsmjr;

    /**
     * 2回目固形分比率
     */
    private FXHDD01 nikaimekokeibunhiritu;

    /**
     * 2回目固形分測定担当者
     */
    private FXHDD01 nkmkkbskttts;

    /**
     * 2回目溶剤調整量
     */
    private FXHDD01 nkmyztsr;

    /**
     * 2回目ﾄﾙｴﾝ調整量
     */
    private FXHDD01 nikaimetoluenetyouseiryou;

    /**
     * 2回目ｿﾙﾐｯｸｽ調整量
     */
    private FXHDD01 nikaimesolmixtyouseiryou;

    /**
     * 2回目溶剤秤量日
     */
    private FXHDD01 nkmyzkeiryou_day;

    /**
     * 2回目溶剤秤量時間
     */
    private FXHDD01 nkmyzkeiryou_time;

    /**
     * 2回目溶剤①_材料品名
     */
    private FXHDD01 nkmyz1_zairyouhinmei;

    /**
     * 2回目溶剤①_調合量規格
     */
    private FXHDD01 nkmyz1_tyougouryoukikaku;

    /**
     * 2回目溶剤①_部材在庫No1
     */
    private FXHDD01 nkmyz1_buzaizaikolotno1;

    /**
     * 2回目溶剤①_調合量1
     */
    private FXHDD01 nkmyz1_tyougouryou1;

    /**
     * 2回目溶剤①_部材在庫No2
     */
    private FXHDD01 nkmyz1_buzaizaikolotno2;

    /**
     * 2回目溶剤①_調合量2
     */
    private FXHDD01 nkmyz1_tyougouryou2;

    /**
     * 2回目溶剤②_材料品名
     */
    private FXHDD01 nkmyz2_zairyouhinmei;

    /**
     * 2回目溶剤②_調合量規格
     */
    private FXHDD01 nkmyz2_tyougouryoukikaku;

    /**
     * 2回目溶剤②_部材在庫No1
     */
    private FXHDD01 nkmyz2_buzaizaikolotno1;

    /**
     * 2回目溶剤②_調合量1
     */
    private FXHDD01 nkmyz2_tyougouryou1;

    /**
     * 2回目溶剤②_部材在庫No2
     */
    private FXHDD01 nkmyz2_buzaizaikolotno2;

    /**
     * 2回目溶剤②_調合量2
     */
    private FXHDD01 nkmyz2_tyougouryou2;

    /**
     * 2回目担当者
     */
    private FXHDD01 nikaimetantousya;

    /**
     * 3回目撹拌設備
     */
    private FXHDD01 sankaimekakuhansetubi;

    /**
     * 3回目撹拌時間
     */
    private FXHDD01 sankaimekakuhanjikan;

    /**
     * 3回目撹拌開始日
     */
    private FXHDD01 sankaimekakuhankaisi_day;

    /**
     * 3回目撹拌開始時間
     */
    private FXHDD01 sankaimekakuhankaisi_time;

    /**
     * 3回目撹拌終了日
     */
    private FXHDD01 skmkkhsr_day;

    /**
     * 3回目撹拌終了時間
     */
    private FXHDD01 skmkkhsr_time;

    /**
     * 3回目脱脂皿の種類
     */
    private FXHDD01 sankaimedassizaranosyurui;

    /**
     * 3回目ｱﾙﾐ皿風袋重量
     */
    private FXHDD01 skmarmzftjr;

    /**
     * 3回目乾燥前ｽﾗﾘｰ重量
     */
    private FXHDD01 skmksmslrjr;

    /**
     * 3回目乾燥機
     */
    private FXHDD01 sankaimekansouki;

    /**
     * 3回目乾燥温度
     */
    private FXHDD01 sankaimekansouondo;

    /**
     * 3回目乾燥時間
     */
    private FXHDD01 sankaimekansoujikan;

    /**
     * 3回目乾燥開始日
     */
    private FXHDD01 sankaimekansoukaisi_day;

    /**
     * 3回目乾燥開始時間
     */
    private FXHDD01 sankaimekansoukaisi_time;

    /**
     * 3回目乾燥終了日
     */
    private FXHDD01 sankaimekansousyuuryou_day;

    /**
     * 3回目乾燥終了時間
     */
    private FXHDD01 sankaimekansousyuuryou_time;

    /**
     * 3回目乾燥後総重量
     */
    private FXHDD01 sankaimekansougosoujyuuryou;

    /**
     * 3回目乾燥後正味重量
     */
    private FXHDD01 skmksgsmjr;

    /**
     * 3回目固形分比率
     */
    private FXHDD01 sankaimekokeibunhiritu;

    /**
     * 3回目固形分測定担当者
     */
    private FXHDD01 skmkkbskttts;

    /**
     * F/P準備_風袋重量
     */
    private FXHDD01 fpjyunbi_fuutaijyuuryou;

    /**
     * F/P準備_担当者
     */
    private FXHDD01 fpjyunbi_tantousya;

    /**
     * ﾌｨﾙﾀｰ連結
     */
    private FXHDD01 filterrenketu;

    /**
     * F/P準備_ﾌｨﾙﾀｰ品名①
     */
    private FXHDD01 fpjyunbi_filterhinmei1;

    /**
     * F/P準備_LotNo①
     */
    private FXHDD01 fpjyunbi_lotno1;

    /**
     * F/P準備_取り付け本数①
     */
    private FXHDD01 fpjyunbi_toritukehonsuu1;

    /**
     * F/P準備_ﾌｨﾙﾀｰ品名②
     */
    private FXHDD01 fpjyunbi_filterhinmei2;

    /**
     * F/P準備_LotNo②
     */
    private FXHDD01 fpjyunbi_lotno2;

    /**
     * F/P準備_取り付け本数②
     */
    private FXHDD01 fpjyunbi_toritukehonsuu2;

    /**
     * 圧送ﾀﾝｸの洗浄
     */
    private FXHDD01 assoutanknosenjyou;

    /**
     * 排出容器の内袋
     */
    private FXHDD01 haisyutuyoukinoutibukuro;

    /**
     * F/PﾀﾝｸNo
     */
    private FXHDD01 fptankno;

    /**
     * F/P開始日
     */
    private FXHDD01 fpkaisi_day;

    /**
     * F/P開始時間
     */
    private FXHDD01 fpkaisi_time;

    /**
     * 圧送ﾚｷﾞｭﾚｰﾀｰNo
     */
    private FXHDD01 assouregulatorno;

    /**
     * 圧送圧力
     */
    private FXHDD01 assouaturyoku;

    /**
     * F/P開始_担当者
     */
    private FXHDD01 fpkaisi_tantousya;

    /**
     * F/P停止日
     */
    private FXHDD01 fpteisi_day;

    /**
     * F/P停止時間
     */
    private FXHDD01 fpteisi_time;

    /**
     * F/P交換_ﾌｨﾙﾀｰ品名①
     */
    private FXHDD01 fpkoukan_filterhinmei1;

    /**
     * F/P交換_LotNo①
     */
    private FXHDD01 fpkoukan_lotno1;

    /**
     * F/P交換_取り付け本数①
     */
    private FXHDD01 fpkoukan_toritukehonsuu1;

    /**
     * F/P交換_ﾌｨﾙﾀｰ品名②
     */
    private FXHDD01 fpkoukan_filterhinmei2;

    /**
     * F/P交換_LotNo②
     */
    private FXHDD01 fpkoukan_lotno2;

    /**
     * F/P交換_取り付け本数②
     */
    private FXHDD01 fpkoukan_toritukehonsuu2;

    /**
     * F/P再開日
     */
    private FXHDD01 fpsaikai_day;

    /**
     * F/P再開時間
     */
    private FXHDD01 fpsaikai_time;

    /**
     * F/P交換_担当者
     */
    private FXHDD01 fpkoukan_tantousya;

    /**
     * F/P終了日
     */
    private FXHDD01 fpsyuuryou_day;

    /**
     * F/P終了時間
     */
    private FXHDD01 fpsyuuryou_time;

    /**
     * F/P時間
     */
    private FXHDD01 fpjikan;

    /**
     * F/P終了_担当者
     */
    private FXHDD01 fpsyuurou_tantousya;

    /**
     * 総重量
     */
    private FXHDD01 soujyuuryou;

    /**
     * 正味重量
     */
    private FXHDD01 syoumijyuuryou;

    /**
     * 備考1
     */
    private FXHDD01 bikou1;

    /**
     * 備考2
     */
    private FXHDD01 bikou2;

    /**
     * WIPﾛｯﾄNo
     * @return the wiplotno
     */
    public FXHDD01 getWiplotno() {
        return wiplotno;
    }

    /**
     * WIPﾛｯﾄNo
     * @param wiplotno the wiplotno to set
     */
    public void setWiplotno(FXHDD01 wiplotno) {
        this.wiplotno = wiplotno;
    }

    /**
     * ｽﾘｯﾌﾟ品名
     * @return the sliphinmei
     */
    public FXHDD01 getSliphinmei() {
        return sliphinmei;
    }

    /**
     * ｽﾘｯﾌﾟ品名
     * @param sliphinmei the sliphinmei to set
     */
    public void setSliphinmei(FXHDD01 sliphinmei) {
        this.sliphinmei = sliphinmei;
    }

    /**
     * ｽﾘｯﾌﾟLotNo
     * @return the sliplotno
     */
    public FXHDD01 getSliplotno() {
        return sliplotno;
    }

    /**
     * ｽﾘｯﾌﾟLotNo
     * @param sliplotno the sliplotno to set
     */
    public void setSliplotno(FXHDD01 sliplotno) {
        this.sliplotno = sliplotno;
    }

    /**
     * ﾛｯﾄ区分
     * @return the lotkubun
     */
    public FXHDD01 getLotkubun() {
        return lotkubun;
    }

    /**
     * ﾛｯﾄ区分
     * @param lotkubun the lotkubun to set
     */
    public void setLotkubun(FXHDD01 lotkubun) {
        this.lotkubun = lotkubun;
    }

    /**
     * 原料記号
     * @return the genryoukigou
     */
    public FXHDD01 getGenryoukigou() {
        return genryoukigou;
    }

    /**
     * 原料記号
     * @param genryoukigou the genryoukigou to set
     */
    public void setGenryoukigou(FXHDD01 genryoukigou) {
        this.genryoukigou = genryoukigou;
    }

    /**
     * 秤量号機
     * @return the goki
     */
    public FXHDD01 getGoki() {
        return goki;
    }

    /**
     * 秤量号機
     * @param goki the goki to set
     */
    public void setGoki(FXHDD01 goki) {
        this.goki = goki;
    }

    /**
     * ｽﾗﾘｰ品名
     * @return the slurryhinmei
     */
    public FXHDD01 getSlurryhinmei() {
        return slurryhinmei;
    }

    /**
     * ｽﾗﾘｰ品名
     * @param slurryhinmei the slurryhinmei to set
     */
    public void setSlurryhinmei(FXHDD01 slurryhinmei) {
        this.slurryhinmei = slurryhinmei;
    }

    /**
     * ｽﾗﾘｰLotNo
     * @return the slurrylotno
     */
    public FXHDD01 getSlurrylotno() {
        return slurrylotno;
    }

    /**
     * ｽﾗﾘｰLotNo
     * @param slurrylotno the slurrylotno to set
     */
    public void setSlurrylotno(FXHDD01 slurrylotno) {
        this.slurrylotno = slurrylotno;
    }

    /**
     * 容器No
     * @return the youkino
     */
    public FXHDD01 getYoukino() {
        return youkino;
    }

    /**
     * 容器No
     * @param youkino the youkino to set
     */
    public void setYoukino(FXHDD01 youkino) {
        this.youkino = youkino;
    }

    /**
     * ｽﾗﾘｰ有効期限
     * @return the slurryyuukoukigen
     */
    public FXHDD01 getSlurryyuukoukigen() {
        return slurryyuukoukigen;
    }

    /**
     * ｽﾗﾘｰ有効期限
     * @param slurryyuukoukigen the slurryyuukoukigen to set
     */
    public void setSlurryyuukoukigen(FXHDD01 slurryyuukoukigen) {
        this.slurryyuukoukigen = slurryyuukoukigen;
    }

    /**
     * 乾燥固形分
     * @return the kansoukokeibun
     */
    public FXHDD01 getKansoukokeibun() {
        return kansoukokeibun;
    }

    /**
     * 乾燥固形分
     * @param kansoukokeibun the kansoukokeibun to set
     */
    public void setKansoukokeibun(FXHDD01 kansoukokeibun) {
        this.kansoukokeibun = kansoukokeibun;
    }

    /**
     * 脱脂固形分
     * @return the dassikokeibun
     */
    public FXHDD01 getDassikokeibun() {
        return dassikokeibun;
    }

    /**
     * 脱脂固形分
     * @param dassikokeibun the dassikokeibun to set
     */
    public void setDassikokeibun(FXHDD01 dassikokeibun) {
        this.dassikokeibun = dassikokeibun;
    }

    /**
     * 粉砕終了日
     * @return the funsaisyuuryou_day
     */
    public FXHDD01 getFunsaisyuuryou_day() {
        return funsaisyuuryou_day;
    }

    /**
     * 粉砕終了日
     * @param funsaisyuuryou_day the funsaisyuuryou_day to set
     */
    public void setFunsaisyuuryou_day(FXHDD01 funsaisyuuryou_day) {
        this.funsaisyuuryou_day = funsaisyuuryou_day;
    }

    /**
     * 粉砕終了時間
     * @return the funsaisyuuryou_time
     */
    public FXHDD01 getFunsaisyuuryou_time() {
        return funsaisyuuryou_time;
    }

    /**
     * 粉砕終了時間
     * @param funsaisyuuryou_time the funsaisyuuryou_time to set
     */
    public void setFunsaisyuuryou_time(FXHDD01 funsaisyuuryou_time) {
        this.funsaisyuuryou_time = funsaisyuuryou_time;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合日
     * @return the binderkongou_day
     */
    public FXHDD01 getBinderkongou_day() {
        return binderkongou_day;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合日
     * @param binderkongou_day the binderkongou_day to set
     */
    public void setBinderkongou_day(FXHDD01 binderkongou_day) {
        this.binderkongou_day = binderkongou_day;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合時間
     * @return the binderkongou_time
     */
    public FXHDD01 getBinderkongou_time() {
        return binderkongou_time;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合時間
     * @param binderkongou_time the binderkongou_time to set
     */
    public void setBinderkongou_time(FXHDD01 binderkongou_time) {
        this.binderkongou_time = binderkongou_time;
    }

    /**
     * ｽﾗﾘｰｴｰｼﾞﾝｸﾞ
     * @return the slurraging
     */
    public FXHDD01 getSlurraging() {
        return slurraging;
    }

    /**
     * ｽﾗﾘｰｴｰｼﾞﾝｸﾞ
     * @param slurraging the slurraging to set
     */
    public void setSlurraging(FXHDD01 slurraging) {
        this.slurraging = slurraging;
    }

    /**
     * ｽﾗﾘｰ経過日数
     * @return the slurrykeikanisuu
     */
    public FXHDD01 getSlurrykeikanisuu() {
        return slurrykeikanisuu;
    }

    /**
     * ｽﾗﾘｰ経過日数
     * @param slurrykeikanisuu the slurrykeikanisuu to set
     */
    public void setSlurrykeikanisuu(FXHDD01 slurrykeikanisuu) {
        this.slurrykeikanisuu = slurrykeikanisuu;
    }

    /**
     * ｽﾘｯﾌﾟ情報_風袋重量
     * @return the slipjyouhou_fuutaijyuuryou
     */
    public FXHDD01 getSlipjyouhou_fuutaijyuuryou() {
        return slipjyouhou_fuutaijyuuryou;
    }

    /**
     * ｽﾘｯﾌﾟ情報_風袋重量
     * @param slipjyouhou_fuutaijyuuryou the slipjyouhou_fuutaijyuuryou to set
     */
    public void setSlipjyouhou_fuutaijyuuryou(FXHDD01 slipjyouhou_fuutaijyuuryou) {
        this.slipjyouhou_fuutaijyuuryou = slipjyouhou_fuutaijyuuryou;
    }

    /**
     * 総重量
     * @return the slurrysoujyuuryou
     */
    public FXHDD01 getSlurrysoujyuuryou() {
        return slurrysoujyuuryou;
    }

    /**
     * 総重量
     * @param slurrysoujyuuryou the slurrysoujyuuryou to set
     */
    public void setSlurrysoujyuuryou(FXHDD01 slurrysoujyuuryou) {
        this.slurrysoujyuuryou = slurrysoujyuuryou;
    }

    /**
     * ｽﾗﾘｰ重量
     * @return the slurryjyuuryou
     */
    public FXHDD01 getSlurryjyuuryou() {
        return slurryjyuuryou;
    }

    /**
     * ｽﾗﾘｰ重量
     * @param slurryjyuuryou the slurryjyuuryou to set
     */
    public void setSlurryjyuuryou(FXHDD01 slurryjyuuryou) {
        this.slurryjyuuryou = slurryjyuuryou;
    }

    /**
     * 撹拌設備
     * @return the kakuhansetubi
     */
    public FXHDD01 getKakuhansetubi() {
        return kakuhansetubi;
    }

    /**
     * 撹拌設備
     * @param kakuhansetubi the kakuhansetubi to set
     */
    public void setKakuhansetubi(FXHDD01 kakuhansetubi) {
        this.kakuhansetubi = kakuhansetubi;
    }

    /**
     * 撹拌号機
     * @return the kakuhangoki
     */
    public FXHDD01 getKakuhangoki() {
        return kakuhangoki;
    }

    /**
     * 撹拌号機
     * @param kakuhangoki the kakuhangoki to set
     */
    public void setKakuhangoki(FXHDD01 kakuhangoki) {
        this.kakuhangoki = kakuhangoki;
    }

    /**
     * 撹拌回転数
     * @return the kakuhankaitensuu
     */
    public FXHDD01 getKakuhankaitensuu() {
        return kakuhankaitensuu;
    }

    /**
     * 撹拌回転数
     * @param kakuhankaitensuu the kakuhankaitensuu to set
     */
    public void setKakuhankaitensuu(FXHDD01 kakuhankaitensuu) {
        this.kakuhankaitensuu = kakuhankaitensuu;
    }

    /**
     * 撹拌時間
     * @return the kakuhanjikan
     */
    public FXHDD01 getKakuhanjikan() {
        return kakuhanjikan;
    }

    /**
     * 撹拌時間
     * @param kakuhanjikan the kakuhanjikan to set
     */
    public void setKakuhanjikan(FXHDD01 kakuhanjikan) {
        this.kakuhanjikan = kakuhanjikan;
    }

    /**
     * 撹拌開始日
     * @return the kakuhankaisi_day
     */
    public FXHDD01 getKakuhankaisi_day() {
        return kakuhankaisi_day;
    }

    /**
     * 撹拌開始日
     * @param kakuhankaisi_day the kakuhankaisi_day to set
     */
    public void setKakuhankaisi_day(FXHDD01 kakuhankaisi_day) {
        this.kakuhankaisi_day = kakuhankaisi_day;
    }

    /**
     * 撹拌開始時間
     * @return the kakuhankaisi_time
     */
    public FXHDD01 getKakuhankaisi_time() {
        return kakuhankaisi_time;
    }

    /**
     * 撹拌開始時間
     * @param kakuhankaisi_time the kakuhankaisi_time to set
     */
    public void setKakuhankaisi_time(FXHDD01 kakuhankaisi_time) {
        this.kakuhankaisi_time = kakuhankaisi_time;
    }

    /**
     * 撹拌終了日
     * @return the kakuhansyuuryou_day
     */
    public FXHDD01 getKakuhansyuuryou_day() {
        return kakuhansyuuryou_day;
    }

    /**
     * 撹拌終了日
     * @param kakuhansyuuryou_day the kakuhansyuuryou_day to set
     */
    public void setKakuhansyuuryou_day(FXHDD01 kakuhansyuuryou_day) {
        this.kakuhansyuuryou_day = kakuhansyuuryou_day;
    }

    /**
     * 撹拌終了時間
     * @return the kakuhansyuuryou_time
     */
    public FXHDD01 getKakuhansyuuryou_time() {
        return kakuhansyuuryou_time;
    }

    /**
     * 撹拌終了時間
     * @param kakuhansyuuryou_time the kakuhansyuuryou_time to set
     */
    public void setKakuhansyuuryou_time(FXHDD01 kakuhansyuuryou_time) {
        this.kakuhansyuuryou_time = kakuhansyuuryou_time;
    }

    /**
     * 脱脂皿の種類
     * @return the dassizaranosyurui
     */
    public FXHDD01 getDassizaranosyurui() {
        return dassizaranosyurui;
    }

    /**
     * 脱脂皿の種類
     * @param dassizaranosyurui the dassizaranosyurui to set
     */
    public void setDassizaranosyurui(FXHDD01 dassizaranosyurui) {
        this.dassizaranosyurui = dassizaranosyurui;
    }

    /**
     * 固形分測定_風袋重量
     * @return the kkbskt_fuutaijyuuryou
     */
    public FXHDD01 getKkbskt_fuutaijyuuryou() {
        return kkbskt_fuutaijyuuryou;
    }

    /**
     * 固形分測定_風袋重量
     * @param kkbskt_fuutaijyuuryou the kkbskt_fuutaijyuuryou to set
     */
    public void setKkbskt_fuutaijyuuryou(FXHDD01 kkbskt_fuutaijyuuryou) {
        this.kkbskt_fuutaijyuuryou = kkbskt_fuutaijyuuryou;
    }

    /**
     * 乾燥前ｽﾗﾘｰ重量
     * @return the kansoumaeslurryjyuuryou
     */
    public FXHDD01 getKansoumaeslurryjyuuryou() {
        return kansoumaeslurryjyuuryou;
    }

    /**
     * 乾燥前ｽﾗﾘｰ重量
     * @param kansoumaeslurryjyuuryou the kansoumaeslurryjyuuryou to set
     */
    public void setKansoumaeslurryjyuuryou(FXHDD01 kansoumaeslurryjyuuryou) {
        this.kansoumaeslurryjyuuryou = kansoumaeslurryjyuuryou;
    }

    /**
     * 乾燥機
     * @return the kansouki
     */
    public FXHDD01 getKansouki() {
        return kansouki;
    }

    /**
     * 乾燥機
     * @param kansouki the kansouki to set
     */
    public void setKansouki(FXHDD01 kansouki) {
        this.kansouki = kansouki;
    }

    /**
     * 乾燥温度
     * @return the kansouondo
     */
    public FXHDD01 getKansouondo() {
        return kansouondo;
    }

    /**
     * 乾燥温度
     * @param kansouondo the kansouondo to set
     */
    public void setKansouondo(FXHDD01 kansouondo) {
        this.kansouondo = kansouondo;
    }

    /**
     * 乾燥時間
     * @return the kansoujikan
     */
    public FXHDD01 getKansoujikan() {
        return kansoujikan;
    }

    /**
     * 乾燥時間
     * @param kansoujikan the kansoujikan to set
     */
    public void setKansoujikan(FXHDD01 kansoujikan) {
        this.kansoujikan = kansoujikan;
    }

    /**
     * 乾燥開始日
     * @return the kansoukaisi_day
     */
    public FXHDD01 getKansoukaisi_day() {
        return kansoukaisi_day;
    }

    /**
     * 乾燥開始日
     * @param kansoukaisi_day the kansoukaisi_day to set
     */
    public void setKansoukaisi_day(FXHDD01 kansoukaisi_day) {
        this.kansoukaisi_day = kansoukaisi_day;
    }

    /**
     * 乾燥開始時間
     * @return the kansoukaisi_time
     */
    public FXHDD01 getKansoukaisi_time() {
        return kansoukaisi_time;
    }

    /**
     * 乾燥開始時間
     * @param kansoukaisi_time the kansoukaisi_time to set
     */
    public void setKansoukaisi_time(FXHDD01 kansoukaisi_time) {
        this.kansoukaisi_time = kansoukaisi_time;
    }

    /**
     * 乾燥終了日
     * @return the kansousyuuryou_day
     */
    public FXHDD01 getKansousyuuryou_day() {
        return kansousyuuryou_day;
    }

    /**
     * 乾燥終了日
     * @param kansousyuuryou_day the kansousyuuryou_day to set
     */
    public void setKansousyuuryou_day(FXHDD01 kansousyuuryou_day) {
        this.kansousyuuryou_day = kansousyuuryou_day;
    }

    /**
     * 乾燥終了時間
     * @return the kansousyuuryou_time
     */
    public FXHDD01 getKansousyuuryou_time() {
        return kansousyuuryou_time;
    }

    /**
     * 乾燥終了時間
     * @param kansousyuuryou_time the kansousyuuryou_time to set
     */
    public void setKansousyuuryou_time(FXHDD01 kansousyuuryou_time) {
        this.kansousyuuryou_time = kansousyuuryou_time;
    }

    /**
     * 乾燥後総重量
     * @return the kansougosoujyuuryou
     */
    public FXHDD01 getKansougosoujyuuryou() {
        return kansougosoujyuuryou;
    }

    /**
     * 乾燥後総重量
     * @param kansougosoujyuuryou the kansougosoujyuuryou to set
     */
    public void setKansougosoujyuuryou(FXHDD01 kansougosoujyuuryou) {
        this.kansougosoujyuuryou = kansougosoujyuuryou;
    }

    /**
     * 乾燥後正味重量
     * @return the kansougosyoumijyuuryou
     */
    public FXHDD01 getKansougosyoumijyuuryou() {
        return kansougosyoumijyuuryou;
    }

    /**
     * 乾燥後正味重量
     * @param kansougosyoumijyuuryou the kansougosyoumijyuuryou to set
     */
    public void setKansougosyoumijyuuryou(FXHDD01 kansougosyoumijyuuryou) {
        this.kansougosyoumijyuuryou = kansougosyoumijyuuryou;
    }

    /**
     * 固形分比率
     * @return the kokeibunhiritu
     */
    public FXHDD01 getKokeibunhiritu() {
        return kokeibunhiritu;
    }

    /**
     * 固形分比率
     * @param kokeibunhiritu the kokeibunhiritu to set
     */
    public void setKokeibunhiritu(FXHDD01 kokeibunhiritu) {
        this.kokeibunhiritu = kokeibunhiritu;
    }

    /**
     * 固形分測定担当者
     * @return the kokeibunsokuteitantousya
     */
    public FXHDD01 getKokeibunsokuteitantousya() {
        return kokeibunsokuteitantousya;
    }

    /**
     * 固形分測定担当者
     * @param kokeibunsokuteitantousya the kokeibunsokuteitantousya to set
     */
    public void setKokeibunsokuteitantousya(FXHDD01 kokeibunsokuteitantousya) {
        this.kokeibunsokuteitantousya = kokeibunsokuteitantousya;
    }

    /**
     * 溶剤調整量
     * @return the youzaityouseiryou
     */
    public FXHDD01 getYouzaityouseiryou() {
        return youzaityouseiryou;
    }

    /**
     * 溶剤調整量
     * @param youzaityouseiryou the youzaityouseiryou to set
     */
    public void setYouzaityouseiryou(FXHDD01 youzaityouseiryou) {
        this.youzaityouseiryou = youzaityouseiryou;
    }

    /**
     * ﾄﾙｴﾝ調整量
     * @return the toluenetyouseiryou
     */
    public FXHDD01 getToluenetyouseiryou() {
        return toluenetyouseiryou;
    }

    /**
     * ﾄﾙｴﾝ調整量
     * @param toluenetyouseiryou the toluenetyouseiryou to set
     */
    public void setToluenetyouseiryou(FXHDD01 toluenetyouseiryou) {
        this.toluenetyouseiryou = toluenetyouseiryou;
    }

    /**
     * ｿﾙﾐｯｸｽ調整量
     * @return the solmixtyouseiryou
     */
    public FXHDD01 getSolmixtyouseiryou() {
        return solmixtyouseiryou;
    }

    /**
     * ｿﾙﾐｯｸｽ調整量
     * @param solmixtyouseiryou the solmixtyouseiryou to set
     */
    public void setSolmixtyouseiryou(FXHDD01 solmixtyouseiryou) {
        this.solmixtyouseiryou = solmixtyouseiryou;
    }

    /**
     * 溶剤秤量日
     * @return the youzaikeiryou_day
     */
    public FXHDD01 getYouzaikeiryou_day() {
        return youzaikeiryou_day;
    }

    /**
     * 溶剤秤量日
     * @param youzaikeiryou_day the youzaikeiryou_day to set
     */
    public void setYouzaikeiryou_day(FXHDD01 youzaikeiryou_day) {
        this.youzaikeiryou_day = youzaikeiryou_day;
    }

    /**
     * 溶剤秤量時間
     * @return the youzaikeiryou_time
     */
    public FXHDD01 getYouzaikeiryou_time() {
        return youzaikeiryou_time;
    }

    /**
     * 溶剤秤量時間
     * @param youzaikeiryou_time the youzaikeiryou_time to set
     */
    public void setYouzaikeiryou_time(FXHDD01 youzaikeiryou_time) {
        this.youzaikeiryou_time = youzaikeiryou_time;
    }

    /**
     * 溶剤①_材料品名
     * @return the youzai1_zairyouhinmei
     */
    public FXHDD01 getYouzai1_zairyouhinmei() {
        return youzai1_zairyouhinmei;
    }

    /**
     * 溶剤①_材料品名
     * @param youzai1_zairyouhinmei the youzai1_zairyouhinmei to set
     */
    public void setYouzai1_zairyouhinmei(FXHDD01 youzai1_zairyouhinmei) {
        this.youzai1_zairyouhinmei = youzai1_zairyouhinmei;
    }

    /**
     * 溶剤①_調合量規格
     * @return the youzai1_tyougouryoukikaku
     */
    public FXHDD01 getYouzai1_tyougouryoukikaku() {
        return youzai1_tyougouryoukikaku;
    }

    /**
     * 溶剤①_調合量規格
     * @param youzai1_tyougouryoukikaku the youzai1_tyougouryoukikaku to set
     */
    public void setYouzai1_tyougouryoukikaku(FXHDD01 youzai1_tyougouryoukikaku) {
        this.youzai1_tyougouryoukikaku = youzai1_tyougouryoukikaku;
    }

    /**
     * 溶剤①_部材在庫No1
     * @return the youzai1_buzaizaikolotno1
     */
    public FXHDD01 getYouzai1_buzaizaikolotno1() {
        return youzai1_buzaizaikolotno1;
    }

    /**
     * 溶剤①_部材在庫No1
     * @param youzai1_buzaizaikolotno1 the youzai1_buzaizaikolotno1 to set
     */
    public void setYouzai1_buzaizaikolotno1(FXHDD01 youzai1_buzaizaikolotno1) {
        this.youzai1_buzaizaikolotno1 = youzai1_buzaizaikolotno1;
    }

    /**
     * 溶剤①_調合量1
     * @return the youzai1_tyougouryou1
     */
    public FXHDD01 getYouzai1_tyougouryou1() {
        return youzai1_tyougouryou1;
    }

    /**
     * 溶剤①_調合量1
     * @param youzai1_tyougouryou1 the youzai1_tyougouryou1 to set
     */
    public void setYouzai1_tyougouryou1(FXHDD01 youzai1_tyougouryou1) {
        this.youzai1_tyougouryou1 = youzai1_tyougouryou1;
    }

    /**
     * 溶剤①_部材在庫No2
     * @return the youzai1_buzaizaikolotno2
     */
    public FXHDD01 getYouzai1_buzaizaikolotno2() {
        return youzai1_buzaizaikolotno2;
    }

    /**
     * 溶剤①_部材在庫No2
     * @param youzai1_buzaizaikolotno2 the youzai1_buzaizaikolotno2 to set
     */
    public void setYouzai1_buzaizaikolotno2(FXHDD01 youzai1_buzaizaikolotno2) {
        this.youzai1_buzaizaikolotno2 = youzai1_buzaizaikolotno2;
    }

    /**
     * 溶剤①_調合量2
     * @return the youzai1_tyougouryou2
     */
    public FXHDD01 getYouzai1_tyougouryou2() {
        return youzai1_tyougouryou2;
    }

    /**
     * 溶剤①_調合量2
     * @param youzai1_tyougouryou2 the youzai1_tyougouryou2 to set
     */
    public void setYouzai1_tyougouryou2(FXHDD01 youzai1_tyougouryou2) {
        this.youzai1_tyougouryou2 = youzai1_tyougouryou2;
    }

    /**
     * 溶剤②_材料品名
     * @return the youzai2_zairyouhinmei
     */
    public FXHDD01 getYouzai2_zairyouhinmei() {
        return youzai2_zairyouhinmei;
    }

    /**
     * 溶剤②_材料品名
     * @param youzai2_zairyouhinmei the youzai2_zairyouhinmei to set
     */
    public void setYouzai2_zairyouhinmei(FXHDD01 youzai2_zairyouhinmei) {
        this.youzai2_zairyouhinmei = youzai2_zairyouhinmei;
    }

    /**
     * 溶剤②_調合量規格
     * @return the youzai2_tyougouryoukikaku
     */
    public FXHDD01 getYouzai2_tyougouryoukikaku() {
        return youzai2_tyougouryoukikaku;
    }

    /**
     * 溶剤②_調合量規格
     * @param youzai2_tyougouryoukikaku the youzai2_tyougouryoukikaku to set
     */
    public void setYouzai2_tyougouryoukikaku(FXHDD01 youzai2_tyougouryoukikaku) {
        this.youzai2_tyougouryoukikaku = youzai2_tyougouryoukikaku;
    }

    /**
     * 溶剤②_部材在庫No1
     * @return the youzai2_buzaizaikolotno1
     */
    public FXHDD01 getYouzai2_buzaizaikolotno1() {
        return youzai2_buzaizaikolotno1;
    }

    /**
     * 溶剤②_部材在庫No1
     * @param youzai2_buzaizaikolotno1 the youzai2_buzaizaikolotno1 to set
     */
    public void setYouzai2_buzaizaikolotno1(FXHDD01 youzai2_buzaizaikolotno1) {
        this.youzai2_buzaizaikolotno1 = youzai2_buzaizaikolotno1;
    }

    /**
     * 溶剤②_調合量1
     * @return the youzai2_tyougouryou1
     */
    public FXHDD01 getYouzai2_tyougouryou1() {
        return youzai2_tyougouryou1;
    }

    /**
     * 溶剤②_調合量1
     * @param youzai2_tyougouryou1 the youzai2_tyougouryou1 to set
     */
    public void setYouzai2_tyougouryou1(FXHDD01 youzai2_tyougouryou1) {
        this.youzai2_tyougouryou1 = youzai2_tyougouryou1;
    }

    /**
     * 溶剤②_部材在庫No2
     * @return the youzai2_buzaizaikolotno2
     */
    public FXHDD01 getYouzai2_buzaizaikolotno2() {
        return youzai2_buzaizaikolotno2;
    }

    /**
     * 溶剤②_部材在庫No2
     * @param youzai2_buzaizaikolotno2 the youzai2_buzaizaikolotno2 to set
     */
    public void setYouzai2_buzaizaikolotno2(FXHDD01 youzai2_buzaizaikolotno2) {
        this.youzai2_buzaizaikolotno2 = youzai2_buzaizaikolotno2;
    }

    /**
     * 溶剤②_調合量2
     * @return the youzai2_tyougouryou2
     */
    public FXHDD01 getYouzai2_tyougouryou2() {
        return youzai2_tyougouryou2;
    }

    /**
     * 溶剤②_調合量2
     * @param youzai2_tyougouryou2 the youzai2_tyougouryou2 to set
     */
    public void setYouzai2_tyougouryou2(FXHDD01 youzai2_tyougouryou2) {
        this.youzai2_tyougouryou2 = youzai2_tyougouryou2;
    }

    /**
     * 担当者
     * @return the tantousya
     */
    public FXHDD01 getTantousya() {
        return tantousya;
    }

    /**
     * 担当者
     * @param tantousya the tantousya to set
     */
    public void setTantousya(FXHDD01 tantousya) {
        this.tantousya = tantousya;
    }

    /**
     * 2回目撹拌設備
     * @return the nikaimekakuhansetubi
     */
    public FXHDD01 getNikaimekakuhansetubi() {
        return nikaimekakuhansetubi;
    }

    /**
     * 2回目撹拌設備
     * @param nikaimekakuhansetubi the nikaimekakuhansetubi to set
     */
    public void setNikaimekakuhansetubi(FXHDD01 nikaimekakuhansetubi) {
        this.nikaimekakuhansetubi = nikaimekakuhansetubi;
    }

    /**
     * 2回目撹拌時間
     * @return the nikaimekakuhanjikan
     */
    public FXHDD01 getNikaimekakuhanjikan() {
        return nikaimekakuhanjikan;
    }

    /**
     * 2回目撹拌時間
     * @param nikaimekakuhanjikan the nikaimekakuhanjikan to set
     */
    public void setNikaimekakuhanjikan(FXHDD01 nikaimekakuhanjikan) {
        this.nikaimekakuhanjikan = nikaimekakuhanjikan;
    }

    /**
     * 2回目撹拌開始日
     * @return the nikaimekakuhankaisi_day
     */
    public FXHDD01 getNikaimekakuhankaisi_day() {
        return nikaimekakuhankaisi_day;
    }

    /**
     * 2回目撹拌開始日
     * @param nikaimekakuhankaisi_day the nikaimekakuhankaisi_day to set
     */
    public void setNikaimekakuhankaisi_day(FXHDD01 nikaimekakuhankaisi_day) {
        this.nikaimekakuhankaisi_day = nikaimekakuhankaisi_day;
    }

    /**
     * 2回目撹拌開始時間
     * @return the nikaimekakuhankaisi_time
     */
    public FXHDD01 getNikaimekakuhankaisi_time() {
        return nikaimekakuhankaisi_time;
    }

    /**
     * 2回目撹拌開始時間
     * @param nikaimekakuhankaisi_time the nikaimekakuhankaisi_time to set
     */
    public void setNikaimekakuhankaisi_time(FXHDD01 nikaimekakuhankaisi_time) {
        this.nikaimekakuhankaisi_time = nikaimekakuhankaisi_time;
    }

    /**
     * 2回目撹拌終了日
     * @return the nkmkkhsr_day
     */
    public FXHDD01 getNkmkkhsr_day() {
        return nkmkkhsr_day;
    }

    /**
     * 2回目撹拌終了日
     * @param nkmkkhsr_day the nkmkkhsr_day to set
     */
    public void setNkmkkhsr_day(FXHDD01 nkmkkhsr_day) {
        this.nkmkkhsr_day = nkmkkhsr_day;
    }

    /**
     * 2回目撹拌終了時間
     * @return the nkmkkhsr_time
     */
    public FXHDD01 getNkmkkhsr_time() {
        return nkmkkhsr_time;
    }

    /**
     * 2回目撹拌終了時間
     * @param nkmkkhsr_time the nkmkkhsr_time to set
     */
    public void setNkmkkhsr_time(FXHDD01 nkmkkhsr_time) {
        this.nkmkkhsr_time = nkmkkhsr_time;
    }

    /**
     * 2回目脱脂皿の種類
     * @return the nikaimedassizaranosyurui
     */
    public FXHDD01 getNikaimedassizaranosyurui() {
        return nikaimedassizaranosyurui;
    }

    /**
     * 2回目脱脂皿の種類
     * @param nikaimedassizaranosyurui the nikaimedassizaranosyurui to set
     */
    public void setNikaimedassizaranosyurui(FXHDD01 nikaimedassizaranosyurui) {
        this.nikaimedassizaranosyurui = nikaimedassizaranosyurui;
    }

    /**
     * 2回目ｱﾙﾐ皿風袋重量
     * @return the nkmarmzftjr
     */
    public FXHDD01 getNkmarmzftjr() {
        return nkmarmzftjr;
    }

    /**
     * 2回目ｱﾙﾐ皿風袋重量
     * @param nkmarmzftjr the nkmarmzftjr to set
     */
    public void setNkmarmzftjr(FXHDD01 nkmarmzftjr) {
        this.nkmarmzftjr = nkmarmzftjr;
    }

    /**
     * 2回目乾燥前ｽﾗﾘｰ重量
     * @return the nkmksmslrjr
     */
    public FXHDD01 getNkmksmslrjr() {
        return nkmksmslrjr;
    }

    /**
     * 2回目乾燥前ｽﾗﾘｰ重量
     * @param nkmksmslrjr the nkmksmslrjr to set
     */
    public void setNkmksmslrjr(FXHDD01 nkmksmslrjr) {
        this.nkmksmslrjr = nkmksmslrjr;
    }

    /**
     * 2回目乾燥機
     * @return the nikaimekansouki
     */
    public FXHDD01 getNikaimekansouki() {
        return nikaimekansouki;
    }

    /**
     * 2回目乾燥機
     * @param nikaimekansouki the nikaimekansouki to set
     */
    public void setNikaimekansouki(FXHDD01 nikaimekansouki) {
        this.nikaimekansouki = nikaimekansouki;
    }

    /**
     * 2回目乾燥温度
     * @return the nikaimekansouondo
     */
    public FXHDD01 getNikaimekansouondo() {
        return nikaimekansouondo;
    }

    /**
     * 2回目乾燥温度
     * @param nikaimekansouondo the nikaimekansouondo to set
     */
    public void setNikaimekansouondo(FXHDD01 nikaimekansouondo) {
        this.nikaimekansouondo = nikaimekansouondo;
    }

    /**
     * 2回目乾燥時間
     * @return the nikaimekansoujikan
     */
    public FXHDD01 getNikaimekansoujikan() {
        return nikaimekansoujikan;
    }

    /**
     * 2回目乾燥時間
     * @param nikaimekansoujikan the nikaimekansoujikan to set
     */
    public void setNikaimekansoujikan(FXHDD01 nikaimekansoujikan) {
        this.nikaimekansoujikan = nikaimekansoujikan;
    }

    /**
     * 2回目乾燥開始日
     * @return the nikaimekansoukaisi_day
     */
    public FXHDD01 getNikaimekansoukaisi_day() {
        return nikaimekansoukaisi_day;
    }

    /**
     * 2回目乾燥開始日
     * @param nikaimekansoukaisi_day the nikaimekansoukaisi_day to set
     */
    public void setNikaimekansoukaisi_day(FXHDD01 nikaimekansoukaisi_day) {
        this.nikaimekansoukaisi_day = nikaimekansoukaisi_day;
    }

    /**
     * 2回目乾燥開始時間
     * @return the nikaimekansoukaisi_time
     */
    public FXHDD01 getNikaimekansoukaisi_time() {
        return nikaimekansoukaisi_time;
    }

    /**
     * 2回目乾燥開始時間
     * @param nikaimekansoukaisi_time the nikaimekansoukaisi_time to set
     */
    public void setNikaimekansoukaisi_time(FXHDD01 nikaimekansoukaisi_time) {
        this.nikaimekansoukaisi_time = nikaimekansoukaisi_time;
    }

    /**
     * 2回目乾燥終了日
     * @return the nikaimekansousyuuryou_day
     */
    public FXHDD01 getNikaimekansousyuuryou_day() {
        return nikaimekansousyuuryou_day;
    }

    /**
     * 2回目乾燥終了日
     * @param nikaimekansousyuuryou_day the nikaimekansousyuuryou_day to set
     */
    public void setNikaimekansousyuuryou_day(FXHDD01 nikaimekansousyuuryou_day) {
        this.nikaimekansousyuuryou_day = nikaimekansousyuuryou_day;
    }

    /**
     * 2回目乾燥終了時間
     * @return the nikaimekansousyuuryou_time
     */
    public FXHDD01 getNikaimekansousyuuryou_time() {
        return nikaimekansousyuuryou_time;
    }

    /**
     * 2回目乾燥終了時間
     * @param nikaimekansousyuuryou_time the nikaimekansousyuuryou_time to set
     */
    public void setNikaimekansousyuuryou_time(FXHDD01 nikaimekansousyuuryou_time) {
        this.nikaimekansousyuuryou_time = nikaimekansousyuuryou_time;
    }

    /**
     * 2回目乾燥後総重量
     * @return the nikaimekansougosoujyuuryou
     */
    public FXHDD01 getNikaimekansougosoujyuuryou() {
        return nikaimekansougosoujyuuryou;
    }

    /**
     * 2回目乾燥後総重量
     * @param nikaimekansougosoujyuuryou the nikaimekansougosoujyuuryou to set
     */
    public void setNikaimekansougosoujyuuryou(FXHDD01 nikaimekansougosoujyuuryou) {
        this.nikaimekansougosoujyuuryou = nikaimekansougosoujyuuryou;
    }

    /**
     * 2回目乾燥後正味重量
     * @return the nkmksgsmjr
     */
    public FXHDD01 getNkmksgsmjr() {
        return nkmksgsmjr;
    }

    /**
     * 2回目乾燥後正味重量
     * @param nkmksgsmjr the nkmksgsmjr to set
     */
    public void setNkmksgsmjr(FXHDD01 nkmksgsmjr) {
        this.nkmksgsmjr = nkmksgsmjr;
    }

    /**
     * 2回目固形分比率
     * @return the nikaimekokeibunhiritu
     */
    public FXHDD01 getNikaimekokeibunhiritu() {
        return nikaimekokeibunhiritu;
    }

    /**
     * 2回目固形分比率
     * @param nikaimekokeibunhiritu the nikaimekokeibunhiritu to set
     */
    public void setNikaimekokeibunhiritu(FXHDD01 nikaimekokeibunhiritu) {
        this.nikaimekokeibunhiritu = nikaimekokeibunhiritu;
    }

    /**
     * 2回目固形分測定担当者
     * @return the nkmkkbskttts
     */
    public FXHDD01 getNkmkkbskttts() {
        return nkmkkbskttts;
    }

    /**
     * 2回目固形分測定担当者
     * @param nkmkkbskttts the nkmkkbskttts to set
     */
    public void setNkmkkbskttts(FXHDD01 nkmkkbskttts) {
        this.nkmkkbskttts = nkmkkbskttts;
    }

    /**
     * 2回目溶剤調整量
     * @return the nkmyztsr
     */
    public FXHDD01 getNkmyztsr() {
        return nkmyztsr;
    }

    /**
     * 2回目溶剤調整量
     * @param nkmyztsr the nkmyztsr to set
     */
    public void setNkmyztsr(FXHDD01 nkmyztsr) {
        this.nkmyztsr = nkmyztsr;
    }

    /**
     * 2回目ﾄﾙｴﾝ調整量
     * @return the nikaimetoluenetyouseiryou
     */
    public FXHDD01 getNikaimetoluenetyouseiryou() {
        return nikaimetoluenetyouseiryou;
    }

    /**
     * 2回目ﾄﾙｴﾝ調整量
     * @param nikaimetoluenetyouseiryou the nikaimetoluenetyouseiryou to set
     */
    public void setNikaimetoluenetyouseiryou(FXHDD01 nikaimetoluenetyouseiryou) {
        this.nikaimetoluenetyouseiryou = nikaimetoluenetyouseiryou;
    }

    /**
     * 2回目ｿﾙﾐｯｸｽ調整量
     * @return the nikaimesolmixtyouseiryou
     */
    public FXHDD01 getNikaimesolmixtyouseiryou() {
        return nikaimesolmixtyouseiryou;
    }

    /**
     * 2回目ｿﾙﾐｯｸｽ調整量
     * @param nikaimesolmixtyouseiryou the nikaimesolmixtyouseiryou to set
     */
    public void setNikaimesolmixtyouseiryou(FXHDD01 nikaimesolmixtyouseiryou) {
        this.nikaimesolmixtyouseiryou = nikaimesolmixtyouseiryou;
    }

    /**
     * 2回目溶剤秤量日
     * @return the nkmyzkeiryou_day
     */
    public FXHDD01 getNkmyzkeiryou_day() {
        return nkmyzkeiryou_day;
    }

    /**
     * 2回目溶剤秤量日
     * @param nkmyzkeiryou_day the nkmyzkeiryou_day to set
     */
    public void setNkmyzkeiryou_day(FXHDD01 nkmyzkeiryou_day) {
        this.nkmyzkeiryou_day = nkmyzkeiryou_day;
    }

    /**
     * 2回目溶剤秤量時間
     * @return the nkmyzkeiryou_time
     */
    public FXHDD01 getNkmyzkeiryou_time() {
        return nkmyzkeiryou_time;
    }

    /**
     * 2回目溶剤秤量時間
     * @param nkmyzkeiryou_time the nkmyzkeiryou_time to set
     */
    public void setNkmyzkeiryou_time(FXHDD01 nkmyzkeiryou_time) {
        this.nkmyzkeiryou_time = nkmyzkeiryou_time;
    }

    /**
     * 2回目溶剤①_材料品名
     * @return the nkmyz1_zairyouhinmei
     */
    public FXHDD01 getNkmyz1_zairyouhinmei() {
        return nkmyz1_zairyouhinmei;
    }

    /**
     * 2回目溶剤①_材料品名
     * @param nkmyz1_zairyouhinmei the nkmyz1_zairyouhinmei to set
     */
    public void setNkmyz1_zairyouhinmei(FXHDD01 nkmyz1_zairyouhinmei) {
        this.nkmyz1_zairyouhinmei = nkmyz1_zairyouhinmei;
    }

    /**
     * 2回目溶剤①_調合量規格
     * @return the nkmyz1_tyougouryoukikaku
     */
    public FXHDD01 getNkmyz1_tyougouryoukikaku() {
        return nkmyz1_tyougouryoukikaku;
    }

    /**
     * 2回目溶剤①_調合量規格
     * @param nkmyz1_tyougouryoukikaku the nkmyz1_tyougouryoukikaku to set
     */
    public void setNkmyz1_tyougouryoukikaku(FXHDD01 nkmyz1_tyougouryoukikaku) {
        this.nkmyz1_tyougouryoukikaku = nkmyz1_tyougouryoukikaku;
    }

    /**
     * 2回目溶剤①_部材在庫No1
     * @return the nkmyz1_buzaizaikolotno1
     */
    public FXHDD01 getNkmyz1_buzaizaikolotno1() {
        return nkmyz1_buzaizaikolotno1;
    }

    /**
     * 2回目溶剤①_部材在庫No1
     * @param nkmyz1_buzaizaikolotno1 the nkmyz1_buzaizaikolotno1 to set
     */
    public void setNkmyz1_buzaizaikolotno1(FXHDD01 nkmyz1_buzaizaikolotno1) {
        this.nkmyz1_buzaizaikolotno1 = nkmyz1_buzaizaikolotno1;
    }

    /**
     * 2回目溶剤①_調合量1
     * @return the nkmyz1_tyougouryou1
     */
    public FXHDD01 getNkmyz1_tyougouryou1() {
        return nkmyz1_tyougouryou1;
    }

    /**
     * 2回目溶剤①_調合量1
     * @param nkmyz1_tyougouryou1 the nkmyz1_tyougouryou1 to set
     */
    public void setNkmyz1_tyougouryou1(FXHDD01 nkmyz1_tyougouryou1) {
        this.nkmyz1_tyougouryou1 = nkmyz1_tyougouryou1;
    }

    /**
     * 2回目溶剤①_部材在庫No2
     * @return the nkmyz1_buzaizaikolotno2
     */
    public FXHDD01 getNkmyz1_buzaizaikolotno2() {
        return nkmyz1_buzaizaikolotno2;
    }

    /**
     * 2回目溶剤①_部材在庫No2
     * @param nkmyz1_buzaizaikolotno2 the nkmyz1_buzaizaikolotno2 to set
     */
    public void setNkmyz1_buzaizaikolotno2(FXHDD01 nkmyz1_buzaizaikolotno2) {
        this.nkmyz1_buzaizaikolotno2 = nkmyz1_buzaizaikolotno2;
    }

    /**
     * 2回目溶剤①_調合量2
     * @return the nkmyz1_tyougouryou2
     */
    public FXHDD01 getNkmyz1_tyougouryou2() {
        return nkmyz1_tyougouryou2;
    }

    /**
     * 2回目溶剤①_調合量2
     * @param nkmyz1_tyougouryou2 the nkmyz1_tyougouryou2 to set
     */
    public void setNkmyz1_tyougouryou2(FXHDD01 nkmyz1_tyougouryou2) {
        this.nkmyz1_tyougouryou2 = nkmyz1_tyougouryou2;
    }

    /**
     * 2回目溶剤②_材料品名
     * @return the nkmyz2_zairyouhinmei
     */
    public FXHDD01 getNkmyz2_zairyouhinmei() {
        return nkmyz2_zairyouhinmei;
    }

    /**
     * 2回目溶剤②_材料品名
     * @param nkmyz2_zairyouhinmei the nkmyz2_zairyouhinmei to set
     */
    public void setNkmyz2_zairyouhinmei(FXHDD01 nkmyz2_zairyouhinmei) {
        this.nkmyz2_zairyouhinmei = nkmyz2_zairyouhinmei;
    }

    /**
     * 2回目溶剤②_調合量規格
     * @return the nkmyz2_tyougouryoukikaku
     */
    public FXHDD01 getNkmyz2_tyougouryoukikaku() {
        return nkmyz2_tyougouryoukikaku;
    }

    /**
     * 2回目溶剤②_調合量規格
     * @param nkmyz2_tyougouryoukikaku the nkmyz2_tyougouryoukikaku to set
     */
    public void setNkmyz2_tyougouryoukikaku(FXHDD01 nkmyz2_tyougouryoukikaku) {
        this.nkmyz2_tyougouryoukikaku = nkmyz2_tyougouryoukikaku;
    }

    /**
     * 2回目溶剤②_部材在庫No1
     * @return the nkmyz2_buzaizaikolotno1
     */
    public FXHDD01 getNkmyz2_buzaizaikolotno1() {
        return nkmyz2_buzaizaikolotno1;
    }

    /**
     * 2回目溶剤②_部材在庫No1
     * @param nkmyz2_buzaizaikolotno1 the nkmyz2_buzaizaikolotno1 to set
     */
    public void setNkmyz2_buzaizaikolotno1(FXHDD01 nkmyz2_buzaizaikolotno1) {
        this.nkmyz2_buzaizaikolotno1 = nkmyz2_buzaizaikolotno1;
    }

    /**
     * 2回目溶剤②_調合量1
     * @return the nkmyz2_tyougouryou1
     */
    public FXHDD01 getNkmyz2_tyougouryou1() {
        return nkmyz2_tyougouryou1;
    }

    /**
     * 2回目溶剤②_調合量1
     * @param nkmyz2_tyougouryou1 the nkmyz2_tyougouryou1 to set
     */
    public void setNkmyz2_tyougouryou1(FXHDD01 nkmyz2_tyougouryou1) {
        this.nkmyz2_tyougouryou1 = nkmyz2_tyougouryou1;
    }

    /**
     * 2回目溶剤②_部材在庫No2
     * @return the nkmyz2_buzaizaikolotno2
     */
    public FXHDD01 getNkmyz2_buzaizaikolotno2() {
        return nkmyz2_buzaizaikolotno2;
    }

    /**
     * 2回目溶剤②_部材在庫No2
     * @param nkmyz2_buzaizaikolotno2 the nkmyz2_buzaizaikolotno2 to set
     */
    public void setNkmyz2_buzaizaikolotno2(FXHDD01 nkmyz2_buzaizaikolotno2) {
        this.nkmyz2_buzaizaikolotno2 = nkmyz2_buzaizaikolotno2;
    }

    /**
     * 2回目溶剤②_調合量2
     * @return the nkmyz2_tyougouryou2
     */
    public FXHDD01 getNkmyz2_tyougouryou2() {
        return nkmyz2_tyougouryou2;
    }

    /**
     * 2回目溶剤②_調合量2
     * @param nkmyz2_tyougouryou2 the nkmyz2_tyougouryou2 to set
     */
    public void setNkmyz2_tyougouryou2(FXHDD01 nkmyz2_tyougouryou2) {
        this.nkmyz2_tyougouryou2 = nkmyz2_tyougouryou2;
    }

    /**
     * 2回目担当者
     * @return the nikaimetantousya
     */
    public FXHDD01 getNikaimetantousya() {
        return nikaimetantousya;
    }

    /**
     * 2回目担当者
     * @param nikaimetantousya the nikaimetantousya to set
     */
    public void setNikaimetantousya(FXHDD01 nikaimetantousya) {
        this.nikaimetantousya = nikaimetantousya;
    }

    /**
     * 3回目撹拌設備
     * @return the sankaimekakuhansetubi
     */
    public FXHDD01 getSankaimekakuhansetubi() {
        return sankaimekakuhansetubi;
    }

    /**
     * 3回目撹拌設備
     * @param sankaimekakuhansetubi the sankaimekakuhansetubi to set
     */
    public void setSankaimekakuhansetubi(FXHDD01 sankaimekakuhansetubi) {
        this.sankaimekakuhansetubi = sankaimekakuhansetubi;
    }

    /**
     * 3回目撹拌時間
     * @return the sankaimekakuhanjikan
     */
    public FXHDD01 getSankaimekakuhanjikan() {
        return sankaimekakuhanjikan;
    }

    /**
     * 3回目撹拌時間
     * @param sankaimekakuhanjikan the sankaimekakuhanjikan to set
     */
    public void setSankaimekakuhanjikan(FXHDD01 sankaimekakuhanjikan) {
        this.sankaimekakuhanjikan = sankaimekakuhanjikan;
    }

    /**
     * 3回目撹拌開始日
     * @return the sankaimekakuhankaisi_day
     */
    public FXHDD01 getSankaimekakuhankaisi_day() {
        return sankaimekakuhankaisi_day;
    }

    /**
     * 3回目撹拌開始日
     * @param sankaimekakuhankaisi_day the sankaimekakuhankaisi_day to set
     */
    public void setSankaimekakuhankaisi_day(FXHDD01 sankaimekakuhankaisi_day) {
        this.sankaimekakuhankaisi_day = sankaimekakuhankaisi_day;
    }

    /**
     * 3回目撹拌開始時間
     * @return the sankaimekakuhankaisi_time
     */
    public FXHDD01 getSankaimekakuhankaisi_time() {
        return sankaimekakuhankaisi_time;
    }

    /**
     * 3回目撹拌開始時間
     * @param sankaimekakuhankaisi_time the sankaimekakuhankaisi_time to set
     */
    public void setSankaimekakuhankaisi_time(FXHDD01 sankaimekakuhankaisi_time) {
        this.sankaimekakuhankaisi_time = sankaimekakuhankaisi_time;
    }

    /**
     * 3回目撹拌終了日
     * @return the skmkkhsr_day
     */
    public FXHDD01 getSkmkkhsr_day() {
        return skmkkhsr_day;
    }

    /**
     * 3回目撹拌終了日
     * @param skmkkhsr_day the skmkkhsr_day to set
     */
    public void setSkmkkhsr_day(FXHDD01 skmkkhsr_day) {
        this.skmkkhsr_day = skmkkhsr_day;
    }

    /**
     * 3回目撹拌終了時間
     * @return the skmkkhsr_time
     */
    public FXHDD01 getSkmkkhsr_time() {
        return skmkkhsr_time;
    }

    /**
     * 3回目撹拌終了時間
     * @param skmkkhsr_time the skmkkhsr_time to set
     */
    public void setSkmkkhsr_time(FXHDD01 skmkkhsr_time) {
        this.skmkkhsr_time = skmkkhsr_time;
    }

    /**
     * 3回目脱脂皿の種類
     * @return the sankaimedassizaranosyurui
     */
    public FXHDD01 getSankaimedassizaranosyurui() {
        return sankaimedassizaranosyurui;
    }

    /**
     * 3回目脱脂皿の種類
     * @param sankaimedassizaranosyurui the sankaimedassizaranosyurui to set
     */
    public void setSankaimedassizaranosyurui(FXHDD01 sankaimedassizaranosyurui) {
        this.sankaimedassizaranosyurui = sankaimedassizaranosyurui;
    }

    /**
     * 3回目ｱﾙﾐ皿風袋重量
     * @return the skmarmzftjr
     */
    public FXHDD01 getSkmarmzftjr() {
        return skmarmzftjr;
    }

    /**
     * 3回目ｱﾙﾐ皿風袋重量
     * @param skmarmzftjr the skmarmzftjr to set
     */
    public void setSkmarmzftjr(FXHDD01 skmarmzftjr) {
        this.skmarmzftjr = skmarmzftjr;
    }

    /**
     * 3回目乾燥前ｽﾗﾘｰ重量
     * @return the skmksmslrjr
     */
    public FXHDD01 getSkmksmslrjr() {
        return skmksmslrjr;
    }

    /**
     * 3回目乾燥前ｽﾗﾘｰ重量
     * @param skmksmslrjr the skmksmslrjr to set
     */
    public void setSkmksmslrjr(FXHDD01 skmksmslrjr) {
        this.skmksmslrjr = skmksmslrjr;
    }

    /**
     * 3回目乾燥機
     * @return the sankaimekansouki
     */
    public FXHDD01 getSankaimekansouki() {
        return sankaimekansouki;
    }

    /**
     * 3回目乾燥機
     * @param sankaimekansouki the sankaimekansouki to set
     */
    public void setSankaimekansouki(FXHDD01 sankaimekansouki) {
        this.sankaimekansouki = sankaimekansouki;
    }

    /**
     * 3回目乾燥温度
     * @return the sankaimekansouondo
     */
    public FXHDD01 getSankaimekansouondo() {
        return sankaimekansouondo;
    }

    /**
     * 3回目乾燥温度
     * @param sankaimekansouondo the sankaimekansouondo to set
     */
    public void setSankaimekansouondo(FXHDD01 sankaimekansouondo) {
        this.sankaimekansouondo = sankaimekansouondo;
    }

    /**
     * 3回目乾燥時間
     * @return the sankaimekansoujikan
     */
    public FXHDD01 getSankaimekansoujikan() {
        return sankaimekansoujikan;
    }

    /**
     * 3回目乾燥時間
     * @param sankaimekansoujikan the sankaimekansoujikan to set
     */
    public void setSankaimekansoujikan(FXHDD01 sankaimekansoujikan) {
        this.sankaimekansoujikan = sankaimekansoujikan;
    }

    /**
     * 3回目乾燥開始日
     * @return the sankaimekansoukaisi_day
     */
    public FXHDD01 getSankaimekansoukaisi_day() {
        return sankaimekansoukaisi_day;
    }

    /**
     * 3回目乾燥開始日
     * @param sankaimekansoukaisi_day the sankaimekansoukaisi_day to set
     */
    public void setSankaimekansoukaisi_day(FXHDD01 sankaimekansoukaisi_day) {
        this.sankaimekansoukaisi_day = sankaimekansoukaisi_day;
    }

    /**
     * 3回目乾燥開始時間
     * @return the sankaimekansoukaisi_time
     */
    public FXHDD01 getSankaimekansoukaisi_time() {
        return sankaimekansoukaisi_time;
    }

    /**
     * 3回目乾燥開始時間
     * @param sankaimekansoukaisi_time the sankaimekansoukaisi_time to set
     */
    public void setSankaimekansoukaisi_time(FXHDD01 sankaimekansoukaisi_time) {
        this.sankaimekansoukaisi_time = sankaimekansoukaisi_time;
    }

    /**
     * 3回目乾燥終了日
     * @return the sankaimekansousyuuryou_day
     */
    public FXHDD01 getSankaimekansousyuuryou_day() {
        return sankaimekansousyuuryou_day;
    }

    /**
     * 3回目乾燥終了日
     * @param sankaimekansousyuuryou_day the sankaimekansousyuuryou_day to set
     */
    public void setSankaimekansousyuuryou_day(FXHDD01 sankaimekansousyuuryou_day) {
        this.sankaimekansousyuuryou_day = sankaimekansousyuuryou_day;
    }

    /**
     * 3回目乾燥終了時間
     * @return the sankaimekansousyuuryou_time
     */
    public FXHDD01 getSankaimekansousyuuryou_time() {
        return sankaimekansousyuuryou_time;
    }

    /**
     * 3回目乾燥終了時間
     * @param sankaimekansousyuuryou_time the sankaimekansousyuuryou_time to set
     */
    public void setSankaimekansousyuuryou_time(FXHDD01 sankaimekansousyuuryou_time) {
        this.sankaimekansousyuuryou_time = sankaimekansousyuuryou_time;
    }

    /**
     * 3回目乾燥後総重量
     * @return the sankaimekansougosoujyuuryou
     */
    public FXHDD01 getSankaimekansougosoujyuuryou() {
        return sankaimekansougosoujyuuryou;
    }

    /**
     * 3回目乾燥後総重量
     * @param sankaimekansougosoujyuuryou the sankaimekansougosoujyuuryou to set
     */
    public void setSankaimekansougosoujyuuryou(FXHDD01 sankaimekansougosoujyuuryou) {
        this.sankaimekansougosoujyuuryou = sankaimekansougosoujyuuryou;
    }

    /**
     * 3回目乾燥後正味重量
     * @return the skmksgsmjr
     */
    public FXHDD01 getSkmksgsmjr() {
        return skmksgsmjr;
    }

    /**
     * 3回目乾燥後正味重量
     * @param skmksgsmjr the skmksgsmjr to set
     */
    public void setSkmksgsmjr(FXHDD01 skmksgsmjr) {
        this.skmksgsmjr = skmksgsmjr;
    }

    /**
     * 3回目固形分比率
     * @return the sankaimekokeibunhiritu
     */
    public FXHDD01 getSankaimekokeibunhiritu() {
        return sankaimekokeibunhiritu;
    }

    /**
     * 3回目固形分比率
     * @param sankaimekokeibunhiritu the sankaimekokeibunhiritu to set
     */
    public void setSankaimekokeibunhiritu(FXHDD01 sankaimekokeibunhiritu) {
        this.sankaimekokeibunhiritu = sankaimekokeibunhiritu;
    }

    /**
     * 3回目固形分測定担当者
     * @return the skmkkbskttts
     */
    public FXHDD01 getSkmkkbskttts() {
        return skmkkbskttts;
    }

    /**
     * 3回目固形分測定担当者
     * @param skmkkbskttts the skmkkbskttts to set
     */
    public void setSkmkkbskttts(FXHDD01 skmkkbskttts) {
        this.skmkkbskttts = skmkkbskttts;
    }

    /**
     * F/P準備_風袋重量
     * @return the fpjyunbi_fuutaijyuuryou
     */
    public FXHDD01 getFpjyunbi_fuutaijyuuryou() {
        return fpjyunbi_fuutaijyuuryou;
    }

    /**
     * F/P準備_風袋重量
     * @param fpjyunbi_fuutaijyuuryou the fpjyunbi_fuutaijyuuryou to set
     */
    public void setFpjyunbi_fuutaijyuuryou(FXHDD01 fpjyunbi_fuutaijyuuryou) {
        this.fpjyunbi_fuutaijyuuryou = fpjyunbi_fuutaijyuuryou;
    }

    /**
     * F/P準備_担当者
     * @return the fpjyunbi_tantousya
     */
    public FXHDD01 getFpjyunbi_tantousya() {
        return fpjyunbi_tantousya;
    }

    /**
     * F/P準備_担当者
     * @param fpjyunbi_tantousya the fpjyunbi_tantousya to set
     */
    public void setFpjyunbi_tantousya(FXHDD01 fpjyunbi_tantousya) {
        this.fpjyunbi_tantousya = fpjyunbi_tantousya;
    }

    /**
     * ﾌｨﾙﾀｰ連結
     * @return the filterrenketu
     */
    public FXHDD01 getFilterrenketu() {
        return filterrenketu;
    }

    /**
     * ﾌｨﾙﾀｰ連結
     * @param filterrenketu the filterrenketu to set
     */
    public void setFilterrenketu(FXHDD01 filterrenketu) {
        this.filterrenketu = filterrenketu;
    }

    /**
     * F/P準備_ﾌｨﾙﾀｰ品名①
     * @return the fpjyunbi_filterhinmei1
     */
    public FXHDD01 getFpjyunbi_filterhinmei1() {
        return fpjyunbi_filterhinmei1;
    }

    /**
     * F/P準備_ﾌｨﾙﾀｰ品名①
     * @param fpjyunbi_filterhinmei1 the fpjyunbi_filterhinmei1 to set
     */
    public void setFpjyunbi_filterhinmei1(FXHDD01 fpjyunbi_filterhinmei1) {
        this.fpjyunbi_filterhinmei1 = fpjyunbi_filterhinmei1;
    }

    /**
     * F/P準備_LotNo①
     * @return the fpjyunbi_lotno1
     */
    public FXHDD01 getFpjyunbi_lotno1() {
        return fpjyunbi_lotno1;
    }

    /**
     * F/P準備_LotNo①
     * @param fpjyunbi_lotno1 the fpjyunbi_lotno1 to set
     */
    public void setFpjyunbi_lotno1(FXHDD01 fpjyunbi_lotno1) {
        this.fpjyunbi_lotno1 = fpjyunbi_lotno1;
    }

    /**
     * F/P準備_取り付け本数①
     * @return the fpjyunbi_toritukehonsuu1
     */
    public FXHDD01 getFpjyunbi_toritukehonsuu1() {
        return fpjyunbi_toritukehonsuu1;
    }

    /**
     * F/P準備_取り付け本数①
     * @param fpjyunbi_toritukehonsuu1 the fpjyunbi_toritukehonsuu1 to set
     */
    public void setFpjyunbi_toritukehonsuu1(FXHDD01 fpjyunbi_toritukehonsuu1) {
        this.fpjyunbi_toritukehonsuu1 = fpjyunbi_toritukehonsuu1;
    }

    /**
     * F/P準備_ﾌｨﾙﾀｰ品名②
     * @return the fpjyunbi_filterhinmei2
     */
    public FXHDD01 getFpjyunbi_filterhinmei2() {
        return fpjyunbi_filterhinmei2;
    }

    /**
     * F/P準備_ﾌｨﾙﾀｰ品名②
     * @param fpjyunbi_filterhinmei2 the fpjyunbi_filterhinmei2 to set
     */
    public void setFpjyunbi_filterhinmei2(FXHDD01 fpjyunbi_filterhinmei2) {
        this.fpjyunbi_filterhinmei2 = fpjyunbi_filterhinmei2;
    }

    /**
     * F/P準備_LotNo②
     * @return the fpjyunbi_lotno2
     */
    public FXHDD01 getFpjyunbi_lotno2() {
        return fpjyunbi_lotno2;
    }

    /**
     * F/P準備_LotNo②
     * @param fpjyunbi_lotno2 the fpjyunbi_lotno2 to set
     */
    public void setFpjyunbi_lotno2(FXHDD01 fpjyunbi_lotno2) {
        this.fpjyunbi_lotno2 = fpjyunbi_lotno2;
    }

    /**
     * F/P準備_取り付け本数②
     * @return the fpjyunbi_toritukehonsuu2
     */
    public FXHDD01 getFpjyunbi_toritukehonsuu2() {
        return fpjyunbi_toritukehonsuu2;
    }

    /**
     * F/P準備_取り付け本数②
     * @param fpjyunbi_toritukehonsuu2 the fpjyunbi_toritukehonsuu2 to set
     */
    public void setFpjyunbi_toritukehonsuu2(FXHDD01 fpjyunbi_toritukehonsuu2) {
        this.fpjyunbi_toritukehonsuu2 = fpjyunbi_toritukehonsuu2;
    }

    /**
     * 圧送ﾀﾝｸの洗浄
     * @return the assoutanknosenjyou
     */
    public FXHDD01 getAssoutanknosenjyou() {
        return assoutanknosenjyou;
    }

    /**
     * 圧送ﾀﾝｸの洗浄
     * @param assoutanknosenjyou the assoutanknosenjyou to set
     */
    public void setAssoutanknosenjyou(FXHDD01 assoutanknosenjyou) {
        this.assoutanknosenjyou = assoutanknosenjyou;
    }

    /**
     * 排出容器の内袋
     * @return the haisyutuyoukinoutibukuro
     */
    public FXHDD01 getHaisyutuyoukinoutibukuro() {
        return haisyutuyoukinoutibukuro;
    }

    /**
     * 排出容器の内袋
     * @param haisyutuyoukinoutibukuro the haisyutuyoukinoutibukuro to set
     */
    public void setHaisyutuyoukinoutibukuro(FXHDD01 haisyutuyoukinoutibukuro) {
        this.haisyutuyoukinoutibukuro = haisyutuyoukinoutibukuro;
    }

    /**
     * F/PﾀﾝｸNo
     * @return the fptankno
     */
    public FXHDD01 getFptankno() {
        return fptankno;
    }

    /**
     * F/PﾀﾝｸNo
     * @param fptankno the fptankno to set
     */
    public void setFptankno(FXHDD01 fptankno) {
        this.fptankno = fptankno;
    }

    /**
     * F/P開始日
     * @return the fpkaisi_day
     */
    public FXHDD01 getFpkaisi_day() {
        return fpkaisi_day;
    }

    /**
     * F/P開始日
     * @param fpkaisi_day the fpkaisi_day to set
     */
    public void setFpkaisi_day(FXHDD01 fpkaisi_day) {
        this.fpkaisi_day = fpkaisi_day;
    }

    /**
     * F/P開始時間
     * @return the fpkaisi_time
     */
    public FXHDD01 getFpkaisi_time() {
        return fpkaisi_time;
    }

    /**
     * F/P開始時間
     * @param fpkaisi_time the fpkaisi_time to set
     */
    public void setFpkaisi_time(FXHDD01 fpkaisi_time) {
        this.fpkaisi_time = fpkaisi_time;
    }

    /**
     * 圧送ﾚｷﾞｭﾚｰﾀｰNo
     * @return the assouregulatorno
     */
    public FXHDD01 getAssouregulatorno() {
        return assouregulatorno;
    }

    /**
     * 圧送ﾚｷﾞｭﾚｰﾀｰNo
     * @param assouregulatorno the assouregulatorno to set
     */
    public void setAssouregulatorno(FXHDD01 assouregulatorno) {
        this.assouregulatorno = assouregulatorno;
    }

    /**
     * 圧送圧力
     * @return the assouaturyoku
     */
    public FXHDD01 getAssouaturyoku() {
        return assouaturyoku;
    }

    /**
     * 圧送圧力
     * @param assouaturyoku the assouaturyoku to set
     */
    public void setAssouaturyoku(FXHDD01 assouaturyoku) {
        this.assouaturyoku = assouaturyoku;
    }

    /**
     * F/P開始_担当者
     * @return the fpkaisi_tantousya
     */
    public FXHDD01 getFpkaisi_tantousya() {
        return fpkaisi_tantousya;
    }

    /**
     * F/P開始_担当者
     * @param fpkaisi_tantousya the fpkaisi_tantousya to set
     */
    public void setFpkaisi_tantousya(FXHDD01 fpkaisi_tantousya) {
        this.fpkaisi_tantousya = fpkaisi_tantousya;
    }

    /**
     * F/P停止日
     * @return the fpteisi_day
     */
    public FXHDD01 getFpteisi_day() {
        return fpteisi_day;
    }

    /**
     * F/P停止日
     * @param fpteisi_day the fpteisi_day to set
     */
    public void setFpteisi_day(FXHDD01 fpteisi_day) {
        this.fpteisi_day = fpteisi_day;
    }

    /**
     * F/P停止時間
     * @return the fpteisi_time
     */
    public FXHDD01 getFpteisi_time() {
        return fpteisi_time;
    }

    /**
     * F/P停止時間
     * @param fpteisi_time the fpteisi_time to set
     */
    public void setFpteisi_time(FXHDD01 fpteisi_time) {
        this.fpteisi_time = fpteisi_time;
    }

    /**
     * F/P交換_ﾌｨﾙﾀｰ品名①
     * @return the fpkoukan_filterhinmei1
     */
    public FXHDD01 getFpkoukan_filterhinmei1() {
        return fpkoukan_filterhinmei1;
    }

    /**
     * F/P交換_ﾌｨﾙﾀｰ品名①
     * @param fpkoukan_filterhinmei1 the fpkoukan_filterhinmei1 to set
     */
    public void setFpkoukan_filterhinmei1(FXHDD01 fpkoukan_filterhinmei1) {
        this.fpkoukan_filterhinmei1 = fpkoukan_filterhinmei1;
    }

    /**
     * F/P交換_LotNo①
     * @return the fpkoukan_lotno1
     */
    public FXHDD01 getFpkoukan_lotno1() {
        return fpkoukan_lotno1;
    }

    /**
     * F/P交換_LotNo①
     * @param fpkoukan_lotno1 the fpkoukan_lotno1 to set
     */
    public void setFpkoukan_lotno1(FXHDD01 fpkoukan_lotno1) {
        this.fpkoukan_lotno1 = fpkoukan_lotno1;
    }

    /**
     * F/P交換_取り付け本数①
     * @return the fpkoukan_toritukehonsuu1
     */
    public FXHDD01 getFpkoukan_toritukehonsuu1() {
        return fpkoukan_toritukehonsuu1;
    }

    /**
     * F/P交換_取り付け本数①
     * @param fpkoukan_toritukehonsuu1 the fpkoukan_toritukehonsuu1 to set
     */
    public void setFpkoukan_toritukehonsuu1(FXHDD01 fpkoukan_toritukehonsuu1) {
        this.fpkoukan_toritukehonsuu1 = fpkoukan_toritukehonsuu1;
    }

    /**
     * F/P交換_ﾌｨﾙﾀｰ品名②
     * @return the fpkoukan_filterhinmei2
     */
    public FXHDD01 getFpkoukan_filterhinmei2() {
        return fpkoukan_filterhinmei2;
    }

    /**
     * F/P交換_ﾌｨﾙﾀｰ品名②
     * @param fpkoukan_filterhinmei2 the fpkoukan_filterhinmei2 to set
     */
    public void setFpkoukan_filterhinmei2(FXHDD01 fpkoukan_filterhinmei2) {
        this.fpkoukan_filterhinmei2 = fpkoukan_filterhinmei2;
    }

    /**
     * F/P交換_LotNo②
     * @return the fpkoukan_lotno2
     */
    public FXHDD01 getFpkoukan_lotno2() {
        return fpkoukan_lotno2;
    }

    /**
     * F/P交換_LotNo②
     * @param fpkoukan_lotno2 the fpkoukan_lotno2 to set
     */
    public void setFpkoukan_lotno2(FXHDD01 fpkoukan_lotno2) {
        this.fpkoukan_lotno2 = fpkoukan_lotno2;
    }

    /**
     * F/P交換_取り付け本数②
     * @return the fpkoukan_toritukehonsuu2
     */
    public FXHDD01 getFpkoukan_toritukehonsuu2() {
        return fpkoukan_toritukehonsuu2;
    }

    /**
     * F/P交換_取り付け本数②
     * @param fpkoukan_toritukehonsuu2 the fpkoukan_toritukehonsuu2 to set
     */
    public void setFpkoukan_toritukehonsuu2(FXHDD01 fpkoukan_toritukehonsuu2) {
        this.fpkoukan_toritukehonsuu2 = fpkoukan_toritukehonsuu2;
    }

    /**
     * F/P再開日
     * @return the fpsaikai_day
     */
    public FXHDD01 getFpsaikai_day() {
        return fpsaikai_day;
    }

    /**
     * F/P再開日
     * @param fpsaikai_day the fpsaikai_day to set
     */
    public void setFpsaikai_day(FXHDD01 fpsaikai_day) {
        this.fpsaikai_day = fpsaikai_day;
    }

    /**
     * F/P再開時間
     * @return the fpsaikai_time
     */
    public FXHDD01 getFpsaikai_time() {
        return fpsaikai_time;
    }

    /**
     * F/P再開時間
     * @param fpsaikai_time the fpsaikai_time to set
     */
    public void setFpsaikai_time(FXHDD01 fpsaikai_time) {
        this.fpsaikai_time = fpsaikai_time;
    }

    /**
     * F/P交換_担当者
     * @return the fpkoukan_tantousya
     */
    public FXHDD01 getFpkoukan_tantousya() {
        return fpkoukan_tantousya;
    }

    /**
     * F/P交換_担当者
     * @param fpkoukan_tantousya the fpkoukan_tantousya to set
     */
    public void setFpkoukan_tantousya(FXHDD01 fpkoukan_tantousya) {
        this.fpkoukan_tantousya = fpkoukan_tantousya;
    }

    /**
     * F/P終了日
     * @return the fpsyuuryou_day
     */
    public FXHDD01 getFpsyuuryou_day() {
        return fpsyuuryou_day;
    }

    /**
     * F/P終了日
     * @param fpsyuuryou_day the fpsyuuryou_day to set
     */
    public void setFpsyuuryou_day(FXHDD01 fpsyuuryou_day) {
        this.fpsyuuryou_day = fpsyuuryou_day;
    }

    /**
     * F/P終了時間
     * @return the fpsyuuryou_time
     */
    public FXHDD01 getFpsyuuryou_time() {
        return fpsyuuryou_time;
    }

    /**
     * F/P終了時間
     * @param fpsyuuryou_time the fpsyuuryou_time to set
     */
    public void setFpsyuuryou_time(FXHDD01 fpsyuuryou_time) {
        this.fpsyuuryou_time = fpsyuuryou_time;
    }

    /**
     * F/P時間
     * @return the fpjikan
     */
    public FXHDD01 getFpjikan() {
        return fpjikan;
    }

    /**
     * F/P時間
     * @param fpjikan the fpjikan to set
     */
    public void setFpjikan(FXHDD01 fpjikan) {
        this.fpjikan = fpjikan;
    }

    /**
     * F/P終了_担当者
     * @return the fpsyuurou_tantousya
     */
    public FXHDD01 getFpsyuurou_tantousya() {
        return fpsyuurou_tantousya;
    }

    /**
     * F/P終了_担当者
     * @param fpsyuurou_tantousya the fpsyuurou_tantousya to set
     */
    public void setFpsyuurou_tantousya(FXHDD01 fpsyuurou_tantousya) {
        this.fpsyuurou_tantousya = fpsyuurou_tantousya;
    }

    /**
     * 総重量
     * @return the soujyuuryou
     */
    public FXHDD01 getSoujyuuryou() {
        return soujyuuryou;
    }

    /**
     * 総重量
     * @param soujyuuryou the soujyuuryou to set
     */
    public void setSoujyuuryou(FXHDD01 soujyuuryou) {
        this.soujyuuryou = soujyuuryou;
    }

    /**
     * 正味重量
     * @return the syoumijyuuryou
     */
    public FXHDD01 getSyoumijyuuryou() {
        return syoumijyuuryou;
    }

    /**
     * 正味重量
     * @param syoumijyuuryou the syoumijyuuryou to set
     */
    public void setSyoumijyuuryou(FXHDD01 syoumijyuuryou) {
        this.syoumijyuuryou = syoumijyuuryou;
    }

    /**
     * 備考1
     * @return the bikou1
     */
    public FXHDD01 getBikou1() {
        return bikou1;
    }

    /**
     * 備考1
     * @param bikou1 the bikou1 to set
     */
    public void setBikou1(FXHDD01 bikou1) {
        this.bikou1 = bikou1;
    }

    /**
     * 備考2
     * @return the bikou2
     */
    public FXHDD01 getBikou2() {
        return bikou2;
    }

    /**
     * 備考2
     * @param bikou2 the bikou2 to set
     */
    public void setBikou2(FXHDD01 bikou2) {
        this.bikou2 = bikou2;
    }
}
