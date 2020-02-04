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
 * 変更日	2020/02/03<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * テーピング作業のモデルクラスです。
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2020/02/03
 */
public class SrTapingSagyo {

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
     * 送り良品数
     */
    private Integer okuriryouhinsuu;

    /**
     * 受入れ単位重量
     */
    private BigDecimal ukeiretannijyuryo;

    /**
     * 受入れ総重量
     */
    private BigDecimal ukeiresoujyuryou;

    /**
     * 検査場所
     */
    private String kensabasyo;

    /**
     * 号機
     */
    private String gouki;

    /**
     * TP仕様
     */
    private String tpsiyou;

    /**
     * 確保数
     */
    private Integer kakuhosu;

    /**
     * 確保ﾘｰﾙ巻数①
     */
    private Integer kakuhoreelmaki1;

    /**
     * 確保ﾘｰﾙ本数①
     */
    private Integer kakuhoreelhonsu1;

    /**
     * 確保ﾘｰﾙ巻数②
     */
    private Integer kakuhoreelmaki2;

    /**
     * 確保ﾘｰﾙ本数②
     */
    private Integer kakuhoreelhonsu2;

    /**
     * ｷｬﾘｱﾃｰﾌﾟLOT NO.①
     */
    private String tapelotno1;

    /**
     * ｷｬﾘｱﾃｰﾌﾟLOT NO.②
     */
    private String tapelotno2;

    /**
     * ｷｬﾘｱﾃｰﾌﾟLOT NO.③
     */
    private String tapelotno3;

    /**
     * ｷｬﾘｱﾃｰﾌﾟLOT NO.④
     */
    private String tapelotno4;

    /**
     * ｷｬﾘｱﾃｰﾌﾟLOT NO.⑤
     */
    private String tapelotno5;

    /**
     * ｷｬﾘｱﾃｰﾌﾟLOT NO.⑥
     */
    private String tapelotno6;

    /**
     * ｷｬﾘｱﾃｰﾌﾟLOT NO.⑦
     */
    private String tapelotno7;

    /**
     * ｷｬﾘｱﾃｰﾌﾟLOT NO.⑧
     */
    private String tapelotno8;

    /**
     * ｷｬﾘｱﾃｰﾌﾟLOT NO.⑨
     */
    private String tapelotno9;

    /**
     * ｷｬﾘｱﾃｰﾌﾟLOT NO.⑩
     */
    private String tapelotno10;

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟLOT NO.①
     */
    private String toptapelotno1;

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟLOT NO.②
     */
    private String toptapelotno2;

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟLOT NO.③
     */
    private String toptapelotno3;

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟLOT NO.④
     */
    private String toptapelotno4;

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑤
     */
    private String toptapelotno5;

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑥
     */
    private String toptapelotno6;

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑦
     */
    private String toptapelotno7;

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑧
     */
    private String toptapelotno8;

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑨
     */
    private String toptapelotno9;

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑩
     */
    private String toptapelotno10;

    /**
     * ﾎﾞﾄﾑﾃｰﾌﾟLOT NO.①
     */
    private String bottomtapelot1;

    /**
     * ﾎﾞﾄﾑﾃｰﾌﾟLOT NO.②
     */
    private String bottomtapelot2;

    /**
     * ﾎﾞﾄﾑﾃｰﾌﾟLOT NO.③
     */
    private String bottomtapelot3;

    /**
     * 容量範囲ｾｯﾄ値　Hi
     */
    private BigDecimal yoryohannihi;

    /**
     * 容量範囲ｾｯﾄ値　Lo
     */
    private BigDecimal yoryohannilo;

    /**
     * 容量範囲ｾｯﾄ値単位
     */
    private String yoryohannitanni;

    /**
     * ﾚﾝｼﾞ表示
     */
    private Integer rangehyoji;

    /**
     * ﾚﾝｼﾞ表示単位
     */
    private String rangehyojitanni;

    /**
     * 容量値
     */
    private BigDecimal youryou;

    /**
     * 容量値単位
     */
    private String youryoutanni;

    /**
     * DFｾｯﾄ値　Hi
     */
    private BigDecimal dfsethi;

    /**
     * DFｾｯﾄ値　Lo
     */
    private BigDecimal dfsetlo;

    /**
     * DF値
     */
    private BigDecimal dfatai;

    /**
     * 画像設定条件
     */
    private String gazousetteijyoken;

    /**
     * ﾎｯﾊﾟｰﾈｼﾞ確認
     */
    private String hoperneji;

    /**
     * ｺﾃ清掃
     */
    private String koteseisou;

    /**
     * 開始前に製品残なき事
     */
    private String kaisimaesehinzannasi;

    /**
     * SET者
     */
    private String setsya;

    /**
     * Wﾁｪｯｸ者
     */
    private String kakuninsya;

    /**
     * 開始日時
     */
    private Timestamp kaisinichiji;

    /**
     * 試験担当者
     */
    private String sikentantou;

    /**
     * 自重落下試験
     */
    private String jijyurakkasiken;

    /**
     * ﾊﾞﾗｼﾁｪｯｸ
     */
    private String barasicheck;

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟ確認
     */
    private String toptapekakunin;

    /**
     * 挿入数
     */
    private Integer sounyuusu;

    /**
     * 投入数
     */
    private Integer tounyuusu;

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
     * 良品数
     */
    private Integer ryouhinsu;

    /**
     * 容量NG(NG1)
     */
    private Integer youryong1;

    /**
     * 画像NG(上画像数):NG2
     */
    private Integer gazoungue2;

