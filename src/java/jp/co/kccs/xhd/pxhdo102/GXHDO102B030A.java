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
 * 変更日	2021/12/10<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B030(ｽﾘｯﾌﾟ作製・溶剤秤量・投入(ｽﾃﾝ容器))
 *
 * @author KCSS K.Jo
 * @since  2021/12/10
 */
@ViewScoped
@Named
public class GXHDO102B030A implements Serializable {
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
     * 誘電体ｽﾗﾘｰ重量
     */
    private FXHDD01 yuudentaislurryjyuuryou;

    /**
     * 誘電体ｽﾗﾘｰ_材料品名
     */
    private FXHDD01 yuudentaislurry_zairyouhinmei;

    /**
     * 誘電体ｽﾗﾘｰ_調合量規格
     */
    private FXHDD01 yuudentaislurry_tyougouryoukikaku;

    /**
     * 誘電体ｽﾗﾘｰ_部材在庫No1
     */
    private FXHDD01 yuudentaislurry_buzaizaikolotno1;

    /**
     * 誘電体ｽﾗﾘｰ_調合量1
     */
    private FXHDD01 yuudentaislurry_tyougouryou1;

    /**
     * 誘電体ｽﾗﾘｰ_部材在庫No2
     */
    private FXHDD01 yuudentaislurry_buzaizaikolotno2;

    /**
     * 誘電体ｽﾗﾘｰ_調合量2
     */
    private FXHDD01 yuudentaislurry_tyougouryou2;

    /**
     * 担当者
     */
    private FXHDD01 tantousya;

    /**
     * 誘電体ｽﾗﾘｰ投入①
     */
    private FXHDD01 yuudentaislurrytounyuu1;

    /**
     * 誘電体ｽﾗﾘｰ投入②
     */
    private FXHDD01 yuudentaislurrytounyuu2;

    /**
     * 誘電体ｽﾗﾘｰ投入③
     */
    private FXHDD01 yuudentaislurrytounyuu3;

    /**
     * 誘電体ｽﾗﾘｰ投入④
     */
    private FXHDD01 yuudentaislurrytounyuu4;

    /**
     * 誘電体ｽﾗﾘｰ投入⑤
     */
    private FXHDD01 yuudentaislurrytounyuu5;

    /**
     * 誘電体ｽﾗﾘｰ投入⑥
     */
    private FXHDD01 yuudentaislurrytounyuu6;

    /**
     * 誘電体ｽﾗﾘｰ投入担当者
     */
    private FXHDD01 yuudentaislurrytounyuutantousya;

    /**
     * 溶剤調整量
     */
    private FXHDD01 youzaityouseiryou;

    /**
     * ﾄﾙｴﾝ調整量
     */
    private FXHDD01 toluenetyouseiryou;

    /**
     * ｿﾙﾐｯｸｽ調整量
     */
    private FXHDD01 solmixtyouseiryou;

    /**
     * 溶剤秤量日
     */
    private FXHDD01 youzaikeiryou_day;

    /**
     * 溶剤秤量時間
     */
    private FXHDD01 youzaikeiryou_time;

    /**
     * 分散材①_材料品名
     */
    private FXHDD01 zunsanzai1_zairyouhinmei;

    /**
     * 分散材①_調合量規格
     */
    private FXHDD01 zunsanzai1_tyougouryoukikaku;

    /**
     * 分散材①_部材在庫No1
     */
    private FXHDD01 zunsanzai1_buzaizaikolotno1;

    /**
     * 分散材①_調合量1
     */
    private FXHDD01 zunsanzai1_tyougouryou1;

    /**
     * 分散材①_部材在庫No2
     */
    private FXHDD01 zunsanzai1_buzaizaikolotno2;

    /**
     * 分散材①_調合量2
     */
    private FXHDD01 zunsanzai1_tyougouryou2;

    /**
     * 分散材②_材料品名
     */
    private FXHDD01 zunsanzai2_zairyouhinmei;

    /**
     * 分散材②_調合量規格
     */
    private FXHDD01 zunsanzai2_tyougouryoukikaku;

    /**
     * 分散材②_部材在庫No1
     */
    private FXHDD01 zunsanzai2_buzaizaikolotno1;

    /**
     * 分散材②_調合量1
     */
    private FXHDD01 zunsanzai2_tyougouryou1;

