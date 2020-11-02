/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2019/08/07<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	863 F.Zhang<br>
 * 変更理由	新規作成<br>
 * <br>
 * <br>
 * 変更日	2020/09/22<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	863 sujialiang<br>
 * 変更理由	項目追加・変更<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * ｶｯﾄ・押切ｶｯﾄ履歴検索画面のモデルクラスです。
 *
 * @author 863 F.Zhang
 * @since 2019/08/07
 */
public class GXHDO201B010Model implements Serializable{
    
    /** ﾛｯﾄNo. */
    private String lotno = "";
    /** KCPNO */
    private String kcpno = "";
    /** 開始日時 */
    private Timestamp kaisinichiji = null;
    /** 終了日時 */
    private Timestamp syuryonichiji = null;
    /** ｶｯﾄ刃使用数(枚) */
    private Integer cutbamaisuu = null;
    /** 号機 */
    private String goki = "";
    /** ｶｯﾄﾃｰﾌﾞﾙ温度 */
    private String cuttablenodo = "";
    /** 開始担当者 */
    private String kaisiTantosya ="";
    /** 開始確認者 */
    private String kaisiKakusya = "";
    /** 終了担当者 */
    private String syuryoTantosya = "";
    /** B担当者 */
    private String bTantosya = "";
    /** A担当者 */
    private String aTantosya = "";
    /** 受入個数 */
    private Integer ukeirekoSuu = null;
    /** 良品個数 */
    private Integer ryohinKosuu = null;
    /** 厚み01 */
    private Integer atumi01 = null;
    /** 厚み02 */
    private Integer atumi02 = null;
    /** 厚み03 */
    private Integer atumi03 = null;
    /** 厚み04 */
    private Integer atumi04 = null;
    /** 厚み05 */
    private Integer atumi05 = null;
    /** 厚み06 */
    private Integer atumi06 = null;
    /** 厚み07 */
    private Integer atumi07 = null;
    /** 厚み08 */
    private Integer atumi08 = null;
    /** 厚み09 */
    private Integer atumi09 = null;
    /** 厚み10 */
    private Integer atumi10 = null;
    /** 厚みMIN */
    private Integer atumiMin = null;
    /** 厚みMAX */
    private Integer atumiMax = null;
    /** 備考1 */
    private String biko1 = "";
    /** 備考2 */
    private String biko2 = "";
    /** 備考3 */
    private String biko3 = "";
    /** 備考4 */
    private String biko4 = "";
    /** 転写者 */
    private String tensyasya = "";
    /** ﾆｼﾞﾐ回数 */
    private Integer nijimicnt = null;
    /** 総重量 */
    private BigDecimal soujyuryo = null;
    /** 単位重量 */
    private BigDecimal tanijyuryo = null;
    /** ｶｯﾄ刃種類確認 */
    private String cutbashuruicheck = "";
    /** ｶｯﾄ刃直進度左(μm) */
    private Integer cutbachiokushindo = null;
    /** ｶｯﾄ刃直進度右(μm) */
    private Integer cutbachokushindomigi = null;
    /** ｶｯﾄ刃使用回数ST1(回) */
    private Integer sutbasiyoukaisuust1 = null;
    /** ｶｯﾄ刃ST1使用ロットNo */
    private Integer station1lotno = null;
    /** ｶｯﾄ刃使用回数ST2(回) */
    private Integer sutbasiyoukaisuust2 = null;
    /** ｶｯﾄ刃ST2使用ロットNo */
    private Integer station2lotno = null;
    /** ﾌﾟﾛｸﾞﾗﾑ名 */
    private String programmei = "";
    /** 行×列確認 */
    private String gyoretuKakunin = "";
    /** ﾏｰｸ外取り数 */
    private String marktorisuu = "";
    /** ｶｯﾄ補正量(μm) */
    private Integer cuthoSeiryou = null;
    /** ﾃｰﾌﾞﾙ温度 設定 左(℃) */
    private BigDecimal tableondosethidari = null;
    /** ﾃｰﾌﾞﾙ温度 設定 中(℃) */
    private BigDecimal tableondosetnaka = null;
    /** ﾃｰﾌﾞﾙ温度 設定 右(℃) */
    private BigDecimal tableondosetmigi = null;
    /** ﾃｰﾌﾞﾙ温度 実測 左(℃) */
    private BigDecimal tableondosokuhidari = null;
    /** ﾃｰﾌﾞﾙ温度 実測 中(℃) */
    private BigDecimal tableondosokunaka = null;
    /** ﾃｰﾌﾞﾙ温度 実測 右(℃) */
    private BigDecimal tableondosokumigi = null;
    /** 第2ﾃｰﾌﾞﾙ温度 設定 左(℃) */
    private BigDecimal dai2tableondosethidari = null;
    /** 第2ﾃｰﾌﾞﾙ温度 設定 中(℃) */
    private BigDecimal dai2tableondosetnaka = null;
    /** 第2ﾃｰﾌﾞﾙ温度 設定 右(℃) */
    private BigDecimal dai2tableondosetmigi = null;
    /** 第2ﾃｰﾌﾞﾙ温度 実測 左(℃) */
    private BigDecimal dai2tableondosokuhidari = null;
    /** 第2ﾃｰﾌﾞﾙ温度 実測 中(℃) */
    private BigDecimal dai2tableondosokunaka = null;
    /** 第2ﾃｰﾌﾞﾙ温度 実測 右(℃) */
    private BigDecimal dai2tableondosokumigi = null;
    /** 外観確認 */
    private String gaikanCheck = "";
    /** 刃高さNG(SET) */
    private Integer hatakasang = null;
    /** ｶｯﾄ後剥がし温度(℃) */
    private BigDecimal ondo = null;
    /** 処理ｾｯﾄ数(SET) */
    private Integer syoriSetsuu = null;
    /** 良品ｾｯﾄ数(SET) */
    private BigDecimal ryouhinSetsuu = null;
    /** 作業場所 */
    private String sagyouBasyo = "";
    
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
     * 終了日時
     * @return the syuryonichiji
     */
    public Timestamp getSyuryonichiji() {
        return syuryonichiji;
    }

