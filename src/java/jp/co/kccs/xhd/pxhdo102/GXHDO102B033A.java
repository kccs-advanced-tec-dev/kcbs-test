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
 * 変更日	2021/12/13<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B033(ｽﾘｯﾌﾟ作製・ｽﾘｯﾌﾟ固形分測定)
 *
 * @author KCSS K.Jo
 * @since  2021/12/13
 */
@ViewScoped
@Named
public class GXHDO102B033A implements Serializable {
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
     * 脱脂皿の種類
     */
    private FXHDD01 dassizaranosyurui;

    /**
     * ﾙﾂﾎﾞNo
     */
    private FXHDD01 rutubono;

    /**
     * ﾙﾂﾎﾞ風袋重量
     */
    private FXHDD01 rutubofuutaijyuuryou;

    /**
     * 乾燥前ｽﾘｯﾌﾟ重量
     */
    private FXHDD01 kansoumaeslipjyuuryou;

    /**
     * 乾燥機①
     */
    private FXHDD01 kansouki1;

    /**
     * 乾燥温度①
     */
    private FXHDD01 kansouondo1;

    /**
     * 乾燥時間①
     */
    private FXHDD01 kansoujikan1;

    /**
     * 乾燥開始日①
     */
    private FXHDD01 kansoukaisi1_day;

    /**
     * 乾燥開始時間①
     */
    private FXHDD01 kansoukaisi1_time;

    /**
     * 乾燥終了日①
     */
    private FXHDD01 kansousyuuryou1_day;

    /**
     * 乾燥終了時間①
     */
    private FXHDD01 kansousyuuryou1_time;

    /**
     * 乾燥機②
     */
    private FXHDD01 kansouki2;

    /**
     * 乾燥温度②
     */
    private FXHDD01 kansouondo2;

    /**
     * 乾燥時間②
     */
    private FXHDD01 kansoujikan2;

    /**
     * 乾燥開始日②
     */
    private FXHDD01 kansoukaisi2_day;

    /**
     * 乾燥開始時間②
     */
    private FXHDD01 kansoukaisi2_time;

    /**
     * 乾燥終了日②
     */
    private FXHDD01 kansousyuuryou2_day;

    /**
     * 乾燥終了時間②
     */
    private FXHDD01 kansousyuuryou2_time;

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
     * 撹拌_撹拌ﾓｰﾄﾞ
     */
    private FXHDD01 kakuhan_kakuhanmode;

    /**
     * 撹拌_回転方向
     */
    private FXHDD01 kakuhan_kaitenhoukou;

    /**
     * 撹拌_回転数(rpm)
     */
    private FXHDD01 kakuhan_kaitensuu;

    /**
     * 撹拌_撹拌時間
     */
    private FXHDD01 kakuhan_kakuhanjikan;

    /**
     * 撹拌_撹拌開始日
     */
    private FXHDD01 kakuhan_kakuhankaisi_day;

    /**
     * 撹拌_撹拌開始時間
     */
    private FXHDD01 kakuhan_kakuhankaisi_time;

    /**
     * 開始電流値(A)
     */
    private FXHDD01 kaisidenryuuti;

    /**
     * 撹拌_撹拌終了予定日
     */
    private FXHDD01 kakuhan_kakuhansyuuryouyotei_day;

    /**
     * 撹拌_撹拌終了予定時間
     */
    private FXHDD01 kakuhan_kakuhansyuuryouyotei_time;

    /**
     * 撹拌_撹拌終了日
     */
    private FXHDD01 kakuhan_kakuhansyuuryou_day;

    /**
     * 撹拌_撹拌終了時間
     */
    private FXHDD01 kakuhan_kakuhansyuuryou_time;

    /**
     * 終了電流値(A)
     */
    private FXHDD01 syuuryoudenryuuti;

    /**
     * 撹拌_撹拌担当者
     */
    private FXHDD01 kakuhan_kakuhantantousya;

    /**
     * 排出前撹拌_撹拌ﾓｰﾄﾞ
     */
    private FXHDD01 haisyutumaekakuhan_kakuhanmode;

