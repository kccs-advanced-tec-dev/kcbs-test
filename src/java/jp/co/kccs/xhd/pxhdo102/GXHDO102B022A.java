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
 * 変更日	2021/11/12<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B022(誘電体ｽﾗﾘｰ作製・主原料秤量)
 *
 * @author KCSS K.Jo
 * @since  2021/11/12
 */
@ViewScoped
@Named
public class GXHDO102B022A implements Serializable {
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
     * 主原料秤量開始日
     */
    private FXHDD01 sgrhyouryoukaisi_day;

    /**
     * 主原料秤量開始時間
     */
    private FXHDD01 sgrhyouryoukaisi_time;

    /**
     * 主原料①_材料品名
     */
    private FXHDD01 sgr1_zairyouhinmei;

    /**
     * 主原料①_調合量規格
     */
    private FXHDD01 sgr1_tyougouryoukikaku;

    /**
     * 主原料①_部材在庫No
     */
    private FXHDD01 sgr1_buzaizaikolotno;

    /**
     * 主原料①_調合量1
     */
    private FXHDD01 sgr1_tyougouryou1;

    /**
     * 主原料①_調合量2
     */
    private FXHDD01 sgr1_tyougouryou2;

    /**
     * 主原料①_調合量3
     */
    private FXHDD01 sgr1_tyougouryou3;

    /**
     * 主原料①_調合量4
     */
    private FXHDD01 sgr1_tyougouryou4;

    /**
     * 主原料①_調合量5
     */
    private FXHDD01 sgr1_tyougouryou5;

    /**
     * 主原料①_調合量6
     */
    private FXHDD01 sgr1_tyougouryou6;

    /**
     * 主原料①_調合量7
     */
    private FXHDD01 sgr1_tyougouryou7;

    /**
     * 主原料②_材料品名
     */
    private FXHDD01 sgr2_zairyouhinmei;

    /**
     * 主原料②_調合量規格
     */
    private FXHDD01 sgr2_tyougouryoukikaku;

    /**
     * 主原料②_部材在庫No
     */
    private FXHDD01 sgr2_buzaizaikolotno;

    /**
     * 主原料②_調合量1
     */
    private FXHDD01 sgr2_tyougouryou1;

    /**
     * 主原料②_調合量2
     */
    private FXHDD01 sgr2_tyougouryou2;

    /**
     * 主原料②_調合量3
     */
    private FXHDD01 sgr2_tyougouryou3;

    /**
     * 主原料③_材料品名
     */
    private FXHDD01 sgr3_zairyouhinmei;

    /**
     * 主原料③_調合量規格
     */
    private FXHDD01 sgr3_tyougouryoukikaku;

    /**
     * 主原料③_部材在庫No
     */
    private FXHDD01 sgr3_buzaizaikolotno;

    /**
     * 主原料③_調合量1
     */
    private FXHDD01 sgr3_tyougouryou1;

    /**
     * 主原料③_調合量2
     */
    private FXHDD01 sgr3_tyougouryou2;

    /**
     * 主原料③_調合量3
     */
    private FXHDD01 sgr3_tyougouryou3;

    /**
     * 主原料④_材料品名
     */
    private FXHDD01 sgr4_zairyouhinmei;

    /**
     * 主原料④_調合量規格
     */
    private FXHDD01 sgr4_tyougouryoukikaku;

    /**
     * 主原料④_部材在庫No
     */
    private FXHDD01 sgr4_buzaizaikolotno;

    /**
     * 主原料④_調合量1
     */
    private FXHDD01 sgr4_tyougouryou1;

    /**
     * 主原料④_調合量2
     */
    private FXHDD01 sgr4_tyougouryou2;

    /**
     * 主原料⑤_材料品名
     */
    private FXHDD01 sgr5_zairyouhinmei;

    /**
     * 主原料⑤_調合量規格
     */
    private FXHDD01 sgr5_tyougouryoukikaku;

