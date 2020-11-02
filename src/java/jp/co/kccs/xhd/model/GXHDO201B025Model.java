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
 * 変更者	863 F.Zhang<br>
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
 * 外部電極・ﾒ外部電極塗布(三端子,4端子)履歴検索画面のモデルクラスです。
 *
 * @author 863 F.Zhang
 * @since 2019/12/05
 */
public class GXHDO201B025Model implements Serializable {

    /**
     * ﾛｯﾄNo
     */
    private String lotno = "";

    /**
     * 回数
     */
    private Integer kaisuu = null;

    /**
     * KCPNO
     */
    private String kcpno = "";

    /**
     * 受入れ良品数
     */
    private Integer syorisuu = null;

    /**
     * 受入れ単位重量
     */
    private BigDecimal ukeiretannijyuryo = null;

    /**
     * 受入れ総重量
     */
    private BigDecimal ukeiresoujyuryou = null;

    /**
     * ﾛｯﾄﾌﾟﾚ
     */
    private String lotpre = "";

    /**
     * 塗布号機
     */
    private String tofugoki = "";

    /**
     * ﾍﾟｰｽﾄ品名
     */
    private String pastehinmei = "";

    /**
     * ﾍﾟｰｽﾄﾛｯﾄNo
     */
    private String pastelotno = "";

    /**
     * ﾍﾟｰｽﾄ再生回数
     */
    private Integer pastesaiseikaisuu = null;

    /**
     * ﾍﾟｰｽﾄ粘度
     */
    private Integer pastenendo = null;

    /**
     * 条件設定者
     */
    private String setteisya = "";

    /**
     * 開始日時
     */
    private Timestamp startdatetime = null;

    /**
     * 開始担当者
     */
    private String starttantosyacode = "";

    /**
     * 開始確認者
     */
    private String startkakuninsyacode = "";

    /**
     * ｷｬﾘｱﾃｰﾌﾟ
     */
    private String carriertape = "";

    /**
     * 印刷幅
     */
    private BigDecimal insatsuhaba = null;

    /**
     * 磁器L寸法(MAX)
     */
    private BigDecimal jikilsunpoumax = null;

    /**
     * 磁器L寸法(MIN)
     */
    private BigDecimal jikilsunpoumin = null;

    /**
     * 磁器W寸法(MAX)
     */
    private BigDecimal jikiwsunpoumax = null;

    /**
     * 磁器W寸法(MIN)
     */
    private BigDecimal jikiwsunpoumin = null;

    /**
     * 磁器T寸法(MAX)
     */
    private BigDecimal jikitsunpoumax = null;

    /**
     * 磁器T寸法(MIN)
     */
    private BigDecimal jikitsunpoumin = null;

    /**
     * 回り込みAVE
     */
    private BigDecimal mawarikomi = null;

    /**
     * 位置ｽﾞﾚMAX
     */
    private BigDecimal itizure = null;

    /**
     * 端子間幅MIN
     */
    private BigDecimal tansikanhaba = null;

    /**
     * 塗布後外観結果
     */
    private String tofugogaikan = "";

    /**
     * 処理個数
     */
    private Integer syorikosuu = null;

    /**
     * 重量
     */
    private BigDecimal juryou = null;

    /**
     * 終了日時
     */
    private Timestamp enddatetime = null;

    /**
     * 終了担当者
     */
    private String endtantosyacode = "";

    /**
     * 作業場所
     */
    private String sagyobasyo = "";

    /**
     * 備考1
     */
    private String biko1 = "";

    /**
     * 備考2
     */
    private String biko2 = "";

    /**
     * ﾛｯﾄNo
     *
     * @return lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * ﾛｯﾄNo
     *
     * @param lotno セットする lotno
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * 回数
     *
     * @return kaisuu
     */
    public Integer getKaisuu() {
        return kaisuu;
    }

