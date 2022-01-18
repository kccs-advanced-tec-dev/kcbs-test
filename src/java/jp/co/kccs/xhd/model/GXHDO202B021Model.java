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
 * 誘電体ｽﾗﾘｰ作製・添加材・ｿﾞﾙ秤量履歴検索画面のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/11/23
 */
public class GXHDO202B021Model implements Serializable {
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

    /** 秤量開始日時 */
    private Timestamp hyouryoukaisinichiji = null;

    /** 添加材①_材料品名 */
    private String tenkazai1_zairyouhinmei = "";

    /** 添加材①_調合量規格 */
    private String tenkazai1_tyougouryoukikaku = "";

    /** 添加材①_部材在庫No1 */
    private String tenkazai1_buzaizaikolotno1 = "";

    /** 添加材①_調合量1 */
    private Integer tenkazai1_tyougouryou1 = null;

    /** 添加材①_部材在庫No2 */
    private String tenkazai1_buzaizaikolotno2 = "";

    /** 添加材①_調合量2 */
    private Integer tenkazai1_tyougouryou2 = null;

    /** 添加材②_材料品名 */
    private String tenkazai2_zairyouhinmei = "";

    /** 添加材②_調合量規格 */
    private String tenkazai2_tyougouryoukikaku = "";

    /** 添加材②_部材在庫No1 */
    private String tenkazai2_buzaizaikolotno1 = "";

    /** 添加材②_調合量1 */
    private Integer tenkazai2_tyougouryou1 = null;

    /** 添加材②_部材在庫No2 */
    private String tenkazai2_buzaizaikolotno2 = "";

    /** 添加材②_調合量2 */
    private Integer tenkazai2_tyougouryou2 = null;

    /** 添加材③_材料品名 */
    private String tenkazai3_zairyouhinmei = "";

    /** 添加材③_調合量規格 */
    private String tenkazai3_tyougouryoukikaku = "";

    /** 添加材③_部材在庫No1 */
    private String tenkazai3_buzaizaikolotno1 = "";

    /** 添加材③_調合量1 */
    private Integer tenkazai3_tyougouryou1 = null;

    /** 添加材③_部材在庫No2 */
    private String tenkazai3_buzaizaikolotno2 = "";

    /** 添加材③_調合量2 */
    private Integer tenkazai3_tyougouryou2 = null;

    /** 添加材④_材料品名 */
    private String tenkazai4_zairyouhinmei = "";

    /** 添加材④_調合量規格 */
    private String tenkazai4_tyougouryoukikaku = "";

    /** 添加材④_部材在庫No1 */
    private String tenkazai4_buzaizaikolotno1 = "";

    /** 添加材④_調合量1 */
    private Integer tenkazai4_tyougouryou1 = null;

    /** 添加材④_部材在庫No2 */
    private String tenkazai4_buzaizaikolotno2 = "";

    /** 添加材④_調合量2 */
    private Integer tenkazai4_tyougouryou2 = null;

    /** 添加材⑤_材料品名 */
    private String tenkazai5_zairyouhinmei = "";

    /** 添加材⑤_調合量規格 */
    private String tenkazai5_tyougouryoukikaku = "";

    /** 添加材⑤_部材在庫No1 */
    private String tenkazai5_buzaizaikolotno1 = "";

    /** 添加材⑤_調合量1 */
    private Integer tenkazai5_tyougouryou1 = null;

    /** 添加材⑤_部材在庫No2 */
    private String tenkazai5_buzaizaikolotno2 = "";

    /** 添加材⑤_調合量2 */
    private Integer tenkazai5_tyougouryou2 = null;

    /** ｿﾞﾙ①_材料品名 */
    private String sol1_zairyouhinmei = "";

    /** ｿﾞﾙ①_調合量規格 */
    private String sol1_tyougouryoukikaku = "";

    /** ｿﾞﾙ①_部材在庫No1 */
    private String sol1_buzaizaikolotno1 = "";

    /** ｿﾞﾙ①_調合量1 */
    private Integer sol1_tyougouryou1 = null;

    /** ｿﾞﾙ①_部材在庫No2 */
    private String sol1_buzaizaikolotno2 = "";

    /** ｿﾞﾙ①_調合量2 */
    private Integer sol1_tyougouryou2 = null;

    /** ｿﾞﾙ②_材料品名 */
    private String sol2_zairyouhinmei = "";

    /** ｿﾞﾙ②_調合量規格 */
    private String sol2_tyougouryoukikaku = "";

    /** ｿﾞﾙ②_部材在庫No1 */
    private String sol2_buzaizaikolotno1 = "";

    /** ｿﾞﾙ②_調合量1 */
    private Integer sol2_tyougouryou1 = null;

    /** ｿﾞﾙ②_部材在庫No2 */
    private String sol2_buzaizaikolotno2 = "";

