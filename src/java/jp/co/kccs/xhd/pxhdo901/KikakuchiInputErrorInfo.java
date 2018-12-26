/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo901;

/**
 *
 * @author koksk-desk2
 */
public class KikakuchiInputErrorInfo implements Cloneable {

    /**
     * クローン実装
     *
     * @return クローン
     * @throws java.lang.CloneNotSupportedException
     */
    @Override
    public KikakuchiInputErrorInfo clone() throws CloneNotSupportedException {
        KikakuchiInputErrorInfo cloneModel = (KikakuchiInputErrorInfo) super.clone();
        return cloneModel;
    }

    /**
     * コンストラクタ
     */
    public KikakuchiInputErrorInfo() {

    }

    /**
     * コンストラクタ
     *
     * @param itemId 項目ID
     * @param itemLabel 項目ラベル
     * @param itemKikakuchi 項目規格値
     * @param itemIndex 項目インデックス
     * @param itemInputValue 項目値
     */
    public KikakuchiInputErrorInfo(String itemId, String itemLabel, String itemKikakuchi, int itemIndex, String itemInputValue) {
        this.itemId = itemId;
        this.itemLabel = itemLabel;
        this.itemKikakuchi = itemKikakuchi;
        this.itemIndex = itemIndex;
        this.itemInputValue = itemInputValue;
    }

    /**
     * 項目ID
     */
    private String itemId;

    /**
     * 項目ラベル(項目名)
     */
    private String itemLabel;

    /**
     * 項目規格値
     */
    private String itemKikakuchi;

    /**
     * 項目インデックス
     */
    private int itemIndex;

    /**
     * 入力値
     */
    private String itemInputValue;

    /**
     * 項目ID
     *
     * @return the itemId
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * 項目ID
     *
     * @param itemId the itemId to set
     */
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    /**
     * 項目ラベル(項目名)
     *
     * @return the itemLabel
     */
    public String getItemLabel() {
        return itemLabel;
    }

    /**
     * 項目ラベル(項目名)
     *
     * @param itemLabel the itemLabel to set
     */
    public void setItemLabel(String itemLabel) {
        this.itemLabel = itemLabel;
    }

    /**
     * 項目規格値
     *
     * @return the itemKikakuchi
     */
    public String getItemKikakuchi() {
        return itemKikakuchi;
    }

    /**
     * 項目規格値
     *
     * @param itemKikakuchi the itemKikakuchi to set
     */
    public void setItemKikakuchi(String itemKikakuchi) {
        this.itemKikakuchi = itemKikakuchi;
    }

    /**
     * 項目インデックス
     *
     * @return the itemIndex
     */
    public int getItemIndex() {
        return itemIndex;
    }

    /**
     * 項目インデックス
     *
     * @param itemIndex the itemIndex to set
     */
    public void setItemIndex(int itemIndex) {
        this.itemIndex = itemIndex;
    }

    /**
     * 項目入力値
     *
     * @return the itemInputValue
     */
    public String getItemInputValue() {
        return itemInputValue;
    }

    /**
     * 項目入力値
     *
     * @param itemInputValue the itemInputValue to set
     */
    public void setItemInputValue(String itemInputValue) {
        this.itemInputValue = itemInputValue;
    }

}
