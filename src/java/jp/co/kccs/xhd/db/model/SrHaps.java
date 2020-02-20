/*
 * Copyright 2020 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2020/02/11<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * sr_haps:印刷積層HAPSのモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2020/02/11
 */
public class SrHaps {
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
     * ﾃｰﾌﾟﾛｯﾄNo
     */
    private String tapelotno;

    /**
     * 原料記号
     */
    private String genryokigo;

    /**
     * 設備温度
     */
    private Integer setubiondo;

    /**
     * 設備湿度
     */
    private Integer setubisitudo;

    /**
     * 電極ﾍﾟｰｽﾄﾛｯﾄNo
     */
    private String pastelotno;

    /**
     * 電極ﾍﾟｰｽﾄ粘度
     */
    private Integer pastenendo;

    /**
     * ﾍﾟｰｽﾄ温度
     */
    private Integer pasteondo;

    /**
     * 電極製版No
     */
    private String seihanno;

    /**
     * 電極製版枚数
     */
    private Integer seihanmaisuu;

    /**
     * 電極ｽｷｰｼﾞNo
     */
    private String skeegeno;

    /**
     * 電極ｽｷｰｼﾞ枚数
     */
    private Integer skeegemaisuu;

    /**
     * 電極ｸﾘｱﾗﾝｽ
     */
    private BigDecimal clearance;

    /**
     * 乾燥温度
     */
    private BigDecimal kansoondo;

    /**
     * 乾燥速度
     */
    private Integer kansosokudo;

    /**
     * 仮ﾌﾟﾚｽ高圧
     */
    private Integer karipresskou;

    /**
     * 仮ﾌﾟﾚｽ低圧
     */
    private Integer karipresstei;

    /**
     * 仮ﾌﾟﾚｽ脱気時間
     */
    private BigDecimal karipressdakki;

    /**
     * 電極差圧
     */
    private BigDecimal saatu;

    /**
     * 電極ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
     */
    private Integer skeegespeed;

    /**
     * ｽｷｰｼﾞ角度
     */
    private Integer skeegekakudo;

    /**
     * MLD
     */
    private String mld;

    /**
     * 積層後厚み
     */
    private Integer sekisoatumi;

    /**
     * 印刷幅1
     */
    private Integer insatuhaba1;

    /**
     * 印刷幅2
     */
    private Integer insatuhaba2;

    /**
     * 印刷幅3
     */
    private Integer insatuhaba3;

    /**
     * 印刷幅4
     */
    private Integer insatuhaba4;

    /**
     * 印刷幅5
     */
    private Integer insatuhaba5;

    /**
     * 号機
     */
    private String goki;

    /**
     * 印刷積層開始担当者
     */
    private String tantosya;

    /**
     * 印刷積層開始確認者
     */
    private String kakuninsya;

    /**
     * 印刷積層開始日時
     */
    private Timestamp kaisinichiji;

    /**
     * 印刷積層終了日時
     */
    private Timestamp syuryonichiji;

    /**
     * 外観初め
     */
    private String gaikanf;

    /**
     * 外観中間
     */
    private String gaikanm;

    /**
     * 外観終わり
     */
    private String gaikane;

    /**
     * 登録日時
     */
    private Timestamp torokunichiji;

    /**
     * 更新日時
     */
    private Timestamp kosinnichiji;
    
    /**
     * ｾｯﾄ数
     */
    private Integer setsuu;
    
    /**
     * 積層量
     */
    private String sekisouryo;

    /**
     * 電極ﾍﾟｰｽﾄ
     */
    private String epaste;

    /**
     * 電極製版名
     */
    private String eseihanmei;

    /**
     * 積層ｽﾗｲﾄﾞ量
     */
    private String sekisouslideryo;

    /**
     * 加圧時間
     */
    private BigDecimal kaatujikan;

    /**
     * 剥離速度
     */
    private BigDecimal hakurispeed;

    /**
     * 上乾燥温度
     */
    private BigDecimal uwakansoondo;

    /**
     * 下乾燥温度
     */
    private BigDecimal shitakansoondo;

    /**
     * 電極L/Dｽﾀｰﾄ時
     */
    private BigDecimal eldstart;

    /**
     * 印刷積層終了担当者
     */
    private String endtantou;

    /**
     * 処理ｾｯﾄ数
     */
    private Integer syorisetsuu;

    /**
     * 良品ｾｯﾄ数
     */
    private Integer ryouhinsetsuu;

    /**
     * 備考1
     */
    private String bikou1;

    /**
     * 備考2
     */
    private String bikou2;

    /**
     * revision
     */
    private Integer revision;

    /**
     * 削除ﾌﾗｸﾞ
     */
    private Integer deleteflag;

    public String getKojyo() {
        return kojyo;
    }

    public void setKojyo(String kojyo) {
        this.kojyo = kojyo;
    }

    public String getLotno() {
        return lotno;
    }

    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    public String getEdaban() {
        return edaban;
    }

    public void setEdaban(String edaban) {
        this.edaban = edaban;
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

    public Timestamp getTorokunichiji() {
        return torokunichiji;
    }

    public void setTorokunichiji(Timestamp torokunichiji) {
        this.torokunichiji = torokunichiji;
    }

    public Timestamp getKosinnichiji() {
        return kosinnichiji;
    }

    public void setKosinnichiji(Timestamp kosinnichiji) {
        this.kosinnichiji = kosinnichiji;
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

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public Integer getDeleteflag() {
        return deleteflag;
    }

    public void setDeleteflag(Integer deleteflag) {
        this.deleteflag = deleteflag;
    }

}