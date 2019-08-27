/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2019/08/06<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	863 F.Zhang<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * ﾌﾟﾚｽ・仮ﾌﾟﾚｽ履歴検索画面のモデルクラスです。
 *
 * @author 863 F.Zhang
 * @since 2019/08/06
 */
public class GXHDO201B007Model implements Serializable{
    
    /** ﾛｯﾄNo. */
    private String lotno = "";
    /** KCPNO */
    private String kcpno = "";
    /** 開始日時 */
    private Timestamp kaisinichiji = null;
    /** 終了日時 */
    private Timestamp syuryonichiji = null;
    /** 温度(℃) */
    private Integer ondohyoji = null;
    /** 1次圧力(KN) */
    private Integer aturyokusetteiti = null;
    /** 1次時間(s) */
    private Integer kaatujikan = null;
    /** 仮ﾌﾟﾚｽ号機 */
    private String goki = "";
    /** 開始担当者 */
    private String kaisitantosya ="";
    /** 開始確認者 */
    private String kaisikakusya = "";
    /** 備考1 */
    private String biko1 = "";
    /** 作業区分 */
    private String sagyokubun = "";
    /** 処理ｾｯﾄ数(SET) */
    private Integer setsuu = null;
    /** 端子枚数(枚) */
    private Integer tansimaisuu = null;
    /** 圧力表示値 */
    private Integer aturyokuhyouji = null;
    /** ﾌﾟﾚｽｻｲﾄﾞ */
    private String pressside = "";
    /** 実績No */
    private Integer jissekion = null;
    /** 工程ｺｰﾄﾞ */
    private String koteicode = "";
    /** 緩衝材 */
    private String kansyouzai = "";
    /** 真空保持(min) */
    private Integer shinkuuhoji = null;
    /** 2次圧力(KN) */
    private Integer aturyokusetteiti2 = null;
    /** 2次時間(s) */
    private Integer kaatujikan2 = null;
    /** 3次圧力(KN) */
    private Integer aturyokusetteiti3 = null;
    /** 3次時間(s) */
    private Integer kaatujikan3 = null;
    /** 真空度 */
    private Integer shinkuudo = null;
    /** 保管条件 */
    private String hokanjouken = "";
    /** 終了担当者 */
    private String syuryotantosya = "";
    /** 良品ｾｯﾄ数(SET) */
    private Integer ryouhinSetsuu = null;
    /** 備考2 */
    private String biko2 = "";   
    
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
     * 実績No
     * @return the jissekion
     */
    public Integer getJissekion() {
        return jissekion;
    }

