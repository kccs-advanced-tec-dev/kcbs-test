/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日       2021/11/23<br>
 * 計画書No     MB2101-DK002<br>
 * 変更者       KCSS K.Jo<br>
 * 変更理由     新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 誘電体ｽﾗﾘｰ作製・主原料秤量履歴検索画面のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/11/23
 */
public class GXHDO202B022Model implements Serializable {
    /** WIPﾛｯﾄNo */
    private String lotno = "";

    /** 誘電体ｽﾗﾘｰ品名 */
    private String yuudentaislurryhinmei = "";

    /** 誘電体ｽﾗﾘｰLotNo */
    private String yuudentaislurrylotno = "";

    /** ﾛｯﾄ区分 */
    private String lotkubun = "";

    /** 原料LotNo */
    private String genryoulotno = "";

    /** 原料記号 */
    private String genryoukigou = "";

    /** 秤量号機 */
    private String goki = "";

    /** 主原料秤量開始日時 */
    private Timestamp syugenryouhyouryoukaisinichiji = null;

    /** 主原料①_材料品名 */
    private String syugenryou1_zairyouhinmei = "";

    /** 主原料①_調合量規格 */
    private String syugenryou1_tyougouryoukikaku = "";

    /** 主原料①_部材在庫No */
    private String syugenryou1_buzaizaikolotno = "";

    /** 主原料①_調合量1 */
    private Integer syugenryou1_tyougouryou1 = null;

    /** 主原料①_調合量2 */
    private Integer syugenryou1_tyougouryou2 = null;

    /** 主原料①_調合量3 */
    private Integer syugenryou1_tyougouryou3 = null;

    /** 主原料①_調合量4 */
    private Integer syugenryou1_tyougouryou4 = null;

    /** 主原料①_調合量5 */
    private Integer syugenryou1_tyougouryou5 = null;

    /** 主原料①_調合量6 */
    private Integer syugenryou1_tyougouryou6 = null;

    /** 主原料①_調合量7 */
    private Integer syugenryou1_tyougouryou7 = null;

    /** 主原料②_材料品名 */
    private String syugenryou2_zairyouhinmei = "";

    /** 主原料②_調合量規格 */
    private String syugenryou2_tyougouryoukikaku = "";

    /** 主原料②_部材在庫No */
    private String syugenryou2_buzaizaikolotno = "";

    /** 主原料②_調合量1 */
    private Integer syugenryou2_tyougouryou1 = null;

    /** 主原料②_調合量2 */
    private Integer syugenryou2_tyougouryou2 = null;

    /** 主原料②_調合量3 */
    private Integer syugenryou2_tyougouryou3 = null;

    /** 主原料③_材料品名 */
    private String syugenryou3_zairyouhinmei = "";

    /** 主原料③_調合量規格 */
    private String syugenryou3_tyougouryoukikaku = "";

    /** 主原料③_部材在庫No */
    private String syugenryou3_buzaizaikolotno = "";

    /** 主原料③_調合量1 */
    private Integer syugenryou3_tyougouryou1 = null;

    /** 主原料③_調合量2 */
    private Integer syugenryou3_tyougouryou2 = null;

    /** 主原料③_調合量3 */
    private Integer syugenryou3_tyougouryou3 = null;

    /** 主原料④_材料品名 */
    private String syugenryou4_zairyouhinmei = "";

    /** 主原料④_調合量規格 */
    private String syugenryou4_tyougouryoukikaku = "";

    /** 主原料④_部材在庫No */
    private String syugenryou4_buzaizaikolotno = "";

    /** 主原料④_調合量1 */
    private Integer syugenryou4_tyougouryou1 = null;

    /** 主原料④_調合量2 */
    private Integer syugenryou4_tyougouryou2 = null;

    /** 主原料⑤_材料品名 */
    private String syugenryou5_zairyouhinmei = "";

    /** 主原料⑤_調合量規格 */
    private String syugenryou5_tyougouryoukikaku = "";

    /** 主原料⑤_部材在庫No */
    private String syugenryou5_buzaizaikolotno = "";

    /** 主原料⑤_調合量1 */
    private Integer syugenryou5_tyougouryou1 = null;

    /** 主原料⑤_調合量2 */
    private Integer syugenryou5_tyougouryou2 = null;

    /** 主原料⑥_材料品名 */
    private String syugenryou6_zairyouhinmei = "";

