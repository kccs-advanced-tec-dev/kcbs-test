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
 * 変更日	2021/10/22<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B012(添加材ｽﾗﾘｰ作製・FP排出)
 *
 * @author KCSS K.Jo
 * @since  2021/10/22
 */
@ViewScoped
@Named
public class GXHDO102B012A implements Serializable {
    /**
     * WIPﾛｯﾄNo
     */
    private FXHDD01 wiplotno;

    /**
     * 添加材ｽﾗﾘｰ品名
     */
    private FXHDD01 tenkazaislurryhinmei;

    /**
     * 添加材ｽﾗﾘｰLotNo
     */
    private FXHDD01 tenkazaislurrylotno;

    /**
     * ﾛｯﾄ区分
     */
    private FXHDD01 lotkubun;

    /**
     * 投入量
     */
    private FXHDD01 tounyuuryou;

    /**
     * 最終パス回数
     */
    private FXHDD01 saisyuupasskaisuu;

    /**
     * 保存用ｻﾝﾌﾟﾙ回収
     */
    private FXHDD01 hozonyousamplekaisyuu;

    /**
     * 風袋重量①
     */
    private FXHDD01 fuutaijyuuryou1;

    /**
     * 風袋重量②
     */
    private FXHDD01 fuutaijyuuryou2;

    /**
     * 風袋重量③
     */
    private FXHDD01 fuutaijyuuryou3;

    /**
     * 風袋重量④
     */
    private FXHDD01 fuutaijyuuryou4;

    /**
     * 風袋重量⑤
     */
    private FXHDD01 fuutaijyuuryou5;

    /**
     * 風袋重量⑥
     */
    private FXHDD01 fuutaijyuuryou6;

    /**
     * F/P準備_担当者
     */
    private FXHDD01 fpjyunbi_tantousya;

    /**
     * F/P準備_ﾌｨﾙﾀｰ連結
     */
    private FXHDD01 fpjyunbi_filterrenketu;

    /**
     * F/P準備_ﾌｨﾙﾀｰ品名①
     */
    private FXHDD01 fpjyunbi_filterhinmei1;

    /**
     * F/P準備_LotNo①
     */
    private FXHDD01 fpjyunbi_lotno1;

    /**
     * F/P準備_取り付け本数①
     */
    private FXHDD01 fpjyunbi_toritukehonsuu1;

    /**
     * F/P準備_ﾌｨﾙﾀｰ品名②
     */
    private FXHDD01 fpjyunbi_filterhinmei2;

    /**
     * F/P準備_LotNo②
     */
    private FXHDD01 fpjyunbi_lotno2;

    /**
     * F/P準備_取り付け本数②
     */
    private FXHDD01 fpjyunbi_toritukehonsuu2;

    /**
     * 排出容器の内袋
     */
    private FXHDD01 haisyutuyoukinoutibukuro;

    /**
     * ﾌｨﾙﾀｰ使用回数
     */
    private FXHDD01 filterkaisuu;

    /**
     * F/PﾀﾝｸNo
     */
    private FXHDD01 fptankno;

    /**
     * 洗浄確認
     */
    private FXHDD01 senjyoukakunin;

    /**
     * F/P開始日
     */
    private FXHDD01 fpkaisi_day;

    /**
     * F/P開始時間
     */
    private FXHDD01 fpkaisi_time;

    /**
     * 圧送ﾚｷﾞｭﾚｰﾀｰNo
     */
    private FXHDD01 assouregulatorno;

    /**
     * 圧送圧力
     */
    private FXHDD01 assouaturyoku;

    /**
     * F/P開始_担当者
     */
    private FXHDD01 fpkaisi_tantousya;

    /**
     * F/P停止日
     */
    private FXHDD01 fpteisi_day;

    /**
     * F/P停止時間
     */
    private FXHDD01 fpteisi_time;

    /**
     * F/P交換_ﾌｨﾙﾀｰ連結
     */
    private FXHDD01 fpkoukan_filterrenketu;

    /**
     * F/P交換_ﾌｨﾙﾀｰ品名①
     */
    private FXHDD01 fpkoukan_filterhinmei1;

    /**
     * F/P交換_LotNo①
     */
    private FXHDD01 fpkoukan_lotno1;

    /**
     * F/P交換_取り付け本数①
     */
    private FXHDD01 fpkoukan_toritukehonsuu1;