    /** ｿﾞﾙ②_調合量2 */
    private Integer sol2_tyougouryou2 = null;

    /** ｿﾞﾙ③_材料品名 */
    private String sol3_zairyouhinmei = "";

    /** ｿﾞﾙ③_調合量規格 */
    private String sol3_tyougouryoukikaku = "";

    /** ｿﾞﾙ③_部材在庫No1 */
    private String sol3_buzaizaikolotno1 = "";

    /** ｿﾞﾙ③_調合量1 */
    private Integer sol3_tyougouryou1 = null;

    /** ｿﾞﾙ③_部材在庫No2 */
    private String sol3_buzaizaikolotno2 = "";

    /** ｿﾞﾙ③_調合量2 */
    private Integer sol3_tyougouryou2 = null;

    /** ｿﾞﾙ④_材料品名 */
    private String sol4_zairyouhinmei = "";

    /** ｿﾞﾙ④_調合量規格 */
    private String sol4_tyougouryoukikaku = "";

    /** ｿﾞﾙ④_部材在庫No1 */
    private String sol4_buzaizaikolotno1 = "";

    /** ｿﾞﾙ④_調合量1 */
    private Integer sol4_tyougouryou1 = null;

    /** ｿﾞﾙ④_部材在庫No2 */
    private String sol4_buzaizaikolotno2 = "";

    /** ｿﾞﾙ④_調合量2 */
    private Integer sol4_tyougouryou2 = null;

    /** 秤量終了日時 */
    private Timestamp hyouryousyuuryounichiji = null;

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
     * 秤量開始日時
     * @return the hyouryoukaisinichiji
     */
    public Timestamp getHyouryoukaisinichiji() {
        return hyouryoukaisinichiji;
    }

    /**
     * 秤量開始日時
     * @param hyouryoukaisinichiji the hyouryoukaisinichiji to set
     */
    public void setHyouryoukaisinichiji(Timestamp hyouryoukaisinichiji) {
        this.hyouryoukaisinichiji = hyouryoukaisinichiji;
    }

    /**
     * 添加材①_材料品名
     * @return the tenkazai1_zairyouhinmei
     */
    public String getTenkazai1_zairyouhinmei() {
        return tenkazai1_zairyouhinmei;
    }

    /**
     * 添加材①_材料品名
     * @param tenkazai1_zairyouhinmei the tenkazai1_zairyouhinmei to set
     */
    public void setTenkazai1_zairyouhinmei(String tenkazai1_zairyouhinmei) {
        this.tenkazai1_zairyouhinmei = tenkazai1_zairyouhinmei;
    }

    /**
     * 添加材①_調合量規格
     * @return the tenkazai1_tyougouryoukikaku
     */
    public String getTenkazai1_tyougouryoukikaku() {
        return tenkazai1_tyougouryoukikaku;
    }

    /**
     * 添加材①_調合量規格
     * @param tenkazai1_tyougouryoukikaku the tenkazai1_tyougouryoukikaku to set
     */
    public void setTenkazai1_tyougouryoukikaku(String tenkazai1_tyougouryoukikaku) {
        this.tenkazai1_tyougouryoukikaku = tenkazai1_tyougouryoukikaku;
    }

    /**
     * 添加材①_部材在庫No1
     * @return the tenkazai1_buzaizaikolotno1
     */
    public String getTenkazai1_buzaizaikolotno1() {
        return tenkazai1_buzaizaikolotno1;
    }

    /**
     * 添加材①_部材在庫No1
     * @param tenkazai1_buzaizaikolotno1 the tenkazai1_buzaizaikolotno1 to set
     */
    public void setTenkazai1_buzaizaikolotno1(String tenkazai1_buzaizaikolotno1) {
        this.tenkazai1_buzaizaikolotno1 = tenkazai1_buzaizaikolotno1;
    }

    /**
     * 添加材①_調合量1
     * @return the tenkazai1_tyougouryou1
     */
    public Integer getTenkazai1_tyougouryou1() {
        return tenkazai1_tyougouryou1;
    }

    /**
     * 添加材①_調合量1
     * @param tenkazai1_tyougouryou1 the tenkazai1_tyougouryou1 to set
     */
    public void setTenkazai1_tyougouryou1(Integer tenkazai1_tyougouryou1) {
        this.tenkazai1_tyougouryou1 = tenkazai1_tyougouryou1;
    }

    /**
     * 添加材①_部材在庫No2
     * @return the tenkazai1_buzaizaikolotno2
     */
    public String getTenkazai1_buzaizaikolotno2() {
        return tenkazai1_buzaizaikolotno2;
    }

