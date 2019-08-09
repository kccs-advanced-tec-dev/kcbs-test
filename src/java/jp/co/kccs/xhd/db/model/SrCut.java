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
 * 変更日	2019/05/24<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SR_CUT(ﾀﾞｲｼﾝｸﾞｶｯﾄ)のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2019/05/24
 */
public class SrCut {
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
     * ｶｯﾄ刃枚数
     */
    private Integer cutbamaisuu;

    /**
     * 号機ｺｰﾄﾞ
     */
    private String gouki;

    /**
     * ｶｯﾄﾃｰﾌﾞﾙ温度
     */
    private Integer cuttableondo;

    /**
     * 担当者
     */
    private String tantousya;

    /**
     * 確認者
     */
    private String kakuninsya;

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
     * 方式
     */
    private String housiki;

    /**
     * 厚みMIN
     */
    private Integer atumimin;

    /**
     * 厚みMAX
     */
    private Integer atumimax;

    /**
     * ｶｯﾄ刃種類
     */
    private String cutbashurui;

    /**
     * ｶｯﾄ向き
     */
    private String cutmuki;

    /**
     * 発泡ｼｰﾄ色
     */
    private String happosheetcolor;

    /**
     * 乾燥
     */
    private Integer kansou;

    /**
     * 補正判定
     */
    private Integer hoseihantei;

    /**
     * 終了担当者
     */
    private String endtantousyacode;

    /**
     * 処理ｾｯﾄ数
     */
    private Integer syorisetsuu;

    /**
     * 良品ｾｯﾄ数
     */
    private BigDecimal ryouhinsetsuu;

    /**
     * revision
     */
    private Integer revision;

    /**
     * 削除ﾌﾗｸﾞ
     */
    private Integer deleteflag;
    
    /**
     * KCPNO
     */
    private String kcpno;

    /**
     * @return kojyo
     */
    public String getKojyo() {
        return kojyo;
    }

    /**
     * @param kojyo kojyo
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
     * @param lotno lotno
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
     * @param edaban edaban
     */
    public void setEdaban(String edaban) {
        this.edaban = edaban;
    }

    /**
     * @return startdatetime
     */
    public Timestamp getStartdatetime() {
        return startdatetime;
    }

    /**
     * @param startdatetime startdatetime
     */
    public void setStartdatetime(Timestamp startdatetime) {
        this.startdatetime = startdatetime;
    }

    /**
     * @return enddatetime
     */
    public Timestamp getEnddatetime() {
        return enddatetime;
    }

    /**
     * @param enddatetime enddatetime
     */
    public void setEnddatetime(Timestamp enddatetime) {
        this.enddatetime = enddatetime;
    }

    /**
     * @return cutbamaisuu
     */
    public Integer getCutbamaisuu() {
        return cutbamaisuu;
    }

    /**
     * @param cutbamaisuu cutbamaisuu
     */
    public void setCutbamaisuu(Integer cutbamaisuu) {
        this.cutbamaisuu = cutbamaisuu;
    }

    /**
     * @return gouki
     */
    public String getGouki() {
        return gouki;
    }

    /**
     * @param gouki gouki
     */
    public void setGouki(String gouki) {
        this.gouki = gouki;
    }

    /**
     * @return cuttableondo
     */
    public Integer getCuttableondo() {
        return cuttableondo;
    }

    /**
     * @param cuttableondo cuttableondo
     */
    public void setCuttableondo(Integer cuttableondo) {
        this.cuttableondo = cuttableondo;
    }

    /**
     * @return tantousya
     */
    public String getTantousya() {
        return tantousya;
    }

    /**
     * @param tantousya tantousya
     */
    public void setTantousya(String tantousya) {
        this.tantousya = tantousya;
    }

    /**
     * @return kakuninsya
     */
    public String getKakuninsya() {
        return kakuninsya;
    }

    /**
     * @param kakuninsya kakuninsya
     */
    public void setKakuninsya(String kakuninsya) {
        this.kakuninsya = kakuninsya;
    }

    /**
     * @return bikou1
     */
    public String getBikou1() {
        return bikou1;
    }

    /**
     * @param bikou1 bikou1
     */
    public void setBikou1(String bikou1) {
        this.bikou1 = bikou1;
    }

