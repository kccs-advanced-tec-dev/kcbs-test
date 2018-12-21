/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo901;

import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import jp.co.kccs.xhd.db.model.FXHDD01;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2018/04/24<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	KCCS D.Yanagida<br>
 * 変更理由	新規作成<br>
 * <br>
 * 変更日	2018/11/13<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	ﾛｯﾄｶｰﾄﾞ電子化対応<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * 処理制御データ
 *
 * @author KCCS D.Yanagida
 * @since 2018/04/24
 */
public class ProcessData {

    /**
     * 処理名
     */
    private String processName;
    /**
     * 画面クラス名
     */
    private String formClassName;
    /**
     * 画面クラス
     */
    private IFormLogic formLogic;
    /**
     * 処理メソッド
     */
    private String method;
    /**
     * 項目データ
     */
    private List<FXHDD01> itemList;
    /**
     * 活性ボタンIDリスト
     */
    private List<String> activeButtonId;
    /**
     * 非活性ボタンIDリスト
     */
    private List<String> inactiveButtonId;
    /**
     * 情報メッセージ
     */
    private String infoMessage;
    /**
     * 警告メッセージ
     */
    private String warnMessage;
//    /**
//     * エラーメッセージ
//     */
//    private List<String> errorMessage;
    /**
     * DataSource(DocumentServer)
     */
    private DataSource dataSourceDocServer;
    /**
     * DataSource(QCDB)
     */
    private DataSource dataSourceQcdb;
    /**
     * DataSource(WIP)
     */
    private DataSource dataSourceWip;
    /**
     * ユーザー認証有無
     */
    private boolean rquireAuth;
    /**
     * ユーザー認証パラメータ
     */
    private String userAuthParam;
    /**
     * コールバックパラメータ
     */
    private String collBackParam;
    /**
     * チェック無しボタンIDリスト(設定しているIDについてはエラー処理の背景色をクリアしない)
     */
    private List<String> noCheckButtonId;

    /**
     * サブ画面初期表示メッセージ
     */
    private List<String> subInitDispMsgList;
    
    /**
     * エラーメッセージ情報リスト
     */
    private List<ErrorMessageInfo> errorMessageInfoList;

    /**
     * 初期表示時エラーメッセージリスト
     */
    private List<String> initMessageList;
    
    /**
     * 実行スクリプト
     */
    private String executeScript;
    
    /**
     * 規格値入力エラー情報リスト
     */
    private List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList;

    /**
     * コンストラクタ
     */
    public ProcessData() {
        //this.errorMessage = new ArrayList<>();
        this.itemList = new ArrayList<>();
        this.activeButtonId = new ArrayList<>();
        this.inactiveButtonId = new ArrayList<>();
        this.noCheckButtonId = new ArrayList<>();
        this.subInitDispMsgList = new ArrayList<>();
        this.errorMessageInfoList = new ArrayList<>();
        this.initMessageList = new ArrayList<>();
        this.kikakuchiInputErrorInfoList = new ArrayList<>();
    }

    /**
     * 処理名
     *
     * @return the processName
     */
    public String getProcessName() {
        return processName;
    }

    /**
     * 処理名
     *
     * @param processName the processName to set
     */
    public void setProcessName(String processName) {
        this.processName = processName;
    }

    /**
     * 画面クラス名
     *
     * @return the formClassName
     */
    public String getFormClassName() {
        return formClassName;
    }

    /**
     * 画面クラス名
     *
     * @param formClassName the formClassName to set
     */
    public void setFormClassName(String formClassName) {
        this.formClassName = formClassName;
    }

    /**
     * 画面クラス
     *
     * @return the formLogic
     */
    public IFormLogic getFormLogic() {
        return formLogic;
    }

    /**
     * 画面クラス
     *
     * @param formLogic the formLogic to set
     */
    public void setFormLogic(IFormLogic formLogic) {
        this.formLogic = formLogic;
    }

    /**
     * 処理メソッド
     *
     * @return the method
     */
    public String getMethod() {
        return method;
    }

    /**
     * 処理メソッド
     *
     * @param method the method to set
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * 項目データ
     *
     * @return the itemList
     */
    public List<FXHDD01> getItemList() {
        return itemList;
    }

    /**
     * 項目データ
     *
     * @param itemList the itemList to set
     */
    public void setItemList(List<FXHDD01> itemList) {
        this.itemList = itemList;
    }

    /**
     * 活性ボタンIDリスト
     *
     * @return the activeButtonId
     */
    public List<String> getActiveButtonId() {
        return activeButtonId;
    }

    /**
     * 活性ボタンIDリスト
     *
     * @param activeButtonId the activeButtonId to set
     */
    public void setActiveButtonId(List<String> activeButtonId) {
        this.activeButtonId = activeButtonId;
    }

    /**
     * 非活性ボタンIDリスト
     *
     * @return the inactiveButtonId
     */
    public List<String> getInactiveButtonId() {
        return inactiveButtonId;
    }

    /**
     * 非活性ボタンIDリスト
     *
     * @param inactiveButtonId the inactiveButtonId to set
     */
    public void setInactiveButtonId(List<String> inactiveButtonId) {
        this.inactiveButtonId = inactiveButtonId;
    }

    /**
     * 情報メッセージ
     *
     * @return the infoMessage
     */
    public String getInfoMessage() {
        return infoMessage;
    }

    /**
     * 情報メッセージ
     *
     * @param infoMessage the infoMessage to set
     */
    public void setInfoMessage(String infoMessage) {
        this.infoMessage = infoMessage;
    }

    /**
     * 警告メッセージ
     *
     * @return the warnMessage
     */
    public String getWarnMessage() {
        return warnMessage;
    }

