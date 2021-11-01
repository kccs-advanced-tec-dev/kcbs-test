/*
     * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.jaxrs;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/10/19<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCCS M.Takahashi<br>
 * 変更理由	新規作成<br>
 * ===============================================================================<br>
 */
/**
 * 重量連携用RESTAPIのモデルクラスです。
 */
public class MkjuryoJson {

    private String goki;
    private String juryo;

    /**
     * @return the goki
     */
    public String getGoki() {
        return goki;
    }

    /**
     * @param goki the goki to set
     */
    public void setGoki(String goki) {
        this.goki = goki;
    }

    /**
     * @return the juryo
     */
    public String getJuryo() {
        return juryo;
    }

    /**
     * @param juryo the juryo to set
     */
    public void setJuryo(String juryo) {
        this.juryo = juryo;
    }
}
