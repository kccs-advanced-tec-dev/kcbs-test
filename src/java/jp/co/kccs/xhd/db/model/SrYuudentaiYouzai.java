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
 * 変更日	2021/11/09<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SR_YUUDENTAI_YOUZAI(誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量)のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/11/09
 */
public class SrYuudentaiYouzai {
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
     * 誘電体ｽﾗﾘｰ品名
     */
    private String yuudentaislurryhinmei;

    /**
     * 誘電体ｽﾗﾘｰLotNo
     */
    private String yuudentaislurrylotno;

    /**
     * ﾛｯﾄ区分
     */
    private String lotkubun;

    /**
     * 原料LotNo
     */
    private String genryoulotno;

    /**
     * 原料記号
     */
    private String genryoukigou;

    /**
     * 秤量号機
     */
    private String goki;

    /**
     * 溶剤秤量開始日時
     */
    private Timestamp youzaihyouryoukaisinichiji;

    /**
     * 分散材①_材料品名
     */
    private String zunsanzai1_zairyouhinmei;

    /**
     * 分散材①_調合量規格
     */
    private String zunsanzai1_tyougouryoukikaku;

    /**
     * 分散材①_部材在庫No1
     */
    private String zunsanzai1_buzaizaikolotno1;

    /**
     * 分散材①_調合量1
     */
    private Integer zunsanzai1_tyougouryou1;

    /**
     * 分散材①_部材在庫No2
     */
    private String zunsanzai1_buzaizaikolotno2;

    /**
     * 分散材①_調合量2
     */
    private Integer zunsanzai1_tyougouryou2;

    /**
     * 分散材②_材料品名
     */
    private String zunsanzai2_zairyouhinmei;

    /**
     * 分散材②_調合量規格
     */
    private String zunsanzai2_tyougouryoukikaku;

    /**
     * 分散材②_部材在庫No1
     */
    private String zunsanzai2_buzaizaikolotno1;

    /**
     * 分散材②_調合量1
     */
    private Integer zunsanzai2_tyougouryou1;

    /**
     * 分散材②_部材在庫No2
     */
    private String zunsanzai2_buzaizaikolotno2;

    /**
     * 分散材②_調合量2
     */
    private Integer zunsanzai2_tyougouryou2;

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
    private String youzai1_buzaizaikolotno1;

    /**
     * 溶剤①_調合量1
     */
    private Integer youzai1_tyougouryou1;

    /**
     * 溶剤①_部材在庫No2
     */
    private String youzai1_buzaizaikolotno2;

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
    private String youzai2_buzaizaikolotno1;

    /**
     * 溶剤②_調合量1
     */
    private Integer youzai2_tyougouryou1;

    /**
     * 溶剤②_部材在庫No2
     */
    private String youzai2_buzaizaikolotno2;

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
    private String youzai3_buzaizaikolotno1;

    /**
     * 溶剤③_調合量1
     */
    private Integer youzai3_tyougouryou1;

    /**
     * 溶剤③_部材在庫No2
     */
    private String youzai3_buzaizaikolotno2;

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
    private String youzai4_buzaizaikolotno1;

    /**
     * 溶剤④_調合量1
     */
    private Integer youzai4_tyougouryou1;

    /**
     * 溶剤④_部材在庫No2
     */
    private String youzai4_buzaizaikolotno2;

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
    private String youzai5_buzaizaikolotno1;

    /**
     * 溶剤⑤_調合量1
     */
    private Integer youzai5_tyougouryou1;

    /**
     * 溶剤⑤_部材在庫No2
     */
    private String youzai5_buzaizaikolotno2;

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
    private String youzai6_buzaizaikolotno1;

    /**
     * 溶剤⑥_調合量1
     */
    private Integer youzai6_tyougouryou1;

    /**
     * 溶剤⑥_部材在庫No2
     */
    private String youzai6_buzaizaikolotno2;

    /**
     * 溶剤⑥_調合量2
     */
    private Integer youzai6_tyougouryou2;

    /**
     * 溶剤⑦_材料品名
     */
    private String youzai7_zairyouhinmei;

    /**
     * 溶剤⑦_調合量規格
     */
    private String youzai7_tyougouryoukikaku;

    /**
     * 溶剤⑦_部材在庫No1
     */
    private String youzai7_buzaizaikolotno1;

    /**
     * 溶剤⑦_調合量1
     */
    private Integer youzai7_tyougouryou1;

