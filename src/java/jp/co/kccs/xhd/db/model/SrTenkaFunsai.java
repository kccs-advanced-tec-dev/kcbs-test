/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/10/19<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SR_TENKA_FUNSAI(添加材ｽﾗﾘｰ作製・粉砕)のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/10/19
 */
public class SrTenkaFunsai {
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
     * 添加材ｽﾗﾘｰ品名
     */
    private String tenkazaislurryhinmei;

    /**
     * 添加材ｽﾗﾘｰLotNo
     */
    private String tenkazaislurrylotno;

    /**
     * ﾛｯﾄ区分
     */
    private String lotkubun;

    /**
     * 秤量号機
     */
    private String hyouryougouki;

    /**
     * 粉砕機
     */
    private Integer funsaiki;

    /**
     * 粉砕号機
     */
    private Integer funsaigouki;

    /**
     * ﾒﾃﾞｨｱLotNo
     */
    private Integer medialotno;

    /**
     * 連続運転回数
     */
    private Integer renzokuuntenkaisuu;

    /**
     * 投入量
     */
    private Integer tounyuuryou;

    /**
     * 時間/ﾊﾟｽ回数
     */
    private Integer jikan_passkaisuu;

    /**
     * ﾐﾙ周波数
     */
    private Integer millsyuuhasuu;
    
    /**
     * スクリーン
     */
    private Integer screen;
    
    /**
     * メディア径
     */
    private Integer mediakei;
    
    /**
     * 玉石重量
     */
    private Integer tamaisijuryo;
   

    /**
     * 周速
     */
    private Integer syuusoku;

    /**
     * ﾎﾟﾝﾌﾟ出力
     */
    private Integer pumpsyuturyokcheck;

    /**
     * 流量
     */
    private Integer ryuuryou;

    /**
     * ﾊﾟｽ回数
     */
    private Integer passkaisuu;

    /**
     * ﾃﾞｨｽﾊﾟの種類
     */
    private Integer dispanosyurui;

    /**
     * ﾃﾞｨｽﾊﾟ回転数
     */
    private Integer dispakaitensuu;

    /**
     * ﾊﾟｽ回数
     */
    private Integer passkaisuu_sf1;
    
    /**
     * ﾊﾟｽ回数
     */
    private Integer passkaisuuinput_sf1;

    /**
     * 開始日時
     */
    private Timestamp kaisinichiji_sf1;

    /**
     * 終了予定日時
     */
    private Timestamp syuuryouyoteinichiji_sf1;

    /**
     * 負荷電流値
     */
    private BigDecimal fukadenryuuti_sf1;

    /**
     * 製品温度
     */
    private BigDecimal seihinondo_sf1;

    /**
     * ｼｰﾙ温度
     */
    private BigDecimal stickerondo_sf1;

    /**
     * ﾎﾟﾝﾌﾟ目盛
     */
    private BigDecimal pumpmemori_sf1;

    /**
     * ﾎﾟﾝﾌﾟ圧
     */
    private BigDecimal pumpatu_sf1;

    /**
     * 流量
     */
    private BigDecimal ryuuryou_sf1;
    
    /**
     * 粉砕担当者
     */
    private String funsaitantousya_sf1;
    
    /**
     * 粉砕確認者
     */
    private String funsaikakuninsya_sf1;

    /**
     * 備考1
     */
    private String bikou1_sf1;

    /**
     * 備考2
     */
    private String bikou2_sf1;

    /**
     * 温度(往)
     */
    private Integer ondo_ou_sf1;

    /**
     * 温度(還)
     */
    private Integer ondo_kan_sf1;

    /**
     * 圧力(往)
     */
    private BigDecimal aturyoku_ou_sf1;

    /**
     * 圧力(還)
     */
    private BigDecimal aturyoku_kan_sf1;

    /**
     * 終了日時
     */
    private Timestamp syuuryounichiji_sf1;

    /**
     * 担当者
     */
    private String tantousya_sf1;

    /**
     * ﾊﾟｽ回数
     */
    private Integer passkaisuu_sf2;
    
    /**
     * ﾊﾟｽ回数
     */
    private Integer passkaisuuinput_sf2;

    /**
     * 開始日時
     */
    private Timestamp kaisinichiji_sf2;

    /**
     * 終了予定日時
     */
    private Timestamp syuuryouyoteinichiji_sf2;

