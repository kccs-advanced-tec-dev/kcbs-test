/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.sql.Timestamp;

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
 * SR_BINDER_YOUZAI(ﾊﾞｲﾝﾀﾞｰ溶液作製・溶剤秤量)のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/10/26
 */
public class SrBinderYouzai {
    /**
     * 工場ｺｰﾄﾞ
     */
    private String kojyo;

    /**
     * ﾛｯﾄNo
     */
    private String lotno;

    /**
     * 枝番
     */
    private String edaban;

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液品名
     */
    private String binderyouekihinmei;

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液LotNo
     */
    private String binderyouekilotno;

    /**
     * ﾛｯﾄ区分
     */
    private String lotkubun;

    /**
     * 秤量号機
     */
    private String goki;

    /**
     * 秤量開始日時
     */
    private Timestamp hyouryoukaisinichiji;

    /**
     * 溶剤①_材料品名
     */
    private String youzai1_zairyouhinmei;

    /**
     * 溶剤①_調合量規格
     */
    private String youzai1_tyougouryoukikaku;

    /**
     * 溶剤①_部材在庫No1
     */
    private String youzai1_buzaizaikono1;

    /**
     * 溶剤①_調合量1
     */
    private Integer youzai1_tyougouryou1;

    /**
     * 溶剤①_部材在庫No2
     */
    private String youzai1_buzaizaikono2;

    /**
     * 溶剤①_調合量2
     */
    private Integer youzai1_tyougouryou2;

    /**
     * 溶剤②_材料品名
     */
    private String youzai2_zairyouhinmei;

    /**
     * 溶剤②_調合量規格
     */
    private String youzai2_tyougouryoukikaku;

    /**
     * 溶剤②_部材在庫No1
     */
    private String youzai2_buzaizaikono1;

    /**
     * 溶剤②_調合量1
     */
    private Integer youzai2_tyougouryou1;

    /**
     * 溶剤②_部材在庫No2
     */
    private String youzai2_buzaizaikono2;

    /**
     * 溶剤②_調合量2
     */
    private Integer youzai2_tyougouryou2;

    /**
     * 溶剤③_材料品名
     */
    private String youzai3_zairyouhinmei;

    /**
     * 溶剤③_調合量規格
     */
    private String youzai3_tyougouryoukikaku;

    /**
     * 溶剤③_部材在庫No1
     */
    private String youzai3_buzaizaikono1;

    /**
     * 溶剤③_調合量1
     */
    private Integer youzai3_tyougouryou1;

    /**
     * 溶剤③_部材在庫No2
     */
    private String youzai3_buzaizaikono2;

    /**
     * 溶剤③_調合量2
     */
    private Integer youzai3_tyougouryou2;

    /**
     * 溶剤④_材料品名
     */
    private String youzai4_zairyouhinmei;

    /**
     * 溶剤④_調合量規格
     */
    private String youzai4_tyougouryoukikaku;

    /**
     * 溶剤④_部材在庫No1
     */
    private String youzai4_buzaizaikono1;

    /**
     * 溶剤④_調合量1
     */
    private Integer youzai4_tyougouryou1;

    /**
     * 溶剤④_部材在庫No2
     */
    private String youzai4_buzaizaikono2;

    /**
     * 溶剤④_調合量2
     */
    private Integer youzai4_tyougouryou2;

    /**
     * 溶剤⑤_材料品名
     */
    private String youzai5_zairyouhinmei;

    /**
     * 溶剤⑤_調合量規格
     */
    private String youzai5_tyougouryoukikaku;

    /**
     * 溶剤⑤_部材在庫No1
     */
    private String youzai5_buzaizaikono1;

    /**
     * 溶剤⑤_調合量1
     */
    private Integer youzai5_tyougouryou1;

    /**
     * 溶剤⑤_部材在庫No2
     */
    private String youzai5_buzaizaikono2;

    /**
     * 溶剤⑤_調合量2
     */
    private Integer youzai5_tyougouryou2;

    /**
     * 溶剤⑥_材料品名
     */
    private String youzai6_zairyouhinmei;

    /**
     * 溶剤⑥_調合量規格
     */
    private String youzai6_tyougouryoukikaku;

    /**
     * 溶剤⑥_部材在庫No1
     */
    private String youzai6_buzaizaikono1;

    /**
     * 溶剤⑥_調合量1
     */
    private Integer youzai6_tyougouryou1;

    /**
     * 溶剤⑥_部材在庫No2
     */
    private String youzai6_buzaizaikono2;

    /**
     * 溶剤⑥_調合量2
     */
    private Integer youzai6_tyougouryou2;

