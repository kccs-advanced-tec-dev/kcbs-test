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
 * 変更日	2021/11/14<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B026(誘電体ｽﾗﾘｰ作製・ﾌｨﾙﾀｰﾊﾟｽ・保管)
 *
 * @author KCSS K.Jo
 * @since  2021/11/14
 */
@ViewScoped
@Named
public class GXHDO102B026A implements Serializable {
    /**
     * WIPﾛｯﾄNo
     */
    private FXHDD01 wiplotno;

    /**
     * 誘電体ｽﾗﾘｰ品名
     */
    private FXHDD01 yuudentaislurryhinmei;

    /**
     * 誘電体ｽﾗﾘｰLotNo
     */
    private FXHDD01 yuudentaislurrylotno;

    /**
     * ﾛｯﾄ区分
     */
    private FXHDD01 lotkubun;

    /**
     * 原料LotNo
     */
    private FXHDD01 genryoulotno;

    /**
     * 原料記号
     */
    private FXHDD01 genryoukigou;

    /**
     * 風袋重量測定_ｽﾃﾝ容器1
     */
    private FXHDD01 fuutaijyuuryousokutei_sutenyouki1;

    /**
     * 風袋重量測定_ｽﾃﾝ容器2
     */
    private FXHDD01 fuutaijyuuryousokutei_sutenyouki2;

    /**
     * 風袋重量測定_ｽﾃﾝ容器3
     */
    private FXHDD01 fuutaijyuuryousokutei_sutenyouki3;

    /**
     * 風袋重量測定_ｽﾃﾝ容器4
     */
    private FXHDD01 fuutaijyuuryousokutei_sutenyouki4;

    /**
     * 風袋重量測定_ｽﾃﾝ容器5
     */
    private FXHDD01 fuutaijyuuryousokutei_sutenyouki5;

    /**
     * 風袋重量測定_ｽﾃﾝ容器6
     */
    private FXHDD01 fuutaijyuuryousokutei_sutenyouki6;

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器1
     */
    private FXHDD01 fuutaijyuuryousokutei_siropori1;

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器2
     */
    private FXHDD01 fuutaijyuuryousokutei_siropori2;

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器3
     */
    private FXHDD01 fuutaijyuuryousokutei_siropori3;

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器4
     */
    private FXHDD01 fuutaijyuuryousokutei_siropori4;

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器5
     */
    private FXHDD01 fuutaijyuuryousokutei_siropori5;

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器6
     */
    private FXHDD01 fuutaijyuuryousokutei_siropori6;

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器7
     */
    private FXHDD01 fuutaijyuuryousokutei_siropori7;

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器8
     */
    private FXHDD01 fuutaijyuuryousokutei_siropori8;

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器9
     */
    private FXHDD01 fuutaijyuuryousokutei_siropori9;

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器10
     */
    private FXHDD01 fuutaijyuuryousokutei_siropori10;

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器11
     */
    private FXHDD01 fuutaijyuuryousokutei_siropori11;

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器12
     */
    private FXHDD01 fuutaijyuuryousokutei_siropori12;

    /**
     * 保管容器準備_担当者
     */
    private FXHDD01 fuutaijyuuryousokutei_tantousya;

    /**
     * ﾌｨﾙﾀｰ連結
     */
    private FXHDD01 filterrenketu;

    /**
     * ﾌｨﾙﾀｰ取り付け_1次ﾌｨﾙﾀｰ品名
     */
    private FXHDD01 filtertorituke_itijifilterhinmei;

    /**
     * ﾌｨﾙﾀｰ取り付け_LotNo1
     */
    private FXHDD01 filtertorituke_lotno1;

    /**
     * ﾌｨﾙﾀｰ取り付け_取り付け本数1
     */
    private FXHDD01 filtertorituke_toritukehonsuu1;

    /**
     * ﾌｨﾙﾀｰ取り付け_2次ﾌｨﾙﾀｰ品名
     */
    private FXHDD01 filtertorituke_nijifilterhinmei;

    /**
     * ﾌｨﾙﾀｰ取り付け_LotNo2
     */
    private FXHDD01 filtertorituke_lotno2;

    /**
     * ﾌｨﾙﾀｰ取り付け_取り付け本数2
     */
    private FXHDD01 filtertorituke_toritukehonsuu2;

    /**
     * ﾌｨﾙﾀｰ取り付け_3次ﾌｨﾙﾀｰ品名
     */
    private FXHDD01 filtertorituke_sanjifilterhinmei;

    /**
     * ﾌｨﾙﾀｰ取り付け_LotNo3
     */
    private FXHDD01 filtertorituke_lotno3;

    /**
     * ﾌｨﾙﾀｰ取り付け_取り付け本数3
     */
    private FXHDD01 filtertorituke_toritukehonsuu3;

    /**
     * ﾌｨﾙﾀｰ取り付け_担当者
     */
    private FXHDD01 filtertorituke_tantousya;

    /**
     * 排出容器の内袋
     */
    private FXHDD01 haisyutuyoukinoutibukuro;

    /**
     * ﾌｨﾙﾀｰ使用回数
     */
    private FXHDD01 filtersiyoukaisuu;

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
     * ﾌｨﾙﾀｰﾊﾟｽ開始_担当者
     */
    private FXHDD01 filterpasskaisi_tantousya;

    /**
     * ﾌｨﾙﾀｰ交換①_F/P停止日
     */
    private FXHDD01 filterkoukan1_fpteisi_day;

    /**
     * ﾌｨﾙﾀｰ交換①_F/P停止時間
     */
    private FXHDD01 filterkoukan1_fpteisi_time;

    /**
     * ﾌｨﾙﾀｰ交換①_1次ﾌｨﾙﾀｰ品名
     */
    private FXHDD01 filterkoukan1_itijifilterhinmei;

    /**
     * ﾌｨﾙﾀｰ交換①_LotNo1
     */
    private FXHDD01 filterkoukan1_lotno1;

    /**
     * ﾌｨﾙﾀｰ交換①_取り付け本数1
     */
    private FXHDD01 filterkoukan1_toritukehonsuu1;

    /**
     * ﾌｨﾙﾀｰ交換①_2次ﾌｨﾙﾀｰ品名
     */
    private FXHDD01 filterkoukan1_nijifilterhinmei;

    /**
     * ﾌｨﾙﾀｰ交換①_LotNo2
     */
    private FXHDD01 filterkoukan1_lotno2;

    /**
     * ﾌｨﾙﾀｰ交換①_取り付け本数2
     */
    private FXHDD01 filterkoukan1_toritukehonsuu2;

    /**
     * ﾌｨﾙﾀｰ交換①_3次ﾌｨﾙﾀｰ品名
     */
    private FXHDD01 filterkoukan1_sanjifilterhinmei;

    /**
     * ﾌｨﾙﾀｰ交換①_LotNo3
     */
    private FXHDD01 filterkoukan1_lotno3;

    /**
     * ﾌｨﾙﾀｰ交換①_取り付け本数3
     */
    private FXHDD01 filterkoukan1_toritukehonsuu3;

    /**
     * ﾌｨﾙﾀｰ交換①_F/P再開日
     */
    private FXHDD01 filterkoukan1_fpsaikai_day;

    /**
     * ﾌｨﾙﾀｰ交換①_F/P再開時間
     */
    private FXHDD01 filterkoukan1_fpsaikai_time;

    /**
     * ﾌｨﾙﾀｰ交換①_担当者
     */
    private FXHDD01 filterkoukan1_tantousya;

    /**
     * F/P終了日
     */
    private FXHDD01 fpsyuuryou_day;

    /**
     * F/P終了時間
     */
    private FXHDD01 fpsyuuryou_time;

    /**
     * F/Pﾄｰﾀﾙ時間
     */
    private FXHDD01 fptotaljikan;

    /**
     * 1次ﾌｨﾙﾀｰ総使用本数
     */
    private FXHDD01 itijifiltersousiyouhonsuu;

    /**
     * 2次ﾌｨﾙﾀｰ総使用本数
     */
    private FXHDD01 nijifiltersousiyouhonsuu;

    /**
     * 3次ﾌｨﾙﾀｰ総使用本数
     */
    private FXHDD01 sanjifiltersousiyouhonsuu;

