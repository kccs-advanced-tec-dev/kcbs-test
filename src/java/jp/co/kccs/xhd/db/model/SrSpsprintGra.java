/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/01/07<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * 変更日	2022/06/02<br>
 * 計画書No	MB2205-D010<br>
 * 変更者	KCSS wxf<br>
 * 変更理由	項目追加・変更<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 印刷SPSｸﾞﾗﾋﾞｱのモデルクラスです。
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2019/01/07
 */
public class SrSpsprintGra {

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
     * ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
     */
    private String tapelotno;
    
    /**
     * PETﾌｨﾙﾑ種類
     */
    private String petfilmsyurui;
    
    /**
     * ﾃｰﾌﾟﾛｰﾙNo1
     */
    private String taperollno1;
    
    /**
     * ﾃｰﾌﾟﾛｰﾙNo2
     */
    private String taperollno2;
    
    /**
     * ﾃｰﾌﾟﾛｰﾙNo3
     */
    private String taperollno3;
    
    /**
     * ﾍﾟｰｽﾄﾛｯﾄNo1
     */
    private String pastelotno;
    
    /**
     * ﾍﾟｰｽﾄ粘度1
     */
    private BigDecimal pastenendo;
    
    /**
     * ﾍﾟｰｽﾄ温度1
     */
    private BigDecimal pasteondo;
    
    /**
     * ﾍﾟｰｽﾄ固形分1
     */
    private BigDecimal pkokeibun1;
    
    /**
     * ﾍﾟｰｽﾄﾛｯﾄNo2
     */
    private String pastelotno2;
    
    /**
     * ﾍﾟｰｽﾄ粘度2
     */
    private BigDecimal pastenendo2;
    
    /**
     * ﾍﾟｰｽﾄ温度2
     */
    private BigDecimal pasteondo2;
    
    /**
     * ﾍﾟｰｽﾄ固形分2
     */
    private BigDecimal pkokeibun2;
    
    /**
     * 版胴名
     */
    private String handoumei;
    
    /**
     * 版胴No
     */
    private String handouno;
    
    /**
     * 版胴使用枚数
     */
    private Long handoumaisuu;
    
    /**
     * ﾌﾞﾚｰﾄﾞNo.
     */
    private String bladeno;
    
    /**
     * ﾌﾞﾚｰﾄﾞ外観
     */
    private Integer bladegaikan;
    
    /**
     * ﾌﾞﾚｰﾄﾞ圧力
     */
    private BigDecimal bladeATu;
    
    /**
     * ﾌﾞﾚｰﾄﾞ印刷長
     */
    private Integer bladeinsatsutyo;
    
    /**
     * 圧胴No
     */
    private String atudoNo;
    
    /**
     * 圧胴使用枚数
     */
    private Long atudoMaisuu;
    
    /**
     * 圧胴圧力
     */
    private BigDecimal atuDoATu;
    
    /**
     * 号機ｺｰﾄﾞ
     */
    private String gouki;
    
    /**
     * 乾燥温度
     */
    private BigDecimal kansouondo;
    
    /**
     * 乾燥温度2
     */
    private BigDecimal kansouondo2;
    
    /**
     * 乾燥温度3
     */
    private BigDecimal kansouondo3;
    
    /**
     * 乾燥温度4
     */
    private BigDecimal kansouondo4;
    
    /**
     * 乾燥温度下側
     */
    private BigDecimal kansouondoshita;
    
    /**
     * 乾燥温度下側2
     */
    private BigDecimal kansouondoshita2;
    
    /**
     * 乾燥温度下側3
     */
    private BigDecimal kansouondoshita3;
    
    /**
     * 乾燥温度下側4
     */
    private BigDecimal kansouondoshita4;
    
    /**
     * 乾燥温度5
     */
    private BigDecimal kansouondo5;
    
    /**
     * 搬送速度
     */
    private Integer hansouspeed;
    
    /**
     * ﾌﾟﾘﾝﾄ開始日時
     */
    private Timestamp startdatetime;
    
    /**
     * ｾｯﾃｨﾝｸﾞ担当者ｺｰﾄﾞ
     */
    private String tantousya;
    
    /**
     * 印刷スタート時確認者
     */
    private String kakuninsya;
    
    /**
     * 清掃 ローラ部
     */
    private Integer seisourollerbu;
    
    /**
     * 清掃 印刷周辺
     */
    private Integer seisouinsatsusyuhen;
    
    /**
     * 清掃 乾燥炉内
     */
    private Integer seisoukansouronai;
    
    /**
     * 3μｍフィルター 適用
     */
    private Integer sanmicronmfiltertekiyou;
    
    /**
     * 3μｍフィルター 交換
     */
    private Integer sanmicronmfilterkoukan;
    
    /**
     * インクパンストッパーロック確認
     */
    private Integer inkpanstopperlockkakunin;
    
    /**
     * 先行工場ｺｰﾄﾞ
     */
    private String skojyo;
    
    /**
     * 先行ﾛｯﾄNo
     */
    private String slotno;
    
    /**
     * 先行枝番
     */
    private String sedaban;
    
    /**
     * 先行ﾛｯﾄNo
     */
    private String senkoulotno;
    
    /**
     * ﾃｰﾌﾟ使い切り
     */
    private Integer tapetsukaikiri;
    
    /**
     * 次ﾛｯﾄへ
     */
    private Integer jilothe;
    
    /**
     * 成形長さ
     */
    private Integer seikeinagasa;  
    
    /**
     * 備考3
     */
    private String bikou3;
    
    /**
     * 備考4
     */
    private String bikou4;
    
    /**
     * 備考5
     */
    private String bikou5;
    
