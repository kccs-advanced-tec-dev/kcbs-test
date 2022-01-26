/*
 * Copyright 2022 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2022/01/11<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS wxf<br>
 * 変更理由	新規作成<br>
 * ===============================================================================<br>
 */
/**
 * 比表面積測定記録のモデルクラスです。
 *
 * @author KCSS wxf
 * @since 2022/01/11
 */
public class GXHDO502BModel implements Cloneable {

    /**
     * 行番号
     */
    private Integer rowIndx = null;
    /**
     * 削除ﾁｪｯｸﾎﾞｯｸｽ
     */
    private String chkboxvalue = "";
    /**
     * 品名
     */
    private String hinmei = "";
    /**
     * ﾛｯﾄ
     */
    private String lot = "";
    /**
     * ﾊﾟｽ回数(PASS)
     */
    private Integer passkaisuu = null;
    /**
     * 流れ品
     */
    private Integer nagarehin = null;
    /**
     * WIPLotNo
     */
    private String wiplotno = "";
    /**
     * 測定物
     */
    private String sokuteibutu = null;
    /**
     * 測定回数
     */
    private String sokuteikaisuu = null;
    /**
     * No
     */
    private Integer no = null;
    /**
     * 工程
     */
    private String koutei = null;
    /**
     * 測定者
     */
    private String sokuteisya = "";
    /**
     * 乾燥皿の種類
     */
    private String kansouzaranosyurui = null;
    /**
     * ﾙﾂﾎﾞNo
     */
    private Integer rutubono = null;
    /**
     * 測定重量規格
     */
    private String sokuteijyuuryoukikaku = "";
    /**
     * 測定重量(g)
     */
    private BigDecimal sokuteijyuuryou = null;
    /**
     * 乾燥温度(℃)
     */
    private Integer kansouondo = null;
    /**
     * 乾燥時間規格
     */
    private String kansoujikankikaku = "";
    /**
     * 乾燥開始日時
     */
    private String kansoukaisinichij = null;
    /**
     * 乾燥終了日時
     */
    private String kansousyuuryounichiji = null;
    /**
     * 脱脂炉号機(号機)
     */
    private Integer dassirogouki = null;
    /**
     * 脱脂温度(℃)
     */
    private Integer dassiondo = null;
    /**
     * 脱脂時間規格
     */
    private String dassijikankikaku = "";
    /**
     * 脱脂開始日時
     */
    private String dassikaisinichiji = null;
    /**
     * 脱脂終了日時
     */
    private String dassisyuuryounichiji = null;
    /**
     * 乾燥担当者
     */
    private String kansoutantousya = "";
    /**
     * 前処理温度(℃)
     */
    private Integer maesyoriondo = null;
    /**
     * 前処理時間
     */
    private String maesyorijikan = "";
    /**
     * 前処理開始日時
     */
    private String maesyorikaisinichiji = null;
    /**
     * 前処理終了日時
     */
    private String maesyorisyuuryounichiji = null;
    /**
     * 前処理担当者
     */
    private String maesyoritantousya = "";
    /**
     * 測定後総重量(g)
     */
    private BigDecimal sokuteigosoujyuuryou = null;
    /**
     * 比表面積測定値(㎡/g)
     */
    private BigDecimal hihyoumensekisokuteiti = null;
    /**
     * 測定担当者
     */
    private String sokuteitantousya = "";
    /**
     * 備考
     */
    private String bikou = "";
    /**
     * 更新日時
     */
    private Timestamp kosinnichiji = null;

    /**
     * 品名の背景色
     */
    private String hinmeibgcolor = "";
    /**
     * ﾛｯﾄの背景色
     */
    private String lotbgcolor = "";
    /**
     * ﾊﾟｽ回数(PASS)の背景色
     */
    private String passkaisuubgcolor = "";
    /**
     * WIPLotNoの背景色
     */
    private String wiplotnobgcolor = "";
    /**
     * 測定物の背景色
     */
    private String sokuteibutubgcolor = "";
    /**
     * 測定回数の背景色
     */
    private String sokuteikaisuubgcolor = "";
    /**
     * Noの背景色
     */
    private String nobgcolor = "";
    /**
     * 工程の背景色
     */
    private String kouteibgcolor = "";
    /**
     * 測定者の背景色
     */
    private String sokuteisyabgcolor = "";
    /**
     * 乾燥皿の種類の背景色
     */
    private String kansouzaranosyuruibgcolor = "";
    /**
     * ﾙﾂﾎﾞNoの背景色
     */
    private String rutubonobgcolor = "";
    /**
     * 測定重量規格の背景色
     */
    private String sokuteijyuuryoukikakubgcolor = "";
    /**
     * 測定重量(g)の背景色
     */
    private String sokuteijyuuryoubgcolor = "";
    /**
     * 乾燥温度(℃)の背景色
     */
    private String kansouondobgcolor = "";
    /**
     * 乾燥時間規格の背景色
     */
    private String kansoujikankikakubgcolor = "";
    /**
     * 乾燥開始日時の背景色
     */
    private String kansoukaisinichijbgcolor = "";
    /**
     * 乾燥終了日時の背景色
     */
    private String kansousyuuryounichijibgcolor = "";
    /**
     * 脱脂炉号機(号機)の背景色
     */
    private String dassirogoukibgcolor = "";
    /**
     * 脱脂温度(℃)の背景色
     */
    private String dassiondobgcolor = "";
    /**
     * 脱脂時間規格の背景色
     */
    private String dassijikankikakubgcolor = "";
    /**
     * 脱脂開始日時の背景色
     */
    private String dassikaisinichijibgcolor = "";
    /**
     * 脱脂終了日時の背景色
     */
    private String dassisyuuryounichijibgcolor = "";
    /**
     * 乾燥担当者の背景色
     */
    private String kansoutantousyabgcolor = "";
    /**
     * 前処理温度(℃)の背景色
     */
    private String maesyoriondobgcolor = "";
    /**
     * 前処理時間の背景色
     */
    private String maesyorijikanbgcolor = "";
    /**
     * 前処理開始日時の背景色
     */
    private String maesyorikaisinichijibgcolor = "";
    /**
     * 前処理終了日時の背景色
     */
    private String maesyorisyuuryounichijibgcolor = "";
    /**
     * 前処理担当者の背景色
     */
    private String maesyoritantousyabgcolor = "";
    /**
     * 測定後総重量(g)の背景色
     */
    private String sokuteigosoujyuuryoubgcolor = "";
    /**
     * 比表面積測定値(㎡/g)の背景色
     */
    private String hihyoumensekisokuteitibgcolor = "";
    /**
     * 測定担当者の背景色
     */
    private String sokuteitantousyabgcolor = "";
    /**
     * 備考の背景色
     */
    private String bikoubgcolor = "";

