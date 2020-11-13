/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.kccs.xhd.model;

import java.io.Serializable;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2020/08/29<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	KCCS R.yamakiri<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 設計仕様検索画面のモデルクラスです。
 *
 * @author KCCS R.yamakiri
 * @since 2020/08/29
 */
public class GXHDO301AModel implements Serializable {
    
   /** ﾛｯﾄNo */
    private String lotno = "";
    /** KCPNO */
    private String hinmei = "";
    /** ｾｯﾄ数 */
    private String setsuu = "";    
    /** 客先 */
    private String tokuisaki = "";
    /** ﾛｯﾄ区分 */
    private String kubun1 = "";
    /** 電極ﾃｰﾌﾟ上段 */
    private String etapetop = "";
    /** 電極ﾃｰﾌﾟ下段 */
    private String etapebuttom = "";
    /** 最上層 */
    private String abslide = "";
    /** 印刷ﾛｰﾙNo */
    private String rollno = "";
    /** ｶﾊﾞｰﾃｰﾌﾟ仕様 */
    private String covertape = "";
    /** 上ｶﾊﾞｰﾃｰﾌﾟ1 */
    private String ctcovertape1 = "";
    /** 上ｶﾊﾞｰﾃｰﾌﾟ2 */
    private String stcovertape2 = "";
    /** 下ｶﾊﾞｰﾃｰﾌﾟ1 */
    private String cbcovertape1 = "";
    /** 下ｶﾊﾞｰﾃｰﾌﾟ2 */
    private String sbcovertape2 = "";
    /** 誘電体ﾍﾟｰｽﾄ */
    private String ypaste = "";
    /** PETﾌｨﾙﾑ種類 */
    private String petfilm = "";
    /** 固着ｼｰﾄ */
    private String kochakusite = "";
    /** ｵｰﾅｰ */
    private String owner = "";
    /** 電極ﾍﾟｰｽﾄ */
    private String epaste = "";
    /** 電極製版名 */
    private String eseihanmei = "";
    /** 電極製版仕様 */
    private String eseihan = "";
    /** 電極製版ﾛｯﾄ */
    private String scrnlot = "";
    /** 積層ｽﾗｲﾄﾞ量 */
    private String sekislide = "";
    /** 誘電体製版名 */
    private String yseihanmei ="";
    /** 誘電体製版仕様 */
    private String yseihan = "";
    /** 指定公差 */
    private String kousa = "";
    /** 取り個数 */
    private String torikosuu = "";
    /** 個数 */
    private String kosuu = "";
    /** 個数 */
    private String goki = "";
        
    /**
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * @param lotno the lotno to set
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * @return the hinmei
     */
    public String getHinmei() {
        return hinmei;
    }

    /**
     * @param hinmei the hinmei to set
     */
    public void setHinmei(String hinmei) {
        this.hinmei = hinmei;
    }

    /**
     * @return the setsuu
     */
    public String getSetsuu() {
        return setsuu;
    }

    /**
     * @param setsuu the setsuu to set
     */
    public void setSetsuu(String setsuu) {
        this.setsuu = setsuu;
    }

    /**
     * @return the tokuisaki
     */
    public String getTokuisaki() {
        return tokuisaki;
    }

    /**
     * @param tokuisaki the tokuisaki to set
     */
    public void setTokuisaki(String tokuisaki) {
        this.tokuisaki = tokuisaki;
    }

    /**
     * @return the kubun1
     */
    public String getKubun1() {
        return kubun1;
    }

    /**
     * @param kubun1 the kubun1 to set
     */
    public void setKubun1(String kubun1) {
        this.kubun1 = kubun1;
    }

    /**
     * @return the etapetop
     */
    public String getEtapetop() {
        return etapetop;
    }

    /**
     * @param etapetop the etapetop to set
     */
    public void setEtapetop(String etapetop) {
        this.etapetop = etapetop;
    }

    /**
     * @return the etapebuttom
     */
    public String getEtapebuttom() {
        return etapebuttom;
    }

    /**
     * @param etapebuttom the etapebuttom to set
     */
    public void setEtapebuttom(String etapebuttom) {
        this.etapebuttom = etapebuttom;
    }

    /**
     * @return the abslide
     */
    public String getAbslide() {
        return abslide;
    }

    /**
     * @param abslide the abslide to set
     */
    public void setAbslide(String abslide) {
        this.abslide = abslide;
    }

    /**
     * @return the rollno
     */
    public String getRollno() {
        return rollno;
    }

    /**
     * @param rollno the rollno to set
     */
    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    /**
     * @return the covertape
     */
    public String getCovertape() {
        return covertape;
    }

    /**
     * @param covertape the covertape to set
     */
    public void setCovertape(String covertape) {
        this.covertape = covertape;
    }

