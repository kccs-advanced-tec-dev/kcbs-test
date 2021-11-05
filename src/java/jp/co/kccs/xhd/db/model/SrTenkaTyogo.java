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
 * 変更日	2021/10/11<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SR_TENKA_TYOGO(添加材ｽﾗﾘｰ作製・添加材調合)のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2021/10/11
 */
public class SrTenkaTyogo {
    
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
     * 添加材ｽﾗﾘｰ品名
     */
    private String tenkazaislurryhinmei;

    /**
     * 添加材ｽﾗﾘｰLotNo
     */
    private String tenkazaislurrylotno;

    /**
     * ﾛｯﾄ区分
     */
    private String lotkubun;

    /**
     * 秤量号機
     */
    private String hyouryougouki;

    /**
     * 風袋重量
     */
    private Integer fuutaijyuuryou;

    /**
     * 秤量開始日時
     */
    private Timestamp hyouryoukaisinichiji;

    /**
     * 添加材①_材料品名
     */
    private String tenkazai1_zairyoumei;

    /**
     * 添加材①_調合量規格
     */
    private String tenkazai1_tyougouryoukikaku;

    /**
     * 添加材①_部材在庫No1
     */
    private String tenkazai1_buzaizaikolotno1;

    /**
     * 添加材①_調合量1
     */
    private String tenkazai1_tyougouryou1;

    /**
     * 添加材①_部材在庫No2
     */
    private String tenkazai1_buzaizaikolotno2;

    /**
     * 添加材①_調合量2
     */
    private String tenkazai1_tyougouryou2;

    /**
     * 添加材②_材料品名
     */
    private String tenkazai2_zairyoumei;

    /**
     * 添加材②_調合量規格
     */
    private String tenkazai2_tyougouryoukikaku;

    /**
     * 添加材②_部材在庫No1
     */
    private String tenkazai2_buzaizaikolotno1;

    /**
     * 添加材②_調合量1
     */
    private String tenkazai2_tyougouryou1;

    /**
     * 添加材②_部材在庫No2
     */
    private String tenkazai2_buzaizaikolotno2;

    /**
     * 添加材②_調合量2
     */
    private String tenkazai2_tyougouryou2;

    /**
     * 添加材③_材料品名
     */
    private String tenkazai3_zairyoumei;

    /**
     * 添加材③_調合量規格
     */
    private String tenkazai3_tyougouryoukikaku;

    /**
     * 添加材③_部材在庫No1
     */
    private String tenkazai3_buzaizaikolotno1;

    /**
     * 添加材③_調合量1
     */
    private String tenkazai3_tyougouryou1;

    /**
     * 添加材③_部材在庫No2
     */
    private String tenkazai3_buzaizaikolotno2;

    /**
     * 添加材③_調合量2
     */
    private String tenkazai3_tyougouryou2;

    /**
     * 添加材④_材料品名
     */
    private String tenkazai4_zairyoumei;

    /**
     * 添加材④_調合量規格
     */
    private String tenkazai4_tyougouryoukikaku;

    /**
     * 添加材④_部材在庫No1
     */
    private String tenkazai4_buzaizaikolotno1;

    /**
     * 添加材④_調合量1
     */
    private String tenkazai4_tyougouryou1;

    /**
     * 添加材④_部材在庫No2
     */
    private String tenkazai4_buzaizaikolotno2;

    /**
     * 添加材④_調合量2
     */
    private String tenkazai4_tyougouryou2;

    /**
     * 添加材⑤_材料品名
     */
    private String tenkazai5_zairyoumei;

    /**
     * 添加材⑤_調合量規格
     */
    private String tenkazai5_tyougouryoukikaku;

    /**
     * 添加材⑤_部材在庫No1
     */
    private String tenkazai5_buzaizaikolotno1;

    /**
     * 添加材⑤_調合量1
     */
    private String tenkazai5_tyougouryou1;

    /**
     * 添加材⑤_部材在庫No2
     */
    private String tenkazai5_buzaizaikolotno2;

    /**
     * 添加材⑤_調合量2
     */
    private String tenkazai5_tyougouryou2;

    /**
     * 添加材⑥_材料品名
     */
    private String tenkazai6_zairyoumei;

    /**
     * 添加材⑥_調合量規格
     */
    private String tenkazai6_tyougouryoukikaku;

    /**
     * 添加材⑥_部材在庫No1
     */
    private String tenkazai6_buzaizaikolotno1;

    /**
     * 添加材⑥_調合量1
     */
    private String tenkazai6_tyougouryou1;

    /**
     * 添加材⑥_部材在庫No2
     */
    private String tenkazai6_buzaizaikolotno2;

    /**
     * 添加材⑥_調合量2
     */
    private String tenkazai6_tyougouryou2;

    /**
     * 添加材⑦_材料品名
     */
    private String tenkazai7_zairyoumei;

    /**
     * 添加材⑦_調合量規格
     */
    private String tenkazai7_tyougouryoukikaku;

    /**
     * 添加材⑦_部材在庫No1
     */
    private String tenkazai7_buzaizaikolotno1;

