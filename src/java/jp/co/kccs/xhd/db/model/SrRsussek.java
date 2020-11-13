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
 * 変更日	2019/03/08<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * <br>
 * 変更日       2020/9/18<br>
 * 計画書No     MB2008-DK001<br>
 * 変更者       863 zhangjinyan<br>
 * 変更理由     項目追加・変更<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SR_RSUSSEK(積層RSUS)のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2019/03/08
 */
public class SrRsussek {

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
     * KCPNO
     */
    private String kcpno;

    /**
     * 端子ﾃｰﾌﾟ種類
     */
    private String tntapesyurui;

    /**
     * 端子ﾃｰﾌﾟNo
     */
    private String tntapeno;

    /**
     * 端子ﾃｰﾌﾟ原料
     */
    private String tntapegenryo;

    /**
     * 開始日時
     */
    private Timestamp kaisinichiji;

    /**
     * 終了日時
     */
    private Timestamp syuryonichiji;

    /**
     * 号機ｺｰﾄﾞ
     */
    private String goki;

    /**
     * 実圧力
     */
    private String jituaturyoku;

    /**
     * 積層ｽﾞﾚ2
     */
    private Integer sekisozure2;

    /**
     * 担当者ｺｰﾄﾞ
     */
    private String tantosya;

    /**
     * 確認者ｺｰﾄﾞ
     */
    private String kakuninsya;

    /**
     * 印刷ﾛｰﾙNo
     */
    private String insaturollno;

    /**
     * 発砲ｼｰﾄNo
     */
    private String happosheetno;

    /**
     * 瞬時加熱時間
     */
    private BigDecimal skjikan;

    /**
     * ﾀｸﾄ
     */
    private BigDecimal takuto;

    /**
     * 備考1
     */
    private String biko1;

    /**
     * 登録日時
     */
    private Timestamp torokunichiji;

    /**
     * 更新日時
     */
    private Timestamp kosinnichiji;

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
     * ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
     */
    private String tapelotno;

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
     * 原料記号
     */
    private String genryoukigou;

    /**
     * PETﾌｨﾙﾑ種類
     */
    private String petfilmsyurui;

    /**
     * 固着ｼｰﾄ貼付り機
     */
    private String kotyakugouki;

    /**
     * 固着ｼｰﾄ
     */
    private String kotyakusheet;

    /**
     * 下端子号機
     */
    private String shitatanshigouki;

    /**
     * 上端子号機
     */
    private String uwatanshigouki;

    /**
     * 下端子ﾌﾞｸ抜き
     */
    private Integer shitatanshibukunuki;

    /**
     * 下端子
     */
    private String shitatanshi;

    /**
     * 上端子
     */
    private String uwatanshi;
    
    /**
     * 処理ｾｯﾄ数
     */
    private Integer syorisetsuu;

    /**
     * 良品ｾｯﾄ数
     */
    private BigDecimal ryouhinsetsuu;
    
    /**
     * 外観確認1
     */
    private String gaikankakunin1;

    /**
     * 外観確認2
     */
    private String gaikankakunin2;

    /**
     * 外観確認3
     */
    private String gaikankakunin3;

    /**
     * 外観確認4
     */
    private String gaikankakunin4;
    
    /**
     * 終了担当者
     */
    private String endtantousyacode;

    /**
     * 端子ﾃｰﾌﾟ種類
     */
    private Integer tanshitapesyurui;
    
    /**
     * 隔離NG回数
     */
    private Integer hngkaisuu;

    /**
     * 剥離NG回数AVE
     */
    private BigDecimal hngkaisuuave;

    /**
     * 画像NG回数
     */
    private Integer gngkaisuu;

    /**
     * 画像NG回数AVE
     */
    private BigDecimal gngkaisuuave;

    /**
     * 備考2
     */
    private String bikou2;

    /**
     * revision
     */
    private Integer revision;

    /**
     * ｾｯﾄ数
     */
    private Integer setsuu;

    /**
     * 客先
     */
    private String tokuisaki;

    /**
     * ﾛｯﾄ区分
     */
    private String lotkubuncode;

    /**
     * ｵｰﾅｰ
     */
    private String ownercode;

    /**
     * 下ｶﾊﾞｰﾃｰﾌﾟ1種類
     */
    private String syurui3;

    /**
     * 下ｶﾊﾞｰﾃｰﾌﾟ1厚み
     */
    private double atumi3;