    /**
     * 画像NG(下画像数):NG2
     */
    private Integer gazoungsita2;

    /**
     * 歩留まり
     */
    private BigDecimal budomari;

    /**
     * 終了日時
     */
    private Timestamp shuryonichiji;

    /**
     * ﾒﾝﾃﾅﾝｽ回数
     */
    private Integer mentekaisu;

    /**
     * ﾒﾝﾃ後TP外観
     */
    private String mentegotpgaikan;

    /**
     * ﾊﾞﾗｼ対象ﾘｰﾙ数
     */
    private Integer barasitaisyoreelsu;

    /**
     * 空ﾘｰﾙ数
     */
    private Integer akireelsu;

    /**
     * 良品ﾘｰﾙ数
     */
    private Integer ryouhinreelsu;

    /**
     * QA確認依頼ﾘｰﾙ数
     */
    private Integer qakakuniniraireelsu;

    /**
     * ﾘｰﾙ数ﾁｪｯｸ
     */
    private String reelsucheck;

    /**
     * TP後清掃：ﾎｯﾊﾟｰ部
     */
    private String tpatohopper;

    /**
     * TP後清掃：ﾌｨｰﾀﾞ部
     */
    private String tpatofeeder;

    /**
     * TP後清掃：INDEX内
     */
    private String tpatoindex;

    /**
     * TP後清掃：NGBOX内
     */
    private String tpatongbox;

    /**
     * 清掃担当者
     */
    private String seisoutantou;

    /**
     * 清掃確認者
     */
    private String seisoukakunin;

    /**
     * ﾊﾞﾗｼ依頼ﾘｰﾙ数
     */
    private Integer barasiiraireelsu;

    /**
     * ﾊﾞﾗｼ開始日時
     */
    private Timestamp barasikaisinichiji;

    /**
     * ﾊﾞﾗｼ終了日時
     */
    private Timestamp barasiksyuryonichiji;

    /**
     * ﾊﾞﾗｼ担当者
     */
    private String barasitantou;

    /**
     * 脱磁依頼ﾘｰﾙ数
     */
    private Integer datujiiraireelsu;

    /**
     * 脱磁開始日時
     */
    private Timestamp datujikaisinichiji;

    /**
     * 脱磁担当者
     */
    private String datujitantou;

    /**
     * 確保ﾘｰﾙ数2
     */
    private Integer kakuhoreelsu;

    /**
     * 空ﾘｰﾙ数2
     */
    private Integer akireelsu2;

    /**
     * 良品ﾘｰﾙ数2
     */
    private Integer ryouhinreelsu2;

    /**
     * QA確認依頼ﾘｰﾙ数2
     */
    private Integer qakakuniniraireelsu2;

    /**
     * 最終確認担当者
     */
    private String saisyukakuninn;

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
     * 送り良品数
     *
     * @return the okuriryouhinsuu
     */
    public Integer getOkuriryouhinsuu() {
        return okuriryouhinsuu;
    }

    /**
     * 送り良品数
     *
     * @param okuriryouhinsuu the okuriryouhinsuu to set
     */
    public void setOkuriryouhinsuu(Integer okuriryouhinsuu) {
        this.okuriryouhinsuu = okuriryouhinsuu;
    }

    /**
     * 受入れ単位重量
     *
     * @return the ukeiretannijyuryo
     */
    public BigDecimal getUkeiretannijyuryo() {
        return ukeiretannijyuryo;
    }

    /**
     * 受入れ単位重量
     *
     * @param ukeiretannijyuryo the ukeiretannijyuryo to set
     */
    public void setUkeiretannijyuryo(BigDecimal ukeiretannijyuryo) {
        this.ukeiretannijyuryo = ukeiretannijyuryo;
    }

    /**
     * 受入れ総重量
     *
     * @return the ukeiresoujyuryou
     */
    public BigDecimal getUkeiresoujyuryou() {
        return ukeiresoujyuryou;
    }

