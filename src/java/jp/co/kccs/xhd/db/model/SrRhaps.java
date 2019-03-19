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
 * 変更日	2019/03/15<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 印刷積層RHAPSのモデルクラスです。
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2019/03/15
 */
public class SrRhaps {

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
     * 開始日時
     */
    private Timestamp kaisinichiji;

    /**
     * 終了日時
     */
    private Timestamp syuryonichiji;

    /**
     * 端子ﾃｰﾌﾟ種類
     */
    private String ttapesyurui;

    /**
     * 端子ﾃｰﾌﾟﾛｯﾄNo
     */
    private String ttapelotno;

    /**
     * 端子ﾃｰﾌﾟｽﾘｯﾌﾟ記号
     */
    private String tTapeSlipKigo;

    /**
     * 端子ﾃｰﾌﾟﾛｰﾙNo1
     */
    private String tTapeRollNo1;

    /**
     * 端子ﾃｰﾌﾟﾛｰﾙNo2
     */
    private String tTapeRollNo2;

    /**
     * 端子ﾃｰﾌﾟﾛｰﾙNo3
     */
    private String tTapeRollNo3;

    /**
     * 端子ﾃｰﾌﾟﾛｰﾙNo4
     */
    private String tTapeRollNo4;

    /**
     * 端子ﾃｰﾌﾟﾛｰﾙNo5
     */
    private String tTapeRollNo5;

    /**
     * 端子原料記号
     */
    private String tgenryokigo;

    /**
     * 下端子仕様
     */
    private String stsiyo;

    /**
     * 電極積層仕様
     */
    private Integer esekisosiyo;

    /**
     * 電極ﾃｰﾌﾟ種類
     */
    private String etapesyurui;

    /**
     * 電極ﾃｰﾌﾟ原料ﾛｯﾄ
     */
    private String etapeglot;

    /**
     * 電極ﾃｰﾌﾟﾛｯﾄ
     */
    private String etapelot;

    /**
     * 電極ﾃｰﾌﾟｽﾘｯﾌﾟ記号
     */
    private String eTapeSlipKigo;

    /**
     * 電極ﾃｰﾌﾟﾛｰﾙNo1
     */
    private String eTapeRollNo1;

    /**
     * 電極ﾃｰﾌﾟﾛｰﾙNo2
     */
    private String eTapeRollNo2;

    /**
     * 電極ﾃｰﾌﾟﾛｰﾙNo3
     */
    private String eTapeRollNo3;

    /**
     * 電極ﾃｰﾌﾟﾛｰﾙNo4
     */
    private String eTapeRollNo4;

    /**
     * 電極ﾃｰﾌﾟﾛｰﾙNo5
     */
    private String eTapeRollNo5;

    /**
     * 積層ﾌﾟﾚｽ通電時間
     */
    private BigDecimal sptudenjikan;

    /**
     * 積層加圧力
     */
    private BigDecimal skaaturyoku;

    /**
     * 瞬時加熱ﾍｯﾄﾞNo
     */
    private String skheadno;

    /**
     * SUS板使用回数
     */
    private Long susskaisuu;

    /**
     * 電極誘電体ﾍﾟｰｽﾄ名
     */
    private String ecpastemei;

    /**
     * 電極ﾍﾟｰｽﾄﾛｯﾄNo
     */
    private String epastelotno;

    /**
     * 電極ﾍﾟｰｽﾄ粘度
     */
    private BigDecimal epastenendo;

    /**
     * 電極ﾍﾟｰｽﾄ温度
     */
    private BigDecimal epasteondo;

    /**
     * 電極製版名
     */
    private String eseihanmei;

    /**
     * 電極製版No
     */
    private String eseihanno;

    /**
     * 電極製版枚数
     */
    private Integer eseimaisuu;

    /**
     * 電極ｸﾘｱﾗﾝｽ
     */
    private BigDecimal eclearance;

    /**
     * 電極差圧
     */
    private BigDecimal esaatu;

    /**
     * 電極ｽｷｰｼﾞNo
     */
    private String eskeegeno;

    /**
     * 電極ｽｷｰｼﾞ枚数
     */
    private Integer eskmaisuu;

    /**
     * 電極ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
     */
    private Integer eskspeed;

    /**
     * 電極ｽｸﾚｯﾊﾟｸﾘｱﾗﾝｽ
     */
    private BigDecimal escclearance;

    /**
     * 電極ｽｷｰｼﾞ下降待ち時間
     */
    private BigDecimal eskkmjikan;

    /**
     * 電極L/Dｽﾀｰﾄ時
     */
    private BigDecimal eldstart;

    /**
     * 電極製版面積
     */
    private BigDecimal eseimenseki;

    /**
     * 電極膜厚
     */
    private BigDecimal emakuatu;

    /**
     * 電極ｽﾗｲﾄﾞ量
     */
    private BigDecimal eslideryo;

    /**
     * 電極乾燥温度
     */
    private Integer ekansoondo;

    /**
     * 電極乾燥時間
     */
    private Integer ekansojikan;

    /**
     * 誘電体ﾍﾟｰｽﾄﾛｯﾄNo
     */
    private String cpastelotno;

    /**
     * 誘電体ﾍﾟｰｽﾄ粘度
     */
    private Integer cpastenendo;

    /**
     * 誘電体ﾍﾟｰｽﾄ温度
     */
    private Integer cpasteondo;

    /**
     * 誘電体製版名
     */
    private String cseihanmei;

    /**
     * 誘電体製版No
     */
    private String cseihanno;

    /**
     * 誘電体製版枚数
     */
    private Integer cseimaisuu;

    /**
     * 誘電体ｸﾘｱﾗﾝｽ
     */
    private BigDecimal cclearance;

    /**
     * 誘電体差圧
     */
    private BigDecimal csaatu;

    /**
     * 誘電体ｽｷｰｼﾞNo
     */
    private String cskeegeno;

    /**
     * 誘電体ｽｷｰｼﾞ枚数
     */
    private Integer cskmaisuu;

    /**
     * 誘電体ｽｸﾚｯﾊﾟｸﾘｱﾗﾝｽ
     */
    private BigDecimal cscclearance;

    /**
     * 誘電体ｽｷｰｼﾞ下降待ち時間
     */
    private BigDecimal cskkmjikan;

    /**
     * 誘電体ｼﾌﾄ印刷
     */
    private Integer cshiftinsatu;

    /**
     * 誘電体L/Dｽﾀｰﾄ時
     */
    private BigDecimal cldstart;

    /**
     * 誘電体製版面積
     */
    private BigDecimal cseimenseki;

    /**
     * 誘電体ｽﾗｲﾄﾞ量
     */
    private BigDecimal cslideryo;

    /**
     * 誘電体乾燥温度
     */
    private Integer ckansoondo;

    /**
     * 誘電体乾燥時間
     */
    private Integer ckansojikan;

    /**
     * 誘電体膜厚
     */
    private BigDecimal cmakuatu;

    /**
     * 合わせ印刷ｻｲﾄﾞRZ1
     */
    private BigDecimal ainsatusrz1;

    /**
     * 合わせ印刷ｻｲﾄﾞRZ2
     */
    private BigDecimal ainsatusrz2;

    /**
     * 合わせ印刷ｻｲﾄﾞRZ3
     */
    private BigDecimal ainsatusrz3;

    /**
     * 合わせ印刷ｻｲﾄﾞRZ4
     */
    private BigDecimal ainsatusrz4;

    /**
     * 合わせ印刷ｻｲﾄﾞRZ5
     */
    private BigDecimal ainsatusrz5;

    /**
     * 合わせ印刷ｻｲﾄﾞRZAVE
     */
    private BigDecimal ainsatusrzave;

    /**
     * 上端子仕様
     */
    private BigDecimal utsiyo;

    /**
     * 上端子通電時間
     */
    private BigDecimal uttudenjikan;

    /**
     * 上端子加圧力
     */
    private BigDecimal utkaaturyoku;

    /**
     * 積層体厚み補正
     */
    private BigDecimal stahosei;

    /**
     * ﾀｰｹﾞｯﾄ印刷ｸﾘｱﾗﾝｽ
     */
    private BigDecimal ticlearance;

    /**
     * ﾀｰｹﾞｯﾄ印刷差圧
     */
    private BigDecimal tisaatu;

    /**
     * ﾀｰｹﾞｯﾄ印刷ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
     */
    private Integer tiskspeed;

    /**
     * 初層左上X1
     */
    private Integer fsthux1;

    /**
     * 初層左上X2
     */
    private Integer fsthux2;

    /**
     * 初層左上Y1
     */
    private Integer fsthuy1;

    /**
     * 初層左上Y2
     */
    private Integer fsthuy2;

    /**
     * 初層左下X1
     */
    private Integer fsthsx1;

    /**
     * 初層左下X2
     */
    private Integer fsthsx2;

    /**
     * 初層左下Y1
     */
    private Integer fsthsy1;

    /**
     * 初層左下Y2
     */
    private Integer fsthsy2;

    /**
     * 初層中央X1
     */
    private Integer fstcx1;

    /**
     * 初層中央X2
     */
    private Integer fstcx2;

    /**
     * 初層中央Y1
     */
    private Integer fstcy1;

    /**
     * 初層中央Y2
     */
    private Integer fstcy2;

    /**
     * 初層右上X1
     */
    private Integer fstmux1;

    /**
     * 初層右上X2
     */
    private Integer fstmux2;

    /**
     * 初層右上Y1
     */
    private Integer fstmuy1;

    /**
     * 初層右上Y2
     */
    private Integer fstmuy2;

    /**
     * 初層右下X1
     */
    private Integer fstmsx1;

    /**
     * 初層右下X2
     */
    private Integer fstmsx2;

    /**
     * 初層右下Y1
     */
    private Integer fstmsy1;

    /**
     * 初層右下Y2
     */
    private Integer fstmsy2;

    /**
     * 最終層左上X1
     */
    private Integer lsthux1;

    /**
     * 最終層左上X2
     */
    private Integer lsthux2;

    /**
     * 最終層左上Y1
     */
    private Integer lsthuy1;

    /**
     * 最終層左上Y2
     */
    private Integer lsthuy2;

    /**
     * 最終層左下X1
     */
    private Integer lsthsx1;

    /**
     * 最終層左下X2
     */
    private Integer lsthsx2;

