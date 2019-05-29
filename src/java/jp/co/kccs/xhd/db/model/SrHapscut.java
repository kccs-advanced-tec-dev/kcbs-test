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
 * 変更日	2019/05/25<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	KCCS D.Yanagida<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SR_HAPSCUT(押切ｶｯﾄ)のモデルクラスです。
 *
 * @author KCCS D.Yanagida
 * @since  2019/05/23
 */
public class SrHapscut {
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
     * KCPNO
     */
    private String kcpno;

    /**
     * 開始日時
     */
    private Timestamp kaisinichiji;

    /**
     * 終了日時
     */
    private Timestamp syuryonichiji;

    /**
     * ｶｯﾄ刃枚数
     */
    private Integer cutbamaisuu;

    /**
     * 号機ｺｰﾄﾞ
     */
    private String goki;

    /**
     * ｶｯﾄﾃｰﾌﾞﾙ温度
     */
    private String cuttableondo;

    /**
     * ｶｯﾄ担当者
     */
    private String cuttantosya;

    /**
     * 確認者
     */
    private String kakuninsya;

    /**
     * ﾁｪｯｸ担当者
     */
    private String chktantosya;

    /**
     * B担当者
     */
    private String btantosya;

    /**
     * A担当者
     */
    private String atantosya;

    /**
     * 受入個数
     */
    private Integer ukeirekosuu;

    /**
     * 良品個数
     */
    private Integer ryohinkosuu;

    /**
     * 厚み01
     */
    private Long atumi01;

    /**
     * 厚み02
     */
    private Long atumi02;

    /**
     * 厚み03
     */
    private Long atumi03;

    /**
     * 厚み04
     */
    private Long atumi04;

    /**
     * 厚み05
     */
    private Long atumi05;

    /**
     * 厚み06
     */
    private Long atumi06;

    /**
     * 厚み07
     */
    private Long atumi07;

    /**
     * 厚み08
     */
    private Long atumi08;

    /**
     * 厚み09
     */
    private Long atumi09;

    /**
     * 厚み10
     */
    private Long atumi10;

    /**
     * 厚みMIN
     */
    private Integer atumimin;

    /**
     * 厚みMAX
     */
    private Integer atumimax;

    /**
     * 備考1
     */
    private String biko1;

    /**
     * 備考2
     */
    private String biko2;

    /**
     * 備考3
     */
    private String biko3;

    /**
     * 備考4
     */
    private String biko4;

    /**
     * 登録日時
     */
    private Timestamp torokunichiji;

    /**
     * 更新日時
     */
    private Timestamp kosinnichiji;

    /**
     * 転写者
     */
    private String tensyasya;

    /**
     * ﾆｼﾞﾐ回数
     */
    private Long nijimicnt;

    /**
     * 総重量
     */
    private BigDecimal soujyuryo;

    /**
     * 単位重量
     */
    private BigDecimal tanijyuryo;

    /**
     * ｶｯﾄ刃種類確認
     */
    private Integer cutbashuruicheck;

    /**
     * ｶｯﾄ刃直進度
     */
    private Integer cutbachokushindo;

    /**
     * ｶｯﾄ刃使用回数ST1
     */
    private Integer cutbasiyoukaisuust1;

    /**
     * ｶｯﾄ刃使用回数ST2
     */
    private Integer cutbasiyoukaisuust2;

    /**
     * ﾌﾟﾛｸﾞﾗﾑ名
     */
    private String programmei;

    /**
     * 行×列確認
     */
    private Integer gyoretukakunin;

    /**
     * ﾏｰｸ外取り数
     */
    private Integer marktorisuu;

    /**
     * ｶｯﾄ補正量
     */
    private Integer cuthoseiryou;

    /**
     * ﾃｰﾌﾞﾙ温度 設定
     */
    private Integer tableondoset;

    /**
     * ﾃｰﾌﾞﾙ温度 実測
     */
    private Integer tableondosoku;

    /**
     * 外観確認
     */
    private Integer gaikancheck;

    /**
     * 刃高さNG
     */
    private Integer hatakasang;

    /**
     * 処理ｾｯﾄ数
     */
    private Integer syorisetsuu;

    /**
     * 良品ｾｯﾄ数
     */
    private BigDecimal ryouhinsetsuu;

    /**
     * 作業場所
     */
    private String sagyoubasyo;

    /**
     * revision
     */
    private Long revision;
    
    /**
     * 削除ﾌﾗｸﾞ
     */
    private Integer deleteflag;

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
     * ｶｯﾄ刃枚数
     * @return the cutbamaisuu
     */
    public Integer getCutbamaisuu() {
        return cutbamaisuu;
    }

