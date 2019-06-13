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
 * メカプレスのモデルクラスです。
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2019/05/22
 */
public class SrMekapress {

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
     * 受入ｾｯﾄ数
     */
    private Integer ukeiresetsuu;

    /**
     * 良品ｾｯﾄ数
     */
    private Integer ryouhinsetsuu;

    /**
     * 号機ｺｰﾄﾞ
     */
    private String goki;

    /**
     * 温度
     */
    private BigDecimal ondo;

    /**
     * 圧力
     */
    private Integer aturyoku;

    /**
     * 室温
     */
    private BigDecimal situon;

    /**
     * 湿度
     */
    private BigDecimal situdo;

    /**
     * 厚みMIN
     */
    private Integer atumimin;

    /**
     * 厚みMAX
     */
    private Integer atumimax;

    /**
     * 厚み担当者
     */
    private String atumitantosya;

    /**
     * 担当者
     */
    private String tantosya;

    /**
     * 確認者
     */
    private String kakuninsya;

    /**
     * 備考1
     */
    private String biko1;

    /**
     * 備考2
     */
    private String biko2;

    /**
     * 登録日時
     */
    private Timestamp torokunichiji;

    /**
     * 更新日時
     */
    private Timestamp kosinnichiji;

    /**
     * Air抜き
     */
    private Integer airnuki;

    /**
     * 脱気時間
     */
    private Integer dakkijikan;

    /**
     * 脱気圧力
     */
    private Integer dakkiaturyoku;

    /**
     * 終了担当者
     */
    private String endtantousyacode;

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
     * KCPNO
     *
     * @return the kcpno
     */
    public String getKcpno() {
        return kcpno;
    }

    /**
     * KCPNO
     *
     * @param kcpno the kcpno to set
     */
    public void setKcpno(String kcpno) {
        this.kcpno = kcpno;
    }

    /**
     * 開始日時
     *
     * @return the kaisinichiji
     */
    public Timestamp getKaisinichiji() {
        return kaisinichiji;
    }

    /**
     * 開始日時
     *
     * @param kaisinichiji the kaisinichiji to set
     */
    public void setKaisinichiji(Timestamp kaisinichiji) {
        this.kaisinichiji = kaisinichiji;
    }

    /**
     * 終了日時
     *
     * @return the syuryonichiji
     */
    public Timestamp getSyuryonichiji() {
        return syuryonichiji;
    }

    /**
     * 終了日時
     *
     * @param syuryonichiji the syuryonichiji to set
     */
    public void setSyuryonichiji(Timestamp syuryonichiji) {
        this.syuryonichiji = syuryonichiji;
    }

    /**
     * 受入ｾｯﾄ数
     *
     * @return the ukeiresetsuu
     */
    public Integer getUkeiresetsuu() {
        return ukeiresetsuu;
    }

    /**
     * 受入ｾｯﾄ数
     *
     * @param ukeiresetsuu the ukeiresetsuu to set
     */
    public void setUkeiresetsuu(Integer ukeiresetsuu) {
        this.ukeiresetsuu = ukeiresetsuu;
    }

    /**
     * 良品ｾｯﾄ数
     *
     * @return the ryouhinsetsuu
     */
    public Integer getRyouhinsetsuu() {
        return ryouhinsetsuu;
    }

    /**
     * 良品ｾｯﾄ数
     *
     * @param ryouhinsetsuu the ryouhinsetsuu to set
     */
    public void setRyouhinsetsuu(Integer ryouhinsetsuu) {
        this.ryouhinsetsuu = ryouhinsetsuu;
    }

    /**
     * 号機ｺｰﾄﾞ
     *
     * @return the goki
     */
    public String getGoki() {
        return goki;
    }

    /**
     * 号機ｺｰﾄﾞ
     *
     * @param goki the goki to set
     */
    public void setGoki(String goki) {
        this.goki = goki;
    }

    /**
     * 温度
     *
     * @return the ondo
     */
    public BigDecimal getOndo() {
        return ondo;
    }

    /**
     * 温度
     *
     * @param ondo the ondo to set
     */
    public void setOndo(BigDecimal ondo) {
        this.ondo = ondo;
    }

