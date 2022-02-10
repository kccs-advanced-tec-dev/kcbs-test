/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import jp.co.kccs.xhd.db.model.FXHDD01;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2021/12/21<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS wxf<br>
 * 変更理由	新規作成<br>
 * ===============================================================================<br>
 */
/**
 * ｽﾘｯﾌﾟ作製・高圧分散の「ﾊﾟｽ」タブ用のモデルクラスです。
 *
 * @author KCSS wxf
 * @since 2021/12/21
 */
public class GXHDO102B034Model {

    /**
     * ﾊﾟｽ回数
     */
    private FXHDD01 pass;

    /**
     * 送液側ﾀﾝｸNo.
     */
    private FXHDD01 souekigawatankno;

    /**
     * 排出側ﾀﾝｸNo.
     */
    private FXHDD01 haisyutsugawatankno;

    /**
     * 設定圧力
     */
    private FXHDD01 setteiatsuryoku;

    /**
     * 開始直後排気量
     */
    private FXHDD01 kaishityokugohaikiryou;

    /**
     * 高圧分散開始日
     */
    private FXHDD01 kouatsubunsankaishi_day;

    /**
     * 高圧分散開始時間
     */
    private FXHDD01 kouatsubunsankaishi_time;

    /**
     * 廃棄確認
     */
    private FXHDD01 haikikakunin;

    /**
     * 実圧力(最大値)
     */
    private FXHDD01 jitsuatsuryoku;

    /**
     * ｽﾘｯﾌﾟ流量
     */
    private FXHDD01 slipryuuryou;

    /**
     * ｽﾘｯﾌﾟ温度(IN)
     */
    private FXHDD01 slipondoin;

    /**
     * ｽﾘｯﾌﾟ温度(OUT)
     */
    private FXHDD01 slipondoout;

    /**
     * 高圧分散開始担当者
     */
    private FXHDD01 kouatsubunsankaishitantousya;

    /**
     * 高圧分散終了日
     */
    private FXHDD01 kouatsubunsansyuuryou_day;

    /**
     * 高圧分散終了時間
     */
    private FXHDD01 kouatsubunsansyuuryou_time;

    /**
     * 高圧分散停止担当者
     */
    private FXHDD01 kouatsubunsanteishitantousya;

    /**
     * 備考1
     */
    private FXHDD01 bikou1;

    /**
     * 備考2
     */
    private FXHDD01 bikou2;

    /**
     * 追加Flag
     */
    private String addFlg = "";

    /**
     * ﾊﾟｽ回数
     * @return the pass
     */
    public FXHDD01 getPass() {
        return pass;
    }

    /**
     * ﾊﾟｽ回数
     * @param pass the pass to set
     */
    public void setPass(FXHDD01 pass) {
        this.pass = pass;
    }

    /**
     * 送液側ﾀﾝｸNo.
     * @return the souekigawatankno
     */
    public FXHDD01 getSouekigawatankno() {
        return souekigawatankno;
    }

    /**
     * 送液側ﾀﾝｸNo.
     * @param souekigawatankno the souekigawatankno to set
     */
    public void setSouekigawatankno(FXHDD01 souekigawatankno) {
        this.souekigawatankno = souekigawatankno;
    }

    /**
     * 排出側ﾀﾝｸNo.
     * @return the haisyutsugawatankno
     */
    public FXHDD01 getHaisyutsugawatankno() {
        return haisyutsugawatankno;
    }

    /**
     * 排出側ﾀﾝｸNo.
     * @param haisyutsugawatankno the haisyutsugawatankno to set
     */
    public void setHaisyutsugawatankno(FXHDD01 haisyutsugawatankno) {
        this.haisyutsugawatankno = haisyutsugawatankno;
    }

    /**
     * 設定圧力
     * @return the setteiatsuryoku
     */
    public FXHDD01 getSetteiatsuryoku() {
        return setteiatsuryoku;
    }

    /**
     * 設定圧力
     * @param setteiatsuryoku the setteiatsuryoku to set
     */
    public void setSetteiatsuryoku(FXHDD01 setteiatsuryoku) {
        this.setteiatsuryoku = setteiatsuryoku;
    }

    /**
     * 開始直後排気量
     * @return the kaishityokugohaikiryou
     */
    public FXHDD01 getKaishityokugohaikiryou() {
        return kaishityokugohaikiryou;
    }

    /**
     * 開始直後排気量
     * @param kaishityokugohaikiryou the kaishityokugohaikiryou to set
     */
    public void setKaishityokugohaikiryou(FXHDD01 kaishityokugohaikiryou) {
        this.kaishityokugohaikiryou = kaishityokugohaikiryou;
    }

    /**
     * 高圧分散開始日
     * @return the kouatsubunsankaishi_day
     */
    public FXHDD01 getKouatsubunsankaishi_day() {
        return kouatsubunsankaishi_day;
    }

    /**
     * 高圧分散開始日
     * @param kouatsubunsankaishi_day the kouatsubunsankaishi_day to set
     */
    public void setKouatsubunsankaishi_day(FXHDD01 kouatsubunsankaishi_day) {
        this.kouatsubunsankaishi_day = kouatsubunsankaishi_day;
    }

