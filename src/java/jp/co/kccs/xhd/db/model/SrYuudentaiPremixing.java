/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.sql.Timestamp;

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
 * SR_YUUDENTAI_PREMIXING(誘電体ｽﾗﾘｰ作製・ﾌﾟﾚﾐｷｼﾝｸﾞ)のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/11/12
 */
public class SrYuudentaiPremixing {
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
     * 溶剤洗浄_撹拌機
     */
    private String youzaisenjyou_kakuhanki;

    /**
     * 溶剤洗浄_使用ﾀﾝｸ
     */
    private String youzaisenjyou_siyoutank;

    /**
     * 溶剤量
     */
    private String youzairyou;

    /**
     * 溶剤投入
     */
    private Integer youzaitounyuu;

    /**
     * 主軸
     */
    private String syuziku;

    /**
     * ﾎﾟﾝﾌﾟ
     */
    private String pump;

    /**
     * 洗浄条件_ﾃﾞｨｽﾊﾟ回転数
     */
    private String senjyoujyouken_dispakaitensuu;

    /**
     * ｾﾊﾟﾚｰﾀ
     */
    private String separate;

    /**
     * ｱｼﾞﾃｰﾀ
     */
    private String agitator;

    /**
     * 自動運転
     */
    private Integer jidouunten;

    /**
     * ﾀﾝｸAB循環
     */
    private Integer tankABjyunkan;

    /**
     * ﾊﾟｽ数
     */
    private String passsuu;

    /**
     * 洗浄時間
     */
    private String senjyoujikan;

    /**
     * ﾗｯﾌﾟﾀｲﾑ
     */
    private String raptime;

    /**
     * ﾌﾟﾚﾐｷ_撹拌機
     */
    private String premixing_kakuhanki;

    /**
     * ﾌﾟﾚﾐｷ_使用ﾀﾝｸ
     */
    private String premixing_siyoutank;

    /**
     * ｱｰｽｸﾞﾘｯﾌﾟ接続確認
     */
    private Integer earthgripsetuzokukakunin;

    /**
     * ﾌﾟﾚﾐｷ_ﾃﾞｨｽﾊﾟ回転数
     */
    private String premixing_dispakaitensuu;

    /**
     * 投入開始日時
     */
    private Timestamp tounyuukaisinichiji;

    /**
     * 投入①
     */
    private String tounyuu1;

    /**
     * 投入②
     */
    private String tounyuu2;

    /**
     * 撹拌時間規格①
     */
    private String kakuhanjikankikaku1;

    /**
     * 撹拌開始日時①
     */
    private Timestamp kakuhankaisinichiji1;

    /**
     * 撹拌終了日時①
     */
    private Timestamp kakuhansyuuryounichiji1;

    /**
     * 投入③
     */
    private String tounyuu3;

    /**
     * 撹拌時間規格②
     */
    private String kakuhanjikankikaku2;

    /**
     * 撹拌開始日時②
     */
    private Timestamp kakuhankaisinichiji2;

    /**
     * 撹拌終了日時②
     */
    private Timestamp kakuhansyuuryounichiji2;

    /**
     * 投入④
     */
    private String tounyuu4;

    /**
     * 撹拌時間規格③
     */
    private String kakuhanjikankikaku3;

    /**
     * 撹拌開始日時③
     */
    private Timestamp kakuhankaisinichiji3;

    /**
     * 撹拌終了日時③
     */
    private Timestamp kakuhansyuuryounichiji3;

    /**
     * 投入⑤
     */
    private String tounyuu5;

    /**
     * 撹拌時間規格④
     */
    private String kakuhanjikankikaku4;

    /**
     * 撹拌開始日時④
     */
    private Timestamp kakuhankaisinichiji4;

    /**
     * 撹拌終了日時④
     */
    private Timestamp kakuhansyuuryounichiji4;

    /**
     * 投入⑥
     */
    private String tounyuu6;

    /**
     * 撹拌時間規格⑤
     */
    private String kakuhanjikankikaku5;

    /**
     * 撹拌開始日時⑤
     */
    private Timestamp kakuhankaisinichiji5;

    /**
     * 撹拌終了日時⑤
     */
    private Timestamp kakuhansyuuryounichiji5;

    /**
     * 回転数変更
     */
    private String kaitensuuhenkou;

