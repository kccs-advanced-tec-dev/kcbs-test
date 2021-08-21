/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.io.Serializable;
import java.util.List;
import jp.co.kccs.xhd.db.model.DaMkhyojunjoken;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	原材料規格管理機能(コンデンサ)<br>
 * <br>
 * 変更日	2021/07/30<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS wxf<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */

/**
 * GXHD501A(原材料規格ｱｯﾌﾟﾛｰﾄﾞ機能)のモデルクラスです。
 * 
 * @author KCSS wxf
 * @since  2021/07/30
 */
public class GXHDO501AModel extends DaMkhyojunjoken implements Serializable{
    /** ダウンロード用List */
    private List<String> kikakutilist = null;
    /** 結果(A列) */
    private String resulta = null;
    /** 結果(B列) */
    private String resultb = null;
    /**  採番した[品名] */
    private String hinmeisaiban;
    /** 製造LotNo */
    private String lotno = null;
    /** 重量 */
    private String jyuuryou = null;
    /** ﾛｯﾄ区分 */
    private String lotkubunn = null;
    /** ｵｰﾅｰ */
    private String owner = null;
    /** ﾊﾟﾀｰﾝ */
    private String patterns = null;
    /** 毎行の取込データ */
    private List<DaMkhyojunjoken> rowdata = null;

    /**
     * 採番した[品名]
     * @return the hinmeisaiban
     */
    public String getHinmeisaiban() {
        return hinmeisaiban;
    }

    /**
     * 重量
     * @return the jyuuryou
     */
    public String getJyuuryou() {
        return jyuuryou;
    }

    /**
     * ダウンロード用List
     * @return the kikakutilist
     */
    public List<String> getKikakutilist() {
        return kikakutilist;
    }

    /**
     * ﾛｯﾄ区分
     * @return the lotkubunn
     */
    public String getLotkubunn() {
        return lotkubunn;
    }

    /**
     * 製造LotNo
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * ｵｰﾅｰ
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * ﾊﾟﾀｰﾝ
     * @return the patterns
     */
    public String getPatterns() {
        return patterns;
    }

    /**
     * 結果(A列)
     * @return the resulta
     */
    public String getResulta() {
        return resulta;
    }

    /**
     * 結果(B列)
     * @return the resultb
     */
    public String getResultb() {
        return resultb;
    }

    /**
     * 毎行の取込データ
     * @return the rowdata
     */
    public List<DaMkhyojunjoken> getRowdata() {
        return rowdata;
    }

    /**
     * 採番した[品名]
     * @param hinmeisaiban the hinmeisaiban to set
     */
    public void setHinmeisaiban(String hinmeisaiban) {
        this.hinmeisaiban = hinmeisaiban;
    }

    /**
     * 重量
     * @param jyuuryou the jyuuryou to set
     */
    public void setJyuuryou(String jyuuryou) {
        this.jyuuryou = jyuuryou;
    }

    /**
     * ダウンロード用List
     * @param kikakutilist the kikakutilist to set
     */
    public void setKikakutilist(List<String> kikakutilist) {
        this.kikakutilist = kikakutilist;
    }

    /**
     * ﾛｯﾄ区分
     * @param lotkubunn the lotkubunn to set
     */
    public void setLotkubunn(String lotkubunn) {
        this.lotkubunn = lotkubunn;
    }

    /**
     * 製造LotNo
     * @param lotno the lotno to set
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * ｵｰﾅｰ
     * @param owner the owner to set
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * ﾊﾟﾀｰﾝ
     * @param patterns the patterns to set
     */
    public void setPatterns(String patterns) {
        this.patterns = patterns;
    }

    /**
     * 結果(A列)
     * @param resulta the resulta to set
     */
    public void setResulta(String resulta) {
        this.resulta = resulta;
    }

    /**
     * 結果(B列)
     * @param resultb the resultb to set
     */
    public void setResultb(String resultb) {
        this.resultb = resultb;
    }

    /**
     * 毎行の取込データ
     * @param rowdata the rowdata to set
     */
    public void setRowdata(List<DaMkhyojunjoken> rowdata) {
        this.rowdata = rowdata;
    }
}
