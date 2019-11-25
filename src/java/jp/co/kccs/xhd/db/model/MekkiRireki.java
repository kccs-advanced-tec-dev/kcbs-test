/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

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
 * mekki_rireki(ﾒｯｷ履歴)のモデルクラスです。
 *
 * @author 863 F.Zhang
 * @since 2019/11/07
 */
public class MekkiRireki {

    /**
     * * Kojyo
     */
    private String kojyo;
    /**
     * * LotNo
     */
    private String lotNo;
    /**
     * * EdaBan
     */
    private String edaBan;
    /**
     * * BunkatuNo
     */
    private Integer bunkatuNo;
    /**
     * * GoukiCode
     */
    private String goukiCode;
    /**
     * * KCPNO
     */
    private String kcpno;
    /**
     * * BunkatuSuu
     */
    private Integer bunkatuSuu;
    /**
     * * MediaName1
     */
    private String mediaName1;
    /**
     * * Mediacc1
     */
    private Integer mediacc1;
    /**
     * * MediaName2
     */
    private String mediaName2;
    /**
     * * Mediacc2
     */
    private Integer mediacc2;
    /**
     * * NiA
     */
    private Integer niA;
    /**
     * * NiAM
     */
    private Integer niAM;
    /**
     * * SnA
     */
    private Integer snA;
    /**
     * * SnAM
     */
    private Integer snAM;
    /**
     * * JokenNo
     */
    private Integer jokenNo;
    /**
     * * TorokuSyaCode
     */
    private String torokuSyaCode;
    /**
     * * TonyuSyaCode
     */
    private String tonyuSyaCode;
    /**
     * * DomeZanChk
     */
    private Integer domeZanChk;
    /**
     * * DomeNo
     */
    private String domeNo;
    /**
     * * NiSou
     */
    private Integer niSou;
    /**
     * * SnSou
     */
    private Integer snSou;
    /**
     * * TorokuNichiji
     */
    private Timestamp torokuNichiji;
    /**
     * * StartNichiji
     */
    private Timestamp startNichiji;
    /**
     * * EndNichiji
     */
    private Timestamp endNichiji;
    /**
     * * KaisyuSyaCode
     */
    private String kaisyuSyaCode;
    /**
     * * ContainerNo
     */
    private Integer containerNo;
    /**
     * * NiTime
     */
    private Integer niTime;
    /**
     * * SnTime
     */
    private Integer snTime;
    /**
     * * StartNichiji_Sn
     */
    private Timestamp startNichiji_Sn;
    /**
     * * EndNichiji_Ni
     */
    private Timestamp endNichiji_Ni;
    /**
     * * Nurekensakekka
     */
    private String nurekensakekka;
    /**
     * * Tainetsukensakekka
     */
    private String tainetsukensakekka;
    /**
     * * Gaikankensakekka
     */
    private String gaikankensakekka;

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
     * @return lotNo
     */
    public String getLotNo() {
        return lotNo;
    }

    /**
     * @param lotNo セットする lotNo
     */
    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    /**
     * @return edaBan
     */
    public String getEdaBan() {
        return edaBan;
    }

    /**
     * @param edaBan セットする edaBan
     */
    public void setEdaBan(String edaBan) {
        this.edaBan = edaBan;
    }

    /**
     * @return bunkatuNo
     */
    public Integer getBunkatuNo() {
        return bunkatuNo;
    }

    /**
     * @param bunkatuNo セットする bunkatuNo
     */
    public void setBunkatuNo(Integer bunkatuNo) {
        this.bunkatuNo = bunkatuNo;
    }

    /**
     * @return goukiCode
     */
    public String getGoukiCode() {
        return goukiCode;
    }

