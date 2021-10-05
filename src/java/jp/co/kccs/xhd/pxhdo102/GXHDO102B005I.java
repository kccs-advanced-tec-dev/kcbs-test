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
 * GXHDO102B005I(ｶﾞﾗｽｽﾗﾘｰ作製・秤量)
 *
 * @author KCSS K.Jo
 * @since  2021/09/08
 */
@ViewScoped
@Named("beanGXHDO102C005I")
public class GXHDO102B005I implements Serializable {

    /**
     * ﾎﾟｯﾄ8_材料品名1
     */
    private FXHDD01 potto8_zairyohinmei1;

    /**
     * ﾎﾟｯﾄ8_調合量規格1
     */
    private FXHDD01 potto8_tyogouryoukikaku1;

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.1_1
     */
    private FXHDD01 potto8_sizailotno1_1;

    /**
     * ﾎﾟｯﾄ8_調合量1_1
     */
    private FXHDD01 potto8_tyougouryou1_1;

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.1_2
     */
    private FXHDD01 potto8_sizailotno1_2;

    /**
     * ﾎﾟｯﾄ8_調合量1_2
     */
    private FXHDD01 potto8_tyougouryou1_2;

    /**
     * ﾎﾟｯﾄ8_材料品名2
     */
    private FXHDD01 potto8_zairyohinmei2;

    /**
     * ﾎﾟｯﾄ8_調合量規格2
     */
    private FXHDD01 potto8_tyogouryoukikaku2;

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.2_1
     */
    private FXHDD01 potto8_sizailotno2_1;

    /**
     * ﾎﾟｯﾄ8_調合量2_1
     */
    private FXHDD01 potto8_tyougouryou2_1;

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.2_2
     */
    private FXHDD01 potto8_sizailotno2_2;

    /**
     * ﾎﾟｯﾄ8_調合量2_2
     */
    private FXHDD01 potto8_tyougouryou2_2;

    /**
     * ﾎﾟｯﾄ8_材料品名3
     */
    private FXHDD01 potto8_zairyohinmei3;

    /**
     * ﾎﾟｯﾄ8_調合量規格3
     */
    private FXHDD01 potto8_tyogouryoukikaku3;

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.3_1
     */
    private FXHDD01 potto8_sizailotno3_1;

    /**
     * ﾎﾟｯﾄ8_調合量3_1
     */
    private FXHDD01 potto8_tyougouryou3_1;

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.3_2
     */
    private FXHDD01 potto8_sizailotno3_2;

    /**
     * ﾎﾟｯﾄ8_調合量3_2
     */
    private FXHDD01 potto8_tyougouryou3_2;

    /**
     * ﾎﾟｯﾄ8_材料品名4
     */
    private FXHDD01 potto8_zairyohinmei4;

    /**
     * ﾎﾟｯﾄ8_調合量規格4
     */
    private FXHDD01 potto8_tyogouryoukikaku4;

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.4_1
     */
    private FXHDD01 potto8_sizailotno4_1;

    /**
     * ﾎﾟｯﾄ8_調合量4_1
     */
    private FXHDD01 potto8_tyougouryou4_1;

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.4_2
     */
    private FXHDD01 potto8_sizailotno4_2;

    /**
     * ﾎﾟｯﾄ8_調合量4_2
     */
    private FXHDD01 potto8_tyougouryou4_2;

    /**
     * コンストラクタ
     */
    public GXHDO102B005I() {
    }

    /**
     * ﾎﾟｯﾄ8_材料品名1
     * @return the potto8_zairyohinmei1
     */
    public FXHDD01 getPotto8_zairyohinmei1() {
        return potto8_zairyohinmei1;
    }

    /**
     * ﾎﾟｯﾄ8_材料品名1
     * @param potto8_zairyohinmei1 the potto8_zairyohinmei1 to set
     */
    public void setPotto8_zairyohinmei1(FXHDD01 potto8_zairyohinmei1) {
        this.potto8_zairyohinmei1 = potto8_zairyohinmei1;
    }

    /**
     * ﾎﾟｯﾄ8_調合量規格1
     * @return the potto8_tyogouryoukikaku1
     */
    public FXHDD01 getPotto8_tyogouryoukikaku1() {
        return potto8_tyogouryoukikaku1;
    }