    /**
     * ｶｯﾄ刃枚数
     * @param cutbamaisuu the cutbamaisuu to set
     */
    public void setCutbamaisuu(Integer cutbamaisuu) {
        this.cutbamaisuu = cutbamaisuu;
    }

    /**
     * 号機ｺｰﾄﾞ
     * @return the goki
     */
    public String getGoki() {
        return goki;
    }

    /**
     * 号機ｺｰﾄﾞ
     * @param goki the goki to set
     */
    public void setGoki(String goki) {
        this.goki = goki;
    }

    /**
     * ｶｯﾄﾃｰﾌﾞﾙ温度
     * @return the cuttableondo
     */
    public String getCuttableondo() {
        return cuttableondo;
    }

    /**
     * ｶｯﾄﾃｰﾌﾞﾙ温度
     * @param cuttableondo the cuttableondo to set
     */
    public void setCuttableondo(String cuttableondo) {
        this.cuttableondo = cuttableondo;
    }

    /**
     * ｶｯﾄ担当者
     * @return the cuttantosya
     */
    public String getCuttantosya() {
        return cuttantosya;
    }

    /**
     * ｶｯﾄ担当者
     * @param cuttantosya the cuttantosya to set
     */
    public void setCuttantosya(String cuttantosya) {
        this.cuttantosya = cuttantosya;
    }

    /**
     * 確認者
     * @return the kakuninsya
     */
    public String getKakuninsya() {
        return kakuninsya;
    }

    /**
     * 確認者
     * @param kakuninsya the kakuninsya to set
     */
    public void setKakuninsya(String kakuninsya) {
        this.kakuninsya = kakuninsya;
    }

    /**
     * ﾁｪｯｸ担当者
     * @return the chktantosya
     */
    public String getChktantosya() {
        return chktantosya;
    }

    /**
     * ﾁｪｯｸ担当者
     * @param chktantosya the chktantosya to set
     */
    public void setChktantosya(String chktantosya) {
        this.chktantosya = chktantosya;
    }

    /**
     * B担当者
     * @return the btantosya
     */
    public String getBtantosya() {
        return btantosya;
    }

    /**
     * B担当者
     * @param btantosya the btantosya to set
     */
    public void setBtantosya(String btantosya) {
        this.btantosya = btantosya;
    }

    /**
     * A担当者
     * @return the atantosya
     */
    public String getAtantosya() {
        return atantosya;
    }

    /**
     * A担当者
     * @param atantosya the atantosya to set
     */
    public void setAtantosya(String atantosya) {
        this.atantosya = atantosya;
    }

    /**
     * 受入個数
     * @return the ukeirekosuu
     */
    public Integer getUkeirekosuu() {
        return ukeirekosuu;
    }

    /**
     * 受入個数
     * @param ukeirekosuu the ukeirekosuu to set
     */
    public void setUkeirekosuu(Integer ukeirekosuu) {
        this.ukeirekosuu = ukeirekosuu;
    }

    /**
     * 良品個数
     * @return the ryohinkosuu
     */
    public Integer getRyohinkosuu() {
        return ryohinkosuu;
    }

    /**
     * 良品個数
     * @param ryohinkosuu the ryohinkosuu to set
     */
    public void setRyohinkosuu(Integer ryohinkosuu) {
        this.ryohinkosuu = ryohinkosuu;
    }

    /**
     * 厚み01
     * @return the atumi01
     */
    public Long getAtumi01() {
        return atumi01;
    }

    /**
     * 厚み01
     * @param atumi01 the atumi01 to set
     */
    public void setAtumi01(Long atumi01) {
        this.atumi01 = atumi01;
    }

    /**
     * 厚み02
     * @return the atumi02
     */
    public Long getAtumi02() {
        return atumi02;
    }

    /**
     * 厚み02
     * @param atumi02 the atumi02 to set
     */
    public void setAtumi02(Long atumi02) {
        this.atumi02 = atumi02;
    }

    /**
     * 厚み03
     * @return the atumi03
     */
    public Long getAtumi03() {
        return atumi03;
    }

    /**
     * 厚み03
     * @param atumi03 the atumi03 to set
     */
    public void setAtumi03(Long atumi03) {
        this.atumi03 = atumi03;
    }

    /**
     * 厚み04
     * @return the atumi04
     */
    public Long getAtumi04() {
        return atumi04;
    }

    /**
     * 厚み04
     * @param atumi04 the atumi04 to set
     */
    public void setAtumi04(Long atumi04) {
        this.atumi04 = atumi04;
    }

