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
 * 変更日	2021/10/18<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B011(添加材ｽﾗﾘｰ作製・粉砕)
 *
 * @author KCSS K.Jo
 * @since  2021/10/18
 */
@ViewScoped
@Named
public class GXHDO102B011A implements Serializable {
    /**
     * WIPﾛｯﾄNo
     */
    private FXHDD01 wiplotno;

    /**
     * 添加材ｽﾗﾘｰ品名
     */
    private FXHDD01 tenkazaislurryhinmei;

    /**
     * 添加材ｽﾗﾘｰLotNo
     */
    private FXHDD01 tenkazaislurrylotno;

    /**
     * ﾛｯﾄ区分
     */
    private FXHDD01 lotkubun;

    /**
     * 秤量号機
     */
    private FXHDD01 hyouryougouki;

    /**
     * 粉砕機
     */
    private FXHDD01 funsaiki;

    /**
     * 粉砕号機
     */
    private FXHDD01 funsaigouki;

    /**
     * ﾒﾃﾞｨｱLotNo
     */
    private FXHDD01 medialotno;

    /**
     * 連続運転回数
     */
    private FXHDD01 renzokuuntenkaisuu;

    /**
     * 投入量
     */
    private FXHDD01 tounyuuryou;

    /**
     * 時間/ﾊﾟｽ回数
     */
    private FXHDD01 jikan_passkaisuu;

    /**
     * ﾐﾙ周波数
     */
    private FXHDD01 millsyuuhasuu;
    
    /**
     * スクリーン
     */
    private FXHDD01 screen;
    
    /**
     * メディア径
     */
    private FXHDD01 mediakei;
    
    /**
     * 玉石重量
     */
    private FXHDD01 tamaisijuryo;

    /**
     * 周速
     */
    private FXHDD01 syuusoku;

    /**
     * ﾎﾟﾝﾌﾟ出力
     */
    private FXHDD01 pumpsyuturyokcheck;

    /**
     * 流量
     */
    private FXHDD01 ryuuryou;

    /**
     * ﾊﾟｽ回数
     */
    private FXHDD01 passkaisuu;

    /**
     * ﾃﾞｨｽﾊﾟの種類
     */
    private FXHDD01 dispanosyurui;

    /**
     * ﾃﾞｨｽﾊﾟ回転数
     */
    private FXHDD01 dispakaitensuu;

    /**
     * ﾊﾟｽ回数
     */
    private FXHDD01 passkaisuu_sf1;
    
    /**
     * ﾊﾟｽ回数
     */
    private FXHDD01 passkaisuu_sf1_input;

    /**
     * 開始日
     */
    private FXHDD01 kaisi_sf1_day;

    /**
     * 開始時間
     */
    private FXHDD01 kaisi_sf1_time;

    /**
     * 終了予定日
     */
    private FXHDD01 syuuryouyotei_sf1_day;

    /**
     * 終了予定時間
     */
    private FXHDD01 syuuryouyotei_sf1_time;

    /**
     * 負荷電流値
     */
    private FXHDD01 fukadenryuuti_sf1;

    /**
     * 製品温度
     */
    private FXHDD01 seihinondo_sf1;

    /**
     * ｼｰﾙ温度
     */
    private FXHDD01 stickerondo_sf1;

    /**
     * ﾎﾟﾝﾌﾟ目盛
     */
    private FXHDD01 pumpmemori_sf1;

    /**
     * ﾎﾟﾝﾌﾟ圧
     */
    private FXHDD01 pumpatu_sf1;

    /**
     * 流量
     */
    private FXHDD01 ryuuryou_sf1;
    
    /**
     * 粉砕担当者
     */
    private FXHDD01 funsaitantousya_sf1;
    
    /**
     * 粉砕確認者
     */
    private FXHDD01 funsaikakuninsya_sf1;

    /**
     * 備考1
     */
    private FXHDD01 bikou1_sf1;

    /**
     * 備考2
     */
    private FXHDD01 bikou2_sf1;

    /**
     * 温度(往)
     */
    private FXHDD01 ondo_ou_sf1;

    /**
     * 温度(還)
     */
    private FXHDD01 ondo_kan_sf1;

    /**
     * 圧力(往)
     */
    private FXHDD01 aturyoku_ou_sf1;

    /**
     * 圧力(還)
     */
    private FXHDD01 aturyoku_kan_sf1;

    /**
     * 終了日
     */
    private FXHDD01 syuuryou_sf1_day;

    /**
     * 終了時間
     */
    private FXHDD01 syuuryou_sf1_time;

    /**
     * 担当者
     */
    private FXHDD01 tantousya_sf1;

    /**
     * ﾊﾟｽ回数
     */
    private FXHDD01 passkaisuu_sf2;
    
    /**
     * ﾊﾟｽ回数
     */
    private FXHDD01 passkaisuu_sf2_input;

