/*
 * Copyright 2020 Kyocera Communication Systems Co., Ltd All rights reserved.
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
 * 変更日	2020/02/14<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 外部電極・ﾒｯｷ膜厚履歴検索画面のモデルクラスです。
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2020/02/14
 */
public class GXHDO201B044Model implements Serializable {

    /**
     * ﾛｯﾄNo
     */
    private String lotno = "";

    /**
     * KCPNO
     */
    private String kcpno = "";

    /**
     * 分割No
     */
    private Integer bunkatuno = null;

    /**
     * 個数
     */
    private Integer bunkatuSuu = null;

    /**
     * ﾒﾃﾞｨｱ
     */
    private String mediaName1 = "";

    /**
     * ﾒﾃﾞｨｱ量1(cc)
     */
    private Integer mediacc1 = null;

    /**
     * ﾒﾃﾞｨｱ2
     */
    private String mediaName2 = "";

    /**
     * ﾒﾃﾞｨｱ量2(cc)
     */
    private Integer mediacc2 = null;

    /**
     * 条件NIﾒｯｷ(電力)
     */
    private Integer niA = null;

    /**
     * 条件NIﾒｯｷ(積算)
     */
    private Integer niAM = null;

    /**
     * 条件SNﾒｯｷ(電力)
     */
    private Integer snA = null;

    /**
     * 条件SNﾒｯｷ(積算)
     */
    private Integer snAM = null;

    /**
     * ﾌﾟﾛｸﾞﾗﾑNo
     */
    private Integer jokenNo = null;

    /**
     * 登録日時
     */
    private Timestamp torokuNichiji = null;

    /**
     * 登録者
     */
    private String torokuSyaCode = "";

    /**
     * 投入日時
     */
    private Timestamp startNichiji = null;

    /**
     * 投入者
     */
    private String tonyuSyaCode = "";

    /**
     * ﾄﾞｰﾑ製品残
     */
    private Integer domeZanChk = null;

    /**
     * ﾄﾞｰﾑ番号
     */
    private String domeNo = "";

    /**
     * 槽NI
     */
    private Integer niSou = null;

    /**
     * 槽SN
     */
    private Integer snSou = null;

    /**
     * 回収日時
     */
    private Timestamp endNichiji = null;

    /**
     * 回収者
     */
    private String kaisyuSyaCode = "";

    /**
     * Ni膜厚(MIN)
     */
    private BigDecimal makuatsunimin = null;

    /**
     * Ni膜厚(MAX)
     */
    private BigDecimal makuatsunimax = null;

    /**
     * Ni膜厚(AVE)
     */
    private BigDecimal makuatsuniave = null;

    /**
     * Sn膜厚(MIN)
     */
    private BigDecimal makuatsusnmin = null;

    /**
     * Sn膜厚(MAX)
     */
    private BigDecimal makuatsusnmax = null;

    /**
     * Sn膜厚(AVE)
     */
    private BigDecimal makuatsusnave = null;

    /**
     * Ni膜厚01
     */
    private BigDecimal makuatsuni01 = null;

    /**
     * Ni膜厚02
     */
    private BigDecimal makuatsuni02 = null;

    /**
     * Ni膜厚03
     */
    private BigDecimal makuatsuni03 = null;

    /**
     * Ni膜厚04
     */
    private BigDecimal makuatsuni04 = null;

    /**
     * Ni膜厚05
     */
    private BigDecimal makuatsuni05 = null;

    /**
     * Ni膜厚06
     */
    private BigDecimal makuatsuni06 = null;

    /**
     * Ni膜厚07
     */
    private BigDecimal makuatsuni07 = null;

    /**
     * Ni膜厚08
     */
    private BigDecimal makuatsuni08 = null;

    /**
     * Ni膜厚09
     */
    private BigDecimal makuatsuni09 = null;

    /**
     * Ni膜厚10
     */
    private BigDecimal makuatsuni10 = null;

    /**
     * Ni膜厚11
     */
    private BigDecimal makuatsuni11 = null;

    /**
     * Ni膜厚12
     */
    private BigDecimal makuatsuni12 = null;

    /**
     * Ni膜厚13
     */
    private BigDecimal makuatsuni13 = null;