    /**
     * 最終層左下Y1
     */
    private Integer lsthsy1;

    /**
     * 最終層左下Y2
     */
    private Integer lsthsy2;

    /**
     * 最終層中央X1
     */
    private Integer lstcx1;

    /**
     * 最終層中央X2
     */
    private Integer lstcx2;

    /**
     * 最終層中央Y1
     */
    private Integer lstcy1;

    /**
     * 最終層中央Y2
     */
    private Integer lstcy2;

    /**
     * 最終層右上X1
     */
    private Integer lstmux1;

    /**
     * 最終層右上X2
     */
    private Integer lstmux2;

    /**
     * 最終層右上Y1
     */
    private Integer lstmuy1;

    /**
     * 最終層右上Y2
     */
    private Integer lstmuy2;

    /**
     * 最終層右下X1
     */
    private Integer lstmsx1;

    /**
     * 最終層右下X2
     */
    private Integer lstmsx2;

    /**
     * 最終層右下Y1
     */
    private Integer lstmsy1;

    /**
     * 最終層右下Y2
     */
    private Integer lstmsy2;

    /**
     * 備考1
     */
    private String biko1;

    /**
     * 備考2
     */
    private String biko2;

    /**
     * 登録日時
     */
    private Timestamp torokunichiji;

    /**
     * 更新日時
     */
    private Timestamp kosinnichiji;

    /**
     * 号機ｺｰﾄﾞ
     */
    private String goki;

    /**
     * 特別端子枚数上
     */
    private Integer ttansisuuu;

    /**
     * 特別端子枚数下
     */
    private Integer ttansisuus;

    /**
     * 瞬時加熱時間
     */
    private BigDecimal shunkankanetsujikan;

    /**
     * PETﾌｨﾙﾑ種類
     */
    private String petfilmsyurui;

    /**
     * 加圧力
     */
    private Integer kaaturyoku;

    /**
     * 外観確認
     */
    private Integer gaikankakunin1;

    /**
     * 積層上昇速切替位置
     */
    private BigDecimal sekijousyousokuichi;

    /**
     * 積層下降速切替位置
     */
    private BigDecimal sekikakousokuichi;

    /**
     * 加圧時間
     */
    private BigDecimal kaatujikan;

    /**
     * ﾃｰﾌﾟ搬送送りﾋﾟｯﾁ
     */
    private Integer tapehansoupitch;

    /**
     * ﾃｰﾌﾟ搬送目視確認
     */
    private Integer tapehansoukakunin;

    /**
     * 電極膜厚設定
     */
    private BigDecimal dmakuatsusettei;

    /**
     * 電極熱風風量
     */
    private Integer dneppufuryou;

    /**
     * 電極膜厚AVE
     */
    private BigDecimal dmakuatsuave;

    /**
     * 電極膜厚MAX
     */
    private BigDecimal dmakuatsumax;

    /**
     * 電極膜厚MIN
     */
    private BigDecimal dmakuatsumin;

    /**
     * にじみ量測定(ﾊﾟﾀｰﾝ間距離)
     */
    private Integer nijimisokuteiptn;

    /**
     * 印刷ｻﾝﾌﾟﾙ外観確認
     */
    private Integer prnsamplegaikan;

    /**
     * 印刷位置余白長さ
     */
    private Integer prnichiyohakunagasa;

    /**
     * 誘電体ﾃｰﾌﾞﾙｸﾘｱﾗﾝｽ
     */
    private BigDecimal ctableclearance;

    /**
     * 誘電体膜厚設定
     */
    private BigDecimal cmakuatsusettei;

    /**
     * 誘電体ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
     */
    private Integer cskspeed;

    /**
     * 誘電体熱風風量
     */
    private Integer cneppufuryou;

    /**
     * 被り量測定
     */
    private Integer kaburiryou;

    /**
     * 積層中外観
     */
    private Integer sgaikan;

    /**
     * にじみ量測定(積層後)
     */
    private Integer nijimisokuteisekisougo;

    /**
     * 積層品外観
     */
    private Integer sekisouhingaikan;

    /**
     * 積層ｽﾞﾚﾁｪｯｸ
     */
    private Integer sekisouzure;

    /**
     * 上端子上昇速切替位置
     */
    private BigDecimal uejousyousokuichi;

    /**
     * 下端子下降速切替位置
     */
    private BigDecimal sitakakousokuichi;

    /**
     * ﾀｰｹﾞｯﾄｲﾝｸ種類
     */
    private String tinkshuryui;

    /**
     * ﾀｰｹﾞｯﾄｲﾝｸLOT
     */
    private String tinklot;

    /**
     * ﾀｰｹﾞｯﾄ印刷外観
     */
    private Integer tgaikan;

    /**
     * 印刷積層開始担当者
     */
    private String starttantou;

    /**
     * 印刷積層終了担当者
     */
    private String endtantou;

    /**
     * ﾀｰｹﾞｯﾄ印刷終了日
     */
    private String tendday;

    /**
     * ﾀｰｹﾞｯﾄ印刷担当者
     */
    private String tendtantou;

    /**
     * 処理ｾｯﾄ数
     */
    private Integer syorisetsuu;

    /**
     * 良品ｾｯﾄ数
     */
    private Integer ryouhinsetsuu;

    /**
     * ﾍｯﾄﾞ交換者
     */
    private String headkoukansha;

    /**
     * 積層条件者
     */
    private String sekisoujoukensha;

    /**
     * E製版ｾｯﾄ者
     */
    private String eseihansetsha;

    /**
     * C製版ｾｯﾄ者
     */
    private String cseihansetsha;

    /**
     * 段差測定者
     */
    private String dansasokuteisha;

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
     * 終了日時
     *
     * @return the syuryonichiji
     */
    public Timestamp getSyuryonichiji() {
        return syuryonichiji;
    }

    /**
     * 終了日時
     *
     * @param syuryonichiji the syuryonichiji to set
     */
    public void setSyuryonichiji(Timestamp syuryonichiji) {
        this.syuryonichiji = syuryonichiji;
    }

    /**
     * 端子ﾃｰﾌﾟ種類
     *
     * @return the ttapesyurui
     */
    public String getTtapesyurui() {
        return ttapesyurui;
    }

    /**
     * 端子ﾃｰﾌﾟ種類
     *
     * @param ttapesyurui the ttapesyurui to set
     */
    public void setTtapesyurui(String ttapesyurui) {
        this.ttapesyurui = ttapesyurui;
    }

    /**
     * 端子ﾃｰﾌﾟﾛｯﾄNo
     *
     * @return the ttapelotno
     */
    public String getTtapelotno() {
        return ttapelotno;
    }

    /**
     * 端子ﾃｰﾌﾟﾛｯﾄNo
     *
     * @param ttapelotno the ttapelotno to set
     */
    public void setTtapelotno(String ttapelotno) {
        this.ttapelotno = ttapelotno;
    }

    /**
     * 端子ﾃｰﾌﾟｽﾘｯﾌﾟ記号
     *
     * @return the tTapeSlipKigo
     */
    public String gettTapeSlipKigo() {
        return tTapeSlipKigo;
    }

    /**
     * 端子ﾃｰﾌﾟｽﾘｯﾌﾟ記号
     *
     * @param tTapeSlipKigo the tTapeSlipKigo to set
     */
    public void settTapeSlipKigo(String tTapeSlipKigo) {
        this.tTapeSlipKigo = tTapeSlipKigo;
    }

    /**
     * 端子ﾃｰﾌﾟﾛｰﾙNo1
     *
     * @return the tTapeRollNo1
     */
    public String gettTapeRollNo1() {
        return tTapeRollNo1;
    }

    /**
     * 端子ﾃｰﾌﾟﾛｰﾙNo1
     *
     * @param tTapeRollNo1 the tTapeRollNo1 to set
     */
    public void settTapeRollNo1(String tTapeRollNo1) {
        this.tTapeRollNo1 = tTapeRollNo1;
    }

    /**
     * 端子ﾃｰﾌﾟﾛｰﾙNo2
     *
     * @return the tTapeRollNo2
     */
    public String gettTapeRollNo2() {
        return tTapeRollNo2;
    }

    /**
     * 端子ﾃｰﾌﾟﾛｰﾙNo2
     *
     * @param tTapeRollNo2 the tTapeRollNo2 to set
     */
    public void settTapeRollNo2(String tTapeRollNo2) {
        this.tTapeRollNo2 = tTapeRollNo2;
    }

    /**
     * 端子ﾃｰﾌﾟﾛｰﾙNo3
     *
     * @return the tTapeRollNo3
     */
    public String gettTapeRollNo3() {
        return tTapeRollNo3;
    }

    /**
     * 端子ﾃｰﾌﾟﾛｰﾙNo3
     *
     * @param tTapeRollNo3 the tTapeRollNo3 to set
     */
    public void settTapeRollNo3(String tTapeRollNo3) {
        this.tTapeRollNo3 = tTapeRollNo3;
    }

    /**
     * 端子ﾃｰﾌﾟﾛｰﾙNo4
     *
     * @return the tTapeRollNo4
     */
    public String gettTapeRollNo4() {
        return tTapeRollNo4;
    }

    /**
     * 端子ﾃｰﾌﾟﾛｰﾙNo4
     *
     * @param tTapeRollNo4 the tTapeRollNo4 to set
     */
    public void settTapeRollNo4(String tTapeRollNo4) {
        this.tTapeRollNo4 = tTapeRollNo4;
    }

    /**
     * 端子ﾃｰﾌﾟﾛｰﾙNo5
     *
     * @return the tTapeRollNo5
     */
    public String gettTapeRollNo5() {
        return tTapeRollNo5;
    }

    /**
     * 端子ﾃｰﾌﾟﾛｰﾙNo5
     *
     * @param tTapeRollNo5 the tTapeRollNo5 to set
     */
    public void settTapeRollNo5(String tTapeRollNo5) {
        this.tTapeRollNo5 = tTapeRollNo5;
    }

    /**
     * 端子原料記号
     *
     * @return the tgenryokigo
     */
    public String getTgenryokigo() {
        return tgenryokigo;
    }

    /**
     * 端子原料記号
     *
     * @param tgenryokigo the tgenryokigo to set
     */
    public void setTgenryokigo(String tgenryokigo) {
        this.tgenryokigo = tgenryokigo;
    }

