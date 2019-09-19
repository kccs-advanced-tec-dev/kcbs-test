/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2019/09/02<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	863 K.Zhang<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SR_SOSUI(疎水処理)のモデルクラスです。
 *
 * @author 863 K.Zhang
 * @since  2019/09/02
 */
public class SrSosui {
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
     * 処理数
     */
    private Integer syorisuu;

    /**
     * 処理号機
     */
    private String syorigoki;

    /**
     * ﾁｬｰｼﾞ量
     */
    private Integer chargeryou;

    /**
     * ﾄﾚｰ枚数
     */
    private Integer traymaisuu;

    /**
     * 処理ﾌﾟﾛｸﾞﾗﾑNo
     */
    private Integer programno;

    /**
     * 開始日時
     */
    private Timestamp kaisinichiji;

    /**
     * 開始担当者
     */
    private String starttantosyacode;

    /**
     * 開始確認者
     */
    private String startkakuninsyacode;

    /**
     * 終了日時
     */
    private Timestamp syuuryounichiji;

    /**
     * 終了担当者
     */
    private String endtantosyacode;

    /**
     * 作業場所
     */
    private String sagyoubasyo;

    /**
     * 備考1
     */
    private String biko1;

    /**
     * 備考2
     */
    private String biko2;

    /**
     * 回数
     */
    private Integer kaisuu;

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
     * @return syorigoki
     */
    public String getSyorigoki() {
        return syorigoki;
    }

    /**
     * @param syorigoki セットする syorigoki
     */
    public void setSyorigoki(String syorigoki) {
        this.syorigoki = syorigoki;
    }

    /**
     * @return chargeryou
     */
    public Integer getChargeryou() {
        return chargeryou;
    }

    /**
     * @param chargeryou セットする chargeryou
     */
    public void setChargeryou(Integer chargeryou) {
        this.chargeryou = chargeryou;
    }

    /**
     * @return traymaisuu
     */
    public Integer getTraymaisuu() {
        return traymaisuu;
    }

    /**
     * @param traymaisuu セットする traymaisuu
     */
    public void setTraymaisuu(Integer traymaisuu) {
        this.traymaisuu = traymaisuu;
    }

    /**
     * @return programno
     */
    public Integer getProgramno() {
        return programno;
    }

    /**
     * @param programno セットする programno
     */
    public void setProgramno(Integer programno) {
        this.programno = programno;
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
     * @return starttantosyacode
     */
    public String getStarttantosyacode() {
        return starttantosyacode;
    }

    /**
     * @param starttantosyacode セットする starttantosyacode
     */
    public void setStarttantosyacode(String starttantosyacode) {
        this.starttantosyacode = starttantosyacode;
    }

    /**
     * @return startkakuninsyacode
     */
    public String getStartkakuninsyacode() {
        return startkakuninsyacode;
    }

    /**
     * @param startkakuninsyacode セットする startkakuninsyacode
     */
    public void setStartkakuninsyacode(String startkakuninsyacode) {
        this.startkakuninsyacode = startkakuninsyacode;
    }

    /**
     * @return syuuryounichiji
     */
    public Timestamp getSyuuryounichiji() {
        return syuuryounichiji;
    }

    /**
     * @param syuuryounichiji セットする syuuryounichiji
     */
    public void setSyuuryounichiji(Timestamp syuuryounichiji) {
        this.syuuryounichiji = syuuryounichiji;
    }

    /**
     * @return endtantosyacode
     */
    public String getEndtantosyacode() {
        return endtantosyacode;
    }

    /**
     * @param endtantosyacode セットする endtantosyacode
     */
    public void setEndtantosyacode(String endtantosyacode) {
        this.endtantosyacode = endtantosyacode;
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