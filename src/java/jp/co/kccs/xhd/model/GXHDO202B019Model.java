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
 * 変更日       2021/11/23<br>
 * 計画書No     MB2101-DK002<br>
 * 変更者       KCSS K.Jo<br>
 * 変更理由     新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 誘電体ｽﾗﾘｰ作製・添加材ｽﾗﾘｰ固形分測定履歴検索画面のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/11/23
 */
public class GXHDO202B019Model implements Serializable {
    /** WIPﾛｯﾄNo */
    private String lotno = "";

    /** 誘電体ｽﾗﾘｰ品名 */
    private String yuudentaislurryhinmei = "";

    /** 誘電体ｽﾗﾘｰLotNo */
    private String yuudentaislurrylotno = "";

    /** ﾛｯﾄ区分 */
    private String lotkubun = "";

    /** 原料LotNo */
    private String genryoulotno = "";

    /** 原料記号 */
    private String genryoukigou = "";

    /** 添加材ｽﾗﾘｰ品名 */
    private String tenkazaislurryhinmei = "";

    /** 添加材ｽﾗﾘｰLotNo */
    private String tenkazaislurrylotno = "";

    /** 風袋重量① */
    private BigDecimal fuutaijyuuryou1 = null;

    /** 風袋重量② */
    private BigDecimal fuutaijyuuryou2 = null;

    /** 風袋重量③ */
    private BigDecimal fuutaijyuuryou3 = null;

    /** 総重量① */
    private Integer soujyuuryou1 = null;

    /** 総重量② */
    private Integer soujyuuryou2 = null;

    /** 総重量③ */
    private Integer soujyuuryou3 = null;

    /** 添加材ｽﾗﾘｰ重量 */
    private Integer tenkazaislurryjyuuryou = null;

    /** 撹拌機 */
    private String kakuhanki = "";

    /** 回転数 */
    private String kaitensuu = "";

    /** 撹拌時間 */
    private String kakuhanjikan = "";

    /** 撹拌開始日時 */
    private Timestamp kakuhankaisinichiji = null;

    /** 撹拌終了日時 */
    private Timestamp kakuhansyuuryounichiji = null;

    /** 乾燥皿種類 */
    private String kansouzarasyurui = "";

    /** ｱﾙﾐ皿風袋重量① */
    private BigDecimal arumizarafuutaijyuuryou1 = null;

    /** 乾燥前ｽﾗﾘｰ重量① */
    private BigDecimal kansoumaeslurryjyuuryou1 = null;

    /** ｱﾙﾐ皿風袋重量② */
    private BigDecimal arumizarafuutaijyuuryou2 = null;

    /** 乾燥前ｽﾗﾘｰ重量② */
    private BigDecimal kansoumaeslurryjyuuryou2 = null;

    /** ｱﾙﾐ皿風袋重量③ */
    private BigDecimal arumizarafuutaijyuuryou3 = null;

    /** 乾燥前ｽﾗﾘｰ重量③ */
    private BigDecimal kansoumaeslurryjyuuryou3 = null;

    /** 乾燥機① */
    private String kansouki1 = "";

    /** 乾燥温度① */
    private String kansouondo1 = "";

    /** 乾燥時間① */
    private String kansoujikan1 = "";

    /** 乾燥開始日時① */
    private Timestamp kansoukaisinichiji1 = null;

    /** 乾燥終了日時① */
    private Timestamp kansousyuuryounichiji1 = null;

    /** 乾燥機② */
    private String kansouki2 = "";

    /** 乾燥温度② */
    private String kansouondo2 = "";

    /** 乾燥時間② */
    private String kansoujikan2 = "";

    /** 乾燥開始日時② */
    private Timestamp kansoukaisinichiji2 = null;

    /** 乾燥終了日時② */
    private Timestamp kansousyuuryounichiji2 = null;

    /** 冷却時間 */
    private String reikyakujikan = "";

    /** 乾燥後総重量① */
    private BigDecimal kansougosoujyuuryou1 = null;

    /** 乾燥後総重量② */
    private BigDecimal kansougosoujyuuryou2 = null;

