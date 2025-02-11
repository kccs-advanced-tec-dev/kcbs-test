/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.io.Serializable;
import java.util.List;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2021/08/24<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	SRC K.Ijuin<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101D001Model(品質確認連絡書画面用)のモデルクラスです。
 *
 * @author SRC K.Ijuin
 * @since 2021/08/24
 */
public class GXHDO101D001Model implements Serializable {

    /**
     * @return the jikiqckakuninsya
     */
    public String getJikiqckakuninsya() {
        return jikiqckakuninsya;
    }

    /**
     * @param jikiqckakuninsya the jikiqckakuninsya to set
     */
    public void setJikiqckakuninsya(String jikiqckakuninsya) {
        this.jikiqckakuninsya = jikiqckakuninsya;
    }

    /**
     * 磁器QC確認者
     * @return the jikiqckakuninsyamei
     */
    public String getJikiqckakuninsyamei() {
        return jikiqckakuninsyamei;
    }

    /**
     * 磁器QC確認者
     * @param jikiqckakuninsyamei the jikiqckakuninsyamei to set
     */
    public void setJikiqckakuninsyamei(String jikiqckakuninsyamei) {
        this.jikiqckakuninsyamei = jikiqckakuninsyamei;
    }

    /**
     * @return the jikiqckakuninnichiji
     */
    public java.sql.Date getJikiqckakuninnichiji() {
        return jikiqckakuninnichiji;
    }

    /**
     * @param jikiqckakuninnichiji the jikiqckakuninnichiji to set
     */
    public void setJikiqckakuninnichiji(java.sql.Date jikiqckakuninnichiji) {
        this.jikiqckakuninnichiji = jikiqckakuninnichiji;
    }
    
    /**
     * @return the rank
     */
    public String getRank() {
        return rank;
    }

    /**
     * @param rank the rank to set
     */
    public void setRank(String rank) {
        this.rank = rank;
    }

    /**
     * @return the hakkobi
     */
    public java.sql.Date getHakkobi() {
        return hakkobi;
    }

    /**
     * @param hakkobi the hakkobi to set
     */
    public void setHakkobi(java.sql.Date hakkobi) {
        this.hakkobi = hakkobi;
    }

    /**
     * 担当者
     * @return the hakkosya
     */
    public String getHakkosya() {
        return hakkosya;
    }

    /**
     * 担当者
     * @param hakkosya the hakkosya to set
     */
    public void setHakkosya(String hakkosya) {
        this.hakkosya = hakkosya;
    }

    /**
     * 担当者名
     * @return the hakkosyamei
     */
    public String getHakkosyamei() {
        return hakkosyamei;
    }

    /**
     * 担当者名
     * @param hakkosyamei the hakkosyamei to set
     */
    public void setHakkosyamei(String hakkosyamei) {
        this.hakkosyamei = hakkosyamei;
    }

    /**
     * @return the tokuisaki
     */
    public String getTokuisaki() {
        return tokuisaki;
    }

    /**
     * @param tokuisaki the tokuisaki to set
     */
    public void setTokuisaki(String tokuisaki) {
        this.tokuisaki = tokuisaki;
    }

    /**
     * @return the hakkenkotei
     */
    public String getHakkenkotei() {
        return hakkenkotei;
    }

    /**
     * @param hakkenkotei the hakkenkotei to set
     */
    public void setHakkenkotei(String hakkenkotei) {
        this.hakkenkotei = hakkenkotei;
    }

    /**
     * 発見工程名称
     * @return the hakkenkoteimei
     */
    public String getHakkenkoteimei() {
        return hakkenkoteimei;
    }

    /**
     * 発見工程名称
     * @param hakkenkoteimei the hakkenkoteimei to set
     */
    public void setHakkenkoteimei(String hakkenkoteimei) {
        this.hakkenkoteimei = hakkenkoteimei;
    }

    /**
     * @return the torokuno
     */
    public String getTorokuno() {
        return torokuno;
    }

    /**
     * @param torokuno the torokuno to set
     */
    public void setTorokuno(String torokuno) {
        this.torokuno = torokuno;
    }

    /**
     * @return the kcpno
     */
    public String getKcpno() {
        return kcpno;
    }

    /**
     * @param kcpno the kcpno to set
     */
    public void setKcpno(String kcpno) {
        this.kcpno = kcpno;
    }

    /**
     * @return the sikasuuryo
     */
    public Long getSikasuuryo() {
        return sikasuuryo;
    }

    /**
     * @param sikasuuryo the sikasuuryo to set
     */
    public void setSikasuuryo(Long sikasuuryo) {
        this.sikasuuryo = sikasuuryo;
    }

    /**
     * @return the ownercode
     */
    public String getOwnercode() {
        return ownercode;
    }

    /**
     * @param ownercode the ownercode to set
     */
    public void setOwnercode(String ownercode) {
        this.ownercode = ownercode;
    }

    /**
     * @return the lotkubuncode
     */
    public String getLotkubuncode() {
        return lotkubuncode;
    }

    /**
     * @param lotkubuncode the lotkubuncode to set
     */
    public void setLotkubuncode(String lotkubuncode) {
        this.lotkubuncode = lotkubuncode;
    }

    /**
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * @param lotno the lotno to set
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * @return the goukicode
     */
    public String getGoukicode() {
        return goukicode;
    }

    /**
     * @param goukicode the goukicode to set
     */
    public void setGoukicode(String goukicode) {
        this.goukicode = goukicode;
    }

    /**
     * @return the furyocode1
     */
    public String getFuryocode1() {
        return furyocode1;
    }

