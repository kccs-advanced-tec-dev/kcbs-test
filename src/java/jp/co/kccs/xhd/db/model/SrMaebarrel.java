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
 * 変更日	2021/09/22<br>
 * 計画書No	MB2108-DK001<br>
 * 変更者	SRC T.Ushiyama<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SR_MAEBARREL(焼成前バレル)のモデルクラスです。
 *
 * @author SRC T.Ushiyama
 * @since  2021/09/22
 */
public class SrMaebarrel {
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
    * 受入個数
    */
   private Integer ukeirekosuu;

   /**
    * 研磨方法
    */
   private String kenma;

   /**
    * 研磨号機
    */
   private String bgoki;

   /**
    * ﾎﾟｯﾄ数
    */
   private Integer potsuu;

   /**
    * ﾎﾟｯﾄﾁｬｰｼﾞ量
    */
   private Integer potcsuu;

   /**
    * 片栗粉量
    */
   private Integer katakuriko;

   /**
    * ﾒﾃﾞｨｱ種類
    */
   private String mediasyurui;

   /**
    * ﾒﾃﾞｨｱ量
    */
   private Integer mediasenbetu;

   /**
    * 研磨時間①
    */
   private Integer bjikan1;

   /**
    * 研磨機回転数①
    */
   private Integer bkaiten1;

   /**
    * 研磨時間②
    */
   private Integer bjikan2;

   /**
    * 研磨機回転数②
    */
   private Integer bkaiten2;

   /**
    * 研磨時間③
    */
   private Integer bjikan3;

   /**
    * 研磨機回転数③
    */
   private Integer bkaiten3;

   /**
    * 研磨時間④
    */
   private Integer bjikan4;

   /**
    * 研磨機回転数④
    */
   private Integer bkaiten4;

   /**
    * 研磨時間⑤
    */
   private Integer bjikan5;

   /**
    * 研磨機回転数⑤
    */
   private Integer bkaiten5;

   /**
    * 研磨時間⑥
    */
   private Integer bjikan6;

   /**
    * 研磨機回転数⑥
    */
   private Integer bkaiten6;

   /**
    * 開始日時
    */
   private Timestamp kaisinichiji;

   /**
    * 担当者
    */
   private String kaisitantosya;

   /**
    * 開始確認者
    */
   private String kaisikakuninsya;

   /**
    * 終了日時
    */
   private Timestamp syuryonichiji;

   /**
    * 終了担当者
    */
   private String syuryotantosya;

   /**
    * 備考1
    */
   private String biko1;

   /**
    * 備考2
    */
   private String biko2;

   /**
    * 備考3
    */
   private String biko3;

   /**
    * 備考4
    */
   private String biko4;

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



//<editor-fold defaultstate="collapsed" desc="#Getter・Setter">
    /**
     * @return kojyo
     */
    public String getKojyo() {
        return kojyo;
    }

    /**
     * @param kojyo セットする kojyo
     */
    public void setKojyo(String kojyo) {
        this.kojyo = kojyo;
    }

    /**
     * @return lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * @param lotno セットする lotno
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * @return edaban
     */
    public String getEdaban() {
        return edaban;
    }

    /**
     * @param edaban セットする edaban
     */
    public void setEdaban(String edaban) {
        this.edaban = edaban;
    }

    /**
     * @return kcpno
     */
    public String getKcpno() {
        return kcpno;
    }

    /**
     * @param kcpno セットする kcpno
     */
    public void setKcpno(String kcpno) {
        this.kcpno = kcpno;
    }

    /**
     * @return tokuisaki
     */
    public String getTokuisaki() {
        return tokuisaki;
    }

    /**
     * @param tokuisaki セットする tokuisaki
     */
    public void setTokuisaki(String tokuisaki) {
        this.tokuisaki = tokuisaki;
    }

    /**
     * @return lotkubuncode
     */
    public String getLotkubuncode() {
        return lotkubuncode;
    }

    /**
     * @param lotkubuncode セットする lotkubuncode
     */
    public void setLotkubuncode(String lotkubuncode) {
        this.lotkubuncode = lotkubuncode;
    }

    /**
     * @return ownercode
     */
    public String getOwnercode() {
        return ownercode;
    }

    /**
     * @param ownercode セットする ownercode
     */
    public void setOwnercodee(String ownercode) {
        this.ownercode = ownercode;
    }

    /**
     * @return ukeirekosuu
     */
    public Integer getUkeirekosuu() {
        return ukeirekosuu;
    }

    /**
     * @param ukeirekosuu セットする ukeirekosuu
     */
    public void setUkeirekosuu(Integer ukeirekosuu) {
        this.ukeirekosuu = ukeirekosuu;
    }

    /**
     * @return kenma
     */
    public String getKenma() {
        return kenma;
    }

