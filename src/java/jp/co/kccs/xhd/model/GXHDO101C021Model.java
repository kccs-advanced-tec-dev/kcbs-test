/*
 * Copyright 2020 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.util.ArrayList;
import java.util.List;
import jp.co.kccs.xhd.db.model.SrKoteifuryo;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2021/08/10<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	SRC K.Ijuin<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C021Model(B･Cﾗﾝｸ連絡書一覧画面用)のモデルクラスです。
 *
 * @author SRC K.Ijuin
 * @since 2021/08/10
 */
public class GXHDO101C021Model implements Cloneable {
    
    /**
     * クローン実装
     *
     * @return クローン
     * @throws java.lang.CloneNotSupportedException
     */
    @Override
    public GXHDO101C021Model clone() throws CloneNotSupportedException {
        GXHDO101C021Model cloneModel = (GXHDO101C021Model) super.clone();
        List<SrKoteifuryo> newList = new ArrayList<>();
        for (SrKoteifuryo data : this.getSrKoteifuryoList()) {
            SrKoteifuryo newData = new SrKoteifuryo();
            newData.setTorokuno(data.getTorokuno());
            newList.add(newData);
        }
        
        cloneModel.setSrKoteifuryoList(newList);
        return cloneModel;
    }
    
    private List<SrKoteifuryo> srKoteifuryoList;
    
    public GXHDO101C021Model(){
//        this.srKoteifuryoList = new ArrayList<>();
    }
    
    /**
     * @return the srKoteifuryoList
     */
    public List<SrKoteifuryo> getSrKoteifuryoList() {
        return srKoteifuryoList;
    }
    
    /**
     * @param srKoteifuryoList the srKoteifuryoList to set
     */
    public void setSrKoteifuryoList(List<SrKoteifuryo> srKoteifuryoList) {
        this.srKoteifuryoList = srKoteifuryoList;
    }

}
