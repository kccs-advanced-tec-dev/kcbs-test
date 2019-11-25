/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/11/05<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	863 K.Zhang<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * sr_mksinkuukansou:ﾒｯｷ真空乾燥のモデルクラスです。
 *
 * @author 863 K.Zhang
 * @since  2019/11/05
 */
public class SrMksinkuukansou {
    /**
     * 号機
     */
    private String goukicode;
    
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
     * 回数
     */
    private Integer syorikaisuu;

    /**
     * KCPNO
     */
    private String kcpno;
    
    /**
     * 開始担当者
     */
    private String tantousyacode;
    
    /**
     * 乾燥時間
     */
    private String kansoujikan;

    /**
     * 温度
     */
    private String ondo;
    
    /**
     * 真空度
     */
    private String sinkuudo;
    
    /**
     * 開始日時
     */
    private Timestamp kaisinichiji;
    
    /**
     * 備考1
     */
    private String bikou;
    
    /**
     * 登録日時
     */
    private Timestamp tourokunichiji;
    
    /**
     * 更新日時
     */
    private Timestamp kousinnichiji;
    
    /**
     * 客先
     */
    private String tokuisaki;
    
    /**
     * ﾛｯﾄ区分
     */
    private String lotkubuncode;
    
    /**
     * ｵｰﾅｰ
     */
    private String ownercode;
    
    /**
     * 処理数
     */
    private Integer syorisuu;

    /**
     * 作業場所
     */
    private String sagyoubasyo;

    /**
     * ﾊﾞﾚﾙ洗浄開始日時
     */
    private Timestamp barrelkaishinichiji;
    
    /**
     * ﾊﾞﾚﾙ洗浄担当者
     */
    private String barreltantousya;
    
    /**
     * 備考2
     */
    private String bikou2;

    /**
     * revision
     */
    private Integer revision;

    /**
     * 削除ﾌﾗｸﾞ
     */
    private Integer deleteflag;
    
    /**
     * @return goukicode
     */
    public String getGoukicode() {
        return goukicode;
    }

    /**
     * @param goukicode セットする goukicode
     */
    public void setGoukicode(String goukicode) {
        this.goukicode = goukicode;
    }

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
     * @return syorikaisuu
     */
    public Integer getSyorikaisuu() {
        return syorikaisuu;
    }

    /**
     * @param syorikaisuu セットする syorikaisuu
     */
    public void setSyorikaisuu(Integer syorikaisuu) {
        this.syorikaisuu = syorikaisuu;
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
     * @return tantousyacode
     */
    public String getTantousyacode() {
        return tantousyacode;
    }

    /**
     * @param tantousyacode セットする tantousyacode
     */
    public void setTantousyacode(String tantousyacode) {
        this.tantousyacode = tantousyacode;
    }

    /**
     * @return kansoujikan
     */
    public String getKansoujikan() {
        return kansoujikan;
    }

    /**
     * @param kansoujikan セットする kansoujikan
     */
    public void setKansoujikan(String kansoujikan) {
        this.kansoujikan = kansoujikan;
    }

    /**
     * @return ondo
     */
    public String getOndo() {
        return ondo;
    }

    /**
     * @param ondo セットする ondo
     */
    public void setOndo(String ondo) {
        this.ondo = ondo;
    }

    /**
     * @return sinkuudo
     */
    public String getSinkuudo() {
        return sinkuudo;
    }

    /**
     * @param sinkuudo セットする sinkuudo
     */
    public void setSinkuudo(String sinkuudo) {
        this.sinkuudo = sinkuudo;
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
     * @return bikou
     */
    public String getBikou() {
        return bikou;
    }

    /**
     * @param bikou セットする bikou
     */
    public void setBikou(String bikou) {
        this.bikou = bikou;
    }

    /**
     * @return tourokunichiji
     */
    public Timestamp getTourokunichiji() {
        return tourokunichiji;
    }

    /**
     * @param tourokunichiji セットする tourokunichiji
     */
    public void setTourokunichiji(Timestamp tourokunichiji) {
        this.tourokunichiji = tourokunichiji;
    }

    /**
     * @return kousinnichiji
     */
    public Timestamp getKousinnichiji() {
        return kousinnichiji;
    }

    /**
     * @param kousinnichiji セットする kousinnichiji
     */
    public void setKousinnichiji(Timestamp kousinnichiji) {
        this.kousinnichiji = kousinnichiji;
    }
    
    /**
     * @return tokuisaki
     */
    public String getTokuisaki() {
        return tokuisaki;
    }

    /**
     * @param tokuisaki セットする tokuisaki
     */
    public void setTokuisaki(String tokuisaki) {
        this.tokuisaki = tokuisaki;
    }

    /**
     * @return lotkubuncode
     */
    public String getLotkubuncode() {
        return lotkubuncode;
    }

    /**
     * @param lotkubuncode セットする lotkubuncode
     */
    public void setLotkubuncode(String lotkubuncode) {
        this.lotkubuncode = lotkubuncode;
    }

    /**
     * @return ownercode
     */
    public String getOwnercode() {
        return ownercode;
    }

    /**
     * @param ownercode セットする ownercode
     */
    public void setOwnercode(String ownercode) {
        this.ownercode = ownercode;
    }

    /**
     * @return syorisuu
     */
    public Integer getSyorisuu() {
        return syorisuu;
    }

    /**
     * @param syorisuu セットする syorisuu
     */
    public void setSyorisuu(Integer syorisuu) {
        this.syorisuu = syorisuu;
    }
    
    /**
     * @return sagyoubasyo
     */
    public String getSagyoubasyo() {
        return sagyoubasyo;
    }

    /**
     * @param sagyoubasyo セットする sagyoubasyo
     */
    public void setSagyoubasyo(String sagyoubasyo) {
        this.sagyoubasyo = sagyoubasyo;
    }

    /**
     * @return barrelkaishinichiji
     */
    public Timestamp getBarrelkaishinichiji() {
        return barrelkaishinichiji;
    }

    /**
     * @param barrelkaishinichiji セットする barrelkaishinichiji
     */
    public void setBarrelkaishinichiji(Timestamp barrelkaishinichiji) {
        this.barrelkaishinichiji = barrelkaishinichiji;
    }

    /**
     * @return barreltantousya
     */
    public String getBarreltantousya() {
        return barreltantousya;
    }

    /**
     * @param barreltantousya セットする barreltantousya
     */
    public void setBarreltantousya(String barreltantousya) {
        this.barreltantousya = barreltantousya;
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
     * @return revision
     */
    public Integer getRevision() {
        return revision;
    }

    /**
     * @param revision セットする revision
     */
    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    /**
     * @return deleteflag
     */
    public Integer getDeleteflag() {
        return deleteflag;
    }

    /**
     * @param deleteflag セットする deleteflag
     */
    public void setDeleteflag(Integer deleteflag) {
        this.deleteflag = deleteflag;
    }

    
}