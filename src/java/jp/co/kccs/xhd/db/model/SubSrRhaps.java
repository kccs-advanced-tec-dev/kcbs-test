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
 * 変更日	2019/03/15<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 印刷積層RHAPS_ｻﾌﾞ画面のモデルクラスです。
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2019/03/15
 */
public class SubSrRhaps {

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
     * 電極膜厚1
     */
    private BigDecimal dmakuatsu1;

    /**
     * 電極膜厚2
     */
    private BigDecimal dmakuatsu2;

    /**
     * 電極膜厚3
     */
    private BigDecimal dmakuatsu3;

    /**
     * 電極膜厚4
     */
    private BigDecimal dmakuatsu4;

    /**
     * 電極膜厚5
     */
    private BigDecimal dmakuatsu5;

    /**
     * 電極膜厚6
     */
    private BigDecimal dmakuatsu6;

    /**
     * 電極膜厚7
     */
    private BigDecimal dmakuatsu7;

    /**
     * 電極膜厚8
     */
    private BigDecimal dmakuatsu8;

    /**
     * 電極膜厚9
     */
    private BigDecimal dmakuatsu9;

    /**
     * ﾊﾟﾀｰﾝ間距離1
     */
    private Integer ptndist1;

    /**
     * ﾊﾟﾀｰﾝ間距離2
     */
    private Integer ptndist2;

    /**
     * ﾊﾟﾀｰﾝ間距離3
     */
    private Integer ptndist3;

    /**
     * ﾊﾟﾀｰﾝ間距離4
     */
    private Integer ptndist4;

    /**
     * ﾊﾟﾀｰﾝ間距離5
     */
    private Integer ptndist5;

    /**
     * 合わせ(RZ)1
     */
    private BigDecimal awaserz1;

    /**
     * 合わせ(RZ)2
     */
    private BigDecimal awaserz2;

    /**
     * 合わせ(RZ)3
     */
    private BigDecimal awaserz3;

    /**
     * 合わせ(RZ)4
     */
    private BigDecimal awaserz4;

    /**
     * 合わせ(RZ)5
     */
    private BigDecimal awaserz5;

    /**
     * 合わせ(RZ)6
     */
    private BigDecimal awaserz6;

    /**
     * 合わせ(RZ)7
     */
    private BigDecimal awaserz7;

    /**
     * 合わせ(RZ)8
     */
    private BigDecimal awaserz8;

    /**
     * 合わせ(RZ)9
     */
    private BigDecimal awaserz9;

    /**
     * 被り量（μｍ）1
     */
    private Integer kaburihidariuex1;

    /**
     * 被り量（μｍ）2
     */
    private Integer kaburihidariuex2;

    /**
     * 被り量（μｍ）3
     */
    private Integer kaburihidariuey1;

    /**
     * 被り量（μｍ）4
     */
    private Integer kaburihidariuey2;

    /**
     * 被り量（μｍ）5
     */
    private Integer kaburihidarisitax1;

    /**
     * 被り量（μｍ）6
     */
    private Integer kaburihidarisitax2;

    /**
     * 被り量（μｍ）7
     */
    private Integer kaburihidarisitay1;

    /**
     * 被り量（μｍ）8
     */
    private Integer kaburihidarisitay2;

    /**
     * 被り量（μｍ）9
     */
    private Integer kaburihidaricenterx1;

    /**
     * 被り量（μｍ）10
     */
    private Integer kaburihidaricenterx2;

    /**
     * 被り量（μｍ）11
     */
    private Integer kaburihidaricentery1;

    /**
     * 被り量（μｍ）12
     */
    private Integer kaburihidaricentery2;

    /**
     * 被り量（μｍ）13
     */
    private Integer kaburimigiuex1;

    /**
     * 被り量（μｍ）14
     */
    private Integer kaburimigiuex2;

    /**
     * 被り量（μｍ）15
     */
    private Integer kaburimigiuey1;