    /**
     * @param furyocode1 the furyocode1 to set
     */
    public void setFuryocode1(String furyocode1) {
        this.furyocode1 = furyocode1;
    }

    /**
     * 不良ｺｰﾄﾞ名1
     * @return the furyocodemei1
     */
    public String getFuryocodemei1() {
        return furyocodemei1;
    }

    /**
     * 不良ｺｰﾄﾞ名1
     * @param furyocodemei1 the furyocodemei1 to set
     */
    public void setFuryocodemei1(String furyocodemei1) {
        this.furyocodemei1 = furyocodemei1;
    }

    /**
     * @return the furyomeisai1
     */
    public String getFuryomeisai1() {
        return furyomeisai1;
    }

    /**
     * @param furyomeisai1 the furyomeisai1 to set
     */
    public void setFuryomeisai1(String furyomeisai1) {
        this.furyomeisai1 = furyomeisai1;
    }

    /**
     * @return the furyoritiu1
     */
    public String getFuryoritiu1() {
        return furyoritiu1;
    }

    /**
     * @param furyoritiu1 the furyoritiu1 to set
     */
    public void setFuryoritiu1(String furyoritiu1) {
        this.furyoritiu1 = furyoritiu1;
    }

    /**
     * @return the furyobunrui1
     */
    public String getFuryobunrui1() {
        return furyobunrui1;
    }

    /**
     * @param furyobunrui1 the furyobunrui1 to set
     */
    public void setFuryobunrui1(String furyobunrui1) {
        this.furyobunrui1 = furyobunrui1;
    }

    /**
     * @return the furyocode2
     */
    public String getFuryocode2() {
        return furyocode2;
    }

    /**
     * @param furyocode2 the furyocode2 to set
     */
    public void setFuryocode2(String furyocode2) {
        this.furyocode2 = furyocode2;
    }

    /**
     * 不良ｺｰﾄﾞ名2
     * @return the furyocodemei2
     */
    public String getFuryocodemei2() {
        return furyocodemei2;
    }

    /**
     * 不良ｺｰﾄﾞ名2
     * @param furyocodemei2 the furyocodemei2 to set
     */
    public void setFuryocodemei2(String furyocodemei2) {
        this.furyocodemei2 = furyocodemei2;
    }

    /**
     * @return the furyomeisai2
     */
    public String getFuryomeisai2() {
        return furyomeisai2;
    }

    /**
     * @param furyomeisai2 the furyomeisai2 to set
     */
    public void setFuryomeisai2(String furyomeisai2) {
        this.furyomeisai2 = furyomeisai2;
    }

    /**
     * @return the furyoritiu2
     */
    public String getFuryoritiu2() {
        return furyoritiu2;
    }

    /**
     * @param furyoritiu2 the furyoritiu2 to set
     */
    public void setFuryoritiu2(String furyoritiu2) {
        this.furyoritiu2 = furyoritiu2;
    }

    /**
     * @return the furyobunrui2
     */
    public String getFuryobunrui2() {
        return furyobunrui2;
    }

    /**
     * @param furyobunrui2 the furyobunrui2 to set
     */
    public void setFuryobunrui2(String furyobunrui2) {
        this.furyobunrui2 = furyobunrui2;
    }

    /**
     * @return the furyocode3
     */
    public String getFuryocode3() {
        return furyocode3;
    }

    /**
     * @param furyocode3 the furyocode3 to set
     */
    public void setFuryocode3(String furyocode3) {
        this.furyocode3 = furyocode3;
    }

    /**
     * @return the furyomeisai3
     */
    public String getFuryomeisai3() {
        return furyomeisai3;
    }

    /**
     * @param furyomeisai3 the furyomeisai3 to set
     */
    public void setFuryomeisai3(String furyomeisai3) {
        this.furyomeisai3 = furyomeisai3;
    }

    /**
     * @return the furyoritiu3
     */
    public String getFuryoritiu3() {
        return furyoritiu3;
    }

    /**
     * @param furyoritiu3 the furyoritiu3 to set
     */
    public void setFuryoritiu3(String furyoritiu3) {
        this.furyoritiu3 = furyoritiu3;
    }

    /**
     * @return the furyobunrui3
     */
    public String getFuryobunrui3() {
        return furyobunrui3;
    }

    /**
     * @param furyobunrui3 the furyobunrui3 to set
     */
    public void setFuryobunrui3(String furyobunrui3) {
        this.furyobunrui3 = furyobunrui3;
    }

    /**
     * 不良ｺｰﾄﾞ名3
     * @return the furyocodemei3
     */
    public String getFuryocodemei3() {
        return furyocodemei3;
    }

    /**
     * 不良ｺｰﾄﾞ名3
     * @param furyocodemei3 the furyocodemei3 to set
     */
    public void setFuryocodemei3(String furyocodemei3) {
        this.furyocodemei3 = furyocodemei3;
    }

    /**
     * @return the furyocode4
     */
    public String getFuryocode4() {
        return furyocode4;
    }

    /**
     * @param furyocode4 the furyocode4 to set
     */
    public void setFuryocode4(String furyocode4) {
        this.furyocode4 = furyocode4;
    }

    /**
     * 不良ｺｰﾄﾞ名4
     * @return the furyocodemei4
     */
    public String getFuryocodemei4() {
        return furyocodemei4;
    }

    /**
     * 不良ｺｰﾄﾞ名4
     * @param furyocodemei4 the furyocodemei4 to set
     */
    public void setFuryocodemei4(String furyocodemei4) {
        this.furyocodemei4 = furyocodemei4;
    }

