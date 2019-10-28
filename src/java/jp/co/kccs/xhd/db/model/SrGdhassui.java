/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/10/14<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	863 K.Zhang<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * sr_gdhassui:外部電極洗浄(撥水処理)のモデルクラスです。
 *
 * @author 863 K.Zhang
 * @since  2019/10/14
 */
public class SrGdhassui {
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
     * 回数
     */
    private Integer kaisuu;

    /**
     * KCPNO
     */
    private String kcpno;

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
     * ﾛｯﾄﾌﾟﾚ
     */
    private String lotpre;
    
    /**
     * 処理数
     */
    private Integer syorisuu;
    
    /**
     * 浸漬時間
     */
    private Integer sinsekijikan;
    
    /**
     * 洗浄時間
     */
    private Integer senjoujikan;
    
    /**
     * ﾒﾀﾉｰﾙ交換時間
     */
    private String methanolkoukanjikan;
    
    /**
     * ﾒﾀﾉｰﾙ交換担当者
     */
    private String methanolkoukantantousya;
    
    /**
     * 撥水処理日時
     */
    private Timestamp hassuidatetime;

    /**
     * 撥水処理担当者
     */
    private String hassuitantosyacode;

    /**
     * 乾燥時間
     */
    private Integer kansoujikan;
    
    /**
     * 乾燥温度
     */
    private String kansouondo;
    
    /**
     * 乾燥処理日時
     */
    private Timestamp kansoudatetime;
    
    /**
     * 乾燥処理担当者
     */
    private String kansoutantosyacode;

    
    
    /**
     * 備考1
     */
    private String biko1;
    
    /**
     * 備考2
     */
    private String biko2;
    
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
    private Integer revision;

    /**
     * 削除ﾌﾗｸﾞ
     */
    private Integer deleteflag;

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
     * @return kaisuu
     */
    public Integer getKaisuu() {
        return kaisuu;
    }

    /**
     * @param kaisuu セットする kaisuu
     */
    public void setKaisuu(Integer kaisuu) {
        this.kaisuu = kaisuu;
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
     * 
     * @return tokuisaki
     */
    public String getTokuisaki() {
        return tokuisaki;
    }

    /**
     * 
     * @param tokuisaki セットする tokuisaki
     */
    public void setTokuisaki(String tokuisaki) {
        this.tokuisaki = tokuisaki;
    }

    /**
     * 
     * @return lotkubuncode
     */
    public String getLotkubuncode() {
        return lotkubuncode;
    }

    /**
     * 
     * @param lotkubuncode セットする lotkubuncode
     */
    public void setLotkubuncode(String lotkubuncode) {
        this.lotkubuncode = lotkubuncode;
    }

    /**
     * 
     * @return ownercode
     */
    public String getOwnercode() {
        return ownercode;
    }

    /**
     * 
     * @param ownercode セットする ownercode
     */
    public void setOwnercode(String ownercode) {
        this.ownercode = ownercode;
    }

    /**
     * 
     * @return lotpre
     */
    public String getLotpre() {
        return lotpre;
    }

    /**
     * 
     * @param lotpre セットする lotpre
     */
    public void setLotpre(String lotpre) {
        this.lotpre = lotpre;
    }

    /**
     * 
     * @return syorisuu
     */
    public Integer getSyorisuu() {
        return syorisuu;
    }

    /**
     * 
     * @param syorisuu セットする syorisuu
     */
    public void setSyorisuu(Integer syorisuu) {
        this.syorisuu = syorisuu;
    }

    /**
     * 
     * @return sinsekijikan
     */
    public Integer getSinsekijikan() {
        return sinsekijikan;
    }

    /**
     * 
     * @param sinsekijikan セットする sinsekijikan
     */
    public void setSinsekijikan(Integer sinsekijikan) {
        this.sinsekijikan = sinsekijikan;
    }

    /**
     * 
     * @return senjoujikan
     */
    public Integer getSenjoujikan() {
        return senjoujikan;
    }

    /**
     * 
     * @param senjoujikan セットする senjoujikan
     */
    public void setSenjoujikan(Integer senjoujikan) {
        this.senjoujikan = senjoujikan;
    }

    /**
     * 
     * @return methanolkoukanjikan
     */
    public String getMethanolkoukanjikan() {
        return methanolkoukanjikan;
    }

    /**
     * 
     * @param methanolkoukanjikan セットする methanolkoukanjikan
     */
    public void setMethanolkoukanjikan(String methanolkoukanjikan) {
        this.methanolkoukanjikan = methanolkoukanjikan;
    }

    /**
     * 
     * @return methanolkoukantantousya
     */
    public String getMethanolkoukantantousya() {
        return methanolkoukantantousya;
    }

    /**
     * 
     * @param methanolkoukantantousya セットする methanolkoukantantousya
     */
    public void setMethanolkoukantantousya(String methanolkoukantantousya) {
        this.methanolkoukantantousya = methanolkoukantantousya;
    }

    /**
     * 
     * @return hassuidatetime
     */
    public Timestamp getHassuidatetime() {
        return hassuidatetime;
    }

    /**
     * 
     * @param hassuidatetime セットする hassuidatetime
     */
    public void setHassuidatetime(Timestamp hassuidatetime) {
        this.hassuidatetime = hassuidatetime;
    }

    /**
     * 
     * @return hassuitantosyacode
     */
    public String getHassuitantosyacode() {
        return hassuitantosyacode;
    }

    /**
     * 
     * @param hassuitantosyacode セットする hassuitantosyacode
     */
    public void setHassuitantosyacode(String hassuitantosyacode) {
        this.hassuitantosyacode = hassuitantosyacode;
    }

    /**
     * 
     * @return kansoujikan
     */
    public Integer getKansoujikan() {
        return kansoujikan;
    }

    /**
     * 
     * @param kansoujikan セットする kansoujikan
     */
    public void setKansoujikan(Integer kansoujikan) {
        this.kansoujikan = kansoujikan;
    }

    /**
     * 
     * @return kansouondo
     */
    public String getKansouondo() {
        return kansouondo;
    }

    /**
     * 
     * @param kansouondo セットする kansouondo
     */
    public void setKansouondo(String kansouondo) {
        this.kansouondo = kansouondo;
    }

    /**
     * 
     * @return kansoudatetime
     */
    public Timestamp getKansoudatetime() {
        return kansoudatetime;
    }

    /**
     * 
     * @param kansoudatetime セットする kansoudatetime
     */
    public void setKansoudatetime(Timestamp kansoudatetime) {
        this.kansoudatetime = kansoudatetime;
    }

    /**
     * 
     * @return kansoutantosyacode
     */
    public String getKansoutantosyacode() {
        return kansoutantosyacode;
    }

    /**
     * 
     * @param kansoutantosyacode セットする kansoutantosyacode
     */
    public void setKansoutantosyacode(String kansoutantosyacode) {
        this.kansoutantosyacode = kansoutantosyacode;
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
     * @return biko2
     */
    public String getBiko2() {
        return biko2;
    }

    /**
     * @param biko2 セットする biko2
     */
    public void setBiko2(String biko2) {
        this.biko2 = biko2;
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
     * @return deleteflag
     */
    public Integer getDeleteflag() {
        return deleteflag;
    }

    /**
     * @param deleteflag セットする deleteflag
     */
    public void setDeleteflag(Integer deleteflag) {
        this.deleteflag = deleteflag;
    }

    
}