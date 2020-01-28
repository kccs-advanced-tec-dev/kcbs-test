/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2019/11/07<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	863 F.Zhang<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * sr_qasunpou1(QA寸法)のモデルクラスです。
 *
 * @author 863 F.Zhang
 * @since 2019/11/07
 */
public class SrQasunpou1 {

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
     * ﾛｯﾄﾌﾟﾚ
     */
    private String lotpre;
    /**
     * 検査日時
     */
    private Timestamp kensabi;
    /**
     * 担当者
     */
    private String tantousya;
    /**
     * 不可避
     */
    private String fukahi;
    /**
     * サンプル数
     */
    private Integer samplesuu;
    /**
     * 判定
     */
    private String hantei;
    /**
     * 備考1
     */
    private String bikou1;
    /**
     * 備考2
     */
    private String bikou2;
    /**
     * 実績No
     */
    private Integer jissekino;
    /**
     * T寸法AVE
     */
    private BigDecimal lsunave;
    /**
     * L寸法MAX
     */
    private BigDecimal lsunmax;
    /**
     * L寸法MIN
     */
    private BigDecimal lsunmin;
    /**
     * W寸法AEV
     */
    private BigDecimal wsunave;
    /**
     * W寸法MAX
     */
    private BigDecimal wsunmax;
    /**
     * W寸法MIN
     */
    private BigDecimal wsunmin;
    /**
     * T寸法AVE
     */
    private BigDecimal tsunave;
    /**
     * T寸法MAX
     */
    private BigDecimal tsunmax;
    /**
     * T寸法MIN
     */
    private BigDecimal tsunmin;
    /**
     * D寸法AVE
     */
    private BigDecimal dsunave;
    /**
     * D寸法MAX
     */
    private BigDecimal dsunmax;
    /**
     * D寸法MIN
     */
    private BigDecimal dsunmin;
    /**
     * P寸法AVE
     */
    private BigDecimal psunave;
    /**
     * *
     * P寸法MAX
     */
    private BigDecimal psunmax;
    /**
     * P寸法MIN
     */
    private BigDecimal psunmin;
    /**
     * PP寸法MIN
     */
    private BigDecimal ppsunave;
    /**
     * PP寸法MAX
     */
    private BigDecimal ppsunmax;
    /**
     * PP寸法MIN
     */
    private BigDecimal ppsunmin;
    /**
     * a3
     */
    private BigDecimal a3;
    /**
     * 備考3
     */
    private String bikou3;
    /**
     * 備考4
     */
    private String bikou4;
    /**
     * 判定内容1
     */
    private String hanteinaiyo1;
    /**
     * 判定内容2
     */
    private String hanteinaiyo2;
    /**
     * 判定内容3
     */
    private String hanteinaiyo3;
    /**
     * 判定内容4
     */
    private String hanteinaiyo4;
    /**
     * ユーザーID
     */
    private String userid;

    /**
     * @return kojyo
     */
    public String getKojyo() {
        return kojyo;
    }

    /**
     * @param kojyo セットする kojyo
     */
    public void setKojyo(String kojyo) {
        this.kojyo = kojyo;
    }

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
     * @return edaban
     */
    public String getEdaban() {
        return edaban;
    }

