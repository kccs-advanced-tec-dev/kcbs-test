/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/08/20<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	SRC K.Ijuin<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * sr_koteifuryo(工程不良)のモデルクラスです。
 *
 * @author SRC K.Ijuin
 * @since  2021/08/20
 */
public class SrKoteifuryo {
    
    //TODO: 全カラムを追加していない。
    
    /**
     * 登録No
     */
    private String torokuno;

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
     * @return the torokuno
     */
    public String getTorokuno() {
        return torokuno;
    }

    /**
     * @param torokuno the torokuno to set
     */
    public void setTorokuno(String torokuno) {
        this.torokuno = torokuno;
    }

    /**
     * @return the kojyo
     */
    public String getKojyo() {
        return kojyo;
    }

    /**
     * @param kojyo the kojyo to set
     */
    public void setKojyo(String kojyo) {
        this.kojyo = kojyo;
    }

    /**
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * @param lotno the lotno to set
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * @return the edaban
     */
    public String getEdaban() {
        return edaban;
    }

    /**
     * @param edaban the edaban to set
     */
    public void setEdaban(String edaban) {
        this.edaban = edaban;
    }
}
