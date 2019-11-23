/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo111;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;
import jp.co.kccs.xhd.model.GXHDO101C001Model;
import jp.co.kccs.xhd.model.GXHDO111CModel;
import jp.co.kccs.xhd.pxhdo901.GXHDO901A;
import jp.co.kccs.xhd.util.DBUtil;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.NumberUtil;
import jp.co.kccs.xhd.util.StringUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.primefaces.context.RequestContext;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2018/12/08<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C001(膜厚(SPS))
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2018/11/13
 */
@ManagedBean(name = "beanGXHDO111C")
@SessionScoped
public class GXHDO111C implements Serializable {

    /**
     * 項目入力
     */
    private String itemInput = "";

    /**
     * 項目入力
     */
    private String itemInputOld = "";

    /**
     * 項目入力
     */
    private GXHDO111CModel selectItem;

    /**
     * 項目リスト
     */
    private List<GXHDO111CModel> itemList = new ArrayList<>(Arrays.asList(
            new GXHDO111CModel("AAAAA", ""), new GXHDO111CModel("AAABB", ""), new GXHDO111CModel("AAACC", ""), new GXHDO111CModel("AAADD", ""), new GXHDO111CModel("AAAEE", ""),
            new GXHDO111CModel("AAAFF", ""), new GXHDO111CModel("AAAGG", ""), new GXHDO111CModel("AAAHH", ""), new GXHDO111CModel("AAAII", ""), new GXHDO111CModel("AAAJJ", ""),
            new GXHDO111CModel("AABAA", ""), new GXHDO111CModel("AABBB", ""), new GXHDO111CModel("AABCC", ""), new GXHDO111CModel("AABDD", ""), new GXHDO111CModel("AABEE", ""),
            new GXHDO111CModel("AABFF", ""), new GXHDO111CModel("AABGG", ""), new GXHDO111CModel("AABHH", ""), new GXHDO111CModel("AABII", ""), new GXHDO111CModel("AABJJ", "")
    ));

    /**
     * 項目入力
     *
     * @return the itemInput
     */
    public String getItemInput() {
        return itemInput;
    }

    /**
     * 項目入力
     *
     * @param itemInput the itemInput to set
     */
    public void setItemInput(String itemInput) {
        this.itemInput = itemInput.trim();
    }

    /**
     * 項目リスト
     *
     * @return the itemList
     */
    public List<GXHDO111CModel> getItemList() {
        return itemList;
    }

    /**
     * 項目リスト
     *
     * @param itemList the itemList to set
     */
    public void setItemList(List<GXHDO111CModel> itemList) {
        this.itemList = itemList;
    }

    /**
     * QC工程ダイアログ内にて選択したQC工程を取得します。
     *
     * @return プロセス情報
     * @author KCCS fujimura
     * @since 2017/02/18
     */
    public GXHDO111CModel getSelectItem() {
        return selectItem;
    }

    /**
     * QC工程ダイアログ内にて選択したQC工程を設定します。
     *
     * @param selectItem 選択項目
     * @author KCCS fujimura
     * @since 2017/02/18
     */
    public void setSelectItem(GXHDO111CModel selectItem) {
        this.selectItem = selectItem;

    }

    /**
     * コンストラクタ
     */
    public GXHDO111C() {
    }

    /**
     * OKボタン押下時のチェック処理を行う。
     */
    public void doOk() {
        String aaa = "";
    }

    /**
     * OKボタンチェック処理
     *
     * @return 正常:true、異常:fasle
     */
    private boolean checkOK() {

        return true;
    }

    /**
     * OKボタン押下時のチェック処理を行う。
     */
    public void doChangeInputKomoku() {
        
        if(this.itemInput.equals(this.itemInputOld)){
            return;
        }
        List<GXHDO111CModel> selectData
                = this.itemList.stream().filter(n -> n.getItemName().startsWith(itemInput)).collect(Collectors.toList());

        for (Iterator<GXHDO111CModel> iterator = selectData.iterator(); iterator.hasNext();) {
            GXHDO111CModel next = iterator.next();
            next.setStyleDisplay("");
        }

        List<GXHDO111CModel> unselectData
                = this.itemList.stream().filter(n -> !n.getItemName().startsWith(itemInput)).collect(Collectors.toList());
        for (Iterator<GXHDO111CModel> iterator = unselectData.iterator(); iterator.hasNext();) {
            GXHDO111CModel next = iterator.next();
            next.setStyleDisplay("display:none;");
        }
        
        this.itemInputOld = this.itemInput;

    }

}