    /**
     * Ni膜厚14
     */
    private BigDecimal makuatsuni14 = null;

    /**
     * Ni膜厚15
     */
    private BigDecimal makuatsuni15 = null;

    /**
     * Ni膜厚16
     */
    private BigDecimal makuatsuni16 = null;

    /**
     * Ni膜厚17
     */
    private BigDecimal makuatsuni17 = null;

    /**
     * Ni膜厚18
     */
    private BigDecimal makuatsuni18 = null;

    /**
     * Ni膜厚19
     */
    private BigDecimal makuatsuni19 = null;

    /**
     * Ni膜厚20
     */
    private BigDecimal makuatsuni20 = null;

    /**
     * Sn膜厚01
     */
    private BigDecimal makuatsusn01 = null;

    /**
     * Sn膜厚02
     */
    private BigDecimal makuatsusn02 = null;

    /**
     * Sn膜厚03
     */
    private BigDecimal makuatsusn03 = null;

    /**
     * Sn膜厚04
     */
    private BigDecimal makuatsusn04 = null;

    /**
     * Sn膜厚05
     */
    private BigDecimal makuatsusn05 = null;

    /**
     * Sn膜厚06
     */
    private BigDecimal makuatsusn06 = null;

    /**
     * Sn膜厚07
     */
    private BigDecimal makuatsusn07 = null;

    /**
     * Sn膜厚08
     */
    private BigDecimal makuatsusn08 = null;

    /**
     * Sn膜厚09
     */
    private BigDecimal makuatsusn09 = null;

    /**
     * Sn膜厚10
     */
    private BigDecimal makuatsusn10 = null;

    /**
     * Sn膜厚11
     */
    private BigDecimal makuatsusn11 = null;

    /**
     * Sn膜厚12
     */
    private BigDecimal makuatsusn12 = null;

    /**
     * Sn膜厚13
     */
    private BigDecimal makuatsusn13 = null;

    /**
     * Sn膜厚14
     */
    private BigDecimal makuatsusn14 = null;

    /**
     * Sn膜厚15
     */
    private BigDecimal makuatsusn15 = null;

    /**
     * Sn膜厚16
     */
    private BigDecimal makuatsusn16 = null;

    /**
     * Sn膜厚17
     */
    private BigDecimal makuatsusn17 = null;

    /**
     * Sn膜厚18
     */
    private BigDecimal makuatsusn18 = null;

    /**
     * Sn膜厚19
     */
    private BigDecimal makuatsusn19 = null;

    /**
     * Sn膜厚20
     */
    private BigDecimal makuatsusn20 = null;

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
     * 分割No
     *
     * @return the bunkatuno
     */
    public Integer getBunkatuno() {
        return bunkatuno;
    }

    /**
     * 分割No
     *
     * @param bunkatuno the bunkatuno to set
     */
    public void setBunkatuno(Integer bunkatuno) {
        this.bunkatuno = bunkatuno;
    }

    /**
     * 個数
     *
     * @return the bunkatuSuu
     */
    public Integer getBunkatuSuu() {
        return bunkatuSuu;
    }

    /**
     * 個数
     *
     * @param bunkatuSuu the bunkatuSuu to set
     */
    public void setBunkatuSuu(Integer bunkatuSuu) {
        this.bunkatuSuu = bunkatuSuu;
    }

    /**
     * ﾒﾃﾞｨｱ
     *
     * @return the mediaName1
     */
    public String getMediaName1() {
        return mediaName1;
    }

    /**
     * ﾒﾃﾞｨｱ
     *
     * @param mediaName1 the mediaName1 to set
     */
    public void setMediaName1(String mediaName1) {
        this.mediaName1 = mediaName1;
    }

    /**
     * ﾒﾃﾞｨｱ量1(cc)
     *
     * @return the mediacc1
     */
    public Integer getMediacc1() {
        return mediacc1;
    }

    /**
     * ﾒﾃﾞｨｱ量1(cc)
     *
     * @param mediacc1 the mediacc1 to set
     */
    public void setMediacc1(Integer mediacc1) {
        this.mediacc1 = mediacc1;
    }

    /**
     * ﾒﾃﾞｨｱ2
     *
     * @return the mediaName2
     */
    public String getMediaName2() {
        return mediaName2;
    }

