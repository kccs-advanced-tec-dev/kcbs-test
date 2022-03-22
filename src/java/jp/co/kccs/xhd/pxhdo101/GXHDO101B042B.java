/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.io.Serializable;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
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
 * 変更日	2020/10/20<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	863 sujialiang<br>
 * 変更理由	項目追加・変更<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101B042B(電気特性・一般品(耐電圧設定条件))
 *
 * @author 863 F.Zhang
 * @since 2019/12/28
 */
@ViewScoped
@Named("beanGXHDO101C042B")
public class GXHDO101B042B implements Serializable {

    /**
     * 耐電圧 電圧1
     */
    private FXHDD01 denatsu1;

    /**
     * 耐電圧 判定値1
     */
    private FXHDD01 hanteichi1;

    /**
     * 耐電圧 判定値1(低)
     */
    private FXHDD01 hanteichi1low;

    /**
     * 耐電圧 判定値1 単位
     */
    private FXHDD01 hanteichi1tani;

    /**
     * 耐電圧 充電時間1
     */
    private FXHDD01 judenTime1;

    /**
     * 耐電圧 電圧2
     */
    private FXHDD01 denatsu2;

    /**
     * 耐電圧 判定値2
     */
    private FXHDD01 hanteichi2;

    /**
     * 耐電圧 判定値2(低)
     */
    private FXHDD01 hanteichi2low;

    /**
     * 耐電圧 判定値2 単位
     */
    private FXHDD01 hanteichi2tani;

    /**
     * 耐電圧 充電時間2
     */
    private FXHDD01 judenTime2;

    /**
     * 耐電圧 電圧3
     */
    private FXHDD01 denatsu3;

    /**
     * 耐電圧 判定値3
     */
    private FXHDD01 hanteichi3;

    /**
     * 耐電圧 判定値3(低)
     */
    private FXHDD01 hanteichi3low;

    /**
     * 耐電圧 判定値3 単位
     */
    private FXHDD01 hanteichi3tani;

    /**
     * 耐電圧 充電時間3
     */
    private FXHDD01 judenTime3;

    /**
     * 耐電圧 電圧4
     */
    private FXHDD01 denatsu4;

    /**
     * 耐電圧 判定値4
     */
    private FXHDD01 hanteichi4;

    /**
     * 耐電圧 判定値4(低)
     */
    private FXHDD01 hanteichi4low;

    /**
     * 耐電圧 判定値4 単位
     */
    private FXHDD01 hanteichi4tani;

    /**
     * 耐電圧 充電時間4
     */
    private FXHDD01 judenTime4;

    /**
     * 耐電圧 電圧5
     */
    private FXHDD01 denatsu5;

    /**
     * 耐電圧 判定値5
     */
    private FXHDD01 hanteichi5;

    /**
     * 耐電圧 判定値5(低)
     */
    private FXHDD01 hanteichi5low;

    /**
     * 耐電圧 判定値5 単位
     */
    private FXHDD01 hanteichi5tani;

    /**
     * 耐電圧 充電時間5
     */
    private FXHDD01 judenTime5;

    /**
     * 耐電圧 電圧6
     */
    private FXHDD01 denatsu6;

    /**
     * 耐電圧 判定値6
     */
    private FXHDD01 hanteichi6;

    /**
     * 耐電圧 判定値6(低)
     */
    private FXHDD01 hanteichi6low;

    /**
     * 耐電圧 判定値6 単位
     */
    private FXHDD01 hanteichi6tani;

    /**
     * 耐電圧 充電時間6
     */
    private FXHDD01 judenTime6;

    /**
     * 耐電圧 電圧7
     */
    private FXHDD01 denatsu7;

    /**
     * 耐電圧 判定値7
     */
    private FXHDD01 hanteichi7;

    /**
     * 耐電圧 判定値7(低)
     */
    private FXHDD01 hanteichi7low;

    /**
     * 耐電圧 判定値7 単位
     */
    private FXHDD01 hanteichi7tani;