    /**
     * ﾌｨﾙﾀｰﾊﾟｽ終了_担当者
     */
    private FXHDD01 filterpasssyuuryou_tantousya;

    /**
     * 総重量測定1
     */
    private FXHDD01 soujyuurousokutei1;

    /**
     * 総重量測定2
     */
    private FXHDD01 soujyuurousokutei2;

    /**
     * 総重量測定3
     */
    private FXHDD01 soujyuurousokutei3;

    /**
     * 総重量測定4
     */
    private FXHDD01 soujyuurousokutei4;

    /**
     * 総重量測定5
     */
    private FXHDD01 soujyuurousokutei5;

    /**
     * 総重量測定6
     */
    private FXHDD01 soujyuurousokutei6;

    /**
     * 総重量測定7
     */
    private FXHDD01 soujyuurousokutei7;

    /**
     * 総重量測定8
     */
    private FXHDD01 soujyuurousokutei8;

    /**
     * 総重量測定9
     */
    private FXHDD01 soujyuurousokutei9;

    /**
     * 総重量測定10
     */
    private FXHDD01 soujyuurousokutei10;

    /**
     * 総重量測定11
     */
    private FXHDD01 soujyuurousokutei11;

    /**
     * 総重量測定12
     */
    private FXHDD01 soujyuurousokutei12;

    /**
     * 誘電体ｽﾗﾘｰ重量1
     */
    private FXHDD01 yuudentaislurryjyuurou1;

    /**
     * 誘電体ｽﾗﾘｰ重量2
     */
    private FXHDD01 yuudentaislurryjyuurou2;

    /**
     * 誘電体ｽﾗﾘｰ重量3
     */
    private FXHDD01 yuudentaislurryjyuurou3;

    /**
     * 誘電体ｽﾗﾘｰ重量4
     */
    private FXHDD01 yuudentaislurryjyuurou4;

    /**
     * 誘電体ｽﾗﾘｰ重量5
     */
    private FXHDD01 yuudentaislurryjyuurou5;

    /**
     * 誘電体ｽﾗﾘｰ重量6
     */
    private FXHDD01 yuudentaislurryjyuurou6;

    /**
     * 誘電体ｽﾗﾘｰ重量7
     */
    private FXHDD01 yuudentaislurryjyuurou7;

    /**
     * 誘電体ｽﾗﾘｰ重量8
     */
    private FXHDD01 yuudentaislurryjyuurou8;

    /**
     * 誘電体ｽﾗﾘｰ重量9
     */
    private FXHDD01 yuudentaislurryjyuurou9;

    /**
     * 誘電体ｽﾗﾘｰ重量10
     */
    private FXHDD01 yuudentaislurryjyuurou10;

    /**
     * 誘電体ｽﾗﾘｰ重量11
     */
    private FXHDD01 yuudentaislurryjyuurou11;

    /**
     * 誘電体ｽﾗﾘｰ重量12
     */
    private FXHDD01 yuudentaislurryjyuurou12;

    /**
     * 誘電体ｽﾗﾘｰ重量合計
     */
    private FXHDD01 yuudentaislurryjyuurougoukei;

    /**
     * 投入量
     */
    private FXHDD01 tounyuuryou;

    /**
     * 歩留まり計算
     */
    private FXHDD01 budomarikeisan;

    /**
     * 誘電体ｽﾗﾘｰ有効期限
     */
    private FXHDD01 yuudentaislurryyuukoukigen;

    /**
     * 粉砕判定
     */
    private FXHDD01 funsaihantei;

    /**
     * 製品重量確認_担当者
     */
    private FXHDD01 seihinjyuuryoukakunin_tantousya;

    /**
     * 保存用ｻﾝﾌﾟﾙ回収
     */
    private FXHDD01 hozonyousamplekaisyu;

    /**
     * 分析用ｻﾝﾌﾟﾙ回収
     */
    private FXHDD01 zunsekiyousamplekaisyu;

    /**
     * 乾燥皿
     */
    private FXHDD01 kansouzara;

    /**
     * ｱﾙﾐ皿風袋重量
     */
    private FXHDD01 arumizarafuutaijyuuryou;

    /**
     * ｽﾗﾘｰｻﾝﾌﾟﾙ重量規格
     */
    private FXHDD01 slurrysamplejyuuryoukikaku;

    /**
     * 乾燥前ｽﾗﾘｰ重量
     */
    private FXHDD01 kansoumaeslurryjyuuryou;

    /**
     * 乾燥機
     */
    private FXHDD01 kansouki;

    /**
     * 乾燥温度規格
     */
    private FXHDD01 kansouondokikaku;

    /**
     * 乾燥時間規格
     */
    private FXHDD01 kansoujikankikaku;

    /**
     * 乾燥開始日
     */
    private FXHDD01 kansoukaisi_day;

    /**
     * 乾燥開始時間
     */
    private FXHDD01 kansoukaisi_time;

    /**
     * 乾燥終了日
     */
    private FXHDD01 kansousyuuryou_day;

    /**
     * 乾燥終了時間
     */
    private FXHDD01 kansousyuuryou_time;

    /**
     * 乾燥時間ﾄｰﾀﾙ
     */
    private FXHDD01 kansoujikantotal;

    /**
     * 乾燥後総重量
     */
    private FXHDD01 kansougosoujyuuryou;

    /**
     * 乾燥後正味重量
     */
    private FXHDD01 kansougosyoumijyuuryou;

