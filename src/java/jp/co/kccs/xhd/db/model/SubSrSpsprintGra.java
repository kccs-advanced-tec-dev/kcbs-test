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
 * 変更日	2019/01/07<br>
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
 * @since 2019/01/07
 */
public class SubSrSpsprintGra {

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
     * 膜厚ｽﾀｰﾄ6
     */
    private BigDecimal makuatsuStart6;
    /**
     * 膜厚ｽﾀｰﾄ7
     */
    private BigDecimal makuatsuStart7;
    /**
     * 膜厚ｽﾀｰﾄ8
     */
    private BigDecimal makuatsuStart8;
    /**
     * 膜厚ｽﾀｰﾄ9
     */
    private BigDecimal makuatsuStart9;
    /**
     * PTN距離X ｽﾀｰﾄ1
     */
    private Integer startPtnDistX1;
    /**
     * PTN距離X ｽﾀｰﾄ2
     */
    private Integer startPtnDistX2;
    /**
     * PTN距離X ｽﾀｰﾄ3
     */
    private Integer startPtnDistX3;
    /**
     * PTN距離X ｽﾀｰﾄ4
     */
    private Integer startPtnDistX4;
    /**
     * PTN距離X ｽﾀｰﾄ5
     */
    private Integer startPtnDistX5;
    /**
     * PTN距離Y ｽﾀｰﾄ1
     */
    private Integer startPtnDistY1;
    /**
     * PTN距離Y ｽﾀｰﾄ2
     */
    private Integer startPtnDistY2;
    /**
     * PTN距離Y ｽﾀｰﾄ3
     */
    private Integer startPtnDistY3;
    /**
     * PTN距離Y ｽﾀｰﾄ4
     */
    private Integer startPtnDistY4;
    /**
     * PTN距離Y ｽﾀｰﾄ5
     */
    private Integer startPtnDistY5;
    /**
     * 膜厚ｴﾝﾄﾞ1
     */
    private BigDecimal makuatsuEnd1;
    /**
     * 膜厚ｴﾝﾄﾞ2
     */
    private BigDecimal makuatsuEnd2;
    /**
     * 膜厚ｴﾝﾄﾞ3
     */
    private BigDecimal makuatsuEnd3;
    /**
     * 膜厚ｴﾝﾄﾞ4
     */
    private BigDecimal makuatsuEnd4;
    /**
     * 膜厚ｴﾝﾄﾞ5
     */
    private BigDecimal makuatsuEnd5;
    /**
     * 膜厚ｴﾝﾄﾞ6
     */
    private BigDecimal makuatsuEnd6;
    /**
     * 膜厚ｴﾝﾄﾞ7
     */
    private BigDecimal makuatsuEnd7;
    /**
     * 膜厚ｴﾝﾄﾞ8
     */
    private BigDecimal makuatsuEnd8;
    /**
     * 膜厚ｴﾝﾄﾞ9
     */
    private BigDecimal makuatsuEnd9;
    /**
     * PTN距離X ｴﾝﾄﾞ1
     */
    private Integer endPtnDistX1;
    /**
     * PTN距離X ｴﾝﾄﾞ2
     */
    private Integer endPtnDistX2;
    /**
     * PTN距離X ｴﾝﾄﾞ3
     */
    private Integer endPtnDistX3;
    /**
     * PTN距離X ｴﾝﾄﾞ4
     */
    private Integer endPtnDistX4;
    /**
     * PTN距離X ｴﾝﾄﾞ5
     */
    private Integer endPtnDistX5;
    /**
     * PTN距離Y ｴﾝﾄﾞ1
     */
    private Integer endPtnDistY1;
    /**
     * PTN距離Y ｴﾝﾄﾞ2
     */
    private Integer endPtnDistY2;
    /**
     * PTN距離Y ｴﾝﾄﾞ3
     */
    private Integer endPtnDistY3;
    /**
     * PTN距離Y ｴﾝﾄﾞ4
     */
    private Integer endPtnDistY4;
    /**
     * PTN距離Y ｴﾝﾄﾞ5
     */
    private Integer endPtnDistY5;
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
     * 膜厚ｽﾀｰﾄ6
     *
     * @return the makuatsuStart6
     */
    public BigDecimal getMakuatsuStart6() {
        return makuatsuStart6;
    }

