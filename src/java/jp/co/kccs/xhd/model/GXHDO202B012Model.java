/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日       2021/10/15<br>
 * 計画書No     MB2101-DK002<br>
 * 変更者       KCSS K.Jo<br>
 * 変更理由     新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 添加材ｽﾗﾘｰ作製・FP排出履歴検索画面のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/10/15
 */
public class GXHDO202B012Model implements Serializable {
    /** WIPﾛｯﾄNo */
    private String lotno = "";

    /** 添加材ｽﾗﾘｰ品名 */
    private String tenkazaislurryhinmei = "";

    /** 添加材ｽﾗﾘｰLotNo */
    private String tenkazaislurrylotno = "";

    /** ﾛｯﾄ区分 */
    private String lotkubun = "";

    /** 最終パス回数 */
    private Integer saisyuupasskaisuu = null;

    /** 保存用ｻﾝﾌﾟﾙ回収 */
    private String hozonyousamplekaisyuu = "";

    /** 風袋重量① */
    private Integer fuutaijyuuryou1 = null;

    /** 風袋重量② */
    private Integer fuutaijyuuryou2 = null;

    /** 風袋重量③ */
    private Integer fuutaijyuuryou3 = null;

    /** 風袋重量④ */
    private Integer fuutaijyuuryou4 = null;

    /** 風袋重量⑤ */
    private Integer fuutaijyuuryou5 = null;

    /** 風袋重量⑥ */
    private Integer fuutaijyuuryou6 = null;

    /** F/P準備_担当者 */
    private String FPjyunbi_tantousya = "";

    /** F/P準備_ﾌｨﾙﾀｰ連結 */
    private String FPjyunbi_filterrenketu = "";

    /** F/P準備_ﾌｨﾙﾀｰ品名① */
    private String FPjyunbi_filterhinmei1 = "";

    /** F/P準備_LotNo① */
    private String FPjyunbi_lotno1 = "";

    /** F/P準備_取り付け本数① */
    private Integer FPjyunbi_toritukehonsuu1 = null;

    /** F/P準備_ﾌｨﾙﾀｰ品名① */
    private String FPjyunbi_filterhinmei2 = "";

    /** F/P準備_LotNo② */
    private String FPjyunbi_lotno2 = "";

    /** F/P準備_取り付け本数② */
    private Integer FPjyunbi_toritukehonsuu2 = null;

    /** 排出容器の内袋 */
    private String haisyutuyoukinoutibukuro = "";

    /** ﾌｨﾙﾀｰ使用回数 */
    private String filterkaisuu = "";

    /** F/PﾀﾝｸNo */
    private Integer FPtankno = null;

    /** 洗浄確認 */
    private String senjyoukakunin = "";

    /** F/P開始日時 */
    private Timestamp FPkaisinichiji = null;

    /** 圧送ﾚｷﾞｭﾚｰﾀｰNo */
    private String assouregulatorNo = "";

    /** 圧送圧力 */
    private BigDecimal assouaturyoku = null;

    /** F/P開始_担当者 */
    private String FPkaisi_tantousya = "";

    /** F/P停止日時 */
    private Timestamp FPteisinichiji = null;

    /** F/P交換_ﾌｨﾙﾀｰ連結 */
    private String FPkoukan_filterrenketu = "";

    /** F/P交換_ﾌｨﾙﾀｰ品名① */
    private String FPkoukan_filterhinmei1 = "";

    /** F/P交換_LotNo① */
    private String FPkoukan_lotno1 = "";

    /** F/P交換_取り付け本数① */
    private Integer FPkoukan_toritukehonsuu1 = null;

    /** F/P交換_ﾌｨﾙﾀｰ品名① */
    private String FPkoukan_filterhinmei2 = "";

    /** F/P交換_LotNo② */
    private String FPkoukan_lotno2 = "";

    /** F/P交換_取り付け本数② */
    private Integer FPkoukan_toritukehonsuu2 = null;

    /** F/P再開日時 */
    private Timestamp FPsaikainichiji = null;

    /** F/P交換_担当者 */
    private String FPkoukan_tantousya = "";

    /** F/P終了日時 */
    private Timestamp FPsyuuryounichiji = null;

