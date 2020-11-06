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
 * 変更日	2019/09/10<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * 変更日	2020/04/10<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	項目追加<br>
 * <br>
 * 変更日	2020/10/14<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	863 sujialiang<br>
 * 変更理由	項目追加・変更<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * FXHDD07(電気特性設備)のモデルクラスです。
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2020/01/10
 */
public class FXHDD07 {



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
     * 号機
     */
    private String gouki;

    /**
     * 分類ｴｱｰ圧
     */
    private BigDecimal bunruiairatu;

    /**
     * CDｺﾝﾀｸﾄ圧
     */
    private Integer cdcontactatu;

    /**
     * IRｺﾝﾀｸﾄ圧
     */
    private Integer ircontactatu;

    /**
     * Tanδ
     */
    private BigDecimal tan;

    /**
     * 測定周波数
     */
    private String sokuteisyuhasuu;

    /**
     * 測定電圧
     */
    private BigDecimal sokuteidenatu;

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 ＰＣ① 電圧
     */
    private BigDecimal pcdenatu1;

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 ＰＣ① 充電時間
     */
    private Integer pcjudenjikan1;

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 ＰＣ② 電圧
     */
    private BigDecimal pcdenatu2;

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 ＰＣ② 充電時間
     */
    private Integer pcjudenjikan2;

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 ＰＣ③ 電圧
     */
    private BigDecimal pcdenatu3;

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 ＰＣ③ 充電時間
     */
    private Integer pcjudenjikan3;

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 ＰＣ④ 電圧
     */
    private BigDecimal pcdenatu4;

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 ＰＣ④ 充電時間
     */
    private Integer pcjudenjikan4;

    /**
     * 耐電圧設定条件 ＩＲ① 電圧
     */
    private BigDecimal irdenatu1;

    /**
     * 耐電圧設定条件 ＩＲ① 判定値(低)
     */
    private BigDecimal irhanteiti1Low;

    /**
     * 耐電圧設定条件 ＩＲ① 判定値
     */
    private BigDecimal irhanteiti1;

    /**
     * 耐電圧設定条件 ＩＲ① 判定値 単位
     */
    private String irhantei1tani;

    /**
     * 耐電圧設定条件 ＩＲ① 充電時間
     */
    private Integer irjudenjikan1;

    /**
     * 耐電圧設定条件 ＩＲ② 電圧
     */
    private BigDecimal irdenatu2;

    /**
     * 耐電圧設定条件 ＩＲ② 判定値(低)
     */
    private BigDecimal irhanteiti2Low;

    /**
     * 耐電圧設定条件 ＩＲ② 判定値
     */
    private BigDecimal irhanteiti2;

    /**
     * 耐電圧設定条件 ＩＲ② 判定値 単位
     */
    private String irhantei2tani;

    /**
     * 耐電圧設定条件 ＩＲ② 充電時間
     */
    private Integer irjudenjikan2;

    /**
     * 耐電圧設定条件 ＩＲ③ 電圧
     */
    private BigDecimal irdenatu3;

    /**
     * 耐電圧設定条件 ＩＲ③ 判定値(低)
     */
    private BigDecimal irhanteiti3Low;

    /**
     * 耐電圧設定条件 ＩＲ③ 判定値
     */
    private BigDecimal irhanteiti3;

    /**
     * 耐電圧設定条件 ＩＲ③ 判定値 単位
     */
    private String irhantei3tani;

    /**
     * 耐電圧設定条件 ＩＲ③ 充電時間
     */
    private Integer irjudenjikan3;

    /**
     * 耐電圧設定条件 ＩＲ④ 電圧
     */
    private BigDecimal irdenatu4;

    /**
     * 耐電圧設定条件 ＩＲ④ 判定値(低)
     */
    private BigDecimal irhanteiti4Low;

    /**
     * 耐電圧設定条件 ＩＲ④ 判定値
     */
    private BigDecimal irhanteiti4;

    /**
     * 耐電圧設定条件 ＩＲ④ 判定値 単位
     */
    private String irhantei4tani;

    /**
     * 耐電圧設定条件 ＩＲ④ 充電時間
     */
    private Integer irjudenjikan4;

    /**
     * 耐電圧設定条件 ＩＲ⑤ 電圧
     */
    private BigDecimal irdenatu5;

    /**
     * 耐電圧設定条件 ＩＲ⑤ 判定値(低)
     */
    private BigDecimal irhanteiti5Low;

    /**
     * 耐電圧設定条件 ＩＲ⑤ 判定値
     */
    private BigDecimal irhanteiti5;

    /**
     * 耐電圧設定条件 ＩＲ⑤ 判定値 単位
     */
    private String irhantei5tani;

    /**
     * 耐電圧設定条件 ＩＲ⑤ 充電時間
     */
    private Integer irjudenjikan5;

    /**
     * 耐電圧設定条件 ＩＲ⑥ 電圧
     */
    private BigDecimal irdenatu6;

    /**
     * 耐電圧設定条件 ＩＲ⑥ 判定値(低)
     */
    private BigDecimal irhanteiti6Low;

    /**
     * 耐電圧設定条件 ＩＲ⑥ 判定値
     */
    private BigDecimal irhanteiti6;

    /**
     * 耐電圧設定条件 ＩＲ⑥ 判定値 単位
     */
    private String irhantei6tani;

    /**
     * 耐電圧設定条件 ＩＲ⑥ 充電時間
     */
    private Integer irjudenjikan6;

    /**
     * 耐電圧設定条件 ＩＲ⑦ 電圧
     */
    private BigDecimal irdenatu7;

    /**
     * 耐電圧設定条件 ＩＲ⑦ 判定値(低)
     */
    private BigDecimal irhanteiti7Low;

    /**
     * 耐電圧設定条件 ＩＲ⑦ 判定値
     */
    private BigDecimal irhanteiti7;

    /**
     * 耐電圧設定条件 ＩＲ⑦ 判定値 単位
     */
    private String irhantei7tani;

    /**
     * 耐電圧設定条件 ＩＲ⑦ 充電時間
     */
    private Integer irjudenjikan7;

    /**
     * 耐電圧設定条件 ＩＲ⑧ 電圧
     */
    private BigDecimal irdenatu8;

    /**
     * 耐電圧設定条件 ＩＲ⑧ 判定値(低)
     */
    private BigDecimal irhanteiti8Low;

    /**
     * 耐電圧設定条件 ＩＲ⑧ 判定値
     */
    private BigDecimal irhanteiti8;

    /**
     * 耐電圧設定条件 ＩＲ⑧ 判定値 単位
     */
    private String irhantei8tani;

    /**
     * 耐電圧設定条件 ＩＲ⑧ 充電時間
     */
    private Integer irjudenjikan8;
    
    /**
     * RDC1 ﾚﾝｼﾞ
     */
    private BigDecimal rdcrange1;

    /**
     * RDC1 判定値
     */
    private BigDecimal rdchantei1;
    
    /**
     * RDC2 ﾚﾝｼﾞ
     */
    private BigDecimal rdcrange2;
    
    /**
     * RDC2 判定値
     */
    private BigDecimal rdchantei2;
    /**
     * BIN1 ｶｳﾝﾀｰ数
     */
    private Integer bin1countersuu;

    /**
     * BIN2 ｶｳﾝﾀｰ数
     */
    private Integer bin2countersuu;

    /**
     * BIN3 ｶｳﾝﾀｰ数
     */
    private Integer bin3countersuu;

