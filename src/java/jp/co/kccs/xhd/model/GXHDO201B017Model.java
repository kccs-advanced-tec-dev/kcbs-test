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
 * 変更日       2019/08/05<br>
 * 計画書No     K1811-DS001<br>
 * 変更者       KCSS K.Jo<br>
 * 変更理由     新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 焼成・焼成履歴検索画面のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2019/08/05
 */
public class GXHDO201B017Model implements Serializable {

    /**
     * ﾛｯﾄNo.
     */
    private String lotno = "";

    /**
     * KCPNO
     */
    private String kcpno = "";

    /**
     * 実績No
     */
    private Integer jissekino = null;

    /**
     * 個数
     */
    private Integer kosuu = null;

    /**
     * 原料品種名
     */
    private String genryohinsyumei = "";

    /**
     * 原料ｸﾞﾙｰﾌﾟ
     */
    private String genryogroup = "";

    /**
     * 焼成開始日時
     */
    private Timestamp skaisinichiji = null;

    /**
     * 焼成終了日時
     */
    private Timestamp ssyuryonichiji = null;

    /**
     * ﾊﾞｯﾁ炉ﾌﾟﾛｸﾞﾗﾑNo
     */
    private Integer bprogramno = null;

    /**
     * 焼成温度
     */
    private Integer syoseiondo = null;

    /**
     * ﾄﾝﾈﾙ炉・ﾊﾞｯﾁ炉号機
     */
    private String goki = "";

    /**
     * 受入ｾｯﾀ枚数
     */
    private Integer ssettermaisuu = null;

    /**
     * 入炉台板枚数
     */
    private BigDecimal nyurodaibanmaisuu = null;

    /**
     * 焼成開始担当者
     */
    private String skaisitantosya = "";

    /**
     * 終了担当者
     */
    private String ssyuryotantosya = "";

    /**
     * 備考1
     */
    private String biko1 = "";

    /**
     * 備考2
     */
    private String biko2 = "";

    /**
     * 備考3
     */
    private String biko3 = "";

    /**
     * 備考4
     */
    private String biko4 = "";

    /**
     * 備考5
     */
    private String biko5 = "";

    /**
     * ﾊﾞﾗｼ開始日時
     */
    private Timestamp bkaisinichiji = null;

    /**
     * ﾊﾞﾗｼ終了日時
     */
    private Timestamp bsyuryonichiji = null;

    /**
     * ﾊﾞﾗｼｾｯﾀｰ枚数
     */
    private Integer bsettermaisuu = null;

    /**
     * ﾎﾟｯﾄ数
     */
    private Integer potsuu = null;

    /**
     * ﾎﾟｯﾄNo
     */
    private String potno = "";

    /**
     * ﾊﾞﾗｼ担当者
     */
    private String btantosya = "";

    /**
     * 備考6
     */
    private String biko6 = "";

    /**
     * 備考7
     */
    private String biko7 = "";

    /**
     * 再酸化号機
     */
    private String sankagoki = "";

    /**
     * 再酸化温度
     */
    private Integer sankaondo = null;

    /**
     * 再酸化終了日時
     */
    private Timestamp sankasyuryonichiji = null;

    /**
     * 投入ｾｯﾀ枚数
     */
    private Integer tounyusettasuu = null;

    /**
     * 焼成設定ﾊﾟﾀｰﾝﾁｪｯｸ
     */
    private String setteipattern = "";

    /**
     * 段数
     */
    private Integer dansuu = null;

    /**
     * 外観確認
     */
    private String gaikancheck = "";

    /**
     * 回収ｾｯﾀ枚数
     */
    private Integer kaishusettasuu = null;

    /**
     * 開始確認者
     */
    private String startkakuninsyacode = "";

    /**
     * 2次脱脂号機
     */
    private String nijidasshigouki = "";

    /**
     * 2次脱脂設定ﾊﾟﾀｰﾝ
     */
    private String nijidasshisetteipt = "";

    /**
     * 2次脱脂ｷｰﾌﾟ温度
     */
    private Integer nijidasshikeepondo = null;

    /**
     * 2次脱脂ｺﾝﾍﾞｱ速度
     */
    private Integer nijidasshispeed = null;

    /**
     * 焼成ﾋﾟｰｸ温度指示
     */
    private Integer peakondo = null;

    /**
     * 焼成ﾛｰﾗｰ速度
     */
    private Integer syoseispeed = null;

    /**
     * 焼成ﾊﾟｰｼﾞ
     */
    private Integer syoseipurge = null;

    /**
     * 1回目再酸化号機1
     */
    private String saisankagouki1 = "";

    /**
     * 1回目再酸化号機2
     */
    private String saisankagouki2 = "";

