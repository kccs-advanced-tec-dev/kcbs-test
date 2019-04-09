/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/04/08<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCCS D.Yanagida<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 印刷積層・RHAPS履歴検索画面のモデルクラスです。
 *
 * @author KCCS D.Yanagida
 * @since 2019/04/08
 */
public class GXHDO201B006Model {
    /** ﾛｯﾄNo. */
    private String lotno = "";
    /** KCPNO */
    private String kcpno = "";
    /** 開始日時 */
    private Timestamp kaisinichiji = null;
    /** 終了日時 */
    private Timestamp syuryonichiji = null;
    /** 端子ﾃｰﾌﾟ種類 */
    private String ttapesyurui = "";
    /** 端子ﾃｰﾌﾟﾛｯﾄNo */
    private String ttapelotno = "";
    /** 端子ﾃｰﾌﾟｽﾘｯﾌﾟ記号 */
    private String ttapeslipkigo = "";
    /** 端子ﾃｰﾌﾟﾛｰﾙNo 1本目 */
    private String ttaperollno1 = "";
    /** 端子ﾃｰﾌﾟﾛｰﾙNo 2本目 */
    private String ttaperollno2 = "";
    /** 端子ﾃｰﾌﾟﾛｰﾙNo 3本目 */
    private String ttaperollno3 = "";
    /** 端子ﾃｰﾌﾟﾛｰﾙNo 4本目 */
    private String ttaperollno4 = "";
    /** 端子ﾃｰﾌﾟﾛｰﾙNo 5本目 */
    private String ttaperollno5 = "";
    /** 端子原料記号 */
    private String tgenryokigo = "";
    /** 下端子仕様 */
    private String stsiyo = "";
    /** 電極積層仕様 */
    private Long esekisosiyo = null;
    /** 電極ﾃｰﾌﾟ種類 */
    private String etapesyurui = "";
    /** 電極ﾃｰﾌﾟ原料lot */
    private String etapeglot = "";
    /** 電極ﾃｰﾌﾟﾛｯﾄ */
    private String etapelot = "";
    /** 電極ﾃｰﾌﾟｽﾘｯﾌﾟ記号 */
    private String etapeslipkigo = "";
    /** 電極ﾃｰﾌﾟﾛｰﾙNo 1本目 */
    private String etaperollno1 = "";
    /** 電極ﾃｰﾌﾟﾛｰﾙNo 2本目 */
    private String etaperollno2 = "";
    /** 電極ﾃｰﾌﾟﾛｰﾙNo 3本目 */
    private String etaperollno3 = "";
    /** 電極ﾃｰﾌﾟﾛｰﾙNo 4本目 */
    private String etaperollno4 = "";
    /** 電極ﾃｰﾌﾟﾛｰﾙNo 5本目 */
    private String etaperollno5 = "";
    /** 積層ﾌﾟﾚｽ通電時間(瞬時加熱) */
    private BigDecimal sptudenjikan = null;
    /** 積層加圧力 */
    private BigDecimal skaaturyoku = null;
    /** 瞬時加熱ﾍｯﾄﾞNo */
    private String skheadno = "";
    /** SUS板使用回数 */
    private Long susskaisuu = null;
    /** 電極誘電体ﾍﾟｰｽﾄ名 */
    private String ecpastemei = "";
    /** 電極ﾍﾟｰｽﾄﾛｯﾄNo */
    private String epastelotno = "";
    /** 電極ﾍﾟｰｽﾄ粘度 */
    private BigDecimal epastenendo = null;
    /** 電極ﾍﾟｰｽﾄ温度 */
    private BigDecimal epasteondo = null;
    /** 電極製版名 */
    private String eseihanmei = "";
    /** 電極製版No */
    private String eseihanno = "";
    /** 電極製版枚数 */
    private Long eseimaisuu = null;
    /** 電極ｸﾘｱﾗﾝｽ */
    private BigDecimal eclearance = null;
    /** 電極差圧 */
    private BigDecimal esaatu = null;
    /** 電極ｽｷｰｼﾞNo */
    private String eskeegeno = "";
    /** 電極ｽｷｰｼﾞ枚数 */
    private Long eskmaisuu = null;
    /** 電極ｽｷｰｼﾞｽﾋﾟｰﾄﾞ */
    private Long eskspeed = null;
    /** 電極ｽｸﾚｯﾊﾟｸﾘｱﾗﾝｽ */
    private BigDecimal escclearance = null;
    /** 電極ｽｷｰｼﾞ下降待ち時間 */
    private BigDecimal eskkmjikan = null;
    /** 電極L/Dｽﾀｰﾄ時 */
    private BigDecimal eldstart = null;
    /** 電極製版面積 */
    private BigDecimal eseimenseki = null;
    /** 電極膜厚 */
    private BigDecimal emakuatu = null;
    /** 電極ｽﾗｲﾄﾞ量 */
    private BigDecimal eslideryo = null;
    /** 電極乾燥温度 */
    private Long ekansoondo = null;
    /** 電極乾燥時間 */
    private Long ekansojikan = null;
    /** 誘電体ﾍﾟｰｽﾄﾛｯﾄNo */
    private String cpastelotno = "";
    /** 誘電体ﾍﾟｰｽﾄ粘度 */
    private BigDecimal cpastenendo = null;
    /** 誘電体ﾍﾟｰｽﾄ温度 */
    private BigDecimal cpasteondo = null;
    /** 誘電体製版名 */
    private String cseihanmei = "";
    /** 誘電体製版No */
    private String cseihanno = "";
    /** 誘電体製版枚数 */
    private Long cseimaisuu = null;
    /** 誘電体ｸﾘｱﾗﾝｽ */
    private BigDecimal cclearance = null;
    /** 誘電体差圧 */
    private BigDecimal csaatu = null;
    /** 誘電体ｽｷｰｼﾞNo */
    private String cskeegeno = "";
    /** 誘電体ｽｷｰｼﾞ枚数 */
    private Long cskmaisuu = null;
    /** 誘電体ｽｸﾚｯﾊﾟｸﾘｱﾗﾝｽ */
    private BigDecimal cscclearance = null;
    /** 誘電体ｽｷｰｼﾞ下降待ち時間 */
    private BigDecimal cskkmjikan = null;
    /** 誘電体ｼﾌﾄ印刷 */
    private Long cshiftinsatu = null;
    /** 誘電体L/Dｽﾀｰﾄ時 */
    private BigDecimal cldstart = null;
    /** 誘電体製版面積 */
    private BigDecimal cseimenseki = null;
    /** 誘電体ｽﾗｲﾄﾞ量 */
    private BigDecimal cslideryo = null;
    /** 誘電体乾燥温度 */
    private Long ckansoondo = null;
    /** 誘電体乾燥時間 */
    private Long ckansojikan = null;
    /** 誘電体膜厚 */
    private BigDecimal cmakuatu = null;
    /** 合わせ印刷ｻｲﾄﾞRZ1 */
    private BigDecimal ainsatusrz1 = null;
    /** 合わせ印刷ｻｲﾄﾞRZ2 */
    private BigDecimal ainsatusrz2 = null;
    /** 合わせ印刷ｻｲﾄﾞRZ3 */
    private BigDecimal ainsatusrz3 = null;
    /** 合わせ印刷ｻｲﾄﾞRZ4 */
    private BigDecimal ainsatusrz4 = null;
    /** 合わせ印刷ｻｲﾄﾞRZ5 */
    private BigDecimal ainsatusrz5 = null;
    /** 合わせ印刷ｻｲﾄﾞRZAVE */
    private BigDecimal ainsatusrzave = null;
    /** 上端子仕様 */
    private BigDecimal utsiyo = null;
    /** 上端子通電時間 */
    private BigDecimal uttudenjikan = null;
    /** 上端子加圧力 */
    private BigDecimal utkaaturyoku = null;
    /** 積層体厚み補正 */
    private BigDecimal stahosei = null;
    /** ﾀｰｹﾞｯﾄ印刷ｸﾘｱﾗﾝｽ */
    private BigDecimal ticlearance = null;
    /** ﾀｰｹﾞｯﾄ印刷差圧 */
    private BigDecimal tisaatu = null;
    /** ﾀｰｹﾞｯﾄ印刷ｽｷｰｼﾞｽﾋﾟｰﾄﾞ */
    private Long tiskspeed = null;
    /** 初層左上X1 */
    private Long fsthux1 = null;
    /** 初層左上X2 */
    private Long fsthux2 = null;
    /** 初層左上Y1 */
    private Long fsthuy1 = null;
    /** 初層左上Y2 */
    private Long fsthuy2 = null;
    /** 初層左下X1 */
    private Long fsthsx1 = null;
    /** 初層左下X2 */
    private Long fsthsx2 = null;
    /** 初層左下Y1 */
    private Long fsthsy1 = null;
    /** 初層左下Y2 */
    private Long fsthsy2 = null;
    /** 初層中央X1 */
    private Long fstcx1 = null;
    /** 初層中央X2 */
    private Long fstcx2 = null;
    /** 初層中央Y1 */
    private Long fstcy1 = null;
    /** 初層中央Y2 */
    private Long fstcy2 = null;
    /** 初層右上X1 */
    private Long fstmux1 = null;
    /** 初層右上X2 */
    private Long fstmux2 = null;
    /** 初層右上Y1 */
    private Long fstmuy1 = null;
    /** 初層右上Y2 */
    private Long fstmuy2 = null;
    /** 初層右下X1 */
    private Long fstmsx1 = null;
    /** 初層右下X2 */
    private Long fstmsx2 = null;
    /** 初層右下Y1 */
    private Long fstmsy1 = null;
    /** 初層右下Y2 */
    private Long fstmsy2 = null;
    /** 最終層左上X1 */
    private Long lsthux1 = null;
    /** 最終層左上X2 */
    private Long lsthux2 = null;
    /** 最終層左上Y1 */
    private Long lsthuy1 = null;
    /** 最終層左上Y2 */
    private Long lsthuy2 = null;
    /** 最終層左下X1 */
    private Long lsthsx1 = null;
    /** 最終層左下X2 */
    private Long lsthsx2 = null;
    /** 最終層左下Y1 */
    private Long lsthsy1 = null;
    /** 最終層左下Y2 */
    private Long lsthsy2 = null;
    /** 最終層中央X1 */
    private Long lstcx1 = null;
    /** 最終層中央X2 */
    private Long lstcx2 = null;
    /** 最終層中央Y1 */
    private Long lstcy1 = null;
    /** 最終層中央Y2 */
    private Long lstcy2 = null;
    /** 最終層右上X1 */
    private Long lstmux1 = null;
    /** 最終層右上X2 */
    private Long lstmux2 = null;
    /** 最終層右上Y1 */
    private Long lstmuy1 = null;
    /** 最終層右上Y2 */
    private Long lstmuy2 = null;
    /** 最終層右下X1 */
    private Long lstmsx1 = null;
    /** 最終層右下X2 */
    private Long lstmsx2 = null;
    /** 最終層右下Y1 */
    private Long lstmsy1 = null;
    /** 最終層右下Y2 */
    private Long lstmsy2 = null;
    /** 備考1 */
    private String biko1 = "";
    /** 備考2 */
    private String biko2 = "";
    /** 号機 */
    private String goki = "";
    /** 特別端子枚数上 */
    private Long ttansisuuu = null;
    /** 特別端子枚数下 */
    private Long ttansisuus = null;
    /** 瞬時加熱時間 */
    private BigDecimal shunkankanetsujikan = null;
    /** PETﾌｨﾙﾑ種類 */
    private String petfilmsyurui = "";
    /** 加圧力 */
    private Long kaaturyoku = null;
    /** 外観確認 */
    private String gaikankakunin = "";
    /** 積層上昇速切替位置 */
    private BigDecimal sekijsskirikaeichi = null;
    /** 積層下降速切替位置 */
    private BigDecimal sekikkskirikaeichi = null;
    /** 加圧時間 */
    private BigDecimal kaatujikan = null;
    /** ﾃｰﾌﾟ搬送送りﾋﾟｯﾁ */
    private Long tapehansoupitch = null;
    /** ﾃｰﾌﾟ搬送目視確認 */
    private String tapehansoukakunin = "";
    /** 電極膜厚設定 */
    private BigDecimal emakuatsusettei = null;
    /** 電極熱風風量 */
    private Long eneppufuryou = null;
    /** 電極膜厚AVE */
    private BigDecimal emakuatsuave = null;
    /** 電極膜厚MAX */
    private BigDecimal emakuatsumax = null;
    /** 電極膜厚MIN */
    private BigDecimal emakuatsumin = null;
    /** にじみ量測定(ﾊﾟﾀｰﾝ間距離) */
    private Long nijimisokuteiptn = null;
    /** 印刷ｻﾝﾌﾟﾙ外観確認 */
    private String prnsamplegaikan = "";
    /** 印刷位置余白長さ */
    private String prnichiyohakunagasa = "";
    /** 誘電体ﾃｰﾌﾞﾙｸﾘｱﾗﾝｽ */
    private BigDecimal ctableclearance = null;
    /** 誘電体膜厚設定 */
    private BigDecimal cmakuatsusettei = null;
    /** 誘電体ｽｷｰｼﾞｽﾋﾟｰﾄﾞ */
    private Long cskspeed = null;
    /** 誘電体熱風風量 */
    private Long cneppufuryou = null;
    /** 被り量測定 */
    private String kaburiryou = "";
    /** 積層中外観 */
    private String sgaikan = "";
    /** にじみ量測定(積層後) */
    private Long nijimisokuteisekisougo = null;
    /** 積層品外観 */
    private String sekisouhingaikan = "";
    /** 積層ｽﾞﾚﾁｪｯｸ */
    private String sekisouzure = "";
    /** 上端子上昇速切替位置 */
    private BigDecimal uwajsskirikaeichi = null;
    /** 下端子下降速切替位置 */
    private BigDecimal shitakkskirikaeichi = null;
    /** ﾀｰｹﾞｯﾄｲﾝｸ種類 */
    private String tinksyuryui = "";
    /** ﾀｰｹﾞｯﾄｲﾝｸLOT */
    private String tinklot = "";
    /** ﾀｰｹﾞｯﾄ印刷外観 */
    private Long tgaikan = null;
    /** 印刷積層開始担当者 */
    private String starttantou = "";
    /** 印刷積層終了担当者 */
    private String endtantou = "";
    /** ﾀｰｹﾞｯﾄ印刷終了日 */
    private String tendday = "";
    /** ﾀｰｹﾞｯﾄ印刷担当者 */
    private String tendtantou = "";
    /** 処理ｾｯﾄ数 */
    private Long syorisetsuu = null;
    /** 良品ｾｯﾄ数 */
    private Long ryouhinsetsuu = null;
    /** ﾍｯﾄﾞ交換者 */
    private String headkoukantantou = "";
    /** 積層条件者 */
    private String sekisoujoukentantou = "";
    /** E製版ｾｯﾄ者 */
    private String eseihansettantou = "";
    /** C製版ｾｯﾄ者 */
    private String cseihansettantou = "";
    /** 段差測定者 */
    private String dansasokuteitantou = "";
    /** 電極膜厚1 */
    private BigDecimal emakuatsu1 = null;
    /** 電極膜厚2 */
    private BigDecimal emakuatsu2 = null;
    /** 電極膜厚3 */
    private BigDecimal emakuatsu3 = null;
    /** 電極膜厚4 */
    private BigDecimal emakuatsu4 = null;
    /** 電極膜厚5 */
    private BigDecimal emakuatsu5 = null;
    /** 電極膜厚6 */
    private BigDecimal emakuatsu6 = null;
    /** 電極膜厚7 */
    private BigDecimal emakuatsu7 = null;
    /** 電極膜厚8 */
    private BigDecimal emakuatsu8 = null;
    /** 電極膜厚9 */
    private BigDecimal emakuatsu9 = null;
    /** ﾊﾟﾀｰﾝ間距離1 */
    private Long ptndist1 = null;
    /** ﾊﾟﾀｰﾝ間距離2 */
    private Long ptndist2 = null;
    /** ﾊﾟﾀｰﾝ間距離3 */
    private Long ptndist3 = null;
    /** ﾊﾟﾀｰﾝ間距離4 */
    private Long ptndist4 = null;
    /** ﾊﾟﾀｰﾝ間距離5 */
    private Long ptndist5 = null;
    /** 合わせ(RZ)1 */
    private BigDecimal awaserz1 = null;
    /** 合わせ(RZ)2 */
    private BigDecimal awaserz2 = null;
    /** 合わせ(RZ)3 */
    private BigDecimal awaserz3 = null;
    /** 合わせ(RZ)4 */
    private BigDecimal awaserz4 = null;
    /** 合わせ(RZ)5 */
    private BigDecimal awaserz5 = null;
    /** 合わせ(RZ)6 */
    private BigDecimal awaserz6 = null;
    /** 合わせ(RZ)7 */
    private BigDecimal awaserz7 = null;
    /** 合わせ(RZ)8 */
    private BigDecimal awaserz8 = null;
    /** 合わせ(RZ)9 */
    private BigDecimal awaserz9 = null;
    /** 被り量左上X1 */
    private Long kaburihidariuex1 = null;
    /** 被り量左上X2 */
    private Long kaburihidariuex2 = null;
    /** 被り量左上Y1 */
    private Long kaburihidariuey1 = null;
    /** 被り量左上Y2 */
    private Long kaburihidariuey2 = null;
    /** 被り量左下X1 */
    private Long kaburihidarisitax1 = null;
    /** 被り量左下X2 */
    private Long kaburihidarisitax2 = null;
    /** 被り量左下Y1 */
    private Long kaburihidarisitay1 = null;
    /** 被り量左下Y2 */
    private Long kaburihidarisitay2 = null;
    /** 被り量中央X1 */
    private Long kaburihidaricenterx1 = null;
    /** 被り量中央X2 */
    private Long kaburihidaricenterx2 = null;
    /** 被り量中央Y1 */
    private Long kaburihidaricentery1 = null;
    /** 被り量中央Y2 */
    private Long kaburihidaricentery2 = null;
    /** 被り量右上X1 */
    private Long kaburimigiuex1 = null;
    /** 被り量右上X2 */
    private Long kaburimigiuex2 = null;
    /** 被り量右上Y1 */
    private Long kaburimigiuey1 = null;
    /** 被り量右上Y2 */
    private Long kaburimigiuey2 = null;
    /** 被り量右下X1 */
    private Long kaburimigisitax1 = null;
    /** 被り量右下X2 */
    private Long kaburimigisitax2 = null;
    /** 被り量右下Y1 */
    private Long kaburimigisitay1 = null;
    /** 被り量右下Y2 */
    private Long kaburimigisitay2 = null;
    
