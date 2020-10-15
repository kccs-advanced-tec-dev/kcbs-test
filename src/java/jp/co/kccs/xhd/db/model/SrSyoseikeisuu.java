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
 * 変更日	2019/07/22<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * <br>
 * 変更日	2020/09/18<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	863 sujialiang<br>
 * 変更理由	項目追加<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SR_SYOSEIKEISUU(計数)のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2019/07/22
 */
public class SrSyoseikeisuu {
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
     * 受入個数
     */
    private Integer ukeirekosuu;

    /**
     * 単位重量
     */
    private BigDecimal tanijyuryo;

    /**
     * 総重量
     */
    private BigDecimal soujuryou;

    /**
     * 良品個数
     */
    private Integer ryohinkosuu;

    /**
     * 計数日時
     */
    private Timestamp keinichiji;

    /**
     * 計数担当者
     */
    private String keitantosya;

    /**
     * 歩留まり
     */
    private BigDecimal budomari;

    /**
     * 備考1
     */
    private String biko1;

    /**
     * 備考2
     */
    private String biko2;

    /**
     * ﾊﾞﾚﾙ後判定
     */
    private String barrelgohantei;

    /**
     * 登録日時
     */
    private Timestamp torokunichiji;

    /**
     * 更新日時
     */
    private Timestamp kosinNichiji;

    /**
     * revision
     */
    private Integer revision;

    /**
     * 袋詰め担当者
     */
    private String fukurotantosya;

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
     * @return ukeirekosuu
     */
    public Integer getUkeirekosuu() {
        return ukeirekosuu;
    }

    /**
     * @param ukeirekosuu セットする ukeirekosuu
     */
    public void setUkeirekosuu(Integer ukeirekosuu) {
        this.ukeirekosuu = ukeirekosuu;
    }

    /**
     * @return tanijyuryo
     */
    public BigDecimal getTanijyuryo() {
        return tanijyuryo;
    }

    /**
     * @param tanijyuryo セットする tanijyuryo
     */
    public void setTanijyuryo(BigDecimal tanijyuryo) {
        this.tanijyuryo = tanijyuryo;
    }

    /**
     * @return soujuryou
     */
    public BigDecimal getSoujuryou() {
        return soujuryou;
    }

    /**
     * @param soujuryou セットする soujuryou
     */
    public void setSoujuryou(BigDecimal soujuryou) {
        this.soujuryou = soujuryou;
    }

    /**
     * @return ryohinkosuu
     */
    public Integer getRyohinkosuu() {
        return ryohinkosuu;
    }

    /**
     * @param ryohinkosuu セットする ryohinkosuu
     */
    public void setRyohinkosuu(Integer ryohinkosuu) {
        this.ryohinkosuu = ryohinkosuu;
    }

    /**
     * @return keinichiji
     */
    public Timestamp getKeinichiji() {
        return keinichiji;
    }

    /**
     * @param keinichiji セットする keinichiji
     */
    public void setKeinichiji(Timestamp keinichiji) {
        this.keinichiji = keinichiji;
    }

    /**
     * @return keitantosya
     */
    public String getKeitantosya() {
        return keitantosya;
    }

    /**
     * @param keitantosya セットする keitantosya
     */
    public void setKeitantosya(String keitantosya) {
        this.keitantosya = keitantosya;
    }

    /**
     * @return budomari
     */
    public BigDecimal getBudomari() {
        return budomari;
    }

    /**
     * @param budomari セットする budomari
     */
    public void setBudomari(BigDecimal budomari) {
        this.budomari = budomari;
    }

    /**
     * @return biko1
     */
    public String getBiko1() {
        return biko1;
    }

    /**
     * @param biko1 セットする biko1
     */
    public void setBiko1(String biko1) {
        this.biko1 = biko1;
    }

    /**
     * @return biko2
     */
    public String getBiko2() {
        return biko2;
    }

    /**
     * @param biko2 セットする biko2
     */
    public void setBiko2(String biko2) {
        this.biko2 = biko2;
    }

    /**
     * @return barrelgohantei
     */
    public String getBarrelgohantei() {
        return barrelgohantei;
    }

    /**
     * @param barrelgohantei セットする barrelgohantei
     */
    public void setBarrelgohantei(String barrelgohantei) {
        this.barrelgohantei = barrelgohantei;
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
     * @return kosinNichiji
     */
    public Timestamp getKosinNichiji() {
        return kosinNichiji;
    }

    /**
     * @param kosinNichiji セットする kosinNichiji
     */
    public void setKosinNichiji(Timestamp kosinNichiji) {
        this.kosinNichiji = kosinNichiji;
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
     * @return fukurotantosya
     */
    public String getFukurotantosya() {
        return fukurotantosya;
    }

    /**
     * @param fukurotantosya 
     */
    public void setFukurotantosya(String fukurotantosya) {
        this.fukurotantosya = fukurotantosya;
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