    /**
     * 投入⑦
     */
    private String tounyuu7;

    /**
     * 投入⑧
     */
    private String tounyuu8;

    /**
     * 投入⑨
     */
    private String tounyuu9;

    /**
     * 撹拌時間規格⑥
     */
    private String kakuhanjikankikaku6;

    /**
     * 撹拌開始日時⑥
     */
    private Timestamp kakuhankaisinichiji6;

    /**
     * 撹拌終了日時⑥
     */
    private Timestamp kakuhansyuuryounichiji6;

    /**
     * 投入⑩
     */
    private String tounyuu10;

    /**
     * 投入⑪
     */
    private String tounyuu11;

    /**
     * 投入⑫
     */
    private String tounyuu12;

    /**
     * 投入⑬
     */
    private String tounyuu13;

    /**
     * 投入⑭
     */
    private String tounyuu14;

    /**
     * 投入⑮
     */
    private String tounyuu15;

    /**
     * 投入⑯
     */
    private String tounyuu16;

    /**
     * 投入⑰
     */
    private String tounyuu17;

    /**
     * 投入⑱
     */
    private String tounyuu18;

    /**
     * 投入⑲
     */
    private String tounyuu19;

    /**
     * 投入終了日時
     */
    private Timestamp tounyuusyuuryounichiji;

    /**
     * 撹拌機
     */
    private String kakuhanki;

    /**
     * 回転数
     */
    private String kaitensuu;

    /**
     * 撹拌時間規格⑦
     */
    private String kakuhanjikankikaku7;

    /**
     * 撹拌開始日時⑦
     */
    private Timestamp kakuhankaisinichiji7;

    /**
     * 回転体への接触の確認
     */
    private Integer kaitentaihenosessyokunokakunin;

    /**
     * 撹拌終了日時⑦
     */
    private Timestamp kakuhansyuuryounichiji7;

    /**
     * 担当者
     */
    private String tantousya;

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
     * 溶剤洗浄_撹拌機
     * @return the youzaisenjyou_kakuhanki
     */
    public String getYouzaisenjyou_kakuhanki() {
        return youzaisenjyou_kakuhanki;
    }

    /**
     * 溶剤洗浄_撹拌機
     * @param youzaisenjyou_kakuhanki the youzaisenjyou_kakuhanki to set
     */
    public void setYouzaisenjyou_kakuhanki(String youzaisenjyou_kakuhanki) {
        this.youzaisenjyou_kakuhanki = youzaisenjyou_kakuhanki;
    }

    /**
     * 溶剤洗浄_使用ﾀﾝｸ
     * @return the youzaisenjyou_siyoutank
     */
    public String getYouzaisenjyou_siyoutank() {
        return youzaisenjyou_siyoutank;
    }

    /**
     * 溶剤洗浄_使用ﾀﾝｸ
     * @param youzaisenjyou_siyoutank the youzaisenjyou_siyoutank to set
     */
    public void setYouzaisenjyou_siyoutank(String youzaisenjyou_siyoutank) {
        this.youzaisenjyou_siyoutank = youzaisenjyou_siyoutank;
    }

    /**
     * 溶剤量
     * @return the youzairyou
     */
    public String getYouzairyou() {
        return youzairyou;
    }

    /**
     * 溶剤量
     * @param youzairyou the youzairyou to set
     */
    public void setYouzairyou(String youzairyou) {
        this.youzairyou = youzairyou;
    }

    /**
     * 溶剤投入
     * @return the youzaitounyuu
     */
    public Integer getYouzaitounyuu() {
        return youzaitounyuu;
    }

    /**
     * 溶剤投入
     * @param youzaitounyuu the youzaitounyuu to set
     */
    public void setYouzaitounyuu(Integer youzaitounyuu) {
        this.youzaitounyuu = youzaitounyuu;
    }

    /**
     * 主軸
     * @return the syuziku
     */
    public String getSyuziku() {
        return syuziku;
    }

    /**
     * 主軸
     * @param syuziku the syuziku to set
     */
    public void setSyuziku(String syuziku) {
        this.syuziku = syuziku;
    }

    /**
     * ﾎﾟﾝﾌﾟ
     * @return the pump
     */
    public String getPump() {
        return pump;
    }

