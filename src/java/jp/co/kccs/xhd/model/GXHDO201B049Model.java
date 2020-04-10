/*
 * Copyright 2020 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2020/03/02<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	863 K.Zhang<br>
 * 変更理由	新規作成<br>
 * <br>
 * 変更日	2020/04/09<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	ﾄｯﾌﾟﾃｰﾌﾟ、ｷｬﾘｱﾃｰﾌﾟ、ﾎﾞﾄﾑﾃｰﾌﾟ異常 型変更<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * ﾃｰﾋﾟﾝｸﾞﾁｪｯｸ履歴検索画面のモデルクラスです。
 *
 * @author 863 K.Zhang
 * @since  2020/03/02
 */
public class GXHDO201B049Model implements Serializable{

    /**
     * ﾛｯﾄNo
     */
    private String lotno = "";

    /**
     * 検査回数
     */
    private Integer kaisuu = null;

    /**
     * KCPNO
     */
    private String kcpno = "";
    
    /**
     * 客先
     */
    private String tokuisaki = "";
    
    /**
     * ﾛｯﾄ区分
     */
    private String lotkubuncode = "";

    /**
     * ｵｰﾅｰ
     */
    private String ownercode = "";

    /**
     * 良品TPﾘｰﾙ巻数①
     */
    private Integer ryouhintopreelmaki1 = null;

    /**
     * 良品TPﾘｰﾙ本数①
     */
    private Integer ryouhintopreelhonsu1 = null;

    /**
     * 良品TPﾘｰﾙ巻数②
     */
    private Integer ryouhintopreelmaki2 = null;

    /**
     * 良品TPﾘｰﾙ本数②
     */
    private Integer ryouhintopreelhonsu2 = null;

    /**
     * ﾃｰﾋﾟﾝｸﾞ号機
     */
    private String tapinggouki = "";

    /**
     * 検査場所
     */
    private String kensabasyo = "";

    /**
     * ﾘｰﾙﾁｪｯｸ数
     */
    private Integer reelchecksu = null;

    /**
     * 検査開始日時
     */
    private Timestamp kensakaisinichiji = null;

    /**
     * 検査開始担当者
     */
    private String kensakaisitantou = "";

    /**
     * 物立ち
     */
    private Integer monotati = null;

    /**
     * 剥離
     */
    private Integer hakuri = null;

    /**
     * 歯抜け
     */
    private Integer hanuke = null;

    /**
     * 破れ
     */
    private Integer rabure = null;

    /**
     * ｶｹNG
     */
    private Integer kakeng = null;

    /**
     * DIP不良
     */
    private Integer dipfuryo = null;

    /**
     * その他
     */
    private Integer sonota = null;

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟ、ｷｬﾘｱﾃｰﾌﾟ、ﾎﾞﾄﾑﾃｰﾌﾟ異常
     */
    private String tapeijyo = null;

    /**
     * ﾘｰﾙﾁｪｯｸ結果
     */
    private String reelcheckkekka = "";

    /**
     * 検査終了日時
     */
    private Timestamp kensasyuryonichiji = null;

    /**
     * 検査終了担当者
     */
    private String kensasyuryotantou = "";

    /**
     * TPNG1
     */
    private String tapeng1 = "";

    /**
     * TPNG2
     */
    private String tapeng2 = "";

    /**
     * 電気特性再検査
     */
    private String denkitokuseisaikensa = "";

    /**
     * 外観再検査
     */
    private String gaikansaikensa = "";

    /**
     * 備考1
     */
    private String bikou1 = "";

    /**
     * 備考2
     */
    private String bikou2 = "";

    /**
     * 実績No(検査回数)
     */
    private Integer tpng1jissekino = null;

    /**
     * 検査日
     */
    private Timestamp tpng1kensabi = null;
    
    /**
     * 検査区分(1=容量, 2=絶縁抵抗)
     */
    private String tpng1kensakubun = "";

    /**
     * 検査者コード
     */
    private String tpng1kensasya = "";
    
    /**
     * TP個数(依頼数)
     */
    private Integer tpng1tpkosuu = null;

    /**
     * NG1個数
     */
    private Integer tpng1ng1kosuu = null;

    /**
     * 検査抜取個数
     */
    private Integer tpng1nukitorikosuu = null;

    /**
     * 容量/絶縁抵抗NG個数
     */
    private Integer tpng1cap_ir_ngkosuu = null;

    /**
     * ショート個数
     */
    private Integer tpng1shortkosuu = null;

    /**
     * その他個数
     */
    private Integer tpng1etckosuu = null;
    
    /**
     * 備考1
     */
    private String tpng1biko1 = "";

    /**
     * 備考2
     */
    private String tpng1biko2 = "";

    /**
     * 判定(1=OK, 2=NG)
     */
    private String tpng1hantei = "";

    /**
     * 実績No(検査回数)
     */
    private Integer tpng2jissekino = null;

    /**
     * 検査日
     */
    private Timestamp tpng2kensabi = null;

    /**
     * 検査区分(1=流れ品, 2=バラシ品)
     */
    private String tpng2kensakubun = "";
    
    /**
     * 検査者コード
     */
    private String tpng2kensasya = "";

    /**
     * TP個数(依頼数)
     */
    private Integer tpng2tpkosuu = null;

    /**
     * NG2個数
     */
    private Integer tpng2ng2kosuu = null;

    /**
     * 検査抜取個数
     */
    private Integer tpng2nukitorikosuu = null;

    /**
     * Aモード個数
     */
    private Integer tpng2a_modekosuu = null;

    /**
     * Cモード個数
     */
    private Integer tpng2c_modekosuu = null;

    /**
     * Dモード個数
     */
    private Integer tpng2d_modekosuu = null;
    
    /**
     * その他個数
     */
    private Integer tpng2etckosuu = null;

    /**
     * 端子電極ハガレ個数
     */
    private Integer tpng2dhagarekosuu = null;

    /**
     * 端子電極キズ個数
     */
    private Integer tpng2dkizukosuu = null;

    /**
     * 磁器クラック個数
     */
    private Integer tpng2jcrackkosuu = null;

    /**
     * めっきなし個数
     */
    private Integer tpng2mekkinasikosuu = null;

    /**
     * めっき浮き個数
     */
    private Integer tpng2mekkiukikosuu = null;

    /**
     * 備考1
     */
    private String tpng2biko1 = "";

    /**
     * 備考2
     */
    private String tpng2biko2 = "";
     
    /**
     * 判定(1=OK, 2=NG)
     */
    private String tpng2hantei = "";
    
    /**
     * 実績No(検査回数)
     */
    private Integer tpng1jissekino2 = null;

    /**
     * 検査日
     */
    private Timestamp tpng1kensabi2 = null;
    
    /**
     * 検査区分(1=容量, 2=絶縁抵抗)
     */
    private String tpng1kensakubun2 = "";

    /**
     * 検査者コード
     */
    private String tpng1kensasya2 = "";
    
    /**
     * TP個数(依頼数)
     */
    private Integer tpng1tpkosuu2 = null;

    /**
     * NG1個数
     */
    private Integer tpng1ng1kosuu2 = null;

    /**
     * 検査抜取個数
     */
    private Integer tpng1nukitorikosuu2 = null;

    /**
     * 容量/絶縁抵抗NG個数
     */
    private Integer tpng1cap_ir_ngkosuu2 = null;

    /**
     * ショート個数
     */
    private Integer tpng1shortkosuu2 = null;

    /**
     * その他個数
     */
    private Integer tpng1etckosuu2 = null;
    
    /**
     * 備考1
     */
    private String tpng1biko1_2 = "";

    /**
     * 備考2
     */
    private String tpng1biko2_2 = "";

    /**
     * 判定(1=OK, 2=NG)
     */
    private String tpng1hantei2 = "";

    /**
     * 実績No(検査回数)
     */
    private Integer tpng2jissekino2 = null;

    /**
     * 検査日
     */
    private Timestamp tpng2kensabi2 = null;

    /**
     * 検査区分(1=流れ品, 2=バラシ品)
     */
    private String tpng2kensakubun2 = "";
    
    /**
     * 検査者コード
     */
    private String tpng2kensasya2 = "";

    /**
     * TP個数(依頼数)
     */
    private Integer tpng2tpkosuu2 = null;

