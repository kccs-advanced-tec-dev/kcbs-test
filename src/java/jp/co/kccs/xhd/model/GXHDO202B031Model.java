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
 * 変更日       2021/12/25<br>
 * 計画書No     MB2101-DK002<br>
 * 変更者       KCSS K.Jo<br>
 * 変更理由     新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入履歴検索画面のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/12/25
 */
public class GXHDO202B031Model implements Serializable {
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

    /** ﾊﾞｲﾝﾀﾞｰ混合設備 */
    private String binderkongousetub = "";

    /** ﾊﾞｲﾝﾀﾞｰ混合号機 */
    private String binderkongougoki = "";

    /** 混合ﾀﾝｸ種類 */
    private String kongoutanksyurui = "";

    /** 混合ﾀﾝｸNo */
    private Integer kongoutankno = null;

    /** ﾊﾞｲﾝﾀﾞｰ秤量日時 */
    private Timestamp binderkeiryounichiji = null;

    /** ﾊﾞｲﾝﾀﾞｰ品名 */
    private String binderhinmei = "";

    /** ﾊﾞｲﾝﾀﾞｰLotNo */
    private Integer binderlotno = null;

    /** ﾊﾞｲﾝﾀﾞｰ固形分 */
    private String binderkokeibun = "";

    /** ﾊﾞｲﾝﾀﾞｰ有効期限 */
    private String binderyuukoukigen = "";

    /** ﾊﾞｲﾝﾀﾞｰ添加量規格 */
    private String bindertenkaryoukikaku = "";

    /** ﾎｯﾊﾟｰ使用 */
    private String hoppersiyou = "";

    /** 投入開始日時 */
    private Timestamp tounyuukaisinichiji = null;

    /** 投入時回転数 */
    private Integer tounyuujikaitensuu = null;

    /** 秤量① */
    private Integer hyouryou1 = null;

    /** 投入確認① */
    private String tounyuukakunin1 = "";

    /** 秤量② */
    private Integer hyouryou2 = null;

    /** 投入確認② */
    private String tounyuukakunin2 = "";

    /** 秤量③ */
    private Integer hyouryou3 = null;

    /** 投入確認③ */
    private String tounyuukakunin3 = "";

    /** 秤量④ */
    private Integer hyouryou4 = null;

    /** 投入確認④ */
    private String tounyuukakunin4 = "";

    /** 秤量⑤ */
    private Integer hyouryou5 = null;

    /** 投入確認⑤ */
    private String tounyuukakunin5 = "";

    /** 秤量⑥ */
    private Integer hyouryou6 = null;

    /** 投入確認⑥ */
    private String tounyuukakunin6 = "";

    /** 投入終了日時 */
    private Timestamp tounyuusyuuryounichiji = null;

    /** ﾊﾞｲﾝﾀﾞｰ添加量合計 */
    private Integer bindertenkaryougoukei = null;

