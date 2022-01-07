/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo102;

import java.io.Serializable;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import jp.co.kccs.xhd.db.model.FXHDD01;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/10/28<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B017(ﾊﾞｲﾝﾀﾞｰ溶液作製・撹拌)
 *
 * @author KCSS K.Jo
 * @since  2021/10/28
 */
@ViewScoped
@Named
public class GXHDO102B017A implements Serializable {
    /**
     * WIPﾛｯﾄNo
     */
    private FXHDD01 wiplotno;

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液品名
     */
    private FXHDD01 binderyouekihinmei;

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液LotNo
     */
    private FXHDD01 binderyouekilotno;

    /**
     * ﾛｯﾄ区分
     */
    private FXHDD01 lotkubun;

    /**
     * ﾃﾞｨｽﾊﾟ号機
     */
    private FXHDD01 dispagouki;

    /**
     * 風袋重量
     */
    private FXHDD01 fuutaijyuuryou;

    /**
     * 粉末投入_開始日
     */
    private FXHDD01 funmatutounyuu_kaisi_day;

    /**
     * 粉末投入_開始時間
     */
    private FXHDD01 funmatutounyuu_kaisi_time;

    /**
     * 粉末投入_終了日
     */
    private FXHDD01 funmatutounyuu_syuuryou_day;

    /**
     * 粉末投入_終了時間
     */
    private FXHDD01 funmatutounyuu_syuuryou_time;

    /**
     * 羽根の種類
     */
    private FXHDD01 hanenosyurui;

    /**
     * 撹拌_開始日
     */
    private FXHDD01 kakuhan_kaisi_day;

    /**
     * 撹拌_開始時間
     */
    private FXHDD01 kakuhan_kaisi_time;

    /**
     * 撹拌_終了日
     */
    private FXHDD01 kakuhan_syuuryou_day;

    /**
     * 撹拌_終了時間
     */
    private FXHDD01 kakuhan_syuuryou_time;

    /**
     * 撹拌時間
     */
    private FXHDD01 kakuhanjikan;

    /**
     * 設定回転数
     */
    private FXHDD01 setteikaitensuu;

    /**
     * 回転数
     */
    private FXHDD01 kaitensuu;

    /**
     * ｴｰｼﾞﾝｸﾞ時間規定
     */
    private FXHDD01 agingjikankitei;

    /**
     * ｴｰｼﾞﾝｸﾞ_開始日
     */
    private FXHDD01 aging_kaisi_day;

    /**
     * ｴｰｼﾞﾝｸﾞ_開始時間
     */
    private FXHDD01 aging_kaisi_time;

    /**
     * ｴｰｼﾞﾝｸﾞ_終了日
     */
    private FXHDD01 aging_syuuryou_day;

    /**
     * ｴｰｼﾞﾝｸﾞ_終了時間
     */
    private FXHDD01 aging_syuuryou_time;

    /**
     * 担当者
     */
    private FXHDD01 tantousya;

    /**
     * 確認者
     */
    private FXHDD01 kakuninsya;

    /**
     * 備考1
     */
    private FXHDD01 bikou1;

    /**
     * 備考2
     */
    private FXHDD01 bikou2;

    /**
     * コンストラクタ
     */
    public GXHDO102B017A() {
    }

    /**
     * WIPﾛｯﾄNo
     * @return the wiplotno
     */
    public FXHDD01 getWiplotno() {
        return wiplotno;
    }

