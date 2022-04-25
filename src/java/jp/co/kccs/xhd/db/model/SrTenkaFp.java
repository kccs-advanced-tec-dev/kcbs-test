/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/10/22<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SR_TENKA_FP(添加材ｽﾗﾘｰ作製・FP排出)のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/10/22
 */
public class SrTenkaFp {
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
     * 添加材ｽﾗﾘｰ品名
     */
    private String tenkazaislurryhinmei;

    /**
     * 添加材ｽﾗﾘｰLotNo
     */
    private String tenkazaislurrylotno;

    /**
     * ﾛｯﾄ区分
     */
    private String lotkubun;

    /**
     * 投入量
     */
    private String tounyuuryou;

    /**
     * 最終パス回数
     */
    private Integer saisyuupasskaisuu;

    /**
     * 保存用ｻﾝﾌﾟﾙ回収
     */
    private Integer hozonyousamplekaisyuu;

    /**
     * 風袋重量①
     */
    private Integer fuutaijyuuryou1;

    /**
     * 風袋重量②
     */
    private Integer fuutaijyuuryou2;

    /**
     * 風袋重量③
     */
    private Integer fuutaijyuuryou3;

    /**
     * 風袋重量④
     */
    private Integer fuutaijyuuryou4;

    /**
     * 風袋重量⑤
     */
    private Integer fuutaijyuuryou5;

    /**
     * 風袋重量⑥
     */
    private Integer fuutaijyuuryou6;

    /**
     * F/P準備_担当者
     */
    private String fpjyunbi_tantousya;

    /**
     * F/P準備_ﾌｨﾙﾀｰ連結
     */
    private String fpjyunbi_filterrenketu;

    /**
     * F/P準備_ﾌｨﾙﾀｰ品名①
     */
    private String fpjyunbi_filterhinmei1;

    /**
     * F/P準備_LotNo①
     */
    private String fpjyunbi_lotno1;

    /**
     * F/P準備_取り付け本数①
     */
    private Integer fpjyunbi_toritukehonsuu1;

    /**
     * F/P準備_ﾌｨﾙﾀｰ品名①
     */
    private String fpjyunbi_filterhinmei2;

    /**
     * F/P準備_LotNo②
     */
    private String fpjyunbi_lotno2;

    /**
     * F/P準備_取り付け本数②
     */
    private Integer fpjyunbi_toritukehonsuu2;

    /**
     * 排出容器の内袋
     */
    private Integer haisyutuyoukinoutibukuro;

    /**
     * ﾌｨﾙﾀｰ使用回数
     */
    private String filterkaisuu;

    /**
     * F/PﾀﾝｸNo
     */
    private Integer fptankno;

    /**
     * 洗浄確認
     */
    private Integer senjyoukakunin;

    /**
     * F/P開始日時
     */
    private Timestamp fpkaisinichiji;

    /**
     * 圧送ﾚｷﾞｭﾚｰﾀｰNo
     */
    private String assouregulatorno;

    /**
     * 圧送圧力
     */
    private BigDecimal assouaturyoku;

    /**
     * F/P開始_担当者
     */
    private String fpkaisi_tantousya;

    /**
     * F/P停止日時
     */
    private Timestamp fpteisinichiji;

    /**
     * F/P交換_ﾌｨﾙﾀｰ連結
     */
    private String fpkoukan_filterrenketu;

    /**
     * F/P交換_ﾌｨﾙﾀｰ品名①
     */
    private String fpkoukan_filterhinmei1;

    /**
     * F/P交換_LotNo①
     */
    private String fpkoukan_lotno1;

    /**
     * F/P交換_取り付け本数①
     */
    private Integer fpkoukan_toritukehonsuu1;

    /**
     * F/P交換_ﾌｨﾙﾀｰ品名①
     */
    private String fpkoukan_filterhinmei2;

    /**
     * F/P交換_LotNo②
     */
    private String fpkoukan_lotno2;

    /**
     * F/P交換_取り付け本数②
     */
    private Integer fpkoukan_toritukehonsuu2;

    /**
     * F/P再開日時
     */
    private Timestamp fpsaikainichiji;

    /**
     * F/P交換_担当者
     */
    private String fpkoukan_tantousya;

    /**
     * F/P終了日時
     */
    private Timestamp fpsyuuryounichiji;

    /**
     * F/P時間
     */
    private String fpjikan;

    /**
     * F/P終了_担当者
     */
    private String fpsyuurou_tantousya;

    /**
     * 総重量①
     */
    private Integer soujyuurou1;

    /**
     * 総重量②
     */
    private Integer soujyuurou2;

    /**
     * 総重量③
     */
    private Integer soujyuurou3;

    /**
     * 総重量④
     */
    private Integer soujyuurou4;

