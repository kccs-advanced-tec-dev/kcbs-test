/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/01/17<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * 変更日       2022/03/09<br>
 * 計画書No     MB2202-D013<br>
 * 変更者       KCSS WXF<br>
 * 変更理由     仕様変更対応<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 印刷(RSUS)のモデルクラスです。
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2019/01/17
 */
public class SrRsusprn {

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
     * KCPNO
     */
    private String kcpno;

    /**
     * ﾃｰﾌﾟ種類
     */
    private String tapesyurui;

    /**
     * ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
     */
    private String tapelotno;

    /**
     * ﾃｰﾌﾟｽﾘｯﾌﾟ記号
     */
    private String tapeSlipKigo;

    /**
     * 原料記号
     */
    private String genryoukigou;

    /**
     * 開始日時
     */
    private Timestamp kaisinichiji;

    /**
     * 終了日時
     */
    private Timestamp syuryonichiji;

    /**
     * 号機
     */
    private String goki;

    /**
     * ｽｷｰｼﾞNo
     */
    private String skeegeno;

    /**
     * ｽｷｰｼﾞ枚数
     */
    private Integer skeegemaisuu;

    /**
     * ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
     */
    private Integer skeegespeed;

    /**
     * 乾燥温度
     */
    private BigDecimal kansoondo;

    /**
     * ｸﾘｱﾗﾝｽ設定値
     */
    private BigDecimal clearance;

    /**
     * 差圧
     */
    private BigDecimal saatu;

    /**
     * 膜厚1
     */
    private BigDecimal makuatu1;

    /**
     * 製版No
     */
    private String seihanno;

    /**
     * 製版枚数
     */
    private Long seihanmaisuu;

    /**
     * 最大処理数
     */
    private Integer saidaisyorisuu;

    /**
     * 累計処理数
     */
    private Integer ruikeisyorisuu;

    /**
     * ﾍﾟｰｽﾄﾛｯﾄNo
     */
    private String pastelotno;

    /**
     * ﾍﾟｰｽﾄ粘度
     */
    private BigDecimal pastenendo;

    /**
     * ﾍﾟｰｽﾄ温度
     */
    private BigDecimal pasteondo;

    /**
     * 印刷ﾛｰﾙNo1
     */
    private String insaturollno;

    /**
     * 印刷ﾛｰﾙNo2
     */
    private String insaturollno2;

    /**
     * 印刷ﾛｰﾙNo3
     */
    private String insaturollno3;

    /**
     * 印刷ﾛｰﾙNo4
     */
    private String insaturollno4;

    /**
     * 印刷ﾛｰﾙNo5
     */
    private String insaturollno5;

    /**
     * 印刷幅始め平均
     */
    private BigDecimal insatuhabasave;

    /**
     * 印刷幅終り平均
     */
    private BigDecimal insatuhabaeave;

    /**
     * MLD
     */
    private Integer mld;

    /**
     * 備考1
     */
    private String biko1;

    /**
     * 備考2
     */
    private String biko2;
    
    /**
     * 登録日時
     */
    private Timestamp torokunichiji;

    /**
     * 更新日時
     */
    private Timestamp kosinnichiji;

    /**
     * 担当者ｺｰﾄﾞ
     */
    private String tantosya;

    /**
     * ﾍﾟｰｽﾄ固形分1
     */
    private BigDecimal pkokeibun1;

    /**
     * ﾍﾟｰｽﾄﾛｯﾄNo2
     */
    private String pastelotno2;

    /**
     * ﾍﾟｰｽﾄ粘度2
     */
    private BigDecimal pastenendo2;

    /**
     * ﾍﾟｰｽﾄ温度2
     */
    private BigDecimal pasteondo2;

    /**
     * ﾍﾟｰｽﾄ固形分2
     */
    private BigDecimal pkokeibun2;

    /**
     * PETﾌｨﾙﾑ種類
     */
    private String petfilmsyurui;

    /**
     * 乾燥温度表示値2
     */
    private BigDecimal kansoondo2;

    /**
     * 乾燥温度表示値3
     */
    private BigDecimal kansoondo3;

    /**
     * 乾燥温度表示値4
     */
    private BigDecimal kansoondo4;

    /**
     * 乾燥温度表示値5
     */
    private BigDecimal kansoondo5;

