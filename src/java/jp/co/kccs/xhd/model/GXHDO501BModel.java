/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2021/07/14<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS GC<br>
 * 変更理由	新規作成<br>
 * ===============================================================================<br>
 */
/**
 * 原材料規格取込結果確認機能のモデルクラスです。
 *
 * @author KCSS gc
 * @since  2021/07/15
 */
public class GXHDO501BModel {
    /** 取込No */
    private Integer torikomino = null;
    /** 規格 */
    private String kikaku = "";
    /** 種類 */
    private String syurui = "";
    /** 担当者 */
    private String tantousya = "";
    /** NG数 */
    private Integer ngsuu = null;
    /** 登録日 */
    private String tourokunichiji = null;
    /** 更新日 */
    private String koushinnichiji = null;

    
    /**
     * 取込No
     * @return the torikomino
     */
    public Integer getTorikomino() {
        return torikomino;
    }
    /**
     * 取込No
     * @param torikomino the torikomino to set
     */
    public void setTorikomino(Integer torikomino) {
        this.torikomino = torikomino;
    }
    /**
     * 規格
     * @return the kikaku
     */
    public String getKikaku() {
        return kikaku;
    }
    /**
     * 規格
     * @param kikaku the kikaku to set
     */
    public void setKikaku(String kikaku) {
        this.kikaku = kikaku;
    }
    /**
     * 種類
     * @return the syurui
     */
    public String getSyurui() {
        return syurui;
    }
    /**
     * 種類
     * @param syurui the syurui to set
     */
    public void setSyurui(String syurui) {
        this.syurui = syurui;
    }
    /**
     * 担当者
     * @return the tantousya
     */
    public String getTantousya() {
        return tantousya;
    }
    /**
     * 担当者
     * @param tantousya the tantousya to set
     */
    public void setTantousya(String tantousya) {
        this.tantousya = tantousya;
    }
    /**
     * NG数
     * @return the ngsuu
     */
    public Integer getNgsuu() {
        return ngsuu;
    }
    /**
     * NG数
     * @param ngsuu the ngsuu to set
     */
    public void setNgsuu(Integer ngsuu) {
        this.ngsuu = ngsuu;
    }
    /**
     * 登録日
     * @return the tourokunichiji
     */
    public String getTourokunichiji() {
        return tourokunichiji;
    }
    /**
     * 登録日
     * @param tourokunichiji the tourokunichiji to set
     */
    public void setTourokunichiji(String tourokunichiji) {
        this.tourokunichiji = tourokunichiji;
    }
    /**
     * 更新日
     * @return the koushinnichiji
     */
    public String getKoushinnichiji() {
        return koushinnichiji;
    }
    /**
     * 更新日
     * @param koushinnichiji the koushinnichiji to set
     */
    public void setKoushinnichiji(String koushinnichiji) {
        this.koushinnichiji = koushinnichiji;
    }    
}
