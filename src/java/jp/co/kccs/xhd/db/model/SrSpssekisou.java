/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/02/27<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SR_SPSSEKISOU(積層SPS)のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2019/02/27
 */
public class SrSpssekisou {

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
     * 端子ﾃｰﾌﾟ種類
     */
    private String tntapesyurui;

    /**
     * 端子ﾃｰﾌﾟNo
     */
    private String tntapeno;

    /**
     * 端子ﾃｰﾌﾟ原料
     */
    private String tntapegenryou;

    /**
     * 号機ｺｰﾄﾞ
     */
    private String gouki;

    /**
     * 開始日時
     */
    private Timestamp startdatetime;

    /**
     * 終了日時
     */
    private Timestamp enddatetime;

    /**
     * 積層ｽﾞﾚ
     */
    private Integer sekisouzure;

    /**
     * 担当者ｺｰﾄﾞ
     */
    private String tantousya;

    /**
     * 確認者ｺｰﾄﾞ
     */
    private String kakuninsya;

    /**
     * 積層ｽﾞﾚ2
     */
    private Integer sekisouzure2;

    /**
     * 備考1
     */
    private String bikou1;

    /**
     * 備考2
     */
    private String bikou2;

    /**
     * 備考3
     */
    private String bikou3;

    /**
     * 備考4
     */
    private String bikou4;

    /**
     * 備考5
     */
    private String bikou5;

    /**
     * KCPNO
     */
    private String kcpno;

    /**
     * 号機ｺｰﾄﾞ
     */
    private String goukiCode;

    /**
     * ﾃｰﾌﾟLot
     */
    private String tpLot;

    /**
     * 剥離ｽﾋﾟｰﾄﾞ
     */
    private Integer hakuriSpeed;

    /**
     * 金型温度
     */
    private BigDecimal kanaOndo;

    /**
     * 電極圧力
     */
    private Integer daturyoku;

    /**
     * 電極加圧時間
     */
    private BigDecimal dkaatuJikan;

    /**
     * 中間ﾌﾟﾚｽ圧力
     */
    private Integer cpressAturyoku;

    /**
     * 中間ﾌﾟﾚｽ加圧時間
     */
    private BigDecimal cpressKaatuJikan;

    /**
     * 中間ﾌﾟﾚｽ間隔総数
     */
    private Integer cpressKankakuSosuu;

    /**
     * 最終加圧力
     */
    private Integer lastKaaturyoku;

    /**
     * 最終加圧時間
     */
    private Integer lastKaatuJikan;

    /**
     * 4分割担当者ｺｰﾄﾞ
     */
    private String fourSplitTantou;

    /**
     * 積層ﾀｵﾚ量1
     */
    private Integer staoreryo1;

    /**
     * 積層ﾀｵﾚ量2
     */
    private Integer staoreryo2;

    /**
     * 積層ﾀｵﾚ量3
     */
    private Integer staoreryo3;

    /**
     * 積層ﾀｵﾚ量4
     */
    private Integer staoreryo4;

    /**
     * 積層後T寸法AVE
     */
    private Integer stsunAve;

    /**
     * 積層後T寸法MAX
     */
    private Integer stsunMax;

    /**
     * 積層後T寸法MIN
     */
    private Integer stsunMin;

    /**
     * 積層後T寸法σ
     */
    private BigDecimal stsunSiguma;

    /**
     * 隔離NG回数
     */
    private Integer hngKaisuu;

    /**
     * 最終外観担当者ｺｰﾄﾞ
     */
    private String gaikanTantou;

    /**
     * 中間ﾌﾟﾚｽ回数
     */
    private Integer cpressKaisuu;

    /**
     * 剥離NG回数AVE
     */
    private BigDecimal hngKaisuuAve;

    /**
     * 画像NG回数
     */
    private Integer gngKaisuu;

    /**
     * 画像NG回数AVE
     */
    private BigDecimal gngKaisuuAve;

    /**
     * ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
     */
    private String tapelotno;

    /**
     * ﾃｰﾌﾟﾛｰﾙNo1
     */
    private String taperollno1;

    /**
     * ﾃｰﾌﾟﾛｰﾙNo2
     */
    private String taperollno2;

    /**
     * ﾃｰﾌﾟﾛｰﾙNo3
     */
    private String taperollno3;

    /**
     * 原料記号
     */
    private String genryoukigou;

    /**
     * PETﾌｨﾙﾑ種類
     */
    private String petfilmsyurui;

    /**
     * 固着ｼｰﾄ
     */
    private String kotyakusheet;

