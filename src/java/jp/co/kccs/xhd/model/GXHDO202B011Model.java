/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * 添加材ｽﾗﾘｰ作製・粉砕履歴検索画面のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/10/15
 */
public class GXHDO202B011Model implements Serializable {
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

    /** 粉砕機 */
    private String funsaiki = "";

    /** 粉砕号機 */
    private String funsaigouki = "";

    /** ﾒﾃﾞｨｱLotNo */
    private Integer medialotno = null;

    /** 連続運転回数 */
    private String renzokuuntenkaisuu = "";

    /** 投入量 */
    private String tounyuuryou = "";

    /** 時間/ﾊﾟｽ回数 */
    private String jikan_passkaisuu = "";

    /** ﾐﾙ周波数 */
    private String millsyuuhasuu = "";

    /** 周速 */
    private String syuusoku = "";

    /** ﾎﾟﾝﾌﾟ出力 */
    private String pumpsyuturyokcheck = "";

    /** 流量 */
    private String ryuuryou = "";

    /** ﾊﾟｽ回数 */
    private String passkaisuu = "";

    /** ﾃﾞｨｽﾊﾟの種類 */
    private String dispanosyurui = "";

    /** ﾃﾞｨｽﾊﾟ回転数 */
    private String dispakaitensuu = "";

    /** ﾊﾟｽ回数 */
    private Integer passkaisuu_sf = null;

    /** 開始日時 */
    private Timestamp kaisinichiji = null;

    /** 終了予定日時 */
    private Timestamp syuuryouyoteinichiji = null;

    /** 負荷電流値 */
    private Integer fukadenryuuti = null;

    /** 製品温度 */
    private Integer seihinondo = null;

    /** ｼｰﾙ温度 */
    private Integer stickerondo = null;

    /** ﾎﾟﾝﾌﾟ目盛 */
    private BigDecimal pumpmemori = null;

    /** ﾎﾟﾝﾌﾟ圧 */
    private BigDecimal pumpatu = null;

    /** 流量 */
    private BigDecimal ryuuryou_sf = null;

    /** 備考1 */
    private String bikou1 = "";

    /** 備考2 */
    private String bikou2 = "";

    /** 温度(往) */
    private Integer ondo_ou = null;

    /** 温度(還) */
    private Integer ondo_kan = null;

    /** 圧力(往) */
    private BigDecimal aturyoku_ou = null;

    /** 圧力(還) */
    private BigDecimal aturyoku_kan = null;

    /** 終了日時 */
    private Timestamp syuuryounichiji = null;

    /** 担当者 */
    private String tantousya_sf = "";

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

    /** 担当者 */
    private String tantousya = "";

    /** ﾎﾟﾝﾌﾟ出力 */
    private String pumpsyuturyoku = "";

    /** ﾐﾙ周波数 */
    private String millsyuuhasuu2 = "";

    /** 希釈溶剤添加 */
    private String kisyakuyouzaitenka = "";

    /** 溶剤循環時間 */
    private Integer youzaijyunkanjikan = null;

    /** 循環開始日時 */
    private Timestamp jyunkankaisinichiji = null;

    /** 循環終了日時 */
    private Timestamp jyunkansyuuryounichiji = null;

    /** 担当者 */
    private String jyunkantantousya = "";

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
     * 粉砕機
     * @return the funsaiki
     */
    public String getFunsaiki() {
        return funsaiki;
    }

    /**
     * 粉砕機
     * @param funsaiki the funsaiki to set
     */
    public void setFunsaiki(String funsaiki) {
        this.funsaiki = funsaiki;
    }

    /**
     * 粉砕号機
     * @return the funsaigouki
     */
    public String getFunsaigouki() {
        return funsaigouki;
    }

    /**
     * 粉砕号機
     * @param funsaigouki the funsaigouki to set
     */
    public void setFunsaigouki(String funsaigouki) {
        this.funsaigouki = funsaigouki;
    }

    /**
     * ﾒﾃﾞｨｱLotNo
     * @return the medialotno
     */
    public Integer getMedialotno() {
        return medialotno;
    }

    /**
     * ﾒﾃﾞｨｱLotNo
     * @param medialotno the medialotno to set
     */
    public void setMedialotno(Integer medialotno) {
        this.medialotno = medialotno;
    }

    /**
     * 連続運転回数
     * @return the renzokuuntenkaisuu
     */
    public String getRenzokuuntenkaisuu() {
        return renzokuuntenkaisuu;
    }