    /**
     * ﾎﾟﾝﾌﾟ
     * @param pump the pump to set
     */
    public void setPump(String pump) {
        this.pump = pump;
    }

    /**
     * 洗浄条件_ﾃﾞｨｽﾊﾟ回転数
     * @return the senjyoujyouken_dispakaitensuu
     */
    public String getSenjyoujyouken_dispakaitensuu() {
        return senjyoujyouken_dispakaitensuu;
    }

    /**
     * 洗浄条件_ﾃﾞｨｽﾊﾟ回転数
     * @param senjyoujyouken_dispakaitensuu the senjyoujyouken_dispakaitensuu to set
     */
    public void setSenjyoujyouken_dispakaitensuu(String senjyoujyouken_dispakaitensuu) {
        this.senjyoujyouken_dispakaitensuu = senjyoujyouken_dispakaitensuu;
    }

    /**
     * ｾﾊﾟﾚｰﾀ
     * @return the separate
     */
    public String getSeparate() {
        return separate;
    }

    /**
     * ｾﾊﾟﾚｰﾀ
     * @param separate the separate to set
     */
    public void setSeparate(String separate) {
        this.separate = separate;
    }

    /**
     * ｱｼﾞﾃｰﾀ
     * @return the agitator
     */
    public String getAgitator() {
        return agitator;
    }

    /**
     * ｱｼﾞﾃｰﾀ
     * @param agitator the agitator to set
     */
    public void setAgitator(String agitator) {
        this.agitator = agitator;
    }

    /**
     * 自動運転
     * @return the jidouunten
     */
    public Integer getJidouunten() {
        return jidouunten;
    }

    /**
     * 自動運転
     * @param jidouunten the jidouunten to set
     */
    public void setJidouunten(Integer jidouunten) {
        this.jidouunten = jidouunten;
    }

    /**
     * ﾀﾝｸAB循環
     * @return the tankABjyunkan
     */
    public Integer getTankABjyunkan() {
        return tankABjyunkan;
    }

    /**
     * ﾀﾝｸAB循環
     * @param tankABjyunkan the tankABjyunkan to set
     */
    public void setTankABjyunkan(Integer tankABjyunkan) {
        this.tankABjyunkan = tankABjyunkan;
    }

    /**
     * ﾊﾟｽ数
     * @return the passsuu
     */
    public String getPasssuu() {
        return passsuu;
    }

    /**
     * ﾊﾟｽ数
     * @param passsuu the passsuu to set
     */
    public void setPasssuu(String passsuu) {
        this.passsuu = passsuu;
    }

    /**
     * 洗浄時間
     * @return the senjyoujikan
     */
    public String getSenjyoujikan() {
        return senjyoujikan;
    }

    /**
     * 洗浄時間
     * @param senjyoujikan the senjyoujikan to set
     */
    public void setSenjyoujikan(String senjyoujikan) {
        this.senjyoujikan = senjyoujikan;
    }

    /**
     * ﾗｯﾌﾟﾀｲﾑ
     * @return the raptime
     */
    public String getRaptime() {
        return raptime;
    }

    /**
     * ﾗｯﾌﾟﾀｲﾑ
     * @param raptime the raptime to set
     */
    public void setRaptime(String raptime) {
        this.raptime = raptime;
    }

    /**
     * ﾌﾟﾚﾐｷ_撹拌機
     * @return the premixing_kakuhanki
     */
    public String getPremixing_kakuhanki() {
        return premixing_kakuhanki;
    }

    /**
     * ﾌﾟﾚﾐｷ_撹拌機
     * @param premixing_kakuhanki the premixing_kakuhanki to set
     */
    public void setPremixing_kakuhanki(String premixing_kakuhanki) {
        this.premixing_kakuhanki = premixing_kakuhanki;
    }

    /**
     * ﾌﾟﾚﾐｷ_使用ﾀﾝｸ
     * @return the premixing_siyoutank
     */
    public String getPremixing_siyoutank() {
        return premixing_siyoutank;
    }

    /**
     * ﾌﾟﾚﾐｷ_使用ﾀﾝｸ
     * @param premixing_siyoutank the premixing_siyoutank to set
     */
    public void setPremixing_siyoutank(String premixing_siyoutank) {
        this.premixing_siyoutank = premixing_siyoutank;
    }

