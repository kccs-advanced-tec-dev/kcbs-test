/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.common;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/03/06<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * InfoMessage(警告メッセージ)
 *
 * @author KCSS K.Jo
 * @since  2019/03/06
 */
@ManagedBean(name = "beanInfoMessage")
@SessionScoped
public class InfoMessage implements Serializable {

    /**
     * 警告メッセージリスト
     */
    private List<String> infoMessageList;

    /**
     * コンストラクタ
     */
    public InfoMessage() {
    }

    /**
     * 警告メッセージリスト
     *
     * @return the infoMessageList
     */
    public List<String> getInfoMessageList() {
        return infoMessageList;
    }

    /**
     * 警告メッセージリスト
     *
     * @param infoMessageList the infoMessageList to set
     */
    public void setInfoMessageList(List<String> infoMessageList) {
        this.infoMessageList = infoMessageList;
    }

}