    /**
     * 下端子号機
     */
    private String shitaTanshigouki;

    /**
     * 上端子号機
     */
    private String uwaTanshigouki;

    /**
     * 下端子ﾌﾞｸ抜き
     */
    private String shitaTanshiBukunuki;

    /**
     * 下端子
     */
    private String shitaTanshi;

    /**
     * 剥離吸引圧
     */
    private Integer hakuriKyuin;

    /**
     * 剥離ｸﾘｱﾗﾝｽ
     */
    private BigDecimal hakuriClearrance;

    /**
     * 剥離ｶｯﾄ速度
     */
    private Integer hakuriCutSpeed;

    /**
     * 下ﾊﾟﾝﾁ温度
     */
    private BigDecimal shitaPanchiOndo;

    /**
     * 上ﾊﾟﾝﾁ温度
     */
    private BigDecimal uwaPanchiOndo;

    /**
     * 加圧時間
     */
    private BigDecimal kaatuJikan;

    /**
     * 加圧圧力
     */
    private Integer kaatuAturyoku;

    /**
     * 上端子
     */
    private String uwaTanshi;

    /**
     * 処理ｾｯﾄ数
     */
    private Integer syoriSetsuu;

    /**
     * 良品ｾｯﾄ数
     */
    private BigDecimal ryouhinSetsuu;

    /**
     * 開始担当者ｺｰﾄﾞ
     */
    private String startTantosyacode;

    /**
     * 終了担当者ｺｰﾄﾞ
     */
    private String endTantousyacode;

    /**
     * 端子ﾃｰﾌﾟ種類
     */
    private Integer tanshiTapeSyurui;

    /**
     * 登録日時
     */
    private Timestamp torokunichiji;

    /**
     * 更新日時
     */
    private Timestamp kosinnichiji;

    /**
     * 空打ち
     */
    private String karauti;
    
    /**
     * 空打ち秒
     */
    private Integer karautibyou;
    
    /**
     * 空打ち回
     */
    private Integer karautikai;
    
    /**
     * ｽﾞﾚ値
     */
    private Integer zureti;
    
    /**
     * 外観確認5
     */
    private String gaikanKakunin5;

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
     * @return kojyo
     */
    public String getKojyo() {
        return kojyo;
    }

    /**
     * 工場ｺｰﾄﾞ
     * @param kojyo セットする kojyo
     */
    public void setKojyo(String kojyo) {
        this.kojyo = kojyo;
    }

    /**
     * ﾛｯﾄNo
     * @return lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * ﾛｯﾄNo
     * @param lotno セットする lotno
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * 枝番
     * @return edaban
     */
    public String getEdaban() {
        return edaban;
    }

    /**
     * 枝番
     * @param edaban セットする edaban
     */
    public void setEdaban(String edaban) {
        this.edaban = edaban;
    }

    /**
     * 端子ﾃｰﾌﾟ種類
     * @return tntapesyurui
     */
    public String getTntapesyurui() {
        return tntapesyurui;
    }

    /**
     * 端子ﾃｰﾌﾟ種類
     * @param tntapesyurui セットする tntapesyurui
     */
    public void setTntapesyurui(String tntapesyurui) {
        this.tntapesyurui = tntapesyurui;
    }

    /**
     * 端子ﾃｰﾌﾟNo
     * @return tntapeno
     */
    public String getTntapeno() {
        return tntapeno;
    }

    /**
     * 端子ﾃｰﾌﾟNo
     * @param tntapeno セットする tntapeno
     */
    public void setTntapeno(String tntapeno) {
        this.tntapeno = tntapeno;
    }

    /**
     * 端子ﾃｰﾌﾟ原料
     * @return tntapegenryou
     */
    public String getTntapegenryou() {
        return tntapegenryou;
    }

    /**
     * 端子ﾃｰﾌﾟ原料
     * @param tntapegenryou セットする tntapegenryou
     */
    public void setTntapegenryou(String tntapegenryou) {
        this.tntapegenryou = tntapegenryou;
    }

    /**
     * 号機ｺｰﾄﾞ
     * @return gouki
     */
    public String getGouki() {
        return gouki;
    }

    /**
     * 号機ｺｰﾄﾞ
     * @param gouki セットする gouki
     */
    public void setGouki(String gouki) {
        this.gouki = gouki;
    }

    /**
     * 開始日時
     * @return startdatetime
     */
    public Timestamp getStartdatetime() {
        return startdatetime;
    }