    /**
     * 膜厚ｽﾀｰﾄ6
     *
     * @param makuatsuStart6 the makuatsuStart6 to set
     */
    public void setMakuatsuStart6(BigDecimal makuatsuStart6) {
        this.makuatsuStart6 = makuatsuStart6;
    }

    /**
     * 膜厚ｽﾀｰﾄ7
     *
     * @return the makuatsuStart7
     */
    public BigDecimal getMakuatsuStart7() {
        return makuatsuStart7;
    }

    /**
     * 膜厚ｽﾀｰﾄ7
     *
     * @param makuatsuStart7 the makuatsuStart7 to set
     */
    public void setMakuatsuStart7(BigDecimal makuatsuStart7) {
        this.makuatsuStart7 = makuatsuStart7;
    }

    /**
     * 膜厚ｽﾀｰﾄ8
     *
     * @return the makuatsuStart8
     */
    public BigDecimal getMakuatsuStart8() {
        return makuatsuStart8;
    }

    /**
     * 膜厚ｽﾀｰﾄ8
     *
     * @param makuatsuStart8 the makuatsuStart8 to set
     */
    public void setMakuatsuStart8(BigDecimal makuatsuStart8) {
        this.makuatsuStart8 = makuatsuStart8;
    }

    /**
     * 膜厚ｽﾀｰﾄ9
     *
     * @return the makuatsuStart9
     */
    public BigDecimal getMakuatsuStart9() {
        return makuatsuStart9;
    }

    /**
     * 膜厚ｽﾀｰﾄ9
     *
     * @param makuatsuStart9 the makuatsuStart9 to set
     */
    public void setMakuatsuStart9(BigDecimal makuatsuStart9) {
        this.makuatsuStart9 = makuatsuStart9;
    }

    /**
     * PTN距離X ｽﾀｰﾄ1
     *
     * @return the startPtnDistX1
     */
    public Integer getStartPtnDistX1() {
        return startPtnDistX1;
    }

    /**
     * PTN距離X ｽﾀｰﾄ1
     *
     * @param startPtnDistX1 the startPtnDistX1 to set
     */
    public void setStartPtnDistX1(Integer startPtnDistX1) {
        this.startPtnDistX1 = startPtnDistX1;
    }

    /**
     * PTN距離X ｽﾀｰﾄ2
     *
     * @return the startPtnDistX2
     */
    public Integer getStartPtnDistX2() {
        return startPtnDistX2;
    }

    /**
     * PTN距離X ｽﾀｰﾄ2
     *
     * @param startPtnDistX2 the startPtnDistX2 to set
     */
    public void setStartPtnDistX2(Integer startPtnDistX2) {
        this.startPtnDistX2 = startPtnDistX2;
    }

    /**
     * PTN距離X ｽﾀｰﾄ3
     *
     * @return the startPtnDistX3
     */
    public Integer getStartPtnDistX3() {
        return startPtnDistX3;
    }

    /**
     * PTN距離X ｽﾀｰﾄ3
     *
     * @param startPtnDistX3 the startPtnDistX3 to set
     */
    public void setStartPtnDistX3(Integer startPtnDistX3) {
        this.startPtnDistX3 = startPtnDistX3;
    }

    /**
     * PTN距離X ｽﾀｰﾄ4
     *
     * @return the startPtnDistX4
     */
    public Integer getStartPtnDistX4() {
        return startPtnDistX4;
    }

    /**
     * PTN距離X ｽﾀｰﾄ4
     *
     * @param startPtnDistX4 the startPtnDistX4 to set
     */
    public void setStartPtnDistX4(Integer startPtnDistX4) {
        this.startPtnDistX4 = startPtnDistX4;
    }

    /**
     * PTN距離X ｽﾀｰﾄ5
     *
     * @return the startPtnDistX5
     */
    public Integer getStartPtnDistX5() {
        return startPtnDistX5;
    }

