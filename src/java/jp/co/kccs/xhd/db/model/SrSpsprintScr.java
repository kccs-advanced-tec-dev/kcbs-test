/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2018/12/17<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SR_SPSPRINT(SPS印刷)のモデルクラスです。
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2018/12/17
 */
public class SrSpsprintScr {

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
     * ﾃｰﾌﾟ種類
     */
    private String tapesyurui;
    /**
     * ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
     */
    private String tapelotno;
    /**
     * ﾃｰﾌﾟｽﾘｯﾌﾟ記号
     */
    private String tapeSlipKigo;
    /**
     * 原料記号
     */
    private String genryoukigou;
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
     * 製版No
     */
    private String seihanno;
    /**
     * 製版枚数
     */
    private Long seihanmaisuu;
    /**
     * ﾌﾟﾘﾝﾄ開始日時
     */
    private Timestamp startdatetime;
    /**
     * ﾌﾟﾘﾝﾄ終了日時
     */
    private Timestamp enddatetime;
    /**
     * ｽｷｰｼﾞNo
     */
    private String skeegeno;
    /**
     * ｽｷｰｼﾞ枚数
     */
    private Long skeegemaisuu;
    /**
     * 号機ｺｰﾄﾞ
     */
    private String gouki;
    /**
     * 担当者ｺｰﾄﾞ
     */
    private String tantousya;
    /**
     * 確認者ｺｰﾄﾞ
     */
    private String kakuninsya;
    /**
     * 乾燥温度
     */
    private BigDecimal kansouondo;
    /**
     * 印刷ﾌﾟﾛﾌｧｲﾙ
     */
    private String prnprofile;
    /**
     * 乾燥時間
     */
    private BigDecimal kansoutime;
    /**
     * 差圧
     */
    private BigDecimal saatu;
    /**
     * ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
     */
    private Integer skeegespeed;
    /**
     * ｽｷｰｼﾞ角度
     */
    private Integer skeegeangle;
    /**
     * ﾒﾀﾙﾚｲﾀﾞｳﾝ値
     */
    private Integer mld;
    /**
     * ｸﾘｱﾗﾝｽ設定値
     */
    private BigDecimal clearrance;
    /**
     * 備考1
     */
    private String bikou1;
    /**
     * 備考2
     */
    private String bikou2;
    /**
     * 膜厚1
     */
    private BigDecimal makuatu1;
    /**
     * 膜厚2
     */
    private BigDecimal makuatu2;
    /**
     * 膜厚3
     */
    private BigDecimal makuatu3;
    /**
     * 膜厚4
     */
    private BigDecimal makuatu4;
    /**
     * 膜厚5
     */
    private BigDecimal makuatu5;
    /**
     * ﾍﾟｰｽﾄﾛｯﾄNo2
     */
    private String pastelotno2;
    /**
     * ﾍﾟｰｽﾄﾛｯﾄNo3
     */
    private String pastelotno3;
    /**
     * ﾍﾟｰｽﾄﾛｯﾄNo4
     */
    private String pastelotno4;
    /**
     * ﾍﾟｰｽﾄﾛｯﾄNo5
     */
    private String pastelotno5;
    /**
     * ﾍﾟｰｽﾄ粘度2
     */
    private BigDecimal pastenendo2;
    /**
     * ﾍﾟｰｽﾄ粘度3
     */
    private Integer pastenendo3;
    /**
     * ﾍﾟｰｽﾄ粘度4
     */
    private Integer pastenendo4;
    /**
     * ﾍﾟｰｽﾄ粘度5
     */
    private Integer pastenendo5;
    /**
     * ﾍﾟｰｽﾄ温度2
     */
    private Integer pasteondo2;
    /**
     * ﾍﾟｰｽﾄ温度3
     */
    private Integer pasteondo3;
    /**
     * ﾍﾟｰｽﾄ温度4
     */
    private Integer pasteondo4;
    /**
     * ﾍﾟｰｽﾄ温度5
     */
    private Integer pasteondo5;
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
     * 乾燥温度5
     */
    private BigDecimal kansouondo5;
    /**
     * ｽｷｰｼﾞ枚数2
     */
    private Integer skeegemaisuu2;
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
     * ﾃｰﾌﾟﾛｰﾙNo4
     */
    private String taperollno4;
    /**
     * ﾃｰﾌﾟﾛｰﾙNo5
     */
    private String taperollno5;
    /**
     * ﾍﾟｰｽﾄ品名
     */
    private String pastehinmei;
    /**
     * 製版名
     */
    private String seihanmei;
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
     * ｽﾀｰﾄ時PTN間距離X
     */
    private Integer startPtnDistX;
    /**
     * ｽﾀｰﾄ時PTN間距離Y
     */
    private Integer startPtnDistY;
    /**
     * ｾｯﾃｨﾝｸﾞ担当者ｺｰﾄﾞ
     */
    private String tantoSetting;
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
     * 終了時PTN間距離X
     */
    private Integer endPtnDistX;
    /**
     * 終了時PTN間距離Y
     */
    private Integer endPtnDistY;
    /**
     * 終了時担当者ｺｰﾄﾞ
     */
    private String tantoEnd;
    /**
     * KCPNO
     */
    private String kcpno;
    /**
     * 指示乾燥温度1
     */
    private BigDecimal sijiondo;
    /**
     * 指示乾燥温度2
     */
    private BigDecimal sijiondo2;
    /**
     * 指示乾燥温度3
     */
    private BigDecimal sijiondo3;
    /**
     * 指示乾燥温度4
     */
    private BigDecimal sijiondo4;
    /**
     * 指示乾燥温度5
     */
    private BigDecimal sijiondo5;
    /**
     * ﾍﾟｰｽﾄ固形分1
     */
    private BigDecimal pkokeibun1;
    /**
     * ﾍﾟｰｽﾄ固形分2
     */
    private BigDecimal pkokeibun2;
    /**
     * PETﾌｨﾙﾑ種類
     */
    private String petfilmsyurui;
    /**
     * 印刷ｽﾀｰﾄ膜厚CV
     */
    private BigDecimal makuatucvStart;
    /**
     * ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
     */
    private Integer nijimikasureStart;
    /**
     * 印刷ｴﾝﾄﾞ膜厚CV
     */
    private BigDecimal makuatucvEnd;
    /**
     * 終了時ﾆｼﾞﾐ・ｶｽﾚ確認
     */
    private Integer nijimikasureEnd;
    /**
     * 印刷枚数
     */
    private Integer printmaisuu;
    /**
     * ﾃｰﾌﾞﾙｸﾘｱﾗﾝｽ
     */
    private BigDecimal tableClearrance;
    /**
     * ｽｸﾚｯﾊﾟｰ速度
     */
    private Integer scraperspeed;
    /**
     * ｽｷｰｼﾞ外観
     */
    private Integer skeegegaikan;
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
    private String revision;
    
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
     * @return the tapeSlipKigo
     */
    public String getTapeSlipKigo() {
        return tapeSlipKigo;
    }

