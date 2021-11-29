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
 * 変更日       2021/10/15<br>
 * 計画書No     MB2101-DK002<br>
 * 変更者       KCSS K.Jo<br>
 * 変更理由     新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 添加材ｽﾗﾘｰ作製・溶剤調合履歴検索画面のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/10/15
 */
public class GXHDO202B009Model implements Serializable {
    /** WIPﾛｯﾄNo */
    private String lotno = "";

    /** 添加材ｽﾗﾘｰ品名 */
    private String tenkazaislurryhinmei = "";

    /** 添加材ｽﾗﾘｰLotNo */
    private String tenkazaislurrylotno = "";

    /** ﾛｯﾄ区分 */
    private String lotkubun = "";

    /** 秤量号機 */
    private String hyouryougouki = "";

    /** 秤量開始日時 */
    private Timestamp hyouryoukaisinichiji = null;

    /** 分散材①_材料品名 */
    private String bunsanzai1_zairyouhinmei = "";

    /** 分散材①_調合量規格 */
    private String bunsanzai1_tyougouryoukikaku = "";

    /** 分散材①_部材在庫No1 */
    private String bunsanzai1_buzaizaikolotno1 = "";

    /** 分散材①_調合量1 */
    private Integer bunsanzai1_tyougouryou1 = null;

    /** 分散材①_部材在庫No2 */
    private String bunsanzai1_buzaizaikolotno2 = "";

    /** 分散材①_調合量2 */
    private Integer bunsanzai1_tyougouryou2 = null;

    /** 分散材②_材料品名 */
    private String bunsanzai2_zairyouhinmei = "";

    /** 分散材②_調合量規格 */
    private String bunsanzai2_tyougouryoukikaku = "";

    /** 分散材②_部材在庫No1 */
    private String bunsanzai2_buzaizaikolotno1 = "";

    /** 分散材②_調合量1 */
    private Integer bunsanzai2_tyougouryou1 = null;

    /** 分散材②_部材在庫No2 */
    private String bunsanzai2_buzaizaikolotno2 = "";

    /** 分散材②_調合量2 */
    private Integer bunsanzai2_tyougouryou2 = null;

    /** 溶剤①_材料品名 */
    private String youzai1_zairyouhinmei = "";

    /** 溶剤①_調合量規格 */
    private String youzai1_tyougouryoukikaku = "";

    /** 溶剤①_部材在庫No1 */
    private String youzai1_buzaizaikolotno1 = "";

    /** 溶剤①_調合量1 */
    private Integer youzai1_tyougouryou1 = null;

    /** 溶剤①_部材在庫No2 */
    private String youzai1_buzaizaikolotno2 = "";

    /** 溶剤①_調合量2 */
    private Integer youzai1_tyougouryou2 = null;

    /** 溶剤②_材料品名 */
    private String youzai2_zairyouhinmei = "";

    /** 溶剤②_調合量規格 */
    private String youzai2_tyougouryoukikaku = "";

    /** 溶剤②_部材在庫No1 */
    private String youzai2_buzaizaikolotno1 = "";

    /** 溶剤②_調合量1 */
    private Integer youzai2_tyougouryou1 = null;

    /** 溶剤②_部材在庫No2 */
    private String youzai2_buzaizaikolotno2 = "";

    /** 溶剤②_調合量2 */
    private Integer youzai2_tyougouryou2 = null;

    /** 溶剤③_材料品名 */
    private String youzai3_zairyouhinmei = "";

    /** 溶剤③_調合量規格 */
    private String youzai3_tyougouryoukikaku = "";

    /** 溶剤③_部材在庫No1 */
    private String youzai3_buzaizaikolotno1 = "";

    /** 溶剤③_調合量1 */
    private Integer youzai3_tyougouryou1 = null;

    /** 溶剤③_部材在庫No2 */
    private String youzai3_buzaizaikolotno2 = "";

    /** 溶剤③_調合量2 */
    private Integer youzai3_tyougouryou2 = null;

