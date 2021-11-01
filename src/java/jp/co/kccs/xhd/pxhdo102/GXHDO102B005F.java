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
 * GXHDO102B005F(ｶﾞﾗｽｽﾗﾘｰ作製・秤量)
 *
 * @author KCSS K.Jo
 * @since  2021/09/08
 */
@ViewScoped
@Named("beanGXHDO102C005F")
public class GXHDO102B005F implements Serializable {

    /**
     * ﾎﾟｯﾄ5_材料品名1
     */
    private FXHDD01 potto5_zairyohinmei1;

    /**
     * ﾎﾟｯﾄ5_調合量規格1
     */
    private FXHDD01 potto5_tyogouryoukikaku1;

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.1_1
     */
    private FXHDD01 potto5_sizailotno1_1;

    /**
     * ﾎﾟｯﾄ5_調合量1_1
     */
    private FXHDD01 potto5_tyougouryou1_1;

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.1_2
     */
    private FXHDD01 potto5_sizailotno1_2;

    /**
     * ﾎﾟｯﾄ5_調合量1_2
     */
    private FXHDD01 potto5_tyougouryou1_2;

    /**
     * ﾎﾟｯﾄ5_材料品名2
     */
    private FXHDD01 potto5_zairyohinmei2;

    /**
     * ﾎﾟｯﾄ5_調合量規格2
     */
    private FXHDD01 potto5_tyogouryoukikaku2;

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.2_1
     */
    private FXHDD01 potto5_sizailotno2_1;

    /**
     * ﾎﾟｯﾄ5_調合量2_1
     */
    private FXHDD01 potto5_tyougouryou2_1;

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.2_2
     */
    private FXHDD01 potto5_sizailotno2_2;

    /**
     * ﾎﾟｯﾄ5_調合量2_2
     */
    private FXHDD01 potto5_tyougouryou2_2;

    /**
     * ﾎﾟｯﾄ5_材料品名3
     */
    private FXHDD01 potto5_zairyohinmei3;

    /**
     * ﾎﾟｯﾄ5_調合量規格3
     */
    private FXHDD01 potto5_tyogouryoukikaku3;

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.3_1
     */
    private FXHDD01 potto5_sizailotno3_1;

    /**
     * ﾎﾟｯﾄ5_調合量3_1
     */
    private FXHDD01 potto5_tyougouryou3_1;

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.3_2
     */
    private FXHDD01 potto5_sizailotno3_2;

    /**
     * ﾎﾟｯﾄ5_調合量3_2
     */
    private FXHDD01 potto5_tyougouryou3_2;

    /**
     * ﾎﾟｯﾄ5_材料品名4
     */
    private FXHDD01 potto5_zairyohinmei4;

    /**
     * ﾎﾟｯﾄ5_調合量規格4
     */
    private FXHDD01 potto5_tyogouryoukikaku4;

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.4_1
     */
    private FXHDD01 potto5_sizailotno4_1;

    /**
     * ﾎﾟｯﾄ5_調合量4_1
     */
    private FXHDD01 potto5_tyougouryou4_1;

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.4_2
     */
    private FXHDD01 potto5_sizailotno4_2;

    /**
     * ﾎﾟｯﾄ5_調合量4_2
     */
    private FXHDD01 potto5_tyougouryou4_2;

    /**
     * コンストラクタ
     */
    public GXHDO102B005F() {
    }

    /**
     * ﾎﾟｯﾄ5_材料品名1
     * @return the potto5_zairyohinmei1
     */
    public FXHDD01 getPotto5_zairyohinmei1() {
        return potto5_zairyohinmei1;
    }

    /**
     * ﾎﾟｯﾄ5_材料品名1
     * @param potto5_zairyohinmei1 the potto5_zairyohinmei1 to set
     */
    public void setPotto5_zairyohinmei1(FXHDD01 potto5_zairyohinmei1) {
        this.potto5_zairyohinmei1 = potto5_zairyohinmei1;
    }

    /**
     * ﾎﾟｯﾄ5_調合量規格1
     * @return the potto5_tyogouryoukikaku1
     */
    public FXHDD01 getPotto5_tyogouryoukikaku1() {
        return potto5_tyogouryoukikaku1;
    }

