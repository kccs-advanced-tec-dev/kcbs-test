/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.io.Serializable;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日       2021/12/25<br>
 * 計画書No     MB2101-DK002<br>
 * 変更者       KCSS K.Jo<br>
 * 変更理由     新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * ｽﾘｯﾌﾟ作製・高圧分散履歴検索画面のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/12/25
 */
public class GXHDO202B034Model implements Serializable {
    /** WIPﾛｯﾄNo */
    private String lotno = "";

    /** ｽﾘｯﾌﾟ品名 */
    private String sliphinmei = "";

    /** ｽﾘｯﾌﾟLotNo */
    private String sliplotno = "";

    /** ﾛｯﾄ区分 */
    private String lotkubun = "";

    /** 原料記号 */
    private String genryoukigou = "";

    /** 高圧分散機 */
    private String kouatsubunsanki = "";

    /** 高圧分散号機 */
    private String kouatsubunsangouki = "";

    /** 排出容器内袋確認 */
    private Integer haisyutsuyoukiuchibukuro = null;

    /** 排出容器洗浄確認 */
    private Integer haisyutsuyoukisenjyou = null;

    /** 設備内洗浄確認 */
    private Integer setsubinaisenjyou = null;

    /** ﾉｽﾞﾙ径 */
    private String nozzlekei = "";

    /** 冷却水ﾊﾞﾙﾌﾞ開 */
    private String reikyakusuivalve = "";

    /** 冷却水温度規格 */
    private String reikyakusuiondokikaku = "";

    /** 冷却水温度 */
    private Integer reikyakusuiondo = null;

    /** 高圧分散回数 */
    private String kouatsubunsankaisuu = "";

    /** ﾀﾝｸにｱｰｽｸﾞﾘｯﾌﾟ接続 */
    private Integer tankearthgripsetsuzoku = null;

    /** 備考1 */
    private String bikou1 = "";

    /** 備考2 */
    private String bikou2 = "";

    /**
     * WIPﾛｯﾄNo
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * WIPﾛｯﾄNo
     * @param lotno the lotno to set
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * ｽﾘｯﾌﾟ品名
     * @return the sliphinmei
     */
    public String getSliphinmei() {
        return sliphinmei;
    }

    /**
     * ｽﾘｯﾌﾟ品名
     * @param sliphinmei the sliphinmei to set
     */
    public void setSliphinmei(String sliphinmei) {
        this.sliphinmei = sliphinmei;
    }

    /**
     * ｽﾘｯﾌﾟLotNo
     * @return the sliplotno
     */
    public String getSliplotno() {
        return sliplotno;
    }

    /**
     * ｽﾘｯﾌﾟLotNo
     * @param sliplotno the sliplotno to set
     */
    public void setSliplotno(String sliplotno) {
        this.sliplotno = sliplotno;
    }

    /**
     * ﾛｯﾄ区分
     * @return the lotkubun
     */
    public String getLotkubun() {
        return lotkubun;
    }

    /**
     * ﾛｯﾄ区分
     * @param lotkubun the lotkubun to set
     */
    public void setLotkubun(String lotkubun) {
        this.lotkubun = lotkubun;
    }

    /**
     * 原料記号
     * @return the genryoukigou
     */
    public String getGenryoukigou() {
        return genryoukigou;
    }

    /**
     * 原料記号
     * @param genryoukigou the genryoukigou to set
     */
    public void setGenryoukigou(String genryoukigou) {
        this.genryoukigou = genryoukigou;
    }

    /**
     * 高圧分散機
     * @return the kouatsubunsanki
     */
    public String getKouatsubunsanki() {
        return kouatsubunsanki;
    }

    /**
     * 高圧分散機
     * @param kouatsubunsanki the kouatsubunsanki to set
     */
    public void setKouatsubunsanki(String kouatsubunsanki) {
        this.kouatsubunsanki = kouatsubunsanki;
    }

    /**
     * 高圧分散号機
     * @return the kouatsubunsangouki
     */
    public String getKouatsubunsangouki() {
        return kouatsubunsangouki;
    }

    /**
     * 高圧分散号機
     * @param kouatsubunsangouki the kouatsubunsangouki to set
     */
    public void setKouatsubunsangouki(String kouatsubunsangouki) {
        this.kouatsubunsangouki = kouatsubunsangouki;
    }

