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
 * 変更日	2020/03/09<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	863 K.Zhang<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 外観検査履歴検索画面のモデルクラスです。
 *
 * @author 863 K.Zhang
 * @since  2020/03/09
 */
public class GXHDO201B046Model implements Serializable{

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
     * ｵｰﾅｰ
     */
    private String ownercode = "";
    
    /**
     * ﾛｯﾄ区分
     */
    private String lotkubuncode = "";

    /**
     * 後工程指示内容
     */
    private String atokouteisijinaiyou = "";

    /**
     * 送り良品数
     */
    private Integer okuriryouhinsuu = null;

    /**
     * 受入れ単位重量
     */
    private BigDecimal ukeiretannijyuryo = null;

    /**
     * 受入れ総重量
     */
    private BigDecimal ukeiresoujyuryou = null;

    /**
     * 外観検査種類
     */
    private Integer gaikankensasyurui = null;

    /**
     * 検査場所
     */
    private String kensabasyo = "";

    /**
     * 検査種類
     */
    private String kensasyurui = "";

    /**
     * 検査号機
     */
    private String kensagouki = "";

    /**
     * 検査ﾌｧｲﾙNo.
     */
    private String kensafileno = "";

    /**
     * 検査面
     */
    private String kensamen = "";

    /**
     * 検査開始日時
     */
    private Timestamp kensakaishinichiji = null;

    /**
     * 検査開始担当者
     */
    private String kensakaishitantousya = "";

    /**
     * 検査開始認定者
     */
    private String kensakaishininteisya = "";

    /**
     * 10kﾁｪｯｸ 未検査
     */
    private String kcheckmikensa10 = "";

    /**
     * 10kﾁｪｯｸ ｶﾒﾗ差
     */
    private String kcheckcamerasa10 = "";

    /**
     * 1回目処理個数
     */
    private Integer kaimesyorikosuu1 = null;

    /**
     * 1回目良品個数
     */
    private Integer kaimeryouhinkosuu1 = null;

    /**
     * 1回目NG1数
     */
    private Integer kaimeng1suu1 = null;

    /**
     * 1回目NG2数
     */
    private Integer kaimeng2suu1 = null;

    /**
     * 1回目歩留まり
     */
    private BigDecimal kaimebudomari1 = null;

    /**
     * 1回目未処理・ﾘﾃｽﾄ個数
     */
    private Integer kaimemisyori1 = null;

    /**
     * 2回目処理個数
     */
    private Integer kaimesyorikosuu2 = null;

    /**
     * 2回目良品個数
     */
    private Integer kaimeryouhinkosuu2 = null;

    /**
     * 2回目NG1数
     */
    private Integer kaimeng1suu2 = null;

    /**
     * 2回目NG2数
     */
    private Integer kaimeng2suu2 = null;

    /**
     * 2回目歩留まり
     */
    private BigDecimal kaimebudomari2 = null;

    /**
     * 2回目未処理・ﾘﾃｽﾄ個数
     */
    private Integer kaimemisyori2 = null;

    /**
     * 3回目処理個数
     */
    private Integer kaimesyorikosuu3 = null;
    
    /**
     * 3回目良品個数
     */
    private Integer kaimeryouhinkosuu3 = null;

    /**
     * 3回目NG1数
     */
    private Integer kaimeng1suu3 = null;
    
    /**
     * 3回目NG2数
     */
    private Integer kaimeng2suu3 = null;

    /**
     * 3回目歩留まり
     */
    private BigDecimal kaimebudomari3 = null;

    /**
     * 3回目未処理・ﾘﾃｽﾄ個数
     */
    private Integer kaimemisyori3 = null;

    /**
     * 合計処理個数
     */
    private Integer goukeisyorikosuu = null;

    /**
     * 良品総重量
     */
    private BigDecimal ryouhinsoujyuuryou = null;

    /**
     * 合計良品個数
     */
    private Integer goukeiryouhinkosuu = null;
    
    /**
     * NG総重量
     */
    private BigDecimal ngsoujyuuryou = null;

    /**
     * 合計NG数
     */
    private Integer goukeingsuu = null;