    /**
     * 連続運転回数
     * @param renzokuuntenkaisuu the renzokuuntenkaisuu to set
     */
    public void setRenzokuuntenkaisuu(String renzokuuntenkaisuu) {
        this.renzokuuntenkaisuu = renzokuuntenkaisuu;
    }

    /**
     * 投入量
     * @return the tounyuuryou
     */
    public String getTounyuuryou() {
        return tounyuuryou;
    }

    /**
     * 投入量
     * @param tounyuuryou the tounyuuryou to set
     */
    public void setTounyuuryou(String tounyuuryou) {
        this.tounyuuryou = tounyuuryou;
    }

    /**
     * 時間/ﾊﾟｽ回数
     * @return the jikan_passkaisuu
     */
    public String getJikan_passkaisuu() {
        return jikan_passkaisuu;
    }

    /**
     * 時間/ﾊﾟｽ回数
     * @param jikan_passkaisuu the jikan_passkaisuu to set
     */
    public void setJikan_passkaisuu(String jikan_passkaisuu) {
        this.jikan_passkaisuu = jikan_passkaisuu;
    }

    /**
     * ﾐﾙ周波数
     * @return the millsyuuhasuu
     */
    public String getMillsyuuhasuu() {
        return millsyuuhasuu;
    }

    /**
     * ﾐﾙ周波数
     * @param millsyuuhasuu the millsyuuhasuu to set
     */
    public void setMillsyuuhasuu(String millsyuuhasuu) {
        this.millsyuuhasuu = millsyuuhasuu;
    }

    /**
     * 周速
     * @return the syuusoku
     */
    public String getSyuusoku() {
        return syuusoku;
    }

    /**
     * 周速
     * @param syuusoku the syuusoku to set
     */
    public void setSyuusoku(String syuusoku) {
        this.syuusoku = syuusoku;
    }

    /**
     * ﾎﾟﾝﾌﾟ出力
     * @return the pumpsyuturyokcheck
     */
    public String getPumpsyuturyokcheck() {
        return pumpsyuturyokcheck;
    }

    /**
     * ﾎﾟﾝﾌﾟ出力
     * @param pumpsyuturyokcheck the pumpsyuturyokcheck to set
     */
    public void setPumpsyuturyokcheck(String pumpsyuturyokcheck) {
        this.pumpsyuturyokcheck = pumpsyuturyokcheck;
    }

    /**
     * 流量
     * @return the ryuuryou
     */
    public String getRyuuryou() {
        return ryuuryou;
    }

    /**
     * 流量
     * @param ryuuryou the ryuuryou to set
     */
    public void setRyuuryou(String ryuuryou) {
        this.ryuuryou = ryuuryou;
    }

    /**
     * ﾊﾟｽ回数
     * @return the passkaisuu
     */
    public String getPasskaisuu() {
        return passkaisuu;
    }

    /**
     * ﾊﾟｽ回数
     * @param passkaisuu the passkaisuu to set
     */
    public void setPasskaisuu(String passkaisuu) {
        this.passkaisuu = passkaisuu;
    }

    /**
     * ﾃﾞｨｽﾊﾟの種類
     * @return the dispanosyurui
     */
    public String getDispanosyurui() {
        return dispanosyurui;
    }

    /**
     * ﾃﾞｨｽﾊﾟの種類
     * @param dispanosyurui the dispanosyurui to set
     */
    public void setDispanosyurui(String dispanosyurui) {
        this.dispanosyurui = dispanosyurui;
    }

    /**
     * ﾃﾞｨｽﾊﾟ回転数
     * @return the dispakaitensuu
     */
    public String getDispakaitensuu() {
        return dispakaitensuu;
    }

    /**
     * ﾃﾞｨｽﾊﾟ回転数
     * @param dispakaitensuu the dispakaitensuu to set
     */
    public void setDispakaitensuu(String dispakaitensuu) {
        this.dispakaitensuu = dispakaitensuu;
    }

    /**
     * ﾊﾟｽ回数
     * @return the passkaisuu_sf
     */
    public Integer getPasskaisuu_sf() {
        return passkaisuu_sf;
    }

    /**
     * ﾊﾟｽ回数
     * @param passkaisuu_sf the passkaisuu_sf to set
     */
    public void setPasskaisuu_sf(Integer passkaisuu_sf) {
        this.passkaisuu_sf = passkaisuu_sf;
    }