    /**
     * 耐電圧 充電時間7
     */
    private FXHDD01 judenTime7;

    /**
     * 耐電圧 電圧8
     */
    private FXHDD01 denatsu8;

    /**
     * 耐電圧 判定値8
     */
    private FXHDD01 hanteichi8;

    /**
     * 耐電圧 判定値8(低)
     */
    private FXHDD01 hanteichi8low;

    /**
     * 耐電圧 判定値8 単位
     */
    private FXHDD01 hanteichi8tani;

    /**
     * 耐電圧 充電時間8
     */
    private FXHDD01 judenTime8;

    /**
     * 耐電圧 判定値1(低) 単位
     */
    private FXHDD01 hanteichi1tani_low;

    /**
     * 耐電圧 判定値2(低) 単位
     */
    private FXHDD01 hanteichi2tani_low;

    /**
     * 耐電圧 判定値3(低) 単位
     */
    private FXHDD01 hanteichi3tani_low;

    /**
     * 耐電圧 判定値4(低) 単位
     */
    private FXHDD01 hanteichi4tani_low;

    /**
     * コンストラクタ
     */
    public GXHDO101B042B() {
    }

    /**
     * 耐電圧 電圧1
     *
     * @return the denatsu1
     */
    public FXHDD01 getDenatsu1() {
        return denatsu1;
    }

    /**
     * 耐電圧 電圧1
     *
     * @param denatsu1 the denatsu1 to set
     */
    public void setDenatsu1(FXHDD01 denatsu1) {
        this.denatsu1 = denatsu1;
    }

    /**
     * 耐電圧 判定値1
     *
     * @return the hanteichi1
     */
    public FXHDD01 getHanteichi1() {
        return hanteichi1;
    }

    /**
     * 耐電圧 判定値1
     *
     * @param hanteichi1 the hanteichi1 to set
     */
    public void setHanteichi1(FXHDD01 hanteichi1) {
        this.hanteichi1 = hanteichi1;
    }

    /**
     * 耐電圧 判定値1(低)
     * 
     * @return hanteichi1low
     */
    public FXHDD01 getHanteichi1low() {
        return hanteichi1low;
    }

    /**
     * 耐電圧 判定値1(低)
     * 
     * @param hanteichi1low
     */
    public void setHanteichi1low(FXHDD01 hanteichi1low) {
        this.hanteichi1low = hanteichi1low;
    }

    /**
     * 耐電圧 判定値1 単位
     * 
     * @return hanteichi1tani
     */
    public FXHDD01 getHanteichi1tani() {
        return hanteichi1tani;
    }

    /**
     * 耐電圧 判定値1 単位
     * 
     * @param hanteichi1tani
     */
    public void setHanteichi1tani(FXHDD01 hanteichi1tani) {
        this.hanteichi1tani = hanteichi1tani;
    }

    /**
     * 耐電圧 充電時間1
     *
     * @return the judenTime1
     */
    public FXHDD01 getJudenTime1() {
        return judenTime1;
    }

    /**
     * 耐電圧 充電時間1
     *
     * @param judenTime1 the judenTime1 to set
     */
    public void setJudenTime1(FXHDD01 judenTime1) {
        this.judenTime1 = judenTime1;
    }

    /**
     * 耐電圧 電圧2
     *
     * @return the denatsu2
     */
    public FXHDD01 getDenatsu2() {
        return denatsu2;
    }

    /**
     * 耐電圧 電圧2
     *
     * @param denatsu2 the denatsu2 to set
     */
    public void setDenatsu2(FXHDD01 denatsu2) {
        this.denatsu2 = denatsu2;
    }

    /**
     * 耐電圧 判定値2
     *
     * @return the hanteichi2
     */
    public FXHDD01 getHanteichi2() {
        return hanteichi2;
    }

    /**
     * 耐電圧 判定値2
     *
     * @param hanteichi2 the hanteichi2 to set
     */
    public void setHanteichi2(FXHDD01 hanteichi2) {
        this.hanteichi2 = hanteichi2;
    }

