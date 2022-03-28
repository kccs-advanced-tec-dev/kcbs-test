/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.io.Serializable;
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
 * ｶﾞﾗｽ作製・秤量履歴検索画面のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/10/15
 */
public class GXHDO202B010Model implements Serializable {
    /** WIPﾛｯﾄNo */
    private String lotno = "";

    /** 添加材ｽﾗﾘｰ品名 */
    private String tenkazaislurryhinmei = "";

    /** 添加材ｽﾗﾘｰLotNo */
    private String tenkazaislurrylotno = "";

    /** ﾛｯﾄ区分 */
    private String lotkubun = "";

    /** 撹拌機 */
    private String kakuhanki = "";

    /** ﾀﾝｸ */
    private String tanku = "";

    /** 溶剤 */
    private String youzai = "";

    /** 洗浄時間 */
    private String senjojikan = "";

    /** 主軸 */
    private String shujiku = "";

    /** ﾎﾟﾝﾌﾟ */
    private String ponpu = "";

    /** ﾃﾞｽﾊﾟ回転数 */
    private String desupakaitensuu = "";

    /** 投入① */
    private String tounyu1 = "";

    /** 投入② */
    private String tounyu2 = "";

    /** 途中撹拌時間 */
    private String totyukakuhanjikan = "";

    /** 途中撹拌開始日時 */
    private Timestamp totyukakuhankaisinichiji = null;

    /** 途中撹拌終了日時 */
    private Timestamp totyukakuhansyuryonichiji = null;

    /** 投入③ */
    private String tonyu3 = "";

    /** 投入④ */
    private String tonyu4 = "";

    /** 投入⑤ */
    private String tonyu5 = "";

    /** 撹拌時間 */
    private String kakuhanjikan = "";

    /** 撹拌開始日時 */
    private Timestamp kakuhankaisinichiji = null;

    /** 撹拌終了日時 */
    private Timestamp kakuhansyuryonichiji = null;

    /** 回転体の接触確認 */
    private String kaitentai = "";

    /** 担当者 */
    private String tantousya = "";

    /** 確認者 */
    private String kakuninsya = "";

    /** 備考1 */
    private String bikou1 = "";

    /** 備考2 */
    private String bikou2 = "";

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
     * @return the desupakaitensuu
     */
    public String getDesupakaitensuu() {
        return desupakaitensuu;
    }

    /**
     * ﾃﾞｽﾊﾟ回転数
     * @param desupakaitensuu the desupakaitensuu to set
     */
    public void setDesupakaitensuu(String desupakaitensuu) {
        this.desupakaitensuu = desupakaitensuu;
    }

    /**
     * 投入①
     * @return the tounyu1
     */
    public String getTounyu1() {
        return tounyu1;
    }

    /**
     * 投入①
     * @param tounyu1 the tounyu1 to set
     */
    public void setTounyu1(String tounyu1) {
        this.tounyu1 = tounyu1;
    }

    /**
     * 投入②
     * @return the tounyu2
     */
    public String getTounyu2() {
        return tounyu2;
    }

    /**
     * 投入②
     * @param tounyu2 the tounyu2 to set
     */
    public void setTounyu2(String tounyu2) {
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
    public String getTonyu3() {
        return tonyu3;
    }

    /**
     * 投入③
     * @param tonyu3 the tonyu3 to set
     */
    public void setTonyu3(String tonyu3) {
        this.tonyu3 = tonyu3;
    }

    /**
     * 投入④
     * @return the tonyu4
     */
    public String getTonyu4() {
        return tonyu4;
    }

    /**
     * 投入④
     * @param tonyu4 the tonyu4 to set
     */
    public void setTonyu4(String tonyu4) {
        this.tonyu4 = tonyu4;
    }

    /**
     * 投入⑤
     * @return the tonyu5
     */
    public String getTonyu5() {
        return tonyu5;
    }

    /**
     * 投入⑤
     * @param tonyu5 the tonyu5 to set
     */
    public void setTonyu5(String tonyu5) {
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
    public String getKaitentai() {
        return kaitentai;
    }

    /**
     * 回転体の接触確認
     * @param kaitentai the kaitentai to set
     */
    public void setKaitentai(String kaitentai) {
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

}