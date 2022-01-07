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
 * 変更日	2021/10/27<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B016(ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾊﾞｲﾝﾀﾞｰ粉秤量)
 *
 * @author KCSS K.Jo
 * @since 2021/10/27
 */
@ViewScoped
@Named
public class GXHDO102B016A implements Serializable {
    /**
     * WIPﾛｯﾄNo
     */
    private FXHDD01 wiplotno;

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液品名
     */
    private FXHDD01 binderyouekihinmei;

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液LotNo
     */
    private FXHDD01 binderyouekilotno;

    /**
     * ﾛｯﾄ区分
     */
    private FXHDD01 lotkubun;

    /**
     * 秤量号機
     */
    private FXHDD01 goki;

    /**
     * ﾃﾞｨｽﾊﾟ号機
     */
    private FXHDD01 dispagouki;

    /**
     * 風袋重量
     */
    private FXHDD01 fuutaijyuuryou;

    /**
     * 秤量開始日
     */
    private FXHDD01 hyouryoukaisi_day;

    /**
     * 秤量開始時間
     */
    private FXHDD01 hyouryoukaisi_time;

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_材料品名
     */
    private FXHDD01 binderjyusi1_zairyouhinmei;

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_調合量規格
     */
    private FXHDD01 binderjyusi1_tyougouryoukikaku;

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_部材在庫No1
     */
    private FXHDD01 binderjyusi1_buzaizaikono1;

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_調合量1
     */
    private FXHDD01 binderjyusi1_tyougouryou1;

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_部材在庫No2
     */
    private FXHDD01 binderjyusi1_buzaizaikono2;

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_調合量2
     */
    private FXHDD01 binderjyusi1_tyougouryou2;

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_材料品名
     */
    private FXHDD01 binderjyusi2_zairyouhinmei;

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_調合量規格
     */
    private FXHDD01 binderjyusi2_tyougouryoukikaku;

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_部材在庫No1
     */
    private FXHDD01 binderjyusi2_buzaizaikono1;

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_調合量1
     */
    private FXHDD01 binderjyusi2_tyougouryou1;

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_部材在庫No2
     */
    private FXHDD01 binderjyusi2_buzaizaikono2;

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_調合量2
     */
    private FXHDD01 binderjyusi2_tyougouryou2;

    /**
     * 溶剤①_材料品名
     */
    private FXHDD01 youzai1_zairyouhinmei;

    /**
     * 溶剤①_調合量規格
     */
    private FXHDD01 youzai1_tyougouryoukikaku;

    /**
     * 溶剤①_部材在庫No1
     */
    private FXHDD01 youzai1_buzaizaikono1;

    /**
     * 溶剤①_調合量1
     */
    private FXHDD01 youzai1_tyougouryou1;

    /**
     * 溶剤①_部材在庫No2
     */
    private FXHDD01 youzai1_buzaizaikono2;

    /**
     * 溶剤①_調合量2
     */
    private FXHDD01 youzai1_tyougouryou2;

    /**
     * 溶剤②_材料品名
     */
    private FXHDD01 youzai2_zairyouhinmei;

    /**
     * 溶剤②_調合量規格
     */
    private FXHDD01 youzai2_tyougouryoukikaku;

    /**
     * 溶剤②_部材在庫No1
     */
    private FXHDD01 youzai2_buzaizaikono1;

    /**
     * 溶剤②_調合量1
     */
    private FXHDD01 youzai2_tyougouryou1;

    /**
     * 溶剤②_部材在庫No2
     */
    private FXHDD01 youzai2_buzaizaikono2;

    /**
     * 溶剤②_調合量2
     */
    private FXHDD01 youzai2_tyougouryou2;

    /**
     * 秤量終了日
     */
    private FXHDD01 hyouryousyuuryou_day;

    /**
     * 秤量終了時間
     */
    private FXHDD01 hyouryousyuuryou_time;

    /**
     * 総重量
     */
    private FXHDD01 soujyuuryou;

    /**
     * 正味重量
     */
    private FXHDD01 syoumijyuuryou;

    /**
     * 担当者
     */
    private FXHDD01 tantousya;

