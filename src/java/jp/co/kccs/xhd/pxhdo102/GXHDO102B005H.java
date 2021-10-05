/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo102;

import java.io.Serializable;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import jp.co.kccs.xhd.db.model.FXHDD01;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/09/08<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B005H(ｶﾞﾗｽｽﾗﾘｰ作製・秤量)
 *
 * @author KCSS K.Jo
 * @since  2021/09/08
 */
@ViewScoped
@Named("beanGXHDO102C005H")
public class GXHDO102B005H implements Serializable {

    /**
     * ﾎﾟｯﾄ7_材料品名1
     */
    private FXHDD01 potto7_zairyohinmei1;

    /**
     * ﾎﾟｯﾄ7_調合量規格1
     */
    private FXHDD01 potto7_tyogouryoukikaku1;

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.1_1
     */
    private FXHDD01 potto7_sizailotno1_1;

    /**
     * ﾎﾟｯﾄ7_調合量1_1
     */
    private FXHDD01 potto7_tyougouryou1_1;

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.1_2
     */
    private FXHDD01 potto7_sizailotno1_2;

    /**
     * ﾎﾟｯﾄ7_調合量1_2
     */
    private FXHDD01 potto7_tyougouryou1_2;

    /**
     * ﾎﾟｯﾄ7_材料品名2
     */
    private FXHDD01 potto7_zairyohinmei2;

    /**
     * ﾎﾟｯﾄ7_調合量規格2
     */
    private FXHDD01 potto7_tyogouryoukikaku2;

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.2_1
     */
    private FXHDD01 potto7_sizailotno2_1;

    /**
     * ﾎﾟｯﾄ7_調合量2_1
     */
    private FXHDD01 potto7_tyougouryou2_1;

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.2_2
     */
    private FXHDD01 potto7_sizailotno2_2;

    /**
     * ﾎﾟｯﾄ7_調合量2_2
     */
    private FXHDD01 potto7_tyougouryou2_2;

    /**
     * ﾎﾟｯﾄ7_材料品名3
     */
    private FXHDD01 potto7_zairyohinmei3;

    /**
     * ﾎﾟｯﾄ7_調合量規格3
     */
    private FXHDD01 potto7_tyogouryoukikaku3;

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.3_1
     */
    private FXHDD01 potto7_sizailotno3_1;

    /**
     * ﾎﾟｯﾄ7_調合量3_1
     */
    private FXHDD01 potto7_tyougouryou3_1;

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.3_2
     */
    private FXHDD01 potto7_sizailotno3_2;

    /**
     * ﾎﾟｯﾄ7_調合量3_2
     */
    private FXHDD01 potto7_tyougouryou3_2;

    /**
     * ﾎﾟｯﾄ7_材料品名4
     */
    private FXHDD01 potto7_zairyohinmei4;

    /**
     * ﾎﾟｯﾄ7_調合量規格4
     */
    private FXHDD01 potto7_tyogouryoukikaku4;

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.4_1
     */
    private FXHDD01 potto7_sizailotno4_1;

    /**
     * ﾎﾟｯﾄ7_調合量4_1
     */
    private FXHDD01 potto7_tyougouryou4_1;

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.4_2
     */
    private FXHDD01 potto7_sizailotno4_2;

    /**
     * ﾎﾟｯﾄ7_調合量4_2
     */
    private FXHDD01 potto7_tyougouryou4_2;

    /**
     * コンストラクタ
     */
    public GXHDO102B005H() {
    }

    /**
     * ﾎﾟｯﾄ7_材料品名1
     * @return the potto7_zairyohinmei1
     */
    public FXHDD01 getPotto7_zairyohinmei1() {
        return potto7_zairyohinmei1;
    }

    /**
     * ﾎﾟｯﾄ7_材料品名1
     * @param potto7_zairyohinmei1 the potto7_zairyohinmei1 to set
     */
    public void setPotto7_zairyohinmei1(FXHDD01 potto7_zairyohinmei1) {
        this.potto7_zairyohinmei1 = potto7_zairyohinmei1;
    }

    /**
     * ﾎﾟｯﾄ7_調合量規格1
     * @return the potto7_tyogouryoukikaku1
     */
    public FXHDD01 getPotto7_tyogouryoukikaku1() {
        return potto7_tyogouryoukikaku1;
    }