    /**
     * 主原料⑤_部材在庫No
     */
    private FXHDD01 sgr5_buzaizaikolotno;

    /**
     * 主原料⑤_調合量1
     */
    private FXHDD01 sgr5_tyougouryou1;

    /**
     * 主原料⑤_調合量2
     */
    private FXHDD01 sgr5_tyougouryou2;

    /**
     * 主原料⑥_材料品名
     */
    private FXHDD01 sgr6_zairyouhinmei;

    /**
     * 主原料⑥_調合量規格
     */
    private FXHDD01 sgr6_tyougouryoukikaku;

    /**
     * 主原料⑥_部材在庫No
     */
    private FXHDD01 sgr6_buzaizaikolotno;

    /**
     * 主原料⑥_調合量1
     */
    private FXHDD01 sgr6_tyougouryou1;

    /**
     * 調合量合計
     */
    private FXHDD01 tyougouryougoukei;

    /**
     * 主原料秤量終了日
     */
    private FXHDD01 sgrhyouryousyuuryou_day;

    /**
     * 主原料秤量終了時間
     */
    private FXHDD01 sgrhyouryousyuuryou_time;

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
     * 主原料秤量開始日
     * @return the sgrhyouryoukaisi_day
     */
    public FXHDD01 getSgrhyouryoukaisi_day() {
        return sgrhyouryoukaisi_day;
    }

    /**
     * 主原料秤量開始日
     * @param sgrhyouryoukaisi_day the sgrhyouryoukaisi_day to set
     */
    public void setSgrhyouryoukaisi_day(FXHDD01 sgrhyouryoukaisi_day) {
        this.sgrhyouryoukaisi_day = sgrhyouryoukaisi_day;
    }

    /**
     * 主原料秤量開始時間
     * @return the sgrhyouryoukaisi_time
     */
    public FXHDD01 getSgrhyouryoukaisi_time() {
        return sgrhyouryoukaisi_time;
    }

    /**
     * 主原料秤量開始時間
     * @param sgrhyouryoukaisi_time the sgrhyouryoukaisi_time to set
     */
    public void setSgrhyouryoukaisi_time(FXHDD01 sgrhyouryoukaisi_time) {
        this.sgrhyouryoukaisi_time = sgrhyouryoukaisi_time;
    }

    /**
     * 主原料①_材料品名
     * @return the sgr1_zairyouhinmei
     */
    public FXHDD01 getSgr1_zairyouhinmei() {
        return sgr1_zairyouhinmei;
    }

    /**
     * 主原料①_材料品名
     * @param sgr1_zairyouhinmei the sgr1_zairyouhinmei to set
     */
    public void setSgr1_zairyouhinmei(FXHDD01 sgr1_zairyouhinmei) {
        this.sgr1_zairyouhinmei = sgr1_zairyouhinmei;
    }

    /**
     * 主原料①_調合量規格
     * @return the sgr1_tyougouryoukikaku
     */
    public FXHDD01 getSgr1_tyougouryoukikaku() {
        return sgr1_tyougouryoukikaku;
    }

    /**
     * 主原料①_調合量規格
     * @param sgr1_tyougouryoukikaku the sgr1_tyougouryoukikaku to set
     */
    public void setSgr1_tyougouryoukikaku(FXHDD01 sgr1_tyougouryoukikaku) {
        this.sgr1_tyougouryoukikaku = sgr1_tyougouryoukikaku;
    }

    /**
     * 主原料①_部材在庫No
     * @return the sgr1_buzaizaikolotno
     */
    public FXHDD01 getSgr1_buzaizaikolotno() {
        return sgr1_buzaizaikolotno;
    }

    /**
     * 主原料①_部材在庫No
     * @param sgr1_buzaizaikolotno the sgr1_buzaizaikolotno to set
     */
    public void setSgr1_buzaizaikolotno(FXHDD01 sgr1_buzaizaikolotno) {
        this.sgr1_buzaizaikolotno = sgr1_buzaizaikolotno;
    }