    /**
     * ﾛｯﾄNo.
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * ﾛｯﾄNo.
     * @param lotno the lotno to set
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
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
     * 開始日時
     * @return the kaisinichiji
     */
    public Timestamp getKaisinichiji() {
        return kaisinichiji;
    }

    /**
     * 開始日時
     * @param kaisinichiji the kaisinichiji to set
     */
    public void setKaisinichiji(Timestamp kaisinichiji) {
        this.kaisinichiji = kaisinichiji;
    }

    /**
     * 終了日時
     * @return the syuryonichiji
     */
    public Timestamp getSyuryonichiji() {
        return syuryonichiji;
    }

    /**
     * 終了日時
     * @param syuryonichiji the syuryonichiji to set
     */
    public void setSyuryonichiji(Timestamp syuryonichiji) {
        this.syuryonichiji = syuryonichiji;
    }

    /**
     * 端子ﾃｰﾌﾟ種類
     * @return the ttapesyurui
     */
    public String getTtapesyurui() {
        return ttapesyurui;
    }

    /**
     * 端子ﾃｰﾌﾟ種類
     * @param ttapesyurui the ttapesyurui to set
     */
    public void setTtapesyurui(String ttapesyurui) {
        this.ttapesyurui = ttapesyurui;
    }

    /**
     * 端子ﾃｰﾌﾟﾛｯﾄNo
     * @return the ttapelotno
     */
    public String getTtapelotno() {
        return ttapelotno;
    }

    /**
     * 端子ﾃｰﾌﾟﾛｯﾄNo
     * @param ttapelotno the ttapelotno to set
     */
    public void setTtapelotno(String ttapelotno) {
        this.ttapelotno = ttapelotno;
    }

    /**
     * 端子ﾃｰﾌﾟｽﾘｯﾌﾟ記号
     * @return the ttapeslipkigo
     */
    public String getTtapeslipkigo() {
        return ttapeslipkigo;
    }

    /**
     * 端子ﾃｰﾌﾟｽﾘｯﾌﾟ記号
     * @param ttapeslipkigo the ttapeslipkigo to set
     */
    public void setTtapeslipkigo(String ttapeslipkigo) {
        this.ttapeslipkigo = ttapeslipkigo;
    }

    /**
     * 端子ﾃｰﾌﾟﾛｰﾙNo 1本目
     * @return the ttaperollno1
     */
    public String getTtaperollno1() {
        return ttaperollno1;
    }

    /**
     * 端子ﾃｰﾌﾟﾛｰﾙNo 1本目
     * @param ttaperollno1 the ttaperollno1 to set
     */
    public void setTtaperollno1(String ttaperollno1) {
        this.ttaperollno1 = ttaperollno1;
    }

    /**
     * 端子ﾃｰﾌﾟﾛｰﾙNo 2本目
     * @return the ttaperollno2
     */
    public String getTtaperollno2() {
        return ttaperollno2;
    }

    /**
     * 端子ﾃｰﾌﾟﾛｰﾙNo 2本目
     * @param ttaperollno2 the ttaperollno2 to set
     */
    public void setTtaperollno2(String ttaperollno2) {
        this.ttaperollno2 = ttaperollno2;
    }

    /**
     * 端子ﾃｰﾌﾟﾛｰﾙNo 3本目
     * @return the ttaperollno3
     */
    public String getTtaperollno3() {
        return ttaperollno3;
    }

    /**
     * 端子ﾃｰﾌﾟﾛｰﾙNo 3本目
     * @param ttaperollno3 the ttaperollno3 to set
     */
    public void setTtaperollno3(String ttaperollno3) {
        this.ttaperollno3 = ttaperollno3;
    }

    /**
     * 端子ﾃｰﾌﾟﾛｰﾙNo 4本目
     * @return the ttaperollno4
     */
    public String getTtaperollno4() {
        return ttaperollno4;
    }

    /**
     * 端子ﾃｰﾌﾟﾛｰﾙNo 4本目
     * @param ttaperollno4 the ttaperollno4 to set
     */
    public void setTtaperollno4(String ttaperollno4) {
        this.ttaperollno4 = ttaperollno4;
    }

    /**
     * 端子ﾃｰﾌﾟﾛｰﾙNo 5本目
     * @return the ttaperollno5
     */
    public String getTtaperollno5() {
        return ttaperollno5;
    }

    /**
     * 端子ﾃｰﾌﾟﾛｰﾙNo 5本目
     * @param ttaperollno5 the ttaperollno5 to set
     */
    public void setTtaperollno5(String ttaperollno5) {
        this.ttaperollno5 = ttaperollno5;
    }

    /**
     * 端子原料記号
     * @return the tgenryokigo
     */
    public String getTgenryokigo() {
        return tgenryokigo;
    }

    /**
     * 端子原料記号
     * @param tgenryokigo the tgenryokigo to set
     */
    public void setTgenryokigo(String tgenryokigo) {
        this.tgenryokigo = tgenryokigo;
    }

    /**
     * 下端子仕様
     * @return the stsiyo
     */
    public String getStsiyo() {
        return stsiyo;
    }

    /**
     * 下端子仕様
     * @param stsiyo the stsiyo to set
     */
    public void setStsiyo(String stsiyo) {
        this.stsiyo = stsiyo;
    }

    /**
     * 電極積層仕様
     * @return the esekisosiyo
     */
    public Long getEsekisosiyo() {
        return esekisosiyo;
    }

    /**
     * 電極積層仕様
     * @param esekisosiyo the esekisosiyo to set
     */
    public void setEsekisosiyo(Long esekisosiyo) {
        this.esekisosiyo = esekisosiyo;
    }

    /**
     * 電極ﾃｰﾌﾟ種類
     * @return the etapesyurui
     */
    public String getEtapesyurui() {
        return etapesyurui;
    }

    /**
     * 電極ﾃｰﾌﾟ種類
     * @param etapesyurui the etapesyurui to set
     */
    public void setEtapesyurui(String etapesyurui) {
        this.etapesyurui = etapesyurui;
    }

    /**
     * 電極ﾃｰﾌﾟ原料lot
     * @return the etapeglot
     */
    public String getEtapeglot() {
        return etapeglot;
    }

