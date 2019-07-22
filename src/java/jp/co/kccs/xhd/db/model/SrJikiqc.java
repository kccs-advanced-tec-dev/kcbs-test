/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/06/28<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SR_JIKIQC(磁器QC)のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2019/06/28
 */
public class SrJikiqc {
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
     * KCPNO
     */
    private String kcpno;

    /**
     * 合否判定
     */
    private String gouhihantei;

    /**
     * チェック日
     */
    private Timestamp checkbi;

    /**
     * チェック担当者
     */
    private String checktantousyacode;

    /**
     * 後工程指示内容1
     */
    private String sijinaiyou1;

    /**
     * 後工程指示内容2
     */
    private String sijinaiyou2;

    /**
     * 後工程指示内容6
     */
    private String sijinaiyou6;

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
    private Integer deleteflag;

    /**
     * @return kojyo
     */
    public String getKojyo() {
        return kojyo;
    }

    /**
     * @param kojyo セットする kojyo
     */
    public void setKojyo(String kojyo) {
        this.kojyo = kojyo;
    }

    /**
     * @return lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * @param lotno セットする lotno
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * @return edaban
     */
    public String getEdaban() {
        return edaban;
    }

    /**
     * @param edaban セットする edaban
     */
    public void setEdaban(String edaban) {
        this.edaban = edaban;
    }

    /**
     * @return jissekino
     */
    public Integer getJissekino() {
        return jissekino;
    }

    /**
     * @param jissekino セットする jissekino
     */
    public void setJissekino(Integer jissekino) {
        this.jissekino = jissekino;
    }

    /**
     * @return kcpno
     */
    public String getKcpno() {
        return kcpno;
    }

    /**
     * @param kcpno セットする kcpno
     */
    public void setKcpno(String kcpno) {
        this.kcpno = kcpno;
    }

    /**
     * @return gouhihantei
     */
    public String getGouhihantei() {
        return gouhihantei;
    }

    /**
     * @param gouhihantei セットする gouhihantei
     */
    public void setGouhihantei(String gouhihantei) {
        this.gouhihantei = gouhihantei;
    }

    /**
     * @return checkbi
     */
    public Timestamp getCheckbi() {
        return checkbi;
    }

    /**
     * @param checkbi セットする checkbi
     */
    public void setCheckbi(Timestamp checkbi) {
        this.checkbi = checkbi;
    }

    /**
     * @return checktantousyacode
     */
    public String getChecktantousyacode() {
        return checktantousyacode;
    }

    /**
     * @param checktantousyacode セットする checktantousyacode
     */
    public void setChecktantousyacode(String checktantousyacode) {
        this.checktantousyacode = checktantousyacode;
    }

    /**
     * @return sijinaiyou1
     */
    public String getSijinaiyou1() {
        return sijinaiyou1;
    }

    /**
     * @param sijinaiyou1 セットする sijinaiyou1
     */
    public void setSijinaiyou1(String sijinaiyou1) {
        this.sijinaiyou1 = sijinaiyou1;
    }

    /**
     * @return sijinaiyou2
     */
    public String getSijinaiyou2() {
        return sijinaiyou2;
    }

    /**
     * @param sijinaiyou2 セットする sijinaiyou2
     */
    public void setSijinaiyou2(String sijinaiyou2) {
        this.sijinaiyou2 = sijinaiyou2;
    }

    /**
     * @return sijinaiyou6
     */
    public String getSijinaiyou6() {
        return sijinaiyou6;
    }

    /**
     * @param sijinaiyou6 セットする sijinaiyou6
     */
    public void setSijinaiyou6(String sijinaiyou6) {
        this.sijinaiyou6 = sijinaiyou6;
    }

    /**
     * @return torokunichiji
     */
    public Timestamp getTorokunichiji() {
        return torokunichiji;
    }

    /**
     * @param torokunichiji セットする torokunichiji
     */
    public void setTorokunichiji(Timestamp torokunichiji) {
        this.torokunichiji = torokunichiji;
    }

    /**
     * @return kosinnichiji
     */
    public Timestamp getKosinnichiji() {
        return kosinnichiji;
    }

    /**
     * @param kosinnichiji セットする kosinnichiji
     */
    public void setKosinnichiji(Timestamp kosinnichiji) {
        this.kosinnichiji = kosinnichiji;
    }

    /**
     * @return revision
     */
    public Integer getRevision() {
        return revision;
    }

    /**
     * @param revision セットする revision
     */
    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    /**
     * @return deleteflag
     */
    public Integer getDeleteflag() {
        return deleteflag;
    }

    /**
     * @param deleteflag セットする deleteflag
     */
    public void setDeleteflag(Integer deleteflag) {
        this.deleteflag = deleteflag;
    }
}