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
 * 誘電体ｽﾗﾘｰ作製・比表面積測定履歴検索画面のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/11/23
 */
public class GXHDO202B025Model implements Serializable {
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

    /** 乾燥皿の種類 */
    private String kansouzaranosyurui = "";

    /** ﾙﾂﾎﾞNo(ﾓﾆﾀｰ) */
    private Integer rutubono_monitor = null;

    /** ﾙﾂﾎﾞNo(製品) */
    private Integer rutubono_seihin = null;

    /** ｽﾗﾘｰ重量(ﾓﾆﾀｰ) */
    private BigDecimal slurryjyuuryou_monitor = null;

    /** ｽﾗﾘｰ重量(製品) */
    private BigDecimal slurryjyuuryou_seihin = null;

    /** 乾燥温度 */
    private Integer kansouondo = null;

    /** 乾燥時間規格 */
    private Integer kansoujikankikaku = null;

    /** 乾燥開始日時 */
    private Timestamp kansoukaisinichij = null;

    /** 乾燥終了日時 */
    private Timestamp kansousyuuryounichiji = null;

    /** 脱脂炉号機 */
    private String dassirogouki = "";

    /** 脱脂温度 */
    private String dassiondo = "";

    /** 脱脂時間規格 */
    private String dassijikankikaku = "";

    /** 脱脂開始日時 */
    private Timestamp dassikaisinichiji = null;

    /** 脱脂終了日時 */
    private Timestamp dassisyuuryounichiji = null;

    /** 乾燥担当者 */
    private String kansoutantousya = "";

    /** 前処理 */
    private String maesyori = "";

    /** 前処理温度 */
    private String maesyoriondo = "";

    /** 前処理時間 */
    private String maesyorijikan = "";

    /** 前処理開始日時 */
    private Timestamp maesyorikaisinichiji = null;

    /** 前処理終了日時 */
    private Timestamp maesyorisyuuryounichiji = null;

    /** 前処理担当者 */
    private String maesyoritantousya = "";

    /** 比表面積測定開始日時 */
    private Timestamp hihyoumensekisokuteikaisinichiji = null;

    /** 比表面積測定終了日時 */
    private Timestamp hihyoumensekisokuteisyuuryounichiji = null;

    /** ﾓﾆﾀｰ実測値 */
    private BigDecimal monitorjissokuti = null;

    /** ﾓﾆﾀｰ補正値 */
    private BigDecimal monitorhoseiti = null;

    /** 比表面積規格 */
    private String hihyoumensekikikaku = "";

    /** 比表面積狙い値 */
    private String hihyoumensekineraiti = "";

    /** 比表面積参考値 */
    private String hihyoumensekisankouti = "";

    /** 比表面積測定結果① */
    private BigDecimal hihyoumensekisokuteikekka1 = null;

    /** 比表面積測定結果② */
    private BigDecimal hihyoumensekisokuteikekka2 = null;

    /** 比表面積測定結果③ */
    private BigDecimal hihyoumensekisokuteikekka3 = null;

    /** 比表面積測定結果 */
    private BigDecimal hihyoumensekisokuteikekka = null;

    /** SSA比規格 */
    private String ssahikikaku = "";

    /** SSA比参考値 */
    private String ssahisankouti = "";

    /** SSA比 */
    private BigDecimal ssahi = null;

    /** 比表面積測定担当者 */
    private String hihyoumensekisokuteitantousya = "";

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
     * 乾燥皿の種類
     * @return the kansouzaranosyurui
     */
    public String getKansouzaranosyurui() {
        return kansouzaranosyurui;
    }

    /**
     * 乾燥皿の種類
     * @param kansouzaranosyurui the kansouzaranosyurui to set
     */
    public void setKansouzaranosyurui(String kansouzaranosyurui) {
        this.kansouzaranosyurui = kansouzaranosyurui;
    }

    /**
     * ﾙﾂﾎﾞNo(ﾓﾆﾀｰ)
     * @return the rutubono_monitor
     */
    public Integer getRutubono_monitor() {
        return rutubono_monitor;
    }

    /**
     * ﾙﾂﾎﾞNo(ﾓﾆﾀｰ)
     * @param rutubono_monitor the rutubono_monitor to set
     */
    public void setRutubono_monitor(Integer rutubono_monitor) {
        this.rutubono_monitor = rutubono_monitor;
    }

    /**
     * ﾙﾂﾎﾞNo(製品)
     * @return the rutubono_seihin
     */
    public Integer getRutubono_seihin() {
        return rutubono_seihin;
    }

    /**
     * ﾙﾂﾎﾞNo(製品)
     * @param rutubono_seihin the rutubono_seihin to set
     */
    public void setRutubono_seihin(Integer rutubono_seihin) {
        this.rutubono_seihin = rutubono_seihin;
    }