    /**
     * ﾎﾟｯﾄ8_調合量規格1
     * @param potto8_tyogouryoukikaku1 the potto8_tyogouryoukikaku1 to set
     */
    public void setPotto8_tyogouryoukikaku1(FXHDD01 potto8_tyogouryoukikaku1) {
        this.potto8_tyogouryoukikaku1 = potto8_tyogouryoukikaku1;
    }

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.1_1
     * @return the potto8_sizailotno1_1
     */
    public FXHDD01 getPotto8_sizailotno1_1() {
        return potto8_sizailotno1_1;
    }

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.1_1
     * @param potto8_sizailotno1_1 the potto8_sizailotno1_1 to set
     */
    public void setPotto8_sizailotno1_1(FXHDD01 potto8_sizailotno1_1) {
        this.potto8_sizailotno1_1 = potto8_sizailotno1_1;
    }

    /**
     * ﾎﾟｯﾄ8_調合量1_1
     * @return the potto8_tyougouryou1_1
     */
    public FXHDD01 getPotto8_tyougouryou1_1() {
        return potto8_tyougouryou1_1;
    }

    /**
     * ﾎﾟｯﾄ8_調合量1_1
     * @param potto8_tyougouryou1_1 the potto8_tyougouryou1_1 to set
     */
    public void setPotto8_tyougouryou1_1(FXHDD01 potto8_tyougouryou1_1) {
        this.potto8_tyougouryou1_1 = potto8_tyougouryou1_1;
    }

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.1_2
     * @return the potto8_sizailotno1_2
     */
    public FXHDD01 getPotto8_sizailotno1_2() {
        return potto8_sizailotno1_2;
    }

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.1_2
     * @param potto8_sizailotno1_2 the potto8_sizailotno1_2 to set
     */
    public void setPotto8_sizailotno1_2(FXHDD01 potto8_sizailotno1_2) {
        this.potto8_sizailotno1_2 = potto8_sizailotno1_2;
    }

    /**
     * ﾎﾟｯﾄ8_調合量1_2
     * @return the potto8_tyougouryou1_2
     */
    public FXHDD01 getPotto8_tyougouryou1_2() {
        return potto8_tyougouryou1_2;
    }

    /**
     * ﾎﾟｯﾄ8_調合量1_2
     * @param potto8_tyougouryou1_2 the potto8_tyougouryou1_2 to set
     */
    public void setPotto8_tyougouryou1_2(FXHDD01 potto8_tyougouryou1_2) {
        this.potto8_tyougouryou1_2 = potto8_tyougouryou1_2;
    }

    /**
     * ﾎﾟｯﾄ8_材料品名2
     * @return the potto8_zairyohinmei2
     */
    public FXHDD01 getPotto8_zairyohinmei2() {
        return potto8_zairyohinmei2;
    }

    /**
     * ﾎﾟｯﾄ8_材料品名2
     * @param potto8_zairyohinmei2 the potto8_zairyohinmei2 to set
     */
    public void setPotto8_zairyohinmei2(FXHDD01 potto8_zairyohinmei2) {
        this.potto8_zairyohinmei2 = potto8_zairyohinmei2;
    }

    /**
     * ﾎﾟｯﾄ8_調合量規格2
     * @return the potto8_tyogouryoukikaku2
     */
    public FXHDD01 getPotto8_tyogouryoukikaku2() {
        return potto8_tyogouryoukikaku2;
    }

    /**
     * ﾎﾟｯﾄ8_調合量規格2
     * @param potto8_tyogouryoukikaku2 the potto8_tyogouryoukikaku2 to set
     */
    public void setPotto8_tyogouryoukikaku2(FXHDD01 potto8_tyogouryoukikaku2) {
        this.potto8_tyogouryoukikaku2 = potto8_tyogouryoukikaku2;
    }

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.2_1
     * @return the potto8_sizailotno2_1
     */
    public FXHDD01 getPotto8_sizailotno2_1() {
        return potto8_sizailotno2_1;
    }

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.2_1
     * @param potto8_sizailotno2_1 the potto8_sizailotno2_1 to set
     */
    public void setPotto8_sizailotno2_1(FXHDD01 potto8_sizailotno2_1) {
        this.potto8_sizailotno2_1 = potto8_sizailotno2_1;
    }

