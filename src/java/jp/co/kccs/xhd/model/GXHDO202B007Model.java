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
 * 変更日       2021/10/15<br>
 * 計画書No     MB2101-DK002<br>
 * 変更者       KCSS K.Jo<br>
 * 変更理由     新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 添加材ｽﾗﾘｰ作製・ｶﾞﾗｽ履歴検索画面のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/10/15
 */
public class GXHDO202B007Model implements Serializable {
    /** WIPﾛｯﾄNo */
    private String lotno = "";

    /** 添加材ｽﾗﾘｰ品名 */
    private String tenkahinmei = "";

    /** 添加材ｽﾗﾘｰLotNo */
    private String tenkalotno = "";

    /** ﾛｯﾄ区分 */
    private String lotkubun = "";

    /** 固形分測定日時 */
    private Timestamp kokeibunsokuteinichiji = null;

    /** 容器重量 */
    private Integer youkijyuuryou = null;

    /** 総重量 */
    private Integer soujyuuryou = null;

    /** 正味重量 */
    private Integer syoumijyuuryou = null;

    /** 撹拌機 */
    private String kakuhanki = "";

    /** 撹拌時間 */
    private String kakuhajikan = "";

    /** 撹拌開始日時 */
    private Timestamp kakuhankaisinichiji = null;

    /** 撹拌終了日時 */
    private Timestamp kakuhansyuuryounichiji = null;

    /** 撹拌担当者 */
    private String kakuhantantousya = "";

    /** ﾙﾂﾎﾞの種類 */
    private String rutubo = "";

    /** 風袋重量 */
    private BigDecimal fuutaijyuuryou = null;

    /** 乾燥前重量 */
    private BigDecimal kansoumaejyuuryou = null;

    /** 乾燥方法① */
    private String kansouhouhou1 = "";

    /** 乾燥温度① */
    private String kansouondo1 = "";

    /** 乾燥時間① */
    private String kansoujikan1 = "";

    /** 乾燥開始日時① */
    private Timestamp kansoukaisinichiji1 = null;

    /** 乾燥終了日時① */
    private Timestamp kansousyuuryounichiji1 = null;

    /** 乾燥方法② */
    private String kansouhouhou2 = "";

    /** 乾燥温度② */
    private String kansouondo2 = "";

    /** 乾燥時間② */
    private String kansoujikan2 = "";

    /** 乾燥開始日時② */
    private Timestamp kansoukaisinichiji2 = null;

    /** 乾燥終了日時② */
    private Timestamp kansousyuuryounichiji2 = null;

    /** 乾燥後総重量 */
    private BigDecimal kansougosoujyuuryou = null;

    /** 乾燥後重量 */
    private BigDecimal kansougojyuuryou = null;

    /** 固形分 */
    private BigDecimal kokeibun = null;

    /** 固形分測定担当者 */
    private String kokeibunsokuteitantousya = "";

    /** 固形分補正量 */
    private Integer kokeibunhoseiryou = null;

    /** 溶剤①添加量 */
    private Integer youzai1tenkaryou = null;

    /** 溶剤①調合量 */
    private Integer youzai1tyougouryou = null;

    /** 溶剤②添加量 */
    private Integer youzai2tenkaryou = null;

    /** 溶剤②調合量 */
    private Integer youzai2tyougouryou = null;

    /** 2回目撹拌機 */
    private String kakuhanki2 = "";

    /** 2回目撹拌時間 */
    private String kakuhajikan2 = "";

    /** 2回目撹拌開始日時 */
    private Timestamp kakuhankaisinichiji2 = null;

    /** 2回目撹拌終了日時 */
    private Timestamp kakuhansyuuryounichiji2 = null;

    /** 2回目撹拌担当者 */
    private String kakuhantantousya2 = "";

    /** 2回目ﾙﾂﾎﾞの種類 */
    private String rutubo2 = "";

    /** 2回目風袋重量 */
    private BigDecimal fuutaijyuuryou2 = null;

    /** 2回目乾燥前重量 */
    private BigDecimal kansoumaejyuuryou2 = null;

    /** 2回目乾燥方法① */
    private String kansouhouhou1_2 = "";

