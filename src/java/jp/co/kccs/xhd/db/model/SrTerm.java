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
 * 変更日	2019/09/02<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * <br>
 * 変更日	2020/09/08<br>
 * 計画書No	K2008-DS002<br>
 * 変更者	863 zhangjinyan<br>
 * 変更理由	仕様変更<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * SR_TERM(外部電極塗布)のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2019/09/02
 */
public class SrTerm {
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
     * ﾛｯﾄﾌﾟﾚ
     */
    private String lotpre;

    /**
     * KCPNO
     */
    private String kcpno;

    /**
     * 数量
     */
    private Integer suuryou;

    /**
     * 客先
     */
    private String kyakusaki;

    /**
     * 作業場所
     */
    private String sagyobasyo;

    /**
     * 号機1
     */
    private String gouki1;

    /**
     * 条件設定者1
     */
    private String setteisya1;

    /**
     * ﾍﾟｰｽﾄ品名
     */
    private String pastehinmei;

    /**
     * ﾍﾟｰｽﾄﾛｯﾄNo
     */
    private String pastelotno;

    /**
     * ﾍﾟｰｽﾄ再生回数
     */
    private Integer pastesaiseikaisuu;

    /**
     * ﾍﾟｰｽﾄ粘度
     */
    private Integer pastenendo;

    /**
     * 開始日時
     */
    private Timestamp startdatetime;

    /**
     * 終了日時
     */
    private Timestamp enddatetime;

    /**
     * P寸法AVE
     */
    private BigDecimal psunpouave2;

    /**
     * 端面厚み
     */
    private BigDecimal tanmenatsumi2;

    /**
     * DIP治具ｻｲｽﾞ
     */
    private Integer dipjigusize;

    /**
     * DIP治具枚数
     */
    private Integer dipjigumaisuu;

    /**
     * DIP後外観結果
     */
    private String dipgogaikankekka;

    /**
     * 備考1
     */
    private String bikou1;

    /**
     * 備考2
     */
    private String bikou2;

    /**
     * L寸法AVE
     */
    private BigDecimal lsunpouave2;

    /**
     * L寸法MIN
     */
    private BigDecimal lsunpoumin2;

    /**
     * L寸法MAX
     */
    private BigDecimal lsunpoumax2;

    /**
     * P寸法MIN
     */
    private BigDecimal psunpoumin2;

    /**
     * P寸法MAX
     */
    private BigDecimal psunpoumax2;

    /**
     * 設備種類
     */
    private String setubisyurui;

    /**
     * 塗布回数
     */
    private Integer tofukaisuu;

    /**
     * 保持ｼﾞｸﾞ
     */
    private String hojijigu;

    /**
     * 粘着ｼｰﾄﾛｯﾄ  1次側
     */
    private String nentyakusheetlot1;

    /**
     * 粘着ｼｰﾄﾛｯﾄ  2次側
     */
    private String nentyakusheetlot2;

    /**
     * 塗布ｼﾞｸﾞ取り個数
     */
    private Integer tofujigutorikosuu;

    /**
     * 開始担当者
     */
    private String starttantosyacode;

    /**
     * 開始確認者
     */
    private String startkakuninsyacode;

    /**
     * 重量
     */
    private BigDecimal juryou;

    /**
     * 処理個数
     */
    private Integer syorikosuu;

    /**
     * 終了担当者
     */
    private String endtantosyacode;

    /**
     * 1次ﾍﾟｰｽﾄ厚み設定値
     */
    private BigDecimal pasteatsumi1ji;

    /**
     * 2次ﾍﾟｰｽﾄ厚み設定値
     */
    private BigDecimal pasteatsumi2ji;

    /**
     * ｲﾝｸ厚みA
     */
    private Integer atsumiinkua;

    /**
     * ｲﾝｸ厚みB
     */
    private Integer atsumiinkub;

    /**
     * 回数
     */
    private Integer kaisuu;

    /**
     * 登録日時
     */
    private Timestamp torokunichiji;

    /**
     * 更新日時
     */
    private Timestamp kosinnichiji;

    /**
     * revision
     */
    private Integer revision;

    /**
     * 削除ﾌﾗｸﾞ
     */
    private Integer deleteflag;
    
    /**
     * 処理数
     */
    private Integer syorisuu;
    
    /**
     * 受入れ単位重量
     */
    private BigDecimal ukeiretannijyuryo;
    
    /**
     * 受入れ総重量
     */
    private BigDecimal ukeiresoujyuryou;
    
    /**
     * 磁器L寸法(MAX)
     */
    private BigDecimal jikilsunpoumax;
    
    /**
     * 磁器L寸法(MIN)
     */
    private BigDecimal jikilsunpoumin;
    
    /**
     * 磁器W寸法(MAX)
     */
    private BigDecimal jikiwsunpoumax;
    
    /**
     * 磁器W寸法(MIN)
     */
    private BigDecimal jikiwsunpoumin;
    
    /**
     * 磁器T寸法(MAX)
     */
    private BigDecimal jikitsunpoumax;
    
    /**
     * 磁器T寸法(MIN)
     */
    private BigDecimal jikitsunpoumin;
    
    /**
     * P寸法1
     */
    private Integer psunpou2a;

    /**
     * P寸法2
     */
    private Integer psunpou2b;

    /**
     * P寸法3
     */
    private Integer psunpou2c;

    /**
     * P寸法4
     */
    private Integer psunpou2d;

    /**
     * P寸法5
     */
    private Integer psunpou2e;

    /**
     * P寸法6
     */
    private Integer psunpou2f;

    /**
     * P寸法7
     */
    private Integer psunpou2g;

    /**
     * P寸法8
     */
    private Integer psunpou2h;

    /**
     * P寸法9
     */
    private Integer psunpou2i;

    /**
     * P寸法10
     */
    private Integer psunpou2j;

    /**
     * P寸法11
     */
    private Integer psunpou2k;

    /**
     * P寸法12
     */
    private Integer psunpou2l;

    /**
     * P寸法13
     */
    private Integer psunpou2m;

    /**
     * P寸法14
     */
    private Integer psunpou2n;

    /**
     * P寸法15
     */
    private Integer psunpou2o;

    /**
     * P寸法16
     */
    private Integer psunpou2p;

    /**
     * P寸法17
     */
    private Integer psunpou2q;

    /**
     * P寸法18
     */
    private Integer psunpou2r;

    /**
     * P寸法19
     */
    private Integer psunpou2s;

    /**
     * P寸法20
     */
    private Integer psunpou2t;

    /**
     * P寸法21
     */
    private Integer psunpou2u;

    /**
     * P寸法22
     */
    private Integer psunpou2v;

    /**
     * P寸法23
     */
    private Integer psunpou2w;

    /**
     * P寸法24
     */
    private Integer psunpou2x;

    /**
     * P寸法25
     */
    private Integer psunpou2y;

    /**
     * P寸法26
     */
    private Integer psunpou2z;

    /**
     * P寸法27
     */
    private Integer psunpou2aa;

    /**
     * P寸法28
     */
    private Integer psunpou2ab;

    /**
     * P寸法29
     */
    private Integer psunpou2ac;

    /**
     * P寸法30
     */
    private Integer psunpou2ad;

    /**
     * P寸法31
     */
    private Integer psunpou2ae;

    /**
     * P寸法32
     */
    private Integer psunpou2af;

    /**
     * P寸法33
     */
    private Integer psunpou2ag;

    /**
     * P寸法34
     */
    private Integer psunpou2ah;

    /**
     * P寸法35
     */
    private Integer psunpou2ai;

    /**
     * P寸法36
     */
    private Integer psunpou2aj;

    /**
     * P寸法37
     */
    private Integer psunpou2ak;

    /**
     * P寸法38
     */
    private Integer psunpou2al;

    /**
     * P寸法39
     */
    private Integer psunpou2am;

    /**
     * P寸法40
     */
    private Integer psunpou2an;

    /**
     * L寸法1
     */
    private Integer lsunpou2a;

    /**
     * L寸法2
     */
    private Integer lsunpou2b;

    /**
     * L寸法3
     */
    private Integer lsunpou2c;

    /**
     * L寸法4
     */
    private Integer lsunpou2d;

    /**
     * L寸法5
     */
    private Integer lsunpou2e;

    /**
     * L寸法6
     */
    private Integer lsunpou2f;

    /**
     * L寸法7
     */
    private Integer lsunpou2g;

    /**
     * L寸法8
     */
    private Integer lsunpou2h;

    /**
     * L寸法9
     */
    private Integer lsunpou2i;

    /**
     * L寸法10
     */
    private Integer lsunpou2j;

    /**
     * L寸法11
     */
    private Integer lsunpou2k;

    /**
     * L寸法12
     */
    private Integer lsunpou2l;

    /**
     * L寸法13
     */
    private Integer lsunpou2m;

    /**
     * L寸法14
     */
    private Integer lsunpou2n;

    /**
     * L寸法15
     */
    private Integer lsunpou2o;

    /**
     * L寸法16
     */
    private Integer lsunpou2p;

