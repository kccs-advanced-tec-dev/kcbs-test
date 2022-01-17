/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.util.ArrayList;
import java.util.List;
import jp.co.kccs.xhd.db.model.FXHDD01;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/10/18<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS wxf<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102C003Model(ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕入力サブ画面用)のモデルクラスです。
 *
 * @author KCSS wxf
 * @since 2021/10/18
 */
public class GXHDO102C003Model implements Cloneable {

    /**
     * クローン実装
     *
     * @return クローン
     * @throws java.lang.CloneNotSupportedException
     */
    @Override
    public GXHDO102C003Model clone() throws CloneNotSupportedException {
        return (GXHDO102C003Model) super.clone();
    }
    
    /**
     * 表示されるサブ画面名
     */
    private SubGamenData showsubgamendata;
    
    /**
     * 材料品名のﾘﾝｸから遷移したｻﾌﾞ画面用データ
     */
    private SubGamenData subgamen1;
    
    /**
     * コンストラクタ
     */
    public GXHDO102C003Model() {

    }
    
    /**
     * 表示されるサブ画面名
     * @return the showsubgamendata
     */
    public SubGamenData getShowsubgamendata() {
        return showsubgamendata;
    }

    /**
     * 表示されるサブ画面名
     * @param showsubgamendata the showsubgamendata to set
     */
    public void setShowsubgamendata(SubGamenData showsubgamendata) {
        this.showsubgamendata = showsubgamendata;
    }

    /**
     * 材料品名のﾘﾝｸから遷移したｻﾌﾞ画面用データ
     * @return the subgamen1
     */
    public SubGamenData getSubgamen1() {
        return subgamen1;
    }

    /**
     * 材料品名のﾘﾝｸから遷移したｻﾌﾞ画面用データ
     * @param subgamen1 the subgamen1 to set
     */
    public void setSubgamen1(SubGamenData subgamen1) {
        this.subgamen1 = subgamen1;
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕入力サブ画面データ
     */
    public class SubGamenData implements Cloneable {

        /**
         * クローン実装
         *
         * @return クローン
         * @throws java.lang.CloneNotSupportedException
         */
        @Override
        public SubGamenData clone() throws CloneNotSupportedException {
            SubGamenData cloneModel = (SubGamenData) super.clone();
            // サブ画面の部材タブデータリスト
            List<FXHDD01> subDataBuzaitabList = new ArrayList();

            // サブ画面の部材タブデータリストのClone処理
            for (FXHDD01 data : this.getSubDataBuzaitab()) {
                subDataBuzaitabList.add(data.clone());
            }
            cloneModel.setSubDataBuzaitab(subDataBuzaitabList);

            // サブ画面の調合規格データのClone処理
            cloneModel.setSubDataTyogouryoukikaku(this.getSubDataTyogouryoukikaku().clone());
            // サブ画面の調合残量データのClone処理
            cloneModel.setSubDataTyogouzanryou(this.getSubDataTyogouzanryou().clone());

            return cloneModel;
        }

        /**
         * 部材在庫No設定項目ID
         */
        private String returnItemIdBuzailotno = "";

        /**
         * 調合量設定項目ID
         */
        private String returnItemIdTyougouryou = "";

        /**
         * サブ画面の固定値「g」
         */
        private String subDataTani;

        /**
         * 主画面からサブ画面に渡された秤量号機データ
         */
        private String subDataGoki;

        /**
         * サブ画面の調合規格データ
         */
        private FXHDD01 subDataTyogouryoukikaku;
        
        /**
         * サブ画面の調合残量データ
         */
        private FXHDD01 subDataTyogouzanryou;

        /**
         * サブ画面の部材①タブデータリスト
         */
        private List<FXHDD01> subDataBuzaitab;

        /**
         * 部材在庫No設定項目ID
         * @return the returnItemIdBuzailotno
         */
        public String getReturnItemIdBuzailotno() {
            return returnItemIdBuzailotno;
        }

        /**
         * 部材在庫No設定項目ID
         * @param returnItemIdBuzailotno the returnItemIdBuzailotno to set
         */
        public void setReturnItemIdBuzailotno(String returnItemIdBuzailotno) {
            this.returnItemIdBuzailotno = returnItemIdBuzailotno;
        }

        /**
         * 調合量設定項目ID
         * @return the returnItemIdTyougouryou
         */
        public String getReturnItemIdTyougouryou() {
            return returnItemIdTyougouryou;
        }

        /**
         * 調合量設定項目ID
         * @param returnItemIdTyougouryou the returnItemIdTyougouryou to set
         */
        public void setReturnItemIdTyougouryou(String returnItemIdTyougouryou) {
            this.returnItemIdTyougouryou = returnItemIdTyougouryou;
        }

        /**
         * サブ画面の固定値「g」
         * @return the subDataTani
         */
        public String getSubDataTani() {
            return "g";
        }

        /**
         * サブ画面の固定値「g」
         * @param subDataTani the subDataTani to set
         */
        public void setSubDataTani(String subDataTani) {
            this.subDataTani = subDataTani;
        }

        /**
         * 主画面からサブ画面に渡された秤量号機データ
         * @return the subDataGoki
         */
        public String getSubDataGoki() {
            return subDataGoki;
        }

        /**
         * 主画面からサブ画面に渡された秤量号機データ
         * @param subDataGoki the subDataGoki to set
         */
        public void setSubDataGoki(String subDataGoki) {
            this.subDataGoki = subDataGoki;
        }

        /**
         * サブ画面の調合規格データ
         * @return the subDataTyogouryoukikaku
         */
        public FXHDD01 getSubDataTyogouryoukikaku() {
            return subDataTyogouryoukikaku;
        }

        /**
         * サブ画面の調合規格データ
         * @param subDataTyogouryoukikaku the subDataTyogouryoukikaku to set
         */
        public void setSubDataTyogouryoukikaku(FXHDD01 subDataTyogouryoukikaku) {
            this.subDataTyogouryoukikaku = subDataTyogouryoukikaku;
        }

        /**
         * サブ画面の調合残量データ
         * @return the subDataTyogouzanryou
         */
        public FXHDD01 getSubDataTyogouzanryou() {
            return subDataTyogouzanryou;
        }

        /**
         * サブ画面の調合残量データ
         * @param subDataTyogouzanryou the subDataTyogouzanryou to set
         */
        public void setSubDataTyogouzanryou(FXHDD01 subDataTyogouzanryou) {
            this.subDataTyogouzanryou = subDataTyogouzanryou;
        }

        /**
         * サブ画面の部材タブデータリスト
         * @return the subDataBuzaitab
         */
        public List<FXHDD01> getSubDataBuzaitab() {
            return subDataBuzaitab;
        }

        /**
         * サブ画面の部材タブデータリスト
         * @param subDataBuzaitab the subDataBuzaitab to set
         */
        public void setSubDataBuzaitab(List<FXHDD01> subDataBuzaitab) {
            this.subDataBuzaitab = subDataBuzaitab;
        }
    }
}
