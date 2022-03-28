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
 * システム名    品質DB(コンデンサ)<br>
 * <br>
 * 変更日        2019/12/28<br>
 * 計画書No      K1811-DS001<br>
 * 変更者        KCSS K.Jo<br>
 * 変更理由      新規作成<br>
 * <br>
 * 変更日	2020/10/19<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	863 sujialiang<br>
 * 変更理由	項目追加・変更<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101B041B(電気特性・3端子4端子(耐電圧設定条件))
 *
 * @author KCSS K.Jo
 * @since  2019/12/28
 */
@ViewScoped
@Named("beanGXHDO101C041B")
public class GXHDO101B041B implements Serializable {

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
     * RDC1 ﾚﾝｼﾞ
     */
    private FXHDD01 rdc1renji;

    /**
     * RDC1 判定値
     */
    private FXHDD01 rdc1hanteichi;
    
    /**
     * RDC2 ﾚﾝｼﾞ
     */
    private FXHDD01 rdc2renji;

    /**
     * RDC2 判定値
     */
    private FXHDD01 rdc2hanteichi;

    /**
     * 耐電圧 判定値1 単位
     */
    private FXHDD01 hanteichi1tani_low;

    /**
     * 耐電圧 判定値2 単位
     */
    private FXHDD01 hanteichi2tani_low;
        
    /**
     * コンストラクタ
     */
    public GXHDO101B041B() {
    }

    /**
     * 耐電圧 電圧1
     * @return the denatsu1
     */
    public FXHDD01 getDenatsu1() {
        return denatsu1;
    }

    /**
     * 耐電圧 電圧1
     * @param denatsu1 the denatsu1 to set
     */
    public void setDenatsu1(FXHDD01 denatsu1) {
        this.denatsu1 = denatsu1;
    }

    /**
     * 耐電圧 判定値1
     * @return the hanteichi1
     */
    public FXHDD01 getHanteichi1() {
        return hanteichi1;
    }

    /**
     * 耐電圧 判定値1
     * @param hanteichi1 the hanteichi1 to set
     */
    public void setHanteichi1(FXHDD01 hanteichi1) {
        this.hanteichi1 = hanteichi1;
    }

    /**
     * 耐電圧 判定値1(低)
     * @return hanteichi1low
     */
    public FXHDD01 getHanteichi1low() {
        return hanteichi1low;
    }

    /**
     * 耐電圧 判定値1(低)
     * @param hanteichi1low 
     */
    public void setHanteichi1low(FXHDD01 hanteichi1low) {
        this.hanteichi1low = hanteichi1low;
    }

    /**
     * 耐電圧 判定値1 単位
     * @return hanteichi1tani
     */
    public FXHDD01 getHanteichi1tani() {
        return hanteichi1tani;
    }

    /**
     * 耐電圧 判定値1 単位
     * @param hanteichi1tani 
     */
    public void setHanteichi1tani(FXHDD01 hanteichi1tani) {
        this.hanteichi1tani = hanteichi1tani;
    }

    /**
     * 耐電圧 充電時間1
     * @return the judenTime1
     */
    public FXHDD01 getJudenTime1() {
        return judenTime1;
    }

    /**
     * 耐電圧 充電時間1
     * @param judenTime1 the judenTime1 to set
     */
    public void setJudenTime1(FXHDD01 judenTime1) {
        this.judenTime1 = judenTime1;
    }

    /**
     * 耐電圧 電圧2
     * @return the denatsu2
     */
    public FXHDD01 getDenatsu2() {
        return denatsu2;
    }

    /**
     * 耐電圧 電圧2
     * @param denatsu2 the denatsu2 to set
     */
    public void setDenatsu2(FXHDD01 denatsu2) {
        this.denatsu2 = denatsu2;
    }

    /**
     * 耐電圧 判定値2
     * @return the hanteichi2
     */
    public FXHDD01 getHanteichi2() {
        return hanteichi2;
    }