    /**
     * 厚み05
     * @return the atumi05
     */
    public Long getAtumi05() {
        return atumi05;
    }

    /**
     * 厚み05
     * @param atumi05 the atumi05 to set
     */
    public void setAtumi05(Long atumi05) {
        this.atumi05 = atumi05;
    }

    /**
     * 厚み06
     * @return the atumi06
     */
    public Long getAtumi06() {
        return atumi06;
    }

    /**
     * 厚み06
     * @param atumi06 the atumi06 to set
     */
    public void setAtumi06(Long atumi06) {
        this.atumi06 = atumi06;
    }

    /**
     * 厚み07
     * @return the atumi07
     */
    public Long getAtumi07() {
        return atumi07;
    }

    /**
     * 厚み07
     * @param atumi07 the atumi07 to set
     */
    public void setAtumi07(Long atumi07) {
        this.atumi07 = atumi07;
    }

    /**
     * 厚み08
     * @return the atumi08
     */
    public Long getAtumi08() {
        return atumi08;
    }

    /**
     * 厚み08
     * @param atumi08 the atumi08 to set
     */
    public void setAtumi08(Long atumi08) {
        this.atumi08 = atumi08;
    }

    /**
     * 厚み09
     * @return the atumi09
     */
    public Long getAtumi09() {
        return atumi09;
    }

    /**
     * 厚み09
     * @param atumi09 the atumi09 to set
     */
    public void setAtumi09(Long atumi09) {
        this.atumi09 = atumi09;
    }

    /**
     * 厚み10
     * @return the atumi10
     */
    public Long getAtumi10() {
        return atumi10;
    }

    /**
     * 厚み10
     * @param atumi10 the atumi10 to set
     */
    public void setAtumi10(Long atumi10) {
        this.atumi10 = atumi10;
    }

    /**
     * 厚みMIN
     * @return the atumimin
     */
    public Integer getAtumimin() {
        return atumimin;
    }

    /**
     * 厚みMIN
     * @param atumimin the atumimin to set
     */
    public void setAtumimin(Integer atumimin) {
        this.atumimin = atumimin;
    }

    /**
     * 厚みMAX
     * @return the atumimax
     */
    public Integer getAtumimax() {
        return atumimax;
    }