    /**
     * NG2個数
     */
    private Integer tpng2ng2kosuu2 = null;

    /**
     * 検査抜取個数
     */
    private Integer tpng2nukitorikosuu2 = null;

    /**
     * Aモード個数
     */
    private Integer tpng2a_modekosuu2 = null;

    /**
     * Cモード個数
     */
    private Integer tpng2c_modekosuu2 = null;

    /**
     * Dモード個数
     */
    private Integer tpng2d_modekosuu2 = null;
    
    /**
     * その他個数
     */
    private Integer tpng2etckosuu2 = null;

    /**
     * 端子電極ハガレ個数
     */
    private Integer tpng2dhagarekosuu2 = null;

    /**
     * 端子電極キズ個数
     */
    private Integer tpng2dkizukosuu2 = null;

    /**
     * 磁器クラック個数
     */
    private Integer tpng2jcrackkosuu2 = null;

    /**
     * めっきなし個数
     */
    private Integer tpng2mekkinasikosuu2 = null;

    /**
     * めっき浮き個数
     */
    private Integer tpng2mekkiukikosuu2 = null;

    /**
     * 備考1
     */
    private String tpng2biko1_2 = "";

    /**
     * 備考2
     */
    private String tpng2biko2_2 = "";
     
    /**
     * 判定(1=OK, 2=NG)
     */
    private String tpng2hantei2 = "";
    
    /**
     * 実績No(検査回数)
     */
    private Integer tpng1jissekino3 = null;

    /**
     * 検査日
     */
    private Timestamp tpng1kensabi3 = null;
    
    /**
     * 検査区分(1=容量, 2=絶縁抵抗)
     */
    private String tpng1kensakubun3 = "";

    /**
     * 検査者コード
     */
    private String tpng1kensasya3 = "";
    
    /**
     * TP個数(依頼数)
     */
    private Integer tpng1tpkosuu3 = null;

    /**
     * NG1個数
     */
    private Integer tpng1ng1kosuu3 = null;

    /**
     * 検査抜取個数
     */
    private Integer tpng1nukitorikosuu3 = null;

    /**
     * 容量/絶縁抵抗NG個数
     */
    private Integer tpng1cap_ir_ngkosuu3 = null;

    /**
     * ショート個数
     */
    private Integer tpng1shortkosuu3 = null;

    /**
     * その他個数
     */
    private Integer tpng1etckosuu3 = null;
    
    /**
     * 備考1
     */
    private String tpng1biko1_3 = "";

    /**
     * 備考2
     */
    private String tpng1biko2_3 = "";

    /**
     * 判定(1=OK, 2=NG)
     */
    private String tpng1hantei3 = "";

    /**
     * 実績No(検査回数)
     */
    private Integer tpng2jissekino3 = null;

    /**
     * 検査日
     */
    private Timestamp tpng2kensabi3 = null;

    /**
     * 検査区分(1=流れ品, 2=バラシ品)
     */
    private String tpng2kensakubun3 = "";
    
    /**
     * 検査者コード
     */
    private String tpng2kensasya3 = "";

    /**
     * TP個数(依頼数)
     */
    private Integer tpng2tpkosuu3 = null;

    /**
     * NG2個数
     */
    private Integer tpng2ng2kosuu3 = null;

    /**
     * 検査抜取個数
     */
    private Integer tpng2nukitorikosuu3 = null;

    /**
     * Aモード個数
     */
    private Integer tpng2a_modekosuu3 = null;

    /**
     * Cモード個数
     */
    private Integer tpng2c_modekosuu3 = null;

    /**
     * Dモード個数
     */
    private Integer tpng2d_modekosuu3 = null;
    
    /**
     * その他個数
     */
    private Integer tpng2etckosuu3 = null;

    /**
     * 端子電極ハガレ個数
     */
    private Integer tpng2dhagarekosuu3 = null;

    /**
     * 端子電極キズ個数
     */
    private Integer tpng2dkizukosuu3 = null;

    /**
     * 磁器クラック個数
     */
    private Integer tpng2jcrackkosuu3 = null;

    /**
     * めっきなし個数
     */
    private Integer tpng2mekkinasikosuu3 = null;

    /**
     * めっき浮き個数
     */
    private Integer tpng2mekkiukikosuu3 = null;

    /**
     * 備考1
     */
    private String tpng2biko1_3 = "";

    /**
     * 備考2
     */
    private String tpng2biko2_3 = "";
     
    /**
     * 判定(1=OK, 2=NG)
     */
    private String tpng2hantei3 = "";

    


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
     * 検査回数
     * @return the kaisuu
     */
    public Integer getKaisuu() {
        return kaisuu;
    }

