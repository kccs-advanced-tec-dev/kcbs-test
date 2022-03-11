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
 * 変更日       2021/11/22<br>
 * 計画書No     MB2101-DK002<br>
 * 変更者       KCSS K.Jo<br>
 * 変更理由     新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽ履歴検索画面のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/11/22
 */
public class GXHDO202B015Model implements Serializable {
    /** WIPﾛｯﾄNo */
    private String lotno = "";

    /** ﾊﾞｲﾝﾀﾞｰ溶液品名 */
    private String binderyouekihinmei = "";

    /** ﾊﾞｲﾝﾀﾞｰ溶液LotNo */
    private String binderyouekilotno = "";

    /** ﾛｯﾄ区分 */
    private String lotkubun = "";

    /** ﾌｨﾙﾀｰ交換日時 */
    private Timestamp filterkoukannichiji = null;

    /** ﾌｨﾙﾀｰ品名 */
    private String filterhinmei = "";

    /** LotNo */
    private String fplotno = "";

    /** 取り付け本数 */
    private Integer toritukehonsuu = null;

    /** F/P準備_担当者 */
    private String fpjyunbi_tantousya = "";

    /** F/P開始日時 */
    private Timestamp fpkaisinichiji = null;

    /** F/P開始_担当者 */
    private String fpkaisi_tantousya = "";

    /** F/P終了日時 */
    private Timestamp fpsyuuryounichiji = null;

    /** F/P終了_担当者 */
    private String fpsyuuryou_tantousya = "";

    /** F/P時間 */
    private Integer fpjikan = null;

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
     * ﾊﾞｲﾝﾀﾞｰ溶液品名
     * @return the binderyouekihinmei
     */
    public String getBinderyouekihinmei() {
        return binderyouekihinmei;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液品名
     * @param binderyouekihinmei the binderyouekihinmei to set
     */
    public void setBinderyouekihinmei(String binderyouekihinmei) {
        this.binderyouekihinmei = binderyouekihinmei;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液LotNo
     * @return the binderyouekilotno
     */
    public String getBinderyouekilotno() {
        return binderyouekilotno;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液LotNo
     * @param binderyouekilotno the binderyouekilotno to set
     */
    public void setBinderyouekilotno(String binderyouekilotno) {
        this.binderyouekilotno = binderyouekilotno;
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
     * ﾌｨﾙﾀｰ交換日時
     * @return the filterkoukannichiji
     */
    public Timestamp getFilterkoukannichiji() {
        return filterkoukannichiji;
    }

    /**
     * ﾌｨﾙﾀｰ交換日時
     * @param filterkoukannichiji the filterkoukannichiji to set
     */
    public void setFilterkoukannichiji(Timestamp filterkoukannichiji) {
        this.filterkoukannichiji = filterkoukannichiji;
    }

    /**
     * ﾌｨﾙﾀｰ品名
     * @return the filterhinmei
     */
    public String getFilterhinmei() {
        return filterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ品名
     * @param filterhinmei the filterhinmei to set
     */
    public void setFilterhinmei(String filterhinmei) {
        this.filterhinmei = filterhinmei;
    }

    /**
     * LotNo
     * @return the fplotno
     */
    public String getFplotno() {
        return fplotno;
    }

    /**
     * LotNo
     * @param fplotno the fplotno to set
     */
    public void setFplotno(String fplotno) {
        this.fplotno = fplotno;
    }

    /**
     * 取り付け本数
     * @return the toritukehonsuu
     */
    public Integer getToritukehonsuu() {
        return toritukehonsuu;
    }

    /**
     * 取り付け本数
     * @param toritukehonsuu the toritukehonsuu to set
     */
    public void setToritukehonsuu(Integer toritukehonsuu) {
        this.toritukehonsuu = toritukehonsuu;
    }

    /**
     * F/P準備_担当者
     * @return the fpjyunbi_tantousya
     */
    public String getFpjyunbi_tantousya() {
        return fpjyunbi_tantousya;
    }

    /**
     * F/P準備_担当者
     * @param fpjyunbi_tantousya the fpjyunbi_tantousya to set
     */
    public void setFpjyunbi_tantousya(String fpjyunbi_tantousya) {
        this.fpjyunbi_tantousya = fpjyunbi_tantousya;
    }

    /**
     * F/P開始日時
     * @return the fpkaisinichiji
     */
    public Timestamp getFpkaisinichiji() {
        return fpkaisinichiji;
    }

    /**
     * F/P開始日時
     * @param fpkaisinichiji the fpkaisinichiji to set
     */
    public void setFpkaisinichiji(Timestamp fpkaisinichiji) {
        this.fpkaisinichiji = fpkaisinichiji;
    }

    /**
     * F/P開始_担当者
     * @return the fpkaisi_tantousya
     */
    public String getFpkaisi_tantousya() {
        return fpkaisi_tantousya;
    }

    /**
     * F/P開始_担当者
     * @param fpkaisi_tantousya the fpkaisi_tantousya to set
     */
    public void setFpkaisi_tantousya(String fpkaisi_tantousya) {
        this.fpkaisi_tantousya = fpkaisi_tantousya;
    }

    /**
     * F/P終了日時
     * @return the fpsyuuryounichiji
     */
    public Timestamp getFpsyuuryounichiji() {
        return fpsyuuryounichiji;
    }

    /**
     * F/P終了日時
     * @param fpsyuuryounichiji the fpsyuuryounichiji to set
     */
    public void setFpsyuuryounichiji(Timestamp fpsyuuryounichiji) {
        this.fpsyuuryounichiji = fpsyuuryounichiji;
    }

    /**
     * F/P終了_担当者
     * @return the fpsyuuryou_tantousya
     */
    public String getFpsyuuryou_tantousya() {
        return fpsyuuryou_tantousya;
    }

    /**
     * F/P終了_担当者
     * @param fpsyuuryou_tantousya the fpsyuuryou_tantousya to set
     */
    public void setFpsyuuryou_tantousya(String fpsyuuryou_tantousya) {
        this.fpsyuuryou_tantousya = fpsyuuryou_tantousya;
    }

    /**
     * F/P時間
     * @return the fpjikan
     */
    public Integer getFpjikan() {
        return fpjikan;
    }

    /**
     * F/P時間
     * @param fpjikan the fpjikan to set
     */
    public void setFpjikan(Integer fpjikan) {
        this.fpjikan = fpjikan;
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