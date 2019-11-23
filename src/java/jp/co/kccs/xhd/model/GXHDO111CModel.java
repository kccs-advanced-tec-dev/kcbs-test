/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.util.ArrayList;
import java.util.List;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2018/12/04<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C001Model(膜厚(SPS)サブ画面用)のモデルクラスです。
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2018/12/04
 */
public class GXHDO111CModel implements Cloneable {

public GXHDO111CModel(){}
    public GXHDO111CModel(String itemName, String styleDisplay){
        this.itemName = itemName;
        this.styleDisplay = styleDisplay;
    
    }
    /**
     * クローン実装
     *
     * @return クローン
     * @throws java.lang.CloneNotSupportedException
     */
    @Override
    public GXHDO111CModel clone() throws CloneNotSupportedException {
        GXHDO111CModel cloneModel = (GXHDO111CModel) super.clone();
        return cloneModel;
    }
    
    
    /**
     * @return the itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * @param itemName the itemName to set
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * @return the styleDisplay
     */
    public String getStyleDisplay() {
        return styleDisplay;
    }

    /**
     * @param styleDisplay the styleDisplay to set
     */
    public void setStyleDisplay(String styleDisplay) {
        this.styleDisplay = styleDisplay;
    }
    
    
    private String itemName;
    
    private String styleDisplay;

}