    /** 乾燥後総重量③ */
    private BigDecimal kansougosoujyuuryou3 = null;

    /** 乾燥後正味重量① */
    private BigDecimal kansougosyoumijyuuryou1 = null;

    /** 乾燥後正味重量② */
    private BigDecimal kansougosyoumijyuuryou2 = null;

    /** 乾燥後正味重量③ */
    private BigDecimal kansougosyoumijyuuryou3 = null;

    /** 固形分比率① */
    private BigDecimal kokeibunhiritu1 = null;

    /** 固形分比率② */
    private BigDecimal kokeibunhiritu2 = null;

    /** 固形分比率③ */
    private BigDecimal kokeibunhiritu3 = null;

    /** 固形分比率平均 */
    private BigDecimal kokeibunhirituheikin = null;

    /** 固形分測定担当者 */
    private String kokeibunsokuteitantousya = "";

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
     * 誘電体ｽﾗﾘｰ品名
     * @return the yuudentaislurryhinmei
     */
    public String getYuudentaislurryhinmei() {
        return yuudentaislurryhinmei;
    }

    /**
     * 誘電体ｽﾗﾘｰ品名
     * @param yuudentaislurryhinmei the yuudentaislurryhinmei to set
     */
    public void setYuudentaislurryhinmei(String yuudentaislurryhinmei) {
        this.yuudentaislurryhinmei = yuudentaislurryhinmei;
    }

    /**
     * 誘電体ｽﾗﾘｰLotNo
     * @return the yuudentaislurrylotno
     */
    public String getYuudentaislurrylotno() {
        return yuudentaislurrylotno;
    }

