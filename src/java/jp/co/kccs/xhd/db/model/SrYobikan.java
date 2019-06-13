/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/06/06<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 1次脱脂(Air_窒素)のモデルクラスです。
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2019/06/06
 */
public class SrYobikan {

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
     * 処理数
     */
    private Integer kosuu;

    /**
     * 開始日時
     */
    private Timestamp kaisinichiji;

    /**
     * 終了予定日時
     */
    private Timestamp syuryoyoteinichiji;

    /**
     * 終了日時
     */
    private Timestamp syuryonichiji;

    /**
     * 予備乾燥号機ｺｰﾄﾞ
     */
    private String yobikangoki;

    /**
     * ｾｯﾀ枚数
     */
    private Integer sayasuu;

    /**
     * 予備乾燥時間
     */
    private BigDecimal yobikanjikan;

    /**
     * 予備乾燥温度
     */
    private Integer yobikanondo;

    /**
     * ﾋﾟｰｸ時間
     */
    private Integer peakjikan;

    /**
     * 開始担当者ｺｰﾄﾞ
     */
    private String kaisitantosya;

    /**
     * 終了担当者ｺｰﾄﾞ
     */
    private String syuryotantosya;

    /**
     * 実績No
     */
    private Integer jissekino;

    /**
     * 備考1
     */
    private String biko1;

    /**
     * 備考2
     */
    private String biko2;

    /**
     * 備考3
     */
    private String biko3;

    /**
     * 備考4
     */
    private String biko4;

    /**
     * 備考5
     */
    private String biko5;

    /**
     * 登録日時
     */
    private Timestamp torokunichiji;

    /**
     * 更新日時
     */
    private Timestamp kosinnichiji;

    /**
     * 二次開始日時
     */
    private Timestamp nijikaishinichiji;

    /**
     * 二次号機ｺｰﾄﾞ
     */
    private String nijigoki;

    /**
     * プログラムＮｏ．
     */
    private Integer programno;

    /**
     * 投入セッタ枚数
     */
    private Integer tounyusettersuu;

    /**
     * 開始確認者
     */
    private String startkakuninsyacode;

    /**
     * 回収セッタ枚数
     */
    private Integer kaisyusettersuu;

    /**
     * 脱脂種類
     */
    private String dasshisyurui;

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
     * 処理数
     *
     * @return the kosuu
     */
    public Integer getKosuu() {
        return kosuu;
    }

