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
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/01/27<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCCS D.Yanagida<br>
 * 変更理由	新規作成<br>
 * <br>
 * 変更日        2019/9/18<br>
 * 計画書No      K1811-DS001<br>
 * 変更者        KCSS K.Jo<br>
 * 変更理由      項目追加・変更<br>
 * <br>
 * 変更日        2022/03/09<br>
 * 計画書No      MB2202-D013<br>
 * 変更者        KCSS K.Jo<br>
 * 変更理由      項目追加・変更<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 印刷・RSUS履歴検索画面のモデルクラスです。
 *
 * @author KCCS D.Yanagida
 * @since 2019/01/27
 */
public class GXHDO201B003Model implements Serializable {

    /** ﾛｯﾄNo. */
    private String lotno = "";
    /** KCPNO */
    private String kcpno = "";
    /** 客先 */
    private String tokuisaki = "";
    /** ﾛｯﾄ区分 */
    private String lotkubuncode = "";
    /** ｵｰﾅｰ */
    private String ownercode = "";
    /** 号機 */
    private String goki = "";
    /** 原料 */
    private String genryou = "";
    /** 電極厚み */
    private Double eatumi = null;
    /** 成形長さ */
    private Long seikeinagasa = null;
    /** PETﾌｨﾙﾑ種類 */
    private String petfilmsyurui = "";
    /** ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo */
    private String tapelotno = "";
    /** 印刷ﾛｰﾙNo1 */
    private String insaturollno = "";
    /** 原料記号 */
    private String genryokigo = "";
    /** 電極ﾍﾟｰｽﾄ */
    private String epaste = "";
    /** ﾍﾟｰｽﾄﾛｯﾄNo */
    private String pastelotno = "";
    /** ﾍﾟｰｽﾄ粘度 */
    private BigDecimal pastenendo = null;
    /** ﾍﾟｰｽﾄ温度 */
    private BigDecimal pasteondo = null;
    /** ﾍﾟｰｽﾄ固形分1 */
    private BigDecimal pkokeibun1 = null;
    /** ﾍﾟｰｽﾄﾛｯﾄNo2 */
    private String pastelotno2 = "";
    /** ﾍﾟｰｽﾄ粘度2 */
    private BigDecimal pastenendo2 = null;
    /** ﾍﾟｰｽﾄ温度2 */
    private BigDecimal pasteondo2 = null;
    /** ﾍﾟｰｽﾄ固形分2 */
    private BigDecimal pkokeibun2 = null;
    /** 製版名 */
    private String seihanmei = "";
    /** 電極製版仕様 */
    private String pattern = "";
    /** 製版No */
    private String seihanno = "";
    /** 製版枚数 */
    private Long seihanmaisuu = null;
    /** 最大処理数 */
    private Long saidaisyorisuu = null;
    /** 累計処理数 */
    private Long ruikeisyorisuu = null;
    /** ｽｷｰｼﾞNo */
    private String skeegeno = "";
    /** ｽｷｰｼﾞ枚数 */
    private Long skeegemaisuu = null;
    /** ｽｷｰｼﾞｽﾋﾟｰﾄﾞ */
    private Long skeegespeed = null;
    /** ｽｸﾚｯﾊﾟｰ速度 */
    private Long scraperspeed = null;
    /** 乾燥温度 */
    private BigDecimal kansoondo = null;
    /** 乾燥温度表示値2 */
    private BigDecimal kansoondo2 = null;
    /** 乾燥温度表示値3 */
    private BigDecimal kansoondo3 = null;
    /** 乾燥温度表示値4 */
    private BigDecimal kansoondo4 = null;
    /** 乾燥温度表示値5 */
    private BigDecimal kansoondo5 = null;
    /** 差圧 */
    private BigDecimal saatu = null;
    /** ﾃｰﾌﾞﾙｸﾘｱﾗﾝｽ */
    private BigDecimal tableClearrance = null;
    /** 乾燥炉圧 */
    private Long kansouroatsu = null;
    /** MLD */
    private Long mld = null;
    /** 印刷幅 */
    private Long printhaba = null;
    /** 印刷ｽﾀｰﾄ膜厚AVE */
    private BigDecimal makuatsuAveStart = null;
    /** 印刷ｽﾀｰﾄ膜厚MAX */
    private BigDecimal makuatsuMaxStart = null;
    /** 印刷ｽﾀｰﾄ膜厚MIN */
    private BigDecimal makuatsuMinStart = null;
    /** 印刷ｽﾀｰﾄ膜厚CV */
    private BigDecimal makuatucvStart = null;
    /** ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認 */
    private String nijimikasureStart = "";
    /** 終了時ﾆｼﾞﾐ・ｶｽﾚ確認 */
    private String nijimikasureEnd = "";
    /** 印刷位置確認 */
    private Long startitikakunin = null;
    /** 印刷枚数 */
    private Long printmaisuu = null;
    /** 開始日時 */
    private Timestamp kaisinichiji = null;
    /** 担当者ｺｰﾄﾞ */
    private String tantosya = "";
    /** 印刷ｽﾀｰﾄ確認者 */
    private String kakuninsya = "";
    /** 終了日時 */
    private Timestamp syuryonichiji = null;
    /** 印刷ｴﾝﾄﾞ時担当者 */
    private String tantoEnd = "";
    /** 備考1 */
    private String biko1 = "";
    /** 備考2 */
    private String biko2 = "";
    /** 膜厚ｽﾀｰﾄ1 */
    private BigDecimal makuatsuStart1 = null;
    /** 膜厚ｽﾀｰﾄ2 */
    private BigDecimal makuatsuStart2 = null;
    /** 膜厚ｽﾀｰﾄ3 */
    private BigDecimal makuatsuStart3 = null;
    /** 膜厚ｽﾀｰﾄ4 */
    private BigDecimal makuatsuStart4 = null;
    /** 膜厚ｽﾀｰﾄ5 */
    private BigDecimal makuatsuStart5 = null;
    /** 印刷幅ｽﾀｰﾄ1 */
    private Long insatuhabaStart1 = null;
    /** 印刷幅ｽﾀｰﾄ2 */
    private Long insatuhabaStart2 = null;
    /** 印刷幅ｽﾀｰﾄ3 */
    private Long insatuhabaStart3 = null;
    /** 印刷幅ｽﾀｰﾄ4 */
    private Long insatuhabaStart4 = null;
    /** 印刷幅ｽﾀｰﾄ5 */
    private Long insatuhabaStart5 = null;

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
	 * @return goki
	 */
	public String getGoki() {
		return goki;
	}