    /**
     * 電極ﾃｰﾌﾟ原料lot
     * @param etapeglot the etapeglot to set
     */
    public void setEtapeglot(String etapeglot) {
        this.etapeglot = etapeglot;
    }

    /**
     * 電極ﾃｰﾌﾟﾛｯﾄ
     * @return the etapelot
     */
    public String getEtapelot() {
        return etapelot;
    }

    /**
     * 電極ﾃｰﾌﾟﾛｯﾄ
     * @param etapelot the etapelot to set
     */
    public void setEtapelot(String etapelot) {
        this.etapelot = etapelot;
    }

    /**
     * 電極ﾃｰﾌﾟｽﾘｯﾌﾟ記号
     * @return the etapeslipkigo
     */
    public String getEtapeslipkigo() {
        return etapeslipkigo;
    }

    /**
     * 電極ﾃｰﾌﾟｽﾘｯﾌﾟ記号
     * @param etapeslipkigo the etapeslipkigo to set
     */
    public void setEtapeslipkigo(String etapeslipkigo) {
        this.etapeslipkigo = etapeslipkigo;
    }

    /**
     * 電極ﾃｰﾌﾟﾛｰﾙNo 1本目
     * @return the etaperollno1
     */
    public String getEtaperollno1() {
        return etaperollno1;
    }

    /**
     * 電極ﾃｰﾌﾟﾛｰﾙNo 1本目
     * @param etaperollno1 the etaperollno1 to set
     */
    public void setEtaperollno1(String etaperollno1) {
        this.etaperollno1 = etaperollno1;
    }

    /**
     * 電極ﾃｰﾌﾟﾛｰﾙNo 2本目
     * @return the etaperollno2
     */
    public String getEtaperollno2() {
        return etaperollno2;
    }

    /**
     * 電極ﾃｰﾌﾟﾛｰﾙNo 2本目
     * @param etaperollno2 the etaperollno2 to set
     */
    public void setEtaperollno2(String etaperollno2) {
        this.etaperollno2 = etaperollno2;
    }

    /**
     * 電極ﾃｰﾌﾟﾛｰﾙNo 3本目
     * @return the etaperollno3
     */
    public String getEtaperollno3() {
        return etaperollno3;
    }

    /**
     * 電極ﾃｰﾌﾟﾛｰﾙNo 3本目
     * @param etaperollno3 the etaperollno3 to set
     */
    public void setEtaperollno3(String etaperollno3) {
        this.etaperollno3 = etaperollno3;
    }

    /**
     * 電極ﾃｰﾌﾟﾛｰﾙNo 4本目
     * @return the etaperollno4
     */
    public String getEtaperollno4() {
        return etaperollno4;
    }

    /**
     * 電極ﾃｰﾌﾟﾛｰﾙNo 4本目
     * @param etaperollno4 the etaperollno4 to set
     */
    public void setEtaperollno4(String etaperollno4) {
        this.etaperollno4 = etaperollno4;
    }

    /**
     * 電極ﾃｰﾌﾟﾛｰﾙNo 5本目
     * @return the etaperollno5
     */
    public String getEtaperollno5() {
        return etaperollno5;
    }

    /**
     * 電極ﾃｰﾌﾟﾛｰﾙNo 5本目
     * @param etaperollno5 the etaperollno5 to set
     */
    public void setEtaperollno5(String etaperollno5) {
        this.etaperollno5 = etaperollno5;
    }

    /**
     * 積層ﾌﾟﾚｽ通電時間(瞬時加熱)
     * @return the sptudenjikan
     */
    public BigDecimal getSptudenjikan() {
        return sptudenjikan;
    }

    /**
     * 積層ﾌﾟﾚｽ通電時間(瞬時加熱)
     * @param sptudenjikan the sptudenjikan to set
     */
    public void setSptudenjikan(BigDecimal sptudenjikan) {
        this.sptudenjikan = sptudenjikan;
    }

    /**
     * 積層加圧力
     * @return the skaaturyoku
     */
    public BigDecimal getSkaaturyoku() {
        return skaaturyoku;
    }

    /**
     * 積層加圧力
     * @param skaaturyoku the skaaturyoku to set
     */
    public void setSkaaturyoku(BigDecimal skaaturyoku) {
        this.skaaturyoku = skaaturyoku;
    }

    /**
     * 瞬時加熱ﾍｯﾄﾞNo
     * @return the skheadno
     */
    public String getSkheadno() {
        return skheadno;
    }

    /**
     * 瞬時加熱ﾍｯﾄﾞNo
     * @param skheadno the skheadno to set
     */
    public void setSkheadno(String skheadno) {
        this.skheadno = skheadno;
    }

    /**
     * SUS板使用回数
     * @return the susskaisuu
     */
    public Long getSusskaisuu() {
        return susskaisuu;
    }

    /**
     * SUS板使用回数
     * @param susskaisuu the susskaisuu to set
     */
    public void setSusskaisuu(Long susskaisuu) {
        this.susskaisuu = susskaisuu;
    }

    /**
     * 電極誘電体ﾍﾟｰｽﾄ名
     * @return the ecpastemei
     */
    public String getEcpastemei() {
        return ecpastemei;
    }

    /**
     * 電極誘電体ﾍﾟｰｽﾄ名
     * @param ecpastemei the ecpastemei to set
     */
    public void setEcpastemei(String ecpastemei) {
        this.ecpastemei = ecpastemei;
    }

    /**
     * 電極ﾍﾟｰｽﾄﾛｯﾄNo
     * @return the epastelotno
     */
    public String getEpastelotno() {
        return epastelotno;
    }

    /**
     * 電極ﾍﾟｰｽﾄﾛｯﾄNo
     * @param epastelotno the epastelotno to set
     */
    public void setEpastelotno(String epastelotno) {
        this.epastelotno = epastelotno;
    }

    /**
     * 電極ﾍﾟｰｽﾄ粘度
     * @return the epastenendo
     */
    public BigDecimal getEpastenendo() {
        return epastenendo;
    }

    /**
     * 電極ﾍﾟｰｽﾄ粘度
     * @param epastenendo the epastenendo to set
     */
    public void setEpastenendo(BigDecimal epastenendo) {
        this.epastenendo = epastenendo;
    }

    /**
     * 電極ﾍﾟｰｽﾄ温度
     * @return the epasteondo
     */
    public BigDecimal getEpasteondo() {
        return epasteondo;
    }

    /**
     * 電極ﾍﾟｰｽﾄ温度
     * @param epasteondo the epasteondo to set
     */
    public void setEpasteondo(BigDecimal epasteondo) {
        this.epasteondo = epasteondo;
    }

    /**
     * 電極製版名
     * @return the eseihanmei
     */
    public String getEseihanmei() {
        return eseihanmei;
    }

    /**
     * 電極製版名
     * @param eseihanmei the eseihanmei to set
     */
    public void setEseihanmei(String eseihanmei) {
        this.eseihanmei = eseihanmei;
    }

    /**
     * 電極製版No
     * @return the eseihanno
     */
    public String getEseihanno() {
        return eseihanno;
    }

    /**
     * 電極製版No
     * @param eseihanno the eseihanno to set
     */
    public void setEseihanno(String eseihanno) {
        this.eseihanno = eseihanno;
    }

    /**
     * 電極製版枚数
     * @return the eseimaisuu
     */
    public Long getEseimaisuu() {
        return eseimaisuu;
    }

    /**
     * 電極製版枚数
     * @param eseimaisuu the eseimaisuu to set
     */
    public void setEseimaisuu(Long eseimaisuu) {
        this.eseimaisuu = eseimaisuu;
    }

    /**
     * 電極ｸﾘｱﾗﾝｽ
     * @return the eclearance
     */
    public BigDecimal getEclearance() {
        return eclearance;
    }

    /**
     * 電極ｸﾘｱﾗﾝｽ
     * @param eclearance the eclearance to set
     */
    public void setEclearance(BigDecimal eclearance) {
        this.eclearance = eclearance;
    }

    /**
     * 電極差圧
     * @return the esaatu
     */
    public BigDecimal getEsaatu() {
        return esaatu;
    }

    /**
     * 電極差圧
     * @param esaatu the esaatu to set
     */
    public void setEsaatu(BigDecimal esaatu) {
        this.esaatu = esaatu;
    }

    /**
     * 電極ｽｷｰｼﾞNo
     * @return the eskeegeno
     */
    public String getEskeegeno() {
        return eskeegeno;
    }

    /**
     * 電極ｽｷｰｼﾞNo
     * @param eskeegeno the eskeegeno to set
     */
    public void setEskeegeno(String eskeegeno) {
        this.eskeegeno = eskeegeno;
    }

    /**
     * 電極ｽｷｰｼﾞ枚数
     * @return the eskmaisuu
     */
    public Long getEskmaisuu() {
        return eskmaisuu;
    }

    /**
     * 電極ｽｷｰｼﾞ枚数
     * @param eskmaisuu the eskmaisuu to set
     */
    public void setEskmaisuu(Long eskmaisuu) {
        this.eskmaisuu = eskmaisuu;
    }

    /**
     * 電極ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
     * @return the eskspeed
     */
    public Long getEskspeed() {
        return eskspeed;
    }

    /**
     * 電極ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
     * @param eskspeed the eskspeed to set
     */
    public void setEskspeed(Long eskspeed) {
        this.eskspeed = eskspeed;
    }

    /**
     * 電極ｽｸﾚｯﾊﾟｸﾘｱﾗﾝｽ
     * @return the escclearance
     */
    public BigDecimal getEscclearance() {
        return escclearance;
    }

    /**
     * 電極ｽｸﾚｯﾊﾟｸﾘｱﾗﾝｽ
     * @param escclearance the escclearance to set
     */
    public void setEscclearance(BigDecimal escclearance) {
        this.escclearance = escclearance;
    }

    /**
     * 電極ｽｷｰｼﾞ下降待ち時間
     * @return the eskkmjikan
     */
    public BigDecimal getEskkmjikan() {
        return eskkmjikan;
    }

    /**
     * 電極ｽｷｰｼﾞ下降待ち時間
     * @param eskkmjikan the eskkmjikan to set
     */
    public void setEskkmjikan(BigDecimal eskkmjikan) {
        this.eskkmjikan = eskkmjikan;
    }

    /**
     * 電極L/Dｽﾀｰﾄ時
     * @return the eldstart
     */
    public BigDecimal getEldstart() {
        return eldstart;
    }

    /**
     * 電極L/Dｽﾀｰﾄ時
     * @param eldstart the eldstart to set
     */
    public void setEldstart(BigDecimal eldstart) {
        this.eldstart = eldstart;
    }

    /**
     * 電極製版面積
     * @return the eseimenseki
     */
    public BigDecimal getEseimenseki() {
        return eseimenseki;
    }

    /**
     * 電極製版面積
     * @param eseimenseki the eseimenseki to set
     */
    public void setEseimenseki(BigDecimal eseimenseki) {
        this.eseimenseki = eseimenseki;
    }

    /**
     * 電極膜厚
     * @return the emakuatu
     */
    public BigDecimal getEmakuatu() {
        return emakuatu;
    }

    /**
     * 電極膜厚
     * @param emakuatu the emakuatu to set
     */
    public void setEmakuatu(BigDecimal emakuatu) {
        this.emakuatu = emakuatu;
    }

    /**
     * 電極ｽﾗｲﾄﾞ量
     * @return the eslideryo
     */
    public BigDecimal getEslideryo() {
        return eslideryo;
    }

    /**
     * 電極ｽﾗｲﾄﾞ量
     * @param eslideryo the eslideryo to set
     */
    public void setEslideryo(BigDecimal eslideryo) {
        this.eslideryo = eslideryo;
    }

    /**
     * 電極乾燥温度
     * @return the ekansoondo
     */
    public Long getEkansoondo() {
        return ekansoondo;
    }

    /**
     * 電極乾燥温度
     * @param ekansoondo the ekansoondo to set
     */
    public void setEkansoondo(Long ekansoondo) {
        this.ekansoondo = ekansoondo;
    }

    /**
     * 電極乾燥時間
     * @return the ekansojikan
     */
    public Long getEkansojikan() {
        return ekansojikan;
    }

    /**
     * 電極乾燥時間
     * @param ekansojikan the ekansojikan to set
     */
    public void setEkansojikan(Long ekansojikan) {
        this.ekansojikan = ekansojikan;
    }

