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
 * GXHDO101B053B(電気特性・耐電圧設定条件)
 *
 * @author 863 F.Zhang
 * @since 2019/12/28
 */
@ViewScoped
@Named("beanGXHDO101C053B")
public class GXHDO101B053B implements Serializable {

    /**
     * 耐電圧 電圧1
     */
    private FXHDD01 denatsu1;

    /**
     * 耐電圧 時間1
     */
    private FXHDD01 time1;

    /**
     * 耐電圧 電流中心値スタート1
     */
    private FXHDD01 denryuStart1;

    /**
     * 耐電圧 電流中心値スタート 単位1
     */
    private FXHDD01 denryuStartTani1;

    /**
     * 耐電圧 電流中心値エンド1
     */
    private FXHDD01 denryuEnd1;
    
    /**
     * 耐電圧 電流中心値エンド 単位1
     */
    private FXHDD01 denryuEndTani1;

    /**
     * 耐電圧 測定範囲スタート1
     */
    private FXHDD01 sokuteiHaniStart1;

    /**
     * 耐電圧 測定範囲スタート 単位1
     */
    private FXHDD01 sokuteiHaniStartTani1;

    /**
     * 耐電圧 測定範囲エンド1
     */
    private FXHDD01 sokuteiHaniEnd1;

    /**
     * 耐電圧 測定範囲エンド 単位1
     */
    private FXHDD01 sokuteiHaniEndTani1;

    /**
     * 耐電圧 良品範囲上限1
     */
    private FXHDD01 ryohinHani1;

    /**
     * 耐電圧 良品範囲上限 単位1
     */
    private FXHDD01 ryohinHaniTani1;

    /**
     * 耐電圧 良品範囲下限1
     */
    private FXHDD01 ryohinHaniLow1;

    /**
     * 耐電圧 良品範囲下限 単位1
     */
    private FXHDD01 ryohinHaniLowTani1;
    
    /**
     * 耐電圧 電圧2
     */
    private FXHDD01 denatsu2;

    /**
     * 耐電圧 時間2
     */
    private FXHDD01 time2;

    /**
     * 耐電圧 電流中心値スタート2
     */
    private FXHDD01 denryuStart2;

    /**
     * 耐電圧 電流中心値スタート 単位2
     */
    private FXHDD01 denryuStartTani2;

    /**
     * 耐電圧 電流中心値エンド2
     */
    private FXHDD01 denryuEnd2;
    
    /**
     * 耐電圧 電流中心値エンド 単位2
     */
    private FXHDD01 denryuEndTani2;

    /**
     * 耐電圧 測定範囲スタート2
     */
    private FXHDD01 sokuteiHaniStart2;

    /**
     * 耐電圧 測定範囲スタート 単位2
     */
    private FXHDD01 sokuteiHaniStartTani2;

    /**
     * 耐電圧 測定範囲エンド2
     */
    private FXHDD01 sokuteiHaniEnd2;

    /**
     * 耐電圧 測定範囲エンド 単位2
     */
    private FXHDD01 sokuteiHaniEndTani2;

    /**
     * 耐電圧 良品範囲上限2
     */
    private FXHDD01 ryohinHani2;

    /**
     * 耐電圧 良品範囲上限 単位2
     */
    private FXHDD01 ryohinHaniTani2;

    /**
     * 耐電圧 良品範囲下限2
     */
    private FXHDD01 ryohinHaniLow2;

    /**
     * 耐電圧 良品範囲下限 単位2
     */
    private FXHDD01 ryohinHaniLowTani2;

    /**
     * 耐電圧 電圧3
     */
    private FXHDD01 denatsu3;

    /**
     * 耐電圧 時間3
     */
    private FXHDD01 time3;

    /**
     * 耐電圧 電流中心値スタート3
     */
    private FXHDD01 denryuStart3;

    /**
     * 耐電圧 電流中心値スタート 単位3
     */
    private FXHDD01 denryuStartTani3;

    /**
     * 耐電圧 電流中心値エンド3
     */
    private FXHDD01 denryuEnd3;
    
    /**
     * 耐電圧 電流中心値エンド 単位3
     */
    private FXHDD01 denryuEndTani3;

    /**
     * 耐電圧 測定範囲スタート3
     */
    private FXHDD01 sokuteiHaniStart3;

    /**
     * 耐電圧 測定範囲スタート 単位3
     */
    private FXHDD01 sokuteiHaniStartTani3;

    /**
     * 耐電圧 測定範囲エンド3
     */
    private FXHDD01 sokuteiHaniEnd3;

    /**
     * 耐電圧 測定範囲エンド 単位3
     */
    private FXHDD01 sokuteiHaniEndTani3;

    /**
     * 耐電圧 良品範囲上限3
     */
    private FXHDD01 ryohinHani3;

    /**
     * 耐電圧 良品範囲上限 単位3
     */
    private FXHDD01 ryohinHaniTani3;

    /**
     * 耐電圧 良品範囲下限3
     */
    private FXHDD01 ryohinHaniLow3;

    /**
     * 耐電圧 良品範囲下限 単位3
     */
    private FXHDD01 ryohinHaniLowTani3;    
    
    /**
     * 耐電圧 電圧4
     */
    private FXHDD01 denatsu4;

    /**
     * 耐電圧 時間4
     */
    private FXHDD01 time4;

    /**
     * 耐電圧 電流中心値スタート4
     */
    private FXHDD01 denryuStart4;

    /**
     * 耐電圧 電流中心値スタート 単位4
     */
    private FXHDD01 denryuStartTani4;

    /**
     * 耐電圧 電流中心値エンド4
     */
    private FXHDD01 denryuEnd4;
    
    /**
     * 耐電圧 電流中心値エンド 単位4
     */
    private FXHDD01 denryuEndTani4;

    /**
     * 耐電圧 測定範囲スタート4
     */
    private FXHDD01 sokuteiHaniStart4;

    /**
     * 耐電圧 測定範囲スタート 単位4
     */
    private FXHDD01 sokuteiHaniStartTani4;

    /**
     * 耐電圧 測定範囲エンド4
     */
    private FXHDD01 sokuteiHaniEnd4;

    /**
     * 耐電圧 測定範囲エンド 単位4
     */
    private FXHDD01 sokuteiHaniEndTani4;

    /**
     * 耐電圧 良品範囲上限4
     */
    private FXHDD01 ryohinHani4;

    /**
     * 耐電圧 良品範囲上限 単位4
     */
    private FXHDD01 ryohinHaniTani4;

    /**
     * 耐電圧 良品範囲下限4
     */
    private FXHDD01 ryohinHaniLow4;

    /**
     * 耐電圧 良品範囲下限 単位4
     */
    private FXHDD01 ryohinHaniLowTani4;    
    
    /**
     * 耐電圧 電圧5
     */
    private FXHDD01 denatsu5;

    /**
     * 耐電圧 時間5
     */
    private FXHDD01 time5;

    /**
     * 耐電圧 電流中心値スタート5
     */
    private FXHDD01 denryuStart5;

    /**
     * 耐電圧 電流中心値スタート 単位5
     */
    private FXHDD01 denryuStartTani5;

    /**
     * 耐電圧 電流中心値エンド5
     */
    private FXHDD01 denryuEnd5;
    
    /**
     * 耐電圧 電流中心値エンド 単位5
     */
    private FXHDD01 denryuEndTani5;

    /**
     * 耐電圧 測定範囲スタート5
     */
    private FXHDD01 sokuteiHaniStart5;

    /**
     * 耐電圧 測定範囲スタート 単位5
     */
    private FXHDD01 sokuteiHaniStartTani5;

    /**
     * 耐電圧 測定範囲エンド5
     */
    private FXHDD01 sokuteiHaniEnd5;

    /**
     * 耐電圧 測定範囲エンド 単位5
     */
    private FXHDD01 sokuteiHaniEndTani5;

    /**
     * 耐電圧 良品範囲上限5
     */
    private FXHDD01 ryohinHani5;