    /**
     * 添加材①_部材在庫No2
     * @param tenkazai1_buzaizaikolotno2 the tenkazai1_buzaizaikolotno2 to set
     */
    public void setTenkazai1_buzaizaikolotno2(String tenkazai1_buzaizaikolotno2) {
        this.tenkazai1_buzaizaikolotno2 = tenkazai1_buzaizaikolotno2;
    }

    /**
     * 添加材①_調合量2
     * @return the tenkazai1_tyougouryou2
     */
    public Integer getTenkazai1_tyougouryou2() {
        return tenkazai1_tyougouryou2;
    }

    /**
     * 添加材①_調合量2
     * @param tenkazai1_tyougouryou2 the tenkazai1_tyougouryou2 to set
     */
    public void setTenkazai1_tyougouryou2(Integer tenkazai1_tyougouryou2) {
        this.tenkazai1_tyougouryou2 = tenkazai1_tyougouryou2;
    }

    /**
     * 添加材②_材料品名
     * @return the tenkazai2_zairyouhinmei
     */
    public String getTenkazai2_zairyouhinmei() {
        return tenkazai2_zairyouhinmei;
    }

    /**
     * 添加材②_材料品名
     * @param tenkazai2_zairyouhinmei the tenkazai2_zairyouhinmei to set
     */
    public void setTenkazai2_zairyouhinmei(String tenkazai2_zairyouhinmei) {
        this.tenkazai2_zairyouhinmei = tenkazai2_zairyouhinmei;
    }

    /**
     * 添加材②_調合量規格
     * @return the tenkazai2_tyougouryoukikaku
     */
    public String getTenkazai2_tyougouryoukikaku() {
        return tenkazai2_tyougouryoukikaku;
    }

    /**
     * 添加材②_調合量規格
     * @param tenkazai2_tyougouryoukikaku the tenkazai2_tyougouryoukikaku to set
     */
    public void setTenkazai2_tyougouryoukikaku(String tenkazai2_tyougouryoukikaku) {
        this.tenkazai2_tyougouryoukikaku = tenkazai2_tyougouryoukikaku;
    }

    /**
     * 添加材②_部材在庫No1
     * @return the tenkazai2_buzaizaikolotno1
     */
    public String getTenkazai2_buzaizaikolotno1() {
        return tenkazai2_buzaizaikolotno1;
    }

    /**
     * 添加材②_部材在庫No1
     * @param tenkazai2_buzaizaikolotno1 the tenkazai2_buzaizaikolotno1 to set
     */
    public void setTenkazai2_buzaizaikolotno1(String tenkazai2_buzaizaikolotno1) {
        this.tenkazai2_buzaizaikolotno1 = tenkazai2_buzaizaikolotno1;
    }

    /**
     * 添加材②_調合量1
     * @return the tenkazai2_tyougouryou1
     */
    public Integer getTenkazai2_tyougouryou1() {
        return tenkazai2_tyougouryou1;
    }

    /**
     * 添加材②_調合量1
     * @param tenkazai2_tyougouryou1 the tenkazai2_tyougouryou1 to set
     */
    public void setTenkazai2_tyougouryou1(Integer tenkazai2_tyougouryou1) {
        this.tenkazai2_tyougouryou1 = tenkazai2_tyougouryou1;
    }

    /**
     * 添加材②_部材在庫No2
     * @return the tenkazai2_buzaizaikolotno2
     */
    public String getTenkazai2_buzaizaikolotno2() {
        return tenkazai2_buzaizaikolotno2;
    }

    /**
     * 添加材②_部材在庫No2
     * @param tenkazai2_buzaizaikolotno2 the tenkazai2_buzaizaikolotno2 to set
     */
    public void setTenkazai2_buzaizaikolotno2(String tenkazai2_buzaizaikolotno2) {
        this.tenkazai2_buzaizaikolotno2 = tenkazai2_buzaizaikolotno2;
    }

    /**
     * 添加材②_調合量2
     * @return the tenkazai2_tyougouryou2
     */
    public Integer getTenkazai2_tyougouryou2() {
        return tenkazai2_tyougouryou2;
    }

    /**
     * 添加材②_調合量2
     * @param tenkazai2_tyougouryou2 the tenkazai2_tyougouryou2 to set
     */
    public void setTenkazai2_tyougouryou2(Integer tenkazai2_tyougouryou2) {
        this.tenkazai2_tyougouryou2 = tenkazai2_tyougouryou2;
    }

    /**
     * 添加材③_材料品名
     * @return the tenkazai3_zairyouhinmei
     */
    public String getTenkazai3_zairyouhinmei() {
        return tenkazai3_zairyouhinmei;
    }

    /**
     * 添加材③_材料品名
     * @param tenkazai3_zairyouhinmei the tenkazai3_zairyouhinmei to set
     */
    public void setTenkazai3_zairyouhinmei(String tenkazai3_zairyouhinmei) {
        this.tenkazai3_zairyouhinmei = tenkazai3_zairyouhinmei;
    }