    /**
     * L寸法17
     */
    private Integer lsunpou2q;

    /**
     * L寸法18
     */
    private Integer lsunpou2r;

    /**
     * L寸法19
     */
    private Integer lsunpou2s;

    /**
     * L寸法20
     */
    private Integer lsunpou2t;

    /**
     * L寸法21
     */
    private Integer lsunpou2u;

    /**
     * L寸法22
     */
    private Integer lsunpou2v;

    /**
     * L寸法23
     */
    private Integer lsunpou2w;

    /**
     * L寸法24
     */
    private Integer lsunpou2x;

    /**
     * L寸法25
     */
    private Integer lsunpou2y;

    /**
     * L寸法26
     */
    private Integer lsunpou2z;

    /**
     * L寸法27
     */
    private Integer lsunpou2aa;

    /**
     * L寸法28
     */
    private Integer lsunpou2ab;

    /**
     * L寸法29
     */
    private Integer lsunpou2ac;

    /**
     * L寸法30
     */
    private Integer lsunpou2ad;

    /**
     * L寸法31
     */
    private Integer lsunpou2ae;

    /**
     * L寸法32
     */
    private Integer lsunpou2af;

    /**
     * L寸法33
     */
    private Integer lsunpou2ag;

    /**
     * L寸法34
     */
    private Integer lsunpou2ah;

    /**
     * L寸法35
     */
    private Integer lsunpou2ai;

    /**
     * L寸法36
     */
    private Integer lsunpou2aj;

    /**
     * L寸法37
     */
    private Integer lsunpou2ak;

    /**
     * L寸法38
     */
    private Integer lsunpou2al;

    /**
     * L寸法39
     */
    private Integer lsunpou2am;

    /**
     * L寸法40
     */
    private Integer lsunpou2an;

    /**
     * WT寸法1
     */
    private Integer wtsunpou2a;

    /**
     * WT寸法2
     */
    private Integer wtsunpou2b;

    /**
     * WT寸法3
     */
    private Integer wtsunpou2c;

    /**
     * WT寸法4
     */
    private Integer wtsunpou2d;

    /**
     * WT寸法5
     */
    private Integer wtsunpou2e;

    /**
     * WT寸法6
     */
    private Integer wtsunpou2f;

    /**
     * WT寸法7
     */
    private Integer wtsunpou2g;

    /**
     * WT寸法8
     */
    private Integer wtsunpou2h;

    /**
     * WT寸法9
     */
    private Integer wtsunpou2i;

    /**
     * WT寸法10
     */
    private Integer wtsunpou2j;

    /**
     * WT寸法11
     */
    private Integer wtsunpou2k;

    /**
     * WT寸法12
     */
    private Integer wtsunpou2l;

    /**
     * WT寸法13
     */
    private Integer wtsunpou2m;

    /**
     * WT寸法14
     */
    private Integer wtsunpou2n;

    /**
     * WT寸法15
     */
    private Integer wtsunpou2o;

    /**
     * WT寸法16
     */
    private Integer wtsunpou2p;

    /**
     * WT寸法17
     */
    private Integer wtsunpou2q;

    /**
     * WT寸法18
     */
    private Integer wtsunpou2r;

    /**
     * WT寸法19
     */
    private Integer wtsunpou2s;

    /**
     * WT寸法20
     */
    private Integer wtsunpou2t;

    /**
     * WT寸法21
     */
    private Integer wtsunpou2u;

    /**
     * WT寸法22
     */
    private Integer wtsunpou2v;

    /**
     * WT寸法23
     */
    private Integer wtsunpou2w;

    /**
     * WT寸法24
     */
    private Integer wtsunpou2x;

    /**
     * WT寸法25
     */
    private Integer wtsunpou2y;

    /**
     * WT寸法26
     */
    private Integer wtsunpou2z;

    /**
     * WT寸法27
     */
    private Integer wtsunpou2aa;

    /**
     * WT寸法28
     */
    private Integer wtsunpou2ab;

    /**
     * WT寸法29
     */
    private Integer wtsunpou2ac;

    /**
     * WT寸法30
     */
    private Integer wtsunpou2ad;

    /**
     * WT寸法31
     */
    private Integer wtsunpou2ae;

    /**
     * WT寸法32
     */
    private Integer wtsunpou2af;

    /**
     * WT寸法33
     */
    private Integer wtsunpou2ag;

    /**
     * WT寸法34
     */
    private Integer wtsunpou2ah;

    /**
     * WT寸法35
     */
    private Integer wtsunpou2ai;

    /**
     * WT寸法36
     */
    private Integer wtsunpou2aj;

    /**
     * WT寸法37
     */
    private Integer wtsunpou2ak;

    /**
     * WT寸法38
     */
    private Integer wtsunpou2al;

    /**
     * WT寸法39
     */
    private Integer wtsunpou2am;

    /**
     * WT寸法40
     */
    private Integer wtsunpou2an;
    
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
     * @return suuryou
     */
    public Integer getSuuryou() {
        return suuryou;
    }

    /**
     * @param suuryou セットする suuryou
     */
    public void setSuuryou(Integer suuryou) {
        this.suuryou = suuryou;
    }

    /**
     * @return kyakusaki
     */
    public String getKyakusaki() {
        return kyakusaki;
    }

    /**
     * @param kyakusaki セットする kyakusaki
     */
    public void setKyakusaki(String kyakusaki) {
        this.kyakusaki = kyakusaki;
    }

    /**
     * @return sagyobasyo
     */
    public String getSagyobasyo() {
        return sagyobasyo;
    }

    /**
     * @param sagyobasyo セットする sagyobasyo
     */
    public void setSagyobasyo(String sagyobasyo) {
        this.sagyobasyo = sagyobasyo;
    }

    /**
     * @return gouki1
     */
    public String getGouki1() {
        return gouki1;
    }

    /**
     * @param gouki1 セットする gouki1
     */
    public void setGouki1(String gouki1) {
        this.gouki1 = gouki1;
    }

    /**
     * @return setteisya1
     */
    public String getSetteisya1() {
        return setteisya1;
    }

    /**
     * @param setteisya1 セットする setteisya1
     */
    public void setSetteisya1(String setteisya1) {
        this.setteisya1 = setteisya1;
    }

    /**
     * @return pastehinmei
     */
    public String getPastehinmei() {
        return pastehinmei;
    }

    /**
     * @param pastehinmei セットする pastehinmei
     */
    public void setPastehinmei(String pastehinmei) {
        this.pastehinmei = pastehinmei;
    }

    /**
     * @return pastelotno
     */
    public String getPastelotno() {
        return pastelotno;
    }

    /**
     * @param pastelotno セットする pastelotno
     */
    public void setPastelotno(String pastelotno) {
        this.pastelotno = pastelotno;
    }

    /**
     * @return pastesaiseikaisuu
     */
    public Integer getPastesaiseikaisuu() {
        return pastesaiseikaisuu;
    }

    /**
     * @param pastesaiseikaisuu セットする pastesaiseikaisuu
     */
    public void setPastesaiseikaisuu(Integer pastesaiseikaisuu) {
        this.pastesaiseikaisuu = pastesaiseikaisuu;
    }

    /**
     * @return pastenendo
     */
    public Integer getPastenendo() {
        return pastenendo;
    }

    /**
     * @param pastenendo セットする pastenendo
     */
    public void setPastenendo(Integer pastenendo) {
        this.pastenendo = pastenendo;
    }

    /**
     * @return startdatetime
     */
    public Timestamp getStartdatetime() {
        return startdatetime;
    }

    /**
     * @param startdatetime セットする startdatetime
     */
    public void setStartdatetime(Timestamp startdatetime) {
        this.startdatetime = startdatetime;
    }

    /**
     * @return enddatetime
     */
    public Timestamp getEnddatetime() {
        return enddatetime;
    }

    /**
     * @param enddatetime セットする enddatetime
     */
    public void setEnddatetime(Timestamp enddatetime) {
        this.enddatetime = enddatetime;
    }

    /**
     * @return psunpouave2
     */
    public BigDecimal getPsunpouave2() {
        return psunpouave2;
    }

    /**
     * @param psunpouave2 セットする psunpouave2
     */
    public void setPsunpouave2(BigDecimal psunpouave2) {
        this.psunpouave2 = psunpouave2;
    }

    /**
     * @return tanmenatsumi2
     */
    public BigDecimal getTanmenatsumi2() {
        return tanmenatsumi2;
    }

    /**
     * @param tanmenatsumi2 セットする tanmenatsumi2
     */
    public void setTanmenatsumi2(BigDecimal tanmenatsumi2) {
        this.tanmenatsumi2 = tanmenatsumi2;
    }

    /**
     * @return dipjigusize
     */
    public Integer getDipjigusize() {
        return dipjigusize;
    }

    /**
     * @param dipjigusize セットする dipjigusize
     */
    public void setDipjigusize(Integer dipjigusize) {
        this.dipjigusize = dipjigusize;
    }

