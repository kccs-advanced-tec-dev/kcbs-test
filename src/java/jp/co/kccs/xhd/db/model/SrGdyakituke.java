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
 * 変更日	2019/09/29<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	863 F.Zhang<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 外部電極焼成(焼成)のモデルクラスです。
 *
 * @author 863 F.Zhang
 * @since 2019/09/29
 */
public class SrGdyakituke {

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
     * 作業回数
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
     * 号機
     */
    private String gouki;
    
    /**
     * ｻﾔ/SUS板枚数
     */
    private Integer sayamaisuu;
    
    /**
     * 温度
     */
    private Integer ondo;
    
    /**
     * 送りｽﾋﾟｰﾄﾞ
     */
    private String okurispeed;

    /**
     * 開始日時
     */
    private Timestamp startdatetime;

    /**
     * 終了日時
     */
    private Timestamp enddatetime;

    /**
     * 開始担当者
     */
    private String startTantosyacode;
    
    /**
     * 開始確認者
     */
    private String startKakuninsyacode;

    /**
     * 終了担当者
     */
    private String endTantosyacode;    

    /**
     * 備考1
     */
    private String biko1;

    /**
     * 備考2
     */
    private String biko2;
    
    /**
     * 備考3
     */
    private String biko3;
    
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
     *
     * @return the kojyo
     */
    public String getKojyo() {
        return kojyo;
    }

    /**
     * 工場ｺｰﾄﾞ
     *
     * @param kojyo the kojyo to set
     */
    public void setKojyo(String kojyo) {
        this.kojyo = kojyo;
    }

    /**
     * ﾛｯﾄNo
     *
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * ﾛｯﾄNo
     *
     * @param lotno the lotno to set
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * 枝番
     *
     * @return the edaban
     */
    public String getEdaban() {
        return edaban;
    }

    /**
     * 枝番
     *
     * @param edaban the edaban to set
     */
    public void setEdaban(String edaban) {
        this.edaban = edaban;
    }

    /**
     * KCPNO
     *
     * @return the kcpno
     */
    public String getKcpno() {
        return kcpno;
    }

    /**
     * KCPNO
     *
     * @param kcpno the kcpno to set
     */
    public void setKcpno(String kcpno) {
        this.kcpno = kcpno;
    }

    /**
     * 開始日時
     *
     * @return the startdatetime
     */
    public Timestamp getStartdatetime() {
        return startdatetime;
    }

    /**
     * 開始日時
     *
     * @param startdatetime the startdatetime to set
     */
    public void setStartdatetime(Timestamp startdatetime) {
        this.startdatetime = startdatetime;
    }

    /**
     * 終了日時
     *
     * @return the enddatetime
     */
    public Timestamp getEnddatetime() {
        return enddatetime;
    }

    /**
     * 終了日時
     *
     * @param enddatetime the enddatetime to set
     */
    public void setEnddatetime(Timestamp enddatetime) {
        this.enddatetime = enddatetime;
    }

    /**
     * 号機
     *
     * @return the gouki
     */
    public String getGouki() {
        return gouki;
    }

    /**
     * 号機
     *
     * @param gouki the gouki to set
     */
    public void setGouki(String gouki) {
        this.gouki = gouki;
    }

    /**
     * 備考1
     *
     * @return the biko1
     */
    public String getBiko1() {
        return biko1;
    }

    /**
     * 備考1
     *
     * @param biko1 the biko1 to set
     */
    public void setBiko1(String biko1) {
        this.biko1 = biko1;
    }

    /**
     * 備考2
     *
     * @return the biko2
     */
    public String getBiko2() {
        return biko2;
    }

    /**
     * 備考2
     *
     * @param biko2 the biko2 to set
     */
    public void setBiko2(String biko2) {
        this.biko2 = biko2;
    }

    /**
     * 担当者ｺｰﾄﾞ
     *
     * @return the startTantosyacode
     */
    public String getStartTantosyacode() {
        return startTantosyacode;
    }

    /**
     * 担当者ｺｰﾄﾞ
     *
     * @param startTantosyacode the startTantosyacode to set
     */
    public void setStartTantosyacode(String startTantosyacode) {
        this.startTantosyacode = startTantosyacode;
    }

    /**
     * 登録日時
     *
     * @return the torokunichiji
     */
    public Timestamp getTorokunichiji() {
        return torokunichiji;
    }

    /**
     * 登録日時
     *
     * @param torokunichiji the torokunichiji to set
     */
    public void setTorokunichiji(Timestamp torokunichiji) {
        this.torokunichiji = torokunichiji;
    }

    /**
     * 更新日時
     *
     * @return the kosinnichiji
     */
    public Timestamp getKosinnichiji() {
        return kosinnichiji;
    }

    /**
     * 更新日時
     *
     * @param kosinnichiji the kosinnichiji to set
     */
    public void setKosinnichiji(Timestamp kosinnichiji) {
        this.kosinnichiji = kosinnichiji;
    }

