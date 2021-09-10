/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.util.ArrayList;
import java.util.List;

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
        List<TorokuNoData> newList = new ArrayList<>();
        for (TorokuNoData data : this.getTorokuNoDataList()) {
            TorokuNoData newData = new TorokuNoData();
            newData.setTorokuNo(data.getTorokuNo());
            newList.add(newData);
        }

        cloneModel.setTorokuNoDataList(newList);
        return cloneModel;
    }

    private List<TorokuNoData> torokuNoDataList = new ArrayList<>();

    public GXHDO101C021Model() {
    }

    /**
     * @return the torokuNoDataList
     */
    public List<TorokuNoData> getTorokuNoDataList() {
        return torokuNoDataList;
    }

    /**
     * @param torokuNoDataList the torokuNoDataList to set
     */
    public void setTorokuNoDataList(List<TorokuNoData> torokuNoDataList) {
        this.torokuNoDataList = torokuNoDataList;
    }

    /**
     * 登録Noデータ
     */
    public class TorokuNoData {

        /**
         * 登録No
         */
        private String torokuNo;

        /**
         * @return the torokuNo
         */
        public String getTorokuNo() {
            return torokuNo;
        }

        /**
         * @param torokuNo the torokuNo to set
         */
        public void setTorokuNo(String torokuNo) {
            this.torokuNo = torokuNo;
        }
    }

}
