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
 * 変更日	2020/01/29<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 外観検査のモデルクラスです。
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2020/01/29
 */
public class SrGaikankensa {

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
     * 検査回数
     */
    private Integer kaisuu;

    /**
     * KCPNO
     */
    private String kcpno;

    /**
     * 客先
     */
    private String tokuisaki;

    /**
     * ｵｰﾅｰ
     */
    private String ownercode;

    /**
     * ﾛｯﾄ区分
     */
    private String lotkubuncode;

    /**
     * 後工程指示内容
     */
    private String atokouteisijinaiyou;

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
     * 外観検査種類
     */
    private Integer gaikankensasyurui;

    /**
     * 検査場所
     */
    private String kensabasyo;

    /**
     * 検査種類
     */
    private String kensasyurui;

    /**
     * 検査号機
     */
    private String kensagouki;

    /**
     * 検査ﾌｧｲﾙNo.
     */
    private String kensafileno;

    /**
     * 検査面
     */
    private String kensamen;

    /**
     * 検査開始日時
     */
    private Timestamp kensakaishinichiji;

    /**
     * 検査開始担当者
     */
    private String kensakaishitantousya;

    /**
     * 検査開始認定者
     */
    private String kensakaishininteisya;

    /**
     * 10kﾁｪｯｸ 未検査
     */
    private String juuKcheckmikensa;

    /**
     * 10kﾁｪｯｸ ｶﾒﾗ差
     */
    private String juuKcheckcamerasa;

    /**
     * 1回目処理個数
     */
    private Integer ichikaimesyorikosuu;

    /**
     * 1回目良品個数
     */
    private Integer ichikaimeryouhinkosuu;

    /**
     * 1回目NG1数
     */
    private Integer ichikaimeNg1suu;

    /**
     * 1回目NG2数
     */
    private Integer ichikaimeNg2suu;

    /**
     * 1回目歩留まり
     */
    private BigDecimal ichikaimebudomari;

    /**
     * 1回目未処理・ﾘﾃｽﾄ個数
     */
    private Integer ichikaimemisyori;

    /**
     * 2回目処理個数
     */
    private Integer nikaimesyorikosuu;

    /**
     * 2回目良品個数
     */
    private Integer nikaimeryouhinkosuu;

    /**
     * 2回目NG1数
     */
    private Integer nikaimeNg1suu;

    /**
     * 2回目NG2数
     */
    private Integer nikaimeNg2suu;

    /**
     * 2回目歩留まり
     */
    private BigDecimal nikaimebudomari;

    /**
     * 2回目未処理・ﾘﾃｽﾄ個数
     */
    private Integer nikaimemisyori;

    /**
     * 3回目処理個数
     */
    private Integer sankaimesyorikosuu;

    /**
     * 3回目良品個数
     */
    private Integer sankaimeryouhinkosuu;

    /**
     * 3回目NG1数
     */
    private Integer sankaimeNg1suu;

    /**
     * 3回目NG2数
     */
    private Integer sankaimeNg2suu;

    /**
     * 3回目歩留まり
     */
    private BigDecimal sankaimebudomari;

    /**
     * 3回目未処理・ﾘﾃｽﾄ個数
     */
    private Integer sankaimemisyori;

    /**
     * 合計処理個数
     */
    private Integer goukeisyorikosuu;

    /**
     * 良品総重量
     */
    private BigDecimal ryouhinsoujyuuryou;

    /**
     * 合計良品個数
     */
    private Integer goukeiryouhinkosuu;

    /**
     * NG総重量
     */
    private BigDecimal ngsoujyuuryou;

    /**
     * 合計NG数
     */
    private Integer goukeingsuu;

    /**
     * 合計歩留まり
     */
    private BigDecimal goukeibudomari;

    /**
     * 合計未処理・ﾘﾃｽﾄ個数
     */
    private Integer goukeimisyori;

    /**
     * 未検査率
     */
    private BigDecimal mikennsaritu;