    /**
     * ﾒﾃﾞｨｱ2
     *
     * @param mediaName2 the mediaName2 to set
     */
    public void setMediaName2(String mediaName2) {
        this.mediaName2 = mediaName2;
    }

    /**
     * ﾒﾃﾞｨｱ量2(cc)
     *
     * @return the mediacc2
     */
    public Integer getMediacc2() {
        return mediacc2;
    }

    /**
     * ﾒﾃﾞｨｱ量2(cc)
     *
     * @param mediacc2 the mediacc2 to set
     */
    public void setMediacc2(Integer mediacc2) {
        this.mediacc2 = mediacc2;
    }

    /**
     * 条件NIﾒｯｷ(電力)
     *
     * @return the niA
     */
    public Integer getNiA() {
        return niA;
    }

    /**
     * 条件NIﾒｯｷ(電力)
     *
     * @param niA the niA to set
     */
    public void setNiA(Integer niA) {
        this.niA = niA;
    }

    /**
     * 条件NIﾒｯｷ(積算)
     *
     * @return the niAM
     */
    public Integer getNiAM() {
        return niAM;
    }

    /**
     * 条件NIﾒｯｷ(積算)
     *
     * @param niAM the niAM to set
     */
    public void setNiAM(Integer niAM) {
        this.niAM = niAM;
    }

    /**
     * 条件SNﾒｯｷ(電力)
     *
     * @return the snA
     */
    public Integer getSnA() {
        return snA;
    }

    /**
     * 条件SNﾒｯｷ(電力)
     *
     * @param snA the snA to set
     */
    public void setSnA(Integer snA) {
        this.snA = snA;
    }

    /**
     * 条件SNﾒｯｷ(積算)
     *
     * @return the snAM
     */
    public Integer getSnAM() {
        return snAM;
    }

    /**
     * 条件SNﾒｯｷ(積算)
     *
     * @param snAM the snAM to set
     */
    public void setSnAM(Integer snAM) {
        this.snAM = snAM;
    }

    /**
     * ﾌﾟﾛｸﾞﾗﾑNo
     *
     * @return the jokenNo
     */
    public Integer getJokenNo() {
        return jokenNo;
    }

    /**
     * ﾌﾟﾛｸﾞﾗﾑNo
     *
     * @param jokenNo the jokenNo to set
     */
    public void setJokenNo(Integer jokenNo) {
        this.jokenNo = jokenNo;
    }

    /**
     * 登録日時
     *
     * @return the torokuNichiji
     */
    public Timestamp getTorokuNichiji() {
        return torokuNichiji;
    }

    /**
     * 登録日時
     *
     * @param torokuNichiji the torokuNichiji to set
     */
    public void setTorokuNichiji(Timestamp torokuNichiji) {
        this.torokuNichiji = torokuNichiji;
    }

    /**
     * 登録者
     *
     * @return the torokuSyaCode
     */
    public String getTorokuSyaCode() {
        return torokuSyaCode;
    }

    /**
     * 登録者
     *
     * @param torokuSyaCode the torokuSyaCode to set
     */
    public void setTorokuSyaCode(String torokuSyaCode) {
        this.torokuSyaCode = torokuSyaCode;
    }

    /**
     * 投入日時
     *
     * @return the startNichiji
     */
    public Timestamp getStartNichiji() {
        return startNichiji;
    }

    /**
     * 投入日時
     *
     * @param startNichiji the startNichiji to set
     */
    public void setStartNichiji(Timestamp startNichiji) {
        this.startNichiji = startNichiji;
    }

    /**
     * 投入者
     *
     * @return the tonyuSyaCode
     */
    public String getTonyuSyaCode() {
        return tonyuSyaCode;
    }

    /**
     * 投入者
     *
     * @param tonyuSyaCode the tonyuSyaCode to set
     */
    public void setTonyuSyaCode(String tonyuSyaCode) {
        this.tonyuSyaCode = tonyuSyaCode;
    }

    /**
     * ﾄﾞｰﾑ製品残
     *
     * @return the domeZanChk
     */
    public Integer getDomeZanChk() {
        return domeZanChk;
    }

