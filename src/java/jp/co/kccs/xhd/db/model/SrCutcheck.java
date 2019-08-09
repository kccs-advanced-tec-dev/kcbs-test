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
 * 変更日	2019/05/22<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 生品質検査のモデルクラスです。
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2019/05/22
 */
public class SrCutcheck {

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
     * 開始日時
     */
    private Timestamp startdatetime;

    /**
     * 終了日時
     */
    private Timestamp enddatetime;

    /**
     * 担当者
     */
    private String tantousya;

    /**
     * 確認者
     */
    private String kakuninsya;

    /**
     * 厚みMIN
     */
    private Long atumimin;

    /**
     * 厚みMAX
     */
    private Long atumimax;

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
     * 総重量
     */
    private BigDecimal soujyuryo;

    /**
     * 単位重量
     */
    private BigDecimal tanijyuryo;

    /**
     * 外観検査終了担当者
     */
    private String gaikankensatantousya;

    /**
     * ばらし方法
     */
    private String barasshi;

    /**
     * 条件
     */
    private Integer joken;

    /**
     * ばらし開始日時
     */
    private Timestamp barashistartnichiji;

    /**
     * ばらし開始担当者
     */
    private String batashistarttantousya;

    /**
     * ばらし終了日時
     */
    private Timestamp barashiendnichiji;

    /**
     * ばらし終了担当者
     */
    private String barashiendtantousya;

    /**
     * 粉まぶし
     */
    private Integer konamabushi;

    /**
     * 処理個数
     */
    private Integer syorisetsuu;

    /**
     * 良品個数
     */
    private Integer ryouhinsetsuu;

    /**
     * 歩留まり
     */
    private BigDecimal budomari;

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
    private Long revision;

    /**
     * 削除ﾌﾗｸﾞ
     */
    private Integer deleteflag;
    
    /**
     * KCPNO
     */
    private String kcpno;

    /**
     * 工場ｺｰﾄﾞ
     *
     * @return the kojyo
     */
    public String getKojyo() {
        return kojyo;
    }

    /**
     * 工場ｺｰﾄﾞ
     *
     * @param kojyo the kojyo to set
     */
    public void setKojyo(String kojyo) {
        this.kojyo = kojyo;
    }

    /**
     * ﾛｯﾄNo
     *
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * ﾛｯﾄNo
     *
     * @param lotno the lotno to set
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * 枝番
     *
     * @return the edaban
     */
    public String getEdaban() {
        return edaban;
    }

    /**
     * 枝番
     *
     * @param edaban the edaban to set
     */
    public void setEdaban(String edaban) {
        this.edaban = edaban;
    }

    /**
     * 開始日時
     *
     * @return the startdatetime
     */
    public Timestamp getStartdatetime() {
        return startdatetime;
    }

    /**
     * 開始日時
     *
     * @param startdatetime the startdatetime to set
     */
    public void setStartdatetime(Timestamp startdatetime) {
        this.startdatetime = startdatetime;
    }

    /**
     * 終了日時
     *
     * @return the enddatetime
     */
    public Timestamp getEnddatetime() {
        return enddatetime;
    }

    /**
     * 終了日時
     *
     * @param enddatetime the enddatetime to set
     */
    public void setEnddatetime(Timestamp enddatetime) {
        this.enddatetime = enddatetime;
    }

    /**
     * 担当者
     *
     * @return the tantousya
     */
    public String getTantousya() {
        return tantousya;
    }

    /**
     * 担当者
     *
     * @param tantousya the tantousya to set
     */
    public void setTantousya(String tantousya) {
        this.tantousya = tantousya;
    }

    /**
     * 確認者
     *
     * @return the kakuninsya
     */
    public String getKakuninsya() {
        return kakuninsya;
    }

    /**
     * 確認者
     *
     * @param kakuninsya the kakuninsya to set
     */
    public void setKakuninsya(String kakuninsya) {
        this.kakuninsya = kakuninsya;
    }

