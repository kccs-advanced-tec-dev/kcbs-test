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
 * 変更日        2019/9/18<br>
 * 計画書No      K1811-DS001<br>
 * 変更者        KCSS K.Jo<br>
 * 変更理由      項目追加・変更<br>
 * <br>
 * 変更日	2022/06/02<br>
 * 計画書No	MB2205-D010<br>
 * 変更者	KCSS wxf<br>
 * 変更理由	項目追加・変更<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 印刷・SPSグラビア履歴検索画面のモデルクラスです。
 *
 * @author KCCS D.Yanagida
 * @since 2019/01/27
 */
public class GXHDO201B001Model implements Serializable {
    /** ﾛｯﾄNo. */
    private String lotno = "";
    /** KCPNO */
    private String kcpno = "";
    /** ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo */
    private String tapelotno = "";
    /** PETﾌｨﾙﾑ種類 */
    private String petfilmsyurui = "";
    /** ﾃｰﾌﾟﾛｰﾙNo1 */
    private String taperollno1 = "";
    /** ﾃｰﾌﾟﾛｰﾙNo2 */
    private String taperollno2 = "";
    /** ﾃｰﾌﾟﾛｰﾙNo3 */
    private String taperollno3 = "";
    /** ﾍﾟｰｽﾄﾛｯﾄNo1 */
    private String pastelotno = "";
    /** ﾍﾟｰｽﾄ粘度1 */
    private BigDecimal pastenendo = null;
    /** ﾍﾟｰｽﾄ温度1 */
    private BigDecimal pasteondo = null;
    /** ﾍﾟｰｽﾄ固形分1 */
    private BigDecimal pkokeibun1 = null;
    /** ﾍﾟｰｽﾄﾛｯﾄNo2 */
    private String pastelotno2 = "";
    /** ﾍﾟｰｽﾄ粘度2 */
    private BigDecimal pastenendo2 = null;
    /** ﾍﾟｰｽﾄ温度2 */
    private BigDecimal pasteondo2 = null;
    /** ﾍﾟｰｽﾄ固形分2 */
    private BigDecimal pkokeibun2 = null;
    /** 版胴名 */
    private String handoumei = "";
    /** 版胴No */
    private String handouno = "";
    /** 版胴使用枚数 */
    private Long handoumaisuu = null;
    /** ﾌﾞﾚｰﾄﾞNo. */
    private String bladeno = "";
    /** ﾌﾞﾚｰﾄﾞ外観 */
    private String bladegaikan = "";
    /** ﾌﾞﾚｰﾄﾞ圧力 */
    private BigDecimal bladeatu = null;
    /** 圧胴No */
    private String atudono = "";
    /** 圧胴使用枚数 */
    private Long atudomaisuu = null;
    /** 圧胴圧力 */
    private BigDecimal atudoatu = null;
    /** 号機ｺｰﾄﾞ */
    private String gouki = "";
    /** 乾燥温度 */
    private BigDecimal kansouondo = null;
    /** 乾燥温度2 */
    private BigDecimal kansouondo2 = null;
    /** 乾燥温度3 */
    private BigDecimal kansouondo3 = null;
    /** 乾燥温度4 */
    private BigDecimal kansouondo4 = null;
    /** 乾燥温度下側 */
    private BigDecimal kansouondoshita = null;
    /** 乾燥温度下側2 */
    private BigDecimal kansouondoshita2 = null;
    /** 乾燥温度下側3 */
    private BigDecimal kansouondoshita3 = null;
    /** 乾燥温度下側4 */
    private BigDecimal kansouondoshita4 = null;
    /** 乾燥温度5 */
    private BigDecimal kansouondo5 = null;
    /** 搬送速度 */
    private Long hansouspeed = null;
    /** ﾌﾟﾘﾝﾄ開始日時 */
    private Timestamp startdatetime = null;
    /** ｾｯﾃｨﾝｸﾞ担当者ｺｰﾄﾞ */
    private String tantousya = "";
    /** 印刷ｽﾀｰﾄ時確認者 */
    private String kakuninsya = "";
    /** ｽﾀｰﾄ時膜厚AVE */
    private BigDecimal makuatuaveStart = null;
    /** ｽﾀｰﾄ時膜厚MAX */
    private BigDecimal makuatumaxStart = null;
    /** ｽﾀｰﾄ時膜厚MIN */
    private BigDecimal makuatuminStart = null;
    /** 印刷ｽﾀｰﾄ膜厚CV */
    private BigDecimal makuatucvStart = null;
    /** ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認 */
    private String nijimikasureStart = "";
    /** ｽﾀｰﾄ時PTN間距離X */
    private Long startPtnDistX = null;
    /** ｽﾀｰﾄ時PTN間距離Y */
    private Long startPtnDistY = null;
    /** 開始ﾃﾝｼｮﾝ計 */
    private BigDecimal tensionsSum = null;
    /** ﾃﾝｼｮﾝ開始手前 */
    private BigDecimal tensionstemae = null;
    /** ﾃﾝｼｮﾝ開始奥 */
    private BigDecimal tensionsoku = null;
    /** ﾌﾟﾘﾝﾄ終了日時 */
    private Timestamp enddatetime = null;
    /** 終了時担当者ｺｰﾄﾞ */
    private String tantoEnd = "";
    /** 印刷枚数 */
    private Long printmaisuu = null;
    /** 終了時膜厚AVE */
    private BigDecimal makuatuaveEnd = null;
    /** 終了時膜厚MAX */
    private BigDecimal makuatumaxEnd = null;
    /** 終了時膜厚MIN */
    private BigDecimal makuatuminEnd = null;
    /** 印刷ｴﾝﾄﾞ膜厚CV */
    private BigDecimal makuatucvEnd = null;
    /** 終了時ﾆｼﾞﾐ・ｶｽﾚ確認 */
    private String nijimikasureEnd = "";
    /** 終了時PTN間距離X */
    private Long endPtnDistX = null;
    /** 終了時PTN間距離Y */
    private Long endPtnDistY = null;
    /** 終了ﾃﾝｼｮﾝ計 */
    private BigDecimal tensioneSum = null;
    /** ﾃﾝｼｮﾝ終了手前 */
    private BigDecimal tensionetemae = null;
    /** ﾃﾝｼｮﾝ終了奥 */
    private BigDecimal tensioneoku = null;
    /** 印刷ズレ①刷り始め開始 */
    private Long printzure1SurihajimeStart = null;
    /** 印刷ズレ②中央開始 */
    private Long printzure2CenterStart = null;
    /** 印刷ズレ③刷り終わり開始 */
    private Long printzure3SuriowariStart = null;
    /** ABズレ平均スタート */
    private Long abzureHeikinStart = null;
    /** 印刷ズレ①刷り始め終了 */
    private Long printzure1SurihajimeEnd = null;
    /** 印刷ズレ②中央終了 */
    private Long printzure2CenterEnd = null;
    /** 印刷ズレ③刷り終わり終了 */
    private Long printzure3SuriowariEnd = null;
    /** ABズレ平均終了 */
    private Long abzureHeikinEnd = null;
    /** 原料記号 */
    private String genryoukigou = "";
    /** ﾌﾞﾚｰﾄﾞ印刷長 */
    private Long bladeinsatsutyo = null;
    /** 清掃 ローラ部 */
    private String seisourollerbu = "";
    /** 清掃 印刷周辺 */
    private String seisouinsatsusyuhen = "";
    /** 清掃 乾燥炉内 */
    private String seisoukansouronai = "";
    /** 3μｍフィルター 適用 */
    private String sanmicronmfiltertekiyou = "";
    /** 3μｍフィルター 交換 */
    private String sanmicronmfilterkoukan = "";
    /** インクパンストッパーロック確認 */
    private String inkpanstopperlockkakunin = "";
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
     * 版胴名
     * @return the handoumei
     */
    public String getHandoumei() {
        return handoumei;
    }

