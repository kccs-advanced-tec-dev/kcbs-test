/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * 変更日	2021/10/08<br>
 * 計画書No	MB2109-DK002<br>
 * 変更者	SRC T.Ushiyama<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * FXHDD12(ﾃｰﾋﾟﾝｸﾞ号機選択)のモデルクラスです。
 *
 * @author SRC T.Ushiyama
 * @since 2021/10/08
 */
public class FXHDD12 {

    /**
     * 登録者
     */
    private String torokusha;
    /**
     * 登録日
     */
    private Timestamp toroku_date;
    /**
     * 更新者
     */
    private String koshinsha;
    /**
     * 更新日
     */
    private Timestamp koshin_date;
    /**
     * REV
     */
    private int rev;
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
     * 実績No
     */
    private int jissekino;
    /**
     * 番号
     */
    private int bango;
    /**
     * 号機ｺｰﾄﾞ
     */
    private String goki;
    /**
     * 状態ﾌﾗｸﾞ
     */
    private String jotai_flg;

    /**
     * 登録者
     *
     * @return the torokusha
     */
    public String getTorokusha() {
        return torokusha;
    }

    /**
     * 登録者
     *
     * @param torokusha the torokusha to set
     */
    public void setTorokusha(String torokusha) {
        this.torokusha = torokusha;
    }

    /**
     * 登録日
     *
     * @return the toroku_date
     */
    public Timestamp getToroku_date() {
        return toroku_date;
    }

    /**
     * 登録日
     *
     * @param toroku_date the toroku_date to set
     */
    public void setToroku_date(Timestamp toroku_date) {
        this.toroku_date = toroku_date;
    }

    /**
     * 更新者
     *
     * @return the koshinsha
     */
    public String getKoshinsha() {
        return koshinsha;
    }

    /**
     * 更新者
     *
     * @param koshinsha the koshinsha to set
     */
    public void setKoshinsha(String koshinsha) {
        this.koshinsha = koshinsha;
    }

    /**
     * 更新日
     *
     * @return the koshin_date
     */
    public Timestamp getKoshin_date() {
        return koshin_date;
    }

    /**
     * 更新日
     *
     * @param koshin_date the koshin_date to set
     */
    public void setKoshin_date(Timestamp koshin_date) {
        this.koshin_date = koshin_date;
    }

    /**
     * REV
     *
     * @return the rev
     */
    public int getRev() {
        return rev;
    }

    /**
     * REV
     *
     * @param rev the rev to set
     */
    public void setRev(int rev) {
        this.rev = rev;
    }

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
     * 実績No
     *
     * @return the jissekino
     */
    public int getJissekino() {
        return jissekino;
    }

    /**
     * 実績No
     *
     * @param jissekino the jissekino to set
     */
    public void setJissekino(int jissekino) {
        this.jissekino = jissekino;
    }

    /**
     * 番号
     *
     * @return the bango
     */
    public int getBango() {
        return bango;
    }

    /**
     * 番号
     *
     * @param bango the bango to set
     */
    public void setBango(int bango) {
        this.bango = bango;
    }

    /**
     * 号機ｺｰﾄﾞ
     *
     * @return the goki
     */
    public String getGoki() {
        return goki;
    }

    /**
     * 号機ｺｰﾄﾞ
     *
     * @param goki the goki to set
     */
    public void setGoki(String goki) {
        this.goki = goki;
    }

    /**
     * 状態ﾌﾗｸﾞ
     *
     * @return the jotai_flg
     */
    public String getJotai_flg() {
        return jotai_flg;
    }

    /**
     * 状態ﾌﾗｸﾞ
     *
     * @param jotai_flg the jotai_flg to set
     */
    public void setJotai_flg(String jotai_flg) {
        this.jotai_flg = jotai_flg;
    }
}