    /** 溶剤④_材料品名 */
    private String youzai4_zairyouhinmei = "";

    /** 溶剤④_調合量規格 */
    private String youzai4_tyougouryoukikaku = "";

    /** 溶剤④_部材在庫No1 */
    private String youzai4_buzaizaikolotno1 = "";

    /** 溶剤④_調合量1 */
    private Integer youzai4_tyougouryou1 = null;

    /** 溶剤④_部材在庫No2 */
    private String youzai4_buzaizaikolotno2 = "";

    /** 溶剤④_調合量2 */
    private Integer youzai4_tyougouryou2 = null;

    /** ｶﾞﾗｽｽﾗﾘｰ品名 */
    private String glassslurryhinmei = "";

    /** ｶﾞﾗｽｽﾗﾘｰ調合量規格 */
    private String glassslurrytyougouryoukikaku = "";

    /** ｶﾞﾗｽｽﾗﾘｰLotNo */
    private String glassslurrylotno = "";

    /** ｶﾞﾗｽｽﾗﾘｰ調合量 */
    private Integer glassslurrytyougouryou = null;

    /** 秤量終了日時 */
    private Timestamp hyouryousyuuryounichiji = null;

    /** 撹拌機 */
    private String kakuhanki = "";

    /** 撹拌時間 */
    private Integer kakuhanjikan = null;

    /** 撹拌開始日時 */
    private Timestamp kakuhankaisinichiji = null;

    /** 撹拌終了日時 */
    private Timestamp kakuhansyuuryounichiji = null;

    /** 担当者 */
    private String tantousya = "";

    /** 確認者 */
    private String kakuninsya = "";

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
     * 添加材ｽﾗﾘｰ品名
     * @return the tenkazaislurryhinmei
     */
    public String getTenkazaislurryhinmei() {
        return tenkazaislurryhinmei;
    }

    /**
     * 添加材ｽﾗﾘｰ品名
     * @param tenkazaislurryhinmei the tenkazaislurryhinmei to set
     */
    public void setTenkazaislurryhinmei(String tenkazaislurryhinmei) {
        this.tenkazaislurryhinmei = tenkazaislurryhinmei;
    }

    /**
     * 添加材ｽﾗﾘｰLotNo
     * @return the tenkazaislurrylotno
     */
    public String getTenkazaislurrylotno() {
        return tenkazaislurrylotno;
    }

