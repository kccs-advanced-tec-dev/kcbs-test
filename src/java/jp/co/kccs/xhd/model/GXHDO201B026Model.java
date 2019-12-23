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
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2019/12/09<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	863 K.Zhang<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 外部電極・外部電極塗布履歴検索画面のモデルクラスです。
 *
 * @author 863 K.Zhang
 * @since  2019/12/09
 */
public class GXHDO201B026Model implements Serializable{

    /** ﾛｯﾄNo. */
    private String lotno = "";

    /**
     * ﾛｯﾄﾌﾟﾚ
     */
    private String lotpre = "";

    /**
     * KCPNO
     */
    private String kcpno = "";

    /**
     * 数量
     */
    private Integer suuryou;

    /**
     * 客先
     */
    private String kyakusaki = "";

    /**
     * 作業場所
     */
    private String sagyobasyo = "";

    /**
     * 号機1
     */
    private String gouki1 = "";

    /**
     * 条件設定者1
     */
    private String setteisya1 = "";

    /**
     * 号機2
     */
    private String gouki2 = "";
    
    /**
     * 条件設定者2
     */
    private String setteisya2 = "";
    
    /**
     * ﾍﾟｰｽﾄ品名
     */
    private String pastehinmei = "";

    /**
     * ﾍﾟｰｽﾄﾛｯﾄNo
     */
    private String pastelotno = "";

    /**
     * ﾍﾟｰｽﾄ再生回数
     */
    private Integer pastesaiseikaisuu = null;

    /**
     * ﾍﾟｰｽﾄ交換時間
     */
    private String pastekoukanjikan = "";
    
    /**
     * ﾍﾟｰｽﾄ粘度
     */
    private Integer pastenendo = null;
    
    /**
     * ﾍﾟｰｽﾄ温度
     */
    private BigDecimal pasteondo = null;
    
    /**
     * ﾍﾟｰｽﾄ換算粘度
     */
    private Integer pastekansannendo = null;
    
    /**
     * ﾍﾟｰｽﾄ期限
     */
    private String pastekigen = "";

    /**
     * 開始日時
     */
    private Timestamp startdatetime = null;

    /**
     * 終了日時
     */
    private Timestamp enddatetime = null;

    /**
     * 先行外観
     */
    private String senkougaikan = "";
    
    /**
     * 磁器L寸法
     */
    private Integer jikilsunpou = null;
    
    /**
     * 磁器T寸法
     */
    private Integer jikitsunpou = null;

    /**
     * P寸法1OLD
     */
    private Integer psunpou1a = null;
    
    /**
     * P寸法2OLD
     */
    private Integer psunpou1b = null;
    
    /**
     * P寸法3OLD
     */
    private Integer psunpou1c = null;
    
    /**
     * P寸法4OLD
     */
    private Integer psunpou1d = null;
    
    /**
     * P寸法5OLD
     */
    private Integer psunpou1e = null;
    
    /**
     * P寸法AVEOLD
     */
    private BigDecimal psunpouave1 = null;
    
    /**
     * P寸法RANGEOLD
     */
    private Integer psunpourange1 = null;
    
    /**
     * L寸法1OLD
     */
    private Integer lsunpou1a = null;
    
    /**
     * L寸法2OLD
     */
    private Integer lsunpou1b = null;
    
    /**
     * L寸法3OLD
     */
    private Integer lsunpou1c = null;
    
    /**
     * L寸法4OLD
     */
    private Integer lsunpou1d = null;
    
    /**
     * L寸法5OLD
     */
    private Integer lsunpou1e = null;
    
    /**
     * 端面厚みOLD
     */
    private BigDecimal tanmenatsumi1 = null;
    
    /**
     * 判定OLD
     */
    private String sunpouhantei1 = "";
    
    /**
     * P寸法1
     */
    private Integer psunpou2a = null;
    
    /**
     * P寸法2
     */
    private Integer psunpou2b = null;
    
    /**
     * P寸法3
     */
    private Integer psunpou2c = null;
    
    /**
     * P寸法4
     */
    private Integer psunpou2d = null;
    
    /**
     * P寸法5
     */
    private Integer psunpou2e = null;
    
    /**
     * P寸法AVE
     */
    private BigDecimal psunpouave2 = null;
    
    /**
     * P寸法RANGE
     */
    private Integer psunpourange2 = null;
    
    /**
     * L寸法1
     */
    private Integer lsunpou2a = null;
    
    /**
     * L寸法2
     */
    private Integer lsunpou2b = null;
    
    /**
     * L寸法3
     */
    private Integer lsunpou2c = null;
    
    /**
     * L寸法4
     */
    private Integer lsunpou2d = null;
    
    /**
     * L寸法5
     */
    private Integer lsunpou2e = null;
    
    /**
     * 端面厚み
     */
    private BigDecimal tanmenatsumi2 = null;
    
    /**
     * 判定
     */
    private String sunpouhantei2 = "";
    
    /**
     * ﾍﾟｰｽﾄ厚み設定値1次
     */
    private Integer pasteatsumi1 = null;
    
    /**
     * ﾍﾟｰｽﾄ厚み設定値2次
     */
    private Integer pasteatsumi2 = null;

    /**
     * DIP治具ｻｲｽﾞ
     */
    private Integer dipjigusize = null;

    /**
     * DIP治具枚数
     */
    private Integer dipjigumaisuu = null;
    
    /**
     * DIP後外観結果
     */
    private String dipgogaikankekka = "";

    /**
     * 処置内容
     */
    private String syochinaiyou = "";
    
    /**
     * ｲﾝｸ厚みa
     */
    private String inkuatsumia = "";
    
    /**
     * ｲﾝｸ厚みb
     */
    private String inkuatsumib = "";

    /**
     * 設定条件
     */
    private String setteijyouken = "";
    
    /**
     * 備考1
     */
    private String bikou1 = "";

    /**
     * 備考2
     */
    private String bikou2 = "";

    /**
     * 備考3
     */
    private String bikou3 = "";
    
    /**
     * P寸法6OLD
     */
    private Integer psunpou1f = null;
    
    /**
     * P寸法7OLD
     */
    private Integer psunpou1g = null;
    
    /**
     * P寸法8OLD
     */
    private Integer psunpou1h = null;
    
    /**
     * P寸法9OLD
     */
    private Integer psunpou1i = null;
    
    /**
     * P寸法10OLD
     */
    private Integer psunpou1j = null;
    
    /**
     * L寸法6OLD
     */
    private Integer lsunpou1f = null;
    
    /**
     * L寸法7OLD
     */
    private Integer lsunpou1g = null;
    
    /**
     * L寸法8OLD
     */
    private Integer lsunpou1h = null;
    
    /**
     * L寸法9OLD
     */
    private Integer lsunpou1i = null;
    
    /**
     * L寸法10OLD
     */
    private Integer lsunpou1j = null;
    
    /**
     * P寸法6
     */
    private Integer psunpou2f = null;
    
    /**
     * P寸法7
     */
    private Integer psunpou2g = null;
    
    /**
     * P寸法8
     */
    private Integer psunpou2h = null;
    
    /**
     * P寸法9
     */
    private Integer psunpou2i = null;
    
    /**
     * P寸法10
     */
    private Integer psunpou2j = null;
    
    /**
     * L寸法6
     */
    private Integer lsunpou2f = null;
    
    /**
     * L寸法7
     */
    private Integer lsunpou2g = null;
    
    /**
     * L寸法8
     */
    private Integer lsunpou2h = null;
    
    /**
     * L寸法9
     */
    private Integer lsunpou2i = null;
    
    /**
     * L寸法10
     */
    private Integer lsunpou2j = null;
    
    /**
     * L寸法AVEOLD
     */
    private BigDecimal lsunpouave1 = null;
    
    /**
     * L寸法AVE
     */
    private BigDecimal lsunpouave2 = null;

    /**
     * ﾍﾟｰｽﾄ固形分
     */
    private BigDecimal pastekokeibun = null;
    
    /**
     * 1次頭出し設定
     */
    private BigDecimal dip1atamadashisettei = null;
    
    /**
     * 1次DIPｸﾘｱﾗﾝｽ設定
     */
    private BigDecimal dip1clearance = null;
    
    /**
     * 1次DIPｽｷｰｼﾞ設定
     */
    private BigDecimal dip1skeegesettei = null;
    
    /**
     * 1次ﾌﾞﾛｯﾄｸﾘｱﾗﾝｽ設定
     */
    private BigDecimal dip1blotclearance = null;
    
    /**
     * 1次ﾚﾍﾞﾗｰ設定
     */
    private BigDecimal dip1reveler = null;
    
    /**
     * 2次頭出し設定
     */
    private BigDecimal dip2atamadashisettei = null;
    
    /**
     * 2次DIPｸﾘｱﾗﾝｽ設定
     */
    private BigDecimal dip2clearance = null;
    
    /**
     * 2次DIPｽｷｰｼﾞ設定
     */
    private BigDecimal dip2skeegesettei = null;
    
    /**
     * 2次ﾌﾞﾛｯﾄｸﾘｱﾗﾝｽ設定
     */
    private BigDecimal dip2blotclearance = null;

    /**
     * 2次ﾚﾍﾞﾗｰ設定
     */
    private BigDecimal dip2reveler = null;

    /**
     * P寸法11
     */
    private Integer psunpou2k = null;
    
    /**
     * P寸法12
     */
    private Integer psunpou2l = null;
    
    /**
     * P寸法13
     */
    private Integer psunpou2m = null;
    
    /**
     * P寸法14
     */
    private Integer psunpou2n = null;
    
    /**
     * P寸法15
     */
    private Integer psunpou2o = null;
    
    /**
     * P寸法16
     */
    private Integer psunpou2p = null;
    
    /**
     * P寸法17
     */
    private Integer psunpou2q = null;
    
    /**
     * P寸法18
     */
    private Integer psunpou2r = null;
    
    /**
     * P寸法19
     */
    private Integer psunpou2s = null;
    
    /**
     * P寸法20
     */
    private Integer psunpou2t = null;
    
    /**
     * P寸法21
     */
    private Integer psunpou2u = null;
    
    /**
     * P寸法22
     */
    private Integer psunpou2v = null;
    
    /**
     * P寸法23
     */
    private Integer psunpou2w = null;
    
    /**
     * P寸法24
     */
    private Integer psunpou2x = null;
    
    /**
     * P寸法25
     */
    private Integer psunpou2y = null;
    
    /**
     * P寸法26
     */
    private Integer psunpou2z = null;
    
    /**
     * P寸法27
     */
    private Integer psunpou2aa = null;
    
    /**
     * P寸法28
     */
    private Integer psunpou2ab = null;
    
    /**
     * P寸法29
     */
    private Integer psunpou2ac = null;
    
    /**
     * P寸法30
     */
    private Integer psunpou2ad = null;
    
    /**
     * P寸法31
     */
    private Integer psunpou2ae = null;
    
    /**
     * P寸法32
     */
    private Integer psunpou2af = null;
    
    /**
     * P寸法33
     */
    private Integer psunpou2ag = null;
    
    /**
     * P寸法34
     */
    private Integer psunpou2ah = null;
    
    /**
     * P寸法35
     */
    private Integer psunpou2ai = null;
    
    /**
     * P寸法36
     */
    private Integer psunpou2aj = null;
    
    /**
     * P寸法37
     */
    private Integer psunpou2ak = null;
    
    /**
     * P寸法38
     */
    private Integer psunpou2al = null;
    
    /**
     * P寸法39
     */
    private Integer psunpou2am = null;
    
    /**
     * P寸法40
     */
    private Integer psunpou2an = null;
    
    /**
     * L寸法11
     */
    private Integer lsunpou2k = null;
    
    /**
     * L寸法12
     */
    private Integer lsunpou2l = null;
    
    /**
     * L寸法13
     */
    private Integer lsunpou2m = null;
    
    /**
     * L寸法14
     */
    private Integer lsunpou2n = null;
    
    /**
     * L寸法15
     */
    private Integer lsunpou2o = null;
    
    /**
     * L寸法16
     */
    private Integer lsunpou2p = null;
    
    /**
     * L寸法17
     */
    private Integer lsunpou2q = null;
    
    /**
     * L寸法18
     */
    private Integer lsunpou2r = null;
    
    /**
     * L寸法19
     */
    private Integer lsunpou2s = null;
    
    /**
     * L寸法20
     */
    private Integer lsunpou2t = null;
    
    /**
     * L寸法21
     */
    private Integer lsunpou2u = null;
    
    /**
     * L寸法22
     */
    private Integer lsunpou2v = null;
    
    /**
     * L寸法23
     */
    private Integer lsunpou2w = null;

