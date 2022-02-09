/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/11/11<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SUB_SR_YUUDENTAI_SYUGENRYOU(誘電体ｽﾗﾘｰ作製・主原料秤量入力)のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/11/11
 */
public class SubSrYuudentaiSyugenryou {
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
     * 材料区分
     */
    private Integer zairyokubun;

    /**
     * 調合規格
     */
    private String tyogouryoukikaku;

    /**
     * 調合残量
     */
    private Integer tyogouzanryou;

    /**
     * 材料品名
     */
    private String zairyohinmei;

    /**
     * 部材在庫No
     */
    private String buzailotno;

    /**
     * 部材在庫品名
     */
    private String buzaihinmei;

    /**
     * 調合量1_1
     */
    private Integer tyougouryou1_1;

    /**
     * 調合量1_2
     */
    private Integer tyougouryou1_2;

    /**
     * 調合量2_1
     */
    private Integer tyougouryou2_1;

    /**
     * 調合量2_2
     */
    private Integer tyougouryou2_2;

    /**
     * 調合量3_1
     */
    private Integer tyougouryou3_1;

    /**
     * 調合量3_2
     */
    private Integer tyougouryou3_2;

    /**
     * 調合量4_1
     */
    private Integer tyougouryou4_1;

    /**
     * 調合量4_2
     */
    private Integer tyougouryou4_2;

    /**
     * 調合量5_1
     */
    private Integer tyougouryou5_1;

    /**
     * 調合量5_2
     */
    private Integer tyougouryou5_2;

    /**
     * 調合量6_1
     */
    private Integer tyougouryou6_1;

    /**
     * 調合量6_2
     */
    private Integer tyougouryou6_2;

    /**
     * 調合量7_1
     */
    private Integer tyougouryou7_1;

    /**
     * 調合量7_2
     */
    private Integer tyougouryou7_2;

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
    private Integer revision;

    /**
     * 削除ﾌﾗｸﾞ
     */
    private Integer deleteflag;

    /**
     * 工場ｺｰﾄﾞ
     * @return the kojyo
     */
    public String getKojyo() {
        return kojyo;
    }

    /**
     * 工場ｺｰﾄﾞ
     * @param kojyo the kojyo to set
     */
    public void setKojyo(String kojyo) {
        this.kojyo = kojyo;
    }

    /**
     * ﾛｯﾄNo
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * ﾛｯﾄNo
     * @param lotno the lotno to set
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * 枝番
     * @return the edaban
     */
    public String getEdaban() {
        return edaban;
    }

    /**
     * 枝番
     * @param edaban the edaban to set
     */
    public void setEdaban(String edaban) {
        this.edaban = edaban;
    }

    /**
     * 材料区分
     * @return the zairyokubun
     */
    public Integer getZairyokubun() {
        return zairyokubun;
    }

    /**
     * 材料区分
     * @param zairyokubun the zairyokubun to set
     */
    public void setZairyokubun(Integer zairyokubun) {
        this.zairyokubun = zairyokubun;
    }

    /**
     * 調合規格
     * @return the tyogouryoukikaku
     */
    public String getTyogouryoukikaku() {
        return tyogouryoukikaku;
    }

    /**
     * 調合規格
     * @param tyogouryoukikaku the tyogouryoukikaku to set
     */
    public void setTyogouryoukikaku(String tyogouryoukikaku) {
        this.tyogouryoukikaku = tyogouryoukikaku;
    }

    /**
     * 調合残量
     * @return the tyogouzanryou
     */
    public Integer getTyogouzanryou() {
        return tyogouzanryou;
    }

    /**
     * 調合残量
     * @param tyogouzanryou the tyogouzanryou to set
     */
    public void setTyogouzanryou(Integer tyogouzanryou) {
        this.tyogouzanryou = tyogouzanryou;
    }

    /**
     * 材料品名
     * @return the zairyohinmei
     */
    public String getZairyohinmei() {
        return zairyohinmei;
    }

    /**
     * 材料品名
     * @param zairyohinmei the zairyohinmei to set
     */
    public void setZairyohinmei(String zairyohinmei) {
        this.zairyohinmei = zairyohinmei;
    }

    /**
     * 部材在庫No
     * @return the buzailotno
     */
    public String getBuzailotno() {
        return buzailotno;
    }

    /**
     * 部材在庫No
     * @param buzailotno the buzailotno to set
     */
    public void setBuzailotno(String buzailotno) {
        this.buzailotno = buzailotno;
    }

    /**
     * 部材在庫品名
     * @return the buzaihinmei
     */
    public String getBuzaihinmei() {
        return buzaihinmei;
    }