    /**
     * 開始日
     */
    private FXHDD01 kaisi_sf2_day;

    /**
     * 開始時間
     */
    private FXHDD01 kaisi_sf2_time;

    /**
     * 終了予定日
     */
    private FXHDD01 syuuryouyotei_sf2_day;

    /**
     * 終了予定時間
     */
    private FXHDD01 syuuryouyotei_sf2_time;

    /**
     * 負荷電流値
     */
    private FXHDD01 fukadenryuuti_sf2;

    /**
     * 製品温度
     */
    private FXHDD01 seihinondo_sf2;

    /**
     * ｼｰﾙ温度
     */
    private FXHDD01 stickerondo_sf2;

    /**
     * ﾎﾟﾝﾌﾟ目盛
     */
    private FXHDD01 pumpmemori_sf2;

    /**
     * ﾎﾟﾝﾌﾟ圧
     */
    private FXHDD01 pumpatu_sf2;

    /**
     * 終了日
     */
    private FXHDD01 syuuryou_sf2_day;

    /**
     * 終了時間
     */
    private FXHDD01 syuuryou_sf2_time;

    /**
     * 流量
     */
    private FXHDD01 ryuuryou_sf2;

    /**
     * 備考1
     */
    private FXHDD01 bikou1_sf2;

    /**
     * 備考2
     */
    private FXHDD01 bikou2_sf2;

    /**
     * 担当者
     */
    private FXHDD01 tantousya_sf2;

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
     * 担当者
     */
    private FXHDD01 tantousya;

    /**
     * ﾎﾟﾝﾌﾟ出力
     */
    private FXHDD01 pumpsyuturyoku;

    /**
     * ﾐﾙ周波数
     */
    private FXHDD01 millsyuuhasuu2;

    /**
     * 希釈溶剤添加
     */
    private FXHDD01 kisyakuyouzaitenka;

    /**
     * 溶剤循環時間
     */
    private FXHDD01 youzaijyunkanjikan;

    /**
     * 循環開始日
     */
    private FXHDD01 jyunkankaisi_day;

    /**
     * 循環開始時間
     */
    private FXHDD01 jyunkankaisi_time;

    /**
     * 循環終了日
     */
    private FXHDD01 jyunkansyuuryou_day;

    /**
     * 循環終了時間
     */
    private FXHDD01 jyunkansyuuryou_time;

    /**
     * 担当者
     */
    private FXHDD01 jyunkantantousya;

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
     * 添加材ｽﾗﾘｰ品名
     * @return the tenkazaislurryhinmei
     */
    public FXHDD01 getTenkazaislurryhinmei() {
        return tenkazaislurryhinmei;
    }

    /**
     * 添加材ｽﾗﾘｰ品名
     * @param tenkazaislurryhinmei the tenkazaislurryhinmei to set
     */
    public void setTenkazaislurryhinmei(FXHDD01 tenkazaislurryhinmei) {
        this.tenkazaislurryhinmei = tenkazaislurryhinmei;
    }

    /**
     * 添加材ｽﾗﾘｰLotNo
     * @return the tenkazaislurrylotno
     */
    public FXHDD01 getTenkazaislurrylotno() {
        return tenkazaislurrylotno;
    }