    /**
     * ﾄﾞｰﾑ製品残
     *
     * @param domeZanChk the domeZanChk to set
     */
    public void setDomeZanChk(Integer domeZanChk) {
        this.domeZanChk = domeZanChk;
    }

    /**
     * ﾄﾞｰﾑ番号
     *
     * @return the domeNo
     */
    public String getDomeNo() {
        return domeNo;
    }

    /**
     * ﾄﾞｰﾑ番号
     *
     * @param domeNo the domeNo to set
     */
    public void setDomeNo(String domeNo) {
        this.domeNo = domeNo;
    }

    /**
     * 槽NI
     *
     * @return the niSou
     */
    public Integer getNiSou() {
        return niSou;
    }

    /**
     * 槽NI
     *
     * @param niSou the niSou to set
     */
    public void setNiSou(Integer niSou) {
        this.niSou = niSou;
    }

    /**
     * 槽SN
     *
     * @return the snSou
     */
    public Integer getSnSou() {
        return snSou;
    }

    /**
     * 槽SN
     *
     * @param snSou the snSou to set
     */
    public void setSnSou(Integer snSou) {
        this.snSou = snSou;
    }

    /**
     * 回収日時
     *
     * @return the endNichiji
     */
    public Timestamp getEndNichiji() {
        return endNichiji;
    }

    /**
     * 回収日時
     *
     * @param endNichiji the endNichiji to set
     */
    public void setEndNichiji(Timestamp endNichiji) {
        this.endNichiji = endNichiji;
    }

    /**
     * 回収者
     *
     * @return the kaisyuSyaCode
     */
    public String getKaisyuSyaCode() {
        return kaisyuSyaCode;
    }

    /**
     * 回収者
     *
     * @param kaisyuSyaCode the kaisyuSyaCode to set
     */
    public void setKaisyuSyaCode(String kaisyuSyaCode) {
        this.kaisyuSyaCode = kaisyuSyaCode;
    }

    /**
     * Ni膜厚(MIN)
     *
     * @return the makuatsunimin
     */
    public BigDecimal getMakuatsunimin() {
        return makuatsunimin;
    }

    /**
     * Ni膜厚(MIN)
     *
     * @param makuatsunimin the makuatsunimin to set
     */
    public void setMakuatsunimin(BigDecimal makuatsunimin) {
        this.makuatsunimin = makuatsunimin;
    }

    /**
     * Ni膜厚(MAX)
     *
     * @return the makuatsunimax
     */
    public BigDecimal getMakuatsunimax() {
        return makuatsunimax;
    }

    /**
     * Ni膜厚(MAX)
     *
     * @param makuatsunimax the makuatsunimax to set
     */
    public void setMakuatsunimax(BigDecimal makuatsunimax) {
        this.makuatsunimax = makuatsunimax;
    }

    /**
     * Ni膜厚(AVE)
     *
     * @return the makuatsuniave
     */
    public BigDecimal getMakuatsuniave() {
        return makuatsuniave;
    }

    /**
     * Ni膜厚(AVE)
     *
     * @param makuatsuniave the makuatsuniave to set
     */
    public void setMakuatsuniave(BigDecimal makuatsuniave) {
        this.makuatsuniave = makuatsuniave;
    }

    /**
     * Sn膜厚(MIN)
     *
     * @return the makuatsusnmin
     */
    public BigDecimal getMakuatsusnmin() {
        return makuatsusnmin;
    }

    /**
     * Sn膜厚(MIN)
     *
     * @param makuatsusnmin the makuatsusnmin to set
     */
    public void setMakuatsusnmin(BigDecimal makuatsusnmin) {
        this.makuatsusnmin = makuatsusnmin;
    }

    /**
     * Sn膜厚(MAX)
     *
     * @return the makuatsusnmax
     */
    public BigDecimal getMakuatsusnmax() {
        return makuatsusnmax;
    }

    /**
     * Sn膜厚(MAX)
     *
     * @param makuatsusnmax the makuatsusnmax to set
     */
    public void setMakuatsusnmax(BigDecimal makuatsusnmax) {
        this.makuatsusnmax = makuatsusnmax;
    }

    /**
     * Sn膜厚(AVE)
     *
     * @return the makuatsusnave
     */
    public BigDecimal getMakuatsusnave() {
        return makuatsusnave;
    }

