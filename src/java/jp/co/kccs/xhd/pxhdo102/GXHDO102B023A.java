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
 * 変更日	2021/11/12<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B023(誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞ)
 *
 * @author KCSS K.Jo
 * @since  2021/11/12
 */
@ViewScoped
@Named
public class GXHDO102B023A implements Serializable {
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
     * 溶剤洗浄_撹拌機
     */
    private FXHDD01 youzaisenjyou_kakuhanki;

    /**
     * 溶剤洗浄_使用ﾀﾝｸ
     */
    private FXHDD01 youzaisenjyou_siyoutank;

    /**
     * 溶剤量
     */
    private FXHDD01 youzairyou;

    /**
     * 溶剤投入
     */
    private FXHDD01 youzaitounyuu;

    /**
     * 主軸
     */
    private FXHDD01 syuziku;

    /**
     * ﾎﾟﾝﾌﾟ
     */
    private FXHDD01 pump;

    /**
     * 洗浄条件_ﾃﾞｨｽﾊﾟ回転数
     */
    private FXHDD01 sjjyouken_dispakaitensuu;

    /**
     * ｾﾊﾟﾚｰﾀ
     */
    private FXHDD01 separate;

    /**
     * ｱｼﾞﾃｰﾀ
     */
    private FXHDD01 agitator;

    /**
     * 自動運転
     */
    private FXHDD01 jidouunten;

    /**
     * ﾀﾝｸAB循環
     */
    private FXHDD01 tankabjyunkan;

    /**
     * ﾊﾟｽ数
     */
    private FXHDD01 passsuu;

    /**
     * 洗浄時間
     */
    private FXHDD01 senjyoujikan;

    /**
     * ﾗｯﾌﾟﾀｲﾑ
     */
    private FXHDD01 raptime;

    /**
     * ﾌﾟﾚﾐｷ_撹拌機
     */
    private FXHDD01 premixing_kakuhanki;

    /**
     * ﾌﾟﾚﾐｷ_使用ﾀﾝｸ
     */
    private FXHDD01 premixing_siyoutank;

    /**
     * ｱｰｽｸﾞﾘｯﾌﾟ接続確認
     */
    private FXHDD01 earthgripsetuzokukakunin;

    /**
     * ﾌﾟﾚﾐｷ_ﾃﾞｨｽﾊﾟ回転数
     */
    private FXHDD01 premixing_dispakaitensuu;

    /**
     * 投入開始日
     */
    private FXHDD01 tounyuukaisi_day;

    /**
     * 投入開始時間
     */
    private FXHDD01 tounyuukaisi_time;

    /**
     * 投入①
     */
    private FXHDD01 tounyuu1;

    /**
     * 投入②
     */
    private FXHDD01 tounyuu2;

    /**
     * 撹拌時間規格①
     */
    private FXHDD01 kakuhanjikankikaku1;

    /**
     * 撹拌開始日①
     */
    private FXHDD01 kakuhankaisi1_day;

    /**
     * 撹拌開始時間①
     */
    private FXHDD01 kakuhankaisi1_time;

    /**
     * 撹拌終了日①
     */
    private FXHDD01 kakuhansyuuryou1_day;

    /**
     * 撹拌終了時間①
     */
    private FXHDD01 kakuhansyuuryou1_time;

    /**
     * 投入③
     */
    private FXHDD01 tounyuu3;

    /**
     * 撹拌時間規格②
     */
    private FXHDD01 kakuhanjikankikaku2;

    /**
     * 撹拌開始日②
     */
    private FXHDD01 kakuhankaisi2_day;

    /**
     * 撹拌開始時間②
     */
    private FXHDD01 kakuhankaisi2_time;

    /**
     * 撹拌終了日②
     */
    private FXHDD01 kakuhansyuuryou2_day;

    /**
     * 撹拌終了時間②
     */
    private FXHDD01 kakuhansyuuryou2_time;

    /**
     * 投入④
     */
    private FXHDD01 tounyuu4;

    /**
     * 撹拌時間規格③
     */
    private FXHDD01 kakuhanjikankikaku3;

    /**
     * 撹拌開始日③
     */
    private FXHDD01 kakuhankaisi3_day;

    /**
     * 撹拌開始時間③
     */
    private FXHDD01 kakuhankaisi3_time;

    /**
     * 撹拌終了日③
     */
    private FXHDD01 kakuhansyuuryou3_day;

    /**
     * 撹拌終了時間③
     */
    private FXHDD01 kakuhansyuuryou3_time;

    /**
     * 投入⑤
     */
    private FXHDD01 tounyuu5;

    /**
     * 撹拌時間規格④
     */
    private FXHDD01 kakuhanjikankikaku4;

    /**
     * 撹拌開始日④
     */
    private FXHDD01 kakuhankaisi4_day;

    /**
     * 撹拌開始時間④
     */
    private FXHDD01 kakuhankaisi4_time;

    /**
     * 撹拌終了日④
     */
    private FXHDD01 kakuhansyuuryou4_day;

    /**
     * 撹拌終了時間④
     */
    private FXHDD01 kakuhansyuuryou4_time;

    /**
     * 投入⑥
     */
    private FXHDD01 tounyuu6;

    /**
     * 撹拌時間規格⑤
     */
    private FXHDD01 kakuhanjikankikaku5;

    /**
     * 撹拌開始日⑤
     */
    private FXHDD01 kakuhankaisi5_day;

    /**
     * 撹拌開始時間⑤
     */
    private FXHDD01 kakuhankaisi5_time;

    /**
     * 撹拌終了日⑤
     */
    private FXHDD01 kakuhansyuuryou5_day;

