/*
 * Copyright 2020 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2020/02/05<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * テーピングチェックのモデルクラスです。
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2020/02/05
 */
public class SrTapingCheck {

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
     * オーナー
     */
    private String ownercode;

    /**
     * 良品TPﾘｰﾙ巻数①
     */
    private Integer ryouhintopreelmaki1;

    /**
     * 良品TPﾘｰﾙ本数①
     */
    private Integer ryouhintopreelhonsu1;

    /**
     * 良品TPﾘｰﾙ巻数②
     */
    private Integer ryouhintopreelmaki2;

    /**
     * 良品TPﾘｰﾙ本数②
     */
    private Integer ryouhintopreelhonsu2;

    /**
     * ﾃｰﾋﾟﾝｸﾞ号機
     */
    private String tapinggouki;

    /**
     * 検査場所
     */
    private String kensabasyo;

    /**
     * ﾘｰﾙﾁｪｯｸ数
     */
    private Integer reelchecksu;

    /**
     * 検査開始日時
     */
    private Timestamp kensakaisinichiji;

    /**
     * 検査開始担当者
     */
    private String kensakaisitantou;

    /**
     * 物立ち
     */
    private Integer monotati;

    /**
     * 剥離
     */
    private Integer hakuri;

    /**
     * 歯抜け
     */
    private Integer hanuke;

    /**
     * 破れ
     */
    private Integer rabure;

    /**
     * ｶｹNG
     */
    private Integer kakeng;

    /**
     * DIP不良
     */
    private Integer dipfuryo;

    /**
     * その他
     */
    private Integer sonota;

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟ、ｷｬﾘｱﾃｰﾌﾟ、ﾎﾞﾄﾑﾃｰﾌﾟ異常
     */
    private Integer tapeijyo;

    /**
     * ﾘｰﾙﾁｪｯｸ結果
     */
    private String reelcheckkekka;

    /**
     * 検査終了日時
     */
    private Timestamp kensasyuryonichiji;

    /**
     * 検査終了担当者
     */
    private String kensasyuryotantou;

    /**
     * TPNG１
     */
    private Integer tapeng1;

    /**
     * TPNG2
     */
    private Integer tapeng2;

    /**
     * 電気特性再検査
     */
    private Integer denkitokuseisaikensa;

    /**
     * 外観再検査
     */
    private Integer gaikansaikensa;

    /**
     * 備考1
     */
    private String bikou1;

    /**
     * 備考2
     */
    private String bikou2;

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
     * オーナー
     *
     * @return the ownercode
     */
    public String getOwnercode() {
        return ownercode;
    }

    /**
     * オーナー
     *
     * @param ownercode the ownercode to set
     */
    public void setOwnercode(String ownercode) {
        this.ownercode = ownercode;
    }

    /**
     * 良品TPﾘｰﾙ巻数①
     *
     * @return the ryouhintopreelmaki1
     */
    public Integer getRyouhintopreelmaki1() {
        return ryouhintopreelmaki1;
    }

    /**
     * 良品TPﾘｰﾙ巻数①
     *
     * @param ryouhintopreelmaki1 the ryouhintopreelmaki1 to set
     */
    public void setRyouhintopreelmaki1(Integer ryouhintopreelmaki1) {
        this.ryouhintopreelmaki1 = ryouhintopreelmaki1;
    }

    /**
     * 良品TPﾘｰﾙ本数①
     *
     * @return the ryouhintopreelhonsu1
     */
    public Integer getRyouhintopreelhonsu1() {
        return ryouhintopreelhonsu1;
    }

    /**
     * 良品TPﾘｰﾙ本数①
     *
     * @param ryouhintopreelhonsu1 the ryouhintopreelhonsu1 to set
     */
    public void setRyouhintopreelhonsu1(Integer ryouhintopreelhonsu1) {
        this.ryouhintopreelhonsu1 = ryouhintopreelhonsu1;
    }

    /**
     * 良品TPﾘｰﾙ巻数②
     *
     * @return the ryouhintopreelmaki2
     */
    public Integer getRyouhintopreelmaki2() {
        return ryouhintopreelmaki2;
    }

    /**
     * 良品TPﾘｰﾙ巻数②
     *
     * @param ryouhintopreelmaki2 the ryouhintopreelmaki2 to set
     */
    public void setRyouhintopreelmaki2(Integer ryouhintopreelmaki2) {
        this.ryouhintopreelmaki2 = ryouhintopreelmaki2;
    }