    /**
     * Sn膜厚(AVE)
     *
     * @param makuatsusnave the makuatsusnave to set
     */
    public void setMakuatsusnave(BigDecimal makuatsusnave) {
        this.makuatsusnave = makuatsusnave;
    }

    /**
     * Ni膜厚01
     *
     * @return the makuatsuni01
     */
    public BigDecimal getMakuatsuni01() {
        return makuatsuni01;
    }

    /**
     * Ni膜厚01
     *
     * @param makuatsuni01 the makuatsuni01 to set
     */
    public void setMakuatsuni01(BigDecimal makuatsuni01) {
        this.makuatsuni01 = makuatsuni01;
    }

    /**
     * Ni膜厚02
     *
     * @return the makuatsuni02
     */
    public BigDecimal getMakuatsuni02() {
        return makuatsuni02;
    }

    /**
     * Ni膜厚02
     *
     * @param makuatsuni02 the makuatsuni02 to set
     */
    public void setMakuatsuni02(BigDecimal makuatsuni02) {
        this.makuatsuni02 = makuatsuni02;
    }

    /**
     * Ni膜厚03
     *
     * @return the makuatsuni03
     */
    public BigDecimal getMakuatsuni03() {
        return makuatsuni03;
    }

    /**
     * Ni膜厚03
     *
     * @param makuatsuni03 the makuatsuni03 to set
     */
    public void setMakuatsuni03(BigDecimal makuatsuni03) {
        this.makuatsuni03 = makuatsuni03;
    }

    /**
     * Ni膜厚04
     *
     * @return the makuatsuni04
     */
    public BigDecimal getMakuatsuni04() {
        return makuatsuni04;
    }

    /**
     * Ni膜厚04
     *
     * @param makuatsuni04 the makuatsuni04 to set
     */
    public void setMakuatsuni04(BigDecimal makuatsuni04) {
        this.makuatsuni04 = makuatsuni04;
    }

    /**
     * Ni膜厚05
     *
     * @return the makuatsuni05
     */
    public BigDecimal getMakuatsuni05() {
        return makuatsuni05;
    }

    /**
     * Ni膜厚05
     *
     * @param makuatsuni05 the makuatsuni05 to set
     */
    public void setMakuatsuni05(BigDecimal makuatsuni05) {
        this.makuatsuni05 = makuatsuni05;
    }

    /**
     * Ni膜厚06
     *
     * @return the makuatsuni06
     */
    public BigDecimal getMakuatsuni06() {
        return makuatsuni06;
    }

    /**
     * Ni膜厚06
     *
     * @param makuatsuni06 the makuatsuni06 to set
     */
    public void setMakuatsuni06(BigDecimal makuatsuni06) {
        this.makuatsuni06 = makuatsuni06;
    }

    /**
     * Ni膜厚07
     *
     * @return the makuatsuni07
     */
    public BigDecimal getMakuatsuni07() {
        return makuatsuni07;
    }

    /**
     * Ni膜厚07
     *
     * @param makuatsuni07 the makuatsuni07 to set
     */
    public void setMakuatsuni07(BigDecimal makuatsuni07) {
        this.makuatsuni07 = makuatsuni07;
    }

    /**
     * Ni膜厚08
     *
     * @return the makuatsuni08
     */
    public BigDecimal getMakuatsuni08() {
        return makuatsuni08;
    }

    /**
     * Ni膜厚08
     *
     * @param makuatsuni08 the makuatsuni08 to set
     */
    public void setMakuatsuni08(BigDecimal makuatsuni08) {
        this.makuatsuni08 = makuatsuni08;
    }

    /**
     * Ni膜厚09
     *
     * @return the makuatsuni09
     */
    public BigDecimal getMakuatsuni09() {
        return makuatsuni09;
    }

    /**
     * Ni膜厚09
     *
     * @param makuatsuni09 the makuatsuni09 to set
     */
    public void setMakuatsuni09(BigDecimal makuatsuni09) {
        this.makuatsuni09 = makuatsuni09;
    }

    /**
     * Ni膜厚10
     *
     * @return the makuatsuni10
     */
    public BigDecimal getMakuatsuni10() {
        return makuatsuni10;
    }

