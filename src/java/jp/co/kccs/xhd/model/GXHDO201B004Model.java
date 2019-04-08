/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/04/08<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCCS D.Yanagida<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 積層・SPS履歴検索画面のモデルクラスです。
 *
 * @author KCCS D.Yanagida
 * @since 2019/04/08
 */
public class GXHDO201B004Model {
    /** ﾛｯﾄNo. */
    private String lotno = "";
    /** KCPNO */
    private String kcpno = "";
    /** 端子ﾃｰﾌﾟ種類 */
    private String tntapesyurui = "";
    /** 端子ﾃｰﾌﾟNo */
    private String tntapeno = "";
    /** 端子ﾃｰﾌﾟ原料 */
    private String tntapegenryou = "";
    /** 積層号機 */
    private String gouki = "";
    /** 開始日時 */
    private Timestamp startdatetime = null;
    /** 終了日時 */
    private Timestamp enddatetime = null;
    /** 積層ｽﾞﾚ */
    private Long sekisouzure = null;
    /** 担当者ｺｰﾄﾞ */
    private String tantousya = "";
    /** 確認者ｺｰﾄﾞ */
    private String kakuninsya = "";
    /** 積層ｽﾞﾚ2 */
    private Long sekisouzure2 = null;
    /** 備考1 */
    private String bikou1 = "";
    /** 備考2 */
    private String bikou2 = "";
    /** 備考3 */
    private String bikou3 = "";
    /** 備考4 */
    private String bikou4 = "";
    /** 備考5 */
    private String bikou5 = "";
    /** 号機ｺｰﾄﾞ */
    private String goukiCode = "";
    /** ﾃｰﾌﾟLot */
    private String tpLot = "";
    /** 剥離速度 */
    private Long hakuriSpeed = null;
    /** 金型温度 */
    private BigDecimal kanaOndo = null;
    /** 電極圧力 */
    private Long dAturyoku = null;
    /** 電極加圧時間 */
    private BigDecimal dKaatuJikan = null;
    /** 中間ﾌﾟﾚｽ圧力 */
    private Long cPressAturyoku = null;
    /** 中間ﾌﾟﾚｽ加圧時間 */
    private BigDecimal cPressKaatuJikan = null;
    /** 中間ﾌﾟﾚｽ間隔総数 */
    private Long cPressKankakuSosuu = null;
    /** 最終加圧力 */
    private Long lastKaaturyoku = null;
    /** 最終加圧時間 */
    private Long lastKaatuJikan = null;
    /** 4分割担当者ｺｰﾄﾞ */
    private String fourSplitTantou = "";
    /** 積層ﾀｵﾚ量1 */
    private Long sTaoreryo1 = null;
    /** 積層ﾀｵﾚ量2 */
    private Long sTaoreryo2 = null;
    /** 積層ﾀｵﾚ量3 */
    private Long sTaoreryo3 = null;
    /** 積層ﾀｵﾚ量4 */
    private Long sTaoreryo4 = null;
    /** 積層後T寸法AVE */
    private Long sTsunAve = null;
    /** 積層後T寸法MAX */
    private Long sTsunMax = null;
    /** 積層後T寸法MIN */
    private Long sTsunMin = null;
    /** 積層後T寸法σ */
    private BigDecimal sTsunSiguma = null;
    /** 隔離NG回数 */
    private Long hNGKaisuu = null;
    /** 最終外観担当者ｺｰﾄﾞ */
    private String gaikanTantou = "";
    /** 中間ﾌﾟﾚｽ回数 */
    private Long cPressKaisuu = null;
    /** 剥離NG回数AVE */
    private BigDecimal hNGKaisuuAve = null;
    /** 画処NG回数 */
    private Long gNGKaisuu = null;
    /** 画処NG回数AVE */
    private BigDecimal gNGKaisuuAve = null;
    /** ｽﾘｯﾌﾟﾛｯﾄNo */
    private String tapelotno = "";
    /** ﾛｰﾙNo1 */
    private String taperollno1 = "";
    /** ﾛｰﾙNo2 */
    private String taperollno2 = "";
    /** ﾛｰﾙNo3 */
    private String taperollno3 = "";
    /** 原料記号 */
    private String genryoukigou = "";
    /** PETﾌｨﾙﾑ種類 */
    private String petfilmsyurui = "";
    /** 固着ｼｰﾄ貼付り機 */
    private String kotyakugouki = "";
    /** 固着ｼｰﾄ */
    private String kotyakusheet = "";
    /** 下端子号機 */
    private String shitaTanshigouki = "";
    /** 上端子号機 */
    private String uwaTanshigouki = "";
    /** 下端子ﾌﾞｸ抜き */
    private String shitaTanshiBukunuki = "";
    /** 下端子 */
    private String shitaTanshi = "";
    /** 剥離吸引圧 */
    private Long hakuriKyuin = null;
    /** 剥離ｸﾘｱﾗﾝｽ */
    private BigDecimal hakuriClearrance = null;
    /** 剥離ｶｯﾄ速度 */
    private Long hakuriCutSpeed = null;
    /** 下ﾊﾟﾝﾁ温度 */
    private BigDecimal shitaPanchiOndo = null;
    /** 上ﾊﾟﾝﾁ温度 */
    private BigDecimal uwaPanchiOndo = null;
    /** 加圧時間 */
    private BigDecimal kaatuJikan = null;
    /** 加圧圧力 */
    private Long kaatuAturyoku = null;
    /** 上端子 */
    private String uwaTanshi = "";
    /** 外観確認1 */
    private String gaikanKakunin1 = "";
    /** 外観確認2 */
    private String gaikanKakunin2 = "";
    /** 外観確認3 */
    private String gaikanKakunin3 = "";
    /** 外観確認4 */
    private String gaikanKakunin4 = "";
    /** 処理ｾｯﾄ数 */
    private Long syoriSetsuu = null;
    /** 良品ｾｯﾄ数 */
    private BigDecimal ryouhinSetsuu = null;
    /** 開始担当者 */
    private String startTantosyacode = "";
    /** 終了担当者 */
    private String endTantousyacode = "";
    /** 端子ﾃｰﾌﾟ種類確認 */
    private String tanshiTapeSyurui = "";
    /** ｾｯﾄ数1 */
    private Long setsuu1 = null;
    /** ｾｯﾄ数2 */
    private Long setsuu2 = null;
    /** ｾｯﾄ数3 */
    private Long setsuu3 = null;
    /** ｾｯﾄ数4 */
    private Long setsuu4 = null;
    /** ｾｯﾄ数5 */
    private Long setsuu5 = null;
    /** ｾｯﾄ数6 */
    private Long setsuu6 = null;
    /** ｾｯﾄ数7 */
    private Long setsuu7 = null;
    /** ｾｯﾄ数8 */
    private Long setsuu8 = null;
    /** ｾｯﾄ数9 */
    private Long setsuu9 = null;
    /** ｾｯﾄ数10 */
    private Long setsuu10 = null;
    /** ｾｯﾄ数11 */
    private Long setsuu11 = null;
    /** ｾｯﾄ数12 */
    private Long setsuu12 = null;
    /** ｾｯﾄ数13 */
    private Long setsuu13 = null;
    /** ｾｯﾄ数14 */
    private Long setsuu14 = null;
    /** ｾｯﾄ数15 */
    private Long setsuu15 = null;
    /** ｾｯﾄ数16 */
    private Long setsuu16 = null;
    /** ｾｯﾄ数17 */
    private Long setsuu17 = null;
    /** ｾｯﾄ数18 */
    private Long setsuu18 = null;
    /** ｾｯﾄ数19 */
    private Long setsuu19 = null;
    /** ｾｯﾄ数20 */
    private Long setsuu20 = null;
    /** ｾｯﾄ数21 */
    private Long setsuu21 = null;
    /** ｾｯﾄ数22 */
    private Long setsuu22 = null;
    /** ｾｯﾄ数23 */
    private Long setsuu23 = null;
    /** ｾｯﾄ数24 */
    private Long setsuu24 = null;
    /** ｾｯﾄ数25 */
    private Long setsuu25 = null;
    /** ｾｯﾄ数26 */
    private Long setsuu26 = null;
    /** ｾｯﾄ数27 */
    private Long setsuu27 = null;
    /** ｾｯﾄ数28 */
    private Long setsuu28 = null;
    /** ｾｯﾄ数29 */
    private Long setsuu29 = null;
    /** ｾｯﾄ数30 */
    private Long setsuu30 = null;
    /** ｾｯﾄ数31 */
    private Long setsuu31 = null;
    /** ｾｯﾄ数32 */
    private Long setsuu32 = null;
    /** ｾｯﾄ数33 */
    private Long setsuu33 = null;
    /** ｾｯﾄ数34 */
    private Long setsuu34 = null;
    /** ｾｯﾄ数35 */
    private Long setsuu35 = null;
    /** ｾｯﾄ数36 */
    private Long setsuu36 = null;
    /** ｾｯﾄ数37 */
    private Long setsuu37 = null;
    /** ｾｯﾄ数38 */
    private Long setsuu38 = null;
    /** ｾｯﾄ数39 */
    private Long setsuu39 = null;
    /** ｾｯﾄ数40 */
    private Long setsuu40 = null;
    /** 備考1 */
    private String hbikou1 = "";
    /** 備考2 */
    private String hbikou2 = "";
    /** 備考3 */
    private String hbikou3 = "";
    /** 備考4 */
    private String hbikou4 = "";
    /** 備考5 */
    private String hbikou5 = "";
    /** 備考6 */
    private String hbikou6 = "";
    /** 備考7 */
    private String hbikou7 = "";
    /** 備考8 */
    private String hbikou8 = "";
    /** 備考9 */
    private String hbikou9 = "";
    /** 備考10 */
    private String hbikou10 = "";
    /** 備考11 */
    private String hbikou11 = "";
    /** 備考12 */
    private String hbikou12 = "";
    /** 備考13 */
    private String hbikou13 = "";
    /** 備考14 */
    private String hbikou14 = "";
    /** 備考15 */
    private String hbikou15 = "";
    /** 備考16 */
    private String hbikou16 = "";
    /** 備考17 */
    private String hbikou17 = "";
    /** 備考18 */
    private String hbikou18 = "";
    /** 備考19 */
    private String hbikou19 = "";
    /** 備考20 */
    private String hbikou20 = "";
    /** 備考21 */
    private String hbikou21 = "";
    /** 備考22 */
    private String hbikou22 = "";
    /** 備考23 */
    private String hbikou23 = "";
    /** 備考24 */
    private String hbikou24 = "";
    /** 備考25 */
    private String hbikou25 = "";
    /** 備考26 */
    private String hbikou26 = "";
    /** 備考27 */
    private String hbikou27 = "";
    /** 備考28 */
    private String hbikou28 = "";
    /** 備考29 */
    private String hbikou29 = "";
    /** 備考30 */
    private String hbikou30 = "";
    /** 備考31 */
    private String hbikou31 = "";
    /** 備考32 */
    private String hbikou32 = "";
    /** 備考33 */
    private String hbikou33 = "";
    /** 備考34 */
    private String hbikou34 = "";
    /** 備考35 */
    private String hbikou35 = "";
    /** 備考36 */
    private String hbikou36 = "";
    /** 備考37 */
    private String hbikou37 = "";
    /** 備考38 */
    private String hbikou38 = "";
    /** 備考39 */
    private String hbikou39 = "";
    /** 備考40 */
    private String hbikou40 = "";
    
