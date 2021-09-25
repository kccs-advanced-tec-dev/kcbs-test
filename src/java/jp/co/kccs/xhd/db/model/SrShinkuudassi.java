/*
 * Copyright 2020 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/09/22<br>
 * 計画書No	MB2108-DK001<br>
 * 変更者	SRC H.Yamagata<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * sr_shinkuudassi:真空脱脂のモデルクラスです。
 *
 * @author SRC H.Yamagata
 * @since  2021/09/22
 */
public class SrShinkuudassi {

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
     * 受入ｻﾔ枚数
     */
    private Integer ukeiresayasuu;

    /**
     * 号機
     */
    private String goki;

    /**
     * ﾌﾟﾛｸﾞﾗﾑNo.
     */
    private Integer programno;

    /**
     * 最高温度
     */
    private Integer ondo;

    /**
     * ｷｰﾌﾟ時間
     */
    private BigDecimal keepjikan;

    /**
     * 総時間
     */
    private BigDecimal totaljikan;

    /**
     * 投入ｻﾔ枚数
     */
    private Integer tounyusettersuu;

    /**
     * 開始日時
     */
    private Timestamp kaisinichiji;

    /**
     * 開始担当者
     */
    private String kaisitantosya;

    /**
     * 開始確認者
     */
    private String kaisikakuninsya;

    /**
     * 終了日時
     */
    private Timestamp syuryonichiji;

    /**
     * 終了担当者
     */
    private String syuryotantosya;

    /**
     * 回収ｻﾔ枚数
     */
    private Integer kaisyusettersuu;

    /**
     * 備考1
     */
    private String bikou1;

    /**
     * 備考2
     */
    private String bikou2;

    /**
     * 備考3
     */
    private String bikou3;

    /**
     * 備考4
     */
    private String bikou4;

    /**
     * revision
     */
    private Long revision;

    /**
     * 削除ﾌﾗｸﾞ
     */
    private Integer deleteflag;

    /**
     * @return the kojyo
     */
    public String getKojyo() {
        return kojyo;
    }

    /**
     * @param kojyo the kojyo to set
     */
    public void setKojyo(String kojyo) {
        this.kojyo = kojyo;
    }

    /**
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * @param lotno the lotno to set
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * @return the edaban
     */
    public String getEdaban() {
        return edaban;
    }

    /**
     * @param edaban the edaban to set
     */
    public void setEdaban(String edaban) {
        this.edaban = edaban;
    }

    /**
     * @return the kcpno
     */
    public String getKcpno() {
        return kcpno;
    }

    /**
     * @param kcpno the kcpno to set
     */
    public void setKcpno(String kcpno) {
        this.kcpno = kcpno;
    }

    /**
     * @return the tokuisaki
     */
    public String getTokuisaki() {
        return tokuisaki;
    }

    /**
     * @param tokuisaki the tokuisaki to set
     */
    public void setTokuisaki(String tokuisaki) {
        this.tokuisaki = tokuisaki;
    }

    /**
     * @return the lotkubuncode
     */
    public String getLotkubuncode() {
        return lotkubuncode;
    }

    /**
     * @param lotkubuncode the lotkubuncode to set
     */
    public void setLotkubuncode(String lotkubuncode) {
        this.lotkubuncode = lotkubuncode;
    }

    /**
     * @return the ownercode
     */
    public String getOwnercode() {
        return ownercode;
    }

    /**
     * @param ownercode the ownercode to set
     */
    public void setOwnercode(String ownercode) {
        this.ownercode = ownercode;
    }

    /**
     * @return the ukeiresayasuu
     */
    public Integer getUkeiresayasuu() {
        return ukeiresayasuu;
    }

    /**
     * @param ukeiresayasuu the ukeiresayasuu to set
     */
    public void setUkeiresayasuu(Integer ukeiresayasuu) {
        this.ukeiresayasuu = ukeiresayasuu;
    }

    /**
     * @return the goki
     */
    public String getGoki() {
        return goki;
    }

    /**
     * @param goki the goki to set
     */
    public void setGoki(String goki) {
        this.goki = goki;
    }

    /**
     * @return the programno
     */
    public Integer getProgramno() {
        return programno;
    }

    /**
     * @param programno the programno to set
     */
    public void setProgramno(Integer programno) {
        this.programno = programno;
    }

    /**
     * @return the ondo
     */
    public Integer getOndo() {
        return ondo;
    }

    /**
     * @param ondo the ondo to set
     */
    public void setOndo(Integer ondo) {
        this.ondo = ondo;
    }

