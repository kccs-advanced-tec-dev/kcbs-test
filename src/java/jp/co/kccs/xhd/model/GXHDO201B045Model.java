/*
 * Copyright 2020 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2020/02/12<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 印刷積層・HAPS処理履歴検索画面のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2020/02/12
 */
public class GXHDO201B045Model implements Serializable{

    /** ﾛｯﾄNo. */
   private String lotno = "";

    /** KCPNO */
   private String kcpno = "";

    /** ﾃｰﾌﾟ種類 */
   private String tapesyurui = "";

    /** ﾃｰﾌﾟﾛｯﾄNo */
   private String tapelotno = "";

    /** 原料記号 */
   private String genryokigo = "";

    /** 設備温度 */
   private Integer setubiondo = null;

    /** 設備湿度 */
   private Integer setubisitudo = null;

    /** 電極ﾍﾟｰｽﾄﾛｯﾄNo */
   private String pastelotno = "";

    /** 電極ﾍﾟｰｽﾄ粘度 */
   private Integer pastenendo = null;

    /** ﾍﾟｰｽﾄ温度 */
   private Integer pasteondo = null;

    /** 電極製版No */
   private String seihanno = "";

    /** 電極製版枚数 */
   private Integer seihanmaisuu = null;

    /** 電極ｽｷｰｼﾞNo */
   private String skeegeno = "";

    /** 電極ｽｷｰｼﾞ枚数 */
   private Integer skeegemaisuu = null;

    /** 電極ｸﾘｱﾗﾝｽ */
   private BigDecimal clearance = null;

    /** 乾燥温度 */
   private BigDecimal kansoondo = null;

    /** 乾燥速度 */
   private Integer kansosokudo = null;

    /** 仮ﾌﾟﾚｽ高圧 */
   private Integer karipresskou = null;

    /** 仮ﾌﾟﾚｽ低圧 */
   private Integer karipresstei = null;

    /** 仮ﾌﾟﾚｽ脱気時間 */
   private BigDecimal karipressdakki = null;

    /** 電極差圧 */
   private BigDecimal saatu = null;

    /** 電極ｽｷｰｼﾞｽﾋﾟｰﾄﾞ */
   private Integer skeegespeed = null;

    /** ｽｷｰｼﾞ角度 */
   private Integer skeegekakudo = null;

    /** MLD */
   private String mld = "";

    /** 積層後厚み */
   private Integer sekisoatumi = null;

    /** 印刷幅1 */
   private Integer insatuhaba1 = null;

    /** 印刷幅2 */
   private Integer insatuhaba2 = null;

    /** 印刷幅3 */
   private Integer insatuhaba3 = null;

    /** 印刷幅4 */
   private Integer insatuhaba4 = null;

    /** 印刷幅5 */
   private Integer insatuhaba5 = null;

    /** 号機 */
   private String goki = "";

    /** 印刷積層開始担当者 */
   private String tantosya = "";

    /** 印刷積層開始確認者 */
   private String kakuninsya = "";

    /** 印刷積層開始日時 */
   private Timestamp kaisinichiji = null;

    /** 印刷積層終了日時 */
   private Timestamp syuryonichiji = null;

    /** 外観初め */
   private String gaikanf = "";

    /** 外観中間 */
   private String gaikanm = "";

    /** 外観終わり */
   private String gaikane = "";

    /** ｾｯﾄ数 */
   private Integer setsuu = null;

    /** 積層量 */
   private String sekisouryo = "";

    /** 電極ﾍﾟｰｽﾄ */
   private String epaste = "";

    /** 電極製版名 */
   private String eseihanmei = "";

    /** 積層ｽﾗｲﾄﾞ量 */
   private String sekisouslideryo = "";

    /** 加圧時間 */
   private BigDecimal kaatujikan = null;

    /** 剥離速度 */
   private BigDecimal hakurispeed = null;

    /** 上乾燥温度 */
   private BigDecimal uwakansoondo = null;

    /** 下乾燥温度 */
   private BigDecimal shitakansoondo = null;

    /** 電極L/Dｽﾀｰﾄ時 */
   private BigDecimal eldstart = null;

    /** 印刷積層終了担当者 */
   private String endtantou = "";

    /** 処理ｾｯﾄ数 */
   private Integer syorisetsuu = null;

    /** 良品ｾｯﾄ数 */
   private Integer ryouhinsetsuu = null;

    /** 備考1 */
   private String bikou1 = "";

    /** 備考2 */
   private String bikou2 = "";

    public String getLotno() {
        return lotno;
    }

    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    public String getKcpno() {
        return kcpno;
    }

