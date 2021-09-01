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
 * 変更日	2021/08/03<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS wxf<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * da_mkhyojunjokenのモデルクラスです。
 *
 * @author KCSS wxf
 * @since 2021/08/03
 */
public class DaMkhyojunjoken {
    /**
     * 品名
     */
    private String hinmei;
    /**
     * ﾊﾟﾀｰﾝ
     */
    private Integer pattern;
    /**
     * 種類
     */
    private String syurui;
    /**
     * 工程名
     */
    private String kouteimei;
    /**
     * 項目名
     */
    private String koumokumei;
    /**
     * 管理項目名
     */
    private String kanrikoumokumei;
    /**
     * 規格値
     */
    private String kikakuti;
    /**
     * ﾁｪｯｸﾊﾟﾀｰﾝ
     */
    private String tyekkupattern;
    /**
     * 担当者
     */
    private String tantousya;
    /**
     * 登録日
     */
    private Timestamp torokunichiji;
    /**
     * 更新日
     */
    private Timestamp kosinnichiji;
    
    /**
     * 品名
     *
     * @return the hinmei
     */
    public String getHinmei() {
        return hinmei;
    }

    /**
     * 管理項目名
     * @return the kanrikoumokumei
     */
    public String getKanrikoumokumei() {
        return kanrikoumokumei;
    }

    /**
     * 規格値
     * @return the kikakuti
     */
    public String getKikakuti() {
        return kikakuti;
    }

    /**
     * 項目名
     * @return the koumokumei
     */
    public String getKoumokumei() {
        return koumokumei;
    }

    /**
     * 工程名
     * @return the kouteimei
     */
    public String getKouteimei() {
        return kouteimei;
    }

    /**
     * ﾊﾟﾀｰﾝ
     * @return the pattern
     */
    public Integer getPattern() {
        return pattern;
    }

    /**
     * 種類
     * @return the syurui
     */
    public String getSyurui() {
        return syurui;
    }

    /**
     * ﾁｪｯｸﾊﾟﾀｰﾝ
     * @return the tyekkupattern
     */
    public String getTyekkupattern() {
        return tyekkupattern;
    }

    /**
     * 品名
     * @param hinmei the hinmei to set
     */
    public void setHinmei(String hinmei) {
        this.hinmei = hinmei;
    }

    /**
     * 管理項目名
     * @param kanrikoumokumei the kanrikoumokumei to set
     */
    public void setKanrikoumokumei(String kanrikoumokumei) {
        this.kanrikoumokumei = kanrikoumokumei;
    }

    /**
     * 規格値
     * @param kikakuti the kikakuti to set
     */
    public void setKikakuti(String kikakuti) {
        this.kikakuti = kikakuti;
    }

    /**
     * 項目名
     * @param koumokumei the koumokumei to set
     */
    public void setKoumokumei(String koumokumei) {
        this.koumokumei = koumokumei;
    }

    /**
     * 工程名
     * @param kouteimei the kouteimei to set
     */
    public void setKouteimei(String kouteimei) {
        this.kouteimei = kouteimei;
    }

    /**
     * ﾊﾟﾀｰﾝ
     * @param pattern the pattern to set
     */
    public void setPattern(Integer pattern) {
        this.pattern = pattern;
    }

    /**
     * 種類
     * @param syurui the syurui to set
     */
    public void setSyurui(String syurui) {
        this.syurui = syurui;
    }

    /**
     * ﾁｪｯｸﾊﾟﾀｰﾝ
     * @param tyekkupattern the tyekkupattern to set
     */
    public void setTyekkupattern(String tyekkupattern) {
        this.tyekkupattern = tyekkupattern;
    }

    /**
     * 更新日
     * @return the kosinnichiji
     */
    public Timestamp getKosinnichiji() {
        return kosinnichiji;
    }

    /**
     * 担当者
     * @return the tantousya
     */
    public String getTantousya() {
        return tantousya;
    }

    /**
     * 登録日
     * @return the torokunichiji
     */
    public Timestamp getTorokunichiji() {
        return torokunichiji;
    }

    /**
     * 更新日
     * @param kosinnichiji the kosinnichiji to set
     */
    public void setKosinnichiji(Timestamp kosinnichiji) {
        this.kosinnichiji = kosinnichiji;
    }

    /**
     * 担当者
     * @param tantousya the tantousya to set
     */
    public void setTantousya(String tantousya) {
        this.tantousya = tantousya;
    }

    /**
     * 登録日
     * @param torokunichiji the torokunichiji to set
     */
    public void setTorokunichiji(Timestamp torokunichiji) {
        this.torokunichiji = torokunichiji;
    }
}