    /**
     * 開始日時
     * @param startdatetime セットする startdatetime
     */
    public void setStartdatetime(Timestamp startdatetime) {
        this.startdatetime = startdatetime;
    }

    /**
     * 終了日時
     * @return enddatetime
     */
    public Timestamp getEnddatetime() {
        return enddatetime;
    }

    /**
     * 終了日時
     * @param enddatetime セットする enddatetime
     */
    public void setEnddatetime(Timestamp enddatetime) {
        this.enddatetime = enddatetime;
    }

    /**
     * 積層ｽﾞﾚ
     * @return sekisouzure
     */
    public Integer getSekisouzure() {
        return sekisouzure;
    }

    /**
     * 積層ｽﾞﾚ
     * @param sekisouzure セットする sekisouzure
     */
    public void setSekisouzure(Integer sekisouzure) {
        this.sekisouzure = sekisouzure;
    }

    /**
     * 担当者ｺｰﾄﾞ
     * @return tantousya
     */
    public String getTantousya() {
        return tantousya;
    }

    /**
     * 担当者ｺｰﾄﾞ
     * @param tantousya セットする tantousya
     */
    public void setTantousya(String tantousya) {
        this.tantousya = tantousya;
    }

    /**
     * 確認者ｺｰﾄﾞ
     * @return kakuninsya
     */
    public String getKakuninsya() {
        return kakuninsya;
    }

    /**
     * 確認者ｺｰﾄﾞ
     * @param kakuninsya セットする kakuninsya
     */
    public void setKakuninsya(String kakuninsya) {
        this.kakuninsya = kakuninsya;
    }

    /**
     * 積層ｽﾞﾚ2
     * @return sekisouzure2
     */
    public Integer getSekisouzure2() {
        return sekisouzure2;
    }

    /**
     * 積層ｽﾞﾚ2
     * @param sekisouzure2 セットする sekisouzure2
     */
    public void setSekisouzure2(Integer sekisouzure2) {
        this.sekisouzure2 = sekisouzure2;
    }

    /**
     * 備考1
     * @return bikou1
     */
    public String getBikou1() {
        return bikou1;
    }

    /**
     * 備考1
     * @param bikou1 セットする bikou1
     */
    public void setBikou1(String bikou1) {
        this.bikou1 = bikou1;
    }

    /**
     * 備考2
     * @return bikou2
     */
    public String getBikou2() {
        return bikou2;
    }

    /**
     * 備考2
     * @param bikou2 セットする bikou2
     */
    public void setBikou2(String bikou2) {
        this.bikou2 = bikou2;
    }

    /**
     * 備考3
     * @return bikou3
     */
    public String getBikou3() {
        return bikou3;
    }

    /**
     * 備考3
     * @param bikou3 セットする bikou3
     */
    public void setBikou3(String bikou3) {
        this.bikou3 = bikou3;
    }

    /**
     * 備考4
     * @return bikou4
     */
    public String getBikou4() {
        return bikou4;
    }

    /**
     * 備考4
     * @param bikou4 セットする bikou4
     */
    public void setBikou4(String bikou4) {
        this.bikou4 = bikou4;
    }

    /**
     * 備考5
     * @return bikou5
     */
    public String getBikou5() {
        return bikou5;
    }

    /**
     * 備考5
     * @param bikou5 セットする bikou5
     */
    public void setBikou5(String bikou5) {
        this.bikou5 = bikou5;
    }

    /**
     * KCPNO
     * @return kcpno
     */
    public String getKcpno() {
        return kcpno;
    }

    /**
     * KCPNO
     * @param kcpno セットする kcpno
     */
    public void setKcpno(String kcpno) {
        this.kcpno = kcpno;
    }

    /**
     * 号機ｺｰﾄﾞ
     * @return goukiCode
     */
    public String getGoukiCode() {
        return goukiCode;
    }

    /**
     * 号機ｺｰﾄﾞ
     * @param goukiCode セットする goukiCode
     */
    public void setGoukiCode(String goukiCode) {
        this.goukiCode = goukiCode;
    }

    /**
     * ﾃｰﾌﾟLot
     * @return tpLot
     */
    public String getTpLot() {
        return tpLot;
    }

    /**
     * ﾃｰﾌﾟLot
     * @param tpLot セットする tpLot
     */
    public void setTpLot(String tpLot) {
        this.tpLot = tpLot;
    }

    /**
     * 剥離ｽﾋﾟｰﾄﾞ
     * @return hakuriSpeed
     */
    public Integer getHakuriSpeed() {
        return hakuriSpeed;
    }

