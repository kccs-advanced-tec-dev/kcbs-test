/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo901;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * 変更日	2018/12/08<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	項目追加<br>
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
     * 項目データ(拡張)
     */
    private List<FXHDD01> itemListEx;
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
     * DataSource(Spskadoritu)
     */
    private DataSource dataSourceSpskadoritu;
    /**
     * DataSource(Ttpkadoritu)
     */
    private DataSource dataSourceTtpkadoritu;
    /**
     * DataSource(equipment)
     */
    private DataSource dataSourceEquipment;
    
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
     * 警告メッセージ表示時メッセージリスト
     */
    private List<String> infoMessageList;
    /**
     * 実行スクリプト
     */
    private String executeScript;
    /**
     * 規格値入力エラー情報リスト
     */
    private List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList;
    /**
     * リビジョン(起動時)
     */
    private String initRev;
    /**
     * 状態フラグ(起動時)
     */
    private String initJotaiFlg;
    
    /**
     * 致命的エラー
     */
    private boolean fatalError;
    
    /**
     * リビジョンチェック対象ボタンID
     */
    private List<String> checkRevisionButtonId;
    
    /**
     * 完了メッセージ
     */
    private String compMessage;

    /**
     * 隠し項目MAP
     */
    private Map<String, Object> hiddenDataMap;

    /**
     * コンストラクタ
     */
    public ProcessData() {
        this.itemList = new ArrayList<>();
        this.activeButtonId = new ArrayList<>();
        this.inactiveButtonId = new ArrayList<>();
        this.noCheckButtonId = new ArrayList<>();
        this.subInitDispMsgList = new ArrayList<>();
        this.errorMessageInfoList = new ArrayList<>();
        this.initMessageList = new ArrayList<>();
        this.kikakuchiInputErrorInfoList = new ArrayList<>();
        this.checkRevisionButtonId = new ArrayList<>();
        this.infoMessageList = new ArrayList<>();
        this.hiddenDataMap = new HashMap<>();
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
     * 項目データ(拡張)
     * @return the itemListEx
     */
    public List<FXHDD01> getItemListEx() {
        return itemListEx;
    }

    /**
     * 項目データ(拡張)
     * @param itemListEx the itemListEx to set
     */
    public void setItemListEx(List<FXHDD01> itemListEx) {
        this.itemListEx = itemListEx;
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
     * DataSource(Spskadoritu)
     *
     * @return the dataSourceSpskadoritu
     */
    public DataSource getDataSourceSpskadoritu() {
        return dataSourceSpskadoritu;
    }

    /**
     * DataSource(Spskadoritu)
     *
     * @param dataSourceSpskadoritu the dataSourceSpskadoritu to set
     */
    public void setDataSourceSpskadoritu(DataSource dataSourceSpskadoritu) {
        this.dataSourceSpskadoritu = dataSourceSpskadoritu;
    }

    /**
     * DataSource(Ttpkadoritu)
     *
     * @return the dataSourceTtpkadoritu
     */
    public DataSource getDataSourceTtpkadoritu() {
        return dataSourceTtpkadoritu;
    }

    /**
     * DataSource(Ttpkadoritu)
     *
     * @param dataSourceTtpkadoritu the dataSourceTtpkadoritu to set
     */
    public void setDataSourceTtpkadoritu(DataSource dataSourceTtpkadoritu) {
        this.dataSourceTtpkadoritu = dataSourceTtpkadoritu;
    }

    /**
     * DataSource(equipment)
     * @return the dataSourceEquipment
     */
    public DataSource getDataSourceEquipment() {
        return dataSourceEquipment;
    }

    /**
     * DataSource(equipment)
     * @param dataSourceEquipment the dataSourceEquipment to set
     */
    public void setDataSourceEquipment(DataSource dataSourceEquipment) {
        this.dataSourceEquipment = dataSourceEquipment;
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
     *
     * @return the errorMessageInfoList
     */
    public List<ErrorMessageInfo> getErrorMessageInfoList() {
        return errorMessageInfoList;
    }

    /**
     * エラーメッセージ情報リスト
     *
     * @param errorMessageInfoList the errorMessageInfoList to set
     */
    public void setErrorMessageInfoList(List<ErrorMessageInfo> errorMessageInfoList) {
        this.errorMessageInfoList = errorMessageInfoList;
    }

    /**
     * 初期表示時エラーメッセージリスト
     * @return the initMessageList
     */
    public List<String> getInitMessageList() {
        return initMessageList;
    }

    /**
     * 初期表示時エラーメッセージリスト
     * @param initMessageList the initMessageList to set
     */
    public void setInitMessageList(List<String> initMessageList) {
        this.initMessageList = initMessageList;
    }

    /**
     * 警告メッセージ表示時メッセージリスト
     * @return the infoMessageList
     */
    public List<String> getInfoMessageList() {
        return infoMessageList;
    }

    /**
     * 警告メッセージ表示時メッセージリスト
     * @param infoMessageList the infoMessageList to set
     */
    public void setInfoMessageList(List<String> infoMessageList) {
        this.infoMessageList = infoMessageList;
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
     *
     * @return the kikakuchiInputErrorInfoList
     */
    public List<KikakuchiInputErrorInfo> getKikakuchiInputErrorInfoList() {
        return kikakuchiInputErrorInfoList;
    }

    /**
     * 規格値入力エラー情報リスト
     *
     * @param kikakuchiInputErrorInfoList the kikakuchiInputErrorInfoList to set
     */
    public void setKikakuchiInputErrorInfoList(List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList) {
        this.kikakuchiInputErrorInfoList = kikakuchiInputErrorInfoList;
    }

    /**
     * リビジョン(起動時)
     * @return the initRev
     */
    public String getInitRev() {
        return initRev;
    }

    /**
     * リビジョン(起動時)
     * @param initRev the initRev to set
     */
    public void setInitRev(String initRev) {
        this.initRev = initRev;
    }

    /**
     * 状態フラグ(起動時)
     * @return the initJotaiFlg
     */
    public String getInitJotaiFlg() {
        return initJotaiFlg;
    }

    /**
     * 状態フラグ
     * @param initJotaiFlg the initJotaiFlg to set
     */
    public void setInitJotaiFlg(String initJotaiFlg) {
        this.initJotaiFlg = initJotaiFlg;
    }

    /**
     * 致命的ｴﾗｰ
     * @return the fatalError
     */
    public boolean isFatalError() {
        return fatalError;
    }

    /**
     * 致命的ｴﾗｰ
     * @param fatalError the fatalError to set
     */
    public void setFatalError(boolean fatalError) {
        this.fatalError = fatalError;
    }
    
    
    /**
     * リビジョンチェック対象ボタンID
     * @return the checkRevisionButtonId
     */
    public List<String> getCheckRevisionButtonId() {
        return checkRevisionButtonId;
    }

    /**
     * リビジョンチェック対象ボタンID
     * @param checkRevisionButtonId the checkRevisionButtonId to set
     */
    public void setCheckRevisionButtonId(List<String> checkRevisionButtonId) {
        this.checkRevisionButtonId = checkRevisionButtonId;
    }

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

    /**
     * 隠し項目MAP
     * @return the hiddenDataMap
     */
    public Map getHiddenDataMap() {
        return hiddenDataMap;
    }

    /**
     * 隠し項目MAP
     * @param hiddenItemMap the hiddenDataMap to set
     */
    public void setHiddenDataMap(Map hiddenItemMap) {
        this.hiddenDataMap = hiddenItemMap;
    }
}