    /**
     * 製版名
     */
    private String seihanmei;

    /**
     * 印刷ｽﾀｰﾄ膜厚AVE
     */
    private BigDecimal makuatsuAveStart;

    /**
     * 印刷ｽﾀｰﾄ膜厚MAX
     */
    private BigDecimal makuatsuMaxStart;

    /**
     * 印刷ｽﾀｰﾄ膜厚MIN
     */
    private BigDecimal makuatsuMinStart;

    /**
     * 印刷ｽﾀｰﾄ膜厚CV
     */
    private BigDecimal makuatuCvStart;

    /**
     * ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
     */
    private Integer nijimikasureStart;

    /**
     * 終了時ﾆｼﾞﾐ・ｶｽﾚ確認
     */
    private Integer nijimikasureEnd;

    /**
     * 印刷ｴﾝﾄﾞ時担当者
     */
    private String tantoEnd;

    /**
     * 印刷枚数
     */
    private Integer printmaisuu;

    /**
     * 乾燥炉圧
     */
    private Integer kansouroatsu;

    /**
     * 印刷幅
     */
    private Integer printhaba;

    /**
     * ﾃｰﾌﾞﾙｸﾘｱﾗﾝｽ
     */
    private BigDecimal tableClearrance;
    
    /**
     * 客先
     */
    private String tokuisaki;
    
    /**
     * ﾛｯﾄ区分
     */
    private String lotkubuncode;
    
    /**
     * ｵｰﾅｰ
     */
    private String ownercode;
    
    /**
     * 成形長さ
     */
    private Integer seikeinagasa;
    
    /**
     * 原料
     */
    private String genryou;
    
    /**
     * 電極厚み
     */
    private Double eatumi;
    
    /**
     * 電極ﾍﾟｰｽﾄ
     */
    private String epaste;
    
    /**
     * 電極製版仕様
     */
    private String pattern;
    
    /**
     * ｽｸﾚｯﾊﾟｰ速度
     */
    private Integer scraperspeed;
    
    /**
     * 印刷位置確認
     */
    private Integer startitikakunin;
    
    /**
     * 印刷ｽﾀｰﾄ確認者
     */
    private String kakuninsya;
    
    /**
     * revision
     */
    private Long revision;

    /**
     * 削除ﾌﾗｸﾞ
     */
    private Integer deleteflag;

    /**
     * 工場ｺｰﾄﾞ
     *
     * @return the kojyo
     */
    public String getKojyo() {
        return kojyo;
    }

    /**
     * 工場ｺｰﾄﾞ
     *
     * @param kojyo the kojyo to set
     */
    public void setKojyo(String kojyo) {
        this.kojyo = kojyo;
    }

    /**
     * ﾛｯﾄNo
     *
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * ﾛｯﾄNo
     *
     * @param lotno the lotno to set
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * 枝番
     *
     * @return the edaban
     */
    public String getEdaban() {
        return edaban;
    }

    /**
     * 枝番
     *
     * @param edaban the edaban to set
     */
    public void setEdaban(String edaban) {
        this.edaban = edaban;
    }

    /**
     * KCPNO
     *
     * @return the kcpno
     */
    public String getKcpno() {
        return kcpno;
    }

    /**
     * KCPNO
     *
     * @param kcpno the kcpno to set
     */
    public void setKcpno(String kcpno) {
        this.kcpno = kcpno;
    }

    /**
     * ﾃｰﾌﾟ種類
     *
     * @return the tapesyurui
     */
    public String getTapesyurui() {
        return tapesyurui;
    }

    /**
     * ﾃｰﾌﾟ種類
     *
     * @param tapesyurui the tapesyurui to set
     */
    public void setTapesyurui(String tapesyurui) {
        this.tapesyurui = tapesyurui;
    }

    /**
     * ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
     *
     * @return the tapelotno
     */
    public String getTapelotno() {
        return tapelotno;
    }

    /**
     * ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
     *
     * @param tapelotno the tapelotno to set
     */
    public void setTapelotno(String tapelotno) {
        this.tapelotno = tapelotno;
    }

    /**
     * ﾃｰﾌﾟｽﾘｯﾌﾟ記号
     *
     * @return the tapeSlipKigo
     */
    public String getTapeSlipKigo() {
        return tapeSlipKigo;
    }

