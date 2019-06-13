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
 * 変更日	2019/06/11<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * ｾｯﾀ詰めのモデルクラスです。
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2019/06/11
 */
public class SrSayadume {

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
    private Timestamp kaishinichiji;

    /**
     * 終了日時
     */
    private Timestamp syuuryounichiji;

    /**
     * 号機ｺｰﾄﾞ
     */
    private String sayadumegouki;

    /**
     * ｾｯﾀ枚数
     */
    private Integer sayasuu;

    /**
     * 処理数
     */
    private Integer kosuu;

    /**
     * 担当者ｺｰﾄﾞ
     */
    private String tantousya;

    /**
     * 実績No
     */
    private Integer jissekino;

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
     * 登録日時
     */
    private Timestamp tourokunichiji;

    /**
     * 更新日時
     */
    private Timestamp koushinnichiji;

    /**
     * ｾｯﾀ詰め量
     */
    private Integer settertumeryou;

    /**
     * ｾｯﾀ詰め方法
     */
    private String settertumehouhou;

    /**
     * ｾｯﾀ種類
     */
    private Integer settersyurui;

    /**
     * 粉まぶし
     */
    private Integer konamabushi;

    /**
     * 開始確認者
     */
    private String startkakuninsyacode;

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
     * @return the kaishinichiji
     */
    public Timestamp getKaishinichiji() {
        return kaishinichiji;
    }

    /**
     * 開始日時
     *
     * @param kaishinichiji the kaishinichiji to set
     */
    public void setKaishinichiji(Timestamp kaishinichiji) {
        this.kaishinichiji = kaishinichiji;
    }

    /**
     * 終了日時
     *
     * @return the syuuryounichiji
     */
    public Timestamp getSyuuryounichiji() {
        return syuuryounichiji;
    }

    /**
     * 終了日時
     *
     * @param syuuryounichiji the syuuryounichiji to set
     */
    public void setSyuuryounichiji(Timestamp syuuryounichiji) {
        this.syuuryounichiji = syuuryounichiji;
    }

    /**
     * 号機ｺｰﾄﾞ
     *
     * @return the sayadumegouki
     */
    public String getSayadumegouki() {
        return sayadumegouki;
    }

    /**
     * 号機ｺｰﾄﾞ
     *
     * @param sayadumegouki the sayadumegouki to set
     */
    public void setSayadumegouki(String sayadumegouki) {
        this.sayadumegouki = sayadumegouki;
    }

    /**
     * ｾｯﾀ枚数
     *
     * @return the sayasuu
     */
    public Integer getSayasuu() {
        return sayasuu;
    }

    /**
     * ｾｯﾀ枚数
     *
     * @param sayasuu the sayasuu to set
     */
    public void setSayasuu(Integer sayasuu) {
        this.sayasuu = sayasuu;
    }

    /**
     * 処理数
     *
     * @return the kosuu
     */
    public Integer getKosuu() {
        return kosuu;
    }

    /**
     * 処理数
     *
     * @param kosuu the kosuu to set
     */
    public void setKosuu(Integer kosuu) {
        this.kosuu = kosuu;
    }

    /**
     * 担当者ｺｰﾄﾞ
     *
     * @return the tantousya
     */
    public String getTantousya() {
        return tantousya;
    }

    /**
     * 担当者ｺｰﾄﾞ
     *
     * @param tantousya the tantousya to set
     */
    public void setTantousya(String tantousya) {
        this.tantousya = tantousya;
    }

    /**
     * 実績No
     *
     * @return the jissekino
     */
    public Integer getJissekino() {
        return jissekino;
    }

    /**
     * 実績No
     *
     * @param jissekino the jissekino to set
     */
    public void setJissekino(Integer jissekino) {
        this.jissekino = jissekino;
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
     * 登録日時
     *
     * @return the tourokunichiji
     */
    public Timestamp getTourokunichiji() {
        return tourokunichiji;
    }

    /**
     * 登録日時
     *
     * @param tourokunichiji the tourokunichiji to set
     */
    public void setTourokunichiji(Timestamp tourokunichiji) {
        this.tourokunichiji = tourokunichiji;
    }

    /**
     * 更新日時
     *
     * @return the koushinnichiji
     */
    public Timestamp getKoushinnichiji() {
        return koushinnichiji;
    }

    /**
     * 更新日時
     *
     * @param koushinnichiji the koushinnichiji to set
     */
    public void setKoushinnichiji(Timestamp koushinnichiji) {
        this.koushinnichiji = koushinnichiji;
    }

    /**
     * ｾｯﾀ詰め量
     *
     * @return the settertumeryou
     */
    public Integer getSettertumeryou() {
        return settertumeryou;
    }

    /**
     * ｾｯﾀ詰め量
     *
     * @param settertumeryou the settertumeryou to set
     */
    public void setSettertumeryou(Integer settertumeryou) {
        this.settertumeryou = settertumeryou;
    }

    /**
     * ｾｯﾀ詰め方法
     *
     * @return the settertumehouhou
     */
    public String getSettertumehouhou() {
        return settertumehouhou;
    }

    /**
     * ｾｯﾀ詰め方法
     *
     * @param settertumehouhou the settertumehouhou to set
     */
    public void setSettertumehouhou(String settertumehouhou) {
        this.settertumehouhou = settertumehouhou;
    }

    /**
     * ｾｯﾀ種類
     *
     * @return the settersyurui
     */
    public Integer getSettersyurui() {
        return settersyurui;
    }

    /**
     * ｾｯﾀ種類
     *
     * @param settersyurui the settersyurui to set
     */
    public void setSettersyurui(Integer settersyurui) {
        this.settersyurui = settersyurui;
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
     * 開始確認者
     *
     * @return the startkakuninsyacode
     */
    public String getStartkakuninsyacode() {
        return startkakuninsyacode;
    }

    /**
     * 開始確認者
     *
     * @param startkakuninsyacode the startkakuninsyacode to set
     */
    public void setStartkakuninsyacode(String startkakuninsyacode) {
        this.startkakuninsyacode = startkakuninsyacode;
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
