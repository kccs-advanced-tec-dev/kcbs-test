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
 * 変更日	2019/12/05<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	863 K.Zhang<br>
 * 変更理由	新規作成<br>
 * <br>
 * <br>
 * 変更日	2020/09/04<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	863 sujialiang<br>
 * 変更理由	項目追加・変更<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 外部電極・疎水処理履歴検索画面のモデルクラスです。
 *
 * @author 863 K.Zhang
 * @since  2019/12/05
 */
public class GXHDO201B024Model implements Serializable{

    /** ﾛｯﾄNo. */
    private String lotno = "";

    /** KCPNO */
    private String kcpno = "";

    /** 受入れ良品数 */
    private Integer syorisuu = null;

    /** 受入れ単位重量 */
    private BigDecimal ukeiretannijyuryo = null;

    /** 受入れ総重量 */
    private BigDecimal ukeiresoujyuryou = null;

    /** 処理号機 */
    private String syorigoki = "";

    /** ﾁｬｰｼﾞ量 */
    private Integer chargeryou = null;

    /** ﾄﾚｰ枚数 */
    private Integer traymaisuu = null;

    /** 処理ﾌﾟﾛｸﾞﾗﾑNo */
    private Integer programno = null;

    /** 開始日時 */
    private Timestamp kaisinichiji = null;

    /** 開始担当者 */
    private String starttantosyacode = "";

    /** 開始確認者 */
    private String startkakuninsyacode = "";

    /** 終了日時 */
    private Timestamp syuuryounichiji = null;

    /** 終了担当者 */
    private String endtantosyacode = "";

    /** 作業場所 */
    private String sagyoubasyo = "";

    /** 備考1 */
    private String biko1 = "";

    /** 備考2 */
    private String biko2 = "";

    /** 回数 */
    private Integer kaisuu = null;
    
    
    /**
     * ﾛｯﾄNo
     * @return lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * ﾛｯﾄNo
     * @param lotno セットする lotno
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }
    
    /**
     * KCPNO
     * @return kcpno
     */
    public String getKcpno() {
        return kcpno;
    }

    /**
     * KCPNO
     * @param kcpno セットする kcpno
     */
    public void setKcpno(String kcpno) {
        this.kcpno = kcpno;
    }
    
    /**
     * 受入れ良品数
     * @return syorisuu
     */
     public Integer getSyorisuu() {
        return syorisuu;
    }

     /**
      * 受入れ良品数
      * @param syorisuu セットする syorisuu
      */
    public void setSyorisuu(Integer syorisuu) {
        this.syorisuu = syorisuu;
    }

    /**
     * 受入れ単位重量
     * @return ukeiretannijyuryo
     */
    public BigDecimal getUkeiretannijyuryo() {
        return ukeiretannijyuryo;
    }

    /**
     * 受入れ単位重量
     * @param ukeiretannijyuryo 
     */
    public void setUkeiretannijyuryo(BigDecimal ukeiretannijyuryo) {
        this.ukeiretannijyuryo = ukeiretannijyuryo;
    }

    /**
     * 受入れ総重量
     * @return ukeiresoujyuryou
     */
    public BigDecimal getUkeiresoujyuryou() {
        return ukeiresoujyuryou;
    }

    /**
     * 受入れ総重量
     * @param ukeiresoujyuryou 
     */
    public void setUkeiresoujyuryou(BigDecimal ukeiresoujyuryou) {
        this.ukeiresoujyuryou = ukeiresoujyuryou;
    }

    /**
     * 処理号機
     * @return syorigoki
     */
    public String getSyorigoki() {
        return syorigoki;
    }

    /**
     * 処理号機
     * @param syorigoki セットする syorigoki
     */
    public void setSyorigoki(String syorigoki) {
        this.syorigoki = syorigoki;
    }

    /**
     * ﾁｬｰｼﾞ量
     * @return chargeryou
     */
    public Integer getChargeryou() {
        return chargeryou;
    }

