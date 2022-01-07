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
 * 変更日	2021/11/10<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B021(誘電体ｽﾗﾘｰ作製・添加材・ｿﾞﾙ秤量)
 *
 * @author KCSS K.Jo
 * @since  2021/11/10
 */
@ViewScoped
@Named
public class GXHDO102B021A implements Serializable {
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
     * 秤量開始日
     */
    private FXHDD01 hyouryoukaisi_day;

    /**
     * 秤量開始時間
     */
    private FXHDD01 hyouryoukaisi_time;

    /**
     * 添加材①_材料品名
     */
    private FXHDD01 tenkazai1_zairyouhinmei;

    /**
     * 添加材①_調合量規格
     */
    private FXHDD01 tenkazai1_tyougouryoukikaku;

    /**
     * 添加材①_部材在庫No1
     */
    private FXHDD01 tenkazai1_buzaizaikolotno1;

    /**
     * 添加材①_調合量1
     */
    private FXHDD01 tenkazai1_tyougouryou1;

    /**
     * 添加材①_部材在庫No2
     */
    private FXHDD01 tenkazai1_buzaizaikolotno2;

    /**
     * 添加材①_調合量2
     */
    private FXHDD01 tenkazai1_tyougouryou2;

    /**
     * 添加材②_材料品名
     */
    private FXHDD01 tenkazai2_zairyouhinmei;

    /**
     * 添加材②_調合量規格
     */
    private FXHDD01 tenkazai2_tyougouryoukikaku;

    /**
     * 添加材②_部材在庫No1
     */
    private FXHDD01 tenkazai2_buzaizaikolotno1;

    /**
     * 添加材②_調合量1
     */
    private FXHDD01 tenkazai2_tyougouryou1;

    /**
     * 添加材②_部材在庫No2
     */
    private FXHDD01 tenkazai2_buzaizaikolotno2;

    /**
     * 添加材②_調合量2
     */
    private FXHDD01 tenkazai2_tyougouryou2;

    /**
     * 添加材③_材料品名
     */
    private FXHDD01 tenkazai3_zairyouhinmei;

    /**
     * 添加材③_調合量規格
     */
    private FXHDD01 tenkazai3_tyougouryoukikaku;

    /**
     * 添加材③_部材在庫No1
     */
    private FXHDD01 tenkazai3_buzaizaikolotno1;

    /**
     * 添加材③_調合量1
     */
    private FXHDD01 tenkazai3_tyougouryou1;

    /**
     * 添加材③_部材在庫No2
     */
    private FXHDD01 tenkazai3_buzaizaikolotno2;

    /**
     * 添加材③_調合量2
     */
    private FXHDD01 tenkazai3_tyougouryou2;

    /**
     * 添加材④_材料品名
     */
    private FXHDD01 tenkazai4_zairyouhinmei;

    /**
     * 添加材④_調合量規格
     */
    private FXHDD01 tenkazai4_tyougouryoukikaku;

    /**
     * 添加材④_部材在庫No1
     */
    private FXHDD01 tenkazai4_buzaizaikolotno1;

    /**
     * 添加材④_調合量1
     */
    private FXHDD01 tenkazai4_tyougouryou1;

    /**
     * 添加材④_部材在庫No2
     */
    private FXHDD01 tenkazai4_buzaizaikolotno2;

    /**
     * 添加材④_調合量2
     */
    private FXHDD01 tenkazai4_tyougouryou2;

    /**
     * 添加材⑤_材料品名
     */
    private FXHDD01 tenkazai5_zairyouhinmei;

    /**
     * 添加材⑤_調合量規格
     */
    private FXHDD01 tenkazai5_tyougouryoukikaku;

    /**
     * 添加材⑤_部材在庫No1
     */
    private FXHDD01 tenkazai5_buzaizaikolotno1;

    /**
     * 添加材⑤_調合量1
     */
    private FXHDD01 tenkazai5_tyougouryou1;

    /**
     * 添加材⑤_部材在庫No2
     */
    private FXHDD01 tenkazai5_buzaizaikolotno2;

    /**
     * 添加材⑤_調合量2
     */
    private FXHDD01 tenkazai5_tyougouryou2;

    /**
     * ｿﾞﾙ①_材料品名
     */
    private FXHDD01 sol1_zairyouhinmei;

    /**
     * ｿﾞﾙ①_調合量規格
     */
    private FXHDD01 sol1_tyougouryoukikaku;

    /**
     * ｿﾞﾙ①_部材在庫No1
     */
    private FXHDD01 sol1_buzaizaikolotno1;

    /**
     * ｿﾞﾙ①_調合量1
     */
    private FXHDD01 sol1_tyougouryou1;

    /**
     * ｿﾞﾙ①_部材在庫No2
     */
    private FXHDD01 sol1_buzaizaikolotno2;

    /**
     * ｿﾞﾙ①_調合量2
     */
    private FXHDD01 sol1_tyougouryou2;