    /**
     * ﾃｰﾌﾟｽﾘｯﾌﾟ記号
     * @param TapeSlipKigo the tapeSlipKigo to set
     */
    public void setTapeSlipKigo(String TapeSlipKigo) {
        this.tapeSlipKigo = TapeSlipKigo;
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
    public Integer getSkeegespeed() {
        return skeegespeed;
    }

    /**
     * ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
     * @param skeegespeed the skeegespeed to set
     */
    public void setSkeegespeed(Integer skeegespeed) {
        this.skeegespeed = skeegespeed;
    }

    /**
     * ｽｷｰｼﾞ角度
     * @return the skeegeangle
     */
    public Integer getSkeegeangle() {
        return skeegeangle;
    }

    /**
     * ｽｷｰｼﾞ角度
     * @param skeegeangle the skeegeangle to set
     */
    public void setSkeegeangle(Integer skeegeangle) {
        this.skeegeangle = skeegeangle;
    }

    /**
     * ﾒﾀﾙﾚｲﾀﾞｳﾝ値
     * @return the mld
     */
    public Integer getMld() {
        return mld;
    }

    /**
     * ﾒﾀﾙﾚｲﾀﾞｳﾝ値
     * @param mld the mld to set
     */
    public void setMld(Integer mld) {
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
    public Integer getPastenendo3() {
        return pastenendo3;
    }

    /**
     * ﾍﾟｰｽﾄ粘度3
     * @param pastenendo3 the pastenendo3 to set
     */
    public void setPastenendo3(Integer pastenendo3) {
        this.pastenendo3 = pastenendo3;
    }

    /**
     * ﾍﾟｰｽﾄ粘度4
     * @return the pastenendo4
     */
    public Integer getPastenendo4() {
        return pastenendo4;
    }

    /**
     * ﾍﾟｰｽﾄ粘度4
     * @param pastenendo4 the pastenendo4 to set
     */
    public void setPastenendo4(Integer pastenendo4) {
        this.pastenendo4 = pastenendo4;
    }

    /**
     * ﾍﾟｰｽﾄ粘度5
     * @return the pastenendo5
     */
    public Integer getPastenendo5() {
        return pastenendo5;
    }

    /**
     * ﾍﾟｰｽﾄ粘度5
     * @param pastenendo5 the pastenendo5 to set
     */
    public void setPastenendo5(Integer pastenendo5) {
        this.pastenendo5 = pastenendo5;
    }

    /**
     * ﾍﾟｰｽﾄ温度2
     * @return the pasteondo2
     */
    public Integer getPasteondo2() {
        return pasteondo2;
    }

    /**
     * ﾍﾟｰｽﾄ温度2
     * @param pasteondo2 the pasteondo2 to set
     */
    public void setPasteondo2(Integer pasteondo2) {
        this.pasteondo2 = pasteondo2;
    }

    /**
     * ﾍﾟｰｽﾄ温度3
     * @return the pasteondo3
     */
    public Integer getPasteondo3() {
        return pasteondo3;
    }

    /**
     * ﾍﾟｰｽﾄ温度3
     * @param pasteondo3 the pasteondo3 to set
     */
    public void setPasteondo3(Integer pasteondo3) {
        this.pasteondo3 = pasteondo3;
    }

    /**
     * ﾍﾟｰｽﾄ温度4
     * @return the pasteondo4
     */
    public Integer getPasteondo4() {
        return pasteondo4;
    }

    /**
     * ﾍﾟｰｽﾄ温度4
     * @param pasteondo4 the pasteondo4 to set
     */
    public void setPasteondo4(Integer pasteondo4) {
        this.pasteondo4 = pasteondo4;
    }

    /**
     * ﾍﾟｰｽﾄ温度5
     * @return the pasteondo5
     */
    public Integer getPasteondo5() {
        return pasteondo5;
    }

    /**
     * ﾍﾟｰｽﾄ温度5
     * @param pasteondo5 the pasteondo5 to set
     */
    public void setPasteondo5(Integer pasteondo5) {
        this.pasteondo5 = pasteondo5;
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
    public Integer getSkeegemaisuu2() {
        return skeegemaisuu2;
    }

    /**
     * ｽｷｰｼﾞ枚数2
     * @param skeegemaisuu2 the skeegemaisuu2 to set
     */
    public void setSkeegemaisuu2(Integer skeegemaisuu2) {
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
    public Integer getStartPtnDistX() {
        return startPtnDistX;
    }

    /**
     * ｽﾀｰﾄ時PTN間距離X
     * @param startPtnDistX the startPtnDistX to set
     */
    public void setStartPtnDistX(Integer startPtnDistX) {
        this.startPtnDistX = startPtnDistX;
    }

    /**
     * ｽﾀｰﾄ時PTN間距離Y
     * @return the startPtnDistY
     */
    public Integer getStartPtnDistY() {
        return startPtnDistY;
    }

    /**
     * ｽﾀｰﾄ時PTN間距離Y
     * @param startPtnDistY the startPtnDistY to set
     */
    public void setStartPtnDistY(Integer startPtnDistY) {
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
    public Integer getEndPtnDistX() {
        return endPtnDistX;
    }

    /**
     * 終了時PTN間距離X
     * @param endPtnDistX the endPtnDistX to set
     */
    public void setEndPtnDistX(Integer endPtnDistX) {
        this.endPtnDistX = endPtnDistX;
    }

    /**
     * 終了時PTN間距離Y
     * @return the endPtnDistY
     */
    public Integer getEndPtnDistY() {
        return endPtnDistY;
    }

    /**
     * 終了時PTN間距離Y
     * @param endPtnDistY the endPtnDistY to set
     */
    public void setEndPtnDistY(Integer endPtnDistY) {
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
    public Integer getNijimikasureStart() {
        return nijimikasureStart;
    }

    /**
     * ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
     * @param nijimikasureStart the nijimikasureStart to set
     */
    public void setNijimikasureStart(Integer nijimikasureStart) {
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
    public Integer getNijimikasureEnd() {
        return nijimikasureEnd;
    }

    /**
     * 終了時ﾆｼﾞﾐ・ｶｽﾚ確認
     * @param nijimikasureEnd the nijimikasureEnd to set
     */
    public void setNijimikasureEnd(Integer nijimikasureEnd) {
        this.nijimikasureEnd = nijimikasureEnd;
    }

    /**
     * 印刷枚数
     * @return the printmaisuu
     */
    public Integer getPrintmaisuu() {
        return printmaisuu;
    }

    /**
     * 印刷枚数
     * @param printmaisuu the printmaisuu to set
     */
    public void setPrintmaisuu(Integer printmaisuu) {
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
    public Integer getScraperspeed() {
        return scraperspeed;
    }

    /**
     * ｽｸﾚｯﾊﾟｰ速度
     * @param scraperspeed the scraperspeed to set
     */
    public void setScraperspeed(Integer scraperspeed) {
        this.scraperspeed = scraperspeed;
    }

    /**
     * ｽｷｰｼﾞ外観
     * @return the skeegegaikan
     */
    public Integer getSkeegegaikan() {
        return skeegegaikan;
    }

    /**
     * ｽｷｰｼﾞ外観
     * @param skeegegaikan the skeegegaikan to set
     */
    public void setSkeegegaikan(Integer skeegegaikan) {
        this.skeegegaikan = skeegegaikan;
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
    public String getRevision() {
        return revision;
    }

    /**
     * revision
     * @param revision the revision to set
     */
    public void setRevision(String revision) {
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
