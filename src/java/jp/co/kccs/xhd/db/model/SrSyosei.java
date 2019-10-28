/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/06/15<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 焼成のモデルクラスです。
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2019/06/15
 */
public class SrSyosei {

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
     * 実績No
     */
    private Integer jissekino;

    /**
     * KCPNo
     */
    private String kcpno;

    /**
     * 個数
     */
    private Integer kosuu;

    /**
     * 原料品種名
     */
    private String genryohinsyumei;

    /**
     * 原料ｸﾞﾙｰﾌﾟ
     */
    private String genryogroup;

    /**
     * 焼成開始日時
     */
    private Timestamp skaisinichiji;

    /**
     * 焼成終了日時
     */
    private Timestamp ssyuryonichiji;

    /**
     * ﾊﾞｯﾁ炉ﾌﾟﾛｸﾞﾗﾑNo
     */
    private Integer bprogramno;

    /**
     * 焼成温度
     */
    private Integer syoseiondo;

    /**
     * ﾄﾝﾈﾙ炉・ﾊﾞｯﾁ炉号機
     */
    private String goki;

    /**
     * 受入ｾｯﾀ枚数
     */
    private Integer ssettermaisuu;

    /**
     * 入炉台板枚数
     */
    private BigDecimal nyurodaibanmaisuu;

    /**
     * 焼成開始担当者
     */
    private String skaisitantosya;

    /**
     * 終了担当者
     */
    private String ssyuryotantosya;

    /**
     * 備考1
     */
    private String biko1;

    /**
     * 備考2
     */
    private String biko2;

    /**
     * 備考3
     */
    private String biko3;

    /**
     * 備考4
     */
    private String biko4;

    /**
     * 備考5
     */
    private String biko5;

    /**
     * ﾊﾞﾗｼ開始日時
     */
    private Timestamp bkaisinichiji;

    /**
     * ﾊﾞﾗｼ終了日時
     */
    private Timestamp bsyuryonichiji;

    /**
     * ﾊﾞﾗｼｾｯﾀｰ枚数
     */
    private Integer bsettermaisuu;

    /**
     * ﾎﾟｯﾄ数
     */
    private Integer potsuu;

    /**
     * ﾎﾟｯﾄNo
     */
    private String potno;

    /**
     * ﾊﾞﾗｼ担当者
     */
    private String btantosya;

    /**
     * 備考6
     */
    private String biko6;

    /**
     * 備考7
     */
    private String biko7;

    /**
     * 登録日時
     */
    private Timestamp torokunichiji;

    /**
     * 更新日時
     */
    private Timestamp kosinnichiji;

    /**
     * 再酸化号機
     */
    private String sankagoki;

    /**
     * 再酸化温度
     */
    private Long sankaondo;

    /**
     * 再酸化終了日時
     */
    private Timestamp sankasyuryonichiji;

    /**
     * 投入ｾｯﾀ枚数
     */
    private Integer tounyusettasuu;

    /**
     * 焼成設定ﾊﾟﾀｰﾝﾁｪｯｸ
     */
    private Integer setteipattern;

    /**
     * 段数
     */
    private Integer dansuu;

    /**
     * 外観確認
     */
    private Integer gaikancheck;

    /**
     * 回収ｾｯﾀ枚数
     */
    private Integer kaishusettasuu;

    /**
     * 開始確認者
     */
    private String startkakuninsyacode;

    /**
     * 2次脱脂号機
     */
    private String nijidasshigouki;

    /**
     * 2次脱脂設定ﾊﾟﾀｰﾝ
     */
    private Integer nijidasshisetteipt;

    /**
     * 2次脱脂ｷｰﾌﾟ温度
     */
    private Integer nijidasshikeepondo;

    /**
     * 2次脱脂ｺﾝﾍﾞｱ速度
     */
    private Integer nijidasshispeed;

