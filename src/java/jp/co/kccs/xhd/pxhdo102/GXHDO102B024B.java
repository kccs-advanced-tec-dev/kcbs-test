/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo102;

import java.io.Serializable;
import java.util.List;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.model.GXHDO102B024Model;

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
 * GXHDO102B024(誘電体ｽﾗﾘｰ作製・粉砕)
 *
 * @author KCSS K.Jo
 * @since 2021/12/26
 */
@ViewScoped
@Named("beanGXHDO102B024B")
public class GXHDO102B024B implements Serializable {

    /**
     * 一覧表示データ
     */
    private List<GXHDO102B024Model> listdata;

    /**
     * 行削除チェックボックス
     */
    private FXHDD01 deleterow_checkbox;

    /**
     * 日付
     */
    private FXHDD01 kaishi_day;

    /**
     * 開始時刻
     */
    private FXHDD01 kaishi_time;

    /**
     * 停止予定時刻
     */
    private FXHDD01 teishiyotei_time;

    /**
     * 停止時刻
     */
    private FXHDD01 teishi_time;

    /**
     * 主軸電流
     */
    private FXHDD01 syujikudenryuu;

    /**
     * 出口温度
     */
    private FXHDD01 deguchiondo;

    /**
     * ｼｰﾙ温度
     */
    private FXHDD01 sealondo;

    /**
     * ﾎﾟﾝﾌﾟ目盛
     */
    private FXHDD01 pumpmemori;

    /**
     * ﾎﾟﾝﾌﾟ圧
     */
    private FXHDD01 pumpatsu;

    /**
     * D50規格
     */
    private FXHDD01 d50kikaku;

    /**
     * D50
     */
    private FXHDD01 d50;

    /**
     * BET規格
     */
    private FXHDD01 betkikaku;

    /**
     * BET
     */
    private FXHDD01 bet;

    /**
     * 流量規格
     */
    private FXHDD01 ryuuryoukikaku;

    /**
     * 流量
     */
    private FXHDD01 ryuuryou;

    /**
     * ﾊﾟｽ規格
     */
    private FXHDD01 passkikaku;

    /**
     * 開始ﾊﾟｽ
     */
    private FXHDD01 kaishipass;

    /**
     * 停止ﾊﾟｽ
     */
    private FXHDD01 teishipass;

    /**
     * 備考1
     */
    private FXHDD01 bikou1;

    /**
     * 備考2
     */
    private FXHDD01 bikou2;

    /**
     * 担当者
     */
    private FXHDD01 tantousya;

    /**
     * 一覧表示データ
     * @return the listdata
     */
    public List<GXHDO102B024Model> getListdata() {
        return listdata;
    }

    /**
     * 一覧表示データ
     * @param listdata the listdata to set
     */
    public void setListdata(List<GXHDO102B024Model> listdata) {
        this.listdata = listdata;
    }

    /**
     * 行削除チェックボックス
     * @return the deleterow_checkbox
     */
    public FXHDD01 getDeleterow_checkbox() {
        return deleterow_checkbox;
    }

    /**
     * 行削除チェックボックス
     * @param deleterow_checkbox the deleterow_checkbox to set
     */
    public void setDeleterow_checkbox(FXHDD01 deleterow_checkbox) {
        this.deleterow_checkbox = deleterow_checkbox;
    }

    /**
     * 日付
     * @return the kaishi_day
     */
    public FXHDD01 getKaishi_day() {
        return kaishi_day;
    }

    /**
     * 日付
     * @param kaishi_day the kaishi_day to set
     */
    public void setKaishi_day(FXHDD01 kaishi_day) {
        this.kaishi_day = kaishi_day;
    }

    /**
     * 開始時刻
     * @return the kaishi_time
     */
    public FXHDD01 getKaishi_time() {
        return kaishi_time;
    }

    /**
     * 開始時刻
     * @param kaishi_time the kaishi_time to set
     */
    public void setKaishi_time(FXHDD01 kaishi_time) {
        this.kaishi_time = kaishi_time;
    }

    /**
     * 停止予定日時
     * @return the teishiyotei_time
     */
    public FXHDD01 getTeishiyotei_time() {
        return teishiyotei_time;
    }

    /**
     * 停止予定日時
     * @param teishiyotei_time the teishiyotei_time to set
     */
    public void setTeishiyotei_time(FXHDD01 teishiyotei_time) {
        this.teishiyotei_time = teishiyotei_time;
    }

    /**
     * 停止日時
     * @return the teishi_time
     */
    public FXHDD01 getTeishi_time() {
        return teishi_time;
    }

    /**
     * 停止日時
     * @param teishi_time the teishi_time to set
     */
    public void setTeishi_time(FXHDD01 teishi_time) {
        this.teishi_time = teishi_time;
    }

    /**
     * 主軸電流
     * @return the syujikudenryuu
     */
    public FXHDD01 getSyujikudenryuu() {
        return syujikudenryuu;
    }

