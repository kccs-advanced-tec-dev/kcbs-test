/*
 * Copyright 2020 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.math.BigDecimal;

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
 * SR_CKNSUNPOU2のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2020/09/03
 */
public class SrCknsunpou2 {
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
     * nankome
     */
    private Integer nankome;

    /**
     * L寸法
     */
    private BigDecimal l;

    /**
     * lhantei
     */
    private String lhantei;

    /**
     * W寸法
     */
    private BigDecimal w;

    /**
     * whantei
     */
    private String whantei;

    /**
     * T寸法
     */
    private BigDecimal t;

    /**
     * thantei
     */
    private String thantei;

    /**
     * d
     */
    private BigDecimal d;

    /**
     * dhantei
     */
    private String dhantei;

    /**
     * 実績No
     */
    private Integer jissekino;

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
     * NANKOME
     *
     * @return the nankome
     */
    public Integer getNankome() {
        return nankome;
    }

    /**
     * NANKOME
     *
     * @param nankome the nankome to set
     */
    public void setNankome(Integer nankome) {
        this.nankome = nankome;
    }

    /**
     * L寸法
     *
     * @return the l
     */
    public BigDecimal getL() {
        return l;
    }

    /**
     * L寸法
     *
     * @param l the l to set
     */
    public void setL(BigDecimal l) {
        this.l = l;
    }

    /**
     * LHANTEI
     *
     * @return the lhantei
     */
    public String getLhantei() {
        return lhantei;
    }

    /**
     * LHANTEI
     *
     * @param lhantei the lhantei to set
     */
    public void setLhantei(String lhantei) {
        this.lhantei = lhantei;
    }

    /**
     * W寸法
     *
     * @return the w
     */
    public BigDecimal getW() {
        return w;
    }

    /**
     * W寸法
     *
     * @param w the w to set
     */
    public void setW(BigDecimal w) {
        this.w = w;
    }

    /**
     * WHANTEI
     *
     * @return the whantei
     */
    public String getWhantei() {
        return whantei;
    }

    /**
     * WHANTEI
     *
     * @param whantei the whantei to set
     */
    public void setWhantei(String whantei) {
        this.whantei = whantei;
    }

    /**
     * T寸法
     *
     * @return the t
     */
    public BigDecimal getT() {
        return t;
    }

    /**
     * T寸法
     *
     * @param t the t to set
     */
    public void setT(BigDecimal t) {
        this.t = t;
    }

    /**
     * THANTEI
     *
     * @return the thantei
     */
    public String getThantei() {
        return thantei;
    }

    /**
     * THANTEI
     *
     * @param thantei the thantei to set
     */
    public void setThantei(String thantei) {
        this.thantei = thantei;
    }

    /**
     * D
     *
     * @return the d
     */
    public BigDecimal getD() {
        return d;
    }

    /**
     * D
     *
     * @param d the d to set
     */
    public void setD(BigDecimal d) {
        this.d = d;
    }

    /**
     * DHANTEI
     *
     * @return the dhantei
     */
    public String getDhantei() {
        return dhantei;
    }

    /**
     * DHANTEI
     *
     * @param dhantei the dhantei to set
     */
    public void setDhantei(String dhantei) {
        this.dhantei = dhantei;
    }

    /**
     * 実績No
     *
     * @return the jissekino
     */
    public Integer getJissekino() {
        return jissekino;
    }

    /**
     * 実績No
     *
     * @param jissekino the jissekino to set
     */
    public void setJissekino(Integer jissekino) {
        this.jissekino = jissekino;
    }

}