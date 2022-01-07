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
 * 変更日	2021/10/26<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B014(ﾊﾞｲﾝﾀﾞｰ溶液作製・溶剤秤量)
 *
 * @author KCSS K.Jo
 * @since  2021/10/26
 */
@ViewScoped
@Named
public class GXHDO102B014A implements Serializable {
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
     * 秤量開始日
     */
    private FXHDD01 hyouryoukaisi_day;

    /**
     * 秤量開始時間
     */
    private FXHDD01 hyouryoukaisi_time;

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
     * 溶剤③_材料品名
     */
    private FXHDD01 youzai3_zairyouhinmei;

    /**
     * 溶剤③_調合量規格
     */
    private FXHDD01 youzai3_tyougouryoukikaku;

    /**
     * 溶剤③_部材在庫No1
     */
    private FXHDD01 youzai3_buzaizaikono1;

    /**
     * 溶剤③_調合量1
     */
    private FXHDD01 youzai3_tyougouryou1;

    /**
     * 溶剤③_部材在庫No2
     */
    private FXHDD01 youzai3_buzaizaikono2;

    /**
     * 溶剤③_調合量2
     */
    private FXHDD01 youzai3_tyougouryou2;

    /**
     * 溶剤④_材料品名
     */
    private FXHDD01 youzai4_zairyouhinmei;

    /**
     * 溶剤④_調合量規格
     */
    private FXHDD01 youzai4_tyougouryoukikaku;

    /**
     * 溶剤④_部材在庫No1
     */
    private FXHDD01 youzai4_buzaizaikono1;

    /**
     * 溶剤④_調合量1
     */
    private FXHDD01 youzai4_tyougouryou1;

    /**
     * 溶剤④_部材在庫No2
     */
    private FXHDD01 youzai4_buzaizaikono2;

    /**
     * 溶剤④_調合量2
     */
    private FXHDD01 youzai4_tyougouryou2;

    /**
     * 溶剤⑤_材料品名
     */
    private FXHDD01 youzai5_zairyouhinmei;

    /**
     * 溶剤⑤_調合量規格
     */
    private FXHDD01 youzai5_tyougouryoukikaku;

    /**
     * 溶剤⑤_部材在庫No1
     */
    private FXHDD01 youzai5_buzaizaikono1;

    /**
     * 溶剤⑤_調合量1
     */
    private FXHDD01 youzai5_tyougouryou1;

    /**
     * 溶剤⑤_部材在庫No2
     */
    private FXHDD01 youzai5_buzaizaikono2;

    /**
     * 溶剤⑤_調合量2
     */
    private FXHDD01 youzai5_tyougouryou2;

    /**
     * 溶剤⑥_材料品名
     */
    private FXHDD01 youzai6_zairyouhinmei;

    /**
     * 溶剤⑥_調合量規格
     */
    private FXHDD01 youzai6_tyougouryoukikaku;

    /**
     * 溶剤⑥_部材在庫No1
     */
    private FXHDD01 youzai6_buzaizaikono1;

    /**
     * 溶剤⑥_調合量1
     */
    private FXHDD01 youzai6_tyougouryou1;

    /**
     * 溶剤⑥_部材在庫No2
     */
    private FXHDD01 youzai6_buzaizaikono2;

    /**
     * 溶剤⑥_調合量2
     */
    private FXHDD01 youzai6_tyougouryou2;

    /**
     * 秤量終了日
     */
    private FXHDD01 hyouryousyuuryou_day;

    /**
     * 秤量終了時間
     */
    private FXHDD01 hyouryousyuuryou_time;

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
     * 溶剤③_材料品名
     * @return the youzai3_zairyouhinmei
     */
    public FXHDD01 getYouzai3_zairyouhinmei() {
        return youzai3_zairyouhinmei;
    }

    /**
     * 溶剤③_材料品名
     * @param youzai3_zairyouhinmei the youzai3_zairyouhinmei to set
     */
    public void setYouzai3_zairyouhinmei(FXHDD01 youzai3_zairyouhinmei) {
        this.youzai3_zairyouhinmei = youzai3_zairyouhinmei;
    }

    /**
     * 溶剤③_調合量規格
     * @return the youzai3_tyougouryoukikaku
     */
    public FXHDD01 getYouzai3_tyougouryoukikaku() {
        return youzai3_tyougouryoukikaku;
    }

