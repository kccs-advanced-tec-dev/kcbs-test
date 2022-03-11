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
 * 変更日	2021/12/08<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B031(ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入)
 *
 * @author KCSS K.Jo
 * @since  2021/12/08
 */
@ViewScoped
@Named
public class GXHDO102B031A implements Serializable {
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
     * ﾊﾞｲﾝﾀﾞｰ混合設備
     */
    private FXHDD01 binderkongousetub;

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
     * ﾊﾞｲﾝﾀﾞｰ秤量日
     */
    private FXHDD01 binderkeiryou_day;

    /**
     * ﾊﾞｲﾝﾀﾞｰ秤量時間
     */
    private FXHDD01 binderkeiryou_time;

    /**
     * ﾊﾞｲﾝﾀﾞｰ品名
     */
    private FXHDD01 binderhinmei;

    /**
     * ﾊﾞｲﾝﾀﾞｰLotNo
     */
    private FXHDD01 binderlotno;

    /**
     * ﾊﾞｲﾝﾀﾞｰ固形分
     */
    private FXHDD01 binderkokeibun;

    /**
     * ﾊﾞｲﾝﾀﾞｰ有効期限
     */
    private FXHDD01 binderyuukoukigen;

    /**
     * ﾊﾞｲﾝﾀﾞｰ添加量規格
     */
    private FXHDD01 bindertenkaryoukikaku;

    /**
     * ﾎｯﾊﾟｰ使用
     */
    private FXHDD01 hoppersiyou;

    /**
     * 投入開始日
     */
    private FXHDD01 tounyuukaisi_day;

    /**
     * 投入開始時間
     */
    private FXHDD01 tounyuukaisi_time;

    /**
     * 投入時回転数
     */
    private FXHDD01 tounyuujikaitensuu;

    /**
     * 秤量①
     */
    private FXHDD01 hyouryou1;

    /**
     * 投入確認①
     */
    private FXHDD01 tounyuukakunin1;

    /**
     * 秤量②
     */
    private FXHDD01 hyouryou2;

    /**
     * 投入確認②
     */
    private FXHDD01 tounyuukakunin2;

    /**
     * 秤量③
     */
    private FXHDD01 hyouryou3;

    /**
     * 投入確認③
     */
    private FXHDD01 tounyuukakunin3;

    /**
     * 秤量④
     */
    private FXHDD01 hyouryou4;

    /**
     * 投入確認④
     */
    private FXHDD01 tounyuukakunin4;

    /**
     * 秤量⑤
     */
    private FXHDD01 hyouryou5;

    /**
     * 投入確認⑤
     */
    private FXHDD01 tounyuukakunin5;

    /**
     * 秤量⑥
     */
    private FXHDD01 hyouryou6;

    /**
     * 投入確認⑥
     */
    private FXHDD01 tounyuukakunin6;

    /**
     * 投入終了日
     */
    private FXHDD01 tounyuusyuuryou_day;

    /**
     * 投入終了時間
     */
    private FXHDD01 tounyuusyuuryou_time;

    /**
     * ﾊﾞｲﾝﾀﾞｰ添加量合計
     */
    private FXHDD01 bindertenkaryougoukei;

    /**
     * 担当者
     */
    private FXHDD01 tantousya;

    /**
     * 備考1
     */
    private FXHDD01 bikou1;

    /**
     * 備考2
     */
    private FXHDD01 bikou2;