    /**
     * BIN4 ｶｳﾝﾀｰ数
     */
    private Integer bin4countersuu;

    /**
     * BIN5 ｶｳﾝﾀｰ数
     */
    private Integer bin5countersuu;

    /**
     * BIN6 ｶｳﾝﾀｰ数
     */
    private Integer bin6countersuu;

    /**
     * BIN7 ｶｳﾝﾀｰ数
     */
    private Integer bin7countersuu;

    /**
     * BIN8 ｶｳﾝﾀｰ数
     */
    private Integer bin8countersuu;

    /**
     * BIN5 %区分(設定値)
     */
    private String bin5setteiti;

    /**
     * BIN6 %区分(設定値)
     */
    private String bin6setteiti;

    /**
     * BIN7 %区分(設定値)
     */
    private String bin7setteiti;

    /**
     * BIN8 %区分(設定値)
     */
    private String bin8setteiti;

    /**
     * 登録日時
     */
    private Timestamp torokuDate;

    /**
     * 更新日時
     */
    private Timestamp koshinDate;

    /**
     * 削除ﾌﾗｸﾞ
     */
    private Integer deleteflag;
    
    /**
     * DROP1,3 PC
     */
    private BigDecimal drop13pc;
    
    /**
     * DROP1,3 PS
     */
    private BigDecimal drop13ps;
    
    /**
     * DROP1,3 MS･DC
     */
    private BigDecimal drop13msdc;
    
    /**
     * DROP2,4 PC
     */
    private BigDecimal drop24pc;
    
    /**
     * DROP2,4 PS
     */
    private BigDecimal drop24ps;
    
    /**
     * DROP2,4 MS･DC
     */
    private BigDecimal drop24msdc;

    /**
     * BIN1 選別区分
     */
    private String bin1senbetsukbn;
    
    /**
     * BIN2 選別区分
     */
    private String bin2senbetsukbn;
    
    /**
     * BIN3 選別区分
     */
    private String bin3senbetsukbn;
    
    /**
     * BIN4 選別区分
     */
    private String bin4senbetsukbn;
    
    /**
     * BIN5 選別区分
     */
    private String bin5senbetsukbn;
    
    /**
     * BIN6 選別区分
     */
    private String bin6senbetsukbn;
    
    /**
     * BIN7 選別区分
     */
    private String bin7senbetsukbn;
    
    /**
     * BIN8 選別区分
     */
    private String bin8senbetsukbn;
    
    /**
     * ﾃｽﾄﾌﾟﾚｰﾄ管理No
     */
    private String testplatekanrino;

    /**
     * 半田サンプル
     */
    private String handasample;

    /**
     * 信頼性サンプル
     */
    private String sinraiseisample;

    /**
     * 検査場所
     */
    private String kensabasyo;

    /**
     * 選別順序変更
     */
    private String senbetujunjo;

    /**
     * 設定ﾓｰﾄﾞ確認
     */
    private String setteikakunin;

    /**
     * 配線確認
     */
    private String haisenkakunin;

    /**
     * 固定電極 外観・段差
     */
    private String koteidenkyoku;

    /**
     * ﾃｽﾄﾌﾟﾚｰﾄ　形状・清掃
     */
    private String testplatekeijo;

    /**
     * 分類吹き出し穴
     */
    private String bunruifukidasi;

    /**
     * ﾃｽﾄﾌﾟﾚｰﾄ位置確認(穴位置)
     */
    private String testplatekakunin;

    /**
     * 製品投入状態
     */
    private String seihintounyuujotai;

    /**
     * 分類確認
     */
    private String bunruikakunin;

    /**
     * 外観確認
     */
    private String gaikankakunin;

    /**
     * 選別開始日時
     */
    private Timestamp senbetukaisinitiji;

    /**
     * 選別終了日時
     */
    private Timestamp senbetusyuryounitiji;

    /**
     * 設定値BIN1low
     */
    private String setteiti1low;

    /**
     * 設定値BIN1up
     */
    private String setteiti1up;

    /**
     * 設定値BIN2low
     */
    private String setteiti2low;

    /**
     * 設定値BIN2up
     */
    private String setteiti2up;

    /**
     * 設定値BIN3low
     */
    private String setteiti3low;

    /**
     * 設定値BIN3up
     */
    private String setteiti3up;

    /**
     * TTNG1
     */
    private Integer ttng1;

    /**
     * TTNG2
     */
    private Integer ttng2;

    /**
     * MC
     */
    private Integer mc;

    /**
     * RI
     */
    private Integer ri;

    /**
     * DNG
     */
    private Integer dng;

    /**
     * RNG
     */
    private Integer rng;

    /**
     * DropNG
     */
    private Integer dropng;

    /**
     * DropNG1
     */
    private Integer dropng1;

    /**
     * DropNG2
     */
    private Integer dropng2;

    /**
     * ﾛｯﾄ終了区分
     */
    private String lotkbn;

    /**
     * 設定値cap1
     */
    private Integer setteicap1;

    /**
     * 設定値cap2
     */
    private Integer setteicap2;

    /**
     * 設定値cap3
     */
    private Integer setteicap3;
    
    /**
     * 工場ｺｰﾄﾞ
     * @return the kojyo
     */
    public String getKojyo() {
        return kojyo;
    }

    /**
     * 工場ｺｰﾄﾞ
     * @param kojyo the kojyo to set
     */
    public void setKojyo(String kojyo) {
        this.kojyo = kojyo;
    }

    /**
     * ﾛｯﾄNo
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * ﾛｯﾄNo
     * @param lotno the lotno to set
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * 枝番
     * @return the edaban
     */
    public String getEdaban() {
        return edaban;
    }

    /**
     * 枝番
     * @param edaban the edaban to set
     */
    public void setEdaban(String edaban) {
        this.edaban = edaban;
    }

    /**
     * 号機
     * @return the gouki
     */
    public String getGouki() {
        return gouki;
    }

    /**
     * 号機
     * @param gouki the gouki to set
     */
    public void setGouki(String gouki) {
        this.gouki = gouki;
    }

    /**
     * 分類ｴｱｰ圧
     * @return the bunruiairatu
     */
    public BigDecimal getBunruiairatu() {
        return bunruiairatu;
    }

    /**
     * 分類ｴｱｰ圧
     * @param bunruiairatu the bunruiairatu to set
     */
    public void setBunruiairatu(BigDecimal bunruiairatu) {
        this.bunruiairatu = bunruiairatu;
    }

    /**
     * CDｺﾝﾀｸﾄ圧
     * @return the cdcontactatu
     */
    public Integer getCdcontactatu() {
        return cdcontactatu;
    }

    /**
     * CDｺﾝﾀｸﾄ圧
     * @param cdcontactatu the cdcontactatu to set
     */
    public void setCdcontactatu(Integer cdcontactatu) {
        this.cdcontactatu = cdcontactatu;
    }

    /**
     * IRｺﾝﾀｸﾄ圧
     * @return the ircontactatu
     */
    public Integer getIrcontactatu() {
        return ircontactatu;
    }

    /**
     * IRｺﾝﾀｸﾄ圧
     * @param ircontactatu the ircontactatu to set
     */
    public void setIrcontactatu(Integer ircontactatu) {
        this.ircontactatu = ircontactatu;
    }

    /**
     * Tanδ
     * @return the tan
     */
    public BigDecimal getTan() {
        return tan;
    }

    /**
     * Tanδ
     * @param tan the tan to set
     */
    public void setTan(BigDecimal tan) {
        this.tan = tan;
    }

