/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.math.BigDecimal;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/12/03<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	863 F.Zhang<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * sr_mkmakuatsu(外部電極ﾒｯｷ膜厚)のモデルクラスです。
 *
 * @author 863 F.Zhang
 * @since 2019/12/03
 */
public class SrMkmakuatsu {

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
     * 測定数
     */
    private Integer sokuteikaisuu;

    /**
     * 測定No
     */
    private Integer sokuteino;

    /**
     * ﾊﾞﾚﾙNo
     */
    private Integer barelno;

    /**
     * Ni膜厚
     */
    private BigDecimal nimakuatsu;

    /**
     * Sn膜厚
     */
    private BigDecimal snmakuatsu;

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
     * 測定数
     *
     * @return sokuteikaisuu
     */
    public Integer getSokuteikaisuu() {
        return sokuteikaisuu;
    }

    /**
     * 測定数
     *
     * @param sokuteikaisuu セットする sokuteikaisuu
     */
    public void setSokuteikaisuu(Integer sokuteikaisuu) {
        this.sokuteikaisuu = sokuteikaisuu;
    }

    /**
     * 測定No
     *
     * @return sokuteino
     */
    public Integer getSokuteino() {
        return sokuteino;
    }

    /**
     * 測定No
     *
     * @param sokuteino セットする sokuteino
     */
    public void setSokuteino(Integer sokuteino) {
        this.sokuteino = sokuteino;
    }

    /**
     * ﾊﾞﾚﾙNo
     *
     * @return barelno
     */
    public Integer getBarelno() {
        return barelno;
    }

    /**
     * ﾊﾞﾚﾙNo
     *
     * @param barelno セットする barelno
     */
    public void setBarelno(Integer barelno) {
        this.barelno = barelno;
    }

    /**
     * Ni膜厚
     *
     * @return nimakuatsu
     */
    public BigDecimal getNimakuatsu() {
        return nimakuatsu;
    }

    /**
     * Ni膜厚
     *
     * @param nimakuatsu セットする nimakuatsu
     */
    public void setNimakuatsu(BigDecimal nimakuatsu) {
        this.nimakuatsu = nimakuatsu;
    }

    /**
     * Sn膜厚
     *
     * @return snmakuatsu
     */
    public BigDecimal getSnmakuatsu() {
        return snmakuatsu;
    }

    /**
     * Sn膜厚
     *
     * @param snmakuatsu セットする snmakuatsu
     */
    public void setSnmakuatsu(BigDecimal snmakuatsu) {
        this.snmakuatsu = snmakuatsu;
    }

}