    /**
     * ﾃｰﾌﾟｽﾘｯﾌﾟ記号
     *
     * @param tapeSlipKigo the tapeSlipKigo to set
     */
    public void setTapeSlipKigo(String tapeSlipKigo) {
        this.tapeSlipKigo = tapeSlipKigo;
    }

    /**
     * 原料記号
     *
     * @return the genryoukigou
     */
    public String getGenryoukigou() {
        return genryoukigou;
    }

    /**
     * 原料記号
     *
     * @param genryoukigou the genryoukigou to set
     */
    public void setGenryoukigou(String genryoukigou) {
        this.genryoukigou = genryoukigou;
    }

    /**
     * 開始日時
     *
     * @return the kaisinichiji
     */
    public Timestamp getKaisinichiji() {
        return kaisinichiji;
    }

    /**
     * 開始日時
     *
     * @param kaisinichiji the kaisinichiji to set
     */
    public void setKaisinichiji(Timestamp kaisinichiji) {
        this.kaisinichiji = kaisinichiji;
    }

    /**
     * 終了日時
     *
     * @return the syuryonichiji
     */
    public Timestamp getSyuryonichiji() {
        return syuryonichiji;
    }

    /**
     * 終了日時
     *
     * @param syuryonichiji the syuryonichiji to set
     */
    public void setSyuryonichiji(Timestamp syuryonichiji) {
        this.syuryonichiji = syuryonichiji;
    }

    /**
     * 号機
     *
     * @return the goki
     */
    public String getGoki() {
        return goki;
    }

    /**
     * 号機
     *
     * @param goki the goki to set
     */
    public void setGoki(String goki) {
        this.goki = goki;
    }

    /**
     * ｽｷｰｼﾞNo
     *
     * @return the skeegeno
     */
    public String getSkeegeno() {
        return skeegeno;
    }

    /**
     * ｽｷｰｼﾞNo
     *
     * @param skeegeno the skeegeno to set
     */
    public void setSkeegeno(String skeegeno) {
        this.skeegeno = skeegeno;
    }

    /**
     * ｽｷｰｼﾞ枚数
     *
     * @return the skeegemaisuu
     */
    public Integer getSkeegemaisuu() {
        return skeegemaisuu;
    }

    /**
     * ｽｷｰｼﾞ枚数
     *
     * @param skeegemaisuu the skeegemaisuu to set
     */
    public void setSkeegemaisuu(Integer skeegemaisuu) {
        this.skeegemaisuu = skeegemaisuu;
    }

    /**
     * ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
     *
     * @return the skeegespeed
     */
    public Integer getSkeegespeed() {
        return skeegespeed;
    }

    /**
     * ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
     *
     * @param skeegespeed the skeegespeed to set
     */
    public void setSkeegespeed(Integer skeegespeed) {
        this.skeegespeed = skeegespeed;
    }

    /**
     * 乾燥温度
     *
     * @return the kansoondo
     */
    public BigDecimal getKansoondo() {
        return kansoondo;
    }

    /**
     * 乾燥温度
     *
     * @param kansoondo the kansoondo to set
     */
    public void setKansoondo(BigDecimal kansoondo) {
        this.kansoondo = kansoondo;
    }

    /**
     * ｸﾘｱﾗﾝｽ設定値
     *
     * @return the clearance
     */
    public BigDecimal getClearance() {
        return clearance;
    }

    /**
     * ｸﾘｱﾗﾝｽ設定値
     *
     * @param clearance the clearance to set
     */
    public void setClearance(BigDecimal clearance) {
        this.clearance = clearance;
    }

    /**
     * 差圧
     *
     * @return the saatu
     */
    public BigDecimal getSaatu() {
        return saatu;
    }

    /**
     * 差圧
     *
     * @param saatu the saatu to set
     */
    public void setSaatu(BigDecimal saatu) {
        this.saatu = saatu;
    }

    /**
     * 膜厚1
     *
     * @return the makuatu1
     */
    public BigDecimal getMakuatu1() {
        return makuatu1;
    }

    /**
     * 膜厚1
     *
     * @param makuatu1 the makuatu1 to set
     */
    public void setMakuatu1(BigDecimal makuatu1) {
        this.makuatu1 = makuatu1;
    }