    /**
     * ﾎﾟｯﾄ8_調合量2_1
     * @return the potto8_tyougouryou2_1
     */
    public FXHDD01 getPotto8_tyougouryou2_1() {
        return potto8_tyougouryou2_1;
    }

    /**
     * ﾎﾟｯﾄ8_調合量2_1
     * @param potto8_tyougouryou2_1 the potto8_tyougouryou2_1 to set
     */
    public void setPotto8_tyougouryou2_1(FXHDD01 potto8_tyougouryou2_1) {
        this.potto8_tyougouryou2_1 = potto8_tyougouryou2_1;
    }

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.2_2
     * @return the potto8_sizailotno2_2
     */
    public FXHDD01 getPotto8_sizailotno2_2() {
        return potto8_sizailotno2_2;
    }

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.2_2
     * @param potto8_sizailotno2_2 the potto8_sizailotno2_2 to set
     */
    public void setPotto8_sizailotno2_2(FXHDD01 potto8_sizailotno2_2) {
        this.potto8_sizailotno2_2 = potto8_sizailotno2_2;
    }

    /**
     * ﾎﾟｯﾄ8_調合量2_2
     * @return the potto8_tyougouryou2_2
     */
    public FXHDD01 getPotto8_tyougouryou2_2() {
        return potto8_tyougouryou2_2;
    }

    /**
     * ﾎﾟｯﾄ8_調合量2_2
     * @param potto8_tyougouryou2_2 the potto8_tyougouryou2_2 to set
     */
    public void setPotto8_tyougouryou2_2(FXHDD01 potto8_tyougouryou2_2) {
        this.potto8_tyougouryou2_2 = potto8_tyougouryou2_2;
    }

    /**
     * ﾎﾟｯﾄ8_材料品名3
     * @return the potto8_zairyohinmei3
     */
    public FXHDD01 getPotto8_zairyohinmei3() {
        return potto8_zairyohinmei3;
    }

    /**
     * ﾎﾟｯﾄ8_材料品名3
     * @param potto8_zairyohinmei3 the potto8_zairyohinmei3 to set
     */
    public void setPotto8_zairyohinmei3(FXHDD01 potto8_zairyohinmei3) {
        this.potto8_zairyohinmei3 = potto8_zairyohinmei3;
    }

    /**
     * ﾎﾟｯﾄ8_調合量規格3
     * @return the potto8_tyogouryoukikaku3
     */
    public FXHDD01 getPotto8_tyogouryoukikaku3() {
        return potto8_tyogouryoukikaku3;
    }

    /**
     * ﾎﾟｯﾄ8_調合量規格3
     * @param potto8_tyogouryoukikaku3 the potto8_tyogouryoukikaku3 to set
     */
    public void setPotto8_tyogouryoukikaku3(FXHDD01 potto8_tyogouryoukikaku3) {
        this.potto8_tyogouryoukikaku3 = potto8_tyogouryoukikaku3;
    }

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.3_1
     * @return the potto8_sizailotno3_1
     */
    public FXHDD01 getPotto8_sizailotno3_1() {
        return potto8_sizailotno3_1;
    }

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.3_1
     * @param potto8_sizailotno3_1 the potto8_sizailotno3_1 to set
     */
    public void setPotto8_sizailotno3_1(FXHDD01 potto8_sizailotno3_1) {
        this.potto8_sizailotno3_1 = potto8_sizailotno3_1;
    }

    /**
     * ﾎﾟｯﾄ8_調合量3_1
     * @return the potto8_tyougouryou3_1
     */
    public FXHDD01 getPotto8_tyougouryou3_1() {
        return potto8_tyougouryou3_1;
    }

    /**
     * ﾎﾟｯﾄ8_調合量3_1
     * @param potto8_tyougouryou3_1 the potto8_tyougouryou3_1 to set
     */
    public void setPotto8_tyougouryou3_1(FXHDD01 potto8_tyougouryou3_1) {
        this.potto8_tyougouryou3_1 = potto8_tyougouryou3_1;
    }

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.3_2
     * @return the potto8_sizailotno3_2
     */
    public FXHDD01 getPotto8_sizailotno3_2() {
        return potto8_sizailotno3_2;
    }

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.3_2
     * @param potto8_sizailotno3_2 the potto8_sizailotno3_2 to set
     */
    public void setPotto8_sizailotno3_2(FXHDD01 potto8_sizailotno3_2) {
        this.potto8_sizailotno3_2 = potto8_sizailotno3_2;
    }