    /**
     * 主原料①_調合量1
     * @return the sgr1_tyougouryou1
     */
    public FXHDD01 getSgr1_tyougouryou1() {
        return sgr1_tyougouryou1;
    }

    /**
     * 主原料①_調合量1
     * @param sgr1_tyougouryou1 the sgr1_tyougouryou1 to set
     */
    public void setSgr1_tyougouryou1(FXHDD01 sgr1_tyougouryou1) {
        this.sgr1_tyougouryou1 = sgr1_tyougouryou1;
    }

    /**
     * 主原料①_調合量2
     * @return the sgr1_tyougouryou2
     */
    public FXHDD01 getSgr1_tyougouryou2() {
        return sgr1_tyougouryou2;
    }

    /**
     * 主原料①_調合量2
     * @param sgr1_tyougouryou2 the sgr1_tyougouryou2 to set
     */
    public void setSgr1_tyougouryou2(FXHDD01 sgr1_tyougouryou2) {
        this.sgr1_tyougouryou2 = sgr1_tyougouryou2;
    }

    /**
     * 主原料①_調合量3
     * @return the sgr1_tyougouryou3
     */
    public FXHDD01 getSgr1_tyougouryou3() {
        return sgr1_tyougouryou3;
    }

    /**
     * 主原料①_調合量3
     * @param sgr1_tyougouryou3 the sgr1_tyougouryou3 to set
     */
    public void setSgr1_tyougouryou3(FXHDD01 sgr1_tyougouryou3) {
        this.sgr1_tyougouryou3 = sgr1_tyougouryou3;
    }

    /**
     * 主原料①_調合量4
     * @return the sgr1_tyougouryou4
     */
    public FXHDD01 getSgr1_tyougouryou4() {
        return sgr1_tyougouryou4;
    }

    /**
     * 主原料①_調合量4
     * @param sgr1_tyougouryou4 the sgr1_tyougouryou4 to set
     */
    public void setSgr1_tyougouryou4(FXHDD01 sgr1_tyougouryou4) {
        this.sgr1_tyougouryou4 = sgr1_tyougouryou4;
    }

    /**
     * 主原料①_調合量5
     * @return the sgr1_tyougouryou5
     */
    public FXHDD01 getSgr1_tyougouryou5() {
        return sgr1_tyougouryou5;
    }

    /**
     * 主原料①_調合量5
     * @param sgr1_tyougouryou5 the sgr1_tyougouryou5 to set
     */
    public void setSgr1_tyougouryou5(FXHDD01 sgr1_tyougouryou5) {
        this.sgr1_tyougouryou5 = sgr1_tyougouryou5;
    }

    /**
     * 主原料①_調合量6
     * @return the sgr1_tyougouryou6
     */
    public FXHDD01 getSgr1_tyougouryou6() {
        return sgr1_tyougouryou6;
    }

    /**
     * 主原料①_調合量6
     * @param sgr1_tyougouryou6 the sgr1_tyougouryou6 to set
     */
    public void setSgr1_tyougouryou6(FXHDD01 sgr1_tyougouryou6) {
        this.sgr1_tyougouryou6 = sgr1_tyougouryou6;
    }

    /**
     * 主原料①_調合量7
     * @return the sgr1_tyougouryou7
     */
    public FXHDD01 getSgr1_tyougouryou7() {
        return sgr1_tyougouryou7;
    }

    /**
     * 主原料①_調合量7
     * @param sgr1_tyougouryou7 the sgr1_tyougouryou7 to set
     */
    public void setSgr1_tyougouryou7(FXHDD01 sgr1_tyougouryou7) {
        this.sgr1_tyougouryou7 = sgr1_tyougouryou7;
    }

    /**
     * 主原料②_材料品名
     * @return the sgr2_zairyouhinmei
     */
    public FXHDD01 getSgr2_zairyouhinmei() {
        return sgr2_zairyouhinmei;
    }

