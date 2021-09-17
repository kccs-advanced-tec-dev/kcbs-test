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
 * 変更日	Deng.H<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B002(ｶﾞﾗｽ作製・SC粉砕)
 *
 * @author KCSS K.Jo
 * @since  2021/08/31
 */
public class SrGlassscfunsai {

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
     * 粉砕機
     */
    private String funsaiki;

    /**
     * 玉石径
     */
    private String tamaishikei;

    /**
     * 玉石重量
     */
    private String tamaishijuryo;

    /**
     * 回転数
     */
    private String kaitensuu;

    /**
     * 循環周波数
     */
    private String junkanshuhasuu;

    /**
     * 粉砕時間
     */
    private String funsaijikan;

    /**
     * 累計稼働時間
     */
    private BigDecimal kadoujikan;

    /**
     * ｼｰﾙ液量確認
     */
    private Integer siruekikakunin;

    /**
     * 粉砕開始日時
     */
    private Timestamp funsaikaisinichiji;

    /**
     * 開始担当者
     */
    private String kaisitantosya;

    /**
     * 粉砕終了予定日時
     */
    private Timestamp funsaisyuuryouyoteinichiji;

    /**
     * 負荷電流値(10min)
     */
    private Integer fukadenryuuti10min;

    /**
     * ﾐﾙ出口液温(10min)
     */
    private Integer mirudegutiekion10min;

    /**
     * 内圧(10min)
     */
    private BigDecimal naiatu10min;

    /**
     * ﾎﾟﾝﾌﾟ周波数(10min)
     */
    private Integer ponpusyuuhasuu10min;

    /**
     * 粉砕終了日時
     */
    private Timestamp funsaisyuuryounichiji;

    /**
     * 終了担当者
     */
    private String syuryotantosya;

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
     * 粉砕機
     * @return the funsaiki
     */
    public String getFunsaiki() {
        return funsaiki;
    }

