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
 * 変更日	2021/10/15<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS wxf<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B009(添加材ｽﾗﾘｰ作製・溶剤調合)
 *
 * @author KCSS wxf
 * @since 2021/10/15
 */
@ViewScoped
@Named
public class GXHDO102B009A implements Serializable {

    /**
     * WIPﾛｯﾄNo
     */
    private FXHDD01 wiplotno;
    /**
     * 添加材ｽﾗﾘｰ品名
     */
    private FXHDD01 tenkazaislurryhinmei;
    /**
     * 添加材ｽﾗﾘｰLotNo
     */
    private FXHDD01 tenkazaislurrylotno;
    /**
     * ﾛｯﾄ区分
     */
    private FXHDD01 lotkubun;
    /**
     * 秤量号機
     */
    private FXHDD01 hyouryougouki;
    /**
     * 秤量開始日
     */
    private FXHDD01 hyouryoukaisi_day;
    /**
     * 秤量開始時間
     */
    private FXHDD01 hyouryoukaisi_time;
    /**
     * 分散材①_材料品名
     */
    private FXHDD01 bunsanzai1_zairyouhinmei;
    /**
     * 分散材①_調合量規格
     */
    private FXHDD01 bunsanzai1_tyougouryoukikaku;
    /**
     * 分散材①_部材在庫No1
     */
    private FXHDD01 bunsanzai1_buzaizaikolotno1;
    /**
     * 分散材①_調合量1
     */
    private FXHDD01 bunsanzai1_tyougouryou1;
    /**
     * 分散材①_部材在庫No2
     */
    private FXHDD01 bunsanzai1_buzaizaikolotno2;
    /**
     * 分散材①_調合量2
     */
    private FXHDD01 bunsanzai1_tyougouryou2;
    /**
     * 分散材②_材料品名
     */
    private FXHDD01 bunsanzai2_zairyouhinmei;
    /**
     * 分散材②_調合量規格
     */
    private FXHDD01 bunsanzai2_tyougouryoukikaku;
    /**
     * 分散材②_部材在庫No1
     */
    private FXHDD01 bunsanzai2_buzaizaikolotno1;
    /**
     * 分散材②_調合量1
     */
    private FXHDD01 bunsanzai2_tyougouryou1;
    /**
     * 分散材②_部材在庫No2
     */
    private FXHDD01 bunsanzai2_buzaizaikolotno2;
    /**
     * 分散材②_調合量2
     */
    private FXHDD01 bunsanzai2_tyougouryou2;
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
    private FXHDD01 youzai1_buzaizaikolotno1;
    /**
     * 溶剤①_調合量1
     */
    private FXHDD01 youzai1_tyougouryou1;
    /**
     * 溶剤①_部材在庫No2
     */
    private FXHDD01 youzai1_buzaizaikolotno2;
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
    private FXHDD01 youzai2_buzaizaikolotno1;
    /**
     * 溶剤②_調合量1
     */
    private FXHDD01 youzai2_tyougouryou1;
    /**
     * 溶剤②_部材在庫No2
     */
    private FXHDD01 youzai2_buzaizaikolotno2;
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
    private FXHDD01 youzai3_buzaizaikolotno1;
    /**
     * 溶剤③_調合量1
     */
    private FXHDD01 youzai3_tyougouryou1;
    /**
     * 溶剤③_部材在庫No2
     */
    private FXHDD01 youzai3_buzaizaikolotno2;
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
    private FXHDD01 youzai4_buzaizaikolotno1;
    /**
     * 溶剤④_調合量1
     */
    private FXHDD01 youzai4_tyougouryou1;
    /**
     * 溶剤④_部材在庫No2
     */
    private FXHDD01 youzai4_buzaizaikolotno2;
    /**
     * 溶剤④_調合量2
     */
    private FXHDD01 youzai4_tyougouryou2;
    /**
     * ｶﾞﾗｽｽﾗﾘｰ品名
     */
    private FXHDD01 glassslurryhinmei;
    /**
     * ｶﾞﾗｽｽﾗﾘｰ調合量規格
     */
    private FXHDD01 glassslurrytyougouryoukikaku;
    /**
     * ｶﾞﾗｽｽﾗﾘｰLotNo
     */
    private FXHDD01 glassslurrylotno;
    /**
     * ｶﾞﾗｽｽﾗﾘｰ調合量
     */
    private FXHDD01 glassslurrytyougouryou;
    /**
     * 秤量終了日
     */
    private FXHDD01 hyouryousyuuryou_day;
    /**
     * 秤量終了時間
     */
    private FXHDD01 hyouryousyuuryou_time;
    /**
     * 撹拌機
     */
    private FXHDD01 kakuhanki;
    /**
     * 撹拌時間
     */
    private FXHDD01 kakuhanjikan;
    /**
     * 撹拌開始日
     */
    private FXHDD01 kakuhankaisi_day;
    /**
     * 撹拌開始時間
     */
    private FXHDD01 kakuhankaisi_time;
    /**
     * 撹拌終了日
     */
    private FXHDD01 kakuhansyuuryou_day;
    /**
     * 撹拌終了時間
     */
    private FXHDD01 kakuhansyuuryou_time;
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
    public GXHDO102B009A() {
    }