    /**
     * 排出前撹拌_回転方向
     */
    private FXHDD01 haisyutumaekakuhan_kaitenhoukou;

    /**
     * 排出前撹拌_回転数(rpm)
     */
    private FXHDD01 haisyutumaekakuhan_kaitensuu;

    /**
     * 排出前撹拌_撹拌時間
     */
    private FXHDD01 haisyutumaekakuhan_kakuhanjikan;

    /**
     * 排出前撹拌_撹拌開始日
     */
    private FXHDD01 haisyutumaekakuhan_kakuhankaisi_day;

    /**
     * 排出前撹拌_撹拌開始時間
     */
    private FXHDD01 haisyutumaekakuhan_kakuhankaisi_time;

    /**
     * 排出前撹拌_撹拌終了予定日
     */
    private FXHDD01 haisyutumaekakuhan_kakuhansyuuryouyotei_day;

    /**
     * 排出前撹拌_撹拌終了予定時間
     */
    private FXHDD01 haisyutumaekakuhan_kakuhansyuuryouyotei_time;

    /**
     * 排出前撹拌_撹拌終了日
     */
    private FXHDD01 haisyutumaekakuhan_kakuhansyuuryou_day;

    /**
     * 排出前撹拌_撹拌終了時間
     */
    private FXHDD01 haisyutumaekakuhan_kakuhansyuuryou_time;

    /**
     * 排出前撹拌_撹拌担当者
     */
    private FXHDD01 haisyutumaekakuhan_kakuhantantousya;

    /**
     * 排出開始日
     */
    private FXHDD01 haisyutukaisi_day;

    /**
     * 排出開始時間
     */
    private FXHDD01 haisyutukaisi_time;

    /**
     * 排出終了日
     */
    private FXHDD01 haisyutusyuuryou_day;

    /**
     * 排出終了時間
     */
    private FXHDD01 haisyutusyuuryou_time;

    /**
     * 排出担当者
     */
    private FXHDD01 haisyututantousya;

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
     * ﾙﾂﾎﾞNo
     * @return the rutubono
     */
    public FXHDD01 getRutubono() {
        return rutubono;
    }

    /**
     * ﾙﾂﾎﾞNo
     * @param rutubono the rutubono to set
     */
    public void setRutubono(FXHDD01 rutubono) {
        this.rutubono = rutubono;
    }

    /**
     * ﾙﾂﾎﾞ風袋重量
     * @return the rutubofuutaijyuuryou
     */
    public FXHDD01 getRutubofuutaijyuuryou() {
        return rutubofuutaijyuuryou;
    }

    /**
     * ﾙﾂﾎﾞ風袋重量
     * @param rutubofuutaijyuuryou the rutubofuutaijyuuryou to set
     */
    public void setRutubofuutaijyuuryou(FXHDD01 rutubofuutaijyuuryou) {
        this.rutubofuutaijyuuryou = rutubofuutaijyuuryou;
    }

    /**
     * 乾燥前ｽﾘｯﾌﾟ重量
     * @return the kansoumaeslipjyuuryou
     */
    public FXHDD01 getKansoumaeslipjyuuryou() {
        return kansoumaeslipjyuuryou;
    }

    /**
     * 乾燥前ｽﾘｯﾌﾟ重量
     * @param kansoumaeslipjyuuryou the kansoumaeslipjyuuryou to set
     */
    public void setKansoumaeslipjyuuryou(FXHDD01 kansoumaeslipjyuuryou) {
        this.kansoumaeslipjyuuryou = kansoumaeslipjyuuryou;
    }

    /**
     * 乾燥機①
     * @return the kansouki1
     */
    public FXHDD01 getKansouki1() {
        return kansouki1;
    }

    /**
     * 乾燥機①
     * @param kansouki1 the kansouki1 to set
     */
    public void setKansouki1(FXHDD01 kansouki1) {
        this.kansouki1 = kansouki1;
    }

    /**
     * 乾燥温度①
     * @return the kansouondo1
     */
    public FXHDD01 getKansouondo1() {
        return kansouondo1;
    }

