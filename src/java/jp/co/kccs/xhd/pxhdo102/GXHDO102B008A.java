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
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B008(添加材ｽﾗﾘｰ作製・添加材調合)
 *
 * @author KCSS K.Jo
 * @since  2021/10/15
 */
@ViewScoped
@Named
public class GXHDO102B008A implements Serializable {
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
     * 添加材①_材料品名
     */
    private FXHDD01 tenkazai1_zairyoumei;

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
    private FXHDD01 tenkazai2_zairyoumei;

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
    private FXHDD01 tenkazai3_zairyoumei;

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
    private FXHDD01 tenkazai4_zairyoumei;

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
    private FXHDD01 tenkazai5_zairyoumei;

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
     * 添加材⑥_材料品名
     */
    private FXHDD01 tenkazai6_zairyoumei;

    /**
     * 添加材⑥_調合量規格
     */
    private FXHDD01 tenkazai6_tyougouryoukikaku;

    /**
     * 添加材⑥_部材在庫No1
     */
    private FXHDD01 tenkazai6_buzaizaikolotno1;

    /**
     * 添加材⑥_調合量1
     */
    private FXHDD01 tenkazai6_tyougouryou1;

    /**
     * 添加材⑥_部材在庫No2
     */
    private FXHDD01 tenkazai6_buzaizaikolotno2;

    /**
     * 添加材⑥_調合量2
     */
    private FXHDD01 tenkazai6_tyougouryou2;

    /**
     * 添加材⑦_材料品名
     */
    private FXHDD01 tenkazai7_zairyoumei;

    /**
     * 添加材⑦_調合量規格
     */
    private FXHDD01 tenkazai7_tyougouryoukikaku;

    /**
     * 添加材⑦_部材在庫No1
     */
    private FXHDD01 tenkazai7_buzaizaikolotno1;

    /**
     * 添加材⑦_調合量1
     */
    private FXHDD01 tenkazai7_tyougouryou1;

    /**
     * 添加材⑦_部材在庫No2
     */
    private FXHDD01 tenkazai7_buzaizaikolotno2;

    /**
     * 添加材⑦_調合量2
     */
    private FXHDD01 tenkazai7_tyougouryou2;

    /**
     * 添加材⑧_材料品名
     */
    private FXHDD01 tenkazai8_zairyoumei;

    /**
     * 添加材⑧_調合量規格
     */
    private FXHDD01 tenkazai8_tyougouryoukikaku;

    /**
     * 添加材⑧_部材在庫No1
     */
    private FXHDD01 tenkazai8_buzaizaikolotno1;

    /**
     * 添加材⑧_調合量1
     */
    private FXHDD01 tenkazai8_tyougouryou1;

    /**
     * 添加材⑧_部材在庫No2
     */
    private FXHDD01 tenkazai8_buzaizaikolotno2;

    /**
     * 添加材⑧_調合量2
     */
    private FXHDD01 tenkazai8_tyougouryou2;

    /**
     * 添加材⑨_材料品名
     */
    private FXHDD01 tenkazai9_zairyoumei;

    /**
     * 添加材⑨_調合量規格
     */
    private FXHDD01 tenkazai9_tyougouryoukikaku;

    /**
     * 添加材⑨_部材在庫No1
     */
    private FXHDD01 tenkazai9_buzaizaikolotno1;

    /**
     * 添加材⑨_調合量1
     */
    private FXHDD01 tenkazai9_tyougouryou1;

    /**
     * 添加材⑨_部材在庫No2
     */
    private FXHDD01 tenkazai9_buzaizaikolotno2;

    /**
     * 添加材⑨_調合量2
     */
    private FXHDD01 tenkazai9_tyougouryou2;

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
     * 添加材ｽﾗﾘｰ品名
     * @return the tenkazaislurryhinmei
     */
    public FXHDD01 getTenkazaislurryhinmei() {
        return tenkazaislurryhinmei;
    }

    /**
     * 添加材ｽﾗﾘｰ品名
     * @param tenkazaislurryhinmei the tenkazaislurryhinmei to set
     */
    public void setTenkazaislurryhinmei(FXHDD01 tenkazaislurryhinmei) {
        this.tenkazaislurryhinmei = tenkazaislurryhinmei;
    }

    /**
     * 添加材ｽﾗﾘｰLotNo
     * @return the tenkazaislurrylotno
     */
    public FXHDD01 getTenkazaislurrylotno() {
        return tenkazaislurrylotno;
    }