    /**
     * 被り量（μｍ）16
     */
    private Integer kaburimigiuey2;

    /**
     * 被り量（μｍ）17
     */
    private Integer kaburimigisitax1;

    /**
     * 被り量（μｍ）18
     */
    private Integer kaburimigisitax2;

    /**
     * 被り量（μｍ）19
     */
    private Integer kaburimigisitay1;

    /**
     * 被り量（μｍ）20
     */
    private Integer kaburimigisitay2;

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
    private Long revision;

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
     * 電極膜厚1
     *
     * @return the dmakuatsu1
     */
    public BigDecimal getDmakuatsu1() {
        return dmakuatsu1;
    }

    /**
     * 電極膜厚1
     *
     * @param dmakuatsu1 the dmakuatsu1 to set
     */
    public void setDmakuatsu1(BigDecimal dmakuatsu1) {
        this.dmakuatsu1 = dmakuatsu1;
    }

    /**
     * 電極膜厚2
     *
     * @return the dmakuatsu2
     */
    public BigDecimal getDmakuatsu2() {
        return dmakuatsu2;
    }

    /**
     * 電極膜厚2
     *
     * @param dmakuatsu2 the dmakuatsu2 to set
     */
    public void setDmakuatsu2(BigDecimal dmakuatsu2) {
        this.dmakuatsu2 = dmakuatsu2;
    }

    /**
     * 電極膜厚3
     *
     * @return the dmakuatsu3
     */
    public BigDecimal getDmakuatsu3() {
        return dmakuatsu3;
    }

    /**
     * 電極膜厚3
     *
     * @param dmakuatsu3 the dmakuatsu3 to set
     */
    public void setDmakuatsu3(BigDecimal dmakuatsu3) {
        this.dmakuatsu3 = dmakuatsu3;
    }

    /**
     * 電極膜厚4
     *
     * @return the dmakuatsu4
     */
    public BigDecimal getDmakuatsu4() {
        return dmakuatsu4;
    }

    /**
     * 電極膜厚4
     *
     * @param dmakuatsu4 the dmakuatsu4 to set
     */
    public void setDmakuatsu4(BigDecimal dmakuatsu4) {
        this.dmakuatsu4 = dmakuatsu4;
    }

    /**
     * 電極膜厚5
     *
     * @return the dmakuatsu5
     */
    public BigDecimal getDmakuatsu5() {
        return dmakuatsu5;
    }

    /**
     * 電極膜厚5
     *
     * @param dmakuatsu5 the dmakuatsu5 to set
     */
    public void setDmakuatsu5(BigDecimal dmakuatsu5) {
        this.dmakuatsu5 = dmakuatsu5;
    }

    /**
     * 電極膜厚6
     *
     * @return the dmakuatsu6
     */
    public BigDecimal getDmakuatsu6() {
        return dmakuatsu6;
    }

    /**
     * 電極膜厚6
     *
     * @param dmakuatsu6 the dmakuatsu6 to set
     */
    public void setDmakuatsu6(BigDecimal dmakuatsu6) {
        this.dmakuatsu6 = dmakuatsu6;
    }

    /**
     * 電極膜厚7
     *
     * @return the dmakuatsu7
     */
    public BigDecimal getDmakuatsu7() {
        return dmakuatsu7;
    }

    /**
     * 電極膜厚7
     *
     * @param dmakuatsu7 the dmakuatsu7 to set
     */
    public void setDmakuatsu7(BigDecimal dmakuatsu7) {
        this.dmakuatsu7 = dmakuatsu7;
    }

    /**
     * 電極膜厚8
     *
     * @return the dmakuatsu8
     */
    public BigDecimal getDmakuatsu8() {
        return dmakuatsu8;
    }

    /**
     * 電極膜厚8
     *
     * @param dmakuatsu8 the dmakuatsu8 to set
     */
    public void setDmakuatsu8(BigDecimal dmakuatsu8) {
        this.dmakuatsu8 = dmakuatsu8;
    }

