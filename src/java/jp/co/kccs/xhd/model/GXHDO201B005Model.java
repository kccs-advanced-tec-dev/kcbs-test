/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/04/08<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCCS D.Yanagida<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 積層・RSUS履歴検索画面のモデルクラスです。
 *
 * @author KCCS D.Yanagida
 * @since 2019/04/08
 */
public class GXHDO201B005Model {
    /** ﾛｯﾄNo. */
    private String lotno = "";
    /** KCPNO */
    private String kcpno = "";
    /** ｾｯﾄ数 */
    private String setsuu = "";
    /** 客先 */
    private String tokuisaki = "";
    /** ﾛｯﾄ区分 */
    private String lotkubuncode = "";
    /** ｵｰﾅｰ */
    private String ownercode = "";
    /** 端子ﾃｰﾌﾟ種類 */
    private String tntapesyurui = "";
    /** 端子ﾃｰﾌﾟNo */
    private String tntapeno = "";
    /** 端子ﾃｰﾌﾟ原料 */
    private String tntapegenryo = "";
    /** 開始日時 */
    private Timestamp kaisinichiji = null;
    /** 終了日時 */
    private Timestamp syuryonichiji = null;
    /** 積層号機 */
    private String goki = "";
    /** 実圧力 */
    private String jituaturyoku = "";
    /** 積層ｽﾞﾚ値2 */
    private Long sekisozure2 = null;
    /** 開始担当者 */
    private String tantosya = "";
    /** 確認者ｺｰﾄﾞ */
    private String kakuninsya = "";
    /** 発泡ｼｰﾄNo */
    private String happosheetno = "";
    /** 瞬時加熱時間 */
    private BigDecimal skjikan = null;
    /** ﾀｸﾄ */
    private BigDecimal takuto = null;
    /** 備考1 */
    private String biko1 = "";
    /** 先行ﾛｯﾄNo */
    private String slotno = "";
    /** ｽﾘｯﾌﾟﾛｯﾄNo */
    private String tapelotno = "";
    /** ﾛｰﾙNo1 */
    private String taperollno1 = "";
    /** 原料記号 */
    private String genryoukigou = "";
    /** PETﾌｨﾙﾑ種類 */
    private String petfilmsyurui = "";
    /** 固着ｼｰﾄ貼付り機 */
    private String kotyakugouki = "";
    /** 固着ｼｰﾄ */
    private String kotyakusheet = "";
    /** 下端子号機 */
    private String shitaTanshigouki = "";
    /** 上端子号機 */
    private String uwaTanshigouki = "";
    /** 上端子ﾃｰﾌﾟ */
    private String uwaTanshiTape = "";
    /** 上端子積層数 */
    private Long uwaTanshiMaisuu = null;
    /** 下端子ﾃｰﾌﾟ */
    private String shitaTanshiTape = "";
    /** 下端子積層数 */
    private Long shitaTanshiMaisuu = null;
    /** 下端子 */
    private String shitaTanshi = "";
    /** 上端子 */
    private String uwaTanshi = "";
    /** 外観確認1 */
    private String gaikanKakunin1 = "";
    /** 処理ｾｯﾄ数 */
    private Long syoriSetsuu = null;    
    /** 良品ｾｯﾄ数 */
    private BigDecimal ryouhinSetsuu = null;
    /** 終了担当者 */
    private String endTantousyacode = "";
    /** 備考2 */
    private String bikou2 = "";
    /** 列×行 */
    private String lretuWretu = "";
    /** 製版名 */
    private String patern = "";
    /** 取り個数 */
    private Long torikosuu = null;
    /** ﾋﾟｯﾁ */
    private String pitch = "";
    /** 電極ﾍﾟｰｽﾄ */
    private String epaste = "";
    /** 電極ﾃｰﾌﾟ */
    private String etape = "";
    /** 電極ﾃｰﾌﾟ厚み */
    private Double etapeatumi = null;
    /** 電極積層数 */
    private Long sekisousu = null;
    /** 積層ｽﾗｲﾄﾞ量(mm) */
    private String sekisouslideryo = "";
    /** 最上層ｽﾗｲﾄﾞ量(μm) */
    private String lastlayerslideryo = "";
    /** 連続積層枚数 */
    private Long renzokusekisoumaisuu = null;
    /** B層補正量 */
    private BigDecimal bsouhoseiryou = null;
    /** Y軸補正量 */
    private Long yjikuhoseiryou = null;    

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
     * 端子ﾃｰﾌﾟ種類
     * @return the tntapesyurui
     */
    public String getTntapesyurui() {
        return tntapesyurui;
    }

