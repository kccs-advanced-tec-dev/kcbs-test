/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/08/06<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 焼成温度検索画面のモデルクラスです。
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2019/11/12
 */
public class GXHDO211CModel implements Serializable {

    /**
     * 工場ｺｰﾄﾞ
     */
    private String kojyo = "";
    /**
     * ﾛｯﾄNo.
     */
    private String lotno = "";
    /**
     * ﾛｯﾄNo.(表示用)
     */
    private String lotnoView = "";
    /**
     * 枝番
     */
    private String edaban = "";
    /**
     * KCPNO
     */
    private String kcpno = "";
    /**
     * 工程ｺｰﾄﾞ
     */
    private String koteicode = "";
    /**
     * 号機
     */
    private String gouki = "";
    /**
     * 受入日(表示用)
     */
    private String ukeirebiView  = "";

    /**
     * 受入日
     */
    private Integer ukeirebi = null;
    /**
     * 個数
     */
    private String kosu = null;
    /**
     * ﾃｰﾌﾟﾛｯﾄNo
     */
    private String tapelotNo  = "";
    /**
     * 窒素濃度
     */
    private String tissonoudo = "";
    /**
     * 水素濃度1
     */
    private String suisonoudo1 = null;
    /**
     * ﾋﾟｰｸ温度1
     */
    private String peakondo1  = "";
    /**
     * 水素濃度2
     */
    private String suisonoudo2 = null;
    /**
     * ﾋﾟｰｸ温度2
     */
    private String peakondo2  = "";
    /**
     * 水素濃度3
     */
    private String suisonoudo3 = null;
    /**
     * ﾋﾟｰｸ温度3
     */
    private String peakondo3  = "";
    /**
     * 水素濃度4
     */
    private String suisonoudo4 = null;
    /**
     * ﾋﾟｰｸ温度4
     */
    private String peakondo4  = "";
    /**
     * 水素濃度5
     */
    private String suisonoudo5 = null;
    /**
     * ﾋﾟｰｸ温度5
     */
    private String peakondo5  = "";
    
    /**
     * 品種
     */
    private String hinsyu  = "";


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
     * ﾛｯﾄNo.
     *
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * ﾛｯﾄNo.
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
     * ﾛｯﾄNo.(表示用)
     * @return the lotnoView
     */
    public String getLotnoView() {
        return lotnoView;
    }