    /**
     * 耐電圧 良品範囲上限 単位5
     */
    private FXHDD01 ryohinHaniTani5;

    /**
     * 耐電圧 良品範囲下限5
     */
    private FXHDD01 ryohinHaniLow5;

    /**
     * 耐電圧 良品範囲下限 単位5
     */
    private FXHDD01 ryohinHaniLowTani5;
    
    /**
     * 耐電圧 電圧6
     */
    private FXHDD01 denatsu6;

    /**
     * 耐電圧 時間6
     */
    private FXHDD01 time6;

    /**
     * 耐電圧 電流中心値スタート6
     */
    private FXHDD01 denryuStart6;

    /**
     * 耐電圧 電流中心値スタート 単位6
     */
    private FXHDD01 denryuStartTani6;

    /**
     * 耐電圧 電流中心値エンド6
     */
    private FXHDD01 denryuEnd6;
    
    /**
     * 耐電圧 電流中心値エンド 単位6
     */
    private FXHDD01 denryuEndTani6;

    /**
     * 耐電圧 測定範囲スタート6
     */
    private FXHDD01 sokuteiHaniStart6;

    /**
     * 耐電圧 測定範囲スタート 単位6
     */
    private FXHDD01 sokuteiHaniStartTani6;

    /**
     * 耐電圧 測定範囲エンド6
     */
    private FXHDD01 sokuteiHaniEnd6;

    /**
     * 耐電圧 測定範囲エンド 単位6
     */
    private FXHDD01 sokuteiHaniEndTani6;

    /**
     * 耐電圧 良品範囲上限6
     */
    private FXHDD01 ryohinHani6;

    /**
     * 耐電圧 良品範囲上限 単位6
     */
    private FXHDD01 ryohinHaniTani6;

    /**
     * 耐電圧 良品範囲下限6
     */
    private FXHDD01 ryohinHaniLow6;

    /**
     * 耐電圧 良品範囲下限 単位6
     */
    private FXHDD01 ryohinHaniLowTani6;    
    
    /**
     * 耐電圧 電圧7
     */
    private FXHDD01 denatsu7;

    /**
     * 耐電圧 時間7
     */
    private FXHDD01 time7;

    /**
     * 耐電圧 電流中心値スタート7
     */
    private FXHDD01 denryuStart7;

    /**
     * 耐電圧 電流中心値スタート 単位7
     */
    private FXHDD01 denryuStartTani7;

    /**
     * 耐電圧 電流中心値エンド7
     */
    private FXHDD01 denryuEnd7;
    
    /**
     * 耐電圧 電流中心値エンド 単位7
     */
    private FXHDD01 denryuEndTani7;

    /**
     * 耐電圧 測定範囲スタート7
     */
    private FXHDD01 sokuteiHaniStart7;

    /**
     * 耐電圧 測定範囲スタート 単位7
     */
    private FXHDD01 sokuteiHaniStartTani7;

    /**
     * 耐電圧 測定範囲エンド7
     */
    private FXHDD01 sokuteiHaniEnd7;

    /**
     * 耐電圧 測定範囲エンド 単位7
     */
    private FXHDD01 sokuteiHaniEndTani7;

    /**
     * 耐電圧 良品範囲上限7
     */
    private FXHDD01 ryohinHani7;

    /**
     * 耐電圧 良品範囲上限 単位7
     */
    private FXHDD01 ryohinHaniTani7;

    /**
     * 耐電圧 良品範囲下限7
     */
    private FXHDD01 ryohinHaniLow7;

    /**
     * 耐電圧 良品範囲下限 単位7
     */
    private FXHDD01 ryohinHaniLowTani7;    
    
    /**
     * 耐電圧 電圧8
     */
    private FXHDD01 denatsu8;

    /**
     * 耐電圧 時間8
     */
    private FXHDD01 time8;

    /**
     * 耐電圧 電流中心値スタート8
     */
    private FXHDD01 denryuStart8;

    /**
     * 耐電圧 電流中心値スタート 単位8
     */
    private FXHDD01 denryuStartTani8;

    /**
     * 耐電圧 電流中心値エンド8
     */
    private FXHDD01 denryuEnd8;
    
    /**
     * 耐電圧 電流中心値エンド 単位8
     */
    private FXHDD01 denryuEndTani8;

    /**
     * 耐電圧 測定範囲スタート8
     */
    private FXHDD01 sokuteiHaniStart8;

    /**
     * 耐電圧 測定範囲スタート 単位8
     */
    private FXHDD01 sokuteiHaniStartTani8;

    /**
     * 耐電圧 測定範囲エンド8
     */
    private FXHDD01 sokuteiHaniEnd8;

    /**
     * 耐電圧 測定範囲エンド 単位8
     */
    private FXHDD01 sokuteiHaniEndTani8;

    /**
     * 耐電圧 良品範囲上限8
     */
    private FXHDD01 ryohinHani8;

    /**
     * 耐電圧 良品範囲上限 単位8
     */
    private FXHDD01 ryohinHaniTani8;

    /**
     * 耐電圧 良品範囲下限8
     */
    private FXHDD01 ryohinHaniLow8;

    /**
     * 耐電圧 良品範囲下限 単位8
     */
    private FXHDD01 ryohinHaniLowTani8;    
    
    
    
    
    
    
    
    
    
    
    
    
  