    /**
     * 乾燥温度①
     * @param kansouondo1 the kansouondo1 to set
     */
    public void setKansouondo1(FXHDD01 kansouondo1) {
        this.kansouondo1 = kansouondo1;
    }

    /**
     * 乾燥時間①
     * @return the kansoujikan1
     */
    public FXHDD01 getKansoujikan1() {
        return kansoujikan1;
    }

    /**
     * 乾燥時間①
     * @param kansoujikan1 the kansoujikan1 to set
     */
    public void setKansoujikan1(FXHDD01 kansoujikan1) {
        this.kansoujikan1 = kansoujikan1;
    }

    /**
     * 乾燥開始日①
     * @return the kansoukaisi1_day
     */
    public FXHDD01 getKansoukaisi1_day() {
        return kansoukaisi1_day;
    }

    /**
     * 乾燥開始日①
     * @param kansoukaisi1_day the kansoukaisi1_day to set
     */
    public void setKansoukaisi1_day(FXHDD01 kansoukaisi1_day) {
        this.kansoukaisi1_day = kansoukaisi1_day;
    }

    /**
     * 乾燥開始時間①
     * @return the kansoukaisi1_time
     */
    public FXHDD01 getKansoukaisi1_time() {
        return kansoukaisi1_time;
    }

    /**
     * 乾燥開始時間①
     * @param kansoukaisi1_time the kansoukaisi1_time to set
     */
    public void setKansoukaisi1_time(FXHDD01 kansoukaisi1_time) {
        this.kansoukaisi1_time = kansoukaisi1_time;
    }

    /**
     * 乾燥終了日①
     * @return the kansousyuuryou1_day
     */
    public FXHDD01 getKansousyuuryou1_day() {
        return kansousyuuryou1_day;
    }

    /**
     * 乾燥終了日①
     * @param kansousyuuryou1_day the kansousyuuryou1_day to set
     */
    public void setKansousyuuryou1_day(FXHDD01 kansousyuuryou1_day) {
        this.kansousyuuryou1_day = kansousyuuryou1_day;
    }

    /**
     * 乾燥終了時間①
     * @return the kansousyuuryou1_time
     */
    public FXHDD01 getKansousyuuryou1_time() {
        return kansousyuuryou1_time;
    }

    /**
     * 乾燥終了時間①
     * @param kansousyuuryou1_time the kansousyuuryou1_time to set
     */
    public void setKansousyuuryou1_time(FXHDD01 kansousyuuryou1_time) {
        this.kansousyuuryou1_time = kansousyuuryou1_time;
    }

    /**
     * 乾燥機②
     * @return the kansouki2
     */
    public FXHDD01 getKansouki2() {
        return kansouki2;
    }

    /**
     * 乾燥機②
     * @param kansouki2 the kansouki2 to set
     */
    public void setKansouki2(FXHDD01 kansouki2) {
        this.kansouki2 = kansouki2;
    }

    /**
     * 乾燥温度②
     * @return the kansouondo2
     */
    public FXHDD01 getKansouondo2() {
        return kansouondo2;
    }

    /**
     * 乾燥温度②
     * @param kansouondo2 the kansouondo2 to set
     */
    public void setKansouondo2(FXHDD01 kansouondo2) {
        this.kansouondo2 = kansouondo2;
    }

    /**
     * 乾燥時間②
     * @return the kansoujikan2
     */
    public FXHDD01 getKansoujikan2() {
        return kansoujikan2;
    }

    /**
     * 乾燥時間②
     * @param kansoujikan2 the kansoujikan2 to set
     */
    public void setKansoujikan2(FXHDD01 kansoujikan2) {
        this.kansoujikan2 = kansoujikan2;
    }

    /**
     * 乾燥開始日②
     * @return the kansoukaisi2_day
     */
    public FXHDD01 getKansoukaisi2_day() {
        return kansoukaisi2_day;
    }

    /**
     * 乾燥開始日②
     * @param kansoukaisi2_day the kansoukaisi2_day to set
     */
    public void setKansoukaisi2_day(FXHDD01 kansoukaisi2_day) {
        this.kansoukaisi2_day = kansoukaisi2_day;
    }

