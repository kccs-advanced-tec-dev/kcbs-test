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
 * 変更日	2021/10/15<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SR_TENKA_PREMIXING(添加材ｽﾗﾘｰ作製・予備混合)のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/10/15
 */
public class SrTenkaPremixing {
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
     * 撹拌機
     */
    private String kakuhanki;

    /**
     * ﾀﾝｸ
     */
    private String tanku;

    /**
     * 溶剤
     */
    private String youzai;

    /**
     * 洗浄時間
     */
    private String senjojikan;

    /**
     * 主軸
     */
    private String shujiku;

    /**
     * ﾎﾟﾝﾌﾟ
     */
    private String ponpu;

    /**
     * ﾃﾞｽﾊﾟ回転数
     */
    private String desubakaitensuu;

    /**
     * 投入①
     */
    private Integer tounyu1;

    /**
     * 投入②
     */
    private Integer tounyu2;

    /**
     * 途中撹拌時間
     */
    private String totyukakuhanjikan;

    /**
     * 途中撹拌開始日時
     */
    private Timestamp totyukakuhankaisinichiji;

    /**
     * 途中撹拌終了日時
     */
    private Timestamp totyukakuhansyuryonichiji;

    /**
     * 投入③
     */
    private Integer tonyu3;

    /**
     * 投入④
     */
    private Integer tonyu4;

    /**
     * 投入⑤
     */
    private Integer tonyu5;

    /**
     * 撹拌時間
     */
    private String kakuhanjikan;

    /**
     * 撹拌開始日時
     */
    private Timestamp kakuhankaisinichiji;

    /**
     * 撹拌終了日時
     */
    private Timestamp kakuhansyuryonichiji;

    /**
     * 回転体の接触確認
     */
    private Integer kaitentai;

    /**
     * 担当者
     */
    private String tantousya;

    /**
     * 確認者
     */
    private String kakuninsya;

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
     * ﾀﾝｸ
     * @return the tanku
     */
    public String getTanku() {
        return tanku;
    }

    /**
     * ﾀﾝｸ
     * @param tanku the tanku to set
     */
    public void setTanku(String tanku) {
        this.tanku = tanku;
    }

    /**
     * 溶剤
     * @return the youzai
     */
    public String getYouzai() {
        return youzai;
    }

    /**
     * 溶剤
     * @param youzai the youzai to set
     */
    public void setYouzai(String youzai) {
        this.youzai = youzai;
    }

    /**
     * 洗浄時間
     * @return the senjojikan
     */
    public String getSenjojikan() {
        return senjojikan;
    }

    /**
     * 洗浄時間
     * @param senjojikan the senjojikan to set
     */
    public void setSenjojikan(String senjojikan) {
        this.senjojikan = senjojikan;
    }

    /**
     * 主軸
     * @return the shujiku
     */
    public String getShujiku() {
        return shujiku;
    }

    /**
     * 主軸
     * @param shujiku the shujiku to set
     */
    public void setShujiku(String shujiku) {
        this.shujiku = shujiku;
    }

    /**
     * ﾎﾟﾝﾌﾟ
     * @return the ponpu
     */
    public String getPonpu() {
        return ponpu;
    }

    /**
     * ﾎﾟﾝﾌﾟ
     * @param ponpu the ponpu to set
     */
    public void setPonpu(String ponpu) {
        this.ponpu = ponpu;
    }

    /**
     * ﾃﾞｽﾊﾟ回転数
     * @return the desubakaitensuu
     */
    public String getDesubakaitensuu() {
        return desubakaitensuu;
    }

    /**
     * ﾃﾞｽﾊﾟ回転数
     * @param desubakaitensuu the desubakaitensuu to set
     */
    public void setDesubakaitensuu(String desubakaitensuu) {
        this.desubakaitensuu = desubakaitensuu;
    }

    /**
     * 投入①
     * @return the tounyu1
     */
    public Integer getTounyu1() {
        return tounyu1;
    }

    /**
     * 投入①
     * @param tounyu1 the tounyu1 to set
     */
    public void setTounyu1(Integer tounyu1) {
        this.tounyu1 = tounyu1;
    }

    /**
     * 投入②
     * @return the tounyu2
     */
    public Integer getTounyu2() {
        return tounyu2;
    }

    /**
     * 投入②
     * @param tounyu2 the tounyu2 to set
     */
    public void setTounyu2(Integer tounyu2) {
        this.tounyu2 = tounyu2;
    }