    /**
     * 溶剤③_調合量規格
     * @param youzai3_tyougouryoukikaku the youzai3_tyougouryoukikaku to set
     */
    public void setYouzai3_tyougouryoukikaku(FXHDD01 youzai3_tyougouryoukikaku) {
        this.youzai3_tyougouryoukikaku = youzai3_tyougouryoukikaku;
    }

    /**
     * 溶剤③_部材在庫No1
     * @return the youzai3_buzaizaikono1
     */
    public FXHDD01 getYouzai3_buzaizaikono1() {
        return youzai3_buzaizaikono1;
    }

    /**
     * 溶剤③_部材在庫No1
     * @param youzai3_buzaizaikono1 the youzai3_buzaizaikono1 to set
     */
    public void setYouzai3_buzaizaikono1(FXHDD01 youzai3_buzaizaikono1) {
        this.youzai3_buzaizaikono1 = youzai3_buzaizaikono1;
    }

    /**
     * 溶剤③_調合量1
     * @return the youzai3_tyougouryou1
     */
    public FXHDD01 getYouzai3_tyougouryou1() {
        return youzai3_tyougouryou1;
    }

    /**
     * 溶剤③_調合量1
     * @param youzai3_tyougouryou1 the youzai3_tyougouryou1 to set
     */
    public void setYouzai3_tyougouryou1(FXHDD01 youzai3_tyougouryou1) {
        this.youzai3_tyougouryou1 = youzai3_tyougouryou1;
    }

    /**
     * 溶剤③_部材在庫No2
     * @return the youzai3_buzaizaikono2
     */
    public FXHDD01 getYouzai3_buzaizaikono2() {
        return youzai3_buzaizaikono2;
    }

    /**
     * 溶剤③_部材在庫No2
     * @param youzai3_buzaizaikono2 the youzai3_buzaizaikono2 to set
     */
    public void setYouzai3_buzaizaikono2(FXHDD01 youzai3_buzaizaikono2) {
        this.youzai3_buzaizaikono2 = youzai3_buzaizaikono2;
    }

    /**
     * 溶剤③_調合量2
     * @return the youzai3_tyougouryou2
     */
    public FXHDD01 getYouzai3_tyougouryou2() {
        return youzai3_tyougouryou2;
    }

    /**
     * 溶剤③_調合量2
     * @param youzai3_tyougouryou2 the youzai3_tyougouryou2 to set
     */
    public void setYouzai3_tyougouryou2(FXHDD01 youzai3_tyougouryou2) {
        this.youzai3_tyougouryou2 = youzai3_tyougouryou2;
    }

    /**
     * 溶剤④_材料品名
     * @return the youzai4_zairyouhinmei
     */
    public FXHDD01 getYouzai4_zairyouhinmei() {
        return youzai4_zairyouhinmei;
    }

    /**
     * 溶剤④_材料品名
     * @param youzai4_zairyouhinmei the youzai4_zairyouhinmei to set
     */
    public void setYouzai4_zairyouhinmei(FXHDD01 youzai4_zairyouhinmei) {
        this.youzai4_zairyouhinmei = youzai4_zairyouhinmei;
    }

    /**
     * 溶剤④_調合量規格
     * @return the youzai4_tyougouryoukikaku
     */
    public FXHDD01 getYouzai4_tyougouryoukikaku() {
        return youzai4_tyougouryoukikaku;
    }

    /**
     * 溶剤④_調合量規格
     * @param youzai4_tyougouryoukikaku the youzai4_tyougouryoukikaku to set
     */
    public void setYouzai4_tyougouryoukikaku(FXHDD01 youzai4_tyougouryoukikaku) {
        this.youzai4_tyougouryoukikaku = youzai4_tyougouryoukikaku;
    }

    /**
     * 溶剤④_部材在庫No1
     * @return the youzai4_buzaizaikono1
     */
    public FXHDD01 getYouzai4_buzaizaikono1() {
        return youzai4_buzaizaikono1;
    }

    /**
     * 溶剤④_部材在庫No1
     * @param youzai4_buzaizaikono1 the youzai4_buzaizaikono1 to set
     */
    public void setYouzai4_buzaizaikono1(FXHDD01 youzai4_buzaizaikono1) {
        this.youzai4_buzaizaikono1 = youzai4_buzaizaikono1;
    }