    /**
     * 厚みMIN
     *
     * @return the atumimin
     */
    public Long getAtumimin() {
        return atumimin;
    }

    /**
     * 厚みMIN
     *
     * @param atumimin the atumimin to set
     */
    public void setAtumimin(Long atumimin) {
        this.atumimin = atumimin;
    }

    /**
     * 厚みMAX
     *
     * @return the atumimax
     */
    public Long getAtumimax() {
        return atumimax;
    }

    /**
     * 厚みMAX
     *
     * @param atumimax the atumimax to set
     */
    public void setAtumimax(Long atumimax) {
        this.atumimax = atumimax;
    }

    /**
     * 厚み01
     *
     * @return the atumi01
     */
    public Long getAtumi01() {
        return atumi01;
    }

    /**
     * 厚み01
     *
     * @param atumi01 the atumi01 to set
     */
    public void setAtumi01(Long atumi01) {
        this.atumi01 = atumi01;
    }

    /**
     * 厚み02
     *
     * @return the atumi02
     */
    public Long getAtumi02() {
        return atumi02;
    }

    /**
     * 厚み02
     *
     * @param atumi02 the atumi02 to set
     */
    public void setAtumi02(Long atumi02) {
        this.atumi02 = atumi02;
    }

    /**
     * 厚み03
     *
     * @return the atumi03
     */
    public Long getAtumi03() {
        return atumi03;
    }

    /**
     * 厚み03
     *
     * @param atumi03 the atumi03 to set
     */
    public void setAtumi03(Long atumi03) {
        this.atumi03 = atumi03;
    }

    /**
     * 厚み04
     *
     * @return the atumi04
     */
    public Long getAtumi04() {
        return atumi04;
    }

    /**
     * 厚み04
     *
     * @param atumi04 the atumi04 to set
     */
    public void setAtumi04(Long atumi04) {
        this.atumi04 = atumi04;
    }

    /**
     * 厚み05
     *
     * @return the atumi05
     */
    public Long getAtumi05() {
        return atumi05;
    }

    /**
     * 厚み05
     *
     * @param atumi05 the atumi05 to set
     */
    public void setAtumi05(Long atumi05) {
        this.atumi05 = atumi05;
    }

    /**
     * 厚み06
     *
     * @return the atumi06
     */
    public Long getAtumi06() {
        return atumi06;
    }

    /**
     * 厚み06
     *
     * @param atumi06 the atumi06 to set
     */
    public void setAtumi06(Long atumi06) {
        this.atumi06 = atumi06;
    }

    /**
     * 厚み07
     *
     * @return the atumi07
     */
    public Long getAtumi07() {
        return atumi07;
    }

    /**
     * 厚み07
     *
     * @param atumi07 the atumi07 to set
     */
    public void setAtumi07(Long atumi07) {
        this.atumi07 = atumi07;
    }

    /**
     * 厚み08
     *
     * @return the atumi08
     */
    public Long getAtumi08() {
        return atumi08;
    }

    /**
     * 厚み08
     *
     * @param atumi08 the atumi08 to set
     */
    public void setAtumi08(Long atumi08) {
        this.atumi08 = atumi08;
    }

    /**
     * 厚み09
     *
     * @return the atumi09
     */
    public Long getAtumi09() {
        return atumi09;
    }

    /**
     * 厚み09
     *
     * @param atumi09 the atumi09 to set
     */
    public void setAtumi09(Long atumi09) {
        this.atumi09 = atumi09;
    }

    /**
     * 厚み10
     *
     * @return the atumi10
     */
    public Long getAtumi10() {
        return atumi10;
    }

    /**
     * 厚み10
     *
     * @param atumi10 the atumi10 to set
     */
    public void setAtumi10(Long atumi10) {
        this.atumi10 = atumi10;
    }