    /** 主原料⑥_調合量規格 */
    private String syugenryou6_tyougouryoukikaku = "";

    /** 主原料⑥_部材在庫No */
    private String syugenryou6_buzaizaikolotno = "";

    /** 主原料⑥_調合量1 */
    private Integer syugenryou6_tyougouryou1 = null;

    /** 調合量合計 */
    private Integer tyougouryougoukei = null;

    /** 主原料秤量終了日時 */
    private Timestamp syugenryouhyouryousyuuryounichiji = null;

    /** 固形分測定担当者 */
    private String kokeibunsokuteitantousya = "";

    /** 備考1 */
    private String bikou1 = "";

    /** 備考2 */
    private String bikou2 = "";

    /**
     * WIPﾛｯﾄNo
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * WIPﾛｯﾄNo
     * @param lotno the lotno to set
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * 誘電体ｽﾗﾘｰ品名
     * @return the yuudentaislurryhinmei
     */
    public String getYuudentaislurryhinmei() {
        return yuudentaislurryhinmei;
    }

    /**
     * 誘電体ｽﾗﾘｰ品名
     * @param yuudentaislurryhinmei the yuudentaislurryhinmei to set
     */
    public void setYuudentaislurryhinmei(String yuudentaislurryhinmei) {
        this.yuudentaislurryhinmei = yuudentaislurryhinmei;
    }

    /**
     * 誘電体ｽﾗﾘｰLotNo
     * @return the yuudentaislurrylotno
     */
    public String getYuudentaislurrylotno() {
        return yuudentaislurrylotno;
    }

    /**
     * 誘電体ｽﾗﾘｰLotNo
     * @param yuudentaislurrylotno the yuudentaislurrylotno to set
     */
    public void setYuudentaislurrylotno(String yuudentaislurrylotno) {
        this.yuudentaislurrylotno = yuudentaislurrylotno;
    }

    /**
     * ﾛｯﾄ区分
     * @return the lotkubun
     */
    public String getLotkubun() {
        return lotkubun;
    }

    /**
     * ﾛｯﾄ区分
     * @param lotkubun the lotkubun to set
     */
    public void setLotkubun(String lotkubun) {
        this.lotkubun = lotkubun;
    }

    /**
     * 原料LotNo
     * @return the genryoulotno
     */
    public String getGenryoulotno() {
        return genryoulotno;
    }

    /**
     * 原料LotNo
     * @param genryoulotno the genryoulotno to set
     */
    public void setGenryoulotno(String genryoulotno) {
        this.genryoulotno = genryoulotno;
    }

    /**
     * 原料記号
     * @return the genryoukigou
     */
    public String getGenryoukigou() {
        return genryoukigou;
    }

    /**
     * 原料記号
     * @param genryoukigou the genryoukigou to set
     */
    public void setGenryoukigou(String genryoukigou) {
        this.genryoukigou = genryoukigou;
    }

    /**
     * 秤量号機
     * @return the goki
     */
    public String getGoki() {
        return goki;
    }

    /**
     * 秤量号機
     * @param goki the goki to set
     */
    public void setGoki(String goki) {
        this.goki = goki;
    }

    /**
     * 主原料秤量開始日時
     * @return the syugenryouhyouryoukaisinichiji
     */
    public Timestamp getSyugenryouhyouryoukaisinichiji() {
        return syugenryouhyouryoukaisinichiji;
    }

    /**
     * 主原料秤量開始日時
     * @param syugenryouhyouryoukaisinichiji the syugenryouhyouryoukaisinichiji to set
     */
    public void setSyugenryouhyouryoukaisinichiji(Timestamp syugenryouhyouryoukaisinichiji) {
        this.syugenryouhyouryoukaisinichiji = syugenryouhyouryoukaisinichiji;
    }

    /**
     * 主原料①_材料品名
     * @return the syugenryou1_zairyouhinmei
     */
    public String getSyugenryou1_zairyouhinmei() {
        return syugenryou1_zairyouhinmei;
    }

    /**
     * 主原料①_材料品名
     * @param syugenryou1_zairyouhinmei the syugenryou1_zairyouhinmei to set
     */
    public void setSyugenryou1_zairyouhinmei(String syugenryou1_zairyouhinmei) {
        this.syugenryou1_zairyouhinmei = syugenryou1_zairyouhinmei;
    }