    /**
     * 下端子仕様
     *
     * @return the stsiyo
     */
    public String getStsiyo() {
        return stsiyo;
    }

    /**
     * 下端子仕様
     *
     * @param stsiyo the stsiyo to set
     */
    public void setStsiyo(String stsiyo) {
        this.stsiyo = stsiyo;
    }

    /**
     * 電極積層仕様
     *
     * @return the esekisosiyo
     */
    public Integer getEsekisosiyo() {
        return esekisosiyo;
    }

    /**
     * 電極積層仕様
     *
     * @param esekisosiyo the esekisosiyo to set
     */
    public void setEsekisosiyo(Integer esekisosiyo) {
        this.esekisosiyo = esekisosiyo;
    }

    /**
     * 電極ﾃｰﾌﾟ種類
     *
     * @return the etapesyurui
     */
    public String getEtapesyurui() {
        return etapesyurui;
    }

    /**
     * 電極ﾃｰﾌﾟ種類
     *
     * @param etapesyurui the etapesyurui to set
     */
    public void setEtapesyurui(String etapesyurui) {
        this.etapesyurui = etapesyurui;
    }

    /**
     * 電極ﾃｰﾌﾟ原料ﾛｯﾄ
     *
     * @return the etapeglot
     */
    public String getEtapeglot() {
        return etapeglot;
    }

    /**
     * 電極ﾃｰﾌﾟ原料ﾛｯﾄ
     *
     * @param etapeglot the etapeglot to set
     */
    public void setEtapeglot(String etapeglot) {
        this.etapeglot = etapeglot;
    }

    /**
     * 電極ﾃｰﾌﾟﾛｯﾄ
     *
     * @return the etapelot
     */
    public String getEtapelot() {
        return etapelot;
    }

    /**
     * 電極ﾃｰﾌﾟﾛｯﾄ
     *
     * @param etapelot the etapelot to set
     */
    public void setEtapelot(String etapelot) {
        this.etapelot = etapelot;
    }

    /**
     * 電極ﾃｰﾌﾟｽﾘｯﾌﾟ記号
     *
     * @return the eTapeSlipKigo
     */
    public String geteTapeSlipKigo() {
        return eTapeSlipKigo;
    }

    /**
     * 電極ﾃｰﾌﾟｽﾘｯﾌﾟ記号
     *
     * @param eTapeSlipKigo the eTapeSlipKigo to set
     */
    public void seteTapeSlipKigo(String eTapeSlipKigo) {
        this.eTapeSlipKigo = eTapeSlipKigo;
    }

    /**
     * 電極ﾃｰﾌﾟﾛｰﾙNo1
     *
     * @return the eTapeRollNo1
     */
    public String geteTapeRollNo1() {
        return eTapeRollNo1;
    }

    /**
     * 電極ﾃｰﾌﾟﾛｰﾙNo1
     *
     * @param eTapeRollNo1 the eTapeRollNo1 to set
     */
    public void seteTapeRollNo1(String eTapeRollNo1) {
        this.eTapeRollNo1 = eTapeRollNo1;
    }

    /**
     * 電極ﾃｰﾌﾟﾛｰﾙNo2
     *
     * @return the eTapeRollNo2
     */
    public String geteTapeRollNo2() {
        return eTapeRollNo2;
    }

    /**
     * 電極ﾃｰﾌﾟﾛｰﾙNo2
     *
     * @param eTapeRollNo2 the eTapeRollNo2 to set
     */
    public void seteTapeRollNo2(String eTapeRollNo2) {
        this.eTapeRollNo2 = eTapeRollNo2;
    }

    /**
     * 電極ﾃｰﾌﾟﾛｰﾙNo3
     *
     * @return the eTapeRollNo3
     */
    public String geteTapeRollNo3() {
        return eTapeRollNo3;
    }

    /**
     * 電極ﾃｰﾌﾟﾛｰﾙNo3
     *
     * @param eTapeRollNo3 the eTapeRollNo3 to set
     */
    public void seteTapeRollNo3(String eTapeRollNo3) {
        this.eTapeRollNo3 = eTapeRollNo3;
    }

    /**
     * 電極ﾃｰﾌﾟﾛｰﾙNo4
     *
     * @return the eTapeRollNo4
     */
    public String geteTapeRollNo4() {
        return eTapeRollNo4;
    }

    /**
     * 電極ﾃｰﾌﾟﾛｰﾙNo4
     *
     * @param eTapeRollNo4 the eTapeRollNo4 to set
     */
    public void seteTapeRollNo4(String eTapeRollNo4) {
        this.eTapeRollNo4 = eTapeRollNo4;
    }

    /**
     * 電極ﾃｰﾌﾟﾛｰﾙNo5
     *
     * @return the eTapeRollNo5
     */
    public String geteTapeRollNo5() {
        return eTapeRollNo5;
    }

    /**
     * 電極ﾃｰﾌﾟﾛｰﾙNo5
     *
     * @param eTapeRollNo5 the eTapeRollNo5 to set
     */
    public void seteTapeRollNo5(String eTapeRollNo5) {
        this.eTapeRollNo5 = eTapeRollNo5;
    }

    /**
     * 積層ﾌﾟﾚｽ通電時間
     *
     * @return the sptudenjikan
     */
    public BigDecimal getSptudenjikan() {
        return sptudenjikan;
    }

    /**
     * 積層ﾌﾟﾚｽ通電時間
     *
     * @param sptudenjikan the sptudenjikan to set
     */
    public void setSptudenjikan(BigDecimal sptudenjikan) {
        this.sptudenjikan = sptudenjikan;
    }

    /**
     * 積層加圧力
     *
     * @return the skaaturyoku
     */
    public BigDecimal getSkaaturyoku() {
        return skaaturyoku;
    }

    /**
     * 積層加圧力
     *
     * @param skaaturyoku the skaaturyoku to set
     */
    public void setSkaaturyoku(BigDecimal skaaturyoku) {
        this.skaaturyoku = skaaturyoku;
    }

    /**
     * 瞬時加熱ﾍｯﾄﾞNo
     *
     * @return the skheadno
     */
    public String getSkheadno() {
        return skheadno;
    }

    /**
     * 瞬時加熱ﾍｯﾄﾞNo
     *
     * @param skheadno the skheadno to set
     */
    public void setSkheadno(String skheadno) {
        this.skheadno = skheadno;
    }

    /**
     * SUS板使用回数
     *
     * @return the susskaisuu
     */
    public Long getSusskaisuu() {
        return susskaisuu;
    }

    /**
     * SUS板使用回数
     *
     * @param susskaisuu the susskaisuu to set
     */
    public void setSusskaisuu(Long susskaisuu) {
        this.susskaisuu = susskaisuu;
    }

    /**
     * 電極誘電体ﾍﾟｰｽﾄ名
     *
     * @return the ecpastemei
     */
    public String getEcpastemei() {
        return ecpastemei;
    }

    /**
     * 電極誘電体ﾍﾟｰｽﾄ名
     *
     * @param ecpastemei the ecpastemei to set
     */
    public void setEcpastemei(String ecpastemei) {
        this.ecpastemei = ecpastemei;
    }

    /**
     * 電極ﾍﾟｰｽﾄﾛｯﾄNo
     *
     * @return the epastelotno
     */
    public String getEpastelotno() {
        return epastelotno;
    }

    /**
     * 電極ﾍﾟｰｽﾄﾛｯﾄNo
     *
     * @param epastelotno the epastelotno to set
     */
    public void setEpastelotno(String epastelotno) {
        this.epastelotno = epastelotno;
    }

    /**
     * 電極ﾍﾟｰｽﾄ粘度
     *
     * @return the epastenendo
     */
    public BigDecimal getEpastenendo() {
        return epastenendo;
    }

    /**
     * 電極ﾍﾟｰｽﾄ粘度
     *
     * @param epastenendo the epastenendo to set
     */
    public void setEpastenendo(BigDecimal epastenendo) {
        this.epastenendo = epastenendo;
    }

    /**
     * 電極ﾍﾟｰｽﾄ温度
     *
     * @return the epasteondo
     */
    public BigDecimal getEpasteondo() {
        return epasteondo;
    }

    /**
     * 電極ﾍﾟｰｽﾄ温度
     *
     * @param epasteondo the epasteondo to set
     */
    public void setEpasteondo(BigDecimal epasteondo) {
        this.epasteondo = epasteondo;
    }

    /**
     * 電極製版名
     *
     * @return the eseihanmei
     */
    public String getEseihanmei() {
        return eseihanmei;
    }

    /**
     * 電極製版名
     *
     * @param eseihanmei the eseihanmei to set
     */
    public void setEseihanmei(String eseihanmei) {
        this.eseihanmei = eseihanmei;
    }

    /**
     * 電極製版No
     *
     * @return the eseihanno
     */
    public String getEseihanno() {
        return eseihanno;
    }

    /**
     * 電極製版No
     *
     * @param eseihanno the eseihanno to set
     */
    public void setEseihanno(String eseihanno) {
        this.eseihanno = eseihanno;
    }

    /**
     * 電極製版枚数
     *
     * @return the eseimaisuu
     */
    public Integer getEseimaisuu() {
        return eseimaisuu;
    }

    /**
     * 電極製版枚数
     *
     * @param eseimaisuu the eseimaisuu to set
     */
    public void setEseimaisuu(Integer eseimaisuu) {
        this.eseimaisuu = eseimaisuu;
    }

    /**
     * 電極ｸﾘｱﾗﾝｽ
     *
     * @return the eclearance
     */
    public BigDecimal getEclearance() {
        return eclearance;
    }

    /**
     * 電極ｸﾘｱﾗﾝｽ
     *
     * @param eclearance the eclearance to set
     */
    public void setEclearance(BigDecimal eclearance) {
        this.eclearance = eclearance;
    }

    /**
     * 電極差圧
     *
     * @return the esaatu
     */
    public BigDecimal getEsaatu() {
        return esaatu;
    }

    /**
     * 電極差圧
     *
     * @param esaatu the esaatu to set
     */
    public void setEsaatu(BigDecimal esaatu) {
        this.esaatu = esaatu;
    }

    /**
     * 電極ｽｷｰｼﾞNo
     *
     * @return the eskeegeno
     */
    public String getEskeegeno() {
        return eskeegeno;
    }