    /**
     * 乾燥開始時間②
     * @return the kansoukaisi2_time
     */
    public FXHDD01 getKansoukaisi2_time() {
        return kansoukaisi2_time;
    }

    /**
     * 乾燥開始時間②
     * @param kansoukaisi2_time the kansoukaisi2_time to set
     */
    public void setKansoukaisi2_time(FXHDD01 kansoukaisi2_time) {
        this.kansoukaisi2_time = kansoukaisi2_time;
    }

    /**
     * 乾燥終了日②
     * @return the kansousyuuryou2_day
     */
    public FXHDD01 getKansousyuuryou2_day() {
        return kansousyuuryou2_day;
    }

    /**
     * 乾燥終了日②
     * @param kansousyuuryou2_day the kansousyuuryou2_day to set
     */
    public void setKansousyuuryou2_day(FXHDD01 kansousyuuryou2_day) {
        this.kansousyuuryou2_day = kansousyuuryou2_day;
    }

    /**
     * 乾燥終了時間②
     * @return the kansousyuuryou2_time
     */
    public FXHDD01 getKansousyuuryou2_time() {
        return kansousyuuryou2_time;
    }

    /**
     * 乾燥終了時間②
     * @param kansousyuuryou2_time the kansousyuuryou2_time to set
     */
    public void setKansousyuuryou2_time(FXHDD01 kansousyuuryou2_time) {
        this.kansousyuuryou2_time = kansousyuuryou2_time;
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
     * 撹拌_撹拌ﾓｰﾄﾞ
     * @return the kakuhan_kakuhanmode
     */
    public FXHDD01 getKakuhan_kakuhanmode() {
        return kakuhan_kakuhanmode;
    }

    /**
     * 撹拌_撹拌ﾓｰﾄﾞ
     * @param kakuhan_kakuhanmode the kakuhan_kakuhanmode to set
     */
    public void setKakuhan_kakuhanmode(FXHDD01 kakuhan_kakuhanmode) {
        this.kakuhan_kakuhanmode = kakuhan_kakuhanmode;
    }

    /**
     * 撹拌_回転方向
     * @return the kakuhan_kaitenhoukou
     */
    public FXHDD01 getKakuhan_kaitenhoukou() {
        return kakuhan_kaitenhoukou;
    }

    /**
     * 撹拌_回転方向
     * @param kakuhan_kaitenhoukou the kakuhan_kaitenhoukou to set
     */
    public void setKakuhan_kaitenhoukou(FXHDD01 kakuhan_kaitenhoukou) {
        this.kakuhan_kaitenhoukou = kakuhan_kaitenhoukou;
    }

    /**
     * 撹拌_回転数(rpm)
     * @return the kakuhan_kaitensuu
     */
    public FXHDD01 getKakuhan_kaitensuu() {
        return kakuhan_kaitensuu;
    }

    /**
     * 撹拌_回転数(rpm)
     * @param kakuhan_kaitensuu the kakuhan_kaitensuu to set
     */
    public void setKakuhan_kaitensuu(FXHDD01 kakuhan_kaitensuu) {
        this.kakuhan_kaitensuu = kakuhan_kaitensuu;
    }

    /**
     * 撹拌_撹拌時間
     * @return the kakuhan_kakuhanjikan
     */
    public FXHDD01 getKakuhan_kakuhanjikan() {
        return kakuhan_kakuhanjikan;
    }

    /**
     * 撹拌_撹拌時間
     * @param kakuhan_kakuhanjikan the kakuhan_kakuhanjikan to set
     */
    public void setKakuhan_kakuhanjikan(FXHDD01 kakuhan_kakuhanjikan) {
        this.kakuhan_kakuhanjikan = kakuhan_kakuhanjikan;
    }

    /**
     * 撹拌_撹拌開始日
     * @return the kakuhan_kakuhankaisi_day
     */
    public FXHDD01 getKakuhan_kakuhankaisi_day() {
        return kakuhan_kakuhankaisi_day;
    }

    /**
     * 撹拌_撹拌開始日
     * @param kakuhan_kakuhankaisi_day the kakuhan_kakuhankaisi_day to set
     */
    public void setKakuhan_kakuhankaisi_day(FXHDD01 kakuhan_kakuhankaisi_day) {
        this.kakuhan_kakuhankaisi_day = kakuhan_kakuhankaisi_day;
    }

    /**
     * 撹拌_撹拌開始時間
     * @return the kakuhan_kakuhankaisi_time
     */
    public FXHDD01 getKakuhan_kakuhankaisi_time() {
        return kakuhan_kakuhankaisi_time;
    }

    /**
     * 撹拌_撹拌開始時間
     * @param kakuhan_kakuhankaisi_time the kakuhan_kakuhankaisi_time to set
     */
    public void setKakuhan_kakuhankaisi_time(FXHDD01 kakuhan_kakuhankaisi_time) {
        this.kakuhan_kakuhankaisi_time = kakuhan_kakuhankaisi_time;
    }

    /**
     * 開始電流値(A)
     * @return the kaisidenryuuti
     */
    public FXHDD01 getKaisidenryuuti() {
        return kaisidenryuuti;
    }

    /**
     * 開始電流値(A)
     * @param kaisidenryuuti the kaisidenryuuti to set
     */
    public void setKaisidenryuuti(FXHDD01 kaisidenryuuti) {
        this.kaisidenryuuti = kaisidenryuuti;
    }

    /**
     * 撹拌_撹拌終了予定日
     * @return the kakuhan_kakuhansyuuryouyotei_day
     */
    public FXHDD01 getKakuhan_kakuhansyuuryouyotei_day() {
        return kakuhan_kakuhansyuuryouyotei_day;
    }

    /**
     * 撹拌_撹拌終了予定日
     * @param kakuhan_kakuhansyuuryouyotei_day the kakuhan_kakuhansyuuryouyotei_day to set
     */
    public void setKakuhan_kakuhansyuuryouyotei_day(FXHDD01 kakuhan_kakuhansyuuryouyotei_day) {
        this.kakuhan_kakuhansyuuryouyotei_day = kakuhan_kakuhansyuuryouyotei_day;
    }

    /**
     * 撹拌_撹拌終了予定時間
     * @return the kakuhan_kakuhansyuuryouyotei_time
     */
    public FXHDD01 getKakuhan_kakuhansyuuryouyotei_time() {
        return kakuhan_kakuhansyuuryouyotei_time;
    }

    /**
     * 撹拌_撹拌終了予定時間
     * @param kakuhan_kakuhansyuuryouyotei_time the kakuhan_kakuhansyuuryouyotei_time to set
     */
    public void setKakuhan_kakuhansyuuryouyotei_time(FXHDD01 kakuhan_kakuhansyuuryouyotei_time) {
        this.kakuhan_kakuhansyuuryouyotei_time = kakuhan_kakuhansyuuryouyotei_time;
    }

    /**
     * 撹拌_撹拌終了日
     * @return the kakuhan_kakuhansyuuryou_day
     */
    public FXHDD01 getKakuhan_kakuhansyuuryou_day() {
        return kakuhan_kakuhansyuuryou_day;
    }

    /**
     * 撹拌_撹拌終了日
     * @param kakuhan_kakuhansyuuryou_day the kakuhan_kakuhansyuuryou_day to set
     */
    public void setKakuhan_kakuhansyuuryou_day(FXHDD01 kakuhan_kakuhansyuuryou_day) {
        this.kakuhan_kakuhansyuuryou_day = kakuhan_kakuhansyuuryou_day;
    }

    /**
     * 撹拌_撹拌終了時間
     * @return the kakuhan_kakuhansyuuryou_time
     */
    public FXHDD01 getKakuhan_kakuhansyuuryou_time() {
        return kakuhan_kakuhansyuuryou_time;
    }

    /**
     * 撹拌_撹拌終了時間
     * @param kakuhan_kakuhansyuuryou_time the kakuhan_kakuhansyuuryou_time to set
     */
    public void setKakuhan_kakuhansyuuryou_time(FXHDD01 kakuhan_kakuhansyuuryou_time) {
        this.kakuhan_kakuhansyuuryou_time = kakuhan_kakuhansyuuryou_time;
    }

    /**
     * 終了電流値(A)
     * @return the syuuryoudenryuuti
     */
    public FXHDD01 getSyuuryoudenryuuti() {
        return syuuryoudenryuuti;
    }

    /**
     * 終了電流値(A)
     * @param syuuryoudenryuuti the syuuryoudenryuuti to set
     */
    public void setSyuuryoudenryuuti(FXHDD01 syuuryoudenryuuti) {
        this.syuuryoudenryuuti = syuuryoudenryuuti;
    }

    /**
     * 撹拌_撹拌担当者
     * @return the kakuhan_kakuhantantousya
     */
    public FXHDD01 getKakuhan_kakuhantantousya() {
        return kakuhan_kakuhantantousya;
    }

    /**
     * 撹拌_撹拌担当者
     * @param kakuhan_kakuhantantousya the kakuhan_kakuhantantousya to set
     */
    public void setKakuhan_kakuhantantousya(FXHDD01 kakuhan_kakuhantantousya) {
        this.kakuhan_kakuhantantousya = kakuhan_kakuhantantousya;
    }

    /**
     * 排出前撹拌_撹拌ﾓｰﾄﾞ
     * @return the haisyutumaekakuhan_kakuhanmode
     */
    public FXHDD01 getHaisyutumaekakuhan_kakuhanmode() {
        return haisyutumaekakuhan_kakuhanmode;
    }

    /**
     * 排出前撹拌_撹拌ﾓｰﾄﾞ
     * @param haisyutumaekakuhan_kakuhanmode the haisyutumaekakuhan_kakuhanmode to set
     */
    public void setHaisyutumaekakuhan_kakuhanmode(FXHDD01 haisyutumaekakuhan_kakuhanmode) {
        this.haisyutumaekakuhan_kakuhanmode = haisyutumaekakuhan_kakuhanmode;
    }

    /**
     * 排出前撹拌_回転方向
     * @return the haisyutumaekakuhan_kaitenhoukou
     */
    public FXHDD01 getHaisyutumaekakuhan_kaitenhoukou() {
        return haisyutumaekakuhan_kaitenhoukou;
    }

    /**
     * 排出前撹拌_回転方向
     * @param haisyutumaekakuhan_kaitenhoukou the haisyutumaekakuhan_kaitenhoukou to set
     */
    public void setHaisyutumaekakuhan_kaitenhoukou(FXHDD01 haisyutumaekakuhan_kaitenhoukou) {
        this.haisyutumaekakuhan_kaitenhoukou = haisyutumaekakuhan_kaitenhoukou;
    }

    /**
     * 排出前撹拌_回転数(rpm)
     * @return the haisyutumaekakuhan_kaitensuu
     */
    public FXHDD01 getHaisyutumaekakuhan_kaitensuu() {
        return haisyutumaekakuhan_kaitensuu;
    }

    /**
     * 排出前撹拌_回転数(rpm)
     * @param haisyutumaekakuhan_kaitensuu the haisyutumaekakuhan_kaitensuu to set
     */
    public void setHaisyutumaekakuhan_kaitensuu(FXHDD01 haisyutumaekakuhan_kaitensuu) {
        this.haisyutumaekakuhan_kaitensuu = haisyutumaekakuhan_kaitensuu;
    }

    /**
     * 排出前撹拌_撹拌時間
     * @return the haisyutumaekakuhan_kakuhanjikan
     */
    public FXHDD01 getHaisyutumaekakuhan_kakuhanjikan() {
        return haisyutumaekakuhan_kakuhanjikan;
    }

    /**
     * 排出前撹拌_撹拌時間
     * @param haisyutumaekakuhan_kakuhanjikan the haisyutumaekakuhan_kakuhanjikan to set
     */
    public void setHaisyutumaekakuhan_kakuhanjikan(FXHDD01 haisyutumaekakuhan_kakuhanjikan) {
        this.haisyutumaekakuhan_kakuhanjikan = haisyutumaekakuhan_kakuhanjikan;
    }

    /**
     * 排出前撹拌_撹拌開始日
     * @return the haisyutumaekakuhan_kakuhankaisi_day
     */
    public FXHDD01 getHaisyutumaekakuhan_kakuhankaisi_day() {
        return haisyutumaekakuhan_kakuhankaisi_day;
    }

    /**
     * 排出前撹拌_撹拌開始日
     * @param haisyutumaekakuhan_kakuhankaisi_day the haisyutumaekakuhan_kakuhankaisi_day to set
     */
    public void setHaisyutumaekakuhan_kakuhankaisi_day(FXHDD01 haisyutumaekakuhan_kakuhankaisi_day) {
        this.haisyutumaekakuhan_kakuhankaisi_day = haisyutumaekakuhan_kakuhankaisi_day;
    }

    /**
     * 排出前撹拌_撹拌開始時間
     * @return the haisyutumaekakuhan_kakuhankaisi_time
     */
    public FXHDD01 getHaisyutumaekakuhan_kakuhankaisi_time() {
        return haisyutumaekakuhan_kakuhankaisi_time;
    }

    /**
     * 排出前撹拌_撹拌開始時間
     * @param haisyutumaekakuhan_kakuhankaisi_time the haisyutumaekakuhan_kakuhankaisi_time to set
     */
    public void setHaisyutumaekakuhan_kakuhankaisi_time(FXHDD01 haisyutumaekakuhan_kakuhankaisi_time) {
        this.haisyutumaekakuhan_kakuhankaisi_time = haisyutumaekakuhan_kakuhankaisi_time;
    }

    /**
     * 排出前撹拌_撹拌終了予定日
     * @return the haisyutumaekakuhan_kakuhansyuuryouyotei_day
     */
    public FXHDD01 getHaisyutumaekakuhan_kakuhansyuuryouyotei_day() {
        return haisyutumaekakuhan_kakuhansyuuryouyotei_day;
    }

    /**
     * 排出前撹拌_撹拌終了予定日
     * @param haisyutumaekakuhan_kakuhansyuuryouyotei_day the haisyutumaekakuhan_kakuhansyuuryouyotei_day to set
     */
    public void setHaisyutumaekakuhan_kakuhansyuuryouyotei_day(FXHDD01 haisyutumaekakuhan_kakuhansyuuryouyotei_day) {
        this.haisyutumaekakuhan_kakuhansyuuryouyotei_day = haisyutumaekakuhan_kakuhansyuuryouyotei_day;
    }

    /**
     * 排出前撹拌_撹拌終了予定時間
     * @return the haisyutumaekakuhan_kakuhansyuuryouyotei_time
     */
    public FXHDD01 getHaisyutumaekakuhan_kakuhansyuuryouyotei_time() {
        return haisyutumaekakuhan_kakuhansyuuryouyotei_time;
    }

    /**
     * 排出前撹拌_撹拌終了予定時間
     * @param haisyutumaekakuhan_kakuhansyuuryouyotei_time the haisyutumaekakuhan_kakuhansyuuryouyotei_time to set
     */
    public void setHaisyutumaekakuhan_kakuhansyuuryouyotei_time(FXHDD01 haisyutumaekakuhan_kakuhansyuuryouyotei_time) {
        this.haisyutumaekakuhan_kakuhansyuuryouyotei_time = haisyutumaekakuhan_kakuhansyuuryouyotei_time;
    }

    /**
     * 排出前撹拌_撹拌終了日
     * @return the haisyutumaekakuhan_kakuhansyuuryou_day
     */
    public FXHDD01 getHaisyutumaekakuhan_kakuhansyuuryou_day() {
        return haisyutumaekakuhan_kakuhansyuuryou_day;
    }

    /**
     * 排出前撹拌_撹拌終了日
     * @param haisyutumaekakuhan_kakuhansyuuryou_day the haisyutumaekakuhan_kakuhansyuuryou_day to set
     */
    public void setHaisyutumaekakuhan_kakuhansyuuryou_day(FXHDD01 haisyutumaekakuhan_kakuhansyuuryou_day) {
        this.haisyutumaekakuhan_kakuhansyuuryou_day = haisyutumaekakuhan_kakuhansyuuryou_day;
    }

    /**
     * 排出前撹拌_撹拌終了時間
     * @return the haisyutumaekakuhan_kakuhansyuuryou_time
     */
    public FXHDD01 getHaisyutumaekakuhan_kakuhansyuuryou_time() {
        return haisyutumaekakuhan_kakuhansyuuryou_time;
    }

    /**
     * 排出前撹拌_撹拌終了時間
     * @param haisyutumaekakuhan_kakuhansyuuryou_time the haisyutumaekakuhan_kakuhansyuuryou_time to set
     */
    public void setHaisyutumaekakuhan_kakuhansyuuryou_time(FXHDD01 haisyutumaekakuhan_kakuhansyuuryou_time) {
        this.haisyutumaekakuhan_kakuhansyuuryou_time = haisyutumaekakuhan_kakuhansyuuryou_time;
    }

    /**
     * 排出前撹拌_撹拌担当者
     * @return the haisyutumaekakuhan_kakuhantantousya
     */
    public FXHDD01 getHaisyutumaekakuhan_kakuhantantousya() {
        return haisyutumaekakuhan_kakuhantantousya;
    }

    /**
     * 排出前撹拌_撹拌担当者
     * @param haisyutumaekakuhan_kakuhantantousya the haisyutumaekakuhan_kakuhantantousya to set
     */
    public void setHaisyutumaekakuhan_kakuhantantousya(FXHDD01 haisyutumaekakuhan_kakuhantantousya) {
        this.haisyutumaekakuhan_kakuhantantousya = haisyutumaekakuhan_kakuhantantousya;
    }

    /**
     * 排出開始日
     * @return the haisyutukaisi_day
     */
    public FXHDD01 getHaisyutukaisi_day() {
        return haisyutukaisi_day;
    }

    /**
     * 排出開始日
     * @param haisyutukaisi_day the haisyutukaisi_day to set
     */
    public void setHaisyutukaisi_day(FXHDD01 haisyutukaisi_day) {
        this.haisyutukaisi_day = haisyutukaisi_day;
    }

    /**
     * 排出開始時間
     * @return the haisyutukaisi_time
     */
    public FXHDD01 getHaisyutukaisi_time() {
        return haisyutukaisi_time;
    }

    /**
     * 排出開始時間
     * @param haisyutukaisi_time the haisyutukaisi_time to set
     */
    public void setHaisyutukaisi_time(FXHDD01 haisyutukaisi_time) {
        this.haisyutukaisi_time = haisyutukaisi_time;
    }

    /**
     * 排出終了日
     * @return the haisyutusyuuryou_day
     */
    public FXHDD01 getHaisyutusyuuryou_day() {
        return haisyutusyuuryou_day;
    }

    /**
     * 排出終了日
     * @param haisyutusyuuryou_day the haisyutusyuuryou_day to set
     */
    public void setHaisyutusyuuryou_day(FXHDD01 haisyutusyuuryou_day) {
        this.haisyutusyuuryou_day = haisyutusyuuryou_day;
    }

    /**
     * 排出終了時間
     * @return the haisyutusyuuryou_time
     */
    public FXHDD01 getHaisyutusyuuryou_time() {
        return haisyutusyuuryou_time;
    }

    /**
     * 排出終了時間
     * @param haisyutusyuuryou_time the haisyutusyuuryou_time to set
     */
    public void setHaisyutusyuuryou_time(FXHDD01 haisyutusyuuryou_time) {
        this.haisyutusyuuryou_time = haisyutusyuuryou_time;
    }

    /**
     * 排出担当者
     * @return the haisyututantousya
     */
    public FXHDD01 getHaisyututantousya() {
        return haisyututantousya;
    }

    /**
     * 排出担当者
     * @param haisyututantousya the haisyututantousya to set
     */
    public void setHaisyututantousya(FXHDD01 haisyututantousya) {
        this.haisyututantousya = haisyututantousya;
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