    /**
     * 主原料①_調合量規格
     * @return the syugenryou1_tyougouryoukikaku
     */
    public String getSyugenryou1_tyougouryoukikaku() {
        return syugenryou1_tyougouryoukikaku;
    }

    /**
     * 主原料①_調合量規格
     * @param syugenryou1_tyougouryoukikaku the syugenryou1_tyougouryoukikaku to set
     */
    public void setSyugenryou1_tyougouryoukikaku(String syugenryou1_tyougouryoukikaku) {
        this.syugenryou1_tyougouryoukikaku = syugenryou1_tyougouryoukikaku;
    }

    /**
     * 主原料①_部材在庫No
     * @return the syugenryou1_buzaizaikolotno
     */
    public String getSyugenryou1_buzaizaikolotno() {
        return syugenryou1_buzaizaikolotno;
    }

    /**
     * 主原料①_部材在庫No
     * @param syugenryou1_buzaizaikolotno the syugenryou1_buzaizaikolotno to set
     */
    public void setSyugenryou1_buzaizaikolotno(String syugenryou1_buzaizaikolotno) {
        this.syugenryou1_buzaizaikolotno = syugenryou1_buzaizaikolotno;
    }

    /**
     * 主原料①_調合量1
     * @return the syugenryou1_tyougouryou1
     */
    public Integer getSyugenryou1_tyougouryou1() {
        return syugenryou1_tyougouryou1;
    }

    /**
     * 主原料①_調合量1
     * @param syugenryou1_tyougouryou1 the syugenryou1_tyougouryou1 to set
     */
    public void setSyugenryou1_tyougouryou1(Integer syugenryou1_tyougouryou1) {
        this.syugenryou1_tyougouryou1 = syugenryou1_tyougouryou1;
    }

    /**
     * 主原料①_調合量2
     * @return the syugenryou1_tyougouryou2
     */
    public Integer getSyugenryou1_tyougouryou2() {
        return syugenryou1_tyougouryou2;
    }

    /**
     * 主原料①_調合量2
     * @param syugenryou1_tyougouryou2 the syugenryou1_tyougouryou2 to set
     */
    public void setSyugenryou1_tyougouryou2(Integer syugenryou1_tyougouryou2) {
        this.syugenryou1_tyougouryou2 = syugenryou1_tyougouryou2;
    }

    /**
     * 主原料①_調合量3
     * @return the syugenryou1_tyougouryou3
     */
    public Integer getSyugenryou1_tyougouryou3() {
        return syugenryou1_tyougouryou3;
    }

    /**
     * 主原料①_調合量3
     * @param syugenryou1_tyougouryou3 the syugenryou1_tyougouryou3 to set
     */
    public void setSyugenryou1_tyougouryou3(Integer syugenryou1_tyougouryou3) {
        this.syugenryou1_tyougouryou3 = syugenryou1_tyougouryou3;
    }

    /**
     * 主原料①_調合量4
     * @return the syugenryou1_tyougouryou4
     */
    public Integer getSyugenryou1_tyougouryou4() {
        return syugenryou1_tyougouryou4;
    }

    /**
     * 主原料①_調合量4
     * @param syugenryou1_tyougouryou4 the syugenryou1_tyougouryou4 to set
     */
    public void setSyugenryou1_tyougouryou4(Integer syugenryou1_tyougouryou4) {
        this.syugenryou1_tyougouryou4 = syugenryou1_tyougouryou4;
    }

    /**
     * 主原料①_調合量5
     * @return the syugenryou1_tyougouryou5
     */
    public Integer getSyugenryou1_tyougouryou5() {
        return syugenryou1_tyougouryou5;
    }

    /**
     * 主原料①_調合量5
     * @param syugenryou1_tyougouryou5 the syugenryou1_tyougouryou5 to set
     */
    public void setSyugenryou1_tyougouryou5(Integer syugenryou1_tyougouryou5) {
        this.syugenryou1_tyougouryou5 = syugenryou1_tyougouryou5;
    }

    /**
     * 主原料①_調合量6
     * @return the syugenryou1_tyougouryou6
     */
    public Integer getSyugenryou1_tyougouryou6() {
        return syugenryou1_tyougouryou6;
    }