    /**
     * 主軸電流
     * @param syujikudenryuu the syujikudenryuu to set
     */
    public void setSyujikudenryuu(FXHDD01 syujikudenryuu) {
        this.syujikudenryuu = syujikudenryuu;
    }

    /**
     * 出口温度
     * @return the deguchiondo
     */
    public FXHDD01 getDeguchiondo() {
        return deguchiondo;
    }

    /**
     * 出口温度
     * @param deguchiondo the deguchiondo to set
     */
    public void setDeguchiondo(FXHDD01 deguchiondo) {
        this.deguchiondo = deguchiondo;
    }

    /**
     * ｼｰﾙ温度
     * @return the sealondo
     */
    public FXHDD01 getSealondo() {
        return sealondo;
    }

    /**
     * ｼｰﾙ温度
     * @param sealondo the sealondo to set
     */
    public void setSealondo(FXHDD01 sealondo) {
        this.sealondo = sealondo;
    }

    /**
     * ﾎﾟﾝﾌﾟ目盛
     * @return the pumpmemori
     */
    public FXHDD01 getPumpmemori() {
        return pumpmemori;
    }

    /**
     * ﾎﾟﾝﾌﾟ目盛
     * @param pumpmemori the pumpmemori to set
     */
    public void setPumpmemori(FXHDD01 pumpmemori) {
        this.pumpmemori = pumpmemori;
    }

    /**
     * ﾎﾟﾝﾌﾟ圧
     * @return the pumpatsu
     */
    public FXHDD01 getPumpatsu() {
        return pumpatsu;
    }

    /**
     * ﾎﾟﾝﾌﾟ圧
     * @param pumpatsu the pumpatsu to set
     */
    public void setPumpatsu(FXHDD01 pumpatsu) {
        this.pumpatsu = pumpatsu;
    }

    /**
     * D50規格
     * @return the d50kikaku
     */
    public FXHDD01 getD50kikaku() {
        return d50kikaku;
    }

    /**
     * D50規格
     * @param d50kikaku the d50kikaku to set
     */
    public void setD50kikaku(FXHDD01 d50kikaku) {
        this.d50kikaku = d50kikaku;
    }

    /**
     * D50
     * @return the d50
     */
    public FXHDD01 getD50() {
        return d50;
    }

    /**
     * D50
     * @param d50 the d50 to set
     */
    public void setD50(FXHDD01 d50) {
        this.d50 = d50;
    }

    /**
     * BET規格
     * @return the betkikaku
     */
    public FXHDD01 getBetkikaku() {
        return betkikaku;
    }

    /**
     * BET規格
     * @param betkikaku the betkikaku to set
     */
    public void setBetkikaku(FXHDD01 betkikaku) {
        this.betkikaku = betkikaku;
    }

    /**
     * BET
     * @return the bet
     */
    public FXHDD01 getBet() {
        return bet;
    }

    /**
     * BET
     * @param bet the bet to set
     */
    public void setBet(FXHDD01 bet) {
        this.bet = bet;
    }

    /**
     * 流量規格
     * @return the ryuuryoukikaku
     */
    public FXHDD01 getRyuuryoukikaku() {
        return ryuuryoukikaku;
    }

    /**
     * 流量規格
     * @param ryuuryoukikaku the ryuuryoukikaku to set
     */
    public void setRyuuryoukikaku(FXHDD01 ryuuryoukikaku) {
        this.ryuuryoukikaku = ryuuryoukikaku;
    }

    /**
     * 流量
     * @return the ryuuryou
     */
    public FXHDD01 getRyuuryou() {
        return ryuuryou;
    }

    /**
     * 流量
     * @param ryuuryou the ryuuryou to set
     */
    public void setRyuuryou(FXHDD01 ryuuryou) {
        this.ryuuryou = ryuuryou;
    }

    /**
     * ﾊﾟｽ規格
     * @return the passkikaku
     */
    public FXHDD01 getPasskikaku() {
        return passkikaku;
    }

    /**
     * ﾊﾟｽ規格
     * @param passkikaku the passkikaku to set
     */
    public void setPasskikaku(FXHDD01 passkikaku) {
        this.passkikaku = passkikaku;
    }

    /**
     * 開始ﾊﾟｽ
     * @return the kaishipass
     */
    public FXHDD01 getKaishipass() {
        return kaishipass;
    }

    /**
     * 開始ﾊﾟｽ
     * @param kaishipass the kaishipass to set
     */
    public void setKaishipass(FXHDD01 kaishipass) {
        this.kaishipass = kaishipass;
    }

    /**
     * 停止ﾊﾟｽ
     * @return the teishipass
     */
    public FXHDD01 getTeishipass() {
        return teishipass;
    }

    /**
     * 停止ﾊﾟｽ
     * @param teishipass the teishipass to set
     */
    public void setTeishipass(FXHDD01 teishipass) {
        this.teishipass = teishipass;
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
}
