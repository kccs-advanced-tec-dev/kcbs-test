/*
 * Copyright 2020 Kyocera Communication Systems Co., Ltd All rights reserved.
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
 * 変更日	2020/03/09<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	863 F.Zhang<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 電気特性・熱処理履歴検索画面のモデルクラスです。
 *
 * @author 863 F.Zhang
 * @since 2020/03/09
 */
public class GXHDO201B043Model implements Serializable {

    /**
     * ﾛｯﾄNo
     */
    private String lotno;

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
     * ﾛｯﾄNo
     *
     * @return lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * ﾛｯﾄNo
     *
     * @param lotno セットする lotno
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * ﾛｯﾄﾌﾟﾚ
     *
     * @return lotpre
     */
    public String getLotpre() {
        return lotpre;
    }

    /**
     * ﾛｯﾄﾌﾟﾚ
     *
     * @param lotpre セットする lotpre
     */
    public void setLotpre(String lotpre) {
        this.lotpre = lotpre;
    }

    /**
     * KCPNO
     *
     * @return kcpno
     */
    public String getKcpno() {
        return kcpno;
    }

    /**
     * KCPNO
     *
     * @param kcpno セットする kcpno
     */
    public void setKcpno(String kcpno) {
        this.kcpno = kcpno;
    }

    /**
     * 開始日時
     *
     * @return syoribi
     */
    public Timestamp getSyoribi() {
        return syoribi;
    }

    /**
     * 開始日時
     *
     * @param syoribi セットする syoribi
     */
    public void setSyoribi(Timestamp syoribi) {
        this.syoribi = syoribi;
    }

    /**
     * 開始時間
     *
     * @return kaishijikan
     */
    public String getKaishijikan() {
        return kaishijikan;
    }

    /**
     * 開始時間
     *
     * @param kaishijikan セットする kaishijikan
     */
    public void setKaishijikan(String kaishijikan) {
        this.kaishijikan = kaishijikan;
    }

    /**
     * 終了時間
     *
     * @return syuuryoujikan
     */
    public String getSyuuryoujikan() {
        return syuuryoujikan;
    }

    /**
     * 終了時間
     *
     * @param syuuryoujikan セットする syuuryoujikan
     */
    public void setSyuuryoujikan(String syuuryoujikan) {
        this.syuuryoujikan = syuuryoujikan;
    }

    /**
     * 開始担当者
     *
     * @return sagyosya
     */
    public String getSagyosya() {
        return sagyosya;
    }

    /**
     * 開始担当者
     *
     * @param sagyosya セットする sagyosya
     */
    public void setSagyosya(String sagyosya) {
        this.sagyosya = sagyosya;
    }

    /**
     * 工程
     *
     * @return koutei
     */
    public String getKoutei() {
        return koutei;
    }

    /**
     * 工程
     *
     * @param koutei セットする koutei
     */
    public void setKoutei(String koutei) {
        this.koutei = koutei;
    }

    /**
     * 号機
     *
     * @return gouki
     */
    public String getGouki() {
        return gouki;
    }

    /**
     * 号機
     *
     * @param gouki セットする gouki
     */
    public void setGouki(String gouki) {
        this.gouki = gouki;
    }

    /**
     * 設定温度
     *
     * @return setteiondo
     */
    public String getSetteiondo() {
        return setteiondo;
    }

    /**
     * 設定温度
     *
     * @param setteiondo セットする setteiondo
     */
    public void setSetteiondo(String setteiondo) {
        this.setteiondo = setteiondo;
    }

    /**
     * 設定時間
     *
     * @return setteijikan
     */
    public String getSetteijikan() {
        return setteijikan;
    }

    /**
     * 設定時間
     *
     * @param setteijikan セットする setteijikan
     */
    public void setSetteijikan(String setteijikan) {
        this.setteijikan = setteijikan;
    }

    /**
     * 回数
     *
     * @return kaisuu
     */
    public Integer getKaisuu() {
        return kaisuu;
    }

    /**
     * 回数
     *
     * @param kaisuu セットする kaisuu
     */
    public void setKaisuu(Integer kaisuu) {
        this.kaisuu = kaisuu;
    }

    /**
     * 送り良品数
     *
     * @return suuryo
     */
    public Integer getSuuryo() {
        return suuryo;
    }

    /**
     * 送り良品数
     *
     * @param suuryo セットする suuryo
     */
    public void setSuuryo(Integer suuryo) {
        this.suuryo = suuryo;
    }