    /**
     * 主原料①_調合量6
     * @param syugenryou1_tyougouryou6 the syugenryou1_tyougouryou6 to set
     */
    public void setSyugenryou1_tyougouryou6(Integer syugenryou1_tyougouryou6) {
        this.syugenryou1_tyougouryou6 = syugenryou1_tyougouryou6;
    }

    /**
     * 主原料①_調合量7
     * @return the syugenryou1_tyougouryou7
     */
    public Integer getSyugenryou1_tyougouryou7() {
        return syugenryou1_tyougouryou7;
    }

    /**
     * 主原料①_調合量7
     * @param syugenryou1_tyougouryou7 the syugenryou1_tyougouryou7 to set
     */
    public void setSyugenryou1_tyougouryou7(Integer syugenryou1_tyougouryou7) {
        this.syugenryou1_tyougouryou7 = syugenryou1_tyougouryou7;
    }

    /**
     * 主原料②_材料品名
     * @return the syugenryou2_zairyouhinmei
     */
    public String getSyugenryou2_zairyouhinmei() {
        return syugenryou2_zairyouhinmei;
    }

    /**
     * 主原料②_材料品名
     * @param syugenryou2_zairyouhinmei the syugenryou2_zairyouhinmei to set
     */
    public void setSyugenryou2_zairyouhinmei(String syugenryou2_zairyouhinmei) {
        this.syugenryou2_zairyouhinmei = syugenryou2_zairyouhinmei;
    }

    /**
     * 主原料②_調合量規格
     * @return the syugenryou2_tyougouryoukikaku
     */
    public String getSyugenryou2_tyougouryoukikaku() {
        return syugenryou2_tyougouryoukikaku;
    }

    /**
     * 主原料②_調合量規格
     * @param syugenryou2_tyougouryoukikaku the syugenryou2_tyougouryoukikaku to set
     */
    public void setSyugenryou2_tyougouryoukikaku(String syugenryou2_tyougouryoukikaku) {
        this.syugenryou2_tyougouryoukikaku = syugenryou2_tyougouryoukikaku;
    }

    /**
     * 主原料②_部材在庫No
     * @return the syugenryou2_buzaizaikolotno
     */
    public String getSyugenryou2_buzaizaikolotno() {
        return syugenryou2_buzaizaikolotno;
    }

    /**
     * 主原料②_部材在庫No
     * @param syugenryou2_buzaizaikolotno the syugenryou2_buzaizaikolotno to set
     */
    public void setSyugenryou2_buzaizaikolotno(String syugenryou2_buzaizaikolotno) {
        this.syugenryou2_buzaizaikolotno = syugenryou2_buzaizaikolotno;
    }

    /**
     * 主原料②_調合量1
     * @return the syugenryou2_tyougouryou1
     */
    public Integer getSyugenryou2_tyougouryou1() {
        return syugenryou2_tyougouryou1;
    }

    /**
     * 主原料②_調合量1
     * @param syugenryou2_tyougouryou1 the syugenryou2_tyougouryou1 to set
     */
    public void setSyugenryou2_tyougouryou1(Integer syugenryou2_tyougouryou1) {
        this.syugenryou2_tyougouryou1 = syugenryou2_tyougouryou1;
    }

    /**
     * 主原料②_調合量2
     * @return the syugenryou2_tyougouryou2
     */
    public Integer getSyugenryou2_tyougouryou2() {
        return syugenryou2_tyougouryou2;
    }

    /**
     * 主原料②_調合量2
     * @param syugenryou2_tyougouryou2 the syugenryou2_tyougouryou2 to set
     */
    public void setSyugenryou2_tyougouryou2(Integer syugenryou2_tyougouryou2) {
        this.syugenryou2_tyougouryou2 = syugenryou2_tyougouryou2;
    }

    /**
     * 主原料②_調合量3
     * @return the syugenryou2_tyougouryou3
     */
    public Integer getSyugenryou2_tyougouryou3() {
        return syugenryou2_tyougouryou3;
    }

    /**
     * 主原料②_調合量3
     * @param syugenryou2_tyougouryou3 the syugenryou2_tyougouryou3 to set
     */
    public void setSyugenryou2_tyougouryou3(Integer syugenryou2_tyougouryou3) {
        this.syugenryou2_tyougouryou3 = syugenryou2_tyougouryou3;
    }

    /**
     * 主原料③_材料品名
     * @return the syugenryou3_zairyouhinmei
     */
    public String getSyugenryou3_zairyouhinmei() {
        return syugenryou3_zairyouhinmei;
    }