    /**
     * @return the furyomeisai4
     */
    public String getFuryomeisai4() {
        return furyomeisai4;
    }

    /**
     * @param furyomeisai4 the furyomeisai4 to set
     */
    public void setFuryomeisai4(String furyomeisai4) {
        this.furyomeisai4 = furyomeisai4;
    }

    /**
     * @return the furyoritiu4
     */
    public String getFuryoritiu4() {
        return furyoritiu4;
    }

    /**
     * @param furyoritiu4 the furyoritiu4 to set
     */
    public void setFuryoritiu4(String furyoritiu4) {
        this.furyoritiu4 = furyoritiu4;
    }

    /**
     * @return the furyobunrui4
     */
    public String getFuryobunrui4() {
        return furyobunrui4;
    }

    /**
     * @param furyobunrui4 the furyobunrui4 to set
     */
    public void setFuryobunrui4(String furyobunrui4) {
        this.furyobunrui4 = furyobunrui4;
    }

    /**
     * @return the furyocode5
     */
    public String getFuryocode5() {
        return furyocode5;
    }

    /**
     * @param furyocode5 the furyocode5 to set
     */
    public void setFuryocode5(String furyocode5) {
        this.furyocode5 = furyocode5;
    }

    /**
     * 不良ｺｰﾄﾞ名5
     * @return the furyocodemei5
     */
    public String getFuryocodemei5() {
        return furyocodemei5;
    }

    /**
     * 不良ｺｰﾄﾞ名5
     * @param furyocodemei5 the furyocodemei5 to set
     */
    public void setFuryocodemei5(String furyocodemei5) {
        this.furyocodemei5 = furyocodemei5;
    }

    /**
     * @return the furyomeisai5
     */
    public String getFuryomeisai5() {
        return furyomeisai5;
    }

    /**
     * @param furyomeisai5 the furyomeisai5 to set
     */
    public void setFuryomeisai5(String furyomeisai5) {
        this.furyomeisai5 = furyomeisai5;
    }

    /**
     * @return the furyoritiu5
     */
    public String getFuryoritiu5() {
        return furyoritiu5;
    }

    /**
     * @param furyoritiu5 the furyoritiu5 to set
     */
    public void setFuryoritiu5(String furyoritiu5) {
        this.furyoritiu5 = furyoritiu5;
    }

    /**
     * @return the furyobunrui5
     */
    public String getFuryobunrui5() {
        return furyobunrui5;
    }

    /**
     * @param furyobunrui5 the furyobunrui5 to set
     */
    public void setFuryobunrui5(String furyobunrui5) {
        this.furyobunrui5 = furyobunrui5;
    }

    /**
     * @return the furyocode6
     */
    public String getFuryocode6() {
        return furyocode6;
    }

    /**
     * @param furyocode6 the furyocode6 to set
     */
    public void setFuryocode6(String furyocode6) {
        this.furyocode6 = furyocode6;
    }

    /**
     * 不良ｺｰﾄﾞ名6
     * @return the furyocodemei6
     */
    public String getFuryocodemei6() {
        return furyocodemei6;
    }

    /**
     * 不良ｺｰﾄﾞ名6
     * @param furyocodemei6 the furyocodemei6 to set
     */
    public void setFuryocodemei6(String furyocodemei6) {
        this.furyocodemei6 = furyocodemei6;
    }

    /**
     * @return the furyomeisai6
     */
    public String getFuryomeisai6() {
        return furyomeisai6;
    }

    /**
     * @param furyomeisai6 the furyomeisai6 to set
     */
    public void setFuryomeisai6(String furyomeisai6) {
        this.furyomeisai6 = furyomeisai6;
    }

    /**
     * @return the furyoritiu6
     */
    public String getFuryoritiu6() {
        return furyoritiu6;
    }

    /**
     * @param furyoritiu6 the furyoritiu6 to set
     */
    public void setFuryoritiu6(String furyoritiu6) {
        this.furyoritiu6 = furyoritiu6;
    }

    /**
     * @return the furyobunrui6
     */
    public String getFuryobunrui6() {
        return furyobunrui6;
    }

    /**
     * @param furyobunrui6 the furyobunrui6 to set
     */
    public void setFuryobunrui6(String furyobunrui6) {
        this.furyobunrui6 = furyobunrui6;
    }

    /**
     * @return the furyocode7
     */
    public String getFuryocode7() {
        return furyocode7;
    }

    /**
     * @param furyocode7 the furyocode7 to set
     */
    public void setFuryocode7(String furyocode7) {
        this.furyocode7 = furyocode7;
    }

    /**
     * 不良ｺｰﾄﾞ名7
     * @return the furyocodemei7
     */
    public String getFuryocodemei7() {
        return furyocodemei7;
    }

    /**
     * 不良ｺｰﾄﾞ名7
     * @param furyocodemei7 the furyocodemei7 to set
     */
    public void setFuryocodemei7(String furyocodemei7) {
        this.furyocodemei7 = furyocodemei7;
    }

    /**
     * @return the furyomeisai7
     */
    public String getFuryomeisai7() {
        return furyomeisai7;
    }

    /**
     * @param furyomeisai7 the furyomeisai7 to set
     */
    public void setFuryomeisai7(String furyomeisai7) {
        this.furyomeisai7 = furyomeisai7;
    }

    /**
     * @return the furyoritiu7
     */
    public String getFuryoritiu7() {
        return furyoritiu7;
    }