    /**
     * WIPﾛｯﾄNo
     *
     * @return the wiplotno
     */
    public FXHDD01 getWiplotno() {
        return wiplotno;
    }

    /**
     * WIPﾛｯﾄNo
     *
     * @param wiplotno the wiplotno to set
     */
    public void setWiplotno(FXHDD01 wiplotno) {
        this.wiplotno = wiplotno;
    }

    /**
     * 添加材ｽﾗﾘｰ品名
     *
     * @return the tenkazaislurryhinmei
     */
    public FXHDD01 getTenkazaislurryhinmei() {
        return tenkazaislurryhinmei;
    }

    /**
     * 添加材ｽﾗﾘｰ品名
     *
     * @param tenkazaislurryhinmei the tenkazaislurryhinmei to set
     */
    public void setTenkazaislurryhinmei(FXHDD01 tenkazaislurryhinmei) {
        this.tenkazaislurryhinmei = tenkazaislurryhinmei;
    }

    /**
     * 添加材ｽﾗﾘｰLotNo
     *
     * @return the tenkazaislurrylotno
     */
    public FXHDD01 getTenkazaislurrylotno() {
        return tenkazaislurrylotno;
    }

    /**
     * 添加材ｽﾗﾘｰLotNo
     *
     * @param tenkazaislurrylotno the tenkazaislurrylotno to set
     */
    public void setTenkazaislurrylotno(FXHDD01 tenkazaislurrylotno) {
        this.tenkazaislurrylotno = tenkazaislurrylotno;
    }

    /**
     * ﾛｯﾄ区分
     *
     * @return the lotkubun
     */
    public FXHDD01 getLotkubun() {
        return lotkubun;
    }

    /**
     * ﾛｯﾄ区分
     *
     * @param lotkubun the lotkubun to set
     */
    public void setLotkubun(FXHDD01 lotkubun) {
        this.lotkubun = lotkubun;
    }

    /**
     * 秤量号機
     *
     * @return the hyouryougouki
     */
    public FXHDD01 getHyouryougouki() {
        return hyouryougouki;
    }

    /**
     * 秤量号機
     *
     * @param hyouryougouki the hyouryougouki to set
     */
    public void setHyouryougouki(FXHDD01 hyouryougouki) {
        this.hyouryougouki = hyouryougouki;
    }

    /**
     * 秤量開始日
     *
     * @return the hyouryoukaisi_day
     */
    public FXHDD01 getHyouryoukaisi_day() {
        return hyouryoukaisi_day;
    }

    /**
     * 秤量開始日
     *
     * @param hyouryoukaisi_day the hyouryoukaisi_day to set
     */
    public void setHyouryoukaisi_day(FXHDD01 hyouryoukaisi_day) {
        this.hyouryoukaisi_day = hyouryoukaisi_day;
    }

    /**
     * 秤量開始時間
     *
     * @return the hyouryoukaisi_time
     */
    public FXHDD01 getHyouryoukaisi_time() {
        return hyouryoukaisi_time;
    }

    /**
     * 秤量開始時間
     *
     * @param hyouryoukaisi_time the hyouryoukaisi_time to set
     */
    public void setHyouryoukaisi_time(FXHDD01 hyouryoukaisi_time) {
        this.hyouryoukaisi_time = hyouryoukaisi_time;
    }

    /**
     * 分散材①_材料品名
     *
     * @return the bunsanzai1_zairyouhinmei
     */
    public FXHDD01 getBunsanzai1_zairyouhinmei() {
        return bunsanzai1_zairyouhinmei;
    }

    /**
     * 分散材①_材料品名
     *
     * @param bunsanzai1_zairyouhinmei the bunsanzai1_zairyouhinmei to set
     */
    public void setBunsanzai1_zairyouhinmei(FXHDD01 bunsanzai1_zairyouhinmei) {
        this.bunsanzai1_zairyouhinmei = bunsanzai1_zairyouhinmei;
    }

    /**
     * 分散材①_調合量規格
     *
     * @return the bunsanzai1_tyougouryoukikaku
     */
    public FXHDD01 getBunsanzai1_tyougouryoukikaku() {
        return bunsanzai1_tyougouryoukikaku;
    }

