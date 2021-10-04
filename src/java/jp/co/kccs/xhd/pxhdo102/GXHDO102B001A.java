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
 * 変更日	2021/08/19<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS wxf<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B001(ｶﾞﾗｽ作製・秤量)
 *
 * @author KCSS wxf
 * @since 2021/08/19
 */
@ViewScoped
@Named
public class GXHDO102B001A implements Serializable {

    /**
     * WIPﾛｯﾄNo
     */
    private FXHDD01 wiplotno;

    /**
     * ｶﾞﾗｽ品名
     */
    private FXHDD01 glasshinmei;

    /**
     * ｶﾞﾗｽLotNo
     */
    private FXHDD01 glasslotno;

    /**
     * ﾛｯﾄ区分
     */
    private FXHDD01 lotkubun;

    /**
     * 秤量号機
     */
    private FXHDD01 goki;

    /**
     * 材料品名1
     */
    private FXHDD01 zairyohinmei1;

    /**
     * 調合量規格1
     */
    private FXHDD01 tyogouryoukikaku1;

    /**
     * 部材在庫No1_1
     */
    private FXHDD01 sizailotno1_1;

    /**
     * 調合量1_1
     */
    private FXHDD01 tyougouryou1_1;
    
    /**
     * 部材在庫No1_2
     */
    private FXHDD01 sizailotno1_2;

    /**
     * 調合量1_2
     */
    private FXHDD01 tyougouryou1_2;
    
    /**
     * 材料品名2
     */
    private FXHDD01 zairyohinmei2;

    /**
     * 調合量規格2
     */
    private FXHDD01 tyogouryoukikaku2;

    /**
     * 部材在庫No2_1
     */
    private FXHDD01 sizailotno2_1;

    /**
     * 調合量2_1
     */
    private FXHDD01 tyougouryou2_1;
    
    /**
     * 部材在庫No2_2
     */
    private FXHDD01 sizailotno2_2;

    /**
     * 調合量2_2
     */
    private FXHDD01 tyougouryou2_2;

    /**
     * 秤量日
     */
    private FXHDD01 keiryounichiji_day;

    /**
     * 秤量時間
     */
    private FXHDD01 keiryounichiji_time;

    /**
     * 担当者
     */
    private FXHDD01 tantousya;

    /**
     * 確認者
     */
    private FXHDD01 kakuninsya;

    /**
     * 備考1
     */
    private FXHDD01 bikou1;

    /**
     * 備考2
     */
    private FXHDD01 bikou2;

    /**
     * コンストラクタ
     */
    public GXHDO102B001A() {
    }

    /**
     * WIPﾛｯﾄNo
     * @return the wiplotno
     */
    public FXHDD01 getWiplotno() {
        return wiplotno;
    }

    /**
     * WIPﾛｯﾄNo
     * @param wiplotno the wiplotno to set
     */
    public void setWiplotno(FXHDD01 wiplotno) {
        this.wiplotno = wiplotno;
    }

    /**
     * ｶﾞﾗｽ品名
     * @return the glasshinmei
     */
    public FXHDD01 getGlasshinmei() {
        return glasshinmei;
    }

    /**
     * ｶﾞﾗｽ品名
     * @param glasshinmei the glasshinmei to set
     */
    public void setGlasshinmei(FXHDD01 glasshinmei) {
        this.glasshinmei = glasshinmei;
    }

    /**
     * ｶﾞﾗｽLotNo
     * @return the glasslotno
     */
    public FXHDD01 getGlasslotno() {
        return glasslotno;
    }

    /**
     * ｶﾞﾗｽLotNo
     * @param glasslotno the glasslotno to set
     */
    public void setGlasslotno(FXHDD01 glasslotno) {
        this.glasslotno = glasslotno;
    }

    /**
     * ﾛｯﾄ区分
     * @return the lotkubun
     */
    public FXHDD01 getLotkubun() {
        return lotkubun;
    }