    /**
     * 下ｶﾊﾞｰﾃｰﾌﾟ1枚数
     */
    private Integer maisuu3;

    /**
     * 製版名
     */
    private String patern;

    /**
     * 取り個数
     */
    private Integer torikosuu;

    /**
     * 電極ﾃｰﾌﾟ
     */
    private String etape;

    /**
     * 列
     */
    private Integer lretu;

    /**
     * 行
     */
    private Integer wretu;

    /**
     * LSUN
     */
    private BigDecimal lsun;

    /**
     * WSUN
     */
    private BigDecimal wsun;

    /**
     * 電極ﾍﾟｰｽﾄ
     */
    private String epaste;

    /**
     * 原料
     */
    private String genryou;

    /**
     * 電極厚み
     */
    private double eatumi;

    /**
     * 総数
     */
    private double sousuu;

    /**
     * 電極枚数
     */
    private Integer emaisuu;

    /**
     * 積層ｽﾗｲﾄﾞ量
     */
    private String sekisouslideryo;

    /**
     * 最上層ｽﾗｲﾄﾞ量
     */
    private double lastlayerslideryo;

    /**
     * 連続積層枚数
     */
    private Integer renzokusekisoumaisuu;

    /**
     * B層補正量
     */
    private BigDecimal bsouhoseiryou;

    /**
     * Y軸補正量
     */
    private Integer yjikuhoseiryou;

    /**
     * 上ｶﾊﾞｰﾃｰﾌﾟ1種類
     */
    private String syurui2;

    /**
     * 上ｶﾊﾞｰﾃｰﾌﾟ1厚み
     */
    private double atumi2;

    /**
     * 上ｶﾊﾞｰﾃｰﾌﾟ1枚数
     */
    private Integer maisuu2;
    
    /**
     * 下端子開始日時
     */
    private Timestamp shitatanshikaisinichiji;
    
    /**
     * 下端子終了日時
     */
    private Timestamp shitatanshisyuryonichiji;
    
    /**
     * 下端子担当者
     */
    private String shitatanshitantosya;
    
    /**
     * 下端子確認者
     */
    private String shitatanshikakuninsya;
    
    /**
     * 下端子備考
     */
    private String shitatanshibiko;
    
    /**
     * 上端子開始日時
     */
    private Timestamp uwatanshikaisinichiji;
    
    /**
     * 上端子終了日時
     */
    private Timestamp uwatanshisyuryonichiji;
    
    /**
     * 上端子担当者
     */
    private String uwatanshitantosya;
    
    /**
     * 上端子確認者
     */
    private String uwatanshikakuninsya;
    
    /**
     * 上端子備考
     */
    private String uwatanshibiko;
    
    /**
     * ﾍｯﾄﾞNo
     */
    private String headno;
    
    /**
     * SUS板枚数
     */
    private Integer susitamaisu;
    
    /**
     * 最上層担当者
     */
    private String lastlayertantosya;
    
    /**
     * 最上層備考
     */
    private String lastlayerbiko;
    
    /**
     * 電極製版ﾛｯﾄNo
     */
    private String elotno;

    /**
     * 電極製版ﾛｯﾄNo
     */
    private String sekiatsu;

    /**
     * @return kojyo
     */
    public String getKojyo() {
            return kojyo;
    }

    /**
     * @param kojyo セットする kojyo
     */
    public void setKojyo(String kojyo) {
            this.kojyo = kojyo;
    }

    /**
     * @return lotno
     */
    public String getLotno() {
            return lotno;
    }

    /**
     * @param lotno セットする lotno
     */
    public void setLotno(String lotno) {
            this.lotno = lotno;
    }

    /**
     * @return edaban
     */
    public String getEdaban() {
            return edaban;
    }

    /**
     * @param edaban セットする edaban
     */
    public void setEdaban(String edaban) {
            this.edaban = edaban;
    }

    /**
     * @return kcpno
     */
    public String getKcpno() {
            return kcpno;
    }

    /**
     * @param kcpno セットする kcpno
     */
    public void setKcpno(String kcpno) {
            this.kcpno = kcpno;
    }

    /**
     * @return tntapesyurui
     */
    public String getTntapesyurui() {
            return tntapesyurui;
    }