    /**
     * 剥離ｽﾋﾟｰﾄﾞ
     * @param hakuriSpeed セットする hakuriSpeed
     */
    public void setHakuriSpeed(Integer hakuriSpeed) {
        this.hakuriSpeed = hakuriSpeed;
    }

    /**
     * 金型温度
     * @return kanaOndo
     */
    public BigDecimal getKanaOndo() {
        return kanaOndo;
    }

    /**
     * 金型温度
     * @param kanaOndo セットする kanaOndo
     */
    public void setKanaOndo(BigDecimal kanaOndo) {
        this.kanaOndo = kanaOndo;
    }

    /**
     * 電極圧力
     * @return daturyoku
     */
    public Integer getDaturyoku() {
        return daturyoku;
    }

    /**
     * 電極圧力
     * @param daturyoku セットする daturyoku
     */
    public void setDaturyoku(Integer daturyoku) {
        this.daturyoku = daturyoku;
    }

    /**
     * 電極加圧時間
     * @return dkaatuJikan
     */
    public BigDecimal getDkaatuJikan() {
        return dkaatuJikan;
    }

    /**
     * 電極加圧時間
     * @param dkaatuJikan セットする dkaatuJikan
     */
    public void setDkaatuJikan(BigDecimal dkaatuJikan) {
        this.dkaatuJikan = dkaatuJikan;
    }

    /**
     * 中間ﾌﾟﾚｽ圧力
     * @return cpressAturyoku
     */
    public Integer getCpressAturyoku() {
        return cpressAturyoku;
    }

    /**
     * 中間ﾌﾟﾚｽ圧力
     * @param cpressAturyoku セットする cpressAturyoku
     */
    public void setCpressAturyoku(Integer cpressAturyoku) {
        this.cpressAturyoku = cpressAturyoku;
    }

    /**
     * 中間ﾌﾟﾚｽ加圧時間
     * @return cpressKaatuJikan
     */
    public BigDecimal getCpressKaatuJikan() {
        return cpressKaatuJikan;
    }

    /**
     * 中間ﾌﾟﾚｽ加圧時間
     * @param cpressKaatuJikan セットする cpressKaatuJikan
     */
    public void setCpressKaatuJikan(BigDecimal cpressKaatuJikan) {
        this.cpressKaatuJikan = cpressKaatuJikan;
    }

    /**
     * 中間ﾌﾟﾚｽ間隔総数
     * @return cpressKankakuSosuu
     */
    public Integer getCpressKankakuSosuu() {
        return cpressKankakuSosuu;
    }

    /**
     * 中間ﾌﾟﾚｽ間隔総数
     * @param cpressKankakuSosuu セットする cpressKankakuSosuu
     */
    public void setCpressKankakuSosuu(Integer cpressKankakuSosuu) {
        this.cpressKankakuSosuu = cpressKankakuSosuu;
    }

    /**
     * 最終加圧力
     * @return lastKaaturyoku
     */
    public Integer getLastKaaturyoku() {
        return lastKaaturyoku;
    }

    /**
     * 最終加圧力
     * @param lastKaaturyoku セットする lastKaaturyoku
     */
    public void setLastKaaturyoku(Integer lastKaaturyoku) {
        this.lastKaaturyoku = lastKaaturyoku;
    }

    /**
     * 最終加圧時間
     * @return lastKaatuJikan
     */
    public Integer getLastKaatuJikan() {
        return lastKaatuJikan;
    }

    /**
     * 最終加圧時間
     * @param lastKaatuJikan セットする lastKaatuJikan
     */
    public void setLastKaatuJikan(Integer lastKaatuJikan) {
        this.lastKaatuJikan = lastKaatuJikan;
    }

    /**
     * 4分割担当者ｺｰﾄﾞ
     * @return fourSplitTantou
     */
    public String getFourSplitTantou() {
        return fourSplitTantou;
    }

    /**
     * 4分割担当者ｺｰﾄﾞ
     * @param fourSplitTantou セットする fourSplitTantou
     */
    public void setFourSplitTantou(String fourSplitTantou) {
        this.fourSplitTantou = fourSplitTantou;
    }

    /**
     * 積層ﾀｵﾚ量1
     * @return staoreryo1
     */
    public Integer getStaoreryo1() {
        return staoreryo1;
    }

    /**
     * 積層ﾀｵﾚ量1
     * @param staoreryo1 セットする staoreryo1
     */
    public void setStaoreryo1(Integer staoreryo1) {
        this.staoreryo1 = staoreryo1;
    }

