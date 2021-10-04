/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2021/09/16<br>
 * 計画書No	MB2108-DK001<br>
 * 変更者	SRC T.Takenouichi<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * seisan(生産実績)のモデルクラスです。
 *
 * @author SRC T.Takenouichi
 * @since  2021/09/16
 */
public class Seisan {
    /**
     * 実績No
     */
    private Integer jissekino;

    /**
     * 良品数
     */
    private Integer ryohinsuu;

    /**
     * 振向数
     */
    private Integer furimukesuu;

    /**
     * 廃棄数
     */
    private Integer haikisuu;

    /**
     * 不良数
     */
    private Integer furyosuu;

    /**
     * 保留数
     */
    private Integer horyusuu;

    /**
     * 再生数
     */
    private Integer saiseisuu;

    /**
     * 号機ｺｰﾄﾞ
     */
    private String goukicode;

//<editor-fold defaultstate="collapsed" desc="#Getter・Setter">
    /**
     * 実績No
     *
     * @return the jissekino
     */
    public Integer getJissekino() {
        return jissekino;
    }
    /**
     * 実績No
     *
     * @param jissekino the jissekino to set
     */
    public void setJissekino(Integer jissekino) {
        this.jissekino = jissekino;
    }

    /**
     * 良品数
     *
     * @return the ryohinsuu
     */
    public Integer getRyohinsuu() {
        return ryohinsuu;
    }
    /**
     * 良品数
     *
     * @param ryohinsuu the ryohinsuu to set
     */
    public void setRyohinsuu(Integer ryohinsuu) {
        this.ryohinsuu = ryohinsuu;
    }

    /**
     * 振向数
     *
     * @return the furimukesuu
     */
    public Integer getFurimukesuu() {
        return furimukesuu;
    }
    /**
     * 振向数
     *
     * @param furimukesuu the furimukesuu to set
     */
    public void setFurimukesuu(Integer furimukesuu) {
        this.furimukesuu = furimukesuu;
    }

    /**
     * 廃棄数
     *
     * @return the haikisuu
     */
    public Integer getHaikisuu() {
        return haikisuu;
    }
    /**
     * 廃棄数
     *
     * @param haikisuu the haikisuu to set
     */
    public void setHaikisuu(Integer haikisuu) {
        this.haikisuu = haikisuu;
    }

    /**
     * 不良数
     *
     * @return the furyosuu
     */
    public Integer getFuryosuu() {
        return furyosuu;
    }
    /**
     * 不良数
     *
     * @param furyosuu the furyosuu to set
     */
    public void setFuryosuu(Integer furyosuu) {
        this.furyosuu = furyosuu;
    }

    /**
     * 保留数
     *
     * @return the horyusuu
     */
    public Integer getHoryusuu() {
        return horyusuu;
    }
    /**
     * 保留数
     *
     * @param horyusuu the horyusuu to set
     */
    public void setHoryusuu(Integer horyusuu) {
        this.horyusuu = horyusuu;
    }

    /**
     * 再生数
     *
     * @return the saiseisuu
     */
    public Integer getSaiseisuu() {
        return saiseisuu;
    }
    /**
     * 再生数
     *
     * @param saiseisuu the saiseisuu to set
     */
    public void setSaiseisuu(Integer saiseisuu) {
        this.saiseisuu = saiseisuu;
    }

    /**
     * 号機ｺｰﾄﾞ
     *
     * @return the goukicode
     */
    public String getGoukicode() {
        return goukicode;
    }
    /**
     * 号機ｺｰﾄﾞ
     *
     * @param goukicode the goukicode to set
     */
    public void setGoukicode(String goukicode) {
        this.goukicode = goukicode;
    }

//</editor-fold>
    
}
