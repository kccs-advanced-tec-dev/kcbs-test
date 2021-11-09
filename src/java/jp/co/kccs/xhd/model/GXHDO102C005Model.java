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
 * 変更日	2021/10/15<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS wxf<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102C005Model(添加材ｽﾗﾘｰ作製・溶剤調合入力サブ画面用)のモデルクラスです。
 *
 * @author KCSS wxf
 * @since 2021/10/15
 */
public class GXHDO102C005Model implements Cloneable {

    /**
     * クローン実装
     *
     * @return クローン
     * @throws java.lang.CloneNotSupportedException
     */
    @Override
    public GXHDO102C005Model clone() throws CloneNotSupportedException {
        return (GXHDO102C005Model) super.clone();
    }
    
    /**
     * 表示されるサブ画面名
     */
    private SubGamenData showsubgamendata;
    
    /**
     * 分散材1_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面用データ
     */
    private SubGamenData subgamen1;
    
    /**
     * 分散材2_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面用データ
     */
    private SubGamenData subgamen2;
    
    /**
     * 溶剤1_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面用データ
     */
    private SubGamenData subgamen3;
    
    /**
     * 溶剤2_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面用データ
     */
    private SubGamenData subgamen4;
    
    /**
     * 溶剤3_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面用データ
     */
    private SubGamenData subgamen5;
    
    /**
     * 溶剤4_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面用データ
     */
    private SubGamenData subgamen6;
    
    /**
     * ｶﾞﾗｽｽﾗﾘｰ品名のﾘﾝｸから遷移したｻﾌﾞ画面用データ
     */
    private SubGamenData subgamen7;
    