    /**
     * 添加材③_調合量規格
     * @return the tenkazai3_tyougouryoukikaku
     */
    public String getTenkazai3_tyougouryoukikaku() {
        return tenkazai3_tyougouryoukikaku;
    }

    /**
     * 添加材③_調合量規格
     * @param tenkazai3_tyougouryoukikaku the tenkazai3_tyougouryoukikaku to set
     */
    public void setTenkazai3_tyougouryoukikaku(String tenkazai3_tyougouryoukikaku) {
        this.tenkazai3_tyougouryoukikaku = tenkazai3_tyougouryoukikaku;
    }

    /**
     * 添加材③_部材在庫No1
     * @return the tenkazai3_buzaizaikolotno1
     */
    public String getTenkazai3_buzaizaikolotno1() {
        return tenkazai3_buzaizaikolotno1;
    }

    /**
     * 添加材③_部材在庫No1
     * @param tenkazai3_buzaizaikolotno1 the tenkazai3_buzaizaikolotno1 to set
     */
    public void setTenkazai3_buzaizaikolotno1(String tenkazai3_buzaizaikolotno1) {
        this.tenkazai3_buzaizaikolotno1 = tenkazai3_buzaizaikolotno1;
    }

    /**
     * 添加材③_調合量1
     * @return the tenkazai3_tyougouryou1
     */
    public Integer getTenkazai3_tyougouryou1() {
        return tenkazai3_tyougouryou1;
    }

    /**
     * 添加材③_調合量1
     * @param tenkazai3_tyougouryou1 the tenkazai3_tyougouryou1 to set
     */
    public void setTenkazai3_tyougouryou1(Integer tenkazai3_tyougouryou1) {
        this.tenkazai3_tyougouryou1 = tenkazai3_tyougouryou1;
    }

    /**
     * 添加材③_部材在庫No2
     * @return the tenkazai3_buzaizaikolotno2
     */
    public String getTenkazai3_buzaizaikolotno2() {
        return tenkazai3_buzaizaikolotno2;
    }

    /**
     * 添加材③_部材在庫No2
     * @param tenkazai3_buzaizaikolotno2 the tenkazai3_buzaizaikolotno2 to set
     */
    public void setTenkazai3_buzaizaikolotno2(String tenkazai3_buzaizaikolotno2) {
        this.tenkazai3_buzaizaikolotno2 = tenkazai3_buzaizaikolotno2;
    }

    /**
     * 添加材③_調合量2
     * @return the tenkazai3_tyougouryou2
     */
    public Integer getTenkazai3_tyougouryou2() {
        return tenkazai3_tyougouryou2;
    }

    /**
     * 添加材③_調合量2
     * @param tenkazai3_tyougouryou2 the tenkazai3_tyougouryou2 to set
     */
    public void setTenkazai3_tyougouryou2(Integer tenkazai3_tyougouryou2) {
        this.tenkazai3_tyougouryou2 = tenkazai3_tyougouryou2;
    }

    /**
     * 添加材④_材料品名
     * @return the tenkazai4_zairyouhinmei
     */
    public String getTenkazai4_zairyouhinmei() {
        return tenkazai4_zairyouhinmei;
    }

    /**
     * 添加材④_材料品名
     * @param tenkazai4_zairyouhinmei the tenkazai4_zairyouhinmei to set
     */
    public void setTenkazai4_zairyouhinmei(String tenkazai4_zairyouhinmei) {
        this.tenkazai4_zairyouhinmei = tenkazai4_zairyouhinmei;
    }

    /**
     * 添加材④_調合量規格
     * @return the tenkazai4_tyougouryoukikaku
     */
    public String getTenkazai4_tyougouryoukikaku() {
        return tenkazai4_tyougouryoukikaku;
    }

    /**
     * 添加材④_調合量規格
     * @param tenkazai4_tyougouryoukikaku the tenkazai4_tyougouryoukikaku to set
     */
    public void setTenkazai4_tyougouryoukikaku(String tenkazai4_tyougouryoukikaku) {
        this.tenkazai4_tyougouryoukikaku = tenkazai4_tyougouryoukikaku;
    }

    /**
     * 添加材④_部材在庫No1
     * @return the tenkazai4_buzaizaikolotno1
     */
    public String getTenkazai4_buzaizaikolotno1() {
        return tenkazai4_buzaizaikolotno1;
    }

    /**
     * 添加材④_部材在庫No1
     * @param tenkazai4_buzaizaikolotno1 the tenkazai4_buzaizaikolotno1 to set
     */
    public void setTenkazai4_buzaizaikolotno1(String tenkazai4_buzaizaikolotno1) {
        this.tenkazai4_buzaizaikolotno1 = tenkazai4_buzaizaikolotno1;
    }