	/**
	 * @param goki セットする goki
	 */
	public void setGoki(String goki) {
		this.goki = goki;
	}

	/**
	 * @return genryou
	 */
	public String getGenryou() {
		return genryou;
	}

	/**
	 * @param genryou セットする genryou
	 */
	public void setGenryou(String genryou) {
		this.genryou = genryou;
	}

	/**
	 * @return eatumi
	 */
	public Double getEatumi() {
		return eatumi;
	}

	/**
	 * @param eatumi セットする eatumi
	 */
	public void setEatumi(Double eatumi) {
		this.eatumi = eatumi;
	}

	/**
	 * @return seikeinagasa
	 */
	public Long getSeikeinagasa() {
		return seikeinagasa;
	}

	/**
	 * @param seikeinagasa セットする seikeinagasa
	 */
	public void setSeikeinagasa(Long seikeinagasa) {
		this.seikeinagasa = seikeinagasa;
	}

	/**
	 * @return petfilmsyurui
	 */
	public String getPetfilmsyurui() {
		return petfilmsyurui;
	}

	/**
	 * @param petfilmsyurui セットする petfilmsyurui
	 */
	public void setPetfilmsyurui(String petfilmsyurui) {
		this.petfilmsyurui = petfilmsyurui;
	}

	/**
	 * @return tapelotno
	 */
	public String getTapelotno() {
		return tapelotno;
	}

	/**
	 * @param tapelotno セットする tapelotno
	 */
	public void setTapelotno(String tapelotno) {
		this.tapelotno = tapelotno;
	}

	/**
	 * @return insaturollno
	 */
	public String getInsaturollno() {
		return insaturollno;
	}

	/**
	 * @param insaturollno セットする insaturollno
	 */
	public void setInsaturollno(String insaturollno) {
		this.insaturollno = insaturollno;
	}

