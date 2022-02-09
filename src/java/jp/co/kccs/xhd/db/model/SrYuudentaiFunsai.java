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
 * 変更日	2021/12/26<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SR_YUUDENTAI_FUNSAI(誘電体ｽﾗﾘｰ作製・粉砕)のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/12/26
 */
public class SrYuudentaiFunsai {
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
    private String yuudentaihinmei;

    /**
     * 誘電体ｽﾗﾘｰLotNo
     */
    private String yuudentailotno;

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
     * 粉砕機
     */
    private String funsaiki;

    /**
     * 粉砕機洗浄①
     */
    private String funsaikisenjyou1;

    /**
     * 粉砕機洗浄②
     */
    private String funsaikisenjyou2;

    /**
     * 連続運転回数①
     */
    private String renzokuunten1;

    /**
     * 連続運転回数②
     */
    private String renzokuunten2;

    /**
     * 玉石_重量
     */
    private String gyokusekijyuryou;

    /**
     * 玉石_ﾛｯﾄ
     */
    private String gyokusekilot;

    /**
     * 玉石_ﾒﾃﾞｨｱ径
     */
    private String gyokusekimediakei;

    /**
     * 投入量
     */
    private String tounyuuryou;

    /**
     * 時間/ﾊﾟｽ回数
     */
    private String zikanpass;

    /**
     * ｽｸﾘｰﾝ
     */
    private String screen;

    /**
     * 回転数_ﾃﾞｨｽﾊﾟ
     */
    private String kaitensuudisp;

    /**
     * 回転数_主軸
     */
    private String kaitensuusyujiku;

    /**
     * ﾎﾟﾝﾌﾟ出力
     */
    private String pompsyutsuryoku;

    /**
     * 流量
     */
    private String ryuuryou;

    /**
     * ﾊﾟｽ回数
     */
    private String passkaisuu;

    /**
     * 開始日時
     */
    private Timestamp kaishinichiji;

    /**
     * 終了日時
     */
    private Timestamp syuuryounichiji;

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
     * @return the yuudentaihinmei
     */
    public String getYuudentaihinmei() {
        return yuudentaihinmei;
    }

    /**
     * 誘電体ｽﾗﾘｰ品名
     * @param yuudentaihinmei the yuudentaihinmei to set
     */
    public void setYuudentaihinmei(String yuudentaihinmei) {
        this.yuudentaihinmei = yuudentaihinmei;
    }

    /**
     * 誘電体ｽﾗﾘｰLotNo
     * @return the yuudentailotno
     */
    public String getYuudentailotno() {
        return yuudentailotno;
    }

