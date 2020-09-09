/*
 * Copyright 2020 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2020/09/03<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SR_CKNSUNPOU1のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2020/09/03
 */
public class SrCknsunpou1 {
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
     * lotpre
     */
    private String lotpre;

    /**
     * kensabi
     */
    private Timestamp kensabi;

    /**
     * tantousya
     */
    private String tantousya;

    /**
     * fukahi
     */
    private String fukahi;

    /**
     * samplesuu
     */
    private Integer samplesuu;

    /**
     * hantei
     */
    private String hantei;

    /**
     * 備考1
     */
    private String bikou1;

    /**
     * 備考2
     */
    private String bikou2;

    /**
     * 実績No
     */
    private Integer jissekino;

    /**
     * TorokuNichiji
     */
    private Timestamp TorokuNichiji;

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
     * LOTPRE
     *
     * @return the lotpre
     */
    public String getLotpre() {
        return lotpre;
    }

    /**
     * LOTPRE
     *
     * @param lotpre the lotpre to set
     */
    public void setLotpre(String lotpre) {
        this.lotpre = lotpre;
    }

    /**
     * KENSABI
     *
     * @return the kensabi
     */
    public Timestamp getKensabi() {
        return kensabi;
    }

    /**
     * KENSABI
     *
     * @param kensabi the kensabi to set
     */
    public void setKensabi(Timestamp kensabi) {
        this.kensabi = kensabi;
    }

    /**
     * TANTOUSYA
     *
     * @return the tantousya
     */
    public String getTantousya() {
        return tantousya;
    }

    /**
     * TANTOUSYA
     *
     * @param tantousya the tantousya to set
     */
    public void setTantousya(String tantousya) {
        this.tantousya = tantousya;
    }

    /**
     * FUKAHI
     *
     * @return the fukahi
     */
    public String getFukahi() {
        return fukahi;
    }

    /**
     * FUKAHI
     *
     * @param fukahi the fukahi to set
     */
    public void setFukahi(String fukahi) {
        this.fukahi = fukahi;
    }

    /**
     * SAMPLESUU
     *
     * @return the samplesuu
     */
    public Integer getSamplesuu() {
        return samplesuu;
    }

    /**
     * SAMPLESUU
     *
     * @param samplesuu the samplesuu to set
     */
    public void setSamplesuu(Integer samplesuu) {
        this.samplesuu = samplesuu;
    }

    /**
     * HANTEI
     *
     * @return the hantei
     */
    public String getHantei() {
        return hantei;
    }

    /**
     * HANTEI
     *
     * @param hantei the hantei to set
     */
    public void setHantei(String hantei) {
        this.hantei = hantei;
    }

    /**
     * 備考1
     *
     * @return the bikou1
     */
    public String getBikou1() {
        return bikou1;
    }

    /**
     * 備考1
     *
     * @param bikou1 the bikou1 to set
     */
    public void setBikou1(String bikou1) {
        this.bikou1 = bikou1;
    }

    /**
     * 備考2
     *
     * @return the bikou2
     */
    public String getBikou2() {
        return bikou2;
    }

    /**
     * 備考2
     *
     * @param bikou2 the bikou2 to set
     */
    public void setBikou2(String bikou2) {
        this.bikou2 = bikou2;
    }

    /**
     * JISSEKINO
     *
     * @return the jissekino
     */
    public Integer getJissekino() {
        return jissekino;
    }

    /**
     * JISSEKINO
     *
     * @param jissekino the jissekino to set
     */
    public void setJissekino(Integer jissekino) {
        this.jissekino = jissekino;
    }

    /**
     * TOROKUNICHIJI
     *
     * @return the TorokuNichiji
     */
    public Timestamp getTorokuNichiji() {
        return TorokuNichiji;
    }

    /**
     * TOROKUNICHIJI
     *
     * @param TorokuNichiji the TorokuNichiji to set
     */
    public void setTorokuNichiji(Timestamp TorokuNichiji) {
        this.TorokuNichiji = TorokuNichiji;
    }

}