    /**
     * revision
     *
     * @return the revision
     */
    public Long getRevision() {
        return revision;
    }

    /**
     * revision
     *
     * @param revision the revision to set
     */
    public void setRevision(Long revision) {
        this.revision = revision;
    }

    /**
     * 削除ﾌﾗｸﾞ
     *
     * @return the deleteflag
     */
    public Integer getDeleteflag() {
        return deleteflag;
    }

    /**
     * 削除ﾌﾗｸﾞ
     *
     * @param deleteflag the deleteflag to set
     */
    public void setDeleteflag(Integer deleteflag) {
        this.deleteflag = deleteflag;
    }

    /**
     * 客先
     *
     * @return the tokuisaki
     */
    public String getTokuisaki() {
        return tokuisaki;
    }

    /**
     * 客先
     *
     * @param tokuisaki the tokuisaki to set
     */
    public void setTokuisaki(String tokuisaki) {
        this.tokuisaki = tokuisaki;
    }

    /**
     * ﾛｯﾄ区分
     *
     * @return the lotkubuncode
     */
    public String getLotkubuncode() {
        return lotkubuncode;
    }

    /**
     * ﾛｯﾄ区分
     *
     * @param lotkubuncode the lotkubuncode to set
     */
    public void setLotkubuncode(String lotkubuncode) {
        this.lotkubuncode = lotkubuncode;
    }

    /**
     * ｵｰﾅｰ
     *
     * @return the ownercode
     */
    public String getOwnercode() {
        return ownercode;
    }

    /**
     * ｵｰﾅｰ
     *
     * @param ownercode the ownercode to set
     */
    public void setOwnercode(String ownercode) {
        this.ownercode = ownercode;
    }

    /**
     * 作業回数
     *
     * @return the kaisuu
     */
    public Integer getKaisuu() {
        return kaisuu;
    }

    /**
     * 作業回数
     *
     * @param kaisuu the kaisuu to set
     */
    public void setKaisuu(Integer kaisuu) {
        this.kaisuu = kaisuu;
    }

    /**
     * ﾛｯﾄﾌﾟﾚ
     *
     * @return the lotpre
     */
    public String getLotpre() {
        return lotpre;
    }

    /**
     * ﾛｯﾄﾌﾟﾚ
     *
     * @param lotpre the lotpre to set
     */
    public void setLotpre(String lotpre) {
        this.lotpre = lotpre;
    }

    /**
     * 処理数
     *
     * @return the syorisuu
     */
    public Integer getSyorisuu() {
        return syorisuu;
    }

    /**
     * 処理数
     *
     * @param syorisuu the syorisuu to set
     */
    public void setSyorisuu(Integer syorisuu) {
        this.syorisuu = syorisuu;
    }

    /**
     * ｻﾔ/SUS板枚数
     *
     * @return the sayamaisuu
     */
    public Integer getSayamaisuu() {
        return sayamaisuu;
    }

    /**
     * ｻﾔ/SUS板枚数
     *
     * @param sayamaisuu the sayamaisuu to set
     */
    public void setSayamaisuu(Integer sayamaisuu) {
        this.sayamaisuu = sayamaisuu;
    }

    /**
     * 温度
     *
     * @return the ondo
     */
    public Integer getOndo() {
        return ondo;
    }

    /**
     * 温度
     *
     * @param ondo the ondo to set
     */
    public void setOndo(Integer ondo) {
        this.ondo = ondo;
    }

    /**
     * 送りｽﾋﾟｰﾄﾞ
     *
     * @return the okurispeed
     */
    public String getOkurispeed() {
        return okurispeed;
    }

    /**
     * 送りｽﾋﾟｰﾄﾞ
     *
     * @param okurispeed the okurispeed to set
     */
    public void setOkurispeed(String okurispeed) {
        this.okurispeed = okurispeed;
    }

    /**
     * 開始確認者
     *
     * @return the startKakuninsyacode
     */
    public String getStartKakuninsyacode() {
        return startKakuninsyacode;
    }

    /**
     * 開始確認者
     *
     * @param startKakuninsyacode the startKakuninsyacode to set
     */
    public void setStartKakuninsyacode(String startKakuninsyacode) {
        this.startKakuninsyacode = startKakuninsyacode;
    }

    /**
     * 終了担当者
     *
     * @return the endTantosyacode
     */
    public String getEndTantosyacode() {
        return endTantosyacode;
    }

    /**
     * 終了担当者
     *
     * @param endTantosyacode the endTantosyacode to set
     */
    public void setEndTantosyacode(String endTantosyacode) {
        this.endTantosyacode = endTantosyacode;
    }

    /**
     * 備考3
     *
     * @return the biko3
     */
    public String getBiko3() {
        return biko3;
    }

    /**
     * 備考3
     *
     * @param biko3 the biko3 to set
     */
    public void setBiko3(String biko3) {
        this.biko3 = biko3;
    }    

}
