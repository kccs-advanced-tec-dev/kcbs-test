/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2018/01/07<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面のモデルクラスです。
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2018/01/17
 */
public class SubSrRsusprn {

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
     * 膜厚ｽﾀｰﾄ1
     */
    private BigDecimal makuatsuStart1;

    /**
     * 膜厚ｽﾀｰﾄ2
     */
    private BigDecimal makuatsuStart2;

    /**
     * 膜厚ｽﾀｰﾄ3
     */
    private BigDecimal makuatsuStart3;

    /**
     * 膜厚ｽﾀｰﾄ4
     */
    private BigDecimal makuatsuStart4;

    /**
     * 膜厚ｽﾀｰﾄ5
     */
    private BigDecimal makuatsuStart5;

    /**
     * 印刷幅ｽﾀｰﾄ1
     */
    private BigDecimal insatuhabaStart1;

    /**
     * 印刷幅ｽﾀｰﾄ2
     */
    private BigDecimal insatuhabaStart2;

    /**
     * 印刷幅ｽﾀｰﾄ3
     */
    private BigDecimal insatuhabaStart3;

    /**
     * 印刷幅ｽﾀｰﾄ4
     */
    private BigDecimal insatuhabaStart4;

    /**
     * 印刷幅ｽﾀｰﾄ5
     */
    private BigDecimal insatuhabaStart5;

    /**
     * 登録日時
     */
    private Timestamp torokunichiji;

    /**
     * 更新日時
     */
    private Timestamp kosinnichiji;

    /**
     * revision
     */
    private String revision;

    /**
     * 削除ﾌﾗｸﾞ
     */
    private Integer deleteflag;

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
     * 膜厚ｽﾀｰﾄ1
     *
     * @return the makuatsuStart1
     */
    public BigDecimal getMakuatsuStart1() {
        return makuatsuStart1;
    }

    /**
     * 膜厚ｽﾀｰﾄ1
     *
     * @param makuatsuStart1 the makuatsuStart1 to set
     */
    public void setMakuatsuStart1(BigDecimal makuatsuStart1) {
        this.makuatsuStart1 = makuatsuStart1;
    }

    /**
     * 膜厚ｽﾀｰﾄ2
     *
     * @return the makuatsuStart2
     */
    public BigDecimal getMakuatsuStart2() {
        return makuatsuStart2;
    }

    /**
     * 膜厚ｽﾀｰﾄ2
     *
     * @param makuatsuStart2 the makuatsuStart2 to set
     */
    public void setMakuatsuStart2(BigDecimal makuatsuStart2) {
        this.makuatsuStart2 = makuatsuStart2;
    }

    /**
     * 膜厚ｽﾀｰﾄ3
     *
     * @return the makuatsuStart3
     */
    public BigDecimal getMakuatsuStart3() {
        return makuatsuStart3;
    }

    /**
     * 膜厚ｽﾀｰﾄ3
     *
     * @param makuatsuStart3 the makuatsuStart3 to set
     */
    public void setMakuatsuStart3(BigDecimal makuatsuStart3) {
        this.makuatsuStart3 = makuatsuStart3;
    }

    /**
     * 膜厚ｽﾀｰﾄ4
     *
     * @return the makuatsuStart4
     */
    public BigDecimal getMakuatsuStart4() {
        return makuatsuStart4;
    }

    /**
     * 膜厚ｽﾀｰﾄ4
     *
     * @param makuatsuStart4 the makuatsuStart4 to set
     */
    public void setMakuatsuStart4(BigDecimal makuatsuStart4) {
        this.makuatsuStart4 = makuatsuStart4;
    }

    /**
     * 膜厚ｽﾀｰﾄ5
     *
     * @return the makuatsuStart5
     */
    public BigDecimal getMakuatsuStart5() {
        return makuatsuStart5;
    }

    /**
     * 膜厚ｽﾀｰﾄ5
     *
     * @param makuatsuStart5 the makuatsuStart5 to set
     */
    public void setMakuatsuStart5(BigDecimal makuatsuStart5) {
        this.makuatsuStart5 = makuatsuStart5;
    }

