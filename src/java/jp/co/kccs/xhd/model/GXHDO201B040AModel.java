/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2021/07/06<br>
 * 計画書No	MB2106-DS017<br>
 * 変更者	KCSS gc<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 品質DB履歴検索・履歴情報画面のモデルクラスです。
 *
 * @author KCSS gc
 * @since  2021/07/06
 */
public class GXHDO201B040AModel implements Serializable {
    /** 工場ｺｰﾄ */
    private	String   kojyo = "";
    /** ﾛｯﾄNo */
    private	String   lotno = "";
    /** 枝番 */
    private	String   edaban = "";
    /** 回数 */
    private	Integer   kaisu = null;
    /** ｶｯﾄ値履歴開始 */
    private	Timestamp  rirekistart = null;
    /** ｶｯﾄ値履歴終了 */
    private	Timestamp  rirekiend  = null;
    /** ｶｯﾄ値履歴BIN1カット値下限 */
    private	String   bin1low  = "";
    /** ｶｯﾄ値履歴BIN1カット値上限 */
    private	String   bin1high  = "";
    /** ｶｯﾄ値履歴BIN1カウンター数 */
    private	Integer   bin1countersuu  = null;
    /** ｶｯﾄ値履歴BIN2カット値下限 */
    private	String   bin2low  = "";
    /** ｶｯﾄ値履歴BIN2カット値上限 */
    private	String   bin2high  = "";
    /** ｶｯﾄ値履歴BIN2カウンター数 */
    private	Integer   bin2countersuu  = null;
    /** ｶｯﾄ値履歴BIN3カット値下限 */
    private	String   bin3low  = "";
    /** ｶｯﾄ値履歴BIN3カット値上限 */
    private	String   bin3high  = "";
    /** ｶｯﾄ値履歴BIN3カウンター数 */
    private	Integer   bin3countersuu  = null;
    /** ｶｯﾄ値履歴BIN4カット値下限 */
    private	String   bin4low  = "";
    /** ｶｯﾄ値履歴BIN4カット値上限 */
    private	String   bin4high  = "";
    /** ｶｯﾄ値履歴BIN4カウンター数 */
    private	Integer   bin4countersuu  = null;
    /** ｶｯﾄ値履歴BIN5カット値下限 */
    private	String   bin5low  = "";
    /** ｶｯﾄ値履歴BIN5カット値上限 */
    private	String   bin5high  = "";
    /** ｶｯﾄ値履歴BIN5カウンター数 */
    private	Integer   bin5countersuu  = null;
    /** ｶｯﾄ値履歴BIN6カット値下限 */
    private	String   bin6low  = "";
    /** ｶｯﾄ値履歴BIN6カット値上限 */
    private	String   bin6high  = "";
    /** ｶｯﾄ値履歴BIN6カウンター数 */
    private	Integer   bin6countersuu  = null;
    /** ｶｯﾄ値履歴BIN7カット値下限 */
    private	String   bin7low   = "";
    /** ｶｯﾄ値履歴BIN7カット値上限 */
    private	String   bin7high  = "";
    /** ｶｯﾄ値履歴BIN7カウンター数 */
    private	Integer   bin7countersuu  = null;
    /** ｶｯﾄ値履歴BIN8カット値下限 */
    private	String   bin8low  = "";
    /** ｶｯﾄ値履歴BIN8カット値上限 */
    private	String   bin8high  = "";
    /** ｶｯﾄ値履歴BIN8カウンター数 */
    private	Integer   bin8countersuu  = null;
    /** 削除ﾌﾗｸ */
    private	Integer   deleteflag  = null;
    /** 登録日時 */
    private	Timestamp  torokudate   = null;
    /** 更新日時 */
    private	Timestamp  koshindate   = null;