    /** 担当者 */
    private String tantousya = "";

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
     * ﾊﾞｲﾝﾀﾞｰ秤量日時
     * @return the binderkeiryounichiji
     */
    public Timestamp getBinderkeiryounichiji() {
        return binderkeiryounichiji;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ秤量日時
     * @param binderkeiryounichiji the binderkeiryounichiji to set
     */
    public void setBinderkeiryounichiji(Timestamp binderkeiryounichiji) {
        this.binderkeiryounichiji = binderkeiryounichiji;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ品名
     * @return the binderhinmei
     */
    public String getBinderhinmei() {
        return binderhinmei;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ品名
     * @param binderhinmei the binderhinmei to set
     */
    public void setBinderhinmei(String binderhinmei) {
        this.binderhinmei = binderhinmei;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰLotNo
     * @return the binderlotno
     */
    public Integer getBinderlotno() {
        return binderlotno;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰLotNo
     * @param binderlotno the binderlotno to set
     */
    public void setBinderlotno(Integer binderlotno) {
        this.binderlotno = binderlotno;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ固形分
     * @return the binderkokeibun
     */
    public String getBinderkokeibun() {
        return binderkokeibun;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ固形分
     * @param binderkokeibun the binderkokeibun to set
     */
    public void setBinderkokeibun(String binderkokeibun) {
        this.binderkokeibun = binderkokeibun;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ有効期限
     * @return the binderyuukoukigen
     */
    public String getBinderyuukoukigen() {
        return binderyuukoukigen;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ有効期限
     * @param binderyuukoukigen the binderyuukoukigen to set
     */
    public void setBinderyuukoukigen(String binderyuukoukigen) {
        this.binderyuukoukigen = binderyuukoukigen;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ添加量規格
     * @return the bindertenkaryoukikaku
     */
    public String getBindertenkaryoukikaku() {
        return bindertenkaryoukikaku;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ添加量規格
     * @param bindertenkaryoukikaku the bindertenkaryoukikaku to set
     */
    public void setBindertenkaryoukikaku(String bindertenkaryoukikaku) {
        this.bindertenkaryoukikaku = bindertenkaryoukikaku;
    }

    /**
     * ﾎｯﾊﾟｰ使用
     * @return the hoppersiyou
     */
    public String getHoppersiyou() {
        return hoppersiyou;
    }

    /**
     * ﾎｯﾊﾟｰ使用
     * @param hoppersiyou the hoppersiyou to set
     */
    public void setHoppersiyou(String hoppersiyou) {
        this.hoppersiyou = hoppersiyou;
    }

    /**
     * 投入開始日時
     * @return the tounyuukaisinichiji
     */
    public Timestamp getTounyuukaisinichiji() {
        return tounyuukaisinichiji;
    }

    /**
     * 投入開始日時
     * @param tounyuukaisinichiji the tounyuukaisinichiji to set
     */
    public void setTounyuukaisinichiji(Timestamp tounyuukaisinichiji) {
        this.tounyuukaisinichiji = tounyuukaisinichiji;
    }

    /**
     * 投入時回転数
     * @return the tounyuujikaitensuu
     */
    public Integer getTounyuujikaitensuu() {
        return tounyuujikaitensuu;
    }

    /**
     * 投入時回転数
     * @param tounyuujikaitensuu the tounyuujikaitensuu to set
     */
    public void setTounyuujikaitensuu(Integer tounyuujikaitensuu) {
        this.tounyuujikaitensuu = tounyuujikaitensuu;
    }

    /**
     * 秤量①
     * @return the hyouryou1
     */
    public Integer getHyouryou1() {
        return hyouryou1;
    }

    /**
     * 秤量①
     * @param hyouryou1 the hyouryou1 to set
     */
    public void setHyouryou1(Integer hyouryou1) {
        this.hyouryou1 = hyouryou1;
    }

    /**
     * 投入確認①
     * @return the tounyuukakunin1
     */
    public String getTounyuukakunin1() {
        return tounyuukakunin1;
    }

    /**
     * 投入確認①
     * @param tounyuukakunin1 the tounyuukakunin1 to set
     */
    public void setTounyuukakunin1(String tounyuukakunin1) {
        this.tounyuukakunin1 = tounyuukakunin1;
    }

    /**
     * 秤量②
     * @return the hyouryou2
     */
    public Integer getHyouryou2() {
        return hyouryou2;
    }

    /**
     * 秤量②
     * @param hyouryou2 the hyouryou2 to set
     */
    public void setHyouryou2(Integer hyouryou2) {
        this.hyouryou2 = hyouryou2;
    }

    /**
     * 投入確認②
     * @return the tounyuukakunin2
     */
    public String getTounyuukakunin2() {
        return tounyuukakunin2;
    }

    /**
     * 投入確認②
     * @param tounyuukakunin2 the tounyuukakunin2 to set
     */
    public void setTounyuukakunin2(String tounyuukakunin2) {
        this.tounyuukakunin2 = tounyuukakunin2;
    }

    /**
     * 秤量③
     * @return the hyouryou3
     */
    public Integer getHyouryou3() {
        return hyouryou3;
    }

    /**
     * 秤量③
     * @param hyouryou3 the hyouryou3 to set
     */
    public void setHyouryou3(Integer hyouryou3) {
        this.hyouryou3 = hyouryou3;
    }

    /**
     * 投入確認③
     * @return the tounyuukakunin3
     */
    public String getTounyuukakunin3() {
        return tounyuukakunin3;
    }

    /**
     * 投入確認③
     * @param tounyuukakunin3 the tounyuukakunin3 to set
     */
    public void setTounyuukakunin3(String tounyuukakunin3) {
        this.tounyuukakunin3 = tounyuukakunin3;
    }

    /**
     * 秤量④
     * @return the hyouryou4
     */
    public Integer getHyouryou4() {
        return hyouryou4;
    }

    /**
     * 秤量④
     * @param hyouryou4 the hyouryou4 to set
     */
    public void setHyouryou4(Integer hyouryou4) {
        this.hyouryou4 = hyouryou4;
    }

    /**
     * 投入確認④
     * @return the tounyuukakunin4
     */
    public String getTounyuukakunin4() {
        return tounyuukakunin4;
    }

    /**
     * 投入確認④
     * @param tounyuukakunin4 the tounyuukakunin4 to set
     */
    public void setTounyuukakunin4(String tounyuukakunin4) {
        this.tounyuukakunin4 = tounyuukakunin4;
    }

    /**
     * 秤量⑤
     * @return the hyouryou5
     */
    public Integer getHyouryou5() {
        return hyouryou5;
    }

    /**
     * 秤量⑤
     * @param hyouryou5 the hyouryou5 to set
     */
    public void setHyouryou5(Integer hyouryou5) {
        this.hyouryou5 = hyouryou5;
    }

    /**
     * 投入確認⑤
     * @return the tounyuukakunin5
     */
    public String getTounyuukakunin5() {
        return tounyuukakunin5;
    }

    /**
     * 投入確認⑤
     * @param tounyuukakunin5 the tounyuukakunin5 to set
     */
    public void setTounyuukakunin5(String tounyuukakunin5) {
        this.tounyuukakunin5 = tounyuukakunin5;
    }

    /**
     * 秤量⑥
     * @return the hyouryou6
     */
    public Integer getHyouryou6() {
        return hyouryou6;
    }

    /**
     * 秤量⑥
     * @param hyouryou6 the hyouryou6 to set
     */
    public void setHyouryou6(Integer hyouryou6) {
        this.hyouryou6 = hyouryou6;
    }

    /**
     * 投入確認⑥
     * @return the tounyuukakunin6
     */
    public String getTounyuukakunin6() {
        return tounyuukakunin6;
    }

    /**
     * 投入確認⑥
     * @param tounyuukakunin6 the tounyuukakunin6 to set
     */
    public void setTounyuukakunin6(String tounyuukakunin6) {
        this.tounyuukakunin6 = tounyuukakunin6;
    }

    /**
     * 投入終了日時
     * @return the tounyuusyuuryounichiji
     */
    public Timestamp getTounyuusyuuryounichiji() {
        return tounyuusyuuryounichiji;
    }

    /**
     * 投入終了日時
     * @param tounyuusyuuryounichiji the tounyuusyuuryounichiji to set
     */
    public void setTounyuusyuuryounichiji(Timestamp tounyuusyuuryounichiji) {
        this.tounyuusyuuryounichiji = tounyuusyuuryounichiji;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ添加量合計
     * @return the bindertenkaryougoukei
     */
    public Integer getBindertenkaryougoukei() {
        return bindertenkaryougoukei;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ添加量合計
     * @param bindertenkaryougoukei the bindertenkaryougoukei to set
     */
    public void setBindertenkaryougoukei(Integer bindertenkaryougoukei) {
        this.bindertenkaryougoukei = bindertenkaryougoukei;
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