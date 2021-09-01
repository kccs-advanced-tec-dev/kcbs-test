/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2021/07/21<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS GC<br>
 * 変更理由	新規作成<br>
 * ===============================================================================<br>
 */
/**
 * 原材料規格ﾒﾝﾃﾅﾝｽ機能のモデルクラスです。
 *
 * @author KCSS gc
 * @since  2021/07/21
 */
public class GXHDO501DModel {
    /** 工程名 */
    private String kouteimei = "";
    /** 項目名 */
    private String koumokumei = "";
    /** 管理項目 */
    private String kanrikoumokumei = "";
    /** MAX(規格値) */
    private String kikakuti = "";
    /** MAX(ﾁｪｯｸﾊﾟﾀｰﾝ) */
    private String tyekkupattern = "";
    /** MAX(標準規格値) */
    private String hyojunkikakuti = "";
    /** MAX(標準ﾁｪｯｸﾊﾟﾀｰﾝ) */
    private String hyojuntyekkupattern = "";
    /** 規格値の背景色 */
    private String kikakutibgcolor = "";
    /** ﾁｪｯｸﾊﾟﾀｰﾝの背景色 */
    private String tyekkupatternbgcolor = "";
    /** 追加Flag */
    private String addFlg = "";
    /** 工程名の背景色 */
    private String kouteimeibgcolor = "";
    /** 項目名の背景色 */
    private String koumokumeibgcolor = "";
    /** 管理項目の背景色 */
    private String kanrikoumokumeibgcolor = "";
            
    /**
     * 工程名
     * @return the kouteimei
     */
    public String getKouteimei() {
        return kouteimei;
    }
    /**
     * 工程名
     * @param kouteimei the kouteimei to set
     */
    public void setKouteimei(String kouteimei) {
        this.kouteimei = kouteimei;
    }
    /**
     * 項目名
     * @return the koumokumei
     */
    public String getKoumokumei() {
        return koumokumei;
    }
    /**
     * 項目名
     * @param koumokumei the koumokumei to set
     */
    public void setKoumokumei(String koumokumei) {
        this.koumokumei = koumokumei;
    }
    /**
     * 管理項目
     * @return the kanrikoumokumei
     */
    public String getKanrikoumokumei() {
        return kanrikoumokumei;
    }
    /**
     * 管理項目
     * @param kanrikoumokumei the kanrikoumokumei to set
     */
    public void setKanrikoumokumei(String kanrikoumokumei) {
        this.kanrikoumokumei = kanrikoumokumei;
    }
    /**
     * MAX(規格値)
     * @return the kikakuti
     */
    public String getKikakuti() {
        return kikakuti;
    }
    /**
     * MAX(規格値)
     * @param kikakuti the kikakuti to set
     */
    public void setKikakuti(String kikakuti) {
        this.kikakuti = kikakuti;
    }
    /**
     * MAX(ﾁｪｯｸﾊﾟﾀｰﾝ)
     * @return the tyekkupattern
     */
    public String getTyekkupattern() {
        return tyekkupattern;
    }
    /**
     * MAX(ﾁｪｯｸﾊﾟﾀｰﾝ)
     * @param tyekkupattern the tyekkupattern to set
     */
    public void setTyekkupattern(String tyekkupattern) {
        this.tyekkupattern = tyekkupattern;
    }
    /**
     * MAX(標準規格値)
     * @return the hyojunkikakuti
     */
    public String getHyojunkikakuti() {
        return hyojunkikakuti;
    }
    /**
     * MAX(標準規格値)
     * @param hyojunkikakuti the hyojunkikakuti to set
     */
    public void setHyojunkikakuti(String hyojunkikakuti) {
        this.hyojunkikakuti = hyojunkikakuti;
    }
    /**
     * MAX(標準ﾁｪｯｸﾊﾟﾀｰﾝ
     * @return the hyojuntyekkupattern
     */
    public String getHyojuntyekkupattern() {
        return hyojuntyekkupattern;
    }
    /**
     * MAX(標準ﾁｪｯｸﾊﾟﾀｰﾝ
     * @param hyojuntyekkupattern the hyojuntyekkupattern to set
     */
    public void setHyojuntyekkupattern(String hyojuntyekkupattern) {
        this.hyojuntyekkupattern = hyojuntyekkupattern;
    }
    /**
     * 規格値の背景色
     * @return the kikakutibgcolor
     */
    public String getKikakutibgcolor() {
        return kikakutibgcolor;
    }
    /**
     * 規格値の背景色
     * @param kikakutibgcolor the kikakutibgcolor to set
     */
    public void setKikakutibgcolor(String kikakutibgcolor) {
        this.kikakutibgcolor = kikakutibgcolor;
    }
    /**
     * ﾁｪｯｸﾊﾟﾀｰﾝの背景色
     * @return the tyekkupatternbgcolor
     */
    public String getTyekkupatternbgcolor() {
        return tyekkupatternbgcolor;
    }
    /**
     * ﾁｪｯｸﾊﾟﾀｰﾝの背景色
     * @param tyekkupatternbgcolor the tyekkupatternbgcolor to set
     */
    public void setTyekkupatternbgcolor(String tyekkupatternbgcolor) {
        this.tyekkupatternbgcolor = tyekkupatternbgcolor;
    }
    /**
     * 追加Flag
     * @return the addFlg
     */
    public String getAddFlg() {
        return addFlg;
    }
    /**
     * 追加Flag
     * @param addFlg the addFlg to set
     */
    public void setAddFlg(String addFlg) {
        this.addFlg = addFlg;
    }
    /**
     * 工程名の背景色 
     * @return the kouteimeibgcolor
     */
    public String getKouteimeibgcolor() {
        return kouteimeibgcolor;
    }
    /**
     * 工程名の背景色 
     * @param kouteimeibgcolor the kouteimeibgcolor to set
     */
    public void setKouteimeibgcolor(String kouteimeibgcolor) {
        this.kouteimeibgcolor = kouteimeibgcolor;
    }
    /**
     * 項目名の背景色 
     * @return the koumokumeibgcolor
     */
    public String getKoumokumeibgcolor() {
        return koumokumeibgcolor;
    }
    /**
     * 項目名の背景色 
     * @param koumokumeibgcolor the koumokumeibgcolor to set
     */
    public void setKoumokumeibgcolor(String koumokumeibgcolor) {
        this.koumokumeibgcolor = koumokumeibgcolor;
    }
    /**
     * 管理項目の背景色 
     * @return the kanrikoumokumeibgcolor
     */
    public String getKanrikoumokumeibgcolor() {
        return kanrikoumokumeibgcolor;
    }
    /**
     * 管理項目の背景色 
     * @param kanrikoumokumeibgcolor the kanrikoumokumeibgcolor to set
     */
    public void setKanrikoumokumeibgcolor(String kanrikoumokumeibgcolor) {
        this.kanrikoumokumeibgcolor = kanrikoumokumeibgcolor;
    }

}