    /**
     * 誘電体ｽﾗﾘｰLotNo
     * @param yuudentaislurrylotno the yuudentaislurrylotno to set
     */
    public void setYuudentaislurrylotno(String yuudentaislurrylotno) {
        this.yuudentaislurrylotno = yuudentaislurrylotno;
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
     * 原料LotNo
     * @return the genryoulotno
     */
    public String getGenryoulotno() {
        return genryoulotno;
    }

    /**
     * 原料LotNo
     * @param genryoulotno the genryoulotno to set
     */
    public void setGenryoulotno(String genryoulotno) {
        this.genryoulotno = genryoulotno;
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
     * 添加材ｽﾗﾘｰ品名
     * @return the tenkazaislurryhinmei
     */
    public String getTenkazaislurryhinmei() {
        return tenkazaislurryhinmei;
    }

    /**
     * 添加材ｽﾗﾘｰ品名
     * @param tenkazaislurryhinmei the tenkazaislurryhinmei to set
     */
    public void setTenkazaislurryhinmei(String tenkazaislurryhinmei) {
        this.tenkazaislurryhinmei = tenkazaislurryhinmei;
    }

    /**
     * 添加材ｽﾗﾘｰLotNo
     * @return the tenkazaislurrylotno
     */
    public String getTenkazaislurrylotno() {
        return tenkazaislurrylotno;
    }

    /**
     * 添加材ｽﾗﾘｰLotNo
     * @param tenkazaislurrylotno the tenkazaislurrylotno to set
     */
    public void setTenkazaislurrylotno(String tenkazaislurrylotno) {
        this.tenkazaislurrylotno = tenkazaislurrylotno;
    }

    /**
     * 風袋重量①
     * @return the fuutaijyuuryou1
     */
    public BigDecimal getFuutaijyuuryou1() {
        return fuutaijyuuryou1;
    }

    /**
     * 風袋重量①
     * @param fuutaijyuuryou1 the fuutaijyuuryou1 to set
     */
    public void setFuutaijyuuryou1(BigDecimal fuutaijyuuryou1) {
        this.fuutaijyuuryou1 = fuutaijyuuryou1;
    }

    /**
     * 風袋重量②
     * @return the fuutaijyuuryou2
     */
    public BigDecimal getFuutaijyuuryou2() {
        return fuutaijyuuryou2;
    }

    /**
     * 風袋重量②
     * @param fuutaijyuuryou2 the fuutaijyuuryou2 to set
     */
    public void setFuutaijyuuryou2(BigDecimal fuutaijyuuryou2) {
        this.fuutaijyuuryou2 = fuutaijyuuryou2;
    }

    /**
     * 風袋重量③
     * @return the fuutaijyuuryou3
     */
    public BigDecimal getFuutaijyuuryou3() {
        return fuutaijyuuryou3;
    }

    /**
     * 風袋重量③
     * @param fuutaijyuuryou3 the fuutaijyuuryou3 to set
     */
    public void setFuutaijyuuryou3(BigDecimal fuutaijyuuryou3) {
        this.fuutaijyuuryou3 = fuutaijyuuryou3;
    }

    /**
     * 総重量①
     * @return the soujyuuryou1
     */
    public Integer getSoujyuuryou1() {
        return soujyuuryou1;
    }

    /**
     * 総重量①
     * @param soujyuuryou1 the soujyuuryou1 to set
     */
    public void setSoujyuuryou1(Integer soujyuuryou1) {
        this.soujyuuryou1 = soujyuuryou1;
    }

    /**
     * 総重量②
     * @return the soujyuuryou2
     */
    public Integer getSoujyuuryou2() {
        return soujyuuryou2;
    }

    /**
     * 総重量②
     * @param soujyuuryou2 the soujyuuryou2 to set
     */
    public void setSoujyuuryou2(Integer soujyuuryou2) {
        this.soujyuuryou2 = soujyuuryou2;
    }

    /**
     * 総重量③
     * @return the soujyuuryou3
     */
    public Integer getSoujyuuryou3() {
        return soujyuuryou3;
    }

    /**
     * 総重量③
     * @param soujyuuryou3 the soujyuuryou3 to set
     */
    public void setSoujyuuryou3(Integer soujyuuryou3) {
        this.soujyuuryou3 = soujyuuryou3;
    }

    /**
     * 添加材ｽﾗﾘｰ重量
     * @return the tenkazaislurryjyuuryou
     */
    public Integer getTenkazaislurryjyuuryou() {
        return tenkazaislurryjyuuryou;
    }

    /**
     * 添加材ｽﾗﾘｰ重量
     * @param tenkazaislurryjyuuryou the tenkazaislurryjyuuryou to set
     */
    public void setTenkazaislurryjyuuryou(Integer tenkazaislurryjyuuryou) {
        this.tenkazaislurryjyuuryou = tenkazaislurryjyuuryou;
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
     * 回転数
     * @return the kaitensuu
     */
    public String getKaitensuu() {
        return kaitensuu;
    }

    /**
     * 回転数
     * @param kaitensuu the kaitensuu to set
     */
    public void setKaitensuu(String kaitensuu) {
        this.kaitensuu = kaitensuu;
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
     * 乾燥皿種類
     * @return the kansouzarasyurui
     */
    public String getKansouzarasyurui() {
        return kansouzarasyurui;
    }

    /**
     * 乾燥皿種類
     * @param kansouzarasyurui the kansouzarasyurui to set
     */
    public void setKansouzarasyurui(String kansouzarasyurui) {
        this.kansouzarasyurui = kansouzarasyurui;
    }

    /**
     * ｱﾙﾐ皿風袋重量①
     * @return the arumizarafuutaijyuuryou1
     */
    public BigDecimal getArumizarafuutaijyuuryou1() {
        return arumizarafuutaijyuuryou1;
    }

    /**
     * ｱﾙﾐ皿風袋重量①
     * @param arumizarafuutaijyuuryou1 the arumizarafuutaijyuuryou1 to set
     */
    public void setArumizarafuutaijyuuryou1(BigDecimal arumizarafuutaijyuuryou1) {
        this.arumizarafuutaijyuuryou1 = arumizarafuutaijyuuryou1;
    }

    /**
     * 乾燥前ｽﾗﾘｰ重量①
     * @return the kansoumaeslurryjyuuryou1
     */
    public BigDecimal getKansoumaeslurryjyuuryou1() {
        return kansoumaeslurryjyuuryou1;
    }

    /**
     * 乾燥前ｽﾗﾘｰ重量①
     * @param kansoumaeslurryjyuuryou1 the kansoumaeslurryjyuuryou1 to set
     */
    public void setKansoumaeslurryjyuuryou1(BigDecimal kansoumaeslurryjyuuryou1) {
        this.kansoumaeslurryjyuuryou1 = kansoumaeslurryjyuuryou1;
    }

    /**
     * ｱﾙﾐ皿風袋重量②
     * @return the arumizarafuutaijyuuryou2
     */
    public BigDecimal getArumizarafuutaijyuuryou2() {
        return arumizarafuutaijyuuryou2;
    }

    /**
     * ｱﾙﾐ皿風袋重量②
     * @param arumizarafuutaijyuuryou2 the arumizarafuutaijyuuryou2 to set
     */
    public void setArumizarafuutaijyuuryou2(BigDecimal arumizarafuutaijyuuryou2) {
        this.arumizarafuutaijyuuryou2 = arumizarafuutaijyuuryou2;
    }

    /**
     * 乾燥前ｽﾗﾘｰ重量②
     * @return the kansoumaeslurryjyuuryou2
     */
    public BigDecimal getKansoumaeslurryjyuuryou2() {
        return kansoumaeslurryjyuuryou2;
    }

    /**
     * 乾燥前ｽﾗﾘｰ重量②
     * @param kansoumaeslurryjyuuryou2 the kansoumaeslurryjyuuryou2 to set
     */
    public void setKansoumaeslurryjyuuryou2(BigDecimal kansoumaeslurryjyuuryou2) {
        this.kansoumaeslurryjyuuryou2 = kansoumaeslurryjyuuryou2;
    }

    /**
     * ｱﾙﾐ皿風袋重量③
     * @return the arumizarafuutaijyuuryou3
     */
    public BigDecimal getArumizarafuutaijyuuryou3() {
        return arumizarafuutaijyuuryou3;
    }

    /**
     * ｱﾙﾐ皿風袋重量③
     * @param arumizarafuutaijyuuryou3 the arumizarafuutaijyuuryou3 to set
     */
    public void setArumizarafuutaijyuuryou3(BigDecimal arumizarafuutaijyuuryou3) {
        this.arumizarafuutaijyuuryou3 = arumizarafuutaijyuuryou3;
    }

    /**
     * 乾燥前ｽﾗﾘｰ重量③
     * @return the kansoumaeslurryjyuuryou3
     */
    public BigDecimal getKansoumaeslurryjyuuryou3() {
        return kansoumaeslurryjyuuryou3;
    }

    /**
     * 乾燥前ｽﾗﾘｰ重量③
     * @param kansoumaeslurryjyuuryou3 the kansoumaeslurryjyuuryou3 to set
     */
    public void setKansoumaeslurryjyuuryou3(BigDecimal kansoumaeslurryjyuuryou3) {
        this.kansoumaeslurryjyuuryou3 = kansoumaeslurryjyuuryou3;
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
     * 冷却時間
     * @return the reikyakujikan
     */
    public String getReikyakujikan() {
        return reikyakujikan;
    }

    /**
     * 冷却時間
     * @param reikyakujikan the reikyakujikan to set
     */
    public void setReikyakujikan(String reikyakujikan) {
        this.reikyakujikan = reikyakujikan;
    }

    /**
     * 乾燥後総重量①
     * @return the kansougosoujyuuryou1
     */
    public BigDecimal getKansougosoujyuuryou1() {
        return kansougosoujyuuryou1;
    }

    /**
     * 乾燥後総重量①
     * @param kansougosoujyuuryou1 the kansougosoujyuuryou1 to set
     */
    public void setKansougosoujyuuryou1(BigDecimal kansougosoujyuuryou1) {
        this.kansougosoujyuuryou1 = kansougosoujyuuryou1;
    }

    /**
     * 乾燥後総重量②
     * @return the kansougosoujyuuryou2
     */
    public BigDecimal getKansougosoujyuuryou2() {
        return kansougosoujyuuryou2;
    }

    /**
     * 乾燥後総重量②
     * @param kansougosoujyuuryou2 the kansougosoujyuuryou2 to set
     */
    public void setKansougosoujyuuryou2(BigDecimal kansougosoujyuuryou2) {
        this.kansougosoujyuuryou2 = kansougosoujyuuryou2;
    }

    /**
     * 乾燥後総重量③
     * @return the kansougosoujyuuryou3
     */
    public BigDecimal getKansougosoujyuuryou3() {
        return kansougosoujyuuryou3;
    }

    /**
     * 乾燥後総重量③
     * @param kansougosoujyuuryou3 the kansougosoujyuuryou3 to set
     */
    public void setKansougosoujyuuryou3(BigDecimal kansougosoujyuuryou3) {
        this.kansougosoujyuuryou3 = kansougosoujyuuryou3;
    }

    /**
     * 乾燥後正味重量①
     * @return the kansougosyoumijyuuryou1
     */
    public BigDecimal getKansougosyoumijyuuryou1() {
        return kansougosyoumijyuuryou1;
    }

    /**
     * 乾燥後正味重量①
     * @param kansougosyoumijyuuryou1 the kansougosyoumijyuuryou1 to set
     */
    public void setKansougosyoumijyuuryou1(BigDecimal kansougosyoumijyuuryou1) {
        this.kansougosyoumijyuuryou1 = kansougosyoumijyuuryou1;
    }

    /**
     * 乾燥後正味重量②
     * @return the kansougosyoumijyuuryou2
     */
    public BigDecimal getKansougosyoumijyuuryou2() {
        return kansougosyoumijyuuryou2;
    }

    /**
     * 乾燥後正味重量②
     * @param kansougosyoumijyuuryou2 the kansougosyoumijyuuryou2 to set
     */
    public void setKansougosyoumijyuuryou2(BigDecimal kansougosyoumijyuuryou2) {
        this.kansougosyoumijyuuryou2 = kansougosyoumijyuuryou2;
    }

    /**
     * 乾燥後正味重量③
     * @return the kansougosyoumijyuuryou3
     */
    public BigDecimal getKansougosyoumijyuuryou3() {
        return kansougosyoumijyuuryou3;
    }

    /**
     * 乾燥後正味重量③
     * @param kansougosyoumijyuuryou3 the kansougosyoumijyuuryou3 to set
     */
    public void setKansougosyoumijyuuryou3(BigDecimal kansougosyoumijyuuryou3) {
        this.kansougosyoumijyuuryou3 = kansougosyoumijyuuryou3;
    }

    /**
     * 固形分比率①
     * @return the kokeibunhiritu1
     */
    public BigDecimal getKokeibunhiritu1() {
        return kokeibunhiritu1;
    }

    /**
     * 固形分比率①
     * @param kokeibunhiritu1 the kokeibunhiritu1 to set
     */
    public void setKokeibunhiritu1(BigDecimal kokeibunhiritu1) {
        this.kokeibunhiritu1 = kokeibunhiritu1;
    }

    /**
     * 固形分比率②
     * @return the kokeibunhiritu2
     */
    public BigDecimal getKokeibunhiritu2() {
        return kokeibunhiritu2;
    }

    /**
     * 固形分比率②
     * @param kokeibunhiritu2 the kokeibunhiritu2 to set
     */
    public void setKokeibunhiritu2(BigDecimal kokeibunhiritu2) {
        this.kokeibunhiritu2 = kokeibunhiritu2;
    }

    /**
     * 固形分比率③
     * @return the kokeibunhiritu3
     */
    public BigDecimal getKokeibunhiritu3() {
        return kokeibunhiritu3;
    }

    /**
     * 固形分比率③
     * @param kokeibunhiritu3 the kokeibunhiritu3 to set
     */
    public void setKokeibunhiritu3(BigDecimal kokeibunhiritu3) {
        this.kokeibunhiritu3 = kokeibunhiritu3;
    }

    /**
     * 固形分比率平均
     * @return the kokeibunhirituheikin
     */
    public BigDecimal getKokeibunhirituheikin() {
        return kokeibunhirituheikin;
    }

    /**
     * 固形分比率平均
     * @param kokeibunhirituheikin the kokeibunhirituheikin to set
     */
    public void setKokeibunhirituheikin(BigDecimal kokeibunhirituheikin) {
        this.kokeibunhirituheikin = kokeibunhirituheikin;
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