/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
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
 * 変更日	2019/12/28<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	863 K.Zhang<br>
 * 変更理由	新規作成<br>
 * <br>
 * 変更日	2020/10/09<br>
 * 計画書No	MB2004-DS015<br>
 * 変更者	863 sujialiang<br>
 * 変更理由	項目変更<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 電気特性・TWA履歴検索画面のモデルクラスです。
 *
 * @author KCCS A.Hayashi
 * @since  2024/12/09
 */
public class GXHDO201B053Model implements Serializable{

    /**
     * ﾛｯﾄNo
     */
    private String lotno = "";

    /**
     * 回数
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
     * 指定公差
     */
    private String siteikousa = "";

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
     * 検査場所
     */
    private String kensabasyo = "";

    /**
     * 選別開始日時
     */
    private Timestamp senbetukaisinitiji = null;

    /**
     * 選別終了日時
     */
    private Timestamp senbetusyuryounitiji = null;
    
    /**
     * 特性第1公差(指定公差歩留まり1)
     */
    private String siteikousabudomari1 = "";
    
    /**
     * 特性第2公差(指定公差歩留まり2)
     */
    private String siteikousabudomari2 = "";
    
    /**
     * 特性第3公差(指定公差歩留まり3)
     */
    private String siteikousabudomari3 = "";

    /**
     * 検査号機
     */
    private String kensagouki = "";
    
    /**
     * インデックステーブル管理No.(ﾃｽﾄﾌﾟﾚｰﾄ管理No)
     */
    private String testplatekanrino = "";
    
    /**
     * インデックステーブル目視(ﾃｽﾄﾌﾟﾚｰﾄ 形状･清掃)
     */
    private String testplatekeijo = "";

    /**
     * メインエアー圧(分類ｴｱｰ圧)
     */
    private BigDecimal bunruiairatu = null;

    /**
     * インデックステーブル位置確認(穴位置)(ﾃｽﾄﾌﾟﾚｰﾄ位置確認(穴位置))
     */
    private String testplatekakunin = "";
    
    /**
     * 原点復帰動作
     */
    private String gentenhukkidousa = "";

    /**
     * 測定機1,2動作確認
     */
    private String sokuteiki12dousakakunin = "";
    
    /**
     * 測定ピン(フロント)外観
     */
    private String sokuteipinfront = "";
    
    /**
     * 測定ピン(リア)外観
     */
    private String sokuteipinrear = "";
    
    /**
     * 測定周波数
     */
    private String sokuteisyuhasuu = "";

    /**
     * 測定電圧
     */
    private BigDecimal sokuteidenatu = null;

    /**
     * 同品種
     */
    private Integer douhinsyu;
    
    /**
     * 補正用ﾁｯﾌﾟ容量
     */
    private BigDecimal hoseiyoutippuyoryou = null;

    /**
     * 補正用ﾁｯﾌﾟTanδ
     */
    private BigDecimal hoseiyoutipputan = null;
    
    /**
     * 補正前
     */
    private BigDecimal hoseimae = null;

    /**
     * 補正後
     */
    private BigDecimal hoseigo = null;

    /**
     * 補正率
     */
    private BigDecimal hoseiritu = null;
    
    /**
     * Tanδ
     */
    private BigDecimal tan = null;
    
    /**
     * BINﾎﾞｯｸｽ内の清掃ﾁｪｯｸ
     */
    private String binboxseisoucheck = "";
    
    /**
     * 分類確認
     */
    private String bunruikakunin = "";

    /**
     * 外観確認
     */
    private String gaikankakunin = "";
    
    /**
     * 熱処理日時
     */
    private Timestamp netsusyorinitiji = null;

    /**
     * ｴｰｼﾞﾝｸﾞ時間
     */
    private BigDecimal agingjikan = null;
    
    /**
     * ｾｯﾄ者
     */
    private String setsya = "";

    /**
     * 確認者
     */
    private String kakuninsya = "";
    
    /**
     * 承認者
     */
    private String syoninsha = "";

    /**
     * 振向者
     */
    private String furimukesya = "";
    
    /**
     * 備考1
     */
    private String bikou1 = "";

    /**
     * 備考2
     */
    private String bikou2 = "";

    /**
     * IR① 電圧
     */
    private BigDecimal irdenatu1 = null;
    
    /**
     * IR① 時間
     */
    private BigDecimal ir1jikan = null;
    
    /**
     * IR① 時間 単位
     */
    private String ir1jikantani = "";

    /**
     * IR① 電流中心値スタート
     */
    private BigDecimal ir1denryustart = null;
    
    /**
     * IR① 電流中心値スタート 単位
     */
    private String ir1denryustarttani = "";

    /**
     * IR① 電流中心値エンド
     */
    private BigDecimal ir1denryuend = null;
    
    /**
     * IR① 電流中心値エンド 単位
     */
    private String ir1denryuendtani = "";

    /**
     * IR① 測定範囲スタート
     */
    private BigDecimal ir1sokuteihanistart = null;
    
    /**
     * IR① 測定範囲スタート 単位
     */
    private String ir1sokuteihanistarttani = "";

    /**
     * IR① 測定範囲エンド
     */
    private BigDecimal ir1sokuteihaniend = null;
    
    /**
     * IR① 測定範囲エンド 単位
     */
    private String ir1sokuteihaniendtani = "";

    /**
     * IR① 良品範囲上限(耐電圧設定条件 IR① 判定値)
     */
    private BigDecimal irhanteiti1 = null;
    
    /**
     * IR① 良品範囲上限 単位(耐電圧設定条件 IR① 判定値 単位)
     */
    private String irhanteiti1tani = "";
    
    /**
     * IR① 良品範囲下限(耐電圧設定条件 IR① 判定値(低))
     */
    private BigDecimal irhanteiti1low = null;

    /**
     * IR① 良品範囲下限 単位(耐電圧設定条件 IR① 判定値(低) 単位)
     */
    private String irhanteiti1tanilow = "";
/**
     * IR② 電圧
     */
    private BigDecimal irdenatu2 = null;
    
    /**
     * IR② 時間
     */
    private BigDecimal ir2jikan = null;
    
    /**
     * IR② 時間 単位
     */
    private String ir2jikantani = "";

    /**
     * IR② 電流中心値スタート
     */
    private BigDecimal ir2denryustart = null;
    
    /**
     * IR② 電流中心値スタート 単位
     */
    private String ir2denryustarttani = "";

    /**
     * IR② 電流中心値エンド
     */
    private BigDecimal ir2denryuend = null;
    
    /**
     * IR② 電流中心値エンド 単位
     */
    private String ir2denryuendtani = "";

    /**
     * IR② 測定範囲スタート
     */
    private BigDecimal ir2sokuteihanistart = null;
    
    /**
     * IR② 測定範囲スタート 単位
     */
    private String ir2sokuteihanistarttani = "";

    /**
     * IR② 測定範囲エンド
     */
    private BigDecimal ir2sokuteihaniend = null;
    
    /**
     * IR② 測定範囲エンド 単位
     */
    private String ir2sokuteihaniendtani = "";

    /**
     * IR② 良品範囲上限(耐電圧設定条件 IR② 判定値)
     */
    private BigDecimal irhanteiti2 = null;
    
    /**
     * IR② 良品範囲上限 単位(耐電圧設定条件 IR② 判定値 単位)
     */
    private String irhanteiti2tani = "";
    
    /**
     * IR② 良品範囲下限(耐電圧設定条件 IR② 判定値(低))
     */
    private BigDecimal irhanteiti2low = null;

    /**
     * IR② 良品範囲下限 単位(耐電圧設定条件 IR② 判定値(低) 単位)
     */
    private String irhanteiti2tanilow = "";

    /**
     * IR③ 電圧
     */
    private BigDecimal irdenatu3 = null;
    
    /**
     * IR③ 時間
     */
    private BigDecimal ir3jikan = null;
    
    /**
     * IR③ 時間 単位
     */
    private String ir3jikantani = "";

    /**
     * IR③ 電流中心値スタート
     */
    private BigDecimal ir3denryustart = null;
    
    /**
     * IR③ 電流中心値スタート 単位
     */
    private String ir3denryustarttani = "";

    /**
     * IR③ 電流中心値エンド
     */
    private BigDecimal ir3denryuend = null;
    
    /**
     * IR③ 電流中心値エンド 単位
     */
    private String ir3denryuendtani = "";

    /**
     * IR③ 測定範囲スタート
     */
    private BigDecimal ir3sokuteihanistart = null;
    
    /**
     * IR③ 測定範囲スタート 単位
     */
    private String ir3sokuteihanistarttani = "";

    /**
     * IR③ 測定範囲エンド
     */
    private BigDecimal ir3sokuteihaniend = null;
    
    /**
     * IR③ 測定範囲エンド 単位
     */
    private String ir3sokuteihaniendtani = "";

    /**
     * IR③ 良品範囲上限(耐電圧設定条件 IR③ 判定値)
     */
    private BigDecimal irhanteiti3 = null;
    
    /**
     * IR③ 良品範囲上限 単位(耐電圧設定条件 IR③ 判定値 単位)
     */
    private String irhanteiti3tani = "";
    
    /**
     * IR③ 良品範囲下限(耐電圧設定条件 IR③ 判定値(低))
     */
    private BigDecimal irhanteiti3low = null;

    /**
     * IR③ 良品範囲下限 単位(耐電圧設定条件 IR③ 判定値(低) 単位)
     */
    private String irhanteiti3tanilow = "";
    
    /**
     * IR④ 電圧
     */
    private BigDecimal irdenatu4 = null;
    
    /**
     * IR④ 時間
     */
    private BigDecimal ir4jikan = null;
    
    /**
     * IR④ 時間 単位
     */
    private String ir4jikantani = "";

    /**
     * IR④ 電流中心値スタート
     */
    private BigDecimal ir4denryustart = null;
    
    /**
     * IR④ 電流中心値スタート 単位
     */
    private String ir4denryustarttani = "";

    /**
     * IR④ 電流中心値エンド
     */
    private BigDecimal ir4denryuend = null;
    
    /**
     * IR④ 電流中心値エンド 単位
     */
    private String ir4denryuendtani = "";

    /**
     * IR④ 測定範囲スタート
     */
    private BigDecimal ir4sokuteihanistart = null;
    
    /**
     * IR④ 測定範囲スタート 単位
     */
    private String ir4sokuteihanistarttani = "";

    /**
     * IR④ 測定範囲エンド
     */
    private BigDecimal ir4sokuteihaniend = null;
    
    /**
     * IR④ 測定範囲エンド 単位
     */
    private String ir4sokuteihaniendtani = "";

    /**
     * IR④ 良品範囲上限(耐電圧設定条件 IR④ 判定値)
     */
    private BigDecimal irhanteiti4 = null;
    
    /**
     * IR④ 良品範囲上限 単位(耐電圧設定条件 IR④ 判定値 単位)
     */
    private String irhanteiti4tani = "";
    
    /**
     * IR④ 良品範囲下限(耐電圧設定条件 IR④ 判定値(低))
     */
    private BigDecimal irhanteiti4low = null;

    /**
     * IR④ 良品範囲下限 単位(耐電圧設定条件 IR④ 判定値(低) 単位)
     */
    private String irhanteiti4tanilow = ""; 
    
    /**
     * IR⑤ 電圧
     */
    private BigDecimal irdenatu5 = null;
    
    /**
     * IR⑤ 時間
     */
    private BigDecimal ir5jikan = null;
    
    /**
     * IR⑤ 時間 単位
     */
    private String ir5jikantani = "";

    /**
     * IR⑤ 電流中心値スタート
     */
    private BigDecimal ir5denryustart = null;
    
    /**
     * IR⑤ 電流中心値スタート 単位
     */
    private String ir5denryustarttani = "";

    /**
     * IR⑤ 電流中心値エンド
     */
    private BigDecimal ir5denryuend = null;
    
    /**
     * IR⑤ 電流中心値エンド 単位
     */
    private String ir5denryuendtani = "";