    /**
     * 製版No
     *
     * @return the seihanno
     */
    public String getSeihanno() {
        return seihanno;
    }

    /**
     * 製版No
     *
     * @param seihanno the seihanno to set
     */
    public void setSeihanno(String seihanno) {
        this.seihanno = seihanno;
    }

    /**
     * 製版枚数
     *
     * @return the seihanmaisuu
     */
    public Long getSeihanmaisuu() {
        return seihanmaisuu;
    }

    /**
     * 製版枚数
     *
     * @param seihanmaisuu the seihanmaisuu to set
     */
    public void setSeihanmaisuu(Long seihanmaisuu) {
        this.seihanmaisuu = seihanmaisuu;
    }

    /**
     * 最大処理数
     *
     * @return the saidaisyorisuu
     */
    public Integer getSaidaisyorisuu() {
        return saidaisyorisuu;
    }

    /**
     * 最大処理数
     *
     * @param saidaisyorisuu the saidaisyorisuu to set
     */
    public void setSaidaisyorisuu(Integer saidaisyorisuu) {
        this.saidaisyorisuu = saidaisyorisuu;
    }

    /**
     * 累計処理数
     *
     * @return the ruikeisyorisuu
     */
    public Integer getRuikeisyorisuu() {
        return ruikeisyorisuu;
    }

    /**
     * 累計処理数
     *
     * @param ruikeisyorisuu the ruikeisyorisuu to set
     */
    public void setRuikeisyorisuu(Integer ruikeisyorisuu) {
        this.ruikeisyorisuu = ruikeisyorisuu;
    }

    /**
     * ﾍﾟｰｽﾄﾛｯﾄNo
     *
     * @return the pastelotno
     */
    public String getPastelotno() {
        return pastelotno;
    }

    /**
     * ﾍﾟｰｽﾄﾛｯﾄNo
     *
     * @param pastelotno the pastelotno to set
     */
    public void setPastelotno(String pastelotno) {
        this.pastelotno = pastelotno;
    }

    /**
     * ﾍﾟｰｽﾄ粘度
     *
     * @return the pastenendo
     */
    public BigDecimal getPastenendo() {
        return pastenendo;
    }

    /**
     * ﾍﾟｰｽﾄ粘度
     *
     * @param pastenendo the pastenendo to set
     */
    public void setPastenendo(BigDecimal pastenendo) {
        this.pastenendo = pastenendo;
    }

    /**
     * ﾍﾟｰｽﾄ温度
     *
     * @return the pasteondo
     */
    public BigDecimal getPasteondo() {
        return pasteondo;
    }

    /**
     * ﾍﾟｰｽﾄ温度
     *
     * @param pasteondo the pasteondo to set
     */
    public void setPasteondo(BigDecimal pasteondo) {
        this.pasteondo = pasteondo;
    }

    /**
     * 印刷ﾛｰﾙNo1
     *
     * @return the insaturollno
     */
    public String getInsaturollno() {
        return insaturollno;
    }

    /**
     * 印刷ﾛｰﾙNo1
     *
     * @param insaturollno the insaturollno to set
     */
    public void setInsaturollno(String insaturollno) {
        this.insaturollno = insaturollno;
    }

    /**
     * 印刷ﾛｰﾙNo2
     *
     * @return the insaturollno2
     */
    public String getInsaturollno2() {
        return insaturollno2;
    }

    /**
     * 印刷ﾛｰﾙNo2
     *
     * @param insaturollno2 the insaturollno2 to set
     */
    public void setInsaturollno2(String insaturollno2) {
        this.insaturollno2 = insaturollno2;
    }

    /**
     * 印刷ﾛｰﾙNo3
     *
     * @return the insaturollno3
     */
    public String getInsaturollno3() {
        return insaturollno3;
    }

    /**
     * 印刷ﾛｰﾙNo3
     *
     * @param insaturollno3 the insaturollno3 to set
     */
    public void setInsaturollno3(String insaturollno3) {
        this.insaturollno3 = insaturollno3;
    }

    /**
     * 印刷ﾛｰﾙNo4
     *
     * @return the insaturollno4
     */
    public String getInsaturollno4() {
        return insaturollno4;
    }