    /**
     * ﾁｬｰｼﾞ量
     * @param chargeryou セットする chargeryou
     */
    public void setChargeryou(Integer chargeryou) {
        this.chargeryou = chargeryou;
    }

    /**
     * ﾄﾚｰ枚数
     * @return traymaisuu
     */
    public Integer getTraymaisuu() {
        return traymaisuu;
    }

    /**
     * ﾄﾚｰ枚数
     * @param traymaisuu セットする traymaisuu
     */
    public void setTraymaisuu(Integer traymaisuu) {
        this.traymaisuu = traymaisuu;
    }

    /**
     * 処理ﾌﾟﾛｸﾞﾗﾑNo
     * @return programno
     */
    public Integer getProgramno() {
        return programno;
    }

    /**
     * 処理ﾌﾟﾛｸﾞﾗﾑNo
     * @param programno セットする programno
     */
    public void setProgramno(Integer programno) {
        this.programno = programno;
    }

    /**
     * 開始日時
     * @return kaisinichiji
     */
    public Timestamp getKaisinichiji() {
        return kaisinichiji;
    }

    /**
     * 開始日時
     * @param kaisinichiji セットする kaisinichiji
     */
    public void setKaisinichiji(Timestamp kaisinichiji) {
        this.kaisinichiji = kaisinichiji;
    }

    /**
     * 開始担当者
     * @return starttantosyacode
     */
    public String getStarttantosyacode() {
        return starttantosyacode;
    }

    /**
     * 開始担当者
     * @param starttantosyacode セットする starttantosyacode
     */
    public void setStarttantosyacode(String starttantosyacode) {
        this.starttantosyacode = starttantosyacode;
    }

    /**
     * 開始確認者
     * @return startkakuninsyacode
     */
    public String getStartkakuninsyacode() {
        return startkakuninsyacode;
    }

    /**
     * 開始確認者
     * @param startkakuninsyacode セットする startkakuninsyacode
     */
    public void setStartkakuninsyacode(String startkakuninsyacode) {
        this.startkakuninsyacode = startkakuninsyacode;
    }

    /**
     * 終了日時
     * @return syuuryounichiji
     */
    public Timestamp getSyuuryounichiji() {
        return syuuryounichiji;
    }

    /**
     * 終了日時
     * @param syuuryounichiji セットする syuuryounichiji
     */
    public void setSyuuryounichiji(Timestamp syuuryounichiji) {
        this.syuuryounichiji = syuuryounichiji;
    }

    /**
     * 終了担当者
     * @return endtantosyacode
     */
    public String getEndtantosyacode() {
        return endtantosyacode;
    }

    /**
     * 終了担当者
     * @param endtantosyacode セットする endtantosyacode
     */
    public void setEndtantosyacode(String endtantosyacode) {
        this.endtantosyacode = endtantosyacode;
    }

    /**
     * 作業場所
     * @return sagyoubasyo
     */
    public String getSagyoubasyo() {
        return sagyoubasyo;
    }

    /**
     * 作業場所
     * @param sagyoubasyo セットする sagyoubasyo
     */
    public void setSagyoubasyo(String sagyoubasyo) {
        this.sagyoubasyo = sagyoubasyo;
    }

    /**
     * 備考1
     * @return biko1
     */
    public String getBiko1() {
        return biko1;
    }

    /**
     * 備考1
     * @param biko1 セットする biko1
     */
    public void setBiko1(String biko1) {
        this.biko1 = biko1;
    }

    /**
     * 備考2
     * @return biko2
     */
    public String getBiko2() {
        return biko2;
    }

    /**
     * 備考2
     * @param biko2 セットする biko2
     */
    public void setBiko2(String biko2) {
        this.biko2 = biko2;
    }

    /**
     * 回数
     * @return kaisuu
     */
    public Integer getKaisuu() {
        return kaisuu;
    }

    /**
     * 回数
     * @param kaisuu セットする kaisuu
     */
    public void setKaisuu(Integer kaisuu) {
        this.kaisuu = kaisuu;
    }

}