    /**
     * 電極膜厚9
     *
     * @return the dmakuatsu9
     */
    public BigDecimal getDmakuatsu9() {
        return dmakuatsu9;
    }

    /**
     * 電極膜厚9
     *
     * @param dmakuatsu9 the dmakuatsu9 to set
     */
    public void setDmakuatsu9(BigDecimal dmakuatsu9) {
        this.dmakuatsu9 = dmakuatsu9;
    }

    /**
     * ﾊﾟﾀｰﾝ間距離1
     *
     * @return the ptndist1
     */
    public Integer getPtndist1() {
        return ptndist1;
    }

    /**
     * ﾊﾟﾀｰﾝ間距離1
     *
     * @param ptndist1 the ptndist1 to set
     */
    public void setPtndist1(Integer ptndist1) {
        this.ptndist1 = ptndist1;
    }

    /**
     * ﾊﾟﾀｰﾝ間距離2
     *
     * @return the ptndist2
     */
    public Integer getPtndist2() {
        return ptndist2;
    }

    /**
     * ﾊﾟﾀｰﾝ間距離2
     *
     * @param ptndist2 the ptndist2 to set
     */
    public void setPtndist2(Integer ptndist2) {
        this.ptndist2 = ptndist2;
    }

    /**
     * ﾊﾟﾀｰﾝ間距離3
     *
     * @return the ptndist3
     */
    public Integer getPtndist3() {
        return ptndist3;
    }

    /**
     * ﾊﾟﾀｰﾝ間距離3
     *
     * @param ptndist3 the ptndist3 to set
     */
    public void setPtndist3(Integer ptndist3) {
        this.ptndist3 = ptndist3;
    }

    /**
     * ﾊﾟﾀｰﾝ間距離4
     *
     * @return the ptndist4
     */
    public Integer getPtndist4() {
        return ptndist4;
    }

    /**
     * ﾊﾟﾀｰﾝ間距離4
     *
     * @param ptndist4 the ptndist4 to set
     */
    public void setPtndist4(Integer ptndist4) {
        this.ptndist4 = ptndist4;
    }

    /**
     * ﾊﾟﾀｰﾝ間距離5
     *
     * @return the ptndist5
     */
    public Integer getPtndist5() {
        return ptndist5;
    }

    /**
     * ﾊﾟﾀｰﾝ間距離5
     *
     * @param ptndist5 the ptndist5 to set
     */
    public void setPtndist5(Integer ptndist5) {
        this.ptndist5 = ptndist5;
    }

    /**
     * 合わせ(RZ)1
     *
     * @return the awaserz1
     */
    public BigDecimal getAwaserz1() {
        return awaserz1;
    }

    /**
     * 合わせ(RZ)1
     *
     * @param awaserz1 the awaserz1 to set
     */
    public void setAwaserz1(BigDecimal awaserz1) {
        this.awaserz1 = awaserz1;
    }

    /**
     * 合わせ(RZ)2
     *
     * @return the awaserz2
     */
    public BigDecimal getAwaserz2() {
        return awaserz2;
    }

    /**
     * 合わせ(RZ)2
     *
     * @param awaserz2 the awaserz2 to set
     */
    public void setAwaserz2(BigDecimal awaserz2) {
        this.awaserz2 = awaserz2;
    }

    /**
     * 合わせ(RZ)3
     *
     * @return the awaserz3
     */
    public BigDecimal getAwaserz3() {
        return awaserz3;
    }

    /**
     * 合わせ(RZ)3
     *
     * @param awaserz3 the awaserz3 to set
     */
    public void setAwaserz3(BigDecimal awaserz3) {
        this.awaserz3 = awaserz3;
    }

    /**
     * 合わせ(RZ)4
     *
     * @return the awaserz4
     */
    public BigDecimal getAwaserz4() {
        return awaserz4;
    }