    /**
     * F/P交換_ﾌｨﾙﾀｰ品名②
     */
    private FXHDD01 fpkoukan_filterhinmei2;

    /**
     * F/P交換_LotNo②
     */
    private FXHDD01 fpkoukan_lotno2;

    /**
     * F/P交換_取り付け本数②
     */
    private FXHDD01 fpkoukan_toritukehonsuu2;

    /**
     * F/P再開日
     */
    private FXHDD01 fpsaikai_day;

    /**
     * F/P再開時間
     */
    private FXHDD01 fpsaikai_time;

    /**
     * F/P交換_担当者
     */
    private FXHDD01 fpkoukan_tantousya;

    /**
     * F/P終了日
     */
    private FXHDD01 fpsyuuryou_day;

    /**
     * F/P終了時間
     */
    private FXHDD01 fpsyuuryou_time;

    /**
     * F/P時間
     */
    private FXHDD01 fpjikan;

    /**
     * F/P終了_担当者
     */
    private FXHDD01 fpsyuurou_tantousya;

    /**
     * 総重量①
     */
    private FXHDD01 soujyuurou1;

    /**
     * 総重量②
     */
    private FXHDD01 soujyuurou2;

    /**
     * 総重量③
     */
    private FXHDD01 soujyuurou3;

    /**
     * 総重量④
     */
    private FXHDD01 soujyuurou4;

    /**
     * 総重量⑤
     */
    private FXHDD01 soujyuurou5;

    /**
     * 総重量⑥
     */
    private FXHDD01 soujyuurou6;

    /**
     * 添加材ｽﾗﾘｰ重量①
     */
    private FXHDD01 tenkazaislurryjyuuryou1;

    /**
     * 添加材ｽﾗﾘｰ重量②
     */
    private FXHDD01 tenkazaislurryjyuuryou2;

    /**
     * 添加材ｽﾗﾘｰ重量③
     */
    private FXHDD01 tenkazaislurryjyuuryou3;

    /**
     * 添加材ｽﾗﾘｰ重量④
     */
    private FXHDD01 tenkazaislurryjyuuryou4;

    /**
     * 添加材ｽﾗﾘｰ重量⑤
     */
    private FXHDD01 tenkazaislurryjyuuryou5;

    /**
     * 添加材ｽﾗﾘｰ重量⑥
     */
    private FXHDD01 tenkazaislurryjyuuryou6;

    /**
     * 添加材ｽﾗﾘｰ重量合計
     */
    private FXHDD01 tenkazaislurryjyuuryougoukei;

    /**
     * 歩留まり
     */
    private FXHDD01 budomari;

    /**
     * 添加材ｽﾗﾘｰ有効期限
     */
    private FXHDD01 tenkazaislurryyuukoukigen;

    /**
     * 粉砕判定
     */
    private FXHDD01 funsaihantei;

    /**
     * 担当者
     */
    private FXHDD01 tantousya;