    /**
     * 焼成ﾋﾟｰｸ温度指示
     */
    private String peakondo;

    /**
     * 焼成ﾛｰﾗｰ速度
     */
    private Integer syoseispeed;

    /**
     * 焼成ﾊﾟｰｼﾞ
     */
    private Integer syoseipurge;

    /**
     * 1回目再酸化号機1
     */
    private String saisankagouki1;

    /**
     * 1回目再酸化号機2
     */
    private String saisankagouki2;

    /**
     * 1回目再酸化設定ﾊﾟﾀｰﾝ
     */
    private Integer saisankasetteipt;

    /**
     * 1回目再酸化ｷｰﾌﾟ温度
     */
    private Integer saisankakeepondo;

    /**
     * 1回目再酸化ｺﾝﾍﾞｱ速度
     */
    private Integer saisankacsokudo;

    /**
     * 1回目再酸化後外観
     */
    private Integer saisankagogaikan;

    /**
     * 焼成種類
     */
    private String syoseisyurui;

    /**
     * revision
     */
    private Long revision;

    /**
     * 削除ﾌﾗｸﾞ
     */
    private Integer deleteflag;

    /**
     * 工場ｺｰﾄﾞ
     *
     * @return the kojyo
     */
    public String getKojyo() {
        return kojyo;
    }

    /**
     * 工場ｺｰﾄﾞ
     *
     * @param kojyo the kojyo to set
     */
    public void setKojyo(String kojyo) {
        this.kojyo = kojyo;
    }

    /**
     * ﾛｯﾄNo
     *
     * @return the lotno
     */
    public String getLotno() {
        return lotno;
    }

    /**
     * ﾛｯﾄNo
     *
     * @param lotno the lotno to set
     */
    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * 枝番
     *
     * @return the edaban
     */
    public String getEdaban() {
        return edaban;
    }

    /**
     * 枝番
     *
     * @param edaban the edaban to set
     */
    public void setEdaban(String edaban) {
        this.edaban = edaban;
    }

    /**
     * 実績No
     *
     * @return the jissekino
     */
    public Integer getJissekino() {
        return jissekino;
    }

    /**
     * 実績No
     *
     * @param jissekino the jissekino to set
     */
    public void setJissekino(Integer jissekino) {
        this.jissekino = jissekino;
    }

    /**
     * KCPNo
     *
     * @return the kcpno
     */
    public String getKcpno() {
        return kcpno;
    }

    /**
     * KCPNo
     *
     * @param kcpno the kcpno to set
     */
    public void setKcpno(String kcpno) {
        this.kcpno = kcpno;
    }

    /**
     * 個数
     *
     * @return the kosuu
     */
    public Integer getKosuu() {
        return kosuu;
    }

    /**
     * 個数
     *
     * @param kosuu the kosuu to set
     */
    public void setKosuu(Integer kosuu) {
        this.kosuu = kosuu;
    }

    /**
     * 原料品種名
     *
     * @return the genryohinsyumei
     */
    public String getGenryohinsyumei() {
        return genryohinsyumei;
    }

    /**
     * 原料品種名
     *
     * @param genryohinsyumei the genryohinsyumei to set
     */
    public void setGenryohinsyumei(String genryohinsyumei) {
        this.genryohinsyumei = genryohinsyumei;
    }

    /**
     * 原料ｸﾞﾙｰﾌﾟ
     *
     * @return the genryogroup
     */
    public String getGenryogroup() {
        return genryogroup;
    }

    /**
     * 原料ｸﾞﾙｰﾌﾟ
     *
     * @param genryogroup the genryogroup to set
     */
    public void setGenryogroup(String genryogroup) {
        this.genryogroup = genryogroup;
    }

    /**
     * 焼成開始日時
     *
     * @return the skaisinichiji
     */
    public Timestamp getSkaisinichiji() {
        return skaisinichiji;
    }

