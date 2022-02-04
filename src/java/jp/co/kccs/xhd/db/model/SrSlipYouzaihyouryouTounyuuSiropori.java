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
 * 変更日	2021/12/09<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SR_SLIP_YOUZAIHYOURYOU_TOUNYUU_SIROPORI(ｽﾘｯﾌﾟ作製・溶剤秤量・投入(白ﾎﾟﾘ))のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/12/09
 */
public class SrSlipYouzaihyouryouTounyuuSiropori {
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
     * ｽﾘｯﾌﾟ品名
     */
    private String sliphinmei;

    /**
     * ｽﾘｯﾌﾟLotNo
     */
    private String sliplotno;

    /**
     * ﾛｯﾄ区分
     */
    private String lotkubun;

    /**
     * 原料記号
     */
    private String genryoukigou;

    /**
     * 秤量号機
     */
    private String goki;

    /**
     * 溶剤秤量日時
     */
    private Timestamp youzaikeiryounichiji;

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
     * 担当者
     */
    private String tantousya;

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合設備
     */
    private String binderkongousetub;

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合号機
     */
    private String binderkongougoki;

    /**
     * 混合ﾀﾝｸ種類
     */
    private String kongoutanksyurui;

    /**
     * 混合ﾀﾝｸNo
     */
    private Integer kongoutankno;

    /**
     * ﾀﾝｸ内洗浄確認
     */
    private Integer tanknaisenjyoukakunin;

    /**
     * ﾀﾝｸ内内袋確認
     */
    private Integer tanknaiutibukurokakunin;

    /**
     * 撹拌羽根洗浄確認
     */
    private Integer kakuhanhanesenjyoukakunin;

    /**
     * 撹拌軸洗浄確認
     */
    private Integer kakuhanjikusenjyoukakunin;

    /**
     * 投入①
     */
    private Integer tounyuu1;

    /**
     * 投入②
     */
    private Integer tounyuu2;

    /**
     * 投入③
     */
    private Integer tounyuu3;

    /**
     * 投入④
     */
    private Integer tounyuu4;

    /**
     * 投入⑤
     */
    private Integer tounyuu5;

    /**
     * 投入⑥
     */
    private Integer tounyuu6;

    /**
     * ｽﾗﾘｰ投入確認者
     */
    private String slurrytounyuukakuninsya;

    /**
     * 投入⑦
     */
    private Integer tounyuu7;

    /**
     * 投入⑧
     */
    private Integer tounyuu8;

    /**
     * 溶剤投入確認者
     */
    private String youzaitounyuukakuninsya;

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
     * ｽﾘｯﾌﾟ品名
     * @return the sliphinmei
     */
    public String getSliphinmei() {
        return sliphinmei;
    }

    /**
     * ｽﾘｯﾌﾟ品名
     * @param sliphinmei the sliphinmei to set
     */
    public void setSliphinmei(String sliphinmei) {
        this.sliphinmei = sliphinmei;
    }

    /**
     * ｽﾘｯﾌﾟLotNo
     * @return the sliplotno
     */
    public String getSliplotno() {
        return sliplotno;
    }