    /**
     * 溶剤④_調合量1
     * @return the youzai4_tyougouryou1
     */
    public FXHDD01 getYouzai4_tyougouryou1() {
        return youzai4_tyougouryou1;
    }

    /**
     * 溶剤④_調合量1
     * @param youzai4_tyougouryou1 the youzai4_tyougouryou1 to set
     */
    public void setYouzai4_tyougouryou1(FXHDD01 youzai4_tyougouryou1) {
        this.youzai4_tyougouryou1 = youzai4_tyougouryou1;
    }

    /**
     * 溶剤④_部材在庫No2
     * @return the youzai4_buzaizaikono2
     */
    public FXHDD01 getYouzai4_buzaizaikono2() {
        return youzai4_buzaizaikono2;
    }

    /**
     * 溶剤④_部材在庫No2
     * @param youzai4_buzaizaikono2 the youzai4_buzaizaikono2 to set
     */
    public void setYouzai4_buzaizaikono2(FXHDD01 youzai4_buzaizaikono2) {
        this.youzai4_buzaizaikono2 = youzai4_buzaizaikono2;
    }

    /**
     * 溶剤④_調合量2
     * @return the youzai4_tyougouryou2
     */
    public FXHDD01 getYouzai4_tyougouryou2() {
        return youzai4_tyougouryou2;
    }

    /**
     * 溶剤④_調合量2
     * @param youzai4_tyougouryou2 the youzai4_tyougouryou2 to set
     */
    public void setYouzai4_tyougouryou2(FXHDD01 youzai4_tyougouryou2) {
        this.youzai4_tyougouryou2 = youzai4_tyougouryou2;
    }

    /**
     * 溶剤⑤_材料品名
     * @return the youzai5_zairyouhinmei
     */
    public FXHDD01 getYouzai5_zairyouhinmei() {
        return youzai5_zairyouhinmei;
    }

    /**
     * 溶剤⑤_材料品名
     * @param youzai5_zairyouhinmei the youzai5_zairyouhinmei to set
     */
    public void setYouzai5_zairyouhinmei(FXHDD01 youzai5_zairyouhinmei) {
        this.youzai5_zairyouhinmei = youzai5_zairyouhinmei;
    }

    /**
     * 溶剤⑤_調合量規格
     * @return the youzai5_tyougouryoukikaku
     */
    public FXHDD01 getYouzai5_tyougouryoukikaku() {
        return youzai5_tyougouryoukikaku;
    }

    /**
     * 溶剤⑤_調合量規格
     * @param youzai5_tyougouryoukikaku the youzai5_tyougouryoukikaku to set
     */
    public void setYouzai5_tyougouryoukikaku(FXHDD01 youzai5_tyougouryoukikaku) {
        this.youzai5_tyougouryoukikaku = youzai5_tyougouryoukikaku;
    }

    /**
     * 溶剤⑤_部材在庫No1
     * @return the youzai5_buzaizaikono1
     */
    public FXHDD01 getYouzai5_buzaizaikono1() {
        return youzai5_buzaizaikono1;
    }

    /**
     * 溶剤⑤_部材在庫No1
     * @param youzai5_buzaizaikono1 the youzai5_buzaizaikono1 to set
     */
    public void setYouzai5_buzaizaikono1(FXHDD01 youzai5_buzaizaikono1) {
        this.youzai5_buzaizaikono1 = youzai5_buzaizaikono1;
    }

    /**
     * 溶剤⑤_調合量1
     * @return the youzai5_tyougouryou1
     */
    public FXHDD01 getYouzai5_tyougouryou1() {
        return youzai5_tyougouryou1;
    }

    /**
     * 溶剤⑤_調合量1
     * @param youzai5_tyougouryou1 the youzai5_tyougouryou1 to set
     */
    public void setYouzai5_tyougouryou1(FXHDD01 youzai5_tyougouryou1) {
        this.youzai5_tyougouryou1 = youzai5_tyougouryou1;
    }

    /**
     * 溶剤⑤_部材在庫No2
     * @return the youzai5_buzaizaikono2
     */
    public FXHDD01 getYouzai5_buzaizaikono2() {
        return youzai5_buzaizaikono2;
    }