    /**
     * 誘電体ｽﾗﾘｰLotNo
     * @param yuudentailotno the yuudentailotno to set
     */
    public void setYuudentailotno(String yuudentailotno) {
        this.yuudentailotno = yuudentailotno;
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
     * 粉砕機
     * @return the funsaiki
     */
    public String getFunsaiki() {
        return funsaiki;
    }

    /**
     * 粉砕機
     * @param funsaiki the funsaiki to set
     */
    public void setFunsaiki(String funsaiki) {
        this.funsaiki = funsaiki;
    }

    /**
     * 粉砕機洗浄①
     * @return the funsaikisenjyou1
     */
    public String getFunsaikisenjyou1() {
        return funsaikisenjyou1;
    }

    /**
     * 粉砕機洗浄①
     * @param funsaikisenjyou1 the funsaikisenjyou1 to set
     */
    public void setFunsaikisenjyou1(String funsaikisenjyou1) {
        this.funsaikisenjyou1 = funsaikisenjyou1;
    }

    /**
     * 粉砕機洗浄②
     * @return the funsaikisenjyou2
     */
    public String getFunsaikisenjyou2() {
        return funsaikisenjyou2;
    }

    /**
     * 粉砕機洗浄②
     * @param funsaikisenjyou2 the funsaikisenjyou2 to set
     */
    public void setFunsaikisenjyou2(String funsaikisenjyou2) {
        this.funsaikisenjyou2 = funsaikisenjyou2;
    }

    /**
     * 連続運転回数①
     * @return the renzokuunten1
     */
    public String getRenzokuunten1() {
        return renzokuunten1;
    }

    /**
     * 連続運転回数①
     * @param renzokuunten1 the renzokuunten1 to set
     */
    public void setRenzokuunten1(String renzokuunten1) {
        this.renzokuunten1 = renzokuunten1;
    }

    /**
     * 連続運転回数②
     * @return the renzokuunten2
     */
    public String getRenzokuunten2() {
        return renzokuunten2;
    }

    /**
     * 連続運転回数②
     * @param renzokuunten2 the renzokuunten2 to set
     */
    public void setRenzokuunten2(String renzokuunten2) {
        this.renzokuunten2 = renzokuunten2;
    }

    /**
     * 玉石_重量
     * @return the gyokusekijyuryou
     */
    public String getGyokusekijyuryou() {
        return gyokusekijyuryou;
    }

    /**
     * 玉石_重量
     * @param gyokusekijyuryou the gyokusekijyuryou to set
     */
    public void setGyokusekijyuryou(String gyokusekijyuryou) {
        this.gyokusekijyuryou = gyokusekijyuryou;
    }

    /**
     * 玉石_ﾛｯﾄ
     * @return the gyokusekilot
     */
    public String getGyokusekilot() {
        return gyokusekilot;
    }

    /**
     * 玉石_ﾛｯﾄ
     * @param gyokusekilot the gyokusekilot to set
     */
    public void setGyokusekilot(String gyokusekilot) {
        this.gyokusekilot = gyokusekilot;
    }

    /**
     * 玉石_ﾒﾃﾞｨｱ径
     * @return the gyokusekimediakei
     */
    public String getGyokusekimediakei() {
        return gyokusekimediakei;
    }

    /**
     * 玉石_ﾒﾃﾞｨｱ径
     * @param gyokusekimediakei the gyokusekimediakei to set
     */
    public void setGyokusekimediakei(String gyokusekimediakei) {
        this.gyokusekimediakei = gyokusekimediakei;
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
     * 時間/ﾊﾟｽ回数
     * @return the zikanpass
     */
    public String getZikanpass() {
        return zikanpass;
    }

    /**
     * 時間/ﾊﾟｽ回数
     * @param zikanpass the zikanpass to set
     */
    public void setZikanpass(String zikanpass) {
        this.zikanpass = zikanpass;
    }

    /**
     * ｽｸﾘｰﾝ
     * @return the screen
     */
    public String getScreen() {
        return screen;
    }

    /**
     * ｽｸﾘｰﾝ
     * @param screen the screen to set
     */
    public void setScreen(String screen) {
        this.screen = screen;
    }

    /**
     * 回転数_ﾃﾞｨｽﾊﾟ
     * @return the kaitensuudisp
     */
    public String getKaitensuudisp() {
        return kaitensuudisp;
    }

    /**
     * 回転数_ﾃﾞｨｽﾊﾟ
     * @param kaitensuudisp the kaitensuudisp to set
     */
    public void setKaitensuudisp(String kaitensuudisp) {
        this.kaitensuudisp = kaitensuudisp;
    }

    /**
     * 回転数_主軸
     * @return the kaitensuusyujiku
     */
    public String getKaitensuusyujiku() {
        return kaitensuusyujiku;
    }

    /**
     * 回転数_主軸
     * @param kaitensuusyujiku the kaitensuusyujiku to set
     */
    public void setKaitensuusyujiku(String kaitensuusyujiku) {
        this.kaitensuusyujiku = kaitensuusyujiku;
    }

    /**
     * ﾎﾟﾝﾌﾟ出力
     * @return the pompsyutsuryoku
     */
    public String getPompsyutsuryoku() {
        return pompsyutsuryoku;
    }

    /**
     * ﾎﾟﾝﾌﾟ出力
     * @param pompsyutsuryoku the pompsyutsuryoku to set
     */
    public void setPompsyutsuryoku(String pompsyutsuryoku) {
        this.pompsyutsuryoku = pompsyutsuryoku;
    }

    /**
     * 流量
     * @return the ryuuryou
     */
    public String getRyuuryou() {
        return ryuuryou;
    }

    /**
     * 流量
     * @param ryuuryou the ryuuryou to set
     */
    public void setRyuuryou(String ryuuryou) {
        this.ryuuryou = ryuuryou;
    }

    /**
     * ﾊﾟｽ回数
     * @return the passkaisuu
     */
    public String getPasskaisuu() {
        return passkaisuu;
    }

    /**
     * ﾊﾟｽ回数
     * @param passkaisuu the passkaisuu to set
     */
    public void setPasskaisuu(String passkaisuu) {
        this.passkaisuu = passkaisuu;
    }

    /**
     * 開始日時
     * @return the kaishinichiji
     */
    public Timestamp getKaishinichiji() {
        return kaishinichiji;
    }

    /**
     * 開始日時
     * @param kaishinichiji the kaishinichiji to set
     */
    public void setKaishinichiji(Timestamp kaishinichiji) {
        this.kaishinichiji = kaishinichiji;
    }

    /**
     * 終了日時
     * @return the syuuryounichiji
     */
    public Timestamp getSyuuryounichiji() {
        return syuuryounichiji;
    }

    /**
     * 終了日時
     * @param syuuryounichiji the syuuryounichiji to set
     */
    public void setSyuuryounichiji(Timestamp syuuryounichiji) {
        this.syuuryounichiji = syuuryounichiji;
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