    /**
     * 排出容器内袋確認
     * @return the haisyutsuyoukiuchibukuro
     */
    public Integer getHaisyutsuyoukiuchibukuro() {
        return haisyutsuyoukiuchibukuro;
    }

    /**
     * 排出容器内袋確認
     * @param haisyutsuyoukiuchibukuro the haisyutsuyoukiuchibukuro to set
     */
    public void setHaisyutsuyoukiuchibukuro(Integer haisyutsuyoukiuchibukuro) {
        this.haisyutsuyoukiuchibukuro = haisyutsuyoukiuchibukuro;
    }

    /**
     * 排出容器洗浄確認
     * @return the haisyutsuyoukisenjyou
     */
    public Integer getHaisyutsuyoukisenjyou() {
        return haisyutsuyoukisenjyou;
    }

    /**
     * 排出容器洗浄確認
     * @param haisyutsuyoukisenjyou the haisyutsuyoukisenjyou to set
     */
    public void setHaisyutsuyoukisenjyou(Integer haisyutsuyoukisenjyou) {
        this.haisyutsuyoukisenjyou = haisyutsuyoukisenjyou;
    }

    /**
     * 設備内洗浄確認
     * @return the setsubinaisenjyou
     */
    public Integer getSetsubinaisenjyou() {
        return setsubinaisenjyou;
    }

    /**
     * 設備内洗浄確認
     * @param setsubinaisenjyou the setsubinaisenjyou to set
     */
    public void setSetsubinaisenjyou(Integer setsubinaisenjyou) {
        this.setsubinaisenjyou = setsubinaisenjyou;
    }

    /**
     * ﾉｽﾞﾙ径
     * @return the nozzlekei
     */
    public String getNozzlekei() {
        return nozzlekei;
    }

    /**
     * ﾉｽﾞﾙ径
     * @param nozzlekei the nozzlekei to set
     */
    public void setNozzlekei(String nozzlekei) {
        this.nozzlekei = nozzlekei;
    }

    /**
     * 冷却水ﾊﾞﾙﾌﾞ開
     * @return the reikyakusuivalve
     */
    public String getReikyakusuivalve() {
        return reikyakusuivalve;
    }

    /**
     * 冷却水ﾊﾞﾙﾌﾞ開
     * @param reikyakusuivalve the reikyakusuivalve to set
     */
    public void setReikyakusuivalve(String reikyakusuivalve) {
        this.reikyakusuivalve = reikyakusuivalve;
    }

    /**
     * 冷却水温度規格
     * @return the reikyakusuiondokikaku
     */
    public String getReikyakusuiondokikaku() {
        return reikyakusuiondokikaku;
    }

    /**
     * 冷却水温度規格
     * @param reikyakusuiondokikaku the reikyakusuiondokikaku to set
     */
    public void setReikyakusuiondokikaku(String reikyakusuiondokikaku) {
        this.reikyakusuiondokikaku = reikyakusuiondokikaku;
    }

    /**
     * 冷却水温度
     * @return the reikyakusuiondo
     */
    public Integer getReikyakusuiondo() {
        return reikyakusuiondo;
    }

    /**
     * 冷却水温度
     * @param reikyakusuiondo the reikyakusuiondo to set
     */
    public void setReikyakusuiondo(Integer reikyakusuiondo) {
        this.reikyakusuiondo = reikyakusuiondo;
    }

    /**
     * 高圧分散回数
     * @return the kouatsubunsankaisuu
     */
    public String getKouatsubunsankaisuu() {
        return kouatsubunsankaisuu;
    }

    /**
     * 高圧分散回数
     * @param kouatsubunsankaisuu the kouatsubunsankaisuu to set
     */
    public void setKouatsubunsankaisuu(String kouatsubunsankaisuu) {
        this.kouatsubunsankaisuu = kouatsubunsankaisuu;
    }

    /**
     * ﾀﾝｸにｱｰｽｸﾞﾘｯﾌﾟ接続
     * @return the tankearthgripsetsuzoku
     */
    public Integer getTankearthgripsetsuzoku() {
        return tankearthgripsetsuzoku;
    }

    /**
     * ﾀﾝｸにｱｰｽｸﾞﾘｯﾌﾟ接続
     * @param tankearthgripsetsuzoku the tankearthgripsetsuzoku to set
     */
    public void setTankearthgripsetsuzoku(Integer tankearthgripsetsuzoku) {
        this.tankearthgripsetsuzoku = tankearthgripsetsuzoku;
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

}