    /**
     * 電極ｽｷｰｼﾞNo
     *
     * @param eskeegeno the eskeegeno to set
     */
    public void setEskeegeno(String eskeegeno) {
        this.eskeegeno = eskeegeno;
    }

    /**
     * 電極ｽｷｰｼﾞ枚数
     *
     * @return the eskmaisuu
     */
    public Integer getEskmaisuu() {
        return eskmaisuu;
    }

    /**
     * 電極ｽｷｰｼﾞ枚数
     *
     * @param eskmaisuu the eskmaisuu to set
     */
    public void setEskmaisuu(Integer eskmaisuu) {
        this.eskmaisuu = eskmaisuu;
    }

    /**
     * 電極ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
     *
     * @return the eskspeed
     */
    public Integer getEskspeed() {
        return eskspeed;
    }

    /**
     * 電極ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
     *
     * @param eskspeed the eskspeed to set
     */
    public void setEskspeed(Integer eskspeed) {
        this.eskspeed = eskspeed;
    }

    /**
     * 電極ｽｸﾚｯﾊﾟｸﾘｱﾗﾝｽ
     *
     * @return the escclearance
     */
    public BigDecimal getEscclearance() {
        return escclearance;
    }

    /**
     * 電極ｽｸﾚｯﾊﾟｸﾘｱﾗﾝｽ
     *
     * @param escclearance the escclearance to set
     */
    public void setEscclearance(BigDecimal escclearance) {
        this.escclearance = escclearance;
    }

    /**
     * 電極ｽｷｰｼﾞ下降待ち時間
     *
     * @return the eskkmjikan
     */
    public BigDecimal getEskkmjikan() {
        return eskkmjikan;
    }

    /**
     * 電極ｽｷｰｼﾞ下降待ち時間
     *
     * @param eskkmjikan the eskkmjikan to set
     */
    public void setEskkmjikan(BigDecimal eskkmjikan) {
        this.eskkmjikan = eskkmjikan;
    }

    /**
     * 電極L/Dｽﾀｰﾄ時
     *
     * @return the eldstart
     */
    public BigDecimal getEldstart() {
        return eldstart;
    }

    /**
     * 電極L/Dｽﾀｰﾄ時
     *
     * @param eldstart the eldstart to set
     */
    public void setEldstart(BigDecimal eldstart) {
        this.eldstart = eldstart;
    }

    /**
     * 電極製版面積
     *
     * @return the eseimenseki
     */
    public BigDecimal getEseimenseki() {
        return eseimenseki;
    }

    /**
     * 電極製版面積
     *
     * @param eseimenseki the eseimenseki to set
     */
    public void setEseimenseki(BigDecimal eseimenseki) {
        this.eseimenseki = eseimenseki;
    }

    /**
     * 電極膜厚
     *
     * @return the emakuatu
     */
    public BigDecimal getEmakuatu() {
        return emakuatu;
    }

    /**
     * 電極膜厚
     *
     * @param emakuatu the emakuatu to set
     */
    public void setEmakuatu(BigDecimal emakuatu) {
        this.emakuatu = emakuatu;
    }

    /**
     * 電極ｽﾗｲﾄﾞ量
     *
     * @return the eslideryo
     */
    public BigDecimal getEslideryo() {
        return eslideryo;
    }

    /**
     * 電極ｽﾗｲﾄﾞ量
     *
     * @param eslideryo the eslideryo to set
     */
    public void setEslideryo(BigDecimal eslideryo) {
        this.eslideryo = eslideryo;
    }

    /**
     * 電極乾燥温度
     *
     * @return the ekansoondo
     */
    public Integer getEkansoondo() {
        return ekansoondo;
    }

    /**
     * 電極乾燥温度
     *
     * @param ekansoondo the ekansoondo to set
     */
    public void setEkansoondo(Integer ekansoondo) {
        this.ekansoondo = ekansoondo;
    }

    /**
     * 電極乾燥時間
     *
     * @return the ekansojikan
     */
    public Integer getEkansojikan() {
        return ekansojikan;
    }

    /**
     * 電極乾燥時間
     *
     * @param ekansojikan the ekansojikan to set
     */
    public void setEkansojikan(Integer ekansojikan) {
        this.ekansojikan = ekansojikan;
    }

    /**
     * 誘電体ﾍﾟｰｽﾄﾛｯﾄNo
     *
     * @return the cpastelotno
     */
    public String getCpastelotno() {
        return cpastelotno;
    }

    /**
     * 誘電体ﾍﾟｰｽﾄﾛｯﾄNo
     *
     * @param cpastelotno the cpastelotno to set
     */
    public void setCpastelotno(String cpastelotno) {
        this.cpastelotno = cpastelotno;
    }

    /**
     * 誘電体ﾍﾟｰｽﾄ粘度
     *
     * @return the cpastenendo
     */
    public Integer getCpastenendo() {
        return cpastenendo;
    }

    /**
     * 誘電体ﾍﾟｰｽﾄ粘度
     *
     * @param cpastenendo the cpastenendo to set
     */
    public void setCpastenendo(Integer cpastenendo) {
        this.cpastenendo = cpastenendo;
    }

    /**
     * 誘電体ﾍﾟｰｽﾄ温度
     *
     * @return the cpasteondo
     */
    public Integer getCpasteondo() {
        return cpasteondo;
    }

    /**
     * 誘電体ﾍﾟｰｽﾄ温度
     *
     * @param cpasteondo the cpasteondo to set
     */
    public void setCpasteondo(Integer cpasteondo) {
        this.cpasteondo = cpasteondo;
    }

    /**
     * 誘電体製版名
     *
     * @return the cseihanmei
     */
    public String getCseihanmei() {
        return cseihanmei;
    }

    /**
     * 誘電体製版名
     *
     * @param cseihanmei the cseihanmei to set
     */
    public void setCseihanmei(String cseihanmei) {
        this.cseihanmei = cseihanmei;
    }

    /**
     * 誘電体製版No
     *
     * @return the cseihanno
     */
    public String getCseihanno() {
        return cseihanno;
    }

    /**
     * 誘電体製版No
     *
     * @param cseihanno the cseihanno to set
     */
    public void setCseihanno(String cseihanno) {
        this.cseihanno = cseihanno;
    }

    /**
     * 誘電体製版枚数
     *
     * @return the cseimaisuu
     */
    public Integer getCseimaisuu() {
        return cseimaisuu;
    }

    /**
     * 誘電体製版枚数
     *
     * @param cseimaisuu the cseimaisuu to set
     */
    public void setCseimaisuu(Integer cseimaisuu) {
        this.cseimaisuu = cseimaisuu;
    }

    /**
     * 誘電体ｸﾘｱﾗﾝｽ
     *
     * @return the cclearance
     */
    public BigDecimal getCclearance() {
        return cclearance;
    }

    /**
     * 誘電体ｸﾘｱﾗﾝｽ
     *
     * @param cclearance the cclearance to set
     */
    public void setCclearance(BigDecimal cclearance) {
        this.cclearance = cclearance;
    }

    /**
     * 誘電体差圧
     *
     * @return the csaatu
     */
    public BigDecimal getCsaatu() {
        return csaatu;
    }

    /**
     * 誘電体差圧
     *
     * @param csaatu the csaatu to set
     */
    public void setCsaatu(BigDecimal csaatu) {
        this.csaatu = csaatu;
    }

    /**
     * 誘電体ｽｷｰｼﾞNo
     *
     * @return the cskeegeno
     */
    public String getCskeegeno() {
        return cskeegeno;
    }

    /**
     * 誘電体ｽｷｰｼﾞNo
     *
     * @param cskeegeno the cskeegeno to set
     */
    public void setCskeegeno(String cskeegeno) {
        this.cskeegeno = cskeegeno;
    }

    /**
     * 誘電体ｽｷｰｼﾞ枚数
     *
     * @return the cskmaisuu
     */
    public Integer getCskmaisuu() {
        return cskmaisuu;
    }

    /**
     * 誘電体ｽｷｰｼﾞ枚数
     *
     * @param cskmaisuu the cskmaisuu to set
     */
    public void setCskmaisuu(Integer cskmaisuu) {
        this.cskmaisuu = cskmaisuu;
    }

    /**
     * 誘電体ｽｸﾚｯﾊﾟｸﾘｱﾗﾝｽ
     *
     * @return the cscclearance
     */
    public BigDecimal getCscclearance() {
        return cscclearance;
    }

    /**
     * 誘電体ｽｸﾚｯﾊﾟｸﾘｱﾗﾝｽ
     *
     * @param cscclearance the cscclearance to set
     */
    public void setCscclearance(BigDecimal cscclearance) {
        this.cscclearance = cscclearance;
    }

    /**
     * 誘電体ｽｷｰｼﾞ下降待ち時間
     *
     * @return the cskkmjikan
     */
    public BigDecimal getCskkmjikan() {
        return cskkmjikan;
    }

    /**
     * 誘電体ｽｷｰｼﾞ下降待ち時間
     *
     * @param cskkmjikan the cskkmjikan to set
     */
    public void setCskkmjikan(BigDecimal cskkmjikan) {
        this.cskkmjikan = cskkmjikan;
    }

    /**
     * 誘電体ｼﾌﾄ印刷
     *
     * @return the cshiftinsatu
     */
    public Integer getCshiftinsatu() {
        return cshiftinsatu;
    }

    /**
     * 誘電体ｼﾌﾄ印刷
     *
     * @param cshiftinsatu the cshiftinsatu to set
     */
    public void setCshiftinsatu(Integer cshiftinsatu) {
        this.cshiftinsatu = cshiftinsatu;
    }

    /**
     * 誘電体L/Dｽﾀｰﾄ時
     *
     * @return the cldstart
     */
    public BigDecimal getCldstart() {
        return cldstart;
    }

    /**
     * 誘電体L/Dｽﾀｰﾄ時
     *
     * @param cldstart the cldstart to set
     */
    public void setCldstart(BigDecimal cldstart) {
        this.cldstart = cldstart;
    }

    /**
     * 誘電体製版面積
     *
     * @return the cseimenseki
     */
    public BigDecimal getCseimenseki() {
        return cseimenseki;
    }

    /**
     * 誘電体製版面積
     *
     * @param cseimenseki the cseimenseki to set
     */
    public void setCseimenseki(BigDecimal cseimenseki) {
        this.cseimenseki = cseimenseki;
    }