    /**
     * L寸法24
     */
    private Integer lsunpou2x = null;
    
    /**
     * L寸法25
     */
    private Integer lsunpou2y = null;
    
    /**
     * L寸法26
     */
    private Integer lsunpou2z = null;
    
    /**
     * L寸法27
     */
    private Integer lsunpou2aa = null;
    
    /**
     * L寸法28
     */
    private Integer lsunpou2ab = null;
    
    /**
     * L寸法29
     */
    private Integer lsunpou2ac = null;
    
    /**
     * L寸法30
     */
    private Integer lsunpou2ad = null;
    
    /**
     * L寸法31
     */
    private Integer lsunpou2ae = null;
    
    /**
     * L寸法32
     */
    private Integer lsunpou2af = null;
    
    /**
     * L寸法33
     */
    private Integer lsunpou2ag = null;
    
    /**
     * L寸法34
     */
    private Integer lsunpou2ah = null;
    
    /**
     * L寸法35
     */
    private Integer lsunpou2ai = null;
    
    /**
     * L寸法36
     */
    private Integer lsunpou2aj = null;
    
    /**
     * L寸法37
     */
    private Integer lsunpou2ak = null;
    
    /**
     * L寸法38
     */
    private Integer lsunpou2al = null;
    
    /**
     * L寸法39
     */
    private Integer lsunpou2am = null;
    
    /**
     * L寸法40
     */
    private Integer lsunpou2an = null;
    
    /**
     * L寸法RANGE
     */
    private Integer lsunpourange2 = null;
    
    /**
     * L寸法MIN
     */
    private BigDecimal lsunpoumin2 = null;
    
    /**
     * L寸法MAX
     */
    private BigDecimal lsunpoumax2 = null;
    
    /**
     * WT寸法1
     */
    private Integer wtsunpou2a = null;
    
    /**
     * WT寸法2
     */
    private Integer wtsunpou2b = null;
    
    /**
     * WT寸法3
     */
    private Integer wtsunpou2c = null;
    
    /**
     * WT寸法4
     */
    private Integer wtsunpou2d = null;
    
    /**
     * WT寸法5
     */
    private Integer wtsunpou2e = null;
    
    /**
     * WT寸法6
     */
    private Integer wtsunpou2f = null;
    
    /**
     * WT寸法7
     */
    private Integer wtsunpou2g = null;
    
    /**
     * WT寸法8
     */
    private Integer wtsunpou2h = null;
    
    /**
     * WT寸法9
     */
    private Integer wtsunpou2i = null;
    
    /**
     * WT寸法10
     */
    private Integer wtsunpou2j = null;
    
    /**
     * WT寸法11
     */
    private Integer wtsunpou2k = null;
    
    /**
     * WT寸法12
     */
    private Integer wtsunpou2l = null;
    
    /**
     * WT寸法13
     */
    private Integer wtsunpou2m = null;
    
    /**
     * WT寸法14
     */
    private Integer wtsunpou2n = null;
    
    /**
     * WT寸法15
     */
    private Integer wtsunpou2o = null;
    
    /**
     * WT寸法16
     */
    private Integer wtsunpou2p = null;
    
    /**
     * WT寸法17
     */
    private Integer wtsunpou2q = null;
    
    /**
     * WT寸法18
     */
    private Integer wtsunpou2r = null;
    
    /**
     * WT寸法19
     */
    private Integer wtsunpou2s = null;
    
    /**
     * WT寸法20
     */
    private Integer wtsunpou2t = null;
    
    /**
     * WT寸法21
     */
    private Integer wtsunpou2u = null;
    
    /**
     * WT寸法22
     */
    private Integer wtsunpou2v = null;
    
    /**
     * WT寸法23
     */
    private Integer wtsunpou2w = null;
    
    /**
     * WT寸法24
     */
    private Integer wtsunpou2x = null;
    
    /**
     * WT寸法25
     */
    private Integer wtsunpou2y = null;
    
    /**
     * WT寸法26
     */
    private Integer wtsunpou2z = null;
    
    /**
     * WT寸法27
     */
    private Integer wtsunpou2aa = null;
    
    /**
     * WT寸法28
     */
    private Integer wtsunpou2ab = null;
    
    /**
     * WT寸法29
     */
    private Integer wtsunpou2ac = null;
    
    /**
     * WT寸法30
     */
    private Integer wtsunpou2ad = null;
    
    /**
     * WT寸法31
     */
    private Integer wtsunpou2ae = null;
    
    /**
     * WT寸法32
     */
    private Integer wtsunpou2af = null;
    
    /**
     * WT寸法33
     */
    private Integer wtsunpou2ag = null;
    
    /**
     * WT寸法34
     */
    private Integer wtsunpou2ah = null;
    
    /**
     * WT寸法35
     */
    private Integer wtsunpou2ai = null;
    
    /**
     * WT寸法36
     */
    private Integer wtsunpou2aj = null;
    
    /**
     * WT寸法37
     */
    private Integer wtsunpou2ak = null;
    
    /**
     * WT寸法38
     */
    private Integer wtsunpou2al = null;
    
    /**
     * WT寸法39
     */
    private Integer wtsunpou2am = null;
    /**
     * WT寸法40
     */
    private Integer wtsunpou2an = null;
    
    /**
     * WT寸法AVE
     */
    private BigDecimal wtsunpouave2 = null;

    /**
     * WT寸法RANGE
     */
    private Integer wtsunpourange2 = null;
    
    /**
     * WT寸法MIN
     */
    private Integer wtsunpoumin2 = null;

    /**
     * WT寸法MAX
     */
    private Integer wtsunpoumax2 = null;

    /**
     * P寸法MIN
     */
    private BigDecimal psunpoumin2 = null;
    
    /**
     * P寸法MAX
     */
    private BigDecimal psunpoumax2 = null;
    
    /**
     * 設備種類
     */
    private String setubisyurui = "";
    
    /**
     * 塗布回数
     */
    private Integer tofukaisuu = null;
    
    /**
     * 保持ｼﾞｸﾞ
     */
    private String hojijigu = "";

    /**
     * 粘着ｼｰﾄﾛｯﾄ  1次側
     */
    private String nentyakusheetlot1 = "";

    /**
     * 粘着ｼｰﾄﾛｯﾄ  2次側
     */
    private String nentyakusheetlot2 = "";

    /**
     * 塗布ｼﾞｸﾞ取り個数
     */
    private Integer tofujigutorikosuu = null;

    /**
     * 開始担当者
     */
    private String starttantosyacode = "";

    /**
     * 開始確認者
     */
    private String startkakuninsyacode = "";

    /**
     * 重量
     */
    private BigDecimal juryou = null;

    /**
     * 処理個数
     */
    private Integer syorikosuu = null;

    /**
     * 終了担当者
     */
    private String endtantosyacode = "";

    /**
     * 1次ﾍﾟｰｽﾄ厚み設定値
     */
    private BigDecimal pasteatsumi1ji = null;

    /**
     * 2次ﾍﾟｰｽﾄ厚み設定値
     */
    private BigDecimal pasteatsumi2ji = null;

    /**
     * ｲﾝｸ厚みA
     */
    private Integer atsumiinkua = null;

    /**
     * ｲﾝｸ厚みB
     */
    private Integer atsumiinkub = null;

    /**
     * 回数
     */
    private Integer kaisuu = null;

    /**
     * ﾛｯﾄNo
     * @return lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * ﾛｯﾄNo
     * @param lotno セットする lotno
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * ﾛｯﾄﾌﾟﾚ
     * @return lotpre
     */
    public String getLotpre() {
        return lotpre;
    }

    /**
     * ﾛｯﾄﾌﾟﾚ
     * @param lotpre セットする lotpre
     */
    public void setLotpre(String lotpre) {
        this.lotpre = lotpre;
    }

    /**
     * KCPNO
     * @return kcpno
     */
    public String getKcpno() {
        return kcpno;
    }

    /**
     * KCPNO
     * @param kcpno セットする kcpno
     */
    public void setKcpno(String kcpno) {
        this.kcpno = kcpno;
    }

    /**
     * 数量
     * @return suuryou
     */
    public Integer getSuuryou() {
        return suuryou;
    }

    /**
     * 数量
     * @param suuryou セットする suuryou
     */
    public void setSuuryou(Integer suuryou) {
        this.suuryou = suuryou;
    }

    /**
     * 客先
     * @return kyakusaki
     */
    public String getKyakusaki() {
        return kyakusaki;
    }

    /**
     * 客先
     * @param kyakusaki セットする kyakusaki
     */
    public void setKyakusaki(String kyakusaki) {
        this.kyakusaki = kyakusaki;
    }

    /**
     * 作業場所
     * @return sagyobasyo
     */
    public String getSagyobasyo() {
        return sagyobasyo;
    }

    /**
     * 作業場所
     * @param sagyobasyo セットする sagyobasyo
     */
    public void setSagyobasyo(String sagyobasyo) {
        this.sagyobasyo = sagyobasyo;
    }

    /**
     * 号機1
     * @return gouki1
     */
    public String getGouki1() {
        return gouki1;
    }

    /**
     * 号機1
     * @param gouki1 セットする gouki1
     */
    public void setGouki1(String gouki1) {
        this.gouki1 = gouki1;
    }

    /**
     * 条件設定者1
     * @return setteisya1
     */
    public String getSetteisya1() {
        return setteisya1;
    }

    /**
     * 条件設定者1
     * @param setteisya1 セットする setteisya1
     */
    public void setSetteisya1(String setteisya1) {
        this.setteisya1 = setteisya1;
    }

    /**
     * 号機2
     * @return gouki2
     */
    public String getGouki2() {
        return gouki2;
    }

    /**
     * 号機2
     * @param gouki2 セットする gouki2
     */
    public void setGouki2(String gouki2) {
        this.gouki2 = gouki2;
    }

    /**
     * 条件設定者2
     * @return setteisya2
     */
    public String getSetteisya2() {
        return setteisya2;
    }

    /**
     * 条件設定者2
     * @param setteisya2 セットする setteisya2
     */
    public void setSetteisya2(String setteisya2) {
        this.setteisya2 = setteisya2;
    }

    /**
     * ﾍﾟｰｽﾄ品名
     * @return pastehinmei
     */
    public String getPastehinmei() {
        return pastehinmei;
    }

    /**
     * ﾍﾟｰｽﾄ品名
     * @param pastehinmei セットする pastehinmei
     */
    public void setPastehinmei(String pastehinmei) {
        this.pastehinmei = pastehinmei;
    }

    /**
     * ﾍﾟｰｽﾄﾛｯﾄNo
     * @return pastelotno
     */
    public String getPastelotno() {
        return pastelotno;
    }

    /**
     * ﾍﾟｰｽﾄﾛｯﾄNo
     * @param pastelotno セットする pastelotno
     */
    public void setPastelotno(String pastelotno) {
        this.pastelotno = pastelotno;
    }

    /**
     * ﾍﾟｰｽﾄ再生回数
     * @return pastesaiseikaisuu
     */
    public Integer getPastesaiseikaisuu() {
        return pastesaiseikaisuu;
    }

    /**
     * ﾍﾟｰｽﾄ再生回数
     * @param pastesaiseikaisuu セットする pastesaiseikaisuu
     */
    public void setPastesaiseikaisuu(Integer pastesaiseikaisuu) {
        this.pastesaiseikaisuu = pastesaiseikaisuu;
    }

    /**
     * ﾍﾟｰｽﾄ交換時間
     * @return pastekoukanjikan
     */
    public String getPastekoukanjikan() {
        return pastekoukanjikan;
    }

    /**
     * ﾍﾟｰｽﾄ交換時間
     * @param pastekoukanjikan セットする pastekoukanjikan
     */
    public void setPastekoukanjikan(String pastekoukanjikan) {
        this.pastekoukanjikan = pastekoukanjikan;
    }

    /**
     * ﾍﾟｰｽﾄ粘度
     * @return pastenendo
     */
    public Integer getPastenendo() {
        return pastenendo;
    }

    /**
     * ﾍﾟｰｽﾄ粘度
     * @param pastenendo セットする pastenendo
     */
    public void setPastenendo(Integer pastenendo) {
        this.pastenendo = pastenendo;
    }

    /**
     * ﾍﾟｰｽﾄ温度
     * @return pasteondo
     */
    public BigDecimal getPasteondo() {
        return pasteondo;
    }

    /**
     * ﾍﾟｰｽﾄ温度
     * @param pasteondo セットする pasteondo
     */
    public void setPasteondo(BigDecimal pasteondo) {
        this.pasteondo = pasteondo;
    }

