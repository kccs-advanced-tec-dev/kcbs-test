/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
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
 * 変更日	2019/01/27<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCCS D.Yanagida<br>
 * 変更理由	新規作成<br>
 * <br>
 * 変更日       2022/03/02<br>
 * 計画書No     MB2202-D013<br>
 * 変更者       KCSS K.Jo<br>
 * 変更理由     項目追加・変更<br>
 * <br>
 * 変更日	2022/06/10<br>
 * 計画書No	MB2205-D010<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	画面表示項目を追加<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 印刷・SPSスクリーン履歴検索画面のモデルクラスです。
 *
 * @author KCCS D.Yanagida
 * @since 2019/01/27
 */
public class GXHDO201B002Model implements Serializable {
    /** ﾛｯﾄNo. */
    private String lotno = "";
    /** KCPNO */
    private String kcpno = "";
    /** ﾃｰﾌﾟ種類 */
    private String tapesyurui = "";
    /** ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo */
    private String tapelotno = "";
    /** ﾃｰﾌﾟｽﾘｯﾌﾟ記号 */
    private String tapeslipkigo = "";
    /** 原料記号 */
    private String genryoukigou = "";
    /** ﾍﾟｰｽﾄﾛｯﾄNo1 */
    private String pastelotno = "";
    /** ﾍﾟｰｽﾄ粘度1 */
    private BigDecimal pastenendo = null;
    /** ﾍﾟｰｽﾄ温度1 */
    private BigDecimal pasteondo = null;
    /** 製版No */
    private String seihanno = "";
    /** 製版枚数 */
    private Long seihanmaisuu = null;
    /** 最大処理数 */
    private Long saidaisyorisuu = null;
    /** 累計処理数 */
    private Long ruikeisyorisuu = null;
    /** ﾌﾟﾘﾝﾄ開始日時 */
    private Timestamp startdatetime = null;
    /** ﾌﾟﾘﾝﾄ終了日時 */
    private Timestamp enddatetime = null;
    /** ｽｷｰｼﾞNo */
    private String skeegeno = "";
    /** ｽｷｰｼﾞ枚数 */
    private Long skeegemaisuu = null;
    /** 号機ｺｰﾄﾞ */
    private String gouki = "";
    /** 担当者ｺｰﾄﾞ */
    private String tantousya = "";
    /** 確認者ｺｰﾄﾞ */
    private String kakuninsya = "";
    /** 乾燥温度 */
    private BigDecimal kansouondo = null;
    /** 印刷ﾌﾟﾛﾌｧｲﾙ */
    private String prnprofile = "";
    /** 乾燥時間 */
    private BigDecimal kansoutime = null;
    /** 差圧 */
    private BigDecimal saatu = null;
    /** ｽｷｰｼﾞｽﾋﾟｰﾄﾞ */
    private BigDecimal skeegespeed = null;
    /** ｽｷｰｼﾞ角度 */
    private Long skeegeangle = null;
    /** ﾒﾀﾙﾚｲﾀﾞｳﾝ値 */
    private Long mld = null;
    /** ｸﾘｱﾗﾝｽ設定値 */
    private BigDecimal clearrance = null;
    /** 先行ﾛｯﾄNo */
    private String senkoulotno = "";
    /** ﾃｰﾌﾟ使い切り */
    private String tapetsukaikiri = "";
    /** 次ﾛｯﾄへ */
    private String jilothe = "";
    /** 成形長さ */
    private Long seikeinagasa = null;
    /** 備考1 */
    private String bikou1 = "";
    /** 備考2 */
    private String bikou2 = "";
    /** 備考3 */
    private String bikou3 = "";
    /** 備考4 */
    private String bikou4 = "";
    /** 備考5 */
    private String bikou5 = "";
    /** 膜厚1 */
    private BigDecimal makuatu1 = null;
    /** 膜厚2 */
    private BigDecimal makuatu2 = null;
    /** 膜厚3 */
    private BigDecimal makuatu3 = null;
    /** 膜厚4 */
    private BigDecimal makuatu4 = null;
    /** 膜厚5 */
    private BigDecimal makuatu5 = null;
    /** ﾍﾟｰｽﾄﾛｯﾄNo2 */
    private String pastelotno2 = "";
    /** ﾍﾟｰｽﾄﾛｯﾄNo3 */
    private String pastelotno3 = "";
    /** ﾍﾟｰｽﾄﾛｯﾄNo4 */
    private String pastelotno4 = "";
    /** ﾍﾟｰｽﾄﾛｯﾄNo5 */
    private String pastelotno5 = "";
    /** ﾍﾟｰｽﾄ粘度2 */
    private BigDecimal pastenendo2 = null;
    /** ﾍﾟｰｽﾄ粘度3 */
    private BigDecimal pastenendo3 = null;
    /** ﾍﾟｰｽﾄ粘度4 */
    private BigDecimal pastenendo4 = null;
    /** ﾍﾟｰｽﾄ粘度5 */
    private BigDecimal pastenendo5 = null;
    /** ﾍﾟｰｽﾄ温度2 */
    private BigDecimal pasteondo2 = null;
    /** ﾍﾟｰｽﾄ温度3 */
    private BigDecimal pasteondo3 = null;
    /** ﾍﾟｰｽﾄ温度4 */
    private BigDecimal pasteondo4 = null;
    /** ﾍﾟｰｽﾄ温度5 */
    private BigDecimal pasteondo5 = null;
    /** 乾燥温度2 */
    private BigDecimal kansouondo2 = null;
    /** 乾燥温度3 */
    private BigDecimal kansouondo3 = null;
    /** 乾燥温度4 */
    private BigDecimal kansouondo4 = null;
    /** 乾燥温度5 */
    private BigDecimal kansouondo5 = null;
    /** ｽｷｰｼﾞ枚数2 */
    private Long skeegemaisuu2 = null;
    /** ﾃｰﾌﾟﾛｰﾙNo1 */
    private String taperollno1 = "";
    /** ﾃｰﾌﾟﾛｰﾙNo2 */
    private String taperollno2 = "";
    /** ﾃｰﾌﾟﾛｰﾙNo3 */
    private String taperollno3 = "";
    /** ﾃｰﾌﾟﾛｰﾙNo4 */
    private String taperollno4 = "";
    /** ﾃｰﾌﾟﾛｰﾙNo5 */
    private String taperollno5 = "";
    /** ﾍﾟｰｽﾄ品名 */
    private String pastehinmei = "";
    /** 製版名 */
    private String seihanmei = "";
    /** ｽﾀｰﾄ時膜厚AVE */
    private BigDecimal makuatuaveStart = null;
    /** ｽﾀｰﾄ時膜厚MAX */
    private BigDecimal makuatumaxStart = null;
    /** ｽﾀｰﾄ時膜厚MIN */
    private BigDecimal makuatuminStart = null;
    /** ｽﾀｰﾄ時PTN間距離X */
    private Long startPtnDistX = null;
    /** ｽﾀｰﾄ時PTN間距離Y */
    private Long startPtnDistY = null;
    /** ｾｯﾃｨﾝｸﾞ担当者ｺｰﾄﾞ */
    private String tantoSetting = "";
    /** 終了時膜厚AVE */
    private BigDecimal makuatuaveEnd = null;
    /** 終了時膜厚MAX */
    private BigDecimal makuatumaxEnd = null;
    /** 終了時膜厚MIN */
    private BigDecimal makuatuminEnd = null;
    /** 終了時PTN間距離X */
    private Long endPtnDistX = null;
    /** 終了時PTN間距離Y */
    private Long endPtnDistY = null;
    /** 終了時担当者ｺｰﾄﾞ */
    private String tantoEnd = "";
    /** 指示乾燥温度1 */
    private BigDecimal sijiondo = null;
    /** 指示乾燥温度2 */
    private BigDecimal sijiondo2 = null;
    /** 指示乾燥温度3 */
    private BigDecimal sijiondo3 = null;
    /** 指示乾燥温度4 */
    private BigDecimal sijiondo4 = null;
    /** 指示乾燥温度5 */
    private BigDecimal sijiondo5 = null;
    /** ﾍﾟｰｽﾄ固形分1 */
    private BigDecimal pkokeibun1 = null;
    /** ﾍﾟｰｽﾄ固形分2 */
    private BigDecimal pkokeibun2 = null;
    /** PETﾌｨﾙﾑ種類 */
    private String petfilmsyurui = "";
    /** 印刷ｽﾀｰﾄ膜厚CV */
    private BigDecimal makuatucvStart = null;
    /** ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認 */
    private String nijimikasureStart = "";
    /** 印刷ｴﾝﾄﾞ膜厚CV */
    private BigDecimal makuatucvEnd = null;
    /** 終了時ﾆｼﾞﾐ・ｶｽﾚ確認 */
    private String nijimikasureEnd = "";
    /** 印刷枚数 */
    private Long printmaisuu = null;
    /** ﾃｰﾌﾞﾙｸﾘｱﾗﾝｽ */
    private BigDecimal tableClearrance = null;
    /** ｽｸﾚｯﾊﾟｰ速度 */
    private Long scraperspeed = null;
    /** ｽｷｰｼﾞ外観 */
    private String skeegegaikan = "";
    /** 膜厚ｽﾀｰﾄ1 */
    private BigDecimal makuatsuStart1 = null;
    /** 膜厚ｽﾀｰﾄ2 */
    private BigDecimal makuatsuStart2 = null;
    /** 膜厚ｽﾀｰﾄ3 */
    private BigDecimal makuatsuStart3 = null;
    /** 膜厚ｽﾀｰﾄ4 */
    private BigDecimal makuatsuStart4 = null;
    /** 膜厚ｽﾀｰﾄ5 */
    private BigDecimal makuatsuStart5 = null;
    /** 膜厚ｽﾀｰﾄ6 */
    private BigDecimal makuatsuStart6 = null;
    /** 膜厚ｽﾀｰﾄ7 */
    private BigDecimal makuatsuStart7 = null;
    /** 膜厚ｽﾀｰﾄ8 */
    private BigDecimal makuatsuStart8 = null;
    /** 膜厚ｽﾀｰﾄ9 */
    private BigDecimal makuatsuStart9 = null;
    /** PTN距離X ｽﾀｰﾄ1 */
    private Long startPtnDistX1 = null;
    /** PTN距離X ｽﾀｰﾄ2 */
    private Long startPtnDistX2 = null;
    /** PTN距離X ｽﾀｰﾄ3 */
    private Long startPtnDistX3 = null;
    /** PTN距離X ｽﾀｰﾄ4 */
    private Long startPtnDistX4 = null;
    /** PTN距離X ｽﾀｰﾄ5 */
    private Long startPtnDistX5 = null;
    /** PTN距離Y ｽﾀｰﾄ1 */
    private Long startPtnDistY1 = null;
    /** PTN距離Y ｽﾀｰﾄ2 */
    private Long startPtnDistY2 = null;
    /** PTN距離Y ｽﾀｰﾄ3 */
    private Long startPtnDistY3 = null;
    /** PTN距離Y ｽﾀｰﾄ4 */
    private Long startPtnDistY4 = null;
    /** PTN距離Y ｽﾀｰﾄ5 */
    private Long startPtnDistY5 = null;
    /** 膜厚ｴﾝﾄﾞ1 */
    private BigDecimal makuatsuEnd1 = null;
    /** 膜厚ｴﾝﾄﾞ2 */
    private BigDecimal makuatsuEnd2 = null;
    /** 膜厚ｴﾝﾄﾞ3 */
    private BigDecimal makuatsuEnd3 = null;
    /** 膜厚ｴﾝﾄﾞ4 */
    private BigDecimal makuatsuEnd4 = null;
    /** 膜厚ｴﾝﾄﾞ5 */
    private BigDecimal makuatsuEnd5 = null;
    /** 膜厚ｴﾝﾄﾞ6 */
    private BigDecimal makuatsuEnd6 = null;
    /** 膜厚ｴﾝﾄﾞ7 */
    private BigDecimal makuatsuEnd7 = null;
    /** 膜厚ｴﾝﾄﾞ8 */
    private BigDecimal makuatsuEnd8 = null;
    /** 膜厚ｴﾝﾄﾞ9 */
    private BigDecimal makuatsuEnd9 = null;
    /** PTN距離X ｴﾝﾄﾞ1 */
    private Long endPtnDistX1 = null;
    /** PTN距離X ｴﾝﾄﾞ2 */
    private Long endPtnDistX2 = null;
    /** PTN距離X ｴﾝﾄﾞ3 */
    private Long endPtnDistX3 = null;
    /** PTN距離X ｴﾝﾄﾞ4 */
    private Long endPtnDistX4 = null;
    /** PTN距離X ｴﾝﾄﾞ5 */
    private Long endPtnDistX5 = null;
    /** PTN距離Y ｴﾝﾄﾞ1 */
    private Long endPtnDistY1 = null;
    /** PTN距離Y ｴﾝﾄﾞ2 */
    private Long endPtnDistY2 = null;
    /** PTN距離Y ｴﾝﾄﾞ3 */
    private Long endPtnDistY3 = null;
    /** PTN距離Y ｴﾝﾄﾞ4 */
    private Long endPtnDistY4 = null;
    /** PTN距離Y ｴﾝﾄﾞ5 */
    private Long endPtnDistY5 = null;
    