    /**
     * 端子ﾃｰﾌﾟ種類
     * @param tntapesyurui the tntapesyurui to set
     */
    public void setTntapesyurui(String tntapesyurui) {
        this.tntapesyurui = tntapesyurui;
    }

    /**
     * 端子ﾃｰﾌﾟNo
     * @return the tntapeno
     */
    public String getTntapeno() {
        return tntapeno;
    }

    /**
     * 端子ﾃｰﾌﾟNo
     * @param tntapeno the tntapeno to set
     */
    public void setTntapeno(String tntapeno) {
        this.tntapeno = tntapeno;
    }

    /**
     * 端子ﾃｰﾌﾟ原料
     * @return the tntapegenryo
     */
    public String getTntapegenryo() {
        return tntapegenryo;
    }

    /**
     * 端子ﾃｰﾌﾟ原料
     * @param tntapegenryo the tntapegenryo to set
     */
    public void setTntapegenryo(String tntapegenryo) {
        this.tntapegenryo = tntapegenryo;
    }

    /**
     * 開始日時
     * @return the kaisinichiji
     */
    public Timestamp getKaisinichiji() {
        return kaisinichiji;
    }

    /**
     * 開始日時
     * @param kaisinichiji the kaisinichiji to set
     */
    public void setKaisinichiji(Timestamp kaisinichiji) {
        this.kaisinichiji = kaisinichiji;
    }

    /**
     * 終了日時
     * @return the syuryonichiji
     */
    public Timestamp getSyuryonichiji() {
        return syuryonichiji;
    }

    /**
     * 終了日時
     * @param syuryonichiji the syuryonichiji to set
     */
    public void setSyuryonichiji(Timestamp syuryonichiji) {
        this.syuryonichiji = syuryonichiji;
    }

    /**
     * 積層号機
     * @return the goki
     */
    public String getGoki() {
        return goki;
    }

    /**
     * 積層号機
     * @param goki the goki to set
     */
    public void setGoki(String goki) {
        this.goki = goki;
    }

    /**
     * 実圧力
     * @return the jituaturyoku
     */
    public String getJituaturyoku() {
        return jituaturyoku;
    }

    /**
     * 実圧力
     * @param jituaturyoku the jituaturyoku to set
     */
    public void setJituaturyoku(String jituaturyoku) {
        this.jituaturyoku = jituaturyoku;
    }

    /**
     * 積層ｽﾞﾚ値2
     * @return the sekisozure2
     */
    public Long getSekisozure2() {
        return sekisozure2;
    }

    /**
     * 積層ｽﾞﾚ値2
     * @param sekisozure2 the sekisozure2 to set
     */
    public void setSekisozure2(Long sekisozure2) {
        this.sekisozure2 = sekisozure2;
    }

    /**
     * 開始担当者
     * @return the tantosya
     */
    public String getTantosya() {
        return tantosya;
    }

