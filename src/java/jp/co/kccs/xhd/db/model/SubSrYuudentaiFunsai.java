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
 * 変更日	2021/12/26<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SUB_SR_YUUDENTAI_FUNSAI(誘電体ｽﾗﾘｰ作製・粉砕_ｻﾌﾞ画面)のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/12/26
 */
public class SubSrYuudentaiFunsai {
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
     * 回数
     */
    private Integer kaisuu;

    /**
     * 開始日時
     */
    private Timestamp kaishinichiji;

    /**
     * 停止予定日時
     */
    private Timestamp teishiyoteinichiji;

    /**
     * 停止日時
     */
    private Timestamp teishinichiji;

    /**
     * 主軸電流
     */
    private Integer syujikudenryuu;

    /**
     * 出口温度
     */
    private Integer deguchiondo;

    /**
     * ｼｰﾙ温度
     */
    private Integer sealondo;

    /**
     * ﾎﾟﾝﾌﾟ目盛
     */
    private Integer pumpmemori;

    /**
     * ﾎﾟﾝﾌﾟ圧
     */
    private Integer pumpatsu;

    /**
     * D50規格
     */
    private String d50kikaku;

    /**
     * D50
     */
    private BigDecimal d50;

    /**
     * BET規格
     */
    private String betkikaku;

    /**
     * BET
     */
    private BigDecimal bet;

    /**
     * 流量規格
     */
    private String ryuuryoukikaku;

    /**
     * 流量 
     */
    private BigDecimal ryuuryou;

    /**
     * 開始ﾊﾟｽ
     */
    private Integer kaishipass;

    /**
     * 停止ﾊﾟｽ
     */
    private Integer teishipass;

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
     * 回数
     * @return the kaisuu
     */
    public Integer getKaisuu() {
        return kaisuu;
    }

    /**
     * 回数
     * @param kaisuu the kaisuu to set
     */
    public void setKaisuu(Integer kaisuu) {
        this.kaisuu = kaisuu;
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
     * 停止予定日時
     * @return the teishiyoteinichiji
     */
    public Timestamp getTeishiyoteinichiji() {
        return teishiyoteinichiji;
    }

    /**
     * 停止予定日時
     * @param teishiyoteinichiji the teishiyoteinichiji to set
     */
    public void setTeishiyoteinichiji(Timestamp teishiyoteinichiji) {
        this.teishiyoteinichiji = teishiyoteinichiji;
    }

    /**
     * 停止日時
     * @return the teishinichiji
     */
    public Timestamp getTeishinichiji() {
        return teishinichiji;
    }

    /**
     * 停止日時
     * @param teishinichiji the teishinichiji to set
     */
    public void setTeishinichiji(Timestamp teishinichiji) {
        this.teishinichiji = teishinichiji;
    }

    /**
     * 主軸電流
     * @return the syujikudenryuu
     */
    public Integer getSyujikudenryuu() {
        return syujikudenryuu;
    }

    /**
     * 主軸電流
     * @param syujikudenryuu the syujikudenryuu to set
     */
    public void setSyujikudenryuu(Integer syujikudenryuu) {
        this.syujikudenryuu = syujikudenryuu;
    }

    /**
     * 出口温度
     * @return the deguchiondo
     */
    public Integer getDeguchiondo() {
        return deguchiondo;
    }

    /**
     * 出口温度
     * @param deguchiondo the deguchiondo to set
     */
    public void setDeguchiondo(Integer deguchiondo) {
        this.deguchiondo = deguchiondo;
    }

    /**
     * ｼｰﾙ温度
     * @return the sealondo
     */
    public Integer getSealondo() {
        return sealondo;
    }

    /**
     * ｼｰﾙ温度
     * @param sealondo the sealondo to set
     */
    public void setSealondo(Integer sealondo) {
        this.sealondo = sealondo;
    }

    /**
     * ﾎﾟﾝﾌﾟ目盛
     * @return the pumpmemori
     */
    public Integer getPumpmemori() {
        return pumpmemori;
    }

    /**
     * ﾎﾟﾝﾌﾟ目盛
     * @param pumpmemori the pumpmemori to set
     */
    public void setPumpmemori(Integer pumpmemori) {
        this.pumpmemori = pumpmemori;
    }

    /**
     * ﾎﾟﾝﾌﾟ圧
     * @return the pumpatsu
     */
    public Integer getPumpatsu() {
        return pumpatsu;
    }

    /**
     * ﾎﾟﾝﾌﾟ圧
     * @param pumpatsu the pumpatsu to set
     */
    public void setPumpatsu(Integer pumpatsu) {
        this.pumpatsu = pumpatsu;
    }

    /**
     * D50規格
     * @return the d50kikaku
     */
    public String getD50kikaku() {
        return d50kikaku;
    }

    /**
     * D50規格
     * @param d50kikaku the d50kikaku to set
     */
    public void setD50kikaku(String d50kikaku) {
        this.d50kikaku = d50kikaku;
    }

    /**
     * D50
     * @return the d50
     */
    public BigDecimal getD50() {
        return d50;
    }

    /**
     * D50
     * @param d50 the d50 to set
     */
    public void setD50(BigDecimal d50) {
        this.d50 = d50;
    }

    /**
     * BET規格
     * @return the betkikaku
     */
    public String getBetkikaku() {
        return betkikaku;
    }

    /**
     * BET規格
     * @param betkikaku the betkikaku to set
     */
    public void setBetkikaku(String betkikaku) {
        this.betkikaku = betkikaku;
    }

    /**
     * BET
     * @return the bet
     */
    public BigDecimal getBet() {
        return bet;
    }

    /**
     * BET
     * @param bet the bet to set
     */
    public void setBet(BigDecimal bet) {
        this.bet = bet;
    }

    /**
     * 流量規格
     * @return the ryuuryoukikaku
     */
    public String getRyuuryoukikaku() {
        return ryuuryoukikaku;
    }

    /**
     * 流量規格
     * @param ryuuryoukikaku the ryuuryoukikaku to set
     */
    public void setRyuuryoukikaku(String ryuuryoukikaku) {
        this.ryuuryoukikaku = ryuuryoukikaku;
    }

    /**
     * 流量
     * @return the ryuuryou
     */
    public BigDecimal getRyuuryou() {
        return ryuuryou;
    }

    /**
     * 流量
     * @param ryuuryou the ryuuryou to set
     */
    public void setRyuuryou(BigDecimal ryuuryou) {
        this.ryuuryou = ryuuryou;
    }

    /**
     * 開始ﾊﾟｽ
     * @return the kaishipass
     */
    public Integer getKaishipass() {
        return kaishipass;
    }

    /**
     * 開始ﾊﾟｽ
     * @param kaishipass the kaishipass to set
     */
    public void setKaishipass(Integer kaishipass) {
        this.kaishipass = kaishipass;
    }

    /**
     * 停止ﾊﾟｽ
     * @return the teishipass
     */
    public Integer getTeishipass() {
        return teishipass;
    }

    /**
     * 停止ﾊﾟｽ
     * @param teishipass the teishipass to set
     */
    public void setTeishipass(Integer teishipass) {
        this.teishipass = teishipass;
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