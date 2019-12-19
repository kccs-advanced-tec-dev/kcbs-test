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
 * 変更日	2019/12/18<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * sr_denkitokuseiesi(電気特性)のモデルクラスです。
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2019/12/18
 */
public class SrDenkitokuseiesi {

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
     * 回数
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
     * 指定公差
     */
    private String siteikousa;

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
     * 外部電極焼付日時
     */
    private Timestamp gdyakitukenitiji;

    /**
     * ﾒｯｷ日時
     */
    private Timestamp mekkinitiji;

    /**
     * 検査場所
     */
    private String kensabasyo;

    /**
     * 選別開始日時
     */
    private Timestamp senbetukaisinitiji;

    /**
     * 選別終了日時
     */
    private Timestamp senbetusyuryounitiji;

    /**
     * 検査号機
     */
    private String kensagouki;

    /**
     * 分類ｴｱｰ圧
     */
    private Integer bunruiairatu;

    /**
     * CDｺﾝﾀｸﾄ圧
     */
    private Integer cdcontactatu;

    /**
     * IRｺﾝﾀｸﾄ圧
     */
    private Integer ircontactatu;

    /**
     * 使用後ｽﾃｰｼｮﾝ確認CD1
     */
    private Integer stationcd1;

    /**
     * 使用後ｽﾃｰｼｮﾝ確認PC1
     */
    private Integer stationpc1;

    /**
     * 使用後ｽﾃｰｼｮﾝ確認PC2
     */
    private Integer stationpc2;

    /**
     * 使用後ｽﾃｰｼｮﾝ確認PC3
     */
    private Integer stationpc3;

    /**
     * 使用後ｽﾃｰｼｮﾝ確認PC4
     */
    private Integer stationpc4;

    /**
     * 使用後ｽﾃｰｼｮﾝ確認IR1
     */
    private Integer stationir1;

    /**
     * 使用後ｽﾃｰｼｮﾝ確認IR2
     */
    private Integer stationir2;

    /**
     * 使用後ｽﾃｰｼｮﾝ確認IR3
     */
    private Integer stationir3;

    /**
     * 使用後ｽﾃｰｼｮﾝ確認IR4
     */
    private Integer stationir4;

    /**
     * 使用後ｽﾃｰｼｮﾝ確認IR5
     */
    private Integer stationir5;

    /**
     * 使用後ｽﾃｰｼｮﾝ確認IR6
     */
    private Integer stationir6;

    /**
     * 使用後ｽﾃｰｼｮﾝ確認IR7
     */
    private Integer stationir7;

    /**
     * 使用後ｽﾃｰｼｮﾝ確認IR8
     */
    private Integer stationir8;

    /**
     * 固定電極 外観･段差
     */
    private String koteidenkyoku;

    /**
     * ﾄﾗｯｸｶﾞｲﾄﾞ隙間
     */
    private String torakkugaido;

    /**
     * ﾃｽﾄﾌﾟﾚｰﾄ 形状･清掃
     */
    private String testplatekeijo;

    /**
     * 分類吹き出し穴
     */
    private String bunruifukidasi;

    /**
     * ﾃｽﾄﾌﾟﾚｰﾄ位置確認(穴位置)
     */
    private String testplatekakunin;

    /**
     * 電極清掃･動作
     */
    private String denkyokuseisou;

    /**
     * 製品投入状態
     */
    private String seihintounyuujotai;

    /**
     * BINﾎﾞｯｸｽ内の清掃ﾁｪｯｸ
     */
    private String binboxseisoucheck;

    /**
     * ｾｯﾄ者
     */
    private String setsya;

    /**
     * 確認者
     */
    private String kakuninsya;

    /**
     * 指定公差歩留まり1
     */
    private String siteikousabudomari1;

    /**
     * 指定公差歩留まり2
     */
    private String siteikousabudomari2;

    /**
     * ﾃｽﾄﾌﾟﾚｰﾄ管理No
     */
    private String testplatekanrino;

    /**
     * Tanδ
     */
    private BigDecimal tan;

    /**
     * 測定周波数
     */
    private String sokuteisyuhasuu;

    /**
     * 測定電圧
     */
    private BigDecimal sokuteidenatu;

    /**
     * 補正用ﾁｯﾌﾟ容量
     */
    private BigDecimal hoseiyoutippuyoryou;

    /**
     * 補正用ﾁｯﾌﾟTanδ
     */
    private BigDecimal hoseiyoutipputan;

    /**
     * 補正前
     */
    private BigDecimal hoseimae;

    /**
     * 補正後
     */
    private BigDecimal hoseigo;

    /**
     * 補正率
     */
    private BigDecimal hoseiritu;

    /**
     * ｽﾀﾝﾀﾞｰﾄﾞ補正
     */
    private String standard;

    /**
     * 分類確認
     */
    private String bunruikakunin;

    /**
     * 外観確認
     */
    private String gaikankakunin;

    /**
     * 熱処理日時
     */
    private Timestamp netsusyorinitiji;

    /**
     * ｴｰｼﾞﾝｸﾞ時間
     */
    private BigDecimal agingjikan;

    /**
     * 充填率
     */
    private String jutenritu;

    /**
     * MC
     */
    private String mc;

    /**
     * 強制排出
     */
    private String kyoseihaisyutu;

    /**
     * 落下
     */
    private String rakka;

    /**
     * 承認者
     */
    private String syoninsha;

    /**
     * 振向者
     */
    private String furimukesya;

    /**
     * 備考1
     */
    private String bikou1;

    /**
     * 備考2
     */
    private String bikou2;

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 PC① 電圧
     */
    private BigDecimal pcdenatu1;

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 PC① 充電時間
     */
    private Integer pcjudenjikan1;

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 PC② 電圧
     */
    private BigDecimal pcdenatu2;

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 PC② 充電時間
     */
    private Integer pcjudenjikan2;

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 PC③ 電圧
     */
    private BigDecimal pcdenatu3;

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 PC③ 充電時間
     */
    private Integer pcjudenjikan3;

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 PC④ 電圧
     */
    private BigDecimal pcdenatu4;

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 PC④ 充電時間
     */
    private Integer pcjudenjikan4;

    /**
     * 耐電圧設定条件 IR① 電圧
     */
    private BigDecimal irdenatu1;

    /**
     * 耐電圧設定条件 IR① 判定値
     */
    private String irhanteiti1;

    /**
     * 耐電圧設定条件 IR① 充電時間
     */
    private Integer irjudenjikan1;

    /**
     * 耐電圧設定条件 IR② 電圧
     */
    private BigDecimal irdenatu2;

    /**
     * 耐電圧設定条件 IR② 判定値
     */
    private String irhanteiti2;

    /**
     * 耐電圧設定条件 IR② 充電時間
     */
    private Integer irjudenjikan2;

    /**
     * 耐電圧設定条件 IR③ 電圧
     */
    private BigDecimal irdenatu3;

    /**
     * 耐電圧設定条件 IR③ 判定値
     */
    private String irhanteiti3;

    /**
     * 耐電圧設定条件 IR③ 充電時間
     */
    private Integer irjudenjikan3;

    /**
     * 耐電圧設定条件 IR④ 電圧
     */
    private BigDecimal irdenatu4;

    /**
     * 耐電圧設定条件 IR④ 判定値
     */
    private String irhanteiti4;

    /**
     * 耐電圧設定条件 IR④ 充電時間
     */
    private Integer irjudenjikan4;

    /**
     * 耐電圧設定条件 IR⑤ 電圧
     */
    private BigDecimal irdenatu5;

    /**
     * 耐電圧設定条件 IR⑤ 判定値
     */
    private String irhanteiti5;

    /**
     * 耐電圧設定条件 IR⑤ 充電時間
     */
    private Integer irjudenjikan5;

    /**
     * 耐電圧設定条件 IR⑥ 電圧
     */
    private BigDecimal irdenatu6;

    /**
     * 耐電圧設定条件 IR⑥ 判定値
     */
    private String irhanteiti6;

    /**
     * 耐電圧設定条件 IR⑥ 充電時間
     */
    private Integer irjudenjikan6;

    /**
     * 耐電圧設定条件 IR⑦ 電圧
     */
    private BigDecimal irdenatu7;

    /**
     * 耐電圧設定条件 IR⑦ 判定値
     */
    private String irhanteiti7;

    /**
     * 耐電圧設定条件 IR⑦ 充電時間
     */
    private Integer irjudenjikan7;

    /**
     * 耐電圧設定条件 IR⑧ 電圧
     */
    private BigDecimal irdenatu8;

    /**
     * 耐電圧設定条件 IR⑧ 判定値
     */
    private String irhanteiti8;

    /**
     * 耐電圧設定条件 IR⑧ 充電時間
     */
    private Integer irjudenjikan8;

    /**
     * BIN1 %区分(設定値)
     */
    private String bin1setteiti;

    /**
     * BIN1 選別区分
     */
    private String bin1senbetukubun;

    /**
     * BIN1 計量後数量
     */
    private Integer bin1keiryougosuryou;

    /**
     * BIN1 ｶｳﾝﾀｰ数
     */
    private Integer bin1countersuu;

    /**
     * BIN1 誤差率(%)
     */
    private BigDecimal bin1gosaritu;

    /**
     * BIN1 ﾏｼﾝ不良率(%)
     */
    private BigDecimal bin1masinfuryouritu;

    /**
     * BIN1 抜き取り結果
     */
    private Integer bin1nukitorikekkabosuu;

    /**
     * BIN1 抜き取り結果
     */
    private Integer bin1nukitorikekka;

    /**
     * BIN1 真の不良率(%)
     */
    private BigDecimal bin1sinnofuryouritu;

    /**
     * BIN1 結果ﾁｪｯｸ
     */
    private String bin1kekkacheck;

    /**
     * BIN2 %区分(設定値)
     */
    private String bin2setteiti;

    /**
     * BIN2 選別区分
     */
    private String bin2senbetukubun;

    /**
     * BIN2 計量後数量
     */
    private Integer bin2keiryougosuryou;

    /**
     * BIN2 ｶｳﾝﾀｰ数
     */
    private Integer bin2countersuu;

    /**
     * BIN2 誤差率(%)
     */
    private BigDecimal bin2gosaritu;

    /**
     * BIN2 ﾏｼﾝ不良率(%)
     */
    private BigDecimal bin2masinfuryouritu;

    /**
     * BIN2 抜き取り結果
     */
    private Integer bin2nukitorikekkabosuu;

    /**
     * BIN2 抜き取り結果
     */
    private Integer bin2nukitorikekka;

    /**
     * BIN2 真の不良率(%)
     */
    private BigDecimal bin2sinnofuryouritu;

    /**
     * BIN2 結果ﾁｪｯｸ
     */
    private String bin2kekkacheck;

    /**
     * BIN3 %区分(設定値)
     */
    private String bin3setteiti;

    /**
     * BIN3 選別区分
     */
    private String bin3senbetukubun;

    /**
     * BIN3 計量後数量
     */
    private Integer bin3keiryougosuryou;

    /**
     * BIN3 ｶｳﾝﾀｰ数
     */
    private Integer bin3countersuu;

    /**
     * BIN3 誤差率(%)
     */
    private BigDecimal bin3gosaritu;

    /**
     * BIN3 ﾏｼﾝ不良率(%)
     */
    private BigDecimal bin3masinfuryouritu;

    /**
     * BIN3 抜き取り結果
     */
    private Integer bin3nukitorikekkabosuu;

    /**
     * BIN3 抜き取り結果
     */
    private Integer bin3nukitorikekka;

    /**
     * BIN3 真の不良率(%)
     */
    private BigDecimal bin3sinnofuryouritu;

    /**
     * BIN3 結果ﾁｪｯｸ
     */
    private String bin3kekkacheck;

    /**
     * BIN4 %区分(設定値)
     */
    private String bin4setteiti;

    /**
     * BIN4 選別区分
     */
    private String bin4senbetukubun;

    /**
     * BIN4 計量後数量
     */
    private Integer bin4keiryougosuryou;

    /**
     * BIN4 ｶｳﾝﾀｰ数
     */
    private Integer bin4countersuu;

    /**
     * BIN4 誤差率(%)
     */
    private BigDecimal bin4gosaritu;

    /**
     * BIN4 ﾏｼﾝ不良率(%)
     */
    private BigDecimal bin4masinfuryouritu;