    /**
     * 終了日時
     * @param syuryonichiji the syuryonichiji to set
     */
    public void setSyuryonichiji(Timestamp syuryonichiji) {
        this.syuryonichiji = syuryonichiji;
    }

    /**
     * 備考1
     * @return the biko1
     */
    public String getBiko1() {
        return biko1;
    }

    /**
     * 備考1
     * @param biko1 the biko1 to set
     */
    public void setBiko1(String biko1) {
        this.biko1 = biko1;
    }

    /**
     * 備考2
     * @return the biko2
     */
    public String getBiko2() {
        return biko2;
    }

    /**
     * 備考2
     * @param biko2 the biko2 to set
     */
    public void setBiko2(String biko2) {
        this.biko2 = biko2;
    }

    /**
     * 開始担当者
     * @return the kaisiTantosya
     */
    public String getKaisiTantosya() {
        return kaisiTantosya;
    }

    /**
     * 開始担当者
     * @param kaisiTantosya the kaisiTantosya to set
     */
    public void setKaisiTantosya(String kaisiTantosya) {
        this.kaisiTantosya = kaisiTantosya;
    }

    /**
     * 開始確認者
     * @return the kaisiKakusya
     */
    public String getKaisiKakusya() {
        return kaisiKakusya;
    }

    /**
     * 開始確認者
     * @param kaisiKakusya the kaisiKakusya to set
     */
    public void setKaisiKakusya(String kaisiKakusya) {
        this.kaisiKakusya = kaisiKakusya;
    }

    /**
     * 終了担当者
     * @return the syuryoTantosya
     */
    public String getSyuryoTantosya() {
        return syuryoTantosya;
    }

    /**
     * 終了担当者
     * @param syuryoTantosya the syuryoTantosya to set
     */
    public void setSyuryoTantosya(String syuryoTantosya) {
        this.syuryoTantosya = syuryoTantosya;
    }

    /**
     * ｶｯﾄ刃使用数(枚)
     * @return the cutbamaisuu
     */
    public Integer getCutbamaisuu() {
        return cutbamaisuu;
    }

    /**
     * ｶｯﾄ刃使用数(枚)
     * @param cutbamaisuu the cutbamaisuu to set
     */
    public void setCutbamaisuu(Integer cutbamaisuu) {
        this.cutbamaisuu = cutbamaisuu;
    }