    /**
     * 焼成開始日時
     *
     * @param skaisinichiji the skaisinichiji to set
     */
    public void setSkaisinichiji(Timestamp skaisinichiji) {
        this.skaisinichiji = skaisinichiji;
    }

    /**
     * 焼成終了日時
     *
     * @return the ssyuryonichiji
     */
    public Timestamp getSsyuryonichiji() {
        return ssyuryonichiji;
    }

    /**
     * 焼成終了日時
     *
     * @param ssyuryonichiji the ssyuryonichiji to set
     */
    public void setSsyuryonichiji(Timestamp ssyuryonichiji) {
        this.ssyuryonichiji = ssyuryonichiji;
    }

    /**
     * ﾊﾞｯﾁ炉ﾌﾟﾛｸﾞﾗﾑNo
     *
     * @return the bprogramno
     */
    public Integer getBprogramno() {
        return bprogramno;
    }

    /**
     * ﾊﾞｯﾁ炉ﾌﾟﾛｸﾞﾗﾑNo
     *
     * @param bprogramno the bprogramno to set
     */
    public void setBprogramno(Integer bprogramno) {
        this.bprogramno = bprogramno;
    }

    /**
     * 焼成温度
     *
     * @return the syoseiondo
     */
    public Integer getSyoseiondo() {
        return syoseiondo;
    }

    /**
     * 焼成温度
     *
     * @param syoseiondo the syoseiondo to set
     */
    public void setSyoseiondo(Integer syoseiondo) {
        this.syoseiondo = syoseiondo;
    }

    /**
     * ﾄﾝﾈﾙ炉・ﾊﾞｯﾁ炉号機
     *
     * @return the goki
     */
    public String getGoki() {
        return goki;
    }

    /**
     * ﾄﾝﾈﾙ炉・ﾊﾞｯﾁ炉号機
     *
     * @param goki the goki to set
     */
    public void setGoki(String goki) {
        this.goki = goki;
    }

    /**
     * 受入ｾｯﾀ枚数
     *
     * @return the ssettermaisuu
     */
    public Integer getSsettermaisuu() {
        return ssettermaisuu;
    }

    /**
     * 受入ｾｯﾀ枚数
     *
     * @param ssettermaisuu the ssettermaisuu to set
     */
    public void setSsettermaisuu(Integer ssettermaisuu) {
        this.ssettermaisuu = ssettermaisuu;
    }

    /**
     * 入炉台板枚数
     *
     * @return the nyurodaibanmaisuu
     */
    public BigDecimal getNyurodaibanmaisuu() {
        return nyurodaibanmaisuu;
    }

    /**
     * 入炉台板枚数
     *
     * @param nyurodaibanmaisuu the nyurodaibanmaisuu to set
     */
    public void setNyurodaibanmaisuu(BigDecimal nyurodaibanmaisuu) {
        this.nyurodaibanmaisuu = nyurodaibanmaisuu;
    }

    /**
     * 焼成開始担当者
     *
     * @return the skaisitantosya
     */
    public String getSkaisitantosya() {
        return skaisitantosya;
    }

    /**
     * 焼成開始担当者
     *
     * @param skaisitantosya the skaisitantosya to set
     */
    public void setSkaisitantosya(String skaisitantosya) {
        this.skaisitantosya = skaisitantosya;
    }

    /**
     * 終了担当者
     *
     * @return the ssyuryotantosya
     */
    public String getSsyuryotantosya() {
        return ssyuryotantosya;
    }

    /**
     * 終了担当者
     *
     * @param ssyuryotantosya the ssyuryotantosya to set
     */
    public void setSsyuryotantosya(String ssyuryotantosya) {
        this.ssyuryotantosya = ssyuryotantosya;
    }

    /**
     * 備考1
     *
     * @return the biko1
     */
    public String getBiko1() {
        return biko1;
    }

    /**
     * 備考1
     *
     * @param biko1 the biko1 to set
     */
    public void setBiko1(String biko1) {
        this.biko1 = biko1;
    }