    /**
     * @param tntapesyurui セットする tntapesyurui
     */
    public void setTntapesyurui(String tntapesyurui) {
            this.tntapesyurui = tntapesyurui;
    }

    /**
     * @return tntapeno
     */
    public String getTntapeno() {
            return tntapeno;
    }

    /**
     * @param tntapeno セットする tntapeno
     */
    public void setTntapeno(String tntapeno) {
            this.tntapeno = tntapeno;
    }

    /**
     * @return tntapegenryo
     */
    public String getTntapegenryo() {
            return tntapegenryo;
    }

    /**
     * @param tntapegenryo セットする tntapegenryo
     */
    public void setTntapegenryo(String tntapegenryo) {
            this.tntapegenryo = tntapegenryo;
    }

    /**
     * @return kaisinichiji
     */
    public Timestamp getKaisinichiji() {
            return kaisinichiji;
    }

    /**
     * @param kaisinichiji セットする kaisinichiji
     */
    public void setKaisinichiji(Timestamp kaisinichiji) {
            this.kaisinichiji = kaisinichiji;
    }

    /**
     * @return syuryonichiji
     */
    public Timestamp getSyuryonichiji() {
            return syuryonichiji;
    }

    /**
     * @param syuryonichiji セットする syuryonichiji
     */
    public void setSyuryonichiji(Timestamp syuryonichiji) {
            this.syuryonichiji = syuryonichiji;
    }

    /**
     * @return goki
     */
    public String getGoki() {
            return goki;
    }

    /**
     * @param goki セットする goki
     */
    public void setGoki(String goki) {
            this.goki = goki;
    }

    /**
     * @return jituaturyoku
     */
    public String getJituaturyoku() {
            return jituaturyoku;
    }

    /**
     * @param jituaturyoku セットする jituaturyoku
     */
    public void setJituaturyoku(String jituaturyoku) {
            this.jituaturyoku = jituaturyoku;
    }

    /**
     * @return sekisozure2
     */
    public Integer getSekisozure2() {
            return sekisozure2;
    }

    /**
     * @param sekisozure2 セットする sekisozure2
     */
    public void setSekisozure2(Integer sekisozure2) {
            this.sekisozure2 = sekisozure2;
    }

    /**
     * @return tantosya
     */
    public String getTantosya() {
            return tantosya;
    }

    /**
     * @param tantosya セットする tantosya
     */
    public void setTantosya(String tantosya) {
            this.tantosya = tantosya;
    }

    /**
     * @return kakuninsya
     */
    public String getKakuninsya() {
            return kakuninsya;
    }

    /**
     * @param kakuninsya セットする kakuninsya
     */
    public void setKakuninsya(String kakuninsya) {
            this.kakuninsya = kakuninsya;
    }

    /**
     * @return insaturollno
     */
    public String getInsaturollno() {
            return insaturollno;
    }

    /**
     * @param insaturollno セットする insaturollno
     */
    public void setInsaturollno(String insaturollno) {
            this.insaturollno = insaturollno;
    }

    /**
     * @return happosheetno
     */
    public String getHapposheetno() {
            return happosheetno;
    }

    /**
     * @param happosheetno セットする happosheetno
     */
    public void setHapposheetno(String happosheetno) {
            this.happosheetno = happosheetno;
    }

    /**
     * @return skjikan
     */
    public BigDecimal getSkjikan() {
            return skjikan;
    }

    /**
     * @param skjikan セットする skjikan
     */
    public void setSkjikan(BigDecimal skjikan) {
            this.skjikan = skjikan;
    }

    /**
     * @return takuto
     */
    public BigDecimal getTakuto() {
            return takuto;
    }

    /**
     * @param takuto セットする takuto
     */
    public void setTakuto(BigDecimal takuto) {
            this.takuto = takuto;
    }

    /**
     * @return biko1
     */
    public String getBiko1() {
            return biko1;
    }

    /**
     * @param biko1 セットする biko1
     */
    public void setBiko1(String biko1) {
            this.biko1 = biko1;
    }

    /**
     * @return torokunichiji
     */
    public Timestamp getTorokunichiji() {
            return torokunichiji;
    }

    /**
     * @param torokunichiji セットする torokunichiji
     */
    public void setTorokunichiji(Timestamp torokunichiji) {
            this.torokunichiji = torokunichiji;
    }

    /**
     * @return kosinnichiji
     */
    public Timestamp getKosinnichiji() {
            return kosinnichiji;
    }

