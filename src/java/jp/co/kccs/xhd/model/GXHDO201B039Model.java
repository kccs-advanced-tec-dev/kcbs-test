/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2019/12/02<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	863 K.Zhang<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 外部電極・ﾒｯｷ真空乾燥履歴検索画面のモデルクラスです。
 *
 * @author 863 K.Zhang
 * @since  2019/12/02
 */
public class GXHDO201B039Model implements Serializable{

    /** ﾛｯﾄNo. */
    private String lotno = "";

    /** KCPNO */
    private String kcpno = "";
    
    /** 号機 */
    private String goukicode = "";
    
    /** 回数 */
    private Integer syorikaisuu = null;

    /** 開始担当者 */
    private String tantousyacode = "";

    /** 乾燥時間 */
    private String kansoujikan = "";

    /** 温度 */
    private String ondo = "";

    /** 真空度 */
    private String sinkuudo = "";

    /** 開始日時 */
    private Timestamp kaisinichiji = null;

    /** 備考1 */
    private String bikou = "";

    /** 客先 */
    private String tokuisaki = "";

    /** ﾛｯﾄ区分 */
    private String lotkubuncode = "";

    /** ｵｰﾅｰ */
    private String ownercode = "";

    /** 処理数 */
    private Integer syorisuu = null;

    /** 作業場所 */
    private String sagyoubasyo = "";

    /** ﾊﾞﾚﾙ洗浄開始日時 */
    private Timestamp barrelkaishinichiji = null;

    /** ﾊﾞﾚﾙ洗浄担当者 */
    private String barreltantousya = "";
    
    /** 備考2 */
    private String bikou2 = "";

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
     * @return goukicode
     */
    public String getGoukicode() {
        return goukicode;
    }

    /**
     * @param goukicode セットする goukicode
     */
    public void setGoukicode(String goukicode) {
        this.goukicode = goukicode;
    }

    /**
     * @return syorikaisuu
     */
    public Integer getSyorikaisuu() {
        return syorikaisuu;
    }

    /**
     * @param syorikaisuu セットする syorikaisuu
     */
    public void setSyorikaisuu(Integer syorikaisuu) {
        this.syorikaisuu = syorikaisuu;
    }

    /**
     * @return tantousyacode
     */
    public String getTantousyacode() {
        return tantousyacode;
    }

    /**
     * @param tantousyacode セットする tantousyacode
     */
    public void setTantousyacode(String tantousyacode) {
        this.tantousyacode = tantousyacode;
    }

    /**
     * @return kansoujikan
     */
    public String getKansoujikan() {
        return kansoujikan;
    }

    /**
     * @param kansoujikan セットする kansoujikan
     */
    public void setKansoujikan(String kansoujikan) {
        this.kansoujikan = kansoujikan;
    }

    /**
     * @return ondo
     */
    public String getOndo() {
        return ondo;
    }

    /**
     * @param ondo セットする ondo
     */
    public void setOndo(String ondo) {
        this.ondo = ondo;
    }

    /**
     * @return sinkuudo
     */
    public String getSinkuudo() {
        return sinkuudo;
    }

    /**
     * @param sinkuudo セットする sinkuudo
     */
    public void setSinkuudo(String sinkuudo) {
        this.sinkuudo = sinkuudo;
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
     * @return bikou
     */
    public String getBikou() {
        return bikou;
    }

    /**
     * @param bikou セットする bikou
     */
    public void setBikou(String bikou) {
        this.bikou = bikou;
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
     * @return syorisuu
     */
    public Integer getSyorisuu() {
        return syorisuu;
    }

    /**
     * @param syorisuu セットする syorisuu
     */
    public void setSyorisuu(Integer syorisuu) {
        this.syorisuu = syorisuu;
    }

    /**
     * @return sagyoubasyo
     */
    public String getSagyoubasyo() {
        return sagyoubasyo;
    }

    /**
     * @param sagyoubasyo セットする sagyoubasyo
     */
    public void setSagyoubasyo(String sagyoubasyo) {
        this.sagyoubasyo = sagyoubasyo;
    }

    /**
     * @return barrelkaishinichiji
     */
    public Timestamp getBarrelkaishinichiji() {
        return barrelkaishinichiji;
    }

    /**
     * @param barrelkaishinichiji セットする barrelkaishinichiji
     */
    public void setBarrelkaishinichiji(Timestamp barrelkaishinichiji) {
        this.barrelkaishinichiji = barrelkaishinichiji;
    }

    /**
     * @return barreltantousya
     */
    public String getBarreltantousya() {
        return barreltantousya;
    }

    /**
     * @param barreltantousya セットする barreltantousya
     */
    public void setBarreltantousya(String barreltantousya) {
        this.barreltantousya = barreltantousya;
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

   

}
