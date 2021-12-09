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
 * 変更日	2021/11/09<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B020(誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量)
 *
 * @author KCSS K.Jo
 * @since  2021/11/09
 */
@ViewScoped
@Named
public class GXHDO102B020A implements Serializable {
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
     * 秤量号機
     */
    private FXHDD01 goki;

    /**
     * 溶剤秤量開始日
     */
    private FXHDD01 youzaihyouryoukaisi_day;

    /**
     * 溶剤秤量開始時間
     */
    private FXHDD01 youzaihyouryoukaisi_time;

    /**
     * 分散材①_材料品名
     */
    private FXHDD01 zunsanzai1_zairyouhinmei;

    /**
     * 分散材①_調合量規格
     */
    private FXHDD01 zunsanzai1_tyougouryoukikaku;

    /**
     * 分散材①_部材在庫No1
     */
    private FXHDD01 zunsanzai1_buzaizaikolotno1;

    /**
     * 分散材①_調合量1
     */
    private FXHDD01 zunsanzai1_tyougouryou1;

    /**
     * 分散材①_部材在庫No2
     */
    private FXHDD01 zunsanzai1_buzaizaikolotno2;

    /**
     * 分散材①_調合量2
     */
    private FXHDD01 zunsanzai1_tyougouryou2;

    /**
     * 分散材②_材料品名
     */
    private FXHDD01 zunsanzai2_zairyouhinmei;

    /**
     * 分散材②_調合量規格
     */
    private FXHDD01 zunsanzai2_tyougouryoukikaku;

    /**
     * 分散材②_部材在庫No1
     */
    private FXHDD01 zunsanzai2_buzaizaikolotno1;

    /**
     * 分散材②_調合量1
     */
    private FXHDD01 zunsanzai2_tyougouryou1;

    /**
     * 分散材②_部材在庫No2
     */
    private FXHDD01 zunsanzai2_buzaizaikolotno2;

    /**
     * 分散材②_調合量2
     */
    private FXHDD01 zunsanzai2_tyougouryou2;

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
    private FXHDD01 youzai5_buzaizaikolotno1;

    /**
     * 溶剤⑤_調合量1
     */
    private FXHDD01 youzai5_tyougouryou1;

    /**
     * 溶剤⑤_部材在庫No2
     */
    private FXHDD01 youzai5_buzaizaikolotno2;

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
    private FXHDD01 youzai6_buzaizaikolotno1;

    /**
     * 溶剤⑥_調合量1
     */
    private FXHDD01 youzai6_tyougouryou1;

    /**
     * 溶剤⑥_部材在庫No2
     */
    private FXHDD01 youzai6_buzaizaikolotno2;

    /**
     * 溶剤⑥_調合量2
     */
    private FXHDD01 youzai6_tyougouryou2;

    /**
     * 溶剤⑦_材料品名
     */
    private FXHDD01 youzai7_zairyouhinmei;

    /**
     * 溶剤⑦_調合量規格
     */
    private FXHDD01 youzai7_tyougouryoukikaku;

    /**
     * 溶剤⑦_部材在庫No1
     */
    private FXHDD01 youzai7_buzaizaikolotno1;

    /**
     * 溶剤⑦_調合量1
     */
    private FXHDD01 youzai7_tyougouryou1;

    /**
     * 溶剤⑦_部材在庫No2
     */
    private FXHDD01 youzai7_buzaizaikolotno2;

    /**
     * 溶剤⑦_調合量2
     */
    private FXHDD01 youzai7_tyougouryou2;

    /**
     * 溶剤⑧_材料品名
     */
    private FXHDD01 youzai8_zairyouhinmei;

    /**
     * 溶剤⑧_調合量規格
     */
    private FXHDD01 youzai8_tyougouryoukikaku;

    /**
     * 溶剤⑧_部材在庫No1
     */
    private FXHDD01 youzai8_buzaizaikolotno1;

    /**
     * 溶剤⑧_調合量1
     */
    private FXHDD01 youzai8_tyougouryou1;

    /**
     * 溶剤⑧_部材在庫No2
     */
    private FXHDD01 youzai8_buzaizaikolotno2;

    /**
     * 溶剤⑧_調合量2
     */
    private FXHDD01 youzai8_tyougouryou2;

    /**
     * 溶剤⑨_材料品名
     */
    private FXHDD01 youzai9_zairyouhinmei;

    /**
     * 溶剤⑨_調合量規格
     */
    private FXHDD01 youzai9_tyougouryoukikaku;

    /**
     * 溶剤⑨_部材在庫No1
     */
    private FXHDD01 youzai9_buzaizaikolotno1;

    /**
     * 溶剤⑨_調合量1
     */
    private FXHDD01 youzai9_tyougouryou1;

    /**
     * 溶剤⑨_部材在庫No2
     */
    private FXHDD01 youzai9_buzaizaikolotno2;

    /**
     * 溶剤⑨_調合量2
     */
    private FXHDD01 youzai9_tyougouryou2;

    /**
     * 溶剤秤量終了日
     */
    private FXHDD01 youzaihyouryousyuuryou_day;

    /**
     * 溶剤秤量終了時間
     */
    private FXHDD01 youzaihyouryousyuuryou_time;

    /**
     * 撹拌機
     */
    private FXHDD01 kakuhanki;

    /**
     * 回転数
     */
    private FXHDD01 kaitensuu;

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
     * 添加材ｽﾗﾘｰ秤量開始日
     */
    private FXHDD01 tenkazaislurryhrkaisi_day;

    /**
     * 添加材ｽﾗﾘｰ秤量開始時間
     */
    private FXHDD01 tenkazaislurryhrkaisi_time;

