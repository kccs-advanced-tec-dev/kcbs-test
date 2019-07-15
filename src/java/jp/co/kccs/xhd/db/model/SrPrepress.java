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
 * 変更日	2019/05/22<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SR_PREPRESS(仮ﾌﾟﾚｽ)のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2019/05/22
 */
public class SrPrepress {

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
     * 温度表示
     */
    private Integer ondohyoji;

    /**
     * 圧力設定値
     */
    private Integer aturyokusetteiti;

    /**
     * 加圧時間
     */
    private Integer kaatujikan;

    /**
     * 号機ｺｰﾄﾞ
     */
    private String goki;

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
     * 登録日時
     */
    private Timestamp torokunichiji;

    /**
     * 更新日時
     */
    private Timestamp kosinnichiji;

    /**
     * 作業区分
     */
    private String sagyokubun;

    /**
     * ｾｯﾄ数
     */
    private Integer setsuu;

    /**
     * 端子枚数
     */
    private Integer tansimaisuu;

    /**
     * 圧力表示値
     */
    private Integer aturyokuhyouji;

    /**
     * ﾌﾟﾚｽｻｲﾄﾞ
     */
    private String pressside;

    /**
     * 実績No
     */
    private Integer jissekino;

    /**
     * 工程ｺｰﾄﾞ
     */
    private String koteicode;

    /**
     * 緩衝材
     */
    private Integer kansyouzai;

    /**
     * 真空保持
     */
    private Integer shinkuuhoji;

    /**
     * 2次圧力
     */
    private Integer aturyokusetteiti2;

    /**
     * 2次時間
     */
    private Integer kaatujikan2;

    /**
     * 3次圧力
     */
    private Integer aturyokusetteiti3;

    /**
     * 3次時間
     */
    private Integer kaatujikan3;

    /**
     * 真空度
     */
    private Integer shinkuudo;

    /**
     * 保管条件
     */
    private String hokanjouken;

    /**
     * 終了担当者
     */
    private String endtantousyacode;

    /**
     * 良品ｾｯﾄ数
     */
    private Integer ryouhinsetsuu;

    /**
     * 備考2
     */
    private String biko2;
    
    /**
     * KCPNO
     */
    private String kcpno;

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
     * @return ondohyoji
     */
    public Integer getOndohyoji() {
        return ondohyoji;
    }

    /**
     * @param ondohyoji ondohyoji
     */
    public void setOndohyoji(Integer ondohyoji) {
        this.ondohyoji = ondohyoji;
    }

    /**
     * @return aturyokusetteiti
     */
    public Integer getAturyokusetteiti() {
        return aturyokusetteiti;
    }

    /**
     * @param aturyokusetteiti aturyokusetteiti
     */
    public void setAturyokusetteiti(Integer aturyokusetteiti) {
        this.aturyokusetteiti = aturyokusetteiti;
    }

    /**
     * @return kaatujikan
     */
    public Integer getKaatujikan() {
        return kaatujikan;
    }

    /**
     * @param kaatujikan kaatujikan
     */
    public void setKaatujikan(Integer kaatujikan) {
        this.kaatujikan = kaatujikan;
    }

    /**
     * @return goki
     */
    public String getGoki() {
        return goki;
    }

    /**
     * @param goki goki
     */
    public void setGoki(String goki) {
        this.goki = goki;
    }

    /**
     * @return tantosya
     */
    public String getTantosya() {
        return tantosya;
    }

    /**
     * @param tantosya tantosya
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
     * @param kakuninsya kakuninsya
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
     * @param biko1 biko1
     */
    public void setBiko1(String biko1) {
        this.biko1 = biko1;
    }

    /**
     * @return torokunichiji
     */
    public Timestamp getTorokunichiji() {
        return torokunichiji;
    }

    /**
     * @param torokunichiji torokunichiji
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
     * @param kosinnichiji kosinnichiji
     */
    public void setKosinnichiji(Timestamp kosinnichiji) {
        this.kosinnichiji = kosinnichiji;
    }

    /**
     * @return sagyokubun
     */
    public String getSagyokubun() {
        return sagyokubun;
    }

    /**
     * @param sagyokubun sagyokubun
     */
    public void setSagyokubun(String sagyokubun) {
        this.sagyokubun = sagyokubun;
    }

    /**
     * @return setsuu
     */
    public Integer getSetsuu() {
        return setsuu;
    }

    /**
     * @param setsuu setsuu
     */
    public void setSetsuu(Integer setsuu) {
        this.setsuu = setsuu;
    }

    /**
     * @return tansimaisuu
     */
    public Integer getTansimaisuu() {
        return tansimaisuu;
    }