    /**
     * ｽﾀｰﾄ時膜厚AVE
     */
    private BigDecimal makuatuaveStart;
    
    /**
     * ｽﾀｰﾄ時膜厚MAX
     */
    private BigDecimal makuatumaxStart;
    
    /**
     * ｽﾀｰﾄ時膜厚MIN
     */
    private BigDecimal makuatuminStart;
    
    /**
     * 印刷ｽﾀｰﾄ膜厚CV
     */
    private BigDecimal makuatucvStart;
    
    /**
     * ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
     */
    private Integer nijimikasureStart;
    
    /**
     * ｽﾀｰﾄ時PTN間距離X
     */
    private Integer startPtnDistX;
    
    /**
     * ｽﾀｰﾄ時PTN間距離Y
     */
    private Integer startPtnDistY;
    
    /**
     * 開始ﾃﾝｼｮﾝ計
     */
    private BigDecimal tensionSSum;
    
    /**
     * ﾃﾝｼｮﾝ開始手前
     */
    private BigDecimal tensionStemae;
    
    /**
     * ﾃﾝｼｮﾝ開始奥
     */
    private BigDecimal tensionSoku;
    
    /**
     * ﾌﾟﾘﾝﾄ終了日時
     */
    private Timestamp enddatetime;
    
    /**
     * 終了時担当者ｺｰﾄﾞ
     */
    private String tantoEnd;
    
    /**
     * 印刷枚数
     */
    private Integer printmaisuu;
    
    /**
     * 終了時膜厚AVE
     */
    private BigDecimal makuatuaveEnd;
    
    /**
     * 終了時膜厚MAX
     */
    private BigDecimal makuatumaxEnd;
    
    /**
     * 終了時膜厚MIN
     */
    private BigDecimal makuatuminEnd;
    
    /**
     * 印刷ｴﾝﾄﾞ膜厚CV
     */
    private BigDecimal makuatucvEnd;
    
    /**
     * 終了時ﾆｼﾞﾐ・ｶｽﾚ確認
     */
    private Integer nijimikasureEnd;
    
    /**
     * 終了時PTN間距離X
     */
    private Integer endPtnDistX;
    
    /**
     * 終了時PTN間距離Y
     */
    private Integer endPtnDistY;
    
    /**
     * 終了ﾃﾝｼｮﾝ計
     */
    private BigDecimal tensionESum;
    
    /**
     * ﾃﾝｼｮﾝ終了手前
     */
    private BigDecimal tensionEtemae;
    
    /**
     * ﾃﾝｼｮﾝ終了奥
     */
    private BigDecimal tensionEoku;
    
    /**
     * 印刷ズレ①刷り始め開始
     */
    private Integer printzure1SurihajimeStart;
    
    /**
     * 印刷ズレ②中央開始
     */
    private Integer printzure2CenterStart;
    
    /**
     * 印刷ズレ③刷り終わり開始
     */
    private Integer printzure3SuriowariStart;
    
    /**
     * ABズレ平均スタート
     */
    private Integer abzureHeikinStart;
    
    /**
     * 印刷ズレ①刷り始め終了
     */
    private Integer printzure1SurihajimeEnd;
    
    /**
     * 印刷ズレ②中央終了
     */
    private Integer printzure2CenterEnd;
    
    /**
     * 印刷ズレ③刷り終わり終了
     */
    private Integer printzure3SuriowariEnd;
    
    /**
     * ABズレ平均終了
     */
    private Integer abzureHeikinEnd;
    
    /**
     * 原料記号
     */
    private String genryoukigou;
    
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
    private Long revision;
    
    /**
     * KCPNO
     */
    private String kcpno;

    /**
     * 削除ﾌﾗｸﾞ
     */
    private Integer deleteflag;

    /**
     * 印刷長さ
     */
    private Integer printlength;

    /**
     * 工場ｺｰﾄﾞ
     *
     * @return the kojyo
     */
    public String getKojyo() {
        return kojyo;
    }

    /**
     * 工場ｺｰﾄﾞ
     *
     * @param kojyo the kojyo to set
     */
    public void setKojyo(String kojyo) {
        this.kojyo = kojyo;
    }

    /**
     * ロットNo
     *
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * ロットNo
     *
     * @param lotno the lotno to set
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * 枝番
     *
     * @return the edaban
     */
    public String getEdaban() {
        return edaban;
    }

    /**
     * 枝番
     *
     * @param edaban the edaban to set
     */
    public void setEdaban(String edaban) {
        this.edaban = edaban;
    }

    /**
     * ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
     *
     * @return the tapelotno
     */
    public String getTapelotno() {
        return tapelotno;
    }

    /**
     * ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
     *
     * @param tapelotno the tapelotno to set
     */
    public void setTapelotno(String tapelotno) {
        this.tapelotno = tapelotno;
    }

    /**
     * PETﾌｨﾙﾑ種類
     *
     * @return the petfilmsyurui
     */
    public String getPetfilmsyurui() {
        return petfilmsyurui;
    }

    /**
     * PETﾌｨﾙﾑ種類
     *
     * @param petfilmsyurui the petfilmsyurui to set
     */
    public void setPetfilmsyurui(String petfilmsyurui) {
        this.petfilmsyurui = petfilmsyurui;
    }

    /**
     * ﾃｰﾌﾟﾛｰﾙNo1
     *
     * @return the taperollno1
     */
    public String getTaperollno1() {
        return taperollno1;
    }

    /**
     * ﾃｰﾌﾟﾛｰﾙNo1
     *
     * @param taperollno1 the taperollno1 to set
     */
    public void setTaperollno1(String taperollno1) {
        this.taperollno1 = taperollno1;
    }