    /**
     * 積層ﾀｵﾚ量2
     * @return staoreryo2
     */
    public Integer getStaoreryo2() {
        return staoreryo2;
    }

    /**
     * 積層ﾀｵﾚ量2
     * @param staoreryo2 セットする staoreryo2
     */
    public void setStaoreryo2(Integer staoreryo2) {
        this.staoreryo2 = staoreryo2;
    }

    /**
     * 積層ﾀｵﾚ量3
     * @return staoreryo3
     */
    public Integer getStaoreryo3() {
        return staoreryo3;
    }

    /**
     * 積層ﾀｵﾚ量3
     * @param staoreryo3 セットする staoreryo3
     */
    public void setStaoreryo3(Integer staoreryo3) {
        this.staoreryo3 = staoreryo3;
    }

    /**
     * 積層ﾀｵﾚ量4
     * @return staoreryo4
     */
    public Integer getStaoreryo4() {
        return staoreryo4;
    }

    /**
     * 積層ﾀｵﾚ量4
     * @param staoreryo4 セットする staoreryo4
     */
    public void setStaoreryo4(Integer staoreryo4) {
        this.staoreryo4 = staoreryo4;
    }

    /**
     * 積層後T寸法AVE
     * @return stsunAve
     */
    public Integer getStsunAve() {
        return stsunAve;
    }

    /**
     * 積層後T寸法AVE
     * @param stsunAve セットする stsunAve
     */
    public void setStsunAve(Integer stsunAve) {
        this.stsunAve = stsunAve;
    }

    /**
     * 積層後T寸法MAX
     * @return stsunMax
     */
    public Integer getStsunMax() {
        return stsunMax;
    }

    /**
     * 積層後T寸法MAX
     * @param stsunMax セットする stsunMax
     */
    public void setStsunMax(Integer stsunMax) {
        this.stsunMax = stsunMax;
    }

    /**
     * 積層後T寸法MIN
     * @return stsunMin
     */
    public Integer getStsunMin() {
        return stsunMin;
    }

    /**
     * 積層後T寸法MIN
     * @param stsunMin セットする stsunMin
     */
    public void setStsunMin(Integer stsunMin) {
        this.stsunMin = stsunMin;
    }

    /**
     * 積層後T寸法σ
     * @return stsunSiguma
     */
    public BigDecimal getStsunSiguma() {
        return stsunSiguma;
    }

    /**
     * 積層後T寸法σ
     * @param stsunSiguma セットする stsunSiguma
     */
    public void setStsunSiguma(BigDecimal stsunSiguma) {
        this.stsunSiguma = stsunSiguma;
    }

    /**
     * 隔離NG回数
     * @return hngKaisuu
     */
    public Integer getHngKaisuu() {
        return hngKaisuu;
    }

    /**
     * 隔離NG回数
     * @param hngKaisuu セットする hngKaisuu
     */
    public void setHngKaisuu(Integer hngKaisuu) {
        this.hngKaisuu = hngKaisuu;
    }

    /**
     * 最終外観担当者ｺｰﾄﾞ
     * @return gaikanTantou
     */
    public String getGaikanTantou() {
        return gaikanTantou;
    }

    /**
     * 最終外観担当者ｺｰﾄﾞ
     * @param gaikanTantou セットする gaikanTantou
     */
    public void setGaikanTantou(String gaikanTantou) {
        this.gaikanTantou = gaikanTantou;
    }

    /**
     * 中間ﾌﾟﾚｽ回数
     * @return cpressKaisuu
     */
    public Integer getCpressKaisuu() {
        return cpressKaisuu;
    }

    /**
     * 中間ﾌﾟﾚｽ回数
     * @param cpressKaisuu セットする cpressKaisuu
     */
    public void setCpressKaisuu(Integer cpressKaisuu) {
        this.cpressKaisuu = cpressKaisuu;
    }

    /**
     * 剥離NG回数AVE
     * @return hngKaisuuAve
     */
    public BigDecimal getHngKaisuuAve() {
        return hngKaisuuAve;
    }

    /**
     * 剥離NG回数AVE
     * @param hngKaisuuAve セットする hngKaisuuAve
     */
    public void setHngKaisuuAve(BigDecimal hngKaisuuAve) {
        this.hngKaisuuAve = hngKaisuuAve;
    }

    /**
     * 画像NG回数
     * @return gngKaisuu
     */
    public Integer getGngKaisuu() {
        return gngKaisuu;
    }

