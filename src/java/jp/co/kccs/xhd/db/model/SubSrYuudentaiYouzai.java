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
 * 変更日	2021/11/09<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SUB_SR_YUUDENTAI_YOUZAI(誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力)のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/11/09
 */
public class SubSrYuudentaiYouzai {
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
     * 部材在庫No1
     */
    private String buzailotno1;

    /**
     * 部材在庫品名1
     */
    private String buzaihinmei1;

    /**
     * 風袋重量1
     */
    private Integer fuutaijyuuryou1;

    /**
     * 調合量1_1
     */
    private Integer tyougouryou1_1;

    /**
     * 調合量1_2
     */
    private Integer tyougouryou1_2;

    /**
     * 調合量1_3
     */
    private Integer tyougouryou1_3;

    /**
     * 調合量1_4
     */
    private Integer tyougouryou1_4;

    /**
     * 調合量1_5
     */
    private Integer tyougouryou1_5;

    /**
     * 調合量1_6
     */
    private Integer tyougouryou1_6;

    /**
     * 部材在庫No2
     */
    private String buzailotno2;

    /**
     * 部材在庫品名2
     */
    private String buzaihinmei2;

    /**
     * 風袋重量2
     */
    private Integer fuutaijyuuryou2;

    /**
     * 調合量2_1
     */
    private Integer tyougouryou2_1;

    /**
     * 調合量2_2
     */
    private Integer tyougouryou2_2;

    /**
     * 調合量2_3
     */
    private Integer tyougouryou2_3;

    /**
     * 調合量2_4
     */
    private Integer tyougouryou2_4;

    /**
     * 調合量2_5
     */
    private Integer tyougouryou2_5;

    /**
     * 調合量2_6
     */
    private Integer tyougouryou2_6;

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
     * 部材在庫No1
     * @return the buzailotno1
     */
    public String getBuzailotno1() {
        return buzailotno1;
    }

    /**
     * 部材在庫No1
     * @param buzailotno1 the buzailotno1 to set
     */
    public void setBuzailotno1(String buzailotno1) {
        this.buzailotno1 = buzailotno1;
    }

    /**
     * 部材在庫品名1
     * @return the buzaihinmei1
     */
    public String getBuzaihinmei1() {
        return buzaihinmei1;
    }

    /**
     * 部材在庫品名1
     * @param buzaihinmei1 the buzaihinmei1 to set
     */
    public void setBuzaihinmei1(String buzaihinmei1) {
        this.buzaihinmei1 = buzaihinmei1;
    }

    /**
     * 風袋重量1
     * @return the fuutaijyuuryou1
     */
    public Integer getFuutaijyuuryou1() {
        return fuutaijyuuryou1;
    }

    /**
     * 風袋重量1
     * @param fuutaijyuuryou1 the fuutaijyuuryou1 to set
     */
    public void setFuutaijyuuryou1(Integer fuutaijyuuryou1) {
        this.fuutaijyuuryou1 = fuutaijyuuryou1;
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
     * 調合量1_3
     * @return the tyougouryou1_3
     */
    public Integer getTyougouryou1_3() {
        return tyougouryou1_3;
    }

    /**
     * 調合量1_3
     * @param tyougouryou1_3 the tyougouryou1_3 to set
     */
    public void setTyougouryou1_3(Integer tyougouryou1_3) {
        this.tyougouryou1_3 = tyougouryou1_3;
    }

    /**
     * 調合量1_4
     * @return the tyougouryou1_4
     */
    public Integer getTyougouryou1_4() {
        return tyougouryou1_4;
    }

    /**
     * 調合量1_4
     * @param tyougouryou1_4 the tyougouryou1_4 to set
     */
    public void setTyougouryou1_4(Integer tyougouryou1_4) {
        this.tyougouryou1_4 = tyougouryou1_4;
    }

    /**
     * 調合量1_5
     * @return the tyougouryou1_5
     */
    public Integer getTyougouryou1_5() {
        return tyougouryou1_5;
    }

    /**
     * 調合量1_5
     * @param tyougouryou1_5 the tyougouryou1_5 to set
     */
    public void setTyougouryou1_5(Integer tyougouryou1_5) {
        this.tyougouryou1_5 = tyougouryou1_5;
    }

    /**
     * 調合量1_6
     * @return the tyougouryou1_6
     */
    public Integer getTyougouryou1_6() {
        return tyougouryou1_6;
    }

    /**
     * 調合量1_6
     * @param tyougouryou1_6 the tyougouryou1_6 to set
     */
    public void setTyougouryou1_6(Integer tyougouryou1_6) {
        this.tyougouryou1_6 = tyougouryou1_6;
    }

    /**
     * 部材在庫No2
     * @return the buzailotno2
     */
    public String getBuzailotno2() {
        return buzailotno2;
    }

    /**
     * 部材在庫No2
     * @param buzailotno2 the buzailotno2 to set
     */
    public void setBuzailotno2(String buzailotno2) {
        this.buzailotno2 = buzailotno2;
    }

    /**
     * 部材在庫品名2
     * @return the buzaihinmei2
     */
    public String getBuzaihinmei2() {
        return buzaihinmei2;
    }

    /**
     * 部材在庫品名2
     * @param buzaihinmei2 the buzaihinmei2 to set
     */
    public void setBuzaihinmei2(String buzaihinmei2) {
        this.buzaihinmei2 = buzaihinmei2;
    }

    /**
     * 風袋重量2
     * @return the fuutaijyuuryou2
     */
    public Integer getFuutaijyuuryou2() {
        return fuutaijyuuryou2;
    }

    /**
     * 風袋重量2
     * @param fuutaijyuuryou2 the fuutaijyuuryou2 to set
     */
    public void setFuutaijyuuryou2(Integer fuutaijyuuryou2) {
        this.fuutaijyuuryou2 = fuutaijyuuryou2;
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
     * 調合量2_3
     * @return the tyougouryou2_3
     */
    public Integer getTyougouryou2_3() {
        return tyougouryou2_3;
    }

    /**
     * 調合量2_3
     * @param tyougouryou2_3 the tyougouryou2_3 to set
     */
    public void setTyougouryou2_3(Integer tyougouryou2_3) {
        this.tyougouryou2_3 = tyougouryou2_3;
    }

    /**
     * 調合量2_4
     * @return the tyougouryou2_4
     */
    public Integer getTyougouryou2_4() {
        return tyougouryou2_4;
    }

    /**
     * 調合量2_4
     * @param tyougouryou2_4 the tyougouryou2_4 to set
     */
    public void setTyougouryou2_4(Integer tyougouryou2_4) {
        this.tyougouryou2_4 = tyougouryou2_4;
    }

    /**
     * 調合量2_5
     * @return the tyougouryou2_5
     */
    public Integer getTyougouryou2_5() {
        return tyougouryou2_5;
    }

    /**
     * 調合量2_5
     * @param tyougouryou2_5 the tyougouryou2_5 to set
     */
    public void setTyougouryou2_5(Integer tyougouryou2_5) {
        this.tyougouryou2_5 = tyougouryou2_5;
    }

    /**
     * 調合量2_6
     * @return the tyougouryou2_6
     */
    public Integer getTyougouryou2_6() {
        return tyougouryou2_6;
    }

    /**
     * 調合量2_6
     * @param tyougouryou2_6 the tyougouryou2_6 to set
     */
    public void setTyougouryou2_6(Integer tyougouryou2_6) {
        this.tyougouryou2_6 = tyougouryou2_6;
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