/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo501;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import jp.co.kccs.xhd.pxhdo901.GXHDO901AEX;
import jp.co.kccs.xhd.db.model.FXHDM01;
import jp.co.kccs.xhd.util.DBUtil;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import jp.co.kccs.xhd.GetModel;
import jp.co.kccs.xhd.common.ResultMessage;
import jp.co.kccs.xhd.db.Parameter;
import jp.co.kccs.xhd.model.GXHDO501DModel;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.StringUtil;
import jp.co.kccs.xhd.util.SubFormUtil;
import jp.co.kccs.xhd.util.ValidateUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.primefaces.context.RequestContext;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/07/24<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO501DA(原材料規格ﾒﾝﾃﾅﾝｽ機能)
 *
 * @author KCSS K.Jo
 * @since  2021/07/24
 */
@Named
@ViewScoped
public class GXHDO501DA extends GXHDO901AEX {    
    /** 一覧表示データ */
    private List<GXHDO501DModel> listData = null;
    /** 警告メッセージ */
    private String warnMessage = "";
    /** 警告時処理 */
    private String warnProcess = "";
    /** 検索条件：ﾛｯﾄNo */
    private String lotNo="";
    /** 検索条件：種類 */
    private String syurui="";
    /** 検索条件：ﾊﾟﾀｰﾝ */
    private String pattern="";
    /** 一覧ﾁｪｯｸﾊﾟﾀｰﾝリスト:表示可能ﾃﾞｰﾀ */
    private String cmbTyekkupatternData[];
    /** * 件数0の場合「戻る」のみが表示された画面を表示。*/
    private String displayStyle = "";
    /** 設計No */
    private String paramSekkeino = "";
    /** LotNo */
    private String paramLotno = "";
    /** 種類 */
    private String paramSyurui = "";
    /** 品名 */
    private String paramHinmei = "";
    /** ﾊﾟﾀｰﾝ */
    private String paramPattern = "";
    /** * ユーザー認証パラメータ(修正) */
    private static final String USER_AUTH_UPDATE_PARAM = "da_mkjoken_update_button";
    /** * ｴﾗｰ発生項目の背景色 */
    private static final String ERROR_COLOR = "#FFB6C1";
    /** * ｴﾗｰがない項目の背景色 */
    private static final String NORMAL_COLOR ="#FFFFFF";

    /**
     * コンストラクタ
     */
    public GXHDO501DA() {
    }

    //<editor-fold defaultstate="collapsed" desc="#getter setter">
    
    /**
     * 件数0の場合「戻る」のみが表示
     *
     * @return the displayStyle
     */
    public String getDisplayStyle() {
        return displayStyle;
    }

    /**
     * 件数0の場合「戻る」のみが表示
     *
     * @param displayStyle the displayStyle to set
     */
    public void setDisplayStyle(String displayStyle) {
        this.displayStyle = displayStyle;
    }
    
    /**
     * 警告メッセージ
     * @return the warnMessage
     */
    public String getWarnMessage() {
        return warnMessage;
    }

    /**
     * 警告メッセージ
     * @param warnMessage the warnMessage to set
     */
    public void setWarnMessage(String warnMessage) {
        this.warnMessage = warnMessage;
    }
    
    /**
     * 一覧表示データ取得
     * @return 一覧表示データ
     */
    public List<GXHDO501DModel> getListData() {
        return listData;
    }
    