    /**
     * 耐電圧 判定値2(低)
     * 
     * @return hanteichi2low
     */
    public FXHDD01 getHanteichi2low() {
        return hanteichi2low;
    }

    /**
     * 耐電圧 判定値2(低)
     * 
     * @param hanteichi2low
     */
    public void setHanteichi2low(FXHDD01 hanteichi2low) {
        this.hanteichi2low = hanteichi2low;
    }

    /**
     * 耐電圧 判定値2 単位
     * 
     * @return hanteichi2tani
     */
    public FXHDD01 getHanteichi2tani() {
        return hanteichi2tani;
    }

    /**
     * 耐電圧 判定値2 単位
     * 
     * @param hanteichi2tani
     */
    public void setHanteichi2tani(FXHDD01 hanteichi2tani) {
        this.hanteichi2tani = hanteichi2tani;
    }

    /**
     * 耐電圧 充電時間2
     *
     * @return the judenTime2
     */
    public FXHDD01 getJudenTime2() {
        return judenTime2;
    }

    /**
     * 耐電圧 充電時間2
     *
     * @param judenTime2 the judenTime2 to set
     */
    public void setJudenTime2(FXHDD01 judenTime2) {
        this.judenTime2 = judenTime2;
    }

    /**
     * 耐電圧 電圧3
     *
     * @return the denatsu3
     */
    public FXHDD01 getDenatsu3() {
        return denatsu3;
    }

    /**
     * 耐電圧 電圧3
     *
     * @param denatsu3 the denatsu3 to set
     */
    public void setDenatsu3(FXHDD01 denatsu3) {
        this.denatsu3 = denatsu3;
    }

    /**
     * 耐電圧 判定値3
     *
     * @return the hanteichi3
     */
    public FXHDD01 getHanteichi3() {
        return hanteichi3;
    }

    /**
     * 耐電圧 判定値3
     *
     * @param hanteichi3 the hanteichi3 to set
     */
    public void setHanteichi3(FXHDD01 hanteichi3) {
        this.hanteichi3 = hanteichi3;
    }

    /**
     * 耐電圧 判定値3(低)
     * 
     * @return hanteichi3low
     */
    public FXHDD01 getHanteichi3low() {
        return hanteichi3low;
    }

    /**
     * 耐電圧 判定値3(低)
     * 
     * @param hanteichi3low
     */
    public void setHanteichi3low(FXHDD01 hanteichi3low) {
        this.hanteichi3low = hanteichi3low;
    }

    /**
     * 耐電圧 判定値3 単位
     * 
     * @return hanteichi3tani
     */
    public FXHDD01 getHanteichi3tani() {
        return hanteichi3tani;
    }

    /**
     * 耐電圧 判定値3 単位
     * 
     * @param hanteichi3tani
     */
    public void setHanteichi3tani(FXHDD01 hanteichi3tani) {
        this.hanteichi3tani = hanteichi3tani;
    }

    /**
     * 耐電圧 充電時間3
     *
     * @return the judenTime3
     */
    public FXHDD01 getJudenTime3() {
        return judenTime3;
    }

    /**
     * 耐電圧 充電時間3
     *
     * @param judenTime3 the judenTime3 to set
     */
    public void setJudenTime3(FXHDD01 judenTime3) {
        this.judenTime3 = judenTime3;
    }

    /**
     * 耐電圧 電圧4
     *
     * @return the denatsu4
     */
    public FXHDD01 getDenatsu4() {
        return denatsu4;
    }

    /**
     * 耐電圧 電圧4
     *
     * @param denatsu4 the denatsu4 to set
     */
    public void setDenatsu4(FXHDD01 denatsu4) {
        this.denatsu4 = denatsu4;
    }

    /**
     * 耐電圧 判定値4
     *
     * @return the hanteichi4
     */
    public FXHDD01 getHanteichi4() {
        return hanteichi4;
    }