    /**
     * ｱｰｽｸﾞﾘｯﾌﾟ接続確認
     * @return the earthgripsetuzokukakunin
     */
    public Integer getEarthgripsetuzokukakunin() {
        return earthgripsetuzokukakunin;
    }

    /**
     * ｱｰｽｸﾞﾘｯﾌﾟ接続確認
     * @param earthgripsetuzokukakunin the earthgripsetuzokukakunin to set
     */
    public void setEarthgripsetuzokukakunin(Integer earthgripsetuzokukakunin) {
        this.earthgripsetuzokukakunin = earthgripsetuzokukakunin;
    }

    /**
     * ﾌﾟﾚﾐｷ_ﾃﾞｨｽﾊﾟ回転数
     * @return the premixing_dispakaitensuu
     */
    public String getPremixing_dispakaitensuu() {
        return premixing_dispakaitensuu;
    }

    /**
     * ﾌﾟﾚﾐｷ_ﾃﾞｨｽﾊﾟ回転数
     * @param premixing_dispakaitensuu the premixing_dispakaitensuu to set
     */
    public void setPremixing_dispakaitensuu(String premixing_dispakaitensuu) {
        this.premixing_dispakaitensuu = premixing_dispakaitensuu;
    }

    /**
     * 投入開始日時
     * @return the tounyuukaisinichiji
     */
    public Timestamp getTounyuukaisinichiji() {
        return tounyuukaisinichiji;
    }

    /**
     * 投入開始日時
     * @param tounyuukaisinichiji the tounyuukaisinichiji to set
     */
    public void setTounyuukaisinichiji(Timestamp tounyuukaisinichiji) {
        this.tounyuukaisinichiji = tounyuukaisinichiji;
    }

    /**
     * 投入①
     * @return the tounyuu1
     */
    public String getTounyuu1() {
        return tounyuu1;
    }

    /**
     * 投入①
     * @param tounyuu1 the tounyuu1 to set
     */
    public void setTounyuu1(String tounyuu1) {
        this.tounyuu1 = tounyuu1;
    }

    /**
     * 投入②
     * @return the tounyuu2
     */
    public String getTounyuu2() {
        return tounyuu2;
    }

    /**
     * 投入②
     * @param tounyuu2 the tounyuu2 to set
     */
    public void setTounyuu2(String tounyuu2) {
        this.tounyuu2 = tounyuu2;
    }

    /**
     * 撹拌時間規格①
     * @return the kakuhanjikankikaku1
     */
    public String getKakuhanjikankikaku1() {
        return kakuhanjikankikaku1;
    }

    /**
     * 撹拌時間規格①
     * @param kakuhanjikankikaku1 the kakuhanjikankikaku1 to set
     */
    public void setKakuhanjikankikaku1(String kakuhanjikankikaku1) {
        this.kakuhanjikankikaku1 = kakuhanjikankikaku1;
    }

    /**
     * 撹拌開始日時①
     * @return the kakuhankaisinichiji1
     */
    public Timestamp getKakuhankaisinichiji1() {
        return kakuhankaisinichiji1;
    }

    /**
     * 撹拌開始日時①
     * @param kakuhankaisinichiji1 the kakuhankaisinichiji1 to set
     */
    public void setKakuhankaisinichiji1(Timestamp kakuhankaisinichiji1) {
        this.kakuhankaisinichiji1 = kakuhankaisinichiji1;
    }

    /**
     * 撹拌終了日時①
     * @return the kakuhansyuuryounichiji1
     */
    public Timestamp getKakuhansyuuryounichiji1() {
        return kakuhansyuuryounichiji1;
    }

    /**
     * 撹拌終了日時①
     * @param kakuhansyuuryounichiji1 the kakuhansyuuryounichiji1 to set
     */
    public void setKakuhansyuuryounichiji1(Timestamp kakuhansyuuryounichiji1) {
        this.kakuhansyuuryounichiji1 = kakuhansyuuryounichiji1;
    }

    /**
     * 投入③
     * @return the tounyuu3
     */
    public String getTounyuu3() {
        return tounyuu3;
    }

    /**
     * 投入③
     * @param tounyuu3 the tounyuu3 to set
     */
    public void setTounyuu3(String tounyuu3) {
        this.tounyuu3 = tounyuu3;
    }