    /** 2回目乾燥温度① */
    private String kansouondo1_2 = "";

    /** 2回目乾燥時間① */
    private String kansoujikan1_2 = "";

    /** 2回目乾燥開始日時① */
    private Timestamp kansoukaisinichiji1_2 = null;

    /** 2回目乾燥終了日時① */
    private Timestamp kansousyuuryounichiji1_2 = null;

    /** 2回目乾燥後総重量 */
    private BigDecimal kansougosoujyuuryou2 = null;

    /** 2回目乾燥後重量 */
    private BigDecimal kansougojyuuryou2 = null;

    /** 2回目固形分 */
    private BigDecimal kokeibun2 = null;

    /** 2回目固形分測定担当者 */
    private String kokeibunsokuteitantousya2 = "";

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
     * 添加材ｽﾗﾘｰ品名
     * @return the tenkahinmei
     */
    public String getTenkahinmei() {
        return tenkahinmei;
    }

    /**
     * 添加材ｽﾗﾘｰ品名
     * @param tenkahinmei the tenkahinmei to set
     */
    public void setTenkahinmei(String tenkahinmei) {
        this.tenkahinmei = tenkahinmei;
    }

    /**
     * 添加材ｽﾗﾘｰLotNo
     * @return the tenkalotno
     */
    public String getTenkalotno() {
        return tenkalotno;
    }

