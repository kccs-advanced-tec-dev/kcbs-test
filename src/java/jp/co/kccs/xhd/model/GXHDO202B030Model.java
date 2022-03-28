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
 * 変更日       2021/12/25<br>
 * 計画書No     MB2101-DK002<br>
 * 変更者       KCSS K.Jo<br>
 * 変更理由     新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器)履歴検索画面のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/12/25
 */
public class GXHDO202B030Model implements Serializable {
    /** WIPﾛｯﾄNo */
    private String lotno = "";

    /** ｽﾘｯﾌﾟ品名 */
    private String sliphinmei = "";

    /** ｽﾘｯﾌﾟLotNo */
    private String sliplotno = "";

    /** ﾛｯﾄ区分 */
    private String lotkubun = "";

    /** 原料記号 */
    private String genryoukigou = "";

    /** 秤量号機 */
    private String goki = "";

    /** 誘電体ｽﾗﾘｰ重量 */
    private Integer yuudentaislurryjyuuryou = null;

    /** 誘電体ｽﾗﾘｰ_材料品名 */
    private String yuudentaislurry_zairyouhinmei = "";

    /** 誘電体ｽﾗﾘｰ_調合量規格 */
    private String yuudentaislurry_tyougouryoukikaku = "";

    /** 誘電体ｽﾗﾘｰ_部材在庫No1 */
    private String yuudentaislurry_buzaizaikolotno1 = "";

    /** 誘電体ｽﾗﾘｰ_調合量1 */
    private Integer yuudentaislurry_tyougouryou1 = null;

    /** 誘電体ｽﾗﾘｰ_部材在庫No2 */
    private String yuudentaislurry_buzaizaikolotno2 = "";

    /** 誘電体ｽﾗﾘｰ_調合量2 */
    private Integer yuudentaislurry_tyougouryou2 = null;

    /** 担当者 */
    private String tantousya = "";

    /** 誘電体ｽﾗﾘｰ投入① */
    private String yuudentaislurrytounyuu1 = "";

    /** 誘電体ｽﾗﾘｰ投入② */
    private String yuudentaislurrytounyuu2 = "";

    /** 誘電体ｽﾗﾘｰ投入③ */
    private String yuudentaislurrytounyuu3 = "";

    /** 誘電体ｽﾗﾘｰ投入④ */
    private String yuudentaislurrytounyuu4 = "";

    /** 誘電体ｽﾗﾘｰ投入⑤ */
    private String yuudentaislurrytounyuu5 = "";

    /** 誘電体ｽﾗﾘｰ投入⑥ */
    private String yuudentaislurrytounyuu6 = "";

    /** 誘電体ｽﾗﾘｰ投入担当者 */
    private String yuudentaislurrytounyuutantousya = "";

    /** 溶剤調整量 */
    private Integer youzaityouseiryou = null;

    /** ﾄﾙｴﾝ調整量 */
    private Integer toluenetyouseiryou = null;

    /** ｿﾙﾐｯｸｽ調整量 */
    private Integer solmixtyouseiryou = null;

    /** 溶剤秤量日時 */
    private Timestamp youzaikeiryounichiji = null;

    /** 分散材①_材料品名 */
    private String zunsanzai1_zairyouhinmei = "";

    /** 分散材①_調合量規格 */
    private String zunsanzai1_tyougouryoukikaku = "";

    /** 分散材①_部材在庫No1 */
    private String zunsanzai1_buzaizaikolotno1 = "";

    /** 分散材①_調合量1 */
    private Integer zunsanzai1_tyougouryou1 = null;

    /** 分散材①_部材在庫No2 */
    private String zunsanzai1_buzaizaikolotno2 = "";

    /** 分散材①_調合量2 */
    private Integer zunsanzai1_tyougouryou2 = null;

    /** 分散材②_材料品名 */
    private String zunsanzai2_zairyouhinmei = "";

    /** 分散材②_調合量規格 */
    private String zunsanzai2_tyougouryoukikaku = "";

    /** 分散材②_部材在庫No1 */
    private String zunsanzai2_buzaizaikolotno1 = "";

    /** 分散材②_調合量1 */
    private Integer zunsanzai2_tyougouryou1 = null;

    /** 分散材②_部材在庫No2 */
    private String zunsanzai2_buzaizaikolotno2 = "";

    /** 分散材②_調合量2 */
    private Integer zunsanzai2_tyougouryou2 = null;

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