    /**
     * ﾛｯﾄ区分
     * @param lotkubun the lotkubun to set
     */
    public void setLotkubun(FXHDD01 lotkubun) {
        this.lotkubun = lotkubun;
    }

    /**
     * 秤量号機
     * @return the goki
     */
    public FXHDD01 getGoki() {
        return goki;
    }

    /**
     * 秤量号機
     * @param goki the goki to set
     */
    public void setGoki(FXHDD01 goki) {
        this.goki = goki;
    }

    /**
     * 材料品名1
     * @return the zairyohinmei1
     */
    public FXHDD01 getZairyohinmei1() {
        return zairyohinmei1;
    }

    /**
     * 材料品名1
     * @param zairyohinmei1 the zairyohinmei1 to set
     */
    public void setZairyohinmei1(FXHDD01 zairyohinmei1) {
        this.zairyohinmei1 = zairyohinmei1;
    }

    /**
     * 調合量規格1
     * @return the tyogouryoukikaku1
     */
    public FXHDD01 getTyogouryoukikaku1() {
        return tyogouryoukikaku1;
    }

    /**
     * 調合量規格1
     * @param tyogouryoukikaku1 the tyogouryoukikaku1 to set
     */
    public void setTyogouryoukikaku1(FXHDD01 tyogouryoukikaku1) {
        this.tyogouryoukikaku1 = tyogouryoukikaku1;
    }

    /**
     * 部材在庫No1_1
     * @return the sizailotno1_1
     */
    public FXHDD01 getSizailotno1_1() {
        return sizailotno1_1;
    }

    /**
     * 部材在庫No1_1
     * @param sizailotno1_1 the sizailotno1_1 to set
     */
    public void setSizailotno1_1(FXHDD01 sizailotno1_1) {
        this.sizailotno1_1 = sizailotno1_1;
    }

    /**
     * 調合量1_1
     * @return the tyougouryou1_1
     */
    public FXHDD01 getTyougouryou1_1() {
        return tyougouryou1_1;
    }

    /**
     * 調合量1_1
     * @param tyougouryou1_1 the tyougouryou1_1 to set
     */
    public void setTyougouryou1_1(FXHDD01 tyougouryou1_1) {
        this.tyougouryou1_1 = tyougouryou1_1;
    }

    /**
     * 部材在庫No1_2
     * @return the sizailotno1_2
     */
    public FXHDD01 getSizailotno1_2() {
        return sizailotno1_2;
    }

    /**
     * 部材在庫No1_2
     * @param sizailotno1_2 the sizailotno1_2 to set
     */
    public void setSizailotno1_2(FXHDD01 sizailotno1_2) {
        this.sizailotno1_2 = sizailotno1_2;
    }

    /**
     * 調合量1_2
     * @return the tyougouryou1_2
     */
    public FXHDD01 getTyougouryou1_2() {
        return tyougouryou1_2;
    }

    /**
     * 調合量1_2
     * @param tyougouryou1_2 the tyougouryou1_2 to set
     */
    public void setTyougouryou1_2(FXHDD01 tyougouryou1_2) {
        this.tyougouryou1_2 = tyougouryou1_2;
    }

    /**
     * 材料品名2
     * @return the zairyohinmei2
     */
    public FXHDD01 getZairyohinmei2() {
        return zairyohinmei2;
    }

    /**
     * 材料品名2
     * @param zairyohinmei2 the zairyohinmei2 to set
     */
    public void setZairyohinmei2(FXHDD01 zairyohinmei2) {
        this.zairyohinmei2 = zairyohinmei2;
    }

    /**
     * 調合量規格2
     * @return the tyogouryoukikaku2
     */
    public FXHDD01 getTyogouryoukikaku2() {
        return tyogouryoukikaku2;
    }

    /**
     * 調合量規格2
     * @param tyogouryoukikaku2 the tyogouryoukikaku2 to set
     */
    public void setTyogouryoukikaku2(FXHDD01 tyogouryoukikaku2) {
        this.tyogouryoukikaku2 = tyogouryoukikaku2;
    }