    /**
     * 添加材ｽﾗﾘｰLotNo
     * @param tenkalotno the tenkalotno to set
     */
    public void setTenkalotno(String tenkalotno) {
        this.tenkalotno = tenkalotno;
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
     * 固形分測定日時
     * @return the kokeibunsokuteinichiji
     */
    public Timestamp getKokeibunsokuteinichiji() {
        return kokeibunsokuteinichiji;
    }

    /**
     * 固形分測定日時
     * @param kokeibunsokuteinichiji the kokeibunsokuteinichiji to set
     */
    public void setKokeibunsokuteinichiji(Timestamp kokeibunsokuteinichiji) {
        this.kokeibunsokuteinichiji = kokeibunsokuteinichiji;
    }

    /**
     * 容器重量
     * @return the youkijyuuryou
     */
    public Integer getYoukijyuuryou() {
        return youkijyuuryou;
    }

    /**
     * 容器重量
     * @param youkijyuuryou the youkijyuuryou to set
     */
    public void setYoukijyuuryou(Integer youkijyuuryou) {
        this.youkijyuuryou = youkijyuuryou;
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
     * 撹拌機
     * @return the kakuhanki
     */
    public String getKakuhanki() {
        return kakuhanki;
    }

    /**
     * 撹拌機
     * @param kakuhanki the kakuhanki to set
     */
    public void setKakuhanki(String kakuhanki) {
        this.kakuhanki = kakuhanki;
    }

    /**
     * 撹拌時間
     * @return the kakuhajikan
     */
    public String getKakuhajikan() {
        return kakuhajikan;
    }

    /**
     * 撹拌時間
     * @param kakuhajikan the kakuhajikan to set
     */
    public void setKakuhajikan(String kakuhajikan) {
        this.kakuhajikan = kakuhajikan;
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
     * 撹拌担当者
     * @return the kakuhantantousya
     */
    public String getKakuhantantousya() {
        return kakuhantantousya;
    }

    /**
     * 撹拌担当者
     * @param kakuhantantousya the kakuhantantousya to set
     */
    public void setKakuhantantousya(String kakuhantantousya) {
        this.kakuhantantousya = kakuhantantousya;
    }

    /**
     * ﾙﾂﾎﾞの種類
     * @return the rutubo
     */
    public String getRutubo() {
        return rutubo;
    }

    /**
     * ﾙﾂﾎﾞの種類
     * @param rutubo the rutubo to set
     */
    public void setRutubo(String rutubo) {
        this.rutubo = rutubo;
    }

    /**
     * 風袋重量
     * @return the fuutaijyuuryou
     */
    public BigDecimal getFuutaijyuuryou() {
        return fuutaijyuuryou;
    }

    /**
     * 風袋重量
     * @param fuutaijyuuryou the fuutaijyuuryou to set
     */
    public void setFuutaijyuuryou(BigDecimal fuutaijyuuryou) {
        this.fuutaijyuuryou = fuutaijyuuryou;
    }

    /**
     * 乾燥前重量
     * @return the kansoumaejyuuryou
     */
    public BigDecimal getKansoumaejyuuryou() {
        return kansoumaejyuuryou;
    }

    /**
     * 乾燥前重量
     * @param kansoumaejyuuryou the kansoumaejyuuryou to set
     */
    public void setKansoumaejyuuryou(BigDecimal kansoumaejyuuryou) {
        this.kansoumaejyuuryou = kansoumaejyuuryou;
    }

    /**
     * 乾燥方法①
     * @return the kansouhouhou1
     */
    public String getKansouhouhou1() {
        return kansouhouhou1;
    }

    /**
     * 乾燥方法①
     * @param kansouhouhou1 the kansouhouhou1 to set
     */
    public void setKansouhouhou1(String kansouhouhou1) {
        this.kansouhouhou1 = kansouhouhou1;
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
     * 乾燥方法②
     * @return the kansouhouhou2
     */
    public String getKansouhouhou2() {
        return kansouhouhou2;
    }

    /**
     * 乾燥方法②
     * @param kansouhouhou2 the kansouhouhou2 to set
     */
    public void setKansouhouhou2(String kansouhouhou2) {
        this.kansouhouhou2 = kansouhouhou2;
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
     * 乾燥後重量
     * @return the kansougojyuuryou
     */
    public BigDecimal getKansougojyuuryou() {
        return kansougojyuuryou;
    }

    /**
     * 乾燥後重量
     * @param kansougojyuuryou the kansougojyuuryou to set
     */
    public void setKansougojyuuryou(BigDecimal kansougojyuuryou) {
        this.kansougojyuuryou = kansougojyuuryou;
    }

    /**
     * 固形分
     * @return the kokeibun
     */
    public BigDecimal getKokeibun() {
        return kokeibun;
    }

    /**
     * 固形分
     * @param kokeibun the kokeibun to set
     */
    public void setKokeibun(BigDecimal kokeibun) {
        this.kokeibun = kokeibun;
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
     * 固形分補正量
     * @return the kokeibunhoseiryou
     */
    public Integer getKokeibunhoseiryou() {
        return kokeibunhoseiryou;
    }

    /**
     * 固形分補正量
     * @param kokeibunhoseiryou the kokeibunhoseiryou to set
     */
    public void setKokeibunhoseiryou(Integer kokeibunhoseiryou) {
        this.kokeibunhoseiryou = kokeibunhoseiryou;
    }

    /**
     * 溶剤①添加量
     * @return the youzai1tenkaryou
     */
    public Integer getYouzai1tenkaryou() {
        return youzai1tenkaryou;
    }

    /**
     * 溶剤①添加量
     * @param youzai1tenkaryou the youzai1tenkaryou to set
     */
    public void setYouzai1tenkaryou(Integer youzai1tenkaryou) {
        this.youzai1tenkaryou = youzai1tenkaryou;
    }

    /**
     * 溶剤①調合量
     * @return the youzai1tyougouryou
     */
    public Integer getYouzai1tyougouryou() {
        return youzai1tyougouryou;
    }

    /**
     * 溶剤①調合量
     * @param youzai1tyougouryou the youzai1tyougouryou to set
     */
    public void setYouzai1tyougouryou(Integer youzai1tyougouryou) {
        this.youzai1tyougouryou = youzai1tyougouryou;
    }

    /**
     * 溶剤②添加量
     * @return the youzai2tenkaryou
     */
    public Integer getYouzai2tenkaryou() {
        return youzai2tenkaryou;
    }

    /**
     * 溶剤②添加量
     * @param youzai2tenkaryou the youzai2tenkaryou to set
     */
    public void setYouzai2tenkaryou(Integer youzai2tenkaryou) {
        this.youzai2tenkaryou = youzai2tenkaryou;
    }

    /**
     * 溶剤②調合量
     * @return the youzai2tyougouryou
     */
    public Integer getYouzai2tyougouryou() {
        return youzai2tyougouryou;
    }

    /**
     * 溶剤②調合量
     * @param youzai2tyougouryou the youzai2tyougouryou to set
     */
    public void setYouzai2tyougouryou(Integer youzai2tyougouryou) {
        this.youzai2tyougouryou = youzai2tyougouryou;
    }

    /**
     * 2回目撹拌機
     * @return the kakuhanki2
     */
    public String getKakuhanki2() {
        return kakuhanki2;
    }

    /**
     * 2回目撹拌機
     * @param kakuhanki2 the kakuhanki2 to set
     */
    public void setKakuhanki2(String kakuhanki2) {
        this.kakuhanki2 = kakuhanki2;
    }

    /**
     * 2回目撹拌時間
     * @return the kakuhajikan2
     */
    public String getKakuhajikan2() {
        return kakuhajikan2;
    }

    /**
     * 2回目撹拌時間
     * @param kakuhajikan2 the kakuhajikan2 to set
     */
    public void setKakuhajikan2(String kakuhajikan2) {
        this.kakuhajikan2 = kakuhajikan2;
    }

    /**
     * 2回目撹拌開始日時
     * @return the kakuhankaisinichiji2
     */
    public Timestamp getKakuhankaisinichiji2() {
        return kakuhankaisinichiji2;
    }

    /**
     * 2回目撹拌開始日時
     * @param kakuhankaisinichiji2 the kakuhankaisinichiji2 to set
     */
    public void setKakuhankaisinichiji2(Timestamp kakuhankaisinichiji2) {
        this.kakuhankaisinichiji2 = kakuhankaisinichiji2;
    }

    /**
     * 2回目撹拌終了日時
     * @return the kakuhansyuuryounichiji2
     */
    public Timestamp getKakuhansyuuryounichiji2() {
        return kakuhansyuuryounichiji2;
    }

    /**
     * 2回目撹拌終了日時
     * @param kakuhansyuuryounichiji2 the kakuhansyuuryounichiji2 to set
     */
    public void setKakuhansyuuryounichiji2(Timestamp kakuhansyuuryounichiji2) {
        this.kakuhansyuuryounichiji2 = kakuhansyuuryounichiji2;
    }

    /**
     * 2回目撹拌担当者
     * @return the kakuhantantousya2
     */
    public String getKakuhantantousya2() {
        return kakuhantantousya2;
    }

    /**
     * 2回目撹拌担当者
     * @param kakuhantantousya2 the kakuhantantousya2 to set
     */
    public void setKakuhantantousya2(String kakuhantantousya2) {
        this.kakuhantantousya2 = kakuhantantousya2;
    }

    /**
     * 2回目ﾙﾂﾎﾞの種類
     * @return the rutubo2
     */
    public String getRutubo2() {
        return rutubo2;
    }

    /**
     * 2回目ﾙﾂﾎﾞの種類
     * @param rutubo2 the rutubo2 to set
     */
    public void setRutubo2(String rutubo2) {
        this.rutubo2 = rutubo2;
    }

    /**
     * 2回目風袋重量
     * @return the fuutaijyuuryou2
     */
    public BigDecimal getFuutaijyuuryou2() {
        return fuutaijyuuryou2;
    }

    /**
     * 2回目風袋重量
     * @param fuutaijyuuryou2 the fuutaijyuuryou2 to set
     */
    public void setFuutaijyuuryou2(BigDecimal fuutaijyuuryou2) {
        this.fuutaijyuuryou2 = fuutaijyuuryou2;
    }

    /**
     * 2回目乾燥前重量
     * @return the kansoumaejyuuryou2
     */
    public BigDecimal getKansoumaejyuuryou2() {
        return kansoumaejyuuryou2;
    }

    /**
     * 2回目乾燥前重量
     * @param kansoumaejyuuryou2 the kansoumaejyuuryou2 to set
     */
    public void setKansoumaejyuuryou2(BigDecimal kansoumaejyuuryou2) {
        this.kansoumaejyuuryou2 = kansoumaejyuuryou2;
    }

    /**
     * 2回目乾燥方法①
     * @return the kansouhouhou1_2
     */
    public String getKansouhouhou1_2() {
        return kansouhouhou1_2;
    }

    /**
     * 2回目乾燥方法①
     * @param kansouhouhou1_2 the kansouhouhou1_2 to set
     */
    public void setKansouhouhou1_2(String kansouhouhou1_2) {
        this.kansouhouhou1_2 = kansouhouhou1_2;
    }

    /**
     * 2回目乾燥温度①
     * @return the kansouondo1_2
     */
    public String getKansouondo1_2() {
        return kansouondo1_2;
    }

    /**
     * 2回目乾燥温度①
     * @param kansouondo1_2 the kansouondo1_2 to set
     */
    public void setKansouondo1_2(String kansouondo1_2) {
        this.kansouondo1_2 = kansouondo1_2;
    }

    /**
     * 2回目乾燥時間①
     * @return the kansoujikan1_2
     */
    public String getKansoujikan1_2() {
        return kansoujikan1_2;
    }

    /**
     * 2回目乾燥時間①
     * @param kansoujikan1_2 the kansoujikan1_2 to set
     */
    public void setKansoujikan1_2(String kansoujikan1_2) {
        this.kansoujikan1_2 = kansoujikan1_2;
    }

    /**
     * 2回目乾燥開始日時①
     * @return the kansoukaisinichiji1_2
     */
    public Timestamp getKansoukaisinichiji1_2() {
        return kansoukaisinichiji1_2;
    }

    /**
     * 2回目乾燥開始日時①
     * @param kansoukaisinichiji1_2 the kansoukaisinichiji1_2 to set
     */
    public void setKansoukaisinichiji1_2(Timestamp kansoukaisinichiji1_2) {
        this.kansoukaisinichiji1_2 = kansoukaisinichiji1_2;
    }

    /**
     * 2回目乾燥終了日時①
     * @return the kansousyuuryounichiji1_2
     */
    public Timestamp getKansousyuuryounichiji1_2() {
        return kansousyuuryounichiji1_2;
    }

    /**
     * 2回目乾燥終了日時①
     * @param kansousyuuryounichiji1_2 the kansousyuuryounichiji1_2 to set
     */
    public void setKansousyuuryounichiji1_2(Timestamp kansousyuuryounichiji1_2) {
        this.kansousyuuryounichiji1_2 = kansousyuuryounichiji1_2;
    }

    /**
     * 2回目乾燥後総重量
     * @return the kansougosoujyuuryou2
     */
    public BigDecimal getKansougosoujyuuryou2() {
        return kansougosoujyuuryou2;
    }

    /**
     * 2回目乾燥後総重量
     * @param kansougosoujyuuryou2 the kansougosoujyuuryou2 to set
     */
    public void setKansougosoujyuuryou2(BigDecimal kansougosoujyuuryou2) {
        this.kansougosoujyuuryou2 = kansougosoujyuuryou2;
    }

    /**
     * 2回目乾燥後重量
     * @return the kansougojyuuryou2
     */
    public BigDecimal getKansougojyuuryou2() {
        return kansougojyuuryou2;
    }

    /**
     * 2回目乾燥後重量
     * @param kansougojyuuryou2 the kansougojyuuryou2 to set
     */
    public void setKansougojyuuryou2(BigDecimal kansougojyuuryou2) {
        this.kansougojyuuryou2 = kansougojyuuryou2;
    }

    /**
     * 2回目固形分
     * @return the kokeibun2
     */
    public BigDecimal getKokeibun2() {
        return kokeibun2;
    }

    /**
     * 2回目固形分
     * @param kokeibun2 the kokeibun2 to set
     */
    public void setKokeibun2(BigDecimal kokeibun2) {
        this.kokeibun2 = kokeibun2;
    }

    /**
     * 2回目固形分測定担当者
     * @return the kokeibunsokuteitantousya2
     */
    public String getKokeibunsokuteitantousya2() {
        return kokeibunsokuteitantousya2;
    }

    /**
     * 2回目固形分測定担当者
     * @param kokeibunsokuteitantousya2 the kokeibunsokuteitantousya2 to set
     */
    public void setKokeibunsokuteitantousya2(String kokeibunsokuteitantousya2) {
        this.kokeibunsokuteitantousya2 = kokeibunsokuteitantousya2;
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