    /**
     * 耐電圧 判定値2
     * @param hanteichi2 the hanteichi2 to set
     */
    public void setHanteichi2(FXHDD01 hanteichi2) {
        this.hanteichi2 = hanteichi2;
    }

    /**
     * 耐電圧 判定値2(低)
     * @return hanteichi2low
     */
    public FXHDD01 getHanteichi2low() {
        return hanteichi2low;
    }

    /**
     * 耐電圧 判定値2(低)
     * @param hanteichi2low 
     */
    public void setHanteichi2low(FXHDD01 hanteichi2low) {
        this.hanteichi2low = hanteichi2low;
    }

    /**
     * 耐電圧 判定値2 単位
     * @return hanteichi2tani
     */
    public FXHDD01 getHanteichi2tani() {
        return hanteichi2tani;
    }

    /**
     * 耐電圧 判定値2 単位
     * @param hanteichi2tani 
     */
    public void setHanteichi2tani(FXHDD01 hanteichi2tani) {
        this.hanteichi2tani = hanteichi2tani;
    }

    /**
     * 耐電圧 充電時間2
     * @return the judenTime2
     */
    public FXHDD01 getJudenTime2() {
        return judenTime2;
    }

    /**
     * 耐電圧 充電時間2
     * @param judenTime2 the judenTime2 to set
     */
    public void setJudenTime2(FXHDD01 judenTime2) {
        this.judenTime2 = judenTime2;
    }

    /**
     * RDC1 ﾚﾝｼﾞ
     * @return the rdc1renji
     */
    public FXHDD01 getRdc1renji() {
        return rdc1renji;
    }

    /**
     * RDC1 ﾚﾝｼﾞ
     * @param rdc1renji the rdc1renji to set
     */
    public void setRdc1renji(FXHDD01 rdc1renji) {
        this.rdc1renji = rdc1renji;
    }

    /**
     * RDC1 判定値
     * @return the rdc1hanteichi
     */
    public FXHDD01 getRdc1hanteichi() {
        return rdc1hanteichi;
    }

    /**
     * RDC1 判定値
     * @param rdc1hanteichi the rdc1hanteichi to set
     */
    public void setRdc1hanteichi(FXHDD01 rdc1hanteichi) {
        this.rdc1hanteichi = rdc1hanteichi;
    }

    /**
     * RDC2 ﾚﾝｼﾞ
     * @return the rdc2renji
     */
    public FXHDD01 getRdc2renji() {
        return rdc2renji;
    }

    /**
     * RDC2 ﾚﾝｼﾞ
     * @param rdc2renji the rdc2renji to set
     */
    public void setRdc2renji(FXHDD01 rdc2renji) {
        this.rdc2renji = rdc2renji;
    }

    /**
     * RDC2 判定値
     * @return the rdc2hanteichi
     */
    public FXHDD01 getRdc2hanteichi() {
        return rdc2hanteichi;
    }

    /**
     * RDC2 判定値
     * @param rdc2hanteichi the rdc2hanteichi to set
     */
    public void setRdc2hanteichi(FXHDD01 rdc2hanteichi) {
        this.rdc2hanteichi = rdc2hanteichi;
    }

    /**
     * 耐電圧 判定値1 単位
     * @return the hanteichi1tani_low
     */
    public FXHDD01 getHanteichi1tani_low() {
        return hanteichi1tani_low;
    }

    /**
     * 耐電圧 判定値1 単位
     * @param hanteichi1tani_low the hanteichi1tani_low to set
     */
    public void setHanteichi1tani_low(FXHDD01 hanteichi1tani_low) {
        this.hanteichi1tani_low = hanteichi1tani_low;
    }

    /**
     * 耐電圧 判定値2 単位
     * @return the hanteichi2tani_low
     */
    public FXHDD01 getHanteichi2tani_low() {
        return hanteichi2tani_low;
    }

    /**
     * 耐電圧 判定値2 単位
     * @param hanteichi2tani_low the hanteichi2tani_low to set
     */
    public void setHanteichi2tani_low(FXHDD01 hanteichi2tani_low) {
        this.hanteichi2tani_low = hanteichi2tani_low;
    }
}
