/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/09/02<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	863 F.Zhang<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * sr_termt(外部電極塗布T)のモデルクラスです。
 *
 * @author 863 F.Zhang
 * @since  2019/09/02
 */
public class SrGdtermt {
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
     * 処理数
     */
    private Integer syorisuu;
    
    /**
     * ﾛｯﾄﾌﾟﾚ
     */
    private String lotpre;
    
    /**
     * 塗布号機
     */
    private String tofugoki;
    
    /**
     * ﾍﾟｰｽﾄ品名
     */
    private String pastehinmei;
    
    /**
     * ﾍﾟｰｽﾄﾛｯﾄNo
     */
    private String pastelotno;
    
    /**
     * ﾍﾟｰｽﾄ再生回数
     */
    private Integer pastesaiseikaisuu;
    
    /**
     * ﾍﾟｰｽﾄ粘度
     */
    private Integer pastenendo;
    
    /**
     * 条件設定者
     */
    private String setteisya;
    
    /**
     * 開始日時
     */
    private Timestamp startdatetime;
    
    /**
     * 開始担当者
     */
    private String starttantosyacode;

    /**
     * 開始確認者
     */
    private String startkakuninsyacode;

    /**
     * ｷｬﾘｱﾃｰﾌﾟ
     */
    private String carriertape;
    
    /**
     * 印刷幅
     */
    private BigDecimal insatsuhaba;
    
    /**
     * 回り込みAVE
     */
    private BigDecimal mawarikomi;
    
    /**
     * 位置ｽﾞﾚMAX
     */
    private BigDecimal itizure;
    
    /**
     * 端子間幅MIN
     */
    private BigDecimal tansikanhaba;
    
    /**
     * 塗布後外観結果
     */
    private String tofugogaikan;
    
    /**
     * 処理個数
     */
    private Integer syorikosuu;
    
    /**
     * 重量
     */
    private BigDecimal juryou;
    
    /**
     * 終了日時
     */
    private Timestamp enddatetime;

    /**
     * 終了担当者
     */
    private String endtantosyacode;
    
    /**
     * 作業場所
     */
    private String sagyobasyo;
     
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
     * @return lotpre
     */
    public String getLotpre() {
        return lotpre;
    }

    /**
     * @param lotpre セットする lotpre
     */
    public void setLotpre(String lotpre) {
        this.lotpre = lotpre;
    }

    /**
     * @return tofugoki
     */
    public String getTofugoki() {
        return tofugoki;
    }

    /**
     * @param tofugoki セットする tofugoki
     */
    public void setTofugoki(String tofugoki) {
        this.tofugoki = tofugoki;
    }

    /**
     * @return pastehinmei
     */
    public String getPastehinmei() {
        return pastehinmei;
    }

    /**
     * @param pastehinmei セットする pastehinmei
     */
    public void setPastehinmei(String pastehinmei) {
        this.pastehinmei = pastehinmei;
    }

    /**
     * @return pastelotno
     */
    public String getPastelotno() {
        return pastelotno;
    }

    /**
     * @param pastelotno セットする pastelotno
     */
    public void setPastelotno(String pastelotno) {
        this.pastelotno = pastelotno;
    }

    /**
     * @return pastesaiseikaisuu
     */
    public Integer getPastesaiseikaisuu() {
        return pastesaiseikaisuu;
    }

    /**
     * @param pastesaiseikaisuu セットする pastesaiseikaisuu
     */
    public void setPastesaiseikaisuu(Integer pastesaiseikaisuu) {
        this.pastesaiseikaisuu = pastesaiseikaisuu;
    }

    /**
     * @return pastenendo
     */
    public Integer getPastenendo() {
        return pastenendo;
    }

    /**
     * @param pastenendo セットする pastenendo
     */
    public void setPastenendo(Integer pastenendo) {
        this.pastenendo = pastenendo;
    }

    /**
     * @return setteisya
     */
    public String getSetteisya() {
        return setteisya;
    }

    /**
     * @param setteisya セットする setteisya
     */
    public void setSetteisya(String setteisya) {
        this.setteisya = setteisya;
    }

    /**
     * @return startdatetime
     */
    public Timestamp getStartdatetime() {
        return startdatetime;
    }

    /**
     * @param startdatetime セットする startdatetime
     */
    public void setStartdatetime(Timestamp startdatetime) {
        this.startdatetime = startdatetime;
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
     * @return carriertape
     */
    public String getCarriertape() {
        return carriertape;
    }

    /**
     * @param carriertape セットする carriertape
     */
    public void setCarriertape(String carriertape) {
        this.carriertape = carriertape;
    }

    /**
     * @return insatsuhaba
     */
    public BigDecimal getInsatsuhaba() {
        return insatsuhaba;
    }

    /**
     * @param insatsuhaba セットする insatsuhaba
     */
    public void setInsatsuhaba(BigDecimal insatsuhaba) {
        this.insatsuhaba = insatsuhaba;
    }

    /**
     * @return mawarikomi
     */
    public BigDecimal getMawarikomi() {
        return mawarikomi;
    }

    /**
     * @param mawarikomi セットする mawarikomi
     */
    public void setMawarikomi(BigDecimal mawarikomi) {
        this.mawarikomi = mawarikomi;
    }

    /**
     * @return itizure
     */
    public BigDecimal getItizure() {
        return itizure;
    }

    /**
     * @param itizure セットする itizure
     */
    public void setItizure(BigDecimal itizure) {
        this.itizure = itizure;
    }

    /**
     * @return tansikanhaba
     */
    public BigDecimal getTansikanhaba() {
        return tansikanhaba;
    }

    /**
     * @param tansikanhaba セットする tansikanhaba
     */
    public void setTansikanhaba(BigDecimal tansikanhaba) {
        this.tansikanhaba = tansikanhaba;
    }

    /**
     * @return tofugogaikan
     */
    public String getTofugogaikan() {
        return tofugogaikan;
    }

    /**
     * @param tofugogaikan セットする tofugogaikan
     */
    public void setTofugogaikan(String tofugogaikan) {
        this.tofugogaikan = tofugogaikan;
    }

    /**
     * @return syorikosuu
     */
    public Integer getSyorikosuu() {
        return syorikosuu;
    }

    /**
     * @param syorikosuu セットする syorikosuu
     */
    public void setSyorikosuu(Integer syorikosuu) {
        this.syorikosuu = syorikosuu;
    }

    /**
     * @return juryou
     */
    public BigDecimal getJuryou() {
        return juryou;
    }

    /**
     * @param juryou セットする juryou
     */
    public void setJuryou(BigDecimal juryou) {
        this.juryou = juryou;
    }

    /**
     * @return enddatetime
     */
    public Timestamp getEnddatetime() {
        return enddatetime;
    }

    /**
     * @param enddatetime セットする enddatetime
     */
    public void setEnddatetime(Timestamp enddatetime) {
        this.enddatetime = enddatetime;
    }

    /**
     * @return sagyobasyo
     */
    public String getSagyobasyo() {
        return sagyobasyo;
    }

    /**
     * @param sagyobasyo セットする sagyobasyo
     */
    public void setSagyobasyo(String sagyobasyo) {
        this.sagyobasyo = sagyobasyo;
    }
    
}