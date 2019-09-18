/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/09/10<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * FXHDD06(指示温度)のモデルクラスです。
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2019/09/10
 */
public class FXHDD06 {

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
     * 担当者
     */
    private String tantousyacode;

    /**
     * 号機情報
     */
    private Integer goukijyoho;

    /**
     * 指示温度
     */
    private Integer shijiondo;

    /**
     * 水素濃度
     */
    private BigDecimal suisonoudo;

    /**
     * 指示温度ｸﾞﾙｰﾌﾟ
     */
    private Integer shijiondogroup;

    /**
     * 更新担当者
     */
    private String koshin_tantousyacode;

    /**
     * 登録日
     */
    private Timestamp toroku_date;

    /**
     * 更新日
     */
    private Timestamp koshin_date;

    /**
     * rev
     */
    private Integer rev;

    /**
     * 削除ﾌﾗｸﾞ
     */
    private String deleteflag;

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
     * 担当者
     *
     * @return the tantousyacode
     */
    public String getTantousyacode() {
        return tantousyacode;
    }

    /**
     * 担当者
     *
     * @param tantousyacode the tantousyacode to set
     */
    public void setTantousyacode(String tantousyacode) {
        this.tantousyacode = tantousyacode;
    }

    /**
     * 号機情報
     *
     * @return the goukijyoho
     */
    public Integer getGoukijyoho() {
        return goukijyoho;
    }

    /**
     * 号機情報
     *
     * @param goukijyoho the goukijyoho to set
     */
    public void setGoukijyoho(Integer goukijyoho) {
        this.goukijyoho = goukijyoho;
    }

    /**
     * 指示温度
     *
     * @return the shijiondo
     */
    public Integer getShijiondo() {
        return shijiondo;
    }

    /**
     * 指示温度
     *
     * @param shijiondo the shijiondo to set
     */
    public void setShijiondo(Integer shijiondo) {
        this.shijiondo = shijiondo;
    }

    /**
     * 水素濃度
     *
     * @return the suisonoudo
     */
    public BigDecimal getSuisonoudo() {
        return suisonoudo;
    }

    /**
     * 水素濃度
     *
     * @param suisonoudo the suisonoudo to set
     */
    public void setSuisonoudo(BigDecimal suisonoudo) {
        this.suisonoudo = suisonoudo;
    }

    /**
     * 指示温度ｸﾞﾙｰﾌﾟ
     *
     * @return the shijiondogroup
     */
    public Integer getShijiondogroup() {
        return shijiondogroup;
    }

    /**
     * 指示温度ｸﾞﾙｰﾌﾟ
     *
     * @param shijiondogroup the shijiondogroup to set
     */
    public void setShijiondogroup(Integer shijiondogroup) {
        this.shijiondogroup = shijiondogroup;
    }

    /**
     * 更新担当者
     *
     * @return the koshin_tantousyacode
     */
    public String getKoshin_tantousyacode() {
        return koshin_tantousyacode;
    }

    /**
     * 更新担当者
     *
     * @param koshin_tantousyacode the koshin_tantousyacode to set
     */
    public void setKoshin_tantousyacode(String koshin_tantousyacode) {
        this.koshin_tantousyacode = koshin_tantousyacode;
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
     * rev
     *
     * @return the rev
     */
    public Integer getRev() {
        return rev;
    }

    /**
     * rev
     *
     * @param rev the rev to set
     */
    public void setRev(Integer rev) {
        this.rev = rev;
    }

    /**
     * 削除ﾌﾗｸﾞ
     *
     * @return the deleteflag
     */
    public String getDeleteflag() {
        return deleteflag;
    }

    /**
     * 削除ﾌﾗｸﾞ
     *
     * @param deleteflag the deleteflag to set
     */
    public void setDeleteflag(String deleteflag) {
        this.deleteflag = deleteflag;
    }

}