    /**
     * ﾎﾟｯﾄ5_調合量規格1
     * @param potto5_tyogouryoukikaku1 the potto5_tyogouryoukikaku1 to set
     */
    public void setPotto5_tyogouryoukikaku1(FXHDD01 potto5_tyogouryoukikaku1) {
        this.potto5_tyogouryoukikaku1 = potto5_tyogouryoukikaku1;
    }

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.1_1
     * @return the potto5_sizailotno1_1
     */
    public FXHDD01 getPotto5_sizailotno1_1() {
        return potto5_sizailotno1_1;
    }

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.1_1
     * @param potto5_sizailotno1_1 the potto5_sizailotno1_1 to set
     */
    public void setPotto5_sizailotno1_1(FXHDD01 potto5_sizailotno1_1) {
        this.potto5_sizailotno1_1 = potto5_sizailotno1_1;
    }

    /**
     * ﾎﾟｯﾄ5_調合量1_1
     * @return the potto5_tyougouryou1_1
     */
    public FXHDD01 getPotto5_tyougouryou1_1() {
        return potto5_tyougouryou1_1;
    }

    /**
     * ﾎﾟｯﾄ5_調合量1_1
     * @param potto5_tyougouryou1_1 the potto5_tyougouryou1_1 to set
     */
    public void setPotto5_tyougouryou1_1(FXHDD01 potto5_tyougouryou1_1) {
        this.potto5_tyougouryou1_1 = potto5_tyougouryou1_1;
    }

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.1_2
     * @return the potto5_sizailotno1_2
     */
    public FXHDD01 getPotto5_sizailotno1_2() {
        return potto5_sizailotno1_2;
    }

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.1_2
     * @param potto5_sizailotno1_2 the potto5_sizailotno1_2 to set
     */
    public void setPotto5_sizailotno1_2(FXHDD01 potto5_sizailotno1_2) {
        this.potto5_sizailotno1_2 = potto5_sizailotno1_2;
    }

    /**
     * ﾎﾟｯﾄ5_調合量1_2
     * @return the potto5_tyougouryou1_2
     */
    public FXHDD01 getPotto5_tyougouryou1_2() {
        return potto5_tyougouryou1_2;
    }

    /**
     * ﾎﾟｯﾄ5_調合量1_2
     * @param potto5_tyougouryou1_2 the potto5_tyougouryou1_2 to set
     */
    public void setPotto5_tyougouryou1_2(FXHDD01 potto5_tyougouryou1_2) {
        this.potto5_tyougouryou1_2 = potto5_tyougouryou1_2;
    }

    /**
     * ﾎﾟｯﾄ5_材料品名2
     * @return the potto5_zairyohinmei2
     */
    public FXHDD01 getPotto5_zairyohinmei2() {
        return potto5_zairyohinmei2;
    }

    /**
     * ﾎﾟｯﾄ5_材料品名2
     * @param potto5_zairyohinmei2 the potto5_zairyohinmei2 to set
     */
    public void setPotto5_zairyohinmei2(FXHDD01 potto5_zairyohinmei2) {
        this.potto5_zairyohinmei2 = potto5_zairyohinmei2;
    }

    /**
     * ﾎﾟｯﾄ5_調合量規格2
     * @return the potto5_tyogouryoukikaku2
     */
    public FXHDD01 getPotto5_tyogouryoukikaku2() {
        return potto5_tyogouryoukikaku2;
    }

    /**
     * ﾎﾟｯﾄ5_調合量規格2
     * @param potto5_tyogouryoukikaku2 the potto5_tyogouryoukikaku2 to set
     */
    public void setPotto5_tyogouryoukikaku2(FXHDD01 potto5_tyogouryoukikaku2) {
        this.potto5_tyogouryoukikaku2 = potto5_tyogouryoukikaku2;
    }

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.2_1
     * @return the potto5_sizailotno2_1
     */
    public FXHDD01 getPotto5_sizailotno2_1() {
        return potto5_sizailotno2_1;
    }

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.2_1
     * @param potto5_sizailotno2_1 the potto5_sizailotno2_1 to set
     */
    public void setPotto5_sizailotno2_1(FXHDD01 potto5_sizailotno2_1) {
        this.potto5_sizailotno2_1 = potto5_sizailotno2_1;
    }

