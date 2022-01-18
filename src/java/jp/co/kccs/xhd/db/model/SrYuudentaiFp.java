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
 * 変更日	2021/11/14<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SR_YUUDENTAI_FP(誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管)のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/11/14
 */
public class SrYuudentaiFp {
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
     * 誘電体ｽﾗﾘｰ品名
     */
    private String yuudentaislurryhinmei;

    /**
     * 誘電体ｽﾗﾘｰLotNo
     */
    private String yuudentaislurrylotno;

    /**
     * ﾛｯﾄ区分
     */
    private String lotkubun;

    /**
     * 原料LotNo
     */
    private String genryoulotno;

    /**
     * 原料記号
     */
    private String genryoukigou;

    /**
     * 風袋重量測定_ｽﾃﾝ容器1
     */
    private Integer fuutaijyuuryousokutei_sutenyouki1;

    /**
     * 風袋重量測定_ｽﾃﾝ容器2
     */
    private Integer fuutaijyuuryousokutei_sutenyouki2;

    /**
     * 風袋重量測定_ｽﾃﾝ容器3
     */
    private Integer fuutaijyuuryousokutei_sutenyouki3;

    /**
     * 風袋重量測定_ｽﾃﾝ容器4
     */
    private Integer fuutaijyuuryousokutei_sutenyouki4;

    /**
     * 風袋重量測定_ｽﾃﾝ容器5
     */
    private Integer fuutaijyuuryousokutei_sutenyouki5;

    /**
     * 風袋重量測定_ｽﾃﾝ容器6
     */
    private Integer fuutaijyuuryousokutei_sutenyouki6;

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器1
     */
    private Integer fuutaijyuuryousokutei_siropori1;

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器2
     */
    private Integer fuutaijyuuryousokutei_siropori2;

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器3
     */
    private Integer fuutaijyuuryousokutei_siropori3;

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器4
     */
    private Integer fuutaijyuuryousokutei_siropori4;

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器5
     */
    private Integer fuutaijyuuryousokutei_siropori5;

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器6
     */
    private Integer fuutaijyuuryousokutei_siropori6;

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器7
     */
    private Integer fuutaijyuuryousokutei_siropori7;

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器8
     */
    private Integer fuutaijyuuryousokutei_siropori8;

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器9
     */
    private Integer fuutaijyuuryousokutei_siropori9;

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器10
     */
    private Integer fuutaijyuuryousokutei_siropori10;

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器11
     */
    private Integer fuutaijyuuryousokutei_siropori11;

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器12
     */
    private Integer fuutaijyuuryousokutei_siropori12;

    /**
     * 保管容器準備_担当者
     */
    private String fuutaijyuuryousokutei_tantousya;

    /**
     * ﾌｨﾙﾀｰ連結
     */
    private String filterrenketu;

    /**
     * ﾌｨﾙﾀｰ取り付け_1次ﾌｨﾙﾀｰ品名
     */
    private String filtertorituke_itijifilterhinmei;

    /**
     * ﾌｨﾙﾀｰ取り付け_LotNo1
     */
    private String filtertorituke_lotno1;

    /**
     * ﾌｨﾙﾀｰ取り付け_取り付け本数1
     */
    private Integer filtertorituke_toritukehonsuu1;

    /**
     * ﾌｨﾙﾀｰ取り付け_2次ﾌｨﾙﾀｰ品名
     */
    private String filtertorituke_nijifilterhinmei;

    /**
     * ﾌｨﾙﾀｰ取り付け_LotNo2
     */
    private String filtertorituke_lotno2;

    /**
     * ﾌｨﾙﾀｰ取り付け_取り付け本数2
     */
    private Integer filtertorituke_toritukehonsuu2;

    /**
     * ﾌｨﾙﾀｰ取り付け_3次ﾌｨﾙﾀｰ品名
     */
    private String filtertorituke_sanjifilterhinmei;

    /**
     * ﾌｨﾙﾀｰ取り付け_LotNo3
     */
    private String filtertorituke_lotno3;

    /**
     * ﾌｨﾙﾀｰ取り付け_取り付け本数3
     */
    private Integer filtertorituke_toritukehonsuu3;

    /**
     * ﾌｨﾙﾀｰ取り付け_担当者
     */
    private String filtertorituke_tantousya;

    /**
     * 排出容器の内袋
     */
    private Integer haisyutuyoukinoutibukuro;

    /**
     * ﾌｨﾙﾀｰ使用回数
     */
    private String filtersiyoukaisuu;

    /**
     * F/PﾀﾝｸNo
     */
    private String Fptankno;

    /**
     * 洗浄確認
     */
    private Integer senjyoukakunin;

    /**
     * F/P開始日時
     */
    private Timestamp Fpkaisinichiji;

    /**
     * 圧送ﾚｷﾞｭﾚｰﾀｰNo
     */
    private String assouregulatorNo;

    /**
     * 圧送圧力
     */
    private BigDecimal assouaturyoku;

    /**
     * ﾌｨﾙﾀｰﾊﾟｽ開始_担当者
     */
    private String filterpasskaisi_tantousya;

    /**
     * ﾌｨﾙﾀｰ交換①_F/P停止日時
     */
    private Timestamp filterkoukan1_Fpteisinichiji;

    /**
     * ﾌｨﾙﾀｰ交換①_1次ﾌｨﾙﾀｰ品名
     */
    private String filterkoukan1_itijifilterhinmei;

    /**
     * ﾌｨﾙﾀｰ交換①_LotNo1
     */
    private String filterkoukan1_lotno1;

    /**
     * ﾌｨﾙﾀｰ交換①_取り付け本数1
     */
    private Integer filterkoukan1_toritukehonsuu1;

    /**
     * ﾌｨﾙﾀｰ交換①_2次ﾌｨﾙﾀｰ品名
     */
    private String filterkoukan1_nijifilterhinmei;

    /**
     * ﾌｨﾙﾀｰ交換①_LotNo2
     */
    private String filterkoukan1_lotno2;

    /**
     * ﾌｨﾙﾀｰ交換①_取り付け本数2
     */
    private Integer filterkoukan1_toritukehonsuu2;

    /**
     * ﾌｨﾙﾀｰ交換①_3次ﾌｨﾙﾀｰ品名
     */
    private String filterkoukan1_sanjifilterhinmei;

    /**
     * ﾌｨﾙﾀｰ交換①_LotNo3
     */
    private String filterkoukan1_lotno3;

    /**
     * ﾌｨﾙﾀｰ交換①_取り付け本数3
     */
    private Integer filterkoukan1_toritukehonsuu3;

    /**
     * ﾌｨﾙﾀｰ交換①_F/P再開日時
     */
    private Timestamp filterkoukan1_Fpsaikainichiji;

    /**
     * ﾌｨﾙﾀｰ交換①_担当者
     */
    private String filterkoukan1_tantousya;

    /**
     * F/P終了日時
     */
    private Timestamp FPsyuuryounichiji;

    /**
     * F/Pﾄｰﾀﾙ時間
     */
    private String FPtotaljikan;

    /**
     * 1次ﾌｨﾙﾀｰ総使用本数
     */
    private Integer itijifiltersousiyouhonsuu;

    /**
     * 2次ﾌｨﾙﾀｰ総使用本数
     */
    private Integer nijifiltersousiyouhonsuu;

    /**
     * 3次ﾌｨﾙﾀｰ総使用本数
     */
    private Integer sanjifiltersousiyouhonsuu;

    /**
     * ﾌｨﾙﾀｰﾊﾟｽ終了_担当者
     */
    private String filterpasssyuuryou_tantousya;

    /**
     * 総重量測定1
     */
    private Integer soujyuurousokutei1;

    /**
     * 総重量測定2
     */
    private Integer soujyuurousokutei2;