    public void setKcpno(String kcpno) {
        this.kcpno = kcpno;
    }

    public String getTapesyurui() {
        return tapesyurui;
    }

    public void setTapesyurui(String tapesyurui) {
        this.tapesyurui = tapesyurui;
    }

    public String getTapelotno() {
        return tapelotno;
    }

    public void setTapelotno(String tapelotno) {
        this.tapelotno = tapelotno;
    }

    public String getGenryokigo() {
        return genryokigo;
    }

    public void setGenryokigo(String genryokigo) {
        this.genryokigo = genryokigo;
    }

    public Integer getSetubiondo() {
        return setubiondo;
    }

    public void setSetubiondo(Integer setubiondo) {
        this.setubiondo = setubiondo;
    }

    public Integer getSetubisitudo() {
        return setubisitudo;
    }

    public void setSetubisitudo(Integer setubisitudo) {
        this.setubisitudo = setubisitudo;
    }

    public String getPastelotno() {
        return pastelotno;
    }

    public void setPastelotno(String pastelotno) {
        this.pastelotno = pastelotno;
    }

    public Integer getPastenendo() {
        return pastenendo;
    }

    public void setPastenendo(Integer pastenendo) {
        this.pastenendo = pastenendo;
    }

    public Integer getPasteondo() {
        return pasteondo;
    }

    public void setPasteondo(Integer pasteondo) {
        this.pasteondo = pasteondo;
    }

    public String getSeihanno() {
        return seihanno;
    }

    public void setSeihanno(String seihanno) {
        this.seihanno = seihanno;
    }

    public Integer getSeihanmaisuu() {
        return seihanmaisuu;
    }

    public void setSeihanmaisuu(Integer seihanmaisuu) {
        this.seihanmaisuu = seihanmaisuu;
    }

    public String getSkeegeno() {
        return skeegeno;
    }

    public void setSkeegeno(String skeegeno) {
        this.skeegeno = skeegeno;
    }

    public Integer getSkeegemaisuu() {
        return skeegemaisuu;
    }

    public void setSkeegemaisuu(Integer skeegemaisuu) {
        this.skeegemaisuu = skeegemaisuu;
    }

    public BigDecimal getClearance() {
        return clearance;
    }

    public void setClearance(BigDecimal clearance) {
        this.clearance = clearance;
    }

    public BigDecimal getKansoondo() {
        return kansoondo;
    }

    public void setKansoondo(BigDecimal kansoondo) {
        this.kansoondo = kansoondo;
    }

    public Integer getKansosokudo() {
        return kansosokudo;
    }

    public void setKansosokudo(Integer kansosokudo) {
        this.kansosokudo = kansosokudo;
    }

    public Integer getKaripresskou() {
        return karipresskou;
    }

    public void setKaripresskou(Integer karipresskou) {
        this.karipresskou = karipresskou;
    }

    public Integer getKaripresstei() {
        return karipresstei;
    }

    public void setKaripresstei(Integer karipresstei) {
        this.karipresstei = karipresstei;
    }

    public BigDecimal getKaripressdakki() {
        return karipressdakki;
    }

    public void setKaripressdakki(BigDecimal karipressdakki) {
        this.karipressdakki = karipressdakki;
    }

    public BigDecimal getSaatu() {
        return saatu;
    }

    public void setSaatu(BigDecimal saatu) {
        this.saatu = saatu;
    }

    public Integer getSkeegespeed() {
        return skeegespeed;
    }

    public void setSkeegespeed(Integer skeegespeed) {
        this.skeegespeed = skeegespeed;
    }

    public Integer getSkeegekakudo() {
        return skeegekakudo;
    }

    public void setSkeegekakudo(Integer skeegekakudo) {
        this.skeegekakudo = skeegekakudo;
    }

    public String getMld() {
        return mld;
    }

    public void setMld(String mld) {
        this.mld = mld;
    }

    public Integer getSekisoatumi() {
        return sekisoatumi;
    }

    public void setSekisoatumi(Integer sekisoatumi) {
        this.sekisoatumi = sekisoatumi;
    }

    public Integer getInsatuhaba1() {
        return insatuhaba1;
    }

    public void setInsatuhaba1(Integer insatuhaba1) {
        this.insatuhaba1 = insatuhaba1;
    }

    public Integer getInsatuhaba2() {
        return insatuhaba2;
    }

    public void setInsatuhaba2(Integer insatuhaba2) {
        this.insatuhaba2 = insatuhaba2;
    }

    public Integer getInsatuhaba3() {
        return insatuhaba3;
    }