    /**
     * 回数
     *
     * @param kaisuu セットする kaisuu
     */
    public void setKaisuu(Integer kaisuu) {
        this.kaisuu = kaisuu;
    }

    /**
     * KCPNO
     *
     * @return kcpno
     */
    public String getKcpno() {
        return kcpno;
    }

    /**
     * KCPNO
     *
     * @param kcpno セットする kcpno
     */
    public void setKcpno(String kcpno) {
        this.kcpno = kcpno;
    }

    /**
     * 備考1
     *
     * @return biko1
     */
    public String getBiko1() {
        return biko1;
    }

    /**
     * 備考1
     *
     * @param biko1 セットする biko1
     */
    public void setBiko1(String biko1) {
        this.biko1 = biko1;
    }

    /**
     * 備考2
     *
     * @return biko2
     */
    public String getBiko2() {
        return biko2;
    }

    /**
     * 備考2
     *
     * @param biko2 セットする biko2
     */
    public void setBiko2(String biko2) {
        this.biko2 = biko2;
    }

    /**
     * 開始確認者
     *
     * @return startkakuninsyacode
     */
    public String getStartkakuninsyacode() {
        return startkakuninsyacode;
    }

    /**
     * 開始確認者
     *
     * @param startkakuninsyacode セットする startkakuninsyacode
     */
    public void setStartkakuninsyacode(String startkakuninsyacode) {
        this.startkakuninsyacode = startkakuninsyacode;
    }

    /**
     * 終了担当者
     *
     * @return endtantosyacode
     */
    public String getEndtantosyacode() {
        return endtantosyacode;
    }

    /**
     * 終了担当者
     *
     * @param endtantosyacode セットする endtantosyacode
     */
    public void setEndtantosyacode(String endtantosyacode) {
        this.endtantosyacode = endtantosyacode;
    }

    /**
     * 受入れ良品数
     *
     * @return syorisuu
     */
    public Integer getSyorisuu() {
        return syorisuu;
    }

    /**
     * 受入れ良品数
     *
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
     * ﾛｯﾄﾌﾟﾚ
     *
     * @return lotpre
     */
    public String getLotpre() {
        return lotpre;
    }

    /**
     * ﾛｯﾄﾌﾟﾚ
     *
     * @param lotpre セットする lotpre
     */
    public void setLotpre(String lotpre) {
        this.lotpre = lotpre;
    }

    /**
     * 塗布号機
     *
     * @return tofugoki
     */
    public String getTofugoki() {
        return tofugoki;
    }

    /**
     * 塗布号機
     *
     * @param tofugoki セットする tofugoki
     */
    public void setTofugoki(String tofugoki) {
        this.tofugoki = tofugoki;
    }

    /**
     * ﾍﾟｰｽﾄ品名
     *
     * @return pastehinmei
     */
    public String getPastehinmei() {
        return pastehinmei;
    }

    /**
     * ﾍﾟｰｽﾄ品名
     *
     * @param pastehinmei セットする pastehinmei
     */
    public void setPastehinmei(String pastehinmei) {
        this.pastehinmei = pastehinmei;
    }

    /**
     * ﾍﾟｰｽﾄﾛｯﾄNo
     *
     * @return pastelotno
     */
    public String getPastelotno() {
        return pastelotno;
    }

    /**
     * ﾍﾟｰｽﾄﾛｯﾄNo
     *
     * @param pastelotno セットする pastelotno
     */
    public void setPastelotno(String pastelotno) {
        this.pastelotno = pastelotno;
    }

    /**
     * ﾍﾟｰｽﾄ再生回数
     *
     * @return pastesaiseikaisuu
     */
    public Integer getPastesaiseikaisuu() {
        return pastesaiseikaisuu;
    }

    /**
     * ﾍﾟｰｽﾄ再生回数
     *
     * @param pastesaiseikaisuu セットする pastesaiseikaisuu
     */
    public void setPastesaiseikaisuu(Integer pastesaiseikaisuu) {
        this.pastesaiseikaisuu = pastesaiseikaisuu;
    }

