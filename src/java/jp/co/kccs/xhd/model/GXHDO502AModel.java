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
 * 変更日	2022/01/10<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS zjh<br>
 * 変更理由	新規作成<br>
 * ===============================================================================<br>
 */
/**
 * 粒度記録のモデルクラスです。
 *
 * @author KCSS zjh
 * @since 2022/01/10
 */
public class GXHDO502AModel {

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
     * 品名の背景色
     */
    private String hinmeibgcolor = "";
    /**
     * ﾛｯﾄ
     */
    private String lot = "";
    /**
     * ﾛｯﾄの背景色
     */
    private String lotbgcolor = "";
    /**
     * ﾊﾟｽ回数(PASS)
     */
    private Integer passkaisuu = null;
    /**
     * ﾊﾟｽ回数(PASS)の背景色
     */
    private String passkaisuubgcolor = "";
    /**
     * WIPLotNo
     */
    private String wiplotno = "";
    /**
     * WIPLotNoの背景色
     */
    private String wiplotnobgcolor = "";
    /**
     * 測定回数
     */
    private String sokuteikaisuu = "";
    /**
     * 測定回数の背景色
     */
    private String sokuteikaisuubgcolor = "";
    /**
     * No
     */
    private Integer no = null;
    /**
     * noの背景色
     */
    private String nobgcolor = "";
    /**
     * 工程
     */
    private String koutei = "";
    /**
     * 工程の背景色
     */
    private String kouteibgcolor = "";
    /**
     * 測定者
     */
    private String sokuteisya = "";
    /**
     * 測定者の背景色
     */
    private String sokuteisyabgcolor = "";
    /**
     * 純水量
     */
    private String jyunsuiryou = "";
    /**
     * 純水量の背景色
     */
    private String jyunsuiryoubgcolor = "";
    /**
     * ｻﾝﾌﾟﾙ量
     */
    private String sampleryou = "";
    /**
     * ｻﾝﾌﾟﾙ量の背景色
     */
    private String sampleryoubgcolor = "";
    /**
     * 前超音波時間
     */
    private String maetyouonpajikan = "";
    /**
     * 前超音波時間の背景色
     */
    private String maetyouonpajikanbgcolor = "";
    /**
     * 測定号機(号機)
     */
    private Integer sokuteigoki = null;
    /**
     * 測定号機(号機)の背景色
     */
    private String sokuteigokibgcolor = "";
    /**
     * 透過率規格
     */
    private String toukaritukikaku = "";
    /**
     * 透過率規格の背景色
     */
    private String toukaritukikakubgcolor = "";
    /**
     * D50粒度規格
     */
    private String d50ryuudokikaku = "";
    /**
     * D50粒度規格の背景色
     */
    private String d50ryuudokikakubgcolor = "";
    /**
     * D50狙い値
     */
    private String d50neraiti = "";
    /**
     * D50狙い値の背景色
     */
    private String d50neraitibgcolor = "";
    /**
     * D50粒度測定値(μm)
     */
    private BigDecimal d50ryuudosokuteiti = null;
    /**
     * D50粒度測定値(μm)の背景色
     */
    private String d50ryuudosokuteitibgcolor = "";
    /**
     * D90粒度規格
     */
    private String d90ryuudokikaku = "";
    /**
     * D90粒度規格の背景色
     */
    private String d90ryuudokikakubgcolor = "";
    /**
     * D90粒度測定値(μm)
     */
    private BigDecimal d90ryuudosokuteiti = null;
    /**
     * D90粒度測定値(μm)の背景色
     */
    private String d90ryuudosokuteitibgcolor = "";
    /**
     * 透過率(%)
     */
    private BigDecimal toukaritu = null;
    /**
     * 透過率(%)の背景色
     */
    private String toukaritubgcolor = "";
    /**
     * 備考
     */
    private String bikou = "";
    /**
     * 更新日時
     */
    private Timestamp kosinnichiji = null;

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
     * 純水量使用可、使用不可
     */
    private String jyunsuiryoudisabled = "";
    /**
     * ｻﾝﾌﾟﾙ量使用可、使用不可
     */
    private String sampleryoudisabled = "";
    /**
     * 前超音波時間使用可、使用不可
     */
    private String maetyouonpajikandisabled = "";
    /**
     * 透過率規格使用可、使用不可
     */
    private String toukaritukikakudisabled = "";
    /**
     * D50粒度規格使用可、使用不可
     */
    private String d50ryuudokikakudisabled = "";
    /**
     * D50狙い値使用可、使用不可
     */
    private String d50neraitidisabled = "";
    /**
     * D90粒度規格使用可、使用不可
     */
    private String d90ryuudokikakudisabled = "";