    /**
     * 印刷ﾛｰﾙNo4
     *
     * @param insaturollno4 the insaturollno4 to set
     */
    public void setInsaturollno4(String insaturollno4) {
        this.insaturollno4 = insaturollno4;
    }

    /**
     * 印刷ﾛｰﾙNo5
     *
     * @return the insaturollno5
     */
    public String getInsaturollno5() {
        return insaturollno5;
    }

    /**
     * 印刷ﾛｰﾙNo5
     *
     * @param insaturollno5 the insaturollno5 to set
     */
    public void setInsaturollno5(String insaturollno5) {
        this.insaturollno5 = insaturollno5;
    }

    /**
     * 印刷幅始め平均
     *
     * @return the insatuhabasave
     */
    public BigDecimal getInsatuhabasave() {
        return insatuhabasave;
    }

    /**
     * 印刷幅始め平均
     *
     * @param insatuhabasave the insatuhabasave to set
     */
    public void setInsatuhabasave(BigDecimal insatuhabasave) {
        this.insatuhabasave = insatuhabasave;
    }

    /**
     * 印刷幅終り平均
     *
     * @return the insatuhabaeave
     */
    public BigDecimal getInsatuhabaeave() {
        return insatuhabaeave;
    }

    /**
     * 印刷幅終り平均
     *
     * @param insatuhabaeave the insatuhabaeave to set
     */
    public void setInsatuhabaeave(BigDecimal insatuhabaeave) {
        this.insatuhabaeave = insatuhabaeave;
    }

    /**
     * MLD
     *
     * @return the mld
     */
    public Integer getMld() {
        return mld;
    }

    /**
     * MLD
     *
     * @param mld the mld to set
     */
    public void setMld(Integer mld) {
        this.mld = mld;
    }

    /**
     * 備考1
     *
     * @return the biko1
     */
    public String getBiko1() {
        return biko1;
    }

    /**
     * 備考1
     *
     * @param biko1 the biko1 to set
     */
    public void setBiko1(String biko1) {
        this.biko1 = biko1;
    }

    /**
     * 備考2
     *
     * @return the biko2
     */
    public String getBiko2() {
        return biko2;
    }

    /**
     * 備考2
     *
     * @param biko2 the biko2 to set
     */
    public void setBiko2(String biko2) {
        this.biko2 = biko2;
    }

    /**
     * 担当者ｺｰﾄﾞ
     *
     * @return the tantosya
     */
    public String getTantosya() {
        return tantosya;
    }

    /**
     * 担当者ｺｰﾄﾞ
     *
     * @param tantosya the tantosya to set
     */
    public void setTantosya(String tantosya) {
        this.tantosya = tantosya;
    }

    /**
     * ﾍﾟｰｽﾄ固形分1
     *
     * @return the pkokeibun1
     */
    public BigDecimal getPkokeibun1() {
        return pkokeibun1;
    }

    /**
     * ﾍﾟｰｽﾄ固形分1
     *
     * @param pkokeibun1 the pkokeibun1 to set
     */
    public void setPkokeibun1(BigDecimal pkokeibun1) {
        this.pkokeibun1 = pkokeibun1;
    }

    /**
     * ﾍﾟｰｽﾄﾛｯﾄNo2
     *
     * @return the pastelotno2
     */
    public String getPastelotno2() {
        return pastelotno2;
    }

    /**
     * ﾍﾟｰｽﾄﾛｯﾄNo2
     *
     * @param pastelotno2 the pastelotno2 to set
     */
    public void setPastelotno2(String pastelotno2) {
        this.pastelotno2 = pastelotno2;
    }

    /**
     * ﾍﾟｰｽﾄ粘度2
     *
     * @return the pastenendo2
     */
    public BigDecimal getPastenendo2() {
        return pastenendo2;
    }

    /**
     * ﾍﾟｰｽﾄ粘度2
     *
     * @param pastenendo2 the pastenendo2 to set
     */
    public void setPastenendo2(BigDecimal pastenendo2) {
        this.pastenendo2 = pastenendo2;
    }

    /**
     * ﾍﾟｰｽﾄ温度2
     *
     * @return the pasteondo2
     */
    public BigDecimal getPasteondo2() {
        return pasteondo2;
    }