    /**
     * ﾍﾟｰｽﾄ粘度
     *
     * @return pastenendo
     */
    public Integer getPastenendo() {
        return pastenendo;
    }

    /**
     * ﾍﾟｰｽﾄ粘度
     *
     * @param pastenendo セットする pastenendo
     */
    public void setPastenendo(Integer pastenendo) {
        this.pastenendo = pastenendo;
    }

    /**
     * 条件設定者
     *
     * @return setteisya
     */
    public String getSetteisya() {
        return setteisya;
    }

    /**
     * 条件設定者
     *
     * @param setteisya セットする setteisya
     */
    public void setSetteisya(String setteisya) {
        this.setteisya = setteisya;
    }

    /**
     * 開始日時
     *
     * @return startdatetime
     */
    public Timestamp getStartdatetime() {
        return startdatetime;
    }

    /**
     * 開始日時
     *
     * @param startdatetime セットする startdatetime
     */
    public void setStartdatetime(Timestamp startdatetime) {
        this.startdatetime = startdatetime;
    }

    /**
     * 開始担当者
     *
     * @return starttantosyacode
     */
    public String getStarttantosyacode() {
        return starttantosyacode;
    }

    /**
     * 開始担当者
     *
     * @param starttantosyacode セットする starttantosyacode
     */
    public void setStarttantosyacode(String starttantosyacode) {
        this.starttantosyacode = starttantosyacode;
    }

    /**
     * ｷｬﾘｱﾃｰﾌﾟ
     *
     * @return carriertape
     */
    public String getCarriertape() {
        return carriertape;
    }

    /**
     * ｷｬﾘｱﾃｰﾌﾟ
     *
     * @param carriertape セットする carriertape
     */
    public void setCarriertape(String carriertape) {
        this.carriertape = carriertape;
    }

    /**
     * 磁器L寸法(MAX)
     * @return jikilsunpoumax
     */
    public BigDecimal getJikilsunpoumax() {
        return jikilsunpoumax;
    }

    /**
     * 磁器L寸法(MAX)
     * @param jikilsunpoumax 
     */
    public void setJikilsunpoumax(BigDecimal jikilsunpoumax) {
        this.jikilsunpoumax = jikilsunpoumax;
    }

    /**
     * 磁器L寸法(MIN)
     * @return jikilsunpoumin
     */
    public BigDecimal getJikilsunpoumin() {
        return jikilsunpoumin;
    }

    /**
     * 磁器L寸法(MIN)
     * @param jikilsunpoumin 
     */
    public void setJikilsunpoumin(BigDecimal jikilsunpoumin) {
        this.jikilsunpoumin = jikilsunpoumin;
    }

    /**
     * 磁器W寸法(MAX)
     * @return jikiwsunpoumax
     */
    public BigDecimal getJikiwsunpoumax() {
        return jikiwsunpoumax;
    }

    /**
     * 磁器W寸法(MAX)
     * @param jikiwsunpoumax 
     */
    public void setJikiwsunpoumax(BigDecimal jikiwsunpoumax) {
        this.jikiwsunpoumax = jikiwsunpoumax;
    }

    /**
     * 磁器W寸法(MIN)
     * @return jikiwsunpoumin
     */
    public BigDecimal getJikiwsunpoumin() {
        return jikiwsunpoumin;
    }

    /**
     * 磁器W寸法(MIN)
     * @param jikiwsunpoumin 
     */
    public void setJikiwsunpoumin(BigDecimal jikiwsunpoumin) {
        this.jikiwsunpoumin = jikiwsunpoumin;
    }

    /**
     * 磁器T寸法(MAX)
     * @return jikitsunpoumax
     */
    public BigDecimal getJikitsunpoumax() {
        return jikitsunpoumax;
    }