    /**
     * 高圧分散開始時間
     * @return the kouatsubunsankaishi_time
     */
    public FXHDD01 getKouatsubunsankaishi_time() {
        return kouatsubunsankaishi_time;
    }

    /**
     * 高圧分散開始時間
     * @param kouatsubunsankaishi_time the kouatsubunsankaishi_time to set
     */
    public void setKouatsubunsankaishi_time(FXHDD01 kouatsubunsankaishi_time) {
        this.kouatsubunsankaishi_time = kouatsubunsankaishi_time;
    }

    /**
     * 廃棄確認
     * @return the haikikakunin
     */
    public FXHDD01 getHaikikakunin() {
        return haikikakunin;
    }

    /**
     * 廃棄確認
     * @param haikikakunin the haikikakunin to set
     */
    public void setHaikikakunin(FXHDD01 haikikakunin) {
        this.haikikakunin = haikikakunin;
    }

    /**
     * 実圧力(最大値)
     * @return the jitsuatsuryoku
     */
    public FXHDD01 getJitsuatsuryoku() {
        return jitsuatsuryoku;
    }

    /**
     * 実圧力(最大値)
     * @param jitsuatsuryoku the jitsuatsuryoku to set
     */
    public void setJitsuatsuryoku(FXHDD01 jitsuatsuryoku) {
        this.jitsuatsuryoku = jitsuatsuryoku;
    }

    /**
     * ｽﾘｯﾌﾟ流量
     * @return the slipryuuryou
     */
    public FXHDD01 getSlipryuuryou() {
        return slipryuuryou;
    }

    /**
     * ｽﾘｯﾌﾟ流量
     * @param slipryuuryou the slipryuuryou to set
     */
    public void setSlipryuuryou(FXHDD01 slipryuuryou) {
        this.slipryuuryou = slipryuuryou;
    }

    /**
     * ｽﾘｯﾌﾟ温度(IN)
     * @return the slipondoin
     */
    public FXHDD01 getSlipondoin() {
        return slipondoin;
    }

    /**
     * ｽﾘｯﾌﾟ温度(IN)
     * @param slipondoin the slipondoin to set
     */
    public void setSlipondoin(FXHDD01 slipondoin) {
        this.slipondoin = slipondoin;
    }

    /**
     * ｽﾘｯﾌﾟ温度(OUT)
     * @return the slipondoout
     */
    public FXHDD01 getSlipondoout() {
        return slipondoout;
    }

    /**
     * ｽﾘｯﾌﾟ温度(OUT)
     * @param slipondoout the slipondoout to set
     */
    public void setSlipondoout(FXHDD01 slipondoout) {
        this.slipondoout = slipondoout;
    }

    /**
     * 高圧分散開始担当者
     * @return the kouatsubunsankaishitantousya
     */
    public FXHDD01 getKouatsubunsankaishitantousya() {
        return kouatsubunsankaishitantousya;
    }

    /**
     * 高圧分散開始担当者
     * @param kouatsubunsankaishitantousya the kouatsubunsankaishitantousya to set
     */
    public void setKouatsubunsankaishitantousya(FXHDD01 kouatsubunsankaishitantousya) {
        this.kouatsubunsankaishitantousya = kouatsubunsankaishitantousya;
    }

    /**
     * 高圧分散終了日
     * @return the kouatsubunsansyuuryou_day
     */
    public FXHDD01 getKouatsubunsansyuuryou_day() {
        return kouatsubunsansyuuryou_day;
    }

    /**
     * 高圧分散終了日
     * @param kouatsubunsansyuuryou_day the kouatsubunsansyuuryou_day to set
     */
    public void setKouatsubunsansyuuryou_day(FXHDD01 kouatsubunsansyuuryou_day) {
        this.kouatsubunsansyuuryou_day = kouatsubunsansyuuryou_day;
    }

    /**
     * 高圧分散終了時間
     * @return the kouatsubunsansyuuryou_time
     */
    public FXHDD01 getKouatsubunsansyuuryou_time() {
        return kouatsubunsansyuuryou_time;
    }

    /**
     * 高圧分散終了時間
     * @param kouatsubunsansyuuryou_time the kouatsubunsansyuuryou_time to set
     */
    public void setKouatsubunsansyuuryou_time(FXHDD01 kouatsubunsansyuuryou_time) {
        this.kouatsubunsansyuuryou_time = kouatsubunsansyuuryou_time;
    }

    /**
     * 高圧分散停止担当者
     * @return the kouatsubunsanteishitantousya
     */
    public FXHDD01 getKouatsubunsanteishitantousya() {
        return kouatsubunsanteishitantousya;
    }

    /**
     * 高圧分散停止担当者
     * @param kouatsubunsanteishitantousya the kouatsubunsanteishitantousya to set
     */
    public void setKouatsubunsanteishitantousya(FXHDD01 kouatsubunsanteishitantousya) {
        this.kouatsubunsanteishitantousya = kouatsubunsanteishitantousya;
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

    /**
     * 追加Flag
     * @return the addFlg
     */
    public String getAddFlg() {
        return addFlg;
    }

    /**
     * 追加Flag
     * @param addFlg the addFlg to set
     */
    public void setAddFlg(String addFlg) {
        this.addFlg = addFlg;
    }
}
