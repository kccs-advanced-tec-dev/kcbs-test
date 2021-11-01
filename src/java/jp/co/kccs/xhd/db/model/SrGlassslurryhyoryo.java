/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/09/08<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SR_GLASSSLURRYHYORYO(ｶﾞﾗｽｽﾗﾘｰ作製・秤量)のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/09/08
 */
public class SrGlassslurryhyoryo {
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
     * ｶﾞﾗｽｽﾗﾘｰ品名
     */
    private String glassslurryhinmei;

    /**
     * ｶﾞﾗｽｽﾗﾘｰ品名LotNo
     */
    private String glassslurrylotno;

    /**
     * ﾛｯﾄ区分
     */
    private String lotkubun;

    /**
     * 粉砕ﾎﾟｯﾄｻｲｽﾞ
     */
    private String fusaipottosize;

    /**
     * 玉石径
     */
    private String tamaishikei;

    /**
     * 秤量号機
     */
    private String goki;

    /**
     * ﾎﾟｯﾄ1_材料品名1
     */
    private String potto1_zairyohinmei1;

    /**
     * ﾎﾟｯﾄ1_調合量規格1
     */
    private String potto1_tyogouryoukikaku1;

    /**
     * ﾎﾟｯﾄ1_資材ﾛｯﾄNo.1_1
     */
    private String potto1_sizailotno1_1;

    /**
     * ﾎﾟｯﾄ1_調合量1_1
     */
    private Integer potto1_tyougouryou1_1;

    /**
     * ﾎﾟｯﾄ1_資材ﾛｯﾄNo.1_2
     */
    private String potto1_sizailotno1_2;

    /**
     * ﾎﾟｯﾄ1_調合量1_2
     */
    private Integer potto1_tyougouryou1_2;

    /**
     * ﾎﾟｯﾄ1_材料品名2
     */
    private String potto1_zairyohinmei2;

    /**
     * ﾎﾟｯﾄ1_調合量規格2
     */
    private String potto1_tyogouryoukikaku2;

    /**
     * ﾎﾟｯﾄ1_資材ﾛｯﾄNo.2_1
     */
    private String potto1_sizailotno2_1;

    /**
     * ﾎﾟｯﾄ1_調合量2_1
     */
    private Integer potto1_tyougouryou2_1;

    /**
     * ﾎﾟｯﾄ1_資材ﾛｯﾄNo.2_2
     */
    private String potto1_sizailotno2_2;

    /**
     * ﾎﾟｯﾄ1_調合量2_2
     */
    private Integer potto1_tyougouryou2_2;

    /**
     * ﾎﾟｯﾄ1_材料品名3
     */
    private String potto1_zairyohinmei3;

    /**
     * ﾎﾟｯﾄ1_調合量規格3
     */
    private String potto1_tyogouryoukikaku3;

    /**
     * ﾎﾟｯﾄ1_資材ﾛｯﾄNo.3_1
     */
    private String potto1_sizailotno3_1;

    /**
     * ﾎﾟｯﾄ1_調合量3_1
     */
    private Integer potto1_tyougouryou3_1;

    /**
     * ﾎﾟｯﾄ1_資材ﾛｯﾄNo.3_2
     */
    private String potto1_sizailotno3_2;

    /**
     * ﾎﾟｯﾄ1_調合量3_2
     */
    private Integer potto1_tyougouryou3_2;

    /**
     * ﾎﾟｯﾄ1_材料品名4
     */
    private String potto1_zairyohinmei4;

    /**
     * ﾎﾟｯﾄ1_調合量規格4
     */
    private String potto1_tyogouryoukikaku4;

    /**
     * ﾎﾟｯﾄ1_資材ﾛｯﾄNo.4_1
     */
    private String potto1_sizailotno4_1;

    /**
     * ﾎﾟｯﾄ1_調合量4_1
     */
    private Integer potto1_tyougouryou4_1;

    /**
     * ﾎﾟｯﾄ1_資材ﾛｯﾄNo.4_2
     */
    private String potto1_sizailotno4_2;

    /**
     * ﾎﾟｯﾄ1_調合量4_2
     */
    private Integer potto1_tyougouryou4_2;

    /**
     * ﾎﾟｯﾄ2_材料品名1
     */
    private String potto2_zairyohinmei1;

    /**
     * ﾎﾟｯﾄ2_調合量規格1
     */
    private String potto2_tyogouryoukikaku1;

    /**
     * ﾎﾟｯﾄ2_資材ﾛｯﾄNo.1_1
     */
    private String potto2_sizailotno1_1;

    /**
     * ﾎﾟｯﾄ2_調合量1_1
     */
    private Integer potto2_tyougouryou1_1;

    /**
     * ﾎﾟｯﾄ2_資材ﾛｯﾄNo.1_2
     */
    private String potto2_sizailotno1_2;

    /**
     * ﾎﾟｯﾄ2_調合量1_2
     */
    private Integer potto2_tyougouryou1_2;

    /**
     * ﾎﾟｯﾄ2_材料品名2
     */
    private String potto2_zairyohinmei2;

    /**
     * ﾎﾟｯﾄ2_調合量規格2
     */
    private String potto2_tyogouryoukikaku2;

    /**
     * ﾎﾟｯﾄ2_資材ﾛｯﾄNo.2_1
     */
    private String potto2_sizailotno2_1;

    /**
     * ﾎﾟｯﾄ2_調合量2_1
     */
    private Integer potto2_tyougouryou2_1;

    /**
     * ﾎﾟｯﾄ2_資材ﾛｯﾄNo.2_2
     */
    private String potto2_sizailotno2_2;

    /**
     * ﾎﾟｯﾄ2_調合量2_2
     */
    private Integer potto2_tyougouryou2_2;

    /**
     * ﾎﾟｯﾄ2_材料品名3
     */
    private String potto2_zairyohinmei3;

    /**
     * ﾎﾟｯﾄ2_調合量規格3
     */
    private String potto2_tyogouryoukikaku3;

    /**
     * ﾎﾟｯﾄ2_資材ﾛｯﾄNo.3_1
     */
    private String potto2_sizailotno3_1;

    /**
     * ﾎﾟｯﾄ2_調合量3_1
     */
    private Integer potto2_tyougouryou3_1;

    /**
     * ﾎﾟｯﾄ2_資材ﾛｯﾄNo.3_2
     */
    private String potto2_sizailotno3_2;

    /**
     * ﾎﾟｯﾄ2_調合量3_2
     */
    private Integer potto2_tyougouryou3_2;

    /**
     * ﾎﾟｯﾄ2_材料品名4
     */
    private String potto2_zairyohinmei4;

    /**
     * ﾎﾟｯﾄ2_調合量規格4
     */
    private String potto2_tyogouryoukikaku4;

    /**
     * ﾎﾟｯﾄ2_資材ﾛｯﾄNo.4_1
     */
    private String potto2_sizailotno4_1;

    /**
     * ﾎﾟｯﾄ2_調合量4_1
     */
    private Integer potto2_tyougouryou4_1;

    /**
     * ﾎﾟｯﾄ2_資材ﾛｯﾄNo.4_2
     */
    private String potto2_sizailotno4_2;

    /**
     * ﾎﾟｯﾄ2_調合量4_2
     */
    private Integer potto2_tyougouryou4_2;

    /**
     * ﾎﾟｯﾄ3_材料品名1
     */
    private String potto3_zairyohinmei1;

    /**
     * ﾎﾟｯﾄ3_調合量規格1
     */
    private String potto3_tyogouryoukikaku1;

    /**
     * ﾎﾟｯﾄ3_資材ﾛｯﾄNo.1_1
     */
    private String potto3_sizailotno1_1;

    /**
     * ﾎﾟｯﾄ3_調合量1_1
     */
    private Integer potto3_tyougouryou1_1;

    /**
     * ﾎﾟｯﾄ3_資材ﾛｯﾄNo.1_2
     */
    private String potto3_sizailotno1_2;

    /**
     * ﾎﾟｯﾄ3_調合量1_2
     */
    private Integer potto3_tyougouryou1_2;

    /**
     * ﾎﾟｯﾄ3_材料品名2
     */
    private String potto3_zairyohinmei2;

    /**
     * ﾎﾟｯﾄ3_調合量規格2
     */
    private String potto3_tyogouryoukikaku2;

    /**
     * ﾎﾟｯﾄ3_資材ﾛｯﾄNo.2_1
     */
    private String potto3_sizailotno2_1;

    /**
     * ﾎﾟｯﾄ3_調合量2_1
     */
    private Integer potto3_tyougouryou2_1;

    /**
     * ﾎﾟｯﾄ3_資材ﾛｯﾄNo.2_2
     */
    private String potto3_sizailotno2_2;

    /**
     * ﾎﾟｯﾄ3_調合量2_2
     */
    private Integer potto3_tyougouryou2_2;

    /**
     * ﾎﾟｯﾄ3_材料品名3
     */
    private String potto3_zairyohinmei3;

    /**
     * ﾎﾟｯﾄ3_調合量規格3
     */
    private String potto3_tyogouryoukikaku3;

    /**
     * ﾎﾟｯﾄ3_資材ﾛｯﾄNo.3_1
     */
    private String potto3_sizailotno3_1;

    /**
     * ﾎﾟｯﾄ3_調合量3_1
     */
    private Integer potto3_tyougouryou3_1;

    /**
     * ﾎﾟｯﾄ3_資材ﾛｯﾄNo.3_2
     */
    private String potto3_sizailotno3_2;

    /**
     * ﾎﾟｯﾄ3_調合量3_2
     */
    private Integer potto3_tyougouryou3_2;

    /**
     * ﾎﾟｯﾄ3_材料品名4
     */
    private String potto3_zairyohinmei4;

    /**
     * ﾎﾟｯﾄ3_調合量規格4
     */
    private String potto3_tyogouryoukikaku4;

    /**
     * ﾎﾟｯﾄ3_資材ﾛｯﾄNo.4_1
     */
    private String potto3_sizailotno4_1;

    /**
     * ﾎﾟｯﾄ3_調合量4_1
     */
    private Integer potto3_tyougouryou4_1;

    /**
     * ﾎﾟｯﾄ3_資材ﾛｯﾄNo.4_2
     */
    private String potto3_sizailotno4_2;

    /**
     * ﾎﾟｯﾄ3_調合量4_2
     */
    private Integer potto3_tyougouryou4_2;

    /**
     * ﾎﾟｯﾄ4_材料品名1
     */
    private String potto4_zairyohinmei1;

    /**
     * ﾎﾟｯﾄ4_調合量規格1
     */
    private String potto4_tyogouryoukikaku1;

    /**
     * ﾎﾟｯﾄ4_資材ﾛｯﾄNo.1_1
     */
    private String potto4_sizailotno1_1;

    /**
     * ﾎﾟｯﾄ4_調合量1_1
     */
    private Integer potto4_tyougouryou1_1;

    /**
     * ﾎﾟｯﾄ4_資材ﾛｯﾄNo.1_2
     */
    private String potto4_sizailotno1_2;

    /**
     * ﾎﾟｯﾄ4_調合量1_2
     */
    private Integer potto4_tyougouryou1_2;

    /**
     * ﾎﾟｯﾄ4_材料品名2
     */
    private String potto4_zairyohinmei2;

    /**
     * ﾎﾟｯﾄ4_調合量規格2
     */
    private String potto4_tyogouryoukikaku2;

    /**
     * ﾎﾟｯﾄ4_資材ﾛｯﾄNo.2_1
     */
    private String potto4_sizailotno2_1;

    /**
     * ﾎﾟｯﾄ4_調合量2_1
     */
    private Integer potto4_tyougouryou2_1;

    /**
     * ﾎﾟｯﾄ4_資材ﾛｯﾄNo.2_2
     */
    private String potto4_sizailotno2_2;

    /**
     * ﾎﾟｯﾄ4_調合量2_2
     */
    private Integer potto4_tyougouryou2_2;

    /**
     * ﾎﾟｯﾄ4_材料品名3
     */
    private String potto4_zairyohinmei3;

    /**
     * ﾎﾟｯﾄ4_調合量規格3
     */
    private String potto4_tyogouryoukikaku3;

    /**
     * ﾎﾟｯﾄ4_資材ﾛｯﾄNo.3_1
     */
    private String potto4_sizailotno3_1;

    /**
     * ﾎﾟｯﾄ4_調合量3_1
     */
    private Integer potto4_tyougouryou3_1;

    /**
     * ﾎﾟｯﾄ4_資材ﾛｯﾄNo.3_2
     */
    private String potto4_sizailotno3_2;

    /**
     * ﾎﾟｯﾄ4_調合量3_2
     */
    private Integer potto4_tyougouryou3_2;

    /**
     * ﾎﾟｯﾄ4_材料品名4
     */
    private String potto4_zairyohinmei4;

    /**
     * ﾎﾟｯﾄ4_調合量規格4
     */
    private String potto4_tyogouryoukikaku4;

    /**
     * ﾎﾟｯﾄ4_資材ﾛｯﾄNo.4_1
     */
    private String potto4_sizailotno4_1;

    /**
     * ﾎﾟｯﾄ4_調合量4_1
     */
    private Integer potto4_tyougouryou4_1;

    /**
     * ﾎﾟｯﾄ4_資材ﾛｯﾄNo.4_2
     */
    private String potto4_sizailotno4_2;

    /**
     * ﾎﾟｯﾄ4_調合量4_2
     */
    private Integer potto4_tyougouryou4_2;

    /**
     * ﾎﾟｯﾄ5_材料品名1
     */
    private String potto5_zairyohinmei1;

    /**
     * ﾎﾟｯﾄ5_調合量規格1
     */
    private String potto5_tyogouryoukikaku1;

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.1_1
     */
    private String potto5_sizailotno1_1;

    /**
     * ﾎﾟｯﾄ5_調合量1_1
     */
    private Integer potto5_tyougouryou1_1;

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.1_2
     */
    private String potto5_sizailotno1_2;

    /**
     * ﾎﾟｯﾄ5_調合量1_2
     */
    private Integer potto5_tyougouryou1_2;

    /**
     * ﾎﾟｯﾄ5_材料品名2
     */
    private String potto5_zairyohinmei2;

    /**
     * ﾎﾟｯﾄ5_調合量規格2
     */
    private String potto5_tyogouryoukikaku2;

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.2_1
     */
    private String potto5_sizailotno2_1;

    /**
     * ﾎﾟｯﾄ5_調合量2_1
     */
    private Integer potto5_tyougouryou2_1;

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.2_2
     */
    private String potto5_sizailotno2_2;

    /**
     * ﾎﾟｯﾄ5_調合量2_2
     */
    private Integer potto5_tyougouryou2_2;

    /**
     * ﾎﾟｯﾄ5_材料品名3
     */
    private String potto5_zairyohinmei3;

    /**
     * ﾎﾟｯﾄ5_調合量規格3
     */
    private String potto5_tyogouryoukikaku3;

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.3_1
     */
    private String potto5_sizailotno3_1;

    /**
     * ﾎﾟｯﾄ5_調合量3_1
     */
    private Integer potto5_tyougouryou3_1;

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.3_2
     */
    private String potto5_sizailotno3_2;

    /**
     * ﾎﾟｯﾄ5_調合量3_2
     */
    private Integer potto5_tyougouryou3_2;

    /**
     * ﾎﾟｯﾄ5_材料品名4
     */
    private String potto5_zairyohinmei4;

    /**
     * ﾎﾟｯﾄ5_調合量規格4
     */
    private String potto5_tyogouryoukikaku4;

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.4_1
     */
    private String potto5_sizailotno4_1;

    /**
     * ﾎﾟｯﾄ5_調合量4_1
     */
    private Integer potto5_tyougouryou4_1;

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.4_2
     */
    private String potto5_sizailotno4_2;

    /**
     * ﾎﾟｯﾄ5_調合量4_2
     */
    private Integer potto5_tyougouryou4_2;

    /**
     * ﾎﾟｯﾄ6_材料品名1
     */
    private String potto6_zairyohinmei1;

    /**
     * ﾎﾟｯﾄ6_調合量規格1
     */
    private String potto6_tyogouryoukikaku1;

    /**
     * ﾎﾟｯﾄ6_資材ﾛｯﾄNo.1_1
     */
    private String potto6_sizailotno1_1;

    /**
     * ﾎﾟｯﾄ6_調合量1_1
     */
    private Integer potto6_tyougouryou1_1;

    /**
     * ﾎﾟｯﾄ6_資材ﾛｯﾄNo.1_2
     */
    private String potto6_sizailotno1_2;

    /**
     * ﾎﾟｯﾄ6_調合量1_2
     */
    private Integer potto6_tyougouryou1_2;

    /**
     * ﾎﾟｯﾄ6_材料品名2
     */
    private String potto6_zairyohinmei2;

    /**
     * ﾎﾟｯﾄ6_調合量規格2
     */
    private String potto6_tyogouryoukikaku2;

    /**
     * ﾎﾟｯﾄ6_資材ﾛｯﾄNo.2_1
     */
    private String potto6_sizailotno2_1;

    /**
     * ﾎﾟｯﾄ6_調合量2_1
     */
    private Integer potto6_tyougouryou2_1;