    /**
     * ﾍﾟｰｽﾄ換算粘度
     * @return pastekansannendo
     */
    public Integer getPastekansannendo() {
        return pastekansannendo;
    }

    /**
     * ﾍﾟｰｽﾄ換算粘度
     * @param pastekansannendo セットする pastekansannendo
     */
    public void setPastekansannendo(Integer pastekansannendo) {
        this.pastekansannendo = pastekansannendo;
    }

    /**
     * ﾍﾟｰｽﾄ期限
     * @return pastekigen
     */
    public String getPastekigen() {
        return pastekigen;
    }

    /**
     * ﾍﾟｰｽﾄ期限
     * @param pastekigen セットする pastekigen
     */
    public void setPastekigen(String pastekigen) {
        this.pastekigen = pastekigen;
    }

    /**
     * 開始日時
     * @return startdatetime
     */
    public Timestamp getStartdatetime() {
        return startdatetime;
    }

    /**
     * 開始日時
     * @param startdatetime セットする startdatetime
     */
    public void setStartdatetime(Timestamp startdatetime) {
        this.startdatetime = startdatetime;
    }

    /**
     * 終了日時
     * @return enddatetime
     */
    public Timestamp getEnddatetime() {
        return enddatetime;
    }

    /**
     * 終了日時
     * @param enddatetime セットする enddatetime
     */
    public void setEnddatetime(Timestamp enddatetime) {
        this.enddatetime = enddatetime;
    }

    /**
     * 先行外観
     * @return senkougaikan
     */
    public String getSenkougaikan() {
        return senkougaikan;
    }

    /**
     * 先行外観
     * @param senkougaikan セットする senkougaikan
     */
    public void setSenkougaikan(String senkougaikan) {
        this.senkougaikan = senkougaikan;
    }

    /**
     * 磁器L寸法
     * @return jikilsunpou
     */
    public Integer getJikilsunpou() {
        return jikilsunpou;
    }

    /**
     * 磁器L寸法
     * @param jikilsunpou セットする jikilsunpou
     */
    public void setJikilsunpou(Integer jikilsunpou) {
        this.jikilsunpou = jikilsunpou;
    }

    /**
     * 磁器T寸法
     * @return jikitsunpou
     */
    public Integer getJikitsunpou() {
        return jikitsunpou;
    }

    /**
     * 磁器T寸法
     * @param jikitsunpou セットする jikitsunpou
     */
    public void setJikitsunpou(Integer jikitsunpou) {
        this.jikitsunpou = jikitsunpou;
    }

    /**
     * P寸法1OLD
     * @return psunpou1a
     */
    public Integer getPsunpou1a() {
        return psunpou1a;
    }

    /**
     * P寸法1OLD
     * @param psunpou1a セットする psunpou1a
     */
    public void setPsunpou1a(Integer psunpou1a) {
        this.psunpou1a = psunpou1a;
    }

    /**
     * P寸法2OLD
     * @return psunpou1b
     */
    public Integer getPsunpou1b() {
        return psunpou1b;
    }

    /**
     * P寸法2OLD
     * @param psunpou1b セットする psunpou1b
     */
    public void setPsunpou1b(Integer psunpou1b) {
        this.psunpou1b = psunpou1b;
    }

    /**
     * P寸法3OLD
     * @return psunpou1c
     */
    public Integer getPsunpou1c() {
        return psunpou1c;
    }

    /**
     * P寸法3OLD
     * @param psunpou1c セットする psunpou1c
     */
    public void setPsunpou1c(Integer psunpou1c) {
        this.psunpou1c = psunpou1c;
    }

    /**
     * P寸法4OLD
     * @return psunpou1d
     */
    public Integer getPsunpou1d() {
        return psunpou1d;
    }

    /**
     * P寸法4OLD
     * @param psunpou1d セットする psunpou1d
     */
    public void setPsunpou1d(Integer psunpou1d) {
        this.psunpou1d = psunpou1d;
    }

    /**
     * P寸法5OLD
     * @return psunpou1e
     */
    public Integer getPsunpou1e() {
        return psunpou1e;
    }

    /**
     * P寸法5OLD
     * @param psunpou1e セットする psunpou1e
     */
    public void setPsunpou1e(Integer psunpou1e) {
        this.psunpou1e = psunpou1e;
    }

    /**
     * P寸法AVEOLD
     * @return psunpouave1
     */
    public BigDecimal getPsunpouave1() {
        return psunpouave1;
    }

    /**
     * P寸法AVEOLD
     * @param psunpouave1 セットする psunpouave1
     */
    public void setPsunpouave1(BigDecimal psunpouave1) {
        this.psunpouave1 = psunpouave1;
    }

    /**
     * P寸法RANGEOLD
     * @return psunpourange1
     */
    public Integer getPsunpourange1() {
        return psunpourange1;
    }

    /**
     * P寸法RANGEOLD
     * @param psunpourange1 セットする psunpourange1
     */
    public void setPsunpourange1(Integer psunpourange1) {
        this.psunpourange1 = psunpourange1;
    }

    /**
     * L寸法1OLD
     * @return lsunpou1a
     */
    public Integer getLsunpou1a() {
        return lsunpou1a;
    }

    /**
     * L寸法1OLD
     * @param lsunpou1a セットする lsunpou1a
     */
    public void setLsunpou1a(Integer lsunpou1a) {
        this.lsunpou1a = lsunpou1a;
    }

    /**
     * L寸法2OLD
     * @return lsunpou1b
     */
    public Integer getLsunpou1b() {
        return lsunpou1b;
    }

    /**
     * L寸法2OLD
     * @param lsunpou1b セットする lsunpou1b
     */
    public void setLsunpou1b(Integer lsunpou1b) {
        this.lsunpou1b = lsunpou1b;
    }

    /**
     * L寸法3OLD
     * @return lsunpou1c
     */
    public Integer getLsunpou1c() {
        return lsunpou1c;
    }

    /**
     * L寸法3OLD
     * @param lsunpou1c セットする lsunpou1c
     */
    public void setLsunpou1c(Integer lsunpou1c) {
        this.lsunpou1c = lsunpou1c;
    }

    /**
     * L寸法4OLD
     * @return lsunpou1d
     */
    public Integer getLsunpou1d() {
        return lsunpou1d;
    }

    /**
     * L寸法4OLD
     * @param lsunpou1d セットする lsunpou1d
     */
    public void setLsunpou1d(Integer lsunpou1d) {
        this.lsunpou1d = lsunpou1d;
    }

    /**
     * L寸法5OLD
     * @return lsunpou1e
     */
    public Integer getLsunpou1e() {
        return lsunpou1e;
    }

    /**
     * L寸法5OLD
     * @param lsunpou1e セットする lsunpou1e
     */
    public void setLsunpou1e(Integer lsunpou1e) {
        this.lsunpou1e = lsunpou1e;
    }

    /**
     * 端面厚みOLD
     * @return tanmenatsumi1
     */
    public BigDecimal getTanmenatsumi1() {
        return tanmenatsumi1;
    }

    /**
     * 端面厚みOLD
     * @param tanmenatsumi1 セットする tanmenatsumi1
     */
    public void setTanmenatsumi1(BigDecimal tanmenatsumi1) {
        this.tanmenatsumi1 = tanmenatsumi1;
    }

    /**
     * 判定OLD
     * @return sunpouhantei1
     */
    public String getSunpouhantei1() {
        return sunpouhantei1;
    }

    /**
     * 判定OLD
     * @param sunpouhantei1 セットする sunpouhantei1
     */
    public void setSunpouhantei1(String sunpouhantei1) {
        this.sunpouhantei1 = sunpouhantei1;
    }

    /**
     * P寸法1
     * @return psunpou2a
     */
    public Integer getPsunpou2a() {
        return psunpou2a;
    }

    /**
     * P寸法1
     * @param psunpou2a セットする psunpou2a
     */
    public void setPsunpou2a(Integer psunpou2a) {
        this.psunpou2a = psunpou2a;
    }

    /**
     * P寸法2
     * @return psunpou2b
     */
    public Integer getPsunpou2b() {
        return psunpou2b;
    }

    /**
     * P寸法2
     * @param psunpou2b セットする psunpou2b
     */
    public void setPsunpou2b(Integer psunpou2b) {
        this.psunpou2b = psunpou2b;
    }

    /**
     * P寸法3
     * @return psunpou2c
     */
    public Integer getPsunpou2c() {
        return psunpou2c;
    }

    /**
     * P寸法3
     * @param psunpou2c セットする psunpou2c
     */
    public void setPsunpou2c(Integer psunpou2c) {
        this.psunpou2c = psunpou2c;
    }

    /**
     * P寸法4
     * @return psunpou2d
     */
    public Integer getPsunpou2d() {
        return psunpou2d;
    }

    /**
     * P寸法4
     * @param psunpou2d セットする psunpou2d
     */
    public void setPsunpou2d(Integer psunpou2d) {
        this.psunpou2d = psunpou2d;
    }

    /**
     * P寸法5
     * @return psunpou2e
     */
    public Integer getPsunpou2e() {
        return psunpou2e;
    }

    /**
     * P寸法5
     * @param psunpou2e セットする psunpou2e
     */
    public void setPsunpou2e(Integer psunpou2e) {
        this.psunpou2e = psunpou2e;
    }

    /**
     * P寸法AVE
     * @return psunpouave2
     */
    public BigDecimal getPsunpouave2() {
        return psunpouave2;
    }

    /**
     * P寸法AVE
     * @param psunpouave2 セットする psunpouave2
     */
    public void setPsunpouave2(BigDecimal psunpouave2) {
        this.psunpouave2 = psunpouave2;
    }

    /**
     * P寸法RANGE
     * @return psunpourange2
     */
    public Integer getPsunpourange2() {
        return psunpourange2;
    }

    /**
     * P寸法RANGE
     * @param psunpourange2 セットする psunpourange2
     */
    public void setPsunpourange2(Integer psunpourange2) {
        this.psunpourange2 = psunpourange2;
    }

    /**
     * L寸法1
     * @return lsunpou2a
     */
    public Integer getLsunpou2a() {
        return lsunpou2a;
    }

    /**
     * L寸法1
     * @param lsunpou2a セットする lsunpou2a
     */
    public void setLsunpou2a(Integer lsunpou2a) {
        this.lsunpou2a = lsunpou2a;
    }

    /**
     * L寸法2
     * @return lsunpou2b
     */
    public Integer getLsunpou2b() {
        return lsunpou2b;
    }

    /**
     * L寸法2
     * @param lsunpou2b セットする lsunpou2b
     */
    public void setLsunpou2b(Integer lsunpou2b) {
        this.lsunpou2b = lsunpou2b;
    }

    /**
     * L寸法3
     * @return lsunpou2c
     */
    public Integer getLsunpou2c() {
        return lsunpou2c;
    }

    /**
     * L寸法3
     * @param lsunpou2c セットする lsunpou2c
     */
    public void setLsunpou2c(Integer lsunpou2c) {
        this.lsunpou2c = lsunpou2c;
    }

    /**
     * L寸法4
     * @return lsunpou2d
     */
    public Integer getLsunpou2d() {
        return lsunpou2d;
    }

    /**
     * L寸法4
     * @param lsunpou2d セットする lsunpou2d
     */
    public void setLsunpou2d(Integer lsunpou2d) {
        this.lsunpou2d = lsunpou2d;
    }

    /**
     * L寸法5
     * @return lsunpou2e
     */
    public Integer getLsunpou2e() {
        return lsunpou2e;
    }

    /**
     * L寸法5
     * @param lsunpou2e セットする lsunpou2e
     */
    public void setLsunpou2e(Integer lsunpou2e) {
        this.lsunpou2e = lsunpou2e;
    }

    /**
     * 端面厚み
     * @return tanmenatsumi2
     */
    public BigDecimal getTanmenatsumi2() {
        return tanmenatsumi2;
    }

    /**
     * 端面厚み
     * @param tanmenatsumi2 セットする tanmenatsumi2
     */
    public void setTanmenatsumi2(BigDecimal tanmenatsumi2) {
        this.tanmenatsumi2 = tanmenatsumi2;
    }

    /**
     * 判定
     * @return sunpouhantei2
     */
    public String getSunpouhantei2() {
        return sunpouhantei2;
    }

    /**
     * 判定
     * @param sunpouhantei2 セットする sunpouhantei2
     */
    public void setSunpouhantei2(String sunpouhantei2) {
        this.sunpouhantei2 = sunpouhantei2;
    }

    /**
     * ﾍﾟｰｽﾄ厚み設定値1次
     * @return pasteatsumi1
     */
    public Integer getPasteatsumi1() {
        return pasteatsumi1;
    }