    /**
     * ﾎﾟｯﾄ7_調合量規格1
     * @param potto7_tyogouryoukikaku1 the potto7_tyogouryoukikaku1 to set
     */
    public void setPotto7_tyogouryoukikaku1(FXHDD01 potto7_tyogouryoukikaku1) {
        this.potto7_tyogouryoukikaku1 = potto7_tyogouryoukikaku1;
    }

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.1_1
     * @return the potto7_sizailotno1_1
     */
    public FXHDD01 getPotto7_sizailotno1_1() {
        return potto7_sizailotno1_1;
    }

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.1_1
     * @param potto7_sizailotno1_1 the potto7_sizailotno1_1 to set
     */
    public void setPotto7_sizailotno1_1(FXHDD01 potto7_sizailotno1_1) {
        this.potto7_sizailotno1_1 = potto7_sizailotno1_1;
    }

    /**
     * ﾎﾟｯﾄ7_調合量1_1
     * @return the potto7_tyougouryou1_1
     */
    public FXHDD01 getPotto7_tyougouryou1_1() {
        return potto7_tyougouryou1_1;
    }

    /**
     * ﾎﾟｯﾄ7_調合量1_1
     * @param potto7_tyougouryou1_1 the potto7_tyougouryou1_1 to set
     */
    public void setPotto7_tyougouryou1_1(FXHDD01 potto7_tyougouryou1_1) {
        this.potto7_tyougouryou1_1 = potto7_tyougouryou1_1;
    }

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.1_2
     * @return the potto7_sizailotno1_2
     */
    public FXHDD01 getPotto7_sizailotno1_2() {
        return potto7_sizailotno1_2;
    }

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.1_2
     * @param potto7_sizailotno1_2 the potto7_sizailotno1_2 to set
     */
    public void setPotto7_sizailotno1_2(FXHDD01 potto7_sizailotno1_2) {
        this.potto7_sizailotno1_2 = potto7_sizailotno1_2;
    }

    /**
     * ﾎﾟｯﾄ7_調合量1_2
     * @return the potto7_tyougouryou1_2
     */
    public FXHDD01 getPotto7_tyougouryou1_2() {
        return potto7_tyougouryou1_2;
    }

    /**
     * ﾎﾟｯﾄ7_調合量1_2
     * @param potto7_tyougouryou1_2 the potto7_tyougouryou1_2 to set
     */
    public void setPotto7_tyougouryou1_2(FXHDD01 potto7_tyougouryou1_2) {
        this.potto7_tyougouryou1_2 = potto7_tyougouryou1_2;
    }

    /**
     * ﾎﾟｯﾄ7_材料品名2
     * @return the potto7_zairyohinmei2
     */
    public FXHDD01 getPotto7_zairyohinmei2() {
        return potto7_zairyohinmei2;
    }

    /**
     * ﾎﾟｯﾄ7_材料品名2
     * @param potto7_zairyohinmei2 the potto7_zairyohinmei2 to set
     */
    public void setPotto7_zairyohinmei2(FXHDD01 potto7_zairyohinmei2) {
        this.potto7_zairyohinmei2 = potto7_zairyohinmei2;
    }

    /**
     * ﾎﾟｯﾄ7_調合量規格2
     * @return the potto7_tyogouryoukikaku2
     */
    public FXHDD01 getPotto7_tyogouryoukikaku2() {
        return potto7_tyogouryoukikaku2;
    }

    /**
     * ﾎﾟｯﾄ7_調合量規格2
     * @param potto7_tyogouryoukikaku2 the potto7_tyogouryoukikaku2 to set
     */
    public void setPotto7_tyogouryoukikaku2(FXHDD01 potto7_tyogouryoukikaku2) {
        this.potto7_tyogouryoukikaku2 = potto7_tyogouryoukikaku2;
    }

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.2_1
     * @return the potto7_sizailotno2_1
     */
    public FXHDD01 getPotto7_sizailotno2_1() {
        return potto7_sizailotno2_1;
    }

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.2_1
     * @param potto7_sizailotno2_1 the potto7_sizailotno2_1 to set
     */
    public void setPotto7_sizailotno2_1(FXHDD01 potto7_sizailotno2_1) {
        this.potto7_sizailotno2_1 = potto7_sizailotno2_1;
    }