    /**
     * 1回目再酸化設定ﾊﾟﾀｰﾝ
     */
    private String saisankasetteipt = "";

    /**
     * 1回目再酸化ｷｰﾌﾟ温度
     */
    private Integer saisankakeepondo = null;

    /**
     * 1回目再酸化ｺﾝﾍﾞｱ速度
     */
    private Integer saisankacsokudo = null;

    /**
     * 1回目再酸化後外観
     */
    private String saisankagogaikan = "";

    /**
     * 焼成種類
     */
    private String syoseisyurui = "";

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
     * @return jissekino
     */
    public Integer getJissekino() {
        return jissekino;
    }

    /**
     * @param jissekino セットする jissekino
     */
    public void setJissekino(Integer jissekino) {
        this.jissekino = jissekino;
    }

    /**
     * @return kosuu
     */
    public Integer getKosuu() {
        return kosuu;
    }

    /**
     * @param kosuu セットする kosuu
     */
    public void setKosuu(Integer kosuu) {
        this.kosuu = kosuu;
    }

    /**
     * @return genryohinsyumei
     */
    public String getGenryohinsyumei() {
        return genryohinsyumei;
    }

    /**
     * @param genryohinsyumei セットする genryohinsyumei
     */
    public void setGenryohinsyumei(String genryohinsyumei) {
        this.genryohinsyumei = genryohinsyumei;
    }

    /**
     * @return genryogroup
     */
    public String getGenryogroup() {
        return genryogroup;
    }

    /**
     * @param genryogroup セットする genryogroup
     */
    public void setGenryogroup(String genryogroup) {
        this.genryogroup = genryogroup;
    }

    /**
     * @return skaisinichiji
     */
    public Timestamp getSkaisinichiji() {
        return skaisinichiji;
    }

    /**
     * @param skaisinichiji セットする skaisinichiji
     */
    public void setSkaisinichiji(Timestamp skaisinichiji) {
        this.skaisinichiji = skaisinichiji;
    }

    /**
     * @return ssyuryonichiji
     */
    public Timestamp getSsyuryonichiji() {
        return ssyuryonichiji;
    }

    /**
     * @param ssyuryonichiji セットする ssyuryonichiji
     */
    public void setSsyuryonichiji(Timestamp ssyuryonichiji) {
        this.ssyuryonichiji = ssyuryonichiji;
    }

    /**
     * @return bprogramno
     */
    public Integer getBprogramno() {
        return bprogramno;
    }

    /**
     * @param bprogramno セットする bprogramno
     */
    public void setBprogramno(Integer bprogramno) {
        this.bprogramno = bprogramno;
    }

    /**
     * @return syoseiondo
     */
    public Integer getSyoseiondo() {
        return syoseiondo;
    }