    /**
     * ﾛｯﾄNo.(表示用)
     * @param lotnoView the lotnoView to set
     */
    public void setLotnoView(String lotnoView) {
        this.lotnoView = lotnoView;
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
     * 工程ｺｰﾄﾞ
     * @return the koteicode
     */
    public String getKoteicode() {
        return koteicode;
    }

    /**
     * 工程ｺｰﾄﾞ
     * @param koteicode the koteicode to set
     */
    public void setKoteicode(String koteicode) {
        this.koteicode = koteicode;
    }

    /**
     * 号機ｺｰﾄﾞ
     *
     * @return the gouki
     */
    public String getGouki() {
        return gouki;
    }

    /**
     * 号機ｺｰﾄﾞ
     *
     * @param gouki the gouki to set
     */
    public void setGouki(String gouki) {
        this.gouki = gouki;
    }

    /**
     * 受入日(値)
     *
     * @return the ukeirebiView
     */
    public String getUkeirebiView() {
        return ukeirebiView;
    }

    /**
     * 受入日(値)
     *
     * @param ukeirebiView the ukeirebiView to set
     */
    public void setUkeirebiView(String ukeirebiView) {
        this.ukeirebiView = ukeirebiView;
    }

    /**
     * 受入日
     *
     * @return the ukeirebi
     */
    public Integer getUkeirebi() {
        return ukeirebi;
    }

    /**
     * 受入日
     *
     * @param ukeirebi the ukeirebi to set
     */
    public void setUkeirebi(Integer ukeirebi) {
        this.ukeirebi = ukeirebi;
    }

    /**
     * 個数
     *
     * @return the kosu
     */
    public String getKosu() {
        return kosu;
    }

    /**
     * 個数
     *
     * @param kosu the kosu to set
     */
    public void setKosu(String kosu) {
        this.kosu = kosu;
    }

    /**
     * ﾃｰﾌﾟﾛｯﾄNo
     *
     * @return the tapelotNo
     */
    public String getTapelotNo() {
        return tapelotNo;
    }

    /**
     * ﾃｰﾌﾟﾛｯﾄNo
     *
     * @param tapelotNo the tapelotNo to set
     */
    public void setTapelotNo(String tapelotNo) {
        this.tapelotNo = tapelotNo;
    }

    /**
     * 窒素濃度
     *
     * @return the tissonoudo
     */
    public String getTissonoudo() {
        return tissonoudo;
    }

    /**
     * 窒素濃度
     *
     * @param tissonoudo the tissonoudo to set
     */
    public void setTissonoudo(String tissonoudo) {
        this.tissonoudo = tissonoudo;
    }

    /**
     * 水素濃度1
     *
     * @return the suisonoudo1
     */
    public String getSuisonoudo1() {
        return suisonoudo1;
    }

    /**
     * 水素濃度1
     *
     * @param suisonoudo1 the suisonoudo1 to set
     */
    public void setSuisonoudo1(String suisonoudo1) {
        this.suisonoudo1 = suisonoudo1;
    }

    /**
     * ﾋﾟｰｸ温度1
     *
     * @return the peakondo1
     */
    public String getPeakondo1() {
        return peakondo1;
    }

    /**
     * ﾋﾟｰｸ温度1
     *
     * @param peakondo1 the peakondo1 to set
     */
    public void setPeakondo1(String peakondo1) {
        this.peakondo1 = peakondo1;
    }

    /**
     * 水素濃度2
     *
     * @return the suisonoudo2
     */
    public String getSuisonoudo2() {
        return suisonoudo2;
    }

    /**
     * 水素濃度2
     *
     * @param suisonoudo2 the suisonoudo2 to set
     */
    public void setSuisonoudo2(String suisonoudo2) {
        this.suisonoudo2 = suisonoudo2;
    }

    /**
     * ﾋﾟｰｸ温度2
     *
     * @return the peakondo2
     */
    public String getPeakondo2() {
        return peakondo2;
    }

    /**
     * ﾋﾟｰｸ温度2
     *
     * @param peakondo2 the peakondo2 to set
     */
    public void setPeakondo2(String peakondo2) {
        this.peakondo2 = peakondo2;
    }

    /**
     * 水素濃度3
     *
     * @return the suisonoudo3
     */
    public String getSuisonoudo3() {
        return suisonoudo3;
    }

    /**
     * 水素濃度3
     *
     * @param suisonoudo3 the suisonoudo3 to set
     */
    public void setSuisonoudo3(String suisonoudo3) {
        this.suisonoudo3 = suisonoudo3;
    }

    /**
     * ﾋﾟｰｸ温度3
     *
     * @return the peakondo3
     */
    public String getPeakondo3() {
        return peakondo3;
    }

    /**
     * ﾋﾟｰｸ温度3
     *
     * @param peakondo3 the peakondo3 to set
     */
    public void setPeakondo3(String peakondo3) {
        this.peakondo3 = peakondo3;
    }

    /**
     * 水素濃度4
     *
     * @return the suisonoudo4
     */
    public String getSuisonoudo4() {
        return suisonoudo4;
    }

    /**
     * 水素濃度4
     *
     * @param suisonoudo4 the suisonoudo4 to set
     */
    public void setSuisonoudo4(String suisonoudo4) {
        this.suisonoudo4 = suisonoudo4;
    }

    /**
     * ﾋﾟｰｸ温度4
     *
     * @return the peakondo4
     */
    public String getPeakondo4() {
        return peakondo4;
    }

    /**
     * ﾋﾟｰｸ温度4
     *
     * @param peakondo4 the peakondo4 to set
     */
    public void setPeakondo4(String peakondo4) {
        this.peakondo4 = peakondo4;
    }

    /**
     * 水素濃度5
     *
     * @return the suisonoudo5
     */
    public String getSuisonoudo5() {
        return suisonoudo5;
    }

    /**
     * 水素濃度5
     *
     * @param suisonoudo5 the suisonoudo5 to set
     */
    public void setSuisonoudo5(String suisonoudo5) {
        this.suisonoudo5 = suisonoudo5;
    }

    /**
     * ﾋﾟｰｸ温度5
     *
     * @return the peakondo5
     */
    public String getPeakondo5() {
        return peakondo5;
    }

    /**
     * ﾋﾟｰｸ温度5
     *
     * @param peakondo5 the peakondo5 to set
     */
    public void setPeakondo5(String peakondo5) {
        this.peakondo5 = peakondo5;
    }

    /**
     * 品種
     * @return the hinsyu
     */
    public String getHinsyu() {
        return hinsyu;
    }

    /**
     * 品種
     * @param hinsyu the hinsyu to set
     */
    public void setHinsyu(String hinsyu) {
        this.hinsyu = hinsyu;
    }

}