    /**
     * ｽﾗﾘｰ重量(ﾓﾆﾀｰ)
     * @return the slurryjyuuryou_monitor
     */
    public BigDecimal getSlurryjyuuryou_monitor() {
        return slurryjyuuryou_monitor;
    }

    /**
     * ｽﾗﾘｰ重量(ﾓﾆﾀｰ)
     * @param slurryjyuuryou_monitor the slurryjyuuryou_monitor to set
     */
    public void setSlurryjyuuryou_monitor(BigDecimal slurryjyuuryou_monitor) {
        this.slurryjyuuryou_monitor = slurryjyuuryou_monitor;
    }

    /**
     * ｽﾗﾘｰ重量(製品)
     * @return the slurryjyuuryou_seihin
     */
    public BigDecimal getSlurryjyuuryou_seihin() {
        return slurryjyuuryou_seihin;
    }

    /**
     * ｽﾗﾘｰ重量(製品)
     * @param slurryjyuuryou_seihin the slurryjyuuryou_seihin to set
     */
    public void setSlurryjyuuryou_seihin(BigDecimal slurryjyuuryou_seihin) {
        this.slurryjyuuryou_seihin = slurryjyuuryou_seihin;
    }

    /**
     * 乾燥温度
     * @return the kansouondo
     */
    public Integer getKansouondo() {
        return kansouondo;
    }

    /**
     * 乾燥温度
     * @param kansouondo the kansouondo to set
     */
    public void setKansouondo(Integer kansouondo) {
        this.kansouondo = kansouondo;
    }

    /**
     * 乾燥時間規格
     * @return the kansoujikankikaku
     */
    public Integer getKansoujikankikaku() {
        return kansoujikankikaku;
    }