    /**
     * 分散材①_調合量規格
     *
     * @param bunsanzai1_tyougouryoukikaku the bunsanzai1_tyougouryoukikaku to
     * set
     */
    public void setBunsanzai1_tyougouryoukikaku(FXHDD01 bunsanzai1_tyougouryoukikaku) {
        this.bunsanzai1_tyougouryoukikaku = bunsanzai1_tyougouryoukikaku;
    }

    /**
     * 分散材①_部材在庫No1
     *
     * @return the bunsanzai1_buzaizaikolotno1
     */
    public FXHDD01 getBunsanzai1_buzaizaikolotno1() {
        return bunsanzai1_buzaizaikolotno1;
    }

    /**
     * 分散材①_部材在庫No1
     *
     * @param bunsanzai1_buzaizaikolotno1 the bunsanzai1_buzaizaikolotno1 to set
     */
    public void setBunsanzai1_buzaizaikolotno1(FXHDD01 bunsanzai1_buzaizaikolotno1) {
        this.bunsanzai1_buzaizaikolotno1 = bunsanzai1_buzaizaikolotno1;
    }

    /**
     * 分散材①_調合量1
     *
     * @return the bunsanzai1_tyougouryou1
     */
    public FXHDD01 getBunsanzai1_tyougouryou1() {
        return bunsanzai1_tyougouryou1;
    }

    /**
     * 分散材①_調合量1
     *
     * @param bunsanzai1_tyougouryou1 the bunsanzai1_tyougouryou1 to set
     */
    public void setBunsanzai1_tyougouryou1(FXHDD01 bunsanzai1_tyougouryou1) {
        this.bunsanzai1_tyougouryou1 = bunsanzai1_tyougouryou1;
    }

    /**
     * 分散材①_部材在庫No2
     *
     * @return the bunsanzai1_buzaizaikolotno2
     */
    public FXHDD01 getBunsanzai1_buzaizaikolotno2() {
        return bunsanzai1_buzaizaikolotno2;
    }

    /**
     * 分散材①_部材在庫No2
     *
     * @param bunsanzai1_buzaizaikolotno2 the bunsanzai1_buzaizaikolotno2 to set
     */
    public void setBunsanzai1_buzaizaikolotno2(FXHDD01 bunsanzai1_buzaizaikolotno2) {
        this.bunsanzai1_buzaizaikolotno2 = bunsanzai1_buzaizaikolotno2;
    }

    /**
     * 分散材①_調合量2
     *
     * @return the bunsanzai1_tyougouryou2
     */
    public FXHDD01 getBunsanzai1_tyougouryou2() {
        return bunsanzai1_tyougouryou2;
    }

    /**
     * 分散材①_調合量2
     *
     * @param bunsanzai1_tyougouryou2 the bunsanzai1_tyougouryou2 to set
     */
    public void setBunsanzai1_tyougouryou2(FXHDD01 bunsanzai1_tyougouryou2) {
        this.bunsanzai1_tyougouryou2 = bunsanzai1_tyougouryou2;
    }

    /**
     * 分散材②_材料品名
     *
     * @return the bunsanzai2_zairyouhinmei
     */
    public FXHDD01 getBunsanzai2_zairyouhinmei() {
        return bunsanzai2_zairyouhinmei;
    }

    /**
     * 分散材②_材料品名
     *
     * @param bunsanzai2_zairyouhinmei the bunsanzai2_zairyouhinmei to set
     */
    public void setBunsanzai2_zairyouhinmei(FXHDD01 bunsanzai2_zairyouhinmei) {
        this.bunsanzai2_zairyouhinmei = bunsanzai2_zairyouhinmei;
    }

    /**
     * 分散材②_調合量規格
     *
     * @return the bunsanzai2_tyougouryoukikaku
     */
    public FXHDD01 getBunsanzai2_tyougouryoukikaku() {
        return bunsanzai2_tyougouryoukikaku;
    }

    /**
     * 分散材②_調合量規格
     *
     * @param bunsanzai2_tyougouryoukikaku the bunsanzai2_tyougouryoukikaku to
     * set
     */
    public void setBunsanzai2_tyougouryoukikaku(FXHDD01 bunsanzai2_tyougouryoukikaku) {
        this.bunsanzai2_tyougouryoukikaku = bunsanzai2_tyougouryoukikaku;
    }

    /**
     * 分散材②_部材在庫No1
     *
     * @return the bunsanzai2_buzaizaikolotno1
     */
    public FXHDD01 getBunsanzai2_buzaizaikolotno1() {
        return bunsanzai2_buzaizaikolotno1;
    }