    /**
     * ﾍﾟｰｽﾄ温度2
     *
     * @param pasteondo2 the pasteondo2 to set
     */
    public void setPasteondo2(BigDecimal pasteondo2) {
        this.pasteondo2 = pasteondo2;
    }

    /**
     * ﾍﾟｰｽﾄ固形分2
     *
     * @return the pkokeibun2
     */
    public BigDecimal getPkokeibun2() {
        return pkokeibun2;
    }

    /**
     * ﾍﾟｰｽﾄ固形分2
     *
     * @param pkokeibun2 the pkokeibun2 to set
     */
    public void setPkokeibun2(BigDecimal pkokeibun2) {
        this.pkokeibun2 = pkokeibun2;
    }

    /**
     * PETﾌｨﾙﾑ種類
     *
     * @return the petfilmsyurui
     */
    public String getPetfilmsyurui() {
        return petfilmsyurui;
    }

    /**
     * PETﾌｨﾙﾑ種類
     *
     * @param petfilmsyurui the petfilmsyurui to set
     */
    public void setPetfilmsyurui(String petfilmsyurui) {
        this.petfilmsyurui = petfilmsyurui;
    }

    /**
     * 乾燥温度表示値2
     *
     * @return the kansoondo2
     */
    public BigDecimal getKansoondo2() {
        return kansoondo2;
    }

    /**
     * 乾燥温度表示値2
     *
     * @param kansoondo2 the kansoondo2 to set
     */
    public void setKansoondo2(BigDecimal kansoondo2) {
        this.kansoondo2 = kansoondo2;
    }

    /**
     * 乾燥温度表示値3
     *
     * @return the kansoondo3
     */
    public BigDecimal getKansoondo3() {
        return kansoondo3;
    }

    /**
     * 乾燥温度表示値3
     *
     * @param kansoondo3 the kansoondo3 to set
     */
    public void setKansoondo3(BigDecimal kansoondo3) {
        this.kansoondo3 = kansoondo3;
    }

    /**
     * 乾燥温度表示値4
     *
     * @return the kansoondo4
     */
    public BigDecimal getKansoondo4() {
        return kansoondo4;
    }

    /**
     * 乾燥温度表示値4
     *
     * @param kansoondo4 the kansoondo4 to set
     */
    public void setKansoondo4(BigDecimal kansoondo4) {
        this.kansoondo4 = kansoondo4;
    }

    /**
     * 乾燥温度表示値5
     *
     * @return the kansoondo5
     */
    public BigDecimal getKansoondo5() {
        return kansoondo5;
    }

    /**
     * 乾燥温度表示値5
     *
     * @param kansoondo5 the kansoondo5 to set
     */
    public void setKansoondo5(BigDecimal kansoondo5) {
        this.kansoondo5 = kansoondo5;
    }

    /**
     * 製版名
     *
     * @return the seihanmei
     */
    public String getSeihanmei() {
        return seihanmei;
    }

    /**
     * 製版名
     *
     * @param seihanmei the seihanmei to set
     */
    public void setSeihanmei(String seihanmei) {
        this.seihanmei = seihanmei;
    }

    /**
     * 印刷ｽﾀｰﾄ膜厚AVE
     *
     * @return the makuatsuAveStart
     */
    public BigDecimal getMakuatsuAveStart() {
        return makuatsuAveStart;
    }

    /**
     * 印刷ｽﾀｰﾄ膜厚AVE
     *
     * @param makuatsuAveStart the makuatsuAveStart to set
     */
    public void setMakuatsuAveStart(BigDecimal makuatsuAveStart) {
        this.makuatsuAveStart = makuatsuAveStart;
    }

    /**
     * 印刷ｽﾀｰﾄ膜厚MAX
     *
     * @return the makuatsuMaxStart
     */
    public BigDecimal getMakuatsuMaxStart() {
        return makuatsuMaxStart;
    }

    /**
     * 印刷ｽﾀｰﾄ膜厚MAX
     *
     * @param makuatsuMaxStart the makuatsuMaxStart to set
     */
    public void setMakuatsuMaxStart(BigDecimal makuatsuMaxStart) {
        this.makuatsuMaxStart = makuatsuMaxStart;
    }

    /**
     * 印刷ｽﾀｰﾄ膜厚MIN
     *
     * @return the makuatsuMinStart
     */
    public BigDecimal getMakuatsuMinStart() {
        return makuatsuMinStart;
    }

