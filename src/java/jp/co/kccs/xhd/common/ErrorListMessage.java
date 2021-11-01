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
 * 変更日	2021/10/09<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS wxf<br>
 * 変更理由	新規作成<br>
 * ===============================================================================<br>
 */
/**
 * ErrorListMessage(情報表示メッセージ)
 *
 * @author KCSS wxf
 * @since  2021/10/09
 */
@ManagedBean(name = "beanErrorListMessage")
@SessionScoped
public class ErrorListMessage implements Serializable {
    
    /**
     * タイトル表示メッセージ
     */
    private String titleMessage;
    /**
     * 情報表示メッセージ
     */
    private String resultMessage;
    /**
     * 情報表示メッセージリスト
     */
    private List<String> resultMessageList;
    /**
     * コンストラクタ
     */
    public ErrorListMessage() {
    }

    /**
     * タイトル表示メッセージ
     * @return the titleMessage
     */
    public String getTitleMessage() {
        return titleMessage;
    }

    /**
     * タイトル表示メッセージ
     * @param titleMessage the titleMessage to set
     */
    public void setTitleMessage(String titleMessage) {
        this.titleMessage = titleMessage;
    }

    /**
     * 情報表示メッセージ
     * @return the resultMessage
     */
    public String getResultMessage() {
        return resultMessage;
    }

    /**
     * 情報表示メッセージ
     * @param resultMessage the resultMessage to set
     */
    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    /**
     * 情報表示メッセージリスト
     * @return the resultMessageList
     */
    public List<String> getResultMessageList() {
        return resultMessageList;
    }

    /**
     * 情報表示メッセージリスト
     * @param resultMessageList the resultMessageList to set
     */
    public void setResultMessageList(List<String> resultMessageList) {
        this.resultMessageList = resultMessageList;
    }
}