    /**
     * コンストラクタ
     */
    public void GXHDO201B002Model() {
    }

    /**
     * ﾛｯﾄNo.
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * ﾛｯﾄNo.
     * @param lotno the lotno to set
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * KCPNO
     * @return the kcpno
     */
    public String getKcpno() {
        return kcpno;
    }

    /**
     * KCPNO
     * @param kcpno the kcpno to set
     */
    public void setKcpno(String kcpno) {
        this.kcpno = kcpno;
    }

    /**
     * ﾃｰﾌﾟ種類
     * @return the tapesyurui
     */
    public String getTapesyurui() {
        return tapesyurui;
    }

    /**
     * ﾃｰﾌﾟ種類
     * @param tapesyurui the tapesyurui to set
     */
    public void setTapesyurui(String tapesyurui) {
        this.tapesyurui = tapesyurui;
    }

    /**
     * ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
     * @return the tapelotno
     */
    public String getTapelotno() {
        return tapelotno;
    }

    /**
     * ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
     * @param tapelotno the tapelotno to set
     */
    public void setTapelotno(String tapelotno) {
        this.tapelotno = tapelotno;
    }

    /**
     * ﾃｰﾌﾟｽﾘｯﾌﾟ記号
     * @return the tapeslipkigo
     */
    public String getTapeslipkigo() {
        return tapeslipkigo;
    }