    /**
     * 主原料③_材料品名
     * @param syugenryou3_zairyouhinmei the syugenryou3_zairyouhinmei to set
     */
    public void setSyugenryou3_zairyouhinmei(String syugenryou3_zairyouhinmei) {
        this.syugenryou3_zairyouhinmei = syugenryou3_zairyouhinmei;
    }

    /**
     * 主原料③_調合量規格
     * @return the syugenryou3_tyougouryoukikaku
     */
    public String getSyugenryou3_tyougouryoukikaku() {
        return syugenryou3_tyougouryoukikaku;
    }

    /**
     * 主原料③_調合量規格
     * @param syugenryou3_tyougouryoukikaku the syugenryou3_tyougouryoukikaku to set
     */
    public void setSyugenryou3_tyougouryoukikaku(String syugenryou3_tyougouryoukikaku) {
        this.syugenryou3_tyougouryoukikaku = syugenryou3_tyougouryoukikaku;
    }

    /**
     * 主原料③_部材在庫No
     * @return the syugenryou3_buzaizaikolotno
     */
    public String getSyugenryou3_buzaizaikolotno() {
        return syugenryou3_buzaizaikolotno;
    }

    /**
     * 主原料③_部材在庫No
     * @param syugenryou3_buzaizaikolotno the syugenryou3_buzaizaikolotno to set
     */
    public void setSyugenryou3_buzaizaikolotno(String syugenryou3_buzaizaikolotno) {
        this.syugenryou3_buzaizaikolotno = syugenryou3_buzaizaikolotno;
    }

    /**
     * 主原料③_調合量1
     * @return the syugenryou3_tyougouryou1
     */
    public Integer getSyugenryou3_tyougouryou1() {
        return syugenryou3_tyougouryou1;
    }

    /**
     * 主原料③_調合量1
     * @param syugenryou3_tyougouryou1 the syugenryou3_tyougouryou1 to set
     */
    public void setSyugenryou3_tyougouryou1(Integer syugenryou3_tyougouryou1) {
        this.syugenryou3_tyougouryou1 = syugenryou3_tyougouryou1;
    }

    /**
     * 主原料③_調合量2
     * @return the syugenryou3_tyougouryou2
     */
    public Integer getSyugenryou3_tyougouryou2() {
        return syugenryou3_tyougouryou2;
    }

    /**
     * 主原料③_調合量2
     * @param syugenryou3_tyougouryou2 the syugenryou3_tyougouryou2 to set
     */
    public void setSyugenryou3_tyougouryou2(Integer syugenryou3_tyougouryou2) {
        this.syugenryou3_tyougouryou2 = syugenryou3_tyougouryou2;
    }

    /**
     * 主原料③_調合量3
     * @return the syugenryou3_tyougouryou3
     */
    public Integer getSyugenryou3_tyougouryou3() {
        return syugenryou3_tyougouryou3;
    }

    /**
     * 主原料③_調合量3
     * @param syugenryou3_tyougouryou3 the syugenryou3_tyougouryou3 to set
     */
    public void setSyugenryou3_tyougouryou3(Integer syugenryou3_tyougouryou3) {
        this.syugenryou3_tyougouryou3 = syugenryou3_tyougouryou3;
    }

    /**
     * 主原料④_材料品名
     * @return the syugenryou4_zairyouhinmei
     */
    public String getSyugenryou4_zairyouhinmei() {
        return syugenryou4_zairyouhinmei;
    }

    /**
     * 主原料④_材料品名
     * @param syugenryou4_zairyouhinmei the syugenryou4_zairyouhinmei to set
     */
    public void setSyugenryou4_zairyouhinmei(String syugenryou4_zairyouhinmei) {
        this.syugenryou4_zairyouhinmei = syugenryou4_zairyouhinmei;
    }

    /**
     * 主原料④_調合量規格
     * @return the syugenryou4_tyougouryoukikaku
     */
    public String getSyugenryou4_tyougouryoukikaku() {
        return syugenryou4_tyougouryoukikaku;
    }

    /**
     * 主原料④_調合量規格
     * @param syugenryou4_tyougouryoukikaku the syugenryou4_tyougouryoukikaku to set
     */
    public void setSyugenryou4_tyougouryoukikaku(String syugenryou4_tyougouryoukikaku) {
        this.syugenryou4_tyougouryoukikaku = syugenryou4_tyougouryoukikaku;
    }