    /**
     * 分散材②_部材在庫No1
     *
     * @param bunsanzai2_buzaizaikolotno1 the bunsanzai2_buzaizaikolotno1 to set
     */
    public void setBunsanzai2_buzaizaikolotno1(FXHDD01 bunsanzai2_buzaizaikolotno1) {
        this.bunsanzai2_buzaizaikolotno1 = bunsanzai2_buzaizaikolotno1;
    }

    /**
     * 分散材②_調合量1
     *
     * @return the bunsanzai2_tyougouryou1
     */
    public FXHDD01 getBunsanzai2_tyougouryou1() {
        return bunsanzai2_tyougouryou1;
    }

    /**
     * 分散材②_調合量1
     *
     * @param bunsanzai2_tyougouryou1 the bunsanzai2_tyougouryou1 to set
     */
    public void setBunsanzai2_tyougouryou1(FXHDD01 bunsanzai2_tyougouryou1) {
        this.bunsanzai2_tyougouryou1 = bunsanzai2_tyougouryou1;
    }

    /**
     * 分散材②_部材在庫No2
     *
     * @return the bunsanzai2_buzaizaikolotno2
     */
    public FXHDD01 getBunsanzai2_buzaizaikolotno2() {
        return bunsanzai2_buzaizaikolotno2;
    }

    /**
     * 分散材②_部材在庫No2
     *
     * @param bunsanzai2_buzaizaikolotno2 the bunsanzai2_buzaizaikolotno2 to set
     */
    public void setBunsanzai2_buzaizaikolotno2(FXHDD01 bunsanzai2_buzaizaikolotno2) {
        this.bunsanzai2_buzaizaikolotno2 = bunsanzai2_buzaizaikolotno2;
    }

    /**
     * 分散材②_調合量2
     *
     * @return the bunsanzai2_tyougouryou2
     */
    public FXHDD01 getBunsanzai2_tyougouryou2() {
        return bunsanzai2_tyougouryou2;
    }

    /**
     * 分散材②_調合量2
     *
     * @param bunsanzai2_tyougouryou2 the bunsanzai2_tyougouryou2 to set
     */
    public void setBunsanzai2_tyougouryou2(FXHDD01 bunsanzai2_tyougouryou2) {
        this.bunsanzai2_tyougouryou2 = bunsanzai2_tyougouryou2;
    }

    /**
     * 溶剤①_材料品名
     *
     * @return the youzai1_zairyouhinmei
     */
    public FXHDD01 getYouzai1_zairyouhinmei() {
        return youzai1_zairyouhinmei;
    }

    /**
     * 溶剤①_材料品名
     *
     * @param youzai1_zairyouhinmei the youzai1_zairyouhinmei to set
     */
    public void setYouzai1_zairyouhinmei(FXHDD01 youzai1_zairyouhinmei) {
        this.youzai1_zairyouhinmei = youzai1_zairyouhinmei;
    }

    /**
     * 溶剤①_調合量規格
     *
     * @return the youzai1_tyougouryoukikaku
     */
    public FXHDD01 getYouzai1_tyougouryoukikaku() {
        return youzai1_tyougouryoukikaku;
    }

    /**
     * 溶剤①_調合量規格
     *
     * @param youzai1_tyougouryoukikaku the youzai1_tyougouryoukikaku to set
     */
    public void setYouzai1_tyougouryoukikaku(FXHDD01 youzai1_tyougouryoukikaku) {
        this.youzai1_tyougouryoukikaku = youzai1_tyougouryoukikaku;
    }

    /**
     * 溶剤①_部材在庫No1
     *
     * @return the youzai1_buzaizaikolotno1
     */
    public FXHDD01 getYouzai1_buzaizaikolotno1() {
        return youzai1_buzaizaikolotno1;
    }

    /**
     * 溶剤①_部材在庫No1
     *
     * @param youzai1_buzaizaikolotno1 the youzai1_buzaizaikolotno1 to set
     */
    public void setYouzai1_buzaizaikolotno1(FXHDD01 youzai1_buzaizaikolotno1) {
        this.youzai1_buzaizaikolotno1 = youzai1_buzaizaikolotno1;
    }

    /**
     * 溶剤①_調合量1
     *
     * @return the youzai1_tyougouryou1
     */
    public FXHDD01 getYouzai1_tyougouryou1() {
        return youzai1_tyougouryou1;
    }

    /**
     * 溶剤①_調合量1
     *
     * @param youzai1_tyougouryou1 the youzai1_tyougouryou1 to set
     */
    public void setYouzai1_tyougouryou1(FXHDD01 youzai1_tyougouryou1) {
        this.youzai1_tyougouryou1 = youzai1_tyougouryou1;
    }