    /**
     * 良品TPﾘｰﾙ本数②
     *
     * @return the ryouhintopreelhonsu2
     */
    public Integer getRyouhintopreelhonsu2() {
        return ryouhintopreelhonsu2;
    }

    /**
     * 良品TPﾘｰﾙ本数②
     *
     * @param ryouhintopreelhonsu2 the ryouhintopreelhonsu2 to set
     */
    public void setRyouhintopreelhonsu2(Integer ryouhintopreelhonsu2) {
        this.ryouhintopreelhonsu2 = ryouhintopreelhonsu2;
    }

    /**
     * ﾃｰﾋﾟﾝｸﾞ号機
     *
     * @return the tapinggouki
     */
    public String getTapinggouki() {
        return tapinggouki;
    }

    /**
     * ﾃｰﾋﾟﾝｸﾞ号機
     *
     * @param tapinggouki the tapinggouki to set
     */
    public void setTapinggouki(String tapinggouki) {
        this.tapinggouki = tapinggouki;
    }

    /**
     * 検査場所
     *
     * @return the kensabasyo
     */
    public String getKensabasyo() {
        return kensabasyo;
    }

    /**
     * 検査場所
     *
     * @param kensabasyo the kensabasyo to set
     */
    public void setKensabasyo(String kensabasyo) {
        this.kensabasyo = kensabasyo;
    }

    /**
     * ﾘｰﾙﾁｪｯｸ数
     *
     * @return the reelchecksu
     */
    public Integer getReelchecksu() {
        return reelchecksu;
    }

    /**
     * ﾘｰﾙﾁｪｯｸ数
     *
     * @param reelchecksu the reelchecksu to set
     */
    public void setReelchecksu(Integer reelchecksu) {
        this.reelchecksu = reelchecksu;
    }

    /**
     * 検査開始日時
     *
     * @return the kensakaisinichiji
     */
    public Timestamp getKensakaisinichiji() {
        return kensakaisinichiji;
    }

    /**
     * 検査開始日時
     *
     * @param kensakaisinichiji the kensakaisinichiji to set
     */
    public void setKensakaisinichiji(Timestamp kensakaisinichiji) {
        this.kensakaisinichiji = kensakaisinichiji;
    }

    /**
     * 検査開始担当者
     *
     * @return the kensakaisitantou
     */
    public String getKensakaisitantou() {
        return kensakaisitantou;
    }

    /**
     * 検査開始担当者
     *
     * @param kensakaisitantou the kensakaisitantou to set
     */
    public void setKensakaisitantou(String kensakaisitantou) {
        this.kensakaisitantou = kensakaisitantou;
    }

    /**
     * 物立ち
     *
     * @return the monotati
     */
    public Integer getMonotati() {
        return monotati;
    }

    /**
     * 物立ち
     *
     * @param monotati the monotati to set
     */
    public void setMonotati(Integer monotati) {
        this.monotati = monotati;
    }

    /**
     * 剥離
     *
     * @return the hakuri
     */
    public Integer getHakuri() {
        return hakuri;
    }

    /**
     * 剥離
     *
     * @param hakuri the hakuri to set
     */
    public void setHakuri(Integer hakuri) {
        this.hakuri = hakuri;
    }

    /**
     * 歯抜け
     *
     * @return the hanuke
     */
    public Integer getHanuke() {
        return hanuke;
    }

    /**
     * 歯抜け
     *
     * @param hanuke the hanuke to set
     */
    public void setHanuke(Integer hanuke) {
        this.hanuke = hanuke;
    }

    /**
     * 破れ
     *
     * @return the rabure
     */
    public Integer getRabure() {
        return rabure;
    }

    /**
     * 破れ
     *
     * @param rabure the rabure to set
     */
    public void setRabure(Integer rabure) {
        this.rabure = rabure;
    }

    /**
     * ｶｹNG
     *
     * @return the kakeng
     */
    public Integer getKakeng() {
        return kakeng;
    }

    /**
     * ｶｹNG
     *
     * @param kakeng the kakeng to set
     */
    public void setKakeng(Integer kakeng) {
        this.kakeng = kakeng;
    }

    /**
     * DIP不良
     *
     * @return the dipfuryo
     */
    public Integer getDipfuryo() {
        return dipfuryo;
    }

    /**
     * DIP不良
     *
     * @param dipfuryo the dipfuryo to set
     */
    public void setDipfuryo(Integer dipfuryo) {
        this.dipfuryo = dipfuryo;
    }

