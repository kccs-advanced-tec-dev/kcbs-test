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
}