    /**
     * 主原料④_部材在庫No
     * @return the syugenryou4_buzaizaikolotno
     */
    public String getSyugenryou4_buzaizaikolotno() {
        return syugenryou4_buzaizaikolotno;
    }

    /**
     * 主原料④_部材在庫No
     * @param syugenryou4_buzaizaikolotno the syugenryou4_buzaizaikolotno to set
     */
    public void setSyugenryou4_buzaizaikolotno(String syugenryou4_buzaizaikolotno) {
        this.syugenryou4_buzaizaikolotno = syugenryou4_buzaizaikolotno;
    }

    /**
     * 主原料④_調合量1
     * @return the syugenryou4_tyougouryou1
     */
    public Integer getSyugenryou4_tyougouryou1() {
        return syugenryou4_tyougouryou1;
    }

    /**
     * 主原料④_調合量1
     * @param syugenryou4_tyougouryou1 the syugenryou4_tyougouryou1 to set
     */
    public void setSyugenryou4_tyougouryou1(Integer syugenryou4_tyougouryou1) {
        this.syugenryou4_tyougouryou1 = syugenryou4_tyougouryou1;
    }

    /**
     * 主原料④_調合量2
     * @return the syugenryou4_tyougouryou2
     */
    public Integer getSyugenryou4_tyougouryou2() {
        return syugenryou4_tyougouryou2;
    }

    /**
     * 主原料④_調合量2
     * @param syugenryou4_tyougouryou2 the syugenryou4_tyougouryou2 to set
     */
    public void setSyugenryou4_tyougouryou2(Integer syugenryou4_tyougouryou2) {
        this.syugenryou4_tyougouryou2 = syugenryou4_tyougouryou2;
    }

    /**
     * 主原料⑤_材料品名
     * @return the syugenryou5_zairyouhinmei
     */
    public String getSyugenryou5_zairyouhinmei() {
        return syugenryou5_zairyouhinmei;
    }

    /**
     * 主原料⑤_材料品名
     * @param syugenryou5_zairyouhinmei the syugenryou5_zairyouhinmei to set
     */
    public void setSyugenryou5_zairyouhinmei(String syugenryou5_zairyouhinmei) {
        this.syugenryou5_zairyouhinmei = syugenryou5_zairyouhinmei;
    }

    /**
     * 主原料⑤_調合量規格
     * @return the syugenryou5_tyougouryoukikaku
     */
    public String getSyugenryou5_tyougouryoukikaku() {
        return syugenryou5_tyougouryoukikaku;
    }

    /**
     * 主原料⑤_調合量規格
     * @param syugenryou5_tyougouryoukikaku the syugenryou5_tyougouryoukikaku to set
     */
    public void setSyugenryou5_tyougouryoukikaku(String syugenryou5_tyougouryoukikaku) {
        this.syugenryou5_tyougouryoukikaku = syugenryou5_tyougouryoukikaku;
    }

    /**
     * 主原料⑤_部材在庫No
     * @return the syugenryou5_buzaizaikolotno
     */
    public String getSyugenryou5_buzaizaikolotno() {
        return syugenryou5_buzaizaikolotno;
    }

    /**
     * 主原料⑤_部材在庫No
     * @param syugenryou5_buzaizaikolotno the syugenryou5_buzaizaikolotno to set
     */
    public void setSyugenryou5_buzaizaikolotno(String syugenryou5_buzaizaikolotno) {
        this.syugenryou5_buzaizaikolotno = syugenryou5_buzaizaikolotno;
    }

    /**
     * 主原料⑤_調合量1
     * @return the syugenryou5_tyougouryou1
     */
    public Integer getSyugenryou5_tyougouryou1() {
        return syugenryou5_tyougouryou1;
    }

    /**
     * 主原料⑤_調合量1
     * @param syugenryou5_tyougouryou1 the syugenryou5_tyougouryou1 to set
     */
    public void setSyugenryou5_tyougouryou1(Integer syugenryou5_tyougouryou1) {
        this.syugenryou5_tyougouryou1 = syugenryou5_tyougouryou1;
    }

    /**
     * 主原料⑤_調合量2
     * @return the syugenryou5_tyougouryou2
     */
    public Integer getSyugenryou5_tyougouryou2() {
        return syugenryou5_tyougouryou2;
    }

