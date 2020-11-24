/*
 * Copyright 2020 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2020/11/23<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	863 zhangjy<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * sub_sr_shinkuukansou:熱処理_ｻﾌﾞ画面のモデルクラスです。
 *
 * @author 863 zhangjy
 * @since  2020/11/23
 */
public class SubSrShinkuukansou {
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
     * 回数
     */
    private Integer kaisuu;
    /**
     * 号機①ｻﾔNo1
     */
    private Integer gouki1saya1;
    /**
     * 号機①ｻﾔNo2
     */
    private Integer gouki1saya2;
    /**
     * 号機①ｻﾔNo3
     */
    private Integer gouki1saya3;
    /**
     * 号機①ｻﾔNo4
     */
    private Integer gouki1saya4;
    /**
     * 号機①ｻﾔNo5
     */
    private Integer gouki1saya5;
    /**
     * 号機①ｻﾔNo6
     */
    private Integer gouki1saya6;
    /**
     * 号機①ｻﾔNo7
     */
    private Integer gouki1saya7;
    /**
     * 号機①ｻﾔNo8
     */
    private Integer gouki1saya8;
    /**
     * 号機①ｻﾔNo9
     */
    private Integer gouki1saya9;
    /**
     * 号機①ｻﾔNo10
     */
    private Integer gouki1saya10;
    /**
     * 号機①ﾁｪｯｸ1
     */
    private Integer gouki1check1;
    /**
     * 号機①ﾁｪｯｸ2
     */
    private Integer gouki1check2;
    /**
     * 号機①ﾁｪｯｸ3
     */
    private Integer gouki1check3;
    /**
     * 号機①ﾁｪｯｸ4
     */
    private Integer gouki1check4;
    /**
     * 号機①ﾁｪｯｸ5
     */
    private Integer gouki1check5;
    /**
     * 号機①ﾁｪｯｸ6
     */
    private Integer gouki1check6;
    /**
     * 号機①ﾁｪｯｸ7
     */
    private Integer gouki1check7;
    /**
     * 号機①ﾁｪｯｸ8
     */
    private Integer gouki1check8;
    /**
     * 号機①ﾁｪｯｸ9
     */
    private Integer gouki1check9;
    /**
     * 号機①ﾁｪｯｸ10
     */
    private Integer gouki1check10;
    /**
     * 号機②ｻﾔNo1
     */
    private Integer gouki2saya1;
    /**
     * 号機②ｻﾔNo2
     */
    private Integer gouki2saya2;
    /**
     * 号機②ｻﾔNo3
     */
    private Integer gouki2saya3;
    /**
     * 号機②ｻﾔNo4
     */
    private Integer gouki2saya4;
    /**
     * 号機②ｻﾔNo5
     */
    private Integer gouki2saya5;
    /**
     * 号機②ｻﾔNo6
     */
    private Integer gouki2saya6;
    /**
     * 号機②ｻﾔNo7
     */
    private Integer gouki2saya7;
    /**
     * 号機②ｻﾔNo8
     */
    private Integer gouki2saya8;
    /**
     * 号機②ｻﾔNo9
     */
    private Integer gouki2saya9;
    /**
     * 号機②ｻﾔNo10
     */
    private Integer gouki2saya10;
    /**
     * 号機②ﾁｪｯｸ1
     */
    private Integer gouki2check1;
    /**
     * 号機②ﾁｪｯｸ2
     */
    private Integer gouki2check2;
    /**
     * 号機②ﾁｪｯｸ3
     */
    private Integer gouki2check3;
    /**
     * 号機②ﾁｪｯｸ4
     */
    private Integer gouki2check4;
    /**
     * 号機②ﾁｪｯｸ5
     */
    private Integer gouki2check5;
    /**
     * 号機②ﾁｪｯｸ6
     */
    private Integer gouki2check6;
    /**
     * 号機②ﾁｪｯｸ7
     */
    private Integer gouki2check7;
    /**
     * 号機②ﾁｪｯｸ8
     */
    private Integer gouki2check8;
    /**
     * 号機②ﾁｪｯｸ9
     */
    private Integer gouki2check9;
    /**
     * 号機②ﾁｪｯｸ10
     */
    private Integer gouki2check10;
    /**
     * 号機③ｻﾔNo1
     */
    private Integer gouki3saya1;
    /**
     * 号機③ｻﾔNo2
     */
    private Integer gouki3saya2;
    /**
     * 号機③ｻﾔNo3
     */
    private Integer gouki3saya3;
    /**
     * 号機③ｻﾔNo4
     */
    private Integer gouki3saya4;
    /**
     * 号機③ｻﾔNo5
     */
    private Integer gouki3saya5;
    /**
     * 号機③ｻﾔNo6
     */
    private Integer gouki3saya6;
    /**
     * 号機③ｻﾔNo7
     */
    private Integer gouki3saya7;
    /**
     * 号機③ｻﾔNo8
     */
    private Integer gouki3saya8;
    /**
     * 号機③ｻﾔNo9
     */
    private Integer gouki3saya9;
    /**
     * 号機③ｻﾔNo10
     */
    private Integer gouki3saya10;
    /**
     * 号機③ﾁｪｯｸ1
     */
    private Integer gouki3check1;
    /**
     * 号機③ﾁｪｯｸ2
     */
    private Integer gouki3check2;
    /**
     * 号機③ﾁｪｯｸ3
     */
    private Integer gouki3check3;
    /**
     * 号機③ﾁｪｯｸ4
     */
    private Integer gouki3check4;
    /**
     * 号機③ﾁｪｯｸ5
     */
    private Integer gouki3check5;
    /**
     * 号機③ﾁｪｯｸ6
     */
    private Integer gouki3check6;
    /**
     * 号機③ﾁｪｯｸ7
     */
    private Integer gouki3check7;
    /**
     * 号機③ﾁｪｯｸ8
     */
    private Integer gouki3check8;
    /**
     * 号機③ﾁｪｯｸ9
     */
    private Integer gouki3check9;
    /**
     * 号機③ﾁｪｯｸ10
     */
    private Integer gouki3check10;
    /**
     * 号機④ｻﾔNo1
     */
    private Integer gouki4saya1;
    /**
     * 号機④ｻﾔNo2
     */
    private Integer gouki4saya2;
    /**
     * 号機④ｻﾔNo3
     */
    private Integer gouki4saya3;
    /**
     * 号機④ｻﾔNo4
     */
    private Integer gouki4saya4;
    /**
     * 号機④ｻﾔNo5
     */
    private Integer gouki4saya5;
    /**
     * 号機④ｻﾔNo6
     */
    private Integer gouki4saya6;
    /**
     * 号機④ｻﾔNo7
     */
    private Integer gouki4saya7;
    /**
     * 号機④ｻﾔNo8
     */
    private Integer gouki4saya8;
    /**
     * 号機④ｻﾔNo9
     */
    private Integer gouki4saya9;
    /**
     * 号機④ｻﾔNo10
     */
    private Integer gouki4saya10;
    /**
     * 号機④ﾁｪｯｸ1
     */
    private Integer gouki4check1;
    /**
     * 号機④ﾁｪｯｸ2
     */
    private Integer gouki4check2;
    /**
     * 号機④ﾁｪｯｸ3
     */
    private Integer gouki4check3;
    /**
     * 号機④ﾁｪｯｸ4
     */
    private Integer gouki4check4;
    /**
     * 号機④ﾁｪｯｸ5
     */
    private Integer gouki4check5;
    /**
     * 号機④ﾁｪｯｸ6
     */
    private Integer gouki4check6;
    /**
     * 号機④ﾁｪｯｸ7
     */
    private Integer gouki4check7;
    /**
     * 号機④ﾁｪｯｸ8
     */
    private Integer gouki4check8;
    /**
     * 号機④ﾁｪｯｸ9
     */
    private Integer gouki4check9;
    /**
     * 号機④ﾁｪｯｸ10
     */
    private Integer gouki4check10;
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
    private String revision;
    /**
     * 削除ﾌﾗｸﾞ
     */
    private Integer deleteflag;
    
