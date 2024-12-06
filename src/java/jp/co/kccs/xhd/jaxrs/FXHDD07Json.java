/*
 * Copyright 2020 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.jaxrs;


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
 * 変更日	2020/04/09<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	項目追加対応<br>
 * <br>
 * 変更日	2024/12/06<br>
 * 計画書No	MB2408-D009<br>
 * 変更者	SYSNAVI H.Kamo<br>
 * 変更理由	電気特性・新設備（TWA）対応<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * データ取込処理のモデルクラスです。
 */
public class FXHDD07Json {
    private String maker;
    private String kojyo;
    private String lotno;
    private String edaban;
    private String gouki;
    private String bunruiairatu;
    private String cdcontactatu;
    private String ircontactatu;
    private String tan;
    private String sokuteisyuhasuu;
    private String sokuteidenatu;
    private String pcdenatu1;
    private String pcjudenjikan1;
    private String pcdenatu2;
    private String pcjudenjikan2;
    private String pcdenatu3;
    private String pcjudenjikan3;
    private String pcdenatu4;
    private String pcjudenjikan4;
    private String irdenatu1;
    private String irhanteiti1_low;
    private String irhanteiti1;
    private String irjudenjikan1;
    private String irdenatu2;
    private String irhanteiti2_low;
    private String irhanteiti2;
    private String irjudenjikan2;
    private String irdenatu3;
    private String irhanteiti3_low;
    private String irhanteiti3;
    private String irjudenjikan3;
    private String irdenatu4;
    private String irhanteiti4_low;
    private String irhanteiti4;
    private String irjudenjikan4;
    private String irdenatu5;
    private String irhanteiti5_low;
    private String irhanteiti5;
    private String irjudenjikan5;
    private String irdenatu6;
    private String irhanteiti6_low;
    private String irhanteiti6;
    private String irjudenjikan6;
    private String irdenatu7;
    private String irhanteiti7_low;
    private String irhanteiti7;
    private String irjudenjikan7;
    private String irdenatu8;
    private String irhanteiti8_low;
    private String irhanteiti8;
    private String irjudenjikan8;
    private String rdcrange1;
    private String rdchantei1;
    private String rdcrange2;
    private String rdchantei2;
    private String bin1countersuu;
    private String bin2countersuu;
    private String bin3countersuu;
    private String bin4countersuu;
    private String bin5countersuu;
    private String bin6countersuu;
    private String bin7countersuu;
    private String bin8countersuu;
    private String bin5setteiti;
    private String bin6setteiti;
    private String bin7setteiti;
    private String bin8setteiti;
    private String drop13pc;
    private String drop13ps;
    private String drop13msdc;
    private String drop24pc;
    private String drop24ps;
    private String drop24msdc;
    private String bin1senbetsukbn;
    private String bin2senbetsukbn;
    private String bin3senbetsukbn;
    private String bin4senbetsukbn;
    private String bin5senbetsukbn;
    private String bin6senbetsukbn;
    private String bin7senbetsukbn;
    private String bin8senbetsukbn;
    private String testplatekanrino;
    private String irhantei1tani;
    private String irhantei2tani;
    private String irhantei3tani;
    private String irhantei4tani;
    private String irhantei5tani;
    private String irhantei6tani;
    private String irhantei7tani;
    private String irhantei8tani;
    private String handasample;
    private String sinraiseisample;
    private String kensabasyo;
    private String senbetujunjo;
    private String setteikakunin;
    private String haisenkakunin;
    private String koteidenkyoku;
    private String testplatekeijo;
    private String bunruifukidasi;
    private String testplatekakunin;
    private String seihintounyuujotai;
    private String bunruikakunin;
    private String gaikankakunin;
    private String senbetukaisinitiji;
    private String senbetusyuryounitiji;
    private String setteiti1low;
    private String setteiti1up;
    private String setteiti2low;
    private String setteiti2up;
    private String setteiti3low;
    private String setteiti3up;
    private String ttng1;
    private String ttng2;
    private String mc;
    private String ri;
    private String dng;
    private String rng;
    private String dropng;
    private String dropng1;
    private String dropng2;
    private String lotkbn;
    private String setteicap1;
    private String setteicap2;
    private String setteicap3;
    private String irhantei1tani_low;
    private String irhantei2tani_low;
    private String irhantei3tani_low;
    private String irhantei4tani_low;
    private String irhantei5tani_low;
    private String irhantei6tani_low;
    private String irhantei7tani_low;
    private String irhantei8tani_low;
    private String denkyokuseisou;
    private String setteicap4;
    private String setteiti4low;
    private String setteiti4up;
    private String bin1setteiti;
    private String bin2setteiti;
    private String bin3setteiti;
    private String bin4setteiti;
    private String gentenhukkidousa;
    private String sokuteiki12dousakakunin;
    private String sokuteipinfront;
    private String sokuteipinrear;
    private String ir1denryustart;
    private String ir1denryustarttani;
    private String ir1denryuend;
    private String ir1denryuendtani;
    private String ir1sokuteihanistart;
    private String ir1sokuteihanistarttani;
    private String ir1sokuteihaniend;
    private String ir1sokuteihaniendtani;
    private String ir2denryustart;
    private String ir2denryustarttani;
    private String ir2denryuend;
    private String ir2denryuendtani;
    private String ir2sokuteihanistart;
    private String ir2sokuteihanistarttani;
    private String ir2sokuteihaniend;
    private String ir2sokuteihaniendtani;
    private String ir3denryustart;
    private String ir3denryustarttani;
    private String ir3denryuend;
    private String ir3denryuendtani;
    private String ir3sokuteihanistart;
    private String ir3sokuteihanistarttani;
    private String ir3sokuteihaniend;
    private String ir3sokuteihaniendtani;
    private String ir4denryustart;
    private String ir4denryustarttani;
    private String ir4denryuend;
    private String ir4denryuendtani;
    private String ir4sokuteihanistart;
    private String ir4sokuteihanistarttani;
    private String ir4sokuteihaniend;
    private String ir4sokuteihaniendtani;
    private String ir5denryustart;
    private String ir5denryustarttani;
    private String ir5denryuend;
    private String ir5denryuendtani;
    private String ir5sokuteihanistart;
    private String ir5sokuteihanistarttani;
    private String ir5sokuteihaniend;
    private String ir5sokuteihaniendtani;
    private String ir6denryustart;
    private String ir6denryustarttani;
    private String ir6denryuend;
    private String ir6denryuendtani;
    private String ir6sokuteihanistart;
    private String ir6sokuteihanistarttani;
    private String ir6sokuteihaniend;
    private String ir6sokuteihaniendtani;
    private String ir7denryustart;
    private String ir7denryustarttani;
    private String ir7denryuend;
    private String ir7denryuendtani;
    private String ir7sokuteihanistart;
    private String ir7sokuteihanistarttani;
    private String ir7sokuteihaniend;
    private String ir7sokuteihaniendtani;
    private String ir8denryustart;
    private String ir8denryustarttani;
    private String ir8denryuend;
    private String ir8denryuendtani;
    private String ir8sokuteihanistart;
    private String ir8sokuteihanistarttani;
    private String ir8sokuteihaniend;
    private String ir8sokuteihaniendtani;
    private String senbetukaisinitijitwa;
    private String senbetusyuryounitijitwa;
    private String satsample;
    private String binboxseisoucheck;
    private String bin1setteititwa;
    private String bin2setteititwa;
    private String bin3setteititwa;
    private String bin4setteititwa;
    private String bin5setteititwa;
    private String bin6setteititwa;
    private String bin7setteititwa;
    private String bin8setteititwa;
    private String hoseiyoutippuyoryou;
    private String hoseiyoutipputan;
    private String ir1jikan;
    private String ir1jikantani;
    private String ir2jikan;
    private String ir2jikantani;
    private String ir3jikan;
    private String ir3jikantani;
    private String ir4jikan;
    private String ir4jikantani;
    private String ir5jikan;
    private String ir5jikantani;
    private String ir6jikan;
    private String ir6jikantani;
    private String ir7jikan;
    private String ir7jikantani;
    private String ir8jikan;
    private String ir8jikantani;

    /**
     * @return the maker
     */
    public String getMaker() {
        return maker;
    }

    /**
     * @param maker the maker to set
     */
    public void setMaker(String maker) {
        this.maker = maker;
    }
    
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
    public String getBunruiairatu() {
        return bunruiairatu;
    }

    /**
     * @param bunruiairatu the bunruiairatu to set
     */
    public void setBunruiairatu(String bunruiairatu) {
        this.bunruiairatu = bunruiairatu;
    }

    /**
     * @return the cdcontactatu
     */
    public String getCdcontactatu() {
        return cdcontactatu;
    }

    /**
     * @param cdcontactatu the cdcontactatu to set
     */
    public void setCdcontactatu(String cdcontactatu) {
        this.cdcontactatu = cdcontactatu;
    }

    /**
     * @return the ircontactatu
     */
    public String getIrcontactatu() {
        return ircontactatu;
    }

    /**
     * @param ircontactatu the ircontactatu to set
     */
    public void setIrcontactatu(String ircontactatu) {
        this.ircontactatu = ircontactatu;
    }

    /**
     * @return the tan
     */
    public String getTan() {
        return tan;
    }