    /**
     * 耐電圧 判定値4
     *
     * @param hanteichi4 the hanteichi4 to set
     */
    public void setHanteichi4(FXHDD01 hanteichi4) {
        this.hanteichi4 = hanteichi4;
    }

    /**
     * 耐電圧 判定値4(低)
     * 
     * @return hanteichi4low
     */
    public FXHDD01 getHanteichi4low() {
        return hanteichi4low;
    }

    /**
     * 耐電圧 判定値4(低)
     * 
     * @param hanteichi4low
     */
    public void setHanteichi4low(FXHDD01 hanteichi4low) {
        this.hanteichi4low = hanteichi4low;
    }

    /**
     * 耐電圧 判定値4 単位
     * 
     * @return hanteichi4tani
     */
    public FXHDD01 getHanteichi4tani() {
        return hanteichi4tani;
    }

    /**
     * 耐電圧 判定値4 単位
     * 
     * @param hanteichi4tani
     */
    public void setHanteichi4tani(FXHDD01 hanteichi4tani) {
        this.hanteichi4tani = hanteichi4tani;
    }

    /**
     * 耐電圧 充電時間4
     *
     * @return the judenTime4
     */
    public FXHDD01 getJudenTime4() {
        return judenTime4;
    }

    /**
     * 耐電圧 充電時間4
     *
     * @param judenTime4 the judenTime4 to set
     */
    public void setJudenTime4(FXHDD01 judenTime4) {
        this.judenTime4 = judenTime4;
    }

    /**
     * 耐電圧 電圧5
     *
     * @return the denatsu5
     */
    public FXHDD01 getDenatsu5() {
        return denatsu5;
    }

    /**
     * 耐電圧 電圧5
     *
     * @param denatsu5 the denatsu5 to set
     */
    public void setDenatsu5(FXHDD01 denatsu5) {
        this.denatsu5 = denatsu5;
    }

    /**
     * 耐電圧 判定値5
     *
     * @return the hanteichi5
     */
    public FXHDD01 getHanteichi5() {
        return hanteichi5;
    }

    /**
     * 耐電圧 判定値5
     *
     * @param hanteichi5 the hanteichi5 to set
     */
    public void setHanteichi5(FXHDD01 hanteichi5) {
        this.hanteichi5 = hanteichi5;
    }

    /**
     * 耐電圧 判定値5(低)
     * 
     * @return hanteichi5low
     */
    public FXHDD01 getHanteichi5low() {
        return hanteichi5low;
    }

    /**
     * 耐電圧 判定値5(低)
     * 
     * @param hanteichi5low
     */
    public void setHanteichi5low(FXHDD01 hanteichi5low) {
        this.hanteichi5low = hanteichi5low;
    }

    /**
     * 耐電圧 判定値5 単位
     * 
     * @return hanteichi5tani
     */
    public FXHDD01 getHanteichi5tani() {
        return hanteichi5tani;
    }

    /**
     * 耐電圧 判定値5 単位
     * 
     * @param hanteichi5tani
     */
    public void setHanteichi5tani(FXHDD01 hanteichi5tani) {
        this.hanteichi5tani = hanteichi5tani;
    }

    /**
     * 耐電圧 充電時間5
     *
     * @return the judenTime5
     */
    public FXHDD01 getJudenTime5() {
        return judenTime5;
    }

    /**
     * 耐電圧 充電時間5
     *
     * @param judenTime5 the judenTime5 to set
     */
    public void setJudenTime5(FXHDD01 judenTime5) {
        this.judenTime5 = judenTime5;
    }

    /**
     * 耐電圧 電圧6
     *
     * @return the denatsu6
     */
    public FXHDD01 getDenatsu6() {
        return denatsu6;
    }

    /**
     * 耐電圧 電圧6
     *
     * @param denatsu6 the denatsu6 to set
     */
    public void setDenatsu6(FXHDD01 denatsu6) {
        this.denatsu6 = denatsu6;
    }

