/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/06/20<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCCS D.Yanagida<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 再酸化のモデルクラスです。
 *
 * @author KCCS D.Yanagida
 * @since 2019/06/20
 */
public class SrSaisanka {
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
     * 実績No
     */
    private Integer jissekino;

    /**
     * KCPNo
     */
    private String KCPNO;

    /**
     * 受入セッタ枚数
     */
    private Long ukeiresettamaisuu;

    /**
     * 指定再酸化回数
     */
    private Long siteisaisaka;

    /**
     * 投入セッタ枚数
     */
    private Long tounyusettasuu;

    /**
     * 号機
     */
    private String gouki;

    /**
     * 設定ﾊﾟﾀｰﾝ
     */
    private Long setteipattern;

    /**
     * ｷｰﾌﾟ温度
     */
    private Long keepondo;

    /**
     * 後外観確認
     */
    private Integer atogaikan;

    /**
     * 回収セッタ枚数
     */
    private Long kaishusettasuu;

    /**
     * 開始日時
     */
    private Timestamp kaisinichiji;

    /**
     * 開始担当者
     */
    private String StartTantosyacode;

    /**
     * 開始確認者
     */
    private String StartKakuninsyacode;

    /**
     * 終了日時
     */
    private Timestamp syuuryounichiji;

    /**
     * 終了担当者
     */
    private String EndTantosyacode;

    /**
     * 備考1
     */
    private String bikou1;

    /**
     * 備考2
     */
    private String bikou2;

    /**
     * 登録日時
     */
    private Timestamp tourokunichiji;

    /**
     * 更新日時
     */
    private Timestamp koushinnichiji;

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
     * 実績No
     * @return the jissekino
     */
    public Integer getJissekino() {
        return jissekino;
    }

    /**
     * 実績No
     * @param jissekino the jissekino to set
     */
    public void setJissekino(Integer jissekino) {
        this.jissekino = jissekino;
    }

    /**
     * KCPNo
     * @return the KCPNO
     */
    public String getKCPNO() {
        return KCPNO;
    }

    /**
     * KCPNo
     * @param KCPNO the KCPNO to set
     */
    public void setKCPNO(String KCPNO) {
        this.KCPNO = KCPNO;
    }

    /**
     * 受入セッタ枚数
     * @return the ukeiresettamaisuu
     */
    public Long getUkeiresettamaisuu() {
        return ukeiresettamaisuu;
    }

    /**
     * 受入セッタ枚数
     * @param ukeiresettamaisuu the ukeiresettamaisuu to set
     */
    public void setUkeiresettamaisuu(Long ukeiresettamaisuu) {
        this.ukeiresettamaisuu = ukeiresettamaisuu;
    }

    /**
     * 指定再酸化回数
     * @return the siteisaisaka
     */
    public Long getSiteisaisaka() {
        return siteisaisaka;
    }

    /**
     * 指定再酸化回数
     * @param siteisaisaka the siteisaisaka to set
     */
    public void setSiteisaisaka(Long siteisaisaka) {
        this.siteisaisaka = siteisaisaka;
    }

    /**
     * 投入セッタ枚数
     * @return the tounyusettasuu
     */
    public Long getTounyusettasuu() {
        return tounyusettasuu;
    }

    /**
     * 投入セッタ枚数
     * @param tounyusettasuu the tounyusettasuu to set
     */
    public void setTounyusettasuu(Long tounyusettasuu) {
        this.tounyusettasuu = tounyusettasuu;
    }

    /**
     * 号機
     * @return the gouki
     */
    public String getGouki() {
        return gouki;
    }

    /**
     * 号機
     * @param gouki the gouki to set
     */
    public void setGouki(String gouki) {
        this.gouki = gouki;
    }

    /**
     * 設定ﾊﾟﾀｰﾝ
     * @return the setteipattern
     */
    public Long getSetteipattern() {
        return setteipattern;
    }

    /**
     * 設定ﾊﾟﾀｰﾝ
     * @param setteipattern the setteipattern to set
     */
    public void setSetteipattern(Long setteipattern) {
        this.setteipattern = setteipattern;
    }

    /**
     * ｷｰﾌﾟ温度
     * @return the keepondo
     */
    public Long getKeepondo() {
        return keepondo;
    }

    /**
     * ｷｰﾌﾟ温度
     * @param keepondo the keepondo to set
     */
    public void setKeepondo(Long keepondo) {
        this.keepondo = keepondo;
    }

    /**
     * 後外観確認
     * @return the atogaikan
     */
    public Integer getAtogaikan() {
        return atogaikan;
    }

    /**
     * 後外観確認
     * @param atogaikan the atogaikan to set
     */
    public void setAtogaikan(Integer atogaikan) {
        this.atogaikan = atogaikan;
    }

    /**
     * 回収セッタ枚数
     * @return the kaishusettasuu
     */
    public Long getKaishusettasuu() {
        return kaishusettasuu;
    }

    /**
     * 回収セッタ枚数
     * @param kaishusettasuu the kaishusettasuu to set
     */
    public void setKaishusettasuu(Long kaishusettasuu) {
        this.kaishusettasuu = kaishusettasuu;
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
     * 開始担当者
     * @return the StartTantosyacode
     */
    public String getStartTantosyacode() {
        return StartTantosyacode;
    }

    /**
     * 開始担当者
     * @param StartTantosyacode the StartTantosyacode to set
     */
    public void setStartTantosyacode(String StartTantosyacode) {
        this.StartTantosyacode = StartTantosyacode;
    }

    /**
     * 開始確認者
     * @return the StartKakuninsyacode
     */
    public String getStartKakuninsyacode() {
        return StartKakuninsyacode;
    }

    /**
     * 開始確認者
     * @param StartKakuninsyacode the StartKakuninsyacode to set
     */
    public void setStartKakuninsyacode(String StartKakuninsyacode) {
        this.StartKakuninsyacode = StartKakuninsyacode;
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
     * 終了担当者
     * @return the EndTantosyacode
     */
    public String getEndTantosyacode() {
        return EndTantosyacode;
    }

    /**
     * 終了担当者
     * @param EndTantosyacode the EndTantosyacode to set
     */
    public void setEndTantosyacode(String EndTantosyacode) {
        this.EndTantosyacode = EndTantosyacode;
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
     * 登録日時
     * @return the tourokunichiji
     */
    public Timestamp getTourokunichiji() {
        return tourokunichiji;
    }

    /**
     * 登録日時
     * @param tourokunichiji the tourokunichiji to set
     */
    public void setTourokunichiji(Timestamp tourokunichiji) {
        this.tourokunichiji = tourokunichiji;
    }

    /**
     * 更新日時
     * @return the koushinnichiji
     */
    public Timestamp getKoushinnichiji() {
        return koushinnichiji;
    }

    /**
     * 更新日時
     * @param koushinnichiji the koushinnichiji to set
     */
    public void setKoushinnichiji(Timestamp koushinnichiji) {
        this.koushinnichiji = koushinnichiji;
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