    /**
     * @param furyoritiu7 the furyoritiu7 to set
     */
    public void setFuryoritiu7(String furyoritiu7) {
        this.furyoritiu7 = furyoritiu7;
    }

    /**
     * @return the furyobunrui7
     */
    public String getFuryobunrui7() {
        return furyobunrui7;
    }

    /**
     * @param furyobunrui7 the furyobunrui7 to set
     */
    public void setFuryobunrui7(String furyobunrui7) {
        this.furyobunrui7 = furyobunrui7;
    }

    /**
     * @return the furyocode8
     */
    public String getFuryocode8() {
        return furyocode8;
    }

    /**
     * @param furyocode8 the furyocode8 to set
     */
    public void setFuryocode8(String furyocode8) {
        this.furyocode8 = furyocode8;
    }

    /**
     * 不良ｺｰﾄﾞ名8
     * @return the furyocodemei8
     */
    public String getFuryocodemei8() {
        return furyocodemei8;
    }

    /**
     * 不良ｺｰﾄﾞ名8
     * @param furyocodemei8 the furyocodemei8 to set
     */
    public void setFuryocodemei8(String furyocodemei8) {
        this.furyocodemei8 = furyocodemei8;
    }

    /**
     * @return the furyomeisai8
     */
    public String getFuryomeisai8() {
        return furyomeisai8;
    }

    /**
     * @param furyomeisai8 the furyomeisai8 to set
     */
    public void setFuryomeisai8(String furyomeisai8) {
        this.furyomeisai8 = furyomeisai8;
    }

    /**
     * @return the furyoritiu8
     */
    public String getFuryoritiu8() {
        return furyoritiu8;
    }

    /**
     * @param furyoritiu8 the furyoritiu8 to set
     */
    public void setFuryoritiu8(String furyoritiu8) {
        this.furyoritiu8 = furyoritiu8;
    }

    /**
     * @return the furyobunrui8
     */
    public String getFuryobunrui8() {
        return furyobunrui8;
    }

    /**
     * @param furyobunrui8 the furyobunrui8 to set
     */
    public void setFuryobunrui8(String furyobunrui8) {
        this.furyobunrui8 = furyobunrui8;
    }

    /**
     * @return the furyocode9
     */
    public String getFuryocode9() {
        return furyocode9;
    }

    /**
     * @param furyocode9 the furyocode9 to set
     */
    public void setFuryocode9(String furyocode9) {
        this.furyocode9 = furyocode9;
    }

    /**
     * 不良ｺｰﾄﾞ名9
     * @return the furyocodemei9
     */
    public String getFuryocodemei9() {
        return furyocodemei9;
    }

    /**
     * 不良ｺｰﾄﾞ名9
     * @param furyocodemei9 the furyocodemei9 to set
     */
    public void setFuryocodemei9(String furyocodemei9) {
        this.furyocodemei9 = furyocodemei9;
    }

    /**
     * @return the furyomeisai9
     */
    public String getFuryomeisai9() {
        return furyomeisai9;
    }

    /**
     * @param furyomeisai9 the furyomeisai9 to set
     */
    public void setFuryomeisai9(String furyomeisai9) {
        this.furyomeisai9 = furyomeisai9;
    }

    /**
     * @return the furyoritiu9
     */
    public String getFuryoritiu9() {
        return furyoritiu9;
    }

    /**
     * @param furyoritiu9 the furyoritiu9 to set
     */
    public void setFuryoritiu9(String furyoritiu9) {
        this.furyoritiu9 = furyoritiu9;
    }

    /**
     * @return the furyobunrui9
     */
    public String getFuryobunrui9() {
        return furyobunrui9;
    }

    /**
     * @param furyobunrui9 the furyobunrui9 to set
     */
    public void setFuryobunrui9(String furyobunrui9) {
        this.furyobunrui9 = furyobunrui9;
    }

    /**
     * @return the furyocode10
     */
    public String getFuryocode10() {
        return furyocode10;
    }

    /**
     * @param furyocode10 the furyocode10 to set
     */
    public void setFuryocode10(String furyocode10) {
        this.furyocode10 = furyocode10;
    }

    /**
     * @return the furyomeisai10
     */
    public String getFuryomeisai10() {
        return furyomeisai10;
    }

    /**
     * @param furyomeisai10 the furyomeisai10 to set
     */
    public void setFuryomeisai10(String furyomeisai10) {
        this.furyomeisai10 = furyomeisai10;
    }

    /**
     * 不良ｺｰﾄﾞ名10
     * @return the furyocodemei10
     */
    public String getFuryocodemei10() {
        return furyocodemei10;
    }

    /**
     * 不良ｺｰﾄﾞ名10
     * @param furyocodemei10 the furyocodemei10 to set
     */
    public void setFuryocodemei10(String furyocodemei10) {
        this.furyocodemei10 = furyocodemei10;
    }

    /**
     * @return the furyoritiu10
     */
    public String getFuryoritiu10() {
        return furyoritiu10;
    }

    /**
     * @param furyoritiu10 the furyoritiu10 to set
     */
    public void setFuryoritiu10(String furyoritiu10) {
        this.furyoritiu10 = furyoritiu10;
    }

    /**
     * @return the furyobunrui10
     */
    public String getFuryobunrui10() {
        return furyobunrui10;
    }

    /**
     * @param furyobunrui10 the furyobunrui10 to set
     */
    public void setFuryobunrui10(String furyobunrui10) {
        this.furyobunrui10 = furyobunrui10;
    }

