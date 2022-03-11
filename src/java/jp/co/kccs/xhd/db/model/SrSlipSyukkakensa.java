/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/12/22<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SR_SLIP_SYUKKAKENSA(ｽﾘｯﾌﾟ作製・出荷検査)のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since 2021/12/22
 */
public class SrSlipSyukkakensa {

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
     * ｽﾘｯﾌﾟ品名
     */
    private String sliphinmei;

    /**
     * ｽﾘｯﾌﾟLotNo
     */
    private String sliplotno;

    /**
     * ﾛｯﾄ区分
     */
    private String lotkubun;

    /**
     * 原料記号
     */
    private String genryoukigou;

    /**
     * 脱脂皿の種類
     */
    private String dasshisara;

    /**
     * ﾙﾂﾎﾞNo
     */
    private String rutsubono;

    /**
     * ﾙﾂﾎﾞ風袋重量
     */
    private BigDecimal rutsubohuutaijyuuryou;

    /**
     * 乾燥前ｽﾘｯﾌﾟ重量規格
     */
    private String kansoumaeslipjyuuryoukikaku;

    /**
     * 乾燥前ｽﾘｯﾌﾟ重量
     */
    private BigDecimal kansoumaeslipjyuuryou;

    /**
     * 乾燥機①
     */
    private String kannsouki1;

    /**
     * 乾燥温度①
     */
    private String kannsouondo1;

    /**
     * 乾燥時間①
     */
    private String kansoujikan1;

    /**
     * 乾燥開始日時①
     */
    private Timestamp kansoukaishijikan1;

    /**
     * 乾燥終了日時①
     */
    private Timestamp kansousyuuryoujikan1;

    /**
     * 乾燥機②
     */
    private String kannsouki2;

    /**
     * 乾燥温度②
     */
    private String kannsouondo2;

    /**
     * 乾燥時間②
     */
    private String kansoujikan2;

    /**
     * 乾燥開始日時②
     */
    private Timestamp kansoukaishijikan2;

    /**
     * 乾燥終了日時②
     */
    private Timestamp kansousyuuryoujikan2;

    /**
     * 乾燥後総重量
     */
    private BigDecimal kansougosoujyuuryou;

    /**
     * 乾燥後正味重量
     */
    private BigDecimal kansougosyoumijyuuryou;

    /**
     * 固形分規格
     */
    private String kokeibunkikaku;

    /**
     * 固形分比率
     */
    private BigDecimal kokeibunhiritsu;

    /**
     * 固形分測定担当者
     */
    private String kokeibunsokuteitantousya;

    /**
     * 測定器
     */
    private String sokuteiki;

    /**
     * 測定号機
     */
    private String sokuteigouki;

    /**
     * ｽﾋﾟﾝﾄﾞﾙの種類
     */
    private String spindlesyurui;

    /**
     * 回転数
     */
    private String kaitensuu;

    /**
     * 測定日時
     */
    private Timestamp sokuteinichiji;

    /**
     * 粘度測定規格
     */
    private String nendosokuteikikaku;

    /**
     * 粘度測定結果
     */
    private BigDecimal nendosokuteikekka;

    /**
     * 温度測定規格
     */
    private String ondosokuteikikaku;

    /**
     * 温度測定結果
     */
    private BigDecimal ondosokuteikekka;

    /**
     * 粘度測定担当者
     */
    private String nendosokuteitantousya;

    /**
     * 収率(%)
     */
    private BigDecimal syuuritsu;

    /**
     * ｽﾘｯﾌﾟ有効期限
     */
    private Timestamp slipyuukoukigen;

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
    private Integer revision;

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
     * 実績No
     *
     * @return the jissekino
     */
    public Integer getJissekino() {
        return jissekino;
    }

    /**
     * 実績No
     *
     * @param jissekino the jissekino to set
     */
    public void setJissekino(Integer jissekino) {
        this.jissekino = jissekino;
    }

    /**
     * ｽﾘｯﾌﾟ品名
     *
     * @return the sliphinmei
     */
    public String getSliphinmei() {
        return sliphinmei;
    }