    /**
     * 状態Flag: 0:登録、1:更新
     */
    private String jyoutaiFlg = "";

    /**
     * 削除ﾁｪｯｸﾎﾞｯｸｽ表示、非表示
     */
    private String chkboxrender = "";

    /**
     * 品名使用可、使用不可
     */
    private String hinmeidisabled = "";

    /**
     * ﾛｯﾄ使用可、使用不可
     */
    private String lotdisabled = "";

    /**
     * ﾊﾟｽ回数(PASS)使用可、使用不可
     */
    private String passkaisuudisabled = "";

    /**
     * 測定回数使用可、使用不可
     */
    private String sokuteikaisuudisabled = "";

    /**
     * 測定重量規格使用可、使用不可
     */
    private String sokuteijyuuryoukikakudisabled = "";

    /**
     * 乾燥時間規格使用可、使用不可
     */
    private String kansoujikankikakudisabled = "";

    /**
     * 脱脂時間規格使用可、使用不可
     */
    private String dassijikankikakudisabled = "";

    /**
     * 前処理時間使用可、使用不可
     */
    private String maesyorijikandisabled = "";

    /**
     * クローン実装
     *
     * @return クローン
     * @throws java.lang.CloneNotSupportedException
     */
    @Override
    public GXHDO502BModel clone() throws CloneNotSupportedException {
        return (GXHDO502BModel) super.clone();
    }

    /**
     * 行番号
     *
     * @return the rowIndx
     */
    public Integer getRowIndx() {
        return rowIndx;
    }

    /**
     * 行番号
     *
     * @param rowIndx the rowIndx to set
     */
    public void setRowIndx(Integer rowIndx) {
        this.rowIndx = rowIndx;
    }

    /**
     * 削除ﾁｪｯｸﾎﾞｯｸｽ
     *
     * @return the chkboxvalue
     */
    public String getChkboxvalue() {
        return chkboxvalue;
    }

    /**
     * 削除ﾁｪｯｸﾎﾞｯｸｽ
     *
     * @param chkboxvalue the chkboxvalue to set
     */
    public void setChkboxvalue(String chkboxvalue) {
        this.chkboxvalue = chkboxvalue;
    }

    /**
     * 品名
     *
     * @return the hinmei
     */
    public String getHinmei() {
        return hinmei;
    }

    /**
     * 品名
     *
     * @param hinmei the hinmei to set
     */
    public void setHinmei(String hinmei) {
        this.hinmei = hinmei;
    }

    /**
     * ﾛｯﾄ
     *
     * @return the lot
     */
    public String getLot() {
        return lot;
    }

    /**
     * ﾛｯﾄ
     *
     * @param lot the lot to set
     */
    public void setLot(String lot) {
        this.lot = lot;
    }

    /**
     * ﾊﾟｽ回数(PASS)
     *
     * @return the passkaisuu
     */
    public Integer getPasskaisuu() {
        return passkaisuu;
    }

    /**
     * ﾊﾟｽ回数(PASS)
     *
     * @param passkaisuu the passkaisuu to set
     */
    public void setPasskaisuu(Integer passkaisuu) {
        this.passkaisuu = passkaisuu;
    }

    /**
     * 流れ品
     *
     * @return the nagarehin
     */
    public Integer getNagarehin() {
        return nagarehin;
    }

    /**
     * 流れ品
     *
     * @param nagarehin the nagarehin to set
     */
    public void setNagarehin(Integer nagarehin) {
        this.nagarehin = nagarehin;
    }

    /**
     * WIPLotNo
     *
     * @return the wiplotno
     */
    public String getWiplotno() {
        return wiplotno;
    }

    /**
     * WIPLotNo
     *
     * @param wiplotno the wiplotno to set
     */
    public void setWiplotno(String wiplotno) {
        this.wiplotno = wiplotno;
    }

    /**
     * 測定物
     *
     * @return the sokuteibutu
     */
    public String getSokuteibutu() {
        return sokuteibutu;
    }

    /**
     * 測定物
     *
     * @param sokuteibutu the sokuteibutu to set
     */
    public void setSokuteibutu(String sokuteibutu) {
        this.sokuteibutu = sokuteibutu;
    }