    /**
     * ｿﾞﾙ②_材料品名
     */
    private FXHDD01 sol2_zairyouhinmei;

    /**
     * ｿﾞﾙ②_調合量規格
     */
    private FXHDD01 sol2_tyougouryoukikaku;

    /**
     * ｿﾞﾙ②_部材在庫No1
     */
    private FXHDD01 sol2_buzaizaikolotno1;

    /**
     * ｿﾞﾙ②_調合量1
     */
    private FXHDD01 sol2_tyougouryou1;

    /**
     * ｿﾞﾙ②_部材在庫No2
     */
    private FXHDD01 sol2_buzaizaikolotno2;

    /**
     * ｿﾞﾙ②_調合量2
     */
    private FXHDD01 sol2_tyougouryou2;

    /**
     * ｿﾞﾙ③_材料品名
     */
    private FXHDD01 sol3_zairyouhinmei;

    /**
     * ｿﾞﾙ③_調合量規格
     */
    private FXHDD01 sol3_tyougouryoukikaku;

    /**
     * ｿﾞﾙ③_部材在庫No1
     */
    private FXHDD01 sol3_buzaizaikolotno1;

    /**
     * ｿﾞﾙ③_調合量1
     */
    private FXHDD01 sol3_tyougouryou1;

    /**
     * ｿﾞﾙ③_部材在庫No2
     */
    private FXHDD01 sol3_buzaizaikolotno2;

    /**
     * ｿﾞﾙ③_調合量2
     */
    private FXHDD01 sol3_tyougouryou2;

    /**
     * ｿﾞﾙ④_材料品名
     */
    private FXHDD01 sol4_zairyouhinmei;

    /**
     * ｿﾞﾙ④_調合量規格
     */
    private FXHDD01 sol4_tyougouryoukikaku;

    /**
     * ｿﾞﾙ④_部材在庫No1
     */
    private FXHDD01 sol4_buzaizaikolotno1;

    /**
     * ｿﾞﾙ④_調合量1
     */
    private FXHDD01 sol4_tyougouryou1;

    /**
     * ｿﾞﾙ④_部材在庫No2
     */
    private FXHDD01 sol4_buzaizaikolotno2;

    /**
     * ｿﾞﾙ④_調合量2
     */
    private FXHDD01 sol4_tyougouryou2;

    /**
     * 秤量終了日
     */
    private FXHDD01 hyouryousyuuryou_day;

    /**
     * 秤量終了時間
     */
    private FXHDD01 hyouryousyuuryou_time;

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
     * 添加材①_材料品名
     * @return the tenkazai1_zairyouhinmei
     */
    public FXHDD01 getTenkazai1_zairyouhinmei() {
        return tenkazai1_zairyouhinmei;
    }

    /**
     * 添加材①_材料品名
     * @param tenkazai1_zairyouhinmei the tenkazai1_zairyouhinmei to set
     */
    public void setTenkazai1_zairyouhinmei(FXHDD01 tenkazai1_zairyouhinmei) {
        this.tenkazai1_zairyouhinmei = tenkazai1_zairyouhinmei;
    }

    /**
     * 添加材①_調合量規格
     * @return the tenkazai1_tyougouryoukikaku
     */
    public FXHDD01 getTenkazai1_tyougouryoukikaku() {
        return tenkazai1_tyougouryoukikaku;
    }

    /**
     * 添加材①_調合量規格
     * @param tenkazai1_tyougouryoukikaku the tenkazai1_tyougouryoukikaku to set
     */
    public void setTenkazai1_tyougouryoukikaku(FXHDD01 tenkazai1_tyougouryoukikaku) {
        this.tenkazai1_tyougouryoukikaku = tenkazai1_tyougouryoukikaku;
    }

    /**
     * 添加材①_部材在庫No1
     * @return the tenkazai1_buzaizaikolotno1
     */
    public FXHDD01 getTenkazai1_buzaizaikolotno1() {
        return tenkazai1_buzaizaikolotno1;
    }

    /**
     * 添加材①_部材在庫No1
     * @param tenkazai1_buzaizaikolotno1 the tenkazai1_buzaizaikolotno1 to set
     */
    public void setTenkazai1_buzaizaikolotno1(FXHDD01 tenkazai1_buzaizaikolotno1) {
        this.tenkazai1_buzaizaikolotno1 = tenkazai1_buzaizaikolotno1;
    }

    /**
     * 添加材①_調合量1
     * @return the tenkazai1_tyougouryou1
     */
    public FXHDD01 getTenkazai1_tyougouryou1() {
        return tenkazai1_tyougouryou1;
    }

    /**
     * 添加材①_調合量1
     * @param tenkazai1_tyougouryou1 the tenkazai1_tyougouryou1 to set
     */
    public void setTenkazai1_tyougouryou1(FXHDD01 tenkazai1_tyougouryou1) {
        this.tenkazai1_tyougouryou1 = tenkazai1_tyougouryou1;
    }