    /**
     * ｽﾘｯﾌﾟ品名
     *
     * @param sliphinmei the sliphinmei to set
     */
    public void setSliphinmei(String sliphinmei) {
        this.sliphinmei = sliphinmei;
    }

    /**
     * ｽﾘｯﾌﾟLotNo
     *
     * @return the sliplotno
     */
    public String getSliplotno() {
        return sliplotno;
    }

    /**
     * ｽﾘｯﾌﾟLotNo
     *
     * @param sliplotno the sliplotno to set
     */
    public void setSliplotno(String sliplotno) {
        this.sliplotno = sliplotno;
    }

    /**
     * ﾛｯﾄ区分
     *
     * @return the lotkubun
     */
    public String getLotkubun() {
        return lotkubun;
    }

    /**
     * ﾛｯﾄ区分
     *
     * @param lotkubun the lotkubun to set
     */
    public void setLotkubun(String lotkubun) {
        this.lotkubun = lotkubun;
    }

    /**
     * 原料記号
     *
     * @return the genryoukigou
     */
    public String getGenryoukigou() {
        return genryoukigou;
    }

    /**
     * 原料記号
     *
     * @param genryoukigou the genryoukigou to set
     */
    public void setGenryoukigou(String genryoukigou) {
        this.genryoukigou = genryoukigou;
    }

    /**
     * 脱脂皿の種類
     *
     * @return the dasshisara
     */
    public String getDasshisara() {
        return dasshisara;
    }

    /**
     * 脱脂皿の種類
     *
     * @param dasshisara the dasshisara to set
     */
    public void setDasshisara(String dasshisara) {
        this.dasshisara = dasshisara;
    }

    /**
     * ﾙﾂﾎﾞNo
     *
     * @return the rutsubono
     */
    public String getRutsubono() {
        return rutsubono;
    }

    /**
     * ﾙﾂﾎﾞNo
     *
     * @param rutsubono the rutsubono to set
     */
    public void setRutsubono(String rutsubono) {
        this.rutsubono = rutsubono;
    }

    /**
     * ﾙﾂﾎﾞ風袋重量
     *
     * @return the rutsubohuutaijyuuryou
     */
    public BigDecimal getRutsubohuutaijyuuryou() {
        return rutsubohuutaijyuuryou;
    }

    /**
     * ﾙﾂﾎﾞ風袋重量
     *
     * @param rutsubohuutaijyuuryou the rutsubohuutaijyuuryou to set
     */
    public void setRutsubohuutaijyuuryou(BigDecimal rutsubohuutaijyuuryou) {
        this.rutsubohuutaijyuuryou = rutsubohuutaijyuuryou;
    }

    /**
     * 乾燥前ｽﾘｯﾌﾟ重量規格
     *
     * @return the kansoumaeslipjyuuryoukikaku
     */
    public String getKansoumaeslipjyuuryoukikaku() {
        return kansoumaeslipjyuuryoukikaku;
    }

    /**
     * 乾燥前ｽﾘｯﾌﾟ重量規格
     *
     * @param kansoumaeslipjyuuryoukikaku the kansoumaeslipjyuuryoukikaku to set
     */
    public void setKansoumaeslipjyuuryoukikaku(String kansoumaeslipjyuuryoukikaku) {
        this.kansoumaeslipjyuuryoukikaku = kansoumaeslipjyuuryoukikaku;
    }

    /**
     * 乾燥前ｽﾘｯﾌﾟ重量
     *
     * @return the kansoumaeslipjyuuryou
     */
    public BigDecimal getKansoumaeslipjyuuryou() {
        return kansoumaeslipjyuuryou;
    }

    /**
     * 乾燥前ｽﾘｯﾌﾟ重量
     *
     * @param kansoumaeslipjyuuryou the kansoumaeslipjyuuryou to set
     */
    public void setKansoumaeslipjyuuryou(BigDecimal kansoumaeslipjyuuryou) {
        this.kansoumaeslipjyuuryou = kansoumaeslipjyuuryou;
    }

