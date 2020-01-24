/*
 * Copyright 2020 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.jaxrs;

import java.math.BigDecimal;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2020/01/23<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCCS D.Yanagida<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * データ取込処理のモデルクラスです。
 */
public class FXHDD07Json {
    private String kojyo;
    private String lotno;
    private String edaban;
    private String gouki;
    private Integer bunruiairatu;
    private Integer cdcontactatu;
    private Integer ircontactatu;
    private BigDecimal tan;
    private String sokuteisyuhasuu;
    private BigDecimal sokuteidenatu;
    private BigDecimal pcdenatu1;
    private Integer pcjudenjikan1;
    private BigDecimal pcdenatu2;
    private Integer pcjudenjikan2;
    private BigDecimal pcdenatu3;
    private Integer pcjudenjikan3;
    private BigDecimal pcdenatu4;
    private Integer pcjudenjikan4;
    private BigDecimal irdenatu1;
    private BigDecimal irhanteiti1_low;
    private BigDecimal irhanteiti1;
    private Integer irjudenjikan1;
    private BigDecimal irdenatu2;
    private BigDecimal irhanteiti2_low;
    private BigDecimal irhanteiti2;
    private Integer irjudenjikan2;
    private BigDecimal irdenatu3;
    private BigDecimal irhanteiti3_low;
    private BigDecimal irhanteiti3;
    private Integer irjudenjikan3;
    private BigDecimal irdenatu4;
    private BigDecimal irhanteiti4_low;
    private BigDecimal irhanteiti4;
    private Integer irjudenjikan4;
    private BigDecimal irdenatu5;
    private BigDecimal irhanteiti5_low;
    private BigDecimal irhanteiti5;
    private Integer irjudenjikan5;
    private BigDecimal irdenatu6;
    private BigDecimal irhanteiti6_low;
    private BigDecimal irhanteiti6;
    private Integer irjudenjikan6;
    private BigDecimal irdenatu7;
    private BigDecimal irhanteiti7_low;
    private BigDecimal irhanteiti7;
    private Integer irjudenjikan7;
    private BigDecimal irdenatu8;
    private BigDecimal irhanteiti8_low;
    private BigDecimal irhanteiti8;
    private Integer irjudenjikan8;
    private BigDecimal rdcrange1;
    private BigDecimal rdchantei1;
    private BigDecimal rdcrange2;
    private BigDecimal rdchantei2;
    private Integer bin1countersuu;
    private Integer bin2countersuu;
    private Integer bin3countersuu;
    private Integer bin4countersuu;
    private Integer bin5countersuu;
    private Integer bin6countersuu;
    private Integer bin7countersuu;
    private Integer bin8countersuu;
    private String bin5setteiti;
    private String bin6setteiti;
    private String bin7setteiti;
    private String bin8setteiti;
    
    /**
     * @return the kojyo
     */
    public String getKojyo() {
        return kojyo;
    }

