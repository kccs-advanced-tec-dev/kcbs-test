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
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日       2019/08/06<br>
 * 計画書No     K1811-DS001<br>
 * 変更者       KCSS K.Jo<br>
 * 変更理由     新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * ﾌﾟﾚｽ・ﾒｶﾌﾟﾚｽ履歴検索画面のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2019/08/06
 */
public class GXHDO201B009Model implements Serializable {

    /**
     * ﾛｯﾄNo.
     */
    private String lotno = "";

    /**
     * KCPNO
     */
    private String kcpno = "";

    /**
     * 開始日時
     */
    private Timestamp kaisinichiji = null;

    /**
     * 終了日時
     */
    private Timestamp syuryonichiji = null;

    /**
     * 受入ｾｯﾄ数
     */
    private Integer ukeiresetsuu = null;

    /**
     * 良品ｾｯﾄ数
     */
    private Integer ryouhinsetsuu = null;

    /**
     * 号機ｺｰﾄﾞ
     */
    private String goki = "";

    /**
     * 温度
     */
    private BigDecimal ondo = null;

    /**
     * 圧力
     */
    private Integer aturyoku = null;

    /**
     * 室温
     */
    private BigDecimal situon = null;

    /**
     * 湿度
     */
    private BigDecimal situdo = null;

    /**
     * 厚みMIN
     */
    private Integer atumimin = null;

    /**
     * 厚みMAX
     */
    private Integer atumimax = null;

    /**
     * 厚み担当者
     */
    private String atumitantosya = "";

    /**
     * 担当者
     */
    private String tantosya = "";

    /**
     * 確認者
     */
    private String kakuninsya = "";

    /**
     * 備考1
     */
    private String biko1 = "";

    /**
     * 備考2
     */
    private String biko2 = "";

    /**
     * Air抜き
     */
    private Integer airnuki = null;

    /**
     * 脱気時間
     */
    private Integer dakkijikan = null;

    /**
     * 脱気圧力
     */
    private Integer dakkiaturyoku = null;

    /**
     * 終了担当者
     */
    private String endtantousyacode = "";

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
     * @return kaisinichiji
     */
    public Timestamp getKaisinichiji() {
        return kaisinichiji;
    }

    /**
     * @param kaisinichiji セットする kaisinichiji
     */
    public void setKaisinichiji(Timestamp kaisinichiji) {
        this.kaisinichiji = kaisinichiji;
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
     * @return ukeiresetsuu
     */
    public Integer getUkeiresetsuu() {
        return ukeiresetsuu;
    }

    /**
     * @param ukeiresetsuu セットする ukeiresetsuu
     */
    public void setUkeiresetsuu(Integer ukeiresetsuu) {
        this.ukeiresetsuu = ukeiresetsuu;
    }

    /**
     * @return ryouhinsetsuu
     */
    public Integer getRyouhinsetsuu() {
        return ryouhinsetsuu;
    }

    /**
     * @param ryouhinsetsuu セットする ryouhinsetsuu
     */
    public void setRyouhinsetsuu(Integer ryouhinsetsuu) {
        this.ryouhinsetsuu = ryouhinsetsuu;
    }

    /**
     * @return goki
     */
    public String getGoki() {
        return goki;
    }

    /**
     * @param goki セットする goki
     */
    public void setGoki(String goki) {
        this.goki = goki;
    }

    /**
     * @return ondo
     */
    public BigDecimal getOndo() {
        return ondo;
    }

    /**
     * @param ondo セットする ondo
     */
    public void setOndo(BigDecimal ondo) {
        this.ondo = ondo;
    }

    /**
     * @return aturyoku
     */
    public Integer getAturyoku() {
        return aturyoku;
    }

    /**
     * @param aturyoku セットする aturyoku
     */
    public void setAturyoku(Integer aturyoku) {
        this.aturyoku = aturyoku;
    }

    /**
     * @return situon
     */
    public BigDecimal getSituon() {
        return situon;
    }

    /**
     * @param situon セットする situon
     */
    public void setSituon(BigDecimal situon) {
        this.situon = situon;
    }

    /**
     * @return situdo
     */
    public BigDecimal getSitudo() {
        return situdo;
    }

    /**
     * @param situdo セットする situdo
     */
    public void setSitudo(BigDecimal situdo) {
        this.situdo = situdo;
    }

    /**
     * @return atumimin
     */
    public Integer getAtumimin() {
        return atumimin;
    }

    /**
     * @param atumimin セットする atumimin
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
     * @param atumimax セットする atumimax
     */
    public void setAtumimax(Integer atumimax) {
        this.atumimax = atumimax;
    }

    /**
     * @return atumitantosya
     */
    public String getAtumitantosya() {
        return atumitantosya;
    }

    /**
     * @param atumitantosya セットする atumitantosya
     */
    public void setAtumitantosya(String atumitantosya) {
        this.atumitantosya = atumitantosya;
    }

    /**
     * @return tantosya
     */
    public String getTantosya() {
        return tantosya;
    }

    /**
     * @param tantosya セットする tantosya
     */
    public void setTantosya(String tantosya) {
        this.tantosya = tantosya;
    }

    /**
     * @return kakuninsya
     */
    public String getKakuninsya() {
        return kakuninsya;
    }

    /**
     * @param kakuninsya セットする kakuninsya
     */
    public void setKakuninsya(String kakuninsya) {
        this.kakuninsya = kakuninsya;
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
     * @return airnuki
     */
    public Integer getAirnuki() {
        return airnuki;
    }

    /**
     * @param airnuki セットする airnuki
     */
    public void setAirnuki(Integer airnuki) {
        this.airnuki = airnuki;
    }

    /**
     * @return dakkijikan
     */
    public Integer getDakkijikan() {
        return dakkijikan;
    }

    /**
     * @param dakkijikan セットする dakkijikan
     */
    public void setDakkijikan(Integer dakkijikan) {
        this.dakkijikan = dakkijikan;
    }

    /**
     * @return dakkiaturyoku
     */
    public Integer getDakkiaturyoku() {
        return dakkiaturyoku;
    }

    /**
     * @param dakkiaturyoku セットする dakkiaturyoku
     */
    public void setDakkiaturyoku(Integer dakkiaturyoku) {
        this.dakkiaturyoku = dakkiaturyoku;
    }

    /**
     * @return endtantousyacode
     */
    public String getEndtantousyacode() {
        return endtantousyacode;
    }

    /**
     * @param endtantousyacode セットする endtantousyacode
     */
    public void setEndtantousyacode(String endtantousyacode) {
        this.endtantousyacode = endtantousyacode;
    }
}