    /**
     * 版胴名
     * @param handoumei the handoumei to set
     */
    public void setHandoumei(String handoumei) {
        this.handoumei = handoumei;
    }

    /**
     * 版胴No
     * @return the handouno
     */
    public String getHandouno() {
        return handouno;
    }

    /**
     * 版胴No
     * @param handouno the handouno to set
     */
    public void setHandouno(String handouno) {
        this.handouno = handouno;
    }

    /**
     * 版胴使用枚数
     * @return the handoumaisuu
     */
    public Long getHandoumaisuu() {
        return handoumaisuu;
    }

    /**
     * 版胴使用枚数
     * @param handoumaisuu the handoumaisuu to set
     */
    public void setHandoumaisuu(Long handoumaisuu) {
        this.handoumaisuu = handoumaisuu;
    }

    /**
     * ﾌﾞﾚｰﾄﾞNo.
     * @return the bladeno
     */
    public String getBladeno() {
        return bladeno;
    }

    /**
     * ﾌﾞﾚｰﾄﾞNo.
     * @param bladeno the bladeno to set
     */
    public void setBladeno(String bladeno) {
        this.bladeno = bladeno;
    }

    /**
     * ﾌﾞﾚｰﾄﾞ外観
     * @return the bladegaikan
     */
    public String getBladegaikan() {
        return bladegaikan;
    }