    /**
     * 負荷電流値
     */
    private BigDecimal fukadenryuuti_sf2;

    /**
     * 製品温度
     */
    private BigDecimal seihinondo_sf2;

    /**
     * ｼｰﾙ温度
     */
    private BigDecimal stickerondo_sf2;

    /**
     * ﾎﾟﾝﾌﾟ目盛
     */
    private BigDecimal pumpmemori_sf2;

    /**
     * ﾎﾟﾝﾌﾟ圧
     */
    private BigDecimal pumpatu_sf2;

    /**
     * 終了日時
     */
    private Timestamp syuuryounichiji_sf2;

    /**
     * 流量
     */
    private BigDecimal ryuuryou_sf2;

    /**
     * 備考1
     */
    private String bikou1_sf2;

    /**
     * 備考2
     */
    private String bikou2_sf2;

    /**
     * 担当者
     */
    private String tantousya_sf2;

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
    private String youzai1_tyougouryou1;

    /**
     * 溶剤①_部材在庫No2
     */
    private String youzai1_buzaizaikolotno2;

    /**
     * 溶剤①_調合量2
     */
    private String youzai1_tyougouryou2;

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
    private String youzai2_tyougouryou1;

    /**
     * 溶剤②_部材在庫No2
     */
    private String youzai2_buzaizaikolotno2;

    /**
     * 溶剤②_調合量2
     */
    private String youzai2_tyougouryou2;

    /**
     * 担当者
     */
    private String tantousya;

    /**
     * ﾎﾟﾝﾌﾟ出力
     */
    private String pumpsyuturyoku;

    /**
     * ﾐﾙ周波数
     */
    private String millsyuuhasuu2;

    /**
     * 希釈溶剤添加
     */
    private Integer kisyakuyouzaitenka;

    /**
     * 溶剤循環時間
     */
    private String youzaijyunkanjikan;

    /**
     * 循環開始日時
     */
    private Timestamp jyunkankaisinichiji;

    /**
     * 循環終了日時
     */
    private Timestamp jyunkansyuuryounichiji;

    /**
     * 担当者
     */
    private String jyunkantantousya;

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
    private Integer sakujyoflg;

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
    public Integer getFunsaiki() {
        return funsaiki;
    }

    /**
     * 粉砕機
     * @param funsaiki the funsaiki to set
     */
    public void setFunsaiki(Integer funsaiki) {
        this.funsaiki = funsaiki;
    }

    /**
     * 粉砕号機
     * @return the funsaigouki
     */
    public Integer getFunsaigouki() {
        return funsaigouki;
    }