    /**
     * IR⑤ 測定範囲スタート
     */
    private BigDecimal ir5sokuteihanistart = null;
    
    /**
     * IR⑤ 測定範囲スタート 単位
     */
    private String ir5sokuteihanistarttani = "";

    /**
     * IR⑤ 測定範囲エンド
     */
    private BigDecimal ir5sokuteihaniend = null;
    
    /**
     * IR⑤ 測定範囲エンド 単位
     */
    private String ir5sokuteihaniendtani = "";

    /**
     * IR⑤ 良品範囲上限(耐電圧設定条件 IR⑤ 判定値)
     */
    private BigDecimal irhanteiti5 = null;
    
    /**
     * IR⑤ 良品範囲上限 単位(耐電圧設定条件 IR⑤ 判定値 単位)
     */
    private String irhanteiti5tani = "";
    
    /**
     * IR⑤ 良品範囲下限(耐電圧設定条件 IR⑤ 判定値(低))
     */
    private BigDecimal irhanteiti5low = null;

    /**
     * IR⑤ 良品範囲下限 単位(耐電圧設定条件 IR⑤ 判定値(低) 単位)
     */
    private String irhanteiti5tanilow = "";
    
    /**
     * IR⑥ 電圧
     */
    private BigDecimal irdenatu6 = null;
    
    /**
     * IR⑥ 時間
     */
    private BigDecimal ir6jikan = null;
    
    /**
     * IR⑥ 時間 単位
     */
    private String ir6jikantani = "";

    /**
     * IR⑥ 電流中心値スタート
     */
    private BigDecimal ir6denryustart = null;
    
    /**
     * IR⑥ 電流中心値スタート 単位
     */
    private String ir6denryustarttani = "";

    /**
     * IR⑥ 電流中心値エンド
     */
    private BigDecimal ir6denryuend = null;
    
    /**
     * IR⑥ 電流中心値エンド 単位
     */
    private String ir6denryuendtani = "";

    /**
     * IR⑥ 測定範囲スタート
     */
    private BigDecimal ir6sokuteihanistart = null;
    
    /**
     * IR⑥ 測定範囲スタート 単位
     */
    private String ir6sokuteihanistarttani = "";

    /**
     * IR⑥ 測定範囲エンド
     */
    private BigDecimal ir6sokuteihaniend = null;
    
    /**
     * IR⑥ 測定範囲エンド 単位
     */
    private String ir6sokuteihaniendtani = "";

    /**
     * IR⑥ 良品範囲上限(耐電圧設定条件 IR⑥ 判定値)
     */
    private BigDecimal irhanteiti6 = null;
    
    /**
     * IR⑥ 良品範囲上限 単位(耐電圧設定条件 IR⑥ 判定値 単位)
     */
    private String irhanteiti6tani = "";
    
    /**
     * IR⑥ 良品範囲下限(耐電圧設定条件 IR⑥ 判定値(低))
     */
    private BigDecimal irhanteiti6low = null;

    /**
     * IR⑥ 良品範囲下限 単位(耐電圧設定条件 IR⑥ 判定値(低) 単位)
     */
    private String irhanteiti6tanilow = "";
    
    /**
     * IR⑦ 電圧
     */
    private BigDecimal irdenatu7 = null;
    
    /**
     * IR⑦ 時間
     */
    private BigDecimal ir7jikan = null;
    
    /**
     * IR⑦ 時間 単位
     */
    private String ir7jikantani = "";

    /**
     * IR⑦ 電流中心値スタート
     */
    private BigDecimal ir7denryustart = null;
    
    /**
     * IR⑦ 電流中心値スタート 単位
     */
    private String ir7denryustarttani = "";

    /**
     * IR⑦ 電流中心値エンド
     */
    private BigDecimal ir7denryuend = null;
    
    /**
     * IR⑦ 電流中心値エンド 単位
     */
    private String ir7denryuendtani = "";

    /**
     * IR⑦ 測定範囲スタート
     */
    private BigDecimal ir7sokuteihanistart = null;
    
    /**
     * IR⑦ 測定範囲スタート 単位
     */
    private String ir7sokuteihanistarttani = "";

    /**
     * IR⑦ 測定範囲エンド
     */
    private BigDecimal ir7sokuteihaniend = null;
    
    /**
     * IR⑦ 測定範囲エンド 単位
     */
    private String ir7sokuteihaniendtani = "";

    /**
     * IR⑦ 良品範囲上限(耐電圧設定条件 IR⑦ 判定値)
     */
    private BigDecimal irhanteiti7 = null;
    
    /**
     * IR⑦ 良品範囲上限 単位(耐電圧設定条件 IR⑦ 判定値 単位)
     */
    private String irhanteiti7tani = "";
    
    /**
     * IR⑦ 良品範囲下限(耐電圧設定条件 IR⑦ 判定値(低))
     */
    private BigDecimal irhanteiti7low = null;

    /**
     * IR⑦ 良品範囲下限 単位(耐電圧設定条件 IR⑦ 判定値(低) 単位)
     */
    private String irhanteiti7tanilow = "";
    
    /**
     * IR⑧ 電圧
     */
    private BigDecimal irdenatu8 = null;
    
    /**
     * IR⑧ 時間
     */
    private BigDecimal ir8jikan = null;
    
    /**
     * IR⑧ 時間 単位
     */
    private String ir8jikantani = "";

    /**
     * IR⑧ 電流中心値スタート
     */
    private BigDecimal ir8denryustart = null;
    
    /**
     * IR⑧ 電流中心値スタート 単位
     */
    private String ir8denryustarttani = "";

    /**
     * IR⑧ 電流中心値エンド
     */
    private BigDecimal ir8denryuend = null;
    
    /**
     * IR⑧ 電流中心値エンド 単位
     */
    private String ir8denryuendtani = "";

    /**
     * IR⑧ 測定範囲スタート
     */
    private BigDecimal ir8sokuteihanistart = null;
    
    /**
     * IR⑧ 測定範囲スタート 単位
     */
    private String ir8sokuteihanistarttani = "";

    /**
     * IR⑧ 測定範囲エンド
     */
    private BigDecimal ir8sokuteihaniend = null;
    
    /**
     * IR⑧ 測定範囲エンド 単位
     */
    private String ir8sokuteihaniendtani = "";

    /**
     * IR⑧ 良品範囲上限(耐電圧設定条件 IR⑧ 判定値)
     */
    private BigDecimal irhanteiti8 = null;
    
    /**
     * IR⑧ 良品範囲上限 単位(耐電圧設定条件 IR⑧ 判定値 単位)
     */
    private String irhanteiti8tani = "";
    
    /**
     * IR⑧ 良品範囲下限(耐電圧設定条件 IR⑧ 判定値(低))
     */
    private BigDecimal irhanteiti8low = null;

    /**
     * IR⑧ 良品範囲下限 単位(耐電圧設定条件 IR⑧ 判定値(低) 単位)
     */
    private String irhanteiti8tanilow = "";
    
    /**
     * BIN1 %区分(設定値)
     */
    private String bin1setteiti = "";

    /**
     * BIN1 選別区分
     */
    private String bin1senbetukubun = "";

    /**
     * BIN1 計量後数量
     */
    private Integer bin1keiryougosuryou = null;

    /**
     * BIN1 ｶｳﾝﾀｰ数
     */
    private Integer bin1countersuu = null;

    /**
     * BIN1 誤差率(%)
     */
    private BigDecimal bin1gosaritu = null;

    /**
     * BIN1 ﾏｼﾝ不良率(%)
     */
    private BigDecimal bin1masinfuryouritu = null;

    /**
     * BIN1 抜き取り結果(子数)
     */
    private Integer bin1nukitorikekka = null;
    
    /**
     * BIN1 抜き取り結果(母数)
     */
    private Integer bin1nukitorikekkabosuu = null;

    /**
     * BIN1 真の不良率(%)
     */
    private BigDecimal bin1sinnofuryouritu = null;

    /**
     * BIN1 結果ﾁｪｯｸ
     */
    private String bin1kekkacheck = "";

    /**
     * BIN1 袋ﾁｪｯｸ
     */
    private Integer bin1fukurocheck = null;
    
    /**
     * BIN2 %区分(設定値)
     */
    private String bin2setteiti = "";

    /**
     * BIN2 選別区分
     */
    private String bin2senbetukubun = "";

    /**
     * BIN2 計量後数量
     */
    private Integer bin2keiryougosuryou = null;

    /**
     * BIN2 ｶｳﾝﾀｰ数
     */
    private Integer bin2countersuu = null;

    /**
     * BIN2 誤差率(%)
     */
    private BigDecimal bin2gosaritu = null;

    /**
     * BIN2 ﾏｼﾝ不良率(%)
     */
    private BigDecimal bin2masinfuryouritu = null;
    
    /**
     * BIN2 抜き取り結果(子数)
     */
    private Integer bin2nukitorikekka = null;

    /**
     * BIN2 抜き取り結果(母数)
     */
    private Integer bin2nukitorikekkabosuu = null;

    /**
     * BIN2 真の不良率(%)
     */
    private BigDecimal bin2sinnofuryouritu = null;

    /**
     * BIN2 結果ﾁｪｯｸ
     */
    private String bin2kekkacheck = "";

    /**
     * BIN2 袋ﾁｪｯｸ
     */
    private Integer bin2fukurocheck = null;

    /**
     * BIN3 %区分(設定値)
     */
    private String bin3setteiti = "";

    /**
     * BIN3 選別区分
     */
    private String bin3senbetukubun = "";

    /**
     * BIN3 計量後数量
     */
    private Integer bin3keiryougosuryou = null;

    /**
     * BIN3 ｶｳﾝﾀｰ数
     */
    private Integer bin3countersuu = null;

    /**
     * BIN3 誤差率(%)
     */
    private BigDecimal bin3gosaritu = null;

    /**
     * BIN3 ﾏｼﾝ不良率(%)
     */
    private BigDecimal bin3masinfuryouritu = null;

    /**
     * BIN3 抜き取り結果(子数)
     */
    private Integer bin3nukitorikekka = null;
    
    /**
     * BIN3 抜き取り結果(母数)
     */
    private Integer bin3nukitorikekkabosuu = null;

    /**
     * BIN3 真の不良率(%)
     */
    private BigDecimal bin3sinnofuryouritu = null;

    /**
     * BIN3 結果ﾁｪｯｸ
     */
    private String bin3kekkacheck = "";

    /**
     * BIN3 袋ﾁｪｯｸ
     */
    private Integer bin3fukurocheck = null;

    /**
     * BIN4 %区分(設定値)
     */
    private String bin4setteiti = "";

    /**
     * BIN4 選別区分
     */
    private String bin4senbetukubun = "";

    /**
     * BIN4 計量後数量
     */
    private Integer bin4keiryougosuryou = null;

    /**
     * BIN4 ｶｳﾝﾀｰ数
     */
    private Integer bin4countersuu = null;

    /**
     * BIN4 誤差率(%)
     */
    private BigDecimal bin4gosaritu = null;

    /**
     * BIN4 ﾏｼﾝ不良率(%)
     */
    private BigDecimal bin4masinfuryouritu = null;

    /**
     * BIN4 抜き取り結果(子数)
     */
    private Integer bin4nukitorikekka = null;
    
    /**
     * BIN4 抜き取り結果(母数)
     */
    private Integer bin4nukitorikekkabosuu = null;

    /**
     * BIN4 真の不良率(%)
     */
    private BigDecimal bin4sinnofuryouritu = null;

    /**
     * BIN4 結果ﾁｪｯｸ
     */
    private String bin4kekkacheck = "";

    /**
     * BIN4 袋ﾁｪｯｸ
     */
    private Integer bin4fukurocheck = null;

    /**
     * BIN5 %区分(設定値)
     */
    private String bin5setteiti = "";

    /**
     * BIN5 選別区分
     */
    private String bin5senbetukubun = "";

    /**
     * BIN5 計量後数量
     */
    private Integer bin5keiryougosuryou = null;

