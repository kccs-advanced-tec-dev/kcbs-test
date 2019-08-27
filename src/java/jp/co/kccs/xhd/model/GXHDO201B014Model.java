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
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日       2019/08/02<br>
 * 計画書No     K1811-DS001<br>
 * 変更者       KCSS K.Jo<br>
 * 変更理由     新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 焼成・Air脱脂|窒素脱脂履歴検索画面のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2019/08/02
 */
public class GXHDO201B014Model implements Serializable {

    /**
     * ﾛｯﾄNo.
     */
    private String lotno = "";

    /**
     * KCPNO
     */
    private String kcpno = "";

    /**
     * 処理数
     */
    private Integer kosuu = null;

    /**
     * 開始日時
     */
    private Timestamp kaisinichiji = null;

    /**
     * 終了予定日時
     */
    private Timestamp syuryoyoteinichiji = null;

    /**
     * 終了日時
     */
    private Timestamp syuryonichiji = null;

    /**
     * 予備乾燥号機ｺｰﾄﾞ
     */
    private String yobikangoki = "";

    /**
     * ｾｯﾀ枚数
     */
    private Integer sayasuu = null;

    /**
     * 予備乾燥時間
     */
    private BigDecimal yobikanjikan = null;

    /**
     * 予備乾燥温度
     */
    private Integer yobikanondo = null;

    /**
     * ﾋﾟｰｸ時間
     */
    private BigDecimal peakjikan = null;

    /**
     * 開始担当者ｺｰﾄﾞ
     */
    private String kaisitantosya = "";

    /**
     * 終了担当者ｺｰﾄﾞ
     */
    private String syuryotantosya = "";

    /**
     * 実績no
     */
    private Integer jissekino = null;

    /**
     * 備考1
     */
    private String biko1 = "";

    /**
     * 備考2
     */
    private String biko2 = "";

    /**
     * 備考3
     */
    private String biko3 = "";

    /**
     * 備考4
     */
    private String biko4 = "";

    /**
     * 備考5
     */
    private String biko5 = "";

    /**
     * 二次開始日時
     */
    private Timestamp nijikaishinichiji = null;

    /**
     * 二次号機ｺｰﾄﾞ
     */
    private String nijigoki = "";

    /**
     * プログラムｎｏ．
     */
    private Integer programno = null;

    /**
     * 投入セッタ枚数
     */
    private Integer tounyusettersuu = null;

    /**
     * 開始確認者
     */
    private String startkakuninsyacode = "";

    /**
     * 回収セッタ枚数
     */
    private Integer kaisyusettersuu = null;

    /**
     * 脱脂種類
     */
    private String dasshisyurui = "";

    /**
     * @return lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * @param lotno セットする lotno
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * @return kcpno
     */
    public String getKcpno() {
        return kcpno;
    }

    /**
     * @param kcpno セットする kcpno
     */
    public void setKcpno(String kcpno) {
        this.kcpno = kcpno;
    }

    /**
     * @return kosuu
     */
    public Integer getKosuu() {
        return kosuu;
    }

    /**
     * @param kosuu セットする kosuu
     */
    public void setKosuu(Integer kosuu) {
        this.kosuu = kosuu;
    }

    /**
     * @return kaisinichiji
     */
    public Timestamp getKaisinichiji() {
        return kaisinichiji;
    }

    /**
     * @param kaisinichiji セットする kaisinichiji
     */
    public void setKaisinichiji(Timestamp kaisinichiji) {
        this.kaisinichiji = kaisinichiji;
    }

    /**
     * @return syuryoyoteinichiji
     */
    public Timestamp getSyuryoyoteinichiji() {
        return syuryoyoteinichiji;
    }

    /**
     * @param syuryoyoteinichiji セットする syuryoyoteinichiji
     */
    public void setSyuryoyoteinichiji(Timestamp syuryoyoteinichiji) {
        this.syuryoyoteinichiji = syuryoyoteinichiji;
    }

    /**
     * @return syuryonichiji
     */
    public Timestamp getSyuryonichiji() {
        return syuryonichiji;
    }

    /**
     * @param syuryonichiji セットする syuryonichiji
     */
    public void setSyuryonichiji(Timestamp syuryonichiji) {
        this.syuryonichiji = syuryonichiji;
    }

    /**
     * @return yobikangoki
     */
    public String getYobikangoki() {
        return yobikangoki;
    }

    /**
     * @param yobikangoki セットする yobikangoki
     */
    public void setYobikangoki(String yobikangoki) {
        this.yobikangoki = yobikangoki;
    }

    /**
     * @return sayasuu
     */
    public Integer getSayasuu() {
        return sayasuu;
    }

    /**
     * @param sayasuu セットする sayasuu
     */
    public void setSayasuu(Integer sayasuu) {
        this.sayasuu = sayasuu;
    }

    /**
     * @return yobikanjikan
     */
    public BigDecimal getYobikanjikan() {
        return yobikanjikan;
    }

