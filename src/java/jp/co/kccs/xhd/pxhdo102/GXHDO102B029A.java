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
 * 変更日	2021/12/09<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B029(ｽﾘｯﾌﾟ作製・溶剤秤量・投入(白ﾎﾟﾘ))
 *
 * @author KCSS K.Jo
 * @since  2021/12/09
 */
@ViewScoped
@Named
public class GXHDO102B029A implements Serializable {
    /**
     * WIPﾛｯﾄNo
     */
    private FXHDD01 wiplotno;

    /**
     * ｽﾘｯﾌﾟ品名
     */
    private FXHDD01 sliphinmei;

    /**
     * ｽﾘｯﾌﾟLotNo
     */
    private FXHDD01 sliplotno;

    /**
     * ﾛｯﾄ区分
     */
    private FXHDD01 lotkubun;

    /**
     * 原料記号
     */
    private FXHDD01 genryoukigou;

    /**
     * 秤量号機
     */
    private FXHDD01 goki;

    /**
     * 溶剤秤量日
     */
    private FXHDD01 youzaikeiryou_day;

    /**
     * 溶剤秤量時間
     */
    private FXHDD01 youzaikeiryou_time;

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
     * 担当者
     */
    private FXHDD01 tantousya;

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合設備
     */
    private FXHDD01 binderkongousetub;

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合号機
     */
    private FXHDD01 binderkongougoki;

    /**
     * 混合ﾀﾝｸ種類
     */
    private FXHDD01 kongoutanksyurui;

    /**
     * 混合ﾀﾝｸNo
     */
    private FXHDD01 kongoutankno;

    /**
     * ﾀﾝｸ内洗浄確認
     */
    private FXHDD01 tanknaisenjyoukakunin;

    /**
     * ﾀﾝｸ内内袋確認
     */
    private FXHDD01 tanknaiutibukurokakunin;

    /**
     * 撹拌羽根洗浄確認
     */
    private FXHDD01 kakuhanhanesenjyoukakunin;

    /**
     * 撹拌軸洗浄確認
     */
    private FXHDD01 kakuhanjikusenjyoukakunin;

    /**
     * 投入①
     */
    private FXHDD01 tounyuu1;

    /**
     * 投入②
     */
    private FXHDD01 tounyuu2;

    /**
     * 投入③
     */
    private FXHDD01 tounyuu3;

    /**
     * 投入④
     */
    private FXHDD01 tounyuu4;

    /**
     * 投入⑤
     */
    private FXHDD01 tounyuu5;

    /**
     * 投入⑥
     */
    private FXHDD01 tounyuu6;

    /**
     * ｽﾗﾘｰ投入確認者
     */
    private FXHDD01 slurrytounyuukakuninsya;

    /**
     * 投入⑦
     */
    private FXHDD01 tounyuu7;

    /**
     * 投入⑧
     */
    private FXHDD01 tounyuu8;

    /**
     * 溶剤投入確認者
     */
    private FXHDD01 youzaitounyuukakuninsya;

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
     * ｽﾘｯﾌﾟ品名
     * @return the sliphinmei
     */
    public FXHDD01 getSliphinmei() {
        return sliphinmei;
    }

    /**
     * ｽﾘｯﾌﾟ品名
     * @param sliphinmei the sliphinmei to set
     */
    public void setSliphinmei(FXHDD01 sliphinmei) {
        this.sliphinmei = sliphinmei;
    }

    /**
     * ｽﾘｯﾌﾟLotNo
     * @return the sliplotno
     */
    public FXHDD01 getSliplotno() {
        return sliplotno;
    }