    /**
     * @param kosinnichiji セットする kosinnichiji
     */
    public void setKosinnichiji(Timestamp kosinnichiji) {
            this.kosinnichiji = kosinnichiji;
    }

    /**
     * @return skojyo
     */
    public String getSkojyo() {
            return skojyo;
    }

    /**
     * @param skojyo セットする skojyo
     */
    public void setSkojyo(String skojyo) {
            this.skojyo = skojyo;
    }

    /**
     * @return slotno
     */
    public String getSlotno() {
            return slotno;
    }

    /**
     * @param slotno セットする slotno
     */
    public void setSlotno(String slotno) {
            this.slotno = slotno;
    }

    /**
     * @return sedaban
     */
    public String getSedaban() {
            return sedaban;
    }

    /**
     * @param sedaban セットする sedaban
     */
    public void setSedaban(String sedaban) {
            this.sedaban = sedaban;
    }

    /**
     * @return tapelotno
     */
    public String getTapelotno() {
            return tapelotno;
    }

    /**
     * @param tapelotno セットする tapelotno
     */
    public void setTapelotno(String tapelotno) {
            this.tapelotno = tapelotno;
    }

    /**
     * @return taperollno1
     */
    public String getTaperollno1() {
            return taperollno1;
    }

    /**
     * @param taperollno1 セットする taperollno1
     */
    public void setTaperollno1(String taperollno1) {
            this.taperollno1 = taperollno1;
    }

    /**
     * @return taperollno2
     */
    public String getTaperollno2() {
            return taperollno2;
    }

    /**
     * @param taperollno2 セットする taperollno2
     */
    public void setTaperollno2(String taperollno2) {
            this.taperollno2 = taperollno2;
    }

    /**
     * @return taperollno3
     */
    public String getTaperollno3() {
            return taperollno3;
    }

    /**
     * @param taperollno3 セットする taperollno3
     */
    public void setTaperollno3(String taperollno3) {
            this.taperollno3 = taperollno3;
    }

    /**
     * @return genryoukigou
     */
    public String getGenryoukigou() {
            return genryoukigou;
    }

    /**
     * @param genryoukigou セットする genryoukigou
     */
    public void setGenryoukigou(String genryoukigou) {
            this.genryoukigou = genryoukigou;
    }

    /**
     * @return petfilmsyurui
     */
    public String getPetfilmsyurui() {
            return petfilmsyurui;
    }

    /**
     * @param petfilmsyurui セットする petfilmsyurui
     */
    public void setPetfilmsyurui(String petfilmsyurui) {
            this.petfilmsyurui = petfilmsyurui;
    }

    /**
     * @return kotyakugouki
     */
    public String getKotyakugouki() {
            return kotyakugouki;
    }

    /**
     * @param kotyakugouki セットする kotyakugouki
     */
    public void setKotyakugouki(String kotyakugouki) {
            this.kotyakugouki = kotyakugouki;
    }

    /**
     * @return kotyakusheet
     */
    public String getKotyakusheet() {
            return kotyakusheet;
    }

    /**
     * @param kotyakusheet セットする kotyakusheet
     */
    public void setKotyakusheet(String kotyakusheet) {
            this.kotyakusheet = kotyakusheet;
    }

    /**
     * @return shitatanshigouki
     */
    public String getShitatanshigouki() {
            return shitatanshigouki;
    }

    /**
     * @param shitatanshigouki セットする shitatanshigouki
     */
    public void setShitatanshigouki(String shitatanshigouki) {
            this.shitatanshigouki = shitatanshigouki;
    }

    /**
     * @return uwatanshigouki
     */
    public String getUwatanshigouki() {
            return uwatanshigouki;
    }

    /**
     * @param uwatanshigouki セットする uwatanshigouki
     */
    public void setUwatanshigouki(String uwatanshigouki) {
            this.uwatanshigouki = uwatanshigouki;
    }

    /**
     * @return shitatanshibukunuki
     */
    public Integer getShitatanshibukunuki() {
            return shitatanshibukunuki;
    }

    /**
     * @param shitatanshibukunuki セットする shitatanshibukunuki
     */
    public void setShitatanshibukunuki(Integer shitatanshibukunuki) {
            this.shitatanshibukunuki = shitatanshibukunuki;
    }

    /**
     * @return shitatanshi
     */
    public String getShitatanshi() {
            return shitatanshi;
    }