    /**
     * BIN4 抜き取り結果
     */
    private Integer bin4nukitorikekkabosuu;

    /**
     * BIN4 抜き取り結果
     */
    private Integer bin4nukitorikekka;

    /**
     * BIN4 真の不良率(%)
     */
    private BigDecimal bin4sinnofuryouritu;

    /**
     * BIN4 結果ﾁｪｯｸ
     */
    private String bin4kekkacheck;

    /**
     * BIN5 %区分(設定値)
     */
    private String bin5setteiti;

    /**
     * BIN5 選別区分
     */
    private String bin5senbetukubun;

    /**
     * BIN5 計量後数量
     */
    private Integer bin5keiryougosuryou;

    /**
     * BIN5 ｶｳﾝﾀｰ数
     */
    private Integer bin5countersuu;

    /**
     * BIN5 誤差率(%)
     */
    private BigDecimal bin5gosaritu;

    /**
     * BIN5 ﾏｼﾝ不良率(%)
     */
    private BigDecimal bin5masinfuryouritu;

    /**
     * BIN5 抜き取り結果
     */
    private Integer bin5nukitorikekkabosuu;

    /**
     * BIN5 抜き取り結果
     */
    private Integer bin5nukitorikekka;

    /**
     * BIN5 真の不良率(%)
     */
    private BigDecimal bin5sinnofuryouritu;

    /**
     * BIN5 結果ﾁｪｯｸ
     */
    private String bin5kekkacheck;

    /**
     * BIN5 袋ﾁｪｯｸ
     */
    private Integer bin5fukurocheck;

    /**
     * BIN6 %区分(設定値)
     */
    private String bin6setteiti;

    /**
     * BIN6 選別区分
     */
    private String bin6senbetukubun;

    /**
     * BIN6 計量後数量
     */
    private Integer bin6keiryougosuryou;

    /**
     * BIN6 ｶｳﾝﾀｰ数
     */
    private Integer bin6countersuu;

    /**
     * BIN6 誤差率(%)
     */
    private BigDecimal bin6gosaritu;

    /**
     * BIN6 ﾏｼﾝ不良率(%)
     */
    private BigDecimal bin6masinfuryouritu;

    /**
     * BIN6 抜き取り結果
     */
    private Integer bin6nukitorikekkabosuu;

    /**
     * BIN6 抜き取り結果
     */
    private Integer bin6nukitorikekka;

    /**
     * BIN6 真の不良率(%)
     */
    private BigDecimal bin6sinnofuryouritu;

    /**
     * BIN6 結果ﾁｪｯｸ
     */
    private String bin6kekkacheck;

    /**
     * BIN6 袋ﾁｪｯｸ
     */
    private Integer bin6fukurocheck;

    /**
     * BIN7 %区分(設定値)
     */
    private String bin7setteiti;

    /**
     * BIN7 選別区分
     */
    private String bin7senbetukubun;

    /**
     * BIN7 計量後数量
     */
    private Integer bin7keiryougosuryou;

    /**
     * BIN7 ｶｳﾝﾀｰ数
     */
    private Integer bin7countersuu;

    /**
     * BIN7 誤差率(%)
     */
    private BigDecimal bin7gosaritu;

    /**
     * BIN7 ﾏｼﾝ不良率(%)
     */
    private BigDecimal bin7masinfuryouritu;

    /**
     * BIN7 袋ﾁｪｯｸ
     */
    private Integer bin7fukurocheck;

    /**
     * BIN8 %区分(設定値)
     */
    private String bin8setteiti;

    /**
     * BIN8 選別区分
     */
    private String bin8senbetukubun;

    /**
     * BIN8 計量後数量
     */
    private Integer bin8keiryougosuryou;

    /**
     * BIN8 ｶｳﾝﾀｰ数
     */
    private Integer bin8countersuu;

    /**
     * BIN8 誤差率(%)
     */
    private BigDecimal bin8gosaritu;

    /**
     * BIN8 ﾏｼﾝ不良率(%)
     */
    private BigDecimal bin8masinfuryouritu;

    /**
     * BIN8 袋ﾁｪｯｸ
     */
    private Integer bin8fukurocheck;

    /**
     * BIN9 強制排出 計量後数量
     */
    private Integer bin9keiryougosuryou;

    /**
     * BIN9 強制排出 ﾏｼﾝ不良率
     */
    private BigDecimal bin9masinfuryouritu;

    /**
     * 落下 計量後数量
     */
    private Integer rakkakeiryougosuryou;

    /**
     * 落下 ﾏｼﾝ不良率
     */
    private BigDecimal rakkamasinfuryouritu;

    /**
     * 半田ｻﾝﾌﾟﾙ
     */
    private String handasample;

    /**
     * 信頼性ｻﾝﾌﾟﾙ
     */
    private String sinraiseisample;

    /**
     * 真不良判定者
     */
    private String sinfuryouhanteisya;

    /**
     * 判定入力者
     */
    private String hanteinyuuryokusya;

    /**
     * 取出者
     */
    private String toridasisya;

    /**
     * 公差①
     */
    private String kousa1;

    /**
     * 重量①
     */
    private BigDecimal juryou1;

    /**
     * 個数①
     */
    private Integer kosuu1;

    /**
     * 公差②
     */
    private String kousa2;

    /**
     * 重量②
     */
    private BigDecimal juryou2;

    /**
     * 個数②
     */
    private Integer kosuu2;

    /**
     * 公差③
     */
    private String kousa3;

    /**
     * 重量③
     */
    private BigDecimal juryou3;

    /**
     * 個数③
     */
    private Integer kosuu3;

    /**
     * 公差④
     */
    private String kousa4;

    /**
     * 重量④
     */
    private BigDecimal juryou4;

    /**
     * 個数④
     */
    private Integer kosuu4;

    /**
     * ｶｳﾝﾀｰ総数
     */
    private Integer countersousuu;

    /**
     * 良品重量
     */
    private BigDecimal ryohinjuryou;

    /**
     * 良品個数
     */
    private Integer ryohinkosuu;

    /**
     * 歩留まり
     */
    private BigDecimal budomari;

    /**
     * BIN確認者
     */
    private String binkakuninsya;

    /**
     * 電気特性再検
     */
    private String saiken;

    /**
     * 設備区分
     */
    private String setubikubun;

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
     * @return the kojyo
     */
    public String getKojyo() {
        return kojyo;
    }

    /**
     * 工場ｺｰﾄﾞ
     * @param kojyo the kojyo to set
     */
    public void setKojyo(String kojyo) {
        this.kojyo = kojyo;
    }

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
     * 枝番
     * @return the edaban
     */
    public String getEdaban() {
        return edaban;
    }

    /**
     * 枝番
     * @param edaban the edaban to set
     */
    public void setEdaban(String edaban) {
        this.edaban = edaban;
    }

    /**
     * 回数
     * @return the kaisuu
     */
    public Integer getKaisuu() {
        return kaisuu;
    }

    /**
     * 回数
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
     * 指定公差
     * @return the siteikousa
     */
    public String getSiteikousa() {
        return siteikousa;
    }

