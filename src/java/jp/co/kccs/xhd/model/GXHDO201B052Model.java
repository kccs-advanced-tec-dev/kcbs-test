/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2021/09/20<br>
 * 計画書No	MB2108-DK001<br>
 * 変更者	SRC Y.Kurozumi<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 焼成・焼成前ﾊﾞﾚﾙ履歴検索画面のモデルクラスです。
 *
 * @author SRC Y.Kurozumi
 * @since 2021/09/20
 */
public class GXHDO201B052Model implements Serializable{
    
    /** ﾛｯﾄNo. */
    private String lotno = "";
    /** KCPNO */
    private String kcpno = "";
    /** 客先 */
    private String tokuisaki = "";
    /** ﾛｯﾄ区分 */
    private String lotkubuncode = "";
    /** ｵｰﾅｰ */
    private String ownercode = "";
    /** 受入個数 */
    private Integer ukeirekosuu = null;
    /** 研磨方法 */
    private String kenma = "";
    /** 研磨号機 */
    private String bgoki = "";
    /** ﾎﾟｯﾄ数 */
    private Integer potsuu = null;
    /** ﾎﾟｯﾄﾁｬｰｼﾞ量 */
    private Integer potcsuu = null;
    /** 片栗粉量 */
    private Integer katakuriko = null;
    /** ﾒﾃﾞｨｱ種類 */
    private String mediasyurui = "";
    /** ﾒﾃﾞｨｱ量 */
    private Integer mediasenbetu = null;
    /** 研磨時間① */
    private Integer bjikan1 = null;
    /** 研磨機回転数① */
    private Integer bkaiten1 = null;
    /** 研磨時間② */
    private Integer bjikan2 = null;
    /** 研磨機回転数② */
    private Integer bkaiten2 = null;
    /** 研磨時間③ */
    private Integer bjikan3 = null;
    /** 研磨機回転数③ */
    private Integer bkaiten3 = null;
    /** 研磨時間④ */
    private Integer bjikan4 = null;
    /** 研磨機回転数④ */
    private Integer bkaiten4 = null;
    /** 研磨時間⑤ */
    private Integer bjikan5 = null;
    /** 研磨機回転数⑤ */
    private Integer bkaiten5 = null;
    /** 研磨時間5⑥ */
    private Integer bjikan6 = null;
    /** 研磨機回転数⑥ */
    private Integer bkaiten6 = null;
    /** 開始日時 */
    private Timestamp kaisinichiji = null;
    /** 開始担当者 */
    private String kaisitantosya = "";
    /** 開始確認者 */
    private String kaisikakuninsya = "";
    /** 終了日時 */
    private Timestamp syuryonichiji = null;
    /** 終了担当者 */
    private String syuryotantosya = "";
    /** 備考1 */
    private String biko1 = "";
    /** 備考2 */
    private String biko2 = "";
    /** 備考3 */
    private String biko3 = "";
    /** 備考4 */
    private String biko4 = "";
    
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
     * 客先
     * @return the tokuisaki
     */
    public String getTokuisaki() {
        return tokuisaki;
    }

    /**
     * 客先
     * @param tokuisaki the tokuisaki to set
     */
    public void setTokuisaki(String tokuisaki) {
        this.tokuisaki = tokuisaki;
    }

    /**
     * ﾛｯﾄ区分
     * @return the lotkubuncode
     */
    public String getLotkubuncode() {
        return lotkubuncode;
    }

    /**
     * ﾛｯﾄ区分
     * @param lotkubuncode the lotkubuncode to set
     */
    public void setLotkubuncode(String lotkubuncode) {
        this.lotkubuncode = lotkubuncode;
    }

    /**
     * ｵｰﾅｰ
     * @return the ownercode
     */
    public String getOwnercode() {
        return ownercode;
    }

    /**
     * ｵｰﾅｰ
     * @param ownercode the ownercode to set
     */
    public void setOwnercode(String ownercode) {
        this.ownercode = ownercode;
    }