    /**
     * その他
     *
     * @return the sonota
     */
    public Integer getSonota() {
        return sonota;
    }

    /**
     * その他
     *
     * @param sonota the sonota to set
     */
    public void setSonota(Integer sonota) {
        this.sonota = sonota;
    }

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟ、ｷｬﾘｱﾃｰﾌﾟ、ﾎﾞﾄﾑﾃｰﾌﾟ異常
     *
     * @return the tapeijyo
     */
    public Integer getTapeijyo() {
        return tapeijyo;
    }

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟ、ｷｬﾘｱﾃｰﾌﾟ、ﾎﾞﾄﾑﾃｰﾌﾟ異常
     *
     * @param tapeijyo the tapeijyo to set
     */
    public void setTapeijyo(Integer tapeijyo) {
        this.tapeijyo = tapeijyo;
    }

    /**
     * ﾘｰﾙﾁｪｯｸ結果
     *
     * @return the reelcheckkekka
     */
    public String getReelcheckkekka() {
        return reelcheckkekka;
    }

    /**
     * ﾘｰﾙﾁｪｯｸ結果
     *
     * @param reelcheckkekka the reelcheckkekka to set
     */
    public void setReelcheckkekka(String reelcheckkekka) {
        this.reelcheckkekka = reelcheckkekka;
    }

    /**
     * 検査終了日時
     *
     * @return the kensasyuryonichiji
     */
    public Timestamp getKensasyuryonichiji() {
        return kensasyuryonichiji;
    }

    /**
     * 検査終了日時
     *
     * @param kensasyuryonichiji the kensasyuryonichiji to set
     */
    public void setKensasyuryonichiji(Timestamp kensasyuryonichiji) {
        this.kensasyuryonichiji = kensasyuryonichiji;
    }

    /**
     * 検査終了担当者
     *
     * @return the kensasyuryotantou
     */
    public String getKensasyuryotantou() {
        return kensasyuryotantou;
    }

    /**
     * 検査終了担当者
     *
     * @param kensasyuryotantou the kensasyuryotantou to set
     */
    public void setKensasyuryotantou(String kensasyuryotantou) {
        this.kensasyuryotantou = kensasyuryotantou;
    }

    /**
     * TPNG１
     *
     * @return the tapeng1
     */
    public Integer getTapeng1() {
        return tapeng1;
    }

    /**
     * TPNG１
     *
     * @param tapeng1 the tapeng1 to set
     */
    public void setTapeng1(Integer tapeng1) {
        this.tapeng1 = tapeng1;
    }

    /**
     * TPNG2
     *
     * @return the tapeng2
     */
    public Integer getTapeng2() {
        return tapeng2;
    }

    /**
     * TPNG2
     *
     * @param tapeng2 the tapeng2 to set
     */
    public void setTapeng2(Integer tapeng2) {
        this.tapeng2 = tapeng2;
    }

    /**
     * 電気特性再検査
     *
     * @return the denkitokuseisaikensa
     */
    public Integer getDenkitokuseisaikensa() {
        return denkitokuseisaikensa;
    }

    /**
     * 電気特性再検査
     *
     * @param denkitokuseisaikensa the denkitokuseisaikensa to set
     */
    public void setDenkitokuseisaikensa(Integer denkitokuseisaikensa) {
        this.denkitokuseisaikensa = denkitokuseisaikensa;
    }

    /**
     * 外観再検査
     *
     * @return the gaikansaikensa
     */
    public Integer getGaikansaikensa() {
        return gaikansaikensa;
    }

    /**
     * 外観再検査
     *
     * @param gaikansaikensa the gaikansaikensa to set
     */
    public void setGaikansaikensa(Integer gaikansaikensa) {
        this.gaikansaikensa = gaikansaikensa;
    }

    /**
     * 備考1
     *
     * @return the bikou1
     */
    public String getBikou1() {
        return bikou1;
    }

    /**
     * 備考1
     *
     * @param bikou1 the bikou1 to set
     */
    public void setBikou1(String bikou1) {
        this.bikou1 = bikou1;
    }

    /**
     * 備考2
     *
     * @return the bikou2
     */
    public String getBikou2() {
        return bikou2;
    }

    /**
     * 備考2
     *
     * @param bikou2 the bikou2 to set
     */
    public void setBikou2(String bikou2) {
        this.bikou2 = bikou2;
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

}
