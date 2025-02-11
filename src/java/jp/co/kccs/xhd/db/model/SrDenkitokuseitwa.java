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
 * ===============================================================================<br>
 */
/**
 * sr_denkitokuseitwa(電気特性)のモデルクラスです。
 *
 * @author N.Ichiki
 * @since 2025/01/10
 */
public class SrDenkitokuseitwa {

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
     * 最大処理数
     */
    private Integer saidaisyorisuu;
    
    /**
     * 累計処理数
     */
    private Integer ruikeisyorisuu;
    
    
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
    private BigDecimal bunruiairatu;

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
     * 選別順序変更
     */
    private String senbetujunjo;

    /**
     * 設定ﾓｰﾄﾞ確認
     */
    private String setteikakunin;

    /**
     * 配線確認
     */
    private String haisenkakunin;

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
     * 指定公差歩留まり3
     */
    private String siteikousabudomari3;

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
    private BigDecimal irhanteiti1;

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
    private BigDecimal irhanteiti2;

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
    private BigDecimal irhanteiti3;

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
    private BigDecimal irhanteiti4;

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
    private BigDecimal irhanteiti5;

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
    private BigDecimal irhanteiti6;

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
    private BigDecimal irhanteiti7;

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
    private BigDecimal irhanteiti8;

    /**
     * 耐電圧設定条件 IR⑧ 充電時間
     */
    private Integer irjudenjikan8;
    
    /**
     * RDC1 ﾚﾝｼﾞ
     */
    private BigDecimal rdcrange1;

    /**
     * RDC1 判定値
     */
    private BigDecimal rdchantei1;

    /**
     * RDC2 ﾚﾝｼﾞ
     */
    private BigDecimal rdcrange2;

    /**
     * RDC2 判定値
     */
    private BigDecimal rdchantei2;

    /**
     * DROP1,3 PC
     */
    private BigDecimal drop13pc;

    /**
     * DROP1,3 PS
     */
    private BigDecimal drop13ps;

    /**
     * DROP1,3 MS･DC
     */
    private BigDecimal drop13msdc;

    /**
     * DROP2,4 PC
     */
    private BigDecimal drop24pc;

    /**
     * DROP2,4 PS
     */
    private BigDecimal drop24ps;

    /**
     * DROP2,4 MS･DC
     */
    private BigDecimal drop24msdc;

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
     * BIN1 袋ﾁｪｯｸ
     */
    private Integer bin1fukurocheck;

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
     * BIN2 袋ﾁｪｯｸ
     */
    private Integer bin2fukurocheck;

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
     * BIN3 袋ﾁｪｯｸ
     */
    private Integer bin3fukurocheck;

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
     * BIN4 袋ﾁｪｯｸ
     */
    private Integer bin4fukurocheck;

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
     * BIN7 抜き取り結果
     */
    private Integer bin7nukitorikekkabosuu;

    /**
     * BIN7 抜き取り結果
     */
    private Integer bin7nukitorikekka;

    /**
     * BIN7 真の不良率(%)
     */
    private BigDecimal bin7sinnofuryouritu;

    /**
     * BIN7 結果ﾁｪｯｸ
     */
    private String bin7kekkacheck;

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
     * BIN8 抜き取り結果
     */
    private Integer bin8nukitorikekkabosuu;

    /**
     * BIN8 抜き取り結果
     */
    private Integer bin8nukitorikekka;

    /**
     * BIN8 真の不良率(%)
     */
    private BigDecimal bin8sinnofuryouritu;

    /**
     * BIN8 結果ﾁｪｯｸ
     */
    private String bin8kekkacheck;

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
     * SATｻﾝﾌﾟﾙ
     */
    private String satsample;

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
     * 耐電圧設定条件 IR① 判定値(低)
     */
    private BigDecimal irhanteiti1low;

    /**
     * 耐電圧設定条件 IR① 判定値 単位
     */
    private String irhanteiti1tani;

    /**
     * 耐電圧設定条件 IR② 判定値(低)
     */
    private BigDecimal irhanteiti2low;

    /**
     * 耐電圧設定条件 IR② 判定値 単位
     */
    private String irhanteiti2tani;

    /**
     * 耐電圧設定条件 IR③ 判定値(低)
     */
    private BigDecimal irhanteiti3low;

    /**
     * 耐電圧設定条件 IR③ 判定値 単位
     */
    private String irhanteiti3tani;

    /**
     * 耐電圧設定条件 IR④ 判定値(低)
     */
    private BigDecimal irhanteiti4low;

    /**
     * 耐電圧設定条件 IR④ 判定値 単位
     */
    private String irhanteiti4tani;

    /**
     * 耐電圧設定条件 IR⑤ 判定値(低)
     */
    private BigDecimal irhanteiti5low;

    /**
     * 耐電圧設定条件 IR⑤ 判定値 単位
     */
    private String irhanteiti5tani;

    /**
     * 耐電圧設定条件 IR⑥ 判定値(低)
     */
    private BigDecimal irhanteiti6low;

    /**
     * 耐電圧設定条件 IR⑥ 判定値 単位
     */
    private String irhanteiti6tani;

    /**
     * 耐電圧設定条件 IR⑦ 判定値(低)
     */
    private BigDecimal irhanteiti7low;

    /**
     * 耐電圧設定条件 IR⑦ 判定値 単位
     */
    private String irhanteiti7tani;

    /**
     * 耐電圧設定条件 IR⑧ 判定値(低)
     */
    private BigDecimal irhanteiti8low;

    /**
     * 耐電圧設定条件 IR⑧ 判定値 単位
     */
    private String irhanteiti8tani;

    /**
     * BIN1 ｶｳﾝﾀｰ数2
     */
    private Integer bin1countersuu2;

    /**
     * BIN1 ｶｳﾝﾀｰ数3
     */
    private Integer bin1countersuu3;

    /**
     * BIN2 ｶｳﾝﾀｰ数2
     */
    private Integer bin2countersuu2;

    /**
     * BIN2 ｶｳﾝﾀｰ数3
     */
    private Integer bin2countersuu3;

    /**
     * BIN3 ｶｳﾝﾀｰ数2
     */
    private Integer bin3countersuu2;

    /**
     * BIN3 ｶｳﾝﾀｰ数3
     */
    private Integer bin3countersuu3;

    /**
     * BIN4 ｶｳﾝﾀｰ数2
     */
    private Integer bin4countersuu2;

    /**
     * BIN4 ｶｳﾝﾀｰ数3
     */
    private Integer bin4countersuu3;

    /**
     * BIN5 ｶｳﾝﾀｰ数2
     */
    private Integer bin5countersuu2;

    /**
     * BIN5 ｶｳﾝﾀｰ数3
     */
    private Integer bin5countersuu3;

    /**
     * BIN6 ｶｳﾝﾀｰ数2
     */
    private Integer bin6countersuu2;

    /**
     * BIN6 ｶｳﾝﾀｰ数3
     */
    private Integer bin6countersuu3;

    /**
     * BIN7 ｶｳﾝﾀｰ数2
     */
    private Integer bin7countersuu2;

    /**
     * BIN7 ｶｳﾝﾀｰ数3
     */
    private Integer bin7countersuu3;

    /**
     * BIN8 ｶｳﾝﾀｰ数2
     */
    private Integer bin8countersuu2;

    /**
     * BIN8 ｶｳﾝﾀｰ数3
     */
    private Integer bin8countersuu3;

    /**
     * 同品種
     */
    private Integer douhinsyu;

    /**
     * 耐電圧設定条件 IR① 判定値(低) 単位
     */
    private String irhanteiti1tanilow;

    /**
     * 耐電圧設定条件 IR② 判定値(低) 単位
     */
    private String irhanteiti2tanilow;

    /**
     * 耐電圧設定条件 IR③ 判定値(低) 単位
     */
    private String irhanteiti3tanilow;

    /**
     * 耐電圧設定条件 IR④ 判定値(低) 単位
     */
    private String irhanteiti4tanilow;

    /**
     * 耐電圧設定条件 IR⑤ 判定値(低) 単位
     */
    private String irhanteiti5tanilow;

    /**
     * 耐電圧設定条件 IR⑥ 判定値(低) 単位
     */
    private String irhanteiti6tanilow;

    /**
     * 耐電圧設定条件 IR⑦ 判定値(低) 単位
     */
    private String irhanteiti7tanilow;

    /**
     * 耐電圧設定条件 IR⑧ 判定値(低) 単位
     */
    private String irhanteiti8tanilow;

    /**
     * 削除ﾌﾗｸﾞ
     */
    private Integer deleteflag;
    
    /**
     * 原点復帰動作
     */
    private String gentenhukkidousa;
    
    /**
     * 測定機1,2動作確認
     */
    private String sokuteiki12dousakakunin;
    
    /**
     * 測定ピン(フロント)外観
     */
    private String sokuteipinfront;
    
    /**
     * 測定ピン(リア)外観
     */
    private String sokuteipinrear;
    
    /**
     * IR① 電流中心値スタート
     */
    private BigDecimal ir1denryustart;
    
    /**
     * IR① 電流中心値スタート 単位
     */
    private String ir1denryustarttani;
    
    /**
     * IR① 電流中心値エンド
     */
    private BigDecimal ir1denryuend;
    
    /**
     * IR① 電流中心値エンド 単位
     */
    private String ir1denryuendtani;
    
    /**
     * IR① 測定範囲スタート
     */
    private BigDecimal ir1sokuteihanistart;
    
    /**
     * IR① 測定範囲スタート 単位
     */
    private String ir1sokuteihanistarttani;
    
    /**
     * IR① 測定範囲エンド
     */
    private BigDecimal ir1sokuteihaniend;
    
    /**
     * IR① 測定範囲エンド 単位
     */
    private String ir1sokuteihaniendtani;
    
    /**
     * IR② 電流中心値スタート
     */
    private BigDecimal ir2denryustart;
    
    /**
     * IR② 電流中心値スタート 単位
     */
    private String ir2denryustarttani;
    
    /**
     * IR② 電流中心値エンド
     */
    private BigDecimal ir2denryuend;
    
    /**
     * IR② 電流中心値エンド 単位
     */
    private String ir2denryuendtani;
    
    /**
     * IR② 測定範囲スタート
     */
    private BigDecimal ir2sokuteihanistart;
    
    /**
     * IR② 測定範囲スタート 単位
     */
    private String ir2sokuteihanistarttani;
    
    /**
     * IR② 測定範囲エンド
     */
    private BigDecimal ir2sokuteihaniend;
    
    /**
     * IR② 測定範囲エンド 単位
     */
    private String ir2sokuteihaniendtani;
    
    /**
     * IR③ 電流中心値スタート
     */
    private BigDecimal ir3denryustart;
    
    /**
     * IR③ 電流中心値スタート 単位
     */
    private String ir3denryustarttani;
    