    /**
     * BIN5 ｶｳﾝﾀｰ数
     */
    private Integer bin5countersuu = null;

    /**
     * BIN5 誤差率(%)
     */
    private BigDecimal bin5gosaritu = null;

    /**
     * BIN5 ﾏｼﾝ不良率(%)
     */
    private BigDecimal bin5masinfuryouritu = null;

    /**
     * BIN5 抜き取り結果(子数)
     */
    private Integer bin5nukitorikekka = null;
    
    /**
     * BIN5 抜き取り結果(母数)
     */
    private Integer bin5nukitorikekkabosuu = null;

    /**
     * BIN5 真の不良率(%)
     */
    private BigDecimal bin5sinnofuryouritu = null;

    /**
     * BIN5 結果ﾁｪｯｸ
     */
    private String bin5kekkacheck = "";

    /**
     * BIN5 袋ﾁｪｯｸ
     */
    private Integer bin5fukurocheck = null;

    /**
     * BIN6 %区分(設定値)
     */
    private String bin6setteiti = "";

    /**
     * BIN6 選別区分
     */
    private String bin6senbetukubun = "";

    /**
     * BIN6 計量後数量
     */
    private Integer bin6keiryougosuryou = null;

    /**
     * BIN6 ｶｳﾝﾀｰ数
     */
    private Integer bin6countersuu = null;

    /**
     * BIN6 誤差率(%)
     */
    private BigDecimal bin6gosaritu = null;

    /**
     * BIN6 ﾏｼﾝ不良率(%)
     */
    private BigDecimal bin6masinfuryouritu = null;

    /**
     * BIN6 抜き取り結果(子数)
     */
    private Integer bin6nukitorikekka = null;
    
    /**
     * BIN6 抜き取り結果(母数)
     */
    private Integer bin6nukitorikekkabosuu = null;

    /**
     * BIN6 真の不良率(%)
     */
    private BigDecimal bin6sinnofuryouritu = null;

    /**
     * BIN6 結果ﾁｪｯｸ
     */
    private String bin6kekkacheck = "";

    /**
     * BIN6 袋ﾁｪｯｸ
     */
    private Integer bin6fukurocheck = null;

    /**
     * BIN7 %区分(設定値)
     */
    private String bin7setteiti = "";

    /**
     * BIN7 選別区分
     */
    private String bin7senbetukubun = "";

    /**
     * BIN7 計量後数量
     */
    private Integer bin7keiryougosuryou = null;

    /**
     * BIN7 ｶｳﾝﾀｰ数
     */
    private Integer bin7countersuu = null;

    /**
     * BIN7 誤差率(%)
     */
    private BigDecimal bin7gosaritu = null;

    /**
     * BIN7 ﾏｼﾝ不良率(%)
     */
    private BigDecimal bin7masinfuryouritu = null;

    /**
     * BIN7 抜き取り結果(子数)
     */
    private Integer bin7nukitorikekka = null;

    /**
     * BIN7 抜き取り結果(母数)
     */
    private Integer bin7nukitorikekkabosuu = null;

    /**
     * BIN7 真の不良率(%)
     */
    private BigDecimal bin7sinnofuryouritu = null;

    /**
     * BIN7 結果ﾁｪｯｸ
     */
    private String bin7kekkacheck = "";

    /**
     * BIN7 袋ﾁｪｯｸ
     */
    private Integer bin7fukurocheck = null;

    /**
     * BIN8 %区分(設定値)
     */
    private String bin8setteiti = "";

    /**
     * BIN8 選別区分
     */
    private String bin8senbetukubun = "";

    /**
     * BIN8 計量後数量
     */
    private Integer bin8keiryougosuryou = null;

    /**
     * BIN8 ｶｳﾝﾀｰ数
     */
    private Integer bin8countersuu = null;

    /**
     * BIN8 誤差率(%)
     */
    private BigDecimal bin8gosaritu = null;

    /**
     * BIN8 ﾏｼﾝ不良率(%)
     */
    private BigDecimal bin8masinfuryouritu = null;

    /**
     * BIN8 抜き取り結果(子数)
     */
    private Integer bin8nukitorikekka = null;

    /**
     * BIN8 抜き取り結果(母数)
     */
    private Integer bin8nukitorikekkabosuu = null;

    /**
     * BIN8 真の不良率(%)
     */
    private BigDecimal bin8sinnofuryouritu = null;

    /**
     * BIN8 結果ﾁｪｯｸ
     */
    private String bin8kekkacheck = "";

    /**
     * BIN8 袋ﾁｪｯｸ
     */
    private Integer bin8fukurocheck = null;

    /**
     * BIN9 強制排出 計量後数量
     */
    private Integer bin9keiryougosuryou = null;

    /**
     * BIN9 強制排出 ﾏｼﾝ不良率
     */
    private BigDecimal bin9masinfuryouritu = null;

    /**
     * 落下 計量後数量
     */
    private Integer rakkakeiryougosuryou = null;

    /**
     * 落下 ﾏｼﾝ不良率
     */
    private BigDecimal rakkamasinfuryouritu = null;

    /**
     * 半田ｻﾝﾌﾟﾙ
     */
    private String handasample = "";

    /**
     * 信頼性ｻﾝﾌﾟﾙ
     */
    private String sinraiseisample = "";

    /**
     * SATｻﾝﾌﾟﾙ
     */
    private String satsample = "";

    /**
     * 真不良判定者
     */
    private String sinfuryouhanteisya = "";

    /**
     * 判定入力者
     */
    private String hanteinyuuryokusya = "";

    /**
     * 取出者
     */
    private String toridasisya = "";

    /**
     * 公差①
     */
    private String kousa1 = "";

    /**
     * 重量①
     */
    private BigDecimal juryou1 = null;

    /**
     * 個数①
     */
    private Integer kosuu1 = null;

    /**
     * 公差②
     */
    private String kousa2 = "";

    /**
     * 重量②
     */
    private BigDecimal juryou2 = null;

    /**
     * 個数②
     */
    private Integer kosuu2 = null;

    /**
     * 公差③
     */
    private String kousa3 = "";

    /**
     * 重量③
     */
    private BigDecimal juryou3 = null;

    /**
     * 個数③
     */
    private Integer kosuu3 = null;

    /**
     * 公差④
     */
    private String kousa4 = "";

    /**
     * 重量④
     */
    private BigDecimal juryou4 = null;

    /**
     * 個数④
     */
    private Integer kosuu4 = null;

    /**
     * 計量総数(ｶｳﾝﾀｰ総数)
     */
    private Integer countersousuu = null;

    /**
     * 良品重量
     */
    private BigDecimal ryohinjuryou = null;

    /**
     * 良品個数
     */
    private Integer ryohinkosuu = null;

    /**
     * 歩留まり
     */
    private BigDecimal budomari = null;

    /**
     * BIN確認者
     */
    private String binkakuninsya = "";

    /**
     * 電気特性再検
     */
    private String saiken = "";

    /**
     * 設備区分
     */
    private String setubikubun = "";

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
     * 特性第1公差(指定公差歩留まり1)
     * @return the siteikousabudomari1
     */
    public String getSiteikousabudomari1() {
        return siteikousabudomari1;
    }

    /**
     * 特性第1公差(指定公差歩留まり1)
     * @param siteikousabudomari1 the siteikousabudomari1 to set
     */
    public void setSiteikousabudomari1(String siteikousabudomari1) {
        this.siteikousabudomari1 = siteikousabudomari1;
    }

    /**
     * 特性第2公差(指定公差歩留まり2)
     * @return the siteikousabudomari2
     */
    public String getSiteikousabudomari2() {
        return siteikousabudomari2;
    }

    /**
     * 特性第2公差(指定公差歩留まり2)
     * @param siteikousabudomari2 the siteikousabudomari2 to set
     */
    public void setSiteikousabudomari2(String siteikousabudomari2) {
        this.siteikousabudomari2 = siteikousabudomari2;
    }

    /**
     * 特性第3公差(指定公差歩留まり3)
     * @return the siteikousabudomari3
     */
    public String getSiteikousabudomari3() {
        return siteikousabudomari3;
    }

