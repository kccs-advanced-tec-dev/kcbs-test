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
 * 変更日	2021/12/20<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SUB_SR_SLIP_KOUATSUBUNSAN(ｽﾘｯﾌﾟ作製・高圧分散ｻﾌﾞ)のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/12/20
 */
public class SubSrSlipKouatsubunsan {
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
     * 実績No
     */
    private Integer jissekino;

    /**
     * ﾊﾟｽ回数
     */
    private Integer pass;

    /**
     * 送液側ﾀﾝｸNo.
     */
    private Integer souekigawatankno;

    /**
     * 排出側ﾀﾝｸNo.
     */
    private Integer haisyutsugawatankno;

    /**
     * 設定圧力
     */
    private String setteiatsuryoku;

    /**
     * 開始直後排気量
     */
    private String kaishityokugohaikiryou;

    /**
     * 高圧分散開始日時
     */
    private Timestamp kouatsubunsankaishinichiji;

    /**
     * 廃棄確認
     */
    private Integer haikikakunin;

    /**
     * 実圧力(最大値)
     */
    private Integer jitsuatsuryoku;

    /**
     * ｽﾘｯﾌﾟ流量
     */
    private BigDecimal slipryuuryou;

    /**
     * ｽﾘｯﾌﾟ温度(IN)
     */
    private Integer slipondoin;

    /**
     * ｽﾘｯﾌﾟ温度(OUT)
     */
    private Integer slipondoout;

    /**
     * 高圧分散開始担当者
     */
    private String kouatsubunsankaishitantousya;

    /**
     * 高圧分散終了日時
     */
    private Timestamp kouatsubunsansyuuryounichiji;

    /**
     * 高圧分散停止担当者
     */
    private String kouatsubunsanteishitantousya;

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
     * 実績No
     * @return the jissekino
     */
    public Integer getJissekino() {
        return jissekino;
    }

    /**
     * 実績No
     * @param jissekino the jissekino to set
     */
    public void setJissekino(Integer jissekino) {
        this.jissekino = jissekino;
    }

    /**
     * ﾊﾟｽ回数
     * @return the pass
     */
    public Integer getPass() {
        return pass;
    }

    /**
     * ﾊﾟｽ回数
     * @param pass the pass to set
     */
    public void setPass(Integer pass) {
        this.pass = pass;
    }

    /**
     * 送液側ﾀﾝｸNo.
     * @return the souekigawatankno
     */
    public Integer getSouekigawatankno() {
        return souekigawatankno;
    }

    /**
     * 送液側ﾀﾝｸNo.
     * @param souekigawatankno the souekigawatankno to set
     */
    public void setSouekigawatankno(Integer souekigawatankno) {
        this.souekigawatankno = souekigawatankno;
    }

    /**
     * 排出側ﾀﾝｸNo.
     * @return the haisyutsugawatankno
     */
    public Integer getHaisyutsugawatankno() {
        return haisyutsugawatankno;
    }

    /**
     * 排出側ﾀﾝｸNo.
     * @param haisyutsugawatankno the haisyutsugawatankno to set
     */
    public void setHaisyutsugawatankno(Integer haisyutsugawatankno) {
        this.haisyutsugawatankno = haisyutsugawatankno;
    }

    /**
     * 設定圧力
     * @return the setteiatsuryoku
     */
    public String getSetteiatsuryoku() {
        return setteiatsuryoku;
    }

    /**
     * 設定圧力
     * @param setteiatsuryoku the setteiatsuryoku to set
     */
    public void setSetteiatsuryoku(String setteiatsuryoku) {
        this.setteiatsuryoku = setteiatsuryoku;
    }

    /**
     * 開始直後排気量
     * @return the kaishityokugohaikiryou
     */
    public String getKaishityokugohaikiryou() {
        return kaishityokugohaikiryou;
    }

    /**
     * 開始直後排気量
     * @param kaishityokugohaikiryou the kaishityokugohaikiryou to set
     */
    public void setKaishityokugohaikiryou(String kaishityokugohaikiryou) {
        this.kaishityokugohaikiryou = kaishityokugohaikiryou;
    }