    /**
     * @param kenma セットする kenma
     */
    public void setKenma(String kenma) {
        this.kenma = kenma;
    }

    /**
     * @return bgoki
     */
    public String getBgoki() {
        return bgoki;
    }

    /**
     * @param bgoki セットする bgoki
     */
    public void setBgoki(String bgoki) {
        this.bgoki = bgoki;
    }

    /**
     * @return potsuu
     */
    public Integer getPotsuu() {
        return potsuu;
    }

    /**
     * @param potsuu セットする potsuu
     */
    public void setPotsuu(Integer potsuu) {
        this.potsuu = potsuu;
    }

    /**
     * @return potcsuu
     */
    public Integer getPotcsuu() {
        return potcsuu;
    }

    /**
     * @param potcsuu セットする potcsuu
     */
    public void setPotcsuu(Integer potcsuu) {
        this.potcsuu = potcsuu;
    }

    /**
     * @return katakuriko
     */
    public Integer getKatakuriko() {
        return katakuriko;
    }

    /**
     * @param katakuriko セットする katakuriko
     */
    public void setKatakuriko(Integer katakuriko) {
        this.katakuriko = katakuriko;
    }

    /**
     * @return mediasyurui
     */
    public String getMediasyurui() {
        return mediasyurui;
    }

    /**
     * @param mediasyurui セットする mediasyurui
     */
    public void setMediasyurui(String mediasyurui) {
        this.mediasyurui = mediasyurui;
    }

    /**
     * @return mediasenbetu
     */
    public Integer getMediasenbetu() {
        return mediasenbetu;
    }

    /**
     * @param mediasenbetu セットする mediasenbetu
     */
    public void setMediasenbetu(Integer mediasenbetu) {
        this.mediasenbetu = mediasenbetu;
    }

    /**
     * 研磨時間①
     *
     * @return the bjikan1
     */
    public Integer getBjikan1() {
        return bjikan1;
    }
    /**
     * 研磨時間①
     *
     * @param bjikan1 the bjikan1 to set
     */
    public void setBjikan1(Integer bjikan1) {
        this.bjikan1 = bjikan1;
    }

    /**
     * 研磨機回転数①
     *
     * @return the bkaiten1
     */
    public Integer getBkaiten1() {
        return bkaiten1;
    }
    /**
     * 研磨機回転数①
     *
     * @param bkaiten1 the bkaiten1 to set
     */
    public void setBkaiten1(Integer bkaiten1) {
        this.bkaiten1 = bkaiten1;
    }

    /**
     * 研磨時間②
     *
     * @return the bjikan2
     */
    public Integer getBjikan2() {
        return bjikan2;
    }
    /**
     * 研磨時間②
     *
     * @param bjikan2 the bjikan2 to set
     */
    public void setBjikan2(Integer bjikan2) {
        this.bjikan2 = bjikan2;
    }

    /**
     * 研磨機回転数②
     *
     * @return the bkaiten2
     */
    public Integer getBkaiten2() {
        return bkaiten2;
    }
    /**
     * 研磨機回転数②
     *
     * @param bkaiten2 the bkaiten2 to set
     */
    public void setBkaiten2(Integer bkaiten2) {
        this.bkaiten2 = bkaiten2;
    }

    /**
     * 研磨時間③
     *
     * @return the bjikan3
     */
    public Integer getBjikan3() {
        return bjikan3;
    }
    /**
     * 研磨時間③
     *
     * @param bjikan3 the bjikan3 to set
     */
    public void setBjikan3(Integer bjikan3) {
        this.bjikan3 = bjikan3;
    }

    /**
     * 研磨機回転数③
     *
     * @return the bkaiten3
     */
    public Integer getBkaiten3() {
        return bkaiten3;
    }
    /**
     * 研磨機回転数③
     *
     * @param bkaiten3 the bkaiten3 to set
     */
    public void setBkaiten3(Integer bkaiten3) {
        this.bkaiten3 = bkaiten3;
    }

    /**
     * 研磨時間④
     *
     * @return the bjikan4
     */
    public Integer getBjikan4() {
        return bjikan4;
    }
    /**
     * 研磨時間④
     *
     * @param bjikan4 the bjikan4 to set
     */
    public void setBjikan4(Integer bjikan4) {
        this.bjikan4 = bjikan4;
    }

    /**
     * 研磨機回転数④
     *
     * @return the bkaiten4
     */
    public Integer getBkaiten4() {
        return bkaiten4;
    }
    /**
     * 研磨機回転数④
     *
     * @param bkaiten4 the bkaiten4 to set
     */
    public void setBkaiten4(Integer bkaiten4) {
        this.bkaiten4 = bkaiten4;
    }