    /**
     * 備考2
     *
     * @return the biko2
     */
    public String getBiko2() {
        return biko2;
    }

    /**
     * 備考2
     *
     * @param biko2 the biko2 to set
     */
    public void setBiko2(String biko2) {
        this.biko2 = biko2;
    }

    /**
     * 備考3
     *
     * @return the biko3
     */
    public String getBiko3() {
        return biko3;
    }

    /**
     * 備考3
     *
     * @param biko3 the biko3 to set
     */
    public void setBiko3(String biko3) {
        this.biko3 = biko3;
    }

    /**
     * 備考4
     *
     * @return the biko4
     */
    public String getBiko4() {
        return biko4;
    }

    /**
     * 備考4
     *
     * @param biko4 the biko4 to set
     */
    public void setBiko4(String biko4) {
        this.biko4 = biko4;
    }

    /**
     * 備考5
     *
     * @return the biko5
     */
    public String getBiko5() {
        return biko5;
    }

    /**
     * 備考5
     *
     * @param biko5 the biko5 to set
     */
    public void setBiko5(String biko5) {
        this.biko5 = biko5;
    }

    /**
     * ﾊﾞﾗｼ開始日時
     *
     * @return the bkaisinichiji
     */
    public Timestamp getBkaisinichiji() {
        return bkaisinichiji;
    }

    /**
     * ﾊﾞﾗｼ開始日時
     *
     * @param bkaisinichiji the bkaisinichiji to set
     */
    public void setBkaisinichiji(Timestamp bkaisinichiji) {
        this.bkaisinichiji = bkaisinichiji;
    }

    /**
     * ﾊﾞﾗｼ終了日時
     *
     * @return the bsyuryonichiji
     */
    public Timestamp getBsyuryonichiji() {
        return bsyuryonichiji;
    }

    /**
     * ﾊﾞﾗｼ終了日時
     *
     * @param bsyuryonichiji the bsyuryonichiji to set
     */
    public void setBsyuryonichiji(Timestamp bsyuryonichiji) {
        this.bsyuryonichiji = bsyuryonichiji;
    }

    /**
     * ﾊﾞﾗｼｾｯﾀｰ枚数
     *
     * @return the bsettermaisuu
     */
    public Integer getBsettermaisuu() {
        return bsettermaisuu;
    }

    /**
     * ﾊﾞﾗｼｾｯﾀｰ枚数
     *
     * @param bsettermaisuu the bsettermaisuu to set
     */
    public void setBsettermaisuu(Integer bsettermaisuu) {
        this.bsettermaisuu = bsettermaisuu;
    }

    /**
     * ﾎﾟｯﾄ数
     *
     * @return the potsuu
     */
    public Integer getPotsuu() {
        return potsuu;
    }

    /**
     * ﾎﾟｯﾄ数
     *
     * @param potsuu the potsuu to set
     */
    public void setPotsuu(Integer potsuu) {
        this.potsuu = potsuu;
    }

    /**
     * ﾎﾟｯﾄNo
     *
     * @return the potno
     */
    public String getPotno() {
        return potno;
    }

    /**
     * ﾎﾟｯﾄNo
     *
     * @param potno the potno to set
     */
    public void setPotno(String potno) {
        this.potno = potno;
    }

    /**
     * ﾊﾞﾗｼ担当者
     *
     * @return the btantosya
     */
    public String getBtantosya() {
        return btantosya;
    }

    /**
     * ﾊﾞﾗｼ担当者
     *
     * @param btantosya the btantosya to set
     */
    public void setBtantosya(String btantosya) {
        this.btantosya = btantosya;
    }

    /**
     * 備考6
     *
     * @return the biko6
     */
    public String getBiko6() {
        return biko6;
    }

    /**
     * 備考6
     *
     * @param biko6 the biko6 to set
     */
    public void setBiko6(String biko6) {
        this.biko6 = biko6;
    }

