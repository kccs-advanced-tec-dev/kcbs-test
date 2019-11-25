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
 * 変更日	2019/11/12<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 外部電極・親水処理履歴検索画面のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2019/11/12
 */
public class GXHDO201B036Model implements Serializable{

    /** ﾛｯﾄNo. */
    private String lotno = "";

    /** KCPNO */
    private String kcpno = "";

    /** 処理数 */
    private Integer syorisuu = null;

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

    public String getLotno() {
        return lotno;
    }

    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    public String getKcpno() {
        return kcpno;
    }

    public void setKcpno(String kcpno) {
        this.kcpno = kcpno;
    }

    public Integer getSyorisuu() {
        return syorisuu;
    }

    public void setSyorisuu(Integer syorisuu) {
        this.syorisuu = syorisuu;
    }

    public String getSyorigoki() {
        return syorigoki;
    }

    public void setSyorigoki(String syorigoki) {
        this.syorigoki = syorigoki;
    }

    public Integer getChargeryou() {
        return chargeryou;
    }

    public void setChargeryou(Integer chargeryou) {
        this.chargeryou = chargeryou;
    }

    public Integer getTraymaisuu() {
        return traymaisuu;
    }

    public void setTraymaisuu(Integer traymaisuu) {
        this.traymaisuu = traymaisuu;
    }

    public Integer getProgramno() {
        return programno;
    }

    public void setProgramno(Integer programno) {
        this.programno = programno;
    }

    public Timestamp getKaisinichiji() {
        return kaisinichiji;
    }

    public void setKaisinichiji(Timestamp kaisinichiji) {
        this.kaisinichiji = kaisinichiji;
    }

    public String getStarttantosyacode() {
        return starttantosyacode;
    }

    public void setStarttantosyacode(String starttantosyacode) {
        this.starttantosyacode = starttantosyacode;
    }

    public String getStartkakuninsyacode() {
        return startkakuninsyacode;
    }

    public void setStartkakuninsyacode(String startkakuninsyacode) {
        this.startkakuninsyacode = startkakuninsyacode;
    }

    public Timestamp getSyuuryounichiji() {
        return syuuryounichiji;
    }

    public void setSyuuryounichiji(Timestamp syuuryounichiji) {
        this.syuuryounichiji = syuuryounichiji;
    }

    public String getEndtantosyacode() {
        return endtantosyacode;
    }

    public void setEndtantosyacode(String endtantosyacode) {
        this.endtantosyacode = endtantosyacode;
    }

    public String getSagyoubasyo() {
        return sagyoubasyo;
    }

    public void setSagyoubasyo(String sagyoubasyo) {
        this.sagyoubasyo = sagyoubasyo;
    }
    
    public String getBiko1() {
        return biko1;
    }

    public void setBiko1(String biko1) {
        this.biko1 = biko1;
    }

    public String getBiko2() {
        return biko2;
    }

    public void setBiko2(String biko2) {
        this.biko2 = biko2;
    }

    public Integer getKaisuu() {
        return kaisuu;
    }

    public void setKaisuu(Integer kaisuu) {
        this.kaisuu = kaisuu;
    }

}