    /**
     * 測定周波数
     * @return the sokuteisyuhasuu
     */
    public String getSokuteisyuhasuu() {
        return sokuteisyuhasuu;
    }

    /**
     * 測定周波数
     * @param sokuteisyuhasuu the sokuteisyuhasuu to set
     */
    public void setSokuteisyuhasuu(String sokuteisyuhasuu) {
        this.sokuteisyuhasuu = sokuteisyuhasuu;
    }

    /**
     * 測定電圧
     * @return the sokuteidenatu
     */
    public BigDecimal getSokuteidenatu() {
        return sokuteidenatu;
    }

    /**
     * 測定電圧
     * @param sokuteidenatu the sokuteidenatu to set
     */
    public void setSokuteidenatu(BigDecimal sokuteidenatu) {
        this.sokuteidenatu = sokuteidenatu;
    }

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 ＰＣ① 電圧
     * @return the pcdenatu1
     */
    public BigDecimal getPcdenatu1() {
        return pcdenatu1;
    }

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 ＰＣ① 電圧
     * @param pcdenatu1 the pcdenatu1 to set
     */
    public void setPcdenatu1(BigDecimal pcdenatu1) {
        this.pcdenatu1 = pcdenatu1;
    }

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 ＰＣ① 充電時間
     * @return the pcjudenjikan1
     */
    public Integer getPcjudenjikan1() {
        return pcjudenjikan1;
    }

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 ＰＣ① 充電時間
     * @param pcjudenjikan1 the pcjudenjikan1 to set
     */
    public void setPcjudenjikan1(Integer pcjudenjikan1) {
        this.pcjudenjikan1 = pcjudenjikan1;
    }

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 ＰＣ② 電圧
     * @return the pcdenatu2
     */
    public BigDecimal getPcdenatu2() {
        return pcdenatu2;
    }

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 ＰＣ② 電圧
     * @param pcdenatu2 the pcdenatu2 to set
     */
    public void setPcdenatu2(BigDecimal pcdenatu2) {
        this.pcdenatu2 = pcdenatu2;
    }

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 ＰＣ② 充電時間
     * @return the pcjudenjikan2
     */
    public Integer getPcjudenjikan2() {
        return pcjudenjikan2;
    }

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 ＰＣ② 充電時間
     * @param pcjudenjikan2 the pcjudenjikan2 to set
     */
    public void setPcjudenjikan2(Integer pcjudenjikan2) {
        this.pcjudenjikan2 = pcjudenjikan2;
    }

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 ＰＣ③ 電圧
     * @return the pcdenatu3
     */
    public BigDecimal getPcdenatu3() {
        return pcdenatu3;
    }

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 ＰＣ③ 電圧
     * @param pcdenatu3 the pcdenatu3 to set
     */
    public void setPcdenatu3(BigDecimal pcdenatu3) {
        this.pcdenatu3 = pcdenatu3;
    }

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 ＰＣ③ 充電時間
     * @return the pcjudenjikan3
     */
    public Integer getPcjudenjikan3() {
        return pcjudenjikan3;
    }

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 ＰＣ③ 充電時間
     * @param pcjudenjikan3 the pcjudenjikan3 to set
     */
    public void setPcjudenjikan3(Integer pcjudenjikan3) {
        this.pcjudenjikan3 = pcjudenjikan3;
    }

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 ＰＣ④ 電圧
     * @return the pcdenatu4
     */
    public BigDecimal getPcdenatu4() {
        return pcdenatu4;
    }

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 ＰＣ④ 電圧
     * @param pcdenatu4 the pcdenatu4 to set
     */
    public void setPcdenatu4(BigDecimal pcdenatu4) {
        this.pcdenatu4 = pcdenatu4;
    }

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 ＰＣ④ 充電時間
     * @return the pcjudenjikan4
     */
    public Integer getPcjudenjikan4() {
        return pcjudenjikan4;
    }

    /**
     * ﾌﾟﾘﾁｬｰｼﾞ条件 ＰＣ④ 充電時間
     * @param pcjudenjikan4 the pcjudenjikan4 to set
     */
    public void setPcjudenjikan4(Integer pcjudenjikan4) {
        this.pcjudenjikan4 = pcjudenjikan4;
    }

    /**
     * 耐電圧設定条件 ＩＲ① 電圧
     * @return the irdenatu1
     */
    public BigDecimal getIrdenatu1() {
        return irdenatu1;
    }

    /**
     * 耐電圧設定条件 ＩＲ① 電圧
     * @param irdenatu1 the irdenatu1 to set
     */
    public void setIrdenatu1(BigDecimal irdenatu1) {
        this.irdenatu1 = irdenatu1;
    }

    /**
     * 耐電圧設定条件 ＩＲ① 判定値(低)
     * @return the irhanteiti1Low
     */
    public BigDecimal getIrhanteiti1Low() {
        return irhanteiti1Low;
    }

    /**
     * 耐電圧設定条件 ＩＲ① 判定値(低)
     * @param irhanteiti1Low the irhanteiti1Low to set
     */
    public void setIrhanteiti1Low(BigDecimal irhanteiti1Low) {
        this.irhanteiti1Low = irhanteiti1Low;
    }

    /**
     * 耐電圧設定条件 ＩＲ① 判定値
     * @return the irhanteiti1
     */
    public BigDecimal getIrhanteiti1() {
        return irhanteiti1;
    }

    /**
     * 耐電圧設定条件 ＩＲ① 判定値
     * @param irhanteiti1 the irhanteiti1 to set
     */
    public void setIrhanteiti1(BigDecimal irhanteiti1) {
        this.irhanteiti1 = irhanteiti1;
    }

    /**
     * 耐電圧設定条件 ＩＲ① 判定値 単位
     * @return irhantei1tani
     */
    public String getIrhantei1tani() {
        return irhantei1tani;
    }

    /**
     * 耐電圧設定条件 ＩＲ① 判定値 単位
     * @param irhantei1tani 
     */
    public void setIrhantei1tani(String irhantei1tani) {
        this.irhantei1tani = irhantei1tani;
    }

    /**
     * 耐電圧設定条件 ＩＲ① 充電時間
     * @return the irjudenjikan1
     */
    public Integer getIrjudenjikan1() {
        return irjudenjikan1;
    }

    /**
     * 耐電圧設定条件 ＩＲ① 充電時間
     * @param irjudenjikan1 the irjudenjikan1 to set
     */
    public void setIrjudenjikan1(Integer irjudenjikan1) {
        this.irjudenjikan1 = irjudenjikan1;
    }

    /**
     * 耐電圧設定条件 ＩＲ② 電圧
     * @return the irdenatu2
     */
    public BigDecimal getIrdenatu2() {
        return irdenatu2;
    }

    /**
     * 耐電圧設定条件 ＩＲ② 電圧
     * @param irdenatu2 the irdenatu2 to set
     */
    public void setIrdenatu2(BigDecimal irdenatu2) {
        this.irdenatu2 = irdenatu2;
    }

    /**
     * 耐電圧設定条件 ＩＲ② 判定値(低)
     * @return the irhanteiti2Low
     */
    public BigDecimal getIrhanteiti2Low() {
        return irhanteiti2Low;
    }

    /**
     * 耐電圧設定条件 ＩＲ② 判定値(低)
     * @param irhanteiti2Low the irhanteiti2Low to set
     */
    public void setIrhanteiti2Low(BigDecimal irhanteiti2Low) {
        this.irhanteiti2Low = irhanteiti2Low;
    }

