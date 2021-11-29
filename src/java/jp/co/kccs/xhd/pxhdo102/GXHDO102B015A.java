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
 * 変更日	2021/10/27<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B015(ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽ)
 *
 * @author KCSS K.Jo
 * @since  2021/10/27
 */
@ViewScoped
@Named
public class GXHDO102B015A implements Serializable {
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
     * ﾌｨﾙﾀｰ交換日
     */
    private FXHDD01 filterkoukan_day;

    /**
     * ﾌｨﾙﾀｰ交換時間
     */
    private FXHDD01 filterkoukan_time;

    /**
     * ﾌｨﾙﾀｰ品名
     */
    private FXHDD01 filterhinmei;

    /**
     * LotNo
     */
    private FXHDD01 fplotno;

    /**
     * 取り付け本数
     */
    private FXHDD01 toritukehonsuu;

    /**
     * F/P準備_担当者
     */
    private FXHDD01 fpjyunbi_tantousya;

    /**
     * F/P開始日
     */
    private FXHDD01 fpkaisi_day;

    /**
     * F/P開始時間
     */
    private FXHDD01 fpkaisi_time;

    /**
     * F/P開始_担当者
     */
    private FXHDD01 fpkaisi_tantousya;

    /**
     * F/P終了日
     */
    private FXHDD01 fpsyuuryou_day;

    /**
     * F/P終了時間
     */
    private FXHDD01 fpsyuuryou_time;

    /**
     * F/P終了_担当者
     */
    private FXHDD01 fpsyuuryou_tantousya;

    /**
     * F/P時間
     */
    private FXHDD01 fpjikan;

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
    public GXHDO102B015A() {
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
     * ﾌｨﾙﾀｰ交換日
     * @return the filterkoukan_day
     */
    public FXHDD01 getFilterkoukan_day() {
        return filterkoukan_day;
    }

    /**
     * ﾌｨﾙﾀｰ交換日
     * @param filterkoukan_day the filterkoukan_day to set
     */
    public void setFilterkoukan_day(FXHDD01 filterkoukan_day) {
        this.filterkoukan_day = filterkoukan_day;
    }

    /**
     * ﾌｨﾙﾀｰ交換時間
     * @return the filterkoukan_time
     */
    public FXHDD01 getFilterkoukan_time() {
        return filterkoukan_time;
    }

    /**
     * ﾌｨﾙﾀｰ交換時間
     * @param filterkoukan_time the filterkoukan_time to set
     */
    public void setFilterkoukan_time(FXHDD01 filterkoukan_time) {
        this.filterkoukan_time = filterkoukan_time;
    }

    /**
     * ﾌｨﾙﾀｰ品名
     * @return the filterhinmei
     */
    public FXHDD01 getFilterhinmei() {
        return filterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ品名
     * @param filterhinmei the filterhinmei to set
     */
    public void setFilterhinmei(FXHDD01 filterhinmei) {
        this.filterhinmei = filterhinmei;
    }

    /**
     * LotNo
     * @return the fplotno
     */
    public FXHDD01 getFplotno() {
        return fplotno;
    }

    /**
     * LotNo
     * @param fplotno the fplotno to set
     */
    public void setFplotno(FXHDD01 fplotno) {
        this.fplotno = fplotno;
    }

    /**
     * 取り付け本数
     * @return the toritukehonsuu
     */
    public FXHDD01 getToritukehonsuu() {
        return toritukehonsuu;
    }

    /**
     * 取り付け本数
     * @param toritukehonsuu the toritukehonsuu to set
     */
    public void setToritukehonsuu(FXHDD01 toritukehonsuu) {
        this.toritukehonsuu = toritukehonsuu;
    }

    /**
     * F/P準備_担当者
     * @return the fpjyunbi_tantousya
     */
    public FXHDD01 getFpjyunbi_tantousya() {
        return fpjyunbi_tantousya;
    }

    /**
     * F/P準備_担当者
     * @param fpjyunbi_tantousya the fpjyunbi_tantousya to set
     */
    public void setFpjyunbi_tantousya(FXHDD01 fpjyunbi_tantousya) {
        this.fpjyunbi_tantousya = fpjyunbi_tantousya;
    }

    /**
     * F/P開始日
     * @return the fpkaisi_day
     */
    public FXHDD01 getFpkaisi_day() {
        return fpkaisi_day;
    }

    /**
     * F/P開始日
     * @param fpkaisi_day the fpkaisi_day to set
     */
    public void setFpkaisi_day(FXHDD01 fpkaisi_day) {
        this.fpkaisi_day = fpkaisi_day;
    }

    /**
     * F/P開始時間
     * @return the fpkaisi_time
     */
    public FXHDD01 getFpkaisi_time() {
        return fpkaisi_time;
    }

    /**
     * F/P開始時間
     * @param fpkaisi_time the fpkaisi_time to set
     */
    public void setFpkaisi_time(FXHDD01 fpkaisi_time) {
        this.fpkaisi_time = fpkaisi_time;
    }

    /**
     * F/P開始_担当者
     * @return the fpkaisi_tantousya
     */
    public FXHDD01 getFpkaisi_tantousya() {
        return fpkaisi_tantousya;
    }

    /**
     * F/P開始_担当者
     * @param fpkaisi_tantousya the fpkaisi_tantousya to set
     */
    public void setFpkaisi_tantousya(FXHDD01 fpkaisi_tantousya) {
        this.fpkaisi_tantousya = fpkaisi_tantousya;
    }

    /**
     * F/P終了日
     * @return the fpsyuuryou_day
     */
    public FXHDD01 getFpsyuuryou_day() {
        return fpsyuuryou_day;
    }

    /**
     * F/P終了日
     * @param fpsyuuryou_day the fpsyuuryou_day to set
     */
    public void setFpsyuuryou_day(FXHDD01 fpsyuuryou_day) {
        this.fpsyuuryou_day = fpsyuuryou_day;
    }

    /**
     * F/P終了時間
     * @return the fpsyuuryou_time
     */
    public FXHDD01 getFpsyuuryou_time() {
        return fpsyuuryou_time;
    }

    /**
     * F/P終了時間
     * @param fpsyuuryou_time the fpsyuuryou_time to set
     */
    public void setFpsyuuryou_time(FXHDD01 fpsyuuryou_time) {
        this.fpsyuuryou_time = fpsyuuryou_time;
    }

    /**
     * F/P終了_担当者
     * @return the fpsyuuryou_tantousya
     */
    public FXHDD01 getFpsyuuryou_tantousya() {
        return fpsyuuryou_tantousya;
    }

    /**
     * F/P終了_担当者
     * @param fpsyuuryou_tantousya the fpsyuuryou_tantousya to set
     */
    public void setFpsyuuryou_tantousya(FXHDD01 fpsyuuryou_tantousya) {
        this.fpsyuuryou_tantousya = fpsyuuryou_tantousya;
    }

    /**
     * F/P時間
     * @return the fpjikan
     */
    public FXHDD01 getFpjikan() {
        return fpjikan;
    }

    /**
     * F/P時間
     * @param fpjikan the fpjikan to set
     */
    public void setFpjikan(FXHDD01 fpjikan) {
        this.fpjikan = fpjikan;
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