    /**
     * 耐電圧 判定値6
     *
     * @return the hanteichi6
     */
    public FXHDD01 getHanteichi6() {
        return hanteichi6;
    }

    /**
     * 耐電圧 判定値6
     *
     * @param hanteichi6 the hanteichi6 to set
     */
    public void setHanteichi6(FXHDD01 hanteichi6) {
        this.hanteichi6 = hanteichi6;
    }

    /**
     * 耐電圧 判定値6(低)
     * 
     * @return hanteichi6low
     */
    public FXHDD01 getHanteichi6low() {
        return hanteichi6low;
    }

    /**
     * 耐電圧 判定値6(低)
     * 
     * @param hanteichi6low
     */
    public void setHanteichi6low(FXHDD01 hanteichi6low) {
        this.hanteichi6low = hanteichi6low;
    }

    /**
     * 耐電圧 判定値6 単位
     * 
     * @return hanteichi6tani
     */
    public FXHDD01 getHanteichi6tani() {
        return hanteichi6tani;
    }

    /**
     * 耐電圧 判定値6 単位
     * 
     * @param hanteichi6tani
     */
    public void setHanteichi6tani(FXHDD01 hanteichi6tani) {
        this.hanteichi6tani = hanteichi6tani;
    }

    /**
     * 耐電圧 充電時間6
     *
     * @return the judenTime6
     */
    public FXHDD01 getJudenTime6() {
        return judenTime6;
    }

    /**
     * 耐電圧 充電時間6
     *
     * @param judenTime6 the judenTime6 to set
     */
    public void setJudenTime6(FXHDD01 judenTime6) {
        this.judenTime6 = judenTime6;
    }

    /**
     * 耐電圧 電圧7
     *
     * @return the denatsu7
     */
    public FXHDD01 getDenatsu7() {
        return denatsu7;
    }

    /**
     * 耐電圧 電圧7
     *
     * @param denatsu7 the denatsu7 to set
     */
    public void setDenatsu7(FXHDD01 denatsu7) {
        this.denatsu7 = denatsu7;
    }

    /**
     * 耐電圧 判定値7
     *
     * @return the hanteichi7
     */
    public FXHDD01 getHanteichi7() {
        return hanteichi7;
    }

    /**
     * 耐電圧 判定値7
     *
     * @param hanteichi7 the hanteichi7 to set
     */
    public void setHanteichi7(FXHDD01 hanteichi7) {
        this.hanteichi7 = hanteichi7;
    }

    /**
     * 耐電圧 判定値7(低)
     * 
     * @return hanteichi7low
     */
    public FXHDD01 getHanteichi7low() {
        return hanteichi7low;
    }

    /**
     * 耐電圧 判定値7(低)
     * 
     * @param hanteichi7low
     */
    public void setHanteichi7low(FXHDD01 hanteichi7low) {
        this.hanteichi7low = hanteichi7low;
    }

    /**
     * 耐電圧 判定値7 単位
     * 
     * @return hanteichi7tani
     */
    public FXHDD01 getHanteichi7tani() {
        return hanteichi7tani;
    }

    /**
     * 耐電圧 判定値7 単位
     * 
     * @param hanteichi7tani
     */
    public void setHanteichi7tani(FXHDD01 hanteichi7tani) {
        this.hanteichi7tani = hanteichi7tani;
    }

    /**
     * 耐電圧 充電時間7
     *
     * @return the judenTime7
     */
    public FXHDD01 getJudenTime7() {
        return judenTime7;
    }

    /**
     * 耐電圧 充電時間7
     *
     * @param judenTime7 the judenTime7 to set
     */
    public void setJudenTime7(FXHDD01 judenTime7) {
        this.judenTime7 = judenTime7;
    }

    /**
     * 耐電圧 電圧8
     *
     * @return the denatsu8
     */
    public FXHDD01 getDenatsu8() {
        return denatsu8;
    }