    /**
     * Ni膜厚10
     *
     * @param makuatsuni10 the makuatsuni10 to set
     */
    public void setMakuatsuni10(BigDecimal makuatsuni10) {
        this.makuatsuni10 = makuatsuni10;
    }

    /**
     * Ni膜厚11
     *
     * @return the makuatsuni11
     */
    public BigDecimal getMakuatsuni11() {
        return makuatsuni11;
    }

    /**
     * Ni膜厚11
     *
     * @param makuatsuni11 the makuatsuni11 to set
     */
    public void setMakuatsuni11(BigDecimal makuatsuni11) {
        this.makuatsuni11 = makuatsuni11;
    }

    /**
     * Ni膜厚12
     *
     * @return the makuatsuni12
     */
    public BigDecimal getMakuatsuni12() {
        return makuatsuni12;
    }

    /**
     * Ni膜厚12
     *
     * @param makuatsuni12 the makuatsuni12 to set
     */
    public void setMakuatsuni12(BigDecimal makuatsuni12) {
        this.makuatsuni12 = makuatsuni12;
    }

    /**
     * Ni膜厚13
     *
     * @return the makuatsuni13
     */
    public BigDecimal getMakuatsuni13() {
        return makuatsuni13;
    }

    /**
     * Ni膜厚13
     *
     * @param makuatsuni13 the makuatsuni13 to set
     */
    public void setMakuatsuni13(BigDecimal makuatsuni13) {
        this.makuatsuni13 = makuatsuni13;
    }

    /**
     * Ni膜厚14
     *
     * @return the makuatsuni14
     */
    public BigDecimal getMakuatsuni14() {
        return makuatsuni14;
    }

    /**
     * Ni膜厚14
     *
     * @param makuatsuni14 the makuatsuni14 to set
     */
    public void setMakuatsuni14(BigDecimal makuatsuni14) {
        this.makuatsuni14 = makuatsuni14;
    }

    /**
     * Ni膜厚15
     *
     * @return the makuatsuni15
     */
    public BigDecimal getMakuatsuni15() {
        return makuatsuni15;
    }

    /**
     * Ni膜厚15
     *
     * @param makuatsuni15 the makuatsuni15 to set
     */
    public void setMakuatsuni15(BigDecimal makuatsuni15) {
        this.makuatsuni15 = makuatsuni15;
    }

    /**
     * Ni膜厚16
     *
     * @return the makuatsuni16
     */
    public BigDecimal getMakuatsuni16() {
        return makuatsuni16;
    }

    /**
     * Ni膜厚16
     *
     * @param makuatsuni16 the makuatsuni16 to set
     */
    public void setMakuatsuni16(BigDecimal makuatsuni16) {
        this.makuatsuni16 = makuatsuni16;
    }

    /**
     * Ni膜厚17
     *
     * @return the makuatsuni17
     */
    public BigDecimal getMakuatsuni17() {
        return makuatsuni17;
    }

    /**
     * Ni膜厚17
     *
     * @param makuatsuni17 the makuatsuni17 to set
     */
    public void setMakuatsuni17(BigDecimal makuatsuni17) {
        this.makuatsuni17 = makuatsuni17;
    }

    /**
     * Ni膜厚18
     *
     * @return the makuatsuni18
     */
    public BigDecimal getMakuatsuni18() {
        return makuatsuni18;
    }

    /**
     * Ni膜厚18
     *
     * @param makuatsuni18 the makuatsuni18 to set
     */
    public void setMakuatsuni18(BigDecimal makuatsuni18) {
        this.makuatsuni18 = makuatsuni18;
    }

    /**
     * Ni膜厚19
     *
     * @return the makuatsuni19
     */
    public BigDecimal getMakuatsuni19() {
        return makuatsuni19;
    }

    /**
     * Ni膜厚19
     *
     * @param makuatsuni19 the makuatsuni19 to set
     */
    public void setMakuatsuni19(BigDecimal makuatsuni19) {
        this.makuatsuni19 = makuatsuni19;
    }

    /**
     * Ni膜厚20
     *
     * @return the makuatsuni20
     */
    public BigDecimal getMakuatsuni20() {
        return makuatsuni20;
    }

    /**
     * Ni膜厚20
     *
     * @param makuatsuni20 the makuatsuni20 to set
     */
    public void setMakuatsuni20(BigDecimal makuatsuni20) {
        this.makuatsuni20 = makuatsuni20;
    }