    /**
     * 削除ﾁｪｯｸﾎﾞｯｸｽ
     *
     * @return the chkboxvalue
     */
    public String getChkboxvalue() {
        return chkboxvalue;
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
     * noの背景色
     *
     * @return the nobgcolor
     */
    public String getNobgcolor() {
        return nobgcolor;
    }

    /**
     * noの背景色
     *
     * @param nobgcolor the nobgcolor to set
     */
    public void setNobgcolor(String nobgcolor) {
        this.nobgcolor = nobgcolor;
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
     * 純水量
     *
     * @return the jyunsuiryou
     */
    public String getJyunsuiryou() {
        return jyunsuiryou;
    }

    /**
     * 純水量
     *
     * @param jyunsuiryou the jyunsuiryou to set
     */
    public void setJyunsuiryou(String jyunsuiryou) {
        this.jyunsuiryou = jyunsuiryou;
    }

    /**
     * 純水量の背景色
     *
     * @return the jyunsuiryoubgcolor
     */
    public String getJyunsuiryoubgcolor() {
        return jyunsuiryoubgcolor;
    }

    /**
     * 純水量の背景色
     *
     * @param jyunsuiryoubgcolor the jyunsuiryoubgcolor to set
     */
    public void setJyunsuiryoubgcolor(String jyunsuiryoubgcolor) {
        this.jyunsuiryoubgcolor = jyunsuiryoubgcolor;
    }

    /**
     * ｻﾝﾌﾟﾙ量
     *
     * @return the sampleryou
     */
    public String getSampleryou() {
        return sampleryou;
    }

    /**
     * ｻﾝﾌﾟﾙ量
     *
     * @param sampleryou the sampleryou to set
     */
    public void setSampleryou(String sampleryou) {
        this.sampleryou = sampleryou;
    }

    /**
     * ｻﾝﾌﾟﾙ量の背景色
     *
     * @return the sampleryoubgcolor
     */
    public String getSampleryoubgcolor() {
        return sampleryoubgcolor;
    }

    /**
     * ｻﾝﾌﾟﾙ量の背景色
     *
     * @param sampleryoubgcolor the sampleryoubgcolor to set
     */
    public void setSampleryoubgcolor(String sampleryoubgcolor) {
        this.sampleryoubgcolor = sampleryoubgcolor;
    }

    /**
     * 前超音波時間
     *
     * @return the maetyouonpajikan
     */
    public String getMaetyouonpajikan() {
        return maetyouonpajikan;
    }

    /**
     * 前超音波時間
     *
     * @param maetyouonpajikan the maetyouonpajikan to set
     */
    public void setMaetyouonpajikan(String maetyouonpajikan) {
        this.maetyouonpajikan = maetyouonpajikan;
    }

    /**
     * 前超音波時間の背景色
     *
     * @return the maetyouonpajikanbgcolor
     */
    public String getMaetyouonpajikanbgcolor() {
        return maetyouonpajikanbgcolor;
    }

    /**
     * 前超音波時間の背景色
     *
     * @param maetyouonpajikanbgcolor the maetyouonpajikanbgcolor to set
     */
    public void setMaetyouonpajikanbgcolor(String maetyouonpajikanbgcolor) {
        this.maetyouonpajikanbgcolor = maetyouonpajikanbgcolor;
    }

    /**
     * 測定号機(号機)
     *
     * @return the sokuteigoki
     */
    public Integer getSokuteigoki() {
        return sokuteigoki;
    }

    /**
     * 測定号機(号機)
     *
     * @param sokuteigoki the sokuteigoki to set
     */
    public void setSokuteigoki(Integer sokuteigoki) {
        this.sokuteigoki = sokuteigoki;
    }

    /**
     * 測定号機(号機)の背景色
     *
     * @return the sokuteigokibgcolor
     */
    public String getSokuteigokibgcolor() {
        return sokuteigokibgcolor;
    }

    /**
     * 測定号機(号機)の背景色
     *
     * @param sokuteigokibgcolor the sokuteigokibgcolor to set
     */
    public void setSokuteigokibgcolor(String sokuteigokibgcolor) {
        this.sokuteigokibgcolor = sokuteigokibgcolor;
    }

    /**
     * 透過率規格
     *
     * @return the toukaritukikaku
     */
    public String getToukaritukikaku() {
        return toukaritukikaku;
    }

    /**
     * 透過率規格
     *
     * @param toukaritukikaku the toukaritukikaku to set
     */
    public void setToukaritukikaku(String toukaritukikaku) {
        this.toukaritukikaku = toukaritukikaku;
    }

    /**
     * 透過率規格の背景色
     *
     * @return the toukaritukikakubgcolor
     */
    public String getToukaritukikakubgcolor() {
        return toukaritukikakubgcolor;
    }

    /**
     * 透過率規格の背景色
     *
     * @param toukaritukikakubgcolor the toukaritukikakubgcolor to set
     */
    public void setToukaritukikakubgcolor(String toukaritukikakubgcolor) {
        this.toukaritukikakubgcolor = toukaritukikakubgcolor;
    }

    /**
     * D50粒度規格
     *
     * @return the d50ryuudokikaku
     */
    public String getD50ryuudokikaku() {
        return d50ryuudokikaku;
    }

    /**
     * D50粒度規格
     *
     * @param d50ryuudokikaku the d50ryuudokikaku to set
     */
    public void setD50ryuudokikaku(String d50ryuudokikaku) {
        this.d50ryuudokikaku = d50ryuudokikaku;
    }

    /**
     * D50粒度規格の背景色
     *
     * @return the d50ryuudokikakubgcolor
     */
    public String getD50ryuudokikakubgcolor() {
        return d50ryuudokikakubgcolor;
    }

    /**
     * D50粒度規格の背景色
     *
     * @param d50ryuudokikakubgcolor the d50ryuudokikakubgcolor to set
     */
    public void setD50ryuudokikakubgcolor(String d50ryuudokikakubgcolor) {
        this.d50ryuudokikakubgcolor = d50ryuudokikakubgcolor;
    }

    /**
     * D50狙い値
     *
     * @return the d50neraiti
     */
    public String getD50neraiti() {
        return d50neraiti;
    }

    /**
     * D50狙い値
     *
     * @param d50neraiti the d50neraiti to set
     */
    public void setD50neraiti(String d50neraiti) {
        this.d50neraiti = d50neraiti;
    }

    /**
     * D50狙い値の背景色
     *
     * @return the d50neraitibgcolor
     */
    public String getD50neraitibgcolor() {
        return d50neraitibgcolor;
    }

    /**
     * D50狙い値の背景色
     *
     * @param d50neraitibgcolor the d50neraitibgcolor to set
     */
    public void setD50neraitibgcolor(String d50neraitibgcolor) {
        this.d50neraitibgcolor = d50neraitibgcolor;
    }

    /**
     * D50粒度測定値(μm)
     *
     * @return the d50ryuudosokuteiti
     */
    public BigDecimal getD50ryuudosokuteiti() {
        return d50ryuudosokuteiti;
    }

    /**
     * D50粒度測定値(μm)
     *
     * @param d50ryuudosokuteiti the d50ryuudosokuteiti to set
     */
    public void setD50ryuudosokuteiti(BigDecimal d50ryuudosokuteiti) {
        this.d50ryuudosokuteiti = d50ryuudosokuteiti;
    }

    /**
     * D50粒度測定値(μm)の背景色
     *
     * @return the d50ryuudosokuteitibgcolor
     */
    public String getD50ryuudosokuteitibgcolor() {
        return d50ryuudosokuteitibgcolor;
    }

    /**
     * D50粒度測定値(μm)の背景色
     *
     * @param d50ryuudosokuteitibgcolor the d50ryuudosokuteitibgcolor to set
     */
    public void setD50ryuudosokuteitibgcolor(String d50ryuudosokuteitibgcolor) {
        this.d50ryuudosokuteitibgcolor = d50ryuudosokuteitibgcolor;
    }

    /**
     * D90粒度規格
     *
     * @return the d90ryuudokikaku
     */
    public String getD90ryuudokikaku() {
        return d90ryuudokikaku;
    }

    /**
     * D90粒度規格
     *
     * @param d90ryuudokikaku the d90ryuudokikaku to set
     */
    public void setD90ryuudokikaku(String d90ryuudokikaku) {
        this.d90ryuudokikaku = d90ryuudokikaku;
    }

    /**
     * D90粒度規格の背景色
     *
     * @return the d90ryuudokikakubgcolor
     */
    public String getD90ryuudokikakubgcolor() {
        return d90ryuudokikakubgcolor;
    }

    /**
     * D90粒度規格の背景色
     *
     * @param d90ryuudokikakubgcolor the d90ryuudokikakubgcolor to set
     */
    public void setD90ryuudokikakubgcolor(String d90ryuudokikakubgcolor) {
        this.d90ryuudokikakubgcolor = d90ryuudokikakubgcolor;
    }

    /**
     * D90粒度測定値(μm)
     *
     * @return the d90ryuudosokuteiti
     */
    public BigDecimal getD90ryuudosokuteiti() {
        return d90ryuudosokuteiti;
    }

    /**
     * D90粒度測定値(μm)
     *
     * @param d90ryuudosokuteiti the d90ryuudosokuteiti to set
     */
    public void setD90ryuudosokuteiti(BigDecimal d90ryuudosokuteiti) {
        this.d90ryuudosokuteiti = d90ryuudosokuteiti;
    }

    /**
     * D90粒度測定値(μm)の背景色
     *
     * @return the d90ryuudosokuteitibgcolor
     */
    public String getD90ryuudosokuteitibgcolor() {
        return d90ryuudosokuteitibgcolor;
    }

    /**
     * D90粒度測定値(μm)の背景色
     *
     * @param d90ryuudosokuteitibgcolor the d90ryuudosokuteitibgcolor to set
     */
    public void setD90ryuudosokuteitibgcolor(String d90ryuudosokuteitibgcolor) {
        this.d90ryuudosokuteitibgcolor = d90ryuudosokuteitibgcolor;
    }

    /**
     * 透過率(%)
     *
     * @return the toukaritu
     */
    public BigDecimal getToukaritu() {
        return toukaritu;
    }

    /**
     * 透過率(%)
     *
     * @param toukaritu the toukaritu to set
     */
    public void setToukaritu(BigDecimal toukaritu) {
        this.toukaritu = toukaritu;
    }

    /**
     * 透過率(%)の背景色
     *
     * @return the toukaritubgcolor
     */
    public String getToukaritubgcolor() {
        return toukaritubgcolor;
    }

    /**
     * 透過率(%)の背景色
     *
     * @param toukaritubgcolor the toukaritubgcolor to set
     */
    public void setToukaritubgcolor(String toukaritubgcolor) {
        this.toukaritubgcolor = toukaritubgcolor;
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
     * 純水量使用可、使用不可
     *
     * @return the jyunsuiryoudisabled
     */
    public String getJyunsuiryoudisabled() {
        return jyunsuiryoudisabled;
    }

    /**
     * 純水量使用可、使用不可
     *
     * @param jyunsuiryoudisabled the jyunsuiryoudisabled to set
     */
    public void setJyunsuiryoudisabled(String jyunsuiryoudisabled) {
        this.jyunsuiryoudisabled = jyunsuiryoudisabled;
    }

    /**
     * ｻﾝﾌﾟﾙ量使用可、使用不可
     *
     * @return the sampleryoudisabled
     */
    public String getSampleryoudisabled() {
        return sampleryoudisabled;
    }

    /**
     * ｻﾝﾌﾟﾙ量使用可、使用不可
     *
     * @param sampleryoudisabled the sampleryoudisabled to set
     */
    public void setSampleryoudisabled(String sampleryoudisabled) {
        this.sampleryoudisabled = sampleryoudisabled;
    }

    /**
     * 前超音波時間使用可、使用不可
     *
     * @return the maetyouonpajikandisabled
     */
    public String getMaetyouonpajikandisabled() {
        return maetyouonpajikandisabled;
    }

    /**
     * 前超音波時間使用可、使用不可
     *
     * @param maetyouonpajikandisabled the maetyouonpajikandisabled to set
     */
    public void setMaetyouonpajikandisabled(String maetyouonpajikandisabled) {
        this.maetyouonpajikandisabled = maetyouonpajikandisabled;
    }

    /**
     * 透過率規格使用可、使用不可
     *
     * @return the toukaritukikakudisabled
     */
    public String getToukaritukikakudisabled() {
        return toukaritukikakudisabled;
    }

    /**
     * 透過率規格使用可、使用不可
     *
     * @param toukaritukikakudisabled the toukaritukikakudisabled to set
     */
    public void setToukaritukikakudisabled(String toukaritukikakudisabled) {
        this.toukaritukikakudisabled = toukaritukikakudisabled;
    }

    /**
     * D50粒度規格使用可、使用不可
     *
     * @return the d50ryuudokikakudisabled
     */
    public String getD50ryuudokikakudisabled() {
        return d50ryuudokikakudisabled;
    }

    /**
     * D50粒度規格使用可、使用不可
     *
     * @param d50ryuudokikakudisabled the d50ryuudokikakudisabled to set
     */
    public void setD50ryuudokikakudisabled(String d50ryuudokikakudisabled) {
        this.d50ryuudokikakudisabled = d50ryuudokikakudisabled;
    }

    /**
     * D50狙い値使用可、使用不可
     *
     * @return the d50neraitidisabled
     */
    public String getD50neraitidisabled() {
        return d50neraitidisabled;
    }

    /**
     * D50狙い値使用可、使用不可
     *
     * @param d50neraitidisabled the d50neraitidisabled to set
     */
    public void setD50neraitidisabled(String d50neraitidisabled) {
        this.d50neraitidisabled = d50neraitidisabled;
    }

    /**
     * D90粒度規格使用可、使用不可
     *
     * @return the d90ryuudokikakudisabled
     */
    public String getD90ryuudokikakudisabled() {
        return d90ryuudokikakudisabled;
    }

    /**
     * D90粒度規格使用可、使用不可
     *
     * @param d90ryuudokikakudisabled the d90ryuudokikakudisabled to set
     */
    public void setD90ryuudokikakudisabled(String d90ryuudokikakudisabled) {
        this.d90ryuudokikakudisabled = d90ryuudokikakudisabled;
    }

}