    /**
     * ﾃｰﾌﾟｽﾘｯﾌﾟ記号
     * @param tapeslipkigo the tapeslipkigo to set
     */
    public void setTapeslipkigo(String tapeslipkigo) {
        this.tapeslipkigo = tapeslipkigo;
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
     * ﾍﾟｰｽﾄﾛｯﾄNo1
     * @return the pastelotno
     */
    public String getPastelotno() {
        return pastelotno;
    }

    /**
     * ﾍﾟｰｽﾄﾛｯﾄNo1
     * @param pastelotno the pastelotno to set
     */
    public void setPastelotno(String pastelotno) {
        this.pastelotno = pastelotno;
    }

    /**
     * ﾍﾟｰｽﾄ粘度1
     * @return the pastenendo
     */
    public BigDecimal getPastenendo() {
        return pastenendo;
    }

    /**
     * ﾍﾟｰｽﾄ粘度1
     * @param pastenendo the pastenendo to set
     */
    public void setPastenendo(BigDecimal pastenendo) {
        this.pastenendo = pastenendo;
    }

    /**
     * ﾍﾟｰｽﾄ温度1
     * @return the pasteondo
     */
    public BigDecimal getPasteondo() {
        return pasteondo;
    }

    /**
     * ﾍﾟｰｽﾄ温度1
     * @param pasteondo the pasteondo to set
     */
    public void setPasteondo(BigDecimal pasteondo) {
        this.pasteondo = pasteondo;
    }

    /**
     * 製版No
     * @return the seihanno
     */
    public String getSeihanno() {
        return seihanno;
    }

    /**
     * 製版No
     * @param seihanno the seihanno to set
     */
    public void setSeihanno(String seihanno) {
        this.seihanno = seihanno;
    }

    /**
     * 製版枚数
     * @return the seihanmaisuu
     */
    public Long getSeihanmaisuu() {
        return seihanmaisuu;
    }

    /**
     * 製版枚数
     * @param seihanmaisuu the seihanmaisuu to set
     */
    public void setSeihanmaisuu(Long seihanmaisuu) {
        this.seihanmaisuu = seihanmaisuu;
    }

    /**
     * 最大処理数
     * @return the saidaisyorisuu
     */
    public Long getSaidaisyorisuu() {
        return saidaisyorisuu;
    }

    /**
     * 最大処理数
     * @param saidaisyorisuu the saidaisyorisuu to set
     */
    public void setSaidaisyorisuu(Long saidaisyorisuu) {
        this.saidaisyorisuu = saidaisyorisuu;
    }

    /**
     * 累計処理数
     * @return the ruikeisyorisuu
     */
    public Long getRuikeisyorisuu() {
        return ruikeisyorisuu;
    }

    /**
     * 累計処理数
     * @param ruikeisyorisuu the ruikeisyorisuu to set
     */
    public void setRuikeisyorisuu(Long ruikeisyorisuu) {
        this.ruikeisyorisuu = ruikeisyorisuu;
    }

    /**
     * ﾌﾟﾘﾝﾄ開始日時
     * @return the startdatetime
     */
    public Timestamp getStartdatetime() {
        return startdatetime;
    }

    /**
     * ﾌﾟﾘﾝﾄ開始日時
     * @param startdatetime the startdatetime to set
     */
    public void setStartdatetime(Timestamp startdatetime) {
        this.startdatetime = startdatetime;
    }

    /**
     * ﾌﾟﾘﾝﾄ終了日時
     * @return the enddatetime
     */
    public Timestamp getEnddatetime() {
        return enddatetime;
    }

    /**
     * ﾌﾟﾘﾝﾄ終了日時
     * @param enddatetime the enddatetime to set
     */
    public void setEnddatetime(Timestamp enddatetime) {
        this.enddatetime = enddatetime;
    }

    /**
     * ｽｷｰｼﾞNo
     * @return the skeegeno
     */
    public String getSkeegeno() {
        return skeegeno;
    }

    /**
     * ｽｷｰｼﾞNo
     * @param skeegeno the skeegeno to set
     */
    public void setSkeegeno(String skeegeno) {
        this.skeegeno = skeegeno;
    }

    /**
     * ｽｷｰｼﾞ枚数
     * @return the skeegemaisuu
     */
    public Long getSkeegemaisuu() {
        return skeegemaisuu;
    }

    /**
     * ｽｷｰｼﾞ枚数
     * @param skeegemaisuu the skeegemaisuu to set
     */
    public void setSkeegemaisuu(Long skeegemaisuu) {
        this.skeegemaisuu = skeegemaisuu;
    }

    /**
     * 号機ｺｰﾄﾞ
     * @return the gouki
     */
    public String getGouki() {
        return gouki;
    }

    /**
     * 号機ｺｰﾄﾞ
     * @param gouki the gouki to set
     */
    public void setGouki(String gouki) {
        this.gouki = gouki;
    }

    /**
     * 担当者ｺｰﾄﾞ
     * @return the tantousya
     */
    public String getTantousya() {
        return tantousya;
    }

    /**
     * 担当者ｺｰﾄﾞ
     * @param tantousya the tantousya to set
     */
    public void setTantousya(String tantousya) {
        this.tantousya = tantousya;
    }

    /**
     * 確認者ｺｰﾄﾞ
     * @return the kakuninsya
     */
    public String getKakuninsya() {
        return kakuninsya;
    }

    /**
     * 確認者ｺｰﾄﾞ
     * @param kakuninsya the kakuninsya to set
     */
    public void setKakuninsya(String kakuninsya) {
        this.kakuninsya = kakuninsya;
    }

    /**
     * 乾燥温度
     * @return the kansouondo
     */
    public BigDecimal getKansouondo() {
        return kansouondo;
    }

    /**
     * 乾燥温度
     * @param kansouondo the kansouondo to set
     */
    public void setKansouondo(BigDecimal kansouondo) {
        this.kansouondo = kansouondo;
    }

    /**
     * 印刷ﾌﾟﾛﾌｧｲﾙ
     * @return the prnprofile
     */
    public String getPrnprofile() {
        return prnprofile;
    }

    /**
     * 印刷ﾌﾟﾛﾌｧｲﾙ
     * @param prnprofile the prnprofile to set
     */
    public void setPrnprofile(String prnprofile) {
        this.prnprofile = prnprofile;
    }

    /**
     * 乾燥時間
     * @return the kansoutime
     */
    public BigDecimal getKansoutime() {
        return kansoutime;
    }

    /**
     * 乾燥時間
     * @param kansoutime the kansoutime to set
     */
    public void setKansoutime(BigDecimal kansoutime) {
        this.kansoutime = kansoutime;
    }

    /**
     * 差圧
     * @return the saatu
     */
    public BigDecimal getSaatu() {
        return saatu;
    }

    /**
     * 差圧
     * @param saatu the saatu to set
     */
    public void setSaatu(BigDecimal saatu) {
        this.saatu = saatu;
    }

    /**
     * ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
     * @return the skeegespeed
     */
    public BigDecimal getSkeegespeed() {
        return skeegespeed;
    }

    /**
     * ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
     * @param skeegespeed the skeegespeed to set
     */
    public void setSkeegespeed(BigDecimal skeegespeed) {
        this.skeegespeed = skeegespeed;
    }

    /**
     * ｽｷｰｼﾞ角度
     * @return the skeegeangle
     */
    public Long getSkeegeangle() {
        return skeegeangle;
    }

    /**
     * ｽｷｰｼﾞ角度
     * @param skeegeangle the skeegeangle to set
     */
    public void setSkeegeangle(Long skeegeangle) {
        this.skeegeangle = skeegeangle;
    }

    /**
     * ﾒﾀﾙﾚｲﾀﾞｳﾝ値
     * @return the mld
     */
    public Long getMld() {
        return mld;
    }

    /**
     * ﾒﾀﾙﾚｲﾀﾞｳﾝ値
     * @param mld the mld to set
     */
    public void setMld(Long mld) {
        this.mld = mld;
    }

    /**
     * ｸﾘｱﾗﾝｽ設定値
     * @return the clearrance
     */
    public BigDecimal getClearrance() {
        return clearrance;
    }

    /**
     * ｸﾘｱﾗﾝｽ設定値
     * @param clearrance the clearrance to set
     */
    public void setClearrance(BigDecimal clearrance) {
        this.clearrance = clearrance;
    }

    /**
     * 先行ﾛｯﾄNo
     * @return the senkoulotno
     */
    public String getSenkoulotno() {
        return senkoulotno;
    }

    /**
     * 先行ﾛｯﾄNo
     * @param senkoulotno the senkoulotno to set
     */
    public void setSenkoulotno(String senkoulotno) {
        this.senkoulotno = senkoulotno;
    }

    /**
     * ﾃｰﾌﾟ使い切り
     * @return the tapetsukaikiri
     */
    public String getTapetsukaikiri() {
        return tapetsukaikiri;
    }

    /**
     * ﾃｰﾌﾟ使い切り
     * @param tapetsukaikiri the tapetsukaikiri to set
     */
    public void setTapetsukaikiri(String tapetsukaikiri) {
        this.tapetsukaikiri = tapetsukaikiri;
    }

    /**
     * 次ﾛｯﾄへ
     * @return the jilothe
     */
    public String getJilothe() {
        return jilothe;
    }

    /**
     * 次ﾛｯﾄへ
     * @param jilothe the jilothe to set
     */
    public void setJilothe(String jilothe) {
        this.jilothe = jilothe;
    }

    /**
     * 成形長さ
     * @return the seikeinagasa
     */
    public Long getSeikeinagasa() {
        return seikeinagasa;
    }

    /**
     * 成形長さ
     * @param seikeinagasa the seikeinagasa to set
     */
    public void setSeikeinagasa(Long seikeinagasa) {
        this.seikeinagasa = seikeinagasa;
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
     * 備考3
     * @return the bikou3
     */
    public String getBikou3() {
        return bikou3;
    }

    /**
     * 備考3
     * @param bikou3 the bikou3 to set
     */
    public void setBikou3(String bikou3) {
        this.bikou3 = bikou3;
    }

    /**
     * 備考4
     * @return the bikou4
     */
    public String getBikou4() {
        return bikou4;
    }

    /**
     * 備考4
     * @param bikou4 the bikou4 to set
     */
    public void setBikou4(String bikou4) {
        this.bikou4 = bikou4;
    }

    /**
     * 備考5
     * @return the bikou5
     */
    public String getBikou5() {
        return bikou5;
    }

    /**
     * 備考5
     * @param bikou5 the bikou5 to set
     */
    public void setBikou5(String bikou5) {
        this.bikou5 = bikou5;
    }

    /**
     * 膜厚1
     * @return the makuatu1
     */
    public BigDecimal getMakuatu1() {
        return makuatu1;
    }

    /**
     * 膜厚1
     * @param makuatu1 the makuatu1 to set
     */
    public void setMakuatu1(BigDecimal makuatu1) {
        this.makuatu1 = makuatu1;
    }

    /**
     * 膜厚2
     * @return the makuatu2
     */
    public BigDecimal getMakuatu2() {
        return makuatu2;
    }

    /**
     * 膜厚2
     * @param makuatu2 the makuatu2 to set
     */
    public void setMakuatu2(BigDecimal makuatu2) {
        this.makuatu2 = makuatu2;
    }

    /**
     * 膜厚3
     * @return the makuatu3
     */
    public BigDecimal getMakuatu3() {
        return makuatu3;
    }

    /**
     * 膜厚3
     * @param makuatu3 the makuatu3 to set
     */
    public void setMakuatu3(BigDecimal makuatu3) {
        this.makuatu3 = makuatu3;
    }

    /**
     * 膜厚4
     * @return the makuatu4
     */
    public BigDecimal getMakuatu4() {
        return makuatu4;
    }

    /**
     * 膜厚4
     * @param makuatu4 the makuatu4 to set
     */
    public void setMakuatu4(BigDecimal makuatu4) {
        this.makuatu4 = makuatu4;
    }

    /**
     * 膜厚5
     * @return the makuatu5
     */
    public BigDecimal getMakuatu5() {
        return makuatu5;
    }

    /**
     * 膜厚5
     * @param makuatu5 the makuatu5 to set
     */
    public void setMakuatu5(BigDecimal makuatu5) {
        this.makuatu5 = makuatu5;
    }

    /**
     * ﾍﾟｰｽﾄﾛｯﾄNo2
     * @return the pastelotno2
     */
    public String getPastelotno2() {
        return pastelotno2;
    }

    /**
     * ﾍﾟｰｽﾄﾛｯﾄNo2
     * @param pastelotno2 the pastelotno2 to set
     */
    public void setPastelotno2(String pastelotno2) {
        this.pastelotno2 = pastelotno2;
    }

    /**
     * ﾍﾟｰｽﾄﾛｯﾄNo3
     * @return the pastelotno3
     */
    public String getPastelotno3() {
        return pastelotno3;
    }

    /**
     * ﾍﾟｰｽﾄﾛｯﾄNo3
     * @param pastelotno3 the pastelotno3 to set
     */
    public void setPastelotno3(String pastelotno3) {
        this.pastelotno3 = pastelotno3;
    }

    /**
     * ﾍﾟｰｽﾄﾛｯﾄNo4
     * @return the pastelotno4
     */
    public String getPastelotno4() {
        return pastelotno4;
    }

    /**
     * ﾍﾟｰｽﾄﾛｯﾄNo4
     * @param pastelotno4 the pastelotno4 to set
     */
    public void setPastelotno4(String pastelotno4) {
        this.pastelotno4 = pastelotno4;
    }

    /**
     * ﾍﾟｰｽﾄﾛｯﾄNo5
     * @return the pastelotno5
     */
    public String getPastelotno5() {
        return pastelotno5;
    }

    /**
     * ﾍﾟｰｽﾄﾛｯﾄNo5
     * @param pastelotno5 the pastelotno5 to set
     */
    public void setPastelotno5(String pastelotno5) {
        this.pastelotno5 = pastelotno5;
    }

    /**
     * ﾍﾟｰｽﾄ粘度2
     * @return the pastenendo2
     */
    public BigDecimal getPastenendo2() {
        return pastenendo2;
    }

    /**
     * ﾍﾟｰｽﾄ粘度2
     * @param pastenendo2 the pastenendo2 to set
     */
    public void setPastenendo2(BigDecimal pastenendo2) {
        this.pastenendo2 = pastenendo2;
    }

    /**
     * ﾍﾟｰｽﾄ粘度3
     * @return the pastenendo3
     */
    public BigDecimal getPastenendo3() {
        return pastenendo3;
    }

    /**
     * ﾍﾟｰｽﾄ粘度3
     * @param pastenendo3 the pastenendo3 to set
     */
    public void setPastenendo3(BigDecimal pastenendo3) {
        this.pastenendo3 = pastenendo3;
    }

    /**
     * ﾍﾟｰｽﾄ粘度4
     * @return the pastenendo4
     */
    public BigDecimal getPastenendo4() {
        return pastenendo4;
    }

    /**
     * ﾍﾟｰｽﾄ粘度4
     * @param pastenendo4 the pastenendo4 to set
     */
    public void setPastenendo4(BigDecimal pastenendo4) {
        this.pastenendo4 = pastenendo4;
    }

    /**
     * ﾍﾟｰｽﾄ粘度5
     * @return the pastenendo5
     */
    public BigDecimal getPastenendo5() {
        return pastenendo5;
    }

    /**
     * ﾍﾟｰｽﾄ粘度5
     * @param pastenendo5 the pastenendo5 to set
     */
    public void setPastenendo5(BigDecimal pastenendo5) {
        this.pastenendo5 = pastenendo5;
    }

    /**
     * ﾍﾟｰｽﾄ温度2
     * @return the pasteondo2
     */
    public BigDecimal getPasteondo2() {
        return pasteondo2;
    }

    /**
     * ﾍﾟｰｽﾄ温度2
     * @param pasteondo2 the pasteondo2 to set
     */
    public void setPasteondo2(BigDecimal pasteondo2) {
        this.pasteondo2 = pasteondo2;
    }

    /**
     * ﾍﾟｰｽﾄ温度3
     * @return the pasteondo3
     */
    public BigDecimal getPasteondo3() {
        return pasteondo3;
    }

    /**
     * ﾍﾟｰｽﾄ温度3
     * @param pasteondo3 the pasteondo3 to set
     */
    public void setPasteondo3(BigDecimal pasteondo3) {
        this.pasteondo3 = pasteondo3;
    }

    /**
     * ﾍﾟｰｽﾄ温度4
     * @return the pasteondo4
     */
    public BigDecimal getPasteondo4() {
        return pasteondo4;
    }

    /**
     * ﾍﾟｰｽﾄ温度4
     * @param pasteondo4 the pasteondo4 to set
     */
    public void setPasteondo4(BigDecimal pasteondo4) {
        this.pasteondo4 = pasteondo4;
    }

    /**
     * ﾍﾟｰｽﾄ温度5
     * @return the pasteondo5
     */
    public BigDecimal getPasteondo5() {
        return pasteondo5;
    }

    /**
     * ﾍﾟｰｽﾄ温度5
     * @param pasteondo5 the pasteondo5 to set
     */
    public void setPasteondo5(BigDecimal pasteondo5) {
        this.pasteondo5 = pasteondo5;
    }

    /**
     * 乾燥温度2
     * @return the kansouondo2
     */
    public BigDecimal getKansouondo2() {
        return kansouondo2;
    }

    /**
     * 乾燥温度2
     * @param kansouondo2 the kansouondo2 to set
     */
    public void setKansouondo2(BigDecimal kansouondo2) {
        this.kansouondo2 = kansouondo2;
    }

    /**
     * 乾燥温度3
     * @return the kansouondo3
     */
    public BigDecimal getKansouondo3() {
        return kansouondo3;
    }

    /**
     * 乾燥温度3
     * @param kansouondo3 the kansouondo3 to set
     */
    public void setKansouondo3(BigDecimal kansouondo3) {
        this.kansouondo3 = kansouondo3;
    }

    /**
     * 乾燥温度4
     * @return the kansouondo4
     */
    public BigDecimal getKansouondo4() {
        return kansouondo4;
    }

    /**
     * 乾燥温度4
     * @param kansouondo4 the kansouondo4 to set
     */
    public void setKansouondo4(BigDecimal kansouondo4) {
        this.kansouondo4 = kansouondo4;
    }

    /**
     * 乾燥温度5
     * @return the kansouondo5
     */
    public BigDecimal getKansouondo5() {
        return kansouondo5;
    }

    /**
     * 乾燥温度5
     * @param kansouondo5 the kansouondo5 to set
     */
    public void setKansouondo5(BigDecimal kansouondo5) {
        this.kansouondo5 = kansouondo5;
    }

    /**
     * ｽｷｰｼﾞ枚数2
     * @return the skeegemaisuu2
     */
    public Long getSkeegemaisuu2() {
        return skeegemaisuu2;
    }

    /**
     * ｽｷｰｼﾞ枚数2
     * @param skeegemaisuu2 the skeegemaisuu2 to set
     */
    public void setSkeegemaisuu2(Long skeegemaisuu2) {
        this.skeegemaisuu2 = skeegemaisuu2;
    }

    /**
     * ﾃｰﾌﾟﾛｰﾙNo1
     * @return the taperollno1
     */
    public String getTaperollno1() {
        return taperollno1;
    }

    /**
     * ﾃｰﾌﾟﾛｰﾙNo1
     * @param taperollno1 the taperollno1 to set
     */
    public void setTaperollno1(String taperollno1) {
        this.taperollno1 = taperollno1;
    }

    /**
     * ﾃｰﾌﾟﾛｰﾙNo2
     * @return the taperollno2
     */
    public String getTaperollno2() {
        return taperollno2;
    }

    /**
     * ﾃｰﾌﾟﾛｰﾙNo2
     * @param taperollno2 the taperollno2 to set
     */
    public void setTaperollno2(String taperollno2) {
        this.taperollno2 = taperollno2;
    }

    /**
     * ﾃｰﾌﾟﾛｰﾙNo3
     * @return the taperollno3
     */
    public String getTaperollno3() {
        return taperollno3;
    }

    /**
     * ﾃｰﾌﾟﾛｰﾙNo3
     * @param taperollno3 the taperollno3 to set
     */
    public void setTaperollno3(String taperollno3) {
        this.taperollno3 = taperollno3;
    }

    /**
     * ﾃｰﾌﾟﾛｰﾙNo4
     * @return the taperollno4
     */
    public String getTaperollno4() {
        return taperollno4;
    }

    /**
     * ﾃｰﾌﾟﾛｰﾙNo4
     * @param taperollno4 the taperollno4 to set
     */
    public void setTaperollno4(String taperollno4) {
        this.taperollno4 = taperollno4;
    }

    /**
     * ﾃｰﾌﾟﾛｰﾙNo5
     * @return the taperollno5
     */
    public String getTaperollno5() {
        return taperollno5;
    }

    /**
     * ﾃｰﾌﾟﾛｰﾙNo5
     * @param taperollno5 the taperollno5 to set
     */
    public void setTaperollno5(String taperollno5) {
        this.taperollno5 = taperollno5;
    }

    /**
     * ﾍﾟｰｽﾄ品名
     * @return the pastehinmei
     */
    public String getPastehinmei() {
        return pastehinmei;
    }

    /**
     * ﾍﾟｰｽﾄ品名
     * @param pastehinmei the pastehinmei to set
     */
    public void setPastehinmei(String pastehinmei) {
        this.pastehinmei = pastehinmei;
    }

    /**
     * 製版名
     * @return the seihanmei
     */
    public String getSeihanmei() {
        return seihanmei;
    }

    /**
     * 製版名
     * @param seihanmei the seihanmei to set
     */
    public void setSeihanmei(String seihanmei) {
        this.seihanmei = seihanmei;
    }

    /**
     * ｽﾀｰﾄ時膜厚AVE
     * @return the makuatuaveStart
     */
    public BigDecimal getMakuatuaveStart() {
        return makuatuaveStart;
    }

    /**
     * ｽﾀｰﾄ時膜厚AVE
     * @param makuatuaveStart the makuatuaveStart to set
     */
    public void setMakuatuaveStart(BigDecimal makuatuaveStart) {
        this.makuatuaveStart = makuatuaveStart;
    }

    /**
     * ｽﾀｰﾄ時膜厚MAX
     * @return the makuatumaxStart
     */
    public BigDecimal getMakuatumaxStart() {
        return makuatumaxStart;
    }

    /**
     * ｽﾀｰﾄ時膜厚MAX
     * @param makuatumaxStart the makuatumaxStart to set
     */
    public void setMakuatumaxStart(BigDecimal makuatumaxStart) {
        this.makuatumaxStart = makuatumaxStart;
    }

    /**
     * ｽﾀｰﾄ時膜厚MIN
     * @return the makuatuminStart
     */
    public BigDecimal getMakuatuminStart() {
        return makuatuminStart;
    }

    /**
     * ｽﾀｰﾄ時膜厚MIN
     * @param makuatuminStart the makuatuminStart to set
     */
    public void setMakuatuminStart(BigDecimal makuatuminStart) {
        this.makuatuminStart = makuatuminStart;
    }

    /**
     * ｽﾀｰﾄ時PTN間距離X
     * @return the startPtnDistX
     */
    public Long getStartPtnDistX() {
        return startPtnDistX;
    }

    /**
     * ｽﾀｰﾄ時PTN間距離X
     * @param startPtnDistX the startPtnDistX to set
     */
    public void setStartPtnDistX(Long startPtnDistX) {
        this.startPtnDistX = startPtnDistX;
    }

    /**
     * ｽﾀｰﾄ時PTN間距離Y
     * @return the startPtnDistY
     */
    public Long getStartPtnDistY() {
        return startPtnDistY;
    }

    /**
     * ｽﾀｰﾄ時PTN間距離Y
     * @param startPtnDistY the startPtnDistY to set
     */
    public void setStartPtnDistY(Long startPtnDistY) {
        this.startPtnDistY = startPtnDistY;
    }

    /**
     * ｾｯﾃｨﾝｸﾞ担当者ｺｰﾄﾞ
     * @return the tantoSetting
     */
    public String getTantoSetting() {
        return tantoSetting;
    }

    /**
     * ｾｯﾃｨﾝｸﾞ担当者ｺｰﾄﾞ
     * @param tantoSetting the tantoSetting to set
     */
    public void setTantoSetting(String tantoSetting) {
        this.tantoSetting = tantoSetting;
    }

    /**
     * 終了時膜厚AVE
     * @return the makuatuaveEnd
     */
    public BigDecimal getMakuatuaveEnd() {
        return makuatuaveEnd;
    }

    /**
     * 終了時膜厚AVE
     * @param makuatuaveEnd the makuatuaveEnd to set
     */
    public void setMakuatuaveEnd(BigDecimal makuatuaveEnd) {
        this.makuatuaveEnd = makuatuaveEnd;
    }

    /**
     * 終了時膜厚MAX
     * @return the makuatumaxEnd
     */
    public BigDecimal getMakuatumaxEnd() {
        return makuatumaxEnd;
    }

    /**
     * 終了時膜厚MAX
     * @param makuatumaxEnd the makuatumaxEnd to set
     */
    public void setMakuatumaxEnd(BigDecimal makuatumaxEnd) {
        this.makuatumaxEnd = makuatumaxEnd;
    }

    /**
     * 終了時膜厚MIN
     * @return the makuatuminEnd
     */
    public BigDecimal getMakuatuminEnd() {
        return makuatuminEnd;
    }

    /**
     * 終了時膜厚MIN
     * @param makuatuminEnd the makuatuminEnd to set
     */
    public void setMakuatuminEnd(BigDecimal makuatuminEnd) {
        this.makuatuminEnd = makuatuminEnd;
    }

    /**
     * 終了時PTN間距離X
     * @return the endPtnDistX
     */
    public Long getEndPtnDistX() {
        return endPtnDistX;
    }

    /**
     * 終了時PTN間距離X
     * @param endPtnDistX the endPtnDistX to set
     */
    public void setEndPtnDistX(Long endPtnDistX) {
        this.endPtnDistX = endPtnDistX;
    }

    /**
     * 終了時PTN間距離Y
     * @return the endPtnDistY
     */
    public Long getEndPtnDistY() {
        return endPtnDistY;
    }

    /**
     * 終了時PTN間距離Y
     * @param endPtnDistY the endPtnDistY to set
     */
    public void setEndPtnDistY(Long endPtnDistY) {
        this.endPtnDistY = endPtnDistY;
    }

    /**
     * 終了時担当者ｺｰﾄﾞ
     * @return the tantoEnd
     */
    public String getTantoEnd() {
        return tantoEnd;
    }

    /**
     * 終了時担当者ｺｰﾄﾞ
     * @param tantoEnd the tantoEnd to set
     */
    public void setTantoEnd(String tantoEnd) {
        this.tantoEnd = tantoEnd;
    }

    /**
     * 指示乾燥温度1
     * @return the sijiondo
     */
    public BigDecimal getSijiondo() {
        return sijiondo;
    }

    /**
     * 指示乾燥温度1
     * @param sijiondo the sijiondo to set
     */
    public void setSijiondo(BigDecimal sijiondo) {
        this.sijiondo = sijiondo;
    }

    /**
     * 指示乾燥温度2
     * @return the sijiondo2
     */
    public BigDecimal getSijiondo2() {
        return sijiondo2;
    }

    /**
     * 指示乾燥温度2
     * @param sijiondo2 the sijiondo2 to set
     */
    public void setSijiondo2(BigDecimal sijiondo2) {
        this.sijiondo2 = sijiondo2;
    }

    /**
     * 指示乾燥温度3
     * @return the sijiondo3
     */
    public BigDecimal getSijiondo3() {
        return sijiondo3;
    }

    /**
     * 指示乾燥温度3
     * @param sijiondo3 the sijiondo3 to set
     */
    public void setSijiondo3(BigDecimal sijiondo3) {
        this.sijiondo3 = sijiondo3;
    }

    /**
     * 指示乾燥温度4
     * @return the sijiondo4
     */
    public BigDecimal getSijiondo4() {
        return sijiondo4;
    }

    /**
     * 指示乾燥温度4
     * @param sijiondo4 the sijiondo4 to set
     */
    public void setSijiondo4(BigDecimal sijiondo4) {
        this.sijiondo4 = sijiondo4;
    }

    /**
     * 指示乾燥温度5
     * @return the sijiondo5
     */
    public BigDecimal getSijiondo5() {
        return sijiondo5;
    }

    /**
     * 指示乾燥温度5
     * @param sijiondo5 the sijiondo5 to set
     */
    public void setSijiondo5(BigDecimal sijiondo5) {
        this.sijiondo5 = sijiondo5;
    }

    /**
     * ﾍﾟｰｽﾄ固形分1
     * @return the pkokeibun1
     */
    public BigDecimal getPkokeibun1() {
        return pkokeibun1;
    }

    /**
     * ﾍﾟｰｽﾄ固形分1
     * @param pkokeibun1 the pkokeibun1 to set
     */
    public void setPkokeibun1(BigDecimal pkokeibun1) {
        this.pkokeibun1 = pkokeibun1;
    }

    /**
     * ﾍﾟｰｽﾄ固形分2
     * @return the pkokeibun2
     */
    public BigDecimal getPkokeibun2() {
        return pkokeibun2;
    }

    /**
     * ﾍﾟｰｽﾄ固形分2
     * @param pkokeibun2 the pkokeibun2 to set
     */
    public void setPkokeibun2(BigDecimal pkokeibun2) {
        this.pkokeibun2 = pkokeibun2;
    }

    /**
     * PETﾌｨﾙﾑ種類
     * @return the petfilmsyurui
     */
    public String getPetfilmsyurui() {
        return petfilmsyurui;
    }

    /**
     * PETﾌｨﾙﾑ種類
     * @param petfilmsyurui the petfilmsyurui to set
     */
    public void setPetfilmsyurui(String petfilmsyurui) {
        this.petfilmsyurui = petfilmsyurui;
    }

    /**
     * 印刷ｽﾀｰﾄ膜厚CV
     * @return the makuatucvStart
     */
    public BigDecimal getMakuatucvStart() {
        return makuatucvStart;
    }

    /**
     * 印刷ｽﾀｰﾄ膜厚CV
     * @param makuatucvStart the makuatucvStart to set
     */
    public void setMakuatucvStart(BigDecimal makuatucvStart) {
        this.makuatucvStart = makuatucvStart;
    }

    /**
     * ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
     * @return the nijimikasureStart
     */
    public String getNijimikasureStart() {
        return nijimikasureStart;
    }

    /**
     * ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
     * @param nijimikasureStart the nijimikasureStart to set
     */
    public void setNijimikasureStart(String nijimikasureStart) {
        this.nijimikasureStart = nijimikasureStart;
    }

    /**
     * 印刷ｴﾝﾄﾞ膜厚CV
     * @return the makuatucvEnd
     */
    public BigDecimal getMakuatucvEnd() {
        return makuatucvEnd;
    }

    /**
     * 印刷ｴﾝﾄﾞ膜厚CV
     * @param makuatucvEnd the makuatucvEnd to set
     */
    public void setMakuatucvEnd(BigDecimal makuatucvEnd) {
        this.makuatucvEnd = makuatucvEnd;
    }

    /**
     * 終了時ﾆｼﾞﾐ・ｶｽﾚ確認
     * @return the nijimikasureEnd
     */
    public String getNijimikasureEnd() {
        return nijimikasureEnd;
    }

    /**
     * 終了時ﾆｼﾞﾐ・ｶｽﾚ確認
     * @param nijimikasureEnd the nijimikasureEnd to set
     */
    public void setNijimikasureEnd(String nijimikasureEnd) {
        this.nijimikasureEnd = nijimikasureEnd;
    }

    /**
     * 印刷枚数
     * @return the printmaisuu
     */
    public Long getPrintmaisuu() {
        return printmaisuu;
    }

    /**
     * 印刷枚数
     * @param printmaisuu the printmaisuu to set
     */
    public void setPrintmaisuu(Long printmaisuu) {
        this.printmaisuu = printmaisuu;
    }

    /**
     * ﾃｰﾌﾞﾙｸﾘｱﾗﾝｽ
     * @return the tableClearrance
     */
    public BigDecimal getTableClearrance() {
        return tableClearrance;
    }

    /**
     * ﾃｰﾌﾞﾙｸﾘｱﾗﾝｽ
     * @param tableClearrance the tableClearrance to set
     */
    public void setTableClearrance(BigDecimal tableClearrance) {
        this.tableClearrance = tableClearrance;
    }

    /**
     * ｽｸﾚｯﾊﾟｰ速度
     * @return the scraperspeed
     */
    public Long getScraperspeed() {
        return scraperspeed;
    }

    /**
     * ｽｸﾚｯﾊﾟｰ速度
     * @param scraperspeed the scraperspeed to set
     */
    public void setScraperspeed(Long scraperspeed) {
        this.scraperspeed = scraperspeed;
    }

    /**
     * ｽｷｰｼﾞ外観
     * @return the skeegegaikan
     */
    public String getSkeegegaikan() {
        return skeegegaikan;
    }

    /**
     * ｽｷｰｼﾞ外観
     * @param skeegegaikan the skeegegaikan to set
     */
    public void setSkeegegaikan(String skeegegaikan) {
        this.skeegegaikan = skeegegaikan;
    }

    /**
     * 膜厚ｽﾀｰﾄ1
     * @return the makuatsuStart1
     */
    public BigDecimal getMakuatsuStart1() {
        return makuatsuStart1;
    }

    /**
     * 膜厚ｽﾀｰﾄ1
     * @param makuatsuStart1 the makuatsuStart1 to set
     */
    public void setMakuatsuStart1(BigDecimal makuatsuStart1) {
        this.makuatsuStart1 = makuatsuStart1;
    }

    /**
     * 膜厚ｽﾀｰﾄ2
     * @return the makuatsuStart2
     */
    public BigDecimal getMakuatsuStart2() {
        return makuatsuStart2;
    }

    /**
     * 膜厚ｽﾀｰﾄ2
     * @param makuatsuStart2 the makuatsuStart2 to set
     */
    public void setMakuatsuStart2(BigDecimal makuatsuStart2) {
        this.makuatsuStart2 = makuatsuStart2;
    }

    /**
     * 膜厚ｽﾀｰﾄ3
     * @return the makuatsuStart3
     */
    public BigDecimal getMakuatsuStart3() {
        return makuatsuStart3;
    }

    /**
     * 膜厚ｽﾀｰﾄ3
     * @param makuatsuStart3 the makuatsuStart3 to set
     */
    public void setMakuatsuStart3(BigDecimal makuatsuStart3) {
        this.makuatsuStart3 = makuatsuStart3;
    }

    /**
     * 膜厚ｽﾀｰﾄ4
     * @return the makuatsuStart4
     */
    public BigDecimal getMakuatsuStart4() {
        return makuatsuStart4;
    }

    /**
     * 膜厚ｽﾀｰﾄ4
     * @param makuatsuStart4 the makuatsuStart4 to set
     */
    public void setMakuatsuStart4(BigDecimal makuatsuStart4) {
        this.makuatsuStart4 = makuatsuStart4;
    }

    /**
     * 膜厚ｽﾀｰﾄ5
     * @return the makuatsuStart5
     */
    public BigDecimal getMakuatsuStart5() {
        return makuatsuStart5;
    }

    /**
     * 膜厚ｽﾀｰﾄ5
     * @param makuatsuStart5 the makuatsuStart5 to set
     */
    public void setMakuatsuStart5(BigDecimal makuatsuStart5) {
        this.makuatsuStart5 = makuatsuStart5;
    }

    /**
     * 膜厚ｽﾀｰﾄ6
     * @return the makuatsuStart6
     */
    public BigDecimal getMakuatsuStart6() {
        return makuatsuStart6;
    }

    /**
     * 膜厚ｽﾀｰﾄ6
     * @param makuatsuStart6 the makuatsuStart6 to set
     */
    public void setMakuatsuStart6(BigDecimal makuatsuStart6) {
        this.makuatsuStart6 = makuatsuStart6;
    }

    /**
     * 膜厚ｽﾀｰﾄ7
     * @return the makuatsuStart7
     */
    public BigDecimal getMakuatsuStart7() {
        return makuatsuStart7;
    }

    /**
     * 膜厚ｽﾀｰﾄ7
     * @param makuatsuStart7 the makuatsuStart7 to set
     */
    public void setMakuatsuStart7(BigDecimal makuatsuStart7) {
        this.makuatsuStart7 = makuatsuStart7;
    }

    /**
     * 膜厚ｽﾀｰﾄ8
     * @return the makuatsuStart8
     */
    public BigDecimal getMakuatsuStart8() {
        return makuatsuStart8;
    }

    /**
     * 膜厚ｽﾀｰﾄ8
     * @param makuatsuStart8 the makuatsuStart8 to set
     */
    public void setMakuatsuStart8(BigDecimal makuatsuStart8) {
        this.makuatsuStart8 = makuatsuStart8;
    }

    /**
     * 膜厚ｽﾀｰﾄ9
     * @return the makuatsuStart9
     */
    public BigDecimal getMakuatsuStart9() {
        return makuatsuStart9;
    }

    /**
     * 膜厚ｽﾀｰﾄ9
     * @param makuatsuStart9 the makuatsuStart9 to set
     */
    public void setMakuatsuStart9(BigDecimal makuatsuStart9) {
        this.makuatsuStart9 = makuatsuStart9;
    }

    /**
     * PTN距離X ｽﾀｰﾄ1
     * @return the startPtnDistX1
     */
    public Long getStartPtnDistX1() {
        return startPtnDistX1;
    }

    /**
     * PTN距離X ｽﾀｰﾄ1
     * @param startPtnDistX1 the startPtnDistX1 to set
     */
    public void setStartPtnDistX1(Long startPtnDistX1) {
        this.startPtnDistX1 = startPtnDistX1;
    }

    /**
     * PTN距離X ｽﾀｰﾄ2
     * @return the startPtnDistX2
     */
    public Long getStartPtnDistX2() {
        return startPtnDistX2;
    }

    /**
     * PTN距離X ｽﾀｰﾄ2
     * @param startPtnDistX2 the startPtnDistX2 to set
     */
    public void setStartPtnDistX2(Long startPtnDistX2) {
        this.startPtnDistX2 = startPtnDistX2;
    }

    /**
     * PTN距離X ｽﾀｰﾄ3
     * @return the startPtnDistX3
     */
    public Long getStartPtnDistX3() {
        return startPtnDistX3;
    }

    /**
     * PTN距離X ｽﾀｰﾄ3
     * @param startPtnDistX3 the startPtnDistX3 to set
     */
    public void setStartPtnDistX3(Long startPtnDistX3) {
        this.startPtnDistX3 = startPtnDistX3;
    }

    /**
     * PTN距離X ｽﾀｰﾄ4
     * @return the startPtnDistX4
     */
    public Long getStartPtnDistX4() {
        return startPtnDistX4;
    }

    /**
     * PTN距離X ｽﾀｰﾄ4
     * @param startPtnDistX4 the startPtnDistX4 to set
     */
    public void setStartPtnDistX4(Long startPtnDistX4) {
        this.startPtnDistX4 = startPtnDistX4;
    }

    /**
     * PTN距離X ｽﾀｰﾄ5
     * @return the startPtnDistX5
     */
    public Long getStartPtnDistX5() {
        return startPtnDistX5;
    }

    /**
     * PTN距離X ｽﾀｰﾄ5
     * @param startPtnDistX5 the startPtnDistX5 to set
     */
    public void setStartPtnDistX5(Long startPtnDistX5) {
        this.startPtnDistX5 = startPtnDistX5;
    }

    /**
     * PTN距離Y ｽﾀｰﾄ1
     * @return the startPtnDistY1
     */
    public Long getStartPtnDistY1() {
        return startPtnDistY1;
    }

    /**
     * PTN距離Y ｽﾀｰﾄ1
     * @param startPtnDistY1 the startPtnDistY1 to set
     */
    public void setStartPtnDistY1(Long startPtnDistY1) {
        this.startPtnDistY1 = startPtnDistY1;
    }

    /**
     * PTN距離Y ｽﾀｰﾄ2
     * @return the startPtnDistY2
     */
    public Long getStartPtnDistY2() {
        return startPtnDistY2;
    }

    /**
     * PTN距離Y ｽﾀｰﾄ2
     * @param startPtnDistY2 the startPtnDistY2 to set
     */
    public void setStartPtnDistY2(Long startPtnDistY2) {
        this.startPtnDistY2 = startPtnDistY2;
    }

    /**
     * PTN距離Y ｽﾀｰﾄ3
     * @return the startPtnDistY3
     */
    public Long getStartPtnDistY3() {
        return startPtnDistY3;
    }

    /**
     * PTN距離Y ｽﾀｰﾄ3
     * @param startPtnDistY3 the startPtnDistY3 to set
     */
    public void setStartPtnDistY3(Long startPtnDistY3) {
        this.startPtnDistY3 = startPtnDistY3;
    }

    /**
     * PTN距離Y ｽﾀｰﾄ4
     * @return the startPtnDistY4
     */
    public Long getStartPtnDistY4() {
        return startPtnDistY4;
    }

    /**
     * PTN距離Y ｽﾀｰﾄ4
     * @param startPtnDistY4 the startPtnDistY4 to set
     */
    public void setStartPtnDistY4(Long startPtnDistY4) {
        this.startPtnDistY4 = startPtnDistY4;
    }

    /**
     * PTN距離Y ｽﾀｰﾄ5
     * @return the startPtnDistY5
     */
    public Long getStartPtnDistY5() {
        return startPtnDistY5;
    }

    /**
     * PTN距離Y ｽﾀｰﾄ5
     * @param startPtnDistY5 the startPtnDistY5 to set
     */
    public void setStartPtnDistY5(Long startPtnDistY5) {
        this.startPtnDistY5 = startPtnDistY5;
    }

    /**
     * 膜厚ｴﾝﾄﾞ1
     * @return the makuatsuEnd1
     */
    public BigDecimal getMakuatsuEnd1() {
        return makuatsuEnd1;
    }

    /**
     * 膜厚ｴﾝﾄﾞ1
     * @param makuatsuEnd1 the makuatsuEnd1 to set
     */
    public void setMakuatsuEnd1(BigDecimal makuatsuEnd1) {
        this.makuatsuEnd1 = makuatsuEnd1;
    }

    /**
     * 膜厚ｴﾝﾄﾞ2
     * @return the makuatsuEnd2
     */
    public BigDecimal getMakuatsuEnd2() {
        return makuatsuEnd2;
    }

    /**
     * 膜厚ｴﾝﾄﾞ2
     * @param makuatsuEnd2 the makuatsuEnd2 to set
     */
    public void setMakuatsuEnd2(BigDecimal makuatsuEnd2) {
        this.makuatsuEnd2 = makuatsuEnd2;
    }

    /**
     * 膜厚ｴﾝﾄﾞ3
     * @return the makuatsuEnd3
     */
    public BigDecimal getMakuatsuEnd3() {
        return makuatsuEnd3;
    }

    /**
     * 膜厚ｴﾝﾄﾞ3
     * @param makuatsuEnd3 the makuatsuEnd3 to set
     */
    public void setMakuatsuEnd3(BigDecimal makuatsuEnd3) {
        this.makuatsuEnd3 = makuatsuEnd3;
    }

    /**
     * 膜厚ｴﾝﾄﾞ4
     * @return the makuatsuEnd4
     */
    public BigDecimal getMakuatsuEnd4() {
        return makuatsuEnd4;
    }

    /**
     * 膜厚ｴﾝﾄﾞ4
     * @param makuatsuEnd4 the makuatsuEnd4 to set
     */
    public void setMakuatsuEnd4(BigDecimal makuatsuEnd4) {
        this.makuatsuEnd4 = makuatsuEnd4;
    }

    /**
     * 膜厚ｴﾝﾄﾞ5
     * @return the makuatsuEnd5
     */
    public BigDecimal getMakuatsuEnd5() {
        return makuatsuEnd5;
    }

    /**
     * 膜厚ｴﾝﾄﾞ5
     * @param makuatsuEnd5 the makuatsuEnd5 to set
     */
    public void setMakuatsuEnd5(BigDecimal makuatsuEnd5) {
        this.makuatsuEnd5 = makuatsuEnd5;
    }

    /**
     * 膜厚ｴﾝﾄﾞ6
     * @return the makuatsuEnd6
     */
    public BigDecimal getMakuatsuEnd6() {
        return makuatsuEnd6;
    }

    /**
     * 膜厚ｴﾝﾄﾞ6
     * @param makuatsuEnd6 the makuatsuEnd6 to set
     */
    public void setMakuatsuEnd6(BigDecimal makuatsuEnd6) {
        this.makuatsuEnd6 = makuatsuEnd6;
    }

    /**
     * 膜厚ｴﾝﾄﾞ7
     * @return the makuatsuEnd7
     */
    public BigDecimal getMakuatsuEnd7() {
        return makuatsuEnd7;
    }

    /**
     * 膜厚ｴﾝﾄﾞ7
     * @param makuatsuEnd7 the makuatsuEnd7 to set
     */
    public void setMakuatsuEnd7(BigDecimal makuatsuEnd7) {
        this.makuatsuEnd7 = makuatsuEnd7;
    }

    /**
     * 膜厚ｴﾝﾄﾞ8
     * @return the makuatsuEnd8
     */
    public BigDecimal getMakuatsuEnd8() {
        return makuatsuEnd8;
    }

    /**
     * 膜厚ｴﾝﾄﾞ8
     * @param makuatsuEnd8 the makuatsuEnd8 to set
     */
    public void setMakuatsuEnd8(BigDecimal makuatsuEnd8) {
        this.makuatsuEnd8 = makuatsuEnd8;
    }

    /**
     * 膜厚ｴﾝﾄﾞ9
     * @return the makuatsuEnd9
     */
    public BigDecimal getMakuatsuEnd9() {
        return makuatsuEnd9;
    }

    /**
     * 膜厚ｴﾝﾄﾞ9
     * @param makuatsuEnd9 the makuatsuEnd9 to set
     */
    public void setMakuatsuEnd9(BigDecimal makuatsuEnd9) {
        this.makuatsuEnd9 = makuatsuEnd9;
    }

    /**
     * PTN距離X ｴﾝﾄﾞ1
     * @return the endPtnDistX1
     */
    public Long getEndPtnDistX1() {
        return endPtnDistX1;
    }

    /**
     * PTN距離X ｴﾝﾄﾞ1
     * @param endPtnDistX1 the endPtnDistX1 to set
     */
    public void setEndPtnDistX1(Long endPtnDistX1) {
        this.endPtnDistX1 = endPtnDistX1;
    }

    /**
     * PTN距離X ｴﾝﾄﾞ2
     * @return the endPtnDistX2
     */
    public Long getEndPtnDistX2() {
        return endPtnDistX2;
    }

    /**
     * PTN距離X ｴﾝﾄﾞ2
     * @param endPtnDistX2 the endPtnDistX2 to set
     */
    public void setEndPtnDistX2(Long endPtnDistX2) {
        this.endPtnDistX2 = endPtnDistX2;
    }

    /**
     * PTN距離X ｴﾝﾄﾞ3
     * @return the endPtnDistX3
     */
    public Long getEndPtnDistX3() {
        return endPtnDistX3;
    }

    /**
     * PTN距離X ｴﾝﾄﾞ3
     * @param endPtnDistX3 the endPtnDistX3 to set
     */
    public void setEndPtnDistX3(Long endPtnDistX3) {
        this.endPtnDistX3 = endPtnDistX3;
    }

    /**
     * PTN距離X ｴﾝﾄﾞ4
     * @return the endPtnDistX4
     */
    public Long getEndPtnDistX4() {
        return endPtnDistX4;
    }

    /**
     * PTN距離X ｴﾝﾄﾞ4
     * @param endPtnDistX4 the endPtnDistX4 to set
     */
    public void setEndPtnDistX4(Long endPtnDistX4) {
        this.endPtnDistX4 = endPtnDistX4;
    }

    /**
     * PTN距離X ｴﾝﾄﾞ5
     * @return the endPtnDistX5
     */
    public Long getEndPtnDistX5() {
        return endPtnDistX5;
    }

    /**
     * PTN距離X ｴﾝﾄﾞ5
     * @param endPtnDistX5 the endPtnDistX5 to set
     */
    public void setEndPtnDistX5(Long endPtnDistX5) {
        this.endPtnDistX5 = endPtnDistX5;
    }

    /**
     * PTN距離Y ｴﾝﾄﾞ1
     * @return the endPtnDistY1
     */
    public Long getEndPtnDistY1() {
        return endPtnDistY1;
    }

    /**
     * PTN距離Y ｴﾝﾄﾞ1
     * @param endPtnDistY1 the endPtnDistY1 to set
     */
    public void setEndPtnDistY1(Long endPtnDistY1) {
        this.endPtnDistY1 = endPtnDistY1;
    }

    /**
     * PTN距離Y ｴﾝﾄﾞ2
     * @return the endPtnDistY2
     */
    public Long getEndPtnDistY2() {
        return endPtnDistY2;
    }

    /**
     * PTN距離Y ｴﾝﾄﾞ2
     * @param endPtnDistY2 the endPtnDistY2 to set
     */
    public void setEndPtnDistY2(Long endPtnDistY2) {
        this.endPtnDistY2 = endPtnDistY2;
    }

    /**
     * PTN距離Y ｴﾝﾄﾞ3
     * @return the endPtnDistY3
     */
    public Long getEndPtnDistY3() {
        return endPtnDistY3;
    }

    /**
     * PTN距離Y ｴﾝﾄﾞ3
     * @param endPtnDistY3 the endPtnDistY3 to set
     */
    public void setEndPtnDistY3(Long endPtnDistY3) {
        this.endPtnDistY3 = endPtnDistY3;
    }

    /**
     * PTN距離Y ｴﾝﾄﾞ4
     * @return the endPtnDistY4
     */
    public Long getEndPtnDistY4() {
        return endPtnDistY4;
    }

    /**
     * PTN距離Y ｴﾝﾄﾞ4
     * @param endPtnDistY4 the endPtnDistY4 to set
     */
    public void setEndPtnDistY4(Long endPtnDistY4) {
        this.endPtnDistY4 = endPtnDistY4;
    }

    /**
     * PTN距離Y ｴﾝﾄﾞ5
     * @return the endPtnDistY5
     */
    public Long getEndPtnDistY5() {
        return endPtnDistY5;
    }

    /**
     * PTN距離Y ｴﾝﾄﾞ5
     * @param endPtnDistY5 the endPtnDistY5 to set
     */
    public void setEndPtnDistY5(Long endPtnDistY5) {
        this.endPtnDistY5 = endPtnDistY5;
    }

}