    /**
     * 添加材ｽﾗﾘｰLotNo
     * @param tenkazaislurrylotno the tenkazaislurrylotno to set
     */
    public void setTenkazaislurrylotno(FXHDD01 tenkazaislurrylotno) {
        this.tenkazaislurrylotno = tenkazaislurrylotno;
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
     * 秤量号機
     * @return the hyouryougouki
     */
    public FXHDD01 getHyouryougouki() {
        return hyouryougouki;
    }

    /**
     * 秤量号機
     * @param hyouryougouki the hyouryougouki to set
     */
    public void setHyouryougouki(FXHDD01 hyouryougouki) {
        this.hyouryougouki = hyouryougouki;
    }

    /**
     * 粉砕機
     * @return the funsaiki
     */
    public FXHDD01 getFunsaiki() {
        return funsaiki;
    }

    /**
     * 粉砕機
     * @param funsaiki the funsaiki to set
     */
    public void setFunsaiki(FXHDD01 funsaiki) {
        this.funsaiki = funsaiki;
    }

    /**
     * 粉砕号機
     * @return the funsaigouki
     */
    public FXHDD01 getFunsaigouki() {
        return funsaigouki;
    }

    /**
     * 粉砕号機
     * @param funsaigouki the funsaigouki to set
     */
    public void setFunsaigouki(FXHDD01 funsaigouki) {
        this.funsaigouki = funsaigouki;
    }

    /**
     * ﾒﾃﾞｨｱLotNo
     * @return the medialotno
     */
    public FXHDD01 getMedialotno() {
        return medialotno;
    }

    /**
     * ﾒﾃﾞｨｱLotNo
     * @param medialotno the medialotno to set
     */
    public void setMedialotno(FXHDD01 medialotno) {
        this.medialotno = medialotno;
    }

    /**
     * 連続運転回数
     * @return the renzokuuntenkaisuu
     */
    public FXHDD01 getRenzokuuntenkaisuu() {
        return renzokuuntenkaisuu;
    }

    /**
     * 連続運転回数
     * @param renzokuuntenkaisuu the renzokuuntenkaisuu to set
     */
    public void setRenzokuuntenkaisuu(FXHDD01 renzokuuntenkaisuu) {
        this.renzokuuntenkaisuu = renzokuuntenkaisuu;
    }

    /**
     * 投入量
     * @return the tounyuuryou
     */
    public FXHDD01 getTounyuuryou() {
        return tounyuuryou;
    }

    /**
     * 投入量
     * @param tounyuuryou the tounyuuryou to set
     */
    public void setTounyuuryou(FXHDD01 tounyuuryou) {
        this.tounyuuryou = tounyuuryou;
    }

    /**
     * 時間/ﾊﾟｽ回数
     * @return the jikan_passkaisuu
     */
    public FXHDD01 getJikan_passkaisuu() {
        return jikan_passkaisuu;
    }

    /**
     * 時間/ﾊﾟｽ回数
     * @param jikan_passkaisuu the jikan_passkaisuu to set
     */
    public void setJikan_passkaisuu(FXHDD01 jikan_passkaisuu) {
        this.jikan_passkaisuu = jikan_passkaisuu;
    }

    /**
     * ﾐﾙ周波数
     * @return the millsyuuhasuu
     */
    public FXHDD01 getMillsyuuhasuu() {
        return millsyuuhasuu;
    }

    /**
     * ﾐﾙ周波数
     * @param millsyuuhasuu the millsyuuhasuu to set
     */
    public void setMillsyuuhasuu(FXHDD01 millsyuuhasuu) {
        this.millsyuuhasuu = millsyuuhasuu;
    }
    
    /**
     * スクリーン
     * @return the screen
     */
    public FXHDD01 getScreen() {
        return screen;
    }

    /**
     * スクリーン
     * @param screen the screen to set
     */
    public void setScreen(FXHDD01 screen) {
        this.screen = screen;
    }
    
    /**
     * メディア径
     * @return the mediakei
     */
    public FXHDD01 getMediakei() {
        return mediakei;
    }

    /**
     * メディア径
     * @param mediakei the mediakei to set
     */
    public void setMediakei(FXHDD01 mediakei) {
        this.mediakei = mediakei;
    }
    
    /**
     * 玉石重量
     * @return the tamaisijuryo
     */
    public FXHDD01 getTamaisijuryo() {
        return tamaisijuryo;
    }

    /**
     * 玉石重量
     * @param tamaisijuryo the tamaisijuryo to set
     */
    public void setTamaisijuryo(FXHDD01 tamaisijuryo) {
        this.tamaisijuryo = tamaisijuryo;
    }

    /**
     * 周速
     * @return the syuusoku
     */
    public FXHDD01 getSyuusoku() {
        return syuusoku;
    }

    /**
     * 周速
     * @param syuusoku the syuusoku to set
     */
    public void setSyuusoku(FXHDD01 syuusoku) {
        this.syuusoku = syuusoku;
    }

    /**
     * ﾎﾟﾝﾌﾟ出力
     * @return the pumpsyuturyokcheck
     */
    public FXHDD01 getPumpsyuturyokcheck() {
        return pumpsyuturyokcheck;
    }

    /**
     * ﾎﾟﾝﾌﾟ出力
     * @param pumpsyuturyokcheck the pumpsyuturyokcheck to set
     */
    public void setPumpsyuturyokcheck(FXHDD01 pumpsyuturyokcheck) {
        this.pumpsyuturyokcheck = pumpsyuturyokcheck;
    }

    /**
     * 流量
     * @return the ryuuryou
     */
    public FXHDD01 getRyuuryou() {
        return ryuuryou;
    }

    /**
     * 流量
     * @param ryuuryou the ryuuryou to set
     */
    public void setRyuuryou(FXHDD01 ryuuryou) {
        this.ryuuryou = ryuuryou;
    }

    /**
     * ﾊﾟｽ回数
     * @return the passkaisuu
     */
    public FXHDD01 getPasskaisuu() {
        return passkaisuu;
    }

    /**
     * ﾊﾟｽ回数
     * @param passkaisuu the passkaisuu to set
     */
    public void setPasskaisuu(FXHDD01 passkaisuu) {
        this.passkaisuu = passkaisuu;
    }

    /**
     * ﾃﾞｨｽﾊﾟの種類
     * @return the dispanosyurui
     */
    public FXHDD01 getDispanosyurui() {
        return dispanosyurui;
    }

    /**
     * ﾃﾞｨｽﾊﾟの種類
     * @param dispanosyurui the dispanosyurui to set
     */
    public void setDispanosyurui(FXHDD01 dispanosyurui) {
        this.dispanosyurui = dispanosyurui;
    }

    /**
     * ﾃﾞｨｽﾊﾟ回転数
     * @return the dispakaitensuu
     */
    public FXHDD01 getDispakaitensuu() {
        return dispakaitensuu;
    }

    /**
     * ﾃﾞｨｽﾊﾟ回転数
     * @param dispakaitensuu the dispakaitensuu to set
     */
    public void setDispakaitensuu(FXHDD01 dispakaitensuu) {
        this.dispakaitensuu = dispakaitensuu;
    }

    /**
     * ﾊﾟｽ回数
     * @return the passkaisuu_sf1
     */
    public FXHDD01 getPasskaisuu_sf1() {
        return passkaisuu_sf1;
    }

    /**
     * ﾊﾟｽ回数
     * @param passkaisuu_sf1 the passkaisuu_sf1 to set
     */
    public void setPasskaisuu_sf1(FXHDD01 passkaisuu_sf1) {
        this.passkaisuu_sf1 = passkaisuu_sf1;
    }
    
    /**
     * ﾊﾟｽ回数
     * @return the passkaisuu_sf1
     */
    public FXHDD01 getPasskaisuu_sf1_input() {
        return passkaisuu_sf1_input;
    }

    /**
     * ﾊﾟｽ回数
     * @param passkaisuu_sf1_input the passkaisuu_sf1 to set
     */
    public void setPasskaisuu_sf1_input(FXHDD01 passkaisuu_sf1_input) {
        this.passkaisuu_sf1_input = passkaisuu_sf1_input;
    }

    /**
     * 開始日
     * @return the kaisi_sf1_day
     */
    public FXHDD01 getKaisi_sf1_day() {
        return kaisi_sf1_day;
    }

    /**
     * 開始日
     * @param kaisi_sf1_day the kaisi_sf1_day to set
     */
    public void setKaisi_sf1_day(FXHDD01 kaisi_sf1_day) {
        this.kaisi_sf1_day = kaisi_sf1_day;
    }

    /**
     * 開始時間
     * @return the kaisi_sf1_time
     */
    public FXHDD01 getKaisi_sf1_time() {
        return kaisi_sf1_time;
    }

    /**
     * 開始時間
     * @param kaisi_sf1_time the kaisi_sf1_time to set
     */
    public void setKaisi_sf1_time(FXHDD01 kaisi_sf1_time) {
        this.kaisi_sf1_time = kaisi_sf1_time;
    }

    /**
     * 終了予定日
     * @return the syuuryouyotei_sf1_day
     */
    public FXHDD01 getSyuuryouyotei_sf1_day() {
        return syuuryouyotei_sf1_day;
    }

    /**
     * 終了予定日
     * @param syuuryouyotei_sf1_day the syuuryouyotei_sf1_day to set
     */
    public void setSyuuryouyotei_sf1_day(FXHDD01 syuuryouyotei_sf1_day) {
        this.syuuryouyotei_sf1_day = syuuryouyotei_sf1_day;
    }

    /**
     * 終了予定時間
     * @return the syuuryouyotei_sf1_time
     */
    public FXHDD01 getSyuuryouyotei_sf1_time() {
        return syuuryouyotei_sf1_time;
    }

    /**
     * 終了予定時間
     * @param syuuryouyotei_sf1_time the syuuryouyotei_sf1_time to set
     */
    public void setSyuuryouyotei_sf1_time(FXHDD01 syuuryouyotei_sf1_time) {
        this.syuuryouyotei_sf1_time = syuuryouyotei_sf1_time;
    }

    /**
     * 負荷電流値
     * @return the fukadenryuuti_sf1
     */
    public FXHDD01 getFukadenryuuti_sf1() {
        return fukadenryuuti_sf1;
    }

    /**
     * 負荷電流値
     * @param fukadenryuuti_sf1 the fukadenryuuti_sf1 to set
     */
    public void setFukadenryuuti_sf1(FXHDD01 fukadenryuuti_sf1) {
        this.fukadenryuuti_sf1 = fukadenryuuti_sf1;
    }

    /**
     * 製品温度
     * @return the seihinondo_sf1
     */
    public FXHDD01 getSeihinondo_sf1() {
        return seihinondo_sf1;
    }

    /**
     * 製品温度
     * @param seihinondo_sf1 the seihinondo_sf1 to set
     */
    public void setSeihinondo_sf1(FXHDD01 seihinondo_sf1) {
        this.seihinondo_sf1 = seihinondo_sf1;
    }

    /**
     * ｼｰﾙ温度
     * @return the stickerondo_sf1
     */
    public FXHDD01 getStickerondo_sf1() {
        return stickerondo_sf1;
    }

    /**
     * ｼｰﾙ温度
     * @param stickerondo_sf1 the stickerondo_sf1 to set
     */
    public void setStickerondo_sf1(FXHDD01 stickerondo_sf1) {
        this.stickerondo_sf1 = stickerondo_sf1;
    }

    /**
     * ﾎﾟﾝﾌﾟ目盛
     * @return the pumpmemori_sf1
     */
    public FXHDD01 getPumpmemori_sf1() {
        return pumpmemori_sf1;
    }

    /**
     * ﾎﾟﾝﾌﾟ目盛
     * @param pumpmemori_sf1 the pumpmemori_sf1 to set
     */
    public void setPumpmemori_sf1(FXHDD01 pumpmemori_sf1) {
        this.pumpmemori_sf1 = pumpmemori_sf1;
    }

    /**
     * ﾎﾟﾝﾌﾟ圧
     * @return the pumpatu_sf1
     */
    public FXHDD01 getPumpatu_sf1() {
        return pumpatu_sf1;
    }

    /**
     * ﾎﾟﾝﾌﾟ圧
     * @param pumpatu_sf1 the pumpatu_sf1 to set
     */
    public void setPumpatu_sf1(FXHDD01 pumpatu_sf1) {
        this.pumpatu_sf1 = pumpatu_sf1;
    }

    /**
     * 流量
     * @return the ryuuryou_sf1
     */
    public FXHDD01 getRyuuryou_sf1() {
        return ryuuryou_sf1;
    }

    /**
     * 流量
     * @param ryuuryou_sf1 the ryuuryou_sf1 to set
     */
    public void setRyuuryou_sf1(FXHDD01 ryuuryou_sf1) {
        this.ryuuryou_sf1 = ryuuryou_sf1;
    }
    
    /**
     * 粉砕担当者
     * @return the funsaitantousya_sf1
     */
    public FXHDD01 getFunsaitantousya_sf1() {
        return funsaitantousya_sf1;
    }

    /**
     * 粉砕担当者
     * @param funsaitantousya_sf1 the funsaitantousya_sf1 to set
     */
    public void setFunsaitantousya_sf1(FXHDD01 funsaitantousya_sf1) {
        this.funsaitantousya_sf1 = funsaitantousya_sf1;
    }
    
        
    /**
     * 粉砕確認者
     * @return the funsaikakuninsya_sf1
     */
    public FXHDD01 getFunsaikakuninsya_sf1() {
        return funsaikakuninsya_sf1;
    }

    /**
     * 粉砕確認者
     * @param funsaikakuninsya_sf1 the funsaikakuninsya_sf1 to set
     */
    public void setFunsaikakuninsya_sf1(FXHDD01 funsaikakuninsya_sf1) {
        this.funsaikakuninsya_sf1 = funsaikakuninsya_sf1;
    }

    /**
     * 備考1
     * @return the bikou1_sf1
     */
    public FXHDD01 getBikou1_sf1() {
        return bikou1_sf1;
    }

    /**
     * 備考1
     * @param bikou1_sf1 the bikou1_sf1 to set
     */
    public void setBikou1_sf1(FXHDD01 bikou1_sf1) {
        this.bikou1_sf1 = bikou1_sf1;
    }

    /**
     * 備考2
     * @return the bikou2_sf1
     */
    public FXHDD01 getBikou2_sf1() {
        return bikou2_sf1;
    }

    /**
     * 備考2
     * @param bikou2_sf1 the bikou2_sf1 to set
     */
    public void setBikou2_sf1(FXHDD01 bikou2_sf1) {
        this.bikou2_sf1 = bikou2_sf1;
    }

    /**
     * 温度(往)
     * @return the ondo_ou_sf1
     */
    public FXHDD01 getOndo_ou_sf1() {
        return ondo_ou_sf1;
    }

    /**
     * 温度(往)
     * @param ondo_ou_sf1 the ondo_ou_sf1 to set
     */
    public void setOndo_ou_sf1(FXHDD01 ondo_ou_sf1) {
        this.ondo_ou_sf1 = ondo_ou_sf1;
    }

    /**
     * 温度(還)
     * @return the ondo_kan_sf1
     */
    public FXHDD01 getOndo_kan_sf1() {
        return ondo_kan_sf1;
    }

    /**
     * 温度(還)
     * @param ondo_kan_sf1 the ondo_kan_sf1 to set
     */
    public void setOndo_kan_sf1(FXHDD01 ondo_kan_sf1) {
        this.ondo_kan_sf1 = ondo_kan_sf1;
    }

    /**
     * 圧力(往)
     * @return the aturyoku_ou_sf1
     */
    public FXHDD01 getAturyoku_ou_sf1() {
        return aturyoku_ou_sf1;
    }

    /**
     * 圧力(往)
     * @param aturyoku_ou_sf1 the aturyoku_ou_sf1 to set
     */
    public void setAturyoku_ou_sf1(FXHDD01 aturyoku_ou_sf1) {
        this.aturyoku_ou_sf1 = aturyoku_ou_sf1;
    }

    /**
     * 圧力(還)
     * @return the aturyoku_kan_sf1
     */
    public FXHDD01 getAturyoku_kan_sf1() {
        return aturyoku_kan_sf1;
    }

    /**
     * 圧力(還)
     * @param aturyoku_kan_sf1 the aturyoku_kan_sf1 to set
     */
    public void setAturyoku_kan_sf1(FXHDD01 aturyoku_kan_sf1) {
        this.aturyoku_kan_sf1 = aturyoku_kan_sf1;
    }

    /**
     * 終了日
     * @return the syuuryou_sf1_day
     */
    public FXHDD01 getSyuuryou_sf1_day() {
        return syuuryou_sf1_day;
    }

    /**
     * 終了日
     * @param syuuryou_sf1_day the syuuryou_sf1_day to set
     */
    public void setSyuuryou_sf1_day(FXHDD01 syuuryou_sf1_day) {
        this.syuuryou_sf1_day = syuuryou_sf1_day;
    }

    /**
     * 終了時間
     * @return the syuuryou_sf1_time
     */
    public FXHDD01 getSyuuryou_sf1_time() {
        return syuuryou_sf1_time;
    }

    /**
     * 終了時間
     * @param syuuryou_sf1_time the syuuryou_sf1_time to set
     */
    public void setSyuuryou_sf1_time(FXHDD01 syuuryou_sf1_time) {
        this.syuuryou_sf1_time = syuuryou_sf1_time;
    }

    /**
     * 担当者
     * @return the tantousya_sf1
     */
    public FXHDD01 getTantousya_sf1() {
        return tantousya_sf1;
    }

    /**
     * 担当者
     * @param tantousya_sf1 the tantousya_sf1 to set
     */
    public void setTantousya_sf1(FXHDD01 tantousya_sf1) {
        this.tantousya_sf1 = tantousya_sf1;
    }

    /**
     * ﾊﾟｽ回数
     * @return the passkaisuu_sf2
     */
    public FXHDD01 getPasskaisuu_sf2() {
        return passkaisuu_sf2;
    }

    /**
     * ﾊﾟｽ回数
     * @param passkaisuu_sf2 the passkaisuu_sf2 to set
     */
    public void setPasskaisuu_sf2(FXHDD01 passkaisuu_sf2) {
        this.passkaisuu_sf2 = passkaisuu_sf2;
    }
    
    /**
     * ﾊﾟｽ回数
     * @return the passkaisuu_sf2_input
     */
    public FXHDD01 getPasskaisuu_sf2_input() {
        return passkaisuu_sf2_input;
    }

    /**
     * ﾊﾟｽ回数
     * @param passkaisuu_sf2_input the passkaisuu_sf2 to set
     */
    public void setPasskaisuu_sf2_input(FXHDD01 passkaisuu_sf2_input) {
        this.passkaisuu_sf2_input = passkaisuu_sf2_input;
    }

    /**
     * 開始日
     * @return the kaisi_sf2_day
     */
    public FXHDD01 getKaisi_sf2_day() {
        return kaisi_sf2_day;
    }

    /**
     * 開始日
     * @param kaisi_sf2_day the kaisi_sf2_day to set
     */
    public void setKaisi_sf2_day(FXHDD01 kaisi_sf2_day) {
        this.kaisi_sf2_day = kaisi_sf2_day;
    }

    /**
     * 開始時間
     * @return the kaisi_sf2_time
     */
    public FXHDD01 getKaisi_sf2_time() {
        return kaisi_sf2_time;
    }

    /**
     * 開始時間
     * @param kaisi_sf2_time the kaisi_sf2_time to set
     */
    public void setKaisi_sf2_time(FXHDD01 kaisi_sf2_time) {
        this.kaisi_sf2_time = kaisi_sf2_time;
    }

    /**
     * 終了予定日
     * @return the syuuryouyotei_sf2_day
     */
    public FXHDD01 getSyuuryouyotei_sf2_day() {
        return syuuryouyotei_sf2_day;
    }

    /**
     * 終了予定日
     * @param syuuryouyotei_sf2_day the syuuryouyotei_sf2_day to set
     */
    public void setSyuuryouyotei_sf2_day(FXHDD01 syuuryouyotei_sf2_day) {
        this.syuuryouyotei_sf2_day = syuuryouyotei_sf2_day;
    }

    /**
     * 終了予定時間
     * @return the syuuryouyotei_sf2_time
     */
    public FXHDD01 getSyuuryouyotei_sf2_time() {
        return syuuryouyotei_sf2_time;
    }

    /**
     * 終了予定時間
     * @param syuuryouyotei_sf2_time the syuuryouyotei_sf2_time to set
     */
    public void setSyuuryouyotei_sf2_time(FXHDD01 syuuryouyotei_sf2_time) {
        this.syuuryouyotei_sf2_time = syuuryouyotei_sf2_time;
    }

    /**
     * 負荷電流値
     * @return the fukadenryuuti_sf2
     */
    public FXHDD01 getFukadenryuuti_sf2() {
        return fukadenryuuti_sf2;
    }

    /**
     * 負荷電流値
     * @param fukadenryuuti_sf2 the fukadenryuuti_sf2 to set
     */
    public void setFukadenryuuti_sf2(FXHDD01 fukadenryuuti_sf2) {
        this.fukadenryuuti_sf2 = fukadenryuuti_sf2;
    }

    /**
     * 製品温度
     * @return the seihinondo_sf2
     */
    public FXHDD01 getSeihinondo_sf2() {
        return seihinondo_sf2;
    }

    /**
     * 製品温度
     * @param seihinondo_sf2 the seihinondo_sf2 to set
     */
    public void setSeihinondo_sf2(FXHDD01 seihinondo_sf2) {
        this.seihinondo_sf2 = seihinondo_sf2;
    }

    /**
     * ｼｰﾙ温度
     * @return the stickerondo_sf2
     */
    public FXHDD01 getStickerondo_sf2() {
        return stickerondo_sf2;
    }

    /**
     * ｼｰﾙ温度
     * @param stickerondo_sf2 the stickerondo_sf2 to set
     */
    public void setStickerondo_sf2(FXHDD01 stickerondo_sf2) {
        this.stickerondo_sf2 = stickerondo_sf2;
    }

    /**
     * ﾎﾟﾝﾌﾟ目盛
     * @return the pumpmemori_sf2
     */
    public FXHDD01 getPumpmemori_sf2() {
        return pumpmemori_sf2;
    }

    /**
     * ﾎﾟﾝﾌﾟ目盛
     * @param pumpmemori_sf2 the pumpmemori_sf2 to set
     */
    public void setPumpmemori_sf2(FXHDD01 pumpmemori_sf2) {
        this.pumpmemori_sf2 = pumpmemori_sf2;
    }

    /**
     * ﾎﾟﾝﾌﾟ圧
     * @return the pumpatu_sf2
     */
    public FXHDD01 getPumpatu_sf2() {
        return pumpatu_sf2;
    }

    /**
     * ﾎﾟﾝﾌﾟ圧
     * @param pumpatu_sf2 the pumpatu_sf2 to set
     */
    public void setPumpatu_sf2(FXHDD01 pumpatu_sf2) {
        this.pumpatu_sf2 = pumpatu_sf2;
    }

    /**
     * 終了日
     * @return the syuuryou_sf2_day
     */
    public FXHDD01 getSyuuryou_sf2_day() {
        return syuuryou_sf2_day;
    }

    /**
     * 終了日
     * @param syuuryou_sf2_day the syuuryou_sf2_day to set
     */
    public void setSyuuryou_sf2_day(FXHDD01 syuuryou_sf2_day) {
        this.syuuryou_sf2_day = syuuryou_sf2_day;
    }

    /**
     * 終了時間
     * @return the syuuryou_sf2_time
     */
    public FXHDD01 getSyuuryou_sf2_time() {
        return syuuryou_sf2_time;
    }

    /**
     * 終了時間
     * @param syuuryou_sf2_time the syuuryou_sf2_time to set
     */
    public void setSyuuryou_sf2_time(FXHDD01 syuuryou_sf2_time) {
        this.syuuryou_sf2_time = syuuryou_sf2_time;
    }

    /**
     * 流量
     * @return the ryuuryou_sf2
     */
    public FXHDD01 getRyuuryou_sf2() {
        return ryuuryou_sf2;
    }

    /**
     * 流量
     * @param ryuuryou_sf2 the ryuuryou_sf2 to set
     */
    public void setRyuuryou_sf2(FXHDD01 ryuuryou_sf2) {
        this.ryuuryou_sf2 = ryuuryou_sf2;
    }

    /**
     * 備考1
     * @return the bikou1_sf2
     */
    public FXHDD01 getBikou1_sf2() {
        return bikou1_sf2;
    }

    /**
     * 備考1
     * @param bikou1_sf2 the bikou1_sf2 to set
     */
    public void setBikou1_sf2(FXHDD01 bikou1_sf2) {
        this.bikou1_sf2 = bikou1_sf2;
    }

    /**
     * 備考2
     * @return the bikou2_sf2
     */
    public FXHDD01 getBikou2_sf2() {
        return bikou2_sf2;
    }

    /**
     * 備考2
     * @param bikou2_sf2 the bikou2_sf2 to set
     */
    public void setBikou2_sf2(FXHDD01 bikou2_sf2) {
        this.bikou2_sf2 = bikou2_sf2;
    }

    /**
     * 担当者
     * @return the tantousya_sf2
     */
    public FXHDD01 getTantousya_sf2() {
        return tantousya_sf2;
    }

    /**
     * 担当者
     * @param tantousya_sf2 the tantousya_sf2 to set
     */
    public void setTantousya_sf2(FXHDD01 tantousya_sf2) {
        this.tantousya_sf2 = tantousya_sf2;
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
     * ﾎﾟﾝﾌﾟ出力
     * @return the pumpsyuturyoku
     */
    public FXHDD01 getPumpsyuturyoku() {
        return pumpsyuturyoku;
    }

    /**
     * ﾎﾟﾝﾌﾟ出力
     * @param pumpsyuturyoku the pumpsyuturyoku to set
     */
    public void setPumpsyuturyoku(FXHDD01 pumpsyuturyoku) {
        this.pumpsyuturyoku = pumpsyuturyoku;
    }

    /**
     * ﾐﾙ周波数
     * @return the millsyuuhasuu2
     */
    public FXHDD01 getMillsyuuhasuu2() {
        return millsyuuhasuu2;
    }

    /**
     * ﾐﾙ周波数
     * @param millsyuuhasuu2 the millsyuuhasuu2 to set
     */
    public void setMillsyuuhasuu2(FXHDD01 millsyuuhasuu2) {
        this.millsyuuhasuu2 = millsyuuhasuu2;
    }

    /**
     * 希釈溶剤添加
     * @return the kisyakuyouzaitenka
     */
    public FXHDD01 getKisyakuyouzaitenka() {
        return kisyakuyouzaitenka;
    }

    /**
     * 希釈溶剤添加
     * @param kisyakuyouzaitenka the kisyakuyouzaitenka to set
     */
    public void setKisyakuyouzaitenka(FXHDD01 kisyakuyouzaitenka) {
        this.kisyakuyouzaitenka = kisyakuyouzaitenka;
    }

    /**
     * 溶剤循環時間
     * @return the youzaijyunkanjikan
     */
    public FXHDD01 getYouzaijyunkanjikan() {
        return youzaijyunkanjikan;
    }

    /**
     * 溶剤循環時間
     * @param youzaijyunkanjikan the youzaijyunkanjikan to set
     */
    public void setYouzaijyunkanjikan(FXHDD01 youzaijyunkanjikan) {
        this.youzaijyunkanjikan = youzaijyunkanjikan;
    }

    /**
     * 循環開始日
     * @return the jyunkankaisi_day
     */
    public FXHDD01 getJyunkankaisi_day() {
        return jyunkankaisi_day;
    }

    /**
     * 循環開始日
     * @param jyunkankaisi_day the jyunkankaisi_day to set
     */
    public void setJyunkankaisi_day(FXHDD01 jyunkankaisi_day) {
        this.jyunkankaisi_day = jyunkankaisi_day;
    }

    /**
     * 循環開始時間
     * @return the jyunkankaisi_time
     */
    public FXHDD01 getJyunkankaisi_time() {
        return jyunkankaisi_time;
    }

    /**
     * 循環開始時間
     * @param jyunkankaisi_time the jyunkankaisi_time to set
     */
    public void setJyunkankaisi_time(FXHDD01 jyunkankaisi_time) {
        this.jyunkankaisi_time = jyunkankaisi_time;
    }

    /**
     * 循環終了日
     * @return the jyunkansyuuryou_day
     */
    public FXHDD01 getJyunkansyuuryou_day() {
        return jyunkansyuuryou_day;
    }

    /**
     * 循環終了日
     * @param jyunkansyuuryou_day the jyunkansyuuryou_day to set
     */
    public void setJyunkansyuuryou_day(FXHDD01 jyunkansyuuryou_day) {
        this.jyunkansyuuryou_day = jyunkansyuuryou_day;
    }

    /**
     * 循環終了時間
     * @return the jyunkansyuuryou_time
     */
    public FXHDD01 getJyunkansyuuryou_time() {
        return jyunkansyuuryou_time;
    }

    /**
     * 循環終了時間
     * @param jyunkansyuuryou_time the jyunkansyuuryou_time to set
     */
    public void setJyunkansyuuryou_time(FXHDD01 jyunkansyuuryou_time) {
        this.jyunkansyuuryou_time = jyunkansyuuryou_time;
    }

    /**
     * 担当者
     * @return the jyunkantantousya
     */
    public FXHDD01 getJyunkantantousya() {
        return jyunkantantousya;
    }

    /**
     * 担当者
     * @param jyunkantantousya the jyunkantantousya to set
     */
    public void setJyunkantantousya(FXHDD01 jyunkantantousya) {
        this.jyunkantantousya = jyunkantantousya;
    }

}