    /**
     * 磁器T寸法(MAX)
     * @param jikitsunpoumax 
     */
    public void setJikitsunpoumax(BigDecimal jikitsunpoumax) {
        this.jikitsunpoumax = jikitsunpoumax;
    }

    /**
     * 磁器T寸法(MIN)
     * @return jikitsunpoumin
     */
    public BigDecimal getJikitsunpoumin() {
        return jikitsunpoumin;
    }

    /**
     * 磁器T寸法(MIN)
     * @param jikitsunpoumin 
     */
    public void setJikitsunpoumin(BigDecimal jikitsunpoumin) {
        this.jikitsunpoumin = jikitsunpoumin;
    }

    /**
     * 印刷幅
     *
     * @return insatsuhaba
     */
    public BigDecimal getInsatsuhaba() {
        return insatsuhaba;
    }

    /**
     * 印刷幅
     *
     * @param insatsuhaba セットする insatsuhaba
     */
    public void setInsatsuhaba(BigDecimal insatsuhaba) {
        this.insatsuhaba = insatsuhaba;
    }

    /**
     * 回り込みAVE
     *
     * @return mawarikomi
     */
    public BigDecimal getMawarikomi() {
        return mawarikomi;
    }

    /**
     * 回り込みAVE
     *
     * @param mawarikomi セットする mawarikomi
     */
    public void setMawarikomi(BigDecimal mawarikomi) {
        this.mawarikomi = mawarikomi;
    }

    /**
     * 位置ｽﾞﾚMAX
     *
     * @return itizure
     */
    public BigDecimal getItizure() {
        return itizure;
    }

    /**
     * 位置ｽﾞﾚMAX
     *
     * @param itizure セットする itizure
     */
    public void setItizure(BigDecimal itizure) {
        this.itizure = itizure;
    }

    /**
     * 端子間幅MIN
     *
     * @return tansikanhaba
     */
    public BigDecimal getTansikanhaba() {
        return tansikanhaba;
    }

    /**
     * 端子間幅MIN
     *
     * @param tansikanhaba セットする tansikanhaba
     */
    public void setTansikanhaba(BigDecimal tansikanhaba) {
        this.tansikanhaba = tansikanhaba;
    }

    /**
     * 塗布後外観結果
     *
     * @return tofugogaikan
     */
    public String getTofugogaikan() {
        return tofugogaikan;
    }

    /**
     * 塗布後外観結果
     *
     * @param tofugogaikan セットする tofugogaikan
     */
    public void setTofugogaikan(String tofugogaikan) {
        this.tofugogaikan = tofugogaikan;
    }

    /**
     * 処理個数
     *
     * @return syorikosuu
     */
    public Integer getSyorikosuu() {
        return syorikosuu;
    }

    /**
     * 処理個数
     *
     * @param syorikosuu セットする syorikosuu
     */
    public void setSyorikosuu(Integer syorikosuu) {
        this.syorikosuu = syorikosuu;
    }

    /**
     * 重量
     *
     * @return juryou
     */
    public BigDecimal getJuryou() {
        return juryou;
    }

    /**
     * 重量
     *
     * @param juryou セットする juryou
     */
    public void setJuryou(BigDecimal juryou) {
        this.juryou = juryou;
    }

    /**
     * 終了日時
     *
     * @return enddatetime
     */
    public Timestamp getEnddatetime() {
        return enddatetime;
    }

    /**
     * 終了日時
     *
     * @param enddatetime セットする enddatetime
     */
    public void setEnddatetime(Timestamp enddatetime) {
        this.enddatetime = enddatetime;
    }

    /**
     * 作業場所
     *
     * @return sagyobasyo
     */
    public String getSagyobasyo() {
        return sagyobasyo;
    }

    /**
     * 作業場所
     *
     * @param sagyobasyo セットする sagyobasyo
     */
    public void setSagyobasyo(String sagyobasyo) {
        this.sagyobasyo = sagyobasyo;
    }

}