    /**
     * 撹拌時間規格②
     * @return the kakuhanjikankikaku2
     */
    public String getKakuhanjikankikaku2() {
        return kakuhanjikankikaku2;
    }

    /**
     * 撹拌時間規格②
     * @param kakuhanjikankikaku2 the kakuhanjikankikaku2 to set
     */
    public void setKakuhanjikankikaku2(String kakuhanjikankikaku2) {
        this.kakuhanjikankikaku2 = kakuhanjikankikaku2;
    }

    /**
     * 撹拌開始日時②
     * @return the kakuhankaisinichiji2
     */
    public Timestamp getKakuhankaisinichiji2() {
        return kakuhankaisinichiji2;
    }

    /**
     * 撹拌開始日時②
     * @param kakuhankaisinichiji2 the kakuhankaisinichiji2 to set
     */
    public void setKakuhankaisinichiji2(Timestamp kakuhankaisinichiji2) {
        this.kakuhankaisinichiji2 = kakuhankaisinichiji2;
    }

    /**
     * 撹拌終了日時②
     * @return the kakuhansyuuryounichiji2
     */
    public Timestamp getKakuhansyuuryounichiji2() {
        return kakuhansyuuryounichiji2;
    }

    /**
     * 撹拌終了日時②
     * @param kakuhansyuuryounichiji2 the kakuhansyuuryounichiji2 to set
     */
    public void setKakuhansyuuryounichiji2(Timestamp kakuhansyuuryounichiji2) {
        this.kakuhansyuuryounichiji2 = kakuhansyuuryounichiji2;
    }

    /**
     * 投入④
     * @return the tounyuu4
     */
    public String getTounyuu4() {
        return tounyuu4;
    }

    /**
     * 投入④
     * @param tounyuu4 the tounyuu4 to set
     */
    public void setTounyuu4(String tounyuu4) {
        this.tounyuu4 = tounyuu4;
    }

    /**
     * 撹拌時間規格③
     * @return the kakuhanjikankikaku3
     */
    public String getKakuhanjikankikaku3() {
        return kakuhanjikankikaku3;
    }

    /**
     * 撹拌時間規格③
     * @param kakuhanjikankikaku3 the kakuhanjikankikaku3 to set
     */
    public void setKakuhanjikankikaku3(String kakuhanjikankikaku3) {
        this.kakuhanjikankikaku3 = kakuhanjikankikaku3;
    }

    /**
     * 撹拌開始日時③
     * @return the kakuhankaisinichiji3
     */
    public Timestamp getKakuhankaisinichiji3() {
        return kakuhankaisinichiji3;
    }

    /**
     * 撹拌開始日時③
     * @param kakuhankaisinichiji3 the kakuhankaisinichiji3 to set
     */
    public void setKakuhankaisinichiji3(Timestamp kakuhankaisinichiji3) {
        this.kakuhankaisinichiji3 = kakuhankaisinichiji3;
    }

    /**
     * 撹拌終了日時③
     * @return the kakuhansyuuryounichiji3
     */
    public Timestamp getKakuhansyuuryounichiji3() {
        return kakuhansyuuryounichiji3;
    }

    /**
     * 撹拌終了日時③
     * @param kakuhansyuuryounichiji3 the kakuhansyuuryounichiji3 to set
     */
    public void setKakuhansyuuryounichiji3(Timestamp kakuhansyuuryounichiji3) {
        this.kakuhansyuuryounichiji3 = kakuhansyuuryounichiji3;
    }

    /**
     * 投入⑤
     * @return the tounyuu5
     */
    public String getTounyuu5() {
        return tounyuu5;
    }

    /**
     * 投入⑤
     * @param tounyuu5 the tounyuu5 to set
     */
    public void setTounyuu5(String tounyuu5) {
        this.tounyuu5 = tounyuu5;
    }

    /**
     * 撹拌時間規格④
     * @return the kakuhanjikankikaku4
     */
    public String getKakuhanjikankikaku4() {
        return kakuhanjikankikaku4;
    }

    /**
     * 撹拌時間規格④
     * @param kakuhanjikankikaku4 the kakuhanjikankikaku4 to set
     */
    public void setKakuhanjikankikaku4(String kakuhanjikankikaku4) {
        this.kakuhanjikankikaku4 = kakuhanjikankikaku4;
    }

