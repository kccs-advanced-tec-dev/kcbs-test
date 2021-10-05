/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/09/22<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * sr_glassslurryfunsai(ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕)のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/09/22
 */
public class SrGlassslurryfunsai {
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
     * ｶﾞﾗｽｽﾗﾘｰ品名
     */
    private String glassslurryhinmei;

    /**
     * ｶﾞﾗｽｽﾗﾘｰ品名LotNo
     */
    private String glassslurrylotno;

    /**
     * ﾛｯﾄ区分
     */
    private String lotkubun;

    /**
     * 周速
     */
    private String syuusoku;

    /**
     * 粉砕回転台号機
     */
    private Integer kaitendaigouki;

    /**
     * 粉砕開始日時
     */
    private Timestamp funsaikaisinichiji;

    /**
     * 粉砕終了予定日時
     */
    private Timestamp funsaiyoteisyuuryounichiji;

    /**
     * 粉砕開始担当者
     */
    private String kaisitantosya;

    /**
     * 粉砕終了日時
     */
    private Timestamp funsaisyuuryounichiji;

    /**
     * 粉砕終了担当者
     */
    private String syuryotantosya;

    /**
     * 粉砕時間
     */
    private Integer funsaijikan;

    /**
     * 材料品名
     */
    private String zairyohinmei;

    /**
     * 部材在庫No
     */
    private String buzaizaikono;

    /**
     * 調合量
     */
    private String tyougouryou;

    /**
     * 風袋重量
     */
    private Integer fuutaijyuuryou;

    /**
     * 総重量
     */
    private Integer soujyuuryou;

    /**
     * 正味重量
     */
    private Integer syoumijyuuryou;

    /**
     * 歩留まり
     */
    private BigDecimal budomari;

    /**
     * 排出担当者
     */
    private String haisyututantousya;

    /**
     * 保管開始日時
     */
    private Timestamp hokankaisinichiji;

    /**
     * 保管場所
     */
    private String hokanbasyo;

    /**
     * 保管回転台号機
     */
    private String hokankaitengouki;

    /**
     * 回転数
     */
    private String kaitensuu;

    /**
     * 保管担当者
     */
    private String hokantantosya;

    /**
     * 備考1
     */
    private String bikou1;