    /**
     * 印刷幅ｽﾀｰﾄ1
     *
     * @return the insatuhabaStart1
     */
    public BigDecimal getInsatuhabaStart1() {
        return insatuhabaStart1;
    }

    /**
     * 印刷幅ｽﾀｰﾄ1
     *
     * @param insatuhabaStart1 the insatuhabaStart1 to set
     */
    public void setInsatuhabaStart1(BigDecimal insatuhabaStart1) {
        this.insatuhabaStart1 = insatuhabaStart1;
    }

    /**
     * 印刷幅ｽﾀｰﾄ2
     *
     * @return the insatuhabaStart2
     */
    public BigDecimal getInsatuhabaStart2() {
        return insatuhabaStart2;
    }

    /**
     * 印刷幅ｽﾀｰﾄ2
     *
     * @param insatuhabaStart2 the insatuhabaStart2 to set
     */
    public void setInsatuhabaStart2(BigDecimal insatuhabaStart2) {
        this.insatuhabaStart2 = insatuhabaStart2;
    }

    /**
     * 印刷幅ｽﾀｰﾄ3
     *
     * @return the insatuhabaStart3
     */
    public BigDecimal getInsatuhabaStart3() {
        return insatuhabaStart3;
    }

    /**
     * 印刷幅ｽﾀｰﾄ3
     *
     * @param insatuhabaStart3 the insatuhabaStart3 to set
     */
    public void setInsatuhabaStart3(BigDecimal insatuhabaStart3) {
        this.insatuhabaStart3 = insatuhabaStart3;
    }

    /**
     * 印刷幅ｽﾀｰﾄ4
     *
     * @return the insatuhabaStart4
     */
    public BigDecimal getInsatuhabaStart4() {
        return insatuhabaStart4;
    }

    /**
     * 印刷幅ｽﾀｰﾄ4
     *
     * @param insatuhabaStart4 the insatuhabaStart4 to set
     */
    public void setInsatuhabaStart4(BigDecimal insatuhabaStart4) {
        this.insatuhabaStart4 = insatuhabaStart4;
    }

    /**
     * 印刷幅ｽﾀｰﾄ5
     *
     * @return the insatuhabaStart5
     */
    public BigDecimal getInsatuhabaStart5() {
        return insatuhabaStart5;
    }

    /**
     * 印刷幅ｽﾀｰﾄ5
     *
     * @param insatuhabaStart5 the insatuhabaStart5 to set
     */
    public void setInsatuhabaStart5(BigDecimal insatuhabaStart5) {
        this.insatuhabaStart5 = insatuhabaStart5;
    }

    /**
     * 登録日時
     *
     * @return the torokunichiji
     */
    public Timestamp getTorokunichiji() {
        return torokunichiji;
    }

    /**
     * 登録日時
     *
     * @param torokunichiji the torokunichiji to set
     */
    public void setTorokunichiji(Timestamp torokunichiji) {
        this.torokunichiji = torokunichiji;
    }

    /**
     * 更新日時
     *
     * @return the kosinnichiji
     */
    public Timestamp getKosinnichiji() {
        return kosinnichiji;
    }

    /**
     * 更新日時
     *
     * @param kosinnichiji the kosinnichiji to set
     */
    public void setKosinnichiji(Timestamp kosinnichiji) {
        this.kosinnichiji = kosinnichiji;
    }

    /**
     * revision
     *
     * @return the revision
     */
    public String getRevision() {
        return revision;
    }

    /**
     * revision
     *
     * @param revision the revision to set
     */
    public void setRevision(String revision) {
        this.revision = revision;
    }

    /**
     * 削除ﾌﾗｸﾞ
     *
     * @return the deleteflag
     */
    public Integer getDeleteflag() {
        return deleteflag;
    }

    /**
     * 削除ﾌﾗｸﾞ
     *
     * @param deleteflag the deleteflag to set
     */
    public void setDeleteflag(Integer deleteflag) {
        this.deleteflag = deleteflag;
    }

}