    /**
     * 合計歩留まり
     */
    private BigDecimal goukeibudomari = null;

    /**
     * 合計未処理・ﾘﾃｽﾄ個数
     */
    private Integer goukeimisyori = null;

    /**
     * 未検査率
     */
    private BigDecimal mikennsaritu = null;

    /**
     * 検査終了日
     */
    private Timestamp kensasyuuryounichiji = null;
    
    /**
     * 検査終了担当者
     */
    private String kensasyuuryoutantousya = "";

    /**
     * 検査終了認定者
     */
    private String kensasyuuryouninteisya = "";

    /**
     * QA外観抜き取り検査
     */
    private String qagaikannukitorikensa = "";

    /**
     * 備考1
     */
    private String bikou1 = "";

    /**
     * 備考2
     */
    private String bikou2 = "";

    /**
     * 備考2
     */
    private String jokyo = "";


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
     * 後工程指示内容
     * @return the atokouteisijinaiyou
     */
    public String getAtokouteisijinaiyou() {
        return atokouteisijinaiyou;
    }

    /**
     * 後工程指示内容
     * @param atokouteisijinaiyou the atokouteisijinaiyou to set
     */
    public void setAtokouteisijinaiyou(String atokouteisijinaiyou) {
        this.atokouteisijinaiyou = atokouteisijinaiyou;
    }

    /**
     * 送り良品数
     * @return the okuriryouhinsuu
     */
    public Integer getOkuriryouhinsuu() {
        return okuriryouhinsuu;
    }

    /**
     * 送り良品数
     * @param okuriryouhinsuu the okuriryouhinsuu to set
     */
    public void setOkuriryouhinsuu(Integer okuriryouhinsuu) {
        this.okuriryouhinsuu = okuriryouhinsuu;
    }

    /**
     * 受入れ単位重量
     * @return the ukeiretannijyuryo
     */
    public BigDecimal getUkeiretannijyuryo() {
        return ukeiretannijyuryo;
    }

    /**
     * 受入れ単位重量
     * @param ukeiretannijyuryo the ukeiretannijyuryo to set
     */
    public void setUkeiretannijyuryo(BigDecimal ukeiretannijyuryo) {
        this.ukeiretannijyuryo = ukeiretannijyuryo;
    }

    /**
     * 受入れ総重量
     * @return the ukeiresoujyuryou
     */
    public BigDecimal getUkeiresoujyuryou() {
        return ukeiresoujyuryou;
    }

    /**
     * 受入れ総重量
     * @param ukeiresoujyuryou the ukeiresoujyuryou to set
     */
    public void setUkeiresoujyuryou(BigDecimal ukeiresoujyuryou) {
        this.ukeiresoujyuryou = ukeiresoujyuryou;
    }

    /**
     * 外観検査種類
     * @return the gaikankensasyurui
     */
    public Integer getGaikankensasyurui() {
        return gaikankensasyurui;
    }