    /**
     * 粉砕機
     * @param funsaiki the funsaiki to set
     */
    public void setFunsaiki(String funsaiki) {
        this.funsaiki = funsaiki;
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
     * 回転数
     * @return the kaitensuu
     */
    public String getKaitensuu() {
        return kaitensuu;
    }

    /**
     * 回転数
     * @param kaitensuu the kaitensuu to set
     */
    public void setKaitensuu(String kaitensuu) {
        this.kaitensuu = kaitensuu;
    }

    /**
     * 循環周波数
     * @return the junkanshuhasuu
     */
    public String getJunkanshuhasuu() {
        return junkanshuhasuu;
    }

    /**
     * 循環周波数
     * @param junkanshuhasuu the junkanshuhasuu to set
     */
    public void setJunkanshuhasuu(String junkanshuhasuu) {
        this.junkanshuhasuu = junkanshuhasuu;
    }

    /**
     * 粉砕時間
     * @return the funsaijikan
     */
    public String getFunsaijikan() {
        return funsaijikan;
    }

    /**
     * 粉砕時間
     * @param funsaijikan the funsaijikan to set
     */
    public void setFunsaijikan(String funsaijikan) {
        this.funsaijikan = funsaijikan;
    }

    /**
     * 累計稼働時間
     * @return the kadoujikan
     */
    public BigDecimal getKadoujikan() {
        return kadoujikan;
    }

    /**
     * 累計稼働時間
     * @param kadoujikan the kadoujikan to set
     */
    public void setKadoujikan(BigDecimal kadoujikan) {
        this.kadoujikan = kadoujikan;
    }

    /**
     * ｼｰﾙ液量確認
     * @return the siruekikakunin
     */
    public Integer getSiruekikakunin() {
        return siruekikakunin;
    }

    /**
     * ｼｰﾙ液量確認
     * @param siruekikakunin the siruekikakunin to set
     */
    public void setSiruekikakunin(Integer siruekikakunin) {
        this.siruekikakunin = siruekikakunin;
    }

    /**
     * 粉砕開始日時
     * @return the funsaikaisinichiji
     */
    public Timestamp getFunsaikaisinichiji() {
        return funsaikaisinichiji;
    }

    /**
     * 粉砕開始日時
     * @param funsaikaisinichiji the funsaikaisinichiji to set
     */
    public void setFunsaikaisinichiji(Timestamp funsaikaisinichiji) {
        this.funsaikaisinichiji = funsaikaisinichiji;
    }

    /**
     * 開始担当者
     * @return the kaisitantosya
     */
    public String getKaisitantosya() {
        return kaisitantosya;
    }

    /**
     * 開始担当者
     * @param kaisitantosya the kaisitantosya to set
     */
    public void setKaisitantosya(String kaisitantosya) {
        this.kaisitantosya = kaisitantosya;
    }

    /**
     * 粉砕終了予定日時
     * @return the funsaisyuuryouyoteinichiji
     */
    public Timestamp getFunsaisyuuryouyoteinichiji() {
        return funsaisyuuryouyoteinichiji;
    }

    /**
     * 粉砕終了予定日時
     * @param funsaisyuuryouyoteinichiji the funsaisyuuryouyoteinichiji to set
     */
    public void setFunsaisyuuryouyoteinichiji(Timestamp funsaisyuuryouyoteinichiji) {
        this.funsaisyuuryouyoteinichiji = funsaisyuuryouyoteinichiji;
    }

    /**
     * 負荷電流値(10min)
     * @return the fukadenryuuti10min
     */
    public Integer getFukadenryuuti10min() {
        return fukadenryuuti10min;
    }

    /**
     * 負荷電流値(10min)
     * @param fukadenryuuti10min the fukadenryuuti10min to set
     */
    public void setFukadenryuuti10min(Integer fukadenryuuti10min) {
        this.fukadenryuuti10min = fukadenryuuti10min;
    }

    /**
     * ﾐﾙ出口液温(10min)
     * @return the mirudegutiekion10min
     */
    public Integer getMirudegutiekion10min() {
        return mirudegutiekion10min;
    }

    /**
     * ﾐﾙ出口液温(10min)
     * @param mirudegutiekion10min the mirudegutiekion10min to set
     */
    public void setMirudegutiekion10min(Integer mirudegutiekion10min) {
        this.mirudegutiekion10min = mirudegutiekion10min;
    }

    /**
     * 内圧(10min)
     * @return the naiatu10min
     */
    public BigDecimal getNaiatu10min() {
        return naiatu10min;
    }

    /**
     * 内圧(10min)
     * @param naiatu10min the naiatu10min to set
     */
    public void setNaiatu10min(BigDecimal naiatu10min) {
        this.naiatu10min = naiatu10min;
    }

    /**
     * ﾎﾟﾝﾌﾟ周波数(10min)
     * @return the ponpusyuuhasuu10min
     */
    public Integer getPonpusyuuhasuu10min() {
        return ponpusyuuhasuu10min;
    }

    /**
     * ﾎﾟﾝﾌﾟ周波数(10min)
     * @param ponpusyuuhasuu10min the ponpusyuuhasuu10min to set
     */
    public void setPonpusyuuhasuu10min(Integer ponpusyuuhasuu10min) {
        this.ponpusyuuhasuu10min = ponpusyuuhasuu10min;
    }

    /**
     * 粉砕終了日時
     * @return the funsaisyuuryounichiji
     */
    public Timestamp getFunsaisyuuryounichiji() {
        return funsaisyuuryounichiji;
    }

    /**
     * 粉砕終了日時
     * @param funsaisyuuryounichiji the funsaisyuuryounichiji to set
     */
    public void setFunsaisyuuryounichiji(Timestamp funsaisyuuryounichiji) {
        this.funsaisyuuryounichiji = funsaisyuuryounichiji;
    }

    /**
     * 終了担当者
     * @return the syuryotantosya
     */
    public String getSyuryotantosya() {
        return syuryotantosya;
    }

    /**
     * 終了担当者
     * @param syuryotantosya the syuryotantosya to set
     */
    public void setSyuryotantosya(String syuryotantosya) {
        this.syuryotantosya = syuryotantosya;
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