    /**
     * 主原料②_材料品名
     * @param sgr2_zairyouhinmei the sgr2_zairyouhinmei to set
     */
    public void setSgr2_zairyouhinmei(FXHDD01 sgr2_zairyouhinmei) {
        this.sgr2_zairyouhinmei = sgr2_zairyouhinmei;
    }

    /**
     * 主原料②_調合量規格
     * @return the sgr2_tyougouryoukikaku
     */
    public FXHDD01 getSgr2_tyougouryoukikaku() {
        return sgr2_tyougouryoukikaku;
    }

    /**
     * 主原料②_調合量規格
     * @param sgr2_tyougouryoukikaku the sgr2_tyougouryoukikaku to set
     */
    public void setSgr2_tyougouryoukikaku(FXHDD01 sgr2_tyougouryoukikaku) {
        this.sgr2_tyougouryoukikaku = sgr2_tyougouryoukikaku;
    }

    /**
     * 主原料②_部材在庫No
     * @return the sgr2_buzaizaikolotno
     */
    public FXHDD01 getSgr2_buzaizaikolotno() {
        return sgr2_buzaizaikolotno;
    }

    /**
     * 主原料②_部材在庫No
     * @param sgr2_buzaizaikolotno the sgr2_buzaizaikolotno to set
     */
    public void setSgr2_buzaizaikolotno(FXHDD01 sgr2_buzaizaikolotno) {
        this.sgr2_buzaizaikolotno = sgr2_buzaizaikolotno;
    }

    /**
     * 主原料②_調合量1
     * @return the sgr2_tyougouryou1
     */
    public FXHDD01 getSgr2_tyougouryou1() {
        return sgr2_tyougouryou1;
    }

    /**
     * 主原料②_調合量1
     * @param sgr2_tyougouryou1 the sgr2_tyougouryou1 to set
     */
    public void setSgr2_tyougouryou1(FXHDD01 sgr2_tyougouryou1) {
        this.sgr2_tyougouryou1 = sgr2_tyougouryou1;
    }

    /**
     * 主原料②_調合量2
     * @return the sgr2_tyougouryou2
     */
    public FXHDD01 getSgr2_tyougouryou2() {
        return sgr2_tyougouryou2;
    }

    /**
     * 主原料②_調合量2
     * @param sgr2_tyougouryou2 the sgr2_tyougouryou2 to set
     */
    public void setSgr2_tyougouryou2(FXHDD01 sgr2_tyougouryou2) {
        this.sgr2_tyougouryou2 = sgr2_tyougouryou2;
    }

    /**
     * 主原料②_調合量3
     * @return the sgr2_tyougouryou3
     */
    public FXHDD01 getSgr2_tyougouryou3() {
        return sgr2_tyougouryou3;
    }

    /**
     * 主原料②_調合量3
     * @param sgr2_tyougouryou3 the sgr2_tyougouryou3 to set
     */
    public void setSgr2_tyougouryou3(FXHDD01 sgr2_tyougouryou3) {
        this.sgr2_tyougouryou3 = sgr2_tyougouryou3;
    }

    /**
     * 主原料③_材料品名
     * @return the sgr3_zairyouhinmei
     */
    public FXHDD01 getSgr3_zairyouhinmei() {
        return sgr3_zairyouhinmei;
    }

    /**
     * 主原料③_材料品名
     * @param sgr3_zairyouhinmei the sgr3_zairyouhinmei to set
     */
    public void setSgr3_zairyouhinmei(FXHDD01 sgr3_zairyouhinmei) {
        this.sgr3_zairyouhinmei = sgr3_zairyouhinmei;
    }

    /**
     * 主原料③_調合量規格
     * @return the sgr3_tyougouryoukikaku
     */
    public FXHDD01 getSgr3_tyougouryoukikaku() {
        return sgr3_tyougouryoukikaku;
    }