    /**
     * 撹拌開始日時④
     * @return the kakuhankaisinichiji4
     */
    public Timestamp getKakuhankaisinichiji4() {
        return kakuhankaisinichiji4;
    }

    /**
     * 撹拌開始日時④
     * @param kakuhankaisinichiji4 the kakuhankaisinichiji4 to set
     */
    public void setKakuhankaisinichiji4(Timestamp kakuhankaisinichiji4) {
        this.kakuhankaisinichiji4 = kakuhankaisinichiji4;
    }

    /**
     * 撹拌終了日時④
     * @return the kakuhansyuuryounichiji4
     */
    public Timestamp getKakuhansyuuryounichiji4() {
        return kakuhansyuuryounichiji4;
    }

    /**
     * 撹拌終了日時④
     * @param kakuhansyuuryounichiji4 the kakuhansyuuryounichiji4 to set
     */
    public void setKakuhansyuuryounichiji4(Timestamp kakuhansyuuryounichiji4) {
        this.kakuhansyuuryounichiji4 = kakuhansyuuryounichiji4;
    }

    /**
     * 投入⑥
     * @return the tounyuu6
     */
    public String getTounyuu6() {
        return tounyuu6;
    }

    /**
     * 投入⑥
     * @param tounyuu6 the tounyuu6 to set
     */
    public void setTounyuu6(String tounyuu6) {
        this.tounyuu6 = tounyuu6;
    }

    /**
     * 撹拌時間規格⑤
     * @return the kakuhanjikankikaku5
     */
    public String getKakuhanjikankikaku5() {
        return kakuhanjikankikaku5;
    }

    /**
     * 撹拌時間規格⑤
     * @param kakuhanjikankikaku5 the kakuhanjikankikaku5 to set
     */
    public void setKakuhanjikankikaku5(String kakuhanjikankikaku5) {
        this.kakuhanjikankikaku5 = kakuhanjikankikaku5;
    }

    /**
     * 撹拌開始日時⑤
     * @return the kakuhankaisinichiji5
     */
    public Timestamp getKakuhankaisinichiji5() {
        return kakuhankaisinichiji5;
    }

    /**
     * 撹拌開始日時⑤
     * @param kakuhankaisinichiji5 the kakuhankaisinichiji5 to set
     */
    public void setKakuhankaisinichiji5(Timestamp kakuhankaisinichiji5) {
        this.kakuhankaisinichiji5 = kakuhankaisinichiji5;
    }

    /**
     * 撹拌終了日時⑤
     * @return the kakuhansyuuryounichiji5
     */
    public Timestamp getKakuhansyuuryounichiji5() {
        return kakuhansyuuryounichiji5;
    }

    /**
     * 撹拌終了日時⑤
     * @param kakuhansyuuryounichiji5 the kakuhansyuuryounichiji5 to set
     */
    public void setKakuhansyuuryounichiji5(Timestamp kakuhansyuuryounichiji5) {
        this.kakuhansyuuryounichiji5 = kakuhansyuuryounichiji5;
    }

    /**
     * 回転数変更
     * @return the kaitensuuhenkou
     */
    public String getKaitensuuhenkou() {
        return kaitensuuhenkou;
    }

    /**
     * 回転数変更
     * @param kaitensuuhenkou the kaitensuuhenkou to set
     */
    public void setKaitensuuhenkou(String kaitensuuhenkou) {
        this.kaitensuuhenkou = kaitensuuhenkou;
    }

    /**
     * 投入⑦
     * @return the tounyuu7
     */
    public String getTounyuu7() {
        return tounyuu7;
    }

    /**
     * 投入⑦
     * @param tounyuu7 the tounyuu7 to set
     */
    public void setTounyuu7(String tounyuu7) {
        this.tounyuu7 = tounyuu7;
    }

    /**
     * 投入⑧
     * @return the tounyuu8
     */
    public String getTounyuu8() {
        return tounyuu8;
    }

    /**
     * 投入⑧
     * @param tounyuu8 the tounyuu8 to set
     */
    public void setTounyuu8(String tounyuu8) {
        this.tounyuu8 = tounyuu8;
    }

    /**
     * 投入⑨
     * @return the tounyuu9
     */
    public String getTounyuu9() {
        return tounyuu9;
    }