    /**
     * ﾎﾟｯﾄ6_資材ﾛｯﾄNo.2_2
     */
    private String potto6_sizailotno2_2;

    /**
     * ﾎﾟｯﾄ6_調合量2_2
     */
    private Integer potto6_tyougouryou2_2;

    /**
     * ﾎﾟｯﾄ6_材料品名3
     */
    private String potto6_zairyohinmei3;

    /**
     * ﾎﾟｯﾄ6_調合量規格3
     */
    private String potto6_tyogouryoukikaku3;

    /**
     * ﾎﾟｯﾄ6_資材ﾛｯﾄNo.3_1
     */
    private String potto6_sizailotno3_1;

    /**
     * ﾎﾟｯﾄ6_調合量3_1
     */
    private Integer potto6_tyougouryou3_1;

    /**
     * ﾎﾟｯﾄ6_資材ﾛｯﾄNo.3_2
     */
    private String potto6_sizailotno3_2;

    /**
     * ﾎﾟｯﾄ6_調合量3_2
     */
    private Integer potto6_tyougouryou3_2;

    /**
     * ﾎﾟｯﾄ6_材料品名4
     */
    private String potto6_zairyohinmei4;

    /**
     * ﾎﾟｯﾄ6_調合量規格4
     */
    private String potto6_tyogouryoukikaku4;

    /**
     * ﾎﾟｯﾄ6_資材ﾛｯﾄNo.4_1
     */
    private String potto6_sizailotno4_1;

    /**
     * ﾎﾟｯﾄ6_調合量4_1
     */
    private Integer potto6_tyougouryou4_1;

    /**
     * ﾎﾟｯﾄ6_資材ﾛｯﾄNo.4_2
     */
    private String potto6_sizailotno4_2;

    /**
     * ﾎﾟｯﾄ6_調合量4_2
     */
    private Integer potto6_tyougouryou4_2;

    /**
     * ﾎﾟｯﾄ7_材料品名1
     */
    private String potto7_zairyohinmei1;

    /**
     * ﾎﾟｯﾄ7_調合量規格1
     */
    private String potto7_tyogouryoukikaku1;

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.1_1
     */
    private String potto7_sizailotno1_1;

    /**
     * ﾎﾟｯﾄ7_調合量1_1
     */
    private Integer potto7_tyougouryou1_1;

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.1_2
     */
    private String potto7_sizailotno1_2;

    /**
     * ﾎﾟｯﾄ7_調合量1_2
     */
    private Integer potto7_tyougouryou1_2;

    /**
     * ﾎﾟｯﾄ7_材料品名2
     */
    private String potto7_zairyohinmei2;

    /**
     * ﾎﾟｯﾄ7_調合量規格2
     */
    private String potto7_tyogouryoukikaku2;

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.2_1
     */
    private String potto7_sizailotno2_1;

    /**
     * ﾎﾟｯﾄ7_調合量2_1
     */
    private Integer potto7_tyougouryou2_1;

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.2_2
     */
    private String potto7_sizailotno2_2;

    /**
     * ﾎﾟｯﾄ7_調合量2_2
     */
    private Integer potto7_tyougouryou2_2;

    /**
     * ﾎﾟｯﾄ7_材料品名3
     */
    private String potto7_zairyohinmei3;

    /**
     * ﾎﾟｯﾄ7_調合量規格3
     */
    private String potto7_tyogouryoukikaku3;

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.3_1
     */
    private String potto7_sizailotno3_1;

    /**
     * ﾎﾟｯﾄ7_調合量3_1
     */
    private Integer potto7_tyougouryou3_1;

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.3_2
     */
    private String potto7_sizailotno3_2;

    /**
     * ﾎﾟｯﾄ7_調合量3_2
     */
    private Integer potto7_tyougouryou3_2;

    /**
     * ﾎﾟｯﾄ7_材料品名4
     */
    private String potto7_zairyohinmei4;

    /**
     * ﾎﾟｯﾄ7_調合量規格4
     */
    private String potto7_tyogouryoukikaku4;

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.4_1
     */
    private String potto7_sizailotno4_1;

    /**
     * ﾎﾟｯﾄ7_調合量4_1
     */
    private Integer potto7_tyougouryou4_1;

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.4_2
     */
    private String potto7_sizailotno4_2;

    /**
     * ﾎﾟｯﾄ7_調合量4_2
     */
    private Integer potto7_tyougouryou4_2;

    /**
     * ﾎﾟｯﾄ8_材料品名1
     */
    private String potto8_zairyohinmei1;

    /**
     * ﾎﾟｯﾄ8_調合量規格1
     */
    private String potto8_tyogouryoukikaku1;

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.1_1
     */
    private String potto8_sizailotno1_1;

    /**
     * ﾎﾟｯﾄ8_調合量1_1
     */
    private Integer potto8_tyougouryou1_1;

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.1_2
     */
    private String potto8_sizailotno1_2;

    /**
     * ﾎﾟｯﾄ8_調合量1_2
     */
    private Integer potto8_tyougouryou1_2;

    /**
     * ﾎﾟｯﾄ8_材料品名2
     */
    private String potto8_zairyohinmei2;

    /**
     * ﾎﾟｯﾄ8_調合量規格2
     */
    private String potto8_tyogouryoukikaku2;

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.2_1
     */
    private String potto8_sizailotno2_1;

    /**
     * ﾎﾟｯﾄ8_調合量2_1
     */
    private Integer potto8_tyougouryou2_1;

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.2_2
     */
    private String potto8_sizailotno2_2;

    /**
     * ﾎﾟｯﾄ8_調合量2_2
     */
    private Integer potto8_tyougouryou2_2;

    /**
     * ﾎﾟｯﾄ8_材料品名3
     */
    private String potto8_zairyohinmei3;

    /**
     * ﾎﾟｯﾄ8_調合量規格3
     */
    private String potto8_tyogouryoukikaku3;

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.3_1
     */
    private String potto8_sizailotno3_1;

    /**
     * ﾎﾟｯﾄ8_調合量3_1
     */
    private Integer potto8_tyougouryou3_1;

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.3_2
     */
    private String potto8_sizailotno3_2;

    /**
     * ﾎﾟｯﾄ8_調合量3_2
     */
    private Integer potto8_tyougouryou3_2;

    /**
     * ﾎﾟｯﾄ8_材料品名4
     */
    private String potto8_zairyohinmei4;

    /**
     * ﾎﾟｯﾄ8_調合量規格4
     */
    private String potto8_tyogouryoukikaku4;

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.4_1
     */
    private String potto8_sizailotno4_1;

    /**
     * ﾎﾟｯﾄ8_調合量4_1
     */
    private Integer potto8_tyougouryou4_1;

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.4_2
     */
    private String potto8_sizailotno4_2;

    /**
     * ﾎﾟｯﾄ8_調合量4_2
     */
    private Integer potto8_tyougouryou4_2;

    /**
     * 秤量日時
     */
    private Timestamp hyouryounichiji;

    /**
     * 担当者
     */
    private String tantousya;

    /**
     * 確認者
     */
    private String kakuninsya;

    /**
     * 備考1
     */
    private String bikou1;

    /**
     * 備考2
     */
    private String bikou2;

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
    private Integer revision;

    /**
     * 削除ﾌﾗｸﾞ
     */
    private Integer deleteflag;

    /**
     * 工場ｺｰﾄﾞ
     * @return the kojyo
     */
    public String getKojyo() {
        return kojyo;
    }

    /**
     * 工場ｺｰﾄﾞ
     * @param kojyo the kojyo to set
     */
    public void setKojyo(String kojyo) {
        this.kojyo = kojyo;
    }

    /**
     * ﾛｯﾄNo
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * ﾛｯﾄNo
     * @param lotno the lotno to set
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * 枝番
     * @return the edaban
     */
    public String getEdaban() {
        return edaban;
    }