    /**
     * 実績No
     * @param jissekion the jissekion to set
     */
    public void setJissekion(Integer jissekion) {
        this.jissekion = jissekion;
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
     * 終了担当者
     * @return the syuryotantosya
     */
    public String getSyuryotantosya() {
        return syuryotantosya;
    }

    /**
     * 終了担当者
     * @param syuryotantosya the syuryotantosya to set
     */
    public void setSyuryotantosya(String syuryotantosya) {
        this.syuryotantosya = syuryotantosya;
    }
    
    /**
     * 開始確認者
     * @return the kaisikakusya
     */
    public String getKaisikakusya() {
        return kaisikakusya;
    }

    /**
     * 開始確認者
     * @param kaisikakusya the kaisikakusya to set
     */
    public void setKaisikakusya(String kaisikakusya) {
        this.kaisikakusya = kaisikakusya;
    }
    
    /**
     * 開始担当者
     * @return the kaisitantosya
     */
    public String getKaisitantosya() {
        return kaisitantosya;
    }

    /**
     * 開始担当者
     * @param kaisitantosya the kaisitantosya to set
     */
    public void setKaisitantosya(String kaisitantosya) {
        this.kaisitantosya = kaisitantosya;
    }

    /**
     * 温度(℃)
     * @return the ondohyoji
     */
    public Integer getOndohyoji() {
        return ondohyoji;
    }

    /**
     * 温度(℃)
     * @param ondohyoji the ondohyoji to set
     */
    public void setOndohyoji(Integer ondohyoji) {
        this.ondohyoji = ondohyoji;
    }

    /**
     * 1次圧力(KN)
     * @return the aturyokusetteiti
     */
    public Integer getAturyokusetteiti() {
        return aturyokusetteiti;
    }

    /**
     * 1次圧力(KN)
     * @param aturyokusetteiti the aturyokusetteiti to set
     */
    public void setAturyokusetteiti(Integer aturyokusetteiti) {
        this.aturyokusetteiti = aturyokusetteiti;
    }

    /**
     * 1次時間(s)
     * @return the kaatujikan
     */
    public Integer getKaatujikan() {
        return kaatujikan;
    }

    /**
     * 1次時間(s)
     * @param kaatujikan the kaatujikan to set
     */
    public void setKaatujikan(Integer kaatujikan) {
        this.kaatujikan = kaatujikan;
    }

    /**
     * 仮ﾌﾟﾚｽ号機
     * @return the goki
     */
    public String getGoki() {
        return goki;
    }

    /**
     * 仮ﾌﾟﾚｽ号機
     * @param goki the goki to set
     */
    public void setGoki(String goki) {
        this.goki = goki;
    }

    /**
     * 作業区分
     * @return the sagyokubun
     */
    public String getSagyokubun() {
        return sagyokubun;
    }

    /**
     * 作業区分
     * @param sagyokubun the sagyokubun to set
     */
    public void setSagyokubun(String sagyokubun) {
        this.sagyokubun = sagyokubun;
    }

    /**
     * 処理ｾｯﾄ数(SET)
     * @return the setsuu
     */
    public Integer getSetsuu() {
        return setsuu;
    }

    /**
     * 処理ｾｯﾄ数(SET)
     * @param setsuu the setsuu to set
     */
    public void setSetsuu(Integer setsuu) {
        this.setsuu = setsuu;
    }

    /**
     * 端子枚数(枚)
     * @return the tansimaisuu
     */
    public Integer getTansimaisuu() {
        return tansimaisuu;
    }

    /**
     * 端子枚数(枚)
     * @param tansimaisuu the tansimaisuu to set
     */
    public void setTansimaisuu(Integer tansimaisuu) {
        this.tansimaisuu = tansimaisuu;
    }

    /**
     * 圧力表示値
     * @return the aturyokuhyouji
     */
    public Integer getAturyokuhyouji() {
        return aturyokuhyouji;
    }

    /**
     * 圧力表示値
     * @param aturyokuhyouji the aturyokuhyouji to set
     */
    public void setAturyokuhyouji(Integer aturyokuhyouji) {
        this.aturyokuhyouji = aturyokuhyouji;
    }

    /**
     * ﾌﾟﾚｽｻｲﾄﾞ
     * @return the pressside
     */
    public String getPressside() {
        return pressside;
    }

    /**
     * ﾌﾟﾚｽｻｲﾄﾞ
     * @param pressside the pressside to set
     */
    public void setPressside(String pressside) {
        this.pressside = pressside;
    }

    /**
     * 工程ｺｰﾄﾞ
     * @return the koteicode
     */
    public String getKoteicode() {
        return koteicode;
    }

    /**
     * 工程ｺｰﾄﾞ
     * @param koteicode the koteicode to set
     */
    public void setKoteicode(String koteicode) {
        this.koteicode = koteicode;
    }

    /**
     * 緩衝材
     * @return the kansyouzai
     */
    public String getKansyouzai() {
        return kansyouzai;
    }

    /**
     * 緩衝材
     * @param kansyouzai the kansyouzai to set
     */
    public void setKansyouzai(String kansyouzai) {
        this.kansyouzai = kansyouzai;
    }

    /**
     * 真空保持(min)
     * @return the shinkuuhoji
     */
    public Integer getShinkuuhoji() {
        return shinkuuhoji;
    }

    /**
     * 真空保持(min)
     * @param shinkuuhoji the shinkuuhoji to set
     */
    public void setShinkuuhoji(Integer shinkuuhoji) {
        this.shinkuuhoji = shinkuuhoji;
    }

    /**
     * 2次圧力(KN)
     * @return the aturyokusetteiti2
     */
    public Integer getAturyokusetteiti2() {
        return aturyokusetteiti2;
    }

    /**
     * 2次圧力(KN)
     * @param aturyokusetteiti2 the aturyokusetteiti2 to set
     */
    public void setAturyokusetteiti2(Integer aturyokusetteiti2) {
        this.aturyokusetteiti2 = aturyokusetteiti2;
    }

    /**
     * 2次時間(s)
     * @return the kaatujikan2
     */
    public Integer getKaatujikan2() {
        return kaatujikan2;
    }

    /**
     * 2次時間(s)
     * @param kaatujikan2 the kaatujikan2 to set
     */
    public void setKaatujikan2(Integer kaatujikan2) {
        this.kaatujikan2 = kaatujikan2;
    }

    /**
     * 3次圧力(KN)
     * @return the aturyokusetteiti3
     */
    public Integer getAturyokusetteiti3() {
        return aturyokusetteiti3;
    }

    /**
     * 3次圧力(KN)
     * @param aturyokusetteiti3 the aturyokusetteiti3 to set
     */
    public void setAturyokusetteiti3(Integer aturyokusetteiti3) {
        this.aturyokusetteiti3 = aturyokusetteiti3;
    }

    /**
     * 3次時間(s)
     * @return the kaatujikan3
     */
    public Integer getKaatujikan3() {
        return kaatujikan3;
    }

    /**
     * 3次時間(s)
     * @param kaatujikan3 the kaatujikan3 to set
     */
    public void setKaatujikan3(Integer kaatujikan3) {
        this.kaatujikan3 = kaatujikan3;
    }

    /**
     * 真空度
     * @return the shinkuudo
     */
    public Integer getShinkuudo() {
        return shinkuudo;
    }

    /**
     * 真空度
     * @param shinkuudo the shinkuudo to set
     */
    public void setShinkuudo(Integer shinkuudo) {
        this.shinkuudo = shinkuudo;
    }

    /**
     * 保管条件
     * @return the hokanjouken
     */
    public String getHokanjouken() {
        return hokanjouken;
    }

    /**
     * 保管条件
     * @param hokanjouken the hokanjouken to set
     */
    public void setHokanjouken(String hokanjouken) {
        this.hokanjouken = hokanjouken;
    }

    /**
     * 良品ｾｯﾄ数(SET)
     * @return the ryouhinSetsuu
     */
    public Integer getRyouhinSetsuu() {
        return ryouhinSetsuu;
    }

    /**
     * 良品ｾｯﾄ数(SET)
     * @param ryouhinSetsuu the ryouhinSetsuu to set
     */
    public void setRyouhinSetsuu(Integer ryouhinSetsuu) {
        this.ryouhinSetsuu = ryouhinSetsuu;
    }
    
}
