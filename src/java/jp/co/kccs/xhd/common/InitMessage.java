/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
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
 * 変更日	2018/12/08<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * InitMessage(初期表示メッセージ)
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2018/12/08
 */
@ManagedBean(name = "beanInitMessage")
@SessionScoped
public class InitMessage implements Serializable {

    /**
     * 初期表示メッセージリスト
     */
    private List<String> initMessageList;


    /**
     * コンストラクタ
     */
    public InitMessage() {
    }

    /**
     * 初期表示メッセージリスト
     * @return the initMessageList
     */
    public List<String> getInitMessageList() {
        return initMessageList;
    }

    /**
     * 初期表示メッセージリスト
     * @param initMessageList the initMessageList to set
     */
    public void setInitMessageList(List<String> initMessageList) {
        this.initMessageList = initMessageList;
    }

}