    /**
     * 部材在庫品名
     * @param buzaihinmei the buzaihinmei to set
     */
    public void setBuzaihinmei(String buzaihinmei) {
        this.buzaihinmei = buzaihinmei;
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
     * 調合量3_1
     * @return the tyougouryou3_1
     */
    public Integer getTyougouryou3_1() {
        return tyougouryou3_1;
    }

    /**
     * 調合量3_1
     * @param tyougouryou3_1 the tyougouryou3_1 to set
     */
    public void setTyougouryou3_1(Integer tyougouryou3_1) {
        this.tyougouryou3_1 = tyougouryou3_1;
    }

    /**
     * 調合量3_2
     * @return the tyougouryou3_2
     */
    public Integer getTyougouryou3_2() {
        return tyougouryou3_2;
    }

    /**
     * 調合量3_2
     * @param tyougouryou3_2 the tyougouryou3_2 to set
     */
    public void setTyougouryou3_2(Integer tyougouryou3_2) {
        this.tyougouryou3_2 = tyougouryou3_2;
    }

    /**
     * 調合量4_1
     * @return the tyougouryou4_1
     */
    public Integer getTyougouryou4_1() {
        return tyougouryou4_1;
    }

    /**
     * 調合量4_1
     * @param tyougouryou4_1 the tyougouryou4_1 to set
     */
    public void setTyougouryou4_1(Integer tyougouryou4_1) {
        this.tyougouryou4_1 = tyougouryou4_1;
    }

    /**
     * 調合量4_2
     * @return the tyougouryou4_2
     */
    public Integer getTyougouryou4_2() {
        return tyougouryou4_2;
    }

    /**
     * 調合量4_2
     * @param tyougouryou4_2 the tyougouryou4_2 to set
     */
    public void setTyougouryou4_2(Integer tyougouryou4_2) {
        this.tyougouryou4_2 = tyougouryou4_2;
    }

    /**
     * 調合量5_1
     * @return the tyougouryou5_1
     */
    public Integer getTyougouryou5_1() {
        return tyougouryou5_1;
    }

    /**
     * 調合量5_1
     * @param tyougouryou5_1 the tyougouryou5_1 to set
     */
    public void setTyougouryou5_1(Integer tyougouryou5_1) {
        this.tyougouryou5_1 = tyougouryou5_1;
    }

    /**
     * 調合量5_2
     * @return the tyougouryou5_2
     */
    public Integer getTyougouryou5_2() {
        return tyougouryou5_2;
    }

    /**
     * 調合量5_2
     * @param tyougouryou5_2 the tyougouryou5_2 to set
     */
    public void setTyougouryou5_2(Integer tyougouryou5_2) {
        this.tyougouryou5_2 = tyougouryou5_2;
    }

    /**
     * 調合量6_1
     * @return the tyougouryou6_1
     */
    public Integer getTyougouryou6_1() {
        return tyougouryou6_1;
    }

    /**
     * 調合量6_1
     * @param tyougouryou6_1 the tyougouryou6_1 to set
     */
    public void setTyougouryou6_1(Integer tyougouryou6_1) {
        this.tyougouryou6_1 = tyougouryou6_1;
    }

    /**
     * 調合量6_2
     * @return the tyougouryou6_2
     */
    public Integer getTyougouryou6_2() {
        return tyougouryou6_2;
    }

    /**
     * 調合量6_2
     * @param tyougouryou6_2 the tyougouryou6_2 to set
     */
    public void setTyougouryou6_2(Integer tyougouryou6_2) {
        this.tyougouryou6_2 = tyougouryou6_2;
    }

    /**
     * 調合量7_1
     * @return the tyougouryou7_1
     */
    public Integer getTyougouryou7_1() {
        return tyougouryou7_1;
    }

    /**
     * 調合量7_1
     * @param tyougouryou7_1 the tyougouryou7_1 to set
     */
    public void setTyougouryou7_1(Integer tyougouryou7_1) {
        this.tyougouryou7_1 = tyougouryou7_1;
    }

    /**
     * 調合量7_2
     * @return the tyougouryou7_2
     */
    public Integer getTyougouryou7_2() {
        return tyougouryou7_2;
    }

    /**
     * 調合量7_2
     * @param tyougouryou7_2 the tyougouryou7_2 to set
     */
    public void setTyougouryou7_2(Integer tyougouryou7_2) {
        this.tyougouryou7_2 = tyougouryou7_2;
    }

    /**
     * 登録日時
     * @return the torokunichiji
     */
    public Timestamp getTorokunichiji() {
        return torokunichiji;
    }

    /**
     * 登録日時
     * @param torokunichiji the torokunichiji to set
     */
    public void setTorokunichiji(Timestamp torokunichiji) {
        this.torokunichiji = torokunichiji;
    }

    /**
     * 更新日時
     * @return the kosinnichiji
     */
    public Timestamp getKosinnichiji() {
        return kosinnichiji;
    }

    /**
     * 更新日時
     * @param kosinnichiji the kosinnichiji to set
     */
    public void setKosinnichiji(Timestamp kosinnichiji) {
        this.kosinnichiji = kosinnichiji;
    }

    /**
     * revision
     * @return the revision
     */
    public Integer getRevision() {
        return revision;
    }

    /**
     * revision
     * @param revision the revision to set
     */
    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    /**
     * 削除ﾌﾗｸﾞ
     * @return the deleteflag
     */
    public Integer getDeleteflag() {
        return deleteflag;
    }

    /**
     * 削除ﾌﾗｸﾞ
     * @param deleteflag the deleteflag to set
     */
    public void setDeleteflag(Integer deleteflag) {
        this.deleteflag = deleteflag;
    }

}