    /**
     * 耐電圧 電圧8
     *
     * @param denatsu8 the denatsu8 to set
     */
    public void setDenatsu8(FXHDD01 denatsu8) {
        this.denatsu8 = denatsu8;
    }

    /**
     * 耐電圧 判定値8
     *
     * @return the hanteichi8
     */
    public FXHDD01 getHanteichi8() {
        return hanteichi8;
    }

    /**
     * 耐電圧 判定値8
     *
     * @param hanteichi8 the hanteichi8 to set
     */
    public void setHanteichi8(FXHDD01 hanteichi8) {
        this.hanteichi8 = hanteichi8;
    }

    /**
     * 耐電圧 判定値8(低)
     * 
     * @return hanteichi8low
     */
    public FXHDD01 getHanteichi8low() {
        return hanteichi8low;
    }

    /**
     * 耐電圧 判定値8(低)
     * 
     * @param hanteichi8low
     */
    public void setHanteichi8low(FXHDD01 hanteichi8low) {
        this.hanteichi8low = hanteichi8low;
    }

    /**
     * 耐電圧 判定値8 単位
     * 
     * @return hanteichi8tani
     */
    public FXHDD01 getHanteichi8tani() {
        return hanteichi8tani;
    }

    /**
     * 耐電圧 判定値8 単位
     * 
     * @param hanteichi8tani
     */
    public void setHanteichi8tani(FXHDD01 hanteichi8tani) {
        this.hanteichi8tani = hanteichi8tani;
    }

    /**
     * 耐電圧 充電時間8
     *
     * @return the judenTime8
     */
    public FXHDD01 getJudenTime8() {
        return judenTime8;
    }

    /**
     * 耐電圧 充電時間8
     *
     * @param judenTime8 the judenTime8 to set
     */
    public void setJudenTime8(FXHDD01 judenTime8) {
        this.judenTime8 = judenTime8;
    }

    /**
     * 耐電圧 判定値1(低) 単位
     * @return the hanteichi1tani_low
     */
    public FXHDD01 getHanteichi1tani_low() {
        return hanteichi1tani_low;
    }

    /**
     * 耐電圧 判定値1(低) 単位
     * @param hanteichi1tani_low the hanteichi1tani_low to set
     */
    public void setHanteichi1tani_low(FXHDD01 hanteichi1tani_low) {
        this.hanteichi1tani_low = hanteichi1tani_low;
    }

    /**
     * 耐電圧 判定値2(低) 単位
     * @return the hanteichi2tani_low
     */
    public FXHDD01 getHanteichi2tani_low() {
        return hanteichi2tani_low;
    }

    /**
     * 耐電圧 判定値2(低) 単位
     * @param hanteichi2tani_low the hanteichi2tani_low to set
     */
    public void setHanteichi2tani_low(FXHDD01 hanteichi2tani_low) {
        this.hanteichi2tani_low = hanteichi2tani_low;
    }

    /**
     * 耐電圧 判定値3(低) 単位
     * @return the hanteichi3tani_low
     */
    public FXHDD01 getHanteichi3tani_low() {
        return hanteichi3tani_low;
    }

    /**
     * 耐電圧 判定値3(低) 単位
     * @param hanteichi3tani_low the hanteichi3tani_low to set
     */
    public void setHanteichi3tani_low(FXHDD01 hanteichi3tani_low) {
        this.hanteichi3tani_low = hanteichi3tani_low;
    }

    /**
     * 耐電圧 判定値4(低) 単位
     * @return the hanteichi4tani_low
     */
    public FXHDD01 getHanteichi4tani_low() {
        return hanteichi4tani_low;
    }

    /**
     * 耐電圧 判定値4(低) 単位
     * @param hanteichi4tani_low the hanteichi4tani_low to set
     */
    public void setHanteichi4tani_low(FXHDD01 hanteichi4tani_low) {
        this.hanteichi4tani_low = hanteichi4tani_low;
    }

}