    /**
     * ｽﾘｯﾌﾟLotNo
     * @param sliplotno the sliplotno to set
     */
    public void setSliplotno(FXHDD01 sliplotno) {
        this.sliplotno = sliplotno;
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
     * 溶剤秤量日
     * @return the youzaikeiryou_day
     */
    public FXHDD01 getYouzaikeiryou_day() {
        return youzaikeiryou_day;
    }

    /**
     * 溶剤秤量日
     * @param youzaikeiryou_day the youzaikeiryou_day to set
     */
    public void setYouzaikeiryou_day(FXHDD01 youzaikeiryou_day) {
        this.youzaikeiryou_day = youzaikeiryou_day;
    }

    /**
     * 溶剤秤量時間
     * @return the youzaikeiryou_time
     */
    public FXHDD01 getYouzaikeiryou_time() {
        return youzaikeiryou_time;
    }

    /**
     * 溶剤秤量時間
     * @param youzaikeiryou_time the youzaikeiryou_time to set
     */
    public void setYouzaikeiryou_time(FXHDD01 youzaikeiryou_time) {
        this.youzaikeiryou_time = youzaikeiryou_time;
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
     * ﾊﾞｲﾝﾀﾞｰ混合設備
     * @return the binderkongousetub
     */
    public FXHDD01 getBinderkongousetub() {
        return binderkongousetub;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合設備
     * @param binderkongousetub the binderkongousetub to set
     */
    public void setBinderkongousetub(FXHDD01 binderkongousetub) {
        this.binderkongousetub = binderkongousetub;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合号機
     * @return the binderkongougoki
     */
    public FXHDD01 getBinderkongougoki() {
        return binderkongougoki;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合号機
     * @param binderkongougoki the binderkongougoki to set
     */
    public void setBinderkongougoki(FXHDD01 binderkongougoki) {
        this.binderkongougoki = binderkongougoki;
    }

    /**
     * 混合ﾀﾝｸ種類
     * @return the kongoutanksyurui
     */
    public FXHDD01 getKongoutanksyurui() {
        return kongoutanksyurui;
    }

    /**
     * 混合ﾀﾝｸ種類
     * @param kongoutanksyurui the kongoutanksyurui to set
     */
    public void setKongoutanksyurui(FXHDD01 kongoutanksyurui) {
        this.kongoutanksyurui = kongoutanksyurui;
    }

    /**
     * 混合ﾀﾝｸNo
     * @return the kongoutankno
     */
    public FXHDD01 getKongoutankno() {
        return kongoutankno;
    }

    /**
     * 混合ﾀﾝｸNo
     * @param kongoutankno the kongoutankno to set
     */
    public void setKongoutankno(FXHDD01 kongoutankno) {
        this.kongoutankno = kongoutankno;
    }

    /**
     * ﾀﾝｸ内洗浄確認
     * @return the tanknaisenjyoukakunin
     */
    public FXHDD01 getTanknaisenjyoukakunin() {
        return tanknaisenjyoukakunin;
    }

    /**
     * ﾀﾝｸ内洗浄確認
     * @param tanknaisenjyoukakunin the tanknaisenjyoukakunin to set
     */
    public void setTanknaisenjyoukakunin(FXHDD01 tanknaisenjyoukakunin) {
        this.tanknaisenjyoukakunin = tanknaisenjyoukakunin;
    }

    /**
     * ﾀﾝｸ内内袋確認
     * @return the tanknaiutibukurokakunin
     */
    public FXHDD01 getTanknaiutibukurokakunin() {
        return tanknaiutibukurokakunin;
    }

    /**
     * ﾀﾝｸ内内袋確認
     * @param tanknaiutibukurokakunin the tanknaiutibukurokakunin to set
     */
    public void setTanknaiutibukurokakunin(FXHDD01 tanknaiutibukurokakunin) {
        this.tanknaiutibukurokakunin = tanknaiutibukurokakunin;
    }

    /**
     * 撹拌羽根洗浄確認
     * @return the kakuhanhanesenjyoukakunin
     */
    public FXHDD01 getKakuhanhanesenjyoukakunin() {
        return kakuhanhanesenjyoukakunin;
    }

    /**
     * 撹拌羽根洗浄確認
     * @param kakuhanhanesenjyoukakunin the kakuhanhanesenjyoukakunin to set
     */
    public void setKakuhanhanesenjyoukakunin(FXHDD01 kakuhanhanesenjyoukakunin) {
        this.kakuhanhanesenjyoukakunin = kakuhanhanesenjyoukakunin;
    }

    /**
     * 撹拌軸洗浄確認
     * @return the kakuhanjikusenjyoukakunin
     */
    public FXHDD01 getKakuhanjikusenjyoukakunin() {
        return kakuhanjikusenjyoukakunin;
    }

    /**
     * 撹拌軸洗浄確認
     * @param kakuhanjikusenjyoukakunin the kakuhanjikusenjyoukakunin to set
     */
    public void setKakuhanjikusenjyoukakunin(FXHDD01 kakuhanjikusenjyoukakunin) {
        this.kakuhanjikusenjyoukakunin = kakuhanjikusenjyoukakunin;
    }

    /**
     * 投入①
     * @return the tounyuu1
     */
    public FXHDD01 getTounyuu1() {
        return tounyuu1;
    }

    /**
     * 投入①
     * @param tounyuu1 the tounyuu1 to set
     */
    public void setTounyuu1(FXHDD01 tounyuu1) {
        this.tounyuu1 = tounyuu1;
    }

    /**
     * 投入②
     * @return the tounyuu2
     */
    public FXHDD01 getTounyuu2() {
        return tounyuu2;
    }

    /**
     * 投入②
     * @param tounyuu2 the tounyuu2 to set
     */
    public void setTounyuu2(FXHDD01 tounyuu2) {
        this.tounyuu2 = tounyuu2;
    }

    /**
     * 投入③
     * @return the tounyuu3
     */
    public FXHDD01 getTounyuu3() {
        return tounyuu3;
    }

    /**
     * 投入③
     * @param tounyuu3 the tounyuu3 to set
     */
    public void setTounyuu3(FXHDD01 tounyuu3) {
        this.tounyuu3 = tounyuu3;
    }

    /**
     * 投入④
     * @return the tounyuu4
     */
    public FXHDD01 getTounyuu4() {
        return tounyuu4;
    }

    /**
     * 投入④
     * @param tounyuu4 the tounyuu4 to set
     */
    public void setTounyuu4(FXHDD01 tounyuu4) {
        this.tounyuu4 = tounyuu4;
    }

    /**
     * 投入⑤
     * @return the tounyuu5
     */
    public FXHDD01 getTounyuu5() {
        return tounyuu5;
    }

    /**
     * 投入⑤
     * @param tounyuu5 the tounyuu5 to set
     */
    public void setTounyuu5(FXHDD01 tounyuu5) {
        this.tounyuu5 = tounyuu5;
    }

    /**
     * 投入⑥
     * @return the tounyuu6
     */
    public FXHDD01 getTounyuu6() {
        return tounyuu6;
    }

    /**
     * 投入⑥
     * @param tounyuu6 the tounyuu6 to set
     */
    public void setTounyuu6(FXHDD01 tounyuu6) {
        this.tounyuu6 = tounyuu6;
    }

    /**
     * ｽﾗﾘｰ投入確認者
     * @return the slurrytounyuukakuninsya
     */
    public FXHDD01 getSlurrytounyuukakuninsya() {
        return slurrytounyuukakuninsya;
    }

    /**
     * ｽﾗﾘｰ投入確認者
     * @param slurrytounyuukakuninsya the slurrytounyuukakuninsya to set
     */
    public void setSlurrytounyuukakuninsya(FXHDD01 slurrytounyuukakuninsya) {
        this.slurrytounyuukakuninsya = slurrytounyuukakuninsya;
    }

    /**
     * 投入⑦
     * @return the tounyuu7
     */
    public FXHDD01 getTounyuu7() {
        return tounyuu7;
    }

    /**
     * 投入⑦
     * @param tounyuu7 the tounyuu7 to set
     */
    public void setTounyuu7(FXHDD01 tounyuu7) {
        this.tounyuu7 = tounyuu7;
    }

    /**
     * 投入⑧
     * @return the tounyuu8
     */
    public FXHDD01 getTounyuu8() {
        return tounyuu8;
    }

    /**
     * 投入⑧
     * @param tounyuu8 the tounyuu8 to set
     */
    public void setTounyuu8(FXHDD01 tounyuu8) {
        this.tounyuu8 = tounyuu8;
    }

    /**
     * 溶剤投入確認者
     * @return the youzaitounyuukakuninsya
     */
    public FXHDD01 getYouzaitounyuukakuninsya() {
        return youzaitounyuukakuninsya;
    }

    /**
     * 溶剤投入確認者
     * @param youzaitounyuukakuninsya the youzaitounyuukakuninsya to set
     */
    public void setYouzaitounyuukakuninsya(FXHDD01 youzaitounyuukakuninsya) {
        this.youzaitounyuukakuninsya = youzaitounyuukakuninsya;
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