    /**
     * 工場ｺｰﾄﾞ       
     *
     * @return
     */
    public String getKojyo() {
        return kojyo;
    }

    /**
     * 工場ｺｰﾄﾞ       
     *
     * @param kojyo
     */
    public void setKojyo(String kojyo) {
        this.kojyo = kojyo;
    }

    /**
     * ﾛｯﾄNo          
     *
     * @return
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * ﾛｯﾄNo          
     *
     * @param lotno
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * 枝番           
     *
     * @return
     */
    public String getEdaban() {
        return edaban;
    }

    /**
     * 枝番           
     *
     * @param edaban
     */
    public void setEdaban(String edaban) {
        this.edaban = edaban;
    }

    /**
     * 回数           
     *
     * @return
     */
    public Integer getKaisuu() {
        return kaisuu;
    }

    /**
     * 回数           
     *
     * @param kaisuu
     */
    public void setKaisuu(Integer kaisuu) {
        this.kaisuu = kaisuu;
    }

    /**
     * 号機①ｻﾔNo1    
     *
     * @return
     */
    public Integer getGouki1saya1() {
        return gouki1saya1;
    }

    /**
     * 号機①ｻﾔNo1    
     *
     * @param gouki1saya1
     */
    public void setGouki1saya1(Integer gouki1saya1) {
        this.gouki1saya1 = gouki1saya1;
    }

    /**
     * 号機①ｻﾔNo2    
     *
     * @return
     */
    public Integer getGouki1saya2() {
        return gouki1saya2;
    }

    /**
     * 号機①ｻﾔNo2    
     *
     * @param gouki1saya2
     */
    public void setGouki1saya2(Integer gouki1saya2) {
        this.gouki1saya2 = gouki1saya2;
    }

    /**
     * 号機①ｻﾔNo3    
     *
     * @return
     */
    public Integer getGouki1saya3() {
        return gouki1saya3;
    }