    /**
     * 開始日時
     * @return the kaisinichiji
     */
    public Timestamp getKaisinichiji() {
        return kaisinichiji;
    }

    /**
     * 開始日時
     * @param kaisinichiji the kaisinichiji to set
     */
    public void setKaisinichiji(Timestamp kaisinichiji) {
        this.kaisinichiji = kaisinichiji;
    }

    /**
     * 終了予定日時
     * @return the syuuryouyoteinichiji
     */
    public Timestamp getSyuuryouyoteinichiji() {
        return syuuryouyoteinichiji;
    }

    /**
     * 終了予定日時
     * @param syuuryouyoteinichiji the syuuryouyoteinichiji to set
     */
    public void setSyuuryouyoteinichiji(Timestamp syuuryouyoteinichiji) {
        this.syuuryouyoteinichiji = syuuryouyoteinichiji;
    }

    /**
     * 負荷電流値
     * @return the fukadenryuuti
     */
    public Integer getFukadenryuuti() {
        return fukadenryuuti;
    }

    /**
     * 負荷電流値
     * @param fukadenryuuti the fukadenryuuti to set
     */
    public void setFukadenryuuti(Integer fukadenryuuti) {
        this.fukadenryuuti = fukadenryuuti;
    }

    /**
     * 製品温度
     * @return the seihinondo
     */
    public Integer getSeihinondo() {
        return seihinondo;
    }

    /**
     * 製品温度
     * @param seihinondo the seihinondo to set
     */
    public void setSeihinondo(Integer seihinondo) {
        this.seihinondo = seihinondo;
    }

    /**
     * ｼｰﾙ温度
     * @return the stickerondo
     */
    public Integer getStickerondo() {
        return stickerondo;
    }

    /**
     * ｼｰﾙ温度
     * @param stickerondo the stickerondo to set
     */
    public void setStickerondo(Integer stickerondo) {
        this.stickerondo = stickerondo;
    }

    /**
     * ﾎﾟﾝﾌﾟ目盛
     * @return the pumpmemori
     */
    public BigDecimal getPumpmemori() {
        return pumpmemori;
    }

    /**
     * ﾎﾟﾝﾌﾟ目盛
     * @param pumpmemori the pumpmemori to set
     */
    public void setPumpmemori(BigDecimal pumpmemori) {
        this.pumpmemori = pumpmemori;
    }

    /**
     * ﾎﾟﾝﾌﾟ圧
     * @return the pumpatu
     */
    public BigDecimal getPumpatu() {
        return pumpatu;
    }

    /**
     * ﾎﾟﾝﾌﾟ圧
     * @param pumpatu the pumpatu to set
     */
    public void setPumpatu(BigDecimal pumpatu) {
        this.pumpatu = pumpatu;
    }

    /**
     * 流量
     * @return the ryuuryou_sf
     */
    public BigDecimal getRyuuryou_sf() {
        return ryuuryou_sf;
    }