    /**
     * ﾛｯﾄNo
     * @return the lotNo
     */
    public String getLotNo() {
        return lotNo;
    }
    /**
     * ﾛｯﾄNo
     * @param lotNo the lotNo to set
     */
    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }
    /**
     * 種類
     * @return the syurui
     */
    public String getSyurui() {
        return syurui;
    }
    /**
     * 種類
     * @param syurui the syurui to set
     */
    public void setSyurui(String syurui) {
        this.syurui = syurui;
    }
    /**
     * ﾊﾟﾀｰﾝ
     * @return the pattern
     */
    public String getPattern() {
        return pattern;
    }
    /**
     * ﾊﾟﾀｰﾝ
     * @param pattern the pattern to set
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
    /**
     * ﾁｪｯｸﾊﾟﾀｰﾝリスト:表示可能ﾃﾞｰﾀ
     *
     * @return the cmbTyekkupatternData
     */
    public String[] getCmbTyekkupatternData() {
        return cmbTyekkupatternData;
    }
    /**
     * ﾁｪｯｸﾊﾟﾀｰﾝリスト:表示可能ﾃﾞｰﾀ
     *
     * @param cmbTyekkupatternData the cmbTyekkupatternData to set
     */
    public void setCmbTyekkupatternData(String[] cmbTyekkupatternData) {
        this.cmbTyekkupatternData = cmbTyekkupatternData;
    }
    
    //</editor-fold>
    /**
     * 画面起動時処理
     */
    @Override
    public void init() {

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
                
        String login_user_name = (String) session.getAttribute("login_user_name");

        if (null == login_user_name || "".equals(login_user_name)) {
            // セッションタイムアウト時はセッション情報を破棄してエラー画面に遷移
            try {
                session.invalidate();
                externalContext.redirect(externalContext.getRequestContextPath() + "/faces/timeout.xhtml?faces-redirect=true");
            } catch (Exception e) {
            }
            return;
        }

        // 設計No
        paramSekkeino = session.getAttribute("sekkeino") != null ? session.getAttribute("sekkeino").toString() : "";
        // LotNo
        paramLotno = session.getAttribute("lotNo") != null ? session.getAttribute("lotNo").toString() : "";
        // 種類
        paramSyurui = session.getAttribute("syurui") != null ? session.getAttribute("syurui").toString() : "";
        // 品名
        paramHinmei = session.getAttribute("hinmei") != null ? session.getAttribute("hinmei").toString() : "";
        // ﾊﾟﾀｰﾝ
        paramPattern = session.getAttribute("pattern") != null ? session.getAttribute("pattern").toString() : "";
        // 初期化データを取得
        selectListData(paramSekkeino,paramSyurui,paramHinmei,paramPattern);
        
        if(listData.isEmpty()) {
            // 検索結果が0件の場合エラー終了
            FacesMessage message = 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000210","規格情報"), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            displayStyle = "display:none;";
            return;
        }

        // 検索部のデータを設定
        this.setLotNo(paramLotno); //LotNo
        this.setSyurui(paramSyurui); //種類
        this.setPattern(paramPattern); //ﾊﾟﾀｰﾝ
        
        List<String> userGrpList = (List<String>) session.getAttribute("login_user_group");

        List<FXHDM01> menuListGXHDO101;
        // user-agent
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String uAgent = request.getHeader("user-agent");
        // model
        GetModel getModel = new GetModel(uAgent);
        String model = getModel.getModel();
        // userAgentでPC or タブレットを判定
        boolean isPC = false;
        if (model.equals("ie11")) {
            isPC = true;
        }
        
        try {
            QueryRunner queryRunnerDoc = new QueryRunner(dataSourceDocServer);
            menuListGXHDO101 = getMenuList(queryRunnerDoc, "GXHDO501D", userGrpList, isPC);

            menuListGXHDO101.stream().map((fxhdm01) -> {
                // 画面遷移パラメータをセッション変数に保持
                session.setAttribute("formId", "GXHDO501D");
                session.setAttribute("callerFormId", "GXHDO501D");
                session.setAttribute("formTitle", fxhdm01.getFormTitle());
                return fxhdm01;
            }).map((fxhdm01) -> {
                session.setAttribute("menuName", fxhdm01.getMenuName());
                return fxhdm01;
            }).map((fxhdm01) -> {
                session.setAttribute("menuComment", fxhdm01.getMenuComment());
                return fxhdm01;
            }).map((fxhdm01) -> {
                session.setAttribute("titleSetting", fxhdm01.getTitleSetting());
                return fxhdm01;
            }).map((fxhdm01) -> {
                session.setAttribute("formClassName", fxhdm01.getFormClassName());
                return fxhdm01;
            }).forEachOrdered((fxhdm01) -> {
                session.setAttribute("hyojiKensu", fxhdm01.getHyojiKensu());
            });
            
            //親の初期処理呼び出し
            super.init();

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("メニュー項目未登録", ex, LOGGER);
        }

    }

    /**
     * 一覧表示データ検索
     * @param sekkeino 設計No
     * @param syurui 種類
     * @param hinmei 品名
     * @param pattern ﾊﾟﾀｰﾝ
     */
    public void selectListData(String sekkeino,String syurui,String hinmei,String pattern) {
        try {
             QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
              String sql = "SELECT kouteimei"
                      + ", koumokumei"
                      + ", kanrikoumokumei"
                      + ", MAX(kikakuti) AS kikakuti"
                      + ", MAX(tyekkupattern) AS tyekkupattern"
                      + ", MAX(hyojunkikakuti) AS hyojunkikakuti"
                      + ", MAX(hyojuntyekkupattern) AS hyojuntyekkupattern"
                      + " FROM ("
                      + " SELECT yy.kouteimei"
                      + ", yy.koumokumei"
                      + ", yy.kanrikoumokumei"
                      + ", yy.kikakuti"
                      + ", yy.tyekkupattern"
                      + ", zz.kikakuti AS hyojunkikakuti"
                      + ", zz.tyekkupattern AS hyojuntyekkupattern"
                      + " FROM da_mksekkei xx"
                      + " JOIN da_mkjoken yy"
                      + " ON xx.sekkeino= yy.sekkeino"
                      + " LEFT JOIN da_mkhyojunjoken zz"
                      + " ON  xx.hinmei = zz.hinmei"
                      + " AND xx.pattern = zz.pattern"
                      + " AND xx.syurui = zz.syurui"
                      + " AND yy.kouteimei = zz.kouteimei"
                      + " AND yy.koumokumei = zz.koumokumei"
                      + " AND yy.kanrikoumokumei = zz.kanrikoumokumei"
                      + " WHERE xx.sekkeino= ? "
                      + " UNION ALL"
                      + " SELECT zz.kouteimei"
                      + ", zz.koumokumei"
                      + ", zz.kanrikoumokumei"
                      + ", NULL"
                      + ", NULL"
                      + ", zz.kikakuti AS hyojunkikakuti"
                      + ", zz.tyekkupattern AS hyojuntyekkupattern"
                      + " FROM da_mkhyojunjoken zz"
                      + " WHERE zz.pattern = ? "
                      + " AND zz.hinmei = ? "
                      + " AND zz.syurui = ? "
                      + " ) AS A"
                      + " GROUP BY kouteimei,koumokumei,kanrikoumokumei"
                      + " ORDER BY kouteimei,koumokumei,kanrikoumokumei";
                      
            // モデルクラスとのマッピング定義
            Map<String, String> mapping = new HashMap<>();
            mapping.put("kouteimei", "kouteimei"); //工程名
            mapping.put("koumokumei", "koumokumei"); //項目名
            mapping.put("kanrikoumokumei", "kanrikoumokumei"); //管理項目
            mapping.put("kikakuti", "kikakuti"); //規格値
            mapping.put("tyekkupattern", "tyekkupattern"); //ﾁｪｯｸﾊﾟﾀｰﾝ
            mapping.put("hyojunkikakuti", "hyojunkikakuti"); //標準規格値
            mapping.put("hyojuntyekkupattern","hyojuntyekkupattern"); //標準ﾁｪｯｸﾊﾟﾀｰﾝ

            BeanProcessor beanProcessor = new BeanProcessor(mapping);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<GXHDO501DModel>> beanHandler = 
                    new BeanListHandler<>(GXHDO501DModel.class, rowProcessor);
             // パラメータ設定
             List<Object> params = new ArrayList<>();
        
            params.addAll(Arrays.asList(sekkeino));
            params.addAll(Arrays.asList(pattern));
            params.addAll(Arrays.asList(hinmei));
            params.addAll(Arrays.asList(syurui));
            
            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            listData = queryRunner.query(sql, beanHandler, params.toArray());
            // ﾁｪｯｸﾊﾟﾀｰﾝコンボボックス設定
            cmbTyekkupatternData = new String[]{"±","～","≧","≦","MAX","MIN","="};
            
        } catch (SQLException ex) {
            listData = new ArrayList<>();
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
    
    }
    
    /**
     * 追加処理
     */
    public void addDataRow(){
        selectListData(paramSekkeino,paramSyurui,paramHinmei,paramPattern);
        
        if(listData.isEmpty()) {
            // 検索結果が0件の場合エラー終了
            FacesMessage message = 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000210","規格情報"), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            displayStyle = "display:none;";
            return;
        }
        GXHDO501DModel model = new GXHDO501DModel();
        model.setKouteimei("");
        model.setKoumokumei("");
        model.setKanrikoumokumei("");
        model.setKikakuti("");
        model.setTyekkupattern("");
        model.setHyojunkikakuti("");
        model.setHyojuntyekkupattern("");
        model.setAddFlg("1");
        listData.add(model);
    }
        
    /**
     * メニューリスト取得
     *
     * @param queryRunnerDoc データオブジェクト
     * @param listGamenID 画面IDリスト
     * @param userGrpList ユーザーグループリスト
     * @param isPC PC判定
     * @return メニューリスト
     * @throws SQLException 例外エラー
     */
    private List<FXHDM01> getMenuList(QueryRunner queryRunnerDoc, String listGamenID, List<String> userGrpList, boolean isPC) throws SQLException {
        List<FXHDM01> menuListData;
        String sql = "SELECT gamen_id,gamen_title,title_setting,menu_no,menu_name,link_char,menu_parameter,menu_comment,gamen_classname,hyouji_kensu "
                + "FROM fxhdm01 WHERE menu_group_id = 'qcdb_mksekkei' ";

        if (!listGamenID.isEmpty()) {
            sql += " AND ";
            sql += "gamen_id = ? ";
        }
        sql += " AND " + DBUtil.getInConditionPreparedStatement("user_role", userGrpList.size());

        if (!isPC) {
            sql += " AND pc_flg = '0'";
        }

        sql += " ORDER BY menu_no ASC ";

        Map<String, String> mapping = new HashMap<>();
        mapping.put("gamen_id", "formId");
        mapping.put("gamen_title", "formTitle");
        mapping.put("title_setting", "titleSetting");
        mapping.put("menu_no", "menuNo");
        mapping.put("menu_name", "menuName");
        mapping.put("link_char", "linkChar");
        mapping.put("menu_parameter", "menuParam");
        mapping.put("menu_comment", "menuComment");
        mapping.put("gamen_classname", "formClassName");
        mapping.put("hyouji_kensu", "hyojiKensu");

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<FXHDM01>> beanHandler = new BeanListHandler<>(FXHDM01.class, rowProcessor);

        List<Object> params3 = new ArrayList<>();
        params3.add(listGamenID);
        params3.addAll(userGrpList);

        if (listGamenID.isEmpty()) {
            DBUtil.outputSQLLog(sql, userGrpList.toArray(), LOGGER);
            menuListData = queryRunnerDoc.query(sql, beanHandler, userGrpList.toArray());
        } else {
            DBUtil.outputSQLLog(sql, params3.toArray(), LOGGER);
            menuListData = queryRunnerDoc.query(sql, beanHandler, params3.toArray());
        }        
        return menuListData;
    }
    
    /**
     * 修正処理確認
     */
    public void confirmUpdate() {
        
        // 入力チェック処理
        ValidateUtil validateUtil = new ValidateUtil();
        boolean chkFlg = true;
        //背景色をクリア
        for(int i = 0;i<this.listData.size();i++){
            listData.get(i).setKikakutibgcolor(NORMAL_COLOR);
            listData.get(i).setTyekkupatternbgcolor(NORMAL_COLOR);
            listData.get(i).setKouteimeibgcolor(NORMAL_COLOR);
            listData.get(i).setKoumokumeibgcolor(NORMAL_COLOR);
            listData.get(i).setKanrikoumokumeibgcolor(NORMAL_COLOR);
        }
        for(int i = 0;i<this.listData.size();i++){
            
            // 追加場合
            // 工程名入力チェック
            if (existError(validateUtil.checkC001(listData.get(i).getKouteimei(), "工程名"))) {
                listData.get(i).setKouteimeibgcolor(ERROR_COLOR);
                chkFlg = false;
                break;
            }
            // 桁数チェック
            if (existError(validateUtil.checkC103(listData.get(i).getKouteimei(), "工程名", 20))) {
                listData.get(i).setKouteimeibgcolor(ERROR_COLOR);
                chkFlg = false;
                break;
            }
            // 項目名入力チェック
            if (existError(validateUtil.checkC001(listData.get(i).getKoumokumei(), "項目名"))) {
                listData.get(i).setKoumokumeibgcolor(ERROR_COLOR);
                chkFlg = false;
                break;
            }
            // 桁数チェック
            if (existError(validateUtil.checkC103(listData.get(i).getKoumokumei(), "項目名", 20))) {
                listData.get(i).setKoumokumeibgcolor(ERROR_COLOR);
                chkFlg = false;
                break;
            }
            // 管理項目入力チェック
            if (existError(validateUtil.checkC001(listData.get(i).getKanrikoumokumei(), "管理項目"))) {
                listData.get(i).setKanrikoumokumeibgcolor(ERROR_COLOR);
                chkFlg = false;
                break;
            }
            // 桁数チェック
            if (existError(validateUtil.checkC103(listData.get(i).getKanrikoumokumei(), "管理項目", 20))) {
                listData.get(i).setKanrikoumokumeibgcolor(ERROR_COLOR);
                chkFlg = false;
                break;
            }

            // 規格値入力チェック
            if (existError(validateUtil.checkC001(listData.get(i).getKikakuti(), "規格値"))) {
                listData.get(i).setKikakutibgcolor(ERROR_COLOR);
                chkFlg = false;
                break;
            }
            // 桁数チェック
            if (existError(validateUtil.checkC103(listData.get(i).getKikakuti(), "規格値", 20))) {
                listData.get(i).setKikakutibgcolor(ERROR_COLOR);
                chkFlg = false;
                break;
            }
            
            // ﾁｪｯｸﾊﾟﾀｰﾝ入力チェック
            if (existError(validateUtil.checkC001(StringUtil.nullToBlank(listData.get(i).getTyekkupattern()), "ﾁｪｯｸﾊﾟﾀｰﾝ"))) {
                listData.get(i).setTyekkupatternbgcolor(ERROR_COLOR);
                chkFlg = false;
                break;
            }
            
            // 規格値 チェック
            if(!chkTyekkupatternError(listData.get(i).getKikakuti(),listData.get(i).getTyekkupattern())){
                listData.get(i).setKikakutibgcolor(ERROR_COLOR);
                FacesMessage message = 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000213","規格値",listData.get(i).getTyekkupattern()), null);
                FacesContext.getCurrentInstance().addMessage(null, message);
                chkFlg = false;
                break;
            }
        }
        if(!chkFlg){
            return;
        }

        //ユーザ認証処理
        checkUserAuth();
    }
    
    /**
     * 警告OK選択時処理
     * @throws java.sql.SQLException
     */
    public void processWarnOkClick() throws SQLException {
        switch (this.warnProcess) {
            case "update":
                doUpdate();
                break;
        }
    }
    
    /**
     * 正常な場合検修正理を実行する
     * @throws java.sql.SQLException
     */
    public void doUpdate() throws SQLException{
        // 削除件数取得
        long delCount = selectDelDataCount(paramSekkeino);
        
        QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
        Connection conDoc = null;
        try{
            conDoc = DBUtil.transactionStart(queryRunner.getDataSource().getConnection());
            // 規格情報のﾃﾞｰﾀ削除を行う
            deleteFromDaMkjoken(queryRunner, conDoc,paramSekkeino);

            // 規格情報にﾃﾞｰﾀ登録を行う
            insertDaMkjoken(queryRunner, conDoc,listData,paramSekkeino);

            List<String> messageList = new ArrayList<>();
            messageList.add("規格情報を"+delCount+"件削除しました。");
            messageList.add("規格情報を"+listData.size()+"件登録しました。");
            ResultMessage beanResultMessage = (ResultMessage) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_RESULT_MESSAGE);
            beanResultMessage.setResultMessageList(messageList);
            RequestContext.getCurrentInstance().execute("PF('W_dlg_resultMessage').show();");

            selectListData(paramSekkeino,paramSyurui,paramHinmei,paramPattern);
        
            DbUtils.commitAndCloseQuietly(conDoc);
            
        } catch (SQLException e) {
             // コネクションロールバック処理
            DBUtil.rollbackConnection(conDoc, LOGGER);

            FacesMessage message =
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "実行時エラー", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        } 
    }
    
    /**
     * 前工程規格情報(da_mkjoken)登録処理
     * 
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param conDoc コネクション
     * @param listData 画面のデータ
     * @param paramSekkeino 設計No
     * @throws SQLException 例外エラー
     */
    private void insertDaMkjoken(QueryRunner queryRunnerDoc, Connection conDoc,List<GXHDO501DModel> listData, String paramSekkeino) throws SQLException{
        
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        for(int i=0;i<listData.size();i++){
            String sql = "INSERT INTO da_mkjoken ("
                + "sekkeino,kouteimei,koumokumei,kanrikoumokumei,kikakuti,tyekkupattern,tourokunichiji"
                + ") VALUES ("
                + "?, ?, ?, ?, ?, ?, ?) ";
            
            List<Object> params = new ArrayList<>();
            params.add(paramSekkeino); //設計No
            params.add(listData.get(i).getKouteimei()); //工程名
            params.add(listData.get(i).getKoumokumei()); //項目名
            params.add(listData.get(i).getKanrikoumokumei()); //管理項目名
            params.add(listData.get(i).getKikakuti().replace((char)12288, ' ').trim()); //規格値
            params.add(listData.get(i).getTyekkupattern()); //'ﾁｪｯｸﾊﾟﾀｰﾝ
            params.add(df.format(new Date()));//登録日
            
            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            queryRunnerDoc.update(conDoc, sql, params.toArray());
        }
    }
      
    /**
     * 削除
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param conDoc コネクション
     * @param paramSekkeino 設計No
     * @throws java.sql.SQLException
     */
    public void deleteFromDaMkjoken(QueryRunner queryRunnerDoc, Connection conDoc,String paramSekkeino) throws SQLException {
        String sql = "DELETE FROM da_mkjoken WHERE sekkeino = ? ";
        
        List<Object> params = new ArrayList<>();
        //検索条件設定
        params.add(paramSekkeino);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerDoc.update(conDoc, sql, params.toArray());
    }
            
            
    /**
     * 削除件数取得
     * @param paramSekkeino 設計No
     * @return 検索結果件数
     */
    public long selectDelDataCount(String paramSekkeino) {
         long count;
          try {
            QueryRunner queryRunner = new QueryRunner(dataSourceQcdb);
            String sql = "SELECT COUNT(sekkeino) AS CNT "
                    + "FROM da_mkjoken "
                    + " WHERE ( sekkeino = ?) ";

            // パラメータ設定
            List<Object> params = new ArrayList<>();
            params.addAll(Arrays.asList(paramSekkeino));

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            Map result = queryRunner.query(sql, new MapHandler(), params.toArray());
            count = (long) result.get("CNT");
        } catch (SQLException ex) {
            count = 0;
            listData = new ArrayList<>();
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
        return count;
    }

    /**
     * エラーチェック：
     * エラーが存在する場合ポップアップ用メッセージをセットする
     * @param errorMessage エラーメッセージ
     * @return エラーが存在する場合true
     */
    private boolean existError(String errorMessage) {
        if (StringUtil.isEmpty(errorMessage)) {
            return false;
        }
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
        return true;
    }

    /**
     * Numﾁｪｯｸ
     * @param kikakutiStr1 規格値前半部分
     * @param kikakutiStr2 規格値後半部分
     * 
    */
    private boolean chkNumTypeError(String kikakutiStr1, String kikakutiStr2) {
        Pattern patternChk = Pattern.compile("[0-9]*\\.?[0-9]+");
        Matcher isNum = patternChk.matcher(kikakutiStr1);
        if( !isNum.matches() ){
            return false;
        }else{
            Matcher isNum1 = patternChk.matcher(kikakutiStr2);
            return isNum1.matches();
        }
    }
    
    /**
     * Numﾁｪｯｸ [≧],[≦]
     * @param tmpStr [≧],[≦]
     * @param str 規格値
     * @param arrKikakuti 規格値array
     * 
    */
    private boolean chkKikakuchiError(String tmpStr,String str,String [] arrKikakuti){
        Pattern patternChk = Pattern.compile("[0-9]*\\.?[0-9]+");
         if((tmpStr.equals(str.substring(0, 1)))){
            if(!"".equals(arrKikakuti[1])){
                Matcher isNum0 = patternChk.matcher(arrKikakuti[1]);
                return isNum0.matches();
            }else{
                 return false;
            }
        }else{
            if((tmpStr.equals(str.substring(str.length()-1)))){
                if(!"".equals(arrKikakuti[0])){
                    Matcher isNum0 = patternChk.matcher(arrKikakuti[0]);
                    return isNum0.matches();
                }else{
                    return false;
                }
            }else{
                    return false;
                }
            }
    }

    
    /**
     * 規格値 チェック
     * @param kikakuti 規格値
     * @param tyekkupattern ﾁｪｯｸﾊﾟﾀｰﾝ
     * 
    */
    private boolean chkTyekkupatternError(String strKikakuti, String strTyekkupattern) {
       
        Pattern patternChk = Pattern.compile("[0-9]*\\.?[0-9]+");
        
        if(!strKikakuti.contains(strTyekkupattern)){
            return false;
        }
        //半角・全角ｽﾍﾟｰｽを削除
        String str=strKikakuti.replace((char)12288, ' ');
        String  arrKikakuti []= str.trim().split(strTyekkupattern);
        if(arrKikakuti.length>2 || arrKikakuti.length==0){
            return false;
        }
        
        //"±","～","≧","≦","MAX","MIN","="
        switch (strTyekkupattern) { 
            case "±":
            case "～":
                //[±],[～]の前後に数字が入っていない場合ｴﾗｰ
                if(arrKikakuti.length!=2){
                    return false;
                }else{
                    if(("".equals(arrKikakuti[0])) || ("".contains(arrKikakuti[1]))){
                        return false;
                    }else{
                        return chkNumTypeError(arrKikakuti[0],arrKikakuti[1]);
                    }
                }
            case "≧":   
               return chkKikakuchiError("≧",str.trim(),arrKikakuti) ;
            case "≦":
                return chkKikakuchiError("≦",str.trim(),arrKikakuti) ;
            case "MAX":
                //[MAX]の後に数字が入っていない場合
                if( ("MAX".equals(str.trim().substring(0, 3)))){
                    Matcher isNum0 = patternChk.matcher(arrKikakuti[1]);
                    return isNum0.matches();
                }else{
                    return false;
                }         
            case "MIN":
                //[MIN]の後に数字が入っていない場合
                if(("MIN".equals(str.trim().substring(0, 3)))){
                    Matcher isNum0 = patternChk.matcher(arrKikakuti[1]);
                    return isNum0.matches();
                }else{
                    return false;
                }            
            case "=":   
                //[=]の後に数字が入っていない場合
                if(("=".equals(str.trim().substring(0, 1)))){
                    Matcher isNum0 = patternChk.matcher(arrKikakuti[1]);
                    return isNum0.matches();
                }else{
                    return false;
                }
            default:
                return false;
        }
    }
    
    /**
     * ユーザ認証処理
     */
    public void checkUserAuth() {

        // ユーザ認証用のパラメータをセットする。
        processData.setUserAuthParam(USER_AUTH_UPDATE_PARAM);

        // ログインユーザーの権限チェックを実施する
        if (!this.userAuthLogin()) {
            // 権限がない場合はユーザー認証画面を表示
            RequestContext context = RequestContext.getCurrentInstance();
            context.addCallbackParam("firstParam", "auth");
        }else{
            RequestContext context = RequestContext.getCurrentInstance();
            context.addCallbackParam("firstParam", "warning");
            this.warnMessage = "修正します。よろしいですか？";
            this.warnProcess = "update";
        }
    }
    
    /**
     * ログインユーザー権限チェック
     *
     * @return 権限がある場合true
     */
    private boolean userAuthLogin() {
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
     * ユーザーグループマスタ検索
     *
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
            return ((List<Map>) fxhbm02).stream()
                    .map(n -> n.get("usergroup").toString())
                    .filter(n -> !StringUtil.isEmpty(n))
                    .collect(Collectors.toList());

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            return new ArrayList<>();
        }
    }
}