    /**
     * 誘電体ｽﾗｲﾄﾞ量
     *
     * @return the cslideryo
     */
    public BigDecimal getCslideryo() {
        return cslideryo;
    }

    /**
     * 誘電体ｽﾗｲﾄﾞ量
     *
     * @param cslideryo the cslideryo to set
     */
    public void setCslideryo(BigDecimal cslideryo) {
        this.cslideryo = cslideryo;
    }

    /**
     * 誘電体乾燥温度
     *
     * @return the ckansoondo
     */
    public Integer getCkansoondo() {
        return ckansoondo;
    }

    /**
     * 誘電体乾燥温度
     *
     * @param ckansoondo the ckansoondo to set
     */
    public void setCkansoondo(Integer ckansoondo) {
        this.ckansoondo = ckansoondo;
    }

    /**
     * 誘電体乾燥時間
     *
     * @return the ckansojikan
     */
    public Integer getCkansojikan() {
        return ckansojikan;
    }

    /**
     * 誘電体乾燥時間
     *
     * @param ckansojikan the ckansojikan to set
     */
    public void setCkansojikan(Integer ckansojikan) {
        this.ckansojikan = ckansojikan;
    }

    /**
     * 誘電体膜厚
     *
     * @return the cmakuatu
     */
    public BigDecimal getCmakuatu() {
        return cmakuatu;
    }

    /**
     * 誘電体膜厚
     *
     * @param cmakuatu the cmakuatu to set
     */
    public void setCmakuatu(BigDecimal cmakuatu) {
        this.cmakuatu = cmakuatu;
    }

    /**
     * 合わせ印刷ｻｲﾄﾞRZ1
     *
     * @return the ainsatusrz1
     */
    public BigDecimal getAinsatusrz1() {
        return ainsatusrz1;
    }

    /**
     * 合わせ印刷ｻｲﾄﾞRZ1
     *
     * @param ainsatusrz1 the ainsatusrz1 to set
     */
    public void setAinsatusrz1(BigDecimal ainsatusrz1) {
        this.ainsatusrz1 = ainsatusrz1;
    }

    /**
     * 合わせ印刷ｻｲﾄﾞRZ2
     *
     * @return the ainsatusrz2
     */
    public BigDecimal getAinsatusrz2() {
        return ainsatusrz2;
    }

    /**
     * 合わせ印刷ｻｲﾄﾞRZ2
     *
     * @param ainsatusrz2 the ainsatusrz2 to set
     */
    public void setAinsatusrz2(BigDecimal ainsatusrz2) {
        this.ainsatusrz2 = ainsatusrz2;
    }

    /**
     * 合わせ印刷ｻｲﾄﾞRZ3
     *
     * @return the ainsatusrz3
     */
    public BigDecimal getAinsatusrz3() {
        return ainsatusrz3;
    }

    /**
     * 合わせ印刷ｻｲﾄﾞRZ3
     *
     * @param ainsatusrz3 the ainsatusrz3 to set
     */
    public void setAinsatusrz3(BigDecimal ainsatusrz3) {
        this.ainsatusrz3 = ainsatusrz3;
    }

    /**
     * 合わせ印刷ｻｲﾄﾞRZ4
     *
     * @return the ainsatusrz4
     */
    public BigDecimal getAinsatusrz4() {
        return ainsatusrz4;
    }

    /**
     * 合わせ印刷ｻｲﾄﾞRZ4
     *
     * @param ainsatusrz4 the ainsatusrz4 to set
     */
    public void setAinsatusrz4(BigDecimal ainsatusrz4) {
        this.ainsatusrz4 = ainsatusrz4;
    }

    /**
     * 合わせ印刷ｻｲﾄﾞRZ5
     *
     * @return the ainsatusrz5
     */
    public BigDecimal getAinsatusrz5() {
        return ainsatusrz5;
    }

    /**
     * 合わせ印刷ｻｲﾄﾞRZ5
     *
     * @param ainsatusrz5 the ainsatusrz5 to set
     */
    public void setAinsatusrz5(BigDecimal ainsatusrz5) {
        this.ainsatusrz5 = ainsatusrz5;
    }

    /**
     * 合わせ印刷ｻｲﾄﾞRZAVE
     *
     * @return the ainsatusrzave
     */
    public BigDecimal getAinsatusrzave() {
        return ainsatusrzave;
    }

    /**
     * 合わせ印刷ｻｲﾄﾞRZAVE
     *
     * @param ainsatusrzave the ainsatusrzave to set
     */
    public void setAinsatusrzave(BigDecimal ainsatusrzave) {
        this.ainsatusrzave = ainsatusrzave;
    }

    /**
     * 上端子仕様
     *
     * @return the utsiyo
     */
    public BigDecimal getUtsiyo() {
        return utsiyo;
    }

    /**
     * 上端子仕様
     *
     * @param utsiyo the utsiyo to set
     */
    public void setUtsiyo(BigDecimal utsiyo) {
        this.utsiyo = utsiyo;
    }

    /**
     * 上端子通電時間
     *
     * @return the uttudenjikan
     */
    public BigDecimal getUttudenjikan() {
        return uttudenjikan;
    }

    /**
     * 上端子通電時間
     *
     * @param uttudenjikan the uttudenjikan to set
     */
    public void setUttudenjikan(BigDecimal uttudenjikan) {
        this.uttudenjikan = uttudenjikan;
    }

    /**
     * 上端子加圧力
     *
     * @return the utkaaturyoku
     */
    public BigDecimal getUtkaaturyoku() {
        return utkaaturyoku;
    }

    /**
     * 上端子加圧力
     *
     * @param utkaaturyoku the utkaaturyoku to set
     */
    public void setUtkaaturyoku(BigDecimal utkaaturyoku) {
        this.utkaaturyoku = utkaaturyoku;
    }

    /**
     * 積層体厚み補正
     *
     * @return the stahosei
     */
    public BigDecimal getStahosei() {
        return stahosei;
    }

    /**
     * 積層体厚み補正
     *
     * @param stahosei the stahosei to set
     */
    public void setStahosei(BigDecimal stahosei) {
        this.stahosei = stahosei;
    }

    /**
     * ﾀｰｹﾞｯﾄ印刷ｸﾘｱﾗﾝｽ
     *
     * @return the ticlearance
     */
    public BigDecimal getTiclearance() {
        return ticlearance;
    }

    /**
     * ﾀｰｹﾞｯﾄ印刷ｸﾘｱﾗﾝｽ
     *
     * @param ticlearance the ticlearance to set
     */
    public void setTiclearance(BigDecimal ticlearance) {
        this.ticlearance = ticlearance;
    }

    /**
     * ﾀｰｹﾞｯﾄ印刷差圧
     *
     * @return the tisaatu
     */
    public BigDecimal getTisaatu() {
        return tisaatu;
    }

    /**
     * ﾀｰｹﾞｯﾄ印刷差圧
     *
     * @param tisaatu the tisaatu to set
     */
    public void setTisaatu(BigDecimal tisaatu) {
        this.tisaatu = tisaatu;
    }

    /**
     * ﾀｰｹﾞｯﾄ印刷ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
     *
     * @return the tiskspeed
     */
    public Integer getTiskspeed() {
        return tiskspeed;
    }

    /**
     * ﾀｰｹﾞｯﾄ印刷ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
     *
     * @param tiskspeed the tiskspeed to set
     */
    public void setTiskspeed(Integer tiskspeed) {
        this.tiskspeed = tiskspeed;
    }

    /**
     * 初層左上X1
     *
     * @return the fsthux1
     */
    public Integer getFsthux1() {
        return fsthux1;
    }

    /**
     * 初層左上X1
     *
     * @param fsthux1 the fsthux1 to set
     */
    public void setFsthux1(Integer fsthux1) {
        this.fsthux1 = fsthux1;
    }

    /**
     * 初層左上X2
     *
     * @return the fsthux2
     */
    public Integer getFsthux2() {
        return fsthux2;
    }

    /**
     * 初層左上X2
     *
     * @param fsthux2 the fsthux2 to set
     */
    public void setFsthux2(Integer fsthux2) {
        this.fsthux2 = fsthux2;
    }

    /**
     * 初層左上Y1
     *
     * @return the fsthuy1
     */
    public Integer getFsthuy1() {
        return fsthuy1;
    }

    /**
     * 初層左上Y1
     *
     * @param fsthuy1 the fsthuy1 to set
     */
    public void setFsthuy1(Integer fsthuy1) {
        this.fsthuy1 = fsthuy1;
    }

    /**
     * 初層左上Y2
     *
     * @return the fsthuy2
     */
    public Integer getFsthuy2() {
        return fsthuy2;
    }

    /**
     * 初層左上Y2
     *
     * @param fsthuy2 the fsthuy2 to set
     */
    public void setFsthuy2(Integer fsthuy2) {
        this.fsthuy2 = fsthuy2;
    }

    /**
     * 初層左下X1
     *
     * @return the fsthsx1
     */
    public Integer getFsthsx1() {
        return fsthsx1;
    }

    /**
     * 初層左下X1
     *
     * @param fsthsx1 the fsthsx1 to set
     */
    public void setFsthsx1(Integer fsthsx1) {
        this.fsthsx1 = fsthsx1;
    }

    /**
     * 初層左下X2
     *
     * @return the fsthsx2
     */
    public Integer getFsthsx2() {
        return fsthsx2;
    }

    /**
     * 初層左下X2
     *
     * @param fsthsx2 the fsthsx2 to set
     */
    public void setFsthsx2(Integer fsthsx2) {
        this.fsthsx2 = fsthsx2;
    }

    /**
     * 初層左下Y1
     *
     * @return the fsthsy1
     */
    public Integer getFsthsy1() {
        return fsthsy1;
    }

    /**
     * 初層左下Y1
     *
     * @param fsthsy1 the fsthsy1 to set
     */
    public void setFsthsy1(Integer fsthsy1) {
        this.fsthsy1 = fsthsy1;
    }

    /**
     * 初層左下Y2
     *
     * @return the fsthsy2
     */
    public Integer getFsthsy2() {
        return fsthsy2;
    }

    /**
     * 初層左下Y2
     *
     * @param fsthsy2 the fsthsy2 to set
     */
    public void setFsthsy2(Integer fsthsy2) {
        this.fsthsy2 = fsthsy2;
    }