    /**
     * @param tansimaisuu tansimaisuu
     */
    public void setTansimaisuu(Integer tansimaisuu) {
        this.tansimaisuu = tansimaisuu;
    }

    /**
     * @return aturyokuhyouji
     */
    public Integer getAturyokuhyouji() {
        return aturyokuhyouji;
    }

    /**
     * @param aturyokuhyouji aturyokuhyouji
     */
    public void setAturyokuhyouji(Integer aturyokuhyouji) {
        this.aturyokuhyouji = aturyokuhyouji;
    }

    /**
     * @return pressside
     */
    public String getPressside() {
        return pressside;
    }

    /**
     * @param pressside pressside
     */
    public void setPressside(String pressside) {
        this.pressside = pressside;
    }

    /**
     * @return jissekino
     */
    public Integer getJissekino() {
        return jissekino;
    }

    /**
     * @param jissekino jissekino
     */
    public void setJissekino(Integer jissekino) {
        this.jissekino = jissekino;
    }

    /**
     * @return koteicode
     */
    public String getKoteicode() {
        return koteicode;
    }

    /**
     * @param koteicode koteicode
     */
    public void setKoteicode(String koteicode) {
        this.koteicode = koteicode;
    }

    /**
     * @return kansyouzai
     */
    public Integer getKansyouzai() {
        return kansyouzai;
    }

    /**
     * @param kansyouzai kansyouzai
     */
    public void setKansyouzai(Integer kansyouzai) {
        this.kansyouzai = kansyouzai;
    }

    /**
     * @return shinkuuhoji
     */
    public Integer getShinkuuhoji() {
        return shinkuuhoji;
    }

    /**
     * @param shinkuuhoji shinkuuhoji
     */
    public void setShinkuuhoji(Integer shinkuuhoji) {
        this.shinkuuhoji = shinkuuhoji;
    }

    /**
     * @return aturyokusetteiti2
     */
    public Integer getAturyokusetteiti2() {
        return aturyokusetteiti2;
    }

    /**
     * @param aturyokusetteiti2 aturyokusetteiti2
     */
    public void setAturyokusetteiti2(Integer aturyokusetteiti2) {
        this.aturyokusetteiti2 = aturyokusetteiti2;
    }

    /**
     * @return kaatujikan2
     */
    public Integer getKaatujikan2() {
        return kaatujikan2;
    }

    /**
     * @param kaatujikan2 kaatujikan2
     */
    public void setKaatujikan2(Integer kaatujikan2) {
        this.kaatujikan2 = kaatujikan2;
    }

    /**
     * @return aturyokusetteiti3
     */
    public Integer getAturyokusetteiti3() {
        return aturyokusetteiti3;
    }

    /**
     * @param aturyokusetteiti3 aturyokusetteiti3
     */
    public void setAturyokusetteiti3(Integer aturyokusetteiti3) {
        this.aturyokusetteiti3 = aturyokusetteiti3;
    }

    /**
     * @return kaatujikan3
     */
    public Integer getKaatujikan3() {
        return kaatujikan3;
    }

    /**
     * @param kaatujikan3 kaatujikan3
     */
    public void setKaatujikan3(Integer kaatujikan3) {
        this.kaatujikan3 = kaatujikan3;
    }

    /**
     * @return shinkuudo
     */
    public Integer getShinkuudo() {
        return shinkuudo;
    }

    /**
     * @param shinkuudo shinkuudo
     */
    public void setShinkuudo(Integer shinkuudo) {
        this.shinkuudo = shinkuudo;
    }

    /**
     * @return hokanjouken
     */
    public String getHokanjouken() {
        return hokanjouken;
    }

    /**
     * @param hokanjouken hokanjouken
     */
    public void setHokanjouken(String hokanjouken) {
        this.hokanjouken = hokanjouken;
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
     * @return ryouhinsetsuu
     */
    public Integer getRyouhinsetsuu() {
        return ryouhinsetsuu;
    }

    /**
     * @param ryouhinsetsuu ryouhinsetsuu
     */
    public void setRyouhinsetsuu(Integer ryouhinsetsuu) {
        this.ryouhinsetsuu = ryouhinsetsuu;
    }

    /**
     * @return biko2
     */
    public String getBiko2() {
        return biko2;
    }

    /**
     * @param biko2 biko2
     */
    public void setBiko2(String biko2) {
        this.biko2 = biko2;
    }

    /**
     * @return the kcpno
     */
    public String getKcpno() {
        return kcpno;
    }

    /**
     * @param kcpno the kcpno to set
     */
    public void setKcpno(String kcpno) {
        this.kcpno = kcpno;
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
}