    /**
     * 検査回数
     * @param kaisuu the kaisuu to set
     */
    public void setKaisuu(Integer kaisuu) {
        this.kaisuu = kaisuu;
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
     * 良品TPﾘｰﾙ巻数①
     * @return the ryouhintopreelmaki1
     */
    public Integer getRyouhintopreelmaki1() {
        return ryouhintopreelmaki1;
    }

    /**
     * 良品TPﾘｰﾙ巻数①
     * @param ryouhintopreelmaki1 the ryouhintopreelmaki1 to set
     */
    public void setRyouhintopreelmaki1(Integer ryouhintopreelmaki1) {
        this.ryouhintopreelmaki1 = ryouhintopreelmaki1;
    }

    /**
     * 良品TPﾘｰﾙ本数①
     * @return the ryouhintopreelhonsu1
     */
    public Integer getRyouhintopreelhonsu1() {
        return ryouhintopreelhonsu1;
    }

    /**
     * 良品TPﾘｰﾙ本数①
     * @param ryouhintopreelhonsu1 the ryouhintopreelhonsu1 to set
     */
    public void setRyouhintopreelhonsu1(Integer ryouhintopreelhonsu1) {
        this.ryouhintopreelhonsu1 = ryouhintopreelhonsu1;
    }

    /**
     * 良品TPﾘｰﾙ巻数②
     * @return the ryouhintopreelmaki2
     */
    public Integer getRyouhintopreelmaki2() {
        return ryouhintopreelmaki2;
    }

    /**
     * 良品TPﾘｰﾙ巻数②
     * @param ryouhintopreelmaki2 the ryouhintopreelmaki2 to set
     */
    public void setRyouhintopreelmaki2(Integer ryouhintopreelmaki2) {
        this.ryouhintopreelmaki2 = ryouhintopreelmaki2;
    }

    /**
     * 良品TPﾘｰﾙ本数②
     * @return the ryouhintopreelhonsu2
     */
    public Integer getRyouhintopreelhonsu2() {
        return ryouhintopreelhonsu2;
    }

    /**
     * 良品TPﾘｰﾙ本数②
     * @param ryouhintopreelhonsu2 the ryouhintopreelhonsu2 to set
     */
    public void setRyouhintopreelhonsu2(Integer ryouhintopreelhonsu2) {
        this.ryouhintopreelhonsu2 = ryouhintopreelhonsu2;
    }

    /**
     * ﾃｰﾋﾟﾝｸﾞ号機
     * @return the tapinggouki
     */
    public String getTapinggouki() {
        return tapinggouki;
    }

    /**
     * ﾃｰﾋﾟﾝｸﾞ号機
     * @param tapinggouki the tapinggouki to set
     */
    public void setTapinggouki(String tapinggouki) {
        this.tapinggouki = tapinggouki;
    }

    /**
     * 検査場所
     * @return the kensabasyo
     */
    public String getKensabasyo() {
        return kensabasyo;
    }

    /**
     * 検査場所
     * @param kensabasyo the kensabasyo to set
     */
    public void setKensabasyo(String kensabasyo) {
        this.kensabasyo = kensabasyo;
    }

    /**
     * ﾘｰﾙﾁｪｯｸ数
     * @return the reelchecksu
     */
    public Integer getReelchecksu() {
        return reelchecksu;
    }

    /**
     * ﾘｰﾙﾁｪｯｸ数
     * @param reelchecksu the reelchecksu to set
     */
    public void setReelchecksu(Integer reelchecksu) {
        this.reelchecksu = reelchecksu;
    }

    /**
     * 検査開始日時
     * @return the kensakaisinichiji
     */
    public Timestamp getKensakaisinichiji() {
        return kensakaisinichiji;
    }

    /**
     * 検査開始日時
     * @param kensakaisinichiji the kensakaisinichiji to set
     */
    public void setKensakaisinichiji(Timestamp kensakaisinichiji) {
        this.kensakaisinichiji = kensakaisinichiji;
    }

    /**
     * 検査開始担当者
     * @return the kensakaisitantou
     */
    public String getKensakaisitantou() {
        return kensakaisitantou;
    }

    /**
     * 検査開始担当者
     * @param kensakaisitantou the kensakaisitantou to set
     */
    public void setKensakaisitantou(String kensakaisitantou) {
        this.kensakaisitantou = kensakaisitantou;
    }

    /**
     * 物立ち
     * @return the monotati
     */
    public Integer getMonotati() {
        return monotati;
    }

    /**
     * 物立ち
     * @param monotati the monotati to set
     */
    public void setMonotati(Integer monotati) {
        this.monotati = monotati;
    }

    /**
     * 剥離
     * @return the hakuri
     */
    public Integer getHakuri() {
        return hakuri;
    }

    /**
     * 剥離
     * @param hakuri the hakuri to set
     */
    public void setHakuri(Integer hakuri) {
        this.hakuri = hakuri;
    }

    /**
     * 歯抜け
     * @return the hanuke
     */
    public Integer getHanuke() {
        return hanuke;
    }

    /**
     * 歯抜け
     * @param hanuke the hanuke
     */
    public void setHanuke(Integer hanuke) {
        this.hanuke = hanuke;
    }

    /**
     * 破れ
     * @return the rabure
     */
    public Integer getRabure() {
        return rabure;
    }

    /**
     * 破れ
     * @param rabure the rabure to set
     */
    public void setRabure(Integer rabure) {
        this.rabure = rabure;
    }

    /**
     * ｶｹNG
     * @return the kakeng
     */
    public Integer getKakeng() {
        return kakeng;
    }

    /**
     * ｶｹNG
     * @param kakeng the kakeng to set
     */
    public void setKakeng(Integer kakeng) {
        this.kakeng = kakeng;
    }

    /**
     * DIP不良
     * @return the dipfuryo
     */
    public Integer getDipfuryo() {
        return dipfuryo;
    }

    /**
     * DIP不良
     * @param dipfuryo the dipfuryo to set
     */
    public void setDipfuryo(Integer dipfuryo) {
        this.dipfuryo = dipfuryo;
    }

    /**
     * その他
     * @return the sonota
     */
    public Integer getSonota() {
        return sonota;
    }

    /**
     * その他
     * @param sonota the sonota to set
     */
    public void setSonota(Integer sonota) {
        this.sonota = sonota;
    }

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟ、ｷｬﾘｱﾃｰﾌﾟ、ﾎﾞﾄﾑﾃｰﾌﾟ異常
     * @return the tapeijyo
     */
    public String getTapeijyo() {
        return tapeijyo;
    }

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟ、ｷｬﾘｱﾃｰﾌﾟ、ﾎﾞﾄﾑﾃｰﾌﾟ異常
     * @param tapeijyo the tapeijyo to set
     */
    public void setTapeijyo(String tapeijyo) {
        this.tapeijyo = tapeijyo;
    }

    /**
     * ﾘｰﾙﾁｪｯｸ結果
     * @return the reelcheckkekka
     */
    public String getReelcheckkekka() {
        return reelcheckkekka;
    }

    /**
     * ﾘｰﾙﾁｪｯｸ結果
     * @param reelcheckkekka the reelcheckkekka to set
     */
    public void setReelcheckkekka(String reelcheckkekka) {
        this.reelcheckkekka = reelcheckkekka;
    }

    /**
     * 検査終了日時
     * @return the kensasyuryonichiji
     */
    public Timestamp getKensasyuryonichiji() {
        return kensasyuryonichiji;
    }

    /**
     * 検査終了日時
     * @param kensasyuryonichiji the kensasyuryonichiji to set
     */
    public void setKensasyuryonichiji(Timestamp kensasyuryonichiji) {
        this.kensasyuryonichiji = kensasyuryonichiji;
    }

    /**
     * 検査終了担当者
     * @return the kensasyuryotantou
     */
    public String getKensasyuryotantou() {
        return kensasyuryotantou;
    }

    /**
     * 検査終了担当者
     * @param kensasyuryotantou the kensasyuryotantou to set
     */
    public void setKensasyuryotantou(String kensasyuryotantou) {
        this.kensasyuryotantou = kensasyuryotantou;
    }

    /**
     * TPNG1
     * @return the tapeng1
     */
    public String getTapeng1() {
        return tapeng1;
    }

    /**
     * TPNG1
     * @param tapeng1 the tapeng1 to set
     */
    public void setTapeng1(String tapeng1) {
        this.tapeng1 = tapeng1;
    }

    /**
     * TPNG2
     * @return the tapeng2
     */
    public String getTapeng2() {
        return tapeng2;
    }

    /**
     * TPNG2
     * @param tapeng2 the tapeng2 to set
     */
    public void setTapeng2(String tapeng2) {
        this.tapeng2 = tapeng2;
    }

    /**
     * 電気特性再検査
     * @return the denkitokuseisaikensa
     */
    public String getDenkitokuseisaikensa() {
        return denkitokuseisaikensa;
    }

    /**
     * 電気特性再検査
     * @param denkitokuseisaikensa the denkitokuseisaikensa to set
     */
    public void setDenkitokuseisaikensa(String denkitokuseisaikensa) {
        this.denkitokuseisaikensa = denkitokuseisaikensa;
    }

    /**
     * 外観再検査
     * @return the gaikansaikensa
     */
    public String getGaikansaikensa() {
        return gaikansaikensa;
    }

    /**
     * 外観再検査
     * @param gaikansaikensa the gaikansaikensa to set
     */
    public void setGaikansaikensa(String gaikansaikensa) {
        this.gaikansaikensa = gaikansaikensa;
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
     * 実績No(検査回数)
     * @return the tpng1jissekino
     */
    public Integer getTpng1jissekino() {
        return tpng1jissekino;
    }

    /**
     * 実績No(検査回数)
     * @param tpng1jissekino the tpng1jissekino
     */
    public void setTpng1jissekino(Integer tpng1jissekino) {
        this.tpng1jissekino = tpng1jissekino;
    }

    /**
     * 検査日
     * @return the tpng1kensabi
     */
    public Timestamp getTpng1kensabi() {
        return tpng1kensabi;
    }

    /**
     * 検査日
     * @param tpng1kensabi the tpng1kensabi to set
     */
    public void setTpng1kensabi(Timestamp tpng1kensabi) {
        this.tpng1kensabi = tpng1kensabi;
    }

    /**
     * 検査者コード
     * @return the tpng1kensasya
     */
    public String getTpng1kensasya() {
        return tpng1kensasya;
    }

    /**
     * 検査者コード
     * @param tpng1kensasya the tpng1kensasya to set
     */
    public void setTpng1kensasya(String tpng1kensasya) {
        this.tpng1kensasya = tpng1kensasya;
    }

    /**
     * 検査区分(1=容量, 2=絶縁抵抗)
     * @return the tpng1kensakubun
     */
    public String getTpng1kensakubun() {
        return tpng1kensakubun;
    }

    /**
     * 検査区分(1=容量, 2=絶縁抵抗)
     * @param tpng1kensakubun the tpng1kensakubun to set
     */
    public void setTpng1kensakubun(String tpng1kensakubun) {
        this.tpng1kensakubun = tpng1kensakubun;
    }

    /**
     * TP個数(依頼数)
     * @return the tpng1tpkosuu
     */
    public Integer getTpng1tpkosuu() {
        return tpng1tpkosuu;
    }

    /**
     * TP個数(依頼数)
     * @param tpng1tpkosuu the tpng1tpkosuu to set
     */
    public void setTpng1tpkosuu(Integer tpng1tpkosuu) {
        this.tpng1tpkosuu = tpng1tpkosuu;
    }

    /**
     * NG1個数
     * @return the tpng1ng1kosuu
     */
    public Integer getTpng1ng1kosuu() {
        return tpng1ng1kosuu;
    }

    /**
     * NG1個数
     * @param tpng1ng1kosuu the tpng1ng1kosuu to set
     */
    public void setTpng1ng1kosuu(Integer tpng1ng1kosuu) {
        this.tpng1ng1kosuu = tpng1ng1kosuu;
    }

    /**
     * 検査抜取個数
     * @return the tpng1nukitorikosuu
     */
    public Integer getTpng1nukitorikosuu() {
        return tpng1nukitorikosuu;
    }

    /**
     * 検査抜取個数
     * @param tpng1nukitorikosuu the tpng1nukitorikosuu to set
     */
    public void setTpng1nukitorikosuu(Integer tpng1nukitorikosuu) {
        this.tpng1nukitorikosuu = tpng1nukitorikosuu;
    }

    /**
     * 容量/絶縁抵抗NG個数
     * @return the tpng1cap_ir_ngkosuu
     */
    public Integer getTpng1cap_ir_ngkosuu() {
        return tpng1cap_ir_ngkosuu;
    }

    /**
     * 容量/絶縁抵抗NG個数
     * @param tpng1cap_ir_ngkosuu the tpng1cap_ir_ngkosuu to set
     */
    public void setTpng1cap_ir_ngkosuu(Integer tpng1cap_ir_ngkosuu) {
        this.tpng1cap_ir_ngkosuu = tpng1cap_ir_ngkosuu;
    }

    /**
     * ショート個数
     * @return the tpng1shortkosuu
     */
    public Integer getTpng1shortkosuu() {
        return tpng1shortkosuu;
    }

    /**
     * ショート個数
     * @param tpng1shortkosuu the tpng1shortkosuu to set
     */
    public void setTpng1shortkosuu(Integer tpng1shortkosuu) {
        this.tpng1shortkosuu = tpng1shortkosuu;
    }

    /**
     * その他個数
     * @return the tpng1etckosuu
     */
    public Integer getTpng1etckosuu() {
        return tpng1etckosuu;
    }

    /**
     * その他個数
     * @param tpng1etckosuu the tpng1etckosuu to set
     */
    public void setTpng1etckosuu(Integer tpng1etckosuu) {
        this.tpng1etckosuu = tpng1etckosuu;
    }

    /**
     * 判定(1=OK, 2=NG)
     * @return the tpng1hantei
     */
    public String getTpng1hantei() {
        return tpng1hantei;
    }

    /**
     * 判定(1=OK, 2=NG)
     * @param tpng1hantei the tpng1hantei to set
     */
    public void setTpng1hantei(String tpng1hantei) {
        this.tpng1hantei = tpng1hantei;
    }

    /**
     * 備考1
     * @return the tpng1biko1
     */
    public String getTpng1biko1() {
        return tpng1biko1;
    }

    /**
     * 備考1
     * @param tpng1biko1 the tpng1biko1 to set
     */
    public void setTpng1biko1(String tpng1biko1) {
        this.tpng1biko1 = tpng1biko1;
    }

    /**
     * 備考2
     * @return the tpng1biko2
     */
    public String getTpng1biko2() {
        return tpng1biko2;
    }

    /**
     * 備考2
     * @param tpng1biko2 the tpng1biko2 to set
     */
    public void setTpng1biko2(String tpng1biko2) {
        this.tpng1biko2 = tpng1biko2;
    }

    /**
     * 実績No(検査回数)
     * @return the tpng2jissekino
     */
    public Integer getTpng2jissekino() {
        return tpng2jissekino;
    }

    /**
     * 実績No(検査回数)
     * @param tpng2jissekino the tpng2jissekino to set
     */
    public void setTpng2jissekino(Integer tpng2jissekino) {
        this.tpng2jissekino = tpng2jissekino;
    }

    /**
     * 検査日
     * @return the tpng2kensabi
     */
    public Timestamp getTpng2kensabi() {
        return tpng2kensabi;
    }

    /**
     * 検査日
     * @param tpng2kensabi the tpng2kensabi to set
     */
    public void setTpng2kensabi(Timestamp tpng2kensabi) {
        this.tpng2kensabi = tpng2kensabi;
    }

    /**
     * 検査者コード
     * @return the tpng2kensasya
     */
    public String getTpng2kensasya() {
        return tpng2kensasya;
    }

    /**
     * 検査者コード
     * @param tpng2kensasya the tpng2kensasya to set
     */
    public void setTpng2kensasya(String tpng2kensasya) {
        this.tpng2kensasya = tpng2kensasya;
    }

    /**
     * 検査区分(1=流れ品, 2=バラシ品)
     * @return the tpng2kensakubun
     */
    public String getTpng2kensakubun() {
        return tpng2kensakubun;
    }

    /**
     * 検査区分(1=流れ品, 2=バラシ品)
     * @param tpng2kensakubun the tpng2kensakubun to set
     */
    public void setTpng2kensakubun(String tpng2kensakubun) {
        this.tpng2kensakubun = tpng2kensakubun;
    }

    /**
     * TP個数(依頼数)
     * @return the tpng2tpkosuu
     */
    public Integer getTpng2tpkosuu() {
        return tpng2tpkosuu;
    }

    /**
     * TP個数(依頼数)
     * @param tpng2tpkosuu the tpng2tpkosuu to set
     */
    public void setTpng2tpkosuu(Integer tpng2tpkosuu) {
        this.tpng2tpkosuu = tpng2tpkosuu;
    }

    /**
     * NG2個数
     * @return the tpng2ng2kosuu
     */
    public Integer getTpng2ng2kosuu() {
        return tpng2ng2kosuu;
    }

    /**
     * NG2個数
     * @param tpng2ng2kosuu the tpng2ng2kosuu to set
     */
    public void setTpng2ng2kosuu(Integer tpng2ng2kosuu) {
        this.tpng2ng2kosuu = tpng2ng2kosuu;
    }

    /**
     * 検査抜取個数
     * @return the tpng2nukitorikosuu
     */
    public Integer getTpng2nukitorikosuu() {
        return tpng2nukitorikosuu;
    }

    /**
     * 検査抜取個数
     * @param tpng2nukitorikosuu the tpng2nukitorikosuu to set
     */
    public void setTpng2nukitorikosuu(Integer tpng2nukitorikosuu) {
        this.tpng2nukitorikosuu = tpng2nukitorikosuu;
    }

    /**
     * Aモード個数
     * @return the tpng2a_modekosuu
     */
    public Integer getTpng2a_modekosuu() {
        return tpng2a_modekosuu;
    }

    /**
     * Aモード個数
     * @param tpng2a_modekosuu the tpng2a_modekosuu to set
     */
    public void setTpng2a_modekosuu(Integer tpng2a_modekosuu) {
        this.tpng2a_modekosuu = tpng2a_modekosuu;
    }

    /**
     * Cモード個数
     * @return the tpng2c_modekosuu
     */
    public Integer getTpng2c_modekosuu() {
        return tpng2c_modekosuu;
    }

    /**
     * Cモード個数
     * @param tpng2c_modekosuu the tpng2c_modekosuu to set
     */
    public void setTpng2c_modekosuu(Integer tpng2c_modekosuu) {
        this.tpng2c_modekosuu = tpng2c_modekosuu;
    }

    /**
     * 判定(1=OK, 2=NG)
     * @return the tpng2hantei
     */
    public String getTpng2hantei() {
        return tpng2hantei;
    }

    /**
     * 判定(1=OK, 2=NG)
     * @param tpng2hantei the tpng2hantei to set
     */
    public void setTpng2hantei(String tpng2hantei) {
        this.tpng2hantei = tpng2hantei;
    }

    /**
     * 備考1
     * @return the tpng2biko1
     */
    public String getTpng2biko1() {
        return tpng2biko1;
    }

    /**
     * 備考1
     * @param tpng2biko1 the tpng2biko1 to set
     */
    public void setTpng2biko1(String tpng2biko1) {
        this.tpng2biko1 = tpng2biko1;
    }

    /**
     * 備考2
     * @return the tpng2biko2
     */
    public String getTpng2biko2() {
        return tpng2biko2;
    }

    /**
     * 備考2
     * @param tpng2biko2 the tpng2biko2 to set
     */
    public void setTpng2biko2(String tpng2biko2) {
        this.tpng2biko2 = tpng2biko2;
    }

    /**
     * Dモード個数
     * @return the tpng2d_modekosuu
     */
    public Integer getTpng2d_modekosuu() {
        return tpng2d_modekosuu;
    }

    /**
     * Dモード個数
     * @param tpng2d_modekosuu the tpng2d_modekosuu to set
     */
    public void setTpng2d_modekosuu(Integer tpng2d_modekosuu) {
        this.tpng2d_modekosuu = tpng2d_modekosuu;
    }

    /**
     * その他個数
     * @return the tpng2etckosuu
     */
    public Integer getTpng2etckosuu() {
        return tpng2etckosuu;
    }

    /**
     * その他個数
     * @param tpng2etckosuu the tpng2etckosuu to set
     */
    public void setTpng2etckosuu(Integer tpng2etckosuu) {
        this.tpng2etckosuu = tpng2etckosuu;
    }

    /**
     * 端子電極ハガレ個数
     * @return the tpng2dhagarekosuu 
     */
    public Integer getTpng2dhagarekosuu() {
        return tpng2dhagarekosuu;
    }

    /**
     * 端子電極ハガレ個数
     * @param tpng2dhagarekosuu the tpng2dhagarekosuu to set
     */
    public void setTpng2dhagarekosuu(Integer tpng2dhagarekosuu) {
        this.tpng2dhagarekosuu = tpng2dhagarekosuu;
    }

    /**
     * 端子電極キズ個数
     * @return the tpng2dkizukosuu 
     */
    public Integer getTpng2dkizukosuu() {
        return tpng2dkizukosuu;
    }

    /**
     * 端子電極キズ個数
     * @param tpng2dkizukosuu the tpng2dkizukosuu to set
     */
    public void setTpng2dkizukosuu(Integer tpng2dkizukosuu) {
        this.tpng2dkizukosuu = tpng2dkizukosuu;
    }

    /**
     * 磁器クラック個数
     * @return the tpng2jcrackkosuu 
     */
    public Integer getTpng2jcrackkosuu() {
        return tpng2jcrackkosuu;
    }

    /**
     * 磁器クラック個数
     * @param tpng2jcrackkosuu the tpng2jcrackkosuu to set
     */
    public void setTpng2jcrackkosuu(Integer tpng2jcrackkosuu) {
        this.tpng2jcrackkosuu = tpng2jcrackkosuu;
    }

    /**
     * めっきなし個数
     * @return the tpng2mekkinasikosuu 
     */
    public Integer getTpng2mekkinasikosuu() {
        return tpng2mekkinasikosuu;
    }

    /**
     * めっきなし個数
     * @param tpng2mekkinasikosuu the tpng2mekkinasikosuu to set
     */
    public void setTpng2mekkinasikosuu(Integer tpng2mekkinasikosuu) {
        this.tpng2mekkinasikosuu = tpng2mekkinasikosuu;
    }

    /**
     * めっき浮き個数
     * @return the tpng2mekkiukikosuu 
     */
    public Integer getTpng2mekkiukikosuu() {
        return tpng2mekkiukikosuu;
    }

    /**
     * めっき浮き個数
     * @param tpng2mekkiukikosuu the tpng2mekkiukikosuu to set
     */
    public void setTpng2mekkiukikosuu(Integer tpng2mekkiukikosuu) {
        this.tpng2mekkiukikosuu = tpng2mekkiukikosuu;
    }
    
    /**
     * 実績No(検査回数)
     * @return the tpng1jissekino2
     */
    public Integer getTpng1jissekino2() {
        return tpng1jissekino2;
    }

    /**
     * 実績No(検査回数)
     * @param tpng1jissekino2 the tpng1jissekino2
     */
    public void setTpng1jissekino2(Integer tpng1jissekino2) {
        this.tpng1jissekino2 = tpng1jissekino2;
    }

    /**
     * 検査日
     * @return the tpng1kensabi2
     */
    public Timestamp getTpng1kensabi2() {
        return tpng1kensabi2;
    }

    /**
     * 検査日
     * @param tpng1kensabi2 the tpng1kensabi2 to set
     */
    public void setTpng1kensabi2(Timestamp tpng1kensabi2) {
        this.tpng1kensabi2 = tpng1kensabi2;
    }

    /**
     * 検査者コード
     * @return the tpng1kensasya2
     */
    public String getTpng1kensasya2() {
        return tpng1kensasya2;
    }

    /**
     * 検査者コード
     * @param tpng1kensasya2 the tpng1kensasya2 to set
     */
    public void setTpng1kensasya2(String tpng1kensasya2) {
        this.tpng1kensasya2 = tpng1kensasya2;
    }

    /**
     * 検査区分(1=容量, 2=絶縁抵抗)
     * @return the tpng1kensakubun2
     */
    public String getTpng1kensakubun2() {
        return tpng1kensakubun2;
    }

    /**
     * 検査区分(1=容量, 2=絶縁抵抗)
     * @param tpng1kensakubun2 the tpng1kensakubun2 to set
     */
    public void setTpng1kensakubun2(String tpng1kensakubun2) {
        this.tpng1kensakubun2 = tpng1kensakubun2;
    }

    /**
     * TP個数(依頼数)
     * @return the tpng1tpkosuu2
     */
    public Integer getTpng1tpkosuu2() {
        return tpng1tpkosuu2;
    }

    /**
     * TP個数(依頼数)
     * @param tpng1tpkosuu2 the tpng1tpkosuu2 to set
     */
    public void setTpng1tpkosuu2(Integer tpng1tpkosuu2) {
        this.tpng1tpkosuu2 = tpng1tpkosuu2;
    }

    /**
     * NG1個数
     * @return the tpng1ng1kosuu2
     */
    public Integer getTpng1ng1kosuu2() {
        return tpng1ng1kosuu2;
    }

    /**
     * NG1個数
     * @param tpng1ng1kosuu2 the tpng1ng1kosuu2 to set
     */
    public void setTpng1ng1kosuu2(Integer tpng1ng1kosuu2) {
        this.tpng1ng1kosuu2 = tpng1ng1kosuu2;
    }

    /**
     * 検査抜取個数
     * @return the tpng1nukitorikosuu2
     */
    public Integer getTpng1nukitorikosuu2() {
        return tpng1nukitorikosuu2;
    }

    /**
     * 検査抜取個数
     * @param tpng1nukitorikosuu2 the tpng1nukitorikosuu2 to set
     */
    public void setTpng1nukitorikosuu2(Integer tpng1nukitorikosuu2) {
        this.tpng1nukitorikosuu2 = tpng1nukitorikosuu2;
    }

    /**
     * 容量/絶縁抵抗NG個数
     * @return the tpng1cap_ir_ngkosuu2
     */
    public Integer getTpng1cap_ir_ngkosuu2() {
        return tpng1cap_ir_ngkosuu2;
    }

    /**
     * 容量/絶縁抵抗NG個数
     * @param tpng1cap_ir_ngkosuu2 the tpng1cap_ir_ngkosuu2 to set
     */
    public void setTpng1cap_ir_ngkosuu2(Integer tpng1cap_ir_ngkosuu2) {
        this.tpng1cap_ir_ngkosuu2 = tpng1cap_ir_ngkosuu2;
    }

    /**
     * ショート個数
     * @return the tpng1shortkosuu2
     */
    public Integer getTpng1shortkosuu2() {
        return tpng1shortkosuu2;
    }

    /**
     * ショート個数
     * @param tpng1shortkosuu2 the tpng1shortkosuu2 to set
     */
    public void setTpng1shortkosuu2(Integer tpng1shortkosuu2) {
        this.tpng1shortkosuu2 = tpng1shortkosuu2;
    }

    /**
     * その他個数
     * @return the tpng1etckosuu2
     */
    public Integer getTpng1etckosuu2() {
        return tpng1etckosuu2;
    }

    /**
     * その他個数
     * @param tpng1etckosuu2 the tpng1etckosuu2 to set
     */
    public void setTpng1etckosuu2(Integer tpng1etckosuu2) {
        this.tpng1etckosuu2 = tpng1etckosuu2;
    }

    /**
     * 判定(1=OK, 2=NG)
     * @return the tpng1hantei2
     */
    public String getTpng1hantei2() {
        return tpng1hantei2;
    }

    /**
     * 判定(1=OK, 2=NG)
     * @param tpng1hantei2 the tpng1hantei2 to set
     */
    public void setTpng1hantei2(String tpng1hantei2) {
        this.tpng1hantei2 = tpng1hantei2;
    }

    /**
     * 備考1
     * @return the tpng1biko1_2
     */
    public String getTpng1biko1_2() {
        return tpng1biko1_2;
    }

    /**
     * 備考1
     * @param tpng1biko1_2 the tpng1biko1_2 to set
     */
    public void setTpng1biko1_2(String tpng1biko1_2) {
        this.tpng1biko1_2 = tpng1biko1_2;
    }

    /**
     * 備考2
     * @return the tpng1biko2_2
     */
    public String getTpng1biko2_2() {
        return tpng1biko2_2;
    }

    /**
     * 備考2
     * @param tpng1biko2_2 the tpng1biko2_2 to set
     */
    public void setTpng1biko2_2(String tpng1biko2_2) {
        this.tpng1biko2_2 = tpng1biko2_2;
    }

    /**
     * 実績No(検査回数)
     * @return the tpng2jissekino2
     */
    public Integer getTpng2jissekino2() {
        return tpng2jissekino2;
    }

    /**
     * 実績No(検査回数)
     * @param tpng2jissekino2 the tpng2jissekino2 to set
     */
    public void setTpng2jissekino2(Integer tpng2jissekino2) {
        this.tpng2jissekino2 = tpng2jissekino2;
    }

    /**
     * 検査日
     * @return the tpng2kensabi2
     */
    public Timestamp getTpng2kensabi2() {
        return tpng2kensabi2;
    }

    /**
     * 検査日
     * @param tpng2kensabi2 the tpng2kensabi2 to set
     */
    public void setTpng2kensabi2(Timestamp tpng2kensabi2) {
        this.tpng2kensabi2 = tpng2kensabi2;
    }

    /**
     * 検査者コード
     * @return the tpng2kensasya2
     */
    public String getTpng2kensasya2() {
        return tpng2kensasya2;
    }

    /**
     * 検査者コード
     * @param tpng2kensasya2 the tpng2kensasya2 to set
     */
    public void setTpng2kensasya2(String tpng2kensasya2) {
        this.tpng2kensasya2 = tpng2kensasya2;
    }

    /**
     * 検査区分(1=流れ品, 2=バラシ品)
     * @return the tpng2kensakubun2
     */
    public String getTpng2kensakubun2() {
        return tpng2kensakubun2;
    }

    /**
     * 検査区分(1=流れ品, 2=バラシ品)
     * @param tpng2kensakubun2 the tpng2kensakubun2 to set
     */
    public void setTpng2kensakubun2(String tpng2kensakubun2) {
        this.tpng2kensakubun2 = tpng2kensakubun2;
    }

    /**
     * TP個数(依頼数)
     * @return the tpng2tpkosuu2
     */
    public Integer getTpng2tpkosuu2() {
        return tpng2tpkosuu2;
    }

    /**
     * TP個数(依頼数)
     * @param tpng2tpkosuu2 the tpng2tpkosuu2 to set
     */
    public void setTpng2tpkosuu2(Integer tpng2tpkosuu2) {
        this.tpng2tpkosuu2 = tpng2tpkosuu2;
    }

    /**
     * NG2個数
     * @return the tpng2ng2kosuu2
     */
    public Integer getTpng2ng2kosuu2() {
        return tpng2ng2kosuu2;
    }

    /**
     * NG2個数
     * @param tpng2ng2kosuu2 the tpng2ng2kosuu2 to set
     */
    public void setTpng2ng2kosuu2(Integer tpng2ng2kosuu2) {
        this.tpng2ng2kosuu2 = tpng2ng2kosuu2;
    }

    /**
     * 検査抜取個数
     * @return the tpng2nukitorikosuu2
     */
    public Integer getTpng2nukitorikosuu2() {
        return tpng2nukitorikosuu2;
    }

    /**
     * 検査抜取個数
     * @param tpng2nukitorikosuu2 the tpng2nukitorikosuu2 to set
     */
    public void setTpng2nukitorikosuu2(Integer tpng2nukitorikosuu2) {
        this.tpng2nukitorikosuu2 = tpng2nukitorikosuu2;
    }

    /**
     * Aモード個数
     * @return the tpng2a_modekosuu2
     */
    public Integer getTpng2a_modekosuu2() {
        return tpng2a_modekosuu2;
    }

    /**
     * Aモード個数
     * @param tpng2a_modekosuu2 the tpng2a_modekosuu2 to set
     */
    public void setTpng2a_modekosuu2(Integer tpng2a_modekosuu2) {
        this.tpng2a_modekosuu2 = tpng2a_modekosuu2;
    }

    /**
     * Cモード個数
     * @return the tpng2c_modekosuu2
     */
    public Integer getTpng2c_modekosuu2() {
        return tpng2c_modekosuu2;
    }

    /**
     * Cモード個数
     * @param tpng2c_modekosuu2 the tpng2c_modekosuu2 to set
     */
    public void setTpng2c_modekosuu2(Integer tpng2c_modekosuu2) {
        this.tpng2c_modekosuu2 = tpng2c_modekosuu2;
    }

    /**
     * 判定(1=OK, 2=NG)
     * @return the tpng2hantei2
     */
    public String getTpng2hantei2() {
        return tpng2hantei2;
    }

    /**
     * 判定(1=OK, 2=NG)
     * @param tpng2hantei2 the tpng2hantei2 to set
     */
    public void setTpng2hantei2(String tpng2hantei2) {
        this.tpng2hantei2 = tpng2hantei2;
    }

    /**
     * 備考1
     * @return the tpng2biko1_2
     */
    public String getTpng2biko1_2() {
        return tpng2biko1_2;
    }

    /**
     * 備考1
     * @param tpng2biko1_2 the tpng2biko1_2 to set
     */
    public void setTpng2biko1_2(String tpng2biko1_2) {
        this.tpng2biko1_2 = tpng2biko1_2;
    }

    /**
     * 備考2
     * @return the tpng2biko2_2
     */
    public String getTpng2biko2_2() {
        return tpng2biko2_2;
    }

    /**
     * 備考2
     * @param tpng2biko2_2 the tpng2biko2_2 to set
     */
    public void setTpng2biko2_2(String tpng2biko2_2) {
        this.tpng2biko2_2 = tpng2biko2_2;
    }

    /**
     * Dモード個数
     * @return the tpng2d_modekosuu2
     */
    public Integer getTpng2d_modekosuu2() {
        return tpng2d_modekosuu2;
    }

    /**
     * Dモード個数
     * @param tpng2d_modekosuu2 the tpng2d_modekosuu2 to set
     */
    public void setTpng2d_modekosuu2(Integer tpng2d_modekosuu2) {
        this.tpng2d_modekosuu2 = tpng2d_modekosuu2;
    }

    /**
     * その他個数
     * @return the tpng2etckosuu2
     */
    public Integer getTpng2etckosuu2() {
        return tpng2etckosuu2;
    }

    /**
     * その他個数
     * @param tpng2etckosuu2 the tpng2etckosuu2 to set
     */
    public void setTpng2etckosuu2(Integer tpng2etckosuu2) {
        this.tpng2etckosuu2 = tpng2etckosuu2;
    }

    /**
     * 端子電極ハガレ個数
     * @return the tpng2dhagarekosuu2 
     */
    public Integer getTpng2dhagarekosuu2() {
        return tpng2dhagarekosuu2;
    }

    /**
     * 端子電極ハガレ個数
     * @param tpng2dhagarekosuu2 the tpng2dhagarekosuu2 to set
     */
    public void setTpng2dhagarekosuu2(Integer tpng2dhagarekosuu2) {
        this.tpng2dhagarekosuu2 = tpng2dhagarekosuu2;
    }

    /**
     * 端子電極キズ個数
     * @return the tpng2dkizukosuu2 
     */
    public Integer getTpng2dkizukosuu2() {
        return tpng2dkizukosuu2;
    }

    /**
     * 端子電極キズ個数
     * @param tpng2dkizukosuu2 the tpng2dkizukosuu2 to set
     */
    public void setTpng2dkizukosuu2(Integer tpng2dkizukosuu2) {
        this.tpng2dkizukosuu2 = tpng2dkizukosuu2;
    }

    /**
     * 磁器クラック個数
     * @return the tpng2jcrackkosuu2 
     */
    public Integer getTpng2jcrackkosuu2() {
        return tpng2jcrackkosuu2;
    }

    /**
     * 磁器クラック個数
     * @param tpng2jcrackkosuu2 the tpng2jcrackkosuu2 to set
     */
    public void setTpng2jcrackkosuu2(Integer tpng2jcrackkosuu2) {
        this.tpng2jcrackkosuu2 = tpng2jcrackkosuu2;
    }

    /**
     * めっきなし個数
     * @return the tpng2mekkinasikosuu2 
     */
    public Integer getTpng2mekkinasikosuu2() {
        return tpng2mekkinasikosuu2;
    }

    /**
     * めっきなし個数
     * @param tpng2mekkinasikosuu2 the tpng2mekkinasikosuu2 to set
     */
    public void setTpng2mekkinasikosuu2(Integer tpng2mekkinasikosuu2) {
        this.tpng2mekkinasikosuu2 = tpng2mekkinasikosuu2;
    }

    /**
     * めっき浮き個数
     * @return the tpng2mekkiukikosuu2 
     */
    public Integer getTpng2mekkiukikosuu2() {
        return tpng2mekkiukikosuu2;
    }

    /**
     * めっき浮き個数
     * @param tpng2mekkiukikosuu2 the tpng2mekkiukikosuu2 to set
     */
    public void setTpng2mekkiukikosuu2(Integer tpng2mekkiukikosuu2) {
        this.tpng2mekkiukikosuu2 = tpng2mekkiukikosuu2;
    }
    
    /**
     * 実績No(検査回数)
     * @return the tpng1jissekino3
     */
    public Integer getTpng1jissekino3() {
        return tpng1jissekino3;
    }

    /**
     * 実績No(検査回数)
     * @param tpng1jissekino3 the tpng1jissekino3
     */
    public void setTpng1jissekino3(Integer tpng1jissekino3) {
        this.tpng1jissekino3 = tpng1jissekino3;
    }

    /**
     * 検査日
     * @return the tpng1kensabi3
     */
    public Timestamp getTpng1kensabi3() {
        return tpng1kensabi3;
    }

    /**
     * 検査日
     * @param tpng1kensabi3 the tpng1kensabi3 to set
     */
    public void setTpng1kensabi3(Timestamp tpng1kensabi3) {
        this.tpng1kensabi3 = tpng1kensabi3;
    }

    /**
     * 検査者コード
     * @return the tpng1kensasya3
     */
    public String getTpng1kensasya3() {
        return tpng1kensasya3;
    }

    /**
     * 検査者コード
     * @param tpng1kensasya3 the tpng1kensasya3 to set
     */
    public void setTpng1kensasya3(String tpng1kensasya3) {
        this.tpng1kensasya3 = tpng1kensasya3;
    }

    /**
     * 検査区分(1=容量, 2=絶縁抵抗)
     * @return the tpng1kensakubun3
     */
    public String getTpng1kensakubun3() {
        return tpng1kensakubun3;
    }

    /**
     * 検査区分(1=容量, 2=絶縁抵抗)
     * @param tpng1kensakubun3 the tpng1kensakubun3 to set
     */
    public void setTpng1kensakubun3(String tpng1kensakubun3) {
        this.tpng1kensakubun3 = tpng1kensakubun3;
    }

    /**
     * TP個数(依頼数)
     * @return the tpng1tpkosuu3
     */
    public Integer getTpng1tpkosuu3() {
        return tpng1tpkosuu3;
    }

    /**
     * TP個数(依頼数)
     * @param tpng1tpkosuu3 the tpng1tpkosuu3 to set
     */
    public void setTpng1tpkosuu3(Integer tpng1tpkosuu3) {
        this.tpng1tpkosuu3 = tpng1tpkosuu3;
    }

    /**
     * NG1個数
     * @return the tpng1ng1kosuu3
     */
    public Integer getTpng1ng1kosuu3() {
        return tpng1ng1kosuu3;
    }

    /**
     * NG1個数
     * @param tpng1ng1kosuu3 the tpng1ng1kosuu3 to set
     */
    public void setTpng1ng1kosuu3(Integer tpng1ng1kosuu3) {
        this.tpng1ng1kosuu3 = tpng1ng1kosuu3;
    }

    /**
     * 検査抜取個数
     * @return the tpng1nukitorikosuu3
     */
    public Integer getTpng1nukitorikosuu3() {
        return tpng1nukitorikosuu3;
    }

    /**
     * 検査抜取個数
     * @param tpng1nukitorikosuu3 the tpng1nukitorikosuu3 to set
     */
    public void setTpng1nukitorikosuu3(Integer tpng1nukitorikosuu3) {
        this.tpng1nukitorikosuu3 = tpng1nukitorikosuu3;
    }

    /**
     * 容量/絶縁抵抗NG個数
     * @return the tpng1cap_ir_ngkosuu3
     */
    public Integer getTpng1cap_ir_ngkosuu3() {
        return tpng1cap_ir_ngkosuu3;
    }

    /**
     * 容量/絶縁抵抗NG個数
     * @param tpng1cap_ir_ngkosuu3 the tpng1cap_ir_ngkosuu3 to set
     */
    public void setTpng1cap_ir_ngkosuu3(Integer tpng1cap_ir_ngkosuu3) {
        this.tpng1cap_ir_ngkosuu3 = tpng1cap_ir_ngkosuu3;
    }

    /**
     * ショート個数
     * @return the tpng1shortkosuu3
     */
    public Integer getTpng1shortkosuu3() {
        return tpng1shortkosuu3;
    }

    /**
     * ショート個数
     * @param tpng1shortkosuu3 the tpng1shortkosuu3 to set
     */
    public void setTpng1shortkosuu3(Integer tpng1shortkosuu3) {
        this.tpng1shortkosuu3 = tpng1shortkosuu3;
    }

    /**
     * その他個数
     * @return the tpng1etckosuu3
     */
    public Integer getTpng1etckosuu3() {
        return tpng1etckosuu3;
    }

    /**
     * その他個数
     * @param tpng1etckosuu3 the tpng1etckosuu3 to set
     */
    public void setTpng1etckosuu3(Integer tpng1etckosuu3) {
        this.tpng1etckosuu3 = tpng1etckosuu3;
    }

    /**
     * 判定(1=OK, 2=NG)
     * @return the tpng1hantei3
     */
    public String getTpng1hantei3() {
        return tpng1hantei3;
    }

    /**
     * 判定(1=OK, 2=NG)
     * @param tpng1hantei3 the tpng1hantei3 to set
     */
    public void setTpng1hantei3(String tpng1hantei3) {
        this.tpng1hantei3 = tpng1hantei3;
    }

    /**
     * 備考1
     * @return the tpng1biko1_3
     */
    public String getTpng1biko1_3() {
        return tpng1biko1_3;
    }

    /**
     * 備考1
     * @param tpng1biko1_3 the tpng1biko1_3 to set
     */
    public void setTpng1biko1_3(String tpng1biko1_3) {
        this.tpng1biko1_3 = tpng1biko1_3;
    }

    /**
     * 備考2
     * @return the tpng1biko2_3
     */
    public String getTpng1biko2_3() {
        return tpng1biko2_3;
    }

    /**
     * 備考2
     * @param tpng1biko2_3 the tpng1biko2_3 to set
     */
    public void setTpng1biko2_3(String tpng1biko2_3) {
        this.tpng1biko2_3 = tpng1biko2_3;
    }

    /**
     * 実績No(検査回数)
     * @return the tpng2jissekino3
     */
    public Integer getTpng2jissekino3() {
        return tpng2jissekino3;
    }

    /**
     * 実績No(検査回数)
     * @param tpng2jissekino3 the tpng2jissekino3 to set
     */
    public void setTpng2jissekino3(Integer tpng2jissekino3) {
        this.tpng2jissekino3 = tpng2jissekino3;
    }

    /**
     * 検査日
     * @return the tpng2kensabi3
     */
    public Timestamp getTpng2kensabi3() {
        return tpng2kensabi3;
    }

    /**
     * 検査日
     * @param tpng2kensabi3 the tpng2kensabi3 to set
     */
    public void setTpng2kensabi3(Timestamp tpng2kensabi3) {
        this.tpng2kensabi3 = tpng2kensabi3;
    }

    /**
     * 検査者コード
     * @return the tpng2kensasya3
     */
    public String getTpng2kensasya3() {
        return tpng2kensasya3;
    }

    /**
     * 検査者コード
     * @param tpng2kensasya3 the tpng2kensasya3 to set
     */
    public void setTpng2kensasya3(String tpng2kensasya3) {
        this.tpng2kensasya3 = tpng2kensasya3;
    }

    /**
     * 検査区分(1=流れ品, 2=バラシ品)
     * @return the tpng2kensakubun3
     */
    public String getTpng2kensakubun3() {
        return tpng2kensakubun3;
    }

    /**
     * 検査区分(1=流れ品, 2=バラシ品)
     * @param tpng2kensakubun3 the tpng2kensakubun3 to set
     */
    public void setTpng2kensakubun3(String tpng2kensakubun3) {
        this.tpng2kensakubun3 = tpng2kensakubun3;
    }

    /**
     * TP個数(依頼数)
     * @return the tpng2tpkosuu3
     */
    public Integer getTpng2tpkosuu3() {
        return tpng2tpkosuu3;
    }

    /**
     * TP個数(依頼数)
     * @param tpng2tpkosuu3 the tpng2tpkosuu3 to set
     */
    public void setTpng2tpkosuu3(Integer tpng2tpkosuu3) {
        this.tpng2tpkosuu3 = tpng2tpkosuu3;
    }

    /**
     * NG2個数
     * @return the tpng2ng2kosuu3
     */
    public Integer getTpng2ng2kosuu3() {
        return tpng2ng2kosuu3;
    }

    /**
     * NG2個数
     * @param tpng2ng2kosuu3 the tpng2ng2kosuu3 to set
     */
    public void setTpng2ng2kosuu3(Integer tpng2ng2kosuu3) {
        this.tpng2ng2kosuu3 = tpng2ng2kosuu3;
    }

    /**
     * 検査抜取個数
     * @return the tpng2nukitorikosuu3
     */
    public Integer getTpng2nukitorikosuu3() {
        return tpng2nukitorikosuu3;
    }

    /**
     * 検査抜取個数
     * @param tpng2nukitorikosuu3 the tpng2nukitorikosuu3 to set
     */
    public void setTpng2nukitorikosuu3(Integer tpng2nukitorikosuu3) {
        this.tpng2nukitorikosuu3 = tpng2nukitorikosuu3;
    }

    /**
     * Aモード個数
     * @return the tpng2a_modekosuu3
     */
    public Integer getTpng2a_modekosuu3() {
        return tpng2a_modekosuu3;
    }

    /**
     * Aモード個数
     * @param tpng2a_modekosuu3 the tpng2a_modekosuu3 to set
     */
    public void setTpng2a_modekosuu3(Integer tpng2a_modekosuu3) {
        this.tpng2a_modekosuu3 = tpng2a_modekosuu3;
    }

    /**
     * Cモード個数
     * @return the tpng2c_modekosuu3
     */
    public Integer getTpng2c_modekosuu3() {
        return tpng2c_modekosuu3;
    }

    /**
     * Cモード個数
     * @param tpng2c_modekosuu3 the tpng2c_modekosuu3 to set
     */
    public void setTpng2c_modekosuu3(Integer tpng2c_modekosuu3) {
        this.tpng2c_modekosuu3 = tpng2c_modekosuu3;
    }

    /**
     * 判定(1=OK, 2=NG)
     * @return the tpng2hantei3
     */
    public String getTpng2hantei3() {
        return tpng2hantei3;
    }

    /**
     * 判定(1=OK, 2=NG)
     * @param tpng2hantei3 the tpng2hantei3 to set
     */
    public void setTpng2hantei3(String tpng2hantei3) {
        this.tpng2hantei3 = tpng2hantei3;
    }

    /**
     * 備考1
     * @return the tpng2biko1_3
     */
    public String getTpng2biko1_3() {
        return tpng2biko1_3;
    }

    /**
     * 備考1
     * @param tpng2biko1_3 the tpng2biko1_3 to set
     */
    public void setTpng2biko1_3(String tpng2biko1_3) {
        this.tpng2biko1_3 = tpng2biko1_3;
    }

    /**
     * 備考2
     * @return the tpng2biko2_3
     */
    public String getTpng2biko2_3() {
        return tpng2biko2_3;
    }

    /**
     * 備考2
     * @param tpng2biko2_3 the tpng2biko2_3 to set
     */
    public void setTpng2biko2_3(String tpng2biko2_3) {
        this.tpng2biko2_3 = tpng2biko2_3;
    }

    /**
     * Dモード個数
     * @return the tpng2d_modekosuu3
     */
    public Integer getTpng2d_modekosuu3() {
        return tpng2d_modekosuu3;
    }

    /**
     * Dモード個数
     * @param tpng2d_modekosuu3 the tpng2d_modekosuu3 to set
     */
    public void setTpng2d_modekosuu3(Integer tpng2d_modekosuu3) {
        this.tpng2d_modekosuu3 = tpng2d_modekosuu3;
    }

    /**
     * その他個数
     * @return the tpng2etckosuu3
     */
    public Integer getTpng2etckosuu3() {
        return tpng2etckosuu3;
    }

    /**
     * その他個数
     * @param tpng2etckosuu3 the tpng2etckosuu3 to set
     */
    public void setTpng2etckosuu3(Integer tpng2etckosuu3) {
        this.tpng2etckosuu3 = tpng2etckosuu3;
    }

    /**
     * 端子電極ハガレ個数
     * @return the tpng2dhagarekosuu3 
     */
    public Integer getTpng2dhagarekosuu3() {
        return tpng2dhagarekosuu3;
    }

    /**
     * 端子電極ハガレ個数
     * @param tpng2dhagarekosuu3 the tpng2dhagarekosuu3 to set
     */
    public void setTpng2dhagarekosuu3(Integer tpng2dhagarekosuu3) {
        this.tpng2dhagarekosuu3 = tpng2dhagarekosuu3;
    }

    /**
     * 端子電極キズ個数
     * @return the tpng2dkizukosuu3 
     */
    public Integer getTpng2dkizukosuu3() {
        return tpng2dkizukosuu3;
    }

    /**
     * 端子電極キズ個数
     * @param tpng2dkizukosuu3 the tpng2dkizukosuu3 to set
     */
    public void setTpng2dkizukosuu3(Integer tpng2dkizukosuu3) {
        this.tpng2dkizukosuu3 = tpng2dkizukosuu3;
    }

    /**
     * 磁器クラック個数
     * @return the tpng2jcrackkosuu3 
     */
    public Integer getTpng2jcrackkosuu3() {
        return tpng2jcrackkosuu3;
    }

    /**
     * 磁器クラック個数
     * @param tpng2jcrackkosuu3 the tpng2jcrackkosuu3 to set
     */
    public void setTpng2jcrackkosuu3(Integer tpng2jcrackkosuu3) {
        this.tpng2jcrackkosuu3 = tpng2jcrackkosuu3;
    }

    /**
     * めっきなし個数
     * @return the tpng2mekkinasikosuu3 
     */
    public Integer getTpng2mekkinasikosuu3() {
        return tpng2mekkinasikosuu3;
    }

    /**
     * めっきなし個数
     * @param tpng2mekkinasikosuu3 the tpng2mekkinasikosuu3 to set
     */
    public void setTpng2mekkinasikosuu3(Integer tpng2mekkinasikosuu3) {
        this.tpng2mekkinasikosuu3 = tpng2mekkinasikosuu3;
    }

    /**
     * めっき浮き個数
     * @return the tpng2mekkiukikosuu3 
     */
    public Integer getTpng2mekkiukikosuu3() {
        return tpng2mekkiukikosuu3;
    }

    /**
     * めっき浮き個数
     * @param tpng2mekkiukikosuu3 the tpng2mekkiukikosuu3 to set
     */
    public void setTpng2mekkiukikosuu3(Integer tpng2mekkiukikosuu3) {
        this.tpng2mekkiukikosuu3 = tpng2mekkiukikosuu3;
    }
   

}