    /**
     * 初層中央X1
     *
     * @return the fstcx1
     */
    public Integer getFstcx1() {
        return fstcx1;
    }

    /**
     * 初層中央X1
     *
     * @param fstcx1 the fstcx1 to set
     */
    public void setFstcx1(Integer fstcx1) {
        this.fstcx1 = fstcx1;
    }

    /**
     * 初層中央X2
     *
     * @return the fstcx2
     */
    public Integer getFstcx2() {
        return fstcx2;
    }

    /**
     * 初層中央X2
     *
     * @param fstcx2 the fstcx2 to set
     */
    public void setFstcx2(Integer fstcx2) {
        this.fstcx2 = fstcx2;
    }

    /**
     * 初層中央Y1
     *
     * @return the fstcy1
     */
    public Integer getFstcy1() {
        return fstcy1;
    }

    /**
     * 初層中央Y1
     *
     * @param fstcy1 the fstcy1 to set
     */
    public void setFstcy1(Integer fstcy1) {
        this.fstcy1 = fstcy1;
    }

    /**
     * 初層中央Y2
     *
     * @return the fstcy2
     */
    public Integer getFstcy2() {
        return fstcy2;
    }

    /**
     * 初層中央Y2
     *
     * @param fstcy2 the fstcy2 to set
     */
    public void setFstcy2(Integer fstcy2) {
        this.fstcy2 = fstcy2;
    }

    /**
     * 初層右上X1
     *
     * @return the fstmux1
     */
    public Integer getFstmux1() {
        return fstmux1;
    }

    /**
     * 初層右上X1
     *
     * @param fstmux1 the fstmux1 to set
     */
    public void setFstmux1(Integer fstmux1) {
        this.fstmux1 = fstmux1;
    }

    /**
     * 初層右上X2
     *
     * @return the fstmux2
     */
    public Integer getFstmux2() {
        return fstmux2;
    }

    /**
     * 初層右上X2
     *
     * @param fstmux2 the fstmux2 to set
     */
    public void setFstmux2(Integer fstmux2) {
        this.fstmux2 = fstmux2;
    }

    /**
     * 初層右上Y1
     *
     * @return the fstmuy1
     */
    public Integer getFstmuy1() {
        return fstmuy1;
    }

    /**
     * 初層右上Y1
     *
     * @param fstmuy1 the fstmuy1 to set
     */
    public void setFstmuy1(Integer fstmuy1) {
        this.fstmuy1 = fstmuy1;
    }

    /**
     * 初層右上Y2
     *
     * @return the fstmuy2
     */
    public Integer getFstmuy2() {
        return fstmuy2;
    }

    /**
     * 初層右上Y2
     *
     * @param fstmuy2 the fstmuy2 to set
     */
    public void setFstmuy2(Integer fstmuy2) {
        this.fstmuy2 = fstmuy2;
    }

    /**
     * 初層右下X1
     *
     * @return the fstmsx1
     */
    public Integer getFstmsx1() {
        return fstmsx1;
    }

    /**
     * 初層右下X1
     *
     * @param fstmsx1 the fstmsx1 to set
     */
    public void setFstmsx1(Integer fstmsx1) {
        this.fstmsx1 = fstmsx1;
    }

    /**
     * 初層右下X2
     *
     * @return the fstmsx2
     */
    public Integer getFstmsx2() {
        return fstmsx2;
    }

    /**
     * 初層右下X2
     *
     * @param fstmsx2 the fstmsx2 to set
     */
    public void setFstmsx2(Integer fstmsx2) {
        this.fstmsx2 = fstmsx2;
    }

    /**
     * 初層右下Y1
     *
     * @return the fstmsy1
     */
    public Integer getFstmsy1() {
        return fstmsy1;
    }

    /**
     * 初層右下Y1
     *
     * @param fstmsy1 the fstmsy1 to set
     */
    public void setFstmsy1(Integer fstmsy1) {
        this.fstmsy1 = fstmsy1;
    }

    /**
     * 初層右下Y2
     *
     * @return the fstmsy2
     */
    public Integer getFstmsy2() {
        return fstmsy2;
    }

    /**
     * 初層右下Y2
     *
     * @param fstmsy2 the fstmsy2 to set
     */
    public void setFstmsy2(Integer fstmsy2) {
        this.fstmsy2 = fstmsy2;
    }

    /**
     * 最終層左上X1
     *
     * @return the lsthux1
     */
    public Integer getLsthux1() {
        return lsthux1;
    }

    /**
     * 最終層左上X1
     *
     * @param lsthux1 the lsthux1 to set
     */
    public void setLsthux1(Integer lsthux1) {
        this.lsthux1 = lsthux1;
    }

    /**
     * 最終層左上X2
     *
     * @return the lsthux2
     */
    public Integer getLsthux2() {
        return lsthux2;
    }

    /**
     * 最終層左上X2
     *
     * @param lsthux2 the lsthux2 to set
     */
    public void setLsthux2(Integer lsthux2) {
        this.lsthux2 = lsthux2;
    }

    /**
     * 最終層左上Y1
     *
     * @return the lsthuy1
     */
    public Integer getLsthuy1() {
        return lsthuy1;
    }

    /**
     * 最終層左上Y1
     *
     * @param lsthuy1 the lsthuy1 to set
     */
    public void setLsthuy1(Integer lsthuy1) {
        this.lsthuy1 = lsthuy1;
    }

    /**
     * 最終層左上Y2
     *
     * @return the lsthuy2
     */
    public Integer getLsthuy2() {
        return lsthuy2;
    }

    /**
     * 最終層左上Y2
     *
     * @param lsthuy2 the lsthuy2 to set
     */
    public void setLsthuy2(Integer lsthuy2) {
        this.lsthuy2 = lsthuy2;
    }

    /**
     * 最終層左下X1
     *
     * @return the lsthsx1
     */
    public Integer getLsthsx1() {
        return lsthsx1;
    }

    /**
     * 最終層左下X1
     *
     * @param lsthsx1 the lsthsx1 to set
     */
    public void setLsthsx1(Integer lsthsx1) {
        this.lsthsx1 = lsthsx1;
    }

    /**
     * 最終層左下X2
     *
     * @return the lsthsx2
     */
    public Integer getLsthsx2() {
        return lsthsx2;
    }

    /**
     * 最終層左下X2
     *
     * @param lsthsx2 the lsthsx2 to set
     */
    public void setLsthsx2(Integer lsthsx2) {
        this.lsthsx2 = lsthsx2;
    }

    /**
     * 最終層左下Y1
     *
     * @return the lsthsy1
     */
    public Integer getLsthsy1() {
        return lsthsy1;
    }

    /**
     * 最終層左下Y1
     *
     * @param lsthsy1 the lsthsy1 to set
     */
    public void setLsthsy1(Integer lsthsy1) {
        this.lsthsy1 = lsthsy1;
    }

    /**
     * 最終層左下Y2
     *
     * @return the lsthsy2
     */
    public Integer getLsthsy2() {
        return lsthsy2;
    }

    /**
     * 最終層左下Y2
     *
     * @param lsthsy2 the lsthsy2 to set
     */
    public void setLsthsy2(Integer lsthsy2) {
        this.lsthsy2 = lsthsy2;
    }

    /**
     * 最終層中央X1
     *
     * @return the lstcx1
     */
    public Integer getLstcx1() {
        return lstcx1;
    }

    /**
     * 最終層中央X1
     *
     * @param lstcx1 the lstcx1 to set
     */
    public void setLstcx1(Integer lstcx1) {
        this.lstcx1 = lstcx1;
    }

    /**
     * 最終層中央X2
     *
     * @return the lstcx2
     */
    public Integer getLstcx2() {
        return lstcx2;
    }

    /**
     * 最終層中央X2
     *
     * @param lstcx2 the lstcx2 to set
     */
    public void setLstcx2(Integer lstcx2) {
        this.lstcx2 = lstcx2;
    }

    /**
     * 最終層中央Y1
     *
     * @return the lstcy1
     */
    public Integer getLstcy1() {
        return lstcy1;
    }

    /**
     * 最終層中央Y1
     *
     * @param lstcy1 the lstcy1 to set
     */
    public void setLstcy1(Integer lstcy1) {
        this.lstcy1 = lstcy1;
    }

    /**
     * 最終層中央Y2
     *
     * @return the lstcy2
     */
    public Integer getLstcy2() {
        return lstcy2;
    }

    /**
     * 最終層中央Y2
     *
     * @param lstcy2 the lstcy2 to set
     */
    public void setLstcy2(Integer lstcy2) {
        this.lstcy2 = lstcy2;
    }

    /**
     * 最終層右上X1
     *
     * @return the lstmux1
     */
    public Integer getLstmux1() {
        return lstmux1;
    }

    /**
     * 最終層右上X1
     *
     * @param lstmux1 the lstmux1 to set
     */
    public void setLstmux1(Integer lstmux1) {
        this.lstmux1 = lstmux1;
    }

    /**
     * 最終層右上X2
     *
     * @return the lstmux2
     */
    public Integer getLstmux2() {
        return lstmux2;
    }

    /**
     * 最終層右上X2
     *
     * @param lstmux2 the lstmux2 to set
     */
    public void setLstmux2(Integer lstmux2) {
        this.lstmux2 = lstmux2;
    }

    /**
     * 最終層右上Y1
     *
     * @return the lstmuy1
     */
    public Integer getLstmuy1() {
        return lstmuy1;
    }

    /**
     * 最終層右上Y1
     *
     * @param lstmuy1 the lstmuy1 to set
     */
    public void setLstmuy1(Integer lstmuy1) {
        this.lstmuy1 = lstmuy1;
    }

    /**
     * 最終層右上Y2
     *
     * @return the lstmuy2
     */
    public Integer getLstmuy2() {
        return lstmuy2;
    }

    /**
     * 最終層右上Y2
     *
     * @param lstmuy2 the lstmuy2 to set
     */
    public void setLstmuy2(Integer lstmuy2) {
        this.lstmuy2 = lstmuy2;
    }

    /**
     * 最終層右下X1
     *
     * @return the lstmsx1
     */
    public Integer getLstmsx1() {
        return lstmsx1;
    }

    /**
     * 最終層右下X1
     *
     * @param lstmsx1 the lstmsx1 to set
     */
    public void setLstmsx1(Integer lstmsx1) {
        this.lstmsx1 = lstmsx1;
    }