    /**
     * @param tan the tan to set
     */
    public void setTan(String tan) {
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
    public String getSokuteidenatu() {
        return sokuteidenatu;
    }

    /**
     * @param sokuteidenatu the sokuteidenatu to set
     */
    public void setSokuteidenatu(String sokuteidenatu) {
        this.sokuteidenatu = sokuteidenatu;
    }

    /**
     * @return the pcdenatu1
     */
    public String getPcdenatu1() {
        return pcdenatu1;
    }

    /**
     * @param pcdenatu1 the pcdenatu1 to set
     */
    public void setPcdenatu1(String pcdenatu1) {
        this.pcdenatu1 = pcdenatu1;
    }

    /**
     * @return the pcjudenjikan1
     */
    public String getPcjudenjikan1() {
        return pcjudenjikan1;
    }

    /**
     * @param pcjudenjikan1 the pcjudenjikan1 to set
     */
    public void setPcjudenjikan1(String pcjudenjikan1) {
        this.pcjudenjikan1 = pcjudenjikan1;
    }

    /**
     * @return the pcdenatu2
     */
    public String getPcdenatu2() {
        return pcdenatu2;
    }

    /**
     * @param pcdenatu2 the pcdenatu2 to set
     */
    public void setPcdenatu2(String pcdenatu2) {
        this.pcdenatu2 = pcdenatu2;
    }

    /**
     * @return the pcjudenjikan2
     */
    public String getPcjudenjikan2() {
        return pcjudenjikan2;
    }

    /**
     * @param pcjudenjikan2 the pcjudenjikan2 to set
     */
    public void setPcjudenjikan2(String pcjudenjikan2) {
        this.pcjudenjikan2 = pcjudenjikan2;
    }

    /**
     * @return the pcdenatu3
     */
    public String getPcdenatu3() {
        return pcdenatu3;
    }

    /**
     * @param pcdenatu3 the pcdenatu3 to set
     */
    public void setPcdenatu3(String pcdenatu3) {
        this.pcdenatu3 = pcdenatu3;
    }

    /**
     * @return the pcjudenjikan3
     */
    public String getPcjudenjikan3() {
        return pcjudenjikan3;
    }

    /**
     * @param pcjudenjikan3 the pcjudenjikan3 to set
     */
    public void setPcjudenjikan3(String pcjudenjikan3) {
        this.pcjudenjikan3 = pcjudenjikan3;
    }

    /**
     * @return the pcdenatu4
     */
    public String getPcdenatu4() {
        return pcdenatu4;
    }

    /**
     * @param pcdenatu4 the pcdenatu4 to set
     */
    public void setPcdenatu4(String pcdenatu4) {
        this.pcdenatu4 = pcdenatu4;
    }

    /**
     * @return the pcjudenjikan4
     */
    public String getPcjudenjikan4() {
        return pcjudenjikan4;
    }

    /**
     * @param pcjudenjikan4 the pcjudenjikan4 to set
     */
    public void setPcjudenjikan4(String pcjudenjikan4) {
        this.pcjudenjikan4 = pcjudenjikan4;
    }

    /**
     * @return the irdenatu1
     */
    public String getIrdenatu1() {
        return irdenatu1;
    }

    /**
     * @param irdenatu1 the irdenatu1 to set
     */
    public void setIrdenatu1(String irdenatu1) {
        this.irdenatu1 = irdenatu1;
    }

    /**
     * @return the irhanteiti1_low
     */
    public String getIrhanteiti1_low() {
        return irhanteiti1_low;
    }

    /**
     * @param irhanteiti1_low the irhanteiti1_low to set
     */
    public void setIrhanteiti1_low(String irhanteiti1_low) {
        this.irhanteiti1_low = irhanteiti1_low;
    }

    /**
     * @return the irhanteiti1
     */
    public String getIrhanteiti1() {
        return irhanteiti1;
    }

    /**
     * @param irhanteiti1 the irhanteiti1 to set
     */
    public void setIrhanteiti1(String irhanteiti1) {
        this.irhanteiti1 = irhanteiti1;
    }

    /**
     * @return the irjudenjikan1
     */
    public String getIrjudenjikan1() {
        return irjudenjikan1;
    }

    /**
     * @param irjudenjikan1 the irjudenjikan1 to set
     */
    public void setIrjudenjikan1(String irjudenjikan1) {
        this.irjudenjikan1 = irjudenjikan1;
    }

    /**
     * @return the irdenatu2
     */
    public String getIrdenatu2() {
        return irdenatu2;
    }

    /**
     * @param irdenatu2 the irdenatu2 to set
     */
    public void setIrdenatu2(String irdenatu2) {
        this.irdenatu2 = irdenatu2;
    }

    /**
     * @return the irhanteiti2_low
     */
    public String getIrhanteiti2_low() {
        return irhanteiti2_low;
    }

    /**
     * @param irhanteiti2_low the irhanteiti2_low to set
     */
    public void setIrhanteiti2_low(String irhanteiti2_low) {
        this.irhanteiti2_low = irhanteiti2_low;
    }

    /**
     * @return the irhanteiti2
     */
    public String getIrhanteiti2() {
        return irhanteiti2;
    }

    /**
     * @param irhanteiti2 the irhanteiti2 to set
     */
    public void setIrhanteiti2(String irhanteiti2) {
        this.irhanteiti2 = irhanteiti2;
    }

    /**
     * @return the irjudenjikan2
     */
    public String getIrjudenjikan2() {
        return irjudenjikan2;
    }

    /**
     * @param irjudenjikan2 the irjudenjikan2 to set
     */
    public void setIrjudenjikan2(String irjudenjikan2) {
        this.irjudenjikan2 = irjudenjikan2;
    }

    /**
     * @return the irdenatu3
     */
    public String getIrdenatu3() {
        return irdenatu3;
    }

    /**
     * @param irdenatu3 the irdenatu3 to set
     */
    public void setIrdenatu3(String irdenatu3) {
        this.irdenatu3 = irdenatu3;
    }

    /**
     * @return the irhanteiti3_low
     */
    public String getIrhanteiti3_low() {
        return irhanteiti3_low;
    }

    /**
     * @param irhanteiti3_low the irhanteiti3_low to set
     */
    public void setIrhanteiti3_low(String irhanteiti3_low) {
        this.irhanteiti3_low = irhanteiti3_low;
    }

    /**
     * @return the irhanteiti3
     */
    public String getIrhanteiti3() {
        return irhanteiti3;
    }

    /**
     * @param irhanteiti3 the irhanteiti3 to set
     */
    public void setIrhanteiti3(String irhanteiti3) {
        this.irhanteiti3 = irhanteiti3;
    }

    /**
     * @return the irjudenjikan3
     */
    public String getIrjudenjikan3() {
        return irjudenjikan3;
    }

    /**
     * @param irjudenjikan3 the irjudenjikan3 to set
     */
    public void setIrjudenjikan3(String irjudenjikan3) {
        this.irjudenjikan3 = irjudenjikan3;
    }

    /**
     * @return the irdenatu4
     */
    public String getIrdenatu4() {
        return irdenatu4;
    }

    /**
     * @param irdenatu4 the irdenatu4 to set
     */
    public void setIrdenatu4(String irdenatu4) {
        this.irdenatu4 = irdenatu4;
    }

    /**
     * @return the irhanteiti4_low
     */
    public String getIrhanteiti4_low() {
        return irhanteiti4_low;
    }

    /**
     * @param irhanteiti4_low the irhanteiti4_low to set
     */
    public void setIrhanteiti4_low(String irhanteiti4_low) {
        this.irhanteiti4_low = irhanteiti4_low;
    }

    /**
     * @return the irhanteiti4
     */
    public String getIrhanteiti4() {
        return irhanteiti4;
    }

    /**
     * @param irhanteiti4 the irhanteiti4 to set
     */
    public void setIrhanteiti4(String irhanteiti4) {
        this.irhanteiti4 = irhanteiti4;
    }

    /**
     * @return the irjudenjikan4
     */
    public String getIrjudenjikan4() {
        return irjudenjikan4;
    }

    /**
     * @param irjudenjikan4 the irjudenjikan4 to set
     */
    public void setIrjudenjikan4(String irjudenjikan4) {
        this.irjudenjikan4 = irjudenjikan4;
    }

    /**
     * @return the irdenatu5
     */
    public String getIrdenatu5() {
        return irdenatu5;
    }

    /**
     * @param irdenatu5 the irdenatu5 to set
     */
    public void setIrdenatu5(String irdenatu5) {
        this.irdenatu5 = irdenatu5;
    }

    /**
     * @return the irhanteiti5_low
     */
    public String getIrhanteiti5_low() {
        return irhanteiti5_low;
    }

    /**
     * @param irhanteiti5_low the irhanteiti5_low to set
     */
    public void setIrhanteiti5_low(String irhanteiti5_low) {
        this.irhanteiti5_low = irhanteiti5_low;
    }

    /**
     * @return the irhanteiti5
     */
    public String getIrhanteiti5() {
        return irhanteiti5;
    }

    /**
     * @param irhanteiti5 the irhanteiti5 to set
     */
    public void setIrhanteiti5(String irhanteiti5) {
        this.irhanteiti5 = irhanteiti5;
    }

    /**
     * @return the irjudenjikan5
     */
    public String getIrjudenjikan5() {
        return irjudenjikan5;
    }

    /**
     * @param irjudenjikan5 the irjudenjikan5 to set
     */
    public void setIrjudenjikan5(String irjudenjikan5) {
        this.irjudenjikan5 = irjudenjikan5;
    }

    /**
     * @return the irdenatu6
     */
    public String getIrdenatu6() {
        return irdenatu6;
    }

    /**
     * @param irdenatu6 the irdenatu6 to set
     */
    public void setIrdenatu6(String irdenatu6) {
        this.irdenatu6 = irdenatu6;
    }

    /**
     * @return the irhanteiti6_low
     */
    public String getIrhanteiti6_low() {
        return irhanteiti6_low;
    }

    /**
     * @param irhanteiti6_low the irhanteiti6_low to set
     */
    public void setIrhanteiti6_low(String irhanteiti6_low) {
        this.irhanteiti6_low = irhanteiti6_low;
    }

    /**
     * @return the irhanteiti6
     */
    public String getIrhanteiti6() {
        return irhanteiti6;
    }

    /**
     * @param irhanteiti6 the irhanteiti6 to set
     */
    public void setIrhanteiti6(String irhanteiti6) {
        this.irhanteiti6 = irhanteiti6;
    }

    /**
     * @return the irjudenjikan6
     */
    public String getIrjudenjikan6() {
        return irjudenjikan6;
    }

    /**
     * @param irjudenjikan6 the irjudenjikan6 to set
     */
    public void setIrjudenjikan6(String irjudenjikan6) {
        this.irjudenjikan6 = irjudenjikan6;
    }

    /**
     * @return the irdenatu7
     */
    public String getIrdenatu7() {
        return irdenatu7;
    }

    /**
     * @param irdenatu7 the irdenatu7 to set
     */
    public void setIrdenatu7(String irdenatu7) {
        this.irdenatu7 = irdenatu7;
    }

    /**
     * @return the irhanteiti7_low
     */
    public String getIrhanteiti7_low() {
        return irhanteiti7_low;
    }

    /**
     * @param irhanteiti7_low the irhanteiti7_low to set
     */
    public void setIrhanteiti7_low(String irhanteiti7_low) {
        this.irhanteiti7_low = irhanteiti7_low;
    }

    /**
     * @return the irhanteiti7
     */
    public String getIrhanteiti7() {
        return irhanteiti7;
    }

    /**
     * @param irhanteiti7 the irhanteiti7 to set
     */
    public void setIrhanteiti7(String irhanteiti7) {
        this.irhanteiti7 = irhanteiti7;
    }

    /**
     * @return the irjudenjikan7
     */
    public String getIrjudenjikan7() {
        return irjudenjikan7;
    }

    /**
     * @param irjudenjikan7 the irjudenjikan7 to set
     */
    public void setIrjudenjikan7(String irjudenjikan7) {
        this.irjudenjikan7 = irjudenjikan7;
    }

    /**
     * @return the irdenatu8
     */
    public String getIrdenatu8() {
        return irdenatu8;
    }

    /**
     * @param irdenatu8 the irdenatu8 to set
     */
    public void setIrdenatu8(String irdenatu8) {
        this.irdenatu8 = irdenatu8;
    }

    /**
     * @return the irhanteiti8_low
     */
    public String getIrhanteiti8_low() {
        return irhanteiti8_low;
    }

    /**
     * @param irhanteiti8_low the irhanteiti8_low to set
     */
    public void setIrhanteiti8_low(String irhanteiti8_low) {
        this.irhanteiti8_low = irhanteiti8_low;
    }

    /**
     * @return the irhanteiti8
     */
    public String getIrhanteiti8() {
        return irhanteiti8;
    }

    /**
     * @param irhanteiti8 the irhanteiti8 to set
     */
    public void setIrhanteiti8(String irhanteiti8) {
        this.irhanteiti8 = irhanteiti8;
    }

    /**
     * @return the irjudenjikan8
     */
    public String getIrjudenjikan8() {
        return irjudenjikan8;
    }

    /**
     * @param irjudenjikan8 the irjudenjikan8 to set
     */
    public void setIrjudenjikan8(String irjudenjikan8) {
        this.irjudenjikan8 = irjudenjikan8;
    }

    /**
     * @return the rdcrange1
     */
    public String getRdcrange1() {
        return rdcrange1;
    }

    /**
     * @param rdcrange1 the rdcrange1 to set
     */
    public void setRdcrange1(String rdcrange1) {
        this.rdcrange1 = rdcrange1;
    }

    /**
     * @return the rdchantei1
     */
    public String getRdchantei1() {
        return rdchantei1;
    }

    /**
     * @param rdchantei1 the rdchantei1 to set
     */
    public void setRdchantei1(String rdchantei1) {
        this.rdchantei1 = rdchantei1;
    }

    /**
     * @return the rdcrange2
     */
    public String getRdcrange2() {
        return rdcrange2;
    }

    /**
     * @param rdcrange2 the rdcrange2 to set
     */
    public void setRdcrange2(String rdcrange2) {
        this.rdcrange2 = rdcrange2;
    }

    /**
     * @return the rdchantei2
     */
    public String getRdchantei2() {
        return rdchantei2;
    }

    /**
     * @param rdchantei2 the rdchantei2 to set
     */
    public void setRdchantei2(String rdchantei2) {
        this.rdchantei2 = rdchantei2;
    }

    /**
     * @return the bin1countersuu
     */
    public String getBin1countersuu() {
        return bin1countersuu;
    }

    /**
     * @param bin1countersuu the bin1countersuu to set
     */
    public void setBin1countersuu(String bin1countersuu) {
        this.bin1countersuu = bin1countersuu;
    }

    /**
     * @return the bin2countersuu
     */
    public String getBin2countersuu() {
        return bin2countersuu;
    }

    /**
     * @param bin2countersuu the bin2countersuu to set
     */
    public void setBin2countersuu(String bin2countersuu) {
        this.bin2countersuu = bin2countersuu;
    }

    /**
     * @return the bin3countersuu
     */
    public String getBin3countersuu() {
        return bin3countersuu;
    }

    /**
     * @param bin3countersuu the bin3countersuu to set
     */
    public void setBin3countersuu(String bin3countersuu) {
        this.bin3countersuu = bin3countersuu;
    }

    /**
     * @return the bin4countersuu
     */
    public String getBin4countersuu() {
        return bin4countersuu;
    }

    /**
     * @param bin4countersuu the bin4countersuu to set
     */
    public void setBin4countersuu(String bin4countersuu) {
        this.bin4countersuu = bin4countersuu;
    }

    /**
     * @return the bin5countersuu
     */
    public String getBin5countersuu() {
        return bin5countersuu;
    }

    /**
     * @param bin5countersuu the bin5countersuu to set
     */
    public void setBin5countersuu(String bin5countersuu) {
        this.bin5countersuu = bin5countersuu;
    }

    /**
     * @return the bin6countersuu
     */
    public String getBin6countersuu() {
        return bin6countersuu;
    }

    /**
     * @param bin6countersuu the bin6countersuu to set
     */
    public void setBin6countersuu(String bin6countersuu) {
        this.bin6countersuu = bin6countersuu;
    }

    /**
     * @return the bin7countersuu
     */
    public String getBin7countersuu() {
        return bin7countersuu;
    }

    /**
     * @param bin7countersuu the bin7countersuu to set
     */
    public void setBin7countersuu(String bin7countersuu) {
        this.bin7countersuu = bin7countersuu;
    }

    /**
     * @return the bin8countersuu
     */
    public String getBin8countersuu() {
        return bin8countersuu;
    }

    /**
     * @param bin8countersuu the bin8countersuu to set
     */
    public void setBin8countersuu(String bin8countersuu) {
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

    /**
     * @return the drop13pc
     */
    public String getDrop13pc() {
        return drop13pc;
    }

    /**
     * @param drop13pc the drop13pc to set
     */
    public void setDrop13pc(String drop13pc) {
        this.drop13pc = drop13pc;
    }

    /**
     * @return the drop13ps
     */
    public String getDrop13ps() {
        return drop13ps;
    }

    /**
     * @param drop13ps the drop13ps to set
     */
    public void setDrop13ps(String drop13ps) {
        this.drop13ps = drop13ps;
    }

    /**
     * @return the drop13msdc
     */
    public String getDrop13msdc() {
        return drop13msdc;
    }

    /**
     * @param drop13msdc the drop13msdc to set
     */
    public void setDrop13msdc(String drop13msdc) {
        this.drop13msdc = drop13msdc;
    }

    /**
     * @return the drop24pc
     */
    public String getDrop24pc() {
        return drop24pc;
    }

    /**
     * @param drop24pc the drop24pc to set
     */
    public void setDrop24pc(String drop24pc) {
        this.drop24pc = drop24pc;
    }

    /**
     * @return the drop24ps
     */
    public String getDrop24ps() {
        return drop24ps;
    }

    /**
     * @param drop24ps the drop24ps to set
     */
    public void setDrop24ps(String drop24ps) {
        this.drop24ps = drop24ps;
    }

    /**
     * @return the drop24msdc
     */
    public String getDrop24msdc() {
        return drop24msdc;
    }

    /**
     * @param drop24msdc the drop24msdc to set
     */
    public void setDrop24msdc(String drop24msdc) {
        this.drop24msdc = drop24msdc;
    }

    /**
     * @return the bin1senbetsukbn
     */
    public String getBin1senbetsukbn() {
        return bin1senbetsukbn;
    }

    /**
     * @param bin1senbetsukbn the bin1senbetsukbn to set
     */
    public void setBin1senbetsukbn(String bin1senbetsukbn) {
        this.bin1senbetsukbn = bin1senbetsukbn;
    }

    /**
     * @return the bin2senbetsukbn
     */
    public String getBin2senbetsukbn() {
        return bin2senbetsukbn;
    }

    /**
     * @param bin2senbetsukbn the bin2senbetsukbn to set
     */
    public void setBin2senbetsukbn(String bin2senbetsukbn) {
        this.bin2senbetsukbn = bin2senbetsukbn;
    }

    /**
     * @return the bin3senbetsukbn
     */
    public String getBin3senbetsukbn() {
        return bin3senbetsukbn;
    }

    /**
     * @param bin3senbetsukbn the bin3senbetsukbn to set
     */
    public void setBin3senbetsukbn(String bin3senbetsukbn) {
        this.bin3senbetsukbn = bin3senbetsukbn;
    }

    /**
     * @return the bin4senbetsukbn
     */
    public String getBin4senbetsukbn() {
        return bin4senbetsukbn;
    }

    /**
     * @param bin4senbetsukbn the bin4senbetsukbn to set
     */
    public void setBin4senbetsukbn(String bin4senbetsukbn) {
        this.bin4senbetsukbn = bin4senbetsukbn;
    }

    /**
     * @return the bin5senbetsukbn
     */
    public String getBin5senbetsukbn() {
        return bin5senbetsukbn;
    }

    /**
     * @param bin5senbetsukbn the bin5senbetsukbn to set
     */
    public void setBin5senbetsukbn(String bin5senbetsukbn) {
        this.bin5senbetsukbn = bin5senbetsukbn;
    }

    /**
     * @return the bin6senbetsukbn
     */
    public String getBin6senbetsukbn() {
        return bin6senbetsukbn;
    }

    /**
     * @param bin6senbetsukbn the bin6senbetsukbn to set
     */
    public void setBin6senbetsukbn(String bin6senbetsukbn) {
        this.bin6senbetsukbn = bin6senbetsukbn;
    }

    /**
     * @return the bin7senbetsukbn
     */
    public String getBin7senbetsukbn() {
        return bin7senbetsukbn;
    }

    /**
     * @param bin7senbetsukbn the bin7senbetsukbn to set
     */
    public void setBin7senbetsukbn(String bin7senbetsukbn) {
        this.bin7senbetsukbn = bin7senbetsukbn;
    }

    /**
     * @return the bin8senbetsukbn
     */
    public String getBin8senbetsukbn() {
        return bin8senbetsukbn;
    }

    /**
     * @param bin8senbetsukbn the bin8senbetsukbn to set
     */
    public void setBin8senbetsukbn(String bin8senbetsukbn) {
        this.bin8senbetsukbn = bin8senbetsukbn;
    }

    /**
     * @return testplatekanrino
     */
    public String getTestplatekanrino() {
        return testplatekanrino;
    }

    /**
     * @param testplatekanrino 
     */
    public void setTestplatekanrino(String testplatekanrino) {
        this.testplatekanrino = testplatekanrino;
    }

    /**
     * @return irhantei1tani
     */
    public String getIrhantei1tani() {
        return irhantei1tani;
    }

    /**
     * @param irhantei1tani 
     */
    public void setIrhantei1tani(String irhantei1tani) {
        this.irhantei1tani = irhantei1tani;
    }

    /**
     * @return irhantei2tani
     */
    public String getIrhantei2tani() {
        return irhantei2tani;
    }

    /**
     * @param irhantei2tani 
     */
    public void setIrhantei2tani(String irhantei2tani) {
        this.irhantei2tani = irhantei2tani;
    }
    
    /**
     * @return irhantei3tani
     */
    public String getIrhantei3tani() {
        return irhantei3tani;
    }

    /**
     * @param irhantei3tani 
     */
    public void setIrhantei3tani(String irhantei3tani) {
        this.irhantei3tani = irhantei3tani;
    }

    /**
     * @return irhantei4tani
     */
    public String getIrhantei4tani() {
        return irhantei4tani;
    }

    /**
     * @param irhantei4tani 
     */
    public void setIrhantei4tani(String irhantei4tani) {
        this.irhantei4tani = irhantei4tani;
    }

    /**
     * @return irhantei5tani
     */
    public String getIrhantei5tani() {
        return irhantei5tani;
    }

    /**
     * @param irhantei5tani 
     */
    public void setIrhantei5tani(String irhantei5tani) {
        this.irhantei5tani = irhantei5tani;
    }

    /**
     * @return irhantei6tani
     */
    public String getIrhantei6tani() {
        return irhantei6tani;
    }

    /**
     * @param irhantei6tani 
     */
    public void setIrhantei6tani(String irhantei6tani) {
        this.irhantei6tani = irhantei6tani;
    }

    /**
     * @return irhantei7tani
     */
    public String getIrhantei7tani() {
        return irhantei7tani;
    }

    /**
     * @param irhantei7tani 
     */
    public void setIrhantei7tani(String irhantei7tani) {
        this.irhantei7tani = irhantei7tani;
    }

    /**
     * @return irhantei8tani
     */
    public String getIrhantei8tani() {
        return irhantei8tani;
    }

    /**
     * @param irhantei8tani 
     */
    public void setIrhantei8tani(String irhantei8tani) {
        this.irhantei8tani = irhantei8tani;
    }

    /**
     * @return handasample
     */
    public String getHandasample() {
        return handasample;
    }

    /**
     * @param handasample 
     */
    public void setHandasample(String handasample) {
        this.handasample = handasample;
    }

    /**
     * @return sinraiseisample
     */
    public String getSinraiseisample() {
        return sinraiseisample;
    }

    /**
     * @param sinraiseisample 
     */
    public void setSinraiseisample(String sinraiseisample) {
        this.sinraiseisample = sinraiseisample;
    }

    /**
     * @return kensabasyo
     */
    public String getKensabasyo() {
        return kensabasyo;
    }

    /**
     * @param kensabasyo 
     */
    public void setKensabasyo(String kensabasyo) {
        this.kensabasyo = kensabasyo;
    }

    /**
     * @return senbetujunjo
     */
    public String getSenbetujunjo() {
        return senbetujunjo;
    }

    /**
     * @param senbetujunjo 
     */
    public void setSenbetujunjo(String senbetujunjo) {
        this.senbetujunjo = senbetujunjo;
    }

    /**
     * @return setteikakunin
     */
    public String getSetteikakunin() {
        return setteikakunin;
    }

    /**
     * @param setteikakunin 
     */
    public void setSetteikakunin(String setteikakunin) {
        this.setteikakunin = setteikakunin;
    }

    /**
     * @return haisenkakunin
     */
    public String getHaisenkakunin() {
        return haisenkakunin;
    }

    /**
     * @param haisenkakunin 
     */
    public void setHaisenkakunin(String haisenkakunin) {
        this.haisenkakunin = haisenkakunin;
    }

    /**
     * @return koteidenkyoku
     */
    public String getKoteidenkyoku() {
        return koteidenkyoku;
    }

    /**
     * @param koteidenkyoku 
     */
    public void setKoteidenkyoku(String koteidenkyoku) {
        this.koteidenkyoku = koteidenkyoku;
    }

    /**
     * @return testplatekeijo
     */
    public String getTestplatekeijo() {
        return testplatekeijo;
    }

    /**
     * @param testplatekeijo 
     */
    public void setTestplatekeijo(String testplatekeijo) {
        this.testplatekeijo = testplatekeijo;
    }

    /**
     * @return bunruifukidasi
     */
    public String getBunruifukidasi() {
        return bunruifukidasi;
    }

    /**
     * @param bunruifukidasi 
     */
    public void setBunruifukidasi(String bunruifukidasi) {
        this.bunruifukidasi = bunruifukidasi;
    }

    /**
     * @return testplatekakunin
     */
    public String getTestplatekakunin() {
        return testplatekakunin;
    }

    /**
     * @param testplatekakunin 
     */
    public void setTestplatekakunin(String testplatekakunin) {
        this.testplatekakunin = testplatekakunin;
    }

    /**
     * @return seihintounyuujotai
     */
    public String getSeihintounyuujotai() {
        return seihintounyuujotai;
    }

    /**
     * @param seihintounyuujotai 
     */
    public void setSeihintounyuujotai(String seihintounyuujotai) {
        this.seihintounyuujotai = seihintounyuujotai;
    }

    /**
     * @return bunruikakunin
     */
    public String getBunruikakunin() {
        return bunruikakunin;
    }

    /**
     * @param bunruikakunin 
     */
    public void setBunruikakunin(String bunruikakunin) {
        this.bunruikakunin = bunruikakunin;
    }

    /**
     * @return gaikankakunin
     */
    public String getGaikankakunin() {
        return gaikankakunin;
    }

    /**
     * @param gaikankakunin 
     */
    public void setGaikankakunin(String gaikankakunin) {
        this.gaikankakunin = gaikankakunin;
    }

    /**
     * @return senbetukaisinitiji
     */
    public String getSenbetukaisinitiji() {
        return senbetukaisinitiji;
    }

    /**
     * @param senbetukaisinitiji 
     */
    public void setSenbetukaisinitiji(String senbetukaisinitiji) {
        this.senbetukaisinitiji = senbetukaisinitiji;
    }

    /**
     * @return senbetusyuryounitiji
     */
    public String getSenbetusyuryounitiji() {
        return senbetusyuryounitiji;
    }

    /**
     * @param senbetusyuryounitiji 
     */
    public void setSenbetusyuryounitiji(String senbetusyuryounitiji) {
        this.senbetusyuryounitiji = senbetusyuryounitiji;
    }

    /**
     * @return setteiti1low
     */
    public String getSetteiti1low() {
        return setteiti1low;
    }

    /**
     * @param setteiti1low 
     */
    public void setSetteiti1low(String setteiti1low) {
        this.setteiti1low = setteiti1low;
    }

    /**
     * @return setteiti1up
     */
    public String getSetteiti1up() {
        return setteiti1up;
    }

    /**
     * @param setteiti1up 
     */
    public void setSetteiti1up(String setteiti1up) {
        this.setteiti1up = setteiti1up;
    }

    /**
     * @return setteiti2low
     */
    public String getSetteiti2low() {
        return setteiti2low;
    }

    /**
     * @param setteiti2low 
     */
    public void setSetteiti2low(String setteiti2low) {
        this.setteiti2low = setteiti2low;
    }

    /**
     * @return setteiti2up
     */
    public String getSetteiti2up() {
        return setteiti2up;
    }

    /**
     * @param setteiti2up 
     */
    public void setSetteiti2up(String setteiti2up) {
        this.setteiti2up = setteiti2up;
    }

    /**
     * @return setteiti3low
     */
    public String getSetteiti3low() {
        return setteiti3low;
    }

    /**
     * @param setteiti3low 
     */
    public void setSetteiti3low(String setteiti3low) {
        this.setteiti3low = setteiti3low;
    }

    /**
     * @return setteiti3up
     */
    public String getSetteiti3up() {
        return setteiti3up;
    }

    /**
     * @param setteiti3up 
     */
    public void setSetteiti3up(String setteiti3up) {
        this.setteiti3up = setteiti3up;
    }

    /**
     * @return ttng1
     */
    public String getTtng1() {
        return ttng1;
    }

    /**
     * @param ttng1 
     */
    public void setTtng1(String ttng1) {
        this.ttng1 = ttng1;
    }

    /**
     * @return ttng2
     */
    public String getTtng2() {
        return ttng2;
    }

    /**
     * @param ttng2 
     */
    public void setTtng2(String ttng2) {
        this.ttng2 = ttng2;
    }

    /**
     * @return mc
     */
    public String getMc() {
        return mc;
    }

    /**
     * @param mc 
     */
    public void setMc(String mc) {
        this.mc = mc;
    }

    /**
     * @return ri
     */
    public String getRi() {
        return ri;
    }

    /**
     * @param ri 
     */
    public void setRi(String ri) {
        this.ri = ri;
    }

    /**
     * @return dng
     */
    public String getDng() {
        return dng;
    }

    /**
     * @param dng 
     */
    public void setDng(String dng) {
        this.dng = dng;
    }

    /**
     * @return rng
     */
    public String getRng() {
        return rng;
    }

    /**
     * @param rng 
     */
    public void setRng(String rng) {
        this.rng = rng;
    }

    /**
     * @return dropng
     */
    public String getDropng() {
        return dropng;
    }

    /**
     * @param dropng 
     */
    public void setDropng(String dropng) {
        this.dropng = dropng;
    }

    /**
     * @return dropng1
     */
    public String getDropng1() {
        return dropng1;
    }

    /**
     * @param dropng1 
     */
    public void setDropng1(String dropng1) {
        this.dropng1 = dropng1;
    }

    /**
     * @return dropng2
     */
    public String getDropng2() {
        return dropng2;
    }

    /**
     * @param dropng2 
     */
    public void setDropng2(String dropng2) {
        this.dropng2 = dropng2;
    }

    /**
     * @return lotkbn
     */
    public String getLotkbn() {
        return lotkbn;
    }

    /**
     * @param lotkbn 
     */
    public void setLotkbn(String lotkbn) {
        this.lotkbn = lotkbn;
    }

    /**
     * @return setteicap1
     */
    public String getSetteicap1() {
        return setteicap1;
    }

    /**
     * @param setteicap1 
     */
    public void setSetteicap1(String setteicap1) {
        this.setteicap1 = setteicap1;
    }

    /**
     * @return setteicap2
     */
    public String getSetteicap2() {
        return setteicap2;
    }

    /**
     * @param setteicap2 
     */
    public void setSetteicap2(String setteicap2) {
        this.setteicap2 = setteicap2;
    }

    /**
     * @return setteicap3
     */
    public String getSetteicap3() {
        return setteicap3;
    }

    /**
     * @param setteicap3 
     */
    public void setSetteicap3(String setteicap3) {
        this.setteicap3 = setteicap3;
    }

    /**
     * @return the irhantei1tani_low
     */
    public String getIrhantei1tani_low() {
        return irhantei1tani_low;
    }

    /**
     * @param irhantei1tani_low the irhantei1tani_low to set
     */
    public void setIrhantei1tani_low(String irhantei1tani_low) {
        this.irhantei1tani_low = irhantei1tani_low;
    }

    /**
     * @return the irhantei2tani_low
     */
    public String getIrhantei2tani_low() {
        return irhantei2tani_low;
    }

    /**
     * @param irhantei2tani_low the irhantei2tani_low to set
     */
    public void setIrhantei2tani_low(String irhantei2tani_low) {
        this.irhantei2tani_low = irhantei2tani_low;
    }

    /**
     * @return the irhantei3tani_low
     */
    public String getIrhantei3tani_low() {
        return irhantei3tani_low;
    }

    /**
     * @param irhantei3tani_low the irhantei3tani_low to set
     */
    public void setIrhantei3tani_low(String irhantei3tani_low) {
        this.irhantei3tani_low = irhantei3tani_low;
    }

    /**
     * @return the irhantei4tani_low
     */
    public String getIrhantei4tani_low() {
        return irhantei4tani_low;
    }

    /**
     * @param irhantei4tani_low the irhantei4tani_low to set
     */
    public void setIrhantei4tani_low(String irhantei4tani_low) {
        this.irhantei4tani_low = irhantei4tani_low;
    }

    /**
     * @return the irhantei5tani_low
     */
    public String getIrhantei5tani_low() {
        return irhantei5tani_low;
    }

    /**
     * @param irhantei5tani_low the irhantei5tani_low to set
     */
    public void setIrhantei5tani_low(String irhantei5tani_low) {
        this.irhantei5tani_low = irhantei5tani_low;
    }

    /**
     * @return the irhantei6tani_low
     */
    public String getIrhantei6tani_low() {
        return irhantei6tani_low;
    }

    /**
     * @param irhantei6tani_low the irhantei6tani_low to set
     */
    public void setIrhantei6tani_low(String irhantei6tani_low) {
        this.irhantei6tani_low = irhantei6tani_low;
    }

    /**
     * @return the irhantei7tani_low
     */
    public String getIrhantei7tani_low() {
        return irhantei7tani_low;
    }

    /**
     * @param irhantei7tani_low the irhantei7tani_low to set
     */
    public void setIrhantei7tani_low(String irhantei7tani_low) {
        this.irhantei7tani_low = irhantei7tani_low;
    }

    /**
     * @return the irhantei8tani_low
     */
    public String getIrhantei8tani_low() {
        return irhantei8tani_low;
    }

    /**
     * @param irhantei8tani_low the irhantei8tani_low to set
     */
    public void setIrhantei8tani_low(String irhantei8tani_low) {
        this.irhantei8tani_low = irhantei8tani_low;
    }

    /**
     * @return the denkyokuseisou
     */
    public String getDenkyokuseisou() {
        return denkyokuseisou;
    }

    /**
     * @param denkyokuseisou the denkyokuseisou to set
     */
    public void setDenkyokuseisou(String denkyokuseisou) {
        this.denkyokuseisou = denkyokuseisou;
    }

    /**
     * @return the setteicap4
     */
    public String getSetteicap4() {
        return setteicap4;
    }

    /**
     * @param setteicap4 the setteicap4 to set
     */
    public void setSetteicap4(String setteicap4) {
        this.setteicap4 = setteicap4;
    }

    /**
     * @return the setteiti4low
     */
    public String getSetteiti4low() {
        return setteiti4low;
    }

    /**
     * @param setteiti4low the setteiti4low to set
     */
    public void setSetteiti4low(String setteiti4low) {
        this.setteiti4low = setteiti4low;
    }

    /**
     * @return the setteiti4up
     */
    public String getSetteiti4up() {
        return setteiti4up;
    }

    /**
     * @param setteiti4up the setteiti4up to set
     */
    public void setSetteiti4up(String setteiti4up) {
        this.setteiti4up = setteiti4up;
    }

    /**
     * @return the bin1setteiti
     */
    public String getBin1setteiti() {
        return bin1setteiti;
    }

    /**
     * @param bin1setteiti the bin1setteiti to set
     */
    public void setBin1setteiti(String bin1setteiti) {
        this.bin1setteiti = bin1setteiti;
    }

    /**
     * @return the bin2setteiti
     */
    public String getBin2setteiti() {
        return bin2setteiti;
    }

    /**
     * @param bin2setteiti the bin2setteiti to set
     */
    public void setBin2setteiti(String bin2setteiti) {
        this.bin2setteiti = bin2setteiti;
    }

    /**
     * @return the bin3setteiti
     */
    public String getBin3setteiti() {
        return bin3setteiti;
    }

    /**
     * @param bin3setteiti the bin3setteiti to set
     */
    public void setBin3setteiti(String bin3setteiti) {
        this.bin3setteiti = bin3setteiti;
    }

    /**
     * @return the bin4setteiti
     */
    public String getBin4setteiti() {
        return bin4setteiti;
    }

    /**
     * @param bin4setteiti the bin4setteiti to set
     */
    public void setBin4setteiti(String bin4setteiti) {
        this.bin4setteiti = bin4setteiti;
    }

    /**
     * @return the gentenhukkidousa
     */
    public String getGentenhukkidousa() {
        return gentenhukkidousa;
    }

    /**
     * @param gentenhukkidousa the gentenhukkidousa to set
     */
    public void setGentenhukkidousa(String gentenhukkidousa) {
        this.gentenhukkidousa = gentenhukkidousa;
    }

    /**
     * @return the sokuteiki12dousakakunin
     */
    public String getSokuteiki12dousakakunin() {
        return sokuteiki12dousakakunin;
    }

    /**
     * @param sokuteiki12dousakakunin the sokuteiki12dousakakunin to set
     */
    public void setSokuteiki12dousakakunin(String sokuteiki12dousakakunin) {
        this.sokuteiki12dousakakunin = sokuteiki12dousakakunin;
    }

    /**
     * @return the sokuteipinfront
     */
    public String getSokuteipinfront() {
        return sokuteipinfront;
    }

    /**
     * @param sokuteipinfront the sokuteipinfront to set
     */
    public void setSokuteipinfront(String sokuteipinfront) {
        this.sokuteipinfront = sokuteipinfront;
    }

    /**
     * @return the sokuteipinrear
     */
    public String getSokuteipinrear() {
        return sokuteipinrear;
    }

    /**
     * @param sokuteipinrear the sokuteipinrear to set
     */
    public void setSokuteipinrear(String sokuteipinrear) {
        this.sokuteipinrear = sokuteipinrear;
    }

    /**
     * @return the ir1denryustart
     */
    public String getIr1denryustart() {
        return ir1denryustart;
    }

    /**
     * @param ir1denryustart the ir1denryustart to set
     */
    public void setIr1denryustart(String ir1denryustart) {
        this.ir1denryustart = ir1denryustart;
    }

    /**
     * @return the ir1denryustarttani
     */
    public String getIr1denryustarttani() {
        return ir1denryustarttani;
    }

    /**
     * @param ir1denryustarttani the ir1denryustarttani to set
     */
    public void setIr1denryustarttani(String ir1denryustarttani) {
        this.ir1denryustarttani = ir1denryustarttani;
    }

    /**
     * @return the ir1denryuend
     */
    public String getIr1denryuend() {
        return ir1denryuend;
    }

    /**
     * @param ir1denryuend the ir1denryuend to set
     */
    public void setIr1denryuend(String ir1denryuend) {
        this.ir1denryuend = ir1denryuend;
    }

    /**
     * @return the ir1denryuendtani
     */
    public String getIr1denryuendtani() {
        return ir1denryuendtani;
    }

    /**
     * @param ir1denryuendtani the ir1denryuendtani to set
     */
    public void setIr1denryuendtani(String ir1denryuendtani) {
        this.ir1denryuendtani = ir1denryuendtani;
    }

    /**
     * @return the ir1sokuteihanistart
     */
    public String getIr1sokuteihanistart() {
        return ir1sokuteihanistart;
    }

    /**
     * @param ir1sokuteihanistart the ir1sokuteihanistart to set
     */
    public void setIr1sokuteihanistart(String ir1sokuteihanistart) {
        this.ir1sokuteihanistart = ir1sokuteihanistart;
    }

    /**
     * @return the ir1sokuteihanistarttani
     */
    public String getIr1sokuteihanistarttani() {
        return ir1sokuteihanistarttani;
    }

    /**
     * @param ir1sokuteihanistarttani the ir1sokuteihanistarttani to set
     */
    public void setIr1sokuteihanistarttani(String ir1sokuteihanistarttani) {
        this.ir1sokuteihanistarttani = ir1sokuteihanistarttani;
    }

    /**
     * @return the ir1sokuteihaniend
     */
    public String getIr1sokuteihaniend() {
        return ir1sokuteihaniend;
    }

    /**
     * @param ir1sokuteihaniend the ir1sokuteihaniend to set
     */
    public void setIr1sokuteihaniend(String ir1sokuteihaniend) {
        this.ir1sokuteihaniend = ir1sokuteihaniend;
    }

    /**
     * @return the ir1sokuteihaniendtani
     */
    public String getIr1sokuteihaniendtani() {
        return ir1sokuteihaniendtani;
    }

    /**
     * @param ir1sokuteihaniendtani the ir1sokuteihaniendtani to set
     */
    public void setIr1sokuteihaniendtani(String ir1sokuteihaniendtani) {
        this.ir1sokuteihaniendtani = ir1sokuteihaniendtani;
    }

    /**
     * @return the ir2denryustart
     */
    public String getIr2denryustart() {
        return ir2denryustart;
    }

    /**
     * @param ir2denryustart the ir2denryustart to set
     */
    public void setIr2denryustart(String ir2denryustart) {
        this.ir2denryustart = ir2denryustart;
    }

    /**
     * @return the ir2denryustarttani
     */
    public String getIr2denryustarttani() {
        return ir2denryustarttani;
    }

    /**
     * @param ir2denryustarttani the ir2denryustarttani to set
     */
    public void setIr2denryustarttani(String ir2denryustarttani) {
        this.ir2denryustarttani = ir2denryustarttani;
    }

    /**
     * @return the ir2denryuend
     */
    public String getIr2denryuend() {
        return ir2denryuend;
    }

    /**
     * @param ir2denryuend the ir2denryuend to set
     */
    public void setIr2denryuend(String ir2denryuend) {
        this.ir2denryuend = ir2denryuend;
    }

    /**
     * @return the ir2denryuendtani
     */
    public String getIr2denryuendtani() {
        return ir2denryuendtani;
    }

    /**
     * @param ir2denryuendtani the ir2denryuendtani to set
     */
    public void setIr2denryuendtani(String ir2denryuendtani) {
        this.ir2denryuendtani = ir2denryuendtani;
    }

    /**
     * @return the ir2sokuteihanistart
     */
    public String getIr2sokuteihanistart() {
        return ir2sokuteihanistart;
    }

    /**
     * @param ir2sokuteihanistart the ir2sokuteihanistart to set
     */
    public void setIr2sokuteihanistart(String ir2sokuteihanistart) {
        this.ir2sokuteihanistart = ir2sokuteihanistart;
    }

    /**
     * @return the ir2sokuteihanistarttani
     */
    public String getIr2sokuteihanistarttani() {
        return ir2sokuteihanistarttani;
    }

    /**
     * @param ir2sokuteihanistarttani the ir2sokuteihanistarttani to set
     */
    public void setIr2sokuteihanistarttani(String ir2sokuteihanistarttani) {
        this.ir2sokuteihanistarttani = ir2sokuteihanistarttani;
    }

    /**
     * @return the ir2sokuteihaniend
     */
    public String getIr2sokuteihaniend() {
        return ir2sokuteihaniend;
    }

    /**
     * @param ir2sokuteihaniend the ir2sokuteihaniend to set
     */
    public void setIr2sokuteihaniend(String ir2sokuteihaniend) {
        this.ir2sokuteihaniend = ir2sokuteihaniend;
    }

    /**
     * @return the ir2sokuteihaniendtani
     */
    public String getIr2sokuteihaniendtani() {
        return ir2sokuteihaniendtani;
    }

    /**
     * @param ir2sokuteihaniendtani the ir2sokuteihaniendtani to set
     */
    public void setIr2sokuteihaniendtani(String ir2sokuteihaniendtani) {
        this.ir2sokuteihaniendtani = ir2sokuteihaniendtani;
    }

    /**
     * @return the ir3denryustart
     */
    public String getIr3denryustart() {
        return ir3denryustart;
    }

    /**
     * @param ir3denryustart the ir3denryustart to set
     */
    public void setIr3denryustart(String ir3denryustart) {
        this.ir3denryustart = ir3denryustart;
    }

    /**
     * @return the ir3denryustarttani
     */
    public String getIr3denryustarttani() {
        return ir3denryustarttani;
    }

    /**
     * @param ir3denryustarttani the ir3denryustarttani to set
     */
    public void setIr3denryustarttani(String ir3denryustarttani) {
        this.ir3denryustarttani = ir3denryustarttani;
    }

    /**
     * @return the ir3denryuend
     */
    public String getIr3denryuend() {
        return ir3denryuend;
    }

    /**
     * @param ir3denryuend the ir3denryuend to set
     */
    public void setIr3denryuend(String ir3denryuend) {
        this.ir3denryuend = ir3denryuend;
    }

    /**
     * @return the ir3denryuendtani
     */
    public String getIr3denryuendtani() {
        return ir3denryuendtani;
    }

    /**
     * @param ir3denryuendtani the ir3denryuendtani to set
     */
    public void setIr3denryuendtani(String ir3denryuendtani) {
        this.ir3denryuendtani = ir3denryuendtani;
    }

    /**
     * @return the ir3sokuteihanistart
     */
    public String getIr3sokuteihanistart() {
        return ir3sokuteihanistart;
    }

    /**
     * @param ir3sokuteihanistart the ir3sokuteihanistart to set
     */
    public void setIr3sokuteihanistart(String ir3sokuteihanistart) {
        this.ir3sokuteihanistart = ir3sokuteihanistart;
    }

    /**
     * @return the ir3sokuteihanistarttani
     */
    public String getIr3sokuteihanistarttani() {
        return ir3sokuteihanistarttani;
    }

    /**
     * @param ir3sokuteihanistarttani the ir3sokuteihanistarttani to set
     */
    public void setIr3sokuteihanistarttani(String ir3sokuteihanistarttani) {
        this.ir3sokuteihanistarttani = ir3sokuteihanistarttani;
    }

    /**
     * @return the ir3sokuteihaniend
     */
    public String getIr3sokuteihaniend() {
        return ir3sokuteihaniend;
    }

    /**
     * @param ir3sokuteihaniend the ir3sokuteihaniend to set
     */
    public void setIr3sokuteihaniend(String ir3sokuteihaniend) {
        this.ir3sokuteihaniend = ir3sokuteihaniend;
    }

    /**
     * @return the ir3sokuteihaniendtani
     */
    public String getIr3sokuteihaniendtani() {
        return ir3sokuteihaniendtani;
    }

    /**
     * @param ir3sokuteihaniendtani the ir3sokuteihaniendtani to set
     */
    public void setIr3sokuteihaniendtani(String ir3sokuteihaniendtani) {
        this.ir3sokuteihaniendtani = ir3sokuteihaniendtani;
    }

    /**
     * @return the ir4denryustart
     */
    public String getIr4denryustart() {
        return ir4denryustart;
    }

    /**
     * @param ir4denryustart the ir4denryustart to set
     */
    public void setIr4denryustart(String ir4denryustart) {
        this.ir4denryustart = ir4denryustart;
    }

    /**
     * @return the ir4denryustarttani
     */
    public String getIr4denryustarttani() {
        return ir4denryustarttani;
    }

    /**
     * @param ir4denryustarttani the ir4denryustarttani to set
     */
    public void setIr4denryustarttani(String ir4denryustarttani) {
        this.ir4denryustarttani = ir4denryustarttani;
    }

    /**
     * @return the ir4denryuend
     */
    public String getIr4denryuend() {
        return ir4denryuend;
    }

    /**
     * @param ir4denryuend the ir4denryuend to set
     */
    public void setIr4denryuend(String ir4denryuend) {
        this.ir4denryuend = ir4denryuend;
    }

    /**
     * @return the ir4denryuendtani
     */
    public String getIr4denryuendtani() {
        return ir4denryuendtani;
    }

    /**
     * @param ir4denryuendtani the ir4denryuendtani to set
     */
    public void setIr4denryuendtani(String ir4denryuendtani) {
        this.ir4denryuendtani = ir4denryuendtani;
    }

    /**
     * @return the ir4sokuteihanistart
     */
    public String getIr4sokuteihanistart() {
        return ir4sokuteihanistart;
    }

    /**
     * @param ir4sokuteihanistart the ir4sokuteihanistart to set
     */
    public void setIr4sokuteihanistart(String ir4sokuteihanistart) {
        this.ir4sokuteihanistart = ir4sokuteihanistart;
    }

    /**
     * @return the ir4sokuteihanistarttani
     */
    public String getIr4sokuteihanistarttani() {
        return ir4sokuteihanistarttani;
    }

    /**
     * @param ir4sokuteihanistarttani the ir4sokuteihanistarttani to set
     */
    public void setIr4sokuteihanistarttani(String ir4sokuteihanistarttani) {
        this.ir4sokuteihanistarttani = ir4sokuteihanistarttani;
    }

    /**
     * @return the ir4sokuteihaniend
     */
    public String getIr4sokuteihaniend() {
        return ir4sokuteihaniend;
    }

    /**
     * @param ir4sokuteihaniend the ir4sokuteihaniend to set
     */
    public void setIr4sokuteihaniend(String ir4sokuteihaniend) {
        this.ir4sokuteihaniend = ir4sokuteihaniend;
    }

    /**
     * @return the ir4sokuteihaniendtani
     */
    public String getIr4sokuteihaniendtani() {
        return ir4sokuteihaniendtani;
    }

    /**
     * @param ir4sokuteihaniendtani the ir4sokuteihaniendtani to set
     */
    public void setIr4sokuteihaniendtani(String ir4sokuteihaniendtani) {
        this.ir4sokuteihaniendtani = ir4sokuteihaniendtani;
    }

    /**
     * @return the ir5denryustart
     */
    public String getIr5denryustart() {
        return ir5denryustart;
    }

    /**
     * @param ir5denryustart the ir5denryustart to set
     */
    public void setIr5denryustart(String ir5denryustart) {
        this.ir5denryustart = ir5denryustart;
    }

    /**
     * @return the ir5denryustarttani
     */
    public String getIr5denryustarttani() {
        return ir5denryustarttani;
    }

    /**
     * @param ir5denryustarttani the ir5denryustarttani to set
     */
    public void setIr5denryustarttani(String ir5denryustarttani) {
        this.ir5denryustarttani = ir5denryustarttani;
    }

    /**
     * @return the ir5denryuend
     */
    public String getIr5denryuend() {
        return ir5denryuend;
    }

    /**
     * @param ir5denryuend the ir5denryuend to set
     */
    public void setIr5denryuend(String ir5denryuend) {
        this.ir5denryuend = ir5denryuend;
    }

    /**
     * @return the ir5denryuendtani
     */
    public String getIr5denryuendtani() {
        return ir5denryuendtani;
    }

    /**
     * @param ir5denryuendtani the ir5denryuendtani to set
     */
    public void setIr5denryuendtani(String ir5denryuendtani) {
        this.ir5denryuendtani = ir5denryuendtani;
    }

    /**
     * @return the ir5sokuteihanistart
     */
    public String getIr5sokuteihanistart() {
        return ir5sokuteihanistart;
    }

    /**
     * @param ir5sokuteihanistart the ir5sokuteihanistart to set
     */
    public void setIr5sokuteihanistart(String ir5sokuteihanistart) {
        this.ir5sokuteihanistart = ir5sokuteihanistart;
    }

    /**
     * @return the ir5sokuteihanistarttani
     */
    public String getIr5sokuteihanistarttani() {
        return ir5sokuteihanistarttani;
    }

    /**
     * @param ir5sokuteihanistarttani the ir5sokuteihanistarttani to set
     */
    public void setIr5sokuteihanistarttani(String ir5sokuteihanistarttani) {
        this.ir5sokuteihanistarttani = ir5sokuteihanistarttani;
    }

    /**
     * @return the ir5sokuteihaniend
     */
    public String getIr5sokuteihaniend() {
        return ir5sokuteihaniend;
    }

    /**
     * @param ir5sokuteihaniend the ir5sokuteihaniend to set
     */
    public void setIr5sokuteihaniend(String ir5sokuteihaniend) {
        this.ir5sokuteihaniend = ir5sokuteihaniend;
    }

    /**
     * @return the ir5sokuteihaniendtani
     */
    public String getIr5sokuteihaniendtani() {
        return ir5sokuteihaniendtani;
    }

    /**
     * @param ir5sokuteihaniendtani the ir5sokuteihaniendtani to set
     */
    public void setIr5sokuteihaniendtani(String ir5sokuteihaniendtani) {
        this.ir5sokuteihaniendtani = ir5sokuteihaniendtani;
    }

    /**
     * @return the ir6denryustart
     */
    public String getIr6denryustart() {
        return ir6denryustart;
    }

    /**
     * @param ir6denryustart the ir6denryustart to set
     */
    public void setIr6denryustart(String ir6denryustart) {
        this.ir6denryustart = ir6denryustart;
    }

    /**
     * @return the ir6denryustarttani
     */
    public String getIr6denryustarttani() {
        return ir6denryustarttani;
    }

    /**
     * @param ir6denryustarttani the ir6denryustarttani to set
     */
    public void setIr6denryustarttani(String ir6denryustarttani) {
        this.ir6denryustarttani = ir6denryustarttani;
    }

    /**
     * @return the ir6denryuend
     */
    public String getIr6denryuend() {
        return ir6denryuend;
    }

    /**
     * @param ir6denryuend the ir6denryuend to set
     */
    public void setIr6denryuend(String ir6denryuend) {
        this.ir6denryuend = ir6denryuend;
    }

    /**
     * @return the ir6denryuendtani
     */
    public String getIr6denryuendtani() {
        return ir6denryuendtani;
    }

    /**
     * @param ir6denryuendtani the ir6denryuendtani to set
     */
    public void setIr6denryuendtani(String ir6denryuendtani) {
        this.ir6denryuendtani = ir6denryuendtani;
    }

    /**
     * @return the ir6sokuteihanistart
     */
    public String getIr6sokuteihanistart() {
        return ir6sokuteihanistart;
    }

    /**
     * @param ir6sokuteihanistart the ir6sokuteihanistart to set
     */
    public void setIr6sokuteihanistart(String ir6sokuteihanistart) {
        this.ir6sokuteihanistart = ir6sokuteihanistart;
    }

    /**
     * @return the ir6sokuteihanistarttani
     */
    public String getIr6sokuteihanistarttani() {
        return ir6sokuteihanistarttani;
    }

    /**
     * @param ir6sokuteihanistarttani the ir6sokuteihanistarttani to set
     */
    public void setIr6sokuteihanistarttani(String ir6sokuteihanistarttani) {
        this.ir6sokuteihanistarttani = ir6sokuteihanistarttani;
    }

    /**
     * @return the ir6sokuteihaniend
     */
    public String getIr6sokuteihaniend() {
        return ir6sokuteihaniend;
    }

    /**
     * @param ir6sokuteihaniend the ir6sokuteihaniend to set
     */
    public void setIr6sokuteihaniend(String ir6sokuteihaniend) {
        this.ir6sokuteihaniend = ir6sokuteihaniend;
    }

    /**
     * @return the ir6sokuteihaniendtani
     */
    public String getIr6sokuteihaniendtani() {
        return ir6sokuteihaniendtani;
    }

    /**
     * @param ir6sokuteihaniendtani the ir6sokuteihaniendtani to set
     */
    public void setIr6sokuteihaniendtani(String ir6sokuteihaniendtani) {
        this.ir6sokuteihaniendtani = ir6sokuteihaniendtani;
    }

    /**
     * @return the ir7denryustart
     */
    public String getIr7denryustart() {
        return ir7denryustart;
    }

    /**
     * @param ir7denryustart the ir7denryustart to set
     */
    public void setIr7denryustart(String ir7denryustart) {
        this.ir7denryustart = ir7denryustart;
    }

    /**
     * @return the ir7denryustarttani
     */
    public String getIr7denryustarttani() {
        return ir7denryustarttani;
    }

    /**
     * @param ir7denryustarttani the ir7denryustarttani to set
     */
    public void setIr7denryustarttani(String ir7denryustarttani) {
        this.ir7denryustarttani = ir7denryustarttani;
    }

    /**
     * @return the ir7denryuend
     */
    public String getIr7denryuend() {
        return ir7denryuend;
    }

    /**
     * @param ir7denryuend the ir7denryuend to set
     */
    public void setIr7denryuend(String ir7denryuend) {
        this.ir7denryuend = ir7denryuend;
    }

    /**
     * @return the ir7denryuendtani
     */
    public String getIr7denryuendtani() {
        return ir7denryuendtani;
    }

    /**
     * @param ir7denryuendtani the ir7denryuendtani to set
     */
    public void setIr7denryuendtani(String ir7denryuendtani) {
        this.ir7denryuendtani = ir7denryuendtani;
    }

    /**
     * @return the ir7sokuteihanistart
     */
    public String getIr7sokuteihanistart() {
        return ir7sokuteihanistart;
    }

    /**
     * @param ir7sokuteihanistart the ir7sokuteihanistart to set
     */
    public void setIr7sokuteihanistart(String ir7sokuteihanistart) {
        this.ir7sokuteihanistart = ir7sokuteihanistart;
    }

    /**
     * @return the ir7sokuteihanistarttani
     */
    public String getIr7sokuteihanistarttani() {
        return ir7sokuteihanistarttani;
    }

    /**
     * @param ir7sokuteihanistarttani the ir7sokuteihanistarttani to set
     */
    public void setIr7sokuteihanistarttani(String ir7sokuteihanistarttani) {
        this.ir7sokuteihanistarttani = ir7sokuteihanistarttani;
    }

    /**
     * @return the ir7sokuteihaniend
     */
    public String getIr7sokuteihaniend() {
        return ir7sokuteihaniend;
    }

    /**
     * @param ir7sokuteihaniend the ir7sokuteihaniend to set
     */
    public void setIr7sokuteihaniend(String ir7sokuteihaniend) {
        this.ir7sokuteihaniend = ir7sokuteihaniend;
    }

    /**
     * @return the ir7sokuteihaniendtani
     */
    public String getIr7sokuteihaniendtani() {
        return ir7sokuteihaniendtani;
    }

    /**
     * @param ir7sokuteihaniendtani the ir7sokuteihaniendtani to set
     */
    public void setIr7sokuteihaniendtani(String ir7sokuteihaniendtani) {
        this.ir7sokuteihaniendtani = ir7sokuteihaniendtani;
    }

    /**
     * @return the ir8denryustart
     */
    public String getIr8denryustart() {
        return ir8denryustart;
    }

    /**
     * @param ir8denryustart the ir8denryustart to set
     */
    public void setIr8denryustart(String ir8denryustart) {
        this.ir8denryustart = ir8denryustart;
    }

    /**
     * @return the ir8denryustarttani
     */
    public String getIr8denryustarttani() {
        return ir8denryustarttani;
    }

    /**
     * @param ir8denryustarttani the ir8denryustarttani to set
     */
    public void setIr8denryustarttani(String ir8denryustarttani) {
        this.ir8denryustarttani = ir8denryustarttani;
    }

    /**
     * @return the ir8denryuend
     */
    public String getIr8denryuend() {
        return ir8denryuend;
    }

    /**
     * @param ir8denryuend the ir8denryuend to set
     */
    public void setIr8denryuend(String ir8denryuend) {
        this.ir8denryuend = ir8denryuend;
    }

    /**
     * @return the ir8denryuendtani
     */
    public String getIr8denryuendtani() {
        return ir8denryuendtani;
    }

    /**
     * @param ir8denryuendtani the ir8denryuendtani to set
     */
    public void setIr8denryuendtani(String ir8denryuendtani) {
        this.ir8denryuendtani = ir8denryuendtani;
    }

    /**
     * @return the ir8sokuteihanistart
     */
    public String getIr8sokuteihanistart() {
        return ir8sokuteihanistart;
    }

    /**
     * @param ir8sokuteihanistart the ir8sokuteihanistart to set
     */
    public void setIr8sokuteihanistart(String ir8sokuteihanistart) {
        this.ir8sokuteihanistart = ir8sokuteihanistart;
    }

    /**
     * @return the ir8sokuteihanistarttani
     */
    public String getIr8sokuteihanistarttani() {
        return ir8sokuteihanistarttani;
    }

    /**
     * @param ir8sokuteihanistarttani the ir8sokuteihanistarttani to set
     */
    public void setIr8sokuteihanistarttani(String ir8sokuteihanistarttani) {
        this.ir8sokuteihanistarttani = ir8sokuteihanistarttani;
    }

    /**
     * @return the ir8sokuteihaniend
     */
    public String getIr8sokuteihaniend() {
        return ir8sokuteihaniend;
    }

    /**
     * @param ir8sokuteihaniend the ir8sokuteihaniend to set
     */
    public void setIr8sokuteihaniend(String ir8sokuteihaniend) {
        this.ir8sokuteihaniend = ir8sokuteihaniend;
    }

    /**
     * @return the ir8sokuteihaniendtani
     */
    public String getIr8sokuteihaniendtani() {
        return ir8sokuteihaniendtani;
    }

    /**
     * @param ir8sokuteihaniendtani the ir8sokuteihaniendtani to set
     */
    public void setIr8sokuteihaniendtani(String ir8sokuteihaniendtani) {
        this.ir8sokuteihaniendtani = ir8sokuteihaniendtani;
    }

    /**
     * @return the senbetukaisinitijitwa
     */
    public String getSenbetukaisinitijitwa() {
        return senbetukaisinitijitwa;
    }

    /**
     * @param senbetukaisinitijitwa the senbetukaisinitijitwa to set
     */
    public void setSenbetukaisinitijitwa(String senbetukaisinitijitwa) {
        this.senbetukaisinitijitwa = senbetukaisinitijitwa;
    }

    /**
     * @return the senbetusyuryounitijitwa
     */
    public String getSenbetusyuryounitijitwa() {
        return senbetusyuryounitijitwa;
    }

    /**
     * @param senbetusyuryounitijitwa the senbetusyuryounitijitwa to set
     */
    public void setSenbetusyuryounitijitwa(String senbetusyuryounitijitwa) {
        this.senbetusyuryounitijitwa = senbetusyuryounitijitwa;
    }

    /**
     * @return the satsample
     */
    public String getSatsample() {
        return satsample;
    }

    /**
     * @param satsample the satsample to set
     */
    public void setSatsample(String satsample) {
        this.satsample = satsample;
    }

    /**
     * @return the binboxseisoucheck
     */
    public String getBinboxseisoucheck() {
        return binboxseisoucheck;
    }

    /**
     * @param binboxseisoucheck the binboxseisoucheck to set
     */
    public void setBinboxseisoucheck(String binboxseisoucheck) {
        this.binboxseisoucheck = binboxseisoucheck;
    }

    /**
     * @return the bin1setteititwa
     */
    public String getBin1setteititwa() {
        return bin1setteititwa;
    }

    /**
     * @param bin1setteititwa the bin1setteititwa to set
     */
    public void setBin1setteititwa(String bin1setteititwa) {
        this.bin1setteititwa = bin1setteititwa;
    }

    /**
     * @return the bin2setteititwa
     */
    public String getBin2setteititwa() {
        return bin2setteititwa;
    }

    /**
     * @param bin2setteititwa the bin2setteititwa to set
     */
    public void setBin2setteititwa(String bin2setteititwa) {
        this.bin2setteititwa = bin2setteititwa;
    }

    /**
     * @return the bin3setteititwa
     */
    public String getBin3setteititwa() {
        return bin3setteititwa;
    }

    /**
     * @param bin3setteititwa the bin3setteititwa to set
     */
    public void setBin3setteititwa(String bin3setteititwa) {
        this.bin3setteititwa = bin3setteititwa;
    }

    /**
     * @return the bin4setteititwa
     */
    public String getBin4setteititwa() {
        return bin4setteititwa;
    }

    /**
     * @param bin4setteititwa the bin4setteititwa to set
     */
    public void setBin4setteititwa(String bin4setteititwa) {
        this.bin4setteititwa = bin4setteititwa;
    }

    /**
     * @return the bin5setteititwa
     */
    public String getBin5setteititwa() {
        return bin5setteititwa;
    }

    /**
     * @param bin5setteititwa the bin5setteititwa to set
     */
    public void setBin5setteititwa(String bin5setteititwa) {
        this.bin5setteititwa = bin5setteititwa;
    }

    /**
     * @return the bin6setteititwa
     */
    public String getBin6setteititwa() {
        return bin6setteititwa;
    }

    /**
     * @param bin6setteititwa the bin6setteititwa to set
     */
    public void setBin6setteititwa(String bin6setteititwa) {
        this.bin6setteititwa = bin6setteititwa;
    }

    /**
     * @return the bin7setteititwa
     */
    public String getBin7setteititwa() {
        return bin7setteititwa;
    }

    /**
     * @param bin7setteititwa the bin7setteititwa to set
     */
    public void setBin7setteititwa(String bin7setteititwa) {
        this.bin7setteititwa = bin7setteititwa;
    }

    /**
     * @return the bin8setteititwa
     */
    public String getBin8setteititwa() {
        return bin8setteititwa;
    }

    /**
     * @param bin8setteititwa the bin8setteititwa to set
     */
    public void setBin8setteititwa(String bin8setteititwa) {
        this.bin8setteititwa = bin8setteititwa;
    }

    /**
     * @return the hoseiyoutippuyoryou
     */
    public String getHoseiyoutippuyoryou() {
        return hoseiyoutippuyoryou;
    }

    /**
     * @param hoseiyoutippuyoryou the hoseiyoutippuyoryou to set
     */
    public void setHoseiyoutippuyoryou(String hoseiyoutippuyoryou) {
        this.hoseiyoutippuyoryou = hoseiyoutippuyoryou;
    }

    /**
     * @return the hoseiyoutipputan
     */
    public String getHoseiyoutipputan() {
        return hoseiyoutipputan;
    }

    /**
     * @param hoseiyoutipputan the hoseiyoutipputan to set
     */
    public void setHoseiyoutipputan(String hoseiyoutipputan) {
        this.hoseiyoutipputan = hoseiyoutipputan;
    }

    /**
     * @return the ir1jikan
     */
    public String getIr1jikan() {
        return ir1jikan;
    }

    /**
     * @param ir1jikan the ir1jikan to set
     */
    public void setIr1jikan(String ir1jikan) {
        this.ir1jikan = ir1jikan;
    }

    /**
     * @return the ir1jikantani
     */
    public String getIr1jikantani() {
        return ir1jikantani;
    }

    /**
     * @param ir1jikantani the ir1jikantani to set
     */
    public void setIr1jikantani(String ir1jikantani) {
        this.ir1jikantani = ir1jikantani;
    }

    /**
     * @return the ir2jikan
     */
    public String getIr2jikan() {
        return ir2jikan;
    }

    /**
     * @param ir2jikan the ir2jikan to set
     */
    public void setIr2jikan(String ir2jikan) {
        this.ir2jikan = ir2jikan;
    }

    /**
     * @return the ir2jikantani
     */
    public String getIr2jikantani() {
        return ir2jikantani;
    }

    /**
     * @param ir2jikantani the ir2jikantani to set
     */
    public void setIr2jikantani(String ir2jikantani) {
        this.ir2jikantani = ir2jikantani;
    }

    /**
     * @return the ir3jikan
     */
    public String getIr3jikan() {
        return ir3jikan;
    }

    /**
     * @param ir3jikan the ir3jikan to set
     */
    public void setIr3jikan(String ir3jikan) {
        this.ir3jikan = ir3jikan;
    }

    /**
     * @return the ir3jikantani
     */
    public String getIr3jikantani() {
        return ir3jikantani;
    }

    /**
     * @param ir3jikantani the ir3jikantani to set
     */
    public void setIr3jikantani(String ir3jikantani) {
        this.ir3jikantani = ir3jikantani;
    }

    /**
     * @return the ir4jikan
     */
    public String getIr4jikan() {
        return ir4jikan;
    }

    /**
     * @param ir4jikan the ir4jikan to set
     */
    public void setIr4jikan(String ir4jikan) {
        this.ir4jikan = ir4jikan;
    }

    /**
     * @return the ir4jikantani
     */
    public String getIr4jikantani() {
        return ir4jikantani;
    }

    /**
     * @param ir4jikantani the ir4jikantani to set
     */
    public void setIr4jikantani(String ir4jikantani) {
        this.ir4jikantani = ir4jikantani;
    }

    /**
     * @return the ir5jikan
     */
    public String getIr5jikan() {
        return ir5jikan;
    }

    /**
     * @param ir5jikan the ir5jikan to set
     */
    public void setIr5jikan(String ir5jikan) {
        this.ir5jikan = ir5jikan;
    }

    /**
     * @return the ir5jikantani
     */
    public String getIr5jikantani() {
        return ir5jikantani;
    }

    /**
     * @param ir5jikantani the ir5jikantani to set
     */
    public void setIr5jikantani(String ir5jikantani) {
        this.ir5jikantani = ir5jikantani;
    }

    /**
     * @return the ir6jikan
     */
    public String getIr6jikan() {
        return ir6jikan;
    }

    /**
     * @param ir6jikan the ir6jikan to set
     */
    public void setIr6jikan(String ir6jikan) {
        this.ir6jikan = ir6jikan;
    }

    /**
     * @return the ir6jikantani
     */
    public String getIr6jikantani() {
        return ir6jikantani;
    }

    /**
     * @param ir6jikantani the ir6jikantani to set
     */
    public void setIr6jikantani(String ir6jikantani) {
        this.ir6jikantani = ir6jikantani;
    }

    /**
     * @return the ir7jikan
     */
    public String getIr7jikan() {
        return ir7jikan;
    }

    /**
     * @param ir7jikan the ir7jikan to set
     */
    public void setIr7jikan(String ir7jikan) {
        this.ir7jikan = ir7jikan;
    }

    /**
     * @return the ir7jikantani
     */
    public String getIr7jikantani() {
        return ir7jikantani;
    }

    /**
     * @param ir7jikantani the ir7jikantani to set
     */
    public void setIr7jikantani(String ir7jikantani) {
        this.ir7jikantani = ir7jikantani;
    }

    /**
     * @return the ir8jikan
     */
    public String getIr8jikan() {
        return ir8jikan;
    }

    /**
     * @param ir8jikan the ir8jikan to set
     */
    public void setIr8jikan(String ir8jikan) {
        this.ir8jikan = ir8jikan;
    }

    /**
     * @return the ir8jikantani
     */
    public String getIr8jikantani() {
        return ir8jikantani;
    }

    /**
     * @param ir8jikantani the ir8jikantani to set
     */
    public void setIr8jikantani(String ir8jikantani) {
        this.ir8jikantani = ir8jikantani;
    }
}