    /** 撹拌機 */
    private String kakuhanki = "";

    /** 撹拌時間 */
    private String kakuhanjikan = "";

    /** 撹拌開始日時 */
    private Timestamp kakuhankaisinichiji = null;

    /** 撹拌終了日時 */
    private Timestamp kakuhansyuuryounichiji = null;

    /** ﾊﾞｲﾝﾀﾞｰ混合設備 */
    private String binderkongousetub = "";

    /** 設備ｻｲｽﾞ */
    private String setubisize = "";

    /** ﾊﾞｲﾝﾀﾞｰ混合号機 */
    private String binderkongougoki = "";

    /** 混合ﾀﾝｸ種類 */
    private String kongoutanksyurui = "";

    /** 混合ﾀﾝｸNo */
    private Integer kongoutankno = null;

    /** ﾀﾝｸ内洗浄確認 */
    private String tanknaisenjyoukakunin = "";

    /** ﾀﾝｸ内内袋確認 */
    private String tanknaiutibukurokakunin = "";

    /** 撹拌羽根洗浄確認 */
    private String kakuhanhanesenjyoukakunin = "";

    /** 撹拌軸洗浄確認 */
    private String kakuhanjikusenjyoukakunin = "";

    /** 転流板の高さ */
    private String tenryuubannotakasa = "";

    /** ﾀﾝｸにｱｰｽｸﾞﾘｯﾌﾟ接続 */
    private String tankniearthgripsetuzoku = "";

    /** 分散材溶剤投入 */
    private String zunsanzaiyouzaitounyuu = "";

    /** 溶剤投入① */
    private String youzaitounyuu1 = "";

    /** 溶剤投入② */
    private String youzaitounyuu2 = "";