    /**
     * 号機
     * @return the goki
     */
    public String getGoki() {
        return goki;
    }

    /**
     * 号機
     * @param goki the goki to set
     */
    public void setGoki(String goki) {
        this.goki = goki;
    }

    /**
     * ｶｯﾄﾃｰﾌﾞﾙ温度
     * @return the cuttablenodo
     */
    public String getCuttablenodo() {
        return cuttablenodo;
    }

    /**
     * ｶｯﾄﾃｰﾌﾞﾙ温度
     * @param cuttablenodo the cuttablenodo to set
     */
    public void setCuttablenodo(String cuttablenodo) {
        this.cuttablenodo = cuttablenodo;
    }

    /**
     * B担当者
     * @return the bTantosya
     */
    public String getbTantosya() {
        return bTantosya;
    }

    /**
     * B担当者
     * @param bTantosya the bTantosya to set
     */
    public void setbTantosya(String bTantosya) {
        this.bTantosya = bTantosya;
    }

    /**
     * A担当者
     * @return the aTantosya
     */
    public String getaTantosya() {
        return aTantosya;
    }

    /**
     * A担当者
     * @param aTantosya the aTantosya to set
     */
    public void setaTantosya(String aTantosya) {
        this.aTantosya = aTantosya;
    }

    /**
     * 受入個数
     * @return the ukeirekoSuu
     */
    public Integer getUkeirekoSuu() {
        return ukeirekoSuu;
    }

    /**
     * 受入個数
     * @param ukeirekoSuu the ukeirekoSuu to set
     */
    public void setUkeirekoSuu(Integer ukeirekoSuu) {
        this.ukeirekoSuu = ukeirekoSuu;
    }

    /**
     * 良品個数
     * @return the ryohinKosuu
     */
    public Integer getRyohinKosuu() {
        return ryohinKosuu;
    }

    /**
     * 良品個数
     * @param ryohinKosuu the ryohinKosuu to set
     */
    public void setRyohinKosuu(Integer ryohinKosuu) {
        this.ryohinKosuu = ryohinKosuu;
    }

    /**
     * 厚み01
     * @return the atumi01
     */
    public Integer getAtumi01() {
        return atumi01;
    }

    /**
     * 厚み01
     * @param atumi01 the atumi01 to set
     */
    public void setAtumi01(Integer atumi01) {
        this.atumi01 = atumi01;
    }

    /**
     * 厚み02
     * @return the atumi02
     */
    public Integer getAtumi02() {
        return atumi02;
    }

    /**
     * 厚み02
     * @param atumi02 the atumi02 to set
     */
    public void setAtumi02(Integer atumi02) {
        this.atumi02 = atumi02;
    }

    /**
     * 厚み03
     * @return the atumi03
     */
    public Integer getAtumi03() {
        return atumi03;
    }

    /**
     * 厚み03
     * @param atumi03 the atumi03 to set
     */
    public void setAtumi03(Integer atumi03) {
        this.atumi03 = atumi03;
    }

    /**
     * 厚み04
     * @return the atumi04
     */
    public Integer getAtumi04() {
        return atumi04;
    }

    /**
     * 厚み04
     * @param atumi04 the atumi04 to set
     */
    public void setAtumi04(Integer atumi04) {
        this.atumi04 = atumi04;
    }

    /**
     * 厚み05
     * @return the atumi05
     */
    public Integer getAtumi05() {
        return atumi05;
    }

    /**
     * 厚み05
     * @param atumi05 the atumi05 to set
     */
    public void setAtumi05(Integer atumi05) {
        this.atumi05 = atumi05;
    }

    /**
     * 厚み06
     * @return the atumi06
     */
    public Integer getAtumi06() {
        return atumi06;
    }

    /**
     * 厚み06
     * @param atumi06 the atumi06 to set
     */
    public void setAtumi06(Integer atumi06) {
        this.atumi06 = atumi06;
    }

    /**
     * 厚み07
     * @return the atumi07
     */
    public Integer getAtumi07() {
        return atumi07;
    }

    /**
     * 厚み07
     * @param atumi07 the atumi07 to set
     */
    public void setAtumi07(Integer atumi07) {
        this.atumi07 = atumi07;
    }

