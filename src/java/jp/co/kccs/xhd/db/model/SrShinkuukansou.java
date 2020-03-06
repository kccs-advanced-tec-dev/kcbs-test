/*
 * Copyright 2020 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2020/02/10<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * sr_shinkuukansou:熱処理のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2020/02/10
 */
public class SrShinkuukansou {
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
     * ﾛｯﾄﾌﾟﾚ
     */
    private String lotpre;

    /**
     * KCPNO
     */
    private String kcpno;

    /**
     * 開始日時
     */
    private Timestamp syoribi;

    /**
     * 開始時間
     */
    private String kaishijikan;

    /**
     * 終了時間
     */
    private String syuuryoujikan;

    /**
     * 開始担当者
     */
    private String sagyosya;

    /**
     * 工程
     */
    private String koutei;

    /**
     * 号機
     */
    private String gouki;

    /**
     * 設定温度
     */
    private String setteiondo;

    /**
     * 設定時間
     */
    private String setteijikan;

    /**
     * 回数
     */
    private Integer kaisuu;

    /**
     * 送り良品数
     */
    private Integer suuryo;

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
     * 客先
     */
    private String tokuisaki;

    /**
     * ﾛｯﾄ区分
     */
    private String lotkubuncode;

    /**
     * ｵｰﾅｰ
     */
    private String ownercode;

    /**
     * 受入れ単位重量
     */
    private BigDecimal ukeiretannijyuryo;

    /**
     * 受入れ総重量
     */
    private BigDecimal ukeiresoujyuryou;

    /**
     * 種類
     */
    private String syurui;

    /**
     * 開始確認者
     */
    private String startkakunin;

    /**
     * 終了日時
     */
    private Timestamp syuryonichiji;
    
    /**
     * 終了担当者
     */
    private String endtantou;
    
    /**
     * 検査場所
     */
    private String kensabasyo;
    
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
     * @return lotpre
     */
    public String getLotpre() {
        return lotpre;
    }