    /**
     * 備考2
     */
    private String bikou2;

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
     * ｶﾞﾗｽｽﾗﾘｰ品名
     * @return the glassslurryhinmei
     */
    public String getGlassslurryhinmei() {
        return glassslurryhinmei;
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ品名
     * @param glassslurryhinmei the glassslurryhinmei to set
     */
    public void setGlassslurryhinmei(String glassslurryhinmei) {
        this.glassslurryhinmei = glassslurryhinmei;
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ品名LotNo
     * @return the glassslurrylotno
     */
    public String getGlassslurrylotno() {
        return glassslurrylotno;
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ品名LotNo
     * @param glassslurrylotno the glassslurrylotno to set
     */
    public void setGlassslurrylotno(String glassslurrylotno) {
        this.glassslurrylotno = glassslurrylotno;
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
     * 周速
     * @return the syuusoku
     */
    public String getSyuusoku() {
        return syuusoku;
    }

    /**
     * 周速
     * @param syuusoku the syuusoku to set
     */
    public void setSyuusoku(String syuusoku) {
        this.syuusoku = syuusoku;
    }

    /**
     * 粉砕回転台号機
     * @return the kaitendaigouki
     */
    public Integer getKaitendaigouki() {
        return kaitendaigouki;
    }

    /**
     * 粉砕回転台号機
     * @param kaitendaigouki the kaitendaigouki to set
     */
    public void setKaitendaigouki(Integer kaitendaigouki) {
        this.kaitendaigouki = kaitendaigouki;
    }

    /**
     * 粉砕開始日時
     * @return the funsaikaisinichiji
     */
    public Timestamp getFunsaikaisinichiji() {
        return funsaikaisinichiji;
    }

    /**
     * 粉砕開始日時
     * @param funsaikaisinichiji the funsaikaisinichiji to set
     */
    public void setFunsaikaisinichiji(Timestamp funsaikaisinichiji) {
        this.funsaikaisinichiji = funsaikaisinichiji;
    }

    /**
     * 粉砕終了予定日時
     * @return the funsaiyoteisyuuryounichiji
     */
    public Timestamp getFunsaiyoteisyuuryounichiji() {
        return funsaiyoteisyuuryounichiji;
    }

    /**
     * 粉砕終了予定日時
     * @param funsaiyoteisyuuryounichiji the funsaiyoteisyuuryounichiji to set
     */
    public void setFunsaiyoteisyuuryounichiji(Timestamp funsaiyoteisyuuryounichiji) {
        this.funsaiyoteisyuuryounichiji = funsaiyoteisyuuryounichiji;
    }

    /**
     * 粉砕開始担当者
     * @return the kaisitantosya
     */
    public String getKaisitantosya() {
        return kaisitantosya;
    }

    /**
     * 粉砕開始担当者
     * @param kaisitantosya the kaisitantosya to set
     */
    public void setKaisitantosya(String kaisitantosya) {
        this.kaisitantosya = kaisitantosya;
    }

    /**
     * 粉砕終了日時
     * @return the funsaisyuuryounichiji
     */
    public Timestamp getFunsaisyuuryounichiji() {
        return funsaisyuuryounichiji;
    }

    /**
     * 粉砕終了日時
     * @param funsaisyuuryounichiji the funsaisyuuryounichiji to set
     */
    public void setFunsaisyuuryounichiji(Timestamp funsaisyuuryounichiji) {
        this.funsaisyuuryounichiji = funsaisyuuryounichiji;
    }

    /**
     * 粉砕終了担当者
     * @return the syuryotantosya
     */
    public String getSyuryotantosya() {
        return syuryotantosya;
    }

    /**
     * 粉砕終了担当者
     * @param syuryotantosya the syuryotantosya to set
     */
    public void setSyuryotantosya(String syuryotantosya) {
        this.syuryotantosya = syuryotantosya;
    }

    /**
     * 粉砕時間
     * @return the funsaijikan
     */
    public Integer getFunsaijikan() {
        return funsaijikan;
    }

    /**
     * 粉砕時間
     * @param funsaijikan the funsaijikan to set
     */
    public void setFunsaijikan(Integer funsaijikan) {
        this.funsaijikan = funsaijikan;
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
     * @return the buzaizaikono
     */
    public String getBuzaizaikono() {
        return buzaizaikono;
    }

    /**
     * 部材在庫No
     * @param buzaizaikono the buzaizaikono to set
     */
    public void setBuzaizaikono(String buzaizaikono) {
        this.buzaizaikono = buzaizaikono;
    }

    /**
     * 調合量
     * @return the tyougouryou
     */
    public String getTyougouryou() {
        return tyougouryou;
    }

    /**
     * 調合量
     * @param tyougouryou the tyougouryou to set
     */
    public void setTyougouryou(String tyougouryou) {
        this.tyougouryou = tyougouryou;
    }

    /**
     * 風袋重量
     * @return the fuutaijyuuryou
     */
    public Integer getFuutaijyuuryou() {
        return fuutaijyuuryou;
    }

    /**
     * 風袋重量
     * @param fuutaijyuuryou the fuutaijyuuryou to set
     */
    public void setFuutaijyuuryou(Integer fuutaijyuuryou) {
        this.fuutaijyuuryou = fuutaijyuuryou;
    }

    /**
     * 総重量
     * @return the soujyuuryou
     */
    public Integer getSoujyuuryou() {
        return soujyuuryou;
    }

    /**
     * 総重量
     * @param soujyuuryou the soujyuuryou to set
     */
    public void setSoujyuuryou(Integer soujyuuryou) {
        this.soujyuuryou = soujyuuryou;
    }

    /**
     * 正味重量
     * @return the syoumijyuuryou
     */
    public Integer getSyoumijyuuryou() {
        return syoumijyuuryou;
    }

    /**
     * 正味重量
     * @param syoumijyuuryou the syoumijyuuryou to set
     */
    public void setSyoumijyuuryou(Integer syoumijyuuryou) {
        this.syoumijyuuryou = syoumijyuuryou;
    }

    /**
     * 歩留まり
     * @return the budomari
     */
    public BigDecimal getBudomari() {
        return budomari;
    }

    /**
     * 歩留まり
     * @param budomari the budomari to set
     */
    public void setBudomari(BigDecimal budomari) {
        this.budomari = budomari;
    }

    /**
     * 排出担当者
     * @return the haisyututantousya
     */
    public String getHaisyututantousya() {
        return haisyututantousya;
    }

    /**
     * 排出担当者
     * @param haisyututantousya the haisyututantousya to set
     */
    public void setHaisyututantousya(String haisyututantousya) {
        this.haisyututantousya = haisyututantousya;
    }

    /**
     * 保管開始日時
     * @return the hokankaisinichiji
     */
    public Timestamp getHokankaisinichiji() {
        return hokankaisinichiji;
    }

    /**
     * 保管開始日時
     * @param hokankaisinichiji the hokankaisinichiji to set
     */
    public void setHokankaisinichiji(Timestamp hokankaisinichiji) {
        this.hokankaisinichiji = hokankaisinichiji;
    }

    /**
     * 保管場所
     * @return the hokanbasyo
     */
    public String getHokanbasyo() {
        return hokanbasyo;
    }

    /**
     * 保管場所
     * @param hokanbasyo the hokanbasyo to set
     */
    public void setHokanbasyo(String hokanbasyo) {
        this.hokanbasyo = hokanbasyo;
    }

    /**
     * 保管回転台号機
     * @return the hokankaitengouki
     */
    public String getHokankaitengouki() {
        return hokankaitengouki;
    }

    /**
     * 保管回転台号機
     * @param hokankaitengouki the hokankaitengouki to set
     */
    public void setHokankaitengouki(String hokankaitengouki) {
        this.hokankaitengouki = hokankaitengouki;
    }

    /**
     * 回転数
     * @return the kaitensuu
     */
    public String getKaitensuu() {
        return kaitensuu;
    }

    /**
     * 回転数
     * @param kaitensuu the kaitensuu to set
     */
    public void setKaitensuu(String kaitensuu) {
        this.kaitensuu = kaitensuu;
    }

    /**
     * 保管担当者
     * @return the hokantantosya
     */
    public String getHokantantosya() {
        return hokantantosya;
    }

    /**
     * 保管担当者
     * @param hokantantosya the hokantantosya to set
     */
    public void setHokantantosya(String hokantantosya) {
        this.hokantantosya = hokantantosya;
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