    /**
     * 誘電体ﾍﾟｰｽﾄﾛｯﾄNo
     * @return the cpastelotno
     */
    public String getCpastelotno() {
        return cpastelotno;
    }

    /**
     * 誘電体ﾍﾟｰｽﾄﾛｯﾄNo
     * @param cpastelotno the cpastelotno to set
     */
    public void setCpastelotno(String cpastelotno) {
        this.cpastelotno = cpastelotno;
    }

    /**
     * 誘電体ﾍﾟｰｽﾄ粘度
     * @return the cpastenendo
     */
    public BigDecimal getCpastenendo() {
        return cpastenendo;
    }

    /**
     * 誘電体ﾍﾟｰｽﾄ粘度
     * @param cpastenendo the cpastenendo to set
     */
    public void setCpastenendo(BigDecimal cpastenendo) {
        this.cpastenendo = cpastenendo;
    }

    /**
     * 誘電体ﾍﾟｰｽﾄ温度
     * @return the cpasteondo
     */
    public BigDecimal getCpasteondo() {
        return cpasteondo;
    }

    /**
     * 誘電体ﾍﾟｰｽﾄ温度
     * @param cpasteondo the cpasteondo to set
     */
    public void setCpasteondo(BigDecimal cpasteondo) {
        this.cpasteondo = cpasteondo;
    }

    /**
     * 誘電体製版名
     * @return the cseihanmei
     */
    public String getCseihanmei() {
        return cseihanmei;
    }

    /**
     * 誘電体製版名
     * @param cseihanmei the cseihanmei to set
     */
    public void setCseihanmei(String cseihanmei) {
        this.cseihanmei = cseihanmei;
    }

    /**
     * 誘電体製版No
     * @return the cseihanno
     */
    public String getCseihanno() {
        return cseihanno;
    }

    /**
     * 誘電体製版No
     * @param cseihanno the cseihanno to set
     */
    public void setCseihanno(String cseihanno) {
        this.cseihanno = cseihanno;
    }

    /**
     * 誘電体製版枚数
     * @return the cseimaisuu
     */
    public Long getCseimaisuu() {
        return cseimaisuu;
    }

    /**
     * 誘電体製版枚数
     * @param cseimaisuu the cseimaisuu to set
     */
    public void setCseimaisuu(Long cseimaisuu) {
        this.cseimaisuu = cseimaisuu;
    }

    /**
     * 誘電体ｸﾘｱﾗﾝｽ
     * @return the cclearance
     */
    public BigDecimal getCclearance() {
        return cclearance;
    }

    /**
     * 誘電体ｸﾘｱﾗﾝｽ
     * @param cclearance the cclearance to set
     */
    public void setCclearance(BigDecimal cclearance) {
        this.cclearance = cclearance;
    }

    /**
     * 誘電体差圧
     * @return the csaatu
     */
    public BigDecimal getCsaatu() {
        return csaatu;
    }

    /**
     * 誘電体差圧
     * @param csaatu the csaatu to set
     */
    public void setCsaatu(BigDecimal csaatu) {
        this.csaatu = csaatu;
    }

    /**
     * 誘電体ｽｷｰｼﾞNo
     * @return the cskeegeno
     */
    public String getCskeegeno() {
        return cskeegeno;
    }

    /**
     * 誘電体ｽｷｰｼﾞNo
     * @param cskeegeno the cskeegeno to set
     */
    public void setCskeegeno(String cskeegeno) {
        this.cskeegeno = cskeegeno;
    }

    /**
     * 誘電体ｽｷｰｼﾞ枚数
     * @return the cskmaisuu
     */
    public Long getCskmaisuu() {
        return cskmaisuu;
    }

    /**
     * 誘電体ｽｷｰｼﾞ枚数
     * @param cskmaisuu the cskmaisuu to set
     */
    public void setCskmaisuu(Long cskmaisuu) {
        this.cskmaisuu = cskmaisuu;
    }

    /**
     * 誘電体ｽｸﾚｯﾊﾟｸﾘｱﾗﾝｽ
     * @return the cscclearance
     */
    public BigDecimal getCscclearance() {
        return cscclearance;
    }

    /**
     * 誘電体ｽｸﾚｯﾊﾟｸﾘｱﾗﾝｽ
     * @param cscclearance the cscclearance to set
     */
    public void setCscclearance(BigDecimal cscclearance) {
        this.cscclearance = cscclearance;
    }

    /**
     * 誘電体ｽｷｰｼﾞ下降待ち時間
     * @return the cskkmjikan
     */
    public BigDecimal getCskkmjikan() {
        return cskkmjikan;
    }

    /**
     * 誘電体ｽｷｰｼﾞ下降待ち時間
     * @param cskkmjikan the cskkmjikan to set
     */
    public void setCskkmjikan(BigDecimal cskkmjikan) {
        this.cskkmjikan = cskkmjikan;
    }

    /**
     * 誘電体ｼﾌﾄ印刷
     * @return the cshiftinsatu
     */
    public Long getCshiftinsatu() {
        return cshiftinsatu;
    }

    /**
     * 誘電体ｼﾌﾄ印刷
     * @param cshiftinsatu the cshiftinsatu to set
     */
    public void setCshiftinsatu(Long cshiftinsatu) {
        this.cshiftinsatu = cshiftinsatu;
    }

    /**
     * 誘電体L/Dｽﾀｰﾄ時
     * @return the cldstart
     */
    public BigDecimal getCldstart() {
        return cldstart;
    }

    /**
     * 誘電体L/Dｽﾀｰﾄ時
     * @param cldstart the cldstart to set
     */
    public void setCldstart(BigDecimal cldstart) {
        this.cldstart = cldstart;
    }

    /**
     * 誘電体製版面積
     * @return the cseimenseki
     */
    public BigDecimal getCseimenseki() {
        return cseimenseki;
    }

    /**
     * 誘電体製版面積
     * @param cseimenseki the cseimenseki to set
     */
    public void setCseimenseki(BigDecimal cseimenseki) {
        this.cseimenseki = cseimenseki;
    }

    /**
     * 誘電体ｽﾗｲﾄﾞ量
     * @return the cslideryo
     */
    public BigDecimal getCslideryo() {
        return cslideryo;
    }

    /**
     * 誘電体ｽﾗｲﾄﾞ量
     * @param cslideryo the cslideryo to set
     */
    public void setCslideryo(BigDecimal cslideryo) {
        this.cslideryo = cslideryo;
    }

    /**
     * 誘電体乾燥温度
     * @return the ckansoondo
     */
    public Long getCkansoondo() {
        return ckansoondo;
    }

    /**
     * 誘電体乾燥温度
     * @param ckansoondo the ckansoondo to set
     */
    public void setCkansoondo(Long ckansoondo) {
        this.ckansoondo = ckansoondo;
    }

    /**
     * 誘電体乾燥時間
     * @return the ckansojikan
     */
    public Long getCkansojikan() {
        return ckansojikan;
    }

    /**
     * 誘電体乾燥時間
     * @param ckansojikan the ckansojikan to set
     */
    public void setCkansojikan(Long ckansojikan) {
        this.ckansojikan = ckansojikan;
    }

    /**
     * 誘電体膜厚
     * @return the cmakuatu
     */
    public BigDecimal getCmakuatu() {
        return cmakuatu;
    }

    /**
     * 誘電体膜厚
     * @param cmakuatu the cmakuatu to set
     */
    public void setCmakuatu(BigDecimal cmakuatu) {
        this.cmakuatu = cmakuatu;
    }

    /**
     * 合わせ印刷ｻｲﾄﾞRZ1
     * @return the ainsatusrz1
     */
    public BigDecimal getAinsatusrz1() {
        return ainsatusrz1;
    }

    /**
     * 合わせ印刷ｻｲﾄﾞRZ1
     * @param ainsatusrz1 the ainsatusrz1 to set
     */
    public void setAinsatusrz1(BigDecimal ainsatusrz1) {
        this.ainsatusrz1 = ainsatusrz1;
    }

    /**
     * 合わせ印刷ｻｲﾄﾞRZ2
     * @return the ainsatusrz2
     */
    public BigDecimal getAinsatusrz2() {
        return ainsatusrz2;
    }

    /**
     * 合わせ印刷ｻｲﾄﾞRZ2
     * @param ainsatusrz2 the ainsatusrz2 to set
     */
    public void setAinsatusrz2(BigDecimal ainsatusrz2) {
        this.ainsatusrz2 = ainsatusrz2;
    }

    /**
     * 合わせ印刷ｻｲﾄﾞRZ3
     * @return the ainsatusrz3
     */
    public BigDecimal getAinsatusrz3() {
        return ainsatusrz3;
    }

    /**
     * 合わせ印刷ｻｲﾄﾞRZ3
     * @param ainsatusrz3 the ainsatusrz3 to set
     */
    public void setAinsatusrz3(BigDecimal ainsatusrz3) {
        this.ainsatusrz3 = ainsatusrz3;
    }

    /**
     * 合わせ印刷ｻｲﾄﾞRZ4
     * @return the ainsatusrz4
     */
    public BigDecimal getAinsatusrz4() {
        return ainsatusrz4;
    }

    /**
     * 合わせ印刷ｻｲﾄﾞRZ4
     * @param ainsatusrz4 the ainsatusrz4 to set
     */
    public void setAinsatusrz4(BigDecimal ainsatusrz4) {
        this.ainsatusrz4 = ainsatusrz4;
    }

    /**
     * 合わせ印刷ｻｲﾄﾞRZ5
     * @return the ainsatusrz5
     */
    public BigDecimal getAinsatusrz5() {
        return ainsatusrz5;
    }

    /**
     * 合わせ印刷ｻｲﾄﾞRZ5
     * @param ainsatusrz5 the ainsatusrz5 to set
     */
    public void setAinsatusrz5(BigDecimal ainsatusrz5) {
        this.ainsatusrz5 = ainsatusrz5;
    }

    /**
     * 合わせ印刷ｻｲﾄﾞRZAVE
     * @return the ainsatusrzave
     */
    public BigDecimal getAinsatusrzave() {
        return ainsatusrzave;
    }

    /**
     * 合わせ印刷ｻｲﾄﾞRZAVE
     * @param ainsatusrzave the ainsatusrzave to set
     */
    public void setAinsatusrzave(BigDecimal ainsatusrzave) {
        this.ainsatusrzave = ainsatusrzave;
    }

    /**
     * 上端子仕様
     * @return the utsiyo
     */
    public BigDecimal getUtsiyo() {
        return utsiyo;
    }

    /**
     * 上端子仕様
     * @param utsiyo the utsiyo to set
     */
    public void setUtsiyo(BigDecimal utsiyo) {
        this.utsiyo = utsiyo;
    }

    /**
     * 上端子通電時間
     * @return the uttudenjikan
     */
    public BigDecimal getUttudenjikan() {
        return uttudenjikan;
    }

    /**
     * 上端子通電時間
     * @param uttudenjikan the uttudenjikan to set
     */
    public void setUttudenjikan(BigDecimal uttudenjikan) {
        this.uttudenjikan = uttudenjikan;
    }

    /**
     * 上端子加圧力
     * @return the utkaaturyoku
     */
    public BigDecimal getUtkaaturyoku() {
        return utkaaturyoku;
    }

    /**
     * 上端子加圧力
     * @param utkaaturyoku the utkaaturyoku to set
     */
    public void setUtkaaturyoku(BigDecimal utkaaturyoku) {
        this.utkaaturyoku = utkaaturyoku;
    }

    /**
     * 積層体厚み補正
     * @return the stahosei
     */
    public BigDecimal getStahosei() {
        return stahosei;
    }

    /**
     * 積層体厚み補正
     * @param stahosei the stahosei to set
     */
    public void setStahosei(BigDecimal stahosei) {
        this.stahosei = stahosei;
    }

    /**
     * ﾀｰｹﾞｯﾄ印刷ｸﾘｱﾗﾝｽ
     * @return the ticlearance
     */
    public BigDecimal getTiclearance() {
        return ticlearance;
    }

    /**
     * ﾀｰｹﾞｯﾄ印刷ｸﾘｱﾗﾝｽ
     * @param ticlearance the ticlearance to set
     */
    public void setTiclearance(BigDecimal ticlearance) {
        this.ticlearance = ticlearance;
    }

    /**
     * ﾀｰｹﾞｯﾄ印刷差圧
     * @return the tisaatu
     */
    public BigDecimal getTisaatu() {
        return tisaatu;
    }