    /**
     * 添加材④_調合量1
     * @return the tenkazai4_tyougouryou1
     */
    public Integer getTenkazai4_tyougouryou1() {
        return tenkazai4_tyougouryou1;
    }

    /**
     * 添加材④_調合量1
     * @param tenkazai4_tyougouryou1 the tenkazai4_tyougouryou1 to set
     */
    public void setTenkazai4_tyougouryou1(Integer tenkazai4_tyougouryou1) {
        this.tenkazai4_tyougouryou1 = tenkazai4_tyougouryou1;
    }

    /**
     * 添加材④_部材在庫No2
     * @return the tenkazai4_buzaizaikolotno2
     */
    public String getTenkazai4_buzaizaikolotno2() {
        return tenkazai4_buzaizaikolotno2;
    }

    /**
     * 添加材④_部材在庫No2
     * @param tenkazai4_buzaizaikolotno2 the tenkazai4_buzaizaikolotno2 to set
     */
    public void setTenkazai4_buzaizaikolotno2(String tenkazai4_buzaizaikolotno2) {
        this.tenkazai4_buzaizaikolotno2 = tenkazai4_buzaizaikolotno2;
    }

    /**
     * 添加材④_調合量2
     * @return the tenkazai4_tyougouryou2
     */
    public Integer getTenkazai4_tyougouryou2() {
        return tenkazai4_tyougouryou2;
    }

    /**
     * 添加材④_調合量2
     * @param tenkazai4_tyougouryou2 the tenkazai4_tyougouryou2 to set
     */
    public void setTenkazai4_tyougouryou2(Integer tenkazai4_tyougouryou2) {
        this.tenkazai4_tyougouryou2 = tenkazai4_tyougouryou2;
    }

    /**
     * 添加材⑤_材料品名
     * @return the tenkazai5_zairyouhinmei
     */
    public String getTenkazai5_zairyouhinmei() {
        return tenkazai5_zairyouhinmei;
    }

    /**
     * 添加材⑤_材料品名
     * @param tenkazai5_zairyouhinmei the tenkazai5_zairyouhinmei to set
     */
    public void setTenkazai5_zairyouhinmei(String tenkazai5_zairyouhinmei) {
        this.tenkazai5_zairyouhinmei = tenkazai5_zairyouhinmei;
    }

    /**
     * 添加材⑤_調合量規格
     * @return the tenkazai5_tyougouryoukikaku
     */
    public String getTenkazai5_tyougouryoukikaku() {
        return tenkazai5_tyougouryoukikaku;
    }

    /**
     * 添加材⑤_調合量規格
     * @param tenkazai5_tyougouryoukikaku the tenkazai5_tyougouryoukikaku to set
     */
    public void setTenkazai5_tyougouryoukikaku(String tenkazai5_tyougouryoukikaku) {
        this.tenkazai5_tyougouryoukikaku = tenkazai5_tyougouryoukikaku;
    }

    /**
     * 添加材⑤_部材在庫No1
     * @return the tenkazai5_buzaizaikolotno1
     */
    public String getTenkazai5_buzaizaikolotno1() {
        return tenkazai5_buzaizaikolotno1;
    }

    /**
     * 添加材⑤_部材在庫No1
     * @param tenkazai5_buzaizaikolotno1 the tenkazai5_buzaizaikolotno1 to set
     */
    public void setTenkazai5_buzaizaikolotno1(String tenkazai5_buzaizaikolotno1) {
        this.tenkazai5_buzaizaikolotno1 = tenkazai5_buzaizaikolotno1;
    }

    /**
     * 添加材⑤_調合量1
     * @return the tenkazai5_tyougouryou1
     */
    public Integer getTenkazai5_tyougouryou1() {
        return tenkazai5_tyougouryou1;
    }

    /**
     * 添加材⑤_調合量1
     * @param tenkazai5_tyougouryou1 the tenkazai5_tyougouryou1 to set
     */
    public void setTenkazai5_tyougouryou1(Integer tenkazai5_tyougouryou1) {
        this.tenkazai5_tyougouryou1 = tenkazai5_tyougouryou1;
    }

    /**
     * 添加材⑤_部材在庫No2
     * @return the tenkazai5_buzaizaikolotno2
     */
    public String getTenkazai5_buzaizaikolotno2() {
        return tenkazai5_buzaizaikolotno2;
    }

    /**
     * 添加材⑤_部材在庫No2
     * @param tenkazai5_buzaizaikolotno2 the tenkazai5_buzaizaikolotno2 to set
     */
    public void setTenkazai5_buzaizaikolotno2(String tenkazai5_buzaizaikolotno2) {
        this.tenkazai5_buzaizaikolotno2 = tenkazai5_buzaizaikolotno2;
    }