    /**
     * 受入れ総重量
     *
     * @param ukeiresoujyuryou the ukeiresoujyuryou to set
     */
    public void setUkeiresoujyuryou(BigDecimal ukeiresoujyuryou) {
        this.ukeiresoujyuryou = ukeiresoujyuryou;
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
     * 号機
     *
     * @return the gouki
     */
    public String getGouki() {
        return gouki;
    }

    /**
     * 号機
     *
     * @param gouki the gouki to set
     */
    public void setGouki(String gouki) {
        this.gouki = gouki;
    }

    /**
     * TP仕様
     *
     * @return the tpsiyou
     */
    public String getTpsiyou() {
        return tpsiyou;
    }

    /**
     * TP仕様
     *
     * @param tpsiyou the tpsiyou to set
     */
    public void setTpsiyou(String tpsiyou) {
        this.tpsiyou = tpsiyou;
    }

    /**
     * 確保数
     *
     * @return the kakuhosu
     */
    public Integer getKakuhosu() {
        return kakuhosu;
    }

    /**
     * 確保数
     *
     * @param kakuhosu the kakuhosu to set
     */
    public void setKakuhosu(Integer kakuhosu) {
        this.kakuhosu = kakuhosu;
    }

    /**
     * 確保ﾘｰﾙ巻数①
     *
     * @return the kakuhoreelmaki1
     */
    public Integer getKakuhoreelmaki1() {
        return kakuhoreelmaki1;
    }

    /**
     * 確保ﾘｰﾙ巻数①
     *
     * @param kakuhoreelmaki1 the kakuhoreelmaki1 to set
     */
    public void setKakuhoreelmaki1(Integer kakuhoreelmaki1) {
        this.kakuhoreelmaki1 = kakuhoreelmaki1;
    }

    /**
     * 確保ﾘｰﾙ本数①
     *
     * @return the kakuhoreelhonsu1
     */
    public Integer getKakuhoreelhonsu1() {
        return kakuhoreelhonsu1;
    }

    /**
     * 確保ﾘｰﾙ本数①
     *
     * @param kakuhoreelhonsu1 the kakuhoreelhonsu1 to set
     */
    public void setKakuhoreelhonsu1(Integer kakuhoreelhonsu1) {
        this.kakuhoreelhonsu1 = kakuhoreelhonsu1;
    }

    /**
     * 確保ﾘｰﾙ巻数②
     *
     * @return the kakuhoreelmaki2
     */
    public Integer getKakuhoreelmaki2() {
        return kakuhoreelmaki2;
    }

    /**
     * 確保ﾘｰﾙ巻数②
     *
     * @param kakuhoreelmaki2 the kakuhoreelmaki2 to set
     */
    public void setKakuhoreelmaki2(Integer kakuhoreelmaki2) {
        this.kakuhoreelmaki2 = kakuhoreelmaki2;
    }

    /**
     * 確保ﾘｰﾙ本数②
     *
     * @return the kakuhoreelhonsu2
     */
    public Integer getKakuhoreelhonsu2() {
        return kakuhoreelhonsu2;
    }

    /**
     * 確保ﾘｰﾙ本数②
     *
     * @param kakuhoreelhonsu2 the kakuhoreelhonsu2 to set
     */
    public void setKakuhoreelhonsu2(Integer kakuhoreelhonsu2) {
        this.kakuhoreelhonsu2 = kakuhoreelhonsu2;
    }

    /**
     * ｷｬﾘｱﾃｰﾌﾟLOT NO.①
     *
     * @return the tapelotno1
     */
    public String getTapelotno1() {
        return tapelotno1;
    }

    /**
     * ｷｬﾘｱﾃｰﾌﾟLOT NO.①
     *
     * @param tapelotno1 the tapelotno1 to set
     */
    public void setTapelotno1(String tapelotno1) {
        this.tapelotno1 = tapelotno1;
    }

    /**
     * ｷｬﾘｱﾃｰﾌﾟLOT NO.②
     *
     * @return the tapelotno2
     */
    public String getTapelotno2() {
        return tapelotno2;
    }

    /**
     * ｷｬﾘｱﾃｰﾌﾟLOT NO.②
     *
     * @param tapelotno2 the tapelotno2 to set
     */
    public void setTapelotno2(String tapelotno2) {
        this.tapelotno2 = tapelotno2;
    }

    /**
     * ｷｬﾘｱﾃｰﾌﾟLOT NO.③
     *
     * @return the tapelotno3
     */
    public String getTapelotno3() {
        return tapelotno3;
    }

    /**
     * ｷｬﾘｱﾃｰﾌﾟLOT NO.③
     *
     * @param tapelotno3 the tapelotno3 to set
     */
    public void setTapelotno3(String tapelotno3) {
        this.tapelotno3 = tapelotno3;
    }

    /**
     * ｷｬﾘｱﾃｰﾌﾟLOT NO.④
     *
     * @return the tapelotno4
     */
    public String getTapelotno4() {
        return tapelotno4;
    }

    /**
     * ｷｬﾘｱﾃｰﾌﾟLOT NO.④
     *
     * @param tapelotno4 the tapelotno4 to set
     */
    public void setTapelotno4(String tapelotno4) {
        this.tapelotno4 = tapelotno4;
    }

    /**
     * ｷｬﾘｱﾃｰﾌﾟLOT NO.⑤
     *
     * @return the tapelotno5
     */
    public String getTapelotno5() {
        return tapelotno5;
    }

    /**
     * ｷｬﾘｱﾃｰﾌﾟLOT NO.⑤
     *
     * @param tapelotno5 the tapelotno5 to set
     */
    public void setTapelotno5(String tapelotno5) {
        this.tapelotno5 = tapelotno5;
    }

    /**
     * ｷｬﾘｱﾃｰﾌﾟLOT NO.⑥
     *
     * @return the tapelotno6
     */
    public String getTapelotno6() {
        return tapelotno6;
    }

    /**
     * ｷｬﾘｱﾃｰﾌﾟLOT NO.⑥
     *
     * @param tapelotno6 the tapelotno6 to set
     */
    public void setTapelotno6(String tapelotno6) {
        this.tapelotno6 = tapelotno6;
    }

    /**
     * ｷｬﾘｱﾃｰﾌﾟLOT NO.⑦
     *
     * @return the tapelotno7
     */
    public String getTapelotno7() {
        return tapelotno7;
    }

    /**
     * ｷｬﾘｱﾃｰﾌﾟLOT NO.⑦
     *
     * @param tapelotno7 the tapelotno7 to set
     */
    public void setTapelotno7(String tapelotno7) {
        this.tapelotno7 = tapelotno7;
    }

    /**
     * ｷｬﾘｱﾃｰﾌﾟLOT NO.⑧
     *
     * @return the tapelotno8
     */
    public String getTapelotno8() {
        return tapelotno8;
    }

    /**
     * ｷｬﾘｱﾃｰﾌﾟLOT NO.⑧
     *
     * @param tapelotno8 the tapelotno8 to set
     */
    public void setTapelotno8(String tapelotno8) {
        this.tapelotno8 = tapelotno8;
    }

    /**
     * ｷｬﾘｱﾃｰﾌﾟLOT NO.⑨
     *
     * @return the tapelotno9
     */
    public String getTapelotno9() {
        return tapelotno9;
    }

    /**
     * ｷｬﾘｱﾃｰﾌﾟLOT NO.⑨
     *
     * @param tapelotno9 the tapelotno9 to set
     */
    public void setTapelotno9(String tapelotno9) {
        this.tapelotno9 = tapelotno9;
    }

    /**
     * ｷｬﾘｱﾃｰﾌﾟLOT NO.⑩
     *
     * @return the tapelotno10
     */
    public String getTapelotno10() {
        return tapelotno10;
    }

    /**
     * ｷｬﾘｱﾃｰﾌﾟLOT NO.⑩
     *
     * @param tapelotno10 the tapelotno10 to set
     */
    public void setTapelotno10(String tapelotno10) {
        this.tapelotno10 = tapelotno10;
    }

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟLOT NO.①
     *
     * @return the toptapelotno1
     */
    public String getToptapelotno1() {
        return toptapelotno1;
    }

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟLOT NO.①
     *
     * @param toptapelotno1 the toptapelotno1 to set
     */
    public void setToptapelotno1(String toptapelotno1) {
        this.toptapelotno1 = toptapelotno1;
    }

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟLOT NO.②
     *
     * @return the toptapelotno2
     */
    public String getToptapelotno2() {
        return toptapelotno2;
    }

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟLOT NO.②
     *
     * @param toptapelotno2 the toptapelotno2 to set
     */
    public void setToptapelotno2(String toptapelotno2) {
        this.toptapelotno2 = toptapelotno2;
    }

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟLOT NO.③
     *
     * @return the toptapelotno3
     */
    public String getToptapelotno3() {
        return toptapelotno3;
    }

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟLOT NO.③
     *
     * @param toptapelotno3 the toptapelotno3 to set
     */
    public void setToptapelotno3(String toptapelotno3) {
        this.toptapelotno3 = toptapelotno3;
    }

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟLOT NO.④
     *
     * @return the toptapelotno4
     */
    public String getToptapelotno4() {
        return toptapelotno4;
    }

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟLOT NO.④
     *
     * @param toptapelotno4 the toptapelotno4 to set
     */
    public void setToptapelotno4(String toptapelotno4) {
        this.toptapelotno4 = toptapelotno4;
    }

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑤
     *
     * @return the toptapelotno5
     */
    public String getToptapelotno5() {
        return toptapelotno5;
    }

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑤
     *
     * @param toptapelotno5 the toptapelotno5 to set
     */
    public void setToptapelotno5(String toptapelotno5) {
        this.toptapelotno5 = toptapelotno5;
    }

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑥
     *
     * @return the toptapelotno6
     */
    public String getToptapelotno6() {
        return toptapelotno6;
    }

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑥
     *
     * @param toptapelotno6 the toptapelotno6 to set
     */
    public void setToptapelotno6(String toptapelotno6) {
        this.toptapelotno6 = toptapelotno6;
    }

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑦
     *
     * @return the toptapelotno7
     */
    public String getToptapelotno7() {
        return toptapelotno7;
    }

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑦
     *
     * @param toptapelotno7 the toptapelotno7 to set
     */
    public void setToptapelotno7(String toptapelotno7) {
        this.toptapelotno7 = toptapelotno7;
    }

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑧
     *
     * @return the toptapelotno8
     */
    public String getToptapelotno8() {
        return toptapelotno8;
    }

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑧
     *
     * @param toptapelotno8 the toptapelotno8 to set
     */
    public void setToptapelotno8(String toptapelotno8) {
        this.toptapelotno8 = toptapelotno8;
    }

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑨
     *
     * @return the toptapelotno9
     */
    public String getToptapelotno9() {
        return toptapelotno9;
    }

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑨
     *
     * @param toptapelotno9 the toptapelotno9 to set
     */
    public void setToptapelotno9(String toptapelotno9) {
        this.toptapelotno9 = toptapelotno9;
    }

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑩
     *
     * @return the toptapelotno10
     */
    public String getToptapelotno10() {
        return toptapelotno10;
    }

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑩
     *
     * @param toptapelotno10 the toptapelotno10 to set
     */
    public void setToptapelotno10(String toptapelotno10) {
        this.toptapelotno10 = toptapelotno10;
    }

    /**
     * ﾎﾞﾄﾑﾃｰﾌﾟLOT NO.①
     *
     * @return the bottomtapelot1
     */
    public String getBottomtapelot1() {
        return bottomtapelot1;
    }

    /**
     * ﾎﾞﾄﾑﾃｰﾌﾟLOT NO.①
     *
     * @param bottomtapelot1 the bottomtapelot1 to set
     */
    public void setBottomtapelot1(String bottomtapelot1) {
        this.bottomtapelot1 = bottomtapelot1;
    }

    /**
     * ﾎﾞﾄﾑﾃｰﾌﾟLOT NO.②
     *
     * @return the bottomtapelot2
     */
    public String getBottomtapelot2() {
        return bottomtapelot2;
    }

    /**
     * ﾎﾞﾄﾑﾃｰﾌﾟLOT NO.②
     *
     * @param bottomtapelot2 the bottomtapelot2 to set
     */
    public void setBottomtapelot2(String bottomtapelot2) {
        this.bottomtapelot2 = bottomtapelot2;
    }

    /**
     * ﾎﾞﾄﾑﾃｰﾌﾟLOT NO.③
     *
     * @return the bottomtapelot3
     */
    public String getBottomtapelot3() {
        return bottomtapelot3;
    }

    /**
     * ﾎﾞﾄﾑﾃｰﾌﾟLOT NO.③
     *
     * @param bottomtapelot3 the bottomtapelot3 to set
     */
    public void setBottomtapelot3(String bottomtapelot3) {
        this.bottomtapelot3 = bottomtapelot3;
    }

    /**
     * 容量範囲ｾｯﾄ値　Hi
     *
     * @return the yoryohannihi
     */
    public BigDecimal getYoryohannihi() {
        return yoryohannihi;
    }

    /**
     * 容量範囲ｾｯﾄ値　Hi
     *
     * @param yoryohannihi the yoryohannihi to set
     */
    public void setYoryohannihi(BigDecimal yoryohannihi) {
        this.yoryohannihi = yoryohannihi;
    }

    /**
     * 容量範囲ｾｯﾄ値　Lo
     *
     * @return the yoryohannilo
     */
    public BigDecimal getYoryohannilo() {
        return yoryohannilo;
    }

    /**
     * 容量範囲ｾｯﾄ値　Lo
     *
     * @param yoryohannilo the yoryohannilo to set
     */
    public void setYoryohannilo(BigDecimal yoryohannilo) {
        this.yoryohannilo = yoryohannilo;
    }

    /**
     * 容量範囲ｾｯﾄ値単位
     *
     * @return the yoryohannitanni
     */
    public String getYoryohannitanni() {
        return yoryohannitanni;
    }

    /**
     * 容量範囲ｾｯﾄ値単位
     *
     * @param yoryohannitanni the yoryohannitanni to set
     */
    public void setYoryohannitanni(String yoryohannitanni) {
        this.yoryohannitanni = yoryohannitanni;
    }

    /**
     * ﾚﾝｼﾞ表示
     *
     * @return the rangehyoji
     */
    public Integer getRangehyoji() {
        return rangehyoji;
    }

    /**
     * ﾚﾝｼﾞ表示
     *
     * @param rangehyoji the rangehyoji to set
     */
    public void setRangehyoji(Integer rangehyoji) {
        this.rangehyoji = rangehyoji;
    }

    /**
     * ﾚﾝｼﾞ表示単位
     *
     * @return the rangehyojitanni
     */
    public String getRangehyojitanni() {
        return rangehyojitanni;
    }

    /**
     * ﾚﾝｼﾞ表示単位
     *
     * @param rangehyojitanni the rangehyojitanni to set
     */
    public void setRangehyojitanni(String rangehyojitanni) {
        this.rangehyojitanni = rangehyojitanni;
    }

    /**
     * 容量値
     *
     * @return the youryou
     */
    public BigDecimal getYouryou() {
        return youryou;
    }

    /**
     * 容量値
     *
     * @param youryou the youryou to set
     */
    public void setYouryou(BigDecimal youryou) {
        this.youryou = youryou;
    }

    /**
     * 容量値単位
     *
     * @return the youryoutanni
     */
    public String getYouryoutanni() {
        return youryoutanni;
    }

    /**
     * 容量値単位
     *
     * @param youryoutanni the youryoutanni to set
     */
    public void setYouryoutanni(String youryoutanni) {
        this.youryoutanni = youryoutanni;
    }

    /**
     * DFｾｯﾄ値　Hi
     *
     * @return the dfsethi
     */
    public BigDecimal getDfsethi() {
        return dfsethi;
    }

    /**
     * DFｾｯﾄ値　Hi
     *
     * @param dfsethi the dfsethi to set
     */
    public void setDfsethi(BigDecimal dfsethi) {
        this.dfsethi = dfsethi;
    }

    /**
     * DFｾｯﾄ値　Lo
     *
     * @return the dfsetlo
     */
    public BigDecimal getDfsetlo() {
        return dfsetlo;
    }

    /**
     * DFｾｯﾄ値　Lo
     *
     * @param dfsetlo the dfsetlo to set
     */
    public void setDfsetlo(BigDecimal dfsetlo) {
        this.dfsetlo = dfsetlo;
    }

    /**
     * DF値
     *
     * @return the dfatai
     */
    public BigDecimal getDfatai() {
        return dfatai;
    }

    /**
     * DF値
     *
     * @param dfatai the dfatai to set
     */
    public void setDfatai(BigDecimal dfatai) {
        this.dfatai = dfatai;
    }

    /**
     * 画像設定条件
     *
     * @return the gazousetteijyoken
     */
    public String getGazousetteijyoken() {
        return gazousetteijyoken;
    }

    /**
     * 画像設定条件
     *
     * @param gazousetteijyoken the gazousetteijyoken to set
     */
    public void setGazousetteijyoken(String gazousetteijyoken) {
        this.gazousetteijyoken = gazousetteijyoken;
    }

    /**
     * ﾎｯﾊﾟｰﾈｼﾞ確認
     *
     * @return the hoperneji
     */
    public String getHoperneji() {
        return hoperneji;
    }

    /**
     * ﾎｯﾊﾟｰﾈｼﾞ確認
     *
     * @param hoperneji the hoperneji to set
     */
    public void setHoperneji(String hoperneji) {
        this.hoperneji = hoperneji;
    }

    /**
     * ｺﾃ清掃
     *
     * @return the koteseisou
     */
    public String getKoteseisou() {
        return koteseisou;
    }

    /**
     * ｺﾃ清掃
     *
     * @param koteseisou the koteseisou to set
     */
    public void setKoteseisou(String koteseisou) {
        this.koteseisou = koteseisou;
    }

    /**
     * 開始前に製品残なき事
     *
     * @return the kaisimaesehinzannasi
     */
    public String getKaisimaesehinzannasi() {
        return kaisimaesehinzannasi;
    }

    /**
     * 開始前に製品残なき事
     *
     * @param kaisimaesehinzannasi the kaisimaesehinzannasi to set
     */
    public void setKaisimaesehinzannasi(String kaisimaesehinzannasi) {
        this.kaisimaesehinzannasi = kaisimaesehinzannasi;
    }

    /**
     * SET者
     *
     * @return the setsya
     */
    public String getSetsya() {
        return setsya;
    }

    /**
     * SET者
     *
     * @param setsya the setsya to set
     */
    public void setSetsya(String setsya) {
        this.setsya = setsya;
    }

    /**
     * Wﾁｪｯｸ者
     *
     * @return the kakuninsya
     */
    public String getKakuninsya() {
        return kakuninsya;
    }

    /**
     * Wﾁｪｯｸ者
     *
     * @param kakuninsya the kakuninsya to set
     */
    public void setKakuninsya(String kakuninsya) {
        this.kakuninsya = kakuninsya;
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
     * 試験担当者
     *
     * @return the sikentantou
     */
    public String getSikentantou() {
        return sikentantou;
    }

    /**
     * 試験担当者
     *
     * @param sikentantou the sikentantou to set
     */
    public void setSikentantou(String sikentantou) {
        this.sikentantou = sikentantou;
    }

    /**
     * 自重落下試験
     *
     * @return the jijyurakkasiken
     */
    public String getJijyurakkasiken() {
        return jijyurakkasiken;
    }

    /**
     * 自重落下試験
     *
     * @param jijyurakkasiken the jijyurakkasiken to set
     */
    public void setJijyurakkasiken(String jijyurakkasiken) {
        this.jijyurakkasiken = jijyurakkasiken;
    }

    /**
     * ﾊﾞﾗｼﾁｪｯｸ
     *
     * @return the barasicheck
     */
    public String getBarasicheck() {
        return barasicheck;
    }

    /**
     * ﾊﾞﾗｼﾁｪｯｸ
     *
     * @param barasicheck the barasicheck to set
     */
    public void setBarasicheck(String barasicheck) {
        this.barasicheck = barasicheck;
    }

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟ確認
     *
     * @return the toptapekakunin
     */
    public String getToptapekakunin() {
        return toptapekakunin;
    }

    /**
     * ﾄｯﾌﾟﾃｰﾌﾟ確認
     *
     * @param toptapekakunin the toptapekakunin to set
     */
    public void setToptapekakunin(String toptapekakunin) {
        this.toptapekakunin = toptapekakunin;
    }

    /**
     * 挿入数
     *
     * @return the sounyuusu
     */
    public Integer getSounyuusu() {
        return sounyuusu;
    }

    /**
     * 挿入数
     *
     * @param sounyuusu the sounyuusu to set
     */
    public void setSounyuusu(Integer sounyuusu) {
        this.sounyuusu = sounyuusu;
    }

    /**
     * 投入数
     *
     * @return the tounyuusu
     */
    public Integer getTounyuusu() {
        return tounyuusu;
    }

    /**
     * 投入数
     *
     * @param tounyuusu the tounyuusu to set
     */
    public void setTounyuusu(Integer tounyuusu) {
        this.tounyuusu = tounyuusu;
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
     * 良品数
     *
     * @return the ryouhinsu
     */
    public Integer getRyouhinsu() {
        return ryouhinsu;
    }

    /**
     * 良品数
     *
     * @param ryouhinsu the ryouhinsu to set
     */
    public void setRyouhinsu(Integer ryouhinsu) {
        this.ryouhinsu = ryouhinsu;
    }

    /**
     * 容量NG(NG1)
     *
     * @return the youryong1
     */
    public Integer getYouryong1() {
        return youryong1;
    }

    /**
     * 容量NG(NG1)
     *
     * @param youryong1 the youryong1 to set
     */
    public void setYouryong1(Integer youryong1) {
        this.youryong1 = youryong1;
    }

    /**
     * 画像NG(上画像数):NG2
     *
     * @return the gazoungue2
     */
    public Integer getGazoungue2() {
        return gazoungue2;
    }

    /**
     * 画像NG(上画像数):NG2
     *
     * @param gazoungue2 the gazoungue2 to set
     */
    public void setGazoungue2(Integer gazoungue2) {
        this.gazoungue2 = gazoungue2;
    }

    /**
     * 画像NG(下画像数):NG2
     *
     * @return the gazoungsita2
     */
    public Integer getGazoungsita2() {
        return gazoungsita2;
    }

    /**
     * 画像NG(下画像数):NG2
     *
     * @param gazoungsita2 the gazoungsita2 to set
     */
    public void setGazoungsita2(Integer gazoungsita2) {
        this.gazoungsita2 = gazoungsita2;
    }

    /**
     * 歩留まり
     *
     * @return the budomari
     */
    public BigDecimal getBudomari() {
        return budomari;
    }

    /**
     * 歩留まり
     *
     * @param budomari the budomari to set
     */
    public void setBudomari(BigDecimal budomari) {
        this.budomari = budomari;
    }

    /**
     * 終了日時
     *
     * @return the shuryonichiji
     */
    public Timestamp getShuryonichiji() {
        return shuryonichiji;
    }

    /**
     * 終了日時
     *
     * @param shuryonichiji the shuryonichiji to set
     */
    public void setShuryonichiji(Timestamp shuryonichiji) {
        this.shuryonichiji = shuryonichiji;
    }

    /**
     * ﾒﾝﾃﾅﾝｽ回数
     *
     * @return the mentekaisu
     */
    public Integer getMentekaisu() {
        return mentekaisu;
    }

    /**
     * ﾒﾝﾃﾅﾝｽ回数
     *
     * @param mentekaisu the mentekaisu to set
     */
    public void setMentekaisu(Integer mentekaisu) {
        this.mentekaisu = mentekaisu;
    }

    /**
     * ﾒﾝﾃ後TP外観
     *
     * @return the mentegotpgaikan
     */
    public String getMentegotpgaikan() {
        return mentegotpgaikan;
    }

    /**
     * ﾒﾝﾃ後TP外観
     *
     * @param mentegotpgaikan the mentegotpgaikan to set
     */
    public void setMentegotpgaikan(String mentegotpgaikan) {
        this.mentegotpgaikan = mentegotpgaikan;
    }

    /**
     * ﾊﾞﾗｼ対象ﾘｰﾙ数
     *
     * @return the barasitaisyoreelsu
     */
    public Integer getBarasitaisyoreelsu() {
        return barasitaisyoreelsu;
    }

    /**
     * ﾊﾞﾗｼ対象ﾘｰﾙ数
     *
     * @param barasitaisyoreelsu the barasitaisyoreelsu to set
     */
    public void setBarasitaisyoreelsu(Integer barasitaisyoreelsu) {
        this.barasitaisyoreelsu = barasitaisyoreelsu;
    }

    /**
     * 空ﾘｰﾙ数
     *
     * @return the akireelsu
     */
    public Integer getAkireelsu() {
        return akireelsu;
    }

    /**
     * 空ﾘｰﾙ数
     *
     * @param akireelsu the akireelsu to set
     */
    public void setAkireelsu(Integer akireelsu) {
        this.akireelsu = akireelsu;
    }

    /**
     * 良品ﾘｰﾙ数
     *
     * @return the ryouhinreelsu
     */
    public Integer getRyouhinreelsu() {
        return ryouhinreelsu;
    }

    /**
     * 良品ﾘｰﾙ数
     *
     * @param ryouhinreelsu the ryouhinreelsu to set
     */
    public void setRyouhinreelsu(Integer ryouhinreelsu) {
        this.ryouhinreelsu = ryouhinreelsu;
    }

    /**
     * QA確認依頼ﾘｰﾙ数
     *
     * @return the qakakuniniraireelsu
     */
    public Integer getQakakuniniraireelsu() {
        return qakakuniniraireelsu;
    }

    /**
     * QA確認依頼ﾘｰﾙ数
     *
     * @param qakakuniniraireelsu the qakakuniniraireelsu to set
     */
    public void setQakakuniniraireelsu(Integer qakakuniniraireelsu) {
        this.qakakuniniraireelsu = qakakuniniraireelsu;
    }

    /**
     * ﾘｰﾙ数ﾁｪｯｸ
     *
     * @return the reelsucheck
     */
    public String getReelsucheck() {
        return reelsucheck;
    }

    /**
     * ﾘｰﾙ数ﾁｪｯｸ
     *
     * @param reelsucheck the reelsucheck to set
     */
    public void setReelsucheck(String reelsucheck) {
        this.reelsucheck = reelsucheck;
    }

    /**
     * TP後清掃：ﾎｯﾊﾟｰ部
     *
     * @return the tpatohopper
     */
    public String getTpatohopper() {
        return tpatohopper;
    }

    /**
     * TP後清掃：ﾎｯﾊﾟｰ部
     *
     * @param tpatohopper the tpatohopper to set
     */
    public void setTpatohopper(String tpatohopper) {
        this.tpatohopper = tpatohopper;
    }

    /**
     * TP後清掃：ﾌｨｰﾀﾞ部
     *
     * @return the tpatofeeder
     */
    public String getTpatofeeder() {
        return tpatofeeder;
    }

    /**
     * TP後清掃：ﾌｨｰﾀﾞ部
     *
     * @param tpatofeeder the tpatofeeder to set
     */
    public void setTpatofeeder(String tpatofeeder) {
        this.tpatofeeder = tpatofeeder;
    }

    /**
     * TP後清掃：INDEX内
     *
     * @return the tpatoindex
     */
    public String getTpatoindex() {
        return tpatoindex;
    }

    /**
     * TP後清掃：INDEX内
     *
     * @param tpatoindex the tpatoindex to set
     */
    public void setTpatoindex(String tpatoindex) {
        this.tpatoindex = tpatoindex;
    }

    /**
     * TP後清掃：NGBOX内
     *
     * @return the tpatongbox
     */
    public String getTpatongbox() {
        return tpatongbox;
    }

    /**
     * TP後清掃：NGBOX内
     *
     * @param tpatongbox the tpatongbox to set
     */
    public void setTpatongbox(String tpatongbox) {
        this.tpatongbox = tpatongbox;
    }

    /**
     * 清掃担当者
     *
     * @return the seisoutantou
     */
    public String getSeisoutantou() {
        return seisoutantou;
    }

    /**
     * 清掃担当者
     *
     * @param seisoutantou the seisoutantou to set
     */
    public void setSeisoutantou(String seisoutantou) {
        this.seisoutantou = seisoutantou;
    }

    /**
     * 清掃確認者
     *
     * @return the seisoukakunin
     */
    public String getSeisoukakunin() {
        return seisoukakunin;
    }

    /**
     * 清掃確認者
     *
     * @param seisoukakunin the seisoukakunin to set
     */
    public void setSeisoukakunin(String seisoukakunin) {
        this.seisoukakunin = seisoukakunin;
    }

    /**
     * ﾊﾞﾗｼ依頼ﾘｰﾙ数
     *
     * @return the barasiiraireelsu
     */
    public Integer getBarasiiraireelsu() {
        return barasiiraireelsu;
    }

    /**
     * ﾊﾞﾗｼ依頼ﾘｰﾙ数
     *
     * @param barasiiraireelsu the barasiiraireelsu to set
     */
    public void setBarasiiraireelsu(Integer barasiiraireelsu) {
        this.barasiiraireelsu = barasiiraireelsu;
    }

    /**
     * ﾊﾞﾗｼ開始日時
     *
     * @return the barasikaisinichiji
     */
    public Timestamp getBarasikaisinichiji() {
        return barasikaisinichiji;
    }

    /**
     * ﾊﾞﾗｼ開始日時
     *
     * @param barasikaisinichiji the barasikaisinichiji to set
     */
    public void setBarasikaisinichiji(Timestamp barasikaisinichiji) {
        this.barasikaisinichiji = barasikaisinichiji;
    }

    /**
     * ﾊﾞﾗｼ終了日時
     *
     * @return the barasiksyuryonichiji
     */
    public Timestamp getBarasiksyuryonichiji() {
        return barasiksyuryonichiji;
    }

    /**
     * ﾊﾞﾗｼ終了日時
     *
     * @param barasiksyuryonichiji the barasiksyuryonichiji to set
     */
    public void setBarasiksyuryonichiji(Timestamp barasiksyuryonichiji) {
        this.barasiksyuryonichiji = barasiksyuryonichiji;
    }

    /**
     * ﾊﾞﾗｼ担当者
     *
     * @return the barasitantou
     */
    public String getBarasitantou() {
        return barasitantou;
    }

    /**
     * ﾊﾞﾗｼ担当者
     *
     * @param barasitantou the barasitantou to set
     */
    public void setBarasitantou(String barasitantou) {
        this.barasitantou = barasitantou;
    }

    /**
     * 脱磁依頼ﾘｰﾙ数
     *
     * @return the datujiiraireelsu
     */
    public Integer getDatujiiraireelsu() {
        return datujiiraireelsu;
    }

    /**
     * 脱磁依頼ﾘｰﾙ数
     *
     * @param datujiiraireelsu the datujiiraireelsu to set
     */
    public void setDatujiiraireelsu(Integer datujiiraireelsu) {
        this.datujiiraireelsu = datujiiraireelsu;
    }

    /**
     * 脱磁開始日時
     *
     * @return the datujikaisinichiji
     */
    public Timestamp getDatujikaisinichiji() {
        return datujikaisinichiji;
    }

    /**
     * 脱磁開始日時
     *
     * @param datujikaisinichiji the datujikaisinichiji to set
     */
    public void setDatujikaisinichiji(Timestamp datujikaisinichiji) {
        this.datujikaisinichiji = datujikaisinichiji;
    }

    /**
     * 脱磁担当者
     *
     * @return the datujitantou
     */
    public String getDatujitantou() {
        return datujitantou;
    }

    /**
     * 脱磁担当者
     *
     * @param datujitantou the datujitantou to set
     */
    public void setDatujitantou(String datujitantou) {
        this.datujitantou = datujitantou;
    }

    /**
     * 確保ﾘｰﾙ数2
     *
     * @return the kakuhoreelsu
     */
    public Integer getKakuhoreelsu() {
        return kakuhoreelsu;
    }

    /**
     * 確保ﾘｰﾙ数2
     *
     * @param kakuhoreelsu the kakuhoreelsu to set
     */
    public void setKakuhoreelsu(Integer kakuhoreelsu) {
        this.kakuhoreelsu = kakuhoreelsu;
    }

    /**
     * 空ﾘｰﾙ数2
     *
     * @return the akireelsu2
     */
    public Integer getAkireelsu2() {
        return akireelsu2;
    }

    /**
     * 空ﾘｰﾙ数2
     *
     * @param akireelsu2 the akireelsu2 to set
     */
    public void setAkireelsu2(Integer akireelsu2) {
        this.akireelsu2 = akireelsu2;
    }

    /**
     * 良品ﾘｰﾙ数2
     *
     * @return the ryouhinreelsu2
     */
    public Integer getRyouhinreelsu2() {
        return ryouhinreelsu2;
    }

    /**
     * 良品ﾘｰﾙ数2
     *
     * @param ryouhinreelsu2 the ryouhinreelsu2 to set
     */
    public void setRyouhinreelsu2(Integer ryouhinreelsu2) {
        this.ryouhinreelsu2 = ryouhinreelsu2;
    }

    /**
     * QA確認依頼ﾘｰﾙ数2
     *
     * @return the qakakuniniraireelsu2
     */
    public Integer getQakakuniniraireelsu2() {
        return qakakuniniraireelsu2;
    }

    /**
     * QA確認依頼ﾘｰﾙ数2
     *
     * @param qakakuniniraireelsu2 the qakakuniniraireelsu2 to set
     */
    public void setQakakuniniraireelsu2(Integer qakakuniniraireelsu2) {
        this.qakakuniniraireelsu2 = qakakuniniraireelsu2;
    }

    /**
     * 最終確認担当者
     *
     * @return the saisyukakuninn
     */
    public String getSaisyukakuninn() {
        return saisyukakuninn;
    }

    /**
     * 最終確認担当者
     *
     * @param saisyukakuninn the saisyukakuninn to set
     */
    public void setSaisyukakuninn(String saisyukakuninn) {
        this.saisyukakuninn = saisyukakuninn;
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