    /**
     * 号機①ｻﾔNo3    
     *
     * @param gouki1saya3
     */
    public void setGouki1saya3(Integer gouki1saya3) {
        this.gouki1saya3 = gouki1saya3;
    }

    /**
     * 号機①ｻﾔNo4    
     *
     * @return
     */
    public Integer getGouki1saya4() {
        return gouki1saya4;
    }

    /**
     * 号機①ｻﾔNo4    
     *
     * @param gouki1saya4
     */
    public void setGouki1saya4(Integer gouki1saya4) {
        this.gouki1saya4 = gouki1saya4;
    }

    /**
     * 号機①ｻﾔNo5    
     *
     * @return
     */
    public Integer getGouki1saya5() {
        return gouki1saya5;
    }

    /**
     * 号機①ｻﾔNo5    
     *
     * @param gouki1saya5
     */
    public void setGouki1saya5(Integer gouki1saya5) {
        this.gouki1saya5 = gouki1saya5;
    }

    /**
     * 号機①ｻﾔNo6    
     *
     * @return
     */
    public Integer getGouki1saya6() {
        return gouki1saya6;
    }

    /**
     * 号機①ｻﾔNo6    
     *
     * @param gouki1saya6
     */
    public void setGouki1saya6(Integer gouki1saya6) {
        this.gouki1saya6 = gouki1saya6;
    }

    /**
     * 号機①ｻﾔNo7    
     *
     * @return
     */
    public Integer getGouki1saya7() {
        return gouki1saya7;
    }

    /**
     * 号機①ｻﾔNo7    
     *
     * @param gouki1saya7
     */
    public void setGouki1saya7(Integer gouki1saya7) {
        this.gouki1saya7 = gouki1saya7;
    }

    /**
     * 号機①ｻﾔNo8    
     *
     * @return
     */
    public Integer getGouki1saya8() {
        return gouki1saya8;
    }

    /**
     * 号機①ｻﾔNo8    
     *
     * @param gouki1saya8
     */
    public void setGouki1saya8(Integer gouki1saya8) {
        this.gouki1saya8 = gouki1saya8;
    }

    /**
     * 号機①ｻﾔNo9    
     *
     * @return
     */
    public Integer getGouki1saya9() {
        return gouki1saya9;
    }

    /**
     * 号機①ｻﾔNo9    
     *
     * @param gouki1saya9
     */
    public void setGouki1saya9(Integer gouki1saya9) {
        this.gouki1saya9 = gouki1saya9;
    }

    /**
     * 号機①ｻﾔNo10   
     *
     * @return
     */
    public Integer getGouki1saya10() {
        return gouki1saya10;
    }

    /**
     * 号機①ｻﾔNo10   
     *
     * @param gouki1saya10
     */
    public void setGouki1saya10(Integer gouki1saya10) {
        this.gouki1saya10 = gouki1saya10;
    }

    /**
     * 号機①ﾁｪｯｸ1    
     *
     * @return
     */
    public Integer getGouki1check1() {
        return gouki1check1;
    }

    /**
     * 号機①ﾁｪｯｸ1    
     *
     * @param gouki1check1
     */
    public void setGouki1check1(Integer gouki1check1) {
        this.gouki1check1 = gouki1check1;
    }

    /**
     * 号機①ﾁｪｯｸ2    
     *
     * @return
     */
    public Integer getGouki1check2() {
        return gouki1check2;
    }

    /**
     * 号機①ﾁｪｯｸ2    
     *
     * @param gouki1check2
     */
    public void setGouki1check2(Integer gouki1check2) {
        this.gouki1check2 = gouki1check2;
    }

    /**
     * 号機①ﾁｪｯｸ3    
     *
     * @return
     */
    public Integer getGouki1check3() {
        return gouki1check3;
    }

    /**
     * 号機①ﾁｪｯｸ3    
     *
     * @param gouki1check3
     */
    public void setGouki1check3(Integer gouki1check3) {
        this.gouki1check3 = gouki1check3;
    }

    /**
     * 号機①ﾁｪｯｸ4    
     *
     * @return
     */
    public Integer getGouki1check4() {
        return gouki1check4;
    }

    /**
     * 号機①ﾁｪｯｸ4    
     *
     * @param gouki1check4
     */
    public void setGouki1check4(Integer gouki1check4) {
        this.gouki1check4 = gouki1check4;
    }

    /**
     * 号機①ﾁｪｯｸ5    
     *
     * @return
     */
    public Integer getGouki1check5() {
        return gouki1check5;
    }

    /**
     * 号機①ﾁｪｯｸ5    
     *
     * @param gouki1check5
     */
    public void setGouki1check5(Integer gouki1check5) {
        this.gouki1check5 = gouki1check5;
    }

    /**
     * 号機①ﾁｪｯｸ6    
     *
     * @return
     */
    public Integer getGouki1check6() {
        return gouki1check6;
    }

    /**
     * 号機①ﾁｪｯｸ6    
     *
     * @param gouki1check6
     */
    public void setGouki1check6(Integer gouki1check6) {
        this.gouki1check6 = gouki1check6;
    }

