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
 * 変更日	2019/06/10<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 二次脱脂(ﾍﾞﾙﾄ)のモデルクラスです。
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2019/06/10
 */
public class SrNijidasshi {

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
     * セッタ枚数
     */
    private Integer ukeiresettamaisuu;

    /**
     * 開始日時
     */
    private Timestamp kaisinichiji;

    /**
     * 開始担当者
     */
    private String starttantosyacode;

    /**
     * 開始確認者
     */
    private String startkakuninsyacode;

    /**
     * 投入セッタ枚数
     */
    private Integer tounyusettasuu;

    /**
     * 2次脱脂号機
     */
    private String nijidasshigouki;

    /**
     * 2次脱脂設定ﾊﾟﾀｰﾝ
     */
    private String nijidasshisetteipt;

    /**
     * 2次脱脂ｷｰﾌﾟ温度
     */
    private Integer nijidasshikeepondo;

    /**
     * 2次脱脂ｺﾝﾍﾞｱ速度
     */
    private Integer nijidasshispeed;

    /**
     * 終了日時
     */
    private Timestamp syuuryounichiji;

    /**
     * 終了担当者
     */
    private String endtantosyacode;

    /**
     * 回収セッタ枚数
     */
    private Integer kaishuusettasuu;

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
     * セッタ枚数
     *
     * @return the ukeiresettamaisuu
     */
    public Integer getUkeiresettamaisuu() {
        return ukeiresettamaisuu;
    }

    /**
     * セッタ枚数
     *
     * @param ukeiresettamaisuu the ukeiresettamaisuu to set
     */
    public void setUkeiresettamaisuu(Integer ukeiresettamaisuu) {
        this.ukeiresettamaisuu = ukeiresettamaisuu;
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
     * 開始担当者
     *
     * @return the starttantosyacode
     */
    public String getStarttantosyacode() {
        return starttantosyacode;
    }

    /**
     * 開始担当者
     *
     * @param starttantosyacode the starttantosyacode to set
     */
    public void setStarttantosyacode(String starttantosyacode) {
        this.starttantosyacode = starttantosyacode;
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
     * 投入セッタ枚数
     *
     * @return the tounyusettasuu
     */
    public Integer getTounyusettasuu() {
        return tounyusettasuu;
    }

    /**
     * 投入セッタ枚数
     *
     * @param tounyusettasuu the tounyusettasuu to set
     */
    public void setTounyusettasuu(Integer tounyusettasuu) {
        this.tounyusettasuu = tounyusettasuu;
    }

    /**
     * 2次脱脂号機
     *
     * @return the nijidasshigouki
     */
    public String getNijidasshigouki() {
        return nijidasshigouki;
    }

    /**
     * 2次脱脂号機
     *
     * @param nijidasshigouki the nijidasshigouki to set
     */
    public void setNijidasshigouki(String nijidasshigouki) {
        this.nijidasshigouki = nijidasshigouki;
    }

    /**
     * 2次脱脂設定ﾊﾟﾀｰﾝ
     *
     * @return the nijidasshisetteipt
     */
    public String getNijidasshisetteipt() {
        return nijidasshisetteipt;
    }

    /**
     * 2次脱脂設定ﾊﾟﾀｰﾝ
     *
     * @param nijidasshisetteipt the nijidasshisetteipt to set
     */
    public void setNijidasshisetteipt(String nijidasshisetteipt) {
        this.nijidasshisetteipt = nijidasshisetteipt;
    }

    /**
     * 2次脱脂ｷｰﾌﾟ温度
     *
     * @return the nijidasshikeepondo
     */
    public Integer getNijidasshikeepondo() {
        return nijidasshikeepondo;
    }

    /**
     * 2次脱脂ｷｰﾌﾟ温度
     *
     * @param nijidasshikeepondo the nijidasshikeepondo to set
     */
    public void setNijidasshikeepondo(Integer nijidasshikeepondo) {
        this.nijidasshikeepondo = nijidasshikeepondo;
    }

    /**
     * 2次脱脂ｺﾝﾍﾞｱ速度
     *
     * @return the nijidasshispeed
     */
    public Integer getNijidasshispeed() {
        return nijidasshispeed;
    }

    /**
     * 2次脱脂ｺﾝﾍﾞｱ速度
     *
     * @param nijidasshispeed the nijidasshispeed to set
     */
    public void setNijidasshispeed(Integer nijidasshispeed) {
        this.nijidasshispeed = nijidasshispeed;
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
     * 終了担当者
     *
     * @return the endtantosyacode
     */
    public String getEndtantosyacode() {
        return endtantosyacode;
    }

    /**
     * 終了担当者
     *
     * @param endtantosyacode the endtantosyacode to set
     */
    public void setEndtantosyacode(String endtantosyacode) {
        this.endtantosyacode = endtantosyacode;
    }

    /**
     * 回収セッタ枚数
     *
     * @return the kaishuusettasuu
     */
    public Integer getKaishuusettasuu() {
        return kaishuusettasuu;
    }

    /**
     * 回収セッタ枚数
     *
     * @param kaishuusettasuu the kaishuusettasuu to set
     */
    public void setKaishuusettasuu(Integer kaishuusettasuu) {
        this.kaishuusettasuu = kaishuusettasuu;
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