    /**
     * 投入⑨
     * @param tounyuu9 the tounyuu9 to set
     */
    public void setTounyuu9(String tounyuu9) {
        this.tounyuu9 = tounyuu9;
    }

    /**
     * 撹拌時間規格⑥
     * @return the kakuhanjikankikaku6
     */
    public String getKakuhanjikankikaku6() {
        return kakuhanjikankikaku6;
    }

    /**
     * 撹拌時間規格⑥
     * @param kakuhanjikankikaku6 the kakuhanjikankikaku6 to set
     */
    public void setKakuhanjikankikaku6(String kakuhanjikankikaku6) {
        this.kakuhanjikankikaku6 = kakuhanjikankikaku6;
    }

    /**
     * 撹拌開始日時⑥
     * @return the kakuhankaisinichiji6
     */
    public Timestamp getKakuhankaisinichiji6() {
        return kakuhankaisinichiji6;
    }

    /**
     * 撹拌開始日時⑥
     * @param kakuhankaisinichiji6 the kakuhankaisinichiji6 to set
     */
    public void setKakuhankaisinichiji6(Timestamp kakuhankaisinichiji6) {
        this.kakuhankaisinichiji6 = kakuhankaisinichiji6;
    }

    /**
     * 撹拌終了日時⑥
     * @return the kakuhansyuuryounichiji6
     */
    public Timestamp getKakuhansyuuryounichiji6() {
        return kakuhansyuuryounichiji6;
    }

    /**
     * 撹拌終了日時⑥
     * @param kakuhansyuuryounichiji6 the kakuhansyuuryounichiji6 to set
     */
    public void setKakuhansyuuryounichiji6(Timestamp kakuhansyuuryounichiji6) {
        this.kakuhansyuuryounichiji6 = kakuhansyuuryounichiji6;
    }

    /**
     * 投入⑩
     * @return the tounyuu10
     */
    public String getTounyuu10() {
        return tounyuu10;
    }

    /**
     * 投入⑩
     * @param tounyuu10 the tounyuu10 to set
     */
    public void setTounyuu10(String tounyuu10) {
        this.tounyuu10 = tounyuu10;
    }

    /**
     * 投入⑪
     * @return the tounyuu11
     */
    public String getTounyuu11() {
        return tounyuu11;
    }

    /**
     * 投入⑪
     * @param tounyuu11 the tounyuu11 to set
     */
    public void setTounyuu11(String tounyuu11) {
        this.tounyuu11 = tounyuu11;
    }

    /**
     * 投入⑫
     * @return the tounyuu12
     */
    public String getTounyuu12() {
        return tounyuu12;
    }

    /**
     * 投入⑫
     * @param tounyuu12 the tounyuu12 to set
     */
    public void setTounyuu12(String tounyuu12) {
        this.tounyuu12 = tounyuu12;
    }

    /**
     * 投入⑬
     * @return the tounyuu13
     */
    public String getTounyuu13() {
        return tounyuu13;
    }

    /**
     * 投入⑬
     * @param tounyuu13 the tounyuu13 to set
     */
    public void setTounyuu13(String tounyuu13) {
        this.tounyuu13 = tounyuu13;
    }

    /**
     * 投入⑭
     * @return the tounyuu14
     */
    public String getTounyuu14() {
        return tounyuu14;
    }

    /**
     * 投入⑭
     * @param tounyuu14 the tounyuu14 to set
     */
    public void setTounyuu14(String tounyuu14) {
        this.tounyuu14 = tounyuu14;
    }

    /**
     * 投入⑮
     * @return the tounyuu15
     */
    public String getTounyuu15() {
        return tounyuu15;
    }

    /**
     * 投入⑮
     * @param tounyuu15 the tounyuu15 to set
     */
    public void setTounyuu15(String tounyuu15) {
        this.tounyuu15 = tounyuu15;
    }

    /**
     * 投入⑯
     * @return the tounyuu16
     */
    public String getTounyuu16() {
        return tounyuu16;
    }

    /**
     * 投入⑯
     * @param tounyuu16 the tounyuu16 to set
     */
    public void setTounyuu16(String tounyuu16) {
        this.tounyuu16 = tounyuu16;
    }

    /**
     * 投入⑰
     * @return the tounyuu17
     */
    public String getTounyuu17() {
        return tounyuu17;
    }