    /**
     * @param shitatanshi セットする shitatanshi
     */
    public void setShitatanshi(String shitatanshi) {
            this.shitatanshi = shitatanshi;
    }

    /**
     * @return uwatanshi
     */
    public String getUwatanshi() {
            return uwatanshi;
    }

    /**
     * @param uwatanshi セットする uwatanshi
     */
    public void setUwatanshi(String uwatanshi) {
            this.uwatanshi = uwatanshi;
    }

    /**
     * @return syorisetsuu
     */
    public Integer getSyorisetsuu() {
        return syorisetsuu;
    }
    
    /**
     * @param syorisetsuu セットする syorisetsuu
     */
    public void setSyorisetsuu(Integer syorisetsuu) {
        this.syorisetsuu = syorisetsuu;
    }

    /**
     * @return ryouhinsetsuu
     */
    public BigDecimal getRyouhinsetsuu() {
        return ryouhinsetsuu;
    }
    
    /**
     * @param ryouhinsetsuu セットする ryouhinsetsuu
     */
    public void setRyouhinsetsuu(BigDecimal ryouhinsetsuu) {
        this.ryouhinsetsuu = ryouhinsetsuu;
    }
    
    /**
     * @return gaikankakunin1
     */
    public String getGaikankakunin1() {
            return gaikankakunin1;
    }

    /**
     * @param gaikankakunin1 セットする gaikankakunin1
     */
    public void setGaikankakunin1(String gaikankakunin1) {
            this.gaikankakunin1 = gaikankakunin1;
    }

    /**
     * @return gaikankakunin2
     */
    public String getGaikankakunin2() {
            return gaikankakunin2;
    }

    /**
     * @param gaikankakunin2 セットする gaikankakunin2
     */
    public void setGaikankakunin2(String gaikankakunin2) {
            this.gaikankakunin2 = gaikankakunin2;
    }

    /**
     * @return gaikankakunin3
     */
    public String getGaikankakunin3() {
            return gaikankakunin3;
    }

    /**
     * @param gaikankakunin3 セットする gaikankakunin3
     */
    public void setGaikankakunin3(String gaikankakunin3) {
            this.gaikankakunin3 = gaikankakunin3;
    }

    /**
     * @return gaikankakunin4
     */
    public String getGaikankakunin4() {
            return gaikankakunin4;
    }

    /**
     * @param gaikankakunin4 セットする gaikankakunin4
     */
    public void setGaikankakunin4(String gaikankakunin4) {
            this.gaikankakunin4 = gaikankakunin4;
    }    
       
    /**
     * @return endtantousyacode
     */
    public String getEndtantousyacode() {
            return endtantousyacode;
    }

    /**
     * @param endtantousyacode セットする endtantousyacode
     */
    public void setEndtantousyacode(String endtantousyacode) {
            this.endtantousyacode = endtantousyacode;
    }

    /**
     * @return tanshitapesyurui
     */
    public Integer getTanshitapesyurui() {
            return tanshitapesyurui;
    }

    /**
     * @param tanshitapesyurui セットする tanshitapesyurui
     */
    public void setTanshitapesyurui(Integer tanshitapesyurui) {
            this.tanshitapesyurui = tanshitapesyurui;
    }

    /**
     * @return hngkaisuu
     */
    public Integer getHngkaisuu() {
            return hngkaisuu;
    }

    /**
     * @param hngkaisuu セットする hngkaisuu
     */
    public void setHngkaisuu(Integer hngkaisuu) {
            this.hngkaisuu = hngkaisuu;
    }

    /**
     * @return hngkaisuuave
     */
    public BigDecimal getHngkaisuuave() {
            return hngkaisuuave;
    }

    /**
     * @param hngkaisuuave セットする hngkaisuuave
     */
    public void setHngkaisuuave(BigDecimal hngkaisuuave) {
            this.hngkaisuuave = hngkaisuuave;
    }

    /**
     * @return gngkaisuu
     */
    public Integer getGngkaisuu() {
            return gngkaisuu;
    }

    /**
     * @param gngkaisuu セットする gngkaisuu
     */
    public void setGngkaisuu(Integer gngkaisuu) {
            this.gngkaisuu = gngkaisuu;
    }

    /**
     * @return gngkaisuuave
     */
    public BigDecimal getGngkaisuuave() {
            return gngkaisuuave;
    }