    /**
     * 外観検査種類
     * @param gaikankensasyurui the gaikankensasyurui to set
     */
    public void setGaikankensasyurui(Integer gaikankensasyurui) {
        this.gaikankensasyurui = gaikankensasyurui;
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
     * 検査種類
     * @return the kensasyurui
     */
    public String getKensasyurui() {
        return kensasyurui;
    }

    /**
     * 検査種類
     * @param kensasyurui the kensasyurui to set
     */
    public void setKensasyurui(String kensasyurui) {
        this.kensasyurui = kensasyurui;
    }

    /**
     * 検査号機
     * @return the kensagouki
     */
    public String getKensagouki() {
        return kensagouki;
    }

    /**
     * 検査号機
     * @param kensagouki the kensagouki to set
     */
    public void setKensagouki(String kensagouki) {
        this.kensagouki = kensagouki;
    }

    /**
     * 検査ﾌｧｲﾙNo.
     * @return the kensafileno
     */
    public String getKensafileno() {
        return kensafileno;
    }

    /**
     * 検査ﾌｧｲﾙNo.
     * @param kensafileno the kensafileno to set
     */
    public void setKensafileno(String kensafileno) {
        this.kensafileno = kensafileno;
    }

    /**
     * 検査面
     * @return the kensamen
     */
    public String getKensamen() {
        return kensamen;
    }

    /**
     * 検査面
     * @param kensamen the kensamen to set
     */
    public void setKensamen(String kensamen) {
        this.kensamen = kensamen;
    }

    /**
     * 検査開始日時
     * @return the kensakaishitantousya
     */
    public Timestamp getKensakaishinichiji() {
        return kensakaishinichiji;
    }

    /**
     * 検査開始日時
     * @param kensakaishinichiji the kensakaishitantousya to set
     */
    public void setKensakaishinichiji(Timestamp kensakaishinichiji) {
        this.kensakaishinichiji = kensakaishinichiji;
    }

    /**
     * 検査開始担当者
     * @return the kensakaishitantousya
     */
    public String getKensakaishitantousya() {
        return kensakaishitantousya;
    }

    /**
     * 検査開始担当者
     * @param kensakaishitantousya the kensakaishitantousya to set
     */
    public void setKensakaishitantousya(String kensakaishitantousya) {
        this.kensakaishitantousya = kensakaishitantousya;
    }

    /**
     * 検査開始認定者
     * @return the kensakaishininteisya
     */
    public String getKensakaishininteisya() {
        return kensakaishininteisya;
    }

    /**
     * 検査開始認定者
     * @param kensakaishininteisya the kensakaishininteisya to set
     */
    public void setKensakaishininteisya(String kensakaishininteisya) {
        this.kensakaishininteisya = kensakaishininteisya;
    }

    /**
     * 10kﾁｪｯｸ 未検査
     * @return the kcheckmikensa10
     */
    public String getKcheckmikensa10() {
        return kcheckmikensa10;
    }

    /**
     * 10kﾁｪｯｸ 未検査
     * @param kcheckmikensa10 the kcheckmikensa10 to set
     */
    public void setKcheckmikensa10(String kcheckmikensa10) {
        this.kcheckmikensa10 = kcheckmikensa10;
    }

    /**
     * 10kﾁｪｯｸ ｶﾒﾗ差
     * @return the Kcheckcamerasa10
     */
    public String getKcheckcamerasa10() {
        return kcheckcamerasa10;
    }

    /**
     * 10kﾁｪｯｸ ｶﾒﾗ差
     * @param kcheckcamerasa10 the kcheckcamerasa10 to set
     */
    public void setKcheckcamerasa10(String kcheckcamerasa10) {
        this.kcheckcamerasa10 = kcheckcamerasa10;
    }

    /**
     * 1回目処理個数
     * @return the kaimesyorikosuu1
     */
    public Integer getKaimesyorikosuu1() {
        return kaimesyorikosuu1;
    }

    /**
     * 1回目処理個数
     * @param kaimesyorikosuu1 the kaimesyorikosuu1 to set
     */
    public void setKaimesyorikosuu1(Integer kaimesyorikosuu1) {
        this.kaimesyorikosuu1 = kaimesyorikosuu1;
    }

    /**
     * 1回目良品個数
     * @return the kaimeryouhinkosuu1
     */
    public Integer getKaimeryouhinkosuu1() {
        return kaimeryouhinkosuu1;
    }

    /**
     * 1回目良品個数
     * @param kaimeryouhinkosuu1 the kaimeryouhinkosuu1 to set
     */
    public void setKaimeryouhinkosuu1(Integer kaimeryouhinkosuu1) {
        this.kaimeryouhinkosuu1 = kaimeryouhinkosuu1;
    }

    /**
     * 1回目NG1数
     * @return the kaimeng1suu1
     */
    public Integer getKaimeng1suu1() {
        return kaimeng1suu1;
    }

    /**
     * 1回目NG1数
     * @param kaimeng1suu1 the kaimeng1suu1 to set
     */
    public void setKaimeng1suu1(Integer kaimeng1suu1) {
        this.kaimeng1suu1 = kaimeng1suu1;
    }

    /**
     * 1回目NG2数
     * @return the kaimeng2suu1
     */
    public Integer getKaimeng2suu1() {
        return kaimeng2suu1;
    }

    /**
     * 1回目NG2数
     * @param kaimeng2suu1 the kaimeng2suu1 to set
     */
    public void setKaimeng2suu1(Integer kaimeng2suu1) {
        this.kaimeng2suu1 = kaimeng2suu1;
    }

    /**
     * 1回目歩留まり
     * @return the kaimebudomari1
     */
    public BigDecimal getKaimebudomari1() {
        return kaimebudomari1;
    }

    /**
     * 1回目歩留まり
     * @param kaimebudomari1 the kaimebudomari1 to set
     */
    public void setKaimebudomari1(BigDecimal kaimebudomari1) {
        this.kaimebudomari1 = kaimebudomari1;
    }

    /**
     * 1回目未処理・ﾘﾃｽﾄ個数
     * @return the kaimemisyori1
     */
    public Integer getKaimemisyori1() {
        return kaimemisyori1;
    }

    /**
     * 1回目未処理・ﾘﾃｽﾄ個数
     * @param kaimemisyori1 the kaimemisyori1 to set
     */
    public void setKaimemisyori1(Integer kaimemisyori1) {
        this.kaimemisyori1 = kaimemisyori1;
    }

    /**
     * 2回目処理個数
     * @return the kaimesyorikosuu2
     */
    public Integer getKaimesyorikosuu2() {
        return kaimesyorikosuu2;
    }

    /**
     * 2回目処理個数
     * @param kaimesyorikosuu2 the kaimesyorikosuu2 to set
     */
    public void setKaimesyorikosuu2(Integer kaimesyorikosuu2) {
        this.kaimesyorikosuu2 = kaimesyorikosuu2;
    }

    /**
     * 2回目良品個数
     * @return the kaimeryouhinkosuu2
     */
    public Integer getKaimeryouhinkosuu2() {
        return kaimeryouhinkosuu2;
    }

    /**
     * 2回目良品個数
     * @param kaimeryouhinkosuu2 the kaimeryouhinkosuu2 to set
     */
    public void setKaimeryouhinkosuu2(Integer kaimeryouhinkosuu2) {
        this.kaimeryouhinkosuu2 = kaimeryouhinkosuu2;
    }

    /**
     * 2回目NG1数
     * @return the kaimeng1suu2
     */
    public Integer getKaimeng1suu2() {
        return kaimeng1suu2;
    }

    /**
     * 2回目NG1数
     * @param kaimeng1suu2 the kaimeng1suu2 to set
     */
    public void setKaimeng1suu2(Integer kaimeng1suu2) {
        this.kaimeng1suu2 = kaimeng1suu2;
    }

    /**
     * 2回目NG2数
     * @return the kaimeng2suu2
     */
    public Integer getKaimeng2suu2() {
        return kaimeng2suu2;
    }

    /**
     * 2回目NG2数
     * @param kaimeng2suu2 the kaimeng2suu2 to set
     */
    public void setKaimeng2suu2(Integer kaimeng2suu2) {
        this.kaimeng2suu2 = kaimeng2suu2;
    }

    /**
     * 2回目歩留まり
     * @return the kaimebudomari2
     */
    public BigDecimal getKaimebudomari2() {
        return kaimebudomari2;
    }

    /**
     * 2回目歩留まり
     * @param kaimebudomari2 the kaimebudomari2 to set
     */
    public void setKaimebudomari2(BigDecimal kaimebudomari2) {
        this.kaimebudomari2 = kaimebudomari2;
    }

    /**
     * 2回目未処理・ﾘﾃｽﾄ個数
     * @return the kaimemisyori2
     */
    public Integer getKaimemisyori2() {
        return kaimemisyori2;
    }

    /**
     * 2回目未処理・ﾘﾃｽﾄ個数
     * @param kaimemisyori2 the kaimemisyori2 to set
     */
    public void setKaimemisyori2(Integer kaimemisyori2) {
        this.kaimemisyori2 = kaimemisyori2;
    }

    /**
     * 3回目処理個数
     * @return the kaimesyorikosuu3
     */
    public Integer getKaimesyorikosuu3() {
        return kaimesyorikosuu3;
    }

    /**
     * 3回目処理個数
     * @param kaimesyorikosuu3 the kaimesyorikosuu3 to set
     */
    public void setKaimesyorikosuu3(Integer kaimesyorikosuu3) {
        this.kaimesyorikosuu3 = kaimesyorikosuu3;
    }

    /**
     * 3回目良品個数
     * @return the kaimeryouhinkosuu3
     */
    public Integer getKaimeryouhinkosuu3() {
        return kaimeryouhinkosuu3;
    }

    /**
     * 3回目良品個数
     * @param kaimeryouhinkosuu3 the kaimeryouhinkosuu3 to set
     */
    public void setKaimeryouhinkosuu3(Integer kaimeryouhinkosuu3) {
        this.kaimeryouhinkosuu3 = kaimeryouhinkosuu3;
    }

    /**
     * 3回目NG1数
     * @return the kaimeng1suu3
     */
    public Integer getKaimeng1suu3() {
        return kaimeng1suu3;
    }

    /**
     * 3回目NG1数
     * @param kaimeng1suu3 the kaimeng1suu3 to set
     */
    public void setKaimeng1suu3(Integer kaimeng1suu3) {
        this.kaimeng1suu3 = kaimeng1suu3;
    }

    /**
     * 3回目NG2数
     * @return the kaimeng2suu3
     */
    public Integer getKaimeng2suu3() {
        return kaimeng2suu3;
    }

    /**
     * 3回目NG2数
     * @param kaimeng2suu3 the kaimeng2suu3 to set
     */
    public void setKaimeng2suu3(Integer kaimeng2suu3) {
        this.kaimeng2suu3 = kaimeng2suu3;
    }

    /**
     * 3回目歩留まり
     * @return the kaimebudomari3
     */
    public BigDecimal getKaimebudomari3() {
        return kaimebudomari3;
    }

    /**
     * 3回目歩留まり
     * @param kaimebudomari3 the kaimebudomari3 to set
     */
    public void setKaimebudomari3(BigDecimal kaimebudomari3) {
        this.kaimebudomari3 = kaimebudomari3;
    }

    /**
     * 3回目未処理・ﾘﾃｽﾄ個数
     * @return the kaimemisyori3
     */
    public Integer getKaimemisyori3() {
        return kaimemisyori3;
    }

    /**
     * 3回目未処理・ﾘﾃｽﾄ個数
     * @param kaimemisyori3 the kaimemisyori3 to set
     */
    public void setKaimemisyori3(Integer kaimemisyori3) {
        this.kaimemisyori3 = kaimemisyori3;
    }

    /**
     * 合計処理個数
     * @return the goukeisyorikosuu
     */
    public Integer getGoukeisyorikosuu() {
        return goukeisyorikosuu;
    }

    /**
     * 合計処理個数
     * @param goukeisyorikosuu the goukeisyorikosuu to set
     */
    public void setGoukeisyorikosuu(Integer goukeisyorikosuu) {
        this.goukeisyorikosuu = goukeisyorikosuu;
    }

    /**
     * 良品総重量
     * @return the ryouhinsoujyuuryou
     */
    public BigDecimal getRyouhinsoujyuuryou() {
        return ryouhinsoujyuuryou;
    }

    /**
     * 良品総重量
     * @param ryouhinsoujyuuryou the ryouhinsoujyuuryou to set
     */
    public void setRyouhinsoujyuuryou(BigDecimal ryouhinsoujyuuryou) {
        this.ryouhinsoujyuuryou = ryouhinsoujyuuryou;
    }

    /**
     * 合計良品個数
     * @return the goukeiryouhinkosuu
     */
    public Integer getGoukeiryouhinkosuu() {
        return goukeiryouhinkosuu;
    }

    /**
     * 合計良品個数
     * @param goukeiryouhinkosuu the goukeiryouhinkosuu to set
     */
    public void setGoukeiryouhinkosuu(Integer goukeiryouhinkosuu) {
        this.goukeiryouhinkosuu = goukeiryouhinkosuu;
    }

    /**
     * NG総重量
     * @return the ngsoujyuuryou
     */
    public BigDecimal getNgsoujyuuryou() {
        return ngsoujyuuryou;
    }

    /**
     * NG総重量
     * @param ngsoujyuuryou the ngsoujyuuryou to set
     */
    public void setNgsoujyuuryou(BigDecimal ngsoujyuuryou) {
        this.ngsoujyuuryou = ngsoujyuuryou;
    }

    /**
     * 合計NG数
     * @return the goukeingsuu
     */
    public Integer getGoukeingsuu() {
        return goukeingsuu;
    }

    /**
     * 合計NG数
     * @param goukeingsuu the goukeingsuu to set
     */
    public void setGoukeingsuu(Integer goukeingsuu) {
        this.goukeingsuu = goukeingsuu;
    }

    /**
     * 合計歩留まり
     * @return the goukeibudomari
     */
    public BigDecimal getGoukeibudomari() {
        return goukeibudomari;
    }

    /**
     * 合計歩留まり
     * @param goukeibudomari the goukeibudomari to set
     */
    public void setGoukeibudomari(BigDecimal goukeibudomari) {
        this.goukeibudomari = goukeibudomari;
    }

    /**
     * 合計未処理・ﾘﾃｽﾄ個数
     * @return the goukeimisyori
     */
    public Integer getGoukeimisyori() {
        return goukeimisyori;
    }

    /**
     * 合計未処理・ﾘﾃｽﾄ個数
     * @param goukeimisyori the goukeimisyori to set
     */
    public void setGoukeimisyori(Integer goukeimisyori) {
        this.goukeimisyori = goukeimisyori;
    }

    /**
     * 未検査率
     * @return the mikennsaritu
     */
    public BigDecimal getMikennsaritu() {
        return mikennsaritu;
    }

    /**
     * 未検査率
     * @param mikennsaritu the mikennsaritu to set
     */
    public void setMikennsaritu(BigDecimal mikennsaritu) {
        this.mikennsaritu = mikennsaritu;
    }

    /**
     * 検査終了日
     * @return the kensasyuuryounichiji
     */
    public Timestamp getKensasyuuryounichiji() {
        return kensasyuuryounichiji;
    }

    /**
     * 検査終了日
     * @param kensasyuuryounichiji the kensasyuuryounichiji to set
     */
    public void setKensasyuuryounichiji(Timestamp kensasyuuryounichiji) {
        this.kensasyuuryounichiji = kensasyuuryounichiji;
    }

    /**
     * 検査終了担当者
     * @return the kensasyuuryoutantousya
     */
    public String getKensasyuuryoutantousya() {
        return kensasyuuryoutantousya;
    }

    /**
     * 検査終了担当者
     * @param kensasyuuryoutantousya the kensasyuuryoutantousya to set
     */
    public void setKensasyuuryoutantousya(String kensasyuuryoutantousya) {
        this.kensasyuuryoutantousya = kensasyuuryoutantousya;
    }

    /**
     * 検査終了認定者
     * @return the kensasyuuryouninteisya
     */
    public String getKensasyuuryouninteisya() {
        return kensasyuuryouninteisya;
    }

    /**
     * 検査終了認定者
     * @param kensasyuuryouninteisya the kensasyuuryouninteisya to set
     */
    public void setKensasyuuryouninteisya(String kensasyuuryouninteisya) {
        this.kensasyuuryouninteisya = kensasyuuryouninteisya;
    }

    /**
     * QA外観抜き取り検査
     * @return the qagaikannukitorikensa
     */
    public String getQagaikannukitorikensa() {
        return qagaikannukitorikensa;
    }

    /**
     * QA外観抜き取り検査
     * @param qagaikannukitorikensa the qagaikannukitorikensa to set
     */
    public void setQagaikannukitorikensa(String qagaikannukitorikensa) {
        this.qagaikannukitorikensa = qagaikannukitorikensa;
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
     * 備考2
     * @return the jokyo
     */
    public String getJokyo() {
        return jokyo;
    }

    /**
     * 備考2
     * @param jokyo the jokyo to set
     */
    public void setJokyo(String jokyo) {
        this.jokyo = jokyo;
    }


}