    /**
     * 備考7
     *
     * @return the biko7
     */
    public String getBiko7() {
        return biko7;
    }

    /**
     * 備考7
     *
     * @param biko7 the biko7 to set
     */
    public void setBiko7(String biko7) {
        this.biko7 = biko7;
    }

    /**
     * 登録日時
     *
     * @return the torokunichiji
     */
    public Timestamp getTorokunichiji() {
        return torokunichiji;
    }

    /**
     * 登録日時
     *
     * @param torokunichiji the torokunichiji to set
     */
    public void setTorokunichiji(Timestamp torokunichiji) {
        this.torokunichiji = torokunichiji;
    }

    /**
     * 更新日時
     *
     * @return the kosinnichiji
     */
    public Timestamp getKosinnichiji() {
        return kosinnichiji;
    }

    /**
     * 更新日時
     *
     * @param kosinnichiji the kosinnichiji to set
     */
    public void setKosinnichiji(Timestamp kosinnichiji) {
        this.kosinnichiji = kosinnichiji;
    }

    /**
     * 再酸化号機
     *
     * @return the sankagoki
     */
    public String getSankagoki() {
        return sankagoki;
    }

    /**
     * 再酸化号機
     *
     * @param sankagoki the sankagoki to set
     */
    public void setSankagoki(String sankagoki) {
        this.sankagoki = sankagoki;
    }

    /**
     * 再酸化温度
     *
     * @return the sankaondo
     */
    public Long getSankaondo() {
        return sankaondo;
    }

    /**
     * 再酸化温度
     *
     * @param sankaondo the sankaondo to set
     */
    public void setSankaondo(Long sankaondo) {
        this.sankaondo = sankaondo;
    }

    /**
     * 再酸化終了日時
     *
     * @return the sankasyuryonichiji
     */
    public Timestamp getSankasyuryonichiji() {
        return sankasyuryonichiji;
    }

    /**
     * 再酸化終了日時
     *
     * @param sankasyuryonichiji the sankasyuryonichiji to set
     */
    public void setSankasyuryonichiji(Timestamp sankasyuryonichiji) {
        this.sankasyuryonichiji = sankasyuryonichiji;
    }

    /**
     * 投入ｾｯﾀ枚数
     *
     * @return the tounyusettasuu
     */
    public Integer getTounyusettasuu() {
        return tounyusettasuu;
    }

    /**
     * 投入ｾｯﾀ枚数
     *
     * @param tounyusettasuu the tounyusettasuu to set
     */
    public void setTounyusettasuu(Integer tounyusettasuu) {
        this.tounyusettasuu = tounyusettasuu;
    }

    /**
     * 焼成設定ﾊﾟﾀｰﾝﾁｪｯｸ
     *
     * @return the setteipattern
     */
    public Integer getSetteipattern() {
        return setteipattern;
    }

    /**
     * 焼成設定ﾊﾟﾀｰﾝﾁｪｯｸ
     *
     * @param setteipattern the setteipattern to set
     */
    public void setSetteipattern(Integer setteipattern) {
        this.setteipattern = setteipattern;
    }

    /**
     * 段数
     *
     * @return the dansuu
     */
    public Integer getDansuu() {
        return dansuu;
    }

    /**
     * 段数
     *
     * @param dansuu the dansuu to set
     */
    public void setDansuu(Integer dansuu) {
        this.dansuu = dansuu;
    }

    /**
     * 外観確認
     *
     * @return the gaikancheck
     */
    public Integer getGaikancheck() {
        return gaikancheck;
    }

    /**
     * 外観確認
     *
     * @param gaikancheck the gaikancheck to set
     */
    public void setGaikancheck(Integer gaikancheck) {
        this.gaikancheck = gaikancheck;
    }

