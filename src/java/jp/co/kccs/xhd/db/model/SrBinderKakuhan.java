/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/10/26<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SR_BINDER_KAKUHAN(ﾊﾞｲﾝﾀﾞｰ溶液作製・撹拌)のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/10/26
 */
public class SrBinderKakuhan {
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
     * ﾊﾞｲﾝﾀﾞｰ溶液品名
     */
    private String binderyouekihinmei;

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液LotNo
     */
    private String binderyouekilotno;

    /**
     * ﾛｯﾄ区分
     */
    private String lotkubun;

    /**
     * ﾃﾞｨｽﾊﾟ号機
     */
    private String dispagouki;

    /**
     * 風袋重量
     */
    private Integer fuutaijyuuryou;

    /**
     * 粉末投入_開始日時
     */
    private Timestamp funmatutounyuu_kaisinichiji;

    /**
     * 粉末投入_終了日時
     */
    private Timestamp funmatutounyuu_syuuryounichiji;

    /**
     * 羽根の種類
     */
    private String hanenosyurui;

    /**
     * 撹拌_開始日時
     */
    private Timestamp kakuhan_kaisinichiji;

    /**
     * 撹拌_終了日時
     */
    private Timestamp kakuhan_syuuryounichiji;

    /**
     * 撹拌時間
     */
    private String kakuhanjikan;

    /**
     * 設定回転数
     */
    private String setteikaitensuu;

    /**
     * 回転数
     */
    private Integer kaitensuu;

    /**
     * ｴｰｼﾞﾝｸﾞ時間規定
     */
    private String agingjikankitei;

    /**
     * ｴｰｼﾞﾝｸﾞ_開始日時
     */
    private Timestamp aging_kaisinichiji;

    /**
     * ｴｰｼﾞﾝｸﾞ_終了日時
     */
    private Timestamp aging_syuuryounichiji;

    /**
     * 担当者
     */
    private String tantousya;

    /**
     * 確認者
     */
    private String kakuninsya;

    /**
     * 備考1
     */
    private String bikou1;

    /**
     * 備考2
     */
    private String bikou2;

    /**
     * 登録日時
     */
    private Timestamp torokunichiji;

    /**
     * 更新日時
     */
    private Timestamp kosinnichiji;

    /**
     * revision
     */
    private Integer revision;

    /**
     * 削除ﾌﾗｸﾞ
     */
    private Integer deleteflag;

    /**
     * 工場ｺｰﾄﾞ
     * @return the kojyo
     */
    public String getKojyo() {
        return kojyo;
    }

    /**
     * 工場ｺｰﾄﾞ
     * @param kojyo the kojyo to set
     */
    public void setKojyo(String kojyo) {
        this.kojyo = kojyo;
    }

    /**
     * ﾛｯﾄNo
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * ﾛｯﾄNo
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
     * ﾃﾞｨｽﾊﾟ号機
     * @return the dispagouki
     */
    public String getDispagouki() {
        return dispagouki;
    }

    /**
     * ﾃﾞｨｽﾊﾟ号機
     * @param dispagouki the dispagouki to set
     */
    public void setDispagouki(String dispagouki) {
        this.dispagouki = dispagouki;
    }

    /**
     * 風袋重量
     * @return the fuutaijyuuryou
     */
    public Integer getFuutaijyuuryou() {
        return fuutaijyuuryou;
    }

    /**
     * 風袋重量
     * @param fuutaijyuuryou the fuutaijyuuryou to set
     */
    public void setFuutaijyuuryou(Integer fuutaijyuuryou) {
        this.fuutaijyuuryou = fuutaijyuuryou;
    }

    /**
     * 粉末投入_開始日時
     * @return the funmatutounyuu_kaisinichiji
     */
    public Timestamp getFunmatutounyuu_kaisinichiji() {
        return funmatutounyuu_kaisinichiji;
    }

    /**
     * 粉末投入_開始日時
     * @param funmatutounyuu_kaisinichiji the funmatutounyuu_kaisinichiji to set
     */
    public void setFunmatutounyuu_kaisinichiji(Timestamp funmatutounyuu_kaisinichiji) {
        this.funmatutounyuu_kaisinichiji = funmatutounyuu_kaisinichiji;
    }

    /**
     * 粉末投入_終了日時
     * @return the funmatutounyuu_syuuryounichiji
     */
    public Timestamp getFunmatutounyuu_syuuryounichiji() {
        return funmatutounyuu_syuuryounichiji;
    }

    /**
     * 粉末投入_終了日時
     * @param funmatutounyuu_syuuryounichiji the funmatutounyuu_syuuryounichiji to set
     */
    public void setFunmatutounyuu_syuuryounichiji(Timestamp funmatutounyuu_syuuryounichiji) {
        this.funmatutounyuu_syuuryounichiji = funmatutounyuu_syuuryounichiji;
    }

    /**
     * 羽根の種類
     * @return the hanenosyurui
     */
    public String getHanenosyurui() {
        return hanenosyurui;
    }

    /**
     * 羽根の種類
     * @param hanenosyurui the hanenosyurui to set
     */
    public void setHanenosyurui(String hanenosyurui) {
        this.hanenosyurui = hanenosyurui;
    }