    /**
     * 指定公差
     * @param siteikousa the siteikousa to set
     */
    public void setSiteikousa(String siteikousa) {
        this.siteikousa = siteikousa;
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
     * 外部電極焼付日時
     * @return the gdyakitukenitiji
     */
    public Timestamp getGdyakitukenitiji() {
        return gdyakitukenitiji;
    }

    /**
     * 外部電極焼付日時
     * @param gdyakitukenitiji the gdyakitukenitiji to set
     */
    public void setGdyakitukenitiji(Timestamp gdyakitukenitiji) {
        this.gdyakitukenitiji = gdyakitukenitiji;
    }

    /**
     * ﾒｯｷ日時
     * @return the mekkinitiji
     */
    public Timestamp getMekkinitiji() {
        return mekkinitiji;
    }

    /**
     * ﾒｯｷ日時
     * @param mekkinitiji the mekkinitiji to set
     */
    public void setMekkinitiji(Timestamp mekkinitiji) {
        this.mekkinitiji = mekkinitiji;
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
     * 選別開始日時
     * @return the senbetukaisinitiji
     */
    public Timestamp getSenbetukaisinitiji() {
        return senbetukaisinitiji;
    }

    /**
     * 選別開始日時
     * @param senbetukaisinitiji the senbetukaisinitiji to set
     */
    public void setSenbetukaisinitiji(Timestamp senbetukaisinitiji) {
        this.senbetukaisinitiji = senbetukaisinitiji;
    }

    /**
     * 選別終了日時
     * @return the senbetusyuryounitiji
     */
    public Timestamp getSenbetusyuryounitiji() {
        return senbetusyuryounitiji;
    }

    /**
     * 選別終了日時
     * @param senbetusyuryounitiji the senbetusyuryounitiji to set
     */
    public void setSenbetusyuryounitiji(Timestamp senbetusyuryounitiji) {
        this.senbetusyuryounitiji = senbetusyuryounitiji;
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
     * 分類ｴｱｰ圧
     * @return the bunruiairatu
     */
    public Integer getBunruiairatu() {
        return bunruiairatu;
    }

    /**
     * 分類ｴｱｰ圧
     * @param bunruiairatu the bunruiairatu to set
     */
    public void setBunruiairatu(Integer bunruiairatu) {
        this.bunruiairatu = bunruiairatu;
    }

    /**
     * CDｺﾝﾀｸﾄ圧
     * @return the cdcontactatu
     */
    public Integer getCdcontactatu() {
        return cdcontactatu;
    }

    /**
     * CDｺﾝﾀｸﾄ圧
     * @param cdcontactatu the cdcontactatu to set
     */
    public void setCdcontactatu(Integer cdcontactatu) {
        this.cdcontactatu = cdcontactatu;
    }

    /**
     * IRｺﾝﾀｸﾄ圧
     * @return the ircontactatu
     */
    public Integer getIrcontactatu() {
        return ircontactatu;
    }

    /**
     * IRｺﾝﾀｸﾄ圧
     * @param ircontactatu the ircontactatu to set
     */
    public void setIrcontactatu(Integer ircontactatu) {
        this.ircontactatu = ircontactatu;
    }

    /**
     * 使用後ｽﾃｰｼｮﾝ確認CD1
     * @return the stationcd1
     */
    public Integer getStationcd1() {
        return stationcd1;
    }

    /**
     * 使用後ｽﾃｰｼｮﾝ確認CD1
     * @param stationcd1 the stationcd1 to set
     */
    public void setStationcd1(Integer stationcd1) {
        this.stationcd1 = stationcd1;
    }

    /**
     * 使用後ｽﾃｰｼｮﾝ確認PC1
     * @return the stationpc1
     */
    public Integer getStationpc1() {
        return stationpc1;
    }

    /**
     * 使用後ｽﾃｰｼｮﾝ確認PC1
     * @param stationpc1 the stationpc1 to set
     */
    public void setStationpc1(Integer stationpc1) {
        this.stationpc1 = stationpc1;
    }

    /**
     * 使用後ｽﾃｰｼｮﾝ確認PC2
     * @return the stationpc2
     */
    public Integer getStationpc2() {
        return stationpc2;
    }

    /**
     * 使用後ｽﾃｰｼｮﾝ確認PC2
     * @param stationpc2 the stationpc2 to set
     */
    public void setStationpc2(Integer stationpc2) {
        this.stationpc2 = stationpc2;
    }

    /**
     * 使用後ｽﾃｰｼｮﾝ確認PC3
     * @return the stationpc3
     */
    public Integer getStationpc3() {
        return stationpc3;
    }

    /**
     * 使用後ｽﾃｰｼｮﾝ確認PC3
     * @param stationpc3 the stationpc3 to set
     */
    public void setStationpc3(Integer stationpc3) {
        this.stationpc3 = stationpc3;
    }

    /**
     * 使用後ｽﾃｰｼｮﾝ確認PC4
     * @return the stationpc4
     */
    public Integer getStationpc4() {
        return stationpc4;
    }

    /**
     * 使用後ｽﾃｰｼｮﾝ確認PC4
     * @param stationpc4 the stationpc4 to set
     */
    public void setStationpc4(Integer stationpc4) {
        this.stationpc4 = stationpc4;
    }

    /**
     * 使用後ｽﾃｰｼｮﾝ確認IR1
     * @return the stationir1
     */
    public Integer getStationir1() {
        return stationir1;
    }

    /**
     * 使用後ｽﾃｰｼｮﾝ確認IR1
     * @param stationir1 the stationir1 to set
     */
    public void setStationir1(Integer stationir1) {
        this.stationir1 = stationir1;
    }

    /**
     * 使用後ｽﾃｰｼｮﾝ確認IR2
     * @return the stationir2
     */
    public Integer getStationir2() {
        return stationir2;
    }

    /**
     * 使用後ｽﾃｰｼｮﾝ確認IR2
     * @param stationir2 the stationir2 to set
     */
    public void setStationir2(Integer stationir2) {
        this.stationir2 = stationir2;
    }

    /**
     * 使用後ｽﾃｰｼｮﾝ確認IR3
     * @return the stationir3
     */
    public Integer getStationir3() {
        return stationir3;
    }

    /**
     * 使用後ｽﾃｰｼｮﾝ確認IR3
     * @param stationir3 the stationir3 to set
     */
    public void setStationir3(Integer stationir3) {
        this.stationir3 = stationir3;
    }

    /**
     * 使用後ｽﾃｰｼｮﾝ確認IR4
     * @return the stationir4
     */
    public Integer getStationir4() {
        return stationir4;
    }

    /**
     * 使用後ｽﾃｰｼｮﾝ確認IR4
     * @param stationir4 the stationir4 to set
     */
    public void setStationir4(Integer stationir4) {
        this.stationir4 = stationir4;
    }

    /**
     * 使用後ｽﾃｰｼｮﾝ確認IR5
     * @return the stationir5
     */
    public Integer getStationir5() {
        return stationir5;
    }

    /**
     * 使用後ｽﾃｰｼｮﾝ確認IR5
     * @param stationir5 the stationir5 to set
     */
    public void setStationir5(Integer stationir5) {
        this.stationir5 = stationir5;
    }

    /**
     * 使用後ｽﾃｰｼｮﾝ確認IR6
     * @return the stationir6
     */
    public Integer getStationir6() {
        return stationir6;
    }

    /**
     * 使用後ｽﾃｰｼｮﾝ確認IR6
     * @param stationir6 the stationir6 to set
     */
    public void setStationir6(Integer stationir6) {
        this.stationir6 = stationir6;
    }

    /**
     * 使用後ｽﾃｰｼｮﾝ確認IR7
     * @return the stationir7
     */
    public Integer getStationir7() {
        return stationir7;
    }

    /**
     * 使用後ｽﾃｰｼｮﾝ確認IR7
     * @param stationir7 the stationir7 to set
     */
    public void setStationir7(Integer stationir7) {
        this.stationir7 = stationir7;
    }

    /**
     * 使用後ｽﾃｰｼｮﾝ確認IR8
     * @return the stationir8
     */
    public Integer getStationir8() {
        return stationir8;
    }

    /**
     * 使用後ｽﾃｰｼｮﾝ確認IR8
     * @param stationir8 the stationir8 to set
     */
    public void setStationir8(Integer stationir8) {
        this.stationir8 = stationir8;
    }

    /**
     * 固定電極 外観･段差
     * @return the koteidenkyoku
     */
    public String getKoteidenkyoku() {
        return koteidenkyoku;
    }

    /**
     * 固定電極 外観･段差
     * @param koteidenkyoku the koteidenkyoku to set
     */
    public void setKoteidenkyoku(String koteidenkyoku) {
        this.koteidenkyoku = koteidenkyoku;
    }

    /**
     * ﾄﾗｯｸｶﾞｲﾄﾞ隙間
     * @return the torakkugaido
     */
    public String getTorakkugaido() {
        return torakkugaido;
    }

    /**
     * ﾄﾗｯｸｶﾞｲﾄﾞ隙間
     * @param torakkugaido the torakkugaido to set
     */
    public void setTorakkugaido(String torakkugaido) {
        this.torakkugaido = torakkugaido;
    }

    /**
     * ﾃｽﾄﾌﾟﾚｰﾄ 形状･清掃
     * @return the testplatekeijo
     */
    public String getTestplatekeijo() {
        return testplatekeijo;
    }

    /**
     * ﾃｽﾄﾌﾟﾚｰﾄ 形状･清掃
     * @param testplatekeijo the testplatekeijo to set
     */
    public void setTestplatekeijo(String testplatekeijo) {
        this.testplatekeijo = testplatekeijo;
    }

    /**
     * 分類吹き出し穴
     * @return the bunruifukidasi
     */
    public String getBunruifukidasi() {
        return bunruifukidasi;
    }

    /**
     * 分類吹き出し穴
     * @param bunruifukidasi the bunruifukidasi to set
     */
    public void setBunruifukidasi(String bunruifukidasi) {
        this.bunruifukidasi = bunruifukidasi;
    }

    /**
     * ﾃｽﾄﾌﾟﾚｰﾄ位置確認(穴位置)
     * @return the testplatekakunin
     */
    public String getTestplatekakunin() {
        return testplatekakunin;
    }

    /**
     * ﾃｽﾄﾌﾟﾚｰﾄ位置確認(穴位置)
     * @param testplatekakunin the testplatekakunin to set
     */
    public void setTestplatekakunin(String testplatekakunin) {
        this.testplatekakunin = testplatekakunin;
    }

    /**
     * 電極清掃･動作
     * @return the denkyokuseisou
     */
    public String getDenkyokuseisou() {
        return denkyokuseisou;
    }

    /**
     * 電極清掃･動作
     * @param denkyokuseisou the denkyokuseisou to set
     */
    public void setDenkyokuseisou(String denkyokuseisou) {
        this.denkyokuseisou = denkyokuseisou;
    }

    /**
     * 製品投入状態
     * @return the seihintounyuujotai
     */
    public String getSeihintounyuujotai() {
        return seihintounyuujotai;
    }

    /**
     * 製品投入状態
     * @param seihintounyuujotai the seihintounyuujotai to set
     */
    public void setSeihintounyuujotai(String seihintounyuujotai) {
        this.seihintounyuujotai = seihintounyuujotai;
    }

    /**
     * BINﾎﾞｯｸｽ内の清掃ﾁｪｯｸ
     * @return the binboxseisoucheck
     */
    public String getBinboxseisoucheck() {
        return binboxseisoucheck;
    }

    /**
     * BINﾎﾞｯｸｽ内の清掃ﾁｪｯｸ
     * @param binboxseisoucheck the binboxseisoucheck to set
     */
    public void setBinboxseisoucheck(String binboxseisoucheck) {
        this.binboxseisoucheck = binboxseisoucheck;
    }

    /**
     * ｾｯﾄ者
     * @return the setsya
     */
    public String getSetsya() {
        return setsya;
    }

    /**
     * ｾｯﾄ者
     * @param setsya the setsya to set
     */
    public void setSetsya(String setsya) {
        this.setsya = setsya;
    }

    /**
     * 確認者
     * @return the kakuninsya
     */
    public String getKakuninsya() {
        return kakuninsya;
    }

    /**
     * 確認者
     * @param kakuninsya the kakuninsya to set
     */
    public void setKakuninsya(String kakuninsya) {
        this.kakuninsya = kakuninsya;
    }

    /**
     * 指定公差歩留まり1
     * @return the siteikousabudomari1
     */
    public String getSiteikousabudomari1() {
        return siteikousabudomari1;
    }

    /**
     * 指定公差歩留まり1
     * @param siteikousabudomari1 the siteikousabudomari1 to set
     */
    public void setSiteikousabudomari1(String siteikousabudomari1) {
        this.siteikousabudomari1 = siteikousabudomari1;
    }

    /**
     * 指定公差歩留まり2
     * @return the siteikousabudomari2
     */
    public String getSiteikousabudomari2() {
        return siteikousabudomari2;
    }

    /**
     * 指定公差歩留まり2
     * @param siteikousabudomari2 the siteikousabudomari2 to set
     */
    public void setSiteikousabudomari2(String siteikousabudomari2) {
        this.siteikousabudomari2 = siteikousabudomari2;
    }

    /**
     * ﾃｽﾄﾌﾟﾚｰﾄ管理No
     * @return the testplatekanrino
     */
    public String getTestplatekanrino() {
        return testplatekanrino;
    }

    /**
     * ﾃｽﾄﾌﾟﾚｰﾄ管理No
     * @param testplatekanrino the testplatekanrino to set
     */
    public void setTestplatekanrino(String testplatekanrino) {
        this.testplatekanrino = testplatekanrino;
    }

    /**
     * Tanδ
     * @return the tan
     */
    public BigDecimal getTan() {
        return tan;
    }

    /**
     * Tanδ
     * @param tan the tan to set
     */
    public void setTan(BigDecimal tan) {
        this.tan = tan;
    }

    /**
     * 測定周波数
     * @return the sokuteisyuhasuu
     */
    public String getSokuteisyuhasuu() {
        return sokuteisyuhasuu;
    }

    /**
     * 測定周波数
     * @param sokuteisyuhasuu the sokuteisyuhasuu to set
     */
    public void setSokuteisyuhasuu(String sokuteisyuhasuu) {
        this.sokuteisyuhasuu = sokuteisyuhasuu;
    }

    /**
     * 測定電圧
     * @return the sokuteidenatu
     */
    public BigDecimal getSokuteidenatu() {
        return sokuteidenatu;
    }

    /**
     * 測定電圧
     * @param sokuteidenatu the sokuteidenatu to set
     */
    public void setSokuteidenatu(BigDecimal sokuteidenatu) {
        this.sokuteidenatu = sokuteidenatu;
    }

    /**
     * 補正用ﾁｯﾌﾟ容量
     * @return the hoseiyoutippuyoryou
     */
    public BigDecimal getHoseiyoutippuyoryou() {
        return hoseiyoutippuyoryou;
    }

    /**
     * 補正用ﾁｯﾌﾟ容量
     * @param hoseiyoutippuyoryou the hoseiyoutippuyoryou to set
     */
    public void setHoseiyoutippuyoryou(BigDecimal hoseiyoutippuyoryou) {
        this.hoseiyoutippuyoryou = hoseiyoutippuyoryou;
    }

    /**
     * 補正用ﾁｯﾌﾟTanδ
     * @return the hoseiyoutipputan
     */
    public BigDecimal getHoseiyoutipputan() {
        return hoseiyoutipputan;
    }

    /**
     * 補正用ﾁｯﾌﾟTanδ
     * @param hoseiyoutipputan the hoseiyoutipputan to set
     */
    public void setHoseiyoutipputan(BigDecimal hoseiyoutipputan) {
        this.hoseiyoutipputan = hoseiyoutipputan;
    }

    /**
     * 補正前
     * @return the hoseimae
     */
    public BigDecimal getHoseimae() {
        return hoseimae;
    }

    /**
     * 補正前
     * @param hoseimae the hoseimae to set
     */
    public void setHoseimae(BigDecimal hoseimae) {
        this.hoseimae = hoseimae;
    }

    /**
     * 補正後
     * @return the hoseigo
     */
    public BigDecimal getHoseigo() {
        return hoseigo;
    }

    /**
     * 補正後
     * @param hoseigo the hoseigo to set
     */
    public void setHoseigo(BigDecimal hoseigo) {
        this.hoseigo = hoseigo;
    }

    /**
     * 補正率
     * @return the hoseiritu
     */
    public BigDecimal getHoseiritu() {
        return hoseiritu;
    }

    /**
     * 補正率
     * @param hoseiritu the hoseiritu to set
     */
    public void setHoseiritu(BigDecimal hoseiritu) {
        this.hoseiritu = hoseiritu;
    }

    /**
     * ｽﾀﾝﾀﾞｰﾄﾞ補正
     * @return the standard
     */
    public String getStandard() {
        return standard;
    }

    /**
     * ｽﾀﾝﾀﾞｰﾄﾞ補正
     * @param standard the standard to set
     */
    public void setStandard(String standard) {
        this.standard = standard;
    }

    /**
     * 分類確認
     * @return the bunruikakunin
     */
    public String getBunruikakunin() {
        return bunruikakunin;
    }

    /**
     * 分類確認
     * @param bunruikakunin the bunruikakunin to set
     */
    public void setBunruikakunin(String bunruikakunin) {
        this.bunruikakunin = bunruikakunin;
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
     * 熱処理日時
     * @return the netsusyorinitiji
     */
    public Timestamp getNetsusyorinitiji() {
        return netsusyorinitiji;
    }

    /**
     * 熱処理日時
     * @param netsusyorinitiji the netsusyorinitiji to set
     */
    public void setNetsusyorinitiji(Timestamp netsusyorinitiji) {
        this.netsusyorinitiji = netsusyorinitiji;
    }

    /**
     * ｴｰｼﾞﾝｸﾞ時間
     * @return the agingjikan
     */
    public BigDecimal getAgingjikan() {
        return agingjikan;
    }

    /**
     * ｴｰｼﾞﾝｸﾞ時間
     * @param agingjikan the agingjikan to set
     */
    public void setAgingjikan(BigDecimal agingjikan) {
        this.agingjikan = agingjikan;
    }

    /**
     * 充填率
     * @return the jutenritu
     */
    public String getJutenritu() {
        return jutenritu;
    }

    /**
     * 充填率
     * @param jutenritu the jutenritu to set
     */
    public void setJutenritu(String jutenritu) {
        this.jutenritu = jutenritu;
    }

    /**
     * MC
     * @return the mc
     */
    public String getMc() {
        return mc;
    }

    /**
     * MC
     * @param mc the mc to set
     */
    public void setMc(String mc) {
        this.mc = mc;
    }

    /**
     * 強制排出
     * @return the kyoseihaisyutu
     */
    public String getKyoseihaisyutu() {
        return kyoseihaisyutu;
    }

    /**
     * 強制排出
     * @param kyoseihaisyutu the kyoseihaisyutu to set
     */
    public void setKyoseihaisyutu(String kyoseihaisyutu) {
        this.kyoseihaisyutu = kyoseihaisyutu;
    }

    /**
     * 落下
     * @return the rakka
     */
    public String getRakka() {
        return rakka;
    }

    /**
     * 落下
     * @param rakka the rakka to set
     */
    public void setRakka(String rakka) {
        this.rakka = rakka;
    }

    /**
     * 承認者
     * @return the syoninsha
     */
    public String getSyoninsha() {
        return syoninsha;
    }

    /**
     * 承認者
     * @param syoninsha the syoninsha to set
     */
    public void setSyoninsha(String syoninsha) {
        this.syoninsha = syoninsha;
    }

    /**
     * 振向者
     * @return the furimukesya
     */
    public String getFurimukesya() {
        return furimukesya;
    }

    /**
     * 振向者
     * @param furimukesya the furimukesya to set
     */
    public void setFurimukesya(String furimukesya) {
        this.furimukesya = furimukesya;
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
     * ﾌﾟﾘﾁｬｰｼﾞ条件 PC① 電圧
     * @return the pcdenatu1
     */
    public BigDecimal getPcdenatu1() {
        return pcdenatu1;
    }

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 PC① 電圧
     * @param pcdenatu1 the pcdenatu1 to set
     */
    public void setPcdenatu1(BigDecimal pcdenatu1) {
        this.pcdenatu1 = pcdenatu1;
    }

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 PC① 充電時間
     * @return the pcjudenjikan1
     */
    public Integer getPcjudenjikan1() {
        return pcjudenjikan1;
    }

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 PC① 充電時間
     * @param pcjudenjikan1 the pcjudenjikan1 to set
     */
    public void setPcjudenjikan1(Integer pcjudenjikan1) {
        this.pcjudenjikan1 = pcjudenjikan1;
    }

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 PC② 電圧
     * @return the pcdenatu2
     */
    public BigDecimal getPcdenatu2() {
        return pcdenatu2;
    }

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 PC② 電圧
     * @param pcdenatu2 the pcdenatu2 to set
     */
    public void setPcdenatu2(BigDecimal pcdenatu2) {
        this.pcdenatu2 = pcdenatu2;
    }

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 PC② 充電時間
     * @return the pcjudenjikan2
     */
    public Integer getPcjudenjikan2() {
        return pcjudenjikan2;
    }

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 PC② 充電時間
     * @param pcjudenjikan2 the pcjudenjikan2 to set
     */
    public void setPcjudenjikan2(Integer pcjudenjikan2) {
        this.pcjudenjikan2 = pcjudenjikan2;
    }

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 PC③ 電圧
     * @return the pcdenatu3
     */
    public BigDecimal getPcdenatu3() {
        return pcdenatu3;
    }

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 PC③ 電圧
     * @param pcdenatu3 the pcdenatu3 to set
     */
    public void setPcdenatu3(BigDecimal pcdenatu3) {
        this.pcdenatu3 = pcdenatu3;
    }

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 PC③ 充電時間
     * @return the pcjudenjikan3
     */
    public Integer getPcjudenjikan3() {
        return pcjudenjikan3;
    }

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 PC③ 充電時間
     * @param pcjudenjikan3 the pcjudenjikan3 to set
     */
    public void setPcjudenjikan3(Integer pcjudenjikan3) {
        this.pcjudenjikan3 = pcjudenjikan3;
    }

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 PC④ 電圧
     * @return the pcdenatu4
     */
    public BigDecimal getPcdenatu4() {
        return pcdenatu4;
    }

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 PC④ 電圧
     * @param pcdenatu4 the pcdenatu4 to set
     */
    public void setPcdenatu4(BigDecimal pcdenatu4) {
        this.pcdenatu4 = pcdenatu4;
    }

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 PC④ 充電時間
     * @return the pcjudenjikan4
     */
    public Integer getPcjudenjikan4() {
        return pcjudenjikan4;
    }

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 PC④ 充電時間
     * @param pcjudenjikan4 the pcjudenjikan4 to set
     */
    public void setPcjudenjikan4(Integer pcjudenjikan4) {
        this.pcjudenjikan4 = pcjudenjikan4;
    }

    /**
     * 耐電圧設定条件 IR① 電圧
     * @return the irdenatu1
     */
    public BigDecimal getIrdenatu1() {
        return irdenatu1;
    }

    /**
     * 耐電圧設定条件 IR① 電圧
     * @param irdenatu1 the irdenatu1 to set
     */
    public void setIrdenatu1(BigDecimal irdenatu1) {
        this.irdenatu1 = irdenatu1;
    }

    /**
     * 耐電圧設定条件 IR① 判定値
     * @return the irhanteiti1
     */
    public String getIrhanteiti1() {
        return irhanteiti1;
    }

    /**
     * 耐電圧設定条件 IR① 判定値
     * @param irhanteiti1 the irhanteiti1 to set
     */
    public void setIrhanteiti1(String irhanteiti1) {
        this.irhanteiti1 = irhanteiti1;
    }

    /**
     * 耐電圧設定条件 IR① 充電時間
     * @return the irjudenjikan1
     */
    public Integer getIrjudenjikan1() {
        return irjudenjikan1;
    }

    /**
     * 耐電圧設定条件 IR① 充電時間
     * @param irjudenjikan1 the irjudenjikan1 to set
     */
    public void setIrjudenjikan1(Integer irjudenjikan1) {
        this.irjudenjikan1 = irjudenjikan1;
    }

    /**
     * 耐電圧設定条件 IR② 電圧
     * @return the irdenatu2
     */
    public BigDecimal getIrdenatu2() {
        return irdenatu2;
    }

    /**
     * 耐電圧設定条件 IR② 電圧
     * @param irdenatu2 the irdenatu2 to set
     */
    public void setIrdenatu2(BigDecimal irdenatu2) {
        this.irdenatu2 = irdenatu2;
    }

    /**
     * 耐電圧設定条件 IR② 判定値
     * @return the irhanteiti2
     */
    public String getIrhanteiti2() {
        return irhanteiti2;
    }

    /**
     * 耐電圧設定条件 IR② 判定値
     * @param irhanteiti2 the irhanteiti2 to set
     */
    public void setIrhanteiti2(String irhanteiti2) {
        this.irhanteiti2 = irhanteiti2;
    }

    /**
     * 耐電圧設定条件 IR② 充電時間
     * @return the irjudenjikan2
     */
    public Integer getIrjudenjikan2() {
        return irjudenjikan2;
    }

    /**
     * 耐電圧設定条件 IR② 充電時間
     * @param irjudenjikan2 the irjudenjikan2 to set
     */
    public void setIrjudenjikan2(Integer irjudenjikan2) {
        this.irjudenjikan2 = irjudenjikan2;
    }

    /**
     * 耐電圧設定条件 IR③ 電圧
     * @return the irdenatu3
     */
    public BigDecimal getIrdenatu3() {
        return irdenatu3;
    }

    /**
     * 耐電圧設定条件 IR③ 電圧
     * @param irdenatu3 the irdenatu3 to set
     */
    public void setIrdenatu3(BigDecimal irdenatu3) {
        this.irdenatu3 = irdenatu3;
    }

    /**
     * 耐電圧設定条件 IR③ 判定値
     * @return the irhanteiti3
     */
    public String getIrhanteiti3() {
        return irhanteiti3;
    }

    /**
     * 耐電圧設定条件 IR③ 判定値
     * @param irhanteiti3 the irhanteiti3 to set
     */
    public void setIrhanteiti3(String irhanteiti3) {
        this.irhanteiti3 = irhanteiti3;
    }

    /**
     * 耐電圧設定条件 IR③ 充電時間
     * @return the irjudenjikan3
     */
    public Integer getIrjudenjikan3() {
        return irjudenjikan3;
    }

    /**
     * 耐電圧設定条件 IR③ 充電時間
     * @param irjudenjikan3 the irjudenjikan3 to set
     */
    public void setIrjudenjikan3(Integer irjudenjikan3) {
        this.irjudenjikan3 = irjudenjikan3;
    }

    /**
     * 耐電圧設定条件 IR④ 電圧
     * @return the irdenatu4
     */
    public BigDecimal getIrdenatu4() {
        return irdenatu4;
    }

    /**
     * 耐電圧設定条件 IR④ 電圧
     * @param irdenatu4 the irdenatu4 to set
     */
    public void setIrdenatu4(BigDecimal irdenatu4) {
        this.irdenatu4 = irdenatu4;
    }

    /**
     * 耐電圧設定条件 IR④ 判定値
     * @return the irhanteiti4
     */
    public String getIrhanteiti4() {
        return irhanteiti4;
    }

    /**
     * 耐電圧設定条件 IR④ 判定値
     * @param irhanteiti4 the irhanteiti4 to set
     */
    public void setIrhanteiti4(String irhanteiti4) {
        this.irhanteiti4 = irhanteiti4;
    }

    /**
     * 耐電圧設定条件 IR④ 充電時間
     * @return the irjudenjikan4
     */
    public Integer getIrjudenjikan4() {
        return irjudenjikan4;
    }

    /**
     * 耐電圧設定条件 IR④ 充電時間
     * @param irjudenjikan4 the irjudenjikan4 to set
     */
    public void setIrjudenjikan4(Integer irjudenjikan4) {
        this.irjudenjikan4 = irjudenjikan4;
    }

    /**
     * 耐電圧設定条件 IR⑤ 電圧
     * @return the irdenatu5
     */
    public BigDecimal getIrdenatu5() {
        return irdenatu5;
    }

    /**
     * 耐電圧設定条件 IR⑤ 電圧
     * @param irdenatu5 the irdenatu5 to set
     */
    public void setIrdenatu5(BigDecimal irdenatu5) {
        this.irdenatu5 = irdenatu5;
    }

    /**
     * 耐電圧設定条件 IR⑤ 判定値
     * @return the irhanteiti5
     */
    public String getIrhanteiti5() {
        return irhanteiti5;
    }

    /**
     * 耐電圧設定条件 IR⑤ 判定値
     * @param irhanteiti5 the irhanteiti5 to set
     */
    public void setIrhanteiti5(String irhanteiti5) {
        this.irhanteiti5 = irhanteiti5;
    }

    /**
     * 耐電圧設定条件 IR⑤ 充電時間
     * @return the irjudenjikan5
     */
    public Integer getIrjudenjikan5() {
        return irjudenjikan5;
    }

    /**
     * 耐電圧設定条件 IR⑤ 充電時間
     * @param irjudenjikan5 the irjudenjikan5 to set
     */
    public void setIrjudenjikan5(Integer irjudenjikan5) {
        this.irjudenjikan5 = irjudenjikan5;
    }

    /**
     * 耐電圧設定条件 IR⑥ 電圧
     * @return the irdenatu6
     */
    public BigDecimal getIrdenatu6() {
        return irdenatu6;
    }

    /**
     * 耐電圧設定条件 IR⑥ 電圧
     * @param irdenatu6 the irdenatu6 to set
     */
    public void setIrdenatu6(BigDecimal irdenatu6) {
        this.irdenatu6 = irdenatu6;
    }

    /**
     * 耐電圧設定条件 IR⑥ 判定値
     * @return the irhanteiti6
     */
    public String getIrhanteiti6() {
        return irhanteiti6;
    }

    /**
     * 耐電圧設定条件 IR⑥ 判定値
     * @param irhanteiti6 the irhanteiti6 to set
     */
    public void setIrhanteiti6(String irhanteiti6) {
        this.irhanteiti6 = irhanteiti6;
    }

    /**
     * 耐電圧設定条件 IR⑥ 充電時間
     * @return the irjudenjikan6
     */
    public Integer getIrjudenjikan6() {
        return irjudenjikan6;
    }

    /**
     * 耐電圧設定条件 IR⑥ 充電時間
     * @param irjudenjikan6 the irjudenjikan6 to set
     */
    public void setIrjudenjikan6(Integer irjudenjikan6) {
        this.irjudenjikan6 = irjudenjikan6;
    }

    /**
     * 耐電圧設定条件 IR⑦ 電圧
     * @return the irdenatu7
     */
    public BigDecimal getIrdenatu7() {
        return irdenatu7;
    }

    /**
     * 耐電圧設定条件 IR⑦ 電圧
     * @param irdenatu7 the irdenatu7 to set
     */
    public void setIrdenatu7(BigDecimal irdenatu7) {
        this.irdenatu7 = irdenatu7;
    }

    /**
     * 耐電圧設定条件 IR⑦ 判定値
     * @return the irhanteiti7
     */
    public String getIrhanteiti7() {
        return irhanteiti7;
    }

    /**
     * 耐電圧設定条件 IR⑦ 判定値
     * @param irhanteiti7 the irhanteiti7 to set
     */
    public void setIrhanteiti7(String irhanteiti7) {
        this.irhanteiti7 = irhanteiti7;
    }

    /**
     * 耐電圧設定条件 IR⑦ 充電時間
     * @return the irjudenjikan7
     */
    public Integer getIrjudenjikan7() {
        return irjudenjikan7;
    }

    /**
     * 耐電圧設定条件 IR⑦ 充電時間
     * @param irjudenjikan7 the irjudenjikan7 to set
     */
    public void setIrjudenjikan7(Integer irjudenjikan7) {
        this.irjudenjikan7 = irjudenjikan7;
    }

    /**
     * 耐電圧設定条件 IR⑧ 電圧
     * @return the irdenatu8
     */
    public BigDecimal getIrdenatu8() {
        return irdenatu8;
    }

    /**
     * 耐電圧設定条件 IR⑧ 電圧
     * @param irdenatu8 the irdenatu8 to set
     */
    public void setIrdenatu8(BigDecimal irdenatu8) {
        this.irdenatu8 = irdenatu8;
    }

    /**
     * 耐電圧設定条件 IR⑧ 判定値
     * @return the irhanteiti8
     */
    public String getIrhanteiti8() {
        return irhanteiti8;
    }

    /**
     * 耐電圧設定条件 IR⑧ 判定値
     * @param irhanteiti8 the irhanteiti8 to set
     */
    public void setIrhanteiti8(String irhanteiti8) {
        this.irhanteiti8 = irhanteiti8;
    }

    /**
     * 耐電圧設定条件 IR⑧ 充電時間
     * @return the irjudenjikan8
     */
    public Integer getIrjudenjikan8() {
        return irjudenjikan8;
    }

    /**
     * 耐電圧設定条件 IR⑧ 充電時間
     * @param irjudenjikan8 the irjudenjikan8 to set
     */
    public void setIrjudenjikan8(Integer irjudenjikan8) {
        this.irjudenjikan8 = irjudenjikan8;
    }

    /**
     * BIN1 %区分(設定値)
     * @return the bin1setteiti
     */
    public String getBin1setteiti() {
        return bin1setteiti;
    }

    /**
     * BIN1 %区分(設定値)
     * @param bin1setteiti the bin1setteiti to set
     */
    public void setBin1setteiti(String bin1setteiti) {
        this.bin1setteiti = bin1setteiti;
    }

    /**
     * BIN1 選別区分
     * @return the bin1senbetukubun
     */
    public String getBin1senbetukubun() {
        return bin1senbetukubun;
    }

    /**
     * BIN1 選別区分
     * @param bin1senbetukubun the bin1senbetukubun to set
     */
    public void setBin1senbetukubun(String bin1senbetukubun) {
        this.bin1senbetukubun = bin1senbetukubun;
    }

    /**
     * BIN1 計量後数量
     * @return the bin1keiryougosuryou
     */
    public Integer getBin1keiryougosuryou() {
        return bin1keiryougosuryou;
    }

    /**
     * BIN1 計量後数量
     * @param bin1keiryougosuryou the bin1keiryougosuryou to set
     */
    public void setBin1keiryougosuryou(Integer bin1keiryougosuryou) {
        this.bin1keiryougosuryou = bin1keiryougosuryou;
    }

    /**
     * BIN1 ｶｳﾝﾀｰ数
     * @return the bin1countersuu
     */
    public Integer getBin1countersuu() {
        return bin1countersuu;
    }

    /**
     * BIN1 ｶｳﾝﾀｰ数
     * @param bin1countersuu the bin1countersuu to set
     */
    public void setBin1countersuu(Integer bin1countersuu) {
        this.bin1countersuu = bin1countersuu;
    }

    /**
     * BIN1 誤差率(%)
     * @return the bin1gosaritu
     */
    public BigDecimal getBin1gosaritu() {
        return bin1gosaritu;
    }

    /**
     * BIN1 誤差率(%)
     * @param bin1gosaritu the bin1gosaritu to set
     */
    public void setBin1gosaritu(BigDecimal bin1gosaritu) {
        this.bin1gosaritu = bin1gosaritu;
    }

    /**
     * BIN1 ﾏｼﾝ不良率(%)
     * @return the bin1masinfuryouritu
     */
    public BigDecimal getBin1masinfuryouritu() {
        return bin1masinfuryouritu;
    }

    /**
     * BIN1 ﾏｼﾝ不良率(%)
     * @param bin1masinfuryouritu the bin1masinfuryouritu to set
     */
    public void setBin1masinfuryouritu(BigDecimal bin1masinfuryouritu) {
        this.bin1masinfuryouritu = bin1masinfuryouritu;
    }

    /**
     * BIN1 抜き取り結果
     * @return the bin1nukitorikekkabosuu
     */
    public Integer getBin1nukitorikekkabosuu() {
        return bin1nukitorikekkabosuu;
    }

    /**
     * BIN1 抜き取り結果
     * @param bin1nukitorikekkabosuu the bin1nukitorikekkabosuu to set
     */
    public void setBin1nukitorikekkabosuu(Integer bin1nukitorikekkabosuu) {
        this.bin1nukitorikekkabosuu = bin1nukitorikekkabosuu;
    }

    /**
     * BIN1 抜き取り結果
     * @return the bin1nukitorikekka
     */
    public Integer getBin1nukitorikekka() {
        return bin1nukitorikekka;
    }

    /**
     * BIN1 抜き取り結果
     * @param bin1nukitorikekka the bin1nukitorikekka to set
     */
    public void setBin1nukitorikekka(Integer bin1nukitorikekka) {
        this.bin1nukitorikekka = bin1nukitorikekka;
    }

    /**
     * BIN1 真の不良率(%)
     * @return the bin1sinnofuryouritu
     */
    public BigDecimal getBin1sinnofuryouritu() {
        return bin1sinnofuryouritu;
    }

    /**
     * BIN1 真の不良率(%)
     * @param bin1sinnofuryouritu the bin1sinnofuryouritu to set
     */
    public void setBin1sinnofuryouritu(BigDecimal bin1sinnofuryouritu) {
        this.bin1sinnofuryouritu = bin1sinnofuryouritu;
    }

    /**
     * BIN1 結果ﾁｪｯｸ
     * @return the bin1kekkacheck
     */
    public String getBin1kekkacheck() {
        return bin1kekkacheck;
    }

    /**
     * BIN1 結果ﾁｪｯｸ
     * @param bin1kekkacheck the bin1kekkacheck to set
     */
    public void setBin1kekkacheck(String bin1kekkacheck) {
        this.bin1kekkacheck = bin1kekkacheck;
    }

    /**
     * BIN2 %区分(設定値)
     * @return the bin2setteiti
     */
    public String getBin2setteiti() {
        return bin2setteiti;
    }

    /**
     * BIN2 %区分(設定値)
     * @param bin2setteiti the bin2setteiti to set
     */
    public void setBin2setteiti(String bin2setteiti) {
        this.bin2setteiti = bin2setteiti;
    }

    /**
     * BIN2 選別区分
     * @return the bin2senbetukubun
     */
    public String getBin2senbetukubun() {
        return bin2senbetukubun;
    }

    /**
     * BIN2 選別区分
     * @param bin2senbetukubun the bin2senbetukubun to set
     */
    public void setBin2senbetukubun(String bin2senbetukubun) {
        this.bin2senbetukubun = bin2senbetukubun;
    }

    /**
     * BIN2 計量後数量
     * @return the bin2keiryougosuryou
     */
    public Integer getBin2keiryougosuryou() {
        return bin2keiryougosuryou;
    }

    /**
     * BIN2 計量後数量
     * @param bin2keiryougosuryou the bin2keiryougosuryou to set
     */
    public void setBin2keiryougosuryou(Integer bin2keiryougosuryou) {
        this.bin2keiryougosuryou = bin2keiryougosuryou;
    }

    /**
     * BIN2 ｶｳﾝﾀｰ数
     * @return the bin2countersuu
     */
    public Integer getBin2countersuu() {
        return bin2countersuu;
    }

    /**
     * BIN2 ｶｳﾝﾀｰ数
     * @param bin2countersuu the bin2countersuu to set
     */
    public void setBin2countersuu(Integer bin2countersuu) {
        this.bin2countersuu = bin2countersuu;
    }

    /**
     * BIN2 誤差率(%)
     * @return the bin2gosaritu
     */
    public BigDecimal getBin2gosaritu() {
        return bin2gosaritu;
    }

    /**
     * BIN2 誤差率(%)
     * @param bin2gosaritu the bin2gosaritu to set
     */
    public void setBin2gosaritu(BigDecimal bin2gosaritu) {
        this.bin2gosaritu = bin2gosaritu;
    }

    /**
     * BIN2 ﾏｼﾝ不良率(%)
     * @return the bin2masinfuryouritu
     */
    public BigDecimal getBin2masinfuryouritu() {
        return bin2masinfuryouritu;
    }

    /**
     * BIN2 ﾏｼﾝ不良率(%)
     * @param bin2masinfuryouritu the bin2masinfuryouritu to set
     */
    public void setBin2masinfuryouritu(BigDecimal bin2masinfuryouritu) {
        this.bin2masinfuryouritu = bin2masinfuryouritu;
    }

    /**
     * BIN2 抜き取り結果
     * @return the bin2nukitorikekkabosuu
     */
    public Integer getBin2nukitorikekkabosuu() {
        return bin2nukitorikekkabosuu;
    }

    /**
     * BIN2 抜き取り結果
     * @param bin2nukitorikekkabosuu the bin2nukitorikekkabosuu to set
     */
    public void setBin2nukitorikekkabosuu(Integer bin2nukitorikekkabosuu) {
        this.bin2nukitorikekkabosuu = bin2nukitorikekkabosuu;
    }

    /**
     * BIN2 抜き取り結果
     * @return the bin2nukitorikekka
     */
    public Integer getBin2nukitorikekka() {
        return bin2nukitorikekka;
    }

    /**
     * BIN2 抜き取り結果
     * @param bin2nukitorikekka the bin2nukitorikekka to set
     */
    public void setBin2nukitorikekka(Integer bin2nukitorikekka) {
        this.bin2nukitorikekka = bin2nukitorikekka;
    }

    /**
     * BIN2 真の不良率(%)
     * @return the bin2sinnofuryouritu
     */
    public BigDecimal getBin2sinnofuryouritu() {
        return bin2sinnofuryouritu;
    }

    /**
     * BIN2 真の不良率(%)
     * @param bin2sinnofuryouritu the bin2sinnofuryouritu to set
     */
    public void setBin2sinnofuryouritu(BigDecimal bin2sinnofuryouritu) {
        this.bin2sinnofuryouritu = bin2sinnofuryouritu;
    }

    /**
     * BIN2 結果ﾁｪｯｸ
     * @return the bin2kekkacheck
     */
    public String getBin2kekkacheck() {
        return bin2kekkacheck;
    }

    /**
     * BIN2 結果ﾁｪｯｸ
     * @param bin2kekkacheck the bin2kekkacheck to set
     */
    public void setBin2kekkacheck(String bin2kekkacheck) {
        this.bin2kekkacheck = bin2kekkacheck;
    }

    /**
     * BIN3 %区分(設定値)
     * @return the bin3setteiti
     */
    public String getBin3setteiti() {
        return bin3setteiti;
    }

    /**
     * BIN3 %区分(設定値)
     * @param bin3setteiti the bin3setteiti to set
     */
    public void setBin3setteiti(String bin3setteiti) {
        this.bin3setteiti = bin3setteiti;
    }

    /**
     * BIN3 選別区分
     * @return the bin3senbetukubun
     */
    public String getBin3senbetukubun() {
        return bin3senbetukubun;
    }

    /**
     * BIN3 選別区分
     * @param bin3senbetukubun the bin3senbetukubun to set
     */
    public void setBin3senbetukubun(String bin3senbetukubun) {
        this.bin3senbetukubun = bin3senbetukubun;
    }

    /**
     * BIN3 計量後数量
     * @return the bin3keiryougosuryou
     */
    public Integer getBin3keiryougosuryou() {
        return bin3keiryougosuryou;
    }

    /**
     * BIN3 計量後数量
     * @param bin3keiryougosuryou the bin3keiryougosuryou to set
     */
    public void setBin3keiryougosuryou(Integer bin3keiryougosuryou) {
        this.bin3keiryougosuryou = bin3keiryougosuryou;
    }

    /**
     * BIN3 ｶｳﾝﾀｰ数
     * @return the bin3countersuu
     */
    public Integer getBin3countersuu() {
        return bin3countersuu;
    }

    /**
     * BIN3 ｶｳﾝﾀｰ数
     * @param bin3countersuu the bin3countersuu to set
     */
    public void setBin3countersuu(Integer bin3countersuu) {
        this.bin3countersuu = bin3countersuu;
    }

    /**
     * BIN3 誤差率(%)
     * @return the bin3gosaritu
     */
    public BigDecimal getBin3gosaritu() {
        return bin3gosaritu;
    }

    /**
     * BIN3 誤差率(%)
     * @param bin3gosaritu the bin3gosaritu to set
     */
    public void setBin3gosaritu(BigDecimal bin3gosaritu) {
        this.bin3gosaritu = bin3gosaritu;
    }

    /**
     * BIN3 ﾏｼﾝ不良率(%)
     * @return the bin3masinfuryouritu
     */
    public BigDecimal getBin3masinfuryouritu() {
        return bin3masinfuryouritu;
    }

    /**
     * BIN3 ﾏｼﾝ不良率(%)
     * @param bin3masinfuryouritu the bin3masinfuryouritu to set
     */
    public void setBin3masinfuryouritu(BigDecimal bin3masinfuryouritu) {
        this.bin3masinfuryouritu = bin3masinfuryouritu;
    }

    /**
     * BIN3 抜き取り結果
     * @return the bin3nukitorikekkabosuu
     */
    public Integer getBin3nukitorikekkabosuu() {
        return bin3nukitorikekkabosuu;
    }

    /**
     * BIN3 抜き取り結果
     * @param bin3nukitorikekkabosuu the bin3nukitorikekkabosuu to set
     */
    public void setBin3nukitorikekkabosuu(Integer bin3nukitorikekkabosuu) {
        this.bin3nukitorikekkabosuu = bin3nukitorikekkabosuu;
    }

    /**
     * BIN3 抜き取り結果
     * @return the bin3nukitorikekka
     */
    public Integer getBin3nukitorikekka() {
        return bin3nukitorikekka;
    }

    /**
     * BIN3 抜き取り結果
     * @param bin3nukitorikekka the bin3nukitorikekka to set
     */
    public void setBin3nukitorikekka(Integer bin3nukitorikekka) {
        this.bin3nukitorikekka = bin3nukitorikekka;
    }

    /**
     * BIN3 真の不良率(%)
     * @return the bin3sinnofuryouritu
     */
    public BigDecimal getBin3sinnofuryouritu() {
        return bin3sinnofuryouritu;
    }

    /**
     * BIN3 真の不良率(%)
     * @param bin3sinnofuryouritu the bin3sinnofuryouritu to set
     */
    public void setBin3sinnofuryouritu(BigDecimal bin3sinnofuryouritu) {
        this.bin3sinnofuryouritu = bin3sinnofuryouritu;
    }

    /**
     * BIN3 結果ﾁｪｯｸ
     * @return the bin3kekkacheck
     */
    public String getBin3kekkacheck() {
        return bin3kekkacheck;
    }

    /**
     * BIN3 結果ﾁｪｯｸ
     * @param bin3kekkacheck the bin3kekkacheck to set
     */
    public void setBin3kekkacheck(String bin3kekkacheck) {
        this.bin3kekkacheck = bin3kekkacheck;
    }

    /**
     * BIN4 %区分(設定値)
     * @return the bin4setteiti
     */
    public String getBin4setteiti() {
        return bin4setteiti;
    }

    /**
     * BIN4 %区分(設定値)
     * @param bin4setteiti the bin4setteiti to set
     */
    public void setBin4setteiti(String bin4setteiti) {
        this.bin4setteiti = bin4setteiti;
    }

    /**
     * BIN4 選別区分
     * @return the bin4senbetukubun
     */
    public String getBin4senbetukubun() {
        return bin4senbetukubun;
    }

    /**
     * BIN4 選別区分
     * @param bin4senbetukubun the bin4senbetukubun to set
     */
    public void setBin4senbetukubun(String bin4senbetukubun) {
        this.bin4senbetukubun = bin4senbetukubun;
    }

    /**
     * BIN4 計量後数量
     * @return the bin4keiryougosuryou
     */
    public Integer getBin4keiryougosuryou() {
        return bin4keiryougosuryou;
    }

    /**
     * BIN4 計量後数量
     * @param bin4keiryougosuryou the bin4keiryougosuryou to set
     */
    public void setBin4keiryougosuryou(Integer bin4keiryougosuryou) {
        this.bin4keiryougosuryou = bin4keiryougosuryou;
    }

    /**
     * BIN4 ｶｳﾝﾀｰ数
     * @return the bin4countersuu
     */
    public Integer getBin4countersuu() {
        return bin4countersuu;
    }

    /**
     * BIN4 ｶｳﾝﾀｰ数
     * @param bin4countersuu the bin4countersuu to set
     */
    public void setBin4countersuu(Integer bin4countersuu) {
        this.bin4countersuu = bin4countersuu;
    }

    /**
     * BIN4 誤差率(%)
     * @return the bin4gosaritu
     */
    public BigDecimal getBin4gosaritu() {
        return bin4gosaritu;
    }

    /**
     * BIN4 誤差率(%)
     * @param bin4gosaritu the bin4gosaritu to set
     */
    public void setBin4gosaritu(BigDecimal bin4gosaritu) {
        this.bin4gosaritu = bin4gosaritu;
    }

    /**
     * BIN4 ﾏｼﾝ不良率(%)
     * @return the bin4masinfuryouritu
     */
    public BigDecimal getBin4masinfuryouritu() {
        return bin4masinfuryouritu;
    }

    /**
     * BIN4 ﾏｼﾝ不良率(%)
     * @param bin4masinfuryouritu the bin4masinfuryouritu to set
     */
    public void setBin4masinfuryouritu(BigDecimal bin4masinfuryouritu) {
        this.bin4masinfuryouritu = bin4masinfuryouritu;
    }

    /**
     * BIN4 抜き取り結果
     * @return the bin4nukitorikekkabosuu
     */
    public Integer getBin4nukitorikekkabosuu() {
        return bin4nukitorikekkabosuu;
    }

    /**
     * BIN4 抜き取り結果
     * @param bin4nukitorikekkabosuu the bin4nukitorikekkabosuu to set
     */
    public void setBin4nukitorikekkabosuu(Integer bin4nukitorikekkabosuu) {
        this.bin4nukitorikekkabosuu = bin4nukitorikekkabosuu;
    }

    /**
     * BIN4 抜き取り結果
     * @return the bin4nukitorikekka
     */
    public Integer getBin4nukitorikekka() {
        return bin4nukitorikekka;
    }

    /**
     * BIN4 抜き取り結果
     * @param bin4nukitorikekka the bin4nukitorikekka to set
     */
    public void setBin4nukitorikekka(Integer bin4nukitorikekka) {
        this.bin4nukitorikekka = bin4nukitorikekka;
    }

    /**
     * BIN4 真の不良率(%)
     * @return the bin4sinnofuryouritu
     */
    public BigDecimal getBin4sinnofuryouritu() {
        return bin4sinnofuryouritu;
    }

    /**
     * BIN4 真の不良率(%)
     * @param bin4sinnofuryouritu the bin4sinnofuryouritu to set
     */
    public void setBin4sinnofuryouritu(BigDecimal bin4sinnofuryouritu) {
        this.bin4sinnofuryouritu = bin4sinnofuryouritu;
    }

    /**
     * BIN4 結果ﾁｪｯｸ
     * @return the bin4kekkacheck
     */
    public String getBin4kekkacheck() {
        return bin4kekkacheck;
    }

    /**
     * BIN4 結果ﾁｪｯｸ
     * @param bin4kekkacheck the bin4kekkacheck to set
     */
    public void setBin4kekkacheck(String bin4kekkacheck) {
        this.bin4kekkacheck = bin4kekkacheck;
    }

    /**
     * BIN5 %区分(設定値)
     * @return the bin5setteiti
     */
    public String getBin5setteiti() {
        return bin5setteiti;
    }

    /**
     * BIN5 %区分(設定値)
     * @param bin5setteiti the bin5setteiti to set
     */
    public void setBin5setteiti(String bin5setteiti) {
        this.bin5setteiti = bin5setteiti;
    }

    /**
     * BIN5 選別区分
     * @return the bin5senbetukubun
     */
    public String getBin5senbetukubun() {
        return bin5senbetukubun;
    }

    /**
     * BIN5 選別区分
     * @param bin5senbetukubun the bin5senbetukubun to set
     */
    public void setBin5senbetukubun(String bin5senbetukubun) {
        this.bin5senbetukubun = bin5senbetukubun;
    }

    /**
     * BIN5 計量後数量
     * @return the bin5keiryougosuryou
     */
    public Integer getBin5keiryougosuryou() {
        return bin5keiryougosuryou;
    }

    /**
     * BIN5 計量後数量
     * @param bin5keiryougosuryou the bin5keiryougosuryou to set
     */
    public void setBin5keiryougosuryou(Integer bin5keiryougosuryou) {
        this.bin5keiryougosuryou = bin5keiryougosuryou;
    }

    /**
     * BIN5 ｶｳﾝﾀｰ数
     * @return the bin5countersuu
     */
    public Integer getBin5countersuu() {
        return bin5countersuu;
    }

    /**
     * BIN5 ｶｳﾝﾀｰ数
     * @param bin5countersuu the bin5countersuu to set
     */
    public void setBin5countersuu(Integer bin5countersuu) {
        this.bin5countersuu = bin5countersuu;
    }

    /**
     * BIN5 誤差率(%)
     * @return the bin5gosaritu
     */
    public BigDecimal getBin5gosaritu() {
        return bin5gosaritu;
    }

    /**
     * BIN5 誤差率(%)
     * @param bin5gosaritu the bin5gosaritu to set
     */
    public void setBin5gosaritu(BigDecimal bin5gosaritu) {
        this.bin5gosaritu = bin5gosaritu;
    }

    /**
     * BIN5 ﾏｼﾝ不良率(%)
     * @return the bin5masinfuryouritu
     */
    public BigDecimal getBin5masinfuryouritu() {
        return bin5masinfuryouritu;
    }

    /**
     * BIN5 ﾏｼﾝ不良率(%)
     * @param bin5masinfuryouritu the bin5masinfuryouritu to set
     */
    public void setBin5masinfuryouritu(BigDecimal bin5masinfuryouritu) {
        this.bin5masinfuryouritu = bin5masinfuryouritu;
    }

    /**
     * BIN5 抜き取り結果
     * @return the bin5nukitorikekkabosuu
     */
    public Integer getBin5nukitorikekkabosuu() {
        return bin5nukitorikekkabosuu;
    }

    /**
     * BIN5 抜き取り結果
     * @param bin5nukitorikekkabosuu the bin5nukitorikekkabosuu to set
     */
    public void setBin5nukitorikekkabosuu(Integer bin5nukitorikekkabosuu) {
        this.bin5nukitorikekkabosuu = bin5nukitorikekkabosuu;
    }

    /**
     * BIN5 抜き取り結果
     * @return the bin5nukitorikekka
     */
    public Integer getBin5nukitorikekka() {
        return bin5nukitorikekka;
    }

    /**
     * BIN5 抜き取り結果
     * @param bin5nukitorikekka the bin5nukitorikekka to set
     */
    public void setBin5nukitorikekka(Integer bin5nukitorikekka) {
        this.bin5nukitorikekka = bin5nukitorikekka;
    }

    /**
     * BIN5 真の不良率(%)
     * @return the bin5sinnofuryouritu
     */
    public BigDecimal getBin5sinnofuryouritu() {
        return bin5sinnofuryouritu;
    }

    /**
     * BIN5 真の不良率(%)
     * @param bin5sinnofuryouritu the bin5sinnofuryouritu to set
     */
    public void setBin5sinnofuryouritu(BigDecimal bin5sinnofuryouritu) {
        this.bin5sinnofuryouritu = bin5sinnofuryouritu;
    }

    /**
     * BIN5 結果ﾁｪｯｸ
     * @return the bin5kekkacheck
     */
    public String getBin5kekkacheck() {
        return bin5kekkacheck;
    }

    /**
     * BIN5 結果ﾁｪｯｸ
     * @param bin5kekkacheck the bin5kekkacheck to set
     */
    public void setBin5kekkacheck(String bin5kekkacheck) {
        this.bin5kekkacheck = bin5kekkacheck;
    }

    /**
     * BIN5 袋ﾁｪｯｸ
     * @return the bin5fukurocheck
     */
    public Integer getBin5fukurocheck() {
        return bin5fukurocheck;
    }

    /**
     * BIN5 袋ﾁｪｯｸ
     * @param bin5fukurocheck the bin5fukurocheck to set
     */
    public void setBin5fukurocheck(Integer bin5fukurocheck) {
        this.bin5fukurocheck = bin5fukurocheck;
    }

    /**
     * BIN6 %区分(設定値)
     * @return the bin6setteiti
     */
    public String getBin6setteiti() {
        return bin6setteiti;
    }

    /**
     * BIN6 %区分(設定値)
     * @param bin6setteiti the bin6setteiti to set
     */
    public void setBin6setteiti(String bin6setteiti) {
        this.bin6setteiti = bin6setteiti;
    }

    /**
     * BIN6 選別区分
     * @return the bin6senbetukubun
     */
    public String getBin6senbetukubun() {
        return bin6senbetukubun;
    }

    /**
     * BIN6 選別区分
     * @param bin6senbetukubun the bin6senbetukubun to set
     */
    public void setBin6senbetukubun(String bin6senbetukubun) {
        this.bin6senbetukubun = bin6senbetukubun;
    }

    /**
     * BIN6 計量後数量
     * @return the bin6keiryougosuryou
     */
    public Integer getBin6keiryougosuryou() {
        return bin6keiryougosuryou;
    }

    /**
     * BIN6 計量後数量
     * @param bin6keiryougosuryou the bin6keiryougosuryou to set
     */
    public void setBin6keiryougosuryou(Integer bin6keiryougosuryou) {
        this.bin6keiryougosuryou = bin6keiryougosuryou;
    }

    /**
     * BIN6 ｶｳﾝﾀｰ数
     * @return the bin6countersuu
     */
    public Integer getBin6countersuu() {
        return bin6countersuu;
    }

    /**
     * BIN6 ｶｳﾝﾀｰ数
     * @param bin6countersuu the bin6countersuu to set
     */
    public void setBin6countersuu(Integer bin6countersuu) {
        this.bin6countersuu = bin6countersuu;
    }

    /**
     * BIN6 誤差率(%)
     * @return the bin6gosaritu
     */
    public BigDecimal getBin6gosaritu() {
        return bin6gosaritu;
    }

    /**
     * BIN6 誤差率(%)
     * @param bin6gosaritu the bin6gosaritu to set
     */
    public void setBin6gosaritu(BigDecimal bin6gosaritu) {
        this.bin6gosaritu = bin6gosaritu;
    }

    /**
     * BIN6 ﾏｼﾝ不良率(%)
     * @return the bin6masinfuryouritu
     */
    public BigDecimal getBin6masinfuryouritu() {
        return bin6masinfuryouritu;
    }

    /**
     * BIN6 ﾏｼﾝ不良率(%)
     * @param bin6masinfuryouritu the bin6masinfuryouritu to set
     */
    public void setBin6masinfuryouritu(BigDecimal bin6masinfuryouritu) {
        this.bin6masinfuryouritu = bin6masinfuryouritu;
    }

    /**
     * BIN6 抜き取り結果
     * @return the bin6nukitorikekkabosuu
     */
    public Integer getBin6nukitorikekkabosuu() {
        return bin6nukitorikekkabosuu;
    }

    /**
     * BIN6 抜き取り結果
     * @param bin6nukitorikekkabosuu the bin6nukitorikekkabosuu to set
     */
    public void setBin6nukitorikekkabosuu(Integer bin6nukitorikekkabosuu) {
        this.bin6nukitorikekkabosuu = bin6nukitorikekkabosuu;
    }

    /**
     * BIN6 抜き取り結果
     * @return the bin6nukitorikekka
     */
    public Integer getBin6nukitorikekka() {
        return bin6nukitorikekka;
    }

    /**
     * BIN6 抜き取り結果
     * @param bin6nukitorikekka the bin6nukitorikekka to set
     */
    public void setBin6nukitorikekka(Integer bin6nukitorikekka) {
        this.bin6nukitorikekka = bin6nukitorikekka;
    }

    /**
     * BIN6 真の不良率(%)
     * @return the bin6sinnofuryouritu
     */
    public BigDecimal getBin6sinnofuryouritu() {
        return bin6sinnofuryouritu;
    }

    /**
     * BIN6 真の不良率(%)
     * @param bin6sinnofuryouritu the bin6sinnofuryouritu to set
     */
    public void setBin6sinnofuryouritu(BigDecimal bin6sinnofuryouritu) {
        this.bin6sinnofuryouritu = bin6sinnofuryouritu;
    }

    /**
     * BIN6 結果ﾁｪｯｸ
     * @return the bin6kekkacheck
     */
    public String getBin6kekkacheck() {
        return bin6kekkacheck;
    }

    /**
     * BIN6 結果ﾁｪｯｸ
     * @param bin6kekkacheck the bin6kekkacheck to set
     */
    public void setBin6kekkacheck(String bin6kekkacheck) {
        this.bin6kekkacheck = bin6kekkacheck;
    }

    /**
     * BIN6 袋ﾁｪｯｸ
     * @return the bin6fukurocheck
     */
    public Integer getBin6fukurocheck() {
        return bin6fukurocheck;
    }

    /**
     * BIN6 袋ﾁｪｯｸ
     * @param bin6fukurocheck the bin6fukurocheck to set
     */
    public void setBin6fukurocheck(Integer bin6fukurocheck) {
        this.bin6fukurocheck = bin6fukurocheck;
    }

    /**
     * BIN7 %区分(設定値)
     * @return the bin7setteiti
     */
    public String getBin7setteiti() {
        return bin7setteiti;
    }

    /**
     * BIN7 %区分(設定値)
     * @param bin7setteiti the bin7setteiti to set
     */
    public void setBin7setteiti(String bin7setteiti) {
        this.bin7setteiti = bin7setteiti;
    }

    /**
     * BIN7 選別区分
     * @return the bin7senbetukubun
     */
    public String getBin7senbetukubun() {
        return bin7senbetukubun;
    }

    /**
     * BIN7 選別区分
     * @param bin7senbetukubun the bin7senbetukubun to set
     */
    public void setBin7senbetukubun(String bin7senbetukubun) {
        this.bin7senbetukubun = bin7senbetukubun;
    }

    /**
     * BIN7 計量後数量
     * @return the bin7keiryougosuryou
     */
    public Integer getBin7keiryougosuryou() {
        return bin7keiryougosuryou;
    }

    /**
     * BIN7 計量後数量
     * @param bin7keiryougosuryou the bin7keiryougosuryou to set
     */
    public void setBin7keiryougosuryou(Integer bin7keiryougosuryou) {
        this.bin7keiryougosuryou = bin7keiryougosuryou;
    }

    /**
     * BIN7 ｶｳﾝﾀｰ数
     * @return the bin7countersuu
     */
    public Integer getBin7countersuu() {
        return bin7countersuu;
    }

    /**
     * BIN7 ｶｳﾝﾀｰ数
     * @param bin7countersuu the bin7countersuu to set
     */
    public void setBin7countersuu(Integer bin7countersuu) {
        this.bin7countersuu = bin7countersuu;
    }

    /**
     * BIN7 誤差率(%)
     * @return the bin7gosaritu
     */
    public BigDecimal getBin7gosaritu() {
        return bin7gosaritu;
    }

    /**
     * BIN7 誤差率(%)
     * @param bin7gosaritu the bin7gosaritu to set
     */
    public void setBin7gosaritu(BigDecimal bin7gosaritu) {
        this.bin7gosaritu = bin7gosaritu;
    }

    /**
     * BIN7 ﾏｼﾝ不良率(%)
     * @return the bin7masinfuryouritu
     */
    public BigDecimal getBin7masinfuryouritu() {
        return bin7masinfuryouritu;
    }

    /**
     * BIN7 ﾏｼﾝ不良率(%)
     * @param bin7masinfuryouritu the bin7masinfuryouritu to set
     */
    public void setBin7masinfuryouritu(BigDecimal bin7masinfuryouritu) {
        this.bin7masinfuryouritu = bin7masinfuryouritu;
    }

    /**
     * BIN7 袋ﾁｪｯｸ
     * @return the bin7fukurocheck
     */
    public Integer getBin7fukurocheck() {
        return bin7fukurocheck;
    }

    /**
     * BIN7 袋ﾁｪｯｸ
     * @param bin7fukurocheck the bin7fukurocheck to set
     */
    public void setBin7fukurocheck(Integer bin7fukurocheck) {
        this.bin7fukurocheck = bin7fukurocheck;
    }

    /**
     * BIN8 %区分(設定値)
     * @return the bin8setteiti
     */
    public String getBin8setteiti() {
        return bin8setteiti;
    }

    /**
     * BIN8 %区分(設定値)
     * @param bin8setteiti the bin8setteiti to set
     */
    public void setBin8setteiti(String bin8setteiti) {
        this.bin8setteiti = bin8setteiti;
    }

    /**
     * BIN8 選別区分
     * @return the bin8senbetukubun
     */
    public String getBin8senbetukubun() {
        return bin8senbetukubun;
    }

    /**
     * BIN8 選別区分
     * @param bin8senbetukubun the bin8senbetukubun to set
     */
    public void setBin8senbetukubun(String bin8senbetukubun) {
        this.bin8senbetukubun = bin8senbetukubun;
    }

    /**
     * BIN8 計量後数量
     * @return the bin8keiryougosuryou
     */
    public Integer getBin8keiryougosuryou() {
        return bin8keiryougosuryou;
    }

    /**
     * BIN8 計量後数量
     * @param bin8keiryougosuryou the bin8keiryougosuryou to set
     */
    public void setBin8keiryougosuryou(Integer bin8keiryougosuryou) {
        this.bin8keiryougosuryou = bin8keiryougosuryou;
    }

    /**
     * BIN8 ｶｳﾝﾀｰ数
     * @return the bin8countersuu
     */
    public Integer getBin8countersuu() {
        return bin8countersuu;
    }

    /**
     * BIN8 ｶｳﾝﾀｰ数
     * @param bin8countersuu the bin8countersuu to set
     */
    public void setBin8countersuu(Integer bin8countersuu) {
        this.bin8countersuu = bin8countersuu;
    }

    /**
     * BIN8 誤差率(%)
     * @return the bin8gosaritu
     */
    public BigDecimal getBin8gosaritu() {
        return bin8gosaritu;
    }

    /**
     * BIN8 誤差率(%)
     * @param bin8gosaritu the bin8gosaritu to set
     */
    public void setBin8gosaritu(BigDecimal bin8gosaritu) {
        this.bin8gosaritu = bin8gosaritu;
    }

    /**
     * BIN8 ﾏｼﾝ不良率(%)
     * @return the bin8masinfuryouritu
     */
    public BigDecimal getBin8masinfuryouritu() {
        return bin8masinfuryouritu;
    }

    /**
     * BIN8 ﾏｼﾝ不良率(%)
     * @param bin8masinfuryouritu the bin8masinfuryouritu to set
     */
    public void setBin8masinfuryouritu(BigDecimal bin8masinfuryouritu) {
        this.bin8masinfuryouritu = bin8masinfuryouritu;
    }

    /**
     * BIN8 袋ﾁｪｯｸ
     * @return the bin8fukurocheck
     */
    public Integer getBin8fukurocheck() {
        return bin8fukurocheck;
    }

    /**
     * BIN8 袋ﾁｪｯｸ
     * @param bin8fukurocheck the bin8fukurocheck to set
     */
    public void setBin8fukurocheck(Integer bin8fukurocheck) {
        this.bin8fukurocheck = bin8fukurocheck;
    }

    /**
     * BIN9 強制排出 計量後数量
     * @return the bin9keiryougosuryou
     */
    public Integer getBin9keiryougosuryou() {
        return bin9keiryougosuryou;
    }

    /**
     * BIN9 強制排出 計量後数量
     * @param bin9keiryougosuryou the bin9keiryougosuryou to set
     */
    public void setBin9keiryougosuryou(Integer bin9keiryougosuryou) {
        this.bin9keiryougosuryou = bin9keiryougosuryou;
    }

    /**
     * BIN9 強制排出 ﾏｼﾝ不良率
     * @return the bin9masinfuryouritu
     */
    public BigDecimal getBin9masinfuryouritu() {
        return bin9masinfuryouritu;
    }

    /**
     * BIN9 強制排出 ﾏｼﾝ不良率
     * @param bin9masinfuryouritu the bin9masinfuryouritu to set
     */
    public void setBin9masinfuryouritu(BigDecimal bin9masinfuryouritu) {
        this.bin9masinfuryouritu = bin9masinfuryouritu;
    }

    /**
     * 落下 計量後数量
     * @return the rakkakeiryougosuryou
     */
    public Integer getRakkakeiryougosuryou() {
        return rakkakeiryougosuryou;
    }

    /**
     * 落下 計量後数量
     * @param rakkakeiryougosuryou the rakkakeiryougosuryou to set
     */
    public void setRakkakeiryougosuryou(Integer rakkakeiryougosuryou) {
        this.rakkakeiryougosuryou = rakkakeiryougosuryou;
    }

    /**
     * 落下 ﾏｼﾝ不良率
     * @return the rakkamasinfuryouritu
     */
    public BigDecimal getRakkamasinfuryouritu() {
        return rakkamasinfuryouritu;
    }

    /**
     * 落下 ﾏｼﾝ不良率
     * @param rakkamasinfuryouritu the rakkamasinfuryouritu to set
     */
    public void setRakkamasinfuryouritu(BigDecimal rakkamasinfuryouritu) {
        this.rakkamasinfuryouritu = rakkamasinfuryouritu;
    }

    /**
     * 半田ｻﾝﾌﾟﾙ
     * @return the handasample
     */
    public String getHandasample() {
        return handasample;
    }

    /**
     * 半田ｻﾝﾌﾟﾙ
     * @param handasample the handasample to set
     */
    public void setHandasample(String handasample) {
        this.handasample = handasample;
    }

    /**
     * 信頼性ｻﾝﾌﾟﾙ
     * @return the sinraiseisample
     */
    public String getSinraiseisample() {
        return sinraiseisample;
    }

    /**
     * 信頼性ｻﾝﾌﾟﾙ
     * @param sinraiseisample the sinraiseisample to set
     */
    public void setSinraiseisample(String sinraiseisample) {
        this.sinraiseisample = sinraiseisample;
    }

    /**
     * 真不良判定者
     * @return the sinfuryouhanteisya
     */
    public String getSinfuryouhanteisya() {
        return sinfuryouhanteisya;
    }

    /**
     * 真不良判定者
     * @param sinfuryouhanteisya the sinfuryouhanteisya to set
     */
    public void setSinfuryouhanteisya(String sinfuryouhanteisya) {
        this.sinfuryouhanteisya = sinfuryouhanteisya;
    }

    /**
     * 判定入力者
     * @return the hanteinyuuryokusya
     */
    public String getHanteinyuuryokusya() {
        return hanteinyuuryokusya;
    }

    /**
     * 判定入力者
     * @param hanteinyuuryokusya the hanteinyuuryokusya to set
     */
    public void setHanteinyuuryokusya(String hanteinyuuryokusya) {
        this.hanteinyuuryokusya = hanteinyuuryokusya;
    }

    /**
     * 取出者
     * @return the toridasisya
     */
    public String getToridasisya() {
        return toridasisya;
    }

    /**
     * 取出者
     * @param toridasisya the toridasisya to set
     */
    public void setToridasisya(String toridasisya) {
        this.toridasisya = toridasisya;
    }

    /**
     * 公差①
     * @return the kousa1
     */
    public String getKousa1() {
        return kousa1;
    }

    /**
     * 公差①
     * @param kousa1 the kousa1 to set
     */
    public void setKousa1(String kousa1) {
        this.kousa1 = kousa1;
    }

    /**
     * 重量①
     * @return the juryou1
     */
    public BigDecimal getJuryou1() {
        return juryou1;
    }

    /**
     * 重量①
     * @param juryou1 the juryou1 to set
     */
    public void setJuryou1(BigDecimal juryou1) {
        this.juryou1 = juryou1;
    }

    /**
     * 個数①
     * @return the kosuu1
     */
    public Integer getKosuu1() {
        return kosuu1;
    }

    /**
     * 個数①
     * @param kosuu1 the kosuu1 to set
     */
    public void setKosuu1(Integer kosuu1) {
        this.kosuu1 = kosuu1;
    }

    /**
     * 公差②
     * @return the kousa2
     */
    public String getKousa2() {
        return kousa2;
    }

    /**
     * 公差②
     * @param kousa2 the kousa2 to set
     */
    public void setKousa2(String kousa2) {
        this.kousa2 = kousa2;
    }

    /**
     * 重量②
     * @return the juryou2
     */
    public BigDecimal getJuryou2() {
        return juryou2;
    }

    /**
     * 重量②
     * @param juryou2 the juryou2 to set
     */
    public void setJuryou2(BigDecimal juryou2) {
        this.juryou2 = juryou2;
    }

    /**
     * 個数②
     * @return the kosuu2
     */
    public Integer getKosuu2() {
        return kosuu2;
    }

    /**
     * 個数②
     * @param kosuu2 the kosuu2 to set
     */
    public void setKosuu2(Integer kosuu2) {
        this.kosuu2 = kosuu2;
    }

    /**
     * 公差③
     * @return the kousa3
     */
    public String getKousa3() {
        return kousa3;
    }

    /**
     * 公差③
     * @param kousa3 the kousa3 to set
     */
    public void setKousa3(String kousa3) {
        this.kousa3 = kousa3;
    }

    /**
     * 重量③
     * @return the juryou3
     */
    public BigDecimal getJuryou3() {
        return juryou3;
    }

    /**
     * 重量③
     * @param juryou3 the juryou3 to set
     */
    public void setJuryou3(BigDecimal juryou3) {
        this.juryou3 = juryou3;
    }

    /**
     * 個数③
     * @return the kosuu3
     */
    public Integer getKosuu3() {
        return kosuu3;
    }

    /**
     * 個数③
     * @param kosuu3 the kosuu3 to set
     */
    public void setKosuu3(Integer kosuu3) {
        this.kosuu3 = kosuu3;
    }

    /**
     * 公差④
     * @return the kousa4
     */
    public String getKousa4() {
        return kousa4;
    }

    /**
     * 公差④
     * @param kousa4 the kousa4 to set
     */
    public void setKousa4(String kousa4) {
        this.kousa4 = kousa4;
    }

    /**
     * 重量④
     * @return the juryou4
     */
    public BigDecimal getJuryou4() {
        return juryou4;
    }

    /**
     * 重量④
     * @param juryou4 the juryou4 to set
     */
    public void setJuryou4(BigDecimal juryou4) {
        this.juryou4 = juryou4;
    }

    /**
     * 個数④
     * @return the kosuu4
     */
    public Integer getKosuu4() {
        return kosuu4;
    }

    /**
     * 個数④
     * @param kosuu4 the kosuu4 to set
     */
    public void setKosuu4(Integer kosuu4) {
        this.kosuu4 = kosuu4;
    }

    /**
     * ｶｳﾝﾀｰ総数
     * @return the countersousuu
     */
    public Integer getCountersousuu() {
        return countersousuu;
    }

    /**
     * ｶｳﾝﾀｰ総数
     * @param countersousuu the countersousuu to set
     */
    public void setCountersousuu(Integer countersousuu) {
        this.countersousuu = countersousuu;
    }

    /**
     * 良品重量
     * @return the ryohinjuryou
     */
    public BigDecimal getRyohinjuryou() {
        return ryohinjuryou;
    }

    /**
     * 良品重量
     * @param ryohinjuryou the ryohinjuryou to set
     */
    public void setRyohinjuryou(BigDecimal ryohinjuryou) {
        this.ryohinjuryou = ryohinjuryou;
    }

    /**
     * 良品個数
     * @return the ryohinkosuu
     */
    public Integer getRyohinkosuu() {
        return ryohinkosuu;
    }

    /**
     * 良品個数
     * @param ryohinkosuu the ryohinkosuu to set
     */
    public void setRyohinkosuu(Integer ryohinkosuu) {
        this.ryohinkosuu = ryohinkosuu;
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
     * BIN確認者
     * @return the binkakuninsya
     */
    public String getBinkakuninsya() {
        return binkakuninsya;
    }

    /**
     * BIN確認者
     * @param binkakuninsya the binkakuninsya to set
     */
    public void setBinkakuninsya(String binkakuninsya) {
        this.binkakuninsya = binkakuninsya;
    }

    /**
     * 電気特性再検
     * @return the saiken
     */
    public String getSaiken() {
        return saiken;
    }

    /**
     * 電気特性再検
     * @param saiken the saiken to set
     */
    public void setSaiken(String saiken) {
        this.saiken = saiken;
    }

    /**
     * 設備区分
     * @return the setubikubun
     */
    public String getSetubikubun() {
        return setubikubun;
    }

    /**
     * 設備区分
     * @param setubikubun the setubikubun to set
     */
    public void setSetubikubun(String setubikubun) {
        this.setubikubun = setubikubun;
    }

    /**
     * 登録日時
     * @return the torokunichiji
     */
    public Timestamp getTorokunichiji() {
        return torokunichiji;
    }

    /**
     * 登録日時
     * @param torokunichiji the torokunichiji to set
     */
    public void setTorokunichiji(Timestamp torokunichiji) {
        this.torokunichiji = torokunichiji;
    }

    /**
     * 更新日時
     * @return the kosinnichiji
     */
    public Timestamp getKosinnichiji() {
        return kosinnichiji;
    }

    /**
     * 更新日時
     * @param kosinnichiji the kosinnichiji to set
     */
    public void setKosinnichiji(Timestamp kosinnichiji) {
        this.kosinnichiji = kosinnichiji;
    }

    /**
     * revision
     * @return the revision
     */
    public Long getRevision() {
        return revision;
    }

    /**
     * revision
     * @param revision the revision to set
     */
    public void setRevision(Long revision) {
        this.revision = revision;
    }

    /**
     * 削除ﾌﾗｸﾞ
     * @return the deleteflag
     */
    public Integer getDeleteflag() {
        return deleteflag;
    }

    /**
     * 削除ﾌﾗｸﾞ
     * @param deleteflag the deleteflag to set
     */
    public void setDeleteflag(Integer deleteflag) {
        this.deleteflag = deleteflag;
    }

}
