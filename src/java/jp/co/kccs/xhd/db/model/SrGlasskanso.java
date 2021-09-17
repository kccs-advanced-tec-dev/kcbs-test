/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/08/30<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS wxf<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * sr_glasskanso(ｶﾞﾗｽ作製・乾燥)のモデルクラスです。
 *
 * @author KCSS wxf
 * @since 2021/08/30
 */
public class SrGlasskanso {
    
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
     * ｶﾞﾗｽ品名
     */
    private String glasshinmei;

    /**
     * ｶﾞﾗｽ品名LotNo
     */
    private String glasslotno;

    /**
     * ﾛｯﾄ区分
     */
    private String lotkubun;

    /**
     * 乾燥機
     */
    private String kansouki;

    /**
     * 乾燥条件
     */
    private String kansoujouken;

    /**
     * 振動数
     */
    private String shindousuu;

    /**
     * 玉石径
     */
    private String tamaishikei;

    /**
     * 玉石種類
     */
    private String tamaishisyurui;

    /**
     * 玉石重量
     */
    private String tamaishijuryo;

    /**
     * ｶﾞﾗｽ投入重量
     */
    private Integer tounyuujyuuryou;
    
    /**
     * 乾燥開始日時
     */
    private Timestamp kansoukaisinichiji;

    /**
     * 乾燥開始担当者
     */
    private String kaisitantosya;

    /**
     * 乾燥終了日時
     */
    private Timestamp kansousyuuryounichiji;
    
    /**
     * 乾燥終了担当者
     */
    private String syuryotantosya;
    
    /**
     * 乾燥時間
     */
    private Integer kansojikan;

    /**
     * 静置乾燥時間
     */
    private Integer seitikansoujikan;

    /**
     * 間欠振動時間
     */
    private Integer kanketusindoujikan;

    /**
     * 間欠運転時振動時間
     */
    private Integer kanketuuntenjisindoujikan;
    
    /**
     * 間欠運転時静置時間
     */
    private Integer kanketuuntenjiseitijikan;

    /**
     * ｶﾞﾗｽ回収重量
     */
    private Integer garasukaisyuujyuuryou;

    /**
     * 歩留まり
     */
    private BigDecimal budomari;

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
     * ｶﾞﾗｽ品名
     * @return the glasshinmei
     */
    public String getGlasshinmei() {
        return glasshinmei;
    }

    /**
     * ｶﾞﾗｽ品名
     * @param glasshinmei the glasshinmei to set
     */
    public void setGlasshinmei(String glasshinmei) {
        this.glasshinmei = glasshinmei;
    }

    /**
     * ｶﾞﾗｽ品名LotNo
     * @return the glasslotno
     */
    public String getGlasslotno() {
        return glasslotno;
    }