    /**
     * 溶剤①_部材在庫No2
     *
     * @return the youzai1_buzaizaikolotno2
     */
    public FXHDD01 getYouzai1_buzaizaikolotno2() {
        return youzai1_buzaizaikolotno2;
    }

    /**
     * 溶剤①_部材在庫No2
     *
     * @param youzai1_buzaizaikolotno2 the youzai1_buzaizaikolotno2 to set
     */
    public void setYouzai1_buzaizaikolotno2(FXHDD01 youzai1_buzaizaikolotno2) {
        this.youzai1_buzaizaikolotno2 = youzai1_buzaizaikolotno2;
    }

    /**
     * 溶剤①_調合量2
     *
     * @return the youzai1_tyougouryou2
     */
    public FXHDD01 getYouzai1_tyougouryou2() {
        return youzai1_tyougouryou2;
    }

    /**
     * 溶剤①_調合量2
     *
     * @param youzai1_tyougouryou2 the youzai1_tyougouryou2 to set
     */
    public void setYouzai1_tyougouryou2(FXHDD01 youzai1_tyougouryou2) {
        this.youzai1_tyougouryou2 = youzai1_tyougouryou2;
    }

    /**
     * 溶剤②_材料品名
     *
     * @return the youzai2_zairyouhinmei
     */
    public FXHDD01 getYouzai2_zairyouhinmei() {
        return youzai2_zairyouhinmei;
    }

    /**
     * 溶剤②_材料品名
     *
     * @param youzai2_zairyouhinmei the youzai2_zairyouhinmei to set
     */
    public void setYouzai2_zairyouhinmei(FXHDD01 youzai2_zairyouhinmei) {
        this.youzai2_zairyouhinmei = youzai2_zairyouhinmei;
    }

    /**
     * 溶剤②_調合量規格
     *
     * @return the youzai2_tyougouryoukikaku
     */
    public FXHDD01 getYouzai2_tyougouryoukikaku() {
        return youzai2_tyougouryoukikaku;
    }

    /**
     * 溶剤②_調合量規格
     *
     * @param youzai2_tyougouryoukikaku the youzai2_tyougouryoukikaku to set
     */
    public void setYouzai2_tyougouryoukikaku(FXHDD01 youzai2_tyougouryoukikaku) {
        this.youzai2_tyougouryoukikaku = youzai2_tyougouryoukikaku;
    }

    /**
     * 溶剤②_部材在庫No1
     *
     * @return the youzai2_buzaizaikolotno1
     */
    public FXHDD01 getYouzai2_buzaizaikolotno1() {
        return youzai2_buzaizaikolotno1;
    }

    /**
     * 溶剤②_部材在庫No1
     *
     * @param youzai2_buzaizaikolotno1 the youzai2_buzaizaikolotno1 to set
     */
    public void setYouzai2_buzaizaikolotno1(FXHDD01 youzai2_buzaizaikolotno1) {
        this.youzai2_buzaizaikolotno1 = youzai2_buzaizaikolotno1;
    }

    /**
     * 溶剤②_調合量1
     *
     * @return the youzai2_tyougouryou1
     */
    public FXHDD01 getYouzai2_tyougouryou1() {
        return youzai2_tyougouryou1;
    }

    /**
     * 溶剤②_調合量1
     *
     * @param youzai2_tyougouryou1 the youzai2_tyougouryou1 to set
     */
    public void setYouzai2_tyougouryou1(FXHDD01 youzai2_tyougouryou1) {
        this.youzai2_tyougouryou1 = youzai2_tyougouryou1;
    }

    /**
     * 溶剤②_部材在庫No2
     *
     * @return the youzai2_buzaizaikolotno2
     */
    public FXHDD01 getYouzai2_buzaizaikolotno2() {
        return youzai2_buzaizaikolotno2;
    }

    /**
     * 溶剤②_部材在庫No2
     *
     * @param youzai2_buzaizaikolotno2 the youzai2_buzaizaikolotno2 to set
     */
    public void setYouzai2_buzaizaikolotno2(FXHDD01 youzai2_buzaizaikolotno2) {
        this.youzai2_buzaizaikolotno2 = youzai2_buzaizaikolotno2;
    }

    /**
     * 溶剤②_調合量2
     *
     * @return the youzai2_tyougouryou2
     */
    public FXHDD01 getYouzai2_tyougouryou2() {
        return youzai2_tyougouryou2;
    }

    /**
     * 溶剤②_調合量2
     *
     * @param youzai2_tyougouryou2 the youzai2_tyougouryou2 to set
     */
    public void setYouzai2_tyougouryou2(FXHDD01 youzai2_tyougouryou2) {
        this.youzai2_tyougouryou2 = youzai2_tyougouryou2;
    }