    /**
     * 測定回数
     *
     * @return the sokuteikaisuu
     */
    public String getSokuteikaisuu() {
        return sokuteikaisuu;
    }

    /**
     * 測定回数
     *
     * @param sokuteikaisuu the sokuteikaisuu to set
     */
    public void setSokuteikaisuu(String sokuteikaisuu) {
        this.sokuteikaisuu = sokuteikaisuu;
    }

    /**
     * No
     *
     * @return the no
     */
    public Integer getNo() {
        return no;
    }

    /**
     * No
     *
     * @param no the no to set
     */
    public void setNo(Integer no) {
        this.no = no;
    }

    /**
     * 工程
     *
     * @return the koutei
     */
    public String getKoutei() {
        return koutei;
    }

    /**
     * 工程
     *
     * @param koutei the koutei to set
     */
    public void setKoutei(String koutei) {
        this.koutei = koutei;
    }

    /**
     * 測定者
     *
     * @return the sokuteisya
     */
    public String getSokuteisya() {
        return sokuteisya;
    }

    /**
     * 測定者
     *
     * @param sokuteisya the sokuteisya to set
     */
    public void setSokuteisya(String sokuteisya) {
        this.sokuteisya = sokuteisya;
    }

    /**
     * 乾燥皿の種類
     *
     * @return the kansouzaranosyurui
     */
    public String getKansouzaranosyurui() {
        return kansouzaranosyurui;
    }

    /**
     * 乾燥皿の種類
     *
     * @param kansouzaranosyurui the kansouzaranosyurui to set
     */
    public void setKansouzaranosyurui(String kansouzaranosyurui) {
        this.kansouzaranosyurui = kansouzaranosyurui;
    }

    /**
     * ﾙﾂﾎﾞNo
     *
     * @return the rutubono
     */
    public Integer getRutubono() {
        return rutubono;
    }

    /**
     * ﾙﾂﾎﾞNo
     *
     * @param rutubono the rutubono to set
     */
    public void setRutubono(Integer rutubono) {
        this.rutubono = rutubono;
    }

    /**
     * 測定重量規格
     *
     * @return the sokuteijyuuryoukikaku
     */
    public String getSokuteijyuuryoukikaku() {
        return sokuteijyuuryoukikaku;
    }

    /**
     * 測定重量規格
     *
     * @param sokuteijyuuryoukikaku the sokuteijyuuryoukikaku to set
     */
    public void setSokuteijyuuryoukikaku(String sokuteijyuuryoukikaku) {
        this.sokuteijyuuryoukikaku = sokuteijyuuryoukikaku;
    }

    /**
     * 測定重量(g)
     *
     * @return the sokuteijyuuryou
     */
    public BigDecimal getSokuteijyuuryou() {
        return sokuteijyuuryou;
    }

    /**
     * 測定重量(g)
     *
     * @param sokuteijyuuryou the sokuteijyuuryou to set
     */
    public void setSokuteijyuuryou(BigDecimal sokuteijyuuryou) {
        this.sokuteijyuuryou = sokuteijyuuryou;
    }

    /**
     * 乾燥温度(℃)
     *
     * @return the kansouondo
     */
    public Integer getKansouondo() {
        return kansouondo;
    }

    /**
     * 乾燥温度(℃)
     *
     * @param kansouondo the kansouondo to set
     */
    public void setKansouondo(Integer kansouondo) {
        this.kansouondo = kansouondo;
    }

    /**
     * 乾燥時間規格
     *
     * @return the kansoujikankikaku
     */
    public String getKansoujikankikaku() {
        return kansoujikankikaku;
    }

    /**
     * 乾燥時間規格
     *
     * @param kansoujikankikaku the kansoujikankikaku to set
     */
    public void setKansoujikankikaku(String kansoujikankikaku) {
        this.kansoujikankikaku = kansoujikankikaku;
    }

    /**
     * 乾燥開始日時
     *
     * @return the kansoukaisinichij
     */
    public String getKansoukaisinichij() {
        return kansoukaisinichij;
    }

    /**
     * 乾燥開始日時
     *
     * @param kansoukaisinichij the kansoukaisinichij to set
     */
    public void setKansoukaisinichij(String kansoukaisinichij) {
        this.kansoukaisinichij = kansoukaisinichij;
    }

    /**
     * 乾燥終了日時
     *
     * @return the kansousyuuryounichiji
     */
    public String getKansousyuuryounichiji() {
        return kansousyuuryounichiji;
    }

    /**
     * 乾燥終了日時
     *
     * @param kansousyuuryounichiji the kansousyuuryounichiji to set
     */
    public void setKansousyuuryounichiji(String kansousyuuryounichiji) {
        this.kansousyuuryounichiji = kansousyuuryounichiji;
    }

    /**
     * 脱脂炉号機(号機)
     *
     * @return the dassirogouki
     */
    public Integer getDassirogouki() {
        return dassirogouki;
    }

    /**
     * 脱脂炉号機(号機)
     *
     * @param dassirogouki the dassirogouki to set
     */
    public void setDassirogouki(Integer dassirogouki) {
        this.dassirogouki = dassirogouki;
    }

    /**
     * 脱脂温度(℃)
     *
     * @return the dassiondo
     */
    public Integer getDassiondo() {
        return dassiondo;
    }

    /**
     * 脱脂温度(℃)
     *
     * @param dassiondo the dassiondo to set
     */
    public void setDassiondo(Integer dassiondo) {
        this.dassiondo = dassiondo;
    }