    /**
     * IR③ 電流中心値エンド
     */
    private BigDecimal ir3denryuend;
    
    /**
     * IR③ 電流中心値エンド 単位
     */
    private String ir3denryuendtani;
    
    /**
     * IR③ 測定範囲スタート
     */
    private BigDecimal ir3sokuteihanistart;
    
    /**
     * IR③ 測定範囲スタート 単位
     */
    private String ir3sokuteihanistarttani;
    
    /**
     * IR③ 測定範囲エンド
     */
    private BigDecimal ir3sokuteihaniend;
    
    /**
     * IR③ 測定範囲エンド 単位
     */
    private String ir3sokuteihaniendtani;
    
    
    /**
     * IR④ 電流中心値スタート
     */
    private BigDecimal ir4denryustart;
    
    /**
     * IR④ 電流中心値スタート 単位
     */
    private String ir4denryustarttani;
    
    /**
     * IR④ 電流中心値エンド
     */
    private BigDecimal ir4denryuend;
    
    /**
     * IR④ 電流中心値エンド 単位
     */
    private String ir4denryuendtani;
    
    /**
     * IR④ 測定範囲スタート
     */
    private BigDecimal ir4sokuteihanistart;
    
    /**
     * IR④ 測定範囲スタート 単位
     */
    private String ir4sokuteihanistarttani;
    
    /**
     * IR④ 測定範囲エンド
     */
    private BigDecimal ir4sokuteihaniend;
    
    /**
     * IR④ 測定範囲エンド 単位
     */
    private String ir4sokuteihaniendtani;
    
    /**
     * IR⑤ 電流中心値スタート
     */
    private BigDecimal ir5denryustart;
    
    /**
     * IR⑤ 電流中心値スタート 単位
     */
    private String ir5denryustarttani;
    
    /**
     * IR⑤ 電流中心値エンド
     */
    private BigDecimal ir5denryuend;
    
    /**
     * IR⑤ 電流中心値エンド 単位
     */
    private String ir5denryuendtani;
    
    /**
     * IR⑤ 測定範囲スタート
     */
    private BigDecimal ir5sokuteihanistart;
    
    /**
     * IR⑤ 測定範囲スタート 単位
     */
    private String ir5sokuteihanistarttani;
    
    /**
     * IR⑤ 測定範囲エンド
     */
    private BigDecimal ir5sokuteihaniend;
    
    /**
     * IR⑤ 測定範囲エンド 単位
     */
    private String ir5sokuteihaniendtani;
    
    /**
     * IR⑥ 電流中心値スタート
     */
    private BigDecimal ir6denryustart;
    
    /**
     * IR⑥ 電流中心値スタート 単位
     */
    private String ir6denryustarttani;
    
    /**
     * IR⑥ 電流中心値エンド
     */
    private BigDecimal ir6denryuend;
    
    /**
     * IR⑥ 電流中心値エンド 単位
     */
    private String ir6denryuendtani;
    
    /**
     * IR⑥ 測定範囲スタート
     */
    private BigDecimal ir6sokuteihanistart;
    
    /**
     * IR⑥ 測定範囲スタート 単位
     */
    private String ir6sokuteihanistarttani;
    
    /**
     * IR⑥ 測定範囲エンド
     */
    private BigDecimal ir6sokuteihaniend;
    
    /**
     * IR⑥ 測定範囲エンド 単位
     */
    private String ir6sokuteihaniendtani;
    
    /**
     * IR⑦ 電流中心値スタート
     */
    private BigDecimal ir7denryustart;
    
    /**
     * IR⑦ 電流中心値スタート 単位
     */
    private String ir7denryustarttani;
    
    /**
     * IR⑦ 電流中心値エンド
     */
    private BigDecimal ir7denryuend;
    
    /**
     * IR⑦ 電流中心値エンド 単位
     */
    private String ir7denryuendtani;
    
    /**
     * IR⑦ 測定範囲スタート
     */
    private BigDecimal ir7sokuteihanistart;
    
    /**
     * IR⑦ 測定範囲スタート 単位
     */
    private String ir7sokuteihanistarttani;
    
    /**
     * IR⑦ 測定範囲エンド
     */
    private BigDecimal ir7sokuteihaniend;
    
    /**
     * IR⑦ 測定範囲エンド 単位
     */
    private String ir7sokuteihaniendtani;
    
        /**
     * IR⑧ 電流中心値スタート
     */
    private BigDecimal ir8denryustart;
    
    /**
     * IR⑧ 電流中心値スタート 単位
     */
    private String ir8denryustarttani;
    
    /**
     * IR⑧ 電流中心値エンド
     */
    private BigDecimal ir8denryuend;
    
    /**
     * IR⑧ 電流中心値エンド 単位
     */
    private String ir8denryuendtani;
    
    /**
     * IR⑧ 測定範囲スタート
     */
    private BigDecimal ir8sokuteihanistart;
    
    /**
     * IR⑧ 測定範囲スタート 単位
     */
    private String ir8sokuteihanistarttani;
    
    /**
     * IR⑧ 測定範囲エンド
     */
    private BigDecimal ir8sokuteihaniend;
    
    /**
     * IR⑧ 測定範囲エンド 単位
     */
    private String ir8sokuteihaniendtani;
    
    /**
     * IR① 時間 
     */
    private BigDecimal ir1jikan;
    
    /**
     * IR① 時間 単位
     */
    private String ir1jikantani;
    
    /**
     * IR② 時間 
     */
    private BigDecimal ir2jikan;
    
    /**
     * IR② 時間 単位
     */
    private String ir2jikantani;
    
     /**
     * IR③ 時間 
     */
    private BigDecimal ir3jikan;
    
    /**
     * IR③ 時間 単位
     */
    private String ir3jikantani;
    
    /**
     * IR④ 時間 
     */
    private BigDecimal ir4jikan;
    
    /**
     * IR④ 時間 単位
     */
    private String ir4jikantani;
    
     /**
     * IR⑤ 時間 
     */
    private BigDecimal ir5jikan;
    
    /**
     * IR⑤ 時間 単位
     */
    private String ir5jikantani;
    
    /**
     * IR⑥ 時間 
     */
    private BigDecimal ir6jikan;
    
    /**
     * IR⑥ 時間 単位
     */
    private String ir6jikantani;
    
     /**
     * IR⑦ 時間 
     */
    private BigDecimal ir7jikan;
    
    /**
     * IR⑦ 時間 単位
     */
    private String ir7jikantani;
    
    /**
     * IR⑧ 時間 
     */
    private BigDecimal ir8jikan;
    
    /**
     * IR⑧ 時間 単位
     */
    private String ir8jikantani;

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
     * 累計処理数
     * @return the ruikeisyorisuu
     */
    public Integer getRuikeisyorisuu() {
        return ruikeisyorisuu;
    }

    /**
     * 累計処理数
     * @param ruikeisyorisuu the ruikeisyorisuu to set
     */
    public void setRuikeisyorisuu(Integer ruikeisyorisuu) {
        this.ruikeisyorisuu = ruikeisyorisuu;
    }
    
    /**
     * 最大処理数
     * @return the saidaisyorisuu
     */
    public Integer getSaidaisyorisuu() {
        return saidaisyorisuu;
    }

