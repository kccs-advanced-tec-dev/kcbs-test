/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo901;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import jp.co.kccs.xhd.db.Parameter;
import jp.co.kccs.xhd.db.ParameterEJB;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.db.model.FXHDD02;
import jp.co.kccs.xhd.db.model.FXHDM02;
import jp.co.kccs.xhd.db.model.FXHDM06;
import jp.co.kccs.xhd.util.DBUtil;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.StringUtil;
import jp.co.kccs.xhd.util.ValidateUtil;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2018/05/06<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	KCCS D.Yanagida<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */

/**
 * GXHDO901A(品質DB画面共通)
 * 
 * @author KCCS D.Yanagida
 * @since 2018/05/06
 */
@Named
@ViewScoped
public class GXHDO901A implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(GXHDO901A.class.getName());
    private static final int LOTNO_ITEMNO = 100;
    
    /**
     * DataSource(DocumentServer)
     */
    @Resource(mappedName = "jdbc/DocumentServer")
    private transient DataSource dataSourceDocServer;
    /**
     * DataSource(QCDB)
     */
    @Resource(mappedName = "jdbc/qcdb")
    private transient DataSource dataSourceQcdb;
    /**
     * DataSource(WIP)
     */
    @Resource(mappedName = "jdbc/wip")
    private transient DataSource dataSourceWip;
    
    /**
     * パラメータ操作(DB)
     */
    @Inject
    private ParameterEJB parameterEJB;
    
    /**
     * 起動時処理
     */
    private String onLoadProcess = "";
    
    /**
     * タイトル情報
     */
    private FXHDM02 titleInfo;
    /**
     * 画面上部ボタン
     */
    private List<FXHDD02> buttonListTop;
    /**
     * 画面下部ボタン
     */
    private List<FXHDD02> buttonListBottom;
    /**
     * チェックリスト
     */
    private List<FXHDM06> checkListHDM06;
    /**
    /**
     * 項目データ
     */
    private List<FXHDD01> itemList;
    /**
     * 処理データ
     */
    private ProcessData processData = null;
    /**
     * 警告ダイアログ
     */
    private boolean warnDialogRendered = false;
    /**
     * ユーザー認証：ユーザー
     */
    private String authUser;
    /**
     * ユーザー認証：パスワード
     */
    private String authPassword;
    
    /**
     * コンストラクタ
     */
    public GXHDO901A() {
    }

    /**
     * 起動時処理
     * @return the onLoadProcess
     */
    public String getOnLoadProcess() {
        return onLoadProcess;
    }

    /**
     * 起動時処理
     * @param onLoadProcess the onLoadProcess to set
     */
    public void setOnLoadProcess(String onLoadProcess) {
        this.onLoadProcess = onLoadProcess;
    }
    
    /**
     * タイトル情報
     * @return タイトル情報
     */
    public FXHDM02 getTitleInfo() {
        return titleInfo;
    }
    
    /**
     * 画面上部ボタン
     * @return the buttonListTop
     */
    public List<FXHDD02> getButtonListTop() {
        return buttonListTop;
    }

    /**
     * 画面下部ボタン
     * @return the buttonListBottom
     */
    public List<FXHDD02> getButtonListBottom() {
        return buttonListBottom;
    }

    /**
     * 項目データ
     * @return the itemList
     */
    public List<FXHDD01> getItemList() {
        return itemList;
    }

    /**
     * 処理データ
     * @return the processData
     */
    public ProcessData getProcessData() {
        return processData;
    }

    /**
     * 警告ダイアログ
     * @return the warnDialogRendered
     */
    public boolean getWarnDialogRendered() {
        return warnDialogRendered;
    }

    /**
     * 入力項目表示有無
     * @return true or false
     */
    public boolean getItemRendered() {
        if (null == this.itemList || 0 == this.itemList.size()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * ユーザー認証：ユーザー
     * @return the authUser
     */
    public String getAuthUser() {
        return authUser;
    }

    /**
     * ユーザー認証：ユーザー
     * @param authUser the authUser to set
     */
    public void setAuthUser(String authUser) {
        this.authUser = authUser;
    }

    /**
     * ユーザー認証：パスワード
     * @return the authPassword
     */
    public String getAuthPassword() {
        return authPassword;
    }

    /**
     * ユーザー認証：パスワード
     * @param authPassword the authPassword to set
     */
    public void setAuthPassword(String authPassword) {
        this.authPassword = authPassword;
    }

    /**
     * 起動時処理
     */
    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String login_user_name = (String) session.getAttribute("login_user_name");
        
        if (null == login_user_name || "".equals(login_user_name)) {
            // セッションタイムアウト時はログイン画面に遷移
            this.setOnLoadProcess("window.location.href = '" + externalContext.getRequestContextPath()  + "/faces/login.xhtml?faces-redirect=true';");
            return;
        } else {
            this.setOnLoadProcess("");
        }
        
        // 画面遷移パラメータ取得
        String formId = StringUtil.nullToBlank(session.getAttribute("formId"));
        String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));
        String titleSetting = StringUtil.nullToBlank(session.getAttribute("titleSetting"));
        
        // タイトル設定情報取得
        this.loadTitleSettings(titleSetting, formTitle);
        
        // ボタン定義情報取得
        this.loadButtonSettings(formId);
        
        // 項目定義情報取得
        this.loadItemSettings(formId);
        
        // チェック処理情報取得
        this.loadCheckList(formId);
        
        // 初期表示処理を実行する
        // 画面クラス名を取得
        String formClassName = StringUtil.nullToBlank(session.getAttribute("formClassName"));
        
        // 画面クラスをロード
        IFormLogic formLogic = null;
        try {
            Class<?> formClass = Class.forName(formClassName);
            formLogic = (IFormLogic)formClass.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex) {
            ErrUtil.outputErrorLog("画面クラスのロードに失敗", ex, LOGGER);
            return;
        }
        //ロットNo初期表示設定
        String lotNo = StringUtil.nullToBlank(session.getAttribute("lotNo"));
        for(int i=0;i<=this.itemList.size()-1;i++){
            if(this.itemList.get(i).getItemNo() == LOTNO_ITEMNO){
                this.itemList.get(i).setInputDefault(lotNo);
                break;
            }
        }
        
        // 処理データ生成
        ProcessData data = new ProcessData();
        data.setFormClassName(formClassName);
        data.setFormLogic(formLogic);
        data.setMethod("initial");
        data.setItemList(this.itemList);
        data.setDataSourceDocServer(this.dataSourceDocServer);
        data.setDataSourceQcdb(this.dataSourceQcdb);
        data.setDataSourceWip(this.dataSourceWip);
        
        this.processData = data;
        
        // 処理開始
        this.processMain();
    }
    
    /**
     * ログアウトします。
     * @return ログイン用URL文字列
     */
    public String logOut() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        if (session != null) {
            try {
                session.invalidate();
            } catch(IllegalStateException e) {
                LOGGER.log(Level.SEVERE, null, e);
            }
        }
        // goto login
        return "/login.xhtml?faces-redirect=true";
    }
    
    /**
     * メニュー画面遷移
     * @return  メニュー遷移用URL文字列
     */
    public String returnMenu() {
        return "/pxhdo012/gxhdo012a.xhtml?faces-redirect=true";
    }
    
    /**
     * ユーザーマスタ検索
     * @param user 担当者コード
     * @return パスワード
     */
    private String selectFxhbm01(String user) {
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceDocServer);
            String sql = "SELECT password "
                       + "FROM fxhbm01 WHERE user_name = ? ";
            
            List<Object> params = new ArrayList<>();
            params.add(user);
                      
            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            Map fxhbm01 = queryRunner.query(sql, new MapHandler(), params.toArray());
            if (null == fxhbm01 || fxhbm01.isEmpty()) {
                return "";
            }
            return StringUtil.nullToBlank(fxhbm01.get("password"));
            
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            return "";
        }
    }
    
    /**
     * ユーザーグループマスタ検索
     * @param user 担当者コード
     * @return ユーザーグループ
     */
    private List<String> selectFxhbm02(String user) {
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceDocServer);
            String sql = "SELECT usergroup "
                       + "FROM fxhbm02 WHERE user_name = ? ";
            
            List<Object> params = new ArrayList<>();
            params.add(user);
                      
            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            List fxhbm02 = queryRunner.query(sql, new MapListHandler(), params.toArray());
            if (null == fxhbm02 || fxhbm02.isEmpty()) {
                return new ArrayList<>();
            }
            return ((List<Map>)fxhbm02).stream()
                    .map(n -> n.get("usergroup").toString())
                    .filter(n -> !StringUtil.isEmpty(n))
                    .collect(Collectors.toList());
            
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            return new ArrayList<>();
        }
    }
    
    /**
     * ログインユーザー権限チェック
     * @return 権限がある場合true
     */
    private boolean userAuthLogin() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        FacesContext facesContext = FacesContext.getCurrentInstance();

        String authParam = this.processData.getUserAuthParam();
        
        String user = facesContext.getExternalContext().getUserPrincipal().getName();
        List<String> loginUserGroup = this.selectFxhbm02(user);
        
        List<Parameter> paramListLoginUser = parameterEJB.findParameter(user, authParam);
        List<Parameter> paramListLoginUserGroup = new ArrayList<>();
        for (String group : loginUserGroup) {
            paramListLoginUserGroup.addAll(parameterEJB.findParameter(group, authParam));
        }
        if (paramListLoginUser.isEmpty() && paramListLoginUserGroup.isEmpty()) {
            return false;
        }
        return true;
    }
    
    /**
     * ユーザー認証チェック
     */
    public void userAuth() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        FacesContext facesContext = FacesContext.getCurrentInstance();

        String authParam = this.processData.getUserAuthParam();
        String passWord = this.selectFxhbm01(this.authUser);
        
        if (StringUtil.isEmpty(passWord) || 
            !passWord.toUpperCase().equals(this.getSha256(this.authPassword).toUpperCase())) {
            // ユーザーが存在しない場合またはパスワード不一致の場合エラー終了
            FacesMessage message = 
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "権限が有りません。", null);
            facesContext.addMessage(null, message);
            return;
        }
        
        List<String> loginUserGroup = this.selectFxhbm02(this.authUser);
        List<Parameter> paramListUser = parameterEJB.findParameter(this.authUser, authParam);
        List<Parameter> paramListUserGroup = new ArrayList<>();
        for (String group : loginUserGroup) {
            paramListUserGroup.addAll(parameterEJB.findParameter(group, authParam));
        }
        
        if (paramListUser.isEmpty() && paramListUserGroup.isEmpty()) {
            // 権限がない場合エラー終了
            FacesMessage message = 
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "権限が有りません。", null);
            facesContext.addMessage(null, message);
            return;
        }
        
        // 後続処理を実行
        this.processMain();
    }
    
    /**
     * SHA-256変換
     * @param text 文字列
     * @return SHA-256変換後の文字列
     */
    private String getSha256(String text) {
        try {
            if (StringUtil.isEmpty(text)) {
                return "";
            }
            
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(text.getBytes());
            byte[] cipher_byte = md.digest();
            StringBuilder sb = new StringBuilder(2 * cipher_byte.length);
            for(byte b: cipher_byte) {
                sb.append(String.format("%02x", b&0xff) );
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            ErrUtil.outputErrorLog("SHA-256変換失敗", ex, LOGGER);
            return "";
        }
    }
    
    /**
     * ボタン押下時処理
     * @param buttonId ボタンID
     */
    public void btnClick(String buttonId) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        this.warnDialogRendered = false;
        
        // 画面ID、画面クラス名を取得
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String formClassName = StringUtil.nullToBlank(session.getAttribute("formClassName"));
        String login_user_name = (String) session.getAttribute("login_user_name");

        if (null == login_user_name || "".equals(login_user_name)) {
            // セッションタイムアウト時はログイン画面に遷移
            this.setOnLoadProcess("window.location.href = '" + externalContext.getRequestContextPath()  + "/faces/login.xhtml?faces-redirect=true';");
            return;
        }
        
        // 画面クラスをロード
        IFormLogic formLogic = null;
        try {
            Class<?> formClass = Class.forName(formClassName);
            formLogic = (IFormLogic)formClass.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex) {
            ErrUtil.outputErrorLog("画面クラスのロードに失敗", ex, LOGGER);
            return;
        }
        
        // ボタンに対応した処理メソッド名を取得
        String method = formLogic.convertButtonIdToMethod(buttonId);
        if ("error".equals(method)) {
            FacesMessage message = 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "処理が登録されていません", null);
            facesContext.addMessage(null, message);
        }
        
        //共通ﾁｪｯｸ
        String requireCheckErrorMessage = getCheckResult(method);

        // エラーメッセージが設定されている場合、エラー出力のみ
        if (!StringUtil.isEmpty(requireCheckErrorMessage)) {
            FacesMessage message = 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, requireCheckErrorMessage, null);
            facesContext.addMessage(null, message);
            return;
        }
        
        // 処理データ生成
        ProcessData data = new ProcessData();
        data.setFormClassName(formClassName);
        data.setFormLogic(formLogic);
        data.setMethod(method);
        data.setItemList(this.itemList);
        data.setDataSourceDocServer(this.dataSourceDocServer);
        data.setDataSourceQcdb(this.dataSourceQcdb);
        data.setDataSourceWip(this.dataSourceWip);
        
        this.processData = data;
        
        // 処理開始
        this.processMain();
    }
        
    /**
     * 警告ダイアログで「はい」が選択された場合の処理
     */
    public void processWarnOk() {
        IFormLogic formLogic = this.getProcessData().getFormLogic();
        String method = this.getProcessData().getMethod();
        
        // 警告メッセージを削除して処理再開
        this.processData.setWarnMessage("");
        this.warnDialogRendered = false;
        this.processMain();
    }
    
    /**
     * メイン処理
     */
    private void processMain() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        
        // エラーメッセージが設定されている場合、エラー出力のみ
        if (!StringUtil.isEmpty(this.processData.getErrorMessage())) {
            FacesMessage message = 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, this.processData.getErrorMessage(), null);
            facesContext.addMessage(null, message);
            return;
        }
        
        // ユーザー認証が必要な場合
        if (this.processData.isRquireAuth()) {
            this.processData.setRquireAuth(false);
            
            // ログインユーザーの権限チェックを実施する
            if (!this.userAuthLogin()) {
                // 権限がない場合はユーザー認証画面を表示
                RequestContext context = RequestContext.getCurrentInstance();
                context.addCallbackParam("firstParam", "auth");
                return;
            }
        }
        
        // 警告メッセージが設定されている場合、ダイアログを表示する
        if (!StringUtil.isEmpty(this.processData.getWarnMessage())) {
            this.warnDialogRendered = true;
            
            RequestContext context = RequestContext.getCurrentInstance();
            context.addCallbackParam("firstParam", "warning");
            
            return;
        }
        
        // 警告もエラーも存在しない場合、後続処理を実行して結果を反映する
        try {
            IFormLogic formLogic = this.processData.getFormLogic();
            String methodName = this.processData.getMethod();
        
            Class<?> formClass = Class.forName(this.processData.getFormClassName());
            Method method = formClass.getMethod(methodName, ProcessData.class);
            
            // 処理実行
            ProcessData resultData = (ProcessData)method.invoke(formLogic, this.processData);
                
            if (!StringUtil.isEmpty(resultData.getMethod())) {
                // 後続処理が定義されている場合は再起的に実行する
                this.processData = resultData;
                this.processMain();
            } else {
                // 後続処理が定義されていない場合は結果を反映して処理を終了する
                this.itemList = resultData.getItemList();
                // 情報メッセージが設定されていれば出力する
                if (!StringUtils.isEmpty(this.processData.getInfoMessage())) {
                    FacesMessage message = 
                        new FacesMessage(FacesMessage.SEVERITY_INFO, this.processData.getInfoMessage(), null);
                    facesContext.addMessage(null, message);
                }
                // ボタンの活性・非活性制御
                this.setButtonEnabled(this.processData.getActiveButtonId(), this.processData.getInactiveButtonId());
                // 処理制御データクリア
                this.processData = new ProcessData();
            }
            
        } catch (ReflectiveOperationException ex) {
            ErrUtil.outputErrorLog("画面クラスのロードに失敗", ex, LOGGER);
        }
    }
    
    /**
     * ボタンの活性・非活性制御
     * @param activeId 活性にするボタンのID
     * @param inactiveId 非活性にするボタンのID
     */
    private void setButtonEnabled(List<String> activeId, List<String> inactiveId) {
        if (null != activeId) {
            for (String buttonId : activeId) {
                for (FXHDD02 button : this.buttonListTop) {
                    if (buttonId.equals(button.getButtonId())) {
                        button.setEnabled(true);
                    }
                }
                for (FXHDD02 button : this.buttonListBottom) {
                    if (buttonId.equals(button.getButtonId())) {
                        button.setEnabled(true);
                    }
                }
            }
        }

        if (null != inactiveId) {
            for (String buttonId : inactiveId) {
                for (FXHDD02 button : this.buttonListTop) {
                    if (buttonId.equals(button.getButtonId())) {
                        button.setEnabled(false);
                    }
                }
                for (FXHDD02 button : this.buttonListBottom) {
                    if (buttonId.equals(button.getButtonId())) {
                        button.setEnabled(false);
                    }
                }
            }
        }
    }
    
    /**
     * タイトル設定情報取得
     * @param settingId 設定ID 
     * @param formTitle 画面タイトル
     */
    private void loadTitleSettings(String settingId, String formTitle) {
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceDocServer);
            String sql = "SELECT setting_id, font_size, font_color, bg_color "
                       + "FROM fxhdm02 WHERE setting_id = ? ";
            
            List<Object> params = new ArrayList<>();
            params.add(settingId);

            Map<String, String> mapping = new HashMap<>();
            mapping.put("setting_id", "settingId");
            mapping.put("font_size", "fontSize");
            mapping.put("font_color", "fontColor");
            mapping.put("bg_color", "bgColor");
            
            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<FXHDM02>> beanHandler = new BeanListHandler(FXHDM02.class, rowProcessor);
            
            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            List<FXHDM02> fXHDM02List = queryRunner.query(sql, beanHandler, params.toArray());
            
            if (fXHDM02List.isEmpty()) {
                this.titleInfo = new FXHDM02();
                this.titleInfo.setFormName(formTitle);
            } else {
                this.titleInfo = fXHDM02List.get(0);
                this.titleInfo.setFormName(formTitle);
            }
            
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("ボタン項目取得失敗", ex, LOGGER);
        }
    }
    
    /**
     * 項目定義情報取得
     * @param formId 項目定義情報
     */
    private void loadItemSettings(String formId) {
        // ユーザーグループ取得
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        List<String> userGrpList = (List<String>)session.getAttribute("login_user_group");
        
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceDocServer);
            String sql = "SELECT hdd01.gamen_id, hdd01.item_id, hdd01.item_no, hdd01.input_item_mold, hdd01.label1, hdd01.label2, "
                       + "hdd01.input_list, hdd01.input_default, hdd01.input_length, hdd01.input_length_dec, "
                       + "hdm02_1.font_size AS font_size_1, hdm02_1.font_color AS font_color_1, hdm02_1.bg_color AS bg_color_1, "
                       + "hdm04.sekkei_column sekkei_column, hcm02_4.font_size AS font_size_3, hcm02_4.font_color AS font_color_3, hcm02_4.bg_color AS bg_color_3, "
                       + "CASE WHEN hdd01.label1_setting IS NULL THEN 'false' ELSE 'true' END AS render_1, "
                       + "hdm02_2.font_size AS font_size_2, hdm02_2.font_color AS font_color_2, hdm02_2.bg_color AS bg_color_2, "
                       + "CASE WHEN hdd01.label2_setting IS NULL THEN 'false' ELSE 'true' END AS render_2, "
                       + "hdm02_3.font_size AS font_size_input, hdm02_3.font_color AS font_color_input, hdm02_3.bg_color AS bg_color_input, "
                       + "CASE WHEN hdd01.input_setting IS NULL OR hdd01.input_item_mold != '1' THEN 'false' ELSE 'true' END AS render_iput_text, "
                       + "CASE WHEN hdd01.input_setting IS NULL OR hdd01.input_item_mold != '2' THEN 'false' ELSE 'true' END AS render_iput_number, "
                       + "CASE WHEN hdd01.input_setting IS NULL OR hdd01.input_item_mold != '3' THEN 'false' ELSE 'true' END AS render_iput_date, "
                       + "CASE WHEN hdd01.input_setting IS NULL OR hdd01.input_item_mold != '4' THEN 'false' ELSE 'true' END AS render_iput_select, "
                       + "CASE WHEN hdd01.input_setting IS NULL OR hdd01.input_item_mold != '5' THEN 'false' ELSE 'true' END AS render_iput_radio, "
                       + "CASE WHEN hdd01.input_setting IS NULL OR hdd01.input_item_mold != '6' THEN 'false' ELSE 'true' END AS render_iput_time, "
                       + "CASE WHEN hdd01.input_setting IS NULL OR hdd01.input_item_mold != '7' THEN 'false' ELSE 'true' END AS render_output_label "
                       + "FROM fxhdd01 hdd01 "
                       + "LEFT JOIN fxhdm02 hdm02_1 ON (hdd01.label1_setting = hdm02_1.setting_id) "
                       + "LEFT JOIN fxhdm02 hdm02_2 ON (hdd01.label2_setting = hdm02_2.setting_id) "
                       + "LEFT JOIN fxhdm02 hdm02_3 ON (hdd01.input_setting = hdm02_3.setting_id) "
                       + "LEFT JOIN fxhdm02 hcm02_4 ON (hdd01.standard_setting = hcm02_4.setting_id) "
                       + "LEFT JOIN fxhdm04 hdm04 ON (hdm04.gamen_id = ? AND hdm04.item_id =hdd01.item_id) "
                       + "WHERE hdd01.gamen_id = ? AND (hdd01.item_authority IS NULL OR "
                       + DBUtil.getInConditionPreparedStatement("hdd01.item_authority",  userGrpList.size()) + ") "
                       + "ORDER BY hdd01.item_no ";
            
            List<Object> params = new ArrayList<>();
            params.add(formId);
            params.add(formId);
            params.addAll(userGrpList);

            Map<String, String> mapping = new HashMap<>();
            mapping.put("gamen_id", "gamenId");
            mapping.put("item_id", "itemId");
            mapping.put("item_no", "itemNo");
            mapping.put("input_item_mold", "inputItemType");
            mapping.put("label1", "label1");
            mapping.put("label2", "label2");
            mapping.put("input_list", "inputList");
            mapping.put("input_default", "inputDefault");
            mapping.put("input_length", "inputLength");
            mapping.put("input_length_dec", "inputLengthDec");
            mapping.put("font_size_1", "fontSize1");
            mapping.put("font_color_1", "fontColor1");
            mapping.put("bg_color_1", "backColor1");
            mapping.put("render_1", "render1");
            mapping.put("font_size_2", "fontSize2");
            mapping.put("font_color_2", "fontColor2");
            mapping.put("bg_color_2", "backColor2");
            mapping.put("render_2", "render2");
            mapping.put("font_size_input", "fontSizeInput");
            mapping.put("font_color_input", "fontColorInput");
            mapping.put("bg_color_input", "backColorInput");
            mapping.put("render_iput_text", "renderInputText");
            mapping.put("render_iput_number", "renderInputNumber");
            mapping.put("render_iput_date", "renderInputDate");
            mapping.put("render_iput_select", "renderInputSelect");
            mapping.put("render_iput_radio", "renderInputRadio");
            mapping.put("render_iput_time", "renderInputTime");
            mapping.put("render_output_label", "renderOutputLabel");
            mapping.put("sekkei_column", "sekkeiColumn");
            mapping.put("font_size_3", "fontSize3");
            mapping.put("font_color_3", "fontColor3");
            mapping.put("bg_color_3", "backColor3");

            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<FXHDD01>> beanHandler = new BeanListHandler<>(FXHDD01.class, rowProcessor);
            
            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            this.itemList = queryRunner.query(sql, beanHandler, params.toArray());
            
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("項目情報取得失敗", ex, LOGGER);
        }
    }
    
    /**
     * ボタンパラメータ情報取得
     * @param formId 画面ID
     */
    private void loadButtonSettings(String formId) {
        // ユーザーグループ取得
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        List<String> userGrpList = (List<String>)session.getAttribute("login_user_group");
        
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceDocServer);
            String sql = "SELECT hdd02.button_id, hdd02.button_no, hdd02.button_location, hdd02.button_name, hdm02.font_size, hdm02.font_color, hdm02.bg_color "
                       + "FROM fxhdd02 hdd02 INNER JOIN fxhdm02 hdm02 ON (hdd02.button_setting = hdm02.setting_id) "
                       + "WHERE hdd02.gamen_id = ? AND  (hdd02.button_authority IS NULL OR"
                       + DBUtil.getInConditionPreparedStatement("hdd02.button_authority", userGrpList.size()) + ") "
                       + "ORDER BY hdd02.button_location, hdd02.button_no";
            
            List<Object> params = new ArrayList<>();
            params.add(formId);
            params.addAll(userGrpList);

            Map<String, String> mapping = new HashMap<>();
            mapping.put("button_id", "buttonId");
            mapping.put("button_no", "buttonNo");
            mapping.put("button_location", "buttonLocation");
            mapping.put("button_name", "buttonName");
            mapping.put("font_size", "fontSize");
            mapping.put("font_color", "fontColor");
            mapping.put("bg_color", "backColor");
            
            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<FXHDD02>> beanHandler = new BeanListHandler<>(FXHDD02.class, rowProcessor);
            
            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            List<FXHDD02> buttonList = queryRunner.query(sql, beanHandler, params.toArray());
            
            // 上部ボタン
            this.buttonListTop = 
                    buttonList.stream().filter(n -> "1".equals(n.getButtonLocation())).collect(Collectors.toList());
            // 下部ボタン
            this.buttonListBottom = 
                    buttonList.stream().filter(n -> "2".equals(n.getButtonLocation())).collect(Collectors.toList());

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("ボタン項目取得失敗", ex, LOGGER);
        }
    }
    
    /**
     * チェック処理情報取得
     * @param formId 画面ID
     */
    private void loadCheckList(String formId) {
        
        try {
            QueryRunner queryRunner = new QueryRunner(dataSourceDocServer);
            String sql = "SELECT gamen_id,button_id,item_id,check_pattern,check_no,hissu_flag "
                       + "  FROM FXHDM06 "
                       + " WHERE gamen_id = ? ";
            
            List<Object> params = new ArrayList<>();
            params.add(formId);

            Map<String, String> mapping = new HashMap<>();
            mapping.put("gamen_id", "gamenId");
            mapping.put("button_id", "buttonId");
            mapping.put("item_id", "itemId");
            mapping.put("check_pattern", "checkPattern");
            mapping.put("check_no", "checkNo");
            mapping.put("hissu_flag", "hissuFlag");
            
            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<FXHDM06>> beanHandler = new BeanListHandler<>(FXHDM06.class, rowProcessor);
            
            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            this.checkListHDM06 = queryRunner.query(sql, beanHandler, params.toArray());
            
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("チェック処理項目取得失敗", ex, LOGGER);
        }
    }

    /**
     * 共通チェック
     *
     * @param method 処理メソッド名
     * @return エラーメッセージ
     */
    private String getCheckResult(String method) {    
        //共通ﾁｪｯｸ
        List<FXHDM06> itemRowCheckList
                = checkListHDM06.stream().filter(n -> method.equals(n.getButtonId())).collect(Collectors.toList());
        List<ValidateUtil.ValidateInfo> requireCheckList = new ArrayList<>();
        ValidateUtil validateUtil = new ValidateUtil();

        // データを設定する
        for (FXHDM06 fxhddM06 : itemRowCheckList) {            
           for (ValidateUtil.EnumCheckNo checkNo : ValidateUtil.EnumCheckNo.values()) {
             if (checkNo.name().equalsIgnoreCase(fxhddM06.getCheckPattern())) {
                 requireCheckList.add(validateUtil.new ValidateInfo(checkNo, fxhddM06.getItemId(), null, null));
                 break;
             }
           }
        }

        String requireCheckErrorMessage = validateUtil.executeValidation(requireCheckList, this.itemList);        
        return requireCheckErrorMessage;
    }
}