    /**
     * 最終層右下X2
     *
     * @return the lstmsx2
     */
    public Integer getLstmsx2() {
        return lstmsx2;
    }

    /**
     * 最終層右下X2
     *
     * @param lstmsx2 the lstmsx2 to set
     */
    public void setLstmsx2(Integer lstmsx2) {
        this.lstmsx2 = lstmsx2;
    }

    /**
     * 最終層右下Y1
     *
     * @return the lstmsy1
     */
    public Integer getLstmsy1() {
        return lstmsy1;
    }

    /**
     * 最終層右下Y1
     *
     * @param lstmsy1 the lstmsy1 to set
     */
    public void setLstmsy1(Integer lstmsy1) {
        this.lstmsy1 = lstmsy1;
    }

    /**
     * 最終層右下Y2
     *
     * @return the lstmsy2
     */
    public Integer getLstmsy2() {
        return lstmsy2;
    }

    /**
     * 最終層右下Y2
     *
     * @param lstmsy2 the lstmsy2 to set
     */
    public void setLstmsy2(Integer lstmsy2) {
        this.lstmsy2 = lstmsy2;
    }

    /**
     * 備考1
     *
     * @return the biko1
     */
    public String getBiko1() {
        return biko1;
    }

    /**
     * 備考1
     *
     * @param biko1 the biko1 to set
     */
    public void setBiko1(String biko1) {
        this.biko1 = biko1;
    }

    /**
     * 備考2
     *
     * @return the biko2
     */
    public String getBiko2() {
        return biko2;
    }