    /**
     * ﾎﾟｯﾄ7_調合量2_1
     * @return the potto7_tyougouryou2_1
     */
    public FXHDD01 getPotto7_tyougouryou2_1() {
        return potto7_tyougouryou2_1;
    }

    /**
     * ﾎﾟｯﾄ7_調合量2_1
     * @param potto7_tyougouryou2_1 the potto7_tyougouryou2_1 to set
     */
    public void setPotto7_tyougouryou2_1(FXHDD01 potto7_tyougouryou2_1) {
        this.potto7_tyougouryou2_1 = potto7_tyougouryou2_1;
    }

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.2_2
     * @return the potto7_sizailotno2_2
     */
    public FXHDD01 getPotto7_sizailotno2_2() {
        return potto7_sizailotno2_2;
    }

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.2_2
     * @param potto7_sizailotno2_2 the potto7_sizailotno2_2 to set
     */
    public void setPotto7_sizailotno2_2(FXHDD01 potto7_sizailotno2_2) {
        this.potto7_sizailotno2_2 = potto7_sizailotno2_2;
    }

    /**
     * ﾎﾟｯﾄ7_調合量2_2
     * @return the potto7_tyougouryou2_2
     */
    public FXHDD01 getPotto7_tyougouryou2_2() {
        return potto7_tyougouryou2_2;
    }

    /**
     * ﾎﾟｯﾄ7_調合量2_2
     * @param potto7_tyougouryou2_2 the potto7_tyougouryou2_2 to set
     */
    public void setPotto7_tyougouryou2_2(FXHDD01 potto7_tyougouryou2_2) {
        this.potto7_tyougouryou2_2 = potto7_tyougouryou2_2;
    }

    /**
     * ﾎﾟｯﾄ7_材料品名3
     * @return the potto7_zairyohinmei3
     */
    public FXHDD01 getPotto7_zairyohinmei3() {
        return potto7_zairyohinmei3;
    }

    /**
     * ﾎﾟｯﾄ7_材料品名3
     * @param potto7_zairyohinmei3 the potto7_zairyohinmei3 to set
     */
    public void setPotto7_zairyohinmei3(FXHDD01 potto7_zairyohinmei3) {
        this.potto7_zairyohinmei3 = potto7_zairyohinmei3;
    }

    /**
     * ﾎﾟｯﾄ7_調合量規格3
     * @return the potto7_tyogouryoukikaku3
     */
    public FXHDD01 getPotto7_tyogouryoukikaku3() {
        return potto7_tyogouryoukikaku3;
    }

    /**
     * ﾎﾟｯﾄ7_調合量規格3
     * @param potto7_tyogouryoukikaku3 the potto7_tyogouryoukikaku3 to set
     */
    public void setPotto7_tyogouryoukikaku3(FXHDD01 potto7_tyogouryoukikaku3) {
        this.potto7_tyogouryoukikaku3 = potto7_tyogouryoukikaku3;
    }

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.3_1
     * @return the potto7_sizailotno3_1
     */
    public FXHDD01 getPotto7_sizailotno3_1() {
        return potto7_sizailotno3_1;
    }

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.3_1
     * @param potto7_sizailotno3_1 the potto7_sizailotno3_1 to set
     */
    public void setPotto7_sizailotno3_1(FXHDD01 potto7_sizailotno3_1) {
        this.potto7_sizailotno3_1 = potto7_sizailotno3_1;
    }

    /**
     * ﾎﾟｯﾄ7_調合量3_1
     * @return the potto7_tyougouryou3_1
     */
    public FXHDD01 getPotto7_tyougouryou3_1() {
        return potto7_tyougouryou3_1;
    }

    /**
     * ﾎﾟｯﾄ7_調合量3_1
     * @param potto7_tyougouryou3_1 the potto7_tyougouryou3_1 to set
     */
    public void setPotto7_tyougouryou3_1(FXHDD01 potto7_tyougouryou3_1) {
        this.potto7_tyougouryou3_1 = potto7_tyougouryou3_1;
    }

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.3_2
     * @return the potto7_sizailotno3_2
     */
    public FXHDD01 getPotto7_sizailotno3_2() {
        return potto7_sizailotno3_2;
    }

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.3_2
     * @param potto7_sizailotno3_2 the potto7_sizailotno3_2 to set
     */
    public void setPotto7_sizailotno3_2(FXHDD01 potto7_sizailotno3_2) {
        this.potto7_sizailotno3_2 = potto7_sizailotno3_2;
    }