    /**
     * 印刷ｽﾀｰﾄ膜厚MIN
     *
     * @param makuatsuMinStart the makuatsuMinStart to set
     */
    public void setMakuatsuMinStart(BigDecimal makuatsuMinStart) {
        this.makuatsuMinStart = makuatsuMinStart;
    }

    /**
     * 印刷ｽﾀｰﾄ膜厚CV
     *
     * @return the makuatuCvStart
     */
    public BigDecimal getMakuatuCvStart() {
        return makuatuCvStart;
    }

    /**
     * 印刷ｽﾀｰﾄ膜厚CV
     *
     * @param makuatuCvStart the makuatuCvStart to set
     */
    public void setMakuatuCvStart(BigDecimal makuatuCvStart) {
        this.makuatuCvStart = makuatuCvStart;
    }

    /**
     * ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
     *
     * @return the nijimikasureStart
     */
    public Integer getNijimikasureStart() {
        return nijimikasureStart;
    }

    /**
     * ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
     *
     * @param nijimikasureStart the nijimikasureStart to set
     */
    public void setNijimikasureStart(Integer nijimikasureStart) {
        this.nijimikasureStart = nijimikasureStart;
    }

    /**
     * 終了時ﾆｼﾞﾐ・ｶｽﾚ確認
     *
     * @return the nijimikasureEnd
     */
    public Integer getNijimikasureEnd() {
        return nijimikasureEnd;
    }

    /**
     * 終了時ﾆｼﾞﾐ・ｶｽﾚ確認
     *
     * @param nijimikasureEnd the nijimikasureEnd to set
     */
    public void setNijimikasureEnd(Integer nijimikasureEnd) {
        this.nijimikasureEnd = nijimikasureEnd;
    }

    /**
     * 印刷ｴﾝﾄﾞ時担当者
     *
     * @return the tantoEnd
     */
    public String getTantoEnd() {
        return tantoEnd;
    }

    /**
     * 印刷ｴﾝﾄﾞ時担当者
     *
     * @param tantoEnd the tantoEnd to set
     */
    public void setTantoEnd(String tantoEnd) {
        this.tantoEnd = tantoEnd;
    }

    /**
     * 印刷枚数
     *
     * @return the printmaisuu
     */
    public Integer getPrintmaisuu() {
        return printmaisuu;
    }

    /**
     * 印刷枚数
     *
     * @param printmaisuu the printmaisuu to set
     */
    public void setPrintmaisuu(Integer printmaisuu) {
        this.printmaisuu = printmaisuu;
    }

    /**
     * 乾燥炉圧
     *
     * @return the kansouroatsu
     */
    public Integer getKansouroatsu() {
        return kansouroatsu;
    }

    /**
     * 乾燥炉圧
     *
     * @param kansouroatsu the kansouroatsu to set
     */
    public void setKansouroatsu(Integer kansouroatsu) {
        this.kansouroatsu = kansouroatsu;
    }

    /**
     * 印刷幅
     *
     * @return the printhaba
     */
    public Integer getPrinthaba() {
        return printhaba;
    }

    /**
     * 印刷幅
     *
     * @param printhaba the printhaba to set
     */
    public void setPrinthaba(Integer printhaba) {
        this.printhaba = printhaba;
    }

    /**
     * ﾃｰﾌﾞﾙｸﾘｱﾗﾝｽ
     *
     * @return the tableClearrance
     */
    public BigDecimal getTableClearrance() {
        return tableClearrance;
    }

    /**
     * ﾃｰﾌﾞﾙｸﾘｱﾗﾝｽ
     *
     * @param tableClearrance the tableClearrance to set
     */
    public void setTableClearrance(BigDecimal tableClearrance) {
        this.tableClearrance = tableClearrance;
    }

    /**
     * 登録日時
     *
     * @return the torokunichiji
     */
    public Timestamp getTorokunichiji() {
        return torokunichiji;
    }

