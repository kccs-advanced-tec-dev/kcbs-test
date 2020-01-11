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
    private Integer bunruiairatu;

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
     * 耐電圧設定条件 ＩＲ① 判定値
     */
    private String irhanteiti1;

    /**
     * 耐電圧設定条件 ＩＲ① 充電時間
     */
    private Integer irjudenjikan1;

    /**
     * 耐電圧設定条件 ＩＲ② 電圧
     */
    private BigDecimal irdenatu2;

    /**
     * 耐電圧設定条件 ＩＲ② 判定値
     */
    private String irhanteiti2;

    /**
     * 耐電圧設定条件 ＩＲ② 充電時間
     */
    private Integer irjudenjikan2;

    /**
     * 耐電圧設定条件 ＩＲ③ 電圧
     */
    private BigDecimal irdenatu3;

    /**
     * 耐電圧設定条件 ＩＲ③ 判定値
     */
    private String irhanteiti3;

    /**
     * 耐電圧設定条件 ＩＲ③ 充電時間
     */
    private Integer irjudenjikan3;

    /**
     * 耐電圧設定条件 ＩＲ④ 電圧
     */
    private BigDecimal irdenatu4;

    /**
     * 耐電圧設定条件 ＩＲ④ 判定値
     */
    private String irhanteiti4;

    /**
     * 耐電圧設定条件 ＩＲ④ 充電時間
     */
    private Integer irjudenjikan4;

    /**
     * 耐電圧設定条件 ＩＲ⑤ 電圧
     */
    private BigDecimal irdenatu5;

    /**
     * 耐電圧設定条件 ＩＲ⑤ 判定値
     */
    private String irhanteiti5;

    /**
     * 耐電圧設定条件 ＩＲ⑤ 充電時間
     */
    private Integer irjudenjikan5;

    /**
     * 耐電圧設定条件 ＩＲ⑥ 電圧
     */
    private BigDecimal irdenatu6;

    /**
     * 耐電圧設定条件 ＩＲ⑥ 判定値
     */
    private String irhanteiti6;

    /**
     * 耐電圧設定条件 ＩＲ⑥ 充電時間
     */
    private Integer irjudenjikan6;

    /**
     * 耐電圧設定条件 ＩＲ⑦ 電圧
     */
    private BigDecimal irdenatu7;

    /**
     * 耐電圧設定条件 ＩＲ⑦ 判定値
     */
    private String irhanteiti7;

    /**
     * 耐電圧設定条件 ＩＲ⑦ 充電時間
     */
    private Integer irjudenjikan7;

    /**
     * 耐電圧設定条件 ＩＲ⑧ 電圧
     */
    private BigDecimal irdenatu8;

    /**
     * 耐電圧設定条件 ＩＲ⑧ 判定値
     */
    private String irhanteiti8;

    /**
     * 耐電圧設定条件 ＩＲ⑧ 充電時間
     */
    private Integer irjudenjikan8;

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
     * 削除ﾌﾗｸﾞ
     */
    private Integer deleteflag;
    
    
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
    public Integer getBunruiairatu() {
        return bunruiairatu;
    }

    /**
     * 分類ｴｱｰ圧
     * @param bunruiairatu the bunruiairatu to set
     */
    public void setBunruiairatu(Integer bunruiairatu) {
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
     * 耐電圧設定条件 ＩＲ① 判定値
     * @return the irhanteiti1
     */
    public String getIrhanteiti1() {
        return irhanteiti1;
    }

    /**
     * 耐電圧設定条件 ＩＲ① 判定値
     * @param irhanteiti1 the irhanteiti1 to set
     */
    public void setIrhanteiti1(String irhanteiti1) {
        this.irhanteiti1 = irhanteiti1;
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
     * 耐電圧設定条件 ＩＲ② 判定値
     * @return the irhanteiti2
     */
    public String getIrhanteiti2() {
        return irhanteiti2;
    }

    /**
     * 耐電圧設定条件 ＩＲ② 判定値
     * @param irhanteiti2 the irhanteiti2 to set
     */
    public void setIrhanteiti2(String irhanteiti2) {
        this.irhanteiti2 = irhanteiti2;
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
    public String getIrhanteiti3() {
        return irhanteiti3;
    }

    /**
     * 耐電圧設定条件 ＩＲ③ 判定値
     * @param irhanteiti3 the irhanteiti3 to set
     */
    public void setIrhanteiti3(String irhanteiti3) {
        this.irhanteiti3 = irhanteiti3;
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
     * 耐電圧設定条件 ＩＲ④ 判定値
     * @return the irhanteiti4
     */
    public String getIrhanteiti4() {
        return irhanteiti4;
    }

    /**
     * 耐電圧設定条件 ＩＲ④ 判定値
     * @param irhanteiti4 the irhanteiti4 to set
     */
    public void setIrhanteiti4(String irhanteiti4) {
        this.irhanteiti4 = irhanteiti4;
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
     * 耐電圧設定条件 ＩＲ⑤ 判定値
     * @return the irhanteiti5
     */
    public String getIrhanteiti5() {
        return irhanteiti5;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑤ 判定値
     * @param irhanteiti5 the irhanteiti5 to set
     */
    public void setIrhanteiti5(String irhanteiti5) {
        this.irhanteiti5 = irhanteiti5;
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
     * 耐電圧設定条件 ＩＲ⑥ 判定値
     * @return the irhanteiti6
     */
    public String getIrhanteiti6() {
        return irhanteiti6;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑥ 判定値
     * @param irhanteiti6 the irhanteiti6 to set
     */
    public void setIrhanteiti6(String irhanteiti6) {
        this.irhanteiti6 = irhanteiti6;
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
     * 耐電圧設定条件 ＩＲ⑦ 判定値
     * @return the irhanteiti7
     */
    public String getIrhanteiti7() {
        return irhanteiti7;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑦ 判定値
     * @param irhanteiti7 the irhanteiti7 to set
     */
    public void setIrhanteiti7(String irhanteiti7) {
        this.irhanteiti7 = irhanteiti7;
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
     * 耐電圧設定条件 ＩＲ⑧ 判定値
     * @return the irhanteiti8
     */
    public String getIrhanteiti8() {
        return irhanteiti8;
    }

    /**
     * 耐電圧設定条件 ＩＲ⑧ 判定値
     * @param irhanteiti8 the irhanteiti8 to set
     */
    public void setIrhanteiti8(String irhanteiti8) {
        this.irhanteiti8 = irhanteiti8;
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

}