    /**
     * ｽﾘｯﾌﾟLotNo
     * @param sliplotno the sliplotno to set
     */
    public void setSliplotno(String sliplotno) {
        this.sliplotno = sliplotno;
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
     * 溶剤秤量日時
     * @return the youzaikeiryounichiji
     */
    public Timestamp getYouzaikeiryounichiji() {
        return youzaikeiryounichiji;
    }

    /**
     * 溶剤秤量日時
     * @param youzaikeiryounichiji the youzaikeiryounichiji to set
     */
    public void setYouzaikeiryounichiji(Timestamp youzaikeiryounichiji) {
        this.youzaikeiryounichiji = youzaikeiryounichiji;
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
     * ﾊﾞｲﾝﾀﾞｰ混合設備
     * @return the binderkongousetub
     */
    public String getBinderkongousetub() {
        return binderkongousetub;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合設備
     * @param binderkongousetub the binderkongousetub to set
     */
    public void setBinderkongousetub(String binderkongousetub) {
        this.binderkongousetub = binderkongousetub;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合号機
     * @return the binderkongougoki
     */
    public String getBinderkongougoki() {
        return binderkongougoki;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合号機
     * @param binderkongougoki the binderkongougoki to set
     */
    public void setBinderkongougoki(String binderkongougoki) {
        this.binderkongougoki = binderkongougoki;
    }

    /**
     * 混合ﾀﾝｸ種類
     * @return the kongoutanksyurui
     */
    public String getKongoutanksyurui() {
        return kongoutanksyurui;
    }

    /**
     * 混合ﾀﾝｸ種類
     * @param kongoutanksyurui the kongoutanksyurui to set
     */
    public void setKongoutanksyurui(String kongoutanksyurui) {
        this.kongoutanksyurui = kongoutanksyurui;
    }

    /**
     * 混合ﾀﾝｸNo
     * @return the kongoutankno
     */
    public Integer getKongoutankno() {
        return kongoutankno;
    }

    /**
     * 混合ﾀﾝｸNo
     * @param kongoutankno the kongoutankno to set
     */
    public void setKongoutankno(Integer kongoutankno) {
        this.kongoutankno = kongoutankno;
    }

    /**
     * ﾀﾝｸ内洗浄確認
     * @return the tanknaisenjyoukakunin
     */
    public Integer getTanknaisenjyoukakunin() {
        return tanknaisenjyoukakunin;
    }

    /**
     * ﾀﾝｸ内洗浄確認
     * @param tanknaisenjyoukakunin the tanknaisenjyoukakunin to set
     */
    public void setTanknaisenjyoukakunin(Integer tanknaisenjyoukakunin) {
        this.tanknaisenjyoukakunin = tanknaisenjyoukakunin;
    }

    /**
     * ﾀﾝｸ内内袋確認
     * @return the tanknaiutibukurokakunin
     */
    public Integer getTanknaiutibukurokakunin() {
        return tanknaiutibukurokakunin;
    }

    /**
     * ﾀﾝｸ内内袋確認
     * @param tanknaiutibukurokakunin the tanknaiutibukurokakunin to set
     */
    public void setTanknaiutibukurokakunin(Integer tanknaiutibukurokakunin) {
        this.tanknaiutibukurokakunin = tanknaiutibukurokakunin;
    }

    /**
     * 撹拌羽根洗浄確認
     * @return the kakuhanhanesenjyoukakunin
     */
    public Integer getKakuhanhanesenjyoukakunin() {
        return kakuhanhanesenjyoukakunin;
    }

    /**
     * 撹拌羽根洗浄確認
     * @param kakuhanhanesenjyoukakunin the kakuhanhanesenjyoukakunin to set
     */
    public void setKakuhanhanesenjyoukakunin(Integer kakuhanhanesenjyoukakunin) {
        this.kakuhanhanesenjyoukakunin = kakuhanhanesenjyoukakunin;
    }

    /**
     * 撹拌軸洗浄確認
     * @return the kakuhanjikusenjyoukakunin
     */
    public Integer getKakuhanjikusenjyoukakunin() {
        return kakuhanjikusenjyoukakunin;
    }

    /**
     * 撹拌軸洗浄確認
     * @param kakuhanjikusenjyoukakunin the kakuhanjikusenjyoukakunin to set
     */
    public void setKakuhanjikusenjyoukakunin(Integer kakuhanjikusenjyoukakunin) {
        this.kakuhanjikusenjyoukakunin = kakuhanjikusenjyoukakunin;
    }

    /**
     * 投入①
     * @return the tounyuu1
     */
    public Integer getTounyuu1() {
        return tounyuu1;
    }

    /**
     * 投入①
     * @param tounyuu1 the tounyuu1 to set
     */
    public void setTounyuu1(Integer tounyuu1) {
        this.tounyuu1 = tounyuu1;
    }

    /**
     * 投入②
     * @return the tounyuu2
     */
    public Integer getTounyuu2() {
        return tounyuu2;
    }

    /**
     * 投入②
     * @param tounyuu2 the tounyuu2 to set
     */
    public void setTounyuu2(Integer tounyuu2) {
        this.tounyuu2 = tounyuu2;
    }

    /**
     * 投入③
     * @return the tounyuu3
     */
    public Integer getTounyuu3() {
        return tounyuu3;
    }

    /**
     * 投入③
     * @param tounyuu3 the tounyuu3 to set
     */
    public void setTounyuu3(Integer tounyuu3) {
        this.tounyuu3 = tounyuu3;
    }

    /**
     * 投入④
     * @return the tounyuu4
     */
    public Integer getTounyuu4() {
        return tounyuu4;
    }

    /**
     * 投入④
     * @param tounyuu4 the tounyuu4 to set
     */
    public void setTounyuu4(Integer tounyuu4) {
        this.tounyuu4 = tounyuu4;
    }

    /**
     * 投入⑤
     * @return the tounyuu5
     */
    public Integer getTounyuu5() {
        return tounyuu5;
    }

    /**
     * 投入⑤
     * @param tounyuu5 the tounyuu5 to set
     */
    public void setTounyuu5(Integer tounyuu5) {
        this.tounyuu5 = tounyuu5;
    }

    /**
     * 投入⑥
     * @return the tounyuu6
     */
    public Integer getTounyuu6() {
        return tounyuu6;
    }

    /**
     * 投入⑥
     * @param tounyuu6 the tounyuu6 to set
     */
    public void setTounyuu6(Integer tounyuu6) {
        this.tounyuu6 = tounyuu6;
    }

    /**
     * ｽﾗﾘｰ投入確認者
     * @return the slurrytounyuukakuninsya
     */
    public String getSlurrytounyuukakuninsya() {
        return slurrytounyuukakuninsya;
    }

    /**
     * ｽﾗﾘｰ投入確認者
     * @param slurrytounyuukakuninsya the slurrytounyuukakuninsya to set
     */
    public void setSlurrytounyuukakuninsya(String slurrytounyuukakuninsya) {
        this.slurrytounyuukakuninsya = slurrytounyuukakuninsya;
    }

    /**
     * 投入⑦
     * @return the tounyuu7
     */
    public Integer getTounyuu7() {
        return tounyuu7;
    }

    /**
     * 投入⑦
     * @param tounyuu7 the tounyuu7 to set
     */
    public void setTounyuu7(Integer tounyuu7) {
        this.tounyuu7 = tounyuu7;
    }

    /**
     * 投入⑧
     * @return the tounyuu8
     */
    public Integer getTounyuu8() {
        return tounyuu8;
    }

    /**
     * 投入⑧
     * @param tounyuu8 the tounyuu8 to set
     */
    public void setTounyuu8(Integer tounyuu8) {
        this.tounyuu8 = tounyuu8;
    }

    /**
     * 溶剤投入確認者
     * @return the youzaitounyuukakuninsya
     */
    public String getYouzaitounyuukakuninsya() {
        return youzaitounyuukakuninsya;
    }

    /**
     * 溶剤投入確認者
     * @param youzaitounyuukakuninsya the youzaitounyuukakuninsya to set
     */
    public void setYouzaitounyuukakuninsya(String youzaitounyuukakuninsya) {
        this.youzaitounyuukakuninsya = youzaitounyuukakuninsya;
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