    /**
     * 厚み08
     * @return the atumi08
     */
    public Integer getAtumi08() {
        return atumi08;
    }

    /**
     * 厚み08
     * @param atumi08 the atumi08 to set
     */
    public void setAtumi08(Integer atumi08) {
        this.atumi08 = atumi08;
    }

    /**
     * 厚み09
     * @return the atumi09
     */
    public Integer getAtumi09() {
        return atumi09;
    }

    /**
     * 厚み09
     * @param atumi09 the atumi09 to set
     */
    public void setAtumi09(Integer atumi09) {
        this.atumi09 = atumi09;
    }

    /**
     * 厚み10
     * @return the atumi10
     */
    public Integer getAtumi10() {
        return atumi10;
    }

    /**
     * 厚み10
     * @param atumi10 the atumi10 to set
     */
    public void setAtumi10(Integer atumi10) {
        this.atumi10 = atumi10;
    }

    /**
     * 厚みMIN
     * @return the atumiMin
     */
    public Integer getAtumiMin() {
        return atumiMin;
    }

    /**
     * 厚みMIN
     * @param atumiMin the atumiMin to set
     */
    public void setAtumiMin(Integer atumiMin) {
        this.atumiMin = atumiMin;
    }

    /**
     * 厚みMAX
     * @return the atumiMax
     */
    public Integer getAtumiMax() {
        return atumiMax;
    }

    /**
     * 厚みMAX
     * @param atumiMax the atumiMax to set
     */
    public void setAtumiMax(Integer atumiMax) {
        this.atumiMax = atumiMax;
    }

    /**
     * 備考3
     * @return the biko3
     */
    public String getBiko3() {
        return biko3;
    }

    /**
     * 備考3
     * @param biko3 the biko3 to set
     */
    public void setBiko3(String biko3) {
        this.biko3 = biko3;
    }

    /**
     * 備考4
     * @return the biko4
     */
    public String getBiko4() {
        return biko4;
    }

    /**
     * 備考4
     * @param biko4 the biko4 to set
     */
    public void setBiko4(String biko4) {
        this.biko4 = biko4;
    }

    /**
     * 転写者
     * @return the tensyasya
     */
    public String getTensyasya() {
        return tensyasya;
    }

    /**
     * 転写者
     * @param tensyasya the tensyasya to set
     */
    public void setTensyasya(String tensyasya) {
        this.tensyasya = tensyasya;
    }

    /**
     * ﾆｼﾞﾐ回数
     * @return the nijimicnt
     */
    public Integer getNijimicnt() {
        return nijimicnt;
    }

    /**
     * ﾆｼﾞﾐ回数
     * @param nijimicnt the nijimicnt to set
     */
    public void setNijimicnt(Integer nijimicnt) {
        this.nijimicnt = nijimicnt;
    }

    /**
     * 総重量
     * @return the soujyuryo
     */
    public BigDecimal getSoujyuryo() {
        return soujyuryo;
    }

    /**
     * 総重量
     * @param soujyuryo the soujyuryo to set
     */
    public void setSoujyuryo(BigDecimal soujyuryo) {
        this.soujyuryo = soujyuryo;
    }

    /**
     * 単位重量
     * @return the tanijyuryo
     */
    public BigDecimal getTanijyuryo() {
        return tanijyuryo;
    }

    /**
     * 単位重量
     * @param tanijyuryo the tanijyuryo to set
     */
    public void setTanijyuryo(BigDecimal tanijyuryo) {
        this.tanijyuryo = tanijyuryo;
    }

    /**
     * ｶｯﾄ刃種類確認
     * @return the cutbashuruicheck
     */
    public String getCutbashuruicheck() {
        return cutbashuruicheck;
    }

    /**
     * ｶｯﾄ刃種類確認
     * @param cutbashuruicheck the cutbashuruicheck to set
     */
    public void setCutbashuruicheck(String cutbashuruicheck) {
        this.cutbashuruicheck = cutbashuruicheck;
    }

    /**
     * ｶｯﾄ刃直進度左(μm)
     * @return the cutbachiokushindo
     */
    public Integer getCutbachiokushindo() {
        return cutbachiokushindo;
    }