    /**
     * @return the zaikono1
     */
    public String getZaikono1() {
        return zaikono1;
    }

    /**
     * @param zaikono1 the zaikono1 to set
     */
    public void setZaikono1(String zaikono1) {
        this.zaikono1 = zaikono1;
    }

    /**
     * @return the zaikono2
     */
    public String getZaikono2() {
        return zaikono2;
    }

    /**
     * @param zaikono2 the zaikono2 to set
     */
    public void setZaikono2(String zaikono2) {
        this.zaikono2 = zaikono2;
    }

    /**
     * @return the zaikono3
     */
    public String getZaikono3() {
        return zaikono3;
    }

    /**
     * @param zaikono3 the zaikono3 to set
     */
    public void setZaikono3(String zaikono3) {
        this.zaikono3 = zaikono3;
    }

    /**
     * @return the zaikono4
     */
    public String getZaikono4() {
        return zaikono4;
    }

    /**
     * @param zaikono4 the zaikono4 to set
     */
    public void setZaikono4(String zaikono4) {
        this.zaikono4 = zaikono4;
    }

    /**
     * @return the zaikono5
     */
    public String getZaikono5() {
        return zaikono5;
    }

    /**
     * @param zaikono5 the zaikono5 to set
     */
    public void setZaikono5(String zaikono5) {
        this.zaikono5 = zaikono5;
    }

    /**
     * @return the zaikono6
     */
    public String getZaikono6() {
        return zaikono6;
    }

    /**
     * @param zaikono6 the zaikono6 to set
     */
    public void setZaikono6(String zaikono6) {
        this.zaikono6 = zaikono6;
    }

    /**
     * @return the zaikono7
     */
    public String getZaikono7() {
        return zaikono7;
    }

    /**
     * @param zaikono7 the zaikono7 to set
     */
    public void setZaikono7(String zaikono7) {
        this.zaikono7 = zaikono7;
    }

    /**
     * @return the zaikono8
     */
    public String getZaikono8() {
        return zaikono8;
    }

    /**
     * @param zaikono8 the zaikono8 to set
     */
    public void setZaikono8(String zaikono8) {
        this.zaikono8 = zaikono8;
    }

    /**
     * @return the zaikono9
     */
    public String getZaikono9() {
        return zaikono9;
    }

    /**
     * @param zaikono9 the zaikono9 to set
     */
    public void setZaikono9(String zaikono9) {
        this.zaikono9 = zaikono9;
    }

    /**
     * @return the zaikono10
     */
    public String getZaikono10() {
        return zaikono10;
    }

    /**
     * @param zaikono10 the zaikono10 to set
     */
    public void setZaikono10(String zaikono10) {
        this.zaikono10 = zaikono10;
    }

    /**
     * @return the qakakuninsya
     */
    public String getQakakuninsya() {
        return qakakuninsya;
    }

    /**
     * @param qakakuninsya the qakakuninsya to set
     */
    public void setQakakuninsya(String qakakuninsya) {
        this.qakakuninsya = qakakuninsya;
    }

    /**
     * 確認者名
     * @return the qakakuninsyamei
     */
    public String getQakakuninsyamei() {
        return qakakuninsyamei;
    }

    /**
     * 確認者名
     * @param qakakuninsyamei the qakakuninsyamei to set
     */
    public void setQakakuninsyamei(String qakakuninsyamei) {
        this.qakakuninsyamei = qakakuninsyamei;
    }

    /**
     * @return the qakakuninnichiji
     */
    public java.sql.Date getQakakuninnichiji() {
        return qakakuninnichiji;
    }

    /**
     * @param qakakuninnichiji the qakakuninnichiji to set
     */
    public void setQakakuninnichiji(java.sql.Date qakakuninnichiji) {
        this.qakakuninnichiji = qakakuninnichiji;
    }

    /**
     * @return the sijiList
     */
    public List<KoteifuryoSiji> getSijiList() {
        return sijiList;
    }

    /**
     * @param sijiList the sijiList to set
     */
    public void setSijiList(List<KoteifuryoSiji> sijiList) {
        this.sijiList = sijiList;
    }

    /**
     * @return the kekkaList
     */
    public List<KoteifuryoKekka> getKekkaList() {
        return kekkaList;
    }

    /**
     * @param kekkaList the kekkaList to set
     */
    public void setKekkaList(List<KoteifuryoKekka> kekkaList) {
        this.kekkaList = kekkaList;
    }
    /**
     * @return the hanteiList
     */
    public List<KoteifuryoHantei> getHanteiList() {
        return hanteiList;
    }

    /**
     * @param hanteiList the hanteiList to set
     */
    public void setHanteiList(List<KoteifuryoHantei> hanteiList) {
        this.hanteiList = hanteiList;
    }

    /**
     * ﾗﾝｸ
     */
    private String rank;

    /**
     * 発行日
     */
    private java.sql.Date hakkobi;

    /**
     * 担当者
     */
    private String hakkosya;

    /**
     * 担当者名
     */
    private String hakkosyamei;

    /**
     * 得意先
     */
    private String tokuisaki;

    /**
     * 発見工程ID
     */
    private String hakkenkotei;

    /**
     * 発見工程名称
     */
    private String hakkenkoteimei;

    /**
     * 登録No
     */
    private String torokuno;

    /**
     * KCPNO
     */
    private String kcpno;

    /**
     * 仕掛数
     */
    private Long sikasuuryo;

    /**
     * OWNERｺｰﾄﾞ
     */
    private String ownercode;

    /**
     * Lot区分
     */
    private String lotkubuncode;

