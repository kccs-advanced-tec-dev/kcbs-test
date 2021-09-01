/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/07/15<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS DengH<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 原材料規格一覧表示機能検索画面のモデルクラスです
 *
 * @author KCSS DengH
 * @since 2021/07/15
 */
public class GXHDO501CModel implements Serializable {

    /**
     * 設計No
     */
    private Integer sekkeino = null;

    /**
     * 工場ｺｰﾄﾞ
     */
    private String kojyo = "";

    /**
     * LotNo
     */
    private String lotno = "";

    /**
     * 枝番
     */
    private String edaban = "";

    /**
     * 種類
     */
    private String syurui = "";

    /**
     * 品名
     */
    private String hinmei = "";

    /**
     * ﾊﾟﾀｰﾝ
     */
    private Integer pattern = null;

    /**
     * 担当者
     */
    private String tantousya = "";

    /**
     * 登録日
     */
    private Timestamp sekkeidate = null;

    /**
     * 更新日
     */
    private Timestamp koushinnichiji = null;

    /**
     * 設計No
     *
     * @return the sekkeino
     */
    public Integer getSekkeino() {
        return sekkeino;
    }

    /**
     * 設計No
     *
     * @param sekkeino the sekkeino to set
     */
    public void setSekkeino(Integer sekkeino) {
        this.sekkeino = sekkeino;
    }

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
     * LotNo
     *
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * LotNo
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
     * 種類
     *
     * @return the syurui
     */
    public String getSyurui() {
        return syurui;
    }

    /**
     * 種類
     *
     * @param syurui the syurui to set
     */
    public void setSyurui(String syurui) {
        this.syurui = syurui;
    }

    /**
     * 品名
     *
     * @return the hinmei
     */
    public String getHinmei() {
        return hinmei;
    }

    /**
     * 品名
     *
     * @param hinmei the hinmei to set
     */
    public void setHinmei(String hinmei) {
        this.hinmei = hinmei;
    }

    /**
     * ﾊﾟﾀｰﾝ
     *
     * @return the pattern
     */
    public Integer getPattern() {
        return pattern;
    }

    /**
     * ﾊﾟﾀｰﾝ
     *
     * @param pattern the pattern to set
     */
    public void setPattern(Integer pattern) {
        this.pattern = pattern;
    }

    /**
     * 担当者
     *
     * @return the tantousya
     */
    public String getTantousya() {
        return tantousya;
    }

    /**
     * 担当者
     *
     * @param tantousya the tantousya to set
     */
    public void setTantousya(String tantousya) {
        this.tantousya = tantousya;
    }

    /**
     * 登録日
     *
     * @return the sekkeidate
     */
    public Timestamp getSekkeiDate() {
        return sekkeidate;
    }

    /**
     * 登録日
     *
     * @param sekkeidate the sekkeidate to set
     */
    public void setSekkeiDate(Timestamp sekkeidate) {
        this.sekkeidate = sekkeidate;
    }

    /**
     * 更新日
     *
     * @return the koushinnichiji
     */
    public Timestamp getKoushinnichiji() {
        return koushinnichiji;
    }

    /**
     * 更新日
     *
     * @param koushinnichiji the koushinnichiji to set
     */
    public void setKoushinnichiji(Timestamp koushinnichiji) {
        this.koushinnichiji = koushinnichiji;
    }

}