    /**
     * 総重量測定3
     */
    private Integer soujyuurousokutei3;

    /**
     * 総重量測定4
     */
    private Integer soujyuurousokutei4;

    /**
     * 総重量測定5
     */
    private Integer soujyuurousokutei5;

    /**
     * 総重量測定6
     */
    private Integer soujyuurousokutei6;

    /**
     * 総重量測定7
     */
    private Integer soujyuurousokutei7;

    /**
     * 総重量測定8
     */
    private Integer soujyuurousokutei8;

    /**
     * 総重量測定9
     */
    private Integer soujyuurousokutei9;

    /**
     * 総重量測定10
     */
    private Integer soujyuurousokutei10;

    /**
     * 総重量測定11
     */
    private Integer soujyuurousokutei11;

    /**
     * 総重量測定12
     */
    private Integer soujyuurousokutei12;

    /**
     * 誘電体ｽﾗﾘｰ重量1
     */
    private Integer yuudentaislurryjyuurou1;

    /**
     * 誘電体ｽﾗﾘｰ重量2
     */
    private Integer yuudentaislurryjyuurou2;

    /**
     * 誘電体ｽﾗﾘｰ重量3
     */
    private Integer yuudentaislurryjyuurou3;

    /**
     * 誘電体ｽﾗﾘｰ重量4
     */
    private Integer yuudentaislurryjyuurou4;

    /**
     * 誘電体ｽﾗﾘｰ重量5
     */
    private Integer yuudentaislurryjyuurou5;

    /**
     * 誘電体ｽﾗﾘｰ重量6
     */
    private Integer yuudentaislurryjyuurou6;

    /**
     * 誘電体ｽﾗﾘｰ重量7
     */
    private Integer yuudentaislurryjyuurou7;

    /**
     * 誘電体ｽﾗﾘｰ重量8
     */
    private Integer yuudentaislurryjyuurou8;

    /**
     * 誘電体ｽﾗﾘｰ重量9
     */
    private Integer yuudentaislurryjyuurou9;

    /**
     * 誘電体ｽﾗﾘｰ重量10
     */
    private Integer yuudentaislurryjyuurou10;

    /**
     * 誘電体ｽﾗﾘｰ重量11
     */
    private Integer yuudentaislurryjyuurou11;

    /**
     * 誘電体ｽﾗﾘｰ重量12
     */
    private Integer yuudentaislurryjyuurou12;

    /**
     * 誘電体ｽﾗﾘｰ重量合計
     */
    private Integer yuudentaislurryjyuurougoukei;

    /**
     * 投入量
     */
    private String tounyuuryou;

    /**
     * 歩留まり計算
     */
    private BigDecimal budomarikeisan;

    /**
     * 誘電体ｽﾗﾘｰ有効期限
     */
    private String yuudentaislurryyuukoukigen;

    /**
     * 粉砕判定
     */
    private Integer funsaihantei;

    /**
     * 製品重量確認_担当者
     */
    private String seihinjyuuryoukakunin_tantousya;

    /**
     * 保存用ｻﾝﾌﾟﾙ回収
     */
    private Integer hozonyousamplekaisyu;

    /**
     * 分析用ｻﾝﾌﾟﾙ回収
     */
    private Integer zunsekiyousamplekaisyu;

    /**
     * 乾燥皿
     */
    private String kansouzara;

    /**
     * ｱﾙﾐ皿風袋重量
     */
    private Integer arumizarafuutaijyuuryou;

    /**
     * ｽﾗﾘｰｻﾝﾌﾟﾙ重量規格
     */
    private String slurrysamplejyuuryoukikaku;

    /**
     * 乾燥前ｽﾗﾘｰ重量
     */
    private Integer kansoumaeslurryjyuuryou;

    /**
     * 乾燥機
     */
    private String kansouki;

    /**
     * 乾燥温度規格
     */
    private String kansouondokikaku;

    /**
     * 乾燥時間規格
     */
    private String kansoujikankikaku;

    /**
     * 乾燥開始日時
     */
    private Timestamp kansoukaisinichiji;

    /**
     * 乾燥終了日時
     */
    private Timestamp kansousyuuryounichiji;

    /**
     * 乾燥時間ﾄｰﾀﾙ
     */
    private String kansoujikantotal;

    /**
     * 乾燥後総重量
     */
    private BigDecimal kansougosoujyuuryou;

    /**
     * 乾燥後正味重量
     */
    private BigDecimal kansougosyoumijyuuryou;

    /**
     * 固形分比率
     */
    private BigDecimal kokeibunhiritu;

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
     * 誘電体ｽﾗﾘｰ品名
     * @return the yuudentaislurryhinmei
     */
    public String getYuudentaislurryhinmei() {
        return yuudentaislurryhinmei;
    }

    /**
     * 誘電体ｽﾗﾘｰ品名
     * @param yuudentaislurryhinmei the yuudentaislurryhinmei to set
     */
    public void setYuudentaislurryhinmei(String yuudentaislurryhinmei) {
        this.yuudentaislurryhinmei = yuudentaislurryhinmei;
    }

    /**
     * 誘電体ｽﾗﾘｰLotNo
     * @return the yuudentaislurrylotno
     */
    public String getYuudentaislurrylotno() {
        return yuudentaislurrylotno;
    }