    /**
     * ﾃｰﾌﾟﾛｰﾙNo2
     *
     * @return the taperollno2
     */
    public String getTaperollno2() {
        return taperollno2;
    }

    /**
     * ﾃｰﾌﾟﾛｰﾙNo2
     *
     * @param taperollno2 the taperollno2 to set
     */
    public void setTaperollno2(String taperollno2) {
        this.taperollno2 = taperollno2;
    }

    /**
     * ﾃｰﾌﾟﾛｰﾙNo3
     *
     * @return the taperollno3
     */
    public String getTaperollno3() {
        return taperollno3;
    }

    /**
     * ﾃｰﾌﾟﾛｰﾙNo3
     *
     * @param taperollno3 the taperollno3 to set
     */
    public void setTaperollno3(String taperollno3) {
        this.taperollno3 = taperollno3;
    }

    /**
     * ﾍﾟｰｽﾄﾛｯﾄNo1
     *
     * @return the pastelotno
     */
    public String getPastelotno() {
        return pastelotno;
    }

    /**
     * ﾍﾟｰｽﾄﾛｯﾄNo1
     *
     * @param pastelotno the pastelotno to set
     */
    public void setPastelotno(String pastelotno) {
        this.pastelotno = pastelotno;
    }

    /**
     * ﾍﾟｰｽﾄ粘度1
     *
     * @return the pastenendo
     */
    public BigDecimal getPastenendo() {
        return pastenendo;
    }

    /**
     * ﾍﾟｰｽﾄ粘度1
     *
     * @param pastenendo the pastenendo to set
     */
    public void setPastenendo(BigDecimal pastenendo) {
        this.pastenendo = pastenendo;
    }

    /**
     * ﾍﾟｰｽﾄ温度1
     *
     * @return the pasteondo
     */
    public BigDecimal getPasteondo() {
        return pasteondo;
    }

    /**
     * ﾍﾟｰｽﾄ温度1
     *
     * @param pasteondo the pasteondo to set
     */
    public void setPasteondo(BigDecimal pasteondo) {
        this.pasteondo = pasteondo;
    }

    /**
     * ﾍﾟｰｽﾄ固形分1
     *
     * @return the pkokeibun1
     */
    public BigDecimal getPkokeibun1() {
        return pkokeibun1;
    }

    /**
     * ﾍﾟｰｽﾄ固形分1
     *
     * @param pkokeibun1 the pkokeibun1 to set
     */
    public void setPkokeibun1(BigDecimal pkokeibun1) {
        this.pkokeibun1 = pkokeibun1;
    }

    /**
     * ﾍﾟｰｽﾄﾛｯﾄNo2
     *
     * @return the pastelotno2
     */
    public String getPastelotno2() {
        return pastelotno2;
    }

    /**
     * ﾍﾟｰｽﾄﾛｯﾄNo2
     *
     * @param pastelotno2 the pastelotno2 to set
     */
    public void setPastelotno2(String pastelotno2) {
        this.pastelotno2 = pastelotno2;
    }

    /**
     * ﾍﾟｰｽﾄ粘度2
     *
     * @return the pastenendo2
     */
    public BigDecimal getPastenendo2() {
        return pastenendo2;
    }

    /**
     * ﾍﾟｰｽﾄ粘度2
     *
     * @param pastenendo2 the pastenendo2 to set
     */
    public void setPastenendo2(BigDecimal pastenendo2) {
        this.pastenendo2 = pastenendo2;
    }

    /**
     * ﾍﾟｰｽﾄ温度2
     *
     * @return the pasteondo2
     */
    public BigDecimal getPasteondo2() {
        return pasteondo2;
    }

    /**
     * ﾍﾟｰｽﾄ温度2
     *
     * @param pasteondo2 the pasteondo2 to set
     */
    public void setPasteondo2(BigDecimal pasteondo2) {
        this.pasteondo2 = pasteondo2;
    }

    /**
     * ﾍﾟｰｽﾄ固形分2
     *
     * @return the pkokeibun2
     */
    public BigDecimal getPkokeibun2() {
        return pkokeibun2;
    }

    /**
     * ﾍﾟｰｽﾄ固形分2
     *
     * @param pkokeibun2 the pkokeibun2 to set
     */
    public void setPkokeibun2(BigDecimal pkokeibun2) {
        this.pkokeibun2 = pkokeibun2;
    }

    /**
     * 版胴名
     *
     * @return the handoumei
     */
    public String getHandoumei() {
        return handoumei;
    }

    /**
     * 版胴名
     *
     * @param handoumei the handoumei to set
     */
    public void setHandoumei(String handoumei) {
        this.handoumei = handoumei;
    }

    /**
     * 版胴No
     *
     * @return the handouno
     */
    public String getHandouno() {
        return handouno;
    }

    /**
     * 版胴No
     *
     * @param handouno the handouno to set
     */
    public void setHandouno(String handouno) {
        this.handouno = handouno;
    }

    /**
     * 版胴使用枚数
     *
     * @return the handoumaisuu
     */
    public Long getHandoumaisuu() {
        return handoumaisuu;
    }

    /**
     * 版胴使用枚数
     *
     * @param handoumaisuu the handoumaisuu to set
     */
    public void setHandoumaisuu(Long handoumaisuu) {
        this.handoumaisuu = handoumaisuu;
    }

    /**
     * ﾌﾞﾚｰﾄﾞNo.
     *
     * @return the bladeno
     */
    public String getBladeno() {
        return bladeno;
    }

    /**
     * ﾌﾞﾚｰﾄﾞNo.
     *
     * @param bladeno the bladeno to set
     */
    public void setBladeno(String bladeno) {
        this.bladeno = bladeno;
    }

