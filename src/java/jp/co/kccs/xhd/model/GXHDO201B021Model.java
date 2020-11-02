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
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2019/08/05<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	863 K.Zhang<br>
 * 変更理由	新規作成<br>
 * <br>
 * <br>
 * 変更日	2020/09/18<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	863 sujialiang<br>
 * 変更理由	項目追加<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 研磨・計数履歴検索画面のモデルクラスです。
 *
 * @author K
 * @since 2019/08/05
 */
public class GXHDO201B021Model implements Serializable {
    /** ﾛｯﾄNo. */
    private String lotno = "";
    /** KCPNO */
    private String kcpno = "";
    /** 実績No */
    private Integer jissekion = null;
    /** 袋詰め担当者 */
    private String fukurotantosya = "";
    /** 受入個数 */
    private Integer ukeirekosu = null;
    /** 単位重量 */
    private BigDecimal tanijyuryo = null;
    /** 総重量 */
    private BigDecimal soujuryou = null;
    /** 良品個数 */
    private Integer ryohinkosu = null;
    /** 計数日時 */
    private Timestamp keisunichiji = null;
    /** 計数担当者 */
    private String keisuutantosya = "";
    /** 歩留まり */
    private BigDecimal budomari = null;
    /** 備考1 */
    private String biko1 = "";
    /** 備考2 */
    private String biko2 = "";
    /** ﾊﾞﾚﾙ後判定 */
    private String barrelgohantei = "";
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
     * 実績No
     * @return the jissekion
     */
    public Integer getJissekion() {
        return jissekion;
    }

    /**
     * 実績No
     * @param jissekion the jissekion to set
     */
    public void setJissekion(Integer jissekion) {
        this.jissekion = jissekion;
    }

    /**
     * 袋詰め担当者
     * @return fukurotantosya
     */
    public String getFukurotantosya() {
        return fukurotantosya;
    }

    /**
     * 袋詰め担当者
     * @param fukurotantosya 
     */
    public void setFukurotantosya(String fukurotantosya) {
        this.fukurotantosya = fukurotantosya;
    }

    /**
     * 受入個数
     * @return the ukeirekosu
     */
    public Integer getUkeirekosu() {
        return ukeirekosu;
    }

    /**
     * 受入個数
     * @param ukeirekosu the ukeirekosu to set
     */
    public void setUkeirekosu(Integer ukeirekosu) {
        this.ukeirekosu = ukeirekosu;
    }
    /**
     * 単位重量
     * @return the tanijyuryo
     */
    public BigDecimal getTanijyuryo() {
        return tanijyuryo;
    }

    /**
     * 単位重量
     * @param tanijyuryo the tanijyuryo to set
     */
    public void setTanijyuryo(BigDecimal tanijyuryo) {
        this.tanijyuryo = tanijyuryo;
    }
    
    /**
     * 総重量
     * @return the soujuryou
     */
    public BigDecimal getSoujuryou() {
        return soujuryou;
    }

    /**
     * 総重量
     * @param soujuryou the soujuryou to set
     */
    public void setSoujuryou(BigDecimal soujuryou) {
        this.soujuryou = soujuryou;
    }
    /**
     * 良品個数
     * @return the ryohinkosu
     */
    public Integer getRyohinkosu() {
        return ryohinkosu;
    }

    /**
     * 良品個数
     * @param ryohinkosu the ryohinkosu to set
     */
    public void setRyohinkosu(Integer ryohinkosu) {
        this.ryohinkosu = ryohinkosu;
    }
    /**
     * 計数日時
     * @return the keisunichiji
     */
    public Timestamp getKeisunichiji() {
        return keisunichiji;
    }

    /**
     * 計数日時
     * @param keisunichiji the keisunichiji to set
     */
    public void setKeisunichiji(Timestamp keisunichiji) {
        this.keisunichiji = keisunichiji;
    }
    /**
     * 計数担当者
     * @return the keisuutantosya
     */
    public String getKeisuutantosya() {
        return keisuutantosya;
    }

    /**
     * 計数担当者
     * @param keisuutantosya the keisuutantosya to set
     */
    public void setKeisuutantosya(String keisuutantosya) {
        this.keisuutantosya = keisuutantosya;
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
     * 備考2
     * @return the biko2
     */
    public String getBiko2() {
        return biko2;
    }

    /**
     * 備考2
     * @param biko2 the biko2 to set
     */
    public void setBiko2(String biko2) {
        this.biko2 = biko2;
    }
    
    /**
     * ﾊﾞﾚﾙ後判定
     * @return the barrelgohantei
     */
    public String getBarrelgohantei() {
        return barrelgohantei;
    }
    
    /**
     * ﾊﾞﾚﾙ後判定
     * @param barrelgohantei the barrelgohantei to set
     */
    public void setBarrelgohantei(String barrelgohantei) {
        this.barrelgohantei = barrelgohantei;
    }
}