    /**
     * 添加材⑦_調合量1
     */
    private String tenkazai7_tyougouryou1;

    /**
     * 添加材⑦_部材在庫No2
     */
    private String tenkazai7_buzaizaikolotno2;

    /**
     * 添加材⑦_調合量2
     */
    private String tenkazai7_tyougouryou2;

    /**
     * 添加材⑧_材料品名
     */
    private String tenkazai8_zairyoumei;

    /**
     * 添加材⑧_調合量規格
     */
    private String tenkazai8_tyougouryoukikaku;

    /**
     * 添加材⑧_部材在庫No1
     */
    private String tenkazai8_buzaizaikolotno1;

    /**
     * 添加材⑧_調合量1
     */
    private String tenkazai8_tyougouryou1;

    /**
     * 添加材⑧_部材在庫No2
     */
    private String tenkazai8_buzaizaikolotno2;

    /**
     * 添加材⑧_調合量2
     */
    private String tenkazai8_tyougouryou2;

    /**
     * 添加材⑨_材料品名
     */
    private String tenkazai9_zairyoumei;

    /**
     * 添加材⑨_調合量規格
     */
    private String tenkazai9_tyougouryoukikaku;

    /**
     * 添加材⑨_部材在庫No1
     */
    private String tenkazai9_buzaizaikolotno1;

    /**
     * 添加材⑨_調合量1
     */
    private String tenkazai9_tyougouryou1;

    /**
     * 添加材⑨_部材在庫No2
     */
    private String tenkazai9_buzaizaikolotno2;

    /**
     * 添加材⑨_調合量2
     */
    private String tenkazai9_tyougouryou2;

    /**
     * 秤量終了日時
     */
    private Timestamp hyouryousyuuryounichiji;

    /**
     * 担当者
     */
    private String tantousya;

    /**
     * 確認者
     */
    private String kakuninsya;

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
    private Integer sakujyoflg;

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
     * 添加材ｽﾗﾘｰ品名
     * @return the tenkazaislurryhinmei
     */
    public String getTenkazaislurryhinmei() {
        return tenkazaislurryhinmei;
    }

    /**
     * 添加材ｽﾗﾘｰ品名
     * @param tenkazaislurryhinmei the tenkazaislurryhinmei to set
     */
    public void setTenkazaislurryhinmei(String tenkazaislurryhinmei) {
        this.tenkazaislurryhinmei = tenkazaislurryhinmei;
    }

    /**
     * 添加材ｽﾗﾘｰLotNo
     * @return the tenkazaislurrylotno
     */
    public String getTenkazaislurrylotno() {
        return tenkazaislurrylotno;
    }

