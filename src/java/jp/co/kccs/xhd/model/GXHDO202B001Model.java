/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日       2021/9/10<br>
 * 計画書No     MB2101-DK002<br>
 * 変更者       KCSS K.Jo<br>
 * 変更理由     新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * ｶﾞﾗｽ作製・秤量履歴検索画面のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/09/10
 */
public class GXHDO202B001Model implements Serializable {
    /** WIPﾛｯﾄNo */
    private String lotno = "";
    
    /** ｶﾞﾗｽ品名 */
    private String glasshinmei = "";
    
    /** ｶﾞﾗｽ品名LotNo */
    private String glasslotno = "";
    
    /** ﾛｯﾄ区分 */
    private String lotkubun = "";
    
    /** 秤量号機 */
    private String goki = "";
    
    /** 材料品名1 */
    private String zairyohinmei1 = "";
    
    /** 調合量規格1 */
    private String tyogouryoukikaku1 = "";
    
    /** 資材ﾛｯﾄNo.1_1 */
    private String sizailotno1_1 = "";
    
    /** 調合量1_1 */
    private Integer tyougouryou1_1 = null;
    
    /** 資材ﾛｯﾄNo.1_2 */
    private String sizailotno1_2 = "";
    
    /** 調合量1_2 */
    private Integer tyougouryou1_2 = null;
    
    /** 材料品名2 */
    private String zairyohinmei2 = "";
    
    /** 調合量規格2 */
    private String tyogouryoukikaku2 = "";
    
    /** 資材ﾛｯﾄNo.2_1 */
    private String sizailotno2_1 = "";
    
    /** 調合量2_1 */
    private Integer tyougouryou2_1 = null;
    
    /** 資材ﾛｯﾄNo.2_2 */
    private String sizailotno2_2 = "";
    
    /** 調合量2_2 */
    private Integer tyougouryou2_2 = null;
    
    /** 計量日時 */
    private Timestamp keiryounichiji = null;
    
    /** 担当者 */
    private String tantousya = "";
    
    /** 確認者 */
    private String kakuninsya = "";
    
    /** 備考1 */
    private String bikou1 = "";
    
    /** 備考2 */
    private String bikou2 = "";

    /**
     * WIPﾛｯﾄNo
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * WIPﾛｯﾄNo
     * @param lotno the lotno to set
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * ｶﾞﾗｽ品名
     * @return the glasshinmei
     */
    public String getGlasshinmei() {
        return glasshinmei;
    }

    /**
     * ｶﾞﾗｽ品名
     * @param glasshinmei the glasshinmei to set
     */
    public void setGlasshinmei(String glasshinmei) {
        this.glasshinmei = glasshinmei;
    }

    /**
     * ｶﾞﾗｽ品名LotNo
     * @return the glasslotno
     */
    public String getGlasslotno() {
        return glasslotno;
    }

    /**
     * ｶﾞﾗｽ品名LotNo
     * @param glasslotno the glasslotno to set
     */
    public void setGlasslotno(String glasslotno) {
        this.glasslotno = glasslotno;
    }

    /**
     * ﾛｯﾄ区分
     * @return the lotkubun
     */
    public String getLotkubun() {
        return lotkubun;
    }

    /**
     * ﾛｯﾄ区分
     * @param lotkubun the lotkubun to set
     */
    public void setLotkubun(String lotkubun) {
        this.lotkubun = lotkubun;
    }

    /**
     * 秤量号機
     * @return the goki
     */
    public String getGoki() {
        return goki;
    }

    /**
     * 秤量号機
     * @param goki the goki to set
     */
    public void setGoki(String goki) {
        this.goki = goki;
    }

    /**
     * 材料品名1
     * @return the zairyohinmei1
     */
    public String getZairyohinmei1() {
        return zairyohinmei1;
    }

    /**
     * 材料品名1
     * @param zairyohinmei1 the zairyohinmei1 to set
     */
    public void setZairyohinmei1(String zairyohinmei1) {
        this.zairyohinmei1 = zairyohinmei1;
    }

    /**
     * 調合量規格1
     * @return the tyogouryoukikaku1
     */
    public String getTyogouryoukikaku1() {
        return tyogouryoukikaku1;
    }

    /**
     * 調合量規格1
     * @param tyogouryoukikaku1 the tyogouryoukikaku1 to set
     */
    public void setTyogouryoukikaku1(String tyogouryoukikaku1) {
        this.tyogouryoukikaku1 = tyogouryoukikaku1;
    }

    /**
     * 資材ﾛｯﾄNo.1_1
     * @return the sizailotno1_1
     */
    public String getSizailotno1_1() {
        return sizailotno1_1;
    }

    /**
     * 資材ﾛｯﾄNo.1_1
     * @param sizailotno1_1 the sizailotno1_1 to set
     */
    public void setSizailotno1_1(String sizailotno1_1) {
        this.sizailotno1_1 = sizailotno1_1;
    }

    /**
     * 調合量1_1
     * @return the tyougouryou1_1
     */
    public Integer getTyougouryou1_1() {
        return tyougouryou1_1;
    }