    /**
     * 開始担当者
     * @param tantosya the tantosya to set
     */
    public void setTantosya(String tantosya) {
        this.tantosya = tantosya;
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
     * 発砲ｼｰﾄNo
     * @return the happosheetno
     */
    public String getHapposheetno() {
        return happosheetno;
    }

    /**
     * 発砲ｼｰﾄNo
     * @param happosheetno the happosheetno to set
     */
    public void setHapposheetno(String happosheetno) {
        this.happosheetno = happosheetno;
    }

    /**
     * 瞬時加熱時間
     * @return the skjikan
     */
    public BigDecimal getSkjikan() {
        return skjikan;
    }

    /**
     * 瞬時加熱時間
     * @param skjikan the skjikan to set
     */
    public void setSkjikan(BigDecimal skjikan) {
        this.skjikan = skjikan;
    }

    /**
     * ﾀｸﾄ
     * @return the takuto
     */
    public BigDecimal getTakuto() {
        return takuto;
    }

    /**
     * ﾀｸﾄ
     * @param takuto the takuto to set
     */
    public void setTakuto(BigDecimal takuto) {
        this.takuto = takuto;
    }

    /**
     * 備考1
     * @return the biko1
     */
    public String getBiko1() {
        return biko1;
    }

    /**
     * 備考1
     * @param biko1 the biko1 to set
     */
    public void setBiko1(String biko1) {
        this.biko1 = biko1;
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
     * ｽﾘｯﾌﾟﾛｯﾄNo
     * @return the tapelotno
     */
    public String getTapelotno() {
        return tapelotno;
    }

    /**
     * ｽﾘｯﾌﾟﾛｯﾄNo
     * @param tapelotno the tapelotno to set
     */
    public void setTapelotno(String tapelotno) {
        this.tapelotno = tapelotno;
    }

    /**
     * ﾛｰﾙNo1
     * @return the taperollno1
     */
    public String getTaperollno1() {
        return taperollno1;
    }

    /**
     * ﾛｰﾙNo1
     * @param taperollno1 the taperollno1 to set
     */
    public void setTaperollno1(String taperollno1) {
        this.taperollno1 = taperollno1;
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
     * 固着ｼｰﾄ貼付り機
     * @return the kotyakugouki
     */
    public String getKotyakugouki() {
        return kotyakugouki;
    }

    /**
     * 固着ｼｰﾄ貼付り機
     * @param kotyakugouki the kotyakugouki to set
     */
    public void setKotyakugouki(String kotyakugouki) {
        this.kotyakugouki = kotyakugouki;
    }

    /**
     * 固着ｼｰﾄ
     * @return the kotyakusheet
     */
    public String getKotyakusheet() {
        return kotyakusheet;
    }

    /**
     * 固着ｼｰﾄ
     * @param kotyakusheet the kotyakusheet to set
     */
    public void setKotyakusheet(String kotyakusheet) {
        this.kotyakusheet = kotyakusheet;
    }

    /**
     * 下端子号機
     * @return the shitaTanshigouki
     */
    public String getShitaTanshigouki() {
        return shitaTanshigouki;
    }

    /**
     * 下端子号機
     * @param shitaTanshigouki the shitaTanshigouki to set
     */
    public void setShitaTanshigouki(String shitaTanshigouki) {
        this.shitaTanshigouki = shitaTanshigouki;
    }

    /**
     * 上端子号機
     * @return the uwaTanshigouki
     */
    public String getUwaTanshigouki() {
        return uwaTanshigouki;
    }

    /**
     * 上端子号機
     * @param uwaTanshigouki the uwaTanshigouki to set
     */
    public void setUwaTanshigouki(String uwaTanshigouki) {
        this.uwaTanshigouki = uwaTanshigouki;
    }

    /**
     * 下端子
     * @return the shitaTanshi
     */
    public String getShitaTanshi() {
        return shitaTanshi;
    }

    /**
     * 下端子
     * @param shitaTanshi the shitaTanshi to set
     */
    public void setShitaTanshi(String shitaTanshi) {
        this.shitaTanshi = shitaTanshi;
    }

    /**
     * 上端子
     * @return the uwaTanshi
     */
    public String getUwaTanshi() {
        return uwaTanshi;
    }

    /**
     * 上端子
     * @param uwaTanshi the uwaTanshi to set
     */
    public void setUwaTanshi(String uwaTanshi) {
        this.uwaTanshi = uwaTanshi;
    }

    /**
     * 処理ｾｯﾄ数
     * @return the syoriSetsuu
     */
    public Long getSyoriSetsuu() {
        return syoriSetsuu;
    }

    /**
     * 処理ｾｯﾄ数
     * @param syoriSetsuu the syoriSetsuu to set
     */
    public void setSyoriSetsuu(Long syoriSetsuu) {
        this.syoriSetsuu = syoriSetsuu;
    }
    
    /**
     * 外観確認1
     * @return the gaikanKakunin1
     */
    public String getGaikanKakunin1() {
        return gaikanKakunin1;
    }

    /**
     * 外観確認1
     * @param gaikanKakunin1 the gaikanKakunin1 to set
     */
    public void setGaikanKakunin1(String gaikanKakunin1) {
        this.gaikanKakunin1 = gaikanKakunin1;
    }

    /**
     * 良品ｾｯﾄ数
     * @return the ryouhinSetsuu
     */
    public BigDecimal getRyouhinSetsuu() {
        return ryouhinSetsuu;
    }

    /**
     * 良品ｾｯﾄ数
     * @param ryouhinSetsuu the ryouhinSetsuu to set
     */
    public void setRyouhinSetsuu(BigDecimal ryouhinSetsuu) {
        this.ryouhinSetsuu = ryouhinSetsuu;
    }

    /**
     * 終了担当者
     * @return the endTantousyacode
     */
    public String getEndTantousyacode() {
        return endTantousyacode;
    }

    /**
     * 終了担当者
     * @param endTantousyacode the endTantousyacode to set
     */
    public void setEndTantousyacode(String endTantousyacode) {
        this.endTantousyacode = endTantousyacode;
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
     * ｾｯﾄ数
     * @return the setsuu
     */
    public String getSetsuu() {
        return setsuu;
    }

    /**
     * ｾｯﾄ数
     * @param setsuu the setsuu to set
     */
    public void setSetsuu(String setsuu) {
        this.setsuu = setsuu;
    }

    /**
     * 客先
     * @return the tokuisaki
     */
    public String getTokuisaki() {
        return tokuisaki;
    }

    /**
     * 客先
     * @param tokuisaki the tokuisaki to set
     */
    public void setTokuisaki(String tokuisaki) {
        this.tokuisaki = tokuisaki;
    }

    /**
     * ﾛｯﾄ区分
     * @return the lotkubuncode
     */
    public String getLotkubuncode() {
        return lotkubuncode;
    }

    /**
     * ﾛｯﾄ区分
     * @param lotkubuncode the lotkubuncode to set
     */
    public void setLotkubuncode(String lotkubuncode) {
        this.lotkubuncode = lotkubuncode;
    }

    /**
     * ｵｰﾅｰ
     * @return the ownercode
     */
    public String getOwnercode() {
        return ownercode;
    }

    /**
     * ｵｰﾅｰ
     * @param ownercode the ownercode to set
     */
    public void setOwnercode(String ownercode) {
        this.ownercode = ownercode;
    }

    /**
     * 上端子ﾃｰﾌﾟ
     * @return the uwaTanshiTape
     */
    public String getUwaTanshiTape() {
        return uwaTanshiTape;
    }

     /**
     * 上端子ﾃｰﾌﾟ
     * @param uwaTanshiTape the uwaTanshiTape to set
     */
    public void setUwaTanshiTape(String uwaTanshiTape) {
        this.uwaTanshiTape = uwaTanshiTape;
    }

    /**
     * 上端子積層数
     * @return the uwaTanshiMaisuu
     */
    public Long getUwaTanshiMaisuu() {
        return uwaTanshiMaisuu;
    }

    /**
     * 上端子積層数
     * @param uwaTanshiMaisuu the uwaTanshiMaisuu to set
     */
    public void setUwaTanshiMaisuu(Long uwaTanshiMaisuu) {
        this.uwaTanshiMaisuu = uwaTanshiMaisuu;
    }

    /**
     * 下端子ﾃｰﾌﾟ
     * @return the shitaTanshiTape
     */
    public String getShitaTanshiTape() {
        return shitaTanshiTape;
    }

    /**
     * 下端子ﾃｰﾌﾟ
     * @param shitaTanshiTape the shitaTanshiTape to set
     */
    public void setShitaTanshiTape(String shitaTanshiTape) {
        this.shitaTanshiTape = shitaTanshiTape;
    }

    /**
     * 下端子積層数
     * @return the shitaTanshiMaisuu
     */
    public Long getShitaTanshiMaisuu() {
        return shitaTanshiMaisuu;
    }

    /**
     * 下端子積層数
     * @param shitaTanshiMaisuu the shitaTanshiMaisuu to set
     */
    public void setShitaTanshiMaisuu(Long shitaTanshiMaisuu) {
        this.shitaTanshiMaisuu = shitaTanshiMaisuu;
    }

    /**
     * 列×行
     * @return the lretuWretu
     */
    public String getLretuWretu() {
        return lretuWretu;
    }

    /**
     * 列×行
     * @param lretuWretu the lretuWretu to set
     */
    public void setLretuWretu(String lretuWretu) {
        this.lretuWretu = lretuWretu;
    }

    /**
     * 製版名
     * @return the patern
     */
    public String getPatern() {
        return patern;
    }

    /**
     * 製版名
     * @param patern the patern to set
     */
    public void setPatern(String patern) {
        this.patern = patern;
    }

    /**
     * 取り個数
     * @return the torikosuu
     */
    public Long getTorikosuu() {
        return torikosuu;
    }

    /**
     * 取り個数
     * @param torikosuu the torikosuu to set
     */
    public void setTorikosuu(Long torikosuu) {
        this.torikosuu = torikosuu;
    }

    /**
     * ﾋﾟｯﾁ
     * @return the pitch
     */
    public String getPitch() {
        return pitch;
    }

    /**
     * ﾋﾟｯﾁ
     * @param pitch the pitch to set
     */
    public void setPitch(String pitch) {
        this.pitch = pitch;
    }

    /**
     * 電極ﾍﾟｰｽﾄ
     * @return the epaste
     */
    public String getEpaste() {
        return epaste;
    }

    /**
     * 電極ﾍﾟｰｽﾄ
     * @param epaste the epaste to set
     */
    public void setEpaste(String epaste) {
        this.epaste = epaste;
    }

    /**
     * 電極ﾃｰﾌﾟ
     * @return the etape
     */
    public String getEtape() {
        return etape;
    }

    /**
     * 電極ﾃｰﾌﾟ
     * @param etape the etape to set
     */
    public void setEtape(String etape) {
        this.etape = etape;
    }

    /**
     * 電極ﾃｰﾌﾟ厚み
     * @return the etapeatumi
     */
    public Double getEtapeatumi() {
        return etapeatumi;
    }

    /**
     * 電極ﾃｰﾌﾟ厚み
     * @param etapeatumi the etapeatumi to set
     */
    public void setEtapeatumi(Double etapeatumi) {
        this.etapeatumi = etapeatumi;
    }

    /**
     * 電極積層数
     * @return the sekisousu
     */
    public Long getSekisousu() {
        return sekisousu;
    }

    /**
     * 電極積層数
     * @param sekisousu the sekisousu to set
     */
    public void setSekisousu(Long sekisousu) {
        this.sekisousu = sekisousu;
    }

    /**
     * 積層ｽﾗｲﾄﾞ量(mm)
     * @return the sekisouslideryo
     */
    public String getSekisouslideryo() {
        return sekisouslideryo;
    }

    /**
     * 積層ｽﾗｲﾄﾞ量(mm)
     * @param sekisouslideryo the sekisouslideryo to set
     */
    public void setSekisouslideryo(String sekisouslideryo) {
        this.sekisouslideryo = sekisouslideryo;
    }

    /**
     * 最上層ｽﾗｲﾄﾞ量(μm)
     * @return the lastlayerslideryo
     */
    public String getLastlayerslideryo() {
        return lastlayerslideryo;
    }

    /**
     * 最上層ｽﾗｲﾄﾞ量(μm)
     * @param lastlayerslideryo the lastlayerslideryo to set
     */
    public void setLastlayerslideryo(String lastlayerslideryo) {
        this.lastlayerslideryo = lastlayerslideryo;
    }

    /**
     * 連続積層枚数
     * @return the renzokusekisoumaisuu
     */
    public Long getRenzokusekisoumaisuu() {
        return renzokusekisoumaisuu;
    }

    /**
     * 連続積層枚数
     * @param renzokusekisoumaisuu the renzokusekisoumaisuu to set
     */
    public void setRenzokusekisoumaisuu(Long renzokusekisoumaisuu) {
        this.renzokusekisoumaisuu = renzokusekisoumaisuu;
    }

    /**
     * B層補正量
     * @return the bsouhoseiryou
     */
    public BigDecimal getBsouhoseiryou() {
        return bsouhoseiryou;
    }

    /**
     * B層補正量
     * @param bsouhoseiryou the bsouhoseiryou to set
     */
    public void setBsouhoseiryou(BigDecimal bsouhoseiryou) {
        this.bsouhoseiryou = bsouhoseiryou;
    }

    /**
     * Y軸補正量
     * @return the yjikuhoseiryou
     */
    public Long getYjikuhoseiryou() {
        return yjikuhoseiryou;
    }

    /**
     * Y軸補正量
     * @param yjikuhoseiryou the yjikuhoseiryou to set
     */
    public void setYjikuhoseiryou(Long yjikuhoseiryou) {
        this.yjikuhoseiryou = yjikuhoseiryou;
    }

}