    /**
     * ﾎﾟｯﾄ8_調合量3_2
     * @return the potto8_tyougouryou3_2
     */
    public FXHDD01 getPotto8_tyougouryou3_2() {
        return potto8_tyougouryou3_2;
    }

    /**
     * ﾎﾟｯﾄ8_調合量3_2
     * @param potto8_tyougouryou3_2 the potto8_tyougouryou3_2 to set
     */
    public void setPotto8_tyougouryou3_2(FXHDD01 potto8_tyougouryou3_2) {
        this.potto8_tyougouryou3_2 = potto8_tyougouryou3_2;
    }

    /**
     * ﾎﾟｯﾄ8_材料品名4
     * @return the potto8_zairyohinmei4
     */
    public FXHDD01 getPotto8_zairyohinmei4() {
        return potto8_zairyohinmei4;
    }

    /**
     * ﾎﾟｯﾄ8_材料品名4
     * @param potto8_zairyohinmei4 the potto8_zairyohinmei4 to set
     */
    public void setPotto8_zairyohinmei4(FXHDD01 potto8_zairyohinmei4) {
        this.potto8_zairyohinmei4 = potto8_zairyohinmei4;
    }

    /**
     * ﾎﾟｯﾄ8_調合量規格4
     * @return the potto8_tyogouryoukikaku4
     */
    public FXHDD01 getPotto8_tyogouryoukikaku4() {
        return potto8_tyogouryoukikaku4;
    }

    /**
     * ﾎﾟｯﾄ8_調合量規格4
     * @param potto8_tyogouryoukikaku4 the potto8_tyogouryoukikaku4 to set
     */
    public void setPotto8_tyogouryoukikaku4(FXHDD01 potto8_tyogouryoukikaku4) {
        this.potto8_tyogouryoukikaku4 = potto8_tyogouryoukikaku4;
    }

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.4_1
     * @return the potto8_sizailotno4_1
     */
    public FXHDD01 getPotto8_sizailotno4_1() {
        return potto8_sizailotno4_1;
    }

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.4_1
     * @param potto8_sizailotno4_1 the potto8_sizailotno4_1 to set
     */
    public void setPotto8_sizailotno4_1(FXHDD01 potto8_sizailotno4_1) {
        this.potto8_sizailotno4_1 = potto8_sizailotno4_1;
    }

    /**
     * ﾎﾟｯﾄ8_調合量4_1
     * @return the potto8_tyougouryou4_1
     */
    public FXHDD01 getPotto8_tyougouryou4_1() {
        return potto8_tyougouryou4_1;
    }

    /**
     * ﾎﾟｯﾄ8_調合量4_1
     * @param potto8_tyougouryou4_1 the potto8_tyougouryou4_1 to set
     */
    public void setPotto8_tyougouryou4_1(FXHDD01 potto8_tyougouryou4_1) {
        this.potto8_tyougouryou4_1 = potto8_tyougouryou4_1;
    }

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.4_2
     * @return the potto8_sizailotno4_2
     */
    public FXHDD01 getPotto8_sizailotno4_2() {
        return potto8_sizailotno4_2;
    }

    /**
     * ﾎﾟｯﾄ8_資材ﾛｯﾄNo.4_2
     * @param potto8_sizailotno4_2 the potto8_sizailotno4_2 to set
     */
    public void setPotto8_sizailotno4_2(FXHDD01 potto8_sizailotno4_2) {
        this.potto8_sizailotno4_2 = potto8_sizailotno4_2;
    }

    /**
     * ﾎﾟｯﾄ8_調合量4_2
     * @return the potto8_tyougouryou4_2
     */
    public FXHDD01 getPotto8_tyougouryou4_2() {
        return potto8_tyougouryou4_2;
    }

    /**
     * ﾎﾟｯﾄ8_調合量4_2
     * @param potto8_tyougouryou4_2 the potto8_tyougouryou4_2 to set
     */
    public void setPotto8_tyougouryou4_2(FXHDD01 potto8_tyougouryou4_2) {
        this.potto8_tyougouryou4_2 = potto8_tyougouryou4_2;
    }

}