    /**
     * 主原料③_調合量規格
     * @param sgr3_tyougouryoukikaku the sgr3_tyougouryoukikaku to set
     */
    public void setSgr3_tyougouryoukikaku(FXHDD01 sgr3_tyougouryoukikaku) {
        this.sgr3_tyougouryoukikaku = sgr3_tyougouryoukikaku;
    }

    /**
     * 主原料③_部材在庫No
     * @return the sgr3_buzaizaikolotno
     */
    public FXHDD01 getSgr3_buzaizaikolotno() {
        return sgr3_buzaizaikolotno;
    }

    /**
     * 主原料③_部材在庫No
     * @param sgr3_buzaizaikolotno the sgr3_buzaizaikolotno to set
     */
    public void setSgr3_buzaizaikolotno(FXHDD01 sgr3_buzaizaikolotno) {
        this.sgr3_buzaizaikolotno = sgr3_buzaizaikolotno;
    }

    /**
     * 主原料③_調合量1
     * @return the sgr3_tyougouryou1
     */
    public FXHDD01 getSgr3_tyougouryou1() {
        return sgr3_tyougouryou1;
    }

    /**
     * 主原料③_調合量1
     * @param sgr3_tyougouryou1 the sgr3_tyougouryou1 to set
     */
    public void setSgr3_tyougouryou1(FXHDD01 sgr3_tyougouryou1) {
        this.sgr3_tyougouryou1 = sgr3_tyougouryou1;
    }

    /**
     * 主原料③_調合量2
     * @return the sgr3_tyougouryou2
     */
    public FXHDD01 getSgr3_tyougouryou2() {
        return sgr3_tyougouryou2;
    }

    /**
     * 主原料③_調合量2
     * @param sgr3_tyougouryou2 the sgr3_tyougouryou2 to set
     */
    public void setSgr3_tyougouryou2(FXHDD01 sgr3_tyougouryou2) {
        this.sgr3_tyougouryou2 = sgr3_tyougouryou2;
    }

    /**
     * 主原料③_調合量3
     * @return the sgr3_tyougouryou3
     */
    public FXHDD01 getSgr3_tyougouryou3() {
        return sgr3_tyougouryou3;
    }

    /**
     * 主原料③_調合量3
     * @param sgr3_tyougouryou3 the sgr3_tyougouryou3 to set
     */
    public void setSgr3_tyougouryou3(FXHDD01 sgr3_tyougouryou3) {
        this.sgr3_tyougouryou3 = sgr3_tyougouryou3;
    }

    /**
     * 主原料④_材料品名
     * @return the sgr4_zairyouhinmei
     */
    public FXHDD01 getSgr4_zairyouhinmei() {
        return sgr4_zairyouhinmei;
    }

    /**
     * 主原料④_材料品名
     * @param sgr4_zairyouhinmei the sgr4_zairyouhinmei to set
     */
    public void setSgr4_zairyouhinmei(FXHDD01 sgr4_zairyouhinmei) {
        this.sgr4_zairyouhinmei = sgr4_zairyouhinmei;
    }

    /**
     * 主原料④_調合量規格
     * @return the sgr4_tyougouryoukikaku
     */
    public FXHDD01 getSgr4_tyougouryoukikaku() {
        return sgr4_tyougouryoukikaku;
    }

    /**
     * 主原料④_調合量規格
     * @param sgr4_tyougouryoukikaku the sgr4_tyougouryoukikaku to set
     */
    public void setSgr4_tyougouryoukikaku(FXHDD01 sgr4_tyougouryoukikaku) {
        this.sgr4_tyougouryoukikaku = sgr4_tyougouryoukikaku;
    }

    /**
     * 主原料④_部材在庫No
     * @return the sgr4_buzaizaikolotno
     */
    public FXHDD01 getSgr4_buzaizaikolotno() {
        return sgr4_buzaizaikolotno;
    }

    /**
     * 主原料④_部材在庫No
     * @param sgr4_buzaizaikolotno the sgr4_buzaizaikolotno to set
     */
    public void setSgr4_buzaizaikolotno(FXHDD01 sgr4_buzaizaikolotno) {
        this.sgr4_buzaizaikolotno = sgr4_buzaizaikolotno;
    }