    /**
     * @param gngkaisuuave セットする gngkaisuuave
     */
    public void setGngkaisuuave(BigDecimal gngkaisuuave) {
            this.gngkaisuuave = gngkaisuuave;
    }

    /**
     * @return bikou2
     */
    public String getBikou2() {
            return bikou2;
    }

    /**
     * @param bikou2 セットする bikou2
     */
    public void setBikou2(String bikou2) {
            this.bikou2 = bikou2;
    }
    
    /**
     * @return revision
     */
    public Integer getRevision() {
            return revision;
    }

    /**
     * @param revision セットする revision
     */
    public void setRevision(Integer revision) {
            this.revision = revision;
    }


    /**
     * @return setsuu
     */
    public Integer getSetsuu() {
            return setsuu;
    }


    /**
     * @param setsuu セットする setsuu
     */
    public void setSetsuu(Integer setsuu) {
            this.setsuu = setsuu;
    }


    /**
     * @return tokuisaki
     */
    public String getTokuisaki() {
            return tokuisaki;
    }


    /**
     * @param tokuisaki セットする tokuisaki
     */
    public void setTokuisaki(String tokuisaki) {
            this.tokuisaki = tokuisaki;
    }


    /**
     * @return lotkubuncode
     */
    public String getLotkubuncode() {
            return lotkubuncode;
    }


    /**
     * @param lotkubuncode セットする lotkubuncode
     */
    public void setLotkubuncode(String lotkubuncode) {
            this.lotkubuncode = lotkubuncode;
    }


    /**
     * @return ownercode
     */
    public String getOwnercode() {
            return ownercode;
    }


    /**
     * @param ownercode セットする ownercode
     */
    public void setOwnercode(String ownercode) {
            this.ownercode = ownercode;
    }


    /**
     * @return syurui3
     */
    public String getSyurui3() {
            return syurui3;
    }


    /**
     * @param syurui3 セットする syurui3
     */
    public void setSyurui3(String syurui3) {
            this.syurui3 = syurui3;
    }


    /**
     * @return atumi3
     */
    public double getAtumi3() {
            return atumi3;
    }


    /**
     * @param atumi3 セットする atumi3
     */
    public void setAtumi3(double atumi3) {
            this.atumi3 = atumi3;
    }


    /**
     * @return maisuu3
     */
    public Integer getMaisuu3() {
            return maisuu3;
    }


    /**
     * @param maisuu3 セットする maisuu3
     */
    public void setMaisuu3(Integer maisuu3) {
            this.maisuu3 = maisuu3;
    }


    /**
     * @return patern
     */
    public String getPatern() {
            return patern;
    }


    /**
     * @param patern セットする patern
     */
    public void setPatern(String patern) {
            this.patern = patern;
    }


    /**
     * @return torikosuu
     */
    public Integer getTorikosuu() {
            return torikosuu;
    }


    /**
     * @param torikosuu セットする torikosuu
     */
    public void setTorikosuu(Integer torikosuu) {
            this.torikosuu = torikosuu;
    }


    /**
     * @return etape
     */
    public String getEtape() {
            return etape;
    }


    /**
     * @param etape セットする etape
     */
    public void setEtape(String etape) {
            this.etape = etape;
    }


    /**
     * @return lretu
     */
    public Integer getLretu() {
            return lretu;
    }


    /**
     * @param lretu セットする lretu
     */
    public void setLretu(Integer lretu) {
            this.lretu = lretu;
    }


    /**
     * @return wretu
     */
    public Integer getWretu() {
            return wretu;
    }


    /**
     * @param wretu セットする wretu
     */
    public void setWretu(Integer wretu) {
            this.wretu = wretu;
    }


    /**
     * @return lsun
     */
    public BigDecimal getLsun() {
            return lsun;
    }


    /**
     * @param lsun セットする lsun
     */
    public void setLsun(BigDecimal lsun) {
            this.lsun = lsun;
    }


    /**
     * @return wsun
     */
    public BigDecimal getWsun() {
            return wsun;
    }


    /**
     * @param wsun セットする wsun
     */
    public void setWsun(BigDecimal wsun) {
            this.wsun = wsun;
    }


    /**
     * @return epaste
     */
    public String getEpaste() {
            return epaste;
    }


    /**
     * @param epaste セットする epaste
     */
    public void setEpaste(String epaste) {
            this.epaste = epaste;
    }