    /**
     * 溶剤③_材料品名
     *
     * @return the youzai3_zairyouhinmei
     */
    public FXHDD01 getYouzai3_zairyouhinmei() {
        return youzai3_zairyouhinmei;
    }

    /**
     * 溶剤③_材料品名
     *
     * @param youzai3_zairyouhinmei the youzai3_zairyouhinmei to set
     */
    public void setYouzai3_zairyouhinmei(FXHDD01 youzai3_zairyouhinmei) {
        this.youzai3_zairyouhinmei = youzai3_zairyouhinmei;
    }

    /**
     * 溶剤③_調合量規格
     *
     * @return the youzai3_tyougouryoukikaku
     */
    public FXHDD01 getYouzai3_tyougouryoukikaku() {
        return youzai3_tyougouryoukikaku;
    }

    /**
     * 溶剤③_調合量規格
     *
     * @param youzai3_tyougouryoukikaku the youzai3_tyougouryoukikaku to set
     */
    public void setYouzai3_tyougouryoukikaku(FXHDD01 youzai3_tyougouryoukikaku) {
        this.youzai3_tyougouryoukikaku = youzai3_tyougouryoukikaku;
    }

    /**
     * 溶剤③_部材在庫No1
     *
     * @return the youzai3_buzaizaikolotno1
     */
    public FXHDD01 getYouzai3_buzaizaikolotno1() {
        return youzai3_buzaizaikolotno1;
    }

    /**
     * 溶剤③_部材在庫No1
     *
     * @param youzai3_buzaizaikolotno1 the youzai3_buzaizaikolotno1 to set
     */
    public void setYouzai3_buzaizaikolotno1(FXHDD01 youzai3_buzaizaikolotno1) {
        this.youzai3_buzaizaikolotno1 = youzai3_buzaizaikolotno1;
    }

    /**
     * 溶剤③_調合量1
     *
     * @return the youzai3_tyougouryou1
     */
    public FXHDD01 getYouzai3_tyougouryou1() {
        return youzai3_tyougouryou1;
    }

    /**
     * 溶剤③_調合量1
     *
     * @param youzai3_tyougouryou1 the youzai3_tyougouryou1 to set
     */
    public void setYouzai3_tyougouryou1(FXHDD01 youzai3_tyougouryou1) {
        this.youzai3_tyougouryou1 = youzai3_tyougouryou1;
    }

    /**
     * 溶剤③_部材在庫No2
     *
     * @return the youzai3_buzaizaikolotno2
     */
    public FXHDD01 getYouzai3_buzaizaikolotno2() {
        return youzai3_buzaizaikolotno2;
    }

    /**
     * 溶剤③_部材在庫No2
     *
     * @param youzai3_buzaizaikolotno2 the youzai3_buzaizaikolotno2 to set
     */
    public void setYouzai3_buzaizaikolotno2(FXHDD01 youzai3_buzaizaikolotno2) {
        this.youzai3_buzaizaikolotno2 = youzai3_buzaizaikolotno2;
    }

    /**
     * 溶剤③_調合量2
     *
     * @return the youzai3_tyougouryou2
     */
    public FXHDD01 getYouzai3_tyougouryou2() {
        return youzai3_tyougouryou2;
    }

    /**
     * 溶剤③_調合量2
     *
     * @param youzai3_tyougouryou2 the youzai3_tyougouryou2 to set
     */
    public void setYouzai3_tyougouryou2(FXHDD01 youzai3_tyougouryou2) {
        this.youzai3_tyougouryou2 = youzai3_tyougouryou2;
    }

    /**
     * 溶剤④_材料品名
     *
     * @return the youzai4_zairyouhinmei
     */
    public FXHDD01 getYouzai4_zairyouhinmei() {
        return youzai4_zairyouhinmei;
    }

    /**
     * 溶剤④_材料品名
     *
     * @param youzai4_zairyouhinmei the youzai4_zairyouhinmei to set
     */
    public void setYouzai4_zairyouhinmei(FXHDD01 youzai4_zairyouhinmei) {
        this.youzai4_zairyouhinmei = youzai4_zairyouhinmei;
    }

    /**
     * 溶剤④_調合量規格
     *
     * @return the youzai4_tyougouryoukikaku
     */
    public FXHDD01 getYouzai4_tyougouryoukikaku() {
        return youzai4_tyougouryoukikaku;
    }

    /**
     * 溶剤④_調合量規格
     *
     * @param youzai4_tyougouryoukikaku the youzai4_tyougouryoukikaku to set
     */
    public void setYouzai4_tyougouryoukikaku(FXHDD01 youzai4_tyougouryoukikaku) {
        this.youzai4_tyougouryoukikaku = youzai4_tyougouryoukikaku;
    }