    /** 溶剤投入担当者 */
    private String youzaitounyuutantousya = "";

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
     * 誘電体ｽﾗﾘｰ重量
     * @return the yuudentaislurryjyuuryou
     */
    public Integer getYuudentaislurryjyuuryou() {
        return yuudentaislurryjyuuryou;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量
     * @param yuudentaislurryjyuuryou the yuudentaislurryjyuuryou to set
     */
    public void setYuudentaislurryjyuuryou(Integer yuudentaislurryjyuuryou) {
        this.yuudentaislurryjyuuryou = yuudentaislurryjyuuryou;
    }

    /**
     * 誘電体ｽﾗﾘｰ_材料品名
     * @return the yuudentaislurry_zairyouhinmei
     */
    public String getYuudentaislurry_zairyouhinmei() {
        return yuudentaislurry_zairyouhinmei;
    }

    /**
     * 誘電体ｽﾗﾘｰ_材料品名
     * @param yuudentaislurry_zairyouhinmei the yuudentaislurry_zairyouhinmei to set
     */
    public void setYuudentaislurry_zairyouhinmei(String yuudentaislurry_zairyouhinmei) {
        this.yuudentaislurry_zairyouhinmei = yuudentaislurry_zairyouhinmei;
    }

    /**
     * 誘電体ｽﾗﾘｰ_調合量規格
     * @return the yuudentaislurry_tyougouryoukikaku
     */
    public String getYuudentaislurry_tyougouryoukikaku() {
        return yuudentaislurry_tyougouryoukikaku;
    }

    /**
     * 誘電体ｽﾗﾘｰ_調合量規格
     * @param yuudentaislurry_tyougouryoukikaku the yuudentaislurry_tyougouryoukikaku to set
     */
    public void setYuudentaislurry_tyougouryoukikaku(String yuudentaislurry_tyougouryoukikaku) {
        this.yuudentaislurry_tyougouryoukikaku = yuudentaislurry_tyougouryoukikaku;
    }

    /**
     * 誘電体ｽﾗﾘｰ_部材在庫No1
     * @return the yuudentaislurry_buzaizaikolotno1
     */
    public String getYuudentaislurry_buzaizaikolotno1() {
        return yuudentaislurry_buzaizaikolotno1;
    }

    /**
     * 誘電体ｽﾗﾘｰ_部材在庫No1
     * @param yuudentaislurry_buzaizaikolotno1 the yuudentaislurry_buzaizaikolotno1 to set
     */
    public void setYuudentaislurry_buzaizaikolotno1(String yuudentaislurry_buzaizaikolotno1) {
        this.yuudentaislurry_buzaizaikolotno1 = yuudentaislurry_buzaizaikolotno1;
    }

    /**
     * 誘電体ｽﾗﾘｰ_調合量1
     * @return the yuudentaislurry_tyougouryou1
     */
    public Integer getYuudentaislurry_tyougouryou1() {
        return yuudentaislurry_tyougouryou1;
    }

    /**
     * 誘電体ｽﾗﾘｰ_調合量1
     * @param yuudentaislurry_tyougouryou1 the yuudentaislurry_tyougouryou1 to set
     */
    public void setYuudentaislurry_tyougouryou1(Integer yuudentaislurry_tyougouryou1) {
        this.yuudentaislurry_tyougouryou1 = yuudentaislurry_tyougouryou1;
    }

    /**
     * 誘電体ｽﾗﾘｰ_部材在庫No2
     * @return the yuudentaislurry_buzaizaikolotno2
     */
    public String getYuudentaislurry_buzaizaikolotno2() {
        return yuudentaislurry_buzaizaikolotno2;
    }

    /**
     * 誘電体ｽﾗﾘｰ_部材在庫No2
     * @param yuudentaislurry_buzaizaikolotno2 the yuudentaislurry_buzaizaikolotno2 to set
     */
    public void setYuudentaislurry_buzaizaikolotno2(String yuudentaislurry_buzaizaikolotno2) {
        this.yuudentaislurry_buzaizaikolotno2 = yuudentaislurry_buzaizaikolotno2;
    }

    /**
     * 誘電体ｽﾗﾘｰ_調合量2
     * @return the yuudentaislurry_tyougouryou2
     */
    public Integer getYuudentaislurry_tyougouryou2() {
        return yuudentaislurry_tyougouryou2;
    }

    /**
     * 誘電体ｽﾗﾘｰ_調合量2
     * @param yuudentaislurry_tyougouryou2 the yuudentaislurry_tyougouryou2 to set
     */
    public void setYuudentaislurry_tyougouryou2(Integer yuudentaislurry_tyougouryou2) {
        this.yuudentaislurry_tyougouryou2 = yuudentaislurry_tyougouryou2;
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
     * 誘電体ｽﾗﾘｰ投入①
     * @return the yuudentaislurrytounyuu1
     */
    public String getYuudentaislurrytounyuu1() {
        return yuudentaislurrytounyuu1;
    }

    /**
     * 誘電体ｽﾗﾘｰ投入①
     * @param yuudentaislurrytounyuu1 the yuudentaislurrytounyuu1 to set
     */
    public void setYuudentaislurrytounyuu1(String yuudentaislurrytounyuu1) {
        this.yuudentaislurrytounyuu1 = yuudentaislurrytounyuu1;
    }

    /**
     * 誘電体ｽﾗﾘｰ投入②
     * @return the yuudentaislurrytounyuu2
     */
    public String getYuudentaislurrytounyuu2() {
        return yuudentaislurrytounyuu2;
    }

    /**
     * 誘電体ｽﾗﾘｰ投入②
     * @param yuudentaislurrytounyuu2 the yuudentaislurrytounyuu2 to set
     */
    public void setYuudentaislurrytounyuu2(String yuudentaislurrytounyuu2) {
        this.yuudentaislurrytounyuu2 = yuudentaislurrytounyuu2;
    }

    /**
     * 誘電体ｽﾗﾘｰ投入③
     * @return the yuudentaislurrytounyuu3
     */
    public String getYuudentaislurrytounyuu3() {
        return yuudentaislurrytounyuu3;
    }

    /**
     * 誘電体ｽﾗﾘｰ投入③
     * @param yuudentaislurrytounyuu3 the yuudentaislurrytounyuu3 to set
     */
    public void setYuudentaislurrytounyuu3(String yuudentaislurrytounyuu3) {
        this.yuudentaislurrytounyuu3 = yuudentaislurrytounyuu3;
    }

    /**
     * 誘電体ｽﾗﾘｰ投入④
     * @return the yuudentaislurrytounyuu4
     */
    public String getYuudentaislurrytounyuu4() {
        return yuudentaislurrytounyuu4;
    }

    /**
     * 誘電体ｽﾗﾘｰ投入④
     * @param yuudentaislurrytounyuu4 the yuudentaislurrytounyuu4 to set
     */
    public void setYuudentaislurrytounyuu4(String yuudentaislurrytounyuu4) {
        this.yuudentaislurrytounyuu4 = yuudentaislurrytounyuu4;
    }

    /**
     * 誘電体ｽﾗﾘｰ投入⑤
     * @return the yuudentaislurrytounyuu5
     */
    public String getYuudentaislurrytounyuu5() {
        return yuudentaislurrytounyuu5;
    }

    /**
     * 誘電体ｽﾗﾘｰ投入⑤
     * @param yuudentaislurrytounyuu5 the yuudentaislurrytounyuu5 to set
     */
    public void setYuudentaislurrytounyuu5(String yuudentaislurrytounyuu5) {
        this.yuudentaislurrytounyuu5 = yuudentaislurrytounyuu5;
    }

    /**
     * 誘電体ｽﾗﾘｰ投入⑥
     * @return the yuudentaislurrytounyuu6
     */
    public String getYuudentaislurrytounyuu6() {
        return yuudentaislurrytounyuu6;
    }

    /**
     * 誘電体ｽﾗﾘｰ投入⑥
     * @param yuudentaislurrytounyuu6 the yuudentaislurrytounyuu6 to set
     */
    public void setYuudentaislurrytounyuu6(String yuudentaislurrytounyuu6) {
        this.yuudentaislurrytounyuu6 = yuudentaislurrytounyuu6;
    }

    /**
     * 誘電体ｽﾗﾘｰ投入担当者
     * @return the yuudentaislurrytounyuutantousya
     */
    public String getYuudentaislurrytounyuutantousya() {
        return yuudentaislurrytounyuutantousya;
    }

    /**
     * 誘電体ｽﾗﾘｰ投入担当者
     * @param yuudentaislurrytounyuutantousya the yuudentaislurrytounyuutantousya to set
     */
    public void setYuudentaislurrytounyuutantousya(String yuudentaislurrytounyuutantousya) {
        this.yuudentaislurrytounyuutantousya = yuudentaislurrytounyuutantousya;
    }

    /**
     * 溶剤調整量
     * @return the youzaityouseiryou
     */
    public Integer getYouzaityouseiryou() {
        return youzaityouseiryou;
    }

    /**
     * 溶剤調整量
     * @param youzaityouseiryou the youzaityouseiryou to set
     */
    public void setYouzaityouseiryou(Integer youzaityouseiryou) {
        this.youzaityouseiryou = youzaityouseiryou;
    }

    /**
     * ﾄﾙｴﾝ調整量
     * @return the toluenetyouseiryou
     */
    public Integer getToluenetyouseiryou() {
        return toluenetyouseiryou;
    }

    /**
     * ﾄﾙｴﾝ調整量
     * @param toluenetyouseiryou the toluenetyouseiryou to set
     */
    public void setToluenetyouseiryou(Integer toluenetyouseiryou) {
        this.toluenetyouseiryou = toluenetyouseiryou;
    }

    /**
     * ｿﾙﾐｯｸｽ調整量
     * @return the solmixtyouseiryou
     */
    public Integer getSolmixtyouseiryou() {
        return solmixtyouseiryou;
    }

    /**
     * ｿﾙﾐｯｸｽ調整量
     * @param solmixtyouseiryou the solmixtyouseiryou to set
     */
    public void setSolmixtyouseiryou(Integer solmixtyouseiryou) {
        this.solmixtyouseiryou = solmixtyouseiryou;
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
     * 設備ｻｲｽﾞ
     * @return the setubisize
     */
    public String getSetubisize() {
        return setubisize;
    }

    /**
     * 設備ｻｲｽﾞ
     * @param setubisize the setubisize to set
     */
    public void setSetubisize(String setubisize) {
        this.setubisize = setubisize;
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
    public String getTanknaisenjyoukakunin() {
        return tanknaisenjyoukakunin;
    }

    /**
     * ﾀﾝｸ内洗浄確認
     * @param tanknaisenjyoukakunin the tanknaisenjyoukakunin to set
     */
    public void setTanknaisenjyoukakunin(String tanknaisenjyoukakunin) {
        this.tanknaisenjyoukakunin = tanknaisenjyoukakunin;
    }

    /**
     * ﾀﾝｸ内内袋確認
     * @return the tanknaiutibukurokakunin
     */
    public String getTanknaiutibukurokakunin() {
        return tanknaiutibukurokakunin;
    }

    /**
     * ﾀﾝｸ内内袋確認
     * @param tanknaiutibukurokakunin the tanknaiutibukurokakunin to set
     */
    public void setTanknaiutibukurokakunin(String tanknaiutibukurokakunin) {
        this.tanknaiutibukurokakunin = tanknaiutibukurokakunin;
    }

    /**
     * 撹拌羽根洗浄確認
     * @return the kakuhanhanesenjyoukakunin
     */
    public String getKakuhanhanesenjyoukakunin() {
        return kakuhanhanesenjyoukakunin;
    }

    /**
     * 撹拌羽根洗浄確認
     * @param kakuhanhanesenjyoukakunin the kakuhanhanesenjyoukakunin to set
     */
    public void setKakuhanhanesenjyoukakunin(String kakuhanhanesenjyoukakunin) {
        this.kakuhanhanesenjyoukakunin = kakuhanhanesenjyoukakunin;
    }

    /**
     * 撹拌軸洗浄確認
     * @return the kakuhanjikusenjyoukakunin
     */
    public String getKakuhanjikusenjyoukakunin() {
        return kakuhanjikusenjyoukakunin;
    }

    /**
     * 撹拌軸洗浄確認
     * @param kakuhanjikusenjyoukakunin the kakuhanjikusenjyoukakunin to set
     */
    public void setKakuhanjikusenjyoukakunin(String kakuhanjikusenjyoukakunin) {
        this.kakuhanjikusenjyoukakunin = kakuhanjikusenjyoukakunin;
    }

    /**
     * 転流板の高さ
     * @return the tenryuubannotakasa
     */
    public String getTenryuubannotakasa() {
        return tenryuubannotakasa;
    }

    /**
     * 転流板の高さ
     * @param tenryuubannotakasa the tenryuubannotakasa to set
     */
    public void setTenryuubannotakasa(String tenryuubannotakasa) {
        this.tenryuubannotakasa = tenryuubannotakasa;
    }

    /**
     * ﾀﾝｸにｱｰｽｸﾞﾘｯﾌﾟ接続
     * @return the tankniearthgripsetuzoku
     */
    public String getTankniearthgripsetuzoku() {
        return tankniearthgripsetuzoku;
    }

    /**
     * ﾀﾝｸにｱｰｽｸﾞﾘｯﾌﾟ接続
     * @param tankniearthgripsetuzoku the tankniearthgripsetuzoku to set
     */
    public void setTankniearthgripsetuzoku(String tankniearthgripsetuzoku) {
        this.tankniearthgripsetuzoku = tankniearthgripsetuzoku;
    }

    /**
     * 分散材溶剤投入
     * @return the zunsanzaiyouzaitounyuu
     */
    public String getZunsanzaiyouzaitounyuu() {
        return zunsanzaiyouzaitounyuu;
    }

    /**
     * 分散材溶剤投入
     * @param zunsanzaiyouzaitounyuu the zunsanzaiyouzaitounyuu to set
     */
    public void setZunsanzaiyouzaitounyuu(String zunsanzaiyouzaitounyuu) {
        this.zunsanzaiyouzaitounyuu = zunsanzaiyouzaitounyuu;
    }

    /**
     * 溶剤投入①
     * @return the youzaitounyuu1
     */
    public String getYouzaitounyuu1() {
        return youzaitounyuu1;
    }

    /**
     * 溶剤投入①
     * @param youzaitounyuu1 the youzaitounyuu1 to set
     */
    public void setYouzaitounyuu1(String youzaitounyuu1) {
        this.youzaitounyuu1 = youzaitounyuu1;
    }

    /**
     * 溶剤投入②
     * @return the youzaitounyuu2
     */
    public String getYouzaitounyuu2() {
        return youzaitounyuu2;
    }

    /**
     * 溶剤投入②
     * @param youzaitounyuu2 the youzaitounyuu2 to set
     */
    public void setYouzaitounyuu2(String youzaitounyuu2) {
        this.youzaitounyuu2 = youzaitounyuu2;
    }

    /**
     * 溶剤投入担当者
     * @return the youzaitounyuutantousya
     */
    public String getYouzaitounyuutantousya() {
        return youzaitounyuutantousya;
    }

    /**
     * 溶剤投入担当者
     * @param youzaitounyuutantousya the youzaitounyuutantousya to set
     */
    public void setYouzaitounyuutantousya(String youzaitounyuutantousya) {
        this.youzaitounyuutantousya = youzaitounyuutantousya;
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