    /**
     * 備考1
     *
     * @return bikou1
     */
    public String getBikou1() {
        return bikou1;
    }

    /**
     * 備考1
     *
     * @param bikou1 セットする bikou1
     */
    public void setBikou1(String bikou1) {
        this.bikou1 = bikou1;
    }

    /**
     * 備考2
     *
     * @return bikou2
     */
    public String getBikou2() {
        return bikou2;
    }

    /**
     * 備考2
     *
     * @param bikou2 セットする bikou2
     */
    public void setBikou2(String bikou2) {
        this.bikou2 = bikou2;
    }

    /**
     * 備考3
     *
     * @return bikou3
     */
    public String getBikou3() {
        return bikou3;
    }

    /**
     * 備考3
     *
     * @param bikou3 セットする bikou3
     */
    public void setBikou3(String bikou3) {
        this.bikou3 = bikou3;
    }

    /**
     * 客先
     *
     * @return tokuisaki
     */
    public String getTokuisaki() {
        return tokuisaki;
    }

    /**
     * 客先
     *
     * @param tokuisaki セットする tokuisaki
     */
    public void setTokuisaki(String tokuisaki) {
        this.tokuisaki = tokuisaki;
    }

    /**
     * ﾛｯﾄ区分
     *
     * @return lotkubuncode
     */
    public String getLotkubuncode() {
        return lotkubuncode;
    }

    /**
     * ﾛｯﾄ区分
     *
     * @param lotkubuncode セットする lotkubuncode
     */
    public void setLotkubuncode(String lotkubuncode) {
        this.lotkubuncode = lotkubuncode;
    }

    /**
     * ｵｰﾅｰ
     *
     * @return ownercode
     */
    public String getOwnercode() {
        return ownercode;
    }

    /**
     * ｵｰﾅｰ
     *
     * @param ownercode セットする ownercode
     */
    public void setOwnercode(String ownercode) {
        this.ownercode = ownercode;
    }

    /**
     * 受入れ単位重量
     *
     * @return ukeiretannijyuryo
     */
    public BigDecimal getUkeiretannijyuryo() {
        return ukeiretannijyuryo;
    }

    /**
     * 受入れ単位重量
     *
     * @param ukeiretannijyuryo セットする ukeiretannijyuryo
     */
    public void setUkeiretannijyuryo(BigDecimal ukeiretannijyuryo) {
        this.ukeiretannijyuryo = ukeiretannijyuryo;
    }

    /**
     * 受入れ総重量
     *
     * @return ukeiresoujyuryou
     */
    public BigDecimal getUkeiresoujyuryou() {
        return ukeiresoujyuryou;
    }

    /**
     * 受入れ総重量
     *
     * @param ukeiresoujyuryou セットする ukeiresoujyuryou
     */
    public void setUkeiresoujyuryou(BigDecimal ukeiresoujyuryou) {
        this.ukeiresoujyuryou = ukeiresoujyuryou;
    }

    /**
     * 種類
     *
     * @return syurui
     */
    public String getSyurui() {
        return syurui;
    }

    /**
     * 種類
     *
     * @param syurui セットする syurui
     */
    public void setSyurui(String syurui) {
        this.syurui = syurui;
    }

    /**
     * 開始確認者
     *
     * @return startkakunin
     */
    public String getStartkakunin() {
        return startkakunin;
    }

    /**
     * 開始確認者
     *
     * @param startkakunin セットする startkakunin
     */
    public void setStartkakunin(String startkakunin) {
        this.startkakunin = startkakunin;
    }

    /**
     * 終了日時
     *
     * @return syuryonichiji
     */
    public Timestamp getSyuryonichiji() {
        return syuryonichiji;
    }

    /**
     * 終了日時
     *
     * @param syuryonichiji セットする syuryonichiji
     */
    public void setSyuryonichiji(Timestamp syuryonichiji) {
        this.syuryonichiji = syuryonichiji;
    }

    /**
     * 終了担当者
     *
     * @return endtantou
     */
    public String getEndtantou() {
        return endtantou;
    }

    /**
     * 終了担当者
     *
     * @param endtantou セットする endtantou
     */
    public void setEndtantou(String endtantou) {
        this.endtantou = endtantou;
    }

    /**
     * 検査場所
     *
     * @return the kensabasyo
     */
    public String getKensabasyo() {
        return kensabasyo;
    }

    /**
     * 検査場所
     *
     * @param kensabasyo the kensabasyo to set
     */
    public void setKensabasyo(String kensabasyo) {
        this.kensabasyo = kensabasyo;
    }

}