    /**
     * 備考1
     *
     * @return the bikou1
     */
    public String getBikou1() {
        return bikou1;
    }

    /**
     * 備考1
     *
     * @param bikou1 the bikou1 to set
     */
    public void setBikou1(String bikou1) {
        this.bikou1 = bikou1;
    }

    /**
     * 備考2
     *
     * @return the bikou2
     */
    public String getBikou2() {
        return bikou2;
    }

    /**
     * 備考2
     *
     * @param bikou2 the bikou2 to set
     */
    public void setBikou2(String bikou2) {
        this.bikou2 = bikou2;
    }

    /**
     * 備考3
     *
     * @return the bikou3
     */
    public String getBikou3() {
        return bikou3;
    }

    /**
     * 備考3
     *
     * @param bikou3 the bikou3 to set
     */
    public void setBikou3(String bikou3) {
        this.bikou3 = bikou3;
    }

    /**
     * 備考4
     *
     * @return the bikou4
     */
    public String getBikou4() {
        return bikou4;
    }

    /**
     * 備考4
     *
     * @param bikou4 the bikou4 to set
     */
    public void setBikou4(String bikou4) {
        this.bikou4 = bikou4;
    }

    /**
     * 備考5
     *
     * @return the bikou5
     */
    public String getBikou5() {
        return bikou5;
    }

    /**
     * 備考5
     *
     * @param bikou5 the bikou5 to set
     */
    public void setBikou5(String bikou5) {
        this.bikou5 = bikou5;
    }

    /**
     * 総重量
     *
     * @return the soujyuryo
     */
    public BigDecimal getSoujyuryo() {
        return soujyuryo;
    }

    /**
     * 総重量
     *
     * @param soujyuryo the soujyuryo to set
     */
    public void setSoujyuryo(BigDecimal soujyuryo) {
        this.soujyuryo = soujyuryo;
    }

    /**
     * 単位重量
     *
     * @return the tanijyuryo
     */
    public BigDecimal getTanijyuryo() {
        return tanijyuryo;
    }

    /**
     * 単位重量
     *
     * @param tanijyuryo the tanijyuryo to set
     */
    public void setTanijyuryo(BigDecimal tanijyuryo) {
        this.tanijyuryo = tanijyuryo;
    }

    /**
     * 外観検査終了担当者
     *
     * @return the gaikankensatantousya
     */
    public String getGaikankensatantousya() {
        return gaikankensatantousya;
    }

    /**
     * 外観検査終了担当者
     *
     * @param gaikankensatantousya the gaikankensatantousya to set
     */
    public void setGaikankensatantousya(String gaikankensatantousya) {
        this.gaikankensatantousya = gaikankensatantousya;
    }

    /**
     * ばらし方法
     *
     * @return the barasshi
     */
    public String getBarasshi() {
        return barasshi;
    }

    /**
     * ばらし方法
     *
     * @param barasshi the barasshi to set
     */
    public void setBarasshi(String barasshi) {
        this.barasshi = barasshi;
    }

    /**
     * 条件
     *
     * @return the joken
     */
    public Integer getJoken() {
        return joken;
    }

    /**
     * 条件
     *
     * @param joken the joken to set
     */
    public void setJoken(Integer joken) {
        this.joken = joken;
    }

    /**
     * ばらし開始日時
     *
     * @return the barashistartnichiji
     */
    public Timestamp getBarashistartnichiji() {
        return barashistartnichiji;
    }

    /**
     * ばらし開始日時
     *
     * @param barashistartnichiji the barashistartnichiji to set
     */
    public void setBarashistartnichiji(Timestamp barashistartnichiji) {
        this.barashistartnichiji = barashistartnichiji;
    }

    /**
     * ばらし開始担当者
     *
     * @return the batashistarttantousya
     */
    public String getBatashistarttantousya() {
        return batashistarttantousya;
    }