    /**
     * 耐電圧設定条件 ＩＲ② 判定値
     * @return the irhanteiti2
     */
    public BigDecimal getIrhanteiti2() {
        return irhanteiti2;
    }

    /**
     * 耐電圧設定条件 ＩＲ② 判定値
     * @param irhanteiti2 the irhanteiti2 to set
     */
    public void setIrhanteiti2(BigDecimal irhanteiti2) {
        this.irhanteiti2 = irhanteiti2;
    }

    /**
     * 耐電圧設定条件 ＩＲ② 判定値 単位
     * @return irhantei2tani
     */
    public String getIrhantei2tani() {
        return irhantei2tani;
    }

    /**
     * 耐電圧設定条件 ＩＲ② 判定値 単位
     * @param irhantei2tani
     */
    public void setIrhantei2tani(String irhantei2tani) {
        this.irhantei2tani = irhantei2tani;
    }

    /**
     * 耐電圧設定条件 ＩＲ② 充電時間
     * @return the irjudenjikan2
     */
    public Integer getIrjudenjikan2() {
        return irjudenjikan2;
    }

    /**
     * 耐電圧設定条件 ＩＲ② 充電時間
     * @param irjudenjikan2 the irjudenjikan2 to set
     */
    public void setIrjudenjikan2(Integer irjudenjikan2) {
        this.irjudenjikan2 = irjudenjikan2;
    }

    /**
     * 耐電圧設定条件 ＩＲ③ 電圧
     * @return the irdenatu3
     */
    public BigDecimal getIrdenatu3() {
        return irdenatu3;
    }

    /**
     * 耐電圧設定条件 ＩＲ③ 電圧
     * @param irdenatu3 the irdenatu3 to set
     */
    public void setIrdenatu3(BigDecimal irdenatu3) {
        this.irdenatu3 = irdenatu3;
    }

    /**
     * 耐電圧設定条件 ＩＲ③ 判定値
     * @return the irhanteiti3
     */
    public BigDecimal getIrhanteiti3() {
        return irhanteiti3;
    }

    /**
     * 耐電圧設定条件 ＩＲ③ 判定値(低)
     * @return the irhanteiti3Low
     */
    public BigDecimal getIrhanteiti3Low() {
        return irhanteiti3Low;
    }

    /**
     * 耐電圧設定条件 ＩＲ③ 判定値(低)
     * @param irhanteiti3Low the irhanteiti3Low to set
     */
    public void setIrhanteiti3Low(BigDecimal irhanteiti3Low) {
        this.irhanteiti3Low = irhanteiti3Low;
    }

    /**
     * 耐電圧設定条件 ＩＲ③ 判定値
     * @param irhanteiti3 the irhanteiti3 to set
     */
    public void setIrhanteiti3(BigDecimal irhanteiti3) {
        this.irhanteiti3 = irhanteiti3;
    }

    /**
     * 耐電圧設定条件 ＩＲ③ 判定値 単位
     * @return irhantei3tani
     */
    public String getIrhantei3tani() {
        return irhantei3tani;
    }

    /**
     * 耐電圧設定条件 ＩＲ③ 判定値 単位
     * @param irhantei3tani
     */
    public void setIrhantei3tani(String irhantei3tani) {
        this.irhantei3tani = irhantei3tani;
    }

    /**
     * 耐電圧設定条件 ＩＲ③ 充電時間
     * @return the irjudenjikan3
     */
    public Integer getIrjudenjikan3() {
        return irjudenjikan3;
    }

    /**
     * 耐電圧設定条件 ＩＲ③ 充電時間
     * @param irjudenjikan3 the irjudenjikan3 to set
     */
    public void setIrjudenjikan3(Integer irjudenjikan3) {
        this.irjudenjikan3 = irjudenjikan3;
    }

    /**
     * 耐電圧設定条件 ＩＲ④ 電圧
     * @return the irdenatu4
     */
    public BigDecimal getIrdenatu4() {
        return irdenatu4;
    }

    /**
     * 耐電圧設定条件 ＩＲ④ 電圧
     * @param irdenatu4 the irdenatu4 to set
     */
    public void setIrdenatu4(BigDecimal irdenatu4) {
        this.irdenatu4 = irdenatu4;
    }

    /**
     * 耐電圧設定条件 ＩＲ④ 判定値(低)
     * @return the irhanteiti4Low
     */
    public BigDecimal getIrhanteiti4Low() {
        return irhanteiti4Low;
    }

    /**
     * 耐電圧設定条件 ＩＲ④ 判定値(低)
     * @param irhanteiti4Low the irhanteiti4Low to set
     */
    public void setIrhanteiti4Low(BigDecimal irhanteiti4Low) {
        this.irhanteiti4Low = irhanteiti4Low;
    }

    /**
     * 耐電圧設定条件 ＩＲ④ 判定値
     * @return the irhanteiti4
     */
    public BigDecimal getIrhanteiti4() {
        return irhanteiti4;
    }

    /**
     * 耐電圧設定条件 ＩＲ④ 判定値
     * @param irhanteiti4 the irhanteiti4 to set
     */
    public void setIrhanteiti4(BigDecimal irhanteiti4) {
        this.irhanteiti4 = irhanteiti4;
    }

    /**
     * 耐電圧設定条件 ＩＲ④ 判定値 単位
     * @return irhantei4tani
     */
    public String getIrhantei4tani() {
        return irhantei4tani;
    }

    /**
     * 耐電圧設定条件 ＩＲ④ 判定値 単位
     * @param irhantei4tani
     */
    public void setIrhantei4tani(String irhantei4tani) {
        this.irhantei4tani = irhantei4tani;
    }

    /**
     * 耐電圧設定条件 ＩＲ④ 充電時間
     * @return the irjudenjikan4
     */
    public Integer getIrjudenjikan4() {
        return irjudenjikan4;
    }