    /**
     * @param lotpre セットする lotpre
     */
    public void setLotpre(String lotpre) {
        this.lotpre = lotpre;
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
     * @return syoribi
     */
    public Timestamp getSyoribi() {
        return syoribi;
    }

    /**
     * @param syoribi セットする syoribi
     */
    public void setSyoribi(Timestamp syoribi) {
        this.syoribi = syoribi;
    }

    /**
     * @return kaishijikan
     */
    public String getKaishijikan() {
        return kaishijikan;
    }

    /**
     * @param kaishijikan セットする kaishijikan
     */
    public void setKaishijikan(String kaishijikan) {
        this.kaishijikan = kaishijikan;
    }

    /**
     * @return syuuryoujikan
     */
    public String getSyuuryoujikan() {
        return syuuryoujikan;
    }

    /**
     * @param syuuryoujikan セットする syuuryoujikan
     */
    public void setSyuuryoujikan(String syuuryoujikan) {
        this.syuuryoujikan = syuuryoujikan;
    }

    /**
     * @return sagyosya
     */
    public String getSagyosya() {
        return sagyosya;
    }

    /**
     * @param sagyosya セットする sagyosya
     */
    public void setSagyosya(String sagyosya) {
        this.sagyosya = sagyosya;
    }

    /**
     * @return koutei
     */
    public String getKoutei() {
        return koutei;
    }

    /**
     * @param koutei セットする koutei
     */
    public void setKoutei(String koutei) {
        this.koutei = koutei;
    }

    /**
     * @return gouki
     */
    public String getGouki() {
        return gouki;
    }

    /**
     * @param gouki セットする gouki
     */
    public void setGouki(String gouki) {
        this.gouki = gouki;
    }

    /**
     * @return setteiondo
     */
    public String getSetteiondo() {
        return setteiondo;
    }

    /**
     * @param setteiondo セットする setteiondo
     */
    public void setSetteiondo(String setteiondo) {
        this.setteiondo = setteiondo;
    }

    /**
     * @return setteijikan
     */
    public String getSetteijikan() {
        return setteijikan;
    }

    /**
     * @param setteijikan セットする setteijikan
     */
    public void setSetteijikan(String setteijikan) {
        this.setteijikan = setteijikan;
    }

    /**
     * @return kaisuu
     */
    public Integer getKaisuu() {
        return kaisuu;
    }

    /**
     * @param kaisuu セットする kaisuu
     */
    public void setKaisuu(Integer kaisuu) {
        this.kaisuu = kaisuu;
    }

    /**
     * @return suuryo
     */
    public Integer getSuuryo() {
        return suuryo;
    }

    /**
     * @param suuryo セットする suuryo
     */
    public void setSuuryo(Integer suuryo) {
        this.suuryo = suuryo;
    }

    /**
     * @return bikou1
     */
    public String getBikou1() {
        return bikou1;
    }

    /**
     * @param bikou1 セットする bikou1
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
     * @param bikou2 セットする bikou2
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
     * @param bikou3 セットする bikou3
     */
    public void setBikou3(String bikou3) {
        this.bikou3 = bikou3;
    }

    /**
     * @return tokuisaki
     */
    public String getTokuisaki() {
        return tokuisaki;
    }

    /**
     * @param tokuisaki セットする tokuisaki
     */
    public void setTokuisaki(String tokuisaki) {
        this.tokuisaki = tokuisaki;
    }

    /**
     * @return lotkubuncode
     */
    public String getLotkubuncode() {
        return lotkubuncode;
    }

    /**
     * @param lotkubuncode セットする lotkubuncode
     */
    public void setLotkubuncode(String lotkubuncode) {
        this.lotkubuncode = lotkubuncode;
    }

    /**
     * @return ownercode
     */
    public String getOwnercode() {
        return ownercode;
    }

    /**
     * @param ownercode セットする ownercode
     */
    public void setOwnercode(String ownercode) {
        this.ownercode = ownercode;
    }

    /**
     * @return ukeiretannijyuryo
     */
    public BigDecimal getUkeiretannijyuryo() {
        return ukeiretannijyuryo;
    }

    /**
     * @param ukeiretannijyuryo セットする ukeiretannijyuryo
     */
    public void setUkeiretannijyuryo(BigDecimal ukeiretannijyuryo) {
        this.ukeiretannijyuryo = ukeiretannijyuryo;
    }

    /**
     * @return ukeiresoujyuryou
     */
    public BigDecimal getUkeiresoujyuryou() {
        return ukeiresoujyuryou;
    }

    /**
     * @param ukeiresoujyuryou セットする ukeiresoujyuryou
     */
    public void setUkeiresoujyuryou(BigDecimal ukeiresoujyuryou) {
        this.ukeiresoujyuryou = ukeiresoujyuryou;
    }

    /**
     * @return syurui
     */
    public String getSyurui() {
        return syurui;
    }

    /**
     * @param syurui セットする syurui
     */
    public void setSyurui(String syurui) {
        this.syurui = syurui;
    }

    /**
     * @return startkakunin
     */
    public String getStartkakunin() {
        return startkakunin;
    }

    /**
     * @param startkakunin セットする startkakunin
     */
    public void setStartkakunin(String startkakunin) {
        this.startkakunin = startkakunin;
    }

    /**
     * @return syuryonichiji
     */
    public Timestamp getSyuryonichiji() {
        return syuryonichiji;
    }

    /**
     * @param syuryonichiji セットする syuryonichiji
     */
    public void setSyuryonichiji(Timestamp syuryonichiji) {
        this.syuryonichiji = syuryonichiji;
    }

    /**
     * @return endtantou
     */
    public String getEndtantou() {
        return endtantou;
    }

    /**
     * @param endtantou セットする endtantou
     */
    public void setEndtantou(String endtantou) {
        this.endtantou = endtantou;
    }

    /**
     * 検査場所
     * @return the kensabasyo
     */
    public String getKensabasyo() {
        return kensabasyo;
    }

    /**
     * 検査場所
     * @param kensabasyo the kensabasyo to set
     */
    public void setKensabasyo(String kensabasyo) {
        this.kensabasyo = kensabasyo;
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