    /**
     * 脱脂時間規格
     *
     * @return the dassijikankikaku
     */
    public String getDassijikankikaku() {
        return dassijikankikaku;
    }

    /**
     * 脱脂時間規格
     *
     * @param dassijikankikaku the dassijikankikaku to set
     */
    public void setDassijikankikaku(String dassijikankikaku) {
        this.dassijikankikaku = dassijikankikaku;
    }

    /**
     * 脱脂開始日時
     *
     * @return the dassikaisinichiji
     */
    public String getDassikaisinichiji() {
        return dassikaisinichiji;
    }

    /**
     * 脱脂開始日時
     *
     * @param dassikaisinichiji the dassikaisinichiji to set
     */
    public void setDassikaisinichiji(String dassikaisinichiji) {
        this.dassikaisinichiji = dassikaisinichiji;
    }

    /**
     * 脱脂終了日時
     *
     * @return the dassisyuuryounichiji
     */
    public String getDassisyuuryounichiji() {
        return dassisyuuryounichiji;
    }

    /**
     * 脱脂終了日時
     *
     * @param dassisyuuryounichiji the dassisyuuryounichiji to set
     */
    public void setDassisyuuryounichiji(String dassisyuuryounichiji) {
        this.dassisyuuryounichiji = dassisyuuryounichiji;
    }

    /**
     * 乾燥担当者
     *
     * @return the kansoutantousya
     */
    public String getKansoutantousya() {
        return kansoutantousya;
    }

    /**
     * 乾燥担当者
     *
     * @param kansoutantousya the kansoutantousya to set
     */
    public void setKansoutantousya(String kansoutantousya) {
        this.kansoutantousya = kansoutantousya;
    }

    /**
     * 前処理温度(℃)
     *
     * @return the maesyoriondo
     */
    public Integer getMaesyoriondo() {
        return maesyoriondo;
    }

    /**
     * 前処理温度(℃)
     *
     * @param maesyoriondo the maesyoriondo to set
     */
    public void setMaesyoriondo(Integer maesyoriondo) {
        this.maesyoriondo = maesyoriondo;
    }

    /**
     * 前処理時間
     *
     * @return the maesyorijikan
     */
    public String getMaesyorijikan() {
        return maesyorijikan;
    }

    /**
     * 前処理時間
     *
     * @param maesyorijikan the maesyorijikan to set
     */
    public void setMaesyorijikan(String maesyorijikan) {
        this.maesyorijikan = maesyorijikan;
    }

    /**
     * 前処理開始日時
     *
     * @return the maesyorikaisinichiji
     */
    public String getMaesyorikaisinichiji() {
        return maesyorikaisinichiji;
    }

    /**
     * 前処理開始日時
     *
     * @param maesyorikaisinichiji the maesyorikaisinichiji to set
     */
    public void setMaesyorikaisinichiji(String maesyorikaisinichiji) {
        this.maesyorikaisinichiji = maesyorikaisinichiji;
    }

    /**
     * 前処理終了日時
     *
     * @return the maesyorisyuuryounichiji
     */
    public String getMaesyorisyuuryounichiji() {
        return maesyorisyuuryounichiji;
    }

    /**
     * 前処理終了日時
     *
     * @param maesyorisyuuryounichiji the maesyorisyuuryounichiji to set
     */
    public void setMaesyorisyuuryounichiji(String maesyorisyuuryounichiji) {
        this.maesyorisyuuryounichiji = maesyorisyuuryounichiji;
    }

    /**
     * 前処理担当者
     *
     * @return the maesyoritantousya
     */
    public String getMaesyoritantousya() {
        return maesyoritantousya;
    }

    /**
     * 前処理担当者
     *
     * @param maesyoritantousya the maesyoritantousya to set
     */
    public void setMaesyoritantousya(String maesyoritantousya) {
        this.maesyoritantousya = maesyoritantousya;
    }

    /**
     * 測定後総重量(g)
     *
     * @return the sokuteigosoujyuuryou
     */
    public BigDecimal getSokuteigosoujyuuryou() {
        return sokuteigosoujyuuryou;
    }

    /**
     * 測定後総重量(g)
     *
     * @param sokuteigosoujyuuryou the sokuteigosoujyuuryou to set
     */
    public void setSokuteigosoujyuuryou(BigDecimal sokuteigosoujyuuryou) {
        this.sokuteigosoujyuuryou = sokuteigosoujyuuryou;
    }

    /**
     * 比表面積測定値(㎡/g)
     *
     * @return the hihyoumensekisokuteiti
     */
    public BigDecimal getHihyoumensekisokuteiti() {
        return hihyoumensekisokuteiti;
    }

    /**
     * 比表面積測定値(㎡/g)
     *
     * @param hihyoumensekisokuteiti the hihyoumensekisokuteiti to set
     */
    public void setHihyoumensekisokuteiti(BigDecimal hihyoumensekisokuteiti) {
        this.hihyoumensekisokuteiti = hihyoumensekisokuteiti;
    }

    /**
     * 測定担当者
     *
     * @return the sokuteitantousya
     */
    public String getSokuteitantousya() {
        return sokuteitantousya;
    }

    /**
     * 測定担当者
     *
     * @param sokuteitantousya the sokuteitantousya to set
     */
    public void setSokuteitantousya(String sokuteitantousya) {
        this.sokuteitantousya = sokuteitantousya;
    }

    /**
     * 備考
     *
     * @return the bikou
     */
    public String getBikou() {
        return bikou;
    }