    /**
     * 添加材①_部材在庫No2
     * @return the tenkazai1_buzaizaikolotno2
     */
    public FXHDD01 getTenkazai1_buzaizaikolotno2() {
        return tenkazai1_buzaizaikolotno2;
    }

    /**
     * 添加材①_部材在庫No2
     * @param tenkazai1_buzaizaikolotno2 the tenkazai1_buzaizaikolotno2 to set
     */
    public void setTenkazai1_buzaizaikolotno2(FXHDD01 tenkazai1_buzaizaikolotno2) {
        this.tenkazai1_buzaizaikolotno2 = tenkazai1_buzaizaikolotno2;
    }

    /**
     * 添加材①_調合量2
     * @return the tenkazai1_tyougouryou2
     */
    public FXHDD01 getTenkazai1_tyougouryou2() {
        return tenkazai1_tyougouryou2;
    }

    /**
     * 添加材①_調合量2
     * @param tenkazai1_tyougouryou2 the tenkazai1_tyougouryou2 to set
     */
    public void setTenkazai1_tyougouryou2(FXHDD01 tenkazai1_tyougouryou2) {
        this.tenkazai1_tyougouryou2 = tenkazai1_tyougouryou2;
    }

    /**
     * 添加材②_材料品名
     * @return the tenkazai2_zairyouhinmei
     */
    public FXHDD01 getTenkazai2_zairyouhinmei() {
        return tenkazai2_zairyouhinmei;
    }

    /**
     * 添加材②_材料品名
     * @param tenkazai2_zairyouhinmei the tenkazai2_zairyouhinmei to set
     */
    public void setTenkazai2_zairyouhinmei(FXHDD01 tenkazai2_zairyouhinmei) {
        this.tenkazai2_zairyouhinmei = tenkazai2_zairyouhinmei;
    }

    /**
     * 添加材②_調合量規格
     * @return the tenkazai2_tyougouryoukikaku
     */
    public FXHDD01 getTenkazai2_tyougouryoukikaku() {
        return tenkazai2_tyougouryoukikaku;
    }

    /**
     * 添加材②_調合量規格
     * @param tenkazai2_tyougouryoukikaku the tenkazai2_tyougouryoukikaku to set
     */
    public void setTenkazai2_tyougouryoukikaku(FXHDD01 tenkazai2_tyougouryoukikaku) {
        this.tenkazai2_tyougouryoukikaku = tenkazai2_tyougouryoukikaku;
    }

    /**
     * 添加材②_部材在庫No1
     * @return the tenkazai2_buzaizaikolotno1
     */
    public FXHDD01 getTenkazai2_buzaizaikolotno1() {
        return tenkazai2_buzaizaikolotno1;
    }

    /**
     * 添加材②_部材在庫No1
     * @param tenkazai2_buzaizaikolotno1 the tenkazai2_buzaizaikolotno1 to set
     */
    public void setTenkazai2_buzaizaikolotno1(FXHDD01 tenkazai2_buzaizaikolotno1) {
        this.tenkazai2_buzaizaikolotno1 = tenkazai2_buzaizaikolotno1;
    }

    /**
     * 添加材②_調合量1
     * @return the tenkazai2_tyougouryou1
     */
    public FXHDD01 getTenkazai2_tyougouryou1() {
        return tenkazai2_tyougouryou1;
    }

    /**
     * 添加材②_調合量1
     * @param tenkazai2_tyougouryou1 the tenkazai2_tyougouryou1 to set
     */
    public void setTenkazai2_tyougouryou1(FXHDD01 tenkazai2_tyougouryou1) {
        this.tenkazai2_tyougouryou1 = tenkazai2_tyougouryou1;
    }

    /**
     * 添加材②_部材在庫No2
     * @return the tenkazai2_buzaizaikolotno2
     */
    public FXHDD01 getTenkazai2_buzaizaikolotno2() {
        return tenkazai2_buzaizaikolotno2;
    }

    /**
     * 添加材②_部材在庫No2
     * @param tenkazai2_buzaizaikolotno2 the tenkazai2_buzaizaikolotno2 to set
     */
    public void setTenkazai2_buzaizaikolotno2(FXHDD01 tenkazai2_buzaizaikolotno2) {
        this.tenkazai2_buzaizaikolotno2 = tenkazai2_buzaizaikolotno2;
    }

    /**
     * 添加材②_調合量2
     * @return the tenkazai2_tyougouryou2
     */
    public FXHDD01 getTenkazai2_tyougouryou2() {
        return tenkazai2_tyougouryou2;
    }

    /**
     * 添加材②_調合量2
     * @param tenkazai2_tyougouryou2 the tenkazai2_tyougouryou2 to set
     */
    public void setTenkazai2_tyougouryou2(FXHDD01 tenkazai2_tyougouryou2) {
        this.tenkazai2_tyougouryou2 = tenkazai2_tyougouryou2;
    }