    /**
     * PTN距離X ｽﾀｰﾄ5
     *
     * @param startPtnDistX5 the startPtnDistX5 to set
     */
    public void setStartPtnDistX5(Integer startPtnDistX5) {
        this.startPtnDistX5 = startPtnDistX5;
    }

    /**
     * PTN距離Y ｽﾀｰﾄ1
     *
     * @return the startPtnDistY1
     */
    public Integer getStartPtnDistY1() {
        return startPtnDistY1;
    }

    /**
     * PTN距離Y ｽﾀｰﾄ1
     *
     * @param startPtnDistY1 the startPtnDistY1 to set
     */
    public void setStartPtnDistY1(Integer startPtnDistY1) {
        this.startPtnDistY1 = startPtnDistY1;
    }

    /**
     * PTN距離Y ｽﾀｰﾄ2
     *
     * @return the startPtnDistY2
     */
    public Integer getStartPtnDistY2() {
        return startPtnDistY2;
    }

    /**
     * PTN距離Y ｽﾀｰﾄ2
     *
     * @param startPtnDistY2 the startPtnDistY2 to set
     */
    public void setStartPtnDistY2(Integer startPtnDistY2) {
        this.startPtnDistY2 = startPtnDistY2;
    }

    /**
     * PTN距離Y ｽﾀｰﾄ3
     *
     * @return the startPtnDistY3
     */
    public Integer getStartPtnDistY3() {
        return startPtnDistY3;
    }

    /**
     * PTN距離Y ｽﾀｰﾄ3
     *
     * @param startPtnDistY3 the startPtnDistY3 to set
     */
    public void setStartPtnDistY3(Integer startPtnDistY3) {
        this.startPtnDistY3 = startPtnDistY3;
    }

    /**
     * PTN距離Y ｽﾀｰﾄ4
     *
     * @return the startPtnDistY4
     */
    public Integer getStartPtnDistY4() {
        return startPtnDistY4;
    }

    /**
     * PTN距離Y ｽﾀｰﾄ4
     *
     * @param startPtnDistY4 the startPtnDistY4 to set
     */
    public void setStartPtnDistY4(Integer startPtnDistY4) {
        this.startPtnDistY4 = startPtnDistY4;
    }

    /**
     * PTN距離Y ｽﾀｰﾄ5
     *
     * @return the startPtnDistY5
     */
    public Integer getStartPtnDistY5() {
        return startPtnDistY5;
    }

    /**
     * PTN距離Y ｽﾀｰﾄ5
     *
     * @param startPtnDistY5 the startPtnDistY5 to set
     */
    public void setStartPtnDistY5(Integer startPtnDistY5) {
        this.startPtnDistY5 = startPtnDistY5;
    }

    /**
     * 膜厚ｴﾝﾄﾞ1
     *
     * @return the makuatsuEnd1
     */
    public BigDecimal getMakuatsuEnd1() {
        return makuatsuEnd1;
    }

    /**
     * 膜厚ｴﾝﾄﾞ1
     *
     * @param makuatsuEnd1 the makuatsuEnd1 to set
     */
    public void setMakuatsuEnd1(BigDecimal makuatsuEnd1) {
        this.makuatsuEnd1 = makuatsuEnd1;
    }

    /**
     * 膜厚ｴﾝﾄﾞ2
     *
     * @return the makuatsuEnd2
     */
    public BigDecimal getMakuatsuEnd2() {
        return makuatsuEnd2;
    }

    /**
     * 膜厚ｴﾝﾄﾞ2
     *
     * @param makuatsuEnd2 the makuatsuEnd2 to set
     */
    public void setMakuatsuEnd2(BigDecimal makuatsuEnd2) {
        this.makuatsuEnd2 = makuatsuEnd2;
    }

    /**
     * 膜厚ｴﾝﾄﾞ3
     *
     * @return the makuatsuEnd3
     */
    public BigDecimal getMakuatsuEnd3() {
        return makuatsuEnd3;
    }