    /**
     * 備考
     *
     * @param bikou the bikou to set
     */
    public void setBikou(String bikou) {
        this.bikou = bikou;
    }

    /**
     * 更新日時
     *
     * @return the kosinnichiji
     */
    public Timestamp getKosinnichiji() {
        return kosinnichiji;
    }

    /**
     * 更新日時
     *
     * @param kosinnichiji the kosinnichiji to set
     */
    public void setKosinnichiji(Timestamp kosinnichiji) {
        this.kosinnichiji = kosinnichiji;
    }

    /**
     * 品名の背景色
     *
     * @return the hinmeibgcolor
     */
    public String getHinmeibgcolor() {
        return hinmeibgcolor;
    }

    /**
     * 品名の背景色
     *
     * @param hinmeibgcolor the hinmeibgcolor to set
     */
    public void setHinmeibgcolor(String hinmeibgcolor) {
        this.hinmeibgcolor = hinmeibgcolor;
    }

    /**
     * ﾛｯﾄの背景色
     *
     * @return the lotbgcolor
     */
    public String getLotbgcolor() {
        return lotbgcolor;
    }

    /**
     * ﾛｯﾄの背景色
     *
     * @param lotbgcolor the lotbgcolor to set
     */
    public void setLotbgcolor(String lotbgcolor) {
        this.lotbgcolor = lotbgcolor;
    }

    /**
     * ﾊﾟｽ回数(PASS)の背景色
     *
     * @return the passkaisuubgcolor
     */
    public String getPasskaisuubgcolor() {
        return passkaisuubgcolor;
    }

    /**
     * ﾊﾟｽ回数(PASS)の背景色
     *
     * @param passkaisuubgcolor the passkaisuubgcolor to set
     */
    public void setPasskaisuubgcolor(String passkaisuubgcolor) {
        this.passkaisuubgcolor = passkaisuubgcolor;
    }

    /**
     * WIPLotNoの背景色
     *
     * @return the wiplotnobgcolor
     */
    public String getWiplotnobgcolor() {
        return wiplotnobgcolor;
    }

    /**
     * WIPLotNoの背景色
     *
     * @param wiplotnobgcolor the wiplotnobgcolor to set
     */
    public void setWiplotnobgcolor(String wiplotnobgcolor) {
        this.wiplotnobgcolor = wiplotnobgcolor;
    }

    /**
     * 測定物の背景色
     *
     * @return the sokuteibutubgcolor
     */
    public String getSokuteibutubgcolor() {
        return sokuteibutubgcolor;
    }

    /**
     * 測定物の背景色
     *
     * @param sokuteibutubgcolor the sokuteibutubgcolor to set
     */
    public void setSokuteibutubgcolor(String sokuteibutubgcolor) {
        this.sokuteibutubgcolor = sokuteibutubgcolor;
    }

    /**
     * 測定回数の背景色
     *
     * @return the sokuteikaisuubgcolor
     */
    public String getSokuteikaisuubgcolor() {
        return sokuteikaisuubgcolor;
    }

    /**
     * 測定回数の背景色
     *
     * @param sokuteikaisuubgcolor the sokuteikaisuubgcolor to set
     */
    public void setSokuteikaisuubgcolor(String sokuteikaisuubgcolor) {
        this.sokuteikaisuubgcolor = sokuteikaisuubgcolor;
    }

    /**
     * Noの背景色
     *
     * @return the nobgcolor
     */
    public String getNobgcolor() {
        return nobgcolor;
    }

    /**
     * Noの背景色
     *
     * @param nobgcolor the nobgcolor to set
     */
    public void setNobgcolor(String nobgcolor) {
        this.nobgcolor = nobgcolor;
    }

    /**
     * 工程の背景色
     *
     * @return the kouteibgcolor
     */
    public String getKouteibgcolor() {
        return kouteibgcolor;
    }

    /**
     * 工程の背景色
     *
     * @param kouteibgcolor the kouteibgcolor to set
     */
    public void setKouteibgcolor(String kouteibgcolor) {
        this.kouteibgcolor = kouteibgcolor;
    }

    /**
     * 測定者の背景色
     *
     * @return the sokuteisyabgcolor
     */
    public String getSokuteisyabgcolor() {
        return sokuteisyabgcolor;
    }

    /**
     * 測定者の背景色
     *
     * @param sokuteisyabgcolor the sokuteisyabgcolor to set
     */
    public void setSokuteisyabgcolor(String sokuteisyabgcolor) {
        this.sokuteisyabgcolor = sokuteisyabgcolor;
    }

    /**
     * 乾燥皿の種類の背景色
     *
     * @return the kansouzaranosyuruibgcolor
     */
    public String getKansouzaranosyuruibgcolor() {
        return kansouzaranosyuruibgcolor;
    }

    /**
     * 乾燥皿の種類の背景色
     *
     * @param kansouzaranosyuruibgcolor the kansouzaranosyuruibgcolor to set
     */
    public void setKansouzaranosyuruibgcolor(String kansouzaranosyuruibgcolor) {
        this.kansouzaranosyuruibgcolor = kansouzaranosyuruibgcolor;
    }

    /**
     * ﾙﾂﾎﾞNoの背景色
     *
     * @return the rutubonobgcolor
     */
    public String getRutubonobgcolor() {
        return rutubonobgcolor;
    }

    /**
     * ﾙﾂﾎﾞNoの背景色
     *
     * @param rutubonobgcolor the rutubonobgcolor to set
     */
    public void setRutubonobgcolor(String rutubonobgcolor) {
        this.rutubonobgcolor = rutubonobgcolor;
    }