    /**
     * 添加材③_材料品名
     * @return the tenkazai3_zairyouhinmei
     */
    public FXHDD01 getTenkazai3_zairyouhinmei() {
        return tenkazai3_zairyouhinmei;
    }

    /**
     * 添加材③_材料品名
     * @param tenkazai3_zairyouhinmei the tenkazai3_zairyouhinmei to set
     */
    public void setTenkazai3_zairyouhinmei(FXHDD01 tenkazai3_zairyouhinmei) {
        this.tenkazai3_zairyouhinmei = tenkazai3_zairyouhinmei;
    }

    /**
     * 添加材③_調合量規格
     * @return the tenkazai3_tyougouryoukikaku
     */
    public FXHDD01 getTenkazai3_tyougouryoukikaku() {
        return tenkazai3_tyougouryoukikaku;
    }

    /**
     * 添加材③_調合量規格
     * @param tenkazai3_tyougouryoukikaku the tenkazai3_tyougouryoukikaku to set
     */
    public void setTenkazai3_tyougouryoukikaku(FXHDD01 tenkazai3_tyougouryoukikaku) {
        this.tenkazai3_tyougouryoukikaku = tenkazai3_tyougouryoukikaku;
    }

    /**
     * 添加材③_部材在庫No1
     * @return the tenkazai3_buzaizaikolotno1
     */
    public FXHDD01 getTenkazai3_buzaizaikolotno1() {
        return tenkazai3_buzaizaikolotno1;
    }

    /**
     * 添加材③_部材在庫No1
     * @param tenkazai3_buzaizaikolotno1 the tenkazai3_buzaizaikolotno1 to set
     */
    public void setTenkazai3_buzaizaikolotno1(FXHDD01 tenkazai3_buzaizaikolotno1) {
        this.tenkazai3_buzaizaikolotno1 = tenkazai3_buzaizaikolotno1;
    }

    /**
     * 添加材③_調合量1
     * @return the tenkazai3_tyougouryou1
     */
    public FXHDD01 getTenkazai3_tyougouryou1() {
        return tenkazai3_tyougouryou1;
    }

    /**
     * 添加材③_調合量1
     * @param tenkazai3_tyougouryou1 the tenkazai3_tyougouryou1 to set
     */
    public void setTenkazai3_tyougouryou1(FXHDD01 tenkazai3_tyougouryou1) {
        this.tenkazai3_tyougouryou1 = tenkazai3_tyougouryou1;
    }

    /**
     * 添加材③_部材在庫No2
     * @return the tenkazai3_buzaizaikolotno2
     */
    public FXHDD01 getTenkazai3_buzaizaikolotno2() {
        return tenkazai3_buzaizaikolotno2;
    }

    /**
     * 添加材③_部材在庫No2
     * @param tenkazai3_buzaizaikolotno2 the tenkazai3_buzaizaikolotno2 to set
     */
    public void setTenkazai3_buzaizaikolotno2(FXHDD01 tenkazai3_buzaizaikolotno2) {
        this.tenkazai3_buzaizaikolotno2 = tenkazai3_buzaizaikolotno2;
    }

    /**
     * 添加材③_調合量2
     * @return the tenkazai3_tyougouryou2
     */
    public FXHDD01 getTenkazai3_tyougouryou2() {
        return tenkazai3_tyougouryou2;
    }

    /**
     * 添加材③_調合量2
     * @param tenkazai3_tyougouryou2 the tenkazai3_tyougouryou2 to set
     */
    public void setTenkazai3_tyougouryou2(FXHDD01 tenkazai3_tyougouryou2) {
        this.tenkazai3_tyougouryou2 = tenkazai3_tyougouryou2;
    }

    /**
     * 添加材④_材料品名
     * @return the tenkazai4_zairyouhinmei
     */
    public FXHDD01 getTenkazai4_zairyouhinmei() {
        return tenkazai4_zairyouhinmei;
    }

    /**
     * 添加材④_材料品名
     * @param tenkazai4_zairyouhinmei the tenkazai4_zairyouhinmei to set
     */
    public void setTenkazai4_zairyouhinmei(FXHDD01 tenkazai4_zairyouhinmei) {
        this.tenkazai4_zairyouhinmei = tenkazai4_zairyouhinmei;
    }

    /**
     * 添加材④_調合量規格
     * @return the tenkazai4_tyougouryoukikaku
     */
    public FXHDD01 getTenkazai4_tyougouryoukikaku() {
        return tenkazai4_tyougouryoukikaku;
    }

    /**
     * 添加材④_調合量規格
     * @param tenkazai4_tyougouryoukikaku the tenkazai4_tyougouryoukikaku to set
     */
    public void setTenkazai4_tyougouryoukikaku(FXHDD01 tenkazai4_tyougouryoukikaku) {
        this.tenkazai4_tyougouryoukikaku = tenkazai4_tyougouryoukikaku;
    }

    /**
     * 添加材④_部材在庫No1
     * @return the tenkazai4_buzaizaikolotno1
     */
    public FXHDD01 getTenkazai4_buzaizaikolotno1() {
        return tenkazai4_buzaizaikolotno1;
    }

