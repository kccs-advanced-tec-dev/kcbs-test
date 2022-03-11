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
 * 変更日	2021/12/06<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B027(ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ))
 *
 * @author KCSS K.Jo
 * @since  2021/12/06
 */
@ViewScoped
@Named
public class GXHDO102B027A implements Serializable {
    /**
     * WIPﾛｯﾄNo
     */
    private FXHDD01 wiplotno;

    /**
     * ｽﾘｯﾌﾟ品名
     */
    private FXHDD01 sliphinmei;

    /**
     * ｽﾘｯﾌﾟLotNo
     */
    private FXHDD01 sliplotno;

    /**
     * ﾛｯﾄ区分
     */
    private FXHDD01 lotkubun;

    /**
     * 原料記号
     */
    private FXHDD01 genryoukigou;

    /**
     * ｽﾗﾘｰ品名
     */
    private FXHDD01 slurryhinmei;

    /**
     * ｽﾗﾘｰLotNo①
     */
    private FXHDD01 slurrylotno1;

    /**
     * ｽﾗﾘｰLotNo②
     */
    private FXHDD01 slurrylotno2;

    /**
     * ｽﾗﾘｰLotNo③
     */
    private FXHDD01 slurrylotno3;

    /**
     * ｽﾗﾘｰ有効期限
     */
    private FXHDD01 slurryyuukoukigen;

    /**
     * 乾燥固形分
     */
    private FXHDD01 kansoukokeibun;

    /**
     * 脱脂固形分
     */
    private FXHDD01 dassikokeibun;

    /**
     * 粉砕終了日
     */
    private FXHDD01 funsaisyuuryou_day;

    /**
     * 粉砕終了時間
     */
    private FXHDD01 funsaisyuuryou_time;

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合日
     */
    private FXHDD01 binderkongou_day;

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合時間
     */
    private FXHDD01 binderkongou_time;

    /**
     * ｽﾗﾘｰ経過日数
     */
    private FXHDD01 slurrykeikanisuu;

    /**
     * ｽﾗﾘｰ重量①
     */
    private FXHDD01 slurryjyuuryou1;

    /**
     * ｽﾗﾘｰ重量②
     */
    private FXHDD01 slurryjyuuryou2;

    /**
     * ｽﾗﾘｰ重量③
     */
    private FXHDD01 slurryjyuuryou3;

    /**
     * ｽﾗﾘｰ重量④
     */
    private FXHDD01 slurryjyuuryou4;

    /**
     * ｽﾗﾘｰ重量⑤
     */
    private FXHDD01 slurryjyuuryou5;

    /**
     * ｽﾗﾘｰ重量⑥
     */
    private FXHDD01 slurryjyuuryou6;

    /**
     * ｽﾗﾘｰ合計重量
     */
    private FXHDD01 slurrygoukeijyuuryou;

    /**
     * 固形分比率
     */
    private FXHDD01 kokeibunhiritu;

    /**
     * 固形分調整量➀
     */
    private FXHDD01 kokeibuntyouseiryou1;

    /**
     * 固形分調整量➁
     */
    private FXHDD01 kokeibuntyouseiryou2;

    /**
     * 固形分調整量
     */
    private FXHDD01 kokeibuntyouseiryou;

    /**
     * ﾄﾙｴﾝ添加量
     */
    private FXHDD01 toluenetenkaryou;

    /**
     * ｿﾙﾐｯｸｽ添加量
     */
    private FXHDD01 solmixtenkaryou;