    /**
     * 号機①ﾁｪｯｸ7    
     *
     * @return
     */
    public Integer getGouki1check7() {
        return gouki1check7;
    }

    /**
     * 号機①ﾁｪｯｸ7    
     *
     * @param gouki1check7
     */
    public void setGouki1check7(Integer gouki1check7) {
        this.gouki1check7 = gouki1check7;
    }

    /**
     * 号機①ﾁｪｯｸ8    
     *
     * @return
     */
    public Integer getGouki1check8() {
        return gouki1check8;
    }

    /**
     * 号機①ﾁｪｯｸ8    
     *
     * @param gouki1check8
     */
    public void setGouki1check8(Integer gouki1check8) {
        this.gouki1check8 = gouki1check8;
    }

    /**
     * 号機①ﾁｪｯｸ9    
     *
     * @return
     */
    public Integer getGouki1check9() {
        return gouki1check9;
    }

    /**
     * 号機①ﾁｪｯｸ9    
     *
     * @param gouki1check9
     */
    public void setGouki1check9(Integer gouki1check9) {
        this.gouki1check9 = gouki1check9;
    }

    /**
     * 号機①ﾁｪｯｸ10   
     *
     * @return
     */
    public Integer getGouki1check10() {
        return gouki1check10;
    }

    /**
     * 号機①ﾁｪｯｸ10   
     *
     * @param gouki1check10
     */
    public void setGouki1check10(Integer gouki1check10) {
        this.gouki1check10 = gouki1check10;
    }

    /**
     * 号機②ｻﾔNo1    
     *
     * @return
     */
    public Integer getGouki2saya1() {
        return gouki2saya1;
    }

    /**
     * 号機②ｻﾔNo1    
     *
     * @param gouki2saya1
     */
    public void setGouki2saya1(Integer gouki2saya1) {
        this.gouki2saya1 = gouki2saya1;
    }

    /**
     * 号機②ｻﾔNo2    
     *
     * @return
     */
    public Integer getGouki2saya2() {
        return gouki2saya2;
    }

    /**
     * 号機②ｻﾔNo2    
     *
     * @param gouki2saya2
     */
    public void setGouki2saya2(Integer gouki2saya2) {
        this.gouki2saya2 = gouki2saya2;
    }

    /**
     * 号機②ｻﾔNo3    
     *
     * @return
     */
    public Integer getGouki2saya3() {
        return gouki2saya3;
    }

    /**
     * 号機②ｻﾔNo3    
     *
     * @param gouki2saya3
     */
    public void setGouki2saya3(Integer gouki2saya3) {
        this.gouki2saya3 = gouki2saya3;
    }

    /**
     * 号機②ｻﾔNo4    
     *
     * @return
     */
    public Integer getGouki2saya4() {
        return gouki2saya4;
    }

    /**
     * 号機②ｻﾔNo4    
     *
     * @param gouki2saya4
     */
    public void setGouki2saya4(Integer gouki2saya4) {
        this.gouki2saya4 = gouki2saya4;
    }

    /**
     * 号機②ｻﾔNo5    
     *
     * @return
     */
    public Integer getGouki2saya5() {
        return gouki2saya5;
    }

    /**
     * 号機②ｻﾔNo5    
     *
     * @param gouki2saya5
     */
    public void setGouki2saya5(Integer gouki2saya5) {
        this.gouki2saya5 = gouki2saya5;
    }

    /**
     * 号機②ｻﾔNo6    
     *
     * @return
     */
    public Integer getGouki2saya6() {
        return gouki2saya6;
    }

    /**
     * 号機②ｻﾔNo6    
     *
     * @param gouki2saya6
     */ 
    public void setGouki2saya6(Integer gouki2saya6) {
        this.gouki2saya6 = gouki2saya6;
    }

    /**
     * 号機②ｻﾔNo7    
     *
     * @return
     */
    public Integer getGouki2saya7() {
        return gouki2saya7;
    }

    /**
     * 号機②ｻﾔNo7    
     *
     * @param gouki2saya7
     */
    public void setGouki2saya7(Integer gouki2saya7) {
        this.gouki2saya7 = gouki2saya7;
    }

    /**
     * 号機②ｻﾔNo8    
     *
     * @return
     */
    public Integer getGouki2saya8() {
        return gouki2saya8;
    }

    /**
     * 号機②ｻﾔNo8    
     *
     * @param gouki2saya8
     */
    public void setGouki2saya8(Integer gouki2saya8) {
        this.gouki2saya8 = gouki2saya8;
    }

    /**
     * 号機②ｻﾔNo9    
     *
     * @return
     */
    public Integer getGouki2saya9() {
        return gouki2saya9;
    }

    /**
     * 号機②ｻﾔNo9    
     *
     * @param gouki2saya9
     */
    public void setGouki2saya9(Integer gouki2saya9) {
        this.gouki2saya9 = gouki2saya9;
    }

    /**
     * 号機②ｻﾔNo10   
     *
     * @return
     */
    public Integer getGouki2saya10() {
        return gouki2saya10;
    }