    /**
     * Sn膜厚01
     *
     * @return the makuatsusn01
     */
    public BigDecimal getMakuatsusn01() {
        return makuatsusn01;
    }

    /**
     * Sn膜厚01
     *
     * @param makuatsusn01 the makuatsusn01 to set
     */
    public void setMakuatsusn01(BigDecimal makuatsusn01) {
        this.makuatsusn01 = makuatsusn01;
    }

    /**
     * Sn膜厚02
     *
     * @return the makuatsusn02
     */
    public BigDecimal getMakuatsusn02() {
        return makuatsusn02;
    }

    /**
     * Sn膜厚02
     *
     * @param makuatsusn02 the makuatsusn02 to set
     */
    public void setMakuatsusn02(BigDecimal makuatsusn02) {
        this.makuatsusn02 = makuatsusn02;
    }

    /**
     * Sn膜厚03
     *
     * @return the makuatsusn03
     */
    public BigDecimal getMakuatsusn03() {
        return makuatsusn03;
    }

    /**
     * Sn膜厚03
     *
     * @param makuatsusn03 the makuatsusn03 to set
     */
    public void setMakuatsusn03(BigDecimal makuatsusn03) {
        this.makuatsusn03 = makuatsusn03;
    }

    /**
     * Sn膜厚04
     *
     * @return the makuatsusn04
     */
    public BigDecimal getMakuatsusn04() {
        return makuatsusn04;
    }

    /**
     * Sn膜厚04
     *
     * @param makuatsusn04 the makuatsusn04 to set
     */
    public void setMakuatsusn04(BigDecimal makuatsusn04) {
        this.makuatsusn04 = makuatsusn04;
    }

    /**
     * Sn膜厚05
     *
     * @return the makuatsusn05
     */
    public BigDecimal getMakuatsusn05() {
        return makuatsusn05;
    }

    /**
     * Sn膜厚05
     *
     * @param makuatsusn05 the makuatsusn05 to set
     */
    public void setMakuatsusn05(BigDecimal makuatsusn05) {
        this.makuatsusn05 = makuatsusn05;
    }

    /**
     * Sn膜厚06
     *
     * @return the makuatsusn06
     */
    public BigDecimal getMakuatsusn06() {
        return makuatsusn06;
    }

    /**
     * Sn膜厚06
     *
     * @param makuatsusn06 the makuatsusn06 to set
     */
    public void setMakuatsusn06(BigDecimal makuatsusn06) {
        this.makuatsusn06 = makuatsusn06;
    }

    /**
     * Sn膜厚07
     *
     * @return the makuatsusn07
     */
    public BigDecimal getMakuatsusn07() {
        return makuatsusn07;
    }

    /**
     * Sn膜厚07
     *
     * @param makuatsusn07 the makuatsusn07 to set
     */
    public void setMakuatsusn07(BigDecimal makuatsusn07) {
        this.makuatsusn07 = makuatsusn07;
    }

    /**
     * Sn膜厚08
     *
     * @return the makuatsusn08
     */
    public BigDecimal getMakuatsusn08() {
        return makuatsusn08;
    }

    /**
     * Sn膜厚08
     *
     * @param makuatsusn08 the makuatsusn08 to set
     */
    public void setMakuatsusn08(BigDecimal makuatsusn08) {
        this.makuatsusn08 = makuatsusn08;
    }

    /**
     * Sn膜厚09
     *
     * @return the makuatsusn09
     */
    public BigDecimal getMakuatsusn09() {
        return makuatsusn09;
    }

    /**
     * Sn膜厚09
     *
     * @param makuatsusn09 the makuatsusn09 to set
     */
    public void setMakuatsusn09(BigDecimal makuatsusn09) {
        this.makuatsusn09 = makuatsusn09;
    }

    /**
     * Sn膜厚10
     *
     * @return the makuatsusn10
     */
    public BigDecimal getMakuatsusn10() {
        return makuatsusn10;
    }

    /**
     * Sn膜厚10
     *
     * @param makuatsusn10 the makuatsusn10 to set
     */
    public void setMakuatsusn10(BigDecimal makuatsusn10) {
        this.makuatsusn10 = makuatsusn10;
    }

