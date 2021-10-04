/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	原材料規格管理機能(コンデンサ)<br>
 * 変更日	2021/08/16<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * DA_JOKENのモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/08/16
 */
public class DaMkJoken {

    /**
     * 設計No
     */
    private int sekkeino;

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
     * 登録日時
     */
    private Timestamp tourokunichiji;

    /**
     * 更新日時
     */
    private Timestamp koushinnichiji;

    /**
     * 設計No
     *
     * @return the sekkeino
     */
    public int getSekkeino() {
        return sekkeino;
    }

    /**
     * 設計No
     *
     * @param sekkeino the sekkeino to set
     */
    public void setSekkeino(int sekkeino) {
        this.sekkeino = sekkeino;
    }

    /**
     * 工程名
     *
     * @return the kouteimei
     */
    public String getKouteimei() {
        return kouteimei;
    }

    /**
     * 工程名
     *
     * @param kouteimei the kouteimei to set
     */
    public void setKouteimei(String kouteimei) {
        this.kouteimei = kouteimei;
    }

    /**
     * 項目名
     *
     * @return the koumokumei
     */
    public String getKoumokumei() {
        return koumokumei;
    }

    /**
     * 項目名
     *
     * @param koumokumei the koumokumei to set
     */
    public void setKoumokumei(String koumokumei) {
        this.koumokumei = koumokumei;
    }

    /**
     * 管理項目名
     *
     * @return the kanrikoumokumei
     */
    public String getKanrikoumokumei() {
        return kanrikoumokumei;
    }

    /**
     * 管理項目名
     *
     * @param kanrikoumokumei the kanrikoumokumei to set
     */
    public void setKanrikoumokumei(String kanrikoumokumei) {
        this.kanrikoumokumei = kanrikoumokumei;
    }

    /**
     * 規格値
     *
     * @return the kikakuti
     */
    public String getKikakuti() {
        return kikakuti;
    }

    /**
     * 規格値
     *
     * @param kikakuti the kikakuti to set
     */
    public void setKikakuti(String kikakuti) {
        this.kikakuti = kikakuti;
    }

    /**
     * ﾁｪｯｸﾊﾟﾀｰﾝ
     *
     * @return the tyekkupattern
     */
    public String getTyekkupattern() {
        return tyekkupattern;
    }

    /**
     * ﾁｪｯｸﾊﾟﾀｰﾝ
     *
     * @param tyekkupattern the tyekkupattern to set
     */
    public void setTyekkupattern(String tyekkupattern) {
        this.tyekkupattern = tyekkupattern;
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
}