    /**
     * 溶剤⑦_部材在庫No2
     */
    private String youzai7_buzaizaikolotno2;

    /**
     * 溶剤⑦_調合量2
     */
    private Integer youzai7_tyougouryou2;

    /**
     * 溶剤⑧_材料品名
     */
    private String youzai8_zairyouhinmei;

    /**
     * 溶剤⑧_調合量規格
     */
    private String youzai8_tyougouryoukikaku;

    /**
     * 溶剤⑧_部材在庫No1
     */
    private String youzai8_buzaizaikolotno1;

    /**
     * 溶剤⑧_調合量1
     */
    private Integer youzai8_tyougouryou1;

    /**
     * 溶剤⑧_部材在庫No2
     */
    private String youzai8_buzaizaikolotno2;

    /**
     * 溶剤⑧_調合量2
     */
    private Integer youzai8_tyougouryou2;

    /**
     * 溶剤⑨_材料品名
     */
    private String youzai9_zairyouhinmei;

    /**
     * 溶剤⑨_調合量規格
     */
    private String youzai9_tyougouryoukikaku;

    /**
     * 溶剤⑨_部材在庫No1
     */
    private String youzai9_buzaizaikolotno1;

    /**
     * 溶剤⑨_調合量1
     */
    private Integer youzai9_tyougouryou1;

    /**
     * 溶剤⑨_部材在庫No2
     */
    private String youzai9_buzaizaikolotno2;

    /**
     * 溶剤⑨_調合量2
     */
    private Integer youzai9_tyougouryou2;

    /**
     * 溶剤秤量終了日時
     */
    private Timestamp youzaihyouryousyuuryounichiji;

    /**
     * 撹拌機
     */
    private String kakuhanki;

    /**
     * 回転数
     */
    private String kaitensuu;

    /**
     * 撹拌時間
     */
    private String kakuhanjikan;

    /**
     * 撹拌開始日時
     */
    private Timestamp kakuhankaisinichiji;

    /**
     * 撹拌終了日時
     */
    private Timestamp kakuhansyuuryounichiji;

    /**
     * 添加材ｽﾗﾘｰ秤量開始日時
     */
    private Timestamp tenkazaislurryhyouryoukaisinichiji;

    /**
     * 添加材ｽﾗﾘｰ_材料品名
     */
    private String tenkazaislurry_zairyouhinmei;

    /**
     * 添加材ｽﾗﾘｰ_WIPﾛｯﾄNo
     */
    private String tenkazaislurry_WIPlotno;

    /**
     * 添加材ｽﾗﾘｰ_調合量規格
     */
    private String tenkazaislurry_tyougouryoukikaku;

    /**
     * 添加材ｽﾗﾘｰ_風袋重量1
     */
    private Integer tenkazaislurry_fuutaijyuuryou1;

    /**
     * 添加材ｽﾗﾘｰ_調合量1
     */
    private Integer tenkazaislurry_tyougouryou1;

    /**
     * 添加材ｽﾗﾘｰ_風袋重量2
     */
    private Integer tenkazaislurry_fuutaijyuuryou2;

    /**
     * 添加材ｽﾗﾘｰ_調合量2
     */
    private Integer tenkazaislurry_tyougouryou2;

    /**
     * 添加材ｽﾗﾘｰ秤量終了日時
     */
    private Timestamp tenkazaislurryhyouryousyuuryounichiji;

    /**
     * 固形分測定担当者
     */
    private String kokeibunsokuteitantousya;

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
     * 溶剤秤量開始日時
     * @return the youzaihyouryoukaisinichiji
     */
    public Timestamp getYouzaihyouryoukaisinichiji() {
        return youzaihyouryoukaisinichiji;
    }

    /**
     * 溶剤秤量開始日時
     * @param youzaihyouryoukaisinichiji the youzaihyouryoukaisinichiji to set
     */
    public void setYouzaihyouryoukaisinichiji(Timestamp youzaihyouryoukaisinichiji) {
        this.youzaihyouryoukaisinichiji = youzaihyouryoukaisinichiji;
    }

    /**
     * 分散材①_材料品名
     * @return the zunsanzai1_zairyouhinmei
     */
    public String getZunsanzai1_zairyouhinmei() {
        return zunsanzai1_zairyouhinmei;
    }

    /**
     * 分散材①_材料品名
     * @param zunsanzai1_zairyouhinmei the zunsanzai1_zairyouhinmei to set
     */
    public void setZunsanzai1_zairyouhinmei(String zunsanzai1_zairyouhinmei) {
        this.zunsanzai1_zairyouhinmei = zunsanzai1_zairyouhinmei;
    }