	/**
	 * @return genryokigo
	 */
	public String getGenryokigo() {
		return genryokigo;
	}

	/**
	 * @param genryokigo セットする genryokigo
	 */
	public void setGenryokigo(String genryokigo) {
		this.genryokigo = genryokigo;
	}

	/**
	 * @return epaste
	 */
	public String getEpaste() {
		return epaste;
	}

	/**
	 * @param epaste セットする epaste
	 */
	public void setEpaste(String epaste) {
		this.epaste = epaste;
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
	 * @return pastenendo
	 */
	public BigDecimal getPastenendo() {
		return pastenendo;
	}

	/**
	 * @param pastenendo セットする pastenendo
	 */
	public void setPastenendo(BigDecimal pastenendo) {
		this.pastenendo = pastenendo;
	}

	/**
	 * @return pasteondo
	 */
	public BigDecimal getPasteondo() {
		return pasteondo;
	}

	/**
	 * @param pasteondo セットする pasteondo
	 */
	public void setPasteondo(BigDecimal pasteondo) {
		this.pasteondo = pasteondo;
	}

	/**
	 * @return pkokeibun1
	 */
	public BigDecimal getPkokeibun1() {
		return pkokeibun1;
	}

	/**
	 * @param pkokeibun1 セットする pkokeibun1
	 */
	public void setPkokeibun1(BigDecimal pkokeibun1) {
		this.pkokeibun1 = pkokeibun1;
	}

	/**
	 * @return pastelotno2
	 */
	public String getPastelotno2() {
		return pastelotno2;
	}

	/**
	 * @param pastelotno2 セットする pastelotno2
	 */
	public void setPastelotno2(String pastelotno2) {
		this.pastelotno2 = pastelotno2;
	}

	/**
	 * @return pastenendo2
	 */
	public BigDecimal getPastenendo2() {
		return pastenendo2;
	}

	/**
	 * @param pastenendo2 セットする pastenendo2
	 */
	public void setPastenendo2(BigDecimal pastenendo2) {
		this.pastenendo2 = pastenendo2;
	}

	/**
	 * @return pasteondo2
	 */
	public BigDecimal getPasteondo2() {
		return pasteondo2;
	}

	/**
	 * @param pasteondo2 セットする pasteondo2
	 */
	public void setPasteondo2(BigDecimal pasteondo2) {
		this.pasteondo2 = pasteondo2;
	}

	/**
	 * @return pkokeibun2
	 */
	public BigDecimal getPkokeibun2() {
		return pkokeibun2;
	}

	/**
	 * @param pkokeibun2 セットする pkokeibun2
	 */
	public void setPkokeibun2(BigDecimal pkokeibun2) {
		this.pkokeibun2 = pkokeibun2;
	}

	/**
	 * @return seihanmei
	 */
	public String getSeihanmei() {
		return seihanmei;
	}

	/**
	 * @param seihanmei セットする seihanmei
	 */
	public void setSeihanmei(String seihanmei) {
		this.seihanmei = seihanmei;
	}

	/**
	 * @return pattern
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * @param pattern セットする pattern
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * @return seihanno
	 */
	public String getSeihanno() {
		return seihanno;
	}

	/**
	 * @param seihanno セットする seihanno
	 */
	public void setSeihanno(String seihanno) {
		this.seihanno = seihanno;
	}

	/**
	 * @return seihanmaisuu
	 */
	public Long getSeihanmaisuu() {
		return seihanmaisuu;
	}

	/**
	 * @param seihanmaisuu セットする seihanmaisuu
	 */
	public void setSeihanmaisuu(Long seihanmaisuu) {
		this.seihanmaisuu = seihanmaisuu;
	}

        /**
         * @return the saidaisyorisuu
         */
        public Long getSaidaisyorisuu() {
            return saidaisyorisuu;
        }

        /**
         * @param saidaisyorisuu セットする saidaisyorisuu
         */
        public void setSaidaisyorisuu(Long saidaisyorisuu) {
            this.saidaisyorisuu = saidaisyorisuu;
        }

        /**
         * @return the ruikeisyorisuu
         */
        public Long getRuikeisyorisuu() {
            return ruikeisyorisuu;
        }

        /**
         * @param ruikeisyorisuu セットする ruikeisyorisuu
         */
        public void setRuikeisyorisuu(Long ruikeisyorisuu) {
            this.ruikeisyorisuu = ruikeisyorisuu;
        }

	/**
	 * @return skeegeno
	 */
	public String getSkeegeno() {
		return skeegeno;
	}

	/**
	 * @param skeegeno セットする skeegeno
	 */
	public void setSkeegeno(String skeegeno) {
		this.skeegeno = skeegeno;
	}

	/**
	 * @return skeegemaisuu
	 */
	public Long getSkeegemaisuu() {
		return skeegemaisuu;
	}

	/**
	 * @param skeegemaisuu セットする skeegemaisuu
	 */
	public void setSkeegemaisuu(Long skeegemaisuu) {
		this.skeegemaisuu = skeegemaisuu;
	}

	/**
	 * @return skeegespeed
	 */
	public Long getSkeegespeed() {
		return skeegespeed;
	}

	/**
	 * @param skeegespeed セットする skeegespeed
	 */
	public void setSkeegespeed(Long skeegespeed) {
		this.skeegespeed = skeegespeed;
	}

	/**
	 * @return scraperspeed
	 */
	public Long getScraperspeed() {
		return scraperspeed;
	}

	/**
	 * @param scraperspeed セットする scraperspeed
	 */
	public void setScraperspeed(Long scraperspeed) {
		this.scraperspeed = scraperspeed;
	}

	/**
	 * @return kansoondo
	 */
	public BigDecimal getKansoondo() {
		return kansoondo;
	}

	/**
	 * @param kansoondo セットする kansoondo
	 */
	public void setKansoondo(BigDecimal kansoondo) {
		this.kansoondo = kansoondo;
	}

	/**
	 * @return kansoondo2
	 */
	public BigDecimal getKansoondo2() {
		return kansoondo2;
	}

	/**
	 * @param kansoondo2 セットする kansoondo2
	 */
	public void setKansoondo2(BigDecimal kansoondo2) {
		this.kansoondo2 = kansoondo2;
	}

	/**
	 * @return kansoondo3
	 */
	public BigDecimal getKansoondo3() {
		return kansoondo3;
	}

	/**
	 * @param kansoondo3 セットする kansoondo3
	 */
	public void setKansoondo3(BigDecimal kansoondo3) {
		this.kansoondo3 = kansoondo3;
	}

	/**
	 * @return kansoondo4
	 */
	public BigDecimal getKansoondo4() {
		return kansoondo4;
	}

	/**
	 * @param kansoondo4 セットする kansoondo4
	 */
	public void setKansoondo4(BigDecimal kansoondo4) {
		this.kansoondo4 = kansoondo4;
	}

	/**
	 * @return kansoondo5
	 */
	public BigDecimal getKansoondo5() {
		return kansoondo5;
	}

	/**
	 * @param kansoondo5 セットする kansoondo5
	 */
	public void setKansoondo5(BigDecimal kansoondo5) {
		this.kansoondo5 = kansoondo5;
	}

	/**
	 * @return saatu
	 */
	public BigDecimal getSaatu() {
		return saatu;
	}

	/**
	 * @param saatu セットする saatu
	 */
	public void setSaatu(BigDecimal saatu) {
		this.saatu = saatu;
	}

	/**
	 * @return tableClearrance
	 */
	public BigDecimal getTableClearrance() {
		return tableClearrance;
	}

	/**
	 * @param tableClearrance セットする tableClearrance
	 */
	public void setTableClearrance(BigDecimal tableClearrance) {
		this.tableClearrance = tableClearrance;
	}

	/**
	 * @return kansouroatsu
	 */
	public Long getKansouroatsu() {
		return kansouroatsu;
	}

	/**
	 * @param kansouroatsu セットする kansouroatsu
	 */
	public void setKansouroatsu(Long kansouroatsu) {
		this.kansouroatsu = kansouroatsu;
	}

	/**
	 * @return mld
	 */
	public Long getMld() {
		return mld;
	}

	/**
	 * @param mld セットする mld
	 */
	public void setMld(Long mld) {
		this.mld = mld;
	}

	/**
	 * @return printhaba
	 */
	public Long getPrinthaba() {
		return printhaba;
	}

	/**
	 * @param printhaba セットする printhaba
	 */
	public void setPrinthaba(Long printhaba) {
		this.printhaba = printhaba;
	}

	/**
	 * @return makuatsuAveStart
	 */
	public BigDecimal getMakuatsuAveStart() {
		return makuatsuAveStart;
	}

	/**
	 * @param makuatsuAveStart セットする makuatsuAveStart
	 */
	public void setMakuatsuAveStart(BigDecimal makuatsuAveStart) {
		this.makuatsuAveStart = makuatsuAveStart;
	}

	/**
	 * @return makuatsuMaxStart
	 */
	public BigDecimal getMakuatsuMaxStart() {
		return makuatsuMaxStart;
	}

	/**
	 * @param makuatsuMaxStart セットする makuatsuMaxStart
	 */
	public void setMakuatsuMaxStart(BigDecimal makuatsuMaxStart) {
		this.makuatsuMaxStart = makuatsuMaxStart;
	}

	/**
	 * @return makuatsuMinStart
	 */
	public BigDecimal getMakuatsuMinStart() {
		return makuatsuMinStart;
	}

	/**
	 * @param makuatsuMinStart セットする makuatsuMinStart
	 */
	public void setMakuatsuMinStart(BigDecimal makuatsuMinStart) {
		this.makuatsuMinStart = makuatsuMinStart;
	}

	/**
	 * @return makuatucvStart
	 */
	public BigDecimal getMakuatucvStart() {
		return makuatucvStart;
	}

	/**
	 * @param makuatucvStart セットする makuatucvStart
	 */
	public void setMakuatucvStart(BigDecimal makuatucvStart) {
		this.makuatucvStart = makuatucvStart;
	}

	/**
	 * @return nijimikasureStart
	 */
	public String getNijimikasureStart() {
		return nijimikasureStart;
	}

	/**
	 * @param nijimikasureStart セットする nijimikasureStart
	 */
	public void setNijimikasureStart(String nijimikasureStart) {
		this.nijimikasureStart = nijimikasureStart;
	}

	/**
	 * @return nijimikasureEnd
	 */
	public String getNijimikasureEnd() {
		return nijimikasureEnd;
	}

	/**
	 * @param nijimikasureEnd セットする nijimikasureEnd
	 */
	public void setNijimikasureEnd(String nijimikasureEnd) {
		this.nijimikasureEnd = nijimikasureEnd;
	}

	/**
	 * @return startitikakunin
	 */
	public Long getStartitikakunin() {
		return startitikakunin;
	}

	/**
	 * @param startitikakunin セットする startitikakunin
	 */
	public void setStartitikakunin(Long startitikakunin) {
		this.startitikakunin = startitikakunin;
	}

	/**
	 * @return printmaisuu
	 */
	public Long getPrintmaisuu() {
		return printmaisuu;
	}

	/**
	 * @param printmaisuu セットする printmaisuu
	 */
	public void setPrintmaisuu(Long printmaisuu) {
		this.printmaisuu = printmaisuu;
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
	 * @return tantosya
	 */
	public String getTantosya() {
		return tantosya;
	}

	/**
	 * @param tantosya セットする tantosya
	 */
	public void setTantosya(String tantosya) {
		this.tantosya = tantosya;
	}

	/**
	 * @return kakuninsya
	 */
	public String getKakuninsya() {
		return kakuninsya;
	}

	/**
	 * @param kakuninsya セットする kakuninsya
	 */
	public void setKakuninsya(String kakuninsya) {
		this.kakuninsya = kakuninsya;
	}

	/**
	 * @return syuryonichiji
	 */
	public Timestamp getSyuryonichiji() {
		return syuryonichiji;
	}

	/**
	 * @param syuryonichiji セットする syuryonichiji
	 */
	public void setSyuryonichiji(Timestamp syuryonichiji) {
		this.syuryonichiji = syuryonichiji;
	}

	/**
	 * @return tantoEnd
	 */
	public String getTantoEnd() {
		return tantoEnd;
	}

	/**
	 * @param tantoEnd セットする tantoEnd
	 */
	public void setTantoEnd(String tantoEnd) {
		this.tantoEnd = tantoEnd;
	}

	/**
	 * @return biko1
	 */
	public String getBiko1() {
		return biko1;
	}

	/**
	 * @param biko1 セットする biko1
	 */
	public void setBiko1(String biko1) {
		this.biko1 = biko1;
	}

	/**
	 * @return biko2
	 */
	public String getBiko2() {
		return biko2;
	}

	/**
	 * @param biko2 セットする biko2
	 */
	public void setBiko2(String biko2) {
		this.biko2 = biko2;
	}

	/**
	 * @return makuatsuStart1
	 */
	public BigDecimal getMakuatsuStart1() {
		return makuatsuStart1;
	}

	/**
	 * @param makuatsuStart1 セットする makuatsuStart1
	 */
	public void setMakuatsuStart1(BigDecimal makuatsuStart1) {
		this.makuatsuStart1 = makuatsuStart1;
	}

	/**
	 * @return makuatsuStart2
	 */
	public BigDecimal getMakuatsuStart2() {
		return makuatsuStart2;
	}

	/**
	 * @param makuatsuStart2 セットする makuatsuStart2
	 */
	public void setMakuatsuStart2(BigDecimal makuatsuStart2) {
		this.makuatsuStart2 = makuatsuStart2;
	}

	/**
	 * @return makuatsuStart3
	 */
	public BigDecimal getMakuatsuStart3() {
		return makuatsuStart3;
	}

	/**
	 * @param makuatsuStart3 セットする makuatsuStart3
	 */
	public void setMakuatsuStart3(BigDecimal makuatsuStart3) {
		this.makuatsuStart3 = makuatsuStart3;
	}

	/**
	 * @return makuatsuStart4
	 */
	public BigDecimal getMakuatsuStart4() {
		return makuatsuStart4;
	}

	/**
	 * @param makuatsuStart4 セットする makuatsuStart4
	 */
	public void setMakuatsuStart4(BigDecimal makuatsuStart4) {
		this.makuatsuStart4 = makuatsuStart4;
	}

	/**
	 * @return makuatsuStart5
	 */
	public BigDecimal getMakuatsuStart5() {
		return makuatsuStart5;
	}

	/**
	 * @param makuatsuStart5 セットする makuatsuStart5
	 */
	public void setMakuatsuStart5(BigDecimal makuatsuStart5) {
		this.makuatsuStart5 = makuatsuStart5;
	}

	/**
	 * @return insatuhabaStart1
	 */
	public Long getInsatuhabaStart1() {
		return insatuhabaStart1;
	}

	/**
	 * @param insatuhabaStart1 セットする insatuhabaStart1
	 */
	public void setInsatuhabaStart1(Long insatuhabaStart1) {
		this.insatuhabaStart1 = insatuhabaStart1;
	}

	/**
	 * @return insatuhabaStart2
	 */
	public Long getInsatuhabaStart2() {
		return insatuhabaStart2;
	}

	/**
	 * @param insatuhabaStart2 セットする insatuhabaStart2
	 */
	public void setInsatuhabaStart2(Long insatuhabaStart2) {
		this.insatuhabaStart2 = insatuhabaStart2;
	}

	/**
	 * @return insatuhabaStart3
	 */
	public Long getInsatuhabaStart3() {
		return insatuhabaStart3;
	}

	/**
	 * @param insatuhabaStart3 セットする insatuhabaStart3
	 */
	public void setInsatuhabaStart3(Long insatuhabaStart3) {
		this.insatuhabaStart3 = insatuhabaStart3;
	}

	/**
	 * @return insatuhabaStart4
	 */
	public Long getInsatuhabaStart4() {
		return insatuhabaStart4;
	}

	/**
	 * @param insatuhabaStart4 セットする insatuhabaStart4
	 */
	public void setInsatuhabaStart4(Long insatuhabaStart4) {
		this.insatuhabaStart4 = insatuhabaStart4;
	}

	/**
	 * @return insatuhabaStart5
	 */
	public Long getInsatuhabaStart5() {
		return insatuhabaStart5;
	}

	/**
	 * @param insatuhabaStart5 セットする insatuhabaStart5
	 */
	public void setInsatuhabaStart5(Long insatuhabaStart5) {
		this.insatuhabaStart5 = insatuhabaStart5;
	}

}