    /**
     * ﾌﾞﾚｰﾄﾞ外観
     *
     * @return the bladegaikan
     */
    public Integer getBladegaikan() {
        return bladegaikan;
    }

    /**
     * ﾌﾞﾚｰﾄﾞ外観
     *
     * @param bladegaikan the bladegaikan to set
     */
    public void setBladegaikan(Integer bladegaikan) {
        this.bladegaikan = bladegaikan;
    }

    /**
     * ﾌﾞﾚｰﾄﾞ圧力
     *
     * @return the bladeATu
     */
    public BigDecimal getBladeATu() {
        return bladeATu;
    }

    /**
     * ﾌﾞﾚｰﾄﾞ圧力
     *
     * @param bladeATu the bladeATu to set
     */
    public void setBladeATu(BigDecimal bladeATu) {
        this.bladeATu = bladeATu;
    }

    /**
     * ﾌﾞﾚｰﾄﾞ印刷長
     * @return the bladeinsatsutyo
     */
    public Integer getBladeinsatsutyo() {
        return bladeinsatsutyo;
    }

    /**
     * ﾌﾞﾚｰﾄﾞ印刷長
     * @param bladeinsatsutyo the bladeinsatsutyo to set
     */
    public void setBladeinsatsutyo(Integer bladeinsatsutyo) {
        this.bladeinsatsutyo = bladeinsatsutyo;
    }

    /**
     * 圧胴No
     *
     * @return the atudoNo
     */
    public String getAtudoNo() {
        return atudoNo;
    }

    /**
     * 圧胴No
     *
     * @param atudoNo the atudoNo to set
     */
    public void setAtudoNo(String atudoNo) {
        this.atudoNo = atudoNo;
    }

    /**
     * 圧胴使用枚数
     *
     * @return the atudoMaisuu
     */
    public Long getAtudoMaisuu() {
        return atudoMaisuu;
    }

    /**
     * 圧胴使用枚数
     *
     * @param atudoMaisuu the atudoMaisuu to set
     */
    public void setAtudoMaisuu(Long atudoMaisuu) {
        this.atudoMaisuu = atudoMaisuu;
    }

    /**
     * 圧胴圧力
     *
     * @return the atuDoATu
     */
    public BigDecimal getAtuDoATu() {
        return atuDoATu;
    }

    /**
     * 圧胴圧力
     *
     * @param atuDoATu the atuDoATu to set
     */
    public void setAtuDoATu(BigDecimal atuDoATu) {
        this.atuDoATu = atuDoATu;
    }

    /**
     * 号機ｺｰﾄﾞ
     *
     * @return the gouki
     */
    public String getGouki() {
        return gouki;
    }

    /**
     * 号機ｺｰﾄﾞ
     *
     * @param gouki the gouki to set
     */
    public void setGouki(String gouki) {
        this.gouki = gouki;
    }

    /**
     * 乾燥温度
     *
     * @return the kansouondo
     */
    public BigDecimal getKansouondo() {
        return kansouondo;
    }

    /**
     * 乾燥温度
     *
     * @param kansouondo the kansouondo to set
     */
    public void setKansouondo(BigDecimal kansouondo) {
        this.kansouondo = kansouondo;
    }

    /**
     * 乾燥温度2
     *
     * @return the kansouondo2
     */
    public BigDecimal getKansouondo2() {
        return kansouondo2;
    }

    /**
     * 乾燥温度2
     *
     * @param kansouondo2 the kansouondo2 to set
     */
    public void setKansouondo2(BigDecimal kansouondo2) {
        this.kansouondo2 = kansouondo2;
    }

    /**
     * 乾燥温度3
     *
     * @return the kansouondo3
     */
    public BigDecimal getKansouondo3() {
        return kansouondo3;
    }

    /**
     * 乾燥温度3
     *
     * @param kansouondo3 the kansouondo3 to set
     */
    public void setKansouondo3(BigDecimal kansouondo3) {
        this.kansouondo3 = kansouondo3;
    }

    /**
     * @return the kansouondo4
     */
    public BigDecimal getKansouondo4() {
        return kansouondo4;
    }

    /**
     * 乾燥温度4
     *
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
     *
     * @return the kansouondo5
     */
    public BigDecimal getKansouondo5() {
        return kansouondo5;
    }

    /**
     * 乾燥温度5
     *
     * @param kansouondo5 the kansouondo5 to set
     */
    public void setKansouondo5(BigDecimal kansouondo5) {
        this.kansouondo5 = kansouondo5;
    }

    /**
     * 搬送速度
     *
     * @return the hansouspeed
     */
    public Integer getHansouspeed() {
        return hansouspeed;
    }

    /**
     * 搬送速度
     *
     * @param hansouspeed the hansouspeed to set
     */
    public void setHansouspeed(Integer hansouspeed) {
        this.hansouspeed = hansouspeed;
    }

    /**
     * ﾌﾟﾘﾝﾄ開始日時
     *
     * @return the startdatetime
     */
    public Timestamp getStartdatetime() {
        return startdatetime;
    }

    /**
     * ﾌﾟﾘﾝﾄ開始日時
     *
     * @param startdatetime the startdatetime to set
     */
    public void setStartdatetime(Timestamp startdatetime) {
        this.startdatetime = startdatetime;
    }

    /**
     * ｾｯﾃｨﾝｸﾞ担当者ｺｰﾄﾞ
     *
     * @return the tantousya
     */
    public String getTantousya() {
        return tantousya;
    }

    /**
     * ｾｯﾃｨﾝｸﾞ担当者ｺｰﾄﾞ
     *
     * @param tantousya the tantousya to set
     */
    public void setTantousya(String tantousya) {
        this.tantousya = tantousya;
    }