    /**
     * @return genryou
     */
    public String getGenryou() {
            return genryou;
    }


    /**
     * @param genryou セットする genryou
     */
    public void setGenryou(String genryou) {
            this.genryou = genryou;
    }


    /**
     * @return eatumi
     */
    public double getEatumi() {
            return eatumi;
    }


    /**
     * @param eatumi セットする eatumi
     */
    public void setEatumi(double eatumi) {
            this.eatumi = eatumi;
    }


    /**
     * @return sousuu
     */
    public double getSousuu() {
            return sousuu;
    }


    /**
     * @param sousuu セットする sousuu
     */
    public void setSousuu(double sousuu) {
            this.sousuu = sousuu;
    }


    /**
     * @return emaisuu
     */
    public Integer getEmaisuu() {
            return emaisuu;
    }


    /**
     * @param emaisuu セットする emaisuu
     */
    public void setEmaisuu(Integer emaisuu) {
            this.emaisuu = emaisuu;
    }


    /**
     * @return sekisouslideryo
     */
    public String getSekisouslideryo() {
            return sekisouslideryo;
    }


    /**
     * @param sekisouslideryo セットする sekisouslideryo
     */
    public void setSekisouslideryo(String sekisouslideryo) {
            this.sekisouslideryo = sekisouslideryo;
    }


    /**
     * @return lastlayerslideryo
     */
    public double getLastlayerslideryo() {
            return lastlayerslideryo;
    }


    /**
     * @param lastlayerslideryo セットする lastlayerslideryo
     */
    public void setLastlayerslideryo(double lastlayerslideryo) {
            this.lastlayerslideryo = lastlayerslideryo;
    }


    /**
     * @return renzokusekisoumaisuu
     */
    public Integer getRenzokusekisoumaisuu() {
            return renzokusekisoumaisuu;
    }


    /**
     * @param renzokusekisoumaisuu セットする renzokusekisoumaisuu
     */
    public void setRenzokusekisoumaisuu(Integer renzokusekisoumaisuu) {
            this.renzokusekisoumaisuu = renzokusekisoumaisuu;
    }


    /**
     * @return bsouhoseiryou
     */
    public BigDecimal getBsouhoseiryou() {
            return bsouhoseiryou;
    }


    /**
     * @param bsouhoseiryou セットする bsouhoseiryou
     */
    public void setBsouhoseiryou(BigDecimal bsouhoseiryou) {
            this.bsouhoseiryou = bsouhoseiryou;
    }


    /**
     * @return yjikuhoseiryou
     */
    public Integer getYjikuhoseiryou() {
            return yjikuhoseiryou;
    }


    /**
     * @param yjikuhoseiryou セットする yjikuhoseiryou
     */
    public void setYjikuhoseiryou(Integer yjikuhoseiryou) {
            this.yjikuhoseiryou = yjikuhoseiryou;
    }


    /**
     * @return syurui2
     */
    public String getSyurui2() {
            return syurui2;
    }


    /**
     * @param syurui2 セットする syurui2
     */
    public void setSyurui2(String syurui2) {
            this.syurui2 = syurui2;
    }


    /**
     * @return atumi2
     */
    public double getAtumi2() {
            return atumi2;
    }


    /**
     * @param atumi2 セットする atumi2
     */
    public void setAtumi2(double atumi2) {
            this.atumi2 = atumi2;
    }


    /**
     * @return maisuu2
     */
    public Integer getMaisuu2() {
            return maisuu2;
    }


    /**
     * @param maisuu2 セットする maisuu2
     */
    public void setMaisuu2(Integer maisuu2) {
            this.maisuu2 = maisuu2;
    }

    /**
     * @return 
     */
    public Timestamp getShitatanshikaisinichiji() {
        return shitatanshikaisinichiji;
    }

    /**
     * @param shitatanshikaisinichiji 
     */
    public void setShitatanshikaisinichiji(Timestamp shitatanshikaisinichiji) {
        this.shitatanshikaisinichiji = shitatanshikaisinichiji;
    }

    /**
     * @return 
     */
    public Timestamp getShitatanshisyuryonichiji() {
        return shitatanshisyuryonichiji;
    }

    /**
     * @param shitatanshisyuryonichiji 
     */
    public void setShitatanshisyuryonichiji(Timestamp shitatanshisyuryonichiji) {
        this.shitatanshisyuryonichiji = shitatanshisyuryonichiji;
    }