    /**
     * 誘電体ｽﾗﾘｰLotNo
     * @param yuudentaislurrylotno the yuudentaislurrylotno to set
     */
    public void setYuudentaislurrylotno(String yuudentaislurrylotno) {
        this.yuudentaislurrylotno = yuudentaislurrylotno;
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
     * 原料LotNo
     * @return the genryoulotno
     */
    public String getGenryoulotno() {
        return genryoulotno;
    }

    /**
     * 原料LotNo
     * @param genryoulotno the genryoulotno to set
     */
    public void setGenryoulotno(String genryoulotno) {
        this.genryoulotno = genryoulotno;
    }

    /**
     * 原料記号
     * @return the genryoukigou
     */
    public String getGenryoukigou() {
        return genryoukigou;
    }

    /**
     * 原料記号
     * @param genryoukigou the genryoukigou to set
     */
    public void setGenryoukigou(String genryoukigou) {
        this.genryoukigou = genryoukigou;
    }

    /**
     * 風袋重量測定_ｽﾃﾝ容器1
     * @return the fuutaijyuuryousokutei_sutenyouki1
     */
    public Integer getFuutaijyuuryousokutei_sutenyouki1() {
        return fuutaijyuuryousokutei_sutenyouki1;
    }

    /**
     * 風袋重量測定_ｽﾃﾝ容器1
     * @param fuutaijyuuryousokutei_sutenyouki1 the fuutaijyuuryousokutei_sutenyouki1 to set
     */
    public void setFuutaijyuuryousokutei_sutenyouki1(Integer fuutaijyuuryousokutei_sutenyouki1) {
        this.fuutaijyuuryousokutei_sutenyouki1 = fuutaijyuuryousokutei_sutenyouki1;
    }

    /**
     * 風袋重量測定_ｽﾃﾝ容器2
     * @return the fuutaijyuuryousokutei_sutenyouki2
     */
    public Integer getFuutaijyuuryousokutei_sutenyouki2() {
        return fuutaijyuuryousokutei_sutenyouki2;
    }

    /**
     * 風袋重量測定_ｽﾃﾝ容器2
     * @param fuutaijyuuryousokutei_sutenyouki2 the fuutaijyuuryousokutei_sutenyouki2 to set
     */
    public void setFuutaijyuuryousokutei_sutenyouki2(Integer fuutaijyuuryousokutei_sutenyouki2) {
        this.fuutaijyuuryousokutei_sutenyouki2 = fuutaijyuuryousokutei_sutenyouki2;
    }

    /**
     * 風袋重量測定_ｽﾃﾝ容器3
     * @return the fuutaijyuuryousokutei_sutenyouki3
     */
    public Integer getFuutaijyuuryousokutei_sutenyouki3() {
        return fuutaijyuuryousokutei_sutenyouki3;
    }

    /**
     * 風袋重量測定_ｽﾃﾝ容器3
     * @param fuutaijyuuryousokutei_sutenyouki3 the fuutaijyuuryousokutei_sutenyouki3 to set
     */
    public void setFuutaijyuuryousokutei_sutenyouki3(Integer fuutaijyuuryousokutei_sutenyouki3) {
        this.fuutaijyuuryousokutei_sutenyouki3 = fuutaijyuuryousokutei_sutenyouki3;
    }

    /**
     * 風袋重量測定_ｽﾃﾝ容器4
     * @return the fuutaijyuuryousokutei_sutenyouki4
     */
    public Integer getFuutaijyuuryousokutei_sutenyouki4() {
        return fuutaijyuuryousokutei_sutenyouki4;
    }

    /**
     * 風袋重量測定_ｽﾃﾝ容器4
     * @param fuutaijyuuryousokutei_sutenyouki4 the fuutaijyuuryousokutei_sutenyouki4 to set
     */
    public void setFuutaijyuuryousokutei_sutenyouki4(Integer fuutaijyuuryousokutei_sutenyouki4) {
        this.fuutaijyuuryousokutei_sutenyouki4 = fuutaijyuuryousokutei_sutenyouki4;
    }

    /**
     * 風袋重量測定_ｽﾃﾝ容器5
     * @return the fuutaijyuuryousokutei_sutenyouki5
     */
    public Integer getFuutaijyuuryousokutei_sutenyouki5() {
        return fuutaijyuuryousokutei_sutenyouki5;
    }

    /**
     * 風袋重量測定_ｽﾃﾝ容器5
     * @param fuutaijyuuryousokutei_sutenyouki5 the fuutaijyuuryousokutei_sutenyouki5 to set
     */
    public void setFuutaijyuuryousokutei_sutenyouki5(Integer fuutaijyuuryousokutei_sutenyouki5) {
        this.fuutaijyuuryousokutei_sutenyouki5 = fuutaijyuuryousokutei_sutenyouki5;
    }

    /**
     * 風袋重量測定_ｽﾃﾝ容器6
     * @return the fuutaijyuuryousokutei_sutenyouki6
     */
    public Integer getFuutaijyuuryousokutei_sutenyouki6() {
        return fuutaijyuuryousokutei_sutenyouki6;
    }

    /**
     * 風袋重量測定_ｽﾃﾝ容器6
     * @param fuutaijyuuryousokutei_sutenyouki6 the fuutaijyuuryousokutei_sutenyouki6 to set
     */
    public void setFuutaijyuuryousokutei_sutenyouki6(Integer fuutaijyuuryousokutei_sutenyouki6) {
        this.fuutaijyuuryousokutei_sutenyouki6 = fuutaijyuuryousokutei_sutenyouki6;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器1
     * @return the fuutaijyuuryousokutei_siropori1
     */
    public Integer getFuutaijyuuryousokutei_siropori1() {
        return fuutaijyuuryousokutei_siropori1;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器1
     * @param fuutaijyuuryousokutei_siropori1 the fuutaijyuuryousokutei_siropori1 to set
     */
    public void setFuutaijyuuryousokutei_siropori1(Integer fuutaijyuuryousokutei_siropori1) {
        this.fuutaijyuuryousokutei_siropori1 = fuutaijyuuryousokutei_siropori1;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器2
     * @return the fuutaijyuuryousokutei_siropori2
     */
    public Integer getFuutaijyuuryousokutei_siropori2() {
        return fuutaijyuuryousokutei_siropori2;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器2
     * @param fuutaijyuuryousokutei_siropori2 the fuutaijyuuryousokutei_siropori2 to set
     */
    public void setFuutaijyuuryousokutei_siropori2(Integer fuutaijyuuryousokutei_siropori2) {
        this.fuutaijyuuryousokutei_siropori2 = fuutaijyuuryousokutei_siropori2;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器3
     * @return the fuutaijyuuryousokutei_siropori3
     */
    public Integer getFuutaijyuuryousokutei_siropori3() {
        return fuutaijyuuryousokutei_siropori3;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器3
     * @param fuutaijyuuryousokutei_siropori3 the fuutaijyuuryousokutei_siropori3 to set
     */
    public void setFuutaijyuuryousokutei_siropori3(Integer fuutaijyuuryousokutei_siropori3) {
        this.fuutaijyuuryousokutei_siropori3 = fuutaijyuuryousokutei_siropori3;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器4
     * @return the fuutaijyuuryousokutei_siropori4
     */
    public Integer getFuutaijyuuryousokutei_siropori4() {
        return fuutaijyuuryousokutei_siropori4;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器4
     * @param fuutaijyuuryousokutei_siropori4 the fuutaijyuuryousokutei_siropori4 to set
     */
    public void setFuutaijyuuryousokutei_siropori4(Integer fuutaijyuuryousokutei_siropori4) {
        this.fuutaijyuuryousokutei_siropori4 = fuutaijyuuryousokutei_siropori4;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器5
     * @return the fuutaijyuuryousokutei_siropori5
     */
    public Integer getFuutaijyuuryousokutei_siropori5() {
        return fuutaijyuuryousokutei_siropori5;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器5
     * @param fuutaijyuuryousokutei_siropori5 the fuutaijyuuryousokutei_siropori5 to set
     */
    public void setFuutaijyuuryousokutei_siropori5(Integer fuutaijyuuryousokutei_siropori5) {
        this.fuutaijyuuryousokutei_siropori5 = fuutaijyuuryousokutei_siropori5;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器6
     * @return the fuutaijyuuryousokutei_siropori6
     */
    public Integer getFuutaijyuuryousokutei_siropori6() {
        return fuutaijyuuryousokutei_siropori6;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器6
     * @param fuutaijyuuryousokutei_siropori6 the fuutaijyuuryousokutei_siropori6 to set
     */
    public void setFuutaijyuuryousokutei_siropori6(Integer fuutaijyuuryousokutei_siropori6) {
        this.fuutaijyuuryousokutei_siropori6 = fuutaijyuuryousokutei_siropori6;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器7
     * @return the fuutaijyuuryousokutei_siropori7
     */
    public Integer getFuutaijyuuryousokutei_siropori7() {
        return fuutaijyuuryousokutei_siropori7;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器7
     * @param fuutaijyuuryousokutei_siropori7 the fuutaijyuuryousokutei_siropori7 to set
     */
    public void setFuutaijyuuryousokutei_siropori7(Integer fuutaijyuuryousokutei_siropori7) {
        this.fuutaijyuuryousokutei_siropori7 = fuutaijyuuryousokutei_siropori7;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器8
     * @return the fuutaijyuuryousokutei_siropori8
     */
    public Integer getFuutaijyuuryousokutei_siropori8() {
        return fuutaijyuuryousokutei_siropori8;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器8
     * @param fuutaijyuuryousokutei_siropori8 the fuutaijyuuryousokutei_siropori8 to set
     */
    public void setFuutaijyuuryousokutei_siropori8(Integer fuutaijyuuryousokutei_siropori8) {
        this.fuutaijyuuryousokutei_siropori8 = fuutaijyuuryousokutei_siropori8;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器9
     * @return the fuutaijyuuryousokutei_siropori9
     */
    public Integer getFuutaijyuuryousokutei_siropori9() {
        return fuutaijyuuryousokutei_siropori9;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器9
     * @param fuutaijyuuryousokutei_siropori9 the fuutaijyuuryousokutei_siropori9 to set
     */
    public void setFuutaijyuuryousokutei_siropori9(Integer fuutaijyuuryousokutei_siropori9) {
        this.fuutaijyuuryousokutei_siropori9 = fuutaijyuuryousokutei_siropori9;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器10
     * @return the fuutaijyuuryousokutei_siropori10
     */
    public Integer getFuutaijyuuryousokutei_siropori10() {
        return fuutaijyuuryousokutei_siropori10;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器10
     * @param fuutaijyuuryousokutei_siropori10 the fuutaijyuuryousokutei_siropori10 to set
     */
    public void setFuutaijyuuryousokutei_siropori10(Integer fuutaijyuuryousokutei_siropori10) {
        this.fuutaijyuuryousokutei_siropori10 = fuutaijyuuryousokutei_siropori10;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器11
     * @return the fuutaijyuuryousokutei_siropori11
     */
    public Integer getFuutaijyuuryousokutei_siropori11() {
        return fuutaijyuuryousokutei_siropori11;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器11
     * @param fuutaijyuuryousokutei_siropori11 the fuutaijyuuryousokutei_siropori11 to set
     */
    public void setFuutaijyuuryousokutei_siropori11(Integer fuutaijyuuryousokutei_siropori11) {
        this.fuutaijyuuryousokutei_siropori11 = fuutaijyuuryousokutei_siropori11;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器12
     * @return the fuutaijyuuryousokutei_siropori12
     */
    public Integer getFuutaijyuuryousokutei_siropori12() {
        return fuutaijyuuryousokutei_siropori12;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器12
     * @param fuutaijyuuryousokutei_siropori12 the fuutaijyuuryousokutei_siropori12 to set
     */
    public void setFuutaijyuuryousokutei_siropori12(Integer fuutaijyuuryousokutei_siropori12) {
        this.fuutaijyuuryousokutei_siropori12 = fuutaijyuuryousokutei_siropori12;
    }

    /**
     * 保管容器準備_担当者
     * @return the fuutaijyuuryousokutei_tantousya
     */
    public String getFuutaijyuuryousokutei_tantousya() {
        return fuutaijyuuryousokutei_tantousya;
    }

    /**
     * 保管容器準備_担当者
     * @param fuutaijyuuryousokutei_tantousya the fuutaijyuuryousokutei_tantousya to set
     */
    public void setFuutaijyuuryousokutei_tantousya(String fuutaijyuuryousokutei_tantousya) {
        this.fuutaijyuuryousokutei_tantousya = fuutaijyuuryousokutei_tantousya;
    }

    /**
     * ﾌｨﾙﾀｰ連結
     * @return the filterrenketu
     */
    public String getFilterrenketu() {
        return filterrenketu;
    }

    /**
     * ﾌｨﾙﾀｰ連結
     * @param filterrenketu the filterrenketu to set
     */
    public void setFilterrenketu(String filterrenketu) {
        this.filterrenketu = filterrenketu;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_1次ﾌｨﾙﾀｰ品名
     * @return the filtertorituke_itijifilterhinmei
     */
    public String getFiltertorituke_itijifilterhinmei() {
        return filtertorituke_itijifilterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_1次ﾌｨﾙﾀｰ品名
     * @param filtertorituke_itijifilterhinmei the filtertorituke_itijifilterhinmei to set
     */
    public void setFiltertorituke_itijifilterhinmei(String filtertorituke_itijifilterhinmei) {
        this.filtertorituke_itijifilterhinmei = filtertorituke_itijifilterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_LotNo1
     * @return the filtertorituke_lotno1
     */
    public String getFiltertorituke_lotno1() {
        return filtertorituke_lotno1;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_LotNo1
     * @param filtertorituke_lotno1 the filtertorituke_lotno1 to set
     */
    public void setFiltertorituke_lotno1(String filtertorituke_lotno1) {
        this.filtertorituke_lotno1 = filtertorituke_lotno1;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_取り付け本数1
     * @return the filtertorituke_toritukehonsuu1
     */
    public Integer getFiltertorituke_toritukehonsuu1() {
        return filtertorituke_toritukehonsuu1;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_取り付け本数1
     * @param filtertorituke_toritukehonsuu1 the filtertorituke_toritukehonsuu1 to set
     */
    public void setFiltertorituke_toritukehonsuu1(Integer filtertorituke_toritukehonsuu1) {
        this.filtertorituke_toritukehonsuu1 = filtertorituke_toritukehonsuu1;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_2次ﾌｨﾙﾀｰ品名
     * @return the filtertorituke_nijifilterhinmei
     */
    public String getFiltertorituke_nijifilterhinmei() {
        return filtertorituke_nijifilterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_2次ﾌｨﾙﾀｰ品名
     * @param filtertorituke_nijifilterhinmei the filtertorituke_nijifilterhinmei to set
     */
    public void setFiltertorituke_nijifilterhinmei(String filtertorituke_nijifilterhinmei) {
        this.filtertorituke_nijifilterhinmei = filtertorituke_nijifilterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_LotNo2
     * @return the filtertorituke_lotno2
     */
    public String getFiltertorituke_lotno2() {
        return filtertorituke_lotno2;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_LotNo2
     * @param filtertorituke_lotno2 the filtertorituke_lotno2 to set
     */
    public void setFiltertorituke_lotno2(String filtertorituke_lotno2) {
        this.filtertorituke_lotno2 = filtertorituke_lotno2;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_取り付け本数2
     * @return the filtertorituke_toritukehonsuu2
     */
    public Integer getFiltertorituke_toritukehonsuu2() {
        return filtertorituke_toritukehonsuu2;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_取り付け本数2
     * @param filtertorituke_toritukehonsuu2 the filtertorituke_toritukehonsuu2 to set
     */
    public void setFiltertorituke_toritukehonsuu2(Integer filtertorituke_toritukehonsuu2) {
        this.filtertorituke_toritukehonsuu2 = filtertorituke_toritukehonsuu2;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_3次ﾌｨﾙﾀｰ品名
     * @return the filtertorituke_sanjifilterhinmei
     */
    public String getFiltertorituke_sanjifilterhinmei() {
        return filtertorituke_sanjifilterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_3次ﾌｨﾙﾀｰ品名
     * @param filtertorituke_sanjifilterhinmei the filtertorituke_sanjifilterhinmei to set
     */
    public void setFiltertorituke_sanjifilterhinmei(String filtertorituke_sanjifilterhinmei) {
        this.filtertorituke_sanjifilterhinmei = filtertorituke_sanjifilterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_LotNo3
     * @return the filtertorituke_lotno3
     */
    public String getFiltertorituke_lotno3() {
        return filtertorituke_lotno3;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_LotNo3
     * @param filtertorituke_lotno3 the filtertorituke_lotno3 to set
     */
    public void setFiltertorituke_lotno3(String filtertorituke_lotno3) {
        this.filtertorituke_lotno3 = filtertorituke_lotno3;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_取り付け本数3
     * @return the filtertorituke_toritukehonsuu3
     */
    public Integer getFiltertorituke_toritukehonsuu3() {
        return filtertorituke_toritukehonsuu3;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_取り付け本数3
     * @param filtertorituke_toritukehonsuu3 the filtertorituke_toritukehonsuu3 to set
     */
    public void setFiltertorituke_toritukehonsuu3(Integer filtertorituke_toritukehonsuu3) {
        this.filtertorituke_toritukehonsuu3 = filtertorituke_toritukehonsuu3;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_担当者
     * @return the filtertorituke_tantousya
     */
    public String getFiltertorituke_tantousya() {
        return filtertorituke_tantousya;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_担当者
     * @param filtertorituke_tantousya the filtertorituke_tantousya to set
     */
    public void setFiltertorituke_tantousya(String filtertorituke_tantousya) {
        this.filtertorituke_tantousya = filtertorituke_tantousya;
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
     * @return the filtersiyoukaisuu
     */
    public String getFiltersiyoukaisuu() {
        return filtersiyoukaisuu;
    }

    /**
     * ﾌｨﾙﾀｰ使用回数
     * @param filtersiyoukaisuu the filtersiyoukaisuu to set
     */
    public void setFiltersiyoukaisuu(String filtersiyoukaisuu) {
        this.filtersiyoukaisuu = filtersiyoukaisuu;
    }

    /**
     * F/PﾀﾝｸNo
     * @return the Fptankno
     */
    public String getFptankno() {
        return Fptankno;
    }

    /**
     * F/PﾀﾝｸNo
     * @param Fptankno the Fptankno to set
     */
    public void setFptankno(String Fptankno) {
        this.Fptankno = Fptankno;
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
     * @return the Fpkaisinichiji
     */
    public Timestamp getFpkaisinichiji() {
        return Fpkaisinichiji;
    }

    /**
     * F/P開始日時
     * @param Fpkaisinichiji the Fpkaisinichiji to set
     */
    public void setFpkaisinichiji(Timestamp Fpkaisinichiji) {
        this.Fpkaisinichiji = Fpkaisinichiji;
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
     * ﾌｨﾙﾀｰﾊﾟｽ開始_担当者
     * @return the filterpasskaisi_tantousya
     */
    public String getFilterpasskaisi_tantousya() {
        return filterpasskaisi_tantousya;
    }

    /**
     * ﾌｨﾙﾀｰﾊﾟｽ開始_担当者
     * @param filterpasskaisi_tantousya the filterpasskaisi_tantousya to set
     */
    public void setFilterpasskaisi_tantousya(String filterpasskaisi_tantousya) {
        this.filterpasskaisi_tantousya = filterpasskaisi_tantousya;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_F/P停止日時
     * @return the filterkoukan1_Fpteisinichiji
     */
    public Timestamp getFilterkoukan1_Fpteisinichiji() {
        return filterkoukan1_Fpteisinichiji;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_F/P停止日時
     * @param filterkoukan1_Fpteisinichiji the filterkoukan1_Fpteisinichiji to set
     */
    public void setFilterkoukan1_Fpteisinichiji(Timestamp filterkoukan1_Fpteisinichiji) {
        this.filterkoukan1_Fpteisinichiji = filterkoukan1_Fpteisinichiji;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_1次ﾌｨﾙﾀｰ品名
     * @return the filterkoukan1_itijifilterhinmei
     */
    public String getFilterkoukan1_itijifilterhinmei() {
        return filterkoukan1_itijifilterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_1次ﾌｨﾙﾀｰ品名
     * @param filterkoukan1_itijifilterhinmei the filterkoukan1_itijifilterhinmei to set
     */
    public void setFilterkoukan1_itijifilterhinmei(String filterkoukan1_itijifilterhinmei) {
        this.filterkoukan1_itijifilterhinmei = filterkoukan1_itijifilterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_LotNo1
     * @return the filterkoukan1_lotno1
     */
    public String getFilterkoukan1_lotno1() {
        return filterkoukan1_lotno1;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_LotNo1
     * @param filterkoukan1_lotno1 the filterkoukan1_lotno1 to set
     */
    public void setFilterkoukan1_lotno1(String filterkoukan1_lotno1) {
        this.filterkoukan1_lotno1 = filterkoukan1_lotno1;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_取り付け本数1
     * @return the filterkoukan1_toritukehonsuu1
     */
    public Integer getFilterkoukan1_toritukehonsuu1() {
        return filterkoukan1_toritukehonsuu1;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_取り付け本数1
     * @param filterkoukan1_toritukehonsuu1 the filterkoukan1_toritukehonsuu1 to set
     */
    public void setFilterkoukan1_toritukehonsuu1(Integer filterkoukan1_toritukehonsuu1) {
        this.filterkoukan1_toritukehonsuu1 = filterkoukan1_toritukehonsuu1;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_2次ﾌｨﾙﾀｰ品名
     * @return the filterkoukan1_nijifilterhinmei
     */
    public String getFilterkoukan1_nijifilterhinmei() {
        return filterkoukan1_nijifilterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_2次ﾌｨﾙﾀｰ品名
     * @param filterkoukan1_nijifilterhinmei the filterkoukan1_nijifilterhinmei to set
     */
    public void setFilterkoukan1_nijifilterhinmei(String filterkoukan1_nijifilterhinmei) {
        this.filterkoukan1_nijifilterhinmei = filterkoukan1_nijifilterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_LotNo2
     * @return the filterkoukan1_lotno2
     */
    public String getFilterkoukan1_lotno2() {
        return filterkoukan1_lotno2;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_LotNo2
     * @param filterkoukan1_lotno2 the filterkoukan1_lotno2 to set
     */
    public void setFilterkoukan1_lotno2(String filterkoukan1_lotno2) {
        this.filterkoukan1_lotno2 = filterkoukan1_lotno2;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_取り付け本数2
     * @return the filterkoukan1_toritukehonsuu2
     */
    public Integer getFilterkoukan1_toritukehonsuu2() {
        return filterkoukan1_toritukehonsuu2;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_取り付け本数2
     * @param filterkoukan1_toritukehonsuu2 the filterkoukan1_toritukehonsuu2 to set
     */
    public void setFilterkoukan1_toritukehonsuu2(Integer filterkoukan1_toritukehonsuu2) {
        this.filterkoukan1_toritukehonsuu2 = filterkoukan1_toritukehonsuu2;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_3次ﾌｨﾙﾀｰ品名
     * @return the filterkoukan1_sanjifilterhinmei
     */
    public String getFilterkoukan1_sanjifilterhinmei() {
        return filterkoukan1_sanjifilterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_3次ﾌｨﾙﾀｰ品名
     * @param filterkoukan1_sanjifilterhinmei the filterkoukan1_sanjifilterhinmei to set
     */
    public void setFilterkoukan1_sanjifilterhinmei(String filterkoukan1_sanjifilterhinmei) {
        this.filterkoukan1_sanjifilterhinmei = filterkoukan1_sanjifilterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_LotNo3
     * @return the filterkoukan1_lotno3
     */
    public String getFilterkoukan1_lotno3() {
        return filterkoukan1_lotno3;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_LotNo3
     * @param filterkoukan1_lotno3 the filterkoukan1_lotno3 to set
     */
    public void setFilterkoukan1_lotno3(String filterkoukan1_lotno3) {
        this.filterkoukan1_lotno3 = filterkoukan1_lotno3;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_取り付け本数3
     * @return the filterkoukan1_toritukehonsuu3
     */
    public Integer getFilterkoukan1_toritukehonsuu3() {
        return filterkoukan1_toritukehonsuu3;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_取り付け本数3
     * @param filterkoukan1_toritukehonsuu3 the filterkoukan1_toritukehonsuu3 to set
     */
    public void setFilterkoukan1_toritukehonsuu3(Integer filterkoukan1_toritukehonsuu3) {
        this.filterkoukan1_toritukehonsuu3 = filterkoukan1_toritukehonsuu3;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_F/P再開日時
     * @return the filterkoukan1_Fpsaikainichiji
     */
    public Timestamp getFilterkoukan1_Fpsaikainichiji() {
        return filterkoukan1_Fpsaikainichiji;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_F/P再開日時
     * @param filterkoukan1_Fpsaikainichiji the filterkoukan1_Fpsaikainichiji to set
     */
    public void setFilterkoukan1_Fpsaikainichiji(Timestamp filterkoukan1_Fpsaikainichiji) {
        this.filterkoukan1_Fpsaikainichiji = filterkoukan1_Fpsaikainichiji;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_担当者
     * @return the filterkoukan1_tantousya
     */
    public String getFilterkoukan1_tantousya() {
        return filterkoukan1_tantousya;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_担当者
     * @param filterkoukan1_tantousya the filterkoukan1_tantousya to set
     */
    public void setFilterkoukan1_tantousya(String filterkoukan1_tantousya) {
        this.filterkoukan1_tantousya = filterkoukan1_tantousya;
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
     * F/Pﾄｰﾀﾙ時間
     * @return the FPtotaljikan
     */
    public String getFPtotaljikan() {
        return FPtotaljikan;
    }

    /**
     * F/Pﾄｰﾀﾙ時間
     * @param FPtotaljikan the FPtotaljikan to set
     */
    public void setFPtotaljikan(String FPtotaljikan) {
        this.FPtotaljikan = FPtotaljikan;
    }

    /**
     * 1次ﾌｨﾙﾀｰ総使用本数
     * @return the itijifiltersousiyouhonsuu
     */
    public Integer getItijifiltersousiyouhonsuu() {
        return itijifiltersousiyouhonsuu;
    }

    /**
     * 1次ﾌｨﾙﾀｰ総使用本数
     * @param itijifiltersousiyouhonsuu the itijifiltersousiyouhonsuu to set
     */
    public void setItijifiltersousiyouhonsuu(Integer itijifiltersousiyouhonsuu) {
        this.itijifiltersousiyouhonsuu = itijifiltersousiyouhonsuu;
    }

    /**
     * 2次ﾌｨﾙﾀｰ総使用本数
     * @return the nijifiltersousiyouhonsuu
     */
    public Integer getNijifiltersousiyouhonsuu() {
        return nijifiltersousiyouhonsuu;
    }

    /**
     * 2次ﾌｨﾙﾀｰ総使用本数
     * @param nijifiltersousiyouhonsuu the nijifiltersousiyouhonsuu to set
     */
    public void setNijifiltersousiyouhonsuu(Integer nijifiltersousiyouhonsuu) {
        this.nijifiltersousiyouhonsuu = nijifiltersousiyouhonsuu;
    }

    /**
     * 3次ﾌｨﾙﾀｰ総使用本数
     * @return the sanjifiltersousiyouhonsuu
     */
    public Integer getSanjifiltersousiyouhonsuu() {
        return sanjifiltersousiyouhonsuu;
    }

    /**
     * 3次ﾌｨﾙﾀｰ総使用本数
     * @param sanjifiltersousiyouhonsuu the sanjifiltersousiyouhonsuu to set
     */
    public void setSanjifiltersousiyouhonsuu(Integer sanjifiltersousiyouhonsuu) {
        this.sanjifiltersousiyouhonsuu = sanjifiltersousiyouhonsuu;
    }

    /**
     * ﾌｨﾙﾀｰﾊﾟｽ終了_担当者
     * @return the filterpasssyuuryou_tantousya
     */
    public String getFilterpasssyuuryou_tantousya() {
        return filterpasssyuuryou_tantousya;
    }

    /**
     * ﾌｨﾙﾀｰﾊﾟｽ終了_担当者
     * @param filterpasssyuuryou_tantousya the filterpasssyuuryou_tantousya to set
     */
    public void setFilterpasssyuuryou_tantousya(String filterpasssyuuryou_tantousya) {
        this.filterpasssyuuryou_tantousya = filterpasssyuuryou_tantousya;
    }

    /**
     * 総重量測定1
     * @return the soujyuurousokutei1
     */
    public Integer getSoujyuurousokutei1() {
        return soujyuurousokutei1;
    }

    /**
     * 総重量測定1
     * @param soujyuurousokutei1 the soujyuurousokutei1 to set
     */
    public void setSoujyuurousokutei1(Integer soujyuurousokutei1) {
        this.soujyuurousokutei1 = soujyuurousokutei1;
    }

    /**
     * 総重量測定2
     * @return the soujyuurousokutei2
     */
    public Integer getSoujyuurousokutei2() {
        return soujyuurousokutei2;
    }

    /**
     * 総重量測定2
     * @param soujyuurousokutei2 the soujyuurousokutei2 to set
     */
    public void setSoujyuurousokutei2(Integer soujyuurousokutei2) {
        this.soujyuurousokutei2 = soujyuurousokutei2;
    }

    /**
     * 総重量測定3
     * @return the soujyuurousokutei3
     */
    public Integer getSoujyuurousokutei3() {
        return soujyuurousokutei3;
    }

    /**
     * 総重量測定3
     * @param soujyuurousokutei3 the soujyuurousokutei3 to set
     */
    public void setSoujyuurousokutei3(Integer soujyuurousokutei3) {
        this.soujyuurousokutei3 = soujyuurousokutei3;
    }

    /**
     * 総重量測定4
     * @return the soujyuurousokutei4
     */
    public Integer getSoujyuurousokutei4() {
        return soujyuurousokutei4;
    }

    /**
     * 総重量測定4
     * @param soujyuurousokutei4 the soujyuurousokutei4 to set
     */
    public void setSoujyuurousokutei4(Integer soujyuurousokutei4) {
        this.soujyuurousokutei4 = soujyuurousokutei4;
    }

    /**
     * 総重量測定5
     * @return the soujyuurousokutei5
     */
    public Integer getSoujyuurousokutei5() {
        return soujyuurousokutei5;
    }

    /**
     * 総重量測定5
     * @param soujyuurousokutei5 the soujyuurousokutei5 to set
     */
    public void setSoujyuurousokutei5(Integer soujyuurousokutei5) {
        this.soujyuurousokutei5 = soujyuurousokutei5;
    }

    /**
     * 総重量測定6
     * @return the soujyuurousokutei6
     */
    public Integer getSoujyuurousokutei6() {
        return soujyuurousokutei6;
    }

    /**
     * 総重量測定6
     * @param soujyuurousokutei6 the soujyuurousokutei6 to set
     */
    public void setSoujyuurousokutei6(Integer soujyuurousokutei6) {
        this.soujyuurousokutei6 = soujyuurousokutei6;
    }

    /**
     * 総重量測定7
     * @return the soujyuurousokutei7
     */
    public Integer getSoujyuurousokutei7() {
        return soujyuurousokutei7;
    }

    /**
     * 総重量測定7
     * @param soujyuurousokutei7 the soujyuurousokutei7 to set
     */
    public void setSoujyuurousokutei7(Integer soujyuurousokutei7) {
        this.soujyuurousokutei7 = soujyuurousokutei7;
    }

    /**
     * 総重量測定8
     * @return the soujyuurousokutei8
     */
    public Integer getSoujyuurousokutei8() {
        return soujyuurousokutei8;
    }

    /**
     * 総重量測定8
     * @param soujyuurousokutei8 the soujyuurousokutei8 to set
     */
    public void setSoujyuurousokutei8(Integer soujyuurousokutei8) {
        this.soujyuurousokutei8 = soujyuurousokutei8;
    }

    /**
     * 総重量測定9
     * @return the soujyuurousokutei9
     */
    public Integer getSoujyuurousokutei9() {
        return soujyuurousokutei9;
    }

    /**
     * 総重量測定9
     * @param soujyuurousokutei9 the soujyuurousokutei9 to set
     */
    public void setSoujyuurousokutei9(Integer soujyuurousokutei9) {
        this.soujyuurousokutei9 = soujyuurousokutei9;
    }

    /**
     * 総重量測定10
     * @return the soujyuurousokutei10
     */
    public Integer getSoujyuurousokutei10() {
        return soujyuurousokutei10;
    }

    /**
     * 総重量測定10
     * @param soujyuurousokutei10 the soujyuurousokutei10 to set
     */
    public void setSoujyuurousokutei10(Integer soujyuurousokutei10) {
        this.soujyuurousokutei10 = soujyuurousokutei10;
    }

    /**
     * 総重量測定11
     * @return the soujyuurousokutei11
     */
    public Integer getSoujyuurousokutei11() {
        return soujyuurousokutei11;
    }

    /**
     * 総重量測定11
     * @param soujyuurousokutei11 the soujyuurousokutei11 to set
     */
    public void setSoujyuurousokutei11(Integer soujyuurousokutei11) {
        this.soujyuurousokutei11 = soujyuurousokutei11;
    }

    /**
     * 総重量測定12
     * @return the soujyuurousokutei12
     */
    public Integer getSoujyuurousokutei12() {
        return soujyuurousokutei12;
    }

    /**
     * 総重量測定12
     * @param soujyuurousokutei12 the soujyuurousokutei12 to set
     */
    public void setSoujyuurousokutei12(Integer soujyuurousokutei12) {
        this.soujyuurousokutei12 = soujyuurousokutei12;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量1
     * @return the yuudentaislurryjyuurou1
     */
    public Integer getYuudentaislurryjyuurou1() {
        return yuudentaislurryjyuurou1;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量1
     * @param yuudentaislurryjyuurou1 the yuudentaislurryjyuurou1 to set
     */
    public void setYuudentaislurryjyuurou1(Integer yuudentaislurryjyuurou1) {
        this.yuudentaislurryjyuurou1 = yuudentaislurryjyuurou1;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量2
     * @return the yuudentaislurryjyuurou2
     */
    public Integer getYuudentaislurryjyuurou2() {
        return yuudentaislurryjyuurou2;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量2
     * @param yuudentaislurryjyuurou2 the yuudentaislurryjyuurou2 to set
     */
    public void setYuudentaislurryjyuurou2(Integer yuudentaislurryjyuurou2) {
        this.yuudentaislurryjyuurou2 = yuudentaislurryjyuurou2;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量3
     * @return the yuudentaislurryjyuurou3
     */
    public Integer getYuudentaislurryjyuurou3() {
        return yuudentaislurryjyuurou3;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量3
     * @param yuudentaislurryjyuurou3 the yuudentaislurryjyuurou3 to set
     */
    public void setYuudentaislurryjyuurou3(Integer yuudentaislurryjyuurou3) {
        this.yuudentaislurryjyuurou3 = yuudentaislurryjyuurou3;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量4
     * @return the yuudentaislurryjyuurou4
     */
    public Integer getYuudentaislurryjyuurou4() {
        return yuudentaislurryjyuurou4;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量4
     * @param yuudentaislurryjyuurou4 the yuudentaislurryjyuurou4 to set
     */
    public void setYuudentaislurryjyuurou4(Integer yuudentaislurryjyuurou4) {
        this.yuudentaislurryjyuurou4 = yuudentaislurryjyuurou4;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量5
     * @return the yuudentaislurryjyuurou5
     */
    public Integer getYuudentaislurryjyuurou5() {
        return yuudentaislurryjyuurou5;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量5
     * @param yuudentaislurryjyuurou5 the yuudentaislurryjyuurou5 to set
     */
    public void setYuudentaislurryjyuurou5(Integer yuudentaislurryjyuurou5) {
        this.yuudentaislurryjyuurou5 = yuudentaislurryjyuurou5;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量6
     * @return the yuudentaislurryjyuurou6
     */
    public Integer getYuudentaislurryjyuurou6() {
        return yuudentaislurryjyuurou6;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量6
     * @param yuudentaislurryjyuurou6 the yuudentaislurryjyuurou6 to set
     */
    public void setYuudentaislurryjyuurou6(Integer yuudentaislurryjyuurou6) {
        this.yuudentaislurryjyuurou6 = yuudentaislurryjyuurou6;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量7
     * @return the yuudentaislurryjyuurou7
     */
    public Integer getYuudentaislurryjyuurou7() {
        return yuudentaislurryjyuurou7;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量7
     * @param yuudentaislurryjyuurou7 the yuudentaislurryjyuurou7 to set
     */
    public void setYuudentaislurryjyuurou7(Integer yuudentaislurryjyuurou7) {
        this.yuudentaislurryjyuurou7 = yuudentaislurryjyuurou7;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量8
     * @return the yuudentaislurryjyuurou8
     */
    public Integer getYuudentaislurryjyuurou8() {
        return yuudentaislurryjyuurou8;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量8
     * @param yuudentaislurryjyuurou8 the yuudentaislurryjyuurou8 to set
     */
    public void setYuudentaislurryjyuurou8(Integer yuudentaislurryjyuurou8) {
        this.yuudentaislurryjyuurou8 = yuudentaislurryjyuurou8;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量9
     * @return the yuudentaislurryjyuurou9
     */
    public Integer getYuudentaislurryjyuurou9() {
        return yuudentaislurryjyuurou9;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量9
     * @param yuudentaislurryjyuurou9 the yuudentaislurryjyuurou9 to set
     */
    public void setYuudentaislurryjyuurou9(Integer yuudentaislurryjyuurou9) {
        this.yuudentaislurryjyuurou9 = yuudentaislurryjyuurou9;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量10
     * @return the yuudentaislurryjyuurou10
     */
    public Integer getYuudentaislurryjyuurou10() {
        return yuudentaislurryjyuurou10;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量10
     * @param yuudentaislurryjyuurou10 the yuudentaislurryjyuurou10 to set
     */
    public void setYuudentaislurryjyuurou10(Integer yuudentaislurryjyuurou10) {
        this.yuudentaislurryjyuurou10 = yuudentaislurryjyuurou10;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量11
     * @return the yuudentaislurryjyuurou11
     */
    public Integer getYuudentaislurryjyuurou11() {
        return yuudentaislurryjyuurou11;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量11
     * @param yuudentaislurryjyuurou11 the yuudentaislurryjyuurou11 to set
     */
    public void setYuudentaislurryjyuurou11(Integer yuudentaislurryjyuurou11) {
        this.yuudentaislurryjyuurou11 = yuudentaislurryjyuurou11;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量12
     * @return the yuudentaislurryjyuurou12
     */
    public Integer getYuudentaislurryjyuurou12() {
        return yuudentaislurryjyuurou12;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量12
     * @param yuudentaislurryjyuurou12 the yuudentaislurryjyuurou12 to set
     */
    public void setYuudentaislurryjyuurou12(Integer yuudentaislurryjyuurou12) {
        this.yuudentaislurryjyuurou12 = yuudentaislurryjyuurou12;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量合計
     * @return the yuudentaislurryjyuurougoukei
     */
    public Integer getYuudentaislurryjyuurougoukei() {
        return yuudentaislurryjyuurougoukei;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量合計
     * @param yuudentaislurryjyuurougoukei the yuudentaislurryjyuurougoukei to set
     */
    public void setYuudentaislurryjyuurougoukei(Integer yuudentaislurryjyuurougoukei) {
        this.yuudentaislurryjyuurougoukei = yuudentaislurryjyuurougoukei;
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
     * 歩留まり計算
     * @return the budomarikeisan
     */
    public BigDecimal getBudomarikeisan() {
        return budomarikeisan;
    }

    /**
     * 歩留まり計算
     * @param budomarikeisan the budomarikeisan to set
     */
    public void setBudomarikeisan(BigDecimal budomarikeisan) {
        this.budomarikeisan = budomarikeisan;
    }

    /**
     * 誘電体ｽﾗﾘｰ有効期限
     * @return the yuudentaislurryyuukoukigen
     */
    public String getYuudentaislurryyuukoukigen() {
        return yuudentaislurryyuukoukigen;
    }

    /**
     * 誘電体ｽﾗﾘｰ有効期限
     * @param yuudentaislurryyuukoukigen the yuudentaislurryyuukoukigen to set
     */
    public void setYuudentaislurryyuukoukigen(String yuudentaislurryyuukoukigen) {
        this.yuudentaislurryyuukoukigen = yuudentaislurryyuukoukigen;
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
     * 製品重量確認_担当者
     * @return the seihinjyuuryoukakunin_tantousya
     */
    public String getSeihinjyuuryoukakunin_tantousya() {
        return seihinjyuuryoukakunin_tantousya;
    }

    /**
     * 製品重量確認_担当者
     * @param seihinjyuuryoukakunin_tantousya the seihinjyuuryoukakunin_tantousya to set
     */
    public void setSeihinjyuuryoukakunin_tantousya(String seihinjyuuryoukakunin_tantousya) {
        this.seihinjyuuryoukakunin_tantousya = seihinjyuuryoukakunin_tantousya;
    }

    /**
     * 保存用ｻﾝﾌﾟﾙ回収
     * @return the hozonyousamplekaisyu
     */
    public Integer getHozonyousamplekaisyu() {
        return hozonyousamplekaisyu;
    }

    /**
     * 保存用ｻﾝﾌﾟﾙ回収
     * @param hozonyousamplekaisyu the hozonyousamplekaisyu to set
     */
    public void setHozonyousamplekaisyu(Integer hozonyousamplekaisyu) {
        this.hozonyousamplekaisyu = hozonyousamplekaisyu;
    }

    /**
     * 分析用ｻﾝﾌﾟﾙ回収
     * @return the zunsekiyousamplekaisyu
     */
    public Integer getZunsekiyousamplekaisyu() {
        return zunsekiyousamplekaisyu;
    }

    /**
     * 分析用ｻﾝﾌﾟﾙ回収
     * @param zunsekiyousamplekaisyu the zunsekiyousamplekaisyu to set
     */
    public void setZunsekiyousamplekaisyu(Integer zunsekiyousamplekaisyu) {
        this.zunsekiyousamplekaisyu = zunsekiyousamplekaisyu;
    }

    /**
     * 乾燥皿
     * @return the kansouzara
     */
    public String getKansouzara() {
        return kansouzara;
    }

    /**
     * 乾燥皿
     * @param kansouzara the kansouzara to set
     */
    public void setKansouzara(String kansouzara) {
        this.kansouzara = kansouzara;
    }

    /**
     * ｱﾙﾐ皿風袋重量
     * @return the arumizarafuutaijyuuryou
     */
    public Integer getArumizarafuutaijyuuryou() {
        return arumizarafuutaijyuuryou;
    }

    /**
     * ｱﾙﾐ皿風袋重量
     * @param arumizarafuutaijyuuryou the arumizarafuutaijyuuryou to set
     */
    public void setArumizarafuutaijyuuryou(Integer arumizarafuutaijyuuryou) {
        this.arumizarafuutaijyuuryou = arumizarafuutaijyuuryou;
    }

    /**
     * ｽﾗﾘｰｻﾝﾌﾟﾙ重量規格
     * @return the slurrysamplejyuuryoukikaku
     */
    public String getSlurrysamplejyuuryoukikaku() {
        return slurrysamplejyuuryoukikaku;
    }

    /**
     * ｽﾗﾘｰｻﾝﾌﾟﾙ重量規格
     * @param slurrysamplejyuuryoukikaku the slurrysamplejyuuryoukikaku to set
     */
    public void setSlurrysamplejyuuryoukikaku(String slurrysamplejyuuryoukikaku) {
        this.slurrysamplejyuuryoukikaku = slurrysamplejyuuryoukikaku;
    }

    /**
     * 乾燥前ｽﾗﾘｰ重量
     * @return the kansoumaeslurryjyuuryou
     */
    public Integer getKansoumaeslurryjyuuryou() {
        return kansoumaeslurryjyuuryou;
    }

    /**
     * 乾燥前ｽﾗﾘｰ重量
     * @param kansoumaeslurryjyuuryou the kansoumaeslurryjyuuryou to set
     */
    public void setKansoumaeslurryjyuuryou(Integer kansoumaeslurryjyuuryou) {
        this.kansoumaeslurryjyuuryou = kansoumaeslurryjyuuryou;
    }

    /**
     * 乾燥機
     * @return the kansouki
     */
    public String getKansouki() {
        return kansouki;
    }

    /**
     * 乾燥機
     * @param kansouki the kansouki to set
     */
    public void setKansouki(String kansouki) {
        this.kansouki = kansouki;
    }

    /**
     * 乾燥温度規格
     * @return the kansouondokikaku
     */
    public String getKansouondokikaku() {
        return kansouondokikaku;
    }

    /**
     * 乾燥温度規格
     * @param kansouondokikaku the kansouondokikaku to set
     */
    public void setKansouondokikaku(String kansouondokikaku) {
        this.kansouondokikaku = kansouondokikaku;
    }

    /**
     * 乾燥時間規格
     * @return the kansoujikankikaku
     */
    public String getKansoujikankikaku() {
        return kansoujikankikaku;
    }

    /**
     * 乾燥時間規格
     * @param kansoujikankikaku the kansoujikankikaku to set
     */
    public void setKansoujikankikaku(String kansoujikankikaku) {
        this.kansoujikankikaku = kansoujikankikaku;
    }

    /**
     * 乾燥開始日時
     * @return the kansoukaisinichiji
     */
    public Timestamp getKansoukaisinichiji() {
        return kansoukaisinichiji;
    }

    /**
     * 乾燥開始日時
     * @param kansoukaisinichiji the kansoukaisinichiji to set
     */
    public void setKansoukaisinichiji(Timestamp kansoukaisinichiji) {
        this.kansoukaisinichiji = kansoukaisinichiji;
    }

    /**
     * 乾燥終了日時
     * @return the kansousyuuryounichiji
     */
    public Timestamp getKansousyuuryounichiji() {
        return kansousyuuryounichiji;
    }

    /**
     * 乾燥終了日時
     * @param kansousyuuryounichiji the kansousyuuryounichiji to set
     */
    public void setKansousyuuryounichiji(Timestamp kansousyuuryounichiji) {
        this.kansousyuuryounichiji = kansousyuuryounichiji;
    }

    /**
     * 乾燥時間ﾄｰﾀﾙ
     * @return the kansoujikantotal
     */
    public String getKansoujikantotal() {
        return kansoujikantotal;
    }

    /**
     * 乾燥時間ﾄｰﾀﾙ
     * @param kansoujikantotal the kansoujikantotal to set
     */
    public void setKansoujikantotal(String kansoujikantotal) {
        this.kansoujikantotal = kansoujikantotal;
    }

    /**
     * 乾燥後総重量
     * @return the kansougosoujyuuryou
     */
    public BigDecimal getKansougosoujyuuryou() {
        return kansougosoujyuuryou;
    }

    /**
     * 乾燥後総重量
     * @param kansougosoujyuuryou the kansougosoujyuuryou to set
     */
    public void setKansougosoujyuuryou(BigDecimal kansougosoujyuuryou) {
        this.kansougosoujyuuryou = kansougosoujyuuryou;
    }

    /**
     * 乾燥後正味重量
     * @return the kansougosyoumijyuuryou
     */
    public BigDecimal getKansougosyoumijyuuryou() {
        return kansougosyoumijyuuryou;
    }

    /**
     * 乾燥後正味重量
     * @param kansougosyoumijyuuryou the kansougosyoumijyuuryou to set
     */
    public void setKansougosyoumijyuuryou(BigDecimal kansougosyoumijyuuryou) {
        this.kansougosyoumijyuuryou = kansougosyoumijyuuryou;
    }

    /**
     * 固形分比率
     * @return the kokeibunhiritu
     */
    public BigDecimal getKokeibunhiritu() {
        return kokeibunhiritu;
    }

    /**
     * 固形分比率
     * @param kokeibunhiritu the kokeibunhiritu to set
     */
    public void setKokeibunhiritu(BigDecimal kokeibunhiritu) {
        this.kokeibunhiritu = kokeibunhiritu;
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