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
 * 変更日	2019/07/15<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * 変更日	2020/09/15<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	項目追加<br>
 * <br>
 * 変更日	2021/09/15<br>
 * 計画書No	MB2108-DK001<br>
 * 変更者	SRC T.Takenouchi<br>
 * 変更理由	項目追加<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SR_BARREL1(バレル)のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2019/07/15
 */
public class SrBarrel1 {
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
     * KCPNO
     */
    private String kcpno;

    /**
     * ﾊﾞﾚﾙ開始日時
     */
    private Timestamp bkaisinichiji;

    /**
     * ﾊﾞﾚﾙ終了日時
     */
    private Timestamp bsyuryonichiji;

    /**
     * ﾊﾞﾚﾙ条件設定ﾓｰﾄﾞ
     */
    private String bjyokensetteimode;

    /**
     * ﾊﾞﾚﾙ条件周速度(研磨機回転数①)
     */
    private BigDecimal bjyokensyusokudo;

    /**
     * ﾊﾞﾚﾙ号機(研磨号機)
     */
    private String bgoki;

    /**
     * ﾊﾞﾚﾙ時間(研磨時間①)
     */
    private Integer bjikan;

    /**
     * ﾎﾟｯﾄ数
     */
    private Integer potsuu;

    /**
     * ﾁｯﾌﾟ破片確認
     */
    private String chiphahenkakunin;

    /**
     * ﾎﾟｯﾄ内確認
     */
    private String potkakunin;

    /**
     * ﾊﾞﾚﾙ担当者
     */
    private String btantosya;

    /**
     * ﾎﾟｯﾄ取り出し担当者
     */
    private String ptantosya;

    /**
     * ﾊﾞﾚﾙﾎﾟｯﾄNO1
     */
    private String bpotno1;

    /**
     * 乾燥開始日時
     */
    private Timestamp kankaisinichiji;

    /**
     * 乾燥終了日時
     */
    private Timestamp kansyuryonichiji;

    /**
     * 乾燥担当者
     */
    private String kantantosya;

    /**
     * ﾒﾃﾞｨｱ選別
     */
    private String mediasenbetu;

    /**
     * ﾊﾞﾚﾙﾎﾟｯﾄNO2
     */
    private String bpotno2;

    /**
     * 計数日時
     */
    private Timestamp keinichiji;

    /**
     * 受入個数
     */
    private Integer ukeirekosuu;

    /**
     * 単位重量
     */
    private BigDecimal tanijyuryo;

    /**
     * 良品個数
     */
    private Integer ryohinkosuu;

    /**
     * 不良数
     */
    private Integer furyosuu;

    /**
     * 歩留まり
     */
    private BigDecimal budomari;

    /**
     * 計数担当者
     */
    private String keitantosya;

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
    private Timestamp kosinNichiji;

    /**
     * 研磨方式
     */
    private String kenma;

    /**
     * 研磨材量
     */
    private Integer kenmazairyo;

    /**
     * 研磨材種類
     */
    private String kenmazaisyurui;

    /**
     * 玉石種類
     */
    private String tamaishisyurui;

    /**
     * 玉石量
     */
    private Integer tamaishiryou;

    /**
     * 外観確認
     */
    private Integer gaikancheck;

    /**
     * 開始確認者
     */
    private String startkakuninsyacode;

    /**
     * 終了担当者
     */
    private String endtantosyacode;

    /**
     * revision
     */
    private Integer revision;

    /**
     * 研磨時間単位
     */
    private String kenmajikantani;

    /**
     * 削除ﾌﾗｸﾞ
     */
    private Integer deleteflag;
    
    /**
     * ﾎﾟｯﾄ種類
     */
    private String potsyurui;

    /**
     * ﾁｬｰｼﾞ量
     */
    private Integer chargeryou;

    /**
     * 研磨時間②
     */
    private Integer bjikan2;

    /**
     * 研磨機回転数②
     */
    private BigDecimal bjyokensyusokudo2;

    /**
     * 研磨時間③
     */
    private Integer bjikan3;

    /**
     * 研磨機回転数③
     */
    private BigDecimal bjyokensyusokudo3;

    /**
     * 研磨時間④
     */
    private Integer bjikan4;

    /**
     * 研磨機回転数④
     */
    private BigDecimal bjyokensyusokudo4;

    /**
     * 研磨時間⑤
     */
    private Integer bjikan5;

    /**
     * 研磨機回転数⑤
     */
    private BigDecimal bjyokensyusokudo5;

    /**
     * 研磨時間⑥
     */
    private Integer bjikan6;

    /**
     * 研磨機回転数⑥
     */
    private BigDecimal bjyokensyusokudo6;

    /**
     * 研磨時間合計
     */
    private Integer bjikantotal;