    /**
     * 回収ｾｯﾀ枚数
     *
     * @return the kaishusettasuu
     */
    public Integer getKaishusettasuu() {
        return kaishusettasuu;
    }

    /**
     * 回収ｾｯﾀ枚数
     *
     * @param kaishusettasuu the kaishusettasuu to set
     */
    public void setKaishusettasuu(Integer kaishusettasuu) {
        this.kaishusettasuu = kaishusettasuu;
    }

    /**
     * 開始確認者
     *
     * @return the startkakuninsyacode
     */
    public String getStartkakuninsyacode() {
        return startkakuninsyacode;
    }

    /**
     * 開始確認者
     *
     * @param startkakuninsyacode the startkakuninsyacode to set
     */
    public void setStartkakuninsyacode(String startkakuninsyacode) {
        this.startkakuninsyacode = startkakuninsyacode;
    }


    /**
     * 2次脱脂号機
     *
     * @return the nijidasshigouki
     */
    public String getNijidasshigouki() {
        return nijidasshigouki;
    }

    /**
     * 2次脱脂号機
     *
     * @param nijidasshigouki the nijidasshigouki to set
     */
    public void setNijidasshigouki(String nijidasshigouki) {
        this.nijidasshigouki = nijidasshigouki;
    }

    /**
     * 2次脱脂設定ﾊﾟﾀｰﾝ
     *
     * @return the nijidasshisetteipt
     */
    public Integer getNijidasshisetteipt() {
        return nijidasshisetteipt;
    }

    /**
     * 2次脱脂設定ﾊﾟﾀｰﾝ
     *
     * @param nijidasshisetteipt the nijidasshisetteipt to set
     */
    public void setNijidasshisetteipt(Integer nijidasshisetteipt) {
        this.nijidasshisetteipt = nijidasshisetteipt;
    }

    /**
     * 2次脱脂ｷｰﾌﾟ温度
     *
     * @return the nijidasshikeepondo
     */
    public Integer getNijidasshikeepondo() {
        return nijidasshikeepondo;
    }

    /**
     * 2次脱脂ｷｰﾌﾟ温度
     *
     * @param nijidasshikeepondo the nijidasshikeepondo to set
     */
    public void setNijidasshikeepondo(Integer nijidasshikeepondo) {
        this.nijidasshikeepondo = nijidasshikeepondo;
    }

    /**
     * 2次脱脂ｺﾝﾍﾞｱ速度
     *
     * @return the nijidasshispeed
     */
    public Integer getNijidasshispeed() {
        return nijidasshispeed;
    }

    /**
     * 2次脱脂ｺﾝﾍﾞｱ速度
     *
     * @param nijidasshispeed the nijidasshispeed to set
     */
    public void setNijidasshispeed(Integer nijidasshispeed) {
        this.nijidasshispeed = nijidasshispeed;
    }

    /**
     * 焼成ﾋﾟｰｸ温度指示
     *
     * @return the peakondo
     */
    public String getPeakondo() {
        return peakondo;
    }

    /**
     * 焼成ﾋﾟｰｸ温度指示
     *
     * @param peakondo the peakondo to set
     */
    public void setPeakondo(String peakondo) {
        this.peakondo = peakondo;
    }

    /**
     * 焼成ﾛｰﾗｰ速度
     *
     * @return the syoseispeed
     */
    public Integer getSyoseispeed() {
        return syoseispeed;
    }

    /**
     * 焼成ﾛｰﾗｰ速度
     *
     * @param syoseispeed the syoseispeed to set
     */
    public void setSyoseispeed(Integer syoseispeed) {
        this.syoseispeed = syoseispeed;
    }

    /**
     * 焼成ﾊﾟｰｼﾞ
     *
     * @return the syoseipurge
     */
    public Integer getSyoseipurge() {
        return syoseipurge;
    }

    /**
     * 焼成ﾊﾟｰｼﾞ
     *
     * @param syoseipurge the syoseipurge to set
     */
    public void setSyoseipurge(Integer syoseipurge) {
        this.syoseipurge = syoseipurge;
    }

