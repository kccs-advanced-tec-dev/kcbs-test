/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
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
 * 変更日	2019/08/01<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	863 F.Zhang<br>
 * 変更理由	新規作成<br>
 * <br>
 * <br>
 * 変更日	2020/09/17<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	863 sujialiang<br>
 * 変更理由	項目追加・変更<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 研磨・ﾊﾞﾚﾙ履歴検索画面のモデルクラスです。
 *
 * @author 863 F.Zhang
 * @since 2019/08/01
 */
public class GXHDO201B020Model implements Serializable{
    
    /** ﾛｯﾄNo. */
    private String lotno = "";
    /** KCPNO */
    private String kcpno = "";
    /** 実績No */
    private Integer jissekion = null;
    /** 開始日時 */
    private Timestamp kaisinichiji = null;
    /** 終了日時 */
    private Timestamp syuryonichiji = null;
    /** ﾊﾞﾚﾙ条件設定ﾓｰﾄﾞ */
    private String bjyokensetteimode = "";
     /** 研磨機回転数(rpm) */
    private BigDecimal kenmakikaitensuu = null;
    /** 研磨号機 */
    private String kenmagoki = "";
    /** 研磨時間 */
    private String kenmajikan = "";
    /** 研磨時間単位 */
    private String kenmajikantani = "";
    /** ﾎﾟｯﾄ数(ﾎﾟｯﾄ) */
    private Integer pottosuu = null;
    /** ﾁｯﾌﾟ破片確認 */
    private String chepuhaenkakunin ="";
    /** ﾎﾟｯﾄ内確認 */
    private String pottonaikakunin ="";
    /** 開始担当者 */
    private String kaisitantosya ="";
    /** ﾎﾟｯﾄ取り出し担当者 */
    private String pottotoridasitantosya ="";
    /** ﾊﾞﾚﾙﾎﾟｯﾄNO1 */
    private String barerupottono1 ="";
    /** 乾燥開始日時 */
    private Timestamp kansoukaisinichiji = null;
    /** 乾燥終了日時 */
    private Timestamp kansousyuryonichiji = null;
    /** 乾燥担当者 */
    private String kansoutantosya ="";
    /** ﾒﾃﾞｨｱ選別 */
    private String mediasenbetu =""; 
    /** ﾊﾞﾚﾙﾎﾟｯﾄNO2 */
    private String barerupottono2 ="";    
    /** 計数日時 */
    private Timestamp keisunichiji = null;
    /** 受入個数 */
    private Integer ukeirekosu = null;
    /** 単位重量 */
    private BigDecimal tanijyuryo = null;
    /** 良品個数 */
    private Integer ryohinkosu = null;
    /** 不良数 */
    private Integer furyokosu = null;
    /** 歩留まり */
    private BigDecimal budomari = null;    
    /** 計数担当者 */
    private String keisuutantosya = "";
    /** 備考1 */
    private String biko1 = "";
    /** 備考2 */
    private String biko2 = "";
    /** 研磨方式 */
    private String kenmahosiki = "";
    /** 研磨材量(cc) */
    private Integer kenmazairyo = null;
    /** 研磨材種類 */
    private String kenmazaisyurui = "";
    /** 玉石種類 */
    private String tamaisisyurui = "";
    /** 玉石量(cc) */
    private Integer tamaisiryo = null;
    /** 外観確認 */
    private String gaikankakunin = "";
    /** 開始確認者 */
    private String kaisikakusya = "";
    /** 終了担当者 */
    private String syuryotantosya = "";
    
    /**
     * ﾛｯﾄNo.
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * ﾛｯﾄNo.
     * @param lotno the lotno to set
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * KCPNO
     * @return the kcpno
     */
    public String getKcpno() {
        return kcpno;
    }

    /**
     * KCPNO
     * @param kcpno the kcpno to set
     */
    public void setKcpno(String kcpno) {
        this.kcpno = kcpno;
    }

    /**
     * 実績No
     * @return the jissekion
     */
    public Integer getJissekion() {
        return jissekion;
    }

    /**
     * 実績No
     * @param jissekion the jissekion to set
     */
    public void setJissekion(Integer jissekion) {
        this.jissekion = jissekion;
    }

    /**
     * 開始日時
     * @return the kaisinichiji
     */
    public Timestamp getKaisinichiji() {
        return kaisinichiji;
    }