    /**
     * 膜厚ｴﾝﾄﾞ3
     *
     * @param makuatsuEnd3 the makuatsuEnd3 to set
     */
    public void setMakuatsuEnd3(BigDecimal makuatsuEnd3) {
        this.makuatsuEnd3 = makuatsuEnd3;
    }

    /**
     * 膜厚ｴﾝﾄﾞ4
     *
     * @return the makuatsuEnd4
     */
    public BigDecimal getMakuatsuEnd4() {
        return makuatsuEnd4;
    }

    /**
     * 膜厚ｴﾝﾄﾞ4
     *
     * @param makuatsuEnd4 the makuatsuEnd4 to set
     */
    public void setMakuatsuEnd4(BigDecimal makuatsuEnd4) {
        this.makuatsuEnd4 = makuatsuEnd4;
    }

    /**
     * 膜厚ｴﾝﾄﾞ5
     *
     * @return the makuatsuEnd5
     */
    public BigDecimal getMakuatsuEnd5() {
        return makuatsuEnd5;
    }

    /**
     * 膜厚ｴﾝﾄﾞ5
     *
     * @param makuatsuEnd5 the makuatsuEnd5 to set
     */
    public void setMakuatsuEnd5(BigDecimal makuatsuEnd5) {
        this.makuatsuEnd5 = makuatsuEnd5;
    }

    /**
     * 膜厚ｴﾝﾄﾞ6
     *
     * @return the makuatsuEnd6
     */
    public BigDecimal getMakuatsuEnd6() {
        return makuatsuEnd6;
    }

    /**
     * 膜厚ｴﾝﾄﾞ6
     *
     * @param makuatsuEnd6 the makuatsuEnd6 to set
     */
    public void setMakuatsuEnd6(BigDecimal makuatsuEnd6) {
        this.makuatsuEnd6 = makuatsuEnd6;
    }

    /**
     * 膜厚ｴﾝﾄﾞ7
     *
     * @return the makuatsuEnd7
     */
    public BigDecimal getMakuatsuEnd7() {
        return makuatsuEnd7;
    }

    /**
     * 膜厚ｴﾝﾄﾞ7
     *
     * @param makuatsuEnd7 the makuatsuEnd7 to set
     */
    public void setMakuatsuEnd7(BigDecimal makuatsuEnd7) {
        this.makuatsuEnd7 = makuatsuEnd7;
    }

    /**
     * 膜厚ｴﾝﾄﾞ8
     *
     * @return the makuatsuEnd8
     */
    public BigDecimal getMakuatsuEnd8() {
        return makuatsuEnd8;
    }

    /**
     * 膜厚ｴﾝﾄﾞ8
     *
     * @param makuatsuEnd8 the makuatsuEnd8 to set
     */
    public void setMakuatsuEnd8(BigDecimal makuatsuEnd8) {
        this.makuatsuEnd8 = makuatsuEnd8;
    }

    /**
     * 膜厚ｴﾝﾄﾞ9
     *
     * @return the makuatsuEnd9
     */
    public BigDecimal getMakuatsuEnd9() {
        return makuatsuEnd9;
    }

    /**
     * 膜厚ｴﾝﾄﾞ9
     *
     * @param makuatsuEnd9 the makuatsuEnd9 to set
     */
    public void setMakuatsuEnd9(BigDecimal makuatsuEnd9) {
        this.makuatsuEnd9 = makuatsuEnd9;
    }

    /**
     * PTN距離X ｴﾝﾄﾞ1
     *
     * @return the endPtnDistX1
     */
    public Integer getEndPtnDistX1() {
        return endPtnDistX1;
    }

    /**
     * PTN距離X ｴﾝﾄﾞ1
     *
     * @param endPtnDistX1 the endPtnDistX1 to set
     */
    public void setEndPtnDistX1(Integer endPtnDistX1) {
        this.endPtnDistX1 = endPtnDistX1;
    }

    /**
     * PTN距離X ｴﾝﾄﾞ2
     *
     * @return the endPtnDistX2
     */
    public Integer getEndPtnDistX2() {
        return endPtnDistX2;
    }