    /**
     * 画像NG回数
     * @param gngKaisuu セットする gngKaisuu
     */
    public void setGngKaisuu(Integer gngKaisuu) {
        this.gngKaisuu = gngKaisuu;
    }

    /**
     * 画像NG回数AVE
     * @return gngKaisuuAve
     */
    public BigDecimal getGngKaisuuAve() {
        return gngKaisuuAve;
    }

    /**
     * 画像NG回数AVE
     * @param gngKaisuuAve セットする gngKaisuuAve
     */
    public void setGngKaisuuAve(BigDecimal gngKaisuuAve) {
        this.gngKaisuuAve = gngKaisuuAve;
    }

    /**
     * ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
     * @return tapelotno
     */
    public String getTapelotno() {
        return tapelotno;
    }

    /**
     * ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
     * @param tapelotno セットする tapelotno
     */
    public void setTapelotno(String tapelotno) {
        this.tapelotno = tapelotno;
    }

    /**
     * ﾃｰﾌﾟﾛｰﾙNo1
     * @return taperollno1
     */
    public String getTaperollno1() {
        return taperollno1;
    }

    /**
     * ﾃｰﾌﾟﾛｰﾙNo1
     * @param taperollno1 セットする taperollno1
     */
    public void setTaperollno1(String taperollno1) {
        this.taperollno1 = taperollno1;
    }

    /**
     * ﾃｰﾌﾟﾛｰﾙNo2
     * @return taperollno2
     */
    public String getTaperollno2() {
        return taperollno2;
    }

    /**
     * ﾃｰﾌﾟﾛｰﾙNo2
     * @param taperollno2 セットする taperollno2
     */
    public void setTaperollno2(String taperollno2) {
        this.taperollno2 = taperollno2;
    }

    /**
     * ﾃｰﾌﾟﾛｰﾙNo3
     * @return taperollno3
     */
    public String getTaperollno3() {
        return taperollno3;
    }

    /**
     * ﾃｰﾌﾟﾛｰﾙNo3
     * @param taperollno3 セットする taperollno3
     */
    public void setTaperollno3(String taperollno3) {
        this.taperollno3 = taperollno3;
    }

    /**
     * 原料記号
     * @return genryoukigou
     */
    public String getGenryoukigou() {
        return genryoukigou;
    }

    /**
     * 原料記号
     * @param genryoukigou セットする genryoukigou
     */
    public void setGenryoukigou(String genryoukigou) {
        this.genryoukigou = genryoukigou;
    }

    /**
     * PETﾌｨﾙﾑ種類
     * @return petfilmsyurui
     */
    public String getPetfilmsyurui() {
        return petfilmsyurui;
    }

    /**
     * PETﾌｨﾙﾑ種類
     * @param petfilmsyurui セットする petfilmsyurui
     */
    public void setPetfilmsyurui(String petfilmsyurui) {
        this.petfilmsyurui = petfilmsyurui;
    }

    /**
     * 固着ｼｰﾄ
     * @return kotyakusheet
     */
    public String getKotyakusheet() {
        return kotyakusheet;
    }

    /**
     * 固着ｼｰﾄ
     * @param kotyakusheet セットする kotyakusheet
     */
    public void setKotyakusheet(String kotyakusheet) {
        this.kotyakusheet = kotyakusheet;
    }

    /**
     * 下端子号機
     * @return shitaTanshigouki
     */
    public String getShitaTanshigouki() {
        return shitaTanshigouki;
    }

    /**
     * 下端子号機
     * @param shitaTanshigouki セットする shitaTanshigouki
     */
    public void setShitaTanshigouki(String shitaTanshigouki) {
        this.shitaTanshigouki = shitaTanshigouki;
    }

    /**
     * 上端子号機
     * @return uwaTanshigouki
     */
    public String getUwaTanshigouki() {
        return uwaTanshigouki;
    }

    /**
     * 上端子号機
     * @param uwaTanshigouki セットする uwaTanshigouki
     */
    public void setUwaTanshigouki(String uwaTanshigouki) {
        this.uwaTanshigouki = uwaTanshigouki;
    }

    /**
     * 下端子ﾌﾞｸ抜き
     * @return shitaTanshiBukunuki
     */
    public String getShitaTanshiBukunuki() {
        return shitaTanshiBukunuki;
    }

    /**
     * 下端子ﾌﾞｸ抜き
     * @param shitaTanshiBukunuki セットする shitaTanshiBukunuki
     */
    public void setShitaTanshiBukunuki(String shitaTanshiBukunuki) {
        this.shitaTanshiBukunuki = shitaTanshiBukunuki;
    }