    /**
     * 号機②ｻﾔNo10   
     *
     * @param gouki2saya10
     */
    public void setGouki2saya10(Integer gouki2saya10) {
        this.gouki2saya10 = gouki2saya10;
    }

    /**
     * 号機②ﾁｪｯｸ1    
     *
     * @return
     */
    public Integer getGouki2check1() {
        return gouki2check1;
    }

    /**
     * 号機②ﾁｪｯｸ1    
     *
     * @param gouki2check1
     */
    public void setGouki2check1(Integer gouki2check1) {
        this.gouki2check1 = gouki2check1;
    }

    /**
     * 号機②ﾁｪｯｸ2    
     *
     * @return
     */
    public Integer getGouki2check2() {
        return gouki2check2;
    }

    /**
     * 号機②ﾁｪｯｸ2    
     *
     * @param gouki2check2
     */
    public void setGouki2check2(Integer gouki2check2) {
        this.gouki2check2 = gouki2check2;
    }

    /**
     * 号機②ﾁｪｯｸ3    
     *
     * @return
     */
    public Integer getGouki2check3() {
        return gouki2check3;
    }

    /**
     * 号機②ﾁｪｯｸ3    
     *
     * @param gouki2check3
     */
    public void setGouki2check3(Integer gouki2check3) {
        this.gouki2check3 = gouki2check3;
    }

    /**
     * 号機②ﾁｪｯｸ4    
     *
     * @return
     */
    public Integer getGouki2check4() {
        return gouki2check4;
    }

    /**
     * 号機②ﾁｪｯｸ4    
     *
     * @param gouki2check4
     */
    public void setGouki2check4(Integer gouki2check4) {
        this.gouki2check4 = gouki2check4;
    }

    /**
     * 号機②ﾁｪｯｸ5    
     *
     * @return
     */
    public Integer getGouki2check5() {
        return gouki2check5;
    }

    /**
     * 号機②ﾁｪｯｸ5    
     *
     * @param gouki2check5
     */
    public void setGouki2check5(Integer gouki2check5) {
        this.gouki2check5 = gouki2check5;
    }

    /**
     * 号機②ﾁｪｯｸ6    
     *
     * @return
     */
    public Integer getGouki2check6() {
        return gouki2check6;
    }

    /**
     * 号機②ﾁｪｯｸ6    
     *
     * @param gouki2check6
     */
    public void setGouki2check6(Integer gouki2check6) {
        this.gouki2check6 = gouki2check6;
    }

    /**
     * 号機②ﾁｪｯｸ7    
     *
     * @return
     */
    public Integer getGouki2check7() {
        return gouki2check7;
    }

    /**
     * 号機②ﾁｪｯｸ7    
     *
     * @param gouki2check7
     */
    public void setGouki2check7(Integer gouki2check7) {
        this.gouki2check7 = gouki2check7;
    }

    /**
     * 号機②ﾁｪｯｸ8    
     *
     * @return
     */
    public Integer getGouki2check8() {
        return gouki2check8;
    }

    /**
     * 号機②ﾁｪｯｸ8    
     *
     * @param gouki2check8
     */
    public void setGouki2check8(Integer gouki2check8) {
        this.gouki2check8 = gouki2check8;
    }

    /**
     * 号機②ﾁｪｯｸ9    
     *
     * @return
     */
    public Integer getGouki2check9() {
        return gouki2check9;
    }

    /**
     * 号機②ﾁｪｯｸ9    
     *
     * @param gouki2check9
     */
    public void setGouki2check9(Integer gouki2check9) {
        this.gouki2check9 = gouki2check9;
    }

    /**
     * 号機②ﾁｪｯｸ10   
     *
     * @return
     */
    public Integer getGouki2check10() {
        return gouki2check10;
    }

    /**
     * 号機②ﾁｪｯｸ10   
     *
     * @param gouki2check10
     */
    public void setGouki2check10(Integer gouki2check10) {
        this.gouki2check10 = gouki2check10;
    }

    /**
     * 号機③ｻﾔNo1    
     *
     * @return
     */
    public Integer getGouki3saya1() {
        return gouki3saya1;
    }

    /**
     * 号機③ｻﾔNo1    
     *
     * @param gouki3saya1
     */
    public void setGouki3saya1(Integer gouki3saya1) {
        this.gouki3saya1 = gouki3saya1;
    }

    /**
     * 号機③ｻﾔNo2    
     *
     * @return
     */
    public Integer getGouki3saya2() {
        return gouki3saya2;
    }

    /**
     * 号機③ｻﾔNo2    
     *
     * @param gouki3saya2
     */
    public void setGouki3saya2(Integer gouki3saya2) {
        this.gouki3saya2 = gouki3saya2;
    }

    /**
     * 号機③ｻﾔNo3    
     *
     * @return
     */
    public Integer getGouki3saya3() {
        return gouki3saya3;
    }

    /**
     * 号機③ｻﾔNo3    
     *
     * @param gouki3saya3
     */
    public void setGouki3saya3(Integer gouki3saya3) {
        this.gouki3saya3 = gouki3saya3;
    }