    /**
     * ﾀｰｹﾞｯﾄ印刷差圧
     * @param tisaatu the tisaatu to set
     */
    public void setTisaatu(BigDecimal tisaatu) {
        this.tisaatu = tisaatu;
    }

    /**
     * ﾀｰｹﾞｯﾄ印刷ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
     * @return the tiskspeed
     */
    public Long getTiskspeed() {
        return tiskspeed;
    }

    /**
     * ﾀｰｹﾞｯﾄ印刷ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
     * @param tiskspeed the tiskspeed to set
     */
    public void setTiskspeed(Long tiskspeed) {
        this.tiskspeed = tiskspeed;
    }

    /**
     * 初層左上X1
     * @return the fsthux1
     */
    public Long getFsthux1() {
        return fsthux1;
    }

    /**
     * 初層左上X1
     * @param fsthux1 the fsthux1 to set
     */
    public void setFsthux1(Long fsthux1) {
        this.fsthux1 = fsthux1;
    }

    /**
     * 初層左上X2
     * @return the fsthux2
     */
    public Long getFsthux2() {
        return fsthux2;
    }

    /**
     * 初層左上X2
     * @param fsthux2 the fsthux2 to set
     */
    public void setFsthux2(Long fsthux2) {
        this.fsthux2 = fsthux2;
    }

    /**
     * 初層左上Y1
     * @return the fsthuy1
     */
    public Long getFsthuy1() {
        return fsthuy1;
    }

    /**
     * 初層左上Y1
     * @param fsthuy1 the fsthuy1 to set
     */
    public void setFsthuy1(Long fsthuy1) {
        this.fsthuy1 = fsthuy1;
    }

    /**
     * 初層左上Y2
     * @return the fsthuy2
     */
    public Long getFsthuy2() {
        return fsthuy2;
    }

    /**
     * 初層左上Y2
     * @param fsthuy2 the fsthuy2 to set
     */
    public void setFsthuy2(Long fsthuy2) {
        this.fsthuy2 = fsthuy2;
    }

    /**
     * 初層左下X1
     * @return the fsthsx1
     */
    public Long getFsthsx1() {
        return fsthsx1;
    }

    /**
     * 初層左下X1
     * @param fsthsx1 the fsthsx1 to set
     */
    public void setFsthsx1(Long fsthsx1) {
        this.fsthsx1 = fsthsx1;
    }

    /**
     * 初層左下X2
     * @return the fsthsx2
     */
    public Long getFsthsx2() {
        return fsthsx2;
    }

    /**
     * 初層左下X2
     * @param fsthsx2 the fsthsx2 to set
     */
    public void setFsthsx2(Long fsthsx2) {
        this.fsthsx2 = fsthsx2;
    }

    /**
     * 初層左下Y1
     * @return the fsthsy1
     */
    public Long getFsthsy1() {
        return fsthsy1;
    }

    /**
     * 初層左下Y1
     * @param fsthsy1 the fsthsy1 to set
     */
    public void setFsthsy1(Long fsthsy1) {
        this.fsthsy1 = fsthsy1;
    }

    /**
     * 初層左下Y2
     * @return the fsthsy2
     */
    public Long getFsthsy2() {
        return fsthsy2;
    }

    /**
     * 初層左下Y2
     * @param fsthsy2 the fsthsy2 to set
     */
    public void setFsthsy2(Long fsthsy2) {
        this.fsthsy2 = fsthsy2;
    }

    /**
     * 初層中央X1
     * @return the fstcx1
     */
    public Long getFstcx1() {
        return fstcx1;
    }

    /**
     * 初層中央X1
     * @param fstcx1 the fstcx1 to set
     */
    public void setFstcx1(Long fstcx1) {
        this.fstcx1 = fstcx1;
    }

    /**
     * 初層中央X2
     * @return the fstcx2
     */
    public Long getFstcx2() {
        return fstcx2;
    }

    /**
     * 初層中央X2
     * @param fstcx2 the fstcx2 to set
     */
    public void setFstcx2(Long fstcx2) {
        this.fstcx2 = fstcx2;
    }

    /**
     * 初層中央Y1
     * @return the fstcy1
     */
    public Long getFstcy1() {
        return fstcy1;
    }

    /**
     * 初層中央Y1
     * @param fstcy1 the fstcy1 to set
     */
    public void setFstcy1(Long fstcy1) {
        this.fstcy1 = fstcy1;
    }

    /**
     * 初層中央Y2
     * @return the fstcy2
     */
    public Long getFstcy2() {
        return fstcy2;
    }

    /**
     * 初層中央Y2
     * @param fstcy2 the fstcy2 to set
     */
    public void setFstcy2(Long fstcy2) {
        this.fstcy2 = fstcy2;
    }

    /**
     * 初層右上X1
     * @return the fstmux1
     */
    public Long getFstmux1() {
        return fstmux1;
    }

    /**
     * 初層右上X1
     * @param fstmux1 the fstmux1 to set
     */
    public void setFstmux1(Long fstmux1) {
        this.fstmux1 = fstmux1;
    }

    /**
     * 初層右上X2
     * @return the fstmux2
     */
    public Long getFstmux2() {
        return fstmux2;
    }

    /**
     * 初層右上X2
     * @param fstmux2 the fstmux2 to set
     */
    public void setFstmux2(Long fstmux2) {
        this.fstmux2 = fstmux2;
    }

    /**
     * 初層右上Y1
     * @return the fstmuy1
     */
    public Long getFstmuy1() {
        return fstmuy1;
    }

    /**
     * 初層右上Y1
     * @param fstmuy1 the fstmuy1 to set
     */
    public void setFstmuy1(Long fstmuy1) {
        this.fstmuy1 = fstmuy1;
    }

    /**
     * 初層右上Y2
     * @return the fstmuy2
     */
    public Long getFstmuy2() {
        return fstmuy2;
    }

    /**
     * 初層右上Y2
     * @param fstmuy2 the fstmuy2 to set
     */
    public void setFstmuy2(Long fstmuy2) {
        this.fstmuy2 = fstmuy2;
    }

    /**
     * 初層右下X1
     * @return the fstmsx1
     */
    public Long getFstmsx1() {
        return fstmsx1;
    }

    /**
     * 初層右下X1
     * @param fstmsx1 the fstmsx1 to set
     */
    public void setFstmsx1(Long fstmsx1) {
        this.fstmsx1 = fstmsx1;
    }

    /**
     * 初層右下X2
     * @return the fstmsx2
     */
    public Long getFstmsx2() {
        return fstmsx2;
    }

    /**
     * 初層右下X2
     * @param fstmsx2 the fstmsx2 to set
     */
    public void setFstmsx2(Long fstmsx2) {
        this.fstmsx2 = fstmsx2;
    }

    /**
     * 初層右下Y1
     * @return the fstmsy1
     */
    public Long getFstmsy1() {
        return fstmsy1;
    }

    /**
     * 初層右下Y1
     * @param fstmsy1 the fstmsy1 to set
     */
    public void setFstmsy1(Long fstmsy1) {
        this.fstmsy1 = fstmsy1;
    }

    /**
     * 初層右下Y2
     * @return the fstmsy2
     */
    public Long getFstmsy2() {
        return fstmsy2;
    }

    /**
     * 初層右下Y2
     * @param fstmsy2 the fstmsy2 to set
     */
    public void setFstmsy2(Long fstmsy2) {
        this.fstmsy2 = fstmsy2;
    }

    /**
     * 最終層左上X1
     * @return the lsthux1
     */
    public Long getLsthux1() {
        return lsthux1;
    }

    /**
     * 最終層左上X1
     * @param lsthux1 the lsthux1 to set
     */
    public void setLsthux1(Long lsthux1) {
        this.lsthux1 = lsthux1;
    }

    /**
     * 最終層左上X2
     * @return the lsthux2
     */
    public Long getLsthux2() {
        return lsthux2;
    }

    /**
     * 最終層左上X2
     * @param lsthux2 the lsthux2 to set
     */
    public void setLsthux2(Long lsthux2) {
        this.lsthux2 = lsthux2;
    }

    /**
     * 最終層左上Y1
     * @return the lsthuy1
     */
    public Long getLsthuy1() {
        return lsthuy1;
    }

    /**
     * 最終層左上Y1
     * @param lsthuy1 the lsthuy1 to set
     */
    public void setLsthuy1(Long lsthuy1) {
        this.lsthuy1 = lsthuy1;
    }

    /**
     * 最終層左上Y2
     * @return the lsthuy2
     */
    public Long getLsthuy2() {
        return lsthuy2;
    }

    /**
     * 最終層左上Y2
     * @param lsthuy2 the lsthuy2 to set
     */
    public void setLsthuy2(Long lsthuy2) {
        this.lsthuy2 = lsthuy2;
    }

    /**
     * 最終層左下X1
     * @return the lsthsx1
     */
    public Long getLsthsx1() {
        return lsthsx1;
    }

    /**
     * 最終層左下X1
     * @param lsthsx1 the lsthsx1 to set
     */
    public void setLsthsx1(Long lsthsx1) {
        this.lsthsx1 = lsthsx1;
    }

    /**
     * 最終層左下X2
     * @return the lsthsx2
     */
    public Long getLsthsx2() {
        return lsthsx2;
    }

    /**
     * 最終層左下X2
     * @param lsthsx2 the lsthsx2 to set
     */
    public void setLsthsx2(Long lsthsx2) {
        this.lsthsx2 = lsthsx2;
    }

    /**
     * 最終層左下Y1
     * @return the lsthsy1
     */
    public Long getLsthsy1() {
        return lsthsy1;
    }

    /**
     * 最終層左下Y1
     * @param lsthsy1 the lsthsy1 to set
     */
    public void setLsthsy1(Long lsthsy1) {
        this.lsthsy1 = lsthsy1;
    }

    /**
     * 最終層左下Y2
     * @return the lsthsy2
     */
    public Long getLsthsy2() {
        return lsthsy2;
    }

    /**
     * 最終層左下Y2
     * @param lsthsy2 the lsthsy2 to set
     */
    public void setLsthsy2(Long lsthsy2) {
        this.lsthsy2 = lsthsy2;
    }

    /**
     * 最終層中央X1
     * @return the lstcx1
     */
    public Long getLstcx1() {
        return lstcx1;
    }

    /**
     * 最終層中央X1
     * @param lstcx1 the lstcx1 to set
     */
    public void setLstcx1(Long lstcx1) {
        this.lstcx1 = lstcx1;
    }

    /**
     * 最終層中央X2
     * @return the lstcx2
     */
    public Long getLstcx2() {
        return lstcx2;
    }

    /**
     * 最終層中央X2
     * @param lstcx2 the lstcx2 to set
     */
    public void setLstcx2(Long lstcx2) {
        this.lstcx2 = lstcx2;
    }

    /**
     * 最終層中央Y1
     * @return the lstcy1
     */
    public Long getLstcy1() {
        return lstcy1;
    }

    /**
     * 最終層中央Y1
     * @param lstcy1 the lstcy1 to set
     */
    public void setLstcy1(Long lstcy1) {
        this.lstcy1 = lstcy1;
    }

    /**
     * 最終層中央Y2
     * @return the lstcy2
     */
    public Long getLstcy2() {
        return lstcy2;
    }

    /**
     * 最終層中央Y2
     * @param lstcy2 the lstcy2 to set
     */
    public void setLstcy2(Long lstcy2) {
        this.lstcy2 = lstcy2;
    }

    /**
     * 最終層右上X1
     * @return the lstmux1
     */
    public Long getLstmux1() {
        return lstmux1;
    }

    /**
     * 最終層右上X1
     * @param lstmux1 the lstmux1 to set
     */
    public void setLstmux1(Long lstmux1) {
        this.lstmux1 = lstmux1;
    }

    /**
     * 最終層右上X2
     * @return the lstmux2
     */
    public Long getLstmux2() {
        return lstmux2;
    }

    /**
     * 最終層右上X2
     * @param lstmux2 the lstmux2 to set
     */
    public void setLstmux2(Long lstmux2) {
        this.lstmux2 = lstmux2;
    }

    /**
     * 最終層右上Y1
     * @return the lstmuy1
     */
    public Long getLstmuy1() {
        return lstmuy1;
    }

    /**
     * 最終層右上Y1
     * @param lstmuy1 the lstmuy1 to set
     */
    public void setLstmuy1(Long lstmuy1) {
        this.lstmuy1 = lstmuy1;
    }