    /**
     * ｶﾞﾗｽ品名LotNo
     * @param glasslotno the glasslotno to set
     */
    public void setGlasslotno(String glasslotno) {
        this.glasslotno = glasslotno;
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
     * 乾燥機
     * @return the kansouki
     */
    public String getKansouki() {
        return kansouki;
    }

    /**
     * 乾燥機
     * @param kansouki the kansouki to set
     */
    public void setKansouki(String kansouki) {
        this.kansouki = kansouki;
    }

    /**
     * 乾燥条件
     * @return the kansoujouken
     */
    public String getKansoujouken() {
        return kansoujouken;
    }

    /**
     * 乾燥条件
     * @param kansoujouken the kansoujouken to set
     */
    public void setKansoujouken(String kansoujouken) {
        this.kansoujouken = kansoujouken;
    }

    /**
     * 振動数
     * @return the shindousuu
     */
    public String getShindousuu() {
        return shindousuu;
    }

    /**
     * 振動数
     * @param shindousuu the shindousuu to set
     */
    public void setShindousuu(String shindousuu) {
        this.shindousuu = shindousuu;
    }

    /**
     * 玉石径
     * @return the tamaishikei
     */
    public String getTamaishikei() {
        return tamaishikei;
    }

    /**
     * 玉石径
     * @param tamaishikei the tamaishikei to set
     */
    public void setTamaishikei(String tamaishikei) {
        this.tamaishikei = tamaishikei;
    }

    /**
     * 玉石種類
     * @return the tamaishisyurui
     */
    public String getTamaishisyurui() {
        return tamaishisyurui;
    }

    /**
     * 玉石種類
     * @param tamaishisyurui the tamaishisyurui to set
     */
    public void setTamaishisyurui(String tamaishisyurui) {
        this.tamaishisyurui = tamaishisyurui;
    }

    /**
     * 玉石重量
     * @return the tamaishijuryo
     */
    public String getTamaishijuryo() {
        return tamaishijuryo;
    }

    /**
     * 玉石重量
     * @param tamaishijuryo the tamaishijuryo to set
     */
    public void setTamaishijuryo(String tamaishijuryo) {
        this.tamaishijuryo = tamaishijuryo;
    }

    /**
     * ｶﾞﾗｽ投入重量
     * @return the tounyuujyuuryou
     */
    public Integer getTounyuujyuuryou() {
        return tounyuujyuuryou;
    }

    /**
     * ｶﾞﾗｽ投入重量
     * @param tounyuujyuuryou the tounyuujyuuryou to set
     */
    public void setTounyuujyuuryou(Integer tounyuujyuuryou) {
        this.tounyuujyuuryou = tounyuujyuuryou;
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
     * 乾燥開始担当者
     * @return the kaisitantosya
     */
    public String getKaisitantosya() {
        return kaisitantosya;
    }

    /**
     * 乾燥開始担当者
     * @param kaisitantosya the kaisitantosya to set
     */
    public void setKaisitantosya(String kaisitantosya) {
        this.kaisitantosya = kaisitantosya;
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
     * 乾燥終了担当者
     * @return the syuryotantosya
     */
    public String getSyuryotantosya() {
        return syuryotantosya;
    }

    /**
     * 乾燥終了担当者
     * @param syuryotantosya the syuryotantosya to set
     */
    public void setSyuryotantosya(String syuryotantosya) {
        this.syuryotantosya = syuryotantosya;
    }

    /**
     * 乾燥時間
     * @return the kansojikan
     */
    public Integer getKansojikan() {
        return kansojikan;
    }

    /**
     * 乾燥時間
     * @param kansojikan the kansojikan to set
     */
    public void setKansojikan(Integer kansojikan) {
        this.kansojikan = kansojikan;
    }

    /**
     * 静置乾燥時間
     * @return the seitikansoujikan
     */
    public Integer getSeitikansoujikan() {
        return seitikansoujikan;
    }

    /**
     * 静置乾燥時間
     * @param seitikansoujikan the seitikansoujikan to set
     */
    public void setSeitikansoujikan(Integer seitikansoujikan) {
        this.seitikansoujikan = seitikansoujikan;
    }

    /**
     * 間欠振動時間
     * @return the kanketusindoujikan
     */
    public Integer getKanketusindoujikan() {
        return kanketusindoujikan;
    }

    /**
     * 間欠振動時間
     * @param kanketusindoujikan the kanketusindoujikan to set
     */
    public void setKanketusindoujikan(Integer kanketusindoujikan) {
        this.kanketusindoujikan = kanketusindoujikan;
    }

    /**
     * 間欠運転時振動時間
     * @return the kanketuuntenjisindoujikan
     */
    public Integer getKanketuuntenjisindoujikan() {
        return kanketuuntenjisindoujikan;
    }

    /**
     * 間欠運転時振動時間
     * @param kanketuuntenjisindoujikan the kanketuuntenjisindoujikan to set
     */
    public void setKanketuuntenjisindoujikan(Integer kanketuuntenjisindoujikan) {
        this.kanketuuntenjisindoujikan = kanketuuntenjisindoujikan;
    }

    /**
     * 間欠運転時静置時間
     * @return the kanketuuntenjiseitijikan
     */
    public Integer getKanketuuntenjiseitijikan() {
        return kanketuuntenjiseitijikan;
    }

    /**
     * 間欠運転時静置時間
     * @param kanketuuntenjiseitijikan the kanketuuntenjiseitijikan to set
     */
    public void setKanketuuntenjiseitijikan(Integer kanketuuntenjiseitijikan) {
        this.kanketuuntenjiseitijikan = kanketuuntenjiseitijikan;
    }

    /**
     * ｶﾞﾗｽ回収重量
     * @return the garasukaisyuujyuuryou
     */
    public Integer getGarasukaisyuujyuuryou() {
        return garasukaisyuujyuuryou;
    }

    /**
     * ｶﾞﾗｽ回収重量
     * @param garasukaisyuujyuuryou the garasukaisyuujyuuryou to set
     */
    public void setGarasukaisyuujyuuryou(Integer garasukaisyuujyuuryou) {
        this.garasukaisyuujyuuryou = garasukaisyuujyuuryou;
    }

    /**
     * 歩留まり
     * @return the budomari
     */
    public BigDecimal getBudomari() {
        return budomari;
    }

    /**
     * 歩留まり
     * @param budomari the budomari to set
     */
    public void setBudomari(BigDecimal budomari) {
        this.budomari = budomari;
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
    public Long getRevision() {
        return revision;
    }

    /**
     * revision
     * @param revision the revision to set
     */
    public void setRevision(Long revision) {
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