    /**
     * 号機③ｻﾔNo4    
     *
     * @return
     */
    public Integer getGouki3saya4() {
        return gouki3saya4;
    }

    /**
     * 号機③ｻﾔNo4    
     *
     * @param gouki3saya4
     */
    public void setGouki3saya4(Integer gouki3saya4) {
        this.gouki3saya4 = gouki3saya4;
    }

    /**
     * 号機③ｻﾔNo5    
     *
     * @return
     */
    public Integer getGouki3saya5() {
        return gouki3saya5;
    }

    /**
     * 号機③ｻﾔNo5    
     *
     * @param gouki3saya5
     */
    public void setGouki3saya5(Integer gouki3saya5) {
        this.gouki3saya5 = gouki3saya5;
    }

    /**
     * 号機③ｻﾔNo6    
     *
     * @return
     */
    public Integer getGouki3saya6() {
        return gouki3saya6;
    }

    /**
     * 号機③ｻﾔNo6    
     *
     * @param gouki3saya6
     */
    public void setGouki3saya6(Integer gouki3saya6) {
        this.gouki3saya6 = gouki3saya6;
    }

    /**
     * 号機③ｻﾔNo7    
     *
     * @return
     */
    public Integer getGouki3saya7() {
        return gouki3saya7;
    }

    /**
     * 号機③ｻﾔNo7    
     *
     * @param gouki3saya7
     */
    public void setGouki3saya7(Integer gouki3saya7) {
        this.gouki3saya7 = gouki3saya7;
    }

    /**
     * 号機③ｻﾔNo8    
     *
     * @return
     */
    public Integer getGouki3saya8() {
        return gouki3saya8;
    }

    /**
     * 号機③ｻﾔNo8    
     *
     * @param gouki3saya8
     */
    public void setGouki3saya8(Integer gouki3saya8) {
        this.gouki3saya8 = gouki3saya8;
    }

    /**
     * 号機③ｻﾔNo9    
     *
     * @return
     */
    public Integer getGouki3saya9() {
        return gouki3saya9;
    }

    /**
     * 号機③ｻﾔNo9    
     *
     * @param gouki3saya9
     */
    public void setGouki3saya9(Integer gouki3saya9) {
        this.gouki3saya9 = gouki3saya9;
    }

    /**
     * 号機③ｻﾔNo10   
     *
     * @return
     */
    public Integer getGouki3saya10() {
        return gouki3saya10;
    }

    /**
     * 号機③ｻﾔNo10   
     *
     * @param gouki3saya10
     */
    public void setGouki3saya10(Integer gouki3saya10) {
        this.gouki3saya10 = gouki3saya10;
    }

    /**
     * 号機③ﾁｪｯｸ1    
     *
     * @return
     */
    public Integer getGouki3check1() {
        return gouki3check1;
    }

    /**
     * 号機③ﾁｪｯｸ1    
     *
     * @param gouki3check1
     */
    public void setGouki3check1(Integer gouki3check1) {
        this.gouki3check1 = gouki3check1;
    }

    /**
     * 号機③ﾁｪｯｸ2    
     *
     * @return
     */
    public Integer getGouki3check2() {
        return gouki3check2;
    }

    /**
     * 号機③ﾁｪｯｸ2    
     *
     * @param gouki3check2
     */
    public void setGouki3check2(Integer gouki3check2) {
        this.gouki3check2 = gouki3check2;
    }

    /**
     * 号機③ﾁｪｯｸ3    
     *
     * @return
     */
    public Integer getGouki3check3() {
        return gouki3check3;
    }

    /**
     * 号機③ﾁｪｯｸ3    
     *
     * @param gouki3check3
     */
    public void setGouki3check3(Integer gouki3check3) {
        this.gouki3check3 = gouki3check3;
    }

    /**
     * 号機③ﾁｪｯｸ4    
     *
     * @return
     */
    public Integer getGouki3check4() {
        return gouki3check4;
    }

    /**
     * 号機③ﾁｪｯｸ4    
     *
     * @param gouki3check4
     */
    public void setGouki3check4(Integer gouki3check4) {
        this.gouki3check4 = gouki3check4;
    }

    /**
     * 号機③ﾁｪｯｸ5    
     *
     * @return
     */
    public Integer getGouki3check5() {
        return gouki3check5;
    }

    /**
     * 号機③ﾁｪｯｸ5    
     *
     * @param gouki3check5
     */
    public void setGouki3check5(Integer gouki3check5) {
        this.gouki3check5 = gouki3check5;
    }

    /**
     * 号機③ﾁｪｯｸ6    
     *
     * @return
     */
    public Integer getGouki3check6() {
        return gouki3check6;
    }

    /**
     * 号機③ﾁｪｯｸ6    
     *
     * @param gouki3check6
     */
    public void setGouki3check6(Integer gouki3check6) {
        this.gouki3check6 = gouki3check6;
    }

    /**
     * 号機③ﾁｪｯｸ7    
     *
     * @return
     */
    public Integer getGouki3check7() {
        return gouki3check7;
    }