    /**
     * @param goukiCode セットする goukiCode
     */
    public void setGoukiCode(String goukiCode) {
        this.goukiCode = goukiCode;
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
     * @return bunkatuSuu
     */
    public Integer getBunkatuSuu() {
        return bunkatuSuu;
    }

    /**
     * @param bunkatuSuu セットする bunkatuSuu
     */
    public void setBunkatuSuu(Integer bunkatuSuu) {
        this.bunkatuSuu = bunkatuSuu;
    }

    /**
     * @return mediaName1
     */
    public String getMediaName1() {
        return mediaName1;
    }

    /**
     * @param mediaName1 セットする mediaName1
     */
    public void setMediaName1(String mediaName1) {
        this.mediaName1 = mediaName1;
    }

    /**
     * @return mediacc1
     */
    public Integer getMediacc1() {
        return mediacc1;
    }

    /**
     * @param mediacc1 セットする mediacc1
     */
    public void setMediacc1(Integer mediacc1) {
        this.mediacc1 = mediacc1;
    }

    /**
     * @return mediaName2
     */
    public String getMediaName2() {
        return mediaName2;
    }

    /**
     * @param mediaName2 セットする mediaName2
     */
    public void setMediaName2(String mediaName2) {
        this.mediaName2 = mediaName2;
    }

    /**
     * @return mediacc2
     */
    public Integer getMediacc2() {
        return mediacc2;
    }

    /**
     * @param mediacc2 セットする mediacc2
     */
    public void setMediacc2(Integer mediacc2) {
        this.mediacc2 = mediacc2;
    }

    /**
     * @return niA
     */
    public Integer getNiA() {
        return niA;
    }

    /**
     * @param niA セットする niA
     */
    public void setNiA(Integer niA) {
        this.niA = niA;
    }

    /**
     * @return niAM
     */
    public Integer getNiAM() {
        return niAM;
    }

    /**
     * @param niAM セットする niAM
     */
    public void setNiAM(Integer niAM) {
        this.niAM = niAM;
    }

    /**
     * @return snA
     */
    public Integer getSnA() {
        return snA;
    }

    /**
     * @param snA セットする snA
     */
    public void setSnA(Integer snA) {
        this.snA = snA;
    }

    /**
     * @return snAM
     */
    public Integer getSnAM() {
        return snAM;
    }

    /**
     * @param snAM セットする snAM
     */
    public void setSnAM(Integer snAM) {
        this.snAM = snAM;
    }

    /**
     * @return jokenNo
     */
    public Integer getJokenNo() {
        return jokenNo;
    }

    /**
     * @param jokenNo セットする jokenNo
     */
    public void setJokenNo(Integer jokenNo) {
        this.jokenNo = jokenNo;
    }

    /**
     * @return torokuSyaCode
     */
    public String getTorokuSyaCode() {
        return torokuSyaCode;
    }

    /**
     * @param torokuSyaCode セットする torokuSyaCode
     */
    public void setTorokuSyaCode(String torokuSyaCode) {
        this.torokuSyaCode = torokuSyaCode;
    }

    /**
     * @return tonyuSyaCode
     */
    public String getTonyuSyaCode() {
        return tonyuSyaCode;
    }

    /**
     * @param tonyuSyaCode セットする tonyuSyaCode
     */
    public void setTonyuSyaCode(String tonyuSyaCode) {
        this.tonyuSyaCode = tonyuSyaCode;
    }

    /**
     * @return domeZanChk
     */
    public Integer getDomeZanChk() {
        return domeZanChk;
    }

    /**
     * @param domeZanChk セットする domeZanChk
     */
    public void setDomeZanChk(Integer domeZanChk) {
        this.domeZanChk = domeZanChk;
    }

    /**
     * @return domeNo
     */
    public String getDomeNo() {
        return domeNo;
    }

    /**
     * @param domeNo セットする domeNo
     */
    public void setDomeNo(String domeNo) {
        this.domeNo = domeNo;
    }

    /**
     * @return niSou
     */
    public Integer getNiSou() {
        return niSou;
    }

    /**
     * @param niSou セットする niSou
     */
    public void setNiSou(Integer niSou) {
        this.niSou = niSou;
    }

    /**
     * @return snSou
     */
    public Integer getSnSou() {
        return snSou;
    }

    /**
     * @param snSou セットする snSou
     */
    public void setSnSou(Integer snSou) {
        this.snSou = snSou;
    }

    /**
     * @return torokuNichiji
     */
    public Timestamp getTorokuNichiji() {
        return torokuNichiji;
    }

    /**
     * @param torokuNichiji セットする torokuNichiji
     */
    public void setTorokuNichiji(Timestamp torokuNichiji) {
        this.torokuNichiji = torokuNichiji;
    }

    /**
     * @return startNichiji
     */
    public Timestamp getStartNichiji() {
        return startNichiji;
    }

    /**
     * @param startNichiji セットする startNichiji
     */
    public void setStartNichiji(Timestamp startNichiji) {
        this.startNichiji = startNichiji;
    }

    /**
     * @return endNichiji
     */
    public Timestamp getEndNichiji() {
        return endNichiji;
    }

    /**
     * @param endNichiji セットする endNichiji
     */
    public void setEndNichiji(Timestamp endNichiji) {
        this.endNichiji = endNichiji;
    }

    /**
     * @return kaisyuSyaCode
     */
    public String getKaisyuSyaCode() {
        return kaisyuSyaCode;
    }

    /**
     * @param kaisyuSyaCode セットする kaisyuSyaCode
     */
    public void setKaisyuSyaCode(String kaisyuSyaCode) {
        this.kaisyuSyaCode = kaisyuSyaCode;
    }

    /**
     * @return containerNo
     */
    public Integer getContainerNo() {
        return containerNo;
    }

    /**
     * @param containerNo セットする containerNo
     */
    public void setContainerNo(Integer containerNo) {
        this.containerNo = containerNo;
    }

    /**
     * @return niTime
     */
    public Integer getNiTime() {
        return niTime;
    }

    /**
     * @param niTime セットする niTime
     */
    public void setNiTime(Integer niTime) {
        this.niTime = niTime;
    }

    /**
     * @return snTime
     */
    public Integer getSnTime() {
        return snTime;
    }

    /**
     * @param snTime セットする snTime
     */
    public void setSnTime(Integer snTime) {
        this.snTime = snTime;
    }

    /**
     * @return startNichiji_Sn
     */
    public Timestamp getStartNichiji_Sn() {
        return startNichiji_Sn;
    }

    /**
     * @param startNichiji_Sn セットする startNichiji_Sn
     */
    public void setStartNichiji_Sn(Timestamp startNichiji_Sn) {
        this.startNichiji_Sn = startNichiji_Sn;
    }

    /**
     * @return endNichiji_Ni
     */
    public Timestamp getEndNichiji_Ni() {
        return endNichiji_Ni;
    }

    /**
     * @param endNichiji_Ni セットする endNichiji_Ni
     */
    public void setEndNichiji_Ni(Timestamp endNichiji_Ni) {
        this.endNichiji_Ni = endNichiji_Ni;
    }

    /**
     * @return nurekensakekka
     */
    public String getNurekensakekka() {
        return nurekensakekka;
    }

    /**
     * @param nurekensakekka セットする nurekensakekka
     */
    public void setNurekensakekka(String nurekensakekka) {
        this.nurekensakekka = nurekensakekka;
    }

    /**
     * @return tainetsukensakekka
     */
    public String getTainetsukensakekka() {
        return tainetsukensakekka;
    }

    /**
     * @param tainetsukensakekka セットする tainetsukensakekka
     */
    public void setTainetsukensakekka(String tainetsukensakekka) {
        this.tainetsukensakekka = tainetsukensakekka;
    }

    /**
     * @return gaikankensakekka
     */
    public String getGaikankensakekka() {
        return gaikankensakekka;
    }

    /**
     * @param gaikankensakekka セットする gaikankensakekka
     */
    public void setGaikankensakekka(String gaikankensakekka) {
        this.gaikankensakekka = gaikankensakekka;
    }

}