    /**
     * コンストラクタ
     */
    public GXHDO102B012A() {
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
     * 添加材ｽﾗﾘｰ品名
     * @return the tenkazaislurryhinmei
     */
    public FXHDD01 getTenkazaislurryhinmei() {
        return tenkazaislurryhinmei;
    }

    /**
     * 添加材ｽﾗﾘｰ品名
     * @param tenkazaislurryhinmei the tenkazaislurryhinmei to set
     */
    public void setTenkazaislurryhinmei(FXHDD01 tenkazaislurryhinmei) {
        this.tenkazaislurryhinmei = tenkazaislurryhinmei;
    }

    /**
     * 添加材ｽﾗﾘｰLotNo
     * @return the tenkazaislurrylotno
     */
    public FXHDD01 getTenkazaislurrylotno() {
        return tenkazaislurrylotno;
    }

    /**
     * 添加材ｽﾗﾘｰLotNo
     * @param tenkazaislurrylotno the tenkazaislurrylotno to set
     */
    public void setTenkazaislurrylotno(FXHDD01 tenkazaislurrylotno) {
        this.tenkazaislurrylotno = tenkazaislurrylotno;
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
     * 投入量
     * @return the tounyuuryou
     */
    public FXHDD01 getTounyuuryou() {
        return tounyuuryou;
    }

    /**
     * 投入量
     * @param tounyuuryou the tounyuuryou to set
     */
    public void setTounyuuryou(FXHDD01 tounyuuryou) {
        this.tounyuuryou = tounyuuryou;
    }

    /**
     * 最終パス回数
     * @return the saisyuupasskaisuu
     */
    public FXHDD01 getSaisyuupasskaisuu() {
        return saisyuupasskaisuu;
    }

    /**
     * 最終パス回数
     * @param saisyuupasskaisuu the saisyuupasskaisuu to set
     */
    public void setSaisyuupasskaisuu(FXHDD01 saisyuupasskaisuu) {
        this.saisyuupasskaisuu = saisyuupasskaisuu;
    }

    /**
     * 保存用ｻﾝﾌﾟﾙ回収
     * @return the hozonyousamplekaisyuu
     */
    public FXHDD01 getHozonyousamplekaisyuu() {
        return hozonyousamplekaisyuu;
    }

    /**
     * 保存用ｻﾝﾌﾟﾙ回収
     * @param hozonyousamplekaisyuu the hozonyousamplekaisyuu to set
     */
    public void setHozonyousamplekaisyuu(FXHDD01 hozonyousamplekaisyuu) {
        this.hozonyousamplekaisyuu = hozonyousamplekaisyuu;
    }

    /**
     * 風袋重量①
     * @return the fuutaijyuuryou1
     */
    public FXHDD01 getFuutaijyuuryou1() {
        return fuutaijyuuryou1;
    }

    /**
     * 風袋重量①
     * @param fuutaijyuuryou1 the fuutaijyuuryou1 to set
     */
    public void setFuutaijyuuryou1(FXHDD01 fuutaijyuuryou1) {
        this.fuutaijyuuryou1 = fuutaijyuuryou1;
    }

    /**
     * 風袋重量②
     * @return the fuutaijyuuryou2
     */
    public FXHDD01 getFuutaijyuuryou2() {
        return fuutaijyuuryou2;
    }

    /**
     * 風袋重量②
     * @param fuutaijyuuryou2 the fuutaijyuuryou2 to set
     */
    public void setFuutaijyuuryou2(FXHDD01 fuutaijyuuryou2) {
        this.fuutaijyuuryou2 = fuutaijyuuryou2;
    }

    /**
     * 風袋重量③
     * @return the fuutaijyuuryou3
     */
    public FXHDD01 getFuutaijyuuryou3() {
        return fuutaijyuuryou3;
    }

    /**
     * 風袋重量③
     * @param fuutaijyuuryou3 the fuutaijyuuryou3 to set
     */
    public void setFuutaijyuuryou3(FXHDD01 fuutaijyuuryou3) {
        this.fuutaijyuuryou3 = fuutaijyuuryou3;
    }

    /**
     * 風袋重量④
     * @return the fuutaijyuuryou4
     */
    public FXHDD01 getFuutaijyuuryou4() {
        return fuutaijyuuryou4;
    }

    /**
     * 風袋重量④
     * @param fuutaijyuuryou4 the fuutaijyuuryou4 to set
     */
    public void setFuutaijyuuryou4(FXHDD01 fuutaijyuuryou4) {
        this.fuutaijyuuryou4 = fuutaijyuuryou4;
    }

    /**
     * 風袋重量⑤
     * @return the fuutaijyuuryou5
     */
    public FXHDD01 getFuutaijyuuryou5() {
        return fuutaijyuuryou5;
    }

    /**
     * 風袋重量⑤
     * @param fuutaijyuuryou5 the fuutaijyuuryou5 to set
     */
    public void setFuutaijyuuryou5(FXHDD01 fuutaijyuuryou5) {
        this.fuutaijyuuryou5 = fuutaijyuuryou5;
    }

    /**
     * 風袋重量⑥
     * @return the fuutaijyuuryou6
     */
    public FXHDD01 getFuutaijyuuryou6() {
        return fuutaijyuuryou6;
    }

    /**
     * 風袋重量⑥
     * @param fuutaijyuuryou6 the fuutaijyuuryou6 to set
     */
    public void setFuutaijyuuryou6(FXHDD01 fuutaijyuuryou6) {
        this.fuutaijyuuryou6 = fuutaijyuuryou6;
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
     * F/P準備_ﾌｨﾙﾀｰ連結
     * @return the fpjyunbi_filterrenketu
     */
    public FXHDD01 getFpjyunbi_filterrenketu() {
        return fpjyunbi_filterrenketu;
    }

    /**
     * F/P準備_ﾌｨﾙﾀｰ連結
     * @param fpjyunbi_filterrenketu the fpjyunbi_filterrenketu to set
     */
    public void setFpjyunbi_filterrenketu(FXHDD01 fpjyunbi_filterrenketu) {
        this.fpjyunbi_filterrenketu = fpjyunbi_filterrenketu;
    }

    /**
     * F/P準備_ﾌｨﾙﾀｰ品名①
     * @return the fpjyunbi_filterhinmei1
     */
    public FXHDD01 getFpjyunbi_filterhinmei1() {
        return fpjyunbi_filterhinmei1;
    }

    /**
     * F/P準備_ﾌｨﾙﾀｰ品名①
     * @param fpjyunbi_filterhinmei1 the fpjyunbi_filterhinmei1 to set
     */
    public void setFpjyunbi_filterhinmei1(FXHDD01 fpjyunbi_filterhinmei1) {
        this.fpjyunbi_filterhinmei1 = fpjyunbi_filterhinmei1;
    }

    /**
     * F/P準備_LotNo①
     * @return the fpjyunbi_lotno1
     */
    public FXHDD01 getFpjyunbi_lotno1() {
        return fpjyunbi_lotno1;
    }

    /**
     * F/P準備_LotNo①
     * @param fpjyunbi_lotno1 the fpjyunbi_lotno1 to set
     */
    public void setFpjyunbi_lotno1(FXHDD01 fpjyunbi_lotno1) {
        this.fpjyunbi_lotno1 = fpjyunbi_lotno1;
    }

    /**
     * F/P準備_取り付け本数①
     * @return the fpjyunbi_toritukehonsuu1
     */
    public FXHDD01 getFpjyunbi_toritukehonsuu1() {
        return fpjyunbi_toritukehonsuu1;
    }

    /**
     * F/P準備_取り付け本数①
     * @param fpjyunbi_toritukehonsuu1 the fpjyunbi_toritukehonsuu1 to set
     */
    public void setFpjyunbi_toritukehonsuu1(FXHDD01 fpjyunbi_toritukehonsuu1) {
        this.fpjyunbi_toritukehonsuu1 = fpjyunbi_toritukehonsuu1;
    }

    /**
     * F/P準備_ﾌｨﾙﾀｰ品名②
     * @return the fpjyunbi_filterhinmei2
     */
    public FXHDD01 getFpjyunbi_filterhinmei2() {
        return fpjyunbi_filterhinmei2;
    }

    /**
     * F/P準備_ﾌｨﾙﾀｰ品名②
     * @param fpjyunbi_filterhinmei2 the fpjyunbi_filterhinmei2 to set
     */
    public void setFpjyunbi_filterhinmei2(FXHDD01 fpjyunbi_filterhinmei2) {
        this.fpjyunbi_filterhinmei2 = fpjyunbi_filterhinmei2;
    }

    /**
     * F/P準備_LotNo②
     * @return the fpjyunbi_lotno2
     */
    public FXHDD01 getFpjyunbi_lotno2() {
        return fpjyunbi_lotno2;
    }

    /**
     * F/P準備_LotNo②
     * @param fpjyunbi_lotno2 the fpjyunbi_lotno2 to set
     */
    public void setFpjyunbi_lotno2(FXHDD01 fpjyunbi_lotno2) {
        this.fpjyunbi_lotno2 = fpjyunbi_lotno2;
    }

    /**
     * F/P準備_取り付け本数②
     * @return the fpjyunbi_toritukehonsuu2
     */
    public FXHDD01 getFpjyunbi_toritukehonsuu2() {
        return fpjyunbi_toritukehonsuu2;
    }

    /**
     * F/P準備_取り付け本数②
     * @param fpjyunbi_toritukehonsuu2 the fpjyunbi_toritukehonsuu2 to set
     */
    public void setFpjyunbi_toritukehonsuu2(FXHDD01 fpjyunbi_toritukehonsuu2) {
        this.fpjyunbi_toritukehonsuu2 = fpjyunbi_toritukehonsuu2;
    }

    /**
     * 排出容器の内袋
     * @return the haisyutuyoukinoutibukuro
     */
    public FXHDD01 getHaisyutuyoukinoutibukuro() {
        return haisyutuyoukinoutibukuro;
    }

    /**
     * 排出容器の内袋
     * @param haisyutuyoukinoutibukuro the haisyutuyoukinoutibukuro to set
     */
    public void setHaisyutuyoukinoutibukuro(FXHDD01 haisyutuyoukinoutibukuro) {
        this.haisyutuyoukinoutibukuro = haisyutuyoukinoutibukuro;
    }

    /**
     * ﾌｨﾙﾀｰ使用回数
     * @return the filterkaisuu
     */
    public FXHDD01 getFilterkaisuu() {
        return filterkaisuu;
    }

    /**
     * ﾌｨﾙﾀｰ使用回数
     * @param filterkaisuu the filterkaisuu to set
     */
    public void setFilterkaisuu(FXHDD01 filterkaisuu) {
        this.filterkaisuu = filterkaisuu;
    }

    /**
     * F/PﾀﾝｸNo
     * @return the fptankno
     */
    public FXHDD01 getFptankno() {
        return fptankno;
    }

    /**
     * F/PﾀﾝｸNo
     * @param fptankno the fptankno to set
     */
    public void setFptankno(FXHDD01 fptankno) {
        this.fptankno = fptankno;
    }

    /**
     * 洗浄確認
     * @return the senjyoukakunin
     */
    public FXHDD01 getSenjyoukakunin() {
        return senjyoukakunin;
    }

    /**
     * 洗浄確認
     * @param senjyoukakunin the senjyoukakunin to set
     */
    public void setSenjyoukakunin(FXHDD01 senjyoukakunin) {
        this.senjyoukakunin = senjyoukakunin;
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
     * 圧送ﾚｷﾞｭﾚｰﾀｰNo
     * @return the assouregulatorno
     */
    public FXHDD01 getAssouregulatorno() {
        return assouregulatorno;
    }

    /**
     * 圧送ﾚｷﾞｭﾚｰﾀｰNo
     * @param assouregulatorno the assouregulatorno to set
     */
    public void setAssouregulatorno(FXHDD01 assouregulatorno) {
        this.assouregulatorno = assouregulatorno;
    }

    /**
     * 圧送圧力
     * @return the assouaturyoku
     */
    public FXHDD01 getAssouaturyoku() {
        return assouaturyoku;
    }

    /**
     * 圧送圧力
     * @param assouaturyoku the assouaturyoku to set
     */
    public void setAssouaturyoku(FXHDD01 assouaturyoku) {
        this.assouaturyoku = assouaturyoku;
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
     * F/P停止日
     * @return the fpteisi_day
     */
    public FXHDD01 getFpteisi_day() {
        return fpteisi_day;
    }

    /**
     * F/P停止日
     * @param fpteisi_day the fpteisi_day to set
     */
    public void setFpteisi_day(FXHDD01 fpteisi_day) {
        this.fpteisi_day = fpteisi_day;
    }

    /**
     * F/P停止時間
     * @return the fpteisi_time
     */
    public FXHDD01 getFpteisi_time() {
        return fpteisi_time;
    }

    /**
     * F/P停止時間
     * @param fpteisi_time the fpteisi_time to set
     */
    public void setFpteisi_time(FXHDD01 fpteisi_time) {
        this.fpteisi_time = fpteisi_time;
    }

    /**
     * F/P交換_ﾌｨﾙﾀｰ連結
     * @return the fpkoukan_filterrenketu
     */
    public FXHDD01 getFpkoukan_filterrenketu() {
        return fpkoukan_filterrenketu;
    }

    /**
     * F/P交換_ﾌｨﾙﾀｰ連結
     * @param fpkoukan_filterrenketu the fpkoukan_filterrenketu to set
     */
    public void setFpkoukan_filterrenketu(FXHDD01 fpkoukan_filterrenketu) {
        this.fpkoukan_filterrenketu = fpkoukan_filterrenketu;
    }

    /**
     * F/P交換_ﾌｨﾙﾀｰ品名①
     * @return the fpkoukan_filterhinmei1
     */
    public FXHDD01 getFpkoukan_filterhinmei1() {
        return fpkoukan_filterhinmei1;
    }

    /**
     * F/P交換_ﾌｨﾙﾀｰ品名①
     * @param fpkoukan_filterhinmei1 the fpkoukan_filterhinmei1 to set
     */
    public void setFpkoukan_filterhinmei1(FXHDD01 fpkoukan_filterhinmei1) {
        this.fpkoukan_filterhinmei1 = fpkoukan_filterhinmei1;
    }

    /**
     * F/P交換_LotNo①
     * @return the fpkoukan_lotno1
     */
    public FXHDD01 getFpkoukan_lotno1() {
        return fpkoukan_lotno1;
    }

    /**
     * F/P交換_LotNo①
     * @param fpkoukan_lotno1 the fpkoukan_lotno1 to set
     */
    public void setFpkoukan_lotno1(FXHDD01 fpkoukan_lotno1) {
        this.fpkoukan_lotno1 = fpkoukan_lotno1;
    }

    /**
     * F/P交換_取り付け本数①
     * @return the fpkoukan_toritukehonsuu1
     */
    public FXHDD01 getFpkoukan_toritukehonsuu1() {
        return fpkoukan_toritukehonsuu1;
    }

    /**
     * F/P交換_取り付け本数①
     * @param fpkoukan_toritukehonsuu1 the fpkoukan_toritukehonsuu1 to set
     */
    public void setFpkoukan_toritukehonsuu1(FXHDD01 fpkoukan_toritukehonsuu1) {
        this.fpkoukan_toritukehonsuu1 = fpkoukan_toritukehonsuu1;
    }

    /**
     * F/P交換_ﾌｨﾙﾀｰ品名②
     * @return the fpkoukan_filterhinmei2
     */
    public FXHDD01 getFpkoukan_filterhinmei2() {
        return fpkoukan_filterhinmei2;
    }

    /**
     * F/P交換_ﾌｨﾙﾀｰ品名②
     * @param fpkoukan_filterhinmei2 the fpkoukan_filterhinmei2 to set
     */
    public void setFpkoukan_filterhinmei2(FXHDD01 fpkoukan_filterhinmei2) {
        this.fpkoukan_filterhinmei2 = fpkoukan_filterhinmei2;
    }

    /**
     * F/P交換_LotNo②
     * @return the fpkoukan_lotno2
     */
    public FXHDD01 getFpkoukan_lotno2() {
        return fpkoukan_lotno2;
    }

    /**
     * F/P交換_LotNo②
     * @param fpkoukan_lotno2 the fpkoukan_lotno2 to set
     */
    public void setFpkoukan_lotno2(FXHDD01 fpkoukan_lotno2) {
        this.fpkoukan_lotno2 = fpkoukan_lotno2;
    }

    /**
     * F/P交換_取り付け本数②
     * @return the fpkoukan_toritukehonsuu2
     */
    public FXHDD01 getFpkoukan_toritukehonsuu2() {
        return fpkoukan_toritukehonsuu2;
    }

    /**
     * F/P交換_取り付け本数②
     * @param fpkoukan_toritukehonsuu2 the fpkoukan_toritukehonsuu2 to set
     */
    public void setFpkoukan_toritukehonsuu2(FXHDD01 fpkoukan_toritukehonsuu2) {
        this.fpkoukan_toritukehonsuu2 = fpkoukan_toritukehonsuu2;
    }

    /**
     * F/P再開日
     * @return the fpsaikai_day
     */
    public FXHDD01 getFpsaikai_day() {
        return fpsaikai_day;
    }

    /**
     * F/P再開日
     * @param fpsaikai_day the fpsaikai_day to set
     */
    public void setFpsaikai_day(FXHDD01 fpsaikai_day) {
        this.fpsaikai_day = fpsaikai_day;
    }

    /**
     * F/P再開時間
     * @return the fpsaikai_time
     */
    public FXHDD01 getFpsaikai_time() {
        return fpsaikai_time;
    }

    /**
     * F/P再開時間
     * @param fpsaikai_time the fpsaikai_time to set
     */
    public void setFpsaikai_time(FXHDD01 fpsaikai_time) {
        this.fpsaikai_time = fpsaikai_time;
    }

    /**
     * F/P交換_担当者
     * @return the fpkoukan_tantousya
     */
    public FXHDD01 getFpkoukan_tantousya() {
        return fpkoukan_tantousya;
    }

    /**
     * F/P交換_担当者
     * @param fpkoukan_tantousya the fpkoukan_tantousya to set
     */
    public void setFpkoukan_tantousya(FXHDD01 fpkoukan_tantousya) {
        this.fpkoukan_tantousya = fpkoukan_tantousya;
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
     * F/P終了_担当者
     * @return the fpsyuurou_tantousya
     */
    public FXHDD01 getFpsyuurou_tantousya() {
        return fpsyuurou_tantousya;
    }

    /**
     * F/P終了_担当者
     * @param fpsyuurou_tantousya the fpsyuurou_tantousya to set
     */
    public void setFpsyuurou_tantousya(FXHDD01 fpsyuurou_tantousya) {
        this.fpsyuurou_tantousya = fpsyuurou_tantousya;
    }

    /**
     * 総重量①
     * @return the soujyuurou1
     */
    public FXHDD01 getSoujyuurou1() {
        return soujyuurou1;
    }

    /**
     * 総重量①
     * @param soujyuurou1 the soujyuurou1 to set
     */
    public void setSoujyuurou1(FXHDD01 soujyuurou1) {
        this.soujyuurou1 = soujyuurou1;
    }

    /**
     * 総重量②
     * @return the soujyuurou2
     */
    public FXHDD01 getSoujyuurou2() {
        return soujyuurou2;
    }

    /**
     * 総重量②
     * @param soujyuurou2 the soujyuurou2 to set
     */
    public void setSoujyuurou2(FXHDD01 soujyuurou2) {
        this.soujyuurou2 = soujyuurou2;
    }

    /**
     * 総重量③
     * @return the soujyuurou3
     */
    public FXHDD01 getSoujyuurou3() {
        return soujyuurou3;
    }

    /**
     * 総重量③
     * @param soujyuurou3 the soujyuurou3 to set
     */
    public void setSoujyuurou3(FXHDD01 soujyuurou3) {
        this.soujyuurou3 = soujyuurou3;
    }

    /**
     * 総重量④
     * @return the soujyuurou4
     */
    public FXHDD01 getSoujyuurou4() {
        return soujyuurou4;
    }

    /**
     * 総重量④
     * @param soujyuurou4 the soujyuurou4 to set
     */
    public void setSoujyuurou4(FXHDD01 soujyuurou4) {
        this.soujyuurou4 = soujyuurou4;
    }

    /**
     * 総重量⑤
     * @return the soujyuurou5
     */
    public FXHDD01 getSoujyuurou5() {
        return soujyuurou5;
    }

    /**
     * 総重量⑤
     * @param soujyuurou5 the soujyuurou5 to set
     */
    public void setSoujyuurou5(FXHDD01 soujyuurou5) {
        this.soujyuurou5 = soujyuurou5;
    }

    /**
     * 総重量⑥
     * @return the soujyuurou6
     */
    public FXHDD01 getSoujyuurou6() {
        return soujyuurou6;
    }

    /**
     * 総重量⑥
     * @param soujyuurou6 the soujyuurou6 to set
     */
    public void setSoujyuurou6(FXHDD01 soujyuurou6) {
        this.soujyuurou6 = soujyuurou6;
    }

    /**
     * 添加材ｽﾗﾘｰ重量①
     * @return the tenkazaislurryjyuuryou1
     */
    public FXHDD01 getTenkazaislurryjyuuryou1() {
        return tenkazaislurryjyuuryou1;
    }

    /**
     * 添加材ｽﾗﾘｰ重量①
     * @param tenkazaislurryjyuuryou1 the tenkazaislurryjyuuryou1 to set
     */
    public void setTenkazaislurryjyuuryou1(FXHDD01 tenkazaislurryjyuuryou1) {
        this.tenkazaislurryjyuuryou1 = tenkazaislurryjyuuryou1;
    }

    /**
     * 添加材ｽﾗﾘｰ重量②
     * @return the tenkazaislurryjyuuryou2
     */
    public FXHDD01 getTenkazaislurryjyuuryou2() {
        return tenkazaislurryjyuuryou2;
    }

    /**
     * 添加材ｽﾗﾘｰ重量②
     * @param tenkazaislurryjyuuryou2 the tenkazaislurryjyuuryou2 to set
     */
    public void setTenkazaislurryjyuuryou2(FXHDD01 tenkazaislurryjyuuryou2) {
        this.tenkazaislurryjyuuryou2 = tenkazaislurryjyuuryou2;
    }

    /**
     * 添加材ｽﾗﾘｰ重量③
     * @return the tenkazaislurryjyuuryou3
     */
    public FXHDD01 getTenkazaislurryjyuuryou3() {
        return tenkazaislurryjyuuryou3;
    }

    /**
     * 添加材ｽﾗﾘｰ重量③
     * @param tenkazaislurryjyuuryou3 the tenkazaislurryjyuuryou3 to set
     */
    public void setTenkazaislurryjyuuryou3(FXHDD01 tenkazaislurryjyuuryou3) {
        this.tenkazaislurryjyuuryou3 = tenkazaislurryjyuuryou3;
    }

    /**
     * 添加材ｽﾗﾘｰ重量④
     * @return the tenkazaislurryjyuuryou4
     */
    public FXHDD01 getTenkazaislurryjyuuryou4() {
        return tenkazaislurryjyuuryou4;
    }

    /**
     * 添加材ｽﾗﾘｰ重量④
     * @param tenkazaislurryjyuuryou4 the tenkazaislurryjyuuryou4 to set
     */
    public void setTenkazaislurryjyuuryou4(FXHDD01 tenkazaislurryjyuuryou4) {
        this.tenkazaislurryjyuuryou4 = tenkazaislurryjyuuryou4;
    }

    /**
     * 添加材ｽﾗﾘｰ重量⑤
     * @return the tenkazaislurryjyuuryou5
     */
    public FXHDD01 getTenkazaislurryjyuuryou5() {
        return tenkazaislurryjyuuryou5;
    }

    /**
     * 添加材ｽﾗﾘｰ重量⑤
     * @param tenkazaislurryjyuuryou5 the tenkazaislurryjyuuryou5 to set
     */
    public void setTenkazaislurryjyuuryou5(FXHDD01 tenkazaislurryjyuuryou5) {
        this.tenkazaislurryjyuuryou5 = tenkazaislurryjyuuryou5;
    }

    /**
     * 添加材ｽﾗﾘｰ重量⑥
     * @return the tenkazaislurryjyuuryou6
     */
    public FXHDD01 getTenkazaislurryjyuuryou6() {
        return tenkazaislurryjyuuryou6;
    }

    /**
     * 添加材ｽﾗﾘｰ重量⑥
     * @param tenkazaislurryjyuuryou6 the tenkazaislurryjyuuryou6 to set
     */
    public void setTenkazaislurryjyuuryou6(FXHDD01 tenkazaislurryjyuuryou6) {
        this.tenkazaislurryjyuuryou6 = tenkazaislurryjyuuryou6;
    }

    /**
     * 添加材ｽﾗﾘｰ重量合計
     * @return the tenkazaislurryjyuuryougoukei
     */
    public FXHDD01 getTenkazaislurryjyuuryougoukei() {
        return tenkazaislurryjyuuryougoukei;
    }

    /**
     * 添加材ｽﾗﾘｰ重量合計
     * @param tenkazaislurryjyuuryougoukei the tenkazaislurryjyuuryougoukei to set
     */
    public void setTenkazaislurryjyuuryougoukei(FXHDD01 tenkazaislurryjyuuryougoukei) {
        this.tenkazaislurryjyuuryougoukei = tenkazaislurryjyuuryougoukei;
    }

    /**
     * 歩留まり
     * @return the budomari
     */
    public FXHDD01 getBudomari() {
        return budomari;
    }

    /**
     * 歩留まり
     * @param budomari the budomari to set
     */
    public void setBudomari(FXHDD01 budomari) {
        this.budomari = budomari;
    }

    /**
     * 添加材ｽﾗﾘｰ有効期限
     * @return the tenkazaislurryyuukoukigen
     */
    public FXHDD01 getTenkazaislurryyuukoukigen() {
        return tenkazaislurryyuukoukigen;
    }

    /**
     * 添加材ｽﾗﾘｰ有効期限
     * @param tenkazaislurryyuukoukigen the tenkazaislurryyuukoukigen to set
     */
    public void setTenkazaislurryyuukoukigen(FXHDD01 tenkazaislurryyuukoukigen) {
        this.tenkazaislurryyuukoukigen = tenkazaislurryyuukoukigen;
    }

    /**
     * 粉砕判定
     * @return the funsaihantei
     */
    public FXHDD01 getFunsaihantei() {
        return funsaihantei;
    }

    /**
     * 粉砕判定
     * @param funsaihantei the funsaihantei to set
     */
    public void setFunsaihantei(FXHDD01 funsaihantei) {
        this.funsaihantei = funsaihantei;
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
}