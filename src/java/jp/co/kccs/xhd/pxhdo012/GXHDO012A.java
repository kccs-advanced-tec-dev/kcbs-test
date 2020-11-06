/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo012;

import static com.sun.xml.wss.swa.MimeConstants.CHARSET;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import jp.co.kccs.xhd.GetModel;
import jp.co.kccs.xhd.db.model.FXHDM01;
import jp.co.kccs.xhd.util.DBUtil;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.StringUtil;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;

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
 * 変更日	2018/11/13<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	ﾛｯﾄｶｰﾄﾞ電子化対応<br>
 * <br>
 * ===============================================================================<br>
 */

/**
 * GXHDO012A(メニュー画面)
 * 
 * @author KCCS D.Yanagida
 * @since 2018/05/06
 */
@Named
@ViewScoped
public class GXHDO012A implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(GXHDO012A.class.getName());

    private static final int LOTNO_BYTE = 14;
    private static final String CHARSET = "MS932";
    private static final String PRINT_URL = "/secure/pxhdo301/gxhdo301a002.xhtml?faces-redirect=true";
    private static final String SEKISO_URL = "/secure/pxhdo301/gxhdo301a003.xhtml?faces-redirect=true";
    private static final String RHAPS_URL = "/secure/pxhdo301/gxhdo301a001.xhtml?faces-redirect=true";

    /**
     * ロットNo
     */
    private String lotNo;

    /**
     * 設計仕様検索の表示・非表示
     */    
    private String display;

    /**
     * DataSource
     */
    @Resource(mappedName = "jdbc/DocumentServer")
    private transient DataSource dataSource;

    /**
     * DataSource(QCDB)
     */
    @Resource(mappedName = "jdbc/qcdb")
    private transient DataSource dataSourceQcdb;

    /**
     * メニュー項目
     */
    private List<FXHDM01> menuList;
    /**
     * コンストラクタ
     */
    public GXHDO012A() {
    }
    
    /**
     * メニュー一覧取得処理
     * @return メニュー一覧データ
     */
    public List<FXHDM01> getMenuList() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        List<String> userGrpList = (List<String>)session.getAttribute("login_user_group");
        
        menuList = new ArrayList<>();
        session.setAttribute("lotNo", "");
        
        // user-agent
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String uAgent = request.getHeader("user-agent");
        // model
        GetModel getModel = new GetModel(uAgent);
        String model = getModel.getModel();

        // 設計仕様非表示
        setDisplay("none");
        
        // userAgentでPC or タブレットを判定
        boolean isPC = false;
        if (model.equals("ie11")) {
            isPC = true;
        }
        
        if (0 == userGrpList.size()) {
            // ユーザーグループ未登録の場合、ブランクメニューを表示する
            return menuList;
        } else {
            // 設計仕様の表示・表示
            if (userGrpList.contains("search_sekkeisiyo")) {
                setDisplay("display");
            }
            // ユーザーグループでメニューマスタを検索
            try {
                QueryRunner queryRunner = new QueryRunner(dataSource);
                String sql = "SELECT gamen_id, gamen_title, title_setting, link_char, menu_name, menu_comment, menu_parameter, gamen_classname, hyouji_kensu FROM fxhdm01 "
                           + "WHERE menu_group_id = 'qcdb_mainMenu' AND " 
                        + DBUtil.getInConditionPreparedStatement("user_role", userGrpList.size());
                
                if (!isPC) {
                    sql += " AND pc_flg = '0'";
                }
                
                sql += " ORDER BY menu_no ";

                Map<String, String> mapping = new HashMap<>();
                mapping.put("gamen_id", "formId");
                mapping.put("gamen_title", "formTitle");
                mapping.put("title_setting", "titleSetting");
                mapping.put("menu_name", "menuName");
                mapping.put("link_char", "linkChar");
                mapping.put("menu_parameter", "menuParam");
                mapping.put("menu_comment", "menuComment");
                mapping.put("gamen_classname", "formClassName");
                mapping.put("hyouji_kensu", "hyojiKensu");

                BeanProcessor beanProcessor = new BeanProcessor(mapping);
                RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
                ResultSetHandler<List<FXHDM01>> beanHandler = new BeanListHandler<>(FXHDM01.class, rowProcessor);
                
                DBUtil.outputSQLLog(sql, userGrpList.toArray(), LOGGER);
                menuList = queryRunner.query(sql, beanHandler, userGrpList.toArray());    
            } catch (SQLException ex) {
                ErrUtil.outputErrorLog("メニュー項目未登録", ex, LOGGER);
                menuList = new ArrayList<>();
            }
        }
        return menuList;
    }
    
    /**
     * 品質DBメンテナンス画面open
     * @param rowData 選択行のデータ
     * @return 遷移先画面
     */
    public String openXhdForm(FXHDM01 rowData) {
        return rowData.getLinkChar() + "?faces-redirect=true";
    }
    
    /**
     * ログアウトします。
     * 
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
     * 段取用設計仕様印刷検索
     * 
     * @return 遷移先URL文字列
     */
    public String sekkeiSerchPrint() {

        try {
            // セッション情報
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            // ロットNoのチェック
            if (LOTNO_BYTE != StringUtil.getByte(getLotNo(), CHARSET, LOGGER)) {
                // エラーメッセージ
                addErrorMessage(MessageUtil.getMessage("XHD-000004", "ロットNo", LOTNO_BYTE));
                return null;
            }
            // プロセスから遷移先の画面を判定する
            if (processSearch()) {
                session.setAttribute("lotNo", getLotNo());
                return RHAPS_URL;
            }
            session.setAttribute("lotNo", getLotNo());
            return PRINT_URL;
        } catch (SQLException e) {
            // エラーメッセージ
            addErrorMessage("実行時エラー");
            return null;
        }
    }

    /**
     * 段取用設計仕様積層検索
     * 
     * @return 遷移先URL文字列
     */
    public String sekkeiSerchSekiso() {

        try {
            // セッション情報
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            // ロットNoのチェック
            if (LOTNO_BYTE != StringUtil.getByte(getLotNo(), CHARSET, LOGGER)) {
                // エラーメッセージ
                addErrorMessage(MessageUtil.getMessage("XHD-000004", "ロットNo", LOTNO_BYTE));
                return null;
            }
            // プロセスから遷移先の画面を判定する
            if (processSearch()) {
                session.setAttribute("lotNo", getLotNo());
                return RHAPS_URL;
            }
            session.setAttribute("lotNo", getLotNo());
            return SEKISO_URL;
        } catch (SQLException e) {
            // エラーメッセージ
            addErrorMessage("実行時エラー");
            return null;
        }
    }


    /**
     * 段取用設計仕様印刷検索
     * 
     * @return 遷移先URL文字列
     * @throws java.sql.SQLException
     */
    public boolean processSearch() throws SQLException {
        
        boolean res = false;
        
        // ﾌﾟﾘﾝﾄﾌｫｰﾏｯﾄの検索
        QueryRunner queryRunnerQcdb = new QueryRunner(dataSourceQcdb);
        // 検索用ロットNo
        String lotNo1 = getLotNo().substring(0, 3);
        String lotNo2 = getLotNo().substring(3, 11);
        
        // SQL生成
        String sql = "SELECT PRINTFMT AS printfmt"
                + " FROM da_sekkei "
                + "WHERE KOJYO = ? "
                + "AND LOTNO = ? "
                + "AND EDABAN = '001';";
        
        // パラメータの設定
        List<Object> params = new ArrayList<>();
        params.add(lotNo1);
        params.add(lotNo2);
        
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map sekkeiData = queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());

        if (sekkeiData == null || sekkeiData.isEmpty()) {
            // ｴﾗｰﾒｯｾｰｼﾞを画面に表示する
            addErrorMessage(MessageUtil.getMessage("XHD-000014"));
            return res;
        }
        String printFmt = StringUtil.nullToBlank(sekkeiData.get("printfmt"));
        // 画面遷移
        if (printFmt.equals("LotCardForm_HAPS_OLD.xlsm") 
                || printFmt.equals("LotCardForm_RHAPS.xlsm")) {

            // RHAPS用の画面に遷移
            res = true;
        }

        return res;        
    }
    
    /**
     * //エラーメッセージ追加処理
     *
     * @param errorMessage エラーメッセージ
     */
    private void addErrorMessage(String errorMessage) {
        FacesMessage message
                = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    /**
     * ロットNo
     *
     * @return the lotNo
     */
    public String getLotNo() {
        return lotNo;
    }

    /**
     * ロットNo
     *
     * @param lotNo
     */
    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    /**
     * 設計仕様検索の表示・非表示
     * @return the display
     */
    public String getDisplay() {
        return display;
    }

    /**
     * 設計仕様検索の表示・非表示
     * @param display the display to set
     */
    public void setDisplay(String display) {
        this.display = display;
    }
}