    /**
     * コンストラクタ
     */
    public GXHDO102C005Model() {

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
     * 分散材1_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面用データ
     * @return the subgamen1
     */
    public SubGamenData getSubgamen1() {
        return subgamen1;
    }

    /**
     * 分散材1_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面用データ
     * @param subgamen1 the subgamen1 to set
     */
    public void setSubgamen1(SubGamenData subgamen1) {
        this.subgamen1 = subgamen1;
    }

    /**
     * 分散材2_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面用データ
     * @return the subgamen2
     */
    public SubGamenData getSubgamen2() {
        return subgamen2;
    }

    /**
     * 分散材2_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面用データ
     * @param subgamen2 the subgamen2 to set
     */
    public void setSubgamen2(SubGamenData subgamen2) {
        this.subgamen2 = subgamen2;
    }

    /**
     * 溶剤1_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面用データ
     * @return the subgamen3
     */
    public SubGamenData getSubgamen3() {
        return subgamen3;
    }

    /**
     * 溶剤1_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面用データ
     * @param subgamen3 the subgamen3 to set
     */
    public void setSubgamen3(SubGamenData subgamen3) {
        this.subgamen3 = subgamen3;
    }

    /**
     * 溶剤2_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面用データ
     * @return the subgamen4
     */
    public SubGamenData getSubgamen4() {
        return subgamen4;
    }

    /**
     * 溶剤2_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面用データ
     * @param subgamen4 the subgamen4 to set
     */
    public void setSubgamen4(SubGamenData subgamen4) {
        this.subgamen4 = subgamen4;
    }

    /**
     * 溶剤3_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面用データ
     * @return the subgamen5
     */
    public SubGamenData getSubgamen5() {
        return subgamen5;
    }

    /**
     * 溶剤3_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面用データ
     * @param subgamen5 the subgamen5 to set
     */
    public void setSubgamen5(SubGamenData subgamen5) {
        this.subgamen5 = subgamen5;
    }

    /**
     * 溶剤4_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面用データ
     * @return the subgamen6
     */
    public SubGamenData getSubgamen6() {
        return subgamen6;
    }

    /**
     * 溶剤4_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面用データ
     * @param subgamen6 the subgamen6 to set
     */
    public void setSubgamen6(SubGamenData subgamen6) {
        this.subgamen6 = subgamen6;
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ品名のﾘﾝｸから遷移したｻﾌﾞ画面用データ
     * @return the subgamen7
     */
    public SubGamenData getSubgamen7() {
        return subgamen7;
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ品名のﾘﾝｸから遷移したｻﾌﾞ画面用データ
     * @param subgamen7 the subgamen7 to set
     */
    public void setSubgamen7(SubGamenData subgamen7) {
        this.subgamen7 = subgamen7;
    }
    
    /**
     * 添加材ｽﾗﾘｰ作製・溶剤調合入力サブ画面データ
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
            // サブ画面の部材①タブデータリスト
            List<FXHDD01> subDataBuzaitab1List = new ArrayList();
            // サブ画面の部材②タブデータリスト
            List<FXHDD01> subDataBuzaitab2List = new ArrayList();

            // サブ画面の部材①タブデータリストのClone処理
            for (FXHDD01 data : this.getSubDataBuzaitab1()) {
                subDataBuzaitab1List.add(data.clone());
            }
            cloneModel.setSubDataBuzaitab1(subDataBuzaitab1List);

            // サブ画面の部材②タブデータリストのClone処理
            for (FXHDD01 data : this.getSubDataBuzaitab2()) {
                subDataBuzaitab2List.add(data.clone());
            }
            cloneModel.setSubDataBuzaitab2(subDataBuzaitab2List);
            // サブ画面の調合規格データのClone処理
            cloneModel.setSubDataTyogouryoukikaku(this.getSubDataTyogouryoukikaku().clone());
            // サブ画面の調合残量データのClone処理
            cloneModel.setSubDataTyogouzanryou(this.getSubDataTyogouzanryou().clone());

            return cloneModel;
        }

        /**
         * 部材在庫No1設定項目ID
         */
        private String returnItemIdBuzailotno1 = "";

        /**
         * 調合量1設定項目ID
         */
        private String returnItemIdTyougouryou1 = "";

        /**
         * 部材在庫No2設定項目ID
         */
        private String returnItemIdBuzailotno2 = "";

        /**
         * 調合量2設定項目ID
         */
        private String returnItemIdTyougouryou2 = "";

        /**
         * サブ画面の材料区分
         */
        private Integer subDataZairyokubun;

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
        private List<FXHDD01> subDataBuzaitab1;

        /**
         * サブ画面の部材②タブデータリスト
         */
        private List<FXHDD01> subDataBuzaitab2;

        /**
         * 部材在庫No1設定項目ID
         * @return the returnItemIdBuzailotno1
         */
        public String getReturnItemIdBuzailotno1() {
            return returnItemIdBuzailotno1;
        }

        /**
         * 部材在庫No1設定項目ID
         * @param returnItemIdBuzailotno1 the returnItemIdBuzailotno1 to set
         */
        public void setReturnItemIdBuzailotno1(String returnItemIdBuzailotno1) {
            this.returnItemIdBuzailotno1 = returnItemIdBuzailotno1;
        }

        /**
         * 調合量1設定項目ID
         * @return the returnItemIdTyougouryou1
         */
        public String getReturnItemIdTyougouryou1() {
            return returnItemIdTyougouryou1;
        }

        /**
         * 調合量1設定項目ID
         * @param returnItemIdTyougouryou1 the returnItemIdTyougouryou1 to set
         */
        public void setReturnItemIdTyougouryou1(String returnItemIdTyougouryou1) {
            this.returnItemIdTyougouryou1 = returnItemIdTyougouryou1;
        }

        /**
         * 部材在庫No2設定項目ID
         * @return the returnItemIdBuzailotno2
         */
        public String getReturnItemIdBuzailotno2() {
            return returnItemIdBuzailotno2;
        }

        /**
         * 部材在庫No2設定項目ID
         * @param returnItemIdBuzailotno2 the returnItemIdBuzailotno2 to set
         */
        public void setReturnItemIdBuzailotno2(String returnItemIdBuzailotno2) {
            this.returnItemIdBuzailotno2 = returnItemIdBuzailotno2;
        }

        /**
         * 調合量2設定項目ID
         * @return the returnItemIdTyougouryou2
         */
        public String getReturnItemIdTyougouryou2() {
            return returnItemIdTyougouryou2;
        }

        /**
         * 調合量2設定項目ID
         * @param returnItemIdTyougouryou2 the returnItemIdTyougouryou2 to set
         */
        public void setReturnItemIdTyougouryou2(String returnItemIdTyougouryou2) {
            this.returnItemIdTyougouryou2 = returnItemIdTyougouryou2;
        }

        /**
         * サブ画面の材料区分
         * @return the subDataZairyokubun
         */
        public Integer getSubDataZairyokubun() {
            return subDataZairyokubun;
        }

        /**
         * サブ画面の材料区分
         * @param subDataZairyokubun the subDataZairyokubun to set
         */
        public void setSubDataZairyokubun(Integer subDataZairyokubun) {
            this.subDataZairyokubun = subDataZairyokubun;
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
         * サブ画面の部材①タブデータリスト
         * @return the subDataBuzaitab1
         */
        public List<FXHDD01> getSubDataBuzaitab1() {
            return subDataBuzaitab1;
        }

        /**
         * サブ画面の部材①タブデータリスト
         * @param subDataBuzaitab1 the subDataBuzaitab1 to set
         */
        public void setSubDataBuzaitab1(List<FXHDD01> subDataBuzaitab1) {
            this.subDataBuzaitab1 = subDataBuzaitab1;
        }

        /**
         * サブ画面の部材②タブデータリスト
         * @return the subDataBuzaitab2
         */
        public List<FXHDD01> getSubDataBuzaitab2() {
            return subDataBuzaitab2;
        }

        /**
         * サブ画面の部材②タブデータリスト
         * @param subDataBuzaitab2 the subDataBuzaitab2 to set
         */
        public void setSubDataBuzaitab2(List<FXHDD01> subDataBuzaitab2) {
            this.subDataBuzaitab2 = subDataBuzaitab2;
        }
    }
}