    /**
     * 製造LotNo
     */
    private String lotno;

    /**
     * 号機ｺｰﾄﾞ
     */
    private String goukicode;

    /**
     * 不良ｺｰﾄﾞ1
     */
    private String furyocode1;

    /**
     * 不良ｺｰﾄﾞ名1
     */
    private String furyocodemei1;

    /**
     * 不良名1
     */
    private String furyomeisai1;

    /**
     * 不良率1
     */
    private String furyoritiu1;

    /**
     * 不良分類1
     */
    private String furyobunrui1;

    /**
     * 不良ｺｰﾄﾞ2
     */
    private String furyocode2;

    /**
     * 不良ｺｰﾄﾞ名2
     */
    private String furyocodemei2;

    /**
     * 不良名2
     */
    private String furyomeisai2;

    /**
     * 不良率2
     */
    private String furyoritiu2;

    /**
     * 不良分類2
     */
    private String furyobunrui2;

    /**
     * 不良ｺｰﾄﾞ3
     */
    private String furyocode3;

    /**
     * 不良ｺｰﾄﾞ名3
     */
    private String furyocodemei3;

    /**
     * 不良名3
     */
    private String furyomeisai3;

    /**
     * 不良率3
     */
    private String furyoritiu3;

    /**
     * 不良分類3
     */
    private String furyobunrui3;

    /**
     * 不良ｺｰﾄﾞ4
     */
    private String furyocode4;

    /**
     * 不良ｺｰﾄﾞ名4
     */
    private String furyocodemei4;

    /**
     * 不良名4
     */
    private String furyomeisai4;

    /**
     * 不良率4
     */
    private String furyoritiu4;

    /**
     * 不良分類4
     */
    private String furyobunrui4;

    /**
     * 不良ｺｰﾄﾞ5
     */
    private String furyocode5;

    /**
     * 不良ｺｰﾄﾞ名5
     */
    private String furyocodemei5;

    /**
     * 不良名5
     */
    private String furyomeisai5;

    /**
     * 不良率5
     */
    private String furyoritiu5;

    /**
     * 不良分類5
     */
    private String furyobunrui5;

    /**
     * 不良ｺｰﾄﾞ6
     */
    private String furyocode6;

    /**
     * 不良ｺｰﾄﾞ名6
     */
    private String furyocodemei6;

    /**
     * 不良名6
     */
    private String furyomeisai6;

    /**
     * 不良率6
     */
    private String furyoritiu6;

    /**
     * 不良分類6
     */
    private String furyobunrui6;

    /**
     * 不良ｺｰﾄﾞ7
     */
    private String furyocode7;

    /**
     * 不良ｺｰﾄﾞ名7
     */
    private String furyocodemei7;

    /**
     * 不良名7
     */
    private String furyomeisai7;

    /**
     * 不良率7
     */
    private String furyoritiu7;

    /**
     * 不良分類7
     */
    private String furyobunrui7;

    /**
     * 不良ｺｰﾄﾞ8
     */
    private String furyocode8;

    /**
     * 不良ｺｰﾄﾞ名8
     */
    private String furyocodemei8;

    /**
     * 不良名8
     */
    private String furyomeisai8;

    /**
     * 不良率8
     */
    private String furyoritiu8;

    /**
     * 不良分類8
     */
    private String furyobunrui8;

    /**
     * 不良ｺｰﾄﾞ9
     */
    private String furyocode9;

    /**
     * 不良ｺｰﾄﾞ名9
     */
    private String furyocodemei9;

    /**
     * 不良名9
     */
    private String furyomeisai9;

    /**
     * 不良率9
     */
    private String furyoritiu9;

    /**
     * 不良分類9
     */
    private String furyobunrui9;

    /**
     * 不良ｺｰﾄﾞ10
     */
    private String furyocode10;

    /**
     * 不良ｺｰﾄﾞ名10
     */
    private String furyocodemei10;

    /**
     * 不良名10
     */
    private String furyomeisai10;

    /**
     * 不良率10
     */
    private String furyoritiu10;

    /**
     * 不良分類10
     */
    private String furyobunrui10;

    /**
     * 在庫No1
     */
    private String zaikono1;

    /**
     * 在庫No2
     */
    private String zaikono2;

    /**
     * 在庫No3
     */
    private String zaikono3;

    /**
     * 在庫No4
     */
    private String zaikono4;

    /**
     * 在庫No5
     */
    private String zaikono5;

    /**
     * 在庫No6
     */
    private String zaikono6;

    /**
     * 在庫No7
     */
    private String zaikono7;

    /**
     * 在庫No8
     */
    private String zaikono8;

    /**
     * 在庫No9
     */
    private String zaikono9;

    /**
     * 在庫No10
     */
    private String zaikono10;

    
    /**
     * 磁器QC確認者
     */
    private String jikiqckakuninsya;

    /**
     * 磁器QC確認者
     */
    private String jikiqckakuninsyamei;
    
    /**
     * 磁器QC確認日時
     */
    private java.sql.Date jikiqckakuninnichiji;

    /**
     * 確認者名
     */
    private String qakakuninsya;

    /**
     * 確認者名
     */
    private String qakakuninsyamei;
    /**
     * 日付
     */
    private java.sql.Date qakakuninnichiji;
 
    /**
     * 不良内容表示判定
     */
    private Boolean[] furyoDisp = new Boolean[10];

