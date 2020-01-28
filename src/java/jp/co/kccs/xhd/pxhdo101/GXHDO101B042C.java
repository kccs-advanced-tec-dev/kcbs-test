/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import jp.co.kccs.xhd.db.model.FXHDD01;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/12/28<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	863 F.Zhang<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101B042C(電気特性・一般品(電圧DROP設定条件))
 *
 * @author 863 F.Zhang
 * @since 2019/12/28
 */
@ViewScoped
@Named("beanGXHDO101C042C")
public class GXHDO101B042C implements Serializable {

    /**
     * DROP1,3 PC
     */
    private FXHDD01 drop13pc;

    /**
     * DROP1,3 PS
     */
    private FXHDD01 drop13ps;

    /**
     * DROP1,3 MS･DC
     */
    private FXHDD01 drop13msdc;

    /**
     * DROP2,4 PC
     */
    private FXHDD01 drop24pc;

    /**
     * DROP2,4 PS
     */
    private FXHDD01 drop24ps;

    /**
     * DROP2,4 MS･DC
     */
    private FXHDD01 drop24msdc;

    /**
     * コンストラクタ
     */
    public GXHDO101B042C() {
    }

    /**
     * DROP1,3 PC
     *
     * @return the drop13pc
     */
    public FXHDD01 getDrop13pc() {
        return drop13pc;
    }

    /**
     * DROP1,3 PC
     *
     * @param drop13pc the drop13pc to set
     */
    public void setDrop13pc(FXHDD01 drop13pc) {
        this.drop13pc = drop13pc;
    }

    /**
     * DROP1,3 PS
     *
     * @return the drop13ps
     */
    public FXHDD01 getDrop13ps() {
        return drop13ps;
    }

    /**
     * DROP1,3 PS
     *
     * @param drop13ps the drop13ps to set
     */
    public void setDrop13ps(FXHDD01 drop13ps) {
        this.drop13ps = drop13ps;
    }

    /**
     * DROP1,3 MS･DC
     *
     * @return the drop13msdc
     */
    public FXHDD01 getDrop13msdc() {
        return drop13msdc;
    }

    /**
     * DROP1,3 MS･DC
     *
     * @param drop13msdc the drop13msdc to set
     */
    public void setDrop13msdc(FXHDD01 drop13msdc) {
        this.drop13msdc = drop13msdc;
    }

    /**
     * DROP2,4 PC
     *
     * @return the drop24pc
     */
    public FXHDD01 getDrop24pc() {
        return drop24pc;
    }

    /**
     * DROP2,4 PC
     *
     * @param drop24pc the drop24pc to set
     */
    public void setDrop24pc(FXHDD01 drop24pc) {
        this.drop24pc = drop24pc;
    }

    /**
     * DROP2,4 PS
     *
     * @return the drop24ps
     */
    public FXHDD01 getDrop24ps() {
        return drop24ps;
    }

    /**
     * DROP2,4 PS
     *
     * @param drop24ps the drop24ps to set
     */
    public void setDrop24ps(FXHDD01 drop24ps) {
        this.drop24ps = drop24ps;
    }

    /**
     * DROP2,4 MS･DC
     *
     * @return the drop24msdc
     */
    public FXHDD01 getDrop24msdc() {
        return drop24msdc;
    }

    /**
     * DROP2,4 MS･DC
     *
     * @param drop24msdc the drop24msdc to set
     */
    public void setDrop24msdc(FXHDD01 drop24msdc) {
        this.drop24msdc = drop24msdc;
    }

}