    /**
     * 撹拌終了時間⑤
     */
    private FXHDD01 kakuhansyuuryou5_time;

    /**
     * 回転数変更
     */
    private FXHDD01 kaitensuuhenkou;

    /**
     * 投入⑦
     */
    private FXHDD01 tounyuu7;

    /**
     * 投入⑧
     */
    private FXHDD01 tounyuu8;

    /**
     * 投入⑨
     */
    private FXHDD01 tounyuu9;

    /**
     * 撹拌時間規格⑥
     */
    private FXHDD01 kakuhanjikankikaku6;

    /**
     * 撹拌開始日⑥
     */
    private FXHDD01 kakuhankaisi6_day;

    /**
     * 撹拌開始時間⑥
     */
    private FXHDD01 kakuhankaisi6_time;

    /**
     * 撹拌終了日⑥
     */
    private FXHDD01 kakuhansyuuryou6_day;

    /**
     * 撹拌終了時間⑥
     */
    private FXHDD01 kakuhansyuuryou6_time;

    /**
     * 投入⑩
     */
    private FXHDD01 tounyuu10;

    /**
     * 投入⑪
     */
    private FXHDD01 tounyuu11;

    /**
     * 投入⑫
     */
    private FXHDD01 tounyuu12;

    /**
     * 投入⑬
     */
    private FXHDD01 tounyuu13;

    /**
     * 投入⑭
     */
    private FXHDD01 tounyuu14;

    /**
     * 投入⑮
     */
    private FXHDD01 tounyuu15;

    /**
     * 投入⑯
     */
    private FXHDD01 tounyuu16;

    /**
     * 投入⑰
     */
    private FXHDD01 tounyuu17;

    /**
     * 投入⑱
     */
    private FXHDD01 tounyuu18;

    /**
     * 投入⑲
     */
    private FXHDD01 tounyuu19;

    /**
     * 投入終了日
     */
    private FXHDD01 tounyuusyuuryou_day;

    /**
     * 投入終了時間
     */
    private FXHDD01 tounyuusyuuryou_time;

    /**
     * 撹拌機
     */
    private FXHDD01 kakuhanki;

    /**
     * 回転数
     */
    private FXHDD01 kaitensuu;

    /**
     * 撹拌時間規格⑦
     */
    private FXHDD01 kakuhanjikankikaku7;

    /**
     * 撹拌開始日⑦
     */
    private FXHDD01 kakuhankaisi7_day;

    /**
     * 撹拌開始時間⑦
     */
    private FXHDD01 kakuhankaisi7_time;

    /**
     * 回転体への接触の確認
     */
    private FXHDD01 kaitentaihesesyokukakunin;

    /**
     * 撹拌終了日⑦
     */
    private FXHDD01 kakuhansyuuryou7_day;