    /**
     * 添加材⑤_調合量2
     * @return the tenkazai5_tyougouryou2
     */
    public Integer getTenkazai5_tyougouryou2() {
        return tenkazai5_tyougouryou2;
    }

    /**
     * 添加材⑤_調合量2
     * @param tenkazai5_tyougouryou2 the tenkazai5_tyougouryou2 to set
     */
    public void setTenkazai5_tyougouryou2(Integer tenkazai5_tyougouryou2) {
        this.tenkazai5_tyougouryou2 = tenkazai5_tyougouryou2;
    }

    /**
     * ｿﾞﾙ①_材料品名
     * @return the sol1_zairyouhinmei
     */
    public String getSol1_zairyouhinmei() {
        return sol1_zairyouhinmei;
    }

    /**
     * ｿﾞﾙ①_材料品名
     * @param sol1_zairyouhinmei the sol1_zairyouhinmei to set
     */
    public void setSol1_zairyouhinmei(String sol1_zairyouhinmei) {
        this.sol1_zairyouhinmei = sol1_zairyouhinmei;
    }

    /**
     * ｿﾞﾙ①_調合量規格
     * @return the sol1_tyougouryoukikaku
     */
    public String getSol1_tyougouryoukikaku() {
        return sol1_tyougouryoukikaku;
    }

    /**
     * ｿﾞﾙ①_調合量規格
     * @param sol1_tyougouryoukikaku the sol1_tyougouryoukikaku to set
     */
    public void setSol1_tyougouryoukikaku(String sol1_tyougouryoukikaku) {
        this.sol1_tyougouryoukikaku = sol1_tyougouryoukikaku;
    }

    /**
     * ｿﾞﾙ①_部材在庫No1
     * @return the sol1_buzaizaikolotno1
     */
    public String getSol1_buzaizaikolotno1() {
        return sol1_buzaizaikolotno1;
    }

    /**
     * ｿﾞﾙ①_部材在庫No1
     * @param sol1_buzaizaikolotno1 the sol1_buzaizaikolotno1 to set
     */
    public void setSol1_buzaizaikolotno1(String sol1_buzaizaikolotno1) {
        this.sol1_buzaizaikolotno1 = sol1_buzaizaikolotno1;
    }

    /**
     * ｿﾞﾙ①_調合量1
     * @return the sol1_tyougouryou1
     */
    public Integer getSol1_tyougouryou1() {
        return sol1_tyougouryou1;
    }

    /**
     * ｿﾞﾙ①_調合量1
     * @param sol1_tyougouryou1 the sol1_tyougouryou1 to set
     */
    public void setSol1_tyougouryou1(Integer sol1_tyougouryou1) {
        this.sol1_tyougouryou1 = sol1_tyougouryou1;
    }

    /**
     * ｿﾞﾙ①_部材在庫No2
     * @return the sol1_buzaizaikolotno2
     */
    public String getSol1_buzaizaikolotno2() {
        return sol1_buzaizaikolotno2;
    }

    /**
     * ｿﾞﾙ①_部材在庫No2
     * @param sol1_buzaizaikolotno2 the sol1_buzaizaikolotno2 to set
     */
    public void setSol1_buzaizaikolotno2(String sol1_buzaizaikolotno2) {
        this.sol1_buzaizaikolotno2 = sol1_buzaizaikolotno2;
    }

    /**
     * ｿﾞﾙ①_調合量2
     * @return the sol1_tyougouryou2
     */
    public Integer getSol1_tyougouryou2() {
        return sol1_tyougouryou2;
    }

    /**
     * ｿﾞﾙ①_調合量2
     * @param sol1_tyougouryou2 the sol1_tyougouryou2 to set
     */
    public void setSol1_tyougouryou2(Integer sol1_tyougouryou2) {
        this.sol1_tyougouryou2 = sol1_tyougouryou2;
    }

    /**
     * ｿﾞﾙ②_材料品名
     * @return the sol2_zairyouhinmei
     */
    public String getSol2_zairyouhinmei() {
        return sol2_zairyouhinmei;
    }

    /**
     * ｿﾞﾙ②_材料品名
     * @param sol2_zairyouhinmei the sol2_zairyouhinmei to set
     */
    public void setSol2_zairyouhinmei(String sol2_zairyouhinmei) {
        this.sol2_zairyouhinmei = sol2_zairyouhinmei;
    }

    /**
     * ｿﾞﾙ②_調合量規格
     * @return the sol2_tyougouryoukikaku
     */
    public String getSol2_tyougouryoukikaku() {
        return sol2_tyougouryoukikaku;
    }