    /**
     * 開始日時
     * @param kaisinichiji the kaisinichiji to set
     */
    public void setKaisinichiji(Timestamp kaisinichiji) {
        this.kaisinichiji = kaisinichiji;
    }

    /**
     * 終了日時
     * @return the syuryonichiji
     */
    public Timestamp getSyuryonichiji() {
        return syuryonichiji;
    }

    /**
     * 終了日時
     * @param syuryonichiji the syuryonichiji to set
     */
    public void setSyuryonichiji(Timestamp syuryonichiji) {
        this.syuryonichiji = syuryonichiji;
    }
    /**
     * バレル条件設定モード
     * @return the bjyokensetteimode
     */
    public String getBjyokensetteimode() {
        return bjyokensetteimode;
    }

    /**
     * バレル条件設定モード
     * @param bjyokensetteimode the bjyokensetteimode to set
     */
    public void setBjyokensetteimode(String bjyokensetteimode) {
        this.bjyokensetteimode = bjyokensetteimode;
    }
    /**
     * 研磨機回転数
     * @return the kenmakikaitensuu
     */
    public BigDecimal getKenmakikaitensuu() {
        return kenmakikaitensuu;
    }

    /**
     * 研磨機回転数
     * @param kenmakikaitensuu the kenmakikaitensuu to set
     */
    public void setKenmakikaitensuu(BigDecimal kenmakikaitensuu) {
        this.kenmakikaitensuu = kenmakikaitensuu;
    }
    /**
     * 研磨号機
     * @return the kenmagoki
     */
    public String getKenmagoki() {
        return kenmagoki;
    }

    /**
     * 研磨号機
     * @param kenmagoki the kenmagoki to set
     */
    public void setKenmagoki(String kenmagoki) {
        this.kenmagoki = kenmagoki;
    }

    /**
     * 研磨時間
     * @return the kenmajikan
     */
    public String getKenmajikan() {
        return kenmajikan;
    }

    /**
     * 研磨時間
     * @param kenmajikan the kenmajikan to set
     */
    public void setKenmajikan(String kenmajikan) {
        this.kenmajikan = kenmajikan;
    }

    /**
     * 研磨時間単位
     * @return kenmajikantani
     */
    public String getKenmajikantani() {
        return kenmajikantani;
    }

    /**
     * 研磨時間単位
     * @param kenmajikantani 
     */
    public void setKenmajikantani(String kenmajikantani) {
        this.kenmajikantani = kenmajikantani;
    }

    /**
     * ﾎﾟｯﾄ数(ﾎﾟｯﾄ)
     * @return the pottosuu
     */
    public Integer getPottosuu() {
        return pottosuu;
    }

    /**
     * ﾎﾟｯﾄ数(ﾎﾟｯﾄ)
     * @param pottosuu the pottosuu to set
     */
    public void setPottosuu(Integer pottosuu) {
        this.pottosuu = pottosuu;
    }

    /**
     * 備考1
     * @return the biko1
     */
    public String getBiko1() {
        return biko1;
    }

    /**
     * 備考1
     * @param biko1 the biko1 to set
     */
    public void setBiko1(String biko1) {
        this.biko1 = biko1;
    }

    /**
     * 備考2
     * @return the biko2
     */
    public String getBiko2() {
        return biko2;
    }

    /**
     * 備考2
     * @param biko2 the biko2 to set
     */
    public void setBiko2(String biko2) {
        this.biko2 = biko2;
    }

    /**
     * 計数担当者
     * @return the keisuutantosya
     */
    public String getKeisuutantosya() {
        return keisuutantosya;
    }

    /**
     * 計数担当者
     * @param keisuutantosya the keisuutantosya to set
     */
    public void setKeisuutantosya(String keisuutantosya) {
        this.keisuutantosya = keisuutantosya;
    }
    
    /**
     * ﾁｯﾌﾟ破片確認
     * @return the chepuhaenkakunin
     */
    public String getChepuhaenkakunin() {
        return chepuhaenkakunin;
    }
    
    /**
     * ﾁｯﾌﾟ破片確認
     * @param chepuhaenkakunin the chepuhaenkakunin to set
     */
    public void setChepuhaenkakunin(String chepuhaenkakunin) {
        this.chepuhaenkakunin = chepuhaenkakunin;
    }