    /**
     * 溶剤④_部材在庫No1
     *
     * @return the youzai4_buzaizaikolotno1
     */
    public FXHDD01 getYouzai4_buzaizaikolotno1() {
        return youzai4_buzaizaikolotno1;
    }

    /**
     * 溶剤④_部材在庫No1
     *
     * @param youzai4_buzaizaikolotno1 the youzai4_buzaizaikolotno1 to set
     */
    public void setYouzai4_buzaizaikolotno1(FXHDD01 youzai4_buzaizaikolotno1) {
        this.youzai4_buzaizaikolotno1 = youzai4_buzaizaikolotno1;
    }

    /**
     * 溶剤④_調合量1
     *
     * @return the youzai4_tyougouryou1
     */
    public FXHDD01 getYouzai4_tyougouryou1() {
        return youzai4_tyougouryou1;
    }

    /**
     * 溶剤④_調合量1
     *
     * @param youzai4_tyougouryou1 the youzai4_tyougouryou1 to set
     */
    public void setYouzai4_tyougouryou1(FXHDD01 youzai4_tyougouryou1) {
        this.youzai4_tyougouryou1 = youzai4_tyougouryou1;
    }

    /**
     * 溶剤④_部材在庫No2
     *
     * @return the youzai4_buzaizaikolotno2
     */
    public FXHDD01 getYouzai4_buzaizaikolotno2() {
        return youzai4_buzaizaikolotno2;
    }

    /**
     * 溶剤④_部材在庫No2
     *
     * @param youzai4_buzaizaikolotno2 the youzai4_buzaizaikolotno2 to set
     */
    public void setYouzai4_buzaizaikolotno2(FXHDD01 youzai4_buzaizaikolotno2) {
        this.youzai4_buzaizaikolotno2 = youzai4_buzaizaikolotno2;
    }

    /**
     * 溶剤④_調合量2
     *
     * @return the youzai4_tyougouryou2
     */
    public FXHDD01 getYouzai4_tyougouryou2() {
        return youzai4_tyougouryou2;
    }