    /**
     * 検査終了日
     */
    private Timestamp kensasyuuryounichiji;

    /**
     * 検査終了担当者
     */
    private String kensasyuuryoutantousya;

    /**
     * 検査終了認定者
     */
    private String kensasyuuryouninteisya;

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
    private Timestamp tourokunichiji;

    /**
     * 更新日時
     */
    private Timestamp koushinnichiji;

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
     * 検査回数
     *
     * @return the kaisuu
     */
    public Integer getKaisuu() {
        return kaisuu;
    }

    /**
     * 検査回数
     *
     * @param kaisuu the kaisuu to set
     */
    public void setKaisuu(Integer kaisuu) {
        this.kaisuu = kaisuu;
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
     * 後工程指示内容
     *
     * @return the atokouteisijinaiyou
     */
    public String getAtokouteisijinaiyou() {
        return atokouteisijinaiyou;
    }

    /**
     * 後工程指示内容
     *
     * @param atokouteisijinaiyou the atokouteisijinaiyou to set
     */
    public void setAtokouteisijinaiyou(String atokouteisijinaiyou) {
        this.atokouteisijinaiyou = atokouteisijinaiyou;
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
     * 外観検査種類
     *
     * @return the gaikankensasyurui
     */
    public Integer getGaikankensasyurui() {
        return gaikankensasyurui;
    }

    /**
     * 外観検査種類
     *
     * @param gaikankensasyurui the gaikankensasyurui to set
     */
    public void setGaikankensasyurui(Integer gaikankensasyurui) {
        this.gaikankensasyurui = gaikankensasyurui;
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
     * 検査種類
     *
     * @return the kensasyurui
     */
    public String getKensasyurui() {
        return kensasyurui;
    }

    /**
     * 検査種類
     *
     * @param kensasyurui the kensasyurui to set
     */
    public void setKensasyurui(String kensasyurui) {
        this.kensasyurui = kensasyurui;
    }

    /**
     * 検査号機
     *
     * @return the kensagouki
     */
    public String getKensagouki() {
        return kensagouki;
    }

    /**
     * 検査号機
     *
     * @param kensagouki the kensagouki to set
     */
    public void setKensagouki(String kensagouki) {
        this.kensagouki = kensagouki;
    }

    /**
     * 検査ﾌｧｲﾙNo.
     *
     * @return the kensafileno
     */
    public String getKensafileno() {
        return kensafileno;
    }

    /**
     * 検査ﾌｧｲﾙNo.
     *
     * @param kensafileno the kensafileno to set
     */
    public void setKensafileno(String kensafileno) {
        this.kensafileno = kensafileno;
    }

    /**
     * 検査面
     *
     * @return the kensamen
     */
    public String getKensamen() {
        return kensamen;
    }

    /**
     * 検査面
     *
     * @param kensamen the kensamen to set
     */
    public void setKensamen(String kensamen) {
        this.kensamen = kensamen;
    }

    /**
     * 検査開始日時
     *
     * @return the kensakaishinichiji
     */
    public Timestamp getKensakaishinichiji() {
        return kensakaishinichiji;
    }

    /**
     * 検査開始日時
     *
     * @param kensakaishinichiji the kensakaishinichiji to set
     */
    public void setKensakaishinichiji(Timestamp kensakaishinichiji) {
        this.kensakaishinichiji = kensakaishinichiji;
    }

    /**
     * 検査開始担当者
     *
     * @return the kensakaishitantousya
     */
    public String getKensakaishitantousya() {
        return kensakaishitantousya;
    }

    /**
     * 検査開始担当者
     *
     * @param kensakaishitantousya the kensakaishitantousya to set
     */
    public void setKensakaishitantousya(String kensakaishitantousya) {
        this.kensakaishitantousya = kensakaishitantousya;
    }

    /**
     * 検査開始認定者
     *
     * @return the kensakaishininteisya
     */
    public String getKensakaishininteisya() {
        return kensakaishininteisya;
    }

    /**
     * 検査開始認定者
     *
     * @param kensakaishininteisya the kensakaishininteisya to set
     */
    public void setKensakaishininteisya(String kensakaishininteisya) {
        this.kensakaishininteisya = kensakaishininteisya;
    }

    /**
     * 10kﾁｪｯｸ 未検査
     *
     * @return the juuKcheckmikensa
     */
    public String getJuuKcheckmikensa() {
        return juuKcheckmikensa;
    }

    /**
     * 10kﾁｪｯｸ 未検査
     *
     * @param juuKcheckmikensa the juuKcheckmikensa to set
     */
    public void setJuuKcheckmikensa(String juuKcheckmikensa) {
        this.juuKcheckmikensa = juuKcheckmikensa;
    }

    /**
     * 10kﾁｪｯｸ ｶﾒﾗ差
     *
     * @return the juuKcheckcamerasa
     */
    public String getJuuKcheckcamerasa() {
        return juuKcheckcamerasa;
    }

    /**
     * 10kﾁｪｯｸ ｶﾒﾗ差
     *
     * @param juuKcheckcamerasa the juuKcheckcamerasa to set
     */
    public void setJuuKcheckcamerasa(String juuKcheckcamerasa) {
        this.juuKcheckcamerasa = juuKcheckcamerasa;
    }

    /**
     * 1回目処理個数
     *
     * @return the ichikaimesyorikosuu
     */
    public Integer getIchikaimesyorikosuu() {
        return ichikaimesyorikosuu;
    }

    /**
     * 1回目処理個数
     *
     * @param ichikaimesyorikosuu the ichikaimesyorikosuu to set
     */
    public void setIchikaimesyorikosuu(Integer ichikaimesyorikosuu) {
        this.ichikaimesyorikosuu = ichikaimesyorikosuu;
    }

    /**
     * 1回目良品個数
     *
     * @return the ichikaimeryouhinkosuu
     */
    public Integer getIchikaimeryouhinkosuu() {
        return ichikaimeryouhinkosuu;
    }

    /**
     * 1回目良品個数
     *
     * @param ichikaimeryouhinkosuu the ichikaimeryouhinkosuu to set
     */
    public void setIchikaimeryouhinkosuu(Integer ichikaimeryouhinkosuu) {
        this.ichikaimeryouhinkosuu = ichikaimeryouhinkosuu;
    }

    /**
     * 1回目NG1数
     *
     * @return the ichikaimeNg1suu
     */
    public Integer getIchikaimeNg1suu() {
        return ichikaimeNg1suu;
    }

    /**
     * 1回目NG1数
     *
     * @param ichikaimeNg1suu the ichikaimeNg1suu to set
     */
    public void setIchikaimeNg1suu(Integer ichikaimeNg1suu) {
        this.ichikaimeNg1suu = ichikaimeNg1suu;
    }

    /**
     * 1回目NG2数
     *
     * @return the ichikaimeNg2suu
     */
    public Integer getIchikaimeNg2suu() {
        return ichikaimeNg2suu;
    }

    /**
     * 1回目NG2数
     *
     * @param ichikaimeNg2suu the ichikaimeNg2suu to set
     */
    public void setIchikaimeNg2suu(Integer ichikaimeNg2suu) {
        this.ichikaimeNg2suu = ichikaimeNg2suu;
    }

    /**
     * 1回目歩留まり
     *
     * @return the ichikaimebudomari
     */
    public BigDecimal getIchikaimebudomari() {
        return ichikaimebudomari;
    }

    /**
     * 1回目歩留まり
     *
     * @param ichikaimebudomari the ichikaimebudomari to set
     */
    public void setIchikaimebudomari(BigDecimal ichikaimebudomari) {
        this.ichikaimebudomari = ichikaimebudomari;
    }

    /**
     * 1回目未処理・ﾘﾃｽﾄ個数
     *
     * @return the ichikaimemisyori
     */
    public Integer getIchikaimemisyori() {
        return ichikaimemisyori;
    }

    /**
     * 1回目未処理・ﾘﾃｽﾄ個数
     *
     * @param ichikaimemisyori the ichikaimemisyori to set
     */
    public void setIchikaimemisyori(Integer ichikaimemisyori) {
        this.ichikaimemisyori = ichikaimemisyori;
    }

    /**
     * 2回目処理個数
     *
     * @return the nikaimesyorikosuu
     */
    public Integer getNikaimesyorikosuu() {
        return nikaimesyorikosuu;
    }

    /**
     * 2回目処理個数
     *
     * @param nikaimesyorikosuu the nikaimesyorikosuu to set
     */
    public void setNikaimesyorikosuu(Integer nikaimesyorikosuu) {
        this.nikaimesyorikosuu = nikaimesyorikosuu;
    }

    /**
     * 2回目良品個数
     *
     * @return the nikaimeryouhinkosuu
     */
    public Integer getNikaimeryouhinkosuu() {
        return nikaimeryouhinkosuu;
    }

    /**
     * 2回目良品個数
     *
     * @param nikaimeryouhinkosuu the nikaimeryouhinkosuu to set
     */
    public void setNikaimeryouhinkosuu(Integer nikaimeryouhinkosuu) {
        this.nikaimeryouhinkosuu = nikaimeryouhinkosuu;
    }

    /**
     * 2回目NG1数
     *
     * @return the nikaimeNg1suu
     */
    public Integer getNikaimeNg1suu() {
        return nikaimeNg1suu;
    }

    /**
     * 2回目NG1数
     *
     * @param nikaimeNg1suu the nikaimeNg1suu to set
     */
    public void setNikaimeNg1suu(Integer nikaimeNg1suu) {
        this.nikaimeNg1suu = nikaimeNg1suu;
    }

    /**
     * 2回目NG2数
     *
     * @return the nikaimeNg2suu
     */
    public Integer getNikaimeNg2suu() {
        return nikaimeNg2suu;
    }

    /**
     * 2回目NG2数
     *
     * @param nikaimeNg2suu the nikaimeNg2suu to set
     */
    public void setNikaimeNg2suu(Integer nikaimeNg2suu) {
        this.nikaimeNg2suu = nikaimeNg2suu;
    }

    /**
     * 2回目歩留まり
     *
     * @return the nikaimebudomari
     */
    public BigDecimal getNikaimebudomari() {
        return nikaimebudomari;
    }

    /**
     * 2回目歩留まり
     *
     * @param nikaimebudomari the nikaimebudomari to set
     */
    public void setNikaimebudomari(BigDecimal nikaimebudomari) {
        this.nikaimebudomari = nikaimebudomari;
    }

    /**
     * 2回目未処理・ﾘﾃｽﾄ個数
     *
     * @return the nikaimemisyori
     */
    public Integer getNikaimemisyori() {
        return nikaimemisyori;
    }

    /**
     * 2回目未処理・ﾘﾃｽﾄ個数
     *
     * @param nikaimemisyori the nikaimemisyori to set
     */
    public void setNikaimemisyori(Integer nikaimemisyori) {
        this.nikaimemisyori = nikaimemisyori;
    }

    /**
     * 3回目処理個数
     *
     * @return the sankaimesyorikosuu
     */
    public Integer getSankaimesyorikosuu() {
        return sankaimesyorikosuu;
    }

    /**
     * 3回目処理個数
     *
     * @param sankaimesyorikosuu the sankaimesyorikosuu to set
     */
    public void setSankaimesyorikosuu(Integer sankaimesyorikosuu) {
        this.sankaimesyorikosuu = sankaimesyorikosuu;
    }

    /**
     * 3回目良品個数
     *
     * @return the sankaimeryouhinkosuu
     */
    public Integer getSankaimeryouhinkosuu() {
        return sankaimeryouhinkosuu;
    }

    /**
     * 3回目良品個数
     *
     * @param sankaimeryouhinkosuu the sankaimeryouhinkosuu to set
     */
    public void setSankaimeryouhinkosuu(Integer sankaimeryouhinkosuu) {
        this.sankaimeryouhinkosuu = sankaimeryouhinkosuu;
    }

    /**
     * 3回目NG1数
     *
     * @return the sankaimeNg1suu
     */
    public Integer getSankaimeNg1suu() {
        return sankaimeNg1suu;
    }

    /**
     * 3回目NG1数
     *
     * @param sankaimeNg1suu the sankaimeNg1suu to set
     */
    public void setSankaimeNg1suu(Integer sankaimeNg1suu) {
        this.sankaimeNg1suu = sankaimeNg1suu;
    }

    /**
     * 3回目NG2数
     *
     * @return the sankaimeNg2suu
     */
    public Integer getSankaimeNg2suu() {
        return sankaimeNg2suu;
    }

    /**
     * 3回目NG2数
     *
     * @param sankaimeNg2suu the sankaimeNg2suu to set
     */
    public void setSankaimeNg2suu(Integer sankaimeNg2suu) {
        this.sankaimeNg2suu = sankaimeNg2suu;
    }

    /**
     * 3回目歩留まり
     *
     * @return the sankaimebudomari
     */
    public BigDecimal getSankaimebudomari() {
        return sankaimebudomari;
    }

    /**
     * 3回目歩留まり
     *
     * @param sankaimebudomari the sankaimebudomari to set
     */
    public void setSankaimebudomari(BigDecimal sankaimebudomari) {
        this.sankaimebudomari = sankaimebudomari;
    }

    /**
     * 3回目未処理・ﾘﾃｽﾄ個数
     *
     * @return the sankaimemisyori
     */
    public Integer getSankaimemisyori() {
        return sankaimemisyori;
    }

    /**
     * 3回目未処理・ﾘﾃｽﾄ個数
     *
     * @param sankaimemisyori the sankaimemisyori to set
     */
    public void setSankaimemisyori(Integer sankaimemisyori) {
        this.sankaimemisyori = sankaimemisyori;
    }

    /**
     * 合計処理個数
     *
     * @return the goukeisyorikosuu
     */
    public Integer getGoukeisyorikosuu() {
        return goukeisyorikosuu;
    }

    /**
     * 合計処理個数
     *
     * @param goukeisyorikosuu the goukeisyorikosuu to set
     */
    public void setGoukeisyorikosuu(Integer goukeisyorikosuu) {
        this.goukeisyorikosuu = goukeisyorikosuu;
    }

    /**
     * 良品総重量
     *
     * @return the ryouhinsoujyuuryou
     */
    public BigDecimal getRyouhinsoujyuuryou() {
        return ryouhinsoujyuuryou;
    }

    /**
     * 良品総重量
     *
     * @param ryouhinsoujyuuryou the ryouhinsoujyuuryou to set
     */
    public void setRyouhinsoujyuuryou(BigDecimal ryouhinsoujyuuryou) {
        this.ryouhinsoujyuuryou = ryouhinsoujyuuryou;
    }

    /**
     * 合計良品個数
     *
     * @return the goukeiryouhinkosuu
     */
    public Integer getGoukeiryouhinkosuu() {
        return goukeiryouhinkosuu;
    }

    /**
     * 合計良品個数
     *
     * @param goukeiryouhinkosuu the goukeiryouhinkosuu to set
     */
    public void setGoukeiryouhinkosuu(Integer goukeiryouhinkosuu) {
        this.goukeiryouhinkosuu = goukeiryouhinkosuu;
    }

    /**
     * NG総重量
     *
     * @return the ngsoujyuuryou
     */
    public BigDecimal getNgsoujyuuryou() {
        return ngsoujyuuryou;
    }

    /**
     * NG総重量
     *
     * @param ngsoujyuuryou the ngsoujyuuryou to set
     */
    public void setNgsoujyuuryou(BigDecimal ngsoujyuuryou) {
        this.ngsoujyuuryou = ngsoujyuuryou;
    }

    /**
     * 合計NG数
     *
     * @return the goukeingsuu
     */
    public Integer getGoukeingsuu() {
        return goukeingsuu;
    }

    /**
     * 合計NG数
     *
     * @param goukeingsuu the goukeingsuu to set
     */
    public void setGoukeingsuu(Integer goukeingsuu) {
        this.goukeingsuu = goukeingsuu;
    }

    /**
     * 合計歩留まり
     *
     * @return the goukeibudomari
     */
    public BigDecimal getGoukeibudomari() {
        return goukeibudomari;
    }

    /**
     * 合計歩留まり
     *
     * @param goukeibudomari the goukeibudomari to set
     */
    public void setGoukeibudomari(BigDecimal goukeibudomari) {
        this.goukeibudomari = goukeibudomari;
    }

    /**
     * 合計未処理・ﾘﾃｽﾄ個数
     *
     * @return the goukeimisyori
     */
    public Integer getGoukeimisyori() {
        return goukeimisyori;
    }

    /**
     * 合計未処理・ﾘﾃｽﾄ個数
     *
     * @param goukeimisyori the goukeimisyori to set
     */
    public void setGoukeimisyori(Integer goukeimisyori) {
        this.goukeimisyori = goukeimisyori;
    }

    /**
     * 未検査率
     *
     * @return the mikennsaritu
     */
    public BigDecimal getMikennsaritu() {
        return mikennsaritu;
    }

    /**
     * 未検査率
     *
     * @param mikennsaritu the mikennsaritu to set
     */
    public void setMikennsaritu(BigDecimal mikennsaritu) {
        this.mikennsaritu = mikennsaritu;
    }

    /**
     * 検査終了日
     *
     * @return the kensasyuuryounichiji
     */
    public Timestamp getKensasyuuryounichiji() {
        return kensasyuuryounichiji;
    }

    /**
     * 検査終了日
     *
     * @param kensasyuuryounichiji the kensasyuuryounichiji to set
     */
    public void setKensasyuuryounichiji(Timestamp kensasyuuryounichiji) {
        this.kensasyuuryounichiji = kensasyuuryounichiji;
    }

    /**
     * 検査終了担当者
     *
     * @return the kensasyuuryoutantousya
     */
    public String getKensasyuuryoutantousya() {
        return kensasyuuryoutantousya;
    }

    /**
     * 検査終了担当者
     *
     * @param kensasyuuryoutantousya the kensasyuuryoutantousya to set
     */
    public void setKensasyuuryoutantousya(String kensasyuuryoutantousya) {
        this.kensasyuuryoutantousya = kensasyuuryoutantousya;
    }

    /**
     * 検査終了認定者
     *
     * @return the kensasyuuryouninteisya
     */
    public String getKensasyuuryouninteisya() {
        return kensasyuuryouninteisya;
    }

    /**
     * 検査終了認定者
     *
     * @param kensasyuuryouninteisya the kensasyuuryouninteisya to set
     */
    public void setKensasyuuryouninteisya(String kensasyuuryouninteisya) {
        this.kensasyuuryouninteisya = kensasyuuryouninteisya;
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
     * @return the tourokunichiji
     */
    public Timestamp getTourokunichiji() {
        return tourokunichiji;
    }

    /**
     * 登録日時
     *
     * @param tourokunichiji the tourokunichiji to set
     */
    public void setTourokunichiji(Timestamp tourokunichiji) {
        this.tourokunichiji = tourokunichiji;
    }

    /**
     * 更新日時
     *
     * @return the koushinnichiji
     */
    public Timestamp getKoushinnichiji() {
        return koushinnichiji;
    }

    /**
     * 更新日時
     *
     * @param koushinnichiji the koushinnichiji to set
     */
    public void setKoushinnichiji(Timestamp koushinnichiji) {
        this.koushinnichiji = koushinnichiji;
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