    /**
     * ｶｯﾄ刃直進度左(μm)
     * @param cutbachiokushindo the cutbachiokushindo to set
     */
    public void setCutbachiokushindo(Integer cutbachiokushindo) {
        this.cutbachiokushindo = cutbachiokushindo;
    }

    /**
     * ｶｯﾄ刃直進度右(μm)
     * @return cutbachokushindomigi
     */
    public Integer getCutbachokushindomigi() {
        return cutbachokushindomigi;
    }

    /**
     * ｶｯﾄ刃直進度右(μm)
     * @param cutbachokushindomigi 
     */
    public void setCutbachokushindomigi(Integer cutbachokushindomigi) {
        this.cutbachokushindomigi = cutbachokushindomigi;
    }

    /**
     * ｶｯﾄ刃使用回数ST1(回)
     * @return the sutbasiyoukaisuust1
     */
    public Integer getSutbasiyoukaisuust1() {
        return sutbasiyoukaisuust1;
    }

    /**
     * ｶｯﾄ刃使用回数ST1(回)
     * @param sutbasiyoukaisuust1 the sutbasiyoukaisuust1 to set
     */
    public void setSutbasiyoukaisuust1(Integer sutbasiyoukaisuust1) {
        this.sutbasiyoukaisuust1 = sutbasiyoukaisuust1;
    }

    /**
     * ｶｯﾄ刃ST1使用ロットNo
     * @return station1lotno
     */
    public Integer getStation1lotno() {
        return station1lotno;
    }

    /**
     * ｶｯﾄ刃ST1使用ロットNo
     * @param station1lotno 
     */
    public void setStation1lotno(Integer station1lotno) {
        this.station1lotno = station1lotno;
    }

    /**
     * ｶｯﾄ刃使用回数ST2(回)
     * @return the sutbasiyoukaisuust2
     */
    public Integer getSutbasiyoukaisuust2() {
        return sutbasiyoukaisuust2;
    }

    /**
     * ｶｯﾄ刃使用回数ST2(回)
     * @param sutbasiyoukaisuust2 the sutbasiyoukaisuust2 to set
     */
    public void setSutbasiyoukaisuust2(Integer sutbasiyoukaisuust2) {
        this.sutbasiyoukaisuust2 = sutbasiyoukaisuust2;
    }

    /**
     * ｶｯﾄ刃ST2使用ロットNo
     * @return station2lotno
     */
    public Integer getStation2lotno() {
        return station2lotno;
    }

    /**
     * ｶｯﾄ刃ST2使用ロットNo
     * @param station2lotno 
     */
    public void setStation2lotno(Integer station2lotno) {
        this.station2lotno = station2lotno;
    }

    /**
     * ﾌﾟﾛｸﾞﾗﾑ名
     * @return the programmei
     */
    public String getProgrammei() {
        return programmei;
    }

    /**
     * ﾌﾟﾛｸﾞﾗﾑ名
     * @param programmei the programmei to set
     */
    public void setProgrammei(String programmei) {
        this.programmei = programmei;
    }

    /**
     * 行×列確認
     * @return the gyoretuKakunin
     */
    public String getGyoretuKakunin() {
        return gyoretuKakunin;
    }

    /**
     * 行×列確認
     * @param gyoretuKakunin the gyoretuKakunin to set
     */
    public void setGyoretuKakunin(String gyoretuKakunin) {
        this.gyoretuKakunin = gyoretuKakunin;
    }

    /**
     * ﾏｰｸ外取り数
     * @return the marktorisuu
     */
    public String getMarktorisuu() {
        return marktorisuu;
    }

    /**
     * ﾏｰｸ外取り数
     * @param marktorisuu the marktorisuu to set
     */
    public void setMarktorisuu(String marktorisuu) {
        this.marktorisuu = marktorisuu;
    }

    /**
     * ｶｯﾄ補正量(μm)
     * @return the cuthoSeiryou
     */
    public Integer getCuthoSeiryou() {
        return cuthoSeiryou;
    }

    /**
     * ｶｯﾄ補正量(μm)
     * @param cuthoSeiryou the cuthoSeiryou to set
     */
    public void setCuthoSeiryou(Integer cuthoSeiryou) {
        this.cuthoSeiryou = cuthoSeiryou;
    }