    /**
     * 秤量終了日時
     */
    private Timestamp hyouryousyuuryounichiji;

    /**
     * 担当者
     */
    private String tantousya;

    /**
     * 確認者
     */
    private String kakuninsya;

    /**
     * 備考1
     */
    private String bikou1;

    /**
     * 備考2
     */
    private String bikou2;

    /**
     * 登録日時
     */
    private Timestamp torokunichiji;

    /**
     * 更新日時
     */
    private Timestamp kosinnichiji;

    /**
     * revision
     */
    private Integer revision;

    /**
     * 削除ﾌﾗｸﾞ
     */
    private Integer deleteflag;

    /**
     * 工場ｺｰﾄﾞ
     * @return the kojyo
     */
    public String getKojyo() {
        return kojyo;
    }

    /**
     * 工場ｺｰﾄﾞ
     * @param kojyo the kojyo to set
     */
    public void setKojyo(String kojyo) {
        this.kojyo = kojyo;
    }

    /**
     * ﾛｯﾄNo
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * ﾛｯﾄNo
     * @param lotno the lotno to set
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * 枝番
     * @return the edaban
     */
    public String getEdaban() {
        return edaban;
    }

    /**
     * 枝番
     * @param edaban the edaban to set
     */
    public void setEdaban(String edaban) {
        this.edaban = edaban;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液品名
     * @return the binderyouekihinmei
     */
    public String getBinderyouekihinmei() {
        return binderyouekihinmei;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液品名
     * @param binderyouekihinmei the binderyouekihinmei to set
     */
    public void setBinderyouekihinmei(String binderyouekihinmei) {
        this.binderyouekihinmei = binderyouekihinmei;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液LotNo
     * @return the binderyouekilotno
     */
    public String getBinderyouekilotno() {
        return binderyouekilotno;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液LotNo
     * @param binderyouekilotno the binderyouekilotno to set
     */
    public void setBinderyouekilotno(String binderyouekilotno) {
        this.binderyouekilotno = binderyouekilotno;
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
     * @return the youzai1_buzaizaikono1
     */
    public String getYouzai1_buzaizaikono1() {
        return youzai1_buzaizaikono1;
    }

    /**
     * 溶剤①_部材在庫No1
     * @param youzai1_buzaizaikono1 the youzai1_buzaizaikono1 to set
     */
    public void setYouzai1_buzaizaikono1(String youzai1_buzaizaikono1) {
        this.youzai1_buzaizaikono1 = youzai1_buzaizaikono1;
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
     * @return the youzai1_buzaizaikono2
     */
    public String getYouzai1_buzaizaikono2() {
        return youzai1_buzaizaikono2;
    }

    /**
     * 溶剤①_部材在庫No2
     * @param youzai1_buzaizaikono2 the youzai1_buzaizaikono2 to set
     */
    public void setYouzai1_buzaizaikono2(String youzai1_buzaizaikono2) {
        this.youzai1_buzaizaikono2 = youzai1_buzaizaikono2;
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
     * @return the youzai2_buzaizaikono1
     */
    public String getYouzai2_buzaizaikono1() {
        return youzai2_buzaizaikono1;
    }

    /**
     * 溶剤②_部材在庫No1
     * @param youzai2_buzaizaikono1 the youzai2_buzaizaikono1 to set
     */
    public void setYouzai2_buzaizaikono1(String youzai2_buzaizaikono1) {
        this.youzai2_buzaizaikono1 = youzai2_buzaizaikono1;
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
     * @return the youzai2_buzaizaikono2
     */
    public String getYouzai2_buzaizaikono2() {
        return youzai2_buzaizaikono2;
    }

    /**
     * 溶剤②_部材在庫No2
     * @param youzai2_buzaizaikono2 the youzai2_buzaizaikono2 to set
     */
    public void setYouzai2_buzaizaikono2(String youzai2_buzaizaikono2) {
        this.youzai2_buzaizaikono2 = youzai2_buzaizaikono2;
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
     * @return the youzai3_buzaizaikono1
     */
    public String getYouzai3_buzaizaikono1() {
        return youzai3_buzaizaikono1;
    }

    /**
     * 溶剤③_部材在庫No1
     * @param youzai3_buzaizaikono1 the youzai3_buzaizaikono1 to set
     */
    public void setYouzai3_buzaizaikono1(String youzai3_buzaizaikono1) {
        this.youzai3_buzaizaikono1 = youzai3_buzaizaikono1;
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
     * @return the youzai3_buzaizaikono2
     */
    public String getYouzai3_buzaizaikono2() {
        return youzai3_buzaizaikono2;
    }

    /**
     * 溶剤③_部材在庫No2
     * @param youzai3_buzaizaikono2 the youzai3_buzaizaikono2 to set
     */
    public void setYouzai3_buzaizaikono2(String youzai3_buzaizaikono2) {
        this.youzai3_buzaizaikono2 = youzai3_buzaizaikono2;
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
     * @return the youzai4_buzaizaikono1
     */
    public String getYouzai4_buzaizaikono1() {
        return youzai4_buzaizaikono1;
    }

    /**
     * 溶剤④_部材在庫No1
     * @param youzai4_buzaizaikono1 the youzai4_buzaizaikono1 to set
     */
    public void setYouzai4_buzaizaikono1(String youzai4_buzaizaikono1) {
        this.youzai4_buzaizaikono1 = youzai4_buzaizaikono1;
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
     * @return the youzai4_buzaizaikono2
     */
    public String getYouzai4_buzaizaikono2() {
        return youzai4_buzaizaikono2;
    }

    /**
     * 溶剤④_部材在庫No2
     * @param youzai4_buzaizaikono2 the youzai4_buzaizaikono2 to set
     */
    public void setYouzai4_buzaizaikono2(String youzai4_buzaizaikono2) {
        this.youzai4_buzaizaikono2 = youzai4_buzaizaikono2;
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
     * 溶剤⑤_材料品名
     * @return the youzai5_zairyouhinmei
     */
    public String getYouzai5_zairyouhinmei() {
        return youzai5_zairyouhinmei;
    }

    /**
     * 溶剤⑤_材料品名
     * @param youzai5_zairyouhinmei the youzai5_zairyouhinmei to set
     */
    public void setYouzai5_zairyouhinmei(String youzai5_zairyouhinmei) {
        this.youzai5_zairyouhinmei = youzai5_zairyouhinmei;
    }

    /**
     * 溶剤⑤_調合量規格
     * @return the youzai5_tyougouryoukikaku
     */
    public String getYouzai5_tyougouryoukikaku() {
        return youzai5_tyougouryoukikaku;
    }

    /**
     * 溶剤⑤_調合量規格
     * @param youzai5_tyougouryoukikaku the youzai5_tyougouryoukikaku to set
     */
    public void setYouzai5_tyougouryoukikaku(String youzai5_tyougouryoukikaku) {
        this.youzai5_tyougouryoukikaku = youzai5_tyougouryoukikaku;
    }

    /**
     * 溶剤⑤_部材在庫No1
     * @return the youzai5_buzaizaikono1
     */
    public String getYouzai5_buzaizaikono1() {
        return youzai5_buzaizaikono1;
    }

    /**
     * 溶剤⑤_部材在庫No1
     * @param youzai5_buzaizaikono1 the youzai5_buzaizaikono1 to set
     */
    public void setYouzai5_buzaizaikono1(String youzai5_buzaizaikono1) {
        this.youzai5_buzaizaikono1 = youzai5_buzaizaikono1;
    }

    /**
     * 溶剤⑤_調合量1
     * @return the youzai5_tyougouryou1
     */
    public Integer getYouzai5_tyougouryou1() {
        return youzai5_tyougouryou1;
    }

    /**
     * 溶剤⑤_調合量1
     * @param youzai5_tyougouryou1 the youzai5_tyougouryou1 to set
     */
    public void setYouzai5_tyougouryou1(Integer youzai5_tyougouryou1) {
        this.youzai5_tyougouryou1 = youzai5_tyougouryou1;
    }

    /**
     * 溶剤⑤_部材在庫No2
     * @return the youzai5_buzaizaikono2
     */
    public String getYouzai5_buzaizaikono2() {
        return youzai5_buzaizaikono2;
    }

    /**
     * 溶剤⑤_部材在庫No2
     * @param youzai5_buzaizaikono2 the youzai5_buzaizaikono2 to set
     */
    public void setYouzai5_buzaizaikono2(String youzai5_buzaizaikono2) {
        this.youzai5_buzaizaikono2 = youzai5_buzaizaikono2;
    }

    /**
     * 溶剤⑤_調合量2
     * @return the youzai5_tyougouryou2
     */
    public Integer getYouzai5_tyougouryou2() {
        return youzai5_tyougouryou2;
    }

    /**
     * 溶剤⑤_調合量2
     * @param youzai5_tyougouryou2 the youzai5_tyougouryou2 to set
     */
    public void setYouzai5_tyougouryou2(Integer youzai5_tyougouryou2) {
        this.youzai5_tyougouryou2 = youzai5_tyougouryou2;
    }

    /**
     * 溶剤⑥_材料品名
     * @return the youzai6_zairyouhinmei
     */
    public String getYouzai6_zairyouhinmei() {
        return youzai6_zairyouhinmei;
    }

    /**
     * 溶剤⑥_材料品名
     * @param youzai6_zairyouhinmei the youzai6_zairyouhinmei to set
     */
    public void setYouzai6_zairyouhinmei(String youzai6_zairyouhinmei) {
        this.youzai6_zairyouhinmei = youzai6_zairyouhinmei;
    }

    /**
     * 溶剤⑥_調合量規格
     * @return the youzai6_tyougouryoukikaku
     */
    public String getYouzai6_tyougouryoukikaku() {
        return youzai6_tyougouryoukikaku;
    }

    /**
     * 溶剤⑥_調合量規格
     * @param youzai6_tyougouryoukikaku the youzai6_tyougouryoukikaku to set
     */
    public void setYouzai6_tyougouryoukikaku(String youzai6_tyougouryoukikaku) {
        this.youzai6_tyougouryoukikaku = youzai6_tyougouryoukikaku;
    }

    /**
     * 溶剤⑥_部材在庫No1
     * @return the youzai6_buzaizaikono1
     */
    public String getYouzai6_buzaizaikono1() {
        return youzai6_buzaizaikono1;
    }

    /**
     * 溶剤⑥_部材在庫No1
     * @param youzai6_buzaizaikono1 the youzai6_buzaizaikono1 to set
     */
    public void setYouzai6_buzaizaikono1(String youzai6_buzaizaikono1) {
        this.youzai6_buzaizaikono1 = youzai6_buzaizaikono1;
    }

    /**
     * 溶剤⑥_調合量1
     * @return the youzai6_tyougouryou1
     */
    public Integer getYouzai6_tyougouryou1() {
        return youzai6_tyougouryou1;
    }

    /**
     * 溶剤⑥_調合量1
     * @param youzai6_tyougouryou1 the youzai6_tyougouryou1 to set
     */
    public void setYouzai6_tyougouryou1(Integer youzai6_tyougouryou1) {
        this.youzai6_tyougouryou1 = youzai6_tyougouryou1;
    }

    /**
     * 溶剤⑥_部材在庫No2
     * @return the youzai6_buzaizaikono2
     */
    public String getYouzai6_buzaizaikono2() {
        return youzai6_buzaizaikono2;
    }

    /**
     * 溶剤⑥_部材在庫No2
     * @param youzai6_buzaizaikono2 the youzai6_buzaizaikono2 to set
     */
    public void setYouzai6_buzaizaikono2(String youzai6_buzaizaikono2) {
        this.youzai6_buzaizaikono2 = youzai6_buzaizaikono2;
    }

    /**
     * 溶剤⑥_調合量2
     * @return the youzai6_tyougouryou2
     */
    public Integer getYouzai6_tyougouryou2() {
        return youzai6_tyougouryou2;
    }

    /**
     * 溶剤⑥_調合量2
     * @param youzai6_tyougouryou2 the youzai6_tyougouryou2 to set
     */
    public void setYouzai6_tyougouryou2(Integer youzai6_tyougouryou2) {
        this.youzai6_tyougouryou2 = youzai6_tyougouryou2;
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

    /**
     * 登録日時
     * @return the torokunichiji
     */
    public Timestamp getTorokunichiji() {
        return torokunichiji;
    }

    /**
     * 登録日時
     * @param torokunichiji the torokunichiji to set
     */
    public void setTorokunichiji(Timestamp torokunichiji) {
        this.torokunichiji = torokunichiji;
    }

    /**
     * 更新日時
     * @return the kosinnichiji
     */
    public Timestamp getKosinnichiji() {
        return kosinnichiji;
    }

    /**
     * 更新日時
     * @param kosinnichiji the kosinnichiji to set
     */
    public void setKosinnichiji(Timestamp kosinnichiji) {
        this.kosinnichiji = kosinnichiji;
    }

    /**
     * revision
     * @return the revision
     */
    public Integer getRevision() {
        return revision;
    }

    /**
     * revision
     * @param revision the revision to set
     */
    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    /**
     * 削除ﾌﾗｸﾞ
     * @return the deleteflag
     */
    public Integer getDeleteflag() {
        return deleteflag;
    }

    /**
     * 削除ﾌﾗｸﾞ
     * @param deleteflag the deleteflag to set
     */
    public void setDeleteflag(Integer deleteflag) {
        this.deleteflag = deleteflag;
    }

}