    /**
     * ﾎﾟｯﾄ5_調合量2_1
     * @return the potto5_tyougouryou2_1
     */
    public FXHDD01 getPotto5_tyougouryou2_1() {
        return potto5_tyougouryou2_1;
    }

    /**
     * ﾎﾟｯﾄ5_調合量2_1
     * @param potto5_tyougouryou2_1 the potto5_tyougouryou2_1 to set
     */
    public void setPotto5_tyougouryou2_1(FXHDD01 potto5_tyougouryou2_1) {
        this.potto5_tyougouryou2_1 = potto5_tyougouryou2_1;
    }

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.2_2
     * @return the potto5_sizailotno2_2
     */
    public FXHDD01 getPotto5_sizailotno2_2() {
        return potto5_sizailotno2_2;
    }

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.2_2
     * @param potto5_sizailotno2_2 the potto5_sizailotno2_2 to set
     */
    public void setPotto5_sizailotno2_2(FXHDD01 potto5_sizailotno2_2) {
        this.potto5_sizailotno2_2 = potto5_sizailotno2_2;
    }

    /**
     * ﾎﾟｯﾄ5_調合量2_2
     * @return the potto5_tyougouryou2_2
     */
    public FXHDD01 getPotto5_tyougouryou2_2() {
        return potto5_tyougouryou2_2;
    }

    /**
     * ﾎﾟｯﾄ5_調合量2_2
     * @param potto5_tyougouryou2_2 the potto5_tyougouryou2_2 to set
     */
    public void setPotto5_tyougouryou2_2(FXHDD01 potto5_tyougouryou2_2) {
        this.potto5_tyougouryou2_2 = potto5_tyougouryou2_2;
    }

    /**
     * ﾎﾟｯﾄ5_材料品名3
     * @return the potto5_zairyohinmei3
     */
    public FXHDD01 getPotto5_zairyohinmei3() {
        return potto5_zairyohinmei3;
    }

    /**
     * ﾎﾟｯﾄ5_材料品名3
     * @param potto5_zairyohinmei3 the potto5_zairyohinmei3 to set
     */
    public void setPotto5_zairyohinmei3(FXHDD01 potto5_zairyohinmei3) {
        this.potto5_zairyohinmei3 = potto5_zairyohinmei3;
    }

    /**
     * ﾎﾟｯﾄ5_調合量規格3
     * @return the potto5_tyogouryoukikaku3
     */
    public FXHDD01 getPotto5_tyogouryoukikaku3() {
        return potto5_tyogouryoukikaku3;
    }

    /**
     * ﾎﾟｯﾄ5_調合量規格3
     * @param potto5_tyogouryoukikaku3 the potto5_tyogouryoukikaku3 to set
     */
    public void setPotto5_tyogouryoukikaku3(FXHDD01 potto5_tyogouryoukikaku3) {
        this.potto5_tyogouryoukikaku3 = potto5_tyogouryoukikaku3;
    }

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.3_1
     * @return the potto5_sizailotno3_1
     */
    public FXHDD01 getPotto5_sizailotno3_1() {
        return potto5_sizailotno3_1;
    }

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.3_1
     * @param potto5_sizailotno3_1 the potto5_sizailotno3_1 to set
     */
    public void setPotto5_sizailotno3_1(FXHDD01 potto5_sizailotno3_1) {
        this.potto5_sizailotno3_1 = potto5_sizailotno3_1;
    }

    /**
     * ﾎﾟｯﾄ5_調合量3_1
     * @return the potto5_tyougouryou3_1
     */
    public FXHDD01 getPotto5_tyougouryou3_1() {
        return potto5_tyougouryou3_1;
    }

    /**
     * ﾎﾟｯﾄ5_調合量3_1
     * @param potto5_tyougouryou3_1 the potto5_tyougouryou3_1 to set
     */
    public void setPotto5_tyougouryou3_1(FXHDD01 potto5_tyougouryou3_1) {
        this.potto5_tyougouryou3_1 = potto5_tyougouryou3_1;
    }

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.3_2
     * @return the potto5_sizailotno3_2
     */
    public FXHDD01 getPotto5_sizailotno3_2() {
        return potto5_sizailotno3_2;
    }

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.3_2
     * @param potto5_sizailotno3_2 the potto5_sizailotno3_2 to set
     */
    public void setPotto5_sizailotno3_2(FXHDD01 potto5_sizailotno3_2) {
        this.potto5_sizailotno3_2 = potto5_sizailotno3_2;
    }