    /**
     * ﾃｰﾌﾞﾙ温度 設定 左(℃)
     * @return tableondosethidari
     */
    public BigDecimal getTableondosethidari() {
        return tableondosethidari;
    }

    /**
     * ﾃｰﾌﾞﾙ温度 設定 左(℃)
     * @param tableondosethidari 
     */
    public void setTableondosethidari(BigDecimal tableondosethidari) {
        this.tableondosethidari = tableondosethidari;
    }

    /**
     * ﾃｰﾌﾞﾙ温度 設定 中(℃)
     * @return tableondosetnaka
     */
    public BigDecimal getTableondosetnaka() {
        return tableondosetnaka;
    }

    /**
     * ﾃｰﾌﾞﾙ温度 設定 中(℃)
     * @param tableondosetnaka 
     */
    public void setTableondosetnaka(BigDecimal tableondosetnaka) {
        this.tableondosetnaka = tableondosetnaka;
    }

    /**
     * ﾃｰﾌﾞﾙ温度 設定 右(℃)
     * @return tableondosetmigi
     */
    public BigDecimal getTableondosetmigi() {
        return tableondosetmigi;
    }

    /**
     * ﾃｰﾌﾞﾙ温度 設定 右(℃)
     * @param tableondosetmigi 
     */
    public void setTableondosetmigi(BigDecimal tableondosetmigi) {
        this.tableondosetmigi = tableondosetmigi;
    }

    /**
     * ﾃｰﾌﾞﾙ温度 実測 左(℃)
     * @return tableondosokuhidari
     */
    public BigDecimal getTableondosokuhidari() {
        return tableondosokuhidari;
    }

    /**
     * ﾃｰﾌﾞﾙ温度 実測 左(℃)
     * @param tableondosokuhidari 
     */
    public void setTableondosokuhidari(BigDecimal tableondosokuhidari) {
        this.tableondosokuhidari = tableondosokuhidari;
    }

    /**
     * ﾃｰﾌﾞﾙ温度 実測 中(℃)
     * @return tableondosokunaka
     */
    public BigDecimal getTableondosokunaka() {
        return tableondosokunaka;
    }

    /**
     * ﾃｰﾌﾞﾙ温度 実測 中(℃)
     * @param tableondosokunaka 
     */
    public void setTableondosokunaka(BigDecimal tableondosokunaka) {
        this.tableondosokunaka = tableondosokunaka;
    }

    /**
     * ﾃｰﾌﾞﾙ温度 実測 右(℃)
     * @return tableondosokumigi
     */
    public BigDecimal getTableondosokumigi() {
        return tableondosokumigi;
    }

    /**
     * ﾃｰﾌﾞﾙ温度 実測 右(℃)
     * @param tableondosokumigi 
     */
    public void setTableondosokumigi(BigDecimal tableondosokumigi) {
        this.tableondosokumigi = tableondosokumigi;
    }

    /**
     * 第2ﾃｰﾌﾞﾙ温度 設定 左(℃)
     * @return dai2tableondosethidari
     */
    public BigDecimal getDai2tableondosethidari() {
        return dai2tableondosethidari;
    }

    /**
     * 第2ﾃｰﾌﾞﾙ温度 設定 左(℃)
     * @param dai2tableondosethidari 
     */
    public void setDai2tableondosethidari(BigDecimal dai2tableondosethidari) {
        this.dai2tableondosethidari = dai2tableondosethidari;
    }

    /**
     * 第2ﾃｰﾌﾞﾙ温度 設定 中(℃)
     * @return dai2tableondosetnaka
     */
    public BigDecimal getDai2tableondosetnaka() {
        return dai2tableondosetnaka;
    }

    /**
     * 第2ﾃｰﾌﾞﾙ温度 設定 中(℃)
     * @param dai2tableondosetnaka 
     */
    public void setDai2tableondosetnaka(BigDecimal dai2tableondosetnaka) {
        this.dai2tableondosetnaka = dai2tableondosetnaka;
    }

    /**
     * 第2ﾃｰﾌﾞﾙ温度 設定 右(℃)
     * @return dai2tableondosetmigi
     */
    public BigDecimal getDai2tableondosetmigi() {
        return dai2tableondosetmigi;
    }