    /**
     * 乾燥機①
     *
     * @return the kannsouki1
     */
    public String getKannsouki1() {
        return kannsouki1;
    }

    /**
     * 乾燥機①
     *
     * @param kannsouki1 the kannsouki1 to set
     */
    public void setKannsouki1(String kannsouki1) {
        this.kannsouki1 = kannsouki1;
    }

    /**
     * 乾燥温度①
     *
     * @return the kannsouondo1
     */
    public String getKannsouondo1() {
        return kannsouondo1;
    }

    /**
     * 乾燥温度①
     *
     * @param kannsouondo1 the kannsouondo1 to set
     */
    public void setKannsouondo1(String kannsouondo1) {
        this.kannsouondo1 = kannsouondo1;
    }

    /**
     * 乾燥時間①
     *
     * @return the kansoujikan1
     */
    public String getKansoujikan1() {
        return kansoujikan1;
    }

    /**
     * 乾燥時間①
     *
     * @param kansoujikan1 the kansoujikan1 to set
     */
    public void setKansoujikan1(String kansoujikan1) {
        this.kansoujikan1 = kansoujikan1;
    }

    /**
     * 乾燥開始日時①
     *
     * @return the kansoukaishijikan1
     */
    public Timestamp getKansoukaishijikan1() {
        return kansoukaishijikan1;
    }

    /**
     * 乾燥開始日時①
     *
     * @param kansoukaishijikan1 the kansoukaishijikan1 to set
     */
    public void setKansoukaishijikan1(Timestamp kansoukaishijikan1) {
        this.kansoukaishijikan1 = kansoukaishijikan1;
    }

    /**
     * 乾燥終了日時①
     *
     * @return the kansousyuuryoujikan1
     */
    public Timestamp getKansousyuuryoujikan1() {
        return kansousyuuryoujikan1;
    }

    /**
     * 乾燥終了日時①
     *
     * @param kansousyuuryoujikan1 the kansousyuuryoujikan1 to set
     */
    public void setKansousyuuryoujikan1(Timestamp kansousyuuryoujikan1) {
        this.kansousyuuryoujikan1 = kansousyuuryoujikan1;
    }

    /**
     * 乾燥機②
     *
     * @return the kannsouki2
     */
    public String getKannsouki2() {
        return kannsouki2;
    }

    /**
     * 乾燥機②
     *
     * @param kannsouki2 the kannsouki2 to set
     */
    public void setKannsouki2(String kannsouki2) {
        this.kannsouki2 = kannsouki2;
    }

    /**
     * 乾燥温度②
     *
     * @return the kannsouondo2
     */
    public String getKannsouondo2() {
        return kannsouondo2;
    }

    /**
     * 乾燥温度②
     *
     * @param kannsouondo2 the kannsouondo2 to set
     */
    public void setKannsouondo2(String kannsouondo2) {
        this.kannsouondo2 = kannsouondo2;
    }

    /**
     * 乾燥時間②
     *
     * @return the kansoujikan2
     */
    public String getKansoujikan2() {
        return kansoujikan2;
    }

    /**
     * 乾燥時間②
     *
     * @param kansoujikan2 the kansoujikan2 to set
     */
    public void setKansoujikan2(String kansoujikan2) {
        this.kansoujikan2 = kansoujikan2;
    }

    /**
     * 乾燥開始日時②
     *
     * @return the kansoukaishijikan2
     */
    public Timestamp getKansoukaishijikan2() {
        return kansoukaishijikan2;
    }

    /**
     * 乾燥開始日時②
     *
     * @param kansoukaishijikan2 the kansoukaishijikan2 to set
     */
    public void setKansoukaishijikan2(Timestamp kansoukaishijikan2) {
        this.kansoukaishijikan2 = kansoukaishijikan2;
    }

    /**
     * 乾燥終了日時②
     *
     * @return the kansousyuuryoujikan2
     */
    public Timestamp getKansousyuuryoujikan2() {
        return kansousyuuryoujikan2;
    }