    /**
     * 工場ｺｰﾄ
     * @return the kojyo
     */
    public String getKojyo() {
        return kojyo;
    }
    /**
     * 工場ｺｰﾄ
     * @param kojyo the kojyo to set
     */
    public void setKojyo(String kojyo) {
        this.kojyo = kojyo;
    }
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
     * 枝番
     * @return the edaban
     */
    public String getEdaban() {
        return edaban;
    }
    /**
     * 枝番
     * @param edaban the edaban to set
     */
    public void setEdaban(String edaban) {
        this.edaban = edaban;
    }
    /**
     * 回数
     * @return the kaisu
     */
    public Integer getKaisu() {
        return kaisu;
    }
    /**
     * 回数
     * @param kaisu the kaisu to set
     */
    public void setKaisu(Integer kaisu) {
        this.kaisu = kaisu;
    }
    /**
     * ｶｯﾄ値履歴開始
     * @return the rirekistart
     */
    public Timestamp getRirekistart() {
        return rirekistart;
    }
   /**
     * ｶｯﾄ値履歴開始
     * @param rirekistart the rirekistart to set
     */
    public void setRirekistart(Timestamp rirekistart) {
        this.rirekistart = rirekistart;
    }
    /**
     * ｶｯﾄ値履歴終了
     * @return the rirekiend
     */
    public Timestamp getRirekiend() {
        return rirekiend;
    }
   /**
     * ｶｯﾄ値履歴終了
     * @param rirekiend the rirekiend to set
     */
    public void setRirekiend(Timestamp rirekiend) {
        this.rirekiend = rirekiend;
    }
    /**
     * ｶｯﾄ値履歴BIN1カット値下限
     * @return the bin1low
     */
    public String getBin1low() {
        return bin1low;
    }
   /**
     * ｶｯﾄ値履歴BIN1カット値下限
     * @param bin1low the bin1low to set
     */
    public void setBin1low(String bin1low) {
        this.bin1low = bin1low;
    }
    /**
     * ｶｯﾄ値履歴BIN1カット値上限
     * @return the bin1high
     */
    public String getBin1high() {
        return bin1high;
    }
   /**
     * ｶｯﾄ値履歴BIN1カット値上限
     * @param bin1high the bin1high to set
     */
    public void setBin1high(String bin1high) {
        this.bin1high = bin1high;
    }
    /**
     * ｶｯﾄ値履歴BIN1カウンター数
     * @return the bin1countersuu
     */
    public Integer getBin1countersuu() {
        return bin1countersuu;
    }
    /**
     * ｶｯﾄ値履歴BIN1カウンター数
     * @param bin1countersuu the bin1countersuu to set
     */
    public void setBin1countersuu(Integer bin1countersuu) {
        this.bin1countersuu = bin1countersuu;
    }
    /**
     * ｶｯﾄ値履歴BIN2カット値下限
     * @return the bin2low
     */
    public String getBin2low() {
        return bin2low;
    }
    /**
     * ｶｯﾄ値履歴BIN2カット値下限
     * @param bin2low the bin2low to set
     */
    public void setBin2low(String bin2low) {
        this.bin2low = bin2low;
    }
    /**
     * ｶｯﾄ値履歴BIN2カット値上限
     * @return the bin2high
     */
    public String getBin2high() {
        return bin2high;
    }
    /**
     * ｶｯﾄ値履歴BIN2カット値上限
     * @param bin2high the bin2high to set
     */
    public void setBin2high(String bin2high) {
        this.bin2high = bin2high;
    }
    /**
     * ｶｯﾄ値履歴BIN2カウンター数
     * @return the bin2countersuu
     */
    public Integer getBin2countersuu() {
        return bin2countersuu;
    }
    /**
     * ｶｯﾄ値履歴BIN2カウンター数
     * @param bin2countersuu the bin2countersuu to set
     */
    public void setBin2countersuu(Integer bin2countersuu) {
        this.bin2countersuu = bin2countersuu;
    }
    /**
     * ｶｯﾄ値履歴BIN3カット値下限
     * @return the bin3low
     */
    public String getBin3low() {
        return bin3low;
    }
    /**
     * ｶｯﾄ値履歴BIN3カット値下限
     * @param bin3low the bin3low to set
     */
    public void setBin3low(String bin3low) {
        this.bin3low = bin3low;
    }
    /**
     * ｶｯﾄ値履歴BIN3カット値上限
     * @return the bin3high
     */
    public String getBin3high() {
        return bin3high;
    }
    /**
     * ｶｯﾄ値履歴BIN3カット値上限
     * @param bin3high the bin3high to set
     */
    public void setBin3high(String bin3high) {
        this.bin3high = bin3high;
    }
    /**
     * ｶｯﾄ値履歴BIN3カウンター数
     * @return the bin3countersuu
     */
    public Integer getBin3countersuu() {
        return bin3countersuu;
    }
    /**
     * ｶｯﾄ値履歴BIN3カウンター数
     * @param bin3countersuu the bin3countersuu to set
     */
    public void setBin3countersuu(Integer bin3countersuu) {
        this.bin3countersuu = bin3countersuu;
    }
    /**
     * ｶｯﾄ値履歴BIN4カット値下限
     * @return the bin4low
     */
    public String getBin4low() {
        return bin4low;
    }
    /**
     * ｶｯﾄ値履歴BIN4カット値下限
     * @param bin4low the bin4low to set
     */
    public void setBin4low(String bin4low) {
        this.bin4low = bin4low;
    }
    /**
     * ｶｯﾄ値履歴BIN4カット値上限
     * @return the bin4high
     */
    public String getBin4high() {
        return bin4high;
    }
    /**
     * ｶｯﾄ値履歴BIN4カット値上限
     * @param bin4high the bin4high to set
     */
    public void setBin4high(String bin4high) {
        this.bin4high = bin4high;
    }
    /**
     * ｶｯﾄ値履歴BIN4カウンター数
     * @return the bin4countersuu
     */
    public Integer getBin4countersuu() {
        return bin4countersuu;
    }
    /**
     * ｶｯﾄ値履歴BIN4カウンター数
     * @param bin4countersuu the bin4countersuu to set
     */
    public void setBin4countersuu(Integer bin4countersuu) {
        this.bin4countersuu = bin4countersuu;
    }
    /**
     * ｶｯﾄ値履歴BIN5カット値下限
     * @return the bin5low
     */
    public String getBin5low() {
        return bin5low;
    }
    /**
     * ｶｯﾄ値履歴BIN5カット値上限
     * @param bin5low the bin5low to set
     */
    public void setBin5low(String bin5low) {
        this.bin5low = bin5low;
    }
    /**
     * ｶｯﾄ値履歴BIN5カット値上限
     * @return the bin5high
     */
    public String getBin5high() {
        return bin5high;
    }
    /**
     * ｶｯﾄ値履歴BIN5カット値下限
     * @param bin5high the bin5high to set
     */
    public void setBin5high(String bin5high) {
        this.bin5high = bin5high;
    }
    /**
     * ｶｯﾄ値履歴BIN5カウンター数
     * @return the bin5countersuu
     */
    public Integer getBin5countersuu() {
        return bin5countersuu;
    }
    /**
     * ｶｯﾄ値履歴BIN5カウンター数
     * @param bin5countersuu the bin5countersuu to set
     */
    public void setBin5countersuu(Integer bin5countersuu) {
        this.bin5countersuu = bin5countersuu;
    }
    /**
     * ｶｯﾄ値履歴BIN6カット値下限
     * @return the bin6low
     */
    public String getBin6low() {
        return bin6low;
    }
    /**
     * ｶｯﾄ値履歴BIN6カット値下限
     * @param bin6low the bin6low to set
     */
    public void setBin6low(String bin6low) {
        this.bin6low = bin6low;
    }
    /**
     * ｶｯﾄ値履歴BIN6カット値上限
     * @return the bin6high
     */
    public String getBin6high() {
        return bin6high;
    }
    /**
     * ｶｯﾄ値履歴BIN6カット値上限
     * @param bin6high the bin6high to set
     */
    public void setBin6high(String bin6high) {
        this.bin6high = bin6high;
    }
    /**
     * ｶｯﾄ値履歴BIN6カウンター数
     * @return the bin6countersuu
     */
    public Integer getBin6countersuu() {
        return bin6countersuu;
    }
    /**
     * ｶｯﾄ値履歴BIN6カウンター数
     * @param bin6countersuu the bin6countersuu to set
     */
    public void setBin6countersuu(Integer bin6countersuu) {
        this.bin6countersuu = bin6countersuu;
    }
    /**
     * ｶｯﾄ値履歴BIN7カット値下限
     * @return the bin7low
     */
    public String getBin7low() {
        return bin7low;
    }
    /**
     * ｶｯﾄ値履歴BIN7カット値下限
     * @param bin7low the bin7low to set
     */
    public void setBin7low(String bin7low) {
        this.bin7low = bin7low;
    }
    /**
     * ｶｯﾄ値履歴BIN7カット値上限
     * @return the bin7high
     */
    public String getBin7high() {
        return bin7high;
    }
    /**
     * ｶｯﾄ値履歴BIN7カット値上限
     * @param bin7high the bin7high to set
     */
    public void setBin7high(String bin7high) {
        this.bin7high = bin7high;
    }
    /**
     * ｶｯﾄ値履歴BIN7カウンター数
     * @return the bin7countersuu
     */
    public Integer getBin7countersuu() {
        return bin7countersuu;
    }
    /**
     * ｶｯﾄ値履歴BIN7カウンター数
     * @param bin7countersuu the bin7countersuu to set
     */
    public void setBin7countersuu(Integer bin7countersuu) {
        this.bin7countersuu = bin7countersuu;
    }
    /**
     * ｶｯﾄ値履歴BIN8カット値下限
     * @return the bin8low
     */
    public String getBin8low() {
        return bin8low;
    }
    /**
     * ｶｯﾄ値履歴BIN8カット値下限
     * @param bin8low the bin8low to set
     */
    public void setBin8low(String bin8low) {
        this.bin8low = bin8low;
    }
    /**
     * ｶｯﾄ値履歴BIN8カット値上限
     * @return the bin8high
     */
    public String getBin8high() {
        return bin8high;
    }
    /**
     * ｶｯﾄ値履歴BIN8カット値上限
     * @param bin8high the bin8high to set
     */
    public void setBin8high(String bin8high) {
        this.bin8high = bin8high;
    }
    /**
     * ｶｯﾄ値履歴BIN8カウンター数
     * @return the bin8countersuu
     */
    public Integer getBin8countersuu() {
        return bin8countersuu;
    }
    /**
     * ｶｯﾄ値履歴BIN8カウンター数
     * @param bin8countersuu the bin8countersuu to set
     */
    public void setBin8countersuu(Integer bin8countersuu) {
        this.bin8countersuu = bin8countersuu;
    }
    /**
     * 削除ﾌﾗｸﾞ
     * @return the deleteflag
     */
    public Integer getDeleteflag() {
        return deleteflag;
    }
    /**
     * 削除ﾌﾗｸﾞ
     * @param deleteflag the deleteflag to set
     */
    public void setDeleteflag(Integer deleteflag) {
        this.deleteflag = deleteflag;
    }
    /**
     * 登録日時
     * @return the torokudate
     */
    public Timestamp getTorokudate() {
        return torokudate;
    }
    /**
     * 登録日時
     * @param torokudate the torokudate to set
     */
    public void setTorokudate(Timestamp torokudate) {
        this.torokudate = torokudate;
    }
    /**
     * 更新日時
     * @return the koshindate
     */
    public Timestamp getKoshindate() {
        return koshindate;
    }
    /**
     * 更新日時
     * @param koshindate the koshindate to set
     */
    public void setKoshindate(Timestamp koshindate) {
        this.koshindate = koshindate;
    }
}