    /**
     * 印刷スタート時確認者
     *
     * @return the kakuninsya
     */
    public String getKakuninsya() {
        return kakuninsya;
    }

    /**
     * 印刷スタート時確認者
     *
     * @param kakuninsya the kakuninsya to set
     */
    public void setKakuninsya(String kakuninsya) {
        this.kakuninsya = kakuninsya;
    }

    /**
     * 清掃 ローラ部
     * @return the seisourollerbu
     */
    public Integer getSeisourollerbu() {
        return seisourollerbu;
    }

    /**
     * 清掃 ローラ部
     * @param seisourollerbu the seisourollerbu to set
     */
    public void setSeisourollerbu(Integer seisourollerbu) {
        this.seisourollerbu = seisourollerbu;
    }

    /**
     * 清掃 印刷周辺
     * @return the seisouinsatsusyuhen
     */
    public Integer getSeisouinsatsusyuhen() {
        return seisouinsatsusyuhen;
    }

    /**
     * 清掃 印刷周辺
     * @param seisouinsatsusyuhen the seisouinsatsusyuhen to set
     */
    public void setSeisouinsatsusyuhen(Integer seisouinsatsusyuhen) {
        this.seisouinsatsusyuhen = seisouinsatsusyuhen;
    }

    /**
     * 清掃 乾燥炉内
     * @return the seisoukansouronai
     */
    public Integer getSeisoukansouronai() {
        return seisoukansouronai;
    }

    /**
     * 清掃 乾燥炉内
     * @param seisoukansouronai the seisoukansouronai to set
     */
    public void setSeisoukansouronai(Integer seisoukansouronai) {
        this.seisoukansouronai = seisoukansouronai;
    }

    /**
     * 3μｍフィルター 適用
     * @return the sanmicronmfiltertekiyou
     */
    public Integer getSanmicronmfiltertekiyou() {
        return sanmicronmfiltertekiyou;
    }

    /**
     * 3μｍフィルター 適用
     * @param sanmicronmfiltertekiyou the sanmicronmfiltertekiyou to set
     */
    public void setSanmicronmfiltertekiyou(Integer sanmicronmfiltertekiyou) {
        this.sanmicronmfiltertekiyou = sanmicronmfiltertekiyou;
    }

    /**
     * 3μｍフィルター 交換
     * @return the sanmicronmfilterkoukan
     */
    public Integer getSanmicronmfilterkoukan() {
        return sanmicronmfilterkoukan;
    }

    /**
     * 3μｍフィルター 交換
     * @param sanmicronmfilterkoukan the sanmicronmfilterkoukan to set
     */
    public void setSanmicronmfilterkoukan(Integer sanmicronmfilterkoukan) {
        this.sanmicronmfilterkoukan = sanmicronmfilterkoukan;
    }

    /**
     * インクパンストッパーロック確認
     * @return the inkpanstopperlockkakunin
     */
    public Integer getInkpanstopperlockkakunin() {
        return inkpanstopperlockkakunin;
    }

    /**
     * インクパンストッパーロック確認
     * @param inkpanstopperlockkakunin the inkpanstopperlockkakunin to set
     */
    public void setInkpanstopperlockkakunin(Integer inkpanstopperlockkakunin) {
        this.inkpanstopperlockkakunin = inkpanstopperlockkakunin;
    }

    /**
     * 先行工場ｺｰﾄﾞ
     * @return the skojyo
     */
    public String getSkojyo() {
        return skojyo;
    }

    /**
     * 先行工場ｺｰﾄﾞ
     * @param skojyo the skojyo to set
     */
    public void setSkojyo(String skojyo) {
        this.skojyo = skojyo;
    }

    /**
     * 先行ﾛｯﾄNo
     * @return the slotno
     */
    public String getSlotno() {
        return slotno;
    }

    /**
     * 先行ﾛｯﾄNo
     * @param slotno the slotno to set
     */
    public void setSlotno(String slotno) {
        this.slotno = slotno;
    }

    /**
     * 先行枝番
     * @return the sedaban
     */
    public String getSedaban() {
        return sedaban;
    }