    /**
     * 不良内容表示判定設定
     */
    public void setHuryoDisp() {
        // 不良1
        if (!furyocode1.isEmpty() ||  !furyobunrui1.isEmpty() || !furyoritiu1.isEmpty() || !furyomeisai1.isEmpty() || !zaikono1.isEmpty()) {furyoDisp[0] = true;}
        else {furyoDisp[0] = false;}
        // 不良2
        if (!furyocode2.isEmpty() ||  !furyobunrui2.isEmpty() || !furyoritiu2.isEmpty() || !furyomeisai2.isEmpty() || !zaikono2.isEmpty()) {furyoDisp[1] = true;}
        else {furyoDisp[1] = false;}
        // 不良3
        if (!furyocode3.isEmpty() ||  !furyobunrui3.isEmpty() || !furyoritiu3.isEmpty() || !furyomeisai3.isEmpty() || !zaikono3.isEmpty()) {furyoDisp[2] = true;}
        else {furyoDisp[2] = false;}
         // 不良4
        if (!furyocode4.isEmpty() ||  !furyobunrui4.isEmpty() || !furyoritiu4.isEmpty() || !furyomeisai4.isEmpty() || !zaikono4.isEmpty()) {furyoDisp[3] = true;}
        else {furyoDisp[3] = false;}
        // 不良5
        if (!furyocode5.isEmpty() ||  !furyobunrui5.isEmpty() || !furyoritiu5.isEmpty() || !furyomeisai5.isEmpty() || !zaikono5.isEmpty()) {furyoDisp[4] = true;}
        else {furyoDisp[4] = false;}
        // 不良6
        if (!furyocode6.isEmpty() ||  !furyobunrui6.isEmpty() || !furyoritiu6.isEmpty() || !furyomeisai6.isEmpty() || !zaikono6.isEmpty()) {furyoDisp[5] = true;}
        else {furyoDisp[5] = false;}
        // 不良7
        if (!furyocode7.isEmpty() ||  !furyobunrui7.isEmpty() || !furyoritiu7.isEmpty() || !furyomeisai7.isEmpty() || !zaikono7.isEmpty()) {furyoDisp[6] = true;}
        else {furyoDisp[6] = false;}
        // 不良8
        if (!furyocode8.isEmpty() ||  !furyobunrui8.isEmpty() || !furyoritiu8.isEmpty() || !furyomeisai8.isEmpty() || !zaikono8.isEmpty()) {furyoDisp[7] = true;}
        else {furyoDisp[7] = false;}
        // 不良9
        if (!furyocode9.isEmpty() ||  !furyobunrui9.isEmpty() || !furyoritiu9.isEmpty() || !furyomeisai9.isEmpty() || !zaikono9.isEmpty()) {furyoDisp[8] = true;}
        else {furyoDisp[8] = false;}
        // 不良10
        if (!furyocode10.isEmpty() ||  !furyobunrui10.isEmpty() || !furyoritiu10.isEmpty() || !furyomeisai10.isEmpty() || !zaikono10.isEmpty()) {furyoDisp[9] = true;}
        else {furyoDisp[9] = false;}        
    }

    /**
     * 不良内容表示判定取得
     * @param idx(1～10)
     * @return True：表示、False：非表示
     */
    public Boolean isFuryoDisp(int idx) {
        return furyoDisp[idx - 1];
    }    
  
    /**
     * 工程不良指示一覧
     */
    private List<KoteifuryoSiji> sijiList;

    /**
     * 工程不良結果一覧
     */
    private List<KoteifuryoKekka> kekkaList;
    /**
     * 工程不良判定一覧
     */
    private List<KoteifuryoHantei> hanteiList;
    /**
     * 工程不良指示一覧1行分
     */
    public class KoteifuryoSiji {

        /**
         * @return the furyono
         */
        public String getFuryono() {
            return furyono;
        }

        /**
         * @param furyono the furyono to set
         */
        public void setFuryono(String furyono) {
            this.furyono = furyono;
        }

        /**
         * @return the sijisyacode
         */
        public String getSijisyacode() {
            return sijisyacode;
        }

        /**
         * @param sijisyacode the sijisyacode to set
         */
        public void setSijisyacode(String sijisyacode) {
            this.sijisyacode = sijisyacode;
        }

        /**
         * 工程不良指示.指示者名
         * @return the sijisyacodemei
         */
        public String getSijisyacodemei() {
            return sijisyacodemei;
        }

        /**
         * 工程不良指示.指示者名
         * @param sijisyacodemei the sijisyacodemei to set
         */
        public void setSijisyacodemei(String sijisyacodemei) {
            this.sijisyacodemei = sijisyacodemei;
        }

        /**
         * @return the sijibi
         */
        public java.sql.Date getSijibi() {
            return sijibi;
        }

        /**
         * @param sijibi the sijibi to set
         */
        public void setSijibi(java.sql.Date sijibi) {
            this.sijibi = sijibi;
        }

        /**
         * @return the syochikotei
         */
        public String getSyochikotei() {
            return syochikotei;
        }

        /**
         * @param syochikotei the syochikotei to set
         */
        public void setSyochikotei(String syochikotei) {
            this.syochikotei = syochikotei;
        }

        /**
         * 工程不良指示.処置工程名
         * @return the syochikoteimei
         */
        public String getSyochikoteimei() {
            return syochikoteimei;
        }

        /**
         * 工程不良指示.処置工程名
         * @param syochikoteimei the syochikoteimei to set
         */
        public void setSyochikoteimei(String syochikoteimei) {
            this.syochikoteimei = syochikoteimei;
        }

        /**
         * @return the sijinaiyo
         */
        public String getSijinaiyo() {
            return sijinaiyo;
        }