    /**
     * 測定重量規格の背景色
     *
     * @return the sokuteijyuuryoukikakubgcolor
     */
    public String getSokuteijyuuryoukikakubgcolor() {
        return sokuteijyuuryoukikakubgcolor;
    }

    /**
     * 測定重量規格の背景色
     *
     * @param sokuteijyuuryoukikakubgcolor the sokuteijyuuryoukikakubgcolor to
     * set
     */
    public void setSokuteijyuuryoukikakubgcolor(String sokuteijyuuryoukikakubgcolor) {
        this.sokuteijyuuryoukikakubgcolor = sokuteijyuuryoukikakubgcolor;
    }

    /**
     * 測定重量(g)の背景色
     *
     * @return the sokuteijyuuryoubgcolor
     */
    public String getSokuteijyuuryoubgcolor() {
        return sokuteijyuuryoubgcolor;
    }

    /**
     * 測定重量(g)の背景色
     *
     * @param sokuteijyuuryoubgcolor the sokuteijyuuryoubgcolor to set
     */
    public void setSokuteijyuuryoubgcolor(String sokuteijyuuryoubgcolor) {
        this.sokuteijyuuryoubgcolor = sokuteijyuuryoubgcolor;
    }

    /**
     * 乾燥温度(℃)の背景色
     *
     * @return the kansouondobgcolor
     */
    public String getKansouondobgcolor() {
        return kansouondobgcolor;
    }

    /**
     * 乾燥温度(℃)の背景色
     *
     * @param kansouondobgcolor the kansouondobgcolor to set
     */
    public void setKansouondobgcolor(String kansouondobgcolor) {
        this.kansouondobgcolor = kansouondobgcolor;
    }

    /**
     * 乾燥時間規格の背景色
     *
     * @return the kansoujikankikakubgcolor
     */
    public String getKansoujikankikakubgcolor() {
        return kansoujikankikakubgcolor;
    }

    /**
     * 乾燥時間規格の背景色
     *
     * @param kansoujikankikakubgcolor the kansoujikankikakubgcolor to set
     */
    public void setKansoujikankikakubgcolor(String kansoujikankikakubgcolor) {
        this.kansoujikankikakubgcolor = kansoujikankikakubgcolor;
    }

    /**
     * 乾燥開始日時の背景色
     *
     * @return the kansoukaisinichijbgcolor
     */
    public String getKansoukaisinichijbgcolor() {
        return kansoukaisinichijbgcolor;
    }

    /**
     * 乾燥開始日時の背景色
     *
     * @param kansoukaisinichijbgcolor the kansoukaisinichijbgcolor to set
     */
    public void setKansoukaisinichijbgcolor(String kansoukaisinichijbgcolor) {
        this.kansoukaisinichijbgcolor = kansoukaisinichijbgcolor;
    }

    /**
     * 乾燥終了日時の背景色
     *
     * @return the kansousyuuryounichijibgcolor
     */
    public String getKansousyuuryounichijibgcolor() {
        return kansousyuuryounichijibgcolor;
    }

    /**
     * 乾燥終了日時の背景色
     *
     * @param kansousyuuryounichijibgcolor the kansousyuuryounichijibgcolor to
     * set
     */
    public void setKansousyuuryounichijibgcolor(String kansousyuuryounichijibgcolor) {
        this.kansousyuuryounichijibgcolor = kansousyuuryounichijibgcolor;
    }

    /**
     * 脱脂炉号機(号機)の背景色
     *
     * @return the dassirogoukibgcolor
     */
    public String getDassirogoukibgcolor() {
        return dassirogoukibgcolor;
    }

    /**
     * 脱脂炉号機(号機)の背景色
     *
     * @param dassirogoukibgcolor the dassirogoukibgcolor to set
     */
    public void setDassirogoukibgcolor(String dassirogoukibgcolor) {
        this.dassirogoukibgcolor = dassirogoukibgcolor;
    }

    /**
     * 脱脂温度(℃)の背景色
     *
     * @return the dassiondobgcolor
     */
    public String getDassiondobgcolor() {
        return dassiondobgcolor;
    }

    /**
     * 脱脂温度(℃)の背景色
     *
     * @param dassiondobgcolor the dassiondobgcolor to set
     */
    public void setDassiondobgcolor(String dassiondobgcolor) {
        this.dassiondobgcolor = dassiondobgcolor;
    }

    /**
     * 脱脂時間規格の背景色
     *
     * @return the dassijikankikakubgcolor
     */
    public String getDassijikankikakubgcolor() {
        return dassijikankikakubgcolor;
    }

    /**
     * 脱脂時間規格の背景色
     *
     * @param dassijikankikakubgcolor the dassijikankikakubgcolor to set
     */
    public void setDassijikankikakubgcolor(String dassijikankikakubgcolor) {
        this.dassijikankikakubgcolor = dassijikankikakubgcolor;
    }

    /**
     * 脱脂開始日時の背景色
     *
     * @return the dassikaisinichijibgcolor
     */
    public String getDassikaisinichijibgcolor() {
        return dassikaisinichijibgcolor;
    }

    /**
     * 脱脂開始日時の背景色
     *
     * @param dassikaisinichijibgcolor the dassikaisinichijibgcolor to set
     */
    public void setDassikaisinichijibgcolor(String dassikaisinichijibgcolor) {
        this.dassikaisinichijibgcolor = dassikaisinichijibgcolor;
    }