    /**
     * 圧力
     *
     * @return the aturyoku
     */
    public Integer getAturyoku() {
        return aturyoku;
    }

    /**
     * 圧力
     *
     * @param aturyoku the aturyoku to set
     */
    public void setAturyoku(Integer aturyoku) {
        this.aturyoku = aturyoku;
    }

    /**
     * 室温
     *
     * @return the situon
     */
    public BigDecimal getSituon() {
        return situon;
    }

    /**
     * 室温
     *
     * @param situon the situon to set
     */
    public void setSituon(BigDecimal situon) {
        this.situon = situon;
    }

    /**
     * 湿度
     *
     * @return the situdo
     */
    public BigDecimal getSitudo() {
        return situdo;
    }

    /**
     * 湿度
     *
     * @param situdo the situdo to set
     */
    public void setSitudo(BigDecimal situdo) {
        this.situdo = situdo;
    }

    /**
     * 厚みMIN
     *
     * @return the atumimin
     */
    public Integer getAtumimin() {
        return atumimin;
    }

    /**
     * 厚みMIN
     *
     * @param atumimin the atumimin to set
     */
    public void setAtumimin(Integer atumimin) {
        this.atumimin = atumimin;
    }

    /**
     * 厚みMAX
     *
     * @return the atumimax
     */
    public Integer getAtumimax() {
        return atumimax;
    }

    /**
     * 厚みMAX
     *
     * @param atumimax the atumimax to set
     */
    public void setAtumimax(Integer atumimax) {
        this.atumimax = atumimax;
    }

    /**
     * 厚み担当者
     *
     * @return the atumitantosya
     */
    public String getAtumitantosya() {
        return atumitantosya;
    }

    /**
     * 厚み担当者
     *
     * @param atumitantosya the atumitantosya to set
     */
    public void setAtumitantosya(String atumitantosya) {
        this.atumitantosya = atumitantosya;
    }

    /**
     * 担当者
     *
     * @return the tantosya
     */
    public String getTantosya() {
        return tantosya;
    }

    /**
     * 担当者
     *
     * @param tantosya the tantosya to set
     */
    public void setTantosya(String tantosya) {
        this.tantosya = tantosya;
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
     * 備考1
     *
     * @return the biko1
     */
    public String getBiko1() {
        return biko1;
    }

    /**
     * 備考1
     *
     * @param biko1 the biko1 to set
     */
    public void setBiko1(String biko1) {
        this.biko1 = biko1;
    }

    /**
     * 備考2
     *
     * @return the biko2
     */
    public String getBiko2() {
        return biko2;
    }

    /**
     * 備考2
     *
     * @param biko2 the biko2 to set
     */
    public void setBiko2(String biko2) {
        this.biko2 = biko2;
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
     * Air抜き
     *
     * @return the airnuki
     */
    public Integer getAirnuki() {
        return airnuki;
    }

    /**
     * Air抜き
     *
     * @param airnuki the airnuki to set
     */
    public void setAirnuki(Integer airnuki) {
        this.airnuki = airnuki;
    }

    /**
     * 脱気時間
     *
     * @return the dakkijikan
     */
    public Integer getDakkijikan() {
        return dakkijikan;
    }

    /**
     * 脱気時間
     *
     * @param dakkijikan the dakkijikan to set
     */
    public void setDakkijikan(Integer dakkijikan) {
        this.dakkijikan = dakkijikan;
    }

    /**
     * 脱気圧力
     *
     * @return the dakkiaturyoku
     */
    public Integer getDakkiaturyoku() {
        return dakkiaturyoku;
    }

    /**
     * 脱気圧力
     *
     * @param dakkiaturyoku the dakkiaturyoku to set
     */
    public void setDakkiaturyoku(Integer dakkiaturyoku) {
        this.dakkiaturyoku = dakkiaturyoku;
    }

    /**
     * 終了担当者
     *
     * @return the endtantousyacode
     */
    public String getEndtantousyacode() {
        return endtantousyacode;
    }

    /**
     * 終了担当者
     *
     * @param endtantousyacode the endtantousyacode to set
     */
    public void setEndtantousyacode(String endtantousyacode) {
        this.endtantousyacode = endtantousyacode;
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

}