    /**
     * 添加材④_部材在庫No1
     * @param tenkazai4_buzaizaikolotno1 the tenkazai4_buzaizaikolotno1 to set
     */
    public void setTenkazai4_buzaizaikolotno1(FXHDD01 tenkazai4_buzaizaikolotno1) {
        this.tenkazai4_buzaizaikolotno1 = tenkazai4_buzaizaikolotno1;
    }

    /**
     * 添加材④_調合量1
     * @return the tenkazai4_tyougouryou1
     */
    public FXHDD01 getTenkazai4_tyougouryou1() {
        return tenkazai4_tyougouryou1;
    }

    /**
     * 添加材④_調合量1
     * @param tenkazai4_tyougouryou1 the tenkazai4_tyougouryou1 to set
     */
    public void setTenkazai4_tyougouryou1(FXHDD01 tenkazai4_tyougouryou1) {
        this.tenkazai4_tyougouryou1 = tenkazai4_tyougouryou1;
    }

    /**
     * 添加材④_部材在庫No2
     * @return the tenkazai4_buzaizaikolotno2
     */
    public FXHDD01 getTenkazai4_buzaizaikolotno2() {
        return tenkazai4_buzaizaikolotno2;
    }

    /**
     * 添加材④_部材在庫No2
     * @param tenkazai4_buzaizaikolotno2 the tenkazai4_buzaizaikolotno2 to set
     */
    public void setTenkazai4_buzaizaikolotno2(FXHDD01 tenkazai4_buzaizaikolotno2) {
        this.tenkazai4_buzaizaikolotno2 = tenkazai4_buzaizaikolotno2;
    }

    /**
     * 添加材④_調合量2
     * @return the tenkazai4_tyougouryou2
     */
    public FXHDD01 getTenkazai4_tyougouryou2() {
        return tenkazai4_tyougouryou2;
    }

    /**
     * 添加材④_調合量2
     * @param tenkazai4_tyougouryou2 the tenkazai4_tyougouryou2 to set
     */
    public void setTenkazai4_tyougouryou2(FXHDD01 tenkazai4_tyougouryou2) {
        this.tenkazai4_tyougouryou2 = tenkazai4_tyougouryou2;
    }

    /**
     * 添加材⑤_材料品名
     * @return the tenkazai5_zairyouhinmei
     */
    public FXHDD01 getTenkazai5_zairyouhinmei() {
        return tenkazai5_zairyouhinmei;
    }

    /**
     * 添加材⑤_材料品名
     * @param tenkazai5_zairyouhinmei the tenkazai5_zairyouhinmei to set
     */
    public void setTenkazai5_zairyouhinmei(FXHDD01 tenkazai5_zairyouhinmei) {
        this.tenkazai5_zairyouhinmei = tenkazai5_zairyouhinmei;
    }

    /**
     * 添加材⑤_調合量規格
     * @return the tenkazai5_tyougouryoukikaku
     */
    public FXHDD01 getTenkazai5_tyougouryoukikaku() {
        return tenkazai5_tyougouryoukikaku;
    }

    /**
     * 添加材⑤_調合量規格
     * @param tenkazai5_tyougouryoukikaku the tenkazai5_tyougouryoukikaku to set
     */
    public void setTenkazai5_tyougouryoukikaku(FXHDD01 tenkazai5_tyougouryoukikaku) {
        this.tenkazai5_tyougouryoukikaku = tenkazai5_tyougouryoukikaku;
    }

    /**
     * 添加材⑤_部材在庫No1
     * @return the tenkazai5_buzaizaikolotno1
     */
    public FXHDD01 getTenkazai5_buzaizaikolotno1() {
        return tenkazai5_buzaizaikolotno1;
    }

    /**
     * 添加材⑤_部材在庫No1
     * @param tenkazai5_buzaizaikolotno1 the tenkazai5_buzaizaikolotno1 to set
     */
    public void setTenkazai5_buzaizaikolotno1(FXHDD01 tenkazai5_buzaizaikolotno1) {
        this.tenkazai5_buzaizaikolotno1 = tenkazai5_buzaizaikolotno1;
    }

    /**
     * 添加材⑤_調合量1
     * @return the tenkazai5_tyougouryou1
     */
    public FXHDD01 getTenkazai5_tyougouryou1() {
        return tenkazai5_tyougouryou1;
    }

    /**
     * 添加材⑤_調合量1
     * @param tenkazai5_tyougouryou1 the tenkazai5_tyougouryou1 to set
     */
    public void setTenkazai5_tyougouryou1(FXHDD01 tenkazai5_tyougouryou1) {
        this.tenkazai5_tyougouryou1 = tenkazai5_tyougouryou1;
    }

    /**
     * 添加材⑤_部材在庫No2
     * @return the tenkazai5_buzaizaikolotno2
     */
    public FXHDD01 getTenkazai5_buzaizaikolotno2() {
        return tenkazai5_buzaizaikolotno2;
    }