    /**
     * 主原料④_調合量1
     * @return the sgr4_tyougouryou1
     */
    public FXHDD01 getSgr4_tyougouryou1() {
        return sgr4_tyougouryou1;
    }

    /**
     * 主原料④_調合量1
     * @param sgr4_tyougouryou1 the sgr4_tyougouryou1 to set
     */
    public void setSgr4_tyougouryou1(FXHDD01 sgr4_tyougouryou1) {
        this.sgr4_tyougouryou1 = sgr4_tyougouryou1;
    }

    /**
     * 主原料④_調合量2
     * @return the sgr4_tyougouryou2
     */
    public FXHDD01 getSgr4_tyougouryou2() {
        return sgr4_tyougouryou2;
    }

    /**
     * 主原料④_調合量2
     * @param sgr4_tyougouryou2 the sgr4_tyougouryou2 to set
     */
    public void setSgr4_tyougouryou2(FXHDD01 sgr4_tyougouryou2) {
        this.sgr4_tyougouryou2 = sgr4_tyougouryou2;
    }

    /**
     * 主原料⑤_材料品名
     * @return the sgr5_zairyouhinmei
     */
    public FXHDD01 getSgr5_zairyouhinmei() {
        return sgr5_zairyouhinmei;
    }

    /**
     * 主原料⑤_材料品名
     * @param sgr5_zairyouhinmei the sgr5_zairyouhinmei to set
     */
    public void setSgr5_zairyouhinmei(FXHDD01 sgr5_zairyouhinmei) {
        this.sgr5_zairyouhinmei = sgr5_zairyouhinmei;
    }

    /**
     * 主原料⑤_調合量規格
     * @return the sgr5_tyougouryoukikaku
     */
    public FXHDD01 getSgr5_tyougouryoukikaku() {
        return sgr5_tyougouryoukikaku;
    }

    /**
     * 主原料⑤_調合量規格
     * @param sgr5_tyougouryoukikaku the sgr5_tyougouryoukikaku to set
     */
    public void setSgr5_tyougouryoukikaku(FXHDD01 sgr5_tyougouryoukikaku) {
        this.sgr5_tyougouryoukikaku = sgr5_tyougouryoukikaku;
    }

    /**
     * 主原料⑤_部材在庫No
     * @return the sgr5_buzaizaikolotno
     */
    public FXHDD01 getSgr5_buzaizaikolotno() {
        return sgr5_buzaizaikolotno;
    }

    /**
     * 主原料⑤_部材在庫No
     * @param sgr5_buzaizaikolotno the sgr5_buzaizaikolotno to set
     */
    public void setSgr5_buzaizaikolotno(FXHDD01 sgr5_buzaizaikolotno) {
        this.sgr5_buzaizaikolotno = sgr5_buzaizaikolotno;
    }

    /**
     * 主原料⑤_調合量1
     * @return the sgr5_tyougouryou1
     */
    public FXHDD01 getSgr5_tyougouryou1() {
        return sgr5_tyougouryou1;
    }

    /**
     * 主原料⑤_調合量1
     * @param sgr5_tyougouryou1 the sgr5_tyougouryou1 to set
     */
    public void setSgr5_tyougouryou1(FXHDD01 sgr5_tyougouryou1) {
        this.sgr5_tyougouryou1 = sgr5_tyougouryou1;
    }

    /**
     * 主原料⑤_調合量2
     * @return the sgr5_tyougouryou2
     */
    public FXHDD01 getSgr5_tyougouryou2() {
        return sgr5_tyougouryou2;
    }

    /**
     * 主原料⑤_調合量2
     * @param sgr5_tyougouryou2 the sgr5_tyougouryou2 to set
     */
    public void setSgr5_tyougouryou2(FXHDD01 sgr5_tyougouryou2) {
        this.sgr5_tyougouryou2 = sgr5_tyougouryou2;
    }