    /**
     * 最大処理数
     * @param saidaisyorisuu the saidaisyorisuu to set
     */
    public void setSaidaisyorisuu(Integer saidaisyorisuu) {
        this.saidaisyorisuu = saidaisyorisuu;
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
    public BigDecimal getBunruiairatu() {
        return bunruiairatu;
    }

    /**
     * 分類ｴｱｰ圧
     * @param bunruiairatu the bunruiairatu to set
     */
    public void setBunruiairatu(BigDecimal bunruiairatu) {
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
     * 選別順序変更
     * @return the senbetujunjo
     */
    public String getSenbetujunjo() {
        return senbetujunjo;
    }

    /**
     * 選別順序変更
     * @param senbetujunjo the senbetujunjo to set
     */
    public void setSenbetujunjo(String senbetujunjo) {
        this.senbetujunjo = senbetujunjo;
    }

    /**
     * 設定ﾓｰﾄﾞ確認
     * @return the setteikakunin
     */
    public String getSetteikakunin() {
        return setteikakunin;
    }

    /**
     * 設定ﾓｰﾄﾞ確認
     * @param setteikakunin the setteikakunin to set
     */
    public void setSetteikakunin(String setteikakunin) {
        this.setteikakunin = setteikakunin;
    }

    /**
     * 配線確認
     * @return the haisenkakunin
     */
    public String getHaisenkakunin() {
        return haisenkakunin;
    }

    /**
     * 配線確認
     * @param haisenkakunin the haisenkakunin to set
     */
    public void setHaisenkakunin(String haisenkakunin) {
        this.haisenkakunin = haisenkakunin;
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
     * 指定公差歩留まり3
     * @return the siteikousabudomari3
     */
    public String getSiteikousabudomari3() {
        return siteikousabudomari3;
    }

    /**
     * 指定公差歩留まり3
     * @param siteikousabudomari3 the siteikousabudomari3 to set
     */
    public void setSiteikousabudomari3(String siteikousabudomari3) {
        this.siteikousabudomari3 = siteikousabudomari3;
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
    public BigDecimal getIrhanteiti1() {
        return irhanteiti1;
    }

    /**
     * 耐電圧設定条件 IR① 判定値
     * @param irhanteiti1 the irhanteiti1 to set
     */
    public void setIrhanteiti1(BigDecimal irhanteiti1) {
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
    public BigDecimal getIrhanteiti2() {
        return irhanteiti2;
    }

    /**
     * 耐電圧設定条件 IR② 判定値
     * @param irhanteiti2 the irhanteiti2 to set
     */
    public void setIrhanteiti2(BigDecimal irhanteiti2) {
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
    public BigDecimal getIrhanteiti3() {
        return irhanteiti3;
    }

    /**
     * 耐電圧設定条件 IR③ 判定値
     * @param irhanteiti3 the irhanteiti3 to set
     */
    public void setIrhanteiti3(BigDecimal irhanteiti3) {
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
    public BigDecimal getIrhanteiti4() {
        return irhanteiti4;
    }

    /**
     * 耐電圧設定条件 IR④ 判定値
     * @param irhanteiti4 the irhanteiti4 to set
     */
    public void setIrhanteiti4(BigDecimal irhanteiti4) {
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
    public BigDecimal getIrhanteiti5() {
        return irhanteiti5;
    }

    /**
     * 耐電圧設定条件 IR⑤ 判定値
     * @param irhanteiti5 the irhanteiti5 to set
     */
    public void setIrhanteiti5(BigDecimal irhanteiti5) {
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
    public BigDecimal getIrhanteiti6() {
        return irhanteiti6;
    }

    /**
     * 耐電圧設定条件 IR⑥ 判定値
     * @param irhanteiti6 the irhanteiti6 to set
     */
    public void setIrhanteiti6(BigDecimal irhanteiti6) {
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
    public BigDecimal getIrhanteiti7() {
        return irhanteiti7;
    }

    /**
     * 耐電圧設定条件 IR⑦ 判定値
     * @param irhanteiti7 the irhanteiti7 to set
     */
    public void setIrhanteiti7(BigDecimal irhanteiti7) {
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
    public BigDecimal getIrhanteiti8() {
        return irhanteiti8;
    }

    /**
     * 耐電圧設定条件 IR⑧ 判定値
     * @param irhanteiti8 the irhanteiti8 to set
     */
    public void setIrhanteiti8(BigDecimal irhanteiti8) {
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
     * RDC1 ﾚﾝｼﾞ
     * @return the rdcrange1
     */
    public BigDecimal getRdcrange1() {
        return rdcrange1;
    }

    /**
     * RDC1 ﾚﾝｼﾞ
     * @param rdcrange1 the rdcrange1 to set
     */
    public void setRdcrange1(BigDecimal rdcrange1) {
        this.rdcrange1 = rdcrange1;
    }

    /**
     * RDC1 判定値
     * @return the rdchantei1
     */
    public BigDecimal getRdchantei1() {
        return rdchantei1;
    }

    /**
     * RDC1 判定値
     * @param rdchantei1 the rdchantei1 to set
     */
    public void setRdchantei1(BigDecimal rdchantei1) {
        this.rdchantei1 = rdchantei1;
    }

    /**
     * RDC2 ﾚﾝｼﾞ
     * @return the rdcrange2
     */
    public BigDecimal getRdcrange2() {
        return rdcrange2;
    }

    /**
     * RDC2 ﾚﾝｼﾞ
     * @param rdcrange2 the rdcrange2 to set
     */
    public void setRdcrange2(BigDecimal rdcrange2) {
        this.rdcrange2 = rdcrange2;
    }

    /**
     * RDC2 判定値
     * @return the rdchantei2
     */
    public BigDecimal getRdchantei2() {
        return rdchantei2;
    }

    /**
     * RDC2 判定値
     * @param rdchantei2 the rdchantei2 to set
     */
    public void setRdchantei2(BigDecimal rdchantei2) {
        this.rdchantei2 = rdchantei2;
    }

    /**
     * DROP1,3 PC
     * @return the drop13pc
     */
    public BigDecimal getDrop13pc() {
        return drop13pc;
    }

    /**
     * DROP1,3 PC
     * @param drop13pc the drop13pc to set
     */
    public void setDrop13pc(BigDecimal drop13pc) {
        this.drop13pc = drop13pc;
    }

    /**
     * DROP1,3 PS
     * @return the drop13ps
     */
    public BigDecimal getDrop13ps() {
        return drop13ps;
    }

    /**
     * DROP1,3 PS
     * @param drop13ps the drop13ps to set
     */
    public void setDrop13ps(BigDecimal drop13ps) {
        this.drop13ps = drop13ps;
    }

    /**
     * DROP1,3 MS･DC
     * @return the drop13msdc
     */
    public BigDecimal getDrop13msdc() {
        return drop13msdc;
    }

    /**
     * DROP1,3 MS･DC
     * @param drop13msdc the drop13msdc to set
     */
    public void setDrop13msdc(BigDecimal drop13msdc) {
        this.drop13msdc = drop13msdc;
    }

    /**
     * DROP2,4 PC
     * @return the drop24pc
     */
    public BigDecimal getDrop24pc() {
        return drop24pc;
    }

    /**
     * DROP2,4 PC
     * @param drop24pc the drop24pc to set
     */
    public void setDrop24pc(BigDecimal drop24pc) {
        this.drop24pc = drop24pc;
    }

    /**
     * DROP2,4 PS
     * @return the drop24ps
     */
    public BigDecimal getDrop24ps() {
        return drop24ps;
    }

    /**
     * DROP2,4 PS
     * @param drop24ps the drop24ps to set
     */
    public void setDrop24ps(BigDecimal drop24ps) {
        this.drop24ps = drop24ps;
    }

    /**
     * DROP2,4 MS･DC
     * @return the drop24msdc
     */
    public BigDecimal getDrop24msdc() {
        return drop24msdc;
    }

    /**
     * DROP2,4 MS･DC
     * @param drop24msdc the drop24msdc to set
     */
    public void setDrop24msdc(BigDecimal drop24msdc) {
        this.drop24msdc = drop24msdc;
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
     * BIN1 袋ﾁｪｯｸ
     * @return the bin1fukurocheck
     */
    public Integer getBin1fukurocheck() {
        return bin1fukurocheck;
    }

    /**
     * BIN1 袋ﾁｪｯｸ
     * @param bin1fukurocheck the bin1fukurocheck to set
     */
    public void setBin1fukurocheck(Integer bin1fukurocheck) {
        this.bin1fukurocheck = bin1fukurocheck;
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
     * BIN2 袋ﾁｪｯｸ
     * @return the bin2fukurocheck
     */
    public Integer getBin2fukurocheck() {
        return bin2fukurocheck;
    }

    /**
     * BIN2 袋ﾁｪｯｸ
     * @param bin2fukurocheck the bin2fukurocheck to set
     */
    public void setBin2fukurocheck(Integer bin2fukurocheck) {
        this.bin2fukurocheck = bin2fukurocheck;
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
     * BIN3 袋ﾁｪｯｸ
     * @return the bin3fukurocheck
     */
    public Integer getBin3fukurocheck() {
        return bin3fukurocheck;
    }

    /**
     * BIN3 袋ﾁｪｯｸ
     * @param bin3fukurocheck the bin3fukurocheck to set
     */
    public void setBin3fukurocheck(Integer bin3fukurocheck) {
        this.bin3fukurocheck = bin3fukurocheck;
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
     * BIN4 袋ﾁｪｯｸ
     * @return the bin4fukurocheck
     */
    public Integer getBin4fukurocheck() {
        return bin4fukurocheck;
    }

    /**
     * BIN4 袋ﾁｪｯｸ
     * @param bin4fukurocheck the bin4fukurocheck to set
     */
    public void setBin4fukurocheck(Integer bin4fukurocheck) {
        this.bin4fukurocheck = bin4fukurocheck;
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
     * BIN7 抜き取り結果
     * @return the bin7nukitorikekkabosuu
     */
    public Integer getBin7nukitorikekkabosuu() {
        return bin7nukitorikekkabosuu;
    }

    /**
     * BIN7 抜き取り結果
     * @param bin7nukitorikekkabosuu the bin7nukitorikekkabosuu to set
     */
    public void setBin7nukitorikekkabosuu(Integer bin7nukitorikekkabosuu) {
        this.bin7nukitorikekkabosuu = bin7nukitorikekkabosuu;
    }

    /**
     * BIN7 抜き取り結果
     * @return the bin7nukitorikekka
     */
    public Integer getBin7nukitorikekka() {
        return bin7nukitorikekka;
    }

    /**
     * BIN7 抜き取り結果
     * @param bin7nukitorikekka the bin7nukitorikekka to set
     */
    public void setBin7nukitorikekka(Integer bin7nukitorikekka) {
        this.bin7nukitorikekka = bin7nukitorikekka;
    }

    /**
     * BIN7 真の不良率(%)
     * @return the bin7sinnofuryouritu
     */
    public BigDecimal getBin7sinnofuryouritu() {
        return bin7sinnofuryouritu;
    }

    /**
     * BIN7 真の不良率(%)
     * @param bin7sinnofuryouritu the bin7sinnofuryouritu to set
     */
    public void setBin7sinnofuryouritu(BigDecimal bin7sinnofuryouritu) {
        this.bin7sinnofuryouritu = bin7sinnofuryouritu;
    }

    /**
     * BIN7 結果ﾁｪｯｸ
     * @return the bin7kekkacheck
     */
    public String getBin7kekkacheck() {
        return bin7kekkacheck;
    }

    /**
     * BIN7 結果ﾁｪｯｸ
     * @param bin7kekkacheck the bin7kekkacheck to set
     */
    public void setBin7kekkacheck(String bin7kekkacheck) {
        this.bin7kekkacheck = bin7kekkacheck;
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
     * BIN8 抜き取り結果
     * @return the bin8nukitorikekkabosuu
     */
    public Integer getBin8nukitorikekkabosuu() {
        return bin8nukitorikekkabosuu;
    }

    /**
     * BIN8 抜き取り結果
     * @param bin8nukitorikekkabosuu the bin8nukitorikekkabosuu to set
     */
    public void setBin8nukitorikekkabosuu(Integer bin8nukitorikekkabosuu) {
        this.bin8nukitorikekkabosuu = bin8nukitorikekkabosuu;
    }

    /**
     * BIN8 抜き取り結果
     * @return the bin8nukitorikekka
     */
    public Integer getBin8nukitorikekka() {
        return bin8nukitorikekka;
    }

    /**
     * BIN8 抜き取り結果
     * @param bin8nukitorikekka the bin8nukitorikekka to set
     */
    public void setBin8nukitorikekka(Integer bin8nukitorikekka) {
        this.bin8nukitorikekka = bin8nukitorikekka;
    }

    /**
     * BIN8 真の不良率(%)
     * @return the bin8sinnofuryouritu
     */
    public BigDecimal getBin8sinnofuryouritu() {
        return bin8sinnofuryouritu;
    }

    /**
     * BIN8 真の不良率(%)
     * @param bin8sinnofuryouritu the bin8sinnofuryouritu to set
     */
    public void setBin8sinnofuryouritu(BigDecimal bin8sinnofuryouritu) {
        this.bin8sinnofuryouritu = bin8sinnofuryouritu;
    }

    /**
     * BIN8 結果ﾁｪｯｸ
     * @return the bin8kekkacheck
     */
    public String getBin8kekkacheck() {
        return bin8kekkacheck;
    }

    /**
     * BIN8 結果ﾁｪｯｸ
     * @param bin8kekkacheck the bin8kekkacheck to set
     */
    public void setBin8kekkacheck(String bin8kekkacheck) {
        this.bin8kekkacheck = bin8kekkacheck;
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
     * SATｻﾝﾌﾟﾙ
     * @return the satsample
     */
    public String getSatsample() {
        return satsample;
    }

    /**
     * SATｻﾝﾌﾟﾙ
     * @param satsample the satsample to set
     */
    public void setSatsample(String satsample) {
        this.satsample = satsample;
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
     * 耐電圧設定条件 IR① 判定値(低)
     * @return irhanteiti1low
     */
    public BigDecimal getIrhanteiti1low() {
        return irhanteiti1low;
    }

    /**
     * 耐電圧設定条件 IR① 判定値(低)
     * @param irhanteiti1low 
     */
    public void setIrhanteiti1low(BigDecimal irhanteiti1low) {
        this.irhanteiti1low = irhanteiti1low;
    }

    /**
     * 耐電圧設定条件 IR① 判定値 単位
     * @return irhanteiti1tani
     */
    public String getIrhanteiti1tani() {
        return irhanteiti1tani;
    }

    /**
     * 耐電圧設定条件 IR① 判定値 単位
     * @param irhanteiti1tani 
     */
    public void setIrhanteiti1tani(String irhanteiti1tani) {
        this.irhanteiti1tani = irhanteiti1tani;
    }

    /**
     * 耐電圧設定条件 IR② 判定値(低)
     * @return irhanteiti2low
     */
    public BigDecimal getIrhanteiti2low() {
        return irhanteiti2low;
    }

    /**
     * 耐電圧設定条件 IR② 判定値(低)
     * @param irhanteiti2low 
     */
    public void setIrhanteiti2low(BigDecimal irhanteiti2low) {
        this.irhanteiti2low = irhanteiti2low;
    }

    /**
     * 耐電圧設定条件 IR② 判定値 単位
     * @return irhanteiti2tani
     */
    public String getIrhanteiti2tani() {
        return irhanteiti2tani;
    }

    /**
     * 耐電圧設定条件 IR② 判定値 単位
     * @param irhanteiti2tani 
     */
    public void setIrhanteiti2tani(String irhanteiti2tani) {
        this.irhanteiti2tani = irhanteiti2tani;
    }

    /**
     * 耐電圧設定条件 IR③ 判定値(低)
     * @return irhanteiti3low
     */
    public BigDecimal getIrhanteiti3low() {
        return irhanteiti3low;
    }

    /**
     * 耐電圧設定条件 IR③ 判定値(低)
     * @param irhanteiti3low 
     */
    public void setIrhanteiti3low(BigDecimal irhanteiti3low) {
        this.irhanteiti3low = irhanteiti3low;
    }

    /**
     * 耐電圧設定条件 IR③ 判定値 単位
     * @return irhanteiti3tani
     */
    public String getIrhanteiti3tani() {
        return irhanteiti3tani;
    }

    /**
     * 耐電圧設定条件 IR③ 判定値 単位
     * @param irhanteiti3tani 
     */
    public void setIrhanteiti3tani(String irhanteiti3tani) {
        this.irhanteiti3tani = irhanteiti3tani;
    }

    /**
     * 耐電圧設定条件 IR④ 判定値(低)
     * @return irhanteiti4low
     */
    public BigDecimal getIrhanteiti4low() {
        return irhanteiti4low;
    }

    /**
     * 耐電圧設定条件 IR④ 判定値(低)
     * @param irhanteiti4low 
     */
    public void setIrhanteiti4low(BigDecimal irhanteiti4low) {
        this.irhanteiti4low = irhanteiti4low;
    }

    /**
     * 耐電圧設定条件 IR④ 判定値 単位
     * @return irhanteiti4tani
     */
    public String getIrhanteiti4tani() {
        return irhanteiti4tani;
    }

    /**
     * 耐電圧設定条件 IR④ 判定値 単位
     * @param irhanteiti4tani 
     */
    public void setIrhanteiti4tani(String irhanteiti4tani) {
        this.irhanteiti4tani = irhanteiti4tani;
    }

    /**
     * 耐電圧設定条件 IR⑤ 判定値(低)
     * @return irhanteiti5low
     */
    public BigDecimal getIrhanteiti5low() {
        return irhanteiti5low;
    }

    /**
     * 耐電圧設定条件 IR⑤ 判定値(低)
     * @param irhanteiti5low 
     */
    public void setIrhanteiti5low(BigDecimal irhanteiti5low) {
        this.irhanteiti5low = irhanteiti5low;
    }

    /**
     * 耐電圧設定条件 IR⑤ 判定値 単位
     * @return irhanteiti5tani
     */
    public String getIrhanteiti5tani() {
        return irhanteiti5tani;
    }

    /**
     * 耐電圧設定条件 IR⑤ 判定値 単位
     * @param irhanteiti5tani 
     */
    public void setIrhanteiti5tani(String irhanteiti5tani) {
        this.irhanteiti5tani = irhanteiti5tani;
    }

    /**
     * 耐電圧設定条件 IR⑥ 判定値(低)
     * @return irhanteiti6low
     */
    public BigDecimal getIrhanteiti6low() {
        return irhanteiti6low;
    }

    /**
     * 耐電圧設定条件 IR⑥ 判定値(低)
     * @param irhanteiti6low 
     */
    public void setIrhanteiti6low(BigDecimal irhanteiti6low) {
        this.irhanteiti6low = irhanteiti6low;
    }

    /**
     * 耐電圧設定条件 IR⑥ 判定値 単位
     * @return irhanteiti6tani
     */
    public String getIrhanteiti6tani() {
        return irhanteiti6tani;
    }

    /**
     * 耐電圧設定条件 IR⑥ 判定値 単位
     * @param irhanteiti6tani 
     */
    public void setIrhanteiti6tani(String irhanteiti6tani) {
        this.irhanteiti6tani = irhanteiti6tani;
    }

    /**
     * 耐電圧設定条件 IR⑦ 判定値(低)
     * @return irhanteiti7low
     */
    public BigDecimal getIrhanteiti7low() {
        return irhanteiti7low;
    }

    /**
     * 耐電圧設定条件 IR⑦ 判定値(低)
     * @param irhanteiti7low 
     */
    public void setIrhanteiti7low(BigDecimal irhanteiti7low) {
        this.irhanteiti7low = irhanteiti7low;
    }

    /**
     * 耐電圧設定条件 IR⑦ 判定値 単位
     * @return irhanteiti7tani
     */
    public String getIrhanteiti7tani() {
        return irhanteiti7tani;
    }

    /**
     * 耐電圧設定条件 IR⑦ 判定値 単位
     * @param irhanteiti7tani 
     */
    public void setIrhanteiti7tani(String irhanteiti7tani) {
        this.irhanteiti7tani = irhanteiti7tani;
    }

    /**
     * 耐電圧設定条件 IR⑧ 判定値(低)
     * @return irhanteiti8low
     */
    public BigDecimal getIrhanteiti8low() {
        return irhanteiti8low;
    }

    /**
     * 耐電圧設定条件 IR⑧ 判定値(低)
     * @param irhanteiti8low 
     */
    public void setIrhanteiti8low(BigDecimal irhanteiti8low) {
        this.irhanteiti8low = irhanteiti8low;
    }

    /**
     * 耐電圧設定条件 IR⑧ 判定値 単位
     * @return irhanteiti8tani
     */
    public String getIrhanteiti8tani() {
        return irhanteiti8tani;
    }

    /**
     * 耐電圧設定条件 IR⑧ 判定値 単位
     * @param irhanteiti8tani 
     */
    public void setIrhanteiti8tani(String irhanteiti8tani) {
        this.irhanteiti8tani = irhanteiti8tani;
    }

    /**
     * BIN1 ｶｳﾝﾀｰ数2
     * @return bin1countersuu2
     */
    public Integer getBin1countersuu2() {
        return bin1countersuu2;
    }

    /**
     * BIN1 ｶｳﾝﾀｰ数2
     * @param bin1countersuu2
     */
    public void setBin1countersuu2(Integer bin1countersuu2) {
        this.bin1countersuu2 = bin1countersuu2;
    }

    /**
     * BIN1 ｶｳﾝﾀｰ数3
     * @return bin1countersuu3
     */
    public Integer getBin1countersuu3() {
        return bin1countersuu3;
    }

    /**
     * BIN1 ｶｳﾝﾀｰ数3
     * @param bin1countersuu3
     */
    public void setBin1countersuu3(Integer bin1countersuu3) {
        this.bin1countersuu3 = bin1countersuu3;
    }

    /**
     * BIN2 ｶｳﾝﾀｰ数2
     * @return bin2countersuu2
     */
    public Integer getBin2countersuu2() {
        return bin2countersuu2;
    }

    /**
     * BIN2 ｶｳﾝﾀｰ数2
     * @param bin2countersuu2
     */
    public void setBin2countersuu2(Integer bin2countersuu2) {
        this.bin2countersuu2 = bin2countersuu2;
    }

    /**
     * BIN2 ｶｳﾝﾀｰ数3
     * @return bin2countersuu3
     */
    public Integer getBin2countersuu3() {
        return bin2countersuu3;
    }

    /**
     * BIN2 ｶｳﾝﾀｰ数3
     * @param bin2countersuu3
     */
    public void setBin2countersuu3(Integer bin2countersuu3) {
        this.bin2countersuu3 = bin2countersuu3;
    }

    /**
     * BIN3 ｶｳﾝﾀｰ数2
     * @return bin3countersuu2
     */
    public Integer getBin3countersuu2() {
        return bin3countersuu2;
    }

    /**
     * BIN3 ｶｳﾝﾀｰ数2
     * @param bin3countersuu2
     */
    public void setBin3countersuu2(Integer bin3countersuu2) {
        this.bin3countersuu2 = bin3countersuu2;
    }

    /**
     * BIN3 ｶｳﾝﾀｰ数3
     * @return bin3countersuu3
     */
    public Integer getBin3countersuu3() {
        return bin3countersuu3;
    }

    /**
     * BIN3 ｶｳﾝﾀｰ数3
     * @param bin3countersuu3
     */
    public void setBin3countersuu3(Integer bin3countersuu3) {
        this.bin3countersuu3 = bin3countersuu3;
    }

    /**
     * BIN4 ｶｳﾝﾀｰ数2
     * @return bin4countersuu2
     */
    public Integer getBin4countersuu2() {
        return bin4countersuu2;
    }

    /**
     * BIN4 ｶｳﾝﾀｰ数2
     * @param bin4countersuu2
     */
    public void setBin4countersuu2(Integer bin4countersuu2) {
        this.bin4countersuu2 = bin4countersuu2;
    }

    /**
     * BIN4 ｶｳﾝﾀｰ数3
     * @return bin4countersuu3
     */
    public Integer getBin4countersuu3() {
        return bin4countersuu3;
    }

    /**
     * BIN4 ｶｳﾝﾀｰ数3
     * @param bin4countersuu3
     */
    public void setBin4countersuu3(Integer bin4countersuu3) {
        this.bin4countersuu3 = bin4countersuu3;
    }

    /**
     * BIN5 ｶｳﾝﾀｰ数2
     * @return bin5countersuu2
     */
    public Integer getBin5countersuu2() {
        return bin5countersuu2;
    }

    /**
     * BIN5 ｶｳﾝﾀｰ数2
     * @param bin5countersuu2
     */
    public void setBin5countersuu2(Integer bin5countersuu2) {
        this.bin5countersuu2 = bin5countersuu2;
    }

    /**
     * BIN5 ｶｳﾝﾀｰ数3
     * @return bin5countersuu3
     */
    public Integer getBin5countersuu3() {
        return bin5countersuu3;
    }

    /**
     * BIN5 ｶｳﾝﾀｰ数3
     * @param bin5countersuu3
     */
    public void setBin5countersuu3(Integer bin5countersuu3) {
        this.bin5countersuu3 = bin5countersuu3;
    }

    /**
     * BIN6 ｶｳﾝﾀｰ数2
     * @return bin6countersuu2
     */
    public Integer getBin6countersuu2() {
        return bin6countersuu2;
    }

    /**
     * BIN6 ｶｳﾝﾀｰ数2
     * @param bin6countersuu2
     */
    public void setBin6countersuu2(Integer bin6countersuu2) {
        this.bin6countersuu2 = bin6countersuu2;
    }

    /**
     * BIN6 ｶｳﾝﾀｰ数3
     * @return bin6countersuu3
     */
    public Integer getBin6countersuu3() {
        return bin6countersuu3;
    }

    /**
     * BIN6 ｶｳﾝﾀｰ数3
     * @param bin6countersuu3
     */
    public void setBin6countersuu3(Integer bin6countersuu3) {
        this.bin6countersuu3 = bin6countersuu3;
    }

    /**
     * BIN7 ｶｳﾝﾀｰ数2
     * @return bin7countersuu2
     */
    public Integer getBin7countersuu2() {
        return bin7countersuu2;
    }

    /**
     * BIN7 ｶｳﾝﾀｰ数2
     * @param bin7countersuu2
     */
    public void setBin7countersuu2(Integer bin7countersuu2) {
        this.bin7countersuu2 = bin7countersuu2;
    }

    /**
     * BIN7 ｶｳﾝﾀｰ数3
     * @return bin7countersuu3
     */
    public Integer getBin7countersuu3() {
        return bin7countersuu3;
    }

    /**
     * BIN7 ｶｳﾝﾀｰ数3
     * @param bin7countersuu3
     */
    public void setBin7countersuu3(Integer bin7countersuu3) {
        this.bin7countersuu3 = bin7countersuu3;
    }

    /**
     * BIN8 ｶｳﾝﾀｰ数2
     * @return bin8countersuu2
     */
    public Integer getBin8countersuu2() {
        return bin8countersuu2;
    }

    /**
     * BIN8 ｶｳﾝﾀｰ数2
     * @param bin8countersuu2
     */
    public void setBin8countersuu2(Integer bin8countersuu2) {
        this.bin8countersuu2 = bin8countersuu2;
    }

    /**
     * BIN8 ｶｳﾝﾀｰ数3
     * @return bin8countersuu3
     */
    public Integer getBin8countersuu3() {
        return bin8countersuu3;
    }

    /**
     * BIN8 ｶｳﾝﾀｰ数3
     * @param bin8countersuu3 
     */
    public void setBin8countersuu3(Integer bin8countersuu3) {
        this.bin8countersuu3 = bin8countersuu3;
    }

    /**
     * 同品種
     * @return douhinsyu
     */
    public Integer getDouhinsyu() {
        return douhinsyu;
    }

    /**
     * 同品種
     * @param douhinsyu 
     */
    public void setDouhinsyu(Integer douhinsyu) {
        this.douhinsyu = douhinsyu;
    }

    /**
     * 耐電圧設定条件 IR① 判定値(低) 単位
     * @return the irhanteiti1tanilow
     */
    public String getIrhanteiti1tanilow() {
        return irhanteiti1tanilow;
    }

    /**
     * 耐電圧設定条件 IR① 判定値(低) 単位
     * @param irhanteiti1tanilow the irhanteiti1tanilow to set
     */
    public void setIrhanteiti1tanilow(String irhanteiti1tanilow) {
        this.irhanteiti1tanilow = irhanteiti1tanilow;
    }

    /**
     * 耐電圧設定条件 IR② 判定値(低) 単位
     * @return the irhanteiti2tanilow
     */
    public String getIrhanteiti2tanilow() {
        return irhanteiti2tanilow;
    }

    /**
     * 耐電圧設定条件 IR② 判定値(低) 単位
     * @param irhanteiti2tanilow the irhanteiti2tanilow to set
     */
    public void setIrhanteiti2tanilow(String irhanteiti2tanilow) {
        this.irhanteiti2tanilow = irhanteiti2tanilow;
    }

    /**
     * 耐電圧設定条件 IR③ 判定値(低) 単位
     * @return the irhanteiti3tanilow
     */
    public String getIrhanteiti3tanilow() {
        return irhanteiti3tanilow;
    }

    /**
     * 耐電圧設定条件 IR③ 判定値(低) 単位
     * @param irhanteiti3tanilow the irhanteiti3tanilow to set
     */
    public void setIrhanteiti3tanilow(String irhanteiti3tanilow) {
        this.irhanteiti3tanilow = irhanteiti3tanilow;
    }

    /**
     * 耐電圧設定条件 IR④ 判定値(低) 単位
     * @return the irhanteiti4tanilow
     */
    public String getIrhanteiti4tanilow() {
        return irhanteiti4tanilow;
    }

    /**
     * 耐電圧設定条件 IR④ 判定値(低) 単位
     * @param irhanteiti4tanilow the irhanteiti4tanilow to set
     */
    public void setIrhanteiti4tanilow(String irhanteiti4tanilow) {
        this.irhanteiti4tanilow = irhanteiti4tanilow;
    }

    /**
     * 耐電圧設定条件 IR⑤ 判定値(低) 単位
     * @return the irhanteiti5tanilow
     */
    public String getIrhanteiti5tanilow() {
        return irhanteiti5tanilow;
    }

    /**
     * 耐電圧設定条件 IR⑤ 判定値(低) 単位
     * @param irhanteiti5tanilow the irhanteiti5tanilow to set
     */
    public void setIrhanteiti5tanilow(String irhanteiti5tanilow) {
        this.irhanteiti5tanilow = irhanteiti5tanilow;
    }

    /**
     * 耐電圧設定条件 IR⑥ 判定値(低) 単位
     * @return the irhanteiti6tanilow
     */
    public String getIrhanteiti6tanilow() {
        return irhanteiti6tanilow;
    }

    /**
     * 耐電圧設定条件 IR⑥ 判定値(低) 単位
     * @param irhanteiti6tanilow the irhanteiti6tanilow to set
     */
    public void setIrhanteiti6tanilow(String irhanteiti6tanilow) {
        this.irhanteiti6tanilow = irhanteiti6tanilow;
    }

    /**
     * 耐電圧設定条件 IR⑦ 判定値(低) 単位
     * @return the irhanteiti7tanilow
     */
    public String getIrhanteiti7tanilow() {
        return irhanteiti7tanilow;
    }

    /**
     * 耐電圧設定条件 IR⑦ 判定値(低) 単位
     * @param irhanteiti7tanilow the irhanteiti7tanilow to set
     */
    public void setIrhanteiti7tanilow(String irhanteiti7tanilow) {
        this.irhanteiti7tanilow = irhanteiti7tanilow;
    }

    /**
     * 耐電圧設定条件 IR⑧ 判定値(低) 単位
     * @return the irhanteiti8tanilow
     */
    public String getIrhanteiti8tanilow() {
        return irhanteiti8tanilow;
    }

    /**
     * 耐電圧設定条件 IR⑧ 判定値(低) 単位
     * @param irhanteiti8tanilow the irhanteiti8tanilow to set
     */
    public void setIrhanteiti8tanilow(String irhanteiti8tanilow) {
        this.irhanteiti8tanilow = irhanteiti8tanilow;
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
    
    /**
     * 原点復帰動作
     * @return the gentenhukkidousa
     */
    public String getGentenhukkidousa() {
        return gentenhukkidousa;
    }
    
    /**
     * 原点復帰動作
     * @param gentenhukkidousa the gentenhukkidousa to set
     */
    public void setGentenhukkidousa(String gentenhukkidousa) {
        this.gentenhukkidousa = gentenhukkidousa;
    }
    
    /**
     * 測定機1,2動作確認
     * @return the sokuteiki12dousakakunin
     */
    public String getSokuteiki12dousakakunin() {
        return sokuteiki12dousakakunin;
    }
    
    /**
     * 測定機1,2動作確認
     * @param sokuteiki12dousakakunin the sokuteiki12dousakakunin to set
     */
    public void setSokuteiki12dousakakunin(String sokuteiki12dousakakunin) {
        this.sokuteiki12dousakakunin = sokuteiki12dousakakunin;
    }
    
    /**
     * 測定ピン(フロント)外観
     * @return the sokuteipinfront
     */
    public String getSokuteipinfront() {
        return sokuteipinfront;
    }
    
    /**
     * 測定ピン(フロント)外観
     * @param sokuteipinfront the sokuteipinfront to set
     */
    public void setSokuteipinfront(String sokuteipinfront) {
        this.sokuteipinfront = sokuteipinfront;
    }
    
    /**
     * 測定ピン(リア)外観
     * @return the sokuteipinrear
     */
    public String getSokuteipinrear() {
        return sokuteipinrear;
    }
    
    /**
     * 測定ピン(リア)外観
     * @param sokuteipinrear the sokuteipinrear to set
     */
    public void setSokuteipinrear(String sokuteipinrear) {
        this.sokuteipinrear = sokuteipinrear;
    }
    
    /**
     * IR① 電流中心値スタート
     * @return ir1denryustart
     */
    public BigDecimal getIr1denryustart() {
        return ir1denryustart;
    }

    /**
     * IR① 電流中心値スタート
     * @param ir1denryustart 
     */
    public void setIr1denryustart(BigDecimal ir1denryustart) {
        this.ir1denryustart = ir1denryustart;
    }
    
    /**
     * IR① 電流中心値スタート 単位
     * @return ir1denryustarttani
     */
    public String getIr1denryustarttani() {
        return ir1denryustarttani;
    }

    /**
     * IR① 電流中心値スタート 単位
     * @param ir1denryustarttani 
     */
    public void setIr1denryustarttani(String ir1denryustarttani) {
        this.ir1denryustarttani = ir1denryustarttani;
    }
    
    /**
     * IR① 電流中心値エンド
     * @return ir1denryuend
     */
    public BigDecimal getIr1denryuend() {
        return ir1denryuend;
    }

    /**
     * IR① 電流中心値エンド
     * @param ir1denryuend 
     */
    public void setIr1denryuend(BigDecimal ir1denryuend) {
        this.ir1denryuend = ir1denryuend;
    }
    
    /**
     * IR① 電流中心値エンド 単位
     * @return ir1denryuendtani
     */
    public String getIr1denryuendtani() {
        return ir1denryuendtani;
    }

    /**
     * IR① 電流中心値エンド 単位
     * @param ir1denryuendtani 
     */
    public void setIr1denryuendtani(String ir1denryuendtani) {
        this.ir1denryuendtani = ir1denryuendtani;
    }
    
    /**
     * IR① 測定範囲スタート
     * @return ir1sokuteihanistart
     */
    public BigDecimal getIr1sokuteihanistart() {
        return ir1sokuteihanistart;
    }

    /**
     * IR① 測定範囲スタート
     * @param ir1sokuteihanistart 
     */
    public void setIr1sokuteihanistart(BigDecimal ir1sokuteihanistart) {
        this.ir1sokuteihanistart = ir1sokuteihanistart;
    }
    
    /**
     * IR① 測定範囲スタート 単位
     * @return ir1sokuteihanistarttani
     */
    public String getIr1sokuteihanistarttani() {
        return ir1sokuteihanistarttani;
    }

    /**
     * IR① 測定範囲スタート 単位
     * @param ir1sokuteihanistarttani 
     */
    public void setIr1sokuteihanistarttani(String ir1sokuteihanistarttani) {
        this.ir1sokuteihanistarttani = ir1sokuteihanistarttani;
    }
    
    /**
     * IR① 測定範囲エンド
     * @return ir1sokuteihaniend
     */
    public BigDecimal getIr1sokuteihaniend() {
        return ir1sokuteihaniend;
    }

    /**
     * IR① 測定範囲エンド
     * @param ir1sokuteihaniend 
     */
    public void setIr1sokuteihaniend(BigDecimal ir1sokuteihaniend) {
        this.ir1sokuteihaniend = ir1sokuteihaniend;
    }
    
    /**
     * IR① 測定範囲エンド 単位
     * @return ir1sokuteihaniendtani
     */
    public String getIr1sokuteihaniendtani() {
        return ir1sokuteihaniendtani;
    }

    /**
     * IR① 測定範囲エンド 単位
     * @param ir1sokuteihaniendtani 
     */
    public void setIr1sokuteihaniendtani(String ir1sokuteihaniendtani) {
        this.ir1sokuteihaniendtani = ir1sokuteihaniendtani;
    }
    
    /**
     * IR② 電流中心値スタート
     * @return ir2denryustart
     */
    public BigDecimal getIr2denryustart() {
        return ir2denryustart;
    }

    /**
     * IR② 電流中心値スタート
     * @param ir2denryustart 
     */
    public void setIr2denryustart(BigDecimal ir2denryustart) {
        this.ir2denryustart = ir2denryustart;
    }
    
    /**
     * IR② 電流中心値スタート 単位
     * @return ir2denryustarttani
     */
    public String getIr2denryustarttani() {
        return ir2denryustarttani;
    }

    /**
     * IR② 電流中心値スタート 単位
     * @param ir2denryustarttani 
     */
    public void setIr2denryustarttani(String ir2denryustarttani) {
        this.ir2denryustarttani = ir2denryustarttani;
    }
    
    /**
     * IR② 電流中心値エンド
     * @return ir2denryuend
     */
    public BigDecimal getIr2denryuend() {
        return ir2denryuend;
    }

    /**
     * IR② 電流中心値エンド
     * @param ir2denryuend 
     */
    public void setIr2denryuend(BigDecimal ir2denryuend) {
        this.ir2denryuend = ir2denryuend;
    }
    
    /**
     * IR② 電流中心値エンド 単位
     * @return ir2denryuendtani
     */
    public String getIr2denryuendtani() {
        return ir2denryuendtani;
    }

    /**
     * IR② 電流中心値エンド 単位
     * @param ir2denryuendtani 
     */
    public void setIr2denryuendtani(String ir2denryuendtani) {
        this.ir2denryuendtani = ir2denryuendtani;
    }
    
    /**
     * IR② 測定範囲スタート
     * @return ir2sokuteihanistart
     */
    public BigDecimal getIr2sokuteihanistart() {
        return ir2sokuteihanistart;
    }

    /**
     * IR② 測定範囲スタート
     * @param ir2sokuteihanistart 
     */
    public void setIr2sokuteihanistart(BigDecimal ir2sokuteihanistart) {
        this.ir2sokuteihanistart = ir2sokuteihanistart;
    }
    
    /**
     * IR② 測定範囲スタート 単位
     * @return ir2sokuteihanistarttani
     */
    public String getIr2sokuteihanistarttani() {
        return ir2sokuteihanistarttani;
    }

    /**
     * IR② 測定範囲スタート 単位
     * @param ir2sokuteihanistarttani 
     */
    public void setIr2sokuteihanistarttani(String ir2sokuteihanistarttani) {
        this.ir2sokuteihanistarttani = ir2sokuteihanistarttani;
    }
    
    /**
     * IR② 測定範囲エンド
     * @return ir2sokuteihaniend
     */
    public BigDecimal getIr2sokuteihaniend() {
        return ir2sokuteihaniend;
    }

    /**
     * IR② 測定範囲エンド
     * @param ir2sokuteihaniend 
     */
    public void setIr2sokuteihaniend(BigDecimal ir2sokuteihaniend) {
        this.ir2sokuteihaniend = ir2sokuteihaniend;
    }
    
    /**
     * IR② 測定範囲エンド 単位
     * @return ir2sokuteihaniendtani
     */
    public String getIr2sokuteihaniendtani() {
        return ir2sokuteihaniendtani;
    }

    /**
     * IR② 測定範囲エンド 単位
     * @param ir2sokuteihaniendtani 
     */
    public void setIr2sokuteihaniendtani(String ir2sokuteihaniendtani) {
        this.ir2sokuteihaniendtani = ir2sokuteihaniendtani;
    }
    
    /**
     * IR③ 電流中心値スタート
     * @return ir3denryustart
     */
    public BigDecimal getIr3denryustart() {
        return ir3denryustart;
    }

    /**
     * IR③ 電流中心値スタート
     * @param ir3denryustart 
     */
    public void setIr3denryustart(BigDecimal ir3denryustart) {
        this.ir3denryustart = ir3denryustart;
    }
    
    /**
     * IR③ 電流中心値スタート 単位
     * @return ir3denryustarttani
     */
    public String getIr3denryustarttani() {
        return ir3denryustarttani;
    }

    /**
     * IR③ 電流中心値スタート 単位
     * @param ir3denryustarttani 
     */
    public void setIr3denryustarttani(String ir3denryustarttani) {
        this.ir3denryustarttani = ir3denryustarttani;
    }
    
    /**
     * IR③ 電流中心値エンド
     * @return ir3denryuend
     */
    public BigDecimal getIr3denryuend() {
        return ir3denryuend;
    }

    /**
     * IR③ 電流中心値エンド
     * @param ir3denryuend 
     */
    public void setIr3denryuend(BigDecimal ir3denryuend) {
        this.ir3denryuend = ir3denryuend;
    }
    
    /**
     * IR③ 電流中心値エンド 単位
     * @return ir3denryuendtani
     */
    public String getIr3denryuendtani() {
        return ir3denryuendtani;
    }

    /**
     * IR③ 電流中心値エンド 単位
     * @param ir3denryuendtani 
     */
    public void setIr3denryuendtani(String ir3denryuendtani) {
        this.ir3denryuendtani = ir3denryuendtani;
    }
    
    /**
     * IR③ 測定範囲スタート
     * @return ir3sokuteihanistart
     */
    public BigDecimal getIr3sokuteihanistart() {
        return ir3sokuteihanistart;
    }

    /**
     * IR③ 測定範囲スタート
     * @param ir3sokuteihanistart 
     */
    public void setIr3sokuteihanistart(BigDecimal ir3sokuteihanistart) {
        this.ir3sokuteihanistart = ir3sokuteihanistart;
    }
    
    /**
     * IR③ 測定範囲スタート 単位
     * @return ir3sokuteihanistarttani
     */
    public String getIr3sokuteihanistarttani() {
        return ir3sokuteihanistarttani;
    }

    /**
     * IR③ 測定範囲スタート 単位
     * @param ir3sokuteihanistarttani 
     */
    public void setIr3sokuteihanistarttani(String ir3sokuteihanistarttani) {
        this.ir3sokuteihanistarttani = ir3sokuteihanistarttani;
    }
    
    /**
     * IR③ 測定範囲エンド
     * @return ir3sokuteihaniend
     */
    public BigDecimal getIr3sokuteihaniend() {
        return ir3sokuteihaniend;
    }

    /**
     * IR③ 測定範囲エンド
     * @param ir3sokuteihaniend 
     */
    public void setIr3sokuteihaniend(BigDecimal ir3sokuteihaniend) {
        this.ir3sokuteihaniend = ir3sokuteihaniend;
    }
    
    /**
     * IR③ 測定範囲エンド 単位
     * @return ir3sokuteihaniendtani
     */
    public String getIr3sokuteihaniendtani() {
        return ir3sokuteihaniendtani;
    }

    /**
     * IR③ 測定範囲エンド 単位
     * @param ir3sokuteihaniendtani 
     */
    public void setIr3sokuteihaniendtani(String ir3sokuteihaniendtani) {
        this.ir3sokuteihaniendtani = ir3sokuteihaniendtani;
    }
    
    /**
     * IR④ 電流中心値スタート
     * @return ir4denryustart
     */
    public BigDecimal getIr4denryustart() {
        return ir4denryustart;
    }

    /**
     * IR④ 電流中心値スタート
     * @param ir4denryustart 
     */
    public void setIr4denryustart(BigDecimal ir4denryustart) {
        this.ir4denryustart = ir4denryustart;
    }
    
    /**
     * IR④ 電流中心値スタート 単位
     * @return ir4denryustarttani
     */
    public String getIr4denryustarttani() {
        return ir4denryustarttani;
    }

    /**
     * IR④ 電流中心値スタート 単位
     * @param ir4denryustarttani 
     */
    public void setIr4denryustarttani(String ir4denryustarttani) {
        this.ir4denryustarttani = ir4denryustarttani;
    }
    
    /**
     * IR④ 電流中心値エンド
     * @return ir4denryuend
     */
    public BigDecimal getIr4denryuend() {
        return ir4denryuend;
    }

    /**
     * IR④ 電流中心値エンド
     * @param ir4denryuend 
     */
    public void setIr4denryuend(BigDecimal ir4denryuend) {
        this.ir4denryuend = ir4denryuend;
    }
    
    /**
     * IR④ 電流中心値エンド 単位
     * @return ir4denryuendtani
     */
    public String getIr4denryuendtani() {
        return ir4denryuendtani;
    }

    /**
     * IR④ 電流中心値エンド 単位
     * @param ir4denryuendtani 
     */
    public void setIr4denryuendtani(String ir4denryuendtani) {
        this.ir4denryuendtani = ir4denryuendtani;
    }
    
    /**
     * IR④ 測定範囲スタート
     * @return ir4sokuteihanistart
     */
    public BigDecimal getIr4sokuteihanistart() {
        return ir4sokuteihanistart;
    }

    /**
     * IR④ 測定範囲スタート
     * @param ir4sokuteihanistart 
     */
    public void setIr4sokuteihanistart(BigDecimal ir4sokuteihanistart) {
        this.ir4sokuteihanistart = ir4sokuteihanistart;
    }
    
    /**
     * IR④ 測定範囲スタート 単位
     * @return ir4sokuteihanistarttani
     */
    public String getIr4sokuteihanistarttani() {
        return ir4sokuteihanistarttani;
    }

    /**
     * IR④ 測定範囲スタート 単位
     * @param ir4sokuteihanistarttani 
     */
    public void setIr4sokuteihanistarttani(String ir4sokuteihanistarttani) {
        this.ir4sokuteihanistarttani = ir4sokuteihanistarttani;
    }
    
    /**
     * IR④ 測定範囲エンド
     * @return ir4sokuteihaniend
     */
    public BigDecimal getIr4sokuteihaniend() {
        return ir4sokuteihaniend;
    }

    /**
     * IR④ 測定範囲エンド
     * @param ir4sokuteihaniend 
     */
    public void setIr4sokuteihaniend(BigDecimal ir4sokuteihaniend) {
        this.ir4sokuteihaniend = ir4sokuteihaniend;
    }
    
    /**
     * IR④ 測定範囲エンド 単位
     * @return ir4sokuteihaniendtani
     */
    public String getIr4sokuteihaniendtani() {
        return ir4sokuteihaniendtani;
    }

    /**
     * IR④ 測定範囲エンド 単位
     * @param ir4sokuteihaniendtani 
     */
    public void setIr4sokuteihaniendtani(String ir4sokuteihaniendtani) {
        this.ir4sokuteihaniendtani = ir4sokuteihaniendtani;
    }
    
        /**
     * IR⑤ 電流中心値スタート
     * @return ir5denryustart
     */
    public BigDecimal getIr5denryustart() {
        return ir5denryustart;
    }

    /**
     * IR⑤ 電流中心値スタート
     * @param ir5denryustart 
     */
    public void setIr5denryustart(BigDecimal ir5denryustart) {
        this.ir5denryustart = ir5denryustart;
    }
    
    /**
     * IR⑤ 電流中心値スタート 単位
     * @return ir5denryustarttani
     */
    public String getIr5denryustarttani() {
        return ir5denryustarttani;
    }

    /**
     * IR⑤ 電流中心値スタート 単位
     * @param ir5denryustarttani 
     */
    public void setIr5denryustarttani(String ir5denryustarttani) {
        this.ir5denryustarttani = ir5denryustarttani;
    }
    
    /**
     * IR⑤ 電流中心値エンド
     * @return ir5denryuend
     */
    public BigDecimal getIr5denryuend() {
        return ir5denryuend;
    }

    /**
     * IR⑤ 電流中心値エンド
     * @param ir5denryuend 
     */
    public void setIr5denryuend(BigDecimal ir5denryuend) {
        this.ir5denryuend = ir5denryuend;
    }
    
    /**
     * IR⑤ 電流中心値エンド 単位
     * @return ir5denryuendtani
     */
    public String getIr5denryuendtani() {
        return ir5denryuendtani;
    }

    /**
     * IR⑤ 電流中心値エンド 単位
     * @param ir5denryuendtani 
     */
    public void setIr5denryuendtani(String ir5denryuendtani) {
        this.ir5denryuendtani = ir5denryuendtani;
    }
    
    /**
     * IR⑤ 測定範囲スタート
     * @return ir5sokuteihanistart
     */
    public BigDecimal getIr5sokuteihanistart() {
        return ir5sokuteihanistart;
    }

    /**
     * IR⑤ 測定範囲スタート
     * @param ir5sokuteihanistart 
     */
    public void setIr5sokuteihanistart(BigDecimal ir5sokuteihanistart) {
        this.ir5sokuteihanistart = ir5sokuteihanistart;
    }
    
    /**
     * IR⑤ 測定範囲スタート 単位
     * @return ir5sokuteihanistarttani
     */
    public String getIr5sokuteihanistarttani() {
        return ir5sokuteihanistarttani;
    }

    /**
     * IR⑤ 測定範囲スタート 単位
     * @param ir5sokuteihanistarttani 
     */
    public void setIr5sokuteihanistarttani(String ir5sokuteihanistarttani) {
        this.ir5sokuteihanistarttani = ir5sokuteihanistarttani;
    }
    
    /**
     * IR⑤ 測定範囲エンド
     * @return ir5sokuteihaniend
     */
    public BigDecimal getIr5sokuteihaniend() {
        return ir5sokuteihaniend;
    }

    /**
     * IR⑤ 測定範囲エンド
     * @param ir5sokuteihaniend 
     */
    public void setIr5sokuteihaniend(BigDecimal ir5sokuteihaniend) {
        this.ir5sokuteihaniend = ir5sokuteihaniend;
    }
    
    /**
     * IR⑤ 測定範囲エンド 単位
     * @return ir5sokuteihaniendtani
     */
    public String getIr5sokuteihaniendtani() {
        return ir5sokuteihaniendtani;
    }

    /**
     * IR⑤ 測定範囲エンド 単位
     * @param ir5sokuteihaniendtani 
     */
    public void setIr5sokuteihaniendtani(String ir5sokuteihaniendtani) {
        this.ir5sokuteihaniendtani = ir5sokuteihaniendtani;
    }
    
    /**
     * IR⑥ 電流中心値スタート
     * @return ir6denryustart
     */
    public BigDecimal getIr6denryustart() {
        return ir6denryustart;
    }

    /**
     * IR⑥ 電流中心値スタート
     * @param ir6denryustart 
     */
    public void setIr6denryustart(BigDecimal ir6denryustart) {
        this.ir6denryustart = ir6denryustart;
    }
    
    /**
     * IR⑥ 電流中心値スタート 単位
     * @return ir6denryustarttani
     */
    public String getIr6denryustarttani() {
        return ir6denryustarttani;
    }

    /**
     * IR⑥ 電流中心値スタート 単位
     * @param ir6denryustarttani 
     */
    public void setIr6denryustarttani(String ir6denryustarttani) {
        this.ir6denryustarttani = ir6denryustarttani;
    }
    
    /**
     * IR⑥ 電流中心値エンド
     * @return ir6denryuend
     */
    public BigDecimal getIr6denryuend() {
        return ir6denryuend;
    }

    /**
     * IR⑥ 電流中心値エンド
     * @param ir6denryuend 
     */
    public void setIr6denryuend(BigDecimal ir6denryuend) {
        this.ir6denryuend = ir6denryuend;
    }
    
    /**
     * IR⑥ 電流中心値エンド 単位
     * @return ir6denryuendtani
     */
    public String getIr6denryuendtani() {
        return ir6denryuendtani;
    }

    /**
     * IR⑥ 電流中心値エンド 単位
     * @param ir6denryuendtani 
     */
    public void setIr6denryuendtani(String ir6denryuendtani) {
        this.ir6denryuendtani = ir6denryuendtani;
    }
    
    /**
     * IR⑥ 測定範囲スタート
     * @return ir6sokuteihanistart
     */
    public BigDecimal getIr6sokuteihanistart() {
        return ir6sokuteihanistart;
    }

    /**
     * IR⑥ 測定範囲スタート
     * @param ir6sokuteihanistart 
     */
    public void setIr6sokuteihanistart(BigDecimal ir6sokuteihanistart) {
        this.ir6sokuteihanistart = ir6sokuteihanistart;
    }
    
    /**
     * IR⑥ 測定範囲スタート 単位
     * @return ir6sokuteihanistarttani
     */
    public String getIr6sokuteihanistarttani() {
        return ir6sokuteihanistarttani;
    }

    /**
     * IR⑥ 測定範囲スタート 単位
     * @param ir6sokuteihanistarttani 
     */
    public void setIr6sokuteihanistarttani(String ir6sokuteihanistarttani) {
        this.ir6sokuteihanistarttani = ir6sokuteihanistarttani;
    }
    
    /**
     * IR⑥ 測定範囲エンド
     * @return ir6sokuteihaniend
     */
    public BigDecimal getIr6sokuteihaniend() {
        return ir6sokuteihaniend;
    }

    /**
     * IR⑥ 測定範囲エンド
     * @param ir6sokuteihaniend 
     */
    public void setIr6sokuteihaniend(BigDecimal ir6sokuteihaniend) {
        this.ir6sokuteihaniend = ir6sokuteihaniend;
    }
    
    /**
     * IR⑥ 測定範囲エンド 単位
     * @return ir6sokuteihaniendtani
     */
    public String getIr6sokuteihaniendtani() {
        return ir6sokuteihaniendtani;
    }

    /**
     * IR⑥ 測定範囲エンド 単位
     * @param ir6sokuteihaniendtani 
     */
    public void setIr6sokuteihaniendtani(String ir6sokuteihaniendtani) {
        this.ir6sokuteihaniendtani = ir6sokuteihaniendtani;
    }
    
    /**
     * IR⑦ 電流中心値スタート
     * @return ir7denryustart
     */
    public BigDecimal getIr7denryustart() {
        return ir7denryustart;
    }

    /**
     * IR⑦ 電流中心値スタート
     * @param ir7denryustart 
     */
    public void setIr7denryustart(BigDecimal ir7denryustart) {
        this.ir7denryustart = ir7denryustart;
    }
    
    /**
     * IR⑦ 電流中心値スタート 単位
     * @return ir7denryustarttani
     */
    public String getIr7denryustarttani() {
        return ir7denryustarttani;
    }

    /**
     * IR⑦ 電流中心値スタート 単位
     * @param ir7denryustarttani 
     */
    public void setIr7denryustarttani(String ir7denryustarttani) {
        this.ir7denryustarttani = ir7denryustarttani;
    }
    
    /**
     * IR⑦ 電流中心値エンド
     * @return ir7denryuend
     */
    public BigDecimal getIr7denryuend() {
        return ir7denryuend;
    }

    /**
     * IR⑦ 電流中心値エンド
     * @param ir7denryuend 
     */
    public void setIr7denryuend(BigDecimal ir7denryuend) {
        this.ir7denryuend = ir7denryuend;
    }
    
    /**
     * IR⑦ 電流中心値エンド 単位
     * @return ir7denryuendtani
     */
    public String getIr7denryuendtani() {
        return ir7denryuendtani;
    }

    /**
     * IR⑦ 電流中心値エンド 単位
     * @param ir7denryuendtani 
     */
    public void setIr7denryuendtani(String ir7denryuendtani) {
        this.ir7denryuendtani = ir7denryuendtani;
    }
    
    /**
     * IR⑦ 測定範囲スタート
     * @return ir7sokuteihanistart
     */
    public BigDecimal getIr7sokuteihanistart() {
        return ir7sokuteihanistart;
    }

    /**
     * IR⑦ 測定範囲スタート
     * @param ir7sokuteihanistart 
     */
    public void setIr7sokuteihanistart(BigDecimal ir7sokuteihanistart) {
        this.ir7sokuteihanistart = ir7sokuteihanistart;
    }
    
    /**
     * IR⑦ 測定範囲スタート 単位
     * @return ir7sokuteihanistarttani
     */
    public String getIr7sokuteihanistarttani() {
        return ir7sokuteihanistarttani;
    }

    /**
     * IR⑦ 測定範囲スタート 単位
     * @param ir7sokuteihanistarttani 
     */
    public void setIr7sokuteihanistarttani(String ir7sokuteihanistarttani) {
        this.ir7sokuteihanistarttani = ir7sokuteihanistarttani;
    }
    
    /**
     * IR⑦ 測定範囲エンド
     * @return ir7sokuteihaniend
     */
    public BigDecimal getIr7sokuteihaniend() {
        return ir7sokuteihaniend;
    }

    /**
     * IR⑦ 測定範囲エンド
     * @param ir7sokuteihaniend 
     */
    public void setIr7sokuteihaniend(BigDecimal ir7sokuteihaniend) {
        this.ir7sokuteihaniend = ir7sokuteihaniend;
    }
    
    /**
     * IR⑦ 測定範囲エンド 単位
     * @return ir7sokuteihaniendtani
     */
    public String getIr7sokuteihaniendtani() {
        return ir7sokuteihaniendtani;
    }

    /**
     * IR⑦ 測定範囲エンド 単位
     * @param ir7sokuteihaniendtani 
     */
    public void setIr7sokuteihaniendtani(String ir7sokuteihaniendtani) {
        this.ir7sokuteihaniendtani = ir7sokuteihaniendtani;
    }
    
    /**
     * IR⑧ 電流中心値スタート
     * @return ir8denryustart
     */
    public BigDecimal getIr8denryustart() {
        return ir8denryustart;
    }

    /**
     * IR⑧ 電流中心値スタート
     * @param ir8denryustart 
     */
    public void setIr8denryustart(BigDecimal ir8denryustart) {
        this.ir8denryustart = ir8denryustart;
    }
    
    /**
     * IR⑧ 電流中心値スタート 単位
     * @return ir8denryustarttani
     */
    public String getIr8denryustarttani() {
        return ir8denryustarttani;
    }

    /**
     * IR⑧ 電流中心値スタート 単位
     * @param ir8denryustarttani 
     */
    public void setIr8denryustarttani(String ir8denryustarttani) {
        this.ir8denryustarttani = ir8denryustarttani;
    }
    
    /**
     * IR⑧ 電流中心値エンド
     * @return ir8denryuend
     */
    public BigDecimal getIr8denryuend() {
        return ir8denryuend;
    }

    /**
     * IR⑧ 電流中心値エンド
     * @param ir8denryuend 
     */
    public void setIr8denryuend(BigDecimal ir8denryuend) {
        this.ir8denryuend = ir8denryuend;
    }
    
    /**
     * IR⑧ 電流中心値エンド 単位
     * @return ir8denryuendtani
     */
    public String getIr8denryuendtani() {
        return ir8denryuendtani;
    }

    /**
     * IR⑧ 電流中心値エンド 単位
     * @param ir8denryuendtani 
     */
    public void setIr8denryuendtani(String ir8denryuendtani) {
        this.ir8denryuendtani = ir8denryuendtani;
    }
    
    /**
     * IR⑧ 測定範囲スタート
     * @return ir8sokuteihanistart
     */
    public BigDecimal getIr8sokuteihanistart() {
        return ir8sokuteihanistart;
    }

    /**
     * IR⑧ 測定範囲スタート
     * @param ir8sokuteihanistart 
     */
    public void setIr8sokuteihanistart(BigDecimal ir8sokuteihanistart) {
        this.ir8sokuteihanistart = ir8sokuteihanistart;
    }
    
    /**
     * IR⑧ 測定範囲スタート 単位
     * @return ir8sokuteihanistarttani
     */
    public String getIr8sokuteihanistarttani() {
        return ir8sokuteihanistarttani;
    }

    /**
     * IR⑧ 測定範囲スタート 単位
     * @param ir8sokuteihanistarttani 
     */
    public void setIr8sokuteihanistarttani(String ir8sokuteihanistarttani) {
        this.ir8sokuteihanistarttani = ir8sokuteihanistarttani;
    }
    
    /**
     * IR⑧ 測定範囲エンド
     * @return ir8sokuteihaniend
     */
    public BigDecimal getIr8sokuteihaniend() {
        return ir8sokuteihaniend;
    }

    /**
     * IR⑧ 測定範囲エンド
     * @param ir8sokuteihaniend 
     */
    public void setIr8sokuteihaniend(BigDecimal ir8sokuteihaniend) {
        this.ir8sokuteihaniend = ir8sokuteihaniend;
    }
    
    /**
     * IR⑧ 測定範囲エンド 単位
     * @return ir8sokuteihaniendtani
     */
    public String getIr8sokuteihaniendtani() {
        return ir8sokuteihaniendtani;
    }

    /**
     * IR⑧ 測定範囲エンド 単位
     * @param ir8sokuteihaniendtani 
     */
    public void setIr8sokuteihaniendtani(String ir8sokuteihaniendtani) {
        this.ir8sokuteihaniendtani = ir8sokuteihaniendtani;
    }
    
        /**
     * IR① 時間
     * @return ir1jikan
     */
    public BigDecimal getIr1jikan() {
        return ir1jikan;
    }

    /**
     * IR① 時間
     * @param ir1jikan
     */
    public void setIr1jikan(BigDecimal ir1jikan) {
        this.ir1jikan = ir1jikan;
    }
    
    /**
     * IR① 時間 単位
     * @return ir1jikantani
     */
    public String getIr1jikantani() {
        return ir1jikantani;
    }

    /**
     * IR① 時間 単位
     * @param ir1jikantani
     */
    public void setIr1jikantani(String ir1jikantani) {
        this.ir1jikantani = ir1jikantani;
    }
    
    /**
     * IR② 時間
     * @return ir2jikan
     */
    public BigDecimal getIr2jikan() {
        return ir2jikan;
    }

    /**
     * IR② 時間
     * @param ir2jikan
     */
    public void setIr2jikan(BigDecimal ir2jikan) {
        this.ir2jikan = ir2jikan;
    }
    
    /**
     * IR② 時間 単位
     * @return ir2jikantani
     */
    public String getIr2jikantani() {
        return ir2jikantani;
    }

    /**
     * IR② 時間 単位
     * @param ir2jikantani
     */
    public void setIr2jikantani(String ir2jikantani) {
        this.ir2jikantani = ir2jikantani;
    }
    
    /**
     * IR③ 時間
     * @return ir3jikan
     */
    public BigDecimal getIr3jikan() {
        return ir3jikan;
    }

    /**
     * IR③ 時間
     * @param ir3jikan
     */
    public void setIr3jikan(BigDecimal ir3jikan) {
        this.ir3jikan = ir3jikan;
    }
    /**
     * IR③ 時間 単位
     * @return ir3jikantani
     */
    public String getIr3jikantani() {
        return ir3jikantani;
    }

    /**
     * IR③ 時間 単位
     * @param ir3jikantani
     */
    public void setIr3jikantani(String ir3jikantani) {
        this.ir3jikantani = ir3jikantani;
    }
    
    /**
     * IR④ 時間
     * @return ir4jikan
     */
    public BigDecimal getIr4jikan() {
        return ir4jikan;
    }

    /**
     * IR④ 時間
     * @param ir4jikan
     */
    public void setIr4jikan(BigDecimal ir4jikan) {
        this.ir4jikan = ir4jikan;
    }
    
    /**
     * IR④ 時間 単位
     * @return ir4jikantani
     */
    public String getIr4jikantani() {
        return ir4jikantani;
    }

    /**
     * IR④ 時間 単位
     * @param ir4jikantani
     */
    public void setIr4jikantani(String ir4jikantani) {
        this.ir4jikantani = ir4jikantani;
    }
    
    /**
     * IR⑤ 時間
     * @return ir5jikan
     */
    public BigDecimal getIr5jikan() {
        return ir5jikan;
    }

    /**
     * IR⑤ 時間
     * @param ir5jikan
     */
    public void setIr5jikan(BigDecimal ir5jikan) {
        this.ir5jikan = ir5jikan;
    }
    
    /**
     * IR⑤ 時間 単位
     * @return ir5jikantani
     */
    public String getIr5jikantani() {
        return ir5jikantani;
    }

    /**
     * IR⑤ 時間 単位
     * @param ir5jikantani
     */
    public void setIr5jikantani(String ir5jikantani) {
        this.ir5jikantani = ir5jikantani;
    }
    
    /**
     * IR⑥ 時間
     * @return ir6jikan
     */
    public BigDecimal getIr6jikan() {
        return ir6jikan;
    }

    /**
     * IR⑥ 時間
     * @param ir6jikan
     */
    public void setIr6jikan(BigDecimal ir6jikan) {
        this.ir6jikan = ir6jikan;
    }
    
    /**
     * IR⑥ 時間 単位
     * @return ir6jikantani
     */
    public String getIr6jikantani() {
        return ir6jikantani;
    }

    /**
     * IR⑥ 時間 単位
     * @param ir6jikantani
     */
    public void setIr6jikantani(String ir6jikantani) {
        this.ir6jikantani = ir6jikantani;
    }
    
    /**
     * IR⑦ 時間
     * @return ir7jikan
     */
    public BigDecimal getIr7jikan() {
        return ir7jikan;
    }

    /**
     * IR⑦ 時間
     * @param ir7jikan
     */
    public void setIr7jikan(BigDecimal ir7jikan) {
        this.ir7jikan = ir7jikan;
    }
    
    /**
     * IR⑦ 時間 単位
     * @return ir7jikantani
     */
    public String getIr7jikantani() {
        return ir7jikantani;
    }

    /**
     * IR⑦ 時間 単位
     * @param ir7jikantani
     */
    public void setIr7jikantani(String ir7jikantani) {
        this.ir7jikantani = ir7jikantani;
    }
    
    /**
     * IR⑧ 時間
     * @return ir8jikan
     */
    public BigDecimal getIr8jikan() {
        return ir8jikan;
    }

    /**
     * IR⑧ 時間
     * @param ir8jikan
     */
    public void setIr8jikan(BigDecimal ir8jikan) {
        this.ir8jikan = ir8jikan;
    }
    
    /**
     * IR⑧ 時間 単位
     * @return ir8jikantani
     */
    public String getIr8jikantani() {
        return ir8jikantani;
    }

    /**
     * IR⑧ 時間 単位
     * @param ir8jikantani
     */
    public void setIr8jikantani(String ir8jikantani) {
        this.ir8jikantani = ir8jikantani;
    }
}