    /**
     * ﾍﾟｰｽﾄ厚み設定値1次
     * @param pasteatsumi1 セットする pasteatsumi1
     */
    public void setPasteatsumi1(Integer pasteatsumi1) {
        this.pasteatsumi1 = pasteatsumi1;
    }

    /**
     * ﾍﾟｰｽﾄ厚み設定値2次
     * @return pasteatsumi2
     */
    public Integer getPasteatsumi2() {
        return pasteatsumi2;
    }

    /**
     * ﾍﾟｰｽﾄ厚み設定値2次
     * @param pasteatsumi2 セットする pasteatsumi2
     */
    public void setPasteatsumi2(Integer pasteatsumi2) {
        this.pasteatsumi2 = pasteatsumi2;
    }

    /**
     * DIP治具ｻｲｽﾞ
     * @return dipjigusize
     */
    public Integer getDipjigusize() {
        return dipjigusize;
    }

    /**
     * DIP治具ｻｲｽﾞ
     * @param dipjigusize セットする dipjigusize
     */
    public void setDipjigusize(Integer dipjigusize) {
        this.dipjigusize = dipjigusize;
    }

    /**
     * DIP治具枚数
     * @return dipjigumaisuu
     */
    public Integer getDipjigumaisuu() {
        return dipjigumaisuu;
    }

    /**
     * DIP治具枚数
     * @param dipjigumaisuu セットする dipjigumaisuu
     */
    public void setDipjigumaisuu(Integer dipjigumaisuu) {
        this.dipjigumaisuu = dipjigumaisuu;
    }

    /**
     * DIP後外観結果
     * @return dipgogaikankekka
     */
    public String getDipgogaikankekka() {
        return dipgogaikankekka;
    }

    /**
     * DIP後外観結果
     * @param dipgogaikankekka セットする dipgogaikankekka
     */
    public void setDipgogaikankekka(String dipgogaikankekka) {
        this.dipgogaikankekka = dipgogaikankekka;
    }

    /**
     * 処置内容
     * @return syochinaiyou
     */
    public String getSyochinaiyou() {
        return syochinaiyou;
    }

    /**
     * 処置内容
     * @param syochinaiyou セットする syochinaiyou
     */
    public void setSyochinaiyou(String syochinaiyou) {
        this.syochinaiyou = syochinaiyou;
    }

    /**
     * ｲﾝｸ厚みa
     * @return inkuatsumia
     */
    public String getInkuatsumia() {
        return inkuatsumia;
    }

    /**
     * ｲﾝｸ厚みa
     * @param inkuatsumia セットする inkuatsumia
     */
    public void setInkuatsumia(String inkuatsumia) {
        this.inkuatsumia = inkuatsumia;
    }

    /**
     * ｲﾝｸ厚みb
     * @return inkuatsumib
     */
    public String getInkuatsumib() {
        return inkuatsumib;
    }

    /**
     * ｲﾝｸ厚みb
     * @param inkuatsumib セットする inkuatsumib
     */
    public void setInkuatsumib(String inkuatsumib) {
        this.inkuatsumib = inkuatsumib;
    }

    /**
     * 設定条件
     * @return setteijyouken
     */
    public String getSetteijyouken() {
        return setteijyouken;
    }

    /**
     * 設定条件
     * @param setteijyouken セットする setteijyouken
     */
    public void setSetteijyouken(String setteijyouken) {
        this.setteijyouken = setteijyouken;
    }

    /**
     * 備考1
     * @return bikou1
     */
    public String getBikou1() {
        return bikou1;
    }

    /**
     * 備考1
     * @param bikou1 セットする bikou1
     */
    public void setBikou1(String bikou1) {
        this.bikou1 = bikou1;
    }

    /**
     * 備考2
     * @return bikou2
     */
    public String getBikou2() {
        return bikou2;
    }

    /**
     * 備考2
     * @param bikou2 セットする bikou2
     */
    public void setBikou2(String bikou2) {
        this.bikou2 = bikou2;
    }

    /**
     * 備考3
     * @return bikou3
     */
    public String getBikou3() {
        return bikou3;
    }

    /**
     * 備考3
     * @param bikou3 セットする bikou3
     */
    public void setBikou3(String bikou3) {
        this.bikou3 = bikou3;
    }

    /**
     * P寸法6OLD
     * @return psunpou1f
     */
    public Integer getPsunpou1f() {
        return psunpou1f;
    }

    /**
     * P寸法6OLD
     * @param psunpou1f セットする psunpou1f
     */
    public void setPsunpou1f(Integer psunpou1f) {
        this.psunpou1f = psunpou1f;
    }

    /**
     * P寸法7OLD
     * @return psunpou1g
     */
    public Integer getPsunpou1g() {
        return psunpou1g;
    }

    /**
     * P寸法7OLD
     * @param psunpou1g セットする psunpou1g
     */
    public void setPsunpou1g(Integer psunpou1g) {
        this.psunpou1g = psunpou1g;
    }

    /**
     * P寸法8OLD
     * @return psunpou1h
     */
    public Integer getPsunpou1h() {
        return psunpou1h;
    }

    /**
     * P寸法8OLD
     * @param psunpou1h セットする psunpou1h
     */
    public void setPsunpou1h(Integer psunpou1h) {
        this.psunpou1h = psunpou1h;
    }

    /**
     * P寸法9OLD
     * @return psunpou1i
     */
    public Integer getPsunpou1i() {
        return psunpou1i;
    }

    /**
     * P寸法9OLD
     * @param psunpou1i セットする psunpou1i
     */
    public void setPsunpou1i(Integer psunpou1i) {
        this.psunpou1i = psunpou1i;
    }

    /**
     * P寸法10OLD
     * @return psunpou1j
     */
    public Integer getPsunpou1j() {
        return psunpou1j;
    }

    /**
     * P寸法10OLD
     * @param psunpou1j セットする psunpou1j
     */
    public void setPsunpou1j(Integer psunpou1j) {
        this.psunpou1j = psunpou1j;
    }

    /**
     * L寸法6OLD
     * @return lsunpou1f
     */
    public Integer getLsunpou1f() {
        return lsunpou1f;
    }

    /**
     * L寸法6OLD
     * @param lsunpou1f セットする lsunpou1f
     */
    public void setLsunpou1f(Integer lsunpou1f) {
        this.lsunpou1f = lsunpou1f;
    }

    /**
     * L寸法7OLD
     * @return lsunpou1g
     */
    public Integer getLsunpou1g() {
        return lsunpou1g;
    }

    /**
     * L寸法7OLD
     * @param lsunpou1g セットする lsunpou1g
     */
    public void setLsunpou1g(Integer lsunpou1g) {
        this.lsunpou1g = lsunpou1g;
    }

    /**
     * L寸法8OLD
     * @return lsunpou1h
     */
    public Integer getLsunpou1h() {
        return lsunpou1h;
    }

    /**
     * L寸法8OLD
     * @param lsunpou1h セットする lsunpou1h
     */
    public void setLsunpou1h(Integer lsunpou1h) {
        this.lsunpou1h = lsunpou1h;
    }

    /**
     * L寸法9OLD
     * @return lsunpou1i
     */
    public Integer getLsunpou1i() {
        return lsunpou1i;
    }

    /**
     * L寸法9OLD
     * @param lsunpou1i セットする lsunpou1i
     */
    public void setLsunpou1i(Integer lsunpou1i) {
        this.lsunpou1i = lsunpou1i;
    }

    /**
     * L寸法10OLD
     * @return lsunpou1j
     */
    public Integer getLsunpou1j() {
        return lsunpou1j;
    }

    /**
     * L寸法10OLD
     * @param lsunpou1j セットする lsunpou1j
     */
    public void setLsunpou1j(Integer lsunpou1j) {
        this.lsunpou1j = lsunpou1j;
    }

    /**
     * P寸法6
     * @return psunpou2f
     */
    public Integer getPsunpou2f() {
        return psunpou2f;
    }

    /**
     * P寸法6
     * @param psunpou2f セットする psunpou2f
     */
    public void setPsunpou2f(Integer psunpou2f) {
        this.psunpou2f = psunpou2f;
    }

    /**
     * P寸法7
     * @return psunpou2g
     */
    public Integer getPsunpou2g() {
        return psunpou2g;
    }

    /**
     * P寸法7
     * @param psunpou2g セットする psunpou2g
     */
    public void setPsunpou2g(Integer psunpou2g) {
        this.psunpou2g = psunpou2g;
    }

    /**
     * P寸法8
     * @return psunpou2h
     */
    public Integer getPsunpou2h() {
        return psunpou2h;
    }

    /**
     * P寸法8
     * @param psunpou2h セットする psunpou2h
     */
    public void setPsunpou2h(Integer psunpou2h) {
        this.psunpou2h = psunpou2h;
    }

    /**
     * P寸法9
     * @return psunpou2i
     */
    public Integer getPsunpou2i() {
        return psunpou2i;
    }

    /**
     * P寸法9
     * @param psunpou2i セットする psunpou2i
     */
    public void setPsunpou2i(Integer psunpou2i) {
        this.psunpou2i = psunpou2i;
    }

    /**
     * P寸法10
     * @return psunpou2j
     */
    public Integer getPsunpou2j() {
        return psunpou2j;
    }

    /**
     * P寸法10
     * @param psunpou2j セットする psunpou2j
     */
    public void setPsunpou2j(Integer psunpou2j) {
        this.psunpou2j = psunpou2j;
    }

    /**
     * L寸法6
     * @return lsunpou2f
     */
    public Integer getLsunpou2f() {
        return lsunpou2f;
    }

    /**
     * L寸法6
     * @param lsunpou2f セットする lsunpou2f
     */
    public void setLsunpou2f(Integer lsunpou2f) {
        this.lsunpou2f = lsunpou2f;
    }

    /**
     * L寸法7
     * @return lsunpou2g
     */
    public Integer getLsunpou2g() {
        return lsunpou2g;
    }

    /**
     * L寸法7
     * @param lsunpou2g セットする lsunpou2g
     */
    public void setLsunpou2g(Integer lsunpou2g) {
        this.lsunpou2g = lsunpou2g;
    }

    /**
     * L寸法8
     * @return lsunpou2h
     */
    public Integer getLsunpou2h() {
        return lsunpou2h;
    }

    /**
     * L寸法8
     * @param lsunpou2h セットする lsunpou2h
     */
    public void setLsunpou2h(Integer lsunpou2h) {
        this.lsunpou2h = lsunpou2h;
    }

    /**
     * L寸法9
     * @return lsunpou2i
     */
    public Integer getLsunpou2i() {
        return lsunpou2i;
    }

    /**
     * L寸法9
     * @param lsunpou2i セットする lsunpou2i
     */
    public void setLsunpou2i(Integer lsunpou2i) {
        this.lsunpou2i = lsunpou2i;
    }

    /**
     * L寸法10
     * @return lsunpou2j
     */
    public Integer getLsunpou2j() {
        return lsunpou2j;
    }

    /**
     * L寸法10
     * @param lsunpou2j セットする lsunpou2j
     */
    public void setLsunpou2j(Integer lsunpou2j) {
        this.lsunpou2j = lsunpou2j;
    }

    /**
     * L寸法AVEOLD
     * @return lsunpouave1
     */
    public BigDecimal getLsunpouave1() {
        return lsunpouave1;
    }

    /**
     * L寸法AVEOLD
     * @param lsunpouave1 セットする lsunpouave1
     */
    public void setLsunpouave1(BigDecimal lsunpouave1) {
        this.lsunpouave1 = lsunpouave1;
    }

    /**
     * L寸法AVE
     * @return lsunpouave2
     */
    public BigDecimal getLsunpouave2() {
        return lsunpouave2;
    }

    /**
     * L寸法AVE
     * @param lsunpouave2 セットする lsunpouave2
     */
    public void setLsunpouave2(BigDecimal lsunpouave2) {
        this.lsunpouave2 = lsunpouave2;
    }

    /**
     * ﾍﾟｰｽﾄ固形分
     * @return pastekokeibun
     */
    public BigDecimal getPastekokeibun() {
        return pastekokeibun;
    }

    /**
     * ﾍﾟｰｽﾄ固形分
     * @param pastekokeibun セットする pastekokeibun
     */
    public void setPastekokeibun(BigDecimal pastekokeibun) {
        this.pastekokeibun = pastekokeibun;
    }

    /**
     * 1次頭出し設定
     * @return dip1atamadashisettei
     */
    public BigDecimal getDip1atamadashisettei() {
        return dip1atamadashisettei;
    }