    /**
     * 主原料⑥_材料品名
     * @return the sgr6_zairyouhinmei
     */
    public FXHDD01 getSgr6_zairyouhinmei() {
        return sgr6_zairyouhinmei;
    }

    /**
     * 主原料⑥_材料品名
     * @param sgr6_zairyouhinmei the sgr6_zairyouhinmei to set
     */
    public void setSgr6_zairyouhinmei(FXHDD01 sgr6_zairyouhinmei) {
        this.sgr6_zairyouhinmei = sgr6_zairyouhinmei;
    }

    /**
     * 主原料⑥_調合量規格
     * @return the sgr6_tyougouryoukikaku
     */
    public FXHDD01 getSgr6_tyougouryoukikaku() {
        return sgr6_tyougouryoukikaku;
    }

    /**
     * 主原料⑥_調合量規格
     * @param sgr6_tyougouryoukikaku the sgr6_tyougouryoukikaku to set
     */
    public void setSgr6_tyougouryoukikaku(FXHDD01 sgr6_tyougouryoukikaku) {
        this.sgr6_tyougouryoukikaku = sgr6_tyougouryoukikaku;
    }

    /**
     * 主原料⑥_部材在庫No
     * @return the sgr6_buzaizaikolotno
     */
    public FXHDD01 getSgr6_buzaizaikolotno() {
        return sgr6_buzaizaikolotno;
    }

    /**
     * 主原料⑥_部材在庫No
     * @param sgr6_buzaizaikolotno the sgr6_buzaizaikolotno to set
     */
    public void setSgr6_buzaizaikolotno(FXHDD01 sgr6_buzaizaikolotno) {
        this.sgr6_buzaizaikolotno = sgr6_buzaizaikolotno;
    }

    /**
     * 主原料⑥_調合量1
     * @return the sgr6_tyougouryou1
     */
    public FXHDD01 getSgr6_tyougouryou1() {
        return sgr6_tyougouryou1;
    }

    /**
     * 主原料⑥_調合量1
     * @param sgr6_tyougouryou1 the sgr6_tyougouryou1 to set
     */
    public void setSgr6_tyougouryou1(FXHDD01 sgr6_tyougouryou1) {
        this.sgr6_tyougouryou1 = sgr6_tyougouryou1;
    }

    /**
     * 調合量合計
     * @return the tyougouryougoukei
     */
    public FXHDD01 getTyougouryougoukei() {
        return tyougouryougoukei;
    }

    /**
     * 調合量合計
     * @param tyougouryougoukei the tyougouryougoukei to set
     */
    public void setTyougouryougoukei(FXHDD01 tyougouryougoukei) {
        this.tyougouryougoukei = tyougouryougoukei;
    }

    /**
     * 主原料秤量終了日
     * @return the sgrhyouryousyuuryou_day
     */
    public FXHDD01 getSgrhyouryousyuuryou_day() {
        return sgrhyouryousyuuryou_day;
    }

    /**
     * 主原料秤量終了日
     * @param sgrhyouryousyuuryou_day the sgrhyouryousyuuryou_day to set
     */
    public void setSgrhyouryousyuuryou_day(FXHDD01 sgrhyouryousyuuryou_day) {
        this.sgrhyouryousyuuryou_day = sgrhyouryousyuuryou_day;
    }

    /**
     * 主原料秤量終了時間
     * @return the sgrhyouryousyuuryou_time
     */
    public FXHDD01 getSgrhyouryousyuuryou_time() {
        return sgrhyouryousyuuryou_time;
    }

    /**
     * 主原料秤量終了時間
     * @param sgrhyouryousyuuryou_time the sgrhyouryousyuuryou_time to set
     */
    public void setSgrhyouryousyuuryou_time(FXHDD01 sgrhyouryousyuuryou_time) {
        this.sgrhyouryousyuuryou_time = sgrhyouryousyuuryou_time;
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