    /**
     * 添加材ｽﾗﾘｰ_材料品名
     */
    private FXHDD01 tenkazaislurry_zairyouhinmei;

    /**
     * 添加材ｽﾗﾘｰ_WIPﾛｯﾄNo
     */
    private FXHDD01 tenkazaislurry_WIPlotno;

    /**
     * 添加材ｽﾗﾘｰ_調合量規格
     */
    private FXHDD01 tenkazaislurry_tgrkikaku;

    /**
     * 添加材ｽﾗﾘｰ_風袋重量1
     */
    private FXHDD01 tenkazaislurry_ftaijyuuryou1;

    /**
     * 添加材ｽﾗﾘｰ_調合量1
     */
    private FXHDD01 tenkazaislurry_tyougouryou1;

    /**
     * 添加材ｽﾗﾘｰ_風袋重量2
     */
    private FXHDD01 tenkazaislurry_ftaijyuuryou2;

    /**
     * 添加材ｽﾗﾘｰ_調合量2
     */
    private FXHDD01 tenkazaislurry_tyougouryou2;

    /**
     * 添加材ｽﾗﾘｰ秤量終了日
     */
    private FXHDD01 tenkazaislurryhrsyuryo_day;

    /**
     * 添加材ｽﾗﾘｰ秤量終了時間
     */
    private FXHDD01 tenkazaislurryhrsyuryo_time;

    /**
     * 固形分測定担当者
     */
    private FXHDD01 kokeibunsokuteitantousya;

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
     * 溶剤秤量開始日
     * @return the youzaihyouryoukaisi_day
     */
    public FXHDD01 getYouzaihyouryoukaisi_day() {
        return youzaihyouryoukaisi_day;
    }

    /**
     * 溶剤秤量開始日
     * @param youzaihyouryoukaisi_day the youzaihyouryoukaisi_day to set
     */
    public void setYouzaihyouryoukaisi_day(FXHDD01 youzaihyouryoukaisi_day) {
        this.youzaihyouryoukaisi_day = youzaihyouryoukaisi_day;
    }

    /**
     * 溶剤秤量開始時間
     * @return the youzaihyouryoukaisi_time
     */
    public FXHDD01 getYouzaihyouryoukaisi_time() {
        return youzaihyouryoukaisi_time;
    }

    /**
     * 溶剤秤量開始時間
     * @param youzaihyouryoukaisi_time the youzaihyouryoukaisi_time to set
     */
    public void setYouzaihyouryoukaisi_time(FXHDD01 youzaihyouryoukaisi_time) {
        this.youzaihyouryoukaisi_time = youzaihyouryoukaisi_time;
    }

    /**
     * 分散材①_材料品名
     * @return the zunsanzai1_zairyouhinmei
     */
    public FXHDD01 getZunsanzai1_zairyouhinmei() {
        return zunsanzai1_zairyouhinmei;
    }

    /**
     * 分散材①_材料品名
     * @param zunsanzai1_zairyouhinmei the zunsanzai1_zairyouhinmei to set
     */
    public void setZunsanzai1_zairyouhinmei(FXHDD01 zunsanzai1_zairyouhinmei) {
        this.zunsanzai1_zairyouhinmei = zunsanzai1_zairyouhinmei;
    }

    /**
     * 分散材①_調合量規格
     * @return the zunsanzai1_tyougouryoukikaku
     */
    public FXHDD01 getZunsanzai1_tyougouryoukikaku() {
        return zunsanzai1_tyougouryoukikaku;
    }

    /**
     * 分散材①_調合量規格
     * @param zunsanzai1_tyougouryoukikaku the zunsanzai1_tyougouryoukikaku to set
     */
    public void setZunsanzai1_tyougouryoukikaku(FXHDD01 zunsanzai1_tyougouryoukikaku) {
        this.zunsanzai1_tyougouryoukikaku = zunsanzai1_tyougouryoukikaku;
    }

    /**
     * 分散材①_部材在庫No1
     * @return the zunsanzai1_buzaizaikolotno1
     */
    public FXHDD01 getZunsanzai1_buzaizaikolotno1() {
        return zunsanzai1_buzaizaikolotno1;
    }

    /**
     * 分散材①_部材在庫No1
     * @param zunsanzai1_buzaizaikolotno1 the zunsanzai1_buzaizaikolotno1 to set
     */
    public void setZunsanzai1_buzaizaikolotno1(FXHDD01 zunsanzai1_buzaizaikolotno1) {
        this.zunsanzai1_buzaizaikolotno1 = zunsanzai1_buzaizaikolotno1;
    }

    /**
     * 分散材①_調合量1
     * @return the zunsanzai1_tyougouryou1
     */
    public FXHDD01 getZunsanzai1_tyougouryou1() {
        return zunsanzai1_tyougouryou1;
    }

    /**
     * 分散材①_調合量1
     * @param zunsanzai1_tyougouryou1 the zunsanzai1_tyougouryou1 to set
     */
    public void setZunsanzai1_tyougouryou1(FXHDD01 zunsanzai1_tyougouryou1) {
        this.zunsanzai1_tyougouryou1 = zunsanzai1_tyougouryou1;
    }

    /**
     * 分散材①_部材在庫No2
     * @return the zunsanzai1_buzaizaikolotno2
     */
    public FXHDD01 getZunsanzai1_buzaizaikolotno2() {
        return zunsanzai1_buzaizaikolotno2;
    }