    /**
     * 撹拌_開始日時
     * @return the kakuhan_kaisinichiji
     */
    public Timestamp getKakuhan_kaisinichiji() {
        return kakuhan_kaisinichiji;
    }

    /**
     * 撹拌_開始日時
     * @param kakuhan_kaisinichiji the kakuhan_kaisinichiji to set
     */
    public void setKakuhan_kaisinichiji(Timestamp kakuhan_kaisinichiji) {
        this.kakuhan_kaisinichiji = kakuhan_kaisinichiji;
    }

    /**
     * 撹拌_終了日時
     * @return the kakuhan_syuuryounichiji
     */
    public Timestamp getKakuhan_syuuryounichiji() {
        return kakuhan_syuuryounichiji;
    }

    /**
     * 撹拌_終了日時
     * @param kakuhan_syuuryounichiji the kakuhan_syuuryounichiji to set
     */
    public void setKakuhan_syuuryounichiji(Timestamp kakuhan_syuuryounichiji) {
        this.kakuhan_syuuryounichiji = kakuhan_syuuryounichiji;
    }

    /**
     * 撹拌時間
     * @return the kakuhanjikan
     */
    public String getKakuhanjikan() {
        return kakuhanjikan;
    }

    /**
     * 撹拌時間
     * @param kakuhanjikan the kakuhanjikan to set
     */
    public void setKakuhanjikan(String kakuhanjikan) {
        this.kakuhanjikan = kakuhanjikan;
    }

    /**
     * 設定回転数
     * @return the setteikaitensuu
     */
    public String getSetteikaitensuu() {
        return setteikaitensuu;
    }

    /**
     * 設定回転数
     * @param setteikaitensuu the setteikaitensuu to set
     */
    public void setSetteikaitensuu(String setteikaitensuu) {
        this.setteikaitensuu = setteikaitensuu;
    }

    /**
     * 回転数
     * @return the kaitensuu
     */
    public Integer getKaitensuu() {
        return kaitensuu;
    }

    /**
     * 回転数
     * @param kaitensuu the kaitensuu to set
     */
    public void setKaitensuu(Integer kaitensuu) {
        this.kaitensuu = kaitensuu;
    }

    /**
     * ｴｰｼﾞﾝｸﾞ時間規定
     * @return the agingjikankitei
     */
    public String getAgingjikankitei() {
        return agingjikankitei;
    }

    /**
     * ｴｰｼﾞﾝｸﾞ時間規定
     * @param agingjikankitei the agingjikankitei to set
     */
    public void setAgingjikankitei(String agingjikankitei) {
        this.agingjikankitei = agingjikankitei;
    }

    /**
     * ｴｰｼﾞﾝｸﾞ_開始日時
     * @return the aging_kaisinichiji
     */
    public Timestamp getAging_kaisinichiji() {
        return aging_kaisinichiji;
    }

    /**
     * ｴｰｼﾞﾝｸﾞ_開始日時
     * @param aging_kaisinichiji the aging_kaisinichiji to set
     */
    public void setAging_kaisinichiji(Timestamp aging_kaisinichiji) {
        this.aging_kaisinichiji = aging_kaisinichiji;
    }

    /**
     * ｴｰｼﾞﾝｸﾞ_終了日時
     * @return the aging_syuuryounichiji
     */
    public Timestamp getAging_syuuryounichiji() {
        return aging_syuuryounichiji;
    }

    /**
     * ｴｰｼﾞﾝｸﾞ_終了日時
     * @param aging_syuuryounichiji the aging_syuuryounichiji to set
     */
    public void setAging_syuuryounichiji(Timestamp aging_syuuryounichiji) {
        this.aging_syuuryounichiji = aging_syuuryounichiji;
    }

    /**
     * 担当者
     * @return the tantousya
     */
    public String getTantousya() {
        return tantousya;
    }

    /**
     * 担当者
     * @param tantousya the tantousya to set
     */
    public void setTantousya(String tantousya) {
        this.tantousya = tantousya;
    }

    /**
     * 確認者
     * @return the kakuninsya
     */
    public String getKakuninsya() {
        return kakuninsya;
    }

    /**
     * 確認者
     * @param kakuninsya the kakuninsya to set
     */
    public void setKakuninsya(String kakuninsya) {
        this.kakuninsya = kakuninsya;
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
     * 登録日時
     * @return the torokunichiji
     */
    public Timestamp getTorokunichiji() {
        return torokunichiji;
    }

    /**
     * 登録日時
     * @param torokunichiji the torokunichiji to set
     */
    public void setTorokunichiji(Timestamp torokunichiji) {
        this.torokunichiji = torokunichiji;
    }

    /**
     * 更新日時
     * @return the kosinnichiji
     */
    public Timestamp getKosinnichiji() {
        return kosinnichiji;
    }

    /**
     * 更新日時
     * @param kosinnichiji the kosinnichiji to set
     */
    public void setKosinnichiji(Timestamp kosinnichiji) {
        this.kosinnichiji = kosinnichiji;
    }

    /**
     * revision
     * @return the revision
     */
    public Integer getRevision() {
        return revision;
    }

    /**
     * revision
     * @param revision the revision to set
     */
    public void setRevision(Integer revision) {
        this.revision = revision;
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

}