    /**
     * 1次頭出し設定
     * @param dip1atamadashisettei セットする dip1atamadashisettei
     */
    public void setDip1atamadashisettei(BigDecimal dip1atamadashisettei) {
        this.dip1atamadashisettei = dip1atamadashisettei;
    }

    /**
     * 1次DIPｸﾘｱﾗﾝｽ設定
     * @return dip1clearance
     */
    public BigDecimal getDip1clearance() {
        return dip1clearance;
    }

    /**
     * 1次DIPｸﾘｱﾗﾝｽ設定
     * @param dip1clearance セットする dip1clearance
     */
    public void setDip1clearance(BigDecimal dip1clearance) {
        this.dip1clearance = dip1clearance;
    }

    /**
     * 1次DIPｽｷｰｼﾞ設定
     * @return dip1skeegesettei
     */
    public BigDecimal getDip1skeegesettei() {
        return dip1skeegesettei;
    }

    /**
     * 1次DIPｽｷｰｼﾞ設定
     * @param dip1skeegesettei セットする dip1skeegesettei
     */
    public void setDip1skeegesettei(BigDecimal dip1skeegesettei) {
        this.dip1skeegesettei = dip1skeegesettei;
    }

    /**
     * 1次ﾌﾞﾛｯﾄｸﾘｱﾗﾝｽ設定
     * @return dip1blotclearance
     */
    public BigDecimal getDip1blotclearance() {
        return dip1blotclearance;
    }

    /**
     * 1次ﾌﾞﾛｯﾄｸﾘｱﾗﾝｽ設定
     * @param dip1blotclearance セットする dip1blotclearance
     */
    public void setDip1blotclearance(BigDecimal dip1blotclearance) {
        this.dip1blotclearance = dip1blotclearance;
    }

    /**
     * 1次ﾚﾍﾞﾗｰ設定
     * @return dip1reveler
     */
    public BigDecimal getDip1reveler() {
        return dip1reveler;
    }

    /**
     * 1次ﾚﾍﾞﾗｰ設定
     * @param dip1reveler セットする dip1reveler
     */
    public void setDip1reveler(BigDecimal dip1reveler) {
        this.dip1reveler = dip1reveler;
    }

    /**
     * 2次頭出し設定
     * @return dip2atamadashisettei
     */
    public BigDecimal getDip2atamadashisettei() {
        return dip2atamadashisettei;
    }

    /**
     * 2次頭出し設定
     * @param dip2atamadashisettei セットする dip2atamadashisettei
     */
    public void setDip2atamadashisettei(BigDecimal dip2atamadashisettei) {
        this.dip2atamadashisettei = dip2atamadashisettei;
    }

    /**
     * 2次DIPｸﾘｱﾗﾝｽ設定
     * @return dip2clearance
     */
    public BigDecimal getDip2clearance() {
        return dip2clearance;
    }

    /**
     * 2次DIPｸﾘｱﾗﾝｽ設定
     * @param dip2clearance セットする dip2clearance
     */
    public void setDip2clearance(BigDecimal dip2clearance) {
        this.dip2clearance = dip2clearance;
    }

    /**
     * 2次DIPｽｷｰｼﾞ設定
     * @return dip2skeegesettei
     */
    public BigDecimal getDip2skeegesettei() {
        return dip2skeegesettei;
    }

    /**
     * 2次DIPｽｷｰｼﾞ設定
     * @param dip2skeegesettei セットする dip2skeegesettei
     */
    public void setDip2skeegesettei(BigDecimal dip2skeegesettei) {
        this.dip2skeegesettei = dip2skeegesettei;
    }

    /**
     * 2次ﾌﾞﾛｯﾄｸﾘｱﾗﾝｽ設定
     * @return dip2blotclearance
     */
    public BigDecimal getDip2blotclearance() {
        return dip2blotclearance;
    }

    /**
     * 2次ﾌﾞﾛｯﾄｸﾘｱﾗﾝｽ設定
     * @param dip2blotclearance セットする dip2blotclearance
     */
    public void setDip2blotclearance(BigDecimal dip2blotclearance) {
        this.dip2blotclearance = dip2blotclearance;
    }

    /**
     * 2次ﾚﾍﾞﾗｰ設定
     * @return dip2reveler
     */
    public BigDecimal getDip2reveler() {
        return dip2reveler;
    }

    /**
     * 2次ﾚﾍﾞﾗｰ設定
     * @param dip2reveler セットする dip2reveler
     */
    public void setDip2reveler(BigDecimal dip2reveler) {
        this.dip2reveler = dip2reveler;
    }

    /**
     * P寸法11
     * @return psunpou2k
     */
    public Integer getPsunpou2k() {
        return psunpou2k;
    }

    /**
     * P寸法11
     * @param psunpou2k セットする psunpou2k
     */
    public void setPsunpou2k(Integer psunpou2k) {
        this.psunpou2k = psunpou2k;
    }

    /**
     * P寸法12
     * @return psunpou2l
     */
    public Integer getPsunpou2l() {
        return psunpou2l;
    }

    /**
     * P寸法12
     * @param psunpou2l セットする psunpou2l
     */
    public void setPsunpou2l(Integer psunpou2l) {
        this.psunpou2l = psunpou2l;
    }

    /**
     * P寸法13
     * @return psunpou2m
     */
    public Integer getPsunpou2m() {
        return psunpou2m;
    }

    /**
     * P寸法13
     * @param psunpou2m セットする psunpou2m
     */
    public void setPsunpou2m(Integer psunpou2m) {
        this.psunpou2m = psunpou2m;
    }

    /**
     * P寸法14
     * @return psunpou2n
     */
    public Integer getPsunpou2n() {
        return psunpou2n;
    }

    /**
     * P寸法14
     * @param psunpou2n セットする psunpou2n
     */
    public void setPsunpou2n(Integer psunpou2n) {
        this.psunpou2n = psunpou2n;
    }

    /**
     * P寸法15
     * @return psunpou2o
     */
    public Integer getPsunpou2o() {
        return psunpou2o;
    }

    /**
     * P寸法15
     * @param psunpou2o セットする psunpou2o 
     */
    public void setPsunpou2o(Integer psunpou2o) {
        this.psunpou2o = psunpou2o;
    }

    /**
     * P寸法16
     * @return psunpou2p
     */
    public Integer getPsunpou2p() {
        return psunpou2p;
    }

    /**
     * P寸法16
     * @param psunpou2p セットする psunpou2p
     */
    public void setPsunpou2p(Integer psunpou2p) {
        this.psunpou2p = psunpou2p;
    }

    /**
     * P寸法17
     * @return psunpou2q
     */
    public Integer getPsunpou2q() {
        return psunpou2q;
    }

    /**
     * P寸法17
     * @param psunpou2q セットする psunpou2q
     */
    public void setPsunpou2q(Integer psunpou2q) {
        this.psunpou2q = psunpou2q;
    }

    /**
     * P寸法18
     * @return psunpou2r
     */
    public Integer getPsunpou2r() {
        return psunpou2r;
    }

    /**
     * P寸法18
     * @param psunpou2r セットする psunpou2r
     */
    public void setPsunpou2r(Integer psunpou2r) {
        this.psunpou2r = psunpou2r;
    }

    /**
     * P寸法19
     * @return psunpou2s
     */
    public Integer getPsunpou2s() {
        return psunpou2s;
    }

    /**
     * P寸法19
     * @param psunpou2s セットする psunpou2s
     */
    public void setPsunpou2s(Integer psunpou2s) {
        this.psunpou2s = psunpou2s;
    }

    /**
     * P寸法20
     * @return psunpou2t
     */
    public Integer getPsunpou2t() {
        return psunpou2t;
    }

    /**
     * P寸法20
     * @param psunpou2t セットする psunpou2t
     */
    public void setPsunpou2t(Integer psunpou2t) {
        this.psunpou2t = psunpou2t;
    }

    /**
     * P寸法21
     * @return psunpou2u
     */
    public Integer getPsunpou2u() {
        return psunpou2u;
    }

    /**
     * P寸法21
     * @param psunpou2u セットする psunpou2u
     */
    public void setPsunpou2u(Integer psunpou2u) {
        this.psunpou2u = psunpou2u;
    }

    /**
     * P寸法22
     * @return psunpou2v
     */
    public Integer getPsunpou2v() {
        return psunpou2v;
    }

    /**
     * P寸法22
     * @param psunpou2v セットする psunpou2v
     */
    public void setPsunpou2v(Integer psunpou2v) {
        this.psunpou2v = psunpou2v;
    }

    /**
     * P寸法23
     * @return psunpou2w
     */
    public Integer getPsunpou2w() {
        return psunpou2w;
    }

    /**
     * P寸法23
     * @param psunpou2w セットする psunpou2w
     */
    public void setPsunpou2w(Integer psunpou2w) {
        this.psunpou2w = psunpou2w;
    }

    /**
     * P寸法24
     * @return psunpou2x
     */
    public Integer getPsunpou2x() {
        return psunpou2x;
    }

    /**
     * P寸法24
     * @param psunpou2x セットする psunpou2x
     */
    public void setPsunpou2x(Integer psunpou2x) {
        this.psunpou2x = psunpou2x;
    }

    /**
     * P寸法25
     * @return psunpou2y
     */
    public Integer getPsunpou2y() {
        return psunpou2y;
    }

    /**
     * P寸法25
     * @param psunpou2y セットする psunpou2y
     */
    public void setPsunpou2y(Integer psunpou2y) {
        this.psunpou2y = psunpou2y;
    }

    /**
     * P寸法26
     * @return psunpou2z
     */
    public Integer getPsunpou2z() {
        return psunpou2z;
    }

    /**
     * P寸法26
     * @param psunpou2z セットする psunpou2z
     */
    public void setPsunpou2z(Integer psunpou2z) {
        this.psunpou2z = psunpou2z;
    }

    /**
     * P寸法27
     * @return psunpou2aa
     */
    public Integer getPsunpou2aa() {
        return psunpou2aa;
    }

    /**
     * P寸法27
     * @param psunpou2aa セットする psunpou2aa
     */
    public void setPsunpou2aa(Integer psunpou2aa) {
        this.psunpou2aa = psunpou2aa;
    }

    /**
     * P寸法28
     * @return psunpou2ab
     */
    public Integer getPsunpou2ab() {
        return psunpou2ab;
    }

    /**
     * P寸法28
     * @param psunpou2ab セットする psunpou2ab
     */
    public void setPsunpou2ab(Integer psunpou2ab) {
        this.psunpou2ab = psunpou2ab;
    }

    /**
     * P寸法29
     * @return psunpou2ac
     */
    public Integer getPsunpou2ac() {
        return psunpou2ac;
    }

    /**
     * P寸法29
     * @param psunpou2ac セットする psunpou2ac
     */
    public void setPsunpou2ac(Integer psunpou2ac) {
        this.psunpou2ac = psunpou2ac;
    }

    /**
     * P寸法30
     * @return psunpou2ad
     */
    public Integer getPsunpou2ad() {
        return psunpou2ad;
    }

    /**
     * P寸法30
     * @param psunpou2ad セットする psunpou2ad
     */
    public void setPsunpou2ad(Integer psunpou2ad) {
        this.psunpou2ad = psunpou2ad;
    }

    /**
     * P寸法31
     * @return psunpou2ae
     */
    public Integer getPsunpou2ae() {
        return psunpou2ae;
    }

    /**
     * P寸法31
     * @param psunpou2ae セットする psunpou2ae
     */
    public void setPsunpou2ae(Integer psunpou2ae) {
        this.psunpou2ae = psunpou2ae;
    }

    /**
     * P寸法32
     * @return psunpou2af
     */
    public Integer getPsunpou2af() {
        return psunpou2af;
    }

    /**
     * P寸法32
     * @param psunpou2af セットする psunpou2af
     */
    public void setPsunpou2af(Integer psunpou2af) {
        this.psunpou2af = psunpou2af;
    }

    /**
     * P寸法33
     * @return psunpou2ag
     */
    public Integer getPsunpou2ag() {
        return psunpou2ag;
    }

    /**
     * P寸法33
     * @param psunpou2ag セットする psunpou2ag
     */
    public void setPsunpou2ag(Integer psunpou2ag) {
        this.psunpou2ag = psunpou2ag;
    }

    /**
     * P寸法34
     * @return psunpou2ah
     */
    public Integer getPsunpou2ah() {
        return psunpou2ah;
    }

    /**
     * P寸法34
     * @param psunpou2ah セットする psunpou2ah
     */
    public void setPsunpou2ah(Integer psunpou2ah) {
        this.psunpou2ah = psunpou2ah;
    }

    /**
     * P寸法35
     * @return psunpou2ai
     */
    public Integer getPsunpou2ai() {
        return psunpou2ai;
    }