    /**
     * 最終層右上Y2
     * @return the lstmuy2
     */
    public Long getLstmuy2() {
        return lstmuy2;
    }

    /**
     * 最終層右上Y2
     * @param lstmuy2 the lstmuy2 to set
     */
    public void setLstmuy2(Long lstmuy2) {
        this.lstmuy2 = lstmuy2;
    }

    /**
     * 最終層右下X1
     * @return the lstmsx1
     */
    public Long getLstmsx1() {
        return lstmsx1;
    }

    /**
     * 最終層右下X1
     * @param lstmsx1 the lstmsx1 to set
     */
    public void setLstmsx1(Long lstmsx1) {
        this.lstmsx1 = lstmsx1;
    }

    /**
     * 最終層右下X2
     * @return the lstmsx2
     */
    public Long getLstmsx2() {
        return lstmsx2;
    }

    /**
     * 最終層右下X2
     * @param lstmsx2 the lstmsx2 to set
     */
    public void setLstmsx2(Long lstmsx2) {
        this.lstmsx2 = lstmsx2;
    }

    /**
     * 最終層右下Y1
     * @return the lstmsy1
     */
    public Long getLstmsy1() {
        return lstmsy1;
    }

    /**
     * 最終層右下Y1
     * @param lstmsy1 the lstmsy1 to set
     */
    public void setLstmsy1(Long lstmsy1) {
        this.lstmsy1 = lstmsy1;
    }

    /**
     * 最終層右下Y2
     * @return the lstmsy2
     */
    public Long getLstmsy2() {
        return lstmsy2;
    }

    /**
     * 最終層右下Y2
     * @param lstmsy2 the lstmsy2 to set
     */
    public void setLstmsy2(Long lstmsy2) {
        this.lstmsy2 = lstmsy2;
    }

    /**
     * 備考1
     * @return the biko1
     */
    public String getBiko1() {
        return biko1;
    }

    /**
     * 備考1
     * @param biko1 the biko1 to set
     */
    public void setBiko1(String biko1) {
        this.biko1 = biko1;
    }

    /**
     * 備考2
     * @return the biko2
     */
    public String getBiko2() {
        return biko2;
    }

    /**
     * 備考2
     * @param biko2 the biko2 to set
     */
    public void setBiko2(String biko2) {
        this.biko2 = biko2;
    }

    /**
     * 号機
     * @return the goki
     */
    public String getGoki() {
        return goki;
    }

    /**
     * 号機
     * @param goki the goki to set
     */
    public void setGoki(String goki) {
        this.goki = goki;
    }

    /**
     * 特別端子枚数上
     * @return the ttansisuuu
     */
    public Long getTtansisuuu() {
        return ttansisuuu;
    }

    /**
     * 特別端子枚数上
     * @param ttansisuuu the ttansisuuu to set
     */
    public void setTtansisuuu(Long ttansisuuu) {
        this.ttansisuuu = ttansisuuu;
    }

    /**
     * 特別端子枚数下
     * @return the ttansisuus
     */
    public Long getTtansisuus() {
        return ttansisuus;
    }

    /**
     * 特別端子枚数下
     * @param ttansisuus the ttansisuus to set
     */
    public void setTtansisuus(Long ttansisuus) {
        this.ttansisuus = ttansisuus;
    }

    /**
     * 瞬時加熱時間
     * @return the shunkankanetsujikan
     */
    public BigDecimal getShunkankanetsujikan() {
        return shunkankanetsujikan;
    }

    /**
     * 瞬時加熱時間
     * @param shunkankanetsujikan the shunkankanetsujikan to set
     */
    public void setShunkankanetsujikan(BigDecimal shunkankanetsujikan) {
        this.shunkankanetsujikan = shunkankanetsujikan;
    }

    /**
     * PETﾌｨﾙﾑ種類
     * @return the petfilmsyurui
     */
    public String getPetfilmsyurui() {
        return petfilmsyurui;
    }

    /**
     * PETﾌｨﾙﾑ種類
     * @param petfilmsyurui the petfilmsyurui to set
     */
    public void setPetfilmsyurui(String petfilmsyurui) {
        this.petfilmsyurui = petfilmsyurui;
    }

    /**
     * 加圧力
     * @return the kaaturyoku
     */
    public Long getKaaturyoku() {
        return kaaturyoku;
    }