    /**
     * コンストラクタ
     */
    public GXHDO102B031A() {
    }

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
     * ﾊﾞｲﾝﾀﾞｰ秤量日
     * @return the binderkeiryou_day
     */
    public FXHDD01 getBinderkeiryou_day() {
        return binderkeiryou_day;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ秤量日
     * @param binderkeiryou_day the binderkeiryou_day to set
     */
    public void setBinderkeiryou_day(FXHDD01 binderkeiryou_day) {
        this.binderkeiryou_day = binderkeiryou_day;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ秤量時間
     * @return the binderkeiryou_time
     */
    public FXHDD01 getBinderkeiryou_time() {
        return binderkeiryou_time;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ秤量時間
     * @param binderkeiryou_time the binderkeiryou_time to set
     */
    public void setBinderkeiryou_time(FXHDD01 binderkeiryou_time) {
        this.binderkeiryou_time = binderkeiryou_time;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ品名
     * @return the binderhinmei
     */
    public FXHDD01 getBinderhinmei() {
        return binderhinmei;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ品名
     * @param binderhinmei the binderhinmei to set
     */
    public void setBinderhinmei(FXHDD01 binderhinmei) {
        this.binderhinmei = binderhinmei;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰLotNo
     * @return the binderlotno
     */
    public FXHDD01 getBinderlotno() {
        return binderlotno;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰLotNo
     * @param binderlotno the binderlotno to set
     */
    public void setBinderlotno(FXHDD01 binderlotno) {
        this.binderlotno = binderlotno;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ固形分
     * @return the binderkokeibun
     */
    public FXHDD01 getBinderkokeibun() {
        return binderkokeibun;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ固形分
     * @param binderkokeibun the binderkokeibun to set
     */
    public void setBinderkokeibun(FXHDD01 binderkokeibun) {
        this.binderkokeibun = binderkokeibun;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ有効期限
     * @return the binderyuukoukigen
     */
    public FXHDD01 getBinderyuukoukigen() {
        return binderyuukoukigen;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ有効期限
     * @param binderyuukoukigen the binderyuukoukigen to set
     */
    public void setBinderyuukoukigen(FXHDD01 binderyuukoukigen) {
        this.binderyuukoukigen = binderyuukoukigen;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ添加量規格
     * @return the bindertenkaryoukikaku
     */
    public FXHDD01 getBindertenkaryoukikaku() {
        return bindertenkaryoukikaku;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ添加量規格
     * @param bindertenkaryoukikaku the bindertenkaryoukikaku to set
     */
    public void setBindertenkaryoukikaku(FXHDD01 bindertenkaryoukikaku) {
        this.bindertenkaryoukikaku = bindertenkaryoukikaku;
    }

    /**
     * ﾎｯﾊﾟｰ使用
     * @return the hoppersiyou
     */
    public FXHDD01 getHoppersiyou() {
        return hoppersiyou;
    }

    /**
     * ﾎｯﾊﾟｰ使用
     * @param hoppersiyou the hoppersiyou to set
     */
    public void setHoppersiyou(FXHDD01 hoppersiyou) {
        this.hoppersiyou = hoppersiyou;
    }

    /**
     * 投入開始日
     * @return the tounyuukaisi_day
     */
    public FXHDD01 getTounyuukaisi_day() {
        return tounyuukaisi_day;
    }

    /**
     * 投入開始日
     * @param tounyuukaisi_day the tounyuukaisi_day to set
     */
    public void setTounyuukaisi_day(FXHDD01 tounyuukaisi_day) {
        this.tounyuukaisi_day = tounyuukaisi_day;
    }

    /**
     * 投入開始時間
     * @return the tounyuukaisi_time
     */
    public FXHDD01 getTounyuukaisi_time() {
        return tounyuukaisi_time;
    }

    /**
     * 投入開始時間
     * @param tounyuukaisi_time the tounyuukaisi_time to set
     */
    public void setTounyuukaisi_time(FXHDD01 tounyuukaisi_time) {
        this.tounyuukaisi_time = tounyuukaisi_time;
    }

    /**
     * 投入時回転数
     * @return the tounyuujikaitensuu
     */
    public FXHDD01 getTounyuujikaitensuu() {
        return tounyuujikaitensuu;
    }

    /**
     * 投入時回転数
     * @param tounyuujikaitensuu the tounyuujikaitensuu to set
     */
    public void setTounyuujikaitensuu(FXHDD01 tounyuujikaitensuu) {
        this.tounyuujikaitensuu = tounyuujikaitensuu;
    }

    /**
     * 秤量①
     * @return the hyouryou1
     */
    public FXHDD01 getHyouryou1() {
        return hyouryou1;
    }

    /**
     * 秤量①
     * @param hyouryou1 the hyouryou1 to set
     */
    public void setHyouryou1(FXHDD01 hyouryou1) {
        this.hyouryou1 = hyouryou1;
    }

    /**
     * 投入確認①
     * @return the tounyuukakunin1
     */
    public FXHDD01 getTounyuukakunin1() {
        return tounyuukakunin1;
    }

    /**
     * 投入確認①
     * @param tounyuukakunin1 the tounyuukakunin1 to set
     */
    public void setTounyuukakunin1(FXHDD01 tounyuukakunin1) {
        this.tounyuukakunin1 = tounyuukakunin1;
    }

    /**
     * 秤量②
     * @return the hyouryou2
     */
    public FXHDD01 getHyouryou2() {
        return hyouryou2;
    }

    /**
     * 秤量②
     * @param hyouryou2 the hyouryou2 to set
     */
    public void setHyouryou2(FXHDD01 hyouryou2) {
        this.hyouryou2 = hyouryou2;
    }

    /**
     * 投入確認②
     * @return the tounyuukakunin2
     */
    public FXHDD01 getTounyuukakunin2() {
        return tounyuukakunin2;
    }

    /**
     * 投入確認②
     * @param tounyuukakunin2 the tounyuukakunin2 to set
     */
    public void setTounyuukakunin2(FXHDD01 tounyuukakunin2) {
        this.tounyuukakunin2 = tounyuukakunin2;
    }

    /**
     * 秤量③
     * @return the hyouryou3
     */
    public FXHDD01 getHyouryou3() {
        return hyouryou3;
    }

    /**
     * 秤量③
     * @param hyouryou3 the hyouryou3 to set
     */
    public void setHyouryou3(FXHDD01 hyouryou3) {
        this.hyouryou3 = hyouryou3;
    }

    /**
     * 投入確認③
     * @return the tounyuukakunin3
     */
    public FXHDD01 getTounyuukakunin3() {
        return tounyuukakunin3;
    }

    /**
     * 投入確認③
     * @param tounyuukakunin3 the tounyuukakunin3 to set
     */
    public void setTounyuukakunin3(FXHDD01 tounyuukakunin3) {
        this.tounyuukakunin3 = tounyuukakunin3;
    }

    /**
     * 秤量④
     * @return the hyouryou4
     */
    public FXHDD01 getHyouryou4() {
        return hyouryou4;
    }

    /**
     * 秤量④
     * @param hyouryou4 the hyouryou4 to set
     */
    public void setHyouryou4(FXHDD01 hyouryou4) {
        this.hyouryou4 = hyouryou4;
    }

    /**
     * 投入確認④
     * @return the tounyuukakunin4
     */
    public FXHDD01 getTounyuukakunin4() {
        return tounyuukakunin4;
    }

    /**
     * 投入確認④
     * @param tounyuukakunin4 the tounyuukakunin4 to set
     */
    public void setTounyuukakunin4(FXHDD01 tounyuukakunin4) {
        this.tounyuukakunin4 = tounyuukakunin4;
    }

    /**
     * 秤量⑤
     * @return the hyouryou5
     */
    public FXHDD01 getHyouryou5() {
        return hyouryou5;
    }

    /**
     * 秤量⑤
     * @param hyouryou5 the hyouryou5 to set
     */
    public void setHyouryou5(FXHDD01 hyouryou5) {
        this.hyouryou5 = hyouryou5;
    }

    /**
     * 投入確認⑤
     * @return the tounyuukakunin5
     */
    public FXHDD01 getTounyuukakunin5() {
        return tounyuukakunin5;
    }

    /**
     * 投入確認⑤
     * @param tounyuukakunin5 the tounyuukakunin5 to set
     */
    public void setTounyuukakunin5(FXHDD01 tounyuukakunin5) {
        this.tounyuukakunin5 = tounyuukakunin5;
    }

    /**
     * 秤量⑥
     * @return the hyouryou6
     */
    public FXHDD01 getHyouryou6() {
        return hyouryou6;
    }

    /**
     * 秤量⑥
     * @param hyouryou6 the hyouryou6 to set
     */
    public void setHyouryou6(FXHDD01 hyouryou6) {
        this.hyouryou6 = hyouryou6;
    }

    /**
     * 投入確認⑥
     * @return the tounyuukakunin6
     */
    public FXHDD01 getTounyuukakunin6() {
        return tounyuukakunin6;
    }

    /**
     * 投入確認⑥
     * @param tounyuukakunin6 the tounyuukakunin6 to set
     */
    public void setTounyuukakunin6(FXHDD01 tounyuukakunin6) {
        this.tounyuukakunin6 = tounyuukakunin6;
    }

    /**
     * 投入終了日
     * @return the tounyuusyuuryou_day
     */
    public FXHDD01 getTounyuusyuuryou_day() {
        return tounyuusyuuryou_day;
    }

    /**
     * 投入終了日
     * @param tounyuusyuuryou_day the tounyuusyuuryou_day to set
     */
    public void setTounyuusyuuryou_day(FXHDD01 tounyuusyuuryou_day) {
        this.tounyuusyuuryou_day = tounyuusyuuryou_day;
    }

    /**
     * 投入終了時間
     * @return the tounyuusyuuryou_time
     */
    public FXHDD01 getTounyuusyuuryou_time() {
        return tounyuusyuuryou_time;
    }

    /**
     * 投入終了時間
     * @param tounyuusyuuryou_time the tounyuusyuuryou_time to set
     */
    public void setTounyuusyuuryou_time(FXHDD01 tounyuusyuuryou_time) {
        this.tounyuusyuuryou_time = tounyuusyuuryou_time;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ添加量合計
     * @return the bindertenkaryougoukei
     */
    public FXHDD01 getBindertenkaryougoukei() {
        return bindertenkaryougoukei;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ添加量合計
     * @param bindertenkaryougoukei the bindertenkaryougoukei to set
     */
    public void setBindertenkaryougoukei(FXHDD01 bindertenkaryougoukei) {
        this.bindertenkaryougoukei = bindertenkaryougoukei;
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