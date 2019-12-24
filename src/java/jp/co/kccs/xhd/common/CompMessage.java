/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.common;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/12/05<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * CompMessage(完了メッセージ)
 *
 * @author SYSNAVI K.Hisanaga
 * @since  2019/12/05
 */
@ManagedBean(name = "beanCompMessage")
@SessionScoped
public class CompMessage implements Serializable {

    /**
     * 完了メッセージ
     */
    private String compMessage;

        /**
     * 完了メッセージ
     * @return the compMessage
     */
    public String getCompMessage() {
        return compMessage;
    }

    /**
     * 完了メッセージ
     * @param compMessage the compMessage to set
     */
    public void setCompMessage(String compMessage) {
        this.compMessage = compMessage;
    }
}
