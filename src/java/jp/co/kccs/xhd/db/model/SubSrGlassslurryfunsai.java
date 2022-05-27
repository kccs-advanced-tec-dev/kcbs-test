/*
 * Copyright 2022 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2022/01/10<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * 変更日	2022/05/16<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	材料品名ﾘﾝｸ押下時、調合量規格チェックの追加<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SUB_SR_GLASSSLURRYFUNSAI(ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕入力)のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2022/01/10
 */
public class SubSrGlassslurryfunsai {
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
     * 調合量規格
     */
    private String tyogouryoukikaku;

    /**
     * 調合量規格情報ﾊﾟﾀｰﾝ
     */ 
    private String standardpattern;

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
     * 調合量1
     */
    private Integer tyougouryou1;

    /**
     * 調合量2
     */
    private Integer tyougouryou2;

    /**
     * 調合量3
     */
    private Integer tyougouryou3;

    /**
     * 調合量4
     */
    private Integer tyougouryou4;

    /**
     * 調合量5
     */
    private Integer tyougouryou5;

    /**
     * 調合量6
     */
    private Integer tyougouryou6;

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
     * 調合量規格
     * @return the tyogouryoukikaku
     */
    public String getTyogouryoukikaku() {
        return tyogouryoukikaku;
    }

    /**
     * 調合量規格
     * @param tyogouryoukikaku the tyogouryoukikaku to set
     */
    public void setTyogouryoukikaku(String tyogouryoukikaku) {
        this.tyogouryoukikaku = tyogouryoukikaku;
    }

    /**
     * 調合量規格情報ﾊﾟﾀｰﾝ
     * @return the standardpattern
     */ 
    public String getStandardpattern() {
        return standardpattern;
    }
 
    /**
     * 調合量規格情報ﾊﾟﾀｰﾝ
     * @param standardpattern the standardpattern to set
     */ 
    public void setStandardpattern(String standardpattern) {
        this.standardpattern = standardpattern;
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
     * 調合量1
     * @return the tyougouryou1
     */
    public Integer getTyougouryou1() {
        return tyougouryou1;
    }

    /**
     * 調合量1
     * @param tyougouryou1 the tyougouryou1 to set
     */
    public void setTyougouryou1(Integer tyougouryou1) {
        this.tyougouryou1 = tyougouryou1;
    }

    /**
     * 調合量2
     * @return the tyougouryou2
     */
    public Integer getTyougouryou2() {
        return tyougouryou2;
    }

    /**
     * 調合量2
     * @param tyougouryou2 the tyougouryou2 to set
     */
    public void setTyougouryou2(Integer tyougouryou2) {
        this.tyougouryou2 = tyougouryou2;
    }

    /**
     * 調合量3
     * @return the tyougouryou3
     */
    public Integer getTyougouryou3() {
        return tyougouryou3;
    }

    /**
     * 調合量3
     * @param tyougouryou3 the tyougouryou3 to set
     */
    public void setTyougouryou3(Integer tyougouryou3) {
        this.tyougouryou3 = tyougouryou3;
    }

    /**
     * 調合量4
     * @return the tyougouryou4
     */
    public Integer getTyougouryou4() {
        return tyougouryou4;
    }

    /**
     * 調合量4
     * @param tyougouryou4 the tyougouryou4 to set
     */
    public void setTyougouryou4(Integer tyougouryou4) {
        this.tyougouryou4 = tyougouryou4;
    }

    /**
     * 調合量5
     * @return the tyougouryou5
     */
    public Integer getTyougouryou5() {
        return tyougouryou5;
    }

    /**
     * 調合量5
     * @param tyougouryou5 the tyougouryou5 to set
     */
    public void setTyougouryou5(Integer tyougouryou5) {
        this.tyougouryou5 = tyougouryou5;
    }

    /**
     * 調合量6
     * @return the tyougouryou6
     */
    public Integer getTyougouryou6() {
        return tyougouryou6;
    }

    /**
     * 調合量6
     * @param tyougouryou6 the tyougouryou6 to set
     */
    public void setTyougouryou6(Integer tyougouryou6) {
        this.tyougouryou6 = tyougouryou6;
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