    /**
     * 耐電圧設定条件 ＩＲ④ 充電時間
     * @param irjudenjikan4 the irjudenjikan4 to set
     */
    public void setIrjudenjikan4(Integer irjudenjikan4) {
        this.irjudenjikan4 = irjudenjikan4;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑤ 電圧
     * @return the irdenatu5
     */
    public BigDecimal getIrdenatu5() {
        return irdenatu5;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑤ 電圧
     * @param irdenatu5 the irdenatu5 to set
     */
    public void setIrdenatu5(BigDecimal irdenatu5) {
        this.irdenatu5 = irdenatu5;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑤ 判定値(低)
     * @return the irhanteiti5Low
     */
    public BigDecimal getIrhanteiti5Low() {
        return irhanteiti5Low;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑤ 判定値(低)
     * @param irhanteiti5Low the irhanteiti5Low to set
     */
    public void setIrhanteiti5Low(BigDecimal irhanteiti5Low) {
        this.irhanteiti5Low = irhanteiti5Low;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑤ 判定値
     * @return the irhanteiti5
     */
    public BigDecimal getIrhanteiti5() {
        return irhanteiti5;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑤ 判定値
     * @param irhanteiti5 the irhanteiti5 to set
     */
    public void setIrhanteiti5(BigDecimal irhanteiti5) {
        this.irhanteiti5 = irhanteiti5;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑤ 判定値 単位
     * @return irhantei5tani
     */
    public String getIrhantei5tani() {
        return irhantei5tani;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑤ 判定値 単位
     * @param irhantei5tani
     */
    public void setIrhantei5tani(String irhantei5tani) {
        this.irhantei5tani = irhantei5tani;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑤ 充電時間
     * @return the irjudenjikan5
     */
    public Integer getIrjudenjikan5() {
        return irjudenjikan5;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑤ 充電時間
     * @param irjudenjikan5 the irjudenjikan5 to set
     */
    public void setIrjudenjikan5(Integer irjudenjikan5) {
        this.irjudenjikan5 = irjudenjikan5;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑥ 電圧
     * @return the irdenatu6
     */
    public BigDecimal getIrdenatu6() {
        return irdenatu6;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑥ 電圧
     * @param irdenatu6 the irdenatu6 to set
     */
    public void setIrdenatu6(BigDecimal irdenatu6) {
        this.irdenatu6 = irdenatu6;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑥ 判定値(低)
     * @return the irhanteiti6Low
     */
    public BigDecimal getIrhanteiti6Low() {
        return irhanteiti6Low;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑥ 判定値(低)
     * @param irhanteiti6Low the irhanteiti6Low to set
     */
    public void setIrhanteiti6Low(BigDecimal irhanteiti6Low) {
        this.irhanteiti6Low = irhanteiti6Low;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑥ 判定値
     * @return the irhanteiti6
     */
    public BigDecimal getIrhanteiti6() {
        return irhanteiti6;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑥ 判定値
     * @param irhanteiti6 the irhanteiti6 to set
     */
    public void setIrhanteiti6(BigDecimal irhanteiti6) {
        this.irhanteiti6 = irhanteiti6;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑥ 判定値 単位
     * @return irhantei6tani
     */
    public String getIrhantei6tani() {
        return irhantei6tani;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑥ 判定値 単位
     * @param irhantei6tani
     */
    public void setIrhantei6tani(String irhantei6tani) {
        this.irhantei6tani = irhantei6tani;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑥ 充電時間
     * @return the irjudenjikan6
     */
    public Integer getIrjudenjikan6() {
        return irjudenjikan6;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑥ 充電時間
     * @param irjudenjikan6 the irjudenjikan6 to set
     */
    public void setIrjudenjikan6(Integer irjudenjikan6) {
        this.irjudenjikan6 = irjudenjikan6;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑦ 電圧
     * @return the irdenatu7
     */
    public BigDecimal getIrdenatu7() {
        return irdenatu7;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑦ 電圧
     * @param irdenatu7 the irdenatu7 to set
     */
    public void setIrdenatu7(BigDecimal irdenatu7) {
        this.irdenatu7 = irdenatu7;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑦ 判定値(低)
     * @return the irhanteiti7Low
     */
    public BigDecimal getIrhanteiti7Low() {
        return irhanteiti7Low;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑦ 判定値(低)
     * @param irhanteiti7Low the irhanteiti7Low to set
     */
    public void setIrhanteiti7Low(BigDecimal irhanteiti7Low) {
        this.irhanteiti7Low = irhanteiti7Low;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑦ 判定値
     * @return the irhanteiti7
     */
    public BigDecimal getIrhanteiti7() {
        return irhanteiti7;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑦ 判定値
     * @param irhanteiti7 the irhanteiti7 to set
     */
    public void setIrhanteiti7(BigDecimal irhanteiti7) {
        this.irhanteiti7 = irhanteiti7;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑦ 判定値 単位
     * @return irhantei7tani
     */
    public String getIrhantei7tani() {
        return irhantei7tani;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑦ 判定値 単位
     * @param irhantei7tani
     */
    public void setIrhantei7tani(String irhantei7tani) {
        this.irhantei7tani = irhantei7tani;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑦ 充電時間
     * @return the irjudenjikan7
     */
    public Integer getIrjudenjikan7() {
        return irjudenjikan7;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑦ 充電時間
     * @param irjudenjikan7 the irjudenjikan7 to set
     */
    public void setIrjudenjikan7(Integer irjudenjikan7) {
        this.irjudenjikan7 = irjudenjikan7;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑧ 電圧
     * @return the irdenatu8
     */
    public BigDecimal getIrdenatu8() {
        return irdenatu8;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑧ 電圧
     * @param irdenatu8 the irdenatu8 to set
     */
    public void setIrdenatu8(BigDecimal irdenatu8) {
        this.irdenatu8 = irdenatu8;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑧ 判定値(低)
     * @return the irhanteiti8Low
     */
    public BigDecimal getIrhanteiti8Low() {
        return irhanteiti8Low;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑧ 判定値(低)
     * @param irhanteiti8Low the irhanteiti8Low to set
     */
    public void setIrhanteiti8Low(BigDecimal irhanteiti8Low) {
        this.irhanteiti8Low = irhanteiti8Low;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑧ 判定値
     * @return the irhanteiti8
     */
    public BigDecimal getIrhanteiti8() {
        return irhanteiti8;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑧ 判定値
     * @param irhanteiti8 the irhanteiti8 to set
     */
    public void setIrhanteiti8(BigDecimal irhanteiti8) {
        this.irhanteiti8 = irhanteiti8;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑧ 判定値 単位
     * @return irhantei8tani
     */
    public String getIrhantei8tani() {
        return irhantei8tani;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑧ 判定値 単位
     * @param irhantei8tani
     */
    public void setIrhantei8tani(String irhantei8tani) {
        this.irhantei8tani = irhantei8tani;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑧ 充電時間
     * @return the irjudenjikan8
     */
    public Integer getIrjudenjikan8() {
        return irjudenjikan8;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑧ 充電時間
     * @param irjudenjikan8 the irjudenjikan8 to set
     */
    public void setIrjudenjikan8(Integer irjudenjikan8) {
        this.irjudenjikan8 = irjudenjikan8;
    }
    
    /**
     * RDC1 ﾚﾝｼﾞ
     * @return the rdcrange1
     */
    public BigDecimal getRdcrange1() {
        return rdcrange1;
    }

    /**
     * RDC1 ﾚﾝｼﾞ
     * @param rdcrange1 the rdcrange1 to set
     */
    public void setRdcrange1(BigDecimal rdcrange1) {
        this.rdcrange1 = rdcrange1;
    }

    /**
     * RDC1 判定値
     * @return the rdchantei1
     */
    public BigDecimal getRdchantei1() {
        return rdchantei1;
    }

    /**
     * RDC1 判定値
     * @param rdchantei1 the rdchantei1 to set
     */
    public void setRdchantei1(BigDecimal rdchantei1) {
        this.rdchantei1 = rdchantei1;
    }

    /**
     * RDC2 ﾚﾝｼﾞ
     * @return the rdcrange2
     */
    public BigDecimal getRdcrange2() {
        return rdcrange2;
    }

    /**
     * RDC2 ﾚﾝｼﾞ
     * @param rdcrange2 the rdcrange2 to set
     */
    public void setRdcrange2(BigDecimal rdcrange2) {
        this.rdcrange2 = rdcrange2;
    }

    /**
     * RDC2 判定値
     * @return the rdchantei2
     */
    public BigDecimal getRdchantei2() {
        return rdchantei2;
    }

    /**
     * RDC2 判定値
     * @param rdchantei2 the rdchantei2 to set
     */
    public void setRdchantei2(BigDecimal rdchantei2) {
        this.rdchantei2 = rdchantei2;
    }

    /**
     * BIN1 ｶｳﾝﾀｰ数
     * @return the bin1countersuu
     */
    public Integer getBin1countersuu() {
        return bin1countersuu;
    }

    /**
     * BIN1 ｶｳﾝﾀｰ数
     * @param bin1countersuu the bin1countersuu to set
     */
    public void setBin1countersuu(Integer bin1countersuu) {
        this.bin1countersuu = bin1countersuu;
    }

    /**
     * BIN2 ｶｳﾝﾀｰ数
     * @return the bin2countersuu
     */
    public Integer getBin2countersuu() {
        return bin2countersuu;
    }

    /**
     * BIN2 ｶｳﾝﾀｰ数
     * @param bin2countersuu the bin2countersuu to set
     */
    public void setBin2countersuu(Integer bin2countersuu) {
        this.bin2countersuu = bin2countersuu;
    }

    /**
     * BIN3 ｶｳﾝﾀｰ数
     * @return the bin3countersuu
     */
    public Integer getBin3countersuu() {
        return bin3countersuu;
    }

    /**
     * BIN3 ｶｳﾝﾀｰ数
     * @param bin3countersuu the bin3countersuu to set
     */
    public void setBin3countersuu(Integer bin3countersuu) {
        this.bin3countersuu = bin3countersuu;
    }

    /**
     * BIN4 ｶｳﾝﾀｰ数
     * @return the bin4countersuu
     */
    public Integer getBin4countersuu() {
        return bin4countersuu;
    }

    /**
     * BIN4 ｶｳﾝﾀｰ数
     * @param bin4countersuu the bin4countersuu to set
     */
    public void setBin4countersuu(Integer bin4countersuu) {
        this.bin4countersuu = bin4countersuu;
    }

    /**
     * BIN5 ｶｳﾝﾀｰ数
     * @return the bin5countersuu
     */
    public Integer getBin5countersuu() {
        return bin5countersuu;
    }

    /**
     * BIN5 ｶｳﾝﾀｰ数
     * @param bin5countersuu the bin5countersuu to set
     */
    public void setBin5countersuu(Integer bin5countersuu) {
        this.bin5countersuu = bin5countersuu;
    }

    /**
     * BIN6 ｶｳﾝﾀｰ数
     * @return the bin6countersuu
     */
    public Integer getBin6countersuu() {
        return bin6countersuu;
    }

    /**
     * BIN6 ｶｳﾝﾀｰ数
     * @param bin6countersuu the bin6countersuu to set
     */
    public void setBin6countersuu(Integer bin6countersuu) {
        this.bin6countersuu = bin6countersuu;
    }

    /**
     * BIN7 ｶｳﾝﾀｰ数
     * @return the bin7countersuu
     */
    public Integer getBin7countersuu() {
        return bin7countersuu;
    }

    /**
     * BIN7 ｶｳﾝﾀｰ数
     * @param bin7countersuu the bin7countersuu to set
     */
    public void setBin7countersuu(Integer bin7countersuu) {
        this.bin7countersuu = bin7countersuu;
    }

    /**
     * BIN8 ｶｳﾝﾀｰ数
     * @return the bin8countersuu
     */
    public Integer getBin8countersuu() {
        return bin8countersuu;
    }

    /**
     * BIN8 ｶｳﾝﾀｰ数
     * @param bin8countersuu the bin8countersuu to set
     */
    public void setBin8countersuu(Integer bin8countersuu) {
        this.bin8countersuu = bin8countersuu;
    }

    /**
     * BIN5 %区分(設定値)
     * @return the bin5setteiti
     */
    public String getBin5setteiti() {
        return bin5setteiti;
    }

    /**
     * BIN5 %区分(設定値)
     * @param bin5setteiti the bin5setteiti to set
     */
    public void setBin5setteiti(String bin5setteiti) {
        this.bin5setteiti = bin5setteiti;
    }

    /**
     * BIN6 %区分(設定値)
     * @return the bin6setteiti
     */
    public String getBin6setteiti() {
        return bin6setteiti;
    }

    /**
     * BIN6 %区分(設定値)
     * @param bin6setteiti the bin6setteiti to set
     */
    public void setBin6setteiti(String bin6setteiti) {
        this.bin6setteiti = bin6setteiti;
    }

    /**
     * BIN7 %区分(設定値)
     * @return the bin7setteiti
     */
    public String getBin7setteiti() {
        return bin7setteiti;
    }

    /**
     * BIN7 %区分(設定値)
     * @param bin7setteiti the bin7setteiti to set
     */
    public void setBin7setteiti(String bin7setteiti) {
        this.bin7setteiti = bin7setteiti;
    }

    /**
     * BIN8 %区分(設定値)
     * @return the bin8setteiti
     */
    public String getBin8setteiti() {
        return bin8setteiti;
    }

    /**
     * BIN8 %区分(設定値)
     * @param bin8setteiti the bin8setteiti to set
     */
    public void setBin8setteiti(String bin8setteiti) {
        this.bin8setteiti = bin8setteiti;
    }

    /**
     * 登録日時
     * @return the torokuDate
     */
    public Timestamp getTorokuDate() {
        return torokuDate;
    }

    /**
     * 登録日時
     * @param torokuDate the torokuDate to set
     */
    public void setTorokuDate(Timestamp torokuDate) {
        this.torokuDate = torokuDate;
    }

    /**
     * 更新日時
     * @return the koshinDate
     */
    public Timestamp getKoshinDate() {
        return koshinDate;
    }

    /**
     * 更新日時
     * @param koshinDate the koshinDate to set
     */
    public void setKoshinDate(Timestamp koshinDate) {
        this.koshinDate = koshinDate;
    }

    /**
     * 削除ﾌﾗｸﾞ
     * @return the deleteflag
     */
    public Integer getDeleteflag() {
        return deleteflag;
    }

    /**
     * 削除ﾌﾗｸﾞ
     * @param deleteflag the deleteflag to set
     */
    public void setDeleteflag(Integer deleteflag) {
        this.deleteflag = deleteflag;
    }

    /**
     * DROP1,3 PC
     * @return the drop13pc
     */
    public BigDecimal getDrop13pc() {
        return drop13pc;
    }

    /**
     * DROP1,3 PC
     * @param drop13pc the drop13pc to set
     */
    public void setDrop13pc(BigDecimal drop13pc) {
        this.drop13pc = drop13pc;
    }

    /**
     * DROP1,3 PS
     * @return the drop13ps
     */
    public BigDecimal getDrop13ps() {
        return drop13ps;
    }

    /**
     * DROP1,3 PS
     * @param drop13ps the drop13ps to set
     */
    public void setDrop13ps(BigDecimal drop13ps) {
        this.drop13ps = drop13ps;
    }

    /**
     * DROP1,3 MS･DC
     * @return the drop13msdc
     */
    public BigDecimal getDrop13msdc() {
        return drop13msdc;
    }

    /**
     * DROP1,3 MS･DC
     * @param drop13msdc the drop13msdc to set
     */
    public void setDrop13msdc(BigDecimal drop13msdc) {
        this.drop13msdc = drop13msdc;
    }

    /**
     * DROP2,4 PC
     * @return the drop24pc
     */
    public BigDecimal getDrop24pc() {
        return drop24pc;
    }

    /**
     * DROP2,4 PC
     * @param drop24pc the drop24pc to set
     */
    public void setDrop24pc(BigDecimal drop24pc) {
        this.drop24pc = drop24pc;
    }

    /**
     * DROP2,4 PS
     * @return the drop24ps
     */
    public BigDecimal getDrop24ps() {
        return drop24ps;
    }

    /**
     * DROP2,4 PS
     * @param drop24ps the drop24ps to set
     */
    public void setDrop24ps(BigDecimal drop24ps) {
        this.drop24ps = drop24ps;
    }

    /**
     * DROP2,4 MS･DC
     * @return the drop24msdc
     */
    public BigDecimal getDrop24msdc() {
        return drop24msdc;
    }

    /**
     * DROP2,4 MS･DC
     * @param drop24msdc the drop24msdc to set
     */
    public void setDrop24msdc(BigDecimal drop24msdc) {
        this.drop24msdc = drop24msdc;
    }

    /**
     * BIN1 選別区分
     * @return the bin1senbetsukbn
     */
    public String getBin1senbetsukbn() {
        return bin1senbetsukbn;
    }

    /**
     * BIN1 選別区分
     * @param bin1senbetsukbn the bin1senbetsukbn to set
     */
    public void setBin1senbetsukbn(String bin1senbetsukbn) {
        this.bin1senbetsukbn = bin1senbetsukbn;
    }

    /**
     * BIN2 選別区分
     * @return the bin2senbetsukbn
     */
    public String getBin2senbetsukbn() {
        return bin2senbetsukbn;
    }

    /**
     * BIN2 選別区分
     * @param bin2senbetsukbn the bin2senbetsukbn to set
     */
    public void setBin2senbetsukbn(String bin2senbetsukbn) {
        this.bin2senbetsukbn = bin2senbetsukbn;
    }

    /**
     * BIN3 選別区分
     * @return the bin3senbetsukbn
     */
    public String getBin3senbetsukbn() {
        return bin3senbetsukbn;
    }

    /**
     * BIN3 選別区分
     * @param bin3senbetsukbn the bin3senbetsukbn to set
     */
    public void setBin3senbetsukbn(String bin3senbetsukbn) {
        this.bin3senbetsukbn = bin3senbetsukbn;
    }

    /**
     * BIN4 選別区分
     * @return the bin4senbetsukbn
     */
    public String getBin4senbetsukbn() {
        return bin4senbetsukbn;
    }

    /**
     * BIN4 選別区分
     * @param bin4senbetsukbn the bin4senbetsukbn to set
     */
    public void setBin4senbetsukbn(String bin4senbetsukbn) {
        this.bin4senbetsukbn = bin4senbetsukbn;
    }

    /**
     * BIN5 選別区分
     * @return the bin5senbetsukbn
     */
    public String getBin5senbetsukbn() {
        return bin5senbetsukbn;
    }

    /**
     * BIN5 選別区分
     * @param bin5senbetsukbn the bin5senbetsukbn to set
     */
    public void setBin5senbetsukbn(String bin5senbetsukbn) {
        this.bin5senbetsukbn = bin5senbetsukbn;
    }

    /**
     * BIN6 選別区分
     * @return the bin6senbetsukbn
     */
    public String getBin6senbetsukbn() {
        return bin6senbetsukbn;
    }

    /**
     * BIN6 選別区分
     * @param bin6senbetsukbn the bin6senbetsukbn to set
     */
    public void setBin6senbetsukbn(String bin6senbetsukbn) {
        this.bin6senbetsukbn = bin6senbetsukbn;
    }

    /**
     * BIN7 選別区分
     * @return the bin7senbetsukbn
     */
    public String getBin7senbetsukbn() {
        return bin7senbetsukbn;
    }

    /**
     * BIN7 選別区分
     * @param bin7senbetsukbn the bin7senbetsukbn to set
     */
    public void setBin7senbetsukbn(String bin7senbetsukbn) {
        this.bin7senbetsukbn = bin7senbetsukbn;
    }

    /**
     * BIN8 選別区分
     * @return the bin8senbetsukbn
     */
    public String getBin8senbetsukbn() {
        return bin8senbetsukbn;
    }

    /**
     * BIN8 選別区分
     * @param bin8senbetsukbn the bin8senbetsukbn to set
     */
    public void setBin8senbetsukbn(String bin8senbetsukbn) {
        this.bin8senbetsukbn = bin8senbetsukbn;
    }

    /**
     * ﾃｽﾄﾌﾟﾚｰﾄ管理No
     * @return the testplatekanrino
     */
    public String getTestplatekanrino() {
        return testplatekanrino;
    }

    /**
     * ﾃｽﾄﾌﾟﾚｰﾄ管理No
     * @param testplatekanrino the testplatekanrino to set
     */
    public void setTestplatekanrino(String testplatekanrino) {
        this.testplatekanrino = testplatekanrino;
    }

    /**
     * 半田サンプル
     * @return handasample
     */
    public String getHandasample() {
        return handasample;
    }

    /**
     * 半田サンプル
     * @param handasample
     */
    public void setHandasample(String handasample) {
        this.handasample = handasample;
    }

    /**
     * 信頼性サンプル
     * @return sinraiseisample
     */
    public String getSinraiseisample() {
        return sinraiseisample;
    }

    /**
     * 信頼性サンプル
     * @param sinraiseisample
     */
    public void setSinraiseisample(String sinraiseisample) {
        this.sinraiseisample = sinraiseisample;
    }

    /**
     * 検査場所
     * @return kensabasyo
     */
    public String getKensabasyo() {
        return kensabasyo;
    }

    /**
     * 検査場所
     * @param kensabasyo
     */
    public void setKensabasyo(String kensabasyo) {
        this.kensabasyo = kensabasyo;
    }

    /**
     * 選別順序変更
     * @return senbetujunjo
     */
    public String getSenbetujunjo() {
        return senbetujunjo;
    }

    /**
     * 選別順序変更
     * @param senbetujunjo
     */
    public void setSenbetujunjo(String senbetujunjo) {
        this.senbetujunjo = senbetujunjo;
    }

    /**
     * 設定ﾓｰﾄﾞ確認
     * @return setteikakunin
     */
    public String getSetteikakunin() {
        return setteikakunin;
    }

    /**
     * 設定ﾓｰﾄﾞ確認
     * @param setteikakunin
     */
    public void setSetteikakunin(String setteikakunin) {
        this.setteikakunin = setteikakunin;
    }

    /**
     * 配線確認
     * @return haisenkakunin
     */
    public String getHaisenkakunin() {
        return haisenkakunin;
    }

    /**
     * 配線確認
     * @param haisenkakunin
     */
    public void setHaisenkakunin(String haisenkakunin) {
        this.haisenkakunin = haisenkakunin;
    }

    /**
     * 固定電極 外観・段差
     * @return koteidenkyoku
     */
    public String getKoteidenkyoku() {
        return koteidenkyoku;
    }

    /**
     * 固定電極 外観・段差
     * @param koteidenkyoku
     */
    public void setKoteidenkyoku(String koteidenkyoku) {
        this.koteidenkyoku = koteidenkyoku;
    }

    /**
     * ﾃｽﾄﾌﾟﾚｰﾄ　形状・清掃
     * @return testplatekeijo
     */
    public String getTestplatekeijo() {
        return testplatekeijo;
    }

    /**
     * ﾃｽﾄﾌﾟﾚｰﾄ　形状・清掃
     * @param testplatekeijo
     */
    public void setTestplatekeijo(String testplatekeijo) {
        this.testplatekeijo = testplatekeijo;
    }

    /**
     * 分類吹き出し穴
     * @return bunruifukidasi
     */
    public String getBunruifukidasi() {
        return bunruifukidasi;
    }

    /**
     * 分類吹き出し穴
     * @param bunruifukidasi
     */
    public void setBunruifukidasi(String bunruifukidasi) {
        this.bunruifukidasi = bunruifukidasi;
    }

    /**
     * ﾃｽﾄﾌﾟﾚｰﾄ位置確認(穴位置)
     * @return testplatekakunin
     */
    public String getTestplatekakunin() {
        return testplatekakunin;
    }

    /**
     * ﾃｽﾄﾌﾟﾚｰﾄ位置確認(穴位置)
     * @param testplatekakunin
     */
    public void setTestplatekakunin(String testplatekakunin) {
        this.testplatekakunin = testplatekakunin;
    }

    /**
     * 製品投入状態
     * @return seihintounyuujotai
     */
    public String getSeihintounyuujotai() {
        return seihintounyuujotai;
    }

    /**
     * 製品投入状態
     * @param seihintounyuujotai
     */
    public void setSeihintounyuujotai(String seihintounyuujotai) {
        this.seihintounyuujotai = seihintounyuujotai;
    }

    /**
     * 分類確認
     * @return bunruikakunin
     */
    public String getBunruikakunin() {
        return bunruikakunin;
    }

    /**
     * 分類確認
     * @param bunruikakunin
     */
    public void setBunruikakunin(String bunruikakunin) {
        this.bunruikakunin = bunruikakunin;
    }

    /**
     * 外観確認
     * @return gaikankakunin
     */
    public String getGaikankakunin() {
        return gaikankakunin;
    }

    /**
     * 外観確認
     * @param gaikankakunin
     */
    public void setGaikankakunin(String gaikankakunin) {
        this.gaikankakunin = gaikankakunin;
    }

    /**
     * 選別開始日時
     * @return senbetukaisinitiji
     */
    public Timestamp getSenbetukaisinitiji() {
        return senbetukaisinitiji;
    }

    /**
     * 選別開始日時
     * @param senbetukaisinitiji
     */
    public void setSenbetukaisinitiji(Timestamp senbetukaisinitiji) {
        this.senbetukaisinitiji = senbetukaisinitiji;
    }

    /**
     * 選別終了日時
     * @return senbetusyuryounitiji
     */
    public Timestamp getSenbetusyuryounitiji() {
        return senbetusyuryounitiji;
    }

    /**
     * 選別終了日時
     * @param senbetusyuryounitiji
     */
    public void setSenbetusyuryounitiji(Timestamp senbetusyuryounitiji) {
        this.senbetusyuryounitiji = senbetusyuryounitiji;
    }

    /**
     * 設定値BIN1low
     * @return setteiti1low
     */
    public String getSetteiti1low() {
        return setteiti1low;
    }

    /**
     * 設定値BIN1low
     * @param setteiti1low
     */
    public void setSetteiti1low(String setteiti1low) {
        this.setteiti1low = setteiti1low;
    }

    /**
     * 設定値BIN1up
     * @return setteiti1up
     */
    public String getSetteiti1up() {
        return setteiti1up;
    }

    /**
     * 設定値BIN1up
     * @param setteiti1up
     */
    public void setSetteiti1up(String setteiti1up) {
        this.setteiti1up = setteiti1up;
    }

    /**
     * 設定値BIN2low
     * @return setteiti2low
     */
    public String getSetteiti2low() {
        return setteiti2low;
    }

    /**
     * 設定値BIN2low
     * @param setteiti2low
     */
    public void setSetteiti2low(String setteiti2low) {
        this.setteiti2low = setteiti2low;
    }

    /**
     * 設定値BIN2up
     * @return setteiti2up
     */
    public String getSetteiti2up() {
        return setteiti2up;
    }

    /**
     * 設定値BIN2up
     * @param setteiti2up
     */
    public void setSetteiti2up(String setteiti2up) {
        this.setteiti2up = setteiti2up;
    }

    /**
     * 設定値BIN3low
     * @return setteiti3low
     */
    public String getSetteiti3low() {
        return setteiti3low;
    }

    /**
     * 設定値BIN3low
     * @param setteiti3low
     */
    public void setSetteiti3low(String setteiti3low) {
        this.setteiti3low = setteiti3low;
    }

    /**
     * 設定値BIN3up
     * @return setteiti3up
     */
    public String getSetteiti3up() {
        return setteiti3up;
    }

    /**
     * 設定値BIN3up
     * @param setteiti3up
     */
    public void setSetteiti3up(String setteiti3up) {
        this.setteiti3up = setteiti3up;
    }

    /**
     * TTNG1
     * @return ttng1
     */
    public Integer getTtng1() {
        return ttng1;
    }

    /**
     * TTNG1
     * @param ttng1
     */
    public void setTtng1(Integer ttng1) {
        this.ttng1 = ttng1;
    }

    /**
     * TTNG2
     * @return ttng2
     */
    public Integer getTtng2() {
        return ttng2;
    }

    /**
     * TTNG2
     * @param ttng2
     */
    public void setTtng2(Integer ttng2) {
        this.ttng2 = ttng2;
    }

    /**
     * MC
     * @return mc
     */
    public Integer getMc() {
        return mc;
    }

    /**
     * MC
     * @param mc
     */
    public void setMc(Integer mc) {
        this.mc = mc;
    }

    /**
     * RI
     * @return ri
     */
    public Integer getRi() {
        return ri;
    }

    /**
     * RI
     * @param ri
     */
    public void setRi(Integer ri) {
        this.ri = ri;
    }

    /**
     * DNG
     * @return dng
     */
    public Integer getDng() {
        return dng;
    }

    /**
     * DNG
     * @param dng
     */
    public void setDng(Integer dng) {
        this.dng = dng;
    }

    /**
     * RNG
     * @return rng
     */
    public Integer getRng() {
        return rng;
    }

    /**
     * RNG
     * @param rng
     */
    public void setRng(Integer rng) {
        this.rng = rng;
    }

    /**
     * DropNG
     * @return dropng
     */
    public Integer getDropng() {
        return dropng;
    }

    /**
     * DropNG
     * @param dropng
     */
    public void setDropng(Integer dropng) {
        this.dropng = dropng;
    }

    /**
     * DropNG1
     * @return dropng1
     */
    public Integer getDropng1() {
        return dropng1;
    }

    /**
     * DropNG1
     * @param dropng1
     */
    public void setDropng1(Integer dropng1) {
        this.dropng1 = dropng1;
    }

    /**
     * DropNG2
     * @return dropng2
     */
    public Integer getDropng2() {
        return dropng2;
    }

    /**
     * DropNG2
     * @param dropng2
     */
    public void setDropng2(Integer dropng2) {
        this.dropng2 = dropng2;
    }

    /**
     * ﾛｯﾄ終了区分
     * @return lotkbn
     */
    public String getLotkbn() {
        return lotkbn;
    }

    /**
     * ﾛｯﾄ終了区分
     * @param lotkbn
     */
    public void setLotkbn(String lotkbn) {
        this.lotkbn = lotkbn;
    }

    /**
     * 設定値cap1
     * @return setteicap1
     */
    public Integer getSetteicap1() {
        return setteicap1;
    }

    /**
     * 設定値cap1
     * @param setteicap1
     */
    public void setSetteicap1(Integer setteicap1) {
        this.setteicap1 = setteicap1;
    }

    /**
     * 設定値cap2
     * @return setteicap2
     */
    public Integer getSetteicap2() {
        return setteicap2;
    }

    /**
     * 設定値cap2
     * @param setteicap2
     */
    public void setSetteicap2(Integer setteicap2) {
        this.setteicap2 = setteicap2;
    }

    /**
     * 設定値cap3
     * @return setteicap3
     */
    public Integer getSetteicap3() {
        return setteicap3;
    }

    /**
     * 設定値cap3
     * @param setteicap3
     */
    public void setSetteicap3(Integer setteicap3) {
        this.setteicap3 = setteicap3;
    }
}