    /**
     * 投入⑰
     * @param tounyuu17 the tounyuu17 to set
     */
    public void setTounyuu17(String tounyuu17) {
        this.tounyuu17 = tounyuu17;
    }

    /**
     * 投入⑱
     * @return the tounyuu18
     */
    public String getTounyuu18() {
        return tounyuu18;
    }

    /**
     * 投入⑱
     * @param tounyuu18 the tounyuu18 to set
     */
    public void setTounyuu18(String tounyuu18) {
        this.tounyuu18 = tounyuu18;
    }

    /**
     * 投入⑲
     * @return the tounyuu19
     */
    public String getTounyuu19() {
        return tounyuu19;
    }

    /**
     * 投入⑲
     * @param tounyuu19 the tounyuu19 to set
     */
    public void setTounyuu19(String tounyuu19) {
        this.tounyuu19 = tounyuu19;
    }

    /**
     * 投入終了日時
     * @return the tounyuusyuuryounichiji
     */
    public Timestamp getTounyuusyuuryounichiji() {
        return tounyuusyuuryounichiji;
    }

    /**
     * 投入終了日時
     * @param tounyuusyuuryounichiji the tounyuusyuuryounichiji to set
     */
    public void setTounyuusyuuryounichiji(Timestamp tounyuusyuuryounichiji) {
        this.tounyuusyuuryounichiji = tounyuusyuuryounichiji;
    }

    /**
     * 撹拌機
     * @return the kakuhanki
     */
    public String getKakuhanki() {
        return kakuhanki;
    }

    /**
     * 撹拌機
     * @param kakuhanki the kakuhanki to set
     */
    public void setKakuhanki(String kakuhanki) {
        this.kakuhanki = kakuhanki;
    }

    /**
     * 回転数
     * @return the kaitensuu
     */
    public String getKaitensuu() {
        return kaitensuu;
    }

    /**
     * 回転数
     * @param kaitensuu the kaitensuu to set
     */
    public void setKaitensuu(String kaitensuu) {
        this.kaitensuu = kaitensuu;
    }

    /**
     * 撹拌時間規格⑦
     * @return the kakuhanjikankikaku7
     */
    public String getKakuhanjikankikaku7() {
        return kakuhanjikankikaku7;
    }

    /**
     * 撹拌時間規格⑦
     * @param kakuhanjikankikaku7 the kakuhanjikankikaku7 to set
     */
    public void setKakuhanjikankikaku7(String kakuhanjikankikaku7) {
        this.kakuhanjikankikaku7 = kakuhanjikankikaku7;
    }

    /**
     * 撹拌開始日時⑦
     * @return the kakuhankaisinichiji7
     */
    public Timestamp getKakuhankaisinichiji7() {
        return kakuhankaisinichiji7;
    }

    /**
     * 撹拌開始日時⑦
     * @param kakuhankaisinichiji7 the kakuhankaisinichiji7 to set
     */
    public void setKakuhankaisinichiji7(Timestamp kakuhankaisinichiji7) {
        this.kakuhankaisinichiji7 = kakuhankaisinichiji7;
    }

    /**
     * 回転体への接触の確認
     * @return the kaitentaihenosessyokunokakunin
     */
    public Integer getKaitentaihenosessyokunokakunin() {
        return kaitentaihenosessyokunokakunin;
    }

    /**
     * 回転体への接触の確認
     * @param kaitentaihenosessyokunokakunin the kaitentaihenosessyokunokakunin to set
     */
    public void setKaitentaihenosessyokunokakunin(Integer kaitentaihenosessyokunokakunin) {
        this.kaitentaihenosessyokunokakunin = kaitentaihenosessyokunokakunin;
    }

    /**
     * 撹拌終了日時⑦
     * @return the kakuhansyuuryounichiji7
     */
    public Timestamp getKakuhansyuuryounichiji7() {
        return kakuhansyuuryounichiji7;
    }

    /**
     * 撹拌終了日時⑦
     * @param kakuhansyuuryounichiji7 the kakuhansyuuryounichiji7 to set
     */
    public void setKakuhansyuuryounichiji7(Timestamp kakuhansyuuryounichiji7) {
        this.kakuhansyuuryounichiji7 = kakuhansyuuryounichiji7;
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