    /**
     * 乾燥時間規格
     * @param kansoujikankikaku the kansoujikankikaku to set
     */
    public void setKansoujikankikaku(Integer kansoujikankikaku) {
        this.kansoujikankikaku = kansoujikankikaku;
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
     * 脱脂炉号機
     * @return the dassirogouki
     */
    public String getDassirogouki() {
        return dassirogouki;
    }

    /**
     * 脱脂炉号機
     * @param dassirogouki the dassirogouki to set
     */
    public void setDassirogouki(String dassirogouki) {
        this.dassirogouki = dassirogouki;
    }

    /**
     * 脱脂温度
     * @return the dassiondo
     */
    public String getDassiondo() {
        return dassiondo;
    }

    /**
     * 脱脂温度
     * @param dassiondo the dassiondo to set
     */
    public void setDassiondo(String dassiondo) {
        this.dassiondo = dassiondo;
    }

    /**
     * 脱脂時間規格
     * @return the dassijikankikaku
     */
    public String getDassijikankikaku() {
        return dassijikankikaku;
    }

    /**
     * 脱脂時間規格
     * @param dassijikankikaku the dassijikankikaku to set
     */
    public void setDassijikankikaku(String dassijikankikaku) {
        this.dassijikankikaku = dassijikankikaku;
    }

    /**
     * 脱脂開始日時
     * @return the dassikaisinichiji
     */
    public Timestamp getDassikaisinichiji() {
        return dassikaisinichiji;
    }

    /**
     * 脱脂開始日時
     * @param dassikaisinichiji the dassikaisinichiji to set
     */
    public void setDassikaisinichiji(Timestamp dassikaisinichiji) {
        this.dassikaisinichiji = dassikaisinichiji;
    }

    /**
     * 脱脂終了日時
     * @return the dassisyuuryounichiji
     */
    public Timestamp getDassisyuuryounichiji() {
        return dassisyuuryounichiji;
    }

    /**
     * 脱脂終了日時
     * @param dassisyuuryounichiji the dassisyuuryounichiji to set
     */
    public void setDassisyuuryounichiji(Timestamp dassisyuuryounichiji) {
        this.dassisyuuryounichiji = dassisyuuryounichiji;
    }

    /**
     * 乾燥担当者
     * @return the kansoutantousya
     */
    public String getKansoutantousya() {
        return kansoutantousya;
    }

    /**
     * 乾燥担当者
     * @param kansoutantousya the kansoutantousya to set
     */
    public void setKansoutantousya(String kansoutantousya) {
        this.kansoutantousya = kansoutantousya;
    }

    /**
     * 前処理
     * @return the maesyori
     */
    public String getMaesyori() {
        return maesyori;
    }

    /**
     * 前処理
     * @param maesyori the maesyori to set
     */
    public void setMaesyori(String maesyori) {
        this.maesyori = maesyori;
    }

    /**
     * 前処理温度
     * @return the maesyoriondo
     */
    public String getMaesyoriondo() {
        return maesyoriondo;
    }

    /**
     * 前処理温度
     * @param maesyoriondo the maesyoriondo to set
     */
    public void setMaesyoriondo(String maesyoriondo) {
        this.maesyoriondo = maesyoriondo;
    }

    /**
     * 前処理時間
     * @return the maesyorijikan
     */
    public String getMaesyorijikan() {
        return maesyorijikan;
    }

    /**
     * 前処理時間
     * @param maesyorijikan the maesyorijikan to set
     */
    public void setMaesyorijikan(String maesyorijikan) {
        this.maesyorijikan = maesyorijikan;
    }

    /**
     * 前処理開始日時
     * @return the maesyorikaisinichiji
     */
    public Timestamp getMaesyorikaisinichiji() {
        return maesyorikaisinichiji;
    }

    /**
     * 前処理開始日時
     * @param maesyorikaisinichiji the maesyorikaisinichiji to set
     */
    public void setMaesyorikaisinichiji(Timestamp maesyorikaisinichiji) {
        this.maesyorikaisinichiji = maesyorikaisinichiji;
    }

    /**
     * 前処理終了日時
     * @return the maesyorisyuuryounichiji
     */
    public Timestamp getMaesyorisyuuryounichiji() {
        return maesyorisyuuryounichiji;
    }

    /**
     * 前処理終了日時
     * @param maesyorisyuuryounichiji the maesyorisyuuryounichiji to set
     */
    public void setMaesyorisyuuryounichiji(Timestamp maesyorisyuuryounichiji) {
        this.maesyorisyuuryounichiji = maesyorisyuuryounichiji;
    }

    /**
     * 前処理担当者
     * @return the maesyoritantousya
     */
    public String getMaesyoritantousya() {
        return maesyoritantousya;
    }

    /**
     * 前処理担当者
     * @param maesyoritantousya the maesyoritantousya to set
     */
    public void setMaesyoritantousya(String maesyoritantousya) {
        this.maesyoritantousya = maesyoritantousya;
    }

    /**
     * 比表面積測定開始日時
     * @return the hihyoumensekisokuteikaisinichiji
     */
    public Timestamp getHihyoumensekisokuteikaisinichiji() {
        return hihyoumensekisokuteikaisinichiji;
    }

    /**
     * 比表面積測定開始日時
     * @param hihyoumensekisokuteikaisinichiji the hihyoumensekisokuteikaisinichiji to set
     */
    public void setHihyoumensekisokuteikaisinichiji(Timestamp hihyoumensekisokuteikaisinichiji) {
        this.hihyoumensekisokuteikaisinichiji = hihyoumensekisokuteikaisinichiji;
    }

    /**
     * 比表面積測定終了日時
     * @return the hihyoumensekisokuteisyuuryounichiji
     */
    public Timestamp getHihyoumensekisokuteisyuuryounichiji() {
        return hihyoumensekisokuteisyuuryounichiji;
    }

    /**
     * 比表面積測定終了日時
     * @param hihyoumensekisokuteisyuuryounichiji the hihyoumensekisokuteisyuuryounichiji to set
     */
    public void setHihyoumensekisokuteisyuuryounichiji(Timestamp hihyoumensekisokuteisyuuryounichiji) {
        this.hihyoumensekisokuteisyuuryounichiji = hihyoumensekisokuteisyuuryounichiji;
    }

    /**
     * ﾓﾆﾀｰ実測値
     * @return the monitorjissokuti
     */
    public BigDecimal getMonitorjissokuti() {
        return monitorjissokuti;
    }

    /**
     * ﾓﾆﾀｰ実測値
     * @param monitorjissokuti the monitorjissokuti to set
     */
    public void setMonitorjissokuti(BigDecimal monitorjissokuti) {
        this.monitorjissokuti = monitorjissokuti;
    }

    /**
     * ﾓﾆﾀｰ補正値
     * @return the monitorhoseiti
     */
    public BigDecimal getMonitorhoseiti() {
        return monitorhoseiti;
    }

    /**
     * ﾓﾆﾀｰ補正値
     * @param monitorhoseiti the monitorhoseiti to set
     */
    public void setMonitorhoseiti(BigDecimal monitorhoseiti) {
        this.monitorhoseiti = monitorhoseiti;
    }

    /**
     * 比表面積規格
     * @return the hihyoumensekikikaku
     */
    public String getHihyoumensekikikaku() {
        return hihyoumensekikikaku;
    }

    /**
     * 比表面積規格
     * @param hihyoumensekikikaku the hihyoumensekikikaku to set
     */
    public void setHihyoumensekikikaku(String hihyoumensekikikaku) {
        this.hihyoumensekikikaku = hihyoumensekikikaku;
    }

    /**
     * 比表面積狙い値
     * @return the hihyoumensekineraiti
     */
    public String getHihyoumensekineraiti() {
        return hihyoumensekineraiti;
    }

    /**
     * 比表面積狙い値
     * @param hihyoumensekineraiti the hihyoumensekineraiti to set
     */
    public void setHihyoumensekineraiti(String hihyoumensekineraiti) {
        this.hihyoumensekineraiti = hihyoumensekineraiti;
    }

    /**
     * 比表面積参考値
     * @return the hihyoumensekisankouti
     */
    public String getHihyoumensekisankouti() {
        return hihyoumensekisankouti;
    }

    /**
     * 比表面積参考値
     * @param hihyoumensekisankouti the hihyoumensekisankouti to set
     */
    public void setHihyoumensekisankouti(String hihyoumensekisankouti) {
        this.hihyoumensekisankouti = hihyoumensekisankouti;
    }

    /**
     * 比表面積測定結果①
     * @return the hihyoumensekisokuteikekka1
     */
    public BigDecimal getHihyoumensekisokuteikekka1() {
        return hihyoumensekisokuteikekka1;
    }

    /**
     * 比表面積測定結果①
     * @param hihyoumensekisokuteikekka1 the hihyoumensekisokuteikekka1 to set
     */
    public void setHihyoumensekisokuteikekka1(BigDecimal hihyoumensekisokuteikekka1) {
        this.hihyoumensekisokuteikekka1 = hihyoumensekisokuteikekka1;
    }

    /**
     * 比表面積測定結果②
     * @return the hihyoumensekisokuteikekka2
     */
    public BigDecimal getHihyoumensekisokuteikekka2() {
        return hihyoumensekisokuteikekka2;
    }

    /**
     * 比表面積測定結果②
     * @param hihyoumensekisokuteikekka2 the hihyoumensekisokuteikekka2 to set
     */
    public void setHihyoumensekisokuteikekka2(BigDecimal hihyoumensekisokuteikekka2) {
        this.hihyoumensekisokuteikekka2 = hihyoumensekisokuteikekka2;
    }

    /**
     * 比表面積測定結果③
     * @return the hihyoumensekisokuteikekka3
     */
    public BigDecimal getHihyoumensekisokuteikekka3() {
        return hihyoumensekisokuteikekka3;
    }

    /**
     * 比表面積測定結果③
     * @param hihyoumensekisokuteikekka3 the hihyoumensekisokuteikekka3 to set
     */
    public void setHihyoumensekisokuteikekka3(BigDecimal hihyoumensekisokuteikekka3) {
        this.hihyoumensekisokuteikekka3 = hihyoumensekisokuteikekka3;
    }

    /**
     * 比表面積測定結果
     * @return the hihyoumensekisokuteikekka
     */
    public BigDecimal getHihyoumensekisokuteikekka() {
        return hihyoumensekisokuteikekka;
    }

    /**
     * 比表面積測定結果
     * @param hihyoumensekisokuteikekka the hihyoumensekisokuteikekka to set
     */
    public void setHihyoumensekisokuteikekka(BigDecimal hihyoumensekisokuteikekka) {
        this.hihyoumensekisokuteikekka = hihyoumensekisokuteikekka;
    }

    /**
     * SSA比規格
     * @return the ssahikikaku
     */
    public String getSsahikikaku() {
        return ssahikikaku;
    }

    /**
     * SSA比規格
     * @param ssahikikaku the ssahikikaku to set
     */
    public void setSsahikikaku(String ssahikikaku) {
        this.ssahikikaku = ssahikikaku;
    }

    /**
     * SSA比参考値
     * @return the ssahisankouti
     */
    public String getSsahisankouti() {
        return ssahisankouti;
    }

    /**
     * SSA比参考値
     * @param ssahisankouti the ssahisankouti to set
     */
    public void setSsahisankouti(String ssahisankouti) {
        this.ssahisankouti = ssahisankouti;
    }

    /**
     * SSA比
     * @return the ssahi
     */
    public BigDecimal getSsahi() {
        return ssahi;
    }

    /**
     * SSA比
     * @param ssahi the ssahi to set
     */
    public void setSsahi(BigDecimal ssahi) {
        this.ssahi = ssahi;
    }

    /**
     * 比表面積測定担当者
     * @return the hihyoumensekisokuteitantousya
     */
    public String getHihyoumensekisokuteitantousya() {
        return hihyoumensekisokuteitantousya;
    }

    /**
     * 比表面積測定担当者
     * @param hihyoumensekisokuteitantousya the hihyoumensekisokuteitantousya to set
     */
    public void setHihyoumensekisokuteitantousya(String hihyoumensekisokuteitantousya) {
        this.hihyoumensekisokuteitantousya = hihyoumensekisokuteitantousya;
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