    /**
     * 号機③ﾁｪｯｸ7    
     *
     * @param gouki3check7
     */
    public void setGouki3check7(Integer gouki3check7) {
        this.gouki3check7 = gouki3check7;
    }

    /**
     * 号機③ﾁｪｯｸ8    
     *
     * @return
     */
    public Integer getGouki3check8() {
        return gouki3check8;
    }

    /**
     * 号機③ﾁｪｯｸ8    
     *
     * @param gouki3check8
     */
    public void setGouki3check8(Integer gouki3check8) {
        this.gouki3check8 = gouki3check8;
    }

    /**
     * 号機③ﾁｪｯｸ9    
     *
     * @return
     */
    public Integer getGouki3check9() {
        return gouki3check9;
    }

    /**
     * 号機③ﾁｪｯｸ9    
     *
     * @param gouki3check9
     */
    public void setGouki3check9(Integer gouki3check9) {
        this.gouki3check9 = gouki3check9;
    }

    /**
     * 号機③ﾁｪｯｸ10   
     *
     * @return
     */
    public Integer getGouki3check10() {
        return gouki3check10;
    }

    /**
     * 号機③ﾁｪｯｸ10   
     *
     * @param gouki3check10
     */
    public void setGouki3check10(Integer gouki3check10) {
        this.gouki3check10 = gouki3check10;
    }

    /**
     * 号機④ｻﾔNo1    
     *
     * @return
     */
    public Integer getGouki4saya1() {
        return gouki4saya1;
    }

    /**
     * 号機④ｻﾔNo1    
     *
     * @param gouki4saya1
     */
    public void setGouki4saya1(Integer gouki4saya1) {
        this.gouki4saya1 = gouki4saya1;
    }

    /**
     * 号機④ｻﾔNo2    
     *
     * @return
     */
    public Integer getGouki4saya2() {
        return gouki4saya2;
    }

    /**
     * 号機④ｻﾔNo2    
     *
     * @param gouki4saya2
     */
    public void setGouki4saya2(Integer gouki4saya2) {
        this.gouki4saya2 = gouki4saya2;
    }

    /**
     * 号機④ｻﾔNo3    
     *
     * @return
     */
    public Integer getGouki4saya3() {
        return gouki4saya3;
    }

    /**
     * 号機④ｻﾔNo3    
     *
     * @param gouki4saya3
     */
    public void setGouki4saya3(Integer gouki4saya3) {
        this.gouki4saya3 = gouki4saya3;
    }

    /**
     * 号機④ｻﾔNo4    
     *
     * @return
     */
    public Integer getGouki4saya4() {
        return gouki4saya4;
    }

    /**
     * 号機④ｻﾔNo4    
     *
     * @param gouki4saya4
     */
    public void setGouki4saya4(Integer gouki4saya4) {
        this.gouki4saya4 = gouki4saya4;
    }

    /**
     * 号機④ｻﾔNo5    
     *
     * @return
     */
    public Integer getGouki4saya5() {
        return gouki4saya5;
    }

    /**
     * 号機④ｻﾔNo5    
     *
     * @param gouki4saya5
     */
    public void setGouki4saya5(Integer gouki4saya5) {
        this.gouki4saya5 = gouki4saya5;
    }

    /**
     * 号機④ｻﾔNo6    
     *
     * @return
     */
    public Integer getGouki4saya6() {
        return gouki4saya6;
    }

    /**
     * 号機④ｻﾔNo6    
     *
     * @param gouki4saya6
     */
    public void setGouki4saya6(Integer gouki4saya6) {
        this.gouki4saya6 = gouki4saya6;
    }

    /**
     * 号機④ｻﾔNo7    
     *
     * @return
     */
    public Integer getGouki4saya7() {
        return gouki4saya7;
    }

    /**
     * 号機④ｻﾔNo7    
     *
     * @param gouki4saya7
     */
    public void setGouki4saya7(Integer gouki4saya7) {
        this.gouki4saya7 = gouki4saya7;
    }

    /**
     * 号機④ｻﾔNo8    
     *
     * @return
     */
    public Integer getGouki4saya8() {
        return gouki4saya8;
    }

    /**
     * 号機④ｻﾔNo8    
     *
     * @param gouki4saya8
     */
    public void setGouki4saya8(Integer gouki4saya8) {
        this.gouki4saya8 = gouki4saya8;
    }

    /**
     * 号機④ｻﾔNo9    
     *
     * @return
     */
    public Integer getGouki4saya9() {
        return gouki4saya9;
    }

    /**
     * 号機④ｻﾔNo9    
     *
     * @param gouki4saya9
     */
    public void setGouki4saya9(Integer gouki4saya9) {
        this.gouki4saya9 = gouki4saya9;
    }

    /**
     * 号機④ｻﾔNo10   
     *
     * @return
     */
    public Integer getGouki4saya10() {
        return gouki4saya10;
    }

    /**
     * 号機④ｻﾔNo10   
     *
     * @param gouki4saya10
     */
    public void setGouki4saya10(Integer gouki4saya10) {
        this.gouki4saya10 = gouki4saya10;
    }