    /**
     * @param kojyo the kojyo to set
     */
    public void setKojyo(String kojyo) {
        this.kojyo = kojyo;
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
     * @return the edaban
     */
    public String getEdaban() {
        return edaban;
    }

    /**
     * @param edaban the edaban to set
     */
    public void setEdaban(String edaban) {
        this.edaban = edaban;
    }

    /**
     * @return the gouki
     */
    public String getGouki() {
        return gouki;
    }

    /**
     * @param gouki the gouki to set
     */
    public void setGouki(String gouki) {
        this.gouki = gouki;
    }

    /**
     * @return the bunruiairatu
     */
    public Integer getBunruiairatu() {
        return bunruiairatu;
    }

    /**
     * @param bunruiairatu the bunruiairatu to set
     */
    public void setBunruiairatu(Integer bunruiairatu) {
        this.bunruiairatu = bunruiairatu;
    }

    /**
     * @return the cdcontactatu
     */
    public Integer getCdcontactatu() {
        return cdcontactatu;
    }

    /**
     * @param cdcontactatu the cdcontactatu to set
     */
    public void setCdcontactatu(Integer cdcontactatu) {
        this.cdcontactatu = cdcontactatu;
    }

    /**
     * @return the ircontactatu
     */
    public Integer getIrcontactatu() {
        return ircontactatu;
    }

    /**
     * @param ircontactatu the ircontactatu to set
     */
    public void setIrcontactatu(Integer ircontactatu) {
        this.ircontactatu = ircontactatu;
    }

    /**
     * @return the tan
     */
    public BigDecimal getTan() {
        return tan;
    }

    /**
     * @param tan the tan to set
     */
    public void setTan(BigDecimal tan) {
        this.tan = tan;
    }

    /**
     * @return the sokuteisyuhasuu
     */
    public String getSokuteisyuhasuu() {
        return sokuteisyuhasuu;
    }

    /**
     * @param sokuteisyuhasuu the sokuteisyuhasuu to set
     */
    public void setSokuteisyuhasuu(String sokuteisyuhasuu) {
        this.sokuteisyuhasuu = sokuteisyuhasuu;
    }

    /**
     * @return the sokuteidenatu
     */
    public BigDecimal getSokuteidenatu() {
        return sokuteidenatu;
    }

    /**
     * @param sokuteidenatu the sokuteidenatu to set
     */
    public void setSokuteidenatu(BigDecimal sokuteidenatu) {
        this.sokuteidenatu = sokuteidenatu;
    }

    /**
     * @return the pcdenatu1
     */
    public BigDecimal getPcdenatu1() {
        return pcdenatu1;
    }

    /**
     * @param pcdenatu1 the pcdenatu1 to set
     */
    public void setPcdenatu1(BigDecimal pcdenatu1) {
        this.pcdenatu1 = pcdenatu1;
    }

    /**
     * @return the pcjudenjikan1
     */
    public Integer getPcjudenjikan1() {
        return pcjudenjikan1;
    }

    /**
     * @param pcjudenjikan1 the pcjudenjikan1 to set
     */
    public void setPcjudenjikan1(Integer pcjudenjikan1) {
        this.pcjudenjikan1 = pcjudenjikan1;
    }

    /**
     * @return the pcdenatu2
     */
    public BigDecimal getPcdenatu2() {
        return pcdenatu2;
    }

    /**
     * @param pcdenatu2 the pcdenatu2 to set
     */
    public void setPcdenatu2(BigDecimal pcdenatu2) {
        this.pcdenatu2 = pcdenatu2;
    }

    /**
     * @return the pcjudenjikan2
     */
    public Integer getPcjudenjikan2() {
        return pcjudenjikan2;
    }

    /**
     * @param pcjudenjikan2 the pcjudenjikan2 to set
     */
    public void setPcjudenjikan2(Integer pcjudenjikan2) {
        this.pcjudenjikan2 = pcjudenjikan2;
    }

    /**
     * @return the pcdenatu3
     */
    public BigDecimal getPcdenatu3() {
        return pcdenatu3;
    }

    /**
     * @param pcdenatu3 the pcdenatu3 to set
     */
    public void setPcdenatu3(BigDecimal pcdenatu3) {
        this.pcdenatu3 = pcdenatu3;
    }

    /**
     * @return the pcjudenjikan3
     */
    public Integer getPcjudenjikan3() {
        return pcjudenjikan3;
    }

    /**
     * @param pcjudenjikan3 the pcjudenjikan3 to set
     */
    public void setPcjudenjikan3(Integer pcjudenjikan3) {
        this.pcjudenjikan3 = pcjudenjikan3;
    }

    /**
     * @return the pcdenatu4
     */
    public BigDecimal getPcdenatu4() {
        return pcdenatu4;
    }

    /**
     * @param pcdenatu4 the pcdenatu4 to set
     */
    public void setPcdenatu4(BigDecimal pcdenatu4) {
        this.pcdenatu4 = pcdenatu4;
    }

    /**
     * @return the pcjudenjikan4
     */
    public Integer getPcjudenjikan4() {
        return pcjudenjikan4;
    }

    /**
     * @param pcjudenjikan4 the pcjudenjikan4 to set
     */
    public void setPcjudenjikan4(Integer pcjudenjikan4) {
        this.pcjudenjikan4 = pcjudenjikan4;
    }

    /**
     * @return the irdenatu1
     */
    public BigDecimal getIrdenatu1() {
        return irdenatu1;
    }

    /**
     * @param irdenatu1 the irdenatu1 to set
     */
    public void setIrdenatu1(BigDecimal irdenatu1) {
        this.irdenatu1 = irdenatu1;
    }

    /**
     * @return the irhanteiti1_low
     */
    public BigDecimal getIrhanteiti1_low() {
        return irhanteiti1_low;
    }

    /**
     * @param irhanteiti1_low the irhanteiti1_low to set
     */
    public void setIrhanteiti1_low(BigDecimal irhanteiti1_low) {
        this.irhanteiti1_low = irhanteiti1_low;
    }

    /**
     * @return the irhanteiti1
     */
    public BigDecimal getIrhanteiti1() {
        return irhanteiti1;
    }

    /**
     * @param irhanteiti1 the irhanteiti1 to set
     */
    public void setIrhanteiti1(BigDecimal irhanteiti1) {
        this.irhanteiti1 = irhanteiti1;
    }

    /**
     * @return the irjudenjikan1
     */
    public Integer getIrjudenjikan1() {
        return irjudenjikan1;
    }

    /**
     * @param irjudenjikan1 the irjudenjikan1 to set
     */
    public void setIrjudenjikan1(Integer irjudenjikan1) {
        this.irjudenjikan1 = irjudenjikan1;
    }

    /**
     * @return the irdenatu2
     */
    public BigDecimal getIrdenatu2() {
        return irdenatu2;
    }

    /**
     * @param irdenatu2 the irdenatu2 to set
     */
    public void setIrdenatu2(BigDecimal irdenatu2) {
        this.irdenatu2 = irdenatu2;
    }

    /**
     * @return the irhanteiti2_low
     */
    public BigDecimal getIrhanteiti2_low() {
        return irhanteiti2_low;
    }

    /**
     * @param irhanteiti2_low the irhanteiti2_low to set
     */
    public void setIrhanteiti2_low(BigDecimal irhanteiti2_low) {
        this.irhanteiti2_low = irhanteiti2_low;
    }

    /**
     * @return the irhanteiti2
     */
    public BigDecimal getIrhanteiti2() {
        return irhanteiti2;
    }

    /**
     * @param irhanteiti2 the irhanteiti2 to set
     */
    public void setIrhanteiti2(BigDecimal irhanteiti2) {
        this.irhanteiti2 = irhanteiti2;
    }

    /**
     * @return the irjudenjikan2
     */
    public Integer getIrjudenjikan2() {
        return irjudenjikan2;
    }

    /**
     * @param irjudenjikan2 the irjudenjikan2 to set
     */
    public void setIrjudenjikan2(Integer irjudenjikan2) {
        this.irjudenjikan2 = irjudenjikan2;
    }

    /**
     * @return the irdenatu3
     */
    public BigDecimal getIrdenatu3() {
        return irdenatu3;
    }

    /**
     * @param irdenatu3 the irdenatu3 to set
     */
    public void setIrdenatu3(BigDecimal irdenatu3) {
        this.irdenatu3 = irdenatu3;
    }

    /**
     * @return the irhanteiti3_low
     */
    public BigDecimal getIrhanteiti3_low() {
        return irhanteiti3_low;
    }

    /**
     * @param irhanteiti3_low the irhanteiti3_low to set
     */
    public void setIrhanteiti3_low(BigDecimal irhanteiti3_low) {
        this.irhanteiti3_low = irhanteiti3_low;
    }

    /**
     * @return the irhanteiti3
     */
    public BigDecimal getIrhanteiti3() {
        return irhanteiti3;
    }

    /**
     * @param irhanteiti3 the irhanteiti3 to set
     */
    public void setIrhanteiti3(BigDecimal irhanteiti3) {
        this.irhanteiti3 = irhanteiti3;
    }

    /**
     * @return the irjudenjikan3
     */
    public Integer getIrjudenjikan3() {
        return irjudenjikan3;
    }

    /**
     * @param irjudenjikan3 the irjudenjikan3 to set
     */
    public void setIrjudenjikan3(Integer irjudenjikan3) {
        this.irjudenjikan3 = irjudenjikan3;
    }

    /**
     * @return the irdenatu4
     */
    public BigDecimal getIrdenatu4() {
        return irdenatu4;
    }

    /**
     * @param irdenatu4 the irdenatu4 to set
     */
    public void setIrdenatu4(BigDecimal irdenatu4) {
        this.irdenatu4 = irdenatu4;
    }

    /**
     * @return the irhanteiti4_low
     */
    public BigDecimal getIrhanteiti4_low() {
        return irhanteiti4_low;
    }

    /**
     * @param irhanteiti4_low the irhanteiti4_low to set
     */
    public void setIrhanteiti4_low(BigDecimal irhanteiti4_low) {
        this.irhanteiti4_low = irhanteiti4_low;
    }

    /**
     * @return the irhanteiti4
     */
    public BigDecimal getIrhanteiti4() {
        return irhanteiti4;
    }

    /**
     * @param irhanteiti4 the irhanteiti4 to set
     */
    public void setIrhanteiti4(BigDecimal irhanteiti4) {
        this.irhanteiti4 = irhanteiti4;
    }

    /**
     * @return the irjudenjikan4
     */
    public Integer getIrjudenjikan4() {
        return irjudenjikan4;
    }

    /**
     * @param irjudenjikan4 the irjudenjikan4 to set
     */
    public void setIrjudenjikan4(Integer irjudenjikan4) {
        this.irjudenjikan4 = irjudenjikan4;
    }

    /**
     * @return the irdenatu5
     */
    public BigDecimal getIrdenatu5() {
        return irdenatu5;
    }

    /**
     * @param irdenatu5 the irdenatu5 to set
     */
    public void setIrdenatu5(BigDecimal irdenatu5) {
        this.irdenatu5 = irdenatu5;
    }

    /**
     * @return the irhanteiti5_low
     */
    public BigDecimal getIrhanteiti5_low() {
        return irhanteiti5_low;
    }

    /**
     * @param irhanteiti5_low the irhanteiti5_low to set
     */
    public void setIrhanteiti5_low(BigDecimal irhanteiti5_low) {
        this.irhanteiti5_low = irhanteiti5_low;
    }

    /**
     * @return the irhanteiti5
     */
    public BigDecimal getIrhanteiti5() {
        return irhanteiti5;
    }

    /**
     * @param irhanteiti5 the irhanteiti5 to set
     */
    public void setIrhanteiti5(BigDecimal irhanteiti5) {
        this.irhanteiti5 = irhanteiti5;
    }

    /**
     * @return the irjudenjikan5
     */
    public Integer getIrjudenjikan5() {
        return irjudenjikan5;
    }

    /**
     * @param irjudenjikan5 the irjudenjikan5 to set
     */
    public void setIrjudenjikan5(Integer irjudenjikan5) {
        this.irjudenjikan5 = irjudenjikan5;
    }

    /**
     * @return the irdenatu6
     */
    public BigDecimal getIrdenatu6() {
        return irdenatu6;
    }

    /**
     * @param irdenatu6 the irdenatu6 to set
     */
    public void setIrdenatu6(BigDecimal irdenatu6) {
        this.irdenatu6 = irdenatu6;
    }

    /**
     * @return the irhanteiti6_low
     */
    public BigDecimal getIrhanteiti6_low() {
        return irhanteiti6_low;
    }

    /**
     * @param irhanteiti6_low the irhanteiti6_low to set
     */
    public void setIrhanteiti6_low(BigDecimal irhanteiti6_low) {
        this.irhanteiti6_low = irhanteiti6_low;
    }

    /**
     * @return the irhanteiti6
     */
    public BigDecimal getIrhanteiti6() {
        return irhanteiti6;
    }

    /**
     * @param irhanteiti6 the irhanteiti6 to set
     */
    public void setIrhanteiti6(BigDecimal irhanteiti6) {
        this.irhanteiti6 = irhanteiti6;
    }

    /**
     * @return the irjudenjikan6
     */
    public Integer getIrjudenjikan6() {
        return irjudenjikan6;
    }

    /**
     * @param irjudenjikan6 the irjudenjikan6 to set
     */
    public void setIrjudenjikan6(Integer irjudenjikan6) {
        this.irjudenjikan6 = irjudenjikan6;
    }

    /**
     * @return the irdenatu7
     */
    public BigDecimal getIrdenatu7() {
        return irdenatu7;
    }

    /**
     * @param irdenatu7 the irdenatu7 to set
     */
    public void setIrdenatu7(BigDecimal irdenatu7) {
        this.irdenatu7 = irdenatu7;
    }

    /**
     * @return the irhanteiti7_low
     */
    public BigDecimal getIrhanteiti7_low() {
        return irhanteiti7_low;
    }

    /**
     * @param irhanteiti7_low the irhanteiti7_low to set
     */
    public void setIrhanteiti7_low(BigDecimal irhanteiti7_low) {
        this.irhanteiti7_low = irhanteiti7_low;
    }

    /**
     * @return the irhanteiti7
     */
    public BigDecimal getIrhanteiti7() {
        return irhanteiti7;
    }

    /**
     * @param irhanteiti7 the irhanteiti7 to set
     */
    public void setIrhanteiti7(BigDecimal irhanteiti7) {
        this.irhanteiti7 = irhanteiti7;
    }

    /**
     * @return the irjudenjikan7
     */
    public Integer getIrjudenjikan7() {
        return irjudenjikan7;
    }

    /**
     * @param irjudenjikan7 the irjudenjikan7 to set
     */
    public void setIrjudenjikan7(Integer irjudenjikan7) {
        this.irjudenjikan7 = irjudenjikan7;
    }

    /**
     * @return the irdenatu8
     */
    public BigDecimal getIrdenatu8() {
        return irdenatu8;
    }

    /**
     * @param irdenatu8 the irdenatu8 to set
     */
    public void setIrdenatu8(BigDecimal irdenatu8) {
        this.irdenatu8 = irdenatu8;
    }

    /**
     * @return the irhanteiti8_low
     */
    public BigDecimal getIrhanteiti8_low() {
        return irhanteiti8_low;
    }

    /**
     * @param irhanteiti8_low the irhanteiti8_low to set
     */
    public void setIrhanteiti8_low(BigDecimal irhanteiti8_low) {
        this.irhanteiti8_low = irhanteiti8_low;
    }

    /**
     * @return the irhanteiti8
     */
    public BigDecimal getIrhanteiti8() {
        return irhanteiti8;
    }

    /**
     * @param irhanteiti8 the irhanteiti8 to set
     */
    public void setIrhanteiti8(BigDecimal irhanteiti8) {
        this.irhanteiti8 = irhanteiti8;
    }

    /**
     * @return the irjudenjikan8
     */
    public Integer getIrjudenjikan8() {
        return irjudenjikan8;
    }

    /**
     * @param irjudenjikan8 the irjudenjikan8 to set
     */
    public void setIrjudenjikan8(Integer irjudenjikan8) {
        this.irjudenjikan8 = irjudenjikan8;
    }

    /**
     * @return the rdcrange1
     */
    public BigDecimal getRdcrange1() {
        return rdcrange1;
    }

    /**
     * @param rdcrange1 the rdcrange1 to set
     */
    public void setRdcrange1(BigDecimal rdcrange1) {
        this.rdcrange1 = rdcrange1;
    }

    /**
     * @return the rdchantei1
     */
    public BigDecimal getRdchantei1() {
        return rdchantei1;
    }

    /**
     * @param rdchantei1 the rdchantei1 to set
     */
    public void setRdchantei1(BigDecimal rdchantei1) {
        this.rdchantei1 = rdchantei1;
    }

    /**
     * @return the rdcrange2
     */
    public BigDecimal getRdcrange2() {
        return rdcrange2;
    }

    /**
     * @param rdcrange2 the rdcrange2 to set
     */
    public void setRdcrange2(BigDecimal rdcrange2) {
        this.rdcrange2 = rdcrange2;
    }

    /**
     * @return the rdchantei2
     */
    public BigDecimal getRdchantei2() {
        return rdchantei2;
    }

    /**
     * @param rdchantei2 the rdchantei2 to set
     */
    public void setRdchantei2(BigDecimal rdchantei2) {
        this.rdchantei2 = rdchantei2;
    }

    /**
     * @return the bin1countersuu
     */
    public Integer getBin1countersuu() {
        return bin1countersuu;
    }

    /**
     * @param bin1countersuu the bin1countersuu to set
     */
    public void setBin1countersuu(Integer bin1countersuu) {
        this.bin1countersuu = bin1countersuu;
    }

    /**
     * @return the bin2countersuu
     */
    public Integer getBin2countersuu() {
        return bin2countersuu;
    }

    /**
     * @param bin2countersuu the bin2countersuu to set
     */
    public void setBin2countersuu(Integer bin2countersuu) {
        this.bin2countersuu = bin2countersuu;
    }

    /**
     * @return the bin3countersuu
     */
    public Integer getBin3countersuu() {
        return bin3countersuu;
    }

    /**
     * @param bin3countersuu the bin3countersuu to set
     */
    public void setBin3countersuu(Integer bin3countersuu) {
        this.bin3countersuu = bin3countersuu;
    }

    /**
     * @return the bin4countersuu
     */
    public Integer getBin4countersuu() {
        return bin4countersuu;
    }

    /**
     * @param bin4countersuu the bin4countersuu to set
     */
    public void setBin4countersuu(Integer bin4countersuu) {
        this.bin4countersuu = bin4countersuu;
    }

    /**
     * @return the bin5countersuu
     */
    public Integer getBin5countersuu() {
        return bin5countersuu;
    }

    /**
     * @param bin5countersuu the bin5countersuu to set
     */
    public void setBin5countersuu(Integer bin5countersuu) {
        this.bin5countersuu = bin5countersuu;
    }

    /**
     * @return the bin6countersuu
     */
    public Integer getBin6countersuu() {
        return bin6countersuu;
    }

    /**
     * @param bin6countersuu the bin6countersuu to set
     */
    public void setBin6countersuu(Integer bin6countersuu) {
        this.bin6countersuu = bin6countersuu;
    }

    /**
     * @return the bin7countersuu
     */
    public Integer getBin7countersuu() {
        return bin7countersuu;
    }

    /**
     * @param bin7countersuu the bin7countersuu to set
     */
    public void setBin7countersuu(Integer bin7countersuu) {
        this.bin7countersuu = bin7countersuu;
    }

    /**
     * @return the bin8countersuu
     */
    public Integer getBin8countersuu() {
        return bin8countersuu;
    }

    /**
     * @param bin8countersuu the bin8countersuu to set
     */
    public void setBin8countersuu(Integer bin8countersuu) {
        this.bin8countersuu = bin8countersuu;
    }

    /**
     * @return the bin5setteiti
     */
    public String getBin5setteiti() {
        return bin5setteiti;
    }

    /**
     * @param bin5setteiti the bin5setteiti to set
     */
    public void setBin5setteiti(String bin5setteiti) {
        this.bin5setteiti = bin5setteiti;
    }

    /**
     * @return the bin6setteiti
     */
    public String getBin6setteiti() {
        return bin6setteiti;
    }

    /**
     * @param bin6setteiti the bin6setteiti to set
     */
    public void setBin6setteiti(String bin6setteiti) {
        this.bin6setteiti = bin6setteiti;
    }

    /**
     * @return the bin7setteiti
     */
    public String getBin7setteiti() {
        return bin7setteiti;
    }

    /**
     * @param bin7setteiti the bin7setteiti to set
     */
    public void setBin7setteiti(String bin7setteiti) {
        this.bin7setteiti = bin7setteiti;
    }

    /**
     * @return the bin8setteiti
     */
    public String getBin8setteiti() {
        return bin8setteiti;
    }

    /**
     * @param bin8setteiti the bin8setteiti to set
     */
    public void setBin8setteiti(String bin8setteiti) {
        this.bin8setteiti = bin8setteiti;
    }
}