    /**
     * ﾎﾟｯﾄ内確認
     * @return the pottonaikakunin
     */
    public String getPottonaikakunin() {
        return pottonaikakunin;
    }

    /**
     * ﾎﾟｯﾄ内確認
     * @param pottonaikakunin the pottonaikakunin to set
     */
    public void setPottonaikakunin(String pottonaikakunin) {
        this.pottonaikakunin = pottonaikakunin;
    }

    /**
     * 開始担当者
     * @return the kaisitantosya
     */
    public String getKaisitantosya() {
        return kaisitantosya;
    }

    /**
     * 開始担当者
     * @param kaisitantosya the kaisitantosya to set
     */
    public void setKaisitantosya(String kaisitantosya) {
        this.kaisitantosya = kaisitantosya;
    }

    /**
     * ﾎﾟｯﾄ取り出し担当者
     * @return the pottotoridasitantosya
     */
    public String getPottotoridasitantosya() {
        return pottotoridasitantosya;
    }

    /**
     * ﾎﾟｯﾄ取り出し担当者
     * @param pottotoridasitantosya the pottotoridasitantosya to set
     */
    public void setPottotoridasitantosya(String pottotoridasitantosya) {
        this.pottotoridasitantosya = pottotoridasitantosya;
    }

    /**
     * ﾊﾞﾚﾙﾎﾟｯﾄNO1
     * @return the barerupottono1
     */
    public String getBarerupottono1() {
        return barerupottono1;
    }

    /**
     * ﾊﾞﾚﾙﾎﾟｯﾄNO1
     * @param barerupottono1 the barerupottono1 to set
     */
    public void setBarerupottono1(String barerupottono1) {
        this.barerupottono1 = barerupottono1;
    }

    /**
     * 乾燥開始日時
     * @return the kansoukaisinichiji
     */
    public Timestamp getKansoukaisinichiji() {
        return kansoukaisinichiji;
    }

    /**
     * 乾燥開始日時
     * @param kansoukaisinichiji the kansoukaisinichiji to set
     */
    public void setKansoukaisinichiji(Timestamp kansoukaisinichiji) {
        this.kansoukaisinichiji = kansoukaisinichiji;
    }

    /**
     * 乾燥終了日時
     * @return the kansousyuryonichiji
     */
    public Timestamp getKansousyuryonichiji() {
        return kansousyuryonichiji;
    }

    /**
     * 乾燥終了日時
     * @param kansousyuryonichiji the kansousyuryonichiji to set
     */
    public void setKansousyuryonichiji(Timestamp kansousyuryonichiji) {
        this.kansousyuryonichiji = kansousyuryonichiji;
    }

    /**
     * 乾燥担当者
     * @return the kansoutantosya
     */
    public String getKansoutantosya() {
        return kansoutantosya;
    }

    /**
     * 乾燥担当者
     * @param kansoutantosya the kansoutantosya to set
     */
    public void setKansoutantosya(String kansoutantosya) {
        this.kansoutantosya = kansoutantosya;
    }

    /**
     * ﾒﾃﾞｨｱ選別
     * @return the mediasenbetu
     */
    public String getMediasenbetu() {
        return mediasenbetu;
    }

    /**
     * ﾒﾃﾞｨｱ選別
     * @param mediasenbetu the mediasenbetu to set
     */
    public void setMediasenbetu(String mediasenbetu) {
        this.mediasenbetu = mediasenbetu;
    }

    /**
     * ﾊﾞﾚﾙﾎﾟｯﾄNO2
     * @return the barerupottono2
     */
    public String getBarerupottono2() {
        return barerupottono2;
    }

    /**
     * ﾊﾞﾚﾙﾎﾟｯﾄNO2
     * @param barerupottono2 the barerupottono2 to set
     */
    public void setBarerupottono2(String barerupottono2) {
        this.barerupottono2 = barerupottono2;
    }

    /**
     * 計数日時
     * @return the keisunichiji
     */
    public Timestamp getKeisunichiji() {
        return keisunichiji;
    }

    /**
     * 計数日時
     * @param keisunichiji the keisunichiji to set
     */
    public void setKeisunichiji(Timestamp keisunichiji) {
        this.keisunichiji = keisunichiji;
    }