    /**
     * 主原料⑤_調合量2
     * @param syugenryou5_tyougouryou2 the syugenryou5_tyougouryou2 to set
     */
    public void setSyugenryou5_tyougouryou2(Integer syugenryou5_tyougouryou2) {
        this.syugenryou5_tyougouryou2 = syugenryou5_tyougouryou2;
    }

    /**
     * 主原料⑥_材料品名
     * @return the syugenryou6_zairyouhinmei
     */
    public String getSyugenryou6_zairyouhinmei() {
        return syugenryou6_zairyouhinmei;
    }

    /**
     * 主原料⑥_材料品名
     * @param syugenryou6_zairyouhinmei the syugenryou6_zairyouhinmei to set
     */
    public void setSyugenryou6_zairyouhinmei(String syugenryou6_zairyouhinmei) {
        this.syugenryou6_zairyouhinmei = syugenryou6_zairyouhinmei;
    }

    /**
     * 主原料⑥_調合量規格
     * @return the syugenryou6_tyougouryoukikaku
     */
    public String getSyugenryou6_tyougouryoukikaku() {
        return syugenryou6_tyougouryoukikaku;
    }

    /**
     * 主原料⑥_調合量規格
     * @param syugenryou6_tyougouryoukikaku the syugenryou6_tyougouryoukikaku to set
     */
    public void setSyugenryou6_tyougouryoukikaku(String syugenryou6_tyougouryoukikaku) {
        this.syugenryou6_tyougouryoukikaku = syugenryou6_tyougouryoukikaku;
    }

    /**
     * 主原料⑥_部材在庫No
     * @return the syugenryou6_buzaizaikolotno
     */
    public String getSyugenryou6_buzaizaikolotno() {
        return syugenryou6_buzaizaikolotno;
    }

    /**
     * 主原料⑥_部材在庫No
     * @param syugenryou6_buzaizaikolotno the syugenryou6_buzaizaikolotno to set
     */
    public void setSyugenryou6_buzaizaikolotno(String syugenryou6_buzaizaikolotno) {
        this.syugenryou6_buzaizaikolotno = syugenryou6_buzaizaikolotno;
    }

    /**
     * 主原料⑥_調合量1
     * @return the syugenryou6_tyougouryou1
     */
    public Integer getSyugenryou6_tyougouryou1() {
        return syugenryou6_tyougouryou1;
    }

    /**
     * 主原料⑥_調合量1
     * @param syugenryou6_tyougouryou1 the syugenryou6_tyougouryou1 to set
     */
    public void setSyugenryou6_tyougouryou1(Integer syugenryou6_tyougouryou1) {
        this.syugenryou6_tyougouryou1 = syugenryou6_tyougouryou1;
    }

    /**
     * 調合量合計
     * @return the tyougouryougoukei
     */
    public Integer getTyougouryougoukei() {
        return tyougouryougoukei;
    }

    /**
     * 調合量合計
     * @param tyougouryougoukei the tyougouryougoukei to set
     */
    public void setTyougouryougoukei(Integer tyougouryougoukei) {
        this.tyougouryougoukei = tyougouryougoukei;
    }

    /**
     * 主原料秤量終了日時
     * @return the syugenryouhyouryousyuuryounichiji
     */
    public Timestamp getSyugenryouhyouryousyuuryounichiji() {
        return syugenryouhyouryousyuuryounichiji;
    }

    /**
     * 主原料秤量終了日時
     * @param syugenryouhyouryousyuuryounichiji the syugenryouhyouryousyuuryounichiji to set
     */
    public void setSyugenryouhyouryousyuuryounichiji(Timestamp syugenryouhyouryousyuuryounichiji) {
        this.syugenryouhyouryousyuuryounichiji = syugenryouhyouryousyuuryounichiji;
    }

    /**
     * 固形分測定担当者
     * @return the kokeibunsokuteitantousya
     */
    public String getKokeibunsokuteitantousya() {
        return kokeibunsokuteitantousya;
    }

    /**
     * 固形分測定担当者
     * @param kokeibunsokuteitantousya the kokeibunsokuteitantousya to set
     */
    public void setKokeibunsokuteitantousya(String kokeibunsokuteitantousya) {
        this.kokeibunsokuteitantousya = kokeibunsokuteitantousya;
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

}