    /**
     * 乾燥終了日時②
     *
     * @param kansousyuuryoujikan2 the kansousyuuryoujikan2 to set
     */
    public void setKansousyuuryoujikan2(Timestamp kansousyuuryoujikan2) {
        this.kansousyuuryoujikan2 = kansousyuuryoujikan2;
    }

    /**
     * 乾燥後総重量
     *
     * @return the kansougosoujyuuryou
     */
    public BigDecimal getKansougosoujyuuryou() {
        return kansougosoujyuuryou;
    }

    /**
     * 乾燥後総重量
     *
     * @param kansougosoujyuuryou the kansougosoujyuuryou to set
     */
    public void setKansougosoujyuuryou(BigDecimal kansougosoujyuuryou) {
        this.kansougosoujyuuryou = kansougosoujyuuryou;
    }

    /**
     * 乾燥後正味重量
     *
     * @return the kansougosyoumijyuuryou
     */
    public BigDecimal getKansougosyoumijyuuryou() {
        return kansougosyoumijyuuryou;
    }

    /**
     * 乾燥後正味重量
     *
     * @param kansougosyoumijyuuryou the kansougosyoumijyuuryou to set
     */
    public void setKansougosyoumijyuuryou(BigDecimal kansougosyoumijyuuryou) {
        this.kansougosyoumijyuuryou = kansougosyoumijyuuryou;
    }

    /**
     * 固形分規格
     *
     * @return the kokeibunkikaku
     */
    public String getKokeibunkikaku() {
        return kokeibunkikaku;
    }

    /**
     * 固形分規格
     *
     * @param kokeibunkikaku the kokeibunkikaku to set
     */
    public void setKokeibunkikaku(String kokeibunkikaku) {
        this.kokeibunkikaku = kokeibunkikaku;
    }

    /**
     * 固形分比率
     *
     * @return the kokeibunhiritsu
     */
    public BigDecimal getKokeibunhiritsu() {
        return kokeibunhiritsu;
    }

    /**
     * 固形分比率
     *
     * @param kokeibunhiritsu the kokeibunhiritsu to set
     */
    public void setKokeibunhiritsu(BigDecimal kokeibunhiritsu) {
        this.kokeibunhiritsu = kokeibunhiritsu;
    }

    /**
     * 固形分測定担当者
     *
     * @return the kokeibunsokuteitantousya
     */
    public String getKokeibunsokuteitantousya() {
        return kokeibunsokuteitantousya;
    }

    /**
     * 固形分測定担当者
     *
     * @param kokeibunsokuteitantousya the kokeibunsokuteitantousya to set
     */
    public void setKokeibunsokuteitantousya(String kokeibunsokuteitantousya) {
        this.kokeibunsokuteitantousya = kokeibunsokuteitantousya;
    }

    /**
     * 測定器
     *
     * @return the sokuteiki
     */
    public String getSokuteiki() {
        return sokuteiki;
    }

    /**
     * 測定器
     *
     * @param sokuteiki the sokuteiki to set
     */
    public void setSokuteiki(String sokuteiki) {
        this.sokuteiki = sokuteiki;
    }

    /**
     * 測定号機
     *
     * @return the sokuteigouki
     */
    public String getSokuteigouki() {
        return sokuteigouki;
    }

    /**
     * 測定号機
     *
     * @param sokuteigouki the sokuteigouki to set
     */
    public void setSokuteigouki(String sokuteigouki) {
        this.sokuteigouki = sokuteigouki;
    }

    /**
     * ｽﾋﾟﾝﾄﾞﾙの種類
     *
     * @return the spindlesyurui
     */
    public String getSpindlesyurui() {
        return spindlesyurui;
    }

    /**
     * ｽﾋﾟﾝﾄﾞﾙの種類
     *
     * @param spindlesyurui the spindlesyurui to set
     */
    public void setSpindlesyurui(String spindlesyurui) {
        this.spindlesyurui = spindlesyurui;
    }

