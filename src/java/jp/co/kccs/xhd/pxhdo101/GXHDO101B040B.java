/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import jp.co.kccs.xhd.model.gxhdo101b040.*;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import jp.co.kccs.xhd.db.model.FXHDD01;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/12/04<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101B040BModel(電気特性・ESI プリチャージタブ)のモデルクラスです。
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2018/12/04
 */
@SessionScoped
@Named("beanGXHDO101C040B")
public class GXHDO101B040B implements Serializable {

    /**
     * 電圧1
     */
    private FXHDD01 denatsu1;

    /**
     * 充電時間1
     */
    private FXHDD01 judenTime1;

    /**
     * 電圧2
     */
    private FXHDD01 denatsu2;

    /**
     * 充電時間2
     */
    private FXHDD01 judenTime2;

    /**
     * 電圧3
     */
    private FXHDD01 denatsu3;

    /**
     * 充電時間3
     */
    private FXHDD01 judenTime3;

    /**
     * 電圧4
     */
    private FXHDD01 denatsu4;

    /**
     * 充電時間4
     */
    private FXHDD01 judenTime4;

    /**
     * コンストラクタ
     */
    public GXHDO101B040B() {
    }

    /**
     * 電圧
     *
     * @return the denatsu1
     */
    public FXHDD01 getDenatsu1() {
        return denatsu1;
    }

    /**
     * 電圧1
     *
     * @param denatsu1 the denatsu1 to set
     */
    public void setDenatsu1(FXHDD01 denatsu1) {
        this.denatsu1 = denatsu1;
    }

    /**
     * 充電時間1
     *
     * @return the judenTime1
     */
    public FXHDD01 getJudenTime1() {
        return judenTime1;
    }

    /**
     * 充電時間1
     *
     * @param judenTime1 the judenTime1 to set
     */
    public void setJudenTime1(FXHDD01 judenTime1) {
        this.judenTime1 = judenTime1;
    }

    /**
     * 電圧2
     *
     * @return the denatsu2
     */
    public FXHDD01 getDenatsu2() {
        return denatsu2;
    }

    /**
     * 電圧2
     *
     * @param denatsu2 the denatsu2 to set
     */
    public void setDenatsu2(FXHDD01 denatsu2) {
        this.denatsu2 = denatsu2;
    }

    /**
     * 充電時間2
     *
     * @return the judenTime2
     */
    public FXHDD01 getJudenTime2() {
        return judenTime2;
    }

    /**
     * 充電時間2
     *
     * @param judenTime2 the judenTime2 to set
     */
    public void setJudenTime2(FXHDD01 judenTime2) {
        this.judenTime2 = judenTime2;
    }

    /**
     * 電圧3
     *
     * @return the denatsu3
     */
    public FXHDD01 getDenatsu3() {
        return denatsu3;
    }

    /**
     * 電圧3
     *
     * @param denatsu3 the denatsu3 to set
     */
    public void setDenatsu3(FXHDD01 denatsu3) {
        this.denatsu3 = denatsu3;
    }

    /**
     * 充電時間3
     *
     * @return the judenTime3
     */
    public FXHDD01 getJudenTime3() {
        return judenTime3;
    }

    /**
     * 充電時間3
     *
     * @param judenTime3 the judenTime3 to set
     */
    public void setJudenTime3(FXHDD01 judenTime3) {
        this.judenTime3 = judenTime3;
    }

    /**
     * 電圧4
     *
     * @return the denatsu4
     */
    public FXHDD01 getDenatsu4() {
        return denatsu4;
    }

    /**
     * 電圧4
     *
     * @param denatsu4 the denatsu4 to set
     */
    public void setDenatsu4(FXHDD01 denatsu4) {
        this.denatsu4 = denatsu4;
    }

    /**
     * 充電時間4
     *
     * @return the judenTime4
     */
    public FXHDD01 getJudenTime4() {
        return judenTime4;
    }

    /**
     * 充電時間4
     *
     * @param judenTime4 the judenTime4 to set
     */
    public void setJudenTime4(FXHDD01 judenTime4) {
        this.judenTime4 = judenTime4;
    }

}