    /** F/P時間 */
    private String FPjikan = "";

    /** F/P終了_担当者 */
    private String FPsyuurou_tantousya = "";

    /** 総重量① */
    private Integer soujyuurou1 = null;

    /** 総重量② */
    private Integer soujyuurou2 = null;

    /** 総重量③ */
    private Integer soujyuurou3 = null;

    /** 総重量④ */
    private Integer soujyuurou4 = null;

    /** 総重量⑤ */
    private Integer soujyuurou5 = null;

    /** 総重量⑥ */
    private Integer soujyuurou6 = null;

    /** 添加材ｽﾗﾘｰ重量① */
    private Integer tenkazaislurryjyuuryou1 = null;

    /** 添加材ｽﾗﾘｰ重量② */
    private Integer tenkazaislurryjyuuryou2 = null;

    /** 添加材ｽﾗﾘｰ重量③ */
    private Integer tenkazaislurryjyuuryou3 = null;

    /** 添加材ｽﾗﾘｰ重量④ */
    private Integer tenkazaislurryjyuuryou4 = null;

    /** 添加材ｽﾗﾘｰ重量⑤ */
    private Integer tenkazaislurryjyuuryou5 = null;

    /** 添加材ｽﾗﾘｰ重量⑥ */
    private Integer tenkazaislurryjyuuryou6 = null;

    /** 添加材ｽﾗﾘｰ重量合計 */
    private Integer tenkazaislurryjyuuryougoukei = null;

    /** 歩留まり */
    private BigDecimal budomari = null;

    /** 添加材ｽﾗﾘｰ有効期限 */
    private String tenkazaislurryyuukoukigen = "";

    /** 粉砕判定 */
    private String funsaihantei = "";

    /** 担当者 */
    private String tantousya = "";

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
     * 添加材ｽﾗﾘｰ品名
     * @return the tenkazaislurryhinmei
     */
    public String getTenkazaislurryhinmei() {
        return tenkazaislurryhinmei;
    }

    /**
     * 添加材ｽﾗﾘｰ品名
     * @param tenkazaislurryhinmei the tenkazaislurryhinmei to set
     */
    public void setTenkazaislurryhinmei(String tenkazaislurryhinmei) {
        this.tenkazaislurryhinmei = tenkazaislurryhinmei;
    }

    /**
     * 添加材ｽﾗﾘｰLotNo
     * @return the tenkazaislurrylotno
     */
    public String getTenkazaislurrylotno() {
        return tenkazaislurrylotno;
    }