    /**
     * ﾛｯﾄNo.
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * ﾛｯﾄNo.
     * @param lotno the lotno to set
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * KCPNO
     * @return the kcpno
     */
    public String getKcpno() {
        return kcpno;
    }

    /**
     * KCPNO
     * @param kcpno the kcpno to set
     */
    public void setKcpno(String kcpno) {
        this.kcpno = kcpno;
    }

    /**
     * 端子ﾃｰﾌﾟ種類
     * @return the tntapesyurui
     */
    public String getTntapesyurui() {
        return tntapesyurui;
    }

    /**
     * 端子ﾃｰﾌﾟ種類
     * @param tntapesyurui the tntapesyurui to set
     */
    public void setTntapesyurui(String tntapesyurui) {
        this.tntapesyurui = tntapesyurui;
    }

    /**
     * 端子ﾃｰﾌﾟNo
     * @return the tntapeno
     */
    public String getTntapeno() {
        return tntapeno;
    }

    /**
     * 端子ﾃｰﾌﾟNo
     * @param tntapeno the tntapeno to set
     */
    public void setTntapeno(String tntapeno) {
        this.tntapeno = tntapeno;
    }

    /**
     * 端子ﾃｰﾌﾟ原料
     * @return the tntapegenryou
     */
    public String getTntapegenryou() {
        return tntapegenryou;
    }

    /**
     * 端子ﾃｰﾌﾟ原料
     * @param tntapegenryou the tntapegenryou to set
     */
    public void setTntapegenryou(String tntapegenryou) {
        this.tntapegenryou = tntapegenryou;
    }

    /**
     * 積層号機
     * @return the gouki
     */
    public String getGouki() {
        return gouki;
    }

    /**
     * 積層号機
     * @param gouki the gouki to set
     */
    public void setGouki(String gouki) {
        this.gouki = gouki;
    }

    /**
     * 開始日時
     * @return the startdatetime
     */
    public Timestamp getStartdatetime() {
        return startdatetime;
    }

    /**
     * 開始日時
     * @param startdatetime the startdatetime to set
     */
    public void setStartdatetime(Timestamp startdatetime) {
        this.startdatetime = startdatetime;
    }

    /**
     * 終了日時
     * @return the enddatetime
     */
    public Timestamp getEnddatetime() {
        return enddatetime;
    }

    /**
     * 終了日時
     * @param enddatetime the enddatetime to set
     */
    public void setEnddatetime(Timestamp enddatetime) {
        this.enddatetime = enddatetime;
    }

    /**
     * 積層ｽﾞﾚ
     * @return the sekisouzure
     */
    public Long getSekisouzure() {
        return sekisouzure;
    }

    /**
     * 積層ｽﾞﾚ
     * @param sekisouzure the sekisouzure to set
     */
    public void setSekisouzure(Long sekisouzure) {
        this.sekisouzure = sekisouzure;
    }

    /**
     * 担当者ｺｰﾄﾞ
     * @return the tantousya
     */
    public String getTantousya() {
        return tantousya;
    }

    /**
     * 担当者ｺｰﾄﾞ
     * @param tantousya the tantousya to set
     */
    public void setTantousya(String tantousya) {
        this.tantousya = tantousya;
    }

    /**
     * 確認者ｺｰﾄﾞ
     * @return the kakuninsya
     */
    public String getKakuninsya() {
        return kakuninsya;
    }

    /**
     * 確認者ｺｰﾄﾞ
     * @param kakuninsya the kakuninsya to set
     */
    public void setKakuninsya(String kakuninsya) {
        this.kakuninsya = kakuninsya;
    }

    /**
     * 積層ｽﾞﾚ2
     * @return the sekisouzure2
     */
    public Long getSekisouzure2() {
        return sekisouzure2;
    }