    /**
     * 高圧分散開始日時
     * @return the kouatsubunsankaishinichiji
     */
    public Timestamp getKouatsubunsankaishinichiji() {
        return kouatsubunsankaishinichiji;
    }

    /**
     * 高圧分散開始日時
     * @param kouatsubunsankaishinichiji the kouatsubunsankaishinichiji to set
     */
    public void setKouatsubunsankaishinichiji(Timestamp kouatsubunsankaishinichiji) {
        this.kouatsubunsankaishinichiji = kouatsubunsankaishinichiji;
    }

    /**
     * 廃棄確認
     * @return the haikikakunin
     */
    public Integer getHaikikakunin() {
        return haikikakunin;
    }

    /**
     * 廃棄確認
     * @param haikikakunin the haikikakunin to set
     */
    public void setHaikikakunin(Integer haikikakunin) {
        this.haikikakunin = haikikakunin;
    }

    /**
     * 実圧力(最大値)
     * @return the jitsuatsuryoku
     */
    public Integer getJitsuatsuryoku() {
        return jitsuatsuryoku;
    }

    /**
     * 実圧力(最大値)
     * @param jitsuatsuryoku the jitsuatsuryoku to set
     */
    public void setJitsuatsuryoku(Integer jitsuatsuryoku) {
        this.jitsuatsuryoku = jitsuatsuryoku;
    }

    /**
     * ｽﾘｯﾌﾟ流量
     * @return the slipryuuryou
     */
    public BigDecimal getSlipryuuryou() {
        return slipryuuryou;
    }

    /**
     * ｽﾘｯﾌﾟ流量
     * @param slipryuuryou the slipryuuryou to set
     */
    public void setSlipryuuryou(BigDecimal slipryuuryou) {
        this.slipryuuryou = slipryuuryou;
    }

    /**
     * ｽﾘｯﾌﾟ温度(IN)
     * @return the slipondoin
     */
    public Integer getSlipondoin() {
        return slipondoin;
    }

    /**
     * ｽﾘｯﾌﾟ温度(IN)
     * @param slipondoin the slipondoin to set
     */
    public void setSlipondoin(Integer slipondoin) {
        this.slipondoin = slipondoin;
    }

    /**
     * ｽﾘｯﾌﾟ温度(OUT)
     * @return the slipondoout
     */
    public Integer getSlipondoout() {
        return slipondoout;
    }

    /**
     * ｽﾘｯﾌﾟ温度(OUT)
     * @param slipondoout the slipondoout to set
     */
    public void setSlipondoout(Integer slipondoout) {
        this.slipondoout = slipondoout;
    }

    /**
     * 高圧分散開始担当者
     * @return the kouatsubunsankaishitantousya
     */
    public String getKouatsubunsankaishitantousya() {
        return kouatsubunsankaishitantousya;
    }

    /**
     * 高圧分散開始担当者
     * @param kouatsubunsankaishitantousya the kouatsubunsankaishitantousya to set
     */
    public void setKouatsubunsankaishitantousya(String kouatsubunsankaishitantousya) {
        this.kouatsubunsankaishitantousya = kouatsubunsankaishitantousya;
    }

    /**
     * 高圧分散終了日時
     * @return the kouatsubunsansyuuryounichiji
     */
    public Timestamp getKouatsubunsansyuuryounichiji() {
        return kouatsubunsansyuuryounichiji;
    }

    /**
     * 高圧分散終了日時
     * @param kouatsubunsansyuuryounichiji the kouatsubunsansyuuryounichiji to set
     */
    public void setKouatsubunsansyuuryounichiji(Timestamp kouatsubunsansyuuryounichiji) {
        this.kouatsubunsansyuuryounichiji = kouatsubunsansyuuryounichiji;
    }

    /**
     * 高圧分散停止担当者
     * @return the kouatsubunsanteishitantousya
     */
    public String getKouatsubunsanteishitantousya() {
        return kouatsubunsanteishitantousya;
    }

    /**
     * 高圧分散停止担当者
     * @param kouatsubunsanteishitantousya the kouatsubunsanteishitantousya to set
     */
    public void setKouatsubunsanteishitantousya(String kouatsubunsanteishitantousya) {
        this.kouatsubunsanteishitantousya = kouatsubunsanteishitantousya;
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