    /**
     * PTN距離X ｴﾝﾄﾞ2
     *
     * @param endPtnDistX2 the endPtnDistX2 to set
     */
    public void setEndPtnDistX2(Integer endPtnDistX2) {
        this.endPtnDistX2 = endPtnDistX2;
    }

    /**
     * PTN距離X ｴﾝﾄﾞ3
     *
     * @return the endPtnDistX3
     */
    public Integer getEndPtnDistX3() {
        return endPtnDistX3;
    }

    /**
     * PTN距離X ｴﾝﾄﾞ3
     *
     * @param endPtnDistX3 the endPtnDistX3 to set
     */
    public void setEndPtnDistX3(Integer endPtnDistX3) {
        this.endPtnDistX3 = endPtnDistX3;
    }

    /**
     * PTN距離X ｴﾝﾄﾞ4
     *
     * @return the endPtnDistX4
     */
    public Integer getEndPtnDistX4() {
        return endPtnDistX4;
    }

    /**
     * PTN距離X ｴﾝﾄﾞ4
     *
     * @param endPtnDistX4 the endPtnDistX4 to set
     */
    public void setEndPtnDistX4(Integer endPtnDistX4) {
        this.endPtnDistX4 = endPtnDistX4;
    }

    /**
     * PTN距離X ｴﾝﾄﾞ5
     *
     * @return the endPtnDistX5
     */
    public Integer getEndPtnDistX5() {
        return endPtnDistX5;
    }

    /**
     * PTN距離X ｴﾝﾄﾞ5
     *
     * @param endPtnDistX5 the endPtnDistX5 to set
     */
    public void setEndPtnDistX5(Integer endPtnDistX5) {
        this.endPtnDistX5 = endPtnDistX5;
    }

    /**
     * PTN距離Y ｴﾝﾄﾞ1
     *
     * @return the endPtnDistY1
     */
    public Integer getEndPtnDistY1() {
        return endPtnDistY1;
    }

    /**
     * PTN距離Y ｴﾝﾄﾞ1
     *
     * @param endPtnDistY1 the endPtnDistY1 to set
     */
    public void setEndPtnDistY1(Integer endPtnDistY1) {
        this.endPtnDistY1 = endPtnDistY1;
    }

    /**
     * PTN距離Y ｴﾝﾄﾞ2
     *
     * @return the endPtnDistY2
     */
    public Integer getEndPtnDistY2() {
        return endPtnDistY2;
    }

    /**
     * PTN距離Y ｴﾝﾄﾞ2
     *
     * @param endPtnDistY2 the endPtnDistY2 to set
     */
    public void setEndPtnDistY2(Integer endPtnDistY2) {
        this.endPtnDistY2 = endPtnDistY2;
    }

    /**
     * PTN距離Y ｴﾝﾄﾞ3
     *
     * @return the endPtnDistY3
     */
    public Integer getEndPtnDistY3() {
        return endPtnDistY3;
    }

    /**
     * PTN距離Y ｴﾝﾄﾞ3
     *
     * @param endPtnDistY3 the endPtnDistY3 to set
     */
    public void setEndPtnDistY3(Integer endPtnDistY3) {
        this.endPtnDistY3 = endPtnDistY3;
    }

    /**
     * PTN距離Y ｴﾝﾄﾞ4
     *
     * @return the endPtnDistY4
     */
    public Integer getEndPtnDistY4() {
        return endPtnDistY4;
    }

    /**
     * PTN距離Y ｴﾝﾄﾞ4
     *
     * @param endPtnDistY4 the endPtnDistY4 to set
     */
    public void setEndPtnDistY4(Integer endPtnDistY4) {
        this.endPtnDistY4 = endPtnDistY4;
    }

    /**
     * PTN距離Y ｴﾝﾄﾞ5
     *
     * @return the endPtnDistY5
     */
    public Integer getEndPtnDistY5() {
        return endPtnDistY5;
    }

    /**
     * PTN距離Y ｴﾝﾄﾞ5
     *
     * @param endPtnDistY5 the endPtnDistY5 to set
     */
    public void setEndPtnDistY5(Integer endPtnDistY5) {
        this.endPtnDistY5 = endPtnDistY5;
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