    /**
     * 研磨材量②
     */
    private Integer kenmazairyo2;

    /**
     * 研磨材種類②
     */
    private String kenmazaisyurui2;

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
     * @return jissekino
     */
    public Integer getJissekino() {
        return jissekino;
    }

    /**
     * @param jissekino セットする jissekino
     */
    public void setJissekino(Integer jissekino) {
        this.jissekino = jissekino;
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
     * @return bkaisinichiji
     */
    public Timestamp getBkaisinichiji() {
        return bkaisinichiji;
    }

    /**
     * @param bkaisinichiji セットする bkaisinichiji
     */
    public void setBkaisinichiji(Timestamp bkaisinichiji) {
        this.bkaisinichiji = bkaisinichiji;
    }

    /**
     * @return bsyuryonichiji
     */
    public Timestamp getBsyuryonichiji() {
        return bsyuryonichiji;
    }

    /**
     * @param bsyuryonichiji セットする bsyuryonichiji
     */
    public void setBsyuryonichiji(Timestamp bsyuryonichiji) {
        this.bsyuryonichiji = bsyuryonichiji;
    }

    /**
     * @return bjyokensetteimode
     */
    public String getBjyokensetteimode() {
        return bjyokensetteimode;
    }

    /**
     * @param bjyokensetteimode セットする bjyokensetteimode
     */
    public void setBjyokensetteimode(String bjyokensetteimode) {
        this.bjyokensetteimode = bjyokensetteimode;
    }

    /**
     * @return bjyokensyusokudo
     */
    public BigDecimal getBjyokensyusokudo() {
        return bjyokensyusokudo;
    }

    /**
     * @param bjyokensyusokudo セットする bjyokensyusokudo
     */
    public void setBjyokensyusokudo(BigDecimal bjyokensyusokudo) {
        this.bjyokensyusokudo = bjyokensyusokudo;
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
     * @return bjikan
     */
    public Integer getBjikan() {
        return bjikan;
    }

    /**
     * @param bjikan セットする bjikan
     */
    public void setBjikan(Integer bjikan) {
        this.bjikan = bjikan;
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
     * @return chiphahenkakunin
     */
    public String getChiphahenkakunin() {
        return chiphahenkakunin;
    }

    /**
     * @param chiphahenkakunin セットする chiphahenkakunin
     */
    public void setChiphahenkakunin(String chiphahenkakunin) {
        this.chiphahenkakunin = chiphahenkakunin;
    }

    /**
     * @return potkakunin
     */
    public String getPotkakunin() {
        return potkakunin;
    }

    /**
     * @param potkakunin セットする potkakunin
     */
    public void setPotkakunin(String potkakunin) {
        this.potkakunin = potkakunin;
    }

    /**
     * @return btantosya
     */
    public String getBtantosya() {
        return btantosya;
    }

    /**
     * @param btantosya セットする btantosya
     */
    public void setBtantosya(String btantosya) {
        this.btantosya = btantosya;
    }

    /**
     * @return ptantosya
     */
    public String getPtantosya() {
        return ptantosya;
    }

    /**
     * @param ptantosya セットする ptantosya
     */
    public void setPtantosya(String ptantosya) {
        this.ptantosya = ptantosya;
    }

    /**
     * @return bpotno1
     */
    public String getBpotno1() {
        return bpotno1;
    }

    /**
     * @param bpotno1 セットする bpotno1
     */
    public void setBpotno1(String bpotno1) {
        this.bpotno1 = bpotno1;
    }

    /**
     * @return kankaisinichiji
     */
    public Timestamp getKankaisinichiji() {
        return kankaisinichiji;
    }

    /**
     * @param kankaisinichiji セットする kankaisinichiji
     */
    public void setKankaisinichiji(Timestamp kankaisinichiji) {
        this.kankaisinichiji = kankaisinichiji;
    }

    /**
     * @return kansyuryonichiji
     */
    public Timestamp getKansyuryonichiji() {
        return kansyuryonichiji;
    }

    /**
     * @param kansyuryonichiji セットする kansyuryonichiji
     */
    public void setKansyuryonichiji(Timestamp kansyuryonichiji) {
        this.kansyuryonichiji = kansyuryonichiji;
    }

    /**
     * @return kantantosya
     */
    public String getKantantosya() {
        return kantantosya;
    }

    /**
     * @param kantantosya セットする kantantosya
     */
    public void setKantantosya(String kantantosya) {
        this.kantantosya = kantantosya;
    }

    /**
     * @return mediasenbetu
     */
    public String getMediasenbetu() {
        return mediasenbetu;
    }

    /**
     * @param mediasenbetu セットする mediasenbetu
     */
    public void setMediasenbetu(String mediasenbetu) {
        this.mediasenbetu = mediasenbetu;
    }

    /**
     * @return bpotno2
     */
    public String getBpotno2() {
        return bpotno2;
    }

    /**
     * @param bpotno2 セットする bpotno2
     */
    public void setBpotno2(String bpotno2) {
        this.bpotno2 = bpotno2;
    }

    /**
     * @return keinichiji
     */
    public Timestamp getKeinichiji() {
        return keinichiji;
    }

    /**
     * @param keinichiji セットする keinichiji
     */
    public void setKeinichiji(Timestamp keinichiji) {
        this.keinichiji = keinichiji;
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
     * @return tanijyuryo
     */
    public BigDecimal getTanijyuryo() {
        return tanijyuryo;
    }

    /**
     * @param tanijyuryo セットする tanijyuryo
     */
    public void setTanijyuryo(BigDecimal tanijyuryo) {
        this.tanijyuryo = tanijyuryo;
    }

    /**
     * @return ryohinkosuu
     */
    public Integer getRyohinkosuu() {
        return ryohinkosuu;
    }

    /**
     * @param ryohinkosuu セットする ryohinkosuu
     */
    public void setRyohinkosuu(Integer ryohinkosuu) {
        this.ryohinkosuu = ryohinkosuu;
    }

    /**
     * @return furyosuu
     */
    public Integer getFuryosuu() {
        return furyosuu;
    }

    /**
     * @param furyosuu セットする furyosuu
     */
    public void setFuryosuu(Integer furyosuu) {
        this.furyosuu = furyosuu;
    }

    /**
     * @return budomari
     */
    public BigDecimal getBudomari() {
        return budomari;
    }

    /**
     * @param budomari セットする budomari
     */
    public void setBudomari(BigDecimal budomari) {
        this.budomari = budomari;
    }

    /**
     * @return keitantosya
     */
    public String getKeitantosya() {
        return keitantosya;
    }

    /**
     * @param keitantosya セットする keitantosya
     */
    public void setKeitantosya(String keitantosya) {
        this.keitantosya = keitantosya;
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
     * @return kosinNichiji
     */
    public Timestamp getKosinNichiji() {
        return kosinNichiji;
    }

    /**
     * @param kosinNichiji セットする kosinNichiji
     */
    public void setKosinNichiji(Timestamp kosinNichiji) {
        this.kosinNichiji = kosinNichiji;
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
     * @return kenmazairyo
     */
    public Integer getKenmazairyo() {
        return kenmazairyo;
    }

    /**
     * @param kenmazairyo セットする kenmazairyo
     */
    public void setKenmazairyo(Integer kenmazairyo) {
        this.kenmazairyo = kenmazairyo;
    }

    /**
     * @return kenmazaisyurui
     */
    public String getKenmazaisyurui() {
        return kenmazaisyurui;
    }

    /**
     * @param kenmazaisyurui セットする kenmazaisyurui
     */
    public void setKenmazaisyurui(String kenmazaisyurui) {
        this.kenmazaisyurui = kenmazaisyurui;
    }

    /**
     * @return tamaishisyurui
     */
    public String getTamaishisyurui() {
        return tamaishisyurui;
    }

    /**
     * @param tamaishisyurui セットする tamaishisyurui
     */
    public void setTamaishisyurui(String tamaishisyurui) {
        this.tamaishisyurui = tamaishisyurui;
    }

    /**
     * @return tamaishiryou
     */
    public Integer getTamaishiryou() {
        return tamaishiryou;
    }

    /**
     * @param tamaishiryou セットする tamaishiryou
     */
    public void setTamaishiryou(Integer tamaishiryou) {
        this.tamaishiryou = tamaishiryou;
    }

    /**
     * @return gaikancheck
     */
    public Integer getGaikancheck() {
        return gaikancheck;
    }

    /**
     * @param gaikancheck セットする gaikancheck
     */
    public void setGaikancheck(Integer gaikancheck) {
        this.gaikancheck = gaikancheck;
    }

    /**
     * @return startkakuninsyacode
     */
    public String getStartkakuninsyacode() {
        return startkakuninsyacode;
    }

    /**
     * @param startkakuninsyacode セットする startkakuninsyacode
     */
    public void setStartkakuninsyacode(String startkakuninsyacode) {
        this.startkakuninsyacode = startkakuninsyacode;
    }

    /**
     * @return endtantosyacode
     */
    public String getEndtantosyacode() {
        return endtantosyacode;
    }

    /**
     * @param endtantosyacode セットする endtantosyacode
     */
    public void setEndtantosyacode(String endtantosyacode) {
        this.endtantosyacode = endtantosyacode;
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

    public String getKenmajikantani() {
        return kenmajikantani;
    }

    public void setKenmajikantani(String kenmajikantani) {
        this.kenmajikantani = kenmajikantani;
    }

    /**
     * @return deleteflag
     */
    public Integer getDeleteflag() {
        return deleteflag;
    }

    /**
     * @param deleteflag セットする deleteflag
     */
    public void setDeleteflag(Integer deleteflag) {
        this.deleteflag = deleteflag;
    }

    /**
     * ﾎﾟｯﾄ種類
     *
     * @return the potsyurui
     */
    public String getPotsyurui() {
        return potsyurui;
    }
    /**
     * ﾎﾟｯﾄ種類
     *
     * @param potsyurui the potsyurui to set
     */
    public void setPotsyurui(String potsyurui) {
        this.potsyurui = potsyurui;
    }

    /**
     * ﾁｬｰｼﾞ量
     *
     * @return the chargeryou
     */
    public Integer getChargeryou() {
        return chargeryou;
    }
    /**
     * ﾁｬｰｼﾞ量
     *
     * @param chargeryou the chargeryou to set
     */
    public void setChargeryou(Integer chargeryou) {
        this.chargeryou = chargeryou;
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
     * @return the bjyokensyusokudo2
     */
    public BigDecimal getBjyokensyusokudo2() {
        return bjyokensyusokudo2;
    }
    /**
     * 研磨機回転数②
     *
     * @param bjyokensyusokudo2 the bjyokensyusokudo2 to set
     */
    public void setBjyokensyusokudo2(BigDecimal bjyokensyusokudo2) {
        this.bjyokensyusokudo2 = bjyokensyusokudo2;
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
     * @return the bjyokensyusokudo3
     */
    public BigDecimal getBjyokensyusokudo3() {
        return bjyokensyusokudo3;
    }
    /**
     * 研磨機回転数③
     *
     * @param bjyokensyusokudo3 the bjyokensyusokudo3 to set
     */
    public void setBjyokensyusokudo3(BigDecimal bjyokensyusokudo3) {
        this.bjyokensyusokudo3 = bjyokensyusokudo3;
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
     * @return the bjyokensyusokudo4
     */
    public BigDecimal getBjyokensyusokudo4() {
        return bjyokensyusokudo4;
    }
    /**
     * 研磨機回転数④
     *
     * @param bjyokensyusokudo4 the bjyokensyusokudo4 to set
     */
    public void setBjyokensyusokudo4(BigDecimal bjyokensyusokudo4) {
        this.bjyokensyusokudo4 = bjyokensyusokudo4;
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
     * @return the bjyokensyusokudo5
     */
    public BigDecimal getBjyokensyusokudo5() {
        return bjyokensyusokudo5;
    }
    /**
     * 研磨機回転数⑤
     *
     * @param bjyokensyusokudo5 the bjyokensyusokudo5 to set
     */
    public void setBjyokensyusokudo5(BigDecimal bjyokensyusokudo5) {
        this.bjyokensyusokudo5 = bjyokensyusokudo5;
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
     * @return the bjyokensyusokudo6
     */
    public BigDecimal getBjyokensyusokudo6() {
        return bjyokensyusokudo6;
    }
    /**
     * 研磨機回転数⑥
     *
     * @param bjyokensyusokudo6 the bjyokensyusokudo6 to set
     */
    public void setBjyokensyusokudo6(BigDecimal bjyokensyusokudo6) {
        this.bjyokensyusokudo6 = bjyokensyusokudo6;
    }

    /**
     * 研磨時間合計
     *
     * @return the bjikansum
     */
    public Integer getBjikantotal() {
        return bjikantotal;
    }
    /**
     * 研磨時間合計
     *
     * @param bjikantotal the bjikantotal to set
     */
    public void setBjikantotal(Integer bjikantotal) {
        this.bjikantotal = bjikantotal;
    }

    /**
     * 研磨材量②
     *
     * @return the kenmazairyo2
     */
    public Integer getKenmazairyo2() {
        return kenmazairyo2;
    }
    /**
     * 研磨材量②
     *
     * @param kenmazairyo2 the kenmazairyo2 to set
     */
    public void setKenmazairyo2(Integer kenmazairyo2) {
        this.kenmazairyo2 = kenmazairyo2;
    }

    /**
     * 研磨材種類②
     *
     * @return the kenmazaisyurui2
     */
    public String getKenmazaisyurui2() {
        return kenmazaisyurui2;
    }
    /**
     * 研磨材種類②
     *
     * @param kenmazaisyurui2 the kenmazaisyurui2 to set
     */
    public void setKenmazaisyurui2(String kenmazaisyurui2) {
        this.kenmazaisyurui2 = kenmazaisyurui2;
    }

//</editor-fold>
    
}