    /**
     * 添加材ｽﾗﾘｰLotNo
     * @param tenkazaislurrylotno the tenkazaislurrylotno to set
     */
    public void setTenkazaislurrylotno(FXHDD01 tenkazaislurrylotno) {
        this.tenkazaislurrylotno = tenkazaislurrylotno;
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
     * @return the hyouryougouki
     */
    public FXHDD01 getHyouryougouki() {
        return hyouryougouki;
    }

    /**
     * 秤量号機
     * @param hyouryougouki the hyouryougouki to set
     */
    public void setHyouryougouki(FXHDD01 hyouryougouki) {
        this.hyouryougouki = hyouryougouki;
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
     * 添加材①_材料品名
     * @return the tenkazai1_zairyoumei
     */
    public FXHDD01 getTenkazai1_zairyoumei() {
        return tenkazai1_zairyoumei;
    }

    /**
     * 添加材①_材料品名
     * @param tenkazai1_zairyoumei the tenkazai1_zairyoumei to set
     */
    public void setTenkazai1_zairyoumei(FXHDD01 tenkazai1_zairyoumei) {
        this.tenkazai1_zairyoumei = tenkazai1_zairyoumei;
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
     * @return the tenkazai2_zairyoumei
     */
    public FXHDD01 getTenkazai2_zairyoumei() {
        return tenkazai2_zairyoumei;
    }

    /**
     * 添加材②_材料品名
     * @param tenkazai2_zairyoumei the tenkazai2_zairyoumei to set
     */
    public void setTenkazai2_zairyoumei(FXHDD01 tenkazai2_zairyoumei) {
        this.tenkazai2_zairyoumei = tenkazai2_zairyoumei;
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
     * @return the tenkazai3_zairyoumei
     */
    public FXHDD01 getTenkazai3_zairyoumei() {
        return tenkazai3_zairyoumei;
    }

    /**
     * 添加材③_材料品名
     * @param tenkazai3_zairyoumei the tenkazai3_zairyoumei to set
     */
    public void setTenkazai3_zairyoumei(FXHDD01 tenkazai3_zairyoumei) {
        this.tenkazai3_zairyoumei = tenkazai3_zairyoumei;
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
     * @return the tenkazai4_zairyoumei
     */
    public FXHDD01 getTenkazai4_zairyoumei() {
        return tenkazai4_zairyoumei;
    }

    /**
     * 添加材④_材料品名
     * @param tenkazai4_zairyoumei the tenkazai4_zairyoumei to set
     */
    public void setTenkazai4_zairyoumei(FXHDD01 tenkazai4_zairyoumei) {
        this.tenkazai4_zairyoumei = tenkazai4_zairyoumei;
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
     * @return the tenkazai5_zairyoumei
     */
    public FXHDD01 getTenkazai5_zairyoumei() {
        return tenkazai5_zairyoumei;
    }

    /**
     * 添加材⑤_材料品名
     * @param tenkazai5_zairyoumei the tenkazai5_zairyoumei to set
     */
    public void setTenkazai5_zairyoumei(FXHDD01 tenkazai5_zairyoumei) {
        this.tenkazai5_zairyoumei = tenkazai5_zairyoumei;
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
     * 添加材⑥_材料品名
     * @return the tenkazai6_zairyoumei
     */
    public FXHDD01 getTenkazai6_zairyoumei() {
        return tenkazai6_zairyoumei;
    }

    /**
     * 添加材⑥_材料品名
     * @param tenkazai6_zairyoumei the tenkazai6_zairyoumei to set
     */
    public void setTenkazai6_zairyoumei(FXHDD01 tenkazai6_zairyoumei) {
        this.tenkazai6_zairyoumei = tenkazai6_zairyoumei;
    }

    /**
     * 添加材⑥_調合量規格
     * @return the tenkazai6_tyougouryoukikaku
     */
    public FXHDD01 getTenkazai6_tyougouryoukikaku() {
        return tenkazai6_tyougouryoukikaku;
    }

    /**
     * 添加材⑥_調合量規格
     * @param tenkazai6_tyougouryoukikaku the tenkazai6_tyougouryoukikaku to set
     */
    public void setTenkazai6_tyougouryoukikaku(FXHDD01 tenkazai6_tyougouryoukikaku) {
        this.tenkazai6_tyougouryoukikaku = tenkazai6_tyougouryoukikaku;
    }

    /**
     * 添加材⑥_部材在庫No1
     * @return the tenkazai6_buzaizaikolotno1
     */
    public FXHDD01 getTenkazai6_buzaizaikolotno1() {
        return tenkazai6_buzaizaikolotno1;
    }

    /**
     * 添加材⑥_部材在庫No1
     * @param tenkazai6_buzaizaikolotno1 the tenkazai6_buzaizaikolotno1 to set
     */
    public void setTenkazai6_buzaizaikolotno1(FXHDD01 tenkazai6_buzaizaikolotno1) {
        this.tenkazai6_buzaizaikolotno1 = tenkazai6_buzaizaikolotno1;
    }

    /**
     * 添加材⑥_調合量1
     * @return the tenkazai6_tyougouryou1
     */
    public FXHDD01 getTenkazai6_tyougouryou1() {
        return tenkazai6_tyougouryou1;
    }

    /**
     * 添加材⑥_調合量1
     * @param tenkazai6_tyougouryou1 the tenkazai6_tyougouryou1 to set
     */
    public void setTenkazai6_tyougouryou1(FXHDD01 tenkazai6_tyougouryou1) {
        this.tenkazai6_tyougouryou1 = tenkazai6_tyougouryou1;
    }

    /**
     * 添加材⑥_部材在庫No2
     * @return the tenkazai6_buzaizaikolotno2
     */
    public FXHDD01 getTenkazai6_buzaizaikolotno2() {
        return tenkazai6_buzaizaikolotno2;
    }

    /**
     * 添加材⑥_部材在庫No2
     * @param tenkazai6_buzaizaikolotno2 the tenkazai6_buzaizaikolotno2 to set
     */
    public void setTenkazai6_buzaizaikolotno2(FXHDD01 tenkazai6_buzaizaikolotno2) {
        this.tenkazai6_buzaizaikolotno2 = tenkazai6_buzaizaikolotno2;
    }

    /**
     * 添加材⑥_調合量2
     * @return the tenkazai6_tyougouryou2
     */
    public FXHDD01 getTenkazai6_tyougouryou2() {
        return tenkazai6_tyougouryou2;
    }

    /**
     * 添加材⑥_調合量2
     * @param tenkazai6_tyougouryou2 the tenkazai6_tyougouryou2 to set
     */
    public void setTenkazai6_tyougouryou2(FXHDD01 tenkazai6_tyougouryou2) {
        this.tenkazai6_tyougouryou2 = tenkazai6_tyougouryou2;
    }

    /**
     * 添加材⑦_材料品名
     * @return the tenkazai7_zairyoumei
     */
    public FXHDD01 getTenkazai7_zairyoumei() {
        return tenkazai7_zairyoumei;
    }

    /**
     * 添加材⑦_材料品名
     * @param tenkazai7_zairyoumei the tenkazai7_zairyoumei to set
     */
    public void setTenkazai7_zairyoumei(FXHDD01 tenkazai7_zairyoumei) {
        this.tenkazai7_zairyoumei = tenkazai7_zairyoumei;
    }

    /**
     * 添加材⑦_調合量規格
     * @return the tenkazai7_tyougouryoukikaku
     */
    public FXHDD01 getTenkazai7_tyougouryoukikaku() {
        return tenkazai7_tyougouryoukikaku;
    }

    /**
     * 添加材⑦_調合量規格
     * @param tenkazai7_tyougouryoukikaku the tenkazai7_tyougouryoukikaku to set
     */
    public void setTenkazai7_tyougouryoukikaku(FXHDD01 tenkazai7_tyougouryoukikaku) {
        this.tenkazai7_tyougouryoukikaku = tenkazai7_tyougouryoukikaku;
    }

    /**
     * 添加材⑦_部材在庫No1
     * @return the tenkazai7_buzaizaikolotno1
     */
    public FXHDD01 getTenkazai7_buzaizaikolotno1() {
        return tenkazai7_buzaizaikolotno1;
    }

    /**
     * 添加材⑦_部材在庫No1
     * @param tenkazai7_buzaizaikolotno1 the tenkazai7_buzaizaikolotno1 to set
     */
    public void setTenkazai7_buzaizaikolotno1(FXHDD01 tenkazai7_buzaizaikolotno1) {
        this.tenkazai7_buzaizaikolotno1 = tenkazai7_buzaizaikolotno1;
    }

    /**
     * 添加材⑦_調合量1
     * @return the tenkazai7_tyougouryou1
     */
    public FXHDD01 getTenkazai7_tyougouryou1() {
        return tenkazai7_tyougouryou1;
    }

    /**
     * 添加材⑦_調合量1
     * @param tenkazai7_tyougouryou1 the tenkazai7_tyougouryou1 to set
     */
    public void setTenkazai7_tyougouryou1(FXHDD01 tenkazai7_tyougouryou1) {
        this.tenkazai7_tyougouryou1 = tenkazai7_tyougouryou1;
    }

    /**
     * 添加材⑦_部材在庫No2
     * @return the tenkazai7_buzaizaikolotno2
     */
    public FXHDD01 getTenkazai7_buzaizaikolotno2() {
        return tenkazai7_buzaizaikolotno2;
    }

    /**
     * 添加材⑦_部材在庫No2
     * @param tenkazai7_buzaizaikolotno2 the tenkazai7_buzaizaikolotno2 to set
     */
    public void setTenkazai7_buzaizaikolotno2(FXHDD01 tenkazai7_buzaizaikolotno2) {
        this.tenkazai7_buzaizaikolotno2 = tenkazai7_buzaizaikolotno2;
    }

    /**
     * 添加材⑦_調合量2
     * @return the tenkazai7_tyougouryou2
     */
    public FXHDD01 getTenkazai7_tyougouryou2() {
        return tenkazai7_tyougouryou2;
    }

    /**
     * 添加材⑦_調合量2
     * @param tenkazai7_tyougouryou2 the tenkazai7_tyougouryou2 to set
     */
    public void setTenkazai7_tyougouryou2(FXHDD01 tenkazai7_tyougouryou2) {
        this.tenkazai7_tyougouryou2 = tenkazai7_tyougouryou2;
    }

    /**
     * 添加材⑧_材料品名
     * @return the tenkazai8_zairyoumei
     */
    public FXHDD01 getTenkazai8_zairyoumei() {
        return tenkazai8_zairyoumei;
    }

    /**
     * 添加材⑧_材料品名
     * @param tenkazai8_zairyoumei the tenkazai8_zairyoumei to set
     */
    public void setTenkazai8_zairyoumei(FXHDD01 tenkazai8_zairyoumei) {
        this.tenkazai8_zairyoumei = tenkazai8_zairyoumei;
    }

    /**
     * 添加材⑧_調合量規格
     * @return the tenkazai8_tyougouryoukikaku
     */
    public FXHDD01 getTenkazai8_tyougouryoukikaku() {
        return tenkazai8_tyougouryoukikaku;
    }

    /**
     * 添加材⑧_調合量規格
     * @param tenkazai8_tyougouryoukikaku the tenkazai8_tyougouryoukikaku to set
     */
    public void setTenkazai8_tyougouryoukikaku(FXHDD01 tenkazai8_tyougouryoukikaku) {
        this.tenkazai8_tyougouryoukikaku = tenkazai8_tyougouryoukikaku;
    }

    /**
     * 添加材⑧_部材在庫No1
     * @return the tenkazai8_buzaizaikolotno1
     */
    public FXHDD01 getTenkazai8_buzaizaikolotno1() {
        return tenkazai8_buzaizaikolotno1;
    }

    /**
     * 添加材⑧_部材在庫No1
     * @param tenkazai8_buzaizaikolotno1 the tenkazai8_buzaizaikolotno1 to set
     */
    public void setTenkazai8_buzaizaikolotno1(FXHDD01 tenkazai8_buzaizaikolotno1) {
        this.tenkazai8_buzaizaikolotno1 = tenkazai8_buzaizaikolotno1;
    }

    /**
     * 添加材⑧_調合量1
     * @return the tenkazai8_tyougouryou1
     */
    public FXHDD01 getTenkazai8_tyougouryou1() {
        return tenkazai8_tyougouryou1;
    }

    /**
     * 添加材⑧_調合量1
     * @param tenkazai8_tyougouryou1 the tenkazai8_tyougouryou1 to set
     */
    public void setTenkazai8_tyougouryou1(FXHDD01 tenkazai8_tyougouryou1) {
        this.tenkazai8_tyougouryou1 = tenkazai8_tyougouryou1;
    }

    /**
     * 添加材⑧_部材在庫No2
     * @return the tenkazai8_buzaizaikolotno2
     */
    public FXHDD01 getTenkazai8_buzaizaikolotno2() {
        return tenkazai8_buzaizaikolotno2;
    }

    /**
     * 添加材⑧_部材在庫No2
     * @param tenkazai8_buzaizaikolotno2 the tenkazai8_buzaizaikolotno2 to set
     */
    public void setTenkazai8_buzaizaikolotno2(FXHDD01 tenkazai8_buzaizaikolotno2) {
        this.tenkazai8_buzaizaikolotno2 = tenkazai8_buzaizaikolotno2;
    }

    /**
     * 添加材⑧_調合量2
     * @return the tenkazai8_tyougouryou2
     */
    public FXHDD01 getTenkazai8_tyougouryou2() {
        return tenkazai8_tyougouryou2;
    }

    /**
     * 添加材⑧_調合量2
     * @param tenkazai8_tyougouryou2 the tenkazai8_tyougouryou2 to set
     */
    public void setTenkazai8_tyougouryou2(FXHDD01 tenkazai8_tyougouryou2) {
        this.tenkazai8_tyougouryou2 = tenkazai8_tyougouryou2;
    }

    /**
     * 添加材⑨_材料品名
     * @return the tenkazai9_zairyoumei
     */
    public FXHDD01 getTenkazai9_zairyoumei() {
        return tenkazai9_zairyoumei;
    }

    /**
     * 添加材⑨_材料品名
     * @param tenkazai9_zairyoumei the tenkazai9_zairyoumei to set
     */
    public void setTenkazai9_zairyoumei(FXHDD01 tenkazai9_zairyoumei) {
        this.tenkazai9_zairyoumei = tenkazai9_zairyoumei;
    }

    /**
     * 添加材⑨_調合量規格
     * @return the tenkazai9_tyougouryoukikaku
     */
    public FXHDD01 getTenkazai9_tyougouryoukikaku() {
        return tenkazai9_tyougouryoukikaku;
    }

    /**
     * 添加材⑨_調合量規格
     * @param tenkazai9_tyougouryoukikaku the tenkazai9_tyougouryoukikaku to set
     */
    public void setTenkazai9_tyougouryoukikaku(FXHDD01 tenkazai9_tyougouryoukikaku) {
        this.tenkazai9_tyougouryoukikaku = tenkazai9_tyougouryoukikaku;
    }

    /**
     * 添加材⑨_部材在庫No1
     * @return the tenkazai9_buzaizaikolotno1
     */
    public FXHDD01 getTenkazai9_buzaizaikolotno1() {
        return tenkazai9_buzaizaikolotno1;
    }

    /**
     * 添加材⑨_部材在庫No1
     * @param tenkazai9_buzaizaikolotno1 the tenkazai9_buzaizaikolotno1 to set
     */
    public void setTenkazai9_buzaizaikolotno1(FXHDD01 tenkazai9_buzaizaikolotno1) {
        this.tenkazai9_buzaizaikolotno1 = tenkazai9_buzaizaikolotno1;
    }

    /**
     * 添加材⑨_調合量1
     * @return the tenkazai9_tyougouryou1
     */
    public FXHDD01 getTenkazai9_tyougouryou1() {
        return tenkazai9_tyougouryou1;
    }

    /**
     * 添加材⑨_調合量1
     * @param tenkazai9_tyougouryou1 the tenkazai9_tyougouryou1 to set
     */
    public void setTenkazai9_tyougouryou1(FXHDD01 tenkazai9_tyougouryou1) {
        this.tenkazai9_tyougouryou1 = tenkazai9_tyougouryou1;
    }

    /**
     * 添加材⑨_部材在庫No2
     * @return the tenkazai9_buzaizaikolotno2
     */
    public FXHDD01 getTenkazai9_buzaizaikolotno2() {
        return tenkazai9_buzaizaikolotno2;
    }

    /**
     * 添加材⑨_部材在庫No2
     * @param tenkazai9_buzaizaikolotno2 the tenkazai9_buzaizaikolotno2 to set
     */
    public void setTenkazai9_buzaizaikolotno2(FXHDD01 tenkazai9_buzaizaikolotno2) {
        this.tenkazai9_buzaizaikolotno2 = tenkazai9_buzaizaikolotno2;
    }

    /**
     * 添加材⑨_調合量2
     * @return the tenkazai9_tyougouryou2
     */
    public FXHDD01 getTenkazai9_tyougouryou2() {
        return tenkazai9_tyougouryou2;
    }

    /**
     * 添加材⑨_調合量2
     * @param tenkazai9_tyougouryou2 the tenkazai9_tyougouryou2 to set
     */
    public void setTenkazai9_tyougouryou2(FXHDD01 tenkazai9_tyougouryou2) {
        this.tenkazai9_tyougouryou2 = tenkazai9_tyougouryou2;
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