    /**
     * 1回目再酸化号機1
     *
     * @return the saisankagouki1
     */
    public String getSaisankagouki1() {
        return saisankagouki1;
    }

    /**
     * 1回目再酸化号機1
     *
     * @param saisankagouki1 the saisankagouki1 to set
     */
    public void setSaisankagouki1(String saisankagouki1) {
        this.saisankagouki1 = saisankagouki1;
    }

    /**
     * 1回目再酸化号機2
     *
     * @return the saisankagouki2
     */
    public String getSaisankagouki2() {
        return saisankagouki2;
    }

    /**
     * 1回目再酸化号機2
     *
     * @param saisankagouki2 the saisankagouki2 to set
     */
    public void setSaisankagouki2(String saisankagouki2) {
        this.saisankagouki2 = saisankagouki2;
    }

    /**
     * 1回目再酸化設定ﾊﾟﾀｰﾝ
     *
     * @return the saisankasetteipt
     */
    public Integer getSaisankasetteipt() {
        return saisankasetteipt;
    }

    /**
     * 1回目再酸化設定ﾊﾟﾀｰﾝ
     *
     * @param saisankasetteipt the saisankasetteipt to set
     */
    public void setSaisankasetteipt(Integer saisankasetteipt) {
        this.saisankasetteipt = saisankasetteipt;
    }

    /**
     * 1回目再酸化ｷｰﾌﾟ温度
     *
     * @return the saisankakeepondo
     */
    public Integer getSaisankakeepondo() {
        return saisankakeepondo;
    }

    /**
     * 1回目再酸化ｷｰﾌﾟ温度
     *
     * @param saisankakeepondo the saisankakeepondo to set
     */
    public void setSaisankakeepondo(Integer saisankakeepondo) {
        this.saisankakeepondo = saisankakeepondo;
    }

    /**
     * 1回目再酸化ｺﾝﾍﾞｱ速度
     *
     * @return the saisankacsokudo
     */
    public Integer getSaisankacsokudo() {
        return saisankacsokudo;
    }

    /**
     * 1回目再酸化ｺﾝﾍﾞｱ速度
     *
     * @param saisankacsokudo the saisankacsokudo to set
     */
    public void setSaisankacsokudo(Integer saisankacsokudo) {
        this.saisankacsokudo = saisankacsokudo;
    }

    /**
     * 1回目再酸化後外観
     *
     * @return the saisankagogaikan
     */
    public Integer getSaisankagogaikan() {
        return saisankagogaikan;
    }

    /**
     * 1回目再酸化後外観
     *
     * @param saisankagogaikan the saisankagogaikan to set
     */
    public void setSaisankagogaikan(Integer saisankagogaikan) {
        this.saisankagogaikan = saisankagogaikan;
    }

    /**
     * 焼成種類
     *
     * @return the syoseisyurui
     */
    public String getSyoseisyurui() {
        return syoseisyurui;
    }

    /**
     * 焼成種類
     *
     * @param syoseisyurui the syoseisyurui to set
     */
    public void setSyoseisyurui(String syoseisyurui) {
        this.syoseisyurui = syoseisyurui;
    }

    /**
     * revision
     *
     * @return the revision
     */
    public Long getRevision() {
        return revision;
    }

    /**
     * revision
     *
     * @param revision the revision to set
     */
    public void setRevision(Long revision) {
        this.revision = revision;
    }

    /**
     * 削除ﾌﾗｸﾞ
     *
     * @return the deleteflag
     */
    public Integer getDeleteflag() {
        return deleteflag;
    }

    /**
     * 削除ﾌﾗｸﾞ
     *
     * @param deleteflag the deleteflag to set
     */
    public void setDeleteflag(Integer deleteflag) {
        this.deleteflag = deleteflag;
    }

}