    /**
     * 分散材②_部材在庫No2
     */
    private FXHDD01 zunsanzai2_buzaizaikolotno2;

    /**
     * 分散材②_調合量2
     */
    private FXHDD01 zunsanzai2_tyougouryou2;

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
     * 撹拌機
     */
    private FXHDD01 kakuhanki;

    /**
     * 撹拌時間
     */
    private FXHDD01 kakuhanjikan;

    /**
     * 撹拌開始日
     */
    private FXHDD01 kakuhankaisi_day;

    /**
     * 撹拌開始時間
     */
    private FXHDD01 kakuhankaisi_time;

    /**
     * 撹拌終了日
     */
    private FXHDD01 kakuhansyuuryou_day;

    /**
     * 撹拌終了時間
     */
    private FXHDD01 kakuhansyuuryou_time;

    /**
     * ﾊﾞｲﾝﾀﾞｰ混合設備
     */
    private FXHDD01 binderkongousetub;

    /**
     * 設備ｻｲｽﾞ
     */
    private FXHDD01 setubisize;

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
     * 転流板の高さ
     */
    private FXHDD01 tenryuubannotakasa;

    /**
     * ﾀﾝｸにｱｰｽｸﾞﾘｯﾌﾟ接続
     */
    private FXHDD01 tankniearthgripsetuzoku;

    /**
     * 分散材溶剤投入
     */
    private FXHDD01 zunsanzaiyouzaitounyuu;

    /**
     * 溶剤投入①
     */
    private FXHDD01 youzaitounyuu1;

    /**
     * 溶剤投入②
     */
    private FXHDD01 youzaitounyuu2;

    /**
     * 溶剤投入担当者
     */
    private FXHDD01 youzaitounyuutantousya;

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
     * 誘電体ｽﾗﾘｰ重量
     * @return the yuudentaislurryjyuuryou
     */
    public FXHDD01 getYuudentaislurryjyuuryou() {
        return yuudentaislurryjyuuryou;
    }

    /**
     * 誘電体ｽﾗﾘｰ重量
     * @param yuudentaislurryjyuuryou the yuudentaislurryjyuuryou to set
     */
    public void setYuudentaislurryjyuuryou(FXHDD01 yuudentaislurryjyuuryou) {
        this.yuudentaislurryjyuuryou = yuudentaislurryjyuuryou;
    }

    /**
     * 誘電体ｽﾗﾘｰ_材料品名
     * @return the yuudentaislurry_zairyouhinmei
     */
    public FXHDD01 getYuudentaislurry_zairyouhinmei() {
        return yuudentaislurry_zairyouhinmei;
    }

    /**
     * 誘電体ｽﾗﾘｰ_材料品名
     * @param yuudentaislurry_zairyouhinmei the yuudentaislurry_zairyouhinmei to set
     */
    public void setYuudentaislurry_zairyouhinmei(FXHDD01 yuudentaislurry_zairyouhinmei) {
        this.yuudentaislurry_zairyouhinmei = yuudentaislurry_zairyouhinmei;
    }

    /**
     * 誘電体ｽﾗﾘｰ_調合量規格
     * @return the yuudentaislurry_tyougouryoukikaku
     */
    public FXHDD01 getYuudentaislurry_tyougouryoukikaku() {
        return yuudentaislurry_tyougouryoukikaku;
    }

    /**
     * 誘電体ｽﾗﾘｰ_調合量規格
     * @param yuudentaislurry_tyougouryoukikaku the yuudentaislurry_tyougouryoukikaku to set
     */
    public void setYuudentaislurry_tyougouryoukikaku(FXHDD01 yuudentaislurry_tyougouryoukikaku) {
        this.yuudentaislurry_tyougouryoukikaku = yuudentaislurry_tyougouryoukikaku;
    }

    /**
     * 誘電体ｽﾗﾘｰ_部材在庫No1
     * @return the yuudentaislurry_buzaizaikolotno1
     */
    public FXHDD01 getYuudentaislurry_buzaizaikolotno1() {
        return yuudentaislurry_buzaizaikolotno1;
    }

    /**
     * 誘電体ｽﾗﾘｰ_部材在庫No1
     * @param yuudentaislurry_buzaizaikolotno1 the yuudentaislurry_buzaizaikolotno1 to set
     */
    public void setYuudentaislurry_buzaizaikolotno1(FXHDD01 yuudentaislurry_buzaizaikolotno1) {
        this.yuudentaislurry_buzaizaikolotno1 = yuudentaislurry_buzaizaikolotno1;
    }