    /**
     * 粉砕号機
     * @param funsaigouki the funsaigouki to set
     */
    public void setFunsaigouki(Integer funsaigouki) {
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
    public Integer getRenzokuuntenkaisuu() {
        return renzokuuntenkaisuu;
    }

    /**
     * 連続運転回数
     * @param renzokuuntenkaisuu the renzokuuntenkaisuu to set
     */
    public void setRenzokuuntenkaisuu(Integer renzokuuntenkaisuu) {
        this.renzokuuntenkaisuu = renzokuuntenkaisuu;
    }

    /**
     * 投入量
     * @return the tounyuuryou
     */
    public Integer getTounyuuryou() {
        return tounyuuryou;
    }

    /**
     * 投入量
     * @param tounyuuryou the tounyuuryou to set
     */
    public void setTounyuuryou(Integer tounyuuryou) {
        this.tounyuuryou = tounyuuryou;
    }

    /**
     * 時間/ﾊﾟｽ回数
     * @return the jikan_passkaisuu
     */
    public Integer getJikan_passkaisuu() {
        return jikan_passkaisuu;
    }

    /**
     * 時間/ﾊﾟｽ回数
     * @param jikan_passkaisuu the jikan_passkaisuu to set
     */
    public void setJikan_passkaisuu(Integer jikan_passkaisuu) {
        this.jikan_passkaisuu = jikan_passkaisuu;
    }

    /**
     * ﾐﾙ周波数
     * @return the millsyuuhasuu
     */
    public Integer getMillsyuuhasuu() {
        return millsyuuhasuu;
    }

    /**
     * ﾐﾙ周波数
     * @param millsyuuhasuu the millsyuuhasuu to set
     */
    public void setMillsyuuhasuu(Integer millsyuuhasuu) {
        this.millsyuuhasuu = millsyuuhasuu;
    }
    
    /**
     * スクリーン
     * @return the screen
     */
    public Integer getScreen() {
        return screen;
    }

    /**
     * スクリーン
     * @param screen the screen to set
     */
    public void setScreen(Integer screen) {
        this.screen = screen;
    }
    
    /**
     * メディア径
     * @return the mediakei
     */
    public Integer getMediakei() {
        return mediakei;
    }

    /**
     * メディア径
     * @param mediakei the mediakei to set
     */
    public void setMediakei(Integer mediakei) {
        this.mediakei = mediakei;
    }
    
    /**
     * 玉石重量
     * @return the tamaisijuryo
     */
    public Integer getTamaisijuryo() {
        return tamaisijuryo;
    }

    /**
     * 玉石重量
     * @param tamaisijuryo the tamaisijuryo to set
     */
    public void setTamaisijuryo(Integer tamaisijuryo) {
        this.tamaisijuryo = tamaisijuryo;
    }
    

    /**
     * 周速
     * @return the syuusoku
     */
    public Integer getSyuusoku() {
        return syuusoku;
    }

    /**
     * 周速
     * @param syuusoku the syuusoku to set
     */
    public void setSyuusoku(Integer syuusoku) {
        this.syuusoku = syuusoku;
    }

    /**
     * ﾎﾟﾝﾌﾟ出力
     * @return the pumpsyuturyokcheck
     */
    public Integer getPumpsyuturyokcheck() {
        return pumpsyuturyokcheck;
    }

    /**
     * ﾎﾟﾝﾌﾟ出力
     * @param pumpsyuturyokcheck the pumpsyuturyokcheck to set
     */
    public void setPumpsyuturyokcheck(Integer pumpsyuturyokcheck) {
        this.pumpsyuturyokcheck = pumpsyuturyokcheck;
    }

    /**
     * 流量
     * @return the ryuuryou
     */
    public Integer getRyuuryou() {
        return ryuuryou;
    }

    /**
     * 流量
     * @param ryuuryou the ryuuryou to set
     */
    public void setRyuuryou(Integer ryuuryou) {
        this.ryuuryou = ryuuryou;
    }

    /**
     * ﾊﾟｽ回数
     * @return the passkaisuu
     */
    public Integer getPasskaisuu() {
        return passkaisuu;
    }

    /**
     * ﾊﾟｽ回数
     * @param passkaisuu the passkaisuu to set
     */
    public void setPasskaisuu(Integer passkaisuu) {
        this.passkaisuu = passkaisuu;
    }

    /**
     * ﾃﾞｨｽﾊﾟの種類
     * @return the dispanosyurui
     */
    public Integer getDispanosyurui() {
        return dispanosyurui;
    }

    /**
     * ﾃﾞｨｽﾊﾟの種類
     * @param dispanosyurui the dispanosyurui to set
     */
    public void setDispanosyurui(Integer dispanosyurui) {
        this.dispanosyurui = dispanosyurui;
    }

    /**
     * ﾃﾞｨｽﾊﾟ回転数
     * @return the dispakaitensuu
     */
    public Integer getDispakaitensuu() {
        return dispakaitensuu;
    }

    /**
     * ﾃﾞｨｽﾊﾟ回転数
     * @param dispakaitensuu the dispakaitensuu to set
     */
    public void setDispakaitensuu(Integer dispakaitensuu) {
        this.dispakaitensuu = dispakaitensuu;
    }

    /**
     * ﾊﾟｽ回数
     * @return the passkaisuu_sf1
     */
    public Integer getPasskaisuu_sf1() {
        return passkaisuu_sf1;
    }

    /**
     * ﾊﾟｽ回数
     * @param passkaisuu_sf1 the passkaisuu_sf1 to set
     */
    public void setPasskaisuu_sf1(Integer passkaisuu_sf1) {
        this.passkaisuu_sf1 = passkaisuu_sf1;
    }
    
    /**
     * ﾊﾟｽ回数
     * @return the passkaisuuinput_sf1
     */
    public Integer getPasskaisuuinput_sf1() {
        return passkaisuuinput_sf1;
    }

    /**
     * ﾊﾟｽ回数
     * @param passkaisuuinput_sf1 the passkaisuuinput_sf1 to set
     */
    public void setPasskaisuuinput_sf1(Integer passkaisuuinput_sf1) {
        this.passkaisuuinput_sf1 = passkaisuuinput_sf1;
    }


    /**
     * 開始日時
     * @return the kaisinichiji_sf1
     */
    public Timestamp getKaisinichiji_sf1() {
        return kaisinichiji_sf1;
    }

    /**
     * 開始日時
     * @param kaisinichiji_sf1 the kaisinichiji_sf1 to set
     */
    public void setKaisinichiji_sf1(Timestamp kaisinichiji_sf1) {
        this.kaisinichiji_sf1 = kaisinichiji_sf1;
    }

    /**
     * 終了予定日時
     * @return the syuuryouyoteinichiji_sf1
     */
    public Timestamp getSyuuryouyoteinichiji_sf1() {
        return syuuryouyoteinichiji_sf1;
    }

    /**
     * 終了予定日時
     * @param syuuryouyoteinichiji_sf1 the syuuryouyoteinichiji_sf1 to set
     */
    public void setSyuuryouyoteinichiji_sf1(Timestamp syuuryouyoteinichiji_sf1) {
        this.syuuryouyoteinichiji_sf1 = syuuryouyoteinichiji_sf1;
    }

    /**
     * 負荷電流値
     * @return the fukadenryuuti_sf1
     */
    public BigDecimal getFukadenryuuti_sf1() {
        return fukadenryuuti_sf1;
    }

    /**
     * 負荷電流値
     * @param fukadenryuuti_sf1 the fukadenryuuti_sf1 to set
     */
    public void setFukadenryuuti_sf1(BigDecimal fukadenryuuti_sf1) {
        this.fukadenryuuti_sf1 = fukadenryuuti_sf1;
    }

    /**
     * 製品温度
     * @return the seihinondo_sf1
     */
    public BigDecimal getSeihinondo_sf1() {
        return seihinondo_sf1;
    }

    /**
     * 製品温度
     * @param seihinondo_sf1 the seihinondo_sf1 to set
     */
    public void setSeihinondo_sf1(BigDecimal seihinondo_sf1) {
        this.seihinondo_sf1 = seihinondo_sf1;
    }

    /**
     * ｼｰﾙ温度
     * @return the stickerondo_sf1
     */
    public BigDecimal getStickerondo_sf1() {
        return stickerondo_sf1;
    }

    /**
     * ｼｰﾙ温度
     * @param stickerondo_sf1 the stickerondo_sf1 to set
     */
    public void setStickerondo_sf1(BigDecimal stickerondo_sf1) {
        this.stickerondo_sf1 = stickerondo_sf1;
    }

    /**
     * ﾎﾟﾝﾌﾟ目盛
     * @return the pumpmemori_sf1
     */
    public BigDecimal getPumpmemori_sf1() {
        return pumpmemori_sf1;
    }

    /**
     * ﾎﾟﾝﾌﾟ目盛
     * @param pumpmemori_sf1 the pumpmemori_sf1 to set
     */
    public void setPumpmemori_sf1(BigDecimal pumpmemori_sf1) {
        this.pumpmemori_sf1 = pumpmemori_sf1;
    }

    /**
     * ﾎﾟﾝﾌﾟ圧
     * @return the pumpatu_sf1
     */
    public BigDecimal getPumpatu_sf1() {
        return pumpatu_sf1;
    }

    /**
     * ﾎﾟﾝﾌﾟ圧
     * @param pumpatu_sf1 the pumpatu_sf1 to set
     */
    public void setPumpatu_sf1(BigDecimal pumpatu_sf1) {
        this.pumpatu_sf1 = pumpatu_sf1;
    }

    /**
     * 流量
     * @return the ryuuryou_sf1
     */
    public BigDecimal getRyuuryou_sf1() {
        return ryuuryou_sf1;
    }

    /**
     * 流量
     * @param ryuuryou_sf1 the ryuuryou_sf1 to set
     */
    public void setRyuuryou_sf1(BigDecimal ryuuryou_sf1) {
        this.ryuuryou_sf1 = ryuuryou_sf1;
    }
    
    /**
     * 粉砕担当者
     * @return the funsaitantousya_sf1
     */
    public String getFunsaitantousya_sf1() {
        return funsaitantousya_sf1;
    }

    /**
     * 粉砕担当者
     * @param funsaitantousya_sf1 the funsaitantousya_sf1 to set
     */
    public void setFunsaitantousya_sf1(String funsaitantousya_sf1) {
        this.funsaitantousya_sf1 = funsaitantousya_sf1;
    }
    
    /**
     * 粉砕確認者
     * @return the funsaikakuninsya_sf1
     */
    public String getFunsaikakuninsya_sf1() {
        return funsaikakuninsya_sf1;
    }

    /**
     * 粉砕確認者
     * @param funsaikakuninsya_sf1 the funsaikakuninsya_sf1 to set
     */
    public void setFunsaikakuninsya_sf1(String funsaikakuninsya_sf1) {
        this.funsaikakuninsya_sf1 = funsaikakuninsya_sf1;
    }

    /**
     * 備考1
     * @return the bikou1_sf1
     */
    public String getBikou1_sf1() {
        return bikou1_sf1;
    }

    /**
     * 備考1
     * @param bikou1_sf1 the bikou1_sf1 to set
     */
    public void setBikou1_sf1(String bikou1_sf1) {
        this.bikou1_sf1 = bikou1_sf1;
    }

    /**
     * 備考2
     * @return the bikou2_sf1
     */
    public String getBikou2_sf1() {
        return bikou2_sf1;
    }

    /**
     * 備考2
     * @param bikou2_sf1 the bikou2_sf1 to set
     */
    public void setBikou2_sf1(String bikou2_sf1) {
        this.bikou2_sf1 = bikou2_sf1;
    }

    /**
     * 温度(往)
     * @return the ondo_ou_sf1
     */
    public Integer getOndo_ou_sf1() {
        return ondo_ou_sf1;
    }

    /**
     * 温度(往)
     * @param ondo_ou_sf1 the ondo_ou_sf1 to set
     */
    public void setOndo_ou_sf1(Integer ondo_ou_sf1) {
        this.ondo_ou_sf1 = ondo_ou_sf1;
    }

    /**
     * 温度(還)
     * @return the ondo_kan_sf1
     */
    public Integer getOndo_kan_sf1() {
        return ondo_kan_sf1;
    }

    /**
     * 温度(還)
     * @param ondo_kan_sf1 the ondo_kan_sf1 to set
     */
    public void setOndo_kan_sf1(Integer ondo_kan_sf1) {
        this.ondo_kan_sf1 = ondo_kan_sf1;
    }

    /**
     * 圧力(往)
     * @return the aturyoku_ou_sf1
     */
    public BigDecimal getAturyoku_ou_sf1() {
        return aturyoku_ou_sf1;
    }

    /**
     * 圧力(往)
     * @param aturyoku_ou_sf1 the aturyoku_ou_sf1 to set
     */
    public void setAturyoku_ou_sf1(BigDecimal aturyoku_ou_sf1) {
        this.aturyoku_ou_sf1 = aturyoku_ou_sf1;
    }

    /**
     * 圧力(還)
     * @return the aturyoku_kan_sf1
     */
    public BigDecimal getAturyoku_kan_sf1() {
        return aturyoku_kan_sf1;
    }

    /**
     * 圧力(還)
     * @param aturyoku_kan_sf1 the aturyoku_kan_sf1 to set
     */
    public void setAturyoku_kan_sf1(BigDecimal aturyoku_kan_sf1) {
        this.aturyoku_kan_sf1 = aturyoku_kan_sf1;
    }

    /**
     * 終了日時
     * @return the syuuryounichiji_sf1
     */
    public Timestamp getSyuuryounichiji_sf1() {
        return syuuryounichiji_sf1;
    }

    /**
     * 終了日時
     * @param syuuryounichiji_sf1 the syuuryounichiji_sf1 to set
     */
    public void setSyuuryounichiji_sf1(Timestamp syuuryounichiji_sf1) {
        this.syuuryounichiji_sf1 = syuuryounichiji_sf1;
    }

    /**
     * 担当者
     * @return the tantousya_sf1
     */
    public String getTantousya_sf1() {
        return tantousya_sf1;
    }

    /**
     * 担当者
     * @param tantousya_sf1 the tantousya_sf1 to set
     */
    public void setTantousya_sf1(String tantousya_sf1) {
        this.tantousya_sf1 = tantousya_sf1;
    }

    /**
     * ﾊﾟｽ回数
     * @return the passkaisuu_sf2
     */
    public Integer getPasskaisuu_sf2() {
        return passkaisuu_sf2;
    }

    /**
     * ﾊﾟｽ回数
     * @param passkaisuu_sf2 the passkaisuu_sf2 to set
     */
    public void setPasskaisuu_sf2(Integer passkaisuu_sf2) {
        this.passkaisuu_sf2 = passkaisuu_sf2;
    }
    
    /**
     * ﾊﾟｽ回数
     * @return the passkaisuuinput_sf2
     */
    public Integer getPasskaisuuinput_sf2() {
        return passkaisuuinput_sf2;
    }

    /**
     * ﾊﾟｽ回数
     * @param passkaisuuinput_sf2 the passkaisuuinput_sf2 to set
     */
    public void setPasskaisuuinput_sf2(Integer passkaisuuinput_sf2) {
        this.passkaisuuinput_sf2 = passkaisuuinput_sf2;
    }

    /**
     * 開始日時
     * @return the kaisinichiji_sf2
     */
    public Timestamp getKaisinichiji_sf2() {
        return kaisinichiji_sf2;
    }

    /**
     * 開始日時
     * @param kaisinichiji_sf2 the kaisinichiji_sf2 to set
     */
    public void setKaisinichiji_sf2(Timestamp kaisinichiji_sf2) {
        this.kaisinichiji_sf2 = kaisinichiji_sf2;
    }

    /**
     * 終了予定日時
     * @return the syuuryouyoteinichiji_sf2
     */
    public Timestamp getSyuuryouyoteinichiji_sf2() {
        return syuuryouyoteinichiji_sf2;
    }

    /**
     * 終了予定日時
     * @param syuuryouyoteinichiji_sf2 the syuuryouyoteinichiji_sf2 to set
     */
    public void setSyuuryouyoteinichiji_sf2(Timestamp syuuryouyoteinichiji_sf2) {
        this.syuuryouyoteinichiji_sf2 = syuuryouyoteinichiji_sf2;
    }

    /**
     * 負荷電流値
     * @return the fukadenryuuti_sf2
     */
    public BigDecimal getFukadenryuuti_sf2() {
        return fukadenryuuti_sf2;
    }

    /**
     * 負荷電流値
     * @param fukadenryuuti_sf2 the fukadenryuuti_sf2 to set
     */
    public void setFukadenryuuti_sf2(BigDecimal fukadenryuuti_sf2) {
        this.fukadenryuuti_sf2 = fukadenryuuti_sf2;
    }

    /**
     * 製品温度
     * @return the seihinondo_sf2
     */
    public BigDecimal getSeihinondo_sf2() {
        return seihinondo_sf2;
    }

    /**
     * 製品温度
     * @param seihinondo_sf2 the seihinondo_sf2 to set
     */
    public void setSeihinondo_sf2(BigDecimal seihinondo_sf2) {
        this.seihinondo_sf2 = seihinondo_sf2;
    }

    /**
     * ｼｰﾙ温度
     * @return the stickerondo_sf2
     */
    public BigDecimal getStickerondo_sf2() {
        return stickerondo_sf2;
    }

    /**
     * ｼｰﾙ温度
     * @param stickerondo_sf2 the stickerondo_sf2 to set
     */
    public void setStickerondo_sf2(BigDecimal stickerondo_sf2) {
        this.stickerondo_sf2 = stickerondo_sf2;
    }

    /**
     * ﾎﾟﾝﾌﾟ目盛
     * @return the pumpmemori_sf2
     */
    public BigDecimal getPumpmemori_sf2() {
        return pumpmemori_sf2;
    }

    /**
     * ﾎﾟﾝﾌﾟ目盛
     * @param pumpmemori_sf2 the pumpmemori_sf2 to set
     */
    public void setPumpmemori_sf2(BigDecimal pumpmemori_sf2) {
        this.pumpmemori_sf2 = pumpmemori_sf2;
    }

    /**
     * ﾎﾟﾝﾌﾟ圧
     * @return the pumpatu_sf2
     */
    public BigDecimal getPumpatu_sf2() {
        return pumpatu_sf2;
    }

    /**
     * ﾎﾟﾝﾌﾟ圧
     * @param pumpatu_sf2 the pumpatu_sf2 to set
     */
    public void setPumpatu_sf2(BigDecimal pumpatu_sf2) {
        this.pumpatu_sf2 = pumpatu_sf2;
    }

    /**
     * 終了日時
     * @return the syuuryounichiji_sf2
     */
    public Timestamp getSyuuryounichiji_sf2() {
        return syuuryounichiji_sf2;
    }

    /**
     * 終了日時
     * @param syuuryounichiji_sf2 the syuuryounichiji_sf2 to set
     */
    public void setSyuuryounichiji_sf2(Timestamp syuuryounichiji_sf2) {
        this.syuuryounichiji_sf2 = syuuryounichiji_sf2;
    }

    /**
     * 流量
     * @return the ryuuryou_sf2
     */
    public BigDecimal getRyuuryou_sf2() {
        return ryuuryou_sf2;
    }

    /**
     * 流量
     * @param ryuuryou_sf2 the ryuuryou_sf2 to set
     */
    public void setRyuuryou_sf2(BigDecimal ryuuryou_sf2) {
        this.ryuuryou_sf2 = ryuuryou_sf2;
    }

    /**
     * 備考1
     * @return the bikou1_sf2
     */
    public String getBikou1_sf2() {
        return bikou1_sf2;
    }

    /**
     * 備考1
     * @param bikou1_sf2 the bikou1_sf2 to set
     */
    public void setBikou1_sf2(String bikou1_sf2) {
        this.bikou1_sf2 = bikou1_sf2;
    }

    /**
     * 備考2
     * @return the bikou2_sf2
     */
    public String getBikou2_sf2() {
        return bikou2_sf2;
    }

    /**
     * 備考2
     * @param bikou2_sf2 the bikou2_sf2 to set
     */
    public void setBikou2_sf2(String bikou2_sf2) {
        this.bikou2_sf2 = bikou2_sf2;
    }

    /**
     * 担当者
     * @return the tantousya_sf2
     */
    public String getTantousya_sf2() {
        return tantousya_sf2;
    }

    /**
     * 担当者
     * @param tantousya_sf2 the tantousya_sf2 to set
     */
    public void setTantousya_sf2(String tantousya_sf2) {
        this.tantousya_sf2 = tantousya_sf2;
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
    public String getYouzai1_tyougouryou1() {
        return youzai1_tyougouryou1;
    }

    /**
     * 溶剤①_調合量1
     * @param youzai1_tyougouryou1 the youzai1_tyougouryou1 to set
     */
    public void setYouzai1_tyougouryou1(String youzai1_tyougouryou1) {
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
    public String getYouzai1_tyougouryou2() {
        return youzai1_tyougouryou2;
    }

    /**
     * 溶剤①_調合量2
     * @param youzai1_tyougouryou2 the youzai1_tyougouryou2 to set
     */
    public void setYouzai1_tyougouryou2(String youzai1_tyougouryou2) {
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
    public String getYouzai2_tyougouryou1() {
        return youzai2_tyougouryou1;
    }

    /**
     * 溶剤②_調合量1
     * @param youzai2_tyougouryou1 the youzai2_tyougouryou1 to set
     */
    public void setYouzai2_tyougouryou1(String youzai2_tyougouryou1) {
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
    public String getYouzai2_tyougouryou2() {
        return youzai2_tyougouryou2;
    }

    /**
     * 溶剤②_調合量2
     * @param youzai2_tyougouryou2 the youzai2_tyougouryou2 to set
     */
    public void setYouzai2_tyougouryou2(String youzai2_tyougouryou2) {
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
    public Integer getKisyakuyouzaitenka() {
        return kisyakuyouzaitenka;
    }

    /**
     * 希釈溶剤添加
     * @param kisyakuyouzaitenka the kisyakuyouzaitenka to set
     */
    public void setKisyakuyouzaitenka(Integer kisyakuyouzaitenka) {
        this.kisyakuyouzaitenka = kisyakuyouzaitenka;
    }

    /**
     * 溶剤循環時間
     * @return the youzaijyunkanjikan
     */
    public String getYouzaijyunkanjikan() {
        return youzaijyunkanjikan;
    }

    /**
     * 溶剤循環時間
     * @param youzaijyunkanjikan the youzaijyunkanjikan to set
     */
    public void setYouzaijyunkanjikan(String youzaijyunkanjikan) {
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
     * @return the sakujyoflg
     */
    public Integer getSakujyoflg() {
        return sakujyoflg;
    }

    /**
     * 削除ﾌﾗｸﾞ
     * @param sakujyoflg the sakujyoflg to set
     */
    public void setSakujyoflg(Integer sakujyoflg) {
        this.sakujyoflg = sakujyoflg;
    }
}