    /**
     * P寸法35
     * @param psunpou2ai セットする psunpou2ai
     */
    public void setPsunpou2ai(Integer psunpou2ai) {
        this.psunpou2ai = psunpou2ai;
    }

    /**
     * P寸法36
     * @return psunpou2aj
     */
    public Integer getPsunpou2aj() {
        return psunpou2aj;
    }

    /**
     * P寸法36
     * @param psunpou2aj セットする psunpou2aj
     */
    public void setPsunpou2aj(Integer psunpou2aj) {
        this.psunpou2aj = psunpou2aj;
    }

    /**
     * P寸法37
     * @return psunpou2ak
     */
    public Integer getPsunpou2ak() {
        return psunpou2ak;
    }

    /**
     * P寸法37
     * @param psunpou2ak セットする psunpou2ak
     */
    public void setPsunpou2ak(Integer psunpou2ak) {
        this.psunpou2ak = psunpou2ak;
    }

    /**
     * P寸法38
     * @return psunpou2al
     */
    public Integer getPsunpou2al() {
        return psunpou2al;
    }

    /**
     * P寸法38
     * @param psunpou2al セットする psunpou2al
     */
    public void setPsunpou2al(Integer psunpou2al) {
        this.psunpou2al = psunpou2al;
    }

    /**
     * P寸法39
     * @return psunpou2am
     */
    public Integer getPsunpou2am() {
        return psunpou2am;
    }

    /**
     * P寸法39
     * @param psunpou2am セットする psunpou2am
     */
    public void setPsunpou2am(Integer psunpou2am) {
        this.psunpou2am = psunpou2am;
    }

    /**
     * P寸法40
     * @return 
     */
    public Integer getPsunpou2an() {
        return psunpou2an;
    }

    /**
     * P寸法40
     * @param psunpou2an セットする psunpou2an
     */
    public void setPsunpou2an(Integer psunpou2an) {
        this.psunpou2an = psunpou2an;
    }

    /**
     * L寸法11
     * @return 
     */
    public Integer getLsunpou2k() {
        return lsunpou2k;
    }

    /**
     * L寸法11
     * @param lsunpou2k セットする lsunpou2k
     */
    public void setLsunpou2k(Integer lsunpou2k) {
        this.lsunpou2k = lsunpou2k;
    }

    /**
     * L寸法12
     * @return 
     */
    public Integer getLsunpou2l() {
        return lsunpou2l;
    }

    /**
     * L寸法12
     * @param lsunpou2l セットする lsunpou2l
     */
    public void setLsunpou2l(Integer lsunpou2l) {
        this.lsunpou2l = lsunpou2l;
    }

    /**
     * L寸法13
     * @return 
     */
    public Integer getLsunpou2m() {
        return lsunpou2m;
    }

    /**
     * L寸法13
     * @param lsunpou2m セットする lsunpou2m
     */
    public void setLsunpou2m(Integer lsunpou2m) {
        this.lsunpou2m = lsunpou2m;
    }

    /**
     * L寸法14
     * @return 
     */
    public Integer getLsunpou2n() {
        return lsunpou2n;
    }

    /**
     * L寸法14
     * @param lsunpou2n セットする lsunpou2n
     */
    public void setLsunpou2n(Integer lsunpou2n) {
        this.lsunpou2n = lsunpou2n;
    }

    /**
     * L寸法15
     * @return lsunpou2o
     */
    public Integer getLsunpou2o() {
        return lsunpou2o;
    }

    /**
     * L寸法15
     * @param lsunpou2o セットする lsunpou2o
     */
    public void setLsunpou2o(Integer lsunpou2o) {
        this.lsunpou2o = lsunpou2o;
    }

    /**
     * L寸法16
     * @return lsunpou2p
     */
    public Integer getLsunpou2p() {
        return lsunpou2p;
    }

    /**
     * L寸法16
     * @param lsunpou2p セットする lsunpou2p
     */
    public void setLsunpou2p(Integer lsunpou2p) {
        this.lsunpou2p = lsunpou2p;
    }

    /**
     * L寸法17
     * @return lsunpou2q
     */
    public Integer getLsunpou2q() {
        return lsunpou2q;
    }

    /**
     * L寸法17
     * @param lsunpou2q セットする lsunpou2q
     */
    public void setLsunpou2q(Integer lsunpou2q) {
        this.lsunpou2q = lsunpou2q;
    }

    /**
     * L寸法18
     * @return lsunpou2r
     */
    public Integer getLsunpou2r() {
        return lsunpou2r;
    }

    /**
     * L寸法18
     * @param lsunpou2r セットする lsunpou2r
     */
    public void setLsunpou2r(Integer lsunpou2r) {
        this.lsunpou2r = lsunpou2r;
    }

    /**
     * L寸法19
     * @return lsunpou2s
     */
    public Integer getLsunpou2s() {
        return lsunpou2s;
    }

    /**
     * L寸法19
     * @param lsunpou2s セットする lsunpou2s
     */
    public void setLsunpou2s(Integer lsunpou2s) {
        this.lsunpou2s = lsunpou2s;
    }

    /**
     * L寸法20
     * @return lsunpou2t
     */
    public Integer getLsunpou2t() {
        return lsunpou2t;
    }

    /**
     * L寸法20
     * @param lsunpou2t セットする lsunpou2t
     */
    public void setLsunpou2t(Integer lsunpou2t) {
        this.lsunpou2t = lsunpou2t;
    }

    /**
     * L寸法21
     * @return lsunpou2u
     */
    public Integer getLsunpou2u() {
        return lsunpou2u;
    }

    /**
     * L寸法21
     * @param lsunpou2u セットする lsunpou2u
     */
    public void setLsunpou2u(Integer lsunpou2u) {
        this.lsunpou2u = lsunpou2u;
    }

    /**
     * L寸法22
     * @return lsunpou2v
     */
    public Integer getLsunpou2v() {
        return lsunpou2v;
    }

    /**
     * L寸法22
     * @param lsunpou2v セットする lsunpou2v
     */
    public void setLsunpou2v(Integer lsunpou2v) {
        this.lsunpou2v = lsunpou2v;
    }

    /**
     * L寸法23
     * @return lsunpou2w
     */
    public Integer getLsunpou2w() {
        return lsunpou2w;
    }

    /**
     * L寸法23
     * @param lsunpou2w セットする lsunpou2w
     */
    public void setLsunpou2w(Integer lsunpou2w) {
        this.lsunpou2w = lsunpou2w;
    }

    /**
     * L寸法24
     * @return lsunpou2x
     */
    public Integer getLsunpou2x() {
        return lsunpou2x;
    }

    /**
     * L寸法24
     * @param lsunpou2x セットする lsunpou2x
     */
    public void setLsunpou2x(Integer lsunpou2x) {
        this.lsunpou2x = lsunpou2x;
    }

    /**
     * L寸法25
     * @return lsunpou2y
     */
    public Integer getLsunpou2y() {
        return lsunpou2y;
    }

    /**
     * L寸法25
     * @param lsunpou2y セットする lsunpou2y
     */
    public void setLsunpou2y(Integer lsunpou2y) {
        this.lsunpou2y = lsunpou2y;
    }

    /**
     * L寸法26
     * @return lsunpou2z
     */
    public Integer getLsunpou2z() {
        return lsunpou2z;
    }

    /**
     * L寸法26
     * @param lsunpou2z セットする lsunpou2z
     */
    public void setLsunpou2z(Integer lsunpou2z) {
        this.lsunpou2z = lsunpou2z;
    }

    /**
     * L寸法27
     * @return lsunpou2aa
     */
    public Integer getLsunpou2aa() {
        return lsunpou2aa;
    }

    /**
     * L寸法27
     * @param lsunpou2aa セットする lsunpou2aa
     */
    public void setLsunpou2aa(Integer lsunpou2aa) {
        this.lsunpou2aa = lsunpou2aa;
    }

    /**
     * L寸法28
     * @return lsunpou2ab
     */
    public Integer getLsunpou2ab() {
        return lsunpou2ab;
    }

    /**
     * L寸法28
     * @param lsunpou2ab セットする lsunpou2ab
     */
    public void setLsunpou2ab(Integer lsunpou2ab) {
        this.lsunpou2ab = lsunpou2ab;
    }

    /**
     * L寸法29
     * @return lsunpou2ac
     */
    public Integer getLsunpou2ac() {
        return lsunpou2ac;
    }

    /**
     * L寸法29
     * @param lsunpou2ac セットする lsunpou2ac
     */
    public void setLsunpou2ac(Integer lsunpou2ac) {
        this.lsunpou2ac = lsunpou2ac;
    }

    /**
     * L寸法30
     * @return lsunpou2ad
     */
    public Integer getLsunpou2ad() {
        return lsunpou2ad;
    }

    /**
     * L寸法30
     * @param lsunpou2ad セットする lsunpou2ad
     */
    public void setLsunpou2ad(Integer lsunpou2ad) {
        this.lsunpou2ad = lsunpou2ad;
    }

    /**
     * L寸法31
     * @return lsunpou2ae
     */
    public Integer getLsunpou2ae() {
        return lsunpou2ae;
    }

    /**
     * L寸法31
     * @param lsunpou2ae セットする lsunpou2ae
     */
    public void setLsunpou2ae(Integer lsunpou2ae) {
        this.lsunpou2ae = lsunpou2ae;
    }

    /**
     * L寸法32
     * @return lsunpou2af
     */
    public Integer getLsunpou2af() {
        return lsunpou2af;
    }

    /**
     * L寸法32
     * @param lsunpou2af セットする lsunpou2af
     */
    public void setLsunpou2af(Integer lsunpou2af) {
        this.lsunpou2af = lsunpou2af;
    }

    /**
     * L寸法33
     * @return lsunpou2ag
     */
    public Integer getLsunpou2ag() {
        return lsunpou2ag;
    }

    /**
     * L寸法33
     * @param lsunpou2ag セットする lsunpou2ag
     */
    public void setLsunpou2ag(Integer lsunpou2ag) {
        this.lsunpou2ag = lsunpou2ag;
    }

    /**
     * L寸法34
     * @return lsunpou2ah
     */
    public Integer getLsunpou2ah() {
        return lsunpou2ah;
    }

    /**
     * L寸法34
     * @param lsunpou2ah セットする lsunpou2ah
     */
    public void setLsunpou2ah(Integer lsunpou2ah) {
        this.lsunpou2ah = lsunpou2ah;
    }

    /**
     * L寸法35
     * @return lsunpou2ai
     */
    public Integer getLsunpou2ai() {
        return lsunpou2ai;
    }

    /**
     * L寸法35
     * @param lsunpou2ai セットする lsunpou2ai
     */
    public void setLsunpou2ai(Integer lsunpou2ai) {
        this.lsunpou2ai = lsunpou2ai;
    }

    /**
     * L寸法36
     * @return lsunpou2aj
     */
    public Integer getLsunpou2aj() {
        return lsunpou2aj;
    }

    /**
     * L寸法36
     * @param lsunpou2aj セットする lsunpou2aj
     */
    public void setLsunpou2aj(Integer lsunpou2aj) {
        this.lsunpou2aj = lsunpou2aj;
    }

    /**
     * L寸法37
     * @return lsunpou2ak
     */
    public Integer getLsunpou2ak() {
        return lsunpou2ak;
    }

    /**
     * L寸法37
     * @param lsunpou2ak セットする lsunpou2ak
     */
    public void setLsunpou2ak(Integer lsunpou2ak) {
        this.lsunpou2ak = lsunpou2ak;
    }

    /**
     * L寸法38
     * @return lsunpou2al
     */
    public Integer getLsunpou2al() {
        return lsunpou2al;
    }

    /**
     * L寸法38
     * @param lsunpou2al セットする lsunpou2al
     */
    public void setLsunpou2al(Integer lsunpou2al) {
        this.lsunpou2al = lsunpou2al;
    }

    /**
     * L寸法39
     * @return lsunpou2am
     */
    public Integer getLsunpou2am() {
        return lsunpou2am;
    }

    /**
     * L寸法39
     * @param lsunpou2am セットする lsunpou2am
     */
    public void setLsunpou2am(Integer lsunpou2am) {
        this.lsunpou2am = lsunpou2am;
    }

    /**
     * L寸法40
     * @return lsunpou2an
     */
    public Integer getLsunpou2an() {
        return lsunpou2an;
    }

    /**
     * L寸法40
     * @param lsunpou2an セットする lsunpou2an
     */
    public void setLsunpou2an(Integer lsunpou2an) {
        this.lsunpou2an = lsunpou2an;
    }