    /**
     * Sn膜厚11
     *
     * @return the makuatsusn11
     */
    public BigDecimal getMakuatsusn11() {
        return makuatsusn11;
    }

    /**
     * Sn膜厚11
     *
     * @param makuatsusn11 the makuatsusn11 to set
     */
    public void setMakuatsusn11(BigDecimal makuatsusn11) {
        this.makuatsusn11 = makuatsusn11;
    }

    /**
     * Sn膜厚12
     *
     * @return the makuatsusn12
     */
    public BigDecimal getMakuatsusn12() {
        return makuatsusn12;
    }

    /**
     * Sn膜厚12
     *
     * @param makuatsusn12 the makuatsusn12 to set
     */
    public void setMakuatsusn12(BigDecimal makuatsusn12) {
        this.makuatsusn12 = makuatsusn12;
    }

    /**
     * Sn膜厚13
     *
     * @return the makuatsusn13
     */
    public BigDecimal getMakuatsusn13() {
        return makuatsusn13;
    }

    /**
     * Sn膜厚13
     *
     * @param makuatsusn13 the makuatsusn13 to set
     */
    public void setMakuatsusn13(BigDecimal makuatsusn13) {
        this.makuatsusn13 = makuatsusn13;
    }

    /**
     * Sn膜厚14
     *
     * @return the makuatsusn14
     */
    public BigDecimal getMakuatsusn14() {
        return makuatsusn14;
    }

    /**
     * Sn膜厚14
     *
     * @param makuatsusn14 the makuatsusn14 to set
     */
    public void setMakuatsusn14(BigDecimal makuatsusn14) {
        this.makuatsusn14 = makuatsusn14;
    }

    /**
     * Sn膜厚15
     *
     * @return the makuatsusn15
     */
    public BigDecimal getMakuatsusn15() {
        return makuatsusn15;
    }

    /**
     * Sn膜厚15
     *
     * @param makuatsusn15 the makuatsusn15 to set
     */
    public void setMakuatsusn15(BigDecimal makuatsusn15) {
        this.makuatsusn15 = makuatsusn15;
    }

    /**
     * Sn膜厚16
     *
     * @return the makuatsusn16
     */
    public BigDecimal getMakuatsusn16() {
        return makuatsusn16;
    }

    /**
     * Sn膜厚16
     *
     * @param makuatsusn16 the makuatsusn16 to set
     */
    public void setMakuatsusn16(BigDecimal makuatsusn16) {
        this.makuatsusn16 = makuatsusn16;
    }

    /**
     * Sn膜厚17
     *
     * @return the makuatsusn17
     */
    public BigDecimal getMakuatsusn17() {
        return makuatsusn17;
    }

    /**
     * Sn膜厚17
     *
     * @param makuatsusn17 the makuatsusn17 to set
     */
    public void setMakuatsusn17(BigDecimal makuatsusn17) {
        this.makuatsusn17 = makuatsusn17;
    }

    /**
     * Sn膜厚18
     *
     * @return the makuatsusn18
     */
    public BigDecimal getMakuatsusn18() {
        return makuatsusn18;
    }

    /**
     * Sn膜厚18
     *
     * @param makuatsusn18 the makuatsusn18 to set
     */
    public void setMakuatsusn18(BigDecimal makuatsusn18) {
        this.makuatsusn18 = makuatsusn18;
    }

    /**
     * Sn膜厚19
     *
     * @return the makuatsusn19
     */
    public BigDecimal getMakuatsusn19() {
        return makuatsusn19;
    }

    /**
     * Sn膜厚19
     *
     * @param makuatsusn19 the makuatsusn19 to set
     */
    public void setMakuatsusn19(BigDecimal makuatsusn19) {
        this.makuatsusn19 = makuatsusn19;
    }

    /**
     * Sn膜厚20
     *
     * @return the makuatsusn20
     */
    public BigDecimal getMakuatsusn20() {
        return makuatsusn20;
    }

    /**
     * Sn膜厚20
     *
     * @param makuatsusn20 the makuatsusn20 to set
     */
    public void setMakuatsusn20(BigDecimal makuatsusn20) {
        this.makuatsusn20 = makuatsusn20;
    }

}