    /**
     * 添加材ｽﾗﾘｰLotNo
     * @param tenkazaislurrylotno the tenkazaislurrylotno to set
     */
    public void setTenkazaislurrylotno(String tenkazaislurrylotno) {
        this.tenkazaislurrylotno = tenkazaislurrylotno;
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
     * 最終パス回数
     * @return the saisyuupasskaisuu
     */
    public Integer getSaisyuupasskaisuu() {
        return saisyuupasskaisuu;
    }

    /**
     * 最終パス回数
     * @param saisyuupasskaisuu the saisyuupasskaisuu to set
     */
    public void setSaisyuupasskaisuu(Integer saisyuupasskaisuu) {
        this.saisyuupasskaisuu = saisyuupasskaisuu;
    }

    /**
     * 保存用ｻﾝﾌﾟﾙ回収
     * @return the hozonyousamplekaisyuu
     */
    public String getHozonyousamplekaisyuu() {
        return hozonyousamplekaisyuu;
    }

    /**
     * 保存用ｻﾝﾌﾟﾙ回収
     * @param hozonyousamplekaisyuu the hozonyousamplekaisyuu to set
     */
    public void setHozonyousamplekaisyuu(String hozonyousamplekaisyuu) {
        this.hozonyousamplekaisyuu = hozonyousamplekaisyuu;
    }

    /**
     * 風袋重量①
     * @return the fuutaijyuuryou1
     */
    public Integer getFuutaijyuuryou1() {
        return fuutaijyuuryou1;
    }

    /**
     * 風袋重量①
     * @param fuutaijyuuryou1 the fuutaijyuuryou1 to set
     */
    public void setFuutaijyuuryou1(Integer fuutaijyuuryou1) {
        this.fuutaijyuuryou1 = fuutaijyuuryou1;
    }

    /**
     * 風袋重量②
     * @return the fuutaijyuuryou2
     */
    public Integer getFuutaijyuuryou2() {
        return fuutaijyuuryou2;
    }

    /**
     * 風袋重量②
     * @param fuutaijyuuryou2 the fuutaijyuuryou2 to set
     */
    public void setFuutaijyuuryou2(Integer fuutaijyuuryou2) {
        this.fuutaijyuuryou2 = fuutaijyuuryou2;
    }

    /**
     * 風袋重量③
     * @return the fuutaijyuuryou3
     */
    public Integer getFuutaijyuuryou3() {
        return fuutaijyuuryou3;
    }

    /**
     * 風袋重量③
     * @param fuutaijyuuryou3 the fuutaijyuuryou3 to set
     */
    public void setFuutaijyuuryou3(Integer fuutaijyuuryou3) {
        this.fuutaijyuuryou3 = fuutaijyuuryou3;
    }

    /**
     * 風袋重量④
     * @return the fuutaijyuuryou4
     */
    public Integer getFuutaijyuuryou4() {
        return fuutaijyuuryou4;
    }

    /**
     * 風袋重量④
     * @param fuutaijyuuryou4 the fuutaijyuuryou4 to set
     */
    public void setFuutaijyuuryou4(Integer fuutaijyuuryou4) {
        this.fuutaijyuuryou4 = fuutaijyuuryou4;
    }

    /**
     * 風袋重量⑤
     * @return the fuutaijyuuryou5
     */
    public Integer getFuutaijyuuryou5() {
        return fuutaijyuuryou5;
    }

    /**
     * 風袋重量⑤
     * @param fuutaijyuuryou5 the fuutaijyuuryou5 to set
     */
    public void setFuutaijyuuryou5(Integer fuutaijyuuryou5) {
        this.fuutaijyuuryou5 = fuutaijyuuryou5;
    }

    /**
     * 風袋重量⑥
     * @return the fuutaijyuuryou6
     */
    public Integer getFuutaijyuuryou6() {
        return fuutaijyuuryou6;
    }

    /**
     * 風袋重量⑥
     * @param fuutaijyuuryou6 the fuutaijyuuryou6 to set
     */
    public void setFuutaijyuuryou6(Integer fuutaijyuuryou6) {
        this.fuutaijyuuryou6 = fuutaijyuuryou6;
    }

    /**
     * F/P準備_担当者
     * @return the FPjyunbi_tantousya
     */
    public String getFPjyunbi_tantousya() {
        return FPjyunbi_tantousya;
    }

    /**
     * F/P準備_担当者
     * @param FPjyunbi_tantousya the FPjyunbi_tantousya to set
     */
    public void setFPjyunbi_tantousya(String FPjyunbi_tantousya) {
        this.FPjyunbi_tantousya = FPjyunbi_tantousya;
    }

    /**
     * F/P準備_ﾌｨﾙﾀｰ連結
     * @return the FPjyunbi_filterrenketu
     */
    public String getFPjyunbi_filterrenketu() {
        return FPjyunbi_filterrenketu;
    }

    /**
     * F/P準備_ﾌｨﾙﾀｰ連結
     * @param FPjyunbi_filterrenketu the FPjyunbi_filterrenketu to set
     */
    public void setFPjyunbi_filterrenketu(String FPjyunbi_filterrenketu) {
        this.FPjyunbi_filterrenketu = FPjyunbi_filterrenketu;
    }

    /**
     * F/P準備_ﾌｨﾙﾀｰ品名①
     * @return the FPjyunbi_filterhinmei1
     */
    public String getFPjyunbi_filterhinmei1() {
        return FPjyunbi_filterhinmei1;
    }

    /**
     * F/P準備_ﾌｨﾙﾀｰ品名①
     * @param FPjyunbi_filterhinmei1 the FPjyunbi_filterhinmei1 to set
     */
    public void setFPjyunbi_filterhinmei1(String FPjyunbi_filterhinmei1) {
        this.FPjyunbi_filterhinmei1 = FPjyunbi_filterhinmei1;
    }

    /**
     * F/P準備_LotNo①
     * @return the FPjyunbi_lotno1
     */
    public String getFPjyunbi_lotno1() {
        return FPjyunbi_lotno1;
    }

    /**
     * F/P準備_LotNo①
     * @param FPjyunbi_lotno1 the FPjyunbi_lotno1 to set
     */
    public void setFPjyunbi_lotno1(String FPjyunbi_lotno1) {
        this.FPjyunbi_lotno1 = FPjyunbi_lotno1;
    }

    /**
     * F/P準備_取り付け本数①
     * @return the FPjyunbi_toritukehonsuu1
     */
    public Integer getFPjyunbi_toritukehonsuu1() {
        return FPjyunbi_toritukehonsuu1;
    }

    /**
     * F/P準備_取り付け本数①
     * @param FPjyunbi_toritukehonsuu1 the FPjyunbi_toritukehonsuu1 to set
     */
    public void setFPjyunbi_toritukehonsuu1(Integer FPjyunbi_toritukehonsuu1) {
        this.FPjyunbi_toritukehonsuu1 = FPjyunbi_toritukehonsuu1;
    }

    /**
     * F/P準備_ﾌｨﾙﾀｰ品名①
     * @return the FPjyunbi_filterhinmei2
     */
    public String getFPjyunbi_filterhinmei2() {
        return FPjyunbi_filterhinmei2;
    }

    /**
     * F/P準備_ﾌｨﾙﾀｰ品名①
     * @param FPjyunbi_filterhinmei2 the FPjyunbi_filterhinmei2 to set
     */
    public void setFPjyunbi_filterhinmei2(String FPjyunbi_filterhinmei2) {
        this.FPjyunbi_filterhinmei2 = FPjyunbi_filterhinmei2;
    }

    /**
     * F/P準備_LotNo②
     * @return the FPjyunbi_lotno2
     */
    public String getFPjyunbi_lotno2() {
        return FPjyunbi_lotno2;
    }

    /**
     * F/P準備_LotNo②
     * @param FPjyunbi_lotno2 the FPjyunbi_lotno2 to set
     */
    public void setFPjyunbi_lotno2(String FPjyunbi_lotno2) {
        this.FPjyunbi_lotno2 = FPjyunbi_lotno2;
    }

    /**
     * F/P準備_取り付け本数②
     * @return the FPjyunbi_toritukehonsuu2
     */
    public Integer getFPjyunbi_toritukehonsuu2() {
        return FPjyunbi_toritukehonsuu2;
    }

    /**
     * F/P準備_取り付け本数②
     * @param FPjyunbi_toritukehonsuu2 the FPjyunbi_toritukehonsuu2 to set
     */
    public void setFPjyunbi_toritukehonsuu2(Integer FPjyunbi_toritukehonsuu2) {
        this.FPjyunbi_toritukehonsuu2 = FPjyunbi_toritukehonsuu2;
    }

    /**
     * 排出容器の内袋
     * @return the haisyutuyoukinoutibukuro
     */
    public String getHaisyutuyoukinoutibukuro() {
        return haisyutuyoukinoutibukuro;
    }

    /**
     * 排出容器の内袋
     * @param haisyutuyoukinoutibukuro the haisyutuyoukinoutibukuro to set
     */
    public void setHaisyutuyoukinoutibukuro(String haisyutuyoukinoutibukuro) {
        this.haisyutuyoukinoutibukuro = haisyutuyoukinoutibukuro;
    }

    /**
     * ﾌｨﾙﾀｰ使用回数
     * @return the filterkaisuu
     */
    public String getFilterkaisuu() {
        return filterkaisuu;
    }

    /**
     * ﾌｨﾙﾀｰ使用回数
     * @param filterkaisuu the filterkaisuu to set
     */
    public void setFilterkaisuu(String filterkaisuu) {
        this.filterkaisuu = filterkaisuu;
    }

    /**
     * F/PﾀﾝｸNo
     * @return the FPtankno
     */
    public Integer getFPtankno() {
        return FPtankno;
    }

    /**
     * F/PﾀﾝｸNo
     * @param FPtankno the FPtankno to set
     */
    public void setFPtankno(Integer FPtankno) {
        this.FPtankno = FPtankno;
    }

    /**
     * 洗浄確認
     * @return the senjyoukakunin
     */
    public String getSenjyoukakunin() {
        return senjyoukakunin;
    }

    /**
     * 洗浄確認
     * @param senjyoukakunin the senjyoukakunin to set
     */
    public void setSenjyoukakunin(String senjyoukakunin) {
        this.senjyoukakunin = senjyoukakunin;
    }

    /**
     * F/P開始日時
     * @return the FPkaisinichiji
     */
    public Timestamp getFPkaisinichiji() {
        return FPkaisinichiji;
    }

    /**
     * F/P開始日時
     * @param FPkaisinichiji the FPkaisinichiji to set
     */
    public void setFPkaisinichiji(Timestamp FPkaisinichiji) {
        this.FPkaisinichiji = FPkaisinichiji;
    }

    /**
     * 圧送ﾚｷﾞｭﾚｰﾀｰNo
     * @return the assouregulatorNo
     */
    public String getAssouregulatorNo() {
        return assouregulatorNo;
    }

    /**
     * 圧送ﾚｷﾞｭﾚｰﾀｰNo
     * @param assouregulatorNo the assouregulatorNo to set
     */
    public void setAssouregulatorNo(String assouregulatorNo) {
        this.assouregulatorNo = assouregulatorNo;
    }

    /**
     * 圧送圧力
     * @return the assouaturyoku
     */
    public BigDecimal getAssouaturyoku() {
        return assouaturyoku;
    }

    /**
     * 圧送圧力
     * @param assouaturyoku the assouaturyoku to set
     */
    public void setAssouaturyoku(BigDecimal assouaturyoku) {
        this.assouaturyoku = assouaturyoku;
    }

    /**
     * F/P開始_担当者
     * @return the FPkaisi_tantousya
     */
    public String getFPkaisi_tantousya() {
        return FPkaisi_tantousya;
    }

    /**
     * F/P開始_担当者
     * @param FPkaisi_tantousya the FPkaisi_tantousya to set
     */
    public void setFPkaisi_tantousya(String FPkaisi_tantousya) {
        this.FPkaisi_tantousya = FPkaisi_tantousya;
    }

    /**
     * F/P停止日時
     * @return the FPteisinichiji
     */
    public Timestamp getFPteisinichiji() {
        return FPteisinichiji;
    }

    /**
     * F/P停止日時
     * @param FPteisinichiji the FPteisinichiji to set
     */
    public void setFPteisinichiji(Timestamp FPteisinichiji) {
        this.FPteisinichiji = FPteisinichiji;
    }

    /**
     * F/P交換_ﾌｨﾙﾀｰ連結
     * @return the FPkoukan_filterrenketu
     */
    public String getFPkoukan_filterrenketu() {
        return FPkoukan_filterrenketu;
    }

    /**
     * F/P交換_ﾌｨﾙﾀｰ連結
     * @param FPkoukan_filterrenketu the FPkoukan_filterrenketu to set
     */
    public void setFPkoukan_filterrenketu(String FPkoukan_filterrenketu) {
        this.FPkoukan_filterrenketu = FPkoukan_filterrenketu;
    }

    /**
     * F/P交換_ﾌｨﾙﾀｰ品名①
     * @return the FPkoukan_filterhinmei1
     */
    public String getFPkoukan_filterhinmei1() {
        return FPkoukan_filterhinmei1;
    }

    /**
     * F/P交換_ﾌｨﾙﾀｰ品名①
     * @param FPkoukan_filterhinmei1 the FPkoukan_filterhinmei1 to set
     */
    public void setFPkoukan_filterhinmei1(String FPkoukan_filterhinmei1) {
        this.FPkoukan_filterhinmei1 = FPkoukan_filterhinmei1;
    }

    /**
     * F/P交換_LotNo①
     * @return the FPkoukan_lotno1
     */
    public String getFPkoukan_lotno1() {
        return FPkoukan_lotno1;
    }

    /**
     * F/P交換_LotNo①
     * @param FPkoukan_lotno1 the FPkoukan_lotno1 to set
     */
    public void setFPkoukan_lotno1(String FPkoukan_lotno1) {
        this.FPkoukan_lotno1 = FPkoukan_lotno1;
    }

    /**
     * F/P交換_取り付け本数①
     * @return the FPkoukan_toritukehonsuu1
     */
    public Integer getFPkoukan_toritukehonsuu1() {
        return FPkoukan_toritukehonsuu1;
    }

    /**
     * F/P交換_取り付け本数①
     * @param FPkoukan_toritukehonsuu1 the FPkoukan_toritukehonsuu1 to set
     */
    public void setFPkoukan_toritukehonsuu1(Integer FPkoukan_toritukehonsuu1) {
        this.FPkoukan_toritukehonsuu1 = FPkoukan_toritukehonsuu1;
    }

    /**
     * F/P交換_ﾌｨﾙﾀｰ品名①
     * @return the FPkoukan_filterhinmei2
     */
    public String getFPkoukan_filterhinmei2() {
        return FPkoukan_filterhinmei2;
    }

    /**
     * F/P交換_ﾌｨﾙﾀｰ品名①
     * @param FPkoukan_filterhinmei2 the FPkoukan_filterhinmei2 to set
     */
    public void setFPkoukan_filterhinmei2(String FPkoukan_filterhinmei2) {
        this.FPkoukan_filterhinmei2 = FPkoukan_filterhinmei2;
    }

    /**
     * F/P交換_LotNo②
     * @return the FPkoukan_lotno2
     */
    public String getFPkoukan_lotno2() {
        return FPkoukan_lotno2;
    }

    /**
     * F/P交換_LotNo②
     * @param FPkoukan_lotno2 the FPkoukan_lotno2 to set
     */
    public void setFPkoukan_lotno2(String FPkoukan_lotno2) {
        this.FPkoukan_lotno2 = FPkoukan_lotno2;
    }

    /**
     * F/P交換_取り付け本数②
     * @return the FPkoukan_toritukehonsuu2
     */
    public Integer getFPkoukan_toritukehonsuu2() {
        return FPkoukan_toritukehonsuu2;
    }

    /**
     * F/P交換_取り付け本数②
     * @param FPkoukan_toritukehonsuu2 the FPkoukan_toritukehonsuu2 to set
     */
    public void setFPkoukan_toritukehonsuu2(Integer FPkoukan_toritukehonsuu2) {
        this.FPkoukan_toritukehonsuu2 = FPkoukan_toritukehonsuu2;
    }

    /**
     * F/P再開日時
     * @return the FPsaikainichiji
     */
    public Timestamp getFPsaikainichiji() {
        return FPsaikainichiji;
    }

    /**
     * F/P再開日時
     * @param FPsaikainichiji the FPsaikainichiji to set
     */
    public void setFPsaikainichiji(Timestamp FPsaikainichiji) {
        this.FPsaikainichiji = FPsaikainichiji;
    }

    /**
     * F/P交換_担当者
     * @return the FPkoukan_tantousya
     */
    public String getFPkoukan_tantousya() {
        return FPkoukan_tantousya;
    }

    /**
     * F/P交換_担当者
     * @param FPkoukan_tantousya the FPkoukan_tantousya to set
     */
    public void setFPkoukan_tantousya(String FPkoukan_tantousya) {
        this.FPkoukan_tantousya = FPkoukan_tantousya;
    }

    /**
     * F/P終了日時
     * @return the FPsyuuryounichiji
     */
    public Timestamp getFPsyuuryounichiji() {
        return FPsyuuryounichiji;
    }

    /**
     * F/P終了日時
     * @param FPsyuuryounichiji the FPsyuuryounichiji to set
     */
    public void setFPsyuuryounichiji(Timestamp FPsyuuryounichiji) {
        this.FPsyuuryounichiji = FPsyuuryounichiji;
    }

    /**
     * F/P時間
     * @return the FPjikan
     */
    public String getFPjikan() {
        return FPjikan;
    }

    /**
     * F/P時間
     * @param FPjikan the FPjikan to set
     */
    public void setFPjikan(String FPjikan) {
        this.FPjikan = FPjikan;
    }

    /**
     * F/P終了_担当者
     * @return the FPsyuurou_tantousya
     */
    public String getFPsyuurou_tantousya() {
        return FPsyuurou_tantousya;
    }

    /**
     * F/P終了_担当者
     * @param FPsyuurou_tantousya the FPsyuurou_tantousya to set
     */
    public void setFPsyuurou_tantousya(String FPsyuurou_tantousya) {
        this.FPsyuurou_tantousya = FPsyuurou_tantousya;
    }

    /**
     * 総重量①
     * @return the soujyuurou1
     */
    public Integer getSoujyuurou1() {
        return soujyuurou1;
    }

    /**
     * 総重量①
     * @param soujyuurou1 the soujyuurou1 to set
     */
    public void setSoujyuurou1(Integer soujyuurou1) {
        this.soujyuurou1 = soujyuurou1;
    }

    /**
     * 総重量②
     * @return the soujyuurou2
     */
    public Integer getSoujyuurou2() {
        return soujyuurou2;
    }

    /**
     * 総重量②
     * @param soujyuurou2 the soujyuurou2 to set
     */
    public void setSoujyuurou2(Integer soujyuurou2) {
        this.soujyuurou2 = soujyuurou2;
    }

    /**
     * 総重量③
     * @return the soujyuurou3
     */
    public Integer getSoujyuurou3() {
        return soujyuurou3;
    }

    /**
     * 総重量③
     * @param soujyuurou3 the soujyuurou3 to set
     */
    public void setSoujyuurou3(Integer soujyuurou3) {
        this.soujyuurou3 = soujyuurou3;
    }

    /**
     * 総重量④
     * @return the soujyuurou4
     */
    public Integer getSoujyuurou4() {
        return soujyuurou4;
    }

    /**
     * 総重量④
     * @param soujyuurou4 the soujyuurou4 to set
     */
    public void setSoujyuurou4(Integer soujyuurou4) {
        this.soujyuurou4 = soujyuurou4;
    }

    /**
     * 総重量⑤
     * @return the soujyuurou5
     */
    public Integer getSoujyuurou5() {
        return soujyuurou5;
    }

    /**
     * 総重量⑤
     * @param soujyuurou5 the soujyuurou5 to set
     */
    public void setSoujyuurou5(Integer soujyuurou5) {
        this.soujyuurou5 = soujyuurou5;
    }

    /**
     * 総重量⑥
     * @return the soujyuurou6
     */
    public Integer getSoujyuurou6() {
        return soujyuurou6;
    }

    /**
     * 総重量⑥
     * @param soujyuurou6 the soujyuurou6 to set
     */
    public void setSoujyuurou6(Integer soujyuurou6) {
        this.soujyuurou6 = soujyuurou6;
    }

    /**
     * 添加材ｽﾗﾘｰ重量①
     * @return the tenkazaislurryjyuuryou1
     */
    public Integer getTenkazaislurryjyuuryou1() {
        return tenkazaislurryjyuuryou1;
    }

    /**
     * 添加材ｽﾗﾘｰ重量①
     * @param tenkazaislurryjyuuryou1 the tenkazaislurryjyuuryou1 to set
     */
    public void setTenkazaislurryjyuuryou1(Integer tenkazaislurryjyuuryou1) {
        this.tenkazaislurryjyuuryou1 = tenkazaislurryjyuuryou1;
    }

    /**
     * 添加材ｽﾗﾘｰ重量②
     * @return the tenkazaislurryjyuuryou2
     */
    public Integer getTenkazaislurryjyuuryou2() {
        return tenkazaislurryjyuuryou2;
    }

    /**
     * 添加材ｽﾗﾘｰ重量②
     * @param tenkazaislurryjyuuryou2 the tenkazaislurryjyuuryou2 to set
     */
    public void setTenkazaislurryjyuuryou2(Integer tenkazaislurryjyuuryou2) {
        this.tenkazaislurryjyuuryou2 = tenkazaislurryjyuuryou2;
    }

    /**
     * 添加材ｽﾗﾘｰ重量③
     * @return the tenkazaislurryjyuuryou3
     */
    public Integer getTenkazaislurryjyuuryou3() {
        return tenkazaislurryjyuuryou3;
    }

    /**
     * 添加材ｽﾗﾘｰ重量③
     * @param tenkazaislurryjyuuryou3 the tenkazaislurryjyuuryou3 to set
     */
    public void setTenkazaislurryjyuuryou3(Integer tenkazaislurryjyuuryou3) {
        this.tenkazaislurryjyuuryou3 = tenkazaislurryjyuuryou3;
    }

    /**
     * 添加材ｽﾗﾘｰ重量④
     * @return the tenkazaislurryjyuuryou4
     */
    public Integer getTenkazaislurryjyuuryou4() {
        return tenkazaislurryjyuuryou4;
    }

    /**
     * 添加材ｽﾗﾘｰ重量④
     * @param tenkazaislurryjyuuryou4 the tenkazaislurryjyuuryou4 to set
     */
    public void setTenkazaislurryjyuuryou4(Integer tenkazaislurryjyuuryou4) {
        this.tenkazaislurryjyuuryou4 = tenkazaislurryjyuuryou4;
    }

    /**
     * 添加材ｽﾗﾘｰ重量⑤
     * @return the tenkazaislurryjyuuryou5
     */
    public Integer getTenkazaislurryjyuuryou5() {
        return tenkazaislurryjyuuryou5;
    }

    /**
     * 添加材ｽﾗﾘｰ重量⑤
     * @param tenkazaislurryjyuuryou5 the tenkazaislurryjyuuryou5 to set
     */
    public void setTenkazaislurryjyuuryou5(Integer tenkazaislurryjyuuryou5) {
        this.tenkazaislurryjyuuryou5 = tenkazaislurryjyuuryou5;
    }

    /**
     * 添加材ｽﾗﾘｰ重量⑥
     * @return the tenkazaislurryjyuuryou6
     */
    public Integer getTenkazaislurryjyuuryou6() {
        return tenkazaislurryjyuuryou6;
    }

    /**
     * 添加材ｽﾗﾘｰ重量⑥
     * @param tenkazaislurryjyuuryou6 the tenkazaislurryjyuuryou6 to set
     */
    public void setTenkazaislurryjyuuryou6(Integer tenkazaislurryjyuuryou6) {
        this.tenkazaislurryjyuuryou6 = tenkazaislurryjyuuryou6;
    }

    /**
     * 添加材ｽﾗﾘｰ重量合計
     * @return the tenkazaislurryjyuuryougoukei
     */
    public Integer getTenkazaislurryjyuuryougoukei() {
        return tenkazaislurryjyuuryougoukei;
    }

    /**
     * 添加材ｽﾗﾘｰ重量合計
     * @param tenkazaislurryjyuuryougoukei the tenkazaislurryjyuuryougoukei to set
     */
    public void setTenkazaislurryjyuuryougoukei(Integer tenkazaislurryjyuuryougoukei) {
        this.tenkazaislurryjyuuryougoukei = tenkazaislurryjyuuryougoukei;
    }

    /**
     * 歩留まり
     * @return the budomari
     */
    public BigDecimal getBudomari() {
        return budomari;
    }

    /**
     * 歩留まり
     * @param budomari the budomari to set
     */
    public void setBudomari(BigDecimal budomari) {
        this.budomari = budomari;
    }

    /**
     * 添加材ｽﾗﾘｰ有効期限
     * @return the tenkazaislurryyuukoukigen
     */
    public String getTenkazaislurryyuukoukigen() {
        return tenkazaislurryyuukoukigen;
    }

    /**
     * 添加材ｽﾗﾘｰ有効期限
     * @param tenkazaislurryyuukoukigen the tenkazaislurryyuukoukigen to set
     */
    public void setTenkazaislurryyuukoukigen(String tenkazaislurryyuukoukigen) {
        this.tenkazaislurryyuukoukigen = tenkazaislurryyuukoukigen;
    }

    /**
     * 粉砕判定
     * @return the funsaihantei
     */
    public String getFunsaihantei() {
        return funsaihantei;
    }

    /**
     * 粉砕判定
     * @param funsaihantei the funsaihantei to set
     */
    public void setFunsaihantei(String funsaihantei) {
        this.funsaihantei = funsaihantei;
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

}