    /**
     * 溶剤④_調合量2
     *
     * @param youzai4_tyougouryou2 the youzai4_tyougouryou2 to set
     */
    public void setYouzai4_tyougouryou2(FXHDD01 youzai4_tyougouryou2) {
        this.youzai4_tyougouryou2 = youzai4_tyougouryou2;
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ品名
     *
     * @return the glassslurryhinmei
     */
    public FXHDD01 getGlassslurryhinmei() {
        return glassslurryhinmei;
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ品名
     *
     * @param glassslurryhinmei the glassslurryhinmei to set
     */
    public void setGlassslurryhinmei(FXHDD01 glassslurryhinmei) {
        this.glassslurryhinmei = glassslurryhinmei;
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ調合量規格
     *
     * @return the glassslurrytyougouryoukikaku
     */
    public FXHDD01 getGlassslurrytyougouryoukikaku() {
        return glassslurrytyougouryoukikaku;
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ調合量規格
     *
     * @param glassslurrytyougouryoukikaku the glassslurrytyougouryoukikaku to
     * set
     */
    public void setGlassslurrytyougouryoukikaku(FXHDD01 glassslurrytyougouryoukikaku) {
        this.glassslurrytyougouryoukikaku = glassslurrytyougouryoukikaku;
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰLotNo
     *
     * @return the glassslurrylotno
     */
    public FXHDD01 getGlassslurrylotno() {
        return glassslurrylotno;
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰLotNo
     *
     * @param glassslurrylotno the glassslurrylotno to set
     */
    public void setGlassslurrylotno(FXHDD01 glassslurrylotno) {
        this.glassslurrylotno = glassslurrylotno;
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ調合量
     *
     * @return the glassslurrytyougouryou
     */
    public FXHDD01 getGlassslurrytyougouryou() {
        return glassslurrytyougouryou;
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ調合量
     *
     * @param glassslurrytyougouryou the glassslurrytyougouryou to set
     */
    public void setGlassslurrytyougouryou(FXHDD01 glassslurrytyougouryou) {
        this.glassslurrytyougouryou = glassslurrytyougouryou;
    }

    /**
     * 秤量終了日
     *
     * @return the hyouryousyuuryou_day
     */
    public FXHDD01 getHyouryousyuuryou_day() {
        return hyouryousyuuryou_day;
    }

    /**
     * 秤量終了日
     *
     * @param hyouryousyuuryou_day the hyouryousyuuryou_day to set
     */
    public void setHyouryousyuuryou_day(FXHDD01 hyouryousyuuryou_day) {
        this.hyouryousyuuryou_day = hyouryousyuuryou_day;
    }

    /**
     * 秤量終了時間
     *
     * @return the hyouryousyuuryou_time
     */
    public FXHDD01 getHyouryousyuuryou_time() {
        return hyouryousyuuryou_time;
    }

    /**
     * 秤量終了時間
     *
     * @param hyouryousyuuryou_time the hyouryousyuuryou_time to set
     */
    public void setHyouryousyuuryou_time(FXHDD01 hyouryousyuuryou_time) {
        this.hyouryousyuuryou_time = hyouryousyuuryou_time;
    }

    /**
     * 撹拌機
     *
     * @return the kakuhanki
     */
    public FXHDD01 getKakuhanki() {
        return kakuhanki;
    }

    /**
     * 撹拌機
     *
     * @param kakuhanki the kakuhanki to set
     */
    public void setKakuhanki(FXHDD01 kakuhanki) {
        this.kakuhanki = kakuhanki;
    }

    /**
     * 撹拌時間
     *
     * @return the kakuhanjikan
     */
    public FXHDD01 getKakuhanjikan() {
        return kakuhanjikan;
    }

    /**
     * 撹拌時間
     *
     * @param kakuhanjikan the kakuhanjikan to set
     */
    public void setKakuhanjikan(FXHDD01 kakuhanjikan) {
        this.kakuhanjikan = kakuhanjikan;
    }

    /**
     * 撹拌開始日
     *
     * @return the kakuhankaisi_day
     */
    public FXHDD01 getKakuhankaisi_day() {
        return kakuhankaisi_day;
    }

    /**
     * 撹拌開始日
     *
     * @param kakuhankaisi_day the kakuhankaisi_day to set
     */
    public void setKakuhankaisi_day(FXHDD01 kakuhankaisi_day) {
        this.kakuhankaisi_day = kakuhankaisi_day;
    }

    /**
     * 撹拌開始時間
     *
     * @return the kakuhankaisi_time
     */
    public FXHDD01 getKakuhankaisi_time() {
        return kakuhankaisi_time;
    }

    /**
     * 撹拌開始時間
     *
     * @param kakuhankaisi_time the kakuhankaisi_time to set
     */
    public void setKakuhankaisi_time(FXHDD01 kakuhankaisi_time) {
        this.kakuhankaisi_time = kakuhankaisi_time;
    }

    /**
     * 撹拌終了日
     *
     * @return the kakuhansyuuryou_day
     */
    public FXHDD01 getKakuhansyuuryou_day() {
        return kakuhansyuuryou_day;
    }

    /**
     * 撹拌終了日
     *
     * @param kakuhansyuuryou_day the kakuhansyuuryou_day to set
     */
    public void setKakuhansyuuryou_day(FXHDD01 kakuhansyuuryou_day) {
        this.kakuhansyuuryou_day = kakuhansyuuryou_day;
    }

    /**
     * 撹拌終了時間
     *
     * @return the kakuhansyuuryou_time
     */
    public FXHDD01 getKakuhansyuuryou_time() {
        return kakuhansyuuryou_time;
    }

    /**
     * 撹拌終了時間
     *
     * @param kakuhansyuuryou_time the kakuhansyuuryou_time to set
     */
    public void setKakuhansyuuryou_time(FXHDD01 kakuhansyuuryou_time) {
        this.kakuhansyuuryou_time = kakuhansyuuryou_time;
    }

    /**
     * 担当者
     *
     * @return the tantousya
     */
    public FXHDD01 getTantousya() {
        return tantousya;
    }

    /**
     * 担当者
     *
     * @param tantousya the tantousya to set
     */
    public void setTantousya(FXHDD01 tantousya) {
        this.tantousya = tantousya;
    }

    /**
     * 確認者
     *
     * @return the kakuninsya
     */
    public FXHDD01 getKakuninsya() {
        return kakuninsya;
    }

    /**
     * 確認者
     *
     * @param kakuninsya the kakuninsya to set
     */
    public void setKakuninsya(FXHDD01 kakuninsya) {
        this.kakuninsya = kakuninsya;
    }

    /**
     * 備考1
     *
     * @return the bikou1
     */
    public FXHDD01 getBikou1() {
        return bikou1;
    }

    /**
     * 備考1
     *
     * @param bikou1 the bikou1 to set
     */
    public void setBikou1(FXHDD01 bikou1) {
        this.bikou1 = bikou1;
    }

    /**
     * 備考2
     *
     * @return the bikou2
     */
    public FXHDD01 getBikou2() {
        return bikou2;
    }

    /**
     * 備考2
     *
     * @param bikou2 the bikou2 to set
     */
    public void setBikou2(FXHDD01 bikou2) {
        this.bikou2 = bikou2;
    }
}