    /**
     * WIPﾛｯﾄNo
     * @param wiplotno the wiplotno to set
     */
    public void setWiplotno(FXHDD01 wiplotno) {
        this.wiplotno = wiplotno;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液品名
     * @return the binderyouekihinmei
     */
    public FXHDD01 getBinderyouekihinmei() {
        return binderyouekihinmei;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液品名
     * @param binderyouekihinmei the binderyouekihinmei to set
     */
    public void setBinderyouekihinmei(FXHDD01 binderyouekihinmei) {
        this.binderyouekihinmei = binderyouekihinmei;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液LotNo
     * @return the binderyouekilotno
     */
    public FXHDD01 getBinderyouekilotno() {
        return binderyouekilotno;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液LotNo
     * @param binderyouekilotno the binderyouekilotno to set
     */
    public void setBinderyouekilotno(FXHDD01 binderyouekilotno) {
        this.binderyouekilotno = binderyouekilotno;
    }

    /**
     * ﾛｯﾄ区分
     * @return the lotkubun
     */
    public FXHDD01 getLotkubun() {
        return lotkubun;
    }

    /**
     * ﾛｯﾄ区分
     * @param lotkubun the lotkubun to set
     */
    public void setLotkubun(FXHDD01 lotkubun) {
        this.lotkubun = lotkubun;
    }

    /**
     * ﾃﾞｨｽﾊﾟ号機
     * @return the dispagouki
     */
    public FXHDD01 getDispagouki() {
        return dispagouki;
    }

    /**
     * ﾃﾞｨｽﾊﾟ号機
     * @param dispagouki the dispagouki to set
     */
    public void setDispagouki(FXHDD01 dispagouki) {
        this.dispagouki = dispagouki;
    }

    /**
     * 風袋重量
     * @return the fuutaijyuuryou
     */
    public FXHDD01 getFuutaijyuuryou() {
        return fuutaijyuuryou;
    }

    /**
     * 風袋重量
     * @param fuutaijyuuryou the fuutaijyuuryou to set
     */
    public void setFuutaijyuuryou(FXHDD01 fuutaijyuuryou) {
        this.fuutaijyuuryou = fuutaijyuuryou;
    }

    /**
     * 粉末投入_開始日
     * @return the funmatutounyuu_kaisi_day
     */
    public FXHDD01 getFunmatutounyuu_kaisi_day() {
        return funmatutounyuu_kaisi_day;
    }

    /**
     * 粉末投入_開始日
     * @param funmatutounyuu_kaisi_day the funmatutounyuu_kaisi_day to set
     */
    public void setFunmatutounyuu_kaisi_day(FXHDD01 funmatutounyuu_kaisi_day) {
        this.funmatutounyuu_kaisi_day = funmatutounyuu_kaisi_day;
    }

    /**
     * 粉末投入_開始時間
     * @return the funmatutounyuu_kaisi_time
     */
    public FXHDD01 getFunmatutounyuu_kaisi_time() {
        return funmatutounyuu_kaisi_time;
    }

    /**
     * 粉末投入_開始時間
     * @param funmatutounyuu_kaisi_time the funmatutounyuu_kaisi_time to set
     */
    public void setFunmatutounyuu_kaisi_time(FXHDD01 funmatutounyuu_kaisi_time) {
        this.funmatutounyuu_kaisi_time = funmatutounyuu_kaisi_time;
    }

    /**
     * 粉末投入_終了日
     * @return the funmatutounyuu_syuuryou_day
     */
    public FXHDD01 getFunmatutounyuu_syuuryou_day() {
        return funmatutounyuu_syuuryou_day;
    }

    /**
     * 粉末投入_終了日
     * @param funmatutounyuu_syuuryou_day the funmatutounyuu_syuuryou_day to set
     */
    public void setFunmatutounyuu_syuuryou_day(FXHDD01 funmatutounyuu_syuuryou_day) {
        this.funmatutounyuu_syuuryou_day = funmatutounyuu_syuuryou_day;
    }

    /**
     * 粉末投入_終了時間
     * @return the funmatutounyuu_syuuryou_time
     */
    public FXHDD01 getFunmatutounyuu_syuuryou_time() {
        return funmatutounyuu_syuuryou_time;
    }

    /**
     * 粉末投入_終了時間
     * @param funmatutounyuu_syuuryou_time the funmatutounyuu_syuuryou_time to set
     */
    public void setFunmatutounyuu_syuuryou_time(FXHDD01 funmatutounyuu_syuuryou_time) {
        this.funmatutounyuu_syuuryou_time = funmatutounyuu_syuuryou_time;
    }

    /**
     * 羽根の種類
     * @return the hanenosyurui
     */
    public FXHDD01 getHanenosyurui() {
        return hanenosyurui;
    }

    /**
     * 羽根の種類
     * @param hanenosyurui the hanenosyurui to set
     */
    public void setHanenosyurui(FXHDD01 hanenosyurui) {
        this.hanenosyurui = hanenosyurui;
    }

    /**
     * 撹拌_開始日
     * @return the kakuhan_kaisi_day
     */
    public FXHDD01 getKakuhan_kaisi_day() {
        return kakuhan_kaisi_day;
    }

    /**
     * 撹拌_開始日
     * @param kakuhan_kaisi_day the kakuhan_kaisi_day to set
     */
    public void setKakuhan_kaisi_day(FXHDD01 kakuhan_kaisi_day) {
        this.kakuhan_kaisi_day = kakuhan_kaisi_day;
    }

    /**
     * 撹拌_開始時間
     * @return the kakuhan_kaisi_time
     */
    public FXHDD01 getKakuhan_kaisi_time() {
        return kakuhan_kaisi_time;
    }

    /**
     * 撹拌_開始時間
     * @param kakuhan_kaisi_time the kakuhan_kaisi_time to set
     */
    public void setKakuhan_kaisi_time(FXHDD01 kakuhan_kaisi_time) {
        this.kakuhan_kaisi_time = kakuhan_kaisi_time;
    }

    /**
     * 撹拌_終了日
     * @return the kakuhan_syuuryou_day
     */
    public FXHDD01 getKakuhan_syuuryou_day() {
        return kakuhan_syuuryou_day;
    }

    /**
     * 撹拌_終了日
     * @param kakuhan_syuuryou_day the kakuhan_syuuryou_day to set
     */
    public void setKakuhan_syuuryou_day(FXHDD01 kakuhan_syuuryou_day) {
        this.kakuhan_syuuryou_day = kakuhan_syuuryou_day;
    }

    /**
     * 撹拌_終了時間
     * @return the kakuhan_syuuryou_time
     */
    public FXHDD01 getKakuhan_syuuryou_time() {
        return kakuhan_syuuryou_time;
    }

    /**
     * 撹拌_終了時間
     * @param kakuhan_syuuryou_time the kakuhan_syuuryou_time to set
     */
    public void setKakuhan_syuuryou_time(FXHDD01 kakuhan_syuuryou_time) {
        this.kakuhan_syuuryou_time = kakuhan_syuuryou_time;
    }

    /**
     * 撹拌時間
     * @return the kakuhanjikan
     */
    public FXHDD01 getKakuhanjikan() {
        return kakuhanjikan;
    }

    /**
     * 撹拌時間
     * @param kakuhanjikan the kakuhanjikan to set
     */
    public void setKakuhanjikan(FXHDD01 kakuhanjikan) {
        this.kakuhanjikan = kakuhanjikan;
    }

    /**
     * 設定回転数
     * @return the setteikaitensuu
     */
    public FXHDD01 getSetteikaitensuu() {
        return setteikaitensuu;
    }

    /**
     * 設定回転数
     * @param setteikaitensuu the setteikaitensuu to set
     */
    public void setSetteikaitensuu(FXHDD01 setteikaitensuu) {
        this.setteikaitensuu = setteikaitensuu;
    }

    /**
     * 回転数
     * @return the kaitensuu
     */
    public FXHDD01 getKaitensuu() {
        return kaitensuu;
    }

    /**
     * 回転数
     * @param kaitensuu the kaitensuu to set
     */
    public void setKaitensuu(FXHDD01 kaitensuu) {
        this.kaitensuu = kaitensuu;
    }

    /**
     * ｴｰｼﾞﾝｸﾞ時間規定
     * @return the agingjikankitei
     */
    public FXHDD01 getAgingjikankitei() {
        return agingjikankitei;
    }

    /**
     * ｴｰｼﾞﾝｸﾞ時間規定
     * @param agingjikankitei the agingjikankitei to set
     */
    public void setAgingjikankitei(FXHDD01 agingjikankitei) {
        this.agingjikankitei = agingjikankitei;
    }

    /**
     * ｴｰｼﾞﾝｸﾞ_開始日
     * @return the aging_kaisi_day
     */
    public FXHDD01 getAging_kaisi_day() {
        return aging_kaisi_day;
    }

    /**
     * ｴｰｼﾞﾝｸﾞ_開始日
     * @param aging_kaisi_day the aging_kaisi_day to set
     */
    public void setAging_kaisi_day(FXHDD01 aging_kaisi_day) {
        this.aging_kaisi_day = aging_kaisi_day;
    }

    /**
     * ｴｰｼﾞﾝｸﾞ_開始時間
     * @return the aging_kaisi_time
     */
    public FXHDD01 getAging_kaisi_time() {
        return aging_kaisi_time;
    }

    /**
     * ｴｰｼﾞﾝｸﾞ_開始時間
     * @param aging_kaisi_time the aging_kaisi_time to set
     */
    public void setAging_kaisi_time(FXHDD01 aging_kaisi_time) {
        this.aging_kaisi_time = aging_kaisi_time;
    }

    /**
     * ｴｰｼﾞﾝｸﾞ_終了日
     * @return the aging_syuuryou_day
     */
    public FXHDD01 getAging_syuuryou_day() {
        return aging_syuuryou_day;
    }

    /**
     * ｴｰｼﾞﾝｸﾞ_終了日
     * @param aging_syuuryou_day the aging_syuuryou_day to set
     */
    public void setAging_syuuryou_day(FXHDD01 aging_syuuryou_day) {
        this.aging_syuuryou_day = aging_syuuryou_day;
    }

    /**
     * ｴｰｼﾞﾝｸﾞ_終了時間
     * @return the aging_syuuryou_time
     */
    public FXHDD01 getAging_syuuryou_time() {
        return aging_syuuryou_time;
    }

    /**
     * ｴｰｼﾞﾝｸﾞ_終了時間
     * @param aging_syuuryou_time the aging_syuuryou_time to set
     */
    public void setAging_syuuryou_time(FXHDD01 aging_syuuryou_time) {
        this.aging_syuuryou_time = aging_syuuryou_time;
    }

    /**
     * 担当者
     * @return the tantousya
     */
    public FXHDD01 getTantousya() {
        return tantousya;
    }

    /**
     * 担当者
     * @param tantousya the tantousya to set
     */
    public void setTantousya(FXHDD01 tantousya) {
        this.tantousya = tantousya;
    }

    /**
     * 確認者
     * @return the kakuninsya
     */
    public FXHDD01 getKakuninsya() {
        return kakuninsya;
    }

    /**
     * 確認者
     * @param kakuninsya the kakuninsya to set
     */
    public void setKakuninsya(FXHDD01 kakuninsya) {
        this.kakuninsya = kakuninsya;
    }

    /**
     * 備考1
     * @return the bikou1
     */
    public FXHDD01 getBikou1() {
        return bikou1;
    }

    /**
     * 備考1
     * @param bikou1 the bikou1 to set
     */
    public void setBikou1(FXHDD01 bikou1) {
        this.bikou1 = bikou1;
    }

    /**
     * 備考2
     * @return the bikou2
     */
    public FXHDD01 getBikou2() {
        return bikou2;
    }

    /**
     * 備考2
     * @param bikou2 the bikou2 to set
     */
    public void setBikou2(FXHDD01 bikou2) {
        this.bikou2 = bikou2;
    }

}