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
 * 添加材ｽﾗﾘｰ作製・BET履歴検索画面のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/10/15
 */
public class GXHDO202B013Model implements Serializable {
    /** WIPﾛｯﾄNo */
    private String lotno = "";

    /** 添加材ｽﾗﾘｰ品名 */
    private String tenkazaislurryhinmei = "";

    /** 添加材ｽﾗﾘｰLotNo */
    private String tenkazaislurrylotno = "";

    /** ﾛｯﾄ区分 */
    private String lotkubun = "";

    /** 乾燥皿の種類 */
    private String kansouzaranosyurui = "";

    /** ｽﾗﾘｰ重量 */
    private BigDecimal slurryjyuuryou = null;

    /** 乾燥温度 */
    private String kansouondo = "";

    /** 乾燥時間規格 */
    private String kansoujikankikaku = "";

    /** 乾燥開始日時 */
    private Timestamp kansoukaisinichiji = null;

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

    /** 測定ｻﾝﾌﾟﾙ数 */
    private String sokuteisample = "";

    /** ｻﾝﾌﾟﾙ重量 */
    private String samplejyuuryou = "";

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

    /** 比表面積測定結果 */
    private BigDecimal hihyoumensekisokuteikekka = null;

    /** 判定 */
    private String hantei = "";

    /** 比表面積測定担当者 */
    private String hihyoumensekisokuteitantousya = "";

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
     * ｽﾗﾘｰ重量
     * @return the slurryjyuuryou
     */
    public BigDecimal getSlurryjyuuryou() {
        return slurryjyuuryou;
    }

    /**
     * ｽﾗﾘｰ重量
     * @param slurryjyuuryou the slurryjyuuryou to set
     */
    public void setSlurryjyuuryou(BigDecimal slurryjyuuryou) {
        this.slurryjyuuryou = slurryjyuuryou;
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
     * 乾燥時間規格
     * @return the kansoujikankikaku
     */
    public String getKansoujikankikaku() {
        return kansoujikankikaku;
    }

    /**
     * 乾燥時間規格
     * @param kansoujikankikaku the kansoujikankikaku to set
     */
    public void setKansoujikankikaku(String kansoujikankikaku) {
        this.kansoujikankikaku = kansoujikankikaku;
    }

    /**
     * 乾燥開始日時
     * @return the kansoukaisinichiji
     */
    public Timestamp getKansoukaisinichiji() {
        return kansoukaisinichiji;
    }

    /**
     * 乾燥開始日時
     * @param kansoukaisinichiji the kansoukaisinichiji to set
     */
    public void setKansoukaisinichiji(Timestamp kansoukaisinichiji) {
        this.kansoukaisinichiji = kansoukaisinichiji;
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
     * 測定ｻﾝﾌﾟﾙ数
     * @return the sokuteisample
     */
    public String getSokuteisample() {
        return sokuteisample;
    }

    /**
     * 測定ｻﾝﾌﾟﾙ数
     * @param sokuteisample the sokuteisample to set
     */
    public void setSokuteisample(String sokuteisample) {
        this.sokuteisample = sokuteisample;
    }

    /**
     * ｻﾝﾌﾟﾙ重量
     * @return the samplejyuuryou
     */
    public String getSamplejyuuryou() {
        return samplejyuuryou;
    }

    /**
     * ｻﾝﾌﾟﾙ重量
     * @param samplejyuuryou the samplejyuuryou to set
     */
    public void setSamplejyuuryou(String samplejyuuryou) {
        this.samplejyuuryou = samplejyuuryou;
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
     * 判定
     * @return the hantei
     */
    public String getHantei() {
        return hantei;
    }

    /**
     * 判定
     * @param hantei the hantei to set
     */
    public void setHantei(String hantei) {
        this.hantei = hantei;
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

}