    /**
     * 脱脂終了日時の背景色
     *
     * @return the dassisyuuryounichijibgcolor
     */
    public String getDassisyuuryounichijibgcolor() {
        return dassisyuuryounichijibgcolor;
    }

    /**
     * 脱脂終了日時の背景色
     *
     * @param dassisyuuryounichijibgcolor the dassisyuuryounichijibgcolor to set
     */
    public void setDassisyuuryounichijibgcolor(String dassisyuuryounichijibgcolor) {
        this.dassisyuuryounichijibgcolor = dassisyuuryounichijibgcolor;
    }

    /**
     * 乾燥担当者の背景色
     *
     * @return the kansoutantousyabgcolor
     */
    public String getKansoutantousyabgcolor() {
        return kansoutantousyabgcolor;
    }

    /**
     * 乾燥担当者の背景色
     *
     * @param kansoutantousyabgcolor the kansoutantousyabgcolor to set
     */
    public void setKansoutantousyabgcolor(String kansoutantousyabgcolor) {
        this.kansoutantousyabgcolor = kansoutantousyabgcolor;
    }

    /**
     * 前処理温度(℃)の背景色
     *
     * @return the maesyoriondobgcolor
     */
    public String getMaesyoriondobgcolor() {
        return maesyoriondobgcolor;
    }

    /**
     * 前処理温度(℃)の背景色
     *
     * @param maesyoriondobgcolor the maesyoriondobgcolor to set
     */
    public void setMaesyoriondobgcolor(String maesyoriondobgcolor) {
        this.maesyoriondobgcolor = maesyoriondobgcolor;
    }

    /**
     * 前処理時間の背景色
     *
     * @return the maesyorijikanbgcolor
     */
    public String getMaesyorijikanbgcolor() {
        return maesyorijikanbgcolor;
    }

    /**
     * 前処理時間の背景色
     *
     * @param maesyorijikanbgcolor the maesyorijikanbgcolor to set
     */
    public void setMaesyorijikanbgcolor(String maesyorijikanbgcolor) {
        this.maesyorijikanbgcolor = maesyorijikanbgcolor;
    }

    /**
     * 前処理開始日時の背景色
     *
     * @return the maesyorikaisinichijibgcolor
     */
    public String getMaesyorikaisinichijibgcolor() {
        return maesyorikaisinichijibgcolor;
    }

    /**
     * 前処理開始日時の背景色
     *
     * @param maesyorikaisinichijibgcolor the maesyorikaisinichijibgcolor to set
     */
    public void setMaesyorikaisinichijibgcolor(String maesyorikaisinichijibgcolor) {
        this.maesyorikaisinichijibgcolor = maesyorikaisinichijibgcolor;
    }

    /**
     * 前処理終了日時の背景色
     *
     * @return the maesyorisyuuryounichijibgcolor
     */
    public String getMaesyorisyuuryounichijibgcolor() {
        return maesyorisyuuryounichijibgcolor;
    }

    /**
     * 前処理終了日時の背景色
     *
     * @param maesyorisyuuryounichijibgcolor the maesyorisyuuryounichijibgcolor
     * to set
     */
    public void setMaesyorisyuuryounichijibgcolor(String maesyorisyuuryounichijibgcolor) {
        this.maesyorisyuuryounichijibgcolor = maesyorisyuuryounichijibgcolor;
    }

    /**
     * 前処理担当者の背景色
     *
     * @return the maesyoritantousyabgcolor
     */
    public String getMaesyoritantousyabgcolor() {
        return maesyoritantousyabgcolor;
    }

    /**
     * 前処理担当者の背景色
     *
     * @param maesyoritantousyabgcolor the maesyoritantousyabgcolor to set
     */
    public void setMaesyoritantousyabgcolor(String maesyoritantousyabgcolor) {
        this.maesyoritantousyabgcolor = maesyoritantousyabgcolor;
    }

    /**
     * 測定後総重量(g)の背景色
     *
     * @return the sokuteigosoujyuuryoubgcolor
     */
    public String getSokuteigosoujyuuryoubgcolor() {
        return sokuteigosoujyuuryoubgcolor;
    }

    /**
     * 測定後総重量(g)の背景色
     *
     * @param sokuteigosoujyuuryoubgcolor the sokuteigosoujyuuryoubgcolor to set
     */
    public void setSokuteigosoujyuuryoubgcolor(String sokuteigosoujyuuryoubgcolor) {
        this.sokuteigosoujyuuryoubgcolor = sokuteigosoujyuuryoubgcolor;
    }

    /**
     * 比表面積測定値(㎡/g)の背景色
     *
     * @return the hihyoumensekisokuteitibgcolor
     */
    public String getHihyoumensekisokuteitibgcolor() {
        return hihyoumensekisokuteitibgcolor;
    }

    /**
     * 比表面積測定値(㎡/g)の背景色
     *
     * @param hihyoumensekisokuteitibgcolor the hihyoumensekisokuteitibgcolor to
     * set
     */
    public void setHihyoumensekisokuteitibgcolor(String hihyoumensekisokuteitibgcolor) {
        this.hihyoumensekisokuteitibgcolor = hihyoumensekisokuteitibgcolor;
    }

    /**
     * 測定担当者の背景色
     *
     * @return the sokuteitantousyabgcolor
     */
    public String getSokuteitantousyabgcolor() {
        return sokuteitantousyabgcolor;
    }

    /**
     * 測定担当者の背景色
     *
     * @param sokuteitantousyabgcolor the sokuteitantousyabgcolor to set
     */
    public void setSokuteitantousyabgcolor(String sokuteitantousyabgcolor) {
        this.sokuteitantousyabgcolor = sokuteitantousyabgcolor;
    }