    /**
     * 流量
     * @param ryuuryou_sf the ryuuryou_sf to set
     */
    public void setRyuuryou_sf(BigDecimal ryuuryou_sf) {
        this.ryuuryou_sf = ryuuryou_sf;
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
     * 温度(往)
     * @return the ondo_ou
     */
    public Integer getOndo_ou() {
        return ondo_ou;
    }

    /**
     * 温度(往)
     * @param ondo_ou the ondo_ou to set
     */
    public void setOndo_ou(Integer ondo_ou) {
        this.ondo_ou = ondo_ou;
    }

    /**
     * 温度(還)
     * @return the ondo_kan
     */
    public Integer getOndo_kan() {
        return ondo_kan;
    }

    /**
     * 温度(還)
     * @param ondo_kan the ondo_kan to set
     */
    public void setOndo_kan(Integer ondo_kan) {
        this.ondo_kan = ondo_kan;
    }

    /**
     * 圧力(往)
     * @return the aturyoku_ou
     */
    public BigDecimal getAturyoku_ou() {
        return aturyoku_ou;
    }

    /**
     * 圧力(往)
     * @param aturyoku_ou the aturyoku_ou to set
     */
    public void setAturyoku_ou(BigDecimal aturyoku_ou) {
        this.aturyoku_ou = aturyoku_ou;
    }

    /**
     * 圧力(還)
     * @return the aturyoku_kan
     */
    public BigDecimal getAturyoku_kan() {
        return aturyoku_kan;
    }

    /**
     * 圧力(還)
     * @param aturyoku_kan the aturyoku_kan to set
     */
    public void setAturyoku_kan(BigDecimal aturyoku_kan) {
        this.aturyoku_kan = aturyoku_kan;
    }

    /**
     * 終了日時
     * @return the syuuryounichiji
     */
    public Timestamp getSyuuryounichiji() {
        return syuuryounichiji;
    }

    /**
     * 終了日時
     * @param syuuryounichiji the syuuryounichiji to set
     */
    public void setSyuuryounichiji(Timestamp syuuryounichiji) {
        this.syuuryounichiji = syuuryounichiji;
    }

    /**
     * 担当者
     * @return the tantousya_sf
     */
    public String getTantousya_sf() {
        return tantousya_sf;
    }

    /**
     * 担当者
     * @param tantousya_sf the tantousya_sf to set
     */
    public void setTantousya_sf(String tantousya_sf) {
        this.tantousya_sf = tantousya_sf;
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
     * ﾎﾟﾝﾌﾟ出力
     * @return the pumpsyuturyoku
     */
    public String getPumpsyuturyoku() {
        return pumpsyuturyoku;
    }

    /**
     * ﾎﾟﾝﾌﾟ出力
     * @param pumpsyuturyoku the pumpsyuturyoku to set
     */
    public void setPumpsyuturyoku(String pumpsyuturyoku) {
        this.pumpsyuturyoku = pumpsyuturyoku;
    }

    /**
     * ﾐﾙ周波数
     * @return the millsyuuhasuu2
     */
    public String getMillsyuuhasuu2() {
        return millsyuuhasuu2;
    }

    /**
     * ﾐﾙ周波数
     * @param millsyuuhasuu2 the millsyuuhasuu2 to set
     */
    public void setMillsyuuhasuu2(String millsyuuhasuu2) {
        this.millsyuuhasuu2 = millsyuuhasuu2;
    }

    /**
     * 希釈溶剤添加
     * @return the kisyakuyouzaitenka
     */
    public String getKisyakuyouzaitenka() {
        return kisyakuyouzaitenka;
    }

    /**
     * 希釈溶剤添加
     * @param kisyakuyouzaitenka the kisyakuyouzaitenka to set
     */
    public void setKisyakuyouzaitenka(String kisyakuyouzaitenka) {
        this.kisyakuyouzaitenka = kisyakuyouzaitenka;
    }

    /**
     * 溶剤循環時間
     * @return the youzaijyunkanjikan
     */
    public Integer getYouzaijyunkanjikan() {
        return youzaijyunkanjikan;
    }

    /**
     * 溶剤循環時間
     * @param youzaijyunkanjikan the youzaijyunkanjikan to set
     */
    public void setYouzaijyunkanjikan(Integer youzaijyunkanjikan) {
        this.youzaijyunkanjikan = youzaijyunkanjikan;
    }

    /**
     * 循環開始日時
     * @return the jyunkankaisinichiji
     */
    public Timestamp getJyunkankaisinichiji() {
        return jyunkankaisinichiji;
    }

    /**
     * 循環開始日時
     * @param jyunkankaisinichiji the jyunkankaisinichiji to set
     */
    public void setJyunkankaisinichiji(Timestamp jyunkankaisinichiji) {
        this.jyunkankaisinichiji = jyunkankaisinichiji;
    }

    /**
     * 循環終了日時
     * @return the jyunkansyuuryounichiji
     */
    public Timestamp getJyunkansyuuryounichiji() {
        return jyunkansyuuryounichiji;
    }

    /**
     * 循環終了日時
     * @param jyunkansyuuryounichiji the jyunkansyuuryounichiji to set
     */
    public void setJyunkansyuuryounichiji(Timestamp jyunkansyuuryounichiji) {
        this.jyunkansyuuryounichiji = jyunkansyuuryounichiji;
    }

    /**
     * 担当者
     * @return the jyunkantantousya
     */
    public String getJyunkantantousya() {
        return jyunkantantousya;
    }

    /**
     * 担当者
     * @param jyunkantantousya the jyunkantantousya to set
     */
    public void setJyunkantantousya(String jyunkantantousya) {
        this.jyunkantantousya = jyunkantantousya;
    }

}