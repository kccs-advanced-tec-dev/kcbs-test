/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.common;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;


/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2021/07/21<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS GC<br>
 * 変更理由	新規作成<br>
 * ===============================================================================<br>
 */
/**
 * ResultMessage(情報表示メッセージ)
 *
 * @author KCSS GC
 * @since  2021/07/21
 */
@ManagedBean(name = "beanResultMessage")
@SessionScoped
public class ResultMessage implements Serializable {
    
     /**
     * 情報表示メッセージリスト
     */
    private List<String> resultMessageList;
    /**
     * コンストラクタ
     */
    public ResultMessage() {
    }
    
    /**
     * 情報表示メッセージリスト
     *
     * @return the initMessageList
     */
    public List<String> getResultMessageList() {
        return resultMessageList;
    }

    /**
     * 情報表示メッセージリスト
     *
     * @param resultMessageList the resultMessageList to set
     */
    public void setResultMessageList(List<String> resultMessageList) {
        this.resultMessageList = resultMessageList;
    }
}
