package jp.co.kccs.xhd.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 外観検査・熱処理履歴検索画面のモデルクラス
 */
public class GXHDO201B100Model implements Serializable{

//<editor-fold defaultstate="collapsed" desc="#getter setter">
    /**
     * @return the kotei_name
     */
    public String getKotei_name() {
        return kotei_name;
    }

    /**
     * @param kotei_name the kotei_name to set
     */
    public void setKotei_name(String kotei_name) {
        this.kotei_name = kotei_name;
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
     * @return the bikou1
     */
    public String getBikou1() {
        return bikou1;
    }
    
    /**
     * @param bikou1 the bikou1 to set
     */
    public void setBikou1(String bikou1) {
        this.bikou1 = bikou1;
    }
    
    /**
     * @return the bikou3
     */
    public String getBikou3() {
        return bikou3;
    }
    
    /**
     * @param bikou3 the bikou3 to set
     */
    public void setBikou3(String bikou3) {
        this.bikou3 = bikou3;
    }
    
    /**
     * @return the kotei_code
     */
    public String getKotei_code() {
        return kotei_code;
    }
    
    /**
     * @param kotei_code the kotei_code to set
     */
    public void setKotei_code(String kotei_code) {
        this.kotei_code = kotei_code;
    }
    
    /**
     * @return the suuryo
     */
    public Integer getSuuryo() {
        return suuryo;
    }
    
    /**
     * @param suuryo the suuryo to set
     */
    public void setSuuryo(Integer suuryo) {
        this.suuryo = suuryo;
    }
    
    /**
     * @return the opencloseflag
     */
    public String getOpencloseflag() {
        return opencloseflag;
    }
    
    /**
     * @param opencloseflag the opencloseflag to set
     */
    public void setOpencloseflag(String opencloseflag) {
        this.opencloseflag = opencloseflag;
    }
    
    /**
     * @return the to_gaikan_okurisuu
     */
    public Long getTo_gaikan_okurisuu() {
        return to_gaikan_okurisuu;
    }
    
    /**
     * @param to_gaikan_okurisuu the to_gaikan_okurisuu to set
     */
    public void setTo_gaikan_okurisuu(Long to_gaikan_okurisuu) {
        this.to_gaikan_okurisuu = to_gaikan_okurisuu;
    }
    
    /**
     * @return the qagaikan_nichiji
     */
    public Timestamp getQagaikan_nichiji() {
        return qagaikan_nichiji;
    }
    
    /**
     * @param qagaikan_nichiji the qagaikan_nichiji to set
     */
    public void setQagaikan_nichiji(Timestamp qagaikan_nichiji) {
        this.qagaikan_nichiji = qagaikan_nichiji;
    }
    
    /**
     * @return the qagaikan_kensahinsyurui
     */
    public String getQagaikan_kensahinsyurui() {
        return qagaikan_kensahinsyurui;
    }
    
    /**
     * @param qagaikan_kensahinsyurui the qagaikan_kensahinsyurui to set
     */
    public void setQagaikan_kensahinsyurui(String qagaikan_kensahinsyurui) {
        this.qagaikan_kensahinsyurui = qagaikan_kensahinsyurui;
    }
    
    /**
     * @return the qagaikan_hantei
     */
    public String getQagaikan_hantei() {
        return qagaikan_hantei;
    }
    
    /**
     * @param qagaikan_hantei the qagaikan_hantei to set
     */
    public void setQagaikan_hantei(String qagaikan_hantei) {
        this.qagaikan_hantei = qagaikan_hantei;
    }
    
    /**
     * @return the qagaikan_kensasya_code
     */
    public String getQagaikan_kensasya_code() {
        return qagaikan_kensasya_code;
    }
    
    /**
     * @param qagaikan_kensasya_code the qagaikan_kensasya_code to set
     */
    public void setQagaikan_kensasya_code(String qagaikan_kensasya_code) {
        this.qagaikan_kensasya_code = qagaikan_kensasya_code;
    }
    
    /**
     * @return the qagaikan_kensasya_name
     */
    public String getQagaikan_kensasya_name() {
        return qagaikan_kensasya_name;
    }
    
    /**
     * @param qagaikan_kensasya_name the qagaikan_kensasya_name to set
     */
    public void setQagaikan_kensasya_name(String qagaikan_kensasya_name) {
        this.qagaikan_kensasya_name = qagaikan_kensasya_name;
    }
    
    /**
     * @return the gaikan_kaishinichiji
     */
    public Timestamp getGaikan_kaishinichiji() {
        return gaikan_kaishinichiji;
    }
    
    /**
     * @param gaikan_kaishinichiji the gaikan_kaishinichiji to set
     */
    public void setGaikan_kaishinichiji(Timestamp gaikan_kaishinichiji) {
        this.gaikan_kaishinichiji = gaikan_kaishinichiji;
    }
    
    /**
     * @return the gaikan_syuuryounichiji
     */
    public Timestamp getGaikan_syuuryounichiji() {
        return gaikan_syuuryounichiji;
    }
    
    /**
     * @param gaikan_syuuryounichiji the gaikan_syuuryounichiji to set
     */
    public void setGaikan_syuuryounichiji(Timestamp gaikan_syuuryounichiji) {
        this.gaikan_syuuryounichiji = gaikan_syuuryounichiji;
    }
    
    /**
     * @return the gaikan_kensamen
     */
    public String getGaikan_kensamen() {
        return gaikan_kensamen;
    }
    
    /**
     * @param gaikan_kensamen the gaikan_kensamen to set
     */
    public void setGaikan_kensamen(String gaikan_kensamen) {
        this.gaikan_kensamen = gaikan_kensamen;
    }
    
    /**
     * @return the gaikan_kaisuu
     */
    public Integer getGaikan_kaisuu() {
        return gaikan_kaisuu;
    }
    
    /**
     * @param gaikan_kaisuu the gaikan_kaisuu to set
     */
    public void setGaikan_kaisuu(Integer gaikan_kaisuu) {
        this.gaikan_kaisuu = gaikan_kaisuu;
    }
    
    /**
     * @return the gaikan_kensagouki
     */
    public String getGaikan_kensagouki() {
        return gaikan_kensagouki;
    }
    
    /**
     * @param gaikan_kensagouki the gaikan_kensagouki to set
     */
    public void setGaikan_kensagouki(String gaikan_kensagouki) {
        this.gaikan_kensagouki = gaikan_kensagouki;
    }
    
    /**
     * @return the gaikan_okuriryouhinsuu
     */
    public Integer getGaikan_okuriryouhinsuu() {
        return gaikan_okuriryouhinsuu;
    }
    
    /**
     * @param gaikan_okuriryouhinsuu the gaikan_okuriryouhinsuu to set
     */
    public void setGaikan_okuriryouhinsuu(Integer gaikan_okuriryouhinsuu) {
        this.gaikan_okuriryouhinsuu = gaikan_okuriryouhinsuu;
    }
    
    /**
     * @return the gaikan_goukeiryouhinkosuu
     */
    public Integer getGaikan_goukeiryouhinkosuu() {
        return gaikan_goukeiryouhinkosuu;
    }
    
    /**
     * @param gaikan_goukeiryouhinkosuu the gaikan_goukeiryouhinkosuu to set
     */
    public void setGaikan_goukeiryouhinkosuu(Integer gaikan_goukeiryouhinkosuu) {
        this.gaikan_goukeiryouhinkosuu = gaikan_goukeiryouhinkosuu;
    }
    
    /**
     * @return the netsusyori_syoribi
     */
    public Timestamp getNetsusyori_syoribi() {
        return netsusyori_syoribi;
    }
    
    /**
     * @param netsusyori_syoribi the netsusyori_syoribi to set
     */
    public void setNetsusyori_syoribi(Timestamp netsusyori_syoribi) {
        this.netsusyori_syoribi = netsusyori_syoribi;
    }
    
    /**
     * @return the netsusyori_syuuryoujikan
     */
    public String getNetsusyori_syuuryoujikan() {
        return netsusyori_syuuryoujikan;
    }
    
    /**
     * @param netsusyori_syuuryoujikan the netsusyori_syuuryoujikan to set
     */
    public void setNetsusyori_syuuryoujikan(String netsusyori_syuuryoujikan) {
        this.netsusyori_syuuryoujikan = netsusyori_syuuryoujikan;
    }
//</editor-fold>

    private String lotno;
    private String kcpno;
    private String tokuisaki;
    private String bikou1;
    private String bikou3;
    private String kotei_code;
    private String kotei_name;
    private Integer suuryo;
    private String opencloseflag;
    private Long to_gaikan_okurisuu;
    private Timestamp qagaikan_nichiji;
    private String qagaikan_kensahinsyurui;
    private String qagaikan_hantei;
    private String qagaikan_kensasya_code;
    private String qagaikan_kensasya_name;
    private Timestamp gaikan_kaishinichiji;
    private Timestamp gaikan_syuuryounichiji;
    private String gaikan_kensamen;
    private Integer gaikan_kaisuu;
    private String gaikan_kensagouki;
    private Integer gaikan_okuriryouhinsuu;
    private Integer gaikan_goukeiryouhinkosuu;
    private Timestamp netsusyori_syoribi;
    private String netsusyori_syuuryoujikan;
}