    /**
     * 添加材ｽﾗﾘｰLotNo
     * @param tenkazaislurrylotno the tenkazaislurrylotno to set
     */
    public void setTenkazaislurrylotno(String tenkazaislurrylotno) {
        this.tenkazaislurrylotno = tenkazaislurrylotno;
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
     * 秤量号機
     * @return the hyouryougouki
     */
    public String getHyouryougouki() {
        return hyouryougouki;
    }

    /**
     * 秤量号機
     * @param hyouryougouki the hyouryougouki to set
     */
    public void setHyouryougouki(String hyouryougouki) {
        this.hyouryougouki = hyouryougouki;
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
     * 分散材①_材料品名
     * @return the bunsanzai1_zairyouhinmei
     */
    public String getBunsanzai1_zairyouhinmei() {
        return bunsanzai1_zairyouhinmei;
    }

    /**
     * 分散材①_材料品名
     * @param bunsanzai1_zairyouhinmei the bunsanzai1_zairyouhinmei to set
     */
    public void setBunsanzai1_zairyouhinmei(String bunsanzai1_zairyouhinmei) {
        this.bunsanzai1_zairyouhinmei = bunsanzai1_zairyouhinmei;
    }

    /**
     * 分散材①_調合量規格
     * @return the bunsanzai1_tyougouryoukikaku
     */
    public String getBunsanzai1_tyougouryoukikaku() {
        return bunsanzai1_tyougouryoukikaku;
    }

    /**
     * 分散材①_調合量規格
     * @param bunsanzai1_tyougouryoukikaku the bunsanzai1_tyougouryoukikaku to set
     */
    public void setBunsanzai1_tyougouryoukikaku(String bunsanzai1_tyougouryoukikaku) {
        this.bunsanzai1_tyougouryoukikaku = bunsanzai1_tyougouryoukikaku;
    }

    /**
     * 分散材①_部材在庫No1
     * @return the bunsanzai1_buzaizaikolotno1
     */
    public String getBunsanzai1_buzaizaikolotno1() {
        return bunsanzai1_buzaizaikolotno1;
    }

    /**
     * 分散材①_部材在庫No1
     * @param bunsanzai1_buzaizaikolotno1 the bunsanzai1_buzaizaikolotno1 to set
     */
    public void setBunsanzai1_buzaizaikolotno1(String bunsanzai1_buzaizaikolotno1) {
        this.bunsanzai1_buzaizaikolotno1 = bunsanzai1_buzaizaikolotno1;
    }

    /**
     * 分散材①_調合量1
     * @return the bunsanzai1_tyougouryou1
     */
    public Integer getBunsanzai1_tyougouryou1() {
        return bunsanzai1_tyougouryou1;
    }

    /**
     * 分散材①_調合量1
     * @param bunsanzai1_tyougouryou1 the bunsanzai1_tyougouryou1 to set
     */
    public void setBunsanzai1_tyougouryou1(Integer bunsanzai1_tyougouryou1) {
        this.bunsanzai1_tyougouryou1 = bunsanzai1_tyougouryou1;
    }

    /**
     * 分散材①_部材在庫No2
     * @return the bunsanzai1_buzaizaikolotno2
     */
    public String getBunsanzai1_buzaizaikolotno2() {
        return bunsanzai1_buzaizaikolotno2;
    }

    /**
     * 分散材①_部材在庫No2
     * @param bunsanzai1_buzaizaikolotno2 the bunsanzai1_buzaizaikolotno2 to set
     */
    public void setBunsanzai1_buzaizaikolotno2(String bunsanzai1_buzaizaikolotno2) {
        this.bunsanzai1_buzaizaikolotno2 = bunsanzai1_buzaizaikolotno2;
    }

    /**
     * 分散材①_調合量2
     * @return the bunsanzai1_tyougouryou2
     */
    public Integer getBunsanzai1_tyougouryou2() {
        return bunsanzai1_tyougouryou2;
    }

    /**
     * 分散材①_調合量2
     * @param bunsanzai1_tyougouryou2 the bunsanzai1_tyougouryou2 to set
     */
    public void setBunsanzai1_tyougouryou2(Integer bunsanzai1_tyougouryou2) {
        this.bunsanzai1_tyougouryou2 = bunsanzai1_tyougouryou2;
    }

    /**
     * 分散材②_材料品名
     * @return the bunsanzai2_zairyouhinmei
     */
    public String getBunsanzai2_zairyouhinmei() {
        return bunsanzai2_zairyouhinmei;
    }

    /**
     * 分散材②_材料品名
     * @param bunsanzai2_zairyouhinmei the bunsanzai2_zairyouhinmei to set
     */
    public void setBunsanzai2_zairyouhinmei(String bunsanzai2_zairyouhinmei) {
        this.bunsanzai2_zairyouhinmei = bunsanzai2_zairyouhinmei;
    }

    /**
     * 分散材②_調合量規格
     * @return the bunsanzai2_tyougouryoukikaku
     */
    public String getBunsanzai2_tyougouryoukikaku() {
        return bunsanzai2_tyougouryoukikaku;
    }

    /**
     * 分散材②_調合量規格
     * @param bunsanzai2_tyougouryoukikaku the bunsanzai2_tyougouryoukikaku to set
     */
    public void setBunsanzai2_tyougouryoukikaku(String bunsanzai2_tyougouryoukikaku) {
        this.bunsanzai2_tyougouryoukikaku = bunsanzai2_tyougouryoukikaku;
    }

    /**
     * 分散材②_部材在庫No1
     * @return the bunsanzai2_buzaizaikolotno1
     */
    public String getBunsanzai2_buzaizaikolotno1() {
        return bunsanzai2_buzaizaikolotno1;
    }

    /**
     * 分散材②_部材在庫No1
     * @param bunsanzai2_buzaizaikolotno1 the bunsanzai2_buzaizaikolotno1 to set
     */
    public void setBunsanzai2_buzaizaikolotno1(String bunsanzai2_buzaizaikolotno1) {
        this.bunsanzai2_buzaizaikolotno1 = bunsanzai2_buzaizaikolotno1;
    }

    /**
     * 分散材②_調合量1
     * @return the bunsanzai2_tyougouryou1
     */
    public Integer getBunsanzai2_tyougouryou1() {
        return bunsanzai2_tyougouryou1;
    }

    /**
     * 分散材②_調合量1
     * @param bunsanzai2_tyougouryou1 the bunsanzai2_tyougouryou1 to set
     */
    public void setBunsanzai2_tyougouryou1(Integer bunsanzai2_tyougouryou1) {
        this.bunsanzai2_tyougouryou1 = bunsanzai2_tyougouryou1;
    }

    /**
     * 分散材②_部材在庫No2
     * @return the bunsanzai2_buzaizaikolotno2
     */
    public String getBunsanzai2_buzaizaikolotno2() {
        return bunsanzai2_buzaizaikolotno2;
    }

    /**
     * 分散材②_部材在庫No2
     * @param bunsanzai2_buzaizaikolotno2 the bunsanzai2_buzaizaikolotno2 to set
     */
    public void setBunsanzai2_buzaizaikolotno2(String bunsanzai2_buzaizaikolotno2) {
        this.bunsanzai2_buzaizaikolotno2 = bunsanzai2_buzaizaikolotno2;
    }

    /**
     * 分散材②_調合量2
     * @return the bunsanzai2_tyougouryou2
     */
    public Integer getBunsanzai2_tyougouryou2() {
        return bunsanzai2_tyougouryou2;
    }

    /**
     * 分散材②_調合量2
     * @param bunsanzai2_tyougouryou2 the bunsanzai2_tyougouryou2 to set
     */
    public void setBunsanzai2_tyougouryou2(Integer bunsanzai2_tyougouryou2) {
        this.bunsanzai2_tyougouryou2 = bunsanzai2_tyougouryou2;
    }

    /**
     * 溶剤①_材料品名
     * @return the youzai1_zairyouhinmei
     */
    public String getYouzai1_zairyouhinmei() {
        return youzai1_zairyouhinmei;
    }

    /**
     * 溶剤①_材料品名
     * @param youzai1_zairyouhinmei the youzai1_zairyouhinmei to set
     */
    public void setYouzai1_zairyouhinmei(String youzai1_zairyouhinmei) {
        this.youzai1_zairyouhinmei = youzai1_zairyouhinmei;
    }

    /**
     * 溶剤①_調合量規格
     * @return the youzai1_tyougouryoukikaku
     */
    public String getYouzai1_tyougouryoukikaku() {
        return youzai1_tyougouryoukikaku;
    }

    /**
     * 溶剤①_調合量規格
     * @param youzai1_tyougouryoukikaku the youzai1_tyougouryoukikaku to set
     */
    public void setYouzai1_tyougouryoukikaku(String youzai1_tyougouryoukikaku) {
        this.youzai1_tyougouryoukikaku = youzai1_tyougouryoukikaku;
    }

    /**
     * 溶剤①_部材在庫No1
     * @return the youzai1_buzaizaikolotno1
     */
    public String getYouzai1_buzaizaikolotno1() {
        return youzai1_buzaizaikolotno1;
    }

    /**
     * 溶剤①_部材在庫No1
     * @param youzai1_buzaizaikolotno1 the youzai1_buzaizaikolotno1 to set
     */
    public void setYouzai1_buzaizaikolotno1(String youzai1_buzaizaikolotno1) {
        this.youzai1_buzaizaikolotno1 = youzai1_buzaizaikolotno1;
    }

    /**
     * 溶剤①_調合量1
     * @return the youzai1_tyougouryou1
     */
    public Integer getYouzai1_tyougouryou1() {
        return youzai1_tyougouryou1;
    }

    /**
     * 溶剤①_調合量1
     * @param youzai1_tyougouryou1 the youzai1_tyougouryou1 to set
     */
    public void setYouzai1_tyougouryou1(Integer youzai1_tyougouryou1) {
        this.youzai1_tyougouryou1 = youzai1_tyougouryou1;
    }

    /**
     * 溶剤①_部材在庫No2
     * @return the youzai1_buzaizaikolotno2
     */
    public String getYouzai1_buzaizaikolotno2() {
        return youzai1_buzaizaikolotno2;
    }

    /**
     * 溶剤①_部材在庫No2
     * @param youzai1_buzaizaikolotno2 the youzai1_buzaizaikolotno2 to set
     */
    public void setYouzai1_buzaizaikolotno2(String youzai1_buzaizaikolotno2) {
        this.youzai1_buzaizaikolotno2 = youzai1_buzaizaikolotno2;
    }

    /**
     * 溶剤①_調合量2
     * @return the youzai1_tyougouryou2
     */
    public Integer getYouzai1_tyougouryou2() {
        return youzai1_tyougouryou2;
    }

    /**
     * 溶剤①_調合量2
     * @param youzai1_tyougouryou2 the youzai1_tyougouryou2 to set
     */
    public void setYouzai1_tyougouryou2(Integer youzai1_tyougouryou2) {
        this.youzai1_tyougouryou2 = youzai1_tyougouryou2;
    }

    /**
     * 溶剤②_材料品名
     * @return the youzai2_zairyouhinmei
     */
    public String getYouzai2_zairyouhinmei() {
        return youzai2_zairyouhinmei;
    }

    /**
     * 溶剤②_材料品名
     * @param youzai2_zairyouhinmei the youzai2_zairyouhinmei to set
     */
    public void setYouzai2_zairyouhinmei(String youzai2_zairyouhinmei) {
        this.youzai2_zairyouhinmei = youzai2_zairyouhinmei;
    }

    /**
     * 溶剤②_調合量規格
     * @return the youzai2_tyougouryoukikaku
     */
    public String getYouzai2_tyougouryoukikaku() {
        return youzai2_tyougouryoukikaku;
    }

    /**
     * 溶剤②_調合量規格
     * @param youzai2_tyougouryoukikaku the youzai2_tyougouryoukikaku to set
     */
    public void setYouzai2_tyougouryoukikaku(String youzai2_tyougouryoukikaku) {
        this.youzai2_tyougouryoukikaku = youzai2_tyougouryoukikaku;
    }

    /**
     * 溶剤②_部材在庫No1
     * @return the youzai2_buzaizaikolotno1
     */
    public String getYouzai2_buzaizaikolotno1() {
        return youzai2_buzaizaikolotno1;
    }

    /**
     * 溶剤②_部材在庫No1
     * @param youzai2_buzaizaikolotno1 the youzai2_buzaizaikolotno1 to set
     */
    public void setYouzai2_buzaizaikolotno1(String youzai2_buzaizaikolotno1) {
        this.youzai2_buzaizaikolotno1 = youzai2_buzaizaikolotno1;
    }

    /**
     * 溶剤②_調合量1
     * @return the youzai2_tyougouryou1
     */
    public Integer getYouzai2_tyougouryou1() {
        return youzai2_tyougouryou1;
    }

    /**
     * 溶剤②_調合量1
     * @param youzai2_tyougouryou1 the youzai2_tyougouryou1 to set
     */
    public void setYouzai2_tyougouryou1(Integer youzai2_tyougouryou1) {
        this.youzai2_tyougouryou1 = youzai2_tyougouryou1;
    }

    /**
     * 溶剤②_部材在庫No2
     * @return the youzai2_buzaizaikolotno2
     */
    public String getYouzai2_buzaizaikolotno2() {
        return youzai2_buzaizaikolotno2;
    }

    /**
     * 溶剤②_部材在庫No2
     * @param youzai2_buzaizaikolotno2 the youzai2_buzaizaikolotno2 to set
     */
    public void setYouzai2_buzaizaikolotno2(String youzai2_buzaizaikolotno2) {
        this.youzai2_buzaizaikolotno2 = youzai2_buzaizaikolotno2;
    }

    /**
     * 溶剤②_調合量2
     * @return the youzai2_tyougouryou2
     */
    public Integer getYouzai2_tyougouryou2() {
        return youzai2_tyougouryou2;
    }

    /**
     * 溶剤②_調合量2
     * @param youzai2_tyougouryou2 the youzai2_tyougouryou2 to set
     */
    public void setYouzai2_tyougouryou2(Integer youzai2_tyougouryou2) {
        this.youzai2_tyougouryou2 = youzai2_tyougouryou2;
    }

    /**
     * 溶剤③_材料品名
     * @return the youzai3_zairyouhinmei
     */
    public String getYouzai3_zairyouhinmei() {
        return youzai3_zairyouhinmei;
    }

    /**
     * 溶剤③_材料品名
     * @param youzai3_zairyouhinmei the youzai3_zairyouhinmei to set
     */
    public void setYouzai3_zairyouhinmei(String youzai3_zairyouhinmei) {
        this.youzai3_zairyouhinmei = youzai3_zairyouhinmei;
    }

    /**
     * 溶剤③_調合量規格
     * @return the youzai3_tyougouryoukikaku
     */
    public String getYouzai3_tyougouryoukikaku() {
        return youzai3_tyougouryoukikaku;
    }

    /**
     * 溶剤③_調合量規格
     * @param youzai3_tyougouryoukikaku the youzai3_tyougouryoukikaku to set
     */
    public void setYouzai3_tyougouryoukikaku(String youzai3_tyougouryoukikaku) {
        this.youzai3_tyougouryoukikaku = youzai3_tyougouryoukikaku;
    }

    /**
     * 溶剤③_部材在庫No1
     * @return the youzai3_buzaizaikolotno1
     */
    public String getYouzai3_buzaizaikolotno1() {
        return youzai3_buzaizaikolotno1;
    }

    /**
     * 溶剤③_部材在庫No1
     * @param youzai3_buzaizaikolotno1 the youzai3_buzaizaikolotno1 to set
     */
    public void setYouzai3_buzaizaikolotno1(String youzai3_buzaizaikolotno1) {
        this.youzai3_buzaizaikolotno1 = youzai3_buzaizaikolotno1;
    }

    /**
     * 溶剤③_調合量1
     * @return the youzai3_tyougouryou1
     */
    public Integer getYouzai3_tyougouryou1() {
        return youzai3_tyougouryou1;
    }

    /**
     * 溶剤③_調合量1
     * @param youzai3_tyougouryou1 the youzai3_tyougouryou1 to set
     */
    public void setYouzai3_tyougouryou1(Integer youzai3_tyougouryou1) {
        this.youzai3_tyougouryou1 = youzai3_tyougouryou1;
    }

    /**
     * 溶剤③_部材在庫No2
     * @return the youzai3_buzaizaikolotno2
     */
    public String getYouzai3_buzaizaikolotno2() {
        return youzai3_buzaizaikolotno2;
    }

    /**
     * 溶剤③_部材在庫No2
     * @param youzai3_buzaizaikolotno2 the youzai3_buzaizaikolotno2 to set
     */
    public void setYouzai3_buzaizaikolotno2(String youzai3_buzaizaikolotno2) {
        this.youzai3_buzaizaikolotno2 = youzai3_buzaizaikolotno2;
    }

    /**
     * 溶剤③_調合量2
     * @return the youzai3_tyougouryou2
     */
    public Integer getYouzai3_tyougouryou2() {
        return youzai3_tyougouryou2;
    }

    /**
     * 溶剤③_調合量2
     * @param youzai3_tyougouryou2 the youzai3_tyougouryou2 to set
     */
    public void setYouzai3_tyougouryou2(Integer youzai3_tyougouryou2) {
        this.youzai3_tyougouryou2 = youzai3_tyougouryou2;
    }

    /**
     * 溶剤④_材料品名
     * @return the youzai4_zairyouhinmei
     */
    public String getYouzai4_zairyouhinmei() {
        return youzai4_zairyouhinmei;
    }

    /**
     * 溶剤④_材料品名
     * @param youzai4_zairyouhinmei the youzai4_zairyouhinmei to set
     */
    public void setYouzai4_zairyouhinmei(String youzai4_zairyouhinmei) {
        this.youzai4_zairyouhinmei = youzai4_zairyouhinmei;
    }

    /**
     * 溶剤④_調合量規格
     * @return the youzai4_tyougouryoukikaku
     */
    public String getYouzai4_tyougouryoukikaku() {
        return youzai4_tyougouryoukikaku;
    }

    /**
     * 溶剤④_調合量規格
     * @param youzai4_tyougouryoukikaku the youzai4_tyougouryoukikaku to set
     */
    public void setYouzai4_tyougouryoukikaku(String youzai4_tyougouryoukikaku) {
        this.youzai4_tyougouryoukikaku = youzai4_tyougouryoukikaku;
    }

    /**
     * 溶剤④_部材在庫No1
     * @return the youzai4_buzaizaikolotno1
     */
    public String getYouzai4_buzaizaikolotno1() {
        return youzai4_buzaizaikolotno1;
    }

    /**
     * 溶剤④_部材在庫No1
     * @param youzai4_buzaizaikolotno1 the youzai4_buzaizaikolotno1 to set
     */
    public void setYouzai4_buzaizaikolotno1(String youzai4_buzaizaikolotno1) {
        this.youzai4_buzaizaikolotno1 = youzai4_buzaizaikolotno1;
    }

    /**
     * 溶剤④_調合量1
     * @return the youzai4_tyougouryou1
     */
    public Integer getYouzai4_tyougouryou1() {
        return youzai4_tyougouryou1;
    }

    /**
     * 溶剤④_調合量1
     * @param youzai4_tyougouryou1 the youzai4_tyougouryou1 to set
     */
    public void setYouzai4_tyougouryou1(Integer youzai4_tyougouryou1) {
        this.youzai4_tyougouryou1 = youzai4_tyougouryou1;
    }

    /**
     * 溶剤④_部材在庫No2
     * @return the youzai4_buzaizaikolotno2
     */
    public String getYouzai4_buzaizaikolotno2() {
        return youzai4_buzaizaikolotno2;
    }

    /**
     * 溶剤④_部材在庫No2
     * @param youzai4_buzaizaikolotno2 the youzai4_buzaizaikolotno2 to set
     */
    public void setYouzai4_buzaizaikolotno2(String youzai4_buzaizaikolotno2) {
        this.youzai4_buzaizaikolotno2 = youzai4_buzaizaikolotno2;
    }

    /**
     * 溶剤④_調合量2
     * @return the youzai4_tyougouryou2
     */
    public Integer getYouzai4_tyougouryou2() {
        return youzai4_tyougouryou2;
    }

    /**
     * 溶剤④_調合量2
     * @param youzai4_tyougouryou2 the youzai4_tyougouryou2 to set
     */
    public void setYouzai4_tyougouryou2(Integer youzai4_tyougouryou2) {
        this.youzai4_tyougouryou2 = youzai4_tyougouryou2;
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ品名
     * @return the glassslurryhinmei
     */
    public String getGlassslurryhinmei() {
        return glassslurryhinmei;
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ品名
     * @param glassslurryhinmei the glassslurryhinmei to set
     */
    public void setGlassslurryhinmei(String glassslurryhinmei) {
        this.glassslurryhinmei = glassslurryhinmei;
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ調合量規格
     * @return the glassslurrytyougouryoukikaku
     */
    public String getGlassslurrytyougouryoukikaku() {
        return glassslurrytyougouryoukikaku;
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ調合量規格
     * @param glassslurrytyougouryoukikaku the glassslurrytyougouryoukikaku to set
     */
    public void setGlassslurrytyougouryoukikaku(String glassslurrytyougouryoukikaku) {
        this.glassslurrytyougouryoukikaku = glassslurrytyougouryoukikaku;
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰLotNo
     * @return the glassslurrylotno
     */
    public String getGlassslurrylotno() {
        return glassslurrylotno;
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰLotNo
     * @param glassslurrylotno the glassslurrylotno to set
     */
    public void setGlassslurrylotno(String glassslurrylotno) {
        this.glassslurrylotno = glassslurrylotno;
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ調合量
     * @return the glassslurrytyougouryou
     */
    public Integer getGlassslurrytyougouryou() {
        return glassslurrytyougouryou;
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ調合量
     * @param glassslurrytyougouryou the glassslurrytyougouryou to set
     */
    public void setGlassslurrytyougouryou(Integer glassslurrytyougouryou) {
        this.glassslurrytyougouryou = glassslurrytyougouryou;
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
     * 撹拌機
     * @return the kakuhanki
     */
    public String getKakuhanki() {
        return kakuhanki;
    }

    /**
     * 撹拌機
     * @param kakuhanki the kakuhanki to set
     */
    public void setKakuhanki(String kakuhanki) {
        this.kakuhanki = kakuhanki;
    }

    /**
     * 撹拌時間
     * @return the kakuhanjikan
     */
    public Integer getKakuhanjikan() {
        return kakuhanjikan;
    }

    /**
     * 撹拌時間
     * @param kakuhanjikan the kakuhanjikan to set
     */
    public void setKakuhanjikan(Integer kakuhanjikan) {
        this.kakuhanjikan = kakuhanjikan;
    }

    /**
     * 撹拌開始日時
     * @return the kakuhankaisinichiji
     */
    public Timestamp getKakuhankaisinichiji() {
        return kakuhankaisinichiji;
    }

    /**
     * 撹拌開始日時
     * @param kakuhankaisinichiji the kakuhankaisinichiji to set
     */
    public void setKakuhankaisinichiji(Timestamp kakuhankaisinichiji) {
        this.kakuhankaisinichiji = kakuhankaisinichiji;
    }

    /**
     * 撹拌終了日時
     * @return the kakuhansyuuryounichiji
     */
    public Timestamp getKakuhansyuuryounichiji() {
        return kakuhansyuuryounichiji;
    }

    /**
     * 撹拌終了日時
     * @param kakuhansyuuryounichiji the kakuhansyuuryounichiji to set
     */
    public void setKakuhansyuuryounichiji(Timestamp kakuhansyuuryounichiji) {
        this.kakuhansyuuryounichiji = kakuhansyuuryounichiji;
    }

    /**
     * 担当者
     * @return the tantousya
     */
    public String getTantousya() {
        return tantousya;
    }

    /**
     * 担当者
     * @param tantousya the tantousya to set
     */
    public void setTantousya(String tantousya) {
        this.tantousya = tantousya;
    }

    /**
     * 確認者
     * @return the kakuninsya
     */
    public String getKakuninsya() {
        return kakuninsya;
    }

    /**
     * 確認者
     * @param kakuninsya the kakuninsya to set
     */
    public void setKakuninsya(String kakuninsya) {
        this.kakuninsya = kakuninsya;
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