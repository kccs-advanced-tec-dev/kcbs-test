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
 * 変更日	2018/05/06<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	KCCS D.Yanagida<br>
 * 変更理由	新規作成<br>
 * <br>
 * 変更日	2018/11/13<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	ﾛｯﾄｶｰﾄﾞ電子化対応<br>
 * <br>
 * ===============================================================================<br>
 */

/**
 * SR_SYOSEI(焼成データ)のモデルクラスです。
 * 
 * @author KCCS D.Yanagida
 * @since 2018/05/06
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
    private int jissekino;
    /**
     * KCPNo
     */
    private String kcpno;
    /**
     * 個数
     */
    private int kosuu;
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
    private int bprogramno;
    /**
     * 焼成温度
     */
    private int syoseiondo;
    /**
     * ﾄﾝﾈﾙ炉・ﾊﾞｯﾁ炉号機
     */
    private String goki;
    /**
     * 焼成ｾｯﾀｰ枚数
     */
    private int ssettermaisuu;
    /**
     * 入炉台板枚数
     */
    private BigDecimal nyurodaibanmaisuu;
    /**
     * 焼成開始担当者
     */
    private String skaisitantosya;
    /**
     * 焼成終了担当者
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
    private int bsettermaisuu;
    /**
     * ﾎﾟｯﾄ数
     */
    private int potsuu;
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
    private String sankaGoki;
    /**
     * 再酸化温度
     */
    private int sankaOndo;
    /**
     * 再酸化終了日時
     */
    private Timestamp sankaSyuryoNichiJi;

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
     * 実績No
     * @return the jissekino
     */
    public int getJissekino() {
        return jissekino;
    }

    /**
     * 実績No
     * @param jissekino the jissekino to set
     */
    public void setJissekino(int jissekino) {
        this.jissekino = jissekino;
    }

    /**
     * KCPNo
     * @return the kcpno
     */
    public String getKcpno() {
        return kcpno;
    }

    /**
     * KCPNo
     * @param kcpno the kcpno to set
     */
    public void setKcpno(String kcpno) {
        this.kcpno = kcpno;
    }

    /**
     * 個数
     * @return the kosuu
     */
    public int getKosuu() {
        return kosuu;
    }

    /**
     * 個数
     * @param kosuu the kosuu to set
     */
    public void setKosuu(int kosuu) {
        this.kosuu = kosuu;
    }

    /**
     * 原料品種名
     * @return the genryohinsyumei
     */
    public String getGenryohinsyumei() {
        return genryohinsyumei;
    }

    /**
     * 原料品種名
     * @param genryohinsyumei the genryohinsyumei to set
     */
    public void setGenryohinsyumei(String genryohinsyumei) {
        this.genryohinsyumei = genryohinsyumei;
    }

    /**
     * 原料ｸﾞﾙｰﾌﾟ
     * @return the genryogroup
     */
    public String getGenryogroup() {
        return genryogroup;
    }

    /**
     * 原料ｸﾞﾙｰﾌﾟ
     * @param genryogroup the genryogroup to set
     */
    public void setGenryogroup(String genryogroup) {
        this.genryogroup = genryogroup;
    }

    /**
     * 焼成開始日時
     * @return the skaisinichiji
     */
    public Timestamp getSkaisinichiji() {
        return skaisinichiji;
    }

    /**
     * 焼成開始日時
     * @param skaisinichiji the skaisinichiji to set
     */
    public void setSkaisinichiji(Timestamp skaisinichiji) {
        this.skaisinichiji = skaisinichiji;
    }

    /**
     * 焼成終了日時
     * @return the ssyuryonichiji
     */
    public Timestamp getSsyuryonichiji() {
        return ssyuryonichiji;
    }

    /**
     * 焼成終了日時
     * @param ssyuryonichiji the ssyuryonichiji to set
     */
    public void setSsyuryonichiji(Timestamp ssyuryonichiji) {
        this.ssyuryonichiji = ssyuryonichiji;
    }

    /**
     * ﾊﾞｯﾁ炉ﾌﾟﾛｸﾞﾗﾑNo
     * @return the bprogramno
     */
    public int getBprogramno() {
        return bprogramno;
    }

    /**
     * ﾊﾞｯﾁ炉ﾌﾟﾛｸﾞﾗﾑNo
     * @param bprogramno the bprogramno to set
     */
    public void setBprogramno(int bprogramno) {
        this.bprogramno = bprogramno;
    }

    /**
     * 焼成温度
     * @return the syoseiondo
     */
    public int getSyoseiondo() {
        return syoseiondo;
    }

    /**
     * 焼成温度
     * @param syoseiondo the syoseiondo to set
     */
    public void setSyoseiondo(int syoseiondo) {
        this.syoseiondo = syoseiondo;
    }

    /**
     * ﾄﾝﾈﾙ炉・ﾊﾞｯﾁ炉号機
     * @return the goki
     */
    public String getGoki() {
        return goki;
    }

    /**
     * ﾄﾝﾈﾙ炉・ﾊﾞｯﾁ炉号機
     * @param goki the goki to set
     */
    public void setGoki(String goki) {
        this.goki = goki;
    }

    /**
     * 焼成ｾｯﾀｰ枚数
     * @return the ssettermaisuu
     */
    public int getSsettermaisuu() {
        return ssettermaisuu;
    }

    /**
     * 焼成ｾｯﾀｰ枚数
     * @param ssettermaisuu the ssettermaisuu to set
     */
    public void setSsettermaisuu(int ssettermaisuu) {
        this.ssettermaisuu = ssettermaisuu;
    }

    /**
     * 入炉台板枚数
     * @return the nyurodaibanmaisuu
     */
    public BigDecimal getNyurodaibanmaisuu() {
        return nyurodaibanmaisuu;
    }

    /**
     * 入炉台板枚数
     * @param nyurodaibanmaisuu the nyurodaibanmaisuu to set
     */
    public void setNyurodaibanmaisuu(BigDecimal nyurodaibanmaisuu) {
        this.nyurodaibanmaisuu = nyurodaibanmaisuu;
    }

    /**
     * 焼成開始担当者
     * @return the skaisitantosya
     */
    public String getSkaisitantosya() {
        return skaisitantosya;
    }

    /**
     * 焼成開始担当者
     * @param skaisitantosya the skaisitantosya to set
     */
    public void setSkaisitantosya(String skaisitantosya) {
        this.skaisitantosya = skaisitantosya;
    }

    /**
     * 焼成終了担当者
     * @return the ssyuryotantosya
     */
    public String getSsyuryotantosya() {
        return ssyuryotantosya;
    }

    /**
     * 焼成終了担当者
     * @param ssyuryotantosya the ssyuryotantosya to set
     */
    public void setSsyuryotantosya(String ssyuryotantosya) {
        this.ssyuryotantosya = ssyuryotantosya;
    }

    /**
     * 備考1
     * @return the biko1
     */
    public String getBiko1() {
        return biko1;
    }

    /**
     * 備考1
     * @param biko1 the biko1 to set
     */
    public void setBiko1(String biko1) {
        this.biko1 = biko1;
    }

    /**
     * 備考2
     * @return the biko2
     */
    public String getBiko2() {
        return biko2;
    }

    /**
     * 備考2
     * @param biko2 the biko2 to set
     */
    public void setBiko2(String biko2) {
        this.biko2 = biko2;
    }

    /**
     * 備考3
     * @return the biko3
     */
    public String getBiko3() {
        return biko3;
    }

    /**
     * 備考3
     * @param biko3 the biko3 to set
     */
    public void setBiko3(String biko3) {
        this.biko3 = biko3;
    }

    /**
     * 備考4
     * @return the biko4
     */
    public String getBiko4() {
        return biko4;
    }

    /**
     * 備考4
     * @param biko4 the biko4 to set
     */
    public void setBiko4(String biko4) {
        this.biko4 = biko4;
    }

    /**
     * 備考5
     * @return the biko5
     */
    public String getBiko5() {
        return biko5;
    }

    /**
     * 備考5
     * @param biko5 the biko5 to set
     */
    public void setBiko5(String biko5) {
        this.biko5 = biko5;
    }

    /**
     * ﾊﾞﾗｼ開始日時
     * @return the bkaisinichiji
     */
    public Timestamp getBkaisinichiji() {
        return bkaisinichiji;
    }

    /**
     * ﾊﾞﾗｼ開始日時
     * @param bkaisinichiji the bkaisinichiji to set
     */
    public void setBkaisinichiji(Timestamp bkaisinichiji) {
        this.bkaisinichiji = bkaisinichiji;
    }

    /**
     * ﾊﾞﾗｼ終了日時
     * @return the bsyuryonichiji
     */
    public Timestamp getBsyuryonichiji() {
        return bsyuryonichiji;
    }

    /**
     * ﾊﾞﾗｼ終了日時
     * @param bsyuryonichiji the bsyuryonichiji to set
     */
    public void setBsyuryonichiji(Timestamp bsyuryonichiji) {
        this.bsyuryonichiji = bsyuryonichiji;
    }

    /**
     * ﾊﾞﾗｼｾｯﾀｰ枚数
     * @return the bsettermaisuu
     */
    public int getBsettermaisuu() {
        return bsettermaisuu;
    }

    /**
     * ﾊﾞﾗｼｾｯﾀｰ枚数
     * @param bsettermaisuu the bsettermaisuu to set
     */
    public void setBsettermaisuu(int bsettermaisuu) {
        this.bsettermaisuu = bsettermaisuu;
    }

    /**
     * ﾎﾟｯﾄ数
     * @return the potsuu
     */
    public int getPotsuu() {
        return potsuu;
    }

    /**
     * ﾎﾟｯﾄ数
     * @param potsuu the potsuu to set
     */
    public void setPotsuu(int potsuu) {
        this.potsuu = potsuu;
    }

    /**
     * ﾎﾟｯﾄNo
     * @return the potno
     */
    public String getPotno() {
        return potno;
    }

    /**
     * ﾎﾟｯﾄNo
     * @param potno the potno to set
     */
    public void setPotno(String potno) {
        this.potno = potno;
    }

    /**
     * ﾊﾞﾗｼ担当者
     * @return the btantosya
     */
    public String getBtantosya() {
        return btantosya;
    }

    /**
     * ﾊﾞﾗｼ担当者
     * @param btantosya the btantosya to set
     */
    public void setBtantosya(String btantosya) {
        this.btantosya = btantosya;
    }

    /**
     * 備考6
     * @return the biko6
     */
    public String getBiko6() {
        return biko6;
    }

    /**
     * 備考6
     * @param biko6 the biko6 to set
     */
    public void setBiko6(String biko6) {
        this.biko6 = biko6;
    }

    /**
     * 備考7
     * @return the biko7
     */
    public String getBiko7() {
        return biko7;
    }

    /**
     * 備考7
     * @param biko7 the biko7 to set
     */
    public void setBiko7(String biko7) {
        this.biko7 = biko7;
    }

    /**
     * 登録日時
     * @return the torokunichiji
     */
    public Timestamp getTorokunichiji() {
        return torokunichiji;
    }

    /**
     * 登録日時
     * @param torokunichiji the torokunichiji to set
     */
    public void setTorokunichiji(Timestamp torokunichiji) {
        this.torokunichiji = torokunichiji;
    }

    /**
     * 更新日時
     * @return the kosinnichiji
     */
    public Timestamp getKosinnichiji() {
        return kosinnichiji;
    }

    /**
     * 更新日時
     * @param kosinnichiji the kosinnichiji to set
     */
    public void setKosinnichiji(Timestamp kosinnichiji) {
        this.kosinnichiji = kosinnichiji;
    }

    /**
     * 再酸化号機
     * @return the SankaGoki
     */
    public String getSankaGoki() {
        return sankaGoki;
    }

    /**
     * 再酸化号機
     * @param sankaGoki the sankaGoki to set
     */
    public void setSankaGoki(String sankaGoki) {
        this.sankaGoki = sankaGoki;
    }

    /**
     * 再酸化温度
     * @return the SankaOndo
     */
    public int getSankaOndo() {
        return sankaOndo;
    }

    /**
     * 再酸化温度
     * @param sankaOndo the sankaOndo to set
     */
    public void setSankaOndo(int sankaOndo) {
        this.sankaOndo = sankaOndo;
    }

    /**
     * 再酸化終了日時
     * @return the SankaSyuryoNichiJi
     */
    public Timestamp getSankaSyuryoNichiJi() {
        return sankaSyuryoNichiJi;
    }

    /**
     * 再酸化終了日時
     * @param sankaSyuryoNichiJi the sankaSyuryoNichiJi to set
     */
    public void setSankaSyuryoNichiJi(Timestamp sankaSyuryoNichiJi) {
        this.sankaSyuryoNichiJi = sankaSyuryoNichiJi;
    }
}