    /**
     * 積層ｽﾞﾚ2
     * @param sekisouzure2 the sekisouzure2 to set
     */
    public void setSekisouzure2(Long sekisouzure2) {
        this.sekisouzure2 = sekisouzure2;
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
     * 備考3
     * @return the bikou3
     */
    public String getBikou3() {
        return bikou3;
    }

    /**
     * 備考3
     * @param bikou3 the bikou3 to set
     */
    public void setBikou3(String bikou3) {
        this.bikou3 = bikou3;
    }

    /**
     * 備考4
     * @return the bikou4
     */
    public String getBikou4() {
        return bikou4;
    }

    /**
     * 備考4
     * @param bikou4 the bikou4 to set
     */
    public void setBikou4(String bikou4) {
        this.bikou4 = bikou4;
    }

    /**
     * 備考5
     * @return the bikou5
     */
    public String getBikou5() {
        return bikou5;
    }

    /**
     * 備考5
     * @param bikou5 the bikou5 to set
     */
    public void setBikou5(String bikou5) {
        this.bikou5 = bikou5;
    }

    /**
     * 号機ｺｰﾄﾞ
     * @return the goukiCode
     */
    public String getGoukiCode() {
        return goukiCode;
    }

    /**
     * 号機ｺｰﾄﾞ
     * @param goukiCode the goukiCode to set
     */
    public void setGoukiCode(String goukiCode) {
        this.goukiCode = goukiCode;
    }

    /**
     * ﾃｰﾌﾟLot
     * @return the tpLot
     */
    public String getTpLot() {
        return tpLot;
    }

    /**
     * ﾃｰﾌﾟLot
     * @param tpLot the tpLot to set
     */
    public void setTpLot(String tpLot) {
        this.tpLot = tpLot;
    }

    /**
     * 剥離速度
     * @return the hakuriSpeed
     */
    public Long getHakuriSpeed() {
        return hakuriSpeed;
    }

    /**
     * 剥離速度
     * @param hakuriSpeed the hakuriSpeed to set
     */
    public void setHakuriSpeed(Long hakuriSpeed) {
        this.hakuriSpeed = hakuriSpeed;
    }

    /**
     * 金型温度
     * @return the kanaOndo
     */
    public BigDecimal getKanaOndo() {
        return kanaOndo;
    }

    /**
     * 金型温度
     * @param kanaOndo the kanaOndo to set
     */
    public void setKanaOndo(BigDecimal kanaOndo) {
        this.kanaOndo = kanaOndo;
    }

    /**
     * 電極圧力
     * @return the dAturyoku
     */
    public Long getdAturyoku() {
        return dAturyoku;
    }

    /**
     * 電極圧力
     * @param dAturyoku the dAturyoku to set
     */
    public void setdAturyoku(Long dAturyoku) {
        this.dAturyoku = dAturyoku;
    }

    /**
     * 電極加圧時間
     * @return the dKaatuJikan
     */
    public BigDecimal getdKaatuJikan() {
        return dKaatuJikan;
    }

    /**
     * 電極加圧時間
     * @param dKaatuJikan the dKaatuJikan to set
     */
    public void setdKaatuJikan(BigDecimal dKaatuJikan) {
        this.dKaatuJikan = dKaatuJikan;
    }

    /**
     * 中間ﾌﾟﾚｽ圧力
     * @return the cPressAturyoku
     */
    public Long getcPressAturyoku() {
        return cPressAturyoku;
    }

    /**
     * 中間ﾌﾟﾚｽ圧力
     * @param cPressAturyoku the cPressAturyoku to set
     */
    public void setcPressAturyoku(Long cPressAturyoku) {
        this.cPressAturyoku = cPressAturyoku;
    }

    /**
     * 中間ﾌﾟﾚｽ加圧時間
     * @return the cPressKaatuJikan
     */
    public BigDecimal getcPressKaatuJikan() {
        return cPressKaatuJikan;
    }

    /**
     * 中間ﾌﾟﾚｽ加圧時間
     * @param cPressKaatuJikan the cPressKaatuJikan to set
     */
    public void setcPressKaatuJikan(BigDecimal cPressKaatuJikan) {
        this.cPressKaatuJikan = cPressKaatuJikan;
    }

    /**
     * 中間ﾌﾟﾚｽ間隔総数
     * @return the cPressKankakuSosuu
     */
    public Long getcPressKankakuSosuu() {
        return cPressKankakuSosuu;
    }

    /**
     * 中間ﾌﾟﾚｽ間隔総数
     * @param cPressKankakuSosuu the cPressKankakuSosuu to set
     */
    public void setcPressKankakuSosuu(Long cPressKankakuSosuu) {
        this.cPressKankakuSosuu = cPressKankakuSosuu;
    }

    /**
     * 最終加圧力
     * @return the lastKaaturyoku
     */
    public Long getLastKaaturyoku() {
        return lastKaaturyoku;
    }

    /**
     * 最終加圧力
     * @param lastKaaturyoku the lastKaaturyoku to set
     */
    public void setLastKaaturyoku(Long lastKaaturyoku) {
        this.lastKaaturyoku = lastKaaturyoku;
    }

    /**
     * 最終加圧時間
     * @return the lastKaatuJikan
     */
    public Long getLastKaatuJikan() {
        return lastKaatuJikan;
    }

    /**
     * 最終加圧時間
     * @param lastKaatuJikan the lastKaatuJikan to set
     */
    public void setLastKaatuJikan(Long lastKaatuJikan) {
        this.lastKaatuJikan = lastKaatuJikan;
    }

    /**
     * 4分割担当者ｺｰﾄﾞ
     * @return the fourSplitTantou
     */
    public String getFourSplitTantou() {
        return fourSplitTantou;
    }

    /**
     * 4分割担当者ｺｰﾄﾞ
     * @param fourSplitTantou the fourSplitTantou to set
     */
    public void setFourSplitTantou(String fourSplitTantou) {
        this.fourSplitTantou = fourSplitTantou;
    }

    /**
     * 積層ﾀｵﾚ量1
     * @return the sTaoreryo1
     */
    public Long getsTaoreryo1() {
        return sTaoreryo1;
    }

    /**
     * 積層ﾀｵﾚ量1
     * @param sTaoreryo1 the sTaoreryo1 to set
     */
    public void setsTaoreryo1(Long sTaoreryo1) {
        this.sTaoreryo1 = sTaoreryo1;
    }

    /**
     * 積層ﾀｵﾚ量2
     * @return the sTaoreryo2
     */
    public Long getsTaoreryo2() {
        return sTaoreryo2;
    }

    /**
     * 積層ﾀｵﾚ量2
     * @param sTaoreryo2 the sTaoreryo2 to set
     */
    public void setsTaoreryo2(Long sTaoreryo2) {
        this.sTaoreryo2 = sTaoreryo2;
    }

    /**
     * 積層ﾀｵﾚ量3
     * @return the sTaoreryo3
     */
    public Long getsTaoreryo3() {
        return sTaoreryo3;
    }

    /**
     * 積層ﾀｵﾚ量3
     * @param sTaoreryo3 the sTaoreryo3 to set
     */
    public void setsTaoreryo3(Long sTaoreryo3) {
        this.sTaoreryo3 = sTaoreryo3;
    }

    /**
     * 積層ﾀｵﾚ量4
     * @return the sTaoreryo4
     */
    public Long getsTaoreryo4() {
        return sTaoreryo4;
    }

    /**
     * 積層ﾀｵﾚ量4
     * @param sTaoreryo4 the sTaoreryo4 to set
     */
    public void setsTaoreryo4(Long sTaoreryo4) {
        this.sTaoreryo4 = sTaoreryo4;
    }

    /**
     * 積層後T寸法AVE
     * @return the sTsunAve
     */
    public Long getsTsunAve() {
        return sTsunAve;
    }

    /**
     * 積層後T寸法AVE
     * @param sTsunAve the sTsunAve to set
     */
    public void setsTsunAve(Long sTsunAve) {
        this.sTsunAve = sTsunAve;
    }

    /**
     * 積層後T寸法MAX
     * @return the sTsunMax
     */
    public Long getsTsunMax() {
        return sTsunMax;
    }

    /**
     * 積層後T寸法MAX
     * @param sTsunMax the sTsunMax to set
     */
    public void setsTsunMax(Long sTsunMax) {
        this.sTsunMax = sTsunMax;
    }

    /**
     * 積層後T寸法MIN
     * @return the sTsunMin
     */
    public Long getsTsunMin() {
        return sTsunMin;
    }

    /**
     * 積層後T寸法MIN
     * @param sTsunMin the sTsunMin to set
     */
    public void setsTsunMin(Long sTsunMin) {
        this.sTsunMin = sTsunMin;
    }

    /**
     * 積層後T寸法σ
     * @return the sTsunSiguma
     */
    public BigDecimal getsTsunSiguma() {
        return sTsunSiguma;
    }

    /**
     * 積層後T寸法σ
     * @param sTsunSiguma the sTsunSiguma to set
     */
    public void setsTsunSiguma(BigDecimal sTsunSiguma) {
        this.sTsunSiguma = sTsunSiguma;
    }

    /**
     * 隔離NG回数
     * @return the hNGKaisuu
     */
    public Long gethNGKaisuu() {
        return hNGKaisuu;
    }

    /**
     * 隔離NG回数
     * @param hNGKaisuu the hNGKaisuu to set
     */
    public void sethNGKaisuu(Long hNGKaisuu) {
        this.hNGKaisuu = hNGKaisuu;
    }

    /**
     * 最終外観担当者ｺｰﾄﾞ
     * @return the gaikanTantou
     */
    public String getGaikanTantou() {
        return gaikanTantou;
    }

    /**
     * 最終外観担当者ｺｰﾄﾞ
     * @param gaikanTantou the gaikanTantou to set
     */
    public void setGaikanTantou(String gaikanTantou) {
        this.gaikanTantou = gaikanTantou;
    }

    /**
     * 中間ﾌﾟﾚｽ回数
     * @return the cPressKaisuu
     */
    public Long getcPressKaisuu() {
        return cPressKaisuu;
    }

    /**
     * 中間ﾌﾟﾚｽ回数
     * @param cPressKaisuu the cPressKaisuu to set
     */
    public void setcPressKaisuu(Long cPressKaisuu) {
        this.cPressKaisuu = cPressKaisuu;
    }

    /**
     * 剥離NG回数AVE
     * @return the hNGKaisuuAve
     */
    public BigDecimal gethNGKaisuuAve() {
        return hNGKaisuuAve;
    }

    /**
     * 剥離NG回数AVE
     * @param hNGKaisuuAve the hNGKaisuuAve to set
     */
    public void sethNGKaisuuAve(BigDecimal hNGKaisuuAve) {
        this.hNGKaisuuAve = hNGKaisuuAve;
    }

    /**
     * 画処NG回数
     * @return the gNGKaisuu
     */
    public Long getgNGKaisuu() {
        return gNGKaisuu;
    }

    /**
     * 画処NG回数
     * @param gNGKaisuu the gNGKaisuu to set
     */
    public void setgNGKaisuu(Long gNGKaisuu) {
        this.gNGKaisuu = gNGKaisuu;
    }

    /**
     * 画処NG回数AVE
     * @return the gNGKaisuuAve
     */
    public BigDecimal getgNGKaisuuAve() {
        return gNGKaisuuAve;
    }

    /**
     * 画処NG回数AVE
     * @param gNGKaisuuAve the gNGKaisuuAve to set
     */
    public void setgNGKaisuuAve(BigDecimal gNGKaisuuAve) {
        this.gNGKaisuuAve = gNGKaisuuAve;
    }

    /**
     * ｽﾘｯﾌﾟﾛｯﾄNo
     * @return the tapelotno
     */
    public String getTapelotno() {
        return tapelotno;
    }

    /**
     * ｽﾘｯﾌﾟﾛｯﾄNo
     * @param tapelotno the tapelotno to set
     */
    public void setTapelotno(String tapelotno) {
        this.tapelotno = tapelotno;
    }

    /**
     * ﾛｰﾙNo1
     * @return the taperollno1
     */
    public String getTaperollno1() {
        return taperollno1;
    }

    /**
     * ﾛｰﾙNo1
     * @param taperollno1 the taperollno1 to set
     */
    public void setTaperollno1(String taperollno1) {
        this.taperollno1 = taperollno1;
    }

    /**
     * ﾛｰﾙNo2
     * @return the taperollno2
     */
    public String getTaperollno2() {
        return taperollno2;
    }

    /**
     * ﾛｰﾙNo2
     * @param taperollno2 the taperollno2 to set
     */
    public void setTaperollno2(String taperollno2) {
        this.taperollno2 = taperollno2;
    }

    /**
     * ﾛｰﾙNo3
     * @return the taperollno3
     */
    public String getTaperollno3() {
        return taperollno3;
    }

    /**
     * ﾛｰﾙNo3
     * @param taperollno3 the taperollno3 to set
     */
    public void setTaperollno3(String taperollno3) {
        this.taperollno3 = taperollno3;
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
     * PETﾌｨﾙﾑ種類
     * @return the petfilmsyurui
     */
    public String getPetfilmsyurui() {
        return petfilmsyurui;
    }

    /**
     * PETﾌｨﾙﾑ種類
     * @param petfilmsyurui the petfilmsyurui to set
     */
    public void setPetfilmsyurui(String petfilmsyurui) {
        this.petfilmsyurui = petfilmsyurui;
    }

    /**
     * 固着ｼｰﾄ貼付り機
     * @return the kotyakugouki
     */
    public String getKotyakugouki() {
        return kotyakugouki;
    }

    /**
     * 固着ｼｰﾄ貼付り機
     * @param kotyakugouki the kotyakugouki to set
     */
    public void setKotyakugouki(String kotyakugouki) {
        this.kotyakugouki = kotyakugouki;
    }

    /**
     * 固着ｼｰﾄ
     * @return the kotyakusheet
     */
    public String getKotyakusheet() {
        return kotyakusheet;
    }

    /**
     * 固着ｼｰﾄ
     * @param kotyakusheet the kotyakusheet to set
     */
    public void setKotyakusheet(String kotyakusheet) {
        this.kotyakusheet = kotyakusheet;
    }

    /**
     * 下端子号機
     * @return the shitaTanshigouki
     */
    public String getShitaTanshigouki() {
        return shitaTanshigouki;
    }

    /**
     * 下端子号機
     * @param shitaTanshigouki the shitaTanshigouki to set
     */
    public void setShitaTanshigouki(String shitaTanshigouki) {
        this.shitaTanshigouki = shitaTanshigouki;
    }

    /**
     * 上端子号機
     * @return the uwaTanshigouki
     */
    public String getUwaTanshigouki() {
        return uwaTanshigouki;
    }

    /**
     * 上端子号機
     * @param uwaTanshigouki the uwaTanshigouki to set
     */
    public void setUwaTanshigouki(String uwaTanshigouki) {
        this.uwaTanshigouki = uwaTanshigouki;
    }

    /**
     * 下端子ﾌﾞｸ抜き
     * @return the shitaTanshiBukunuki
     */
    public String getShitaTanshiBukunuki() {
        return shitaTanshiBukunuki;
    }

    /**
     * 下端子ﾌﾞｸ抜き
     * @param shitaTanshiBukunuki the shitaTanshiBukunuki to set
     */
    public void setShitaTanshiBukunuki(String shitaTanshiBukunuki) {
        this.shitaTanshiBukunuki = shitaTanshiBukunuki;
    }

    /**
     * 下端子
     * @return the shitaTanshi
     */
    public String getShitaTanshi() {
        return shitaTanshi;
    }

    /**
     * 下端子
     * @param shitaTanshi the shitaTanshi to set
     */
    public void setShitaTanshi(String shitaTanshi) {
        this.shitaTanshi = shitaTanshi;
    }

    /**
     * 剥離吸引圧
     * @return the hakuriKyuin
     */
    public Long getHakuriKyuin() {
        return hakuriKyuin;
    }

    /**
     * 剥離吸引圧
     * @param hakuriKyuin the hakuriKyuin to set
     */
    public void setHakuriKyuin(Long hakuriKyuin) {
        this.hakuriKyuin = hakuriKyuin;
    }

    /**
     * 剥離ｸﾘｱﾗﾝｽ
     * @return the hakuriClearrance
     */
    public BigDecimal getHakuriClearrance() {
        return hakuriClearrance;
    }

    /**
     * 剥離ｸﾘｱﾗﾝｽ
     * @param hakuriClearrance the hakuriClearrance to set
     */
    public void setHakuriClearrance(BigDecimal hakuriClearrance) {
        this.hakuriClearrance = hakuriClearrance;
    }

    /**
     * 剥離ｶｯﾄ速度
     * @return the hakuriCutSpeed
     */
    public Long getHakuriCutSpeed() {
        return hakuriCutSpeed;
    }

    /**
     * 剥離ｶｯﾄ速度
     * @param hakuriCutSpeed the hakuriCutSpeed to set
     */
    public void setHakuriCutSpeed(Long hakuriCutSpeed) {
        this.hakuriCutSpeed = hakuriCutSpeed;
    }

    /**
     * 下ﾊﾟﾝﾁ温度
     * @return the shitaPanchiOndo
     */
    public BigDecimal getShitaPanchiOndo() {
        return shitaPanchiOndo;
    }

    /**
     * 下ﾊﾟﾝﾁ温度
     * @param shitaPanchiOndo the shitaPanchiOndo to set
     */
    public void setShitaPanchiOndo(BigDecimal shitaPanchiOndo) {
        this.shitaPanchiOndo = shitaPanchiOndo;
    }

    /**
     * 上ﾊﾟﾝﾁ温度
     * @return the uwaPanchiOndo
     */
    public BigDecimal getUwaPanchiOndo() {
        return uwaPanchiOndo;
    }

    /**
     * 上ﾊﾟﾝﾁ温度
     * @param uwaPanchiOndo the uwaPanchiOndo to set
     */
    public void setUwaPanchiOndo(BigDecimal uwaPanchiOndo) {
        this.uwaPanchiOndo = uwaPanchiOndo;
    }

    /**
     * 加圧時間
     * @return the kaatuJikan
     */
    public BigDecimal getKaatuJikan() {
        return kaatuJikan;
    }

    /**
     * 加圧時間
     * @param kaatuJikan the kaatuJikan to set
     */
    public void setKaatuJikan(BigDecimal kaatuJikan) {
        this.kaatuJikan = kaatuJikan;
    }

    /**
     * 加圧圧力
     * @return the kaatuAturyoku
     */
    public Long getKaatuAturyoku() {
        return kaatuAturyoku;
    }

    /**
     * 加圧圧力
     * @param kaatuAturyoku the kaatuAturyoku to set
     */
    public void setKaatuAturyoku(Long kaatuAturyoku) {
        this.kaatuAturyoku = kaatuAturyoku;
    }

    /**
     * 上端子
     * @return the uwaTanshi
     */
    public String getUwaTanshi() {
        return uwaTanshi;
    }

    /**
     * 上端子
     * @param uwaTanshi the uwaTanshi to set
     */
    public void setUwaTanshi(String uwaTanshi) {
        this.uwaTanshi = uwaTanshi;
    }

    /**
     * 外観確認1
     * @return the gaikanKakunin1
     */
    public String getGaikanKakunin1() {
        return gaikanKakunin1;
    }

    /**
     * 外観確認1
     * @param gaikanKakunin1 the gaikanKakunin1 to set
     */
    public void setGaikanKakunin1(String gaikanKakunin1) {
        this.gaikanKakunin1 = gaikanKakunin1;
    }

    /**
     * 外観確認2
     * @return the gaikanKakunin2
     */
    public String getGaikanKakunin2() {
        return gaikanKakunin2;
    }

    /**
     * 外観確認2
     * @param gaikanKakunin2 the gaikanKakunin2 to set
     */
    public void setGaikanKakunin2(String gaikanKakunin2) {
        this.gaikanKakunin2 = gaikanKakunin2;
    }

    /**
     * 外観確認3
     * @return the gaikanKakunin3
     */
    public String getGaikanKakunin3() {
        return gaikanKakunin3;
    }

    /**
     * 外観確認3
     * @param gaikanKakunin3 the gaikanKakunin3 to set
     */
    public void setGaikanKakunin3(String gaikanKakunin3) {
        this.gaikanKakunin3 = gaikanKakunin3;
    }

    /**
     * 外観確認4
     * @return the gaikanKakunin4
     */
    public String getGaikanKakunin4() {
        return gaikanKakunin4;
    }

    /**
     * 外観確認4
     * @param gaikanKakunin4 the gaikanKakunin4 to set
     */
    public void setGaikanKakunin4(String gaikanKakunin4) {
        this.gaikanKakunin4 = gaikanKakunin4;
    }

    /**
     * 処理ｾｯﾄ数
     * @return the syoriSetsuu
     */
    public Long getSyoriSetsuu() {
        return syoriSetsuu;
    }

    /**
     * 処理ｾｯﾄ数
     * @param syoriSetsuu the syoriSetsuu to set
     */
    public void setSyoriSetsuu(Long syoriSetsuu) {
        this.syoriSetsuu = syoriSetsuu;
    }

    /**
     * 良品ｾｯﾄ数
     * @return the ryouhinSetsuu
     */
    public BigDecimal getRyouhinSetsuu() {
        return ryouhinSetsuu;
    }

    /**
     * 良品ｾｯﾄ数
     * @param ryouhinSetsuu the ryouhinSetsuu to set
     */
    public void setRyouhinSetsuu(BigDecimal ryouhinSetsuu) {
        this.ryouhinSetsuu = ryouhinSetsuu;
    }

    /**
     * 開始担当者
     * @return the startTantosyacode
     */
    public String getStartTantosyacode() {
        return startTantosyacode;
    }

    /**
     * 開始担当者
     * @param startTantosyacode the startTantosyacode to set
     */
    public void setStartTantosyacode(String startTantosyacode) {
        this.startTantosyacode = startTantosyacode;
    }

    /**
     * 終了担当者
     * @return the endTantousyacode
     */
    public String getEndTantousyacode() {
        return endTantousyacode;
    }

    /**
     * 終了担当者
     * @param endTantousyacode the endTantousyacode to set
     */
    public void setEndTantousyacode(String endTantousyacode) {
        this.endTantousyacode = endTantousyacode;
    }

    /**
     * 端子ﾃｰﾌﾟ種類確認
     * @return the tanshiTapeSyurui
     */
    public String getTanshiTapeSyurui() {
        return tanshiTapeSyurui;
    }

    /**
     * 端子ﾃｰﾌﾟ種類確認
     * @param tanshiTapeSyurui the tanshiTapeSyurui to set
     */
    public void setTanshiTapeSyurui(String tanshiTapeSyurui) {
        this.tanshiTapeSyurui = tanshiTapeSyurui;
    }

    /**
     * ｾｯﾄ数1
     * @return the setsuu1
     */
    public Long getSetsuu1() {
        return setsuu1;
    }

    /**
     * ｾｯﾄ数1
     * @param setsuu1 the setsuu1 to set
     */
    public void setSetsuu1(Long setsuu1) {
        this.setsuu1 = setsuu1;
    }

    /**
     * ｾｯﾄ数2
     * @return the setsuu2
     */
    public Long getSetsuu2() {
        return setsuu2;
    }

    /**
     * ｾｯﾄ数2
     * @param setsuu2 the setsuu2 to set
     */
    public void setSetsuu2(Long setsuu2) {
        this.setsuu2 = setsuu2;
    }

    /**
     * ｾｯﾄ数3
     * @return the setsuu3
     */
    public Long getSetsuu3() {
        return setsuu3;
    }

    /**
     * ｾｯﾄ数3
     * @param setsuu3 the setsuu3 to set
     */
    public void setSetsuu3(Long setsuu3) {
        this.setsuu3 = setsuu3;
    }

    /**
     * ｾｯﾄ数4
     * @return the setsuu4
     */
    public Long getSetsuu4() {
        return setsuu4;
    }

    /**
     * ｾｯﾄ数4
     * @param setsuu4 the setsuu4 to set
     */
    public void setSetsuu4(Long setsuu4) {
        this.setsuu4 = setsuu4;
    }

    /**
     * ｾｯﾄ数5
     * @return the setsuu5
     */
    public Long getSetsuu5() {
        return setsuu5;
    }

    /**
     * ｾｯﾄ数5
     * @param setsuu5 the setsuu5 to set
     */
    public void setSetsuu5(Long setsuu5) {
        this.setsuu5 = setsuu5;
    }

    /**
     * ｾｯﾄ数6
     * @return the setsuu6
     */
    public Long getSetsuu6() {
        return setsuu6;
    }

    /**
     * ｾｯﾄ数6
     * @param setsuu6 the setsuu6 to set
     */
    public void setSetsuu6(Long setsuu6) {
        this.setsuu6 = setsuu6;
    }

    /**
     * ｾｯﾄ数7
     * @return the setsuu7
     */
    public Long getSetsuu7() {
        return setsuu7;
    }

    /**
     * ｾｯﾄ数7
     * @param setsuu7 the setsuu7 to set
     */
    public void setSetsuu7(Long setsuu7) {
        this.setsuu7 = setsuu7;
    }

    /**
     * ｾｯﾄ数8
     * @return the setsuu8
     */
    public Long getSetsuu8() {
        return setsuu8;
    }

    /**
     * ｾｯﾄ数8
     * @param setsuu8 the setsuu8 to set
     */
    public void setSetsuu8(Long setsuu8) {
        this.setsuu8 = setsuu8;
    }

    /**
     * ｾｯﾄ数9
     * @return the setsuu9
     */
    public Long getSetsuu9() {
        return setsuu9;
    }

    /**
     * ｾｯﾄ数9
     * @param setsuu9 the setsuu9 to set
     */
    public void setSetsuu9(Long setsuu9) {
        this.setsuu9 = setsuu9;
    }

    /**
     * ｾｯﾄ数10
     * @return the setsuu10
     */
    public Long getSetsuu10() {
        return setsuu10;
    }

    /**
     * ｾｯﾄ数10
     * @param setsuu10 the setsuu10 to set
     */
    public void setSetsuu10(Long setsuu10) {
        this.setsuu10 = setsuu10;
    }

    /**
     * ｾｯﾄ数11
     * @return the setsuu11
     */
    public Long getSetsuu11() {
        return setsuu11;
    }

    /**
     * ｾｯﾄ数11
     * @param setsuu11 the setsuu11 to set
     */
    public void setSetsuu11(Long setsuu11) {
        this.setsuu11 = setsuu11;
    }

    /**
     * ｾｯﾄ数12
     * @return the setsuu12
     */
    public Long getSetsuu12() {
        return setsuu12;
    }

    /**
     * ｾｯﾄ数12
     * @param setsuu12 the setsuu12 to set
     */
    public void setSetsuu12(Long setsuu12) {
        this.setsuu12 = setsuu12;
    }

    /**
     * ｾｯﾄ数13
     * @return the setsuu13
     */
    public Long getSetsuu13() {
        return setsuu13;
    }

    /**
     * ｾｯﾄ数13
     * @param setsuu13 the setsuu13 to set
     */
    public void setSetsuu13(Long setsuu13) {
        this.setsuu13 = setsuu13;
    }

    /**
     * ｾｯﾄ数14
     * @return the setsuu14
     */
    public Long getSetsuu14() {
        return setsuu14;
    }

    /**
     * ｾｯﾄ数14
     * @param setsuu14 the setsuu14 to set
     */
    public void setSetsuu14(Long setsuu14) {
        this.setsuu14 = setsuu14;
    }

    /**
     * ｾｯﾄ数15
     * @return the setsuu15
     */
    public Long getSetsuu15() {
        return setsuu15;
    }

    /**
     * ｾｯﾄ数15
     * @param setsuu15 the setsuu15 to set
     */
    public void setSetsuu15(Long setsuu15) {
        this.setsuu15 = setsuu15;
    }

    /**
     * ｾｯﾄ数16
     * @return the setsuu16
     */
    public Long getSetsuu16() {
        return setsuu16;
    }

    /**
     * ｾｯﾄ数16
     * @param setsuu16 the setsuu16 to set
     */
    public void setSetsuu16(Long setsuu16) {
        this.setsuu16 = setsuu16;
    }

    /**
     * ｾｯﾄ数17
     * @return the setsuu17
     */
    public Long getSetsuu17() {
        return setsuu17;
    }

    /**
     * ｾｯﾄ数17
     * @param setsuu17 the setsuu17 to set
     */
    public void setSetsuu17(Long setsuu17) {
        this.setsuu17 = setsuu17;
    }

    /**
     * ｾｯﾄ数18
     * @return the setsuu18
     */
    public Long getSetsuu18() {
        return setsuu18;
    }

    /**
     * ｾｯﾄ数18
     * @param setsuu18 the setsuu18 to set
     */
    public void setSetsuu18(Long setsuu18) {
        this.setsuu18 = setsuu18;
    }

    /**
     * ｾｯﾄ数19
     * @return the setsuu19
     */
    public Long getSetsuu19() {
        return setsuu19;
    }

    /**
     * ｾｯﾄ数19
     * @param setsuu19 the setsuu19 to set
     */
    public void setSetsuu19(Long setsuu19) {
        this.setsuu19 = setsuu19;
    }

    /**
     * ｾｯﾄ数20
     * @return the setsuu20
     */
    public Long getSetsuu20() {
        return setsuu20;
    }

    /**
     * ｾｯﾄ数20
     * @param setsuu20 the setsuu20 to set
     */
    public void setSetsuu20(Long setsuu20) {
        this.setsuu20 = setsuu20;
    }

    /**
     * ｾｯﾄ数21
     * @return the setsuu21
     */
    public Long getSetsuu21() {
        return setsuu21;
    }

    /**
     * ｾｯﾄ数21
     * @param setsuu21 the setsuu21 to set
     */
    public void setSetsuu21(Long setsuu21) {
        this.setsuu21 = setsuu21;
    }

    /**
     * ｾｯﾄ数22
     * @return the setsuu22
     */
    public Long getSetsuu22() {
        return setsuu22;
    }

    /**
     * ｾｯﾄ数22
     * @param setsuu22 the setsuu22 to set
     */
    public void setSetsuu22(Long setsuu22) {
        this.setsuu22 = setsuu22;
    }

    /**
     * ｾｯﾄ数23
     * @return the setsuu23
     */
    public Long getSetsuu23() {
        return setsuu23;
    }

    /**
     * ｾｯﾄ数23
     * @param setsuu23 the setsuu23 to set
     */
    public void setSetsuu23(Long setsuu23) {
        this.setsuu23 = setsuu23;
    }

    /**
     * ｾｯﾄ数24
     * @return the setsuu24
     */
    public Long getSetsuu24() {
        return setsuu24;
    }

    /**
     * ｾｯﾄ数24
     * @param setsuu24 the setsuu24 to set
     */
    public void setSetsuu24(Long setsuu24) {
        this.setsuu24 = setsuu24;
    }

    /**
     * ｾｯﾄ数25
     * @return the setsuu25
     */
    public Long getSetsuu25() {
        return setsuu25;
    }

    /**
     * ｾｯﾄ数25
     * @param setsuu25 the setsuu25 to set
     */
    public void setSetsuu25(Long setsuu25) {
        this.setsuu25 = setsuu25;
    }

    /**
     * ｾｯﾄ数26
     * @return the setsuu26
     */
    public Long getSetsuu26() {
        return setsuu26;
    }

    /**
     * ｾｯﾄ数26
     * @param setsuu26 the setsuu26 to set
     */
    public void setSetsuu26(Long setsuu26) {
        this.setsuu26 = setsuu26;
    }

    /**
     * ｾｯﾄ数27
     * @return the setsuu27
     */
    public Long getSetsuu27() {
        return setsuu27;
    }

    /**
     * ｾｯﾄ数27
     * @param setsuu27 the setsuu27 to set
     */
    public void setSetsuu27(Long setsuu27) {
        this.setsuu27 = setsuu27;
    }

    /**
     * ｾｯﾄ数28
     * @return the setsuu28
     */
    public Long getSetsuu28() {
        return setsuu28;
    }

    /**
     * ｾｯﾄ数28
     * @param setsuu28 the setsuu28 to set
     */
    public void setSetsuu28(Long setsuu28) {
        this.setsuu28 = setsuu28;
    }

    /**
     * ｾｯﾄ数29
     * @return the setsuu29
     */
    public Long getSetsuu29() {
        return setsuu29;
    }

    /**
     * ｾｯﾄ数29
     * @param setsuu29 the setsuu29 to set
     */
    public void setSetsuu29(Long setsuu29) {
        this.setsuu29 = setsuu29;
    }

    /**
     * ｾｯﾄ数30
     * @return the setsuu30
     */
    public Long getSetsuu30() {
        return setsuu30;
    }

    /**
     * ｾｯﾄ数30
     * @param setsuu30 the setsuu30 to set
     */
    public void setSetsuu30(Long setsuu30) {
        this.setsuu30 = setsuu30;
    }

    /**
     * ｾｯﾄ数31
     * @return the setsuu31
     */
    public Long getSetsuu31() {
        return setsuu31;
    }

    /**
     * ｾｯﾄ数31
     * @param setsuu31 the setsuu31 to set
     */
    public void setSetsuu31(Long setsuu31) {
        this.setsuu31 = setsuu31;
    }

    /**
     * ｾｯﾄ数32
     * @return the setsuu32
     */
    public Long getSetsuu32() {
        return setsuu32;
    }

    /**
     * ｾｯﾄ数32
     * @param setsuu32 the setsuu32 to set
     */
    public void setSetsuu32(Long setsuu32) {
        this.setsuu32 = setsuu32;
    }

    /**
     * ｾｯﾄ数33
     * @return the setsuu33
     */
    public Long getSetsuu33() {
        return setsuu33;
    }

    /**
     * ｾｯﾄ数33
     * @param setsuu33 the setsuu33 to set
     */
    public void setSetsuu33(Long setsuu33) {
        this.setsuu33 = setsuu33;
    }

    /**
     * ｾｯﾄ数34
     * @return the setsuu34
     */
    public Long getSetsuu34() {
        return setsuu34;
    }

    /**
     * ｾｯﾄ数34
     * @param setsuu34 the setsuu34 to set
     */
    public void setSetsuu34(Long setsuu34) {
        this.setsuu34 = setsuu34;
    }

    /**
     * ｾｯﾄ数35
     * @return the setsuu35
     */
    public Long getSetsuu35() {
        return setsuu35;
    }

    /**
     * ｾｯﾄ数35
     * @param setsuu35 the setsuu35 to set
     */
    public void setSetsuu35(Long setsuu35) {
        this.setsuu35 = setsuu35;
    }

    /**
     * ｾｯﾄ数36
     * @return the setsuu36
     */
    public Long getSetsuu36() {
        return setsuu36;
    }

    /**
     * ｾｯﾄ数36
     * @param setsuu36 the setsuu36 to set
     */
    public void setSetsuu36(Long setsuu36) {
        this.setsuu36 = setsuu36;
    }

    /**
     * ｾｯﾄ数37
     * @return the setsuu37
     */
    public Long getSetsuu37() {
        return setsuu37;
    }

    /**
     * ｾｯﾄ数37
     * @param setsuu37 the setsuu37 to set
     */
    public void setSetsuu37(Long setsuu37) {
        this.setsuu37 = setsuu37;
    }

    /**
     * ｾｯﾄ数38
     * @return the setsuu38
     */
    public Long getSetsuu38() {
        return setsuu38;
    }

    /**
     * ｾｯﾄ数38
     * @param setsuu38 the setsuu38 to set
     */
    public void setSetsuu38(Long setsuu38) {
        this.setsuu38 = setsuu38;
    }

    /**
     * ｾｯﾄ数39
     * @return the setsuu39
     */
    public Long getSetsuu39() {
        return setsuu39;
    }

    /**
     * ｾｯﾄ数39
     * @param setsuu39 the setsuu39 to set
     */
    public void setSetsuu39(Long setsuu39) {
        this.setsuu39 = setsuu39;
    }

    /**
     * ｾｯﾄ数40
     * @return the setsuu40
     */
    public Long getSetsuu40() {
        return setsuu40;
    }

    /**
     * ｾｯﾄ数40
     * @param setsuu40 the setsuu40 to set
     */
    public void setSetsuu40(Long setsuu40) {
        this.setsuu40 = setsuu40;
    }

    /**
     * 備考1
     * @return the hbikou1
     */
    public String getHbikou1() {
        return hbikou1;
    }

    /**
     * 備考1
     * @param hbikou1 the hbikou1 to set
     */
    public void setHbikou1(String hbikou1) {
        this.hbikou1 = hbikou1;
    }

    /**
     * 備考2
     * @return the hbikou2
     */
    public String getHbikou2() {
        return hbikou2;
    }

    /**
     * 備考2
     * @param hbikou2 the hbikou2 to set
     */
    public void setHbikou2(String hbikou2) {
        this.hbikou2 = hbikou2;
    }

    /**
     * 備考3
     * @return the hbikou3
     */
    public String getHbikou3() {
        return hbikou3;
    }

    /**
     * 備考3
     * @param hbikou3 the hbikou3 to set
     */
    public void setHbikou3(String hbikou3) {
        this.hbikou3 = hbikou3;
    }

    /**
     * 備考4
     * @return the hbikou4
     */
    public String getHbikou4() {
        return hbikou4;
    }

    /**
     * 備考4
     * @param hbikou4 the hbikou4 to set
     */
    public void setHbikou4(String hbikou4) {
        this.hbikou4 = hbikou4;
    }

    /**
     * 備考5
     * @return the hbikou5
     */
    public String getHbikou5() {
        return hbikou5;
    }

    /**
     * 備考5
     * @param hbikou5 the hbikou5 to set
     */
    public void setHbikou5(String hbikou5) {
        this.hbikou5 = hbikou5;
    }

    /**
     * 備考6
     * @return the hbikou6
     */
    public String getHbikou6() {
        return hbikou6;
    }

    /**
     * 備考6
     * @param hbikou6 the hbikou6 to set
     */
    public void setHbikou6(String hbikou6) {
        this.hbikou6 = hbikou6;
    }

    /**
     * 備考7
     * @return the hbikou7
     */
    public String getHbikou7() {
        return hbikou7;
    }

    /**
     * 備考7
     * @param hbikou7 the hbikou7 to set
     */
    public void setHbikou7(String hbikou7) {
        this.hbikou7 = hbikou7;
    }

    /**
     * 備考8
     * @return the hbikou8
     */
    public String getHbikou8() {
        return hbikou8;
    }

    /**
     * 備考8
     * @param hbikou8 the hbikou8 to set
     */
    public void setHbikou8(String hbikou8) {
        this.hbikou8 = hbikou8;
    }

    /**
     * 備考9
     * @return the hbikou9
     */
    public String getHbikou9() {
        return hbikou9;
    }

    /**
     * 備考9
     * @param hbikou9 the hbikou9 to set
     */
    public void setHbikou9(String hbikou9) {
        this.hbikou9 = hbikou9;
    }

    /**
     * 備考10
     * @return the hbikou10
     */
    public String getHbikou10() {
        return hbikou10;
    }

    /**
     * 備考10
     * @param hbikou10 the hbikou10 to set
     */
    public void setHbikou10(String hbikou10) {
        this.hbikou10 = hbikou10;
    }

    /**
     * 備考11
     * @return the hbikou11
     */
    public String getHbikou11() {
        return hbikou11;
    }

    /**
     * 備考11
     * @param hbikou11 the hbikou11 to set
     */
    public void setHbikou11(String hbikou11) {
        this.hbikou11 = hbikou11;
    }

    /**
     * 備考12
     * @return the hbikou12
     */
    public String getHbikou12() {
        return hbikou12;
    }

    /**
     * 備考12
     * @param hbikou12 the hbikou12 to set
     */
    public void setHbikou12(String hbikou12) {
        this.hbikou12 = hbikou12;
    }

    /**
     * 備考13
     * @return the hbikou13
     */
    public String getHbikou13() {
        return hbikou13;
    }

    /**
     * 備考13
     * @param hbikou13 the hbikou13 to set
     */
    public void setHbikou13(String hbikou13) {
        this.hbikou13 = hbikou13;
    }

    /**
     * 備考14
     * @return the hbikou14
     */
    public String getHbikou14() {
        return hbikou14;
    }

    /**
     * 備考14
     * @param hbikou14 the hbikou14 to set
     */
    public void setHbikou14(String hbikou14) {
        this.hbikou14 = hbikou14;
    }

    /**
     * 備考15
     * @return the hbikou15
     */
    public String getHbikou15() {
        return hbikou15;
    }

    /**
     * 備考15
     * @param hbikou15 the hbikou15 to set
     */
    public void setHbikou15(String hbikou15) {
        this.hbikou15 = hbikou15;
    }

    /**
     * 備考16
     * @return the hbikou16
     */
    public String getHbikou16() {
        return hbikou16;
    }

    /**
     * 備考16
     * @param hbikou16 the hbikou16 to set
     */
    public void setHbikou16(String hbikou16) {
        this.hbikou16 = hbikou16;
    }

    /**
     * 備考17
     * @return the hbikou17
     */
    public String getHbikou17() {
        return hbikou17;
    }

    /**
     * 備考17
     * @param hbikou17 the hbikou17 to set
     */
    public void setHbikou17(String hbikou17) {
        this.hbikou17 = hbikou17;
    }

    /**
     * 備考18
     * @return the hbikou18
     */
    public String getHbikou18() {
        return hbikou18;
    }

    /**
     * 備考18
     * @param hbikou18 the hbikou18 to set
     */
    public void setHbikou18(String hbikou18) {
        this.hbikou18 = hbikou18;
    }

    /**
     * 備考19
     * @return the hbikou19
     */
    public String getHbikou19() {
        return hbikou19;
    }

    /**
     * 備考19
     * @param hbikou19 the hbikou19 to set
     */
    public void setHbikou19(String hbikou19) {
        this.hbikou19 = hbikou19;
    }

    /**
     * 備考20
     * @return the hbikou20
     */
    public String getHbikou20() {
        return hbikou20;
    }

    /**
     * 備考20
     * @param hbikou20 the hbikou20 to set
     */
    public void setHbikou20(String hbikou20) {
        this.hbikou20 = hbikou20;
    }

    /**
     * 備考21
     * @return the hbikou21
     */
    public String getHbikou21() {
        return hbikou21;
    }

    /**
     * 備考21
     * @param hbikou21 the hbikou21 to set
     */
    public void setHbikou21(String hbikou21) {
        this.hbikou21 = hbikou21;
    }

    /**
     * 備考22
     * @return the hbikou22
     */
    public String getHbikou22() {
        return hbikou22;
    }

    /**
     * 備考22
     * @param hbikou22 the hbikou22 to set
     */
    public void setHbikou22(String hbikou22) {
        this.hbikou22 = hbikou22;
    }

    /**
     * 備考23
     * @return the hbikou23
     */
    public String getHbikou23() {
        return hbikou23;
    }

    /**
     * 備考23
     * @param hbikou23 the hbikou23 to set
     */
    public void setHbikou23(String hbikou23) {
        this.hbikou23 = hbikou23;
    }

    /**
     * 備考24
     * @return the hbikou24
     */
    public String getHbikou24() {
        return hbikou24;
    }

    /**
     * 備考24
     * @param hbikou24 the hbikou24 to set
     */
    public void setHbikou24(String hbikou24) {
        this.hbikou24 = hbikou24;
    }

    /**
     * 備考25
     * @return the hbikou25
     */
    public String getHbikou25() {
        return hbikou25;
    }

    /**
     * 備考25
     * @param hbikou25 the hbikou25 to set
     */
    public void setHbikou25(String hbikou25) {
        this.hbikou25 = hbikou25;
    }

    /**
     * 備考26
     * @return the hbikou26
     */
    public String getHbikou26() {
        return hbikou26;
    }

    /**
     * 備考26
     * @param hbikou26 the hbikou26 to set
     */
    public void setHbikou26(String hbikou26) {
        this.hbikou26 = hbikou26;
    }

    /**
     * 備考27
     * @return the hbikou27
     */
    public String getHbikou27() {
        return hbikou27;
    }

    /**
     * 備考27
     * @param hbikou27 the hbikou27 to set
     */
    public void setHbikou27(String hbikou27) {
        this.hbikou27 = hbikou27;
    }

    /**
     * 備考28
     * @return the hbikou28
     */
    public String getHbikou28() {
        return hbikou28;
    }

    /**
     * 備考28
     * @param hbikou28 the hbikou28 to set
     */
    public void setHbikou28(String hbikou28) {
        this.hbikou28 = hbikou28;
    }

    /**
     * 備考29
     * @return the hbikou29
     */
    public String getHbikou29() {
        return hbikou29;
    }

    /**
     * 備考29
     * @param hbikou29 the hbikou29 to set
     */
    public void setHbikou29(String hbikou29) {
        this.hbikou29 = hbikou29;
    }

    /**
     * 備考30
     * @return the hbikou30
     */
    public String getHbikou30() {
        return hbikou30;
    }

    /**
     * 備考30
     * @param hbikou30 the hbikou30 to set
     */
    public void setHbikou30(String hbikou30) {
        this.hbikou30 = hbikou30;
    }

    /**
     * 備考31
     * @return the hbikou31
     */
    public String getHbikou31() {
        return hbikou31;
    }

    /**
     * 備考31
     * @param hbikou31 the hbikou31 to set
     */
    public void setHbikou31(String hbikou31) {
        this.hbikou31 = hbikou31;
    }

    /**
     * 備考32
     * @return the hbikou32
     */
    public String getHbikou32() {
        return hbikou32;
    }

    /**
     * 備考32
     * @param hbikou32 the hbikou32 to set
     */
    public void setHbikou32(String hbikou32) {
        this.hbikou32 = hbikou32;
    }

    /**
     * 備考33
     * @return the hbikou33
     */
    public String getHbikou33() {
        return hbikou33;
    }

    /**
     * 備考33
     * @param hbikou33 the hbikou33 to set
     */
    public void setHbikou33(String hbikou33) {
        this.hbikou33 = hbikou33;
    }

    /**
     * 備考34
     * @return the hbikou34
     */
    public String getHbikou34() {
        return hbikou34;
    }

    /**
     * 備考34
     * @param hbikou34 the hbikou34 to set
     */
    public void setHbikou34(String hbikou34) {
        this.hbikou34 = hbikou34;
    }

    /**
     * 備考35
     * @return the hbikou35
     */
    public String getHbikou35() {
        return hbikou35;
    }

    /**
     * 備考35
     * @param hbikou35 the hbikou35 to set
     */
    public void setHbikou35(String hbikou35) {
        this.hbikou35 = hbikou35;
    }

    /**
     * 備考36
     * @return the hbikou36
     */
    public String getHbikou36() {
        return hbikou36;
    }

    /**
     * 備考36
     * @param hbikou36 the hbikou36 to set
     */
    public void setHbikou36(String hbikou36) {
        this.hbikou36 = hbikou36;
    }

    /**
     * 備考37
     * @return the hbikou37
     */
    public String getHbikou37() {
        return hbikou37;
    }

    /**
     * 備考37
     * @param hbikou37 the hbikou37 to set
     */
    public void setHbikou37(String hbikou37) {
        this.hbikou37 = hbikou37;
    }

    /**
     * 備考38
     * @return the hbikou38
     */
    public String getHbikou38() {
        return hbikou38;
    }

    /**
     * 備考38
     * @param hbikou38 the hbikou38 to set
     */
    public void setHbikou38(String hbikou38) {
        this.hbikou38 = hbikou38;
    }

    /**
     * 備考39
     * @return the hbikou39
     */
    public String getHbikou39() {
        return hbikou39;
    }

    /**
     * 備考39
     * @param hbikou39 the hbikou39 to set
     */
    public void setHbikou39(String hbikou39) {
        this.hbikou39 = hbikou39;
    }

    /**
     * 備考40
     * @return the hbikou40
     */
    public String getHbikou40() {
        return hbikou40;
    }

    /**
     * 備考40
     * @param hbikou40 the hbikou40 to set
     */
    public void setHbikou40(String hbikou40) {
        this.hbikou40 = hbikou40;
    }
}