    /**
     * 処理数
     *
     * @param kosuu the kosuu to set
     */
    public void setKosuu(Integer kosuu) {
        this.kosuu = kosuu;
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
     * 終了予定日時
     *
     * @return the syuryoyoteinichiji
     */
    public Timestamp getSyuryoyoteinichiji() {
        return syuryoyoteinichiji;
    }

    /**
     * 終了予定日時
     *
     * @param syuryoyoteinichiji the syuryoyoteinichiji to set
     */
    public void setSyuryoyoteinichiji(Timestamp syuryoyoteinichiji) {
        this.syuryoyoteinichiji = syuryoyoteinichiji;
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
     * 予備乾燥号機ｺｰﾄﾞ
     *
     * @return the yobikangoki
     */
    public String getYobikangoki() {
        return yobikangoki;
    }

    /**
     * 予備乾燥号機ｺｰﾄﾞ
     *
     * @param yobikangoki the yobikangoki to set
     */
    public void setYobikangoki(String yobikangoki) {
        this.yobikangoki = yobikangoki;
    }

    /**
     * ｾｯﾀ枚数
     *
     * @return the sayasuu
     */
    public Integer getSayasuu() {
        return sayasuu;
    }

    /**
     * ｾｯﾀ枚数
     *
     * @param sayasuu the sayasuu to set
     */
    public void setSayasuu(Integer sayasuu) {
        this.sayasuu = sayasuu;
    }

    /**
     * 予備乾燥時間
     *
     * @return the yobikanjikan
     */
    public BigDecimal getYobikanjikan() {
        return yobikanjikan;
    }

    /**
     * 予備乾燥時間
     *
     * @param yobikanjikan the yobikanjikan to set
     */
    public void setYobikanjikan(BigDecimal yobikanjikan) {
        this.yobikanjikan = yobikanjikan;
    }

    /**
     * 予備乾燥温度
     *
     * @return the yobikanondo
     */
    public Integer getYobikanondo() {
        return yobikanondo;
    }

    /**
     * 予備乾燥温度
     *
     * @param yobikanondo the yobikanondo to set
     */
    public void setYobikanondo(Integer yobikanondo) {
        this.yobikanondo = yobikanondo;
    }

    /**
     * ﾋﾟｰｸ時間
     *
     * @return the peakjikan
     */
    public Integer getPeakjikan() {
        return peakjikan;
    }

    /**
     * ﾋﾟｰｸ時間
     *
     * @param peakjikan the peakjikan to set
     */
    public void setPeakjikan(Integer peakjikan) {
        this.peakjikan = peakjikan;
    }

    /**
     * 開始担当者ｺｰﾄﾞ
     *
     * @return the kaisitantosya
     */
    public String getKaisitantosya() {
        return kaisitantosya;
    }

    /**
     * 開始担当者ｺｰﾄﾞ
     *
     * @param kaisitantosya the kaisitantosya to set
     */
    public void setKaisitantosya(String kaisitantosya) {
        this.kaisitantosya = kaisitantosya;
    }

    /**
     * 終了担当者ｺｰﾄﾞ
     *
     * @return the syuryotantosya
     */
    public String getSyuryotantosya() {
        return syuryotantosya;
    }

    /**
     * 終了担当者ｺｰﾄﾞ
     *
     * @param syuryotantosya the syuryotantosya to set
     */
    public void setSyuryotantosya(String syuryotantosya) {
        this.syuryotantosya = syuryotantosya;
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
     * 備考3
     *
     * @return the biko3
     */
    public String getBiko3() {
        return biko3;
    }

    /**
     * 備考3
     *
     * @param biko3 the biko3 to set
     */
    public void setBiko3(String biko3) {
        this.biko3 = biko3;
    }

    /**
     * 備考4
     *
     * @return the biko4
     */
    public String getBiko4() {
        return biko4;
    }

    /**
     * 備考4
     *
     * @param biko4 the biko4 to set
     */
    public void setBiko4(String biko4) {
        this.biko4 = biko4;
    }

    /**
     * 備考5
     *
     * @return the biko5
     */
    public String getBiko5() {
        return biko5;
    }

    /**
     * 備考5
     *
     * @param biko5 the biko5 to set
     */
    public void setBiko5(String biko5) {
        this.biko5 = biko5;
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
     * 二次開始日時
     *
     * @return the nijikaishinichiji
     */
    public Timestamp getNijikaishinichiji() {
        return nijikaishinichiji;
    }

    /**
     * 二次開始日時
     *
     * @param nijikaishinichiji the nijikaishinichiji to set
     */
    public void setNijikaishinichiji(Timestamp nijikaishinichiji) {
        this.nijikaishinichiji = nijikaishinichiji;
    }

    /**
     * 二次号機ｺｰﾄﾞ
     *
     * @return the nijigoki
     */
    public String getNijigoki() {
        return nijigoki;
    }

    /**
     * 二次号機ｺｰﾄﾞ
     *
     * @param nijigoki the nijigoki to set
     */
    public void setNijigoki(String nijigoki) {
        this.nijigoki = nijigoki;
    }

    /**
     * プログラムＮｏ．
     *
     * @return the programno
     */
    public Integer getProgramno() {
        return programno;
    }

    /**
     * プログラムＮｏ．
     *
     * @param programno the programno to set
     */
    public void setProgramno(Integer programno) {
        this.programno = programno;
    }

    /**
     * 投入セッタ枚数
     *
     * @return the tounyusettersuu
     */
    public Integer getTounyusettersuu() {
        return tounyusettersuu;
    }

    /**
     * 投入セッタ枚数
     *
     * @param tounyusettersuu the tounyusettersuu to set
     */
    public void setTounyusettersuu(Integer tounyusettersuu) {
        this.tounyusettersuu = tounyusettersuu;
    }

    /**
     * 開始確認者
     *
     * @return the startkakuninsyacode
     */
    public String getStartkakuninsyacode() {
        return startkakuninsyacode;
    }

    /**
     * 開始確認者
     *
     * @param startkakuninsyacode the startkakuninsyacode to set
     */
    public void setStartkakuninsyacode(String startkakuninsyacode) {
        this.startkakuninsyacode = startkakuninsyacode;
    }

    /**
     * 回収セッタ枚数
     *
     * @return the kaisyusettersuu
     */
    public Integer getKaisyusettersuu() {
        return kaisyusettersuu;
    }

    /**
     * 回収セッタ枚数
     *
     * @param kaisyusettersuu the kaisyusettersuu to set
     */
    public void setKaisyusettersuu(Integer kaisyusettersuu) {
        this.kaisyusettersuu = kaisyusettersuu;
    }

    /**
     * 脱脂種類
     *
     * @return the dasshisyurui
     */
    public String getDasshisyurui() {
        return dasshisyurui;
    }

    /**
     * 脱脂種類
     *
     * @param dasshisyurui the dasshisyurui to set
     */
    public void setDasshisyurui(String dasshisyurui) {
        this.dasshisyurui = dasshisyurui;
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
