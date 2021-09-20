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
 * 変更日	2021/09/20<br>
 * 計画書No	MB2108-DK001<br>
 * 変更者	SRC Y.Kurozumi<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 焼成・真空脱脂ｻﾔ詰め履歴検索画面のモデルクラスです。
 *
 * @author SRC Y.Kurozumi
 * @since 2021/09/20
 */
public class GXHDO201B050Model implements Serializable{
    
    /** ﾛｯﾄNo. */
    private String lotno = "";
    /** KCPNO */
    private String kcpno = "";
    /** 客先 */
    private String tokuisaki = "";
    /** ﾛｯﾄ区分 */
    private String lotkubuncode = "";
    /** ｵｰﾅｰ */
    private String ownercode = "";
    /** 処理数 */
    private Integer kosuu = null;
    /** 単位重量 */
    private BigDecimal ukeiretannijyuryo = null;
    /** 総重量 */
    private BigDecimal ukeiresoujyuryou = null;
    /** 号機ｺｰﾄﾞ */
    private String sayadumegouki = "";
    /** ｻﾔ詰め方法 */
    private String sayadumehouhou = "";
    /** ｻﾔ種類 */
    private String sayasyurui = "";
    /** 粉まぶし */
    private String konamabushi = "";
    /** ｻﾔ詰め量 */
    private Integer sayatumeryou = null;
    /** ｻﾔ枚数 */
    private Integer sayasuu = null;
    /** 開始日時 */
    private Timestamp kaishinichiji = null;
    /** 開始担当者 */
    private String tantousya = "";
    /** 開始確認者 */
    private String startkakuninsyacode = "";
    /** 終了日時 */
    private Timestamp syuuryounichiji = null;
    /** 終了担当者 */
    private String endtantousyacode = "";
    /** 備考1 */
    private String bikou1 = "";
    /** 備考2 */
    private String bikou2 = "";
    /** 備考3 */
    private String bikou3 = "";
    /** 備考4 */
    private String bikou4 = "";
    
    /**
     * ﾛｯﾄNo.
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * ﾛｯﾄNo.
     * @param lotno the lotno to set
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * KCPNO
     * @return the kcpno
     */
    public String getKcpno() {
        return kcpno;
    }

    /**
     * KCPNO
     * @param kcpno the kcpno to set
     */
    public void setKcpno(String kcpno) {
        this.kcpno = kcpno;
    }

    /**
     * 客先
     * @return the tokuisaki
     */
    public String getTokuisaki() {
        return tokuisaki;
    }

    /**
     * 客先
     * @param tokuisaki the tokuisaki to set
     */
    public void setTokuisaki(String tokuisaki) {
        this.tokuisaki = tokuisaki;
    }

    /**
     * ﾛｯﾄ区分
     * @return the lotkubuncode
     */
    public String getLotkubuncode() {
        return lotkubuncode;
    }

    /**
     * ﾛｯﾄ区分
     * @param lotkubuncode the lotkubuncode to set
     */
    public void setLotkubuncode(String lotkubuncode) {
        this.lotkubuncode = lotkubuncode;
    }

    /**
     * ｵｰﾅｰ
     * @return the ownercode
     */
    public String getOwnercode() {
        return ownercode;
    }

    /**
     * ｵｰﾅｰ
     * @param ownercode the ownercode to set
     */
    public void setOwnercode(String ownercode) {
        this.ownercode = ownercode;
    }

    /**
     * 処理数
     * @return the kosuu
     */
    public Integer getKosuu() {
        return kosuu;
    }

    /**
     * 処理数
     * @param kosuu the kosuu to set
     */
    public void setKosuu(Integer kosuu) {
        this.kosuu = kosuu;
    }

    /**
     * 単位重量
     * @return the ukeiretannijyuryo
     */
    public BigDecimal getUkeiretannijyuryo() {
        return ukeiretannijyuryo;
    }

    /**
     * 単位重量
     * @param ukeiretannijyuryo the ukeiretannijyuryo to set
     */
    public void setUkeiretannijyuryo(BigDecimal ukeiretannijyuryo) {
        this.ukeiretannijyuryo = ukeiretannijyuryo;
    }

    /**
     * 総重量
     * @return the ukeiresoujyuryou
     */
    public BigDecimal getUkeiresoujyuryou() {
        return ukeiresoujyuryou;
    }

    /**
     * 総重量
     * @param ukeiresoujyuryou the ukeiresoujyuryou to set
     */
    public void setUkeiresoujyuryou(BigDecimal ukeiresoujyuryou) {
        this.ukeiresoujyuryou = ukeiresoujyuryou;
    }

    /**
     * 号機ｺｰﾄﾞ
     * @return the sayadumegouki
     */
    public String getSayadumegouki() {
        return sayadumegouki;
    }