    /**
     * 溶剤⑤_部材在庫No2
     * @param youzai5_buzaizaikono2 the youzai5_buzaizaikono2 to set
     */
    public void setYouzai5_buzaizaikono2(FXHDD01 youzai5_buzaizaikono2) {
        this.youzai5_buzaizaikono2 = youzai5_buzaizaikono2;
    }

    /**
     * 溶剤⑤_調合量2
     * @return the youzai5_tyougouryou2
     */
    public FXHDD01 getYouzai5_tyougouryou2() {
        return youzai5_tyougouryou2;
    }

    /**
     * 溶剤⑤_調合量2
     * @param youzai5_tyougouryou2 the youzai5_tyougouryou2 to set
     */
    public void setYouzai5_tyougouryou2(FXHDD01 youzai5_tyougouryou2) {
        this.youzai5_tyougouryou2 = youzai5_tyougouryou2;
    }

    /**
     * 溶剤⑥_材料品名
     * @return the youzai6_zairyouhinmei
     */
    public FXHDD01 getYouzai6_zairyouhinmei() {
        return youzai6_zairyouhinmei;
    }

    /**
     * 溶剤⑥_材料品名
     * @param youzai6_zairyouhinmei the youzai6_zairyouhinmei to set
     */
    public void setYouzai6_zairyouhinmei(FXHDD01 youzai6_zairyouhinmei) {
        this.youzai6_zairyouhinmei = youzai6_zairyouhinmei;
    }

    /**
     * 溶剤⑥_調合量規格
     * @return the youzai6_tyougouryoukikaku
     */
    public FXHDD01 getYouzai6_tyougouryoukikaku() {
        return youzai6_tyougouryoukikaku;
    }

    /**
     * 溶剤⑥_調合量規格
     * @param youzai6_tyougouryoukikaku the youzai6_tyougouryoukikaku to set
     */
    public void setYouzai6_tyougouryoukikaku(FXHDD01 youzai6_tyougouryoukikaku) {
        this.youzai6_tyougouryoukikaku = youzai6_tyougouryoukikaku;
    }

    /**
     * 溶剤⑥_部材在庫No1
     * @return the youzai6_buzaizaikono1
     */
    public FXHDD01 getYouzai6_buzaizaikono1() {
        return youzai6_buzaizaikono1;
    }

    /**
     * 溶剤⑥_部材在庫No1
     * @param youzai6_buzaizaikono1 the youzai6_buzaizaikono1 to set
     */
    public void setYouzai6_buzaizaikono1(FXHDD01 youzai6_buzaizaikono1) {
        this.youzai6_buzaizaikono1 = youzai6_buzaizaikono1;
    }

    /**
     * 溶剤⑥_調合量1
     * @return the youzai6_tyougouryou1
     */
    public FXHDD01 getYouzai6_tyougouryou1() {
        return youzai6_tyougouryou1;
    }

    /**
     * 溶剤⑥_調合量1
     * @param youzai6_tyougouryou1 the youzai6_tyougouryou1 to set
     */
    public void setYouzai6_tyougouryou1(FXHDD01 youzai6_tyougouryou1) {
        this.youzai6_tyougouryou1 = youzai6_tyougouryou1;
    }

    /**
     * 溶剤⑥_部材在庫No2
     * @return the youzai6_buzaizaikono2
     */
    public FXHDD01 getYouzai6_buzaizaikono2() {
        return youzai6_buzaizaikono2;
    }

    /**
     * 溶剤⑥_部材在庫No2
     * @param youzai6_buzaizaikono2 the youzai6_buzaizaikono2 to set
     */
    public void setYouzai6_buzaizaikono2(FXHDD01 youzai6_buzaizaikono2) {
        this.youzai6_buzaizaikono2 = youzai6_buzaizaikono2;
    }

    /**
     * 溶剤⑥_調合量2
     * @return the youzai6_tyougouryou2
     */
    public FXHDD01 getYouzai6_tyougouryou2() {
        return youzai6_tyougouryou2;
    }

    /**
     * 溶剤⑥_調合量2
     * @param youzai6_tyougouryou2 the youzai6_tyougouryou2 to set
     */
    public void setYouzai6_tyougouryou2(FXHDD01 youzai6_tyougouryou2) {
        this.youzai6_tyougouryou2 = youzai6_tyougouryou2;
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