    /**
     * @return bikou2
     */
    public String getBikou2() {
        return bikou2;
    }

    /**
     * @param bikou2 bikou2
     */
    public void setBikou2(String bikou2) {
        this.bikou2 = bikou2;
    }

    /**
     * @return bikou3
     */
    public String getBikou3() {
        return bikou3;
    }

    /**
     * @param bikou3 bikou3
     */
    public void setBikou3(String bikou3) {
        this.bikou3 = bikou3;
    }

    /**
     * @return bikou4
     */
    public String getBikou4() {
        return bikou4;
    }

    /**
     * @param bikou4 bikou4
     */
    public void setBikou4(String bikou4) {
        this.bikou4 = bikou4;
    }

    /**
     * @return bikou5
     */
    public String getBikou5() {
        return bikou5;
    }

    /**
     * @param bikou5 bikou5
     */
    public void setBikou5(String bikou5) {
        this.bikou5 = bikou5;
    }

    /**
     * @return housiki
     */
    public String getHousiki() {
        return housiki;
    }

    /**
     * @param housiki housiki
     */
    public void setHousiki(String housiki) {
        this.housiki = housiki;
    }

    /**
     * @return atumimin
     */
    public Integer getAtumimin() {
        return atumimin;
    }

    /**
     * @param atumimin atumimin
     */
    public void setAtumimin(Integer atumimin) {
        this.atumimin = atumimin;
    }

    /**
     * @return atumimax
     */
    public Integer getAtumimax() {
        return atumimax;
    }

    /**
     * @param atumimax atumimax
     */
    public void setAtumimax(Integer atumimax) {
        this.atumimax = atumimax;
    }

    /**
     * @return cutbashurui
     */
    public String getCutbashurui() {
        return cutbashurui;
    }

    /**
     * @param cutbashurui cutbashurui
     */
    public void setCutbashurui(String cutbashurui) {
        this.cutbashurui = cutbashurui;
    }

    /**
     * @return cutmuki
     */
    public String getCutmuki() {
        return cutmuki;
    }

    /**
     * @param cutmuki cutmuki
     */
    public void setCutmuki(String cutmuki) {
        this.cutmuki = cutmuki;
    }

    /**
     * @return happosheetcolor
     */
    public String getHapposheetcolor() {
        return happosheetcolor;
    }

    /**
     * @param happosheetcolor happosheetcolor
     */
    public void setHapposheetcolor(String happosheetcolor) {
        this.happosheetcolor = happosheetcolor;
    }

    /**
     * @return kansou
     */
    public Integer getKansou() {
        return kansou;
    }

    /**
     * @param kansou kansou
     */
    public void setKansou(Integer kansou) {
        this.kansou = kansou;
    }

    /**
     * @return hoseihantei
     */
    public Integer getHoseihantei() {
        return hoseihantei;
    }

    /**
     * @param hoseihantei hoseihantei
     */
    public void setHoseihantei(Integer hoseihantei) {
        this.hoseihantei = hoseihantei;
    }

    /**
     * @return endtantousyacode
     */
    public String getEndtantousyacode() {
        return endtantousyacode;
    }

    /**
     * @param endtantousyacode endtantousyacode
     */
    public void setEndtantousyacode(String endtantousyacode) {
        this.endtantousyacode = endtantousyacode;
    }

    /**
     * @return syorisetsuu
     */
    public Integer getSyorisetsuu() {
        return syorisetsuu;
    }

    /**
     * @param syorisetsuu syorisetsuu
     */
    public void setSyorisetsuu(Integer syorisetsuu) {
        this.syorisetsuu = syorisetsuu;
    }

    /**
     * @return ryouhinsetsuu
     */
    public BigDecimal getRyouhinsetsuu() {
        return ryouhinsetsuu;
    }

    /**
     * @param ryouhinsetsuu ryouhinsetsuu
     */
    public void setRyouhinsetsuu(BigDecimal ryouhinsetsuu) {
        this.ryouhinsetsuu = ryouhinsetsuu;
    }

    /**
     * @return revision
     */
    public Integer getRevision() {
        return revision;
    }

    /**
     * @param revision revision
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
     * @param deleteflag deleteflag
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