    /**
     * L寸法RANGE
     * @return lsunpourange2
     */
    public Integer getLsunpourange2() {
        return lsunpourange2;
    }

    /**
     * L寸法RANGE
     * @param lsunpourange2 セットする lsunpourange2
     */
    public void setLsunpourange2(Integer lsunpourange2) {
        this.lsunpourange2 = lsunpourange2;
    }

    /**
     * L寸法MIN
     * @return lsunpoumin2
     */
    public BigDecimal getLsunpoumin2() {
        return lsunpoumin2;
    }

    /**
     * L寸法MIN
     * @param lsunpoumin2 セットする lsunpoumin2
     */
    public void setLsunpoumin2(BigDecimal lsunpoumin2) {
        this.lsunpoumin2 = lsunpoumin2;
    }

    /**
     * L寸法MAX
     * @return lsunpoumax2
     */
    public BigDecimal getLsunpoumax2() {
        return lsunpoumax2;
    }

    /**
     * L寸法MAX
     * @param lsunpoumax2 セットする lsunpoumax2
     */
    public void setLsunpoumax2(BigDecimal lsunpoumax2) {
        this.lsunpoumax2 = lsunpoumax2;
    }

    /**
     * WT寸法1
     * @return wtsunpou2a
     */
    public Integer getWtsunpou2a() {
        return wtsunpou2a;
    }

    /**
     * WT寸法1
     * @param wtsunpou2a セットする wtsunpou2a
     */
    public void setWtsunpou2a(Integer wtsunpou2a) {
        this.wtsunpou2a = wtsunpou2a;
    }

    /**
     * WT寸法2
     * @return wtsunpou2b
     */
    public Integer getWtsunpou2b() {
        return wtsunpou2b;
    }

    /**
     * WT寸法2
     * @param wtsunpou2b セットする wtsunpou2b
     */
    public void setWtsunpou2b(Integer wtsunpou2b) {
        this.wtsunpou2b = wtsunpou2b;
    }

    /**
     * WT寸法3
     * @return wtsunpou2c
     */
    public Integer getWtsunpou2c() {
        return wtsunpou2c;
    }

    /**
     * WT寸法3
     * @param wtsunpou2c セットする wtsunpou2c
     */
    public void setWtsunpou2c(Integer wtsunpou2c) {
        this.wtsunpou2c = wtsunpou2c;
    }

    /**
     * WT寸法4
     * @return wtsunpou2d
     */
    public Integer getWtsunpou2d() {
        return wtsunpou2d;
    }

    /**
     * WT寸法4
     * @param wtsunpou2d セットする wtsunpou2d
     */
    public void setWtsunpou2d(Integer wtsunpou2d) {
        this.wtsunpou2d = wtsunpou2d;
    }

    /**
     * WT寸法5
     * @return wtsunpou2e
     */
    public Integer getWtsunpou2e() {
        return wtsunpou2e;
    }

    /**
     * WT寸法5
     * @param wtsunpou2e セットする wtsunpou2e
     */
    public void setWtsunpou2e(Integer wtsunpou2e) {
        this.wtsunpou2e = wtsunpou2e;
    }

    /**
     * WT寸法6
     * @return wtsunpou2f
     */
    public Integer getWtsunpou2f() {
        return wtsunpou2f;
    }

    /**
     * WT寸法6
     * @param wtsunpou2f セットする wtsunpou2f
     */
    public void setWtsunpou2f(Integer wtsunpou2f) {
        this.wtsunpou2f = wtsunpou2f;
    }

    /**
     * WT寸法7
     * @return wtsunpou2g
     */
    public Integer getWtsunpou2g() {
        return wtsunpou2g;
    }

    /**
     * WT寸法7
     * @param wtsunpou2g セットする wtsunpou2g
     */
    public void setWtsunpou2g(Integer wtsunpou2g) {
        this.wtsunpou2g = wtsunpou2g;
    }

    /**
     * WT寸法8
     * @return wtsunpou2h
     */
    public Integer getWtsunpou2h() {
        return wtsunpou2h;
    }

    /**
     * WT寸法8
     * @param wtsunpou2h セットする wtsunpou2h
     */
    public void setWtsunpou2h(Integer wtsunpou2h) {
        this.wtsunpou2h = wtsunpou2h;
    }

    /**
     * WT寸法9
     * @return wtsunpou2i
     */
    public Integer getWtsunpou2i() {
        return wtsunpou2i;
    }

    /**
     * WT寸法9
     * @param wtsunpou2i セットする wtsunpou2i
     */
    public void setWtsunpou2i(Integer wtsunpou2i) {
        this.wtsunpou2i = wtsunpou2i;
    }

    /**
     * WT寸法10
     * @return wtsunpou2j
     */
    public Integer getWtsunpou2j() {
        return wtsunpou2j;
    }

    /**
     * WT寸法10
     * @param wtsunpou2j セットする wtsunpou2j
     */
    public void setWtsunpou2j(Integer wtsunpou2j) {
        this.wtsunpou2j = wtsunpou2j;
    }

    /**
     * WT寸法11
     * @return wtsunpou2k
     */
    public Integer getWtsunpou2k() {
        return wtsunpou2k;
    }

    /**
     * WT寸法11
     * @param wtsunpou2k セットする wtsunpou2k
     */
    public void setWtsunpou2k(Integer wtsunpou2k) {
        this.wtsunpou2k = wtsunpou2k;
    }

    /**
     * WT寸法12
     * @return wtsunpou2l
     */
    public Integer getWtsunpou2l() {
        return wtsunpou2l;
    }

    /**
     * WT寸法12
     * @param wtsunpou2l セットする wtsunpou2l
     */
    public void setWtsunpou2l(Integer wtsunpou2l) {
        this.wtsunpou2l = wtsunpou2l;
    }

    /**
     * WT寸法13
     * @return wtsunpou2m
     */
    public Integer getWtsunpou2m() {
        return wtsunpou2m;
    }

    /**
     * WT寸法13
     * @param wtsunpou2m セットする wtsunpou2m
     */
    public void setWtsunpou2m(Integer wtsunpou2m) {
        this.wtsunpou2m = wtsunpou2m;
    }

    /**
     * WT寸法14
     * @return wtsunpou2n
     */
    public Integer getWtsunpou2n() {
        return wtsunpou2n;
    }

    /**
     * WT寸法14
     * @param wtsunpou2n セットする wtsunpou2n
     */
    public void setWtsunpou2n(Integer wtsunpou2n) {
        this.wtsunpou2n = wtsunpou2n;
    }

    /**
     * WT寸法15
     * @return wtsunpou2o
     */
    public Integer getWtsunpou2o() {
        return wtsunpou2o;
    }

    /**
     * WT寸法15
     * @param wtsunpou2o セットする wtsunpou2o
     */
    public void setWtsunpou2o(Integer wtsunpou2o) {
        this.wtsunpou2o = wtsunpou2o;
    }

    /**
     * WT寸法16
     * @return wtsunpou2p
     */
    public Integer getWtsunpou2p() {
        return wtsunpou2p;
    }

    /**
     * WT寸法16
     * @param wtsunpou2p セットする wtsunpou2p
     */
    public void setWtsunpou2p(Integer wtsunpou2p) {
        this.wtsunpou2p = wtsunpou2p;
    }

    /**
     * WT寸法17
     * @return wtsunpou2q
     */
    public Integer getWtsunpou2q() {
        return wtsunpou2q;
    }

    /**
     * WT寸法17
     * @param wtsunpou2q セットする wtsunpou2q
     */
    public void setWtsunpou2q(Integer wtsunpou2q) {
        this.wtsunpou2q = wtsunpou2q;
    }

    /**
     * WT寸法18
     * @return wtsunpou2r
     */
    public Integer getWtsunpou2r() {
        return wtsunpou2r;
    }

    /**
     * WT寸法18
     * @param wtsunpou2r セットする wtsunpou2r
     */
    public void setWtsunpou2r(Integer wtsunpou2r) {
        this.wtsunpou2r = wtsunpou2r;
    }

    /**
     * WT寸法19
     * @return wtsunpou2s
     */
    public Integer getWtsunpou2s() {
        return wtsunpou2s;
    }

    /**
     * WT寸法19
     * @param wtsunpou2s セットする wtsunpou2s
     */
    public void setWtsunpou2s(Integer wtsunpou2s) {
        this.wtsunpou2s = wtsunpou2s;
    }

    /**
     * WT寸法20
     * @return wtsunpou2t
     */
    public Integer getWtsunpou2t() {
        return wtsunpou2t;
    }

    /**
     * WT寸法20
     * @param wtsunpou2t セットする wtsunpou2t
     */
    public void setWtsunpou2t(Integer wtsunpou2t) {
        this.wtsunpou2t = wtsunpou2t;
    }

    /**
     * WT寸法21
     * @return wtsunpou2u
     */
    public Integer getWtsunpou2u() {
        return wtsunpou2u;
    }

    /**
     * WT寸法21
     * @param wtsunpou2u セットする wtsunpou2u
     */
    public void setWtsunpou2u(Integer wtsunpou2u) {
        this.wtsunpou2u = wtsunpou2u;
    }

    /**
     * WT寸法22
     * @return wtsunpou2v
     */
    public Integer getWtsunpou2v() {
        return wtsunpou2v;
    }

    /**
     * WT寸法22
     * @param wtsunpou2v セットする wtsunpou2v
     */
    public void setWtsunpou2v(Integer wtsunpou2v) {
        this.wtsunpou2v = wtsunpou2v;
    }

    /**
     * WT寸法23
     * @return wtsunpou2w
     */
    public Integer getWtsunpou2w() {
        return wtsunpou2w;
    }

    /**
     * WT寸法23
     * @param wtsunpou2w セットする wtsunpou2w
     */
    public void setWtsunpou2w(Integer wtsunpou2w) {
        this.wtsunpou2w = wtsunpou2w;
    }

    /**
     * WT寸法24
     * @return wtsunpou2x
     */
    public Integer getWtsunpou2x() {
        return wtsunpou2x;
    }

    /**
     * WT寸法24
     * @param wtsunpou2x セットする wtsunpou2x
     */
    public void setWtsunpou2x(Integer wtsunpou2x) {
        this.wtsunpou2x = wtsunpou2x;
    }

    /**
     * WT寸法25
     * @return wtsunpou2y
     */
    public Integer getWtsunpou2y() {
        return wtsunpou2y;
    }

    /**
     * WT寸法25
     * @param wtsunpou2y セットする wtsunpou2y
     */
    public void setWtsunpou2y(Integer wtsunpou2y) {
        this.wtsunpou2y = wtsunpou2y;
    }

    /**
     * WT寸法26
     * @return wtsunpou2z
     */
    public Integer getWtsunpou2z() {
        return wtsunpou2z;
    }

    /**
     * WT寸法26
     * @param wtsunpou2z セットする wtsunpou2z
     */
    public void setWtsunpou2z(Integer wtsunpou2z) {
        this.wtsunpou2z = wtsunpou2z;
    }

    /**
     * WT寸法27
     * @return wtsunpou2aa
     */
    public Integer getWtsunpou2aa() {
        return wtsunpou2aa;
    }

    /**
     * WT寸法27
     * @param wtsunpou2aa セットする wtsunpou2aa
     */
    public void setWtsunpou2aa(Integer wtsunpou2aa) {
        this.wtsunpou2aa = wtsunpou2aa;
    }

    /**
     * WT寸法28
     * @return wtsunpou2ab
     */
    public Integer getWtsunpou2ab() {
        return wtsunpou2ab;
    }

    /**
     * WT寸法28
     * @param wtsunpou2ab セットする wtsunpou2ab
     */
    public void setWtsunpou2ab(Integer wtsunpou2ab) {
        this.wtsunpou2ab = wtsunpou2ab;
    }

    /**
     * WT寸法29
     * @return wtsunpou2ac
     */
    public Integer getWtsunpou2ac() {
        return wtsunpou2ac;
    }

    /**
     * WT寸法29
     * @param wtsunpou2ac セットする wtsunpou2ac
     */
    public void setWtsunpou2ac(Integer wtsunpou2ac) {
        this.wtsunpou2ac = wtsunpou2ac;
    }

    /**
     * WT寸法30
     * @return wtsunpou2ad
     */
    public Integer getWtsunpou2ad() {
        return wtsunpou2ad;
    }

    /**
     * WT寸法30
     * @param wtsunpou2ad セットする wtsunpou2ad
     */
    public void setWtsunpou2ad(Integer wtsunpou2ad) {
        this.wtsunpou2ad = wtsunpou2ad;
    }