    /**
     * 誘電体ｽﾗﾘｰ_調合量1
     * @return the yuudentaislurry_tyougouryou1
     */
    public FXHDD01 getYuudentaislurry_tyougouryou1() {
        return yuudentaislurry_tyougouryou1;
    }

    /**
     * 誘電体ｽﾗﾘｰ_調合量1
     * @param yuudentaislurry_tyougouryou1 the yuudentaislurry_tyougouryou1 to set
     */
    public void setYuudentaislurry_tyougouryou1(FXHDD01 yuudentaislurry_tyougouryou1) {
        this.yuudentaislurry_tyougouryou1 = yuudentaislurry_tyougouryou1;
    }

    /**
     * 誘電体ｽﾗﾘｰ_部材在庫No2
     * @return the yuudentaislurry_buzaizaikolotno2
     */
    public FXHDD01 getYuudentaislurry_buzaizaikolotno2() {
        return yuudentaislurry_buzaizaikolotno2;
    }

    /**
     * 誘電体ｽﾗﾘｰ_部材在庫No2
     * @param yuudentaislurry_buzaizaikolotno2 the yuudentaislurry_buzaizaikolotno2 to set
     */
    public void setYuudentaislurry_buzaizaikolotno2(FXHDD01 yuudentaislurry_buzaizaikolotno2) {
        this.yuudentaislurry_buzaizaikolotno2 = yuudentaislurry_buzaizaikolotno2;
    }

    /**
     * 誘電体ｽﾗﾘｰ_調合量2
     * @return the yuudentaislurry_tyougouryou2
     */
    public FXHDD01 getYuudentaislurry_tyougouryou2() {
        return yuudentaislurry_tyougouryou2;
    }