    /**
     * 受入個数
     * @return the ukeirekosuu
     */
    public Integer getUkeirekosuu() {
        return ukeirekosuu;
    }

    /**
     * 受入個数
     * @param ukeirekosuu the ukeirekosuu to set
     */
    public void setUkeirekosuu(Integer ukeirekosuu) {
        this.ukeirekosuu = ukeirekosuu;
    }

    /**
     * 研磨方法
     * @return the kenma
     */
    public String getKenma() {
        return kenma;
    }

    /**
     * 研磨方法
     * @param kenma the kenma to set
     */
    public void setKenma(String kenma) {
        this.kenma = kenma;
    }

    /**
     * 研磨号機
     * @return the bgoki
     */
    public String getBgoki() {
        return bgoki;
    }

    /**
     * 研磨号機
     * @param bgoki the bgoki to set
     */
    public void setBgoki(String bgoki) {
        this.bgoki = bgoki;
    }

    /**
     * ﾎﾟｯﾄ数
     * @return the potsuu
     */
    public Integer getPotsuu() {
        return potsuu;
    }

    /**
     * ﾎﾟｯﾄ数
     * @param potsuu the potsuu to set
     */
    public void setPotsuu(Integer potsuu) {
        this.potsuu = potsuu;
    }

    /**
     * ﾎﾟｯﾄﾁｬｰｼﾞ量
     * @return the potcsuu
     */
    public Integer getPotcsuu() {
        return potcsuu;
    }

    /**
     * ﾎﾟｯﾄﾁｬｰｼﾞ量
     * @param potcsuu the potcsuu to set
     */
    public void setPotcsuu(Integer potcsuu) {
        this.potcsuu = potcsuu;
    }

    /**
     * 片栗粉量
     * @return the katakuriko
     */
    public Integer getKatakuriko() {
        return katakuriko;
    }

    /**
     * 片栗粉量
     * @param katakuriko the katakuriko to set
     */
    public void setKatakuriko(Integer katakuriko) {
        this.katakuriko = katakuriko;
    }

    /**
     * ﾒﾃﾞｨｱ種類
     * @return the mediasyurui
     */
    public String getMediasyurui() {
        return mediasyurui;
    }

    /**
     * ﾒﾃﾞｨｱ種類
     * @param mediasyurui the mediasyurui to set
     */
    public void setMediasyurui(String mediasyurui) {
        this.mediasyurui = mediasyurui;
    }

    /**
     * ﾒﾃﾞｨｱ量
     * @return the mediasenbetu
     */
    public Integer getMediasenbetu() {
        return mediasenbetu;
    }

    /**
     * ﾒﾃﾞｨｱ量
     * @param mediasenbetu the mediasenbetu to set
     */
    public void setMediasenbetu(Integer mediasenbetu) {
        this.mediasenbetu = mediasenbetu;
    }

    /**
     * 研磨時間①
     * @return the bjikan1
     */
    public Integer getBjikan1() {
        return bjikan1;
    }

    /**
     * 研磨時間①
     * @param bjikan1 the bjikan1 to set
     */
    public void setBjikan1(Integer bjikan1) {
        this.bjikan1 = bjikan1;
    }

    /**
     * 研磨機回転数①
     * @return the bkaiten1
     */
    public Integer getBkaiten1() {
        return bkaiten1;
    }

    /**
     * 研磨機回転数①
     * @param bkaiten1 the bkaiten1 to set
     */
    public void setBkaiten1(Integer bkaiten1) {
        this.bkaiten1 = bkaiten1;
    }

    /**
     * 研磨時間②
     * @return the bjikan2
     */
    public Integer getBjikan2() {
        return bjikan2;
    }

    /**
     * 研磨時間②
     * @param bjikan2 the bjikan2 to set
     */
    public void setBjikan2(Integer bjikan2) {
        this.bjikan2 = bjikan2;
    }

    /**
     * 研磨機回転数②
     * @return the bkaiten2
     */
    public Integer getBkaiten2() {
        return bkaiten2;
    }