    /**
     * 添加材⑤_部材在庫No2
     * @param tenkazai5_buzaizaikolotno2 the tenkazai5_buzaizaikolotno2 to set
     */
    public void setTenkazai5_buzaizaikolotno2(FXHDD01 tenkazai5_buzaizaikolotno2) {
        this.tenkazai5_buzaizaikolotno2 = tenkazai5_buzaizaikolotno2;
    }

    /**
     * 添加材⑤_調合量2
     * @return the tenkazai5_tyougouryou2
     */
    public FXHDD01 getTenkazai5_tyougouryou2() {
        return tenkazai5_tyougouryou2;
    }

    /**
     * 添加材⑤_調合量2
     * @param tenkazai5_tyougouryou2 the tenkazai5_tyougouryou2 to set
     */
    public void setTenkazai5_tyougouryou2(FXHDD01 tenkazai5_tyougouryou2) {
        this.tenkazai5_tyougouryou2 = tenkazai5_tyougouryou2;
    }

    /**
     * ｿﾞﾙ①_材料品名
     * @return the sol1_zairyouhinmei
     */
    public FXHDD01 getSol1_zairyouhinmei() {
        return sol1_zairyouhinmei;
    }

    /**
     * ｿﾞﾙ①_材料品名
     * @param sol1_zairyouhinmei the sol1_zairyouhinmei to set
     */
    public void setSol1_zairyouhinmei(FXHDD01 sol1_zairyouhinmei) {
        this.sol1_zairyouhinmei = sol1_zairyouhinmei;
    }

    /**
     * ｿﾞﾙ①_調合量規格
     * @return the sol1_tyougouryoukikaku
     */
    public FXHDD01 getSol1_tyougouryoukikaku() {
        return sol1_tyougouryoukikaku;
    }

    /**
     * ｿﾞﾙ①_調合量規格
     * @param sol1_tyougouryoukikaku the sol1_tyougouryoukikaku to set
     */
    public void setSol1_tyougouryoukikaku(FXHDD01 sol1_tyougouryoukikaku) {
        this.sol1_tyougouryoukikaku = sol1_tyougouryoukikaku;
    }

    /**
     * ｿﾞﾙ①_部材在庫No1
     * @return the sol1_buzaizaikolotno1
     */
    public FXHDD01 getSol1_buzaizaikolotno1() {
        return sol1_buzaizaikolotno1;
    }

    /**
     * ｿﾞﾙ①_部材在庫No1
     * @param sol1_buzaizaikolotno1 the sol1_buzaizaikolotno1 to set
     */
    public void setSol1_buzaizaikolotno1(FXHDD01 sol1_buzaizaikolotno1) {
        this.sol1_buzaizaikolotno1 = sol1_buzaizaikolotno1;
    }

    /**
     * ｿﾞﾙ①_調合量1
     * @return the sol1_tyougouryou1
     */
    public FXHDD01 getSol1_tyougouryou1() {
        return sol1_tyougouryou1;
    }

    /**
     * ｿﾞﾙ①_調合量1
     * @param sol1_tyougouryou1 the sol1_tyougouryou1 to set
     */
    public void setSol1_tyougouryou1(FXHDD01 sol1_tyougouryou1) {
        this.sol1_tyougouryou1 = sol1_tyougouryou1;
    }

    /**
     * ｿﾞﾙ①_部材在庫No2
     * @return the sol1_buzaizaikolotno2
     */
    public FXHDD01 getSol1_buzaizaikolotno2() {
        return sol1_buzaizaikolotno2;
    }

    /**
     * ｿﾞﾙ①_部材在庫No2
     * @param sol1_buzaizaikolotno2 the sol1_buzaizaikolotno2 to set
     */
    public void setSol1_buzaizaikolotno2(FXHDD01 sol1_buzaizaikolotno2) {
        this.sol1_buzaizaikolotno2 = sol1_buzaizaikolotno2;
    }

    /**
     * ｿﾞﾙ①_調合量2
     * @return the sol1_tyougouryou2
     */
    public FXHDD01 getSol1_tyougouryou2() {
        return sol1_tyougouryou2;
    }

    /**
     * ｿﾞﾙ①_調合量2
     * @param sol1_tyougouryou2 the sol1_tyougouryou2 to set
     */
    public void setSol1_tyougouryou2(FXHDD01 sol1_tyougouryou2) {
        this.sol1_tyougouryou2 = sol1_tyougouryou2;
    }

    /**
     * ｿﾞﾙ②_材料品名
     * @return the sol2_zairyouhinmei
     */
    public FXHDD01 getSol2_zairyouhinmei() {
        return sol2_zairyouhinmei;
    }

    /**
     * ｿﾞﾙ②_材料品名
     * @param sol2_zairyouhinmei the sol2_zairyouhinmei to set
     */
    public void setSol2_zairyouhinmei(FXHDD01 sol2_zairyouhinmei) {
        this.sol2_zairyouhinmei = sol2_zairyouhinmei;
    }