    /**
     * 部材在庫No2_1
     * @return the sizailotno2_1
     */
    public FXHDD01 getSizailotno2_1() {
        return sizailotno2_1;
    }

    /**
     * 部材在庫No2_1
     * @param sizailotno2_1 the sizailotno2_1 to set
     */
    public void setSizailotno2_1(FXHDD01 sizailotno2_1) {
        this.sizailotno2_1 = sizailotno2_1;
    }

    /**
     * 調合量2_1
     * @return the tyougouryou2_1
     */
    public FXHDD01 getTyougouryou2_1() {
        return tyougouryou2_1;
    }

    /**
     * 調合量2_1
     * @param tyougouryou2_1 the tyougouryou2_1 to set
     */
    public void setTyougouryou2_1(FXHDD01 tyougouryou2_1) {
        this.tyougouryou2_1 = tyougouryou2_1;
    }

    /**
     * 部材在庫No2_2
     * @return the sizailotno2_2
     */
    public FXHDD01 getSizailotno2_2() {
        return sizailotno2_2;
    }

    /**
     * 部材在庫No2_2
     * @param sizailotno2_2 the sizailotno2_2 to set
     */
    public void setSizailotno2_2(FXHDD01 sizailotno2_2) {
        this.sizailotno2_2 = sizailotno2_2;
    }

    /**
     * 調合量2_2
     * @return the tyougouryou2_2
     */
    public FXHDD01 getTyougouryou2_2() {
        return tyougouryou2_2;
    }

    /**
     * 調合量2_2
     * @param tyougouryou2_2 the tyougouryou2_2 to set
     */
    public void setTyougouryou2_2(FXHDD01 tyougouryou2_2) {
        this.tyougouryou2_2 = tyougouryou2_2;
    }

    /**
     * 秤量日
     * @return the keiryounichiji_day
     */
    public FXHDD01 getKeiryounichiji_day() {
        return keiryounichiji_day;
    }

    /**
     * 秤量日
     * @param keiryounichiji_day the keiryounichiji_day to set
     */
    public void setKeiryounichiji_day(FXHDD01 keiryounichiji_day) {
        this.keiryounichiji_day = keiryounichiji_day;
    }

    /**
     * 秤量時間
     * @return the keiryounichiji_time
     */
    public FXHDD01 getKeiryounichiji_time() {
        return keiryounichiji_time;
    }

    /**
     * 秤量時間
     * @param keiryounichiji_time the keiryounichiji_time to set
     */
    public void setKeiryounichiji_time(FXHDD01 keiryounichiji_time) {
        this.keiryounichiji_time = keiryounichiji_time;
    }

    /**
     * 担当者
     * @return the tantousya
     */
    public FXHDD01 getTantousya() {
        return tantousya;
    }

    /**
     * 担当者
     * @param tantousya the tantousya to set
     */
    public void setTantousya(FXHDD01 tantousya) {
        this.tantousya = tantousya;
    }

    /**
     * 確認者
     * @return the kakuninsya
     */
    public FXHDD01 getKakuninsya() {
        return kakuninsya;
    }

    /**
     * 確認者
     * @param kakuninsya the kakuninsya to set
     */
    public void setKakuninsya(FXHDD01 kakuninsya) {
        this.kakuninsya = kakuninsya;
    }

    /**
     * 備考1
     * @return the bikou1
     */
    public FXHDD01 getBikou1() {
        return bikou1;
    }

    /**
     * 備考1
     * @param bikou1 the bikou1 to set
     */
    public void setBikou1(FXHDD01 bikou1) {
        this.bikou1 = bikou1;
    }

    /**
     * 備考2
     * @return the bikou2
     */
    public FXHDD01 getBikou2() {
        return bikou2;
    }

    /**
     * 備考2
     * @param bikou2 the bikou2 to set
     */
    public void setBikou2(FXHDD01 bikou2) {
        this.bikou2 = bikou2;
    }
}