    /**
     * ｿﾞﾙ②_調合量規格
     * @param sol2_tyougouryoukikaku the sol2_tyougouryoukikaku to set
     */
    public void setSol2_tyougouryoukikaku(String sol2_tyougouryoukikaku) {
        this.sol2_tyougouryoukikaku = sol2_tyougouryoukikaku;
    }

    /**
     * ｿﾞﾙ②_部材在庫No1
     * @return the sol2_buzaizaikolotno1
     */
    public String getSol2_buzaizaikolotno1() {
        return sol2_buzaizaikolotno1;
    }

    /**
     * ｿﾞﾙ②_部材在庫No1
     * @param sol2_buzaizaikolotno1 the sol2_buzaizaikolotno1 to set
     */
    public void setSol2_buzaizaikolotno1(String sol2_buzaizaikolotno1) {
        this.sol2_buzaizaikolotno1 = sol2_buzaizaikolotno1;
    }

    /**
     * ｿﾞﾙ②_調合量1
     * @return the sol2_tyougouryou1
     */
    public Integer getSol2_tyougouryou1() {
        return sol2_tyougouryou1;
    }

    /**
     * ｿﾞﾙ②_調合量1
     * @param sol2_tyougouryou1 the sol2_tyougouryou1 to set
     */
    public void setSol2_tyougouryou1(Integer sol2_tyougouryou1) {
        this.sol2_tyougouryou1 = sol2_tyougouryou1;
    }

    /**
     * ｿﾞﾙ②_部材在庫No2
     * @return the sol2_buzaizaikolotno2
     */
    public String getSol2_buzaizaikolotno2() {
        return sol2_buzaizaikolotno2;
    }

    /**
     * ｿﾞﾙ②_部材在庫No2
     * @param sol2_buzaizaikolotno2 the sol2_buzaizaikolotno2 to set
     */
    public void setSol2_buzaizaikolotno2(String sol2_buzaizaikolotno2) {
        this.sol2_buzaizaikolotno2 = sol2_buzaizaikolotno2;
    }

    /**
     * ｿﾞﾙ②_調合量2
     * @return the sol2_tyougouryou2
     */
    public Integer getSol2_tyougouryou2() {
        return sol2_tyougouryou2;
    }

    /**
     * ｿﾞﾙ②_調合量2
     * @param sol2_tyougouryou2 the sol2_tyougouryou2 to set
     */
    public void setSol2_tyougouryou2(Integer sol2_tyougouryou2) {
        this.sol2_tyougouryou2 = sol2_tyougouryou2;
    }

    /**
     * ｿﾞﾙ③_材料品名
     * @return the sol3_zairyouhinmei
     */
    public String getSol3_zairyouhinmei() {
        return sol3_zairyouhinmei;
    }

    /**
     * ｿﾞﾙ③_材料品名
     * @param sol3_zairyouhinmei the sol3_zairyouhinmei to set
     */
    public void setSol3_zairyouhinmei(String sol3_zairyouhinmei) {
        this.sol3_zairyouhinmei = sol3_zairyouhinmei;
    }

    /**
     * ｿﾞﾙ③_調合量規格
     * @return the sol3_tyougouryoukikaku
     */
    public String getSol3_tyougouryoukikaku() {
        return sol3_tyougouryoukikaku;
    }

    /**
     * ｿﾞﾙ③_調合量規格
     * @param sol3_tyougouryoukikaku the sol3_tyougouryoukikaku to set
     */
    public void setSol3_tyougouryoukikaku(String sol3_tyougouryoukikaku) {
        this.sol3_tyougouryoukikaku = sol3_tyougouryoukikaku;
    }

    /**
     * ｿﾞﾙ③_部材在庫No1
     * @return the sol3_buzaizaikolotno1
     */
    public String getSol3_buzaizaikolotno1() {
        return sol3_buzaizaikolotno1;
    }

    /**
     * ｿﾞﾙ③_部材在庫No1
     * @param sol3_buzaizaikolotno1 the sol3_buzaizaikolotno1 to set
     */
    public void setSol3_buzaizaikolotno1(String sol3_buzaizaikolotno1) {
        this.sol3_buzaizaikolotno1 = sol3_buzaizaikolotno1;
    }

    /**
     * ｿﾞﾙ③_調合量1
     * @return the sol3_tyougouryou1
     */
    public Integer getSol3_tyougouryou1() {
        return sol3_tyougouryou1;
    }

    /**
     * ｿﾞﾙ③_調合量1
     * @param sol3_tyougouryou1 the sol3_tyougouryou1 to set
     */
    public void setSol3_tyougouryou1(Integer sol3_tyougouryou1) {
        this.sol3_tyougouryou1 = sol3_tyougouryou1;
    }

    /**
     * ｿﾞﾙ③_部材在庫No2
     * @return the sol3_buzaizaikolotno2
     */
    public String getSol3_buzaizaikolotno2() {
        return sol3_buzaizaikolotno2;
    }

