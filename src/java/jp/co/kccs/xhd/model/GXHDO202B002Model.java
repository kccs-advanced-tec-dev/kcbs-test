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
 * 変更日       2021/9/10<br>
 * 計画書No     MB2101-DK002<br>
 * 変更者       KCSS K.Jo<br>
 * 変更理由     新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * ｶﾞﾗｽ作製・SC粉砕履歴検索画面のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/09/10
 */
public class GXHDO202B002Model implements Serializable {
    /** WIPﾛｯﾄNo */
    private String lotno = "";
    
    /** ｶﾞﾗｽ品名 */
    private String glasshinmei = "";
    
    /** ｶﾞﾗｽ品名LotNo */
    private String glasslotno = "";
    
    /** ﾛｯﾄ区分 */
    private String lotkubun = "";
    
    /** 粉砕機 */
    private String funsaiki = "";
    
    /** 玉石径 */
    private String tamaishikei = "";
    
    /** 玉石重量 */
    private String tamaishijuryo = "";
    
    /** 回転数 */
    private String kaitensuu = "";
    
    /** 循環周波数 */
    private String junkanshuhasuu = "";
    
    /** 粉砕時間 */
    private String funsaijikan = "";
    
    /** 累計稼働時間 */
    private BigDecimal kadoujikan = null;
    
    /** ｼｰﾙ液量確認 */
    private String si_ruekikakunin = "";
    
    /** 粉砕開始日時 */
    private Timestamp funsaikaisinichiji = null;
    
    /** 粉砕開始担当者 */
    private String kaisitantosya = "";
    
    /** 粉砕終了予定日時 */
    private Timestamp funsaisyuuryouyoteinichiji = null;
    
    /** 負荷電流値 */
    private Integer fukadenryuuti_10min = null;
    
    /** ﾐﾙ出口液温 */
    private Integer mirudegutiekion_10min = null;
    
    /** 内圧 */
    private BigDecimal naiatu_10min = null;
    
    /** ﾎﾟﾝﾌﾟ周波数 */
    private Integer ponpusyuuhasuu_10min = null;
    
    /** 粉砕終了日時 */
    private Timestamp funsaisyuuryounichiji = null;
    
    /** 粉砕終了担当者 */
    private String syuryotantosya = "";
    
    /** 備考1 */
    private String bikou1 = "";
    
    /** 備考2 */
    private String bikou2 = "";

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
     * @return the si_ruekikakunin
     */
    public String getSi_ruekikakunin() {
        return si_ruekikakunin;
    }

    /**
     * ｼｰﾙ液量確認
     * @param si_ruekikakunin the si_ruekikakunin to set
     */
    public void setSi_ruekikakunin(String si_ruekikakunin) {
        this.si_ruekikakunin = si_ruekikakunin;
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
     * 粉砕開始担当者
     * @return the kaisitantosya
     */
    public String getKaisitantosya() {
        return kaisitantosya;
    }

    /**
     * 粉砕開始担当者
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
     * 負荷電流値
     * @return the fukadenryuuti_10min
     */
    public Integer getFukadenryuuti_10min() {
        return fukadenryuuti_10min;
    }

    /**
     * 負荷電流値
     * @param fukadenryuuti_10min the fukadenryuuti_10min to set
     */
    public void setFukadenryuuti_10min(Integer fukadenryuuti_10min) {
        this.fukadenryuuti_10min = fukadenryuuti_10min;
    }

    /**
     * ﾐﾙ出口液温
     * @return the mirudegutiekion_10min
     */
    public Integer getMirudegutiekion_10min() {
        return mirudegutiekion_10min;
    }

    /**
     * ﾐﾙ出口液温
     * @param mirudegutiekion_10min the mirudegutiekion_10min to set
     */
    public void setMirudegutiekion_10min(Integer mirudegutiekion_10min) {
        this.mirudegutiekion_10min = mirudegutiekion_10min;
    }

    /**
     * 内圧
     * @return the naiatu_10min
     */
    public BigDecimal getNaiatu_10min() {
        return naiatu_10min;
    }

    /**
     * 内圧
     * @param naiatu_10min the naiatu_10min to set
     */
    public void setNaiatu_10min(BigDecimal naiatu_10min) {
        this.naiatu_10min = naiatu_10min;
    }

    /**
     * ﾎﾟﾝﾌﾟ周波数
     * @return the ponpusyuuhasuu_10min
     */
    public Integer getPonpusyuuhasuu_10min() {
        return ponpusyuuhasuu_10min;
    }

    /**
     * ﾎﾟﾝﾌﾟ周波数
     * @param ponpusyuuhasuu_10min the ponpusyuuhasuu_10min to set
     */
    public void setPonpusyuuhasuu_10min(Integer ponpusyuuhasuu_10min) {
        this.ponpusyuuhasuu_10min = ponpusyuuhasuu_10min;
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
     * 粉砕終了担当者
     * @return the syuryotantosya
     */
    public String getSyuryotantosya() {
        return syuryotantosya;
    }

    /**
     * 粉砕終了担当者
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

}