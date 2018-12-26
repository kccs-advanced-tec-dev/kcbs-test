/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * 変更日	2018/12/11<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	ﾛｯﾄｶｰﾄﾞ電子化対応<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * DA_JOKENのモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since 2018/12/11
 */
public class DaJoken {

    /**
     * 設計No
     */
    private int sekkeiNo;
    /**
     * process
     */
    private String process;
    /**
     * 条件ﾃｰﾌﾞﾙ工程名
     */
    private String kouteiMei;
    /**
     * 条件ﾃｰﾌﾞﾙ項目名
     */
    private String koumokuMei;
    /**
     * 条件ﾃｰﾌﾞﾙ管理項目
     */
    private String kanriKoumoku;
    /**
     * 規格値
     */
    private String kikakuChi;

    /**
     * 設計No
     *
     * @return the sekkeiNo
     */
    public int getSekkeiNo() {
        return sekkeiNo;
    }

    /**
     * 設計No
     *
     * @param sekkeiNo the sekkeiNo to set
     */
    public void setSekkeiNo(int sekkeiNo) {
        this.sekkeiNo = sekkeiNo;
    }

    /**
     * process
     *
     * @return the process
     */
    public String getProcess() {
        return process;
    }

    /**
     * process
     *
     * @param process the process to set
     */
    public void setProcess(String process) {
        this.process = process;
    }

    /**
     * 条件ﾃｰﾌﾞﾙ工程名
     *
     * @return the kouteiMei
     */
    public String getKouteiMei() {
        return kouteiMei;
    }

    /**
     * 条件ﾃｰﾌﾞﾙ工程名
     *
     * @param kouteiMei the kouteiMei to set
     */
    public void setKouteiMei(String kouteiMei) {
        this.kouteiMei = kouteiMei;
    }

    /**
     * 条件ﾃｰﾌﾞﾙ項目名
     *
     * @return the koumokuMei
     */
    public String getKoumokuMei() {
        return koumokuMei;
    }

    /**
     * 条件ﾃｰﾌﾞﾙ項目名
     *
     * @param koumokuMei the koumokuMei to set
     */
    public void setKoumokuMei(String koumokuMei) {
        this.koumokuMei = koumokuMei;
    }

    /**
     * 条件ﾃｰﾌﾞﾙ管理項目
     *
     * @return the kanriKoumoku
     */
    public String getKanriKoumoku() {
        return kanriKoumoku;
    }

    /**
     * 条件ﾃｰﾌﾞﾙ管理項目
     *
     * @param kanriKoumoku the kanriKoumoku to set
     */
    public void setKanriKoumoku(String kanriKoumoku) {
        this.kanriKoumoku = kanriKoumoku;
    }

    /**
     * 規格値
     *
     * @return the kikakuChi
     */
    public String getKikakuChi() {
        return kikakuChi;
    }

    /**
     * 規格値
     *
     * @param kikakuChi the kikakuChi to set
     */
    public void setKikakuChi(String kikakuChi) {
        this.kikakuChi = kikakuChi;
    }
}