    /**
     * ﾎﾟｯﾄ5_調合量3_2
     * @return the potto5_tyougouryou3_2
     */
    public FXHDD01 getPotto5_tyougouryou3_2() {
        return potto5_tyougouryou3_2;
    }

    /**
     * ﾎﾟｯﾄ5_調合量3_2
     * @param potto5_tyougouryou3_2 the potto5_tyougouryou3_2 to set
     */
    public void setPotto5_tyougouryou3_2(FXHDD01 potto5_tyougouryou3_2) {
        this.potto5_tyougouryou3_2 = potto5_tyougouryou3_2;
    }

    /**
     * ﾎﾟｯﾄ5_材料品名4
     * @return the potto5_zairyohinmei4
     */
    public FXHDD01 getPotto5_zairyohinmei4() {
        return potto5_zairyohinmei4;
    }

    /**
     * ﾎﾟｯﾄ5_材料品名4
     * @param potto5_zairyohinmei4 the potto5_zairyohinmei4 to set
     */
    public void setPotto5_zairyohinmei4(FXHDD01 potto5_zairyohinmei4) {
        this.potto5_zairyohinmei4 = potto5_zairyohinmei4;
    }

    /**
     * ﾎﾟｯﾄ5_調合量規格4
     * @return the potto5_tyogouryoukikaku4
     */
    public FXHDD01 getPotto5_tyogouryoukikaku4() {
        return potto5_tyogouryoukikaku4;
    }

    /**
     * ﾎﾟｯﾄ5_調合量規格4
     * @param potto5_tyogouryoukikaku4 the potto5_tyogouryoukikaku4 to set
     */
    public void setPotto5_tyogouryoukikaku4(FXHDD01 potto5_tyogouryoukikaku4) {
        this.potto5_tyogouryoukikaku4 = potto5_tyogouryoukikaku4;
    }

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.4_1
     * @return the potto5_sizailotno4_1
     */
    public FXHDD01 getPotto5_sizailotno4_1() {
        return potto5_sizailotno4_1;
    }

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.4_1
     * @param potto5_sizailotno4_1 the potto5_sizailotno4_1 to set
     */
    public void setPotto5_sizailotno4_1(FXHDD01 potto5_sizailotno4_1) {
        this.potto5_sizailotno4_1 = potto5_sizailotno4_1;
    }

    /**
     * ﾎﾟｯﾄ5_調合量4_1
     * @return the potto5_tyougouryou4_1
     */
    public FXHDD01 getPotto5_tyougouryou4_1() {
        return potto5_tyougouryou4_1;
    }

    /**
     * ﾎﾟｯﾄ5_調合量4_1
     * @param potto5_tyougouryou4_1 the potto5_tyougouryou4_1 to set
     */
    public void setPotto5_tyougouryou4_1(FXHDD01 potto5_tyougouryou4_1) {
        this.potto5_tyougouryou4_1 = potto5_tyougouryou4_1;
    }

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.4_2
     * @return the potto5_sizailotno4_2
     */
    public FXHDD01 getPotto5_sizailotno4_2() {
        return potto5_sizailotno4_2;
    }

    /**
     * ﾎﾟｯﾄ5_資材ﾛｯﾄNo.4_2
     * @param potto5_sizailotno4_2 the potto5_sizailotno4_2 to set
     */
    public void setPotto5_sizailotno4_2(FXHDD01 potto5_sizailotno4_2) {
        this.potto5_sizailotno4_2 = potto5_sizailotno4_2;
    }

    /**
     * ﾎﾟｯﾄ5_調合量4_2
     * @return the potto5_tyougouryou4_2
     */
    public FXHDD01 getPotto5_tyougouryou4_2() {
        return potto5_tyougouryou4_2;
    }

    /**
     * ﾎﾟｯﾄ5_調合量4_2
     * @param potto5_tyougouryou4_2 the potto5_tyougouryou4_2 to set
     */
    public void setPotto5_tyougouryou4_2(FXHDD01 potto5_tyougouryou4_2) {
        this.potto5_tyougouryou4_2 = potto5_tyougouryou4_2;
    }

}