    /**
     * ﾎﾟｯﾄ7_調合量3_2
     * @return the potto7_tyougouryou3_2
     */
    public FXHDD01 getPotto7_tyougouryou3_2() {
        return potto7_tyougouryou3_2;
    }

    /**
     * ﾎﾟｯﾄ7_調合量3_2
     * @param potto7_tyougouryou3_2 the potto7_tyougouryou3_2 to set
     */
    public void setPotto7_tyougouryou3_2(FXHDD01 potto7_tyougouryou3_2) {
        this.potto7_tyougouryou3_2 = potto7_tyougouryou3_2;
    }

    /**
     * ﾎﾟｯﾄ7_材料品名4
     * @return the potto7_zairyohinmei4
     */
    public FXHDD01 getPotto7_zairyohinmei4() {
        return potto7_zairyohinmei4;
    }

    /**
     * ﾎﾟｯﾄ7_材料品名4
     * @param potto7_zairyohinmei4 the potto7_zairyohinmei4 to set
     */
    public void setPotto7_zairyohinmei4(FXHDD01 potto7_zairyohinmei4) {
        this.potto7_zairyohinmei4 = potto7_zairyohinmei4;
    }

    /**
     * ﾎﾟｯﾄ7_調合量規格4
     * @return the potto7_tyogouryoukikaku4
     */
    public FXHDD01 getPotto7_tyogouryoukikaku4() {
        return potto7_tyogouryoukikaku4;
    }

    /**
     * ﾎﾟｯﾄ7_調合量規格4
     * @param potto7_tyogouryoukikaku4 the potto7_tyogouryoukikaku4 to set
     */
    public void setPotto7_tyogouryoukikaku4(FXHDD01 potto7_tyogouryoukikaku4) {
        this.potto7_tyogouryoukikaku4 = potto7_tyogouryoukikaku4;
    }

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.4_1
     * @return the potto7_sizailotno4_1
     */
    public FXHDD01 getPotto7_sizailotno4_1() {
        return potto7_sizailotno4_1;
    }

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.4_1
     * @param potto7_sizailotno4_1 the potto7_sizailotno4_1 to set
     */
    public void setPotto7_sizailotno4_1(FXHDD01 potto7_sizailotno4_1) {
        this.potto7_sizailotno4_1 = potto7_sizailotno4_1;
    }

    /**
     * ﾎﾟｯﾄ7_調合量4_1
     * @return the potto7_tyougouryou4_1
     */
    public FXHDD01 getPotto7_tyougouryou4_1() {
        return potto7_tyougouryou4_1;
    }

    /**
     * ﾎﾟｯﾄ7_調合量4_1
     * @param potto7_tyougouryou4_1 the potto7_tyougouryou4_1 to set
     */
    public void setPotto7_tyougouryou4_1(FXHDD01 potto7_tyougouryou4_1) {
        this.potto7_tyougouryou4_1 = potto7_tyougouryou4_1;
    }

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.4_2
     * @return the potto7_sizailotno4_2
     */
    public FXHDD01 getPotto7_sizailotno4_2() {
        return potto7_sizailotno4_2;
    }

    /**
     * ﾎﾟｯﾄ7_資材ﾛｯﾄNo.4_2
     * @param potto7_sizailotno4_2 the potto7_sizailotno4_2 to set
     */
    public void setPotto7_sizailotno4_2(FXHDD01 potto7_sizailotno4_2) {
        this.potto7_sizailotno4_2 = potto7_sizailotno4_2;
    }

    /**
     * ﾎﾟｯﾄ7_調合量4_2
     * @return the potto7_tyougouryou4_2
     */
    public FXHDD01 getPotto7_tyougouryou4_2() {
        return potto7_tyougouryou4_2;
    }

    /**
     * ﾎﾟｯﾄ7_調合量4_2
     * @param potto7_tyougouryou4_2 the potto7_tyougouryou4_2 to set
     */
    public void setPotto7_tyougouryou4_2(FXHDD01 potto7_tyougouryou4_2) {
        this.potto7_tyougouryou4_2 = potto7_tyougouryou4_2;
    }

}