    /**
     * 登録日時
     *
     * @param torokunichiji the torokunichiji to set
     */
    public void setTorokunichiji(Timestamp torokunichiji) {
        this.torokunichiji = torokunichiji;
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
     * revision
     *
     * @return the revision
     */
    public Long getRevision() {
        return revision;
    }

    /**
     * revision
     *
     * @param revision the revision to set
     */
    public void setRevision(Long revision) {
        this.revision = revision;
    }

    /**
     * 削除ﾌﾗｸﾞ
     *
     * @return the deleteflag
     */
    public Integer getDeleteflag() {
        return deleteflag;
    }

    /**
     * 削除ﾌﾗｸﾞ
     *
     * @param deleteflag the deleteflag to set
     */
    public void setDeleteflag(Integer deleteflag) {
        this.deleteflag = deleteflag;
    }

    /**
     * 客先
     *
     * @return the tokuisaki
     */
    public String getTokuisaki() {
        return tokuisaki;
    }

    /**
     * 客先
     *
     * @param tokuisaki the tokuisaki to set
     */
    public void setTokuisaki(String tokuisaki) {
        this.tokuisaki = tokuisaki;
    }

    /**
     * ﾛｯﾄ区分
     *
     * @return the lotkubuncode
     */
    public String getLotkubuncode() {
        return lotkubuncode;
    }

    /**
     * ﾛｯﾄ区分
     *
     * @param lotkubuncode the lotkubuncode to set
     */
    public void setLotkubuncode(String lotkubuncode) {
        this.lotkubuncode = lotkubuncode;
    }

    /**
     * ｵｰﾅｰ
     *
     * @return the ownercode
     */
    public String getOwnercode() {
        return ownercode;
    }

    /**
     * ｵｰﾅｰ
     *
     * @param ownercode the ownercode to set
     */
    public void setOwnercode(String ownercode) {
        this.ownercode = ownercode;
    }

    /**
     * 成形長さ
     *
     * @return the seikeinagasa
     */
    public Integer getSeikeinagasa() {
        return seikeinagasa;
    }

    /**
     * 成形長さ
     *
     * @param seikeinagasa the seikeinagasa to set
     */
    public void setSeikeinagasa(Integer seikeinagasa) {
        this.seikeinagasa = seikeinagasa;
    }

    /**
     * 原料
     *
     * @return the genryou
     */
    public String getGenryou() {
        return genryou;
    }

    /**
     * 原料
     *
     * @param genryou the genryou to set
     */
    public void setGenryou(String genryou) {
        this.genryou = genryou;
    }

    /**
     * 電極厚み
     *
     * @return the eatumi
     */
    public Double getEatumi() {
        return eatumi;
    }

    /**
     * 電極厚み
     *
     * @param eatumi the eatumi to set
     */
    public void setEatumi(Double eatumi) {
        this.eatumi = eatumi;
    }

    /**
     * 電極ﾍﾟｰｽﾄ
     *
     * @return the epaste
     */
    public String getEpaste() {
        return epaste;
    }

    /**
     * 電極ﾍﾟｰｽﾄ
     *
     * @param epaste the epaste to set
     */
    public void setEpaste(String epaste) {
        this.epaste = epaste;
    }

    /**
     * 電極製版仕様
     *
     * @return the pattern
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * 電極製版仕様
     *
     * @param pattern the pattern to set
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * ｽｸﾚｯﾊﾟｰ速度
     *
     * @return the scraperspeed
     */
    public Integer getScraperspeed() {
        return scraperspeed;
    }

    /**
     * ｽｸﾚｯﾊﾟｰ速度
     *
     * @param scraperspeed the scraperspeed to set
     */
    public void setScraperspeed(Integer scraperspeed) {
        this.scraperspeed = scraperspeed;
    }

    /**
     * 印刷位置確認
     *
     * @return the startitikakunin
     */
    public Integer getStartitikakunin() {
        return startitikakunin;
    }

    /**
     * 印刷位置確認
     *
     * @param startitikakunin the startitikakunin to set
     */
    public void setStartitikakunin(Integer startitikakunin) {
        this.startitikakunin = startitikakunin;
    }

    /**
     * 印刷ｽﾀｰﾄ確認者
     *
     * @return the kakuninsya
     */
    public String getKakuninsya() {
        return kakuninsya;
    }

    /**
     * 印刷ｽﾀｰﾄ確認者
     *
     * @param kakuninsya the kakuninsya to set
     */
    public void setKakuninsya(String kakuninsya) {
        this.kakuninsya = kakuninsya;
    }    

}