    /**
     * 分散材①_部材在庫No2
     * @param zunsanzai1_buzaizaikolotno2 the zunsanzai1_buzaizaikolotno2 to set
     */
    public void setZunsanzai1_buzaizaikolotno2(FXHDD01 zunsanzai1_buzaizaikolotno2) {
        this.zunsanzai1_buzaizaikolotno2 = zunsanzai1_buzaizaikolotno2;
    }

    /**
     * 分散材①_調合量2
     * @return the zunsanzai1_tyougouryou2
     */
    public FXHDD01 getZunsanzai1_tyougouryou2() {
        return zunsanzai1_tyougouryou2;
    }

    /**
     * 分散材①_調合量2
     * @param zunsanzai1_tyougouryou2 the zunsanzai1_tyougouryou2 to set
     */
    public void setZunsanzai1_tyougouryou2(FXHDD01 zunsanzai1_tyougouryou2) {
        this.zunsanzai1_tyougouryou2 = zunsanzai1_tyougouryou2;
    }

    /**
     * 分散材②_材料品名
     * @return the zunsanzai2_zairyouhinmei
     */
    public FXHDD01 getZunsanzai2_zairyouhinmei() {
        return zunsanzai2_zairyouhinmei;
    }

    /**
     * 分散材②_材料品名
     * @param zunsanzai2_zairyouhinmei the zunsanzai2_zairyouhinmei to set
     */
    public void setZunsanzai2_zairyouhinmei(FXHDD01 zunsanzai2_zairyouhinmei) {
        this.zunsanzai2_zairyouhinmei = zunsanzai2_zairyouhinmei;
    }

    /**
     * 分散材②_調合量規格
     * @return the zunsanzai2_tyougouryoukikaku
     */
    public FXHDD01 getZunsanzai2_tyougouryoukikaku() {
        return zunsanzai2_tyougouryoukikaku;
    }

    /**
     * 分散材②_調合量規格
     * @param zunsanzai2_tyougouryoukikaku the zunsanzai2_tyougouryoukikaku to set
     */
    public void setZunsanzai2_tyougouryoukikaku(FXHDD01 zunsanzai2_tyougouryoukikaku) {
        this.zunsanzai2_tyougouryoukikaku = zunsanzai2_tyougouryoukikaku;
    }

    /**
     * 分散材②_部材在庫No1
     * @return the zunsanzai2_buzaizaikolotno1
     */
    public FXHDD01 getZunsanzai2_buzaizaikolotno1() {
        return zunsanzai2_buzaizaikolotno1;
    }

    /**
     * 分散材②_部材在庫No1
     * @param zunsanzai2_buzaizaikolotno1 the zunsanzai2_buzaizaikolotno1 to set
     */
    public void setZunsanzai2_buzaizaikolotno1(FXHDD01 zunsanzai2_buzaizaikolotno1) {
        this.zunsanzai2_buzaizaikolotno1 = zunsanzai2_buzaizaikolotno1;
    }

    /**
     * 分散材②_調合量1
     * @return the zunsanzai2_tyougouryou1
     */
    public FXHDD01 getZunsanzai2_tyougouryou1() {
        return zunsanzai2_tyougouryou1;
    }

    /**
     * 分散材②_調合量1
     * @param zunsanzai2_tyougouryou1 the zunsanzai2_tyougouryou1 to set
     */
    public void setZunsanzai2_tyougouryou1(FXHDD01 zunsanzai2_tyougouryou1) {
        this.zunsanzai2_tyougouryou1 = zunsanzai2_tyougouryou1;
    }

    /**
     * 分散材②_部材在庫No2
     * @return the zunsanzai2_buzaizaikolotno2
     */
    public FXHDD01 getZunsanzai2_buzaizaikolotno2() {
        return zunsanzai2_buzaizaikolotno2;
    }

    /**
     * 分散材②_部材在庫No2
     * @param zunsanzai2_buzaizaikolotno2 the zunsanzai2_buzaizaikolotno2 to set
     */
    public void setZunsanzai2_buzaizaikolotno2(FXHDD01 zunsanzai2_buzaizaikolotno2) {
        this.zunsanzai2_buzaizaikolotno2 = zunsanzai2_buzaizaikolotno2;
    }

    /**
     * 分散材②_調合量2
     * @return the zunsanzai2_tyougouryou2
     */
    public FXHDD01 getZunsanzai2_tyougouryou2() {
        return zunsanzai2_tyougouryou2;
    }