    /**
     * 誘電体ｽﾗﾘｰ_調合量2
     * @param yuudentaislurry_tyougouryou2 the yuudentaislurry_tyougouryou2 to set
     */
    public void setYuudentaislurry_tyougouryou2(FXHDD01 yuudentaislurry_tyougouryou2) {
        this.yuudentaislurry_tyougouryou2 = yuudentaislurry_tyougouryou2;
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
     * 誘電体ｽﾗﾘｰ投入①
     * @return the yuudentaislurrytounyuu1
     */
    public FXHDD01 getYuudentaislurrytounyuu1() {
        return yuudentaislurrytounyuu1;
    }

    /**
     * 誘電体ｽﾗﾘｰ投入①
     * @param yuudentaislurrytounyuu1 the yuudentaislurrytounyuu1 to set
     */
    public void setYuudentaislurrytounyuu1(FXHDD01 yuudentaislurrytounyuu1) {
        this.yuudentaislurrytounyuu1 = yuudentaislurrytounyuu1;
    }

    /**
     * 誘電体ｽﾗﾘｰ投入②
     * @return the yuudentaislurrytounyuu2
     */
    public FXHDD01 getYuudentaislurrytounyuu2() {
        return yuudentaislurrytounyuu2;
    }

    /**
     * 誘電体ｽﾗﾘｰ投入②
     * @param yuudentaislurrytounyuu2 the yuudentaislurrytounyuu2 to set
     */
    public void setYuudentaislurrytounyuu2(FXHDD01 yuudentaislurrytounyuu2) {
        this.yuudentaislurrytounyuu2 = yuudentaislurrytounyuu2;
    }

    /**
     * 誘電体ｽﾗﾘｰ投入③
     * @return the yuudentaislurrytounyuu3
     */
    public FXHDD01 getYuudentaislurrytounyuu3() {
        return yuudentaislurrytounyuu3;
    }

    /**
     * 誘電体ｽﾗﾘｰ投入③
     * @param yuudentaislurrytounyuu3 the yuudentaislurrytounyuu3 to set
     */
    public void setYuudentaislurrytounyuu3(FXHDD01 yuudentaislurrytounyuu3) {
        this.yuudentaislurrytounyuu3 = yuudentaislurrytounyuu3;
    }

    /**
     * 誘電体ｽﾗﾘｰ投入④
     * @return the yuudentaislurrytounyuu4
     */
    public FXHDD01 getYuudentaislurrytounyuu4() {
        return yuudentaislurrytounyuu4;
    }

    /**
     * 誘電体ｽﾗﾘｰ投入④
     * @param yuudentaislurrytounyuu4 the yuudentaislurrytounyuu4 to set
     */
    public void setYuudentaislurrytounyuu4(FXHDD01 yuudentaislurrytounyuu4) {
        this.yuudentaislurrytounyuu4 = yuudentaislurrytounyuu4;
    }

    /**
     * 誘電体ｽﾗﾘｰ投入⑤
     * @return the yuudentaislurrytounyuu5
     */
    public FXHDD01 getYuudentaislurrytounyuu5() {
        return yuudentaislurrytounyuu5;
    }

    /**
     * 誘電体ｽﾗﾘｰ投入⑤
     * @param yuudentaislurrytounyuu5 the yuudentaislurrytounyuu5 to set
     */
    public void setYuudentaislurrytounyuu5(FXHDD01 yuudentaislurrytounyuu5) {
        this.yuudentaislurrytounyuu5 = yuudentaislurrytounyuu5;
    }

    /**
     * 誘電体ｽﾗﾘｰ投入⑥
     * @return the yuudentaislurrytounyuu6
     */
    public FXHDD01 getYuudentaislurrytounyuu6() {
        return yuudentaislurrytounyuu6;
    }

    /**
     * 誘電体ｽﾗﾘｰ投入⑥
     * @param yuudentaislurrytounyuu6 the yuudentaislurrytounyuu6 to set
     */
    public void setYuudentaislurrytounyuu6(FXHDD01 yuudentaislurrytounyuu6) {
        this.yuudentaislurrytounyuu6 = yuudentaislurrytounyuu6;
    }

    /**
     * 誘電体ｽﾗﾘｰ投入担当者
     * @return the yuudentaislurrytounyuutantousya
     */
    public FXHDD01 getYuudentaislurrytounyuutantousya() {
        return yuudentaislurrytounyuutantousya;
    }

    /**
     * 誘電体ｽﾗﾘｰ投入担当者
     * @param yuudentaislurrytounyuutantousya the yuudentaislurrytounyuutantousya to set
     */
    public void setYuudentaislurrytounyuutantousya(FXHDD01 yuudentaislurrytounyuutantousya) {
        this.yuudentaislurrytounyuutantousya = yuudentaislurrytounyuutantousya;
    }

    /**
     * 溶剤調整量
     * @return the youzaityouseiryou
     */
    public FXHDD01 getYouzaityouseiryou() {
        return youzaityouseiryou;
    }

    /**
     * 溶剤調整量
     * @param youzaityouseiryou the youzaityouseiryou to set
     */
    public void setYouzaityouseiryou(FXHDD01 youzaityouseiryou) {
        this.youzaityouseiryou = youzaityouseiryou;
    }

    /**
     * ﾄﾙｴﾝ調整量
     * @return the toluenetyouseiryou
     */
    public FXHDD01 getToluenetyouseiryou() {
        return toluenetyouseiryou;
    }

    /**
     * ﾄﾙｴﾝ調整量
     * @param toluenetyouseiryou the toluenetyouseiryou to set
     */
    public void setToluenetyouseiryou(FXHDD01 toluenetyouseiryou) {
        this.toluenetyouseiryou = toluenetyouseiryou;
    }

    /**
     * ｿﾙﾐｯｸｽ調整量
     * @return the solmixtyouseiryou
     */
    public FXHDD01 getSolmixtyouseiryou() {
        return solmixtyouseiryou;
    }

    /**
     * ｿﾙﾐｯｸｽ調整量
     * @param solmixtyouseiryou the solmixtyouseiryou to set
     */
    public void setSolmixtyouseiryou(FXHDD01 solmixtyouseiryou) {
        this.solmixtyouseiryou = solmixtyouseiryou;
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
     * 分散材①_材料品名
     * @return the zunsanzai1_zairyouhinmei
     */
    public FXHDD01 getZunsanzai1_zairyouhinmei() {
        return zunsanzai1_zairyouhinmei;
    }

    /**
     * 分散材①_材料品名
     * @param zunsanzai1_zairyouhinmei the zunsanzai1_zairyouhinmei to set
     */
    public void setZunsanzai1_zairyouhinmei(FXHDD01 zunsanzai1_zairyouhinmei) {
        this.zunsanzai1_zairyouhinmei = zunsanzai1_zairyouhinmei;
    }

    /**
     * 分散材①_調合量規格
     * @return the zunsanzai1_tyougouryoukikaku
     */
    public FXHDD01 getZunsanzai1_tyougouryoukikaku() {
        return zunsanzai1_tyougouryoukikaku;
    }

    /**
     * 分散材①_調合量規格
     * @param zunsanzai1_tyougouryoukikaku the zunsanzai1_tyougouryoukikaku to set
     */
    public void setZunsanzai1_tyougouryoukikaku(FXHDD01 zunsanzai1_tyougouryoukikaku) {
        this.zunsanzai1_tyougouryoukikaku = zunsanzai1_tyougouryoukikaku;
    }

    /**
     * 分散材①_部材在庫No1
     * @return the zunsanzai1_buzaizaikolotno1
     */
    public FXHDD01 getZunsanzai1_buzaizaikolotno1() {
        return zunsanzai1_buzaizaikolotno1;
    }

    /**
     * 分散材①_部材在庫No1
     * @param zunsanzai1_buzaizaikolotno1 the zunsanzai1_buzaizaikolotno1 to set
     */
    public void setZunsanzai1_buzaizaikolotno1(FXHDD01 zunsanzai1_buzaizaikolotno1) {
        this.zunsanzai1_buzaizaikolotno1 = zunsanzai1_buzaizaikolotno1;
    }

    /**
     * 分散材①_調合量1
     * @return the zunsanzai1_tyougouryou1
     */
    public FXHDD01 getZunsanzai1_tyougouryou1() {
        return zunsanzai1_tyougouryou1;
    }

    /**
     * 分散材①_調合量1
     * @param zunsanzai1_tyougouryou1 the zunsanzai1_tyougouryou1 to set
     */
    public void setZunsanzai1_tyougouryou1(FXHDD01 zunsanzai1_tyougouryou1) {
        this.zunsanzai1_tyougouryou1 = zunsanzai1_tyougouryou1;
    }

    /**
     * 分散材①_部材在庫No2
     * @return the zunsanzai1_buzaizaikolotno2
     */
    public FXHDD01 getZunsanzai1_buzaizaikolotno2() {
        return zunsanzai1_buzaizaikolotno2;
    }

    /**
     * 分散材①_部材在庫No2
     * @param zunsanzai1_buzaizaikolotno2 the zunsanzai1_buzaizaikolotno2 to set
     */
    public void setZunsanzai1_buzaizaikolotno2(FXHDD01 zunsanzai1_buzaizaikolotno2) {
        this.zunsanzai1_buzaizaikolotno2 = zunsanzai1_buzaizaikolotno2;
    }

    /**
     * 分散材①_調合量2
     * @return the zunsanzai1_tyougouryou2
     */
    public FXHDD01 getZunsanzai1_tyougouryou2() {
        return zunsanzai1_tyougouryou2;
    }

    /**
     * 分散材①_調合量2
     * @param zunsanzai1_tyougouryou2 the zunsanzai1_tyougouryou2 to set
     */
    public void setZunsanzai1_tyougouryou2(FXHDD01 zunsanzai1_tyougouryou2) {
        this.zunsanzai1_tyougouryou2 = zunsanzai1_tyougouryou2;
    }

    /**
     * 分散材②_材料品名
     * @return the zunsanzai2_zairyouhinmei
     */
    public FXHDD01 getZunsanzai2_zairyouhinmei() {
        return zunsanzai2_zairyouhinmei;
    }

    /**
     * 分散材②_材料品名
     * @param zunsanzai2_zairyouhinmei the zunsanzai2_zairyouhinmei to set
     */
    public void setZunsanzai2_zairyouhinmei(FXHDD01 zunsanzai2_zairyouhinmei) {
        this.zunsanzai2_zairyouhinmei = zunsanzai2_zairyouhinmei;
    }

    /**
     * 分散材②_調合量規格
     * @return the zunsanzai2_tyougouryoukikaku
     */
    public FXHDD01 getZunsanzai2_tyougouryoukikaku() {
        return zunsanzai2_tyougouryoukikaku;
    }

    /**
     * 分散材②_調合量規格
     * @param zunsanzai2_tyougouryoukikaku the zunsanzai2_tyougouryoukikaku to set
     */
    public void setZunsanzai2_tyougouryoukikaku(FXHDD01 zunsanzai2_tyougouryoukikaku) {
        this.zunsanzai2_tyougouryoukikaku = zunsanzai2_tyougouryoukikaku;
    }

    /**
     * 分散材②_部材在庫No1
     * @return the zunsanzai2_buzaizaikolotno1
     */
    public FXHDD01 getZunsanzai2_buzaizaikolotno1() {
        return zunsanzai2_buzaizaikolotno1;
    }

    /**
     * 分散材②_部材在庫No1
     * @param zunsanzai2_buzaizaikolotno1 the zunsanzai2_buzaizaikolotno1 to set
     */
    public void setZunsanzai2_buzaizaikolotno1(FXHDD01 zunsanzai2_buzaizaikolotno1) {
        this.zunsanzai2_buzaizaikolotno1 = zunsanzai2_buzaizaikolotno1;
    }

    /**
     * 分散材②_調合量1
     * @return the zunsanzai2_tyougouryou1
     */
    public FXHDD01 getZunsanzai2_tyougouryou1() {
        return zunsanzai2_tyougouryou1;
    }

    /**
     * 分散材②_調合量1
     * @param zunsanzai2_tyougouryou1 the zunsanzai2_tyougouryou1 to set
     */
    public void setZunsanzai2_tyougouryou1(FXHDD01 zunsanzai2_tyougouryou1) {
        this.zunsanzai2_tyougouryou1 = zunsanzai2_tyougouryou1;
    }

    /**
     * 分散材②_部材在庫No2
     * @return the zunsanzai2_buzaizaikolotno2
     */
    public FXHDD01 getZunsanzai2_buzaizaikolotno2() {
        return zunsanzai2_buzaizaikolotno2;
    }

    /**
     * 分散材②_部材在庫No2
     * @param zunsanzai2_buzaizaikolotno2 the zunsanzai2_buzaizaikolotno2 to set
     */
    public void setZunsanzai2_buzaizaikolotno2(FXHDD01 zunsanzai2_buzaizaikolotno2) {
        this.zunsanzai2_buzaizaikolotno2 = zunsanzai2_buzaizaikolotno2;
    }

    /**
     * 分散材②_調合量2
     * @return the zunsanzai2_tyougouryou2
     */
    public FXHDD01 getZunsanzai2_tyougouryou2() {
        return zunsanzai2_tyougouryou2;
    }

    /**
     * 分散材②_調合量2
     * @param zunsanzai2_tyougouryou2 the zunsanzai2_tyougouryou2 to set
     */
    public void setZunsanzai2_tyougouryou2(FXHDD01 zunsanzai2_tyougouryou2) {
        this.zunsanzai2_tyougouryou2 = zunsanzai2_tyougouryou2;
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
     * 撹拌機
     * @return the kakuhanki
     */
    public FXHDD01 getKakuhanki() {
        return kakuhanki;
    }

    /**
     * 撹拌機
     * @param kakuhanki the kakuhanki to set
     */
    public void setKakuhanki(FXHDD01 kakuhanki) {
        this.kakuhanki = kakuhanki;
    }

    /**
     * 撹拌時間
     * @return the kakuhanjikan
     */
    public FXHDD01 getKakuhanjikan() {
        return kakuhanjikan;
    }

    /**
     * 撹拌時間
     * @param kakuhanjikan the kakuhanjikan to set
     */
    public void setKakuhanjikan(FXHDD01 kakuhanjikan) {
        this.kakuhanjikan = kakuhanjikan;
    }

    /**
     * 撹拌開始日
     * @return the kakuhankaisi_day
     */
    public FXHDD01 getKakuhankaisi_day() {
        return kakuhankaisi_day;
    }

    /**
     * 撹拌開始日
     * @param kakuhankaisi_day the kakuhankaisi_day to set
     */
    public void setKakuhankaisi_day(FXHDD01 kakuhankaisi_day) {
        this.kakuhankaisi_day = kakuhankaisi_day;
    }

    /**
     * 撹拌開始時間
     * @return the kakuhankaisi_time
     */
    public FXHDD01 getKakuhankaisi_time() {
        return kakuhankaisi_time;
    }

    /**
     * 撹拌開始時間
     * @param kakuhankaisi_time the kakuhankaisi_time to set
     */
    public void setKakuhankaisi_time(FXHDD01 kakuhankaisi_time) {
        this.kakuhankaisi_time = kakuhankaisi_time;
    }

    /**
     * 撹拌終了日
     * @return the kakuhansyuuryou_day
     */
    public FXHDD01 getKakuhansyuuryou_day() {
        return kakuhansyuuryou_day;
    }

    /**
     * 撹拌終了日
     * @param kakuhansyuuryou_day the kakuhansyuuryou_day to set
     */
    public void setKakuhansyuuryou_day(FXHDD01 kakuhansyuuryou_day) {
        this.kakuhansyuuryou_day = kakuhansyuuryou_day;
    }

    /**
     * 撹拌終了時間
     * @return the kakuhansyuuryou_time
     */
    public FXHDD01 getKakuhansyuuryou_time() {
        return kakuhansyuuryou_time;
    }

    /**
     * 撹拌終了時間
     * @param kakuhansyuuryou_time the kakuhansyuuryou_time to set
     */
    public void setKakuhansyuuryou_time(FXHDD01 kakuhansyuuryou_time) {
        this.kakuhansyuuryou_time = kakuhansyuuryou_time;
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
     * 設備ｻｲｽﾞ
     * @return the setubisize
     */
    public FXHDD01 getSetubisize() {
        return setubisize;
    }

    /**
     * 設備ｻｲｽﾞ
     * @param setubisize the setubisize to set
     */
    public void setSetubisize(FXHDD01 setubisize) {
        this.setubisize = setubisize;
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
     * 転流板の高さ
     * @return the tenryuubannotakasa
     */
    public FXHDD01 getTenryuubannotakasa() {
        return tenryuubannotakasa;
    }

    /**
     * 転流板の高さ
     * @param tenryuubannotakasa the tenryuubannotakasa to set
     */
    public void setTenryuubannotakasa(FXHDD01 tenryuubannotakasa) {
        this.tenryuubannotakasa = tenryuubannotakasa;
    }

    /**
     * ﾀﾝｸにｱｰｽｸﾞﾘｯﾌﾟ接続
     * @return the tankniearthgripsetuzoku
     */
    public FXHDD01 getTankniearthgripsetuzoku() {
        return tankniearthgripsetuzoku;
    }

    /**
     * ﾀﾝｸにｱｰｽｸﾞﾘｯﾌﾟ接続
     * @param tankniearthgripsetuzoku the tankniearthgripsetuzoku to set
     */
    public void setTankniearthgripsetuzoku(FXHDD01 tankniearthgripsetuzoku) {
        this.tankniearthgripsetuzoku = tankniearthgripsetuzoku;
    }

    /**
     * 分散材溶剤投入
     * @return the zunsanzaiyouzaitounyuu
     */
    public FXHDD01 getZunsanzaiyouzaitounyuu() {
        return zunsanzaiyouzaitounyuu;
    }

    /**
     * 分散材溶剤投入
     * @param zunsanzaiyouzaitounyuu the zunsanzaiyouzaitounyuu to set
     */
    public void setZunsanzaiyouzaitounyuu(FXHDD01 zunsanzaiyouzaitounyuu) {
        this.zunsanzaiyouzaitounyuu = zunsanzaiyouzaitounyuu;
    }

    /**
     * 溶剤投入①
     * @return the youzaitounyuu1
     */
    public FXHDD01 getYouzaitounyuu1() {
        return youzaitounyuu1;
    }

    /**
     * 溶剤投入①
     * @param youzaitounyuu1 the youzaitounyuu1 to set
     */
    public void setYouzaitounyuu1(FXHDD01 youzaitounyuu1) {
        this.youzaitounyuu1 = youzaitounyuu1;
    }

    /**
     * 溶剤投入②
     * @return the youzaitounyuu2
     */
    public FXHDD01 getYouzaitounyuu2() {
        return youzaitounyuu2;
    }

    /**
     * 溶剤投入②
     * @param youzaitounyuu2 the youzaitounyuu2 to set
     */
    public void setYouzaitounyuu2(FXHDD01 youzaitounyuu2) {
        this.youzaitounyuu2 = youzaitounyuu2;
    }

    /**
     * 溶剤投入担当者
     * @return the youzaitounyuutantousya
     */
    public FXHDD01 getYouzaitounyuutantousya() {
        return youzaitounyuutantousya;
    }

    /**
     * 溶剤投入担当者
     * @param youzaitounyuutantousya the youzaitounyuutantousya to set
     */
    public void setYouzaitounyuutantousya(FXHDD01 youzaitounyuutantousya) {
        this.youzaitounyuutantousya = youzaitounyuutantousya;
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