    /**
     * 合わせ(RZ)4
     *
     * @param awaserz4 the awaserz4 to set
     */
    public void setAwaserz4(BigDecimal awaserz4) {
        this.awaserz4 = awaserz4;
    }

    /**
     * 合わせ(RZ)5
     *
     * @return the awaserz5
     */
    public BigDecimal getAwaserz5() {
        return awaserz5;
    }

    /**
     * 合わせ(RZ)5
     *
     * @param awaserz5 the awaserz5 to set
     */
    public void setAwaserz5(BigDecimal awaserz5) {
        this.awaserz5 = awaserz5;
    }

    /**
     * 合わせ(RZ)6
     *
     * @return the awaserz6
     */
    public BigDecimal getAwaserz6() {
        return awaserz6;
    }

    /**
     * 合わせ(RZ)6
     *
     * @param awaserz6 the awaserz6 to set
     */
    public void setAwaserz6(BigDecimal awaserz6) {
        this.awaserz6 = awaserz6;
    }

    /**
     * 合わせ(RZ)7
     *
     * @return the awaserz7
     */
    public BigDecimal getAwaserz7() {
        return awaserz7;
    }

    /**
     * 合わせ(RZ)7
     *
     * @param awaserz7 the awaserz7 to set
     */
    public void setAwaserz7(BigDecimal awaserz7) {
        this.awaserz7 = awaserz7;
    }

    /**
     * 合わせ(RZ)8
     *
     * @return the awaserz8
     */
    public BigDecimal getAwaserz8() {
        return awaserz8;
    }

    /**
     * 合わせ(RZ)8
     *
     * @param awaserz8 the awaserz8 to set
     */
    public void setAwaserz8(BigDecimal awaserz8) {
        this.awaserz8 = awaserz8;
    }

    /**
     * 合わせ(RZ)9
     *
     * @return the awaserz9
     */
    public BigDecimal getAwaserz9() {
        return awaserz9;
    }

    /**
     * 合わせ(RZ)9
     *
     * @param awaserz9 the awaserz9 to set
     */
    public void setAwaserz9(BigDecimal awaserz9) {
        this.awaserz9 = awaserz9;
    }

    /**
     * 被り量（μｍ）1
     *
     * @return the kaburihidariuex1
     */
    public Integer getKaburihidariuex1() {
        return kaburihidariuex1;
    }

    /**
     * 被り量（μｍ）1
     *
     * @param kaburihidariuex1 the kaburihidariuex1 to set
     */
    public void setKaburihidariuex1(Integer kaburihidariuex1) {
        this.kaburihidariuex1 = kaburihidariuex1;
    }

    /**
     * 被り量（μｍ）2
     *
     * @return the kaburihidariuex2
     */
    public Integer getKaburihidariuex2() {
        return kaburihidariuex2;
    }

    /**
     * 被り量（μｍ）2
     *
     * @param kaburihidariuex2 the kaburihidariuex2 to set
     */
    public void setKaburihidariuex2(Integer kaburihidariuex2) {
        this.kaburihidariuex2 = kaburihidariuex2;
    }

    /**
     * 被り量（μｍ）3
     *
     * @return the kaburihidariuey1
     */
    public Integer getKaburihidariuey1() {
        return kaburihidariuey1;
    }

    /**
     * 被り量（μｍ）3
     *
     * @param kaburihidariuey1 the kaburihidariuey1 to set
     */
    public void setKaburihidariuey1(Integer kaburihidariuey1) {
        this.kaburihidariuey1 = kaburihidariuey1;
    }

    /**
     * 被り量（μｍ）4
     *
     * @return the kaburihidariuey2
     */
    public Integer getKaburihidariuey2() {
        return kaburihidariuey2;
    }

    /**
     * 被り量（μｍ）4
     *
     * @param kaburihidariuey2 the kaburihidariuey2 to set
     */
    public void setKaburihidariuey2(Integer kaburihidariuey2) {
        this.kaburihidariuey2 = kaburihidariuey2;
    }