    /**
     * 受入個数
     * @return the ukeirekosu
     */
    public Integer getUkeirekosu() {
        return ukeirekosu;
    }

    /**
     * 受入個数
     * @param ukeirekosu the ukeirekosu to set
     */
    public void setUkeirekosu(Integer ukeirekosu) {
        this.ukeirekosu = ukeirekosu;
    }

    /**
     * 単位重量
     * @return the tanijyuryo
     */
    public BigDecimal getTanijyuryo() {
        return tanijyuryo;
    }

    /**
     * 単位重量
     * @param tanijyuryo the tanijyuryo to set
     */
    public void setTanijyuryo(BigDecimal tanijyuryo) {
        this.tanijyuryo = tanijyuryo;
    }

    /**
     * 良品個数
     * @return the ryohinkosu
     */
    public Integer getRyohinkosu() {
        return ryohinkosu;
    }

    /**
     * 良品個数
     * @param ryohinkosu the ryohinkosu to set
     */
    public void setRyohinkosu(Integer ryohinkosu) {
        this.ryohinkosu = ryohinkosu;
    }

    /**
     * 不良数
     * @return the furyokosu
     */
    public Integer getFuryokosu() {
        return furyokosu;
    }

    /**
     * 不良数
     * @param furyokosu the furyokosu to set
     */
    public void setFuryokosu(Integer furyokosu) {
        this.furyokosu = furyokosu;
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
     * 研磨方式
     * @return the kenmahosiki
     */
    public String getKenmahosiki() {
        return kenmahosiki;
    }

    /**
     * 研磨方式
     * @param kenmahosiki the kenmahosiki to set
     */
    public void setKenmahosiki(String kenmahosiki) {
        this.kenmahosiki = kenmahosiki;
    }

    /**
     * 研磨材量(cc)
     * @return the kenmazairyo
     */
    public Integer getKenmazairyo() {
        return kenmazairyo;
    }

    /**
     * 研磨材量(cc)
     * @param kenmazairyo the kenmazairyo to set
     */
    public void setKenmazairyo(Integer kenmazairyo) {
        this.kenmazairyo = kenmazairyo;
    }

    /**
     * 研磨材種類
     * @return the kenmazaisyurui
     */
    public String getKenmazaisyurui() {
        return kenmazaisyurui;
    }

    /**
     * 研磨材種類
     * @param kenmazaisyurui the kenmazaisyurui to set
     */
    public void setKenmazaisyurui(String kenmazaisyurui) {
        this.kenmazaisyurui = kenmazaisyurui;
    }

    /**
     * 玉石種類
     * @return the tamaisisyurui
     */
    public String getTamaisisyurui() {
        return tamaisisyurui;
    }

    /**
     * 玉石種類
     * @param tamaisisyurui the tamaisisyurui to set
     */
    public void setTamaisisyurui(String tamaisisyurui) {
        this.tamaisisyurui = tamaisisyurui;
    }

    /**
     * 玉石量(cc)
     * @return the tamaisiryo
     */
    public Integer getTamaisiryo() {
        return tamaisiryo;
    }

    /**
     * 玉石量(cc)
     * @param tamaisiryo the tamaisiryo to set
     */
    public void setTamaisiryo(Integer tamaisiryo) {
        this.tamaisiryo = tamaisiryo;
    }

    /**
     * 外観確認
     * @return the gaikankakunin
     */
    public String getGaikankakunin() {
        return gaikankakunin;
    }

    /**
     * 外観確認
     * @param gaikankakunin the gaikankakunin to set
     */
    public void setGaikankakunin(String gaikankakunin) {
        this.gaikankakunin = gaikankakunin;
    }

    /**
     * 開始確認者
     * @return the kaisikakusya
     */
    public String getKaisikakusya() {
        return kaisikakusya;
    }

    /**
     * 開始確認者
     * @param kaisikakusya the kaisikakusya to set
     */
    public void setKaisikakusya(String kaisikakusya) {
        this.kaisikakusya = kaisikakusya;
    }

    /**
     * 終了担当者
     * @return the syuryotantosya
     */
    public String getSyuryotantosya() {
        return syuryotantosya;
    }

    /**
     * 終了担当者
     * @param syuryotantosya the syuryotantosya to set
     */
    public void setSyuryotantosya(String syuryotantosya) {
        this.syuryotantosya = syuryotantosya;
    }

    
}