    /**
     * @param yobikanjikan セットする yobikanjikan
     */
    public void setYobikanjikan(BigDecimal yobikanjikan) {
        this.yobikanjikan = yobikanjikan;
    }

    /**
     * @return yobikanondo
     */
    public Integer getYobikanondo() {
        return yobikanondo;
    }

    /**
     * @param yobikanondo セットする yobikanondo
     */
    public void setYobikanondo(Integer yobikanondo) {
        this.yobikanondo = yobikanondo;
    }

    /**
     * @return peakjikan
     */
    public BigDecimal getPeakjikan() {
        return peakjikan;
    }

    /**
     * @param peakjikan セットする peakjikan
     */
    public void setPeakjikan(BigDecimal peakjikan) {
        this.peakjikan = peakjikan;
    }

    /**
     * @return kaisitantosya
     */
    public String getKaisitantosya() {
        return kaisitantosya;
    }

    /**
     * @param kaisitantosya セットする kaisitantosya
     */
    public void setKaisitantosya(String kaisitantosya) {
        this.kaisitantosya = kaisitantosya;
    }

    /**
     * @return syuryotantosya
     */
    public String getSyuryotantosya() {
        return syuryotantosya;
    }

    /**
     * @param syuryotantosya セットする syuryotantosya
     */
    public void setSyuryotantosya(String syuryotantosya) {
        this.syuryotantosya = syuryotantosya;
    }

    /**
     * @return jissekino
     */
    public Integer getJissekino() {
        return jissekino;
    }

    /**
     * @param jissekino セットする jissekino
     */
    public void setJissekino(Integer jissekino) {
        this.jissekino = jissekino;
    }

    /**
     * @return biko1
     */
    public String getBiko1() {
        return biko1;
    }

    /**
     * @param biko1 セットする biko1
     */
    public void setBiko1(String biko1) {
        this.biko1 = biko1;
    }

    /**
     * @return biko2
     */
    public String getBiko2() {
        return biko2;
    }

    /**
     * @param biko2 セットする biko2
     */
    public void setBiko2(String biko2) {
        this.biko2 = biko2;
    }

    /**
     * @return biko3
     */
    public String getBiko3() {
        return biko3;
    }

    /**
     * @param biko3 セットする biko3
     */
    public void setBiko3(String biko3) {
        this.biko3 = biko3;
    }

    /**
     * @return biko4
     */
    public String getBiko4() {
        return biko4;
    }

    /**
     * @param biko4 セットする biko4
     */
    public void setBiko4(String biko4) {
        this.biko4 = biko4;
    }

    /**
     * @return biko5
     */
    public String getBiko5() {
        return biko5;
    }

    /**
     * @param biko5 セットする biko5
     */
    public void setBiko5(String biko5) {
        this.biko5 = biko5;
    }

    /**
     * @return nijikaishinichiji
     */
    public Timestamp getNijikaishinichiji() {
        return nijikaishinichiji;
    }

    /**
     * @param nijikaishinichiji セットする nijikaishinichiji
     */
    public void setNijikaishinichiji(Timestamp nijikaishinichiji) {
        this.nijikaishinichiji = nijikaishinichiji;
    }

    /**
     * @return nijigoki
     */
    public String getNijigoki() {
        return nijigoki;
    }

    /**
     * @param nijigoki セットする nijigoki
     */
    public void setNijigoki(String nijigoki) {
        this.nijigoki = nijigoki;
    }

    /**
     * @return programno
     */
    public Integer getProgramno() {
        return programno;
    }

    /**
     * @param programno セットする programno
     */
    public void setProgramno(Integer programno) {
        this.programno = programno;
    }

    /**
     * @return tounyusettersuu
     */
    public Integer getTounyusettersuu() {
        return tounyusettersuu;
    }

    /**
     * @param tounyusettersuu セットする tounyusettersuu
     */
    public void setTounyusettersuu(Integer tounyusettersuu) {
        this.tounyusettersuu = tounyusettersuu;
    }

    /**
     * @return startkakuninsyacode
     */
    public String getStartkakuninsyacode() {
        return startkakuninsyacode;
    }

    /**
     * @param startkakuninsyacode セットする startkakuninsyacode
     */
    public void setStartkakuninsyacode(String startkakuninsyacode) {
        this.startkakuninsyacode = startkakuninsyacode;
    }

    /**
     * @return kaisyusettersuu
     */
    public Integer getKaisyusettersuu() {
        return kaisyusettersuu;
    }

    /**
     * @param kaisyusettersuu セットする kaisyusettersuu
     */
    public void setKaisyusettersuu(Integer kaisyusettersuu) {
        this.kaisyusettersuu = kaisyusettersuu;
    }

    /**
     * @return dasshisyurui
     */
    public String getDasshisyurui() {
        return dasshisyurui;
    }

    /**
     * @param dasshisyurui セットする dasshisyurui
     */
    public void setDasshisyurui(String dasshisyurui) {
        this.dasshisyurui = dasshisyurui;
    }
}