    /**
     * 担当者
     */
    private FXHDD01 tantousya;

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
    public GXHDO102B027A() {
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
     * ｽﾘｯﾌﾟ品名
     * @return the sliphinmei
     */
    public FXHDD01 getSliphinmei() {
        return sliphinmei;
    }

    /**
     * ｽﾘｯﾌﾟ品名
     * @param sliphinmei the sliphinmei to set
     */
    public void setSliphinmei(FXHDD01 sliphinmei) {
        this.sliphinmei = sliphinmei;
    }

    /**
     * ｽﾘｯﾌﾟLotNo
     * @return the sliplotno
     */
    public FXHDD01 getSliplotno() {
        return sliplotno;
    }

    /**
     * ｽﾘｯﾌﾟLotNo
     * @param sliplotno the sliplotno to set
     */
    public void setSliplotno(FXHDD01 sliplotno) {
        this.sliplotno = sliplotno;
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
     * 原料記号
     * @return the genryoukigou
     */
    public FXHDD01 getGenryoukigou() {
        return genryoukigou;
    }

    /**
     * 原料記号
     * @param genryoukigou the genryoukigou to set
     */
    public void setGenryoukigou(FXHDD01 genryoukigou) {
        this.genryoukigou = genryoukigou;
    }

    /**
     * ｽﾗﾘｰ品名
     * @return the slurryhinmei
     */
    public FXHDD01 getSlurryhinmei() {
        return slurryhinmei;
    }

    /**
     * ｽﾗﾘｰ品名
     * @param slurryhinmei the slurryhinmei to set
     */
    public void setSlurryhinmei(FXHDD01 slurryhinmei) {
        this.slurryhinmei = slurryhinmei;
    }

    /**
     * ｽﾗﾘｰLotNo①
     * @return the slurrylotno1
     */
    public FXHDD01 getSlurrylotno1() {
        return slurrylotno1;
    }

    /**
     * ｽﾗﾘｰLotNo①
     * @param slurrylotno1 the slurrylotno1 to set
     */
    public void setSlurrylotno1(FXHDD01 slurrylotno1) {
        this.slurrylotno1 = slurrylotno1;
    }

    /**
     * ｽﾗﾘｰLotNo②
     * @return the slurrylotno2
     */
    public FXHDD01 getSlurrylotno2() {
        return slurrylotno2;
    }

    /**
     * ｽﾗﾘｰLotNo②
     * @param slurrylotno2 the slurrylotno2 to set
     */
    public void setSlurrylotno2(FXHDD01 slurrylotno2) {
        this.slurrylotno2 = slurrylotno2;
    }

    /**
     * ｽﾗﾘｰLotNo③
     * @return the slurrylotno3
     */
    public FXHDD01 getSlurrylotno3() {
        return slurrylotno3;
    }

    /**
     * ｽﾗﾘｰLotNo③
     * @param slurrylotno3 the slurrylotno3 to set
     */
    public void setSlurrylotno3(FXHDD01 slurrylotno3) {
        this.slurrylotno3 = slurrylotno3;
    }

    /**
     * ｽﾗﾘｰ有効期限
     * @return the slurryyuukoukigen
     */
    public FXHDD01 getSlurryyuukoukigen() {
        return slurryyuukoukigen;
    }

    /**
     * ｽﾗﾘｰ有効期限
     * @param slurryyuukoukigen the slurryyuukoukigen to set
     */
    public void setSlurryyuukoukigen(FXHDD01 slurryyuukoukigen) {
        this.slurryyuukoukigen = slurryyuukoukigen;
    }

    /**
     * 乾燥固形分
     * @return the kansoukokeibun
     */
    public FXHDD01 getKansoukokeibun() {
        return kansoukokeibun;
    }

    /**
     * 乾燥固形分
     * @param kansoukokeibun the kansoukokeibun to set
     */
    public void setKansoukokeibun(FXHDD01 kansoukokeibun) {
        this.kansoukokeibun = kansoukokeibun;
    }

    /**
     * 脱脂固形分
     * @return the dassikokeibun
     */
    public FXHDD01 getDassikokeibun() {
        return dassikokeibun;
    }

    /**
     * 脱脂固形分
     * @param dassikokeibun the dassikokeibun to set
     */
    public void setDassikokeibun(FXHDD01 dassikokeibun) {
        this.dassikokeibun = dassikokeibun;
    }

    /**
     * 粉砕終了日
     * @return the funsaisyuuryou_day
     */
    public FXHDD01 getFunsaisyuuryou_day() {
        return funsaisyuuryou_day;
    }

    /**
     * 粉砕終了日
     * @param funsaisyuuryou_day the funsaisyuuryou_day to set
     */
    public void setFunsaisyuuryou_day(FXHDD01 funsaisyuuryou_day) {
        this.funsaisyuuryou_day = funsaisyuuryou_day;
    }

    /**
     * 粉砕終了時間
     * @return the funsaisyuuryou_time
     */
    public FXHDD01 getFunsaisyuuryou_time() {
        return funsaisyuuryou_time;
    }

    /**
     * 粉砕終了時間
     * @param funsaisyuuryou_time the funsaisyuuryou_time to set
     */
    public void setFunsaisyuuryou_time(FXHDD01 funsaisyuuryou_time) {
        this.funsaisyuuryou_time = funsaisyuuryou_time;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合日
     * @return the binderkongou_day
     */
    public FXHDD01 getBinderkongou_day() {
        return binderkongou_day;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合日
     * @param binderkongou_day the binderkongou_day to set
     */
    public void setBinderkongou_day(FXHDD01 binderkongou_day) {
        this.binderkongou_day = binderkongou_day;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合時間
     * @return the binderkongou_time
     */
    public FXHDD01 getBinderkongou_time() {
        return binderkongou_time;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合時間
     * @param binderkongou_time the binderkongou_time to set
     */
    public void setBinderkongou_time(FXHDD01 binderkongou_time) {
        this.binderkongou_time = binderkongou_time;
    }

    /**
     * ｽﾗﾘｰ経過日数
     * @return the slurrykeikanisuu
     */
    public FXHDD01 getSlurrykeikanisuu() {
        return slurrykeikanisuu;
    }

    /**
     * ｽﾗﾘｰ経過日数
     * @param slurrykeikanisuu the slurrykeikanisuu to set
     */
    public void setSlurrykeikanisuu(FXHDD01 slurrykeikanisuu) {
        this.slurrykeikanisuu = slurrykeikanisuu;
    }

    /**
     * ｽﾗﾘｰ重量①
     * @return the slurryjyuuryou1
     */
    public FXHDD01 getSlurryjyuuryou1() {
        return slurryjyuuryou1;
    }

    /**
     * ｽﾗﾘｰ重量①
     * @param slurryjyuuryou1 the slurryjyuuryou1 to set
     */
    public void setSlurryjyuuryou1(FXHDD01 slurryjyuuryou1) {
        this.slurryjyuuryou1 = slurryjyuuryou1;
    }

    /**
     * ｽﾗﾘｰ重量②
     * @return the slurryjyuuryou2
     */
    public FXHDD01 getSlurryjyuuryou2() {
        return slurryjyuuryou2;
    }

    /**
     * ｽﾗﾘｰ重量②
     * @param slurryjyuuryou2 the slurryjyuuryou2 to set
     */
    public void setSlurryjyuuryou2(FXHDD01 slurryjyuuryou2) {
        this.slurryjyuuryou2 = slurryjyuuryou2;
    }

    /**
     * ｽﾗﾘｰ重量③
     * @return the slurryjyuuryou3
     */
    public FXHDD01 getSlurryjyuuryou3() {
        return slurryjyuuryou3;
    }

    /**
     * ｽﾗﾘｰ重量③
     * @param slurryjyuuryou3 the slurryjyuuryou3 to set
     */
    public void setSlurryjyuuryou3(FXHDD01 slurryjyuuryou3) {
        this.slurryjyuuryou3 = slurryjyuuryou3;
    }

    /**
     * ｽﾗﾘｰ重量④
     * @return the slurryjyuuryou4
     */
    public FXHDD01 getSlurryjyuuryou4() {
        return slurryjyuuryou4;
    }

    /**
     * ｽﾗﾘｰ重量④
     * @param slurryjyuuryou4 the slurryjyuuryou4 to set
     */
    public void setSlurryjyuuryou4(FXHDD01 slurryjyuuryou4) {
        this.slurryjyuuryou4 = slurryjyuuryou4;
    }

    /**
     * ｽﾗﾘｰ重量⑤
     * @return the slurryjyuuryou5
     */
    public FXHDD01 getSlurryjyuuryou5() {
        return slurryjyuuryou5;
    }

    /**
     * ｽﾗﾘｰ重量⑤
     * @param slurryjyuuryou5 the slurryjyuuryou5 to set
     */
    public void setSlurryjyuuryou5(FXHDD01 slurryjyuuryou5) {
        this.slurryjyuuryou5 = slurryjyuuryou5;
    }

    /**
     * ｽﾗﾘｰ重量⑥
     * @return the slurryjyuuryou6
     */
    public FXHDD01 getSlurryjyuuryou6() {
        return slurryjyuuryou6;
    }

    /**
     * ｽﾗﾘｰ重量⑥
     * @param slurryjyuuryou6 the slurryjyuuryou6 to set
     */
    public void setSlurryjyuuryou6(FXHDD01 slurryjyuuryou6) {
        this.slurryjyuuryou6 = slurryjyuuryou6;
    }

    /**
     * ｽﾗﾘｰ合計重量
     * @return the slurrygoukeijyuuryou
     */
    public FXHDD01 getSlurrygoukeijyuuryou() {
        return slurrygoukeijyuuryou;
    }

    /**
     * ｽﾗﾘｰ合計重量
     * @param slurrygoukeijyuuryou the slurrygoukeijyuuryou to set
     */
    public void setSlurrygoukeijyuuryou(FXHDD01 slurrygoukeijyuuryou) {
        this.slurrygoukeijyuuryou = slurrygoukeijyuuryou;
    }

    /**
     * 固形分比率
     * @return the kokeibunhiritu
     */
    public FXHDD01 getKokeibunhiritu() {
        return kokeibunhiritu;
    }

    /**
     * 固形分比率
     * @param kokeibunhiritu the kokeibunhiritu to set
     */
    public void setKokeibunhiritu(FXHDD01 kokeibunhiritu) {
        this.kokeibunhiritu = kokeibunhiritu;
    }

    /**
     * 固形分調整量➀
     * @return the kokeibuntyouseiryou1
     */
    public FXHDD01 getKokeibuntyouseiryou1() {
        return kokeibuntyouseiryou1;
    }

    /**
     * 固形分調整量➀
     * @param kokeibuntyouseiryou1 the kokeibuntyouseiryou1 to set
     */
    public void setKokeibuntyouseiryou1(FXHDD01 kokeibuntyouseiryou1) {
        this.kokeibuntyouseiryou1 = kokeibuntyouseiryou1;
    }

    /**
     * 固形分調整量➁
     * @return the kokeibuntyouseiryou2
     */
    public FXHDD01 getKokeibuntyouseiryou2() {
        return kokeibuntyouseiryou2;
    }

    /**
     * 固形分調整量➁
     * @param kokeibuntyouseiryou2 the kokeibuntyouseiryou2 to set
     */
    public void setKokeibuntyouseiryou2(FXHDD01 kokeibuntyouseiryou2) {
        this.kokeibuntyouseiryou2 = kokeibuntyouseiryou2;
    }

    /**
     * 固形分調整量
     * @return the kokeibuntyouseiryou
     */
    public FXHDD01 getKokeibuntyouseiryou() {
        return kokeibuntyouseiryou;
    }

    /**
     * 固形分調整量
     * @param kokeibuntyouseiryou the kokeibuntyouseiryou to set
     */
    public void setKokeibuntyouseiryou(FXHDD01 kokeibuntyouseiryou) {
        this.kokeibuntyouseiryou = kokeibuntyouseiryou;
    }

    /**
     * ﾄﾙｴﾝ添加量
     * @return the toluenetenkaryou
     */
    public FXHDD01 getToluenetenkaryou() {
        return toluenetenkaryou;
    }

    /**
     * ﾄﾙｴﾝ添加量
     * @param toluenetenkaryou the toluenetenkaryou to set
     */
    public void setToluenetenkaryou(FXHDD01 toluenetenkaryou) {
        this.toluenetenkaryou = toluenetenkaryou;
    }

    /**
     * ｿﾙﾐｯｸｽ添加量
     * @return the solmixtenkaryou
     */
    public FXHDD01 getSolmixtenkaryou() {
        return solmixtenkaryou;
    }

    /**
     * ｿﾙﾐｯｸｽ添加量
     * @param solmixtenkaryou the solmixtenkaryou to set
     */
    public void setSolmixtenkaryou(FXHDD01 solmixtenkaryou) {
        this.solmixtenkaryou = solmixtenkaryou;
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