    /**
     * 備考2
     *
     * @param biko2 the biko2 to set
     */
    public void setBiko2(String biko2) {
        this.biko2 = biko2;
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
     * 号機ｺｰﾄﾞ
     *
     * @return the goki
     */
    public String getGoki() {
        return goki;
    }

    /**
     * 号機ｺｰﾄﾞ
     *
     * @param goki the goki to set
     */
    public void setGoki(String goki) {
        this.goki = goki;
    }

    /**
     * 特別端子枚数上
     *
     * @return the ttansisuuu
     */
    public Integer getTtansisuuu() {
        return ttansisuuu;
    }

    /**
     * 特別端子枚数上
     *
     * @param ttansisuuu the ttansisuuu to set
     */
    public void setTtansisuuu(Integer ttansisuuu) {
        this.ttansisuuu = ttansisuuu;
    }

    /**
     * 特別端子枚数下
     *
     * @return the ttansisuus
     */
    public Integer getTtansisuus() {
        return ttansisuus;
    }

    /**
     * 特別端子枚数下
     *
     * @param ttansisuus the ttansisuus to set
     */
    public void setTtansisuus(Integer ttansisuus) {
        this.ttansisuus = ttansisuus;
    }

    /**
     * 瞬時加熱時間
     *
     * @return the shunkankanetsujikan
     */
    public BigDecimal getShunkankanetsujikan() {
        return shunkankanetsujikan;
    }

    /**
     * 瞬時加熱時間
     *
     * @param shunkankanetsujikan the shunkankanetsujikan to set
     */
    public void setShunkankanetsujikan(BigDecimal shunkankanetsujikan) {
        this.shunkankanetsujikan = shunkankanetsujikan;
    }

    /**
     * PETﾌｨﾙﾑ種類
     *
     * @return the petfilmsyurui
     */
    public String getPetfilmsyurui() {
        return petfilmsyurui;
    }

    /**
     * PETﾌｨﾙﾑ種類
     *
     * @param petfilmsyurui the petfilmsyurui to set
     */
    public void setPetfilmsyurui(String petfilmsyurui) {
        this.petfilmsyurui = petfilmsyurui;
    }

    /**
     * 加圧力
     *
     * @return the kaaturyoku
     */
    public Integer getKaaturyoku() {
        return kaaturyoku;
    }

    /**
     * 加圧力
     *
     * @param kaaturyoku the kaaturyoku to set
     */
    public void setKaaturyoku(Integer kaaturyoku) {
        this.kaaturyoku = kaaturyoku;
    }

    /**
     * 外観確認
     *
     * @return the gaikankakunin1
     */
    public Integer getGaikankakunin1() {
        return gaikankakunin1;
    }

    /**
     * 外観確認
     *
     * @param gaikankakunin1 the gaikankakunin1 to set
     */
    public void setGaikankakunin1(Integer gaikankakunin1) {
        this.gaikankakunin1 = gaikankakunin1;
    }

    /**
     * 積層上昇速切替位置
     *
     * @return the sekijousyousokuichi
     */
    public BigDecimal getSekijousyousokuichi() {
        return sekijousyousokuichi;
    }

    /**
     * 積層上昇速切替位置
     *
     * @param sekijousyousokuichi the sekijousyousokuichi to set
     */
    public void setSekijousyousokuichi(BigDecimal sekijousyousokuichi) {
        this.sekijousyousokuichi = sekijousyousokuichi;
    }

    /**
     * 積層下降速切替位置
     *
     * @return the sekikakousokuichi
     */
    public BigDecimal getSekikakousokuichi() {
        return sekikakousokuichi;
    }

    /**
     * 積層下降速切替位置
     *
     * @param sekikakousokuichi the sekikakousokuichi to set
     */
    public void setSekikakousokuichi(BigDecimal sekikakousokuichi) {
        this.sekikakousokuichi = sekikakousokuichi;
    }

    /**
     * 加圧時間
     *
     * @return the kaatujikan
     */
    public BigDecimal getKaatujikan() {
        return kaatujikan;
    }

    /**
     * 加圧時間
     *
     * @param kaatujikan the kaatujikan to set
     */
    public void setKaatujikan(BigDecimal kaatujikan) {
        this.kaatujikan = kaatujikan;
    }

    /**
     * ﾃｰﾌﾟ搬送送りﾋﾟｯﾁ
     *
     * @return the tapehansoupitch
     */
    public Integer getTapehansoupitch() {
        return tapehansoupitch;
    }

    /**
     * ﾃｰﾌﾟ搬送送りﾋﾟｯﾁ
     *
     * @param tapehansoupitch the tapehansoupitch to set
     */
    public void setTapehansoupitch(Integer tapehansoupitch) {
        this.tapehansoupitch = tapehansoupitch;
    }

    /**
     * ﾃｰﾌﾟ搬送目視確認
     *
     * @return the tapehansoukakunin
     */
    public Integer getTapehansoukakunin() {
        return tapehansoukakunin;
    }

    /**
     * ﾃｰﾌﾟ搬送目視確認
     *
     * @param tapehansoukakunin the tapehansoukakunin to set
     */
    public void setTapehansoukakunin(Integer tapehansoukakunin) {
        this.tapehansoukakunin = tapehansoukakunin;
    }

    /**
     * 電極膜厚設定
     *
     * @return the dmakuatsusettei
     */
    public BigDecimal getDmakuatsusettei() {
        return dmakuatsusettei;
    }

    /**
     * 電極膜厚設定
     *
     * @param dmakuatsusettei the dmakuatsusettei to set
     */
    public void setDmakuatsusettei(BigDecimal dmakuatsusettei) {
        this.dmakuatsusettei = dmakuatsusettei;
    }

    /**
     * 電極熱風風量
     *
     * @return the dneppufuryou
     */
    public Integer getDneppufuryou() {
        return dneppufuryou;
    }

    /**
     * 電極熱風風量
     *
     * @param dneppufuryou the dneppufuryou to set
     */
    public void setDneppufuryou(Integer dneppufuryou) {
        this.dneppufuryou = dneppufuryou;
    }

    /**
     * 電極膜厚AVE
     *
     * @return the dmakuatsuave
     */
    public BigDecimal getDmakuatsuave() {
        return dmakuatsuave;
    }

    /**
     * 電極膜厚AVE
     *
     * @param dmakuatsuave the dmakuatsuave to set
     */
    public void setDmakuatsuave(BigDecimal dmakuatsuave) {
        this.dmakuatsuave = dmakuatsuave;
    }

    /**
     * 電極膜厚MAX
     *
     * @return the dmakuatsumax
     */
    public BigDecimal getDmakuatsumax() {
        return dmakuatsumax;
    }

    /**
     * 電極膜厚MAX
     *
     * @param dmakuatsumax the dmakuatsumax to set
     */
    public void setDmakuatsumax(BigDecimal dmakuatsumax) {
        this.dmakuatsumax = dmakuatsumax;
    }

    /**
     * 電極膜厚MIN
     *
     * @return the dmakuatsumin
     */
    public BigDecimal getDmakuatsumin() {
        return dmakuatsumin;
    }

    /**
     * 電極膜厚MIN
     *
     * @param dmakuatsumin the dmakuatsumin to set
     */
    public void setDmakuatsumin(BigDecimal dmakuatsumin) {
        this.dmakuatsumin = dmakuatsumin;
    }

    /**
     * にじみ量測定(ﾊﾟﾀｰﾝ間距離)
     *
     * @return the nijimisokuteiptn
     */
    public Integer getNijimisokuteiptn() {
        return nijimisokuteiptn;
    }

    /**
     * にじみ量測定(ﾊﾟﾀｰﾝ間距離)
     *
     * @param nijimisokuteiptn the nijimisokuteiptn to set
     */
    public void setNijimisokuteiptn(Integer nijimisokuteiptn) {
        this.nijimisokuteiptn = nijimisokuteiptn;
    }

    /**
     * 印刷ｻﾝﾌﾟﾙ外観確認
     *
     * @return the prnsamplegaikan
     */
    public Integer getPrnsamplegaikan() {
        return prnsamplegaikan;
    }

    /**
     * 印刷ｻﾝﾌﾟﾙ外観確認
     *
     * @param prnsamplegaikan the prnsamplegaikan to set
     */
    public void setPrnsamplegaikan(Integer prnsamplegaikan) {
        this.prnsamplegaikan = prnsamplegaikan;
    }

    /**
     * 印刷位置余白長さ
     *
     * @return the prnichiyohakunagasa
     */
    public Integer getPrnichiyohakunagasa() {
        return prnichiyohakunagasa;
    }

    /**
     * 印刷位置余白長さ
     *
     * @param prnichiyohakunagasa the prnichiyohakunagasa to set
     */
    public void setPrnichiyohakunagasa(Integer prnichiyohakunagasa) {
        this.prnichiyohakunagasa = prnichiyohakunagasa;
    }

    /**
     * 誘電体ﾃｰﾌﾞﾙｸﾘｱﾗﾝｽ
     *
     * @return the ctableclearance
     */
    public BigDecimal getCtableclearance() {
        return ctableclearance;
    }

    /**
     * 誘電体ﾃｰﾌﾞﾙｸﾘｱﾗﾝｽ
     *
     * @param ctableclearance the ctableclearance to set
     */
    public void setCtableclearance(BigDecimal ctableclearance) {
        this.ctableclearance = ctableclearance;
    }

    /**
     * 誘電体膜厚設定
     *
     * @return the cmakuatsusettei
     */
    public BigDecimal getCmakuatsusettei() {
        return cmakuatsusettei;
    }

    /**
     * 誘電体膜厚設定
     *
     * @param cmakuatsusettei the cmakuatsusettei to set
     */
    public void setCmakuatsusettei(BigDecimal cmakuatsusettei) {
        this.cmakuatsusettei = cmakuatsusettei;
    }

    /**
     * 誘電体ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
     *
     * @return the cskspeed
     */
    public Integer getCskspeed() {
        return cskspeed;
    }

    /**
     * 誘電体ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
     *
     * @param cskspeed the cskspeed to set
     */
    public void setCskspeed(Integer cskspeed) {
        this.cskspeed = cskspeed;
    }

    /**
     * 誘電体熱風風量
     *
     * @return the cneppufuryou
     */
    public Integer getCneppufuryou() {
        return cneppufuryou;
    }

    /**
     * 誘電体熱風風量
     *
     * @param cneppufuryou the cneppufuryou to set
     */
    public void setCneppufuryou(Integer cneppufuryou) {
        this.cneppufuryou = cneppufuryou;
    }

    /**
     * 被り量測定
     *
     * @return the kaburiryou
     */
    public Integer getKaburiryou() {
        return kaburiryou;
    }

    /**
     * 被り量測定
     *
     * @param kaburiryou the kaburiryou to set
     */
    public void setKaburiryou(Integer kaburiryou) {
        this.kaburiryou = kaburiryou;
    }

    /**
     * 積層中外観
     *
     * @return the sgaikan
     */
    public Integer getSgaikan() {
        return sgaikan;
    }

    /**
     * 積層中外観
     *
     * @param sgaikan the sgaikan to set
     */
    public void setSgaikan(Integer sgaikan) {
        this.sgaikan = sgaikan;
    }

    /**
     * にじみ量測定(積層後)
     *
     * @return the nijimisokuteisekisougo
     */
    public Integer getNijimisokuteisekisougo() {
        return nijimisokuteisekisougo;
    }

    /**
     * にじみ量測定(積層後)
     *
     * @param nijimisokuteisekisougo the nijimisokuteisekisougo to set
     */
    public void setNijimisokuteisekisougo(Integer nijimisokuteisekisougo) {
        this.nijimisokuteisekisougo = nijimisokuteisekisougo;
    }

    /**
     * 積層品外観
     *
     * @return the sekisouhingaikan
     */
    public Integer getSekisouhingaikan() {
        return sekisouhingaikan;
    }

    /**
     * 積層品外観
     *
     * @param sekisouhingaikan the sekisouhingaikan to set
     */
    public void setSekisouhingaikan(Integer sekisouhingaikan) {
        this.sekisouhingaikan = sekisouhingaikan;
    }

    /**
     * 積層ｽﾞﾚﾁｪｯｸ
     *
     * @return the sekisouzure
     */
    public Integer getSekisouzure() {
        return sekisouzure;
    }

    /**
     * 積層ｽﾞﾚﾁｪｯｸ
     *
     * @param sekisouzure the sekisouzure to set
     */
    public void setSekisouzure(Integer sekisouzure) {
        this.sekisouzure = sekisouzure;
    }

    /**
     * 上端子上昇速切替位置
     *
     * @return the uejousyousokuichi
     */
    public BigDecimal getUejousyousokuichi() {
        return uejousyousokuichi;
    }

    /**
     * 上端子上昇速切替位置
     *
     * @param uejousyousokuichi the uejousyousokuichi to set
     */
    public void setUejousyousokuichi(BigDecimal uejousyousokuichi) {
        this.uejousyousokuichi = uejousyousokuichi;
    }

    /**
     * 下端子下降速切替位置
     *
     * @return the sitakakousokuichi
     */
    public BigDecimal getSitakakousokuichi() {
        return sitakakousokuichi;
    }

    /**
     * 下端子下降速切替位置
     *
     * @param sitakakousokuichi the sitakakousokuichi to set
     */
    public void setSitakakousokuichi(BigDecimal sitakakousokuichi) {
        this.sitakakousokuichi = sitakakousokuichi;
    }

    /**
     * ﾀｰｹﾞｯﾄｲﾝｸ種類
     *
     * @return the tinkshuryui
     */
    public String getTinkshuryui() {
        return tinkshuryui;
    }

    /**
     * ﾀｰｹﾞｯﾄｲﾝｸ種類
     *
     * @param tinkshuryui the tinkshuryui to set
     */
    public void setTinkshuryui(String tinkshuryui) {
        this.tinkshuryui = tinkshuryui;
    }

    /**
     * ﾀｰｹﾞｯﾄｲﾝｸLOT
     *
     * @return the tinklot
     */
    public String getTinklot() {
        return tinklot;
    }

    /**
     * ﾀｰｹﾞｯﾄｲﾝｸLOT
     *
     * @param tinklot the tinklot to set
     */
    public void setTinklot(String tinklot) {
        this.tinklot = tinklot;
    }

    /**
     * ﾀｰｹﾞｯﾄ印刷外観
     *
     * @return the tgaikan
     */
    public Integer getTgaikan() {
        return tgaikan;
    }

    /**
     * ﾀｰｹﾞｯﾄ印刷外観
     *
     * @param tgaikan the tgaikan to set
     */
    public void setTgaikan(Integer tgaikan) {
        this.tgaikan = tgaikan;
    }

    /**
     * 印刷積層開始担当者
     *
     * @return the starttantou
     */
    public String getStarttantou() {
        return starttantou;
    }

    /**
     * 印刷積層開始担当者
     *
     * @param starttantou the starttantou to set
     */
    public void setStarttantou(String starttantou) {
        this.starttantou = starttantou;
    }

    /**
     * 印刷積層終了担当者
     *
     * @return the endtantou
     */
    public String getEndtantou() {
        return endtantou;
    }

    /**
     * 印刷積層終了担当者
     *
     * @param endtantou the endtantou to set
     */
    public void setEndtantou(String endtantou) {
        this.endtantou = endtantou;
    }

    /**
     * ﾀｰｹﾞｯﾄ印刷終了日
     *
     * @return the tendday
     */
    public String getTendday() {
        return tendday;
    }

    /**
     * ﾀｰｹﾞｯﾄ印刷終了日
     *
     * @param tendday the tendday to set
     */
    public void setTendday(String tendday) {
        this.tendday = tendday;
    }

    /**
     * ﾀｰｹﾞｯﾄ印刷担当者
     *
     * @return the tendtantou
     */
    public String getTendtantou() {
        return tendtantou;
    }

    /**
     * ﾀｰｹﾞｯﾄ印刷担当者
     *
     * @param tendtantou the tendtantou to set
     */
    public void setTendtantou(String tendtantou) {
        this.tendtantou = tendtantou;
    }

    /**
     * 処理ｾｯﾄ数
     *
     * @return the syorisetsuu
     */
    public Integer getSyorisetsuu() {
        return syorisetsuu;
    }

    /**
     * 処理ｾｯﾄ数
     *
     * @param syorisetsuu the syorisetsuu to set
     */
    public void setSyorisetsuu(Integer syorisetsuu) {
        this.syorisetsuu = syorisetsuu;
    }

    /**
     * 良品ｾｯﾄ数
     *
     * @return the ryouhinsetsuu
     */
    public Integer getRyouhinsetsuu() {
        return ryouhinsetsuu;
    }

    /**
     * 良品ｾｯﾄ数
     *
     * @param ryouhinsetsuu the ryouhinsetsuu to set
     */
    public void setRyouhinsetsuu(Integer ryouhinsetsuu) {
        this.ryouhinsetsuu = ryouhinsetsuu;
    }

    /**
     * ﾍｯﾄﾞ交換者
     *
     * @return the headkoukansha
     */
    public String getHeadkoukansha() {
        return headkoukansha;
    }

    /**
     * ﾍｯﾄﾞ交換者
     *
     * @param headkoukansha the headkoukansha to set
     */
    public void setHeadkoukansha(String headkoukansha) {
        this.headkoukansha = headkoukansha;
    }

    /**
     * 積層条件者
     *
     * @return the sekisoujoukensha
     */
    public String getSekisoujoukensha() {
        return sekisoujoukensha;
    }

    /**
     * 積層条件者
     *
     * @param sekisoujoukensha the sekisoujoukensha to set
     */
    public void setSekisoujoukensha(String sekisoujoukensha) {
        this.sekisoujoukensha = sekisoujoukensha;
    }

    /**
     * E製版ｾｯﾄ者
     *
     * @return the eseihansetsha
     */
    public String getEseihansetsha() {
        return eseihansetsha;
    }

    /**
     * E製版ｾｯﾄ者
     *
     * @param eseihansetsha the eseihansetsha to set
     */
    public void setEseihansetsha(String eseihansetsha) {
        this.eseihansetsha = eseihansetsha;
    }

    /**
     * C製版ｾｯﾄ者
     *
     * @return the cseihansetsha
     */
    public String getCseihansetsha() {
        return cseihansetsha;
    }

    /**
     * C製版ｾｯﾄ者
     *
     * @param cseihansetsha the cseihansetsha to set
     */
    public void setCseihansetsha(String cseihansetsha) {
        this.cseihansetsha = cseihansetsha;
    }

    /**
     * 段差測定者
     *
     * @return the dansasokuteisha
     */
    public String getDansasokuteisha() {
        return dansasokuteisha;
    }

    /**
     * 段差測定者
     *
     * @param dansasokuteisha the dansasokuteisha to set
     */
    public void setDansasokuteisha(String dansasokuteisha) {
        this.dansasokuteisha = dansasokuteisha;
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