    /**
     * 分散材②_調合量2
     * @param zunsanzai2_tyougouryou2 the zunsanzai2_tyougouryou2 to set
     */
    public void setZunsanzai2_tyougouryou2(FXHDD01 zunsanzai2_tyougouryou2) {
        this.zunsanzai2_tyougouryou2 = zunsanzai2_tyougouryou2;
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
     * @return the youzai1_buzaizaikolotno1
     */
    public FXHDD01 getYouzai1_buzaizaikolotno1() {
        return youzai1_buzaizaikolotno1;
    }

    /**
     * 溶剤①_部材在庫No1
     * @param youzai1_buzaizaikolotno1 the youzai1_buzaizaikolotno1 to set
     */
    public void setYouzai1_buzaizaikolotno1(FXHDD01 youzai1_buzaizaikolotno1) {
        this.youzai1_buzaizaikolotno1 = youzai1_buzaizaikolotno1;
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
     * @return the youzai1_buzaizaikolotno2
     */
    public FXHDD01 getYouzai1_buzaizaikolotno2() {
        return youzai1_buzaizaikolotno2;
    }

    /**
     * 溶剤①_部材在庫No2
     * @param youzai1_buzaizaikolotno2 the youzai1_buzaizaikolotno2 to set
     */
    public void setYouzai1_buzaizaikolotno2(FXHDD01 youzai1_buzaizaikolotno2) {
        this.youzai1_buzaizaikolotno2 = youzai1_buzaizaikolotno2;
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
     * @return the youzai2_buzaizaikolotno1
     */
    public FXHDD01 getYouzai2_buzaizaikolotno1() {
        return youzai2_buzaizaikolotno1;
    }

    /**
     * 溶剤②_部材在庫No1
     * @param youzai2_buzaizaikolotno1 the youzai2_buzaizaikolotno1 to set
     */
    public void setYouzai2_buzaizaikolotno1(FXHDD01 youzai2_buzaizaikolotno1) {
        this.youzai2_buzaizaikolotno1 = youzai2_buzaizaikolotno1;
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
     * @return the youzai2_buzaizaikolotno2
     */
    public FXHDD01 getYouzai2_buzaizaikolotno2() {
        return youzai2_buzaizaikolotno2;
    }

    /**
     * 溶剤②_部材在庫No2
     * @param youzai2_buzaizaikolotno2 the youzai2_buzaizaikolotno2 to set
     */
    public void setYouzai2_buzaizaikolotno2(FXHDD01 youzai2_buzaizaikolotno2) {
        this.youzai2_buzaizaikolotno2 = youzai2_buzaizaikolotno2;
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
     * @return the youzai3_buzaizaikolotno1
     */
    public FXHDD01 getYouzai3_buzaizaikolotno1() {
        return youzai3_buzaizaikolotno1;
    }

    /**
     * 溶剤③_部材在庫No1
     * @param youzai3_buzaizaikolotno1 the youzai3_buzaizaikolotno1 to set
     */
    public void setYouzai3_buzaizaikolotno1(FXHDD01 youzai3_buzaizaikolotno1) {
        this.youzai3_buzaizaikolotno1 = youzai3_buzaizaikolotno1;
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
     * @return the youzai3_buzaizaikolotno2
     */
    public FXHDD01 getYouzai3_buzaizaikolotno2() {
        return youzai3_buzaizaikolotno2;
    }

    /**
     * 溶剤③_部材在庫No2
     * @param youzai3_buzaizaikolotno2 the youzai3_buzaizaikolotno2 to set
     */
    public void setYouzai3_buzaizaikolotno2(FXHDD01 youzai3_buzaizaikolotno2) {
        this.youzai3_buzaizaikolotno2 = youzai3_buzaizaikolotno2;
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
     * @return the youzai4_buzaizaikolotno1
     */
    public FXHDD01 getYouzai4_buzaizaikolotno1() {
        return youzai4_buzaizaikolotno1;
    }

    /**
     * 溶剤④_部材在庫No1
     * @param youzai4_buzaizaikolotno1 the youzai4_buzaizaikolotno1 to set
     */
    public void setYouzai4_buzaizaikolotno1(FXHDD01 youzai4_buzaizaikolotno1) {
        this.youzai4_buzaizaikolotno1 = youzai4_buzaizaikolotno1;
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
     * @return the youzai4_buzaizaikolotno2
     */
    public FXHDD01 getYouzai4_buzaizaikolotno2() {
        return youzai4_buzaizaikolotno2;
    }

    /**
     * 溶剤④_部材在庫No2
     * @param youzai4_buzaizaikolotno2 the youzai4_buzaizaikolotno2 to set
     */
    public void setYouzai4_buzaizaikolotno2(FXHDD01 youzai4_buzaizaikolotno2) {
        this.youzai4_buzaizaikolotno2 = youzai4_buzaizaikolotno2;
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
     * @return the youzai5_buzaizaikolotno1
     */
    public FXHDD01 getYouzai5_buzaizaikolotno1() {
        return youzai5_buzaizaikolotno1;
    }

    /**
     * 溶剤⑤_部材在庫No1
     * @param youzai5_buzaizaikolotno1 the youzai5_buzaizaikolotno1 to set
     */
    public void setYouzai5_buzaizaikolotno1(FXHDD01 youzai5_buzaizaikolotno1) {
        this.youzai5_buzaizaikolotno1 = youzai5_buzaizaikolotno1;
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
     * @return the youzai5_buzaizaikolotno2
     */
    public FXHDD01 getYouzai5_buzaizaikolotno2() {
        return youzai5_buzaizaikolotno2;
    }

    /**
     * 溶剤⑤_部材在庫No2
     * @param youzai5_buzaizaikolotno2 the youzai5_buzaizaikolotno2 to set
     */
    public void setYouzai5_buzaizaikolotno2(FXHDD01 youzai5_buzaizaikolotno2) {
        this.youzai5_buzaizaikolotno2 = youzai5_buzaizaikolotno2;
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
     * @return the youzai6_buzaizaikolotno1
     */
    public FXHDD01 getYouzai6_buzaizaikolotno1() {
        return youzai6_buzaizaikolotno1;
    }

    /**
     * 溶剤⑥_部材在庫No1
     * @param youzai6_buzaizaikolotno1 the youzai6_buzaizaikolotno1 to set
     */
    public void setYouzai6_buzaizaikolotno1(FXHDD01 youzai6_buzaizaikolotno1) {
        this.youzai6_buzaizaikolotno1 = youzai6_buzaizaikolotno1;
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
     * @return the youzai6_buzaizaikolotno2
     */
    public FXHDD01 getYouzai6_buzaizaikolotno2() {
        return youzai6_buzaizaikolotno2;
    }

    /**
     * 溶剤⑥_部材在庫No2
     * @param youzai6_buzaizaikolotno2 the youzai6_buzaizaikolotno2 to set
     */
    public void setYouzai6_buzaizaikolotno2(FXHDD01 youzai6_buzaizaikolotno2) {
        this.youzai6_buzaizaikolotno2 = youzai6_buzaizaikolotno2;
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
     * 溶剤⑦_材料品名
     * @return the youzai7_zairyouhinmei
     */
    public FXHDD01 getYouzai7_zairyouhinmei() {
        return youzai7_zairyouhinmei;
    }

    /**
     * 溶剤⑦_材料品名
     * @param youzai7_zairyouhinmei the youzai7_zairyouhinmei to set
     */
    public void setYouzai7_zairyouhinmei(FXHDD01 youzai7_zairyouhinmei) {
        this.youzai7_zairyouhinmei = youzai7_zairyouhinmei;
    }

    /**
     * 溶剤⑦_調合量規格
     * @return the youzai7_tyougouryoukikaku
     */
    public FXHDD01 getYouzai7_tyougouryoukikaku() {
        return youzai7_tyougouryoukikaku;
    }

    /**
     * 溶剤⑦_調合量規格
     * @param youzai7_tyougouryoukikaku the youzai7_tyougouryoukikaku to set
     */
    public void setYouzai7_tyougouryoukikaku(FXHDD01 youzai7_tyougouryoukikaku) {
        this.youzai7_tyougouryoukikaku = youzai7_tyougouryoukikaku;
    }

    /**
     * 溶剤⑦_部材在庫No1
     * @return the youzai7_buzaizaikolotno1
     */
    public FXHDD01 getYouzai7_buzaizaikolotno1() {
        return youzai7_buzaizaikolotno1;
    }

    /**
     * 溶剤⑦_部材在庫No1
     * @param youzai7_buzaizaikolotno1 the youzai7_buzaizaikolotno1 to set
     */
    public void setYouzai7_buzaizaikolotno1(FXHDD01 youzai7_buzaizaikolotno1) {
        this.youzai7_buzaizaikolotno1 = youzai7_buzaizaikolotno1;
    }

    /**
     * 溶剤⑦_調合量1
     * @return the youzai7_tyougouryou1
     */
    public FXHDD01 getYouzai7_tyougouryou1() {
        return youzai7_tyougouryou1;
    }

    /**
     * 溶剤⑦_調合量1
     * @param youzai7_tyougouryou1 the youzai7_tyougouryou1 to set
     */
    public void setYouzai7_tyougouryou1(FXHDD01 youzai7_tyougouryou1) {
        this.youzai7_tyougouryou1 = youzai7_tyougouryou1;
    }

    /**
     * 溶剤⑦_部材在庫No2
     * @return the youzai7_buzaizaikolotno2
     */
    public FXHDD01 getYouzai7_buzaizaikolotno2() {
        return youzai7_buzaizaikolotno2;
    }

    /**
     * 溶剤⑦_部材在庫No2
     * @param youzai7_buzaizaikolotno2 the youzai7_buzaizaikolotno2 to set
     */
    public void setYouzai7_buzaizaikolotno2(FXHDD01 youzai7_buzaizaikolotno2) {
        this.youzai7_buzaizaikolotno2 = youzai7_buzaizaikolotno2;
    }

    /**
     * 溶剤⑦_調合量2
     * @return the youzai7_tyougouryou2
     */
    public FXHDD01 getYouzai7_tyougouryou2() {
        return youzai7_tyougouryou2;
    }

    /**
     * 溶剤⑦_調合量2
     * @param youzai7_tyougouryou2 the youzai7_tyougouryou2 to set
     */
    public void setYouzai7_tyougouryou2(FXHDD01 youzai7_tyougouryou2) {
        this.youzai7_tyougouryou2 = youzai7_tyougouryou2;
    }

    /**
     * 溶剤⑧_材料品名
     * @return the youzai8_zairyouhinmei
     */
    public FXHDD01 getYouzai8_zairyouhinmei() {
        return youzai8_zairyouhinmei;
    }

    /**
     * 溶剤⑧_材料品名
     * @param youzai8_zairyouhinmei the youzai8_zairyouhinmei to set
     */
    public void setYouzai8_zairyouhinmei(FXHDD01 youzai8_zairyouhinmei) {
        this.youzai8_zairyouhinmei = youzai8_zairyouhinmei;
    }

    /**
     * 溶剤⑧_調合量規格
     * @return the youzai8_tyougouryoukikaku
     */
    public FXHDD01 getYouzai8_tyougouryoukikaku() {
        return youzai8_tyougouryoukikaku;
    }

    /**
     * 溶剤⑧_調合量規格
     * @param youzai8_tyougouryoukikaku the youzai8_tyougouryoukikaku to set
     */
    public void setYouzai8_tyougouryoukikaku(FXHDD01 youzai8_tyougouryoukikaku) {
        this.youzai8_tyougouryoukikaku = youzai8_tyougouryoukikaku;
    }

    /**
     * 溶剤⑧_部材在庫No1
     * @return the youzai8_buzaizaikolotno1
     */
    public FXHDD01 getYouzai8_buzaizaikolotno1() {
        return youzai8_buzaizaikolotno1;
    }

    /**
     * 溶剤⑧_部材在庫No1
     * @param youzai8_buzaizaikolotno1 the youzai8_buzaizaikolotno1 to set
     */
    public void setYouzai8_buzaizaikolotno1(FXHDD01 youzai8_buzaizaikolotno1) {
        this.youzai8_buzaizaikolotno1 = youzai8_buzaizaikolotno1;
    }

    /**
     * 溶剤⑧_調合量1
     * @return the youzai8_tyougouryou1
     */
    public FXHDD01 getYouzai8_tyougouryou1() {
        return youzai8_tyougouryou1;
    }

    /**
     * 溶剤⑧_調合量1
     * @param youzai8_tyougouryou1 the youzai8_tyougouryou1 to set
     */
    public void setYouzai8_tyougouryou1(FXHDD01 youzai8_tyougouryou1) {
        this.youzai8_tyougouryou1 = youzai8_tyougouryou1;
    }

    /**
     * 溶剤⑧_部材在庫No2
     * @return the youzai8_buzaizaikolotno2
     */
    public FXHDD01 getYouzai8_buzaizaikolotno2() {
        return youzai8_buzaizaikolotno2;
    }

    /**
     * 溶剤⑧_部材在庫No2
     * @param youzai8_buzaizaikolotno2 the youzai8_buzaizaikolotno2 to set
     */
    public void setYouzai8_buzaizaikolotno2(FXHDD01 youzai8_buzaizaikolotno2) {
        this.youzai8_buzaizaikolotno2 = youzai8_buzaizaikolotno2;
    }

    /**
     * 溶剤⑧_調合量2
     * @return the youzai8_tyougouryou2
     */
    public FXHDD01 getYouzai8_tyougouryou2() {
        return youzai8_tyougouryou2;
    }

    /**
     * 溶剤⑧_調合量2
     * @param youzai8_tyougouryou2 the youzai8_tyougouryou2 to set
     */
    public void setYouzai8_tyougouryou2(FXHDD01 youzai8_tyougouryou2) {
        this.youzai8_tyougouryou2 = youzai8_tyougouryou2;
    }

    /**
     * 溶剤⑨_材料品名
     * @return the youzai9_zairyouhinmei
     */
    public FXHDD01 getYouzai9_zairyouhinmei() {
        return youzai9_zairyouhinmei;
    }

    /**
     * 溶剤⑨_材料品名
     * @param youzai9_zairyouhinmei the youzai9_zairyouhinmei to set
     */
    public void setYouzai9_zairyouhinmei(FXHDD01 youzai9_zairyouhinmei) {
        this.youzai9_zairyouhinmei = youzai9_zairyouhinmei;
    }

    /**
     * 溶剤⑨_調合量規格
     * @return the youzai9_tyougouryoukikaku
     */
    public FXHDD01 getYouzai9_tyougouryoukikaku() {
        return youzai9_tyougouryoukikaku;
    }

    /**
     * 溶剤⑨_調合量規格
     * @param youzai9_tyougouryoukikaku the youzai9_tyougouryoukikaku to set
     */
    public void setYouzai9_tyougouryoukikaku(FXHDD01 youzai9_tyougouryoukikaku) {
        this.youzai9_tyougouryoukikaku = youzai9_tyougouryoukikaku;
    }

    /**
     * 溶剤⑨_部材在庫No1
     * @return the youzai9_buzaizaikolotno1
     */
    public FXHDD01 getYouzai9_buzaizaikolotno1() {
        return youzai9_buzaizaikolotno1;
    }

    /**
     * 溶剤⑨_部材在庫No1
     * @param youzai9_buzaizaikolotno1 the youzai9_buzaizaikolotno1 to set
     */
    public void setYouzai9_buzaizaikolotno1(FXHDD01 youzai9_buzaizaikolotno1) {
        this.youzai9_buzaizaikolotno1 = youzai9_buzaizaikolotno1;
    }

    /**
     * 溶剤⑨_調合量1
     * @return the youzai9_tyougouryou1
     */
    public FXHDD01 getYouzai9_tyougouryou1() {
        return youzai9_tyougouryou1;
    }

    /**
     * 溶剤⑨_調合量1
     * @param youzai9_tyougouryou1 the youzai9_tyougouryou1 to set
     */
    public void setYouzai9_tyougouryou1(FXHDD01 youzai9_tyougouryou1) {
        this.youzai9_tyougouryou1 = youzai9_tyougouryou1;
    }

    /**
     * 溶剤⑨_部材在庫No2
     * @return the youzai9_buzaizaikolotno2
     */
    public FXHDD01 getYouzai9_buzaizaikolotno2() {
        return youzai9_buzaizaikolotno2;
    }

    /**
     * 溶剤⑨_部材在庫No2
     * @param youzai9_buzaizaikolotno2 the youzai9_buzaizaikolotno2 to set
     */
    public void setYouzai9_buzaizaikolotno2(FXHDD01 youzai9_buzaizaikolotno2) {
        this.youzai9_buzaizaikolotno2 = youzai9_buzaizaikolotno2;
    }

    /**
     * 溶剤⑨_調合量2
     * @return the youzai9_tyougouryou2
     */
    public FXHDD01 getYouzai9_tyougouryou2() {
        return youzai9_tyougouryou2;
    }

    /**
     * 溶剤⑨_調合量2
     * @param youzai9_tyougouryou2 the youzai9_tyougouryou2 to set
     */
    public void setYouzai9_tyougouryou2(FXHDD01 youzai9_tyougouryou2) {
        this.youzai9_tyougouryou2 = youzai9_tyougouryou2;
    }

    /**
     * 溶剤秤量終了日
     * @return the youzaihyouryousyuuryou_day
     */
    public FXHDD01 getYouzaihyouryousyuuryou_day() {
        return youzaihyouryousyuuryou_day;
    }

    /**
     * 溶剤秤量終了日
     * @param youzaihyouryousyuuryou_day the youzaihyouryousyuuryou_day to set
     */
    public void setYouzaihyouryousyuuryou_day(FXHDD01 youzaihyouryousyuuryou_day) {
        this.youzaihyouryousyuuryou_day = youzaihyouryousyuuryou_day;
    }

    /**
     * 溶剤秤量終了時間
     * @return the youzaihyouryousyuuryou_time
     */
    public FXHDD01 getYouzaihyouryousyuuryou_time() {
        return youzaihyouryousyuuryou_time;
    }

    /**
     * 溶剤秤量終了時間
     * @param youzaihyouryousyuuryou_time the youzaihyouryousyuuryou_time to set
     */
    public void setYouzaihyouryousyuuryou_time(FXHDD01 youzaihyouryousyuuryou_time) {
        this.youzaihyouryousyuuryou_time = youzaihyouryousyuuryou_time;
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
     * 撹拌時間
     * @return the kakuhanjikan
     */
    public FXHDD01 getKakuhanjikan() {
        return kakuhanjikan;
    }

    /**
     * 撹拌時間
     * @param kakuhanjikan the kakuhanjikan to set
     */
    public void setKakuhanjikan(FXHDD01 kakuhanjikan) {
        this.kakuhanjikan = kakuhanjikan;
    }

    /**
     * 撹拌開始日
     * @return the kakuhankaisi_day
     */
    public FXHDD01 getKakuhankaisi_day() {
        return kakuhankaisi_day;
    }

    /**
     * 撹拌開始日
     * @param kakuhankaisi_day the kakuhankaisi_day to set
     */
    public void setKakuhankaisi_day(FXHDD01 kakuhankaisi_day) {
        this.kakuhankaisi_day = kakuhankaisi_day;
    }

    /**
     * 撹拌開始時間
     * @return the kakuhankaisi_time
     */
    public FXHDD01 getKakuhankaisi_time() {
        return kakuhankaisi_time;
    }

    /**
     * 撹拌開始時間
     * @param kakuhankaisi_time the kakuhankaisi_time to set
     */
    public void setKakuhankaisi_time(FXHDD01 kakuhankaisi_time) {
        this.kakuhankaisi_time = kakuhankaisi_time;
    }

    /**
     * 撹拌終了日
     * @return the kakuhansyuuryou_day
     */
    public FXHDD01 getKakuhansyuuryou_day() {
        return kakuhansyuuryou_day;
    }

    /**
     * 撹拌終了日
     * @param kakuhansyuuryou_day the kakuhansyuuryou_day to set
     */
    public void setKakuhansyuuryou_day(FXHDD01 kakuhansyuuryou_day) {
        this.kakuhansyuuryou_day = kakuhansyuuryou_day;
    }

    /**
     * 撹拌終了時間
     * @return the kakuhansyuuryou_time
     */
    public FXHDD01 getKakuhansyuuryou_time() {
        return kakuhansyuuryou_time;
    }

    /**
     * 撹拌終了時間
     * @param kakuhansyuuryou_time the kakuhansyuuryou_time to set
     */
    public void setKakuhansyuuryou_time(FXHDD01 kakuhansyuuryou_time) {
        this.kakuhansyuuryou_time = kakuhansyuuryou_time;
    }

    /**
     * 添加材ｽﾗﾘｰ秤量開始日
     * @return the tenkazaislurryhrkaisi_day
     */
    public FXHDD01 getTenkazaislurryhrkaisi_day() {
        return tenkazaislurryhrkaisi_day;
    }

    /**
     * 添加材ｽﾗﾘｰ秤量開始日
     * @param tenkazaislurryhrkaisi_day the tenkazaislurryhrkaisi_day to set
     */
    public void setTenkazaislurryhrkaisi_day(FXHDD01 tenkazaislurryhrkaisi_day) {
        this.tenkazaislurryhrkaisi_day = tenkazaislurryhrkaisi_day;
    }

    /**
     * 添加材ｽﾗﾘｰ秤量開始時間
     * @return the tenkazaislurryhrkaisi_time
     */
    public FXHDD01 getTenkazaislurryhrkaisi_time() {
        return tenkazaislurryhrkaisi_time;
    }

    /**
     * 添加材ｽﾗﾘｰ秤量開始時間
     * @param tenkazaislurryhrkaisi_time the tenkazaislurryhrkaisi_time to set
     */
    public void setTenkazaislurryhrkaisi_time(FXHDD01 tenkazaislurryhrkaisi_time) {
        this.tenkazaislurryhrkaisi_time = tenkazaislurryhrkaisi_time;
    }

    /**
     * 添加材ｽﾗﾘｰ_材料品名
     * @return the tenkazaislurry_zairyouhinmei
     */
    public FXHDD01 getTenkazaislurry_zairyouhinmei() {
        return tenkazaislurry_zairyouhinmei;
    }

    /**
     * 添加材ｽﾗﾘｰ_材料品名
     * @param tenkazaislurry_zairyouhinmei the tenkazaislurry_zairyouhinmei to set
     */
    public void setTenkazaislurry_zairyouhinmei(FXHDD01 tenkazaislurry_zairyouhinmei) {
        this.tenkazaislurry_zairyouhinmei = tenkazaislurry_zairyouhinmei;
    }

    /**
     * 添加材ｽﾗﾘｰ_WIPﾛｯﾄNo
     * @return the tenkazaislurry_WIPlotno
     */
    public FXHDD01 getTenkazaislurry_WIPlotno() {
        return tenkazaislurry_WIPlotno;
    }

    /**
     * 添加材ｽﾗﾘｰ_WIPﾛｯﾄNo
     * @param tenkazaislurry_WIPlotno the tenkazaislurry_WIPlotno to set
     */
    public void setTenkazaislurry_WIPlotno(FXHDD01 tenkazaislurry_WIPlotno) {
        this.tenkazaislurry_WIPlotno = tenkazaislurry_WIPlotno;
    }

    /**
     * 添加材ｽﾗﾘｰ_調合量規格
     * @return the tenkazaislurry_tgrkikaku
     */
    public FXHDD01 getTenkazaislurry_tgrkikaku() {
        return tenkazaislurry_tgrkikaku;
    }

    /**
     * 添加材ｽﾗﾘｰ_調合量規格
     * @param tenkazaislurry_tgrkikaku the tenkazaislurry_tgrkikaku to set
     */
    public void setTenkazaislurry_tgrkikaku(FXHDD01 tenkazaislurry_tgrkikaku) {
        this.tenkazaislurry_tgrkikaku = tenkazaislurry_tgrkikaku;
    }

    /**
     * 添加材ｽﾗﾘｰ_風袋重量1
     * @return the tenkazaislurry_ftaijyuuryou1
     */
    public FXHDD01 getTenkazaislurry_ftaijyuuryou1() {
        return tenkazaislurry_ftaijyuuryou1;
    }

    /**
     * 添加材ｽﾗﾘｰ_風袋重量1
     * @param tenkazaislurry_ftaijyuuryou1 the tenkazaislurry_ftaijyuuryou1 to set
     */
    public void setTenkazaislurry_ftaijyuuryou1(FXHDD01 tenkazaislurry_ftaijyuuryou1) {
        this.tenkazaislurry_ftaijyuuryou1 = tenkazaislurry_ftaijyuuryou1;
    }

    /**
     * 添加材ｽﾗﾘｰ_調合量1
     * @return the tenkazaislurry_tyougouryou1
     */
    public FXHDD01 getTenkazaislurry_tyougouryou1() {
        return tenkazaislurry_tyougouryou1;
    }

    /**
     * 添加材ｽﾗﾘｰ_調合量1
     * @param tenkazaislurry_tyougouryou1 the tenkazaislurry_tyougouryou1 to set
     */
    public void setTenkazaislurry_tyougouryou1(FXHDD01 tenkazaislurry_tyougouryou1) {
        this.tenkazaislurry_tyougouryou1 = tenkazaislurry_tyougouryou1;
    }

    /**
     * 添加材ｽﾗﾘｰ_風袋重量2
     * @return the tenkazaislurry_ftaijyuuryou2
     */
    public FXHDD01 getTenkazaislurry_ftaijyuuryou2() {
        return tenkazaislurry_ftaijyuuryou2;
    }

    /**
     * 添加材ｽﾗﾘｰ_風袋重量2
     * @param tenkazaislurry_ftaijyuuryou2 the tenkazaislurry_ftaijyuuryou2 to set
     */
    public void setTenkazaislurry_ftaijyuuryou2(FXHDD01 tenkazaislurry_ftaijyuuryou2) {
        this.tenkazaislurry_ftaijyuuryou2 = tenkazaislurry_ftaijyuuryou2;
    }

    /**
     * 添加材ｽﾗﾘｰ_調合量2
     * @return the tenkazaislurry_tyougouryou2
     */
    public FXHDD01 getTenkazaislurry_tyougouryou2() {
        return tenkazaislurry_tyougouryou2;
    }

    /**
     * 添加材ｽﾗﾘｰ_調合量2
     * @param tenkazaislurry_tyougouryou2 the tenkazaislurry_tyougouryou2 to set
     */
    public void setTenkazaislurry_tyougouryou2(FXHDD01 tenkazaislurry_tyougouryou2) {
        this.tenkazaislurry_tyougouryou2 = tenkazaislurry_tyougouryou2;
    }

    /**
     * 添加材ｽﾗﾘｰ秤量終了日
     * @return the tenkazaislurryhrsyuryo_day
     */
    public FXHDD01 getTenkazaislurryhrsyuryo_day() {
        return tenkazaislurryhrsyuryo_day;
    }

    /**
     * 添加材ｽﾗﾘｰ秤量終了日
     * @param tenkazaislurryhrsyuryo_day the tenkazaislurryhrsyuryo_day to set
     */
    public void setTenkazaislurryhrsyuryo_day(FXHDD01 tenkazaislurryhrsyuryo_day) {
        this.tenkazaislurryhrsyuryo_day = tenkazaislurryhrsyuryo_day;
    }

    /**
     * 添加材ｽﾗﾘｰ秤量終了時間
     * @return the tenkazaislurryhrsyuryo_time
     */
    public FXHDD01 getTenkazaislurryhrsyuryo_time() {
        return tenkazaislurryhrsyuryo_time;
    }

    /**
     * 添加材ｽﾗﾘｰ秤量終了時間
     * @param tenkazaislurryhrsyuryo_time the tenkazaislurryhrsyuryo_time to set
     */
    public void setTenkazaislurryhrsyuryo_time(FXHDD01 tenkazaislurryhrsyuryo_time) {
        this.tenkazaislurryhrsyuryo_time = tenkazaislurryhrsyuryo_time;
    }

    /**
     * 固形分測定担当者
     * @return the kokeibunsokuteitantousya
     */
    public FXHDD01 getKokeibunsokuteitantousya() {
        return kokeibunsokuteitantousya;
    }

    /**
     * 固形分測定担当者
     * @param kokeibunsokuteitantousya the kokeibunsokuteitantousya to set
     */
    public void setKokeibunsokuteitantousya(FXHDD01 kokeibunsokuteitantousya) {
        this.kokeibunsokuteitantousya = kokeibunsokuteitantousya;
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