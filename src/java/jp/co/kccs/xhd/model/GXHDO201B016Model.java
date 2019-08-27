/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.io.Serializable;
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
 * 焼成・2次脱脂(ﾍﾞﾙﾄ)履歴検索画面のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2019/08/06
 */
public class GXHDO201B016Model implements Serializable {

    /**
     * ﾛｯﾄNo.
     */
    private String lotno = "";

    /**
     * KCPNO
     */
    private String kcpno = "";

    /**
     * 受入セッタ枚数
     */
    private Integer ukeiresettamaisuu = null;

    /**
     * 開始日時
     */
    private Timestamp kaisinichiji = null;

    /**
     * 開始担当者
     */
    private String starttantosyacode = "";

    /**
     * 開始確認者
     */
    private String startkakuninsyacode = "";

    /**
     * 投入セッタ枚数
     */
    private Integer tounyusettasuu = null;

    /**
     * 2次脱脂号機
     */
    private String nijidasshigouki = "";

    /**
     * 2次脱脂設定ﾊﾟﾀｰﾝ
     */
    private String nijidasshisetteipt = "";

    /**
     * 2次脱脂ｷｰﾌﾟ温度
     */
    private Integer nijidasshikeepondo = null;

    /**
     * 2次脱脂ｺﾝﾍﾞｱ速度
     */
    private Integer nijidasshispeed = null;

    /**
     * 終了日時
     */
    private Timestamp syuuryounichiji = null;

    /**
     * 終了担当者
     */
    private String endtantosyacode = "";

    /**
     * 回収セッタ枚数
     */
    private Integer kaishuusettasuu = null;

    /**
     * 備考1
     */
    private String bikou1 = "";

    /**
     * 備考2
     */
    private String bikou2 = "";

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
     * @return ukeiresettamaisuu
     */
    public Integer getUkeiresettamaisuu() {
        return ukeiresettamaisuu;
    }

    /**
     * @param ukeiresettamaisuu セットする ukeiresettamaisuu
     */
    public void setUkeiresettamaisuu(Integer ukeiresettamaisuu) {
        this.ukeiresettamaisuu = ukeiresettamaisuu;
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
     * @return starttantosyacode
     */
    public String getStarttantosyacode() {
        return starttantosyacode;
    }

    /**
     * @param starttantosyacode セットする starttantosyacode
     */
    public void setStarttantosyacode(String starttantosyacode) {
        this.starttantosyacode = starttantosyacode;
    }

    /**
     * @return startkakuninsyacode
     */
    public String getStartkakuninsyacode() {
        return startkakuninsyacode;
    }

    /**
     * @param startkakuninsyacode セットする startkakuninsyacode
     */
    public void setStartkakuninsyacode(String startkakuninsyacode) {
        this.startkakuninsyacode = startkakuninsyacode;
    }

    /**
     * @return tounyusettasuu
     */
    public Integer getTounyusettasuu() {
        return tounyusettasuu;
    }

    /**
     * @param tounyusettasuu セットする tounyusettasuu
     */
    public void setTounyusettasuu(Integer tounyusettasuu) {
        this.tounyusettasuu = tounyusettasuu;
    }

    /**
     * @return nijidasshigouki
     */
    public String getNijidasshigouki() {
        return nijidasshigouki;
    }

    /**
     * @param nijidasshigouki セットする nijidasshigouki
     */
    public void setNijidasshigouki(String nijidasshigouki) {
        this.nijidasshigouki = nijidasshigouki;
    }

    /**
     * @return nijidasshisetteipt
     */
    public String getNijidasshisetteipt() {
        return nijidasshisetteipt;
    }

    /**
     * @param nijidasshisetteipt セットする nijidasshisetteipt
     */
    public void setNijidasshisetteipt(String nijidasshisetteipt) {
        this.nijidasshisetteipt = nijidasshisetteipt;
    }

    /**
     * @return nijidasshikeepondo
     */
    public Integer getNijidasshikeepondo() {
        return nijidasshikeepondo;
    }

    /**
     * @param nijidasshikeepondo セットする nijidasshikeepondo
     */
    public void setNijidasshikeepondo(Integer nijidasshikeepondo) {
        this.nijidasshikeepondo = nijidasshikeepondo;
    }

    /**
     * @return nijidasshispeed
     */
    public Integer getNijidasshispeed() {
        return nijidasshispeed;
    }

    /**
     * @param nijidasshispeed セットする nijidasshispeed
     */
    public void setNijidasshispeed(Integer nijidasshispeed) {
        this.nijidasshispeed = nijidasshispeed;
    }

    /**
     * @return syuuryounichiji
     */
    public Timestamp getSyuuryounichiji() {
        return syuuryounichiji;
    }

    /**
     * @param syuuryounichiji セットする syuuryounichiji
     */
    public void setSyuuryounichiji(Timestamp syuuryounichiji) {
        this.syuuryounichiji = syuuryounichiji;
    }

    /**
     * @return endtantosyacode
     */
    public String getEndtantosyacode() {
        return endtantosyacode;
    }

    /**
     * @param endtantosyacode セットする endtantosyacode
     */
    public void setEndtantosyacode(String endtantosyacode) {
        this.endtantosyacode = endtantosyacode;
    }

    /**
     * @return kaishuusettasuu
     */
    public Integer getKaishuusettasuu() {
        return kaishuusettasuu;
    }

    /**
     * @param kaishuusettasuu セットする kaishuusettasuu
     */
    public void setKaishuusettasuu(Integer kaishuusettasuu) {
        this.kaishuusettasuu = kaishuusettasuu;
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
}

