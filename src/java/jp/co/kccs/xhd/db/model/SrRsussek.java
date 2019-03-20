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
}