    /**
     * @return the keepjikan
     */
    public BigDecimal getKeepjikan() {
        return keepjikan;
    }

    /**
     * @param keepjikan the keepjikan to set
     */
    public void setKeepjikan(BigDecimal keepjikan) {
        this.keepjikan = keepjikan;
    }

    /**
     * @return the totaljikan
     */
    public BigDecimal getTotaljikan() {
        return totaljikan;
    }

    /**
     * @param totaljikan the totaljikan to set
     */
    public void setTotaljikan(BigDecimal totaljikan) {
        this.totaljikan = totaljikan;
    }

    /**
     * @return the tounyusettersuu
     */
    public Integer getTounyusettersuu() {
        return tounyusettersuu;
    }

    /**
     * @param tounyusettersuu the tounyusettersuu to set
     */
    public void setTounyusettersuu(Integer tounyusettersuu) {
        this.tounyusettersuu = tounyusettersuu;
    }

    /**
     * @return the kaisinichiji
     */
    public Timestamp getKaisinichiji() {
        return kaisinichiji;
    }

    /**
     * @param kaisinichiji the kaisinichiji to set
     */
    public void setKaisinichiji(Timestamp kaisinichiji) {
        this.kaisinichiji = kaisinichiji;
    }

    /**
     * @return the kaisitantosya
     */
    public String getKaisitantosya() {
        return kaisitantosya;
    }

    /**
     * @param kaisitantosya the kaisitantosya to set
     */
    public void setKaisitantosya(String kaisitantosya) {
        this.kaisitantosya = kaisitantosya;
    }

    /**
     * @return the kaisikakuninsya
     */
    public String getKaisikakuninsya() {
        return kaisikakuninsya;
    }

    /**
     * @param kaisikakuninsya the kaisikakuninsya to set
     */
    public void setKaisikakuninsya(String kaisikakuninsya) {
        this.kaisikakuninsya = kaisikakuninsya;
    }

    /**
     * @return the syuryonichiji
     */
    public Timestamp getSyuryonichiji() {
        return syuryonichiji;
    }

    /**
     * @param syuryonichiji the syuryonichiji to set
     */
    public void setSyuryonichiji(Timestamp syuryonichiji) {
        this.syuryonichiji = syuryonichiji;
    }

    /**
     * @return the syuryotantosya
     */
    public String getSyuryotantosya() {
        return syuryotantosya;
    }

    /**
     * @param syuryotantosya the syuryotantosya to set
     */
    public void setSyuryotantosya(String syuryotantosya) {
        this.syuryotantosya = syuryotantosya;
    }

    /**
     * @return the kaisyusettersuu
     */
    public Integer getKaisyusettersuu() {
        return kaisyusettersuu;
    }

    /**
     * @param kaisyusettersuu the kaisyusettersuu to set
     */
    public void setKaisyusettersuu(Integer kaisyusettersuu) {
        this.kaisyusettersuu = kaisyusettersuu;
    }

    /**
     * @return the bikou1
     */
    public String getBikou1() {
        return bikou1;
    }

    /**
     * @param bikou1 the bikou1 to set
     */
    public void setBikou1(String bikou1) {
        this.bikou1 = bikou1;
    }

    /**
     * @return the bikou2
     */
    public String getBikou2() {
        return bikou2;
    }

    /**
     * @param bikou2 the bikou2 to set
     */
    public void setBikou2(String bikou2) {
        this.bikou2 = bikou2;
    }

    /**
     * @return the bikou3
     */
    public String getBikou3() {
        return bikou3;
    }

    /**
     * @param bikou3 the bikou3 to set
     */
    public void setBikou3(String bikou3) {
        this.bikou3 = bikou3;
    }

    /**
     * @return the bikou4
     */
    public String getBikou4() {
        return bikou4;
    }

    /**
     * @param bikou4 the bikou4 to set
     */
    public void setBikou4(String bikou4) {
        this.bikou4 = bikou4;
    }

    /**
     * @return the revision
     */
    public Long getRevision() {
        return revision;
    }

    /**
     * @param revision the revision to set
     */
    public void setRevision(Long revision) {
        this.revision = revision;
    }

    /**
     * @return the deleteflag
     */
    public Integer getDeleteflag() {
        return deleteflag;
    }

    /**
     * @param deleteflag the deleteflag to set
     */
    public void setDeleteflag(Integer deleteflag) {
        this.deleteflag = deleteflag;
    }

}