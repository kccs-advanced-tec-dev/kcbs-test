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
 * 焼成・真空脱脂履歴検索画面のモデルクラスです。
 *
 * @author SRC Y.Kurozumi
 * @since 2021/09/20
 */
public class GXHDO201B051Model implements Serializable{
    
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
    /** 受入ｻﾔ枚数 */
    private Integer ukeiresayasuu = null;
    /** 号機 */
    private String goki = "";
    /** ﾌﾟﾛｸﾞﾗﾑNo. */
    private Integer programno = null;
    /** 最高温度 */
    private Integer ondo = null;
    /** ｷｰﾌﾟ時間 */
    private BigDecimal keepjikan = null;
    /** 総時間 */
    private BigDecimal totaljikan = null;
    /** 投入ｻﾔ枚数 */
    private Integer tounyusettersuu = null;
    /** 開始日時 */
    private Timestamp kaisinichiji = null;
    /** 開始担当者 */
    private String kaisitantosya = "";
    /** 開始確認者 */
    private String kaisikakuninsya = "";
    /** 終了日時 */
    private Timestamp syuryonichiji = null;
    /** 終了担当者 */
    private String syuryotantosya = "";
    /** 回収ｻﾔ枚数 */
    private Integer kaisyusettersuu = null;
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
     * 受入ｻﾔ枚数
     * @return the ukeiresayasuu
     */
    public Integer getUkeiresayasuu() {
        return ukeiresayasuu;
    }

    /**
     * 受入ｻﾔ枚数
     * @param ukeiresayasuu the ukeiresayasuu to set
     */
    public void setUkeiresayasuu(Integer ukeiresayasuu) {
        this.ukeiresayasuu = ukeiresayasuu;
    }

    /**
     * 号機
     * @return the goki
     */
    public String getGoki() {
        return goki;
    }

    /**
     * 号機
     * @param goki the goki to set
     */
    public void setGoki(String goki) {
        this.goki = goki;
    }

    /**
     * ﾌﾟﾛｸﾞﾗﾑNo.
     * @return the programno
     */
    public Integer getProgramno() {
        return programno;
    }

    /**
     * ﾌﾟﾛｸﾞﾗﾑNo.
     * @param programno the programno to set
     */
    public void setProgramno(Integer programno) {
        this.programno = programno;
    }

    /**
     * 最高温度
     * @return the ondo
     */
    public Integer getOndo() {
        return ondo;
    }

    /**
     * 最高温度
     * @param ondo the ondo to set
     */
    public void setOndo(Integer ondo) {
        this.ondo = ondo;
    }

    /**
     * ｷｰﾌﾟ時間
     * @return the keepjikan
     */
    public BigDecimal getKeepjikan() {
        return keepjikan;
    }

    /**
     * ｷｰﾌﾟ時間
     * @param keepjikan the keepjikan to set
     */
    public void setKeepjikan(BigDecimal keepjikan) {
        this.keepjikan = keepjikan;
    }

    /**
     * 総時間
     * @return the totaljikan
     */
    public BigDecimal getTotaljikan() {
        return totaljikan;
    }

    /**
     * 総時間
     * @param totaljikan the totaljikan to set
     */
    public void setTotaljikan(BigDecimal totaljikan) {
        this.totaljikan = totaljikan;
    }

    /**
     * 投入ｻﾔ枚数
     * @return the tounyusettersuu
     */
    public Integer getTounyusettersuu() {
        return tounyusettersuu;
    }

    /**
     * 投入ｻﾔ枚数
     * @param tounyusettersuu the tounyusettersuu to set
     */
    public void setTounyusettersuu(Integer tounyusettersuu) {
        this.tounyusettersuu = tounyusettersuu;
    }

    /**
     * 開始日時
     * @return the kaisinichiji
     */
    public Timestamp getKaisinichiji() {
        return kaisinichiji;
    }

    /**
     * 開始日時
     * @param kaisinichiji the kaisinichiji to set
     */
    public void setKaisinichiji(Timestamp kaisinichiji) {
        this.kaisinichiji = kaisinichiji;
    }

    /**
     * 開始担当者
     * @return the kaisitantosya
     */
    public String getKaisitantosya() {
        return kaisitantosya;
    }

    /**
     * 開始担当者
     * @param kaisitantosya the kaisitantosya to set
     */
    public void setKaisitantosya(String kaisitantosya) {
        this.kaisitantosya = kaisitantosya;
    }

    /**
     * 開始確認者
     * @return the kaisikakuninsya
     */
    public String getKaisikakuninsya() {
        return kaisikakuninsya;
    }

    /**
     * 開始確認者
     * @param kaisikakuninsya the kaisikakuninsya to set
     */
    public void setKaisikakuninsya(String kaisikakuninsya) {
        this.kaisikakuninsya = kaisikakuninsya;
    }

    /**
     * 終了日時
     * @return the syuryonichiji
     */
    public Timestamp getSyuryonichiji() {
        return syuryonichiji;
    }

    /**
     * 終了日時
     * @param syuryonichiji the syuryonichiji to set
     */
    public void setSyuryonichiji(Timestamp syuryonichiji) {
        this.syuryonichiji = syuryonichiji;
    }

    /**
     * 終了担当者
     * @return the syuryotantosya
     */
    public String getSyuryotantosya() {
        return syuryotantosya;
    }

    /**
     * 終了担当者
     * @param syuryotantosya the syuryotantosya to set
     */
    public void setSyuryotantosya(String syuryotantosya) {
        this.syuryotantosya = syuryotantosya;
    }

    /**
     * 回収ｻﾔ枚数
     * @return the kaisyusettersuu
     */
    public Integer getKaisyusettersuu() {
        return kaisyusettersuu;
    }

    /**
     * 回収ｻﾔ枚数
     * @param kaisyusettersuu the kaisyusettersuu to set
     */
    public void setKaisyusettersuu(Integer kaisyusettersuu) {
        this.kaisyusettersuu = kaisyusettersuu;
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