    /**
     * 厚みMAX
     * @param atumimax the atumimax to set
     */
    public void setAtumimax(Integer atumimax) {
        this.atumimax = atumimax;
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
    public Long getNijimicnt() {
        return nijimicnt;
    }

    /**
     * ﾆｼﾞﾐ回数
     * @param nijimicnt the nijimicnt to set
     */
    public void setNijimicnt(Long nijimicnt) {
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
    public Integer getCutbashuruicheck() {
        return cutbashuruicheck;
    }

    /**
     * ｶｯﾄ刃種類確認
     * @param cutbashuruicheck the cutbashuruicheck to set
     */
    public void setCutbashuruicheck(Integer cutbashuruicheck) {
        this.cutbashuruicheck = cutbashuruicheck;
    }

    /**
     * ｶｯﾄ刃直進度
     * @return the cutbachokushindo
     */
    public Integer getCutbachokushindo() {
        return cutbachokushindo;
    }

    /**
     * ｶｯﾄ刃直進度
     * @param cutbachokushindo the cutbachokushindo to set
     */
    public void setCutbachokushindo(Integer cutbachokushindo) {
        this.cutbachokushindo = cutbachokushindo;
    }

    /**
     * ｶｯﾄ刃使用回数ST1
     * @return the cutbasiyoukaisuust1
     */
    public Integer getCutbasiyoukaisuust1() {
        return cutbasiyoukaisuust1;
    }

    /**
     * ｶｯﾄ刃使用回数ST1
     * @param cutbasiyoukaisuust1 the cutbasiyoukaisuust1 to set
     */
    public void setCutbasiyoukaisuust1(Integer cutbasiyoukaisuust1) {
        this.cutbasiyoukaisuust1 = cutbasiyoukaisuust1;
    }

    /**
     * ｶｯﾄ刃使用回数ST2
     * @return the cutbasiyoukaisuust2
     */
    public Integer getCutbasiyoukaisuust2() {
        return cutbasiyoukaisuust2;
    }

    /**
     * ｶｯﾄ刃使用回数ST2
     * @param cutbasiyoukaisuust2 the cutbasiyoukaisuust2 to set
     */
    public void setCutbasiyoukaisuust2(Integer cutbasiyoukaisuust2) {
        this.cutbasiyoukaisuust2 = cutbasiyoukaisuust2;
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
     * @return the gyoretukakunin
     */
    public Integer getGyoretukakunin() {
        return gyoretukakunin;
    }

    /**
     * 行×列確認
     * @param gyoretukakunin the gyoretukakunin to set
     */
    public void setGyoretukakunin(Integer gyoretukakunin) {
        this.gyoretukakunin = gyoretukakunin;
    }

    /**
     * ﾏｰｸ外取り数
     * @return the marktorisuu
     */
    public Integer getMarktorisuu() {
        return marktorisuu;
    }

    /**
     * ﾏｰｸ外取り数
     * @param marktorisuu the marktorisuu to set
     */
    public void setMarktorisuu(Integer marktorisuu) {
        this.marktorisuu = marktorisuu;
    }

    /**
     * ｶｯﾄ補正量
     * @return the cuthoseiryou
     */
    public Integer getCuthoseiryou() {
        return cuthoseiryou;
    }

    /**
     * ｶｯﾄ補正量
     * @param cuthoseiryou the cuthoseiryou to set
     */
    public void setCuthoseiryou(Integer cuthoseiryou) {
        this.cuthoseiryou = cuthoseiryou;
    }

    /**
     * ﾃｰﾌﾞﾙ温度 設定
     * @return the tableondoset
     */
    public Integer getTableondoset() {
        return tableondoset;
    }

    /**
     * ﾃｰﾌﾞﾙ温度 設定
     * @param tableondoset the tableondoset to set
     */
    public void setTableondoset(Integer tableondoset) {
        this.tableondoset = tableondoset;
    }

    /**
     * ﾃｰﾌﾞﾙ温度 実測
     * @return the tableondosoku
     */
    public Integer getTableondosoku() {
        return tableondosoku;
    }

    /**
     * ﾃｰﾌﾞﾙ温度 実測
     * @param tableondosoku the tableondosoku to set
     */
    public void setTableondosoku(Integer tableondosoku) {
        this.tableondosoku = tableondosoku;
    }

    /**
     * 外観確認
     * @return the gaikancheck
     */
    public Integer getGaikancheck() {
        return gaikancheck;
    }

    /**
     * 外観確認
     * @param gaikancheck the gaikancheck to set
     */
    public void setGaikancheck(Integer gaikancheck) {
        this.gaikancheck = gaikancheck;
    }

    /**
     * 刃高さNG
     * @return the hatakasang
     */
    public Integer getHatakasang() {
        return hatakasang;
    }

    /**
     * 刃高さNG
     * @param hatakasang the hatakasang to set
     */
    public void setHatakasang(Integer hatakasang) {
        this.hatakasang = hatakasang;
    }

    /**
     * 処理ｾｯﾄ数
     * @return the syorisetsuu
     */
    public Integer getSyorisetsuu() {
        return syorisetsuu;
    }

    /**
     * 処理ｾｯﾄ数
     * @param syorisetsuu the syorisetsuu to set
     */
    public void setSyorisetsuu(Integer syorisetsuu) {
        this.syorisetsuu = syorisetsuu;
    }

    /**
     * 良品ｾｯﾄ数
     * @return the ryouhinsetsuu
     */
    public BigDecimal getRyouhinsetsuu() {
        return ryouhinsetsuu;
    }

    /**
     * 良品ｾｯﾄ数
     * @param ryouhinsetsuu the ryouhinsetsuu to set
     */
    public void setRyouhinsetsuu(BigDecimal ryouhinsetsuu) {
        this.ryouhinsetsuu = ryouhinsetsuu;
    }

    /**
     * 作業場所
     * @return the sagyoubasyo
     */
    public String getSagyoubasyo() {
        return sagyoubasyo;
    }

    /**
     * 作業場所
     * @param sagyoubasyo the sagyoubasyo to set
     */
    public void setSagyoubasyo(String sagyoubasyo) {
        this.sagyoubasyo = sagyoubasyo;
    }

    /**
     * revision
     * @return the revision
     */
    public Long getRevision() {
        return revision;
    }

    /**
     * revision
     * @param revision the revision to set
     */
    public void setRevision(Long revision) {
        this.revision = revision;
    }

    /**
     * 削除ﾌﾗｸﾞ
     * @return the deleteflag
     */
    public Integer getDeleteflag() {
        return deleteflag;
    }

    /**
     * 削除ﾌﾗｸﾞ
     * @param deleteflag the deleteflag to set
     */
    public void setDeleteflag(Integer deleteflag) {
        this.deleteflag = deleteflag;
    }
}