    /**
     * ｿﾞﾙ②_調合量規格
     * @return the sol2_tyougouryoukikaku
     */
    public FXHDD01 getSol2_tyougouryoukikaku() {
        return sol2_tyougouryoukikaku;
    }

    /**
     * ｿﾞﾙ②_調合量規格
     * @param sol2_tyougouryoukikaku the sol2_tyougouryoukikaku to set
     */
    public void setSol2_tyougouryoukikaku(FXHDD01 sol2_tyougouryoukikaku) {
        this.sol2_tyougouryoukikaku = sol2_tyougouryoukikaku;
    }

    /**
     * ｿﾞﾙ②_部材在庫No1
     * @return the sol2_buzaizaikolotno1
     */
    public FXHDD01 getSol2_buzaizaikolotno1() {
        return sol2_buzaizaikolotno1;
    }

    /**
     * ｿﾞﾙ②_部材在庫No1
     * @param sol2_buzaizaikolotno1 the sol2_buzaizaikolotno1 to set
     */
    public void setSol2_buzaizaikolotno1(FXHDD01 sol2_buzaizaikolotno1) {
        this.sol2_buzaizaikolotno1 = sol2_buzaizaikolotno1;
    }

    /**
     * ｿﾞﾙ②_調合量1
     * @return the sol2_tyougouryou1
     */
    public FXHDD01 getSol2_tyougouryou1() {
        return sol2_tyougouryou1;
    }

    /**
     * ｿﾞﾙ②_調合量1
     * @param sol2_tyougouryou1 the sol2_tyougouryou1 to set
     */
    public void setSol2_tyougouryou1(FXHDD01 sol2_tyougouryou1) {
        this.sol2_tyougouryou1 = sol2_tyougouryou1;
    }

    /**
     * ｿﾞﾙ②_部材在庫No2
     * @return the sol2_buzaizaikolotno2
     */
    public FXHDD01 getSol2_buzaizaikolotno2() {
        return sol2_buzaizaikolotno2;
    }

    /**
     * ｿﾞﾙ②_部材在庫No2
     * @param sol2_buzaizaikolotno2 the sol2_buzaizaikolotno2 to set
     */
    public void setSol2_buzaizaikolotno2(FXHDD01 sol2_buzaizaikolotno2) {
        this.sol2_buzaizaikolotno2 = sol2_buzaizaikolotno2;
    }

    /**
     * ｿﾞﾙ②_調合量2
     * @return the sol2_tyougouryou2
     */
    public FXHDD01 getSol2_tyougouryou2() {
        return sol2_tyougouryou2;
    }

    /**
     * ｿﾞﾙ②_調合量2
     * @param sol2_tyougouryou2 the sol2_tyougouryou2 to set
     */
    public void setSol2_tyougouryou2(FXHDD01 sol2_tyougouryou2) {
        this.sol2_tyougouryou2 = sol2_tyougouryou2;
    }

    /**
     * ｿﾞﾙ③_材料品名
     * @return the sol3_zairyouhinmei
     */
    public FXHDD01 getSol3_zairyouhinmei() {
        return sol3_zairyouhinmei;
    }

    /**
     * ｿﾞﾙ③_材料品名
     * @param sol3_zairyouhinmei the sol3_zairyouhinmei to set
     */
    public void setSol3_zairyouhinmei(FXHDD01 sol3_zairyouhinmei) {
        this.sol3_zairyouhinmei = sol3_zairyouhinmei;
    }

    /**
     * ｿﾞﾙ③_調合量規格
     * @return the sol3_tyougouryoukikaku
     */
    public FXHDD01 getSol3_tyougouryoukikaku() {
        return sol3_tyougouryoukikaku;
    }

    /**
     * ｿﾞﾙ③_調合量規格
     * @param sol3_tyougouryoukikaku the sol3_tyougouryoukikaku to set
     */
    public void setSol3_tyougouryoukikaku(FXHDD01 sol3_tyougouryoukikaku) {
        this.sol3_tyougouryoukikaku = sol3_tyougouryoukikaku;
    }

    /**
     * ｿﾞﾙ③_部材在庫No1
     * @return the sol3_buzaizaikolotno1
     */
    public FXHDD01 getSol3_buzaizaikolotno1() {
        return sol3_buzaizaikolotno1;
    }

    /**
     * ｿﾞﾙ③_部材在庫No1
     * @param sol3_buzaizaikolotno1 the sol3_buzaizaikolotno1 to set
     */
    public void setSol3_buzaizaikolotno1(FXHDD01 sol3_buzaizaikolotno1) {
        this.sol3_buzaizaikolotno1 = sol3_buzaizaikolotno1;
    }

    /**
     * ｿﾞﾙ③_調合量1
     * @return the sol3_tyougouryou1
     */
    public FXHDD01 getSol3_tyougouryou1() {
        return sol3_tyougouryou1;
    }