    /**
     * @param syoseiondo セットする syoseiondo
     */
    public void setSyoseiondo(Integer syoseiondo) {
        this.syoseiondo = syoseiondo;
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
     * @return ssettermaisuu
     */
    public Integer getSsettermaisuu() {
        return ssettermaisuu;
    }

    /**
     * @param ssettermaisuu セットする ssettermaisuu
     */
    public void setSsettermaisuu(Integer ssettermaisuu) {
        this.ssettermaisuu = ssettermaisuu;
    }

    /**
     * @return nyurodaibanmaisuu
     */
    public BigDecimal getNyurodaibanmaisuu() {
        return nyurodaibanmaisuu;
    }

    /**
     * @param nyurodaibanmaisuu セットする nyurodaibanmaisuu
     */
    public void setNyurodaibanmaisuu(BigDecimal nyurodaibanmaisuu) {
        this.nyurodaibanmaisuu = nyurodaibanmaisuu;
    }

    /**
     * @return skaisitantosya
     */
    public String getSkaisitantosya() {
        return skaisitantosya;
    }

    /**
     * @param skaisitantosya セットする skaisitantosya
     */
    public void setSkaisitantosya(String skaisitantosya) {
        this.skaisitantosya = skaisitantosya;
    }

    /**
     * @return ssyuryotantosya
     */
    public String getSsyuryotantosya() {
        return ssyuryotantosya;
    }

    /**
     * @param ssyuryotantosya セットする ssyuryotantosya
     */
    public void setSsyuryotantosya(String ssyuryotantosya) {
        this.ssyuryotantosya = ssyuryotantosya;
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
     * @return biko3
     */
    public String getBiko3() {
        return biko3;
    }

    /**
     * @param biko3 セットする biko3
     */
    public void setBiko3(String biko3) {
        this.biko3 = biko3;
    }

    /**
     * @return biko4
     */
    public String getBiko4() {
        return biko4;
    }

    /**
     * @param biko4 セットする biko4
     */
    public void setBiko4(String biko4) {
        this.biko4 = biko4;
    }

    /**
     * @return biko5
     */
    public String getBiko5() {
        return biko5;
    }

    /**
     * @param biko5 セットする biko5
     */
    public void setBiko5(String biko5) {
        this.biko5 = biko5;
    }

    /**
     * @return bkaisinichiji
     */
    public Timestamp getBkaisinichiji() {
        return bkaisinichiji;
    }

    /**
     * @param bkaisinichiji セットする bkaisinichiji
     */
    public void setBkaisinichiji(Timestamp bkaisinichiji) {
        this.bkaisinichiji = bkaisinichiji;
    }

    /**
     * @return bsyuryonichiji
     */
    public Timestamp getBsyuryonichiji() {
        return bsyuryonichiji;
    }

    /**
     * @param bsyuryonichiji セットする bsyuryonichiji
     */
    public void setBsyuryonichiji(Timestamp bsyuryonichiji) {
        this.bsyuryonichiji = bsyuryonichiji;
    }

    /**
     * @return bsettermaisuu
     */
    public Integer getBsettermaisuu() {
        return bsettermaisuu;
    }

    /**
     * @param bsettermaisuu セットする bsettermaisuu
     */
    public void setBsettermaisuu(Integer bsettermaisuu) {
        this.bsettermaisuu = bsettermaisuu;
    }

    /**
     * @return potsuu
     */
    public Integer getPotsuu() {
        return potsuu;
    }

    /**
     * @param potsuu セットする potsuu
     */
    public void setPotsuu(Integer potsuu) {
        this.potsuu = potsuu;
    }

    /**
     * @return potno
     */
    public String getPotno() {
        return potno;
    }

    /**
     * @param potno セットする potno
     */
    public void setPotno(String potno) {
        this.potno = potno;
    }

    /**
     * @return btantosya
     */
    public String getBtantosya() {
        return btantosya;
    }

    /**
     * @param btantosya セットする btantosya
     */
    public void setBtantosya(String btantosya) {
        this.btantosya = btantosya;
    }

    /**
     * @return biko6
     */
    public String getBiko6() {
        return biko6;
    }

    /**
     * @param biko6 セットする biko6
     */
    public void setBiko6(String biko6) {
        this.biko6 = biko6;
    }

    /**
     * @return biko7
     */
    public String getBiko7() {
        return biko7;
    }

    /**
     * @param biko7 セットする biko7
     */
    public void setBiko7(String biko7) {
        this.biko7 = biko7;
    }

    /**
     * @return sankagoki
     */
    public String getSankagoki() {
        return sankagoki;
    }

    /**
     * @param sankagoki セットする sankagoki
     */
    public void setSankagoki(String sankagoki) {
        this.sankagoki = sankagoki;
    }

    /**
     * @return sankaondo
     */
    public Integer getSankaondo() {
        return sankaondo;
    }

    /**
     * @param sankaondo セットする sankaondo
     */
    public void setSankaondo(Integer sankaondo) {
        this.sankaondo = sankaondo;
    }

    /**
     * @return sankasyuryonichiji
     */
    public Timestamp getSankasyuryonichiji() {
        return sankasyuryonichiji;
    }

    /**
     * @param sankasyuryonichiji セットする sankasyuryonichiji
     */
    public void setSankasyuryonichiji(Timestamp sankasyuryonichiji) {
        this.sankasyuryonichiji = sankasyuryonichiji;
    }

    /**
     * @return tounyusettasuu
     */
    public Integer getTounyusettasuu() {
        return tounyusettasuu;
    }

    /**
     * @param tounyusettasuu セットする tounyusettasuu
     */
    public void setTounyusettasuu(Integer tounyusettasuu) {
        this.tounyusettasuu = tounyusettasuu;
    }

    /**
     * @return setteipattern
     */
    public String getSetteipattern() {
        return setteipattern;
    }

    /**
     * @param setteipattern セットする setteipattern
     */
    public void setSetteipattern(String setteipattern) {
        this.setteipattern = setteipattern;
    }

    /**
     * @return dansuu
     */
    public Integer getDansuu() {
        return dansuu;
    }

    /**
     * @param dansuu セットする dansuu
     */
    public void setDansuu(Integer dansuu) {
        this.dansuu = dansuu;
    }

    /**
     * @return gaikancheck
     */
    public String getGaikancheck() {
        return gaikancheck;
    }

    /**
     * @param gaikancheck セットする gaikancheck
     */
    public void setGaikancheck(String gaikancheck) {
        this.gaikancheck = gaikancheck;
    }

    /**
     * @return kaishusettasuu
     */
    public Integer getKaishusettasuu() {
        return kaishusettasuu;
    }

    /**
     * @param kaishusettasuu セットする kaishusettasuu
     */
    public void setKaishusettasuu(Integer kaishusettasuu) {
        this.kaishusettasuu = kaishusettasuu;
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
     * @return nijidasshigouki
     */
    public String getNijidasshigouki() {
        return nijidasshigouki;
    }

    /**
     * @param nijidasshigouki セットする nijidasshigouki
     */
    public void setNijidasshigouki(String nijidasshigouki) {
        this.nijidasshigouki = nijidasshigouki;
    }

    /**
     * @return nijidasshisetteipt
     */
    public String getNijidasshisetteipt() {
        return nijidasshisetteipt;
    }

    /**
     * @param nijidasshisetteipt セットする nijidasshisetteipt
     */
    public void setNijidasshisetteipt(String nijidasshisetteipt) {
        this.nijidasshisetteipt = nijidasshisetteipt;
    }

    /**
     * @return nijidasshikeepondo
     */
    public Integer getNijidasshikeepondo() {
        return nijidasshikeepondo;
    }

    /**
     * @param nijidasshikeepondo セットする nijidasshikeepondo
     */
    public void setNijidasshikeepondo(Integer nijidasshikeepondo) {
        this.nijidasshikeepondo = nijidasshikeepondo;
    }

    /**
     * @return nijidasshispeed
     */
    public Integer getNijidasshispeed() {
        return nijidasshispeed;
    }

    /**
     * @param nijidasshispeed セットする nijidasshispeed
     */
    public void setNijidasshispeed(Integer nijidasshispeed) {
        this.nijidasshispeed = nijidasshispeed;
    }

    /**
     * @return peakondo
     */
    public Integer getPeakondo() {
        return peakondo;
    }

    /**
     * @param peakondo セットする peakondo
     */
    public void setPeakondo(Integer peakondo) {
        this.peakondo = peakondo;
    }

    /**
     * @return syoseispeed
     */
    public Integer getSyoseispeed() {
        return syoseispeed;
    }

    /**
     * @param syoseispeed セットする syoseispeed
     */
    public void setSyoseispeed(Integer syoseispeed) {
        this.syoseispeed = syoseispeed;
    }

    /**
     * @return syoseipurge
     */
    public Integer getSyoseipurge() {
        return syoseipurge;
    }

    /**
     * @param syoseipurge セットする syoseipurge
     */
    public void setSyoseipurge(Integer syoseipurge) {
        this.syoseipurge = syoseipurge;
    }

    /**
     * @return saisankagouki1
     */
    public String getSaisankagouki1() {
        return saisankagouki1;
    }

    /**
     * @param saisankagouki1 セットする saisankagouki1
     */
    public void setSaisankagouki1(String saisankagouki1) {
        this.saisankagouki1 = saisankagouki1;
    }

    /**
     * @return saisankagouki2
     */
    public String getSaisankagouki2() {
        return saisankagouki2;
    }

    /**
     * @param saisankagouki2 セットする saisankagouki2
     */
    public void setSaisankagouki2(String saisankagouki2) {
        this.saisankagouki2 = saisankagouki2;
    }

    /**
     * @return saisankasetteipt
     */
    public String getSaisankasetteipt() {
        return saisankasetteipt;
    }

    /**
     * @param saisankasetteipt セットする saisankasetteipt
     */
    public void setSaisankasetteipt(String saisankasetteipt) {
        this.saisankasetteipt = saisankasetteipt;
    }

    /**
     * @return saisankakeepondo
     */
    public Integer getSaisankakeepondo() {
        return saisankakeepondo;
    }

    /**
     * @param saisankakeepondo セットする saisankakeepondo
     */
    public void setSaisankakeepondo(Integer saisankakeepondo) {
        this.saisankakeepondo = saisankakeepondo;
    }

    /**
     * @return saisankacsokudo
     */
    public Integer getSaisankacsokudo() {
        return saisankacsokudo;
    }

    /**
     * @param saisankacsokudo セットする saisankacsokudo
     */
    public void setSaisankacsokudo(Integer saisankacsokudo) {
        this.saisankacsokudo = saisankacsokudo;
    }

    /**
     * @return saisankagogaikan
     */
    public String getSaisankagogaikan() {
        return saisankagogaikan;
    }

    /**
     * @param saisankagogaikan セットする saisankagogaikan
     */
    public void setSaisankagogaikan(String saisankagogaikan) {
        this.saisankagogaikan = saisankagogaikan;
    }

    /**
     * @return syoseisyurui
     */
    public String getSyoseisyurui() {
        return syoseisyurui;
    }

    /**
     * @param syoseisyurui セットする syoseisyurui
     */
    public void setSyoseisyurui(String syoseisyurui) {
        this.syoseisyurui = syoseisyurui;
    }
}