        /**
         * @param sijinaiyo the sijinaiyo to set
         */
        public void setSijinaiyo(String sijinaiyo) {
            this.sijinaiyo = sijinaiyo;
        }

        /**
         * 工程不良指示.不良No
         */
        private String furyono;

        /**
         * 工程不良指示.指示者ｺｰﾄﾞ
         */
        private String sijisyacode;

        /**
         * 工程不良指示.指示者名
         */
        private String sijisyacodemei;

        /**
         * 工程不良指示.指示日
         */
        private java.sql.Date sijibi;

        /**
         * 工程不良指示.処置工程ｺｰﾄﾞ
         */
        private String syochikotei;
        /**
         * 工程不良指示.処置工程名
         */
        private String syochikoteimei;

        /**
         * 工程不良指示.指示内容
         */
        private String sijinaiyo;

    }

    /**
     * 工程不良結果一覧1行分
     */
    public class KoteifuryoKekka {

        /**
         * @return the furyono
         */
        public String getFuryono() {
            return furyono;
        }

        /**
         * @param furyono the furyono to set
         */
        public void setFuryono(String furyono) {
            this.furyono = furyono;
        }

        /**
         * @return the syotisyacode
         */
        public String getSyotisyacode() {
            return syotisyacode;
        }

        /**
         * @param syotisyacode the syotisyacode to set
         */
        public void setSyotisyacode(String syotisyacode) {
            this.syotisyacode = syotisyacode;
        }

        /**
         * 工程不良結果.処置者名
         * @return the syotisyacodemei
         */
        public String getSyotisyacodemei() {
            return syotisyacodemei;
        }

        /**
         * 工程不良結果.処置者名
         * @param syotisyacodemei the syotisyacodemei to set
         */
        public void setSyotisyacodemei(String syotisyacodemei) {
            this.syotisyacodemei = syotisyacodemei;
        }

        /**
         * @return the syotibi
         */
        public java.sql.Date getSyotibi() {
            return syotibi;
        }

        /**
         * @param syotibi the syotibi to set
         */
        public void setSyotibi(java.sql.Date syotibi) {
            this.syotibi = syotibi;
        }

        /**
         * @return the syotinaiyo
         */
        public String getSyotinaiyo() {
            return syotinaiyo;
        }

        /**
         * @param syotinaiyo the syotinaiyo to set
         */
        public void setSyotinaiyo(String syotinaiyo) {
            this.syotinaiyo = syotinaiyo;
        }

        /**
         * @return the hantei
         */
        public String getHantei() {
            return hantei;
        }

        /**
         * @param hantei the hantei to set
         */
        public void setHantei(String hantei) {
            this.hantei = hantei;
        }

        /**
         * 工程不良結果.不良No
         */
        private String furyono;

        /**
         * 工程不良結果.処置者ｺｰﾄﾞ
         */
        private String syotisyacode;

        /**
         * 工程不良結果.処置者名
         */
        private String syotisyacodemei;

        /**
         * 工程不良結果.処置日
         */
        private java.sql.Date syotibi;

        /**
         * 工程不良結果.処置内容
         */
        private String syotinaiyo;

        /**
         * 工程不良結果.判定
         */
        private String hantei;

    }
    /**
     * 工程不良判定一覧1行分
     */
    public class KoteifuryoHantei {

        /**
         * @return the hantei
         */
        public String getHantei() {
            return hantei;
        }

        /**
         * @param hantei the hantei to set
         */
        public void setHantei(String hantei) {
            this.hantei = hantei;
        }

        /**
         * @return the hanteisyacode
         */
        public String getHanteisyacode() {
            return hanteisyacode;
        }

        /**
         * @param hanteisyacode the hanteisyacode to set
         */
        public void setHanteisyacode(String hanteisyacode) {
            this.hanteisyacode = hanteisyacode;
        }

        /**
         * 工程不良結果.判定者名
         * @return the hanteisyacodemei
         */
        public String getHanteisyacodemei() {
            return hanteisyacodemei;
        }

        /**
         * 工程不良結果.判定者名
         * @param hanteisyacodemei the hanteisyacodemei to set
         */
        public void setHanteisyacodemei(String hanteisyacodemei) {
            this.hanteisyacodemei = hanteisyacodemei;
        }

        /**
         * @return the hanteibi
         */
        public java.sql.Date getHanteibi() {
            return hanteibi;
        }

        /**
         * @param hanteibi the hanteibi to set
         */
        public void setHanteibi(java.sql.Date hanteibi) {
            this.hanteibi = hanteibi;
        }

        /**
         * @return the comment
         */
        public String getComment() {
            return comment;
        }

        /**
         * @param comment the comment to set
         */
        public void setComment(String comment) {
            this.comment = comment;
        }

        /**
         * @return the furyono
         */
        public String getFuryono() {
            return furyono;
        }

        /**
         * @param furyono the furyono to set
         */
        public void setFuryono(String furyono) {
            this.furyono = furyono;
        }

        
        /**
         * 工程不良結果.不良No
         */
        private String furyono;

        /**
         * 工程不良結果.判定者ｺｰﾄﾞ
         */
        private String hanteisyacode;

        /**
         * 工程不良結果.判定者名
         */
        private String hanteisyacodemei;

        /**
         * 工程不良結果.判定日
         */
        private java.sql.Date hanteibi;

        /**
         * 工程不良結果.ｺﾒﾝﾄ
         */
        private String comment;
        
        /**
         * 工程不良結果.判定
         */
        private String hantei;


    }
}