    /**
     * 加圧力
     * @param kaaturyoku the kaaturyoku to set
     */
    public void setKaaturyoku(Long kaaturyoku) {
        this.kaaturyoku = kaaturyoku;
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
     * 積層上昇速切替位置
     * @return the sekijsskirikaeichi
     */
    public BigDecimal getSekijsskirikaeichi() {
        return sekijsskirikaeichi;
    }

    /**
     * 積層上昇速切替位置
     * @param sekijsskirikaeichi the sekijsskirikaeichi to set
     */
    public void setSekijsskirikaeichi(BigDecimal sekijsskirikaeichi) {
        this.sekijsskirikaeichi = sekijsskirikaeichi;
    }

    /**
     * 積層下降速切替位置
     * @return the sekikkskirikaeichi
     */
    public BigDecimal getSekikkskirikaeichi() {
        return sekikkskirikaeichi;
    }

    /**
     * 積層下降速切替位置
     * @param sekikkskirikaeichi the sekikkskirikaeichi to set
     */
    public void setSekikkskirikaeichi(BigDecimal sekikkskirikaeichi) {
        this.sekikkskirikaeichi = sekikkskirikaeichi;
    }

    /**
     * 加圧時間
     * @return the kaatujikan
     */
    public BigDecimal getKaatujikan() {
        return kaatujikan;
    }

    /**
     * 加圧時間
     * @param kaatujikan the kaatujikan to set
     */
    public void setKaatujikan(BigDecimal kaatujikan) {
        this.kaatujikan = kaatujikan;
    }

    /**
     * ﾃｰﾌﾟ搬送送りﾋﾟｯﾁ
     * @return the tapehansoupitch
     */
    public Long getTapehansoupitch() {
        return tapehansoupitch;
    }

    /**
     * ﾃｰﾌﾟ搬送送りﾋﾟｯﾁ
     * @param tapehansoupitch the tapehansoupitch to set
     */
    public void setTapehansoupitch(Long tapehansoupitch) {
        this.tapehansoupitch = tapehansoupitch;
    }

    /**
     * ﾃｰﾌﾟ搬送目視確認
     * @return the tapehansoukakunin
     */
    public String getTapehansoukakunin() {
        return tapehansoukakunin;
    }

    /**
     * ﾃｰﾌﾟ搬送目視確認
     * @param tapehansoukakunin the tapehansoukakunin to set
     */
    public void setTapehansoukakunin(String tapehansoukakunin) {
        this.tapehansoukakunin = tapehansoukakunin;
    }

    /**
     * 電極膜厚設定
     * @return the emakuatsusettei
     */
    public BigDecimal getEmakuatsusettei() {
        return emakuatsusettei;
    }

    /**
     * 電極膜厚設定
     * @param emakuatsusettei the emakuatsusettei to set
     */
    public void setEmakuatsusettei(BigDecimal emakuatsusettei) {
        this.emakuatsusettei = emakuatsusettei;
    }

    /**
     * 電極熱風風量
     * @return the eneppufuryou
     */
    public Long getEneppufuryou() {
        return eneppufuryou;
    }

    /**
     * 電極熱風風量
     * @param eneppufuryou the eneppufuryou to set
     */
    public void setEneppufuryou(Long eneppufuryou) {
        this.eneppufuryou = eneppufuryou;
    }

    /**
     * 電極膜厚AVE
     * @return the emakuatsuave
     */
    public BigDecimal getEmakuatsuave() {
        return emakuatsuave;
    }

    /**
     * 電極膜厚AVE
     * @param emakuatsuave the emakuatsuave to set
     */
    public void setEmakuatsuave(BigDecimal emakuatsuave) {
        this.emakuatsuave = emakuatsuave;
    }

    /**
     * 電極膜厚MAX
     * @return the emakuatsumax
     */
    public BigDecimal getEmakuatsumax() {
        return emakuatsumax;
    }

    /**
     * 電極膜厚MAX
     * @param emakuatsumax the emakuatsumax to set
     */
    public void setEmakuatsumax(BigDecimal emakuatsumax) {
        this.emakuatsumax = emakuatsumax;
    }

    /**
     * 電極膜厚MIN
     * @return the emakuatsumin
     */
    public BigDecimal getEmakuatsumin() {
        return emakuatsumin;
    }

    /**
     * 電極膜厚MIN
     * @param emakuatsumin the emakuatsumin to set
     */
    public void setEmakuatsumin(BigDecimal emakuatsumin) {
        this.emakuatsumin = emakuatsumin;
    }

    /**
     * にじみ量測定(ﾊﾟﾀｰﾝ間距離)
     * @return the nijimisokuteiptn
     */
    public Long getNijimisokuteiptn() {
        return nijimisokuteiptn;
    }

    /**
     * にじみ量測定(ﾊﾟﾀｰﾝ間距離)
     * @param nijimisokuteiptn the nijimisokuteiptn to set
     */
    public void setNijimisokuteiptn(Long nijimisokuteiptn) {
        this.nijimisokuteiptn = nijimisokuteiptn;
    }

    /**
     * 印刷ｻﾝﾌﾟﾙ外観確認
     * @return the prnsamplegaikan
     */
    public String getPrnsamplegaikan() {
        return prnsamplegaikan;
    }

    /**
     * 印刷ｻﾝﾌﾟﾙ外観確認
     * @param prnsamplegaikan the prnsamplegaikan to set
     */
    public void setPrnsamplegaikan(String prnsamplegaikan) {
        this.prnsamplegaikan = prnsamplegaikan;
    }

    /**
     * 印刷位置余白長さ
     * @return the prnichiyohakunagasa
     */
    public String getPrnichiyohakunagasa() {
        return prnichiyohakunagasa;
    }

    /**
     * 印刷位置余白長さ
     * @param prnichiyohakunagasa the prnichiyohakunagasa to set
     */
    public void setPrnichiyohakunagasa(String prnichiyohakunagasa) {
        this.prnichiyohakunagasa = prnichiyohakunagasa;
    }

    /**
     * 誘電体ﾃｰﾌﾞﾙｸﾘｱﾗﾝｽ
     * @return the ctableclearance
     */
    public BigDecimal getCtableclearance() {
        return ctableclearance;
    }

    /**
     * 誘電体ﾃｰﾌﾞﾙｸﾘｱﾗﾝｽ
     * @param ctableclearance the ctableclearance to set
     */
    public void setCtableclearance(BigDecimal ctableclearance) {
        this.ctableclearance = ctableclearance;
    }

    /**
     * 誘電体膜厚設定
     * @return the cmakuatsusettei
     */
    public BigDecimal getCmakuatsusettei() {
        return cmakuatsusettei;
    }

    /**
     * 誘電体膜厚設定
     * @param cmakuatsusettei the cmakuatsusettei to set
     */
    public void setCmakuatsusettei(BigDecimal cmakuatsusettei) {
        this.cmakuatsusettei = cmakuatsusettei;
    }

    /**
     * 誘電体ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
     * @return the cskspeed
     */
    public Long getCskspeed() {
        return cskspeed;
    }

    /**
     * 誘電体ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
     * @param cskspeed the cskspeed to set
     */
    public void setCskspeed(Long cskspeed) {
        this.cskspeed = cskspeed;
    }

    /**
     * 誘電体熱風風量
     * @return the cneppufuryou
     */
    public Long getCneppufuryou() {
        return cneppufuryou;
    }

    /**
     * 誘電体熱風風量
     * @param cneppufuryou the cneppufuryou to set
     */
    public void setCneppufuryou(Long cneppufuryou) {
        this.cneppufuryou = cneppufuryou;
    }

    /**
     * 被り量測定
     * @return the kaburiryou
     */
    public String getKaburiryou() {
        return kaburiryou;
    }

    /**
     * 被り量測定
     * @param kaburiryou the kaburiryou to set
     */
    public void setKaburiryou(String kaburiryou) {
        this.kaburiryou = kaburiryou;
    }

    /**
     * 積層中外観
     * @return the sgaikan
     */
    public String getSgaikan() {
        return sgaikan;
    }

    /**
     * 積層中外観
     * @param sgaikan the sgaikan to set
     */
    public void setSgaikan(String sgaikan) {
        this.sgaikan = sgaikan;
    }

    /**
     * にじみ量測定(積層後)
     * @return the nijimisokuteisekisougo
     */
    public Long getNijimisokuteisekisougo() {
        return nijimisokuteisekisougo;
    }

    /**
     * にじみ量測定(積層後)
     * @param nijimisokuteisekisougo the nijimisokuteisekisougo to set
     */
    public void setNijimisokuteisekisougo(Long nijimisokuteisekisougo) {
        this.nijimisokuteisekisougo = nijimisokuteisekisougo;
    }

    /**
     * 積層品外観
     * @return the sekisouhingaikan
     */
    public String getSekisouhingaikan() {
        return sekisouhingaikan;
    }

    /**
     * 積層品外観
     * @param sekisouhingaikan the sekisouhingaikan to set
     */
    public void setSekisouhingaikan(String sekisouhingaikan) {
        this.sekisouhingaikan = sekisouhingaikan;
    }

    /**
     * 積層ｽﾞﾚﾁｪｯｸ
     * @return the sekisouzure
     */
    public String getSekisouzure() {
        return sekisouzure;
    }

    /**
     * 積層ｽﾞﾚﾁｪｯｸ
     * @param sekisouzure the sekisouzure to set
     */
    public void setSekisouzure(String sekisouzure) {
        this.sekisouzure = sekisouzure;
    }

    /**
     * 上端子上昇速切替位置
     * @return the uwajsskirikaeichi
     */
    public BigDecimal getUwajsskirikaeichi() {
        return uwajsskirikaeichi;
    }

    /**
     * 上端子上昇速切替位置
     * @param uwajsskirikaeichi the uwajsskirikaeichi to set
     */
    public void setUwajsskirikaeichi(BigDecimal uwajsskirikaeichi) {
        this.uwajsskirikaeichi = uwajsskirikaeichi;
    }

    /**
     * 下端子下降速切替位置
     * @return the shitakkskirikaeichi
     */
    public BigDecimal getShitakkskirikaeichi() {
        return shitakkskirikaeichi;
    }

    /**
     * 下端子下降速切替位置
     * @param shitakkskirikaeichi the shitakkskirikaeichi to set
     */
    public void setShitakkskirikaeichi(BigDecimal shitakkskirikaeichi) {
        this.shitakkskirikaeichi = shitakkskirikaeichi;
    }

    /**
     * ﾀｰｹﾞｯﾄｲﾝｸ種類
     * @return the tinksyuryui
     */
    public String getTinksyuryui() {
        return tinksyuryui;
    }

    /**
     * ﾀｰｹﾞｯﾄｲﾝｸ種類
     * @param tinksyuryui the tinksyuryui to set
     */
    public void setTinksyuryui(String tinksyuryui) {
        this.tinksyuryui = tinksyuryui;
    }

    /**
     * ﾀｰｹﾞｯﾄｲﾝｸLOT
     * @return the tinklot
     */
    public String getTinklot() {
        return tinklot;
    }

    /**
     * ﾀｰｹﾞｯﾄｲﾝｸLOT
     * @param tinklot the tinklot to set
     */
    public void setTinklot(String tinklot) {
        this.tinklot = tinklot;
    }

    /**
     * ﾀｰｹﾞｯﾄ印刷外観
     * @return the tgaikan
     */
    public Long getTgaikan() {
        return tgaikan;
    }

    /**
     * ﾀｰｹﾞｯﾄ印刷外観
     * @param tgaikan the tgaikan to set
     */
    public void setTgaikan(Long tgaikan) {
        this.tgaikan = tgaikan;
    }

    /**
     * 印刷積層開始担当者
     * @return the starttantou
     */
    public String getStarttantou() {
        return starttantou;
    }

    /**
     * 印刷積層開始担当者
     * @param starttantou the starttantou to set
     */
    public void setStarttantou(String starttantou) {
        this.starttantou = starttantou;
    }

    /**
     * 印刷積層終了担当者
     * @return the endtantou
     */
    public String getEndtantou() {
        return endtantou;
    }

    /**
     * 印刷積層終了担当者
     * @param endtantou the endtantou to set
     */
    public void setEndtantou(String endtantou) {
        this.endtantou = endtantou;
    }

    /**
     * ﾀｰｹﾞｯﾄ印刷終了日
     * @return the tendday
     */
    public String getTendday() {
        return tendday;
    }

    /**
     * ﾀｰｹﾞｯﾄ印刷終了日
     * @param tendday the tendday to set
     */
    public void setTendday(String tendday) {
        this.tendday = tendday;
    }

    /**
     * ﾀｰｹﾞｯﾄ印刷担当者
     * @return the tendtantou
     */
    public String getTendtantou() {
        return tendtantou;
    }

    /**
     * ﾀｰｹﾞｯﾄ印刷担当者
     * @param tendtantou the tendtantou to set
     */
    public void setTendtantou(String tendtantou) {
        this.tendtantou = tendtantou;
    }

    /**
     * 処理ｾｯﾄ数
     * @return the syorisetsuu
     */
    public Long getSyorisetsuu() {
        return syorisetsuu;
    }

    /**
     * 処理ｾｯﾄ数
     * @param syorisetsuu the syorisetsuu to set
     */
    public void setSyorisetsuu(Long syorisetsuu) {
        this.syorisetsuu = syorisetsuu;
    }

    /**
     * 良品ｾｯﾄ数
     * @return the ryouhinsetsuu
     */
    public Long getRyouhinsetsuu() {
        return ryouhinsetsuu;
    }

    /**
     * 良品ｾｯﾄ数
     * @param ryouhinsetsuu the ryouhinsetsuu to set
     */
    public void setRyouhinsetsuu(Long ryouhinsetsuu) {
        this.ryouhinsetsuu = ryouhinsetsuu;
    }

    /**
     * ﾍｯﾄﾞ交換者
     * @return the headkoukantantou
     */
    public String getHeadkoukantantou() {
        return headkoukantantou;
    }

    /**
     * ﾍｯﾄﾞ交換者
     * @param headkoukantantou the headkoukantantou to set
     */
    public void setHeadkoukantantou(String headkoukantantou) {
        this.headkoukantantou = headkoukantantou;
    }

    /**
     * 積層条件者
     * @return the sekisoujoukentantou
     */
    public String getSekisoujoukentantou() {
        return sekisoujoukentantou;
    }

    /**
     * 積層条件者
     * @param sekisoujoukentantou the sekisoujoukentantou to set
     */
    public void setSekisoujoukentantou(String sekisoujoukentantou) {
        this.sekisoujoukentantou = sekisoujoukentantou;
    }

    /**
     * E製版ｾｯﾄ者
     * @return the eseihansettantou
     */
    public String getEseihansettantou() {
        return eseihansettantou;
    }

    /**
     * E製版ｾｯﾄ者
     * @param eseihansettantou the eseihansettantou to set
     */
    public void setEseihansettantou(String eseihansettantou) {
        this.eseihansettantou = eseihansettantou;
    }

    /**
     * C製版ｾｯﾄ者
     * @return the cseihansettantou
     */
    public String getCseihansettantou() {
        return cseihansettantou;
    }

    /**
     * C製版ｾｯﾄ者
     * @param cseihansettantou the cseihansettantou to set
     */
    public void setCseihansettantou(String cseihansettantou) {
        this.cseihansettantou = cseihansettantou;
    }

    /**
     * 段差測定者
     * @return the dansasokuteitantou
     */
    public String getDansasokuteitantou() {
        return dansasokuteitantou;
    }

    /**
     * 段差測定者
     * @param dansasokuteitantou the dansasokuteitantou to set
     */
    public void setDansasokuteitantou(String dansasokuteitantou) {
        this.dansasokuteitantou = dansasokuteitantou;
    }

    /**
     * 電極膜厚1
     * @return the emakuatsu1
     */
    public BigDecimal getEmakuatsu1() {
        return emakuatsu1;
    }

    /**
     * 電極膜厚1
     * @param emakuatsu1 the emakuatsu1 to set
     */
    public void setEmakuatsu1(BigDecimal emakuatsu1) {
        this.emakuatsu1 = emakuatsu1;
    }

    /**
     * 電極膜厚2
     * @return the emakuatsu2
     */
    public BigDecimal getEmakuatsu2() {
        return emakuatsu2;
    }

    /**
     * 電極膜厚2
     * @param emakuatsu2 the emakuatsu2 to set
     */
    public void setEmakuatsu2(BigDecimal emakuatsu2) {
        this.emakuatsu2 = emakuatsu2;
    }

    /**
     * 電極膜厚3
     * @return the emakuatsu3
     */
    public BigDecimal getEmakuatsu3() {
        return emakuatsu3;
    }

    /**
     * 電極膜厚3
     * @param emakuatsu3 the emakuatsu3 to set
     */
    public void setEmakuatsu3(BigDecimal emakuatsu3) {
        this.emakuatsu3 = emakuatsu3;
    }

    /**
     * 電極膜厚4
     * @return the emakuatsu4
     */
    public BigDecimal getEmakuatsu4() {
        return emakuatsu4;
    }

    /**
     * 電極膜厚4
     * @param emakuatsu4 the emakuatsu4 to set
     */
    public void setEmakuatsu4(BigDecimal emakuatsu4) {
        this.emakuatsu4 = emakuatsu4;
    }

    /**
     * 電極膜厚5
     * @return the emakuatsu5
     */
    public BigDecimal getEmakuatsu5() {
        return emakuatsu5;
    }

    /**
     * 電極膜厚5
     * @param emakuatsu5 the emakuatsu5 to set
     */
    public void setEmakuatsu5(BigDecimal emakuatsu5) {
        this.emakuatsu5 = emakuatsu5;
    }

    /**
     * 電極膜厚6
     * @return the emakuatsu6
     */
    public BigDecimal getEmakuatsu6() {
        return emakuatsu6;
    }

    /**
     * 電極膜厚6
     * @param emakuatsu6 the emakuatsu6 to set
     */
    public void setEmakuatsu6(BigDecimal emakuatsu6) {
        this.emakuatsu6 = emakuatsu6;
    }

    /**
     * 電極膜厚7
     * @return the emakuatsu7
     */
    public BigDecimal getEmakuatsu7() {
        return emakuatsu7;
    }

    /**
     * 電極膜厚7
     * @param emakuatsu7 the emakuatsu7 to set
     */
    public void setEmakuatsu7(BigDecimal emakuatsu7) {
        this.emakuatsu7 = emakuatsu7;
    }

    /**
     * 電極膜厚8
     * @return the emakuatsu8
     */
    public BigDecimal getEmakuatsu8() {
        return emakuatsu8;
    }

    /**
     * 電極膜厚8
     * @param emakuatsu8 the emakuatsu8 to set
     */
    public void setEmakuatsu8(BigDecimal emakuatsu8) {
        this.emakuatsu8 = emakuatsu8;
    }

    /**
     * 電極膜厚9
     * @return the emakuatsu9
     */
    public BigDecimal getEmakuatsu9() {
        return emakuatsu9;
    }

    /**
     * 電極膜厚9
     * @param emakuatsu9 the emakuatsu9 to set
     */
    public void setEmakuatsu9(BigDecimal emakuatsu9) {
        this.emakuatsu9 = emakuatsu9;
    }

    /**
     * ﾊﾟﾀｰﾝ間距離1
     * @return the ptndist1
     */
    public Long getPtndist1() {
        return ptndist1;
    }

    /**
     * ﾊﾟﾀｰﾝ間距離1
     * @param ptndist1 the ptndist1 to set
     */
    public void setPtndist1(Long ptndist1) {
        this.ptndist1 = ptndist1;
    }

    /**
     * ﾊﾟﾀｰﾝ間距離2
     * @return the ptndist2
     */
    public Long getPtndist2() {
        return ptndist2;
    }

    /**
     * ﾊﾟﾀｰﾝ間距離2
     * @param ptndist2 the ptndist2 to set
     */
    public void setPtndist2(Long ptndist2) {
        this.ptndist2 = ptndist2;
    }

    /**
     * ﾊﾟﾀｰﾝ間距離3
     * @return the ptndist3
     */
    public Long getPtndist3() {
        return ptndist3;
    }

    /**
     * ﾊﾟﾀｰﾝ間距離3
     * @param ptndist3 the ptndist3 to set
     */
    public void setPtndist3(Long ptndist3) {
        this.ptndist3 = ptndist3;
    }

    /**
     * ﾊﾟﾀｰﾝ間距離4
     * @return the ptndist4
     */
    public Long getPtndist4() {
        return ptndist4;
    }

    /**
     * ﾊﾟﾀｰﾝ間距離4
     * @param ptndist4 the ptndist4 to set
     */
    public void setPtndist4(Long ptndist4) {
        this.ptndist4 = ptndist4;
    }

    /**
     * ﾊﾟﾀｰﾝ間距離5
     * @return the ptndist5
     */
    public Long getPtndist5() {
        return ptndist5;
    }

    /**
     * ﾊﾟﾀｰﾝ間距離5
     * @param ptndist5 the ptndist5 to set
     */
    public void setPtndist5(Long ptndist5) {
        this.ptndist5 = ptndist5;
    }

    /**
     * 合わせ(RZ)1
     * @return the awaserz1
     */
    public BigDecimal getAwaserz1() {
        return awaserz1;
    }

    /**
     * 合わせ(RZ)1
     * @param awaserz1 the awaserz1 to set
     */
    public void setAwaserz1(BigDecimal awaserz1) {
        this.awaserz1 = awaserz1;
    }

    /**
     * 合わせ(RZ)2
     * @return the awaserz2
     */
    public BigDecimal getAwaserz2() {
        return awaserz2;
    }

    /**
     * 合わせ(RZ)2
     * @param awaserz2 the awaserz2 to set
     */
    public void setAwaserz2(BigDecimal awaserz2) {
        this.awaserz2 = awaserz2;
    }

    /**
     * 合わせ(RZ)3
     * @return the awaserz3
     */
    public BigDecimal getAwaserz3() {
        return awaserz3;
    }

    /**
     * 合わせ(RZ)3
     * @param awaserz3 the awaserz3 to set
     */
    public void setAwaserz3(BigDecimal awaserz3) {
        this.awaserz3 = awaserz3;
    }

    /**
     * 合わせ(RZ)4
     * @return the awaserz4
     */
    public BigDecimal getAwaserz4() {
        return awaserz4;
    }

    /**
     * 合わせ(RZ)4
     * @param awaserz4 the awaserz4 to set
     */
    public void setAwaserz4(BigDecimal awaserz4) {
        this.awaserz4 = awaserz4;
    }

    /**
     * 合わせ(RZ)5
     * @return the awaserz5
     */
    public BigDecimal getAwaserz5() {
        return awaserz5;
    }

    /**
     * 合わせ(RZ)5
     * @param awaserz5 the awaserz5 to set
     */
    public void setAwaserz5(BigDecimal awaserz5) {
        this.awaserz5 = awaserz5;
    }

    /**
     * 合わせ(RZ)6
     * @return the awaserz6
     */
    public BigDecimal getAwaserz6() {
        return awaserz6;
    }

    /**
     * 合わせ(RZ)6
     * @param awaserz6 the awaserz6 to set
     */
    public void setAwaserz6(BigDecimal awaserz6) {
        this.awaserz6 = awaserz6;
    }

    /**
     * 合わせ(RZ)7
     * @return the awaserz7
     */
    public BigDecimal getAwaserz7() {
        return awaserz7;
    }

    /**
     * 合わせ(RZ)7
     * @param awaserz7 the awaserz7 to set
     */
    public void setAwaserz7(BigDecimal awaserz7) {
        this.awaserz7 = awaserz7;
    }

    /**
     * 合わせ(RZ)8
     * @return the awaserz8
     */
    public BigDecimal getAwaserz8() {
        return awaserz8;
    }

    /**
     * 合わせ(RZ)8
     * @param awaserz8 the awaserz8 to set
     */
    public void setAwaserz8(BigDecimal awaserz8) {
        this.awaserz8 = awaserz8;
    }

    /**
     * 合わせ(RZ)9
     * @return the awaserz9
     */
    public BigDecimal getAwaserz9() {
        return awaserz9;
    }

    /**
     * 合わせ(RZ)9
     * @param awaserz9 the awaserz9 to set
     */
    public void setAwaserz9(BigDecimal awaserz9) {
        this.awaserz9 = awaserz9;
    }

    /**
     * 被り量左上X1
     * @return the kaburihidariuex1
     */
    public Long getKaburihidariuex1() {
        return kaburihidariuex1;
    }

    /**
     * 被り量左上X1
     * @param kaburihidariuex1 the kaburihidariuex1 to set
     */
    public void setKaburihidariuex1(Long kaburihidariuex1) {
        this.kaburihidariuex1 = kaburihidariuex1;
    }

    /**
     * 被り量左上X2
     * @return the kaburihidariuex2
     */
    public Long getKaburihidariuex2() {
        return kaburihidariuex2;
    }

    /**
     * 被り量左上X2
     * @param kaburihidariuex2 the kaburihidariuex2 to set
     */
    public void setKaburihidariuex2(Long kaburihidariuex2) {
        this.kaburihidariuex2 = kaburihidariuex2;
    }

    /**
     * 被り量左上Y1
     * @return the kaburihidariuey1
     */
    public Long getKaburihidariuey1() {
        return kaburihidariuey1;
    }

    /**
     * 被り量左上Y1
     * @param kaburihidariuey1 the kaburihidariuey1 to set
     */
    public void setKaburihidariuey1(Long kaburihidariuey1) {
        this.kaburihidariuey1 = kaburihidariuey1;
    }

    /**
     * 被り量左上Y2
     * @return the kaburihidariuey2
     */
    public Long getKaburihidariuey2() {
        return kaburihidariuey2;
    }

    /**
     * 被り量左上Y2
     * @param kaburihidariuey2 the kaburihidariuey2 to set
     */
    public void setKaburihidariuey2(Long kaburihidariuey2) {
        this.kaburihidariuey2 = kaburihidariuey2;
    }

    /**
     * 被り量左下X1
     * @return the kaburihidarisitax1
     */
    public Long getKaburihidarisitax1() {
        return kaburihidarisitax1;
    }

    /**
     * 被り量左下X1
     * @param kaburihidarisitax1 the kaburihidarisitax1 to set
     */
    public void setKaburihidarisitax1(Long kaburihidarisitax1) {
        this.kaburihidarisitax1 = kaburihidarisitax1;
    }

    /**
     * 被り量左下X2
     * @return the kaburihidarisitax2
     */
    public Long getKaburihidarisitax2() {
        return kaburihidarisitax2;
    }

    /**
     * 被り量左下X2
     * @param kaburihidarisitax2 the kaburihidarisitax2 to set
     */
    public void setKaburihidarisitax2(Long kaburihidarisitax2) {
        this.kaburihidarisitax2 = kaburihidarisitax2;
    }

    /**
     * 被り量左下Y1
     * @return the kaburihidarisitay1
     */
    public Long getKaburihidarisitay1() {
        return kaburihidarisitay1;
    }

    /**
     * 被り量左下Y1
     * @param kaburihidarisitay1 the kaburihidarisitay1 to set
     */
    public void setKaburihidarisitay1(Long kaburihidarisitay1) {
        this.kaburihidarisitay1 = kaburihidarisitay1;
    }

    /**
     * 被り量左下Y2
     * @return the kaburihidarisitay2
     */
    public Long getKaburihidarisitay2() {
        return kaburihidarisitay2;
    }

    /**
     * 被り量左下Y2
     * @param kaburihidarisitay2 the kaburihidarisitay2 to set
     */
    public void setKaburihidarisitay2(Long kaburihidarisitay2) {
        this.kaburihidarisitay2 = kaburihidarisitay2;
    }

    /**
     * 被り量中央X1
     * @return the kaburihidaricenterx1
     */
    public Long getKaburihidaricenterx1() {
        return kaburihidaricenterx1;
    }

    /**
     * 被り量中央X1
     * @param kaburihidaricenterx1 the kaburihidaricenterx1 to set
     */
    public void setKaburihidaricenterx1(Long kaburihidaricenterx1) {
        this.kaburihidaricenterx1 = kaburihidaricenterx1;
    }

    /**
     * 被り量中央X2
     * @return the kaburihidaricenterx2
     */
    public Long getKaburihidaricenterx2() {
        return kaburihidaricenterx2;
    }

    /**
     * 被り量中央X2
     * @param kaburihidaricenterx2 the kaburihidaricenterx2 to set
     */
    public void setKaburihidaricenterx2(Long kaburihidaricenterx2) {
        this.kaburihidaricenterx2 = kaburihidaricenterx2;
    }

    /**
     * 被り量中央Y1
     * @return the kaburihidaricentery1
     */
    public Long getKaburihidaricentery1() {
        return kaburihidaricentery1;
    }

    /**
     * 被り量中央Y1
     * @param kaburihidaricentery1 the kaburihidaricentery1 to set
     */
    public void setKaburihidaricentery1(Long kaburihidaricentery1) {
        this.kaburihidaricentery1 = kaburihidaricentery1;
    }

    /**
     * 被り量中央Y2
     * @return the kaburihidaricentery2
     */
    public Long getKaburihidaricentery2() {
        return kaburihidaricentery2;
    }

    /**
     * 被り量中央Y2
     * @param kaburihidaricentery2 the kaburihidaricentery2 to set
     */
    public void setKaburihidaricentery2(Long kaburihidaricentery2) {
        this.kaburihidaricentery2 = kaburihidaricentery2;
    }

    /**
     * 被り量右上X1
     * @return the kaburimigiuex1
     */
    public Long getKaburimigiuex1() {
        return kaburimigiuex1;
    }

    /**
     * 被り量右上X1
     * @param kaburimigiuex1 the kaburimigiuex1 to set
     */
    public void setKaburimigiuex1(Long kaburimigiuex1) {
        this.kaburimigiuex1 = kaburimigiuex1;
    }

    /**
     * 被り量右上X2
     * @return the kaburimigiuex2
     */
    public Long getKaburimigiuex2() {
        return kaburimigiuex2;
    }

    /**
     * 被り量右上X2
     * @param kaburimigiuex2 the kaburimigiuex2 to set
     */
    public void setKaburimigiuex2(Long kaburimigiuex2) {
        this.kaburimigiuex2 = kaburimigiuex2;
    }

    /**
     * 被り量右上Y1
     * @return the kaburimigiuey1
     */
    public Long getKaburimigiuey1() {
        return kaburimigiuey1;
    }

    /**
     * 被り量右上Y1
     * @param kaburimigiuey1 the kaburimigiuey1 to set
     */
    public void setKaburimigiuey1(Long kaburimigiuey1) {
        this.kaburimigiuey1 = kaburimigiuey1;
    }

    /**
     * 被り量右上Y2
     * @return the kaburimigiuey2
     */
    public Long getKaburimigiuey2() {
        return kaburimigiuey2;
    }

    /**
     * 被り量右上Y2
     * @param kaburimigiuey2 the kaburimigiuey2 to set
     */
    public void setKaburimigiuey2(Long kaburimigiuey2) {
        this.kaburimigiuey2 = kaburimigiuey2;
    }

    /**
     * 被り量右下X1
     * @return the kaburimigisitax1
     */
    public Long getKaburimigisitax1() {
        return kaburimigisitax1;
    }

    /**
     * 被り量右下X1
     * @param kaburimigisitax1 the kaburimigisitax1 to set
     */
    public void setKaburimigisitax1(Long kaburimigisitax1) {
        this.kaburimigisitax1 = kaburimigisitax1;
    }

    /**
     * 被り量右下X2
     * @return the kaburimigisitax2
     */
    public Long getKaburimigisitax2() {
        return kaburimigisitax2;
    }

    /**
     * 被り量右下X2
     * @param kaburimigisitax2 the kaburimigisitax2 to set
     */
    public void setKaburimigisitax2(Long kaburimigisitax2) {
        this.kaburimigisitax2 = kaburimigisitax2;
    }

    /**
     * 被り量右下Y1
     * @return the kaburimigisitay1
     */
    public Long getKaburimigisitay1() {
        return kaburimigisitay1;
    }

    /**
     * 被り量右下Y1
     * @param kaburimigisitay1 the kaburimigisitay1 to set
     */
    public void setKaburimigisitay1(Long kaburimigisitay1) {
        this.kaburimigisitay1 = kaburimigisitay1;
    }

    /**
     * 被り量右下Y2
     * @return the kaburimigisitay2
     */
    public Long getKaburimigisitay2() {
        return kaburimigisitay2;
    }

    /**
     * 被り量右下Y2
     * @param kaburimigisitay2 the kaburimigisitay2 to set
     */
    public void setKaburimigisitay2(Long kaburimigisitay2) {
        this.kaburimigisitay2 = kaburimigisitay2;
    }
}