    /**
     * @return dipjigumaisuu
     */
    public Integer getDipjigumaisuu() {
        return dipjigumaisuu;
    }

    /**
     * @param dipjigumaisuu セットする dipjigumaisuu
     */
    public void setDipjigumaisuu(Integer dipjigumaisuu) {
        this.dipjigumaisuu = dipjigumaisuu;
    }

    /**
     * @return dipgogaikankekka
     */
    public String getDipgogaikankekka() {
        return dipgogaikankekka;
    }

    /**
     * @param dipgogaikankekka セットする dipgogaikankekka
     */
    public void setDipgogaikankekka(String dipgogaikankekka) {
        this.dipgogaikankekka = dipgogaikankekka;
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
     * @return lsunpouave2
     */
    public BigDecimal getLsunpouave2() {
        return lsunpouave2;
    }

    /**
     * @param lsunpouave2 セットする lsunpouave2
     */
    public void setLsunpouave2(BigDecimal lsunpouave2) {
        this.lsunpouave2 = lsunpouave2;
    }

    /**
     * @return lsunpoumin2
     */
    public BigDecimal getLsunpoumin2() {
        return lsunpoumin2;
    }

    /**
     * @param lsunpoumin2 セットする lsunpoumin2
     */
    public void setLsunpoumin2(BigDecimal lsunpoumin2) {
        this.lsunpoumin2 = lsunpoumin2;
    }
    
    /**
     * @return lsunpoumax2
     */
    public BigDecimal getLsunpoumax2() {
        return lsunpoumax2;
    }

    /**
     * @param lsunpoumax2 セットする lsunpoumax2
     */
    public void setLsunpoumax2(BigDecimal lsunpoumax2) {
        this.lsunpoumax2 = lsunpoumax2;
    }
    
    /**
     * @return psunpoumin2
     */
    public BigDecimal getPsunpoumin2() {
        return psunpoumin2;
    }

    /**
     * @param psunpoumin2 セットする psunpoumin2
     */
    public void setPsunpoumin2(BigDecimal psunpoumin2) {
        this.psunpoumin2 = psunpoumin2;
    }

    /**
     * @return psunpoumax2
     */
    public BigDecimal getPsunpoumax2() {
        return psunpoumax2;
    }

    /**
     * @param psunpoumax2 セットする psunpoumax2
     */
    public void setPsunpoumax2(BigDecimal psunpoumax2) {
        this.psunpoumax2 = psunpoumax2;
    }

    /**
     * @return setubisyurui
     */
    public String getSetubisyurui() {
        return setubisyurui;
    }

    /**
     * @param setubisyurui セットする setubisyurui
     */
    public void setSetubisyurui(String setubisyurui) {
        this.setubisyurui = setubisyurui;
    }

    /**
     * @return tofukaisuu
     */
    public Integer getTofukaisuu() {
        return tofukaisuu;
    }

    /**
     * @param tofukaisuu セットする tofukaisuu
     */
    public void setTofukaisuu(Integer tofukaisuu) {
        this.tofukaisuu = tofukaisuu;
    }

    /**
     * @return hojijigu
     */
    public String getHojijigu() {
        return hojijigu;
    }

    /**
     * @param hojijigu セットする hojijigu
     */
    public void setHojijigu(String hojijigu) {
        this.hojijigu = hojijigu;
    }

    /**
     * @return nentyakusheetlot1
     */
    public String getNentyakusheetlot1() {
        return nentyakusheetlot1;
    }

    /**
     * @param nentyakusheetlot1 セットする nentyakusheetlot1
     */
    public void setNentyakusheetlot1(String nentyakusheetlot1) {
        this.nentyakusheetlot1 = nentyakusheetlot1;
    }

    /**
     * @return nentyakusheetlot2
     */
    public String getNentyakusheetlot2() {
        return nentyakusheetlot2;
    }

    /**
     * @param nentyakusheetlot2 セットする nentyakusheetlot2
     */
    public void setNentyakusheetlot2(String nentyakusheetlot2) {
        this.nentyakusheetlot2 = nentyakusheetlot2;
    }

    /**
     * @return tofujigutorikosuu
     */
    public Integer getTofujigutorikosuu() {
        return tofujigutorikosuu;
    }

    /**
     * @param tofujigutorikosuu セットする tofujigutorikosuu
     */
    public void setTofujigutorikosuu(Integer tofujigutorikosuu) {
        this.tofujigutorikosuu = tofujigutorikosuu;
    }

    /**
     * @return starttantosyacode
     */
    public String getStarttantosyacode() {
        return starttantosyacode;
    }

    /**
     * @param starttantosyacode セットする starttantosyacode
     */
    public void setStarttantosyacode(String starttantosyacode) {
        this.starttantosyacode = starttantosyacode;
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
     * @return juryou
     */
    public BigDecimal getJuryou() {
        return juryou;
    }

    /**
     * @param juryou セットする juryou
     */
    public void setJuryou(BigDecimal juryou) {
        this.juryou = juryou;
    }

    /**
     * @return syorikosuu
     */
    public Integer getSyorikosuu() {
        return syorikosuu;
    }

    /**
     * @param syorikosuu セットする syorikosuu
     */
    public void setSyorikosuu(Integer syorikosuu) {
        this.syorikosuu = syorikosuu;
    }

    /**
     * @return endtantosyacode
     */
    public String getEndtantosyacode() {
        return endtantosyacode;
    }

    /**
     * @param endtantosyacode セットする endtantosyacode
     */
    public void setEndtantosyacode(String endtantosyacode) {
        this.endtantosyacode = endtantosyacode;
    }

    /**
     * @return pasteatsumi1ji
     */
    public BigDecimal getPasteatsumi1ji() {
        return pasteatsumi1ji;
    }

    /**
     * @param pasteatsumi1ji セットする pasteatsumi1ji
     */
    public void setPasteatsumi1ji(BigDecimal pasteatsumi1ji) {
        this.pasteatsumi1ji = pasteatsumi1ji;
    }

    /**
     * @return pasteatsumi2ji
     */
    public BigDecimal getPasteatsumi2ji() {
        return pasteatsumi2ji;
    }

    /**
     * @param pasteatsumi2ji セットする pasteatsumi2ji
     */
    public void setPasteatsumi2ji(BigDecimal pasteatsumi2ji) {
        this.pasteatsumi2ji = pasteatsumi2ji;
    }

    /**
     * @return atsumiinkua
     */
    public Integer getAtsumiinkua() {
        return atsumiinkua;
    }

    /**
     * @param atsumiinkua セットする atsumiinkua
     */
    public void setAtsumiinkua(Integer atsumiinkua) {
        this.atsumiinkua = atsumiinkua;
    }

    /**
     * @return atsumiinkub
     */
    public Integer getAtsumiinkub() {
        return atsumiinkub;
    }

    /**
     * @param atsumiinkub セットする atsumiinkub
     */
    public void setAtsumiinkub(Integer atsumiinkub) {
        this.atsumiinkub = atsumiinkub;
    }

    /**
     * @return kaisuu
     */
    public Integer getKaisuu() {
        return kaisuu;
    }

    /**
     * @param kaisuu セットする kaisuu
     */
    public void setKaisuu(Integer kaisuu) {
        this.kaisuu = kaisuu;
    }

    /**
     * @return torokunichiji
     */
    public Timestamp getTorokunichiji() {
        return torokunichiji;
    }

    /**
     * @param torokunichiji セットする torokunichiji
     */
    public void setTorokunichiji(Timestamp torokunichiji) {
        this.torokunichiji = torokunichiji;
    }

    /**
     * @return kosinnichiji
     */
    public Timestamp getKosinnichiji() {
        return kosinnichiji;
    }

    /**
     * @param kosinnichiji セットする kosinnichiji
     */
    public void setKosinnichiji(Timestamp kosinnichiji) {
        this.kosinnichiji = kosinnichiji;
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
     * @return 
     */
    public BigDecimal getUkeiretannijyuryo() {
        return ukeiretannijyuryo;
    }

    /**
     * @param ukeiretannijyuryo 
     */
    public void setUkeiretannijyuryo(BigDecimal ukeiretannijyuryo) {
        this.ukeiretannijyuryo = ukeiretannijyuryo;
    }

    /**
     * @return 
     */
    public BigDecimal getUkeiresoujyuryou() {
        return ukeiresoujyuryou;
    }

    /**
     * @param ukeiresoujyuryou 
     */
    public void setUkeiresoujyuryou(BigDecimal ukeiresoujyuryou) {
        this.ukeiresoujyuryou = ukeiresoujyuryou;
    }

    /**
     * @return 
     */
    public BigDecimal getJikilsunpoumax() {
        return jikilsunpoumax;
    }

    /**
     * @param jikilsunpoumax 
     */
    public void setJikilsunpoumax(BigDecimal jikilsunpoumax) {
        this.jikilsunpoumax = jikilsunpoumax;
    }

    /**
     * @return 
     */
    public BigDecimal getJikilsunpoumin() {
        return jikilsunpoumin;
    }

    /**
     * @param jikilsunpoumin 
     */
    public void setJikilsunpoumin(BigDecimal jikilsunpoumin) {
        this.jikilsunpoumin = jikilsunpoumin;
    }

    /**
     * @return 
     */
    public BigDecimal getJikiwsunpoumax() {
        return jikiwsunpoumax;
    }

    /**
     * @param jikiwsunpoumax 
     */
    public void setJikiwsunpoumax(BigDecimal jikiwsunpoumax) {
        this.jikiwsunpoumax = jikiwsunpoumax;
    }

    /**
     * @return 
     */
    public BigDecimal getJikiwsunpoumin() {
        return jikiwsunpoumin;
    }

    /**
     * @param jikiwsunpoumin 
     */
    public void setJikiwsunpoumin(BigDecimal jikiwsunpoumin) {
        this.jikiwsunpoumin = jikiwsunpoumin;
    }

    /**
     * @return 
     */
    public BigDecimal getJikitsunpoumax() {
        return jikitsunpoumax;
    }

    /**
     * @param jikitsunpoumax 
     */
    public void setJikitsunpoumax(BigDecimal jikitsunpoumax) {
        this.jikitsunpoumax = jikitsunpoumax;
    }

    /**
     * @return 
     */
    public BigDecimal getJikitsunpoumin() {
        return jikitsunpoumin;
    }

    /**
     * @param jikitsunpoumin 
     */
    public void setJikitsunpoumin(BigDecimal jikitsunpoumin) {
        this.jikitsunpoumin = jikitsunpoumin;
    }

    /**
     * P寸法1
     * @return the psunpou2a
     */
    public Integer getPsunpou2a() {
        return psunpou2a;
    }

    /**
     * P寸法1
     * @param psunpou2a the psunpou2a to set
     */
    public void setPsunpou2a(Integer psunpou2a) {
        this.psunpou2a = psunpou2a;
    }

    /**
     * P寸法2
     * @return the psunpou2b
     */
    public Integer getPsunpou2b() {
        return psunpou2b;
    }

    /**
     * P寸法2
     * @param psunpou2b the psunpou2b to set
     */
    public void setPsunpou2b(Integer psunpou2b) {
        this.psunpou2b = psunpou2b;
    }

    /**
     * P寸法3
     * @return the psunpou2c
     */
    public Integer getPsunpou2c() {
        return psunpou2c;
    }

    /**
     * P寸法3
     * @param psunpou2c the psunpou2c to set
     */
    public void setPsunpou2c(Integer psunpou2c) {
        this.psunpou2c = psunpou2c;
    }

    /**
     * P寸法4
     * @return the psunpou2d
     */
    public Integer getPsunpou2d() {
        return psunpou2d;
    }

    /**
     * P寸法4
     * @param psunpou2d the psunpou2d to set
     */
    public void setPsunpou2d(Integer psunpou2d) {
        this.psunpou2d = psunpou2d;
    }

    /**
     * P寸法5
     * @return the psunpou2e
     */
    public Integer getPsunpou2e() {
        return psunpou2e;
    }

    /**
     * P寸法5
     * @param psunpou2e the psunpou2e to set
     */
    public void setPsunpou2e(Integer psunpou2e) {
        this.psunpou2e = psunpou2e;
    }

    /**
     * P寸法6
     * @return the psunpou2f
     */
    public Integer getPsunpou2f() {
        return psunpou2f;
    }

    /**
     * P寸法6
     * @param psunpou2f the psunpou2f to set
     */
    public void setPsunpou2f(Integer psunpou2f) {
        this.psunpou2f = psunpou2f;
    }

    /**
     * P寸法7
     * @return the psunpou2g
     */
    public Integer getPsunpou2g() {
        return psunpou2g;
    }

    /**
     * P寸法7
     * @param psunpou2g the psunpou2g to set
     */
    public void setPsunpou2g(Integer psunpou2g) {
        this.psunpou2g = psunpou2g;
    }

    /**
     * P寸法8
     * @return the psunpou2h
     */
    public Integer getPsunpou2h() {
        return psunpou2h;
    }

    /**
     * P寸法8
     * @param psunpou2h the psunpou2h to set
     */
    public void setPsunpou2h(Integer psunpou2h) {
        this.psunpou2h = psunpou2h;
    }

    /**
     * P寸法9
     * @return the psunpou2i
     */
    public Integer getPsunpou2i() {
        return psunpou2i;
    }

    /**
     * P寸法9
     * @param psunpou2i the psunpou2i to set
     */
    public void setPsunpou2i(Integer psunpou2i) {
        this.psunpou2i = psunpou2i;
    }

    /**
     * P寸法10
     * @return the psunpou2j
     */
    public Integer getPsunpou2j() {
        return psunpou2j;
    }

    /**
     * P寸法10
     * @param psunpou2j the psunpou2j to set
     */
    public void setPsunpou2j(Integer psunpou2j) {
        this.psunpou2j = psunpou2j;
    }

    /**
     * P寸法11
     * @return the psunpou2k
     */
    public Integer getPsunpou2k() {
        return psunpou2k;
    }

    /**
     * P寸法11
     * @param psunpou2k the psunpou2k to set
     */
    public void setPsunpou2k(Integer psunpou2k) {
        this.psunpou2k = psunpou2k;
    }

    /**
     * P寸法12
     * @return the psunpou2l
     */
    public Integer getPsunpou2l() {
        return psunpou2l;
    }

    /**
     * P寸法12
     * @param psunpou2l the psunpou2l to set
     */
    public void setPsunpou2l(Integer psunpou2l) {
        this.psunpou2l = psunpou2l;
    }

    /**
     * P寸法13
     * @return the psunpou2m
     */
    public Integer getPsunpou2m() {
        return psunpou2m;
    }

    /**
     * P寸法13
     * @param psunpou2m the psunpou2m to set
     */
    public void setPsunpou2m(Integer psunpou2m) {
        this.psunpou2m = psunpou2m;
    }

    /**
     * P寸法14
     * @return the psunpou2n
     */
    public Integer getPsunpou2n() {
        return psunpou2n;
    }

    /**
     * P寸法14
     * @param psunpou2n the psunpou2n to set
     */
    public void setPsunpou2n(Integer psunpou2n) {
        this.psunpou2n = psunpou2n;
    }

    /**
     * P寸法15
     * @return the psunpou2o
     */
    public Integer getPsunpou2o() {
        return psunpou2o;
    }

    /**
     * P寸法15
     * @param psunpou2o the psunpou2o to set
     */
    public void setPsunpou2o(Integer psunpou2o) {
        this.psunpou2o = psunpou2o;
    }

    /**
     * P寸法16
     * @return the psunpou2p
     */
    public Integer getPsunpou2p() {
        return psunpou2p;
    }

    /**
     * P寸法16
     * @param psunpou2p the psunpou2p to set
     */
    public void setPsunpou2p(Integer psunpou2p) {
        this.psunpou2p = psunpou2p;
    }

    /**
     * P寸法17
     * @return the psunpou2q
     */
    public Integer getPsunpou2q() {
        return psunpou2q;
    }

    /**
     * P寸法17
     * @param psunpou2q the psunpou2q to set
     */
    public void setPsunpou2q(Integer psunpou2q) {
        this.psunpou2q = psunpou2q;
    }

    /**
     * P寸法18
     * @return the psunpou2r
     */
    public Integer getPsunpou2r() {
        return psunpou2r;
    }

    /**
     * P寸法18
     * @param psunpou2r the psunpou2r to set
     */
    public void setPsunpou2r(Integer psunpou2r) {
        this.psunpou2r = psunpou2r;
    }

    /**
     * P寸法19
     * @return the psunpou2s
     */
    public Integer getPsunpou2s() {
        return psunpou2s;
    }

    /**
     * P寸法19
     * @param psunpou2s the psunpou2s to set
     */
    public void setPsunpou2s(Integer psunpou2s) {
        this.psunpou2s = psunpou2s;
    }

    /**
     * P寸法20
     * @return the psunpou2t
     */
    public Integer getPsunpou2t() {
        return psunpou2t;
    }

    /**
     * P寸法20
     * @param psunpou2t the psunpou2t to set
     */
    public void setPsunpou2t(Integer psunpou2t) {
        this.psunpou2t = psunpou2t;
    }

    /**
     * P寸法21
     * @return the psunpou2u
     */
    public Integer getPsunpou2u() {
        return psunpou2u;
    }

    /**
     * P寸法21
     * @param psunpou2u the psunpou2u to set
     */
    public void setPsunpou2u(Integer psunpou2u) {
        this.psunpou2u = psunpou2u;
    }

    /**
     * P寸法22
     * @return the psunpou2v
     */
    public Integer getPsunpou2v() {
        return psunpou2v;
    }

    /**
     * P寸法22
     * @param psunpou2v the psunpou2v to set
     */
    public void setPsunpou2v(Integer psunpou2v) {
        this.psunpou2v = psunpou2v;
    }

    /**
     * P寸法23
     * @return the psunpou2w
     */
    public Integer getPsunpou2w() {
        return psunpou2w;
    }

    /**
     * P寸法23
     * @param psunpou2w the psunpou2w to set
     */
    public void setPsunpou2w(Integer psunpou2w) {
        this.psunpou2w = psunpou2w;
    }

    /**
     * P寸法24
     * @return the psunpou2x
     */
    public Integer getPsunpou2x() {
        return psunpou2x;
    }

    /**
     * P寸法24
     * @param psunpou2x the psunpou2x to set
     */
    public void setPsunpou2x(Integer psunpou2x) {
        this.psunpou2x = psunpou2x;
    }

    /**
     * P寸法25
     * @return the psunpou2y
     */
    public Integer getPsunpou2y() {
        return psunpou2y;
    }

    /**
     * P寸法25
     * @param psunpou2y the psunpou2y to set
     */
    public void setPsunpou2y(Integer psunpou2y) {
        this.psunpou2y = psunpou2y;
    }

    /**
     * P寸法26
     * @return the psunpou2z
     */
    public Integer getPsunpou2z() {
        return psunpou2z;
    }

    /**
     * P寸法26
     * @param psunpou2z the psunpou2z to set
     */
    public void setPsunpou2z(Integer psunpou2z) {
        this.psunpou2z = psunpou2z;
    }

    /**
     * P寸法27
     * @return the psunpou2aa
     */
    public Integer getPsunpou2aa() {
        return psunpou2aa;
    }

    /**
     * P寸法27
     * @param psunpou2aa the psunpou2aa to set
     */
    public void setPsunpou2aa(Integer psunpou2aa) {
        this.psunpou2aa = psunpou2aa;
    }

    /**
     * P寸法28
     * @return the psunpou2ab
     */
    public Integer getPsunpou2ab() {
        return psunpou2ab;
    }

    /**
     * P寸法28
     * @param psunpou2ab the psunpou2ab to set
     */
    public void setPsunpou2ab(Integer psunpou2ab) {
        this.psunpou2ab = psunpou2ab;
    }

    /**
     * P寸法29
     * @return the psunpou2ac
     */
    public Integer getPsunpou2ac() {
        return psunpou2ac;
    }

    /**
     * P寸法29
     * @param psunpou2ac the psunpou2ac to set
     */
    public void setPsunpou2ac(Integer psunpou2ac) {
        this.psunpou2ac = psunpou2ac;
    }

    /**
     * P寸法30
     * @return the psunpou2ad
     */
    public Integer getPsunpou2ad() {
        return psunpou2ad;
    }

    /**
     * P寸法30
     * @param psunpou2ad the psunpou2ad to set
     */
    public void setPsunpou2ad(Integer psunpou2ad) {
        this.psunpou2ad = psunpou2ad;
    }

    /**
     * P寸法31
     * @return the psunpou2ae
     */
    public Integer getPsunpou2ae() {
        return psunpou2ae;
    }

    /**
     * P寸法31
     * @param psunpou2ae the psunpou2ae to set
     */
    public void setPsunpou2ae(Integer psunpou2ae) {
        this.psunpou2ae = psunpou2ae;
    }

    /**
     * P寸法32
     * @return the psunpou2af
     */
    public Integer getPsunpou2af() {
        return psunpou2af;
    }

    /**
     * P寸法32
     * @param psunpou2af the psunpou2af to set
     */
    public void setPsunpou2af(Integer psunpou2af) {
        this.psunpou2af = psunpou2af;
    }

    /**
     * P寸法33
     * @return the psunpou2ag
     */
    public Integer getPsunpou2ag() {
        return psunpou2ag;
    }

    /**
     * P寸法33
     * @param psunpou2ag the psunpou2ag to set
     */
    public void setPsunpou2ag(Integer psunpou2ag) {
        this.psunpou2ag = psunpou2ag;
    }

    /**
     * P寸法34
     * @return the psunpou2ah
     */
    public Integer getPsunpou2ah() {
        return psunpou2ah;
    }

    /**
     * P寸法34
     * @param psunpou2ah the psunpou2ah to set
     */
    public void setPsunpou2ah(Integer psunpou2ah) {
        this.psunpou2ah = psunpou2ah;
    }

    /**
     * P寸法35
     * @return the psunpou2ai
     */
    public Integer getPsunpou2ai() {
        return psunpou2ai;
    }

    /**
     * P寸法35
     * @param psunpou2ai the psunpou2ai to set
     */
    public void setPsunpou2ai(Integer psunpou2ai) {
        this.psunpou2ai = psunpou2ai;
    }

    /**
     * P寸法36
     * @return the psunpou2aj
     */
    public Integer getPsunpou2aj() {
        return psunpou2aj;
    }

    /**
     * P寸法36
     * @param psunpou2aj the psunpou2aj to set
     */
    public void setPsunpou2aj(Integer psunpou2aj) {
        this.psunpou2aj = psunpou2aj;
    }

    /**
     * P寸法37
     * @return the psunpou2ak
     */
    public Integer getPsunpou2ak() {
        return psunpou2ak;
    }

    /**
     * P寸法37
     * @param psunpou2ak the psunpou2ak to set
     */
    public void setPsunpou2ak(Integer psunpou2ak) {
        this.psunpou2ak = psunpou2ak;
    }

    /**
     * P寸法38
     * @return the psunpou2al
     */
    public Integer getPsunpou2al() {
        return psunpou2al;
    }

    /**
     * P寸法38
     * @param psunpou2al the psunpou2al to set
     */
    public void setPsunpou2al(Integer psunpou2al) {
        this.psunpou2al = psunpou2al;
    }

    /**
     * P寸法39
     * @return the psunpou2am
     */
    public Integer getPsunpou2am() {
        return psunpou2am;
    }

    /**
     * P寸法39
     * @param psunpou2am the psunpou2am to set
     */
    public void setPsunpou2am(Integer psunpou2am) {
        this.psunpou2am = psunpou2am;
    }

    /**
     * P寸法40
     * @return the psunpou2an
     */
    public Integer getPsunpou2an() {
        return psunpou2an;
    }

    /**
     * P寸法40
     * @param psunpou2an the psunpou2an to set
     */
    public void setPsunpou2an(Integer psunpou2an) {
        this.psunpou2an = psunpou2an;
    }

    /**
     * L寸法1
     * @return the lsunpou2a
     */
    public Integer getLsunpou2a() {
        return lsunpou2a;
    }

    /**
     * L寸法1
     * @param lsunpou2a the lsunpou2a to set
     */
    public void setLsunpou2a(Integer lsunpou2a) {
        this.lsunpou2a = lsunpou2a;
    }

    /**
     * L寸法2
     * @return the lsunpou2b
     */
    public Integer getLsunpou2b() {
        return lsunpou2b;
    }

    /**
     * L寸法2
     * @param lsunpou2b the lsunpou2b to set
     */
    public void setLsunpou2b(Integer lsunpou2b) {
        this.lsunpou2b = lsunpou2b;
    }

    /**
     * L寸法3
     * @return the lsunpou2c
     */
    public Integer getLsunpou2c() {
        return lsunpou2c;
    }

    /**
     * L寸法3
     * @param lsunpou2c the lsunpou2c to set
     */
    public void setLsunpou2c(Integer lsunpou2c) {
        this.lsunpou2c = lsunpou2c;
    }

    /**
     * L寸法4
     * @return the lsunpou2d
     */
    public Integer getLsunpou2d() {
        return lsunpou2d;
    }

    /**
     * L寸法4
     * @param lsunpou2d the lsunpou2d to set
     */
    public void setLsunpou2d(Integer lsunpou2d) {
        this.lsunpou2d = lsunpou2d;
    }

    /**
     * L寸法5
     * @return the lsunpou2e
     */
    public Integer getLsunpou2e() {
        return lsunpou2e;
    }

    /**
     * L寸法5
     * @param lsunpou2e the lsunpou2e to set
     */
    public void setLsunpou2e(Integer lsunpou2e) {
        this.lsunpou2e = lsunpou2e;
    }

    /**
     * L寸法6
     * @return the lsunpou2f
     */
    public Integer getLsunpou2f() {
        return lsunpou2f;
    }

    /**
     * L寸法6
     * @param lsunpou2f the lsunpou2f to set
     */
    public void setLsunpou2f(Integer lsunpou2f) {
        this.lsunpou2f = lsunpou2f;
    }

    /**
     * L寸法7
     * @return the lsunpou2g
     */
    public Integer getLsunpou2g() {
        return lsunpou2g;
    }

    /**
     * L寸法7
     * @param lsunpou2g the lsunpou2g to set
     */
    public void setLsunpou2g(Integer lsunpou2g) {
        this.lsunpou2g = lsunpou2g;
    }

    /**
     * L寸法8
     * @return the lsunpou2h
     */
    public Integer getLsunpou2h() {
        return lsunpou2h;
    }

    /**
     * L寸法8
     * @param lsunpou2h the lsunpou2h to set
     */
    public void setLsunpou2h(Integer lsunpou2h) {
        this.lsunpou2h = lsunpou2h;
    }

    /**
     * L寸法9
     * @return the lsunpou2i
     */
    public Integer getLsunpou2i() {
        return lsunpou2i;
    }

    /**
     * L寸法9
     * @param lsunpou2i the lsunpou2i to set
     */
    public void setLsunpou2i(Integer lsunpou2i) {
        this.lsunpou2i = lsunpou2i;
    }

    /**
     * L寸法10
     * @return the lsunpou2j
     */
    public Integer getLsunpou2j() {
        return lsunpou2j;
    }

    /**
     * L寸法10
     * @param lsunpou2j the lsunpou2j to set
     */
    public void setLsunpou2j(Integer lsunpou2j) {
        this.lsunpou2j = lsunpou2j;
    }

    /**
     * L寸法11
     * @return the lsunpou2k
     */
    public Integer getLsunpou2k() {
        return lsunpou2k;
    }

    /**
     * L寸法11
     * @param lsunpou2k the lsunpou2k to set
     */
    public void setLsunpou2k(Integer lsunpou2k) {
        this.lsunpou2k = lsunpou2k;
    }

    /**
     * L寸法12
     * @return the lsunpou2l
     */
    public Integer getLsunpou2l() {
        return lsunpou2l;
    }

    /**
     * L寸法12
     * @param lsunpou2l the lsunpou2l to set
     */
    public void setLsunpou2l(Integer lsunpou2l) {
        this.lsunpou2l = lsunpou2l;
    }

    /**
     * L寸法13
     * @return the lsunpou2m
     */
    public Integer getLsunpou2m() {
        return lsunpou2m;
    }

    /**
     * L寸法13
     * @param lsunpou2m the lsunpou2m to set
     */
    public void setLsunpou2m(Integer lsunpou2m) {
        this.lsunpou2m = lsunpou2m;
    }

    /**
     * L寸法14
     * @return the lsunpou2n
     */
    public Integer getLsunpou2n() {
        return lsunpou2n;
    }

    /**
     * L寸法14
     * @param lsunpou2n the lsunpou2n to set
     */
    public void setLsunpou2n(Integer lsunpou2n) {
        this.lsunpou2n = lsunpou2n;
    }

    /**
     * L寸法15
     * @return the lsunpou2o
     */
    public Integer getLsunpou2o() {
        return lsunpou2o;
    }

    /**
     * L寸法15
     * @param lsunpou2o the lsunpou2o to set
     */
    public void setLsunpou2o(Integer lsunpou2o) {
        this.lsunpou2o = lsunpou2o;
    }

    /**
     * L寸法16
     * @return the lsunpou2p
     */
    public Integer getLsunpou2p() {
        return lsunpou2p;
    }

    /**
     * L寸法16
     * @param lsunpou2p the lsunpou2p to set
     */
    public void setLsunpou2p(Integer lsunpou2p) {
        this.lsunpou2p = lsunpou2p;
    }

    /**
     * L寸法17
     * @return the lsunpou2q
     */
    public Integer getLsunpou2q() {
        return lsunpou2q;
    }

    /**
     * L寸法17
     * @param lsunpou2q the lsunpou2q to set
     */
    public void setLsunpou2q(Integer lsunpou2q) {
        this.lsunpou2q = lsunpou2q;
    }

    /**
     * L寸法18
     * @return the lsunpou2r
     */
    public Integer getLsunpou2r() {
        return lsunpou2r;
    }

    /**
     * L寸法18
     * @param lsunpou2r the lsunpou2r to set
     */
    public void setLsunpou2r(Integer lsunpou2r) {
        this.lsunpou2r = lsunpou2r;
    }

    /**
     * L寸法19
     * @return the lsunpou2s
     */
    public Integer getLsunpou2s() {
        return lsunpou2s;
    }

    /**
     * L寸法19
     * @param lsunpou2s the lsunpou2s to set
     */
    public void setLsunpou2s(Integer lsunpou2s) {
        this.lsunpou2s = lsunpou2s;
    }

    /**
     * L寸法20
     * @return the lsunpou2t
     */
    public Integer getLsunpou2t() {
        return lsunpou2t;
    }

    /**
     * L寸法20
     * @param lsunpou2t the lsunpou2t to set
     */
    public void setLsunpou2t(Integer lsunpou2t) {
        this.lsunpou2t = lsunpou2t;
    }

    /**
     * L寸法21
     * @return the lsunpou2u
     */
    public Integer getLsunpou2u() {
        return lsunpou2u;
    }

    /**
     * L寸法21
     * @param lsunpou2u the lsunpou2u to set
     */
    public void setLsunpou2u(Integer lsunpou2u) {
        this.lsunpou2u = lsunpou2u;
    }

    /**
     * L寸法22
     * @return the lsunpou2v
     */
    public Integer getLsunpou2v() {
        return lsunpou2v;
    }

    /**
     * L寸法22
     * @param lsunpou2v the lsunpou2v to set
     */
    public void setLsunpou2v(Integer lsunpou2v) {
        this.lsunpou2v = lsunpou2v;
    }

    /**
     * L寸法23
     * @return the lsunpou2w
     */
    public Integer getLsunpou2w() {
        return lsunpou2w;
    }

    /**
     * L寸法23
     * @param lsunpou2w the lsunpou2w to set
     */
    public void setLsunpou2w(Integer lsunpou2w) {
        this.lsunpou2w = lsunpou2w;
    }

    /**
     * L寸法24
     * @return the lsunpou2x
     */
    public Integer getLsunpou2x() {
        return lsunpou2x;
    }

    /**
     * L寸法24
     * @param lsunpou2x the lsunpou2x to set
     */
    public void setLsunpou2x(Integer lsunpou2x) {
        this.lsunpou2x = lsunpou2x;
    }

    /**
     * L寸法25
     * @return the lsunpou2y
     */
    public Integer getLsunpou2y() {
        return lsunpou2y;
    }

    /**
     * L寸法25
     * @param lsunpou2y the lsunpou2y to set
     */
    public void setLsunpou2y(Integer lsunpou2y) {
        this.lsunpou2y = lsunpou2y;
    }

    /**
     * L寸法26
     * @return the lsunpou2z
     */
    public Integer getLsunpou2z() {
        return lsunpou2z;
    }

    /**
     * L寸法26
     * @param lsunpou2z the lsunpou2z to set
     */
    public void setLsunpou2z(Integer lsunpou2z) {
        this.lsunpou2z = lsunpou2z;
    }

    /**
     * L寸法27
     * @return the lsunpou2aa
     */
    public Integer getLsunpou2aa() {
        return lsunpou2aa;
    }

    /**
     * L寸法27
     * @param lsunpou2aa the lsunpou2aa to set
     */
    public void setLsunpou2aa(Integer lsunpou2aa) {
        this.lsunpou2aa = lsunpou2aa;
    }

    /**
     * L寸法28
     * @return the lsunpou2ab
     */
    public Integer getLsunpou2ab() {
        return lsunpou2ab;
    }

    /**
     * L寸法28
     * @param lsunpou2ab the lsunpou2ab to set
     */
    public void setLsunpou2ab(Integer lsunpou2ab) {
        this.lsunpou2ab = lsunpou2ab;
    }

    /**
     * L寸法29
     * @return the lsunpou2ac
     */
    public Integer getLsunpou2ac() {
        return lsunpou2ac;
    }

    /**
     * L寸法29
     * @param lsunpou2ac the lsunpou2ac to set
     */
    public void setLsunpou2ac(Integer lsunpou2ac) {
        this.lsunpou2ac = lsunpou2ac;
    }

    /**
     * L寸法30
     * @return the lsunpou2ad
     */
    public Integer getLsunpou2ad() {
        return lsunpou2ad;
    }

    /**
     * L寸法30
     * @param lsunpou2ad the lsunpou2ad to set
     */
    public void setLsunpou2ad(Integer lsunpou2ad) {
        this.lsunpou2ad = lsunpou2ad;
    }

    /**
     * L寸法31
     * @return the lsunpou2ae
     */
    public Integer getLsunpou2ae() {
        return lsunpou2ae;
    }

    /**
     * L寸法31
     * @param lsunpou2ae the lsunpou2ae to set
     */
    public void setLsunpou2ae(Integer lsunpou2ae) {
        this.lsunpou2ae = lsunpou2ae;
    }

    /**
     * L寸法32
     * @return the lsunpou2af
     */
    public Integer getLsunpou2af() {
        return lsunpou2af;
    }

    /**
     * L寸法32
     * @param lsunpou2af the lsunpou2af to set
     */
    public void setLsunpou2af(Integer lsunpou2af) {
        this.lsunpou2af = lsunpou2af;
    }

    /**
     * L寸法33
     * @return the lsunpou2ag
     */
    public Integer getLsunpou2ag() {
        return lsunpou2ag;
    }

    /**
     * L寸法33
     * @param lsunpou2ag the lsunpou2ag to set
     */
    public void setLsunpou2ag(Integer lsunpou2ag) {
        this.lsunpou2ag = lsunpou2ag;
    }

    /**
     * L寸法34
     * @return the lsunpou2ah
     */
    public Integer getLsunpou2ah() {
        return lsunpou2ah;
    }

    /**
     * L寸法34
     * @param lsunpou2ah the lsunpou2ah to set
     */
    public void setLsunpou2ah(Integer lsunpou2ah) {
        this.lsunpou2ah = lsunpou2ah;
    }

    /**
     * L寸法35
     * @return the lsunpou2ai
     */
    public Integer getLsunpou2ai() {
        return lsunpou2ai;
    }

    /**
     * L寸法35
     * @param lsunpou2ai the lsunpou2ai to set
     */
    public void setLsunpou2ai(Integer lsunpou2ai) {
        this.lsunpou2ai = lsunpou2ai;
    }

    /**
     * L寸法36
     * @return the lsunpou2aj
     */
    public Integer getLsunpou2aj() {
        return lsunpou2aj;
    }

    /**
     * L寸法36
     * @param lsunpou2aj the lsunpou2aj to set
     */
    public void setLsunpou2aj(Integer lsunpou2aj) {
        this.lsunpou2aj = lsunpou2aj;
    }

    /**
     * L寸法37
     * @return the lsunpou2ak
     */
    public Integer getLsunpou2ak() {
        return lsunpou2ak;
    }

    /**
     * L寸法37
     * @param lsunpou2ak the lsunpou2ak to set
     */
    public void setLsunpou2ak(Integer lsunpou2ak) {
        this.lsunpou2ak = lsunpou2ak;
    }

    /**
     * L寸法38
     * @return the lsunpou2al
     */
    public Integer getLsunpou2al() {
        return lsunpou2al;
    }

    /**
     * L寸法38
     * @param lsunpou2al the lsunpou2al to set
     */
    public void setLsunpou2al(Integer lsunpou2al) {
        this.lsunpou2al = lsunpou2al;
    }

    /**
     * L寸法39
     * @return the lsunpou2am
     */
    public Integer getLsunpou2am() {
        return lsunpou2am;
    }

    /**
     * L寸法39
     * @param lsunpou2am the lsunpou2am to set
     */
    public void setLsunpou2am(Integer lsunpou2am) {
        this.lsunpou2am = lsunpou2am;
    }

    /**
     * L寸法40
     * @return the lsunpou2an
     */
    public Integer getLsunpou2an() {
        return lsunpou2an;
    }

    /**
     * L寸法40
     * @param lsunpou2an the lsunpou2an to set
     */
    public void setLsunpou2an(Integer lsunpou2an) {
        this.lsunpou2an = lsunpou2an;
    }

    /**
     * WT寸法1
     * @return the wtsunpou2a
     */
    public Integer getWtsunpou2a() {
        return wtsunpou2a;
    }

    /**
     * WT寸法1
     * @param wtsunpou2a the wtsunpou2a to set
     */
    public void setWtsunpou2a(Integer wtsunpou2a) {
        this.wtsunpou2a = wtsunpou2a;
    }

    /**
     * WT寸法2
     * @return the wtsunpou2b
     */
    public Integer getWtsunpou2b() {
        return wtsunpou2b;
    }

    /**
     * WT寸法2
     * @param wtsunpou2b the wtsunpou2b to set
     */
    public void setWtsunpou2b(Integer wtsunpou2b) {
        this.wtsunpou2b = wtsunpou2b;
    }

    /**
     * WT寸法3
     * @return the wtsunpou2c
     */
    public Integer getWtsunpou2c() {
        return wtsunpou2c;
    }

    /**
     * WT寸法3
     * @param wtsunpou2c the wtsunpou2c to set
     */
    public void setWtsunpou2c(Integer wtsunpou2c) {
        this.wtsunpou2c = wtsunpou2c;
    }

    /**
     * WT寸法4
     * @return the wtsunpou2d
     */
    public Integer getWtsunpou2d() {
        return wtsunpou2d;
    }

    /**
     * WT寸法4
     * @param wtsunpou2d the wtsunpou2d to set
     */
    public void setWtsunpou2d(Integer wtsunpou2d) {
        this.wtsunpou2d = wtsunpou2d;
    }

    /**
     * WT寸法5
     * @return the wtsunpou2e
     */
    public Integer getWtsunpou2e() {
        return wtsunpou2e;
    }

    /**
     * WT寸法5
     * @param wtsunpou2e the wtsunpou2e to set
     */
    public void setWtsunpou2e(Integer wtsunpou2e) {
        this.wtsunpou2e = wtsunpou2e;
    }

    /**
     * WT寸法6
     * @return the wtsunpou2f
     */
    public Integer getWtsunpou2f() {
        return wtsunpou2f;
    }

    /**
     * WT寸法6
     * @param wtsunpou2f the wtsunpou2f to set
     */
    public void setWtsunpou2f(Integer wtsunpou2f) {
        this.wtsunpou2f = wtsunpou2f;
    }

    /**
     * WT寸法7
     * @return the wtsunpou2g
     */
    public Integer getWtsunpou2g() {
        return wtsunpou2g;
    }

    /**
     * WT寸法7
     * @param wtsunpou2g the wtsunpou2g to set
     */
    public void setWtsunpou2g(Integer wtsunpou2g) {
        this.wtsunpou2g = wtsunpou2g;
    }

    /**
     * WT寸法8
     * @return the wtsunpou2h
     */
    public Integer getWtsunpou2h() {
        return wtsunpou2h;
    }

    /**
     * WT寸法8
     * @param wtsunpou2h the wtsunpou2h to set
     */
    public void setWtsunpou2h(Integer wtsunpou2h) {
        this.wtsunpou2h = wtsunpou2h;
    }

    /**
     * WT寸法9
     * @return the wtsunpou2i
     */
    public Integer getWtsunpou2i() {
        return wtsunpou2i;
    }

    /**
     * WT寸法9
     * @param wtsunpou2i the wtsunpou2i to set
     */
    public void setWtsunpou2i(Integer wtsunpou2i) {
        this.wtsunpou2i = wtsunpou2i;
    }

    /**
     * WT寸法10
     * @return the wtsunpou2j
     */
    public Integer getWtsunpou2j() {
        return wtsunpou2j;
    }

    /**
     * WT寸法10
     * @param wtsunpou2j the wtsunpou2j to set
     */
    public void setWtsunpou2j(Integer wtsunpou2j) {
        this.wtsunpou2j = wtsunpou2j;
    }

    /**
     * WT寸法11
     * @return the wtsunpou2k
     */
    public Integer getWtsunpou2k() {
        return wtsunpou2k;
    }

    /**
     * WT寸法11
     * @param wtsunpou2k the wtsunpou2k to set
     */
    public void setWtsunpou2k(Integer wtsunpou2k) {
        this.wtsunpou2k = wtsunpou2k;
    }

    /**
     * WT寸法12
     * @return the wtsunpou2l
     */
    public Integer getWtsunpou2l() {
        return wtsunpou2l;
    }

    /**
     * WT寸法12
     * @param wtsunpou2l the wtsunpou2l to set
     */
    public void setWtsunpou2l(Integer wtsunpou2l) {
        this.wtsunpou2l = wtsunpou2l;
    }

    /**
     * WT寸法13
     * @return the wtsunpou2m
     */
    public Integer getWtsunpou2m() {
        return wtsunpou2m;
    }

    /**
     * WT寸法13
     * @param wtsunpou2m the wtsunpou2m to set
     */
    public void setWtsunpou2m(Integer wtsunpou2m) {
        this.wtsunpou2m = wtsunpou2m;
    }

    /**
     * WT寸法14
     * @return the wtsunpou2n
     */
    public Integer getWtsunpou2n() {
        return wtsunpou2n;
    }

    /**
     * WT寸法14
     * @param wtsunpou2n the wtsunpou2n to set
     */
    public void setWtsunpou2n(Integer wtsunpou2n) {
        this.wtsunpou2n = wtsunpou2n;
    }

    /**
     * WT寸法15
     * @return the wtsunpou2o
     */
    public Integer getWtsunpou2o() {
        return wtsunpou2o;
    }

    /**
     * WT寸法15
     * @param wtsunpou2o the wtsunpou2o to set
     */
    public void setWtsunpou2o(Integer wtsunpou2o) {
        this.wtsunpou2o = wtsunpou2o;
    }

    /**
     * WT寸法16
     * @return the wtsunpou2p
     */
    public Integer getWtsunpou2p() {
        return wtsunpou2p;
    }

    /**
     * WT寸法16
     * @param wtsunpou2p the wtsunpou2p to set
     */
    public void setWtsunpou2p(Integer wtsunpou2p) {
        this.wtsunpou2p = wtsunpou2p;
    }

    /**
     * WT寸法17
     * @return the wtsunpou2q
     */
    public Integer getWtsunpou2q() {
        return wtsunpou2q;
    }

    /**
     * WT寸法17
     * @param wtsunpou2q the wtsunpou2q to set
     */
    public void setWtsunpou2q(Integer wtsunpou2q) {
        this.wtsunpou2q = wtsunpou2q;
    }

    /**
     * WT寸法18
     * @return the wtsunpou2r
     */
    public Integer getWtsunpou2r() {
        return wtsunpou2r;
    }

    /**
     * WT寸法18
     * @param wtsunpou2r the wtsunpou2r to set
     */
    public void setWtsunpou2r(Integer wtsunpou2r) {
        this.wtsunpou2r = wtsunpou2r;
    }

    /**
     * WT寸法19
     * @return the wtsunpou2s
     */
    public Integer getWtsunpou2s() {
        return wtsunpou2s;
    }

    /**
     * WT寸法19
     * @param wtsunpou2s the wtsunpou2s to set
     */
    public void setWtsunpou2s(Integer wtsunpou2s) {
        this.wtsunpou2s = wtsunpou2s;
    }

    /**
     * WT寸法20
     * @return the wtsunpou2t
     */
    public Integer getWtsunpou2t() {
        return wtsunpou2t;
    }

    /**
     * WT寸法20
     * @param wtsunpou2t the wtsunpou2t to set
     */
    public void setWtsunpou2t(Integer wtsunpou2t) {
        this.wtsunpou2t = wtsunpou2t;
    }

    /**
     * WT寸法21
     * @return the wtsunpou2u
     */
    public Integer getWtsunpou2u() {
        return wtsunpou2u;
    }

    /**
     * WT寸法21
     * @param wtsunpou2u the wtsunpou2u to set
     */
    public void setWtsunpou2u(Integer wtsunpou2u) {
        this.wtsunpou2u = wtsunpou2u;
    }

    /**
     * WT寸法22
     * @return the wtsunpou2v
     */
    public Integer getWtsunpou2v() {
        return wtsunpou2v;
    }

    /**
     * WT寸法22
     * @param wtsunpou2v the wtsunpou2v to set
     */
    public void setWtsunpou2v(Integer wtsunpou2v) {
        this.wtsunpou2v = wtsunpou2v;
    }

    /**
     * WT寸法23
     * @return the wtsunpou2w
     */
    public Integer getWtsunpou2w() {
        return wtsunpou2w;
    }

    /**
     * WT寸法23
     * @param wtsunpou2w the wtsunpou2w to set
     */
    public void setWtsunpou2w(Integer wtsunpou2w) {
        this.wtsunpou2w = wtsunpou2w;
    }

    /**
     * WT寸法24
     * @return the wtsunpou2x
     */
    public Integer getWtsunpou2x() {
        return wtsunpou2x;
    }

    /**
     * WT寸法24
     * @param wtsunpou2x the wtsunpou2x to set
     */
    public void setWtsunpou2x(Integer wtsunpou2x) {
        this.wtsunpou2x = wtsunpou2x;
    }

    /**
     * WT寸法25
     * @return the wtsunpou2y
     */
    public Integer getWtsunpou2y() {
        return wtsunpou2y;
    }

    /**
     * WT寸法25
     * @param wtsunpou2y the wtsunpou2y to set
     */
    public void setWtsunpou2y(Integer wtsunpou2y) {
        this.wtsunpou2y = wtsunpou2y;
    }

    /**
     * WT寸法26
     * @return the wtsunpou2z
     */
    public Integer getWtsunpou2z() {
        return wtsunpou2z;
    }

    /**
     * WT寸法26
     * @param wtsunpou2z the wtsunpou2z to set
     */
    public void setWtsunpou2z(Integer wtsunpou2z) {
        this.wtsunpou2z = wtsunpou2z;
    }

    /**
     * WT寸法27
     * @return the wtsunpou2aa
     */
    public Integer getWtsunpou2aa() {
        return wtsunpou2aa;
    }

    /**
     * WT寸法27
     * @param wtsunpou2aa the wtsunpou2aa to set
     */
    public void setWtsunpou2aa(Integer wtsunpou2aa) {
        this.wtsunpou2aa = wtsunpou2aa;
    }

    /**
     * WT寸法28
     * @return the wtsunpou2ab
     */
    public Integer getWtsunpou2ab() {
        return wtsunpou2ab;
    }

    /**
     * WT寸法28
     * @param wtsunpou2ab the wtsunpou2ab to set
     */
    public void setWtsunpou2ab(Integer wtsunpou2ab) {
        this.wtsunpou2ab = wtsunpou2ab;
    }

    /**
     * WT寸法29
     * @return the wtsunpou2ac
     */
    public Integer getWtsunpou2ac() {
        return wtsunpou2ac;
    }

    /**
     * WT寸法29
     * @param wtsunpou2ac the wtsunpou2ac to set
     */
    public void setWtsunpou2ac(Integer wtsunpou2ac) {
        this.wtsunpou2ac = wtsunpou2ac;
    }

    /**
     * WT寸法30
     * @return the wtsunpou2ad
     */
    public Integer getWtsunpou2ad() {
        return wtsunpou2ad;
    }

    /**
     * WT寸法30
     * @param wtsunpou2ad the wtsunpou2ad to set
     */
    public void setWtsunpou2ad(Integer wtsunpou2ad) {
        this.wtsunpou2ad = wtsunpou2ad;
    }

    /**
     * WT寸法31
     * @return the wtsunpou2ae
     */
    public Integer getWtsunpou2ae() {
        return wtsunpou2ae;
    }

    /**
     * WT寸法31
     * @param wtsunpou2ae the wtsunpou2ae to set
     */
    public void setWtsunpou2ae(Integer wtsunpou2ae) {
        this.wtsunpou2ae = wtsunpou2ae;
    }

    /**
     * WT寸法32
     * @return the wtsunpou2af
     */
    public Integer getWtsunpou2af() {
        return wtsunpou2af;
    }

    /**
     * WT寸法32
     * @param wtsunpou2af the wtsunpou2af to set
     */
    public void setWtsunpou2af(Integer wtsunpou2af) {
        this.wtsunpou2af = wtsunpou2af;
    }

    /**
     * WT寸法33
     * @return the wtsunpou2ag
     */
    public Integer getWtsunpou2ag() {
        return wtsunpou2ag;
    }

    /**
     * WT寸法33
     * @param wtsunpou2ag the wtsunpou2ag to set
     */
    public void setWtsunpou2ag(Integer wtsunpou2ag) {
        this.wtsunpou2ag = wtsunpou2ag;
    }

    /**
     * WT寸法34
     * @return the wtsunpou2ah
     */
    public Integer getWtsunpou2ah() {
        return wtsunpou2ah;
    }

    /**
     * WT寸法34
     * @param wtsunpou2ah the wtsunpou2ah to set
     */
    public void setWtsunpou2ah(Integer wtsunpou2ah) {
        this.wtsunpou2ah = wtsunpou2ah;
    }

    /**
     * WT寸法35
     * @return the wtsunpou2ai
     */
    public Integer getWtsunpou2ai() {
        return wtsunpou2ai;
    }

    /**
     * WT寸法35
     * @param wtsunpou2ai the wtsunpou2ai to set
     */
    public void setWtsunpou2ai(Integer wtsunpou2ai) {
        this.wtsunpou2ai = wtsunpou2ai;
    }

    /**
     * WT寸法36
     * @return the wtsunpou2aj
     */
    public Integer getWtsunpou2aj() {
        return wtsunpou2aj;
    }

    /**
     * WT寸法36
     * @param wtsunpou2aj the wtsunpou2aj to set
     */
    public void setWtsunpou2aj(Integer wtsunpou2aj) {
        this.wtsunpou2aj = wtsunpou2aj;
    }

    /**
     * WT寸法37
     * @return the wtsunpou2ak
     */
    public Integer getWtsunpou2ak() {
        return wtsunpou2ak;
    }

    /**
     * WT寸法37
     * @param wtsunpou2ak the wtsunpou2ak to set
     */
    public void setWtsunpou2ak(Integer wtsunpou2ak) {
        this.wtsunpou2ak = wtsunpou2ak;
    }

    /**
     * WT寸法38
     * @return the wtsunpou2al
     */
    public Integer getWtsunpou2al() {
        return wtsunpou2al;
    }

    /**
     * WT寸法38
     * @param wtsunpou2al the wtsunpou2al to set
     */
    public void setWtsunpou2al(Integer wtsunpou2al) {
        this.wtsunpou2al = wtsunpou2al;
    }

    /**
     * WT寸法39
     * @return the wtsunpou2am
     */
    public Integer getWtsunpou2am() {
        return wtsunpou2am;
    }

    /**
     * WT寸法39
     * @param wtsunpou2am the wtsunpou2am to set
     */
    public void setWtsunpou2am(Integer wtsunpou2am) {
        this.wtsunpou2am = wtsunpou2am;
    }

    /**
     * WT寸法40
     * @return the wtsunpou2an
     */
    public Integer getWtsunpou2an() {
        return wtsunpou2an;
    }

    /**
     * WT寸法40
     * @param wtsunpou2an the wtsunpou2an to set
     */
    public void setWtsunpou2an(Integer wtsunpou2an) {
        this.wtsunpou2an = wtsunpou2an;
    }
}