    /**
     * 添加材ｽﾗﾘｰLotNo
     * @param tenkazaislurrylotno the tenkazaislurrylotno to set
     */
    public void setTenkazaislurrylotno(String tenkazaislurrylotno) {
        this.tenkazaislurrylotno = tenkazaislurrylotno;
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
     * @return the hyouryougouki
     */
    public String getHyouryougouki() {
        return hyouryougouki;
    }

    /**
     * 秤量号機
     * @param hyouryougouki the hyouryougouki to set
     */
    public void setHyouryougouki(String hyouryougouki) {
        this.hyouryougouki = hyouryougouki;
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
     * 秤量開始日時
     * @return the hyouryoukaisinichiji
     */
    public Timestamp getHyouryoukaisinichiji() {
        return hyouryoukaisinichiji;
    }

    /**
     * 秤量開始日時
     * @param hyouryoukaisinichiji the hyouryoukaisinichiji to set
     */
    public void setHyouryoukaisinichiji(Timestamp hyouryoukaisinichiji) {
        this.hyouryoukaisinichiji = hyouryoukaisinichiji;
    }

    /**
     * 添加材①_材料品名
     * @return the tenkazai1_zairyoumei
     */
    public String getTenkazai1_zairyoumei() {
        return tenkazai1_zairyoumei;
    }

    /**
     * 添加材①_材料品名
     * @param tenkazai1_zairyoumei the tenkazai1_zairyoumei to set
     */
    public void setTenkazai1_zairyoumei(String tenkazai1_zairyoumei) {
        this.tenkazai1_zairyoumei = tenkazai1_zairyoumei;
    }

    /**
     * 添加材①_調合量規格
     * @return the tenkazai1_tyougouryoukikaku
     */
    public String getTenkazai1_tyougouryoukikaku() {
        return tenkazai1_tyougouryoukikaku;
    }

    /**
     * 添加材①_調合量規格
     * @param tenkazai1_tyougouryoukikaku the tenkazai1_tyougouryoukikaku to set
     */
    public void setTenkazai1_tyougouryoukikaku(String tenkazai1_tyougouryoukikaku) {
        this.tenkazai1_tyougouryoukikaku = tenkazai1_tyougouryoukikaku;
    }

    /**
     * 添加材①_部材在庫No1
     * @return the tenkazai1_buzaizaikolotno1
     */
    public String getTenkazai1_buzaizaikolotno1() {
        return tenkazai1_buzaizaikolotno1;
    }

    /**
     * 添加材①_部材在庫No1
     * @param tenkazai1_buzaizaikolotno1 the tenkazai1_buzaizaikolotno1 to set
     */
    public void setTenkazai1_buzaizaikolotno1(String tenkazai1_buzaizaikolotno1) {
        this.tenkazai1_buzaizaikolotno1 = tenkazai1_buzaizaikolotno1;
    }

    /**
     * 添加材①_調合量1
     * @return the tenkazai1_tyougouryou1
     */
    public String getTenkazai1_tyougouryou1() {
        return tenkazai1_tyougouryou1;
    }

    /**
     * 添加材①_調合量1
     * @param tenkazai1_tyougouryou1 the tenkazai1_tyougouryou1 to set
     */
    public void setTenkazai1_tyougouryou1(String tenkazai1_tyougouryou1) {
        this.tenkazai1_tyougouryou1 = tenkazai1_tyougouryou1;
    }

    /**
     * 添加材①_部材在庫No2
     * @return the tenkazai1_buzaizaikolotno2
     */
    public String getTenkazai1_buzaizaikolotno2() {
        return tenkazai1_buzaizaikolotno2;
    }

    /**
     * 添加材①_部材在庫No2
     * @param tenkazai1_buzaizaikolotno2 the tenkazai1_buzaizaikolotno2 to set
     */
    public void setTenkazai1_buzaizaikolotno2(String tenkazai1_buzaizaikolotno2) {
        this.tenkazai1_buzaizaikolotno2 = tenkazai1_buzaizaikolotno2;
    }

    /**
     * 添加材①_調合量2
     * @return the tenkazai1_tyougouryou2
     */
    public String getTenkazai1_tyougouryou2() {
        return tenkazai1_tyougouryou2;
    }

    /**
     * 添加材①_調合量2
     * @param tenkazai1_tyougouryou2 the tenkazai1_tyougouryou2 to set
     */
    public void setTenkazai1_tyougouryou2(String tenkazai1_tyougouryou2) {
        this.tenkazai1_tyougouryou2 = tenkazai1_tyougouryou2;
    }

    /**
     * 添加材②_材料品名
     * @return the tenkazai2_zairyoumei
     */
    public String getTenkazai2_zairyoumei() {
        return tenkazai2_zairyoumei;
    }

    /**
     * 添加材②_材料品名
     * @param tenkazai2_zairyoumei the tenkazai2_zairyoumei to set
     */
    public void setTenkazai2_zairyoumei(String tenkazai2_zairyoumei) {
        this.tenkazai2_zairyoumei = tenkazai2_zairyoumei;
    }

    /**
     * 添加材②_調合量規格
     * @return the tenkazai2_tyougouryoukikaku
     */
    public String getTenkazai2_tyougouryoukikaku() {
        return tenkazai2_tyougouryoukikaku;
    }

    /**
     * 添加材②_調合量規格
     * @param tenkazai2_tyougouryoukikaku the tenkazai2_tyougouryoukikaku to set
     */
    public void setTenkazai2_tyougouryoukikaku(String tenkazai2_tyougouryoukikaku) {
        this.tenkazai2_tyougouryoukikaku = tenkazai2_tyougouryoukikaku;
    }

    /**
     * 添加材②_部材在庫No1
     * @return the tenkazai2_buzaizaikolotno1
     */
    public String getTenkazai2_buzaizaikolotno1() {
        return tenkazai2_buzaizaikolotno1;
    }

    /**
     * 添加材②_部材在庫No1
     * @param tenkazai2_buzaizaikolotno1 the tenkazai2_buzaizaikolotno1 to set
     */
    public void setTenkazai2_buzaizaikolotno1(String tenkazai2_buzaizaikolotno1) {
        this.tenkazai2_buzaizaikolotno1 = tenkazai2_buzaizaikolotno1;
    }

    /**
     * 添加材②_調合量1
     * @return the tenkazai2_tyougouryou1
     */
    public String getTenkazai2_tyougouryou1() {
        return tenkazai2_tyougouryou1;
    }

    /**
     * 添加材②_調合量1
     * @param tenkazai2_tyougouryou1 the tenkazai2_tyougouryou1 to set
     */
    public void setTenkazai2_tyougouryou1(String tenkazai2_tyougouryou1) {
        this.tenkazai2_tyougouryou1 = tenkazai2_tyougouryou1;
    }

    /**
     * 添加材②_部材在庫No2
     * @return the tenkazai2_buzaizaikolotno2
     */
    public String getTenkazai2_buzaizaikolotno2() {
        return tenkazai2_buzaizaikolotno2;
    }

    /**
     * 添加材②_部材在庫No2
     * @param tenkazai2_buzaizaikolotno2 the tenkazai2_buzaizaikolotno2 to set
     */
    public void setTenkazai2_buzaizaikolotno2(String tenkazai2_buzaizaikolotno2) {
        this.tenkazai2_buzaizaikolotno2 = tenkazai2_buzaizaikolotno2;
    }

    /**
     * 添加材②_調合量2
     * @return the tenkazai2_tyougouryou2
     */
    public String getTenkazai2_tyougouryou2() {
        return tenkazai2_tyougouryou2;
    }

    /**
     * 添加材②_調合量2
     * @param tenkazai2_tyougouryou2 the tenkazai2_tyougouryou2 to set
     */
    public void setTenkazai2_tyougouryou2(String tenkazai2_tyougouryou2) {
        this.tenkazai2_tyougouryou2 = tenkazai2_tyougouryou2;
    }

    /**
     * 添加材③_材料品名
     * @return the tenkazai3_zairyoumei
     */
    public String getTenkazai3_zairyoumei() {
        return tenkazai3_zairyoumei;
    }

    /**
     * 添加材③_材料品名
     * @param tenkazai3_zairyoumei the tenkazai3_zairyoumei to set
     */
    public void setTenkazai3_zairyoumei(String tenkazai3_zairyoumei) {
        this.tenkazai3_zairyoumei = tenkazai3_zairyoumei;
    }

    /**
     * 添加材③_調合量規格
     * @return the tenkazai3_tyougouryoukikaku
     */
    public String getTenkazai3_tyougouryoukikaku() {
        return tenkazai3_tyougouryoukikaku;
    }

    /**
     * 添加材③_調合量規格
     * @param tenkazai3_tyougouryoukikaku the tenkazai3_tyougouryoukikaku to set
     */
    public void setTenkazai3_tyougouryoukikaku(String tenkazai3_tyougouryoukikaku) {
        this.tenkazai3_tyougouryoukikaku = tenkazai3_tyougouryoukikaku;
    }

    /**
     * 添加材③_部材在庫No1
     * @return the tenkazai3_buzaizaikolotno1
     */
    public String getTenkazai3_buzaizaikolotno1() {
        return tenkazai3_buzaizaikolotno1;
    }

    /**
     * 添加材③_部材在庫No1
     * @param tenkazai3_buzaizaikolotno1 the tenkazai3_buzaizaikolotno1 to set
     */
    public void setTenkazai3_buzaizaikolotno1(String tenkazai3_buzaizaikolotno1) {
        this.tenkazai3_buzaizaikolotno1 = tenkazai3_buzaizaikolotno1;
    }

    /**
     * 添加材③_調合量1
     * @return the tenkazai3_tyougouryou1
     */
    public String getTenkazai3_tyougouryou1() {
        return tenkazai3_tyougouryou1;
    }

    /**
     * 添加材③_調合量1
     * @param tenkazai3_tyougouryou1 the tenkazai3_tyougouryou1 to set
     */
    public void setTenkazai3_tyougouryou1(String tenkazai3_tyougouryou1) {
        this.tenkazai3_tyougouryou1 = tenkazai3_tyougouryou1;
    }

    /**
     * 添加材③_部材在庫No2
     * @return the tenkazai3_buzaizaikolotno2
     */
    public String getTenkazai3_buzaizaikolotno2() {
        return tenkazai3_buzaizaikolotno2;
    }

    /**
     * 添加材③_部材在庫No2
     * @param tenkazai3_buzaizaikolotno2 the tenkazai3_buzaizaikolotno2 to set
     */
    public void setTenkazai3_buzaizaikolotno2(String tenkazai3_buzaizaikolotno2) {
        this.tenkazai3_buzaizaikolotno2 = tenkazai3_buzaizaikolotno2;
    }

    /**
     * 添加材③_調合量2
     * @return the tenkazai3_tyougouryou2
     */
    public String getTenkazai3_tyougouryou2() {
        return tenkazai3_tyougouryou2;
    }

    /**
     * 添加材③_調合量2
     * @param tenkazai3_tyougouryou2 the tenkazai3_tyougouryou2 to set
     */
    public void setTenkazai3_tyougouryou2(String tenkazai3_tyougouryou2) {
        this.tenkazai3_tyougouryou2 = tenkazai3_tyougouryou2;
    }

    /**
     * 添加材④_材料品名
     * @return the tenkazai4_zairyoumei
     */
    public String getTenkazai4_zairyoumei() {
        return tenkazai4_zairyoumei;
    }

    /**
     * 添加材④_材料品名
     * @param tenkazai4_zairyoumei the tenkazai4_zairyoumei to set
     */
    public void setTenkazai4_zairyoumei(String tenkazai4_zairyoumei) {
        this.tenkazai4_zairyoumei = tenkazai4_zairyoumei;
    }

    /**
     * 添加材④_調合量規格
     * @return the tenkazai4_tyougouryoukikaku
     */
    public String getTenkazai4_tyougouryoukikaku() {
        return tenkazai4_tyougouryoukikaku;
    }

    /**
     * 添加材④_調合量規格
     * @param tenkazai4_tyougouryoukikaku the tenkazai4_tyougouryoukikaku to set
     */
    public void setTenkazai4_tyougouryoukikaku(String tenkazai4_tyougouryoukikaku) {
        this.tenkazai4_tyougouryoukikaku = tenkazai4_tyougouryoukikaku;
    }

    /**
     * 添加材④_部材在庫No1
     * @return the tenkazai4_buzaizaikolotno1
     */
    public String getTenkazai4_buzaizaikolotno1() {
        return tenkazai4_buzaizaikolotno1;
    }

    /**
     * 添加材④_部材在庫No1
     * @param tenkazai4_buzaizaikolotno1 the tenkazai4_buzaizaikolotno1 to set
     */
    public void setTenkazai4_buzaizaikolotno1(String tenkazai4_buzaizaikolotno1) {
        this.tenkazai4_buzaizaikolotno1 = tenkazai4_buzaizaikolotno1;
    }

    /**
     * 添加材④_調合量1
     * @return the tenkazai4_tyougouryou1
     */
    public String getTenkazai4_tyougouryou1() {
        return tenkazai4_tyougouryou1;
    }

    /**
     * 添加材④_調合量1
     * @param tenkazai4_tyougouryou1 the tenkazai4_tyougouryou1 to set
     */
    public void setTenkazai4_tyougouryou1(String tenkazai4_tyougouryou1) {
        this.tenkazai4_tyougouryou1 = tenkazai4_tyougouryou1;
    }

    /**
     * 添加材④_部材在庫No2
     * @return the tenkazai4_buzaizaikolotno2
     */
    public String getTenkazai4_buzaizaikolotno2() {
        return tenkazai4_buzaizaikolotno2;
    }

    /**
     * 添加材④_部材在庫No2
     * @param tenkazai4_buzaizaikolotno2 the tenkazai4_buzaizaikolotno2 to set
     */
    public void setTenkazai4_buzaizaikolotno2(String tenkazai4_buzaizaikolotno2) {
        this.tenkazai4_buzaizaikolotno2 = tenkazai4_buzaizaikolotno2;
    }

    /**
     * 添加材④_調合量2
     * @return the tenkazai4_tyougouryou2
     */
    public String getTenkazai4_tyougouryou2() {
        return tenkazai4_tyougouryou2;
    }

    /**
     * 添加材④_調合量2
     * @param tenkazai4_tyougouryou2 the tenkazai4_tyougouryou2 to set
     */
    public void setTenkazai4_tyougouryou2(String tenkazai4_tyougouryou2) {
        this.tenkazai4_tyougouryou2 = tenkazai4_tyougouryou2;
    }

    /**
     * 添加材⑤_材料品名
     * @return the tenkazai5_zairyoumei
     */
    public String getTenkazai5_zairyoumei() {
        return tenkazai5_zairyoumei;
    }

    /**
     * 添加材⑤_材料品名
     * @param tenkazai5_zairyoumei the tenkazai5_zairyoumei to set
     */
    public void setTenkazai5_zairyoumei(String tenkazai5_zairyoumei) {
        this.tenkazai5_zairyoumei = tenkazai5_zairyoumei;
    }

    /**
     * 添加材⑤_調合量規格
     * @return the tenkazai5_tyougouryoukikaku
     */
    public String getTenkazai5_tyougouryoukikaku() {
        return tenkazai5_tyougouryoukikaku;
    }

    /**
     * 添加材⑤_調合量規格
     * @param tenkazai5_tyougouryoukikaku the tenkazai5_tyougouryoukikaku to set
     */
    public void setTenkazai5_tyougouryoukikaku(String tenkazai5_tyougouryoukikaku) {
        this.tenkazai5_tyougouryoukikaku = tenkazai5_tyougouryoukikaku;
    }

    /**
     * 添加材⑤_部材在庫No1
     * @return the tenkazai5_buzaizaikolotno1
     */
    public String getTenkazai5_buzaizaikolotno1() {
        return tenkazai5_buzaizaikolotno1;
    }

    /**
     * 添加材⑤_部材在庫No1
     * @param tenkazai5_buzaizaikolotno1 the tenkazai5_buzaizaikolotno1 to set
     */
    public void setTenkazai5_buzaizaikolotno1(String tenkazai5_buzaizaikolotno1) {
        this.tenkazai5_buzaizaikolotno1 = tenkazai5_buzaizaikolotno1;
    }

    /**
     * 添加材⑤_調合量1
     * @return the tenkazai5_tyougouryou1
     */
    public String getTenkazai5_tyougouryou1() {
        return tenkazai5_tyougouryou1;
    }

    /**
     * 添加材⑤_調合量1
     * @param tenkazai5_tyougouryou1 the tenkazai5_tyougouryou1 to set
     */
    public void setTenkazai5_tyougouryou1(String tenkazai5_tyougouryou1) {
        this.tenkazai5_tyougouryou1 = tenkazai5_tyougouryou1;
    }

    /**
     * 添加材⑤_部材在庫No2
     * @return the tenkazai5_buzaizaikolotno2
     */
    public String getTenkazai5_buzaizaikolotno2() {
        return tenkazai5_buzaizaikolotno2;
    }

    /**
     * 添加材⑤_部材在庫No2
     * @param tenkazai5_buzaizaikolotno2 the tenkazai5_buzaizaikolotno2 to set
     */
    public void setTenkazai5_buzaizaikolotno2(String tenkazai5_buzaizaikolotno2) {
        this.tenkazai5_buzaizaikolotno2 = tenkazai5_buzaizaikolotno2;
    }

    /**
     * 添加材⑤_調合量2
     * @return the tenkazai5_tyougouryou2
     */
    public String getTenkazai5_tyougouryou2() {
        return tenkazai5_tyougouryou2;
    }

    /**
     * 添加材⑤_調合量2
     * @param tenkazai5_tyougouryou2 the tenkazai5_tyougouryou2 to set
     */
    public void setTenkazai5_tyougouryou2(String tenkazai5_tyougouryou2) {
        this.tenkazai5_tyougouryou2 = tenkazai5_tyougouryou2;
    }

    /**
     * 添加材⑥_材料品名
     * @return the tenkazai6_zairyoumei
     */
    public String getTenkazai6_zairyoumei() {
        return tenkazai6_zairyoumei;
    }

    /**
     * 添加材⑥_材料品名
     * @param tenkazai6_zairyoumei the tenkazai6_zairyoumei to set
     */
    public void setTenkazai6_zairyoumei(String tenkazai6_zairyoumei) {
        this.tenkazai6_zairyoumei = tenkazai6_zairyoumei;
    }

    /**
     * 添加材⑥_調合量規格
     * @return the tenkazai6_tyougouryoukikaku
     */
    public String getTenkazai6_tyougouryoukikaku() {
        return tenkazai6_tyougouryoukikaku;
    }

    /**
     * 添加材⑥_調合量規格
     * @param tenkazai6_tyougouryoukikaku the tenkazai6_tyougouryoukikaku to set
     */
    public void setTenkazai6_tyougouryoukikaku(String tenkazai6_tyougouryoukikaku) {
        this.tenkazai6_tyougouryoukikaku = tenkazai6_tyougouryoukikaku;
    }

    /**
     * 添加材⑥_部材在庫No1
     * @return the tenkazai6_buzaizaikolotno1
     */
    public String getTenkazai6_buzaizaikolotno1() {
        return tenkazai6_buzaizaikolotno1;
    }

    /**
     * 添加材⑥_部材在庫No1
     * @param tenkazai6_buzaizaikolotno1 the tenkazai6_buzaizaikolotno1 to set
     */
    public void setTenkazai6_buzaizaikolotno1(String tenkazai6_buzaizaikolotno1) {
        this.tenkazai6_buzaizaikolotno1 = tenkazai6_buzaizaikolotno1;
    }

    /**
     * 添加材⑥_調合量1
     * @return the tenkazai6_tyougouryou1
     */
    public String getTenkazai6_tyougouryou1() {
        return tenkazai6_tyougouryou1;
    }

    /**
     * 添加材⑥_調合量1
     * @param tenkazai6_tyougouryou1 the tenkazai6_tyougouryou1 to set
     */
    public void setTenkazai6_tyougouryou1(String tenkazai6_tyougouryou1) {
        this.tenkazai6_tyougouryou1 = tenkazai6_tyougouryou1;
    }

    /**
     * 添加材⑥_部材在庫No2
     * @return the tenkazai6_buzaizaikolotno2
     */
    public String getTenkazai6_buzaizaikolotno2() {
        return tenkazai6_buzaizaikolotno2;
    }

    /**
     * 添加材⑥_部材在庫No2
     * @param tenkazai6_buzaizaikolotno2 the tenkazai6_buzaizaikolotno2 to set
     */
    public void setTenkazai6_buzaizaikolotno2(String tenkazai6_buzaizaikolotno2) {
        this.tenkazai6_buzaizaikolotno2 = tenkazai6_buzaizaikolotno2;
    }

    /**
     * 添加材⑥_調合量2
     * @return the tenkazai6_tyougouryou2
     */
    public String getTenkazai6_tyougouryou2() {
        return tenkazai6_tyougouryou2;
    }

    /**
     * 添加材⑥_調合量2
     * @param tenkazai6_tyougouryou2 the tenkazai6_tyougouryou2 to set
     */
    public void setTenkazai6_tyougouryou2(String tenkazai6_tyougouryou2) {
        this.tenkazai6_tyougouryou2 = tenkazai6_tyougouryou2;
    }

    /**
     * 添加材⑦_材料品名
     * @return the tenkazai7_zairyoumei
     */
    public String getTenkazai7_zairyoumei() {
        return tenkazai7_zairyoumei;
    }

    /**
     * 添加材⑦_材料品名
     * @param tenkazai7_zairyoumei the tenkazai7_zairyoumei to set
     */
    public void setTenkazai7_zairyoumei(String tenkazai7_zairyoumei) {
        this.tenkazai7_zairyoumei = tenkazai7_zairyoumei;
    }

    /**
     * 添加材⑦_調合量規格
     * @return the tenkazai7_tyougouryoukikaku
     */
    public String getTenkazai7_tyougouryoukikaku() {
        return tenkazai7_tyougouryoukikaku;
    }

    /**
     * 添加材⑦_調合量規格
     * @param tenkazai7_tyougouryoukikaku the tenkazai7_tyougouryoukikaku to set
     */
    public void setTenkazai7_tyougouryoukikaku(String tenkazai7_tyougouryoukikaku) {
        this.tenkazai7_tyougouryoukikaku = tenkazai7_tyougouryoukikaku;
    }

    /**
     * 添加材⑦_部材在庫No1
     * @return the tenkazai7_buzaizaikolotno1
     */
    public String getTenkazai7_buzaizaikolotno1() {
        return tenkazai7_buzaizaikolotno1;
    }

    /**
     * 添加材⑦_部材在庫No1
     * @param tenkazai7_buzaizaikolotno1 the tenkazai7_buzaizaikolotno1 to set
     */
    public void setTenkazai7_buzaizaikolotno1(String tenkazai7_buzaizaikolotno1) {
        this.tenkazai7_buzaizaikolotno1 = tenkazai7_buzaizaikolotno1;
    }

    /**
     * 添加材⑦_調合量1
     * @return the tenkazai7_tyougouryou1
     */
    public String getTenkazai7_tyougouryou1() {
        return tenkazai7_tyougouryou1;
    }

    /**
     * 添加材⑦_調合量1
     * @param tenkazai7_tyougouryou1 the tenkazai7_tyougouryou1 to set
     */
    public void setTenkazai7_tyougouryou1(String tenkazai7_tyougouryou1) {
        this.tenkazai7_tyougouryou1 = tenkazai7_tyougouryou1;
    }

    /**
     * 添加材⑦_部材在庫No2
     * @return the tenkazai7_buzaizaikolotno2
     */
    public String getTenkazai7_buzaizaikolotno2() {
        return tenkazai7_buzaizaikolotno2;
    }

    /**
     * 添加材⑦_部材在庫No2
     * @param tenkazai7_buzaizaikolotno2 the tenkazai7_buzaizaikolotno2 to set
     */
    public void setTenkazai7_buzaizaikolotno2(String tenkazai7_buzaizaikolotno2) {
        this.tenkazai7_buzaizaikolotno2 = tenkazai7_buzaizaikolotno2;
    }

    /**
     * 添加材⑦_調合量2
     * @return the tenkazai7_tyougouryou2
     */
    public String getTenkazai7_tyougouryou2() {
        return tenkazai7_tyougouryou2;
    }

    /**
     * 添加材⑦_調合量2
     * @param tenkazai7_tyougouryou2 the tenkazai7_tyougouryou2 to set
     */
    public void setTenkazai7_tyougouryou2(String tenkazai7_tyougouryou2) {
        this.tenkazai7_tyougouryou2 = tenkazai7_tyougouryou2;
    }

    /**
     * 添加材⑧_材料品名
     * @return the tenkazai8_zairyoumei
     */
    public String getTenkazai8_zairyoumei() {
        return tenkazai8_zairyoumei;
    }

    /**
     * 添加材⑧_材料品名
     * @param tenkazai8_zairyoumei the tenkazai8_zairyoumei to set
     */
    public void setTenkazai8_zairyoumei(String tenkazai8_zairyoumei) {
        this.tenkazai8_zairyoumei = tenkazai8_zairyoumei;
    }

    /**
     * 添加材⑧_調合量規格
     * @return the tenkazai8_tyougouryoukikaku
     */
    public String getTenkazai8_tyougouryoukikaku() {
        return tenkazai8_tyougouryoukikaku;
    }

    /**
     * 添加材⑧_調合量規格
     * @param tenkazai8_tyougouryoukikaku the tenkazai8_tyougouryoukikaku to set
     */
    public void setTenkazai8_tyougouryoukikaku(String tenkazai8_tyougouryoukikaku) {
        this.tenkazai8_tyougouryoukikaku = tenkazai8_tyougouryoukikaku;
    }

    /**
     * 添加材⑧_部材在庫No1
     * @return the tenkazai8_buzaizaikolotno1
     */
    public String getTenkazai8_buzaizaikolotno1() {
        return tenkazai8_buzaizaikolotno1;
    }

    /**
     * 添加材⑧_部材在庫No1
     * @param tenkazai8_buzaizaikolotno1 the tenkazai8_buzaizaikolotno1 to set
     */
    public void setTenkazai8_buzaizaikolotno1(String tenkazai8_buzaizaikolotno1) {
        this.tenkazai8_buzaizaikolotno1 = tenkazai8_buzaizaikolotno1;
    }

    /**
     * 添加材⑧_調合量1
     * @return the tenkazai8_tyougouryou1
     */
    public String getTenkazai8_tyougouryou1() {
        return tenkazai8_tyougouryou1;
    }

    /**
     * 添加材⑧_調合量1
     * @param tenkazai8_tyougouryou1 the tenkazai8_tyougouryou1 to set
     */
    public void setTenkazai8_tyougouryou1(String tenkazai8_tyougouryou1) {
        this.tenkazai8_tyougouryou1 = tenkazai8_tyougouryou1;
    }

    /**
     * 添加材⑧_部材在庫No2
     * @return the tenkazai8_buzaizaikolotno2
     */
    public String getTenkazai8_buzaizaikolotno2() {
        return tenkazai8_buzaizaikolotno2;
    }

    /**
     * 添加材⑧_部材在庫No2
     * @param tenkazai8_buzaizaikolotno2 the tenkazai8_buzaizaikolotno2 to set
     */
    public void setTenkazai8_buzaizaikolotno2(String tenkazai8_buzaizaikolotno2) {
        this.tenkazai8_buzaizaikolotno2 = tenkazai8_buzaizaikolotno2;
    }

    /**
     * 添加材⑧_調合量2
     * @return the tenkazai8_tyougouryou2
     */
    public String getTenkazai8_tyougouryou2() {
        return tenkazai8_tyougouryou2;
    }

    /**
     * 添加材⑧_調合量2
     * @param tenkazai8_tyougouryou2 the tenkazai8_tyougouryou2 to set
     */
    public void setTenkazai8_tyougouryou2(String tenkazai8_tyougouryou2) {
        this.tenkazai8_tyougouryou2 = tenkazai8_tyougouryou2;
    }

    /**
     * 添加材⑨_材料品名
     * @return the tenkazai9_zairyoumei
     */
    public String getTenkazai9_zairyoumei() {
        return tenkazai9_zairyoumei;
    }

    /**
     * 添加材⑨_材料品名
     * @param tenkazai9_zairyoumei the tenkazai9_zairyoumei to set
     */
    public void setTenkazai9_zairyoumei(String tenkazai9_zairyoumei) {
        this.tenkazai9_zairyoumei = tenkazai9_zairyoumei;
    }

    /**
     * 添加材⑨_調合量規格
     * @return the tenkazai9_tyougouryoukikaku
     */
    public String getTenkazai9_tyougouryoukikaku() {
        return tenkazai9_tyougouryoukikaku;
    }

    /**
     * 添加材⑨_調合量規格
     * @param tenkazai9_tyougouryoukikaku the tenkazai9_tyougouryoukikaku to set
     */
    public void setTenkazai9_tyougouryoukikaku(String tenkazai9_tyougouryoukikaku) {
        this.tenkazai9_tyougouryoukikaku = tenkazai9_tyougouryoukikaku;
    }

    /**
     * 添加材⑨_部材在庫No1
     * @return the tenkazai9_buzaizaikolotno1
     */
    public String getTenkazai9_buzaizaikolotno1() {
        return tenkazai9_buzaizaikolotno1;
    }

    /**
     * 添加材⑨_部材在庫No1
     * @param tenkazai9_buzaizaikolotno1 the tenkazai9_buzaizaikolotno1 to set
     */
    public void setTenkazai9_buzaizaikolotno1(String tenkazai9_buzaizaikolotno1) {
        this.tenkazai9_buzaizaikolotno1 = tenkazai9_buzaizaikolotno1;
    }

    /**
     * 添加材⑨_調合量1
     * @return the tenkazai9_tyougouryou1
     */
    public String getTenkazai9_tyougouryou1() {
        return tenkazai9_tyougouryou1;
    }

    /**
     * 添加材⑨_調合量1
     * @param tenkazai9_tyougouryou1 the tenkazai9_tyougouryou1 to set
     */
    public void setTenkazai9_tyougouryou1(String tenkazai9_tyougouryou1) {
        this.tenkazai9_tyougouryou1 = tenkazai9_tyougouryou1;
    }

    /**
     * 添加材⑨_部材在庫No2
     * @return the tenkazai9_buzaizaikolotno2
     */
    public String getTenkazai9_buzaizaikolotno2() {
        return tenkazai9_buzaizaikolotno2;
    }

    /**
     * 添加材⑨_部材在庫No2
     * @param tenkazai9_buzaizaikolotno2 the tenkazai9_buzaizaikolotno2 to set
     */
    public void setTenkazai9_buzaizaikolotno2(String tenkazai9_buzaizaikolotno2) {
        this.tenkazai9_buzaizaikolotno2 = tenkazai9_buzaizaikolotno2;
    }

    /**
     * 添加材⑨_調合量2
     * @return the tenkazai9_tyougouryou2
     */
    public String getTenkazai9_tyougouryou2() {
        return tenkazai9_tyougouryou2;
    }

    /**
     * 添加材⑨_調合量2
     * @param tenkazai9_tyougouryou2 the tenkazai9_tyougouryou2 to set
     */
    public void setTenkazai9_tyougouryou2(String tenkazai9_tyougouryou2) {
        this.tenkazai9_tyougouryou2 = tenkazai9_tyougouryou2;
    }

    /**
     * 秤量終了日時
     * @return the hyouryousyuuryounichiji
     */
    public Timestamp getHyouryousyuuryounichiji() {
        return hyouryousyuuryounichiji;
    }

    /**
     * 秤量終了日時
     * @param hyouryousyuuryounichiji the hyouryousyuuryounichiji to set
     */
    public void setHyouryousyuuryounichiji(Timestamp hyouryousyuuryounichiji) {
        this.hyouryousyuuryounichiji = hyouryousyuuryounichiji;
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
     * @return the sakujyoflg
     */
    public Integer getSakujyoflg() {
        return sakujyoflg;
    }

    /**
     * 削除ﾌﾗｸﾞ
     * @param sakujyoflg the sakujyoflg to set
     */
    public void setSakujyoflg(Integer sakujyoflg) {
        this.sakujyoflg = sakujyoflg;
    }

}