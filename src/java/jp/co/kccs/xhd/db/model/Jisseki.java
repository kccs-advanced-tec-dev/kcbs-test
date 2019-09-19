/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

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
 * jisseki(実績)のモデルクラスです。
 *
 * @author 863 K.Zhang
 * @since  2019/09/02
 */
public class Jisseki {
    /**
     * 実績No
     */
    private Integer jissekino;
    /**
     * 前実績No
     */
    private Integer prejissekino;
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
     * 工程ｺｰﾄﾞ
     */
    private String koteicode;
    /**
     * 処理日
     */
    private Integer syoribi;
    /**
     * 処理時刻
     */
    private Integer syorijikoku;
    /**
     * 再生ﾌﾗｸﾞ
     */
    private String saiseiflag;
    /**
     * 優先ｺｰﾄﾞ
     */
    private String yusencode;
    /**
     * ﾛｯﾄ区分ｺｰﾄﾞ
     */
    private String lotkubuncode;
    /**
     * ｵｰﾅｰｺｰﾄﾞ
     */
    private String ownercode;
    /**
     * 処理区分ｺｰﾄﾞ
     */
    private String syorikubuncode;
    /**
     * 担当者ｺｰﾄﾞ
     */
    private String tantousyacode;
    /**
     * 勤務帯ｺｰﾄﾞ
     */
    private String kinmutaicode;
    /**
     * 処理数
     */
    private Integer syorisuu;

    /**
     * @return jissekino
     */
    public Integer getJissekino() {
        return jissekino;
    }

    /**
     * @param jissekino セットする jissekino
     */
    public void setJissekino(Integer jissekino) {
        this.jissekino = jissekino;
    }

    /**
     * @return prejissekino
     */
    public Integer getPrejissekino() {
        return prejissekino;
    }

    /**
     * @param prejissekino セットする prejissekino
     */
    public void setPrejissekino(Integer prejissekino) {
        this.prejissekino = prejissekino;
    }

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
     * @return koteicode
     */
    public String getKoteicode() {
        return koteicode;
    }

    /**
     * @param koteicode セットする koteicode
     */
    public void setKoteicode(String koteicode) {
        this.koteicode = koteicode;
    }

    /**
     * @return syoribi
     */
    public Integer getSyoribi() {
        return syoribi;
    }

    /**
     * @param syoribi セットする syoribi
     */
    public void setSyoribi(Integer syoribi) {
        this.syoribi = syoribi;
    }

    /**
     * @return syorijikoku
     */
    public Integer getSyorijikoku() {
        return syorijikoku;
    }

    /**
     * @param syorijikoku セットする syorijikoku
     */
    public void setSyorijikoku(Integer syorijikoku) {
        this.syorijikoku = syorijikoku;
    }

    /**
     * @return saiseiflag
     */
    public String getSaiseiflag() {
        return saiseiflag;
    }

    /**
     * @param saiseiflag セットする saiseiflag
     */
    public void setSaiseiflag(String saiseiflag) {
        this.saiseiflag = saiseiflag;
    }

    /**
     * @return yusencode
     */
    public String getYusencode() {
        return yusencode;
    }

    /**
     * @param yusencode セットする yusencode
     */
    public void setYusencode(String yusencode) {
        this.yusencode = yusencode;
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
     * @return syorikubuncode
     */
    public String getSyorikubuncode() {
        return syorikubuncode;
    }

    /**
     * @param syorikubuncode セットする syorikubuncode
     */
    public void setSyorikubuncode(String syorikubuncode) {
        this.syorikubuncode = syorikubuncode;
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
     * @return kinmutaicode
     */
    public String getKinmutaicode() {
        return kinmutaicode;
    }

    /**
     * @param kinmutaicode セットする kinmutaicode
     */
    public void setKinmutaicode(String kinmutaicode) {
        this.kinmutaicode = kinmutaicode;
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
    
    
    
    
    
    
}