    /**
     * @param edaban セットする edaban
     */
    public void setEdaban(String edaban) {
        this.edaban = edaban;
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
     * @return lotpre
     */
    public String getLotpre() {
        return lotpre;
    }

    /**
     * @param lotpre セットする lotpre
     */
    public void setLotpre(String lotpre) {
        this.lotpre = lotpre;
    }

    /**
     * @return kensabi
     */
    public Timestamp getKensabi() {
        return kensabi;
    }

    /**
     * @param kensabi セットする kensabi
     */
    public void setKensabi(Timestamp kensabi) {
        this.kensabi = kensabi;
    }

    /**
     * @return tantousya
     */
    public String getTantousya() {
        return tantousya;
    }

    /**
     * @param tantousya セットする tantousya
     */
    public void setTantousya(String tantousya) {
        this.tantousya = tantousya;
    }

    /**
     * @return fukahi
     */
    public String getFukahi() {
        return fukahi;
    }

    /**
     * @param fukahi セットする fukahi
     */
    public void setFukahi(String fukahi) {
        this.fukahi = fukahi;
    }

    /**
     * @return samplesuu
     */
    public Integer getSamplesuu() {
        return samplesuu;
    }

    /**
     * @param samplesuu セットする samplesuu
     */
    public void setSamplesuu(Integer samplesuu) {
        this.samplesuu = samplesuu;
    }

    /**
     * @return hantei
     */
    public String getHantei() {
        return hantei;
    }

    /**
     * @param hantei セットする hantei
     */
    public void setHantei(String hantei) {
        this.hantei = hantei;
    }

    /**
     * @return bikou1
     */
    public String getBikou1() {
        return bikou1;
    }

    /**
     * @param bikou1 セットする bikou1
     */
    public void setBikou1(String bikou1) {
        this.bikou1 = bikou1;
    }

    /**
     * @return bikou2
     */
    public String getBikou2() {
        return bikou2;
    }

    /**
     * @param bikou2 セットする bikou2
     */
    public void setBikou2(String bikou2) {
        this.bikou2 = bikou2;
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
     * @return lsunave
     */
    public BigDecimal getLsunave() {
        return lsunave;
    }

    /**
     * @param lsunave セットする lsunave
     */
    public void setLsunave(BigDecimal lsunave) {
        this.lsunave = lsunave;
    }

    /**
     * @return lsunmax
     */
    public BigDecimal getLsunmax() {
        return lsunmax;
    }

    /**
     * @param lsunmax セットする lsunmax
     */
    public void setLsunmax(BigDecimal lsunmax) {
        this.lsunmax = lsunmax;
    }

    /**
     * @return lsunmin
     */
    public BigDecimal getLsunmin() {
        return lsunmin;
    }

    /**
     * @param lsunmin セットする lsunmin
     */
    public void setLsunmin(BigDecimal lsunmin) {
        this.lsunmin = lsunmin;
    }

    /**
     * @return wsunave
     */
    public BigDecimal getWsunave() {
        return wsunave;
    }

    /**
     * @param wsunave セットする wsunave
     */
    public void setWsunave(BigDecimal wsunave) {
        this.wsunave = wsunave;
    }

    /**
     * @return wsunmax
     */
    public BigDecimal getWsunmax() {
        return wsunmax;
    }

    /**
     * @param wsunmax セットする wsunmax
     */
    public void setWsunmax(BigDecimal wsunmax) {
        this.wsunmax = wsunmax;
    }

    /**
     * @return wsunmin
     */
    public BigDecimal getWsunmin() {
        return wsunmin;
    }

    /**
     * @param wsunmin セットする wsunmin
     */
    public void setWsunmin(BigDecimal wsunmin) {
        this.wsunmin = wsunmin;
    }

    /**
     * @return tsunave
     */
    public BigDecimal getTsunave() {
        return tsunave;
    }

    /**
     * @param tsunave セットする tsunave
     */
    public void setTsunave(BigDecimal tsunave) {
        this.tsunave = tsunave;
    }

    /**
     * @return tsunmax
     */
    public BigDecimal getTsunmax() {
        return tsunmax;
    }

    /**
     * @param tsunmax セットする tsunmax
     */
    public void setTsunmax(BigDecimal tsunmax) {
        this.tsunmax = tsunmax;
    }

    /**
     * @return tsunmin
     */
    public BigDecimal getTsunmin() {
        return tsunmin;
    }

    /**
     * @param tsunmin セットする tsunmin
     */
    public void setTsunmin(BigDecimal tsunmin) {
        this.tsunmin = tsunmin;
    }

    /**
     * @return dsunave
     */
    public BigDecimal getDsunave() {
        return dsunave;
    }

    /**
     * @param dsunave セットする dsunave
     */
    public void setDsunave(BigDecimal dsunave) {
        this.dsunave = dsunave;
    }

    /**
     * @return dsunmax
     */
    public BigDecimal getDsunmax() {
        return dsunmax;
    }

    /**
     * @param dsunmax セットする dsunmax
     */
    public void setDsunmax(BigDecimal dsunmax) {
        this.dsunmax = dsunmax;
    }

    /**
     * @return dsunmin
     */
    public BigDecimal getDsunmin() {
        return dsunmin;
    }

    /**
     * @param dsunmin セットする dsunmin
     */
    public void setDsunmin(BigDecimal dsunmin) {
        this.dsunmin = dsunmin;
    }

    /**
     * @return psunave
     */
    public BigDecimal getPsunave() {
        return psunave;
    }

    /**
     * @param psunave セットする psunave
     */
    public void setPsunave(BigDecimal psunave) {
        this.psunave = psunave;
    }

    /**
     * @return psunmax
     */
    public BigDecimal getPsunmax() {
        return psunmax;
    }

    /**
     * @param psunmax セットする psunmax
     */
    public void setPsunmax(BigDecimal psunmax) {
        this.psunmax = psunmax;
    }

    /**
     * @return psunmin
     */
    public BigDecimal getPsunmin() {
        return psunmin;
    }

    /**
     * @param psunmin セットする psunmin
     */
    public void setPsunmin(BigDecimal psunmin) {
        this.psunmin = psunmin;
    }

    /**
     * @return ppsunave
     */
    public BigDecimal getPpsunave() {
        return ppsunave;
    }

    /**
     * @param ppsunave セットする ppsunave
     */
    public void setPpsunave(BigDecimal ppsunave) {
        this.ppsunave = ppsunave;
    }

    /**
     * @return ppsunmax
     */
    public BigDecimal getPpsunmax() {
        return ppsunmax;
    }

    /**
     * @param ppsunmax セットする ppsunmax
     */
    public void setPpsunmax(BigDecimal ppsunmax) {
        this.ppsunmax = ppsunmax;
    }

    /**
     * @return ppsunmin
     */
    public BigDecimal getPpsunmin() {
        return ppsunmin;
    }

    /**
     * @param ppsunmin セットする ppsunmin
     */
    public void setPpsunmin(BigDecimal ppsunmin) {
        this.ppsunmin = ppsunmin;
    }

    /**
     * @return a3
     */
    public BigDecimal getA3() {
        return a3;
    }

    /**
     * @param a3 セットする a3
     */
    public void setA3(BigDecimal a3) {
        this.a3 = a3;
    }

    /**
     * @return bikou3
     */
    public String getBikou3() {
        return bikou3;
    }

    /**
     * @param bikou3 セットする bikou3
     */
    public void setBikou3(String bikou3) {
        this.bikou3 = bikou3;
    }

    /**
     * @return bikou4
     */
    public String getBikou4() {
        return bikou4;
    }

    /**
     * @param bikou4 セットする bikou4
     */
    public void setBikou4(String bikou4) {
        this.bikou4 = bikou4;
    }

    /**
     * @return hanteinaiyo1
     */
    public String getHanteinaiyo1() {
        return hanteinaiyo1;
    }

    /**
     * @param hanteinaiyo1 セットする hanteinaiyo1
     */
    public void setHanteinaiyo1(String hanteinaiyo1) {
        this.hanteinaiyo1 = hanteinaiyo1;
    }

    /**
     * @return hanteinaiyo2
     */
    public String getHanteinaiyo2() {
        return hanteinaiyo2;
    }

    /**
     * @param hanteinaiyo2 セットする hanteinaiyo2
     */
    public void setHanteinaiyo2(String hanteinaiyo2) {
        this.hanteinaiyo2 = hanteinaiyo2;
    }

    /**
     * @return hanteinaiyo3
     */
    public String getHanteinaiyo3() {
        return hanteinaiyo3;
    }

    /**
     * @param hanteinaiyo3 セットする hanteinaiyo3
     */
    public void setHanteinaiyo3(String hanteinaiyo3) {
        this.hanteinaiyo3 = hanteinaiyo3;
    }

    /**
     * @return hanteinaiyo4
     */
    public String getHanteinaiyo4() {
        return hanteinaiyo4;
    }

    /**
     * @param hanteinaiyo4 セットする hanteinaiyo4
     */
    public void setHanteinaiyo4(String hanteinaiyo4) {
        this.hanteinaiyo4 = hanteinaiyo4;
    }

    /**
     * @return userid
     */
    public String getUserid() {
        return userid;
    }

    /**
     * @param userid セットする userid
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }
}