    /**
     * 号機④ﾁｪｯｸ1    
     *
     * @return
     */
    public Integer getGouki4check1() {
        return gouki4check1;
    }

    /**
     * 号機④ﾁｪｯｸ1    
     *
     * @param gouki4check1
     */
    public void setGouki4check1(Integer gouki4check1) {
        this.gouki4check1 = gouki4check1;
    }

    /**
     * 号機④ﾁｪｯｸ2    
     *
     * @return
     */
    public Integer getGouki4check2() {
        return gouki4check2;
    }

    /**
     * 号機④ﾁｪｯｸ2    
     *
     * @param gouki4check2
     */
    public void setGouki4check2(Integer gouki4check2) {
        this.gouki4check2 = gouki4check2;
    }

    /**
     * 号機④ﾁｪｯｸ3    
     *
     * @return
     */
    public Integer getGouki4check3() {
        return gouki4check3;
    }

    /**
     * 号機④ﾁｪｯｸ3    
     *
     * @param gouki4check3
     */
    public void setGouki4check3(Integer gouki4check3) {
        this.gouki4check3 = gouki4check3;
    }

    /**
     * 号機④ﾁｪｯｸ4    
     *
     * @return
     */
    public Integer getGouki4check4() {
        return gouki4check4;
    }

    /**
     * 号機④ﾁｪｯｸ4    
     *
     * @param gouki4check4
     */
    public void setGouki4check4(Integer gouki4check4) {
        this.gouki4check4 = gouki4check4;
    }

    /**
     * 号機④ﾁｪｯｸ5    
     *
     * @return
     */
    public Integer getGouki4check5() {
        return gouki4check5;
    }

    /**
     * 号機④ﾁｪｯｸ5    
     *
     * @param gouki4check5
     */
    public void setGouki4check5(Integer gouki4check5) {
        this.gouki4check5 = gouki4check5;
    }

    /**
     * 号機④ﾁｪｯｸ6    
     *
     * @return
     */
    public Integer getGouki4check6() {
        return gouki4check6;
    }

    /**
     * 号機④ﾁｪｯｸ6    
     *
     * @param gouki4check6
     */
    public void setGouki4check6(Integer gouki4check6) {
        this.gouki4check6 = gouki4check6;
    }

    /**
     * 号機④ﾁｪｯｸ7    
     *
     * @return
     */
    public Integer getGouki4check7() {
        return gouki4check7;
    }

    /**
     * 号機④ﾁｪｯｸ7    
     *
     * @param gouki4check7
     */
    public void setGouki4check7(Integer gouki4check7) {
        this.gouki4check7 = gouki4check7;
    }

    /**
     * 号機④ﾁｪｯｸ8    
     *
     * @return
     */
    public Integer getGouki4check8() {
        return gouki4check8;
    }

    /**
     * 号機④ﾁｪｯｸ8    
     *
     * @param gouki4check8
     */
    public void setGouki4check8(Integer gouki4check8) {
        this.gouki4check8 = gouki4check8;
    }

    /**
     * 号機④ﾁｪｯｸ9    
     *
     * @return
     */
    public Integer getGouki4check9() {
        return gouki4check9;
    }

    /**
     * 号機④ﾁｪｯｸ9    
     *
     * @param gouki4check9
     */
    public void setGouki4check9(Integer gouki4check9) {
        this.gouki4check9 = gouki4check9;
    }

    /**
     * 号機④ﾁｪｯｸ10   
     *
     * @return
     */
    public Integer getGouki4check10() {
        return gouki4check10;
    }

    /**
     * 号機④ﾁｪｯｸ10   
     *
     * @param gouki4check10
     */
    public void setGouki4check10(Integer gouki4check10) {
        this.gouki4check10 = gouki4check10;
    }

    /**
     * 登録日時       
     *
     * @return
     */
    public Timestamp getTorokunichiji() {
        return torokunichiji;
    }

    /**
     * 登録日時       
     *
     * @param torokunichiji
     */
    public void setTorokunichiji(Timestamp torokunichiji) {
        this.torokunichiji = torokunichiji;
    }

    /**
     * 更新日時       
     *
     * @return
     */
    public Timestamp getKosinnichiji() {
        return kosinnichiji;
    }

    /**
     * 更新日時       
     *
     * @param kosinnichiji
     */
    public void setKosinnichiji(Timestamp kosinnichiji) {
        this.kosinnichiji = kosinnichiji;
    }

    /**
     * revision       
     *
     * @return
     */
    public String getRevision() {
        return revision;
    }

    /**
     * revision       
     *
     * @param revision
     */
    public void setRevision(String revision) {
        this.revision = revision;
    }

    /**
     * 削除ﾌﾗｸﾞ       
     *
     * @return
     */
    public Integer getDeleteflag() {
        return deleteflag;
    }

    /**
     * 削除ﾌﾗｸﾞ       
     *
     * @param deleteflag
     */
    public void setDeleteflag(Integer deleteflag) {
        this.deleteflag = deleteflag;
    }
}