    /**
     * 第2ﾃｰﾌﾞﾙ温度 設定 右(℃)
     * @param dai2tableondosetmigi 
     */
    public void setDai2tableondosetmigi(BigDecimal dai2tableondosetmigi) {
        this.dai2tableondosetmigi = dai2tableondosetmigi;
    }

    /**
     * 第2ﾃｰﾌﾞﾙ温度 実測 左(℃)
     * @return dai2tableondosokuhidari
     */
    public BigDecimal getDai2tableondosokuhidari() {
        return dai2tableondosokuhidari;
    }

    /**
     * 第2ﾃｰﾌﾞﾙ温度 実測 左(℃)
     * @param dai2tableondosokuhidari 
     */
    public void setDai2tableondosokuhidari(BigDecimal dai2tableondosokuhidari) {
        this.dai2tableondosokuhidari = dai2tableondosokuhidari;
    }

    /**
     * 第2ﾃｰﾌﾞﾙ温度 実測 中(℃)
     * @return dai2tableondosokunaka
     */
    public BigDecimal getDai2tableondosokunaka() {
        return dai2tableondosokunaka;
    }

    /**
     * 第2ﾃｰﾌﾞﾙ温度 実測 中(℃)
     * @param dai2tableondosokunaka 
     */
    public void setDai2tableondosokunaka(BigDecimal dai2tableondosokunaka) {
        this.dai2tableondosokunaka = dai2tableondosokunaka;
    }

    /**
     * 第2ﾃｰﾌﾞﾙ温度 実測 右(℃)
     * @return dai2tableondosokumigi
     */
    public BigDecimal getDai2tableondosokumigi() {
        return dai2tableondosokumigi;
    }

    /**
     * 第2ﾃｰﾌﾞﾙ温度 実測 右(℃)
     * @param dai2tableondosokumigi 
     */
    public void setDai2tableondosokumigi(BigDecimal dai2tableondosokumigi) {
        this.dai2tableondosokumigi = dai2tableondosokumigi;
    }

    /**
     * 外観確認
     * @return the gaikanCheck
     */
    public String getGaikanCheck() {
        return gaikanCheck;
    }

    /**
     * 外観確認
     * @param gaikanCheck the gaikanCheck to set
     */
    public void setGaikanCheck(String gaikanCheck) {
        this.gaikanCheck = gaikanCheck;
    }

    /**
     * 刃高さNG(SET)
     * @return the hatakasang
     */
    public Integer getHatakasang() {
        return hatakasang;
    }

    /**
     * 刃高さNG(SET)
     * @param hatakasang the hatakasang to set
     */
    public void setHatakasang(Integer hatakasang) {
        this.hatakasang = hatakasang;
    }

    /**
     * ｶｯﾄ後剥がし温度(℃)
     * @return ondo
     */
    public BigDecimal getOndo() {
        return ondo;
    }

    /**
     * ｶｯﾄ後剥がし温度(℃)
     * @param ondo 
     */
    public void setOndo(BigDecimal ondo) {
        this.ondo = ondo;
    }

    /**
     * 処理ｾｯﾄ数(SET)
     * @return the syoriSetsuu
     */
    public Integer getSyoriSetsuu() {
        return syoriSetsuu;
    }

    /**
     * 処理ｾｯﾄ数(SET)
     * @param syoriSetsuu the syoriSetsuu to set
     */
    public void setSyoriSetsuu(Integer syoriSetsuu) {
        this.syoriSetsuu = syoriSetsuu;
    }

    /**
     * 良品ｾｯﾄ数(SET)
     * @return the ryouhinSetsuu
     */
    public BigDecimal getRyouhinSetsuu() {
        return ryouhinSetsuu;
    }

    /**
     * 良品ｾｯﾄ数(SET)
     * @param ryouhinSetsuu the ryouhinSetsuu to set
     */
    public void setRyouhinSetsuu(BigDecimal ryouhinSetsuu) {
        this.ryouhinSetsuu = ryouhinSetsuu;
    }

    /**
     * 作業場所
     * @return the sagyouBasyo
     */
    public String getSagyouBasyo() {
        return sagyouBasyo;
    }

    /**
     * 作業場所
     * @param sagyouBasyo the sagyouBasyo to set
     */
    public void setSagyouBasyo(String sagyouBasyo) {
        this.sagyouBasyo = sagyouBasyo;
    }

}