    /**
     * 研磨時間⑤
     *
     * @return the bjikan5
     */
    public Integer getBjikan5() {
        return bjikan5;
    }
    /**
     * 研磨時間⑤
     *
     * @param bjikan5 the bjikan5 to set
     */
    public void setBjikan5(Integer bjikan5) {
        this.bjikan5 = bjikan5;
    }

    /**
     * 研磨機回転数⑤
     *
     * @return the bkaiten5
     */
    public Integer getBkaiten5() {
        return bkaiten5;
    }
    /**
     * 研磨機回転数⑤
     *
     * @param bkaiten5 the bkaiten5 to set
     */
    public void setBkaiten5(Integer bkaiten5) {
        this.bkaiten5 = bkaiten5;
    }

    /**
     * 研磨時間⑥
     *
     * @return the bjikan6
     */
    public Integer getBjikan6() {
        return bjikan6;
    }
    /**
     * 研磨時間⑥
     *
     * @param bjikan6 the bjikan6 to set
     */
    public void setBjikan6(Integer bjikan6) {
        this.bjikan6 = bjikan6;
    }

    /**
     * 研磨機回転数⑥
     *
     * @return the bkaiten6
     */
    public Integer getBkaiten6() {
        return bkaiten6;
    }
    /**
     * 研磨機回転数⑥
     *
     * @param bkaiten6 the bkaiten6 to set
     */
    public void setBkaiten6(Integer bkaiten6) {
        this.bkaiten6 = bkaiten6;
    }

    /**
     * @return kaisinichiji
     */
    public Timestamp getKaisinichiji() {
        return kaisinichiji;
    }

    /**
     * @param kaisinichiji セットする kaisinichiji
     */
    public void setKaisinichiji(Timestamp kaisinichiji) {
        this.kaisinichiji = kaisinichiji;
    }

    /**
     * @return kaisitantosya
     */
    public String getKaisitantosya() {
        return kaisitantosya;
    }

    /**
     * @param kaisitantosya セットする kaisitantosya
     */
    public void setKaisitantosya(String kaisitantosya) {
        this.kaisitantosya = kaisitantosya;
    }

    /**
     * @return kaisikakuninsya
     */
    public String getKaisikakuninsya() {
        return kaisikakuninsya;
    }

    /**
     * @param kaisikakuninsya セットする kaisikakuninsya
     */
    public void setKaisikakuninsya(String kaisikakuninsya) {
        this.kaisikakuninsya = kaisikakuninsya;
    }

    /**
     * @return syuryonichiji
     */
    public Timestamp getSyuryonichiji() {
        return syuryonichiji;
    }

    /**
     * @param syuryonichiji セットする syuryonichiji
     */
    public void setSyuryonichiji(Timestamp syuryonichiji) {
        this.syuryonichiji = syuryonichiji;
    }

    /**
     * @return syuryotantosya
     */
    public String getSyuryotantosya() {
        return syuryotantosya;
    }

    /**
     * @param syuryotantosya セットする syuryotantosya
     */
    public void setSyuryotantosya(String syuryotantosya) {
        this.syuryotantosya = syuryotantosya;
    }

    /**
     * @return biko1
     */
    public String getBiko1() {
        return biko1;
    }

    /**
     * @param biko1 セットする biko1
     */
    public void setBiko1(String biko1) {
        this.biko1 = biko1;
    }

    /**
     * @return biko2
     */
    public String getBiko2() {
        return biko2;
    }

    /**
     * @param biko2 セットする biko2
     */
    public void setBiko2(String biko2) {
        this.biko2 = biko2;
    }

    /**
     * @return biko3
     */
    public String getBiko3() {
        return biko3;
    }

    /**
     * @param biko3 セットする biko3
     */
    public void setBiko3(String biko3) {
        this.biko3 = biko3;
    }

    /**
     * @return biko4
     */
    public String getBiko4() {
        return biko4;
    }

    /**
     * @param biko4 セットする biko4
     */
    public void setBiko4(String biko4) {
        this.biko4 = biko4;
    }

    /**
     * @return torokunichiji
     */
    public Timestamp getTorokunichiji() {
        return torokunichiji;
    }

    /**
     * @param torokunichiji セットする torokunichiji
     */
    public void setTorokunichiji(Timestamp torokunichiji) {
        this.torokunichiji = torokunichiji;
    }

    /**
     * @return kosinnichiji
     */
    public Timestamp getKosinnichiji() {
        return kosinnichiji;
    }

    /**
     * @param kosinnichiji セットする kosinnichiji
     */
    public void setKosinnichiji(Timestamp kosinnichiji) {
        this.kosinnichiji = kosinnichiji;
    }

    /**
     * @return revision
     */
    public Integer getRevision() {
        return revision;
    }

    /**
     * @param revision セットする revision
     */
    public void setRevision(Integer revision) {
        this.revision = revision;
    }

//</editor-fold>
    
}