    /**
     * 総重量⑤
     */
    private Integer soujyuurou5;

    /**
     * 総重量⑥
     */
    private Integer soujyuurou6;

    /**
     * 添加材ｽﾗﾘｰ重量①
     */
    private Integer tenkazaislurryjyuuryou1;

    /**
     * 添加材ｽﾗﾘｰ重量②
     */
    private Integer tenkazaislurryjyuuryou2;

    /**
     * 添加材ｽﾗﾘｰ重量③
     */
    private Integer tenkazaislurryjyuuryou3;

    /**
     * 添加材ｽﾗﾘｰ重量④
     */
    private Integer tenkazaislurryjyuuryou4;

    /**
     * 添加材ｽﾗﾘｰ重量⑤
     */
    private Integer tenkazaislurryjyuuryou5;

    /**
     * 添加材ｽﾗﾘｰ重量⑥
     */
    private Integer tenkazaislurryjyuuryou6;

    /**
     * 添加材ｽﾗﾘｰ重量合計
     */
    private Integer tenkazaislurryjyuuryougoukei;

    /**
     * 歩留まり
     */
    private BigDecimal budomari;

    /**
     * 添加材ｽﾗﾘｰ有効期限
     */
    private String tenkazaislurryyuukoukigen;

    /**
     * 粉砕判定
     */
    private Integer funsaihantei;

    /**
     * 担当者
     */
    private String tantousya;

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
     * 投入量
     * @return the tounyuuryou
     */
    public String getTounyuuryou() {
        return tounyuuryou;
    }