    /**
     * 枝番
     * @param edaban the edaban to set
     */
    public void setEdaban(String edaban) {
        this.edaban = edaban;
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ品名
     * @return the glassslurryhinmei
     */
    public String getGlassslurryhinmei() {
        return glassslurryhinmei;
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ品名
     * @param glassslurryhinmei the glassslurryhinmei to set
     */
    public void setGlassslurryhinmei(String glassslurryhinmei) {
        this.glassslurryhinmei = glassslurryhinmei;
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ品名LotNo
     * @return the glassslurrylotno
     */
    public String getGlassslurrylotno() {
        return glassslurrylotno;
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ品名LotNo
     * @param glassslurrylotno the glassslurrylotno to set
     */
    public void setGlassslurrylotno(String glassslurrylotno) {
        this.glassslurrylotno = glassslurrylotno;
    }

    /**
     * ﾛｯﾄ区分
     * @return the lotkubun
     */
    public String getLotkubun() {
        return lotkubun;
    }

    /**
     * ﾛｯﾄ区分
     * @param lotkubun the lotkubun to set
     */
    public void setLotkubun(String lotkubun) {
        this.lotkubun = lotkubun;
    }

    /**
     * 粉砕ﾎﾟｯﾄｻｲｽﾞ
     * @return the fusaipottosize
     */
    public String getFusaipottosize() {
        return fusaipottosize;
    }

    /**
     * 粉砕ﾎﾟｯﾄｻｲｽﾞ
     * @param fusaipottosize the fusaipottosize to set
     */
    public void setFusaipottosize(String fusaipottosize) {
        this.fusaipottosize = fusaipottosize;
    }

    /**
     * 玉石径
     * @return the tamaishikei
     */
    public String getTamaishikei() {
        return tamaishikei;
    }

    /**
     * 玉石径
     * @param tamaishikei the tamaishikei to set
     */
    public void setTamaishikei(String tamaishikei) {
        this.tamaishikei = tamaishikei;
    }

    /**
     * 秤量号機
     * @return the goki
     */
    public String getGoki() {
        return goki;
    }

    /**
     * 秤量号機
     * @param goki the goki to set
     */
    public void setGoki(String goki) {
        this.goki = goki;
    }

    /**
     * ﾎﾟｯﾄ1_材料品名1
     * @return the potto1_zairyohinmei1
     */
    public String getPotto1_zairyohinmei1() {
        return potto1_zairyohinmei1;
    }

    /**
     * ﾎﾟｯﾄ1_材料品名1
     * @param potto1_zairyohinmei1 the potto1_zairyohinmei1 to set
     */
    public void setPotto1_zairyohinmei1(String potto1_zairyohinmei1) {
        this.potto1_zairyohinmei1 = potto1_zairyohinmei1;
    }

    /**
     * ﾎﾟｯﾄ1_調合量規格1
     * @return the potto1_tyogouryoukikaku1
     */
    public String getPotto1_tyogouryoukikaku1() {
        return potto1_tyogouryoukikaku1;
    }

    /**
     * ﾎﾟｯﾄ1_調合量規格1
     * @param potto1_tyogouryoukikaku1 the potto1_tyogouryoukikaku1 to set
     */
    public void setPotto1_tyogouryoukikaku1(String potto1_tyogouryoukikaku1) {
        this.potto1_tyogouryoukikaku1 = potto1_tyogouryoukikaku1;
    }

    /**
     * ﾎﾟｯﾄ1_資材ﾛｯﾄNo.1_1
     * @return the potto1_sizailotno1_1
     */
    public String getPotto1_sizailotno1_1() {
        return potto1_sizailotno1_1;
    }

    /**
     * ﾎﾟｯﾄ1_資材ﾛｯﾄNo.1_1
     * @param potto1_sizailotno1_1 the potto1_sizailotno1_1 to set
     */
    public void setPotto1_sizailotno1_1(String potto1_sizailotno1_1) {
        this.potto1_sizailotno1_1 = potto1_sizailotno1_1;
    }

    /**
     * ﾎﾟｯﾄ1_調合量1_1
     * @return the potto1_tyougouryou1_1
     */
    public Integer getPotto1_tyougouryou1_1() {
        return potto1_tyougouryou1_1;
    }

    /**
     * ﾎﾟｯﾄ1_調合量1_1
     * @param potto1_tyougouryou1_1 the potto1_tyougouryou1_1 to set
     */
    public void setPotto1_tyougouryou1_1(Integer potto1_tyougouryou1_1) {
        this.potto1_tyougouryou1_1 = potto1_tyougouryou1_1;
    }

    /**
     * ﾎﾟｯﾄ1_資材ﾛｯﾄNo.1_2
     * @return the potto1_sizailotno1_2
     */
    public String getPotto1_sizailotno1_2() {
        return potto1_sizailotno1_2;
    }

    /**
     * ﾎﾟｯﾄ1_資材ﾛｯﾄNo.1_2
     * @param potto1_sizailotno1_2 the potto1_sizailotno1_2 to set
     */
    public void setPotto1_sizailotno1_2(String potto1_sizailotno1_2) {
        this.potto1_sizailotno1_2 = potto1_sizailotno1_2;
    }

    /**
     * ﾎﾟｯﾄ1_調合量1_2
     * @return the potto1_tyougouryou1_2
     */
    public Integer getPotto1_tyougouryou1_2() {
        return potto1_tyougouryou1_2;
    }

    /**
     * ﾎﾟｯﾄ1_調合量1_2
     * @param potto1_tyougouryou1_2 the potto1_tyougouryou1_2 to set
     */
    public void setPotto1_tyougouryou1_2(Integer potto1_tyougouryou1_2) {
        this.potto1_tyougouryou1_2 = potto1_tyougouryou1_2;
    }

    /**
     * ﾎﾟｯﾄ1_材料品名2
     * @return the potto1_zairyohinmei2
     */
    public String getPotto1_zairyohinmei2() {
        return potto1_zairyohinmei2;
    }

    /**
     * ﾎﾟｯﾄ1_材料品名2
     * @param potto1_zairyohinmei2 the potto1_zairyohinmei2 to set
     */
    public void setPotto1_zairyohinmei2(String potto1_zairyohinmei2) {
        this.potto1_zairyohinmei2 = potto1_zairyohinmei2;
    }

    /**
     * ﾎﾟｯﾄ1_調合量規格2
     * @return the potto1_tyogouryoukikaku2
     */
    public String getPotto1_tyogouryoukikaku2() {
        return potto1_tyogouryoukikaku2;
    }

    /**
     * ﾎﾟｯﾄ1_調合量規格2
     * @param potto1_tyogouryoukikaku2 the potto1_tyogouryoukikaku2 to set
     */
    public void setPotto1_tyogouryoukikaku2(String potto1_tyogouryoukikaku2) {
        this.potto1_tyogouryoukikaku2 = potto1_tyogouryoukikaku2;
    }

    /**
     * ﾎﾟｯﾄ1_資材ﾛｯﾄNo.2_1
     * @return the potto1_sizailotno2_1
     */
    public String getPotto1_sizailotno2_1() {
        return potto1_sizailotno2_1;
    }

    /**
     * ﾎﾟｯﾄ1_資材ﾛｯﾄNo.2_1
     * @param potto1_sizailotno2_1 the potto1_sizailotno2_1 to set
     */
    public void setPotto1_sizailotno2_1(String potto1_sizailotno2_1) {
        this.potto1_sizailotno2_1 = potto1_sizailotno2_1;
    }

    /**
     * ﾎﾟｯﾄ1_調合量2_1
     * @return the potto1_tyougouryou2_1
     */
    public Integer getPotto1_tyougouryou2_1() {
        return potto1_tyougouryou2_1;
    }

    /**
     * ﾎﾟｯﾄ1_調合量2_1
     * @param potto1_tyougouryou2_1 the potto1_tyougouryou2_1 to set
     */
    public void setPotto1_tyougouryou2_1(Integer potto1_tyougouryou2_1) {
        this.potto1_tyougouryou2_1 = potto1_tyougouryou2_1;
    }

    /**
     * ﾎﾟｯﾄ1_資材ﾛｯﾄNo.2_2
     * @return the potto1_sizailotno2_2
     */
    public String getPotto1_sizailotno2_2() {
        return potto1_sizailotno2_2;
    }

    /**
     * ﾎﾟｯﾄ1_資材ﾛｯﾄNo.2_2
     * @param potto1_sizailotno2_2 the potto1_sizailotno2_2 to set
     */
    public void setPotto1_sizailotno2_2(String potto1_sizailotno2_2) {
        this.potto1_sizailotno2_2 = potto1_sizailotno2_2;
    }

    /**
     * ﾎﾟｯﾄ1_調合量2_2
     * @return the potto1_tyougouryou2_2
     */
    public Integer getPotto1_tyougouryou2_2() {
        return potto1_tyougouryou2_2;
    }

    /**
     * ﾎﾟｯﾄ1_調合量2_2
     * @param potto1_tyougouryou2_2 the potto1_tyougouryou2_2 to set
     */
    public void setPotto1_tyougouryou2_2(Integer potto1_tyougouryou2_2) {
        this.potto1_tyougouryou2_2 = potto1_tyougouryou2_2;
    }

    /**
     * ﾎﾟｯﾄ1_材料品名3
     * @return the potto1_zairyohinmei3
     */
    public String getPotto1_zairyohinmei3() {
        return potto1_zairyohinmei3;
    }

    /**
     * ﾎﾟｯﾄ1_材料品名3
     * @param potto1_zairyohinmei3 the potto1_zairyohinmei3 to set
     */
    public void setPotto1_zairyohinmei3(String potto1_zairyohinmei3) {
        this.potto1_zairyohinmei3 = potto1_zairyohinmei3;
    }

    /**
     * ﾎﾟｯﾄ1_調合量規格3
     * @return the potto1_tyogouryoukikaku3
     */
    public String getPotto1_tyogouryoukikaku3() {
        return potto1_tyogouryoukikaku3;
    }

    /**
     * ﾎﾟｯﾄ1_調合量規格3
     * @param potto1_tyogouryoukikaku3 the potto1_tyogouryoukikaku3 to set
     */
    public void setPotto1_tyogouryoukikaku3(String potto1_tyogouryoukikaku3) {
        this.potto1_tyogouryoukikaku3 = potto1_tyogouryoukikaku3;
    }

    /**
     * ﾎﾟｯﾄ1_資材ﾛｯﾄNo.3_1
     * @return the potto1_sizailotno3_1
     */
    public String getPotto1_sizailotno3_1() {
        return potto1_sizailotno3_1;
    }

    /**
     * ﾎﾟｯﾄ1_資材ﾛｯﾄNo.3_1
     * @param potto1_sizailotno3_1 the potto1_sizailotno3_1 to set
     */
    public void setPotto1_sizailotno3_1(String potto1_sizailotno3_1) {
        this.potto1_sizailotno3_1 = potto1_sizailotno3_1;
    }

    /**
     * ﾎﾟｯﾄ1_調合量3_1
     * @return the potto1_tyougouryou3_1
     */
    public Integer getPotto1_tyougouryou3_1() {
        return potto1_tyougouryou3_1;
    }

    /**
     * ﾎﾟｯﾄ1_調合量3_1
     * @param potto1_tyougouryou3_1 the potto1_tyougouryou3_1 to set
     */
    public void setPotto1_tyougouryou3_1(Integer potto1_tyougouryou3_1) {
        this.potto1_tyougouryou3_1 = potto1_tyougouryou3_1;
    }

    /**
     * ﾎﾟｯﾄ1_資材ﾛｯﾄNo.3_2
     * @return the potto1_sizailotno3_2
     */
    public String getPotto1_sizailotno3_2() {
        return potto1_sizailotno3_2;
    }

    /**
     * ﾎﾟｯﾄ1_資材ﾛｯﾄNo.3_2
     * @param potto1_sizailotno3_2 the potto1_sizailotno3_2 to set
     */
    public void setPotto1_sizailotno3_2(String potto1_sizailotno3_2) {
        this.potto1_sizailotno3_2 = potto1_sizailotno3_2;
    }

    /**
     * ﾎﾟｯﾄ1_調合量3_2
     * @return the potto1_tyougouryou3_2
     */
    public Integer getPotto1_tyougouryou3_2() {
        return potto1_tyougouryou3_2;
    }

    /**
     * ﾎﾟｯﾄ1_調合量3_2
     * @param potto1_tyougouryou3_2 the potto1_tyougouryou3_2 to set
     */
    public void setPotto1_tyougouryou3_2(Integer potto1_tyougouryou3_2) {
        this.potto1_tyougouryou3_2 = potto1_tyougouryou3_2;
    }

    /**
     * ﾎﾟｯﾄ1_材料品名4
     * @return the potto1_zairyohinmei4
     */
    public String getPotto1_zairyohinmei4() {
        return potto1_zairyohinmei4;
    }

    /**
     * ﾎﾟｯﾄ1_材料品名4
     * @param potto1_zairyohinmei4 the potto1_zairyohinmei4 to set
     */
    public void setPotto1_zairyohinmei4(String potto1_zairyohinmei4) {
        this.potto1_zairyohinmei4 = potto1_zairyohinmei4;
    }

    /**
     * ﾎﾟｯﾄ1_調合量規格4
     * @return the potto1_tyogouryoukikaku4
     */
    public String getPotto1_tyogouryoukikaku4() {
        return potto1_tyogouryoukikaku4;
    }

    /**
     * ﾎﾟｯﾄ1_調合量規格4
     * @param potto1_tyogouryoukikaku4 the potto1_tyogouryoukikaku4 to set
     */
    public void setPotto1_tyogouryoukikaku4(String potto1_tyogouryoukikaku4) {
        this.potto1_tyogouryoukikaku4 = potto1_tyogouryoukikaku4;
    }

    /**
     * ﾎﾟｯﾄ1_資材ﾛｯﾄNo.4_1
     * @return the potto1_sizailotno4_1
     */
    public String getPotto1_sizailotno4_1() {
        return potto1_sizailotno4_1;
    }

    /**
     * ﾎﾟｯﾄ1_資材ﾛｯﾄNo.4_1
     * @param potto1_sizailotno4_1 the potto1_sizailotno4_1 to set
     */
    public void setPotto1_sizailotno4_1(String potto1_sizailotno4_1) {
        this.potto1_sizailotno4_1 = potto1_sizailotno4_1;
    }

    /**
     * ﾎﾟｯﾄ1_調合量4_1
     * @return the potto1_tyougouryou4_1
     */
    public Integer getPotto1_tyougouryou4_1() {
        return potto1_tyougouryou4_1;
    }

    /**
     * ﾎﾟｯﾄ1_調合量4_1
     * @param potto1_tyougouryou4_1 the potto1_tyougouryou4_1 to set
     */
    public void setPotto1_tyougouryou4_1(Integer potto1_tyougouryou4_1) {
        this.potto1_tyougouryou4_1 = potto1_tyougouryou4_1;
    }

    /**
     * ﾎﾟｯﾄ1_資材ﾛｯﾄNo.4_2
     * @return the potto1_sizailotno4_2
     */
    public String getPotto1_sizailotno4_2() {
        return potto1_sizailotno4_2;
    }

    /**
     * ﾎﾟｯﾄ1_資材ﾛｯﾄNo.4_2
     * @param potto1_sizailotno4_2 the potto1_sizailotno4_2 to set
     */
    public void setPotto1_sizailotno4_2(String potto1_sizailotno4_2) {
        this.potto1_sizailotno4_2 = potto1_sizailotno4_2;
    }

    /**
     * ﾎﾟｯﾄ1_調合量4_2
     * @return the potto1_tyougouryou4_2
     */
    public Integer getPotto1_tyougouryou4_2() {
        return potto1_tyougouryou4_2;
    }

    /**
     * ﾎﾟｯﾄ1_調合量4_2
     * @param potto1_tyougouryou4_2 the potto1_tyougouryou4_2 to set
     */
    public void setPotto1_tyougouryou4_2(Integer potto1_tyougouryou4_2) {
        this.potto1_tyougouryou4_2 = potto1_tyougouryou4_2;
    }

    /**
     * ﾎﾟｯﾄ2_材料品名1
     * @return the potto2_zairyohinmei1
     */
    public String getPotto2_zairyohinmei1() {
        return potto2_zairyohinmei1;
    }

    /**
     * ﾎﾟｯﾄ2_材料品名1
     * @param potto2_zairyohinmei1 the potto2_zairyohinmei1 to set
     */
    public void setPotto2_zairyohinmei1(String potto2_zairyohinmei1) {
        this.potto2_zairyohinmei1 = potto2_zairyohinmei1;
    }

    /**
     * ﾎﾟｯﾄ2_調合量規格1
     * @return the potto2_tyogouryoukikaku1
     */
    public String getPotto2_tyogouryoukikaku1() {
        return potto2_tyogouryoukikaku1;
    }

    /**
     * ﾎﾟｯﾄ2_調合量規格1
     * @param potto2_tyogouryoukikaku1 the potto2_tyogouryoukikaku1 to set
     */
    public void setPotto2_tyogouryoukikaku1(String potto2_tyogouryoukikaku1) {
        this.potto2_tyogouryoukikaku1 = potto2_tyogouryoukikaku1;
    }

    /**
     * ﾎﾟｯﾄ2_資材ﾛｯﾄNo.1_1
     * @return the potto2_sizailotno1_1
     */
    public String getPotto2_sizailotno1_1() {
        return potto2_sizailotno1_1;
    }

    /**
     * ﾎﾟｯﾄ2_資材ﾛｯﾄNo.1_1
     * @param potto2_sizailotno1_1 the potto2_sizailotno1_1 to set
     */
    public void setPotto2_sizailotno1_1(String potto2_sizailotno1_1) {
        this.potto2_sizailotno1_1 = potto2_sizailotno1_1;
    }

    /**
     * ﾎﾟｯﾄ2_調合量1_1
     * @return the potto2_tyougouryou1_1
     */
    public Integer getPotto2_tyougouryou1_1() {
        return potto2_tyougouryou1_1;
    }

    /**
     * ﾎﾟｯﾄ2_調合量1_1
     * @param potto2_tyougouryou1_1 the potto2_tyougouryou1_1 to set
     */
    public void setPotto2_tyougouryou1_1(Integer potto2_tyougouryou1_1) {
        this.potto2_tyougouryou1_1 = potto2_tyougouryou1_1;
    }

    /**
     * ﾎﾟｯﾄ2_資材ﾛｯﾄNo.1_2
     * @return the potto2_sizailotno1_2
     */
    public String getPotto2_sizailotno1_2() {
        return potto2_sizailotno1_2;
    }

    /**
     * ﾎﾟｯﾄ2_資材ﾛｯﾄNo.1_2
     * @param potto2_sizailotno1_2 the potto2_sizailotno1_2 to set
     */
    public void setPotto2_sizailotno1_2(String potto2_sizailotno1_2) {
        this.potto2_sizailotno1_2 = potto2_sizailotno1_2;
    }

    /**
     * ﾎﾟｯﾄ2_調合量1_2
     * @return the potto2_tyougouryou1_2
     */
    public Integer getPotto2_tyougouryou1_2() {
        return potto2_tyougouryou1_2;
    }

    /**
     * ﾎﾟｯﾄ2_調合量1_2
     * @param potto2_tyougouryou1_2 the potto2_tyougouryou1_2 to set
     */
    public void setPotto2_tyougouryou1_2(Integer potto2_tyougouryou1_2) {
        this.potto2_tyougouryou1_2 = potto2_tyougouryou1_2;
    }

    /**
     * ﾎﾟｯﾄ2_材料品名2
     * @return the potto2_zairyohinmei2
     */
    public String getPotto2_zairyohinmei2() {
        return potto2_zairyohinmei2;
    }

    /**
     * ﾎﾟｯﾄ2_材料品名2
     * @param potto2_zairyohinmei2 the potto2_zairyohinmei2 to set
     */
    public void setPotto2_zairyohinmei2(String potto2_zairyohinmei2) {
        this.potto2_zairyohinmei2 = potto2_zairyohinmei2;
    }

    /**
     * ﾎﾟｯﾄ2_調合量規格2
     * @return the potto2_tyogouryoukikaku2
     */
    public String getPotto2_tyogouryoukikaku2() {
        return potto2_tyogouryoukikaku2;
    }

    /**
     * ﾎﾟｯﾄ2_調合量規格2
     * @param potto2_tyogouryoukikaku2 the potto2_tyogouryoukikaku2 to set
     */
    public void setPotto2_tyogouryoukikaku2(String potto2_tyogouryoukikaku2) {
        this.potto2_tyogouryoukikaku2 = potto2_tyogouryoukikaku2;
    }

    /**
     * ﾎﾟｯﾄ2_資材ﾛｯﾄNo.2_1
     * @return the potto2_sizailotno2_1
     */
    public String getPotto2_sizailotno2_1() {
        return potto2_sizailotno2_1;
    }

    /**
     * ﾎﾟｯﾄ2_資材ﾛｯﾄNo.2_1
     * @param potto2_sizailotno2_1 the potto2_sizailotno2_1 to set
     */
    public void setPotto2_sizailotno2_1(String potto2_sizailotno2_1) {
        this.potto2_sizailotno2_1 = potto2_sizailotno2_1;
    }

    /**
     * ﾎﾟｯﾄ2_調合量2_1
     * @return the potto2_tyougouryou2_1
     */
    public Integer getPotto2_tyougouryou2_1() {
        return potto2_tyougouryou2_1;
    }

    /**
     * ﾎﾟｯﾄ2_調合量2_1
     * @param potto2_tyougouryou2_1 the potto2_tyougouryou2_1 to set
     */
    public void setPotto2_tyougouryou2_1(Integer potto2_tyougouryou2_1) {
        this.potto2_tyougouryou2_1 = potto2_tyougouryou2_1;
    }

    /**
     * ﾎﾟｯﾄ2_資材ﾛｯﾄNo.2_2
     * @return the potto2_sizailotno2_2
     */
    public String getPotto2_sizailotno2_2() {
        return potto2_sizailotno2_2;
    }

    /**
     * ﾎﾟｯﾄ2_資材ﾛｯﾄNo.2_2
     * @param potto2_sizailotno2_2 the potto2_sizailotno2_2 to set
     */
    public void setPotto2_sizailotno2_2(String potto2_sizailotno2_2) {
        this.potto2_sizailotno2_2 = potto2_sizailotno2_2;
    }

    /**
     * ﾎﾟｯﾄ2_調合量2_2
     * @return the potto2_tyougouryou2_2
     */
    public Integer getPotto2_tyougouryou2_2() {
        return potto2_tyougouryou2_2;
    }

    /**
     * ﾎﾟｯﾄ2_調合量2_2
     * @param potto2_tyougouryou2_2 the potto2_tyougouryou2_2 to set
     */
    public void setPotto2_tyougouryou2_2(Integer potto2_tyougouryou2_2) {
        this.potto2_tyougouryou2_2 = potto2_tyougouryou2_2;
    }

    /**
     * ﾎﾟｯﾄ2_材料品名3
     * @return the potto2_zairyohinmei3
     */
    public String getPotto2_zairyohinmei3() {
        return potto2_zairyohinmei3;
    }

    /**
     * ﾎﾟｯﾄ2_材料品名3
     * @param potto2_zairyohinmei3 the potto2_zairyohinmei3 to set
     */
    public void setPotto2_zairyohinmei3(String potto2_zairyohinmei3) {
        this.potto2_zairyohinmei3 = potto2_zairyohinmei3;
    }

    /**
     * ﾎﾟｯﾄ2_調合量規格3
     * @return the potto2_tyogouryoukikaku3
     */
    public String getPotto2_tyogouryoukikaku3() {
        return potto2_tyogouryoukikaku3;
    }

    /**
     * ﾎﾟｯﾄ2_調合量規格3
     * @param potto2_tyogouryoukikaku3 the potto2_tyogouryoukikaku3 to set
     */
    public void setPotto2_tyogouryoukikaku3(String potto2_tyogouryoukikaku3) {
        this.potto2_tyogouryoukikaku3 = potto2_tyogouryoukikaku3;
    }

    /**
     * ﾎﾟｯﾄ2_資材ﾛｯﾄNo.3_1
     * @return the potto2_sizailotno3_1
     */
    public String getPotto2_sizailotno3_1() {
        return potto2_sizailotno3_1;
    }

    /**
     * ﾎﾟｯﾄ2_資材ﾛｯﾄNo.3_1
     * @param potto2_sizailotno3_1 the potto2_sizailotno3_1 to set
     */
    public void setPotto2_sizailotno3_1(String potto2_sizailotno3_1) {
        this.potto2_sizailotno3_1 = potto2_sizailotno3_1;
    }

    /**
     * ﾎﾟｯﾄ2_調合量3_1
     * @return the potto2_tyougouryou3_1
     */
    public Integer getPotto2_tyougouryou3_1() {
        return potto2_tyougouryou3_1;
    }

    /**
     * ﾎﾟｯﾄ2_調合量3_1
     * @param potto2_tyougouryou3_1 the potto2_tyougouryou3_1 to set
     */
    public void setPotto2_tyougouryou3_1(Integer potto2_tyougouryou3_1) {
        this.potto2_tyougouryou3_1 = potto2_tyougouryou3_1;
    }

    /**
     * ﾎﾟｯﾄ2_資材ﾛｯﾄNo.3_2
     * @return the potto2_sizailotno3_2
     */
    public String getPotto2_sizailotno3_2() {
        return potto2_sizailotno3_2;
    }

    /**
     * ﾎﾟｯﾄ2_資材ﾛｯﾄNo.3_2
     * @param potto2_sizailotno3_2 the potto2_sizailotno3_2 to set
     */
    public void setPotto2_sizailotno3_2(String potto2_sizailotno3_2) {
        this.potto2_sizailotno3_2 = potto2_sizailotno3_2;
    }

    /**
     * ﾎﾟｯﾄ2_調合量3_2
     * @return the potto2_tyougouryou3_2
     */
    public Integer getPotto2_tyougouryou3_2() {
        return potto2_tyougouryou3_2;
    }

    /**
     * ﾎﾟｯﾄ2_調合量3_2
     * @param potto2_tyougouryou3_2 the potto2_tyougouryou3_2 to set
     */
    public void setPotto2_tyougouryou3_2(Integer potto2_tyougouryou3_2) {
        this.potto2_tyougouryou3_2 = potto2_tyougouryou3_2;
    }

    /**
     * ﾎﾟｯﾄ2_材料品名4
     * @return the potto2_zairyohinmei4
     */
    public String getPotto2_zairyohinmei4() {
        return potto2_zairyohinmei4;
    }

    /**
     * ﾎﾟｯﾄ2_材料品名4
     * @param potto2_zairyohinmei4 the potto2_zairyohinmei4 to set
     */
    public void setPotto2_zairyohinmei4(String potto2_zairyohinmei4) {
        this.potto2_zairyohinmei4 = potto2_zairyohinmei4;
    }

    /**
     * ﾎﾟｯﾄ2_調合量規格4
     * @return the potto2_tyogouryoukikaku4
     */
    public String getPotto2_tyogouryoukikaku4() {
        return potto2_tyogouryoukikaku4;
    }

    /**
     * ﾎﾟｯﾄ2_調合量規格4
     * @param potto2_tyogouryoukikaku4 the potto2_tyogouryoukikaku4 to set
     */
    public void setPotto2_tyogouryoukikaku4(String potto2_tyogouryoukikaku4) {
        this.potto2_tyogouryoukikaku4 = potto2_tyogouryoukikaku4;
    }

    /**
     * ﾎﾟｯﾄ2_資材ﾛｯﾄNo.4_1
     * @return the potto2_sizailotno4_1
     */
    public String getPotto2_sizailotno4_1() {
        return potto2_sizailotno4_1;
    }

    /**
     * ﾎﾟｯﾄ2_資材ﾛｯﾄNo.4_1
     * @param potto2_sizailotno4_1 the potto2_sizailotno4_1 to set
     */
    public void setPotto2_sizailotno4_1(String potto2_sizailotno4_1) {
        this.potto2_sizailotno4_1 = potto2_sizailotno4_1;
    }

    /**
     * ﾎﾟｯﾄ2_調合量4_1
     * @return the potto2_tyougouryou4_1
     */
    public Integer getPotto2_tyougouryou4_1() {
        return potto2_tyougouryou4_1;
    }

    /**
     * ﾎﾟｯﾄ2_調合量4_1
     * @param potto2_tyougouryou4_1 the potto2_tyougouryou4_1 to set
     */
    public void setPotto2_tyougouryou4_1(Integer potto2_tyougouryou4_1) {
        this.potto2_tyougouryou4_1 = potto2_tyougouryou4_1;
    }

    /**
     * ﾎﾟｯﾄ2_資材ﾛｯﾄNo.4_2
     * @return the potto2_sizailotno4_2
     */
    public String getPotto2_sizailotno4_2() {
        return potto2_sizailotno4_2;
    }

    /**
     * ﾎﾟｯﾄ2_資材ﾛｯﾄNo.4_2
     * @param potto2_sizailotno4_2 the potto2_sizailotno4_2 to set
     */
    public void setPotto2_sizailotno4_2(String potto2_sizailotno4_2) {
        this.potto2_sizailotno4_2 = potto2_sizailotno4_2;
    }

    /**
     * ﾎﾟｯﾄ2_調合量4_2
     * @return the potto2_tyougouryou4_2
     */
    public Integer getPotto2_tyougouryou4_2() {
        return potto2_tyougouryou4_2;
    }

    /**
     * ﾎﾟｯﾄ2_調合量4_2
     * @param potto2_tyougouryou4_2 the potto2_tyougouryou4_2 to set
     */
    public void setPotto2_tyougouryou4_2(Integer potto2_tyougouryou4_2) {
        this.potto2_tyougouryou4_2 = potto2_tyougouryou4_2;
    }

    /**
     * ﾎﾟｯﾄ3_材料品名1
     * @return the potto3_zairyohinmei1
     */
    public String getPotto3_zairyohinmei1() {
        return potto3_zairyohinmei1;
    }

    /**
     * ﾎﾟｯﾄ3_材料品名1
     * @param potto3_zairyohinmei1 the potto3_zairyohinmei1 to set
     */
    public void setPotto3_zairyohinmei1(String potto3_zairyohinmei1) {
        this.potto3_zairyohinmei1 = potto3_zairyohinmei1;
    }

    /**
     * ﾎﾟｯﾄ3_調合量規格1
     * @return the potto3_tyogouryoukikaku1
     */
    public String getPotto3_tyogouryoukikaku1() {
        return potto3_tyogouryoukikaku1;
    }

    /**
     * ﾎﾟｯﾄ3_調合量規格1
     * @param potto3_tyogouryoukikaku1 the potto3_tyogouryoukikaku1 to set
     */
    public void setPotto3_tyogouryoukikaku1(String potto3_tyogouryoukikaku1) {
        this.potto3_tyogouryoukikaku1 = potto3_tyogouryoukikaku1;
    }

    /**
     * ﾎﾟｯﾄ3_資材ﾛｯﾄNo.1_1
     * @return the potto3_sizailotno1_1
     */
    public String getPotto3_sizailotno1_1() {
        return potto3_sizailotno1_1;
    }

    /**
     * ﾎﾟｯﾄ3_資材ﾛｯﾄNo.1_1
     * @param potto3_sizailotno1_1 the potto3_sizailotno1_1 to set
     */
    public void setPotto3_sizailotno1_1(String potto3_sizailotno1_1) {
        this.potto3_sizailotno1_1 = potto3_sizailotno1_1;
    }

    /**
     * ﾎﾟｯﾄ3_調合量1_1
     * @return the potto3_tyougouryou1_1
     */
    public Integer getPotto3_tyougouryou1_1() {
        return potto3_tyougouryou1_1;
    }

    /**
     * ﾎﾟｯﾄ3_調合量1_1
     * @param potto3_tyougouryou1_1 the potto3_tyougouryou1_1 to set
     */
    public void setPotto3_tyougouryou1_1(Integer potto3_tyougouryou1_1) {
        this.potto3_tyougouryou1_1 = potto3_tyougouryou1_1;
    }

    /**
     * ﾎﾟｯﾄ3_資材ﾛｯﾄNo.1_2
     * @return the potto3_sizailotno1_2
     */
    public String getPotto3_sizailotno1_2() {
        return potto3_sizailotno1_2;
    }

    /**
     * ﾎﾟｯﾄ3_資材ﾛｯﾄNo.1_2
     * @param potto3_sizailotno1_2 the potto3_sizailotno1_2 to set
     */
    public void setPotto3_sizailotno1_2(String potto3_sizailotno1_2) {
        this.potto3_sizailotno1_2 = potto3_sizailotno1_2;
    }

    /**
     * ﾎﾟｯﾄ3_調合量1_2
     * @return the potto3_tyougouryou1_2
     */
    public Integer getPotto3_tyougouryou1_2() {
        return potto3_tyougouryou1_2;
    }

    /**
     * ﾎﾟｯﾄ3_調合量1_2
     * @param potto3_tyougouryou1_2 the potto3_tyougouryou1_2 to set
     */
    public void setPotto3_tyougouryou1_2(Integer potto3_tyougouryou1_2) {
        this.potto3_tyougouryou1_2 = potto3_tyougouryou1_2;
    }

    /**
     * ﾎﾟｯﾄ3_材料品名2
     * @return the potto3_zairyohinmei2
     */
    public String getPotto3_zairyohinmei2() {
        return potto3_zairyohinmei2;
    }

    /**
     * ﾎﾟｯﾄ3_材料品名2
     * @param potto3_zairyohinmei2 the potto3_zairyohinmei2 to set
     */
    public void setPotto3_zairyohinmei2(String potto3_zairyohinmei2) {
        this.potto3_zairyohinmei2 = potto3_zairyohinmei2;
    }

    /**
     * ﾎﾟｯﾄ3_調合量規格2
     * @return the potto3_tyogouryoukikaku2
     */
    public String getPotto3_tyogouryoukikaku2() {
        return potto3_tyogouryoukikaku2;
    }

    /**
     * ﾎﾟｯﾄ3_調合量規格2
     * @param potto3_tyogouryoukikaku2 the potto3_tyogouryoukikaku2 to set
     */
    public void setPotto3_tyogouryoukikaku2(String potto3_tyogouryoukikaku2) {
        this.potto3_tyogouryoukikaku2 = potto3_tyogouryoukikaku2;
    }

    /**
     * ﾎﾟｯﾄ3_資材ﾛｯﾄNo.2_1
     * @return the potto3_sizailotno2_1
     */
    public String getPotto3_sizailotno2_1() {
        return potto3_sizailotno2_1;
    }

    /**
     * ﾎﾟｯﾄ3_資材ﾛｯﾄNo.2_1
     * @param potto3_sizailotno2_1 the potto3_sizailotno2_1 to set
     */
    public void setPotto3_sizailotno2_1(String potto3_sizailotno2_1) {
        this.potto3_sizailotno2_1 = potto3_sizailotno2_1;
    }

    /**
     * ﾎﾟｯﾄ3_調合量2_1
     * @return the potto3_tyougouryou2_1
     */
    public Integer getPotto3_tyougouryou2_1() {
        return potto3_tyougouryou2_1;
    }

    /**
     * ﾎﾟｯﾄ3_調合量2_1
     * @param potto3_tyougouryou2_1 the potto3_tyougouryou2_1 to set
     */
    public void setPotto3_tyougouryou2_1(Integer potto3_tyougouryou2_1) {
        this.potto3_tyougouryou2_1 = potto3_tyougouryou2_1;
    }

    /**
     * ﾎﾟｯﾄ3_資材ﾛｯﾄNo.2_2
     * @return the potto3_sizailotno2_2
     */
    public String getPotto3_sizailotno2_2() {
        return potto3_sizailotno2_2;
    }

    /**
     * ﾎﾟｯﾄ3_資材ﾛｯﾄNo.2_2
     * @param potto3_sizailotno2_2 the potto3_sizailotno2_2 to set
     */
    public void setPotto3_sizailotno2_2(String potto3_sizailotno2_2) {
        this.potto3_sizailotno2_2 = potto3_sizailotno2_2;
    }

    /**
     * ﾎﾟｯﾄ3_調合量2_2
     * @return the potto3_tyougouryou2_2
     */
    public Integer getPotto3_tyougouryou2_2() {
        return potto3_tyougouryou2_2;
    }

    /**
     * ﾎﾟｯﾄ3_調合量2_2
     * @param potto3_tyougouryou2_2 the potto3_tyougouryou2_2 to set
     */
    public void setPotto3_tyougouryou2_2(Integer potto3_tyougouryou2_2) {
        this.potto3_tyougouryou2_2 = potto3_tyougouryou2_2;
    }

    /**
     * ﾎﾟｯﾄ3_材料品名3
     * @return the potto3_zairyohinmei3
     */
    public String getPotto3_zairyohinmei3() {
        return potto3_zairyohinmei3;
    }

    /**
     * ﾎﾟｯﾄ3_材料品名3
     * @param potto3_zairyohinmei3 the potto3_zairyohinmei3 to set
     */
    public void setPotto3_zairyohinmei3(String potto3_zairyohinmei3) {
        this.potto3_zairyohinmei3 = potto3_zairyohinmei3;
    }

    /**
     * ﾎﾟｯﾄ3_調合量規格3
     * @return the potto3_tyogouryoukikaku3
     */
    public String getPotto3_tyogouryoukikaku3() {
        return potto3_tyogouryoukikaku3;
    }

    /**
     * ﾎﾟｯﾄ3_調合量規格3
     * @param potto3_tyogouryoukikaku3 the potto3_tyogouryoukikaku3 to set
     */
    public void setPotto3_tyogouryoukikaku3(String potto3_tyogouryoukikaku3) {
        this.potto3_tyogouryoukikaku3 = potto3_tyogouryoukikaku3;
    }

    /**
     * ﾎﾟｯﾄ3_資材ﾛｯﾄNo.3_1
     * @return the potto3_sizailotno3_1
     */
    public String getPotto3_sizailotno3_1() {
        return potto3_sizailotno3_1;
    }

    /**
     * ﾎﾟｯﾄ3_資材ﾛｯﾄNo.3_1
     * @param potto3_sizailotno3_1 the potto3_sizailotno3_1 to set
     */
    public void setPotto3_sizailotno3_1(String potto3_sizailotno3_1) {
        this.potto3_sizailotno3_1 = potto3_sizailotno3_1;
    }

    /**
     * ﾎﾟｯﾄ3_調合量3_1
     * @return the potto3_tyougouryou3_1
     */
    public Integer getPotto3_tyougouryou3_1() {
        return potto3_tyougouryou3_1;
    }

    /**
     * ﾎﾟｯﾄ3_調合量3_1
     * @param potto3_tyougouryou3_1 the potto3_tyougouryou3_1 to set
     */
    public void setPotto3_tyougouryou3_1(Integer potto3_tyougouryou3_1) {
        this.potto3_tyougouryou3_1 = potto3_tyougouryou3_1;
    }

    /**
     * ﾎﾟｯﾄ3_資材ﾛｯﾄNo.3_2
     * @return the potto3_sizailotno3_2
     */
    public String getPotto3_sizailotno3_2() {
        return potto3_sizailotno3_2;
    }

    /**
     * ﾎﾟｯﾄ3_資材ﾛｯﾄNo.3_2
     * @param potto3_sizailotno3_2 the potto3_sizailotno3_2 to set
     */
    public void setPotto3_sizailotno3_2(String potto3_sizailotno3_2) {
        this.potto3_sizailotno3_2 = potto3_sizailotno3_2;
    }

    /**
     * ﾎﾟｯﾄ3_調合量3_2
     * @return the potto3_tyougouryou3_2
     */
    public Integer getPotto3_tyougouryou3_2() {
        return potto3_tyougouryou3_2;
    }

    /**
     * ﾎﾟｯﾄ3_調合量3_2
     * @param potto3_tyougouryou3_2 the potto3_tyougouryou3_2 to set
     */
    public void setPotto3_tyougouryou3_2(Integer potto3_tyougouryou3_2) {
        this.potto3_tyougouryou3_2 = potto3_tyougouryou3_2;
    }

    /**
     * ﾎﾟｯﾄ3_材料品名4
     * @return the potto3_zairyohinmei4
     */
    public String getPotto3_zairyohinmei4() {
        return potto3_zairyohinmei4;
    }

    /**
     * ﾎﾟｯﾄ3_材料品名4
     * @param potto3_zairyohinmei4 the potto3_zairyohinmei4 to set
     */
    public void setPotto3_zairyohinmei4(String potto3_zairyohinmei4) {
        this.potto3_zairyohinmei4 = potto3_zairyohinmei4;
    }

    /**
     * ﾎﾟｯﾄ3_調合量規格4
     * @return the potto3_tyogouryoukikaku4
     */
    public String getPotto3_tyogouryoukikaku4() {
        return potto3_tyogouryoukikaku4;
    }

    /**
     * ﾎﾟｯﾄ3_調合量規格4
     * @param potto3_tyogouryoukikaku4 the potto3_tyogouryoukikaku4 to set
     */
    public void setPotto3_tyogouryoukikaku4(String potto3_tyogouryoukikaku4) {
        this.potto3_tyogouryoukikaku4 = potto3_tyogouryoukikaku4;
    }

    /**
     * ﾎﾟｯﾄ3_資材ﾛｯﾄNo.4_1
     * @return the potto3_sizailotno4_1
     */
    public String getPotto3_sizailotno4_1() {
        return potto3_sizailotno4_1;
    }

    /**
     * ﾎﾟｯﾄ3_資材ﾛｯﾄNo.4_1
     * @param potto3_sizailotno4_1 the potto3_sizailotno4_1 to set
     */
    public void setPotto3_sizailotno4_1(String potto3_sizailotno4_1) {
        this.potto3_sizailotno4_1 = potto3_sizailotno4_1;
    }

    /**
     * ﾎﾟｯﾄ3_調合量4_1
     * @return the potto3_tyougouryou4_1
     */
    public Integer getPotto3_tyougouryou4_1() {
        return potto3_tyougouryou4_1;
    }

    /**
     * ﾎﾟｯﾄ3_調合量4_1
     * @param potto3_tyougouryou4_1 the potto3_tyougouryou4_1 to set
     */
    public void setPotto3_tyougouryou4_1(Integer potto3_tyougouryou4_1) {
        this.potto3_tyougouryou4_1 = potto3_tyougouryou4_1;
    }

    /**
     * ﾎﾟｯﾄ3_資材ﾛｯﾄNo.4_2
     * @return the potto3_sizailotno4_2
     */
    public String getPotto3_sizailotno4_2() {
        return potto3_sizailotno4_2;
    }

    /**
     * ﾎﾟｯﾄ3_資材ﾛｯﾄNo.4_2
     * @param potto3_sizailotno4_2 the potto3_sizailotno4_2 to set
     */
    public void setPotto3_sizailotno4_2(String potto3_sizailotno4_2) {
        this.potto3_sizailotno4_2 = potto3_sizailotno4_2;
    }

    /**
     * ﾎﾟｯﾄ3_調合量4_2
     * @return the potto3_tyougouryou4_2
     */
    public Integer getPotto3_tyougouryou4_2() {
        return potto3_tyougouryou4_2;
    }

    /**
     * ﾎﾟｯﾄ3_調合量4_2
     * @param potto3_tyougouryou4_2 the potto3_tyougouryou4_2 to set
     */
    public void setPotto3_tyougouryou4_2(Integer potto3_tyougouryou4_2) {
        this.potto3_tyougouryou4_2 = potto3_tyougouryou4_2;
    }

    /**
     * ﾎﾟｯﾄ4_材料品名1
     * @return the potto4_zairyohinmei1
     */
    public String getPotto4_zairyohinmei1() {
        return potto4_zairyohinmei1;
    }

    /**
     * ﾎﾟｯﾄ4_材料品名1
     * @param potto4_zairyohinmei1 the potto4_zairyohinmei1 to set
     */
    public void setPotto4_zairyohinmei1(String potto4_zairyohinmei1) {
        this.potto4_zairyohinmei1 = potto4_zairyohinmei1;
    }

    /**
     * ﾎﾟｯﾄ4_調合量規格1
     * @return the potto4_tyogouryoukikaku1
     */
    public String getPotto4_tyogouryoukikaku1() {
        return potto4_tyogouryoukikaku1;
    }

    /**
     * ﾎﾟｯﾄ4_調合量規格1
     * @param potto4_tyogouryoukikaku1 the potto4_tyogouryoukikaku1 to set
     */
    public void setPotto4_tyogouryoukikaku1(String potto4_tyogouryoukikaku1) {
        this.potto4_tyogouryoukikaku1 = potto4_tyogouryoukikaku1;
    }

    /**
     * ﾎﾟｯﾄ4_資材ﾛｯﾄNo.1_1
     * @return the potto4_sizailotno1_1
     */
    public String getPotto4_sizailotno1_1() {
        return potto4_sizailotno1_1;
    }

    /**
     * ﾎﾟｯﾄ4_資材ﾛｯﾄNo.1_1
     * @param potto4_sizailotno1_1 the potto4_sizailotno1_1 to set
     */
    public void setPotto4_sizailotno1_1(String potto4_sizailotno1_1) {
        this.potto4_sizailotno1_1 = potto4_sizailotno1_1;
    }

    /**
     * ﾎﾟｯﾄ4_調合量1_1
     * @return the potto4_tyougouryou1_1
     */
    public Integer getPotto4_tyougouryou1_1() {
        return potto4_tyougouryou1_1;
    }

    /**
     * ﾎﾟｯﾄ4_調合量1_1
     * @param potto4_tyougouryou1_1 the potto4_tyougouryou1_1 to set
     */
    public void setPotto4_tyougouryou1_1(Integer potto4_tyougouryou1_1) {
        this.potto4_tyougouryou1_1 = potto4_tyougouryou1_1;
    }

    /**
     * ﾎﾟｯﾄ4_資材ﾛｯﾄNo.1_2
     * @return the potto4_sizailotno1_2
     */
    public String getPotto4_sizailotno1_2() {
        return potto4_sizailotno1_2;
    }

    /**
     * ﾎﾟｯﾄ4_資材ﾛｯﾄNo.1_2
     * @param potto4_sizailotno1_2 the potto4_sizailotno1_2 to set
     */
    public void setPotto4_sizailotno1_2(String potto4_sizailotno1_2) {
        this.potto4_sizailotno1_2 = potto4_sizailotno1_2;
    }

    /**
     * ﾎﾟｯﾄ4_調合量1_2
     * @return the potto4_tyougouryou1_2
     */
    public Integer getPotto4_tyougouryou1_2() {
        return potto4_tyougouryou1_2;
    }

    /**
     * ﾎﾟｯﾄ4_調合量1_2
     * @param potto4_tyougouryou1_2 the potto4_tyougouryou1_2 to set
     */
    public void setPotto4_tyougouryou1_2(Integer potto4_tyougouryou1_2) {
        this.potto4_tyougouryou1_2 = potto4_tyougouryou1_2;
    }

    /**
     * ﾎﾟｯﾄ4_材料品名2
     * @return the potto4_zairyohinmei2
     */
    public String getPotto4_zairyohinmei2() {
        return potto4_zairyohinmei2;
    }

    /**
     * ﾎﾟｯﾄ4_材料品名2
     * @param potto4_zairyohinmei2 the potto4_zairyohinmei2 to set
     */
    public void setPotto4_zairyohinmei2(String potto4_zairyohinmei2) {
        this.potto4_zairyohinmei2 = potto4_zairyohinmei2;
    }

    /**
     * ﾎﾟｯﾄ4_調合量規格2
     * @return the potto4_tyogouryoukikaku2
     */
    public String getPotto4_tyogouryoukikaku2() {
        return potto4_tyogouryoukikaku2;
    }

    /**
     * ﾎﾟｯﾄ4_調合量規格2
     * @param potto4_tyogouryoukikaku2 the potto4_tyogouryoukikaku2 to set
     */
    public void setPotto4_tyogouryoukikaku2(String potto4_tyogouryoukikaku2) {
        this.potto4_tyogouryoukikaku2 = potto4_tyogouryoukikaku2;
    }

    /**
     * ﾎﾟｯﾄ4_資材ﾛｯﾄNo.2_1
     * @return the potto4_sizailotno2_1
     */
    public String getPotto4_sizailotno2_1() {
        return potto4_sizailotno2_1;
    }

    /**
     * ﾎﾟｯﾄ4_資材ﾛｯﾄNo.2_1
     * @param potto4_sizailotno2_1 the potto4_sizailotno2_1 to set
     */
    public void setPotto4_sizailotno2_1(String potto4_sizailotno2_1) {
        this.potto4_sizailotno2_1 = potto4_sizailotno2_1;
    }

    /**
     * ﾎﾟｯﾄ4_調合量2_1
     * @return the potto4_tyougouryou2_1
     */
    public Integer getPotto4_tyougouryou2_1() {
        return potto4_tyougouryou2_1;
    }

    /**
     * ﾎﾟｯﾄ4_調合量2_1
     * @param potto4_tyougouryou2_1 the potto4_tyougouryou2_1 to set
     */
    public void setPotto4_tyougouryou2_1(Integer potto4_tyougouryou2_1) {
        this.potto4_tyougouryou2_1 = potto4_tyougouryou2_1;
    }

    /**
     * ﾎﾟｯﾄ4_資材ﾛｯﾄNo.2_2
     * @return the potto4_sizailotno2_2
     */
    public String getPotto4_sizailotno2_2() {
        return potto4_sizailotno2_2;
    }

    /**
     * ﾎﾟｯﾄ4_資材ﾛｯﾄNo.2_2
     * @param potto4_sizailotno2_2 the potto4_sizailotno2_2 to set
     */
    public void setPotto4_sizailotno2_2(String potto4_sizailotno2_2) {
        this.potto4_sizailotno2_2 = potto4_sizailotno2_2;
    }

    /**
     * ﾎﾟｯﾄ4_調合量2_2
     * @return the potto4_tyougouryou2_2
     */
    public Integer getPotto4_tyougouryou2_2() {
        return potto4_tyougouryou2_2;
    }

    /**
     * ﾎﾟｯﾄ4_調合量2_2
     * @param potto4_tyougouryou2_2 the potto4_tyougouryou2_2 to set
     */
    public void setPotto4_tyougouryou2_2(Integer potto4_tyougouryou2_2) {
        this.potto4_tyougouryou2_2 = potto4_tyougouryou2_2;
    }

    /**
     * ﾎﾟｯﾄ4_材料品名3
     * @return the potto4_zairyohinmei3
     */
    public String getPotto4_zairyohinmei3() {
        return potto4_zairyohinmei3;
    }

    /**
     * ﾎﾟｯﾄ4_材料品名3
     * @param potto4_zairyohinmei3 the potto4_zairyohinmei3 to set
     */
    public void setPotto4_zairyohinmei3(String potto4_zairyohinmei3) {
        this.potto4_zairyohinmei3 = potto4_zairyohinmei3;
    }

    /**
     * ﾎﾟｯﾄ4_調合量規格3
     * @return the potto4_tyogouryoukikaku3
     */
    public String getPotto4_tyogouryoukikaku3() {
        return potto4_tyogouryoukikaku3;
    }

    /**
     * ﾎﾟｯﾄ4_調合量規格3
     * @param potto4_tyogouryoukikaku3 the potto4_tyogouryoukikaku3 to set
     */
    public void setPotto4_tyogouryoukikaku3(String potto4_tyogouryoukikaku3) {
        this.potto4_tyogouryoukikaku3 = potto4_tyogouryoukikaku3;
    }

    /**
     * ﾎﾟｯﾄ4_資材ﾛｯﾄNo.3_1
     * @return the potto4_sizailotno3_1
     */
    public String getPotto4_sizailotno3_1() {
        return potto4_sizailotno3_1;
    }

    /**
     * ﾎﾟｯﾄ4_資材ﾛｯﾄNo.3_1
     * @param potto4_sizailotno3_1 the potto4_sizailotno3_1 to set
     */
    public void setPotto4_sizailotno3_1(String potto4_sizailotno3_1) {
        this.potto4_sizailotno3_1 = potto4_sizailotno3_1;
    }

    /**
     * ﾎﾟｯﾄ4_調合量3_1
     * @return the potto4_tyougouryou3_1
     */
    public Integer getPotto4_tyougouryou3_1() {
        return potto4_tyougouryou3_1;
    }

    /**
     * ﾎﾟｯﾄ4_調合量3_1
     * @param potto4_tyougouryou3_1 the potto4_tyougouryou3_1 to set
     */
    public void setPotto4_tyougouryou3_1(Integer potto4_tyougouryou3_1) {
        this.potto4_tyougouryou3_1 = potto4_tyougouryou3_1;
    }

    /**
     * ﾎﾟｯﾄ4_資材ﾛｯﾄNo.3_2
     * @return the potto4_sizailotno3_2
     */
    public String getPotto4_sizailotno3_2() {
        return potto4_sizailotno3_2;
    }

    /**
     * ﾎﾟｯﾄ4_資材ﾛｯﾄNo.3_2
     * @param potto4_sizailotno3_2 the potto4_sizailotno3_2 to set
     */
    public void setPotto4_sizailotno3_2(String potto4_sizailotno3_2) {
        this.potto4_sizailotno3_2 = potto4_sizailotno3_2;
    }

    /**
     * ﾎﾟｯﾄ4_調合量3_2
     * @return the potto4_tyougouryou3_2
     */
    public Integer getPotto4_tyougouryou3_2() {
        return potto4_tyougouryou3_2;
    }

    /**
     * ﾎﾟｯﾄ4_調合量3_2
     * @param potto4_tyougouryou3_2 the potto4_tyougouryou3_2 to set
     */
    public void setPotto4_tyougouryou3_2(Integer potto4_tyougouryou3_2) {
        this.potto4_tyougouryou3_2 = potto4_tyougouryou3_2;
    }

    /**
     * ﾎﾟｯﾄ4_材料品名4
     * @return the potto4_zairyohinmei4
     */
    public String getPotto4_zairyohinmei4() {
        return potto4_zairyohinmei4;
    }

    /**
     * ﾎﾟｯﾄ4_材料品名4
     * @param potto4_zairyohinmei4 the potto4_zairyohinmei4 to set
     */
    public void setPotto4_zairyohinmei4(String potto4_zairyohinmei4) {
        this.potto4_zairyohinmei4 = potto4_zairyohinmei4;
    }

    /**
     * ﾎﾟｯﾄ4_調合量規格4
     * @return the potto4_tyogouryoukikaku4
     */
    public String getPotto4_tyogouryoukikaku4() {
        return potto4_tyogouryoukikaku4;
    }

    /**
     * ﾎﾟｯﾄ4_調合量規格4
     * @param potto4_tyogouryoukikaku4 the potto4_tyogouryoukikaku4 to set
     */
    public void setPotto4_tyogouryoukikaku4(String potto4_tyogouryoukikaku4) {
        this.potto4_tyogouryoukikaku4 = potto4_tyogouryoukikaku4;
    }

    /**
     * ﾎﾟｯﾄ4_資材ﾛｯﾄNo.4_1
     * @return the potto4_sizailotno4_1
     */
    public String getPotto4_sizailotno4_1() {
        return potto4_sizailotno4_1;
    }

    /**
     * ﾎﾟｯﾄ4_資材ﾛｯﾄNo.4_1
     * @param potto4_sizailotno4_1 the potto4_sizailotno4_1 to set
     */
    public void setPotto4_sizailotno4_1(String potto4_sizailotno4_1) {
        this.potto4_sizailotno4_1 = potto4_sizailotno4_1;
    }

    /**
     * ﾎﾟｯﾄ4_調合量4_1
     * @return the potto4_tyougouryou4_1
     */
    public Integer getPotto4_tyougouryou4_1() {
        return potto4_tyougouryou4_1;
    }

    /**
     * ﾎﾟｯﾄ4_調合量4_1
     * @param potto4_tyougouryou4_1 the potto4_tyougouryou4_1 to set
     */
    public void setPotto4_tyougouryou4_1(Integer potto4_tyougouryou4_1) {
        this.potto4_tyougouryou4_1 = potto4_tyougouryou4_1;
    }

    /**
     * ﾎﾟｯﾄ4_資材ﾛｯﾄNo.4_2
     * @return the potto4_sizailotno4_2
     */
    public String getPotto4_sizailotno4_2() {
        return potto4_sizailotno4_2;
    }

    /**
     * ﾎﾟｯﾄ4_資材ﾛｯﾄNo.4_2
     * @param potto4_sizailotno4_2 the potto4_sizailotno4_2 to set
     */
    public void setPotto4_sizailotno4_2(String potto4_sizailotno4_2) {
        this.potto4_sizailotno4_2 = potto4_sizailotno4_2;
    }

    /**
     * ﾎﾟｯﾄ4_調合量4_2
     * @return the potto4_tyougouryou4_2
     */
    public Integer getPotto4_tyougouryou4_2() {
        return potto4_tyougouryou4_2;
    }

    /**
     * ﾎﾟｯﾄ4_調合量4_2
     * @param potto4_tyougouryou4_2 the potto4_tyougouryou4_2 to set
     */
    public void setPotto4_tyougouryou4_2(Integer potto4_tyougouryou4_2) {
        this.potto4_tyougouryou4_2 = potto4_tyougouryou4_2;
    }

    /**
     * ﾎﾟｯﾄ5_材料品名1
     * @return the potto5_zairyohinmei1
     */
    public String getPotto5_zairyohinmei1() {
        return potto5_zairyohinmei1;
    }

    /**
     * ﾎﾟｯﾄ5_材料品名1
     * @param potto5_zairyohinmei1 the potto5_zairyohinmei1 to set
     */
    public void setPotto5_zairyohinmei1(String potto5_zairyohinmei1) {
        this.potto5_zairyohinmei1 = potto5_zairyohinmei1;
    }

    /**
     * ﾎﾟｯﾄ5_調合量規格1
     * @return the potto5_tyogouryoukikaku1
     */
    public String getPotto5_tyogouryoukikaku1() {
        return potto5_tyogouryoukikaku1;
    }

    /**
     * ﾎﾟｯﾄ5_調合量規格1
     * @param potto5_tyogouryoukikaku1 the potto5_tyogouryoukikaku1 to set
     */
    public void setPotto5_tyogouryoukikaku1(String potto5_tyogouryoukikaku1) {
        this.potto5_tyogouryoukikaku1 = potto5_tyogouryoukikaku1;
    }

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.1_1
     * @return the potto5_sizailotno1_1
     */
    public String getPotto5_sizailotno1_1() {
        return potto5_sizailotno1_1;
    }

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.1_1
     * @param potto5_sizailotno1_1 the potto5_sizailotno1_1 to set
     */
    public void setPotto5_sizailotno1_1(String potto5_sizailotno1_1) {
        this.potto5_sizailotno1_1 = potto5_sizailotno1_1;
    }

    /**
     * ﾎﾟｯﾄ5_調合量1_1
     * @return the potto5_tyougouryou1_1
     */
    public Integer getPotto5_tyougouryou1_1() {
        return potto5_tyougouryou1_1;
    }

    /**
     * ﾎﾟｯﾄ5_調合量1_1
     * @param potto5_tyougouryou1_1 the potto5_tyougouryou1_1 to set
     */
    public void setPotto5_tyougouryou1_1(Integer potto5_tyougouryou1_1) {
        this.potto5_tyougouryou1_1 = potto5_tyougouryou1_1;
    }

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.1_2
     * @return the potto5_sizailotno1_2
     */
    public String getPotto5_sizailotno1_2() {
        return potto5_sizailotno1_2;
    }

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.1_2
     * @param potto5_sizailotno1_2 the potto5_sizailotno1_2 to set
     */
    public void setPotto5_sizailotno1_2(String potto5_sizailotno1_2) {
        this.potto5_sizailotno1_2 = potto5_sizailotno1_2;
    }

    /**
     * ﾎﾟｯﾄ5_調合量1_2
     * @return the potto5_tyougouryou1_2
     */
    public Integer getPotto5_tyougouryou1_2() {
        return potto5_tyougouryou1_2;
    }

    /**
     * ﾎﾟｯﾄ5_調合量1_2
     * @param potto5_tyougouryou1_2 the potto5_tyougouryou1_2 to set
     */
    public void setPotto5_tyougouryou1_2(Integer potto5_tyougouryou1_2) {
        this.potto5_tyougouryou1_2 = potto5_tyougouryou1_2;
    }

    /**
     * ﾎﾟｯﾄ5_材料品名2
     * @return the potto5_zairyohinmei2
     */
    public String getPotto5_zairyohinmei2() {
        return potto5_zairyohinmei2;
    }

    /**
     * ﾎﾟｯﾄ5_材料品名2
     * @param potto5_zairyohinmei2 the potto5_zairyohinmei2 to set
     */
    public void setPotto5_zairyohinmei2(String potto5_zairyohinmei2) {
        this.potto5_zairyohinmei2 = potto5_zairyohinmei2;
    }

    /**
     * ﾎﾟｯﾄ5_調合量規格2
     * @return the potto5_tyogouryoukikaku2
     */
    public String getPotto5_tyogouryoukikaku2() {
        return potto5_tyogouryoukikaku2;
    }

    /**
     * ﾎﾟｯﾄ5_調合量規格2
     * @param potto5_tyogouryoukikaku2 the potto5_tyogouryoukikaku2 to set
     */
    public void setPotto5_tyogouryoukikaku2(String potto5_tyogouryoukikaku2) {
        this.potto5_tyogouryoukikaku2 = potto5_tyogouryoukikaku2;
    }

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.2_1
     * @return the potto5_sizailotno2_1
     */
    public String getPotto5_sizailotno2_1() {
        return potto5_sizailotno2_1;
    }

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.2_1
     * @param potto5_sizailotno2_1 the potto5_sizailotno2_1 to set
     */
    public void setPotto5_sizailotno2_1(String potto5_sizailotno2_1) {
        this.potto5_sizailotno2_1 = potto5_sizailotno2_1;
    }

    /**
     * ﾎﾟｯﾄ5_調合量2_1
     * @return the potto5_tyougouryou2_1
     */
    public Integer getPotto5_tyougouryou2_1() {
        return potto5_tyougouryou2_1;
    }

    /**
     * ﾎﾟｯﾄ5_調合量2_1
     * @param potto5_tyougouryou2_1 the potto5_tyougouryou2_1 to set
     */
    public void setPotto5_tyougouryou2_1(Integer potto5_tyougouryou2_1) {
        this.potto5_tyougouryou2_1 = potto5_tyougouryou2_1;
    }

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.2_2
     * @return the potto5_sizailotno2_2
     */
    public String getPotto5_sizailotno2_2() {
        return potto5_sizailotno2_2;
    }

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.2_2
     * @param potto5_sizailotno2_2 the potto5_sizailotno2_2 to set
     */
    public void setPotto5_sizailotno2_2(String potto5_sizailotno2_2) {
        this.potto5_sizailotno2_2 = potto5_sizailotno2_2;
    }

    /**
     * ﾎﾟｯﾄ5_調合量2_2
     * @return the potto5_tyougouryou2_2
     */
    public Integer getPotto5_tyougouryou2_2() {
        return potto5_tyougouryou2_2;
    }

    /**
     * ﾎﾟｯﾄ5_調合量2_2
     * @param potto5_tyougouryou2_2 the potto5_tyougouryou2_2 to set
     */
    public void setPotto5_tyougouryou2_2(Integer potto5_tyougouryou2_2) {
        this.potto5_tyougouryou2_2 = potto5_tyougouryou2_2;
    }

    /**
     * ﾎﾟｯﾄ5_材料品名3
     * @return the potto5_zairyohinmei3
     */
    public String getPotto5_zairyohinmei3() {
        return potto5_zairyohinmei3;
    }

    /**
     * ﾎﾟｯﾄ5_材料品名3
     * @param potto5_zairyohinmei3 the potto5_zairyohinmei3 to set
     */
    public void setPotto5_zairyohinmei3(String potto5_zairyohinmei3) {
        this.potto5_zairyohinmei3 = potto5_zairyohinmei3;
    }

    /**
     * ﾎﾟｯﾄ5_調合量規格3
     * @return the potto5_tyogouryoukikaku3
     */
    public String getPotto5_tyogouryoukikaku3() {
        return potto5_tyogouryoukikaku3;
    }

    /**
     * ﾎﾟｯﾄ5_調合量規格3
     * @param potto5_tyogouryoukikaku3 the potto5_tyogouryoukikaku3 to set
     */
    public void setPotto5_tyogouryoukikaku3(String potto5_tyogouryoukikaku3) {
        this.potto5_tyogouryoukikaku3 = potto5_tyogouryoukikaku3;
    }

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.3_1
     * @return the potto5_sizailotno3_1
     */
    public String getPotto5_sizailotno3_1() {
        return potto5_sizailotno3_1;
    }

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.3_1
     * @param potto5_sizailotno3_1 the potto5_sizailotno3_1 to set
     */
    public void setPotto5_sizailotno3_1(String potto5_sizailotno3_1) {
        this.potto5_sizailotno3_1 = potto5_sizailotno3_1;
    }

    /**
     * ﾎﾟｯﾄ5_調合量3_1
     * @return the potto5_tyougouryou3_1
     */
    public Integer getPotto5_tyougouryou3_1() {
        return potto5_tyougouryou3_1;
    }

    /**
     * ﾎﾟｯﾄ5_調合量3_1
     * @param potto5_tyougouryou3_1 the potto5_tyougouryou3_1 to set
     */
    public void setPotto5_tyougouryou3_1(Integer potto5_tyougouryou3_1) {
        this.potto5_tyougouryou3_1 = potto5_tyougouryou3_1;
    }

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.3_2
     * @return the potto5_sizailotno3_2
     */
    public String getPotto5_sizailotno3_2() {
        return potto5_sizailotno3_2;
    }

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.3_2
     * @param potto5_sizailotno3_2 the potto5_sizailotno3_2 to set
     */
    public void setPotto5_sizailotno3_2(String potto5_sizailotno3_2) {
        this.potto5_sizailotno3_2 = potto5_sizailotno3_2;
    }

    /**
     * ﾎﾟｯﾄ5_調合量3_2
     * @return the potto5_tyougouryou3_2
     */
    public Integer getPotto5_tyougouryou3_2() {
        return potto5_tyougouryou3_2;
    }

    /**
     * ﾎﾟｯﾄ5_調合量3_2
     * @param potto5_tyougouryou3_2 the potto5_tyougouryou3_2 to set
     */
    public void setPotto5_tyougouryou3_2(Integer potto5_tyougouryou3_2) {
        this.potto5_tyougouryou3_2 = potto5_tyougouryou3_2;
    }

    /**
     * ﾎﾟｯﾄ5_材料品名4
     * @return the potto5_zairyohinmei4
     */
    public String getPotto5_zairyohinmei4() {
        return potto5_zairyohinmei4;
    }

    /**
     * ﾎﾟｯﾄ5_材料品名4
     * @param potto5_zairyohinmei4 the potto5_zairyohinmei4 to set
     */
    public void setPotto5_zairyohinmei4(String potto5_zairyohinmei4) {
        this.potto5_zairyohinmei4 = potto5_zairyohinmei4;
    }

    /**
     * ﾎﾟｯﾄ5_調合量規格4
     * @return the potto5_tyogouryoukikaku4
     */
    public String getPotto5_tyogouryoukikaku4() {
        return potto5_tyogouryoukikaku4;
    }

    /**
     * ﾎﾟｯﾄ5_調合量規格4
     * @param potto5_tyogouryoukikaku4 the potto5_tyogouryoukikaku4 to set
     */
    public void setPotto5_tyogouryoukikaku4(String potto5_tyogouryoukikaku4) {
        this.potto5_tyogouryoukikaku4 = potto5_tyogouryoukikaku4;
    }

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.4_1
     * @return the potto5_sizailotno4_1
     */
    public String getPotto5_sizailotno4_1() {
        return potto5_sizailotno4_1;
    }

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.4_1
     * @param potto5_sizailotno4_1 the potto5_sizailotno4_1 to set
     */
    public void setPotto5_sizailotno4_1(String potto5_sizailotno4_1) {
        this.potto5_sizailotno4_1 = potto5_sizailotno4_1;
    }

    /**
     * ﾎﾟｯﾄ5_調合量4_1
     * @return the potto5_tyougouryou4_1
     */
    public Integer getPotto5_tyougouryou4_1() {
        return potto5_tyougouryou4_1;
    }

    /**
     * ﾎﾟｯﾄ5_調合量4_1
     * @param potto5_tyougouryou4_1 the potto5_tyougouryou4_1 to set
     */
    public void setPotto5_tyougouryou4_1(Integer potto5_tyougouryou4_1) {
        this.potto5_tyougouryou4_1 = potto5_tyougouryou4_1;
    }

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.4_2
     * @return the potto5_sizailotno4_2
     */
    public String getPotto5_sizailotno4_2() {
        return potto5_sizailotno4_2;
    }

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.4_2
     * @param potto5_sizailotno4_2 the potto5_sizailotno4_2 to set
     */
    public void setPotto5_sizailotno4_2(String potto5_sizailotno4_2) {
        this.potto5_sizailotno4_2 = potto5_sizailotno4_2;
    }

    /**
     * ﾎﾟｯﾄ5_調合量4_2
     * @return the potto5_tyougouryou4_2
     */
    public Integer getPotto5_tyougouryou4_2() {
        return potto5_tyougouryou4_2;
    }

    /**
     * ﾎﾟｯﾄ5_調合量4_2
     * @param potto5_tyougouryou4_2 the potto5_tyougouryou4_2 to set
     */
    public void setPotto5_tyougouryou4_2(Integer potto5_tyougouryou4_2) {
        this.potto5_tyougouryou4_2 = potto5_tyougouryou4_2;
    }

    /**
     * ﾎﾟｯﾄ6_材料品名1
     * @return the potto6_zairyohinmei1
     */
    public String getPotto6_zairyohinmei1() {
        return potto6_zairyohinmei1;
    }

    /**
     * ﾎﾟｯﾄ6_材料品名1
     * @param potto6_zairyohinmei1 the potto6_zairyohinmei1 to set
     */
    public void setPotto6_zairyohinmei1(String potto6_zairyohinmei1) {
        this.potto6_zairyohinmei1 = potto6_zairyohinmei1;
    }

    /**
     * ﾎﾟｯﾄ6_調合量規格1
     * @return the potto6_tyogouryoukikaku1
     */
    public String getPotto6_tyogouryoukikaku1() {
        return potto6_tyogouryoukikaku1;
    }

    /**
     * ﾎﾟｯﾄ6_調合量規格1
     * @param potto6_tyogouryoukikaku1 the potto6_tyogouryoukikaku1 to set
     */
    public void setPotto6_tyogouryoukikaku1(String potto6_tyogouryoukikaku1) {
        this.potto6_tyogouryoukikaku1 = potto6_tyogouryoukikaku1;
    }

    /**
     * ﾎﾟｯﾄ6_資材ﾛｯﾄNo.1_1
     * @return the potto6_sizailotno1_1
     */
    public String getPotto6_sizailotno1_1() {
        return potto6_sizailotno1_1;
    }

    /**
     * ﾎﾟｯﾄ6_資材ﾛｯﾄNo.1_1
     * @param potto6_sizailotno1_1 the potto6_sizailotno1_1 to set
     */
    public void setPotto6_sizailotno1_1(String potto6_sizailotno1_1) {
        this.potto6_sizailotno1_1 = potto6_sizailotno1_1;
    }

    /**
     * ﾎﾟｯﾄ6_調合量1_1
     * @return the potto6_tyougouryou1_1
     */
    public Integer getPotto6_tyougouryou1_1() {
        return potto6_tyougouryou1_1;
    }

    /**
     * ﾎﾟｯﾄ6_調合量1_1
     * @param potto6_tyougouryou1_1 the potto6_tyougouryou1_1 to set
     */
    public void setPotto6_tyougouryou1_1(Integer potto6_tyougouryou1_1) {
        this.potto6_tyougouryou1_1 = potto6_tyougouryou1_1;
    }

    /**
     * ﾎﾟｯﾄ6_資材ﾛｯﾄNo.1_2
     * @return the potto6_sizailotno1_2
     */
    public String getPotto6_sizailotno1_2() {
        return potto6_sizailotno1_2;
    }

    /**
     * ﾎﾟｯﾄ6_資材ﾛｯﾄNo.1_2
     * @param potto6_sizailotno1_2 the potto6_sizailotno1_2 to set
     */
    public void setPotto6_sizailotno1_2(String potto6_sizailotno1_2) {
        this.potto6_sizailotno1_2 = potto6_sizailotno1_2;
    }

    /**
     * ﾎﾟｯﾄ6_調合量1_2
     * @return the potto6_tyougouryou1_2
     */
    public Integer getPotto6_tyougouryou1_2() {
        return potto6_tyougouryou1_2;
    }

    /**
     * ﾎﾟｯﾄ6_調合量1_2
     * @param potto6_tyougouryou1_2 the potto6_tyougouryou1_2 to set
     */
    public void setPotto6_tyougouryou1_2(Integer potto6_tyougouryou1_2) {
        this.potto6_tyougouryou1_2 = potto6_tyougouryou1_2;
    }

    /**
     * ﾎﾟｯﾄ6_材料品名2
     * @return the potto6_zairyohinmei2
     */
    public String getPotto6_zairyohinmei2() {
        return potto6_zairyohinmei2;
    }

    /**
     * ﾎﾟｯﾄ6_材料品名2
     * @param potto6_zairyohinmei2 the potto6_zairyohinmei2 to set
     */
    public void setPotto6_zairyohinmei2(String potto6_zairyohinmei2) {
        this.potto6_zairyohinmei2 = potto6_zairyohinmei2;
    }

    /**
     * ﾎﾟｯﾄ6_調合量規格2
     * @return the potto6_tyogouryoukikaku2
     */
    public String getPotto6_tyogouryoukikaku2() {
        return potto6_tyogouryoukikaku2;
    }

    /**
     * ﾎﾟｯﾄ6_調合量規格2
     * @param potto6_tyogouryoukikaku2 the potto6_tyogouryoukikaku2 to set
     */
    public void setPotto6_tyogouryoukikaku2(String potto6_tyogouryoukikaku2) {
        this.potto6_tyogouryoukikaku2 = potto6_tyogouryoukikaku2;
    }

    /**
     * ﾎﾟｯﾄ6_資材ﾛｯﾄNo.2_1
     * @return the potto6_sizailotno2_1
     */
    public String getPotto6_sizailotno2_1() {
        return potto6_sizailotno2_1;
    }

    /**
     * ﾎﾟｯﾄ6_資材ﾛｯﾄNo.2_1
     * @param potto6_sizailotno2_1 the potto6_sizailotno2_1 to set
     */
    public void setPotto6_sizailotno2_1(String potto6_sizailotno2_1) {
        this.potto6_sizailotno2_1 = potto6_sizailotno2_1;
    }

    /**
     * ﾎﾟｯﾄ6_調合量2_1
     * @return the potto6_tyougouryou2_1
     */
    public Integer getPotto6_tyougouryou2_1() {
        return potto6_tyougouryou2_1;
    }

    /**
     * ﾎﾟｯﾄ6_調合量2_1
     * @param potto6_tyougouryou2_1 the potto6_tyougouryou2_1 to set
     */
    public void setPotto6_tyougouryou2_1(Integer potto6_tyougouryou2_1) {
        this.potto6_tyougouryou2_1 = potto6_tyougouryou2_1;
    }

    /**
     * ﾎﾟｯﾄ6_資材ﾛｯﾄNo.2_2
     * @return the potto6_sizailotno2_2
     */
    public String getPotto6_sizailotno2_2() {
        return potto6_sizailotno2_2;
    }

    /**
     * ﾎﾟｯﾄ6_資材ﾛｯﾄNo.2_2
     * @param potto6_sizailotno2_2 the potto6_sizailotno2_2 to set
     */
    public void setPotto6_sizailotno2_2(String potto6_sizailotno2_2) {
        this.potto6_sizailotno2_2 = potto6_sizailotno2_2;
    }

    /**
     * ﾎﾟｯﾄ6_調合量2_2
     * @return the potto6_tyougouryou2_2
     */
    public Integer getPotto6_tyougouryou2_2() {
        return potto6_tyougouryou2_2;
    }

    /**
     * ﾎﾟｯﾄ6_調合量2_2
     * @param potto6_tyougouryou2_2 the potto6_tyougouryou2_2 to set
     */
    public void setPotto6_tyougouryou2_2(Integer potto6_tyougouryou2_2) {
        this.potto6_tyougouryou2_2 = potto6_tyougouryou2_2;
    }

    /**
     * ﾎﾟｯﾄ6_材料品名3
     * @return the potto6_zairyohinmei3
     */
    public String getPotto6_zairyohinmei3() {
        return potto6_zairyohinmei3;
    }

    /**
     * ﾎﾟｯﾄ6_材料品名3
     * @param potto6_zairyohinmei3 the potto6_zairyohinmei3 to set
     */
    public void setPotto6_zairyohinmei3(String potto6_zairyohinmei3) {
        this.potto6_zairyohinmei3 = potto6_zairyohinmei3;
    }

    /**
     * ﾎﾟｯﾄ6_調合量規格3
     * @return the potto6_tyogouryoukikaku3
     */
    public String getPotto6_tyogouryoukikaku3() {
        return potto6_tyogouryoukikaku3;
    }

    /**
     * ﾎﾟｯﾄ6_調合量規格3
     * @param potto6_tyogouryoukikaku3 the potto6_tyogouryoukikaku3 to set
     */
    public void setPotto6_tyogouryoukikaku3(String potto6_tyogouryoukikaku3) {
        this.potto6_tyogouryoukikaku3 = potto6_tyogouryoukikaku3;
    }

    /**
     * ﾎﾟｯﾄ6_資材ﾛｯﾄNo.3_1
     * @return the potto6_sizailotno3_1
     */
    public String getPotto6_sizailotno3_1() {
        return potto6_sizailotno3_1;
    }

    /**
     * ﾎﾟｯﾄ6_資材ﾛｯﾄNo.3_1
     * @param potto6_sizailotno3_1 the potto6_sizailotno3_1 to set
     */
    public void setPotto6_sizailotno3_1(String potto6_sizailotno3_1) {
        this.potto6_sizailotno3_1 = potto6_sizailotno3_1;
    }

    /**
     * ﾎﾟｯﾄ6_調合量3_1
     * @return the potto6_tyougouryou3_1
     */
    public Integer getPotto6_tyougouryou3_1() {
        return potto6_tyougouryou3_1;
    }

    /**
     * ﾎﾟｯﾄ6_調合量3_1
     * @param potto6_tyougouryou3_1 the potto6_tyougouryou3_1 to set
     */
    public void setPotto6_tyougouryou3_1(Integer potto6_tyougouryou3_1) {
        this.potto6_tyougouryou3_1 = potto6_tyougouryou3_1;
    }

    /**
     * ﾎﾟｯﾄ6_資材ﾛｯﾄNo.3_2
     * @return the potto6_sizailotno3_2
     */
    public String getPotto6_sizailotno3_2() {
        return potto6_sizailotno3_2;
    }

    /**
     * ﾎﾟｯﾄ6_資材ﾛｯﾄNo.3_2
     * @param potto6_sizailotno3_2 the potto6_sizailotno3_2 to set
     */
    public void setPotto6_sizailotno3_2(String potto6_sizailotno3_2) {
        this.potto6_sizailotno3_2 = potto6_sizailotno3_2;
    }

    /**
     * ﾎﾟｯﾄ6_調合量3_2
     * @return the potto6_tyougouryou3_2
     */
    public Integer getPotto6_tyougouryou3_2() {
        return potto6_tyougouryou3_2;
    }

    /**
     * ﾎﾟｯﾄ6_調合量3_2
     * @param potto6_tyougouryou3_2 the potto6_tyougouryou3_2 to set
     */
    public void setPotto6_tyougouryou3_2(Integer potto6_tyougouryou3_2) {
        this.potto6_tyougouryou3_2 = potto6_tyougouryou3_2;
    }

    /**
     * ﾎﾟｯﾄ6_材料品名4
     * @return the potto6_zairyohinmei4
     */
    public String getPotto6_zairyohinmei4() {
        return potto6_zairyohinmei4;
    }

    /**
     * ﾎﾟｯﾄ6_材料品名4
     * @param potto6_zairyohinmei4 the potto6_zairyohinmei4 to set
     */
    public void setPotto6_zairyohinmei4(String potto6_zairyohinmei4) {
        this.potto6_zairyohinmei4 = potto6_zairyohinmei4;
    }

    /**
     * ﾎﾟｯﾄ6_調合量規格4
     * @return the potto6_tyogouryoukikaku4
     */
    public String getPotto6_tyogouryoukikaku4() {
        return potto6_tyogouryoukikaku4;
    }

    /**
     * ﾎﾟｯﾄ6_調合量規格4
     * @param potto6_tyogouryoukikaku4 the potto6_tyogouryoukikaku4 to set
     */
    public void setPotto6_tyogouryoukikaku4(String potto6_tyogouryoukikaku4) {
        this.potto6_tyogouryoukikaku4 = potto6_tyogouryoukikaku4;
    }

    /**
     * ﾎﾟｯﾄ6_資材ﾛｯﾄNo.4_1
     * @return the potto6_sizailotno4_1
     */
    public String getPotto6_sizailotno4_1() {
        return potto6_sizailotno4_1;
    }

    /**
     * ﾎﾟｯﾄ6_資材ﾛｯﾄNo.4_1
     * @param potto6_sizailotno4_1 the potto6_sizailotno4_1 to set
     */
    public void setPotto6_sizailotno4_1(String potto6_sizailotno4_1) {
        this.potto6_sizailotno4_1 = potto6_sizailotno4_1;
    }

    /**
     * ﾎﾟｯﾄ6_調合量4_1
     * @return the potto6_tyougouryou4_1
     */
    public Integer getPotto6_tyougouryou4_1() {
        return potto6_tyougouryou4_1;
    }

    /**
     * ﾎﾟｯﾄ6_調合量4_1
     * @param potto6_tyougouryou4_1 the potto6_tyougouryou4_1 to set
     */
    public void setPotto6_tyougouryou4_1(Integer potto6_tyougouryou4_1) {
        this.potto6_tyougouryou4_1 = potto6_tyougouryou4_1;
    }

    /**
     * ﾎﾟｯﾄ6_資材ﾛｯﾄNo.4_2
     * @return the potto6_sizailotno4_2
     */
    public String getPotto6_sizailotno4_2() {
        return potto6_sizailotno4_2;
    }

    /**
     * ﾎﾟｯﾄ6_資材ﾛｯﾄNo.4_2
     * @param potto6_sizailotno4_2 the potto6_sizailotno4_2 to set
     */
    public void setPotto6_sizailotno4_2(String potto6_sizailotno4_2) {
        this.potto6_sizailotno4_2 = potto6_sizailotno4_2;
    }

    /**
     * ﾎﾟｯﾄ6_調合量4_2
     * @return the potto6_tyougouryou4_2
     */
    public Integer getPotto6_tyougouryou4_2() {
        return potto6_tyougouryou4_2;
    }

    /**
     * ﾎﾟｯﾄ6_調合量4_2
     * @param potto6_tyougouryou4_2 the potto6_tyougouryou4_2 to set
     */
    public void setPotto6_tyougouryou4_2(Integer potto6_tyougouryou4_2) {
        this.potto6_tyougouryou4_2 = potto6_tyougouryou4_2;
    }

    /**
     * ﾎﾟｯﾄ7_材料品名1
     * @return the potto7_zairyohinmei1
     */
    public String getPotto7_zairyohinmei1() {
        return potto7_zairyohinmei1;
    }

    /**
     * ﾎﾟｯﾄ7_材料品名1
     * @param potto7_zairyohinmei1 the potto7_zairyohinmei1 to set
     */
    public void setPotto7_zairyohinmei1(String potto7_zairyohinmei1) {
        this.potto7_zairyohinmei1 = potto7_zairyohinmei1;
    }

    /**
     * ﾎﾟｯﾄ7_調合量規格1
     * @return the potto7_tyogouryoukikaku1
     */
    public String getPotto7_tyogouryoukikaku1() {
        return potto7_tyogouryoukikaku1;
    }

    /**
     * ﾎﾟｯﾄ7_調合量規格1
     * @param potto7_tyogouryoukikaku1 the potto7_tyogouryoukikaku1 to set
     */
    public void setPotto7_tyogouryoukikaku1(String potto7_tyogouryoukikaku1) {
        this.potto7_tyogouryoukikaku1 = potto7_tyogouryoukikaku1;
    }

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.1_1
     * @return the potto7_sizailotno1_1
     */
    public String getPotto7_sizailotno1_1() {
        return potto7_sizailotno1_1;
    }

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.1_1
     * @param potto7_sizailotno1_1 the potto7_sizailotno1_1 to set
     */
    public void setPotto7_sizailotno1_1(String potto7_sizailotno1_1) {
        this.potto7_sizailotno1_1 = potto7_sizailotno1_1;
    }

    /**
     * ﾎﾟｯﾄ7_調合量1_1
     * @return the potto7_tyougouryou1_1
     */
    public Integer getPotto7_tyougouryou1_1() {
        return potto7_tyougouryou1_1;
    }

    /**
     * ﾎﾟｯﾄ7_調合量1_1
     * @param potto7_tyougouryou1_1 the potto7_tyougouryou1_1 to set
     */
    public void setPotto7_tyougouryou1_1(Integer potto7_tyougouryou1_1) {
        this.potto7_tyougouryou1_1 = potto7_tyougouryou1_1;
    }

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.1_2
     * @return the potto7_sizailotno1_2
     */
    public String getPotto7_sizailotno1_2() {
        return potto7_sizailotno1_2;
    }

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.1_2
     * @param potto7_sizailotno1_2 the potto7_sizailotno1_2 to set
     */
    public void setPotto7_sizailotno1_2(String potto7_sizailotno1_2) {
        this.potto7_sizailotno1_2 = potto7_sizailotno1_2;
    }

    /**
     * ﾎﾟｯﾄ7_調合量1_2
     * @return the potto7_tyougouryou1_2
     */
    public Integer getPotto7_tyougouryou1_2() {
        return potto7_tyougouryou1_2;
    }

    /**
     * ﾎﾟｯﾄ7_調合量1_2
     * @param potto7_tyougouryou1_2 the potto7_tyougouryou1_2 to set
     */
    public void setPotto7_tyougouryou1_2(Integer potto7_tyougouryou1_2) {
        this.potto7_tyougouryou1_2 = potto7_tyougouryou1_2;
    }

    /**
     * ﾎﾟｯﾄ7_材料品名2
     * @return the potto7_zairyohinmei2
     */
    public String getPotto7_zairyohinmei2() {
        return potto7_zairyohinmei2;
    }

    /**
     * ﾎﾟｯﾄ7_材料品名2
     * @param potto7_zairyohinmei2 the potto7_zairyohinmei2 to set
     */
    public void setPotto7_zairyohinmei2(String potto7_zairyohinmei2) {
        this.potto7_zairyohinmei2 = potto7_zairyohinmei2;
    }

    /**
     * ﾎﾟｯﾄ7_調合量規格2
     * @return the potto7_tyogouryoukikaku2
     */
    public String getPotto7_tyogouryoukikaku2() {
        return potto7_tyogouryoukikaku2;
    }

    /**
     * ﾎﾟｯﾄ7_調合量規格2
     * @param potto7_tyogouryoukikaku2 the potto7_tyogouryoukikaku2 to set
     */
    public void setPotto7_tyogouryoukikaku2(String potto7_tyogouryoukikaku2) {
        this.potto7_tyogouryoukikaku2 = potto7_tyogouryoukikaku2;
    }

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.2_1
     * @return the potto7_sizailotno2_1
     */
    public String getPotto7_sizailotno2_1() {
        return potto7_sizailotno2_1;
    }

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.2_1
     * @param potto7_sizailotno2_1 the potto7_sizailotno2_1 to set
     */
    public void setPotto7_sizailotno2_1(String potto7_sizailotno2_1) {
        this.potto7_sizailotno2_1 = potto7_sizailotno2_1;
    }

    /**
     * ﾎﾟｯﾄ7_調合量2_1
     * @return the potto7_tyougouryou2_1
     */
    public Integer getPotto7_tyougouryou2_1() {
        return potto7_tyougouryou2_1;
    }

    /**
     * ﾎﾟｯﾄ7_調合量2_1
     * @param potto7_tyougouryou2_1 the potto7_tyougouryou2_1 to set
     */
    public void setPotto7_tyougouryou2_1(Integer potto7_tyougouryou2_1) {
        this.potto7_tyougouryou2_1 = potto7_tyougouryou2_1;
    }

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.2_2
     * @return the potto7_sizailotno2_2
     */
    public String getPotto7_sizailotno2_2() {
        return potto7_sizailotno2_2;
    }

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.2_2
     * @param potto7_sizailotno2_2 the potto7_sizailotno2_2 to set
     */
    public void setPotto7_sizailotno2_2(String potto7_sizailotno2_2) {
        this.potto7_sizailotno2_2 = potto7_sizailotno2_2;
    }

    /**
     * ﾎﾟｯﾄ7_調合量2_2
     * @return the potto7_tyougouryou2_2
     */
    public Integer getPotto7_tyougouryou2_2() {
        return potto7_tyougouryou2_2;
    }

    /**
     * ﾎﾟｯﾄ7_調合量2_2
     * @param potto7_tyougouryou2_2 the potto7_tyougouryou2_2 to set
     */
    public void setPotto7_tyougouryou2_2(Integer potto7_tyougouryou2_2) {
        this.potto7_tyougouryou2_2 = potto7_tyougouryou2_2;
    }

    /**
     * ﾎﾟｯﾄ7_材料品名3
     * @return the potto7_zairyohinmei3
     */
    public String getPotto7_zairyohinmei3() {
        return potto7_zairyohinmei3;
    }

    /**
     * ﾎﾟｯﾄ7_材料品名3
     * @param potto7_zairyohinmei3 the potto7_zairyohinmei3 to set
     */
    public void setPotto7_zairyohinmei3(String potto7_zairyohinmei3) {
        this.potto7_zairyohinmei3 = potto7_zairyohinmei3;
    }

    /**
     * ﾎﾟｯﾄ7_調合量規格3
     * @return the potto7_tyogouryoukikaku3
     */
    public String getPotto7_tyogouryoukikaku3() {
        return potto7_tyogouryoukikaku3;
    }

    /**
     * ﾎﾟｯﾄ7_調合量規格3
     * @param potto7_tyogouryoukikaku3 the potto7_tyogouryoukikaku3 to set
     */
    public void setPotto7_tyogouryoukikaku3(String potto7_tyogouryoukikaku3) {
        this.potto7_tyogouryoukikaku3 = potto7_tyogouryoukikaku3;
    }

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.3_1
     * @return the potto7_sizailotno3_1
     */
    public String getPotto7_sizailotno3_1() {
        return potto7_sizailotno3_1;
    }

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.3_1
     * @param potto7_sizailotno3_1 the potto7_sizailotno3_1 to set
     */
    public void setPotto7_sizailotno3_1(String potto7_sizailotno3_1) {
        this.potto7_sizailotno3_1 = potto7_sizailotno3_1;
    }

    /**
     * ﾎﾟｯﾄ7_調合量3_1
     * @return the potto7_tyougouryou3_1
     */
    public Integer getPotto7_tyougouryou3_1() {
        return potto7_tyougouryou3_1;
    }

    /**
     * ﾎﾟｯﾄ7_調合量3_1
     * @param potto7_tyougouryou3_1 the potto7_tyougouryou3_1 to set
     */
    public void setPotto7_tyougouryou3_1(Integer potto7_tyougouryou3_1) {
        this.potto7_tyougouryou3_1 = potto7_tyougouryou3_1;
    }

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.3_2
     * @return the potto7_sizailotno3_2
     */
    public String getPotto7_sizailotno3_2() {
        return potto7_sizailotno3_2;
    }

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.3_2
     * @param potto7_sizailotno3_2 the potto7_sizailotno3_2 to set
     */
    public void setPotto7_sizailotno3_2(String potto7_sizailotno3_2) {
        this.potto7_sizailotno3_2 = potto7_sizailotno3_2;
    }

    /**
     * ﾎﾟｯﾄ7_調合量3_2
     * @return the potto7_tyougouryou3_2
     */
    public Integer getPotto7_tyougouryou3_2() {
        return potto7_tyougouryou3_2;
    }

    /**
     * ﾎﾟｯﾄ7_調合量3_2
     * @param potto7_tyougouryou3_2 the potto7_tyougouryou3_2 to set
     */
    public void setPotto7_tyougouryou3_2(Integer potto7_tyougouryou3_2) {
        this.potto7_tyougouryou3_2 = potto7_tyougouryou3_2;
    }

    /**
     * ﾎﾟｯﾄ7_材料品名4
     * @return the potto7_zairyohinmei4
     */
    public String getPotto7_zairyohinmei4() {
        return potto7_zairyohinmei4;
    }

    /**
     * ﾎﾟｯﾄ7_材料品名4
     * @param potto7_zairyohinmei4 the potto7_zairyohinmei4 to set
     */
    public void setPotto7_zairyohinmei4(String potto7_zairyohinmei4) {
        this.potto7_zairyohinmei4 = potto7_zairyohinmei4;
    }

    /**
     * ﾎﾟｯﾄ7_調合量規格4
     * @return the potto7_tyogouryoukikaku4
     */
    public String getPotto7_tyogouryoukikaku4() {
        return potto7_tyogouryoukikaku4;
    }

    /**
     * ﾎﾟｯﾄ7_調合量規格4
     * @param potto7_tyogouryoukikaku4 the potto7_tyogouryoukikaku4 to set
     */
    public void setPotto7_tyogouryoukikaku4(String potto7_tyogouryoukikaku4) {
        this.potto7_tyogouryoukikaku4 = potto7_tyogouryoukikaku4;
    }

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.4_1
     * @return the potto7_sizailotno4_1
     */
    public String getPotto7_sizailotno4_1() {
        return potto7_sizailotno4_1;
    }

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.4_1
     * @param potto7_sizailotno4_1 the potto7_sizailotno4_1 to set
     */
    public void setPotto7_sizailotno4_1(String potto7_sizailotno4_1) {
        this.potto7_sizailotno4_1 = potto7_sizailotno4_1;
    }

    /**
     * ﾎﾟｯﾄ7_調合量4_1
     * @return the potto7_tyougouryou4_1
     */
    public Integer getPotto7_tyougouryou4_1() {
        return potto7_tyougouryou4_1;
    }

    /**
     * ﾎﾟｯﾄ7_調合量4_1
     * @param potto7_tyougouryou4_1 the potto7_tyougouryou4_1 to set
     */
    public void setPotto7_tyougouryou4_1(Integer potto7_tyougouryou4_1) {
        this.potto7_tyougouryou4_1 = potto7_tyougouryou4_1;
    }

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.4_2
     * @return the potto7_sizailotno4_2
     */
    public String getPotto7_sizailotno4_2() {
        return potto7_sizailotno4_2;
    }

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.4_2
     * @param potto7_sizailotno4_2 the potto7_sizailotno4_2 to set
     */
    public void setPotto7_sizailotno4_2(String potto7_sizailotno4_2) {
        this.potto7_sizailotno4_2 = potto7_sizailotno4_2;
    }

    /**
     * ﾎﾟｯﾄ7_調合量4_2
     * @return the potto7_tyougouryou4_2
     */
    public Integer getPotto7_tyougouryou4_2() {
        return potto7_tyougouryou4_2;
    }

    /**
     * ﾎﾟｯﾄ7_調合量4_2
     * @param potto7_tyougouryou4_2 the potto7_tyougouryou4_2 to set
     */
    public void setPotto7_tyougouryou4_2(Integer potto7_tyougouryou4_2) {
        this.potto7_tyougouryou4_2 = potto7_tyougouryou4_2;
    }

    /**
     * ﾎﾟｯﾄ8_材料品名1
     * @return the potto8_zairyohinmei1
     */
    public String getPotto8_zairyohinmei1() {
        return potto8_zairyohinmei1;
    }

    /**
     * ﾎﾟｯﾄ8_材料品名1
     * @param potto8_zairyohinmei1 the potto8_zairyohinmei1 to set
     */
    public void setPotto8_zairyohinmei1(String potto8_zairyohinmei1) {
        this.potto8_zairyohinmei1 = potto8_zairyohinmei1;
    }

    /**
     * ﾎﾟｯﾄ8_調合量規格1
     * @return the potto8_tyogouryoukikaku1
     */
    public String getPotto8_tyogouryoukikaku1() {
        return potto8_tyogouryoukikaku1;
    }

    /**
     * ﾎﾟｯﾄ8_調合量規格1
     * @param potto8_tyogouryoukikaku1 the potto8_tyogouryoukikaku1 to set
     */
    public void setPotto8_tyogouryoukikaku1(String potto8_tyogouryoukikaku1) {
        this.potto8_tyogouryoukikaku1 = potto8_tyogouryoukikaku1;
    }

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.1_1
     * @return the potto8_sizailotno1_1
     */
    public String getPotto8_sizailotno1_1() {
        return potto8_sizailotno1_1;
    }

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.1_1
     * @param potto8_sizailotno1_1 the potto8_sizailotno1_1 to set
     */
    public void setPotto8_sizailotno1_1(String potto8_sizailotno1_1) {
        this.potto8_sizailotno1_1 = potto8_sizailotno1_1;
    }

    /**
     * ﾎﾟｯﾄ8_調合量1_1
     * @return the potto8_tyougouryou1_1
     */
    public Integer getPotto8_tyougouryou1_1() {
        return potto8_tyougouryou1_1;
    }

    /**
     * ﾎﾟｯﾄ8_調合量1_1
     * @param potto8_tyougouryou1_1 the potto8_tyougouryou1_1 to set
     */
    public void setPotto8_tyougouryou1_1(Integer potto8_tyougouryou1_1) {
        this.potto8_tyougouryou1_1 = potto8_tyougouryou1_1;
    }

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.1_2
     * @return the potto8_sizailotno1_2
     */
    public String getPotto8_sizailotno1_2() {
        return potto8_sizailotno1_2;
    }

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.1_2
     * @param potto8_sizailotno1_2 the potto8_sizailotno1_2 to set
     */
    public void setPotto8_sizailotno1_2(String potto8_sizailotno1_2) {
        this.potto8_sizailotno1_2 = potto8_sizailotno1_2;
    }

    /**
     * ﾎﾟｯﾄ8_調合量1_2
     * @return the potto8_tyougouryou1_2
     */
    public Integer getPotto8_tyougouryou1_2() {
        return potto8_tyougouryou1_2;
    }

    /**
     * ﾎﾟｯﾄ8_調合量1_2
     * @param potto8_tyougouryou1_2 the potto8_tyougouryou1_2 to set
     */
    public void setPotto8_tyougouryou1_2(Integer potto8_tyougouryou1_2) {
        this.potto8_tyougouryou1_2 = potto8_tyougouryou1_2;
    }

    /**
     * ﾎﾟｯﾄ8_材料品名2
     * @return the potto8_zairyohinmei2
     */
    public String getPotto8_zairyohinmei2() {
        return potto8_zairyohinmei2;
    }

    /**
     * ﾎﾟｯﾄ8_材料品名2
     * @param potto8_zairyohinmei2 the potto8_zairyohinmei2 to set
     */
    public void setPotto8_zairyohinmei2(String potto8_zairyohinmei2) {
        this.potto8_zairyohinmei2 = potto8_zairyohinmei2;
    }

    /**
     * ﾎﾟｯﾄ8_調合量規格2
     * @return the potto8_tyogouryoukikaku2
     */
    public String getPotto8_tyogouryoukikaku2() {
        return potto8_tyogouryoukikaku2;
    }

    /**
     * ﾎﾟｯﾄ8_調合量規格2
     * @param potto8_tyogouryoukikaku2 the potto8_tyogouryoukikaku2 to set
     */
    public void setPotto8_tyogouryoukikaku2(String potto8_tyogouryoukikaku2) {
        this.potto8_tyogouryoukikaku2 = potto8_tyogouryoukikaku2;
    }

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.2_1
     * @return the potto8_sizailotno2_1
     */
    public String getPotto8_sizailotno2_1() {
        return potto8_sizailotno2_1;
    }

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.2_1
     * @param potto8_sizailotno2_1 the potto8_sizailotno2_1 to set
     */
    public void setPotto8_sizailotno2_1(String potto8_sizailotno2_1) {
        this.potto8_sizailotno2_1 = potto8_sizailotno2_1;
    }

    /**
     * ﾎﾟｯﾄ8_調合量2_1
     * @return the potto8_tyougouryou2_1
     */
    public Integer getPotto8_tyougouryou2_1() {
        return potto8_tyougouryou2_1;
    }

    /**
     * ﾎﾟｯﾄ8_調合量2_1
     * @param potto8_tyougouryou2_1 the potto8_tyougouryou2_1 to set
     */
    public void setPotto8_tyougouryou2_1(Integer potto8_tyougouryou2_1) {
        this.potto8_tyougouryou2_1 = potto8_tyougouryou2_1;
    }

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.2_2
     * @return the potto8_sizailotno2_2
     */
    public String getPotto8_sizailotno2_2() {
        return potto8_sizailotno2_2;
    }

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.2_2
     * @param potto8_sizailotno2_2 the potto8_sizailotno2_2 to set
     */
    public void setPotto8_sizailotno2_2(String potto8_sizailotno2_2) {
        this.potto8_sizailotno2_2 = potto8_sizailotno2_2;
    }

    /**
     * ﾎﾟｯﾄ8_調合量2_2
     * @return the potto8_tyougouryou2_2
     */
    public Integer getPotto8_tyougouryou2_2() {
        return potto8_tyougouryou2_2;
    }

    /**
     * ﾎﾟｯﾄ8_調合量2_2
     * @param potto8_tyougouryou2_2 the potto8_tyougouryou2_2 to set
     */
    public void setPotto8_tyougouryou2_2(Integer potto8_tyougouryou2_2) {
        this.potto8_tyougouryou2_2 = potto8_tyougouryou2_2;
    }

    /**
     * ﾎﾟｯﾄ8_材料品名3
     * @return the potto8_zairyohinmei3
     */
    public String getPotto8_zairyohinmei3() {
        return potto8_zairyohinmei3;
    }

    /**
     * ﾎﾟｯﾄ8_材料品名3
     * @param potto8_zairyohinmei3 the potto8_zairyohinmei3 to set
     */
    public void setPotto8_zairyohinmei3(String potto8_zairyohinmei3) {
        this.potto8_zairyohinmei3 = potto8_zairyohinmei3;
    }

    /**
     * ﾎﾟｯﾄ8_調合量規格3
     * @return the potto8_tyogouryoukikaku3
     */
    public String getPotto8_tyogouryoukikaku3() {
        return potto8_tyogouryoukikaku3;
    }

    /**
     * ﾎﾟｯﾄ8_調合量規格3
     * @param potto8_tyogouryoukikaku3 the potto8_tyogouryoukikaku3 to set
     */
    public void setPotto8_tyogouryoukikaku3(String potto8_tyogouryoukikaku3) {
        this.potto8_tyogouryoukikaku3 = potto8_tyogouryoukikaku3;
    }

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.3_1
     * @return the potto8_sizailotno3_1
     */
    public String getPotto8_sizailotno3_1() {
        return potto8_sizailotno3_1;
    }

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.3_1
     * @param potto8_sizailotno3_1 the potto8_sizailotno3_1 to set
     */
    public void setPotto8_sizailotno3_1(String potto8_sizailotno3_1) {
        this.potto8_sizailotno3_1 = potto8_sizailotno3_1;
    }

    /**
     * ﾎﾟｯﾄ8_調合量3_1
     * @return the potto8_tyougouryou3_1
     */
    public Integer getPotto8_tyougouryou3_1() {
        return potto8_tyougouryou3_1;
    }

    /**
     * ﾎﾟｯﾄ8_調合量3_1
     * @param potto8_tyougouryou3_1 the potto8_tyougouryou3_1 to set
     */
    public void setPotto8_tyougouryou3_1(Integer potto8_tyougouryou3_1) {
        this.potto8_tyougouryou3_1 = potto8_tyougouryou3_1;
    }

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.3_2
     * @return the potto8_sizailotno3_2
     */
    public String getPotto8_sizailotno3_2() {
        return potto8_sizailotno3_2;
    }

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.3_2
     * @param potto8_sizailotno3_2 the potto8_sizailotno3_2 to set
     */
    public void setPotto8_sizailotno3_2(String potto8_sizailotno3_2) {
        this.potto8_sizailotno3_2 = potto8_sizailotno3_2;
    }

    /**
     * ﾎﾟｯﾄ8_調合量3_2
     * @return the potto8_tyougouryou3_2
     */
    public Integer getPotto8_tyougouryou3_2() {
        return potto8_tyougouryou3_2;
    }

    /**
     * ﾎﾟｯﾄ8_調合量3_2
     * @param potto8_tyougouryou3_2 the potto8_tyougouryou3_2 to set
     */
    public void setPotto8_tyougouryou3_2(Integer potto8_tyougouryou3_2) {
        this.potto8_tyougouryou3_2 = potto8_tyougouryou3_2;
    }

    /**
     * ﾎﾟｯﾄ8_材料品名4
     * @return the potto8_zairyohinmei4
     */
    public String getPotto8_zairyohinmei4() {
        return potto8_zairyohinmei4;
    }

    /**
     * ﾎﾟｯﾄ8_材料品名4
     * @param potto8_zairyohinmei4 the potto8_zairyohinmei4 to set
     */
    public void setPotto8_zairyohinmei4(String potto8_zairyohinmei4) {
        this.potto8_zairyohinmei4 = potto8_zairyohinmei4;
    }

    /**
     * ﾎﾟｯﾄ8_調合量規格4
     * @return the potto8_tyogouryoukikaku4
     */
    public String getPotto8_tyogouryoukikaku4() {
        return potto8_tyogouryoukikaku4;
    }

    /**
     * ﾎﾟｯﾄ8_調合量規格4
     * @param potto8_tyogouryoukikaku4 the potto8_tyogouryoukikaku4 to set
     */
    public void setPotto8_tyogouryoukikaku4(String potto8_tyogouryoukikaku4) {
        this.potto8_tyogouryoukikaku4 = potto8_tyogouryoukikaku4;
    }

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.4_1
     * @return the potto8_sizailotno4_1
     */
    public String getPotto8_sizailotno4_1() {
        return potto8_sizailotno4_1;
    }

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.4_1
     * @param potto8_sizailotno4_1 the potto8_sizailotno4_1 to set
     */
    public void setPotto8_sizailotno4_1(String potto8_sizailotno4_1) {
        this.potto8_sizailotno4_1 = potto8_sizailotno4_1;
    }

    /**
     * ﾎﾟｯﾄ8_調合量4_1
     * @return the potto8_tyougouryou4_1
     */
    public Integer getPotto8_tyougouryou4_1() {
        return potto8_tyougouryou4_1;
    }

    /**
     * ﾎﾟｯﾄ8_調合量4_1
     * @param potto8_tyougouryou4_1 the potto8_tyougouryou4_1 to set
     */
    public void setPotto8_tyougouryou4_1(Integer potto8_tyougouryou4_1) {
        this.potto8_tyougouryou4_1 = potto8_tyougouryou4_1;
    }

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.4_2
     * @return the potto8_sizailotno4_2
     */
    public String getPotto8_sizailotno4_2() {
        return potto8_sizailotno4_2;
    }

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.4_2
     * @param potto8_sizailotno4_2 the potto8_sizailotno4_2 to set
     */
    public void setPotto8_sizailotno4_2(String potto8_sizailotno4_2) {
        this.potto8_sizailotno4_2 = potto8_sizailotno4_2;
    }

    /**
     * ﾎﾟｯﾄ8_調合量4_2
     * @return the potto8_tyougouryou4_2
     */
    public Integer getPotto8_tyougouryou4_2() {
        return potto8_tyougouryou4_2;
    }

    /**
     * ﾎﾟｯﾄ8_調合量4_2
     * @param potto8_tyougouryou4_2 the potto8_tyougouryou4_2 to set
     */
    public void setPotto8_tyougouryou4_2(Integer potto8_tyougouryou4_2) {
        this.potto8_tyougouryou4_2 = potto8_tyougouryou4_2;
    }

    /**
     * 秤量日時
     * @return the hyouryounichiji
     */
    public Timestamp getHyouryounichiji() {
        return hyouryounichiji;
    }

    /**
     * 秤量日時
     * @param hyouryounichiji the hyouryounichiji to set
     */
    public void setHyouryounichiji(Timestamp hyouryounichiji) {
        this.hyouryounichiji = hyouryounichiji;
    }

    /**
     * 担当者
     * @return the tantousya
     */
    public String getTantousya() {
        return tantousya;
    }

    /**
     * 担当者
     * @param tantousya the tantousya to set
     */
    public void setTantousya(String tantousya) {
        this.tantousya = tantousya;
    }

    /**
     * 確認者
     * @return the kakuninsya
     */
    public String getKakuninsya() {
        return kakuninsya;
    }

    /**
     * 確認者
     * @param kakuninsya the kakuninsya to set
     */
    public void setKakuninsya(String kakuninsya) {
        this.kakuninsya = kakuninsya;
    }

    /**
     * 備考1
     * @return the bikou1
     */
    public String getBikou1() {
        return bikou1;
    }

    /**
     * 備考1
     * @param bikou1 the bikou1 to set
     */
    public void setBikou1(String bikou1) {
        this.bikou1 = bikou1;
    }

    /**
     * 備考2
     * @return the bikou2
     */
    public String getBikou2() {
        return bikou2;
    }

    /**
     * 備考2
     * @param bikou2 the bikou2 to set
     */
    public void setBikou2(String bikou2) {
        this.bikou2 = bikou2;
    }

    /**
     * 登録日時
     * @return the torokunichiji
     */
    public Timestamp getTorokunichiji() {
        return torokunichiji;
    }

    /**
     * 登録日時
     * @param torokunichiji the torokunichiji to set
     */
    public void setTorokunichiji(Timestamp torokunichiji) {
        this.torokunichiji = torokunichiji;
    }

    /**
     * 更新日時
     * @return the kosinnichiji
     */
    public Timestamp getKosinnichiji() {
        return kosinnichiji;
    }

    /**
     * 更新日時
     * @param kosinnichiji the kosinnichiji to set
     */
    public void setKosinnichiji(Timestamp kosinnichiji) {
        this.kosinnichiji = kosinnichiji;
    }

    /**
     * revision
     * @return the revision
     */
    public Integer getRevision() {
        return revision;
    }

    /**
     * revision
     * @param revision the revision to set
     */
    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    /**
     * 削除ﾌﾗｸﾞ
     * @return the deleteflag
     */
    public Integer getDeleteflag() {
        return deleteflag;
    }

    /**
     * 削除ﾌﾗｸﾞ
     * @param deleteflag the deleteflag to set
     */
    public void setDeleteflag(Integer deleteflag) {
        this.deleteflag = deleteflag;
    }

}