    /**
     * 分散材①_調合量規格
     * @return the zunsanzai1_tyougouryoukikaku
     */
    public String getZunsanzai1_tyougouryoukikaku() {
        return zunsanzai1_tyougouryoukikaku;
    }

    /**
     * 分散材①_調合量規格
     * @param zunsanzai1_tyougouryoukikaku the zunsanzai1_tyougouryoukikaku to set
     */
    public void setZunsanzai1_tyougouryoukikaku(String zunsanzai1_tyougouryoukikaku) {
        this.zunsanzai1_tyougouryoukikaku = zunsanzai1_tyougouryoukikaku;
    }

    /**
     * 分散材①_部材在庫No1
     * @return the zunsanzai1_buzaizaikolotno1
     */
    public String getZunsanzai1_buzaizaikolotno1() {
        return zunsanzai1_buzaizaikolotno1;
    }

    /**
     * 分散材①_部材在庫No1
     * @param zunsanzai1_buzaizaikolotno1 the zunsanzai1_buzaizaikolotno1 to set
     */
    public void setZunsanzai1_buzaizaikolotno1(String zunsanzai1_buzaizaikolotno1) {
        this.zunsanzai1_buzaizaikolotno1 = zunsanzai1_buzaizaikolotno1;
    }

    /**
     * 分散材①_調合量1
     * @return the zunsanzai1_tyougouryou1
     */
    public Integer getZunsanzai1_tyougouryou1() {
        return zunsanzai1_tyougouryou1;
    }

    /**
     * 分散材①_調合量1
     * @param zunsanzai1_tyougouryou1 the zunsanzai1_tyougouryou1 to set
     */
    public void setZunsanzai1_tyougouryou1(Integer zunsanzai1_tyougouryou1) {
        this.zunsanzai1_tyougouryou1 = zunsanzai1_tyougouryou1;
    }

    /**
     * 分散材①_部材在庫No2
     * @return the zunsanzai1_buzaizaikolotno2
     */
    public String getZunsanzai1_buzaizaikolotno2() {
        return zunsanzai1_buzaizaikolotno2;
    }

    /**
     * 分散材①_部材在庫No2
     * @param zunsanzai1_buzaizaikolotno2 the zunsanzai1_buzaizaikolotno2 to set
     */
    public void setZunsanzai1_buzaizaikolotno2(String zunsanzai1_buzaizaikolotno2) {
        this.zunsanzai1_buzaizaikolotno2 = zunsanzai1_buzaizaikolotno2;
    }

    /**
     * 分散材①_調合量2
     * @return the zunsanzai1_tyougouryou2
     */
    public Integer getZunsanzai1_tyougouryou2() {
        return zunsanzai1_tyougouryou2;
    }

    /**
     * 分散材①_調合量2
     * @param zunsanzai1_tyougouryou2 the zunsanzai1_tyougouryou2 to set
     */
    public void setZunsanzai1_tyougouryou2(Integer zunsanzai1_tyougouryou2) {
        this.zunsanzai1_tyougouryou2 = zunsanzai1_tyougouryou2;
    }

    /**
     * 分散材②_材料品名
     * @return the zunsanzai2_zairyouhinmei
     */
    public String getZunsanzai2_zairyouhinmei() {
        return zunsanzai2_zairyouhinmei;
    }

    /**
     * 分散材②_材料品名
     * @param zunsanzai2_zairyouhinmei the zunsanzai2_zairyouhinmei to set
     */
    public void setZunsanzai2_zairyouhinmei(String zunsanzai2_zairyouhinmei) {
        this.zunsanzai2_zairyouhinmei = zunsanzai2_zairyouhinmei;
    }

    /**
     * 分散材②_調合量規格
     * @return the zunsanzai2_tyougouryoukikaku
     */
    public String getZunsanzai2_tyougouryoukikaku() {
        return zunsanzai2_tyougouryoukikaku;
    }

    /**
     * 分散材②_調合量規格
     * @param zunsanzai2_tyougouryoukikaku the zunsanzai2_tyougouryoukikaku to set
     */
    public void setZunsanzai2_tyougouryoukikaku(String zunsanzai2_tyougouryoukikaku) {
        this.zunsanzai2_tyougouryoukikaku = zunsanzai2_tyougouryoukikaku;
    }