    /**
     * 投入量
     * @param tounyuuryou the tounyuuryou to set
     */
    public void setTounyuuryou(String tounyuuryou) {
        this.tounyuuryou = tounyuuryou;
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
    public Integer getHozonyousamplekaisyuu() {
        return hozonyousamplekaisyuu;
    }

    /**
     * 保存用ｻﾝﾌﾟﾙ回収
     * @param hozonyousamplekaisyuu the hozonyousamplekaisyuu to set
     */
    public void setHozonyousamplekaisyuu(Integer hozonyousamplekaisyuu) {
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
     * F/P準備_ﾌｨﾙﾀｰ連結
     * @return the fpjyunbi_filterrenketu
     */
    public String getFpjyunbi_filterrenketu() {
        return fpjyunbi_filterrenketu;
    }

    /**
     * F/P準備_ﾌｨﾙﾀｰ連結
     * @param fpjyunbi_filterrenketu the fpjyunbi_filterrenketu to set
     */
    public void setFpjyunbi_filterrenketu(String fpjyunbi_filterrenketu) {
        this.fpjyunbi_filterrenketu = fpjyunbi_filterrenketu;
    }

    /**
     * F/P準備_ﾌｨﾙﾀｰ品名①
     * @return the fpjyunbi_filterhinmei1
     */
    public String getFpjyunbi_filterhinmei1() {
        return fpjyunbi_filterhinmei1;
    }

    /**
     * F/P準備_ﾌｨﾙﾀｰ品名①
     * @param fpjyunbi_filterhinmei1 the fpjyunbi_filterhinmei1 to set
     */
    public void setFpjyunbi_filterhinmei1(String fpjyunbi_filterhinmei1) {
        this.fpjyunbi_filterhinmei1 = fpjyunbi_filterhinmei1;
    }

    /**
     * F/P準備_LotNo①
     * @return the fpjyunbi_lotno1
     */
    public String getFpjyunbi_lotno1() {
        return fpjyunbi_lotno1;
    }

    /**
     * F/P準備_LotNo①
     * @param fpjyunbi_lotno1 the fpjyunbi_lotno1 to set
     */
    public void setFpjyunbi_lotno1(String fpjyunbi_lotno1) {
        this.fpjyunbi_lotno1 = fpjyunbi_lotno1;
    }

    /**
     * F/P準備_取り付け本数①
     * @return the fpjyunbi_toritukehonsuu1
     */
    public Integer getFpjyunbi_toritukehonsuu1() {
        return fpjyunbi_toritukehonsuu1;
    }

    /**
     * F/P準備_取り付け本数①
     * @param fpjyunbi_toritukehonsuu1 the fpjyunbi_toritukehonsuu1 to set
     */
    public void setFpjyunbi_toritukehonsuu1(Integer fpjyunbi_toritukehonsuu1) {
        this.fpjyunbi_toritukehonsuu1 = fpjyunbi_toritukehonsuu1;
    }

    /**
     * F/P準備_ﾌｨﾙﾀｰ品名①
     * @return the fpjyunbi_filterhinmei2
     */
    public String getFpjyunbi_filterhinmei2() {
        return fpjyunbi_filterhinmei2;
    }

    /**
     * F/P準備_ﾌｨﾙﾀｰ品名①
     * @param fpjyunbi_filterhinmei2 the fpjyunbi_filterhinmei2 to set
     */
    public void setFpjyunbi_filterhinmei2(String fpjyunbi_filterhinmei2) {
        this.fpjyunbi_filterhinmei2 = fpjyunbi_filterhinmei2;
    }

    /**
     * F/P準備_LotNo②
     * @return the fpjyunbi_lotno2
     */
    public String getFpjyunbi_lotno2() {
        return fpjyunbi_lotno2;
    }

    /**
     * F/P準備_LotNo②
     * @param fpjyunbi_lotno2 the fpjyunbi_lotno2 to set
     */
    public void setFpjyunbi_lotno2(String fpjyunbi_lotno2) {
        this.fpjyunbi_lotno2 = fpjyunbi_lotno2;
    }

    /**
     * F/P準備_取り付け本数②
     * @return the fpjyunbi_toritukehonsuu2
     */
    public Integer getFpjyunbi_toritukehonsuu2() {
        return fpjyunbi_toritukehonsuu2;
    }

    /**
     * F/P準備_取り付け本数②
     * @param fpjyunbi_toritukehonsuu2 the fpjyunbi_toritukehonsuu2 to set
     */
    public void setFpjyunbi_toritukehonsuu2(Integer fpjyunbi_toritukehonsuu2) {
        this.fpjyunbi_toritukehonsuu2 = fpjyunbi_toritukehonsuu2;
    }

    /**
     * 排出容器の内袋
     * @return the haisyutuyoukinoutibukuro
     */
    public Integer getHaisyutuyoukinoutibukuro() {
        return haisyutuyoukinoutibukuro;
    }

    /**
     * 排出容器の内袋
     * @param haisyutuyoukinoutibukuro the haisyutuyoukinoutibukuro to set
     */
    public void setHaisyutuyoukinoutibukuro(Integer haisyutuyoukinoutibukuro) {
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
     * @return the fptankno
     */
    public Integer getFptankno() {
        return fptankno;
    }

    /**
     * F/PﾀﾝｸNo
     * @param fptankno the fptankno to set
     */
    public void setFptankno(Integer fptankno) {
        this.fptankno = fptankno;
    }

    /**
     * 洗浄確認
     * @return the senjyoukakunin
     */
    public Integer getSenjyoukakunin() {
        return senjyoukakunin;
    }

    /**
     * 洗浄確認
     * @param senjyoukakunin the senjyoukakunin to set
     */
    public void setSenjyoukakunin(Integer senjyoukakunin) {
        this.senjyoukakunin = senjyoukakunin;
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
     * 圧送ﾚｷﾞｭﾚｰﾀｰNo
     * @return the assouregulatorno
     */
    public String getAssouregulatorno() {
        return assouregulatorno;
    }

    /**
     * 圧送ﾚｷﾞｭﾚｰﾀｰNo
     * @param assouregulatorno the assouregulatorno to set
     */
    public void setAssouregulatorno(String assouregulatorno) {
        this.assouregulatorno = assouregulatorno;
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
     * F/P停止日時
     * @return the fpteisinichiji
     */
    public Timestamp getFpteisinichiji() {
        return fpteisinichiji;
    }

    /**
     * F/P停止日時
     * @param fpteisinichiji the fpteisinichiji to set
     */
    public void setFpteisinichiji(Timestamp fpteisinichiji) {
        this.fpteisinichiji = fpteisinichiji;
    }

    /**
     * F/P交換_ﾌｨﾙﾀｰ連結
     * @return the fpkoukan_filterrenketu
     */
    public String getFpkoukan_filterrenketu() {
        return fpkoukan_filterrenketu;
    }

    /**
     * F/P交換_ﾌｨﾙﾀｰ連結
     * @param fpkoukan_filterrenketu the fpkoukan_filterrenketu to set
     */
    public void setFpkoukan_filterrenketu(String fpkoukan_filterrenketu) {
        this.fpkoukan_filterrenketu = fpkoukan_filterrenketu;
    }

    /**
     * F/P交換_ﾌｨﾙﾀｰ品名①
     * @return the fpkoukan_filterhinmei1
     */
    public String getFpkoukan_filterhinmei1() {
        return fpkoukan_filterhinmei1;
    }

    /**
     * F/P交換_ﾌｨﾙﾀｰ品名①
     * @param fpkoukan_filterhinmei1 the fpkoukan_filterhinmei1 to set
     */
    public void setFpkoukan_filterhinmei1(String fpkoukan_filterhinmei1) {
        this.fpkoukan_filterhinmei1 = fpkoukan_filterhinmei1;
    }

    /**
     * F/P交換_LotNo①
     * @return the fpkoukan_lotno1
     */
    public String getFpkoukan_lotno1() {
        return fpkoukan_lotno1;
    }

    /**
     * F/P交換_LotNo①
     * @param fpkoukan_lotno1 the fpkoukan_lotno1 to set
     */
    public void setFpkoukan_lotno1(String fpkoukan_lotno1) {
        this.fpkoukan_lotno1 = fpkoukan_lotno1;
    }

    /**
     * F/P交換_取り付け本数①
     * @return the fpkoukan_toritukehonsuu1
     */
    public Integer getFpkoukan_toritukehonsuu1() {
        return fpkoukan_toritukehonsuu1;
    }

    /**
     * F/P交換_取り付け本数①
     * @param fpkoukan_toritukehonsuu1 the fpkoukan_toritukehonsuu1 to set
     */
    public void setFpkoukan_toritukehonsuu1(Integer fpkoukan_toritukehonsuu1) {
        this.fpkoukan_toritukehonsuu1 = fpkoukan_toritukehonsuu1;
    }

    /**
     * F/P交換_ﾌｨﾙﾀｰ品名①
     * @return the fpkoukan_filterhinmei2
     */
    public String getFpkoukan_filterhinmei2() {
        return fpkoukan_filterhinmei2;
    }

    /**
     * F/P交換_ﾌｨﾙﾀｰ品名①
     * @param fpkoukan_filterhinmei2 the fpkoukan_filterhinmei2 to set
     */
    public void setFpkoukan_filterhinmei2(String fpkoukan_filterhinmei2) {
        this.fpkoukan_filterhinmei2 = fpkoukan_filterhinmei2;
    }

    /**
     * F/P交換_LotNo②
     * @return the fpkoukan_lotno2
     */
    public String getFpkoukan_lotno2() {
        return fpkoukan_lotno2;
    }

    /**
     * F/P交換_LotNo②
     * @param fpkoukan_lotno2 the fpkoukan_lotno2 to set
     */
    public void setFpkoukan_lotno2(String fpkoukan_lotno2) {
        this.fpkoukan_lotno2 = fpkoukan_lotno2;
    }

    /**
     * F/P交換_取り付け本数②
     * @return the fpkoukan_toritukehonsuu2
     */
    public Integer getFpkoukan_toritukehonsuu2() {
        return fpkoukan_toritukehonsuu2;
    }

    /**
     * F/P交換_取り付け本数②
     * @param fpkoukan_toritukehonsuu2 the fpkoukan_toritukehonsuu2 to set
     */
    public void setFpkoukan_toritukehonsuu2(Integer fpkoukan_toritukehonsuu2) {
        this.fpkoukan_toritukehonsuu2 = fpkoukan_toritukehonsuu2;
    }

    /**
     * F/P再開日時
     * @return the fpsaikainichiji
     */
    public Timestamp getFpsaikainichiji() {
        return fpsaikainichiji;
    }

    /**
     * F/P再開日時
     * @param fpsaikainichiji the fpsaikainichiji to set
     */
    public void setFpsaikainichiji(Timestamp fpsaikainichiji) {
        this.fpsaikainichiji = fpsaikainichiji;
    }

    /**
     * F/P交換_担当者
     * @return the fpkoukan_tantousya
     */
    public String getFpkoukan_tantousya() {
        return fpkoukan_tantousya;
    }

    /**
     * F/P交換_担当者
     * @param fpkoukan_tantousya the fpkoukan_tantousya to set
     */
    public void setFpkoukan_tantousya(String fpkoukan_tantousya) {
        this.fpkoukan_tantousya = fpkoukan_tantousya;
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
     * F/P時間
     * @return the fpjikan
     */
    public String getFpjikan() {
        return fpjikan;
    }

    /**
     * F/P時間
     * @param fpjikan the fpjikan to set
     */
    public void setFpjikan(String fpjikan) {
        this.fpjikan = fpjikan;
    }

    /**
     * F/P終了_担当者
     * @return the fpsyuurou_tantousya
     */
    public String getFpsyuurou_tantousya() {
        return fpsyuurou_tantousya;
    }

    /**
     * F/P終了_担当者
     * @param fpsyuurou_tantousya the fpsyuurou_tantousya to set
     */
    public void setFpsyuurou_tantousya(String fpsyuurou_tantousya) {
        this.fpsyuurou_tantousya = fpsyuurou_tantousya;
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
    public Integer getFunsaihantei() {
        return funsaihantei;
    }

    /**
     * 粉砕判定
     * @param funsaihantei the funsaihantei to set
     */
    public void setFunsaihantei(Integer funsaihantei) {
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