    /**
     * ﾌﾞﾚｰﾄﾞ外観
     * @param bladegaikan the bladegaikan to set
     */
    public void setBladegaikan(String bladegaikan) {
        this.bladegaikan = bladegaikan;
    }

    /**
     * ﾌﾞﾚｰﾄﾞ圧力
     * @return the bladeatu
     */
    public BigDecimal getBladeatu() {
        return bladeatu;
    }

    /**
     * ﾌﾞﾚｰﾄﾞ圧力
     * @param bladeatu the bladeatu to set
     */
    public void setBladeatu(BigDecimal bladeatu) {
        this.bladeatu = bladeatu;
    }

    /**
     * 圧胴No
     * @return the atudono
     */
    public String getAtudono() {
        return atudono;
    }

    /**
     * 圧胴No
     * @param atudono the atudono to set
     */
    public void setAtudono(String atudono) {
        this.atudono = atudono;
    }

    /**
     * 圧胴使用枚数
     * @return the atudomaisuu
     */
    public Long getAtudomaisuu() {
        return atudomaisuu;
    }

    /**
     * 圧胴使用枚数
     * @param atudomaisuu the atudomaisuu to set
     */
    public void setAtudomaisuu(Long atudomaisuu) {
        this.atudomaisuu = atudomaisuu;
    }

    /**
     * 圧胴圧力
     * @return the atudoatu
     */
    public BigDecimal getAtudoatu() {
        return atudoatu;
    }