    /**
     * コンストラクタ
     */
    public GXHDO101B053B() {
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
     * @param denatsu1
     */
    public void setDenatsu1(FXHDD01 denatsu1) {
        this.denatsu1 = denatsu1;
    }

    /**
     * 耐電圧 時間1
     *
     * @return the time1
     */
    public FXHDD01 gettime1() {
        return time1;
    }

    /**
     * 耐電圧 時間1
     *
     * @param time1
     */
    public void setTime1(FXHDD01 time1) {
        this.time1 = time1;
    }

    /**
     * 耐電圧 電流中心値スタート1
     * 
     * @return denryuStart1
     */
    public FXHDD01 getDenryuStart1() {
        return denryuStart1;
    }

    /**
     * 耐電圧 電流中心値スタート1
     * 
     * @param denryuStart1
     */
    public void setDenryuStart1(FXHDD01 denryuStart1) {
        this.denryuStart1 = denryuStart1;
    }

    /**
     * 耐電圧 電流中心値スタート 単位1
     * 
     * @return denryuStartTani1
     */
    public FXHDD01 getDenryuStartTani1() {
        return denryuStartTani1;
    }

    /**
     * 耐電圧 電流中心値スタート 単位1
     * 
     * @param denryuStartTani1
     */
    public void setDenryuStartTani1(FXHDD01 denryuStartTani1) {
        this.denryuStartTani1 = denryuStartTani1;
    }
        
     /**
     * 耐電圧 電流中心値エンド1
     * 
     * @return denryuEnd1
     */
    public FXHDD01 getDenryuEnd1() {
        return denryuEnd1;
    }

    /**
     * 耐電圧 電流中心値エンド1
     * 
     * @param denryuEnd1
     */
    public void setDenryuEnd1(FXHDD01 denryuEnd1) {
        this.denryuEnd1 = denryuEnd1;
    }
       
     /**
     * 耐電圧 電流中心値エンド 単位1
     * 
     * @return denryuEndTani1
     */
    public FXHDD01 getDenryuEndTani1() {
        return denryuEndTani1;
    }

    /**
     * 耐電圧 電流中心値エンド 単位1
     * 
     * @param denryuEndTani1
     */
    public void setDenryuEndTani1(FXHDD01 denryuEndTani1) {
        this.denryuEndTani1 = denryuEndTani1;
    }
       
     /**
     * 耐電圧 測定範囲スタート1
     * 
     * @return sokuteiHaniStart1
     */
    public FXHDD01 getSokuteiHaniStart1() {
        return sokuteiHaniStart1;
    }

    /**
     * 耐電圧 測定範囲スタート1
     * 
     * @param sokuteiHaniStart1
     */
    public void setSokuteiHaniStart1(FXHDD01 sokuteiHaniStart1) {
        this.sokuteiHaniStart1 = sokuteiHaniStart1;
    }           
    
     /**
     * 耐電圧 測定範囲スタート 単位1
     * 
     * @return sokuteiHaniStartTani1
     */
    public FXHDD01 getSokuteiHaniStartTani1() {
        return sokuteiHaniStartTani1;
    }

    /**
     * 耐電圧 測定範囲スタート 単位1
     * 
     * @param sokuteiHaniStartTani1
     */
    public void setSokuteiHaniStartTani1(FXHDD01 sokuteiHaniStartTani1) {
        this.sokuteiHaniStartTani1 = sokuteiHaniStartTani1;
    }        
    
    /**
     * 耐電圧 測定範囲エンド1
     * 
     * @return sokuteiHaniEnd1
     */
    public FXHDD01 getSokuteiHaniEnd1() {
        return sokuteiHaniEnd1;
    }

    /**
     * 耐電圧 測定範囲エンド1
     * 
     * @param sokuteiHaniEnd1
     */
    public void setSokuteiHaniEnd1(FXHDD01 sokuteiHaniEnd1) {
        this.sokuteiHaniEnd1 = sokuteiHaniEnd1;
    }        

    /**
     * 耐電圧 測定範囲エンド 単位1
     * 
     * @return sokuteiHaniEndTani1
     */
    public FXHDD01 getSokuteiHaniEndTani1() {
        return sokuteiHaniEndTani1;
    }

    /**
     * 耐電圧 測定範囲エンド 単位1
     * 
     * @param sokuteiHaniEndTani1
     */
    public void setSokuteiHaniEndTani1(FXHDD01 sokuteiHaniEndTani1) {
        this.sokuteiHaniEndTani1 = sokuteiHaniEndTani1;
    }        
    
    /**
     * 耐電圧 良品範囲上限1
     * 
     * @return ryohinHani1
     */
    public FXHDD01 getRyohinHani1() {
        return ryohinHani1;
    }

    /**
     * 耐電圧 良品範囲上限1
     * 
     * @param ryohinHani1
     */
    public void setRyohinHani1(FXHDD01 ryohinHani1) {
        this.ryohinHani1 = ryohinHani1;
    }
    
    /**
     * 耐電圧 良品範囲上限 単位1
     * 
     * @return ryohinHaniTani1
     */
    public FXHDD01 getRyohinHaniTani1() {
        return ryohinHaniTani1;
    }

    /**
     * 耐電圧 良品範囲上限 単位1
     * 
     * @param ryohinHaniTani1
     */
    public void setRyohinHaniTani1(FXHDD01 ryohinHaniTani1) {
        this.ryohinHaniTani1 = ryohinHaniTani1;
    }
    
    /**
     * 耐電圧 良品範囲下限1
     * 
     * @return ryohinHaniLow1
     */
    public FXHDD01 getRyohinHaniLow1() {
        return ryohinHaniLow1;
    }

    /**
     * 耐電圧 良品範囲下限1
     * 
     * @param ryohinHaniLow1
     */
    public void setRyohinHaniLow1(FXHDD01 ryohinHaniLow1) {
        this.ryohinHaniLow1 = ryohinHaniLow1;
    }
    
        /**
     * 耐電圧 良品範囲下限 単位1
     * 
     * @return ryohinHaniLowTani1
     */
    public FXHDD01 getRyohinHaniLowTani1() {
        return ryohinHaniLowTani1;
    }

    /**
     * 耐電圧 良品範囲下限 単位1
     * 
     * @param ryohinHaniLowTani1
     */
    public void setRyohinHaniLowTani1(FXHDD01 ryohinHaniLowTani1) {
        this.ryohinHaniLowTani1 = ryohinHaniLowTani1;
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
     * @param denatsu2
     */
    public void setDenatsu2(FXHDD01 denatsu2) {
        this.denatsu2 = denatsu2;
    }

    /**
     * 耐電圧 時間2
     *
     * @return the time2
     */
    public FXHDD01 gettime2() {
        return time2;
    }

    /**
     * 耐電圧 時間2
     *
     * @param time2
     */
    public void setTime2(FXHDD01 time2) {
        this.time2 = time2;
    }

    /**
     * 耐電圧 電流中心値スタート2
     * 
     * @return denryuStart2
     */
    public FXHDD01 getDenryuStart2() {
        return denryuStart2;
    }

    /**
     * 耐電圧 電流中心値スタート2
     * 
     * @param denryuStart2
     */
    public void setDenryuStart2(FXHDD01 denryuStart2) {
        this.denryuStart2 = denryuStart2;
    }

    /**
     * 耐電圧 電流中心値スタート 単位2
     * 
     * @return denryuStartTani2
     */
    public FXHDD01 getDenryuStartTani2() {
        return denryuStartTani2;
    }

    /**
     * 耐電圧 電流中心値スタート 単位2
     * 
     * @param denryuStartTani2
     */
    public void setDenryuStartTani2(FXHDD01 denryuStartTani2) {
        this.denryuStartTani2 = denryuStartTani2;
    }
        
     /**
     * 耐電圧 電流中心値エンド2
     * 
     * @return denryuEnd2
     */
    public FXHDD01 getDenryuEnd2() {
        return denryuEnd2;
    }

    /**
     * 耐電圧 電流中心値エンド2
     * 
     * @param denryuEnd2
     */
    public void setDenryuEnd2(FXHDD01 denryuEnd2) {
        this.denryuEnd2 = denryuEnd2;
    }
       
     /**
     * 耐電圧 電流中心値エンド 単位2
     * 
     * @return denryuEndTani2
     */
    public FXHDD01 getDenryuEndTani2() {
        return denryuEndTani2;
    }

    /**
     * 耐電圧 電流中心値エンド 単位2
     * 
     * @param denryuEndTani2
     */
    public void setDenryuEndTani2(FXHDD01 denryuEndTani2) {
        this.denryuEndTani2 = denryuEndTani2;
    }
       
     /**
     * 耐電圧 測定範囲スタート2
     * 
     * @return sokuteiHaniStart2
     */
    public FXHDD01 getSokuteiHaniStart2() {
        return sokuteiHaniStart2;
    }

    /**
     * 耐電圧 測定範囲スタート2
     * 
     * @param sokuteiHaniStart2
     */
    public void setSokuteiHaniStart2(FXHDD01 sokuteiHaniStart2) {
        this.sokuteiHaniStart2 = sokuteiHaniStart2;
    }           
    
     /**
     * 耐電圧 測定範囲スタート 単位2
     * 
     * @return sokuteiHaniStartTani2
     */
    public FXHDD01 getSokuteiHaniStartTani2() {
        return sokuteiHaniStartTani2;
    }

    /**
     * 耐電圧 測定範囲スタート 単位2
     * 
     * @param sokuteiHaniStartTani2
     */
    public void setSokuteiHaniStartTani2(FXHDD01 sokuteiHaniStartTani2) {
        this.sokuteiHaniStartTani2 = sokuteiHaniStartTani2;
    }        
    
    /**
     * 耐電圧 測定範囲エンド2
     * 
     * @return sokuteiHaniEnd2
     */
    public FXHDD01 getSokuteiHaniEnd2() {
        return sokuteiHaniEnd2;
    }

    /**
     * 耐電圧 測定範囲エンド2
     * 
     * @param sokuteiHaniEnd2
     */
    public void setSokuteiHaniEnd2(FXHDD01 sokuteiHaniEnd2) {
        this.sokuteiHaniEnd2 = sokuteiHaniEnd2;
    }        

    /**
     * 耐電圧 測定範囲エンド 単位2
     * 
     * @return sokuteiHaniEndTani2
     */
    public FXHDD01 getSokuteiHaniEndTani2() {
        return sokuteiHaniEndTani2;
    }

    /**
     * 耐電圧 測定範囲エンド 単位2
     * 
     * @param sokuteiHaniEndTani2
     */
    public void setSokuteiHaniEndTani2(FXHDD01 sokuteiHaniEndTani2) {
        this.sokuteiHaniEndTani2 = sokuteiHaniEndTani2;
    }        
    
    /**
     * 耐電圧 良品範囲上限2
     * 
     * @return ryohinHani2
     */
    public FXHDD01 getRyohinHani2() {
        return ryohinHani2;
    }

    /**
     * 耐電圧 良品範囲上限2
     * 
     * @param ryohinHani2
     */
    public void setRyohinHani2(FXHDD01 ryohinHani2) {
        this.ryohinHani2 = ryohinHani2;
    }
    
    /**
     * 耐電圧 良品範囲上限 単位2
     * 
     * @return ryohinHaniTani2
     */
    public FXHDD01 getRyohinHaniTani2() {
        return ryohinHaniTani2;
    }

    /**
     * 耐電圧 良品範囲上限 単位2
     * 
     * @param ryohinHaniTani2
     */
    public void setRyohinHaniTani2(FXHDD01 ryohinHaniTani2) {
        this.ryohinHaniTani2 = ryohinHaniTani2;
    }
    
    /**
     * 耐電圧 良品範囲下限2
     * 
     * @return ryohinHaniLow2
     */
    public FXHDD01 getRyohinHaniLow2() {
        return ryohinHaniLow2;
    }

    /**
     * 耐電圧 良品範囲下限2
     * 
     * @param ryohinHaniLow2
     */
    public void setRyohinHaniLow2(FXHDD01 ryohinHaniLow2) {
        this.ryohinHaniLow2 = ryohinHaniLow2;
    }
    
        /**
     * 耐電圧 良品範囲下限 単位2
     * 
     * @return ryohinHaniLowTani2
     */
    public FXHDD01 getRyohinHaniLowTani2() {
        return ryohinHaniLowTani2;
    }

    /**
     * 耐電圧 良品範囲下限 単位2
     * 
     * @param ryohinHaniLowTani2
     */
    public void setRyohinHaniLowTani2(FXHDD01 ryohinHaniLowTani2) {
        this.ryohinHaniLowTani2 = ryohinHaniLowTani2;
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
     * @param denatsu3
     */
    public void setDenatsu3(FXHDD01 denatsu3) {
        this.denatsu3 = denatsu3;
    }

    /**
     * 耐電圧 時間3
     *
     * @return the time3
     */
    public FXHDD01 gettime3() {
        return time3;
    }

    /**
     * 耐電圧 時間3
     *
     * @param time3
     */
    public void setTime3(FXHDD01 time3) {
        this.time3 = time3;
    }

    /**
     * 耐電圧 電流中心値スタート3
     * 
     * @return denryuStart3
     */
    public FXHDD01 getDenryuStart3() {
        return denryuStart3;
    }

    /**
     * 耐電圧 電流中心値スタート3
     * 
     * @param denryuStart3
     */
    public void setDenryuStart3(FXHDD01 denryuStart3) {
        this.denryuStart3 = denryuStart3;
    }

    /**
     * 耐電圧 電流中心値スタート 単位3
     * 
     * @return denryuStartTani3
     */
    public FXHDD01 getDenryuStartTani3() {
        return denryuStartTani3;
    }

    /**
     * 耐電圧 電流中心値スタート 単位3
     * 
     * @param denryuStartTani3
     */
    public void setDenryuStartTani3(FXHDD01 denryuStartTani3) {
        this.denryuStartTani3 = denryuStartTani3;
    }
        
     /**
     * 耐電圧 電流中心値エンド3
     * 
     * @return denryuEnd3
     */
    public FXHDD01 getDenryuEnd3() {
        return denryuEnd3;
    }

    /**
     * 耐電圧 電流中心値エンド3
     * 
     * @param denryuEnd3
     */
    public void setDenryuEnd3(FXHDD01 denryuEnd3) {
        this.denryuEnd3 = denryuEnd3;
    }
       
     /**
     * 耐電圧 電流中心値エンド 単位3
     * 
     * @return denryuEndTani3
     */
    public FXHDD01 getDenryuEndTani3() {
        return denryuEndTani3;
    }

    /**
     * 耐電圧 電流中心値エンド 単位3
     * 
     * @param denryuEndTani3
     */
    public void setDenryuEndTani3(FXHDD01 denryuEndTani3) {
        this.denryuEndTani3 = denryuEndTani3;
    }
       
     /**
     * 耐電圧 測定範囲スタート3
     * 
     * @return sokuteiHaniStart3
     */
    public FXHDD01 getSokuteiHaniStart3() {
        return sokuteiHaniStart3;
    }

    /**
     * 耐電圧 測定範囲スタート3
     * 
     * @param sokuteiHaniStart3
     */
    public void setSokuteiHaniStart3(FXHDD01 sokuteiHaniStart3) {
        this.sokuteiHaniStart3 = sokuteiHaniStart3;
    }           
    
     /**
     * 耐電圧 測定範囲スタート 単位3
     * 
     * @return sokuteiHaniStartTani3
     */
    public FXHDD01 getSokuteiHaniStartTani3() {
        return sokuteiHaniStartTani3;
    }

    /**
     * 耐電圧 測定範囲スタート 単位3
     * 
     * @param sokuteiHaniStartTani3
     */
    public void setSokuteiHaniStartTani3(FXHDD01 sokuteiHaniStartTani3) {
        this.sokuteiHaniStartTani3 = sokuteiHaniStartTani3;
    }        
    
    /**
     * 耐電圧 測定範囲エンド3
     * 
     * @return sokuteiHaniEnd3
     */
    public FXHDD01 getSokuteiHaniEnd3() {
        return sokuteiHaniEnd3;
    }

    /**
     * 耐電圧 測定範囲エンド3
     * 
     * @param sokuteiHaniEnd3
     */
    public void setSokuteiHaniEnd3(FXHDD01 sokuteiHaniEnd3) {
        this.sokuteiHaniEnd3 = sokuteiHaniEnd3;
    }        

    /**
     * 耐電圧 測定範囲エンド 単位3
     * 
     * @return sokuteiHaniEndTani3
     */
    public FXHDD01 getSokuteiHaniEndTani3() {
        return sokuteiHaniEndTani3;
    }

    /**
     * 耐電圧 測定範囲エンド 単位3
     * 
     * @param sokuteiHaniEndTani3
     */
    public void setSokuteiHaniEndTani3(FXHDD01 sokuteiHaniEndTani3) {
        this.sokuteiHaniEndTani3 = sokuteiHaniEndTani3;
    }        
    
    /**
     * 耐電圧 良品範囲上限3
     * 
     * @return ryohinHani3
     */
    public FXHDD01 getRyohinHani3() {
        return ryohinHani3;
    }

    /**
     * 耐電圧 良品範囲上限3
     * 
     * @param ryohinHani3
     */
    public void setRyohinHani3(FXHDD01 ryohinHani3) {
        this.ryohinHani3 = ryohinHani3;
    }
    
    /**
     * 耐電圧 良品範囲上限 単位3
     * 
     * @return ryohinHaniTani3
     */
    public FXHDD01 getRyohinHaniTani3() {
        return ryohinHaniTani3;
    }

    /**
     * 耐電圧 良品範囲上限 単位3
     * 
     * @param ryohinHaniTani3
     */
    public void setRyohinHaniTani3(FXHDD01 ryohinHaniTani3) {
        this.ryohinHaniTani3 = ryohinHaniTani3;
    }
    
    /**
     * 耐電圧 良品範囲下限3
     * 
     * @return ryohinHaniLow3
     */
    public FXHDD01 getRyohinHaniLow3() {
        return ryohinHaniLow3;
    }

    /**
     * 耐電圧 良品範囲下限3
     * 
     * @param ryohinHaniLow3
     */
    public void setRyohinHaniLow3(FXHDD01 ryohinHaniLow3) {
        this.ryohinHaniLow3 = ryohinHaniLow3;
    }
    
        /**
     * 耐電圧 良品範囲下限 単位3
     * 
     * @return ryohinHaniLowTani3
     */
    public FXHDD01 getRyohinHaniLowTani3() {
        return ryohinHaniLowTani3;
    }

    /**
     * 耐電圧 良品範囲下限 単位3
     * 
     * @param ryohinHaniLowTani3
     */
    public void setRyohinHaniLowTani3(FXHDD01 ryohinHaniLowTani3) {
        this.ryohinHaniLowTani3 = ryohinHaniLowTani3;
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
     * @param denatsu4
     */
    public void setDenatsu4(FXHDD01 denatsu4) {
        this.denatsu4 = denatsu4;
    }

    /**
     * 耐電圧 時間4
     *
     * @return the time4
     */
    public FXHDD01 gettime4() {
        return time4;
    }

    /**
     * 耐電圧 時間4
     *
     * @param time4
     */
    public void setTime4(FXHDD01 time4) {
        this.time4 = time4;
    }

    /**
     * 耐電圧 電流中心値スタート4
     * 
     * @return denryuStart4
     */
    public FXHDD01 getDenryuStart4() {
        return denryuStart4;
    }

    /**
     * 耐電圧 電流中心値スタート4
     * 
     * @param denryuStart4
     */
    public void setDenryuStart4(FXHDD01 denryuStart4) {
        this.denryuStart4 = denryuStart4;
    }

    /**
     * 耐電圧 電流中心値スタート 単位4
     * 
     * @return denryuStartTani4
     */
    public FXHDD01 getDenryuStartTani4() {
        return denryuStartTani4;
    }

    /**
     * 耐電圧 電流中心値スタート 単位4
     * 
     * @param denryuStartTani4
     */
    public void setDenryuStartTani4(FXHDD01 denryuStartTani4) {
        this.denryuStartTani4 = denryuStartTani4;
    }
        
     /**
     * 耐電圧 電流中心値エンド4
     * 
     * @return denryuEnd4
     */
    public FXHDD01 getDenryuEnd4() {
        return denryuEnd4;
    }

    /**
     * 耐電圧 電流中心値エンド4
     * 
     * @param denryuEnd4
     */
    public void setDenryuEnd4(FXHDD01 denryuEnd4) {
        this.denryuEnd4 = denryuEnd4;
    }
       
     /**
     * 耐電圧 電流中心値エンド 単位4
     * 
     * @return denryuEndTani4
     */
    public FXHDD01 getDenryuEndTani4() {
        return denryuEndTani4;
    }

    /**
     * 耐電圧 電流中心値エンド 単位4
     * 
     * @param denryuEndTani4
     */
    public void setDenryuEndTani4(FXHDD01 denryuEndTani4) {
        this.denryuEndTani4 = denryuEndTani4;
    }
       
     /**
     * 耐電圧 測定範囲スタート4
     * 
     * @return sokuteiHaniStart4
     */
    public FXHDD01 getSokuteiHaniStart4() {
        return sokuteiHaniStart4;
    }

    /**
     * 耐電圧 測定範囲スタート4
     * 
     * @param sokuteiHaniStart4
     */
    public void setSokuteiHaniStart4(FXHDD01 sokuteiHaniStart4) {
        this.sokuteiHaniStart4 = sokuteiHaniStart4;
    }           
    
     /**
     * 耐電圧 測定範囲スタート 単位4
     * 
     * @return sokuteiHaniStartTani4
     */
    public FXHDD01 getSokuteiHaniStartTani4() {
        return sokuteiHaniStartTani4;
    }

    /**
     * 耐電圧 測定範囲スタート 単位4
     * 
     * @param sokuteiHaniStartTani4
     */
    public void setSokuteiHaniStartTani4(FXHDD01 sokuteiHaniStartTani4) {
        this.sokuteiHaniStartTani4 = sokuteiHaniStartTani4;
    }        
    
    /**
     * 耐電圧 測定範囲エンド4
     * 
     * @return sokuteiHaniEnd4
     */
    public FXHDD01 getSokuteiHaniEnd4() {
        return sokuteiHaniEnd4;
    }

    /**
     * 耐電圧 測定範囲エンド4
     * 
     * @param sokuteiHaniEnd4
     */
    public void setSokuteiHaniEnd4(FXHDD01 sokuteiHaniEnd4) {
        this.sokuteiHaniEnd4 = sokuteiHaniEnd4;
    }        

    /**
     * 耐電圧 測定範囲エンド 単位4
     * 
     * @return sokuteiHaniEndTani4
     */
    public FXHDD01 getSokuteiHaniEndTani4() {
        return sokuteiHaniEndTani4;
    }

    /**
     * 耐電圧 測定範囲エンド 単位4
     * 
     * @param sokuteiHaniEndTani4
     */
    public void setSokuteiHaniEndTani4(FXHDD01 sokuteiHaniEndTani4) {
        this.sokuteiHaniEndTani4 = sokuteiHaniEndTani4;
    }        
    
    /**
     * 耐電圧 良品範囲上限4
     * 
     * @return ryohinHani4
     */
    public FXHDD01 getRyohinHani4() {
        return ryohinHani4;
    }

    /**
     * 耐電圧 良品範囲上限4
     * 
     * @param ryohinHani4
     */
    public void setRyohinHani4(FXHDD01 ryohinHani4) {
        this.ryohinHani4 = ryohinHani4;
    }
    
    /**
     * 耐電圧 良品範囲上限 単位4
     * 
     * @return ryohinHaniTani4
     */
    public FXHDD01 getRyohinHaniTani4() {
        return ryohinHaniTani4;
    }

    /**
     * 耐電圧 良品範囲上限 単位4
     * 
     * @param ryohinHaniTani4
     */
    public void setRyohinHaniTani4(FXHDD01 ryohinHaniTani4) {
        this.ryohinHaniTani4 = ryohinHaniTani4;
    }
    
    /**
     * 耐電圧 良品範囲下限4
     * 
     * @return ryohinHaniLow4
     */
    public FXHDD01 getRyohinHaniLow4() {
        return ryohinHaniLow4;
    }

    /**
     * 耐電圧 良品範囲下限4
     * 
     * @param ryohinHaniLow4
     */
    public void setRyohinHaniLow4(FXHDD01 ryohinHaniLow4) {
        this.ryohinHaniLow4 = ryohinHaniLow4;
    }
    
        /**
     * 耐電圧 良品範囲下限 単位4
     * 
     * @return ryohinHaniLowTani4
     */
    public FXHDD01 getRyohinHaniLowTani4() {
        return ryohinHaniLowTani4;
    }

    /**
     * 耐電圧 良品範囲下限 単位4
     * 
     * @param ryohinHaniLowTani4
     */
    public void setRyohinHaniLowTani4(FXHDD01 ryohinHaniLowTani4) {
        this.ryohinHaniLowTani4 = ryohinHaniLowTani4;
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
     * @param denatsu5
     */
    public void setDenatsu5(FXHDD01 denatsu5) {
        this.denatsu5 = denatsu5;
    }

    /**
     * 耐電圧 時間5
     *
     * @return the time5
     */
    public FXHDD01 gettime5() {
        return time5;
    }

    /**
     * 耐電圧 時間5
     *
     * @param time5
     */
    public void setTime5(FXHDD01 time5) {
        this.time5 = time5;
    }

    /**
     * 耐電圧 電流中心値スタート5
     * 
     * @return denryuStart5
     */
    public FXHDD01 getDenryuStart5() {
        return denryuStart5;
    }

    /**
     * 耐電圧 電流中心値スタート5
     * 
     * @param denryuStart5
     */
    public void setDenryuStart5(FXHDD01 denryuStart5) {
        this.denryuStart5 = denryuStart5;
    }

    /**
     * 耐電圧 電流中心値スタート 単位5
     * 
     * @return denryuStartTani5
     */
    public FXHDD01 getDenryuStartTani5() {
        return denryuStartTani5;
    }

    /**
     * 耐電圧 電流中心値スタート 単位5
     * 
     * @param denryuStartTani5
     */
    public void setDenryuStartTani5(FXHDD01 denryuStartTani5) {
        this.denryuStartTani5 = denryuStartTani5;
    }
        
     /**
     * 耐電圧 電流中心値エンド5
     * 
     * @return denryuEnd5
     */
    public FXHDD01 getDenryuEnd5() {
        return denryuEnd5;
    }

    /**
     * 耐電圧 電流中心値エンド5
     * 
     * @param denryuEnd5
     */
    public void setDenryuEnd5(FXHDD01 denryuEnd5) {
        this.denryuEnd5 = denryuEnd5;
    }
       
     /**
     * 耐電圧 電流中心値エンド 単位5
     * 
     * @return denryuEndTani5
     */
    public FXHDD01 getDenryuEndTani5() {
        return denryuEndTani5;
    }

    /**
     * 耐電圧 電流中心値エンド 単位5
     * 
     * @param denryuEndTani5
     */
    public void setDenryuEndTani5(FXHDD01 denryuEndTani5) {
        this.denryuEndTani5 = denryuEndTani5;
    }
       
     /**
     * 耐電圧 測定範囲スタート5
     * 
     * @return sokuteiHaniStart5
     */
    public FXHDD01 getSokuteiHaniStart5() {
        return sokuteiHaniStart5;
    }

    /**
     * 耐電圧 測定範囲スタート5
     * 
     * @param sokuteiHaniStart5
     */
    public void setSokuteiHaniStart5(FXHDD01 sokuteiHaniStart5) {
        this.sokuteiHaniStart5 = sokuteiHaniStart5;
    }           
    
     /**
     * 耐電圧 測定範囲スタート 単位5
     * 
     * @return sokuteiHaniStartTani5
     */
    public FXHDD01 getSokuteiHaniStartTani5() {
        return sokuteiHaniStartTani5;
    }

    /**
     * 耐電圧 測定範囲スタート 単位5
     * 
     * @param sokuteiHaniStartTani5
     */
    public void setSokuteiHaniStartTani5(FXHDD01 sokuteiHaniStartTani5) {
        this.sokuteiHaniStartTani5 = sokuteiHaniStartTani5;
    }        
    
    /**
     * 耐電圧 測定範囲エンド5
     * 
     * @return sokuteiHaniEnd5
     */
    public FXHDD01 getSokuteiHaniEnd5() {
        return sokuteiHaniEnd5;
    }

    /**
     * 耐電圧 測定範囲エンド5
     * 
     * @param sokuteiHaniEnd5
     */
    public void setSokuteiHaniEnd5(FXHDD01 sokuteiHaniEnd5) {
        this.sokuteiHaniEnd5 = sokuteiHaniEnd5;
    }        

    /**
     * 耐電圧 測定範囲エンド 単位5
     * 
     * @return sokuteiHaniEndTani5
     */
    public FXHDD01 getSokuteiHaniEndTani5() {
        return sokuteiHaniEndTani5;
    }

    /**
     * 耐電圧 測定範囲エンド 単位5
     * 
     * @param sokuteiHaniEndTani5
     */
    public void setSokuteiHaniEndTani5(FXHDD01 sokuteiHaniEndTani5) {
        this.sokuteiHaniEndTani5 = sokuteiHaniEndTani5;
    }        
    
    /**
     * 耐電圧 良品範囲上限5
     * 
     * @return ryohinHani5
     */
    public FXHDD01 getRyohinHani5() {
        return ryohinHani5;
    }

    /**
     * 耐電圧 良品範囲上限5
     * 
     * @param ryohinHani5
     */
    public void setRyohinHani5(FXHDD01 ryohinHani5) {
        this.ryohinHani5 = ryohinHani5;
    }
    
    /**
     * 耐電圧 良品範囲上限 単位5
     * 
     * @return ryohinHaniTani5
     */
    public FXHDD01 getRyohinHaniTani5() {
        return ryohinHaniTani5;
    }

    /**
     * 耐電圧 良品範囲上限 単位5
     * 
     * @param ryohinHaniTani5
     */
    public void setRyohinHaniTani5(FXHDD01 ryohinHaniTani5) {
        this.ryohinHaniTani5 = ryohinHaniTani5;
    }
    
    /**
     * 耐電圧 良品範囲下限5
     * 
     * @return ryohinHaniLow5
     */
    public FXHDD01 getRyohinHaniLow5() {
        return ryohinHaniLow5;
    }

    /**
     * 耐電圧 良品範囲下限5
     * 
     * @param ryohinHaniLow5
     */
    public void setRyohinHaniLow5(FXHDD01 ryohinHaniLow5) {
        this.ryohinHaniLow5 = ryohinHaniLow5;
    }
    
        /**
     * 耐電圧 良品範囲下限 単位5
     * 
     * @return ryohinHaniLowTani5
     */
    public FXHDD01 getRyohinHaniLowTani5() {
        return ryohinHaniLowTani5;
    }

    /**
     * 耐電圧 良品範囲下限 単位5
     * 
     * @param ryohinHaniLowTani5
     */
    public void setRyohinHaniLowTani5(FXHDD01 ryohinHaniLowTani5) {
        this.ryohinHaniLowTani5 = ryohinHaniLowTani5;
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
     * @param denatsu6
     */
    public void setDenatsu6(FXHDD01 denatsu6) {
        this.denatsu6 = denatsu6;
    }

    /**
     * 耐電圧 時間6
     *
     * @return the time6
     */
    public FXHDD01 gettime6() {
        return time6;
    }

    /**
     * 耐電圧 時間6
     *
     * @param time6
     */
    public void setTime6(FXHDD01 time6) {
        this.time6 = time6;
    }

    /**
     * 耐電圧 電流中心値スタート6
     * 
     * @return denryuStart6
     */
    public FXHDD01 getDenryuStart6() {
        return denryuStart6;
    }

    /**
     * 耐電圧 電流中心値スタート6
     * 
     * @param denryuStart6
     */
    public void setDenryuStart6(FXHDD01 denryuStart6) {
        this.denryuStart6 = denryuStart6;
    }

    /**
     * 耐電圧 電流中心値スタート 単位6
     * 
     * @return denryuStartTani6
     */
    public FXHDD01 getDenryuStartTani6() {
        return denryuStartTani6;
    }

    /**
     * 耐電圧 電流中心値スタート 単位6
     * 
     * @param denryuStartTani6
     */
    public void setDenryuStartTani6(FXHDD01 denryuStartTani6) {
        this.denryuStartTani6 = denryuStartTani6;
    }
        
     /**
     * 耐電圧 電流中心値エンド6
     * 
     * @return denryuEnd6
     */
    public FXHDD01 getDenryuEnd6() {
        return denryuEnd6;
    }

    /**
     * 耐電圧 電流中心値エンド6
     * 
     * @param denryuEnd6
     */
    public void setDenryuEnd6(FXHDD01 denryuEnd6) {
        this.denryuEnd6 = denryuEnd6;
    }
       
     /**
     * 耐電圧 電流中心値エンド 単位6
     * 
     * @return denryuEndTani6
     */
    public FXHDD01 getDenryuEndTani6() {
        return denryuEndTani6;
    }

    /**
     * 耐電圧 電流中心値エンド 単位6
     * 
     * @param denryuEndTani6
     */
    public void setDenryuEndTani6(FXHDD01 denryuEndTani6) {
        this.denryuEndTani6 = denryuEndTani6;
    }
       
     /**
     * 耐電圧 測定範囲スタート6
     * 
     * @return sokuteiHaniStart6
     */
    public FXHDD01 getSokuteiHaniStart6() {
        return sokuteiHaniStart6;
    }

    /**
     * 耐電圧 測定範囲スタート6
     * 
     * @param sokuteiHaniStart6
     */
    public void setSokuteiHaniStart6(FXHDD01 sokuteiHaniStart6) {
        this.sokuteiHaniStart6 = sokuteiHaniStart6;
    }           
    
     /**
     * 耐電圧 測定範囲スタート 単位6
     * 
     * @return sokuteiHaniStartTani6
     */
    public FXHDD01 getSokuteiHaniStartTani6() {
        return sokuteiHaniStartTani6;
    }

    /**
     * 耐電圧 測定範囲スタート 単位6
     * 
     * @param sokuteiHaniStartTani6
     */
    public void setSokuteiHaniStartTani6(FXHDD01 sokuteiHaniStartTani6) {
        this.sokuteiHaniStartTani6 = sokuteiHaniStartTani6;
    }        
    
    /**
     * 耐電圧 測定範囲エンド6
     * 
     * @return sokuteiHaniEnd6
     */
    public FXHDD01 getSokuteiHaniEnd6() {
        return sokuteiHaniEnd6;
    }

    /**
     * 耐電圧 測定範囲エンド6
     * 
     * @param sokuteiHaniEnd6
     */
    public void setSokuteiHaniEnd6(FXHDD01 sokuteiHaniEnd6) {
        this.sokuteiHaniEnd6 = sokuteiHaniEnd6;
    }        

    /**
     * 耐電圧 測定範囲エンド 単位6
     * 
     * @return sokuteiHaniEndTani6
     */
    public FXHDD01 getSokuteiHaniEndTani6() {
        return sokuteiHaniEndTani6;
    }

    /**
     * 耐電圧 測定範囲エンド 単位6
     * 
     * @param sokuteiHaniEndTani6
     */
    public void setSokuteiHaniEndTani6(FXHDD01 sokuteiHaniEndTani6) {
        this.sokuteiHaniEndTani6 = sokuteiHaniEndTani6;
    }        
    
    /**
     * 耐電圧 良品範囲上限6
     * 
     * @return ryohinHani6
     */
    public FXHDD01 getRyohinHani6() {
        return ryohinHani6;
    }

    /**
     * 耐電圧 良品範囲上限6
     * 
     * @param ryohinHani6
     */
    public void setRyohinHani6(FXHDD01 ryohinHani6) {
        this.ryohinHani6 = ryohinHani6;
    }
    
    /**
     * 耐電圧 良品範囲上限 単位6
     * 
     * @return ryohinHaniTani6
     */
    public FXHDD01 getRyohinHaniTani6() {
        return ryohinHaniTani6;
    }

    /**
     * 耐電圧 良品範囲上限 単位6
     * 
     * @param ryohinHaniTani6
     */
    public void setRyohinHaniTani6(FXHDD01 ryohinHaniTani6) {
        this.ryohinHaniTani6 = ryohinHaniTani6;
    }
    
    /**
     * 耐電圧 良品範囲下限6
     * 
     * @return ryohinHaniLow6
     */
    public FXHDD01 getRyohinHaniLow6() {
        return ryohinHaniLow6;
    }

    /**
     * 耐電圧 良品範囲下限6
     * 
     * @param ryohinHaniLow6
     */
    public void setRyohinHaniLow6(FXHDD01 ryohinHaniLow6) {
        this.ryohinHaniLow6 = ryohinHaniLow6;
    }
    
        /**
     * 耐電圧 良品範囲下限 単位6
     * 
     * @return ryohinHaniLowTani6
     */
    public FXHDD01 getRyohinHaniLowTani6() {
        return ryohinHaniLowTani6;
    }

    /**
     * 耐電圧 良品範囲下限 単位6
     * 
     * @param ryohinHaniLowTani6
     */
    public void setRyohinHaniLowTani6(FXHDD01 ryohinHaniLowTani6) {
        this.ryohinHaniLowTani6 = ryohinHaniLowTani6;
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
     * @param denatsu7
     */
    public void setDenatsu7(FXHDD01 denatsu7) {
        this.denatsu7 = denatsu7;
    }

    /**
     * 耐電圧 時間7
     *
     * @return the time7
     */
    public FXHDD01 gettime7() {
        return time7;
    }

    /**
     * 耐電圧 時間7
     *
     * @param time7
     */
    public void setTime7(FXHDD01 time7) {
        this.time7 = time7;
    }

    /**
     * 耐電圧 電流中心値スタート7
     * 
     * @return denryuStart7
     */
    public FXHDD01 getDenryuStart7() {
        return denryuStart7;
    }

    /**
     * 耐電圧 電流中心値スタート7
     * 
     * @param denryuStart7
     */
    public void setDenryuStart7(FXHDD01 denryuStart7) {
        this.denryuStart7 = denryuStart7;
    }

    /**
     * 耐電圧 電流中心値スタート 単位7
     * 
     * @return denryuStartTani7
     */
    public FXHDD01 getDenryuStartTani7() {
        return denryuStartTani7;
    }

    /**
     * 耐電圧 電流中心値スタート 単位7
     * 
     * @param denryuStartTani7
     */
    public void setDenryuStartTani7(FXHDD01 denryuStartTani7) {
        this.denryuStartTani7 = denryuStartTani7;
    }
        
     /**
     * 耐電圧 電流中心値エンド7
     * 
     * @return denryuEnd7
     */
    public FXHDD01 getDenryuEnd7() {
        return denryuEnd7;
    }

    /**
     * 耐電圧 電流中心値エンド7
     * 
     * @param denryuEnd7
     */
    public void setDenryuEnd7(FXHDD01 denryuEnd7) {
        this.denryuEnd7 = denryuEnd7;
    }
       
     /**
     * 耐電圧 電流中心値エンド 単位7
     * 
     * @return denryuEndTani7
     */
    public FXHDD01 getDenryuEndTani7() {
        return denryuEndTani7;
    }

    /**
     * 耐電圧 電流中心値エンド 単位7
     * 
     * @param denryuEndTani7
     */
    public void setDenryuEndTani7(FXHDD01 denryuEndTani7) {
        this.denryuEndTani7 = denryuEndTani7;
    }
       
     /**
     * 耐電圧 測定範囲スタート7
     * 
     * @return sokuteiHaniStart7
     */
    public FXHDD01 getSokuteiHaniStart7() {
        return sokuteiHaniStart7;
    }

    /**
     * 耐電圧 測定範囲スタート7
     * 
     * @param sokuteiHaniStart7
     */
    public void setSokuteiHaniStart7(FXHDD01 sokuteiHaniStart7) {
        this.sokuteiHaniStart7 = sokuteiHaniStart7;
    }           
    
     /**
     * 耐電圧 測定範囲スタート 単位7
     * 
     * @return sokuteiHaniStartTani7
     */
    public FXHDD01 getSokuteiHaniStartTani7() {
        return sokuteiHaniStartTani7;
    }

    /**
     * 耐電圧 測定範囲スタート 単位7
     * 
     * @param sokuteiHaniStartTani7
     */
    public void setSokuteiHaniStartTani7(FXHDD01 sokuteiHaniStartTani7) {
        this.sokuteiHaniStartTani7 = sokuteiHaniStartTani7;
    }        
    
    /**
     * 耐電圧 測定範囲エンド7
     * 
     * @return sokuteiHaniEnd7
     */
    public FXHDD01 getSokuteiHaniEnd7() {
        return sokuteiHaniEnd7;
    }

    /**
     * 耐電圧 測定範囲エンド7
     * 
     * @param sokuteiHaniEnd7
     */
    public void setSokuteiHaniEnd7(FXHDD01 sokuteiHaniEnd7) {
        this.sokuteiHaniEnd7 = sokuteiHaniEnd7;
    }        

    /**
     * 耐電圧 測定範囲エンド 単位7
     * 
     * @return sokuteiHaniEndTani7
     */
    public FXHDD01 getSokuteiHaniEndTani7() {
        return sokuteiHaniEndTani7;
    }

    /**
     * 耐電圧 測定範囲エンド 単位7
     * 
     * @param sokuteiHaniEndTani7
     */
    public void setSokuteiHaniEndTani7(FXHDD01 sokuteiHaniEndTani7) {
        this.sokuteiHaniEndTani7 = sokuteiHaniEndTani7;
    }        
    
    /**
     * 耐電圧 良品範囲上限7
     * 
     * @return ryohinHani7
     */
    public FXHDD01 getRyohinHani7() {
        return ryohinHani7;
    }

    /**
     * 耐電圧 良品範囲上限7
     * 
     * @param ryohinHani7
     */
    public void setRyohinHani7(FXHDD01 ryohinHani7) {
        this.ryohinHani7 = ryohinHani7;
    }
    
    /**
     * 耐電圧 良品範囲上限 単位7
     * 
     * @return ryohinHaniTani7
     */
    public FXHDD01 getRyohinHaniTani7() {
        return ryohinHaniTani7;
    }

    /**
     * 耐電圧 良品範囲上限 単位7
     * 
     * @param ryohinHaniTani7
     */
    public void setRyohinHaniTani7(FXHDD01 ryohinHaniTani7) {
        this.ryohinHaniTani7 = ryohinHaniTani7;
    }
    
    /**
     * 耐電圧 良品範囲下限7
     * 
     * @return ryohinHaniLow7
     */
    public FXHDD01 getRyohinHaniLow7() {
        return ryohinHaniLow7;
    }

    /**
     * 耐電圧 良品範囲下限7
     * 
     * @param ryohinHaniLow7
     */
    public void setRyohinHaniLow7(FXHDD01 ryohinHaniLow7) {
        this.ryohinHaniLow7 = ryohinHaniLow7;
    }
    
        /**
     * 耐電圧 良品範囲下限 単位7
     * 
     * @return ryohinHaniLowTani7
     */
    public FXHDD01 getRyohinHaniLowTani7() {
        return ryohinHaniLowTani7;
    }

    /**
     * 耐電圧 良品範囲下限 単位7
     * 
     * @param ryohinHaniLowTani7
     */
    public void setRyohinHaniLowTani7(FXHDD01 ryohinHaniLowTani7) {
        this.ryohinHaniLowTani7 = ryohinHaniLowTani7;
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
     * @param denatsu8
     */
    public void setDenatsu8(FXHDD01 denatsu8) {
        this.denatsu8 = denatsu8;
    }

    /**
     * 耐電圧 時間8
     *
     * @return the time8
     */
    public FXHDD01 gettime8() {
        return time8;
    }

    /**
     * 耐電圧 時間8
     *
     * @param time8
     */
    public void setTime8(FXHDD01 time8) {
        this.time8 = time8;
    }

    /**
     * 耐電圧 電流中心値スタート8
     * 
     * @return denryuStart8
     */
    public FXHDD01 getDenryuStart8() {
        return denryuStart8;
    }

    /**
     * 耐電圧 電流中心値スタート8
     * 
     * @param denryuStart8
     */
    public void setDenryuStart8(FXHDD01 denryuStart8) {
        this.denryuStart8 = denryuStart8;
    }

    /**
     * 耐電圧 電流中心値スタート 単位8
     * 
     * @return denryuStartTani8
     */
    public FXHDD01 getDenryuStartTani8() {
        return denryuStartTani8;
    }

    /**
     * 耐電圧 電流中心値スタート 単位8
     * 
     * @param denryuStartTani8
     */
    public void setDenryuStartTani8(FXHDD01 denryuStartTani8) {
        this.denryuStartTani8 = denryuStartTani8;
    }
        
     /**
     * 耐電圧 電流中心値エンド8
     * 
     * @return denryuEnd8
     */
    public FXHDD01 getDenryuEnd8() {
        return denryuEnd8;
    }

    /**
     * 耐電圧 電流中心値エンド8
     * 
     * @param denryuEnd8
     */
    public void setDenryuEnd8(FXHDD01 denryuEnd8) {
        this.denryuEnd8 = denryuEnd8;
    }
       
     /**
     * 耐電圧 電流中心値エンド 単位8
     * 
     * @return denryuEndTani8
     */
    public FXHDD01 getDenryuEndTani8() {
        return denryuEndTani8;
    }

    /**
     * 耐電圧 電流中心値エンド 単位8
     * 
     * @param denryuEndTani8
     */
    public void setDenryuEndTani8(FXHDD01 denryuEndTani8) {
        this.denryuEndTani8 = denryuEndTani8;
    }
       
     /**
     * 耐電圧 測定範囲スタート8
     * 
     * @return sokuteiHaniStart8
     */
    public FXHDD01 getSokuteiHaniStart8() {
        return sokuteiHaniStart8;
    }

    /**
     * 耐電圧 測定範囲スタート8
     * 
     * @param sokuteiHaniStart8
     */
    public void setSokuteiHaniStart8(FXHDD01 sokuteiHaniStart8) {
        this.sokuteiHaniStart8 = sokuteiHaniStart8;
    }           
    
     /**
     * 耐電圧 測定範囲スタート 単位8
     * 
     * @return sokuteiHaniStartTani8
     */
    public FXHDD01 getSokuteiHaniStartTani8() {
        return sokuteiHaniStartTani8;
    }

    /**
     * 耐電圧 測定範囲スタート 単位8
     * 
     * @param sokuteiHaniStartTani8
     */
    public void setSokuteiHaniStartTani8(FXHDD01 sokuteiHaniStartTani8) {
        this.sokuteiHaniStartTani8 = sokuteiHaniStartTani8;
    }        
    
    /**
     * 耐電圧 測定範囲エンド8
     * 
     * @return sokuteiHaniEnd8
     */
    public FXHDD01 getSokuteiHaniEnd8() {
        return sokuteiHaniEnd8;
    }

    /**
     * 耐電圧 測定範囲エンド8
     * 
     * @param sokuteiHaniEnd8
     */
    public void setSokuteiHaniEnd8(FXHDD01 sokuteiHaniEnd8) {
        this.sokuteiHaniEnd8 = sokuteiHaniEnd8;
    }        

    /**
     * 耐電圧 測定範囲エンド 単位8
     * 
     * @return sokuteiHaniEndTani8
     */
    public FXHDD01 getSokuteiHaniEndTani8() {
        return sokuteiHaniEndTani8;
    }

    /**
     * 耐電圧 測定範囲エンド 単位8
     * 
     * @param sokuteiHaniEndTani8
     */
    public void setSokuteiHaniEndTani8(FXHDD01 sokuteiHaniEndTani8) {
        this.sokuteiHaniEndTani8 = sokuteiHaniEndTani8;
    }        
    
    /**
     * 耐電圧 良品範囲上限8
     * 
     * @return ryohinHani8
     */
    public FXHDD01 getRyohinHani8() {
        return ryohinHani8;
    }

    /**
     * 耐電圧 良品範囲上限8
     * 
     * @param ryohinHani8
     */
    public void setRyohinHani8(FXHDD01 ryohinHani8) {
        this.ryohinHani8 = ryohinHani8;
    }
    
    /**
     * 耐電圧 良品範囲上限 単位8
     * 
     * @return ryohinHaniTani8
     */
    public FXHDD01 getRyohinHaniTani8() {
        return ryohinHaniTani8;
    }

    /**
     * 耐電圧 良品範囲上限 単位8
     * 
     * @param ryohinHaniTani8
     */
    public void setRyohinHaniTani8(FXHDD01 ryohinHaniTani8) {
        this.ryohinHaniTani8 = ryohinHaniTani8;
    }
    
    /**
     * 耐電圧 良品範囲下限8
     * 
     * @return ryohinHaniLow8
     */
    public FXHDD01 getRyohinHaniLow8() {
        return ryohinHaniLow8;
    }

    /**
     * 耐電圧 良品範囲下限8
     * 
     * @param ryohinHaniLow8
     */
    public void setRyohinHaniLow8(FXHDD01 ryohinHaniLow8) {
        this.ryohinHaniLow8 = ryohinHaniLow8;
    }
    
        /**
     * 耐電圧 良品範囲下限 単位8
     * 
     * @return ryohinHaniLowTani8
     */
    public FXHDD01 getRyohinHaniLowTani8() {
        return ryohinHaniLowTani8;
    }

    /**
     * 耐電圧 良品範囲下限 単位8
     * 
     * @param ryohinHaniLowTani8
     */
    public void setRyohinHaniLowTani8(FXHDD01 ryohinHaniLowTani8) {
        this.ryohinHaniLowTani8 = ryohinHaniLowTani8;
    }
}