    /**
     * WT寸法31
     * @return wtsunpou2ae
     */
    public Integer getWtsunpou2ae() {
        return wtsunpou2ae;
    }

    /**
     * WT寸法31
     * @param wtsunpou2ae セットする wtsunpou2ae
     */
    public void setWtsunpou2ae(Integer wtsunpou2ae) {
        this.wtsunpou2ae = wtsunpou2ae;
    }

    /**
     * WT寸法32
     * @return wtsunpou2af
     */
    public Integer getWtsunpou2af() {
        return wtsunpou2af;
    }

    /**
     * WT寸法32
     * @param wtsunpou2af セットする wtsunpou2af
     */
    public void setWtsunpou2af(Integer wtsunpou2af) {
        this.wtsunpou2af = wtsunpou2af;
    }

    /**
     * WT寸法33
     * @return wtsunpou2ag
     */
    public Integer getWtsunpou2ag() {
        return wtsunpou2ag;
    }

    /**
     * WT寸法33
     * @param wtsunpou2ag セットする wtsunpou2ag
     */
    public void setWtsunpou2ag(Integer wtsunpou2ag) {
        this.wtsunpou2ag = wtsunpou2ag;
    }

    /**
     * WT寸法34
     * @return wtsunpou2ah
     */
    public Integer getWtsunpou2ah() {
        return wtsunpou2ah;
    }

    /**
     * WT寸法34
     * @param wtsunpou2ah セットする wtsunpou2ah
     */
    public void setWtsunpou2ah(Integer wtsunpou2ah) {
        this.wtsunpou2ah = wtsunpou2ah;
    }

    /**
     * WT寸法35
     * @return wtsunpou2ai
     */
    public Integer getWtsunpou2ai() {
        return wtsunpou2ai;
    }

    /**
     * WT寸法35
     * @param wtsunpou2ai セットする wtsunpou2ai
     */
    public void setWtsunpou2ai(Integer wtsunpou2ai) {
        this.wtsunpou2ai = wtsunpou2ai;
    }

    /**
     * WT寸法36
     * @return wtsunpou2aj
     */
    public Integer getWtsunpou2aj() {
        return wtsunpou2aj;
    }

    /**
     * WT寸法36
     * @param wtsunpou2aj セットする wtsunpou2aj
     */
    public void setWtsunpou2aj(Integer wtsunpou2aj) {
        this.wtsunpou2aj = wtsunpou2aj;
    }

    /**
     * WT寸法37
     * @return wtsunpou2ak
     */
    public Integer getWtsunpou2ak() {
        return wtsunpou2ak;
    }

    /**
     * WT寸法37
     * @param wtsunpou2ak セットする wtsunpou2ak
     */
    public void setWtsunpou2ak(Integer wtsunpou2ak) {
        this.wtsunpou2ak = wtsunpou2ak;
    }

    /**
     * WT寸法38
     * @return wtsunpou2al
     */
    public Integer getWtsunpou2al() {
        return wtsunpou2al;
    }

    /**
     * WT寸法38
     * @param wtsunpou2al セットする wtsunpou2al
     */
    public void setWtsunpou2al(Integer wtsunpou2al) {
        this.wtsunpou2al = wtsunpou2al;
    }

    /**
     * WT寸法39
     * @return wtsunpou2am
     */
    public Integer getWtsunpou2am() {
        return wtsunpou2am;
    }

    /**
     * WT寸法39
     * @param wtsunpou2am セットする wtsunpou2am
     */
    public void setWtsunpou2am(Integer wtsunpou2am) {
        this.wtsunpou2am = wtsunpou2am;
    }

    /**
     * WT寸法40
     * @return wtsunpou2an
     */
    public Integer getWtsunpou2an() {
        return wtsunpou2an;
    }

    /**
     * WT寸法40
     * @param wtsunpou2an セットする wtsunpou2an
     */
    public void setWtsunpou2an(Integer wtsunpou2an) {
        this.wtsunpou2an = wtsunpou2an;
    }

    /**
     * WT寸法AVE
     * @return wtsunpouave2
     */
    public BigDecimal getWtsunpouave2() {
        return wtsunpouave2;
    }

    /**
     * WT寸法AVE
     * @param wtsunpouave2 セットする wtsunpouave2
     */
    public void setWtsunpouave2(BigDecimal wtsunpouave2) {
        this.wtsunpouave2 = wtsunpouave2;
    }

    /**
     * WT寸法RANGE
     * @return wtsunpourange2
     */
    public Integer getWtsunpourange2() {
        return wtsunpourange2;
    }

    /**
     * WT寸法RANGE
     * @param wtsunpourange2 セットする wtsunpourange2
     */
    public void setWtsunpourange2(Integer wtsunpourange2) {
        this.wtsunpourange2 = wtsunpourange2;
    }

    /**
     * WT寸法MIN
     * @return wtsunpoumin2
     */
    public Integer getWtsunpoumin2() {
        return wtsunpoumin2;
    }

    /**
     * WT寸法MIN
     * @param wtsunpoumin2 セットする wtsunpoumin2
     */
    public void setWtsunpoumin2(Integer wtsunpoumin2) {
        this.wtsunpoumin2 = wtsunpoumin2;
    }

    /**
     * WT寸法MAX
     * @return wtsunpoumax2
     */
    public Integer getWtsunpoumax2() {
        return wtsunpoumax2;
    }

    /**
     * WT寸法MAX
     * @param wtsunpoumax2 セットする wtsunpoumax2
     */
    public void setWtsunpoumax2(Integer wtsunpoumax2) {
        this.wtsunpoumax2 = wtsunpoumax2;
    }

    /**
     * P寸法MIN
     * @return psunpoumin2
     */
    public BigDecimal getPsunpoumin2() {
        return psunpoumin2;
    }

    /**
     * P寸法MIN
     * @param psunpoumin2 セットする psunpoumin2
     */
    public void setPsunpoumin2(BigDecimal psunpoumin2) {
        this.psunpoumin2 = psunpoumin2;
    }

    /**
     * P寸法MAX
     * @return psunpoumax2
     */
    public BigDecimal getPsunpoumax2() {
        return psunpoumax2;
    }

    /**
     * P寸法MAX
     * @param psunpoumax2 セットする psunpoumax2
     */
    public void setPsunpoumax2(BigDecimal psunpoumax2) {
        this.psunpoumax2 = psunpoumax2;
    }

    /**
     * 設備種類
     * @return setubisyurui
     */
    public String getSetubisyurui() {
        return setubisyurui;
    }

    /**
     * 設備種類
     * @param setubisyurui セットする setubisyurui
     */
    public void setSetubisyurui(String setubisyurui) {
        this.setubisyurui = setubisyurui;
    }

    /**
     * 塗布回数
     * @return tofukaisuu
     */
    public Integer getTofukaisuu() {
        return tofukaisuu;
    }

    /**
     * 塗布回数
     * @param tofukaisuu セットする tofukaisuu
     */
    public void setTofukaisuu(Integer tofukaisuu) {
        this.tofukaisuu = tofukaisuu;
    }

    /**
     * 保持ｼﾞｸﾞ
     * @return hojijigu
     */
    public String getHojijigu() {
        return hojijigu;
    }

    /**
     * 保持ｼﾞｸﾞ
     * @param hojijigu セットする hojijigu
     */
    public void setHojijigu(String hojijigu) {
        this.hojijigu = hojijigu;
    }

    /**
     * 粘着ｼｰﾄﾛｯﾄ  1次側
     * @return nentyakusheetlot1
     */
    public String getNentyakusheetlot1() {
        return nentyakusheetlot1;
    }

    /**
     * 粘着ｼｰﾄﾛｯﾄ  1次側
     * @param nentyakusheetlot1 セットする nentyakusheetlot1
     */
    public void setNentyakusheetlot1(String nentyakusheetlot1) {
        this.nentyakusheetlot1 = nentyakusheetlot1;
    }

    /**
     * 粘着ｼｰﾄﾛｯﾄ  2次側
     * @return nentyakusheetlot2
     */
    public String getNentyakusheetlot2() {
        return nentyakusheetlot2;
    }

    /**
     * 粘着ｼｰﾄﾛｯﾄ  2次側
     * @param nentyakusheetlot2 セットする nentyakusheetlot2
     */
    public void setNentyakusheetlot2(String nentyakusheetlot2) {
        this.nentyakusheetlot2 = nentyakusheetlot2;
    }

    /**
     * 塗布ｼﾞｸﾞ取り個数
     * @return tofujigutorikosuu
     */
    public Integer getTofujigutorikosuu() {
        return tofujigutorikosuu;
    }

    /**
     * 塗布ｼﾞｸﾞ取り個数
     * @param tofujigutorikosuu セットする tofujigutorikosuu
     */
    public void setTofujigutorikosuu(Integer tofujigutorikosuu) {
        this.tofujigutorikosuu = tofujigutorikosuu;
    }

    /**
     * 開始担当者
     * @return starttantosyacode
     */
    public String getStarttantosyacode() {
        return starttantosyacode;
    }

    /**
     * 開始担当者
     * @param starttantosyacode セットする starttantosyacode
     */
    public void setStarttantosyacode(String starttantosyacode) {
        this.starttantosyacode = starttantosyacode;
    }

    /**
     * 開始確認者
     * @return startkakuninsyacode
     */
    public String getStartkakuninsyacode() {
        return startkakuninsyacode;
    }

    /**
     * 開始確認者
     * @param startkakuninsyacode セットする startkakuninsyacode
     */
    public void setStartkakuninsyacode(String startkakuninsyacode) {
        this.startkakuninsyacode = startkakuninsyacode;
    }

    /**
     * 重量
     * @return juryou
     */
    public BigDecimal getJuryou() {
        return juryou;
    }

    /**
     * 重量
     * @param juryou セットする juryou
     */
    public void setJuryou(BigDecimal juryou) {
        this.juryou = juryou;
    }

    /**
     * 処理個数
     * @return syorikosuu
     */
    public Integer getSyorikosuu() {
        return syorikosuu;
    }

    /**
     * 処理個数
     * @param syorikosuu セットする syorikosuu
     */
    public void setSyorikosuu(Integer syorikosuu) {
        this.syorikosuu = syorikosuu;
    }

    /**
     * 終了担当者
     * @return endtantosyacode
     */
    public String getEndtantosyacode() {
        return endtantosyacode;
    }

    /**
     * 終了担当者
     * @param endtantosyacode セットする endtantosyacode
     */
    public void setEndtantosyacode(String endtantosyacode) {
        this.endtantosyacode = endtantosyacode;
    }

    /**
     * 1次ﾍﾟｰｽﾄ厚み設定値
     * @return pasteatsumi1ji
     */
    public BigDecimal getPasteatsumi1ji() {
        return pasteatsumi1ji;
    }

    /**
     * 1次ﾍﾟｰｽﾄ厚み設定値
     * @param pasteatsumi1ji セットする pasteatsumi1ji
     */
    public void setPasteatsumi1ji(BigDecimal pasteatsumi1ji) {
        this.pasteatsumi1ji = pasteatsumi1ji;
    }

    /**
     * 2次ﾍﾟｰｽﾄ厚み設定値
     * @return pasteatsumi2ji
     */
    public BigDecimal getPasteatsumi2ji() {
        return pasteatsumi2ji;
    }

    /**
     * 2次ﾍﾟｰｽﾄ厚み設定値
     * @param pasteatsumi2ji セットする pasteatsumi2ji
     */
    public void setPasteatsumi2ji(BigDecimal pasteatsumi2ji) {
        this.pasteatsumi2ji = pasteatsumi2ji;
    }

    /**
     * ｲﾝｸ厚みA
     * @return atsumiinkua
     */
    public Integer getAtsumiinkua() {
        return atsumiinkua;
    }

    /**
     * ｲﾝｸ厚みA
     * @param atsumiinkua セットする atsumiinkua
     */
    public void setAtsumiinkua(Integer atsumiinkua) {
        this.atsumiinkua = atsumiinkua;
    }

    /**
     * ｲﾝｸ厚みB
     * @return atsumiinkub
     */
    public Integer getAtsumiinkub() {
        return atsumiinkub;
    }

    /**
     * ｲﾝｸ厚みB
     * @param atsumiinkub セットする atsumiinkub
     */
    public void setAtsumiinkub(Integer atsumiinkub) {
        this.atsumiinkub = atsumiinkub;
    }

    /**
     * 回数
     * @return kaisuu
     */
    public Integer getKaisuu() {
        return kaisuu;
    }

    /**
     * 回数
     * @param kaisuu セットする kaisuu
     */
    public void setKaisuu(Integer kaisuu) {
        this.kaisuu = kaisuu;
    }
    
    

}