    /**
     * 分散材②_部材在庫No1
     * @return the zunsanzai2_buzaizaikolotno1
     */
    public String getZunsanzai2_buzaizaikolotno1() {
        return zunsanzai2_buzaizaikolotno1;
    }

    /**
     * 分散材②_部材在庫No1
     * @param zunsanzai2_buzaizaikolotno1 the zunsanzai2_buzaizaikolotno1 to set
     */
    public void setZunsanzai2_buzaizaikolotno1(String zunsanzai2_buzaizaikolotno1) {
        this.zunsanzai2_buzaizaikolotno1 = zunsanzai2_buzaizaikolotno1;
    }

    /**
     * 分散材②_調合量1
     * @return the zunsanzai2_tyougouryou1
     */
    public Integer getZunsanzai2_tyougouryou1() {
        return zunsanzai2_tyougouryou1;
    }

    /**
     * 分散材②_調合量1
     * @param zunsanzai2_tyougouryou1 the zunsanzai2_tyougouryou1 to set
     */
    public void setZunsanzai2_tyougouryou1(Integer zunsanzai2_tyougouryou1) {
        this.zunsanzai2_tyougouryou1 = zunsanzai2_tyougouryou1;
    }

    /**
     * 分散材②_部材在庫No2
     * @return the zunsanzai2_buzaizaikolotno2
     */
    public String getZunsanzai2_buzaizaikolotno2() {
        return zunsanzai2_buzaizaikolotno2;
    }

    /**
     * 分散材②_部材在庫No2
     * @param zunsanzai2_buzaizaikolotno2 the zunsanzai2_buzaizaikolotno2 to set
     */
    public void setZunsanzai2_buzaizaikolotno2(String zunsanzai2_buzaizaikolotno2) {
        this.zunsanzai2_buzaizaikolotno2 = zunsanzai2_buzaizaikolotno2;
    }

    /**
     * 分散材②_調合量2
     * @return the zunsanzai2_tyougouryou2
     */
    public Integer getZunsanzai2_tyougouryou2() {
        return zunsanzai2_tyougouryou2;
    }