    /**
     * ばらし開始担当者
     *
     * @param batashistarttantousya the batashistarttantousya to set
     */
    public void setBatashistarttantousya(String batashistarttantousya) {
        this.batashistarttantousya = batashistarttantousya;
    }

    /**
     * ばらし終了日時
     *
     * @return the barashiendnichiji
     */
    public Timestamp getBarashiendnichiji() {
        return barashiendnichiji;
    }

    /**
     * ばらし終了日時
     *
     * @param barashiendnichiji the barashiendnichiji to set
     */
    public void setBarashiendnichiji(Timestamp barashiendnichiji) {
        this.barashiendnichiji = barashiendnichiji;
    }

    /**
     * ばらし終了担当者
     *
     * @return the barashiendtantousya
     */
    public String getBarashiendtantousya() {
        return barashiendtantousya;
    }

    /**
     * ばらし終了担当者
     *
     * @param barashiendtantousya the barashiendtantousya to set
     */
    public void setBarashiendtantousya(String barashiendtantousya) {
        this.barashiendtantousya = barashiendtantousya;
    }

    /**
     * 粉まぶし
     *
     * @return the konamabushi
     */
    public Integer getKonamabushi() {
        return konamabushi;
    }

    /**
     * 粉まぶし
     *
     * @param konamabushi the konamabushi to set
     */
    public void setKonamabushi(Integer konamabushi) {
        this.konamabushi = konamabushi;
    }

    /**
     * 処理個数
     *
     * @return the syorisetsuu
     */
    public Integer getSyorisetsuu() {
        return syorisetsuu;
    }

    /**
     * 処理個数
     *
     * @param syorisetsuu the syorisetsuu to set
     */
    public void setSyorisetsuu(Integer syorisetsuu) {
        this.syorisetsuu = syorisetsuu;
    }

    /**
     * 良品個数
     *
     * @return the ryouhinsetsuu
     */
    public Integer getRyouhinsetsuu() {
        return ryouhinsetsuu;
    }

    /**
     * 良品個数
     *
     * @param ryouhinsetsuu the ryouhinsetsuu to set
     */
    public void setRyouhinsetsuu(Integer ryouhinsetsuu) {
        this.ryouhinsetsuu = ryouhinsetsuu;
    }

    /**
     * 歩留まり
     *
     * @return the budomari
     */
    public BigDecimal getBudomari() {
        return budomari;
    }

    /**
     * 歩留まり
     *
     * @param budomari the budomari to set
     */
    public void setBudomari(BigDecimal budomari) {
        this.budomari = budomari;
    }

    /**
     * 登録日時
     *
     * @return the torokunichiji
     */
    public Timestamp getTorokunichiji() {
        return torokunichiji;
    }

    /**
     * 登録日時
     *
     * @param torokunichiji the torokunichiji to set
     */
    public void setTorokunichiji(Timestamp torokunichiji) {
        this.torokunichiji = torokunichiji;
    }

    /**
     * 更新日時
     *
     * @return the kosinnichiji
     */
    public Timestamp getKosinnichiji() {
        return kosinnichiji;
    }

    /**
     * 更新日時
     *
     * @param kosinnichiji the kosinnichiji to set
     */
    public void setKosinnichiji(Timestamp kosinnichiji) {
        this.kosinnichiji = kosinnichiji;
    }

    /**
     * revision
     *
     * @return the revision
     */
    public Long getRevision() {
        return revision;
    }

    /**
     * revision
     *
     * @param revision the revision to set
     */
    public void setRevision(Long revision) {
        this.revision = revision;
    }

    /**
     * 削除ﾌﾗｸﾞ
     *
     * @return the deleteflag
     */
    public Integer getDeleteflag() {
        return deleteflag;
    }

    /**
     * 削除ﾌﾗｸﾞ
     *
     * @param deleteflag the deleteflag to set
     */
    public void setDeleteflag(Integer deleteflag) {
        this.deleteflag = deleteflag;
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

}