    /**
     * 調合量1_1
     * @param tyougouryou1_1 the tyougouryou1_1 to set
     */
    public void setTyougouryou1_1(Integer tyougouryou1_1) {
        this.tyougouryou1_1 = tyougouryou1_1;
    }

    /**
     * 資材ﾛｯﾄNo.1_2
     * @return the sizailotno1_2
     */
    public String getSizailotno1_2() {
        return sizailotno1_2;
    }

    /**
     * 資材ﾛｯﾄNo.1_2
     * @param sizailotno1_2 the sizailotno1_2 to set
     */
    public void setSizailotno1_2(String sizailotno1_2) {
        this.sizailotno1_2 = sizailotno1_2;
    }

    /**
     * 調合量1_2
     * @return the tyougouryou1_2
     */
    public Integer getTyougouryou1_2() {
        return tyougouryou1_2;
    }

    /**
     * 調合量1_2
     * @param tyougouryou1_2 the tyougouryou1_2 to set
     */
    public void setTyougouryou1_2(Integer tyougouryou1_2) {
        this.tyougouryou1_2 = tyougouryou1_2;
    }

    /**
     * 材料品名2
     * @return the zairyohinmei2
     */
    public String getZairyohinmei2() {
        return zairyohinmei2;
    }

    /**
     * 材料品名2
     * @param zairyohinmei2 the zairyohinmei2 to set
     */
    public void setZairyohinmei2(String zairyohinmei2) {
        this.zairyohinmei2 = zairyohinmei2;
    }

    /**
     * 調合量規格2
     * @return the tyogouryoukikaku2
     */
    public String getTyogouryoukikaku2() {
        return tyogouryoukikaku2;
    }

    /**
     * 調合量規格2
     * @param tyogouryoukikaku2 the tyogouryoukikaku2 to set
     */
    public void setTyogouryoukikaku2(String tyogouryoukikaku2) {
        this.tyogouryoukikaku2 = tyogouryoukikaku2;
    }

    /**
     * 資材ﾛｯﾄNo.2_1
     * @return the sizailotno2_1
     */
    public String getSizailotno2_1() {
        return sizailotno2_1;
    }

    /**
     * 資材ﾛｯﾄNo.2_1
     * @param sizailotno2_1 the sizailotno2_1 to set
     */
    public void setSizailotno2_1(String sizailotno2_1) {
        this.sizailotno2_1 = sizailotno2_1;
    }

    /**
     * 調合量2_1
     * @return the tyougouryou2_1
     */
    public Integer getTyougouryou2_1() {
        return tyougouryou2_1;
    }

    /**
     * 調合量2_1
     * @param tyougouryou2_1 the tyougouryou2_1 to set
     */
    public void setTyougouryou2_1(Integer tyougouryou2_1) {
        this.tyougouryou2_1 = tyougouryou2_1;
    }

    /**
     * 資材ﾛｯﾄNo.2_2
     * @return the sizailotno2_2
     */
    public String getSizailotno2_2() {
        return sizailotno2_2;
    }

    /**
     * 資材ﾛｯﾄNo.2_2
     * @param sizailotno2_2 the sizailotno2_2 to set
     */
    public void setSizailotno2_2(String sizailotno2_2) {
        this.sizailotno2_2 = sizailotno2_2;
    }

    /**
     * 調合量2_2
     * @return the tyougouryou2_2
     */
    public Integer getTyougouryou2_2() {
        return tyougouryou2_2;
    }

    /**
     * 調合量2_2
     * @param tyougouryou2_2 the tyougouryou2_2 to set
     */
    public void setTyougouryou2_2(Integer tyougouryou2_2) {
        this.tyougouryou2_2 = tyougouryou2_2;
    }

    /**
     * 計量日時
     * @return the keiryounichiji
     */
    public Timestamp getKeiryounichiji() {
        return keiryounichiji;
    }

    /**
     * 計量日時
     * @param keiryounichiji the keiryounichiji to set
     */
    public void setKeiryounichiji(Timestamp keiryounichiji) {
        this.keiryounichiji = keiryounichiji;
    }

    /**
     * 担当者
     * @return the tantousya
     */
    public String getTantousya() {
        return tantousya;
    }

    /**
     * 担当者
     * @param tantousya the tantousya to set
     */
    public void setTantousya(String tantousya) {
        this.tantousya = tantousya;
    }

    /**
     * 確認者
     * @return the kakuninsya
     */
    public String getKakuninsya() {
        return kakuninsya;
    }

    /**
     * 確認者
     * @param kakuninsya the kakuninsya to set
     */
    public void setKakuninsya(String kakuninsya) {
        this.kakuninsya = kakuninsya;
    }

    /**
     * 備考1
     * @return the bikou1
     */
    public String getBikou1() {
        return bikou1;
    }

    /**
     * 備考1
     * @param bikou1 the bikou1 to set
     */
    public void setBikou1(String bikou1) {
        this.bikou1 = bikou1;
    }

    /**
     * 備考2
     * @return the bikou2
     */
    public String getBikou2() {
        return bikou2;
    }

    /**
     * 備考2
     * @param bikou2 the bikou2 to set
     */
    public void setBikou2(String bikou2) {
        this.bikou2 = bikou2;
    }

}