    public void setInsatuhaba3(Integer insatuhaba3) {
        this.insatuhaba3 = insatuhaba3;
    }

    public Integer getInsatuhaba4() {
        return insatuhaba4;
    }

    public void setInsatuhaba4(Integer insatuhaba4) {
        this.insatuhaba4 = insatuhaba4;
    }

    public Integer getInsatuhaba5() {
        return insatuhaba5;
    }

    public void setInsatuhaba5(Integer insatuhaba5) {
        this.insatuhaba5 = insatuhaba5;
    }

    public String getGoki() {
        return goki;
    }

    public void setGoki(String goki) {
        this.goki = goki;
    }

    public String getTantosya() {
        return tantosya;
    }

    public void setTantosya(String tantosya) {
        this.tantosya = tantosya;
    }

    public String getKakuninsya() {
        return kakuninsya;
    }

    public void setKakuninsya(String kakuninsya) {
        this.kakuninsya = kakuninsya;
    }

    public Timestamp getKaisinichiji() {
        return kaisinichiji;
    }

    public void setKaisinichiji(Timestamp kaisinichiji) {
        this.kaisinichiji = kaisinichiji;
    }

    public Timestamp getSyuryonichiji() {
        return syuryonichiji;
    }

    public void setSyuryonichiji(Timestamp syuryonichiji) {
        this.syuryonichiji = syuryonichiji;
    }

    public String getGaikanf() {
        return gaikanf;
    }

    public void setGaikanf(String gaikanf) {
        this.gaikanf = gaikanf;
    }

    public String getGaikanm() {
        return gaikanm;
    }

    public void setGaikanm(String gaikanm) {
        this.gaikanm = gaikanm;
    }

    public String getGaikane() {
        return gaikane;
    }

    public void setGaikane(String gaikane) {
        this.gaikane = gaikane;
    }

    public Integer getSetsuu() {
        return setsuu;
    }

    public void setSetsuu(Integer setsuu) {
        this.setsuu = setsuu;
    }

    public String getSekisouryo() {
        return sekisouryo;
    }

    public void setSekisouryo(String sekisouryo) {
        this.sekisouryo = sekisouryo;
    }
    
    public String getEpaste() {
        return epaste;
    }

    public void setEpaste(String epaste) {
        this.epaste = epaste;
    }

    public String getEseihanmei() {
        return eseihanmei;
    }

    public void setEseihanmei(String eseihanmei) {
        this.eseihanmei = eseihanmei;
    }

    public String getSekisouslideryo() {
        return sekisouslideryo;
    }

    public void setSekisouslideryo(String sekisouslideryo) {
        this.sekisouslideryo = sekisouslideryo;
    }

    public BigDecimal getKaatujikan() {
        return kaatujikan;
    }

    public void setKaatujikan(BigDecimal kaatujikan) {
        this.kaatujikan = kaatujikan;
    }

    public BigDecimal getHakurispeed() {
        return hakurispeed;
    }

    public void setHakurispeed(BigDecimal hakurispeed) {
        this.hakurispeed = hakurispeed;
    }

    public BigDecimal getUwakansoondo() {
        return uwakansoondo;
    }

    public void setUwakansoondo(BigDecimal uwakansoondo) {
        this.uwakansoondo = uwakansoondo;
    }

    public BigDecimal getShitakansoondo() {
        return shitakansoondo;
    }

    public void setShitakansoondo(BigDecimal shitakansoondo) {
        this.shitakansoondo = shitakansoondo;
    }

    public BigDecimal getEldstart() {
        return eldstart;
    }

    public void setEldstart(BigDecimal eldstart) {
        this.eldstart = eldstart;
    }

    public String getEndtantou() {
        return endtantou;
    }

    public void setEndtantou(String endtantou) {
        this.endtantou = endtantou;
    }

    public Integer getSyorisetsuu() {
        return syorisetsuu;
    }

    public void setSyorisetsuu(Integer syorisetsuu) {
        this.syorisetsuu = syorisetsuu;
    }

    public Integer getRyouhinsetsuu() {
        return ryouhinsetsuu;
    }

    public void setRyouhinsetsuu(Integer ryouhinsetsuu) {
        this.ryouhinsetsuu = ryouhinsetsuu;
    }

    public String getBikou1() {
        return bikou1;
    }

    public void setBikou1(String bikou1) {
        this.bikou1 = bikou1;
    }

    public String getBikou2() {
        return bikou2;
    }

    public void setBikou2(String bikou2) {
        this.bikou2 = bikou2;
    }

}