    /**
     * 撹拌終了時間⑦
     */
    private FXHDD01 kakuhansyuuryou7_time;

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
    public GXHDO102B023A() {
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
     * 溶剤洗浄_撹拌機
     * @return the youzaisenjyou_kakuhanki
     */
    public FXHDD01 getYouzaisenjyou_kakuhanki() {
        return youzaisenjyou_kakuhanki;
    }

    /**
     * 溶剤洗浄_撹拌機
     * @param youzaisenjyou_kakuhanki the youzaisenjyou_kakuhanki to set
     */
    public void setYouzaisenjyou_kakuhanki(FXHDD01 youzaisenjyou_kakuhanki) {
        this.youzaisenjyou_kakuhanki = youzaisenjyou_kakuhanki;
    }

    /**
     * 溶剤洗浄_使用ﾀﾝｸ
     * @return the youzaisenjyou_siyoutank
     */
    public FXHDD01 getYouzaisenjyou_siyoutank() {
        return youzaisenjyou_siyoutank;
    }

    /**
     * 溶剤洗浄_使用ﾀﾝｸ
     * @param youzaisenjyou_siyoutank the youzaisenjyou_siyoutank to set
     */
    public void setYouzaisenjyou_siyoutank(FXHDD01 youzaisenjyou_siyoutank) {
        this.youzaisenjyou_siyoutank = youzaisenjyou_siyoutank;
    }

    /**
     * 溶剤量
     * @return the youzairyou
     */
    public FXHDD01 getYouzairyou() {
        return youzairyou;
    }

    /**
     * 溶剤量
     * @param youzairyou the youzairyou to set
     */
    public void setYouzairyou(FXHDD01 youzairyou) {
        this.youzairyou = youzairyou;
    }

    /**
     * 溶剤投入
     * @return the youzaitounyuu
     */
    public FXHDD01 getYouzaitounyuu() {
        return youzaitounyuu;
    }

    /**
     * 溶剤投入
     * @param youzaitounyuu the youzaitounyuu to set
     */
    public void setYouzaitounyuu(FXHDD01 youzaitounyuu) {
        this.youzaitounyuu = youzaitounyuu;
    }

    /**
     * 主軸
     * @return the syuziku
     */
    public FXHDD01 getSyuziku() {
        return syuziku;
    }

    /**
     * 主軸
     * @param syuziku the syuziku to set
     */
    public void setSyuziku(FXHDD01 syuziku) {
        this.syuziku = syuziku;
    }

    /**
     * ﾎﾟﾝﾌﾟ
     * @return the pump
     */
    public FXHDD01 getPump() {
        return pump;
    }

    /**
     * ﾎﾟﾝﾌﾟ
     * @param pump the pump to set
     */
    public void setPump(FXHDD01 pump) {
        this.pump = pump;
    }

    /**
     * 洗浄条件_ﾃﾞｨｽﾊﾟ回転数
     * @return the sjjyouken_dispakaitensuu
     */
    public FXHDD01 getSjjyouken_dispakaitensuu() {
        return sjjyouken_dispakaitensuu;
    }

    /**
     * 洗浄条件_ﾃﾞｨｽﾊﾟ回転数
     * @param sjjyouken_dispakaitensuu the sjjyouken_dispakaitensuu to set
     */
    public void setSjjyouken_dispakaitensuu(FXHDD01 sjjyouken_dispakaitensuu) {
        this.sjjyouken_dispakaitensuu = sjjyouken_dispakaitensuu;
    }

    /**
     * ｾﾊﾟﾚｰﾀ
     * @return the separate
     */
    public FXHDD01 getSeparate() {
        return separate;
    }

    /**
     * ｾﾊﾟﾚｰﾀ
     * @param separate the separate to set
     */
    public void setSeparate(FXHDD01 separate) {
        this.separate = separate;
    }

    /**
     * ｱｼﾞﾃｰﾀ
     * @return the agitator
     */
    public FXHDD01 getAgitator() {
        return agitator;
    }

    /**
     * ｱｼﾞﾃｰﾀ
     * @param agitator the agitator to set
     */
    public void setAgitator(FXHDD01 agitator) {
        this.agitator = agitator;
    }

    /**
     * 自動運転
     * @return the jidouunten
     */
    public FXHDD01 getJidouunten() {
        return jidouunten;
    }

    /**
     * 自動運転
     * @param jidouunten the jidouunten to set
     */
    public void setJidouunten(FXHDD01 jidouunten) {
        this.jidouunten = jidouunten;
    }

    /**
     * ﾀﾝｸAB循環
     * @return the tankabjyunkan
     */
    public FXHDD01 getTankabjyunkan() {
        return tankabjyunkan;
    }

    /**
     * ﾀﾝｸAB循環
     * @param tankabjyunkan the tankabjyunkan to set
     */
    public void setTankabjyunkan(FXHDD01 tankabjyunkan) {
        this.tankabjyunkan = tankabjyunkan;
    }

    /**
     * ﾊﾟｽ数
     * @return the passsuu
     */
    public FXHDD01 getPasssuu() {
        return passsuu;
    }

    /**
     * ﾊﾟｽ数
     * @param passsuu the passsuu to set
     */
    public void setPasssuu(FXHDD01 passsuu) {
        this.passsuu = passsuu;
    }

    /**
     * 洗浄時間
     * @return the senjyoujikan
     */
    public FXHDD01 getSenjyoujikan() {
        return senjyoujikan;
    }

    /**
     * 洗浄時間
     * @param senjyoujikan the senjyoujikan to set
     */
    public void setSenjyoujikan(FXHDD01 senjyoujikan) {
        this.senjyoujikan = senjyoujikan;
    }

    /**
     * ﾗｯﾌﾟﾀｲﾑ
     * @return the raptime
     */
    public FXHDD01 getRaptime() {
        return raptime;
    }

    /**
     * ﾗｯﾌﾟﾀｲﾑ
     * @param raptime the raptime to set
     */
    public void setRaptime(FXHDD01 raptime) {
        this.raptime = raptime;
    }

    /**
     * ﾌﾟﾚﾐｷ_撹拌機
     * @return the premixing_kakuhanki
     */
    public FXHDD01 getPremixing_kakuhanki() {
        return premixing_kakuhanki;
    }

    /**
     * ﾌﾟﾚﾐｷ_撹拌機
     * @param premixing_kakuhanki the premixing_kakuhanki to set
     */
    public void setPremixing_kakuhanki(FXHDD01 premixing_kakuhanki) {
        this.premixing_kakuhanki = premixing_kakuhanki;
    }

    /**
     * ﾌﾟﾚﾐｷ_使用ﾀﾝｸ
     * @return the premixing_siyoutank
     */
    public FXHDD01 getPremixing_siyoutank() {
        return premixing_siyoutank;
    }

    /**
     * ﾌﾟﾚﾐｷ_使用ﾀﾝｸ
     * @param premixing_siyoutank the premixing_siyoutank to set
     */
    public void setPremixing_siyoutank(FXHDD01 premixing_siyoutank) {
        this.premixing_siyoutank = premixing_siyoutank;
    }

    /**
     * ｱｰｽｸﾞﾘｯﾌﾟ接続確認
     * @return the earthgripsetuzokukakunin
     */
    public FXHDD01 getEarthgripsetuzokukakunin() {
        return earthgripsetuzokukakunin;
    }

    /**
     * ｱｰｽｸﾞﾘｯﾌﾟ接続確認
     * @param earthgripsetuzokukakunin the earthgripsetuzokukakunin to set
     */
    public void setEarthgripsetuzokukakunin(FXHDD01 earthgripsetuzokukakunin) {
        this.earthgripsetuzokukakunin = earthgripsetuzokukakunin;
    }

    /**
     * ﾌﾟﾚﾐｷ_ﾃﾞｨｽﾊﾟ回転数
     * @return the premixing_dispakaitensuu
     */
    public FXHDD01 getPremixing_dispakaitensuu() {
        return premixing_dispakaitensuu;
    }

    /**
     * ﾌﾟﾚﾐｷ_ﾃﾞｨｽﾊﾟ回転数
     * @param premixing_dispakaitensuu the premixing_dispakaitensuu to set
     */
    public void setPremixing_dispakaitensuu(FXHDD01 premixing_dispakaitensuu) {
        this.premixing_dispakaitensuu = premixing_dispakaitensuu;
    }

    /**
     * 投入開始日
     * @return the tounyuukaisi_day
     */
    public FXHDD01 getTounyuukaisi_day() {
        return tounyuukaisi_day;
    }

    /**
     * 投入開始日
     * @param tounyuukaisi_day the tounyuukaisi_day to set
     */
    public void setTounyuukaisi_day(FXHDD01 tounyuukaisi_day) {
        this.tounyuukaisi_day = tounyuukaisi_day;
    }

    /**
     * 投入開始時間
     * @return the tounyuukaisi_time
     */
    public FXHDD01 getTounyuukaisi_time() {
        return tounyuukaisi_time;
    }

    /**
     * 投入開始時間
     * @param tounyuukaisi_time the tounyuukaisi_time to set
     */
    public void setTounyuukaisi_time(FXHDD01 tounyuukaisi_time) {
        this.tounyuukaisi_time = tounyuukaisi_time;
    }

    /**
     * 投入①
     * @return the tounyuu1
     */
    public FXHDD01 getTounyuu1() {
        return tounyuu1;
    }

    /**
     * 投入①
     * @param tounyuu1 the tounyuu1 to set
     */
    public void setTounyuu1(FXHDD01 tounyuu1) {
        this.tounyuu1 = tounyuu1;
    }

    /**
     * 投入②
     * @return the tounyuu2
     */
    public FXHDD01 getTounyuu2() {
        return tounyuu2;
    }

    /**
     * 投入②
     * @param tounyuu2 the tounyuu2 to set
     */
    public void setTounyuu2(FXHDD01 tounyuu2) {
        this.tounyuu2 = tounyuu2;
    }

    /**
     * 撹拌時間規格①
     * @return the kakuhanjikankikaku1
     */
    public FXHDD01 getKakuhanjikankikaku1() {
        return kakuhanjikankikaku1;
    }

    /**
     * 撹拌時間規格①
     * @param kakuhanjikankikaku1 the kakuhanjikankikaku1 to set
     */
    public void setKakuhanjikankikaku1(FXHDD01 kakuhanjikankikaku1) {
        this.kakuhanjikankikaku1 = kakuhanjikankikaku1;
    }

    /**
     * 撹拌開始日①
     * @return the kakuhankaisi1_day
     */
    public FXHDD01 getKakuhankaisi1_day() {
        return kakuhankaisi1_day;
    }

    /**
     * 撹拌開始日①
     * @param kakuhankaisi1_day the kakuhankaisi1_day to set
     */
    public void setKakuhankaisi1_day(FXHDD01 kakuhankaisi1_day) {
        this.kakuhankaisi1_day = kakuhankaisi1_day;
    }

    /**
     * 撹拌開始時間①
     * @return the kakuhankaisi1_time
     */
    public FXHDD01 getKakuhankaisi1_time() {
        return kakuhankaisi1_time;
    }

    /**
     * 撹拌開始時間①
     * @param kakuhankaisi1_time the kakuhankaisi1_time to set
     */
    public void setKakuhankaisi1_time(FXHDD01 kakuhankaisi1_time) {
        this.kakuhankaisi1_time = kakuhankaisi1_time;
    }

    /**
     * 撹拌終了日①
     * @return the kakuhansyuuryou1_day
     */
    public FXHDD01 getKakuhansyuuryou1_day() {
        return kakuhansyuuryou1_day;
    }

    /**
     * 撹拌終了日①
     * @param kakuhansyuuryou1_day the kakuhansyuuryou1_day to set
     */
    public void setKakuhansyuuryou1_day(FXHDD01 kakuhansyuuryou1_day) {
        this.kakuhansyuuryou1_day = kakuhansyuuryou1_day;
    }

    /**
     * 撹拌終了時間①
     * @return the kakuhansyuuryou1_time
     */
    public FXHDD01 getKakuhansyuuryou1_time() {
        return kakuhansyuuryou1_time;
    }

    /**
     * 撹拌終了時間①
     * @param kakuhansyuuryou1_time the kakuhansyuuryou1_time to set
     */
    public void setKakuhansyuuryou1_time(FXHDD01 kakuhansyuuryou1_time) {
        this.kakuhansyuuryou1_time = kakuhansyuuryou1_time;
    }

    /**
     * 投入③
     * @return the tounyuu3
     */
    public FXHDD01 getTounyuu3() {
        return tounyuu3;
    }

    /**
     * 投入③
     * @param tounyuu3 the tounyuu3 to set
     */
    public void setTounyuu3(FXHDD01 tounyuu3) {
        this.tounyuu3 = tounyuu3;
    }

    /**
     * 撹拌時間規格②
     * @return the kakuhanjikankikaku2
     */
    public FXHDD01 getKakuhanjikankikaku2() {
        return kakuhanjikankikaku2;
    }

    /**
     * 撹拌時間規格②
     * @param kakuhanjikankikaku2 the kakuhanjikankikaku2 to set
     */
    public void setKakuhanjikankikaku2(FXHDD01 kakuhanjikankikaku2) {
        this.kakuhanjikankikaku2 = kakuhanjikankikaku2;
    }

    /**
     * 撹拌開始日②
     * @return the kakuhankaisi2_day
     */
    public FXHDD01 getKakuhankaisi2_day() {
        return kakuhankaisi2_day;
    }

    /**
     * 撹拌開始日②
     * @param kakuhankaisi2_day the kakuhankaisi2_day to set
     */
    public void setKakuhankaisi2_day(FXHDD01 kakuhankaisi2_day) {
        this.kakuhankaisi2_day = kakuhankaisi2_day;
    }

    /**
     * 撹拌開始時間②
     * @return the kakuhankaisi2_time
     */
    public FXHDD01 getKakuhankaisi2_time() {
        return kakuhankaisi2_time;
    }

    /**
     * 撹拌開始時間②
     * @param kakuhankaisi2_time the kakuhankaisi2_time to set
     */
    public void setKakuhankaisi2_time(FXHDD01 kakuhankaisi2_time) {
        this.kakuhankaisi2_time = kakuhankaisi2_time;
    }

    /**
     * 撹拌終了日②
     * @return the kakuhansyuuryou2_day
     */
    public FXHDD01 getKakuhansyuuryou2_day() {
        return kakuhansyuuryou2_day;
    }

    /**
     * 撹拌終了日②
     * @param kakuhansyuuryou2_day the kakuhansyuuryou2_day to set
     */
    public void setKakuhansyuuryou2_day(FXHDD01 kakuhansyuuryou2_day) {
        this.kakuhansyuuryou2_day = kakuhansyuuryou2_day;
    }

    /**
     * 撹拌終了時間②
     * @return the kakuhansyuuryou2_time
     */
    public FXHDD01 getKakuhansyuuryou2_time() {
        return kakuhansyuuryou2_time;
    }

    /**
     * 撹拌終了時間②
     * @param kakuhansyuuryou2_time the kakuhansyuuryou2_time to set
     */
    public void setKakuhansyuuryou2_time(FXHDD01 kakuhansyuuryou2_time) {
        this.kakuhansyuuryou2_time = kakuhansyuuryou2_time;
    }

    /**
     * 投入④
     * @return the tounyuu4
     */
    public FXHDD01 getTounyuu4() {
        return tounyuu4;
    }

    /**
     * 投入④
     * @param tounyuu4 the tounyuu4 to set
     */
    public void setTounyuu4(FXHDD01 tounyuu4) {
        this.tounyuu4 = tounyuu4;
    }

    /**
     * 撹拌時間規格③
     * @return the kakuhanjikankikaku3
     */
    public FXHDD01 getKakuhanjikankikaku3() {
        return kakuhanjikankikaku3;
    }

    /**
     * 撹拌時間規格③
     * @param kakuhanjikankikaku3 the kakuhanjikankikaku3 to set
     */
    public void setKakuhanjikankikaku3(FXHDD01 kakuhanjikankikaku3) {
        this.kakuhanjikankikaku3 = kakuhanjikankikaku3;
    }

    /**
     * 撹拌開始日③
     * @return the kakuhankaisi3_day
     */
    public FXHDD01 getKakuhankaisi3_day() {
        return kakuhankaisi3_day;
    }

    /**
     * 撹拌開始日③
     * @param kakuhankaisi3_day the kakuhankaisi3_day to set
     */
    public void setKakuhankaisi3_day(FXHDD01 kakuhankaisi3_day) {
        this.kakuhankaisi3_day = kakuhankaisi3_day;
    }

    /**
     * 撹拌開始時間③
     * @return the kakuhankaisi3_time
     */
    public FXHDD01 getKakuhankaisi3_time() {
        return kakuhankaisi3_time;
    }

    /**
     * 撹拌開始時間③
     * @param kakuhankaisi3_time the kakuhankaisi3_time to set
     */
    public void setKakuhankaisi3_time(FXHDD01 kakuhankaisi3_time) {
        this.kakuhankaisi3_time = kakuhankaisi3_time;
    }

    /**
     * 撹拌終了日③
     * @return the kakuhansyuuryou3_day
     */
    public FXHDD01 getKakuhansyuuryou3_day() {
        return kakuhansyuuryou3_day;
    }

    /**
     * 撹拌終了日③
     * @param kakuhansyuuryou3_day the kakuhansyuuryou3_day to set
     */
    public void setKakuhansyuuryou3_day(FXHDD01 kakuhansyuuryou3_day) {
        this.kakuhansyuuryou3_day = kakuhansyuuryou3_day;
    }

    /**
     * 撹拌終了時間③
     * @return the kakuhansyuuryou3_time
     */
    public FXHDD01 getKakuhansyuuryou3_time() {
        return kakuhansyuuryou3_time;
    }

    /**
     * 撹拌終了時間③
     * @param kakuhansyuuryou3_time the kakuhansyuuryou3_time to set
     */
    public void setKakuhansyuuryou3_time(FXHDD01 kakuhansyuuryou3_time) {
        this.kakuhansyuuryou3_time = kakuhansyuuryou3_time;
    }

    /**
     * 投入⑤
     * @return the tounyuu5
     */
    public FXHDD01 getTounyuu5() {
        return tounyuu5;
    }

    /**
     * 投入⑤
     * @param tounyuu5 the tounyuu5 to set
     */
    public void setTounyuu5(FXHDD01 tounyuu5) {
        this.tounyuu5 = tounyuu5;
    }

    /**
     * 撹拌時間規格④
     * @return the kakuhanjikankikaku4
     */
    public FXHDD01 getKakuhanjikankikaku4() {
        return kakuhanjikankikaku4;
    }

    /**
     * 撹拌時間規格④
     * @param kakuhanjikankikaku4 the kakuhanjikankikaku4 to set
     */
    public void setKakuhanjikankikaku4(FXHDD01 kakuhanjikankikaku4) {
        this.kakuhanjikankikaku4 = kakuhanjikankikaku4;
    }

    /**
     * 撹拌開始日④
     * @return the kakuhankaisi4_day
     */
    public FXHDD01 getKakuhankaisi4_day() {
        return kakuhankaisi4_day;
    }

    /**
     * 撹拌開始日④
     * @param kakuhankaisi4_day the kakuhankaisi4_day to set
     */
    public void setKakuhankaisi4_day(FXHDD01 kakuhankaisi4_day) {
        this.kakuhankaisi4_day = kakuhankaisi4_day;
    }

    /**
     * 撹拌開始時間④
     * @return the kakuhankaisi4_time
     */
    public FXHDD01 getKakuhankaisi4_time() {
        return kakuhankaisi4_time;
    }

    /**
     * 撹拌開始時間④
     * @param kakuhankaisi4_time the kakuhankaisi4_time to set
     */
    public void setKakuhankaisi4_time(FXHDD01 kakuhankaisi4_time) {
        this.kakuhankaisi4_time = kakuhankaisi4_time;
    }

    /**
     * 撹拌終了日④
     * @return the kakuhansyuuryou4_day
     */
    public FXHDD01 getKakuhansyuuryou4_day() {
        return kakuhansyuuryou4_day;
    }

    /**
     * 撹拌終了日④
     * @param kakuhansyuuryou4_day the kakuhansyuuryou4_day to set
     */
    public void setKakuhansyuuryou4_day(FXHDD01 kakuhansyuuryou4_day) {
        this.kakuhansyuuryou4_day = kakuhansyuuryou4_day;
    }

    /**
     * 撹拌終了時間④
     * @return the kakuhansyuuryou4_time
     */
    public FXHDD01 getKakuhansyuuryou4_time() {
        return kakuhansyuuryou4_time;
    }

    /**
     * 撹拌終了時間④
     * @param kakuhansyuuryou4_time the kakuhansyuuryou4_time to set
     */
    public void setKakuhansyuuryou4_time(FXHDD01 kakuhansyuuryou4_time) {
        this.kakuhansyuuryou4_time = kakuhansyuuryou4_time;
    }

    /**
     * 投入⑥
     * @return the tounyuu6
     */
    public FXHDD01 getTounyuu6() {
        return tounyuu6;
    }

    /**
     * 投入⑥
     * @param tounyuu6 the tounyuu6 to set
     */
    public void setTounyuu6(FXHDD01 tounyuu6) {
        this.tounyuu6 = tounyuu6;
    }

    /**
     * 撹拌時間規格⑤
     * @return the kakuhanjikankikaku5
     */
    public FXHDD01 getKakuhanjikankikaku5() {
        return kakuhanjikankikaku5;
    }

    /**
     * 撹拌時間規格⑤
     * @param kakuhanjikankikaku5 the kakuhanjikankikaku5 to set
     */
    public void setKakuhanjikankikaku5(FXHDD01 kakuhanjikankikaku5) {
        this.kakuhanjikankikaku5 = kakuhanjikankikaku5;
    }

    /**
     * 撹拌開始日⑤
     * @return the kakuhankaisi5_day
     */
    public FXHDD01 getKakuhankaisi5_day() {
        return kakuhankaisi5_day;
    }

    /**
     * 撹拌開始日⑤
     * @param kakuhankaisi5_day the kakuhankaisi5_day to set
     */
    public void setKakuhankaisi5_day(FXHDD01 kakuhankaisi5_day) {
        this.kakuhankaisi5_day = kakuhankaisi5_day;
    }

    /**
     * 撹拌開始時間⑤
     * @return the kakuhankaisi5_time
     */
    public FXHDD01 getKakuhankaisi5_time() {
        return kakuhankaisi5_time;
    }

    /**
     * 撹拌開始時間⑤
     * @param kakuhankaisi5_time the kakuhankaisi5_time to set
     */
    public void setKakuhankaisi5_time(FXHDD01 kakuhankaisi5_time) {
        this.kakuhankaisi5_time = kakuhankaisi5_time;
    }

    /**
     * 撹拌終了日⑤
     * @return the kakuhansyuuryou5_day
     */
    public FXHDD01 getKakuhansyuuryou5_day() {
        return kakuhansyuuryou5_day;
    }

    /**
     * 撹拌終了日⑤
     * @param kakuhansyuuryou5_day the kakuhansyuuryou5_day to set
     */
    public void setKakuhansyuuryou5_day(FXHDD01 kakuhansyuuryou5_day) {
        this.kakuhansyuuryou5_day = kakuhansyuuryou5_day;
    }

    /**
     * 撹拌終了時間⑤
     * @return the kakuhansyuuryou5_time
     */
    public FXHDD01 getKakuhansyuuryou5_time() {
        return kakuhansyuuryou5_time;
    }

    /**
     * 撹拌終了時間⑤
     * @param kakuhansyuuryou5_time the kakuhansyuuryou5_time to set
     */
    public void setKakuhansyuuryou5_time(FXHDD01 kakuhansyuuryou5_time) {
        this.kakuhansyuuryou5_time = kakuhansyuuryou5_time;
    }

    /**
     * 回転数変更
     * @return the kaitensuuhenkou
     */
    public FXHDD01 getKaitensuuhenkou() {
        return kaitensuuhenkou;
    }

    /**
     * 回転数変更
     * @param kaitensuuhenkou the kaitensuuhenkou to set
     */
    public void setKaitensuuhenkou(FXHDD01 kaitensuuhenkou) {
        this.kaitensuuhenkou = kaitensuuhenkou;
    }

    /**
     * 投入⑦
     * @return the tounyuu7
     */
    public FXHDD01 getTounyuu7() {
        return tounyuu7;
    }

    /**
     * 投入⑦
     * @param tounyuu7 the tounyuu7 to set
     */
    public void setTounyuu7(FXHDD01 tounyuu7) {
        this.tounyuu7 = tounyuu7;
    }

    /**
     * 投入⑧
     * @return the tounyuu8
     */
    public FXHDD01 getTounyuu8() {
        return tounyuu8;
    }

    /**
     * 投入⑧
     * @param tounyuu8 the tounyuu8 to set
     */
    public void setTounyuu8(FXHDD01 tounyuu8) {
        this.tounyuu8 = tounyuu8;
    }

    /**
     * 投入⑨
     * @return the tounyuu9
     */
    public FXHDD01 getTounyuu9() {
        return tounyuu9;
    }

    /**
     * 投入⑨
     * @param tounyuu9 the tounyuu9 to set
     */
    public void setTounyuu9(FXHDD01 tounyuu9) {
        this.tounyuu9 = tounyuu9;
    }

    /**
     * 撹拌時間規格⑥
     * @return the kakuhanjikankikaku6
     */
    public FXHDD01 getKakuhanjikankikaku6() {
        return kakuhanjikankikaku6;
    }

    /**
     * 撹拌時間規格⑥
     * @param kakuhanjikankikaku6 the kakuhanjikankikaku6 to set
     */
    public void setKakuhanjikankikaku6(FXHDD01 kakuhanjikankikaku6) {
        this.kakuhanjikankikaku6 = kakuhanjikankikaku6;
    }

    /**
     * 撹拌開始日⑥
     * @return the kakuhankaisi6_day
     */
    public FXHDD01 getKakuhankaisi6_day() {
        return kakuhankaisi6_day;
    }

    /**
     * 撹拌開始日⑥
     * @param kakuhankaisi6_day the kakuhankaisi6_day to set
     */
    public void setKakuhankaisi6_day(FXHDD01 kakuhankaisi6_day) {
        this.kakuhankaisi6_day = kakuhankaisi6_day;
    }

    /**
     * 撹拌開始時間⑥
     * @return the kakuhankaisi6_time
     */
    public FXHDD01 getKakuhankaisi6_time() {
        return kakuhankaisi6_time;
    }

    /**
     * 撹拌開始時間⑥
     * @param kakuhankaisi6_time the kakuhankaisi6_time to set
     */
    public void setKakuhankaisi6_time(FXHDD01 kakuhankaisi6_time) {
        this.kakuhankaisi6_time = kakuhankaisi6_time;
    }

    /**
     * 撹拌終了日⑥
     * @return the kakuhansyuuryou6_day
     */
    public FXHDD01 getKakuhansyuuryou6_day() {
        return kakuhansyuuryou6_day;
    }

    /**
     * 撹拌終了日⑥
     * @param kakuhansyuuryou6_day the kakuhansyuuryou6_day to set
     */
    public void setKakuhansyuuryou6_day(FXHDD01 kakuhansyuuryou6_day) {
        this.kakuhansyuuryou6_day = kakuhansyuuryou6_day;
    }

    /**
     * 撹拌終了時間⑥
     * @return the kakuhansyuuryou6_time
     */
    public FXHDD01 getKakuhansyuuryou6_time() {
        return kakuhansyuuryou6_time;
    }

    /**
     * 撹拌終了時間⑥
     * @param kakuhansyuuryou6_time the kakuhansyuuryou6_time to set
     */
    public void setKakuhansyuuryou6_time(FXHDD01 kakuhansyuuryou6_time) {
        this.kakuhansyuuryou6_time = kakuhansyuuryou6_time;
    }

    /**
     * 投入⑩
     * @return the tounyuu10
     */
    public FXHDD01 getTounyuu10() {
        return tounyuu10;
    }

    /**
     * 投入⑩
     * @param tounyuu10 the tounyuu10 to set
     */
    public void setTounyuu10(FXHDD01 tounyuu10) {
        this.tounyuu10 = tounyuu10;
    }

    /**
     * 投入⑪
     * @return the tounyuu11
     */
    public FXHDD01 getTounyuu11() {
        return tounyuu11;
    }

    /**
     * 投入⑪
     * @param tounyuu11 the tounyuu11 to set
     */
    public void setTounyuu11(FXHDD01 tounyuu11) {
        this.tounyuu11 = tounyuu11;
    }

    /**
     * 投入⑫
     * @return the tounyuu12
     */
    public FXHDD01 getTounyuu12() {
        return tounyuu12;
    }

    /**
     * 投入⑫
     * @param tounyuu12 the tounyuu12 to set
     */
    public void setTounyuu12(FXHDD01 tounyuu12) {
        this.tounyuu12 = tounyuu12;
    }

    /**
     * 投入⑬
     * @return the tounyuu13
     */
    public FXHDD01 getTounyuu13() {
        return tounyuu13;
    }

    /**
     * 投入⑬
     * @param tounyuu13 the tounyuu13 to set
     */
    public void setTounyuu13(FXHDD01 tounyuu13) {
        this.tounyuu13 = tounyuu13;
    }

    /**
     * 投入⑭
     * @return the tounyuu14
     */
    public FXHDD01 getTounyuu14() {
        return tounyuu14;
    }

    /**
     * 投入⑭
     * @param tounyuu14 the tounyuu14 to set
     */
    public void setTounyuu14(FXHDD01 tounyuu14) {
        this.tounyuu14 = tounyuu14;
    }

    /**
     * 投入⑮
     * @return the tounyuu15
     */
    public FXHDD01 getTounyuu15() {
        return tounyuu15;
    }

    /**
     * 投入⑮
     * @param tounyuu15 the tounyuu15 to set
     */
    public void setTounyuu15(FXHDD01 tounyuu15) {
        this.tounyuu15 = tounyuu15;
    }

    /**
     * 投入⑯
     * @return the tounyuu16
     */
    public FXHDD01 getTounyuu16() {
        return tounyuu16;
    }

    /**
     * 投入⑯
     * @param tounyuu16 the tounyuu16 to set
     */
    public void setTounyuu16(FXHDD01 tounyuu16) {
        this.tounyuu16 = tounyuu16;
    }

    /**
     * 投入⑰
     * @return the tounyuu17
     */
    public FXHDD01 getTounyuu17() {
        return tounyuu17;
    }

    /**
     * 投入⑰
     * @param tounyuu17 the tounyuu17 to set
     */
    public void setTounyuu17(FXHDD01 tounyuu17) {
        this.tounyuu17 = tounyuu17;
    }

    /**
     * 投入⑱
     * @return the tounyuu18
     */
    public FXHDD01 getTounyuu18() {
        return tounyuu18;
    }

    /**
     * 投入⑱
     * @param tounyuu18 the tounyuu18 to set
     */
    public void setTounyuu18(FXHDD01 tounyuu18) {
        this.tounyuu18 = tounyuu18;
    }

    /**
     * 投入⑲
     * @return the tounyuu19
     */
    public FXHDD01 getTounyuu19() {
        return tounyuu19;
    }

    /**
     * 投入⑲
     * @param tounyuu19 the tounyuu19 to set
     */
    public void setTounyuu19(FXHDD01 tounyuu19) {
        this.tounyuu19 = tounyuu19;
    }

    /**
     * 投入終了日
     * @return the tounyuusyuuryou_day
     */
    public FXHDD01 getTounyuusyuuryou_day() {
        return tounyuusyuuryou_day;
    }

    /**
     * 投入終了日
     * @param tounyuusyuuryou_day the tounyuusyuuryou_day to set
     */
    public void setTounyuusyuuryou_day(FXHDD01 tounyuusyuuryou_day) {
        this.tounyuusyuuryou_day = tounyuusyuuryou_day;
    }

    /**
     * 投入終了時間
     * @return the tounyuusyuuryou_time
     */
    public FXHDD01 getTounyuusyuuryou_time() {
        return tounyuusyuuryou_time;
    }

    /**
     * 投入終了時間
     * @param tounyuusyuuryou_time the tounyuusyuuryou_time to set
     */
    public void setTounyuusyuuryou_time(FXHDD01 tounyuusyuuryou_time) {
        this.tounyuusyuuryou_time = tounyuusyuuryou_time;
    }

    /**
     * 撹拌機
     * @return the kakuhanki
     */
    public FXHDD01 getKakuhanki() {
        return kakuhanki;
    }

    /**
     * 撹拌機
     * @param kakuhanki the kakuhanki to set
     */
    public void setKakuhanki(FXHDD01 kakuhanki) {
        this.kakuhanki = kakuhanki;
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
     * 撹拌時間規格⑦
     * @return the kakuhanjikankikaku7
     */
    public FXHDD01 getKakuhanjikankikaku7() {
        return kakuhanjikankikaku7;
    }

    /**
     * 撹拌時間規格⑦
     * @param kakuhanjikankikaku7 the kakuhanjikankikaku7 to set
     */
    public void setKakuhanjikankikaku7(FXHDD01 kakuhanjikankikaku7) {
        this.kakuhanjikankikaku7 = kakuhanjikankikaku7;
    }

    /**
     * 撹拌開始日⑦
     * @return the kakuhankaisi7_day
     */
    public FXHDD01 getKakuhankaisi7_day() {
        return kakuhankaisi7_day;
    }

    /**
     * 撹拌開始日⑦
     * @param kakuhankaisi7_day the kakuhankaisi7_day to set
     */
    public void setKakuhankaisi7_day(FXHDD01 kakuhankaisi7_day) {
        this.kakuhankaisi7_day = kakuhankaisi7_day;
    }

    /**
     * 撹拌開始時間⑦
     * @return the kakuhankaisi7_time
     */
    public FXHDD01 getKakuhankaisi7_time() {
        return kakuhankaisi7_time;
    }

    /**
     * 撹拌開始時間⑦
     * @param kakuhankaisi7_time the kakuhankaisi7_time to set
     */
    public void setKakuhankaisi7_time(FXHDD01 kakuhankaisi7_time) {
        this.kakuhankaisi7_time = kakuhankaisi7_time;
    }

    /**
     * 回転体への接触の確認
     * @return the kaitentaihesesyokukakunin
     */
    public FXHDD01 getKaitentaihesesyokukakunin() {
        return kaitentaihesesyokukakunin;
    }

    /**
     * 回転体への接触の確認
     * @param kaitentaihesesyokukakunin the kaitentaihesesyokukakunin to set
     */
    public void setKaitentaihesesyokukakunin(FXHDD01 kaitentaihesesyokukakunin) {
        this.kaitentaihesesyokukakunin = kaitentaihesesyokukakunin;
    }

    /**
     * 撹拌終了日⑦
     * @return the kakuhansyuuryou7_day
     */
    public FXHDD01 getKakuhansyuuryou7_day() {
        return kakuhansyuuryou7_day;
    }

    /**
     * 撹拌終了日⑦
     * @param kakuhansyuuryou7_day the kakuhansyuuryou7_day to set
     */
    public void setKakuhansyuuryou7_day(FXHDD01 kakuhansyuuryou7_day) {
        this.kakuhansyuuryou7_day = kakuhansyuuryou7_day;
    }

    /**
     * 撹拌終了時間⑦
     * @return the kakuhansyuuryou7_time
     */
    public FXHDD01 getKakuhansyuuryou7_time() {
        return kakuhansyuuryou7_time;
    }

    /**
     * 撹拌終了時間⑦
     * @param kakuhansyuuryou7_time the kakuhansyuuryou7_time to set
     */
    public void setKakuhansyuuryou7_time(FXHDD01 kakuhansyuuryou7_time) {
        this.kakuhansyuuryou7_time = kakuhansyuuryou7_time;
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