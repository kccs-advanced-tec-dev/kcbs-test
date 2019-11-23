/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.io.Serializable;

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
 * 焼成工程検索画面のモデルクラスです。
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2019/11/14
 */
public class GXHDO211DModel implements Serializable {

    /**
     * 工程ｺｰﾄﾞ
     */
    private String kouteiCode = "";
    /**
     * 工程名
     */
    private String koutei = "";

    /**
     * 工程ｺｰﾄﾞ
     *
     * @return the kouteiCode
     */
    public String getKouteiCode() {
        return kouteiCode;
    }

    /**
     * 工程ｺｰﾄﾞ
     *
     * @param kouteiCode the kouteiCode to set
     */
    public void setKouteiCode(String kouteiCode) {
        this.kouteiCode = kouteiCode;
    }

    /**
     * 工程名
     *
     * @return the koutei
     */
    public String getKoutei() {
        return koutei;
    }

    /**
     * 工程名
     *
     * @param koutei the koutei to set
     */
    public void setKoutei(String koutei) {
        this.koutei = koutei;
    }

}