    /**
     * 確認者
     */
    private FXHDD01 kakuninsya;

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
    public GXHDO102B016A() {
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
     * ﾊﾞｲﾝﾀﾞｰ溶液品名
     * @return the binderyouekihinmei
     */
    public FXHDD01 getBinderyouekihinmei() {
        return binderyouekihinmei;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液品名
     * @param binderyouekihinmei the binderyouekihinmei to set
     */
    public void setBinderyouekihinmei(FXHDD01 binderyouekihinmei) {
        this.binderyouekihinmei = binderyouekihinmei;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液LotNo
     * @return the binderyouekilotno
     */
    public FXHDD01 getBinderyouekilotno() {
        return binderyouekilotno;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液LotNo
     * @param binderyouekilotno the binderyouekilotno to set
     */
    public void setBinderyouekilotno(FXHDD01 binderyouekilotno) {
        this.binderyouekilotno = binderyouekilotno;
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
     * 秤量号機
     * @return the goki
     */
    public FXHDD01 getGoki() {
        return goki;
    }

    /**
     * 秤量号機
     * @param goki the goki to set
     */
    public void setGoki(FXHDD01 goki) {
        this.goki = goki;
    }

    /**
     * ﾃﾞｨｽﾊﾟ号機
     * @return the dispagouki
     */
    public FXHDD01 getDispagouki() {
        return dispagouki;
    }

    /**
     * ﾃﾞｨｽﾊﾟ号機
     * @param dispagouki the dispagouki to set
     */
    public void setDispagouki(FXHDD01 dispagouki) {
        this.dispagouki = dispagouki;
    }

    /**
     * 風袋重量
     * @return the fuutaijyuuryou
     */
    public FXHDD01 getFuutaijyuuryou() {
        return fuutaijyuuryou;
    }

    /**
     * 風袋重量
     * @param fuutaijyuuryou the fuutaijyuuryou to set
     */
    public void setFuutaijyuuryou(FXHDD01 fuutaijyuuryou) {
        this.fuutaijyuuryou = fuutaijyuuryou;
    }

    /**
     * 秤量開始日
     * @return the hyouryoukaisi_day
     */
    public FXHDD01 getHyouryoukaisi_day() {
        return hyouryoukaisi_day;
    }

    /**
     * 秤量開始日
     * @param hyouryoukaisi_day the hyouryoukaisi_day to set
     */
    public void setHyouryoukaisi_day(FXHDD01 hyouryoukaisi_day) {
        this.hyouryoukaisi_day = hyouryoukaisi_day;
    }

    /**
     * 秤量開始時間
     * @return the hyouryoukaisi_time
     */
    public FXHDD01 getHyouryoukaisi_time() {
        return hyouryoukaisi_time;
    }

    /**
     * 秤量開始時間
     * @param hyouryoukaisi_time the hyouryoukaisi_time to set
     */
    public void setHyouryoukaisi_time(FXHDD01 hyouryoukaisi_time) {
        this.hyouryoukaisi_time = hyouryoukaisi_time;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_材料品名
     * @return the binderjyusi1_zairyouhinmei
     */
    public FXHDD01 getBinderjyusi1_zairyouhinmei() {
        return binderjyusi1_zairyouhinmei;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_材料品名
     * @param binderjyusi1_zairyouhinmei the binderjyusi1_zairyouhinmei to set
     */
    public void setBinderjyusi1_zairyouhinmei(FXHDD01 binderjyusi1_zairyouhinmei) {
        this.binderjyusi1_zairyouhinmei = binderjyusi1_zairyouhinmei;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_調合量規格
     * @return the binderjyusi1_tyougouryoukikaku
     */
    public FXHDD01 getBinderjyusi1_tyougouryoukikaku() {
        return binderjyusi1_tyougouryoukikaku;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_調合量規格
     * @param binderjyusi1_tyougouryoukikaku the binderjyusi1_tyougouryoukikaku to set
     */
    public void setBinderjyusi1_tyougouryoukikaku(FXHDD01 binderjyusi1_tyougouryoukikaku) {
        this.binderjyusi1_tyougouryoukikaku = binderjyusi1_tyougouryoukikaku;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_部材在庫No1
     * @return the binderjyusi1_buzaizaikono1
     */
    public FXHDD01 getBinderjyusi1_buzaizaikono1() {
        return binderjyusi1_buzaizaikono1;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_部材在庫No1
     * @param binderjyusi1_buzaizaikono1 the binderjyusi1_buzaizaikono1 to set
     */
    public void setBinderjyusi1_buzaizaikono1(FXHDD01 binderjyusi1_buzaizaikono1) {
        this.binderjyusi1_buzaizaikono1 = binderjyusi1_buzaizaikono1;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_調合量1
     * @return the binderjyusi1_tyougouryou1
     */
    public FXHDD01 getBinderjyusi1_tyougouryou1() {
        return binderjyusi1_tyougouryou1;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_調合量1
     * @param binderjyusi1_tyougouryou1 the binderjyusi1_tyougouryou1 to set
     */
    public void setBinderjyusi1_tyougouryou1(FXHDD01 binderjyusi1_tyougouryou1) {
        this.binderjyusi1_tyougouryou1 = binderjyusi1_tyougouryou1;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_部材在庫No2
     * @return the binderjyusi1_buzaizaikono2
     */
    public FXHDD01 getBinderjyusi1_buzaizaikono2() {
        return binderjyusi1_buzaizaikono2;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_部材在庫No2
     * @param binderjyusi1_buzaizaikono2 the binderjyusi1_buzaizaikono2 to set
     */
    public void setBinderjyusi1_buzaizaikono2(FXHDD01 binderjyusi1_buzaizaikono2) {
        this.binderjyusi1_buzaizaikono2 = binderjyusi1_buzaizaikono2;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_調合量2
     * @return the binderjyusi1_tyougouryou2
     */
    public FXHDD01 getBinderjyusi1_tyougouryou2() {
        return binderjyusi1_tyougouryou2;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂①_調合量2
     * @param binderjyusi1_tyougouryou2 the binderjyusi1_tyougouryou2 to set
     */
    public void setBinderjyusi1_tyougouryou2(FXHDD01 binderjyusi1_tyougouryou2) {
        this.binderjyusi1_tyougouryou2 = binderjyusi1_tyougouryou2;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_材料品名
     * @return the binderjyusi2_zairyouhinmei
     */
    public FXHDD01 getBinderjyusi2_zairyouhinmei() {
        return binderjyusi2_zairyouhinmei;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_材料品名
     * @param binderjyusi2_zairyouhinmei the binderjyusi2_zairyouhinmei to set
     */
    public void setBinderjyusi2_zairyouhinmei(FXHDD01 binderjyusi2_zairyouhinmei) {
        this.binderjyusi2_zairyouhinmei = binderjyusi2_zairyouhinmei;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_調合量規格
     * @return the binderjyusi2_tyougouryoukikaku
     */
    public FXHDD01 getBinderjyusi2_tyougouryoukikaku() {
        return binderjyusi2_tyougouryoukikaku;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_調合量規格
     * @param binderjyusi2_tyougouryoukikaku the binderjyusi2_tyougouryoukikaku to set
     */
    public void setBinderjyusi2_tyougouryoukikaku(FXHDD01 binderjyusi2_tyougouryoukikaku) {
        this.binderjyusi2_tyougouryoukikaku = binderjyusi2_tyougouryoukikaku;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_部材在庫No1
     * @return the binderjyusi2_buzaizaikono1
     */
    public FXHDD01 getBinderjyusi2_buzaizaikono1() {
        return binderjyusi2_buzaizaikono1;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_部材在庫No1
     * @param binderjyusi2_buzaizaikono1 the binderjyusi2_buzaizaikono1 to set
     */
    public void setBinderjyusi2_buzaizaikono1(FXHDD01 binderjyusi2_buzaizaikono1) {
        this.binderjyusi2_buzaizaikono1 = binderjyusi2_buzaizaikono1;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_調合量1
     * @return the binderjyusi2_tyougouryou1
     */
    public FXHDD01 getBinderjyusi2_tyougouryou1() {
        return binderjyusi2_tyougouryou1;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_調合量1
     * @param binderjyusi2_tyougouryou1 the binderjyusi2_tyougouryou1 to set
     */
    public void setBinderjyusi2_tyougouryou1(FXHDD01 binderjyusi2_tyougouryou1) {
        this.binderjyusi2_tyougouryou1 = binderjyusi2_tyougouryou1;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_部材在庫No2
     * @return the binderjyusi2_buzaizaikono2
     */
    public FXHDD01 getBinderjyusi2_buzaizaikono2() {
        return binderjyusi2_buzaizaikono2;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_部材在庫No2
     * @param binderjyusi2_buzaizaikono2 the binderjyusi2_buzaizaikono2 to set
     */
    public void setBinderjyusi2_buzaizaikono2(FXHDD01 binderjyusi2_buzaizaikono2) {
        this.binderjyusi2_buzaizaikono2 = binderjyusi2_buzaizaikono2;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_調合量2
     * @return the binderjyusi2_tyougouryou2
     */
    public FXHDD01 getBinderjyusi2_tyougouryou2() {
        return binderjyusi2_tyougouryou2;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ樹脂②_調合量2
     * @param binderjyusi2_tyougouryou2 the binderjyusi2_tyougouryou2 to set
     */
    public void setBinderjyusi2_tyougouryou2(FXHDD01 binderjyusi2_tyougouryou2) {
        this.binderjyusi2_tyougouryou2 = binderjyusi2_tyougouryou2;
    }

    /**
     * 溶剤①_材料品名
     * @return the youzai1_zairyouhinmei
     */
    public FXHDD01 getYouzai1_zairyouhinmei() {
        return youzai1_zairyouhinmei;
    }

    /**
     * 溶剤①_材料品名
     * @param youzai1_zairyouhinmei the youzai1_zairyouhinmei to set
     */
    public void setYouzai1_zairyouhinmei(FXHDD01 youzai1_zairyouhinmei) {
        this.youzai1_zairyouhinmei = youzai1_zairyouhinmei;
    }

    /**
     * 溶剤①_調合量規格
     * @return the youzai1_tyougouryoukikaku
     */
    public FXHDD01 getYouzai1_tyougouryoukikaku() {
        return youzai1_tyougouryoukikaku;
    }

    /**
     * 溶剤①_調合量規格
     * @param youzai1_tyougouryoukikaku the youzai1_tyougouryoukikaku to set
     */
    public void setYouzai1_tyougouryoukikaku(FXHDD01 youzai1_tyougouryoukikaku) {
        this.youzai1_tyougouryoukikaku = youzai1_tyougouryoukikaku;
    }

    /**
     * 溶剤①_部材在庫No1
     * @return the youzai1_buzaizaikono1
     */
    public FXHDD01 getYouzai1_buzaizaikono1() {
        return youzai1_buzaizaikono1;
    }

    /**
     * 溶剤①_部材在庫No1
     * @param youzai1_buzaizaikono1 the youzai1_buzaizaikono1 to set
     */
    public void setYouzai1_buzaizaikono1(FXHDD01 youzai1_buzaizaikono1) {
        this.youzai1_buzaizaikono1 = youzai1_buzaizaikono1;
    }

    /**
     * 溶剤①_調合量1
     * @return the youzai1_tyougouryou1
     */
    public FXHDD01 getYouzai1_tyougouryou1() {
        return youzai1_tyougouryou1;
    }

    /**
     * 溶剤①_調合量1
     * @param youzai1_tyougouryou1 the youzai1_tyougouryou1 to set
     */
    public void setYouzai1_tyougouryou1(FXHDD01 youzai1_tyougouryou1) {
        this.youzai1_tyougouryou1 = youzai1_tyougouryou1;
    }

    /**
     * 溶剤①_部材在庫No2
     * @return the youzai1_buzaizaikono2
     */
    public FXHDD01 getYouzai1_buzaizaikono2() {
        return youzai1_buzaizaikono2;
    }

    /**
     * 溶剤①_部材在庫No2
     * @param youzai1_buzaizaikono2 the youzai1_buzaizaikono2 to set
     */
    public void setYouzai1_buzaizaikono2(FXHDD01 youzai1_buzaizaikono2) {
        this.youzai1_buzaizaikono2 = youzai1_buzaizaikono2;
    }

    /**
     * 溶剤①_調合量2
     * @return the youzai1_tyougouryou2
     */
    public FXHDD01 getYouzai1_tyougouryou2() {
        return youzai1_tyougouryou2;
    }

    /**
     * 溶剤①_調合量2
     * @param youzai1_tyougouryou2 the youzai1_tyougouryou2 to set
     */
    public void setYouzai1_tyougouryou2(FXHDD01 youzai1_tyougouryou2) {
        this.youzai1_tyougouryou2 = youzai1_tyougouryou2;
    }

    /**
     * 溶剤②_材料品名
     * @return the youzai2_zairyouhinmei
     */
    public FXHDD01 getYouzai2_zairyouhinmei() {
        return youzai2_zairyouhinmei;
    }

    /**
     * 溶剤②_材料品名
     * @param youzai2_zairyouhinmei the youzai2_zairyouhinmei to set
     */
    public void setYouzai2_zairyouhinmei(FXHDD01 youzai2_zairyouhinmei) {
        this.youzai2_zairyouhinmei = youzai2_zairyouhinmei;
    }

    /**
     * 溶剤②_調合量規格
     * @return the youzai2_tyougouryoukikaku
     */
    public FXHDD01 getYouzai2_tyougouryoukikaku() {
        return youzai2_tyougouryoukikaku;
    }

    /**
     * 溶剤②_調合量規格
     * @param youzai2_tyougouryoukikaku the youzai2_tyougouryoukikaku to set
     */
    public void setYouzai2_tyougouryoukikaku(FXHDD01 youzai2_tyougouryoukikaku) {
        this.youzai2_tyougouryoukikaku = youzai2_tyougouryoukikaku;
    }

    /**
     * 溶剤②_部材在庫No1
     * @return the youzai2_buzaizaikono1
     */
    public FXHDD01 getYouzai2_buzaizaikono1() {
        return youzai2_buzaizaikono1;
    }

    /**
     * 溶剤②_部材在庫No1
     * @param youzai2_buzaizaikono1 the youzai2_buzaizaikono1 to set
     */
    public void setYouzai2_buzaizaikono1(FXHDD01 youzai2_buzaizaikono1) {
        this.youzai2_buzaizaikono1 = youzai2_buzaizaikono1;
    }

    /**
     * 溶剤②_調合量1
     * @return the youzai2_tyougouryou1
     */
    public FXHDD01 getYouzai2_tyougouryou1() {
        return youzai2_tyougouryou1;
    }

    /**
     * 溶剤②_調合量1
     * @param youzai2_tyougouryou1 the youzai2_tyougouryou1 to set
     */
    public void setYouzai2_tyougouryou1(FXHDD01 youzai2_tyougouryou1) {
        this.youzai2_tyougouryou1 = youzai2_tyougouryou1;
    }

    /**
     * 溶剤②_部材在庫No2
     * @return the youzai2_buzaizaikono2
     */
    public FXHDD01 getYouzai2_buzaizaikono2() {
        return youzai2_buzaizaikono2;
    }

    /**
     * 溶剤②_部材在庫No2
     * @param youzai2_buzaizaikono2 the youzai2_buzaizaikono2 to set
     */
    public void setYouzai2_buzaizaikono2(FXHDD01 youzai2_buzaizaikono2) {
        this.youzai2_buzaizaikono2 = youzai2_buzaizaikono2;
    }

    /**
     * 溶剤②_調合量2
     * @return the youzai2_tyougouryou2
     */
    public FXHDD01 getYouzai2_tyougouryou2() {
        return youzai2_tyougouryou2;
    }

    /**
     * 溶剤②_調合量2
     * @param youzai2_tyougouryou2 the youzai2_tyougouryou2 to set
     */
    public void setYouzai2_tyougouryou2(FXHDD01 youzai2_tyougouryou2) {
        this.youzai2_tyougouryou2 = youzai2_tyougouryou2;
    }

    /**
     * 秤量終了日
     * @return the hyouryousyuuryou_day
     */
    public FXHDD01 getHyouryousyuuryou_day() {
        return hyouryousyuuryou_day;
    }

    /**
     * 秤量終了日
     * @param hyouryousyuuryou_day the hyouryousyuuryou_day to set
     */
    public void setHyouryousyuuryou_day(FXHDD01 hyouryousyuuryou_day) {
        this.hyouryousyuuryou_day = hyouryousyuuryou_day;
    }

    /**
     * 秤量終了時間
     * @return the hyouryousyuuryou_time
     */
    public FXHDD01 getHyouryousyuuryou_time() {
        return hyouryousyuuryou_time;
    }

    /**
     * 秤量終了時間
     * @param hyouryousyuuryou_time the hyouryousyuuryou_time to set
     */
    public void setHyouryousyuuryou_time(FXHDD01 hyouryousyuuryou_time) {
        this.hyouryousyuuryou_time = hyouryousyuuryou_time;
    }

    /**
     * 総重量
     * @return the soujyuuryou
     */
    public FXHDD01 getSoujyuuryou() {
        return soujyuuryou;
    }

    /**
     * 総重量
     * @param soujyuuryou the soujyuuryou to set
     */
    public void setSoujyuuryou(FXHDD01 soujyuuryou) {
        this.soujyuuryou = soujyuuryou;
    }

    /**
     * 正味重量
     * @return the syoumijyuuryou
     */
    public FXHDD01 getSyoumijyuuryou() {
        return syoumijyuuryou;
    }

    /**
     * 正味重量
     * @param syoumijyuuryou the syoumijyuuryou to set
     */
    public void setSyoumijyuuryou(FXHDD01 syoumijyuuryou) {
        this.syoumijyuuryou = syoumijyuuryou;
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
     * 確認者
     * @return the kakuninsya
     */
    public FXHDD01 getKakuninsya() {
        return kakuninsya;
    }

    /**
     * 確認者
     * @param kakuninsya the kakuninsya to set
     */
    public void setKakuninsya(FXHDD01 kakuninsya) {
        this.kakuninsya = kakuninsya;
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