    /**
     * 被り量（μｍ）5
     *
     * @return the kaburihidarisitax1
     */
    public Integer getKaburihidarisitax1() {
        return kaburihidarisitax1;
    }

    /**
     * 被り量（μｍ）5
     *
     * @param kaburihidarisitax1 the kaburihidarisitax1 to set
     */
    public void setKaburihidarisitax1(Integer kaburihidarisitax1) {
        this.kaburihidarisitax1 = kaburihidarisitax1;
    }

    /**
     * 被り量（μｍ）6
     *
     * @return the kaburihidarisitax2
     */
    public Integer getKaburihidarisitax2() {
        return kaburihidarisitax2;
    }

    /**
     * 被り量（μｍ）6
     *
     * @param kaburihidarisitax2 the kaburihidarisitax2 to set
     */
    public void setKaburihidarisitax2(Integer kaburihidarisitax2) {
        this.kaburihidarisitax2 = kaburihidarisitax2;
    }

    /**
     * 被り量（μｍ）7
     *
     * @return the kaburihidarisitay1
     */
    public Integer getKaburihidarisitay1() {
        return kaburihidarisitay1;
    }

    /**
     * 被り量（μｍ）7
     *
     * @param kaburihidarisitay1 the kaburihidarisitay1 to set
     */
    public void setKaburihidarisitay1(Integer kaburihidarisitay1) {
        this.kaburihidarisitay1 = kaburihidarisitay1;
    }

    /**
     * 被り量（μｍ）8
     *
     * @return the kaburihidarisitay2
     */
    public Integer getKaburihidarisitay2() {
        return kaburihidarisitay2;
    }

    /**
     * 被り量（μｍ）8
     *
     * @param kaburihidarisitay2 the kaburihidarisitay2 to set
     */
    public void setKaburihidarisitay2(Integer kaburihidarisitay2) {
        this.kaburihidarisitay2 = kaburihidarisitay2;
    }

    /**
     * 被り量（μｍ）9
     *
     * @return the kaburihidaricenterx1
     */
    public Integer getKaburihidaricenterx1() {
        return kaburihidaricenterx1;
    }

    /**
     * 被り量（μｍ）9
     *
     * @param kaburihidaricenterx1 the kaburihidaricenterx1 to set
     */
    public void setKaburihidaricenterx1(Integer kaburihidaricenterx1) {
        this.kaburihidaricenterx1 = kaburihidaricenterx1;
    }

    /**
     * 被り量（μｍ）10
     *
     * @return the kaburihidaricenterx2
     */
    public Integer getKaburihidaricenterx2() {
        return kaburihidaricenterx2;
    }

    /**
     * 被り量（μｍ）10
     *
     * @param kaburihidaricenterx2 the kaburihidaricenterx2 to set
     */
    public void setKaburihidaricenterx2(Integer kaburihidaricenterx2) {
        this.kaburihidaricenterx2 = kaburihidaricenterx2;
    }

    /**
     * 被り量（μｍ）11
     *
     * @return the kaburihidaricentery1
     */
    public Integer getKaburihidaricentery1() {
        return kaburihidaricentery1;
    }

    /**
     * 被り量（μｍ）11
     *
     * @param kaburihidaricentery1 the kaburihidaricentery1 to set
     */
    public void setKaburihidaricentery1(Integer kaburihidaricentery1) {
        this.kaburihidaricentery1 = kaburihidaricentery1;
    }

    /**
     * 被り量（μｍ）12
     *
     * @return the kaburihidaricentery2
     */
    public Integer getKaburihidaricentery2() {
        return kaburihidaricentery2;
    }

    /**
     * 被り量（μｍ）12
     *
     * @param kaburihidaricentery2 the kaburihidaricentery2 to set
     */
    public void setKaburihidaricentery2(Integer kaburihidaricentery2) {
        this.kaburihidaricentery2 = kaburihidaricentery2;
    }