    /**
     * ｿﾞﾙ③_調合量1
     * @param sol3_tyougouryou1 the sol3_tyougouryou1 to set
     */
    public void setSol3_tyougouryou1(FXHDD01 sol3_tyougouryou1) {
        this.sol3_tyougouryou1 = sol3_tyougouryou1;
    }

    /**
     * ｿﾞﾙ③_部材在庫No2
     * @return the sol3_buzaizaikolotno2
     */
    public FXHDD01 getSol3_buzaizaikolotno2() {
        return sol3_buzaizaikolotno2;
    }

    /**
     * ｿﾞﾙ③_部材在庫No2
     * @param sol3_buzaizaikolotno2 the sol3_buzaizaikolotno2 to set
     */
    public void setSol3_buzaizaikolotno2(FXHDD01 sol3_buzaizaikolotno2) {
        this.sol3_buzaizaikolotno2 = sol3_buzaizaikolotno2;
    }

    /**
     * ｿﾞﾙ③_調合量2
     * @return the sol3_tyougouryou2
     */
    public FXHDD01 getSol3_tyougouryou2() {
        return sol3_tyougouryou2;
    }

    /**
     * ｿﾞﾙ③_調合量2
     * @param sol3_tyougouryou2 the sol3_tyougouryou2 to set
     */
    public void setSol3_tyougouryou2(FXHDD01 sol3_tyougouryou2) {
        this.sol3_tyougouryou2 = sol3_tyougouryou2;
    }

    /**
     * ｿﾞﾙ④_材料品名
     * @return the sol4_zairyouhinmei
     */
    public FXHDD01 getSol4_zairyouhinmei() {
        return sol4_zairyouhinmei;
    }

    /**
     * ｿﾞﾙ④_材料品名
     * @param sol4_zairyouhinmei the sol4_zairyouhinmei to set
     */
    public void setSol4_zairyouhinmei(FXHDD01 sol4_zairyouhinmei) {
        this.sol4_zairyouhinmei = sol4_zairyouhinmei;
    }

    /**
     * ｿﾞﾙ④_調合量規格
     * @return the sol4_tyougouryoukikaku
     */
    public FXHDD01 getSol4_tyougouryoukikaku() {
        return sol4_tyougouryoukikaku;
    }

    /**
     * ｿﾞﾙ④_調合量規格
     * @param sol4_tyougouryoukikaku the sol4_tyougouryoukikaku to set
     */
    public void setSol4_tyougouryoukikaku(FXHDD01 sol4_tyougouryoukikaku) {
        this.sol4_tyougouryoukikaku = sol4_tyougouryoukikaku;
    }

    /**
     * ｿﾞﾙ④_部材在庫No1
     * @return the sol4_buzaizaikolotno1
     */
    public FXHDD01 getSol4_buzaizaikolotno1() {
        return sol4_buzaizaikolotno1;
    }

    /**
     * ｿﾞﾙ④_部材在庫No1
     * @param sol4_buzaizaikolotno1 the sol4_buzaizaikolotno1 to set
     */
    public void setSol4_buzaizaikolotno1(FXHDD01 sol4_buzaizaikolotno1) {
        this.sol4_buzaizaikolotno1 = sol4_buzaizaikolotno1;
    }

    /**
     * ｿﾞﾙ④_調合量1
     * @return the sol4_tyougouryou1
     */
    public FXHDD01 getSol4_tyougouryou1() {
        return sol4_tyougouryou1;
    }

    /**
     * ｿﾞﾙ④_調合量1
     * @param sol4_tyougouryou1 the sol4_tyougouryou1 to set
     */
    public void setSol4_tyougouryou1(FXHDD01 sol4_tyougouryou1) {
        this.sol4_tyougouryou1 = sol4_tyougouryou1;
    }

    /**
     * ｿﾞﾙ④_部材在庫No2
     * @return the sol4_buzaizaikolotno2
     */
    public FXHDD01 getSol4_buzaizaikolotno2() {
        return sol4_buzaizaikolotno2;
    }

    /**
     * ｿﾞﾙ④_部材在庫No2
     * @param sol4_buzaizaikolotno2 the sol4_buzaizaikolotno2 to set
     */
    public void setSol4_buzaizaikolotno2(FXHDD01 sol4_buzaizaikolotno2) {
        this.sol4_buzaizaikolotno2 = sol4_buzaizaikolotno2;
    }

    /**
     * ｿﾞﾙ④_調合量2
     * @return the sol4_tyougouryou2
     */
    public FXHDD01 getSol4_tyougouryou2() {
        return sol4_tyougouryou2;
    }

    /**
     * ｿﾞﾙ④_調合量2
     * @param sol4_tyougouryou2 the sol4_tyougouryou2 to set
     */
    public void setSol4_tyougouryou2(FXHDD01 sol4_tyougouryou2) {
        this.sol4_tyougouryou2 = sol4_tyougouryou2;
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