    /**
     * 特性第3公差(指定公差歩留まり3)
     * @param siteikousabudomari3 the siteikousabudomari3 to set
     */
    public void setSiteikousabudomari3(String siteikousabudomari3) {
        this.siteikousabudomari3 = siteikousabudomari3;
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
     * インデックステーブル管理No.(ﾃｽﾄﾌﾟﾚｰﾄ管理No)
     * @return the testplatekanrino
     */
    public String getTestplatekanrino() {
        return testplatekanrino;
    }

    /**
     * インデックステーブル管理No.(ﾃｽﾄﾌﾟﾚｰﾄ管理No)
     * @param testplatekanrino the testplatekanrino to set
     */
    public void setTestplatekanrino(String testplatekanrino) {
        this.testplatekanrino = testplatekanrino;
    }

    /**
     * インデックステーブル目視(ﾃｽﾄﾌﾟﾚｰﾄ 形状･清掃)
     * @return the testplatekeijo
     */
    public String getTestplatekeijo() {
        return testplatekeijo;
    }

    /**
     * インデックステーブル目視(ﾃｽﾄﾌﾟﾚｰﾄ 形状･清掃)
     * @param testplatekeijo the testplatekeijo to set
     */
    public void setTestplatekeijo(String testplatekeijo) {
        this.testplatekeijo = testplatekeijo;
    }

    /**
     * メインエアー圧(分類ｴｱｰ圧)
     * @return the bunruiairatu
     */
    public BigDecimal getBunruiairatu() {
        return bunruiairatu;
    }

    /**
     * メインエアー圧(分類ｴｱｰ圧)
     * @param bunruiairatu the bunruiairatu to set
     */
    public void setBunruiairatu(BigDecimal bunruiairatu) {
        this.bunruiairatu = bunruiairatu;
    }

    /**
     * インデックステーブル位置確認(穴位置)(ﾃｽﾄﾌﾟﾚｰﾄ位置確認(穴位置))
     * @return the testplatekakunin
     */
    public String getTestplatekakunin() {
        return testplatekakunin;
    }

    /**
     * インデックステーブル位置確認(穴位置)(ﾃｽﾄﾌﾟﾚｰﾄ位置確認(穴位置))
     * @param testplatekakunin the testplatekakunin to set
     */
    public void setTestplatekakunin(String testplatekakunin) {
        this.testplatekakunin = testplatekakunin;
    }

    /**
     * 原点復帰動作
     * @return the gentenhukkidousa
     */
    public String getgentenhukkidousa() {
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
     * IR① 時間
     * @return the ir1jikan
     */
    public BigDecimal getIr1jikan() {
        return ir1jikan;
    }

    /**
     * IR① 時間
     * @param ir1jikan the ir1jikan to set
     */
    public void setIr1jikan(BigDecimal ir1jikan) {
        this.ir1jikan = ir1jikan;
    }
    
    /**
     * IR① 時間 単位
     * @return the ir1jikantani
     */
    public String getIr1jikantani() {
        return ir1jikantani;
    }

    /**
     * IR① 時間 単位
     * @param ir1jikantani the ir1jikantani to set
     */
    public void setIr1jikantani(String ir1jikantani) {
        this.ir1jikantani = ir1jikantani;
    }
    
    /**
     * IR① 電流中心値スタート
     * @return the ir1denryustart
     */
    public BigDecimal getIr1denryustart() {
        return ir1denryustart;
    }

    /**
     * IR① 電流中心値スタート
     * @param ir1denryustart the ir1denryustart to set
     */
    public void setIr1denryustart(BigDecimal ir1denryustart) {
        this.ir1denryustart = ir1denryustart;
    }
    
    /**
     * IR① 電流中心値スタート 単位
     * @return the ir1denryustarttani
     */
    public String getIr1denryustarttani() {
        return ir1denryustarttani;
    }

    /**
     * IR① 電流中心値スタート 単位
     * @param ir1denryustarttani the ir1denryustarttani to set
     */
    public void setIr1denryustarttani(String ir1denryustarttani) {
        this.ir1denryustarttani = ir1denryustarttani;
    }

    /**
     * IR① 電流中心値エンド
     * @return the ir1denryuend
     */
    public BigDecimal getIr1denryuend() {
        return ir1denryuend;
    }

    /**
     * IR① 電流中心値エンド
     * @param ir1denryuend the ir1denryuend to set
     */
    public void setIr1denryuend(BigDecimal ir1denryuend) {
        this.ir1denryuend = ir1denryuend;
    }
    
    /**
     * IR① 電流中心値エンド 単位
     * @return the ir1denryuendtani
     */
    public String getIr1denryuendtani() {
        return ir1denryuendtani;
    }

    /**
     * IR① 電流中心値エンド 単位
     * @param ir1denryuendtani the ir1denryuendtani to set
     */
    public void setIr1denryuendtani(String ir1denryuendtani) {
        this.ir1denryuendtani = ir1denryuendtani;
    }

    /**
     * IR① 測定範囲スタート
     * @return the ir1sokuteihanistart
     */
    public BigDecimal getIr1sokuteihanistart() {
        return ir1sokuteihanistart;
    }

    /**
     * IR① 測定範囲スタート
     * @param ir1sokuteihanistart the ir1sokuteihanistart to set
     */
    public void setIr1sokuteihanistart(BigDecimal ir1sokuteihanistart) {
        this.ir1sokuteihanistart = ir1sokuteihanistart;
    }
    
    /**
     * IR① 測定範囲スタート 単位
     * @return the ir1sokuteihanistarttani
     */
    public String getIr1sokuteihanistarttani() {
        return ir1sokuteihanistarttani;
    }

    /**
     * IR① 測定範囲スタート 単位
     * @param ir1sokuteihanistarttani the ir1sokuteihanistarttani to set
     */
    public void setIr1sokuteihanistarttani(String ir1sokuteihanistarttani) {
        this.ir1sokuteihanistarttani = ir1sokuteihanistarttani;
    }

    /**
     * IR① 測定範囲エンド
     * @return the ir1sokuteihaniend
     */
    public BigDecimal getIr1sokuteihaniend() {
        return ir1sokuteihaniend;
    }

    /**
     * IR① 測定範囲エンド
     * @param ir1sokuteihaniend the ir1sokuteihaniend to set
     */
    public void setIr1sokuteihaniend(BigDecimal ir1sokuteihaniend) {
        this.ir1sokuteihaniend = ir1sokuteihaniend;
    }
    
    /**
     * IR① 測定範囲エンド 単位
     * @return the ir1sokuteihaniendtani
     */
    public String getIr1sokuteihaniendtani() {
        return ir1sokuteihaniendtani;
    }

    /**
     * IR① 測定範囲エンド 単位
     * @param ir1sokuteihaniendtani the ir1sokuteihaniendtani to set
     */
    public void setIr1sokuteihaniendtani(String ir1sokuteihaniendtani) {
        this.ir1sokuteihaniendtani = ir1sokuteihaniendtani;
    }

    /**
     * IR① 良品範囲上限(耐電圧設定条件 IR① 判定値)
     * @return the irhanteiti1
     */
    public BigDecimal getIrhanteiti1() {
        return irhanteiti1;
    }

    /**
     * IR① 良品範囲上限(耐電圧設定条件 IR① 判定値)
     * @param irhanteiti1 the irhanteiti1 to set
     */
    public void setIrhanteiti1(BigDecimal irhanteiti1) {
        this.irhanteiti1 = irhanteiti1;
    }

    /**
     * IR① 良品範囲上限 単位(耐電圧設定条件 IR① 判定値 単位)
     * @return irhanteiti1tani
     */
    public String getIrhanteiti1tani() {
        return irhanteiti1tani;
    }

    /**
     * IR① 良品範囲上限 単位(耐電圧設定条件 IR① 判定値 単位)
     * @param irhanteiti1tani 
     */
    public void setIrhanteiti1tani(String irhanteiti1tani) {
        this.irhanteiti1tani = irhanteiti1tani;
    }

    /**
     * IR① 良品範囲下限(耐電圧設定条件 IR① 判定値(低))
     * @return irhanteiti1low
     */
    public BigDecimal getIrhanteiti1low() {
        return irhanteiti1low;
    }

    /**
     * IR① 良品範囲下限(耐電圧設定条件 IR① 判定値(低))
     * @param irhanteiti1low 
     */
    public void setIrhanteiti1low(BigDecimal irhanteiti1low) {
        this.irhanteiti1low = irhanteiti1low;
    }

    /**
     * IR① 良品範囲下限 単位(耐電圧設定条件 IR① 判定値 単位)
     * @return irhanteiti1tanilow
     */
    public String getIrhanteiti1tanilow() {
        return irhanteiti1tanilow;
    }

    /**
     * IR① 良品範囲下限 単位(耐電圧設定条件 IR① 判定値 単位)
     * @param irhanteiti1tanilow 
     */
    public void setIrhanteiti1tanilow(String irhanteiti1tanilow) {
        this.irhanteiti1tanilow = irhanteiti1tanilow;
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
     * IR② 時間
     * @return the ir2jikan
     */
    public BigDecimal getIr2jikan() {
        return ir2jikan;
    }

    /**
     * IR② 時間
     * @param ir2jikan the ir2jikan to set
     */
    public void setIr2jikan(BigDecimal ir2jikan) {
        this.ir2jikan = ir2jikan;
    }
    
    /**
     * IR② 時間 単位
     * @return the ir2jikantani
     */
    public String getIr2jikantani() {
        return ir2jikantani;
    }

    /**
     * IR② 時間 単位
     * @param ir2jikantani the ir2jikantani to set
     */
    public void setIr2jikantani(String ir2jikantani) {
        this.ir2jikantani = ir2jikantani;
    }
    
    /**
     * IR② 電流中心値スタート
     * @return the ir2denryustart
     */
    public BigDecimal getIr2denryustart() {
        return ir2denryustart;
    }

    /**
     * IR② 電流中心値スタート
     * @param ir2denryustart the ir2denryustart to set
     */
    public void setIr2denryustart(BigDecimal ir2denryustart) {
        this.ir2denryustart = ir2denryustart;
    }
    
    /**
     * IR② 電流中心値スタート 単位
     * @return the ir2denryustarttani
     */
    public String getIr2denryustarttani() {
        return ir2denryustarttani;
    }

    /**
     * IR② 電流中心値スタート 単位
     * @param ir2denryustarttani the ir2denryustarttani to set
     */
    public void setIr2denryustarttani(String ir2denryustarttani) {
        this.ir2denryustarttani = ir2denryustarttani;
    }

    /**
     * IR② 電流中心値エンド
     * @return the ir2denryuend
     */
    public BigDecimal getIr2denryuend() {
        return ir2denryuend;
    }

    /**
     * IR② 電流中心値エンド
     * @param ir2denryuend the ir2denryuend to set
     */
    public void setIr2denryuend(BigDecimal ir2denryuend) {
        this.ir2denryuend = ir2denryuend;
    }
    
    /**
     * IR② 電流中心値エンド 単位
     * @return the ir2denryuendtani
     */
    public String getIr2denryuendtani() {
        return ir2denryuendtani;
    }

    /**
     * IR② 電流中心値エンド 単位
     * @param ir2denryuendtani the ir2denryuendtani to set
     */
    public void setIr2denryuendtani(String ir2denryuendtani) {
        this.ir2denryuendtani = ir2denryuendtani;
    }

    /**
     * IR② 測定範囲スタート
     * @return the ir2sokuteihanistart
     */
    public BigDecimal getIr2sokuteihanistart() {
        return ir2sokuteihanistart;
    }

    /**
     * IR② 測定範囲スタート
     * @param ir2sokuteihanistart the ir2sokuteihanistart to set
     */
    public void setIr2sokuteihanistart(BigDecimal ir2sokuteihanistart) {
        this.ir2sokuteihanistart = ir2sokuteihanistart;
    }
    
    /**
     * IR② 測定範囲スタート 単位
     * @return the ir2sokuteihanistarttani
     */
    public String getIr2sokuteihanistarttani() {
        return ir2sokuteihanistarttani;
    }

    /**
     * IR② 測定範囲スタート 単位
     * @param ir2sokuteihanistarttani the ir2sokuteihanistarttani to set
     */
    public void setIr2sokuteihanistarttani(String ir2sokuteihanistarttani) {
        this.ir2sokuteihanistarttani = ir2sokuteihanistarttani;
    }

    /**
     * IR② 測定範囲エンド
     * @return the ir2sokuteihaniend
     */
    public BigDecimal getIr2sokuteihaniend() {
        return ir2sokuteihaniend;
    }

    /**
     * IR② 測定範囲エンド
     * @param ir2sokuteihaniend the ir2sokuteihaniend to set
     */
    public void setIr2sokuteihaniend(BigDecimal ir2sokuteihaniend) {
        this.ir2sokuteihaniend = ir2sokuteihaniend;
    }
    
    /**
     * IR② 測定範囲エンド 単位
     * @return the ir2sokuteihaniendtani
     */
    public String getIr2sokuteihaniendtani() {
        return ir2sokuteihaniendtani;
    }

    /**
     * IR② 測定範囲エンド 単位
     * @param ir2sokuteihaniendtani the ir2sokuteihaniendtani to set
     */
    public void setIr2sokuteihaniendtani(String ir2sokuteihaniendtani) {
        this.ir2sokuteihaniendtani = ir2sokuteihaniendtani;
    }

    /**
     * IR② 良品範囲上限(耐電圧設定条件 IR② 判定値)
     * @return the irhanteiti2
     */
    public BigDecimal getIrhanteiti2() {
        return irhanteiti2;
    }

    /**
     * IR② 良品範囲上限(耐電圧設定条件 IR② 判定値)
     * @param irhanteiti2 the irhanteiti2 to set
     */
    public void setIrhanteiti2(BigDecimal irhanteiti2) {
        this.irhanteiti2 = irhanteiti2;
    }

    /**
     * IR② 良品範囲上限 単位(耐電圧設定条件 IR② 判定値 単位)
     * @return irhanteiti2tani
     */
    public String getIrhanteiti2tani() {
        return irhanteiti2tani;
    }

    /**
     * IR② 良品範囲上限 単位(耐電圧設定条件 IR② 判定値 単位)
     * @param irhanteiti2tani 
     */
    public void setIrhanteiti2tani(String irhanteiti2tani) {
        this.irhanteiti2tani = irhanteiti2tani;
    }

    /**
     * IR② 良品範囲下限(耐電圧設定条件 IR② 判定値(低))
     * @return irhanteiti2low
     */
    public BigDecimal getIrhanteiti2low() {
        return irhanteiti2low;
    }

    /**
     * IR② 良品範囲下限(耐電圧設定条件 IR② 判定値(低))
     * @param irhanteiti2low 
     */
    public void setIrhanteiti2low(BigDecimal irhanteiti2low) {
        this.irhanteiti2low = irhanteiti2low;
    }

    /**
     * IR② 良品範囲下限 単位(耐電圧設定条件 IR② 判定値 単位)
     * @return irhanteiti2tanilow
     */
    public String getIrhanteiti2tanilow() {
        return irhanteiti2tanilow;
    }

    /**
     * IR② 良品範囲下限 単位(耐電圧設定条件 IR② 判定値 単位)
     * @param irhanteiti2tanilow 
     */
    public void setIrhanteiti2tanilow(String irhanteiti2tanilow) {
        this.irhanteiti2tanilow = irhanteiti2tanilow;
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
     * IR③ 時間
     * @return the ir3jikan
     */
    public BigDecimal getIr3jikan() {
        return ir3jikan;
    }

    /**
     * IR③ 時間
     * @param ir3jikan the ir3jikan to set
     */
    public void setIr3jikan(BigDecimal ir3jikan) {
        this.ir3jikan = ir3jikan;
    }
    
    /**
     * IR③ 時間 単位
     * @return the ir3jikantani
     */
    public String getIr3jikantani() {
        return ir3jikantani;
    }

    /**
     * IR③ 時間 単位
     * @param ir3jikantani the ir3jikantani to set
     */
    public void setIr3jikantani(String ir3jikantani) {
        this.ir3jikantani = ir3jikantani;
    }
    
    /**
     * IR③ 電流中心値スタート
     * @return the ir3denryustart
     */
    public BigDecimal getIr3denryustart() {
        return ir3denryustart;
    }

    /**
     * IR③ 電流中心値スタート
     * @param ir3denryustart the ir3denryustart to set
     */
    public void setIr3denryustart(BigDecimal ir3denryustart) {
        this.ir3denryustart = ir3denryustart;
    }
    
    /**
     * IR③ 電流中心値スタート 単位
     * @return the ir3denryustarttani
     */
    public String getIr3denryustarttani() {
        return ir3denryustarttani;
    }

    /**
     * IR③ 電流中心値スタート 単位
     * @param ir3denryustarttani the ir3denryustarttani to set
     */
    public void setIr3denryustarttani(String ir3denryustarttani) {
        this.ir3denryustarttani = ir3denryustarttani;
    }

    /**
     * IR③ 電流中心値エンド
     * @return the ir3denryuend
     */
    public BigDecimal getIr3denryuend() {
        return ir3denryuend;
    }

    /**
     * IR③ 電流中心値エンド
     * @param ir3denryuend the ir3denryuend to set
     */
    public void setIr3denryuend(BigDecimal ir3denryuend) {
        this.ir3denryuend = ir3denryuend;
    }
    
    /**
     * IR③ 電流中心値エンド 単位
     * @return the ir3denryuendtani
     */
    public String getIr3denryuendtani() {
        return ir3denryuendtani;
    }

    /**
     * IR③ 電流中心値エンド 単位
     * @param ir3denryuendtani the ir3denryuendtani to set
     */
    public void setIr3denryuendtani(String ir3denryuendtani) {
        this.ir3denryuendtani = ir3denryuendtani;
    }

    /**
     * IR③ 測定範囲スタート
     * @return the ir3sokuteihanistart
     */
    public BigDecimal getIr3sokuteihanistart() {
        return ir3sokuteihanistart;
    }

    /**
     * IR③ 測定範囲スタート
     * @param ir3sokuteihanistart the ir3sokuteihanistart to set
     */
    public void setIr3sokuteihanistart(BigDecimal ir3sokuteihanistart) {
        this.ir3sokuteihanistart = ir3sokuteihanistart;
    }
    
    /**
     * IR③ 測定範囲スタート 単位
     * @return the ir3sokuteihanistarttani
     */
    public String getIr3sokuteihanistarttani() {
        return ir3sokuteihanistarttani;
    }

    /**
     * IR③ 測定範囲スタート 単位
     * @param ir3sokuteihanistarttani the ir3sokuteihanistarttani to set
     */
    public void setIr3sokuteihanistarttani(String ir3sokuteihanistarttani) {
        this.ir3sokuteihanistarttani = ir3sokuteihanistarttani;
    }

    /**
     * IR③ 測定範囲エンド
     * @return the ir3sokuteihaniend
     */
    public BigDecimal getIr3sokuteihaniend() {
        return ir3sokuteihaniend;
    }

    /**
     * IR③ 測定範囲エンド
     * @param ir3sokuteihaniend the ir3sokuteihaniend to set
     */
    public void setIr3sokuteihaniend(BigDecimal ir3sokuteihaniend) {
        this.ir3sokuteihaniend = ir3sokuteihaniend;
    }
    
    /**
     * IR③ 測定範囲エンド 単位
     * @return the ir3sokuteihaniendtani
     */
    public String getIr3sokuteihaniendtani() {
        return ir3sokuteihaniendtani;
    }

    /**
     * IR③ 測定範囲エンド 単位
     * @param ir3sokuteihaniendtani the ir3sokuteihaniendtani to set
     */
    public void setIr3sokuteihaniendtani(String ir3sokuteihaniendtani) {
        this.ir3sokuteihaniendtani = ir3sokuteihaniendtani;
    }

    /**
     * IR③ 良品範囲上限(耐電圧設定条件 IR③ 判定値)
     * @return the irhanteiti3
     */
    public BigDecimal getIrhanteiti3() {
        return irhanteiti3;
    }

    /**
     * IR③ 良品範囲上限(耐電圧設定条件 IR③ 判定値)
     * @param irhanteiti3 the irhanteiti3 to set
     */
    public void setIrhanteiti3(BigDecimal irhanteiti3) {
        this.irhanteiti3 = irhanteiti3;
    }

    /**
     * IR③ 良品範囲上限 単位(耐電圧設定条件 IR③ 判定値 単位)
     * @return irhanteiti3tani
     */
    public String getIrhanteiti3tani() {
        return irhanteiti3tani;
    }

    /**
     * IR③ 良品範囲上限 単位(耐電圧設定条件 IR③ 判定値 単位)
     * @param irhanteiti3tani 
     */
    public void setIrhanteiti3tani(String irhanteiti3tani) {
        this.irhanteiti3tani = irhanteiti3tani;
    }

    /**
     * IR③ 良品範囲下限(耐電圧設定条件 IR③ 判定値(低))
     * @return irhanteiti3low
     */
    public BigDecimal getIrhanteiti3low() {
        return irhanteiti3low;
    }

    /**
     * IR③ 良品範囲下限(耐電圧設定条件 IR③ 判定値(低))
     * @param irhanteiti3low 
     */
    public void setIrhanteiti3low(BigDecimal irhanteiti3low) {
        this.irhanteiti3low = irhanteiti3low;
    }

    /**
     * IR③ 良品範囲下限 単位(耐電圧設定条件 IR③ 判定値 単位)
     * @return irhanteiti3tanilow
     */
    public String getIrhanteiti3tanilow() {
        return irhanteiti3tanilow;
    }

    /**
     * IR③ 良品範囲下限 単位(耐電圧設定条件 IR③ 判定値 単位)
     * @param irhanteiti3tanilow 
     */
    public void setIrhanteiti3tanilow(String irhanteiti3tanilow) {
        this.irhanteiti3tanilow = irhanteiti3tanilow;
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
     * IR④ 時間
     * @return the ir4jikan
     */
    public BigDecimal getIr4jikan() {
        return ir4jikan;
    }

    /**
     * IR④ 時間
     * @param ir4jikan the ir4jikan to set
     */
    public void setIr4jikan(BigDecimal ir4jikan) {
        this.ir4jikan = ir4jikan;
    }
    
    /**
     * IR④ 時間 単位
     * @return the ir4jikantani
     */
    public String getIr4jikantani() {
        return ir4jikantani;
    }

    /**
     * IR④ 時間 単位
     * @param ir4jikantani the ir4jikantani to set
     */
    public void setIr4jikantani(String ir4jikantani) {
        this.ir4jikantani = ir4jikantani;
    }
    
    /**
     * IR④ 電流中心値スタート
     * @return the ir4denryustart
     */
    public BigDecimal getIr4denryustart() {
        return ir4denryustart;
    }

    /**
     * IR④ 電流中心値スタート
     * @param ir4denryustart the ir4denryustart to set
     */
    public void setIr4denryustart(BigDecimal ir4denryustart) {
        this.ir4denryustart = ir4denryustart;
    }
    
    /**
     * IR④ 電流中心値スタート 単位
     * @return the ir4denryustarttani
     */
    public String getIr4denryustarttani() {
        return ir4denryustarttani;
    }

    /**
     * IR④ 電流中心値スタート 単位
     * @param ir4denryustarttani the ir4denryustarttani to set
     */
    public void setIr4denryustarttani(String ir4denryustarttani) {
        this.ir4denryustarttani = ir4denryustarttani;
    }

    /**
     * IR④ 電流中心値エンド
     * @return the ir4denryuend
     */
    public BigDecimal getIr4denryuend() {
        return ir4denryuend;
    }

    /**
     * IR④ 電流中心値エンド
     * @param ir4denryuend the ir4denryuend to set
     */
    public void setIr4denryuend(BigDecimal ir4denryuend) {
        this.ir4denryuend = ir4denryuend;
    }
    
    /**
     * IR④ 電流中心値エンド 単位
     * @return the ir4denryuendtani
     */
    public String getIr4denryuendtani() {
        return ir4denryuendtani;
    }

    /**
     * IR④ 電流中心値エンド 単位
     * @param ir4denryuendtani the ir4denryuendtani to set
     */
    public void setIr4denryuendtani(String ir4denryuendtani) {
        this.ir4denryuendtani = ir4denryuendtani;
    }

    /**
     * IR④ 測定範囲スタート
     * @return the ir4sokuteihanistart
     */
    public BigDecimal getIr4sokuteihanistart() {
        return ir4sokuteihanistart;
    }

    /**
     * IR④ 測定範囲スタート
     * @param ir4sokuteihanistart the ir4sokuteihanistart to set
     */
    public void setIr4sokuteihanistart(BigDecimal ir4sokuteihanistart) {
        this.ir4sokuteihanistart = ir4sokuteihanistart;
    }
    
    /**
     * IR④ 測定範囲スタート 単位
     * @return the ir4sokuteihanistarttani
     */
    public String getIr4sokuteihanistarttani() {
        return ir4sokuteihanistarttani;
    }

    /**
     * IR④ 測定範囲スタート 単位
     * @param ir4sokuteihanistarttani the ir4sokuteihanistarttani to set
     */
    public void setIr4sokuteihanistarttani(String ir4sokuteihanistarttani) {
        this.ir4sokuteihanistarttani = ir4sokuteihanistarttani;
    }

    /**
     * IR④ 測定範囲エンド
     * @return the ir4sokuteihaniend
     */
    public BigDecimal getIr4sokuteihaniend() {
        return ir4sokuteihaniend;
    }

    /**
     * IR④ 測定範囲エンド
     * @param ir4sokuteihaniend the ir4sokuteihaniend to set
     */
    public void setIr4sokuteihaniend(BigDecimal ir4sokuteihaniend) {
        this.ir4sokuteihaniend = ir4sokuteihaniend;
    }
    
    /**
     * IR④ 測定範囲エンド 単位
     * @return the ir4sokuteihaniendtani
     */
    public String getIr4sokuteihaniendtani() {
        return ir4sokuteihaniendtani;
    }

    /**
     * IR④ 測定範囲エンド 単位
     * @param ir4sokuteihaniendtani the ir4sokuteihaniendtani to set
     */
    public void setIr4sokuteihaniendtani(String ir4sokuteihaniendtani) {
        this.ir4sokuteihaniendtani = ir4sokuteihaniendtani;
    }

    /**
     * IR④ 良品範囲上限(耐電圧設定条件 IR④ 判定値)
     * @return the irhanteiti4
     */
    public BigDecimal getIrhanteiti4() {
        return irhanteiti4;
    }

    /**
     * IR④ 良品範囲上限(耐電圧設定条件 IR④ 判定値)
     * @param irhanteiti4 the irhanteiti4 to set
     */
    public void setIrhanteiti4(BigDecimal irhanteiti4) {
        this.irhanteiti4 = irhanteiti4;
    }

    /**
     * IR④ 良品範囲上限 単位(耐電圧設定条件 IR④ 判定値 単位)
     * @return irhanteiti4tani
     */
    public String getIrhanteiti4tani() {
        return irhanteiti4tani;
    }

    /**
     * IR④ 良品範囲上限 単位(耐電圧設定条件 IR④ 判定値 単位)
     * @param irhanteiti4tani 
     */
    public void setIrhanteiti4tani(String irhanteiti4tani) {
        this.irhanteiti4tani = irhanteiti4tani;
    }

    /**
     * IR④ 良品範囲下限(耐電圧設定条件 IR④ 判定値(低))
     * @return irhanteiti4low
     */
    public BigDecimal getIrhanteiti4low() {
        return irhanteiti4low;
    }

    /**
     * IR④ 良品範囲下限(耐電圧設定条件 IR④ 判定値(低))
     * @param irhanteiti4low 
     */
    public void setIrhanteiti4low(BigDecimal irhanteiti4low) {
        this.irhanteiti4low = irhanteiti4low;
    }

    /**
     * IR④ 良品範囲下限 単位(耐電圧設定条件 IR④ 判定値 単位)
     * @return irhanteiti4tanilow
     */
    public String getIrhanteiti4tanilow() {
        return irhanteiti4tanilow;
    }

    /**
     * IR④ 良品範囲下限 単位(耐電圧設定条件 IR④ 判定値 単位)
     * @param irhanteiti4tanilow 
     */
    public void setIrhanteiti4tanilow(String irhanteiti4tanilow) {
        this.irhanteiti4tanilow = irhanteiti4tanilow;
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
     * IR⑤ 時間
     * @return the ir5jikan
     */
    public BigDecimal getIr5jikan() {
        return ir5jikan;
    }

    /**
     * IR⑤ 時間
     * @param ir5jikan the ir5jikan to set
     */
    public void setIr5jikan(BigDecimal ir5jikan) {
        this.ir5jikan = ir5jikan;
    }
    
    /**
     * IR⑤ 時間 単位
     * @return the ir5jikantani
     */
    public String getIr5jikantani() {
        return ir5jikantani;
    }

    /**
     * IR⑤ 時間 単位
     * @param ir5jikantani the ir5jikantani to set
     */
    public void setIr5jikantani(String ir5jikantani) {
        this.ir5jikantani = ir5jikantani;
    }
    
    /**
     * IR⑤ 電流中心値スタート
     * @return the ir5denryustart
     */
    public BigDecimal getIr5denryustart() {
        return ir5denryustart;
    }

    /**
     * IR⑤ 電流中心値スタート
     * @param ir5denryustart the ir5denryustart to set
     */
    public void setIr5denryustart(BigDecimal ir5denryustart) {
        this.ir5denryustart = ir5denryustart;
    }
    
    /**
     * IR⑤ 電流中心値スタート 単位
     * @return the ir5denryustarttani
     */
    public String getIr5denryustarttani() {
        return ir5denryustarttani;
    }

    /**
     * IR⑤ 電流中心値スタート 単位
     * @param ir5denryustarttani the ir5denryustarttani to set
     */
    public void setIr5denryustarttani(String ir5denryustarttani) {
        this.ir5denryustarttani = ir5denryustarttani;
    }

    /**
     * IR⑤ 電流中心値エンド
     * @return the ir5denryuend
     */
    public BigDecimal getIr5denryuend() {
        return ir5denryuend;
    }

    /**
     * IR⑤ 電流中心値エンド
     * @param ir5denryuend the ir5denryuend to set
     */
    public void setIr5denryuend(BigDecimal ir5denryuend) {
        this.ir5denryuend = ir5denryuend;
    }
    
    /**
     * IR⑤ 電流中心値エンド 単位
     * @return the ir5denryuendtani
     */
    public String getIr5denryuendtani() {
        return ir5denryuendtani;
    }

    /**
     * IR⑤ 電流中心値エンド 単位
     * @param ir5denryuendtani the ir5denryuendtani to set
     */
    public void setIr5denryuendtani(String ir5denryuendtani) {
        this.ir5denryuendtani = ir5denryuendtani;
    }

    /**
     * IR⑤ 測定範囲スタート
     * @return the ir5sokuteihanistart
     */
    public BigDecimal getIr5sokuteihanistart() {
        return ir5sokuteihanistart;
    }

    /**
     * IR⑤ 測定範囲スタート
     * @param ir5sokuteihanistart the ir5sokuteihanistart to set
     */
    public void setIr5sokuteihanistart(BigDecimal ir5sokuteihanistart) {
        this.ir5sokuteihanistart = ir5sokuteihanistart;
    }
    
    /**
     * IR⑤ 測定範囲スタート 単位
     * @return the ir5sokuteihanistarttani
     */
    public String getIr5sokuteihanistarttani() {
        return ir5sokuteihanistarttani;
    }

    /**
     * IR⑤ 測定範囲スタート 単位
     * @param ir5sokuteihanistarttani the ir5sokuteihanistarttani to set
     */
    public void setIr5sokuteihanistarttani(String ir5sokuteihanistarttani) {
        this.ir5sokuteihanistarttani = ir5sokuteihanistarttani;
    }

    /**
     * IR⑤ 測定範囲エンド
     * @return the ir5sokuteihaniend
     */
    public BigDecimal getIr5sokuteihaniend() {
        return ir5sokuteihaniend;
    }

    /**
     * IR⑤ 測定範囲エンド
     * @param ir5sokuteihaniend the ir5sokuteihaniend to set
     */
    public void setIr5sokuteihaniend(BigDecimal ir5sokuteihaniend) {
        this.ir5sokuteihaniend = ir5sokuteihaniend;
    }
    
    /**
     * IR⑤ 測定範囲エンド 単位
     * @return the ir5sokuteihaniendtani
     */
    public String getIr5sokuteihaniendtani() {
        return ir5sokuteihaniendtani;
    }

    /**
     * IR⑤ 測定範囲エンド 単位
     * @param ir5sokuteihaniendtani the ir5sokuteihaniendtani to set
     */
    public void setIr5sokuteihaniendtani(String ir5sokuteihaniendtani) {
        this.ir5sokuteihaniendtani = ir5sokuteihaniendtani;
    }

    /**
     * IR⑤ 良品範囲上限(耐電圧設定条件 IR⑤ 判定値)
     * @return the irhanteiti5
     */
    public BigDecimal getIrhanteiti5() {
        return irhanteiti5;
    }

    /**
     * IR⑤ 良品範囲上限(耐電圧設定条件 IR⑤ 判定値)
     * @param irhanteiti5 the irhanteiti5 to set
     */
    public void setIrhanteiti5(BigDecimal irhanteiti5) {
        this.irhanteiti5 = irhanteiti5;
    }

    /**
     * IR⑤ 良品範囲上限 単位(耐電圧設定条件 IR⑤ 判定値 単位)
     * @return irhanteiti5tani
     */
    public String getIrhanteiti5tani() {
        return irhanteiti5tani;
    }

    /**
     * IR⑤ 良品範囲上限 単位(耐電圧設定条件 IR⑤ 判定値 単位)
     * @param irhanteiti5tani 
     */
    public void setIrhanteiti5tani(String irhanteiti5tani) {
        this.irhanteiti5tani = irhanteiti5tani;
    }

    /**
     * IR⑤ 良品範囲下限(耐電圧設定条件 IR⑤ 判定値(低))
     * @return irhanteiti5low
     */
    public BigDecimal getIrhanteiti5low() {
        return irhanteiti5low;
    }

    /**
     * IR⑤ 良品範囲下限(耐電圧設定条件 IR⑤ 判定値(低))
     * @param irhanteiti5low 
     */
    public void setIrhanteiti5low(BigDecimal irhanteiti5low) {
        this.irhanteiti5low = irhanteiti5low;
    }

    /**
     * IR⑤ 良品範囲下限 単位(耐電圧設定条件 IR⑤ 判定値 単位)
     * @return irhanteiti5tanilow
     */
    public String getIrhanteiti5tanilow() {
        return irhanteiti5tanilow;
    }

    /**
     * IR⑤ 良品範囲下限 単位(耐電圧設定条件 IR⑤ 判定値 単位)
     * @param irhanteiti5tanilow 
     */
    public void setIrhanteiti5tanilow(String irhanteiti5tanilow) {
        this.irhanteiti5tanilow = irhanteiti5tanilow;
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
     * IR⑥ 時間
     * @return the ir6jikan
     */
    public BigDecimal getIr6jikan() {
        return ir6jikan;
    }

    /**
     * IR⑥ 時間
     * @param ir6jikan the ir6jikan to set
     */
    public void setIr6jikan(BigDecimal ir6jikan) {
        this.ir6jikan = ir6jikan;
    }
    
    /**
     * IR⑥ 時間 単位
     * @return the ir6jikantani
     */
    public String getIr6jikantani() {
        return ir6jikantani;
    }

    /**
     * IR⑥ 時間 単位
     * @param ir6jikantani the ir6jikantani to set
     */
    public void setIr6jikantani(String ir6jikantani) {
        this.ir6jikantani = ir6jikantani;
    }
    
    /**
     * IR⑥ 電流中心値スタート
     * @return the ir6denryustart
     */
    public BigDecimal getIr6denryustart() {
        return ir6denryustart;
    }

    /**
     * IR⑥ 電流中心値スタート
     * @param ir6denryustart the ir6denryustart to set
     */
    public void setIr6denryustart(BigDecimal ir6denryustart) {
        this.ir6denryustart = ir6denryustart;
    }
    
    /**
     * IR⑥ 電流中心値スタート 単位
     * @return the ir6denryustarttani
     */
    public String getIr6denryustarttani() {
        return ir6denryustarttani;
    }

    /**
     * IR⑥ 電流中心値スタート 単位
     * @param ir6denryustarttani the ir6denryustarttani to set
     */
    public void setIr6denryustarttani(String ir6denryustarttani) {
        this.ir6denryustarttani = ir6denryustarttani;
    }

    /**
     * IR⑥ 電流中心値エンド
     * @return the ir6denryuend
     */
    public BigDecimal getIr6denryuend() {
        return ir6denryuend;
    }

    /**
     * IR⑥ 電流中心値エンド
     * @param ir6denryuend the ir6denryuend to set
     */
    public void setIr6denryuend(BigDecimal ir6denryuend) {
        this.ir6denryuend = ir6denryuend;
    }
    
    /**
     * IR⑥ 電流中心値エンド 単位
     * @return the ir6denryuendtani
     */
    public String getIr6denryuendtani() {
        return ir6denryuendtani;
    }

    /**
     * IR⑥ 電流中心値エンド 単位
     * @param ir6denryuendtani the ir6denryuendtani to set
     */
    public void setIr6denryuendtani(String ir6denryuendtani) {
        this.ir6denryuendtani = ir6denryuendtani;
    }

    /**
     * IR⑥ 測定範囲スタート
     * @return the ir6sokuteihanistart
     */
    public BigDecimal getIr6sokuteihanistart() {
        return ir6sokuteihanistart;
    }

    /**
     * IR⑥ 測定範囲スタート
     * @param ir6sokuteihanistart the ir6sokuteihanistart to set
     */
    public void setIr6sokuteihanistart(BigDecimal ir6sokuteihanistart) {
        this.ir6sokuteihanistart = ir6sokuteihanistart;
    }
    
    /**
     * IR⑥ 測定範囲スタート 単位
     * @return the ir6sokuteihanistarttani
     */
    public String getIr6sokuteihanistarttani() {
        return ir6sokuteihanistarttani;
    }

    /**
     * IR⑥ 測定範囲スタート 単位
     * @param ir6sokuteihanistarttani the ir6sokuteihanistarttani to set
     */
    public void setIr6sokuteihanistarttani(String ir6sokuteihanistarttani) {
        this.ir6sokuteihanistarttani = ir6sokuteihanistarttani;
    }

    /**
     * IR⑥ 測定範囲エンド
     * @return the ir6sokuteihaniend
     */
    public BigDecimal getIr6sokuteihaniend() {
        return ir6sokuteihaniend;
    }

    /**
     * IR⑥ 測定範囲エンド
     * @param ir6sokuteihaniend the ir6sokuteihaniend to set
     */
    public void setIr6sokuteihaniend(BigDecimal ir6sokuteihaniend) {
        this.ir6sokuteihaniend = ir6sokuteihaniend;
    }
    
    /**
     * IR⑥ 測定範囲エンド 単位
     * @return the ir6sokuteihaniendtani
     */
    public String getIr6sokuteihaniendtani() {
        return ir6sokuteihaniendtani;
    }

    /**
     * IR⑥ 測定範囲エンド 単位
     * @param ir6sokuteihaniendtani the ir6sokuteihaniendtani to set
     */
    public void setIr6sokuteihaniendtani(String ir6sokuteihaniendtani) {
        this.ir6sokuteihaniendtani = ir6sokuteihaniendtani;
    }

    /**
     * IR⑥ 良品範囲上限(耐電圧設定条件 IR⑥ 判定値)
     * @return the irhanteiti6
     */
    public BigDecimal getIrhanteiti6() {
        return irhanteiti6;
    }

    /**
     * IR⑥ 良品範囲上限(耐電圧設定条件 IR⑥ 判定値)
     * @param irhanteiti6 the irhanteiti6 to set
     */
    public void setIrhanteiti6(BigDecimal irhanteiti6) {
        this.irhanteiti6 = irhanteiti6;
    }

    /**
     * IR⑥ 良品範囲上限 単位(耐電圧設定条件 IR⑥ 判定値 単位)
     * @return irhanteiti6tani
     */
    public String getIrhanteiti6tani() {
        return irhanteiti6tani;
    }

    /**
     * IR⑥ 良品範囲上限 単位(耐電圧設定条件 IR⑥ 判定値 単位)
     * @param irhanteiti6tani 
     */
    public void setIrhanteiti6tani(String irhanteiti6tani) {
        this.irhanteiti6tani = irhanteiti6tani;
    }

    /**
     * IR⑥ 良品範囲下限(耐電圧設定条件 IR⑥ 判定値(低))
     * @return irhanteiti6low
     */
    public BigDecimal getIrhanteiti6low() {
        return irhanteiti6low;
    }

    /**
     * IR⑥ 良品範囲下限(耐電圧設定条件 IR⑥ 判定値(低))
     * @param irhanteiti6low 
     */
    public void setIrhanteiti6low(BigDecimal irhanteiti6low) {
        this.irhanteiti6low = irhanteiti6low;
    }

    /**
     * IR⑥ 良品範囲下限 単位(耐電圧設定条件 IR⑥ 判定値 単位)
     * @return irhanteiti6tanilow
     */
    public String getIrhanteiti6tanilow() {
        return irhanteiti6tanilow;
    }

    /**
     * IR⑥ 良品範囲下限 単位(耐電圧設定条件 IR⑥ 判定値 単位)
     * @param irhanteiti6tanilow 
     */
    public void setIrhanteiti6tanilow(String irhanteiti6tanilow) {
        this.irhanteiti6tanilow = irhanteiti6tanilow;
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
     * IR⑦ 時間
     * @return the ir7jikan
     */
    public BigDecimal getIr7jikan() {
        return ir7jikan;
    }

    /**
     * IR⑦ 時間
     * @param ir7jikan the ir7jikan to set
     */
    public void setIr7jikan(BigDecimal ir7jikan) {
        this.ir7jikan = ir7jikan;
    }
    
    /**
     * IR⑦ 時間 単位
     * @return the ir7jikantani
     */
    public String getIr7jikantani() {
        return ir7jikantani;
    }

    /**
     * IR⑦ 時間 単位
     * @param ir7jikantani the ir7jikantani to set
     */
    public void setIr7jikantani(String ir7jikantani) {
        this.ir7jikantani = ir7jikantani;
    }
    
    /**
     * IR⑦ 電流中心値スタート
     * @return the ir7denryustart
     */
    public BigDecimal getIr7denryustart() {
        return ir7denryustart;
    }

    /**
     * IR⑦ 電流中心値スタート
     * @param ir7denryustart the ir7denryustart to set
     */
    public void setIr7denryustart(BigDecimal ir7denryustart) {
        this.ir7denryustart = ir7denryustart;
    }
    
    /**
     * IR⑦ 電流中心値スタート 単位
     * @return the ir7denryustarttani
     */
    public String getIr7denryustarttani() {
        return ir7denryustarttani;
    }

    /**
     * IR⑦ 電流中心値スタート 単位
     * @param ir7denryustarttani the ir7denryustarttani to set
     */
    public void setIr7denryustarttani(String ir7denryustarttani) {
        this.ir7denryustarttani = ir7denryustarttani;
    }

    /**
     * IR⑦ 電流中心値エンド
     * @return the ir7denryuend
     */
    public BigDecimal getIr7denryuend() {
        return ir7denryuend;
    }

    /**
     * IR⑦ 電流中心値エンド
     * @param ir7denryuend the ir7denryuend to set
     */
    public void setIr7denryuend(BigDecimal ir7denryuend) {
        this.ir7denryuend = ir7denryuend;
    }
    
    /**
     * IR⑦ 電流中心値エンド 単位
     * @return the ir7denryuendtani
     */
    public String getIr7denryuendtani() {
        return ir7denryuendtani;
    }

    /**
     * IR⑦ 電流中心値エンド 単位
     * @param ir7denryuendtani the ir7denryuendtani to set
     */
    public void setIr7denryuendtani(String ir7denryuendtani) {
        this.ir7denryuendtani = ir7denryuendtani;
    }

    /**
     * IR⑦ 測定範囲スタート
     * @return the ir7sokuteihanistart
     */
    public BigDecimal getIr7sokuteihanistart() {
        return ir7sokuteihanistart;
    }

    /**
     * IR⑦ 測定範囲スタート
     * @param ir7sokuteihanistart the ir7sokuteihanistart to set
     */
    public void setIr7sokuteihanistart(BigDecimal ir7sokuteihanistart) {
        this.ir7sokuteihanistart = ir7sokuteihanistart;
    }
    
    /**
     * IR⑦ 測定範囲スタート 単位
     * @return the ir7sokuteihanistarttani
     */
    public String getIr7sokuteihanistarttani() {
        return ir7sokuteihanistarttani;
    }

    /**
     * IR⑦ 測定範囲スタート 単位
     * @param ir7sokuteihanistarttani the ir7sokuteihanistarttani to set
     */
    public void setIr7sokuteihanistarttani(String ir7sokuteihanistarttani) {
        this.ir7sokuteihanistarttani = ir7sokuteihanistarttani;
    }

    /**
     * IR⑦ 測定範囲エンド
     * @return the ir7sokuteihaniend
     */
    public BigDecimal getIr7sokuteihaniend() {
        return ir7sokuteihaniend;
    }

    /**
     * IR⑦ 測定範囲エンド
     * @param ir7sokuteihaniend the ir7sokuteihaniend to set
     */
    public void setIr7sokuteihaniend(BigDecimal ir7sokuteihaniend) {
        this.ir7sokuteihaniend = ir7sokuteihaniend;
    }
    
    /**
     * IR⑦ 測定範囲エンド 単位
     * @return the ir7sokuteihaniendtani
     */
    public String getIr7sokuteihaniendtani() {
        return ir7sokuteihaniendtani;
    }

    /**
     * IR⑦ 測定範囲エンド 単位
     * @param ir7sokuteihaniendtani the ir7sokuteihaniendtani to set
     */
    public void setIr7sokuteihaniendtani(String ir7sokuteihaniendtani) {
        this.ir7sokuteihaniendtani = ir7sokuteihaniendtani;
    }

    /**
     * IR⑦ 良品範囲上限(耐電圧設定条件 IR⑦ 判定値)
     * @return the irhanteiti7
     */
    public BigDecimal getIrhanteiti7() {
        return irhanteiti7;
    }

    /**
     * IR⑦ 良品範囲上限(耐電圧設定条件 IR⑦ 判定値)
     * @param irhanteiti7 the irhanteiti7 to set
     */
    public void setIrhanteiti7(BigDecimal irhanteiti7) {
        this.irhanteiti7 = irhanteiti7;
    }

    /**
     * IR⑦ 良品範囲上限 単位(耐電圧設定条件 IR⑦ 判定値 単位)
     * @return irhanteiti7tani
     */
    public String getIrhanteiti7tani() {
        return irhanteiti7tani;
    }

    /**
     * IR⑦ 良品範囲上限 単位(耐電圧設定条件 IR⑦ 判定値 単位)
     * @param irhanteiti7tani 
     */
    public void setIrhanteiti7tani(String irhanteiti7tani) {
        this.irhanteiti7tani = irhanteiti7tani;
    }

    /**
     * IR⑦ 良品範囲下限(耐電圧設定条件 IR⑦ 判定値(低))
     * @return irhanteiti7low
     */
    public BigDecimal getIrhanteiti7low() {
        return irhanteiti7low;
    }

    /**
     * IR⑦ 良品範囲下限(耐電圧設定条件 IR⑦ 判定値(低))
     * @param irhanteiti7low 
     */
    public void setIrhanteiti7low(BigDecimal irhanteiti7low) {
        this.irhanteiti7low = irhanteiti7low;
    }

    /**
     * IR⑦ 良品範囲下限 単位(耐電圧設定条件 IR⑦ 判定値 単位)
     * @return irhanteiti7tanilow
     */
    public String getIrhanteiti7tanilow() {
        return irhanteiti7tanilow;
    }

    /**
     * IR⑦ 良品範囲下限 単位(耐電圧設定条件 IR⑦ 判定値 単位)
     * @param irhanteiti7tanilow 
     */
    public void setIrhanteiti7tanilow(String irhanteiti7tanilow) {
        this.irhanteiti7tanilow = irhanteiti7tanilow;
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
     * IR⑧ 時間
     * @return the ir8jikan
     */
    public BigDecimal getIr8jikan() {
        return ir8jikan;
    }

    /**
     * IR⑧ 時間
     * @param ir8jikan the ir8jikan to set
     */
    public void setIr8jikan(BigDecimal ir8jikan) {
        this.ir8jikan = ir8jikan;
    }
    
    /**
     * IR⑧ 時間 単位
     * @return the ir8jikantani
     */
    public String getIr8jikantani() {
        return ir8jikantani;
    }

    /**
     * IR⑧ 時間 単位
     * @param ir8jikantani the ir8jikantani to set
     */
    public void setIr8jikantani(String ir8jikantani) {
        this.ir8jikantani = ir8jikantani;
    }
    
    /**
     * IR⑧ 電流中心値スタート
     * @return the ir8denryustart
     */
    public BigDecimal getIr8denryustart() {
        return ir8denryustart;
    }

    /**
     * IR⑧ 電流中心値スタート
     * @param ir8denryustart the ir8denryustart to set
     */
    public void setIr8denryustart(BigDecimal ir8denryustart) {
        this.ir8denryustart = ir8denryustart;
    }
    
    /**
     * IR⑧ 電流中心値スタート 単位
     * @return the ir8denryustarttani
     */
    public String getIr8denryustarttani() {
        return ir8denryustarttani;
    }

    /**
     * IR⑧ 電流中心値スタート 単位
     * @param ir8denryustarttani the ir8denryustarttani to set
     */
    public void setIr8denryustarttani(String ir8denryustarttani) {
        this.ir8denryustarttani = ir8denryustarttani;
    }

    /**
     * IR⑧ 電流中心値エンド
     * @return the ir8denryuend
     */
    public BigDecimal getIr8denryuend() {
        return ir8denryuend;
    }

    /**
     * IR⑧ 電流中心値エンド
     * @param ir8denryuend the ir8denryuend to set
     */
    public void setIr8denryuend(BigDecimal ir8denryuend) {
        this.ir8denryuend = ir8denryuend;
    }
    
    /**
     * IR⑧ 電流中心値エンド 単位
     * @return the ir8denryuendtani
     */
    public String getIr8denryuendtani() {
        return ir8denryuendtani;
    }

    /**
     * IR⑧ 電流中心値エンド 単位
     * @param ir8denryuendtani the ir8denryuendtani to set
     */
    public void setIr8denryuendtani(String ir8denryuendtani) {
        this.ir8denryuendtani = ir8denryuendtani;
    }

    /**
     * IR⑧ 測定範囲スタート
     * @return the ir8sokuteihanistart
     */
    public BigDecimal getIr8sokuteihanistart() {
        return ir8sokuteihanistart;
    }

    /**
     * IR⑧ 測定範囲スタート
     * @param ir8sokuteihanistart the ir8sokuteihanistart to set
     */
    public void setIr8sokuteihanistart(BigDecimal ir8sokuteihanistart) {
        this.ir8sokuteihanistart = ir8sokuteihanistart;
    }
    
    /**
     * IR⑧ 測定範囲スタート 単位
     * @return the ir8sokuteihanistarttani
     */
    public String getIr8sokuteihanistarttani() {
        return ir8sokuteihanistarttani;
    }

    /**
     * IR⑧ 測定範囲スタート 単位
     * @param ir8sokuteihanistarttani the ir8sokuteihanistarttani to set
     */
    public void setIr8sokuteihanistarttani(String ir8sokuteihanistarttani) {
        this.ir8sokuteihanistarttani = ir8sokuteihanistarttani;
    }

    /**
     * IR⑧ 測定範囲エンド
     * @return the ir8sokuteihaniend
     */
    public BigDecimal getIr8sokuteihaniend() {
        return ir8sokuteihaniend;
    }

    /**
     * IR⑧ 測定範囲エンド
     * @param ir8sokuteihaniend the ir8sokuteihaniend to set
     */
    public void setIr8sokuteihaniend(BigDecimal ir8sokuteihaniend) {
        this.ir8sokuteihaniend = ir8sokuteihaniend;
    }
    
    /**
     * IR⑧ 測定範囲エンド 単位
     * @return the ir8sokuteihaniendtani
     */
    public String getIr8sokuteihaniendtani() {
        return ir8sokuteihaniendtani;
    }

    /**
     * IR⑧ 測定範囲エンド 単位
     * @param ir8sokuteihaniendtani the ir8sokuteihaniendtani to set
     */
    public void setIr8sokuteihaniendtani(String ir8sokuteihaniendtani) {
        this.ir8sokuteihaniendtani = ir8sokuteihaniendtani;
    }

    /**
     * IR⑧ 良品範囲上限(耐電圧設定条件 IR⑧ 判定値)
     * @return the irhanteiti8
     */
    public BigDecimal getIrhanteiti8() {
        return irhanteiti8;
    }

    /**
     * IR⑧ 良品範囲上限(耐電圧設定条件 IR⑧ 判定値)
     * @param irhanteiti8 the irhanteiti8 to set
     */
    public void setIrhanteiti8(BigDecimal irhanteiti8) {
        this.irhanteiti8 = irhanteiti8;
    }

    /**
     * IR⑧ 良品範囲上限 単位(耐電圧設定条件 IR⑧ 判定値 単位)
     * @return irhanteiti8tani
     */
    public String getIrhanteiti8tani() {
        return irhanteiti8tani;
    }

    /**
     * IR⑧ 良品範囲上限 単位(耐電圧設定条件 IR⑧ 判定値 単位)
     * @param irhanteiti8tani 
     */
    public void setIrhanteiti8tani(String irhanteiti8tani) {
        this.irhanteiti8tani = irhanteiti8tani;
    }

    /**
     * IR⑧ 良品範囲下限(耐電圧設定条件 IR⑧ 判定値(低))
     * @return irhanteiti8low
     */
    public BigDecimal getIrhanteiti8low() {
        return irhanteiti8low;
    }

    /**
     * IR⑧ 良品範囲下限(耐電圧設定条件 IR⑧ 判定値(低))
     * @param irhanteiti8low 
     */
    public void setIrhanteiti8low(BigDecimal irhanteiti8low) {
        this.irhanteiti8low = irhanteiti8low;
    }

    /**
     * IR⑧ 良品範囲下限 単位(耐電圧設定条件 IR⑧ 判定値 単位)
     * @return irhanteiti8tanilow
     */
    public String getIrhanteiti8tanilow() {
        return irhanteiti8tanilow;
    }

    /**
     * IR⑧ 良品範囲下限 単位(耐電圧設定条件 IR⑧ 判定値 単位)
     * @param irhanteiti8tanilow 
     */
    public void setIrhanteiti8tanilow(String irhanteiti8tanilow) {
        this.irhanteiti8tanilow = irhanteiti8tanilow;
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
     * BIN1 抜き取り結果(子数)
     * @return the bin1nukitorikekka
     */
    public Integer getBin1nukitorikekka() {
        return bin1nukitorikekka;
    }

    /**
     * BIN1 抜き取り結果(子数)
     * @param bin1nukitorikekka the bin1nukitorikekka to set
     */
    public void setBin1nukitorikekka(Integer bin1nukitorikekka) {
        this.bin1nukitorikekka = bin1nukitorikekka;
    }

    /**
     * BIN1 抜き取り結果(母数)
     * @return the bin1nukitorikekkabosuu
     */
    public Integer getBin1nukitorikekkabosuu() {
        return bin1nukitorikekkabosuu;
    }

    /**
     * BIN1 抜き取り結果(母数)
     * @param bin1nukitorikekkabosuu the bin1nukitorikekkabosuu to set
     */
    public void setBin1nukitorikekkabosuu(Integer bin1nukitorikekkabosuu) {
        this.bin1nukitorikekkabosuu = bin1nukitorikekkabosuu;
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
     * BIN2 抜き取り結果(子数)
     * @return the bin2nukitorikekka
     */
    public Integer getBin2nukitorikekka() {
        return bin2nukitorikekka;
    }

    /**
     * BIN2 抜き取り結果(子数)
     * @param bin2nukitorikekka the bin2nukitorikekka to set
     */
    public void setBin2nukitorikekka(Integer bin2nukitorikekka) {
        this.bin2nukitorikekka = bin2nukitorikekka;
    }

    /**
     * BIN2 抜き取り結果(母数)
     * @return the bin2nukitorikekkabosuu
     */
    public Integer getBin2nukitorikekkabosuu() {
        return bin2nukitorikekkabosuu;
    }

    /**
     * BIN2 抜き取り結果(母数)
     * @param bin2nukitorikekkabosuu the bin2nukitorikekkabosuu to set
     */
    public void setBin2nukitorikekkabosuu(Integer bin2nukitorikekkabosuu) {
        this.bin2nukitorikekkabosuu = bin2nukitorikekkabosuu;
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
     * BIN3 抜き取り結果(子数)
     * @return the bin3nukitorikekka
     */
    public Integer getBin3nukitorikekka() {
        return bin3nukitorikekka;
    }

    /**
     * BIN3 抜き取り結果(子数)
     * @param bin3nukitorikekka the bin3nukitorikekka to set
     */
    public void setBin3nukitorikekka(Integer bin3nukitorikekka) {
        this.bin3nukitorikekka = bin3nukitorikekka;
    }

    /**
     * BIN3 抜き取り結果(母数)
     * @return the bin3nukitorikekkabosuu
     */
    public Integer getBin3nukitorikekkabosuu() {
        return bin3nukitorikekkabosuu;
    }

    /**
     * BIN3 抜き取り結果(母数)
     * @param bin3nukitorikekkabosuu the bin3nukitorikekkabosuu to set
     */
    public void setBin3nukitorikekkabosuu(Integer bin3nukitorikekkabosuu) {
        this.bin3nukitorikekkabosuu = bin3nukitorikekkabosuu;
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
     * BIN4 抜き取り結果(子数)
     * @return the bin4nukitorikekka
     */
    public Integer getBin4nukitorikekka() {
        return bin4nukitorikekka;
    }

    /**
     * BIN4 抜き取り結果(子数)
     * @param bin4nukitorikekka the bin4nukitorikekka to set
     */
    public void setBin4nukitorikekka(Integer bin4nukitorikekka) {
        this.bin4nukitorikekka = bin4nukitorikekka;
    }

    /**
     * BIN4 抜き取り結果(母数)
     * @return the bin4nukitorikekkabosuu
     */
    public Integer getBin4nukitorikekkabosuu() {
        return bin4nukitorikekkabosuu;
    }

    /**
     * BIN4 抜き取り結果(母数)
     * @param bin4nukitorikekkabosuu the bin4nukitorikekkabosuu to set
     */
    public void setBin4nukitorikekkabosuu(Integer bin4nukitorikekkabosuu) {
        this.bin4nukitorikekkabosuu = bin4nukitorikekkabosuu;
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
     * BIN5 抜き取り結果(子数)
     * @return the bin5nukitorikekka
     */
    public Integer getBin5nukitorikekka() {
        return bin5nukitorikekka;
    }

    /**
     * BIN5 抜き取り結果(子数)
     * @param bin5nukitorikekka the bin5nukitorikekka to set
     */
    public void setBin5nukitorikekka(Integer bin5nukitorikekka) {
        this.bin5nukitorikekka = bin5nukitorikekka;
    }

    /**
     * BIN5 抜き取り結果(母数)
     * @return the bin5nukitorikekkabosuu
     */
    public Integer getBin5nukitorikekkabosuu() {
        return bin5nukitorikekkabosuu;
    }

    /**
     * BIN5 抜き取り結果(母数)
     * @param bin5nukitorikekkabosuu the bin5nukitorikekkabosuu to set
     */
    public void setBin5nukitorikekkabosuu(Integer bin5nukitorikekkabosuu) {
        this.bin5nukitorikekkabosuu = bin5nukitorikekkabosuu;
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
     * BIN6 抜き取り結果(子数)
     * @return the bin6nukitorikekka
     */
    public Integer getBin6nukitorikekka() {
        return bin6nukitorikekka;
    }

    /**
     * BIN6 抜き取り結果(子数)
     * @param bin6nukitorikekka the bin6nukitorikekka to set
     */
    public void setBin6nukitorikekka(Integer bin6nukitorikekka) {
        this.bin6nukitorikekka = bin6nukitorikekka;
    }

    /**
     * BIN6 抜き取り結果(母数)
     * @return the bin6nukitorikekkabosuu
     */
    public Integer getBin6nukitorikekkabosuu() {
        return bin6nukitorikekkabosuu;
    }

    /**
     * BIN6 抜き取り結果(母数)
     * @param bin6nukitorikekkabosuu the bin6nukitorikekkabosuu to set
     */
    public void setBin6nukitorikekkabosuu(Integer bin6nukitorikekkabosuu) {
        this.bin6nukitorikekkabosuu = bin6nukitorikekkabosuu;
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
     * BIN7 抜き取り結果(子数)
     * @return the bin7nukitorikekka
     */
    public Integer getBin7nukitorikekka() {
        return bin7nukitorikekka;
    }

    /**
     * BIN7 抜き取り結果(子数)
     * @param bin7nukitorikekka the bin7nukitorikekka to set
     */
    public void setBin7nukitorikekka(Integer bin7nukitorikekka) {
        this.bin7nukitorikekka = bin7nukitorikekka;
    }

    /**
     * BIN7 抜き取り結果(母数)
     * @return the bin7nukitorikekkabosuu
     */
    public Integer getBin7nukitorikekkabosuu() {
        return bin7nukitorikekkabosuu;
    }

    /**
     * BIN7 抜き取り結果(母数)
     * @param bin7nukitorikekkabosuu the bin7nukitorikekkabosuu to set
     */
    public void setBin7nukitorikekkabosuu(Integer bin7nukitorikekkabosuu) {
        this.bin7nukitorikekkabosuu = bin7nukitorikekkabosuu;
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
     * BIN8 抜き取り結果(子数)
     * @return the bin8nukitorikekka
     */
    public Integer getBin8nukitorikekka() {
        return bin8nukitorikekka;
    }

    /**
     * BIN8 抜き取り結果(子数)
     * @param bin8nukitorikekka the bin8nukitorikekka to set
     */
    public void setBin8nukitorikekka(Integer bin8nukitorikekka) {
        this.bin8nukitorikekka = bin8nukitorikekka;
    }

    /**
     * BIN8 抜き取り結果(母数)
     * @return the bin8nukitorikekkabosuu
     */
    public Integer getBin8nukitorikekkabosuu() {
        return bin8nukitorikekkabosuu;
    }

    /**
     * BIN8 抜き取り結果(母数)
     * @param bin8nukitorikekkabosuu the bin8nukitorikekkabosuu to set
     */
    public void setBin8nukitorikekkabosuu(Integer bin8nukitorikekkabosuu) {
        this.bin8nukitorikekkabosuu = bin8nukitorikekkabosuu;
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
}