    /**
     * 先行枝番
     * @param sedaban the sedaban to set
     */
    public void setSedaban(String sedaban) {
        this.sedaban = sedaban;
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
    public Integer getTapetsukaikiri() {
        return tapetsukaikiri;
    }

    /**
     * ﾃｰﾌﾟ使い切り
     * @param tapetsukaikiri the tapetsukaikiri to set
     */
    public void setTapetsukaikiri(Integer tapetsukaikiri) {
        this.tapetsukaikiri = tapetsukaikiri;
    }

    /**
     * 次ﾛｯﾄへ
     * @return the jilothe
     */
    public Integer getJilothe() {
        return jilothe;
    }

    /**
     * 次ﾛｯﾄへ
     * @param jilothe the jilothe to set
     */
    public void setJilothe(Integer jilothe) {
        this.jilothe = jilothe;
    }

    /**
     * 成形長さ
     * @return the seikeinagasa
     */
    public Integer getSeikeinagasa() {
        return seikeinagasa;
    }

    /**
     * 成形長さ
     * @param seikeinagasa the seikeinagasa to set
     */
    public void setSeikeinagasa(Integer seikeinagasa) {
        this.seikeinagasa = seikeinagasa;
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
     * ｽﾀｰﾄ時膜厚AVE
     *
     * @return the makuatuaveStart
     */
    public BigDecimal getMakuatuaveStart() {
        return makuatuaveStart;
    }

    /**
     * ｽﾀｰﾄ時膜厚AVE
     *
     * @param makuatuaveStart the makuatuaveStart to set
     */
    public void setMakuatuaveStart(BigDecimal makuatuaveStart) {
        this.makuatuaveStart = makuatuaveStart;
    }

    /**
     * ｽﾀｰﾄ時膜厚MAX
     *
     * @return the makuatumaxStart
     */
    public BigDecimal getMakuatumaxStart() {
        return makuatumaxStart;
    }

    /**
     * ｽﾀｰﾄ時膜厚MAX
     *
     * @param makuatumaxStart the makuatumaxStart to set
     */
    public void setMakuatumaxStart(BigDecimal makuatumaxStart) {
        this.makuatumaxStart = makuatumaxStart;
    }

    /**
     * ｽﾀｰﾄ時膜厚MIN
     *
     * @return the makuatuminStart
     */
    public BigDecimal getMakuatuminStart() {
        return makuatuminStart;
    }

    /**
     * ｽﾀｰﾄ時膜厚MIN
     *
     * @param makuatuminStart the makuatuminStart to set
     */
    public void setMakuatuminStart(BigDecimal makuatuminStart) {
        this.makuatuminStart = makuatuminStart;
    }

    /**
     * 印刷ｽﾀｰﾄ膜厚CV
     *
     * @return the makuatucvStart
     */
    public BigDecimal getMakuatucvStart() {
        return makuatucvStart;
    }

    /**
     * 印刷ｽﾀｰﾄ膜厚CV
     *
     * @param makuatucvStart the makuatucvStart to set
     */
    public void setMakuatucvStart(BigDecimal makuatucvStart) {
        this.makuatucvStart = makuatucvStart;
    }

    /**
     * ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
     *
     * @return the nijimikasureStart
     */
    public Integer getNijimikasureStart() {
        return nijimikasureStart;
    }

    /**
     * ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
     *
     * @param nijimikasureStart the nijimikasureStart to set
     */
    public void setNijimikasureStart(Integer nijimikasureStart) {
        this.nijimikasureStart = nijimikasureStart;
    }

    /**
     * ｽﾀｰﾄ時PTN間距離X
     *
     * @return the startPtnDistX
     */
    public Integer getStartPtnDistX() {
        return startPtnDistX;
    }

    /**
     * ｽﾀｰﾄ時PTN間距離X
     *
     * @param startPtnDistX the startPtnDistX to set
     */
    public void setStartPtnDistX(Integer startPtnDistX) {
        this.startPtnDistX = startPtnDistX;
    }

    /**
     * ｽﾀｰﾄ時PTN間距離Y
     *
     * @return the startPtnDistY
     */
    public Integer getStartPtnDistY() {
        return startPtnDistY;
    }

    /**
     * ｽﾀｰﾄ時PTN間距離Y
     *
     * @param startPtnDistY the startPtnDistY to set
     */
    public void setStartPtnDistY(Integer startPtnDistY) {
        this.startPtnDistY = startPtnDistY;
    }

    /**
     * 開始ﾃﾝｼｮﾝ計
     *
     * @return the tensionSSum
     */
    public BigDecimal getTensionSSum() {
        return tensionSSum;
    }

    /**
     * 開始ﾃﾝｼｮﾝ計
     *
     * @param tensionSSum the tensionSSum to set
     */
    public void setTensionSSum(BigDecimal tensionSSum) {
        this.tensionSSum = tensionSSum;
    }

    /**
     * ﾃﾝｼｮﾝ開始手前
     *
     * @return the tensionStemae
     */
    public BigDecimal getTensionStemae() {
        return tensionStemae;
    }

    /**
     * ﾃﾝｼｮﾝ開始手前
     *
     * @param tensionStemae the tensionStemae to set
     */
    public void setTensionStemae(BigDecimal tensionStemae) {
        this.tensionStemae = tensionStemae;
    }

    /**
     * ﾃﾝｼｮﾝ開始奥
     *
     * @return the tensionSoku
     */
    public BigDecimal getTensionSoku() {
        return tensionSoku;
    }

    /**
     * ﾃﾝｼｮﾝ開始奥
     *
     * @param tensionSoku the tensionSoku to set
     */
    public void setTensionSoku(BigDecimal tensionSoku) {
        this.tensionSoku = tensionSoku;
    }

    /**
     * ﾌﾟﾘﾝﾄ終了日時
     *
     * @return the enddatetime
     */
    public Timestamp getEnddatetime() {
        return enddatetime;
    }

    /**
     * ﾌﾟﾘﾝﾄ終了日時
     *
     * @param enddatetime the enddatetime to set
     */
    public void setEnddatetime(Timestamp enddatetime) {
        this.enddatetime = enddatetime;
    }

    /**
     * 終了時担当者ｺｰﾄﾞ
     *
     * @return the tantoEnd
     */
    public String getTantoEnd() {
        return tantoEnd;
    }

    /**
     * 終了時担当者ｺｰﾄﾞ
     *
     * @param tantoEnd the tantoEnd to set
     */
    public void setTantoEnd(String tantoEnd) {
        this.tantoEnd = tantoEnd;
    }

    /**
     * 印刷枚数
     *
     * @return the printmaisuu
     */
    public Integer getPrintmaisuu() {
        return printmaisuu;
    }

    /**
     * 印刷枚数
     *
     * @param printmaisuu the printmaisuu to set
     */
    public void setPrintmaisuu(Integer printmaisuu) {
        this.printmaisuu = printmaisuu;
    }

    /**
     * 終了時膜厚AVE
     *
     * @return the makuatuaveEnd
     */
    public BigDecimal getMakuatuaveEnd() {
        return makuatuaveEnd;
    }

    /**
     * 終了時膜厚AVE
     *
     * @param makuatuaveEnd the makuatuaveEnd to set
     */
    public void setMakuatuaveEnd(BigDecimal makuatuaveEnd) {
        this.makuatuaveEnd = makuatuaveEnd;
    }

    /**
     * 終了時膜厚MAX
     *
     * @return the makuatumaxEnd
     */
    public BigDecimal getMakuatumaxEnd() {
        return makuatumaxEnd;
    }

    /**
     * 終了時膜厚MAX
     *
     * @param makuatumaxEnd the makuatumaxEnd to set
     */
    public void setMakuatumaxEnd(BigDecimal makuatumaxEnd) {
        this.makuatumaxEnd = makuatumaxEnd;
    }

    /**
     * 終了時膜厚MIN
     *
     * @return the makuatuminEnd
     */
    public BigDecimal getMakuatuminEnd() {
        return makuatuminEnd;
    }

    /**
     * 終了時膜厚MIN
     *
     * @param makuatuminEnd the makuatuminEnd to set
     */
    public void setMakuatuminEnd(BigDecimal makuatuminEnd) {
        this.makuatuminEnd = makuatuminEnd;
    }

    /**
     * 印刷ｴﾝﾄﾞ膜厚CV
     *
     * @return the makuatucvEnd
     */
    public BigDecimal getMakuatucvEnd() {
        return makuatucvEnd;
    }

    /**
     * 印刷ｴﾝﾄﾞ膜厚CV
     *
     * @param makuatucvEnd the makuatucvEnd to set
     */
    public void setMakuatucvEnd(BigDecimal makuatucvEnd) {
        this.makuatucvEnd = makuatucvEnd;
    }

    /**
     * 終了時ﾆｼﾞﾐ・ｶｽﾚ確認
     *
     * @return the nijimikasureEnd
     */
    public Integer getNijimikasureEnd() {
        return nijimikasureEnd;
    }

    /**
     * 終了時ﾆｼﾞﾐ・ｶｽﾚ確認
     *
     * @param nijimikasureEnd the nijimikasureEnd to set
     */
    public void setNijimikasureEnd(Integer nijimikasureEnd) {
        this.nijimikasureEnd = nijimikasureEnd;
    }

    /**
     * 終了時PTN間距離X
     *
     * @return the endPtnDistX
     */
    public Integer getEndPtnDistX() {
        return endPtnDistX;
    }

    /**
     * 終了時PTN間距離X
     *
     * @param endPtnDistX the endPtnDistX to set
     */
    public void setEndPtnDistX(Integer endPtnDistX) {
        this.endPtnDistX = endPtnDistX;
    }

    /**
     * 終了時PTN間距離Y
     *
     * @return the endPtnDistY
     */
    public Integer getEndPtnDistY() {
        return endPtnDistY;
    }

    /**
     * 終了時PTN間距離Y
     *
     * @param endPtnDistY the endPtnDistY to set
     */
    public void setEndPtnDistY(Integer endPtnDistY) {
        this.endPtnDistY = endPtnDistY;
    }

    /**
     * 終了ﾃﾝｼｮﾝ計
     *
     * @return the tensionESum
     */
    public BigDecimal getTensionESum() {
        return tensionESum;
    }

    /**
     * 終了ﾃﾝｼｮﾝ計
     *
     * @param tensionESum the tensionESum to set
     */
    public void setTensionESum(BigDecimal tensionESum) {
        this.tensionESum = tensionESum;
    }

    /**
     * ﾃﾝｼｮﾝ終了手前
     *
     * @return the tensionEtemae
     */
    public BigDecimal getTensionEtemae() {
        return tensionEtemae;
    }

    /**
     * ﾃﾝｼｮﾝ終了手前
     *
     * @param tensionEtemae the tensionEtemae to set
     */
    public void setTensionEtemae(BigDecimal tensionEtemae) {
        this.tensionEtemae = tensionEtemae;
    }

    /**
     * ﾃﾝｼｮﾝ終了奥
     *
     * @return the tensionEoku
     */
    public BigDecimal getTensionEoku() {
        return tensionEoku;
    }

    /**
     * ﾃﾝｼｮﾝ終了奥
     *
     * @param tensionEoku the tensionEoku to set
     */
    public void setTensionEoku(BigDecimal tensionEoku) {
        this.tensionEoku = tensionEoku;
    }

    /**
     * 印刷ズレ①刷り始め開始
     *
     * @return the printzure1SurihajimeStart
     */
    public Integer getPrintzure1SurihajimeStart() {
        return printzure1SurihajimeStart;
    }

    /**
     * 印刷ズレ①刷り始め開始
     *
     * @param printzure1SurihajimeStart the printzure1SurihajimeStart to set
     */
    public void setPrintzure1SurihajimeStart(Integer printzure1SurihajimeStart) {
        this.printzure1SurihajimeStart = printzure1SurihajimeStart;
    }

    /**
     * 印刷ズレ②中央開始
     *
     * @return the printzure2CenterStart
     */
    public Integer getPrintzure2CenterStart() {
        return printzure2CenterStart;
    }

    /**
     * 印刷ズレ②中央開始
     *
     * @param printzure2CenterStart the printzure2CenterStart to set
     */
    public void setPrintzure2CenterStart(Integer printzure2CenterStart) {
        this.printzure2CenterStart = printzure2CenterStart;
    }

    /**
     * 印刷ズレ③刷り終わり開始
     *
     * @return the printzure3SuriowariStart
     */
    public Integer getPrintzure3SuriowariStart() {
        return printzure3SuriowariStart;
    }

    /**
     * 印刷ズレ③刷り終わり開始
     *
     * @param printzure3SuriowariStart the printzure3SuriowariStart to set
     */
    public void setPrintzure3SuriowariStart(Integer printzure3SuriowariStart) {
        this.printzure3SuriowariStart = printzure3SuriowariStart;
    }

    /**
     * ABズレ平均スタート
     *
     * @return the abzureHeikinStart
     */
    public Integer getAbzureHeikinStart() {
        return abzureHeikinStart;
    }

    /**
     * ABズレ平均スタート
     *
     * @param abzureHeikinStart the abzureHeikinStart to set
     */
    public void setAbzureHeikinStart(Integer abzureHeikinStart) {
        this.abzureHeikinStart = abzureHeikinStart;
    }

    /**
     * 印刷ズレ①刷り始め終了
     *
     * @return the printzure1SurihajimeEnd
     */
    public Integer getPrintzure1SurihajimeEnd() {
        return printzure1SurihajimeEnd;
    }

    /**
     * 印刷ズレ①刷り始め終了
     *
     * @param printzure1SurihajimeEnd the printzure1SurihajimeEnd to set
     */
    public void setPrintzure1SurihajimeEnd(Integer printzure1SurihajimeEnd) {
        this.printzure1SurihajimeEnd = printzure1SurihajimeEnd;
    }

    /**
     * 印刷ズレ②中央終了
     *
     * @return the printzure2CenterEnd
     */
    public Integer getPrintzure2CenterEnd() {
        return printzure2CenterEnd;
    }

    /**
     * 印刷ズレ②中央終了
     *
     * @param printzure2CenterEnd the printzure2CenterEnd to set
     */
    public void setPrintzure2CenterEnd(Integer printzure2CenterEnd) {
        this.printzure2CenterEnd = printzure2CenterEnd;
    }

    /**
     * 印刷ズレ③刷り終わり終了
     *
     * @return the printzure3SuriowariEnd
     */
    public Integer getPrintzure3SuriowariEnd() {
        return printzure3SuriowariEnd;
    }

    /**
     * 印刷ズレ③刷り終わり終了
     *
     * @param printzure3SuriowariEnd the printzure3SuriowariEnd to set
     */
    public void setPrintzure3SuriowariEnd(Integer printzure3SuriowariEnd) {
        this.printzure3SuriowariEnd = printzure3SuriowariEnd;
    }

    /**
     * ABズレ平均終了
     *
     * @return the abzureHeikinEnd
     */
    public Integer getAbzureHeikinEnd() {
        return abzureHeikinEnd;
    }

    /**
     * ABズレ平均終了
     *
     * @param abzureHeikinEnd the abzureHeikinEnd to set
     */
    public void setAbzureHeikinEnd(Integer abzureHeikinEnd) {
        this.abzureHeikinEnd = abzureHeikinEnd;
    }

    /**
     * 原料記号
     *
     * @return the genryoukigou
     */
    public String getGenryoukigou() {
        return genryoukigou;
    }

    /**
     * 原料記号
     *
     * @param genryoukigou the genryoukigou to set
     */
    public void setGenryoukigou(String genryoukigou) {
        this.genryoukigou = genryoukigou;
    }

    /**
     * 備考1
     *
     * @return the bikou1
     */
    public String getBikou1() {
        return bikou1;
    }

    /**
     * 備考1
     *
     * @param bikou1 the bikou1 to set
     */
    public void setBikou1(String bikou1) {
        this.bikou1 = bikou1;
    }

    /**
     * 備考2
     *
     * @return the bikou2
     */
    public String getBikou2() {
        return bikou2;
    }

    /**
     * 備考2
     *
     * @param bikou2 the bikou2 to set
     */
    public void setBikou2(String bikou2) {
        this.bikou2 = bikou2;
    }

    /**
     * 登録日時
     *
     * @return the torokunichiji
     */
    public Timestamp getTorokunichiji() {
        return torokunichiji;
    }

    /**
     * 登録日時
     *
     * @param torokunichiji the torokunichiji to set
     */
    public void setTorokunichiji(Timestamp torokunichiji) {
        this.torokunichiji = torokunichiji;
    }

    /**
     * 更新日時
     *
     * @return the kosinnichiji
     */
    public Timestamp getKosinnichiji() {
        return kosinnichiji;
    }

    /**
     * 更新日時
     *
     * @param kosinnichiji the kosinnichiji to set
     */
    public void setKosinnichiji(Timestamp kosinnichiji) {
        this.kosinnichiji = kosinnichiji;
    }

    /**
     * revision
     *
     * @return the revision
     */
    public Long getRevision() {
        return revision;
    }

    /**
     * revision
     *
     * @param revision the revision to set
     */
    public void setRevision(Long revision) {
        this.revision = revision;
    }

    /**
     * KCPNO
     *
     * @return the kcpno
     */
    public String getKcpno() {
        return kcpno;
    }

    /**
     * KCPNO
     *
     * @param kcpno the kcpno to set
     */
    public void setKcpno(String kcpno) {
        this.kcpno = kcpno;
    }

    /**
     * 削除ﾌﾗｸﾞ
     *
     * @return the deleteflag
     */
    public Integer getDeleteflag() {
        return deleteflag;
    }

    /**
     * 削除ﾌﾗｸﾞ
     *
     * @param deleteflag the deleteflag to set
     */
    public void setDeleteflag(Integer deleteflag) {
        this.deleteflag = deleteflag;
    }

    /**
     * 印刷長さ
     * @return the printlength
     */
    public Integer getPrintlength() {
        return printlength;
    }

    /**
     * 印刷長さ
     * @param printlength the printlength to set
     */
    public void setPrintlength(Integer printlength) {
        this.printlength = printlength;
    }
}