    /**
     * 警告メッセージ
     *
     * @param warnMessage the warnMessage to set
     */
    public void setWarnMessage(String warnMessage) {
        this.warnMessage = warnMessage;
    }

//    /**
//     * エラーメッセージ
//     *
//     * @return the errorMessage
//     */
//    public List<String> getErrorMessage() {
//        return errorMessage;
//    }
//
//    /**
//     * エラーメッセージ
//     *
//     * @param errorMessage the errorMessage to set
//     */
//    public void setErrorMessage(List<String> errorMessage) {
//        this.errorMessage = errorMessage;
//    }

    /**
     * DataSource(DocumentServer)
     *
     * @return the dataSourceDocServer
     */
    public DataSource getDataSourceDocServer() {
        return dataSourceDocServer;
    }

    /**
     * DataSource(DocumentServer)
     *
     * @param dataSourceDocServer the dataSourceDocServer to set
     */
    public void setDataSourceDocServer(DataSource dataSourceDocServer) {
        this.dataSourceDocServer = dataSourceDocServer;
    }

    /**
     * DataSource(QCDB)
     *
     * @return the dataSourceQcdb
     */
    public DataSource getDataSourceQcdb() {
        return dataSourceQcdb;
    }

    /**
     * DataSource(QCDB)
     *
     * @param dataSourceQcdb the dataSourceQcdb to set
     */
    public void setDataSourceQcdb(DataSource dataSourceQcdb) {
        this.dataSourceQcdb = dataSourceQcdb;
    }

    /**
     * DataSource(WIP)
     *
     * @return the dataSourceWip
     */
    public DataSource getDataSourceWip() {
        return dataSourceWip;
    }

    /**
     * DataSource(WIP)
     *
     * @param dataSourceWip the dataSourceWip to set
     */
    public void setDataSourceWip(DataSource dataSourceWip) {
        this.dataSourceWip = dataSourceWip;
    }

    /**
     * ユーザー認証有無
     *
     * @return the rquireAuth
     */
    public boolean isRquireAuth() {
        return rquireAuth;
    }

    /**
     * ユーザー認証有無
     *
     * @param rquireAuth the rquireAuth to set
     */
    public void setRquireAuth(boolean rquireAuth) {
        this.rquireAuth = rquireAuth;
    }

    /**
     * ユーザー認証パラメータ
     *
     * @return the userAuthParam
     */
    public String getUserAuthParam() {
        return userAuthParam;
    }

    /**
     * ユーザー認証パラメータ
     *
     * @param userAuthParam the userAuthParam to set
     */
    public void setUserAuthParam(String userAuthParam) {
        this.userAuthParam = userAuthParam;
    }

    //******************************************************************
    /**
     * コールバックパラメータ
     *
     * @return the collBackParam
     */
    public String getCollBackParam() {
        return collBackParam;
    }

    /**
     * コールバックパラメータ
     *
     * @param collBackParam the collBackParam to set
     */
    public void setCollBackParam(String collBackParam) {
        this.collBackParam = collBackParam;
    }

    /**
     * チェック無しボタンIDリスト
     *
     * @return the noCheckButtonId
     */
    public List<String> getNoCheckButtonId() {
        return noCheckButtonId;
    }

    /**
     * チェック無しボタンIDリスト
     *
     * @param noCheckButtonId the noCheckButtonId to set
     */
    public void setNoCheckButtonId(List<String> noCheckButtonId) {
        this.noCheckButtonId = noCheckButtonId;
    }

    /**
     * サブ画面初期表示メッセージ
     *
     * @return the subInitDispMsgList
     */
    public List<String> getSubInitDispMsgList() {
        return subInitDispMsgList;
    }

    /**
     * サブ画面初期表示メッセージ
     *
     * @param subInitDispMsgList the subInitDispMsgList to set
     */
    public void setSubInitDispMsgList(List<String> subInitDispMsgList) {
        this.subInitDispMsgList = subInitDispMsgList;
    }

    /**
     * エラーメッセージ情報リスト
     * @return the errorMessageInfoList
     */
    public List<ErrorMessageInfo> getErrorMessageInfoList() {
        return errorMessageInfoList;
    }

    /**
     * エラーメッセージ情報リスト
     * @param errorMessageInfoList the errorMessageInfoList to set
     */
    public void setErrorMessageInfoList(List<ErrorMessageInfo> errorMessageInfoList) {
        this.errorMessageInfoList = errorMessageInfoList;
    }
    //******************************************************************

    /**
     * @return the initMessageList
     */
    public List<String> getInitMessageList() {
        return initMessageList;
    }

    /**
     * @param initMessageList the initMessageList to set
     */
    public void setInitMessageList(List<String> initMessageList) {
        this.initMessageList = initMessageList;
    }

    /**
     * 実行スクリプト
     *
     * @return the executeScript
     */
    public String getExecuteScript() {
        return executeScript;
    }

    /**
     * 実行スクリプト
     *
     * @param executeScript the executeScript to set
     */
    public void setExecuteScript(String executeScript) {
        this.executeScript = executeScript;
    }

    /**
     * 規格値入力エラー情報リスト
     * @return the kikakuchiInputErrorInfoList
     */
    public List<KikakuchiInputErrorInfo> getKikakuchiInputErrorInfoList() {
        return kikakuchiInputErrorInfoList;
    }

    /**
     * 規格値入力エラー情報リスト
     * @param kikakuchiInputErrorInfoList the kikakuchiInputErrorInfoList to set
     */
    public void setKikakuchiInputErrorInfoList(List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList) {
        this.kikakuchiInputErrorInfoList = kikakuchiInputErrorInfoList;
    }

}