    /**
     * @return 
     */
    public String getShitatanshitantosya() {
        return shitatanshitantosya;
    }

    /**
     * @param shitatanshitantosya 
     */
    public void setShitatanshitantosya(String shitatanshitantosya) {
        this.shitatanshitantosya = shitatanshitantosya;
    }

    /**
     * @return 
     */
    public String getShitatanshikakuninsya() {
        return shitatanshikakuninsya;
    }

    /**
     * @param shitatanshikakuninsya 
     */
    public void setShitatanshikakuninsya(String shitatanshikakuninsya) {
        this.shitatanshikakuninsya = shitatanshikakuninsya;
    }

    /**
     * @return 
     */
    public String getShitatanshibiko() {
        return shitatanshibiko;
    }

    /**
     * @param shitatanshibiko 
     */
    public void setShitatanshibiko(String shitatanshibiko) {
        this.shitatanshibiko = shitatanshibiko;
    }

    /**
     * @return 
     */
    public Timestamp getUwatanshikaisinichiji() {
        return uwatanshikaisinichiji;
    }

    /**
     * @param uwatanshikaisinichiji 
     */
    public void setUwatanshikaisinichiji(Timestamp uwatanshikaisinichiji) {
        this.uwatanshikaisinichiji = uwatanshikaisinichiji;
    }

    /**
     * @return 
     */
    public Timestamp getUwatanshisyuryonichiji() {
        return uwatanshisyuryonichiji;
    }

    /**
     * @param uwatanshisyuryonichiji 
     */
    public void setUwatanshisyuryonichiji(Timestamp uwatanshisyuryonichiji) {
        this.uwatanshisyuryonichiji = uwatanshisyuryonichiji;
    }

    /**
     * @return 
     */
    public String getUwatanshitantosya() {
        return uwatanshitantosya;
    }

    /**
     * @param uwatanshitantosya 
     */
    public void setUwatanshitantosya(String uwatanshitantosya) {
        this.uwatanshitantosya = uwatanshitantosya;
    }

    /**
     * @return 
     */
    public String getUwatanshikakuninsya() {
        return uwatanshikakuninsya;
    }

    /**
     * @param uwatanshikakuninsya 
     */
    public void setUwatanshikakuninsya(String uwatanshikakuninsya) {
        this.uwatanshikakuninsya = uwatanshikakuninsya;
    }

    /**
     * @return 
     */
    public String getUwatanshibiko() {
        return uwatanshibiko;
    }

    /**
     * @param uwatanshibiko 
     */
    public void setUwatanshibiko(String uwatanshibiko) {
        this.uwatanshibiko = uwatanshibiko;
    }

    /**
     * @return 
     */
    public String getHeadno() {
        return headno;
    }

    /**
     * @param headno 
     */
    public void setHeadno(String headno) {
        this.headno = headno;
    }

    /**
     * @return 
     */
    public Integer getSusitamaisu() {
        return susitamaisu;
    }

    /**
     * @param susitamaisu 
     */
    public void setSusitamaisu(Integer susitamaisu) {
        this.susitamaisu = susitamaisu;
    }

    /**
     * @return 
     */
    public String getLastlayertantosya() {
        return lastlayertantosya;
    }

    /**
     * @param lastlayertantosya 
     */
    public void setLastlayertantosya(String lastlayertantosya) {
        this.lastlayertantosya = lastlayertantosya;
    }

    /**
     * @return 
     */
    public String getLastlayerbiko() {
        return lastlayerbiko;
    }

    /**
     * @param lastlayerbiko 
     */
    public void setLastlayerbiko(String lastlayerbiko) {
        this.lastlayerbiko = lastlayerbiko;
    }

    /**
     * @return 
     */
    public String getElotno() {
        return elotno;
    }

    /**
     * @param elotno 
     */
    public void setElotno(String elotno) {
        this.elotno = elotno;
    }

    /**
     * 電極製版ﾛｯﾄNo
     * @return the sekiatsu
     */
    public String getSekiatsu() {
        return sekiatsu;
    }

    /**
     * 電極製版ﾛｯﾄNo
     * @param sekiatsu the sekiatsu to set
     */
    public void setSekiatsu(String sekiatsu) {
        this.sekiatsu = sekiatsu;
    }
    
}