    /**
     * 分散材②_調合量2
     * @param zunsanzai2_tyougouryou2 the zunsanzai2_tyougouryou2 to set
     */
    public void setZunsanzai2_tyougouryou2(Integer zunsanzai2_tyougouryou2) {
        this.zunsanzai2_tyougouryou2 = zunsanzai2_tyougouryou2;
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
     * @return the youzai5_buzaizaikolotno1
     */
    public String getYouzai5_buzaizaikolotno1() {
        return youzai5_buzaizaikolotno1;
    }

    /**
     * 溶剤⑤_部材在庫No1
     * @param youzai5_buzaizaikolotno1 the youzai5_buzaizaikolotno1 to set
     */
    public void setYouzai5_buzaizaikolotno1(String youzai5_buzaizaikolotno1) {
        this.youzai5_buzaizaikolotno1 = youzai5_buzaizaikolotno1;
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
     * @return the youzai5_buzaizaikolotno2
     */
    public String getYouzai5_buzaizaikolotno2() {
        return youzai5_buzaizaikolotno2;
    }

    /**
     * 溶剤⑤_部材在庫No2
     * @param youzai5_buzaizaikolotno2 the youzai5_buzaizaikolotno2 to set
     */
    public void setYouzai5_buzaizaikolotno2(String youzai5_buzaizaikolotno2) {
        this.youzai5_buzaizaikolotno2 = youzai5_buzaizaikolotno2;
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
     * @return the youzai6_buzaizaikolotno1
     */
    public String getYouzai6_buzaizaikolotno1() {
        return youzai6_buzaizaikolotno1;
    }

    /**
     * 溶剤⑥_部材在庫No1
     * @param youzai6_buzaizaikolotno1 the youzai6_buzaizaikolotno1 to set
     */
    public void setYouzai6_buzaizaikolotno1(String youzai6_buzaizaikolotno1) {
        this.youzai6_buzaizaikolotno1 = youzai6_buzaizaikolotno1;
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
     * @return the youzai6_buzaizaikolotno2
     */
    public String getYouzai6_buzaizaikolotno2() {
        return youzai6_buzaizaikolotno2;
    }

    /**
     * 溶剤⑥_部材在庫No2
     * @param youzai6_buzaizaikolotno2 the youzai6_buzaizaikolotno2 to set
     */
    public void setYouzai6_buzaizaikolotno2(String youzai6_buzaizaikolotno2) {
        this.youzai6_buzaizaikolotno2 = youzai6_buzaizaikolotno2;
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
     * 溶剤⑦_材料品名
     * @return the youzai7_zairyouhinmei
     */
    public String getYouzai7_zairyouhinmei() {
        return youzai7_zairyouhinmei;
    }

    /**
     * 溶剤⑦_材料品名
     * @param youzai7_zairyouhinmei the youzai7_zairyouhinmei to set
     */
    public void setYouzai7_zairyouhinmei(String youzai7_zairyouhinmei) {
        this.youzai7_zairyouhinmei = youzai7_zairyouhinmei;
    }

    /**
     * 溶剤⑦_調合量規格
     * @return the youzai7_tyougouryoukikaku
     */
    public String getYouzai7_tyougouryoukikaku() {
        return youzai7_tyougouryoukikaku;
    }

    /**
     * 溶剤⑦_調合量規格
     * @param youzai7_tyougouryoukikaku the youzai7_tyougouryoukikaku to set
     */
    public void setYouzai7_tyougouryoukikaku(String youzai7_tyougouryoukikaku) {
        this.youzai7_tyougouryoukikaku = youzai7_tyougouryoukikaku;
    }

    /**
     * 溶剤⑦_部材在庫No1
     * @return the youzai7_buzaizaikolotno1
     */
    public String getYouzai7_buzaizaikolotno1() {
        return youzai7_buzaizaikolotno1;
    }

    /**
     * 溶剤⑦_部材在庫No1
     * @param youzai7_buzaizaikolotno1 the youzai7_buzaizaikolotno1 to set
     */
    public void setYouzai7_buzaizaikolotno1(String youzai7_buzaizaikolotno1) {
        this.youzai7_buzaizaikolotno1 = youzai7_buzaizaikolotno1;
    }

    /**
     * 溶剤⑦_調合量1
     * @return the youzai7_tyougouryou1
     */
    public Integer getYouzai7_tyougouryou1() {
        return youzai7_tyougouryou1;
    }

    /**
     * 溶剤⑦_調合量1
     * @param youzai7_tyougouryou1 the youzai7_tyougouryou1 to set
     */
    public void setYouzai7_tyougouryou1(Integer youzai7_tyougouryou1) {
        this.youzai7_tyougouryou1 = youzai7_tyougouryou1;
    }

    /**
     * 溶剤⑦_部材在庫No2
     * @return the youzai7_buzaizaikolotno2
     */
    public String getYouzai7_buzaizaikolotno2() {
        return youzai7_buzaizaikolotno2;
    }

    /**
     * 溶剤⑦_部材在庫No2
     * @param youzai7_buzaizaikolotno2 the youzai7_buzaizaikolotno2 to set
     */
    public void setYouzai7_buzaizaikolotno2(String youzai7_buzaizaikolotno2) {
        this.youzai7_buzaizaikolotno2 = youzai7_buzaizaikolotno2;
    }

    /**
     * 溶剤⑦_調合量2
     * @return the youzai7_tyougouryou2
     */
    public Integer getYouzai7_tyougouryou2() {
        return youzai7_tyougouryou2;
    }

    /**
     * 溶剤⑦_調合量2
     * @param youzai7_tyougouryou2 the youzai7_tyougouryou2 to set
     */
    public void setYouzai7_tyougouryou2(Integer youzai7_tyougouryou2) {
        this.youzai7_tyougouryou2 = youzai7_tyougouryou2;
    }

    /**
     * 溶剤⑧_材料品名
     * @return the youzai8_zairyouhinmei
     */
    public String getYouzai8_zairyouhinmei() {
        return youzai8_zairyouhinmei;
    }

    /**
     * 溶剤⑧_材料品名
     * @param youzai8_zairyouhinmei the youzai8_zairyouhinmei to set
     */
    public void setYouzai8_zairyouhinmei(String youzai8_zairyouhinmei) {
        this.youzai8_zairyouhinmei = youzai8_zairyouhinmei;
    }

    /**
     * 溶剤⑧_調合量規格
     * @return the youzai8_tyougouryoukikaku
     */
    public String getYouzai8_tyougouryoukikaku() {
        return youzai8_tyougouryoukikaku;
    }

    /**
     * 溶剤⑧_調合量規格
     * @param youzai8_tyougouryoukikaku the youzai8_tyougouryoukikaku to set
     */
    public void setYouzai8_tyougouryoukikaku(String youzai8_tyougouryoukikaku) {
        this.youzai8_tyougouryoukikaku = youzai8_tyougouryoukikaku;
    }

    /**
     * 溶剤⑧_部材在庫No1
     * @return the youzai8_buzaizaikolotno1
     */
    public String getYouzai8_buzaizaikolotno1() {
        return youzai8_buzaizaikolotno1;
    }

    /**
     * 溶剤⑧_部材在庫No1
     * @param youzai8_buzaizaikolotno1 the youzai8_buzaizaikolotno1 to set
     */
    public void setYouzai8_buzaizaikolotno1(String youzai8_buzaizaikolotno1) {
        this.youzai8_buzaizaikolotno1 = youzai8_buzaizaikolotno1;
    }

    /**
     * 溶剤⑧_調合量1
     * @return the youzai8_tyougouryou1
     */
    public Integer getYouzai8_tyougouryou1() {
        return youzai8_tyougouryou1;
    }

    /**
     * 溶剤⑧_調合量1
     * @param youzai8_tyougouryou1 the youzai8_tyougouryou1 to set
     */
    public void setYouzai8_tyougouryou1(Integer youzai8_tyougouryou1) {
        this.youzai8_tyougouryou1 = youzai8_tyougouryou1;
    }

    /**
     * 溶剤⑧_部材在庫No2
     * @return the youzai8_buzaizaikolotno2
     */
    public String getYouzai8_buzaizaikolotno2() {
        return youzai8_buzaizaikolotno2;
    }

    /**
     * 溶剤⑧_部材在庫No2
     * @param youzai8_buzaizaikolotno2 the youzai8_buzaizaikolotno2 to set
     */
    public void setYouzai8_buzaizaikolotno2(String youzai8_buzaizaikolotno2) {
        this.youzai8_buzaizaikolotno2 = youzai8_buzaizaikolotno2;
    }

    /**
     * 溶剤⑧_調合量2
     * @return the youzai8_tyougouryou2
     */
    public Integer getYouzai8_tyougouryou2() {
        return youzai8_tyougouryou2;
    }

    /**
     * 溶剤⑧_調合量2
     * @param youzai8_tyougouryou2 the youzai8_tyougouryou2 to set
     */
    public void setYouzai8_tyougouryou2(Integer youzai8_tyougouryou2) {
        this.youzai8_tyougouryou2 = youzai8_tyougouryou2;
    }

    /**
     * 溶剤⑨_材料品名
     * @return the youzai9_zairyouhinmei
     */
    public String getYouzai9_zairyouhinmei() {
        return youzai9_zairyouhinmei;
    }

    /**
     * 溶剤⑨_材料品名
     * @param youzai9_zairyouhinmei the youzai9_zairyouhinmei to set
     */
    public void setYouzai9_zairyouhinmei(String youzai9_zairyouhinmei) {
        this.youzai9_zairyouhinmei = youzai9_zairyouhinmei;
    }

    /**
     * 溶剤⑨_調合量規格
     * @return the youzai9_tyougouryoukikaku
     */
    public String getYouzai9_tyougouryoukikaku() {
        return youzai9_tyougouryoukikaku;
    }

    /**
     * 溶剤⑨_調合量規格
     * @param youzai9_tyougouryoukikaku the youzai9_tyougouryoukikaku to set
     */
    public void setYouzai9_tyougouryoukikaku(String youzai9_tyougouryoukikaku) {
        this.youzai9_tyougouryoukikaku = youzai9_tyougouryoukikaku;
    }

    /**
     * 溶剤⑨_部材在庫No1
     * @return the youzai9_buzaizaikolotno1
     */
    public String getYouzai9_buzaizaikolotno1() {
        return youzai9_buzaizaikolotno1;
    }

    /**
     * 溶剤⑨_部材在庫No1
     * @param youzai9_buzaizaikolotno1 the youzai9_buzaizaikolotno1 to set
     */
    public void setYouzai9_buzaizaikolotno1(String youzai9_buzaizaikolotno1) {
        this.youzai9_buzaizaikolotno1 = youzai9_buzaizaikolotno1;
    }

    /**
     * 溶剤⑨_調合量1
     * @return the youzai9_tyougouryou1
     */
    public Integer getYouzai9_tyougouryou1() {
        return youzai9_tyougouryou1;
    }

    /**
     * 溶剤⑨_調合量1
     * @param youzai9_tyougouryou1 the youzai9_tyougouryou1 to set
     */
    public void setYouzai9_tyougouryou1(Integer youzai9_tyougouryou1) {
        this.youzai9_tyougouryou1 = youzai9_tyougouryou1;
    }

    /**
     * 溶剤⑨_部材在庫No2
     * @return the youzai9_buzaizaikolotno2
     */
    public String getYouzai9_buzaizaikolotno2() {
        return youzai9_buzaizaikolotno2;
    }

    /**
     * 溶剤⑨_部材在庫No2
     * @param youzai9_buzaizaikolotno2 the youzai9_buzaizaikolotno2 to set
     */
    public void setYouzai9_buzaizaikolotno2(String youzai9_buzaizaikolotno2) {
        this.youzai9_buzaizaikolotno2 = youzai9_buzaizaikolotno2;
    }

    /**
     * 溶剤⑨_調合量2
     * @return the youzai9_tyougouryou2
     */
    public Integer getYouzai9_tyougouryou2() {
        return youzai9_tyougouryou2;
    }

    /**
     * 溶剤⑨_調合量2
     * @param youzai9_tyougouryou2 the youzai9_tyougouryou2 to set
     */
    public void setYouzai9_tyougouryou2(Integer youzai9_tyougouryou2) {
        this.youzai9_tyougouryou2 = youzai9_tyougouryou2;
    }

    /**
     * 溶剤秤量終了日時
     * @return the youzaihyouryousyuuryounichiji
     */
    public Timestamp getYouzaihyouryousyuuryounichiji() {
        return youzaihyouryousyuuryounichiji;
    }

    /**
     * 溶剤秤量終了日時
     * @param youzaihyouryousyuuryounichiji the youzaihyouryousyuuryounichiji to set
     */
    public void setYouzaihyouryousyuuryounichiji(Timestamp youzaihyouryousyuuryounichiji) {
        this.youzaihyouryousyuuryounichiji = youzaihyouryousyuuryounichiji;
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
     * 回転数
     * @return the kaitensuu
     */
    public String getKaitensuu() {
        return kaitensuu;
    }

    /**
     * 回転数
     * @param kaitensuu the kaitensuu to set
     */
    public void setKaitensuu(String kaitensuu) {
        this.kaitensuu = kaitensuu;
    }

    /**
     * 撹拌時間
     * @return the kakuhanjikan
     */
    public String getKakuhanjikan() {
        return kakuhanjikan;
    }

    /**
     * 撹拌時間
     * @param kakuhanjikan the kakuhanjikan to set
     */
    public void setKakuhanjikan(String kakuhanjikan) {
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
     * 添加材ｽﾗﾘｰ秤量開始日時
     * @return the tenkazaislurryhyouryoukaisinichiji
     */
    public Timestamp getTenkazaislurryhyouryoukaisinichiji() {
        return tenkazaislurryhyouryoukaisinichiji;
    }

    /**
     * 添加材ｽﾗﾘｰ秤量開始日時
     * @param tenkazaislurryhyouryoukaisinichiji the tenkazaislurryhyouryoukaisinichiji to set
     */
    public void setTenkazaislurryhyouryoukaisinichiji(Timestamp tenkazaislurryhyouryoukaisinichiji) {
        this.tenkazaislurryhyouryoukaisinichiji = tenkazaislurryhyouryoukaisinichiji;
    }

    /**
     * 添加材ｽﾗﾘｰ_材料品名
     * @return the tenkazaislurry_zairyouhinmei
     */
    public String getTenkazaislurry_zairyouhinmei() {
        return tenkazaislurry_zairyouhinmei;
    }

    /**
     * 添加材ｽﾗﾘｰ_材料品名
     * @param tenkazaislurry_zairyouhinmei the tenkazaislurry_zairyouhinmei to set
     */
    public void setTenkazaislurry_zairyouhinmei(String tenkazaislurry_zairyouhinmei) {
        this.tenkazaislurry_zairyouhinmei = tenkazaislurry_zairyouhinmei;
    }

    /**
     * 添加材ｽﾗﾘｰ_WIPﾛｯﾄNo
     * @return the tenkazaislurry_WIPlotno
     */
    public String getTenkazaislurry_WIPlotno() {
        return tenkazaislurry_WIPlotno;
    }

    /**
     * 添加材ｽﾗﾘｰ_WIPﾛｯﾄNo
     * @param tenkazaislurry_WIPlotno the tenkazaislurry_WIPlotno to set
     */
    public void setTenkazaislurry_WIPlotno(String tenkazaislurry_WIPlotno) {
        this.tenkazaislurry_WIPlotno = tenkazaislurry_WIPlotno;
    }

    /**
     * 添加材ｽﾗﾘｰ_調合量規格
     * @return the tenkazaislurry_tyougouryoukikaku
     */
    public String getTenkazaislurry_tyougouryoukikaku() {
        return tenkazaislurry_tyougouryoukikaku;
    }

    /**
     * 添加材ｽﾗﾘｰ_調合量規格
     * @param tenkazaislurry_tyougouryoukikaku the tenkazaislurry_tyougouryoukikaku to set
     */
    public void setTenkazaislurry_tyougouryoukikaku(String tenkazaislurry_tyougouryoukikaku) {
        this.tenkazaislurry_tyougouryoukikaku = tenkazaislurry_tyougouryoukikaku;
    }

    /**
     * 添加材ｽﾗﾘｰ_風袋重量1
     * @return the tenkazaislurry_fuutaijyuuryou1
     */
    public Integer getTenkazaislurry_fuutaijyuuryou1() {
        return tenkazaislurry_fuutaijyuuryou1;
    }

    /**
     * 添加材ｽﾗﾘｰ_風袋重量1
     * @param tenkazaislurry_fuutaijyuuryou1 the tenkazaislurry_fuutaijyuuryou1 to set
     */
    public void setTenkazaislurry_fuutaijyuuryou1(Integer tenkazaislurry_fuutaijyuuryou1) {
        this.tenkazaislurry_fuutaijyuuryou1 = tenkazaislurry_fuutaijyuuryou1;
    }

    /**
     * 添加材ｽﾗﾘｰ_調合量1
     * @return the tenkazaislurry_tyougouryou1
     */
    public Integer getTenkazaislurry_tyougouryou1() {
        return tenkazaislurry_tyougouryou1;
    }

    /**
     * 添加材ｽﾗﾘｰ_調合量1
     * @param tenkazaislurry_tyougouryou1 the tenkazaislurry_tyougouryou1 to set
     */
    public void setTenkazaislurry_tyougouryou1(Integer tenkazaislurry_tyougouryou1) {
        this.tenkazaislurry_tyougouryou1 = tenkazaislurry_tyougouryou1;
    }

    /**
     * 添加材ｽﾗﾘｰ_風袋重量2
     * @return the tenkazaislurry_fuutaijyuuryou2
     */
    public Integer getTenkazaislurry_fuutaijyuuryou2() {
        return tenkazaislurry_fuutaijyuuryou2;
    }

    /**
     * 添加材ｽﾗﾘｰ_風袋重量2
     * @param tenkazaislurry_fuutaijyuuryou2 the tenkazaislurry_fuutaijyuuryou2 to set
     */
    public void setTenkazaislurry_fuutaijyuuryou2(Integer tenkazaislurry_fuutaijyuuryou2) {
        this.tenkazaislurry_fuutaijyuuryou2 = tenkazaislurry_fuutaijyuuryou2;
    }

    /**
     * 添加材ｽﾗﾘｰ_調合量2
     * @return the tenkazaislurry_tyougouryou2
     */
    public Integer getTenkazaislurry_tyougouryou2() {
        return tenkazaislurry_tyougouryou2;
    }

    /**
     * 添加材ｽﾗﾘｰ_調合量2
     * @param tenkazaislurry_tyougouryou2 the tenkazaislurry_tyougouryou2 to set
     */
    public void setTenkazaislurry_tyougouryou2(Integer tenkazaislurry_tyougouryou2) {
        this.tenkazaislurry_tyougouryou2 = tenkazaislurry_tyougouryou2;
    }

    /**
     * 添加材ｽﾗﾘｰ秤量終了日時
     * @return the tenkazaislurryhyouryousyuuryounichiji
     */
    public Timestamp getTenkazaislurryhyouryousyuuryounichiji() {
        return tenkazaislurryhyouryousyuuryounichiji;
    }

    /**
     * 添加材ｽﾗﾘｰ秤量終了日時
     * @param tenkazaislurryhyouryousyuuryounichiji the tenkazaislurryhyouryousyuuryounichiji to set
     */
    public void setTenkazaislurryhyouryousyuuryounichiji(Timestamp tenkazaislurryhyouryousyuuryounichiji) {
        this.tenkazaislurryhyouryousyuuryounichiji = tenkazaislurryhyouryousyuuryounichiji;
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