    /**
     * 圧胴圧力
     * @param atudoatu the atudoatu to set
     */
    public void setAtudoatu(BigDecimal atudoatu) {
        this.atudoatu = atudoatu;
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
     * 乾燥温度下側
     * @return the kansouondoshita
     */
    public BigDecimal getKansouondoshita() {
        return kansouondoshita;
    }

    /**
     * 乾燥温度下側
     * @param kansouondoshita the kansouondoshita to set
     */
    public void setKansouondoshita(BigDecimal kansouondoshita) {
        this.kansouondoshita = kansouondoshita;
    }

    /**
     * 乾燥温度下側2
     * @return the kansouondoshita2
     */
    public BigDecimal getKansouondoshita2() {
        return kansouondoshita2;
    }

    /**
     * 乾燥温度下側2
     * @param kansouondoshita2 the kansouondoshita2 to set
     */
    public void setKansouondoshita2(BigDecimal kansouondoshita2) {
        this.kansouondoshita2 = kansouondoshita2;
    }

    /**
     * 乾燥温度下側3
     * @return the kansouondoshita3
     */
    public BigDecimal getKansouondoshita3() {
        return kansouondoshita3;
    }

    /**
     * 乾燥温度下側3
     * @param kansouondoshita3 the kansouondoshita3 to set
     */
    public void setKansouondoshita3(BigDecimal kansouondoshita3) {
        this.kansouondoshita3 = kansouondoshita3;
    }

    /**
     * 乾燥温度下側4
     * @return the kansouondoshita4
     */
    public BigDecimal getKansouondoshita4() {
        return kansouondoshita4;
    }

    /**
     * 乾燥温度下側4
     * @param kansouondoshita4 the kansouondoshita4 to set
     */
    public void setKansouondoshita4(BigDecimal kansouondoshita4) {
        this.kansouondoshita4 = kansouondoshita4;
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
     * 搬送速度
     * @return the hansouspeed
     */
    public Long getHansouspeed() {
        return hansouspeed;
    }

    /**
     * 搬送速度
     * @param hansouspeed the hansouspeed to set
     */
    public void setHansouspeed(Long hansouspeed) {
        this.hansouspeed = hansouspeed;
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
     * ｾｯﾃｨﾝｸﾞ担当者ｺｰﾄﾞ
     * @return the tantousya
     */
    public String getTantousya() {
        return tantousya;
    }

    /**
     * ｾｯﾃｨﾝｸﾞ担当者ｺｰﾄﾞ
     * @param tantousya the tantousya to set
     */
    public void setTantousya(String tantousya) {
        this.tantousya = tantousya;
    }

    /**
     * 印刷ｽﾀｰﾄ時確認者
     * @return the kakuninsya
     */
    public String getKakuninsya() {
        return kakuninsya;
    }

    /**
     * 印刷ｽﾀｰﾄ時確認者
     * @param kakuninsya the kakuninsya to set
     */
    public void setKakuninsya(String kakuninsya) {
        this.kakuninsya = kakuninsya;
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
     * 開始ﾃﾝｼｮﾝ計
     * @return the tensionsSum
     */
    public BigDecimal getTensionsSum() {
        return tensionsSum;
    }

    /**
     * 開始ﾃﾝｼｮﾝ計
     * @param tensionsSum the tensionsSum to set
     */
    public void setTensionsSum(BigDecimal tensionsSum) {
        this.tensionsSum = tensionsSum;
    }

    /**
     * ﾃﾝｼｮﾝ開始手前
     * @return the tensionstemae
     */
    public BigDecimal getTensionstemae() {
        return tensionstemae;
    }

    /**
     * ﾃﾝｼｮﾝ開始手前
     * @param tensionstemae the tensionstemae to set
     */
    public void setTensionstemae(BigDecimal tensionstemae) {
        this.tensionstemae = tensionstemae;
    }

    /**
     * ﾃﾝｼｮﾝ開始奥
     * @return the tensionsoku
     */
    public BigDecimal getTensionsoku() {
        return tensionsoku;
    }

    /**
     * ﾃﾝｼｮﾝ開始奥
     * @param tensionsoku the tensionsoku to set
     */
    public void setTensionsoku(BigDecimal tensionsoku) {
        this.tensionsoku = tensionsoku;
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
     * 終了ﾃﾝｼｮﾝ計
     * @return the tensioneSum
     */
    public BigDecimal getTensioneSum() {
        return tensioneSum;
    }

    /**
     * 終了ﾃﾝｼｮﾝ計
     * @param tensioneSum the tensioneSum to set
     */
    public void setTensioneSum(BigDecimal tensioneSum) {
        this.tensioneSum = tensioneSum;
    }

    /**
     * ﾃﾝｼｮﾝ終了手前
     * @return the tensionetemae
     */
    public BigDecimal getTensionetemae() {
        return tensionetemae;
    }

    /**
     * ﾃﾝｼｮﾝ終了手前
     * @param tensionetemae the tensionetemae to set
     */
    public void setTensionetemae(BigDecimal tensionetemae) {
        this.tensionetemae = tensionetemae;
    }

    /**
     * ﾃﾝｼｮﾝ終了奥
     * @return the tensioneoku
     */
    public BigDecimal getTensioneoku() {
        return tensioneoku;
    }

    /**
     * ﾃﾝｼｮﾝ終了奥
     * @param tensioneoku the tensioneoku to set
     */
    public void setTensioneoku(BigDecimal tensioneoku) {
        this.tensioneoku = tensioneoku;
    }

    /**
     * 印刷ズレ①刷り始め開始
     * @return the printzure1SurihajimeStart
     */
    public Long getPrintzure1SurihajimeStart() {
        return printzure1SurihajimeStart;
    }

    /**
     * 印刷ズレ①刷り始め開始
     * @param printzure1SurihajimeStart the printzure1SurihajimeStart to set
     */
    public void setPrintzure1SurihajimeStart(Long printzure1SurihajimeStart) {
        this.printzure1SurihajimeStart = printzure1SurihajimeStart;
    }

    /**
     * 印刷ズレ②中央開始
     * @return the printzure2CenterStart
     */
    public Long getPrintzure2CenterStart() {
        return printzure2CenterStart;
    }

    /**
     * 印刷ズレ②中央開始
     * @param printzure2CenterStart the printzure2CenterStart to set
     */
    public void setPrintzure2CenterStart(Long printzure2CenterStart) {
        this.printzure2CenterStart = printzure2CenterStart;
    }

    /**
     * 印刷ズレ③刷り終わり開始
     * @return the printzure3SuriowariStart
     */
    public Long getPrintzure3SuriowariStart() {
        return printzure3SuriowariStart;
    }

    /**
     * 印刷ズレ③刷り終わり開始
     * @param printzure3SuriowariStart the printzure3SuriowariStart to set
     */
    public void setPrintzure3SuriowariStart(Long printzure3SuriowariStart) {
        this.printzure3SuriowariStart = printzure3SuriowariStart;
    }

    /**
     * ABズレ平均スタート
     * @return the abzureHeikinStart
     */
    public Long getAbzureHeikinStart() {
        return abzureHeikinStart;
    }

    /**
     * ABズレ平均スタート
     * @param abzureHeikinStart the abzureHeikinStart to set
     */
    public void setAbzureHeikinStart(Long abzureHeikinStart) {
        this.abzureHeikinStart = abzureHeikinStart;
    }

    /**
     * 印刷ズレ①刷り始め終了
     * @return the printzure1SurihajimeEnd
     */
    public Long getPrintzure1SurihajimeEnd() {
        return printzure1SurihajimeEnd;
    }

    /**
     * 印刷ズレ①刷り始め終了
     * @param printzure1SurihajimeEnd the printzure1SurihajimeEnd to set
     */
    public void setPrintzure1SurihajimeEnd(Long printzure1SurihajimeEnd) {
        this.printzure1SurihajimeEnd = printzure1SurihajimeEnd;
    }

    /**
     * 印刷ズレ②中央終了
     * @return the printzure2CenterEnd
     */
    public Long getPrintzure2CenterEnd() {
        return printzure2CenterEnd;
    }

    /**
     * 印刷ズレ②中央終了
     * @param printzure2CenterEnd the printzure2CenterEnd to set
     */
    public void setPrintzure2CenterEnd(Long printzure2CenterEnd) {
        this.printzure2CenterEnd = printzure2CenterEnd;
    }

    /**
     * 印刷ズレ③刷り終わり終了
     * @return the printzure3SuriowariEnd
     */
    public Long getPrintzure3SuriowariEnd() {
        return printzure3SuriowariEnd;
    }

    /**
     * 印刷ズレ③刷り終わり終了
     * @param printzure3SuriowariEnd the printzure3SuriowariEnd to set
     */
    public void setPrintzure3SuriowariEnd(Long printzure3SuriowariEnd) {
        this.printzure3SuriowariEnd = printzure3SuriowariEnd;
    }

    /**
     * ABズレ平均終了
     * @return the abzureHeikinEnd
     */
    public Long getAbzureHeikinEnd() {
        return abzureHeikinEnd;
    }

    /**
     * ABズレ平均終了
     * @param abzureHeikinEnd the abzureHeikinEnd to set
     */
    public void setAbzureHeikinEnd(Long abzureHeikinEnd) {
        this.abzureHeikinEnd = abzureHeikinEnd;
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
     * ﾌﾞﾚｰﾄﾞ印刷長
     * @return the bladeinsatsutyo
     */
    public Long getBladeinsatsutyo() {
        return bladeinsatsutyo;
    }

    /**
     * ﾌﾞﾚｰﾄﾞ印刷長
     * @param bladeinsatsutyo the bladeinsatsutyo to set
     */
    public void setBladeinsatsutyo(Long bladeinsatsutyo) {
        this.bladeinsatsutyo = bladeinsatsutyo;
    }

    /**
     * 清掃 ローラ部
     * @return the seisourollerbu
     */
    public String getSeisourollerbu() {
        return seisourollerbu;
    }

    /**
     * 清掃 ローラ部
     * @param seisourollerbu the seisourollerbu to set
     */
    public void setSeisourollerbu(String seisourollerbu) {
        this.seisourollerbu = seisourollerbu;
    }

    /**
     * 清掃 印刷周辺
     * @return the seisouinsatsusyuhen
     */
    public String getSeisouinsatsusyuhen() {
        return seisouinsatsusyuhen;
    }

    /**
     * 清掃 印刷周辺
     * @param seisouinsatsusyuhen the seisouinsatsusyuhen to set
     */
    public void setSeisouinsatsusyuhen(String seisouinsatsusyuhen) {
        this.seisouinsatsusyuhen = seisouinsatsusyuhen;
    }

    /**
     * 清掃 乾燥炉内
     * @return the seisoukansouronai
     */
    public String getSeisoukansouronai() {
        return seisoukansouronai;
    }

    /**
     * 清掃 乾燥炉内
     * @param seisoukansouronai the seisoukansouronai to set
     */
    public void setSeisoukansouronai(String seisoukansouronai) {
        this.seisoukansouronai = seisoukansouronai;
    }

    /**
     * 3μｍフィルター 適用
     * @return the sanmicronmfiltertekiyou
     */
    public String getSanmicronmfiltertekiyou() {
        return sanmicronmfiltertekiyou;
    }

    /**
     * 3μｍフィルター 適用
     * @param sanmicronmfiltertekiyou the sanmicronmfiltertekiyou to set
     */
    public void setSanmicronmfiltertekiyou(String sanmicronmfiltertekiyou) {
        this.sanmicronmfiltertekiyou = sanmicronmfiltertekiyou;
    }

    /**
     * 3μｍフィルター 交換
     * @return the sanmicronmfilterkoukan
     */
    public String getSanmicronmfilterkoukan() {
        return sanmicronmfilterkoukan;
    }

    /**
     * 3μｍフィルター 交換
     * @param sanmicronmfilterkoukan the sanmicronmfilterkoukan to set
     */
    public void setSanmicronmfilterkoukan(String sanmicronmfilterkoukan) {
        this.sanmicronmfilterkoukan = sanmicronmfilterkoukan;
    }

    /**
     * インクパンストッパーロック確認
     * @return the inkpanstopperlockkakunin
     */
    public String getInkpanstopperlockkakunin() {
        return inkpanstopperlockkakunin;
    }

    /**
     * インクパンストッパーロック確認
     * @param inkpanstopperlockkakunin the inkpanstopperlockkakunin to set
     */
    public void setInkpanstopperlockkakunin(String inkpanstopperlockkakunin) {
        this.inkpanstopperlockkakunin = inkpanstopperlockkakunin;
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