    /**
     * @return the ctcovertape1
     */
    public String getCtcovertape1() {
        return ctcovertape1;
    }

    /**
     * @param ctcovertape1 the ctcovertape1 to set
     */
    public void setCtcovertape1(String ctcovertape1) {
        this.ctcovertape1 = ctcovertape1;
    }

    /**
     * @return the stcovertape2
     */
    public String getStcovertape2() {
        return stcovertape2;
    }

    /**
     * @param stcovertape2 the stcovertape2 to set
     */
    public void setStcovertape2(String stcovertape2) {
        this.stcovertape2 = stcovertape2;
    }

    /**
     * @return the cbcovertape1
     */
    public String getCbcovertape1() {
        return cbcovertape1;
    }

    /**
     * @param cbcovertape1 the cbcovertape1 to set
     */
    public void setCbcovertape1(String cbcovertape1) {
        this.cbcovertape1 = cbcovertape1;
    }

    /**
     * @return the sbcovertape2
     */
    public String getSbcovertape2() {
        return sbcovertape2;
    }

    /**
     * @param sbcovertape2 the sbcovertape2 to set
     */
    public void setSbcovertape2(String sbcovertape2) {
        this.sbcovertape2 = sbcovertape2;
    }

    /**
     * @return the ypaste
     */
    public String getYpaste() {
        return ypaste;
    }

    /**
     * @param ypaste the ypaste to set
     */
    public void setYpaste(String ypaste) {
        this.ypaste = ypaste;
    }

    /**
     * @return the petfilm
     */
    public String getPetfilm() {
        return petfilm;
    }

    /**
     * @param petfilm the petfilm to set
     */
    public void setPetfilm(String petfilm) {
        this.petfilm = petfilm;
    }

    /**
     * @return the kochakusite
     */
    public String getKochakusite() {
        return kochakusite;
    }

    /**
     * @param kochakusite the kochakusite to set
     */
    public void setKochakusite(String kochakusite) {
        this.kochakusite = kochakusite;
    }

    /**
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * @return the epaste
     */
    public String getEpaste() {
        return epaste;
    }

    /**
     * @param epaste the epaste to set
     */
    public void setEpaste(String epaste) {
        this.epaste = epaste;
    }

    /**
     * @return the eseihanmei
     */
    public String getEseihanmei() {
        return eseihanmei;
    }

    /**
     * @param eseihanmei the eseihanmei to set
     */
    public void setEseihanmei(String eseihanmei) {
        this.eseihanmei = eseihanmei;
    }

    /**
     * @return the eseihan
     */
    public String getEseihan() {
        return eseihan;
    }

    /**
     * @param eseihan the eseihan to set
     */
    public void setEseihan(String eseihan) {
        this.eseihan = eseihan;
    }

    /**
     * @return the scrnlot
     */
    public String getScrnlot() {
        return scrnlot;
    }

    /**
     * @param scrnlot the scrnlot to set
     */
    public void setScrnlot(String scrnlot) {
        this.scrnlot = scrnlot;
    }

    /**
     * @return the sekislide
     */
    public String getSekislide() {
        return sekislide;
    }

    /**
     * @param sekislide the sekislide to set
     */
    public void setSekislide(String sekislide) {
        this.sekislide = sekislide;
    }

    /**
     * @return the yseihanmei
     */
    public String getYseihanmei() {
        return yseihanmei;
    }

    /**
     * @param yseihanmei the yseihanmei to set
     */
    public void setYseihanmei(String yseihanmei) {
        this.yseihanmei = yseihanmei;
    }

    /**
     * @return the yseihan
     */
    public String getYseihan() {
        return yseihan;
    }

    /**
     * @param yseihan the yseihan to set
     */
    public void setYseihan(String yseihan) {
        this.yseihan = yseihan;
    }

    /**
     * @return the kousa
     */
    public String getKousa() {
        return kousa;
    }

    /**
     * @param kousa the kousa to set
     */
    public void setKousa(String kousa) {
        this.kousa = kousa;
    }

    /**
     * @return the torikosuu
     */
    public String getTorikosuu() {
        return torikosuu;
    }

    /**
     * @param torikosuu the torikosuu to set
     */
    public void setTorikosuu(String torikosuu) {
        this.torikosuu = torikosuu;
    }

    /**
     * @return the kosuu
     */
    public String getKosuu() {
        return kosuu;
    }

    /**
     * @param kosuu the kosuu to set
     */
    public void setKosuu(String kosuu) {
        this.kosuu = kosuu;
    }

    /**
     * 個数
     * @return the goki
     */
    public String getGoki() {
        return goki;
    }

    /**
     * 個数
     * @param goki the goki to set
     */
    public void setGoki(String goki) {
        this.goki = goki;
    }
    
 
}