    /**
     * 下端子
     * @return shitaTanshi
     */
    public String getShitaTanshi() {
        return shitaTanshi;
    }

    /**
     * 下端子
     * @param shitaTanshi セットする shitaTanshi
     */
    public void setShitaTanshi(String shitaTanshi) {
        this.shitaTanshi = shitaTanshi;
    }

    /**
     * 剥離吸引圧
     * @return hakuriKyuin
     */
    public Integer getHakuriKyuin() {
        return hakuriKyuin;
    }

    /**
     * 剥離吸引圧
     * @param hakuriKyuin セットする hakuriKyuin
     */
    public void setHakuriKyuin(Integer hakuriKyuin) {
        this.hakuriKyuin = hakuriKyuin;
    }

    /**
     * 剥離ｸﾘｱﾗﾝｽ
     * @return hakuriClearrance
     */
    public BigDecimal getHakuriClearrance() {
        return hakuriClearrance;
    }

    /**
     * 剥離ｸﾘｱﾗﾝｽ
     * @param hakuriClearrance セットする hakuriClearrance
     */
    public void setHakuriClearrance(BigDecimal hakuriClearrance) {
        this.hakuriClearrance = hakuriClearrance;
    }

    /**
     * 剥離ｶｯﾄ速度
     * @return hakuriCutSpeed
     */
    public Integer getHakuriCutSpeed() {
        return hakuriCutSpeed;
    }

    /**
     * 剥離ｶｯﾄ速度
     * @param hakuriCutSpeed セットする hakuriCutSpeed
     */
    public void setHakuriCutSpeed(Integer hakuriCutSpeed) {
        this.hakuriCutSpeed = hakuriCutSpeed;
    }

    /**
     * 下ﾊﾟﾝﾁ温度
     * @return shitaPanchiOndo
     */
    public BigDecimal getShitaPanchiOndo() {
        return shitaPanchiOndo;
    }

    /**
     * 下ﾊﾟﾝﾁ温度
     * @param shitaPanchiOndo セットする shitaPanchiOndo
     */
    public void setShitaPanchiOndo(BigDecimal shitaPanchiOndo) {
        this.shitaPanchiOndo = shitaPanchiOndo;
    }

    /**
     * 上ﾊﾟﾝﾁ温度
     * @return uwaPanchiOndo
     */
    public BigDecimal getUwaPanchiOndo() {
        return uwaPanchiOndo;
    }

    /**
     * 上ﾊﾟﾝﾁ温度
     * @param uwaPanchiOndo セットする uwaPanchiOndo
     */
    public void setUwaPanchiOndo(BigDecimal uwaPanchiOndo) {
        this.uwaPanchiOndo = uwaPanchiOndo;
    }

    /**
     * 加圧時間
     * @return kaatuJikan
     */
    public BigDecimal getKaatuJikan() {
        return kaatuJikan;
    }

    /**
     * 加圧時間
     * @param kaatuJikan セットする kaatuJikan
     */
    public void setKaatuJikan(BigDecimal kaatuJikan) {
        this.kaatuJikan = kaatuJikan;
    }

    /**
     * 加圧圧力
     * @return kaatuAturyoku
     */
    public Integer getKaatuAturyoku() {
        return kaatuAturyoku;
    }

    /**
     * 加圧圧力
     * @param kaatuAturyoku セットする kaatuAturyoku
     */
    public void setKaatuAturyoku(Integer kaatuAturyoku) {
        this.kaatuAturyoku = kaatuAturyoku;
    }

    /**
     * 上端子
     * @return uwaTanshi
     */
    public String getUwaTanshi() {
        return uwaTanshi;
    }

    /**
     * 上端子
     * @param uwaTanshi セットする uwaTanshi
     */
    public void setUwaTanshi(String uwaTanshi) {
        this.uwaTanshi = uwaTanshi;
    }

    /**
     * 処理ｾｯﾄ数
     * @return syoriSetsuu
     */
    public Integer getSyoriSetsuu() {
        return syoriSetsuu;
    }

    /**
     * 処理ｾｯﾄ数
     * @param syoriSetsuu セットする syoriSetsuu
     */
    public void setSyoriSetsuu(Integer syoriSetsuu) {
        this.syoriSetsuu = syoriSetsuu;
    }

    /**
     * 良品ｾｯﾄ数
     * @return ryouhinSetsuu
     */
    public BigDecimal getRyouhinSetsuu() {
        return ryouhinSetsuu;
    }