    /**
     * 研磨機回転数②
     * @param bkaiten2 the bkaiten2 to set
     */
    public void setBkaiten2(Integer bkaiten2) {
        this.bkaiten2 = bkaiten2;
    }

    /**
     * 研磨時間③
     * @return the bjikan3
     */
    public Integer getBjikan3() {
        return bjikan3;
    }

    /**
     * 研磨時間③
     * @param bjikan3 the bjikan3 to set
     */
    public void setBjikan3(Integer bjikan3) {
        this.bjikan3 = bjikan3;
    }

    /**
     * 研磨機回転数③
     * @return the bkaiten3
     */
    public Integer getBkaiten3() {
        return bkaiten3;
    }

    /**
     * 研磨機回転数③
     * @param bkaiten3 the bkaiten3 to set
     */
    public void setBkaiten3(Integer bkaiten3) {
        this.bkaiten3 = bkaiten3;
    }

    /**
     * 研磨時間④
     * @return the bjikan4
     */
    public Integer getBjikan4() {
        return bjikan4;
    }

    /**
     * 研磨時間④
     * @param bjikan4 the bjikan4 to set
     */
    public void setBjikan4(Integer bjikan4) {
        this.bjikan4 = bjikan4;
    }

    /**
     * 研磨機回転数④
     * @return the bkaiten4
     */
    public Integer getBkaiten4() {
        return bkaiten4;
    }

    /**
     * 研磨機回転数④
     * @param bkaiten4 the bkaiten4 to set
     */
    public void setBkaiten4(Integer bkaiten4) {
        this.bkaiten4 = bkaiten4;
    }

    /**
     * 研磨時間⑤
     * @return the bjikan5
     */
    public Integer getBjikan5() {
        return bjikan5;
    }

    /**
     * 研磨時間⑤
     * @param bjikan5 the bjikan5 to set
     */
    public void setBjikan5(Integer bjikan5) {
        this.bjikan5 = bjikan5;
    }

    /**
     * 研磨機回転数⑤
     * @return the bkaiten5
     */
    public Integer getBkaiten5() {
        return bkaiten5;
    }

    /**
     * 研磨機回転数⑤
     * @param bkaiten5 the bkaiten5 to set
     */
    public void setBkaiten5(Integer bkaiten5) {
        this.bkaiten5 = bkaiten5;
    }

    /**
     * 研磨時間⑥
     * @return the bjikan6
     */
    public Integer getBjikan6() {
        return bjikan6;
    }

    /**
     * 研磨時間⑥
     * @param bjikan6 the bjikan6 to set
     */
    public void setBjikan6(Integer bjikan6) {
        this.bjikan6 = bjikan6;
    }

    /**
     * 研磨機回転数⑥
     * @return the bkaiten6
     */
    public Integer getBkaiten6() {
        return bkaiten6;
    }

    /**
     * 研磨機回転数⑥
     * @param bkaiten6 the bkaiten6 to set
     */
    public void setBkaiten6(Integer bkaiten6) {
        this.bkaiten6 = bkaiten6;
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
     * 開始確認者
     * @return the kaisikakuninsya
     */
    public String getKaisikakuninsya() {
        return kaisikakuninsya;
    }

    /**
     * 開始確認者
     * @param kaisikakuninsya the kaisikakuninsya to set
     */
    public void setKaisikakuninsya(String kaisikakuninsya) {
        this.kaisikakuninsya = kaisikakuninsya;
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
     * 備考3
     * @return the biko3
     */
    public String getBiko3() {
        return biko3;
    }

    /**
     * 備考3
     * @param biko3 the biko3 to set
     */
    public void setBiko3(String biko3) {
        this.biko3 = biko3;
    }

    /**
     * 備考4
     * @return the biko4
     */
    public String getBiko4() {
        return biko4;
    }

    /**
     * 備考4
     * @param biko4 the biko4 to set
     */
    public void setBiko4(String biko4) {
        this.biko4 = biko4;
    }

    
}