    /**
     * 号機ｺｰﾄﾞ
     * @param sayadumegouki the sayadumegouki to set
     */
    public void setSayadumegouki(String sayadumegouki) {
        this.sayadumegouki = sayadumegouki;
    }

    /**
     * ｻﾔ詰め方法
     * @return the sayadumehouhou
     */
    public String getSayadumehouhou() {
        return sayadumehouhou;
    }

    /**
     * ｻﾔ詰め方法
     * @param sayadumehouhou the sayadumehouhou to set
     */
    public void setSayadumehouhou(String sayadumehouhou) {
        this.sayadumehouhou = sayadumehouhou;
    }

    /**
     * ｻﾔ種類
     * @return the sayasyurui
     */
    public String getSayasyurui() {
        return sayasyurui;
    }

    /**
     * ｻﾔ種類
     * @param sayasyurui the sayasyurui to set
     */
    public void setSayasyurui(String sayasyurui) {
        this.sayasyurui = sayasyurui;
    }

    /**
     * 粉まぶし
     * @return the konamabushi
     */
    public String getKonamabushi() {
        return konamabushi;
    }

    /**
     * 粉まぶし
     * @param konamabushi the konamabushi to set
     */
    public void setKonamabushi(String konamabushi) {
        this.konamabushi = konamabushi;
    }

    /**
     * ｻﾔ詰め量
     * @return the sayatumeryou
     */
    public Integer getSayatumeryou() {
        return sayatumeryou;
    }

    /**
     * ｻﾔ詰め量
     * @param sayatumeryou the sayatumeryou to set
     */
    public void setSayatumeryou(Integer sayatumeryou) {
        this.sayatumeryou = sayatumeryou;
    }

    /**
     * ｻﾔ枚数
     * @return the sayasuu
     */
    public Integer getSayasuu() {
        return sayasuu;
    }

    /**
     * ｻﾔ枚数
     * @param sayasuu the sayasuu to set
     */
    public void setSayasuu(Integer sayasuu) {
        this.sayasuu = sayasuu;
    }

    /**
     * 開始日時
     * @return the kaishinichiji
     */
    public Timestamp getKaishinichiji() {
        return kaishinichiji;
    }

    /**
     * 開始日時
     * @param kaishinichiji the kaishinichiji to set
     */
    public void setKaishinichiji(Timestamp kaishinichiji) {
        this.kaishinichiji = kaishinichiji;
    }

    /**
     * 開始担当者
     * @return the tantousya
     */
    public String getTantousya() {
        return tantousya;
    }

    /**
     * 開始担当者
     * @param tantousya the tantousya to set
     */
    public void setTantousya(String tantousya) {
        this.tantousya = tantousya;
    }

    /**
     * 開始確認者
     * @return the startkakuninsyacode
     */
    public String getStartkakuninsyacode() {
        return startkakuninsyacode;
    }

    /**
     * 開始確認者
     * @param startkakuninsyacode the startkakuninsyacode to set
     */
    public void setStartkakuninsyacode(String startkakuninsyacode) {
        this.startkakuninsyacode = startkakuninsyacode;
    }

    /**
     * 終了日時
     * @return the syuuryounichiji
     */
    public Timestamp getSyuuryounichiji() {
        return syuuryounichiji;
    }

    /**
     * 終了日時
     * @param syuuryounichiji the syuuryounichiji to set
     */
    public void setSyuuryounichiji(Timestamp syuuryounichiji) {
        this.syuuryounichiji = syuuryounichiji;
    }

    /**
     * 終了担当者
     * @return the endtantousyacode
     */
    public String getEndtantousyacode() {
        return endtantousyacode;
    }

    /**
     * 終了担当者
     * @param endtantousyacode the endtantousyacode to set
     */
    public void setEndtantousyacode(String endtantousyacode) {
        this.endtantousyacode = endtantousyacode;
    }

    /**
     * 備考1
     * @return the bikou1
     */
    public String getBikou1() {
        return bikou1;
    }

    /**
     * 備考1
     * @param bikou1 the bikou1 to set
     */
    public void setBikou1(String bikou1) {
        this.bikou1 = bikou1;
    }

    /**
     * 備考2
     * @return the bikou2
     */
    public String getBikou2() {
        return bikou2;
    }

    /**
     * 備考2
     * @param bikou2 the bikou2 to set
     */
    public void setBikou2(String bikou2) {
        this.bikou2 = bikou2;
    }

    /**
     * 備考3
     * @return the bikou3
     */
    public String getBikou3() {
        return bikou3;
    }

    /**
     * 備考3
     * @param bikou3 the bikou3 to set
     */
    public void setBikou3(String bikou3) {
        this.bikou3 = bikou3;
    }

    /**
     * 備考4
     * @return the bikou4
     */
    public String getBikou4() {
        return bikou4;
    }

    /**
     * 備考4
     * @param bikou4 the bikou4 to set
     */
    public void setBikou4(String bikou4) {
        this.bikou4 = bikou4;
    }

    
}