    /**
     * 被り量（μｍ）13
     *
     * @return the kaburimigiuex1
     */
    public Integer getKaburimigiuex1() {
        return kaburimigiuex1;
    }

    /**
     * 被り量（μｍ）13
     *
     * @param kaburimigiuex1 the kaburimigiuex1 to set
     */
    public void setKaburimigiuex1(Integer kaburimigiuex1) {
        this.kaburimigiuex1 = kaburimigiuex1;
    }

    /**
     * 被り量（μｍ）14
     *
     * @return the kaburimigiuex2
     */
    public Integer getKaburimigiuex2() {
        return kaburimigiuex2;
    }

    /**
     * 被り量（μｍ）14
     *
     * @param kaburimigiuex2 the kaburimigiuex2 to set
     */
    public void setKaburimigiuex2(Integer kaburimigiuex2) {
        this.kaburimigiuex2 = kaburimigiuex2;
    }

    /**
     * 被り量（μｍ）15
     *
     * @return the kaburimigiuey1
     */
    public Integer getKaburimigiuey1() {
        return kaburimigiuey1;
    }

    /**
     * 被り量（μｍ）15
     *
     * @param kaburimigiuey1 the kaburimigiuey1 to set
     */
    public void setKaburimigiuey1(Integer kaburimigiuey1) {
        this.kaburimigiuey1 = kaburimigiuey1;
    }

    /**
     * 被り量（μｍ）16
     *
     * @return the kaburimigiuey2
     */
    public Integer getKaburimigiuey2() {
        return kaburimigiuey2;
    }

    /**
     * 被り量（μｍ）16
     *
     * @param kaburimigiuey2 the kaburimigiuey2 to set
     */
    public void setKaburimigiuey2(Integer kaburimigiuey2) {
        this.kaburimigiuey2 = kaburimigiuey2;
    }

    /**
     * 被り量（μｍ）17
     *
     * @return the kaburimigisitax1
     */
    public Integer getKaburimigisitax1() {
        return kaburimigisitax1;
    }

    /**
     * 被り量（μｍ）17
     *
     * @param kaburimigisitax1 the kaburimigisitax1 to set
     */
    public void setKaburimigisitax1(Integer kaburimigisitax1) {
        this.kaburimigisitax1 = kaburimigisitax1;
    }

    /**
     * 被り量（μｍ）18
     *
     * @return the kaburimigisitax2
     */
    public Integer getKaburimigisitax2() {
        return kaburimigisitax2;
    }

    /**
     * 被り量（μｍ）18
     *
     * @param kaburimigisitax2 the kaburimigisitax2 to set
     */
    public void setKaburimigisitax2(Integer kaburimigisitax2) {
        this.kaburimigisitax2 = kaburimigisitax2;
    }

    /**
     * 被り量（μｍ）19
     *
     * @return the kaburimigisitay1
     */
    public Integer getKaburimigisitay1() {
        return kaburimigisitay1;
    }

    /**
     * 被り量（μｍ）19
     *
     * @param kaburimigisitay1 the kaburimigisitay1 to set
     */
    public void setKaburimigisitay1(Integer kaburimigisitay1) {
        this.kaburimigisitay1 = kaburimigisitay1;
    }

    /**
     * 被り量（μｍ）20
     *
     * @return the kaburimigisitay2
     */
    public Integer getKaburimigisitay2() {
        return kaburimigisitay2;
    }

    /**
     * 被り量（μｍ）20
     *
     * @param kaburimigisitay2 the kaburimigisitay2 to set
     */
    public void setKaburimigisitay2(Integer kaburimigisitay2) {
        this.kaburimigisitay2 = kaburimigisitay2;
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
    public Long getRevision() {
        return revision;
    }

    /**
     * revision
     *
     * @param revision the revision to set
     */
    public void setRevision(Long revision) {
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