    /**
     * 良品ｾｯﾄ数
     * @param ryouhinSetsuu セットする ryouhinSetsuu
     */
    public void setRyouhinSetsuu(BigDecimal ryouhinSetsuu) {
        this.ryouhinSetsuu = ryouhinSetsuu;
    }

    /**
     * 開始担当者ｺｰﾄﾞ
     * @return startTantosyacode
     */
    public String getStartTantosyacode() {
        return startTantosyacode;
    }

    /**
     * 開始担当者ｺｰﾄﾞ
     * @param startTantosyacode セットする startTantosyacode
     */
    public void setStartTantosyacode(String startTantosyacode) {
        this.startTantosyacode = startTantosyacode;
    }

    /**
     * 終了担当者ｺｰﾄﾞ
     * @return endTantousyacode
     */
    public String getEndTantousyacode() {
        return endTantousyacode;
    }

    /**
     * 終了担当者ｺｰﾄﾞ
     * @param endTantousyacode セットする endTantousyacode
     */
    public void setEndTantousyacode(String endTantousyacode) {
        this.endTantousyacode = endTantousyacode;
    }

    /**
     * 端子ﾃｰﾌﾟ種類
     * @return tanshiTapeSyurui
     */
    public Integer getTanshiTapeSyurui() {
        return tanshiTapeSyurui;
    }

    /**
     * 端子ﾃｰﾌﾟ種類
     * @param tanshiTapeSyurui セットする tanshiTapeSyurui
     */
    public void setTanshiTapeSyurui(Integer tanshiTapeSyurui) {
        this.tanshiTapeSyurui = tanshiTapeSyurui;
    }

    /**
     * 登録日時
     * @return torokunichiji
     */
    public Timestamp getTorokunichiji() {
        return torokunichiji;
    }

    /**
     * 登録日時
     * @param torokunichiji セットする torokunichiji
     */
    public void setTorokunichiji(Timestamp torokunichiji) {
        this.torokunichiji = torokunichiji;
    }

    /**
     * 更新日時
     * @return kosinnichiji
     */
    public Timestamp getKosinnichiji() {
        return kosinnichiji;
    }

    /**
     * 更新日時
     * @param kosinnichiji セットする kosinnichiji
     */
    public void setKosinnichiji(Timestamp kosinnichiji) {
        this.kosinnichiji = kosinnichiji;
    }
    
    /**
     * 空打ち
     * @return karauti
     */
    public String getKarauti() {
        return karauti;
    }

    /**
     * 空打ち
     * @param karauti セットする karauti
     */
    public void setKarauti(String karauti) {
        this.karauti = karauti;
    }

    /**
     * 空打ち秒
     * @return karautibyou
     */
    public Integer getKarautibyou() {
        return karautibyou;
    }

    /**
     * 空打ち秒
     * @param karautibyou セットする karautibyou
     */
    public void setKarautibyou(Integer karautibyou) {
        this.karautibyou = karautibyou;
    }

    /**
     * 空打ち回
     * @return karautikai
     */
    public Integer getKarautikai() {
        return karautikai;
    }

    /**
     * 空打ち回
     * @param karautikai セットする karautikai
     */
    public void setKarautikai(Integer karautikai) {
        this.karautikai = karautikai;
    }

    /**
     * ｽﾞﾚ値
     * @return zureti
     */
    public Integer getZureti() {
        return zureti;
    }

    /**
     * ｽﾞﾚ値
     * @param zureti セットする zureti
     */
    public void setZureti(Integer zureti) {
        this.zureti = zureti;
    }

    /**
     * 外観確認5
     * @return gaikanKakunin5
     */
    public String getGaikanKakunin5() {
        return gaikanKakunin5;
    }

    /**
     * 外観確認5
     * @param gaikanKakunin5 セットする gaikanKakunin5
     */
    public void setGaikanKakunin5(String gaikanKakunin5) {
        this.gaikanKakunin5 = gaikanKakunin5;
    }

    /**
     * revision
     * @return revision
     */
    public Integer getRevision() {
        return revision;
    }

    /**
     * revision
     * @param revision セットする revision
     */
    public void setRevision(Integer revision) {
        this.revision = revision;
    }
    
    /**
     * 削除ﾌﾗｸﾞ
     * @return deleteflag
     */
    public Integer getDeleteflag() {
        return deleteflag;
    }

    /**
     * 削除ﾌﾗｸﾞ
     * @param deleteflag セットする deleteflag
     */
    public void setDeleteflag(Integer deleteflag) {
        this.deleteflag = deleteflag;
    }
}