    /**
     * 回転数
     *
     * @return the kaitensuu
     */
    public String getKaitensuu() {
        return kaitensuu;
    }

    /**
     * 回転数
     *
     * @param kaitensuu the kaitensuu to set
     */
    public void setKaitensuu(String kaitensuu) {
        this.kaitensuu = kaitensuu;
    }

    /**
     * 測定日時
     *
     * @return the sokuteinichiji
     */
    public Timestamp getSokuteinichiji() {
        return sokuteinichiji;
    }

    /**
     * 測定日時
     *
     * @param sokuteinichiji the sokuteinichiji to set
     */
    public void setSokuteinichiji(Timestamp sokuteinichiji) {
        this.sokuteinichiji = sokuteinichiji;
    }

    /**
     * 粘度測定規格
     *
     * @return the nendosokuteikikaku
     */
    public String getNendosokuteikikaku() {
        return nendosokuteikikaku;
    }

    /**
     * 粘度測定規格
     *
     * @param nendosokuteikikaku the nendosokuteikikaku to set
     */
    public void setNendosokuteikikaku(String nendosokuteikikaku) {
        this.nendosokuteikikaku = nendosokuteikikaku;
    }

    /**
     * 粘度測定結果
     *
     * @return the nendosokuteikekka
     */
    public BigDecimal getNendosokuteikekka() {
        return nendosokuteikekka;
    }

    /**
     * 粘度測定結果
     *
     * @param nendosokuteikekka the nendosokuteikekka to set
     */
    public void setNendosokuteikekka(BigDecimal nendosokuteikekka) {
        this.nendosokuteikekka = nendosokuteikekka;
    }

    /**
     * 温度測定規格
     *
     * @return the ondosokuteikikaku
     */
    public String getOndosokuteikikaku() {
        return ondosokuteikikaku;
    }

    /**
     * 温度測定規格
     *
     * @param ondosokuteikikaku the ondosokuteikikaku to set
     */
    public void setOndosokuteikikaku(String ondosokuteikikaku) {
        this.ondosokuteikikaku = ondosokuteikikaku;
    }

    /**
     * 温度測定結果
     *
     * @return the ondosokuteikekka
     */
    public BigDecimal getOndosokuteikekka() {
        return ondosokuteikekka;
    }

    /**
     * 温度測定結果
     *
     * @param ondosokuteikekka the ondosokuteikekka to set
     */
    public void setOndosokuteikekka(BigDecimal ondosokuteikekka) {
        this.ondosokuteikekka = ondosokuteikekka;
    }

    /**
     * 粘度測定担当者
     *
     * @return the nendosokuteitantousya
     */
    public String getNendosokuteitantousya() {
        return nendosokuteitantousya;
    }

    /**
     * 粘度測定担当者
     *
     * @param nendosokuteitantousya the nendosokuteitantousya to set
     */
    public void setNendosokuteitantousya(String nendosokuteitantousya) {
        this.nendosokuteitantousya = nendosokuteitantousya;
    }

    /**
     * 収率(%)
     *
     * @return the syuuritsu
     */
    public BigDecimal getSyuuritsu() {
        return syuuritsu;
    }

    /**
     * 収率(%)
     *
     * @param syuuritsu the syuuritsu to set
     */
    public void setSyuuritsu(BigDecimal syuuritsu) {
        this.syuuritsu = syuuritsu;
    }

    /**
     * ｽﾘｯﾌﾟ有効期限
     *
     * @return the slipyuukoukigen
     */
    public Timestamp getSlipyuukoukigen() {
        return slipyuukoukigen;
    }

    /**
     * ｽﾘｯﾌﾟ有効期限
     *
     * @param slipyuukoukigen the slipyuukoukigen to set
     */
    public void setSlipyuukoukigen(Timestamp slipyuukoukigen) {
        this.slipyuukoukigen = slipyuukoukigen;
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
    public Integer getRevision() {
        return revision;
    }

    /**
     * revision
     *
     * @param revision the revision to set
     */
    public void setRevision(Integer revision) {
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
