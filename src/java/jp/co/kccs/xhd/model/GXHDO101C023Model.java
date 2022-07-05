/*
 * Copyright 2022 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.util.ArrayList;
import java.util.List;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2022/06/20<br>
 * 計画書No	MB2205-D010<br>
 * 変更者	KCSS wxf<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C023Model(ｽﾞﾚ値サブ画面用)のモデルクラスです。
 *
 * @author KCSS wxf
 * @since 2022/06/20
 */
public class GXHDO101C023Model implements Cloneable {

    /**
     * クローン実装
     *
     * @return クローン
     * @throws java.lang.CloneNotSupportedException
     */
    @Override
    public GXHDO101C023Model clone() throws CloneNotSupportedException {
        GXHDO101C023Model cloneModel = (GXHDO101C023Model) super.clone();
        List<GXHDO101C023Model.ZurechiData> newList = new ArrayList();
        for (GXHDO101C023Model.ZurechiData data : this.zurechiInputDataList) {
            GXHDO101C023Model.ZurechiData newData = new GXHDO101C023Model.ZurechiData();
            newData.setZurechiVal(data.getZurechiVal());
            newData.setZurechiTextRendered(data.isZurechiTextRendered());
            newData.setZurechiTextMaxLength(data.getZurechiTextMaxLength());
            newData.setZurechiTextBackColor(data.getZurechiTextBackColor());
            newData.setZurechiTabIndex(data.getZurechiTabIndex());
            newData.setZurechiIndex(data.getZurechiIndex());
            newData.setZurechiIndexName(data.getZurechiIndexName());
            newList.add(newData);
        }

        cloneModel.setZurechiInputDataList(newList);
        return cloneModel;
    }

    /**
     * ｽﾞﾚ値設定項目ID
     */
    private String returnItemIdZurechi = "";

    
    /**
     * ｽﾞﾚ値データリスト
     */
    private List<ZurechiData> zurechiInputDataList = new ArrayList<>();
    
    /**
     * コンストラクタ
     */
    public GXHDO101C023Model() {
        this.zurechiInputDataList = new ArrayList<>();
    }

    /**
     * @return the returnItemIdZurechi
     */
    public String getReturnItemIdZurechi() {
        return returnItemIdZurechi;
    }

    /**
     * @param returnItemIdZurechi the returnItemIdZurechi to set
     */
    public void setReturnItemIdZurechi(String returnItemIdZurechi) {
        this.returnItemIdZurechi = returnItemIdZurechi;
    }

    /**
     * ｽﾞﾚ値データリスト
     *
     * @return the zurechiInputDataList
     */
    public List<ZurechiData> getZurechiInputDataList() {
        return zurechiInputDataList;
    }

    /**
     * ｽﾞﾚ値データリスト
     *
     * @param zurechiInputDataList the zurechiInputDataList to set
     */
    public void setZurechiInputDataList(List<ZurechiData> zurechiInputDataList) {
        this.zurechiInputDataList = zurechiInputDataList;
    }

    /**
     * ｽﾞﾚ値データ
     */
    public class ZurechiData {

        /**
         * ｽﾞﾚ値項目Index
         */
        private String zurechiIndex;

        /**
         * ｽﾞﾚ値項目IndexName
         */
        private String zurechiIndexName;

        /**
         * ｽﾞﾚ値項目(値)
         */
        private String zurechiVal;

        /**
         * ｽﾞﾚ値項目_テキスト(Rendered)
         */
        private boolean zurechiTextRendered;

        /**
         * ｽﾞﾚ値項目_テキスト(MaxLength)
         */
        private String zurechiTextMaxLength;

        /**
         * ｽﾞﾚ値項目_テキスト(BackGround)
         */
        private String zurechiTextBackColor;
       
        /**
         * ｽﾞﾚ値項目_タブインデックス(TabIndex)
         */
        private String zurechiTabIndex;

        /**
         * ｽﾞﾚ値項目Index
         * @return the zurechiIndex
         */
        public String getZurechiIndex() {
            return zurechiIndex;
        }

        /**
         * ｽﾞﾚ値項目Index
         * @param zurechiIndex the zurechiIndex to set
         */
        public void setZurechiIndex(String zurechiIndex) {
            this.zurechiIndex = zurechiIndex;
        }

        /**
         * ｽﾞﾚ値項目IndexName
         * @return the zurechiIndexName
         */
        public String getZurechiIndexName() {
            return zurechiIndexName;
        }

        /**
         * ｽﾞﾚ値項目IndexName
         * @param zurechiIndexName the zurechiIndexName to set
         */
        public void setZurechiIndexName(String zurechiIndexName) {
            this.zurechiIndexName = zurechiIndexName;
        }

        /**
         * ｽﾞﾚ値項目(値)
         *
         * @return the zurechiVal
         */
        public String getZurechiVal() {
            return zurechiVal;
        }

        /**
         * ｽﾞﾚ値項目(値)
         *
         * @param zurechiVal the zurechiVal to set
         */
        public void setZurechiVal(String zurechiVal) {
            this.zurechiVal = zurechiVal;
        }

        /**
         * ｽﾞﾚ値項目_テキスト(Rendered)
         *
         * @return the zurechiTextRendered
         */
        public boolean isZurechiTextRendered() {
            return zurechiTextRendered;
        }

        /**
         * ｽﾞﾚ値項目_テキスト(Rendered)
         *
         * @param zurechiTextRendered the zurechiTextRendered to set
         */
        public void setZurechiTextRendered(boolean zurechiTextRendered) {
            this.zurechiTextRendered = zurechiTextRendered;
        }

        /**
         * ｽﾞﾚ値項目_テキスト(MaxLength)
         *
         * @return the zurechiTextMaxLength
         */
        public String getZurechiTextMaxLength() {
            return zurechiTextMaxLength;
        }

        /**
         * ｽﾞﾚ値項目_テキスト(MaxLength)
         *
         * @param zurechiTextMaxLength the zurechiTextMaxLength to set
         */
        public void setZurechiTextMaxLength(String zurechiTextMaxLength) {
            this.zurechiTextMaxLength = zurechiTextMaxLength;
        }

        /**
         * ｽﾞﾚ値項目_テキスト(BackGround)
         *
         * @return the zurechiTextBackColor
         */
        public String getZurechiTextBackColor() {
            return zurechiTextBackColor;
        }

        /**
         * ｽﾞﾚ値項目_テキスト(BackGround)
         *
         * @param zurechiTextBackColor the zurechiTextBackColor to set
         */
        public void setZurechiTextBackColor(String zurechiTextBackColor) {
            this.zurechiTextBackColor = zurechiTextBackColor;
        }

        /**
         * ｽﾞﾚ値項目_タブインデックス(TabIndex)
         * 
         * @return the zurechiTabIndex
         */
        public String getZurechiTabIndex() {
            return zurechiTabIndex;
        }

        /**
         * ｽﾞﾚ値項目_タブインデックス(TabIndex)
         * 
         * @param zurechiTabIndex the zurechiTabIndex to set
         */
        public void setZurechiTabIndex(String zurechiTabIndex) {
            this.zurechiTabIndex = zurechiTabIndex;
        }
    }
}
