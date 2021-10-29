/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.util.ArrayList;
import java.util.List;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/10/08<br>
 * 計画書No	MB2109-DK002<br>
 * 変更者	SRC T.Ushiyama<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C022Model(ﾃｰﾋﾟﾝｸﾞ号機選択画面用)のモデルクラスです。
 * 
 * @author SRC T.Ushiyama
 * @since 2021/10/08
 */
public class GXHDO101C022Model implements Cloneable {

    /**
     * クローン実装
     *
     * @return クローン
     * @throws java.lang.CloneNotSupportedException
     */
    @Override
    public GXHDO101C022Model clone() throws CloneNotSupportedException {
        GXHDO101C022Model cloneModel = (GXHDO101C022Model) super.clone();
        
        List<GokiSentakuData> newList = new ArrayList();
        for (GokiSentakuData data : this.getGokiSentakuDataList()) {
            GokiSentakuData newData = new GokiSentakuData();
            newData.setDispgoki(data.getDispgoki());
            newData.setBango(data.getBango());
            newData.setGoki(data.getGoki());
            newData.setJotai(data.getJotai());
            newData.setRev(data.getRev());
            newList.add(newData);
        }

        cloneModel.setGokiSentakuDataList(newList);
        return cloneModel;
    }
    
    /**
     * 号機選択データリスト
     */
    private List<GokiSentakuData> gokiSentakuDataList;

    /**
     * 号機選択データリスト
     *
     * @return the gokiSentakuDataList
     */
    public List<GokiSentakuData> getGokiSentakuDataList() {
        return gokiSentakuDataList;
    }

    /**
     * 号機選択データリスト
     *
     * @param gokiSentakuDataList the gokiSentakuDataList to set
     */
    public void setGokiSentakuDataList(List<GokiSentakuData> gokiSentakuDataList) {
        this.gokiSentakuDataList = gokiSentakuDataList;
    }
    
    /**
     * コンストラクタ
     */
    public GXHDO101C022Model() {

    }
    
    /**
     * 原料ロットデータ
     */
    public class GokiSentakuData {

        /**
         * 表示号機
         */
        private String dispgoki;

        /**
         * 番号
         */
        private int bango;

        /**
         * 号機ｺｰﾄﾞ
         */
        private String goki;
        
        /**
         * 状態ﾌﾗｸﾞ
         */
        private String jotai;

        /**
         * REV
         */
        private int rev;

        /**
         * 表示号機
         *
         * @return the dispgoki
         */
        public String getDispgoki() {
            return dispgoki;
        }

        /**
         * 表示号機
         *
         * @param dispgoki the dispgoki to set
         */
        public void setDispgoki(String dispgoki) {
            this.dispgoki = dispgoki;
        }

        /**
         * 番号
         * 
         * @return 
         */
        public int getBango() {
            return bango;
        }

        /**
         * 番号
         * 
         * @param bango 
         */
        public void setBango(int bango) {
            this.bango = bango;
        }

        /**
         * 号機ｺｰﾄﾞ
         *
         * @return the goki
         */
        public String getGoki() {
            return goki;
        }

        /**
         * 号機ｺｰﾄﾞ
         *
         * @param goki the goki to set
         */
        public void setGoki(String goki) {
            this.goki = goki;
        }

        /**
         * 状態ﾌﾗｸﾞ
         *
         * @return the jotai
         */
        public String getJotai() {
            return jotai;
        }

        /**
         * 状態ﾌﾗｸﾞ
         *
         * @param jotai the jotai to set
         */
        public void setJotai(String jotai) {
            this.jotai = jotai;
        }

        /**
         * REV
         *
         * @return the rev
         */
        public int getRev() {
            return rev;
        }

        /**
         * REV
         *
         * @param rev the rev to set
         */
        public void setRev(int rev) {
            this.rev = rev;
        }
    }
}