    /**
     * 備考の背景色
     *
     * @return the bikoubgcolor
     */
    public String getBikoubgcolor() {
        return bikoubgcolor;
    }

    /**
     * 備考の背景色
     *
     * @param bikoubgcolor the bikoubgcolor to set
     */
    public void setBikoubgcolor(String bikoubgcolor) {
        this.bikoubgcolor = bikoubgcolor;
    }

    /**
     * 状態Flag: 0:登録、1:更新
     *
     * @return the jyoutaiFlg
     */
    public String getJyoutaiFlg() {
        return jyoutaiFlg;
    }

    /**
     * 状態Flag: 0:登録、1:更新
     *
     * @param jyoutaiFlg the jyoutaiFlg to set
     */
    public void setJyoutaiFlg(String jyoutaiFlg) {
        this.jyoutaiFlg = jyoutaiFlg;
    }

    /**
     * 削除ﾁｪｯｸﾎﾞｯｸｽ表示、非表示
     *
     * @return the chkboxrender
     */
    public String getChkboxrender() {
        return chkboxrender;
    }

    /**
     * 削除ﾁｪｯｸﾎﾞｯｸｽ表示、非表示
     *
     * @param chkboxrender the chkboxrender to set
     */
    public void setChkboxrender(String chkboxrender) {
        this.chkboxrender = chkboxrender;
    }

    /**
     * 品名使用可、使用不可
     *
     * @return the hinmeidisabled
     */
    public String getHinmeidisabled() {
        return hinmeidisabled;
    }

    /**
     * 品名使用可、使用不可
     *
     * @param hinmeidisabled the hinmeidisabled to set
     */
    public void setHinmeidisabled(String hinmeidisabled) {
        this.hinmeidisabled = hinmeidisabled;
    }

    /**
     * ﾛｯﾄ使用可、使用不可
     *
     * @return the lotdisabled
     */
    public String getLotdisabled() {
        return lotdisabled;
    }

    /**
     * ﾛｯﾄ使用可、使用不可
     *
     * @param lotdisabled the lotdisabled to set
     */
    public void setLotdisabled(String lotdisabled) {
        this.lotdisabled = lotdisabled;
    }

    /**
     * ﾊﾟｽ回数(PASS)使用可、使用不可
     *
     * @return the passkaisuudisabled
     */
    public String getPasskaisuudisabled() {
        return passkaisuudisabled;
    }

    /**
     * ﾊﾟｽ回数(PASS)使用可、使用不可
     *
     * @param passkaisuudisabled the passkaisuudisabled to set
     */
    public void setPasskaisuudisabled(String passkaisuudisabled) {
        this.passkaisuudisabled = passkaisuudisabled;
    }

    /**
     * 測定回数使用可、使用不可
     *
     * @return the sokuteikaisuudisabled
     */
    public String getSokuteikaisuudisabled() {
        return sokuteikaisuudisabled;
    }

    /**
     * 測定回数使用可、使用不可
     *
     * @param sokuteikaisuudisabled the sokuteikaisuudisabled to set
     */
    public void setSokuteikaisuudisabled(String sokuteikaisuudisabled) {
        this.sokuteikaisuudisabled = sokuteikaisuudisabled;
    }

    /**
     * 測定重量規格使用可、使用不可
     *
     * @return the sokuteijyuuryoukikakudisabled
     */
    public String getSokuteijyuuryoukikakudisabled() {
        return sokuteijyuuryoukikakudisabled;
    }

    /**
     * 測定重量規格使用可、使用不可
     *
     * @param sokuteijyuuryoukikakudisabled the sokuteijyuuryoukikakudisabled to
     * set
     */
    public void setSokuteijyuuryoukikakudisabled(String sokuteijyuuryoukikakudisabled) {
        this.sokuteijyuuryoukikakudisabled = sokuteijyuuryoukikakudisabled;
    }

    /**
     * 乾燥時間規格使用可、使用不可
     *
     * @return the kansoujikankikakudisabled
     */
    public String getKansoujikankikakudisabled() {
        return kansoujikankikakudisabled;
    }

    /**
     * 乾燥時間規格使用可、使用不可
     *
     * @param kansoujikankikakudisabled the kansoujikankikakudisabled to set
     */
    public void setKansoujikankikakudisabled(String kansoujikankikakudisabled) {
        this.kansoujikankikakudisabled = kansoujikankikakudisabled;
    }

    /**
     * 脱脂時間規格使用可、使用不可
     *
     * @return the dassijikankikakudisabled
     */
    public String getDassijikankikakudisabled() {
        return dassijikankikakudisabled;
    }

    /**
     * 脱脂時間規格使用可、使用不可
     *
     * @param dassijikankikakudisabled the dassijikankikakudisabled to set
     */
    public void setDassijikankikakudisabled(String dassijikankikakudisabled) {
        this.dassijikankikakudisabled = dassijikankikakudisabled;
    }

    /**
     * 前処理時間使用可、使用不可
     *
     * @return the maesyorijikandisabled
     */
    public String getMaesyorijikandisabled() {
        return maesyorijikandisabled;
    }

    /**
     * 前処理時間使用可、使用不可
     *
     * @param maesyorijikandisabled the maesyorijikandisabled to set
     */
    public void setMaesyorijikandisabled(String maesyorijikandisabled) {
        this.maesyorijikandisabled = maesyorijikandisabled;
    }
}