    /**
     * 途中撹拌時間
     * @return the totyukakuhanjikan
     */
    public String getTotyukakuhanjikan() {
        return totyukakuhanjikan;
    }

    /**
     * 途中撹拌時間
     * @param totyukakuhanjikan the totyukakuhanjikan to set
     */
    public void setTotyukakuhanjikan(String totyukakuhanjikan) {
        this.totyukakuhanjikan = totyukakuhanjikan;
    }

    /**
     * 途中撹拌開始日時
     * @return the totyukakuhankaisinichiji
     */
    public Timestamp getTotyukakuhankaisinichiji() {
        return totyukakuhankaisinichiji;
    }

    /**
     * 途中撹拌開始日時
     * @param totyukakuhankaisinichiji the totyukakuhankaisinichiji to set
     */
    public void setTotyukakuhankaisinichiji(Timestamp totyukakuhankaisinichiji) {
        this.totyukakuhankaisinichiji = totyukakuhankaisinichiji;
    }

    /**
     * 途中撹拌終了日時
     * @return the totyukakuhansyuryonichiji
     */
    public Timestamp getTotyukakuhansyuryonichiji() {
        return totyukakuhansyuryonichiji;
    }

    /**
     * 途中撹拌終了日時
     * @param totyukakuhansyuryonichiji the totyukakuhansyuryonichiji to set
     */
    public void setTotyukakuhansyuryonichiji(Timestamp totyukakuhansyuryonichiji) {
        this.totyukakuhansyuryonichiji = totyukakuhansyuryonichiji;
    }

    /**
     * 投入③
     * @return the tonyu3
     */
    public Integer getTonyu3() {
        return tonyu3;
    }

    /**
     * 投入③
     * @param tonyu3 the tonyu3 to set
     */
    public void setTonyu3(Integer tonyu3) {
        this.tonyu3 = tonyu3;
    }

    /**
     * 投入④
     * @return the tonyu4
     */
    public Integer getTonyu4() {
        return tonyu4;
    }

    /**
     * 投入④
     * @param tonyu4 the tonyu4 to set
     */
    public void setTonyu4(Integer tonyu4) {
        this.tonyu4 = tonyu4;
    }

    /**
     * 投入⑤
     * @return the tonyu5
     */
    public Integer getTonyu5() {
        return tonyu5;
    }

    /**
     * 投入⑤
     * @param tonyu5 the tonyu5 to set
     */
    public void setTonyu5(Integer tonyu5) {
        this.tonyu5 = tonyu5;
    }

    /**
     * 撹拌時間
     * @return the kakuhanjikan
     */
    public String getKakuhanjikan() {
        return kakuhanjikan;
    }

    /**
     * 撹拌時間
     * @param kakuhanjikan the kakuhanjikan to set
     */
    public void setKakuhanjikan(String kakuhanjikan) {
        this.kakuhanjikan = kakuhanjikan;
    }

    /**
     * 撹拌開始日時
     * @return the kakuhankaisinichiji
     */
    public Timestamp getKakuhankaisinichiji() {
        return kakuhankaisinichiji;
    }

    /**
     * 撹拌開始日時
     * @param kakuhankaisinichiji the kakuhankaisinichiji to set
     */
    public void setKakuhankaisinichiji(Timestamp kakuhankaisinichiji) {
        this.kakuhankaisinichiji = kakuhankaisinichiji;
    }

    /**
     * 撹拌終了日時
     * @return the kakuhansyuryonichiji
     */
    public Timestamp getKakuhansyuryonichiji() {
        return kakuhansyuryonichiji;
    }

    /**
     * 撹拌終了日時
     * @param kakuhansyuryonichiji the kakuhansyuryonichiji to set
     */
    public void setKakuhansyuryonichiji(Timestamp kakuhansyuryonichiji) {
        this.kakuhansyuryonichiji = kakuhansyuryonichiji;
    }

    /**
     * 回転体の接触確認
     * @return the kaitentai
     */
    public Integer getKaitentai() {
        return kaitentai;
    }

    /**
     * 回転体の接触確認
     * @param kaitentai the kaitentai to set
     */
    public void setKaitentai(Integer kaitentai) {
        this.kaitentai = kaitentai;
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
     * 確認者
     * @return the kakuninsya
     */
    public String getKakuninsya() {
        return kakuninsya;
    }

    /**
     * 確認者
     * @param kakuninsya the kakuninsya to set
     */
    public void setKakuninsya(String kakuninsya) {
        this.kakuninsya = kakuninsya;
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