    /**
     * 固形分比率
     */
    private FXHDD01 kokeibunhiritu;

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
    public GXHDO102B026A() {
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
     * 誘電体ｽﾗﾘｰ品名
     * @return the yuudentaislurryhinmei
     */
    public FXHDD01 getYuudentaislurryhinmei() {
        return yuudentaislurryhinmei;
    }

    /**
     * 誘電体ｽﾗﾘｰ品名
     * @param yuudentaislurryhinmei the yuudentaislurryhinmei to set
     */
    public void setYuudentaislurryhinmei(FXHDD01 yuudentaislurryhinmei) {
        this.yuudentaislurryhinmei = yuudentaislurryhinmei;
    }

    /**
     * 誘電体ｽﾗﾘｰLotNo
     * @return the yuudentaislurrylotno
     */
    public FXHDD01 getYuudentaislurrylotno() {
        return yuudentaislurrylotno;
    }

    /**
     * 誘電体ｽﾗﾘｰLotNo
     * @param yuudentaislurrylotno the yuudentaislurrylotno to set
     */
    public void setYuudentaislurrylotno(FXHDD01 yuudentaislurrylotno) {
        this.yuudentaislurrylotno = yuudentaislurrylotno;
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
     * 原料LotNo
     * @return the genryoulotno
     */
    public FXHDD01 getGenryoulotno() {
        return genryoulotno;
    }

    /**
     * 原料LotNo
     * @param genryoulotno the genryoulotno to set
     */
    public void setGenryoulotno(FXHDD01 genryoulotno) {
        this.genryoulotno = genryoulotno;
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
     * 風袋重量測定_ｽﾃﾝ容器1
     * @return the fuutaijyuuryousokutei_sutenyouki1
     */
    public FXHDD01 getFuutaijyuuryousokutei_sutenyouki1() {
        return fuutaijyuuryousokutei_sutenyouki1;
    }

    /**
     * 風袋重量測定_ｽﾃﾝ容器1
     * @param fuutaijyuuryousokutei_sutenyouki1 the fuutaijyuuryousokutei_sutenyouki1 to set
     */
    public void setFuutaijyuuryousokutei_sutenyouki1(FXHDD01 fuutaijyuuryousokutei_sutenyouki1) {
        this.fuutaijyuuryousokutei_sutenyouki1 = fuutaijyuuryousokutei_sutenyouki1;
    }

    /**
     * 風袋重量測定_ｽﾃﾝ容器2
     * @return the fuutaijyuuryousokutei_sutenyouki2
     */
    public FXHDD01 getFuutaijyuuryousokutei_sutenyouki2() {
        return fuutaijyuuryousokutei_sutenyouki2;
    }

    /**
     * 風袋重量測定_ｽﾃﾝ容器2
     * @param fuutaijyuuryousokutei_sutenyouki2 the fuutaijyuuryousokutei_sutenyouki2 to set
     */
    public void setFuutaijyuuryousokutei_sutenyouki2(FXHDD01 fuutaijyuuryousokutei_sutenyouki2) {
        this.fuutaijyuuryousokutei_sutenyouki2 = fuutaijyuuryousokutei_sutenyouki2;
    }

    /**
     * 風袋重量測定_ｽﾃﾝ容器3
     * @return the fuutaijyuuryousokutei_sutenyouki3
     */
    public FXHDD01 getFuutaijyuuryousokutei_sutenyouki3() {
        return fuutaijyuuryousokutei_sutenyouki3;
    }

    /**
     * 風袋重量測定_ｽﾃﾝ容器3
     * @param fuutaijyuuryousokutei_sutenyouki3 the fuutaijyuuryousokutei_sutenyouki3 to set
     */
    public void setFuutaijyuuryousokutei_sutenyouki3(FXHDD01 fuutaijyuuryousokutei_sutenyouki3) {
        this.fuutaijyuuryousokutei_sutenyouki3 = fuutaijyuuryousokutei_sutenyouki3;
    }

    /**
     * 風袋重量測定_ｽﾃﾝ容器4
     * @return the fuutaijyuuryousokutei_sutenyouki4
     */
    public FXHDD01 getFuutaijyuuryousokutei_sutenyouki4() {
        return fuutaijyuuryousokutei_sutenyouki4;
    }

    /**
     * 風袋重量測定_ｽﾃﾝ容器4
     * @param fuutaijyuuryousokutei_sutenyouki4 the fuutaijyuuryousokutei_sutenyouki4 to set
     */
    public void setFuutaijyuuryousokutei_sutenyouki4(FXHDD01 fuutaijyuuryousokutei_sutenyouki4) {
        this.fuutaijyuuryousokutei_sutenyouki4 = fuutaijyuuryousokutei_sutenyouki4;
    }

    /**
     * 風袋重量測定_ｽﾃﾝ容器5
     * @return the fuutaijyuuryousokutei_sutenyouki5
     */
    public FXHDD01 getFuutaijyuuryousokutei_sutenyouki5() {
        return fuutaijyuuryousokutei_sutenyouki5;
    }

    /**
     * 風袋重量測定_ｽﾃﾝ容器5
     * @param fuutaijyuuryousokutei_sutenyouki5 the fuutaijyuuryousokutei_sutenyouki5 to set
     */
    public void setFuutaijyuuryousokutei_sutenyouki5(FXHDD01 fuutaijyuuryousokutei_sutenyouki5) {
        this.fuutaijyuuryousokutei_sutenyouki5 = fuutaijyuuryousokutei_sutenyouki5;
    }

    /**
     * 風袋重量測定_ｽﾃﾝ容器6
     * @return the fuutaijyuuryousokutei_sutenyouki6
     */
    public FXHDD01 getFuutaijyuuryousokutei_sutenyouki6() {
        return fuutaijyuuryousokutei_sutenyouki6;
    }

    /**
     * 風袋重量測定_ｽﾃﾝ容器6
     * @param fuutaijyuuryousokutei_sutenyouki6 the fuutaijyuuryousokutei_sutenyouki6 to set
     */
    public void setFuutaijyuuryousokutei_sutenyouki6(FXHDD01 fuutaijyuuryousokutei_sutenyouki6) {
        this.fuutaijyuuryousokutei_sutenyouki6 = fuutaijyuuryousokutei_sutenyouki6;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器1
     * @return the fuutaijyuuryousokutei_siropori1
     */
    public FXHDD01 getFuutaijyuuryousokutei_siropori1() {
        return fuutaijyuuryousokutei_siropori1;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器1
     * @param fuutaijyuuryousokutei_siropori1 the fuutaijyuuryousokutei_siropori1 to set
     */
    public void setFuutaijyuuryousokutei_siropori1(FXHDD01 fuutaijyuuryousokutei_siropori1) {
        this.fuutaijyuuryousokutei_siropori1 = fuutaijyuuryousokutei_siropori1;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器2
     * @return the fuutaijyuuryousokutei_siropori2
     */
    public FXHDD01 getFuutaijyuuryousokutei_siropori2() {
        return fuutaijyuuryousokutei_siropori2;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器2
     * @param fuutaijyuuryousokutei_siropori2 the fuutaijyuuryousokutei_siropori2 to set
     */
    public void setFuutaijyuuryousokutei_siropori2(FXHDD01 fuutaijyuuryousokutei_siropori2) {
        this.fuutaijyuuryousokutei_siropori2 = fuutaijyuuryousokutei_siropori2;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器3
     * @return the fuutaijyuuryousokutei_siropori3
     */
    public FXHDD01 getFuutaijyuuryousokutei_siropori3() {
        return fuutaijyuuryousokutei_siropori3;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器3
     * @param fuutaijyuuryousokutei_siropori3 the fuutaijyuuryousokutei_siropori3 to set
     */
    public void setFuutaijyuuryousokutei_siropori3(FXHDD01 fuutaijyuuryousokutei_siropori3) {
        this.fuutaijyuuryousokutei_siropori3 = fuutaijyuuryousokutei_siropori3;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器4
     * @return the fuutaijyuuryousokutei_siropori4
     */
    public FXHDD01 getFuutaijyuuryousokutei_siropori4() {
        return fuutaijyuuryousokutei_siropori4;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器4
     * @param fuutaijyuuryousokutei_siropori4 the fuutaijyuuryousokutei_siropori4 to set
     */
    public void setFuutaijyuuryousokutei_siropori4(FXHDD01 fuutaijyuuryousokutei_siropori4) {
        this.fuutaijyuuryousokutei_siropori4 = fuutaijyuuryousokutei_siropori4;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器5
     * @return the fuutaijyuuryousokutei_siropori5
     */
    public FXHDD01 getFuutaijyuuryousokutei_siropori5() {
        return fuutaijyuuryousokutei_siropori5;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器5
     * @param fuutaijyuuryousokutei_siropori5 the fuutaijyuuryousokutei_siropori5 to set
     */
    public void setFuutaijyuuryousokutei_siropori5(FXHDD01 fuutaijyuuryousokutei_siropori5) {
        this.fuutaijyuuryousokutei_siropori5 = fuutaijyuuryousokutei_siropori5;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器6
     * @return the fuutaijyuuryousokutei_siropori6
     */
    public FXHDD01 getFuutaijyuuryousokutei_siropori6() {
        return fuutaijyuuryousokutei_siropori6;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器6
     * @param fuutaijyuuryousokutei_siropori6 the fuutaijyuuryousokutei_siropori6 to set
     */
    public void setFuutaijyuuryousokutei_siropori6(FXHDD01 fuutaijyuuryousokutei_siropori6) {
        this.fuutaijyuuryousokutei_siropori6 = fuutaijyuuryousokutei_siropori6;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器7
     * @return the fuutaijyuuryousokutei_siropori7
     */
    public FXHDD01 getFuutaijyuuryousokutei_siropori7() {
        return fuutaijyuuryousokutei_siropori7;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器7
     * @param fuutaijyuuryousokutei_siropori7 the fuutaijyuuryousokutei_siropori7 to set
     */
    public void setFuutaijyuuryousokutei_siropori7(FXHDD01 fuutaijyuuryousokutei_siropori7) {
        this.fuutaijyuuryousokutei_siropori7 = fuutaijyuuryousokutei_siropori7;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器8
     * @return the fuutaijyuuryousokutei_siropori8
     */
    public FXHDD01 getFuutaijyuuryousokutei_siropori8() {
        return fuutaijyuuryousokutei_siropori8;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器8
     * @param fuutaijyuuryousokutei_siropori8 the fuutaijyuuryousokutei_siropori8 to set
     */
    public void setFuutaijyuuryousokutei_siropori8(FXHDD01 fuutaijyuuryousokutei_siropori8) {
        this.fuutaijyuuryousokutei_siropori8 = fuutaijyuuryousokutei_siropori8;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器9
     * @return the fuutaijyuuryousokutei_siropori9
     */
    public FXHDD01 getFuutaijyuuryousokutei_siropori9() {
        return fuutaijyuuryousokutei_siropori9;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器9
     * @param fuutaijyuuryousokutei_siropori9 the fuutaijyuuryousokutei_siropori9 to set
     */
    public void setFuutaijyuuryousokutei_siropori9(FXHDD01 fuutaijyuuryousokutei_siropori9) {
        this.fuutaijyuuryousokutei_siropori9 = fuutaijyuuryousokutei_siropori9;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器10
     * @return the fuutaijyuuryousokutei_siropori10
     */
    public FXHDD01 getFuutaijyuuryousokutei_siropori10() {
        return fuutaijyuuryousokutei_siropori10;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器10
     * @param fuutaijyuuryousokutei_siropori10 the fuutaijyuuryousokutei_siropori10 to set
     */
    public void setFuutaijyuuryousokutei_siropori10(FXHDD01 fuutaijyuuryousokutei_siropori10) {
        this.fuutaijyuuryousokutei_siropori10 = fuutaijyuuryousokutei_siropori10;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器11
     * @return the fuutaijyuuryousokutei_siropori11
     */
    public FXHDD01 getFuutaijyuuryousokutei_siropori11() {
        return fuutaijyuuryousokutei_siropori11;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器11
     * @param fuutaijyuuryousokutei_siropori11 the fuutaijyuuryousokutei_siropori11 to set
     */
    public void setFuutaijyuuryousokutei_siropori11(FXHDD01 fuutaijyuuryousokutei_siropori11) {
        this.fuutaijyuuryousokutei_siropori11 = fuutaijyuuryousokutei_siropori11;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器12
     * @return the fuutaijyuuryousokutei_siropori12
     */
    public FXHDD01 getFuutaijyuuryousokutei_siropori12() {
        return fuutaijyuuryousokutei_siropori12;
    }

    /**
     * 風袋重量測定_白ﾎﾟﾘ容器12
     * @param fuutaijyuuryousokutei_siropori12 the fuutaijyuuryousokutei_siropori12 to set
     */
    public void setFuutaijyuuryousokutei_siropori12(FXHDD01 fuutaijyuuryousokutei_siropori12) {
        this.fuutaijyuuryousokutei_siropori12 = fuutaijyuuryousokutei_siropori12;
    }

    /**
     * 保管容器準備_担当者
     * @return the fuutaijyuuryousokutei_tantousya
     */
    public FXHDD01 getFuutaijyuuryousokutei_tantousya() {
        return fuutaijyuuryousokutei_tantousya;
    }

    /**
     * 保管容器準備_担当者
     * @param fuutaijyuuryousokutei_tantousya the fuutaijyuuryousokutei_tantousya to set
     */
    public void setFuutaijyuuryousokutei_tantousya(FXHDD01 fuutaijyuuryousokutei_tantousya) {
        this.fuutaijyuuryousokutei_tantousya = fuutaijyuuryousokutei_tantousya;
    }

    /**
     * ﾌｨﾙﾀｰ連結
     * @return the filterrenketu
     */
    public FXHDD01 getFilterrenketu() {
        return filterrenketu;
    }

    /**
     * ﾌｨﾙﾀｰ連結
     * @param filterrenketu the filterrenketu to set
     */
    public void setFilterrenketu(FXHDD01 filterrenketu) {
        this.filterrenketu = filterrenketu;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_1次ﾌｨﾙﾀｰ品名
     * @return the filtertorituke_itijifilterhinmei
     */
    public FXHDD01 getFiltertorituke_itijifilterhinmei() {
        return filtertorituke_itijifilterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_1次ﾌｨﾙﾀｰ品名
     * @param filtertorituke_itijifilterhinmei the filtertorituke_itijifilterhinmei to set
     */
    public void setFiltertorituke_itijifilterhinmei(FXHDD01 filtertorituke_itijifilterhinmei) {
        this.filtertorituke_itijifilterhinmei = filtertorituke_itijifilterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_LotNo1
     * @return the filtertorituke_lotno1
     */
    public FXHDD01 getFiltertorituke_lotno1() {
        return filtertorituke_lotno1;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_LotNo1
     * @param filtertorituke_lotno1 the filtertorituke_lotno1 to set
     */
    public void setFiltertorituke_lotno1(FXHDD01 filtertorituke_lotno1) {
        this.filtertorituke_lotno1 = filtertorituke_lotno1;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_取り付け本数1
     * @return the filtertorituke_toritukehonsuu1
     */
    public FXHDD01 getFiltertorituke_toritukehonsuu1() {
        return filtertorituke_toritukehonsuu1;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_取り付け本数1
     * @param filtertorituke_toritukehonsuu1 the filtertorituke_toritukehonsuu1 to set
     */
    public void setFiltertorituke_toritukehonsuu1(FXHDD01 filtertorituke_toritukehonsuu1) {
        this.filtertorituke_toritukehonsuu1 = filtertorituke_toritukehonsuu1;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_2次ﾌｨﾙﾀｰ品名
     * @return the filtertorituke_nijifilterhinmei
     */
    public FXHDD01 getFiltertorituke_nijifilterhinmei() {
        return filtertorituke_nijifilterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_2次ﾌｨﾙﾀｰ品名
     * @param filtertorituke_nijifilterhinmei the filtertorituke_nijifilterhinmei to set
     */
    public void setFiltertorituke_nijifilterhinmei(FXHDD01 filtertorituke_nijifilterhinmei) {
        this.filtertorituke_nijifilterhinmei = filtertorituke_nijifilterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_LotNo2
     * @return the filtertorituke_lotno2
     */
    public FXHDD01 getFiltertorituke_lotno2() {
        return filtertorituke_lotno2;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_LotNo2
     * @param filtertorituke_lotno2 the filtertorituke_lotno2 to set
     */
    public void setFiltertorituke_lotno2(FXHDD01 filtertorituke_lotno2) {
        this.filtertorituke_lotno2 = filtertorituke_lotno2;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_取り付け本数2
     * @return the filtertorituke_toritukehonsuu2
     */
    public FXHDD01 getFiltertorituke_toritukehonsuu2() {
        return filtertorituke_toritukehonsuu2;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_取り付け本数2
     * @param filtertorituke_toritukehonsuu2 the filtertorituke_toritukehonsuu2 to set
     */
    public void setFiltertorituke_toritukehonsuu2(FXHDD01 filtertorituke_toritukehonsuu2) {
        this.filtertorituke_toritukehonsuu2 = filtertorituke_toritukehonsuu2;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_3次ﾌｨﾙﾀｰ品名
     * @return the filtertorituke_sanjifilterhinmei
     */
    public FXHDD01 getFiltertorituke_sanjifilterhinmei() {
        return filtertorituke_sanjifilterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_3次ﾌｨﾙﾀｰ品名
     * @param filtertorituke_sanjifilterhinmei the filtertorituke_sanjifilterhinmei to set
     */
    public void setFiltertorituke_sanjifilterhinmei(FXHDD01 filtertorituke_sanjifilterhinmei) {
        this.filtertorituke_sanjifilterhinmei = filtertorituke_sanjifilterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_LotNo3
     * @return the filtertorituke_lotno3
     */
    public FXHDD01 getFiltertorituke_lotno3() {
        return filtertorituke_lotno3;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_LotNo3
     * @param filtertorituke_lotno3 the filtertorituke_lotno3 to set
     */
    public void setFiltertorituke_lotno3(FXHDD01 filtertorituke_lotno3) {
        this.filtertorituke_lotno3 = filtertorituke_lotno3;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_取り付け本数3
     * @return the filtertorituke_toritukehonsuu3
     */
    public FXHDD01 getFiltertorituke_toritukehonsuu3() {
        return filtertorituke_toritukehonsuu3;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_取り付け本数3
     * @param filtertorituke_toritukehonsuu3 the filtertorituke_toritukehonsuu3 to set
     */
    public void setFiltertorituke_toritukehonsuu3(FXHDD01 filtertorituke_toritukehonsuu3) {
        this.filtertorituke_toritukehonsuu3 = filtertorituke_toritukehonsuu3;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_担当者
     * @return the filtertorituke_tantousya
     */
    public FXHDD01 getFiltertorituke_tantousya() {
        return filtertorituke_tantousya;
    }

    /**
     * ﾌｨﾙﾀｰ取り付け_担当者
     * @param filtertorituke_tantousya the filtertorituke_tantousya to set
     */
    public void setFiltertorituke_tantousya(FXHDD01 filtertorituke_tantousya) {
        this.filtertorituke_tantousya = filtertorituke_tantousya;
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
     * @return the filtersiyoukaisuu
     */
    public FXHDD01 getFiltersiyoukaisuu() {
        return filtersiyoukaisuu;
    }

    /**
     * ﾌｨﾙﾀｰ使用回数
     * @param filtersiyoukaisuu the filtersiyoukaisuu to set
     */
    public void setFiltersiyoukaisuu(FXHDD01 filtersiyoukaisuu) {
        this.filtersiyoukaisuu = filtersiyoukaisuu;
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
     * ﾌｨﾙﾀｰﾊﾟｽ開始_担当者
     * @return the filterpasskaisi_tantousya
     */
    public FXHDD01 getFilterpasskaisi_tantousya() {
        return filterpasskaisi_tantousya;
    }

    /**
     * ﾌｨﾙﾀｰﾊﾟｽ開始_担当者
     * @param filterpasskaisi_tantousya the filterpasskaisi_tantousya to set
     */
    public void setFilterpasskaisi_tantousya(FXHDD01 filterpasskaisi_tantousya) {
        this.filterpasskaisi_tantousya = filterpasskaisi_tantousya;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_F/P停止日
     * @return the filterkoukan1_fpteisi_day
     */
    public FXHDD01 getFilterkoukan1_fpteisi_day() {
        return filterkoukan1_fpteisi_day;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_F/P停止日
     * @param filterkoukan1_fpteisi_day the filterkoukan1_fpteisi_day to set
     */
    public void setFilterkoukan1_fpteisi_day(FXHDD01 filterkoukan1_fpteisi_day) {
        this.filterkoukan1_fpteisi_day = filterkoukan1_fpteisi_day;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_F/P停止時間
     * @return the filterkoukan1_fpteisi_time
     */
    public FXHDD01 getFilterkoukan1_fpteisi_time() {
        return filterkoukan1_fpteisi_time;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_F/P停止時間
     * @param filterkoukan1_fpteisi_time the filterkoukan1_fpteisi_time to set
     */
    public void setFilterkoukan1_fpteisi_time(FXHDD01 filterkoukan1_fpteisi_time) {
        this.filterkoukan1_fpteisi_time = filterkoukan1_fpteisi_time;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_1次ﾌｨﾙﾀｰ品名
     * @return the filterkoukan1_itijifilterhinmei
     */
    public FXHDD01 getFilterkoukan1_itijifilterhinmei() {
        return filterkoukan1_itijifilterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_1次ﾌｨﾙﾀｰ品名
     * @param filterkoukan1_itijifilterhinmei the filterkoukan1_itijifilterhinmei to set
     */
    public void setFilterkoukan1_itijifilterhinmei(FXHDD01 filterkoukan1_itijifilterhinmei) {
        this.filterkoukan1_itijifilterhinmei = filterkoukan1_itijifilterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_LotNo1
     * @return the filterkoukan1_lotno1
     */
    public FXHDD01 getFilterkoukan1_lotno1() {
        return filterkoukan1_lotno1;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_LotNo1
     * @param filterkoukan1_lotno1 the filterkoukan1_lotno1 to set
     */
    public void setFilterkoukan1_lotno1(FXHDD01 filterkoukan1_lotno1) {
        this.filterkoukan1_lotno1 = filterkoukan1_lotno1;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_取り付け本数1
     * @return the filterkoukan1_toritukehonsuu1
     */
    public FXHDD01 getFilterkoukan1_toritukehonsuu1() {
        return filterkoukan1_toritukehonsuu1;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_取り付け本数1
     * @param filterkoukan1_toritukehonsuu1 the filterkoukan1_toritukehonsuu1 to set
     */
    public void setFilterkoukan1_toritukehonsuu1(FXHDD01 filterkoukan1_toritukehonsuu1) {
        this.filterkoukan1_toritukehonsuu1 = filterkoukan1_toritukehonsuu1;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_2次ﾌｨﾙﾀｰ品名
     * @return the filterkoukan1_nijifilterhinmei
     */
    public FXHDD01 getFilterkoukan1_nijifilterhinmei() {
        return filterkoukan1_nijifilterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_2次ﾌｨﾙﾀｰ品名
     * @param filterkoukan1_nijifilterhinmei the filterkoukan1_nijifilterhinmei to set
     */
    public void setFilterkoukan1_nijifilterhinmei(FXHDD01 filterkoukan1_nijifilterhinmei) {
        this.filterkoukan1_nijifilterhinmei = filterkoukan1_nijifilterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_LotNo2
     * @return the filterkoukan1_lotno2
     */
    public FXHDD01 getFilterkoukan1_lotno2() {
        return filterkoukan1_lotno2;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_LotNo2
     * @param filterkoukan1_lotno2 the filterkoukan1_lotno2 to set
     */
    public void setFilterkoukan1_lotno2(FXHDD01 filterkoukan1_lotno2) {
        this.filterkoukan1_lotno2 = filterkoukan1_lotno2;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_取り付け本数2
     * @return the filterkoukan1_toritukehonsuu2
     */
    public FXHDD01 getFilterkoukan1_toritukehonsuu2() {
        return filterkoukan1_toritukehonsuu2;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_取り付け本数2
     * @param filterkoukan1_toritukehonsuu2 the filterkoukan1_toritukehonsuu2 to set
     */
    public void setFilterkoukan1_toritukehonsuu2(FXHDD01 filterkoukan1_toritukehonsuu2) {
        this.filterkoukan1_toritukehonsuu2 = filterkoukan1_toritukehonsuu2;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_3次ﾌｨﾙﾀｰ品名
     * @return the filterkoukan1_sanjifilterhinmei
     */
    public FXHDD01 getFilterkoukan1_sanjifilterhinmei() {
        return filterkoukan1_sanjifilterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_3次ﾌｨﾙﾀｰ品名
     * @param filterkoukan1_sanjifilterhinmei the filterkoukan1_sanjifilterhinmei to set
     */
    public void setFilterkoukan1_sanjifilterhinmei(FXHDD01 filterkoukan1_sanjifilterhinmei) {
        this.filterkoukan1_sanjifilterhinmei = filterkoukan1_sanjifilterhinmei;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_LotNo3
     * @return the filterkoukan1_lotno3
     */
    public FXHDD01 getFilterkoukan1_lotno3() {
        return filterkoukan1_lotno3;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_LotNo3
     * @param filterkoukan1_lotno3 the filterkoukan1_lotno3 to set
     */
    public void setFilterkoukan1_lotno3(FXHDD01 filterkoukan1_lotno3) {
        this.filterkoukan1_lotno3 = filterkoukan1_lotno3;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_取り付け本数3
     * @return the filterkoukan1_toritukehonsuu3
     */
    public FXHDD01 getFilterkoukan1_toritukehonsuu3() {
        return filterkoukan1_toritukehonsuu3;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_取り付け本数3
     * @param filterkoukan1_toritukehonsuu3 the filterkoukan1_toritukehonsuu3 to set
     */
    public void setFilterkoukan1_toritukehonsuu3(FXHDD01 filterkoukan1_toritukehonsuu3) {
        this.filterkoukan1_toritukehonsuu3 = filterkoukan1_toritukehonsuu3;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_F/P再開日
     * @return the filterkoukan1_fpsaikai_day
     */
    public FXHDD01 getFilterkoukan1_fpsaikai_day() {
        return filterkoukan1_fpsaikai_day;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_F/P再開日
     * @param filterkoukan1_fpsaikai_day the filterkoukan1_fpsaikai_day to set
     */
    public void setFilterkoukan1_fpsaikai_day(FXHDD01 filterkoukan1_fpsaikai_day) {
        this.filterkoukan1_fpsaikai_day = filterkoukan1_fpsaikai_day;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_F/P再開時間
     * @return the filterkoukan1_fpsaikai_time
     */
    public FXHDD01 getFilterkoukan1_fpsaikai_time() {
        return filterkoukan1_fpsaikai_time;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_F/P再開時間
     * @param filterkoukan1_fpsaikai_time the filterkoukan1_fpsaikai_time to set
     */
    public void setFilterkoukan1_fpsaikai_time(FXHDD01 filterkoukan1_fpsaikai_time) {
        this.filterkoukan1_fpsaikai_time = filterkoukan1_fpsaikai_time;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_担当者
     * @return the filterkoukan1_tantousya
     */
    public FXHDD01 getFilterkoukan1_tantousya() {
        return filterkoukan1_tantousya;
    }

    /**
     * ﾌｨﾙﾀｰ交換①_担当者
     * @param filterkoukan1_tantousya the filterkoukan1_tantousya to set
     */
    public void setFilterkoukan1_tantousya(FXHDD01 filterkoukan1_tantousya) {
        this.filterkoukan1_tantousya = filterkoukan1_tantousya;
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
     * F/Pﾄｰﾀﾙ時間
     * @return the fptotaljikan
     */
    public FXHDD01 getFptotaljikan() {
        return fptotaljikan;
    }

    /**
     * F/Pﾄｰﾀﾙ時間
     * @param fptotaljikan the fptotaljikan to set
     */
    public void setFptotaljikan(FXHDD01 fptotaljikan) {
        this.fptotaljikan = fptotaljikan;
    }

    /**
     * 1次ﾌｨﾙﾀｰ総使用本数
     * @return the itijifiltersousiyouhonsuu
     */
    public FXHDD01 getItijifiltersousiyouhonsuu() {
        return itijifiltersousiyouhonsuu;
    }

    /**
     * 1次ﾌｨﾙﾀｰ総使用本数
     * @param itijifiltersousiyouhonsuu the itijifiltersousiyouhonsuu to set
     */
    public void setItijifiltersousiyouhonsuu(FXHDD01 itijifiltersousiyouhonsuu) {
        this.itijifiltersousiyouhonsuu = itijifiltersousiyouhonsuu;
    }

    /**
     * 2次ﾌｨﾙﾀｰ総使用本数
     * @return the nijifiltersousiyouhonsuu
     */
    public FXHDD01 getNijifiltersousiyouhonsuu() {
        return nijifiltersousiyouhonsuu;
    }

    /**
     * 2次ﾌｨﾙﾀｰ総使用本数
     * @param nijifiltersousiyouhonsuu the nijifiltersousiyouhonsuu to set
     */
    public void setNijifiltersousiyouhonsuu(FXHDD01 nijifiltersousiyouhonsuu) {
        this.nijifiltersousiyouhonsuu = nijifiltersousiyouhonsuu;
    }

    /**
     * 3次ﾌｨﾙﾀｰ総使用本数
     * @return the sanjifiltersousiyouhonsuu
     */
    public FXHDD01 getSanjifiltersousiyouhonsuu() {
        return sanjifiltersousiyouhonsuu;
    }

    /**
     * 3次ﾌｨﾙﾀｰ総使用本数
     * @param sanjifiltersousiyouhonsuu the sanjifiltersousiyouhonsuu to set
     */
    public void setSanjifiltersousiyouhonsuu(FXHDD01 sanjifiltersousiyouhonsuu) {
        this.sanjifiltersousiyouhonsuu = sanjifiltersousiyouhonsuu;
    }

    /**
     * ﾌｨﾙﾀｰﾊﾟｽ終了_担当者
     * @return the filterpasssyuuryou_tantousya
     */
    public FXHDD01 getFilterpasssyuuryou_tantousya() {
        return filterpasssyuuryou_tantousya;
    }

    /**
     * ﾌｨﾙﾀｰﾊﾟｽ終了_担当者
     * @param filterpasssyuuryou_tantousya the filterpasssyuuryou_tantousya to set
     */
    public void setFilterpasssyuuryou_tantousya(FXHDD01 filterpasssyuuryou_tantousya) {
        this.filterpasssyuuryou_tantousya = filterpasssyuuryou_tantousya;
    }

    /**
     * 総重量測定1
     * @return the soujyuurousokutei1
     */
    public FXHDD01 getSoujyuurousokutei1() {
        return soujyuurousokutei1;
    }

    /**
     * 総重量測定1
     * @param soujyuurousokutei1 the soujyuurousokutei1 to set
     */
    public void setSoujyuurousokutei1(FXHDD01 soujyuurousokutei1) {
        this.soujyuurousokutei1 = soujyuurousokutei1;
    }

    /**
     * 総重量測定2
     * @return the soujyuurousokutei2
     */
    public FXHDD01 getSoujyuurousokutei2() {
        return soujyuurousokutei2;
    }

    /**
     * 総重量測定2
     * @param soujyuurousokutei2 the soujyuurousokutei2 to set
     */
    public void setSoujyuurousokutei2(FXHDD01 soujyuurousokutei2) {
        this.soujyuurousokutei2 = soujyuurousokutei2;
    }

    /**
     * 総重量測定3
     * @return the soujyuurousokutei3
     */
    public FXHDD01 getSoujyuurousokutei3() {
        return soujyuurousokutei3;
    }

    /**
     * 総重量測定3
     * @param soujyuurousokutei3 the soujyuurousokutei3 to set
     */
    public void setSoujyuurousokutei3(FXHDD01 soujyuurousokutei3) {
        this.soujyuurousokutei3 = soujyuurousokutei3;
    }

    /**
     * 総重量測定4
     * @return the soujyuurousokutei4
     */
    public FXHDD01 getSoujyuurousokutei4() {
        return soujyuurousokutei4;
    }

    /**
     * 総重量測定4
     * @param soujyuurousokutei4 the soujyuurousokutei4 to set
     */
    public void setSoujyuurousokutei4(FXHDD01 soujyuurousokutei4) {
        this.soujyuurousokutei4 = soujyuurousokutei4;
    }

    /**
     * 総重量測定5
     * @return the soujyuurousokutei5
     */
    public FXHDD01 getSoujyuurousokutei5() {
        return soujyuurousokutei5;
    }

    /**
     * 総重量測定5
     * @param soujyuurousokutei5 the soujyuurousokutei5 to set
     */
    public void setSoujyuurousokutei5(FXHDD01 soujyuurousokutei5) {
        this.soujyuurousokutei5 = soujyuurousokutei5;
    }

    /**
     * 総重量測定6
     * @return the soujyuurousokutei6
     */
    public FXHDD01 getSoujyuurousokutei6() {
        return soujyuurousokutei6;
    }

    /**
     * 総重量測定6
     * @param soujyuurousokutei6 the soujyuurousokutei6 to set
     */
    public void setSoujyuurousokutei6(FXHDD01 soujyuurousokutei6) {
        this.soujyuurousokutei6 = soujyuurousokutei6;
    }

    /**
     * 総重量測定7
     * @return the soujyuurousokutei7
     */
    public FXHDD01 getSoujyuurousokutei7() {
        return soujyuurousokutei7;
    }

    /**
     * 総重量測定7
     * @param soujyuurousokutei7 the soujyuurousokutei7 to set
     */
    public void setSoujyuurousokutei7(FXHDD01 soujyuurousokutei7) {
        this.soujyuurousokutei7 = soujyuurousokutei7;
    }

    /**
     * 総重量測定8
     * @return the soujyuurousokutei8
     */
    public FXHDD01 getSoujyuurousokutei8() {
        return soujyuurousokutei8;
    }

    /**
     * 総重量測定8
     * @param soujyuurousokutei8 the soujyuurousokutei8 to set
     */
    public void setSoujyuurousokutei8(FXHDD01 soujyuurousokutei8) {
        this.soujyuurousokutei8 = soujyuurousokutei8;
    }

    /**
     * 総重量測定9
     * @return the soujyuurousokutei9
     */
    public FXHDD01 getSoujyuurousokutei9() {
        return soujyuurousokutei9;
    }

    /**
     * 総重量測定9
     * @param soujyuurousokutei9 the soujyuurousokutei9 to set
     */
    public void setSoujyuurousokutei9(FXHDD01 soujyuurousokutei9) {
        this.soujyuurousokutei9 = soujyuurousokutei9;
    }

    /**
     * 総重量測定10
     * @return the soujyuurousokutei10
     */
    public FXHDD01 getSoujyuurousokutei10() {
        return soujyuurousokutei10;
    }

    /**
     * 総重量測定10
     * @param soujyuurousokutei10 the soujyuurousokutei10 to set
     */
    public void setSoujyuurousokutei10(FXHDD01 soujyuurousokutei10) {
        this.soujyuurousokutei10 = soujyuurousokutei10;
    }

    /**
     * 総重量測定11
     * @return the soujyuurousokutei11
     */
    public FXHDD01 getSoujyuurousokutei11() {
        return soujyuurousokutei11;
    }

    /**
     * 総重量測定11
     * @param soujyuurousokutei11 the soujyuurousokutei11 to set
     */
    public void setSoujyuurousokutei11(FXHDD01 soujyuurousokutei11) {
        this.soujyuurousokutei11 = soujyuurousokutei11;
    }

    /**
     * 総重量測定12
     * @return the soujyuurousokutei12
     */
    public FXHDD01 getSoujyuurousokutei12() {
        return soujyuurousokutei12;
    }

    /**
     * 総重量測定12
     * @param soujyuurousokutei12 the soujyuurousokutei12 to set
     */
    public void setSoujyuurousokutei12(FXHDD01 soujyuurousokutei12) {
        this.soujyuurousokutei12 = soujyuurousokutei12;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量1
     * @return the yuudentaislurryjyuurou1
     */
    public FXHDD01 getYuudentaislurryjyuurou1() {
        return yuudentaislurryjyuurou1;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量1
     * @param yuudentaislurryjyuurou1 the yuudentaislurryjyuurou1 to set
     */
    public void setYuudentaislurryjyuurou1(FXHDD01 yuudentaislurryjyuurou1) {
        this.yuudentaislurryjyuurou1 = yuudentaislurryjyuurou1;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量2
     * @return the yuudentaislurryjyuurou2
     */
    public FXHDD01 getYuudentaislurryjyuurou2() {
        return yuudentaislurryjyuurou2;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量2
     * @param yuudentaislurryjyuurou2 the yuudentaislurryjyuurou2 to set
     */
    public void setYuudentaislurryjyuurou2(FXHDD01 yuudentaislurryjyuurou2) {
        this.yuudentaislurryjyuurou2 = yuudentaislurryjyuurou2;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量3
     * @return the yuudentaislurryjyuurou3
     */
    public FXHDD01 getYuudentaislurryjyuurou3() {
        return yuudentaislurryjyuurou3;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量3
     * @param yuudentaislurryjyuurou3 the yuudentaislurryjyuurou3 to set
     */
    public void setYuudentaislurryjyuurou3(FXHDD01 yuudentaislurryjyuurou3) {
        this.yuudentaislurryjyuurou3 = yuudentaislurryjyuurou3;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量4
     * @return the yuudentaislurryjyuurou4
     */
    public FXHDD01 getYuudentaislurryjyuurou4() {
        return yuudentaislurryjyuurou4;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量4
     * @param yuudentaislurryjyuurou4 the yuudentaislurryjyuurou4 to set
     */
    public void setYuudentaislurryjyuurou4(FXHDD01 yuudentaislurryjyuurou4) {
        this.yuudentaislurryjyuurou4 = yuudentaislurryjyuurou4;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量5
     * @return the yuudentaislurryjyuurou5
     */
    public FXHDD01 getYuudentaislurryjyuurou5() {
        return yuudentaislurryjyuurou5;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量5
     * @param yuudentaislurryjyuurou5 the yuudentaislurryjyuurou5 to set
     */
    public void setYuudentaislurryjyuurou5(FXHDD01 yuudentaislurryjyuurou5) {
        this.yuudentaislurryjyuurou5 = yuudentaislurryjyuurou5;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量6
     * @return the yuudentaislurryjyuurou6
     */
    public FXHDD01 getYuudentaislurryjyuurou6() {
        return yuudentaislurryjyuurou6;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量6
     * @param yuudentaislurryjyuurou6 the yuudentaislurryjyuurou6 to set
     */
    public void setYuudentaislurryjyuurou6(FXHDD01 yuudentaislurryjyuurou6) {
        this.yuudentaislurryjyuurou6 = yuudentaislurryjyuurou6;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量7
     * @return the yuudentaislurryjyuurou7
     */
    public FXHDD01 getYuudentaislurryjyuurou7() {
        return yuudentaislurryjyuurou7;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量7
     * @param yuudentaislurryjyuurou7 the yuudentaislurryjyuurou7 to set
     */
    public void setYuudentaislurryjyuurou7(FXHDD01 yuudentaislurryjyuurou7) {
        this.yuudentaislurryjyuurou7 = yuudentaislurryjyuurou7;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量8
     * @return the yuudentaislurryjyuurou8
     */
    public FXHDD01 getYuudentaislurryjyuurou8() {
        return yuudentaislurryjyuurou8;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量8
     * @param yuudentaislurryjyuurou8 the yuudentaislurryjyuurou8 to set
     */
    public void setYuudentaislurryjyuurou8(FXHDD01 yuudentaislurryjyuurou8) {
        this.yuudentaislurryjyuurou8 = yuudentaislurryjyuurou8;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量9
     * @return the yuudentaislurryjyuurou9
     */
    public FXHDD01 getYuudentaislurryjyuurou9() {
        return yuudentaislurryjyuurou9;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量9
     * @param yuudentaislurryjyuurou9 the yuudentaislurryjyuurou9 to set
     */
    public void setYuudentaislurryjyuurou9(FXHDD01 yuudentaislurryjyuurou9) {
        this.yuudentaislurryjyuurou9 = yuudentaislurryjyuurou9;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量10
     * @return the yuudentaislurryjyuurou10
     */
    public FXHDD01 getYuudentaislurryjyuurou10() {
        return yuudentaislurryjyuurou10;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量10
     * @param yuudentaislurryjyuurou10 the yuudentaislurryjyuurou10 to set
     */
    public void setYuudentaislurryjyuurou10(FXHDD01 yuudentaislurryjyuurou10) {
        this.yuudentaislurryjyuurou10 = yuudentaislurryjyuurou10;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量11
     * @return the yuudentaislurryjyuurou11
     */
    public FXHDD01 getYuudentaislurryjyuurou11() {
        return yuudentaislurryjyuurou11;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量11
     * @param yuudentaislurryjyuurou11 the yuudentaislurryjyuurou11 to set
     */
    public void setYuudentaislurryjyuurou11(FXHDD01 yuudentaislurryjyuurou11) {
        this.yuudentaislurryjyuurou11 = yuudentaislurryjyuurou11;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量12
     * @return the yuudentaislurryjyuurou12
     */
    public FXHDD01 getYuudentaislurryjyuurou12() {
        return yuudentaislurryjyuurou12;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量12
     * @param yuudentaislurryjyuurou12 the yuudentaislurryjyuurou12 to set
     */
    public void setYuudentaislurryjyuurou12(FXHDD01 yuudentaislurryjyuurou12) {
        this.yuudentaislurryjyuurou12 = yuudentaislurryjyuurou12;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量合計
     * @return the yuudentaislurryjyuurougoukei
     */
    public FXHDD01 getYuudentaislurryjyuurougoukei() {
        return yuudentaislurryjyuurougoukei;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量合計
     * @param yuudentaislurryjyuurougoukei the yuudentaislurryjyuurougoukei to set
     */
    public void setYuudentaislurryjyuurougoukei(FXHDD01 yuudentaislurryjyuurougoukei) {
        this.yuudentaislurryjyuurougoukei = yuudentaislurryjyuurougoukei;
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
     * 歩留まり計算
     * @return the budomarikeisan
     */
    public FXHDD01 getBudomarikeisan() {
        return budomarikeisan;
    }

    /**
     * 歩留まり計算
     * @param budomarikeisan the budomarikeisan to set
     */
    public void setBudomarikeisan(FXHDD01 budomarikeisan) {
        this.budomarikeisan = budomarikeisan;
    }

    /**
     * 誘電体ｽﾗﾘｰ有効期限
     * @return the yuudentaislurryyuukoukigen
     */
    public FXHDD01 getYuudentaislurryyuukoukigen() {
        return yuudentaislurryyuukoukigen;
    }

    /**
     * 誘電体ｽﾗﾘｰ有効期限
     * @param yuudentaislurryyuukoukigen the yuudentaislurryyuukoukigen to set
     */
    public void setYuudentaislurryyuukoukigen(FXHDD01 yuudentaislurryyuukoukigen) {
        this.yuudentaislurryyuukoukigen = yuudentaislurryyuukoukigen;
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
     * 製品重量確認_担当者
     * @return the seihinjyuuryoukakunin_tantousya
     */
    public FXHDD01 getSeihinjyuuryoukakunin_tantousya() {
        return seihinjyuuryoukakunin_tantousya;
    }

    /**
     * 製品重量確認_担当者
     * @param seihinjyuuryoukakunin_tantousya the seihinjyuuryoukakunin_tantousya to set
     */
    public void setSeihinjyuuryoukakunin_tantousya(FXHDD01 seihinjyuuryoukakunin_tantousya) {
        this.seihinjyuuryoukakunin_tantousya = seihinjyuuryoukakunin_tantousya;
    }

    /**
     * 保存用ｻﾝﾌﾟﾙ回収
     * @return the hozonyousamplekaisyu
     */
    public FXHDD01 getHozonyousamplekaisyu() {
        return hozonyousamplekaisyu;
    }

    /**
     * 保存用ｻﾝﾌﾟﾙ回収
     * @param hozonyousamplekaisyu the hozonyousamplekaisyu to set
     */
    public void setHozonyousamplekaisyu(FXHDD01 hozonyousamplekaisyu) {
        this.hozonyousamplekaisyu = hozonyousamplekaisyu;
    }

    /**
     * 分析用ｻﾝﾌﾟﾙ回収
     * @return the zunsekiyousamplekaisyu
     */
    public FXHDD01 getZunsekiyousamplekaisyu() {
        return zunsekiyousamplekaisyu;
    }

    /**
     * 分析用ｻﾝﾌﾟﾙ回収
     * @param zunsekiyousamplekaisyu the zunsekiyousamplekaisyu to set
     */
    public void setZunsekiyousamplekaisyu(FXHDD01 zunsekiyousamplekaisyu) {
        this.zunsekiyousamplekaisyu = zunsekiyousamplekaisyu;
    }

    /**
     * 乾燥皿
     * @return the kansouzara
     */
    public FXHDD01 getKansouzara() {
        return kansouzara;
    }

    /**
     * 乾燥皿
     * @param kansouzara the kansouzara to set
     */
    public void setKansouzara(FXHDD01 kansouzara) {
        this.kansouzara = kansouzara;
    }

    /**
     * ｱﾙﾐ皿風袋重量
     * @return the arumizarafuutaijyuuryou
     */
    public FXHDD01 getArumizarafuutaijyuuryou() {
        return arumizarafuutaijyuuryou;
    }

    /**
     * ｱﾙﾐ皿風袋重量
     * @param arumizarafuutaijyuuryou the arumizarafuutaijyuuryou to set
     */
    public void setArumizarafuutaijyuuryou(FXHDD01 arumizarafuutaijyuuryou) {
        this.arumizarafuutaijyuuryou = arumizarafuutaijyuuryou;
    }

    /**
     * ｽﾗﾘｰｻﾝﾌﾟﾙ重量規格
     * @return the slurrysamplejyuuryoukikaku
     */
    public FXHDD01 getSlurrysamplejyuuryoukikaku() {
        return slurrysamplejyuuryoukikaku;
    }

    /**
     * ｽﾗﾘｰｻﾝﾌﾟﾙ重量規格
     * @param slurrysamplejyuuryoukikaku the slurrysamplejyuuryoukikaku to set
     */
    public void setSlurrysamplejyuuryoukikaku(FXHDD01 slurrysamplejyuuryoukikaku) {
        this.slurrysamplejyuuryoukikaku = slurrysamplejyuuryoukikaku;
    }

    /**
     * 乾燥前ｽﾗﾘｰ重量
     * @return the kansoumaeslurryjyuuryou
     */
    public FXHDD01 getKansoumaeslurryjyuuryou() {
        return kansoumaeslurryjyuuryou;
    }

    /**
     * 乾燥前ｽﾗﾘｰ重量
     * @param kansoumaeslurryjyuuryou the kansoumaeslurryjyuuryou to set
     */
    public void setKansoumaeslurryjyuuryou(FXHDD01 kansoumaeslurryjyuuryou) {
        this.kansoumaeslurryjyuuryou = kansoumaeslurryjyuuryou;
    }

    /**
     * 乾燥機
     * @return the kansouki
     */
    public FXHDD01 getKansouki() {
        return kansouki;
    }

    /**
     * 乾燥機
     * @param kansouki the kansouki to set
     */
    public void setKansouki(FXHDD01 kansouki) {
        this.kansouki = kansouki;
    }

    /**
     * 乾燥温度規格
     * @return the kansouondokikaku
     */
    public FXHDD01 getKansouondokikaku() {
        return kansouondokikaku;
    }

    /**
     * 乾燥温度規格
     * @param kansouondokikaku the kansouondokikaku to set
     */
    public void setKansouondokikaku(FXHDD01 kansouondokikaku) {
        this.kansouondokikaku = kansouondokikaku;
    }

    /**
     * 乾燥時間規格
     * @return the kansoujikankikaku
     */
    public FXHDD01 getKansoujikankikaku() {
        return kansoujikankikaku;
    }

    /**
     * 乾燥時間規格
     * @param kansoujikankikaku the kansoujikankikaku to set
     */
    public void setKansoujikankikaku(FXHDD01 kansoujikankikaku) {
        this.kansoujikankikaku = kansoujikankikaku;
    }

    /**
     * 乾燥開始日
     * @return the kansoukaisi_day
     */
    public FXHDD01 getKansoukaisi_day() {
        return kansoukaisi_day;
    }

    /**
     * 乾燥開始日
     * @param kansoukaisi_day the kansoukaisi_day to set
     */
    public void setKansoukaisi_day(FXHDD01 kansoukaisi_day) {
        this.kansoukaisi_day = kansoukaisi_day;
    }

    /**
     * 乾燥開始時間
     * @return the kansoukaisi_time
     */
    public FXHDD01 getKansoukaisi_time() {
        return kansoukaisi_time;
    }

    /**
     * 乾燥開始時間
     * @param kansoukaisi_time the kansoukaisi_time to set
     */
    public void setKansoukaisi_time(FXHDD01 kansoukaisi_time) {
        this.kansoukaisi_time = kansoukaisi_time;
    }

    /**
     * 乾燥終了日
     * @return the kansousyuuryou_day
     */
    public FXHDD01 getKansousyuuryou_day() {
        return kansousyuuryou_day;
    }

    /**
     * 乾燥終了日
     * @param kansousyuuryou_day the kansousyuuryou_day to set
     */
    public void setKansousyuuryou_day(FXHDD01 kansousyuuryou_day) {
        this.kansousyuuryou_day = kansousyuuryou_day;
    }

    /**
     * 乾燥終了時間
     * @return the kansousyuuryou_time
     */
    public FXHDD01 getKansousyuuryou_time() {
        return kansousyuuryou_time;
    }

    /**
     * 乾燥終了時間
     * @param kansousyuuryou_time the kansousyuuryou_time to set
     */
    public void setKansousyuuryou_time(FXHDD01 kansousyuuryou_time) {
        this.kansousyuuryou_time = kansousyuuryou_time;
    }

    /**
     * 乾燥時間ﾄｰﾀﾙ
     * @return the kansoujikantotal
     */
    public FXHDD01 getKansoujikantotal() {
        return kansoujikantotal;
    }

    /**
     * 乾燥時間ﾄｰﾀﾙ
     * @param kansoujikantotal the kansoujikantotal to set
     */
    public void setKansoujikantotal(FXHDD01 kansoujikantotal) {
        this.kansoujikantotal = kansoujikantotal;
    }

    /**
     * 乾燥後総重量
     * @return the kansougosoujyuuryou
     */
    public FXHDD01 getKansougosoujyuuryou() {
        return kansougosoujyuuryou;
    }

    /**
     * 乾燥後総重量
     * @param kansougosoujyuuryou the kansougosoujyuuryou to set
     */
    public void setKansougosoujyuuryou(FXHDD01 kansougosoujyuuryou) {
        this.kansougosoujyuuryou = kansougosoujyuuryou;
    }

    /**
     * 乾燥後正味重量
     * @return the kansougosyoumijyuuryou
     */
    public FXHDD01 getKansougosyoumijyuuryou() {
        return kansougosyoumijyuuryou;
    }

    /**
     * 乾燥後正味重量
     * @param kansougosyoumijyuuryou the kansougosyoumijyuuryou to set
     */
    public void setKansougosyoumijyuuryou(FXHDD01 kansougosyoumijyuuryou) {
        this.kansougosyoumijyuuryou = kansougosyoumijyuuryou;
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