    /**
     * ｿﾞﾙ③_部材在庫No2
     * @param sol3_buzaizaikolotno2 the sol3_buzaizaikolotno2 to set
     */
    public void setSol3_buzaizaikolotno2(String sol3_buzaizaikolotno2) {
        this.sol3_buzaizaikolotno2 = sol3_buzaizaikolotno2;
    }

    /**
     * ｿﾞﾙ③_調合量2
     * @return the sol3_tyougouryou2
     */
    public Integer getSol3_tyougouryou2() {
        return sol3_tyougouryou2;
    }

    /**
     * ｿﾞﾙ③_調合量2
     * @param sol3_tyougouryou2 the sol3_tyougouryou2 to set
     */
    public void setSol3_tyougouryou2(Integer sol3_tyougouryou2) {
        this.sol3_tyougouryou2 = sol3_tyougouryou2;
    }

    /**
     * ｿﾞﾙ④_材料品名
     * @return the sol4_zairyouhinmei
     */
    public String getSol4_zairyouhinmei() {
        return sol4_zairyouhinmei;
    }

    /**
     * ｿﾞﾙ④_材料品名
     * @param sol4_zairyouhinmei the sol4_zairyouhinmei to set
     */
    public void setSol4_zairyouhinmei(String sol4_zairyouhinmei) {
        this.sol4_zairyouhinmei = sol4_zairyouhinmei;
    }

    /**
     * ｿﾞﾙ④_調合量規格
     * @return the sol4_tyougouryoukikaku
     */
    public String getSol4_tyougouryoukikaku() {
        return sol4_tyougouryoukikaku;
    }

    /**
     * ｿﾞﾙ④_調合量規格
     * @param sol4_tyougouryoukikaku the sol4_tyougouryoukikaku to set
     */
    public void setSol4_tyougouryoukikaku(String sol4_tyougouryoukikaku) {
        this.sol4_tyougouryoukikaku = sol4_tyougouryoukikaku;
    }

    /**
     * ｿﾞﾙ④_部材在庫No1
     * @return the sol4_buzaizaikolotno1
     */
    public String getSol4_buzaizaikolotno1() {
        return sol4_buzaizaikolotno1;
    }

    /**
     * ｿﾞﾙ④_部材在庫No1
     * @param sol4_buzaizaikolotno1 the sol4_buzaizaikolotno1 to set
     */
    public void setSol4_buzaizaikolotno1(String sol4_buzaizaikolotno1) {
        this.sol4_buzaizaikolotno1 = sol4_buzaizaikolotno1;
    }

    /**
     * ｿﾞﾙ④_調合量1
     * @return the sol4_tyougouryou1
     */
    public Integer getSol4_tyougouryou1() {
        return sol4_tyougouryou1;
    }

    /**
     * ｿﾞﾙ④_調合量1
     * @param sol4_tyougouryou1 the sol4_tyougouryou1 to set
     */
    public void setSol4_tyougouryou1(Integer sol4_tyougouryou1) {
        this.sol4_tyougouryou1 = sol4_tyougouryou1;
    }

    /**
     * ｿﾞﾙ④_部材在庫No2
     * @return the sol4_buzaizaikolotno2
     */
    public String getSol4_buzaizaikolotno2() {
        return sol4_buzaizaikolotno2;
    }

    /**
     * ｿﾞﾙ④_部材在庫No2
     * @param sol4_buzaizaikolotno2 the sol4_buzaizaikolotno2 to set
     */
    public void setSol4_buzaizaikolotno2(String sol4_buzaizaikolotno2) {
        this.sol4_buzaizaikolotno2 = sol4_buzaizaikolotno2;
    }

    /**
     * ｿﾞﾙ④_調合量2
     * @return the sol4_tyougouryou2
     */
    public Integer getSol4_tyougouryou2() {
        return sol4_tyougouryou2;
    }

    /**
     * ｿﾞﾙ④_調合量2
     * @param sol4_tyougouryou2 the sol4_tyougouryou2 to set
     */
    public void setSol4_tyougouryou2(Integer sol4_tyougouryou2) {
        this.sol4_tyougouryou2 = sol4_tyougouryou2;
    }

    /**
     * 秤量終了日時
     * @return the hyouryousyuuryounichiji
     */
    public Timestamp getHyouryousyuuryounichiji() {
        return hyouryousyuuryounichiji;
    }

    /**
     * 秤量終了日時
     * @param hyouryousyuuryounichiji the hyouryousyuuryounichiji to set
     */
    public void setHyouryousyuuryounichiji(Timestamp hyouryousyuuryounichiji) {
        this.hyouryousyuuryounichiji = hyouryousyuuryounichiji;
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